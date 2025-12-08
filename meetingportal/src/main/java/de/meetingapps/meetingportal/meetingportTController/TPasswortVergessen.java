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

import java.util.List;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaDatumZeit;
import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComAllg.CaPasswortErzeugen;
import de.meetingapps.meetingportal.meetComAllg.CaPasswortVerschluesseln;
import de.meetingapps.meetingportal.meetComAllg.CaString;
import de.meetingapps.meetingportal.meetComBa.BaMailM;
import de.meetingapps.meetingportal.meetComBl.BlEinsprungLinkPortal;
import de.meetingapps.meetingportal.meetComBl.BlTeilnehmerLoginNeu;
import de.meetingapps.meetingportal.meetComBrM.BrMARRemoteZuARLokal;
import de.meetingapps.meetingportal.meetComBrM.BrMAktionaersdaten;
import de.meetingapps.meetingportal.meetComBrM.BrMInit;
import de.meetingapps.meetingportal.meetComBrM.BrMLogin;
import de.meetingapps.meetingportal.meetComEclM.EclDbM;
import de.meetingapps.meetingportal.meetComEclM.EclParamM;
import de.meetingapps.meetingportal.meetComEclM.EclPortalTexteM;
import de.meetingapps.meetingportal.meetComEntities.EclAktienregisterErgaenzung;
import de.meetingapps.meetingportal.meetComEntities.EclAktienregisterWeiterePerson;
import de.meetingapps.meetingportal.meetComEntities.EclAufgaben;
import de.meetingapps.meetingportal.meetComEntities.EclLoginDaten;
import de.meetingapps.meetingportal.meetComKonst.KonstAktienregisterErgaenzung;
import de.meetingapps.meetingportal.meetComKonst.KonstAufgaben;
import de.meetingapps.meetingportal.meetComKonst.KonstAufgabenAnforderer;
import de.meetingapps.meetingportal.meetComKonst.KonstAufgabenStatus;
import de.meetingapps.meetingportal.meetComKonst.KonstLoginKennungArt;
import de.meetingapps.meetingportal.meetComKonst.KonstPortalTexte;
import de.meetingapps.meetingportal.meetComKonst.KonstPortalView;
import de.meetingapps.meetingportal.meetingportTFunktionen.TPruefeStartNachOpen;
import de.meetingapps.meetingportal.meetingportTFunktionen.TRemoteAR;
import de.meetingapps.meetingportal.meetingportTFunktionen.TSessionVerwaltung;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@RequestScoped
@Named
public class TPasswortVergessen {

    private int logDrucken = 10;
    private @Inject BrMInit brMInit;
    private @Inject BrMLogin brMLogin;
    private @Inject BrMARRemoteZuARLokal brMARRemoteZuARLokal;
    private @Inject BrMAktionaersdaten brMAktionaersdaten;
    
    @Inject
    private EclDbM eclDbM;
    @Inject
    private EclPortalTexteM eclTextePortalM;
    @Inject
    private BaMailM baMailm;
    @Inject
    private EclParamM eclParamM;
    @Inject
    private TPasswortVergessenSession tPasswortVergessenSession;
    @Inject
    private PAktionaersdatenSession pAktionaersdatenSession;
   @Inject
    private TPruefeStartNachOpen tPruefeStartNachOpen;
    @Inject
    private TLoginLogoutSession tLoginLogoutSession;
    @Inject
    private TSession tSession;
    @Inject
    private TSessionVerwaltung tSessionVerwaltung;
    @Inject
    private TLinkSession tLinkSession;
    private @Inject TRemoteAR tRemoteAR;

    private void clearDlgVariablen() {
        tPasswortVergessenSession.clearDlgVariablen();
    }

