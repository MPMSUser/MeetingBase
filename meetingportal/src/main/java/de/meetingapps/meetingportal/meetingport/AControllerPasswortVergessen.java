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

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaDatumZeit;
import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComAllg.CaPasswortVerschluesseln;
import de.meetingapps.meetingportal.meetComAllg.CaString;
import de.meetingapps.meetingportal.meetComBa.BaMailM;
import de.meetingapps.meetingportal.meetComBl.BlNummernformen;
import de.meetingapps.meetingportal.meetComBl.BlTeilnehmerLogin;
import de.meetingapps.meetingportal.meetComBlManaged.BlMFuelleEclMAusPufferOderDBEE;
import de.meetingapps.meetingportal.meetComEclM.EclDbM;
import de.meetingapps.meetingportal.meetComEclM.EclParamM;
import de.meetingapps.meetingportal.meetComEclM.EclPortalTexteM;
import de.meetingapps.meetingportal.meetComEntities.EclAktienregister;
import de.meetingapps.meetingportal.meetComEntities.EclAktienregisterErgaenzung;
import de.meetingapps.meetingportal.meetComEntities.EclAktienregisterLoginDaten;
import de.meetingapps.meetingportal.meetComEntities.EclAktienregisterZusatz;
import de.meetingapps.meetingportal.meetComEntities.EclAufgaben;
import de.meetingapps.meetingportal.meetComKonst.KonstAktienregisterErgaenzung;
import de.meetingapps.meetingportal.meetComKonst.KonstAufgaben;
import de.meetingapps.meetingportal.meetComKonst.KonstAufgabenAnforderer;
import de.meetingapps.meetingportal.meetComKonst.KonstAufgabenStatus;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Deprecated
@RequestScoped
@Named
public class AControllerPasswortVergessen {
    @Deprecated

    private int logDrucken = 3;

    @Inject
    EclDbM eclDbM;
    @Inject
    ADlgVariablen aDlgVariablen;
    @Inject
    AFunktionen aFunktionen;
    @Inject
    EclPortalTexteM eclTextePortalM;
    @Inject
    private BaMailM baMailm;
    @Inject
    EclParamM eclParamM;
    @Inject
    AControllerPasswortVergessenSession aControllerPasswortVergessenSession;
    @Inject
    AControllerLogin aControllerLogin;
    @Inject
    BlMFuelleEclMAusPufferOderDBEE blMFuelleEclMAusPufferOderDBEE;

    public void clearFehlermeldung() {
        aControllerPasswortVergessenSession.clearFehlermeldung();
    }

    public void clearDlgVariablen() {
        aControllerPasswortVergessenSession.clearDlgVariablen();
    }

    public void passwortZuruecksetzenVorbereiten() {
        eclDbM.openAll();
        eclDbM.closeAll();
        clearDlgVariablen();
        int verfahrenPasswortVergessen = eclParamM.getParam().paramPortal.verfahrenPasswortVergessen;

        if (eclParamM.getParam().paramPortal.verfahrenPasswortVergessenAblauf == 0) {
            if ((verfahrenPasswortVergessen & 1) == 1 && (verfahrenPasswortVergessen & 4) == 4) {
                /*Auswahl zwischen Email-Adresse und Strasse/Ort ermöglichen*/
                aControllerPasswortVergessenSession.setAuswahlAnbieten(true);
                aControllerPasswortVergessenSession.setEingabeAnbieten(true);
                aControllerPasswortVergessenSession.setEingabeSelektiert("1");

                aControllerPasswortVergessenSession.setTextVariante(4);
            } else {
                /*Keine Auswahl möglich*/
                aControllerPasswortVergessenSession.setAuswahlAnbieten(false);
                aControllerPasswortVergessenSession.setEingabeAnbieten(false);
                aControllerPasswortVergessenSession.setTextVariante(1);

                if ((verfahrenPasswortVergessen & 1) == 1) {
                    aControllerPasswortVergessenSession.setEingabeSelektiert("1");
                    aControllerPasswortVergessenSession.setEingabeAnbieten(true);
                    aControllerPasswortVergessenSession.setTextVariante(2);
                }
                if ((verfahrenPasswortVergessen & 4) == 4) {
                    aControllerPasswortVergessenSession.setEingabeSelektiert("2");
                    aControllerPasswortVergessenSession.setEingabeAnbieten(true);
                    aControllerPasswortVergessenSession.setTextVariante(3);
                }
            }
        } else {/**Ablauf==1 (mit Geburtsdatum)*/
            aControllerPasswortVergessenSession.setAuswahlAnbieten(true);
            aControllerPasswortVergessenSession.setEingabeAnbieten(false);
            aControllerPasswortVergessenSession.setEingabeSelektiert("1");
        }

    }

