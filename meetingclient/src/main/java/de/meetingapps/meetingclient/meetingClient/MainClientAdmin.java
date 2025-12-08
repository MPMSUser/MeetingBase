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
package de.meetingapps.meetingclient.meetingClient;

import java.io.IOException;

import de.meetingapps.meetingclient.meetingClientDialoge.CaZeigeHinweis;
import de.meetingapps.meetingclient.meetingClientDialoge.CtrlWaehleModuleRaw;
import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComBVerwaltung.BvDatenbank;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclEmittenten;
import de.meetingapps.meetingportal.meetComEntities.EclGeraetKlasseSetZuordnung;
import de.meetingapps.meetingportal.meetComEntities.EclGeraeteSet;
import de.meetingapps.meetingportal.meetComEntities.EclUserLogin;
import de.meetingapps.meetingportal.meetComHVParam.ClGlobalVar;
import de.meetingapps.meetingportal.meetComHVParam.HVParam;
import de.meetingapps.meetingportal.meetComHVParam.ParamGeraet;
import de.meetingapps.meetingportal.meetComHVParam.ParamS;
import de.meetingapps.meetingportal.meetComHVParam.ParamServer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**"Raw-Programmteil" - ohn²e jegliche Parameter, nur Datenbankzugriff.
 * Dient rein zum Erstinitialisieren und zum Updaten der Datenbank.
 *
 * Arbeitsplatz ist immer 1, Zugriff immer über Datenbank.
 *
 */
public class MainClientAdmin extends Application {

    /** The start args. */
    static String[] startArgs = null;

    /** The primary stage. */
    private Stage primaryStage;

    /**
     * Start.
     *
     * @param pPrimaryStage the primary stage
     */
    @Override
    public void start(Stage pPrimaryStage) {

        primaryStage = pPrimaryStage;

        /*Parameter "Raw" erzeugen.*/

        ParamS.setClGlobalVar(new ClGlobalVar());
        ParamS.setEclEmittent(new EclEmittenten());
        ParamS.setEclGeraeteSet(new EclGeraeteSet());
        ParamS.setEclGeraetKlasseSetZuordnung(new EclGeraetKlasseSetZuordnung());
        ParamS.setParam(new HVParam());
        ParamS.setParamGeraet(new ParamGeraet());
        ParamS.setParamServer(new ParamServer());
        ParamS.setEclUserLogin(new EclUserLogin());

        ParamS.clGlobalVar.datenbankPfadNr = 2; //Lokale Datenbank
//                          ParamS.clGlobalVar.datenbankPfadNr=0; //ExternerHoster-Produktion 1
//                  		ParamS.clGlobalVar.datenbankPfadNr=5; //ExternerHoster-Produktion 2
//                          ParamS.clGlobalVar.datenbankPfadNr=6; //ExternerHoster-Produktion 3
//              ParamS.clGlobalVar.datenbankPfadNr=7; //ExternerHoster-archiv
//              ParamS.clGlobalVar.datenbankPfadNr=1; //Intern Test

        Stage zwischenDialog = new Stage();

        CtrlWaehleModuleRaw controllerDialog = new CtrlWaehleModuleRaw();

        controllerDialog.init(zwischenDialog);

        FXMLLoader loader1 = new FXMLLoader(
                getClass().getResource("/de/meetingapps/meetingclient/meetingClientDialoge/WaehleModuleRaw.fxml"));
        loader1.setController(controllerDialog);
        Parent mainPane1 = null;
        try {
            mainPane1 = (Parent) loader1.load();
        } catch (IOException e) {
            CaBug.drucke("CaProgrammStart.wahleModulAus 002");
            e.printStackTrace();
        }
        Scene scene1 = new Scene(mainPane1, 1200, 600);
        zwischenDialog.setTitle("Modul auswählen");
        zwischenDialog.setScene(scene1);
        zwischenDialog.initModality(Modality.APPLICATION_MODAL);
        zwischenDialog.showAndWait();

        boolean bRC = controllerDialog.fortsetzen;
        if (bRC == false) {
            return;
        }
        int zuStartendesModul = controllerDialog.ausgewaehltesModul;

        switch (zuStartendesModul) {
        case 1: {
            starteModuleUpdate();
            break;
        }
        case 2: {
            starteModuleNeueDatenbank();
            break;
        }
        case 3: {
            starteModuleUpdate2020a();
            break;
        }
        case 4: {
            starteModuleUpdate2020b();
            break;
        }
        case 5: {
            starteModuleUpdate2020c();
            break;
        }
        case 6: {
            starteModuleUpdate2020d();
            break;
        }
        case 7: {
            starteModuleUpdate2020e();
            break;
        }
        case 8: {
            starteModuleUpdate2020f();
            break;
        }
        case 9: {
            starteModuleUpdate2020g();
            break;
        }
        case 10: {
            starteModuleUpdate2020h();
            break;
        }
        case 11: {
            starteModuleUpdate2020i();
            break;
        }
        case 12:
            starteModuleUpdatePersonenprognose();
            break;
        case 13: {
            starteModuleUpdate2020j();
            break;
        }
        case 14: {
            starteModuleUpdate2020k();
            break;
        }
        case 15: {
            starteModuleUpdate2020l();
            break;
        }
        case 16: {
            starteModuleUpdate2020m();
            break;
        }
        case 17: {
            starteModuleUpdate2020n();
            break;
        }
        case 18: {
            starteModuleUpdate2021a();
            break;
        }
        case 19:
            starteModuleUpdate2021b();
            break;
        case 20:
            starteModuleUpdate2021Aa();
            break;
        case 21:
            starteModuleUpdate2021c();
            break;
        case 22:
            starteModuleUpdate2021d();
            break;
        case 23:
            starteModuleUpdate2021e();
            break;
        case 24:
            starteModuleUpdate2021f();
            break;
        case 25:
            starteModuleUpdate2021g();
            break;
        case 26:
            starteModuleUpdate2022b();
            break;
        case 27:
            starteModuleUpdate2022c();
            break;
        case 28:
            starteModuleUpdate2022d();
            break;
        case 29:
            starteModuleUpdate2023a();
            break;
        case 30:
            starteModuleUpdate2023b();
            break;
        case 31:
            starteModuleUpdate2024a();
            break;
        case 32:
            starteModuleUpdate2024b();
            break;
        case 33:
            starteModuleUpdate2025a();
            break;
        }
    }

