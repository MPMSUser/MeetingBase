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

import de.meetingapps.meetingclient.meetingClientDialoge.CaZeigeHinweis;
import de.meetingapps.meetingclient.meetingClientDialoge.CtrlRoot;
import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComAllg.CaString;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComHVParam.ParamS;
import de.meetingapps.meetingportal.meetComStub.StubParameter;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * The Class CtrlParameterBasis.
 */
public class CtrlParameterBasis extends CtrlRoot {

    /** The resources. */
    @FXML
    private ResourceBundle resources;

    /** The location. */
    @FXML
    private URL location;

    /** The tb pane annzeige. */
    @FXML
    private TabPane tbPaneAnnzeige;

    /** The tp module. */
    @FXML
    private Tab tpModule;

    /** The cb aktionaersportal. */
    @FXML
    private CheckBox cbAktionaersportal;

    /** The cb emittentenportal. */
    @FXML
    private CheckBox cbEmittentenportal;

    /** The cb registeranbindung. */
    @FXML
    private CheckBox cbRegisteranbindung;

    /** The cb elektronisches teilnehmerverzeichnis. */
    @FXML
    private CheckBox cbElektronischesTeilnehmerverzeichnis;

    /** The cb elektronische weisungserfassung HV. */
    @FXML
    private CheckBox cbElektronischeWeisungserfassungHV;

    /** The cb tablet abstimmung. */
    @FXML
    private CheckBox cbTabletAbstimmung;

    /** The cb HV app. */
    @FXML
    private CheckBox cbHVApp;

    /** The cb HV app abstimmung. */
    @FXML
    private CheckBox cbHVAppAbstimmung;

    /** The cb elektronischer stimmblock. */
    @FXML
    private CheckBox cbElektronischerStimmblock;

    /** The cb online teilnahme. */
    @FXML
    private CheckBox cbOnlineTeilnahme;

    /** The rb hv form 0. */
    @FXML
    private RadioButton rbHvForm0;

    /** The rb hv form 1. */
    @FXML
    private RadioButton rbHvForm1;

    /** The rb hv form 2. */
    @FXML
    private RadioButton rbHvForm2;

    /** The cb englische agenda. */
    @FXML
    private CheckBox cbEnglischeAgenda;

    /** The cb briefwahl. */
    @FXML
    private CheckBox cbBriefwahl;

    /** The cb scanner abstimmung. */
    @FXML
    private CheckBox cbScannerAbstimmung;

    /** The cb weisungen externes system. */
    @FXML
    private CheckBox cbWeisungenExternesSystem;

    /** The tp basis. */
    @FXML
    private Tab tpBasis;

    /** The cb inhaberaktien. */
    @FXML
    private CheckBox cbInhaberaktien;

    /** The cb namensaktien. */
    @FXML
    private CheckBox cbNamensaktien;

    /** The cb gattung 1. */
    @FXML
    private CheckBox cbGattung1;

    /** The tf gattung 1. */
    @FXML
    private TextField tfGattung1;

    /** The tf gattung 1 kurz. */
    @FXML
    private TextField tfGattung1kurz;

    /** The cb gattung 2. */
    @FXML
    private CheckBox cbGattung2;

    /** The tf gattung 2. */
    @FXML
    private TextField tfGattung2;

    /** The tf gattung 2 kurz. */
    @FXML
    private TextField tfGattung2kurz;

    /** The cb gattung 3. */
    @FXML
    private CheckBox cbGattung3;

    /** The tf gattung 3. */
    @FXML
    private TextField tfGattung3;

    /** The tf gattung 3 kurz. */
    @FXML
    private TextField tfGattung3kurz;

    /** The cb gattung 4. */
    @FXML
    private CheckBox cbGattung4;

    /** The tf gattung 4. */
    @FXML
    private TextField tfGattung4;

    /** The tf gattung 4 kurz. */
    @FXML
    private TextField tfGattung4kurz;

    /** The cb gattung 5. */
    @FXML
    private CheckBox cbGattung5;

    /** The tf gattung 5. */
    @FXML
    private TextField tfGattung5;