    public void passwortZuruecksetzenVorbereiten() {
        eclDbM.openAll();
        eclDbM.closeAll();
        clearDlgVariablen();
        if (tPruefeStartNachOpen.pruefeStartNachOpenOhneUser()==false) {
            return;
        }
        int verfahrenPasswortVergessen = eclParamM.getParam().paramPortal.verfahrenPasswortVergessen;

        if (eclParamM.getParam().paramPortal.verfahrenPasswortVergessenAblauf == 0) {
            if ((verfahrenPasswortVergessen & 1) == 1 && (verfahrenPasswortVergessen & 4) == 4) {
                /*Auswahl zwischen Email-Adresse und Strasse/Ort ermöglichen*/
                tPasswortVergessenSession.setAuswahlAnbieten(true);
                tPasswortVergessenSession.setEingabeAnbieten(true);
                tPasswortVergessenSession.setEingabeSelektiert("1");

                tPasswortVergessenSession.setTextVariante(4);
            } else {
                /*Keine Auswahl möglich*/
                tPasswortVergessenSession.setAuswahlAnbieten(false);
                tPasswortVergessenSession.setEingabeAnbieten(false);
                tPasswortVergessenSession.setTextVariante(1);

                if ((verfahrenPasswortVergessen & 1) == 1) {
                    tPasswortVergessenSession.setEingabeSelektiert("1");
                    tPasswortVergessenSession.setEingabeAnbieten(true);
                    tPasswortVergessenSession.setTextVariante(2);
                }
                if ((verfahrenPasswortVergessen & 4) == 4) {
                    tPasswortVergessenSession.setEingabeSelektiert("2");
                    tPasswortVergessenSession.setEingabeAnbieten(true);
                    tPasswortVergessenSession.setTextVariante(3);
                }
            }
        } else {/**Ablauf==1 (mit Geburtsdatum)*/
            tPasswortVergessenSession.setAuswahlAnbieten(true);
            tPasswortVergessenSession.setEingabeAnbieten(false);
            tPasswortVergessenSession.setEingabeSelektiert("1");
        }

    }

    /**
     * "Normale" Returncodes, aber mit Meldung:
     * afPWVergessenMailGesendet
     * 
     * Fehler:
     * afKennungUnbekannt
     * afBerechtigungFuerAktionaersportalFehlt
     * afUserLoginTemporaerGesperrt
     * afHinterlegteEmailAdresseEingeben
     * afHinterlegteStrasseOrtEingeben
     * afEMailUnbekannt
     * afPasswortVergessenUeberMailUnzulaessig
     * afGeburtdatumUnzulaessigesFormat
     * afGeburtdatumFalsch
     * afKeineEmailAdresseHinterlegt
     */
    /**Passwort-Zurücksetzen, aus normalem Portal heraus (Passwort-Vergessen)*/
    public void doPasswortZuruecksetzen() {
        if (!tSessionVerwaltung.pruefeStartOhneUserLoginPruefung(KonstPortalView.PASSWORT_VERGESSEN)) {
            tSessionVerwaltung.setzeEnde();
            return;
        }

        eclDbM.openAll();
        eclDbM.openWeitere();
        if (tPruefeStartNachOpen.pruefeStartNachOpenOhneUser()==false) {
            eclDbM.closeAll();
            return;
        }

        boolean bRc = passwortZuruecksetzen(false);
        if (bRc == false) {
            eclDbM.closeAllAbbruch();
            tSessionVerwaltung.setzeEnde();
            return;
        }

        eclDbM.closeAll();
        this.clearDlgVariablen();
        tSessionVerwaltung.setzeEnde(KonstPortalView.PASSWORT_VERGESSEN_QUITTUNG);
    }

    /**Passwort-Zurücksetzen, aus Permanent-Portal heraus (Passwort-Vergessen)*/
        public void doPPasswortZuruecksetzen() {
        if (!tSessionVerwaltung.pruefeStartOhneUserLoginPruefung(KonstPortalView.P_PASSWORT_VERGESSEN)) {
            tSessionVerwaltung.setzeEnde();
            return;
        }

        eclDbM.openAll();
        eclDbM.openWeitere();
        if (tPruefeStartNachOpen.pruefeStartNachOpenOhneUser()==false) {
            eclDbM.closeAll();
            return;
        }

        boolean bRc = passwortZuruecksetzen(false);
        if (bRc == false) {
            eclDbM.closeAllAbbruch();
            tSessionVerwaltung.setzeEnde();
            return;
        }

        eclDbM.closeAll();
        this.clearDlgVariablen();
        tSessionVerwaltung.setzeEnde(KonstPortalView.P_PASSWORT_VERGESSEN_QUITTUNG);
    }

    /*******Interne Funktionen für Zurücksetzen*************************/

