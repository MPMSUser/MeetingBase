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
package de.meetingapps.meetingportal.meetingportTController;

import java.math.BigDecimal;
import java.math.RoundingMode;

import de.meetingapps.meetingportal.meetComAllg.CaBank;
import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComAllg.CaString;
import de.meetingapps.meetingportal.meetComBrM.BrMAktionaersdaten;
import de.meetingapps.meetingportal.meetComBrM.BrMBestand;
import de.meetingapps.meetingportal.meetComBrM.BrMBeteiligungserhoehung;
import de.meetingapps.meetingportal.meetComBrM.BrMPublikationen;
import de.meetingapps.meetingportal.meetComEclM.EclDbM;
import de.meetingapps.meetingportal.meetComEclM.EclLoginDatenM;
import de.meetingapps.meetingportal.meetComEclM.EclParamM;
import de.meetingapps.meetingportal.meetComKonst.KonstAktienregisterErgaenzung;
import de.meetingapps.meetingportal.meetComKonst.KonstPortalView;
import de.meetingapps.meetingportal.meetingCoreReport.RpBrowserAnzeigen;
import de.meetingapps.meetingportal.meetingportTFunktionen.TRemoteAR;
import de.meetingapps.meetingportal.meetingportTFunktionen.TSessionVerwaltung;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@RequestScoped
@Named
public class PBeteiligungserhoehung {

    private int logDrucken = 3;

    private @Inject EclDbM eclDbM;

    private @Inject EclLoginDatenM eclLoginDatenM;

    private @Inject BrMBeteiligungserhoehung brMBeteiligungserhoehung;

    private @Inject BrMBestand brMBestand;

    private @Inject BrMAktionaersdaten brMAktionaersdaten;

    private @Inject BrMPublikationen brMPublikationen;

    private @Inject PBeteiligungserhoehungSession pBeteiligungserhoehungSession;

    private @Inject TRemoteAR tRemoteAR;

    private @Inject TSession tSession;

    private @Inject PBestandSession pBestandSession;

    private @Inject TSessionVerwaltung tSessionVerwaltung;

    private @Inject PAktionaersdatenSession pAktionaersdatenSession;
    private @Inject EclParamM eclParamM;

    /**pOpenDurchfuehren=true => es wird der Open/close in init durchgeführt.
     */
    public int init(boolean pOpenDurchfuehren) {

        pBeteiligungserhoehungSession.clear();

        if (pOpenDurchfuehren) {
            eclDbM.openAll();
        }

        /*
         * Aktueller Stand = Alle Umsätze
         */
        int rc = brMAktionaersdaten.holeAktuellenStand(eclLoginDatenM.getEclLoginDaten().loginKennung);
        int rc2 = brMBestand.holeAktuellenStand(eclLoginDatenM.getEclLoginDaten().loginKennung);
        int rc3 = brMPublikationen.holeAktuelleEinstellungen(eclLoginDatenM.getEclLoginDaten().loginKennung);

        if (tRemoteAR.pruefeVerfuegbar(rc) == false || tRemoteAR.pruefeVerfuegbar(rc2) == false
                || tRemoteAR.pruefeVerfuegbar(rc3) == false) {
            if (pOpenDurchfuehren) {
                eclDbM.closeAll();
            }
            return CaFehler.perRemoteAktienregisterNichtVerfuegbar;
        }

        if (pOpenDurchfuehren) {
            eclDbM.closeAll();
        }

        return 1;
    }