    /** The tf gattung 5 kurz. */
    @FXML
    private TextField tfGattung5kurz;

    /** The tf laenge aktionaersnummer. */
    @FXML
    private TextField tfLaengeAktionaersnummer;

    /** The tf investoren sind. */
    @FXML
    private TextField tfInvestorenSind;

    /** The tf ek seitenzahl. */
    @FXML
    private TextField tfEkSeitenzahl;

    /** The tf ohne null aktionaersnummer. */
    @FXML
    private TextField tfOhneNullAktionaersnummer;

    /** The tf ek formular getrennt je versandweg. */
    @FXML
    private TextField tfEkFormularGetrenntJeVersandweg;

    /** The tf eindeutige HV kennung. */
    @FXML
    private TextField tfEindeutigeHVKennung;

    /** The tf datenbestand ist produktiv. */
    @FXML
    private TextField tfDatenbestandIstProduktiv;

    /** The tf gattung 1 EN. */
    @FXML
    private TextField tfGattung1EN;

    /** The tf gattung 2 EN. */
    @FXML
    private TextField tfGattung2EN;

    /** The tf gattung 3 EN. */
    @FXML
    private TextField tfGattung3EN;

    /** The tf gattung 4 EN. */
    @FXML
    private TextField tfGattung4EN;

    /** The tf gattung 5 EN. */
    @FXML
    private TextField tfGattung5EN;

    /** The cb TP datenbestand produktiv. */
    @FXML
    private CheckBox cbTPDatenbestandProduktiv;

    /** The cb TP vorbereitung gesperrt. */
    @FXML
    private CheckBox cbTPVorbereitungGesperrt;

    /** The cb TP muster formulare ausgeblendet. */
    @FXML
    private CheckBox cbTPMusterFormulareAusgeblendet;

    /** The cb TP muster portal start seiten ausgeblendet. */
    @FXML
    private CheckBox cbTPMusterPortalStartSeitenAusgeblendet;

    /** The cb TP muster portal innen ausgeblendet. */
    @FXML
    private CheckBox cbTPMusterPortalInnenAusgeblendet;

    /** The cb TP passwort vergessen schreiben drucken. */
    @FXML
    private CheckBox cbTPPasswortVergessenSchreibenDrucken;

    /** The btn TB voller teststatus. */
    @FXML
    private Button btnTBVollerTeststatus;

    /** The btn TB voller produktivstatus. */
    @FXML
    private Button btnTBVollerProduktivstatus;

    /** The lbl voller teststatus aktiv. */
    @FXML
    private Label lblVollerTeststatusAktiv;

    /** The lbl voller produktivstatus aktiv. */
    @FXML
    private Label lblVollerProduktivstatusAktiv;

    /** The tf profile klasse. */
    @FXML
    private TextField tfProfileKlasse;

    /** The btn speichern. */
    @FXML
    private Button btnSpeichern;

    /** The lbl fehlermeldung. */
    @FXML
    private Label lblFehlermeldung;

    /** The vorrangig produktionsstatus. */
    /*+++++++++++++++++++++++Individuell+++++++++++++++++++++++++*/
    private boolean vorrangigProduktionsstatus = false;