    /**true => Erstregistrierung für Permanentportal
     * false => normaler Passwortvergessen-Prozess
     */
    private boolean passwortZuruecksetzen(boolean pRegistrierungFuerPermanentPortal) {
        int erg;

//        tPasswortVergessenSession.setTextVariante(0);

        //		blMFuelleEclMAusPufferOderDBEE.fuelleAlleVariablenBeiStart();

        CaBug.druckeLog("tPasswortVergessenSession.getLoginKennung()=" + tPasswortVergessenSession.getLoginKennung(),
                logDrucken, 3);

        
        if (pRegistrierungFuerPermanentPortal) {
            /*In diesem Fall: Erstregistrierung.
             * Hier wird "per Mail" gar nicht angeboten (in der Oberfläche). Deshalb sicherheitshalber auf
             * "per Post" setzen.
             */
            tPasswortVergessenSession.setEingabeSelektiert("2");
        }
        
        if (tSession.isPermanentPortal()) {
            /*Prüfen, ob in Aktienregister vorhanden, sowie aktuelle Daten übertragen.*/
            
            /*Prüfen, ob im Aktienregister vorhanden*/
            brMInit.init();
            
            brMLogin.pruefeNachLogin(tPasswortVergessenSession.getLoginKennung());
            if (brMLogin.getRcAktionaerInRegisterVorhanden()==-1) {
                tSession.trageFehlerEin(CaFehler.perAktionaersnummerNichtInAktienregisterEnthalten);
                return false;
            }
            
            /*Muß immer geholt werden, um ggf. Versanddaten zu aktualisieren*/
            brMARRemoteZuARLokal.fuelleARNachBrmLogin(tPasswortVergessenSession.getLoginKennung());
            
            /*Durchführen, um pAktionaersdatenSession zu füllen und insbesondere Aktionärsart
             * etc. zur Verfügung zu haben, z.B. für Unterscheidung ob Zustellung per Mail
             * zulässig
             */
            int rc=brMAktionaersdaten.holeAktuellenStand(tPasswortVergessenSession.getLoginKennung());
            if (tRemoteAR.pruefeVerfuegbar(rc)==false) {return false;}
        }
        
        BlTeilnehmerLoginNeu blTeilnehmerLogin = new BlTeilnehmerLoginNeu();
        blTeilnehmerLogin.initDB(eclDbM.getDbBundle());
        erg = blTeilnehmerLogin.findeUndPruefeKennung(tPasswortVergessenSession.getLoginKennung(), "", false);
        CaBug.druckeLog("erg nach findeUndPruefeKennung=" + erg, logDrucken, 10);
        if (erg == CaFehler.afBerechtigungFuerAktionaersportalFehlt) {
            tPasswortVergessenSession.setTextVarianteQuittung(3);
            return true;
        }
        if (erg < 0) {
            tSession.trageFehlerEin(erg);
            return false;
        }

        if (blTeilnehmerLogin.eclLoginDaten.kennungArt == KonstLoginKennungArt.personenNatJur
                && eclParamM.getParam().paramPortal.verfahrenPasswortVergessenAblauf == 1) {
            /*Bei Vergessen-Ablauf mit Geburtsdatum: für Dritte und Gäste nicht möglich*/
            tSession.trageFehlerEin(CaFehler.afPasswortVergessenFuerSonstigeNichtMoeglich);
            return false;
        }

        EclLoginDaten lLoginDaten = blTeilnehmerLogin.eclLoginDaten;

        if (tPasswortVergessenSession.getEingabeSelektiert().equals("1") /*Per E-Mail gewählt*/ && (
                blTeilnehmerLogin.dauerhafteRegistrierungUnzulaessig || 
                (tSession.isPermanentPortal() && pAktionaersdatenSession.renderZugangsdatenNurPerPost())
                )
                ) {
            tSession.trageFehlerEin(CaFehler.afPasswortVergessenUeberMailUnzulaessig);
            return false;
        }

        if (eclParamM.getParam().paramPortal.verfahrenPasswortVergessenAblauf == 0) {
            /*++++++++++++++++++++++Standard-Ablauf+++++++++++++++++++++++++++++++++++++++*/
            if (tPasswortVergessenSession.isEingabeAnbieten() == true) {
                /*Verifikationsdaten werden mit eingegeben - manuelles oder automatisches Verfahren*/
                if (tPasswortVergessenSession.getEingabeSelektiert().compareTo("1") == 0) {
                    /*E-Mail-Adresse eingegeben*/
                    if (tPasswortVergessenSession.getEmailAdresse().trim().isEmpty()) {
                        /*Keine E-Mail-Adresse eingegeben*/
                        tSession.trageFehlerEin(CaFehler.afHinterlegteEmailAdresseEingeben);
                        return false;
                    }

                    if (lLoginDaten.eMailFuerVersand
                            .compareToIgnoreCase(tPasswortVergessenSession.getEmailAdresse()) != 0
                            && lLoginDaten.eMail2FuerVersand
                                    .compareToIgnoreCase(tPasswortVergessenSession.getEmailAdresse()) != 0) {
                        /*Eingegebene E-Mail-Adresse stimmt nicht mit hinterlegter E-Mail-Adresse überein*/
                        tSession.trageFehlerEin(CaFehler.afEMailUnbekannt);
                        return false;
                    }

                    eMailVerschicken(lLoginDaten);
                    tPasswortVergessenSession.setTextVarianteQuittung(1);
                    return true;
                } else {
                    /*Strasse+Ort eingegeben*/
                    if (tPasswortVergessenSession.getStrasse().trim().isEmpty()
                            || tPasswortVergessenSession.getOrt().trim().isEmpty()) {
                        tSession.trageFehlerEin(CaFehler.afHinterlegteStrasseOrtEingeben);
                        return false;
                    }

                    /*Aufgabe eintragen*/
                    aufgabeEintragen(blTeilnehmerLogin.eclLoginDaten.loginKennung,
                            tPasswortVergessenSession.getStrasse(), tPasswortVergessenSession.getOrt(), "",
                            KonstAufgabenStatus.gestellt, false);
                 }
            } else {
                /*Immer manuelles Verfahren - keine Eingaben getätigt*/
                /*Aufgabe eintragen*/
                aufgabeEintragen(blTeilnehmerLogin.eclLoginDaten.loginKennung, "", "", "", KonstAufgabenStatus.gestellt, false);
            }
        } else {
            /*++++++++++++++++++Ablauf ==1, Geburtsdatum als Bestätigung+++++++++++++++++++++++++++++++++++++*/
            String eingabeGeburtsdatum = tPasswortVergessenSession.getGeburtsdatum().trim();
            if (tSession.isPermanentPortal()==false) {
                /*------Geburtsdatum bei normalen Portal aus db_aktienregisterErgaenzung holen (nur ku178)----------------*/
                /*Geburtsdatum abprüfen*/
                if (!CaString.isDatum(eingabeGeburtsdatum)) {
                    /*Format unzulässig*/
                    tSession.trageFehlerEin(CaFehler.afGeburtdatumUnzulaessigesFormat);
                    return false;
                }
                int rc = eclDbM.getDbBundle().dbAktienregisterErgaenzung
                        .readZuident(blTeilnehmerLogin.eclAktienregister.aktienregisterIdent);
                if (rc < 1) {
                    tSession.trageFehlerEin(CaFehler.afKennungUnbekannt);
                    return false;
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
                    tSession.trageFehlerEin(CaFehler.afGeburtdatumFalsch);
                    return false;
                }
                if (tPasswortVergessenSession.getEingabeSelektiert().compareTo("1") == 0) {
                    /*E-Mail-Adresse hinterlegt?*/
                    if (lLoginDaten.eMailFuerVersand.isEmpty()) {
                        tSession.trageFehlerEin(CaFehler.afKeineEmailAdresseHinterlegt);
                        return false;
                    }
                    tPasswortVergessenSession.setEmailAdresse(lLoginDaten.eMailFuerVersand);
                    eMailVerschicken(lLoginDaten);

                    tPasswortVergessenSession.setTextVarianteQuittung(1);
                    return true;
                } else {
                   
                    /*Hinweis für ku178: Papier-Versand im normalen Portal ist im ku178 BetterSmart
                     * in der Oberfläche deaktiviert, da ohne GeDix-Anwendung ein Versand an alle
                     * hinterlegten Postempfänger nicht möglich ist!
                     */
                    /*Aufgabe eintragen*/
                    aufgabeEintragen(blTeilnehmerLogin.eclLoginDaten.loginKennung, tPasswortVergessenSession.getStrasse(),
                            tPasswortVergessenSession.getOrt(), "", KonstAufgabenStatus.gestellt, false);
                }
            }
            else {
                /*------Geburtsdatum bei Permanent-Portal aus Remote-Register holen----------------*/
                /*Geburtsdatum abprüfen*/
                if (!CaString.isDatum(eingabeGeburtsdatum)) {
                    /*Format unzulässig*/
                    tSession.trageFehlerEin(CaFehler.afGeburtdatumUnzulaessigesFormat);
                    return false;
                }
                boolean rc=brMLogin.pruefeGeburtsdatum(eingabeGeburtsdatum);
                if (rc==false) {
                    /*Geburtsdatum stimmt nicht mit gespeichertem überein*/
                    tSession.trageFehlerEin(CaFehler.afGeburtdatumFalsch);
                    return false;
                }
                if (pRegistrierungFuerPermanentPortal==false) {
                    if (tPasswortVergessenSession.getEingabeSelektiert().compareTo("1") == 0) {
                        /*E-Mail-Adresse hinterlegt?*/
                        if (lLoginDaten.eMailFuerVersand.isEmpty()) {
                            tSession.trageFehlerEin(CaFehler.afKeineEmailAdresseHinterlegt);
                            return false;
                        }
                        tPasswortVergessenSession.setEmailAdresse(lLoginDaten.eMailFuerVersand);
                        eMailVerschicken(lLoginDaten);

                        tPasswortVergessenSession.setTextVarianteQuittung(1);
                        return true;
                    } else {
                        /*Papierversand*/

                        /*Aufgabe eintragen*/
                        aufgabeEintragen(blTeilnehmerLogin.eclLoginDaten.loginKennung, tPasswortVergessenSession.getStrasse(),
                                tPasswortVergessenSession.getOrt(), "", KonstAufgabenStatus.gestellt, true);
                    }
                    
                }
                else {
                    /*Aufgabe eintragen*/
                    String druckVariante="";
                    if (pRegistrierungFuerPermanentPortal) {
                        druckVariante="2";
                    }
                    aufgabeEintragen(blTeilnehmerLogin.eclLoginDaten.loginKennung, tPasswortVergessenSession.getStrasse(),
                            tPasswortVergessenSession.getOrt(), druckVariante, KonstAufgabenStatus.gestellt, true);
                    
                }
                
            }
        }

        tPasswortVergessenSession.setTextVarianteQuittung(2);
        return true;

    }

