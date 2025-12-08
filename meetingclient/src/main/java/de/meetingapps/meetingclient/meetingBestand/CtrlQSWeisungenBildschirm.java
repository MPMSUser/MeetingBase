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

import java.net.URL;
import java.util.ResourceBundle;

import de.meetingapps.meetingclient.meetingClientDialoge.CtrlRootWeisungenNeu;
import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComBl.BlAbstimmungenWeisungen;
import de.meetingapps.meetingportal.meetComBl.BlNummernformen;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEclM.EclKIAVM;
import de.meetingapps.meetingportal.meetComEclM.EclTeilnehmerLoginM;
import de.meetingapps.meetingportal.meetComEclM.EclWillenserklaerungStatusM;
import de.meetingapps.meetingportal.meetComEclM.EclZugeordneteMeldungListeM;
import de.meetingapps.meetingportal.meetComEclM.EclZugeordneteMeldungM;
import de.meetingapps.meetingportal.meetComEntities.EclWeisungMeldung;
import de.meetingapps.meetingportal.meetComKonst.KonstStimmart;
import de.meetingapps.meetingportal.meetComKonst.KonstWillenserklaerung;
import de.meetingapps.meetingportal.meetComStub.WSClient;
import de.meetingapps.meetingportal.meetComWE.WELogin;
import de.meetingapps.meetingportal.meetComWE.WELoginCheck;
import de.meetingapps.meetingportal.meetComWE.WELoginCheckRC;
import de.meetingapps.meetingportal.meetComWE.WELoginVerify;
import de.meetingapps.meetingportal.meetComWE.WETeilnehmerStatusGetRC;
import de.meetingapps.meetingportal.meetComWE.WEWeisungAendernVorbereitenGet;
import de.meetingapps.meetingportal.meetComWE.WEWeisungAendernVorbereitenGetRC;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * The Class CtrlQSWeisungenBildschirm.
 */
public class CtrlQSWeisungenBildschirm extends CtrlRootWeisungenNeu {

    /** The resources. */
    @FXML
    private ResourceBundle resources;

    /** The location. */
    @FXML
    private URL location;

    /** The tf naechste nummer. */
    @FXML
    private TextField tfNaechsteNummer;

    /** The tf angezeigte nummer. */
    @FXML
    private TextField tfAngezeigteNummer;

    /** The tf name vorname. */
    @FXML
    private TextField tfNameVorname;

    /** The tf ort. */
    @FXML
    private TextField tfOrt;

    /** The tf aktien. */
    @FXML
    private TextField tfAktien;

    /** The tf sammelkarte. */
    @FXML
    private TextField tfSammelkarte;

    /** ********Ab hier individuell***********. */

    private WSClient wsClient = null;

    /** The ecl teilnehmer login M. */
    private EclTeilnehmerLoginM eclTeilnehmerLoginM = null;

    /** The we teilnehmer status get RC. */
    private WETeilnehmerStatusGetRC weTeilnehmerStatusGetRC;

    /** The ecl zugeordnete meldung liste M. */
    private EclZugeordneteMeldungListeM eclZugeordneteMeldungListeM = null;

    /*Für Änderungsmodus*/
    //	private int meldungsIdent=0;
    //	private int willenserklaerungIdent=0;
    //	private int sammelIdent=0;
    /** The kiav M. */
    //	private int weisungsIdent=0;
    private EclKIAVM kiavM = null;

    /**
     * Initialize.
     */
    @FXML
    void initialize() {

        assert tfNaechsteNummer != null
                : "fx:id=\"tfNaechsteNummer\" was not injected: check your FXML file 'QSBriefwahl.fxml'.";
        assert tfAngezeigteNummer != null
                : "fx:id=\"tfAngezeigteNummer\" was not injected: check your FXML file 'QSBriefwahl.fxml'.";

        /********** Ab hier individuell *******************/

        lDbBundle = new DbBundle();

        blAbstimmungenWeisungen = new BlAbstimmungenWeisungen(false, lDbBundle);
        blAbstimmungenWeisungen.leseAgendaFuerInterneWeisungenErfassung();
    }

