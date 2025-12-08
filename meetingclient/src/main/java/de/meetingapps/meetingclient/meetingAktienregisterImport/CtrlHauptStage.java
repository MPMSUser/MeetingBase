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
package de.meetingapps.meetingclient.meetingAktienregisterImport;

import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;

import de.meetingapps.meetingclient.meetingClientAllg.CALeseParameterNeu;
import de.meetingapps.meetingclient.meetingClientDialoge.CaController;
import de.meetingapps.meetingclient.meetingClientDialoge.CaIcon;
import de.meetingapps.meetingclient.meetingClientDialoge.CaZeigeHinweis;
import de.meetingapps.meetingclient.meetingClientDialoge.CtrlLoginNeu;
import de.meetingapps.meetingclient.meetingClientDialoge.CtrlRoot;
import de.meetingapps.meetingclient.meetingClientDialoge.CtrlWaehleModule;
import de.meetingapps.meetingclient.meetingClientOberflaechen.CustomAlert;
import de.meetingapps.meetingclient.meetingClientOberflaechen.LoadingScreen;
import de.meetingapps.meetingclient.meetingClientOberflaechen.ObjectActions;
import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaDateiVerwaltung;
import de.meetingapps.meetingportal.meetComAllg.CaDateiWrite;
import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComBVerwaltung.BvDatenbank;
import de.meetingapps.meetingportal.meetComBl.BlAktienregister;
import de.meetingapps.meetingportal.meetComBl.BlAktienregisterExport;
import de.meetingapps.meetingportal.meetComBl.BlAktienregisterImport;
import de.meetingapps.meetingportal.meetComBl.BlInhaberImport;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclAktienregister;
import de.meetingapps.meetingportal.meetComEntities.EclAktienregisterErgaenzung;
import de.meetingapps.meetingportal.meetComEntities.EclStaaten;
import de.meetingapps.meetingportal.meetComHVParam.ParamS;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

/**
 * The Class CtrlHauptStage.
 */
public class CtrlHauptStage extends CtrlRoot {
    
    @FXML
    private AnchorPane rootPane;
    /** The layout. */
    @FXML
    private HBox layout;

    /*
     * Import 
     */
    @FXML
    private VBox importBox;
    /** The cb format. */
    @FXML
    private ComboBox<String> cbFormat;
    /** The btn datei. */
    @FXML
    private Button btnDatei;
    /** The txt versand. */
    @FXML
    private TextField txtVersand;
    /** The btn next. */
    @FXML
    private Button btnNext;
    /** The btn import. */
    @FXML
    private Button btnImport;
    /** The btn export. */
    @FXML
    private Button btnExport;
    /** The btn folder. */
    @FXML
    private Button btnFolder;
    /** The ta log. */
    @FXML
    private TextArea taLog;
    /** The btn expand. */
    @FXML
    private Button btnExpand;

    /** The termin box. */
    /*
     * Termine
     */
    @FXML
    private VBox terminBox;
    /** The tf emittent. */
    @FXML
    private TextField tfEmittent;
    /** The tf email. */
    @FXML
    private TextField tfEmail;
    /** The dp hv datum. */
    @FXML
    private DatePicker dpHvDatum;
    @FXML
    private Button btnClear;
    /** The dp erst. */
    @FXML
    private DatePicker dpErst;
    @FXML
    private Button btnErst;
    /** The dp nach. */
    @FXML
    private DatePicker dpNach;
    /** The btn nach. */
    @FXML
    private Button btnNach;
    /** The dp letzter. */
    @FXML
    private DatePicker dpLetzter;
    @FXML
    private Button btnLetzter;
    /** The btn email. */
    @FXML
    private Button btnEmail;

    /** The settings box. */
    /*
     * Einstellungen
     */
    @FXML
    private VBox settingsBox;
    @FXML
    private ComboBox<String> cbPasswort;
    @FXML
    private Button btnPasswort;
    /** The cb mitarbeiter. */
    @FXML
    private CheckBox cbMitarbeiter;
    /** The txt pfad. */
    @FXML
    private TextField txtPfad;
    /** The cb insert only. */
    @FXML
    private CheckBox cbInsertOnly;

    /** The loading. */
    private StackPane loading;
    /** The file. */
    private File file = null;
    private File directory = null;
    private byte[] byteFile = null;

    /** The txt. */
    private ExtensionFilter txt = new ExtensionFilter("TXT files", new String[] { "*.txt" });
    /** The csv. */
    private ExtensionFilter csv = new ExtensionFilter("CSV files", new String[] { "*.csv" });
    /** The versandnummer. */
    private int versandnummer = 1;
    /** The result. */
    private Boolean result = true;

    /** The repeat. */
    private Boolean repeat = false;

    private Boolean accept = false;

    /** The bl reg. */
    private BlAktienregister blReg = null;

    /** The bl aktienregister import. */
    private BlAktienregisterImport blAktienregisterImport = null;

    /** The duration. */
    private long duration = 0;

    /** The insert size. */
    private int insertSize = 0;

    /** The update size. */
    private int updateSize = 0;

    /** The t. */
    private Thread t = null;

    /** The pfad. */
    private final String pfad = ParamS.clGlobalVar.lwPfadAllgemein;

    /*
     * Neue Import Formate
     */

    /** The formate. */
    private Set<String> formate = new HashSet<>(List.of("arfuehrer001", "arfuehrer002", "arfuehrer003", "arfuehrer003 ku164", "ku178",
            "ku243", "ku302_303", "ku108", "arfuehrer004", "ku1001", "ku217 HG", "ku1002", "ku1003",
            "ku168", "ku1004", "ku1005", "Seminar"));

