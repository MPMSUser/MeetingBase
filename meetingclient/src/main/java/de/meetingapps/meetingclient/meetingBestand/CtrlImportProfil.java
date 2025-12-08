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
package de.meetingapps.meetingclient.meetingBestand;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;

import de.meetingapps.meetingclient.meetingClientDialoge.CtrlRoot;
import de.meetingapps.meetingclient.meetingClientOberflaechen.ObjectActions;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclInhaberImportAnmeldedaten;
import de.meetingapps.meetingportal.meetComEntities.EclImportProfil;
import de.meetingapps.meetingportal.meetComEntities.EclWortmeldetischAktion;
import de.meetingapps.meetingportal.meetComEntities.EclWortmeldetischFolgeStatus;
import de.meetingapps.meetingportal.meetComEntities.EclWortmeldetischStatus;
import de.meetingapps.meetingportal.meetComEntities.EclWortmeldetischStatusWeiterleitung;
import de.meetingapps.meetingportal.meetComEntities.EclWortmeldetischView;
import de.meetingapps.meetingportal.meetComEntities.EclWortmeldetischViewStatus;
import de.meetingapps.meetingportal.meetComHVParam.ParamS;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * The Class CtrlImportProfil.
 */
public class CtrlImportProfil extends CtrlRoot {

    /** The tf file. */
    @FXML
    private TextField tfFile;

    /** The cb database. */
    @FXML
    private ChoiceBox<String> cbDatabase;

    /** The cb profile. */
    @FXML
    private ChoiceBox<EclImportProfil> cbProfile;

    /** The btn clear profile. */
    @FXML
    private Button btnClearProfile;

    /** The btn file. */
    @FXML
    private Button btnFile;

    /** The btn check. */
    @FXML
    private Button btnCheck;

    /** The btn new. */
    @FXML
    private Button btnNew;

    /** The grid pane. */
    @FXML
    private GridPane gridPane;

    /** The btn close. */
    @FXML
    private Button btnClose;

    /** The tf name. */
    @FXML
    private TextField tfName;

    /** The btn save. */
    @FXML
    private Button btnSave;

    /** The file. */
    private File file = null;

    /** The db bundle. */
    private DbBundle dbBundle = null;

    /** The x. */
    private int x = 0;

    /** The y. */
    private int y = 0;

    /** The encoding. */
    private Charset encoding = Charset.forName("ISO-8859-1");

    /** The call. */
    private int call = 1;

    /** The databases. */
    private final String[] databases = { "EclInhaberImportAnmeldedaten", "EclWortmeldetischView", "EclWortmeldetischViewStatus",
            "EclWortmeldetischStatusWeiterleitung", "EclWortmeldetischFolgeStatus", "EclWortmeldetischAktion",
            "EclWortmeldetischStatus" };

    /** The map. */
    private Map<ChoiceBox<String>, ComboBox<String>> map;

    /** The import profil. */
    private EclImportProfil importProfil = null;

    /** The linked list. */
    private List<String[]> linkedList = null;

    /** The directory. */
    private String directory = "C:\\";

    /** The disabled fields. */
    private final Set<String> disabledFields = Set.of("ident", "referenzEKNr", "gattungId", "datei", "dateikuerzel",
            "EkComparator", "statusArray", "summeOffsetInAnzeigeView", "summeOffsetInAnzeigeVersammlungsleiter");

    /** The database fields. */
    private List<String> databaseFields = new ArrayList<>();;