    /**
     * Starte module update.
     */
    private void starteModuleUpdate() {

        CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
        caZeigeHinweis.zeige(primaryStage,
                "Module Update. Bitte auf Weiter klicken, und dann einfach warten .... wird schon gehn :-)");
        System.out.println("Start Update");
        DbBundle lDbBundle = new DbBundle();
        lDbBundle.openAllOhneParameterCheck();
        BvDatenbank bvDatenbank = new BvDatenbank(lDbBundle);

        int rc = bvDatenbank.liefereTableVersion();
        System.out.println("Table-Version=" + bvDatenbank.aktuellVorhandeneTableVersion + " Returnwert=" + rc + " "
                + CaFehler.getFehlertext(rc, 0));
        if (rc == CaFehler.fTableInfoNichtLesbar || rc == CaFehler.fTablesMuessenUpgedatetWerden) {
            bvDatenbank.updateTableVersion();
            System.out.println("Datenbank wurde upgedatet");
        }

        System.out.println("Fertig");
        caZeigeHinweis.zeige(primaryStage, "So, durch ...");

        lDbBundle.closeAll();

    }

    /**
     * Starte module neue datenbank.
     */
    private void starteModuleNeueDatenbank() {

        CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
        caZeigeHinweis.zeige(primaryStage, "Noch nicht implementiert");
    }

    /**
     * Starte module update 2020 a.
     */
    private void starteModuleUpdate2020a() {

        CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
        caZeigeHinweis.zeige(primaryStage,
                "Module Update2020a. Bitte auf Weiter klicken, und dann einfach warten .... wird schon gehn :-)");
        System.out.println("Start Update");
        DbBundle lDbBundle = new DbBundle();
        lDbBundle.openAllOhneParameterCheck();
        BvDatenbank bvDatenbank = new BvDatenbank(lDbBundle);

        bvDatenbank.updateTableVersion2020A();
        System.out.println("Datenbank wurde upgedatet");

        System.out.println("Fertig");
        caZeigeHinweis.zeige(primaryStage, "So, durch ...");

        lDbBundle.closeAll();

    }

