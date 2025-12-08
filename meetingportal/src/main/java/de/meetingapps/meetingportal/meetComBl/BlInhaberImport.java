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
package de.meetingapps.meetingportal.meetComBl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaDateiVerwaltung;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEh.EhGattung;
import de.meetingapps.meetingportal.meetComEntities.EclAbstimmung;
import de.meetingapps.meetingportal.meetComEntities.EclAktienregister;
import de.meetingapps.meetingportal.meetComEntities.EclInhaberImportAnmeldedaten;
import de.meetingapps.meetingportal.meetComEntities.EclImportAdresse;
import de.meetingapps.meetingportal.meetComEntities.EclImportProfil;
import de.meetingapps.meetingportal.meetComEntities.EclImportProtokoll;
import de.meetingapps.meetingportal.meetComEntities.EclIsin;
import de.meetingapps.meetingportal.meetComKonst.KonstKartenklasse;
import de.meetingapps.meetingportal.meetComStub.StubAbstimmungen;
import de.meetingapps.meetingportal.meetComStub.StubRoot;
import de.meetingapps.meetingportal.meetComStub.WEStubBlInhaberImport;
import de.meetingapps.meetingportal.meetComStub.WEStubBlInhaberImportRC;
import de.meetingapps.meetingportal.meetComWE.WELoginVerify;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class BlInhaberImport extends StubRoot {

    public List<EclInhaberImportAnmeldedaten> InhaberImportList = null;
    public List<EclImportProfil> InhaberImportProfiles = null;
    public List<EclImportProtokoll> importProtokoll = null;
    public List<EhGattung> gattungList = null;
    public int ekVon = 0;
    public int ekBis = 0;
    public int bankCount = 0;
    public int pSammelIdent = 0;
    public byte[] byteFile = null;
    public String fileName = "";
    public String errorLines = null;
    public String errorColumns = null;
    public List<String> violationList = null;
    public EclImportProfil importProfile = null;

    public List<EclInhaberImportAnmeldedaten> errorList = null;

    public List<EclInhaberImportAnmeldedaten> insertList = null;
    public List<EclInhaberImportAnmeldedaten> importList = null;

    public List<EclInhaberImportAnmeldedaten> InhaberImportListInsert = null;
    public List<EclInhaberImportAnmeldedaten> InhaberImportListUpdate = null;

    public List<EclImportAdresse> adressList = null;
    public List<EclInhaberImportAnmeldedaten> emptyAdressList = null;

    private LinkedList<String> stringList = null;
    private int rc = 0;
    public final Set<String> typeExcel = Set.of("xls", "xlsx");
    public final Set<String> typeCSV = Set.of("csv");
    private final Set<String> typeText = Set.of("txt", "1", "2", "3", "4", "5", "6", "7", "8", "9");

    public BlInhaberImport(boolean pIstServer, DbBundle pDbBundle) {
        super(pIstServer, pDbBundle);
    }

    public void setFile(File file, byte[] byteFile, EclImportProfil importProfile) {
        fileName = file.getName().trim();
        this.byteFile = byteFile;
        this.importProfile = importProfile;
    }

    public int loadLists() {

        if (verwendeWebService()) {

            WEStubBlInhaberImport stub = new WEStubBlInhaberImport(1);
            WEStubBlInhaberImportRC stubRC = verifyLogin(stub);

            if (stubRC.getRc() < 1)
                return stubRC.getRc();

            getResult(stubRC);

            return stubRC.getRc();

        } else {

            dbOpen();

            lDbBundle.dbInhaberImportAnmeldedaten.createTable();
            lDbBundle.dbInhaberImportAnmeldedaten.checkErgaenzung();
            lDbBundle.dbInhaberImportAnmeldedaten.updateAdresse();
            InhaberImportList = lDbBundle.dbInhaberImportAnmeldedaten.readAll();
            lDbBundle.dbImportProfil.updateTable();
            InhaberImportProfiles = lDbBundle.dbImportProfil.readAll();
            lDbBundle.dbImportProtokoll.createTable();

            dbClose();

            return 1;
        }
    }

    public int prepareFile() {

        if (verwendeWebService()) {

            WEStubBlInhaberImport stub = new WEStubBlInhaberImport(2);
            stub.setParameter(this);
            WEStubBlInhaberImportRC stubRC = verifyLogin(stub);

            getResult(stubRC);
            return stubRC.getRc();

        } else {
            String fileType = FilenameUtils.getExtension(fileName).toLowerCase();
            if (byteFile != null) {
                File f = null;
                try {
                    f = File.createTempFile("Import-", "." + fileType);

                    FileOutputStream out = new FileOutputStream(f);
                    out.write(byteFile);
                    out.close();
                } catch (IOException e) {
                    System.err.println(e);
                    return 0;
                }

                rc = typeExcel.contains(fileType) ? 1 : createList(f);

                if (rc == 1) {
                    insertList = new ArrayList<>();

                    if (typeText.contains(fileType))
                        rc = textFile();
                    else if (typeCSV.contains(fileType))
                        rc = tableFile(null);
                    else if (typeExcel.contains(fileType))
                        rc = tableFile(excelFile(f));
                    else if (fileType.equals("xml"))
                        rc = 8;

                    if (!insertList.isEmpty() && rc == 1)
                        rc = prepareList();
                }
                f.delete();
            }
            return rc == 1 ? 13 : rc;
        }
    }

    public int doImport() {

        if (verwendeWebService()) {

            WEStubBlInhaberImport stub = new WEStubBlInhaberImport(3);
            stub.setParameter(this);
            WEStubBlInhaberImportRC stubRC = verifyLogin(stub);

            getResult(stubRC);
            return stubRC.getRc();

        } else {

            BlAktienregister blReg = new BlAktienregister(istServer, lDbBundle);
            blReg.pKarteWurdeVorreserviert = true;
            if (blReg.listenLaden() != 1) {
                return 0;
            }

            BlAktienregisterImport blAktienregisterImport = new BlAktienregisterImport(blReg.staaten, lDbBundle);

            dbOpen();

            BlWillenserklaerung lWillenserklaerung = new BlWillenserklaerung();
            lWillenserklaerung.setzeDbBundle(lDbBundle);

            final List<EclInhaberImportAnmeldedaten> bankList = lDbBundle.dbInhaberImportAnmeldedaten
                    .readByKuerzel(importList.get(0).getDateikuerzel());

            if (bankList.size() > bankCount)
                return 11;

            InhaberImportListInsert = new ArrayList<>();

            for (EclInhaberImportAnmeldedaten row : importList) {

                if (row.getSatzKzf().equals("N")) {

                    row.setReferenzEKNr(lWillenserklaerung.getNeueKarteAuto(KonstKartenklasse.eintrittskartennummer,
                            row.getGattungId(), false));

                    InhaberImportListInsert.add(row);
                } else {
                    return 9;
                }
            }

            for (EclInhaberImportAnmeldedaten data : InhaberImportListInsert)
                lDbBundle.dbInhaberImportAnmeldedaten.insert(data);

            importList = lDbBundle.dbInhaberImportAnmeldedaten.readAll().stream()
                    .filter(e -> e.getDatei().equals(InhaberImportListInsert.get(0).getDatei())
                            && e.getGattungId() == InhaberImportListInsert.get(0).getGattungId())
                    .collect(Collectors.toList());

            List<EclAktienregister> registerUpdate = blAktienregisterImport.aufbereitenInhaberImport(importList);

            blReg.pSammelIdent = pSammelIdent;
            blReg.importList = importList;

            blReg.compareChanged(registerUpdate, false);

            final int size = 2500;
            int start = 0;
            int end = 0;

            final int insertSize = blReg.registerListeInsert.size();

            //      Insert
            while (end < insertSize) {

                end = ((start + size) < insertSize) ? start + size : insertSize;

                blReg.registerInsertPart = blReg.registerListeInsert.subList(start, end);
                blReg.loginInsertPart = blReg.loginListeInsert.subList(start, end);


                CaBug.druckeInfo("Insert: " + end + " von " + insertSize);

                start = end;
            }

            ekVon = importList.get(0).getReferenzEKNr();
            ekBis = importList.get(importList.size() - 1).getReferenzEKNr();

            final EclImportProtokoll protokoll = new EclImportProtokoll(0, lDbBundle.eclUserLogin.userLoginIdent, ekVon,
                    ekBis, fileName, DigestUtils.md5Hex(byteFile).toUpperCase(), null);
            lDbBundle.dbImportProtokoll.insert(protokoll);

            dbClose();

            loadLists();

            return 1;
        }
    }

    public int readProtokoll() {

        if (verwendeWebService()) {

            WEStubBlInhaberImport stub = new WEStubBlInhaberImport(4);
            WEStubBlInhaberImportRC stubRC = verifyLogin(stub);

            if (stubRC.getRc() < 1)
                return stubRC.getRc();

            getResult(stubRC);

            return stubRC.getRc();

        } else {

            dbOpen();

            lDbBundle.dbImportProtokoll.createTable();
            importProtokoll = lDbBundle.dbImportProtokoll.readAll();

            dbClose();

        }
        return 1;
    }

    public int insertProtokoll() {

        if (verwendeWebService()) {

            WEStubBlInhaberImport stub = new WEStubBlInhaberImport(5);
            stub.setParameter(this);
            WEStubBlInhaberImportRC stubRC = verifyLogin(stub);

            if (stubRC.getRc() < 1)
                return stubRC.getRc();

            getResult(stubRC);

            return stubRC.getRc();

        } else {

            dbOpen();

            lDbBundle.dbImportProtokoll.createTable();

            final String md5Hex = DigestUtils.md5Hex(byteFile).toUpperCase();
            final EclImportProtokoll tmpProtokoll = lDbBundle.dbImportProtokoll.readByDatei(md5Hex);

            if (tmpProtokoll == null) {

                final EclImportProtokoll protokoll = new EclImportProtokoll(0, lDbBundle.eclUserLogin.userLoginIdent,
                        ekVon, ekBis, fileName, md5Hex, null);
                lDbBundle.dbImportProtokoll.insert(protokoll);
            }
            dbClose();
        }
        return 1;
    }

    private int textFile() {

        for (String str : stringList) {

            try {
                EclInhaberImportAnmeldedaten anm = new EclInhaberImportAnmeldedaten(str);
                insertList.add(anm);
            } catch (NumberFormatException e) {
                return 10;
            }
        }
        return rc;
    }

    private int tableFile(LinkedList<String[]> list) {

        LinkedList<String[]> linkedList = list;

        // Pruefen ob CSV oder Excel, true = CSV
        if (list == null) {
            linkedList = new LinkedList<>();

            // Zerlegen der kompletten Zeilen in einzelne Felder
            for (String str : stringList)
                linkedList.add(str.replaceAll("\\s+", " ").split(";"));
        }

        if (!linkedList.isEmpty()) {

            // Import nach Spalten oder nach Ueberschriften
            Boolean columnImport = checkEmptyArray(importProfile.getDateiFeld());

            // Wird strickt nach Reihenfolge importiert
            if (columnImport) {

                linkedList.remove(0);

                for (var line : linkedList) {

                    if (checkEmptyArray(line)) {
                        break;
                    }

                    Map<String, String> dataMap = new HashMap<>();
                    for (int i = 0; i < line.length; i++) {
                        // db == null > Spalte wird ignoriert
                        final String db = importProfile.getDatenbankFeld()[i];

                        if (db != null) {
                            final String lineTrim = line[i] == null ? "" : line[i].trim();

                            if (lineTrim.isBlank()) {
                                /*
                                 * Leere Felder in den Weisungen führen dazu dazu das, dass System denkt die Weisungen sind danach zu Ende.
                                 * Alle weiteren Weisungen würden danach auf Enthaltung gesetzt werden.
                                 */
                                if (db.contains("abgabe")) {
                                    return 16;
                                }
                            } else {
                                dataMap.put(db, lineTrim);
                            }
                        }
                    }
                    EclInhaberImportAnmeldedaten anm = EclInhaberImportAnmeldedaten.createAnmeldedaten(dataMap);
                    anm.setSatzKzf(anm.getSatzKzf().equals("") ? "N" : anm.getSatzKzf());

                    if (!checkEmpty(anm))
                        insertList.add(anm);
                }

            } else {

                List<String> columnsFile = new LinkedList<>(Arrays.asList(linkedList.get(0)));
                List<String> columnsProfile = Arrays.asList(importProfile.getDateiFeld());

                for (var str : columnsFile) {

                    if (!columnsProfile.contains(str.trim()) && !str.isBlank()) {
                        rc = 5;
                        errorColumns += str + ", ";
                    }
                }

                if (errorColumns != null)
                    errorColumns = errorColumns.substring(0, errorColumns.isBlank() ? 0 : errorColumns.length() - 3);

                if (rc == 1) {
                    linkedList.remove(0);
                    Map<String, String> profilMap = importProfile.createMap();

                    for (var line : linkedList) {
                        Map<String, String> dataMap = new HashMap<>();
                        for (int i = 0; i < columnsFile.size(); i++) {
                            final String db = profilMap.get(columnsFile.get(i).trim());
                            if (db != null) {
                                final String lineTrim = line[i] == null ? "" : line[i].trim();

                                if (lineTrim.isBlank()) {
                                    // Leeres Feld bei Weisungen 
                                    if (db.contains("abgabe")) {
                                        return 16;
                                    }
                                } else {
                                    // Clearstream Import
                                    if (db.equals("abgabe")) {
                                        if (prepareClearstream(dataMap, lineTrim)) {
                                            return 17;
                                        }
                                        for (var top : dataMap.entrySet()) {
                                            System.out.println(top.getKey() + " / " + top.getValue());
                                        }
                                    } else {
                                        dataMap.put(db, lineTrim);
                                    }
                                }
                            }
                        }
                        EclInhaberImportAnmeldedaten anm = EclInhaberImportAnmeldedaten.createAnmeldedaten(dataMap);
                        anm.setSatzKzf(anm.getSatzKzf().equals("") ? "N" : anm.getSatzKzf());

                        if (!checkEmpty(anm))
                            insertList.add(anm);
                    }
                }
            }

            ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
            Validator validator = factory.getValidator();

            for (EclInhaberImportAnmeldedaten anm : insertList) {

                Set<ConstraintViolation<EclInhaberImportAnmeldedaten>> violations = validator.validate(anm);
                if (violations.size() > 0) {
                    rc = 4;
                    if (violationList == null)
                        violationList = new ArrayList<>();

                    for (ConstraintViolation<EclInhaberImportAnmeldedaten> violation : violations)
                        violationList.add(violation.getMessage() + " - " + violation.getInvalidValue() + " - "
                                + violation.getInvalidValue().toString().length() + " Zeichen");

                }
            }

        }
        return rc;
    }

    public LinkedList<String[]> excelFile(File file) {

        LinkedList<String[]> linkedList = new LinkedList<>();

        try {
            InputStream f = new FileInputStream(file);

            try (Workbook book = fileName.endsWith("xls") ? new HSSFWorkbook(f) : new XSSFWorkbook(f)) {

                Sheet sheet = book.getSheetAt(0);

                int arrSize = 0;
                int r = 0;

                for (Row row : sheet) {
                    if (r == row.getRowNum()) {
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
                        linkedList.add(arr);
                        r++;
                    } else {
                        break;
                    }
                }
            }
        } catch (Exception e) {
            CaBug.druckeInfo("Diese Excel wird nicht unterstützt");
            CaBug.druckeInfo(e.getMessage());
            return null;
        }
        return linkedList;
    }

    private int prepareList() {

        final String dateiKuerzel = fileName.split("_| ")[0];

        dbOpenUndWeitere();

        fillGattungen();

        Boolean importAdresse = false;

        for (EclInhaberImportAnmeldedaten row : insertList) {
            if (!row.getAdresse().isBlank()) {
                importAdresse = true;
                break;
            }
        }

        importList = new ArrayList<>();

        if (importAdresse) {
            adressList = lDbBundle.dbImportAdresse.readAll();
            emptyAdressList = new ArrayList<>();
        }

        for (EclInhaberImportAnmeldedaten row : insertList) {

            row.setDatei(fileName);
            row.setDateikuerzel(dateiKuerzel.toLowerCase());

            if (importAdresse)
                fillAdresse(row);

            if (row.getGattungId() == 0) {
                int gattung = findGattung(row);

                if (gattung == 0) {
                    if (errorList == null)
                        errorList = new ArrayList<>();
                    errorList.add(row);
                } else {
                    row.setGattungId(gattung);
                    importList.add(row);
                }
            }
        }

        final EclImportProtokoll tmpProtokoll = lDbBundle.dbImportProtokoll
                .readByDatei(DigestUtils.md5Hex(byteFile).toUpperCase());

        final List<EclInhaberImportAnmeldedaten> banklist = lDbBundle.dbInhaberImportAnmeldedaten.readByKuerzel(dateiKuerzel);
        bankCount = banklist.size();

        dbClose();

        if (!banklist.isEmpty() && !importList.isEmpty()) {
            if (banklist.contains(importList.get(0))) {
                return 3;
            }
        }

        if (tmpProtokoll != null)
            return 3;

        if (emptyAdressList != null && !emptyAdressList.isEmpty())
            return 14;

        if (errorList != null)
            return 6;

        for (EclInhaberImportAnmeldedaten anm : insertList) {
            for (String abgabe : anm.getAbgabe())
                if (abgabe != null)
                    return 12;
        }
        return 1;
    }

    private void fillGattungen() {
        if (gattungList == null) {
            gattungList = new ArrayList<>();

            lDbBundle.dbIsin.readAll();
            final List<EclIsin> fullIsinList = lDbBundle.dbIsin.ergebnis();

            for (int i = 0; i < lDbBundle.param.paramBasis.gattungAktiv.length; i++) {
                if (lDbBundle.param.paramBasis.gattungAktiv[i] == true) {

                    final int gattung = i + 1;
                    List<String> isins = new ArrayList<>();

                    for (EclIsin isin : fullIsinList) {
                        if (isin.gaettungId == gattung)
                            isins.add(isin.isin);
                    }

                    gattungList.add(new EhGattung(i + 1, lDbBundle.param.paramBasis.gattungBezeichnung[i], isins));

                }
            }
        }
    }

    private int createList(File file) {

        CaDateiVerwaltung dateiVerwaltung = new CaDateiVerwaltung();
        stringList = dateiVerwaltung.convertDateiToList(file, CaDateiVerwaltung.isoCharset, 1,
                CaDateiVerwaltung.isoCharset, 0);
        if (stringList == null) {
            errorLines = dateiVerwaltung.createErrorList(file, dateiVerwaltung.bestCharset);
            return 2;
        }
        return 1;
    }

    private int findGattung(EclInhaberImportAnmeldedaten data) {
        if (data.getWkn().isBlank() && data.getIsin().isBlank())
            return 0;

        for (EhGattung gattung : gattungList) {
            for (String str : gattung.getIsinList()) {

                if (str.contains(!data.getIsin().isBlank() ? data.getIsin() : data.getWkn()))
                    return gattung.getGattungId();

            }
        }
        return 0;
    }

    private Boolean checkEmpty(EclInhaberImportAnmeldedaten anm) {

        if (anm.getNachname().isBlank() && anm.getVorname().isBlank() && anm.getAnmeldung().isBlank()
                && anm.getStueck() < 1)
            return true;
        else
            return false;
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

    private void fillAdresse(EclInhaberImportAnmeldedaten anm) {

        EclImportAdresse adresse = adressList.stream().filter(e -> e.getAdresse().equals(anm.getAdresse())).findFirst()
                .orElse(null);

        if (adresse != null) {

            anm.setStrasse(adresse.getStrasse());
            anm.setAdresszusatz(adresse.getAdresszusatz());
            anm.setPlz(adresse.getPostleitzahl());
            anm.setOrt(adresse.getOrt());
            anm.setLand(adresse.getStaat());

        } else {
            emptyAdressList.add(anm);
        }
    }

    private boolean prepareClearstream(Map<String, String> dataMap, String lineTrim) {
        
        dbOpen();

        StubAbstimmungen stubAbstimmungen = new StubAbstimmungen(true, lDbBundle);
        stubAbstimmungen.zeigeSortierungAbstimmungenInit(0, 1, 0);
        EclAbstimmung[] abstimmungen = stubAbstimmungen.angezeigteAbstimmungen;

        dbClose();

        Map<String, Integer> topToAbgabe = new HashMap<>();

        for (var abstimmung : abstimmungen) {
            String top = abstimmung.nummerKey
                    + (abstimmung.nummerindexKey.isBlank() ? "" : "." + abstimmung.nummerindexKey);
            System.out.println(top + " > " + abstimmung.identWeisungssatz);
            if (abstimmung.identWeisungssatz > 0)
                topToAbgabe.put(top, abstimmung.identWeisungssatz);
        }

        final String abgabe = "abgabe";

        if (lineTrim.startsWith("Vote For All:")) {

            String[] splits = lineTrim.split("((?=VoteOption))");

            for (var top : topToAbgabe.entrySet()) {
                dataMap.put(abgabe + top.getValue(), splits[1].trim());
            }

        } else if (lineTrim.startsWith("Global Vote:") || lineTrim.startsWith("Split Vote:")) {

            String[] splits = lineTrim.split("((?=Proposal))");

            for (var s : splits) {
                if (s.startsWith("Proposal")) {

                    splits = s.split(" - ");

                    // 1 = TOP
                    // 3 = Vote (For, Against, Abstain)
                    // 4 = Anzahl (Nur gefüllt bei Split Vote)

                    if (splits.length == 5) {
                        String anz = splits[4].split(" ")[0].trim();
                        if (!dataMap.get("stueck").equals(anz)) {
                            return true;
                        }
                    }

                    int x = topToAbgabe.get(splits[1].trim());

                    dataMap.put(abgabe + x, splits[3].trim());
                }
            }
        } else {
            System.out.println("Kein bekannter Inhalt");
            return true;
        }
        
        dataMap.put("anmeldung", "Clearstream Banking AG");
        dataMap.put("strasse", "Mergenthalerallee 61");
        dataMap.put("plz", "65760");
        dataMap.put("ort", "Eschborn");
        dataMap.put("besitzMm", "V");
        
        return false;
    }

    public void setParameter(WEStubBlInhaberImport stub) {

        InhaberImportList = stub.InhaberImportList;
        InhaberImportProfiles = stub.InhaberImportProfiles;
        importProtokoll = stub.importProtokoll;
        fileName = stub.fileName;
        byteFile = stub.byteFile;
        importProfile = stub.importProfile;
        gattungList = stub.gattungList;
        bankCount = stub.bankCount;
        errorList = stub.errorList;
        insertList = stub.insertList;
        importList = stub.importList;
        InhaberImportListInsert = stub.InhaberImportListInsert;
        pSammelIdent = stub.pSammelIdent;

    }

    public void getResult(WEStubBlInhaberImportRC stubRC) {

        InhaberImportList = stubRC.InhaberImportList == null ? InhaberImportList : stubRC.InhaberImportList;
        InhaberImportProfiles = stubRC.InhaberImportProfiles == null ? InhaberImportProfiles : stubRC.InhaberImportProfiles;
        importProtokoll = stubRC.importProtokoll == null ? importProtokoll : stubRC.importProtokoll;
        gattungList = stubRC.gattungList == null ? gattungList : stubRC.gattungList;
        bankCount = stubRC.bankCount;
        ekVon = stubRC.ekVon;
        ekBis = stubRC.ekBis;
        errorLines = stubRC.errorLines == null ? errorLines : stubRC.errorLines;
        errorColumns = stubRC.errorColumns == null ? errorColumns : stubRC.errorColumns;
        violationList = stubRC.violationList == null ? violationList : stubRC.violationList;
        errorList = stubRC.errorList == null ? errorList : stubRC.errorList;
        insertList = stubRC.insertList == null ? insertList : stubRC.insertList;
        importList = stubRC.importList == null ? importList : stubRC.importList;
        InhaberImportListInsert = stubRC.InhaberImportListInsert == null ? InhaberImportListInsert : stubRC.InhaberImportListInsert;
        emptyAdressList = stubRC.emptyAdressList == null ? emptyAdressList : stubRC.emptyAdressList;

    }

    private WEStubBlInhaberImportRC verifyLogin(WEStubBlInhaberImport stub) {

        WELoginVerify weLoginVerify = new WELoginVerify();
        stub.setWeLoginVerify(weLoginVerify);

        return wsClient.stubBlInhaberImport(stub);
    }
}
