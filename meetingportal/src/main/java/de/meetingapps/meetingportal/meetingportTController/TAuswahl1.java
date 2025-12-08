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

import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComBl.BlVeranstaltungen;
import de.meetingapps.meetingportal.meetComEclM.EclBesitzGesamtAuswahl1M;
import de.meetingapps.meetingportal.meetComEclM.EclDbM;
import de.meetingapps.meetingportal.meetComEclM.EclLoginDatenM;
import de.meetingapps.meetingportal.meetComEclM.EclParamM;
import de.meetingapps.meetingportal.meetComEclM.EclVeranstaltungenM;
import de.meetingapps.meetingportal.meetComKonst.KonstPortalFunktionen;
import de.meetingapps.meetingportal.meetComKonst.KonstPortalUnterlagen;
import de.meetingapps.meetingportal.meetComKonst.KonstPortalView;
import de.meetingapps.meetingportal.meetingportTFunktionen.TFunktionen;
import de.meetingapps.meetingportal.meetingportTFunktionen.TPortalFunktionen;
import de.meetingapps.meetingportal.meetingportTFunktionen.TPruefeStartNachOpen;
import de.meetingapps.meetingportal.meetingportTFunktionen.TSessionVerwaltung;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@RequestScoped
@Named
public class TAuswahl1 {

//    private int logDrucken = 3;

    @Inject
    private EclDbM eclDbM;
    @Inject
    private EclParamM eclParamM;

    private @Inject TSessionVerwaltung tSessionVerwaltung;
    private @Inject TAuswahl1Session tAuswahl1Session;
    private @Inject TEinstellungen tEinstellungen;
    private @Inject TSession tSession;
    private @Inject TPortalFunktionen tPortalFunktionen;
    private @Inject TGeneralversammlung tGeneralversammlung;
    private @Inject TAuswahl1Teilnahme tAuswahl1Teilnahme;
    private @Inject TDialogveranstaltungen tDialogveranstaltungen;
    private @Inject EclLoginDatenM eclLoginDatenM;
    private @Inject TUnterlagen tUnterlagen;
    private @Inject TPruefeStartNachOpen tPruefeStartNachOpen;
    private @Inject TFunktionen tFunktionen;
    private @Inject EclBesitzGesamtAuswahl1M eclBesitzGesamtAuswahl1M;
    private @Inject EclVeranstaltungenM eclVeranstaltungenM;

    public boolean auswahlAnzeigen(String pSNr) {
        int pNr = Integer.parseInt(pSNr);
        if (Math.abs(eclParamM.getParam().paramPortal.lfdHVDialogVeranstaltungenInMenue) == pNr
                && eclLoginDatenM.liefereKennungArt() == 1 && eclBesitzGesamtAuswahl1M.liefereDialogAnmeldungZulaessigFuerMeldung()) {
            return true;
        }
        if (Math.abs(eclParamM.getParam().paramPortal.lfdHVBeiratswahlInMenue) == pNr
                && eclLoginDatenM.liefereKennungArt() == 1) {
            return true;
        }
       if (Math.abs(eclParamM.getParam().paramPortal.lfdHVGeneralversammlungInMenue) == pNr
               && eclBesitzGesamtAuswahl1M.liefereFreiwilligeAnmeldungZulaessigFuerMeldung()) {
            return true;
        }
        if (Math.abs(eclParamM.getParam().paramPortal.lfdHVGeneralversammlungBriefwahlInMenue) == pNr) {
            return true;
        }
        if (Math.abs(eclParamM.getParam().paramPortal.lfdHVGeneralversammlungTeilnahmeInMenue) == pNr
                ) {
            tPortalFunktionen.bereiteVorPruefungEinzelnePortalFunktion(KonstPortalFunktionen.onlineteilnahme);
            boolean angebotenUndBerechtigt=tPortalFunktionen.angebotenUndBerechtigtPortalFunktion(KonstPortalFunktionen.onlineteilnahme);
            if (angebotenUndBerechtigt==true) {
               return true;
            }
        }
        if (Math.abs(eclParamM.getParam().paramPortal.lfdHVUnterlagenInMenue) == pNr) {
            return true;
        }
        if (Math.abs(eclParamM.getParam().paramPortal.lfdHVEinstellungenInMenue) == pNr) {
            return true;
        }
//        if (Math.abs(eclParamM.getParam().paramPortal.lfdMitgliederDatenInMenue) == pNr
//                && eclLoginDatenM.liefereKennungArt() == 1) {
//            return true;
//        }
        if (Math.abs(eclParamM.getParam().paramPortal.lfdHVGeneralversammlungTeilnahmeGast) == pNr) {
            return true;
        }
        return false;
    }

