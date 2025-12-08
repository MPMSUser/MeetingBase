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
package de.meetingapps.meetingclient.meetingHVMaster;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;

import de.meetingapps.meetingclient.meetingClientAllg.CaMailing;
import de.meetingapps.meetingclient.meetingClientDialoge.CtrlRoot;
import de.meetingapps.meetingclient.meetingClientOberflaechen.ComboBoxZusatz;
import de.meetingapps.meetingclient.meetingClientOberflaechen.CustomAlert;
import de.meetingapps.meetingclient.meetingClientOberflaechen.LoadingScreen;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclMailing;
import de.meetingapps.meetingportal.meetComEntities.EclMailingVariablen;
import de.meetingapps.meetingportal.meetComHVParam.ParamS;
import de.meetingapps.meetingportal.meetComStub.StubMailing;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;

public class CtrlMailingVersand extends CtrlRoot {

    public final int width = 1200;
    public final int height = 700;

    @FXML
    private AnchorPane rootPane;

    @FXML
    private VBox box;

    @FXML
    private TextField tfVersandnummer;

    @FXML
    private ComboBox<EclMailing> cbVorlage;

    @FXML
    private ComboBox<String> cbVersandliste;

    @FXML
    private Button btnAnhang;

    @FXML
    private SplitPane splitPane;

    @FXML
    private AnchorPane paneVorschau;

    @FXML
    private TextField tfBetreff;

    @FXML
    private Button btnAusblenden;

    @FXML
    private WebView webView;

    @FXML
    private TableView<EclMailingVariablen> tableView;

    @FXML
    private StackPane reloadPane;

    @FXML
    private Button btnLadeListe;

    @FXML
    private Button btnSenden;

    @FXML
    private CheckBox checkTestmodus;

    @FXML
    private TextField tfTestAdressen;

    @FXML
    private Label lblAnzahl;

    private StackPane loading;

    private final ArrayList<String> list = new ArrayList<>();
    private final Map<String, String> map = new HashMap<>();

    private List<EclMailing> vorlagen = new ArrayList<>();
    private ArrayList<EclMailingVariablen> versandliste = new ArrayList<>();
    private ArrayList<String> anhangListe = new ArrayList<>();

    private DbBundle dbBundle = new DbBundle();

    private StubMailing stubMailing;
    private EclMailing vorlage;
    
    private int aktionaere = 0;

    private int partSize = 500;
    private int part = 0;
    private List<ArrayList<EclMailingVariablen>> partVersandlisten = new ArrayList<>();

    @FXML
    void initialize() {

        btnAnhang.setGraphic(new FontIcon(FontAwesomeSolid.PLUS));
        btnLadeListe.setGraphic(new FontIcon(FontAwesomeSolid.REDO));

        stubMailing = new StubMailing(false, dbBundle);
        stubMailing.holeEclMailingList();
        vorlagen = stubMailing.rcEclMailingList;

        cbVorlage.getItems().setAll(vorlagen);
        cbVorlage.setConverter(ComboBoxZusatz.convertMailing(cbVorlage));
        cbVorlage.setValue(vorlagen.get(0));

        buildVersandlisten();

        buildTable();

        btnAusblenden.setOnAction(e -> {
            paneVorschau.setVisible(false);
            splitPane.setDividerPositions(0.0);
        });

        btnAnhang.setOnAction(e -> addAnhang());

        cbVorlage.getSelectionModel().selectedItemProperty().addListener((o, oV, nV) -> {
            final EclMailingVariablen eintrag = tableView.getSelectionModel().getSelectedItem();
            if (paneVorschau.isVisible() && eintrag != null) {
                showPreview(eintrag);
            }
        });

        cbVersandliste.getSelectionModel().selectedItemProperty().addListener((o, oV, nV) -> {
            tableView.getItems().clear();
            reloadPane.setVisible(true);
        });

        btnLadeListe.setOnAction(e -> ladeListe());
        btnSenden.setOnAction(e -> onSenden());
        loading = LoadingScreen.createLoadingScreen(rootPane);

        checkTestmodus.selectedProperty().addListener((o, oV, nV) -> {
            tableView.getItems().setAll(nV ? new ArrayList<EclMailingVariablen>() : versandliste);
            tfTestAdressen.setVisible(nV);
        });

        tableView.setRowFactory(tv -> {
            TableRow<EclMailingVariablen> row = new TableRow<>();
            row.setOnMouseClicked(x -> {
                if (x.getClickCount() == 2 && !row.isEmpty()) {
                    showPreview(row.getItem());
                }
            });
            return row;
        });
    }

