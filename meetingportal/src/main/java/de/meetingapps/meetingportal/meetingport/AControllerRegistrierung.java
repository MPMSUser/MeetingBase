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

import de.meetingapps.meetingportal.meetComAllg.CaDatumZeit;
import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComAllg.CaPasswortVerschluesseln;
import de.meetingapps.meetingportal.meetComAllg.CaString;
import de.meetingapps.meetingportal.meetComBa.BaMailM;
import de.meetingapps.meetingportal.meetComBl.BlEinsprungLinkPortal;
import de.meetingapps.meetingportal.meetComEclM.EclDbM;
import de.meetingapps.meetingportal.meetComEclM.EclParamM;
import de.meetingapps.meetingportal.meetComEclM.EclPortalTexteM;
import de.meetingapps.meetingportal.meetComEclM.EclPublikationListeM;
import de.meetingapps.meetingportal.meetComEclM.EclPublikationM;
import de.meetingapps.meetingportal.meetComEclM.EclTeilnehmerLoginM;
import de.meetingapps.meetingportal.meetComEntities.EclAktienregisterLoginDaten;
import de.meetingapps.meetingportal.meetComEntities.EclAktienregisterZusatz;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@RequestScoped
@Named
@Deprecated
public class AControllerRegistrierung {

    @Inject
    EclDbM eclDbM;
    @Inject
    private ADlgVariablen aDlgVariablen;
    @Inject
    private EclPortalTexteM eclTextePortalM;

    @Inject
    private EclTeilnehmerLoginM eclTeilnehmerLoginM;
    @Inject
    private EclPublikationListeM eclPublikationListeM;

    @Inject
    private AFunktionen aFunktionen;
    @Inject
    private BaMailM baMailm;
    @Inject
    EclParamM eclParamM;
    @Inject
    AControllerRegistrierungSession aControllerRegistrierungSession;

    /**Voraussetzung: eclDbM gefüllt und offen.
     * Ergebnis: eclPublikationisteM gefüllt.
     */
    public void initPublikationListe() {
        List<EclPublikationM> lPublikationListeM = new LinkedList<>();

        int anz, i, i1, i2;
        anz = eclDbM.getDbBundle().dbPublikation.read_all();
        for (i = 0; i < anz; i++) {
            boolean zulaessig = false;
            for (i1 = 0; i1 < 10; i1++) {
                if (eclDbM.getDbBundle().dbPublikation.ergebnisPosition(i).publikationenZustellung[i1] != 0) {
                    zulaessig = true;
                }
            }
            if (zulaessig == true) {
                EclPublikationM lPublikationM = new EclPublikationM();
                lPublikationM.copyFrom(eclDbM.getDbBundle().dbPublikation.ergebnisPosition(i));

                /*Nun noch die selektierten Wege markieren*/
                int selektionen = eclTeilnehmerLoginM.getAktienregisterZusatzM()
                        .getPublikationenZustellung()[lPublikationM.getPosition()];
                int selektionspotenz = 1;
                for (i2 = 0; i2 < 10; i2++) {
                    if ((selektionen & selektionspotenz) == selektionspotenz) {
                        lPublikationM.getPublikationAngefordert()[i2] = true;
                    }
                    selektionspotenz = selektionspotenz * 2;
                }

                lPublikationListeM.add(lPublikationM);
            }
        }

        eclPublikationListeM.setPublikationListeM(lPublikationListeM);
    }

    public String doAbmelden() {
        aDlgVariablen.clearLogin();
        return aFunktionen.waehleLogout();
    }

    /*Die folgenden Funktionen werden aufgerufen aus den verschiedenen Masken - und verweisen alle auf doEinstellungen*/
    public String doAnmeldenEinstellungen() {
        String naechsteMaske = "";
        if (!aFunktionen.pruefeStart("aAnmelden")) {
            return "aDlgFehler";
        }

        naechsteMaske = doEinstellungen();
        return aFunktionen.setzeEnde(naechsteMaske, true, false);
    }

    /*Die folgenden Funktionen werden aufgerufen aus den verschiedenen Masken - und verweisen alle auf doEinstellungen*/
    public String doStatusEinstellungen() {
        String naechsteMaske = "";
        if (!aFunktionen.pruefeStart("aStatus")) {
            return "aDlgFehler";
        }

        aFunktionen.setzeEnde("aEinstellungen", true, true);

        naechsteMaske = doEinstellungen();
        aFunktionen.setzeEnde();
        return naechsteMaske;
    }

    /*Die folgenden Funktionen werden aufgerufen aus den verschiedenen Masken - und verweisen alle auf doEinstellungen*/
    public String doStatus0BestandEinstellungen() {
        String naechsteMaske = "";
        if (!aFunktionen.pruefeStart("aStatus0Bestand")) {
            return "aDlgFehler";
        }

        aFunktionen.setzeEnde("aEinstellungen", true, true);

        naechsteMaske = doEinstellungen();
        aFunktionen.setzeEnde();
        return naechsteMaske;
    }

    /*Die folgenden Funktionen werden aufgerufen aus den verschiedenen Masken - und verweisen alle auf doEinstellungen*/
    public String doAuswahl1Einstellungen() {
        String naechsteMaske = "";
        if (!aFunktionen.pruefeStart("aAuswahl1")) {
            return "aDlgFehler";
        }

        aFunktionen.setzeEnde("aEinstellungen", true, true);

        naechsteMaske = doEinstellungen();
        aFunktionen.setzeEnde();
        return naechsteMaske;
    }

    /*Die folgenden Funktionen werden aufgerufen aus den verschiedenen Masken - und verweisen alle auf doEinstellungen*/
    public String doFehlerEinstellungen() {
        String naechsteMaske = "";
        if (!aFunktionen.pruefeStart("aRegistrierung")) {
            return "aDlgFehler";
        }

        aFunktionen.setzeEnde("aEinstellungen", true, true);

        naechsteMaske = doEinstellungen();
        aFunktionen.setzeEnde();
        return naechsteMaske;
    }

    /*Funktion wird aufgerufen aus do*Einstellungen, um Maske Einstellungen aufzurufen*/
    public String doEinstellungen() {
        aControllerRegistrierungSession.clearBestaetigungsseite();
        eclDbM.openAll();
        aControllerRegistrierungSession.setErstregistrierung(false);
        aDlgVariablen.clearDlg();
        initPublikationListe();
        pruefeErstregistrierung();
        eclDbM.closeAll();
        aDlgVariablen.setErstRegistrierungOderEinstellungenAktiv(2);
        aFunktionen.setzeEnde();
        return "aEinstellungen";
    }