    /**
     * Initialize.
     */
    @FXML
    void initialize() {
        
        assert tbPaneAnnzeige != null : "fx:id=\"tbPaneAnnzeige\" was not injected: check your FXML file 'ParameterBasis.fxml'.";
        assert tpModule != null : "fx:id=\"tpModule\" was not injected: check your FXML file 'ParameterBasis.fxml'.";
        assert cbAktionaersportal != null : "fx:id=\"cbAktionaersportal\" was not injected: check your FXML file 'ParameterBasis.fxml'.";
        assert cbEmittentenportal != null : "fx:id=\"cbEmittentenportal\" was not injected: check your FXML file 'ParameterBasis.fxml'.";
        assert cbRegisteranbindung != null : "fx:id=\"cbRegisteranbindung\" was not injected: check your FXML file 'ParameterBasis.fxml'.";
        assert cbElektronischesTeilnehmerverzeichnis != null : "fx:id=\"cbElektronischesTeilnehmerverzeichnis\" was not injected: check your FXML file 'ParameterBasis.fxml'.";
        assert cbElektronischeWeisungserfassungHV != null : "fx:id=\"cbElektronischeWeisungserfassungHV\" was not injected: check your FXML file 'ParameterBasis.fxml'.";
        assert cbTabletAbstimmung != null : "fx:id=\"cbTabletAbstimmung\" was not injected: check your FXML file 'ParameterBasis.fxml'.";
        assert cbHVApp != null : "fx:id=\"cbHVApp\" was not injected: check your FXML file 'ParameterBasis.fxml'.";
        assert cbHVAppAbstimmung != null : "fx:id=\"cbHVAppAbstimmung\" was not injected: check your FXML file 'ParameterBasis.fxml'.";
        assert cbElektronischerStimmblock != null : "fx:id=\"cbElektronischerStimmblock\" was not injected: check your FXML file 'ParameterBasis.fxml'.";
        assert cbOnlineTeilnahme != null : "fx:id=\"cbOnlineTeilnahme\" was not injected: check your FXML file 'ParameterBasis.fxml'.";
        assert cbEnglischeAgenda != null : "fx:id=\"cbEnglischeAgenda\" was not injected: check your FXML file 'ParameterBasis.fxml'.";
        assert cbBriefwahl != null : "fx:id=\"cbBriefwahl\" was not injected: check your FXML file 'ParameterBasis.fxml'.";
        assert cbScannerAbstimmung != null : "fx:id=\"cbScannerAbstimmung\" was not injected: check your FXML file 'ParameterBasis.fxml'.";
        assert tpBasis != null : "fx:id=\"tpBasis\" was not injected: check your FXML file 'ParameterBasis.fxml'.";
        assert cbInhaberaktien != null : "fx:id=\"cbInhaberaktien\" was not injected: check your FXML file 'ParameterBasis.fxml'.";
        assert cbNamensaktien != null : "fx:id=\"cbNamensaktien\" was not injected: check your FXML file 'ParameterBasis.fxml'.";
        assert cbGattung1 != null : "fx:id=\"cbGattung1\" was not injected: check your FXML file 'ParameterBasis.fxml'.";
        assert tfGattung1 != null : "fx:id=\"tfGattung1\" was not injected: check your FXML file 'ParameterBasis.fxml'.";
        assert tfGattung1kurz != null : "fx:id=\"tfGattung1kurz\" was not injected: check your FXML file 'ParameterBasis.fxml'.";
        assert cbGattung2 != null : "fx:id=\"cbGattung2\" was not injected: check your FXML file 'ParameterBasis.fxml'.";
        assert tfGattung2 != null : "fx:id=\"tfGattung2\" was not injected: check your FXML file 'ParameterBasis.fxml'.";
        assert tfGattung2kurz != null : "fx:id=\"tfGattung2kurz\" was not injected: check your FXML file 'ParameterBasis.fxml'.";
        assert cbGattung3 != null : "fx:id=\"cbGattung3\" was not injected: check your FXML file 'ParameterBasis.fxml'.";
        assert tfGattung3 != null : "fx:id=\"tfGattung3\" was not injected: check your FXML file 'ParameterBasis.fxml'.";
        assert tfGattung3kurz != null : "fx:id=\"tfGattung3kurz\" was not injected: check your FXML file 'ParameterBasis.fxml'.";
        assert cbGattung4 != null : "fx:id=\"cbGattung4\" was not injected: check your FXML file 'ParameterBasis.fxml'.";
        assert tfGattung4 != null : "fx:id=\"tfGattung4\" was not injected: check your FXML file 'ParameterBasis.fxml'.";
        assert tfGattung4kurz != null : "fx:id=\"tfGattung4kurz\" was not injected: check your FXML file 'ParameterBasis.fxml'.";
        assert cbGattung5 != null : "fx:id=\"cbGattung5\" was not injected: check your FXML file 'ParameterBasis.fxml'.";
        assert tfGattung5 != null : "fx:id=\"tfGattung5\" was not injected: check your FXML file 'ParameterBasis.fxml'.";
        assert tfGattung5kurz != null : "fx:id=\"tfGattung5kurz\" was not injected: check your FXML file 'ParameterBasis.fxml'.";
        assert tfLaengeAktionaersnummer != null : "fx:id=\"tfLaengeAktionaersnummer\" was not injected: check your FXML file 'ParameterBasis.fxml'.";
        assert btnSpeichern != null : "fx:id=\"btnSpeichern\" was not injected: check your FXML file 'ParameterBasis.fxml'.";
        assert lblFehlermeldung != null : "fx:id=\"lblFehlermeldung\" was not injected: check your FXML file 'ParameterBasis.fxml'.";
        
        /*** Ab hier individuell **/

        eigeneStage.setOnCloseRequest(new EventHandler<WindowEvent>() {

            @Override
            public void handle(WindowEvent event) {
                CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
                caZeigeHinweis.zeige(eigeneStage,
                        "Achtung - beim Schließen erfolgt keine erneute Speicherung! Bitte nach Schließen Parameter aktuelle Einstellungen nochmals überprüfen!");
            }
        });

        /****** paramModuleKonfigurierbar ********/
        cbAktionaersportal.setSelected(ParamS.param.paramModuleKonfigurierbar.aktionaersportal);
        cbBriefwahl.setSelected(ParamS.param.paramModuleKonfigurierbar.briefwahl);
        cbWeisungenExternesSystem
                .setSelected(ParamS.param.paramModuleKonfigurierbar.weisungenSchnittstelleExternesSystem);
        cbEmittentenportal.setSelected(ParamS.param.paramModuleKonfigurierbar.emittentenportal);
        cbRegisteranbindung.setSelected(ParamS.param.paramModuleKonfigurierbar.registeranbindung);
        cbElektronischesTeilnehmerverzeichnis
                .setSelected(ParamS.param.paramModuleKonfigurierbar.elektronischesTeilnehmerverzeichnis);
        cbTabletAbstimmung.setSelected(ParamS.param.paramModuleKonfigurierbar.tabletAbstimmung);
        cbScannerAbstimmung.setSelected(ParamS.param.paramModuleKonfigurierbar.scannerAbstimmung);
        cbHVApp.setSelected(ParamS.param.paramModuleKonfigurierbar.hvApp);
        cbHVAppAbstimmung.setSelected(ParamS.param.paramModuleKonfigurierbar.hvAppAbstimmung);
        cbElektronischerStimmblock.setSelected(ParamS.param.paramModuleKonfigurierbar.elektronischerStimmblock);
        cbOnlineTeilnahme.setSelected(ParamS.param.paramModuleKonfigurierbar.onlineTeilnahme);

        switch (ParamS.param.paramModuleKonfigurierbar.hvForm) {
        case 0:
            rbHvForm0.setSelected(true);
            break;
        case 1:
            rbHvForm1.setSelected(true);
            break;
        case 2:
            rbHvForm2.setSelected(true);
            break;
        }

        cbElektronischeWeisungserfassungHV
                .setSelected(ParamS.param.paramModuleKonfigurierbar.elektronischeWeisungserfassungHV);
        cbEnglischeAgenda.setSelected(ParamS.param.paramModuleKonfigurierbar.englischeAgenda);

        /****** paramBasis ********/
        cbInhaberaktien.setSelected(ParamS.param.paramBasis.inhaberaktienAktiv);
        cbNamensaktien.setSelected(ParamS.param.paramBasis.namensaktienAktiv);
        tfInvestorenSind.setText(Integer.toString(ParamS.param.paramBasis.investorenSind));
        tfEkFormularGetrenntJeVersandweg
                .setText(Integer.toString(ParamS.param.paramBasis.ekFormularGetrenntJeVersandweg));
        tfEindeutigeHVKennung.setText(ParamS.param.paramBasis.eindeutigeHVKennung);
        cbGattung1.setSelected(ParamS.param.paramBasis.gattungAktiv[0]);
        cbGattung2.setSelected(ParamS.param.paramBasis.gattungAktiv[1]);
        cbGattung3.setSelected(ParamS.param.paramBasis.gattungAktiv[2]);
        cbGattung4.setSelected(ParamS.param.paramBasis.gattungAktiv[3]);
        cbGattung5.setSelected(ParamS.param.paramBasis.gattungAktiv[4]);
        tfGattung1.setText(ParamS.param.paramBasis.gattungBezeichnung[0]);
        tfGattung2.setText(ParamS.param.paramBasis.gattungBezeichnung[1]);
        tfGattung3.setText(ParamS.param.paramBasis.gattungBezeichnung[2]);
        tfGattung4.setText(ParamS.param.paramBasis.gattungBezeichnung[3]);
        tfGattung5.setText(ParamS.param.paramBasis.gattungBezeichnung[4]);
        tfGattung1EN.setText(ParamS.param.paramBasis.gattungBezeichnungEN[0]);
        tfGattung2EN.setText(ParamS.param.paramBasis.gattungBezeichnungEN[1]);
        tfGattung3EN.setText(ParamS.param.paramBasis.gattungBezeichnungEN[2]);
        tfGattung4EN.setText(ParamS.param.paramBasis.gattungBezeichnungEN[3]);
        tfGattung5EN.setText(ParamS.param.paramBasis.gattungBezeichnungEN[4]);
        tfGattung1kurz.setText(ParamS.param.paramBasis.gattungBezeichnungKurz[0]);
        tfGattung2kurz.setText(ParamS.param.paramBasis.gattungBezeichnungKurz[1]);
        tfGattung3kurz.setText(ParamS.param.paramBasis.gattungBezeichnungKurz[2]);
        tfGattung4kurz.setText(ParamS.param.paramBasis.gattungBezeichnungKurz[3]);
        tfGattung5kurz.setText(ParamS.param.paramBasis.gattungBezeichnungKurz[4]);
        tfLaengeAktionaersnummer.setText(Integer.toString(ParamS.param.paramBasis.laengeAktionaersnummer));
        tfOhneNullAktionaersnummer.setText(Integer.toString(ParamS.param.paramBasis.ohneNullAktionaersnummer));
        tfEkSeitenzahl.setText(Integer.toString(ParamS.param.paramBasis.ekSeitenzahl));

        tfDatenbestandIstProduktiv.setText(Integer.toString(ParamS.eclEmittent.datenbestandIstProduktiv));

        vorrangigProduktionsstatus = ParamS.eclEmittent.liefereDatenbestandProduktivVorrangig();
        if (vorrangigProduktionsstatus) {
            lblVollerProduktivstatusAktiv.setVisible(true);
            lblVollerTeststatusAktiv.setVisible(false);
            btnTBVollerProduktivstatus.setVisible(false);
            btnTBVollerTeststatus.setVisible(true);
        } else {
            lblVollerProduktivstatusAktiv.setVisible(false);
            lblVollerTeststatusAktiv.setVisible(true);
            btnTBVollerProduktivstatus.setVisible(true);
            btnTBVollerTeststatus.setVisible(false);
        }
        cbTPDatenbestandProduktiv.setSelected(ParamS.eclEmittent.liefereDatenbestandIstProduktiv());
        cbTPVorbereitungGesperrt.setSelected(ParamS.eclEmittent.liefereDatenbestandDatenmanipulationIstGesperrt());
        cbTPMusterFormulareAusgeblendet
                .setSelected(ParamS.eclEmittent.liefereDatenbestandMusterAufFormularenAusgeblendet());
        cbTPMusterPortalStartSeitenAusgeblendet
                .setSelected(ParamS.eclEmittent.liefereDatenbestandMusterAufPortalStartseitenAusgeblendet());
        cbTPMusterPortalInnenAusgeblendet
                .setSelected(ParamS.eclEmittent.liefereDatenbestandMusterAufPortalInnenseitenAusgeblendet());
        cbTPPasswortVergessenSchreibenDrucken
                .setSelected(ParamS.eclEmittent.liefereDatenbestandPasswortVergessenSchreibenDrucken());

        tfProfileKlasse.setText(ParamS.param.paramBasis.profileKlasse);

    }

