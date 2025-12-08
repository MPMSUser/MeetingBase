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
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclAbstimmungMeldung;
import de.meetingapps.meetingportal.meetComEntities.EclAbstimmungMeldungSperre;
import de.meetingapps.meetingportal.meetComEntities.EclMeldung;
import de.meetingapps.meetingportal.meetComKonst.KonstWillenserklaerungWeg;
import de.meetingapps.meetingportal.meetComStub.StubRoot;
import de.meetingapps.meetingportal.meetComStub.WEStubBlHybridMitglieder;
import de.meetingapps.meetingportal.meetComStub.WEStubBlHybridMitgliederRC;
import de.meetingapps.meetingportal.meetComStub.WSClientOnline;
import de.meetingapps.meetingportal.meetComWE.WEHybridMitglieder;
import de.meetingapps.meetingportal.meetComWE.WEHybridMitgliederRC;
import de.meetingapps.meetingportal.meetComWE.WELoginVerify;

/**Funktionen für eine Hybrid-Veranstaltung für Mitglieder, mit freiwilliger Anmeldung.
 * 
 * Stub-Fähig
 */
public class BlHybridMitglieder extends StubRoot {

     
    private int logDrucken=10;

    
    /**Alle Meldungen, die auf dem Online Server präsent sind*/
    public List<EclMeldung> rcPraesenteMeldungenAufServer=null;

    /**Alle Meldungen, die auf dem HV-Server präsent sind*/
    public List<EclAbstimmungMeldungSperre> rcPraesenteMeldungenAufHVServer=null;

    public List<EclAbstimmungMeldung> rcAbstimmungMeldungAufServer=null;
    
    public BlHybridMitglieder(boolean pIstServer, DbBundle pDbBundle) {
        super(pIstServer, pDbBundle);
    }
    
    /**
     * Holt alle Präsenten / Präsentgewesenen von Online-Server und setzt
     * das Online-Anwesenheitskennzeichen (statusPraesenz) in zusatzfeld 4
     * 
     * Belegt rcPraesenteMeldungen*/
    public int holePraesenteVonServer() {
        WEHybridMitglieder weHybridMitglieder=new WEHybridMitglieder();
        weHybridMitglieder.funktion=1;
        
        WELoginVerify weLoginVerifyOnline = new WELoginVerify(); /*Mandatennummer wird in WSClient immer gesetzt*/
        weHybridMitglieder.setWeLoginVerify(weLoginVerifyOnline);
        
        WSClientOnline wsClientOnline = new WSClientOnline();

        WEHybridMitgliederRC weHybridMitgliederRC=wsClientOnline.hybridMitglieder(weHybridMitglieder);
        if (weHybridMitgliederRC.getRc() < 1) {
            CaBug.drucke("001");
            return -1;
        }
        
        rcPraesenteMeldungenAufServer=weHybridMitgliederRC.rcPraesenteMeldungenAufServer;
        CaBug.druckeLog("Länge rcPraesenteMeldungenAufServer="+rcPraesenteMeldungenAufServer.size(), logDrucken, 10);
        
        return updatePraesenteVonServer();
    }
    
