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
package de.meetingapps.meetingclient.meetingDesign;

import java.net.URL;
import java.util.ResourceBundle;

import de.meetingapps.meetingclient.meetingClientDialoge.CtrlRoot;
import de.meetingapps.meetingclient.meetingClientOberflaechen.ObjectActions;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComHVParam.ParamS;
import de.meetingapps.meetingportal.meetingCoreReport.RpDrucken;
import de.meetingapps.meetingportal.meetingCoreReport.RpVariablen;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

/**
 * The Class DlAufrufDesigner.
 */
public class DlAufrufDesigner extends CtrlRoot {
    
    public final int width = 900;
    public final int height = 720;
    
    /** The n LL job. */
    int nLLJob_;

    /** The h wnd. */
    int hWnd_;

    /** The rp drucken. */
    private RpDrucken rpDrucken;
    
    @FXML
    private AnchorPane rootPane;

    /** The resources. */
    @FXML
    private ResourceBundle resources;

    /** The location. */
    @FXML
    private URL location;

    /** The tf laufwerk. */
    @FXML
    private TextField tfLaufwerk;

    /** The tf mandant. */
    @FXML
    private TextField tfMandant;

    /** The tf dokument nummer. */
    @FXML
    private TextField tfDokumentNummer;

    /** The btn designer as is liste. */
    @FXML
    private Button btnDesignerAsIsListe;

    /** The btn designer as is label. */
    @FXML
    private Button btnDesignerAsIsLabel;

    /** The btn ek selbstdruck. */
    @FXML
    private Button btnEkSelbstdruck;

    /** The btn ek email. */
    @FXML
    private Button btnEkEmail;

    /** The btn ek papier. */
    @FXML
    private Button btnEkPapier;

    /** The btn gast email. */
    @FXML
    private Button btnGastEmail;

    /** The btn gast stapel. */
    @FXML
    private Button btnGastStapel;

    /** The btn abstimmung aktionaer. */
    @FXML
    private Button btnAbstimmungAktionaer;

    /** The btn meldeliste 01. */
    @FXML
    private Button btnMeldeliste01;

    /** The btn erklaerungen. */
    @FXML
    private Button btnErklaerungen;

    /** The btn negativliste. */
    @FXML
    private Button btnNegativliste;

    /** The btn stimmausschluss liste. */
    @FXML
    private Button btnStimmausschlussListe;

    /** The btn mail registrierungs liste. */
    @FXML
    private Button btnMailRegistrierungsListe;

    /** The btn test barcodes. */
    @FXML
    private Button btnTestBarcodes;

    /** The btn test einladungen. */
    @FXML
    private Button btnTestEinladungen;

    /** The btn praesenzliste. */
    @FXML
    private Button btnPraesenzliste;

    /** The btn praesenzprotokoll. */
    @FXML
    private Button btnPraesenzprotokoll;

    /** The btn meldung statistik. */
    @FXML
    private Button btnMeldungStatistik;

    /** The btn sammelkarten kurz. */
    @FXML
    private Button btnSammelkartenKurz;

    /** The btn sammelkarten mit weisung. */
    @FXML
    private Button btnSammelkartenMitWeisung;

    /** The btn sammelkarten mit aktionaeren. */
    @FXML
    private Button btnSammelkartenMitAktionaeren;

    /** The btn weisungssummen. */
    @FXML
    private Button btnWeisungssummen;

    /** The btn kontrolliste weisungen. */
    @FXML
    private Button btnKontrollisteWeisungen;

    /** The btn gastkarten liste. */
    @FXML
    private Button btnGastkartenListe;

    /** The btn gastkarten uebersicht liste. */
    @FXML
    private Button btnGastkartenUebersichtListe;

    /** The btn praesenz zusammenstellung. */
    @FXML
    private Button btnPraesenzZusammenstellung;

    /** The btn abstimmung ergebnis liste. */
    @FXML
    private Button btnAbstimmungErgebnisListe;

    /** The btn abstimmung vorleseblatt. */
    @FXML
    private Button btnAbstimmungVorleseblatt;

    /** The btn abstimmung zwischenblatt. */
    @FXML
    private Button btnAbstimmungZwischenblatt;