    /**pPermanentportal==true => Sonderbearbeitung
     * Es wird dann aus Remote-Aktienregister alle Versandadressen geholt und
     * an diese fertige Aufgaben - mit Passwort und Versandadresse - aufbereitet
     */
    private void aufgabeEintragen(String pArgument0, String pArgument1, String pArgument2, String pDruckvarianteArgument3, int pStatus, boolean pPermanentportal) {
        if (eclParamM.liefereDatenbestandPasswortVergessenSchreibenDrucken()==false) {
            return;
        }

        if (pPermanentportal==false || pAktionaersdatenSession.isPostempfaengerVorhanden()==false) {
            EclAufgaben lAufgabe = new EclAufgaben();
            lAufgabe.mandant = eclDbM.getDbBundle().clGlobalVar.mandant;
            lAufgabe.aufgabe = KonstAufgaben.aktionaerNeuesPasswortAdressePruefen;
            lAufgabe.zeitpunktErteilt = CaDatumZeit.DatumZeitStringFuerDatenbank();
            lAufgabe.anforderer = KonstAufgabenAnforderer.aktionaerPortal;
            lAufgabe.status = pStatus;
            lAufgabe.argument[0] = pArgument0;
            lAufgabe.argument[1] = pArgument1;
            lAufgabe.argument[2] = pArgument2;
            lAufgabe.argument[3] = pDruckvarianteArgument3;
            eclDbM.getDbBundle().dbAufgaben.insert(lAufgabe);
        }
        else {
            List<EclAktienregisterWeiterePerson> postempfaenger=pAktionaersdatenSession.getPostempfaenger();
            /*Neues Initial-Passwort - für alle Anschreiben generieren*/
            CaPasswortErzeugen caPasswortErzeugen = new CaPasswortErzeugen();

            // Passwortlänge festlegen (Aktuell bei Namensaktien IMMER auf 8)
            int passwort_laenge = eclParamM.getParam().paramPortal.passwortMindestLaenge;

            /* Initialpasswort generieren (von uns generiert und unleserlich gemacht)
             * d.h.:
             * Passwort wird generiert
             * -> pppppppp
             * -> Passwort wird vorne und hinten mit einem String ergänzt, damit es zumindest nicht komplett plain in der DB steht.
             * -> ssssssssppppppppssssssss 
             * 
             * Übergeben wird:
             * 1. Passwortlänge hier: 8
             * 2. Anzahl der Sonderzeichen hier: 2
             * 3. Anzahl der Zahlen hier: 2
             * 4. Großbuchstaben true/false hier: true
             */
            String passwort_initial = caPasswortErzeugen.generatePWInitial(passwort_laenge, 1, 1, true);

//            // Initialpasswort (nur pppppppp) verschlüsseln -> SHA256 String
//            String passwort_verschluesselt = CaPasswortVerschluesseln.verschluesselnAusInitialPW(passwort_initial,
//                    passwort_laenge);
            
            for (EclAktienregisterWeiterePerson iEclAktienregisterWeiterePerson:postempfaenger) {

                EclAufgaben lAufgabe = new EclAufgaben();
                lAufgabe.mandant = eclDbM.getDbBundle().clGlobalVar.mandant;
                lAufgabe.aufgabe = KonstAufgaben.aktionaerNeuesPasswortAdressePruefen;
                lAufgabe.zeitpunktErteilt = CaDatumZeit.DatumZeitStringFuerDatenbank();
                lAufgabe.anforderer = KonstAufgabenAnforderer.aktionaerPortal;
                lAufgabe.status = pStatus;
                lAufgabe.argument[0] = pArgument0;
                lAufgabe.argument[1] = pArgument1;
                lAufgabe.argument[2] = pArgument2;
                lAufgabe.argument[3] = pDruckvarianteArgument3;

                lAufgabe.argument[4] = iEclAktienregisterWeiterePerson.liefereNameKomplett();
                lAufgabe.argument[5] = iEclAktienregisterWeiterePerson.strasse;
                lAufgabe.argument[6] = iEclAktienregisterWeiterePerson.plz+" "+iEclAktienregisterWeiterePerson.ort;
                if (iEclAktienregisterWeiterePerson.land.equals("Deutschland") || iEclAktienregisterWeiterePerson.land.isEmpty()) {
                    lAufgabe.argument[7] = "";
                }
                else {
                    lAufgabe.argument[7]=iEclAktienregisterWeiterePerson.land.toUpperCase();
                }

                lAufgabe.argument[8]=passwort_initial;
                eclDbM.getDbBundle().dbAufgaben.insert(lAufgabe);

            }

        }
    }