    /************************Logik***************************************************/
    private void speichernParameter() {

        /****** paramModuleKonfigurierbar ********/
        ParamS.param.paramModuleKonfigurierbar.aktionaersportal = cbAktionaersportal.isSelected();
        ParamS.param.paramModuleKonfigurierbar.briefwahl = cbBriefwahl.isSelected();
        ParamS.param.paramModuleKonfigurierbar.weisungenSchnittstelleExternesSystem = cbWeisungenExternesSystem
                .isSelected();
        ParamS.param.paramModuleKonfigurierbar.emittentenportal = cbEmittentenportal.isSelected();
        ParamS.param.paramModuleKonfigurierbar.registeranbindung = cbRegisteranbindung.isSelected();
        ParamS.param.paramModuleKonfigurierbar.elektronischesTeilnehmerverzeichnis = cbElektronischesTeilnehmerverzeichnis
                .isSelected();
        ParamS.param.paramModuleKonfigurierbar.tabletAbstimmung = cbTabletAbstimmung.isSelected();
        ParamS.param.paramModuleKonfigurierbar.scannerAbstimmung = cbScannerAbstimmung.isSelected();
        ParamS.param.paramModuleKonfigurierbar.hvApp = cbHVApp.isSelected();
        ParamS.param.paramModuleKonfigurierbar.hvAppAbstimmung = cbHVAppAbstimmung.isSelected();
        ParamS.param.paramModuleKonfigurierbar.elektronischerStimmblock = cbElektronischerStimmblock.isSelected();
        ParamS.param.paramModuleKonfigurierbar.onlineTeilnahme = cbOnlineTeilnahme.isSelected();

        ParamS.param.paramModuleKonfigurierbar.hvForm = 0;
        if (rbHvForm1.isSelected()) {
            ParamS.param.paramModuleKonfigurierbar.hvForm = 1;
        }
        if (rbHvForm2.isSelected()) {
            ParamS.param.paramModuleKonfigurierbar.hvForm = 2;
        }

        ParamS.param.paramModuleKonfigurierbar.elektronischeWeisungserfassungHV = cbElektronischeWeisungserfassungHV
                .isSelected();
        ParamS.param.paramModuleKonfigurierbar.englischeAgenda = cbEnglischeAgenda.isSelected();

        /****** paramBasis ********/
        ParamS.param.paramBasis.inhaberaktienAktiv = cbInhaberaktien.isSelected();
        ParamS.param.paramBasis.namensaktienAktiv = cbNamensaktien.isSelected();
        ParamS.param.paramBasis.gattungAktiv[0] = cbGattung1.isSelected();
        ParamS.param.paramBasis.gattungAktiv[1] = cbGattung2.isSelected();
        ParamS.param.paramBasis.gattungAktiv[2] = cbGattung3.isSelected();
        ParamS.param.paramBasis.gattungAktiv[3] = cbGattung4.isSelected();
        ParamS.param.paramBasis.gattungAktiv[4] = cbGattung5.isSelected();
        ParamS.param.paramBasis.gattungBezeichnung[0] = tfGattung1.getText().trim();
        ParamS.param.paramBasis.gattungBezeichnung[1] = tfGattung2.getText().trim();
        ParamS.param.paramBasis.gattungBezeichnung[2] = tfGattung3.getText().trim();
        ParamS.param.paramBasis.gattungBezeichnung[3] = tfGattung4.getText().trim();
        ParamS.param.paramBasis.gattungBezeichnung[4] = tfGattung5.getText().trim();
        ParamS.param.paramBasis.gattungBezeichnungEN[0] = tfGattung1EN.getText().trim();
        ParamS.param.paramBasis.gattungBezeichnungEN[1] = tfGattung2EN.getText().trim();
        ParamS.param.paramBasis.gattungBezeichnungEN[2] = tfGattung3EN.getText().trim();
        ParamS.param.paramBasis.gattungBezeichnungEN[3] = tfGattung4EN.getText().trim();
        ParamS.param.paramBasis.gattungBezeichnungEN[4] = tfGattung5EN.getText().trim();
        ParamS.param.paramBasis.gattungBezeichnungKurz[0] = tfGattung1kurz.getText().trim();
        ParamS.param.paramBasis.gattungBezeichnungKurz[1] = tfGattung2kurz.getText().trim();
        ParamS.param.paramBasis.gattungBezeichnungKurz[2] = tfGattung3kurz.getText().trim();
        ParamS.param.paramBasis.gattungBezeichnungKurz[3] = tfGattung4kurz.getText().trim();
        ParamS.param.paramBasis.gattungBezeichnungKurz[4] = tfGattung5kurz.getText().trim();

        String hString = tfLaengeAktionaersnummer.getText().trim();
        if (CaString.isNummern(hString)) {
        } else {
            hString = "0";
        }
        ParamS.param.paramBasis.laengeAktionaersnummer = Integer.parseInt(hString);

        hString = tfEkSeitenzahl.getText().trim();
        if (CaString.isNummern(hString)) {
        } else {
            hString = "0";
        }
        ParamS.param.paramBasis.ekSeitenzahl = Integer.parseInt(hString);

        hString = tfEkFormularGetrenntJeVersandweg.getText().trim();
        if (CaString.isNummern(hString)) {
        } else {
            hString = "1";
        }
        ParamS.param.paramBasis.ekFormularGetrenntJeVersandweg = Integer.parseInt(hString);

        hString = tfOhneNullAktionaersnummer.getText().trim();
        if (CaString.isNummern(hString)) {
        } else {
            hString = "1";
        }
        ParamS.param.paramBasis.ohneNullAktionaersnummer = Integer.parseInt(hString);

        hString = tfInvestorenSind.getText().trim();
        if (CaString.isNummern(hString)) {

        } else {
            hString = "1";
        }
        ParamS.param.paramBasis.investorenSind = Integer.parseInt(hString);

        hString = tfEindeutigeHVKennung.getText().trim();
        ParamS.param.paramBasis.eindeutigeHVKennung = hString;

        ParamS.param.paramBasis.profileKlasse = tfProfileKlasse.getText().trim();

        DbBundle lDbBundle = new DbBundle();
        StubParameter stubParameter = new StubParameter(false, lDbBundle);
        int erg = stubParameter.updateHVParam_all(ParamS.param);
        if (erg == CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert) {
            CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
            caZeigeHinweis.zeige(eigeneStage,
                    "Achtung, anderer User im System - Ihre Änderungen wurden nicht gespeichert!");
        } else {

            /*die Emittentenparameter in Emittent übertragen, dann diesen auch speichern*/
            if (ParamS.param.paramModuleKonfigurierbar.aktionaersportal) {
                ParamS.eclEmittent.portalVorhanden = 1;
            } else {
                ParamS.eclEmittent.portalVorhanden = 0;
            }

            if (ParamS.param.paramModuleKonfigurierbar.emittentenportal) {
                ParamS.eclEmittent.emittentenPortalVorhanden = 1;
            } else {
                ParamS.eclEmittent.emittentenPortalVorhanden = 0;
            }

            if (vorrangigProduktionsstatus) {
                ParamS.eclEmittent.belegeDatenbestandKomplettProduktiv();
            } else {
                ParamS.eclEmittent.belegeDatenbestandKomplettTest();
            }
            ParamS.eclEmittent.belegeDatenbestandIstProduktiv(cbTPDatenbestandProduktiv.isSelected());
            ParamS.eclEmittent.belegeDatenbestandDatenmanipulationIstGesperrt(cbTPVorbereitungGesperrt.isSelected());
            ParamS.eclEmittent
                    .belegeDatenbestandMusterAufFormularenAusgeblendet(cbTPMusterFormulareAusgeblendet.isSelected());
            ParamS.eclEmittent.belegeDatenbestandMusterAufPortalStartseitenAusgeblendet(
                    cbTPMusterPortalStartSeitenAusgeblendet.isSelected());
            ParamS.eclEmittent.belegeDatenbestandMusterAufPortalInnenseitenAusgeblendet(
                    cbTPMusterPortalInnenAusgeblendet.isSelected());
            ParamS.eclEmittent.belegeDatenbestandPasswortVergessenSchreibenDrucken(
                    cbTPPasswortVergessenSchreibenDrucken.isSelected());

            if (ParamS.param.paramModuleKonfigurierbar.registeranbindung) {
                ParamS.eclEmittent.registerAnbindungVorhanden = 1;
            } else {
                ParamS.eclEmittent.registerAnbindungVorhanden = 0;
            }

            int lAppAktiv = ParamS.eclEmittent.appAktiv;
            if (ParamS.param.paramModuleKonfigurierbar.hvApp) {
                if (lAppAktiv == 2) {
                    ParamS.eclEmittent.appAktiv = 2;
                } else {
                    ParamS.eclEmittent.appAktiv = 1;
                }
            } else {
                ParamS.eclEmittent.appAktiv = 0;
            }

            lDbBundle.openAllOhneParameterCheck();
            lDbBundle.dbEmittenten.update(ParamS.eclEmittent);

            lDbBundle.closeAll();
        }

    }