    /*Funktion wird nach Login aufgerufen, bevor erst-Registrierungsseite aufgerufen wird*/
    public void startRegistrierung() {
        aControllerRegistrierungSession.setErstregistrierung(true);

    }

    /**Aus Maske aEinstellungen oder aRegistrierung*/
    public String doEmailLink() {
        if (aDlgVariablen.getErstRegistrierungOderEinstellungenAktiv() == 2) {
            if (!aFunktionen.pruefeStart("aEinstellungen")) {
                return "aDlgFehler";
            }
        } else {
            if (!aFunktionen.pruefeStart("aRegistrierung")) {
                return "aDlgFehler";
            }
        }
        aControllerRegistrierungSession.seteMailBestaetigungVerschicken(true);
        aControllerRegistrierungSession.seteMail2BestaetigungVerschicken(false);
        pruefenUndSpeichern();
        eclDbM.openAll();
        pruefeErstregistrierung();
        eclDbM.closeAll();
        aDlgVariablen.setFehlerNr(CaFehler.afEMailWurdeErneutVerschickt);
        aDlgVariablen.setFehlerMeldung(eclTextePortalM.getFehlertext(CaFehler.afEMailWurdeErneutVerschickt));

        String naechsteMaske = "";
        if (aDlgVariablen.getErstRegistrierungOderEinstellungenAktiv() == 2) {
            naechsteMaske = "aEinstellungen";
        } else {
            naechsteMaske = "aRegistrierung";
        }
        aFunktionen.setzeEnde(naechsteMaske, false, false);
        return naechsteMaske;

        /*aFunktionen.setzeEnde();return ""*/
    }

    /**Für Maske Erstregistrierung.
     * Aus historischen Gründen noch vorhanden. Zukünftig immer doMailLink verwenden*/
    public String doEmailLinkErst() {
        return doEmailLink();
    }

    /**Aus Maske aEinstellungen oder aRegistrierung*/
    public String doEmail2Link() {
        if (aDlgVariablen.getErstRegistrierungOderEinstellungenAktiv() == 2) {
            if (!aFunktionen.pruefeStart("aEinstellungen")) {
                return "aDlgFehler";
            }
        } else {
            if (!aFunktionen.pruefeStart("aRegistrierung")) {
                return "aDlgFehler";
            }
        }
        aControllerRegistrierungSession.seteMailBestaetigungVerschicken(false);
        aControllerRegistrierungSession.seteMail2BestaetigungVerschicken(true);
        pruefenUndSpeichern();
        eclDbM.openAll();
        pruefeErstregistrierung();
        eclDbM.closeAll();
        aDlgVariablen.setFehlerNr(CaFehler.afEMailWurdeErneutVerschickt);
        aDlgVariablen.setFehlerMeldung(eclTextePortalM.getFehlertext(CaFehler.afEMailWurdeErneutVerschickt));
        aFunktionen.setzeEnde();
        return "";
    }

    /**Für Maske Erstregistrierung.
     * Aus historischen Gründen noch vorhanden. Zukünftig immer doMailLink verwenden*/
    public String doEmail2LinkErst() {
        return doEmail2Link();
    }

    /**Wird aufgerufen aus der Maske Registrieren oder aus der Maske aEinstellungen*/
    public String doRegistrierenWeiter() {
        String naechsteMaske = "";
        if (aDlgVariablen.getErstRegistrierungOderEinstellungenAktiv() == 2) {
            if (!aFunktionen.pruefeStart("aEinstellungen")) {
                return "aDlgFehler";
            }
        } else {
            if (!aFunktionen.pruefeStart("aRegistrierung")) {
                return "aDlgFehler";
            }
        }
        aDlgVariablen.clearFehlerMeldung();
        aControllerRegistrierungSession.seteMailBestaetigungVerschicken(false);
        aControllerRegistrierungSession.seteMail2BestaetigungVerschicken(false);
        naechsteMaske = pruefenUndSpeichern();
        if (!naechsteMaske.isEmpty()) {
            aFunktionen.setzeEnde(naechsteMaske, false, false);
        }
        aFunktionen.setzeEnde();
        return naechsteMaske;
    }

    /**Wird aufgerufen aus der Maske Einstellungen - aus historischen Gründe noch enthalten*/
    public String doEinstellungenWeiter() {
        return doRegistrierenWeiter();
    }

