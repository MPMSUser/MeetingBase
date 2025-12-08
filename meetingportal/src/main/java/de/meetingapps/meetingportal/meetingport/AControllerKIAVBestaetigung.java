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

import java.util.List;

import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComBl.BlWillenserklaerung;
import de.meetingapps.meetingportal.meetComEclM.EclAbstimmungM;
import de.meetingapps.meetingportal.meetComEclM.EclAbstimmungenListeM;
import de.meetingapps.meetingportal.meetComEclM.EclDbM;
import de.meetingapps.meetingportal.meetComEclM.EclKIAVM;
import de.meetingapps.meetingportal.meetComEclM.EclParamM;
import de.meetingapps.meetingportal.meetComEclM.EclPortalTexteM;
import de.meetingapps.meetingportal.meetComEclM.EclTeilnehmerLoginM;
import de.meetingapps.meetingportal.meetComEclM.EclZugeordneteMeldungM;
import de.meetingapps.meetingportal.meetComEntities.EclAktienregister;
import de.meetingapps.meetingportal.meetComEntities.EclWeisungMeldung;
import de.meetingapps.meetingportal.meetComEntities.EclWeisungMeldungRaw;
import de.meetingapps.meetingportal.meetComKonst.KonstStimmart;
import de.meetingapps.meetingportal.meetComKonst.KonstWillenserklaerung;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@RequestScoped
@Named
@Deprecated
public class AControllerKIAVBestaetigung {

    @Inject
    ADlgVariablen aDlgVariablen;
    @Inject
    EclPortalTexteM eclTextePortalM;
    @Inject
    AFunktionen aFunktionen;
    @Inject
    EclParamM eclParamM;
    @Inject
    private EclDbM eclDbM;
    @Inject
    EclKIAVM eclKIAVM;
    @Inject
    EclAbstimmungenListeM eclAbstimmungenListeM;
    @Inject
    private EclTeilnehmerLoginM eclTeilnehmerLoginM;
    @Inject
    private EclZugeordneteMeldungM eclZugeordneteMeldungM;