    /**
     * On key naechste nummer.
     *
     * @param event the event
     */
    @FXML
    void onKeyNaechsteNummer(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            doEinlesen();
        }
    }

    /**
     * Enter in weisung gedrueckt.
     */
    protected void enterInWeisungGedrueckt() {
        System.out.println("Enter gedrückt");
    }

    /**
     * Do einlesen.
     */
    private void doEinlesen() {

        if (tfNaechsteNummer.getText() == null || tfNaechsteNummer.getText().isEmpty()) {
            fehlerMeldung("Bitte Aktionärsnummer eingeben!");
        }

        DbBundle lDbBundle = new DbBundle();
        BlNummernformen blNummernformen = new BlNummernformen(lDbBundle);
        String aktionaersNummerInArbeit = blNummernformen
                .aktienregisterNraufbereitenFuerIntern(tfNaechsteNummer.getText());
        tfNaechsteNummer.setText(aktionaersNummerInArbeit);

        aktionaersNummerInArbeit = tfNaechsteNummer.getText();
        String hAnzeigen = aktionaersNummerInArbeit;

        tfAngezeigteNummer.setText(hAnzeigen);

        neuEinlesenUndAnzeigen(aktionaersNummerInArbeit);

        tfNaechsteNummer.setText("");
        tfNaechsteNummer.requestFocus();

    }

    /**
     * Neu einlesen und anzeigen.
     *
     * @param aktionaersnummer the aktionaersnummer
     */
    private void neuEinlesenUndAnzeigen(String aktionaersnummer) {
        if (wsClient == null) {
            wsClient = new WSClient();
        }

        WELogin weLogin = new WELogin();

        /***********************
         * Aktionärsdaten grundsätzlich holen / überprüfen auf Existenz
         ************/
        /*Hinweise:
         * > mandant wird automatisch gesetzt aus ClGlobalVar.mandant - diese vorher belegen!
         * > user, uKennung, uPasswort werden automatisch gesetzt
         *
         */
        weLogin.setKennungArt(1); /* kennung enthält Aktionärsnummer*/

        weLogin.setKennung(aktionaersnummer);
        WELoginCheck weLoginCheck = new WELoginCheck();
        weLoginCheck.weLogin = weLogin;

        /*Die Rückgegebenen Aktionärsdaten speichern, da sie für weitere Aktionen
         * benötigt werden
         */
        WELoginCheckRC weLoginCheckRC = null;
        weLoginCheckRC = wsClient.loginCheck(weLoginCheck);
        eclTeilnehmerLoginM = weLoginCheckRC.eclTeilnehmerLoginM;

        int rc = weLoginCheckRC.getRc();
        if (rc < 1) { /*Fehlerbehandlung*/
            switch (rc) {
            case CaFehler.afKennungUnbekannt:
                fehlerMeldung("Aktionärsnummer gibt es nicht!");
                break;
            default:
                fehlerMeldung("Programmierer verständigen! Fehler " + rc + " " + CaFehler.getFehlertext(rc, 0));
                break;
            }
            return;
        }

        aktuelleGattung = eclTeilnehmerLoginM.getGattung();

        tfNameVorname.setText(eclTeilnehmerLoginM.getNameVornameTitel());
        tfOrt.setText(eclTeilnehmerLoginM.getOrt());
        tfAktien.setText(eclTeilnehmerLoginM.getStimmenDE());

        /**************************
         * Aktionärs-Status holen
         *****************************************/

        WELoginVerify weLoginVerify = wsClient.fuelleLoginVerify(eclTeilnehmerLoginM);

        weTeilnehmerStatusGetRC = wsClient.teilnehmerStatusGet(weLoginVerify);
        rc = weTeilnehmerStatusGetRC.getRc();

        eclZugeordneteMeldungListeM = weTeilnehmerStatusGetRC.getEclZugeordneteMeldungListeM();

        if (rc < 1) { /*Fehlerbehandlung*/
            switch (rc) {
            default:
                fehlerMeldung("Programmierer verständigen! Fehler " + rc + " " + CaFehler.getFehlertext(rc, 0));
                break;
            }
            return;
        }

        if (weTeilnehmerStatusGetRC.getAusgewaehlteHauptAktion().compareTo("2") != 0) { /*Noch nicht angemeldet*/
            fehlerMeldung("Noch nicht angemeldet!");
            return;
        }

        int anzahlMeldungen = eclZugeordneteMeldungListeM.getAnzZugeordneteMeldungenEigeneAktienListeM();

        EclZugeordneteMeldungM gefEclZugeordneteMeldungM = null;
        EclWillenserklaerungStatusM gefWillenserklaerung = null;

        for (int i = 0; i < anzahlMeldungen; i++) {
            EclZugeordneteMeldungM eclZugeordneteMeldungM = eclZugeordneteMeldungListeM
                    .getZugeordneteMeldungenEigeneAktienListeM().get(i);
            if (eclZugeordneteMeldungM.getKlasse() == 1) {

                if (eclZugeordneteMeldungM.getZugeordneteWillenserklaerungenListM() != null) {
                    for (int i1 = 0; i1 < eclZugeordneteMeldungM.getZugeordneteWillenserklaerungenListM()
                            .size(); i1++) {
                        EclWillenserklaerungStatusM willenserklaerung = eclZugeordneteMeldungM
                                .getZugeordneteWillenserklaerungenListM().get(i1);
                        if (willenserklaerung.isIstLeerDummy()) {
                        } else {
                            if (!willenserklaerung.isStorniert() && (willenserklaerung
                                    .getWillenserklaerung() == KonstWillenserklaerung.briefwahl
                                    || willenserklaerung
                                            .getWillenserklaerung() == KonstWillenserklaerung.aendernBriefwahl
                                    || willenserklaerung
                                            .getWillenserklaerung() == KonstWillenserklaerung.vollmachtUndWeisungAnSRV
                                    || willenserklaerung
                                            .getWillenserklaerung() == KonstWillenserklaerung.aendernWeisungAnSRV
                                    || willenserklaerung
                                            .getWillenserklaerung() == KonstWillenserklaerung.vollmachtUndWeisungAnKIAV
                                    || willenserklaerung
                                            .getWillenserklaerung() == KonstWillenserklaerung.aendernWeisungAnKIAV
                                    || willenserklaerung
                                            .getWillenserklaerung() == KonstWillenserklaerung.dauervollmachtAnKIAV
                                    || willenserklaerung
                                            .getWillenserklaerung() == KonstWillenserklaerung.aendernWeisungDauervollmachtAnKIAV
                                    || willenserklaerung
                                            .getWillenserklaerung() == KonstWillenserklaerung.organisatorischMitWeisungInSammelkarte
                                    || willenserklaerung
                                            .getWillenserklaerung() == KonstWillenserklaerung.aendernWeisungOrganisatorischInSammelkarte

                            )) {
                                gefEclZugeordneteMeldungM = eclZugeordneteMeldungM;
                                gefWillenserklaerung = willenserklaerung;
                            }
                        }
                    }
                }
            }

        }

        if (gefEclZugeordneteMeldungM == null) {
            fehlerMeldung("Keine Weisung vorhanden!");
            return;
        }

        /*Dann: Aufgerufen im Änderungsmodus. Bisherige Weisung aufrufen und anzeigen*/
        /*Stornieren vorbereiten holen*/

        weLoginVerify = wsClient.fuelleLoginVerify(eclTeilnehmerLoginM);

        weLoginVerify.setEingabeQuelle(1); /*Siehe EclWillenserklaerung - erteiltAufWeg*/
        weLoginVerify.setErteiltZeitpunkt("");
        WEWeisungAendernVorbereitenGet weWeisungAendernVorbereitenGet = new WEWeisungAendernVorbereitenGet();
        weWeisungAendernVorbereitenGet.setWeLoginVerify(weLoginVerify);
        weWeisungAendernVorbereitenGet.setAusgewaehlteHauptAktionString("2");
        weWeisungAendernVorbereitenGet.setAusgewaehlteAktionString(Integer.toString(11)); // ausgweählte Funktion

        weWeisungAendernVorbereitenGet.setZugeordneteMeldungM(gefEclZugeordneteMeldungM);
        weWeisungAendernVorbereitenGet.setEclWillenserklaerungStatusM(gefWillenserklaerung);
        WEWeisungAendernVorbereitenGetRC weWeisungAendernVorbereitenGetRC = wsClient
                .weisungAendernVorbereitenGet(weWeisungAendernVorbereitenGet);

        //		meldungsIdent=weWeisungAendernVorbereitenGetRC.getMeldungsIdent();
        //		willenserklaerungIdent=weWeisungAendernVorbereitenGetRC.getWillenserklaerungIdent();
        //		sammelIdent=weWeisungAendernVorbereitenGetRC.getSammelIdent();
        //		weisungsIdent=weWeisungAendernVorbereitenGetRC.getWeisungsIdent();
        kiavM = weWeisungAendernVorbereitenGetRC.getKiavM();
        aktuelleGattung = gefEclZugeordneteMeldungM.getGattung();

        blAbstimmungenWeisungen.rcWeisungMeldung = new EclWeisungMeldung[1];
        blAbstimmungenWeisungen.rcWeisungMeldung[0] = weWeisungAendernVorbereitenGetRC.getWeisungMeldung();

        tfSammelkarte.setText(kiavM.liefereSammelkartenBezeichnungKomplettIntern());

        ausgewaehlteFunktion = 0;
        hauptFunktion = 3;
        switch (gefWillenserklaerung.getWillenserklaerung()) {
        case KonstWillenserklaerung.briefwahl:
        case KonstWillenserklaerung.aendernBriefwahl:
            ausgewaehlteFunktion = 0;
            break;
        case KonstWillenserklaerung.vollmachtUndWeisungAnSRV:
        case KonstWillenserklaerung.aendernWeisungAnSRV:
            ausgewaehlteFunktion = 5;
            break;
        case KonstWillenserklaerung.vollmachtUndWeisungAnKIAV:
        case KonstWillenserklaerung.aendernWeisungAnKIAV:
        case KonstWillenserklaerung.dauervollmachtAnKIAV:
        case KonstWillenserklaerung.aendernWeisungDauervollmachtAnKIAV:
        case KonstWillenserklaerung.organisatorischMitWeisungInSammelkarte:
        case KonstWillenserklaerung.aendernWeisungOrganisatorischInSammelkarte:
            ausgewaehlteFunktion = 6;
            break;
        default:
            break;
        }

        zeigeAbstimmungFuerGattungAnNeu(KonstStimmart.nichtMarkiert, KonstStimmart.nichtMarkiert, true);
    }

    /**
     * Inits the.
     */
    public void init() {
    }

    /**
     * Zeige gesamt buttons.
     */
    @Override
    protected void zeigeGesamtButtons() {
    }

    /**
     * Verberge gesamt buttons.
     */
    @Override
    protected void verbergeGesamtButtons() {
    }

}