    /**
     * Starte module update 2020 b.
     */
    private void starteModuleUpdate2020b() {

        CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
        caZeigeHinweis.zeige(primaryStage,
                "Module Update2020b - parameterSet (mandantenunabhängig). Bitte auf Weiter klicken, und dann einfach warten .... wird schon gehn :-)");
        System.out.println("Start Update");
        DbBundle lDbBundle = new DbBundle();
        lDbBundle.openAllOhneParameterCheck();
        BvDatenbank bvDatenbank = new BvDatenbank(lDbBundle);

        bvDatenbank.updateTableVersion2020B();
        System.out.println("Datenbank wurde upgedatet");

        System.out.println("Fertig");
        caZeigeHinweis.zeige(primaryStage, "So, durch ...");

        lDbBundle.closeAll();

    }

    /**
     * Starte module update 2020 c.
     */
    private void starteModuleUpdate2020c() {

        CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
        caZeigeHinweis.zeige(primaryStage,
                "Module Update2020c - tbl_drucklauf (mandantenabhängig). Bitte auf Weiter klicken, und dann einfach warten .... wird schon gehn :-)");
        System.out.println("Start Update");
        DbBundle lDbBundle = new DbBundle();
        lDbBundle.openAllOhneParameterCheck();
        BvDatenbank bvDatenbank = new BvDatenbank(lDbBundle);

        bvDatenbank.updateTableVersion2020C();
        System.out.println("Datenbank wurde upgedatet");

        System.out.println("Fertig");
        caZeigeHinweis.zeige(primaryStage, "So, durch ...");

        lDbBundle.closeAll();

    }

    /**
     * Starte module update 2020 d.
     */
    private void starteModuleUpdate2020d() {

        CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
        caZeigeHinweis.zeige(primaryStage,
                "Module Update2020d - tbl_userlogin (insti), tbl_insti (Mandantenübergreifend), tbl_instiBestandsZuordnung, tbl_suchlaufBegriffe, tbl_suchlaufDefinition, tbl_suchlaufErgebnis je Mandant. Bitte auf Weiter klicken, und dann einfach warten .... wird schon gehn :-)");
        System.out.println("Start Update");
        DbBundle lDbBundle = new DbBundle();
        lDbBundle.openAllOhneParameterCheck();
        lDbBundle.openWeitere();
        BvDatenbank bvDatenbank = new BvDatenbank(lDbBundle);

        bvDatenbank.updateTableVersion2020D();
        System.out.println("Datenbank wurde upgedatet");

        System.out.println("Fertig");
        caZeigeHinweis.zeige(primaryStage, "So, durch ...");

        lDbBundle.closeAll();

    }

    /**
     * Starte module update 2020 e.
     */
    private void starteModuleUpdate2020e() {

        CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
        caZeigeHinweis.zeige(primaryStage,
                "Module Update2020e - tbl_instiEmittentenMitZuordnung + Nachrichten (Jeweils Mandantenübergreifend). Bitte auf Weiter klicken, und dann einfach warten .... wird schon gehn :-)");
        System.out.println("Start Update");
        DbBundle lDbBundle = new DbBundle();
        lDbBundle.openAllOhneParameterCheck();
        lDbBundle.openWeitere();
        BvDatenbank bvDatenbank = new BvDatenbank(lDbBundle);

        bvDatenbank.updateTableVersion2020E();
        System.out.println("Datenbank wurde upgedatet");

        System.out.println("Fertig");
        caZeigeHinweis.zeige(primaryStage, "So, durch ...");

        lDbBundle.closeAll();

    }

    /**
     * Starte module update 2020 f.
     */
    private void starteModuleUpdate2020f() {

        CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
        caZeigeHinweis.zeige(primaryStage,
                "Module Update2020f - tbl_aktienregisterLogin erweitern. Bitte auf Weiter klicken, und dann einfach warten .... wird schon gehn :-)");
        System.out.println("Start Update");
        DbBundle lDbBundle = new DbBundle();
        lDbBundle.openAllOhneParameterCheck();
        lDbBundle.openWeitere();
        BvDatenbank bvDatenbank = new BvDatenbank(lDbBundle);

        bvDatenbank.updateTableVersion2020F();
        System.out.println("Datenbank wurde upgedatet");

        System.out.println("Fertig");
        caZeigeHinweis.zeige(primaryStage, "So, durch ...");

        lDbBundle.closeAll();

    }