    /** The btn sammelanmeldebogen. */
    @FXML
    private Button btnSammelanmeldebogen;

    /** The btn datenpflege ku178. */
    @FXML
    private Button btnDatenpflegeku178;

    /** The btn anschreiben passwort. */
    @FXML
    private Button btnAnschreibenPasswort;

    /** The btn fragen mitteilungen. */
    @FXML
    private Button btnFragenMitteilungen;

    /** The btn bestaetigung weisung. */
    @FXML
    private Button btnBestaetigungWeisung;

    /** The btn vollmachtsformular. */
    @FXML
    private Button btnVollmachtsformular;

    /** The btn verzeichnis verein zusammenstellung. */
    @FXML
    private Button btnVerzeichnisVereinZusammenstellung;

    /** The btn verzeichnis verein liste. */
    @FXML
    private Button btnVerzeichnisVereinListe;

    /** The btn bestaetigung weisung mit top. */
    @FXML
    private Button btnBestaetigungWeisungMitTop;

    /** The btn formular erstzugang. */
    @FXML
    private Button btnFormularErstzugang;

    /** The btn EK freiwillige anmeldung. */
    @FXML
    private Button btnEKFreiwilligeAnmeldung;

    /** The btn test druckerzuordnung. */
    @FXML
    private Button btnTestDruckerzuordnung;

    /** The btn konsistenzpruefung. */
    @FXML
    private Button btnKonsistenzpruefung;

    /** The btn stimmkarten. */
    @FXML
    private Button btnStimmkarten;

    /** The btn veranstaltungsmanagement. */
    @FXML
    private Button btnVeranstaltungsmanagement;

    /** The btn batch. */
    @FXML
    private Button btnBatch;

