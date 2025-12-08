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

import java.net.URL;
import java.util.ResourceBundle;

import de.meetingapps.meetingclient.meetingClientDialoge.CaController;
import de.meetingapps.meetingclient.meetingClientDialoge.CaIcon;
import de.meetingapps.meetingclient.meetingClientDialoge.CtrlRoot;
import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaString;
import de.meetingapps.meetingportal.meetComBl.BlAbstimmungsListeBearbeiten;
import de.meetingapps.meetingportal.meetComBl.BlSammelkarten;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclAbstimmung;
import de.meetingapps.meetingportal.meetComEntities.EclAbstimmungZuAbstimmungsblock;
import de.meetingapps.meetingportal.meetComHVParam.ParamS;
import de.meetingapps.meetingportal.meetComKonst.KonstStimmart;
import de.meetingapps.meetingportal.meetComStub.StubAbstimmungen;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * The Class CtrlParameterAbstimmungNeu.
 */
public class CtrlParameterAbstimmungNeu extends CtrlRoot {

    /** The log drucken. */
    private int logDrucken = 10;

    /** The resources. */
    @FXML
    private ResourceBundle resources;

    /** The location. */
    @FXML
    private URL location;

    /** The btn neu. */
    @FXML
    private Button btnNeu;

    /** The btn abbrechen. */
    @FXML
    private Button btnAbbrechen;

    /** The lbl extern. */
    @FXML
    private Label lblExtern;

    /** The tf TOP intern DE. */
    @FXML
    private TextField tfTOPInternDE;

    /** The tf index intern DE. */
    @FXML
    private TextField tfIndexInternDE;

    /** The lbl englisch. */
    @FXML
    private Label lblEnglisch;

    /** The lbl TOP extern DE. */
    @FXML
    private Label lblTOPExternDE;

    /** The tf TOP exten DE. */
    @FXML
    private TextField tfTOPExtenDE;

    /** The lbl index extern DE. */
    @FXML
    private Label lblIndexExternDE;

    /** The tf index extern DE. */
    @FXML
    private TextField tfIndexExternDE;

    /** The lbl TOP extern EN. */
    @FXML
    private Label lblTOPExternEN;

    /** The tf TOP extern EN. */
    @FXML
    private TextField tfTOPExternEN;

    /** The lbl index extern EN. */
    @FXML
    private Label lblIndexExternEN;

    /** The tf index extern EN. */
    @FXML
    private TextField tfIndexExternEN;

    /** The lbl TOP formulare DE. */
    @FXML
    private Label lblTOPFormulareDE;

    /** The tf TOP formulare DE. */
    @FXML
    private TextField tfTOPFormulareDE;

    /** The lbl index formulare DE. */
    @FXML
    private Label lblIndexFormulareDE;

    /** The tf index formulare DE. */
    @FXML
    private TextField tfIndexFormulareDE;

    /** The tf kurzbezeichnung DE. */
    @FXML
    private TextField tfKurzbezeichnungDE;

    /** The tf kurzbezeichnung EN. */
    @FXML
    private TextField tfKurzbezeichnungEN;

    /** The tf langtext DE. */
    @FXML
    private TextArea tfLangtextDE;

    /** The tf langtext EN. */
    @FXML
    private TextArea tfLangtextEN;

    /** The cb als ueberschrift. */
    @FXML
    private CheckBox cbAlsUeberschrift;

    /** The cb gattung 1. */
    @FXML
    private CheckBox cbGattung1;

    /** The cb gattung 2. */
    @FXML
    private CheckBox cbGattung2;

    /** The cb gattung 3. */
    @FXML
    private CheckBox cbGattung3;

    /** The cb gattung 4. */
    @FXML
    private CheckBox cbGattung4;

    /** The lbl stimmen auswerten. */
    @FXML
    private Label lblStimmenAuswerten;

    /** The cb aktiv abstimmung weisung. */
    @FXML
    private CheckBox cbAktivAbstimmungWeisung;

    /** The cb aktiv weisung verlassen. */
    @FXML
    private CheckBox cbAktivWeisungVerlassen;

    /** The cb stimmausschluss V. */
    @FXML
    private CheckBox cbStimmausschlussV;

    /** The cb stimmausschluss A. */
    @FXML
    private CheckBox cbStimmausschlussA;

    /** The tf beschlussvorschlag sonstige. */
    @FXML
    private TextField tfBeschlussvorschlagSonstige;

    /** The cb aktiv weisung internetservice. */
    @FXML
    private CheckBox cbAktivWeisungInternetservice;

    /** The cb aktiv abstimmung tablet app. */
    @FXML
    private CheckBox cbAktivAbstimmungTabletApp;

    /** The lbl weisungen. */
    @FXML
    private Label lblWeisungen;

    /** The lbl weisung KIAV. */
    @FXML
    private Label lblWeisungKIAV;

    /** The lbl weisung SRV. */
    @FXML
    private Label lblWeisungSRV;

    /** The lbl weisung briefwahl. */
    @FXML
    private Label lblWeisungBriefwahl;

    /** The lbl weisung dauer. */
    @FXML
    private Label lblWeisungDauer;

    /** The lbl weisung orga. */
    @FXML
    private Label lblWeisungOrga;

    /** The btn neue abstimmung. */
    @FXML
    private Button btnNeueAbstimmung;

    /** The lbl stimmkartennummer. */
    @FXML
    private Label lblStimmkartennummer;

    /** The tf stimmkartennummer. */
    @FXML
    private TextField tfStimmkartennummer;

    /** The lbl stimmkartennummer position. */
    @FXML
    private Label lblStimmkartennummerPosition;

    /** The tf stimmkartennummer position. */
    @FXML
    private TextField tfStimmkartennummerPosition;

    /** The lbl tablet blaettern seite. */
    @FXML
    private Label lblTabletBlaetternSeite;

    /** The tf tablet blaettern seite. */
    @FXML
    private TextField tfTabletBlaetternSeite;

    /** The tf tablet blaettern position. */
    @FXML
    private TextField tfTabletBlaetternPosition;

    /** The lbl tablet blaettern position. */
    @FXML
    private Label lblTabletBlaetternPosition;

