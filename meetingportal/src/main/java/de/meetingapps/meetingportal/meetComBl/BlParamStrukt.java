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

import java.util.LinkedList;
import java.util.List;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComAllg.CaString;
import de.meetingapps.meetingportal.meetComBVerwaltung.BvReload;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclAbstimmungsblock;
import de.meetingapps.meetingportal.meetComEntities.EclEmittenten;
import de.meetingapps.meetingportal.meetComEntities.EclNummernFormSet;
import de.meetingapps.meetingportal.meetComEntities.EclParameter;
import de.meetingapps.meetingportal.meetComHVParam.ParamStrukt;
import de.meetingapps.meetingportal.meetComHVParam.ParamStruktGruppen;
import de.meetingapps.meetingportal.meetComHVParam.ParamStruktGruppenHeader;
import de.meetingapps.meetingportal.meetComHVParam.ParamStruktPresetArt;
import de.meetingapps.meetingportal.meetComHVParam.ParamStruktStandard;
import de.meetingapps.meetingportal.meetComHVParam.ParamStruktVersammlungstyp;
import de.meetingapps.meetingportal.meetComHVParam.ParamStruktWerte;
import de.meetingapps.meetingportal.meetComStub.StubRoot;
import de.meetingapps.meetingportal.meetComStub.WEStubParamStrukt;
import de.meetingapps.meetingportal.meetComStub.WEStubParamStruktRC;
import de.meetingapps.meetingportal.meetComWE.WELoginVerify;

public class BlParamStrukt extends StubRoot {
    
    private int logDrucken=10;
    
    public List<ParamStruktVersammlungstyp> paramStruktVersammlungstypListe=null;
    
    public List<ParamStruktPresetArt> paramStruktPresetArtListe=null;
    
    public ParamStruktGruppenHeader paramStruktGruppeHeader=null;
    public EclEmittenten emittent=null;
    private boolean emittentGefuellt=false;

    public List<ParamStrukt> paramStruktListe=null;
    
    public List<ParamStruktGruppenHeader> paramStruktGruppenHeaderListe=null;
    
    public BlParamStrukt(boolean pIstServer, DbBundle pDbBundle) {
        super(pIstServer, pDbBundle);
      }
    
    
    /****************************Versammlungstypen lesen******************/
    /**Funktion=2
     * Füllt paramStruktVersammlungstypListe
     * pPresetArt=0 bis 9*/
    public int leseVersammlungstypen(int pPresetArt) {
        if (verwendeWebService()) {
            WEStubParamStrukt weStubParamStrukt = new WEStubParamStrukt();
            weStubParamStrukt.stubFunktion = 2;
            weStubParamStrukt.presetArt=pPresetArt;
            
            WELoginVerify weLoginVerify = new WELoginVerify();
            weStubParamStrukt.setWeLoginVerify(weLoginVerify);

            WEStubParamStruktRC weStubParamStruktRC = wsClient.stubParamStrukt(weStubParamStrukt);

            if (weStubParamStruktRC.rc < 1) {
                return weStubParamStruktRC.rc;
            }

            paramStruktVersammlungstypListe=weStubParamStruktRC.paramStruktVersammlungstypListe;
            
            return weStubParamStruktRC.rc;
        }

        
        dbOpen();
        lDbBundle.openParamStrukt();
        
        lDbBundle.dbParamStruktVersammlungstyp.readAll(pPresetArt);
        
        paramStruktVersammlungstypListe=lDbBundle.dbParamStruktVersammlungstyp.ergebnis();
        
        dbClose();
        
        return 1;
    }
    
    
    /*******************************Einlesen der Parameter zum Bearbeiten**************************************/
    /**Funktion=1
     * Füllt paramStruktGruppeListe*/
    public int leseParameterGruppeZumBearbeiten(int pParamStruktGruppe){
        
        if (verwendeWebService()) {
            WEStubParamStrukt weStubParamStrukt = new WEStubParamStrukt();
            weStubParamStrukt.stubFunktion = 1;
            weStubParamStrukt.paramStruktGruppe=pParamStruktGruppe;

            WELoginVerify weLoginVerify = new WELoginVerify();
            weStubParamStrukt.setWeLoginVerify(weLoginVerify);

            WEStubParamStruktRC weStubParamStruktRC = wsClient.stubParamStrukt(weStubParamStrukt);

            if (weStubParamStruktRC.rc < 1) {
                return weStubParamStruktRC.rc;
            }

            paramStruktGruppeHeader=weStubParamStruktRC.paramStruktGruppeHeader;
            emittent=weStubParamStruktRC.emittent;
            
            return weStubParamStruktRC.rc;
        }

        
        dbOpen();
        lDbBundle.openParamStrukt();
        
        leseParameterStruktGruppe(pParamStruktGruppe);
        
        
        dbClose();
        
        return 1;
        
    }

    
    /*******************************Speichern der Parameter nach Bearbeiten**************************************/
    /**Funktion=3
     * */
    public int speichereParameterGruppe(ParamStruktGruppenHeader pParamStruktGruppe[], EclEmittenten pEmittent){
        
        if (verwendeWebService()) {
            WEStubParamStrukt weStubParamStrukt = new WEStubParamStrukt();
            weStubParamStrukt.stubFunktion = 3;
            weStubParamStrukt.paramStruktGruppeHeader=pParamStruktGruppe;
            weStubParamStrukt.emittent=pEmittent;
            if (pEmittent==null) {
                CaBug.druckeLog("pEmittent==null", logDrucken, 10);
            }

            WELoginVerify weLoginVerify = new WELoginVerify();
            weStubParamStrukt.setWeLoginVerify(weLoginVerify);

            WEStubParamStruktRC weStubParamStruktRC = wsClient.stubParamStrukt(weStubParamStrukt);

            if (weStubParamStruktRC.rc < 1) {
                return weStubParamStruktRC.rc;
            }

            return weStubParamStruktRC.rc;
        }

        
        dbOpen();
        lDbBundle.openParamStrukt();
        
        CaBug.druckeLog("vor speichereParameterGruppeEmittent" , logDrucken, 10);
        int rc=speichereParameterGruppeEmittent(pParamStruktGruppe, pEmittent);
        if (rc<1) {
            /*Emittenten-Speichern nicht möglich*/
            dbClose();
            return CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert;
        }
        
        CaBug.druckeLog("vor speichereParameterGruppeSonstige" , logDrucken, 10);
        rc=speichereParameterGruppeSonstige(pParamStruktGruppe);
        if (rc<1) {
            /*Parameter-Speichern nicht möglich*/
            dbClose();
            return CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert;
        }
        
        dbClose();
        
        return 1;
        
    }

