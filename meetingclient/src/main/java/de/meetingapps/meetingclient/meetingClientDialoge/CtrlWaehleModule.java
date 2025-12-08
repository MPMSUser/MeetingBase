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

import de.meetingapps.meetingclient.meetingBestand.SClErfassungsDaten;
import de.meetingapps.meetingclient.meetingDesign.DlAufrufDesigner;
import de.meetingapps.meetingclient.meetingTeilnahme.CtrlElekTeilnehmerverzAnzeigen;
import de.meetingapps.meetingportal.meetComEntities.EclUserLogin;
import de.meetingapps.meetingportal.meetComHVParam.ParamGeraet;
import de.meetingapps.meetingportal.meetComHVParam.ParamS;
import de.meetingapps.meetingportal.meetComHVParam.ParamSpezial;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * The Class CtrlWaehleModule.
 */
public class CtrlWaehleModule extends CtrlRoot {
    
    public final int width = 900;
    public final int height = 475;

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

    /** The lbl consultant. */
    @FXML
    private Label lblConsultant;

    /** The grpn buttons. */
    @FXML
    private GridPane grpnButtons = null;

    /**
     * Initialize.
     */
    @FXML
    void initialize() {
        assert btnBeenden != null : "fx:id=\"btnBeenden\" was not injected: check your FXML file 'WaehleModule.fxml'.";
        assert scrpnModule != null : "fx:id=\"scrpnModule\" was not injected: check your FXML file 'WaehleModule.fxml'.";

        /************* Ab hier individuell ********************************************/

        lfd = 0;

        ParamGeraet lParamGeraet = ParamS.paramGeraet;

        if (lParamGeraet.moduleHVMaster == true && eingeloggterUserLogin.pruefe_modul_moduleHVMaster() == true
                && (ParamS.clGlobalVar.datenbankPfadNr != -1 || ParamS.clGlobalVar.webServicePfadNr != -1)) {
            Button btnModuleHVMaster = new Button("HVMaster");
            btnModuleHVMaster.setOnAction(e -> {
                starteModuleHVMaster();
            });
            btnModuleHVMaster.setMaxWidth(Double.MAX_VALUE);
            ermittleOffset();
            grpnButtons.add(btnModuleHVMaster, x, y);
        }

        if (lParamGeraet.moduleDesigner == true && eingeloggterUserLogin.pruefe_hvMaster_moduleDesigner() == true
                && ParamS.clGlobalVar.datenbankPfadNr != -1) {
            Button btnModuleDesigner = new Button("Designer");
            btnModuleDesigner.setOnAction(e -> {
                starteModuleDesigner();
            });
            btnModuleDesigner.setMaxWidth(Double.MAX_VALUE);
            ermittleOffset();
            grpnButtons.add(btnModuleDesigner, x, y);
        }

        newLineOffset();

        if (lParamGeraet.moduleFrontOffice == true && eingeloggterUserLogin.pruefe_modul_moduleFrontOffice() == true
                && ParamS.clGlobalVar.webServicePfadNr != -1) {
            Button btnModuleFrontOfficer = null;
            if (ParamSpezial.ku254(ParamS.clGlobalVar.mandant)) {
                btnModuleFrontOfficer = new Button("FrontOffice Verein");
            } else {
                btnModuleFrontOfficer = new Button("FrontOffice");
            }
            btnModuleFrontOfficer.setOnAction(e -> {
                starteModuleFrontOffice();
            });
            btnModuleFrontOfficer.setMaxWidth(Double.MAX_VALUE);
            ermittleOffset();
            grpnButtons.add(btnModuleFrontOfficer, x, y);
        }

        if (lParamGeraet.moduleTabletAbstimmung == true
                && eingeloggterUserLogin.pruefe_modul_moduleTabletAbstimmung() == true
                && ParamS.clGlobalVar.webServicePfadNr != -1) {
            Button btnModuleFrontOfficer = null;
            btnModuleFrontOfficer = new Button("TabletAbstimmung");
            btnModuleFrontOfficer.setOnAction(e -> {
                starteModuleTabletAbstimmung();
            });
            btnModuleFrontOfficer.setMaxWidth(Double.MAX_VALUE);
            ermittleOffset();
            grpnButtons.add(btnModuleFrontOfficer, x, y);
        }

        if (lParamGeraet.moduleKontrolle == true && eingeloggterUserLogin.pruefe_modul_moduleKontrolle() == true
                && ParamS.clGlobalVar.datenbankPfadNr != -1) {
            Button btnModuleKontrolle = new Button("HV-Kontrolle");
            btnModuleKontrolle.setOnAction(e -> {
                starteModuleKontrolle();
            });
            btnModuleKontrolle.setMaxWidth(Double.MAX_VALUE);
            ermittleOffset();
            grpnButtons.add(btnModuleKontrolle, x, y);
        }

        if (lParamGeraet.moduleTeilnahmeverzeichnis == true
                && eingeloggterUserLogin.pruefe_modul_moduleTeilnahmeverzeichnis() == true
                && ParamS.clGlobalVar.datenbankPfadNr != -1) {
            Button btnModuleTeilnahmeverzeichnis = null;
            if (ParamSpezial.ku254(ParamS.clGlobalVar.mandant)) {
                btnModuleTeilnahmeverzeichnis = new Button("Bühneninformation");
            } else {
                btnModuleTeilnahmeverzeichnis = new Button("Elektronisches Teilnehmerverzeichnis");
            }
            btnModuleTeilnahmeverzeichnis.setOnAction(e -> {
                starteModuleTeilnahmeverzeichnis();
            });
            btnModuleTeilnahmeverzeichnis.setMaxWidth(Double.MAX_VALUE);
            ermittleOffset();
            grpnButtons.add(btnModuleTeilnahmeverzeichnis, x, y);
        }

        newLineOffset();

        if (lParamGeraet.moduleServiceDesk == true && eingeloggterUserLogin.pruefe_modul_moduleServiceDesk() == true
                && ParamS.clGlobalVar.webServicePfadNr != -1) {
            Button btnModuleServiceDesk = new Button("Service Desk");
            btnModuleServiceDesk.setOnAction(e -> {
                starteModuleServiceDesk();
            });
            btnModuleServiceDesk.setMaxWidth(Double.MAX_VALUE);
            ermittleOffset();
            grpnButtons.add(btnModuleServiceDesk, x, y);
        }

        if (lParamGeraet.moduleServiceDesk == true && eingeloggterUserLogin.pruefe_modul_moduleServiceDesk() == true
                && ParamS.clGlobalVar.webServicePfadNr != -1 && ParamS.param.paramPortal.varianteDialogablauf == 1) {
            Button btnModuleAkkreditierungku178 = new Button("Akkreditierung ku178");
            btnModuleAkkreditierungku178.setOnAction(e -> {
                starteModuleku178Akkreditierung();
            });
            btnModuleAkkreditierungku178.setMaxWidth(Double.MAX_VALUE);
            ermittleOffset();
            grpnButtons.add(btnModuleAkkreditierungku178, x, y);
        }

        if (lParamGeraet.moduleBestandsverwaltung == true
                && eingeloggterUserLogin.pruefe_modul_moduleBestandsverwaltung() == true
                && ParamS.clGlobalVar.webServicePfadNr != -1) {
            Button btnModuleAnmeldestelle = new Button("Bestandsverwaltung");
            btnModuleAnmeldestelle.setOnAction(e -> {
                starteModuleBestandsverwaltung();
            });
            btnModuleAnmeldestelle.setMaxWidth(Double.MAX_VALUE);
            ermittleOffset();
            grpnButtons.add(btnModuleAnmeldestelle, x, y);
//            Platform.runLater(() -> btnModuleAnmeldestelle.requestFocus());
        }

        if (lParamGeraet.moduleHotline == true && eingeloggterUserLogin.pruefe_modul_moduleHotline() == true
                && ParamS.clGlobalVar.webServicePfadNr != -1) {
            Button btnModuleHotline = new Button("Hotline/Info");
            btnModuleHotline.setOnAction(e -> {
                starteModuleHotline();
            });
            btnModuleHotline.setMaxWidth(Double.MAX_VALUE);
            ermittleOffset();
            grpnButtons.add(btnModuleHotline, x, y);
        }

        newLineOffset();

        // TODO: checken ob ParamS.clGlobalVar.datenbankPfadNr != -1 noch notwendig
        if (lParamGeraet.moduleAktienregisterImport == true
                && eingeloggterUserLogin.pruefe_modul_moduleAktienregisterImport() == true
                && ParamS.clGlobalVar.datenbankPfadNr != -1) {
            Button btnModuleAktienregisterImport = new Button("Aktienregister Import");
            btnModuleAktienregisterImport.setOnAction(e -> {
                starteModuleAktienregisterImport();
            });
            btnModuleAktienregisterImport.setMaxWidth(Double.MAX_VALUE);
            ermittleOffset();
            grpnButtons.add(btnModuleAktienregisterImport, x, y);
        }

        lblConsultant.setText(ParamS.param.paramBasis.mailConsultant);
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
     * New line offset.
     */
    private void newLineOffset() {
        if ((lfd % 2) > 0) {
            lfd++;
        }
        ermittleOffset();
        Label dummyLabel = new Label(" ");
        grpnButtons.add(dummyLabel, x, y);
        lfd++;
    }

    /**
     * Btn beenden clicked.
     *
     * @param event the event
     */
    @FXML
    void btnBeendenClicked(ActionEvent event) {
        eigeneStage.hide();
    }

    /** The eigene stage. */
    private Stage eigeneStage;

    /** The eingeloggter user login. */
    public EclUserLogin eingeloggterUserLogin = null;

    /**
     * Starte modul vorausgewaehlt.
     *
     * @param modulNummer the modul nummer
     */
    public void starteModulVorausgewaehlt(int modulNummer) {
        eigeneStage = new Stage();
        switch (modulNummer) {
        case 1:
            starteModuleHVMaster();
            return;
        case 2:
            starteModuleFrontOffice();
            return;
        case 3:
            starteModuleKontrolle();
            return;
        case 4:
            starteModuleServiceDesk();
            return;
        case 5:
            starteModuleTeilnahmeverzeichnis();
            return;
        case 6:
            starteModuleTabletAbstimmung();
            return;
        case 7:
            starteModuleBestandsverwaltung();
            return;
        case 8:
            starteModuleHotline();
            return;
        case 9:
            starteModuleAktienregisterImport();
            return;
        case 10:
            starteModuleDesigner();
            return;
        }

    }

    /**
     * Starte module HV master.
     */
    private void starteModuleHVMaster() {
        CaIcon.master(eigeneStage);
        de.meetingapps.meetingclient.meetingHVMaster.CtrlHauptStage controllerFenster = new de.meetingapps.meetingclient.meetingHVMaster.CtrlHauptStage();
        controllerFenster.init(eigeneStage);

        CaController caController = new CaController();
        caController.open(eigeneStage, controllerFenster,
                "/de/meetingapps/meetingclient/meetingHVMaster/HauptStage.fxml", 1100, 600, "MeetingPortal HV Master",
                false);
    }

    /**
     * Starte module designer.
     */
    private void starteModuleDesigner() {
        Stage neuerDialog = new Stage();
        CaIcon.designer(neuerDialog);
        DlAufrufDesigner controllerFenster = new DlAufrufDesigner();

        CaController caController = new CaController();
        caController.open(neuerDialog, controllerFenster,
                "/de/meetingapps/meetingclient/meetingDesign/DlAufrufDesigner.fxml", controllerFenster.width, controllerFenster.height, "MeetingDesign", true);
    }

    /**
     * Starte module front office.
     */
    private void starteModuleFrontOffice() {


        CaIcon.frontOffice(eigeneStage);
        de.meetingapps.meetingclient.meetingFrontOffice.CtrlHauptStage controllerFenster = new de.meetingapps.meetingclient.meetingFrontOffice.CtrlHauptStage();
        controllerFenster.init(eigeneStage);

        CaController caController = new CaController();
        caController.open(eigeneStage, controllerFenster,
                "/de/meetingapps/meetingclient/meetingFrontOffice/HauptStage.fxml", 1200, 600,
                "MeetingPortal FrontOffice", false);
    }

    /**
     * Starte module tablet abstimmung.
     */
    private void starteModuleTabletAbstimmung() {
        de.meetingapps.meetingclient.meetingFrontOffice.CtrlHauptStage controllerFenster = new de.meetingapps.meetingclient.meetingFrontOffice.CtrlHauptStage();
        controllerFenster.init(eigeneStage);
        controllerFenster.aufrufFensterTabletAbstimmung();
    }

    /**
     * Starte module kontrolle.
     */
    private void starteModuleKontrolle() {

        CaIcon.kontrolle(eigeneStage);
        de.meetingapps.meetingclient.meetingKontrolle.CtrlHauptStage controllerFenster = new de.meetingapps.meetingclient.meetingKontrolle.CtrlHauptStage();
        controllerFenster.init(eigeneStage);

        CaController caController = new CaController();
        caController.open(eigeneStage, controllerFenster,
                "/de/meetingapps/meetingclient/meetingKontrolle/HauptStage.fxml", 1200, 600, "MeetingPortal Kontrolle",
                false);
    }

    /**
     * Starte module service desk.
     */
    private void starteModuleServiceDesk() {
        CaIcon.serviceDesk(eigeneStage);
        de.meetingapps.meetingclient.meetingServiceDesk.CtrlHauptStage controllerFenster = new de.meetingapps.meetingclient.meetingServiceDesk.CtrlHauptStage();
        controllerFenster.init(eigeneStage);

        CaController caController = new CaController();
        caController.open(eigeneStage, controllerFenster,
                "/de/meetingapps/meetingclient/meetingServiceDesk/HauptStage.fxml", 1100, 650,
                "MeetingPortal ServiceDesk", false);
    }

    /**
     * Starte module ku178 akkreditierung.
     */
    private void starteModuleku178Akkreditierung() {
        CaIcon.serviceDesk(eigeneStage);
        de.meetingapps.meetingclient.meetingku178Akkreditierung.CtrlHauptStage controllerFenster = new de.meetingapps.meetingclient.meetingku178Akkreditierung.CtrlHauptStage();
        controllerFenster.init(eigeneStage);

        CaController caController = new CaController();
        caController.open(eigeneStage, controllerFenster,
                "/de/meetingapps/meetingclient/meetingku178Akkreditierung/HauptStage.fxml", 1100, 650,
                "Meeting Portal Akkreditierung ku178", false);
    }

    /**
     * Starte module teilnahmeverzeichnis.
     */
    private void starteModuleTeilnahmeverzeichnis() {

            CaIcon.teilnehmerverzeichnis(eigeneStage);
            CtrlElekTeilnehmerverzAnzeigen controllerFenster = new CtrlElekTeilnehmerverzAnzeigen();
            controllerFenster.init(eigeneStage);

            CaController caController = new CaController();
            caController.openUndecorated(eigeneStage, controllerFenster,
                    "/de/meetingapps/meetingclient/meetingTeilnahme/ElekTeilnehmerverzAnzeigen.fxml", 1400, 900,
                    "Teilnehmerverzeichnis", false);
    }

    /**
     * Starte module bestandsverwaltung.
     */
    private void starteModuleBestandsverwaltung() {
        SClErfassungsDaten.init();

        CaIcon.bestandsverwaltung(eigeneStage);
        de.meetingapps.meetingclient.meetingBestand.CtrlHauptStage controllerFenster = new de.meetingapps.meetingclient.meetingBestand.CtrlHauptStage();
        controllerFenster.init(eigeneStage);

        CaController caController = new CaController();
        caController.open(eigeneStage, controllerFenster, "/de/meetingapps/meetingclient/meetingBestand/HauptStage.fxml",
                1100, 600, "MeetingPortal Bestand", false);
    }

    /**
     * Starte module hotline.
     */
    private void starteModuleHotline() {
        CaIcon.hotline(eigeneStage);
        de.meetingapps.meetingclient.meetingInfo.CtrlHauptStage controllerFenster = new de.meetingapps.meetingclient.meetingInfo.CtrlHauptStage();
        controllerFenster.init(eigeneStage);

        CaController caController = new CaController();
        caController.open(eigeneStage, controllerFenster, "/de/meetingapps/meetingclient/meetingInfo/HauptStage.fxml",
                1100, 600, "MeetingPortal Hotline/Info-Tool", false);
    }

    /**
     * Starte module aktienregister import.
     */
    private void starteModuleAktienregisterImport() {
        CaIcon.register(eigeneStage);
        de.meetingapps.meetingclient.meetingAktienregisterImport.CtrlHauptStage controllerFenster = new de.meetingapps.meetingclient.meetingAktienregisterImport.CtrlHauptStage();
        controllerFenster.init(eigeneStage);

        CaController caController = new CaController();
        caController.open(eigeneStage, controllerFenster,
                "/de/meetingapps/meetingclient/meetingAktienregisterImport/HauptStage.fxml", 450, 600,
                "Aktienregister Import", false);
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
