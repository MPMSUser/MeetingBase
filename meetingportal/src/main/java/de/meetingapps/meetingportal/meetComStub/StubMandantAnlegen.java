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
package de.meetingapps.meetingportal.meetComStub;

import java.util.List;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComBVerwaltung.BvMandanten;
import de.meetingapps.meetingportal.meetComBVerwaltung.BvRuecksetzenUndInitialisieren;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclEmittenten;
import de.meetingapps.meetingportal.meetComEntities.EclIsin;
import de.meetingapps.meetingportal.meetComEntities.EclParameterSet;
import de.meetingapps.meetingportal.meetComEntities.EclPortalText;
import de.meetingapps.meetingportal.meetComHVParam.HVParam;
import de.meetingapps.meetingportal.meetComHVParam.ParamS;
import de.meetingapps.meetingportal.meetComWE.WELoginVerify;

/**Achtung - dieser Stub verwendet und verändert bei einigen Funktionen ParamS - diese Funktionen dürfen keinesfalls
 * aus einer Wildfly-Bean heraus aufgerufen werden!
 */
public class StubMandantAnlegen extends StubRoot {

    private int logDrucken=3;
    
    public StubMandantAnlegen(boolean pIstServer, DbBundle pDbBundle) {
        super(pIstServer, pDbBundle);
    }

    public List<EclEmittenten> emittentenListe = null;
    public EclEmittenten[] mandantenArray = null;

    public HVParam ergHVParam=null;
    public List<EclIsin> isinListe=null;
    public EclPortalText portalTexteArray[] = null;

    /**-1 => Gleicher Server zum Holen verwenden, wie gerade generell ausgewählt.
     * >=0 => Anderen Server zum Holen von Daten zum Einkopieren verwenden (Emittentenliste, Parameter etc.)
     */
    public int holenVonWebServicePfadNr=-1;
    public int holenVonDatenbankPfadNr=0;
 
    private int normalerWebServicePfadNr=-1;
    public int normalerDatenbankPfadNr=0;

    
    private void schalteUmZuHolenWebService() {
        normalerWebServicePfadNr=ParamS.clGlobalVar.webServicePfadNr;
        ParamS.clGlobalVar.webServicePfadNr=holenVonWebServicePfadNr;
        
        wsClient.initClient();
    }
   
    private void schalteUmZuNormalemWebService() {
        ParamS.clGlobalVar.webServicePfadNr=normalerWebServicePfadNr;
        wsClient.initClient();
    }

    private void schalteUmZuHolenDatenbankService() {
        normalerDatenbankPfadNr=ParamS.clGlobalVar.datenbankPfadNr;
        ParamS.clGlobalVar.datenbankPfadNr=holenVonDatenbankPfadNr;
        
        wsClient.initClient();
    }
   
    private void schalteUmZuNormalemDatenbankService() {
        ParamS.clGlobalVar.datenbankPfadNr=normalerDatenbankPfadNr;
        wsClient.initClient();
    }

    /**1
     * Verwendet ParamS!*/
    public int holeEmittentenFuerAuswahl() {

        if (verwendeWebService()) {
            WEStubMandantAnlegen weStubMandantAnlegen = new WEStubMandantAnlegen();
            weStubMandantAnlegen.stubFunktion = 1;

            WELoginVerify weLoginVerify = new WELoginVerify();
            weStubMandantAnlegen.setWeLoginVerify(weLoginVerify);

            if (holenVonWebServicePfadNr!=-1) {
                schalteUmZuHolenWebService();
            }
            
            WEStubMandantAnlegenRC weStubMandantAnlegenRC = wsClient.stubMandantAnlegen(weStubMandantAnlegen);
            
            if (holenVonWebServicePfadNr!=-1) {
                schalteUmZuNormalemWebService();
            }

            
            if (weStubMandantAnlegenRC.rc < 1) {
                return weStubMandantAnlegenRC.rc;
            }

            emittentenListe = weStubMandantAnlegenRC.emittentenListe;
            mandantenArray = weStubMandantAnlegenRC.mandantenArray;

            return weStubMandantAnlegenRC.rc;
        }

        if (holenVonWebServicePfadNr!=-1) {
            schalteUmZuHolenDatenbankService();
        }
       
        CaBug.druckeLog("Vor Holen", logDrucken, 10);        
        dbOpenOhneParameterCheck();
        BvMandanten bvMandanten = new BvMandanten();
        emittentenListe = bvMandanten.liefereEmittentenListeFuerNrAuswahl(lDbBundle);
        mandantenArray = bvMandanten.liefereEmittentenListeAlle(lDbBundle);
        dbClose();
        CaBug.druckeLog("Nach Holen", logDrucken, 10);        
        
        if (holenVonWebServicePfadNr!=-1) {
            schalteUmZuNormalemDatenbankService();
        }

        return 1;
    }

