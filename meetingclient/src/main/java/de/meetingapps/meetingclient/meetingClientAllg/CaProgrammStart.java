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
package de.meetingapps.meetingclient.meetingClientAllg;

import de.meetingapps.meetingclient.meetingClient.BlParameterUeberWebService;
import de.meetingapps.meetingclient.meetingClientDialoge.CaController;
import de.meetingapps.meetingclient.meetingClientDialoge.CaIcon;
import de.meetingapps.meetingclient.meetingClientDialoge.CaZeigeHinweis;
import de.meetingapps.meetingclient.meetingClientDialoge.CtrlHardwareStart;
import de.meetingapps.meetingclient.meetingClientDialoge.CtrlLoginNeu;
import de.meetingapps.meetingclient.meetingClientDialoge.CtrlVerbindungAuswahl;
import de.meetingapps.meetingclient.meetingClientDialoge.CtrlWaehleModule;
import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComBVerwaltung.BvReload;
import de.meetingapps.meetingportal.meetComBl.BlFuelleStaticAusDBAufClient;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclUserLogin;
import de.meetingapps.meetingportal.meetComHVParam.ClGlobalVar;
import de.meetingapps.meetingportal.meetComHVParam.ParamGeraet;
import de.meetingapps.meetingportal.meetComHVParam.ParamInterneKommunikation;
import de.meetingapps.meetingportal.meetComHVParam.ParamS;
import de.meetingapps.meetingportal.meetComHVParam.SParamProgramm;
import de.meetingapps.meetingportal.meetComStub.WSClient;
import de.meetingapps.meetingportal.meetComWE.WEVersionsabgleich;
import de.meetingapps.meetingportal.meetComWE.WEVersionsabgleichRC;
import javafx.stage.Stage;

/******************Dokumentation der Aufrufparameter*******************************************
 * Generell:s
 * =========
 * A (Arbeitsplatznummer) ist immer zwingend anzugeben. Falls nichts angegeben, wird mit
 * Arbeitsplatznummer 1 gearbeitet - quasi reserviert für den "Master-/Entwickler-Arbeitsplatz".
 *
 * C oder CI sind alternativ (d.h. nur einer von beiden) anzugeben.
 * D oder DI sind alternativ (d.h. nur einer von beiden) anzugeben.
 *
 * CO / COI, DO / DOI sind die Verbindungen zum Online-Server für Hybridveranstaltungen und optional anzugeben, 
 * dann analog zu C/CI/D/DI
 * Achtung: 
 * > wenn C/CI/D/DI nicht angegeben sind, wird auch CO/COI/DO/DOI über die Auswahl belgt.
 * > Wennn C/CI/D/DI angegeben sind, muß (wenn verwendet) auch CO/COI/DO/DOI angegeben werden. Sie können jedoch
 * entfallen, wenn nicht benötigt.
 * 
 * 
 * C*, D* können:
 * > "sowohl als auch" angegeben werden (z.B. für die RZ-Rechner auf der HV,
 * dann sind sowohl die Module die über C* als auch die über D* kommunizieren aufrufbar.
 * > nur C* oder nur D* angegeben werden, dann sind nur die Module aufrufbar, die diese
 * Kommunikationsform verwenden.
 *
 * Module und deren Kommunikationsform: siehe Maske Geräteklassen, Module.
 *
 * Z Laufwerk und Pfad für Zertifikate
 * Muß nur angegeben werden, wenn https-Verbindung verwendet wird.
 * Dann z.B. Z=D:\betterMeeting_root
 *
 * Details:
 * ========
 * z.B. C=6
 * ---
  *
 * z.B. CI=128.34.12.14:8080
 * Connection über Angabe der IP-Adresse: anstelle von C, kann CI verwendet werden, und IP-Adresse und
 * Port des Web-Service-Servers direkt angegeben werden. (nur bei Web_Service-Clients), Nur für
 * HTTP-Kommunikation möglich (kein HTTPS)
 *
 * z.B. D=6
 * Datenbank-Connection-Nummer: Auswahl, über welche (vorprogrammierte) Datenbank-Connection die
 * Datenbank geöffnet werden soll (nicht bei Web-Service-Clients)
  *
 * z.B. DI=127.1.0.0
 * Datenbank-Connection, Spezifikation über IP-Adresse. Anstelle von D, kann DI verwendet werden, und
 * IP-Adresse das Datenbankservers direkt angegeben werden (nicht bei Web-Service-Clients)
 *
 * z.B: A=1
 * Arbeitsplatznummer
 * Über 9000 reserviert für Programminterne Sonderfunktionen
 * 9998=reserviert für App
 * 9999=reserviert für jeweiligen Wildfly-Server
 *
 */