    private Object addAnhang() {
        return null;
    }

    private void prepareListe() {

        dbBundle.openAll();

        versandliste = dbBundle.dbJoined.read_MailingVersandliste(map.get(cbVersandliste.getValue()),
                Integer.valueOf(tfVersandnummer.getText()));
        
        aktionaere = versandliste.size();

        dbBundle.closeAll();
        
        List<EclMailingVariablen> tmpVersandliste = new ArrayList<>();

        for (EclMailingVariablen x : versandliste) {
            if (!x.email.isBlank() && !x.email2.isBlank() && !x.email.toLowerCase().equals(x.email2.toLowerCase())) {
                tmpVersandliste.add(new EclMailingVariablen(x.aktienregisterIdent, x.email2, "", x.kennung, x.passwort,
                        x.anrede, x.nameKomplett, x.titel, x.vorname, x.nachname, x.link));
            } else if (x.email.isBlank()) {
                x.setEmail(x.getEmail2());
            }
            x.setEmail2("");
        }
        versandliste.addAll(tmpVersandliste);
        versandliste.sort(Comparator.comparing(EclMailingVariablen::getKennung));
    }

    private void ladeListe() {

        prepareListe();

        lblAnzahl.setText("Aktionäre: " + aktionaere + " - E-Mails: " + versandliste.size());
        tableView.getItems().addAll(versandliste);

        reloadPane.setVisible(false);

    }

    private void onSenden() {
        final String titel = "Bitte bestätigen";
        final String header = checkTestmodus.isSelected() ? "Testmodus" : "Produktion!";
        final String text = checkTestmodus.isSelected() ? "Emails an Testverteiler senden?"
                : "Emails an ausgewählte Versandliste senden?";

        if (CustomAlert.confirmAlert(titel, header, text)) {
            if (checkTestmodus.isSelected()) {
                new Thread(testVersand()).start();
            } else {

                part = 0;
                partVersandlisten.clear();

                prepareListe();

                int size = versandliste.size();
                int start = 0;
                int end = 0;

                vorlage = readVorlage();

                while (end < size) {

                    end = ((start + partSize) < size) ? start + partSize : size;
                    partVersandlisten.add(new ArrayList<>(versandliste.subList(start, end)));

                    start = end;
                }
                new Thread(prodVersand()).start();
            }
        }
    }

    private Task<Void> testVersand() {
        Task<Void> task = new Task<Void>() {
            protected Void call() throws Exception {
                if (checkTestmodus.isSelected() && tableView.getItems().isEmpty()
                        && !tfTestAdressen.getText().isBlank()) {
                    buildAdressen();
                }

                if (!tableView.getItems().isEmpty()) {

                    vorlage = readVorlage();

                    CaMailing caMailing = new CaMailing();
                    caMailing.sendMails(vorlage, new ArrayList<>(tableView.getItems()), anhangListe);
                }
                return null;
            }
        };
        task.setOnScheduled(e -> loading.setVisible(true));
        task.setOnSucceeded(e -> loading.setVisible(false));
        task.setOnFailed(e -> loading.setVisible(false));

        return task;

    }

    private Task<Void> prodVersand() {
        Task<Void> task = new Task<Void>() {
            protected Void call() throws Exception {

                //                EclMailingVariablen tmpVar = partVersandlisten.get(part).get(0);
                //                System.out.println(tmpVar.toString());
                //                System.out.println(partVersandlisten.get(part).size());

                CaMailing caMailing = new CaMailing();
                caMailing.sendMails(vorlage, partVersandlisten.get(part++), anhangListe);

                return null;
            }
        };
        task.setOnScheduled(e -> loading.setVisible(true));
        task.setOnFailed(e -> {
            loading.setVisible(false);
            System.err.println("The task failed with the following exception:");
            task.getException().printStackTrace(System.err);

        });
        task.setOnSucceeded(e -> {
            loading.setVisible(false);
            System.out.println("Teil " + part + " von " + partVersandlisten.size());
            if (part < partVersandlisten.size()) {

                final String title = "Aktueller Stand";
                final String header = "Möchten Sie fortfahren?";
                final String text = "Weiter mit Teil: " + (part + 1) + " von " + partVersandlisten.size();

                CustomAlert.informationAlert(title, header, text);

                new Thread(prodVersand()).start();
            }
        });
        return task;
    }

