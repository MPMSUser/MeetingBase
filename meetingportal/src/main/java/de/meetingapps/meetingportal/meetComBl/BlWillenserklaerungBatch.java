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
import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclMeldung;
import de.meetingapps.meetingportal.meetComStub.StubRoot;
import de.meetingapps.meetingportal.meetComStub.WEStubBlWillenserklaerungBatch;
import de.meetingapps.meetingportal.meetComStub.WEStubBlWillenserklaerungBatchRC;
import de.meetingapps.meetingportal.meetComWE.WELoginVerify;

/**Stub-Fähig
 * 
 * Batch-Verarbeitungs-Routinen für Willenserklärungen - "Massen-Willenserklärungen".*/
public class BlWillenserklaerungBatch  extends StubRoot {

//    private int logDrucken=10;

    public int rcAnzahlAngemeldeteOhneEK=0;
    
    public BlWillenserklaerungBatch(boolean pIstServer, DbBundle pDbBundle) {
        super(pIstServer, pDbBundle);
    }

    
    /*1*/
    /**Erzeugt für alle angemeldeten Aktionäre (egal ob in Sammelkarte oder nicht) eine Eintrittskarte,
     * soweit noch keine erzeugt wurde.
     * Die EK wird als "bereits ausgestellt" gekennzeichnet (damit sie nicht beim nächsten Drucklauf
     * gedruckt wird).
     *
     * Anzahl der ausgestellten EK wird in rcAnzahlAngemeldeteOhneEK zurückgeliefert
     * @return
     */
    public int erzeugeEKFuerAlleAngemeldeten() {
        if (verwendeWebService()) {
            WEStubBlWillenserklaerungBatch weStubBlWillenserklaerungBatch = new WEStubBlWillenserklaerungBatch();
            weStubBlWillenserklaerungBatch.stubFunktion = 1;

            WELoginVerify weLoginVerify = new WELoginVerify();
            weStubBlWillenserklaerungBatch.setWeLoginVerify(weLoginVerify);

            WEStubBlWillenserklaerungBatchRC wEStubBlWillenserklaerungBatchRC = wsClient.stubBlWillenserklaerungBatch(weStubBlWillenserklaerungBatch);
            rcAnzahlAngemeldeteOhneEK=wEStubBlWillenserklaerungBatchRC.rcAnzahlAngemeldeteOhneEK;
            
            if (wEStubBlWillenserklaerungBatchRC.rc < 1) {
                return wEStubBlWillenserklaerungBatchRC.rc;
            }

            return wEStubBlWillenserklaerungBatchRC.rc;
        }

        dbOpen();
        lDbBundle.dbMeldungen.leseAlleAktionaersmeldungenOhneEK();
        rcAnzahlAngemeldeteOhneEK=lDbBundle.dbMeldungen.anzErgebnis();
        EclMeldung[] meldungsArray=lDbBundle.dbMeldungen.meldungenArray;
        
        for (int i=0;i<rcAnzahlAngemeldeteOhneEK;i++) {
            BlWillenserklaerung ekWillenserklaerung = new BlWillenserklaerung();
            ekWillenserklaerung.pErteiltAufWeg = 51;

            ekWillenserklaerung.piMeldungsIdentAktionaer = meldungsArray[i].meldungsIdent;
            ekWillenserklaerung.pWillenserklaerungGeberIdent = -1; /* Aktionär */

            /* Versandart */
            ekWillenserklaerung.pVersandartEK = 0;

            ekWillenserklaerung.neueZutrittsIdentZuMeldung(lDbBundle);

            boolean brcEK = ekWillenserklaerung.rcIstZulaessig;
            if (brcEK==false) {
                CaBug.drucke("002 "+ekWillenserklaerung.rcGrundFuerUnzulaessig+" / "+CaFehler.getFehlertext(ekWillenserklaerung.rcGrundFuerUnzulaessig, 0));
            }
        }

        dbClose();
        return 1;
       
    }
    
    /*2*/
    /** Liefert rcAnzahlAngemeldeteOhneEK*/
    public int liefereAnzahlAngemeldeteOhneEK() {
        if (verwendeWebService()) {
            WEStubBlWillenserklaerungBatch weStubBlWillenserklaerungBatch = new WEStubBlWillenserklaerungBatch();
            weStubBlWillenserklaerungBatch.stubFunktion = 2;

            WELoginVerify weLoginVerify = new WELoginVerify();
            weStubBlWillenserklaerungBatch.setWeLoginVerify(weLoginVerify);

            WEStubBlWillenserklaerungBatchRC wEStubBlWillenserklaerungBatchRC = wsClient.stubBlWillenserklaerungBatch(weStubBlWillenserklaerungBatch);
            rcAnzahlAngemeldeteOhneEK=wEStubBlWillenserklaerungBatchRC.rcAnzahlAngemeldeteOhneEK;

            if (wEStubBlWillenserklaerungBatchRC.rc < 1) {
                return wEStubBlWillenserklaerungBatchRC.rc;
            }

            return wEStubBlWillenserklaerungBatchRC.rc;
        }

        dbOpen();
        lDbBundle.dbMeldungen.leseAlleAktionaersmeldungenOhneEK();
        rcAnzahlAngemeldeteOhneEK=lDbBundle.dbMeldungen.anzErgebnis();

        dbClose();
        return 1;
    }
}