public class CaProgrammStart {

    /** The log drucken. */
    private int logDrucken = 3;

    /** true => es wurde C* und/oder D* übergeben. */
    private boolean kommunikationUebergeben = false;

    /** Zu startendes Modul in aufrufender Funktion. */
    public int zuStartendesModul = -1;

    /** The db bundle. */
    private DbBundle dbBundle = null;

    /**
     * true => Fortsetzung des Programms zulässig booolean => Fortsetzung des
     * Programms nicht möglich
     * 
     * Liest auch Parameter.
     *
     * @param args the args
     * @return true, if successful
     */
    public boolean programmstart(String[] args) {

        CaBug.druckeLog("A", logDrucken, 10);

        boolean bRC = false;
        int rc = 0;

        /*Aufrufparameter dekodieren*/
        dekodiere(args);

        if (kommunikationUebergeben == false) { /*Keinerlei Kommunikationsparameter angegeben - abfragen*/
            bRC = frageVerbindungAb();
            if (bRC == false) {
                return false;
            }
        }

        /*Warte-Hinweis*/
        Stage lStage = new Stage();
        CaIcon.standard(lStage);

        CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
        caZeigeHinweis.zeigeNurHinweis(lStage, "Verbindungsaufbau - bitte warten");

        CaBug.druckeLog("B", logDrucken, 10);

        /*Prüfen, ob Haupt-Verbindung überhaupt möglich. Verbindung zum Online-Server für Hybrid-Veranstaltung wird nicht getestet*/
        if (ParamS.clGlobalVar.datenbankPfadNr != -1) {
            dbBundle = new DbBundle();
            dbBundle.openAllOhneParameterCheck();
            if (dbBundle.dbBasis.fehlerDatenbank) {
                dbBundle.closeAll();
                String pfadString = "Zugriffsfehler - out of Bounds";
                if (ParamS.clGlobalVar.datenbankPfadNr >= ParamInterneKommunikation.nummerVon && ParamS.clGlobalVar.datenbankPfadNr <= ParamInterneKommunikation.nummerBis) {
                    pfadString = ParamInterneKommunikation.datenbankPfadZurAuswahl[ParamS.clGlobalVar.datenbankPfadNr];
                }
                zeigeFehlerMitStage("Zugriff auf Datenbank nicht möglich! datenbankPfadNr="
                        + ParamS.clGlobalVar.datenbankPfadNr + " datenbankPfadZurAuswahl=" + pfadString);
                return schliesseHinweisReturnFalse(caZeigeHinweis);
            }

            BvReload bvReload = new BvReload(dbBundle);
            bvReload.checkReload(0);

            /*Geräte-Parameter / globale Parameter einlesen*/
            BlFuelleStaticAusDBAufClient blFuellePufferAusDBAufClient = new BlFuelleStaticAusDBAufClient(dbBundle);
            rc = blFuellePufferAusDBAufClient.fuelleGlobalParam(true, bvReload);
            switch (rc) {
            case CaFehler.sysGeraeteSetNichtVorhanden:
                zeigeFehlerMitStage(
                        "Geräte-Set falsch eingestellt! Aktuelles Set=" + dbBundle.paramServer.geraeteSetIdent);
                return schliesseHinweisReturnFalse(caZeigeHinweis);
            case CaFehler.sysGeraeteKlasseSetZuordnungNichtVorhanden:
                zeigeFehlerMitStage("Geräte-Zuordnung/Klasse falsch! Aktuelles Set="
                        + dbBundle.paramServer.geraeteSetIdent + " Gerätenummer=" + dbBundle.clGlobalVar.arbeitsplatz);
                return schliesseHinweisReturnFalse(caZeigeHinweis);
            case CaFehler.sysGeraeteKlasseNichtVorhanden:
                zeigeFehlerMitStage("Geräte-Klasse nicht vorhanden! Aktuelles Set="
                        + dbBundle.paramServer.geraeteSetIdent + " Gerätenummer=" + dbBundle.clGlobalVar.arbeitsplatz
                        + " Klasse=" + dbBundle.eclGeraetKlasseSetZuordnung.geraeteKlasseIdent);
                return schliesseHinweisReturnFalse(caZeigeHinweis);
            }

            dbBundle.closeAll();
        }

        CaBug.druckeLog("C", logDrucken, 10);

        if (ParamS.clGlobalVar.webServicePfadNr != -1) {
            /** Version mit Web-Service abgleichen */
            boolean versionPasst = false;
            WSClient wsClient = new WSClient();
            WEVersionsabgleich weVersionsabgleich = new WEVersionsabgleich();
            weVersionsabgleich.aktuelleClientVersion = SParamProgramm.programmVersion;
            WEVersionsabgleichRC weVersionsabgleichRC = wsClient.versionsabgleich(weVersionsabgleich);
            if (weVersionsabgleichRC.rc < 1) {
                String pfadString = "Zugriffsfehler - out of Bounds";
                if (ParamS.clGlobalVar.webServicePfadNr >= ParamInterneKommunikation.nummerVon && 
                        ParamS.clGlobalVar.webServicePfadNr <= ParamInterneKommunikation.nummerBis) {
                    pfadString = ParamInterneKommunikation.webServicePfadZurAuswahl[ParamS.clGlobalVar.webServicePfadNr];
                }
                zeigeFehlerMitStage("Zugriff auf Web-Service nicht möglich! webServicePfadNr="
                        + ParamS.clGlobalVar.webServicePfadNr + " webServicePfadZurAuswahl=" + pfadString);
                return schliesseHinweisReturnFalse(caZeigeHinweis);
            }
            if (weVersionsabgleichRC.serverPasstZuAktuellerClientVersion == true) {
                versionPasst = true;
            }
            if (versionPasst == false) {
                /*Nun noch überprüfen, ob diese Client-Version mit der Serverversion kann*/
                if (SParamProgramm.programmVersionServerAufClient != null
                        && SParamProgramm.programmVersionServerAufClient.length > 0) {
                    for (int i = 0; i < SParamProgramm.programmVersionServerAufClient.length; i++) {
                        if (weVersionsabgleichRC.aktuelleServerVersion
                                .equals(SParamProgramm.programmVersionServerAufClient[i])) {
                            versionPasst = true;
                        }
                    }
                }
            }
            if (versionPasst == false) {
                zeigeFehlerMitStage("Client und Server-Version passen nicht zusammen! Server-Version="
                        + weVersionsabgleichRC.aktuelleServerVersion + " Client-Version="
                        + SParamProgramm.programmVersion);
                return schliesseHinweisReturnFalse(caZeigeHinweis);
            }

            BlParameterUeberWebService lBlInitHVWebServices = new BlParameterUeberWebService();
            rc = lBlInitHVWebServices.holeGeraeteParameter();

            if (rc < 1) { /*Fehlerbehandlung*/
                String pfadString = "Zugriffsfehler - out of Bounds";
                if (ParamS.clGlobalVar.webServicePfadNr >= ParamInterneKommunikation.nummerVon && 
                        ParamS.clGlobalVar.webServicePfadNr <= ParamInterneKommunikation.nummerBis) {
                    pfadString = ParamInterneKommunikation.webServicePfadZurAuswahl[ParamS.clGlobalVar.webServicePfadNr];
                }
                zeigeFehlerMitStage("Zugriff auf Web-Service nicht möglich! webServicePfadNr="
                        + ParamS.clGlobalVar.webServicePfadNr + " webServicePfadZurAuswahl=" + pfadString);
                return schliesseHinweisReturnFalse(caZeigeHinweis);
            }

        }

        if (ParamS.eclGeraeteSet.ident < 1) {
            zeigeFehlerMitStage("Geräte-Set falsch eingestellt!");
            return schliesseHinweisReturnFalse(caZeigeHinweis);
        }
        if (ParamS.paramGeraet.identKlasse < 1) {
            zeigeFehlerMitStage("Geräte-Zuordnung/Klasse falsch!");
            return schliesseHinweisReturnFalse(caZeigeHinweis);
        }

        caZeigeHinweis.schliesseNurHinweis();

        if (ParamS.paramGeraet.programmStartKontrollScreenAnzeigen) {
            /*Parameter anzeigen*/
            bRC = zeigeStartParameter();
            if (bRC == false) {
                return false;
            }
        }

        CaBug.druckeLog("D", logDrucken, 10);

        /*Zugangsdaten - Kennung und Passwort, ggf. Mandant - abfragen*/
        ParamGeraet lParamGeraet = new ParamGeraet();
        lParamGeraet = ParamS.paramGeraet;
        if (lParamGeraet.festgelegterMandant == 0 || lParamGeraet.festgelegterMandantIstFix == false
                || lParamGeraet.festgelegtesJahr == 0 || lParamGeraet.festgelegtesJahrIstFix == false
                || lParamGeraet.festgelegteHVNummer.isEmpty() || lParamGeraet.festgelegteHVNummerFix == false
                || lParamGeraet.festgelegteDatenbank.isEmpty() || lParamGeraet.festgelegteDatenbankFix == false
                || lParamGeraet.festgelegterBenutzername.isEmpty()
                || lParamGeraet.festgelegteBenutzernameFix == false) {
            CaBug.druckeLog("D1", logDrucken, 10);
            bRC = fuehreLoginDurch();
            System.out.println("CaProgrammStart bRC=" + bRC);
            if (bRC == false) {
                return false;
            }
        } else {
            CaBug.druckeLog("D2", logDrucken, 10);
            CaPruefeLogin caPruefeLogin = new CaPruefeLogin();
            ParamS.clGlobalVar.mandant = lParamGeraet.festgelegterMandant;
            ParamS.clGlobalVar.hvJahr = lParamGeraet.festgelegtesJahr;
            ParamS.clGlobalVar.hvNummer = lParamGeraet.festgelegteHVNummer;
            ParamS.clGlobalVar.datenbereich = lParamGeraet.festgelegteDatenbank;

            rc = caPruefeLogin.pruefeLogin("", "", false, false); //Kennung wird aus Geräteparam genommen, da ja festgelegt; Passwort nicht erforderlich
            CaBug.druckeLog("D3 rc=" + rc, logDrucken, 10);
            if (rc < 0 && rc != CaFehler.afNeuesPasswortErforderlich) {
                String fehlerText = "";
                switch (rc) {
                case CaFehler.afFalscheKennung:
                case CaFehler.afPasswortFalsch:
                    fehlerText = "Kennung oder Passwort falsch!";
                    break;
                case CaFehler.afKennungGesperrt:
                    fehlerText = "Kennung gesperrt!";
                    break;
                //              case CaFehler.afNeuesPasswortErforderlich:
                //                  fehlerText="Passwort-Änderung erforderlich!";break;
                case CaFehler.afHVNichtVorhanden:
                    fehlerText = "Ausgewählter Emittent / HV nicht vorhanden!";
                    break;
                case CaFehler.afHVMitDieserKennungNichtZulaessig:
                    fehlerText = "Ausgewählter Emittent / HV darf mit dieser Kennung nicht bearbeitet werden!";
                    break;
                }
                zeigeFehlerMitStage(fehlerText);
                return false;
            } else {
                ParamS.eclUserLogin = caPruefeLogin.rcUserLogin;
                ParamS.eclEmittent = caPruefeLogin.rcEmittent;
            }
        }

        CaBug.druckeLog("E", logDrucken, 10);

        System.out.println("UserLogin=" + ParamS.clGlobalVar.benutzernr);
        ParamS.clGlobalVar.benutzernr = ParamS.eclUserLogin.userLoginIdent;

        /*Hier: ParamS.eclUserLogin und ParamS.eclEmittent sind gefüllt*/

        /*ggf. Modul abfragen*/
        int anzMoeglicheModule = 0;
        int gewaehltesModul = 0;

        System.out.println(lParamGeraet.moduleHVMaster);
        System.out.println(ParamS.eclUserLogin.pruefe_modul_moduleHVMaster());

        if (lParamGeraet.moduleHVMaster == true && ParamS.eclUserLogin.pruefe_modul_moduleHVMaster() == true
                && (ParamS.clGlobalVar.datenbankPfadNr != -1 || ParamS.clGlobalVar.webServicePfadNr != -1)) {
            anzMoeglicheModule++;
            gewaehltesModul = 1;
        }
        if (lParamGeraet.moduleFrontOffice == true && ParamS.eclUserLogin.pruefe_modul_moduleFrontOffice() == true
                && ParamS.clGlobalVar.webServicePfadNr != -1) {
            anzMoeglicheModule++;
            gewaehltesModul = 2;
        }
        if (lParamGeraet.moduleKontrolle == true && ParamS.eclUserLogin.pruefe_modul_moduleKontrolle() == true
                && ParamS.clGlobalVar.datenbankPfadNr != -1) {
            anzMoeglicheModule++;
            gewaehltesModul = 3;
        }
        if (lParamGeraet.moduleServiceDesk == true && ParamS.eclUserLogin.pruefe_modul_moduleServiceDesk() == true
                && ParamS.clGlobalVar.webServicePfadNr != -1) {
            anzMoeglicheModule++;
            gewaehltesModul = 4;
        }
        if (lParamGeraet.moduleTeilnahmeverzeichnis == true
                && ParamS.eclUserLogin.pruefe_modul_moduleTeilnahmeverzeichnis() == true
                && ParamS.clGlobalVar.datenbankPfadNr != -1) {
            anzMoeglicheModule++;
            gewaehltesModul = 5;
        }
        if (lParamGeraet.moduleTabletAbstimmung == true
                && ParamS.eclUserLogin.pruefe_modul_moduleTabletAbstimmung() == true
                && ParamS.clGlobalVar.webServicePfadNr != -1) {
            anzMoeglicheModule++;
            gewaehltesModul = 6;
        }
        if (lParamGeraet.moduleBestandsverwaltung == true
                && ParamS.eclUserLogin.pruefe_modul_moduleBestandsverwaltung() == true
                && ParamS.clGlobalVar.webServicePfadNr != -1) {
            anzMoeglicheModule++;
            gewaehltesModul = 7;

        }
        if (lParamGeraet.moduleHotline == true && ParamS.eclUserLogin.pruefe_modul_moduleHotline() == true
                && ParamS.clGlobalVar.webServicePfadNr != -1) {
            anzMoeglicheModule++;
            gewaehltesModul = 8;
        }
        if (lParamGeraet.moduleAktienregisterImport == true
                && ParamS.eclUserLogin.pruefe_modul_moduleAktienregisterImport() == true
                && ParamS.clGlobalVar.datenbankPfadNr != -1) {
            anzMoeglicheModule++;
            gewaehltesModul = 9;
        }
        if (lParamGeraet.moduleDesigner == true && ParamS.eclUserLogin.pruefe_hvMaster_moduleDesigner() == true
                && ParamS.clGlobalVar.datenbankPfadNr != -1) {
            anzMoeglicheModule++;
            gewaehltesModul = 10;
        }

        CaBug.druckeLog("F", logDrucken, 10);

        if (anzMoeglicheModule == 0) {
            zeigeFehlerMitStage("Keine Programmodule auswählbar!");
            return false;
        }

        CaBug.druckeLog("G", logDrucken, 10);

        /*Vollständige Parameter laden*/
        CALeseParameterNeu caLeseParameterNeu = new CALeseParameterNeu();
        rc = caLeseParameterNeu.leseHVParameter();
        /** Ggf. zum Reload der aktuellen Parameter */
        if (ParamS.clGlobalVar.webServicePfadNr != -1) {
            if (rc < 0) {
                zeigeFehlerMitStage("Parameter holen über Web-Service nicht möglich!");
                return false;
            }
            dbBundle = new DbBundle();
        } else {
            dbBundle = new DbBundle();
            dbBundle.openAll();
            dbBundle.closeAll();
        }

        CaBug.druckeLog("G", logDrucken, 10);

        if (anzMoeglicheModule > 1) {
            return true;
        }
        if (anzMoeglicheModule < 1) {
            return false;
        }

        /*Nur ein Modul zum Auswählen*/
        CtrlWaehleModule ctrlWaehleModule = new CtrlWaehleModule();
        ctrlWaehleModule.starteModulVorausgewaehlt(gewaehltesModul);

        return false;
        //        if (anzMoeglicheModule > 1) {
        //            rc = waehleModulAus();
        //            if (rc < 0) {
        //                return false;
        //            }
        //            moeglichesModul = rc;
        //        }

        /*Modul starten*/
        //        zuStartendesModul = moeglichesModul;

        //      if (ParamS.clGlobalVar.webServicePfadNr!=-1){
        //          BlParameterUeberWebService lBlInitHVWebServices=new BlParameterUeberWebService();
        //          rc=lBlInitHVWebServices.holeHVParameter();
        //          if (rc<0){
        //              zeigeFehlerMitStage("Offline keine Programmodule auswählbar!");
        //              return false;
        //          }
        //      }
        //      else{
        //          dbBundle.openAllOhneParameterCheck();
        //          BvReload bvReload=new BvReload(dbBundle);
        //          bvReload.checkReload(dbBundle.clGlobalVar.mandant);
        //
        //          /*Geräte-Parameter / globale Parameter einlesen*/
        //          BlFuelleStaticAusDBAufClient blFuellePufferAusDBAufClient=new BlFuelleStaticAusDBAufClient(dbBundle);
        //          rc=blFuellePufferAusDBAufClient.fuelleMandantenParam(true, bvReload);
        //          rc=blFuellePufferAusDBAufClient.fuelleUserLogin(true, bvReload);
        //          dbBundle.closeAll();
        //
        //          /**Ggf. zum Reload der aktuellen Parameter*/
        //          dbBundle=new DbBundle();dbBundle.openAll();dbBundle.closeAll();
        //
        //      }

        //        return true;
    }

