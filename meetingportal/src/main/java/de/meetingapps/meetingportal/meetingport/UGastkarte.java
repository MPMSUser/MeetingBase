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
package de.meetingapps.meetingportal.meetingport;

import java.util.LinkedList;
import java.util.List;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComAllg.CaPasswortVerschluesseln;
import de.meetingapps.meetingportal.meetComAllg.CaString;
import de.meetingapps.meetingportal.meetComAllg.CaVariablenInText;
import de.meetingapps.meetingportal.meetComBa.BaMailM;
import de.meetingapps.meetingportal.meetComBl.BlEinsprungLinkPortal;
import de.meetingapps.meetingportal.meetComBl.BlGastkarte;
import de.meetingapps.meetingportal.meetComBl.BlZutrittsIdent;
import de.meetingapps.meetingportal.meetComEclM.EclAnredeListeM;
import de.meetingapps.meetingportal.meetComEclM.EclAusstellungsgrundListeM;
import de.meetingapps.meetingportal.meetComEclM.EclDbM;
import de.meetingapps.meetingportal.meetComEclM.EclGastListeM;
import de.meetingapps.meetingportal.meetComEclM.EclGastM;
import de.meetingapps.meetingportal.meetComEclM.EclGruppeListeM;
import de.meetingapps.meetingportal.meetComEclM.EclGruppeM;
import de.meetingapps.meetingportal.meetComEclM.EclKommunikationsSpracheListeM;
import de.meetingapps.meetingportal.meetComEclM.EclMeldungAusstellungsgrundListeM;
import de.meetingapps.meetingportal.meetComEclM.EclMeldungAusstellungsgrundM;
import de.meetingapps.meetingportal.meetComEclM.EclMeldungVipKZListeM;
import de.meetingapps.meetingportal.meetComEclM.EclMeldungVipKZM;
import de.meetingapps.meetingportal.meetComEclM.EclMeldungenMeldungenListeM;
import de.meetingapps.meetingportal.meetComEclM.EclParamM;
import de.meetingapps.meetingportal.meetComEclM.EclPortalTexteM;
import de.meetingapps.meetingportal.meetComEclM.EclUserLoginM;
import de.meetingapps.meetingportal.meetComEclM.EclVipKZListeM;
import de.meetingapps.meetingportal.meetComEclM.EclZutrittsIdentAnzeigeM;
import de.meetingapps.meetingportal.meetComEntities.EclDateiDownload;
import de.meetingapps.meetingportal.meetComEntities.EclLoginDaten;
import de.meetingapps.meetingportal.meetComEntities.EclMeldung;
import de.meetingapps.meetingportal.meetComEntities.EclMeldungAusstellungsgrund;
import de.meetingapps.meetingportal.meetComEntities.EclMeldungVipKZ;
import de.meetingapps.meetingportal.meetComEntities.EclMeldungenMeldungen;
import de.meetingapps.meetingportal.meetComEntities.EclZutrittsIdent;
import de.meetingapps.meetingportal.meetComEntities.EclZutrittskarten;
import de.meetingapps.meetingportal.meetComKonst.KonstPortalFunktionen;
import de.meetingapps.meetingportal.meetComKonst.KonstPortalTexte;
import de.meetingapps.meetingportal.meetComKonst.KonstStimmkarteIstGesperrt;
import de.meetingapps.meetingportal.meetingCoreReport.RpBrowserAnzeigen;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@RequestScoped
@Named
public class UGastkarte {

    private int logDrucken = 3;

    private @Inject EclAnredeListeM eclAnredeListeM;
    private @Inject EclKommunikationsSpracheListeM eclKommunikationsSpracheListeM;
    private @Inject EclGruppeListeM eclGruppeListeM;
    private @Inject EclAusstellungsgrundListeM eclAusstellungsgrundListeM;
    private @Inject EclVipKZListeM eclVipKZListeM;
    private @Inject EclMeldungenMeldungenListeM eclMeldungenMeldungenListeM;
    private @Inject EclGastM eclGastM;
    private @Inject EclKommunikationsSpracheListeM eclKommunikationsSpracheM;

    private @Inject USession uSession;
    private @Inject UGastkarteSession uGastkarteSession;

    private @Inject XSessionVerwaltung xSessionVerwaltung;

    private @Inject EclUserLoginM eclUserLoginM;
    private @Inject EclDbM eclDbM;

    private @Inject BaMailM baMailm;

    private @Inject EclMeldungVipKZListeM eclMeldungVipKZListeM;
    private @Inject EclMeldungAusstellungsgrundListeM eclMeldungAusstellungsgrundListeM;
    private @Inject EclGastListeM eclGastListeM;

    private @Inject EclParamM eclParamM;
    private @Inject EclPortalTexteM eclPortalTexteM;

    private @Inject UServicelineAnfrage uServicelineAnfrage;
    /**************************Initialisierung************************************/
    public void initOhneGruppe() {
        uGastkarteSession.setGruppenausstellung(false);
        init();

    }

    public void initMitGruppe() {
        uGastkarteSession.setGruppenausstellung(true);
        init();

    }

    public void initSuchen() {
        init();
        uGastkarteSession.initSuchen();
    }

    public void initBlankoGastkarten() {
        uGastkarteSession.setAnzahlKarten("");
    }

    public void initInstiGastkarten() {
        uGastkarteSession.setInstiAblauf(0);
        uGastkarteSession.setInstiPDFs(null);
    }

    public void initInterneGastkarten() {
        uGastkarteSession.setRzKartenErstellen(true);
        uGastkarteSession.setAnzPortalTeamKartenErstellen("2");
        uGastkarteSession.setAnzConsultantKartenErstellen("2");
        uGastkarteSession.setAnzahlKundenKartenErstellen("2");
        uGastkarteSession.setAnzahlNotarKartenErstellen("2");
    }

    private void init() {
        eclDbM.openAll();
        eclAnredeListeM.fuelleListe(eclDbM.getDbBundle());
        eclKommunikationsSpracheListeM.fuelleListe(eclDbM.getDbBundle());
        eclGruppeListeM.fuelleListe(eclDbM.getDbBundle());
        eclAusstellungsgrundListeM.fuelleListe(eclDbM.getDbBundle());
        eclVipKZListeM.fuelleListe(eclDbM.getDbBundle());
        eclGastM.init();
        uGastkarteSession.init(eclDbM.getDbBundle());
        eclDbM.closeAll();

    }

    /********************************************************************************************/
    /***************************Gastkarte Neu****************************************************/
    /********************************************************************************************/
    public String doWeitererAusstellungsgrund() {
        if (!xSessionVerwaltung.pruefeUStart("uGastkarteNeu", "")) {
            xSessionVerwaltung.setzeEnde();
            return "uDlgFehler";
        }
        weitererAusstellungsgrund();
        xSessionVerwaltung.setzeUEnde();
        return "";
    }

    public boolean weitererAusstellungsgrund() {
        uSession.clearFehlermeldung();
        if (eclGastM.getNeuerAusstellungsgrund() == null || eclGastM.getNeuerAusstellungsgrund().isEmpty() || eclGastM.getNeuerAusstellungsgrund().compareTo("_0") == 0) {
            if (eclGastM.getNeuerAusstellungsgrundKommentar() != null && !eclGastM.getNeuerAusstellungsgrundKommentar().isEmpty()) {
                uSession.setFehlermeldung("Kommentar ohne Kürzel bei Ausstellungsgrund unzulässig!");
                return false;
            }
        } else {
            EclMeldungAusstellungsgrundM meldungAusstellungsgrund = new EclMeldungAusstellungsgrundM();
            meldungAusstellungsgrund.setAusstellungsGrundKuerzel(eclGastM.getNeuerAusstellungsgrund());
            meldungAusstellungsgrund.setKommentar(eclGastM.getNeuerAusstellungsgrundKommentar());
            for (int i = 0; i < eclAusstellungsgrundListeM.getAusstellungsgrundListeM().size(); i++) {
                if (eclAusstellungsgrundListeM.getAusstellungsgrundListeM().get(i).getKuerzel().compareTo(eclGastM.getNeuerAusstellungsgrund()) == 0) {
                    meldungAusstellungsgrund.setBeschreibung(eclAusstellungsgrundListeM.getAusstellungsgrundListeM().get(i).getBeschreibung());
                }
            }
            eclGastM.getAusstellungsgrundListe().add(meldungAusstellungsgrund);
            eclGastM.setNeuerAusstellungsgrund("_0");
            eclGastM.setNeuerAusstellungsgrundKommentar("");
        }
        return true;
    }

    public String doWeiteresVipKZ() {
        if (!xSessionVerwaltung.pruefeUStart("uGastkarteNeu", "")) {
            xSessionVerwaltung.setzeEnde();
            return "uDlgFehler";
        }
        weiteresVipKZ();
        xSessionVerwaltung.setzeUEnde();
        return "";
    }