    /**Stub 1*/
    public int updatePraesenteVonServer() {
        CaBug.druckeLog("Start", logDrucken, 10);
        /**Nun noch Kennzeichen auf HV-Server setzen*/
        if (verwendeWebService()) {
            WEStubBlHybridMitglieder weStubBlHybridMitglieder = new WEStubBlHybridMitglieder();
            weStubBlHybridMitglieder.stubFunktion = 1;
            weStubBlHybridMitglieder.rcPraesenteMeldungenAufServer=rcPraesenteMeldungenAufServer;
            
            WELoginVerify weLoginVerify = new WELoginVerify();
            weStubBlHybridMitglieder.setWeLoginVerify(weLoginVerify);

            WEStubBlHybridMitgliederRC weStubBlHybridMitgliederRC = wsClient.stubBlHybridMitglieder(weStubBlHybridMitglieder);
            
            if (weStubBlHybridMitgliederRC.rc < 1) {
                return weStubBlHybridMitgliederRC.rc;
            }

            return weStubBlHybridMitgliederRC.rc;
        }

        dbOpen();
        CaBug.druckeLog("Nach Open Länge rcPraesenteMeldungenAufServer="+rcPraesenteMeldungenAufServer.size(), logDrucken, 10);
        lDbBundle.dbMeldungen.updateOnlinePraesenz(rcPraesenteMeldungenAufServer);
        dbClose();
        
        return 1;
    }
    
    
    public int setztePraesenteFuerAbstimmungGesperrtInPortal() {
        /*Holen Präsente Meldungen von HV-Server*/
        holePraesenteVonHVServer();
        
        /*Übertrage Präsente auf Online-Server und sperre sie dort für die Abstimmung*/
        WEHybridMitglieder weHybridMitglieder=new WEHybridMitglieder();
        weHybridMitglieder.funktion=2;
        
        WELoginVerify weLoginVerifyOnline = new WELoginVerify(); /*Mandatennummer wird in WSClient immer gesetzt*/
        weHybridMitglieder.setWeLoginVerify(weLoginVerifyOnline);
        
        weHybridMitglieder.rcPraesenteMeldungenAufHVServer=rcPraesenteMeldungenAufHVServer;
        
        WSClientOnline wsClientOnline = new WSClientOnline();

        WEHybridMitgliederRC weHybridMitgliederRC=wsClientOnline.hybridMitglieder(weHybridMitglieder);
        if (weHybridMitgliederRC.getRc() < 1) {
            CaBug.drucke("001");
            return -1;
        }

        return 1;
    }
    
    
    /**Stub 2
     * Belegt rcPraesenteMeldeIdentsAufHVServer
     * */
    public int holePraesenteVonHVServer() {
        CaBug.druckeLog("Start", logDrucken, 10);
        /**Nun noch Kennzeichen auf HV-Server setzen*/
        if (verwendeWebService()) {
            WEStubBlHybridMitglieder weStubBlHybridMitglieder = new WEStubBlHybridMitglieder();
            weStubBlHybridMitglieder.stubFunktion = 2;
            
            WELoginVerify weLoginVerify = new WELoginVerify();
            weStubBlHybridMitglieder.setWeLoginVerify(weLoginVerify);

            WEStubBlHybridMitgliederRC weStubBlHybridMitgliederRC = wsClient.stubBlHybridMitglieder(weStubBlHybridMitglieder);
            
            if (weStubBlHybridMitgliederRC.rc < 1) {
                return weStubBlHybridMitgliederRC.rc;
            }

            rcPraesenteMeldungenAufHVServer=weStubBlHybridMitgliederRC.rcPraesenteMeldungenAufHVServer;
            return weStubBlHybridMitgliederRC.rc;
        }

        dbOpen();
        lDbBundle.dbMeldungen.leseAlleMeldungenAnwesend(-1, false);
        rcPraesenteMeldungenAufHVServer=new LinkedList<EclAbstimmungMeldungSperre>();
        if (lDbBundle.dbMeldungen.meldungenArray!=null) {
            for (int i=0;i<lDbBundle.dbMeldungen.meldungenArray.length;i++){
                EclAbstimmungMeldungSperre lAbstimmungMeldungSperre=new EclAbstimmungMeldungSperre();
                lAbstimmungMeldungSperre.meldungIdentGesperrt=lDbBundle.dbMeldungen.meldungenArray[i].meldungsIdent;
                rcPraesenteMeldungenAufHVServer.add(lAbstimmungMeldungSperre);
            }
        }
        dbClose();
        
        return 1;
    }

    
    /**Hinweis: kein Stub, da im Zusammenhang mit Abstimmung, und dieser nur mit DB-Zugriff*/
    public int uebertragePortalAbstimmungInLokaleAbstimmung() {
        /**Schritt 1: Abstimmungsdaten von Portal-Server holen*/
        WEHybridMitglieder weHybridMitglieder=new WEHybridMitglieder();
        weHybridMitglieder.funktion=3;
        
        WELoginVerify weLoginVerifyOnline = new WELoginVerify(); /*Mandatennummer wird in WSClient immer gesetzt*/
        weHybridMitglieder.setWeLoginVerify(weLoginVerifyOnline);
        
        weHybridMitglieder.rcPraesenteMeldungenAufHVServer=rcPraesenteMeldungenAufHVServer;
        
        WSClientOnline wsClientOnline = new WSClientOnline();

        WEHybridMitgliederRC weHybridMitgliederRC=wsClientOnline.hybridMitglieder(weHybridMitglieder);
        if (weHybridMitgliederRC.getRc() < 1) {
            CaBug.drucke("001");
            return -1;
        }
        
        rcAbstimmungMeldungAufServer=weHybridMitgliederRC.rcAbstimmungMeldungAufServer;
        
        /*Schritt 2: Abstimmungen der aktuellen Abstimmrunde eintragen*/
        dbOpen();
        BlAbstimmung blAbstimmung = new BlAbstimmung(lDbBundle);
        blAbstimmung.leseAktivenAbstimmungsblock();
        if (rcAbstimmungMeldungAufServer != null) {
            for (int i = 0; i < rcAbstimmungMeldungAufServer.size(); i++) {
                EclAbstimmungMeldung eclAbstimmungMeldungAktionaer = rcAbstimmungMeldungAufServer.get(i);
                blAbstimmung.starteSpeichernFuerMeldungsIdent(eclAbstimmungMeldungAktionaer.meldungsIdent);
                for (int i1 = 0; i1 < blAbstimmung.abstimmungenZuAktivenBlock.length; i1++) {
                    if (!blAbstimmung.istUeberschrift(i1)) {
                        int aktuelleStimmabgabe = blAbstimmung.liefereAktuelleMarkierungZuAbstimmungsPosition(i1, eclAbstimmungMeldungAktionaer);
                        blAbstimmung.setzeMarkierungZuAbstimmungsPosition(aktuelleStimmabgabe, i1, KonstWillenserklaerungWeg.abstPortal);
                    }
                }
                blAbstimmung.beendeSpeichernFuerMeldung();
            }
        }
        
        dbClose();
        return 1;
    }
    