    /**
     * "Normale" Returncodes, aber mit Meldung:
     * afPWVergessenMailGesendet
     *
     * Fehler:
     * afKennungUnbekannt
     * afHinterlegteEmailAdresseEingeben
     * afHinterlegteStrasseOrtEingeben
     * afEMailUnbekannt
     *
     */
    public String doPasswortZuruecksetzen() {
        boolean erg;

        if (!aFunktionen.pruefeStart("aPasswortVergessen")) {
            return "aDlgFehler";
        }

        aControllerPasswortVergessenSession.setTextVariante(0);

        eclDbM.openAll();
        blMFuelleEclMAusPufferOderDBEE.fuelleAlleVariablenBeiStart();

        eclDbM.openWeitere();

        BlNummernformen blNummernformen = new BlNummernformen(eclDbM.getDbBundle());
        String alteEingabe = aControllerPasswortVergessenSession.getLoginKennung();
        aControllerPasswortVergessenSession.setLoginKennung(blNummernformen
                .aktienregisterNraufbereitenFuerIntern(aControllerPasswortVergessenSession.getLoginKennung()));

        BlTeilnehmerLogin blTeilnehmerLogin = new BlTeilnehmerLogin();
        blTeilnehmerLogin.initDB(eclDbM.getDbBundle());
        erg = blTeilnehmerLogin.findeKennung(aControllerPasswortVergessenSession.getLoginKennung());
        if (erg == false || blTeilnehmerLogin.anmeldeKennungArt != 1) {
            aDlgVariablen.setFehlerNr(CaFehler.afKennungUnbekannt);
            aDlgVariablen.setFehlerMeldung(eclTextePortalM.getFehlertext(CaFehler.afKennungUnbekannt));
            eclDbM.closeAllAbbruch();
            aControllerPasswortVergessenSession.setLoginKennung(alteEingabe);
            aFunktionen.setzeEnde();
            return "";
        }

        EclAktienregisterZusatz lAktienregisterZusatz = blTeilnehmerLogin.aktienregisterZusatz;

        if (blTeilnehmerLogin.anmeldenUnzulaessig) {
            aControllerPasswortVergessenSession.setTextVarianteQuittung(3);
            eclDbM.closeAll();
            this.clearDlgVariablen();
            return aFunktionen.setzeEnde("aPasswortVergessenQuittung", true, true);
        }

        if (blTeilnehmerLogin.dauerhafteRegistrierungUnzulaessig
                && aControllerPasswortVergessenSession.getEingabeSelektiert().compareTo("1") == 0) {
            aDlgVariablen.setFehlerNr(CaFehler.afPasswortVergessenUeberMailUnzulaessig);
            aDlgVariablen
                    .setFehlerMeldung(eclTextePortalM.getFehlertext(CaFehler.afPasswortVergessenUeberMailUnzulaessig));
            eclDbM.closeAllAbbruch();
            aControllerPasswortVergessenSession.setLoginKennung(alteEingabe);
            aFunktionen.setzeEnde();
            return "";
        }

        /*TODO _Portal Passwort Vergessen noch konsolidieren*/
        //		if (!lAktienregisterZusatz.eMailFuerVersand.isEmpty() ||
        //			!lAktienregisterZusatz.eMail2FuerVersand.isEmpty()){
        //		}

        if (eclParamM.getParam().paramPortal.verfahrenPasswortVergessenAblauf == 0) {
            /*++++++++++++++++++++++Standard-Ablauf+++++++++++++++++++++++++++++++++++++++*/
            if (aControllerPasswortVergessenSession.isEingabeAnbieten() == true) {
                /*Verifikationsdaten werden mit eingegeben - manuelles oder automatisches Verfahren*/
                if (aControllerPasswortVergessenSession.getEingabeSelektiert().compareTo("1") == 0) {
                    /*E-Mail-Adresse eingegeben*/
                    if (aControllerPasswortVergessenSession.getEmailAdresse().trim().isEmpty()) {
                        /*Keine E-Mail-Adresse eingegeben*/
                        aDlgVariablen.setFehlerNr(CaFehler.afHinterlegteEmailAdresseEingeben);
                        aDlgVariablen.setFehlerMeldung(
                                eclTextePortalM.getFehlertext(CaFehler.afHinterlegteEmailAdresseEingeben));
                        eclDbM.closeAllAbbruch();
                        aFunktionen.setzeEnde();
                        return "";
                    }

                    if (lAktienregisterZusatz.eMailFuerVersand
                            .compareToIgnoreCase(aControllerPasswortVergessenSession.getEmailAdresse()) != 0
                            && lAktienregisterZusatz.eMail2FuerVersand
                                    .compareToIgnoreCase(aControllerPasswortVergessenSession.getEmailAdresse()) != 0) {
                        /*Eingegebene E-Mail-Adresse stimmt nicht mit hinterlegter E-Mail-Adresse überein*/
                        aDlgVariablen.setFehlerNr(CaFehler.afEMailUnbekannt);
                        aDlgVariablen.setFehlerMeldung(eclTextePortalM.getFehlertext(CaFehler.afEMailUnbekannt));
                        eclDbM.closeAllAbbruch();
                        aFunktionen.setzeEnde();
                        return "";
                    }

                    eMailVerschicken(lAktienregisterZusatz);

                    aControllerPasswortVergessenSession.setTextVarianteQuittung(1);

                    eclDbM.closeAll();
                    this.clearDlgVariablen();
                    return aFunktionen.setzeEnde("aPasswortVergessenQuittung", true, true);

                } else {
                    /*Strasse+Ort eingegeben*/
                    if (aControllerPasswortVergessenSession.getStrasse().trim().isEmpty()
                            || aControllerPasswortVergessenSession.getOrt().trim().isEmpty()) {
                        aDlgVariablen.setFehlerNr(CaFehler.afHinterlegteStrasseOrtEingeben);
                        aDlgVariablen.setFehlerMeldung(
                                eclTextePortalM.getFehlertext(CaFehler.afHinterlegteStrasseOrtEingeben));
                        eclDbM.closeAllAbbruch();
                        aFunktionen.setzeEnde();
                        return "";
                    }

                    /*Aufgabe eintragen*/
                    aufgabeEintragen(aControllerPasswortVergessenSession.getLoginKennung(),
                            aControllerPasswortVergessenSession.getStrasse(),
                            aControllerPasswortVergessenSession.getOrt());
                }
            } else {
                /*Immer manuelles Verfahren - keine Eingaben getätigt*/
                /*Aufgabe eintragen*/
                aufgabeEintragen(aControllerPasswortVergessenSession.getLoginKennung(), "", "");
            }
        } else {
            /*++++++++++++++++++Ablauf ==1, Geburtsdatum als Bestätigung+++++++++++++++++++++++++++++++++++++*/

            /*Geburtsdatum abprüfen*/
            String eingabeGeburtsdatum = aControllerPasswortVergessenSession.getGeburtsdatum().trim();
            if (!CaString.isDatum(eingabeGeburtsdatum)) {
                /*Format unzulässig*/
                aDlgVariablen.setFehlerNr(CaFehler.afGeburtdatumUnzulaessigesFormat);
                aDlgVariablen
                        .setFehlerMeldung(eclTextePortalM.getFehlertext(CaFehler.afGeburtdatumUnzulaessigesFormat));
                eclDbM.closeAllAbbruch();
                aControllerPasswortVergessenSession.setLoginKennung(alteEingabe);
                aFunktionen.setzeEnde();
                return "";
            }
            int rc = eclDbM.getDbBundle().dbAktienregisterErgaenzung
                    .readZuident(blTeilnehmerLogin.anmeldeIdentAktienregister);
            if (rc < 1) {
                aDlgVariablen.setFehlerNr(CaFehler.afKennungUnbekannt);
                aDlgVariablen.setFehlerMeldung(eclTextePortalM.getFehlertext(CaFehler.afKennungUnbekannt));
                eclDbM.closeAllAbbruch();
                aControllerPasswortVergessenSession.setLoginKennung(alteEingabe);
                aFunktionen.setzeEnde();
                return "";
            }
            EclAktienregisterErgaenzung lAktienregisterErgaenzung = eclDbM.getDbBundle().dbAktienregisterErgaenzung
                    .ergebnisPosition(0);
            String gespeichertGeburtsdatum1 = (lAktienregisterErgaenzung
                    .getErgaenzungString(KonstAktienregisterErgaenzung.ku178_GeburtsdatumMitglied));
            String gespeichertGeburtsdatum2 = (lAktienregisterErgaenzung
                    .getErgaenzungString(KonstAktienregisterErgaenzung.ku178_GeburtsdatumEhegatte));
            if (!gespeichertGeburtsdatum1.equals(eingabeGeburtsdatum)
                    && !gespeichertGeburtsdatum2.equals(eingabeGeburtsdatum)) {
                /*Geburtsdatum stimmt nicht mit gespeichertem überein*/
                aDlgVariablen.setFehlerNr(CaFehler.afGeburtdatumFalsch);
                aDlgVariablen.setFehlerMeldung(eclTextePortalM.getFehlertext(CaFehler.afGeburtdatumFalsch));
                eclDbM.closeAllAbbruch();
                aControllerPasswortVergessenSession.setLoginKennung(alteEingabe);
                aFunktionen.setzeEnde();
                return "";
            }
            if (aControllerPasswortVergessenSession.getEingabeSelektiert().compareTo("1") == 0) {
                /*E-Mail-Adresse hinterlegt?*/
                if (lAktienregisterZusatz.eMailFuerVersand.isEmpty()) {
                    aDlgVariablen.setFehlerNr(CaFehler.afKeineEmailAdresseHinterlegt);
                    aDlgVariablen
                            .setFehlerMeldung(eclTextePortalM.getFehlertext(CaFehler.afKeineEmailAdresseHinterlegt));
                    eclDbM.closeAllAbbruch();
                    aControllerPasswortVergessenSession.setLoginKennung(alteEingabe);
                    aFunktionen.setzeEnde();
                    return "";
                }
                aControllerPasswortVergessenSession.setEmailAdresse(lAktienregisterZusatz.eMailFuerVersand);
                eMailVerschicken(lAktienregisterZusatz);

                aControllerPasswortVergessenSession.setTextVarianteQuittung(1);

                eclDbM.closeAll();
                this.clearDlgVariablen();
                return aFunktionen.setzeEnde("aPasswortVergessenQuittung", true, true);

            } else {
                /*Papierversand*/

                /*Aufgabe eintragen*/
                aufgabeEintragen(aControllerPasswortVergessenSession.getLoginKennung(),
                        aControllerPasswortVergessenSession.getStrasse(), aControllerPasswortVergessenSession.getOrt());
            }
        }

        aControllerPasswortVergessenSession.setTextVarianteQuittung(2);
        eclDbM.closeAll();
        this.clearDlgVariablen();
        return aFunktionen.setzeEnde("aPasswortVergessenQuittung", true, true);

    }