    boolean weiteresVipKZ() {
        uSession.clearFehlermeldung();
        if (eclGastM.getNeuesVipKZ() == null || eclGastM.getNeuesVipKZ().isEmpty() || eclGastM.getNeuesVipKZ().compareTo("_0") == 0) {
            if (eclGastM.getNeuesVipKZKommentar() != null && !eclGastM.getNeuesVipKZKommentar().isEmpty()) {
                uSession.setFehlermeldung("Kommentar ohne Kürzel bei VipKZ unzulässig!");
                return false;
            }
        } else {
            EclMeldungVipKZM meldungVipKZ = new EclMeldungVipKZM();
            meldungVipKZ.setVipKZKuerzel(eclGastM.getNeuesVipKZ());
            meldungVipKZ.setParameter(eclGastM.getNeuesVipKZKommentar());
            int i;
            for (i = 0; i < eclVipKZListeM.getVipKZListeM().size(); i++) {
                if (eclVipKZListeM.getVipKZListeM().get(i).getKuerzel().compareTo(eclGastM.getNeuesVipKZ()) == 0) {
                    meldungVipKZ.setBeschreibung(eclVipKZListeM.getVipKZListeM().get(i).getBeschreibung());
                }
            }
            eclGastM.getVipKZListe().add(meldungVipKZ);
            eclGastM.setNeuesVipKZ("_0");
            eclGastM.setNeuesVipKZKommentar("");
        }
        return true;
    }

    public String doSpeichernSofort() {
        if (!xSessionVerwaltung.pruefeUStart("uGastkarteNeu", "")) {
            xSessionVerwaltung.setzeEnde();
            return "uDlgFehler";
        }
        if (speichernGastkarte(true) == false) { /*Fehler*/
            xSessionVerwaltung.setzeUEnde();
            return "";
        }

        aufbereitenManuelleMail();

        uGastkarteSession.setAenderung(false);
        return xSessionVerwaltung.setzeUEnde("uGastkarteNeuQuittung", false, false, eclUserLoginM.getKennung());
    }

    public String doSpeichernVormerken() {
        if (!xSessionVerwaltung.pruefeUStart("uGastkarteNeu", "")) {
            xSessionVerwaltung.setzeEnde();
            return "uDlgFehler";
        }
        if (speichernGastkarte(false) == false) { /*Fehler*/
            xSessionVerwaltung.setzeEnde();
            return "";
        }

        aufbereitenManuelleMail();
        uGastkarteSession.setAenderung(false);
        return xSessionVerwaltung.setzeUEnde("uGastkarteNeuQuittung", false, false, eclUserLoginM.getKennung());
    }

    public String doZumServiceLineTool() {
        if (!xSessionVerwaltung.pruefeStart("uGastkarteNeu", "")) {
            xSessionVerwaltung.setzeEnde();
            return "uDlgFehler";
        }
        eclDbM.openAll();
        uServicelineAnfrage.init();
        eclDbM.closeAll();
        return xSessionVerwaltung.setzeUEnde("uServicelineAnfrage", true, false, eclUserLoginM.getKennung());
    }
    

    /**True => Alle Zwangsfelder ausgefüllt
     * False => ein Feld ist nicht ausgefüllt, Feldname steht in "auszufuellendesFeld"
     * @return
     */
    private String auszufuellendesFeld = "";

    public boolean pruefenFelderAusgefuellt() {
        auszufuellendesFeld = "";
        if (uGastkarteSession.getFeldAnredeVerwenden() == 2 && (eclGastM.getAnrede().isEmpty() || eclGastM.getAnrede().compareTo("0") == 0)) {
            auszufuellendesFeld = "Anrede";
            return false;
        }
        if (uGastkarteSession.getFeldTitelVerwenden() == 2 && eclGastM.getTitel().isEmpty()) {
            auszufuellendesFeld = "Titel";
            return false;
        }
        if (uGastkarteSession.getFeldAdelstitelVerwenden() == 2 && eclGastM.getAdelstitel().isEmpty()) {
            auszufuellendesFeld = "Adelstitel";
            return false;
        }
        if (uGastkarteSession.getFeldNameVerwenden() == 2 && eclGastM.getName().isEmpty()) {
            auszufuellendesFeld = "Name";
            return false;
        }
        if (uGastkarteSession.getFeldVornameVerwenden() == 2 && eclGastM.getVorname().isEmpty()) {
            auszufuellendesFeld = "Vorname";
            return false;
        }
        if (uGastkarteSession.getFeldZuHaendenVerwenden() == 2 && eclGastM.getZuHdCo().isEmpty()) {
            auszufuellendesFeld = uGastkarteSession.getFeldZuHaendenBezeichnung();
            return false;
        }
        if (uGastkarteSession.getFeldZusatz1Verwenden() == 2 && eclGastM.getZusatz1().isEmpty()) {
            auszufuellendesFeld = uGastkarteSession.getFeldZusatz1Bezeichnung();
            return false;
        }
        if (uGastkarteSession.getFeldZusatz2Verwenden() == 2 && eclGastM.getZusatz2().isEmpty()) {
            auszufuellendesFeld = uGastkarteSession.getFeldZusatz2Bezeichnung();
            return false;
        }
        if (uGastkarteSession.getFeldStrasseVerwenden() == 2 && eclGastM.getStrasse().isEmpty()) {
            auszufuellendesFeld = "Strasse";
            return false;
        }
        if (uGastkarteSession.getFeldLandVerwenden() == 2 && eclGastM.getLand().isEmpty()) {
            auszufuellendesFeld = "Land";
            return false;
        }
        if (uGastkarteSession.getFeldPLZVerwenden() == 2 && eclGastM.getPlz().isEmpty()) {
            auszufuellendesFeld = "PLZ";
            return false;
        }
        if (uGastkarteSession.getFeldOrtVerwenden() == 2 && eclGastM.getOrt().isEmpty()) {
            auszufuellendesFeld = "Ort";
            return false;
        }
        if (uGastkarteSession.getFeldMailadresseVerwenden() == 2 && eclGastM.getMailadresse().isEmpty()) {
            auszufuellendesFeld = "Mailadresse";
            return false;
        }
        if (uGastkarteSession.getFeldKommunikationsspracheVerwenden() == 2 && eclGastM.getKommunikationssprache().isEmpty()) {
            auszufuellendesFeld = "Kommunikationssprache";
            return false;
        }
        if (uGastkarteSession.getFeldGruppeVerwenden() == 2 && eclGastM.getGruppe().isEmpty()) {
            auszufuellendesFeld = "Gruppe";
            return false;
        }
        if (uGastkarteSession.getFeldAusstellungsgrundVerwenden() == 2) {
            if (eclGastM.getAusstellungsgrundListe() == null || eclGastM.getAusstellungsgrundListe().size() == 0) {
                auszufuellendesFeld = "Ausstellungsgrund";
                return false;
            }
        }
        if (uGastkarteSession.getFeldVipVerwenden() == 2) {
            if (eclGastM.getVipKZListe() == null || eclGastM.getVipKZListe().size() == 0) {
                auszufuellendesFeld = "VIP";
                return false;
            }
        }

        return true;
    }

    public void speichernAusstellungsgrund(int pLfdNr, int pMeldungsIdent) {
        List<EclMeldungAusstellungsgrundM> ausstellungsgrundListe = eclGastM.getAusstellungsgrundListe();
        EclMeldungAusstellungsgrund meldungAusstellungsgrund = new EclMeldungAusstellungsgrund();
        meldungAusstellungsgrund.meldungsIdent = pMeldungsIdent;
        meldungAusstellungsgrund.ausstellungsGrundKuerzel = ausstellungsgrundListe.get(pLfdNr).getAusstellungsGrundKuerzel();
        meldungAusstellungsgrund.kommentar = ausstellungsgrundListe.get(pLfdNr).getKommentar();
        eclDbM.getDbBundle().dbMeldungAusstellungsgrund.insert(meldungAusstellungsgrund);
    }

    public void speichernVipKZKuerzel(int pLfdNr, int pMeldungsIdent) {
        List<EclMeldungVipKZM> vipKZListe = eclGastM.getVipKZListe();
        EclMeldungVipKZ meldungVipKZ = new EclMeldungVipKZ();
        meldungVipKZ.meldungsIdent = pMeldungsIdent;
        meldungVipKZ.vipKZKuerzel = vipKZListe.get(pLfdNr).getVipKZKuerzel();
        meldungVipKZ.parameter = vipKZListe.get(pLfdNr).getParameter();
        eclDbM.getDbBundle().dbMeldungVipKZ.insert(meldungVipKZ);
    }