    public String auswahlStart(String pSNr) {
        int pNr = Integer.parseInt(pSNr);
        if (eclLoginDatenM.liefereKennungArt() == 1) {
            if (Math.abs(eclParamM.getParam().paramPortal.lfdHVDialogVeranstaltungenInMenue) == pNr) {
                return "636";
            }
            if (Math.abs(eclParamM.getParam().paramPortal.lfdHVBeiratswahlInMenue) == pNr) {
                return "1960";
            }
           if (Math.abs(eclParamM.getParam().paramPortal.lfdHVGeneralversammlungInMenue) == pNr) {
                return "639";
            }
           if (Math.abs(eclParamM.getParam().paramPortal.lfdHVGeneralversammlungBriefwahlInMenue) == pNr) {
               return "1961";
           }
            if (Math.abs(eclParamM.getParam().paramPortal.lfdHVGeneralversammlungTeilnahmeInMenue) == pNr) {
                return "958";
            }
            if (Math.abs(eclParamM.getParam().paramPortal.lfdHVUnterlagenInMenue) == pNr) {
                return "642";
            }
            if (Math.abs(eclParamM.getParam().paramPortal.lfdHVEinstellungenInMenue) == pNr) {
                return "645";
            }
//            if (Math.abs(eclParamM.getParam().paramPortal.lfdMitgliederDatenInMenue) == pNr) {
//                return "648";
//            }
            if (Math.abs(eclParamM.getParam().paramPortal.lfdHVGeneralversammlungTeilnahmeGast) == pNr) {
                return "1106";
            }
        }
        if (eclLoginDatenM.liefereKennungArt() == 2) {
            /*Bei Nicht-Mitglied nicht möglich*/
//            if (Math.abs(eclParamM.getParam().paramPortal.lfdHVDialogVeranstaltungenInMenue) == pNr) {
//                return "1032";
//            }
            if (Math.abs(eclParamM.getParam().paramPortal.lfdHVGeneralversammlungInMenue) == pNr) {
                return "1035";
            }
            if (Math.abs(eclParamM.getParam().paramPortal.lfdHVGeneralversammlungBriefwahlInMenue) == pNr) {
                return "1962";
            }
           if (Math.abs(eclParamM.getParam().paramPortal.lfdHVGeneralversammlungTeilnahmeInMenue) == pNr) {
                return "1038";
            }
            if (Math.abs(eclParamM.getParam().paramPortal.lfdHVUnterlagenInMenue) == pNr) {
                return "1041";
            }
            if (Math.abs(eclParamM.getParam().paramPortal.lfdHVEinstellungenInMenue) == pNr) {
                return "1044";
            }
            if (Math.abs(eclParamM.getParam().paramPortal.lfdHVGeneralversammlungTeilnahmeGast) == pNr) {
                return "1107";
            }
        }
        return "";
    }

