/*
 *  Copyright 2025 Better Orange IR & HV AG
 *
 *  Licensed under the Meetingbase License (the "License");
 *  Vou may not use this file except in compliance with the License.
 *  You may obtain a copy of the License in the root directory (MEETINGBASE_LICENSE).
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package de.meetingapps.meetingportal.meetComAllg;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.LinkedList;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**Div. Verwaltungsroutinen für Directories und Files*/
public class CaDateiVerwaltung {

    public final static Charset ibmCharset = Charset.forName("IBM850");
    public final static Charset isoCharset = Charset.forName("ISO-8859-1");
    public final static Charset utfCharset = Charset.forName("UTF-8");

    public Charset bestCharset = null;

    public String[] leseDateienInVerzeichnis(String pVerzeichnis) {
        File verzeichnis = new File(pVerzeichnis);
        File[] dateien = verzeichnis.listFiles();

        if (dateien == null) {
            CaBug.drucke("001 - pVerzeichnis=" + pVerzeichnis);
            return null;
        }
        int anz = dateien.length;
        String[] dateiNamen = new String[anz];
        for (int i = 0; i < anz; i++) {
            dateiNamen[i] = dateien[i].getName();
        }
        return dateiNamen;
    }

    public LinkedList<String> convertDateiToList(File file, Charset cs, int call, Charset bestCharset, int count) {

        LinkedList<String> list = new LinkedList<>();
        Charset currentSet = cs;
        Boolean check = false;

        int ibmCount = 0;
        int isoCount = 0;
        int utfCount = 0;

        if (call == 1) {
            if (isContainBOM(file)) {
                file = removeBOM(file);
                if (file == null) {
                    return null;
                }
            }
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file, currentSet))) {
            String line = null;
            while ((line = reader.readLine()) != null) {

                while (line.length() < 500)
                    line += " ";

                ibmCount += new String(line.getBytes(ibmCharset), ibmCharset).equals(line) ? 1 : 0;

                isoCount += new String(line.getBytes(isoCharset), isoCharset).equals(line) ? 1 : 0;

                utfCount += new String(line.getBytes(utfCharset), utfCharset).equals(line) ? 1 : 0;

                list.add(line);
            }

            if (count < ibmCount + isoCount + utfCount) {
                this.bestCharset = currentSet;
                count = ibmCount + isoCount + utfCount;
            }

            System.out.println(currentSet.name());

            if (ibmCount == utfCount && ibmCount == isoCount)
                check = true;

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!check && call == 3) {
            return null;
        } else {
            call++;
        }

        currentSet = call == 2 ? utfCharset : ibmCharset;

        return check ? list : convertDateiToList(file, currentSet, call, bestCharset, count);
    }

    public String createErrorList(File file, Charset cs) {

        LinkedList<String> ibmList = new LinkedList<>();
        LinkedList<String> isoList = new LinkedList<>();
        LinkedList<String> utfList = new LinkedList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(file, cs))) {
            String line = null;
            int i = 1;
            while ((line = reader.readLine()) != null) {

                while (line.length() < 500)
                    line += " ";

                if (!new String(line.getBytes(ibmCharset), ibmCharset).equals(line)) {
                    ibmList.add(i + "");
                }

                if (!new String(line.getBytes(isoCharset), isoCharset).equals(line)) {
                    isoList.add(i + "");
                }

                if (!new String(line.getBytes(utfCharset), utfCharset).equals(line)) {
                    utfList.add(i + "");
                }

                i++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //        System.out.println("ibm " + ibmList.size());
        //        System.out.println("iso " + isoList.size());
        //        System.out.println("utf " + utfList.size());

        LinkedList<String> tmpList = ibmList.size() > isoList.size()
                ? (ibmList.size() > utfList.size() ? ibmList : utfList)
                : (isoList.size() > utfList.size() ? isoList : utfList);

        return Arrays.toString(tmpList.toArray());
    }

    public LinkedList<String[]> convertExceltoList(File file) {

        LinkedList<String[]> linkedList = new LinkedList<>();

        try {
            InputStream f = new FileInputStream(file);

            try (Workbook book = file.getName().endsWith("xls") ? new HSSFWorkbook(f) : new XSSFWorkbook(f)) {

                Sheet sheet = book.getSheetAt(0);

                int arrSize = 0;
                int rowNum = 0;

                for (Row row : sheet) {
                    //                  Prueft ob eine Leerzeile vorhanden > Leerzeile = break
                    if (rowNum == row.getRowNum()) {
                        if (row.getRowNum() == 0)
                            arrSize = row.getLastCellNum();

                        String[] arr = new String[arrSize];
                        for (Cell cell : row) {
                            switch (cell.getCellType()) {
                            case STRING:
                                arr[cell.getColumnIndex()] = cell.getRichStringCellValue().getString()
                                        .replaceAll("\\s+", " ");
                                break;
                            case NUMERIC:
                                arr[cell.getColumnIndex()] = String.valueOf((int) cell.getNumericCellValue());
                                break;
                            default:
                                arr[cell.getColumnIndex()] = "";
                                break;
                            }
                        }
                        if (!checkEmptyArray(arr)) {
                            linkedList.add(arr);
                            rowNum++;
                        }
                    } else {
                        break;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Diese Excel wird nicht unterstützt");
            System.out.println(e.getMessage());
            return null;
        }
        return linkedList;
    }

    // Prüfen ob Array nur leere Felder enthählt, sobald ein Feld gefüllt > false
    private Boolean checkEmptyArray(String[] arr) {

        for (String str : arr) {
            if (str != null && !str.isBlank()) {
                return false;
            }
        }
        return true;
    }

    /**Bsp. für Filter:
         FilenameFilter filter=null;
         filter = new FilenameFilter() {
            @Override
            public boolean accept(File f, String name) {
                return name.endsWith(".pdf");
            }
        };
    */
    public String[] leseDateienInVerzeichnis(String pVerzeichnis, FilenameFilter filter) {
        File verzeichnis = new File(pVerzeichnis);
        File[] dateien = verzeichnis.listFiles(filter);

        if (dateien == null) {
            CaBug.drucke("001 - pVerzeichnis=" + pVerzeichnis);
            return null;
        }
        int anz = dateien.length;
        String[] dateiNamen = new String[anz];
        for (int i = 0; i < anz; i++) {
            dateiNamen[i] = dateien[i].getName();
        }

        return dateiNamen;
    }

    public void deleteDatei(String pDateiname) {
        System.out.println("DeleteDatei=" + pDateiname);
        File file = new File(pDateiname);
        if (file.exists()) {
            file.delete();
        }
    }

    public void copyDatei(String pDateinameQuelle, String pDateinameZiel) {

        File source = new File(pDateinameQuelle);
        File dest = new File(pDateinameZiel);

        InputStream is = null;
        OutputStream os = null;
        try {
            try {
                is = new FileInputStream(source);
                if (is != null) {
                    System.out.println("Datei kopieren");
                    os = new FileOutputStream(dest);
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = is.read(buffer)) > 0) {
                        os.write(buffer, 0, length);
                    }
                }
            } catch (FileNotFoundException e) {
                CaBug.drucke("CaDateiVerwaltung.copyDatei 001");
                //				e.printStackTrace();
            } catch (IOException e) {
                CaBug.drucke("CaDateiVerwaltung.copyDatei 002");
                //				e.printStackTrace();
            }
        } finally {
            try {
                if (is != null) {
                    is.close();
                    os.close();
                }
            } catch (IOException e) {
                CaBug.drucke("CaDateiVerwaltung.copyDatei 003");
                //				e.printStackTrace();
            }
        }
    }

    public static boolean fileExist(String pVerzeichnis) {
        return new File(pVerzeichnis).exists();
    }

    /**Legt alle Verzeichnisse im Pfad an, soweit noch nicht vorhanden
     * -1 => Fehler beim Anlegen*/
    public static boolean createDirectory(String pVerzeichnis) {
        return new File(pVerzeichnis).mkdirs();
    }
    
    public static void openFileExplorer(String pVerzeichnis) {
        try {
            Desktop.getDesktop().open(new File(pVerzeichnis));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isContainBOM(File f) {

        boolean result = false;

        byte[] bom = new byte[3];
        try (InputStream is = new FileInputStream(f)) {

            // read first 3 bytes of a file.
            is.read(bom);

            // BOM encoded as ef bb bf
            String content = new String(Hex.encodeHex(bom));
            if ("efbbbf".equalsIgnoreCase(content)) {
                result = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public File removeBOM(File f) {

        if (isContainBOM(f)) {

            try {

                byte[] bytes = Files.readAllBytes(f.toPath());

                ByteBuffer bb = ByteBuffer.wrap(bytes);

                System.out.println("Found BOM!");

                byte[] bom = new byte[3];
                // get the first 3 bytes
                bb.get(bom, 0, bom.length);

                // remaining
                byte[] contentAfterFirst3Bytes = new byte[bytes.length - 3];
                bb.get(contentAfterFirst3Bytes, 0, contentAfterFirst3Bytes.length);

                System.out.println("Remove the first 3 bytes, and overwrite the file!");

                final String fileName = FilenameUtils.getBaseName(f.getName());
                final File newFile = new File(f.getAbsolutePath().replace(fileName, fileName + "_UTF8"));

                // override the same path
                return Files.write(newFile.toPath(), contentAfterFirst3Bytes).toFile();

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } else {
            System.out.println("This file doesn't contains UTF-8 BOM!");
            return f;
        }
    }
}
