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
package de.meetingapps.meetingclient.meetingClientDialoge;

import java.net.URL;
import java.util.ResourceBundle;

import de.meetingapps.meetingportal.meetComEntities.EclUserLogin;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * The Class CtrlWaehleModuleRaw.
 */
public class CtrlWaehleModuleRaw {

    /** The resources. */
    @FXML
    private ResourceBundle resources;

    /** The location. */
    @FXML
    private URL location;

    /** The btn beenden. */
    @FXML
    private Button btnBeenden;

    /** The scrpn module. */
    @FXML
    private ScrollPane scrpnModule;

    /** The grpn buttons. */
    GridPane grpnButtons = null;

    /**
     * Initialize.
     */
    @FXML
    void initialize() {
        assert btnBeenden != null : "fx:id=\"btnBeenden\" was not injected: check your FXML file 'WaehleModule.fxml'.";
        assert scrpnModule != null : "fx:id=\"scrpnModule\" was not injected: check your FXML file 'WaehleModule.fxml'.";

        /************* Ab hier individuell ********************************************/

        scrpnModule.setContent(null);

        grpnButtons = new GridPane();
        grpnButtons.setVgap(5);
        grpnButtons.setHgap(5);

        lfd = 0;

        Button btnModuleUpdate = new Button("Datenbank-Update prüfen/Durchführen");
        btnModuleUpdate.setOnAction(e -> {
            clickedModuleUpdate(e);
        });
        ermittleOffset();
        grpnButtons.add(btnModuleUpdate, x, y);

        Button btnModuleNeueDatenbank = new Button("Datenbank neu initialisieren");
        btnModuleNeueDatenbank.setOnAction(e -> {
            clickedModuleNeueDatenbank(e);
        });
        ermittleOffset();
        grpnButtons.add(btnModuleNeueDatenbank, x, y);

        Button btnModuleUpdate2000a = new Button("Datenbank-Update 2000a");
        btnModuleUpdate2000a.setOnAction(e -> {
            clickedModuleUpdate2000a(e);
        });
        ermittleOffset();
        grpnButtons.add(btnModuleUpdate2000a, x, y);

        Button btnModuleUpdate2000b = new Button("Datenbank-Update 2000b - parameterSet (Mandantenübergreifend");
        btnModuleUpdate2000b.setOnAction(e -> {
            clickedModuleUpdate2000b(e);
        });
        ermittleOffset();
        grpnButtons.add(btnModuleUpdate2000b, x, y);

        Button btnModuleUpdate2000c = new Button("Datenbank-Update 2000c - tbl_drucklauf je Mandant");
        btnModuleUpdate2000c.setOnAction(e -> {
            clickedModuleUpdate2000c(e);
        });
        ermittleOffset();
        grpnButtons.add(btnModuleUpdate2000c, x, y);

        Button btnModuleUpdate2000d = new Button(
                "Datenbank-Update 2000d - tbl_insti (Mandantenübergreifend), tbl_instiBestandsZuordnung, tbl_suchlaufBegriffe, tbl_suchlaufDefinition, tbl_suchlaufErgebnis je Mandant");
        btnModuleUpdate2000d.setOnAction(e -> {
            clickedModuleUpdate2000d(e);
        });
        ermittleOffset();
        grpnButtons.add(btnModuleUpdate2000d, x, y);

        Button btnModuleUpdate2000e = new Button(
                "Datenbank-Update 2000e - tbl_instiEmittentenMitZuordnung + Nachrichten (jeweils Mandantenübergreifend)");
        btnModuleUpdate2000e.setOnAction(e -> {
            clickedModuleUpdate2000e(e);
        });
        ermittleOffset();
        grpnButtons.add(btnModuleUpdate2000e, x, y);

        Button btnModuleUpdate2000f = new Button("Datenbank-Update 2000f - tbl_aktienregisterLogin erweitern");
        btnModuleUpdate2000f.setOnAction(e -> {
            clickedModuleUpdate2000f(e);
        });
        ermittleOffset();
        grpnButtons.add(btnModuleUpdate2000f, x, y);

        Button btnModuleUpdate2000g = new Button("Module Update2020g- tbl_aktienregisterErgaenzung anlegen");
        btnModuleUpdate2000g.setOnAction(e -> {
            clickedModuleUpdate2000g(e);
        });
        ermittleOffset();
        grpnButtons.add(btnModuleUpdate2000g, x, y);

        Button btnModuleUpdate2000h = new Button("Module Update2020h- tbl_aktienregisterErgaenzung erweitern");
        btnModuleUpdate2000h.setOnAction(e -> {
            clickedModuleUpdate2000h(e);
        });
        ermittleOffset();
        grpnButtons.add(btnModuleUpdate2000h, x, y);

        Button btnModuleUpdate2000i = new Button(
                "Module Update2020i- tbl_veranstaltung + tbl_abstimmungsVorschlagEmpfehlung anlegen");
        btnModuleUpdate2000i.setOnAction(e -> {
            clickedModuleUpdate2000i(e);
        });
        ermittleOffset();
        grpnButtons.add(btnModuleUpdate2000i, x, y);

        Button btnModulePersonenprognose = new Button("Datenbank Update - tbl_personenprognose");
        btnModulePersonenprognose.setOnAction(e -> clickedModulePersonenprognose(e));
        ermittleOffset();
        grpnButtons.add(btnModulePersonenprognose, x, y);

        Button btnModuleUpdate2000j = new Button("Module Update2020j- tbl_aufgaben (global) erweitern um drucklaufnr");
        btnModuleUpdate2000j.setOnAction(e -> {
            clickedModuleUpdate2000j(e);
        });
        ermittleOffset();
        grpnButtons.add(btnModuleUpdate2000j, x, y);

        Button btnModuleUpdate2000k = new Button(
                "Module Update2020k- tbl_portaltexte (global+mandant) erweitern um release");
        btnModuleUpdate2000k.setOnAction(e -> {
            clickedModuleUpdate2000k(e);
        });
        ermittleOffset();
        grpnButtons.add(btnModuleUpdate2000k, x, y);

        scrpnModule.setContent(grpnButtons);

        Button btnModuleUpdate2000l = new Button("Module Update2020l- tbl_fragen, tbl_mitteilungen (mandant) anlegen");
        btnModuleUpdate2000l.setOnAction(e -> {
            clickedModuleUpdate2000l(e);
        });
        ermittleOffset();
        grpnButtons.add(btnModuleUpdate2000l, x, y);

        Button btnModuleUpdate2000m = new Button(
                "Module Update2020m- tbl_fragen, tbl_mitteilungen (mandant) Text-Länge ändern");
        btnModuleUpdate2000m.setOnAction(e -> {
            clickedModuleUpdate2000m(e);
        });
        ermittleOffset();
        grpnButtons.add(btnModuleUpdate2000m, x, y);

        Button btnModuleUpdate2000n = new Button("Module Update2020n- tbl_ktracking (mandant) ändern");
        btnModuleUpdate2000n.setOnAction(e -> {
            clickedModuleUpdate2000n(e);
        });
        ermittleOffset();
        grpnButtons.add(btnModuleUpdate2000n, x, y);

        Button btnModuleUpdate2021a = new Button("Module Update2021a- tbl_loginDaten (mandant) neu");
        btnModuleUpdate2021a.setOnAction(e -> {
            clickedModuleUpdate2021a(e);
        });
        ermittleOffset();
        grpnButtons.add(btnModuleUpdate2021a, x, y);

        Button btnModuleUpdate2021b = new Button("Module Update2021b- tbl_mitteilung (mandant) neu");
        btnModuleUpdate2021b.setOnAction(e -> {
            clickedModuleUpdate2021b(e);
        });
        ermittleOffset();
        grpnButtons.add(btnModuleUpdate2021b, x, y);

        Button btnModuleUpdate2021Aa = new Button("Module Update2021Aa- Aktienregister, Mandant 134");
        btnModuleUpdate2021Aa.setOnAction(e -> {
            clickedModuleUpdate2021Aa(e);
        });
        ermittleOffset();
        grpnButtons.add(btnModuleUpdate2021Aa, x, y);

        Button btnModuleUpdate2021c = new Button("Module Update2021c- Abstimmungen");
        btnModuleUpdate2021c.setOnAction(e -> {
            clickedModuleUpdate2021c(e);
        });
        ermittleOffset();
        grpnButtons.add(btnModuleUpdate2021c, x, y);

        Button btnModuleUpdate2021d = new Button("Module Update202d- mitteilungBestand");
        btnModuleUpdate2021d.setOnAction(e -> {
            clickedModuleUpdate2021d(e);
        });
        ermittleOffset();
        grpnButtons.add(btnModuleUpdate2021d, x, y);

        Button btnModuleUpdate2021e = new Button("Module Update2021e- staaten und Meldung - Index");
        btnModuleUpdate2021e.setOnAction(e -> {
            clickedModuleUpdate2021e(e);
        });
        ermittleOffset();
        grpnButtons.add(btnModuleUpdate2021e, x, y);

        Button btnModuleUpdate2021f = new Button("Module Update2021f- reload portaltexte initialisieren");
        btnModuleUpdate2021f.setOnAction(e -> {
            clickedModuleUpdate2021f(e);
        });
        ermittleOffset();
        grpnButtons.add(btnModuleUpdate2021f, x, y);

        Button btnModuleUpdate2021g = new Button("Module Update2021g- Permanent-Portal initialisieren");
        btnModuleUpdate2021g.setOnAction(e -> {
            clickedModuleUpdate2021g(e);
        });
        ermittleOffset();
        grpnButtons.add(btnModuleUpdate2021g, x, y);

        Button btnModuleUpdate2022b = new Button(
                "Module Update2022b- New Isin, Update aktienregister, Update emittenten (und lokal)");
        btnModuleUpdate2022b.setOnAction(e -> {
            clickedModuleUpdate2022b(e);
        });
        ermittleOffset();
        grpnButtons.add(btnModuleUpdate2022b, x, y);

        Button btnModuleUpdate2022c = new Button("Module Update2022c- New UserLogin");
        btnModuleUpdate2022c.setOnAction(e -> {
            clickedModuleUpdate2022c(e);
        });
        ermittleOffset();
        grpnButtons.add(btnModuleUpdate2022c, x, y);

        Button btnModuleUpdate2022d = new Button("Module Update2022d- New AbstimmungMeldungSperre");
        btnModuleUpdate2022d.setOnAction(e -> {
            clickedModuleUpdate2022d(e);
        });
        ermittleOffset();
        grpnButtons.add(btnModuleUpdate2022d, x, y);

        Button btnModuleUpdate2023a = new Button("Module Update2023a- ku178 und ku310");
        btnModuleUpdate2023a.setOnAction(e -> {
            clickedModuleUpdate2023a(e);
        });
        ermittleOffset();
        grpnButtons.add(btnModuleUpdate2023a, x, y);

        Button btnModuleUpdate2023b = new Button("Module Update2023b- abstimmungku310");
        btnModuleUpdate2023b.setOnAction(e -> {
            clickedModuleUpdate2023b(e);
        });
        ermittleOffset();
        grpnButtons.add(btnModuleUpdate2023b, x, y);

        Button btnModuleUpdate2024a = new Button("Module Update2024a- EindeutigeKennung");
        btnModuleUpdate2024a.setOnAction(e -> {
            clickedModuleUpdate2024a(e);
        });
        ermittleOffset();
        grpnButtons.add(btnModuleUpdate2024a, x, y);

        Button btnModuleUpdate2024b = new Button("Module Update2024b");
        btnModuleUpdate2024b.setOnAction(e -> {
            clickedModuleUpdate2024b(e);
        });
        ermittleOffset();
        grpnButtons.add(btnModuleUpdate2024b, x, y);

        Button btnModuleUpdate2025a = new Button("Module Update2025a- 2025");
        btnModuleUpdate2025a.setOnAction(e -> {
            clickedModuleUpdate2025a(e);
        });
        ermittleOffset();
        grpnButtons.add(btnModuleUpdate2025a, x, y);

        scrpnModule.setContent(grpnButtons);

    }