    private void eMailVerschicken(EclLoginDaten pLoginDaten) {
        /**Nun Automatische Passwort-Vergessen-Prozedur an E-Mail einleiten*/
        String pwVergessenLink = eclDbM.getDbBundle().dbEindeutigerKey.getNextFree(); //Eindeutiger Key, der dann Bestandteil des Links wird*/
        eclDbM.getDbBundle().reOpen();
        pLoginDaten.passwortVergessenLink = pwVergessenLink;

        String sprache = "DE";
        if (eclParamM.getClGlobalVar().sprache == 2) {
            sprache = "EN";
        }
        BlEinsprungLinkPortal lBlEinsprungLinkPortal = new BlEinsprungLinkPortal(eclDbM.getDbBundle());
        tLinkSession.setEinsprungsLinkFuerEmail(lBlEinsprungLinkPortal.linkFuerPasswortVergessen(tSession.getDifZeit(),
                tSession.getHvJahr(), tSession.getHvNummer(), tSession.getDatenbereich(), sprache,
                tSession.getTestModus(), pLoginDaten.passwortVergessenLink, tSession.isPermanentPortal()));
        if (tPasswortVergessenSession.isWurdeUeberAppAngefordert()) {
            tLinkSession.setEinsprungsLinkFuerEmail(lBlEinsprungLinkPortal.linkFuerPasswortVergessenApp(
                    tSession.getDifZeit(), tSession.getHvJahr(), tSession.getHvNummer(), tSession.getDatenbereich(),
                    sprache, tSession.getTestModus(), pLoginDaten.passwortVergessenLink));
        }
        tLinkSession.setEinsprungsLinkNurCode(pLoginDaten.passwortVergessenLink);

        String hMailText = "", hBetreff = "";

        if (tPasswortVergessenSession.isWurdeUeberAppAngefordert() == false) {
            if (tSession.isPermanentPortal()==false) {
                hBetreff = eclTextePortalM.holeIText(KonstPortalTexte.PASSWORT_VERGESSEN_MAIL_NORMALPORTAL_BETREFF);
                hMailText = eclTextePortalM.holeIText(KonstPortalTexte.PASSWORT_VERGESSEN_MAIL_NORMALPORTAL_TEXT);
            }
            else {
                hBetreff = eclTextePortalM.holeIText(KonstPortalTexte.PASSWORT_VERGESSEN_MAIL_PERMANENTPORTAL_BETREFF);
                hMailText = eclTextePortalM.holeIText(KonstPortalTexte.PASSWORT_VERGESSEN_MAIL_PERMANENTPORTAL_TEXT);
            }
        } else {
            hBetreff = eclTextePortalM.holeIText(KonstPortalTexte.PASSWORT_VERGESSEN_MAIL_APP_BETREFF);
            hMailText = eclTextePortalM.holeIText(KonstPortalTexte.PASSWORT_VERGESSEN_MAIL_APP_TEXT);
        }

        CaBug.druckeLog("E-Mail-Adresse=" + tPasswortVergessenSession.getEmailAdresse(),logDrucken, 3);
        baMailm.senden(tPasswortVergessenSession.getEmailAdresse(), hBetreff, hMailText);

        eclDbM.getDbBundle().dbLoginDaten.update(pLoginDaten);

    }