    /**
     * Initialize.
     */
    @FXML
    void initialize() {
        assert tfLaufwerk != null : "fx:id=\"tfLaufwerk\" was not injected: check your FXML file 'DlAufrufDesigner.fxml'.";
        assert tfMandant != null : "fx:id=\"tfMandant\" was not injected: check your FXML file 'DlAufrufDesigner.fxml'.";
        assert tfDokumentNummer != null : "fx:id=\"tfDokumentNummer\" was not injected: check your FXML file 'DlAufrufDesigner.fxml'.";
        assert btnDesignerAsIsListe != null : "fx:id=\"btnDesignerAsIsListe\" was not injected: check your FXML file 'DlAufrufDesigner.fxml'.";
        assert btnDesignerAsIsLabel != null : "fx:id=\"btnDesignerAsIsLabel\" was not injected: check your FXML file 'DlAufrufDesigner.fxml'.";
        assert btnEkSelbstdruck != null : "fx:id=\"btnEkSelbstdruck\" was not injected: check your FXML file 'DlAufrufDesigner.fxml'.";
        assert btnEkEmail != null : "fx:id=\"btnEkEmail\" was not injected: check your FXML file 'DlAufrufDesigner.fxml'.";
        assert btnEkPapier != null : "fx:id=\"btnEkPapier\" was not injected: check your FXML file 'DlAufrufDesigner.fxml'.";
        assert btnGastEmail != null : "fx:id=\"btnGastEmail\" was not injected: check your FXML file 'DlAufrufDesigner.fxml'.";
        assert btnGastStapel != null : "fx:id=\"btnGastStapel\" was not injected: check your FXML file 'DlAufrufDesigner.fxml'.";
        assert btnAbstimmungAktionaer != null : "fx:id=\"btnAbstimmungAktionaer\" was not injected: check your FXML file 'DlAufrufDesigner.fxml'.";
        assert btnMeldeliste01 != null : "fx:id=\"btnMeldeliste01\" was not injected: check your FXML file 'DlAufrufDesigner.fxml'.";
        assert btnErklaerungen != null : "fx:id=\"btnErklaerungen\" was not injected: check your FXML file 'DlAufrufDesigner.fxml'.";
        assert btnNegativliste != null : "fx:id=\"btnNegativliste\" was not injected: check your FXML file 'DlAufrufDesigner.fxml'.";
        assert btnStimmausschlussListe != null : "fx:id=\"btnNegativliste\" was not injected: check your FXML file 'DlAufrufDesigner.fxml'.";
        assert btnMailRegistrierungsListe != null : "fx:id=\"btnMailRegistrierungsListe\" was not injected: check your FXML file 'DlAufrufDesigner.fxml'.";
        assert btnTestBarcodes != null : "fx:id=\"btnTestBarcodes\" was not injected: check your FXML file 'DlAufrufDesigner.fxml'.";
        assert btnTestEinladungen != null : "fx:id=\"btnTestEinladungen\" was not injected: check your FXML file 'DlAufrufDesigner.fxml'.";
        assert btnPraesenzliste != null : "fx:id=\"btnPraesenzliste\" was not injected: check your FXML file 'DlAufrufDesigner.fxml'.";
        assert btnPraesenzprotokoll != null : "fx:id=\"btnPraesenzprotokoll\" was not injected: check your FXML file 'DlAufrufDesigner.fxml'.";
        assert btnMeldungStatistik != null : "fx:id=\"btnMeldungStatistik\" was not injected: check your FXML file 'DlAufrufDesigner.fxml'.";
        assert btnSammelkartenKurz != null : "fx:id=\"btnSammelkartenKurz\" was not injected: check your FXML file 'DlAufrufDesigner.fxml'.";
        assert btnSammelkartenMitWeisung != null : "fx:id=\"btnSammelkartenMitWeisung\" was not injected: check your FXML file 'DlAufrufDesigner.fxml'.";
        assert btnSammelkartenMitAktionaeren != null : "fx:id=\"btnSammelkartenMitAktionaeren\" was not injected: check your FXML file 'DlAufrufDesigner.fxml'.";
        assert btnWeisungssummen != null : "fx:id=\"btnWeisungssummen\" was not injected: check your FXML file 'DlAufrufDesigner.fxml'.";
        assert btnKontrollisteWeisungen != null : "fx:id=\"btnKontrollisteWeisungen\" was not injected: check your FXML file 'DlAufrufDesigner.fxml'.";
        assert btnGastkartenListe != null : "fx:id=\"btnGastkartenListe\" was not injected: check your FXML file 'DlAufrufDesigner.fxml'.";
        assert btnGastkartenUebersichtListe != null : "fx:id=\"btnGastkartenListe\" was not injected: check your FXML file 'DlAufrufDesigner.fxml'.";
        assert btnPraesenzZusammenstellung != null : "fx:id=\"btnPraesenzZusammenstellung\" was not injected: check your FXML file 'DlAufrufDesigner.fxml'.";
        assert btnAbstimmungErgebnisListe != null : "fx:id=\"btnAbstimmungErgebnisListe\" was not injected: check your FXML file 'DlAufrufDesigner.fxml'.";
        assert btnAbstimmungVorleseblatt != null : "fx:id=\"btnAbstimmungVorleseblatt\" was not injected: check your FXML file 'DlAufrufDesigner.fxml'.";

        tfLaufwerk.setText(ParamS.clGlobalVar.lwPfadAllgemein);
        tfMandant.setText(Integer.toString(ParamS.clGlobalVar.mandant));
        tfDokumentNummer.setText("01");

        eigeneStage.addEventHandler(KeyEvent.KEY_PRESSED, e -> {
            if (e.getCode() == KeyCode.M) {
                eigeneStage.hide();
            }
        });
        
        ObjectActions.createInfoButton(rootPane, new String[] { "M - Modulauswahl/Zurück" }, 30.0, 5.0);
    }

    /**
     * Initialize LL.
     *
     * @return true, if successful
     */
    public boolean initializeLL() {
        // add here your preferred DokumentGenerator
        return true;
    }

    /**
     * On btn designer as is liste.
     *
     * @param event the event
     */
    @FXML
    void onBtnDesignerAsIsListe(MouseEvent event) {
        // add here your preferred DokumentGenerator
    }

    /**
     * On btn designer as is label.
     *
     * @param event the event
     */
    @FXML
    void onBtnDesignerAsIsLabel(MouseEvent event) {
        // add here your preferred DokumentGenerator
    }

    /**
     * On btn ek selbstdruck.
     *
     * @param event the event
     */
    @FXML
    void onBtnEkSelbstdruck(MouseEvent event) {
        rufeDesigner(1);
    }