    private boolean schliesseHinweisReturnFalse(CaZeigeHinweis hinweis) {
        hinweis.schliesseNurHinweis();
        return false;
    }

    /**
     * Füllt: ClGlobalVar.webServicePfadNr ClGlobalVar.datenbankPfadNr
     * kommunikationUebergeben
     *
     * @param args the args
     */
    private void dekodiere(String[] args) {

        ParamS.clGlobalVar = new ClGlobalVar();
        ParamS.clGlobalVar.webServicePfadNr = -1;
        ParamS.clGlobalVar.datenbankPfadNr = -1;

        ParamS.clGlobalVar.onlineWebServicePfadNr = -1;
        ParamS.clGlobalVar.onlineDatenbankPfadNr = -1;

        /* INFO Hier Default-Startparameter für Client bearbeiten***************************************************/
        ParamS.clGlobalVar.arbeitsplatz = 1; // 1 oder 101
        ParamS.clGlobalVar.benutzernr = 1; //Wird im Rahmen der Login-Prozedur gesetzt
        ParamS.clGlobalVar.lwPfadZertifikat = "D:\\clientMeeting_Root";

        
        int hOffset=0;
        
        if (args != null) {
            for (int i = 0; i < args.length; i++) {
                if (args[i].startsWith("C=")) { //Connection-Nummer für webServicePfadNr, z.B. 4
                    hOffset=ParamInterneKommunikation.liefereOffsetNeu(Integer.parseInt(args[i].substring(2)));
                    if (hOffset!=-1) {        
                        ParamS.clGlobalVar.webServicePfadNr = hOffset;
                        kommunikationUebergeben = true;
                    }
                }
                if (args[i].startsWith("CO=")) { //Connection-Nummer für onlineWebServicePfadNr, z.B. 4
                    hOffset=ParamInterneKommunikation.liefereOffsetNeu(Integer.parseInt(args[i].substring(3)));
                    if (hOffset!=-1) {        
                        ParamS.clGlobalVar.onlineWebServicePfadNr = hOffset;
                        kommunikationUebergeben = true;
                    }
                }
                if (args[i].startsWith("CI=")) { //IP-Adresse für webServicePfadNr, z.B. 127.1.0.0:8080
                    ParamS.clGlobalVar.webServicePfadNr = ParamInterneKommunikation.freieNummer;
                    ParamInterneKommunikation.webServicePfadZurAuswahl[ParamInterneKommunikation.freieNummer] = "http://" + args[i].substring(3)
                            + "/meetingportal/api/v1/intern/";
                    kommunikationUebergeben = true;
                }
                if (args[i].startsWith("COI=")) { //IP-Adresse für onlineWebServicePfadNr, z.B. 127.1.0.0:8080
                    ParamS.clGlobalVar.onlineWebServicePfadNr = ParamInterneKommunikation.freieOnlineNummer;
                    ParamInterneKommunikation.webServicePfadZurAuswahl[ParamInterneKommunikation.freieOnlineNummer] = "http://" + args[i].substring(4)
                            + "/meetingportal/api/v1/intern/";
                    kommunikationUebergeben = true;
                }

                if (args[i].startsWith("D=")) { //Datenbank-Connection-Nummer für datenbankPfadNr, z.B. 4
                    hOffset=ParamInterneKommunikation.liefereOffsetNeu(Integer.parseInt(args[i].substring(2)));
                    if (hOffset!=-1) {        
                        ParamS.clGlobalVar.datenbankPfadNr = hOffset;
                        kommunikationUebergeben = true;
                    }
                }
                if (args[i].startsWith("DO=")) { //Datenbank-Connection-Nummer für onlineDatenbankPfadNr, z.B. 4
                    hOffset=ParamInterneKommunikation.liefereOffsetNeu(Integer.parseInt(args[i].substring(3)));
                    if (hOffset!=-1) {        
                        ParamS.clGlobalVar.onlineDatenbankPfadNr = hOffset;
                        kommunikationUebergeben = true;
                    }
                }
                if (args[i].startsWith("DI=")) { //IP-Adresse für Datenbank, z.B. 127.1.0.0
                    ParamS.clGlobalVar.datenbankPfadNr = ParamInterneKommunikation.freieNummer;
                    ParamInterneKommunikation.datenbankPfadZurAuswahl[ParamInterneKommunikation.freieNummer] = "jdbc:mysql://" + args[i].substring(3)
                            + ":3306/db_meetingcomfort";
                    kommunikationUebergeben = true;
                }
                if (args[i].startsWith("DOI=")) { //IP-Adresse für Datenbank Online-Server, z.B. 127.1.0.0
                    ParamS.clGlobalVar.onlineDatenbankPfadNr = ParamInterneKommunikation.freieOnlineNummer;
                    ParamInterneKommunikation.datenbankPfadZurAuswahl[ParamInterneKommunikation.freieOnlineNummer] = "jdbc:mysql://" + args[i].substring(4)
                            + ":3306/db_meetingcomfort";
                    kommunikationUebergeben = true;
                }

                if (args[i].startsWith("A=")) { //Arbeitsplatz-Nummer
                    ParamS.clGlobalVar.arbeitsplatz = Integer.parseInt(args[i].substring(2));
                }

                if (args[i].startsWith("Z=")) { //Laufwerk/Pfad für Zertifikat, z.B. D:\MeetingPortal_Root
                    ParamS.clGlobalVar.lwPfadZertifikat = args[i].substring(2);
                }

            }
        }

    }