    /****************Weiteres**************************/

    public void doZumLogin() {
        /*Zur Login-Funktion ist immer möglich - deshalb kein Abprüfen mit aFunktionen.pruefeStart ! */
        tLoginLogoutSession.clearAll();
        tSession.clearSession();
        tSession.setUserEingeloggt("0");
        tSessionVerwaltung.setzeEnde(KonstPortalView.LOGIN);
       return;
    }

    public void doPZumLogin() {
        /*Zur Login-Funktion ist immer möglich - deshalb kein Abprüfen mit aFunktionen.pruefeStart ! */
        tLoginLogoutSession.clearAll();
        tSession.clearSession();
        tSession.setUserEingeloggt("0");
        tSessionVerwaltung.setzeEnde(KonstPortalView.P_LOGIN);
       return;
    }

    /***************************Ansprünge aus pRegistrieren****************************************/
    public void doRegistrieren() {
        if (!tSessionVerwaltung.pruefeStartOhneUserLoginPruefung(KonstPortalView.P_REGISTRIEREN)) {
            tSessionVerwaltung.setzeEnde();
            return;
        }

        eclDbM.openAll();
        eclDbM.openWeitere();
        if (tPruefeStartNachOpen.pruefeStartNachOpenOhneUser()==false) {
            eclDbM.closeAll();
            return;
        }

        boolean bRc = passwortZuruecksetzen(true);
        if (bRc == false) {
            eclDbM.closeAllAbbruch();
            tSessionVerwaltung.setzeEnde();
            return;
        }

        eclDbM.closeAll();
        this.clearDlgVariablen();
        tSessionVerwaltung.setzeEnde(KonstPortalView.P_REGISTRIEREN_QUITTUNG);
        
    }
    
 
    