    /**
     * Eingabewerte (bzw. benötigte Wert):
     * eclTeilnehmerLoginM.aktienregisterZusatzM:
     * 
     * > eMailFuerVersand
     * > eMailFuerVersandBestaetigen
     * 
     * > eMail2FuerVersand
     * > eMail2FuerVersandBestaetigen
     * 
     * > eMailRegistrierungAnzeige
     *  
     * > neuesPasswort 
     * > neuesPasswortBestaetigung
     * 
     * > hinweisAktionaersPortalBestaetigtAnzeige
     * > hinweisHVPortalBestaetigtAnzeige
     * 
     * > kommunikationssprache
     * > kontaktTelefonPrivat
     * > kontaktTelefonGeschaeftlich
     * > kontaktTelefonMobil
     * > kontaktTelefonFax
     * 
     * aDlgVerfahren:
     * > ausgewaehltVergabeEigenesPasswort (Eingabewert!)
     * > passwortBereitsVergeben
     * > neuesPasswort (Eingabewert)
     * > anzeigeHinweisAktionaersPortalBestaetigen
     * > anzeigeHinweisHVPortalBestaetigen
     * 
     * aControllerRegistrierungSession:
     * > wurdeUeberAppAngefordert
     * > eMailBestaetigungVerschicken
     * > eMail2BestaetigungVerschicken
     * > eMailBestaetigungsCode
     * > eMail2BestaetigungsCode
     * 
     * Fehlermeldungen:
     * afKeineGueltigeEmailAdresse
     * afEmailAdresseBestaetigungWeichtAb
     * afKeineGueltigeEmail2Adresse
     * afEmail2AdresseBestaetigungWeichtAb
     * afEVersandErfordertPasswort
     * afPasswortFehlt
     * afPasswortZuKurz
     * afPasswortBestaetigungWeichtAb
     * afBestaetigungNutzungsbedingungenAktionaersPortalFehlt
     * afBestaetigungNutzungsbedingungenHVPortalFehlt
     * afEmailBestaetigungsCodeUnbekannt
     * afEmail2BestaetigungsCodeUnbekannt
     */
    public String pruefenUndSpeichern() {
        boolean aktienregisterZusatzIstNeu = false;
        /*Wenn true, dann wird Email-Bestätigungs-Link verschickt - auch wenn sich Email nicht verändert hat*/
        boolean emailBestaetigenMailVerschicken = false;
        boolean email2BestaetigenMailVerschicken = false;

        if (aControllerRegistrierungSession.iseMailBestaetigungVerschicken()) {
            emailBestaetigenMailVerschicken = true;
        } else {
            emailBestaetigenMailVerschicken = false;
        }
        if (aControllerRegistrierungSession.iseMail2BestaetigungVerschicken()) {
            email2BestaetigenMailVerschicken = true;
        } else {
            email2BestaetigenMailVerschicken = false;
        }

        /*Eingaben überprüfen*/

        /*TODO nicht vollständig implementiert - nur für ku111 derzeit!*/
        if (eclParamM.getParam().paramPortal.emailNurBeiEVersandOderPasswort == 1) {
            if (!eclTeilnehmerLoginM.getAktienregisterZusatzM().geteMailFuerVersand().isEmpty()) {
                if (!eclTeilnehmerLoginM.getAktienregisterZusatzM().iseMailRegistrierungAnzeige()
                        && !aDlgVariablen.isAusgewaehltVergabeEigenesPasswort()) {
                    aDlgVariablen.setFehlerNr(CaFehler.afPasswortEMailNichtZulaessig);
                    aDlgVariablen
                            .setFehlerMeldung(eclTextePortalM.getFehlertext(CaFehler.afPasswortEMailNichtZulaessig));
                    aFunktionen.setzeEnde();
                    return "";

                }
            }
        }

        /*Falls Email-Adresse eingegeben, dann überprüfen*/
        if (!eclTeilnehmerLoginM.getAktienregisterZusatzM().geteMailFuerVersand().isEmpty()) {
            /*Email-Adresse gültig?*/
            if (CaString.isMailadresse(eclTeilnehmerLoginM.getAktienregisterZusatzM().geteMailFuerVersand()) == false) {
                aDlgVariablen.setFehlerNr(CaFehler.afKeineGueltigeEmailAdresse);
                aDlgVariablen.setFehlerMeldung(eclTextePortalM.getFehlertext(CaFehler.afKeineGueltigeEmailAdresse));
                aFunktionen.setzeEnde();
                return "";
            }
            /*Email-Bestätigungs-Adresse gleich?*/
            if (eclTeilnehmerLoginM.getAktienregisterZusatzM().geteMailFuerVersand()
                    .compareTo(eclTeilnehmerLoginM.getAktienregisterZusatzM().geteMailFuerVersandBestaetigen()) != 0) {
                aDlgVariablen.setFehlerNr(CaFehler.afEmailAdresseBestaetigungWeichtAb);
                aDlgVariablen
                        .setFehlerMeldung(eclTextePortalM.getFehlertext(CaFehler.afEmailAdresseBestaetigungWeichtAb));
                aFunktionen.setzeEnde();
                return "";
            }
        }
        /*Falls Email2-Adresse eingegeben, dann überprüfen*/
        if (!eclTeilnehmerLoginM.getAktienregisterZusatzM().geteMail2FuerVersand().isEmpty()) {
            /*Email2-Adresse gültig?*/
            if (CaString
                    .isMailadresse(eclTeilnehmerLoginM.getAktienregisterZusatzM().geteMail2FuerVersand()) == false) {
                aDlgVariablen.setFehlerNr(CaFehler.afKeineGueltigeEmail2Adresse);
                aDlgVariablen.setFehlerMeldung(eclTextePortalM.getFehlertext(CaFehler.afKeineGueltigeEmail2Adresse));
                aFunktionen.setzeEnde();
                return "";
            }
            /*Email-Bestätigungs-Adresse gleich?*/
            if (eclTeilnehmerLoginM.getAktienregisterZusatzM().geteMail2FuerVersand()
                    .compareTo(eclTeilnehmerLoginM.getAktienregisterZusatzM().geteMail2FuerVersandBestaetigen()) != 0) {
                aDlgVariablen.setFehlerNr(CaFehler.afEmail2AdresseBestaetigungWeichtAb);
                aDlgVariablen
                        .setFehlerMeldung(eclTextePortalM.getFehlertext(CaFehler.afEmail2AdresseBestaetigungWeichtAb));
                aFunktionen.setzeEnde();
                return "";
            }
        }
        /*Falls für Email-Versand registriert - dann Email-Adresse hinterlegt?*/
        if (eclTeilnehmerLoginM.getAktienregisterZusatzM().iseMailRegistrierungAnzeige()
                && eclTeilnehmerLoginM.getAktienregisterZusatzM().geteMailFuerVersand().isEmpty()) {
            aDlgVariablen.setFehlerNr(CaFehler.afKeineGueltigeEmailAdresse);
            aDlgVariablen.setFehlerMeldung(eclTextePortalM.getFehlertext(CaFehler.afKeineGueltigeEmailAdresse));
            aFunktionen.setzeEnde();
            return "";
        }

        /*Falls für Email-Versand registriert, und es erforderlich ist, dann dauerhaftes Passwort vergeben?*/
        if (eclTeilnehmerLoginM.getAktienregisterZusatzM().iseMailRegistrierungAnzeige()
                && eclParamM.getParam().paramPortal.dauerhaftesPasswortMoeglich == 2) {
            if (aDlgVariablen.isAusgewaehltVergabeEigenesPasswort() == false) {
                aDlgVariablen.setFehlerNr(CaFehler.afEVersandErfordertPasswort);
                aDlgVariablen.setFehlerMeldung(eclTextePortalM.getFehlertext(CaFehler.afEVersandErfordertPasswort));
                aFunktionen.setzeEnde();
                return "";
            }
        }

        /*Falls eigenes Passwort gewählt ...*/
        if (aDlgVariablen.isAusgewaehltVergabeEigenesPasswort() /*Checkbox ist ausgewählt - Grundvoraussetzung*/
                && (/*Passwort ist bereits vergeben - nur Checken, wenn Änderung*/
                (aDlgVariablen.isPasswortBereitsVergeben() && aDlgVariablen.isNeuesPasswort()) ||
                /*Passwort noch nicht vergeben - dann immer prüfen, da neues passwort*/
                        !aDlgVariablen.isPasswortBereitsVergeben())) {
            //		alte Bedingungen - nach Tests löschen
            //			if ((aDlgVariablen.isAusgewaehltVergabeEigenesPasswort() && 
            //			 aControllerRegistrierungSession.isErstregistrierung()==true) ||
            //				(aControllerRegistrierungSession.isErstregistrierung()==false && (
            //						(!aDlgVariablen.isPasswortBereitsVergeben() && aDlgVariablen.isAusgewaehltVergabeEigenesPasswort()) ||
            //						(aDlgVariablen.isPasswortBereitsVergeben() && aDlgVariablen.isNeuesPasswort())
            //						)
            //			 	)
            //				){
            /*Überhaupt Passwort eingegeben?*/
            if (eclTeilnehmerLoginM.getAktienregisterZusatzM().getNeuesPasswort().isEmpty()) {
                aDlgVariablen.setFehlerNr(CaFehler.afPasswortFehlt);
                aDlgVariablen.setFehlerMeldung(eclTextePortalM.getFehlertext(CaFehler.afPasswortFehlt));
                aFunktionen.setzeEnde();
                return "";
            }
            /*Passwort lang genug*/
            if (eclTeilnehmerLoginM.getAktienregisterZusatzM().getNeuesPasswort()
                    .length() < eclParamM.getParam().paramPortal.passwortMindestLaenge) {
                aDlgVariablen.setFehlerNr(CaFehler.afPasswortZuKurz);
                aDlgVariablen.setFehlerMeldung(eclTextePortalM.getFehlertext(CaFehler.afPasswortZuKurz));
                aFunktionen.setzeEnde();
                return "";
            }
            /*Passwort-Bestätigung gleich?*/
            if (eclTeilnehmerLoginM.getAktienregisterZusatzM().getNeuesPasswort()
                    .compareTo(eclTeilnehmerLoginM.getAktienregisterZusatzM().getNeuesPasswortBestaetigung()) != 0) {
                aDlgVariablen.setFehlerNr(CaFehler.afPasswortBestaetigungWeichtAb);
                aDlgVariablen.setFehlerMeldung(eclTextePortalM.getFehlertext(CaFehler.afPasswortBestaetigungWeichtAb));
                aFunktionen.setzeEnde();
                return "";
            }
            /*Email-Adresse hinterlegt?*/
            if (eclTeilnehmerLoginM.getAktienregisterZusatzM().geteMailFuerVersand().isEmpty()) {
                aDlgVariablen.setFehlerNr(CaFehler.afKeineGueltigeEmailAdresse);
                aDlgVariablen.setFehlerMeldung(eclTextePortalM.getFehlertext(CaFehler.afKeineGueltigeEmailAdresse));
                aFunktionen.setzeEnde();
                return "";
            }

        }

        if (aDlgVariablen.isAnzeigeHinweisAktionaersPortalBestaetigen()) {
            /*Aktionärsportal bestätigt?*/
            if (!eclTeilnehmerLoginM.getAktienregisterZusatzM().isHinweisAktionaersPortalBestaetigtAnzeige()) {
                aDlgVariablen.setFehlerNr(CaFehler.afBestaetigungNutzungsbedingungenAktionaersPortalFehlt);
                aDlgVariablen.setFehlerMeldung(
                        eclTextePortalM.getFehlertext(CaFehler.afBestaetigungNutzungsbedingungenAktionaersPortalFehlt));
                aFunktionen.setzeEnde();
                return "";
            }
        }
        if (aDlgVariablen.isAnzeigeHinweisHVPortalBestaetigen()) {
            /*HV-Portal bestätigt?*/
            if (!eclTeilnehmerLoginM.getAktienregisterZusatzM().isHinweisHVPortalBestaetigtAnzeige()) {
                aDlgVariablen.setFehlerNr(CaFehler.afBestaetigungNutzungsbedingungenHVPortalFehlt);
                aDlgVariablen.setFehlerMeldung(
                        eclTextePortalM.getFehlertext(CaFehler.afBestaetigungNutzungsbedingungenHVPortalFehlt));
                aFunktionen.setzeEnde();
                return "";
            }
        }

        /**E-MailBestätigungscode*/
        if (!aControllerRegistrierungSession.geteMailBestaetigungsCode().isEmpty()) {
            String hString = aControllerRegistrierungSession.geteMailBestaetigungsCode().trim();
            if (hString.compareTo(eclTeilnehmerLoginM.getAktienregisterZusatzM().getEmailBestaetigenLink()) != 0) {
                aDlgVariablen.setFehlerNr(CaFehler.afEmailBestaetigungsCodeUnbekannt);
                aDlgVariablen
                        .setFehlerMeldung(eclTextePortalM.getFehlertext(CaFehler.afEmailBestaetigungsCodeUnbekannt));
                aFunktionen.setzeEnde();
                return "";
            }
        }
        /**E-MailBestätigungscode*/
        if (!aControllerRegistrierungSession.geteMail2BestaetigungsCode().isEmpty()) {
            String hString = aControllerRegistrierungSession.geteMail2BestaetigungsCode().trim();
            if (hString.compareTo(eclTeilnehmerLoginM.getAktienregisterZusatzM().getEmail2BestaetigenLink()) != 0) {
                aDlgVariablen.setFehlerNr(CaFehler.afEmail2BestaetigungsCodeUnbekannt);
                aDlgVariablen
                        .setFehlerMeldung(eclTextePortalM.getFehlertext(CaFehler.afEmail2BestaetigungsCodeUnbekannt));
                aFunktionen.setzeEnde();
                return "";
            }
        }

        /******Verarbeiten******/

        EclAktienregisterZusatz lAktienregisterZusatz = new EclAktienregisterZusatz();
        eclTeilnehmerLoginM.getAktienregisterZusatzM().copyTo(lAktienregisterZusatz);
        if (lAktienregisterZusatz.aktienregisterIdent == 0) {
            /*Bisher noch kein Datensatz in DB enthalten - Ersterzeugung!*/
            /*TODO #App: Achtung: hier fehlt noch für "Nicht-Aktienregister-Sätze!!!!!!!!!!!!!!!!!!!*/
            lAktienregisterZusatz.aktienregisterIdent = eclTeilnehmerLoginM.getAnmeldeIdentAktienregister();
            aktienregisterZusatzIstNeu = true;
        }

        eclDbM.openAll();

        /**!=null => alter Stand ist vorhanden und eingelesen, zum Abgleich was sich verändert hat (setzen
         * der entsprechenden Variablen in AControllerRegistrierungSession für Bestätigungsseite)
         */
        EclAktienregisterZusatz lAktienregisterZusatzAlterStand = null;

        /*Prüfen, ob mittlerweile von anderem User verändert*/
        EclAktienregisterZusatz tAktienregisterZusatz = new EclAktienregisterZusatz();
        boolean dbSchreibenMoeglich = false;
        if (aktienregisterZusatzIstNeu) {
            tAktienregisterZusatz.aktienregisterIdent = eclTeilnehmerLoginM.getAnmeldeIdentAktienregister();
            dbSchreibenMoeglich = eclDbM.getDbBundle().dbAktienregisterZusatz.insertCheck(tAktienregisterZusatz);
        } else {
            eclTeilnehmerLoginM.getAktienregisterZusatzM().copyTo(tAktienregisterZusatz);
            dbSchreibenMoeglich = eclDbM.getDbBundle().dbAktienregisterZusatz.updateCheck(tAktienregisterZusatz);
            if (dbSchreibenMoeglich) {
                lAktienregisterZusatzAlterStand = eclDbM.getDbBundle().dbAktienregisterZusatz.ergebnisPosition(0);
            }
        }
        if (dbSchreibenMoeglich == false) {
            eclDbM.closeAllAbbruch();
            aDlgVariablen.setFehlerMeldung(eclTextePortalM.getFehlertext(CaFehler.afAndererUserAktiv));
            aDlgVariablen.setFehlerNr(CaFehler.afAndererUserAktiv);
            return "aDlgAbbruch";
        }

        /*Hinweise Aktionärsportal gelesen*/
        if (eclTeilnehmerLoginM.getAktienregisterZusatzM().isHinweisAktionaersPortalBestaetigtAnzeige()) {
            lAktienregisterZusatz.hinweisAktionaersPortalBestaetigt = 1;
        }

        /*Hinweise HVPortal gelesen*/
        if (eclTeilnehmerLoginM.getAktienregisterZusatzM().isHinweisHVPortalBestaetigtAnzeige()) {
            lAktienregisterZusatz.hinweisHVPortalBestaetigt = 1;
        }

        /*Newsletter-Registrierung*/
        if (eclTeilnehmerLoginM.getAktienregisterZusatzM().iseMailRegistrierungAnzeige()) {
            lAktienregisterZusatz.eMailRegistrierung = 99;
            if (lAktienregisterZusatz.eMailRegistrierungErstZeitpunkt.isEmpty()) {
                /*Aktuelle Systemzeit = Erstregistrierungszeitpunkt*/
                lAktienregisterZusatz.eMailRegistrierungErstZeitpunkt = CaDatumZeit.DatumZeitStringFuerDatenbank();
            }
        } else {
            lAktienregisterZusatz.eMailRegistrierung = 1;
        }

        /*Passwort*/
        if (aDlgVariablen.isAusgewaehltVergabeEigenesPasswort()) {
            /*Dann ist weiterhin ein eigenes Passwort gespeichert*/
            lAktienregisterZusatz.eigenesPasswort = 99;

            if (/*Passwort ist bereits vergeben - nur neu Speichern, wenn Änderung*/
            (aDlgVariablen.isPasswortBereitsVergeben() && aDlgVariablen.isNeuesPasswort()) ||
            /*Passwort noch nicht vergeben - dann neues passwort*/
                    !aDlgVariablen.isPasswortBereitsVergeben()) {

                //			Alt - nach den Tests löschen.
                //				if ((aDlgVariablen.isAusgewaehltVergabeEigenesPasswort() && aControllerRegistrierungSession.isErstregistrierung()==true) ||
                //					(aControllerRegistrierungSession.isErstregistrierung()==false && (
                //							(!aDlgVariablen.isPasswortBereitsVergeben() && aDlgVariablen.isAusgewaehltVergabeEigenesPasswort()) ||
                //							(aDlgVariablen.isPasswortBereitsVergeben() && aDlgVariablen.isNeuesPasswort())
                //							)
                //							)
                //					){

                /*Dann wurde ein neues Passwort eingegeben, das nun gespeichert werden muß!*/

                /*Passwort übertragen*/
                EclAktienregisterLoginDaten lAktienregisterLoginDaten = new EclAktienregisterLoginDaten();
                lAktienregisterLoginDaten.aktienregisterIdent = eclTeilnehmerLoginM.getAnmeldeIdentAktienregister();
                eclDbM.getDbBundle().dbAktienregisterLoginDaten.read(lAktienregisterLoginDaten);
                lAktienregisterLoginDaten = eclDbM.getDbBundle().dbAktienregisterLoginDaten.ergebnisPosition(0);
                lAktienregisterLoginDaten.passwortVerschluesselt = CaPasswortVerschluesseln
                        .verschluesseln(eclTeilnehmerLoginM.getAktienregisterZusatzM().getNeuesPasswort());

                /*Verschlüsseltes Passwort für Rückgabe an App*/
                aControllerRegistrierungSession
                        .setPasswortVerschluesselt(lAktienregisterLoginDaten.passwortVerschluesselt);

                lAktienregisterLoginDaten.passwortInitial = "";
                eclDbM.getDbBundle().dbAktienregisterLoginDaten.update(lAktienregisterLoginDaten);
            }
        } else {/*Kein eigenes Passwort (mehr), aber wurde bereits abgefragt - setzen*/
            lAktienregisterZusatz.eigenesPasswort = 1;
        }

        /*Email-Bestätigungscode wurde eingegeben?*/
        /**E-MailBestätigungscode*/
        if (!aControllerRegistrierungSession.geteMailBestaetigungsCode().isEmpty()) {
            lAktienregisterZusatz.emailBestaetigt = 1;
        }
        /**E-Mail2Bestätigungscode*/
        if (!aControllerRegistrierungSession.geteMail2BestaetigungsCode().isEmpty()) {
            lAktienregisterZusatz.email2Bestaetigt = 1;
        }

        /*Email-Adresse*/
        if (!lAktienregisterZusatz.eMailFuerVersand.isEmpty()) {
            if (lAktienregisterZusatz.eMailFuerVersand
                    .compareTo(eclTeilnehmerLoginM.getAktienregisterZusatzM().geteMailFuerVersandAlt()) != 0) {
                emailBestaetigenMailVerschicken = true;
                lAktienregisterZusatz.emailBestaetigt = 0;
            }
            if (emailBestaetigenMailVerschicken == true) {
                /*Email-Bestätigung veranlassen*/
                String pwVergessenLink = eclDbM.getDbBundle().dbEindeutigerKey.getNextFree();
                lAktienregisterZusatz.emailBestaetigenLink = pwVergessenLink;
            }
        }

        /*Email-Adresse 2*/
        if (!lAktienregisterZusatz.eMail2FuerVersand.isEmpty()) {
            if (lAktienregisterZusatz.eMail2FuerVersand
                    .compareTo(eclTeilnehmerLoginM.getAktienregisterZusatzM().geteMail2FuerVersandAlt()) != 0) {
                email2BestaetigenMailVerschicken = true;
                lAktienregisterZusatz.email2Bestaetigt = 0;
            }
            if (email2BestaetigenMailVerschicken == true) {
                /*Email-Bestätigung veranlassen*/
                String pwVergessenLink = eclDbM.getDbBundle().dbEindeutigerKey.getNextFree();
                lAktienregisterZusatz.email2BestaetigenLink = pwVergessenLink;
            }
        }

        /*Publikationen*/
        if (eclPublikationListeM.getPublikationListeM() != null) {
            int i, i1;
            for (i = 0; i < eclPublikationListeM.getPublikationListeM().size(); i++) {
                EclPublikationM lPublikationM = eclPublikationListeM.getPublikationListeM().get(i);

                int selektionen = 0;
                int selektionspotenz = 1;
                for (i1 = 0; i1 < 10; i1++) {
                    if (lPublikationM.getPublikationAngefordert()[i1] == true) {
                        selektionen = (selektionen | selektionspotenz);
                        selektionspotenz = selektionspotenz * 2;
                    }
                }
                lAktienregisterZusatz.publikationenZustellung[lPublikationM.getPosition()] = selektionen;

            }
        }

        /*In DB schreiben*/
        if (aktienregisterZusatzIstNeu) {
            eclDbM.getDbBundle().dbAktienregisterZusatz.insert(lAktienregisterZusatz);
        } else {
            eclDbM.getDbBundle().dbAktienregisterZusatz.update(lAktienregisterZusatz);

        }

        /*Nun mit Alt-Satz vergleichen und Variablen für Bestätigungsseite setzen*/

        if (lAktienregisterZusatzAlterStand == null) {
            if (lAktienregisterZusatz.eMailRegistrierung == 99) {
                aControllerRegistrierungSession.seteMailVersandNeuRegistriert(true);
                aControllerRegistrierungSession.setEmailNochNichtBestaetigt(true);
            }
            if (lAktienregisterZusatz.eigenesPasswort == 99) {
                aControllerRegistrierungSession.setDauerhaftesPasswortAktiviert(true);
            }
            if (!lAktienregisterZusatz.eMailFuerVersand.isEmpty()) {
                aControllerRegistrierungSession.setNeueEmailAdresseEingetragen(true);
                aControllerRegistrierungSession.setNeueEmailAdresse(lAktienregisterZusatz.eMailFuerVersand);
            }
            if (!lAktienregisterZusatz.eMail2FuerVersand.isEmpty()) {
                aControllerRegistrierungSession.setNeueEmail2AdresseEingetragen(true);
                aControllerRegistrierungSession.setNeueEmail2Adresse(lAktienregisterZusatz.eMail2FuerVersand);
            }
        } else {
            if (lAktienregisterZusatz.eMailRegistrierung != lAktienregisterZusatzAlterStand.eMailRegistrierung) {
                if (lAktienregisterZusatz.eMailRegistrierung == 99) {
                    aControllerRegistrierungSession.seteMailVersandNeuRegistriert(true);
                    aControllerRegistrierungSession.seteMailVersandDeRegistriert(false);
                } else {
                    aControllerRegistrierungSession.seteMailVersandDeRegistriert(true);
                    aControllerRegistrierungSession.seteMailVersandNeuRegistriert(false);
                }
            }

            if (lAktienregisterZusatz.eMailRegistrierung == 99) {
                if (lAktienregisterZusatzAlterStand.emailBestaetigt == 0) {
                    aControllerRegistrierungSession.setEmailNochNichtBestaetigt(true);
                }
            }

            if (lAktienregisterZusatz.eigenesPasswort != lAktienregisterZusatzAlterStand.eigenesPasswort) {
                if (lAktienregisterZusatz.eigenesPasswort == 99) {
                    aControllerRegistrierungSession.setDauerhaftesPasswortAktiviert(true);
                    aControllerRegistrierungSession.setDauerhaftesPasswortDeAktiviert(false);
                    aControllerRegistrierungSession.setDauerhaftesPasswortGeaendert(false);
                } else {
                    aControllerRegistrierungSession.setDauerhaftesPasswortAktiviert(false);
                    aControllerRegistrierungSession.setDauerhaftesPasswortDeAktiviert(true);
                    aControllerRegistrierungSession.setDauerhaftesPasswortGeaendert(false);
                }
            } else {
                if (aDlgVariablen.isNeuesPasswort()) {
                    aControllerRegistrierungSession.setDauerhaftesPasswortGeaendert(true);
                }
            }

            if (!lAktienregisterZusatz.eMailFuerVersand
                    .contentEquals(lAktienregisterZusatzAlterStand.eMailFuerVersand)) {
                if (lAktienregisterZusatz.eMailFuerVersand.isEmpty()) {
                    aControllerRegistrierungSession.setNeueEmailAdresseAusgetragen(true);
                    aControllerRegistrierungSession.setNeueEmailAdresseEingetragen(false);
                } else {
                    aControllerRegistrierungSession.setNeueEmailAdresseEingetragen(true);
                    aControllerRegistrierungSession.setNeueEmailAdresseAusgetragen(false);
                    aControllerRegistrierungSession.setNeueEmailAdresse(lAktienregisterZusatz.eMailFuerVersand);
                }
            }

            if (!lAktienregisterZusatz.eMail2FuerVersand
                    .contentEquals(lAktienregisterZusatzAlterStand.eMail2FuerVersand)) {
                if (lAktienregisterZusatz.eMail2FuerVersand.isEmpty()) {
                    aControllerRegistrierungSession.setNeueEmail2AdresseAusgetragen(true);
                    aControllerRegistrierungSession.setNeueEmail2AdresseEingetragen(false);
                } else {
                    aControllerRegistrierungSession.setNeueEmail2AdresseEingetragen(true);
                    aControllerRegistrierungSession.setNeueEmail2AdresseAusgetragen(false);
                    aControllerRegistrierungSession.setNeueEmail2Adresse(lAktienregisterZusatz.eMail2FuerVersand);
                }
            }

        }

        /*in eclTeilnehmerLoginM.getAktienregisterZusatzM() veränderten Satz hinterlegen*/
        eclTeilnehmerLoginM.getAktienregisterZusatzM().copyFrom(lAktienregisterZusatz);

        eclDbM.closeAll();

        eclDbM.openAll();
        if (emailBestaetigenMailVerschicken == true) {
            String sprache = "DE";
            if (eclParamM.getClGlobalVar().sprache == 2) {
                sprache = "EN";
            }
            BlEinsprungLinkPortal lBlEinsprungLinkPortal = new BlEinsprungLinkPortal(eclDbM.getDbBundle());
            //			aDlgVariablen.setEinsprungsLinkFuerEmail(lBlEinsprungLinkPortal.linkFuerEmailBestaetigung(lAktienregisterZusatz.emailBestaetigenLink, sprache, 1));
            //			aDlgVariablen.setEinsprungsLinkNurCode(lAktienregisterZusatz.emailBestaetigenLink);

            String hMailText = "", hBetreff = "";
            if (eclTextePortalM.portalNichtUeberNeueTexte()) {
             } else {
                if (!aControllerRegistrierungSession.isWurdeUeberAppAngefordert()) {
                    hBetreff = eclTextePortalM.holeText("220");
                    hMailText = eclTextePortalM.holeText("221");
                } else {
                    hBetreff = eclTextePortalM.holeText("282");
                    hMailText = eclTextePortalM.holeText("283");
                }
            }

            baMailm.senden(lAktienregisterZusatz.eMailFuerVersand, hBetreff, hMailText);
        }

        if (email2BestaetigenMailVerschicken == true) {
            String sprache = "DE";
            if (eclParamM.getClGlobalVar().sprache == 2) {
                sprache = "EN";
            }
            BlEinsprungLinkPortal lBlEinsprungLinkPortal = new BlEinsprungLinkPortal(eclDbM.getDbBundle());
            //			aDlgVariablen.setEinsprungsLinkFuerEmail(lBlEinsprungLinkPortal.linkFuerEmailBestaetigung(lAktienregisterZusatz.email2BestaetigenLink, sprache, 2));
            //			aDlgVariablen.setEinsprungsLinkNurCode(lAktienregisterZusatz.email2BestaetigenLink);

            String hMailText = "", hBetreff = "";
            if (eclTextePortalM.portalNichtUeberNeueTexte()) {
            } else {
                if (!aControllerRegistrierungSession.isWurdeUeberAppAngefordert()) {
                    hBetreff = eclTextePortalM.holeText("220");
                    hMailText = eclTextePortalM.holeText("221");
                } else {
                    hBetreff = eclTextePortalM.holeText("282");
                    hMailText = eclTextePortalM.holeText("283");
                }
            }

            baMailm.senden(lAktienregisterZusatz.eMailFuerVersand, hBetreff, hMailText);
        }

        if (esGibtVveraenderungenFuerBestaetigungsanzeige()
                && eclParamM.getParam().paramPortal.bestaetigungsseiteEinstellungen == 1) {
            /*Bestätigungsseite aufrufen*/
            aControllerRegistrierungSession.setEsGibtVveraenderungenFuerBestaetigungsanzeige(true);
            String naechsteMaske = "aRegistrierungBestaetigung";
            eclDbM.closeAll();
            aFunktionen.setzeEnde();
            return naechsteMaske;
        }

        /*Nächste Maske aufrufen - nach Einstellungen*/
        aControllerRegistrierungSession.setEsGibtVveraenderungenFuerBestaetigungsanzeige(false);
        String naechsteMaske = liefereMaskeNachEinstellungen();
        eclDbM.closeAll();
        aFunktionen.setzeEnde(naechsteMaske, true, false);
        return naechsteMaske;
    }