    /**
     * Starte module update 2020 g.
     */
    private void starteModuleUpdate2020g() {

        CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
        caZeigeHinweis.zeige(primaryStage,
                "Module Update2020g- tbl_aktienregisterErgaenzung anlegen. Bitte auf Weiter klicken, und dann einfach warten .... wird schon gehn :-)");
        System.out.println("Start Update");
        DbBundle lDbBundle = new DbBundle();
        lDbBundle.openAllOhneParameterCheck();
        lDbBundle.openWeitere();
        BvDatenbank bvDatenbank = new BvDatenbank(lDbBundle);

        bvDatenbank.updateTableVersion2020g();
        System.out.println("Datenbank wurde upgedatet");

        System.out.println("Fertig");
        caZeigeHinweis.zeige(primaryStage, "So, durch ...");

        lDbBundle.closeAll();

    }

    /**
     * Starte module update 2020 h.
     */
    private void starteModuleUpdate2020h() {

        CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
        caZeigeHinweis.zeige(primaryStage,
                "Module Update2020h- tbl_aktienregisterErgaenzung erweitern. Bitte auf Weiter klicken, und dann einfach warten .... wird schon gehn :-)");
        System.out.println("Start Update");
        DbBundle lDbBundle = new DbBundle();
        lDbBundle.openAllOhneParameterCheck();
        lDbBundle.openWeitere();
        BvDatenbank bvDatenbank = new BvDatenbank(lDbBundle);

        bvDatenbank.updateTableVersion2020h();
        System.out.println("Datenbank wurde upgedatet");

        System.out.println("Fertig");
        caZeigeHinweis.zeige(primaryStage, "So, durch ...");

        lDbBundle.closeAll();

    }

    /**
     * Starte module update 2020 i.
     */
    private void starteModuleUpdate2020i() {

        CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
        caZeigeHinweis.zeige(primaryStage,
                "Module Update2020i- tbl_veranstaltung + tbl_abstimmungsVorschlagEmpfehlung anlegen. Bitte auf Weiter klicken, und dann einfach warten .... wird schon gehn :-)");
        System.out.println("Start Update");
        DbBundle lDbBundle = new DbBundle();
        lDbBundle.openAllOhneParameterCheck();
        lDbBundle.openWeitere();
        BvDatenbank bvDatenbank = new BvDatenbank(lDbBundle);

        bvDatenbank.updateTableVersion2020i();
        System.out.println("Datenbank wurde upgedatet");

        System.out.println("Fertig");
        caZeigeHinweis.zeige(primaryStage, "So, durch ...");

        lDbBundle.closeAll();

    }

    /**
     * Starte module update personenprognose.
     */
    private void starteModuleUpdatePersonenprognose() {

        CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
        caZeigeHinweis.zeige(primaryStage, "tbl_personenprognose wird für alle vorhandenen Mandanten erstellt");
        System.out.println("Start Update");

        DbBundle lDbBundle = new DbBundle();
        lDbBundle.openAll();
        lDbBundle.openWeitere();

        BvDatenbank bvDatenbank = new BvDatenbank(lDbBundle);

        bvDatenbank.updateMandantenPersonenprognose();
        System.out.println("Datenbank wurde upgedatet");

        System.out.println("Fertig");
        caZeigeHinweis.zeige(primaryStage, "So, durch ...");

        lDbBundle.closeAll();
    }

    /**
     * Starte module update 2020 j.
     */
    private void starteModuleUpdate2020j() {

        CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
        caZeigeHinweis.zeige(primaryStage,
                "Module Update2020j- tbl_aufgaben (global) erweitern um drucklaufnr. Bitte auf Weiter klicken, und dann einfach warten .... wird schon gehn :-)");
        System.out.println("Start Update");
        DbBundle lDbBundle = new DbBundle();
        lDbBundle.openAllOhneParameterCheck();
        lDbBundle.openWeitere();
        BvDatenbank bvDatenbank = new BvDatenbank(lDbBundle);

        bvDatenbank.updateTableVersion2020j();
        System.out.println("Datenbank wurde upgedatet");

        System.out.println("Fertig");
        caZeigeHinweis.zeige(primaryStage, "So, durch ...");

        lDbBundle.closeAll();

    }