    /*2*/
    public int anlegenTablesUndEmittent(EclEmittenten pEmittenten) {

        if (verwendeWebService()) {
            WEStubMandantAnlegen weStubMandantAnlegen = new WEStubMandantAnlegen();
            weStubMandantAnlegen.stubFunktion = 2;
            weStubMandantAnlegen.pEmittenten = pEmittenten;
            WELoginVerify weLoginVerify = new WELoginVerify();
            weStubMandantAnlegen.setWeLoginVerify(weLoginVerify);

            WEStubMandantAnlegenRC weStubMandantAnlegenRC = wsClient.stubMandantAnlegen(weStubMandantAnlegen);

            if (weStubMandantAnlegenRC.rc < 1) {
                return weStubMandantAnlegenRC.rc;
            }

            return weStubMandantAnlegenRC.rc;
        }

        dbOpenMitNew();
        BvMandanten bvMandanten = new BvMandanten();
        int rc = bvMandanten.legeMandantNeuAn(lDbBundle, pEmittenten, true);
        dbClose();
        return rc;

    }

    /*3*/
    public int verwendeParameterSet(EclParameterSet pParameterSet) {

        if (verwendeWebService()) {
            WEStubMandantAnlegen weStubMandantAnlegen = new WEStubMandantAnlegen();
            weStubMandantAnlegen.stubFunktion = 3;
            weStubMandantAnlegen.pParameterSet = pParameterSet;
            WELoginVerify weLoginVerify = new WELoginVerify();
            weStubMandantAnlegen.setWeLoginVerify(weLoginVerify);

            WEStubMandantAnlegenRC weStubMandantAnlegenRC = wsClient.stubMandantAnlegen(weStubMandantAnlegen);

            if (weStubMandantAnlegenRC.rc < 1) {
                return weStubMandantAnlegenRC.rc;
            }

            return weStubMandantAnlegenRC.rc;
        }

        dbOpenMitNew();
        lDbBundle.openWeitere();
        lDbBundle.dbParameter.nutzeSetBeginn(pParameterSet.ident);
        lDbBundle.dbParameter.readHVParam_all();
        
        /*Hinweis: Menü- und Kontaktanfrage-Themen werden derzeit noch nicht kopiert 
         * und deshalb hier nicht geladen.
         */

        lDbBundle.dbParameter.nutzeSetEnde();

        lDbBundle.param = lDbBundle.dbParameter.ergHVParam;
        lDbBundle.dbParameter.updateHVParam_all();
        CaBug.druckeLog("update--------------------------------", logDrucken, 10);
        CaBug.druckeLog("Mandant" + lDbBundle.getMandantPfad() + " " + lDbBundle.clGlobalVar.mandant, logDrucken, 10);

        dbClose();

        return 1;
    }

    private int mandant = 0;
    private int hvJahr = 0;
    private String hvNummer = "";
    private String datenbereich = "";

    private void speichereMandantenKey() {
        mandant = lDbBundle.clGlobalVar.mandant;
        hvJahr = lDbBundle.clGlobalVar.hvJahr;
        hvNummer = lDbBundle.clGlobalVar.hvNummer;
        datenbereich = lDbBundle.clGlobalVar.datenbereich;
    }

