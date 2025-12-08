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
package de.meetingapps.meetingportal.meetComBl;

import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComBVerwaltung.BvReload;
import de.meetingapps.meetingportal.meetComBVerwaltung.BvUserLogin;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclEmittenten;
import de.meetingapps.meetingportal.meetComEntities.EclUserLogin;
import de.meetingapps.meetingportal.meetComHVParam.HVParam;
import de.meetingapps.meetingportal.meetComHVParam.ParamGeraet;
import de.meetingapps.meetingportal.meetComHVParam.ParamS;

/*FIXME Labeldrucker noch besetzen. "0" => keine Zuordnung!*/

/**Füllt Static-Parameter-Variablen aus Datenbank-Direktzugriff.
 * 
 * Nach dem Aufruf dieser Funktion sollte DbBundle.refreshParameterAusStatic
 * aufgerufen werden, damit die Daten auch nach DbBundle übernommen werden 
 * */
public class BlFuelleStaticAusDBAufClient {

    private int logDrucken=9;
    
    private DbBundle dbBundle = null;

    public BlFuelleStaticAusDBAufClient(DbBundle pDbBundle) {
        dbBundle = pDbBundle;
    }

    /**Zwei Aufrufvarianten:
     * a) pImmerLaden=true => alle Parameter werden eingelesen, und Version gemäß bvReload belegt.
     * b) pImmerLaden=false, bvReload (vorbelegt), und Parameter sind bereits eingelesen => 
     * 		Parameter werden eingelesen, wenn andere Version im Speicher ist
     * 
     * Fehler-Return-Codes:
     * sysGeraeteSetNichtVorhanden
     * sysGeraeteKlasseSetZuordnungNichtVorhanden
     * sysGeraeteKlasseNichtVorhanden
     */
    public int fuelleGlobalParam(boolean pImmerLaden, BvReload pBvReload) {

        /*Server-Parameter*/
        boolean reloadServer = false;
        if (pImmerLaden || ParamS.paramServer.reloadVersionParameterServer != pBvReload.reloadParameterServer) {
            dbBundle.dbParameter.readServer_all();
            dbBundle.dbParameter.ergParamServer.reloadVersionParameterServer = pBvReload.reloadParameterServer;
            ParamS.paramServer = dbBundle.dbParameter.ergParamServer;
            dbBundle.paramServer = dbBundle.dbParameter.ergParamServer;
            reloadServer = true;
        }

        /*Geräte-Set*/
        boolean reloadGeraete = false;
        if (pImmerLaden || reloadServer
                || ParamS.eclGeraeteSet.reloadVersionGeraete != pBvReload.reloadParameterGeraete) {
            if (dbBundle.paramServer.geraeteSetIdent < 1) {
                return CaFehler.sysGeraeteSetNichtVorhanden;
            }
            dbBundle.dbGeraeteSet.read(dbBundle.paramServer.geraeteSetIdent);
            if (dbBundle.dbGeraeteSet.anzErgebnis() < 1) {
                return CaFehler.sysGeraeteSetNichtVorhanden;
            }
            dbBundle.dbGeraeteSet.ergebnisPosition(0).reloadVersionGeraete = pBvReload.reloadParameterGeraete;
            ParamS.eclGeraeteSet = dbBundle.dbGeraeteSet.ergebnisPosition(0);
            dbBundle.eclGeraeteSet = dbBundle.dbGeraeteSet.ergebnisPosition(0);
            reloadGeraete = true;
        }

        /*Geräte-Set-Zuordnung*/
        if (pImmerLaden || reloadGeraete) {
            dbBundle.dbGeraeteKlasseSetZuordnung.read(dbBundle.paramServer.geraeteSetIdent,
                    dbBundle.clGlobalVar.arbeitsplatz);
            if (dbBundle.dbGeraeteKlasseSetZuordnung.anzErgebnis() < 1) {
                System.out.println("BlFuellePufferAusDBAufCLient Fehler return");
                return CaFehler.sysGeraeteKlasseSetZuordnungNichtVorhanden;
            }
            ParamS.eclGeraetKlasseSetZuordnung = dbBundle.dbGeraeteKlasseSetZuordnung.ergebnisPosition(0);
            dbBundle.eclGeraetKlasseSetZuordnung = dbBundle.dbGeraeteKlasseSetZuordnung.ergebnisPosition(0);
        }

        /*Geräte-Klasse*/
        if (pImmerLaden || reloadGeraete) {
            ParamGeraet hParamGeraet = null;
            int rc = dbBundle.dbParameter.readGerateKlasse_all(dbBundle.eclGeraetKlasseSetZuordnung.geraeteKlasseIdent);
            if (rc < 1) {
                return CaFehler.sysGeraeteKlasseNichtVorhanden;
            }
            hParamGeraet = dbBundle.dbParameter.ergParamGeraet;//Nur zur Tipp-Erleichterung
            hParamGeraet.serverArt = ParamS.paramServer.serverArt;
            hParamGeraet.serverBezeichnung = ParamS.paramServer.serverBezeichnung;
            if (hParamGeraet.festgelegterMandant > 0) {
                dbBundle.dbEmittenten.readMandantenbezeichnung(hParamGeraet.festgelegterMandant);
                if (dbBundle.dbEmittenten.anzErgebnis() > 0) {
                    hParamGeraet.festgelegterMandantText = dbBundle.dbEmittenten.ergebnisPosition(0).bezeichnungKurz;
                }
            }

            ParamS.paramGeraet = hParamGeraet;
            dbBundle.paramGeraet = hParamGeraet;
        }

        fuelleClGLobalVarAusParamGeraet();
        dbBundle.clGlobalVar = ParamS.clGlobalVar;
        return 1;
    }