    public String auswahlButton(String pSNr) {
        int pNr = Integer.parseInt(pSNr);
        if (eclLoginDatenM.liefereKennungArt() == 1) {
            if (Math.abs(eclParamM.getParam().paramPortal.lfdHVDialogVeranstaltungenInMenue) == pNr) {
                return "637";
            }
            if (Math.abs(eclParamM.getParam().paramPortal.lfdHVBeiratswahlInMenue) == pNr) {
                return "1963";
            }
            if (Math.abs(eclParamM.getParam().paramPortal.lfdHVGeneralversammlungInMenue) == pNr) {
                return "640";
            }
            if (Math.abs(eclParamM.getParam().paramPortal.lfdHVGeneralversammlungBriefwahlInMenue) == pNr) {
                return "1964";
            }
            if (Math.abs(eclParamM.getParam().paramPortal.lfdHVGeneralversammlungTeilnahmeInMenue) == pNr) {
                return "959";
            }
            if (Math.abs(eclParamM.getParam().paramPortal.lfdHVUnterlagenInMenue) == pNr) {
                return "643";
            }
            if (Math.abs(eclParamM.getParam().paramPortal.lfdHVEinstellungenInMenue) == pNr) {
                return "646";
            }
//            if (Math.abs(eclParamM.getParam().paramPortal.lfdMitgliederDatenInMenue) == pNr) {
//                return "649";
//            }
            if (Math.abs(eclParamM.getParam().paramPortal.lfdHVGeneralversammlungTeilnahmeGast) == pNr) {
                return "1108";
            }
        }
        if (eclLoginDatenM.liefereKennungArt() == 2) {
            /*Für Gast nicht zulässig*/
//            if (Math.abs(eclParamM.getParam().paramPortal.lfdHVBeiratswahlInMenue) == pNr) {
//                return "1033";
//            }
            if (Math.abs(eclParamM.getParam().paramPortal.lfdHVGeneralversammlungInMenue) == pNr) {
                return "1036";
            }
            if (Math.abs(eclParamM.getParam().paramPortal.lfdHVGeneralversammlungBriefwahlInMenue) == pNr) {
                return "1965";
            }
             if (Math.abs(eclParamM.getParam().paramPortal.lfdHVGeneralversammlungTeilnahmeInMenue) == pNr) {
                return "1039";
            }
            if (Math.abs(eclParamM.getParam().paramPortal.lfdHVUnterlagenInMenue) == pNr) {
                return "1042";
            }
            if (Math.abs(eclParamM.getParam().paramPortal.lfdHVEinstellungenInMenue) == pNr) {
                return "1045";
            }
            if (Math.abs(eclParamM.getParam().paramPortal.lfdHVGeneralversammlungTeilnahmeGast) == pNr) {
                return "1109";
            }
        }
        return "";
    }

    public String auswahlEnde(String pSNr) {
        int pNr = Integer.parseInt(pSNr);
        if (eclLoginDatenM.liefereKennungArt() == 1) {
            if (Math.abs(eclParamM.getParam().paramPortal.lfdHVDialogVeranstaltungenInMenue) == pNr) {
                return "638";
            }
            if (Math.abs(eclParamM.getParam().paramPortal.lfdHVBeiratswahlInMenue) == pNr) {
                return "1966";
            }
            if (Math.abs(eclParamM.getParam().paramPortal.lfdHVGeneralversammlungInMenue) == pNr) {
                return "641";
            }
            if (Math.abs(eclParamM.getParam().paramPortal.lfdHVGeneralversammlungBriefwahlInMenue) == pNr) {
                return "1967";
            }
            if (Math.abs(eclParamM.getParam().paramPortal.lfdHVGeneralversammlungTeilnahmeInMenue) == pNr) {
                return "960";
            }
            if (Math.abs(eclParamM.getParam().paramPortal.lfdHVUnterlagenInMenue) == pNr) {
                return "644";
            }
            if (Math.abs(eclParamM.getParam().paramPortal.lfdHVEinstellungenInMenue) == pNr) {
                return "647";
            }
//            if (Math.abs(eclParamM.getParam().paramPortal.lfdMitgliederDatenInMenue) == pNr) {
//                return "650";
//            }
            if (Math.abs(eclParamM.getParam().paramPortal.lfdHVGeneralversammlungTeilnahmeGast) == pNr) {
                return "1110";
            }
        }
        if (eclLoginDatenM.liefereKennungArt() == 2) {
//            if (Math.abs(eclParamM.getParam().paramPortal.lfdHVBeiratswahlInMenue) == pNr) {
//                return "1034";
//            }
            if (Math.abs(eclParamM.getParam().paramPortal.lfdHVGeneralversammlungInMenue) == pNr) {
                return "1037";
            }
            if (Math.abs(eclParamM.getParam().paramPortal.lfdHVGeneralversammlungBriefwahlInMenue) == pNr) {
                return "1968";
            }
            if (Math.abs(eclParamM.getParam().paramPortal.lfdHVGeneralversammlungTeilnahmeInMenue) == pNr) {
                return "1040";
            }
            if (Math.abs(eclParamM.getParam().paramPortal.lfdHVUnterlagenInMenue) == pNr) {
                return "1043";
            }
            if (Math.abs(eclParamM.getParam().paramPortal.lfdHVEinstellungenInMenue) == pNr) {
                return "1046";
            }
            if (Math.abs(eclParamM.getParam().paramPortal.lfdHVGeneralversammlungTeilnahmeGast) == pNr) {
                return "1111";
            }
        }
        return "";
    }