    private void restoreMandantenKey() {
        lDbBundle.clGlobalVar.mandant = mandant;
        lDbBundle.clGlobalVar.hvJahr = hvJahr;
        lDbBundle.clGlobalVar.hvNummer = hvNummer;
        lDbBundle.clGlobalVar.datenbereich = datenbereich;
    }

    /**4
     * holt HV-Parameter und ISINs vom Quell-Mandant*/
    public int verwendeMandantHoleParameter(EclEmittenten pMandant) {

        if (verwendeWebService()) {
            speichereMandantenKey();
            lDbBundle.clGlobalVar.mandant = pMandant.mandant;
            lDbBundle.clGlobalVar.hvJahr = pMandant.hvJahr;
            lDbBundle.clGlobalVar.hvNummer = pMandant.hvNummer;
            lDbBundle.clGlobalVar.datenbereich = pMandant.dbArt;

            
            WEStubMandantAnlegen weStubMandantAnlegen = new WEStubMandantAnlegen();
            weStubMandantAnlegen.stubFunktion = 4;
            weStubMandantAnlegen.pEmittenten = pMandant;
            WELoginVerify weLoginVerify = new WELoginVerify();
            weStubMandantAnlegen.setWeLoginVerify(weLoginVerify);

            if (holenVonWebServicePfadNr!=-1) {
                schalteUmZuHolenWebService();
            }
 
            WEStubMandantAnlegenRC weStubMandantAnlegenRC = wsClient.stubMandantAnlegen(weStubMandantAnlegen);

            if (holenVonWebServicePfadNr!=-1) {
                schalteUmZuNormalemWebService();
            }

            restoreMandantenKey();

            if (weStubMandantAnlegenRC.rc < 1) {
                return weStubMandantAnlegenRC.rc;
            }
            ergHVParam=weStubMandantAnlegenRC.ergHVParam;
            isinListe=weStubMandantAnlegenRC.isinListe;
            portalTexteArray=weStubMandantAnlegenRC.portalTexteArray;
            
            CaBug.druckeLog(ergHVParam.paramPortal.stimmrechtsvertreterNameDE, logDrucken, 10);
            
            return weStubMandantAnlegenRC.rc;
        }

        speichereMandantenKey();

        if (holenVonWebServicePfadNr!=-1) {
            schalteUmZuHolenDatenbankService();
        }

        dbOpenOhneParameterCheck();
        dbOpenWeitere();
        lDbBundle.clGlobalVar.mandant = pMandant.mandant;
        lDbBundle.clGlobalVar.hvJahr = pMandant.hvJahr;
        lDbBundle.clGlobalVar.hvNummer = pMandant.hvNummer;
        lDbBundle.clGlobalVar.datenbereich = pMandant.dbArt;

        lDbBundle.dbParameter.readHVParam_all();
        ergHVParam = lDbBundle.dbParameter.ergHVParam;

        lDbBundle.dbIsin.readAll();
        isinListe=lDbBundle.dbIsin.ergebnis();
 
        lDbBundle.dbPortalTexte.read_all(-1, true, false, 0);
        portalTexteArray=lDbBundle.dbPortalTexte.ergebnis();

        /*Hinweis: Menü- und Kontaktanfrage-Themen werden derzeit noch nicht kopiert 
         * und deshalb hier nicht geladen.
         */

        restoreMandantenKey();


        dbClose();
        
        if (holenVonWebServicePfadNr!=-1) {
            schalteUmZuNormalemDatenbankService();
        }

        return 1;
    }

    /**9
     * schreibt HV-Parameter in neuen Mandant. Vorher verwendeMandantHoleParameter aufrufen!*/
    public int verwendeMandantSchreibeParameter() {

        if (verwendeWebService()) {
            WEStubMandantAnlegen weStubMandantAnlegen = new WEStubMandantAnlegen();
            weStubMandantAnlegen.stubFunktion = 9;
            WELoginVerify weLoginVerify = new WELoginVerify();
            weStubMandantAnlegen.setWeLoginVerify(weLoginVerify);
            weStubMandantAnlegen.ergHVParam=ergHVParam;

            WEStubMandantAnlegenRC weStubMandantAnlegenRC = wsClient.stubMandantAnlegen(weStubMandantAnlegen);

            if (weStubMandantAnlegenRC.rc < 1) {
                return weStubMandantAnlegenRC.rc;
            }

            return weStubMandantAnlegenRC.rc;
        }

        dbOpenOhneParameterCheck();
        CaBug.druckeLog(ergHVParam.paramPortal.stimmrechtsvertreterNameDE, logDrucken, 10);
        CaBug.druckeLog("lDbBundle.clGlobalVar.hvJahr="+lDbBundle.clGlobalVar.hvJahr, logDrucken, 10);
        lDbBundle.param = ergHVParam;
        lDbBundle.dbParameter.ergHVParam=ergHVParam;
        lDbBundle.dbParameter.updateHVParam_allForce();
        dbClose();

        return 1;
    }