    /** The cb TOP extern. */
    @FXML
    private CheckBox cbTOPExtern;

    /** The cb TOP formulare. */
    @FXML
    private CheckBox cbTOPFormulare;

    /** The cb gattung 5. */
    @FXML
    private CheckBox cbGattung5;

    /** The tf kandidat DE. */
    @FXML
    private TextField tfKandidatDE;

    /** The tf kandidat EN. */
    @FXML
    private TextField tfKandidatEN;

    /** The lbl weisung holen von. */
    @FXML
    private Label lblWeisungHolenVon;

    /** The cb stimmen auswerten. */
    /*++++++++++++++++++Ab hier individuell++++++++++++++++++++++++++*/
    @FXML
    private ComboBox<String> cbStimmenAuswerten;

    /** The cb erforderliche mehrheit. */
    @FXML
    private ComboBox<String> cbErforderlicheMehrheit;

    /** The cb beschlussvorschlag von. */
    @FXML
    private ComboBox<String> cbBeschlussvorschlagVon;

    /** The cb weisung vor abstimmung. */
    @FXML
    private ComboBox<String> cbWeisungVorAbstimmung;

    /** The cb weisung SRV. */
    @FXML
    private ComboBox<String> cbWeisungSRV;

    /** The cb weisung briefwahl. */
    @FXML
    private ComboBox<String> cbWeisungBriefwahl;

    /** The cb weisung KIAV. */
    @FXML
    private ComboBox<String> cbWeisungKIAV;

    /** The cb weisung dauer. */
    @FXML
    private ComboBox<String> cbWeisungDauer;

    /** The cb weisung orga. */
    @FXML
    private ComboBox<String> cbWeisungOrga;

    /** The cb in abstimmungsvorgang. */
    @FXML
    private ComboBox<String> cbInAbstimmungsvorgang;

    /** The cb weisung holen von. */
    @FXML
    private ComboBox<String> cbWeisungHolenVon;

    @FXML
    private Label lblAnzKurz;

    @FXML
    private Label lblAnzLang;

    @FXML
    private Label lblAnzKurzEN;

    @FXML
    private Label lblAnzLangEN;