    /*************************Abspeichern Vollmacht/Weisung an KI/AV**************************
     * 
     * Eingabeparameter:
     * 		ADlgVariablen.ausgewaehlteHauptAktion
     * 		ADlgVariablen.ausgewaehlteAktion
     * 		EclTeilnehmerLoginM.anmeldeAktionaersnummer
     * 		EclKIAVM
     * 		EclAbstimmungenListeM
     * 
     * 		Falls Anmeldung bereits vorher durchgeführt:
     * 			EclTeilnehmerLoginM.anmeldeIdentPersonenNatJur
     * 			EclZugeordneteMeldungM
     * 
     * 
     * Returncode:
     * 		ADlgVariablen.fehlerMeldung/.fehlerNr
     * 
     * 
     * */
    public String doErteilen() {
        if (!aFunktionen.pruefeStart("aKiavBestaetigung")) {
            return "aDlgFehler";
        }

        EclAktienregister aktienregisterEintrag = null;
        int erg = 0;
        int i;
        /*Initialisieren*/
        aDlgVariablen.clearFehlerMeldung();
        eclDbM.openAll();

        if (!aFunktionen.pruefeOBWillenserklaerungZulaessig(eclDbM.getDbBundle(),
                KonstWillenserklaerung.vollmachtUndWeisungAnKIAV, aDlgVariablen.getAusgewaehlteHauptAktion())) {
            eclDbM.closeAllAbbruch();
            return aFunktionen.setzeEnde("aTransaktionAbbruch", true, true);
        }

        BlWillenserklaerung lWillenserklaerung = null; /*Wird grundsätzlich nur bei "Erstanmeldung" benötigt - aber 
                                                       später im "If-Zweig" enthalten - deshalb hier
                                                       Definition*/
        /********************************************Eintrittskarte für Aktionär******************************************/

        if (aDlgVariablen.getAusgewaehlteHauptAktion().compareTo("1") == 0) {

            if (aFunktionen.reCheckKeineAktienanmeldungen(eclDbM.getDbBundle()) == false) {
                //	aDlgVariablen.setFehlerMeldung(eclTextePortalM.getFehlertext(CaFehler.afAktienregisterEintragNichtMehrVorhanden));
                aDlgVariablen.setFehlerMeldung("Anderer User aktiv gewesen");
                aDlgVariablen.setFehlerNr(CaFehler.afAndererUserAktiv);
                eclDbM.closeAllAbbruch();
                return aFunktionen.setzeEnde("aDlgAbbruch", true, true);

            }

            /***************Anmelden**************/
            lWillenserklaerung = new BlWillenserklaerung();
            lWillenserklaerung.pErteiltAufWeg = aDlgVariablen.getEingabeQuelle();
            lWillenserklaerung.pErteiltZeitpunkt = aDlgVariablen.getErteiltZeitpunkt();
            /*Aktienregister füllen*/
            aktienregisterEintrag = new EclAktienregister();
            aktienregisterEintrag.aktionaersnummer = eclTeilnehmerLoginM.getAnmeldeAktionaersnummer();
            erg = eclDbM.getDbBundle().dbAktienregister.leseZuAktienregisterEintrag(aktienregisterEintrag);
            if (erg <= 0) {/*Aktienregistereintrag nicht mehr vorhanden*/

                aDlgVariablen.setFehlerMeldung(
                        eclTextePortalM.getFehlertext(CaFehler.afAktienregisterEintragNichtMehrVorhanden));
                aDlgVariablen.setFehlerNr(CaFehler.afAktienregisterEintragNichtMehrVorhanden);
                eclDbM.closeAllAbbruch();
                aFunktionen.setzeEnde();
                return "";
            }

            aktienregisterEintrag = eclDbM.getDbBundle().dbAktienregister.ergebnisPosition(0);
            lWillenserklaerung.pEclAktienregisterEintrag = aktienregisterEintrag;

            /*Restliche Parameter füllen*/
            lWillenserklaerung.pAktienAnmelden = -1; /*Alle Aktien anmelden*/
            lWillenserklaerung.pAnmeldungFix = false; /*Nicht "Fix" anmelden*/
            if (aDlgVariablen.getAusgewaehlteAktion().compareTo("2") == 0) {
                lWillenserklaerung.pAnzahlAnmeldungen = 2;
            } else {
                lWillenserklaerung.pAnzahlAnmeldungen = 1;

            }
            lWillenserklaerung.pWillenserklaerungGeberIdent = -1; /*Aktionär gibt in diesem Fall*/

            lWillenserklaerung.anmeldungAusAktienregister(eclDbM.getDbBundle());
            if (lWillenserklaerung.rcIstZulaessig == false) {
                aDlgVariablen
                        .setFehlerMeldung(eclTextePortalM.getFehlertext(lWillenserklaerung.rcGrundFuerUnzulaessig));
                aDlgVariablen.setFehlerNr(lWillenserklaerung.rcGrundFuerUnzulaessig);
                eclDbM.closeAllAbbruch();
                aFunktionen.setzeEnde();
                return "";

            }
        } else {
            if (aFunktionen.reCheckKeineNeueWillenserklaerungen(eclDbM.getDbBundle(),
                    eclZugeordneteMeldungM.getMeldungsIdent(),
                    eclZugeordneteMeldungM.getIdentHoechsteWillenserklaerung()) == false) {
                aDlgVariablen.setFehlerMeldung(eclTextePortalM.getFehlertext(CaFehler.afAndererUserAktiv));
                aDlgVariablen.setFehlerNr(CaFehler.afAndererUserAktiv);
                eclDbM.closeAllAbbruch();
                return aFunktionen.setzeEnde("aDlgAbbruch", true, true);
            }

        }

        /*in lWillenserklaerung.rcMeldungen[0] steht Anmelde-Ident, die weiterverwendet werden kann*/

        /***Vollmacht/Weisung an KIAV****/
        BlWillenserklaerung vwWillenserklaerung = new BlWillenserklaerung();
        vwWillenserklaerung.pErteiltAufWeg = aDlgVariablen.getEingabeQuelle();
        vwWillenserklaerung.pErteiltZeitpunkt = aDlgVariablen.getErteiltZeitpunkt();

        if (aDlgVariablen.getAusgewaehlteHauptAktion().compareTo("1") == 0) { /*Erstanmeldung*/
            vwWillenserklaerung.piMeldungsIdentAktionaer = lWillenserklaerung.rcMeldungen[0];
            vwWillenserklaerung.pFolgeFuerWillenserklaerungIdent = lWillenserklaerung.rcWillenserklaerungIdent;
            vwWillenserklaerung.pWillenserklaerungGeberIdent = -1; /*Aktionär*/
        } else {/*Anmeldung bereits früher durchgeführt*/
            vwWillenserklaerung.piMeldungsIdentAktionaer = eclZugeordneteMeldungM.getMeldungsIdent();

            if (eclZugeordneteMeldungM.getArtBeziehung() == 1) {
                vwWillenserklaerung.pWillenserklaerungGeberIdent = -1; /*Aktionär*/
            } else { /*Hier: es handelt sich um eine Untervollmacht. D.h. Geber = angemeldete personNatJur.*/
                vwWillenserklaerung.pWillenserklaerungGeberIdent = eclTeilnehmerLoginM.getAnmeldeIdentPersonenNatJur();
            }

        }

        /*pAufnahmendeSammelkarteIdent - aufnehmende Sammelkarte*/
        vwWillenserklaerung.pAufnehmendeSammelkarteIdent = eclKIAVM.getMeldeIdent();

        /*Abgegebene Weisung (uninterpretiert)
        public EclWeisungMeldungRaw pEclWeisungMeldungRaw=null;*/
        vwWillenserklaerung.pEclWeisungMeldungRaw = new EclWeisungMeldungRaw();
        /*Abgegebene Weisung (interpretiert)
        public EclWeisungMeldung pEclWeisungMeldung=null;*/
        vwWillenserklaerung.pEclWeisungMeldung = new EclWeisungMeldung();

        if (eclAbstimmungenListeM.getAlternative() == 1) { /**Alternative 1*/

            List<EclAbstimmungM> lAbstimmungenListe = eclAbstimmungenListeM.getAbstimmungenListeM();

            switch (aDlgVariablen.getAusgewaehlteAktion()) { /*Gesamtmarkierungen etc. festlegen. Details in Schleife*/
            case "7":
            case "32":
            case "36":
            /*nur Vollmacht an KI/AV - Weisung = frei*/ {
                vwWillenserklaerung.pEclWeisungMeldungRaw.stimmartGesamt = "       X"; /*frei*/
                vwWillenserklaerung.pEclWeisungMeldung.stimmartGesamt = KonstStimmart.frei;
                break;
            }
            case "8":
            case "33":
            case "37":
            /*Vollmacht/Weisung dediziert*/ {
                break;
            }
            case "9":
            case "34":
            case "38":
            /*Vollmacht/Weisung gemäß Vorschlag*/ {
                vwWillenserklaerung.pEclWeisungMeldungRaw.gemaessEigenemAbstimmungsVorschlagIdent = eclKIAVM
                        .getAbstimmungsVorschlagIdent();
                vwWillenserklaerung.pEclWeisungMeldungRaw.aktualisieren = 0;

                vwWillenserklaerung.pEclWeisungMeldung.gemaessEigenemAbstimmungsVorschlagIdent = eclKIAVM
                        .getAbstimmungsVorschlagIdent();
                vwWillenserklaerung.pEclWeisungMeldung.aktualisieren = 0;
                break;
            }
            }

            for (i = 0; i < lAbstimmungenListe.size(); i++) {

                int posInWeisung = lAbstimmungenListe.get(i).getIdentWeisungssatz();

                if (!lAbstimmungenListe.get(i).isUeberschrift()) {

                    switch (aDlgVariablen.getAusgewaehlteAktion()) { /*Einzelmarkierung*/
                    case "7":
                    case "32":
                    case "36":
                    /*nur Vollmacht an KI/AV - Weisung = frei*/ {
                        vwWillenserklaerung.pEclWeisungMeldung.abgabe[posInWeisung] = KonstStimmart.frei;
                        break;
                    }
                    case "8":
                    case "33":
                    case "37":
                    /*Vollmacht/Weisung dediziert*/ {
                        if (lAbstimmungenListe.get(i).getGewaehlt() != null) {
                            switch (lAbstimmungenListe.get(i).getGewaehlt()) {
                            case "J":
                                vwWillenserklaerung.pEclWeisungMeldungRaw.abgabe[posInWeisung] = " X";
                                vwWillenserklaerung.pEclWeisungMeldung.abgabe[posInWeisung] = 1;
                                break;
                            case "N":
                                vwWillenserklaerung.pEclWeisungMeldungRaw.abgabe[posInWeisung] = "  X";
                                vwWillenserklaerung.pEclWeisungMeldung.abgabe[posInWeisung] = 2;
                                break;
                            case "E":
                                vwWillenserklaerung.pEclWeisungMeldungRaw.abgabe[posInWeisung] = "   X";
                                vwWillenserklaerung.pEclWeisungMeldung.abgabe[posInWeisung] = 3;
                                break;
                            case "U":
                                vwWillenserklaerung.pEclWeisungMeldungRaw.abgabe[posInWeisung] = "    X";
                                vwWillenserklaerung.pEclWeisungMeldung.abgabe[posInWeisung] = 4;
                                break;
                            case "2":
                                vwWillenserklaerung.pEclWeisungMeldungRaw.abgabe[posInWeisung] = "       X";
                                vwWillenserklaerung.pEclWeisungMeldung.abgabe[posInWeisung] = 7;
                                break;
                            case "S":
                                vwWillenserklaerung.pEclWeisungMeldungRaw.abgabe[posInWeisung] = "         X";
                                vwWillenserklaerung.pEclWeisungMeldung.abgabe[posInWeisung] = 9;
                                break;
                            }
                        }

                        break;
                    }
                    case "9":
                    case "34":
                    case "38":
                    /*Vollmacht/Weisung gemäß Vorschlag*/ {
                        vwWillenserklaerung.pEclWeisungMeldung.abgabeLautGesamt[posInWeisung] = 1;
                        switch (lAbstimmungenListe.get(i).getAbstimmungsvorschlag()) {
                        case "J":
                            vwWillenserklaerung.pEclWeisungMeldung.abgabe[posInWeisung] = 1;
                            break;
                        case "N":
                            vwWillenserklaerung.pEclWeisungMeldung.abgabe[posInWeisung] = 2;
                            break;
                        case "E":
                            vwWillenserklaerung.pEclWeisungMeldung.abgabe[posInWeisung] = 3;
                            break;
                        case "U":
                            vwWillenserklaerung.pEclWeisungMeldung.abgabe[posInWeisung] = 4;
                            break;
                        }
                        break;
                    }
                    }
                }
            }

            List<EclAbstimmungM> lGegenantraegeListe = eclAbstimmungenListeM.getGegenantraegeListeM();

            for (i = 0; i < lGegenantraegeListe.size(); i++) {

                int posInWeisung = lGegenantraegeListe.get(i).getIdentWeisungssatz();

                if (!lGegenantraegeListe.get(i).isUeberschrift()) {

                    switch (aDlgVariablen.getAusgewaehlteAktion()) { /*Einzelmarkierung*/
                    case "7":
                    case "32":
                    case "36":
                    /*nur Vollmacht an KI/AV - Weisung = frei*/ {
                        vwWillenserklaerung.pEclWeisungMeldung.abgabe[posInWeisung] = KonstStimmart.frei;
                        break;
                    }
                    case "8":
                    case "33":
                    case "37":
                    /*Vollmacht/Weisung dediziert*/ {
                        if (lGegenantraegeListe.get(i).isMarkiert()) {
                            vwWillenserklaerung.pEclWeisungMeldungRaw.abgabe[posInWeisung] = "            X";
                            vwWillenserklaerung.pEclWeisungMeldung.abgabe[posInWeisung] = 12;
                        } else {
                            if (lGegenantraegeListe.get(i).getGewaehlt() != null) {
                                switch (lGegenantraegeListe.get(i).getGewaehlt()) {
                                case "J":
                                    vwWillenserklaerung.pEclWeisungMeldungRaw.abgabe[posInWeisung] = " X";
                                    vwWillenserklaerung.pEclWeisungMeldung.abgabe[posInWeisung] = 1;
                                    break;
                                case "N":
                                    vwWillenserklaerung.pEclWeisungMeldungRaw.abgabe[posInWeisung] = "  X";
                                    vwWillenserklaerung.pEclWeisungMeldung.abgabe[posInWeisung] = 2;
                                    break;
                                case "E":
                                    vwWillenserklaerung.pEclWeisungMeldungRaw.abgabe[posInWeisung] = "   X";
                                    vwWillenserklaerung.pEclWeisungMeldung.abgabe[posInWeisung] = 3;
                                    break;
                                case "U":
                                    vwWillenserklaerung.pEclWeisungMeldungRaw.abgabe[posInWeisung] = "    X";
                                    vwWillenserklaerung.pEclWeisungMeldung.abgabe[posInWeisung] = 4;
                                    break;
                                case "2":
                                    vwWillenserklaerung.pEclWeisungMeldungRaw.abgabe[posInWeisung] = "       X";
                                    vwWillenserklaerung.pEclWeisungMeldung.abgabe[posInWeisung] = 7;
                                    break;
                                case "S":
                                    vwWillenserklaerung.pEclWeisungMeldungRaw.abgabe[posInWeisung] = "         X";
                                    vwWillenserklaerung.pEclWeisungMeldung.abgabe[posInWeisung] = 9;
                                    break;
                                }
                            }
                        }

                        break;
                    }
                    case "9":
                    case "34":
                    case "38":
                    /*Vollmacht/Weisung gemäß Vorschlag*/ {
                        vwWillenserklaerung.pEclWeisungMeldung.abgabeLautGesamt[posInWeisung] = 1;
                        switch (lGegenantraegeListe.get(i).getAbstimmungsvorschlag()) {
                        case "J":
                            vwWillenserklaerung.pEclWeisungMeldung.abgabe[posInWeisung] = 1;
                            break;
                        case "N":
                            vwWillenserklaerung.pEclWeisungMeldung.abgabe[posInWeisung] = 2;
                            break;
                        case "E":
                            vwWillenserklaerung.pEclWeisungMeldung.abgabe[posInWeisung] = 3;
                            break;
                        case "U":
                            vwWillenserklaerung.pEclWeisungMeldung.abgabe[posInWeisung] = 4;
                            break;
                        }
                        break;
                    }
                    }
                }

            }
        }

        else { /******Alternative 2 - ist fertig aufbereitet************/
            vwWillenserklaerung.pEclWeisungMeldung = eclAbstimmungenListeM.getWeisungMeldung();
            vwWillenserklaerung.pEclWeisungMeldungRaw = eclAbstimmungenListeM.getWeisungMeldungRaw();
            for (int i1 = 0; i1 < 200; i1++) {
                if (vwWillenserklaerung.pEclWeisungMeldung.abgabe[i1] == -999) {
                    vwWillenserklaerung.pEclWeisungMeldung.abgabe[i1] = KonstStimmart.nichtMarkiert;

                }
            }
        }

        /*Willenserklärung speichern*/
        switch (aDlgVariablen.getAusgewaehlteAktion()) { /*Einzelmarkierung*/
        case "7":
        case "8":
        case "9": {
            vwWillenserklaerung.vollmachtUndWeisungAnKIAV(eclDbM.getDbBundle());
            break;
        }
        case "32":
        case "33":
        case "34": {
            vwWillenserklaerung.dauervollmachtAnKIAV(eclDbM.getDbBundle());
            break;
        }

        case "36":
        case "37":
        case "38": {
            vwWillenserklaerung.organisatorischMitWeisungInSammelkarte(eclDbM.getDbBundle());
            break;
        }

        }
        aDlgVariablen.setRcWillenserklaerungIdentAusgefuehrt(vwWillenserklaerung.rcWillenserklaerungIdent);

        /*Falls nicht möglich: Fehlermeldung, zum Anmelden*/
        if (vwWillenserklaerung.rcIstZulaessig == false) {
            aDlgVariablen.setFehlerMeldung(eclTextePortalM.getFehlertext(vwWillenserklaerung.rcGrundFuerUnzulaessig));
            aDlgVariablen.setFehlerNr(vwWillenserklaerung.rcGrundFuerUnzulaessig);
            eclDbM.closeAllAbbruch();
            aFunktionen.setzeEnde();
            return "";
        }

        aFunktionen.setzeEnde("aKiavQuittung", true, false);

        if (eclParamM.getParam().paramPortal.quittungDialog == 1) {
            eclDbM.closeAll();
            return aFunktionen.setzeEnde("aKiavQuittung", true, false);
        } else {

            String naechsteMaske = aFunktionen.waehleAusgangsmaske(eclDbM.getDbBundle());
            eclDbM.closeAll();

            return aFunktionen.setzeEnde(naechsteMaske, true, true);

        }

    }

    public String doZurueck() {
        if (!aFunktionen.pruefeStart("aKiavBestaetigung")) {
            return "aDlgFehler";
        }
        aDlgVariablen.setBestaetigtDassBerechtigt(false);
        return aFunktionen.setzeEnde("aKiavVollmacht", true, false);
    }

    /**Abmelden*/
    public String doAbmelden() {
        aDlgVariablen.clearLogin();
        return aFunktionen.waehleLogout();
    }

}