    /*******Interne Funktionen für Zurücksetzen*************************/

    private void aufgabeEintragen(String pArgument0, String pArgument1, String pArgument2) {
        EclAufgaben lAufgabe = new EclAufgaben();
        lAufgabe.mandant = eclDbM.getDbBundle().clGlobalVar.mandant;
        lAufgabe.aufgabe = KonstAufgaben.aktionaerNeuesPasswortAdressePruefen;
        lAufgabe.zeitpunktErteilt = CaDatumZeit.DatumZeitStringFuerDatenbank();
        lAufgabe.anforderer = KonstAufgabenAnforderer.aktionaerPortal;
        lAufgabe.status = KonstAufgabenStatus.gestellt;
        lAufgabe.argument[0] = pArgument0;
        lAufgabe.argument[1] = pArgument1;
        lAufgabe.argument[2] = pArgument2;
        eclDbM.getDbBundle().dbAufgaben.insert(lAufgabe);

    }

    private void eMailVerschicken(EclAktienregisterZusatz lAktienregisterZusatz) {
        /**Nun Automatische Passwort-Vergessen-Prozedur an E-Mail einleiten*/
        String pwVergessenLink = eclDbM.getDbBundle().dbEindeutigerKey.getNextFree(); //Eindeutiger Key, der dann Bestandteil des Links wird*/
        lAktienregisterZusatz.passwortVergessenLink = pwVergessenLink;

        String sprache = "DE";
        if (eclParamM.getClGlobalVar().sprache == 2) {
            sprache = "EN";
        }
        // DIM: dead store
        //      BlEinsprungLinkPortal lBlEinsprungLinkPortal = new BlEinsprungLinkPortal(eclDbM.getDbBundle());
        //		aDlgVariablen.setEinsprungsLinkFuerEmail(lBlEinsprungLinkPortal.linkFuerPasswortVergessen(lAktienregisterZusatz.passwortVergessenLink, sprache));
        if (aControllerPasswortVergessenSession.isWurdeUeberAppAngefordert()) {
            //			aDlgVariablen.setEinsprungsLinkFuerEmail(lBlEinsprungLinkPortal.linkFuerPasswortVergessenApp(lAktienregisterZusatz.passwortVergessenLink, sprache));
        }
        aDlgVariablen.setEinsprungsLinkNurCode(lAktienregisterZusatz.passwortVergessenLink);

        String hMailText = "", hBetreff = "";

        if (eclTextePortalM.portalNichtUeberNeueTexte()) {
        } else {
            if (aControllerPasswortVergessenSession.isWurdeUeberAppAngefordert() == false) {
                hBetreff = eclTextePortalM.holeText("222");
                hMailText = eclTextePortalM.holeText("223");
            } else {
                hBetreff = eclTextePortalM.holeText("235");
                hMailText = eclTextePortalM.holeText("236");
            }
        }

        CaBug.druckeLog("E-Mail-Adresse=" + aControllerPasswortVergessenSession.getEmailAdresse(),
                logDrucken, 3);
        baMailm.senden(aControllerPasswortVergessenSession.getEmailAdresse(), hBetreff, hMailText);

        eclDbM.getDbBundle().dbAktienregisterZusatz.update(lAktienregisterZusatz);

    }