    /**Funktion=6*/
    public int leseGruppenheaderFuerGruppenAuswahl() {
        if (verwendeWebService()) {
            WEStubParamStrukt weStubParamStrukt = new WEStubParamStrukt();
            weStubParamStrukt.stubFunktion = 6;

            WELoginVerify weLoginVerify = new WELoginVerify();
            weStubParamStrukt.setWeLoginVerify(weLoginVerify);

            WEStubParamStruktRC weStubParamStruktRC = wsClient.stubParamStrukt(weStubParamStrukt);

            if (weStubParamStruktRC.rc < 1) {
                return weStubParamStruktRC.rc;
            }

            paramStruktGruppenHeaderListe=weStubParamStruktRC.paramStruktGruppenHeaderListe;
            
            return weStubParamStruktRC.rc;
        }

        
        dbOpen();
        lDbBundle.openParamStrukt();
        
        lDbBundle.dbParamStruktGruppenHeader.readAlleFuerAuswahl();
        
        paramStruktGruppenHeaderListe=lDbBundle.dbParamStruktGruppenHeader.ergebnis();
        
        dbClose();
        
        return 1;
        
    }
    
    
    private int speichereParameterGruppeEmittent(ParamStruktGruppenHeader pParamStruktGruppe[], EclEmittenten pEmittent) {
        boolean emittentSpeichern=false;
        for (int i=0;i<pParamStruktGruppe.length;i++) {
            List<ParamStruktGruppen> lParamStruktGruppe=pParamStruktGruppe[i].paramStruktGruppenelemente;
            if (lParamStruktGruppe!=null && lParamStruktGruppe.size()>0) {
                for (ParamStruktGruppen iParamStruktGruppe : lParamStruktGruppe) {
                    if (iParamStruktGruppe.elementTyp==0) {
                        ParamStrukt pParamStrukt=iParamStruktGruppe.paramStrukt;
                        if (pParamStrukt.parameterTable==5) {
                            speichereEinzelnenParameterEmittent(pParamStrukt, pEmittent);
                            emittentSpeichern=true;
                        }
                    }
                }

            }
        }
        if (emittentSpeichern) {
            int rc=lDbBundle.dbEmittenten.update(pEmittent);
            if (rc<1) {
                return CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert;
            }
        }
        
        return 1;
    }
    
    private void speichereEinzelnenParameterEmittent(ParamStrukt pParamStrukt, EclEmittenten pEmittent) {
        if (pParamStrukt.parameterTable==5) {
            switch (pParamStrukt.parameterOffsetInTable) {
            case 1: 
                pEmittent.portalVorhanden=Integer.parseInt(pParamStrukt.wert);
                break;
            case 2: 
                pEmittent.registerAnbindungVorhanden=Integer.parseInt(pParamStrukt.wert);
                break;
            case 3: 
                pEmittent.appVorhanden=Integer.parseInt(pParamStrukt.wert);
                break;
            case 4: 
                pEmittent.emittentenPortalVorhanden=Integer.parseInt(pParamStrukt.wert);
                break;
            case 5: 
                pEmittent.datenbestandIstProduktiv=Integer.parseInt(pParamStrukt.wert);
                if ((pEmittent.datenbestandIstProduktiv & 65536)==65536) {
                    pEmittent.datenbestandIstProduktiv=131071;
                }
                break;
            }
        }
     }
    