    /*************************************************************************************************************************************/
    /***********************************************************Businesslogik zu WAIntern.hybridMitglieder********************************/
    /***********************************************************Businesslogik zu WAIntern.hybridMitglieder********************************/
    /*Deshalb nicht Stubfähig, da nur auf Server verwendet nach Aufruf durch Web-Service!*/
    
    public WEHybridMitgliederRC weHybridMitgliederRC=null;
    
    public int serverAusfuehrenFunktion( WEHybridMitglieder weHybridMitglieder) {
        switch (weHybridMitglieder.funktion) {
        case 1:return serverHolePraesenteVonServer();
        case 2:return serverSetzePraesenteFuerAbstimmungGesperrtInportal(weHybridMitglieder);
        case 3:return serverHoleAbstimmungenVonServer();
       
        }
        return 1;
    }
    
    private int serverHolePraesenteVonServer() {
        CaBug.druckeLog("", logDrucken, 1);
        dbOpen();
        lDbBundle.dbMeldungen.leseAlleMeldungenIstOderWarAnwesend(-1, false);
        rcPraesenteMeldungenAufServer=new LinkedList<EclMeldung>();
        if (lDbBundle.dbMeldungen.meldungenArray!=null) {
            int anzMeldungen=lDbBundle.dbMeldungen.meldungenArray.length;
            for (int i=0;i<anzMeldungen;i++) {
                rcPraesenteMeldungenAufServer.add(lDbBundle.dbMeldungen.meldungenArray[i]);
            }
        }
        weHybridMitgliederRC.rcPraesenteMeldungenAufServer=rcPraesenteMeldungenAufServer;
        dbClose();
        return 1;
    }
    
    private int serverSetzePraesenteFuerAbstimmungGesperrtInportal(WEHybridMitglieder weHybridMitglieder) {
        CaBug.druckeLog("", logDrucken, 1);
        dbOpen();
        lDbBundle.dbAbstimmungMeldungSperre.deleteAll();
        rcPraesenteMeldungenAufHVServer=weHybridMitglieder.rcPraesenteMeldungenAufHVServer;
        if (rcPraesenteMeldungenAufHVServer!=null) {
            for (int i=0;i<rcPraesenteMeldungenAufHVServer.size();i++) {
                lDbBundle.dbAbstimmungMeldungSperre.insert(rcPraesenteMeldungenAufHVServer.get(i));
            }
        }
        dbClose();
        return 1;
    }
    
    private int serverHoleAbstimmungenVonServer() {
        CaBug.druckeLog("", logDrucken, 1);
        dbOpen();
        rcAbstimmungMeldungAufServer=new LinkedList<EclAbstimmungMeldung>();
        int anzahlAbstimmungMeldung = lDbBundle.dbAbstimmungMeldung.readinit_all(true);
        if (anzahlAbstimmungMeldung > 0) {
            while (lDbBundle.dbAbstimmungMeldung.readnext_all()) {
                EclAbstimmungMeldung eclAbstimmungMeldungAktionaer = lDbBundle.dbAbstimmungMeldung.ergebnisGefunden(0);
                if (eclAbstimmungMeldungAktionaer.aktiv == 1) {
                    rcAbstimmungMeldungAufServer.add(eclAbstimmungMeldungAktionaer);
                }
            }
        }
       
        weHybridMitgliederRC.rcAbstimmungMeldungAufServer=rcAbstimmungMeldungAufServer;
        dbClose();
        return 1;
    }

}