    /**eclDbM muß geöffnet sein!*/
    private String liefereMaskeNachEinstellungen() {
        String naechsteMaske = "aAnmelden";
        if (eclTeilnehmerLoginM.getAnmeldeKennungArt() == 1 /*Anmeldung mit Aktienregister-Kennung*/
                || eclTeilnehmerLoginM.getAnmeldeKennungArt() == 3 /*Anmeldung über Personen NatJur*/
        ) {
            naechsteMaske = aFunktionen.waehleAusgangsmaske(eclDbM.getDbBundle());
        }
        return naechsteMaske;
    }

    private boolean esGibtVveraenderungenFuerBestaetigungsanzeige() {
        if (aControllerRegistrierungSession.iseMailVersandNeuRegistriert()
                || aControllerRegistrierungSession.iseMailVersandDeRegistriert()
                || aControllerRegistrierungSession.isNeueEmailAdresseEingetragen()
                || aControllerRegistrierungSession.isNeueEmailAdresseAusgetragen()
                || aControllerRegistrierungSession.isNeueEmail2AdresseEingetragen()
                || aControllerRegistrierungSession.isNeueEmail2AdresseAusgetragen()
                || aControllerRegistrierungSession.isDauerhaftesPasswortAktiviert()
                || aControllerRegistrierungSession.isDauerhaftesPasswortDeAktiviert()
                || aControllerRegistrierungSession.isDauerhaftesPasswortGeaendert()
                || aControllerRegistrierungSession.isEmailNochNichtBestaetigt()) {
            return true;
        }
        return false;
    }