    private int speichereParameterGruppeSonstige(ParamStruktGruppenHeader pParamStruktGruppe[]) {
        boolean parameterSpeichern=false;
        for (int i=0;i<pParamStruktGruppe.length;i++) {
            List<ParamStruktGruppen> lParamStruktGruppe=pParamStruktGruppe[i].paramStruktGruppenelemente;
            if (lParamStruktGruppe!=null && lParamStruktGruppe.size()>0) {
                for (ParamStruktGruppen iParamStruktGruppe : lParamStruktGruppe) {
                    if (iParamStruktGruppe.elementTyp==0) {
                        ParamStrukt pParamStrukt=iParamStruktGruppe.paramStrukt;
                        parameterSpeichern=true;
                        speichereEinzelnenParameter(pParamStrukt);
                    }
                }

            }
        }
        if (parameterSpeichern) {
            BvReload bvReload = new BvReload(lDbBundle);
            bvReload.setReloadParameter(lDbBundle.clGlobalVar.mandant);
        }
        return 1;
    }

    
    
    public int leseParameterListeZumBearbeiten(List<Integer> pParameterIdentListe) {
        return 1;
    }
    
    /**Lese alle Parameter ein und belege diese mit aktuellen Werten sowie Standard-Werten für alten und neuen Veranstaltungstyp vor.
     * pPresetArt=0 bis 9
     * Funktion = 4
     */
    public int leseParameterFuerWechselVersammlungstyp(int pPresetArt, int pVeranstaltungstypNeu) {
        if (verwendeWebService()) {
            WEStubParamStrukt weStubParamStrukt = new WEStubParamStrukt();
            weStubParamStrukt.stubFunktion = 4;
            weStubParamStrukt.presetArt=pPresetArt;
            weStubParamStrukt.veranstaltungstypNeu=pVeranstaltungstypNeu;

            WELoginVerify weLoginVerify = new WELoginVerify();
            weStubParamStrukt.setWeLoginVerify(weLoginVerify);

            WEStubParamStruktRC weStubParamStruktRC = wsClient.stubParamStrukt(weStubParamStrukt);

            if (weStubParamStruktRC.rc < 1) {
                return weStubParamStruktRC.rc;
            }

            paramStruktListe=weStubParamStruktRC.paramStruktListe;
            emittent=weStubParamStruktRC.emittent;
            
            return weStubParamStruktRC.rc;
        }

        
        dbOpen();
        lDbBundle.openParamStrukt();
        CaBug.druckeLog("Anfang", logDrucken, 10);
        leseParameterStruktFuerWechselVersammlungstyp(pPresetArt, pVeranstaltungstypNeu);
        CaBug.druckeLog("Ende", logDrucken, 10);
        
        
        dbClose();
        
        return 1;
    }
    