    /**
     * Initialize.
     */
    @FXML
    void initialize() {

        assert btnNeu != null : "fx:id=\"btnNeu\" was not injected: check your FXML file 'ParameterAbstimmungNeu.fxml'.";
        assert btnAbbrechen != null : "fx:id=\"btnAbbrechen\" was not injected: check your FXML file 'ParameterAbstimmungNeu.fxml'.";
        assert lblExtern != null : "fx:id=\"lblExtern\" was not injected: check your FXML file 'ParameterAbstimmungNeu.fxml'.";
        assert tfTOPInternDE != null : "fx:id=\"tfTOPInternDE\" was not injected: check your FXML file 'ParameterAbstimmungNeu.fxml'.";
        assert tfIndexInternDE != null : "fx:id=\"tfIndexInternDE\" was not injected: check your FXML file 'ParameterAbstimmungNeu.fxml'.";
        assert lblEnglisch != null : "fx:id=\"lblEnglisch\" was not injected: check your FXML file 'ParameterAbstimmungNeu.fxml'.";
        assert lblTOPExternDE != null : "fx:id=\"lblTOPExternDE\" was not injected: check your FXML file 'ParameterAbstimmungNeu.fxml'.";
        assert tfTOPExtenDE != null : "fx:id=\"tfTOPExtenDE\" was not injected: check your FXML file 'ParameterAbstimmungNeu.fxml'.";
        assert lblIndexExternDE != null : "fx:id=\"lblIndexExternDE\" was not injected: check your FXML file 'ParameterAbstimmungNeu.fxml'.";
        assert tfIndexExternDE != null : "fx:id=\"tfIndexExternDE\" was not injected: check your FXML file 'ParameterAbstimmungNeu.fxml'.";
        assert lblTOPExternEN != null : "fx:id=\"lblTOPExternEN\" was not injected: check your FXML file 'ParameterAbstimmungNeu.fxml'.";
        assert tfTOPExternEN != null : "fx:id=\"tfTOPExternEN\" was not injected: check your FXML file 'ParameterAbstimmungNeu.fxml'.";
        assert lblIndexExternEN != null : "fx:id=\"lblIndexExternEN\" was not injected: check your FXML file 'ParameterAbstimmungNeu.fxml'.";
        assert tfIndexExternEN != null : "fx:id=\"tfIndexExternEN\" was not injected: check your FXML file 'ParameterAbstimmungNeu.fxml'.";
        assert lblTOPFormulareDE != null : "fx:id=\"lblTOPFormulareDE\" was not injected: check your FXML file 'ParameterAbstimmungNeu.fxml'.";
        assert tfTOPFormulareDE != null : "fx:id=\"tfTOPFormulareDE\" was not injected: check your FXML file 'ParameterAbstimmungNeu.fxml'.";
        assert lblIndexFormulareDE != null : "fx:id=\"lblIndexFormulareDE\" was not injected: check your FXML file 'ParameterAbstimmungNeu.fxml'.";
        assert tfIndexFormulareDE != null : "fx:id=\"tfIndexFormulareDE\" was not injected: check your FXML file 'ParameterAbstimmungNeu.fxml'.";
        assert tfKurzbezeichnungDE != null : "fx:id=\"tfKurzbezeichnungDE\" was not injected: check your FXML file 'ParameterAbstimmungNeu.fxml'.";
        assert tfKurzbezeichnungEN != null : "fx:id=\"tfKurzbezeichnungEN\" was not injected: check your FXML file 'ParameterAbstimmungNeu.fxml'.";
        assert tfLangtextDE != null : "fx:id=\"tfLangtextDE\" was not injected: check your FXML file 'ParameterAbstimmungNeu.fxml'.";
        assert tfLangtextEN != null : "fx:id=\"tfLangtextEN\" was not injected: check your FXML file 'ParameterAbstimmungNeu.fxml'.";
        assert cbAlsUeberschrift != null : "fx:id=\"cbAlsUeberschrift\" was not injected: check your FXML file 'ParameterAbstimmungNeu.fxml'.";
        assert cbGattung1 != null : "fx:id=\"cbGattung1\" was not injected: check your FXML file 'ParameterAbstimmungNeu.fxml'.";
        assert cbGattung2 != null : "fx:id=\"cbGattung2\" was not injected: check your FXML file 'ParameterAbstimmungNeu.fxml'.";
        assert cbGattung3 != null : "fx:id=\"cbGattung3\" was not injected: check your FXML file 'ParameterAbstimmungNeu.fxml'.";
        assert cbGattung4 != null : "fx:id=\"cbGattung4\" was not injected: check your FXML file 'ParameterAbstimmungNeu.fxml'.";
        assert lblStimmenAuswerten != null : "fx:id=\"lblStimmenAuswerten\" was not injected: check your FXML file 'ParameterAbstimmungNeu.fxml'.";
        assert cbStimmenAuswerten != null : "fx:id=\"cbStimmenAuswerten\" was not injected: check your FXML file 'ParameterAbstimmungNeu.fxml'.";
        assert cbAktivAbstimmungWeisung != null : "fx:id=\"cbAktivAbstimmungWeisung\" was not injected: check your FXML file 'ParameterAbstimmungNeu.fxml'.";
        assert cbAktivWeisungVerlassen != null : "fx:id=\"cbAktivWeisungVerlassen\" was not injected: check your FXML file 'ParameterAbstimmungNeu.fxml'.";
        assert cbStimmausschlussV != null : "fx:id=\"cbStimmausschlussV\" was not injected: check your FXML file 'ParameterAbstimmungNeu.fxml'.";
        assert cbStimmausschlussA != null : "fx:id=\"cbStimmausschlussA\" was not injected: check your FXML file 'ParameterAbstimmungNeu.fxml'.";
        assert cbErforderlicheMehrheit != null : "fx:id=\"cbErforderlicheMehrheit\" was not injected: check your FXML file 'ParameterAbstimmungNeu.fxml'.";
        assert cbBeschlussvorschlagVon != null : "fx:id=\"cbBeschlussvorschlagVon\" was not injected: check your FXML file 'ParameterAbstimmungNeu.fxml'.";
        assert tfBeschlussvorschlagSonstige != null : "fx:id=\"tfBeschlussvorschlagSonstige\" was not injected: check your FXML file 'ParameterAbstimmungNeu.fxml'.";
        assert cbAktivWeisungInternetservice != null : "fx:id=\"cbAktivWeisungInternetservice\" was not injected: check your FXML file 'ParameterAbstimmungNeu.fxml'.";
        assert cbWeisungVorAbstimmung != null : "fx:id=\"cbWeisungVorAbstimmung\" was not injected: check your FXML file 'ParameterAbstimmungNeu.fxml'.";
        assert lblWeisungen != null : "fx:id=\"lblWeisungen\" was not injected: check your FXML file 'ParameterAbstimmungNeu.fxml'.";
        assert lblWeisungKIAV != null : "fx:id=\"lblWeisungKIAV\" was not injected: check your FXML file 'ParameterAbstimmungNeu.fxml'.";
        assert lblWeisungSRV != null : "fx:id=\"lblWeisungSRV\" was not injected: check your FXML file 'ParameterAbstimmungNeu.fxml'.";
        assert lblWeisungBriefwahl != null : "fx:id=\"lblWeisungBriefwahl\" was not injected: check your FXML file 'ParameterAbstimmungNeu.fxml'.";
        assert lblWeisungDauer != null : "fx:id=\"lblWeisungDauer\" was not injected: check your FXML file 'ParameterAbstimmungNeu.fxml'.";
        assert lblWeisungOrga != null : "fx:id=\"lblWeisungOrga\" was not injected: check your FXML file 'ParameterAbstimmungNeu.fxml'.";
        assert cbWeisungSRV != null : "fx:id=\"cbWeisungSRV\" was not injected: check your FXML file 'ParameterAbstimmungNeu.fxml'.";
        assert cbWeisungBriefwahl != null : "fx:id=\"cbWeisungBriefwahl\" was not injected: check your FXML file 'ParameterAbstimmungNeu.fxml'.";
        assert cbWeisungKIAV != null : "fx:id=\"cbWeisungKIAV\" was not injected: check your FXML file 'ParameterAbstimmungNeu.fxml'.";
        assert cbWeisungDauer != null : "fx:id=\"cbWeisungDauer\" was not injected: check your FXML file 'ParameterAbstimmungNeu.fxml'.";
        assert cbWeisungOrga != null : "fx:id=\"cbWeisungOrga\" was not injected: check your FXML file 'ParameterAbstimmungNeu.fxml'.";
        assert btnNeueAbstimmung != null : "fx:id=\"btnNeueAbstimmung\" was not injected: check your FXML file 'ParameterAbstimmungNeu.fxml'.";
        assert cbInAbstimmungsvorgang != null : "fx:id=\"cbInAbstimmungsvorgang\" was not injected: check your FXML file 'ParameterAbstimmungNeu.fxml'.";
        assert lblStimmkartennummer != null : "fx:id=\"lblStimmkartennummer\" was not injected: check your FXML file 'ParameterAbstimmungNeu.fxml'.";
        assert tfStimmkartennummer != null : "fx:id=\"tfStimmkartennummer\" was not injected: check your FXML file 'ParameterAbstimmungNeu.fxml'.";
        assert lblStimmkartennummerPosition != null : "fx:id=\"lblStimmkartennummerPosition\" was not injected: check your FXML file 'ParameterAbstimmungNeu.fxml'.";
        assert tfStimmkartennummerPosition != null : "fx:id=\"tfStimmkartennummerPosition\" was not injected: check your FXML file 'ParameterAbstimmungNeu.fxml'.";
        assert lblTabletBlaetternSeite != null : "fx:id=\"lblTabletBlaetternSeite\" was not injected: check your FXML file 'ParameterAbstimmungNeu.fxml'.";
        assert tfTabletBlaetternSeite != null : "fx:id=\"tfTabletBlaetternSeite\" was not injected: check your FXML file 'ParameterAbstimmungNeu.fxml'.";
        assert tfTabletBlaetternPosition != null : "fx:id=\"tfTabletBlaetternPosition\" was not injected: check your FXML file 'ParameterAbstimmungNeu.fxml'.";
        assert lblTabletBlaetternPosition != null : "fx:id=\"lblTabletBlaetternPosition\" was not injected: check your FXML file 'ParameterAbstimmungNeu.fxml'.";
        assert cbTOPExtern != null : "fx:id=\"cbTOPExtern\" was not injected: check your FXML file 'ParameterAbstimmungNeu.fxml'.";
        assert cbTOPFormulare != null : "fx:id=\"cbTOPFormulare\" was not injected: check your FXML file 'ParameterAbstimmungNeu.fxml'.";
        assert cbGattung5 != null : "fx:id=\"cbGattung5\" was not injected: check your FXML file 'ParameterAbstimmungNeu.fxml'.";
        assert tfKandidatDE != null : "fx:id=\"tfKandidatDE\" was not injected: check your FXML file 'ParameterAbstimmungNeu.fxml'.";
        assert tfKandidatEN != null : "fx:id=\"tfKandidatEN\" was not injected: check your FXML file 'ParameterAbstimmungNeu.fxml'.";
        assert lblWeisungHolenVon != null : "fx:id=\"lblWeisungHolenVon\" was not injected: check your FXML file 'ParameterAbstimmungNeu.fxml'.";
        assert cbWeisungHolenVon != null : "fx:id=\"cbWeisungHolenVon\" was not injected: check your FXML file 'ParameterAbstimmungNeu.fxml'.";

        /****** Div. Felder aktivieren/deaktivieren/belegen ******/

        deaktiviereFelderBasis();

        fuelleExternTOP();
        fuelleFormulareTOP();

        CtrlParameterAbstimmungAllgemein.fuelleCbStimmenAuswerten(cbStimmenAuswerten, 0);
        CtrlParameterAbstimmungAllgemein.fuelleCbErforderlicheMehrheit(cbErforderlicheMehrheit, 1);
        CtrlParameterAbstimmungAllgemein.fuelleCbBeschlussvorschlagVon(cbBeschlussvorschlagVon, 3);

        CtrlParameterAbstimmungAllgemein.belegeAbstimmungArray();
        CtrlParameterAbstimmungAllgemein.belegeMitAbstimmungArray(cbWeisungHolenVon, -1, false, "keine Übernahme (-1)");

        /*Stimmberechtigte Gattungen*/
        aktiviereGattung(cbGattung1, 1);
        aktiviereGattung(cbGattung2, 2);
        aktiviereGattung(cbGattung3, 3);
        aktiviereGattung(cbGattung4, 4);
        aktiviereGattung(cbGattung5, 5);

        CtrlParameterAbstimmungAllgemein.belegeWeisungBelegenMit(cbWeisungSRV);
        CtrlParameterAbstimmungAllgemein.belegeWeisungBelegenMit(cbWeisungBriefwahl);
        CtrlParameterAbstimmungAllgemein.belegeWeisungBelegenMit(cbWeisungKIAV);
        CtrlParameterAbstimmungAllgemein.belegeWeisungBelegenMit(cbWeisungDauer);
        CtrlParameterAbstimmungAllgemein.belegeWeisungBelegenMit(cbWeisungOrga);

        CtrlParameterAbstimmungAllgemein.belegeMitAbstimmungArray(cbWeisungVorAbstimmung, -1, true, "am Ende (-1)");

        CtrlParameterAbstimmungAllgemein.belegeAbstimmungsblockArray();
        CtrlParameterAbstimmungAllgemein.belegeMitAbstimmungblockArray(cbInAbstimmungsvorgang, -1);

        tfKurzbezeichnungDE.textProperty().addListener((o, oV, nV) -> lblAnzKurz.setText(nV.length() + " / 160"));
        tfKurzbezeichnungEN.textProperty().addListener((o, oV, nV) -> lblAnzKurzEN.setText(nV.length() + " / 160"));
        
        tfLangtextDE.textProperty().addListener((o, oV, nV) -> lblAnzLang.setText(nV.length() + " / 800"));
        tfLangtextEN.textProperty().addListener((o, oV, nV) -> lblAnzLangEN.setText(nV.length() + " / 800"));

    }