    /** ************Logik******************. */

    private int lfd = 0;

    /** The y. */
    private int y = 0;

    /** The x. */
    private int x = 0;

    /**
     * Ermittle offset.
     */
    private void ermittleOffset() {
        y = lfd / 2;
        x = lfd % 2;
        lfd++;
    }

    /**
     * Btn beenden clicked.
     *
     * @param event the event
     */
    @FXML
    void btnBeendenClicked(ActionEvent event) {
        fortsetzen = false;
        eigeneStage.hide();
    }

    /**
     * Clicked module update.
     *
     * @param event the event
     */
    @FXML
    void clickedModuleUpdate(ActionEvent event) {
        fortsetzen = true;
        ausgewaehltesModul = 1;
        eigeneStage.hide();

    }

    /**
     * Clicked module neue datenbank.
     *
     * @param event the event
     */
    @FXML
    void clickedModuleNeueDatenbank(ActionEvent event) {
        fortsetzen = true;
        ausgewaehltesModul = 2;
        eigeneStage.hide();

    }

    /**
     * Clicked module update 2000 a.
     *
     * @param event the event
     */
    @FXML
    void clickedModuleUpdate2000a(ActionEvent event) {
        fortsetzen = true;
        ausgewaehltesModul = 3;
        eigeneStage.hide();

    }