    /****************Weiteres**************************/

    public String doZumLogin() {
        /*Zur Login-Funktion ist immer möglich - deshalb kein Abprüfen mit aFunktionen.pruefeStart ! */
        aControllerLogin.clearFehlermeldung();
        return aFunktionen.setzeEnde("aLogin", true, true);
    }

    public String doSpeichern() {
        pwZurueckApp = false;
        return doSpeichernIntern();
    }

    public String doSpeichernApp() {
        pwZurueckApp = true;
        return doSpeichernIntern();
    }

    /**Routine für aPwZurueckHtml*
     *
     * Fehlermeldungen:
     * afPasswortFehlt
     * afPasswortBestaetigungWeichtAb
     * afPasswortZuKurz
     * afKennungUnbekannt
     * afPWVergessenLinkUngueltig
     *
     * */
    private boolean pwZurueckApp = false;

    private String doSpeichernIntern() {
        boolean erg;
        aControllerPasswortVergessenSession.clearFehlermeldung();
        /*Wird direkt aufgerufen, deshalb keine Überprüfung möglich - und auch nicht erforderlich. Falls Browser-/Vor-/Zurück verwendet wird,
         *werden halt ggf. jeweils die dann aktuellen Eingaben überprüft.
         */

        if (eclParamM.getClGlobalVar().mandant == 0) {
            CaBug.drucke("Mandant ist 0");
            aFunktionen.setzeEnde();
            return "aDlgFehlerAllgemein";
        }

        /*Überhaupt Passwort eingegeben?*/
        if (aControllerPasswortVergessenSession.getPasswort().isEmpty()) {
            this.setFehlerNr(CaFehler.afPasswortFehlt);
            this.setFehlerMeldung(eclTextePortalM.getFehlertext(CaFehler.afPasswortFehlt));
            aFunktionen.setzeEnde();
            return "";
        }
        /*Passwort-Bestätigung gleich?*/
        if (aControllerPasswortVergessenSession.getPasswort()
                .compareTo(aControllerPasswortVergessenSession.getPasswortBestaetigung()) != 0) {
            this.setFehlerNr(CaFehler.afPasswortBestaetigungWeichtAb);
            this.setFehlerMeldung(eclTextePortalM.getFehlertext(CaFehler.afPasswortBestaetigungWeichtAb));
            aFunktionen.setzeEnde();
            return "";
        }

        eclDbM.openAll();
        blMFuelleEclMAusPufferOderDBEE.fuelleAlleVariablenBeiStart();

        BlNummernformen blNummernformen = new BlNummernformen(eclDbM.getDbBundle());
        aControllerPasswortVergessenSession.setLoginKennung(blNummernformen
                .aktienregisterNraufbereitenFuerIntern(aControllerPasswortVergessenSession.getLoginKennung()));

        /*Passwort lang genug*/
        if (aControllerPasswortVergessenSession.getPasswort()
                .length() < eclParamM.getParam().paramPortal.passwortMindestLaenge) {
            this.setFehlerNr(CaFehler.afPasswortZuKurz);
            this.setFehlerMeldung(eclTextePortalM.getFehlertext(CaFehler.afPasswortZuKurz));
            eclDbM.closeAllAbbruch();
            aFunktionen.setzeEnde();
            return "";
        }

        BlTeilnehmerLogin blTeilnehmerLogin = new BlTeilnehmerLogin();
        blTeilnehmerLogin.initDB(eclDbM.getDbBundle());
        erg = blTeilnehmerLogin.findeKennung(aControllerPasswortVergessenSession.getLoginKennung());
        if (erg == false || blTeilnehmerLogin.anmeldeKennungArt != 1) {
            this.setFehlerNr(CaFehler.afKennungUnbekannt);
            this.setFehlerMeldung(eclTextePortalM.getFehlertext(CaFehler.afKennungUnbekannt));
            eclDbM.closeAllAbbruch();
            aFunktionen.setzeEnde();
            return "";
        }

        EclAktienregisterZusatz lAktienregisterZusatz = blTeilnehmerLogin.aktienregisterZusatz;

        if (lAktienregisterZusatz.passwortVergessenLink
                .compareTo(aControllerPasswortVergessenSession.getBestaetigungsCode()) != 0
                || aControllerPasswortVergessenSession.getBestaetigungsCode().isEmpty()) {
            this.setFehlerNr(CaFehler.afPWVergessenLinkUngueltig);
            this.setFehlerMeldung(eclTextePortalM.getFehlertext(CaFehler.afPWVergessenLinkUngueltig));
            eclDbM.closeAllAbbruch();
            aFunktionen.setzeEnde();
            return "";
        }

        /*Passwort übertragen*/
        EclAktienregister lAktienregisterEintrag = new EclAktienregister();
        lAktienregisterEintrag.aktienregisterIdent = blTeilnehmerLogin.anmeldeIdentAktienregister;
        eclDbM.getDbBundle().dbAktienregister.leseZuAktienregisterEintrag(lAktienregisterEintrag);
        lAktienregisterEintrag = eclDbM.getDbBundle().dbAktienregister.ergebnisPosition(0);

        eclDbM.getDbBundle().dbAktienregisterLoginDaten.readIdent(lAktienregisterEintrag.aktienregisterIdent);
        EclAktienregisterLoginDaten lAktienregisterLoginDaten = eclDbM.getDbBundle().dbAktienregisterLoginDaten
                .ergebnisPosition(0);
        lAktienregisterLoginDaten.passwortVerschluesselt = CaPasswortVerschluesseln
                .verschluesseln(aControllerPasswortVergessenSession.getPasswort());
        lAktienregisterLoginDaten.passwortInitial = "";
        eclDbM.getDbBundle().dbAktienregisterLoginDaten.update(lAktienregisterLoginDaten);

        lAktienregisterZusatz.passwortVergessenLink = "";
        lAktienregisterZusatz.eigenesPasswort = 99;
        eclDbM.getDbBundle().dbAktienregisterZusatz.update(lAktienregisterZusatz);

        eclDbM.closeAll();
        aControllerPasswortVergessenSession.setLoginKennung("");
        aControllerPasswortVergessenSession.setEmailAdresse("");
        aControllerPasswortVergessenSession.setPasswort("");
        aControllerPasswortVergessenSession.setPasswortBestaetigung("");
        aControllerLogin.clearFehlermeldung();
        if (pwZurueckApp == false) {
            return aFunktionen.setzeEnde("aLogin", true, true);
        } else {
            return aFunktionen.setzeEnde("aPwZurueckAppQuittung", true, true);
        }

    }

