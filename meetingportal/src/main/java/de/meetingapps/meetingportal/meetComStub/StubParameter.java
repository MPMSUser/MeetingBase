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

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclEmittenten;
import de.meetingapps.meetingportal.meetComHVParam.HVParam;
import de.meetingapps.meetingportal.meetComWE.WELoginVerify;

/**Wichtig: die Mandanten-Wechselfunktionen sind "nicht nachhaltig", d.h.
 * sie werden beim nächsten Open / New ggf. wieder überschrieben!
 * 
 * Deshalb dort dann nur openAllOhneParameterCheck verwenden!
 */
public class StubParameter extends StubRoot {

    public HVParam rcHVParam = null;
    public EclEmittenten rcEmittent=null;
    public int rcMandant;
    public int rcHvJahr;
    public String rcHvNummer;
    public String rcDatenbereich;

    public StubParameter(boolean pIstServer, DbBundle pDbBundle) {
        super(pIstServer, pDbBundle);
    }

    /*1*/
    public int updateHVParam_all(HVParam pHVParam) {
        if (verwendeWebService()) {
            WEStubParameter weStubParameter = new WEStubParameter();
            weStubParameter.stubFunktion = 1;

            WELoginVerify weLoginVerify = new WELoginVerify();
            weStubParameter.setWeLoginVerify(weLoginVerify);

            weStubParameter.pHVParam = pHVParam;
            WEStubParameterRC weStubParameterRC = wsClient.stubParameter(weStubParameter);

            if (weStubParameterRC.rc < 1) {
                return weStubParameterRC.rc;
            }

            return weStubParameterRC.rc;
        }

        dbOpenOhneParameterCheck();
        lDbBundle.dbParameter.ergHVParam = pHVParam;
        int rc=lDbBundle.dbParameter.updateHVParam_all();
        dbClose();
        if (rc==CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert) {
            return CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert;
        }
        return 1;
    }

    /*2*/
    /**Belegt rcHVParam*/
    public int leseHVParam_all(int pMandant, int pHvJahr, String pHvNummer, String pDatenbereich) {
        if (verwendeWebService()) {
            WEStubParameter weStubParameter = new WEStubParameter();
            weStubParameter.stubFunktion = 2;

            WELoginVerify weLoginVerify = new WELoginVerify();
            weStubParameter.setWeLoginVerify(weLoginVerify);

            weStubParameter.pMandant = pMandant;
            weStubParameter.pHvJahr = pHvJahr;
            weStubParameter.pHvNummer = pHvNummer;
            weStubParameter.pDatenbereich = pDatenbereich;

            WEStubParameterRC weStubParameterRC = wsClient.stubParameter(weStubParameter);

            if (weStubParameterRC.rc < 1) {
                return weStubParameterRC.rc;
            }

            rcHVParam = weStubParameterRC.rcHVParam;
            return weStubParameterRC.rc;
        }

        int zMandant, zHVJahr;
        String zHVNummer, zDatenbereich;
        zMandant = lDbBundle.clGlobalVar.mandant;
        zHVJahr = lDbBundle.clGlobalVar.hvJahr;
        zHVNummer = lDbBundle.clGlobalVar.hvNummer;
        zDatenbereich = lDbBundle.clGlobalVar.datenbereich;

        dbOpen();
        lDbBundle.dbParameter.readHVParam_all();
        rcHVParam = lDbBundle.dbParameter.ergHVParam;

        lDbBundle.clGlobalVar.mandant = zMandant;
        lDbBundle.clGlobalVar.hvJahr = zHVJahr;
        lDbBundle.clGlobalVar.hvNummer = zHVNummer;
        lDbBundle.clGlobalVar.datenbereich = zDatenbereich;

        /*Nun Menü-Struktur und und Kontaktformular-Themen ergänzen*/
        if (rcHVParam.paramPortal.registerAnbindungOberflaeche!=0) {
            rcHVParam.paramPortal.menueListe=lDbBundle.dbMenueEintrag.readMenue();
            rcHVParam.paramPortal.kontaktformularThemenListe=lDbBundle.dbKontaktformularThema.readThemenliste();
        }

        
        dbClose();
        
        return 1;
    }

    /**Zum Zwischenspeichern des aktuellen Mandanten in ClGlobalVar.
     * Kein Datenzugriff*/
    private int zMandant, zHVJahr;
    private String zHVNummer, zDatenbereich;

    public void speichereMandantAktuell() {
        zMandant = lDbBundle.clGlobalVar.mandant;
        zHVJahr = lDbBundle.clGlobalVar.hvJahr;
        zHVNummer = lDbBundle.clGlobalVar.hvNummer;
        zDatenbereich = lDbBundle.clGlobalVar.datenbereich;
    }

    /**Datenzugriff nur intern*/
    public void restoreMandantAktuell() {
        lDbBundle.clGlobalVar.mandant = zMandant;
        lDbBundle.clGlobalVar.hvJahr = zHVJahr;
        lDbBundle.clGlobalVar.hvNummer = zHVNummer;
        lDbBundle.clGlobalVar.datenbereich = zDatenbereich;
        dbOpen();
        leseHVParam_all(zMandant, zHVJahr, zHVNummer, zDatenbereich);
        lDbBundle.param = rcHVParam;
        dbClose();
    }