    /*Funktion 5*/
    public int speichereParameterFuerWechselVersammlungstyp(int pPresetArt, int pVeranstaltungstypNeu, List<ParamStrukt> pParamStruktListe, EclEmittenten pEmittent) {
        
        if (verwendeWebService()) {
            WEStubParamStrukt weStubParamStrukt = new WEStubParamStrukt();
            weStubParamStrukt.stubFunktion = 5;
            weStubParamStrukt.presetArt=pPresetArt;
            weStubParamStrukt.veranstaltungstypNeu=pVeranstaltungstypNeu;
            weStubParamStrukt.paramStruktListe=pParamStruktListe;
            weStubParamStrukt.emittent=pEmittent;
        
            CaBug.druckeLog("Länge pParamStuktListe="+pParamStruktListe.size(), logDrucken, 10);

            WELoginVerify weLoginVerify = new WELoginVerify();
            weStubParamStrukt.setWeLoginVerify(weLoginVerify);

            WEStubParamStruktRC weStubParamStruktRC = wsClient.stubParamStrukt(weStubParamStrukt);

            if (weStubParamStruktRC.rc < 1) {
                return weStubParamStruktRC.rc;
            }

            return weStubParamStruktRC.rc;
        }

        
        dbOpen();
        lDbBundle.openParamStrukt();
        
        boolean emittentSpeichern=false;
        for (ParamStrukt iParamStrukt : pParamStruktListe) {
            CaBug.druckeLog("Pruefe Speichere Parameter "+iParamStrukt.bezeichnungVorEingabefeld, logDrucken, 10);
            if (iParamStrukt.aufNeuenStandardWertUpdaten==true && iParamStrukt.parameterTable==5) {
                CaBug.druckeLog("Update Speichere Parameter "+iParamStrukt.bezeichnungVorEingabefeld, logDrucken, 10);
                speichereEinzelnenParameterEmittent(iParamStrukt, pEmittent);
                emittentSpeichern=true;
            }
        }
        if (emittentSpeichern) {
            int rc=lDbBundle.dbEmittenten.update(pEmittent);
            if (rc<1) {
                dbClose();
                return CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert;
            }
        }

        
        CaBug.druckeLog("Länge pParamStuktListe="+pParamStruktListe.size(), logDrucken, 10);
        for (ParamStrukt iParamStrukt : pParamStruktListe) {
            CaBug.druckeLog("Pruefe Speichere Parameter "+iParamStrukt.bezeichnungVorEingabefeld, logDrucken, 10);
            if (iParamStrukt.aufNeuenStandardWertUpdaten==true) {
                CaBug.druckeLog("Update Speichere Parameter "+iParamStrukt.bezeichnungVorEingabefeld, logDrucken, 10);
                speichereEinzelnenParameter(iParamStrukt);
            }
        }
        
        
        EclParameter lParameter=new EclParameter();
        lParameter.ident=2670+pPresetArt;
        lParameter.beschreibung="veranstaltungstyp";
        lParameter.wert=Integer.toString(pVeranstaltungstypNeu);
        int rc=lDbBundle.dbParameter.update_ohneReload(lParameter);
        if (rc<1) {
            rc=lDbBundle.dbParameter.insert_ohneReload(lParameter);
            CaBug.druckeLog("rc="+rc,logDrucken, 10);
        }

        
        BvReload bvReload = new BvReload(lDbBundle);
        bvReload.setReloadParameter(lDbBundle.clGlobalVar.mandant);

        dbClose();
        
        return 1;
     }
    
    
    /**Funktion = 7**/
    public int speichereNeuenVersammlungstyp(int pPresetArt, String pBeschreibungKurz, String pBeschreibungLang, int pVererbenVon){
        if (verwendeWebService()) {
            WEStubParamStrukt weStubParamStrukt = new WEStubParamStrukt();
            weStubParamStrukt.stubFunktion = 7;
            weStubParamStrukt.presetArt=pPresetArt;
            weStubParamStrukt.beschreibungKurz=pBeschreibungKurz;
            weStubParamStrukt.beschreibungLang=pBeschreibungLang;
            weStubParamStrukt.vererbenVon=pVererbenVon;
        
            WELoginVerify weLoginVerify = new WELoginVerify();
            weStubParamStrukt.setWeLoginVerify(weLoginVerify);

            WEStubParamStruktRC weStubParamStruktRC = wsClient.stubParamStrukt(weStubParamStrukt);

            if (weStubParamStruktRC.rc < 1) {
                return weStubParamStruktRC.rc;
            }

            return weStubParamStruktRC.rc;
        }

        
        dbOpen();
        lDbBundle.openParamStrukt();
        
        
        /*Neue Ident Veranstaltung bestimmen*/
        lDbBundle.dbParamStruktVersammlungstyp.readAll();
        int hoechstIdent=0;
        for (ParamStruktVersammlungstyp iParamStruktVersammlungstyp:lDbBundle.dbParamStruktVersammlungstyp.ergebnis()) {
            if (iParamStruktVersammlungstyp.identVersammlungstyp>hoechstIdent) {
                hoechstIdent=iParamStruktVersammlungstyp.identVersammlungstyp;
            }
        }
        hoechstIdent++;
        
        ParamStruktVersammlungstyp lParamStruktVersammlungstyp=new ParamStruktVersammlungstyp();
        lParamStruktVersammlungstyp.identVersammlungstyp=hoechstIdent;
        lParamStruktVersammlungstyp.typGehoertZuPresetArt=pPresetArt;
        lParamStruktVersammlungstyp.kurzText=pBeschreibungKurz;
        lParamStruktVersammlungstyp.beschreibung=pBeschreibungLang;
        lParamStruktVersammlungstyp.erbtVonIdentVersammlungstyp=pVererbenVon;
        
        lDbBundle.dbParamStruktVersammlungstyp.insert(lParamStruktVersammlungstyp);
      
        /*Bisherige Parameter einlesen, einschließlich Standardwerte*/
        leseParameterStruktFuerWechselVersammlungstyp(pPresetArt, pVererbenVon);
        
        /*Nun Parameter-Standard-Werte speichern*/
        for (ParamStrukt iParamStrukt:paramStruktListe) {
            if (pVererbenVon==-1 || iParamStrukt.wert.equals(iParamStrukt.wertStandardNeu)==false) {
                ParamStruktStandard lParamStruktStandard=new ParamStruktStandard();
                lParamStruktStandard.identParamStrukt=iParamStrukt.ident;
                lParamStruktStandard.giltFuerVersammlungstyp=hoechstIdent;
                lParamStruktStandard.standardwert=iParamStrukt.wert;
                lDbBundle.dbParamStruktStandard.insert(lParamStruktStandard);
            }
        }

        dbClose();
        
        return 1;
    }
    
    
    /**Funktion = 8*/
    public int lesePresetArten() {
        if (verwendeWebService()) {
            WEStubParamStrukt weStubParamStrukt = new WEStubParamStrukt();
            weStubParamStrukt.stubFunktion = 8;
            
            WELoginVerify weLoginVerify = new WELoginVerify();
            weStubParamStrukt.setWeLoginVerify(weLoginVerify);

            WEStubParamStruktRC weStubParamStruktRC = wsClient.stubParamStrukt(weStubParamStrukt);

            if (weStubParamStruktRC.rc < 1) {
                return weStubParamStruktRC.rc;
            }

            paramStruktPresetArtListe=weStubParamStruktRC.paramStruktPresetArtListe;
            
            return weStubParamStruktRC.rc;
        }

        
        dbOpen();
        lDbBundle.openParamStrukt();
        
        lDbBundle.dbParamStruktPresetArt.readAll();
        
        paramStruktPresetArtListe=lDbBundle.dbParamStruktPresetArt.ergebnis();
        
        dbClose();
        
        return 1;
    }
    