    private boolean speichernGastkarte(boolean pSofortdruck) {
        int erg;
        int i, i1;
        int anzahlKartenGruppe = 0;

        /*Plausibilitätsprüfung*/
        uSession.clearFehlermeldung();

        /*Ggf. Ausstellungsgrund und VipKZ in Liste übertragen*/
        if (weitererAusstellungsgrund() == false) {
            return false;
        }
        if (weiteresVipKZ() == false) {
            return false;
        }

        if (pruefenFelderAusgefuellt() == false) {
            uSession.setFehlermeldung("Feld " + auszufuellendesFeld + " ist zwingend auszufüllen");
            return false;
        }

        /*Plausibilitätsprüfung für Gruppenkarte*/
        if (uGastkarteSession.isGruppenausstellung()) {
            if (!CaString.isNummern(eclGastM.getAnzahlZugeordneteEK())) {
                uSession.setFehlermeldung("Bitte im Feld Anzahl der zugeordneten Gastkarten eine gültige Anzahl (nur Ziffern) eingeben!");
                return false;
            }
            anzahlKartenGruppe = Integer.parseInt(eclGastM.getAnzahlZugeordneteEK());
            if (anzahlKartenGruppe < 1) {
                uSession.setFehlermeldung("Bitte im Feld Anzahl der zugeordneten Gastkarten eine gültige Anzahl >0 eingeben!");
                return false;
            }

            if (!CaString.isMailadresse(eclGastM.getMailadresse())) {
                uSession.setFehlermeldung("Gruppenticket ist nur per Mailversand möglich. Bitte geben Sie eine gültige Mailadresse ein!");
                return false;
            }

        }

        /*Abspeichern*/
        eclDbM.openAll();

        BlGastkarte blGastkarte = new BlGastkarte(eclDbM.getDbBundle());
        EclMeldung lGastMeldung = new EclMeldung();

        /*Anmelden*/
        /*Meldung Gast füllen*/
        eclGastM.copyTo(lGastMeldung);
        blGastkarte.berechtigungsWert = eclGastM.liefereBerechtigungsWert();

        /*Versandart*/
        blGastkarte.pVersandart = 2;
        uGastkarteSession.setAusstellungsart("3");
        /*Gruppd ermitteln, daraus EKFormular*/
        if (lGastMeldung.gruppe != 0) {
            for (int i2 = 0; i2 < eclGruppeListeM.getGruppeListeM().size(); i2++) {
                EclGruppeM lGruppeM = eclGruppeListeM.getGruppeListeM().get(i2);
                if (lGruppeM.getGastEKFormular() != 0) {
                    blGastkarte.pGastEKFormular = lGruppeM.getGastEKFormular();
                }
            }
        }

        if (pSofortdruck == true) {
            blGastkarte.pVersandart = 5;
            uGastkarteSession.setAusstellungsart("2");
            if (!eclGastM.getMailadresse().isEmpty()) {
                blGastkarte.pVersandart = 4;
                uGastkarteSession.setAusstellungsart("1");
            }
        } else {//Stapeldruck
            blGastkarte.pVersandart = 2;
            uGastkarteSession.setAusstellungsart("3");
        }

        blGastkarte.pGast = lGastMeldung;
        blGastkarte.pWillenserklaerungGeberIdent = 0; /*undefiniert*/

        aufbereitenFuerQuittung();
        blGastkarte.pAnredeKomplett = eclGastM.getAnredeKomplett();
        blGastkarte.pAnrede = eclGastM.getAnredeText();

        erg = blGastkarte.ausstellen();

        if (erg < 0) {
            uSession.setFehlermeldung(CaFehler.getFehlertext(erg, 0));
            eclDbM.closeAllAbbruch();
            return false;
        }

        /*Gastkarten-PDF erzeugen*/
        if (pSofortdruck == true) {
            uGastkarteSession.setGastkartePdfNr(Integer.toString(blGastkarte.rcGastkartePdfNr));

            if (!eclGastM.getMailadresse().isEmpty()) {
                if ((eclDbM.getDbBundle().param.paramGaesteModul.mailVerschickenGK & 1) == 1) {
                    /*Nun per Mail versenden mit Gastkarte als Anhang*/
                    String[] variablenArray = { "L_ANREDE" };
                    String[] inhalteArray = { "" };
                    inhalteArray[0] = eclGastM.getAnredeKomplett();
                    eclPortalTexteM.setLokaleVariablenNamen(variablenArray);
                    eclPortalTexteM.setLokaleVariablenInhalt(inhalteArray);

                    String betreffText = eclPortalTexteM.holeIText(KonstPortalTexte.GAESTE_MAIL_MIT_GK_BETREFF);
                    betreffText = CaVariablenInText.aufbereiteFuerNormalesMail(betreffText);

                    String inhaltText = eclPortalTexteM.holeIText(KonstPortalTexte.GAESTE_MAIL_MIT_GK_INHALT);
                    inhaltText = CaVariablenInText.aufbereiteFuerNormalesMail(inhaltText);

                    baMailm.sendenMitAnhang(eclGastM.getMailadresse(), eclDbM.getDbBundle().param.paramGaesteModul.bccAdresse1, eclDbM.getDbBundle().param.paramGaesteModul.bccAdresse2, betreffText,
                            inhaltText, eclDbM.getDbBundle().lieferePfadMeetingAusdrucke() + "\\zutrittsdokumentM"
                                    + eclDbM.getDbBundle().getMandantString() + Integer.toString(blGastkarte.rcGastkartePdfNr) + ".pdf");

                    eclPortalTexteM.setLokaleVariablenNamen(null);
                    eclPortalTexteM.setLokaleVariablenInhalt(null);

                    //					baMailm.sendenMitAnhang(eclGastM.getMailadresse(), eclDbM.getDbBundle().param.paramGaesteModul.bccAdresse1, eclDbM.getDbBundle().param.paramGaesteModul.bccAdresse2,  
                    //							betreffText, inhaltText, "D:\\meetingausdrucke\\zutrittsdokumentM"+eclDbM.getDbBundle().getMandantString()+Integer.toString(blGastkarte.rcGastkartePdfNr)+".pdf");
                }
                if ((eclDbM.getDbBundle().param.paramGaesteModul.mailVerschickenGK & 2) == 2) {
                    /*Nun per Mail versenden - nur Bestätigung*/
                    String[] variablenArray = { "L_ANREDE" };
                    String[] inhalteArray = { "" };
                    inhalteArray[0] = eclGastM.getAnredeKomplett();
                    eclPortalTexteM.setLokaleVariablenNamen(variablenArray);
                    eclPortalTexteM.setLokaleVariablenInhalt(inhalteArray);

                    String betreffText = eclPortalTexteM.holeIText(KonstPortalTexte.GAESTE_MAIL_NUR_BESTAETIGUNG_BETREFF);
                    betreffText = CaVariablenInText.aufbereiteFuerNormalesMail(betreffText);

                    String inhaltText = eclPortalTexteM.holeIText(KonstPortalTexte.GAESTE_MAIL_NUR_BESTAETIGUNG_INHALT);
                    inhaltText = CaVariablenInText.aufbereiteFuerNormalesMail(inhaltText);

                    baMailm.sendenMitAnhang(eclGastM.getMailadresse(), eclDbM.getDbBundle().param.paramGaesteModul.bccAdresse1, eclDbM.getDbBundle().param.paramGaesteModul.bccAdresse2, betreffText,
                            inhaltText, "");
                }
            }
        }

        /*MeldungAusstellungsgrund speichern*/
        List<EclMeldungAusstellungsgrundM> ausstellungsgrundListe = eclGastM.getAusstellungsgrundListe();
        for (i = 0; i < ausstellungsgrundListe.size(); i++) {
            speichernAusstellungsgrund(i, blGastkarte.pGast.meldungsIdent);
        }

        /*MeldungVipKZ speichern*/
        List<EclMeldungVipKZM> vipKZListe = eclGastM.getVipKZListe();
        for (i = 0; i < vipKZListe.size(); i++) {
            speichernVipKZKuerzel(i, blGastkarte.pGast.meldungsIdent);
        }

        eclGastM.setNummer(blGastkarte.rcZutrittsIdent);

        /*********************Falls Gruppenkarte: zugeordnete Karten erstellen*****************************/
        if (uGastkarteSession.isGruppenausstellung()) {
            eclMeldungenMeldungenListeM.init();
            EclMeldung lGastZugeordneteMeldung = null;
            BlGastkarte blGastZugeordneteKarte = null;
            for (i1 = 0; i1 < Integer.parseInt(eclGastM.getAnzahlZugeordneteEK()); i1++) {
                blGastZugeordneteKarte = new BlGastkarte(eclDbM.getDbBundle());
                lGastZugeordneteMeldung = new EclMeldung();
                lGastZugeordneteMeldung.kommunikationssprache = Integer.parseInt(eclGastM.getKommunikationssprache());
                lGastZugeordneteMeldung.gruppe = Integer.parseInt(eclGastM.getGruppe());
                blGastZugeordneteKarte.pVersandart = 4;

                blGastZugeordneteKarte.pGast = lGastZugeordneteMeldung;
                blGastZugeordneteKarte.pWillenserklaerungGeberIdent = 0; /*undefiniert*/
                System.out.println("EControllerNeueGastkarte Zugeordnete i1=" + i1);

                erg = blGastZugeordneteKarte.ausstellen();

                if (erg < 0) {
                    uSession.setFehlermeldung(CaFehler.getFehlertext(erg, 0));
                    eclDbM.closeAllAbbruch();
                    return false;
                }

                /*Zuordnung speichern*/
                EclMeldungenMeldungen lMeldungenMeldungen = new EclMeldungenMeldungen();
                lMeldungenMeldungen.verwendung = 1;
                lMeldungenMeldungen.vonMeldungsIdent = blGastZugeordneteKarte.pGast.meldungsIdent;
                lMeldungenMeldungen.zuMeldungsIdent = blGastkarte.pGast.meldungsIdent;
                lMeldungenMeldungen.zutrittsIdent = blGastZugeordneteKarte.rcZutrittsIdent;
                eclDbM.getDbBundle().dbMeldungenMeldungen.insert(lMeldungenMeldungen);
                eclMeldungenMeldungenListeM.add(lMeldungenMeldungen);

            }

            eclGastM.setMeldungenMeldungenListe(eclMeldungenMeldungenListeM.getMeldungenMeldungenListeM());

            baMailm.senden(eclGastM.getMailadresse(), "Ihre Zugangsdaten",
                    "Anbei erhalten Sie Ihre Zugangsdaten für die Registrierung der Ihnen zugeordneten " + eclGastM.getAnzahlZugeordneteEK() + " Gastkarten. "
                            + "Bitte loggen Sie sich unter http://127.0.0.1:8080/meetingportal/M054/aLogin.xhtml?mandant=054&sprache=DE mit der " + "Kennung " + lGastMeldung.loginKennung
                            + " und dem Passwort " + blGastkarte.rcPasswort + " ein und pflegen dort die Daten der Besucher für die " + "Eintrittskarten ein.");

        }

        /*******Nun noch aufbereiten für Anzeige********/
        /*Hinweis auf Papierformat*/
        uGastkarteSession.setHinweisPapierformat(blGastkarte.rcHinweisPapierformat);

        eclDbM.closeAll();
        return true;
    }

