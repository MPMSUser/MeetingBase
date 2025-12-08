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

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclAktienregister;
import de.meetingapps.meetingportal.meetComEntities.EclMeldung;
import de.meetingapps.meetingportal.meetComEntities.EclStimmkarten;
import de.meetingapps.meetingportal.meetComEntities.EclZutrittskarten;
import de.meetingapps.meetingportal.meetComKonst.KonstSuchlaufSuchbegriffArt;
import de.meetingapps.meetingportal.meetComStub.StubRoot;
import de.meetingapps.meetingportal.meetComStub.WEStubBlSuchen;
import de.meetingapps.meetingportal.meetComStub.WEStubBlSuchenRC;
import de.meetingapps.meetingportal.meetComWE.WELoginVerify;

public class BlSuchen extends StubRoot {

    int logDrucken=10;
    
    public BlSuchen(boolean pIstServer, DbBundle pDbBundle) {
        super(pIstServer, pDbBundle);
    }

    public EclAktienregister[] rcAktienregister = null;
    public EclMeldung[] rcMeldungen = null;
    public String[] rcSonstigeVollmacht = null;

    /**Nur gefüllt, wenn Suche nach Stimmkarten*/
    public EclStimmkarten[] rcStimmkarten=null;
    
    /**Nur gefüllt, wenn Suche nach Eintrittskarten*/
    public EclZutrittskarten[] rcZutrittskarten=null;
    