    private void speichereEinzelnenParameter(ParamStrukt pParamStrukt) {
        EclParameter lParameter=new EclParameter();
        int rc=0;
        switch (pParamStrukt.parameterTable) {
        case 1:/*tbl_parameter*/
            CaBug.druckeLog("Speichere Parameter Ident="+pParamStrukt.parameterOffsetInTable, logDrucken, 10);
            if (pParamStrukt.parameterLaengeInTable>0) {
                lDbBundle.dbParameter.read(pParamStrukt.parameterOffsetInTable);
                String alterWert="";
                if (lDbBundle.dbParameter.anzErgebnis()>0) {
                    alterWert=lDbBundle.dbParameter.ergebnisPosition(0).wert;
                }
                lParameter.wert=speichereParameterMitOffset(pParamStrukt, alterWert);
            }
            else {
                lParameter.wert=pParamStrukt.wert;
            }
            lParameter.ident=pParamStrukt.parameterOffsetInTable;
            lParameter.beschreibung=pParamStrukt.parameterName;
            if (lParameter.wert.length() > 40) {
                lParameter.wert = lParameter.wert.substring(0, 40);
            }
            rc=lDbBundle.dbParameter.update_ohneReload(lParameter);
            if (rc<1) {
                rc=lDbBundle.dbParameter.insert_ohneReload(lParameter);
                CaBug.druckeLog("rc="+rc,logDrucken, 10);
            }
        case 4:/*tbl_parameterLang*/
           
            if (pParamStrukt.parameterLaengeInTable>0) {
                lDbBundle.dbParameter.readLang(pParamStrukt.parameterOffsetInTable);
                String alterWert="";
                if (lDbBundle.dbParameter.anzErgebnis()>0) {
                    alterWert=lDbBundle.dbParameter.ergebnisPosition(0).wert;
                }
                lParameter.wert=speichereParameterMitOffset(pParamStrukt, alterWert);
            }
            else {
                lParameter.wert=pParamStrukt.wert;
            }
            
            lParameter.ident=pParamStrukt.parameterOffsetInTable;
            lParameter.beschreibung=pParamStrukt.parameterName;
            if (lParameter.wert.length() > 200) {
                lParameter.wert = lParameter.wert.substring(0, 200);
            }
            rc=lDbBundle.dbParameter.updateLang_ohneReload(lParameter);
            if (rc<1) {
                lDbBundle.dbParameter.insertLang_ohneReload(lParameter);
            }

        }
        
    }
    
    private String speichereParameterMitOffset(ParamStrukt pParamStrukt, String pAlterWert) {
        String lErgebnis="";
        if (pParamStrukt.wert.length()<pParamStrukt.parameterLaengeInTable) {
            pParamStrukt.wert=CaString.fuelleRechtsBlank(pParamStrukt.wert, pParamStrukt.parameterLaengeInTable);
        }
        lErgebnis=CaString.stringUeberschreibeTeilMitFehlerbehandlung(
                pAlterWert,
                pParamStrukt.wert,
                pParamStrukt.parameterSubOffsetInTable);
        return lErgebnis;
    }
    
    
    
    /**************************Einlesen der Parameter-Struktur einer Gruppe***************************/
    
    
    