    /**
     * ************************Anzeigefunktionen**************************************.
     */

    private Stage eigeneStage;

    /**
     * Inits the.
     *
     * @param pEigeneStage the eigene stage
     */
    public void init(Stage pEigeneStage) {
        /** HV-Parameter sicherheitshalber neu holen */
        DbBundle lDbBundle = new DbBundle();
        StubParameter stubParameter = new StubParameter(false, lDbBundle);
        stubParameter.leseHVParam_all(lDbBundle.clGlobalVar.mandant, lDbBundle.clGlobalVar.hvJahr,
                lDbBundle.clGlobalVar.hvNummer, lDbBundle.clGlobalVar.datenbereich);
        ParamS.param = stubParameter.rcHVParam;

        eigeneStage = pEigeneStage;
    }

    /**************************Anzeigefunktionen***************************************/
    
    @FXML
    void clickedSpeichern(ActionEvent event) {

        speichernParameter();

        eigeneStage.hide();

    }

    /**
     * On tp changed.
     *
     * @param event the event
     */
    @FXML
    void onTpChanged(MouseEvent event) {

    }

    /**
     * Clicked TB voller produktivstatus.
     *
     * @param event the event
     */
    @FXML
    void clickedTBVollerProduktivstatus(ActionEvent event) {
        vorrangigProduktionsstatus = true;
        setzeTPHaken(true);
    }