    public void doButton(String pSNr) {
        int pNr = Integer.parseInt(pSNr);
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.AUSWAHL1)) {
            return;
        }
        eclDbM.openAll();

        if (!tPruefeStartNachOpen.pruefeStartNachOpen(0)) {
            tSessionVerwaltung.setzeEnde();
            eclDbM.closeAll();
            return;
        }
        tPortalFunktionen.belegePortalFunktionenWillenserklaerungenStatusAktiv();

        //		if (Math.abs(eclParamM.getParam().paramPortal.lfdHVBeiratswahlInMenue)==pNr) {
        //			aControllerBeiratswahl.init();
        //			return aFunktionen.setzeEnde("aAuswahl1Beiratswahl", true, true);
        //		}
        if (Math.abs(eclParamM.getParam().paramPortal.lfdHVDialogVeranstaltungenInMenue) == pNr) {
            belegeAktiveModule();
            tDialogveranstaltungen.init(false);
            eclDbM.closeAll();
            tSessionVerwaltung.setzeEnde(KonstPortalView.AUSWAHL1_DIALOGVERANSTALTUNGEN);
            return;
        }
        if (Math.abs(eclParamM.getParam().paramPortal.lfdHVBeiratswahlInMenue) == pNr) {
            belegeAktiveModule();
            tGeneralversammlung.init();
            eclDbM.closeAll();
            tSessionVerwaltung.setzeEnde(KonstPortalView.AUSWAHL1_GENERALVERSAMMLUNG_BEIRATSWAHL);
            return;
        }
       if (Math.abs(eclParamM.getParam().paramPortal.lfdHVGeneralversammlungInMenue) == pNr) {
            belegeAktiveModule();
            tGeneralversammlung.init();
            /**Nun Zusatzveranstaltung aktivieren*/
            if (eclBesitzGesamtAuswahl1M.isAnOderAbgemeldet()==false) {
                /*Noch nicht angemeldet - also sozusagen Erstinitialisierung*/
                BlVeranstaltungen blVeranstaltungen=new BlVeranstaltungen(true, eclDbM.getDbBundle());
                blVeranstaltungen.erzeugeVeranstaltungslisteFuerTeilnehmer(BlVeranstaltungen.LAUT_MENUE_NUMMER, -1);
                eclVeranstaltungenM.copyFromBlVeranstaltungen(blVeranstaltungen);
            }
            else {
                /*Quittung "Vorher" anzeigen*/
                BlVeranstaltungen blVeranstaltungen=new BlVeranstaltungen(true, eclDbM.getDbBundle());
                blVeranstaltungen.erzeugeVeranstaltungslisteFuerTeilnehmer(BlVeranstaltungen.LAUT_MENUE_NUMMER, -1);
                eclVeranstaltungenM.copyFromBlVeranstaltungen(blVeranstaltungen);
                blVeranstaltungen.loginKennung=eclLoginDatenM.getEclLoginDaten().loginKennung;
                blVeranstaltungen.belegeWerteVeranstaltungsliste();
                
                /**Prüfe muß durchgeführt werden, damit inLetzterVerarbeitungEnthalten richtig gesetzt wird!*/
                blVeranstaltungen.pruefeVeranstaltungsliste(false);
                
                blVeranstaltungen.rcQuittungsArt=1;
                blVeranstaltungen.aufbereitenQuittung();
                eclVeranstaltungenM.setVeranstaltungenQuittungListe(blVeranstaltungen.rcVeranstaltungenQuittungListe);
            }
            eclDbM.closeAll();
            if (tAuswahl1Session.isGeneralversammlungMoeglich() && eclBesitzGesamtAuswahl1M.isEigenerAREintragVorhanden()==true && eclBesitzGesamtAuswahl1M.isAnOderAbgemeldet()==false) {
                /*In diesem Fall: Seite An-/Abmelden direkt anspringen*/
                tSessionVerwaltung.setzeEnde(KonstPortalView.GENERALVERSAMMLUNG_ANABMELDEN);
           }
            else {
                tSessionVerwaltung.setzeEnde(KonstPortalView.AUSWAHL1_GENERALVERSAMMLUNG);
            }
            return;
        }
       if (Math.abs(eclParamM.getParam().paramPortal.lfdHVGeneralversammlungBriefwahlInMenue) == pNr) {
           belegeAktiveModule();
           tGeneralversammlung.init();
           eclDbM.closeAll();
           tSessionVerwaltung.setzeEnde(KonstPortalView.AUSWAHL1_GENERALVERSAMMLUNG_BRIEFWAHL);
           return;
       }
        if (Math.abs(eclParamM.getParam().paramPortal.lfdHVGeneralversammlungTeilnahmeInMenue) == pNr) {
            belegeAktiveModule();
            tAuswahl1Teilnahme.initAusMenue();
            eclDbM.closeAll();
            tSessionVerwaltung.setzeEnde(KonstPortalView.AUSWAHL1_TEILNAHME);
            return;
        }
        if (Math.abs(eclParamM.getParam().paramPortal.lfdHVGeneralversammlungTeilnahmeGast) == pNr) {
            belegeAktiveModule();
            tAuswahl1Teilnahme.initAusMenue();
            eclDbM.closeAll();
            tSessionVerwaltung.setzeEnde(KonstPortalView.AUSWAHL1_TEILNAHMEGAST);
            return;
        }
        if (Math.abs(eclParamM.getParam().paramPortal.lfdHVUnterlagenInMenue) == pNr) {
            belegeAktiveModule();
            tSession.setRueckkehrZuMenue(KonstPortalView.AUSWAHL1);

            if (!tPruefeStartNachOpen.pruefeStartNachOpen(KonstPortalView.AUSWAHL1)) {
                eclDbM.closeAll();
                return;
            }
            if (tPortalFunktionen.aktivUnterlagen() == false) {
                tSession.trageFehlerEin(CaFehler.afFunktionDerzeitNichtAktiv);
                eclDbM.closeAll();
                return;
            }

            tUnterlagen.init(KonstPortalUnterlagen.ANZEIGEN_UNTERLAGEN, 0);

            eclDbM.closeAll();

            tSessionVerwaltung.setzeEnde(KonstPortalView.UNTERLAGEN);
            return;
        }
        if (Math.abs(eclParamM.getParam().paramPortal.lfdHVEinstellungenInMenue) == pNr) {
            int rc = tEinstellungen.startRegistrierungAusAuswahl();
            eclDbM.closeAll();
            if (rc < 0) {
                switch (rc) {
                case CaFehler.afFunktionNichtAuswaehlbar:
                    tSession.trageFehlerEin(rc);
                    tSessionVerwaltung.setzeEnde(KonstPortalView.AUSWAHL1);
                    return;
                default:
                    tSession.trageFehlerEin(rc);
                    tSessionVerwaltung.setzeEnde(KonstPortalView.fehlerSysLogout);
                    return;
                }
            }
            tSessionVerwaltung.setzeEnde(KonstPortalView.EINSTELLUNGEN);
            return;
        }