    /***************************Einlesen der Parameter-Struktur einer Liste************************************/
    
    
    
    
    /**Einlesen einer Parameter-Gruppe einschließlich Parameter-Strukturen und Inhalt*/
    private void leseParameterStruktGruppe(int pParamStruktGruppe) {
        
        belegeVererbungsliste(lDbBundle.param.paramBasis.veranstaltungstyp, false);
        
        lDbBundle.dbParamStruktGruppenHeader.readGruppe(pParamStruktGruppe);
        paramStruktGruppeHeader=lDbBundle.dbParamStruktGruppenHeader.ergebnisPosition(0);
          
        
        
        lDbBundle.dbParamStruktGruppen.readGruppe(paramStruktGruppeHeader.identGruppe);
        List<ParamStruktGruppen> lParamStruktGruppe=lDbBundle.dbParamStruktGruppen.ergebnis();
        paramStruktGruppeHeader.paramStruktGruppenelemente=lParamStruktGruppe;

        if (lParamStruktGruppe!=null && lParamStruktGruppe.size()>0) {
            for (ParamStruktGruppen iParamStruktGruppe : lParamStruktGruppe) {
                if (iParamStruktGruppe.elementTyp==0) {
                    leseParameterStruktMitInhalt(iParamStruktGruppe);
                }
            }

        }
    }
    
    
    /**pVeranstaltungstypNeu kann -1 sein, dann erfolgt das Einlesen ohne Standardwerte
     * für pVeranstaltungstypNeu.
     */
    private int leseParameterStruktFuerWechselVersammlungstyp(int pPresetArt, int pVeranstaltungstypNeu) {
        CaBug.druckeLog("Start", logDrucken, 10);

        belegeVererbungsliste(lDbBundle.param.paramBasis.veranstaltungstyp, false);
        
        CaBug.druckeLog("Vor belegeVererbungsliste (neu)", logDrucken, 10);
        if (pVeranstaltungstypNeu!=-1) {
            int[] veranstaltungsliste= new int[10];
            for (int i=0;i<10;i++) {
                if (pPresetArt==i) {
                    veranstaltungsliste[i]=pVeranstaltungstypNeu;
                }
                else {
                    veranstaltungsliste[i]=-1;
                }
            }
            belegeVererbungsliste(veranstaltungsliste, true);
        }

        
        CaBug.druckeLog("Readall", logDrucken, 10);
        int anz=lDbBundle.dbParamStrukt.readAll(pPresetArt);
        paramStruktListe=lDbBundle.dbParamStrukt.ergebnis();
        
        CaBug.druckeLog("Vor Schleife", logDrucken, 10);
        for (int i1=0;i1<anz;i1++) {
            ParamStrukt pParamStrukt=paramStruktListe.get(i1);
            
            fuelleParameterStruktZusaetzlicheBeschreibungenUndAktuellenWert(null, pParamStrukt);
            
            /*Nun noch neuen Standardwert belegen*/
            if (pVeranstaltungstypNeu!=-1) {
                List<ParamStruktStandard> struktStandard= pParamStrukt.struktStandard;
                if (struktStandard!=null && struktStandard.size()>0) {
                    for (ParamStruktStandard iParamStruktStandard: struktStandard){
                        for (int i=0;i<vererbungslisteNeu.get(pPresetArt).size();i++) {
                            if (vererbungslisteNeu.get(pPresetArt).get(i)==iParamStruktStandard.giltFuerVersammlungstyp) {
                                if (pParamStrukt.wertStandardNeuGefuellt==-1 || i<pParamStrukt.wertStandardNeuGefuellt) {
                                    pParamStrukt.wertStandardNeuGefuellt=i;
                                    pParamStrukt.wertStandardNeu=iParamStruktStandard.standardwert;
                                }
                            }
                        }

                    }
                }
            }
            
        }
        return 1;
    }

    /**Einlesen einer Parameter-Struktur mit Parameterinhalt*/
    private void leseParameterStruktMitInhalt(ParamStruktGruppen pParamStruktGruppe) {
        int anz=0;
        int pIdentParamStrukt=pParamStruktGruppe.identParamStrukt;

        ParamStrukt lParamStrukt=null;

        anz=lDbBundle.dbParamStrukt.read(pIdentParamStrukt);
        if (anz!=0) {
            lParamStrukt=lDbBundle.dbParamStrukt.ergebnisPosition(0);
            pParamStruktGruppe.paramStrukt=lParamStrukt;
            
            fuelleParameterStruktZusaetzlicheBeschreibungenUndAktuellenWert(pParamStruktGruppe, lParamStrukt);
        }
        
        
    }
    