    /**Füllt die ClGlobalVar-Werte aus ParamS.paramGeraet*/
    public void fuelleClGLobalVarAusParamGeraet() {
        ParamS.clGlobalVar.lwPfadAllgemein = ParamS.paramGeraet.lwPfadAllgemein;
        ParamS.clGlobalVar.lwPfadGrossdokumente = ParamS.paramGeraet.lwPfadGrossdokumente;
        ParamS.clGlobalVar.lwPfadSicherung1 = ParamS.paramGeraet.lwPfadSicherung1;
        ParamS.clGlobalVar.lwPfadSicherung2 = ParamS.paramGeraet.lwPfadSicherung2;
        ParamS.clGlobalVar.lwPfadExportFuerPraesentation = ParamS.paramGeraet.lwPfadExportFuerPraesentation;
        ParamS.clGlobalVar.lwPfadExportFuerBuehnensystem = ParamS.paramGeraet.lwPfadExportFuerBuehnensystem;
        ParamS.clGlobalVar.lwPfadExportExcelFuerPowerpoint = ParamS.paramGeraet.lwPfadExportExcelFuerPowerpoint;
        ParamS.clGlobalVar.lwPfadKundenordnerBasis = ParamS.paramGeraet.lwPfadKundenordnerBasis;
    }

    public int fuelleMandantenParam(boolean pImmerLaden, BvReload pBvReload) {

        /*Emittenten*/
        if (pImmerLaden || ParamS.eclEmittent.reloadVersionEmittenten != pBvReload.reloadEmittenten) {
            EclEmittenten lEmittenten = null;

            dbBundle.dbEmittenten.readEmittentHV(ParamS.clGlobalVar.mandant, ParamS.clGlobalVar.hvJahr,
                    ParamS.clGlobalVar.hvNummer, ParamS.clGlobalVar.datenbereich);
            lEmittenten = dbBundle.dbEmittenten.ergebnisPosition(0);
            lEmittenten.reloadVersionEmittenten = pBvReload.reloadEmittenten;
            ParamS.eclEmittent = lEmittenten;
            dbBundle.eclEmittent = lEmittenten;
        }

        /*HV-Parameter*/
        if (pImmerLaden || ParamS.param.reloadVersionParameter != pBvReload.reloadParameter) {
            HVParam lHVParam = null;

            /*HVParam*/
            dbBundle.dbParameter.readHVParam_all();
            lHVParam = dbBundle.dbParameter.ergHVParam;
            lHVParam.reloadVersionParameter = pBvReload.reloadParameter;
            ParamS.param = lHVParam;
            dbBundle.param = lHVParam;
            
            /*Hinweis: Menü- und Kontaktanfrage-Themen werden beim Client nicht benötigt 
             * und deshalb hier nicht geladen.
             */

            /*Termine - technisch*/
            dbBundle.dbTermine.readAll_technisch(0);
            ParamS.terminlisteTechnisch = dbBundle.dbTermine.ergebnisArray;
            dbBundle.terminlisteTechnisch = dbBundle.dbTermine.ergebnisArray;

        }

        return 1;
    }

    /**Kann immer aufgerufen werden - "noch kein User eingeladen" und trotzdem pImmerLaden=false ist abgefangen!*/
    public int fuelleUserLogin(boolean pImmerLaden, BvReload pBvReload) {
        
        if (pImmerLaden || (ParamS.eclUserLogin != null
                && ParamS.eclUserLogin.reloadVersionUserLogin != pBvReload.reloadUserLogin)) {
            if (ParamS.clGlobalVar.benutzernr == 9999) {
                EclUserLogin lUserLogin = new EclUserLogin();
                lUserLogin.reloadVersionUserLogin = pBvReload.reloadUserLogin;
                ParamS.setEclUserLogin(lUserLogin);
                dbBundle.eclUserLogin = lUserLogin;
                return -1; //Diese Routine wird nur auf dem Client - Kommunikation über DB - verwendet. 9999 heißt in diesem Fall, noch kein User initiiert.
            }
            
            
            dbBundle.dbUserLogin.leseZuIdent(ParamS.clGlobalVar.benutzernr);
            EclUserLogin lUserLogin = dbBundle.dbUserLogin.userLoginArray[0];
            lUserLogin.reloadVersionUserLogin = pBvReload.reloadUserLogin;
            
            BvUserLogin bvUserLogin=new BvUserLogin();
            bvUserLogin.rcUserLogin=lUserLogin;
            bvUserLogin.ergaenzeRcUserLoginUmProfile(dbBundle);
            
            
            ParamS.setEclUserLogin(lUserLogin);
            dbBundle.eclUserLogin = lUserLogin;
        }

        return 1;
    }

    /**Test-Funktion: gibt die Versionen aus den Static-Variablen auf der Console aus*/
    public void testeStatic() {
        System.out.println("Reload-Version in der jeweiligen Parameter-Entity");
        System.out.println("paramServer " + ParamS.paramServer.reloadVersionParameterServer);
        System.out.println("eclGeraeteSet " + ParamS.eclGeraeteSet.reloadVersionGeraete);
        System.out.println("eclEmittent " + ParamS.eclEmittent.reloadVersionEmittenten);
        System.out.println("param.reloadVersionParameter " + ParamS.param.reloadVersionParameter);
        System.out.println("param.reloadVersionFehlermeldungen " + ParamS.param.reloadVersionFehlermeldungen);
        System.out.println("eclUserLogin " + ParamS.eclUserLogin.reloadVersionUserLogin);
    }
}