    /**
     * Starte module update 2020 k.
     */
    private void starteModuleUpdate2020k() {

        CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
        caZeigeHinweis.zeige(primaryStage,
                "Module Update2020k- tbl_portaltexte (global+mandant) erweitern um release. Bitte auf Weiter klicken, und dann einfach warten .... wird schon gehn :-)");
        System.out.println("Start Update");
        DbBundle lDbBundle = new DbBundle();
        lDbBundle.openAllOhneParameterCheck();
        lDbBundle.openWeitere();
        BvDatenbank bvDatenbank = new BvDatenbank(lDbBundle);

        bvDatenbank.updateTableVersion2020k();
        System.out.println("Datenbank wurde upgedatet");

        System.out.println("Fertig");
        caZeigeHinweis.zeige(primaryStage, "So, durch ...");

        lDbBundle.closeAll();

    }

    /**
     * Starte module update 2020 l.
     */
    private void starteModuleUpdate2020l() {

        CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
        caZeigeHinweis.zeige(primaryStage,
                "Module Update2020l- tbl_fragen / tbl_emittenten (mandant) anlegen. Bitte auf Weiter klicken, und dann einfach warten .... wird schon gehn :-)");
        System.out.println("Start Update");
        DbBundle lDbBundle = new DbBundle();
        lDbBundle.openAllOhneParameterCheck();
        lDbBundle.openWeitere();
        BvDatenbank bvDatenbank = new BvDatenbank(lDbBundle);

        bvDatenbank.updateTableVersion2020l();
        System.out.println("Datenbank wurde upgedatet");

        System.out.println("Fertig");
        caZeigeHinweis.zeige(primaryStage, "So, durch ...");

        lDbBundle.closeAll();

    }

    /**
     * Starte module update 2020 m.
     */
    private void starteModuleUpdate2020m() {

        CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
        caZeigeHinweis.zeige(primaryStage,
                "Module Update2020m- tbl_fragen / tbl_emittenten (mandant) Text-Länge ändern. Bitte auf Weiter klicken, und dann einfach warten .... wird schon gehn :-)");
        System.out.println("Start Update");
        DbBundle lDbBundle = new DbBundle();
        lDbBundle.openAllOhneParameterCheck();
        lDbBundle.openWeitere();
        BvDatenbank bvDatenbank = new BvDatenbank(lDbBundle);

        bvDatenbank.updateTableVersion2020m();
        System.out.println("Datenbank wurde upgedatet");

        System.out.println("Fertig");
        caZeigeHinweis.zeige(primaryStage, "So, durch ...");

        lDbBundle.closeAll();

    }

    /**
     * Starte module update 2020 n.
     */
    private void starteModuleUpdate2020n() {

        CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
        caZeigeHinweis.zeige(primaryStage,
                "Module Update2020n- tbl_ktracking (mandant). Bitte auf Weiter klicken, und dann einfach warten .... wird schon gehn :-)");
        System.out.println("Start Update");
        DbBundle lDbBundle = new DbBundle();
        lDbBundle.openAllOhneParameterCheck();
        lDbBundle.openWeitere();
        BvDatenbank bvDatenbank = new BvDatenbank(lDbBundle);

        bvDatenbank.updateTableVersion2020n();
        System.out.println("Datenbank wurde upgedatet");

        System.out.println("Fertig");
        caZeigeHinweis.zeige(primaryStage, "So, durch ...");

        lDbBundle.closeAll();

    }

    /**
     * Starte module update 2021 a.
     */
    private void starteModuleUpdate2021a() {

        CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
        caZeigeHinweis.zeige(primaryStage,
                "Module Update2021a- tbl_logindaten (mandant) neu. Bitte auf Weiter klicken, und dann einfach warten .... wird schon gehn :-)");
        System.out.println("Start Update");
        DbBundle lDbBundle = new DbBundle();
        lDbBundle.openAllOhneParameterCheck();
        lDbBundle.openWeitere();
        BvDatenbank bvDatenbank = new BvDatenbank(lDbBundle);

        bvDatenbank.updateTableVersion2021a();
        System.out.println("Datenbank wurde upgedatet");

        System.out.println("Fertig");
        caZeigeHinweis.zeige(primaryStage, "So, durch ...");

        lDbBundle.closeAll();

    }