    /**pParamStruktGruppe kann null sein!*/
    private void fuelleParameterStruktZusaetzlicheBeschreibungenUndAktuellenWert(ParamStruktGruppen pParamStruktGruppe, ParamStrukt pParamStrukt) {
        int pIdentParamStrukt=pParamStrukt.ident;
        
        switch (pParamStrukt.werteLesenAus) {
        case 0:
            /*zulässige Werte werden aus ParamStruktWerte gelesen. Ergebnis kann
             * leer sein!
             */
            lDbBundle.dbParamStruktWerte.readWerte(pIdentParamStrukt);
            pParamStrukt.struktWerte=lDbBundle.dbParamStruktWerte.ergebnis();
            break;
        case 1:
            /*zulässige Werte werden aus Nummernform-Sets gelesen
             */
            pParamStrukt.struktWerte=new LinkedList<ParamStruktWerte>();
            int anzNummernformSets=lDbBundle.dbNummernFormSet.read_all();
            if (anzNummernformSets>0) {
                for (int i=0;i<anzNummernformSets;i++) {
                    EclNummernFormSet lNummernFormSet=lDbBundle.dbNummernFormSet.ergebnisPosition(i);
                    CaBug.druckeLog("lNummernFormSet.ident="+lNummernFormSet.ident+" lNummernFormSet.name="+lNummernFormSet.name, logDrucken, 10);
                    ParamStruktWerte lParamStruktWert=new ParamStruktWerte();
                    
                    lParamStruktWert.zulaessigeWerteInt=lNummernFormSet.ident;
                    lParamStruktWert.kurztext=lNummernFormSet.name;
                    lParamStruktWert.langtext=lNummernFormSet.beschreibung;
                    
                    pParamStrukt.struktWerte.add(lParamStruktWert);
                }
            }
            break;
        case 2:
            /*zulässige Werte werden aus Abstimmungsblöcken gelesen
             * Zusätzlich auch Wert 0 zulässig ("nicht ausgewählt")
             */
            pParamStrukt.struktWerte=new LinkedList<ParamStruktWerte>();
            int anzStimmbloecke=lDbBundle.dbAbstimmungsblock.read_all();
            if (anzStimmbloecke>0) {
                ParamStruktWerte lParamStruktWert=new ParamStruktWerte();
                lParamStruktWert.zulaessigeWerteInt=0;
                lParamStruktWert.kurztext="(nicht aktiv)";
                lParamStruktWert.langtext="(nicht aktiv)";
                
                pParamStrukt.struktWerte.add(lParamStruktWert);
                for (int i=0;i<anzStimmbloecke;i++) {
                    EclAbstimmungsblock lAbstimmungsblock=lDbBundle.dbAbstimmungsblock.ergebnisPosition(i);
                    
                    lParamStruktWert=new ParamStruktWerte();
                    lParamStruktWert.zulaessigeWerteInt=lAbstimmungsblock.ident;
                    lParamStruktWert.kurztext=lAbstimmungsblock.kurzBeschreibung;
                    lParamStruktWert.langtext=lAbstimmungsblock.beschreibung;
                    
                    pParamStrukt.struktWerte.add(lParamStruktWert);
                }
            }
            break;
            
        }
        
        lDbBundle.dbParamStruktStandard.readStandards(pIdentParamStrukt);
        pParamStrukt.struktStandard=lDbBundle.dbParamStruktStandard.ergebnis();
        
        /*Nun noch aktuellen Parameter-Wert ergänzen*/
        leseParameterWertZuStruktur(pParamStruktGruppe, pParamStrukt);
  
    }
    
    /**Einlesen des aktuellen Parameterwertes zur Struktur*/
    private void leseParameterWertZuStruktur(ParamStruktGruppen pParamStruktGruppe, ParamStrukt pParamStrukt) {
        
        
        String lErgebnis=null;
        switch (pParamStrukt.parameterTable) {
        case 1:/*tbl_paramaeter*/
            lDbBundle.dbParameter.read(pParamStrukt.parameterOffsetInTable);
            if (lDbBundle.dbParameter.anzErgebnis()>0) {
                lErgebnis=leseWertAusString(pParamStrukt, lDbBundle.dbParameter.ergebnisPosition(0).wert);
             }
            break;
        case 4:/*tbl_parameterLang*/
            lDbBundle.dbParameter.readLang(pParamStrukt.parameterOffsetInTable);
            if (lDbBundle.dbParameter.anzErgebnis()>0) {
                CaBug.druckeLog("lDbBundle.dbParameter.ergebnisPosition(0).wert("+pParamStrukt.ident+")="+lDbBundle.dbParameter.ergebnisPosition(0).wert, logDrucken, 10);
                lErgebnis=leseWertAusString(pParamStrukt, lDbBundle.dbParameter.ergebnisPosition(0).wert);
            }
            CaBug.druckeLog("lErgbnis("+pParamStrukt.ident+")="+lErgebnis, logDrucken, 10);
            break;
        case 5:/*tbl_emittenten*/
            holeEmittentInPuffer();
            switch (pParamStrukt.parameterOffsetInTable) {
            case 1: 
                lErgebnis=Integer.toString(emittent.portalVorhanden);
                break;
            case 2: 
                lErgebnis=Integer.toString(emittent.registerAnbindungVorhanden);
                break;
            case 3: 
                lErgebnis=Integer.toString(emittent.appVorhanden);
                break;
            case 4: 
                lErgebnis=Integer.toString(emittent.emittentenPortalVorhanden);
                break;
            case 5: 
                lErgebnis=Integer.toString(emittent.datenbestandIstProduktiv);
                break;
            }
            pParamStrukt.db_versionEmittent=emittent.db_version;
            break;
        
        } 
        
        if (lErgebnis!=null) {
            pParamStrukt.wert=lErgebnis;
            pParamStrukt.wertWurdeAusTableBelegt=true;
            
            pParamStrukt.wertStandardGefuellt=-1;
            
            
            /*Nun noch Standardwert belegen*/
            List<ParamStruktStandard> struktStandard= pParamStrukt.struktStandard;
            if (struktStandard!=null && struktStandard.size()>0) {
                for (ParamStruktStandard iParamStruktStandard: struktStandard){
                    for (int i=0;i<vererbungslisteAktuell.get(pParamStrukt.parameterGehoertZuPresetArt).size();i++) {
                        if (vererbungslisteAktuell.get(pParamStrukt.parameterGehoertZuPresetArt).get(i)==iParamStruktStandard.giltFuerVersammlungstyp) {
                            if (pParamStrukt.wertStandardGefuellt==-1 || i<pParamStrukt.wertStandardGefuellt) {
                                pParamStrukt.wertStandardGefuellt=i;
                                pParamStrukt.wertStandard=iParamStruktStandard.standardwert;
                                if (pParamStruktGruppe!=null) {
                                    if (iParamStruktStandard.anzeigeNurFuerExperten!=-1) {
                                        pParamStruktGruppe.nurImExpertenmodusAnzeigen=iParamStruktStandard.anzeigeNurFuerExperten;
                                    }
                                }
                            }
                        }
                    }
                    
                }
            }
            
        }
        
    }
    
    
    /*+++++++++++++++++++Vererbungsliste füllen++++++++++++++++++++++++*/
    private List<List<Integer>> vererbungslisteAktuell=null;
    private List<List<Integer>> vererbungslisteNeu=null;
    