    public void doZurueck() {
        tLoginLogoutSession.clearAll();
        tSession.clearSession();
        tSession.setUserEingeloggt("0");
        tSessionVerwaltung.setzeEnde(KonstPortalView.P_LOGIN);
       return;
        
    }
    
    public void doRegistrierenQuittungZumLogin() {
        if (!tSessionVerwaltung.pruefeStartOhneUserLoginPruefung(KonstPortalView.P_REGISTRIEREN_QUITTUNG)) {
            tSessionVerwaltung.setzeEnde();
            return;
        }
        tLoginLogoutSession.clearAll();
        tSession.clearSession();
        tSession.setUserEingeloggt("0");
        tSessionVerwaltung.setzeEnde(KonstPortalView.P_LOGIN);
       return;
        
    }

    public void doPPasswortVergessenQuittungZumLogin() {
        if (!tSessionVerwaltung.pruefeStartOhneUserLoginPruefung(KonstPortalView.P_PASSWORT_VERGESSEN_QUITTUNG)){
                
            tSessionVerwaltung.setzeEnde();
            return;
        }
        tLoginLogoutSession.clearAll();
        tSession.clearSession();
        tSession.setUserEingeloggt("0");
        tSessionVerwaltung.setzeEnde(KonstPortalView.P_LOGIN);
       return;
        
    }

    
    
    /*****************Aus iPWZurueck*********************/
    public void doSpeichern() {
        pwZurueckApp = false;
        doSpeichernIntern();
        return;
    }