    /**
     * Creates the register.
     *
     * @param list      the list
     * @param versandnr the versandnr
     * @return the list
     * @throws Exception the exception
     */
    private List<EclAktienregister> createRegister(List<String> list, String versandnr) throws Exception {

        if (cbFormat.getValue().equals("arfuehrer004"))
            return blAktienregisterImport.aufbereitenarfuehrer005(list, versandnr);
        else if (cbFormat.getValue().equals("arfuehrer003"))
            return blAktienregisterImport.aufbereitenarfuehrer003Neu(list, versandnr);
        else if (cbFormat.getValue().equals("arfuehrer001"))
            return blAktienregisterImport.aufbereitenarfuehrer001Neu(list, versandnr);
        else if (cbFormat.getValue().equals("arfuehrer003 ku164"))
            return blAktienregisterImport.aufbereitenarfuehrer003KSNeu(list, versandnr);
        else if (cbFormat.getValue().equals("arfuehrer002"))
            return blAktienregisterImport.aufbereitenarfuehrer002(list, versandnr);
        else if (cbFormat.getValue().equals("ku1006"))
            return blAktienregisterImport.aufbereitenKu1006(list, versandnr);
        else if (cbFormat.getValue().equals("ku178"))
            return blAktienregisterImport.aufbereitenku178(list, versandnr);
        else if (cbFormat.getValue().equals("ku243"))
            return blAktienregisterImport.aufbereitenku243(list, versandnr);
        else if (cbFormat.getValue().equals("ku302_303"))
            return blAktienregisterImport.aufbereitenku302_303(list, versandnr);
        else if (cbFormat.getValue().equals("ku108"))
            return blAktienregisterImport.aufbereitenku108(list, versandnr);
        else if (cbFormat.getValue().equals("ku1001"))
            return blAktienregisterImport.aufbereitenku1001(list, versandnr);
        else if (cbFormat.getValue().equals("ku217 HG"))
            return blAktienregisterImport.aufbereitenku217_2(list, versandnr);
        else if (cbFormat.getValue().equals("ku1002"))
            return blAktienregisterImport.aufbereitenku1002(list, versandnr);
        else if (cbFormat.getValue().equals("ku1003"))
            return blAktienregisterImport.aufbereitenku1008(list, versandnr);
        else if (cbFormat.getValue().equals("ku168"))
            return blAktienregisterImport.aufbereitenku168(list, versandnr);
        else if (cbFormat.getValue().equals("ku1004"))
            return blAktienregisterImport.aufbereitenku1004(list, versandnr);
        else if (cbFormat.getValue().equals("ku1005"))
            return blAktienregisterImport.aufbereitenku1005(list, versandnr);
        else if (cbFormat.getValue().equals("Seminar"))
            return blAktienregisterImport.aufbereitenSeminar(list, versandnr);
        else
            return null;
    }

    /**
     * Initialize.
     */
    /*
     * Funktionen
     */
    @FXML
    void initialize() {

        btnExpand.setGraphic(new FontIcon(FontAwesomeSolid.ANGLE_DOUBLE_RIGHT));
        btnNext.setGraphic(new FontIcon(FontAwesomeSolid.PLUS));
        btnFolder.setGraphic(new FontIcon(FontAwesomeSolid.FOLDER));

        configureTerminBox();

        layout.getChildren().removeAll(terminBox, settingsBox);

        txtPfad.setText(pfad);

        formate = formate.stream().sorted().collect(Collectors.toCollection(LinkedHashSet::new));
        cbFormat.getItems().addAll(formate);
        cbFormat.setValue(cbFormat.getItems().get(0));
        cbFormat.requestFocus();

        ObjectActions.filterComboBox(cbFormat, null);

        btnClear.setGraphic(new FontIcon(FontAwesomeSolid.TRASH));
        btnClear.setOnAction(e -> {
            dpHvDatum.setValue(null);
            dpErst.setValue(null);
            dpNach.setValue(null);
            dpLetzter.setValue(null);
        });

        btnErst.setGraphic(new FontIcon(FontAwesomeSolid.MAIL_BULK));
        btnErst.setOnAction(e -> createCalendarEvent("1. Abzug: " + tfEmittent.getText(), dpErst.getValue()));

        btnNach.setGraphic(new FontIcon(FontAwesomeSolid.MAIL_BULK));
        btnNach.setOnAction(e -> createCalendarEvent("Nachversand: " + tfEmittent.getText(), dpNach.getValue()));

        btnLetzter.setGraphic(new FontIcon(FontAwesomeSolid.MAIL_BULK));
        btnLetzter.setOnAction(e -> createCalendarEvent("Bestandsabgleich: " + tfEmittent.getText(),
                dpLetzter.getValue() == null ? null : dpLetzter.getValue().plusDays(1)));

        cbPasswort.getItems().addAll("Alle", "Leere", "Post", "E-Mail");
        cbPasswort.getSelectionModel().selectedItemProperty()
                .addListener((o, oV, nV) -> btnPasswort.setDisable(nV == null));
        btnPasswort.setGraphic(new FontIcon(FontAwesomeSolid.PLAY));

        loading = LoadingScreen.createLoadingScreen(rootPane);

        importBox.prefWidthProperty().bind(layout.widthProperty().divide(7).multiply(7));
        terminBox.prefWidthProperty().bind(layout.widthProperty().divide(7).multiply(2));
        settingsBox.prefWidthProperty().bind(layout.widthProperty().divide(7).multiply(3));
    }

    /*
     * Funktionen Import-Box
     */