    /**5
     * schreibt Emittenten-Daten in neuen Mandant. Vorher verwendeMandantHoleParameter aufrufen!*/
    public int uebernehmeEmittentendaten(EclEmittenten pMandant) {

        if (verwendeWebService()) {
            WEStubMandantAnlegen weStubMandantAnlegen = new WEStubMandantAnlegen();
            weStubMandantAnlegen.stubFunktion = 5;
            weStubMandantAnlegen.pEmittenten = pMandant;
            weStubMandantAnlegen.ergHVParam=ergHVParam;
            weStubMandantAnlegen.isinListe=isinListe;
            WELoginVerify weLoginVerify = new WELoginVerify();
            weStubMandantAnlegen.setWeLoginVerify(weLoginVerify);

            WEStubMandantAnlegenRC weStubMandantAnlegenRC = wsClient.stubMandantAnlegen(weStubMandantAnlegen);

            if (weStubMandantAnlegenRC.rc < 1) {
                return weStubMandantAnlegenRC.rc;
            }

            return weStubMandantAnlegenRC.rc;
        }

        dbOpenOhneParameterCheck();

        /**EmittentenDaten selbst (aus EclEmittenten)*/
        EclEmittenten aktuellerEmittent = lDbBundle.eclEmittent;
//        lDbBundle.dbEmittenten.readEmittentHV(pMandant.mandant, pMandant.hvJahr, pMandant.hvNummer, pMandant.dbArt);
//        aktuellerEmittent.copyOhneKeyUndKritischeDatenFrom(lDbBundle.dbEmittenten.ergebnisPosition(0));
        aktuellerEmittent.copyOhneKeyUndKritischeDatenFrom(pMandant);
        lDbBundle.eclEmittent = aktuellerEmittent;
        lDbBundle.dbEmittenten.update(aktuellerEmittent);

        /**Kapital-Werte*/
//        speichereMandantenKey();
//        lDbBundle.clGlobalVar.mandant = pMandant.mandant;
//        lDbBundle.clGlobalVar.hvJahr = pMandant.hvJahr;
//        lDbBundle.clGlobalVar.hvNummer = pMandant.hvNummer;
//        lDbBundle.clGlobalVar.datenbereich = pMandant.dbArt;
//        lDbBundle.dbParameter.readHVParam_all();
        
        /*Hinweis: Menü- und Kontaktanfrage-Themen werden derzeit noch nicht kopiert 
         * und deshalb hier nicht geladen.
         */
//        restoreMandantenKey();
        
        for (int i = 0; i < 5; i++) {
            lDbBundle.param.paramBasis.grundkapitalStueck[i] = ergHVParam.paramBasis.grundkapitalStueck[i];
            lDbBundle.param.paramBasis.grundkapitalVermindertStueck[i] = ergHVParam.paramBasis.grundkapitalVermindertStueck[i];
            lDbBundle.param.paramBasis.grundkapitalEigeneAktienStueck[i] = ergHVParam.paramBasis.grundkapitalEigeneAktienStueck[i];
            lDbBundle.param.paramBasis.wertEinerAktie[i] = ergHVParam.paramBasis.wertEinerAktie[i];
            lDbBundle.param.paramBasis.anzahlNachkommastellenKapital[i] = ergHVParam.paramBasis.anzahlNachkommastellenKapital[i];
            lDbBundle.param.paramBasis.eintrittskarteNeuVergeben[i] = ergHVParam.paramBasis.eintrittskarteNeuVergeben[i];
            lDbBundle.param.paramBasis.zugangMoeglich[i] = ergHVParam.paramBasis.zugangMoeglich[i];
       }
        lDbBundle.dbParameter.ergHVParam = lDbBundle.param;
        lDbBundle.dbParameter.updateHVParam_all();
        dbClose();
        return 1;
    }