    /** **********Oberflächen***************************************. */

    /*++++Extern TOP+++++*/
    private boolean benutzeTOPExtern = true;

    /**
     * Show TOP extern.
     */
    private void showTOPExtern() {
        if (benutzeTOPExtern && cbTOPExtern.isSelected() == false) {
            tfTOPExtenDE.setVisible(true);
            tfIndexExternDE.setVisible(true);

            if (benutzeEnglisch) {
                tfTOPExternEN.setVisible(true);
                tfIndexExternEN.setVisible(true);
            } else {
                tfTOPExternEN.setVisible(false);
                tfIndexExternEN.setVisible(false);
            }

        } else {
            tfTOPExtenDE.setVisible(false);
            tfIndexExternDE.setVisible(false);

            tfTOPExternEN.setVisible(false);
            tfIndexExternEN.setVisible(false);

        }
    }

    /**
     * Fuelle extern TOP.
     */
    private void fuelleExternTOP() {
        cbTOPExtern.setSelected(true);
        showTOPExtern();
    }

    /*++++Formulare TOP+++++++*/

    /**
     * Show TOP formulare.
     */
    private void showTOPFormulare() {
        if (cbTOPFormulare.isSelected() == false) {
            tfTOPFormulareDE.setVisible(true);
            tfIndexFormulareDE.setVisible(true);

        } else {
            tfTOPFormulareDE.setVisible(false);
            tfIndexFormulareDE.setVisible(false);
        }
    }

    /**
     * Fuelle formulare TOP.
     */
    private void fuelleFormulareTOP() {
        cbTOPFormulare.setSelected(true);
        showTOPFormulare();
    }

    /** The benutze englisch. */
    private boolean benutzeEnglisch = true;

    /** The benutze briefwahl. */
    private boolean benutzeBriefwahl = true;