    /**
     * Clicked module update 2000 b.
     *
     * @param event the event
     */
    @FXML
    void clickedModuleUpdate2000b(ActionEvent event) {
        fortsetzen = true;
        ausgewaehltesModul = 4;
        eigeneStage.hide();

    }

    /**
     * Clicked module update 2000 c.
     *
     * @param event the event
     */
    @FXML
    void clickedModuleUpdate2000c(ActionEvent event) {
        fortsetzen = true;
        ausgewaehltesModul = 5;
        eigeneStage.hide();

    }

    /**
     * Clicked module update 2000 d.
     *
     * @param event the event
     */
    @FXML
    void clickedModuleUpdate2000d(ActionEvent event) {
        fortsetzen = true;
        ausgewaehltesModul = 6;
        eigeneStage.hide();

    }

    /**
     * Clicked module update 2000 e.
     *
     * @param event the event
     */
    @FXML
    void clickedModuleUpdate2000e(ActionEvent event) {
        fortsetzen = true;
        ausgewaehltesModul = 7;
        eigeneStage.hide();

    }

    /**
     * Clicked module update 2000 f.
     *
     * @param event the event
     */
    @FXML
    void clickedModuleUpdate2000f(ActionEvent event) {
        fortsetzen = true;
        ausgewaehltesModul = 8;
        eigeneStage.hide();

    }