    /**
     * On btn datei.
     */
    @FXML
    void onBtnDatei() {

        FileChooser fc = new FileChooser();

        if (cbFormat.getValue().equals("arfuehrer004") || cbFormat.getValue().equals("arfuehrer001")) {
            fc.getExtensionFilters().add(txt);
        } else {
            fc.getExtensionFilters().add(csv);
        }
        file = fc.showOpenDialog(null);

        if (file != null) {

            btnDatei.setText(file.getName());
            btnImport.setDisable(false);

            try (InputStream in = new FileInputStream(file)) {
                byteFile = IOUtils.toByteArray(in);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * On btn next.
     *
     * @param event the event
     */
    @FXML
    void onBtnNext(ActionEvent event) {

        DbBundle dbBundle = new DbBundle();
        dbBundle.openAll();

        versandnummer = dbBundle.dbAktienregister.readHighestVersandnummer() + 1;
        dbBundle.closeAll();

        txtVersand.setText(String.valueOf(versandnummer));
    }

    /**
     * On btn folder.
     *
     * @param event the event
     */
    @FXML
    void onBtnFolder(ActionEvent event) {

        final String directory = txtPfad.getText() + "\\betteroutput\\" + ParamS.clGlobalVar.getMandantPfad();

        if (!CaDateiVerwaltung.fileExist(directory)) {
            if (CaDateiVerwaltung.createDirectory(directory)) {
                print("Verzeichnis erstellt");
            }
        }
        CaDateiVerwaltung.openFileExplorer(directory);
    }

    /**
     * On btn import.
     *
     * @param event the event
     */
    @FXML
    void onBtnImport(ActionEvent event) {
        if (txtVersand.getText().matches("\\d+")) {
            t = new Thread(compareTask());
            t.start();
        } else
            taLog.appendText("Versandnummer und/oder Datei nicht ausgewählt");
    }

    /**
     * Compare task.
     *
     * @return the task
     */
    private Task<Void> compareTask() {

        Task<Void> task = new Task<Void>() {
            @Override
            public Void call() throws Exception {
                compare();
                return null;
            }
        };

        task.setOnScheduled(e -> loading.setVisible(true));

        task.setOnSucceeded(e -> {

            System.out.println("Compare abgeschlossen.");
            loading.setVisible(false);

            if (insertSize != 0 || updateSize != 0) {

                final long oldCapital = blReg.bestand[0];
                final long newCapital = blAktienregisterImport.bestand;

                if (createDialog(oldCapital, newCapital, insertSize, blReg.zeroShareholder, blReg.change,
                        blReg.stockChange, blReg.addressChange, blReg.fullChange)) {
                    new Thread(updateTask()).start();
                } else {
                    print("Import wurde abgebrochen.");
                }
            }
        });

        task.setOnFailed(e -> {
            System.out.println("Fehler!");
            loading.setVisible(false);
            task.getException().printStackTrace(System.err);
            return;
        });
        return task;
    }

    /**
     * Compare.
     */
    private void compare() {

        String versandNummer = txtVersand.getText();

        duration = 0;
        insertSize = 0;
        updateSize = 0;

        blReg = new BlAktienregister(false, new DbBundle());

        System.out.println(LocalDateTime.now());
        System.out.println("Original Listen füllen...\n");
        if (blReg.listenLaden() == 1) {
            System.out.println("initialisiere Listen");
        }

        final long timeStart = System.currentTimeMillis();

        print("Aktienbestand vor Import: " + NumberFormat.getIntegerInstance().format(blReg.bestand[0]) + " / "
                + NumberFormat.getIntegerInstance().format(blReg.bestand[1]));

        blAktienregisterImport = new BlAktienregisterImport(blReg.staaten, ParamS.param.mandant);

        List<String> convertierteListe = new ArrayList<>();

        if (!cbFormat.getValue().equals("ku178")) {

            Charset startCharset = CaDateiVerwaltung.isoCharset;

            CaDateiVerwaltung dateiVerwaltung = new CaDateiVerwaltung();
            convertierteListe = dateiVerwaltung.convertDateiToList(file, startCharset, 1, startCharset, 0);
            if (convertierteListe == null) {
                print("Zeilen: " + dateiVerwaltung.createErrorList(file, dateiVerwaltung.bestCharset)
                        + " nach Sonderzeichen überprüfen.");
                return;
            }
        }

        if (result) {

            List<EclAktienregister> registerNew;

            try {

                registerNew = createRegister(convertierteListe, versandNummer);

            } catch (Exception e) {
                print("Ausgewählter Importformat hat einen Fehler erkannt!");
                System.out.println(e.getMessage());
                e.printStackTrace();
                return;
            }

            if (registerNew != null && !registerNew.isEmpty()) {
                System.out.println(LocalDateTime.now());

                List<EclAktienregister> list = blReg.compareUnchanged(registerNew);

                if (list == null) {
                    print("Es wurden 2 identische Datensätze nach der Aufbereitung entdeckt");
                    print("Programmierer verständigen und evtl. Aufbereitung überprüfen");
                    return;
                }

                blReg.compareChanged(list, cbInsertOnly.isSelected());

                blReg.compareNull(cbInsertOnly.isSelected());

                if (blReg.registerListeInsert.size() == blReg.loginListeInsert.size()) {
                    insertSize = blReg.registerListeInsert.size();

                    if (blReg.registerListeUpdate.size() != 0)
                        updateSize = blReg.registerListeUpdate.size();

                    if (insertSize == 0 && updateSize == 0) {
                        print("Keine Änderungen");
                        insertImportProtokoll();
                    }

                } else {
                    print("FEHLER - Größe der neuen Registereintrage und Logindaten sind nicht gleich!!!");
                    print("Bitte erneut ausführen");
                }
            }
        } else {
            print("FEHLER im Dateiformat erkannt - Bitte überprüfen!");
            print(cbFormat.getValue().equals("arfuehrer002") ? "Importdatei auf UTF-8 Kodierung ändern" : "");
        }

        final long timeEnd = System.currentTimeMillis();
        final float millisek = (float) (timeEnd - timeStart);

        final String print = millisek > 60000.0 ? new String((millisek / 1000.0 / 60.0) + " Minuten")
                : new String((millisek / 1000.0) + " Sekunden");
        System.out.println("Fertig!");
        System.out.println("Dauer des Vergleiches: " + print);

    }

    /**
     * Update task.
     *
     * @return the task
     */
    private Task<Void> updateTask() {

        Task<Void> task = new Task<Void>() {
            @Override
            public Void call() throws Exception {
                update();
                return null;
            }
        };

        task.setOnScheduled(e -> loading.setVisible(true));

        task.setOnSucceeded(e -> {
            System.out.println("Import abgeschlossen.");
            loading.setVisible(false);

            if (repeat) {
                new Thread(compareTask()).start();
            }
        });

        task.setOnFailed(e -> {
            System.out.println("Fehler!");
            loading.setVisible(false);

            Throwable throwable = task.getException();
            throwable.printStackTrace();
            e.consume();
            return;
        });
        return task;
    }

    /**
     * Update.
     */
    private void update() {

        final long timeStart = System.currentTimeMillis();

        repeat = false;

        print("Übersicht:");
        print(insertSize == 0 ? "" : insertSize + " neue Aktionäre einspielen");
        print(insertSize == 0 ? "" : insertSize + " neue Login einspielen...");
        print(blReg.registerListeUpdate.size() == 0 ? ""
                : blReg.registerListeUpdate.size() + " Registereinträge aktualisieren");

        final int size = 1000;
        int start = 0;
        int end = 0;

        print(insertSize == 0 ? "" : insertSize + " neue Aktionäre einspielen");

        //      Insert
        while (end < insertSize) {

            end = ((start + size) < insertSize) ? start + size : insertSize;

            blReg.registerInsertPart = blReg.registerListeInsert.subList(start, end);
            blReg.loginInsertPart = blReg.loginListeInsert.subList(start, end);

            blReg.insertRegister();

            print("Insert: " + end + " von " + insertSize);

            start = end;

        }

        start = 0;
        end = 0;

        final int updateSize = blReg.registerListeUpdate.size();

        print(updateSize == 0 ? "" : updateSize + " Registereinträge aktualisieren");

        //      Update
        while (end < updateSize) {

            end = ((start + size) < updateSize) ? start + size : updateSize;

            blReg.registerUpdatePart = blReg.registerListeUpdate.subList(start, end);
            blReg.historiePart = blReg.historieListe.subList(start, end);

            blReg.updateRegister();

            System.out.println(blReg.count + " / " + blReg.registerUpdatePart.size());
            if (blReg.count != blReg.registerUpdatePart.size()) {
                repeat = true;
                print("Es wurden Datensätze nicht Verarbeitet - Import nochmal ausführen!");
            }
            print("Update: " + end + " von " + updateSize);

            start = end;

        }
        //      Funkton fuer Mitgleiderversammlungen
        if (!blAktienregisterImport.ergaenzungMap.isEmpty()) {
            importErgaenzung(blAktienregisterImport);
        }

        insertImportProtokoll();

        blReg.readAktienregisterBestand();
        print("Aktienbestand Ende: " + NumberFormat.getIntegerInstance().format(blReg.bestand[0]) + " / "
                + NumberFormat.getIntegerInstance().format(blReg.bestand[1]));

        final long timeEnd = System.currentTimeMillis();
        duration += (timeEnd - timeStart);

        final float erg = (float) (duration / 1000.0 / 60.0);

        print("Fertig!");
        print("Dauer des Updates: " + DecimalFormat.getInstance().format(erg) + " Minuten");
    }

    /**
     * Import ergaenzung.
     *
     * @param blAktienregisterImport the bl aktienregister import
     */
    //  GV
    private void importErgaenzung(BlAktienregisterImport blAktienregisterImport) {

        blReg.listenLaden();

        DbBundle dbBundle = new DbBundle();
        dbBundle.openAll();

        int i = 0;
        for (EclAktienregister ar : blReg.registerListeInsert) {
            EclAktienregisterErgaenzung tmp = blAktienregisterImport.ergaenzungMap.get(ar.aktionaersnummer);
            if (tmp != null) {
                tmp.aktienregisterIdent = blReg.registerListe.stream()
                        .filter(e -> e.aktionaersnummer.equals(ar.aktionaersnummer)).findAny()
                        .orElse(null).aktienregisterIdent;

                if (i % 100 == 0)
                    CaBug.out("Insert Ergänzung: " + i + " von " + blReg.registerListeInsert.size());

                dbBundle.dbAktienregisterErgaenzung.insert(tmp);

                i++;
            }
        }
        i = 0;

        dbBundle.dbAktienregisterErgaenzung.readAll();
        List<EclAktienregisterErgaenzung> ergList = Arrays.asList(dbBundle.dbAktienregisterErgaenzung.ergebnisArray);

        int x = 0;

        for (EclAktienregister ar : blReg.registerListeUpdate) {

            EclAktienregisterErgaenzung tmp = blAktienregisterImport.ergaenzungMap.get(ar.aktionaersnummer);
            if (tmp != null) {
                tmp.aktienregisterIdent = ar.aktienregisterIdent;

                if (i % 100 == 0)
                    CaBug.out("Update Ergänzung: " + i + " von " + blReg.registerListeUpdate.size());

                if (x < 5) {
                    EclAktienregisterErgaenzung erg = ergList.stream()
                            .filter(e -> e.aktienregisterIdent == ar.aktienregisterIdent).findAny().orElse(null);
                    CaBug.out(erg.toString());
                    CaBug.out(tmp.toString());
                    x++;
                }

                if (!ergList.contains(tmp))
                    dbBundle.dbAktienregisterErgaenzung.update(tmp);

                i++;
            }
        }
        dbBundle.closeAll();
    }

    private void insertImportProtokoll() {

        BlInhaberImport blInhaberImport = new BlInhaberImport(false, new DbBundle());
        blInhaberImport.fileName = file.getName().trim();
        blInhaberImport.byteFile = byteFile;
        blInhaberImport.insertProtokoll();
    }

    /**
     * Creates the dialog.
     *
     * @param oldCapital the old capital
     * @param newCapital the new capital
     * @param newShareholder the new shareholder
     * @param zeroShareholder the zero shareholder
     * @param changes the changes
     * @param stock the stock
     * @param address the address
     * @param both the both
     * @return the boolean
     */
    private Boolean createDialog(long oldCapital, long newCapital, int newShareholder, int zeroShareholder, int changes,
            int stock, int address, int both) {

        Alert a = new Alert(AlertType.CONFIRMATION);
        a.setTitle("Import Informationen");
        a.setHeaderText("Sollen folgende Änderungen durchgeführt werden:");

        VBox box = new VBox(2);

        if (oldCapital != newCapital) {
            StringBuilder sb0 = new StringBuilder();
            sb0.append("Neues Grundkapital:\n");
            sb0.append(number(oldCapital) + "\t>\t" + number(newCapital) + "\n");
            Label l0 = new Label(sb0.toString());
            configLabel(l0, true, 10.0);
            box.getChildren().add(l0);
        }

        StringBuilder sb1 = new StringBuilder();
        sb1.append(printDialog("Neue Aktionäre:\t", newShareholder, "\n"));
        sb1.append(printDialog("Änderungen:\t\t", changes, ""));

        StringBuilder sb2 = new StringBuilder();
        sb2.append(printDialog("Bestand:\t\t", stock, "\n"));
        sb2.append(printDialog("Anschrift:\t\t", address, "\n"));
        sb2.append(printDialog("Best. u. An.:\t", both, "\n"));
        sb2.append(printDialog("0-Bestände:\t", zeroShareholder, ""));

        int diff = changes - stock - address - both - zeroShareholder;

        if (diff > 0) {
            sb2.append("\n" + printDialog("Andere:\t\t", diff, ""));
        }

        Label l1 = new Label(sb1.toString());
        configLabel(l1, false, 130.0);
        box.getChildren().add(l1);

        Label l2 = new Label(sb2.toString());
        l2.setTooltip(new Tooltip("Änderungen"));
        configLabel(l2, false, 140.0);
        box.getChildren().add(l2);

        a.getDialogPane().setContent(box);

        Optional<ButtonType> result = a.showAndWait();
        if (result.get() == ButtonType.OK)
            return true;
        else
            return false;
    }

    private String printDialog(String start, int count, String end) {
        return (count > 0) ? (start + number(count) + end) : "";
    }

    private String number(int num) {
        return NumberFormat.getInstance().format(num);
    }

    private String number(long num) {
        return NumberFormat.getInstance().format(num);
    }

    private void configLabel(Label l, Boolean center, Double right) {
        l.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        l.setAlignment(Pos.CENTER_LEFT);
        l.setStyle("-fx-border-radius: 5; -fx-border-color: #231825; -fx-background-color: #e8e8e8");
        if (center) {
            l.setPadding(new Insets(10));
            l.setAlignment(Pos.CENTER);
            l.setTextAlignment(TextAlignment.CENTER);
        } else {
            l.setPadding(new Insets(10, 10, 10, right));
        }
    }

    /**
     * On btn export.
     *
     * @param event the event
     */
    @FXML
    void onBtnExport(ActionEvent event) {
        accept = true;
        if (txtVersand.getText().matches("\\d+")) {
            new Thread(createTask(1)).start();
        } else {
            taLog.appendText("Versandnummer eintragen.\n");
            txtVersand.requestFocus();
        }
    }

    private void buildExport() {
        DbBundle lDbBundle = new DbBundle();
        lDbBundle.openAll();

        lDbBundle.dbStaaten.readAll(0);
        final List<EclStaaten> staaten = lDbBundle.dbStaaten.ergebnis();

        BlAktienregisterExport blAktienregisterExport = new BlAktienregisterExport(lDbBundle, staaten);
        CaDateiWrite caDateiWritePost = new CaDateiWrite();
        CaDateiWrite caDateiWriteEmail = new CaDateiWrite();
        int versandNummer = Integer.parseInt(this.txtVersand.getText());
        char trennzeichen = ';';
        String dateiart = ".csv";

        caDateiWritePost.trennzeichen = trennzeichen;
        caDateiWritePost.dateiart = dateiart;
        caDateiWriteEmail.trennzeichen = trennzeichen;
        caDateiWriteEmail.dateiart = dateiart;

        caDateiWritePost.oeffneAktienregister(lDbBundle, "VersandlistePost", txtPfad.getText());
        caDateiWriteEmail.oeffneAktienregister(lDbBundle, "VersandlisteEmail", txtPfad.getText());

        int postCount = 0;
        if (ParamS.clGlobalVar.mandant == 216) {
            postCount = blAktienregisterExport.listeAusgebenVersandku216(caDateiWritePost, 1, versandNummer);
        } else if (ParamS.clGlobalVar.mandant == 340) {
            postCount = blAktienregisterExport.listeAusgebenexportFormat1001(caDateiWritePost, 1, versandNummer);
        } else {
            postCount = blAktienregisterExport.listeAusgebenVersand(caDateiWritePost, 1, versandNummer,
                    cbMitarbeiter.isSelected());
        }

        print(postCount + " Adressen für Postversand");

        int invCount = 0;
        if (ParamS.clGlobalVar.mandant != 276) {
            invCount = blAktienregisterExport.listeAusgebenVersand(caDateiWriteEmail, 2, versandNummer,
                    cbMitarbeiter.isSelected());
            print(invCount + " Adressen für elektronischen Versand (Einladung)");
        }

        caDateiWritePost.schliessen();
        caDateiWriteEmail.schliessen();

        caDateiWritePost.deleteFile(postCount);
        caDateiWriteEmail.deleteFile(invCount);

        print("____________________________________________\n");

        if (blAktienregisterExport.angemeldet && versandNummer == 1) {
            Platform.runLater(() -> CustomAlert.informationAlert("Achtung", "Achtung Anmeldungen!",
                    "Es gibt bereits angemeldete Bestände in der Datenbank\nggf. zurücksetzen."));
        }
    }

    /**
     * Builds the directory chooser.
     *
     * @return the file
     */
    private File buildDirectoryChooser() {

        final DirectoryChooser dc = new DirectoryChooser();
        final String verzBasis = ParamS.paramGeraet.lwPfadKundenordnerBasis;
        final String verzKundenordner = ParamS.eclEmittent.pfadErgKundenOrdner;
        final File directory = new File(verzBasis + verzKundenordner).getParentFile().getParentFile();

        if (directory.exists()) {
            dc.setInitialDirectory(directory);
        }
        return dc.showDialog(eigeneStage);
    }

    /**
     * On create directory.
     *
     * @param event the event
     */
    @FXML
    void onCreateDirectory(ActionEvent event) {

        directory = buildDirectoryChooser();

        if (directory != null) {

            if (!directory.canWrite()) {
                print("Keine Schreibreichte für das Verzeichnis vorhanden.");
            } else if (CustomAlert.confirmAlert("Verzeichnisse erstellen", "Verzeichnisse hier erstellen?",
                    directory.getPath())) {
                accept = true;
                new Thread(createTask(6)).start();
            }
        }
    }

    private void buildCreateDirectory() {

        final String path = directory.getPath() + "\\Namensaktien";

        CaDateiVerwaltung.createDirectory(path + "\\1 Erstversand\\Aktienregister");
        CaDateiVerwaltung.createDirectory(path + "\\1 Erstversand\\Vorlagen");
        CaDateiVerwaltung.createDirectory(path + "\\2 Nachversand\\Aktienregister");
        CaDateiVerwaltung.createDirectory(path + "\\3 Bestandsabgleich");

        print("Namensaktien Ordner wurden erstellt.");

    }

    /**
     * On file copy.
     *
     * @param event the event
     */
    @FXML
    void onFileCopy(ActionEvent event) {

        if (file == null) {
            print("Keine Datei ausgewählt");
            return;
        }

        directory = buildDirectoryChooser();

        if (directory != null) {

            if (!directory.canWrite()) {
                print("Keine Schreibreichte für das Verzeichnis vorhanden.");
            } else if (CustomAlert.confirmAlert("Datei kopieren", "Soll diese Datei kopiert werden?",
                    this.file.getPath() + "\n" + directory.getPath())) {
                accept = true;
                new Thread(createTask(7)).start();
            }
        }
    }

    private void buildFileCopy() {
        try {
            FileUtils.copyFile(file, new File(directory + "\\" + file.getName()));
            print("Datei wurde kopiert.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Do expand.
     *
     * @param event the event
     */
    @FXML
    void doExpand(ActionEvent event) {

        final int initWidth = 450;

        if (layout.getChildren().size() == 1) {
            importBox.prefWidthProperty().bind(layout.widthProperty().divide(7).multiply(3));
            layout.getChildren().addAll(terminBox, settingsBox);
            eigeneStage.setWidth(initWidth + 600);
            btnExpand.setGraphic(new FontIcon(FontAwesomeSolid.ANGLE_DOUBLE_LEFT));
        } else {
            importBox.prefWidthProperty().bind(layout.widthProperty().divide(7).multiply(7));
            layout.getChildren().removeAll(terminBox, settingsBox);
            eigeneStage.setWidth(initWidth);
            btnExpand.setGraphic(new FontIcon(FontAwesomeSolid.ANGLE_DOUBLE_RIGHT));
        }
    }

    /*
     * Funktionen Termin Box
     */

    /**
     * Configure termin box.
     */
    private void configureTerminBox() {

        ObjectActions.evaluateDate(dpHvDatum);
        ObjectActions.evaluateDate(dpErst);
        ObjectActions.evaluateDate(dpNach);
        ObjectActions.evaluateDate(dpLetzter);

        dpHvDatum.valueProperty().addListener((o, oV, nV) -> {
            if (nV != null && dpErst.getValue() == null && dpNach.getValue() == null && dpLetzter.getValue() == null) {

                dpNach.setValue(nV.minusDays(21));
                dpLetzter.setValue(nV.minusDays(7));
            }
        });

        dpNach.valueProperty().addListener((o, oV, nV) -> {
            if (nV != null && dpHvDatum.getValue() == null && dpErst.getValue() == null
                    && dpLetzter.getValue() == null) {

                dpHvDatum.setValue(nV.plusDays(21));
                dpLetzter.setValue(nV.plusDays(14));
            }
        });
    }

    /**
     * On email.
     *
     * @param event the event
     */
    @FXML
    void onEmail(ActionEvent event) {

        String betreff = "Termine für die Aktienregisterabzüge der " + tfEmittent.getText();

        final String name = tfEmittent.getText();
        final String hvDatum = dpHvDatum.getValue().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        final String hvDatum2 = dpHvDatum.getValue().plusDays(1).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        final String erstversand = dpErst.getValue().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        final String erstversand2 = dpErst.getValue().minusDays(1).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        final String nachversand = dpNach.getValue().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        final String letzterAnmeldetag = dpLetzter.getValue().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        final String letzterAnmeldetag2 = dpLetzter.getValue().plusDays(1)
                .format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));

//      @formatter:off
        String text = String.format("body=Hallo zusammen,\n\n"
                + "anbei sende ich Ihnen die Termine und Fristen für die ordentliche Hauptversammlung der %s zu.\n"
                + "Bei der Meldung bezüglich des Umschreibestopps im Aktienregister würde ich Sie bitten, dass Sie diese bei der Clearstream vornehmen.\n"
                + ""
                + "Die Hauptversammlung findet am %s statt.\n"
                + "\n"
                + "1.\tErstversand: Datenabzug am Vormittag des %s mit Stand vom %s, 24 Uhr\n"
                + "\n"
                + "2.\tNachversand: Datenabzug am Vormittag des %s mit Stand vom %s, 0 Uhr\n"
                + "\n"
                + "3.\tBestandsabgleich: Datenabzug am Vormittag des %s mit Stand vom %s, 24 Uhr\n"
                + "\n"
                + "4.\tUmschreibestopp und Schließung Datentransfer zum Aktienregister am %s, 24 Uhr\n"
                + "\n"
                + "5.\tFreigabe Datentransfer zum Aktienregister am %s, 0 Uhr\n",
                name, hvDatum, erstversand, erstversand2, nachversand, nachversand, letzterAnmeldetag2, letzterAnmeldetag, letzterAnmeldetag, hvDatum2);
//      @formatter:on

        betreff = betreff.replace(" ", "%20");
        text = text.replace(" ", "%20");
        text = text.replace("\n", "%0D%0A");
        text = text.replace("\t", "%09");

        Desktop desktop;
        try {
            if (Desktop.isDesktopSupported() && (desktop = Desktop.getDesktop()).isSupported(Desktop.Action.MAIL)) {
                URI mailto;
                mailto = new URI("mailto:" + (tfEmail.getText().isBlank() ? "" : tfEmail.getText()) + "?subject="
                        + betreff + "&" + text);
                desktop.mail(mailto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createCalendarEvent(String bezeichnung, LocalDate date) {
        if (date == null)
            return;

        final String summary = bezeichnung;
        final String start = date.format(DateTimeFormatter.ofPattern("yyyyMMdd")) + "T090000";
        final String ende = date.format(DateTimeFormatter.ofPattern("yyyyMMdd")) + "T093000";
        final String dtstamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss'Z'"));
        final String userDirectory = "C:\\MeetingTools\\Start-Scripts\\Netzwerkeinrichtung\\";

        String[] array = { "BEGIN:VCALENDAR", "VERSION:2.0",
                "PRODID:-//Microsoft Corporation//Outlook 16.0 MIMEDIR//EN", "BEGIN:VTIMEZONE", "TZID:Europe/Berlin",
                "X-LIC-LOCATION:Europe/Berlin", "BEGIN:DAYLIGHT", "TZOFFSETFROM:+0100", "TZOFFSETTO:+0200",
                "TZNAME:CEST", "DTSTART:19700329T020000", "RRULE:FREQ=YEARLY;BYDAY=-1SU;BYMONTH=3", "END:DAYLIGHT",
                "BEGIN:STANDARD", "TZOFFSETFROM:+0200", "TZOFFSETTO:+0100", "TZNAME:CET", "DTSTART:19701025T030000",
                "RRULE:FREQ=YEARLY;BYDAY=-1SU;BYMONTH=10", "END:STANDARD", "END:VTIMEZONE", "BEGIN:VEVENT",
                "ATTENDEE;CN=Aktienregister;mailto:aktienregister@linkmarketservices.eu", ("SUMMARY:" + summary),
                "CLASS:PUBLIC", ("DTSTART;TZID=Europe/Berlin:" + start), ("DTEND;TZID=Europe/Berlin:" + ende),
                ("DTSTAMP:" + dtstamp), "BEGIN:VALARM", "TRIGGER:PT0M", "ACTION:DISPLAY", "DESCRIPTION:Reminder",
                "END:VALARM", "END:VEVENT", "END:VCALENDAR" };

        final CaDateiWrite writer = new CaDateiWrite();
        writer.oeffneNameExplizitOhneBundle(userDirectory + "event.ics");

        for (String str : array) {
            writer.ausgabePlain(str);
            writer.newline();
        }

        writer.schliessen();

        CaDateiVerwaltung.openFileExplorer(userDirectory + "event.ics");
    }

    /*
     * Funktionen Einstellungen
     */

    /**
     * Delete null.
     */
    @FXML
    void onDeleteNull() {
        accept = CustomAlert.confirmAlert("Null Bestände löschen?!",
                "Sollen alle Nullbestände wirklich gelöscht werden?", "");

        new Thread(createTask(2)).start();
    }

    private void buildDeleteNull() {
        DbBundle lDbBundle = new DbBundle();
        lDbBundle.openAllOhneParameterCheck();
        final BvDatenbank bvDatenbank = new BvDatenbank(lDbBundle);

        bvDatenbank.entferne0BestaendeAusAktienregister();
        lDbBundle.dbAktienregister.deleteAll_aktienregisterHistorie();
        taLog.appendText("Null-Bestände wurden entfernt!\n");
        lDbBundle.closeAll();
    }

    /**
     * Update delivery number.
     */
    @FXML
    void onDeliveryNumber() {
        accept = CustomAlert.confirmAlert("Versandnummer auf 1 setzen?",
                "Versandnummer aller Aktionäre wirklich auf 1 setzen?", "");

        new Thread(createTask(3)).start();
    }

    private void buildDeliveryNumber() {
        DbBundle lDbBundle = new DbBundle();
        lDbBundle.openAllOhneParameterCheck();

        lDbBundle.dbAktienregister.updateVersandnummer();

        taLog.appendText("Versandnummer wurde auf 1 gesetzt.\n");
        lDbBundle.closeAll();
    }

    @FXML
    void onEmail2() {
        DbBundle lDbBundle = new DbBundle();
        lDbBundle.openAllOhneParameterCheck();

        final int anz = lDbBundle.dbLoginDaten.read_anzNeuerEmail2();

        lDbBundle.closeAll();

        if (anz > 0) {
            accept = CustomAlert.confirmAlert("Sollen die E-Mails übernommen werden?",
                    "Es wurden " + anz + " neuer E-Mails gefunden.", "E-Mails übernehmen?");

            new Thread(createTask(4)).start();
        } else
            taLog.appendText("Keine neuen E-Mails im Aktienregister.\n");
    }

    private void buildEmail2() {
        DbBundle lDbBundle = new DbBundle();
        lDbBundle.openAllOhneParameterCheck();
        lDbBundle.dbLoginDaten.updateEmail2();
        lDbBundle.closeAll();
        taLog.appendText("E-Mail Addressen übernommen.\n");
    }

    @FXML
    void onPV() {
        if (!txtVersand.getText().matches("\\d+")) {
            taLog.appendText("Versandnummer eintragen.\n");
            txtVersand.requestFocus();
            return;
        }
        accept = CustomAlert.confirmAlert("PV-Kennzeichen entfernen?",
                "PV-Kennzeichen im Feld 'email' bei Versandnummer " + txtVersand.getText() + " entfernen?\n", "");

        new Thread(createTask(5)).start();
    }

    private void buildPV() {
        DbBundle lDbBundle = new DbBundle();
        lDbBundle.openAllOhneParameterCheck();

        lDbBundle.dbAktienregister.updateEmail(Integer.valueOf(txtVersand.getText()));

        taLog.appendText("PV-Kennzeichen wurden entfernt.\n");
        lDbBundle.closeAll();
    }

    @FXML
    void onPasswort() {
        accept = CustomAlert.confirmAlert("Passwörter zurücksetzen?", "Passwörter zurücksetzen?",
                "Sollen die Passwörter - " + cbPasswort.getValue() + " - neu vergeben werden?");

        new Thread(createTask(8)).start();
    }

    private void buildPasswort() {
        blReg = new BlAktienregister(false, new DbBundle());
        blReg.resetPasswort(cbPasswort.getValue());
        print("Es wurden " + number(blReg.count) + " Passwörter neu vergeben.");
    }

    /**
     * On modulauswahl.
     *
     * @param event the event
     */
    @FXML
    void onModulauswahl(ActionEvent event) {

        Stage newStage = new Stage();
        CaIcon.standard(newStage);

        CtrlWaehleModule controllerFenster = new CtrlWaehleModule();
        controllerFenster.init(newStage);
        controllerFenster.eingeloggterUserLogin = ParamS.eclUserLogin;

        CaController caController = new CaController();
        Platform.runLater(() -> eigeneStage.hide());
        caController.open(newStage, controllerFenster,
                "/de/meetingapps/meetingclient/meetingClientDialoge/WaehleModule.fxml", 900, 474, "Modul auswählen",
                false);
    }

    /**
     * On mandant wechseln.
     *
     * @param event the event
     */
    @FXML
    void onMandantWechseln(ActionEvent event) {

        Stage zwischenDialog = new Stage();
        CaIcon.bestandsverwaltung(zwischenDialog);

        CtrlLoginNeu controllerDialog = new CtrlLoginNeu();
        controllerDialog.nurMandantenwechsel = true;

        controllerDialog.init(zwischenDialog);

        CaController caController = new CaController();
        caController.open(zwischenDialog, controllerDialog,
                "/de/meetingapps/meetingclient/meetingClientDialoge/LoginNeu.fxml", 700, 400, "Mandanten-Auswahl",
                true);

        if (controllerDialog.fortsetzen) {

            CALeseParameterNeu caLeseParameterNeu = new CALeseParameterNeu();
            int rc = caLeseParameterNeu.leseHVParameter();
            if (rc < 1) { // Fehlerbehandlung
                CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
                caZeigeHinweis.zeige(eigeneStage, "Systemfehler " + "Fehler " + CaFehler.getFehlertext(rc, 0));
            }
            new CaController().setzeTitel(eigeneStage, eigenerTitel, eigenerTitelMitMandant);
        }
    }

    /**
     * Prints the.
     *
     * @param line the line
     */
    private void print(String line) {
        if (!line.equals(""))
            Platform.runLater(() -> taLog.appendText(line + "\n"));
    }

    /**
     * Inits the.
     *
     * @param pEigeneStage the eigene stage
     */
    public void init(Stage pEigeneStage) {
        this.eigeneStage = pEigeneStage;
    }

    private Task<Void> createTask(int i) {

        Task<Void> task = new Task<Void>() {
            @Override
            public Void call() throws Exception {
                if (accept) {
                    switch (i) {
                    case 1 -> buildExport();
                    case 2 -> buildDeleteNull();
                    case 3 -> buildDeliveryNumber();
                    case 4 -> buildEmail2();
                    case 5 -> buildPV();
                    case 6 -> buildCreateDirectory();
                    case 7 -> buildFileCopy();
                    case 8 -> buildPasswort();
                    default -> throw new IllegalArgumentException("Unexpected value: " + i);
                    }
                } else {
                    print("Aktion abgebrochen.\n");
                }
                return null;
            }
        };

        task.setOnScheduled(e -> loading.setVisible(true));

        task.setOnSucceeded(e -> {
            loading.setVisible(false);
        });

        task.setOnFailed(e -> {
            loading.setVisible(false);

            Throwable throwable = task.getException();
            throwable.printStackTrace();
            e.consume();
            return;
        });
        return task;
    }
}