    /**
     * Deaktiviere felder basis.
     */
    private void deaktiviereFelderBasis() {
        if (!ParamS.param.paramModuleKonfigurierbar.aktionaersportal
                && !ParamS.param.paramModuleKonfigurierbar.tabletAbstimmung
                && !ParamS.param.paramModuleKonfigurierbar.hvAppAbstimmung) {
            benutzeTOPExtern = false;
            lblExtern.setVisible(false);
            lblTOPExternDE.setVisible(false);
            lblIndexExternDE.setVisible(false);
            cbTOPExtern.setVisible(false);

            lblTOPExternEN.setVisible(false);
            lblIndexExternEN.setVisible(false);

        }

        if (!ParamS.param.paramModuleKonfigurierbar.briefwahl) {
            benutzeBriefwahl = false;
            lblWeisungBriefwahl.setVisible(false);
            cbWeisungBriefwahl.setVisible(false);
        }

        if (!ParamS.param.paramModuleKonfigurierbar.scannerAbstimmung) {
            lblStimmkartennummerPosition.setVisible(false);
            tfStimmkartennummerPosition.setVisible(false);
        }

        if (!ParamS.param.paramModuleKonfigurierbar.tabletAbstimmung) {
            lblTabletBlaetternSeite.setVisible(false);
            tfTabletBlaetternSeite.setVisible(false);

            lblTabletBlaetternPosition.setVisible(false);
            tfTabletBlaetternPosition.setVisible(false);
        }

        if (!ParamS.param.paramModuleKonfigurierbar.aktionaersportal) {
            cbAktivWeisungInternetservice.setVisible(false);
        }

        if (!ParamS.param.paramModuleKonfigurierbar.tabletAbstimmung
                && !ParamS.param.paramModuleKonfigurierbar.hvAppAbstimmung) {
            cbAktivAbstimmungTabletApp.setVisible(false);
        }
        if (!ParamS.param.paramModuleKonfigurierbar.englischeAgenda) {
            benutzeEnglisch = false;
            lblEnglisch.setVisible(false);

            lblTOPExternEN.setVisible(false);
            lblIndexExternEN.setVisible(false);

            tfKurzbezeichnungEN.setVisible(false);
            tfLangtextEN.setVisible(false);
            tfKandidatEN.setVisible(false);
        }

    }

    /**
     * Aktiviere gattung.
     *
     * @param cbGattung the cb gattung
     * @param gattung   the gattung
     */
    private void aktiviereGattung(CheckBox cbGattung, int gattung) {
        if (ParamS.param.paramBasis.getGattungAktiv(gattung)) {
            cbGattung.setText(ParamS.param.paramBasis.getGattungBezeichnungKurz(gattung));
            cbGattung.setSelected(true);
        } else {
            cbGattung.setVisible(false);
        }
    }

    /*************************Reagieren auf Eingabe***************************************************/

    @FXML
    void onCbTOPExtern(ActionEvent event) {
        showTOPExtern();
    }

    /**
     * On cb TOP formulare.
     *
     * @param event the event
     */
    @FXML
    void onCbTOPFormulare(ActionEvent event) {
        showTOPFormulare();
    }

    /**
     * On cb als ueberschrift.
     *
     * @param event the event
     */
    @FXML
    void onCbAlsUeberschrift(ActionEvent event) {
        if (cbAlsUeberschrift.isSelected()) {
            lblWeisungHolenVon.setVisible(false);
            cbWeisungHolenVon.setVisible(false);

            lblStimmenAuswerten.setVisible(false);
            cbStimmenAuswerten.setVisible(false);

            lblWeisungen.setVisible(false);

            lblWeisungSRV.setVisible(false);
            cbWeisungSRV.setVisible(false);

            lblWeisungBriefwahl.setVisible(false);
            cbWeisungBriefwahl.setVisible(false);

            lblWeisungKIAV.setVisible(false);
            cbWeisungKIAV.setVisible(false);

            lblWeisungDauer.setVisible(false);
            cbWeisungDauer.setVisible(false);

            lblWeisungOrga.setVisible(false);
            cbWeisungOrga.setVisible(false);
        } else {
            lblWeisungHolenVon.setVisible(true);
            cbWeisungHolenVon.setVisible(true);

            lblStimmenAuswerten.setVisible(true);
            cbStimmenAuswerten.setVisible(true);

            lblWeisungen.setVisible(true);

            lblWeisungSRV.setVisible(true);
            cbWeisungSRV.setVisible(true);

            if (benutzeBriefwahl) {
                lblWeisungBriefwahl.setVisible(true);
                cbWeisungBriefwahl.setVisible(true);
            }

            lblWeisungKIAV.setVisible(true);
            cbWeisungKIAV.setVisible(true);

            lblWeisungDauer.setVisible(true);
            cbWeisungDauer.setVisible(true);

            lblWeisungOrga.setVisible(true);
            cbWeisungOrga.setVisible(true);
        }
    }

    /**
     * Clicked abbrechen.
     *
     * @param event the event
     */
    @FXML
    void clickedAbbrechen(ActionEvent event) {
        rc = false;
        eigeneStage.hide();
        return;
    }

    /**
     * Clicked neu.
     *
     * @param event the event
     */
    @FXML
    void clickedNeu(ActionEvent event) {
        boolean brc = speichern();
        if (brc == false) {
            return;
        }

        rc = true;
        eigeneStage.hide();
    }

    /**
     * Clicked neue abstimmung.
     *
     * @param event the event
     */
    @FXML
    void clickedNeueAbstimmung(ActionEvent event) {
        /*Eingabe-Maske aufrufen*/
        Stage newStage = new Stage();
        CaIcon.master(newStage);
        CtrlParameterAbstimmungsvorgangNeu controllerFenster = new CtrlParameterAbstimmungsvorgangNeu();
        controllerFenster.init(newStage, stubAbstimmungen);

        CaController caController = new CaController();
        caController.open(newStage, controllerFenster,
                "/de/meetingapps/meetingclient/meetingHVMaster/ParameterAbstimmungsvorgangNeu.fxml", 1015, 200,
                "Neuer Abstimmungsvorgang", true);

        if (controllerFenster.rc) {
            CtrlParameterAbstimmungAllgemein.belegeAbstimmungsblockArray();
            CtrlParameterAbstimmungAllgemein.belegeMitAbstimmungblockArray(cbInAbstimmungsvorgang, -1);
        }
    }

    /**
     * *********************************Logiken********************************.
     *
     * @return true, if successful
     */