    /**
     * Starte module update 2021 b.
     */
    private void starteModuleUpdate2021b() {

        CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
        caZeigeHinweis.zeige(primaryStage,
                "Module Update2021b- tbl_mitteilung etc. (mandant) neu. Bitte auf Weiter klicken, und dann einfach warten .... wird schon gehn :-)");
        System.out.println("Start Update");
        DbBundle lDbBundle = new DbBundle();
        lDbBundle.openAllOhneParameterCheck();
        lDbBundle.openWeitere();
        BvDatenbank bvDatenbank = new BvDatenbank(lDbBundle);

        bvDatenbank.updateTableVersion2021b();
        System.out.println("Datenbank wurde upgedatet");

        System.out.println("Fertig");
        caZeigeHinweis.zeige(primaryStage, "So, durch ...");

        lDbBundle.closeAll();

    }

    /**
     * Starte module update 2021 c.
     */
    private void starteModuleUpdate2021c() {

        CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
        caZeigeHinweis.zeige(primaryStage,
                "Module Update2021c- tbl_abstimmungen. Bitte auf Weiter klicken, und dann einfach warten .... wird schon gehn :-)");
        System.out.println("Start Update");
        DbBundle lDbBundle = new DbBundle();
        lDbBundle.openAllOhneParameterCheck();
        lDbBundle.openWeitere();
        BvDatenbank bvDatenbank = new BvDatenbank(lDbBundle);

        bvDatenbank.updateTableVersion2021c();
        System.out.println("Datenbank wurde upgedatet");

        System.out.println("Fertig");
        caZeigeHinweis.zeige(primaryStage, "So, durch ...");

        lDbBundle.closeAll();

    }

    /**
     * Starte module update 2021 d.
     */
    private void starteModuleUpdate2021d() {

        CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
        caZeigeHinweis.zeige(primaryStage,
                "Module Update2021d- tbl_mitteilungBestand. Bitte auf Weiter klicken, und dann einfach warten .... wird schon gehn :-)");
        System.out.println("Start Update");
        DbBundle lDbBundle = new DbBundle();
        lDbBundle.openAllOhneParameterCheck();
        lDbBundle.openWeitere();
        BvDatenbank bvDatenbank = new BvDatenbank(lDbBundle);

        bvDatenbank.updateTableVersion2021d();
        System.out.println("Datenbank wurde upgedatet");

        System.out.println("Fertig");
        caZeigeHinweis.zeige(primaryStage, "So, durch ...");

        lDbBundle.closeAll();

    }

    /**
     * Starte module update 2021 e.
     */
    private void starteModuleUpdate2021e() {

        CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
        caZeigeHinweis.zeige(primaryStage,
                "Module Update2021e- tbl_staaten und tbl_meldungen - Indexe. Bitte auf Weiter klicken, und dann einfach warten .... wird schon gehn :-)");
        System.out.println("Start Update");
        DbBundle lDbBundle = new DbBundle();
        lDbBundle.openAllOhneParameterCheck();
        lDbBundle.openWeitere();
        BvDatenbank bvDatenbank = new BvDatenbank(lDbBundle);

        bvDatenbank.updateTableVersion2021e();
        System.out.println("Datenbank wurde upgedatet");

        System.out.println("Fertig");
        caZeigeHinweis.zeige(primaryStage, "So, durch ...");

        lDbBundle.closeAll();

    }

    /**
     * Starte module update 2021 f.
     */
    private void starteModuleUpdate2021f() {

        CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
        caZeigeHinweis.zeige(primaryStage,
                "Module Update2021f- reload für portaltexte initialisieren. Auf Weiter klicken, und dann einfach warten .... wird schon gehn :-)");
        System.out.println("Start Update");
        DbBundle lDbBundle = new DbBundle();
        lDbBundle.openAllOhneParameterCheck();
        lDbBundle.openWeitere();
        BvDatenbank bvDatenbank = new BvDatenbank(lDbBundle);

        bvDatenbank.updateTableVersion2021f();
        System.out.println("Datenbank wurde upgedatet");

        System.out.println("Fertig");
        caZeigeHinweis.zeige(primaryStage, "So, durch ...");

        lDbBundle.closeAll();

    }