    private void configAnhangButton(Button btn) {

        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setGraphic(new FontIcon(FontAwesomeSolid.TIMES_CIRCLE));
        btn.setPrefHeight(35.0);
        VBox.setMargin(btn, new Insets(20, 0, 0, 0));

    }

    private void buildVersandlisten() {

        // Wird hier bei jeder Liste benoetigt
        String zusatz = "AND lo.eVersandRegistrierung = 99 AND lo.emailBestaetigt = 1 ";

        if (ParamS.param.paramPortal.emailVersandZweitEMailAusRegister == 1) {
            zusatz = "AND lo.eVersandRegistrierung = 99 AND (lo.emailBestaetigt = 1 OR (lo.eMail2FuerVersand != '' AND lo.email2Bestaetigt = 2)) and are.email != 'PV' ";
        }

        addVersandliste("Alle Registrierten", zusatz);
        addVersandliste("Bekanntes Passwort", zusatz + "AND lo.eigenesPasswort = 99 ");
        addVersandliste("Neues Passwort", zusatz + "AND lo.eigenesPasswort < 98 ");
        addVersandliste("Alle - deutsch",
                zusatz + "AND (are.staatId = 0 OR are.staatId = 1 OR are.staatId = 14 OR are.staatId = 56) ");
        addVersandliste("Alle - englisch",
                zusatz + "AND !(are.staatId = 0 OR are.staatId = 1 OR are.staatId = 14 OR are.staatId = 56) ");
        addVersandliste("Alle - angemeldet", zusatz + "AND are.personNatJur != 0 ");

        cbVersandliste.getItems().addAll(list);
    }

    private void addVersandliste(String vorlage, String where) {

        list.add(vorlage);
        map.put(vorlage, where);
    }

    private void buildTable() {

        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        final Set<String> set = new LinkedHashSet<>(
                List.of("nameKomplett", "kennung", "passwort", "email", "anrede", "vorname", "nachname", "link"));

        for (String str : set) {

            TableColumn<EclMailingVariablen, ?> col = new TableColumn<>(str.toUpperCase(Locale.GERMANY));

            col.setCellValueFactory(new PropertyValueFactory<>(str));

            tableView.getColumns().add(col);
        }
    }

    @FXML
    private void onKeyAdressen(KeyEvent event) {

        if (event.getCode() == KeyCode.ENTER && !tfTestAdressen.getText().isBlank()) {

            buildAdressen();
            tfTestAdressen.clear();

        } else if (event.getCode() == KeyCode.ESCAPE) {
            tfTestAdressen.clear();
        }
    }

    private void buildAdressen() {

        String[] testVersand = tfTestAdressen.getText().trim().split(";");

        int count = 1;

        for (String str : testVersand) {

            final EclMailingVariablen eintrag = new EclMailingVariablen();
            eintrag.setKennung("*Kennung*");
            eintrag.setPasswort("*Passwort*");
            eintrag.setAnrede("*Anrede+Nachname*");
            eintrag.setNameKomplett("*NameKomplett*");
            eintrag.setVorname("*Vorname*");
            eintrag.setNachname("*Nachname*");
            eintrag.setAktienregisterIdent(count++);
            eintrag.setEmail(str.trim());

            tableView.getItems().add(eintrag);

        }
        Platform.runLater(() -> lblAnzahl.setText("Einträge: " + tableView.getItems().size()));
    }

    private void showPreview(EclMailingVariablen eintrag) {

        if (!paneVorschau.isVisible()) {

            paneVorschau.setVisible(true);
            splitPane.setDividerPositions(0.7);

        }

        final EclMailing vorlage = readVorlage();

        Map<String, String> map = CaMailing.createPersonalization(eintrag);

        for (var entry : map.entrySet()) {
            // System.out.println(entry.getKey() + " / " + entry.getValue());

            vorlage.htmlMail = vorlage.htmlMail.replace("${" + entry.getKey() + "}", entry.getValue());

        }
        tfBetreff.setText(vorlage.betreff);
        webView.getEngine().loadContent(vorlage.htmlMail, "text/html");

    }

    private EclMailing readVorlage() {
        stubMailing.holeEclMailingList();
        return stubMailing.rcEclMailingList.stream().filter(e -> e.mailingIdent == cbVorlage.getValue().mailingIdent)
                .findFirst().orElse(null);
    }
}