    /**
     * Initialize.
     */
    @FXML
    private void initialize() {

        cbDatabase.setItems(FXCollections.observableArrayList(databases));
        cbDatabase.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            getDatabaseFields(newValue);
        }));

        cbDatabase.setValue(cbDatabase.getItems().get(1));

        btnClearProfile.setGraphic(new FontIcon(FontAwesomeSolid.TRASH));

        dbBundle = new DbBundle();
        dbBundle.openAllOhneParameterCheck();

        dbBundle.dbImportProfil.createTable();
        dbBundle.dbImportProfil.updateTable();
        List<EclImportProfil> list = dbBundle.dbImportProfil.readAll();

        cbProfile.getItems().addAll(list);

        cbProfile.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            importProfil = newValue;
            createGridPane(false);
            checkButtons();
        }));

        dbBundle.closeAll();
    }

    /**
     * Gets the database fields.
     *
     * @param cl the cl
     */
    private void getDatabaseFields(String cl) {

        Class<?> c = null;
        Object o = null;

        switch (cl) {
        case "EclInhaberImportAnmeldedaten":
            c = EclInhaberImportAnmeldedaten.class;
            o = new EclInhaberImportAnmeldedaten();
            break;
        case "EclWortmeldetischView":
            c = EclWortmeldetischView.class;
            o = new EclWortmeldetischView();
            break;
        case "EclWortmeldetischViewStatus":
            c = EclWortmeldetischViewStatus.class;
            o = new EclWortmeldetischViewStatus();
            break;
        case "EclWortmeldetischStatusWeiterleitung":
            c = EclWortmeldetischStatusWeiterleitung.class;
            o = new EclWortmeldetischStatusWeiterleitung();
            break;
        case "EclWortmeldetischFolgeStatus":
            c = EclWortmeldetischFolgeStatus.class;
            o = new EclWortmeldetischFolgeStatus();
            break;
        case "EclWortmeldetischAktion":
            c = EclWortmeldetischAktion.class;
            o = new EclWortmeldetischAktion();
            break;
        case "EclWortmeldetischStatus":
            c = EclWortmeldetischStatus.class;
            o = new EclWortmeldetischStatus();
            break;
        default:
            return;
        }

        databaseFields.clear();

        for (Field field : c.getDeclaredFields()) {
            field.setAccessible(true);
            if (!disabledFields.contains(field.getName())) {
                if (field.getType().isArray()) {
                    databaseFields.add(field.getName());
                    try {
                        int anz = Array.getLength(field.get(o));
                        for (int i = 0; i < anz; i++) {
                            databaseFields.add(field.getName() + i);
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
                databaseFields.add(field.getName());
            }
        }
    }

    /**
     * On file.
     */
    @FXML
    private void onFile() {

        final String verzBasis = ParamS.paramGeraet.lwPfadKundenordnerBasis + ParamS.eclEmittent.pfadErgKundenOrdner;
        final Boolean directoryCheck = new File(verzBasis).exists();

        final FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(
                new File(directory.equals("C:\\") && directoryCheck ? verzBasis.replace("\\PDF", "") : directory));

        file = fileChooser.showOpenDialog(eigeneStage);
        if (file != null) {
            try {
                tfFile.setText(file.getCanonicalPath());
                checkButtons();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * On check.
     */
    @FXML
    private void onCheck() {

        /*
         * Aktuelles Profil laden!
         */
        EclImportProfil profil = currentProfile();
        Map<String, String> profilMap = profil.createMap();

        call = 1;
        linkedList = createInsertList(encoding, call);
        if (call > 3)
            System.out.println("Fehler Format");

        List<EclInhaberImportAnmeldedaten> anmList = new ArrayList<>();

        List<String> columnsFile = new LinkedList<>(Arrays.asList(linkedList.get(0)));
        List<String> columnsProfile = Arrays.asList(profil.getDateiFeld());

        Boolean comp = true;
        for (var str : columnsFile)
            if (!columnsProfile.contains(str.trim()))
                comp = false;

        if (comp) {
            linkedList.remove(0);

            for (var line : linkedList) {
                Map<String, String> dataMap = new HashMap<>();
                int i = 0;
                for (var column : columnsFile) {
                    final String db = profilMap.get(column.trim());
                    final String lineTrim = line[i++].trim();
                    if (db != null && !lineTrim.isBlank()) {
                        dataMap.put(db, lineTrim);
                    }
                }
                anmList.add(EclInhaberImportAnmeldedaten.createAnmeldedaten(dataMap));
            }
        }

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        for (var anm : anmList) {

            Set<ConstraintViolation<EclInhaberImportAnmeldedaten>> violations = validator.validate(anm);

            if (violations.size() > 0) {

                System.out.println("Es wurden Fehler in der Datei erkannt:");

                for (ConstraintViolation<EclInhaberImportAnmeldedaten> violation : violations) {
                    System.err.println(violation.getMessage() + " - " + violation.getInvalidValue());
                }
            }
        }
    }

    /**
     * On new.
     */
    @FXML
    private void onNew() {

        final String datenbank = (cbDatabase.getValue() == null) ? "" : cbDatabase.getValue();
        cbProfile.setValue(null);

        if (file != null && !datenbank.isEmpty()) {

            String[] dateiFeld = new String[EclImportProfil.anzFelder];
            String[] datenbankFeld = new String[EclImportProfil.anzFelder];

            call = 1;
            linkedList = createInsertList(encoding, call);
            if (call > 3)
                System.out.println("Fehler Format");

            if (!linkedList.isEmpty()) {

                for (var i = 0; i < linkedList.get(0).length; i++) {
                    dateiFeld[i] = linkedList.get(0)[i].trim().isBlank() ? null : linkedList.get(0)[i].trim();
                }

                importProfil = new EclImportProfil(0, "", "", null, dateiFeld, datenbankFeld);

                createGridPane(true);

            } else
                System.out.println("Liste ist leer");
        } else
            System.out.println("Datei oder Datenbank nicht ausgewählt");

    }

    /**
     * Creates the grid pane.
     *
     * @param autofill the autofill
     */
    private void createGridPane(Boolean autofill) {

        if (importProfil != null) {

            map = new LinkedHashMap<>();
            gridPane.getChildren().clear();

            System.out.println(Arrays.toString(importProfil.getDateiFeld()));

            x = -1;
            y = 0;

            for (int i = 0; i < importProfil.getDateiFeld().length; i++) {
                if (importProfil.getDateiFeld()[i] == null)
                    break;

                gridPane.add(newColumn(importProfil.getDateiFeld()[i], importProfil.getDatenbankFeld()[i], i, autofill),
                        x, y);
            }
            tfName.setText(importProfil.getName());

        } else {
            System.out.println("Profil ist null");
            gridPane.getChildren().clear();
        }
    }

    //    private LinkedList<String[]> fileValues(File file) {
    //
    //        /*
    //         * .csv oder .xls/-x
    //         */
    //
    //        LinkedList<String[]> list = new LinkedList<>();
    //
    //        System.out.println(file.getName());
    //        System.out.println(file.getName().matches(".+(\\.csv)$"));
    //
    //        if (file.getName().matches(".+(\\.csv)$")) {
    //
    //            try (BufferedReader reader = new BufferedReader(new FileReader(tfFile.getText()))) {
    //                String line = null;
    //
    //                while ((line = reader.readLine()) != null) {
    //
    //                    list.add(line.split(";"));
    //
    //                }
    //
    //            } catch (Exception e) {
    //                e.printStackTrace();
    //            }
    //        } else
    //            System.out.println("keine csv");
    //
    //        return list;
    //    }

    /**
     * New column.
     *
     * @param fileValue     the file value
     * @param databaseValue the database value
     * @param i             the i
     * @param fill          the fill
     * @return the v box
     */
    private VBox newColumn(String fileValue, String databaseValue, int i, Boolean fill) {

        nextValue();

        final Label lblFileColumn = new Label("Datei Spalte - " + i);

        final ChoiceBox<String> cbFileColumn = new ChoiceBox<String>(FXCollections.observableArrayList(fileValue));
        cbFileColumn.setValue(fileValue);
        cbFileColumn.setMaxWidth(Double.MAX_VALUE);

        final Label lblTargetColumn = new Label("Ziel Spalte - " + i);
        lblTargetColumn.setPadding(new Insets(10, 0, 0, 0));

        final ComboBox<String> cbTargetColumn = new ComboBox<String>(FXCollections.observableArrayList(databaseFields));
        if (fill)
            cbTargetColumn.setValue(cbTargetColumn.getItems().stream()
                    .filter(e -> e.toLowerCase().equals(fileValue.toLowerCase())).findFirst().orElse(null));
        else
            cbTargetColumn.setValue(databaseValue);

        cbTargetColumn.setMaxWidth(Double.MAX_VALUE);
        ObjectActions.filterComboBox(cbTargetColumn, null);

        map.put(cbFileColumn, cbTargetColumn);

        return new VBox(lblFileColumn, cbFileColumn, lblTargetColumn, cbTargetColumn);
    }

    /**
     * On clear profile.
     *
     * @param event the event
     */
    @FXML
    private void onClearProfile(ActionEvent event) {
        cbProfile.setValue(null);
    }

    /**
     * Current profile.
     *
     * @return the ecl import profil
     */
    private EclImportProfil currentProfile() {

        String[] dateiFeld = new String[EclImportProfil.anzFelder];
        String[] datenbankFeld = new String[EclImportProfil.anzFelder];

        Map<String, String> tmpMap = new HashMap<>();

        int i = 0;
        for (var entry : map.entrySet()) {
            dateiFeld[i] = entry.getKey().getValue();
            datenbankFeld[i] = entry.getValue().getValue();
            tmpMap.put(entry.getKey().getValue(), entry.getValue().getValue());
            i++;
        }

        if (importProfil != null)
            return new EclImportProfil(importProfil.getIdent(), tfName.getText(), cbDatabase.getValue(), null,
                    dateiFeld, datenbankFeld);
        else
            return new EclImportProfil(0, tfName.getText(), cbDatabase.getValue(), null, dateiFeld, datenbankFeld);
    }

    /**
     * On save.
     *
     * @param event the event
     */
    @FXML
    private void onSave(ActionEvent event) {
        if (tfName.getText() != null && !tfName.getText().isBlank()) {

            dbBundle.openAll();
            if (cbProfile.getValue() == null) {
                //                insert
                dbBundle.dbImportProfil.insert(currentProfile());
            } else {
                //                update
                dbBundle.dbImportProfil.update(currentProfile());
            }
            dbBundle.closeAll();
        }
    }

    /**
     * On close.
     *
     * @param event the event
     */
    @FXML
    private void onClose(ActionEvent event) {
        eigeneStage.hide();
    }

    /**
     * Check buttons.
     */
    private void checkButtons() {
        btnCheck.setDisable(!(file != null && cbProfile.getValue() != null));
        btnNew.setDisable(file == null);
    }

    /**
     * Next value.
     */
    private void nextValue() {
        if (x < 7)
            x++;
        else {
            x = 0;
            y++;
        }
    }

    /**
     * Creates the insert list.
     *
     * @param cs   the cs
     * @param call the call
     * @return the linked list
     */
    /*
     * Funktionaller gestalten!
     */
    private LinkedList<String[]> createInsertList(Charset cs, int call) {

        LinkedList<String[]> list = new LinkedList<>();

        Charset currentSet = encoding;

        Boolean check = false;

        final Charset ibmCharset = Charset.forName("IBM850");
        final Charset isoCharset = Charset.forName("ISO-8859-1");
        final Charset utfCharset = Charset.forName("UTF-8");

        int ibmCount = 0;
        int isoCount = 0;
        int utfCount = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(tfFile.getText(), currentSet))) {

            String line = null;

            while ((line = reader.readLine()) != null) {

                while (line.length() < 500)
                    line += " ";

                check = new String(line.getBytes(ibmCharset), ibmCharset).equals(line);
                ibmCount += check ? 1 : 0;

                check = new String(line.getBytes(isoCharset), isoCharset).equals(line);
                isoCount += check ? 1 : 0;

                check = new String(line.getBytes(utfCharset), utfCharset).equals(line);
                utfCount += check ? 1 : 0;

                list.add(line.split(";"));

            }

            if (ibmCount == utfCount && ibmCount == isoCount) {
                check = true;
            } else if (ibmCount < utfCount && ibmCount < isoCount) {
                check = false;
                currentSet = ibmCharset;
            } else if (isoCount < utfCount && isoCount < ibmCount) {
                check = false;
                currentSet = isoCharset;
            } else if (isoCount == ibmCount) {
                check = false;
                currentSet = isoCharset;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!check) {
            call++;
        } else {
            System.out.println("Dateiformat: " + currentSet.toString());
        }
        if (call > 3)
            return list;

        return check ? list : createInsertList(currentSet, call);
    }

    /**
     * Inits the.
     *
     * @param pEigeneStage the eigene stage
     */
    public void init(Stage pEigeneStage) {
        eigeneStage = pEigeneStage;
    }

}