    /**
     * Starte module update 2021 g.
     */
    private void starteModuleUpdate2021g() {

        CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
        caZeigeHinweis.zeige(primaryStage,
                "Module Update2021g- Permanentportal initialisieren. Auf Weiter klicken, und dann einfach warten .... wird schon gehn :-)");
        System.out.println("Start Update");
        DbBundle lDbBundle = new DbBundle();
        lDbBundle.openAllOhneParameterCheck();
        lDbBundle.openWeitere();
        BvDatenbank bvDatenbank = new BvDatenbank(lDbBundle);

        bvDatenbank.updateTableVersion2021g();
        System.out.println("Datenbank wurde upgedatet");

        System.out.println("Fertig");
        caZeigeHinweis.zeige(primaryStage, "So, durch ...");

        lDbBundle.closeAll();

    }

    /**
     * Starte module update 2022 b.
     */
    private void starteModuleUpdate2022b() {

        CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
        caZeigeHinweis.zeige(primaryStage,
                "Module Update2022b Programmversion 2022 - Aktienregister, Isin, Emittenten. Auf Weiter klicken, und dann einfach warten .... wird schon gehn :-)");
        System.out.println("Start Update");
        DbBundle lDbBundle = new DbBundle();
        lDbBundle.openAllOhneParameterCheck();
        lDbBundle.openWeitere();
        BvDatenbank bvDatenbank = new BvDatenbank(lDbBundle);

        bvDatenbank.updateTableVersion2022b();
        System.out.println("Datenbank wurde upgedatet");

        System.out.println("Fertig");
        caZeigeHinweis.zeige(primaryStage, "So, durch ...");

        lDbBundle.closeAll();

    }

    /**
     * Starte module update 2022 c.
     */
    private void starteModuleUpdate2022c() {

        CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
        caZeigeHinweis.zeige(primaryStage,
                "Module Update2022c- Programmversion 2022 - UserLogin. Auf Weiter klicken, und dann einfach warten .... wird schon gehn :-)");
        System.out.println("Start Update");
        DbBundle lDbBundle = new DbBundle();
        lDbBundle.openAllOhneParameterCheck();
        lDbBundle.openWeitere();
        BvDatenbank bvDatenbank = new BvDatenbank(lDbBundle);

        bvDatenbank.updateTableVersion2022c();
        System.out.println("Datenbank wurde upgedatet");

        System.out.println("Fertig");
        caZeigeHinweis.zeige(primaryStage, "So, durch ...");

        lDbBundle.closeAll();

    }

    /**
     * Starte module update 2022 d.
     */
    private void starteModuleUpdate2022d() {

        CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
        caZeigeHinweis.zeige(primaryStage,
                "Module Update2022d- Programmversion 2022 - AbstimmungMeldungSperre. Auf Weiter klicken, und dann einfach warten .... wird schon gehn :-)");
        System.out.println("Start Update");
        DbBundle lDbBundle = new DbBundle();
        lDbBundle.openAllOhneParameterCheck();
        lDbBundle.openWeitere();
        BvDatenbank bvDatenbank = new BvDatenbank(lDbBundle);

        bvDatenbank.updateTableVersion2022d();
        System.out.println("Datenbank wurde upgedatet");

        System.out.println("Fertig");
        caZeigeHinweis.zeige(primaryStage, "So, durch ...");

        lDbBundle.closeAll();

    }

    /**
     * Starte module update 2023 a.
     */
    private void starteModuleUpdate2023a() {

        CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
        caZeigeHinweis.zeige(primaryStage,
                "Module Update2023a- Programmversion 2023a - ku178 und ku310. Auf Weiter klicken, und dann einfach warten .... wird schon gehn :-)");
        System.out.println("Start Update");
        DbBundle lDbBundle = new DbBundle();
        lDbBundle.openAllOhneParameterCheck();
        lDbBundle.openWeitere();
        BvDatenbank bvDatenbank = new BvDatenbank(lDbBundle);

        bvDatenbank.updateTableVersion2023a();
        System.out.println("Datenbank wurde upgedatet");

        System.out.println("Fertig");
        caZeigeHinweis.zeige(primaryStage, "So, durch ...");

        lDbBundle.closeAll();

    }