    /**
     * Clicked module update 2000 g.
     *
     * @param event the event
     */
    @FXML
    void clickedModuleUpdate2000g(ActionEvent event) {
        fortsetzen = true;
        ausgewaehltesModul = 9;
        eigeneStage.hide();

    }

    /**
     * Clicked module update 2000 h.
     *
     * @param event the event
     */
    @FXML
    void clickedModuleUpdate2000h(ActionEvent event) {
        fortsetzen = true;
        ausgewaehltesModul = 10;
        eigeneStage.hide();

    }

    /**
     * Clicked module update 2000 i.
     *
     * @param event the event
     */
    @FXML
    void clickedModuleUpdate2000i(ActionEvent event) {
        fortsetzen = true;
        ausgewaehltesModul = 11;
        eigeneStage.hide();

    }

    /**
     * Clicked module personenprognose.
     *
     * @param event the event
     */
    @FXML
    void clickedModulePersonenprognose(ActionEvent event) {
        fortsetzen = true;
        ausgewaehltesModul = 12;
        eigeneStage.hide();
    }

    /**
     * Clicked module update 2000 j.
     *
     * @param event the event
     */
    @FXML
    void clickedModuleUpdate2000j(ActionEvent event) {
        fortsetzen = true;
        ausgewaehltesModul = 13;
        eigeneStage.hide();

    }

    /**
     * Clicked module update 2000 k.
     *
     * @param event the event
     */
    @FXML
    void clickedModuleUpdate2000k(ActionEvent event) {
        fortsetzen = true;
        ausgewaehltesModul = 14;
        eigeneStage.hide();

    }

    /**
     * Clicked module update 2000 l.
     *
     * @param event the event
     */
    @FXML
    void clickedModuleUpdate2000l(ActionEvent event) {
        fortsetzen = true;
        ausgewaehltesModul = 15;
        eigeneStage.hide();

    }

    /**
     * Clicked module update 2000 m.
     *
     * @param event the event
     */
    @FXML
    void clickedModuleUpdate2000m(ActionEvent event) {
        fortsetzen = true;
        ausgewaehltesModul = 16;
        eigeneStage.hide();

    }

    /**
     * Clicked module update 2000 n.
     *
     * @param event the event
     */
    @FXML
    void clickedModuleUpdate2000n(ActionEvent event) {
        fortsetzen = true;
        ausgewaehltesModul = 17;
        eigeneStage.hide();

    }

    /**
     * Clicked module update 2021 a.
     *
     * @param event the event
     */
    @FXML
    void clickedModuleUpdate2021a(ActionEvent event) {
        fortsetzen = true;
        ausgewaehltesModul = 18;
        eigeneStage.hide();

    }