    private boolean speichern() {
        lFehlertext = "";

        BlAbstimmungsListeBearbeiten blAbstimmungsListeBearbeiten = new BlAbstimmungsListeBearbeiten(
                angezeigteAbstimmungen);

        EclAbstimmung abstimmung = new EclAbstimmung();

        int freieIdent = -1;
        if (cbAlsUeberschrift.isSelected()) {
            abstimmung.identWeisungssatz = -1;
        } else {
            freieIdent = blAbstimmungsListeBearbeiten.liefereFreieIdentWeisungssatz();
            if (freieIdent == -1) {
                this.fehlerMeldung("Zu viele Abstimmungen! Max 199 möglich!");
                return false;
            }
            abstimmung.identWeisungssatz = freieIdent;
        }

        /*++++++nummerKey, nummerindexKey+++++*/
        pruefeLaenge(tfTOPInternDE, "TOP Deutsch Intern", 10);
        abstimmung.nummerKey = tfTOPInternDE.getText();

        pruefeLaenge(tfIndexInternDE, "TOP Index Deutsch Intern", 10);
        abstimmung.nummerindexKey = tfIndexInternDE.getText();

        /*++++++nummer, nummerindex+++++*/
        if (cbTOPExtern.isSelected()) {
            String hString = tfTOPInternDE.getText();
            abstimmung.nummer = hString;
            abstimmung.nummerEN = hString;
            if (!hString.isEmpty()) {
                abstimmung.nummer = abstimmung.nummer + ".";
                abstimmung.nummerEN = abstimmung.nummerEN + ".";
            }

            hString = tfIndexInternDE.getText();
            abstimmung.nummerindex = hString;
            abstimmung.nummerindexEN = hString;
            if (!hString.isEmpty()) {
                abstimmung.nummerindex = abstimmung.nummerindex + ")";
                abstimmung.nummerindexEN = abstimmung.nummerindexEN + ")";
            }
        } else {
            pruefeLaenge(tfTOPExtenDE, "TOP Deutsch Extern", 10);
            abstimmung.nummer = tfTOPExtenDE.getText();

            pruefeLaenge(tfIndexExternDE, "TOP Index Deutsch Extern", 10);
            abstimmung.nummerindex = tfIndexExternDE.getText();

            pruefeLaenge(tfTOPExternEN, "TOP Englisch Extern", 10);
            abstimmung.nummerEN = tfTOPExternEN.getText();

            pruefeLaenge(tfIndexExternEN, "TOP Index Englisch Extern", 10);
            abstimmung.nummerindexEN = tfIndexExternEN.getText();

        }

        /*++++++nummerFormular, nummerindexFormular+++++*/
        if (cbTOPFormulare.isSelected()) {
            abstimmung.nummerFormular = tfTOPInternDE.getText();
            abstimmung.nummerindexFormular = tfIndexInternDE.getText();
        } else {
            pruefeLaenge(tfTOPFormulareDE, "TOP Deutsch Formulare", 10);
            abstimmung.nummerFormular = tfTOPFormulareDE.getText();

            pruefeLaenge(tfIndexExternDE, "TOP Index Deutsch Formulare", 10);
            abstimmung.nummerindexFormular = tfIndexFormulareDE.getText();
        }

        /*++++++++++++Beschreibungstexte+++++++++++++++++++++*/
        pruefeLaenge(tfKurzbezeichnungDE, "Kurzbezeichnung Deutsch", 160);
        abstimmung.kurzBezeichnung = tfKurzbezeichnungDE.getText();

        pruefeLaenge(tfLangtextDE, "Langtext Deutsch", 800);
        abstimmung.anzeigeBezeichnungLang = CaString.entferneSteuerzeichen(tfLangtextDE.getText());
        abstimmung.anzeigeBezeichnungKurz = "";

        pruefeLaenge(tfLangtextEN, "Langtext Englisch", 800);
        abstimmung.anzeigeBezeichnungLangEN = CaString.entferneSteuerzeichen(tfLangtextEN.getText());
        abstimmung.anzeigeBezeichnungKurzEN = "";

        pruefeLaenge(tfKandidatDE, "Kandidat", 160);
        abstimmung.kandidat = tfKandidatDE.getText();

        pruefeLaenge(tfKandidatDE, "Kandidat Englisch", 160);
        abstimmung.kandidatEN = tfKandidatEN.getText();

        abstimmung.stimmenAuswerten = CtrlParameterAbstimmungAllgemein
                .liefereAuswahlCbStimmenAuswerten(cbStimmenAuswerten);

        if (cbGattung1.isSelected()) {
            abstimmung.stimmberechtigteGattungen[0] = 1;
        }
        if (cbGattung2.isSelected()) {
            abstimmung.stimmberechtigteGattungen[1] = 1;
        }
        if (cbGattung3.isSelected()) {
            abstimmung.stimmberechtigteGattungen[2] = 1;
        }
        if (cbGattung4.isSelected()) {
            abstimmung.stimmberechtigteGattungen[3] = 1;
        }
        if (cbGattung5.isSelected()) {
            abstimmung.stimmberechtigteGattungen[4] = 1;
        }

        if (cbStimmausschlussV.isSelected()) {
            abstimmung.stimmausschluss = "V";
        }
        if (cbStimmausschlussA.isSelected()) {
            abstimmung.stimmausschluss += "A";
        }

        abstimmung.identErforderlicheMehrheit = CtrlParameterAbstimmungAllgemein
                .liefereAuswahlCbErforderlicheMehrheit(cbErforderlicheMehrheit);
        abstimmung.beschlussvorschlagGestelltVon = CtrlParameterAbstimmungAllgemein
                .liefereAuswahlCbBeschlussvorschlagVonMoeglichkeiten(cbBeschlussvorschlagVon);
        pruefeLaenge(tfBeschlussvorschlagSonstige, "Kandidat", 160);
        abstimmung.beschlussvorschlagGestelltVonSonstige = tfBeschlussvorschlagSonstige.getText();

        abstimmung.vonIdentGesamtweisung = CtrlParameterAbstimmungAllgemein
                .liefereAuswahlAusCbAbstimmungArray(cbWeisungHolenVon);

        /*Die Einsortierung wird erst nach Prüfung / Verarbeitung der restlichen Felder vorgenommen, da hier
         * dann bereits Veränderungen erfolgen*/

        /*Ggf. Abstimmungszuordnung Verarbeiten*/
        EclAbstimmungZuAbstimmungsblock eclAbstimmungZuAbstimmungsblock = null;
        int identAbstimmungsVorgang = CtrlParameterAbstimmungAllgemein
                .liefereAuswahlAusAbstimmungblockArray(cbInAbstimmungsvorgang);
        if (identAbstimmungsVorgang != -1) {
            eclAbstimmungZuAbstimmungsblock = new EclAbstimmungZuAbstimmungsblock();
            eclAbstimmungZuAbstimmungsblock.identAbstimmungsblock = identAbstimmungsVorgang;
            eclAbstimmungZuAbstimmungsblock.identAbstimmung = -1; /*Muß später noch belegt werden, wenn die feststeht*/

            pruefeZahlOderLeer(tfStimmkartennummer, "Stimmkartennummer");
            pruefeZahlOderLeer(tfStimmkartennummerPosition, "Stimmkarten-Position");
            pruefeZahlOderLeer(tfTabletBlaetternSeite, "Tablet-Abstimmung Seite");
            pruefeZahlOderLeer(tfTabletBlaetternPosition, "Tablet-Abstimmung Position");

            String hString = "";

            eclAbstimmungZuAbstimmungsblock.nummerDerStimmkarte = 0;
            hString = tfStimmkartennummer.getText().trim();
            if (!hString.isEmpty()) {
                eclAbstimmungZuAbstimmungsblock.nummerDerStimmkarte = Integer.parseInt(hString);
            }

            eclAbstimmungZuAbstimmungsblock.positionAufStimmkarte = 0;
            hString = tfStimmkartennummerPosition.getText().trim();
            if (!hString.isEmpty()) {
                eclAbstimmungZuAbstimmungsblock.positionAufStimmkarte = Integer.parseInt(hString);
            }

            eclAbstimmungZuAbstimmungsblock.seite = 0;
            hString = tfTabletBlaetternSeite.getText().trim();
            if (!hString.isEmpty()) {
                eclAbstimmungZuAbstimmungsblock.seite = Integer.parseInt(hString);
            }

            eclAbstimmungZuAbstimmungsblock.position = 0;
            hString = tfTabletBlaetternPosition.getText().trim();
            if (!hString.isEmpty()) {
                eclAbstimmungZuAbstimmungsblock.position = Integer.parseInt(hString);
            }

            CtrlParameterAbstimmungAllgemein.belegeAbstimmungZuAbstimmungsblockArray(identAbstimmungsVorgang);

            boolean brc = false;
            brc = CtrlParameterAbstimmungAllgemein.pruefeVorhandenStimmkarteStimmzettelInAbstimmungsblock(
                    eclAbstimmungZuAbstimmungsblock.nummerDerStimmkarte,
                    eclAbstimmungZuAbstimmungsblock.positionAufStimmkarte);
            if (brc) {
                lFehlertext = "Stimmkarte";
                if (eclAbstimmungZuAbstimmungsblock.positionAufStimmkarte != 0) {
                    lFehlertext = lFehlertext + "/Stimmkartenposition";
                }
                lFehlertext = lFehlertext + " existiert bereits!";
            }

            brc = CtrlParameterAbstimmungAllgemein.pruefeVorhandenTabletSeitePositionInAbstimmungsblock(
                    eclAbstimmungZuAbstimmungsblock.seite, eclAbstimmungZuAbstimmungsblock.position);
            if (brc) {
                lFehlertext = "Tablet-Position";
                if (eclAbstimmungZuAbstimmungsblock.seite != 0) {
                    lFehlertext = lFehlertext + "/Seite";
                }
                lFehlertext = lFehlertext + " existiert bereits!";
            }
        }

        if (!lFehlertext.isEmpty()) {
            fehlerMeldung(lFehlertext);
            return false;
        }

        /*Neuen Satz einsortieren*/
        boolean[] abstimmungWurdeVeraendert = new boolean[angezeigteAbstimmungen.length];
        for (int i = 0; i < angezeigteAbstimmungen.length; i++) {
            abstimmungWurdeVeraendert[i] = false;
        }
        int einSortierenVorAbstimmung = CtrlParameterAbstimmungAllgemein
                .liefereAuswahlAusCbAbstimmungArray(cbWeisungVorAbstimmung);
        CaBug.druckeLog("einSortierenVorAbstimmung=" + einSortierenVorAbstimmung, logDrucken, 10);
        if (einSortierenVorAbstimmung < 1) {
            /*Am Ende aufnehmen*/
            abstimmung.anzeigePositionIntern = blAbstimmungsListeBearbeiten.liefere_AnzeigePositionIntern_AmEnde();
            abstimmung.anzeigePositionExternWeisungen = blAbstimmungsListeBearbeiten
                    .liefere_AnzeigePositionExternWeisungen_AmEnde();
            abstimmung.anzeigePositionExternWeisungenHV = blAbstimmungsListeBearbeiten
                    .liefere_AnzeigePositionExternWeisungenHV_AmEnde();
        } else {
            /**
             * in einSortierenVorAbstimmung steht die Ident der Abstimmung, vor der
             * eingefügt werden soll.
             */
            int einSortierenVorAbstimmungOffset = -1;
            for (int i = 0; i < angezeigteAbstimmungen.length; i++) {
                if (angezeigteAbstimmungen[i].ident == einSortierenVorAbstimmung) {
                    einSortierenVorAbstimmungOffset = i;
                }
            }
            if (einSortierenVorAbstimmungOffset == -1) {
                CaBug.drucke("001");
            }
            /*Einsortieren*/
            blAbstimmungsListeBearbeiten.verschiebenPositionAbstimmungen(abstimmung,
                    angezeigteAbstimmungen[einSortierenVorAbstimmungOffset].anzeigePositionIntern, 1,
                    abstimmungWurdeVeraendert);
            blAbstimmungsListeBearbeiten.verschiebenPositionAbstimmungen(abstimmung,
                    angezeigteAbstimmungen[einSortierenVorAbstimmungOffset].anzeigePositionExternWeisungen, 2,
                    abstimmungWurdeVeraendert);
            blAbstimmungsListeBearbeiten.verschiebenPositionAbstimmungen(abstimmung,
                    angezeigteAbstimmungen[einSortierenVorAbstimmungOffset].anzeigePositionExternWeisungenHV, 8,
                    abstimmungWurdeVeraendert);
        }

        /*Aktivieren*/
        if (cbAktivAbstimmungWeisung.isSelected()) {
            abstimmung.aktiv = 1;
            abstimmung.aktivWeisungenAnzeige = 1;
            abstimmung.aktivWeisungenInterneAuswertungen = 1;
            abstimmung.aktivWeisungenExterneAuswertungen = 1;
            abstimmung.aktivWeisungenPflegeIntern = 1;
        }
        if (cbAktivWeisungVerlassen.isSelected()) {
            abstimmung.aktiv = 1;
            abstimmung.aktivWeisungenAufHV = 1;
        }
        if (cbAktivWeisungInternetservice.isSelected()) {
            abstimmung.aktiv = 1;
            abstimmung.aktivWeisungenInPortal = 1;
        }
        if (cbAktivAbstimmungTabletApp.isSelected()) {
            abstimmung.aktiv = 1;
            abstimmung.aktivAbstimmungInPortal = 1;
        }
        abstimmung.aktivBeiSRV = 1;
        abstimmung.aktivBeiBriefwahl = 1;
        abstimmung.aktivBeiKIAVDauer = 1;

        /*Abstimmungs-Möglichkeiten setzen*/
        abstimmung.externJa = 1;
        abstimmung.externNein = 1;
        abstimmung.externEnthaltung = 0;
        abstimmung.externUngueltig = 0;

        abstimmung.internJa = 1;
        abstimmung.internNein = 1;
        abstimmung.internEnthaltung = 1;
        abstimmung.internUngueltig = 1;

        if (ParamS.param.paramModuleKonfigurierbar.tabletAbstimmung
                || ParamS.param.paramModuleKonfigurierbar.hvAppAbstimmung) {
            if (abstimmung.stimmenAuswerten == -1) {
                abstimmung.tabletJa = 1;
                abstimmung.tabletNein = 1;
                abstimmung.tabletEnthaltung = 0;
            } else {
                abstimmung.tabletJa = 0;
                abstimmung.tabletNein = 0;
                abstimmung.tabletEnthaltung = 0;
                if (abstimmung.stimmenAuswerten != KonstStimmart.ja) {
                    abstimmung.tabletJa = 1;
                }
                if (abstimmung.stimmenAuswerten != KonstStimmart.nein) {
                    abstimmung.tabletNein = 1;
                }
                if (abstimmung.stimmenAuswerten != KonstStimmart.enthaltung) {
                    abstimmung.tabletEnthaltung = 1;
                }
            }
        }
        abstimmung.tabletUngueltig = 0;

        /*Sonstiges Standard-Belegen*/
        abstimmung.weisungNichtMarkierteSpeichernAlsSRV = -1;
        abstimmung.weisungNichtMarkierteSpeichernAlsBriefwahl = -1;
        abstimmung.weisungNichtMarkierteSpeichernAlsKIAV = -1;
        abstimmung.weisungNichtMarkierteSpeichernAlsDauer = -1;
        abstimmung.weisungNichtMarkierteSpeichernAlsOrg = -1;

        abstimmung.weisungHVNichtMarkierteSpeichernAls = -1;

        abstimmung.abstimmungNichtMarkierteSpeichernAls = -1;

        abstimmung.gegenantrag = 0;
        abstimmung.gegenantraegeGestellt = 0;
        abstimmung.ergaenzungsantrag = 0;
        abstimmung.zuAbstimmungsgruppe = 0;

        stubAbstimmungen.insertAbstimmung(abstimmung, eclAbstimmungZuAbstimmungsblock);
        stubAbstimmungen.speichernVeraenderteAngezeigteAbstimmungen(angezeigteAbstimmungen, abstimmungWurdeVeraendert);

        if (abstimmung.identWeisungssatz != -1) {
            DbBundle lDbBundle = new DbBundle(); /*Wg. Parameterfüllung*/
            BlSammelkarten blSammelkarten = new BlSammelkarten(false, lDbBundle);
            int artFuerKIAV, artFuerSRV, artFuerOrganisatorisch, artFuerBriefwahl, artFuerDauervollmacht;
            artFuerSRV = KonstStimmart.getIntVonTextKurz(cbWeisungSRV.getValue());
            artFuerKIAV = KonstStimmart.getIntVonTextKurz(cbWeisungKIAV.getValue());
            artFuerOrganisatorisch = KonstStimmart.getIntVonTextKurz(cbWeisungOrga.getValue());
            artFuerBriefwahl = KonstStimmart.getIntVonTextKurz(cbWeisungBriefwahl.getValue());
            artFuerDauervollmacht = KonstStimmart.getIntVonTextKurz(cbWeisungDauer.getValue());
            blSammelkarten.setzeWeisungFuerTOPFuerAlleAktionaereMitWeisung(freieIdent, artFuerKIAV, artFuerSRV,
                    artFuerOrganisatorisch, artFuerBriefwahl, artFuerDauervollmacht);
        }

        return true;
    }

    /** ******Übergabewerte. */
    private EclAbstimmung[] angezeigteAbstimmungen = null;

    /** Return-Wert. true=ausführen, false=beenden */
    public boolean rc = false;

    /** The stub abstimmungen. */
    private StubAbstimmungen stubAbstimmungen = null;

    /**
     * Inits the.
     *
     * @param pEigeneStage            the eigene stage
     * @param pStubAbstimmungen       the stub abstimmungen
     * @param pAngezeigteAbstimmungen the angezeigte abstimmungen
     */
    public void init(Stage pEigeneStage, StubAbstimmungen pStubAbstimmungen, EclAbstimmung[] pAngezeigteAbstimmungen) {
        eigeneStage = pEigeneStage;
        stubAbstimmungen = pStubAbstimmungen;
        angezeigteAbstimmungen = pAngezeigteAbstimmungen;
    }

}