    /*1*/
    /**
     * pSuchlaufbegriffArt siehe KonstSuchlaufSuchbegriffArt
     * 
     * pDurchsuchenSammelkarten, pDurchsuchenInSammelkarten, pDurchsuchenGaeste
     * sind relevant bei vorrangiger Suche im Meldebestand und bei Eintrittskarten/Stimmkarten.
     * 
     * Normale Meldungen (Aktionäre, die nicht in Sammelkarten sind) werden immer im Suchlauf-Ergebnbis geliefert, außer pDurchsuchenNurGaeste==true
     * pDurchsuchenSammelkarten==true => zusätzlich werden die Sammelkarten durchsucht.
     * Analog pDurchsuchenInSammelkarten, pDurchsuchenGaeste - wenn true werden diese zusätzlich gefunden.
     */
    public int sucheAusfuehren(int pSuchlaufbegriffArt, String pSuchbegriff, boolean pDurchsuchenSammelkarten,
            boolean pDurchsuchenInSammelkarten, boolean pDurchsuchenGaeste, boolean pDurchsuchenNurGaeste) {

        if (verwendeWebService()) {
            WEStubBlSuchen weStubBlSuchen = new WEStubBlSuchen();
            weStubBlSuchen.stubFunktion = 1;
            weStubBlSuchen.pSuchlaufbegriffArt = pSuchlaufbegriffArt;
            weStubBlSuchen.pSuchbegriff = pSuchbegriff;
            weStubBlSuchen.pDurchsuchenSammelkarten = pDurchsuchenSammelkarten;
            weStubBlSuchen.pDurchsuchenInSammelkarten = pDurchsuchenInSammelkarten;
            weStubBlSuchen.pDurchsuchenGaeste = pDurchsuchenGaeste;
            weStubBlSuchen.pDurchsuchenNurGaeste = pDurchsuchenNurGaeste;

            WELoginVerify weLoginVerify = new WELoginVerify();
            weStubBlSuchen.setWeLoginVerify(weLoginVerify);

            WEStubBlSuchenRC weStubBlSuchenRC = wsClient.stubBlSuchen(weStubBlSuchen);

            if (weStubBlSuchenRC.rc < 1) {
                return weStubBlSuchenRC.rc;
            }
            rcAktienregister = weStubBlSuchenRC.rcAktienregister;
            rcMeldungen = weStubBlSuchenRC.rcMeldungen;
            rcSonstigeVollmacht = weStubBlSuchenRC.rcSonstigeVollmacht;
            rcStimmkarten =weStubBlSuchenRC.rcStimmkarten;
            rcZutrittskarten =weStubBlSuchenRC.rcZutrittskarten;
                    
            return weStubBlSuchenRC.rc;
        }

        rcAktienregister = null;
        rcMeldungen = null;
        rcSonstigeVollmacht = null;

        dbOpen();
        int anz = 0;
        String[] lSuchbegriff = null;
        
        switch (pSuchlaufbegriffArt) {
        case KonstSuchlaufSuchbegriffArt.registerNummer:
        case KonstSuchlaufSuchbegriffArt.nameAktionaer:
            lSuchbegriff = new String[1];
            lSuchbegriff[0] = suchbegriffUeberarbeitet(pSuchbegriff);

            anz = lDbBundle.dbJoined.read_suchenErstAktienregister(pSuchlaufbegriffArt, lSuchbegriff);
            if (anz != 0) {
                rcAktienregister = lDbBundle.dbJoined.ergebnisAktienregisterEintrag();
                rcMeldungen = lDbBundle.dbJoined.ergebnisMeldung();
                rcSonstigeVollmacht = new String[anz];
                rcStimmkarten=new EclStimmkarten[anz];
                rcZutrittskarten=new EclZutrittskarten[anz];
            }
            break;
        case KonstSuchlaufSuchbegriffArt.ekNummer:
            lSuchbegriff = new String[1];
            lSuchbegriff[0] = pSuchbegriff;
            anz = lDbBundle.dbJoined.read_suchenErstEKNr(pSuchlaufbegriffArt, lSuchbegriff,
                    pDurchsuchenSammelkarten, pDurchsuchenInSammelkarten, pDurchsuchenGaeste);
            if (anz != 0) {
                rcAktienregister = lDbBundle.dbJoined.ergebnisAktienregisterEintrag();
                rcMeldungen = lDbBundle.dbJoined.ergebnisMeldung();
                rcSonstigeVollmacht = new String[anz];
                rcStimmkarten=new EclStimmkarten[anz];
                rcZutrittskarten=lDbBundle.dbJoined.ergebnisZutrittskarten();
           }
            break;
        case KonstSuchlaufSuchbegriffArt.skNummer:
            lSuchbegriff = new String[1];
            lSuchbegriff[0] = pSuchbegriff;
            anz = lDbBundle.dbJoined.read_suchenErstSKNr(pSuchlaufbegriffArt, lSuchbegriff,
                    pDurchsuchenSammelkarten, pDurchsuchenInSammelkarten, pDurchsuchenGaeste);
            if (anz != 0) {
                rcAktienregister = lDbBundle.dbJoined.ergebnisAktienregisterEintrag();
                rcMeldungen = lDbBundle.dbJoined.ergebnisMeldung();
                rcSonstigeVollmacht = new String[anz];
                rcStimmkarten=lDbBundle.dbJoined.ergebnisStimmkarten();
                rcZutrittskarten=new EclZutrittskarten[anz];
           }
            break;
        case KonstSuchlaufSuchbegriffArt.nameAktionaerOderAktuellerVertreter:
        case KonstSuchlaufSuchbegriffArt.meldeIdent:
        case KonstSuchlaufSuchbegriffArt.sammelIdent:
            lSuchbegriff = new String[1];
            if (pSuchlaufbegriffArt==KonstSuchlaufSuchbegriffArt.nameAktionaerOderAktuellerVertreter) {
                lSuchbegriff[0] = suchbegriffUeberarbeitet(pSuchbegriff);
                CaBug.druckeLog("lSuchbegriff[0]="+lSuchbegriff[0], logDrucken, 10);
            }
            else {
                lSuchbegriff[0] = pSuchbegriff;
            }
            anz = lDbBundle.dbJoined.read_suchenErstMeldungen(pSuchlaufbegriffArt, lSuchbegriff,
                    pDurchsuchenSammelkarten, pDurchsuchenInSammelkarten, pDurchsuchenGaeste, pDurchsuchenNurGaeste);
            if (anz != 0) {
                rcAktienregister = lDbBundle.dbJoined.ergebnisAktienregisterEintrag();
                rcMeldungen = lDbBundle.dbJoined.ergebnisMeldung();
                rcSonstigeVollmacht = new String[anz];
                rcStimmkarten=new EclStimmkarten[anz];
                rcZutrittskarten=new EclZutrittskarten[anz];
           }
            break;
        case KonstSuchlaufSuchbegriffArt.nameVertreter:
            lSuchbegriff = new String[1];
            lSuchbegriff[0] = suchbegriffUeberarbeitet(pSuchbegriff);
            anz = lDbBundle.dbJoined.read_suchenErstVertreter(lSuchbegriff, pDurchsuchenSammelkarten,
                    pDurchsuchenInSammelkarten);
            if (anz != 0) {
                rcAktienregister = lDbBundle.dbJoined.ergebnisAktienregisterEintrag();
                rcMeldungen = lDbBundle.dbJoined.ergebnisMeldung();
                rcSonstigeVollmacht = lDbBundle.dbJoined.ergebnisKey();
                rcStimmkarten=new EclStimmkarten[anz];
                rcZutrittskarten=new EclZutrittskarten[anz];
            }
            break;
        case KonstSuchlaufSuchbegriffArt.qrCode:
            /*TODO: Suchen QR-Code implementieren*/
            break;
        case KonstSuchlaufSuchbegriffArt.ungepruefteVertreter:
            lSuchbegriff = new String[1];
            lSuchbegriff[0] = suchbegriffUeberarbeitet(pSuchbegriff);
            anz = lDbBundle.dbJoined.read_suchenErstUngepruefteVertreter(lSuchbegriff, pDurchsuchenSammelkarten,
                    pDurchsuchenInSammelkarten);
            if (anz != 0) {
                rcAktienregister = lDbBundle.dbJoined.ergebnisAktienregisterEintrag();
                rcMeldungen = lDbBundle.dbJoined.ergebnisMeldung();
                rcSonstigeVollmacht = lDbBundle.dbJoined.ergebnisKey();
                rcStimmkarten=new EclStimmkarten[anz];
                rcZutrittskarten=new EclZutrittskarten[anz];
            }
            break;
        }
        CaBug.druckeLog("BlSuchen anz=" + anz, logDrucken, 10);
        dbClose();
        return 1;
    }

    
    private String suchbegriffUeberarbeitet(String pSuchbegriff) {
        String hSuchbegriff=pSuchbegriff.trim();

        hSuchbegriff=hSuchbegriff.replace(" |", "|");
        hSuchbegriff=hSuchbegriff.replace("| ", "|");
        
        return hSuchbegriff;
    }
}