//        if (Math.abs(eclParamM.getParam().paramPortal.lfdMitgliederDatenInMenue) == pNr) {
//            /*Frei - derzeit nicht verwendet!*/
//            belegeAktiveModule();
//            eclDbM.closeAll();
//            tDatenpflege.init();
//            tSessionVerwaltung.setzeEnde(KonstPortalView.DATENPFLEGE);
//            return;
//        }
        return;
    }

    
    public void doAnmeldenAusGVTeilnahme() {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.AUSWAHL1_TEILNAHME)) {
            return;
        }
        eclDbM.openAll();

        if (!tPruefeStartNachOpen.pruefeStartNachOpen(0)) {
            tSessionVerwaltung.setzeEnde();
            eclDbM.closeAll();
            return;
        }
        tPortalFunktionen.belegePortalFunktionenWillenserklaerungenStatusAktiv();

        belegeAktiveModule();
        tGeneralversammlung.init();
        eclDbM.closeAll();
        tSessionVerwaltung.setzeEnde(KonstPortalView.AUSWAHL1_GENERALVERSAMMLUNG);
        return;
       
    }
    
    public void belegeAktiveModule() {
        tAuswahl1Session.setDialogveranstaltungenAktiv(false);
        tAuswahl1Session.setBeiratswahlAktiv(false);
        tAuswahl1Session.setGeneralversammlungAktiv(false);
        tAuswahl1Session.setGeneralversammlungBriefwahlAktiv(false);
        tAuswahl1Session.setGeneralversammlungTeilnahmeAktiv(false);
        tAuswahl1Session.setGeneralversammlungTeilnahmeGastAktiv(false);
        tAuswahl1Session.setUnterlagenAktiv(false);
        tAuswahl1Session.setAdressaenderungAktiv(false);

        //		tAuswahl1Session.setBeiratswahlMoeglich(false);
        //		tAuswahl1Session.setDialogveranstaltungenMoeglich(false);
        //		tAuswahl1Session.setGeneralversammlungMoeglich(false);
        //		tAuswahl1Session.setGeneralversammlungTeilnahmeMoeglich(false);
        //		tAuswahl1Session.setAdressaenderungMoeglich(false);

        if (eclParamM.getParam().paramPortal.dialogveranstaltungAktiv != 0) {
            tAuswahl1Session.setDialogveranstaltungenAktiv(true);
            if (eclParamM.getParam().paramPortal.lfdHVDialogVeranstaltungenInMenue > 0) {
                tAuswahl1Session.setDialogveranstaltungenMoeglich(true);
            }
        }
        if (eclParamM.getParam().paramPortal.lfdHVBeiratswahlInMenue != 0) {
            tAuswahl1Session.setBeiratswahlAktiv(true);
            if (eclParamM.getParam().paramPortal.lfdHVBeiratswahlInMenue > 0) {
                tAuswahl1Session.setBeiratswahlMoeglich(true);
            }
        }
        if (eclParamM.getParam().paramPortal.lfdHVGeneralversammlungInMenue != 0) {
            tAuswahl1Session.setGeneralversammlungAktiv(true);
            if (eclParamM.getParam().paramPortal.lfdHVGeneralversammlungInMenue > 0) {
                tAuswahl1Session.setGeneralversammlungMoeglich(true);
            }
        }
        if (eclParamM.getParam().paramPortal.lfdHVGeneralversammlungBriefwahlInMenue != 0) {
            tAuswahl1Session.setGeneralversammlungBriefwahlAktiv(true);
            if (eclParamM.getParam().paramPortal.lfdHVGeneralversammlungBriefwahlInMenue > 0) {
                tAuswahl1Session.setGeneralversammlungBriefwahlMoeglich(true);
            }
        }
        if (eclParamM.getParam().paramPortal.lfdHVGeneralversammlungTeilnahmeInMenue != 0) {
            tAuswahl1Session.setGeneralversammlungTeilnahmeAktiv(true);
            if (eclParamM.getParam().paramPortal.lfdHVGeneralversammlungTeilnahmeInMenue > 0) {
                tAuswahl1Session.setGeneralversammlungTeilnahmeMoeglich(true);
            }
        }
        if (eclParamM.getParam().paramPortal.lfdHVGeneralversammlungTeilnahmeGast != 0) {
            tAuswahl1Session.setGeneralversammlungTeilnahmeGastAktiv(true);
            if (eclParamM.getParam().paramPortal.lfdHVGeneralversammlungTeilnahmeGast > 0) {
                tAuswahl1Session.setGeneralversammlungTeilnahmeGastMoeglich(true);
            }
        }
        if (eclParamM.getParam().paramPortal.lfdHVUnterlagenInMenue != 0) {
            tAuswahl1Session.setUnterlagenAktiv(true);
            if (eclParamM.getParam().paramPortal.lfdHVUnterlagenInMenue > 0) {
                tAuswahl1Session.setUnterlagenMoeglich(true);
            }
        }
//        CaBug.druckeLog("eclParamM.getParam().paramPortal.lfdMitgliederDatenInMenue="
//                + eclParamM.getParam().paramPortal.lfdMitgliederDatenInMenue, logDrucken, 5);
//        if (eclParamM.getParam().paramPortal.lfdMitgliederDatenInMenue != 0) {
//            tAuswahl1Session.setAdressaenderungAktiv(true);
//            if (eclParamM.getParam().paramPortal.lfdMitgliederDatenInMenue > 0) {
//                tAuswahl1Session.setAdressaenderungMoeglich(true);
//            }
//        }
    }

    //	public String doBriefwahlErteilen() {
    //		
    //		if (!aFunktionen.pruefeStart("aAuswahl1")){return "aDlgFehler";}
    //	
    //		aDlgVariablen.setAusgewaehlteAktion("5");
    //		aDlgVariablen.setAusgewaehlteHauptAktion("2");
    //		
    //		aDlgVariablen.setZielOeffentlicheID("");
    //		aDlgVariablen.setUeberOeffentlicheID(false);
    //
    //		/*Bereitstellen der Willenserklärungsdaten für Oberfläche*/
    //		eclZugeordneteMeldungM.copyFromMOhneStorno(eclZugeordneteMeldungListeM.getZugeordneteMeldungenEigeneAktienListeM().get(0));
    //		
    //		
    //		eclDbM.openAll();
    //		aFunktionen.leseAbstimmungsliste(eclDbM.getDbBundle(), eclZugeordneteMeldungM.getGattung(), KonstWeisungserfassungSicht.portalWeisungserfassung, KonstSkIst.briefwahl);
    //		aControllerKIAVAuswahl.leseAbstimmungsvorschlagGesellschaft(eclDbM.getDbBundle());
    //		eclDbM.closeAll();
    //		return aFunktionen.setzeEnde("aWeisung", true, true);
    //
    //
    //	}
    //	
    //	public String doBriefwahlAendern() {
    //		if (!aFunktionen.pruefeStart("aAuswahl1")){return "aDlgFehler";}
    //		return belegeMeldungUndWillenserklaerung(1);
    //	}
    //
    //	public String doBriefwahlStornieren() {
    //		if (!aFunktionen.pruefeStart("aAuswahl1")){return "aDlgFehler";}
    //		return belegeMeldungUndWillenserklaerung(2);
    //	}
    //
    //	private String belegeMeldungUndWillenserklaerung(int aendernOderStornieren) {
    //		
    //		EclZugeordneteMeldungM pZugeordneteMeldungM=null;
    //		EclWillenserklaerungStatusM pWillenserklaerungStatusM=null;
    //		
    //		int gef=-1;
    //		for (int i=0;i<eclZugeordneteMeldungListeM.getZugeordneteMeldungenEigeneAktienListeM().size();i++) {
    //			pZugeordneteMeldungM=eclZugeordneteMeldungListeM.getZugeordneteMeldungenEigeneAktienListeM().get(i);
    //			for (int i1=0;i1<pZugeordneteMeldungM.getZugeordneteWillenserklaerungenListM().size();i1++) {
    //				pWillenserklaerungStatusM=eclZugeordneteMeldungListeM.getZugeordneteMeldungenEigeneAktienListeM().get(i).getZugeordneteWillenserklaerungenListM().get(i1);
    //				if (pWillenserklaerungStatusM.getWillenserklaerung()==KonstWillenserklaerung.briefwahl || pWillenserklaerungStatusM.getWillenserklaerung()==KonstWillenserklaerung.aendernBriefwahl) {
    //					gef=1;
    //				}
    //				
    //			}
    //			
    //		}
    //		if (gef!=1) {CaBug.drucke("AControllerAuswahl1.belegeMeldungUndWillenserklaerung 001");}
    //		if (aendernOderStornieren==1) {
    //			return aControllerStatus.aendernAusfuehren(pZugeordneteMeldungM, pWillenserklaerungStatusM, KonstWeisungserfassungSicht.portalWeisungserfassung);
    //		}
    //		else {
    //			return aControllerStatus.stornierenAusfuehren(pZugeordneteMeldungM, pWillenserklaerungStatusM, KonstWeisungserfassungSicht.portalWeisungserfassung);
    //		}
    //	}
    //	
    //	public String doAnmeldenAbmelden() {
    //		
    //		if (!aFunktionen.pruefeStart("aAuswahl1")){return "aDlgFehler";}
    //	
    //		return aFunktionen.setzeEnde("aAnmeldenAbmelden", true, true);
    //	}
    //
    //	
    //	/*************************aAuswahl1Beiratswahl*************************************/
    //	
    //	public String doBeiratswahlZurueck() {
    //		if (!aFunktionen.pruefeStart("aAuswahl1Beiratswahl")){return "aDlgFehler";}
    //		
    //		return aFunktionen.setzeEnde("aAuswahl1", true, true);
    //		
    //	}
    //	
    //	/*************************aAuswahl1Generalversammlung*************************************/
    public void doGeneralversammlungZurueck() {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.AUSWAHL1_GENERALVERSAMMLUNG)) {
            return;
        }
        eclDbM.openAll();
        tFunktionen.leseStatusPortal(eclDbM.getDbBundle());
        eclDbM.closeAll();

        tSessionVerwaltung.setzeEnde(KonstPortalView.AUSWAHL1);
        return;
    }
    	
    
    public void doGeneralversammlungBriefwahlZurueck() {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.AUSWAHL1_GENERALVERSAMMLUNG_BRIEFWAHL)) {
            return;
        }
        eclDbM.openAll();
        tFunktionen.leseStatusPortal(eclDbM.getDbBundle());
        eclDbM.closeAll();

        tSessionVerwaltung.setzeEnde(KonstPortalView.AUSWAHL1);
        return;
    }
    
    public void doGeneralversammlungBeiratswahlZurueck() {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.AUSWAHL1_GENERALVERSAMMLUNG_BEIRATSWAHL)) {
            return;
        }
        eclDbM.openAll();
        tFunktionen.leseStatusPortal(eclDbM.getDbBundle());
        eclDbM.closeAll();

        tSessionVerwaltung.setzeEnde(KonstPortalView.AUSWAHL1);
        return;
    }

 	/*************************aAuswahl1Dialogveranstaltungen*************************************/
    	
    	public void doDialogveranstaltungenZurueck() {
            if (!tSessionVerwaltung.pruefeStart(KonstPortalView.AUSWAHL1_DIALOGVERANSTALTUNGEN)) {
                return;
            }
    		
            tSessionVerwaltung.setzeEnde(KonstPortalView.AUSWAHL1);
            return;
    	}
    	
    	

}