    private void aufbereitenManuelleMail() {
        if ((eclDbM.getDbBundle().param.paramGaesteModul.mailVerschickenGK & 4) != 4 || eclGastM.getMailadresse().isEmpty()) {
            uGastkarteSession.setMailtext("");
            uGastkarteSession.setManuellerMailversand(false);
            return;
        }

        boolean undVorHaengen = false;

        String mailKomplett = "" + "<a href=\"mailto:" + eclGastM.getMailadresse() + "?";

        String adresseBCC1 = eclDbM.getDbBundle().param.paramGaesteModul.bccAdresse1;
        if (!adresseBCC1.isEmpty()) {
            mailKomplett = mailKomplett + "bcc=" + adresseBCC1;
            undVorHaengen = true;
        }
        String adresseBCC2 = eclDbM.getDbBundle().param.paramGaesteModul.bccAdresse2;
        if (!adresseBCC2.isEmpty()) {
            mailKomplett = mailKomplett + "; " + adresseBCC2;
            undVorHaengen = true;
        }

        String[] variablenArray = { "L_ANREDE" };
        String[] inhalteArray = { "" };
        inhalteArray[0] = eclGastM.getAnredeKomplett();
        eclPortalTexteM.setLokaleVariablenNamen(variablenArray);
        eclPortalTexteM.setLokaleVariablenInhalt(inhalteArray);

        String betreffText = eclPortalTexteM.holeText("1479");
        betreffText = CaVariablenInText.aufbereiteFuerHTML(betreffText);
        if (!betreffText.isEmpty()) {
            if (undVorHaengen) {
                mailKomplett = mailKomplett + "&amp;";
            }
            mailKomplett = mailKomplett + "subject=" + betreffText;
            undVorHaengen = true;
        }

        String inhaltText = eclPortalTexteM.holeText("1480");
        inhaltText = CaVariablenInText.aufbereiteFuerHTML(inhaltText);
        if (!inhaltText.isEmpty()) {
            if (undVorHaengen) {
                mailKomplett = mailKomplett + "&amp;";
            }
            mailKomplett = mailKomplett + "body=" + inhaltText;
            undVorHaengen = true;
        }

        mailKomplett = mailKomplett + "\">E-Mail manuell verschicken</a>";

        uGastkarteSession.setMailtext(mailKomplett);
        uGastkarteSession.setManuellerMailversand(true);

     }

    public void aufbereitenFuerQuittung() {
        /*Anrede*/
        eclGastM.setAnredeText("");
        eclGastM.setAnredeKomplett("");
        if (eclGastM.getAnrede().compareTo("0") != 0) {
            for (int i = 0; i < eclAnredeListeM.getAnredeListeM().size(); i++) {
                if (eclAnredeListeM.getAnredeListeM().get(i).getAnredennr().equals(eclGastM.getAnrede())) {
                    eclGastM.setAnredeText(eclAnredeListeM.getAnredeListeM().get(i).getAnredentext());
                    eclGastM.setAnredeKomplett(eclAnredeListeM.getAnredeListeM().get(i).getAnredenbrief() + " " + eclGastM.getName());
                }
            }
        }

        /*Kommunikationssprache*/
        eclGastM.setKommunikationsspracheText("");
        if (eclGastM.getKommunikationssprache().compareTo("0") != 0) {
            for (int i = 0; i < eclKommunikationsSpracheListeM.getKommunikationsSpracheListeM().size(); i++) {
                if (eclKommunikationsSpracheListeM.getKommunikationsSpracheListeM().get(i).getSprachennr().equals(eclGastM.getKommunikationssprache())) {
                    eclGastM.setKommunikationsspracheText(eclKommunikationsSpracheListeM.getKommunikationsSpracheListeM().get(i).getSprache());
                }
            }
        }

        /*Gruppe*/
        eclGastM.setGruppeText("");
        if (eclGastM.getGruppe().compareTo("0") != 0) {
            for (int i = 0; i < eclGruppeListeM.getGruppeListeM().size(); i++) {
                if (eclGruppeListeM.getGruppeListeM().get(i).getGruppenNr().equals(eclGastM.getGruppe())) {
                    eclGastM.setGruppeText(eclGruppeListeM.getGruppeListeM().get(i).getGruppenText());
                }
            }
        }

    }

    public String doAbbrechen() {
        if (!xSessionVerwaltung.pruefeUStart("uGastkarteNeu", "")) {
            xSessionVerwaltung.setzeEnde();
            return "uDlgFehler";
        }
        return xSessionVerwaltung.setzeUEnde("uMenue", false, false, eclUserLoginM.getKennung());
    }

    public String getAuszufuellendesFeld() {
        return auszufuellendesFeld;
    }

    public void setAuszufuellendesFeld(String auszufuellendesFeld) {
        this.auszufuellendesFeld = auszufuellendesFeld;
    }

    /********************************************************************************************/
    /***********************Gästekarte Neu Quittung***************************************/
    /********************************************************************************************/

    public String doDrucken() {
        if (!xSessionVerwaltung.pruefeStart("uGastkarteNeuQuittung", "")) {
            xSessionVerwaltung.setzeEnde();
            return "uDlgFehler";
        }

        String dateinr = uGastkarteSession.getGastkartePdfNr();

        eclDbM.openAll();
        String hDatei = eclDbM.getDbBundle().lieferePfadMeetingAusdrucke() + "\\zutrittsdokumentM" + eclDbM.getDbBundle().getMandantString()
                + dateinr + ".pdf";
        eclDbM.closeAll();
        RpBrowserAnzeigen rpBrowserAnzeigen = new RpBrowserAnzeigen();
        rpBrowserAnzeigen.zeigen(hDatei);

        xSessionVerwaltung.setzeEnde();
        return "";

    }

    public String doNaechsteGastkarte() {
        if (!xSessionVerwaltung.pruefeStart("uGastkarteNeuQuittung", "")) {
            xSessionVerwaltung.setzeEnde();
            return "uDlgFehler";
        }
        eclGastM.init();
        return xSessionVerwaltung.setzeEnde("uGastkarteNeu", false, false, "");
    }

    public String doZumServiceLineToolQuittung() {
        if (!xSessionVerwaltung.pruefeStart("uGastkarteNeuQuittung", "")) {
            xSessionVerwaltung.setzeEnde();
            return "uDlgFehler";
        }
        eclDbM.openAll();
        uServicelineAnfrage.init();
        eclDbM.closeAll();
        return xSessionVerwaltung.setzeUEnde("uServicelineAnfrage", true, false, eclUserLoginM.getKennung());
    }

    /********************************************************************************************/
    /***********************Gästekarte Suchen (und ändern)***************************************/
    /********************************************************************************************/
    private String lSuchstring = "";