    /**
     * Clicked TB voller teststatus.
     *
     * @param event the event
     */
    @FXML
    void clickedTBVollerTeststatus(ActionEvent event) {
        vorrangigProduktionsstatus = false;
        setzeTPHaken(false);
    }

    /**
     * Sets the ze TP haken.
     *
     * @param pJa the new ze TP haken
     */
    private void setzeTPHaken(boolean pJa) {
        cbTPDatenbestandProduktiv.setSelected(pJa);
        cbTPVorbereitungGesperrt.setSelected(pJa);
        cbTPMusterFormulareAusgeblendet.setSelected(pJa);
        cbTPMusterPortalStartSeitenAusgeblendet.setSelected(pJa);
        cbTPMusterPortalInnenAusgeblendet.setSelected(pJa);
        cbTPPasswortVergessenSchreibenDrucken.setSelected(pJa);

        if (vorrangigProduktionsstatus) {
            lblVollerProduktivstatusAktiv.setVisible(true);
            lblVollerTeststatusAktiv.setVisible(false);
            btnTBVollerProduktivstatus.setVisible(false);
            btnTBVollerTeststatus.setVisible(true);
        } else {
            lblVollerProduktivstatusAktiv.setVisible(false);
            lblVollerTeststatusAktiv.setVisible(true);
            btnTBVollerProduktivstatus.setVisible(true);
            btnTBVollerTeststatus.setVisible(false);
        }

    }

}
