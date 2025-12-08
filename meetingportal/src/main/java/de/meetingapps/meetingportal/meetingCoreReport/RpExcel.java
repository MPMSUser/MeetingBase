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
package de.meetingapps.meetingportal.meetingCoreReport;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaDatumZeit;
import de.meetingapps.meetingportal.meetComDb.DbBundle;

public class RpExcel {

    private String[] header = null;

    /*
     * Eine Liste fuer jeden Datensatz 
     * 
     */
    public List<List<RpExcelVariablen>> content = new ArrayList<>();

    public List<RpExcelVariablen> formulas = new ArrayList<>();

//    Variablen

//  Calibri, 11
    final static public int TEXT = 1;
//  Calibri, 14, FETT
    final static public int TEXT_BOLD = 2;
//  ohne 1.000 Trennzeichen
    final static public int NUMBER = 3;
//  1.000 Trennzeichen
    final static public int NUMBER_FORMAT = 4;
//  Calibri, 14, FETT, 1.000 Trennzeichen
    final static public int NUMBER_FORMAT_BOLD = 5;

    final static public String SUMME = "SUMME";

    private CellStyle styleText = null;
    private CellStyle styleTextBold = null;

    private CellStyle styleNumber = null;
    private CellStyle styleNumberFormat = null;
    private CellStyle styleNumberFormatBold = null;
    
    public String dateiName = "";

    public RpExcel() {

    }

    public void create(DbBundle dbBundle, String dateiName) {
        
        createFileName(dbBundle, dateiName);

        try (Workbook wb = new XSSFWorkbook()) {
            Sheet s = wb.createSheet("Meldestand");
            Row r = null;

            final Font f1 = createFont(wb);
            final Font f2 = createErgFont(wb);

            styleText = createBasicStyle(wb, f1);
            styleTextBold = createBasicStyle(wb, f2);

            styleNumber = createNumericStyle(wb, f1, false);
            styleNumberFormat = createNumericStyle(wb, f1, true);
            styleNumberFormatBold = createNumericStyle(wb, f2, true);

            settings(wb, s);

            createHeader(s, header, styleText);

            int rowNum = 1;

            for (var meldung : content) {

                r = s.createRow(rowNum++);

                for (RpExcelVariablen var : meldung)
                    setValue(r, var);

            }

            if (r != null) {
                for (int i = 0; i < r.getLastCellNum(); i++)
                    s.autoSizeColumn(i);

                if (!formulas.isEmpty()) {
                    r = s.createRow(++rowNum);

                    for (RpExcelVariablen var : formulas) {

                        setValue(r, var);

                        if (var.length > 0)
                            calcWidth(s, var.column, var.length, var.format);

                    }
                }
            }

//          Druckbereich festlegen
            wb.setPrintArea(0, "A1:" + columnToChar(s.getRow(0).getLastCellNum() - 1) + (rowNum + 1));

            FileOutputStream out = new FileOutputStream(this.dateiName);

            wb.write(out);
            out.close();
        } catch (IOException e) {
            CaBug.drucke("001");
            e.printStackTrace();
        }

    }
    
    private void createFileName(DbBundle pDbBundle, String name) {
        
        dateiName = pDbBundle.lieferePfadMeetingOutput()
        + "\\" + name + "M" + pDbBundle.getMandantString() + "_"
        + CaDatumZeit.DatumZeitStringFuerDateiname() + ".xlsx";
        
    }
    
    private Font createFont(Workbook wb) {
        Font f = wb.createFont();
        f.setFontName("Calibri");
        f.setFontHeightInPoints((short) 11);

        return f;
    }

    private Font createErgFont(Workbook wb) {
        Font f = wb.createFont();
        f.setFontName("Calibri");
        f.setFontHeightInPoints((short) 14);
        f.setBold(true);

        return f;
    }

    private CellStyle createBasicStyle(Workbook wb, Font f) {
        CellStyle cs = wb.createCellStyle();
        cs.setFont(f);

        return cs;
    }

    private CellStyle createNumericStyle(Workbook wb, Font f, Boolean seperator) {
        CellStyle cs = wb.createCellStyle();
        CreationHelper createHelper = wb.getCreationHelper();
        cs.setDataFormat(createHelper.createDataFormat().getFormat(seperator ? "#,##0" : "0"));
        cs.setFont(f);

        return cs;
    }

    /*
     * Erstellt die 1. Zeile und fuellt die es mit Uberschriften
     */
    private void createHeader(Sheet s, String[] header, CellStyle style) {
        Row headline = s.createRow(0);

        for (int i = 0; i < header.length; i++)
            setStringCellValue(headline, header[i], i, style);
    }

    private void setStringCellValue(Row row, String value, int cellNum, CellStyle style) {
        Cell cell = row.createCell(cellNum);
        cell.setCellValue(value);
        cell.setCellStyle(style);
    }

    /*
     * Spaltenbreite der Summe berechnen
     */
    private void calcWidth(Sheet s, int col, int length, int format) {
        if (format == NUMBER_FORMAT_BOLD)
            s.setColumnWidth(col, length * 470);
        else
            s.autoSizeColumn(col);
    }

    /*
     * Wandelt Spaltenzahl in char um 1 > a
     */
    private char columnToChar(int col) {
        return (char) (col + 65);
    }

    /*
     * Fuelle Ueberschriften
     */
    public void fillHeader(String[] srrArray) {
        header = srrArray;
    }

    /*
     * Unterstuetzt wird aktuell String, Integer, Long
     */
    private void setValue(Row row, RpExcelVariablen var) {

        final Cell cell = row.createCell(var.column);
        final Object value = var.obj;

//      Formel?
        if (String.valueOf(value).equals(SUMME))
            cell.setCellFormula(formulaString(row, var));
        else if (value instanceof Integer)
            cell.setCellValue((Integer) value);
        else if (value instanceof Long)
            cell.setCellValue((long) value);
        else
            cell.setCellValue(String.valueOf(value));

        cell.setCellStyle(getStyle(var.format));
    }

    /*
     * Erstellt die Summenformel für die uebergebene Spalte
     * Bisher nur Summe realisiert
     */
    private String formulaString(Row r, RpExcelVariablen var) {
        final char col = columnToChar(var.column);
        return "SUM(" + col + "2:" + col + (r.getRowNum() - 1) + ")";
    }

    private void settings(Workbook wb, Sheet s) {
//      Queerformat
        s.getPrintSetup().setLandscape(true);

//      Spalten werden fuer den Druck auf ggf. verkleinert
        s.setFitToPage(true);
        s.getPrintSetup().setFitWidth((short) 1);
        s.getPrintSetup().setFitHeight((short) 0);

//      Erste Zeile fuer den Druck wiederholen
        s.setRepeatingRows(CellRangeAddress.valueOf("1:1"));
        s.createFreezePane(0, 1);
    }

    private CellStyle getStyle(int constant) {

        if (constant == TEXT_BOLD)
            return styleTextBold;
        else if (constant == NUMBER)
            return styleNumber;
        else if (constant == NUMBER_FORMAT)
            return styleNumberFormat;
        else if (constant == NUMBER_FORMAT_BOLD)
            return styleNumberFormatBold;
        else
            return styleText;
    }
}