    /**
     * Starte module update 2023 b.
     */
    private void starteModuleUpdate2023b() {

        CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
        caZeigeHinweis.zeige(primaryStage,
                "Module Update2023b- Programmversion 2023 - Abstimmungku310. Auf Weiter klicken, und dann einfach warten .... wird schon gehn :-)");
        System.out.println("Start Update");
        DbBundle lDbBundle = new DbBundle();
        lDbBundle.openAllOhneParameterCheck();
        lDbBundle.openWeitere();
        BvDatenbank bvDatenbank = new BvDatenbank(lDbBundle);

        bvDatenbank.updateTableVersion2023b();
        System.out.println("Datenbank wurde upgedatet");

        System.out.println("Fertig");
        caZeigeHinweis.zeige(primaryStage, "So, durch ...");

        lDbBundle.closeAll();

    }

    /**
     * Starte module update 2024 a.
     */
    private void starteModuleUpdate2024a() {

        CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
        caZeigeHinweis.zeige(primaryStage,
                "Module Update2024a- Programmversion 2024 - EindeutigeKennung. Auf Weiter klicken, und dann einfach warten .... wird schon gehn :-)");
        System.out.println("Start Update");
        DbBundle lDbBundle = new DbBundle();
        lDbBundle.openAllOhneParameterCheck();
        lDbBundle.openWeitere();
        BvDatenbank bvDatenbank = new BvDatenbank(lDbBundle);

        bvDatenbank.updateTableVersion2024a();
        System.out.println("Datenbank wurde upgedatet");

        System.out.println("Fertig");
        caZeigeHinweis.zeige(primaryStage, "So, durch ...");

        lDbBundle.closeAll();

    }

    /**
     * Starte module update 2024 b.
     */
    private void starteModuleUpdate2024b() {

        CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
        caZeigeHinweis.zeige(primaryStage,
                "Module Update2024b");
        System.out.println("Start Update");
        DbBundle lDbBundle = new DbBundle();
        lDbBundle.openAllOhneParameterCheck();
        lDbBundle.openWeitere();
        BvDatenbank bvDatenbank = new BvDatenbank(lDbBundle);

        bvDatenbank.updateTableVersion2024b();
        System.out.println("Datenbank wurde upgedatet");

        System.out.println("Fertig");
        caZeigeHinweis.zeige(primaryStage, "Module Update done");

        lDbBundle.closeAll();

    }

    
    /**
     * Starte module update 2024 b.
     */
    private void starteModuleUpdate2025a() {

        CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
        caZeigeHinweis.zeige(primaryStage,
                "Module Update2025a- Programmversion 2025 - Auf Weiter klicken, und dann einfach warten .... wird schon gehn :-)");
        System.out.println("Start Update");
        DbBundle lDbBundle = new DbBundle();
        lDbBundle.openAllOhneParameterCheck();
        lDbBundle.openWeitere();
        BvDatenbank bvDatenbank = new BvDatenbank(lDbBundle);

        bvDatenbank.updateTableVersion2025a();
        System.out.println("Datenbank wurde upgedatet");

        System.out.println("Fertig");
        caZeigeHinweis.zeige(primaryStage, "So, durch ...");

        lDbBundle.closeAll();

    }

    
    
    /**
     * Starte module update 2021 aa.
     */
    private void starteModuleUpdate2021Aa() {

        CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
        caZeigeHinweis.zeige(primaryStage,
                "Module Update2021Aa - Aktienregister. Mandant 134 fest programmiert");
        System.out.println("Start Update");
        DbBundle lDbBundle = new DbBundle();
        lDbBundle.openAllOhneParameterCheck();
        lDbBundle.openWeitere();
        BvDatenbank bvDatenbank = new BvDatenbank(lDbBundle);

        bvDatenbank.updateTableVersion2021Aa();
        System.out.println("Datenbank wurde upgedatet");

        System.out.println("Fertig");
        caZeigeHinweis.zeige(primaryStage, "So, durch ...");

        lDbBundle.closeAll();

    }

    /**
     * The main method.
     *
     * @param args the arguments
     */
    public static void main(String[] args) {

        //        CaLogger.erzeugen();

        startArgs = args;

        //        if (CaLogger.logger.isErrorEnabled()) {
        //            CaLogger.logger.error("Test");
        //            //		CaLogger.logger.error("feferwgrgrtg regt g rgt ergt rgtgrt est");
        //
        //        }

        launch(args);

    }

}