    /**
     * Clicked module update 2021 b.
     *
     * @param event the event
     */
    @FXML
    void clickedModuleUpdate2021b(ActionEvent event) {
        fortsetzen = true;
        ausgewaehltesModul = 19;
        eigeneStage.hide();

    }

    /**
     * Clicked module update 2021 aa.
     *
     * @param event the event
     */
    @FXML
    void clickedModuleUpdate2021Aa(ActionEvent event) {
        fortsetzen = true;
        ausgewaehltesModul = 20;
        eigeneStage.hide();

    }

    /**
     * Clicked module update 2021 c.
     *
     * @param event the event
     */
    @FXML
    void clickedModuleUpdate2021c(ActionEvent event) {
        fortsetzen = true;
        ausgewaehltesModul = 21;
        eigeneStage.hide();

    }

    /**
     * Clicked module update 2021 d.
     *
     * @param event the event
     */
    @FXML
    void clickedModuleUpdate2021d(ActionEvent event) {
        fortsetzen = true;
        ausgewaehltesModul = 22;
        eigeneStage.hide();

    }

    /**
     * Clicked module update 2021 e.
     *
     * @param event the event
     */
    @FXML
    void clickedModuleUpdate2021e(ActionEvent event) {
        fortsetzen = true;
        ausgewaehltesModul = 23;
        eigeneStage.hide();

    }

    /**
     * Clicked module update 2021 f.
     *
     * @param event the event
     */
    @FXML
    void clickedModuleUpdate2021f(ActionEvent event) {
        fortsetzen = true;
        ausgewaehltesModul = 24;
        eigeneStage.hide();

    }

    /**
     * Clicked module update 2021 g.
     *
     * @param event the event
     */
    @FXML
    void clickedModuleUpdate2021g(ActionEvent event) {
        fortsetzen = true;
        ausgewaehltesModul = 25;
        eigeneStage.hide();

    }

    /**
     * Clicked module update 2022 b.
     *
     * @param event the event
     */
    @FXML
    void clickedModuleUpdate2022b(ActionEvent event) {
        fortsetzen = true;
        ausgewaehltesModul = 26;
        eigeneStage.hide();

    }

    /**
     * Clicked module update 2022 c.
     *
     * @param event the event
     */
    @FXML
    void clickedModuleUpdate2022c(ActionEvent event) {
        fortsetzen = true;
        ausgewaehltesModul = 27;
        eigeneStage.hide();

    }

    /**
     * Clicked module update 2022 d.
     *
     * @param event the event
     */
    @FXML
    void clickedModuleUpdate2022d(ActionEvent event) {
        fortsetzen = true;
        ausgewaehltesModul = 28;
        eigeneStage.hide();

    }

    /**
     * Clicked module update 2023 a.
     *
     * @param event the event
     */
    @FXML
    void clickedModuleUpdate2023a(ActionEvent event) {
        fortsetzen = true;
        ausgewaehltesModul = 29;
        eigeneStage.hide();

    }

    /**
     * Clicked module update 2023 b.
     *
     * @param event the event
     */
    @FXML
    void clickedModuleUpdate2023b(ActionEvent event) {
        fortsetzen = true;
        ausgewaehltesModul = 30;
        eigeneStage.hide();

    }

    /**
     * Clicked module update 2024 a.
     *
     * @param event the event
     */
    @FXML
    void clickedModuleUpdate2024a(ActionEvent event) {
        fortsetzen = true;
        ausgewaehltesModul = 31;
        eigeneStage.hide();

    }

    @FXML
    void clickedModuleUpdate2024b(ActionEvent event) {
        fortsetzen = true;
        ausgewaehltesModul = 32;
        eigeneStage.hide();

    }

    @FXML
    void clickedModuleUpdate2025a(ActionEvent event) {
        fortsetzen = true;
        ausgewaehltesModul = 33;
        eigeneStage.hide();

    }

    
    /** The eigene stage. */
    private Stage eigeneStage;

    /** Return-Werte für aufrufende Routine. */
    public boolean fortsetzen = false;

    /** The ausgewaehltes modul. */
    public int ausgewaehltesModul = -1;

    /** The eingeloggter user login. */
    public EclUserLogin eingeloggterUserLogin = null;

    /**
     * Inits the.
     *
     * @param pEigeneStage the eigene stage
     */
    public void init(Stage pEigeneStage) {
        eigeneStage = pEigeneStage;

    }

}