    /*Suchen nach Gast-Nummer*/
    public String doSuchenNr() {
        if (!xSessionVerwaltung.pruefeUStart("uGastkarteSuchen", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }

        int erg;
        uSession.clearFehlermeldung();
        uGastkarteSession.setAnzeigeSeite(0);
        String suchstring = uGastkarteSession.getSucheGastNr().trim();
        if (suchstring.isEmpty()) {
            uSession.setFehlermeldung("Bitte geben Sie eine zulässige Gastkartennummer ein!");
            xSessionVerwaltung.setzeUEnde();
            return "";
        }

        eclDbM.openAll();

        EclZutrittsIdent lEclZutrittsIdent = new EclZutrittsIdent();
        lEclZutrittsIdent.zutrittsIdent = suchstring;
        lEclZutrittsIdent.zutrittsIdentNeben = "00";
        erg = eclDbM.getDbBundle().dbZutrittskarten.readGast(lEclZutrittsIdent, 1);
        if (erg < 1) {
            uSession.setFehlermeldung("Gastkartennummer nicht vorhanden!");
            eclDbM.closeAll();
            xSessionVerwaltung.setzeUEnde();
            return "";
        }
        int meldungsIdent = eclDbM.getDbBundle().dbZutrittskarten.ergebnisPosition(0).meldungsIdentGast;

        einlesenGast(meldungsIdent);

        eclDbM.closeAll();
        return xSessionVerwaltung.setzeUEnde("uGastkarteAendern", false, false, eclUserLoginM.getKennung());
    }

    /*Suchen nach Gast Name*/
    public String doSuchenName() {
        if (!xSessionVerwaltung.pruefeUStart("uGastkarteSuchen", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }

        uSession.clearFehlermeldung();
        lSuchstring = uGastkarteSession.getSucheGastName().trim();
        eclDbM.openAll();

        eclDbM.getDbBundle().dbMeldungen.readarray_SucheNachNamen_init(0, lSuchstring);
        if (eclDbM.getDbBundle().dbMeldungen.readarray_anzahl == 0) {
            uGastkarteSession.setAnzeigeSeite(0);
            uSession.setFehlermeldung("Keine Daten zu diesem Namen gefunden!");
            eclDbM.closeAll();
            xSessionVerwaltung.setzeUEnde();
            return "";
        }

        suchListe(1);

        eclDbM.closeAll();

        xSessionVerwaltung.setzeUEnde();
        return "";
    }

    public String doVor() {
        if (!xSessionVerwaltung.pruefeUStart("uGastkarteSuchen", "")) {
            xSessionVerwaltung.setzeEnde();
            return "uDlgFehler";
        }
        eclDbM.openAll();

        eclDbM.getDbBundle().dbMeldungen.readarray_SucheNachNamen_init(0, lSuchstring);
        if (eclDbM.getDbBundle().dbMeldungen.readarray_anzahl == 0) {
            uGastkarteSession.setAnzeigeSeite(0);
            uSession.setFehlermeldung("Keine Daten zu diesem Namen gefunden!");
            eclDbM.closeAll();
            xSessionVerwaltung.setzeEnde();
            return "";
        }

        suchListe(uGastkarteSession.getAnzeigeSeite() + 1);
        eclDbM.closeAll();
        xSessionVerwaltung.setzeEnde();
        return "";
    }

    public String doZurueck() {
        if (!xSessionVerwaltung.pruefeUStart("uGastkarteSuchen", "")) {
            xSessionVerwaltung.setzeEnde();
            return "uDlgFehler";
        }
        eclDbM.openAll();
        eclDbM.getDbBundle().dbMeldungen.readarray_SucheNachNamen_init(0, lSuchstring);
        if (eclDbM.getDbBundle().dbMeldungen.readarray_anzahl == 0) {
            uGastkarteSession.setAnzeigeSeite(0);
            uSession.setFehlermeldung("Keine Daten zu diesem Namen gefunden!");
            eclDbM.closeAll();
            xSessionVerwaltung.setzeEnde();
            return "";
        }

        suchListe(uGastkarteSession.getAnzeigeSeite() - 1);
        eclDbM.closeAll();
        xSessionVerwaltung.setzeEnde();
        return "";
    }

    public String doAuswaehlen(EclGastM pGast) {
        if (!xSessionVerwaltung.pruefeUStart("uGastkarteSuchen", "")) {
            xSessionVerwaltung.setzeEnde();
            return "uDlgFehler";
        }

        uSession.clearFehlermeldung();
        uGastkarteSession.setAnzeigeSeite(0);

        eclDbM.openAll();

        int meldungsIdent = pGast.getMeldeIdent();

        einlesenGast(meldungsIdent);

        eclDbM.closeAll();
        return xSessionVerwaltung.setzeEnde("uGastkarteAendern", false, false, "");

    }

    /**Anzuzeigende Gastkarte einlesen
     * eclDbM muß offen sein*/
    public void einlesenGast(int pMeldungsIdent) {

        /*Allgemeine Tabellen füllen*/
        eclAnredeListeM.fuelleListe(eclDbM.getDbBundle());
        eclKommunikationsSpracheM.fuelleListe(eclDbM.getDbBundle());
        eclGruppeListeM.fuelleListe(eclDbM.getDbBundle());
        eclAusstellungsgrundListeM.fuelleListe(eclDbM.getDbBundle());
        eclVipKZListeM.fuelleListe(eclDbM.getDbBundle());

        eclGastM.init();

        EclMeldung lMeldung = new EclMeldung();
        lMeldung.meldungsIdent = pMeldungsIdent;

        eclDbM.getDbBundle().dbMeldungen.leseZuMeldungsIdent(lMeldung);
        lMeldung = eclDbM.getDbBundle().dbMeldungen.meldungenArray[0];

        eclGastM.copyFrom(lMeldung);

        /*ZutrittsIdents einlesen. Erste noch gültige ZutrittsIdent wird zusammen mit dem
         * Gast angezeigt. Alle anderen in Liste*/
        eclDbM.getDbBundle().dbZutrittskarten.readZuMeldungsIdentGast(lMeldung.meldungsIdent);
        int gueltigeGefunden = -1;
        for (int i = 0; i < eclDbM.getDbBundle().dbZutrittskarten.anzErgebnis(); i++) {
            EclZutrittskarten lZutrittskarte = eclDbM.getDbBundle().dbZutrittskarten.ergebnisPosition(i);
            if (gueltigeGefunden == -1 && lZutrittskarte.zutrittsIdentIstGesperrt == KonstStimmkarteIstGesperrt.aktiv) {
                eclGastM.setNummer(BlZutrittsIdent.aufbereitenFuerAnzeige(lZutrittskarte.zutrittsIdent, lZutrittskarte.zutrittsIdentNeben));
                eclGastM.setNummerNeben(lZutrittskarte.zutrittsIdentNeben);
                eclGastM.setNummerOhneNeben(lZutrittskarte.zutrittsIdent);
                gueltigeGefunden = 1;
            } else {
                EclZutrittsIdentAnzeigeM lZutrittsIdentAnzeigeM = new EclZutrittsIdentAnzeigeM();
                lZutrittsIdentAnzeigeM.setNummer(lZutrittskarte.zutrittsIdent);
                lZutrittsIdentAnzeigeM.setNummerNeben(lZutrittskarte.zutrittsIdentNeben);
                lZutrittsIdentAnzeigeM.setNummerAnzeige(BlZutrittsIdent.aufbereitenFuerAnzeige(lZutrittskarte.zutrittsIdent, lZutrittskarte.zutrittsIdentNeben));
                if (lZutrittskarte.zutrittsIdentIstGesperrt == KonstStimmkarteIstGesperrt.aktiv) {
                    lZutrittsIdentAnzeigeM.setStorniert(false);
                } else {
                    lZutrittsIdentAnzeigeM.setStorniert(true);
                }
                eclGastM.getAlleZutrittsIdents().add(lZutrittsIdentAnzeigeM);
            }
        }
        if (gueltigeGefunden == 1) {
            uGastkarteSession.setNeueNrZulaessig(false);
            uGastkarteSession.setStornierenNrZulaessig(true);
        } else {
            uGastkarteSession.setNeueNrZulaessig(true);
            uGastkarteSession.setStornierenNrZulaessig(false);
        }

        eclMeldungVipKZListeM.fuelleListe(eclDbM.getDbBundle(), lMeldung);
        eclGastM.setVipKZListe(eclMeldungVipKZListeM.getMeldungVipKZListeM());
        uGastkarteSession.setAnzVipKZListe(eclMeldungVipKZListeM.getMeldungVipKZListeM().size());

        eclMeldungAusstellungsgrundListeM.fuelleListe(eclDbM.getDbBundle(), lMeldung);
        eclGastM.setAusstellungsgrundListe(eclMeldungAusstellungsgrundListeM.getMeldungAusstellungsgrundListeM());
        uGastkarteSession.setAnzAusstellungsgrundListe(eclMeldungAusstellungsgrundListeM.getMeldungAusstellungsgrundListeM().size());

        eclMeldungenMeldungenListeM.fuelleListe(eclDbM.getDbBundle(), lMeldung);
        eclGastM.setMeldungenMeldungenListe(eclMeldungenMeldungenListeM.getMeldungenMeldungenListeM());
        if (eclGastM.getMeldungenMeldungenListe().size() > 0) {
            uGastkarteSession.setGruppenausstellung(true);
        } else {
            uGastkarteSession.setGruppenausstellung(false);
        }

    }

    private int anzahlProSeite = 20;

    /*Suchliste für Seite aufbereiten*/
    private void suchListe(int pSeite) {
        int startsatz = 0;
        int endesatz = 0;

        while ((pSeite - 1) * anzahlProSeite + 1 > eclDbM.getDbBundle().dbMeldungen.readarray_anzahl) {
            pSeite--;
        }

        uGastkarteSession.setAnzeigeSeite(pSeite);
        if (pSeite == 1) {
            uGastkarteSession.setRueckwaerts(false);
        } else {
            uGastkarteSession.setRueckwaerts(true);
        }

        if (eclDbM.getDbBundle().dbMeldungen.readarray_anzahl > pSeite * anzahlProSeite) {
            uGastkarteSession.setVorwaerts(true);
        } else {
            uGastkarteSession.setVorwaerts(false);
        }
        startsatz = (pSeite - 1) * anzahlProSeite + 1;
        endesatz = pSeite * anzahlProSeite;
        if (eclDbM.getDbBundle().dbMeldungen.readarray_anzahl < endesatz) {
            endesatz = eclDbM.getDbBundle().dbMeldungen.readarray_anzahl;
        }

        int i;
        EclMeldung lMeldung = null;
        eclGastListeM.init();
        for (i = startsatz; i <= endesatz; i++) {
            lMeldung = new EclMeldung();
            eclDbM.getDbBundle().dbMeldungen.readarray_SucheNachNamen_getoffset(i - 1, lMeldung);
            eclGastListeM.add(lMeldung);
        }
        return;
    }

    /********************************************************************************************/
    /***************************Gastkarte Ändern****************************************************/
    /********************************************************************************************/

    public String doStornieren() {
        if (!xSessionVerwaltung.pruefeUStart("uGastkarteAendern", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }

        EclMeldungAusstellungsgrundM meldungAusstellungsgrundM = null;
        boolean neuerAusstellungsgrund = false;

        eclDbM.openAll();

        /*Ggf. letzten Ausstellungsgrund in Liste übertragen*/
        uSession.clearFehlermeldung();
        if (uGastkarteSession.getNeuerAusstellungsgrundStorno() == null || uGastkarteSession.getNeuerAusstellungsgrundStorno().isEmpty()
                || uGastkarteSession.getNeuerAusstellungsgrundStorno().compareTo("_0") == 0) {
            if (uGastkarteSession.getNeuerAusstellungsgrundStornoKommentar() != null && !uGastkarteSession.getNeuerAusstellungsgrundStornoKommentar().isEmpty()) {
                uSession.setFehlermeldung("Kommentar ohne Kürzel bei Ausstellungsgrund unzulässig!");
                xSessionVerwaltung.setzeUEnde();
                return "";
            }
        } else {
            meldungAusstellungsgrundM = new EclMeldungAusstellungsgrundM();
            meldungAusstellungsgrundM.setAusstellungsGrundKuerzel(uGastkarteSession.getNeuerAusstellungsgrundStorno());
            meldungAusstellungsgrundM.setKommentar(uGastkarteSession.getNeuerAusstellungsgrundStornoKommentar());
            neuerAusstellungsgrund = true;

            for (int i = 0; i < eclAusstellungsgrundListeM.getAusstellungsgrundStornoListeM().size(); i++) {
                if (eclAusstellungsgrundListeM.getAusstellungsgrundStornoListeM().get(i).getKuerzel().compareTo(uGastkarteSession.getNeuerAusstellungsgrundStorno()) == 0) {
                    meldungAusstellungsgrundM.setBeschreibung(eclAusstellungsgrundListeM.getAusstellungsgrundStornoListeM().get(i).getBeschreibung());
                }
            }
            uGastkarteSession.setNeuerAusstellungsgrundStorno("_0");
            uGastkarteSession.setNeuerAusstellungsgrundStornoKommentar("");
        }

        BlGastkarte lGastkarte = new BlGastkarte(eclDbM.getDbBundle());

        lGastkarte.pZutrittsIdentNebenStorno = eclGastM.getNummerNeben();
        lGastkarte.pZutrittsIdentStorno = eclGastM.getNummerOhneNeben();
        int rc = lGastkarte.stornoGKNr();
        if (rc < 1) {
            CaBug.drucke("EControllerAendernGastkarte.doStornieren 001");
        }

        if (neuerAusstellungsgrund) {
            EclMeldungAusstellungsgrund meldungAusstellungsgrund = new EclMeldungAusstellungsgrund();
            meldungAusstellungsgrund.meldungsIdent = eclGastM.getMeldeIdent();
            meldungAusstellungsgrund.ausstellungsGrundKuerzel = meldungAusstellungsgrundM.getAusstellungsGrundKuerzel();
            meldungAusstellungsgrund.kommentar = meldungAusstellungsgrundM.getKommentar();
            eclDbM.getDbBundle().dbMeldungAusstellungsgrund.insert(meldungAusstellungsgrund);
        }

        einlesenGast(eclGastM.getMeldeIdent());

        eclDbM.closeAll();

        xSessionVerwaltung.setzeUEnde();
        return "";
    }

    public String doNeueNrVergeben() {
        if (!xSessionVerwaltung.pruefeUStart("uGastkarteAendern", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }
        neueNrVergeben(false);
        xSessionVerwaltung.setzeUEnde();
        return "";
    }

    public String doNeueNrVergebenSofortDruck() {
        if (!xSessionVerwaltung.pruefeUStart("uGastkarteAendern", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }

        /*Prüfen*/
        if (pruefeVorSpeichern() == false) {
            xSessionVerwaltung.setzeUEnde();
            return "";
        }

        /*Nummer speichern*/
        neueNrVergeben(true);

        speichernGastkarte(true);
        uGastkarteSession.setAenderung(true);
        return xSessionVerwaltung.setzeUEnde("uGastkarteNeuQuittung", false, false, "");
    }

    /*Ehemals: eControllerAendernGastkarte.doSpeichern*/
    public String doSpeichernAendern() {
        if (!xSessionVerwaltung.pruefeUStart("uGastkarteAendern", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }

        if (pruefeVorSpeichern() == false) {
            xSessionVerwaltung.setzeUEnde();
            return "";
        }
        speichernGastkarteAendern(false);
        return xSessionVerwaltung.setzeUEnde("uMenue", false, false, "");
    }

    /*Ehemals: eControllerAendernGastkarte.doSpeichernErstellen*/
    public String doSpeichernErstellenAendern() {
        if (!xSessionVerwaltung.pruefeUStart("uGastkarteAendern", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }
        if (uGastkarteSession.isNeueNrZulaessig()) {
            uSession.setFehlermeldung("Vor dem Ausstellen muß eine Gastkartennummer zugeordnet werden!");
            xSessionVerwaltung.setzeUEnde();
            return "";
        }
        if (pruefeVorSpeichern() == false) {
            xSessionVerwaltung.setzeUEnde();
            return "";
        }
        speichernGastkarteAendern(true);
        uGastkarteSession.setAenderung(true);
        return xSessionVerwaltung.setzeEnde("uGastkarteNeuQuittung", false, false, "");
    }

    private void neueNrVergeben(boolean pSofortdruck) {
        eclDbM.openAll();

        BlGastkarte lGastkarte = new BlGastkarte(eclDbM.getDbBundle());

        //pWillenserklaerungGeberIdent

        /*Meldung Gast füllen*/
        EclMeldung lGastMeldung = new EclMeldung();
        eclGastM.copyTo(lGastMeldung);

        /*Versandart*/
        lGastkarte.pVersandart = 2;
        uGastkarteSession.setAusstellungsart("3");
        if (pSofortdruck == true) {
            lGastkarte.pVersandart = 5;
            uGastkarteSession.setAusstellungsart("2");
            if (!eclGastM.getMailadresse().isEmpty()) {
                lGastkarte.pVersandart = 4;
                uGastkarteSession.setAusstellungsart("1");
            }
        }

        lGastkarte.pGast = lGastMeldung;
        lGastkarte.pWillenserklaerungGeberIdent = 0; /*undefiniert*/

        lGastkarte.pVersandAnAdresseAusAktienregister = 0;

        int rc = lGastkarte.neueGKNr(eclGastM.getMeldeIdent(), 0);
        if (rc < 1) {
            CaBug.drucke("EControllerAendernGastkarte.doNeueNrVergeben 001");
        }
        eclGastM.setNummer(lGastkarte.rcZutrittsIdent);

        einlesenGast(eclGastM.getMeldeIdent());

        eclDbM.closeAll();
    }

    private boolean pruefeVorSpeichern() {
        /*Plausibilitätsprüfung*/
        uSession.clearFehlermeldung();

        /*Ggf. Ausstellungsgrund und VipKZ in Liste übertragen*/
        if (weitererAusstellungsgrund() == false) {
            return false;
        }
        if (weiteresVipKZ() == false) {
            return false;
        }

        if (pruefenFelderAusgefuellt() == false) {
            uSession.setFehlermeldung("Feld " + getAuszufuellendesFeld() + " ist zwingend auszufüllen");
            return false;
        }
        return true;
    }

    /**Wird von speichernGastkarte gefüllt. Wenn -1, dann wurde Ek per Mail versand - keine Quittung erforderlich*/
    private int pdfNrZumAnzeigen = -1;

    private void speichernGastkarteAendern(boolean pDrucken) {

        eclDbM.openAll();
        BlGastkarte blGastkarte = new BlGastkarte(eclDbM.getDbBundle());
        EclMeldung lMeldung = blGastkarte.aendernInit(eclGastM.getMeldeIdent());

        eclGastM.copyTo(lMeldung);
        int versandart = 5;
        uGastkarteSession.setAusstellungsart("2");
        if (!lMeldung.mailadresse.isEmpty()) {
            versandart = 4; //Mail
            uGastkarteSession.setAusstellungsart("1");
        }

        EclZutrittsIdent lZutrittsIdent = new EclZutrittsIdent();
        lZutrittsIdent.zutrittsIdent = eclGastM.getNummerOhneNeben();
        lZutrittsIdent.zutrittsIdentNeben = eclGastM.getNummerNeben();

        aufbereitenFuerQuittung();
        blGastkarte.pAnredeKomplett = eclGastM.getAnredeKomplett();
        blGastkarte.pAnrede = eclGastM.getAnredeText();

        blGastkarte.aendernSpeichern(lMeldung, pDrucken, versandart, lZutrittsIdent);

        /*Gastkarten-PDF vorbereiten*/
        pdfNrZumAnzeigen = -1;
        if (pDrucken) {
            pdfNrZumAnzeigen = blGastkarte.rcGastkartePdfNr;
            uGastkarteSession.setGastkartePdfNr(Integer.toString(pdfNrZumAnzeigen));

            if (versandart == 4) {//Mail
                if ((eclDbM.getDbBundle().param.paramGaesteModul.mailVerschickenGK & 1) == 1) {
                    /*Nun per Mail versenden mit Gastkarte als Anhang*/
                    String[] variablenArray = { "L_ANREDE" };
                    String[] inhalteArray = { "" };
                    inhalteArray[0] = eclGastM.getAnredeKomplett();
                    eclPortalTexteM.setLokaleVariablenNamen(variablenArray);
                    eclPortalTexteM.setLokaleVariablenInhalt(inhalteArray);

                    String betreffText = eclPortalTexteM.holeText("1477");
                    betreffText = CaVariablenInText.aufbereiteFuerNormalesMail(betreffText);

                    String inhaltText = eclPortalTexteM.holeText("1478");
                    inhaltText = CaVariablenInText.aufbereiteFuerNormalesMail(inhaltText);

                    baMailm.sendenMitAnhang(eclGastM.getMailadresse(), eclDbM.getDbBundle().param.paramGaesteModul.bccAdresse1, eclDbM.getDbBundle().param.paramGaesteModul.bccAdresse2, betreffText,
                            inhaltText, eclDbM.getDbBundle().lieferePfadMeetingAusdrucke() + "\\zutrittsdokumentM"
                                    + eclDbM.getDbBundle().getMandantString() + Integer.toString(blGastkarte.rcGastkartePdfNr) + ".pdf");
                }
                if ((eclDbM.getDbBundle().param.paramGaesteModul.mailVerschickenGK & 2) == 2) {
                    /*Nun per Mail versenden - nur Bestätigung*/
                    String[] variablenArray = { "L_ANREDE" };
                    String[] inhalteArray = { "" };
                    inhalteArray[0] = eclGastM.getAnredeKomplett();
                    eclPortalTexteM.setLokaleVariablenNamen(variablenArray);
                    eclPortalTexteM.setLokaleVariablenInhalt(inhalteArray);

                    String betreffText = eclPortalTexteM.holeText("1479");
                    betreffText = CaVariablenInText.aufbereiteFuerNormalesMail(betreffText);

                    String inhaltText = eclPortalTexteM.holeText("1480");
                    inhaltText = CaVariablenInText.aufbereiteFuerNormalesMail(inhaltText);

                    baMailm.sendenMitAnhang(eclGastM.getMailadresse(), eclDbM.getDbBundle().param.paramGaesteModul.bccAdresse1, eclDbM.getDbBundle().param.paramGaesteModul.bccAdresse2, betreffText,
                            inhaltText, "");
                }
            }
        }

        int anzneu, anzalt;
        /*Ausstellungsgrund speichern*/
        anzneu = eclGastM.getAusstellungsgrundListe().size();
        anzalt = uGastkarteSession.getAnzAusstellungsgrundListe();
        if (anzneu > anzalt) {
            for (int i = anzalt; i < anzneu; i++) {
                speichernAusstellungsgrund(i, lMeldung.meldungsIdent);
            }
        }

        /*VipKZ-Liste speichern*/
        anzneu = eclGastM.getVipKZListe().size();
        anzalt = uGastkarteSession.getAnzVipKZListe();
        if (anzneu > anzalt) {
            for (int i = anzalt; i < anzneu; i++) {
                speichernVipKZKuerzel(i, lMeldung.meldungsIdent);
            }
        }

        aufbereitenFuerQuittung();

        eclDbM.closeAll();
        return;
    }




    public String doBlankoErstellen() {
        if (!xSessionVerwaltung.pruefeUStart("uGastkarteBlanko", "")) {
            xSessionVerwaltung.setzeEnde();
            return "uDlgFehler";
        }
        uSession.clearFehlermeldung();

        String hAnzahlKarten = uGastkarteSession.getAnzahlKarten();
        if (hAnzahlKarten.isEmpty() || !CaString.isNummern(hAnzahlKarten)) {
            uSession.setFehlermeldung("Bitte korrekte Anzahl eingeben");
            xSessionVerwaltung.setzeEnde();
            return "";

        }

        int anzahlKarten = Integer.parseInt(hAnzahlKarten);
        eclDbM.openAll();
        BlGastkarte blGastkarte = new BlGastkarte(eclDbM.getDbBundle());

        for (int i = 0; i < anzahlKarten; i++) {
            EclMeldung lGastMeldung = new EclMeldung();

            lGastMeldung.kommunikationssprache = -1;
            lGastMeldung.name = "Gast";

            blGastkarte.pVersandart = 6;

            blGastkarte.pGast = lGastMeldung;
            blGastkarte.pWillenserklaerungGeberIdent = 0; /*undefiniert*/

            int erg = blGastkarte.ausstellen();

            if (erg < 0) {
                uSession.setFehlermeldung(CaFehler.getFehlertext(erg, 0));
                eclDbM.closeAllAbbruch();
                return "";
            }

        }
        blGastkarte.druckenEnde();

        String hDatei = eclDbM.getDbBundle().lieferePfadMeetingAusdrucke() + "\\" + blGastkarte.rcNamePDF + ".pdf";

        RpBrowserAnzeigen rpBrowserAnzeigen = new RpBrowserAnzeigen();
        rpBrowserAnzeigen.zeigen(hDatei, "BlankoGastkarten.pdf");

        eclDbM.closeAll();

        xSessionVerwaltung.setzeUEnde();
        return "";
    }

    public String doInstiErstellen() {
        if (!xSessionVerwaltung.pruefeUStart("uGastkarteInsti", "")) {
            xSessionVerwaltung.setzeEnde();
            return "uDlgFehler";
        }
        uSession.clearFehlermeldung();

        List<EclDateiDownload> instiPDFs=new LinkedList<EclDateiDownload>();

        eclDbM.openAll();
        BlGastkarte blGastkarte = new BlGastkarte(eclDbM.getDbBundle());
        blGastkarte.pAbweichenderDateiname="Schutz_Zugangskarte";

        EclMeldung lGastMeldung = new EclMeldung();

        lGastMeldung.kommunikationssprache = 2;//Insti
        lGastMeldung.anrede=2;
        lGastMeldung.name = "Schutz";
        lGastMeldung.ort="München";

        blGastkarte.pVersandart = 6;

        blGastkarte.pGast = lGastMeldung;
        blGastkarte.pWillenserklaerungGeberIdent = 0; /*undefiniert*/

        int erg = blGastkarte.ausstellen();

        if (erg < 0) {
            uSession.setFehlermeldung(CaFehler.getFehlertext(erg, 0));
            eclDbM.closeAllAbbruch();
            return "";
        }
        blGastkarte.druckenEnde();

        EclDateiDownload lDateiDownload=new EclDateiDownload();
                
        lDateiDownload.quellDateinameAufServer = blGastkarte.rcNamePDF + ".pdf";
        lDateiDownload.zielDateinameAnzeige="Schutz_Zugangskarte.pdf";
        instiPDFs.add(lDateiDownload);

        blGastkarte = new BlGastkarte(eclDbM.getDbBundle());
        blGastkarte.pAbweichenderDateiname="Schutz1_Zugangskarte";

        lGastMeldung = new EclMeldung();
        lGastMeldung.kommunikationssprache = 1;//Insti
        lGastMeldung.anrede=2;
        lGastMeldung.name = "Schutz1";
        lGastMeldung.ort="Düsseldorf";

        blGastkarte.pVersandart = 6;

        blGastkarte.pGast = lGastMeldung;
        blGastkarte.pWillenserklaerungGeberIdent = 0; /*undefiniert*/

        erg = blGastkarte.ausstellen();

        if (erg < 0) {
            uSession.setFehlermeldung(CaFehler.getFehlertext(erg, 0));
            eclDbM.closeAllAbbruch();
            return "";
        }
        blGastkarte.druckenEnde();

        lDateiDownload=new EclDateiDownload();
        lDateiDownload.quellDateinameAufServer = blGastkarte.rcNamePDF + ".pdf";
        lDateiDownload.zielDateinameAnzeige="Schutz1_Zugangskarte.pdf";
        instiPDFs.add(lDateiDownload);

        
        eclDbM.closeAll();

        uGastkarteSession.setInstiAblauf(1);
        uGastkarteSession.setInstiPDFs(instiPDFs);
        
        xSessionVerwaltung.setzeUEnde();
        return "";
    }

    
    public String doInternErstellen() {
        if (!xSessionVerwaltung.pruefeUStart("uGastkarteIntern", "")) {
            xSessionVerwaltung.setzeEnde();
            return "uDlgFehler";
        }
        uSession.clearFehlermeldung();

        boolean nurErgaenzungMailen=true;

        String hAnzahlKarten = uGastkarteSession.getAnzPortalTeamKartenErstellen();
        if (hAnzahlKarten.isEmpty() || !CaString.isNummern(hAnzahlKarten)) {
            uSession.setFehlermeldung("Bitte korrekte Anzahl eingeben");
            xSessionVerwaltung.setzeEnde();
            return "";
        }
        hAnzahlKarten = uGastkarteSession.getAnzConsultantKartenErstellen();
        if (hAnzahlKarten.isEmpty() || !CaString.isNummern(hAnzahlKarten)) {
            uSession.setFehlermeldung("Bitte korrekte Anzahl eingeben");
            xSessionVerwaltung.setzeEnde();
            return "";
        }
        hAnzahlKarten = uGastkarteSession.getAnzahlKundenKartenErstellen();
        if (hAnzahlKarten.isEmpty() || !CaString.isNummern(hAnzahlKarten)) {
            uSession.setFehlermeldung("Bitte korrekte Anzahl eingeben");
            xSessionVerwaltung.setzeEnde();
            return "";
        }
        hAnzahlKarten = uGastkarteSession.getAnzahlNotarKartenErstellen();
        if (hAnzahlKarten.isEmpty() || !CaString.isNummern(hAnzahlKarten)) {
            uSession.setFehlermeldung("Bitte korrekte Anzahl eingeben");
            xSessionVerwaltung.setzeEnde();
            return "";
        }

        /*Berechtigungen*/
//      @formatter:off
        
        final long berechtigungenRZ=
             KonstPortalFunktionen.berechtigungsbit(KonstPortalFunktionen.fragen) +
             KonstPortalFunktionen.berechtigungsbit(KonstPortalFunktionen.monitoring);

        final long berechtigungenPortalteam=
                KonstPortalFunktionen.berechtigungsbit(KonstPortalFunktionen.fragen) +
                KonstPortalFunktionen.berechtigungsbit(KonstPortalFunktionen.monitoring);

        final long berechtigungenConsultant=
                KonstPortalFunktionen.berechtigungsbit(KonstPortalFunktionen.fragen) +
                KonstPortalFunktionen.berechtigungsbit(KonstPortalFunktionen.wortmeldungen) +
                KonstPortalFunktionen.berechtigungsbit(KonstPortalFunktionen.widersprueche) +
                KonstPortalFunktionen.berechtigungsbit(KonstPortalFunktionen.antraege) +
                KonstPortalFunktionen.berechtigungsbit(KonstPortalFunktionen.sonstigeMitteilungen) +
                KonstPortalFunktionen.berechtigungsbit(KonstPortalFunktionen.teilnehmerverzeichnis) +
                KonstPortalFunktionen.berechtigungsbit(KonstPortalFunktionen.abstimmungsergebnisse) +
                KonstPortalFunktionen.berechtigungsbit(KonstPortalFunktionen.botschaftenEinreichen) +
                KonstPortalFunktionen.berechtigungsbit(KonstPortalFunktionen.botschaftenAnzeige) +
                KonstPortalFunktionen.berechtigungsbit(KonstPortalFunktionen.preview) +
                KonstPortalFunktionen.berechtigungsbit(KonstPortalFunktionen.monitoring);

        final long berechtigungenKunde=
                KonstPortalFunktionen.berechtigungsbit(KonstPortalFunktionen.fragen) +
                KonstPortalFunktionen.berechtigungsbit(KonstPortalFunktionen.widersprueche) +
                KonstPortalFunktionen.berechtigungsbit(KonstPortalFunktionen.abstimmungsergebnisse) +
                KonstPortalFunktionen.berechtigungsbit(KonstPortalFunktionen.monitoring);

        final long berechtigungenNotar=
                KonstPortalFunktionen.berechtigungsbit(KonstPortalFunktionen.fragen) +
                KonstPortalFunktionen.berechtigungsbit(KonstPortalFunktionen.abstimmungsergebnisse) +
                KonstPortalFunktionen.berechtigungsbit(KonstPortalFunktionen.monitoring);

//      @formatter:on

        eclDbM.openAll();
        BlGastkarte blGastkarte = new BlGastkarte(eclDbM.getDbBundle());
        if (uGastkarteSession.isRzKartenErstellen()) {
            nurErgaenzungMailen=false;


        }

        if (uGastkarteSession.isRzKartenErstellen() || uGastkarteSession.isRzKartenErstellenErgaenzen()) {
        }

        int anzahlKarten = Integer.parseInt(uGastkarteSession.getAnzPortalTeamKartenErstellen());
        for (int i = 0; i < anzahlKarten; i++) {
            nurErgaenzungMailen=false;
            EclMeldung lGastMeldung = new EclMeldung();

            lGastMeldung.kommunikationssprache = -1;
            lGastMeldung.name = "#BO_Portalteam_" + Integer.toString(i + 1);
            blGastkarte.pVersandart = 6;
            blGastkarte.pGast = lGastMeldung;
            blGastkarte.pWillenserklaerungGeberIdent = 0; /*undefiniert*/
            blGastkarte.berechtigungsWert = berechtigungenPortalteam;
            int erg = blGastkarte.ausstellen();

            if (erg < 0) {
                uSession.setFehlermeldung(CaFehler.getFehlertext(erg, 0));
                eclDbM.closeAllAbbruch();
                return "";
            }
        }

        anzahlKarten = Integer.parseInt(uGastkarteSession.getAnzConsultantKartenErstellen());
        for (int i = 0; i < anzahlKarten; i++) {
            nurErgaenzungMailen=false;
            EclMeldung lGastMeldung = new EclMeldung();

            lGastMeldung.kommunikationssprache = -1;
            lGastMeldung.name = "#BO_Consultant_" + Integer.toString(i + 1);
            blGastkarte.pVersandart = 6;
            blGastkarte.pGast = lGastMeldung;
            blGastkarte.pWillenserklaerungGeberIdent = 0; /*undefiniert*/
            blGastkarte.berechtigungsWert = berechtigungenConsultant;
            int erg = blGastkarte.ausstellen();

            if (erg < 0) {
                uSession.setFehlermeldung(CaFehler.getFehlertext(erg, 0));
                eclDbM.closeAllAbbruch();
                return "";
            }
        }

        anzahlKarten = Integer.parseInt(uGastkarteSession.getAnzahlKundenKartenErstellen());
        for (int i = 0; i < anzahlKarten; i++) {
            nurErgaenzungMailen=false;
            EclMeldung lGastMeldung = new EclMeldung();

            lGastMeldung.kommunikationssprache = -1;
            lGastMeldung.name = "#AG_Steuerung_" + Integer.toString(i + 1);
            blGastkarte.pVersandart = 6;
            blGastkarte.pGast = lGastMeldung;
            blGastkarte.pWillenserklaerungGeberIdent = 0; /*undefiniert*/
            blGastkarte.berechtigungsWert = berechtigungenKunde;
            int erg = blGastkarte.ausstellen();

            if (erg < 0) {
                uSession.setFehlermeldung(CaFehler.getFehlertext(erg, 0));
                eclDbM.closeAllAbbruch();
                return "";
            }
        }

        anzahlKarten = Integer.parseInt(uGastkarteSession.getAnzahlNotarKartenErstellen());
        for (int i = 0; i < anzahlKarten; i++) {
            nurErgaenzungMailen=false;
            EclMeldung lGastMeldung = new EclMeldung();

            lGastMeldung.kommunikationssprache = -1;
            lGastMeldung.name = "#AG_Notar_" + Integer.toString(i + 1);
            blGastkarte.pVersandart = 6;
            blGastkarte.pGast = lGastMeldung;
            blGastkarte.pWillenserklaerungGeberIdent = 0; /*undefiniert*/
            blGastkarte.berechtigungsWert = berechtigungenNotar;
            int erg = blGastkarte.ausstellen();

            if (erg < 0) {
                uSession.setFehlermeldung(CaFehler.getFehlertext(erg, 0));
                eclDbM.closeAllAbbruch();
                return "";
            }
        }

        blGastkarte.druckenEnde();
        eclDbM.closeAll();

        
        interneGastkarteMailen(nurErgaenzungMailen);

        String hDatei = eclDbM.getDbBundle().lieferePfadMeetingAusdrucke() + "\\" + blGastkarte.rcNamePDF + ".pdf";

        RpBrowserAnzeigen rpBrowserAnzeigen = new RpBrowserAnzeigen();
        rpBrowserAnzeigen.zeigen(hDatei);

        xSessionVerwaltung.setzeUEnde();
        return "";
    }

    public String doInternVerschicken() {
        if (!xSessionVerwaltung.pruefeUStart("uGastkarteIntern", "")) {
            xSessionVerwaltung.setzeEnde();
            return "uDlgFehler";
        }
        uSession.clearFehlermeldung();

        interneGastkarteMailen(false);

        xSessionVerwaltung.setzeUEnde();
        return "";
    }

    private void interneGastkarteMailen(boolean pNurErgaenzungMailen) {
        eclDbM.openAll();

        String mailConsultant=eclParamM.getParam().paramBasis.mailConsultant;
        
        eclDbM.getDbBundle().dbMeldungen.leseAlleGastkarten();
        EclMeldung[] lGaeste = eclDbM.getDbBundle().dbMeldungen.meldungenArray;
        if (lGaeste != null) {
            BlEinsprungLinkPortal blEinsprungLinkPortal=new BlEinsprungLinkPortal(eclDbM.getDbBundle());
            for (int i = 0; i < lGaeste.length; i++) {
                EclMeldung lMeldung = lGaeste[i];
                if (lMeldung.name.startsWith("#") && pNurErgaenzungMailen==false) {
                    String lKennung=lMeldung.loginKennung;
                    eclDbM.getDbBundle().dbLoginDaten.read_loginKennung(lKennung);
                    EclLoginDaten lLoginDaten=eclDbM.getDbBundle().dbLoginDaten.ergebnisPosition(0);
                    String passwort=CaPasswortVerschluesseln.liefereInitialPasswort(lLoginDaten.passwortInitial, eclParamM.getParam().paramPortal.passwortMindestLaenge);
                    
                    String mailAdresse = "";
                    String hBetreff="";
                    //@formatter:off
                    if (lMeldung.name.startsWith("XY")) {mailAdresse = "XY@A.B";hBetreff=lMeldung.name;}
                    //@formatter:on
                    
                    String portalLink=blEinsprungLinkPortal.linkFuerPortalStart((long) 0, 0, "", "", "DE", "", false)+"&nummer="+lKennung+"&p="+passwort;
                    String portalKurzLink=blEinsprungLinkPortal.linkKurzFuerPortalStart()+"?nummer="+lKennung+"&p="+passwort;
                    
                    String mailBetreff="Zugangs-Link "+eclParamM.getMandantString()+" "+eclParamM.getEmittentName()+" "+hBetreff;
                    String mailInhalt="";
                    if (portalKurzLink.isEmpty()==false) {
                        mailInhalt="\n"+portalKurzLink+"\n\n";
                    }

                    mailInhalt+="\n"+portalLink+"\n\n";
                    
//                    System.out.println(mailBetreff);
//                    System.out.println(mailInhalt);
                    
                    String[] zeileSplit = mailAdresse.split(";");
                    for (int i1 = 0; i1 < zeileSplit.length; i1++) {
                        String[] zeileSplitKomma=zeileSplit[i1].split(",");
                        for (int i2=0;i2<zeileSplitKomma.length;i2++) {
                            String empfaenger = zeileSplitKomma[i2].trim();
                            if (!empfaenger.isEmpty()) {
                                CaBug.druckeLog("Mail mit Zugangsdaten an "+empfaenger, logDrucken, 10);
                                baMailm.senden(empfaenger, mailBetreff, mailInhalt);
                            }
                        }
                    }

                }
            }
        }
        eclDbM.closeAll();
    }

}