    /*******************Ab hier: Standard Getter/Setter********************************************/

    public String getLoginKennung() {
        return aControllerPasswortVergessenSession.getLoginKennung();
    }

    public void setLoginKennung(String loginKennung) {
        aControllerPasswortVergessenSession.setLoginKennung(loginKennung);
    }

    public String getEmailAdresse() {
        return aControllerPasswortVergessenSession.getEmailAdresse();
    }

    public void setEmailAdresse(String emailAdresse) {
        aControllerPasswortVergessenSession.setEmailAdresse(emailAdresse);
    }

    public String getPasswort() {
        return aControllerPasswortVergessenSession.getPasswort();
    }

    public void setPasswort(String passwort) {
        aControllerPasswortVergessenSession.setPasswort(passwort);
    }

    public String getPasswortBestaetigung() {
        return aControllerPasswortVergessenSession.getPasswortBestaetigung();
    }

    public void setPasswortBestaetigung(String passwortBestaetigung) {
        aControllerPasswortVergessenSession.setPasswortBestaetigung(passwortBestaetigung);
    }

    public String getBestaetigungsCode() {
        return aControllerPasswortVergessenSession.getBestaetigungsCode();
    }

    public void setBestaetigungsCode(String bestaetigungsCode) {
        aControllerPasswortVergessenSession.setBestaetigungsCode(bestaetigungsCode);
    }

    public String getFehlerMeldung() {
        return aControllerPasswortVergessenSession.getFehlerMeldung();
    }

    public void setFehlerMeldung(String fehlerMeldung) {
        aControllerPasswortVergessenSession.setFehlerMeldung(fehlerMeldung);
    }

    public int getFehlerNr() {
        return aControllerPasswortVergessenSession.getFehlerNr();
    }

    public void setFehlerNr(int fehlerNr) {
        aControllerPasswortVergessenSession.setFehlerNr(fehlerNr);
    }

}