    /**
     * Zeige fehler mit stage.
     *
     * @param pFehlerText the fehler text
     */
    private void zeigeFehlerMitStage(String pFehlerText) {
        Stage lStage = new Stage();
        CaZeigeHinweis zeigeHinweis = new CaZeigeHinweis();
        zeigeHinweis.zeige(lStage, pFehlerText);
    }

    /**
     * Frage verbindung ab.
     *
     * @return true, if successful
     */
    private boolean frageVerbindungAb() {

        Stage zwischenDialog = new Stage();
        CaIcon.standard(zwischenDialog);

        CtrlVerbindungAuswahl controllerFenster = new CtrlVerbindungAuswahl();

        controllerFenster.init(zwischenDialog);

        CaController caController = new CaController();
        caController.open(zwischenDialog, controllerFenster,
                SParamProgramm.sourcePfad+"meetingclient/meetingClientDialoge/VerbindungAuswahl.fxml", 500, 380,
                "Verbindung auswählen", true);

        boolean bRC = controllerFenster.fortsetzen;
        return bRC;
    }

    /**
     * Zeige start parameter.
     *
     * @return true, if successful
     */
    private boolean zeigeStartParameter() {

        Stage zwischenDialog = new Stage();
        CaIcon.standard(zwischenDialog);

        CtrlHardwareStart controllerFenster = new CtrlHardwareStart();

        controllerFenster.init(zwischenDialog);

        CaController caController = new CaController();
        caController.open(zwischenDialog, controllerFenster,
                SParamProgramm.sourcePfad+"meetingclient/meetingClientDialoge/HardwareStart.fxml", 1000, 650,
                "Startparameter", true);

        boolean bRC = controllerFenster.fortsetzen;
        return bRC;
    }