    public void doBerechnen() {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.P_BETEILIGUNGSERHOEHUNG)) {
            return;
        }

        String wunschbetragString = "";
        double wunschbetrag = 0.00;

        if (pBestandSession.getAuffuellBetrag() == 0) {
            pBeteiligungserhoehungSession.setWeitereAnteile(CaString.integerParseInt(pBeteiligungserhoehungSession.getWeitereAnteileString()));
            wunschbetrag = pBeteiligungserhoehungSession.getWeitereAnteile() * 50.00;
            wunschbetragString = CaString.doubleToStringDE(wunschbetrag);
        } else {
            pBeteiligungserhoehungSession.setWeitereAnteile(0);
            pBeteiligungserhoehungSession.setVerwendbarerBetragAnteile(0.0);
            wunschbetragString = pBeteiligungserhoehungSession.getErhoehungsbetragString().replace(".", "");
            wunschbetrag = CaString.doubleParseDouble(wunschbetragString.replace(",", "."));
        }

        pBeteiligungserhoehungSession.setErhoehungsbetragString(CaString.toEuroStringDE(wunschbetrag));
        pBeteiligungserhoehungSession.setErhoehungsbetrag(wunschbetrag);

        /*if (wunschbetragString.replace(".", "").matches("\\d+,\\d{2}")) {*/

        if (wunschbetrag > 0) {

            double co2gespart = (wunschbetrag / 250);

            pBeteiligungserhoehungSession.setCo2gespart(co2gespart);
            pBeteiligungserhoehungSession.setCo2gespartString(CaString.doubleToStringDEKurz(co2gespart));

            if (pBestandSession.getAuffuellBetrag() == 0) {
                berechneAnteile(wunschbetrag, false);
                pBeteiligungserhoehungSession.setBeteiligungserhoehungWeiterStep("4");
            } else {
                berechneAuffuelBetrag(wunschbetrag);
            }

            pBeteiligungserhoehungSession.setBtnBerechnenText("Neu berechnen");

            if (!pruefeObKind()) {
                pBeteiligungserhoehungSession.setOkBeiMinderjaehrig(true);
            }

        } else {
            CaBug.druckeLog("Wert muss > 0 sein.", logDrucken, 10);
            pBeteiligungserhoehungSession.setBerechneteDatenAnzeigen(false);
            tSessionVerwaltung.setzeEnde();
            return;
        }

    }

    private void berechneAnteile(double wunschbetrag, Boolean istUeberschuss) {
        /*if (pBeteiligungserhoehungSession.getErhoehungsbetrag() >= 50) {*/
        double nichtverwendbarerBetrag = wunschbetrag % 50;
        double nichtverwendbarerBetragRounded = round(nichtverwendbarerBetrag, 2);

        if (nichtverwendbarerBetragRounded == 50.0) {
            nichtverwendbarerBetrag = 0.0;
            nichtverwendbarerBetragRounded = 0.0;
        }
        pBeteiligungserhoehungSession.setNichtVerwendbarerBetrag(nichtverwendbarerBetragRounded);
        pBeteiligungserhoehungSession
                .setNichtVerwendbarerBetragString(CaString.doubleToStringDE(round(nichtverwendbarerBetrag, 2)));

        double verwendbarerBetragAnteile = wunschbetrag - nichtverwendbarerBetrag;
        double verwendbarerBetragAnteileRounded = round(verwendbarerBetragAnteile, 2);
        pBeteiligungserhoehungSession.setVerwendbarerBetragAnteile(verwendbarerBetragAnteile);
        pBeteiligungserhoehungSession
                .setVerwendbarerBetragAnteileString(CaString.doubleToStringDE(verwendbarerBetragAnteile));

        double verwendbarerBetragAnteileGesamt = verwendbarerBetragAnteile + nichtverwendbarerBetrag;
        pBeteiligungserhoehungSession.setVerwendbarerBetragAnteileGesamt(verwendbarerBetragAnteileGesamt);
        pBeteiligungserhoehungSession
                .setVerwendbarerBetragAnteileGesamtString(CaString.doubleToStringDE(verwendbarerBetragAnteileGesamt));

        double verwendbarerBetragGesamt = verwendbarerBetragAnteile + pBestandSession.getAuffuellBetrag();
        pBeteiligungserhoehungSession.setVerwendbarerBetragGesamt(verwendbarerBetragGesamt);
        pBeteiligungserhoehungSession
                .setVerwendbarerBetragGesamtString(CaString.doubleToStringDE(verwendbarerBetragGesamt));

        double optimalerBetragDouble = 0.0;

        if (pBestandSession.getAuffuellBetrag() > 0.0 && nichtverwendbarerBetragRounded > 0.0) {
            optimalerBetragDouble = wunschbetrag + pBestandSession.getAuffuellBetrag() + 50.0 - nichtverwendbarerBetrag;
        }

        pBeteiligungserhoehungSession.setOptimalerBetrag(optimalerBetragDouble);
        pBeteiligungserhoehungSession.setOptimalerBetragString(CaString.doubleToStringDE(optimalerBetragDouble));

        int weitereAnteile = (int) (verwendbarerBetragAnteileRounded / 50);
        pBeteiligungserhoehungSession.setWeitereAnteile(weitereAnteile);
        pBeteiligungserhoehungSession.setWeitereAnteileString(CaString.toStringDE(weitereAnteile));
        int geschaeftsanteileNeu = pBestandSession.getGeschaeftsanteile() + weitereAnteile;
        pBeteiligungserhoehungSession.setGeschaeftsanteileNeu(geschaeftsanteileNeu);
        pBeteiligungserhoehungSession.setGeschaeftsanteileNeuDE(CaString.integerToString(geschaeftsanteileNeu));

        double geschaeftsanteileNominalNeu = pBestandSession.getGeschaeftsguthabenNominal() + verwendbarerBetragAnteile;
        pBeteiligungserhoehungSession.setGeschaeftsguthabenNominalNeu(geschaeftsanteileNominalNeu);
        pBeteiligungserhoehungSession
                .setGeschaeftsguthabenNominalNeuDE(CaString.doubleToStringDE(geschaeftsanteileNominalNeu));

        double geschaeftsguthabenAktuellNeu = geschaeftsanteileNominalNeu;
        pBeteiligungserhoehungSession.setGeschaeftsguthabenAktuellNeu(geschaeftsguthabenAktuellNeu);
        pBeteiligungserhoehungSession
                .setGeschaeftsguthabenAktuellNeuDE(CaString.doubleToStringDE(geschaeftsguthabenAktuellNeu));
        
        pBeteiligungserhoehungSession.setBerechneteDatenAnzeigen(true);

    }

    private void berechneAuffuelBetrag(double wunschbetrag) {
        Boolean betragReichtAus = wunschbetrag > pBestandSession.getAuffuellBetrag();
        if (!betragReichtAus) {
            pBeteiligungserhoehungSession.setGeschaeftsanteileNeu(pBestandSession.getGeschaeftsanteile());
            pBeteiligungserhoehungSession.setGeschaeftsanteileNeuDE(pBestandSession.getGeschaeftsanteileDE());

            pBeteiligungserhoehungSession
                    .setGeschaeftsguthabenNominalNeu(pBestandSession.getGeschaeftsguthabenNominal());
            pBeteiligungserhoehungSession
                    .setGeschaeftsguthabenNominalNeuDE(pBestandSession.getGeschaeftsguthabenNominalDE());

            double geschaeftsguthabenAktuellNeu = pBestandSession.getGeschaeftsguthabenAktuell() + wunschbetrag;
            pBeteiligungserhoehungSession.setGeschaeftsguthabenAktuellNeu(geschaeftsguthabenAktuellNeu);
            pBeteiligungserhoehungSession
                    .setGeschaeftsguthabenAktuellNeuDE(CaString.doubleToStringDE(geschaeftsguthabenAktuellNeu));

            double auffuellbetragNeu = pBestandSession.getAuffuellBetrag() - wunschbetrag;
            pBeteiligungserhoehungSession.setAuffuellBetragNeu(auffuellbetragNeu);
            pBeteiligungserhoehungSession.setAuffuellBetragNeuDE(CaString.doubleToStringDE(auffuellbetragNeu));

            pBeteiligungserhoehungSession.setAuffuellBetragZuUeberweisen(wunschbetrag);
            pBeteiligungserhoehungSession.setAuffuellBetragZuUeberweisenString(CaString.doubleToStringDE(wunschbetrag));

            pBeteiligungserhoehungSession.setBerechneteDatenAnzeigen(true);
            pBeteiligungserhoehungSession.setBerechneteDetailsAnzeigen(false);
            pBeteiligungserhoehungSession.setBeteiligungserhoehungWeiterStep("2");

            eclDbM.openAll();
            brMBeteiligungserhoehung.auffuellbetragSpeichern(eclLoginDatenM.getEclLoginDaten().loginKennung);
            eclDbM.closeAll();

        } else {
            double ueberschuss = wunschbetrag - pBestandSession.getAuffuellBetrag();
            berechneAnteile(ueberschuss, true);

            double auffuellbetragNeu = 0.00;
            pBeteiligungserhoehungSession.setAuffuellBetragNeu(auffuellbetragNeu);
            pBeteiligungserhoehungSession.setAuffuellBetragNeuDE(CaString.doubleToStringDE(auffuellbetragNeu));

            pBeteiligungserhoehungSession.setAuffuellBetragZuUeberweisen(pBestandSession.getAuffuellBetrag());
            pBeteiligungserhoehungSession.setAuffuellBetragZuUeberweisenString(pBestandSession.getAuffuellBetragDE());

            pBeteiligungserhoehungSession.setBerechneteDatenAnzeigen(true);
            pBeteiligungserhoehungSession.setBerechneteDetailsAnzeigen(true);

            if (pBeteiligungserhoehungSession.getWeitereAnteile() > 0) {
                if (pBeteiligungserhoehungSession.getErhoehungsbetrag() > 50000) {
                    pBeteiligungserhoehungSession.setSignaturArt("0");
                }
                pBeteiligungserhoehungSession.setBeteiligungserhoehungWeiterStep("4");
            } else {
                pBeteiligungserhoehungSession.setBeteiligungserhoehungWeiterStep("2");
            }

        }
    }

    private double round(double value, int places) {
        if (places < 0)
            throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public void doWeiterZuVorschau() {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.P_BETEILIGUNGSERHOEHUNG)) {
            return;
        }

        if (pBeteiligungserhoehungSession.getZahlungsart().equals("1")) {

            if (pBeteiligungserhoehungSession.getSepaKontoAbweichend()) {

                pBeteiligungserhoehungSession.setSepaKontoAbweichendStrasse(CaString
                        .replaceStreetAbbreviations(pBeteiligungserhoehungSession.getSepaKontoAbweichendStrasse()));

                pBeteiligungserhoehungSession.setSepaKontoAbweichendIBAN(
                        pBeteiligungserhoehungSession.getSepaKontoAbweichendIBAN().trim().replace(" ", ""));
                if (pBeteiligungserhoehungSession.getSepaKontoAbweichendAnrede().trim().equals("")
                        || pBeteiligungserhoehungSession.getSepaKontoAbweichendVorname().trim().equals("")
                        || pBeteiligungserhoehungSession.getSepaKontoAbweichendNachname().trim().equals("")
                        || pBeteiligungserhoehungSession.getSepaKontoAbweichendStrasse().trim().equals("")
                        || pBeteiligungserhoehungSession.getSepaKontoAbweichendPLZ().trim().equals("")
                        || pBeteiligungserhoehungSession.getSepaKontoAbweichendOrt().trim().equals("")
                        || pBeteiligungserhoehungSession.getSepaKontoAbweichendBank().trim().equals("")
                        || pBeteiligungserhoehungSession.getSepaKontoAbweichendIBAN().trim().equals("")
                        || pBeteiligungserhoehungSession.getSepaKontoAbweichendBIC().trim().equals("")
                        || pBeteiligungserhoehungSession.getSepaKontoAbweichendLand().trim().equals("")
                        || pBeteiligungserhoehungSession.getSepaKontoAbweichendEmail().trim().equals("")) {

                    tSession.trageFehlerEinMitArt(
                            "Bitte füllen Sie alle mit einem * gekennzeichnteten Pflichtfelder aus.", 1);
                    tSessionVerwaltung.setzeEnde();
                    return;

                }

                pBeteiligungserhoehungSession.setSepaKontoAbweichendIBAN(
                        pBeteiligungserhoehungSession.getSepaKontoAbweichendIBAN().toUpperCase());

                if (!brMBeteiligungserhoehung.checkIBAN(eclLoginDatenM.getEclLoginDaten().loginKennung,
                        pBeteiligungserhoehungSession.getSepaKontoAbweichendIBAN())) {
                    tSession.trageFehlerEinMitArt("Bitte überprüfen Sie die eingegebene IBAN.", 1);
                    tSessionVerwaltung.setzeEnde();
                    return;
                }

            }

        }

        if (pBeteiligungserhoehungSession.getSepaKontoAbweichend()) {
            pBeteiligungserhoehungSession.setSignaturArt("2");
        }

        //pBeteiligungserhoehungSession.setBeteiligungserhoehungStepPrev(pBeteiligungserhoehungSession.getBeteiligungserhoehungStep());
        pBeteiligungserhoehungSession.setBeteiligungserhoehungStep("1");
        tSessionVerwaltung.setzeEnde();
        return;
    }

    public void doAntrag() {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.P_BETEILIGUNGSERHOEHUNG)) {
            return;
        }
        if (pBeteiligungserhoehungSession.getSignaturArt() == null) {
            CaBug.druckeLog("Bitte wählen Sie eine Signaturmethode aus.", logDrucken, 10);
            tSession.trageFehlerEinMitArt("Bitte wählen Sie eine Signaturmethode aus.", 1);
            tSessionVerwaltung.setzeEnde();
            return;
        }

        eclDbM.openAll();
        String uuid = brMBeteiligungserhoehung
                .beteiligunbgserhoehungSpeichern(eclLoginDatenM.getEclLoginDaten().loginKennung);
        if (uuid != null && !uuid.trim().equals("")) {
            brMBeteiligungserhoehung.antragErstellen(eclLoginDatenM.getEclLoginDaten().loginKennung, uuid);
            eclDbM.closeAll();
            pBeteiligungserhoehungSession.setBeteiligungserhoehungStep("success");
        } else {
            CaBug.druckeLog("Antrag konnte nicht erstellt werden.", logDrucken, 10);
            tSession.trageFehlerEinMitArt("Antrag konnte nicht erstellt werden.", 1);
            tSessionVerwaltung.setzeEnde();
            return;
        }
    }

    public void doClear() {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.P_BETEILIGUNGSERHOEHUNG)) {
            return;
        }
        pBeteiligungserhoehungSession.clear();
        tSessionVerwaltung.setzeEnde(KonstPortalView.P_AUSWAHL);
        return;
    }

    public void doWeiter() {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.P_BETEILIGUNGSERHOEHUNG)) {
            return;
        }

        if (!eclParamM.getParam().paramPortal.liefereku178SepaIstAktiv()) {
            pBeteiligungserhoehungSession.setBeteiligungserhoehungStep("1");
            tSessionVerwaltung.setzeEnde();
            return;
        } else {

            pBeteiligungserhoehungSession
                    .setBeteiligungserhoehungStepPrev(pBeteiligungserhoehungSession.getBeteiligungserhoehungStep());
            pBeteiligungserhoehungSession
                    .setBeteiligungserhoehungStep(pBeteiligungserhoehungSession.getBeteiligungserhoehungWeiterStep());
            tSessionVerwaltung.setzeEnde();
            return;
        }
    }

    public void doStepBack() {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.P_BETEILIGUNGSERHOEHUNG)) {
            return;
        }
        pBeteiligungserhoehungSession
                .setBeteiligungserhoehungStep(pBeteiligungserhoehungSession.getBeteiligungserhoehungStepPrev());
        tSessionVerwaltung.setzeEnde();
        return;
    }

    public void doDownload() {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.P_BETEILIGUNGSERHOEHUNG)) {
            return;
        }
        RpBrowserAnzeigen rpBrowserAnzeigen = new RpBrowserAnzeigen();
        rpBrowserAnzeigen.zeigen(pBeteiligungserhoehungSession.getPdf(), "Beteiligungserhoehung.pdf");
        tSessionVerwaltung.setzeEnde();
        return;

    }

    public void doPersoenlicheDatenAendern() {
        tSessionVerwaltung.setzeEnde(KonstPortalView.P_AKTIONAERSDATEN);
    }

    public void doSetBankSepaAbw() {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.P_BETEILIGUNGSERHOEHUNG)) {
            return;
        }
        String blz = "";

        pBeteiligungserhoehungSession
                .setSepaKontoAbweichendIBAN(pBeteiligungserhoehungSession.getSepaKontoAbweichendIBAN().toUpperCase());
        String ibanEingabe = CaBank.ibanZuIntern(pBeteiligungserhoehungSession.getSepaKontoAbweichendIBAN());
        if (ibanEingabe.length() == 22) {

            blz = ibanEingabe.substring(4, 12);

            eclDbM.openAll();
            eclDbM.openWeitere();

            eclDbM.getDbBundle().dbBlzBank.mandantenabhaengig = true;
            eclDbM.getDbBundle().dbBlzBank.readBlz(blz);
            if (eclDbM.getDbBundle().dbBlzBank.anzErgebnis() > 0) {
                pBeteiligungserhoehungSession
                        .setSepaKontoAbweichendBank(eclDbM.getDbBundle().dbBlzBank.ergebnisPosition(0).bankname);
                ;
                pBeteiligungserhoehungSession
                        .setSepaKontoAbweichendBIC(eclDbM.getDbBundle().dbBlzBank.ergebnisPosition(0).bic);
            } else {
                pBeteiligungserhoehungSession.setSepaKontoAbweichendBank("");
                pBeteiligungserhoehungSession.setSepaKontoAbweichendBIC("");

            }
            eclDbM.getDbBundle().dbBlzBank.mandantenabhaengig = false;

            eclDbM.closeAll();
        }
    }
    
    public void doSetBankSelf() {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.P_BETEILIGUNGSERHOEHUNG)) {
            return;
        }
        String blz = "";

        pBeteiligungserhoehungSession
                .setSepaKontoSelfTempIBAN(pBeteiligungserhoehungSession.getSepaKontoSelfTempIBAN().toUpperCase());
        String ibanEingabe = CaBank.ibanZuIntern(pBeteiligungserhoehungSession.getSepaKontoSelfTempIBAN());
        pBeteiligungserhoehungSession.setSepaKontoSelfTempVorname(pAktionaersdatenSession.getAktionaerVorname());
        pBeteiligungserhoehungSession.setSepaKontoSelfTempNachname(pAktionaersdatenSession.getAktionaerNachname());
        if (ibanEingabe.length() == 22) {

            blz = ibanEingabe.substring(4, 12);

            eclDbM.openAll();
            eclDbM.openWeitere();

            eclDbM.getDbBundle().dbBlzBank.mandantenabhaengig = true;
            eclDbM.getDbBundle().dbBlzBank.readBlz(blz);
            if (eclDbM.getDbBundle().dbBlzBank.anzErgebnis() > 0) {
                pBeteiligungserhoehungSession
                        .setSepaKontoSelfTempBank(eclDbM.getDbBundle().dbBlzBank.ergebnisPosition(0).bankname);
                ;
                pBeteiligungserhoehungSession
                        .setSepaKontoSelfTempBIC(eclDbM.getDbBundle().dbBlzBank.ergebnisPosition(0).bic);
            } else {
                pBeteiligungserhoehungSession.setSepaKontoSelfTempBank("");
                pBeteiligungserhoehungSession.setSepaKontoSelfTempBIC("");

            }
            eclDbM.getDbBundle().dbBlzBank.mandantenabhaengig = false;

            eclDbM.closeAll();
        }
    }

    public Boolean checkWeiterBtnSepa() {

        if (pBeteiligungserhoehungSession.getZahlungsart().equals("1")) {
            if (pAktionaersdatenSession.isBankverbindungFehlt() 
                    && (pBeteiligungserhoehungSession.getSepaKontoSelfTempIBAN().equals("")
                            || pBeteiligungserhoehungSession.getSepaKontoSelfTempBank().equals("")
                            || pBeteiligungserhoehungSession.getSepaKontoSelfTempBank().equals(""))) {
                return false;
            }
            if (!pBeteiligungserhoehungSession.getSepaEinzugErmaechtigung()) {

                return false;
            }
        }
        return true;
    }

    public Boolean checkAntragErstellenBtn() {

        if (pBeteiligungserhoehungSession.getZahlungsart().equals("1")
                && pBeteiligungserhoehungSession.getSignaturArt().equals("1")
                && !pBeteiligungserhoehungSession.getEinwilligungBeteiligung()) {
            return false;
        }

        return true;
    }

    public String showIban(String iban) {

        return CaString.formatIban(iban);

    }

    public boolean pruefeObKind() {

        return (eclLoginDatenM
                .getEclAktienregisterErgaenzung().ergaenzungLangString[KonstAktienregisterErgaenzung.ku178_Gruppe]
                        .equals("3"));

    }

    public String weitereAnteileStringUebersicht() {

        String anteil = "Anteil";
        if (pBeteiligungserhoehungSession.getWeitereAnteile() > 1) {
            anteil = "Anteile";
        }
        String euro = String.valueOf(pBeteiligungserhoehungSession.getWeitereAnteile() * 50) + ",00 Euro";

        String s = pBeteiligungserhoehungSession.getWeitereAnteileString() + " " + anteil + " á 50,00 Euro = " + euro;

        return s;
    }

    public String anredeString() {
        switch (pBeteiligungserhoehungSession.getSepaKontoAbweichendAnrede()) {
        case "1":
            return "Herr";
        case "2":
            return "Frau";
        case "3":
            return "divers";
        case "4":
            return "Firma";
        default:
            return "";
        }
    }
}