    /**6
     * Vorher verwendeMandantHoleParameter aufrufen!*/
    public int uebernehmePortalAppTexte() {

        if (verwendeWebService()) {
            WEStubMandantAnlegen weStubMandantAnlegen = new WEStubMandantAnlegen();
            weStubMandantAnlegen.stubFunktion = 6;
            weStubMandantAnlegen.portalTexteArray=portalTexteArray;
            WELoginVerify weLoginVerify = new WELoginVerify();
            weStubMandantAnlegen.setWeLoginVerify(weLoginVerify);

            WEStubMandantAnlegenRC weStubMandantAnlegenRC = wsClient.stubMandantAnlegen(weStubMandantAnlegen);

            if (weStubMandantAnlegenRC.rc < 1) {
                return weStubMandantAnlegenRC.rc;
            }

            return weStubMandantAnlegenRC.rc;
        }

        dbOpenMitNew();
//        speichereMandantenKey();
//        lDbBundle.clGlobalVar.mandant = pMandant.mandant;
//        lDbBundle.clGlobalVar.hvJahr = pMandant.hvJahr;
//        lDbBundle.clGlobalVar.hvNummer = pMandant.hvNummer;
//        lDbBundle.clGlobalVar.datenbereich = pMandant.dbArt;
//        lDbBundle.dbPortalTexte.read_all(-1, true, false, 0);
//        restoreMandantenKey();
        lDbBundle.dbPortalTexte.speichereAll(portalTexteArray, true);

        dbClose();
        return 1;
    }

    /*7*/
    public int sammelkartenSandardAnlegen() {

        if (verwendeWebService()) {
            WEStubMandantAnlegen weStubMandantAnlegen = new WEStubMandantAnlegen();
            weStubMandantAnlegen.stubFunktion = 7;
            WELoginVerify weLoginVerify = new WELoginVerify();
            weStubMandantAnlegen.setWeLoginVerify(weLoginVerify);

            WEStubMandantAnlegenRC weStubMandantAnlegenRC = wsClient.stubMandantAnlegen(weStubMandantAnlegen);

            if (weStubMandantAnlegenRC.rc < 1) {
                return weStubMandantAnlegenRC.rc;
            }

            return weStubMandantAnlegenRC.rc;
        }

        dbOpenUndWeitereMitNew();
        BvRuecksetzenUndInitialisieren blRuecksetzenUndInitialisieren = new BvRuecksetzenUndInitialisieren(lDbBundle);
        blRuecksetzenUndInitialisieren.anlegenSammelkartenStandard();
        dbClose();
        return 1;

    }
    
    /*8*/
    public int sammelkartenSandardAnlegenErgaenzen() {

        if (verwendeWebService()) {
            WEStubMandantAnlegen weStubMandantAnlegen = new WEStubMandantAnlegen();
            weStubMandantAnlegen.stubFunktion = 8;
            WELoginVerify weLoginVerify = new WELoginVerify();
            weStubMandantAnlegen.setWeLoginVerify(weLoginVerify);

            WEStubMandantAnlegenRC weStubMandantAnlegenRC = wsClient.stubMandantAnlegen(weStubMandantAnlegen);

            if (weStubMandantAnlegenRC.rc < 1) {
                return weStubMandantAnlegenRC.rc;
            }

            return weStubMandantAnlegenRC.rc;
        }

        dbOpenMitNew();
        BvRuecksetzenUndInitialisieren blRuecksetzenUndInitialisieren = new BvRuecksetzenUndInitialisieren(lDbBundle);
        blRuecksetzenUndInitialisieren.anlegenSammelkartenStandardErgaenzen();
        dbClose();
        return 1;

    }

}