    /**Führt Dialog zum Login durch:
     * Benutzer und Mandant abfragen.
     *
     * Anschließend gefüllt:
     *  ParamS.eclUserLogin
     *  ParamS.eclEmittent
     *
     * Return-Code:
     *  false="Abbruch"
     *  true="fortsetzen"
     */
    private boolean fuehreLoginDurch() {

        Stage zwischenDialog = new Stage();
        CaIcon.standard(zwischenDialog);

        CtrlLoginNeu controllerFenster = new CtrlLoginNeu();

        controllerFenster.init(zwischenDialog);

        CaController caController = new CaController();
        caController.open(zwischenDialog, controllerFenster,
                SParamProgramm.sourcePfad+"meetingclient/meetingClientDialoge/LoginNeu.fxml", 700, 400, "Login", true);

        boolean bRC = controllerFenster.fortsetzen;
        ParamS.eclUserLogin = controllerFenster.eclUserLogin;
        ParamS.eclEmittent = controllerFenster.eclEmittent;

        return bRC;
    }

    /**
     * Lade user login mit dummy.
     */
    @Deprecated
    /**
     * Zu verwenden für Programmstart ohne Login-Prozedur (also aktuell: alte
     * Programmstarts)
     */
    public void ladeUserLoginMitDummy() {
        ParamS.setEclUserLogin(new EclUserLogin());
        for (int i = 0; i < 20; i++) {
            for (int i1 = 0; i1 < 20; i1++) {
                ParamS.eclUserLogin.berechtigungen[i][i1] = 1;
            }
        }

    }

}