    /***********Buttons von aRegistrierungBestaetigung*******************************/
    public String doBestaetigungWeiter() {
        if (!aFunktionen.pruefeStart("aRegistrierungBestaetigung")) {
            return "aDlgFehler";
        }
        eclDbM.openAll();
        String naechsteMaske = liefereMaskeNachEinstellungen();
        eclDbM.closeAll();
        aFunktionen.setzeEnde(naechsteMaske, true, false);
        return naechsteMaske;
    }

    public String doBestaetigungZurueck() {
        if (!aFunktionen.pruefeStart("aRegistrierungBestaetigung")) {
            return "aDlgFehler";
        }
        String naechsteMaske = "";

        aFunktionen.setzeEnde("aEinstellungen", true, true);

        naechsteMaske = doEinstellungen();
        aFunktionen.setzeEnde();
        return naechsteMaske;
    }

    /***********Initialisierung für aRegistrierung und aEinstellungen**************************
     * Return-Wert:
     * > Prüft, ob die Erstregistrierungsseite aufgerufen werden muß, oder direkt zum HV-Teil gegangen werden kann
     * > true=Erstregistrierung
     * Ansonsten: Die für aRegistrierung/aEinstellungen benötigten, unten aufgeführten Variablen, werden
     * gesetzt.
     * 
     * Voraussetzung: eclTeilnehmerLoginM ist gefüllt.
     * 
     * Abhängig von:
     * > bereits Newsletterregistrierung angeboten?
     * > bereits eigenes Passwort angeboten?
     * > Disclaimer noch zu bestätigen?
     * 
     * Gesetzt sind anschließend:
     * > aDlgVariablen.anzeigeMeldung
     * > aDlgVariablen.anzeigeMeldungsText1
     * > aDlgVariablen.anzeigeMeldungsText2
     * 
     * > aDlgVariablen.emailbestaetigen (bei Erstregistrierung nicht gebraucht, dennoch auf false setzen)
     * > aDlgVariablen.email2bestaetigen (bei Erstregistrierung nicht gebraucht, dennoch auf false setzen)
     * 
     * > aDlgVariablen.passwortBereitsVergeben
     * > aDlgVariablen.ausgewaehltVergabeEigenesPasswort
     * > aDlgVariablen.neuesPasswort (ist immer false - Checkbox für Änderung)
     * 
     * > aDlgVariablen.anzeigeHinweisDatenschutzerklaerung  
     * > aDlgVariablen.anzeigeHinweisHVPortalBestaetigen
     * > aDlgVariablen.anzeigeHinweisAktionaersPortalBestaetigen
     * 
     * 
     */
    public boolean pruefeErstregistrierung() {
        aDlgVariablen.setAnzeigeMeldung(false);
        aDlgVariablen.setAnzeigeMeldungsText1(false);
        aDlgVariablen.setAnzeigeMeldungsText2(false);

        aDlgVariablen.setEmailbestaetigen(false);
        aDlgVariablen.setEmail2bestaetigen(false);

        aDlgVariablen.setPasswortBereitsVergeben(false);
        aDlgVariablen.setAusgewaehltVergabeEigenesPasswort(false);
        aDlgVariablen.setNeuesPasswort(false);

        aDlgVariablen.setAnzeigeHinweisDatenschutzerklaerung(false);
        aDlgVariablen.setAnzeigeHinweisHVPortalBestaetigen(false);
        aDlgVariablen.setAnzeigeHinweisAktionaersPortalBestaetigen(false);

        /*eclTeilnehmerLoginM.getAktienregisterZusatzM() neu einlesen, da möglicherweise mittlerweile verändert
         * (z.B. durch Email-Bestätigungs-Ablauf)
         */
        EclAktienregisterZusatz lAktienregisterZusatz = new EclAktienregisterZusatz();
        lAktienregisterZusatz.aktienregisterIdent = eclTeilnehmerLoginM.getAktienregisterZusatzM()
                .getAktienregisterIdent();
        if (lAktienregisterZusatz.aktienregisterIdent != 0) {
            eclDbM.getDbBundle().dbAktienregisterZusatz.read(lAktienregisterZusatz);
            if (eclDbM.getDbBundle().dbAktienregisterZusatz.anzErgebnis() == 1) {
                lAktienregisterZusatz = eclDbM.getDbBundle().dbAktienregisterZusatz.ergebnisPosition(0);
                eclTeilnehmerLoginM.getAktienregisterZusatzM().copyFrom(lAktienregisterZusatz);
            }
        }

        /*Meldungen anzeigen?*/
        if (eclTeilnehmerLoginM.getAktienregisterZusatzM().getEmailBestaetigt() == 0
                && (!eclTeilnehmerLoginM.getAktienregisterZusatzM().geteMailFuerVersand().isEmpty())) {
            /*Email-Adresse 1 vorhanden und noch nicht bestätigt?*/
            aDlgVariablen.setAnzeigeMeldung(true);
            aDlgVariablen.setAnzeigeMeldungsText1(true);
            aDlgVariablen.setEmailbestaetigen(true);
        }
        if (eclTeilnehmerLoginM.getAktienregisterZusatzM().getEmail2Bestaetigt() == 0
                && (!eclTeilnehmerLoginM.getAktienregisterZusatzM().geteMail2FuerVersand().isEmpty())) {
            /*Email-Adresse 2 vorhanden und noch nicht bestätigt?*/
            aDlgVariablen.setAnzeigeMeldung(true);
            aDlgVariablen.setAnzeigeMeldungsText2(true);
            aDlgVariablen.setEmail2bestaetigen(true);
        }

        /*Passwort Registrierung*/
        if (eclTeilnehmerLoginM.getAktienregisterZusatzM().getEigenesPasswort() == 99
                || eclTeilnehmerLoginM.getAktienregisterZusatzM().getEigenesPasswort() == 98) {
            /*Bereits eigenes Passwort vergeben*/
            aDlgVariablen.setPasswortBereitsVergeben(true);
            aDlgVariablen.setAusgewaehltVergabeEigenesPasswort(true);
        }

        /*Hinweis Aktionärs Portal anzuzeigen?*/
        if ((eclParamM.getParam().paramPortal.bestaetigenHinweisAktionaersportal != 0
                && (eclTeilnehmerLoginM.getAktienregisterZusatzM().getHinweisAktionaersPortalBestaetigt() == 0
                        || eclTeilnehmerLoginM.getAktienregisterZusatzM().getHinweisAktionaersPortalBestaetigt() == 2))
                || eclParamM
                        .getParam().paramPortal.bestaetigenHinweisAktionaersportal == 2 /*Dann immer bestätigen bei jeder Anmeldung!*/
        ) {
            aDlgVariablen.setAnzeigeHinweisAktionaersPortalBestaetigen(true);
        }

        /*Hinweis HV Portal anzuzeigen?*/
        if ((eclParamM.getParam().paramPortal.bestaetigenHinweisHVportal != 0
                && (eclTeilnehmerLoginM.getAktienregisterZusatzM().getHinweisHVPortalBestaetigt() == 0
                        || eclTeilnehmerLoginM.getAktienregisterZusatzM().getHinweisHVPortalBestaetigt() == 2))
                || eclParamM
                        .getParam().paramPortal.bestaetigenHinweisHVportal == 2 /*Dann immer bestätigen bei jeder Anmeldung!*/
        ) {
            aDlgVariablen.setAnzeigeHinweisHVPortalBestaetigen(true);
        }

        /*Falls einer der Hinweise (Portal oder hV) zu bestätigen ist, dann auch Datenschutzerklärungshinweis anzeigen*/
        if ((aDlgVariablen.isAnzeigeHinweisAktionaersPortalBestaetigen() == true
                || aDlgVariablen.isAnzeigeHinweisHVPortalBestaetigen() == true)
                && eclParamM.getParam().paramPortal.separateDatenschutzerklaerung == 1) {
            aDlgVariablen.setAnzeigeHinweisDatenschutzerklaerung(true);
        }

        /*Checken, ob irgendwas auf der Registrierungsmaske angezeigt werden soll - nur dann die Maske selbst aufrufen, ansonsten überspringen*/
        if (aDlgVariablen.isAnzeigeMeldung() == true || aDlgVariablen.isAnzeigeHinweisDatenschutzerklaerung() == true
                || aDlgVariablen.isAnzeigeHinweisAktionaersPortalBestaetigen() == true
                || aDlgVariablen.isAnzeigeHinweisHVPortalBestaetigen() == true) {
            return true;
        }
        return false;
    }

}