    /**
     * On btn ek mail.
     *
     * @param event the event
     */
    @FXML
    void onBtnEkMail(MouseEvent event) {
        rufeDesigner(2);
    }

    /**
     * On btn ek papier.
     *
     * @param event the event
     */
    @FXML
    void onBtnEkPapier(MouseEvent event) {
        rufeDesigner(6);

    }

    /**
     * On btn gast selbstdruck.
     *
     * @param event the event
     */
    @FXML
    void onBtnGastSelbstdruck(MouseEvent event) {
        rufeDesigner(3);

    }

    /**
     * On btn gast mail.
     *
     * @param event the event
     */
    @FXML
    void onBtnGastMail(MouseEvent event) {
        rufeDesigner(4);

    }

    /**
     * On btn gast stapel.
     *
     * @param event the event
     */
    @FXML
    void onBtnGastStapel(MouseEvent event) {
        rufeDesigner(26);

    }

    /**
     * On btn abstimmung aktionaer.
     *
     * @param event the event
     */
    @FXML
    void onBtnAbstimmungAktionaer(MouseEvent event) {
        rufeDesignerList(5);
    }

    /**
     * On btn meldeliste 01.
     *
     * @param event the event
     */
    @FXML
    void onBtnMeldeliste01(MouseEvent event) {
        rufeDesignerList(7);

    }

    /**
     * On btn erklaerungen.
     *
     * @param event the event
     */
    @FXML
    void onBtnErklaerungen(MouseEvent event) {
        rufeDesignerList(8);
    }

    /**
     * On btn negativliste.
     *
     * @param event the event
     */
    @FXML
    void onBtnNegativliste(MouseEvent event) {
        rufeDesignerList(9);

    }

    /**
     * On btn stimmausschluss liste.
     *
     * @param event the event
     */
    @FXML
    void onBtnStimmausschlussListe(MouseEvent event) {
        rufeDesignerList(27);

    }

    /**
     * On btn mail registrierungs liste.
     *
     * @param event the event
     */
    @FXML
    void onBtnMailRegistrierungsListe(MouseEvent event) {
        rufeDesignerList(10);

    }

    /**
     * On btn test barcodes.
     *
     * @param event the event
     */
    @FXML
    void onBtnTestBarcodes(MouseEvent event) {
        rufeDesigner(11);
    }

    /**
     * On btn test einladungen.
     *
     * @param event the event
     */
    @FXML
    void onBtnTestEinladungen(MouseEvent event) {
        rufeDesigner(24);
    }

    /**
     * On btn praesenzliste.
     *
     * @param event the event
     */
    @FXML
    void onBtnPraesenzliste(MouseEvent event) {
        rufeDesignerList(12);

    }

    /**
     * On btn praesenzprotokoll.
     *
     * @param event the event
     */
    @FXML
    void onBtnPraesenzprotokoll(MouseEvent event) {
        rufeDesignerList(13);

    }

    /**
     * On btn meldung statistik.
     *
     * @param event the event
     */
    @FXML
    void onBtnMeldungStatistik(MouseEvent event) {
        rufeDesigner(14);

    }

    /**
     * On btn sammelkarten kurz.
     *
     * @param event the event
     */
    @FXML
    void onBtnSammelkartenKurz(MouseEvent event) {
        rufeDesignerList(15);

    }

    /**
     * On btn sammelkarten mit weisung.
     *
     * @param event the event
     */
    @FXML
    void onBtnSammelkartenMitWeisung(MouseEvent event) {
        rufeDesignerList(16);

    }

    /**
     * On btn sammelkarten mit aktionaeren.
     *
     * @param event the event
     */
    @FXML
    void onBtnSammelkartenMitAktionaeren(MouseEvent event) {
        rufeDesignerList(17);

    }

    /**
     * On btn weisungssummen.
     *
     * @param event the event
     */
    @FXML
    void onBtnWeisungssummen(MouseEvent event) {
        rufeDesignerList(18);

    }

    /**
     * On kontrolliste weisungen.
     *
     * @param event the event
     */
    @FXML
    void onKontrollisteWeisungen(MouseEvent event) {
        rufeDesignerList(19);

    }