    public void doSpeichernApp() {
        pwZurueckApp = true;
        doSpeichernIntern();
        return;
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

    private void doSpeichernIntern() {
        int erg;
        tPasswortVergessenSession.clearFehler();
        /*Wird direkt aufgerufen, deshalb keine Überprüfung möglich - und auch nicht erforderlich. Falls Browser-/Vor-/Zurück verwendet wird, 
         *werden halt ggf. jeweils die dann aktuellen Eingaben überprüft. 
         */

        if (eclParamM.getClGlobalVar().mandant == 0) {
            CaBug.drucke("Mandant ist 0");
            tSessionVerwaltung.setzeEnde(KonstPortalView.fehlerLinkAufrufBestaetigen);
            return;
        }

        /*Überhaupt Passwort eingegeben?*/
        if (tPasswortVergessenSession.getPasswort().isEmpty()) {
            tSession.trageFehlerEin(CaFehler.afPasswortFehlt);
            tSessionVerwaltung.setzeEnde();
            return;
        }
        /*Passwort-Bestätigung gleich?*/
        if (tPasswortVergessenSession.getPasswort()
                .compareTo(tPasswortVergessenSession.getPasswortBestaetigung()) != 0) {
            tSession.trageFehlerEin(CaFehler.afPasswortBestaetigungWeichtAb);
            tSessionVerwaltung.setzeEnde();
            return;
        }

        eclDbM.openAll();

        if (tPruefeStartNachOpen.pruefeStartNachOpenOhneUser()==false) {
            eclDbM.closeAll();
            return;
        }

        /*Passwort lang genug*/
        if (tPasswortVergessenSession.getPasswort().length() < eclParamM.getParam().paramPortal.passwortMindestLaenge) {
            tSession.trageFehlerEin(CaFehler.afPasswortZuKurz);
            eclDbM.closeAllAbbruch();
            return;
        }
        /*Passwort verschiedene Zeichen?*/
        int rc=CaPasswortVerschluesseln.pruefePasswortZulaessig(tPasswortVergessenSession.getPasswort());
        if (rc<0) {
            tSession.trageFehlerEin(rc);
            eclDbM.closeAllAbbruch();
            return;
        }
        
        BlTeilnehmerLoginNeu blTeilnehmerLogin = new BlTeilnehmerLoginNeu();
        blTeilnehmerLogin.initDB(eclDbM.getDbBundle());
        erg = blTeilnehmerLogin.findeUndPruefeKennung(tPasswortVergessenSession.getLoginKennung(), "", false);
        if (erg < 1) {
            tSession.trageFehlerEin(CaFehler.afKennungUnbekannt);
            eclDbM.closeAllAbbruch();
            return;
        }
        if (blTeilnehmerLogin.eclLoginDaten.kennungArt == KonstLoginKennungArt.personenNatJur
                && eclParamM.getParam().paramPortal.verfahrenPasswortVergessenAblauf == 1) {
            tSession.trageFehlerEin(CaFehler.afPasswortVergessenFuerSonstigeNichtMoeglich);
            eclDbM.closeAllAbbruch();
            return;
        }

        EclLoginDaten lLoginDaten = blTeilnehmerLogin.eclLoginDaten;

        if (lLoginDaten.passwortVergessenLink.compareTo(tPasswortVergessenSession.getBestaetigungsCode()) != 0
                || tPasswortVergessenSession.getBestaetigungsCode().isEmpty()) {
            tSession.trageFehlerEin(CaFehler.afPWVergessenLinkUngueltig);
            eclDbM.closeAllAbbruch();
            return;
        }

        /*Passwort übertragen*/
        lLoginDaten.passwortVerschluesselt = CaPasswortVerschluesseln
                .verschluesseln(tPasswortVergessenSession.getPasswort());
        lLoginDaten.passwortInitial = "";
        lLoginDaten.passwortVergessenLink = "";
        lLoginDaten.eigenesPasswort = 99;

        eclDbM.getDbBundle().dbLoginDaten.update(lLoginDaten);
        eclDbM.closeAll();

        tPasswortVergessenSession.setLoginKennung("");
        tPasswortVergessenSession.setEmailAdresse("");
        tPasswortVergessenSession.setPasswort("");
        tPasswortVergessenSession.setPasswortBestaetigung("");
        tLoginLogoutSession.clearFehler();
        if (tSession.isPermanentPortal()==false) {
        if (pwZurueckApp == false) {
            tSessionVerwaltung.setzeEnde(KonstPortalView.LOGIN);
        } else {
            tSessionVerwaltung.setzeEnde(KonstPortalView.LOGIN);
            //			return aFunktionen.setzeEnde("aPwZurueckAppQuittung", true, true);
        }
        }
        else {
            tSessionVerwaltung.setzeEnde(KonstPortalView.P_LOGIN);
        }
    }

    /*******Interne Funktionen für Zurücksetzen*************************/

    //	/*******************Ab hier: Standard Getter/Setter********************************************/
    //	/*Wofür?*/
    //	
    //	public String getLoginKennung() {
    //		return tPasswortVergessenSession.getLoginKennung();
    //	}
    //
    //
    //	public void setLoginKennung(String loginKennung) {
    //		tPasswortVergessenSession.setLoginKennung(loginKennung);
    //	}
    //
    //
    //	public String getEmailAdresse() {
    //		return tPasswortVergessenSession.getEmailAdresse();
    //	}
    //
    //
    //	public void setEmailAdresse(String emailAdresse) {
    //		tPasswortVergessenSession.setEmailAdresse(emailAdresse);
    //	}
    //
    //	public String getPasswort() {
    //		return tPasswortVergessenSession.getPasswort();
    //	}
    //
    //	public void setPasswort(String passwort) {
    //		tPasswortVergessenSession.setPasswort(passwort);
    //	}
    //
    //	public String getPasswortBestaetigung() {
    //		return tPasswortVergessenSession.getPasswortBestaetigung();
    //	}
    //
    //	public void setPasswortBestaetigung(String passwortBestaetigung) {
    //		tPasswortVergessenSession.setPasswortBestaetigung(passwortBestaetigung);
    //	}
    //
    //	public String getBestaetigungsCode() {
    //		return tPasswortVergessenSession.getBestaetigungsCode();
    //	}
    //
    //	public void setBestaetigungsCode(String bestaetigungsCode) {
    //		tPasswortVergessenSession.setBestaetigungsCode(bestaetigungsCode);
    //	}
    //
    //
    //	public String getFehlerMeldung() {
    //		return tPasswortVergessenSession.getFehlerMeldung();
    //	}
    //
    //
    //	public void setFehlerMeldung(String fehlerMeldung) {
    //		tPasswortVergessenSession.setFehlerMeldung(fehlerMeldung);
    //	}
    //
    //
    //	public int getFehlerNr() {
    //		return tPasswortVergessenSession.getFehlerNr();
    //	}
    //
    //
    //	public void setFehlerNr(int fehlerNr) {
    //		tPasswortVergessenSession.setFehlerNr(fehlerNr);
    //	}
    //

}