    /*3*/
    /**Belegt
     * lDbBundle.param
     * rcHVParam
     */
    public int belegeStandardMandant(int pMandant) {
        if (verwendeWebService()) {
            WEStubParameter weStubParameter = new WEStubParameter();
            weStubParameter.stubFunktion = 3;

            WELoginVerify weLoginVerify = new WELoginVerify();
            weStubParameter.setWeLoginVerify(weLoginVerify);

            weStubParameter.pMandant = pMandant;

            WEStubParameterRC weStubParameterRC = wsClient.stubParameter(weStubParameter);

            if (weStubParameterRC.rc < 1) {
                return weStubParameterRC.rc;
            }

            rcHVParam = weStubParameterRC.rcHVParam;
            lDbBundle.clGlobalVar.mandant = weStubParameterRC.rcMandant;
            lDbBundle.clGlobalVar.hvJahr = weStubParameterRC.rcHvJahr;
            lDbBundle.clGlobalVar.hvNummer = weStubParameterRC.rcHvNummer;
            lDbBundle.clGlobalVar.datenbereich = weStubParameterRC.rcDatenbereich;

            rcMandant = weStubParameterRC.rcMandant;
            rcHvJahr = weStubParameterRC.rcHvJahr;
            rcHvNummer = weStubParameterRC.rcHvNummer;
            rcDatenbereich = weStubParameterRC.rcDatenbereich;

            lDbBundle.param = rcHVParam;
            return weStubParameterRC.rc;
        }

        dbOpen();
        lDbBundle.dbEmittenten.readStandardHV(pMandant);
        if (lDbBundle.dbEmittenten.anzErgebnis()!=0) {
            EclEmittenten lEmittent = lDbBundle.dbEmittenten.ergebnisPosition(0);

            lDbBundle.clGlobalVar.mandant = lEmittent.mandant;
            lDbBundle.clGlobalVar.hvJahr = lEmittent.hvJahr;
            lDbBundle.clGlobalVar.hvNummer = lEmittent.hvNummer;
            lDbBundle.clGlobalVar.datenbereich = lEmittent.dbArt;

            rcMandant = lEmittent.mandant;
            rcHvJahr = lEmittent.hvJahr;
            rcHvNummer = lEmittent.hvNummer;
            rcDatenbereich = lEmittent.dbArt;

            leseHVParam_all(lEmittent.mandant, lEmittent.hvJahr, lEmittent.hvNummer, lEmittent.dbArt);
            lDbBundle.param = rcHVParam;
            lDbBundle.eclEmittent = lEmittent;

            dbClose();

            return 1;
        }
        else {
            dbClose();
            CaBug.drucke("Mandant "+pMandant+" nicht gefunden");
            return -1;
        }
    }
    
    /*4
     * Belegt rcEmittent*/
    public int leseEmittent(int pMandant, int pHvJahr, String pHvNummer, String pDatenbereich) {
        if (verwendeWebService()) {
            WEStubParameter weStubParameter = new WEStubParameter();
            weStubParameter.stubFunktion = 4;

            WELoginVerify weLoginVerify = new WELoginVerify();
            weStubParameter.setWeLoginVerify(weLoginVerify);

            weStubParameter.pMandant = pMandant;
            weStubParameter.pHvJahr = pHvJahr;
            weStubParameter.pHvNummer = pHvNummer;
            weStubParameter.pDatenbereich = pDatenbereich;

            WEStubParameterRC weStubParameterRC = wsClient.stubParameter(weStubParameter);

            if (weStubParameterRC.rc < 1) {
                return weStubParameterRC.rc;
            }

            rcEmittent = weStubParameterRC.rcEmittent;
            return weStubParameterRC.rc;
        }
        
        dbOpen();

        lDbBundle.dbEmittenten.readEmittentHV(pMandant, pHvJahr, pHvNummer, pDatenbereich);
        rcEmittent = lDbBundle.dbEmittenten.ergebnisPosition(0);
       
        dbClose();

        return 1;
    }
    
    /*5
     * Speicher Emittent*/
    public int updateEmittent(EclEmittenten pEmittent) {
        if (verwendeWebService()) {
            WEStubParameter weStubParameter = new WEStubParameter();
            weStubParameter.stubFunktion = 5;

            WELoginVerify weLoginVerify = new WELoginVerify();
            weStubParameter.setWeLoginVerify(weLoginVerify);

            weStubParameter.pEmittent = pEmittent;
            WEStubParameterRC weStubParameterRC = wsClient.stubParameter(weStubParameter);

            if (weStubParameterRC.rc < 1) {
                return weStubParameterRC.rc;
            }

            return weStubParameterRC.rc;
        }

        dbOpenOhneParameterCheck();
        int rc = lDbBundle.dbEmittenten.update(pEmittent);
        dbClose();
        if (rc != 1) {
            return CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert;
        }

        return 1;
    }

}