    /**
     * On gastkarten liste.
     *
     * @param event the event
     */
    @FXML
    void onGastkartenListe(MouseEvent event) {
        rufeDesignerList(20);

    }

    /**
     * On gastkarten uebersicht liste.
     *
     * @param event the event
     */
    @FXML
    void onGastkartenUebersichtListe(MouseEvent event) {
        rufeDesignerList(25);

    }

    /**
     * On btn praesenz zusammenstellung.
     *
     * @param event the event
     */
    @FXML
    void onBtnPraesenzZusammenstellung(MouseEvent event) {
        rufeDesigner(21);

    }

    /**
     * On abstimmung ergebnis liste.
     *
     * @param event the event
     */
    @FXML
    void onAbstimmungErgebnisListe(MouseEvent event) {
        rufeDesignerList(22);

    }

    /**
     * On abstimmung vorleseblatt.
     *
     * @param event the event
     */
    @FXML
    void onAbstimmungVorleseblatt(MouseEvent event) {
        rufeDesigner(23);

    }

    /**
     * On abstimmung zwischenblatt.
     *
     * @param event the event
     */
    @FXML
    void onAbstimmungZwischenblatt(MouseEvent event) {
        rufeDesigner(28);

    }

    /**
     * On sammelanmeldebogen.
     *
     * @param event the event
     */
    @FXML
    void onSammelanmeldebogen(MouseEvent event) {
        rufeDesignerList(29);
    }

    /**
     * On datenpflege ku178.
     *
     * @param event the event
     */
    @FXML
    void onDatenpflegeku178(MouseEvent event) {
        rufeDesigner(30);
    }

    /**
     * On anschreiben passwort.
     *
     * @param event the event
     */
    @FXML
    void onAnschreibenPasswort(MouseEvent event) {
        rufeDesigner(31);
    }

    /**
     * On fragen mitteilungen.
     *
     * @param event the event
     */
    @FXML
    void onFragenMitteilungen(MouseEvent event) {
        rufeDesigner(32);
    }

    /**
     * On bestaetigung weisung.
     *
     * @param event the event
     */
    @FXML
    void onBestaetigungWeisung(MouseEvent event) {
        rufeDesigner(33);
    }

    /**
     * On vollmachtsformular.
     *
     * @param event the event
     */
    @FXML
    void onVollmachtsformular(MouseEvent event) {
        rufeDesigner(34);
    }

    /**
     * On verzeichnis verein zusammenstellung.
     *
     * @param event the event
     */
    @FXML
    void onVerzeichnisVereinZusammenstellung(MouseEvent event) {
        rufeDesigner(35);
    }

    /**
     * On verzeichnis verein liste.
     *
     * @param event the event
     */
    @FXML
    void onVerzeichnisVereinListe(MouseEvent event) {
        rufeDesignerList(36);
    }

    /**
     * On bestaetigung weisung mit top.
     *
     * @param event the event
     */
    @FXML
    void onBestaetigungWeisungMitTop(MouseEvent event) {
        rufeDesignerList(37);
    }

    /**
     * On EK freiwillige anmeldung.
     *
     * @param event the event
     */
    @FXML
    void onEKFreiwilligeAnmeldung(MouseEvent event) {
        rufeDesigner(38);
    }

    /**
     * On formular erstzugang.
     *
     * @param event the event
     */
    @FXML
    void onFormularErstzugang(MouseEvent event) {
        rufeDesigner(39);
    }

    /**
     * On test druckerzuordnung.
     *
     * @param event the event
     */
    @FXML
    void onTestDruckerzuordnung(MouseEvent event) {
        rufeDesigner(40);
    }

    /**
     * On konsistenzpruefung.
     *
     * @param event the event
     */
    @FXML
    void onKonsistenzpruefung(MouseEvent event) {
        rufeDesignerList(41);
    }

    /**
     * On stimmkarten.
     *
     * @param event the event
     */
    @FXML
    void onStimmkarten(MouseEvent event) {
        rufeDesigner(42);
    }

    /**
     * On veranstaltungsmanagement.
     *
     * @param event the event
     */
    @FXML
    void onVeranstaltungsmanagement(MouseEvent event) {
        rufeDesignerList(43);
    }