    private void belegeVererbungsliste(int[] pVeranstaltungstyp, boolean pNeu) {
        
        List<List<Integer>> vererbungsliste=new LinkedList<List<Integer>>();
        for (int i=0;i<10;i++) {
            vererbungsliste.add(new LinkedList<Integer>());
        }
        
        for (int i=0;i<pVeranstaltungstyp.length;i++){
            if (pVeranstaltungstyp[i]!=-1) {
                lDbBundle.dbParamStruktVersammlungstyp.readVersammlungstypIdent(pVeranstaltungstyp[i]);
                ParamStruktVersammlungstyp lParamStruktVersammlungstyp=lDbBundle.dbParamStruktVersammlungstyp.ergebnisPosition(0);
                int lVersammlungsIdent=lParamStruktVersammlungstyp.identVersammlungstyp;
                vererbungsliste.get(i).add(lVersammlungsIdent);
                int lErbtVonVersammlungsIdent=lParamStruktVersammlungstyp.erbtVonIdentVersammlungstyp;
                int rc=1;
                int zaehler=0;
                while (lErbtVonVersammlungsIdent!=-1 && rc==1 && zaehler<100) {
                    zaehler++;
                    rc=lDbBundle.dbParamStruktVersammlungstyp.readVersammlungstypIdent(lErbtVonVersammlungsIdent);
                    lParamStruktVersammlungstyp=lDbBundle.dbParamStruktVersammlungstyp.ergebnisPosition(0);
                    lVersammlungsIdent=lParamStruktVersammlungstyp.identVersammlungstyp;
                    vererbungsliste.get(i).add(lVersammlungsIdent);
                    lErbtVonVersammlungsIdent=lParamStruktVersammlungstyp.erbtVonIdentVersammlungstyp;
                }
            }
        }
        if (pNeu==false) {
            vererbungslisteAktuell=vererbungsliste; 
        }
        else {
            vererbungslisteNeu=vererbungsliste; 
        }
    }
    
    
    private String leseWertAusString(ParamStrukt pParamStrukt, String pWert) {
        String lErgebnis="";
 
        if (pParamStrukt.parameterLaengeInTable>0) {
            lErgebnis=CaString.substringMitFehlerbehandlung(
                    pWert,
                    pParamStrukt.parameterSubOffsetInTable,
                    pParamStrukt.parameterSubOffsetInTable+pParamStrukt.parameterLaengeInTable
                    )
                    ;
            lErgebnis=CaString.entferneBlankRechts(lErgebnis);
        }
        else {
            lErgebnis=pWert;
        }

        
        
        return lErgebnis;
    }
    
    
    /**+++++++++++++++Puffer für emittent++++++++++++++++++++++++++++++++++++++++*/
    
    private void holeEmittentInPuffer() {
        if (emittentGefuellt==true) {
            return;
        }
        lDbBundle.dbEmittenten.readEmittentHV(lDbBundle.clGlobalVar.mandant, lDbBundle.clGlobalVar.hvJahr, lDbBundle.clGlobalVar.hvNummer, lDbBundle.clGlobalVar.datenbereich);
        emittent=lDbBundle.dbEmittenten.ergebnisPosition(0);
        emittentGefuellt=true;
    }
    
    
    
}