    /**
     * On batch.
     *
     * @param event the event
     */
    @FXML
    void onBatch(MouseEvent event) {
        rufeDesigner(44);
    }

    /*Verwendet bis einschließlich 27*/

    /**
     * Rufe designer.
     *
     * @param lfdFormular the lfd formular
     */
    void rufeDesigner(int lfdFormular) {

        String lDokumentNummer = "";
        lDokumentNummer = tfDokumentNummer.getText();
        if (lDokumentNummer == null) {
            lDokumentNummer = "";
        }
        while (lDokumentNummer.length() < 2) {
            lDokumentNummer = "0" + lDokumentNummer;
        }

        //Initialize the List & Label job

        if (initializeLL() == false) {
            return;
        }
        ParamS.clGlobalVar.lwPfadAllgemein = tfLaufwerk.getText();
        ParamS.clGlobalVar.mandant = Integer.parseInt(tfMandant.getText());

        DbBundle lDbBundle = new DbBundle(); /*Nur für Mandanten-Nr!*/

        RpVariablen rpVariablen = new RpVariablen(lDbBundle);
        switch (lfdFormular) {
        case 1:
            rpVariablen.ekSelbstdruck(rpDrucken);
            break;
        case 2:
            rpVariablen.ekMail(rpDrucken);
            break;
        /*TODO #1 Gastkarten noch mal testen im Portal!"  	 */
        case 3:
            rpVariablen.gastkarteSelbstdruck(lDokumentNummer, rpDrucken);
            break;
        case 4:
            rpVariablen.gastkarteMail(lDokumentNummer, rpDrucken);
            break;
        case 6:
            //            rpVariablen.ekPapier_alt(rpDrucken);
            rpVariablen.ekPapier(rpDrucken);
            break;
        case 11:
            rpVariablen.testBarcodes(lDokumentNummer, rpDrucken);
            break;
        case 14:
            rpVariablen.statistikMeldung(lDokumentNummer, rpDrucken);
            break;
        case 21:
            rpVariablen.praesenzZusammenstellung(lDokumentNummer, rpDrucken);
            break;
        case 23:
            rpVariablen.abstimmungVerleseblatt(lDokumentNummer, rpDrucken);
            break;
        case 24:
            rpVariablen.testEinladungen(lDokumentNummer, rpDrucken);
            break;
        case 26:
            rpVariablen.gkStapel(lDokumentNummer, rpDrucken);
            break;
        case 28:
            rpVariablen.abstimmungZwischenblatt(lDokumentNummer, rpDrucken);
            break;
        case 30:
            rpVariablen.datenpflegeMeldung(lDokumentNummer, rpDrucken);
            break;
        case 31:
            rpVariablen.anschreibenNeuesPasswort(lDokumentNummer, rpDrucken);
            break;
        case 32:
            rpVariablen.mitteilung(lDokumentNummer, rpDrucken);
            break;
        case 33:
            rpVariablen.weisungBestaetigung(lDokumentNummer, rpDrucken, false);
            break;
        case 34:
            rpVariablen.vollmachtsFormular(lDokumentNummer, rpDrucken);
            break;
        case 35:
            rpVariablen.feststellungVereinZusammenstellung(lDokumentNummer, rpDrucken);
            break;
        case 38:
            rpVariablen.ekFreiwilligeAnmeldung(lDokumentNummer, rpDrucken);
            break;
        case 39:
            rpVariablen.formularErstzugang(lDokumentNummer, rpDrucken);
            break;
        case 40:
            rpVariablen.testDruckerzuordnung(lDokumentNummer, rpDrucken);
            break;
        case 42:
            rpVariablen.stimmkarten(lDokumentNummer, rpDrucken);
            break;

        case 44:
            rpVariablen.versammlungsbatch(lDokumentNummer, rpDrucken);
            break;
        }
        StringBuffer bufferFilename = rpVariablen.dateiname;

        // add here your preferred DokumentGenerator
    }

    /**
     * Rufe designer list.
     *
     * @param lfdFormular the lfd formular
     */
    void rufeDesignerList(int lfdFormular) {
        // add here your preferred DokumentGenerator
    }

}
