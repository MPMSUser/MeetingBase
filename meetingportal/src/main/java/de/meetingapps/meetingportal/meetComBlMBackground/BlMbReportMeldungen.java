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
package de.meetingapps.meetingportal.meetComBlMBackground;

import java.util.concurrent.Future;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclFuture3String;
import de.meetingapps.meetingportal.meetComReports.RepMeldeliste;
import de.meetingapps.meetingportal.meetingCoreReport.RpDrucken;
import jakarta.ejb.AsyncResult;
import jakarta.ejb.Asynchronous;
import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionManagement;
import jakarta.ejb.TransactionManagementType;

@Stateless
@TransactionManagement(value=TransactionManagementType.BEAN)
@LocalBean
public class BlMbReportMeldungen {

    private int logDrucken=3;

    @Asynchronous
    public Future<EclFuture3String> erzeugeReport(DbBundle pDbBundle, int pSortierung, int pSelektion, int pKlasse, int pNegativHV, int pAnzahlDrucken,
            String pLfdNummer, int pSelektion_inSammelkarte) {
        CaBug.druckeLog("Job-Anfang", logDrucken, 3);

//        try {
//            Thread.sleep(12000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }


        CaBug.druckeLog("A", logDrucken, 10);

        pDbBundle.openEE();
        CaBug.druckeLog("B", logDrucken, 10);

        RpDrucken rpDrucken = new RpDrucken();
        rpDrucken.initServer();

        RepMeldeliste repMeldeliste = new RepMeldeliste();
        repMeldeliste.mitExport = true;
        repMeldeliste.sortierung = pSortierung;
        repMeldeliste.selektion = pSelektion;
        repMeldeliste.selektion_inSammelkarte = pSelektion_inSammelkarte;
        repMeldeliste.klasse = pKlasse;
        repMeldeliste.anzahlDrucken = pAnzahlDrucken;
        if (pNegativHV == 1) {
            repMeldeliste.selektion_praesenz = 2;
            repMeldeliste.selektion_aktionaereInSammelkartenUnterdruecken = 1;
        }
        if (pNegativHV == 2) {
            repMeldeliste.selektion_praesenz = 1;
            repMeldeliste.selektion_aktionaereInSammelkartenUnterdruecken = 1;
        }
        repMeldeliste.druckeMeldeliste(pDbBundle, rpDrucken, pLfdNummer);


        pDbBundle.closeAll();
        CaBug.druckeLog("Job-Ende", logDrucken, 3);

        /*rpDrucken.drucklaufDatei, "", ""*/

        EclFuture3String ergebnis=new EclFuture3String();
        ergebnis.ergebnis1=rpDrucken.drucklaufDatei;
        ergebnis.ergebnis2=repMeldeliste.exportDatei;
        ergebnis.ergebnis3=repMeldeliste.exportExcelDatei;

        AsyncResult<EclFuture3String> asyncResult3String=new AsyncResult<EclFuture3String>(ergebnis);
        return asyncResult3String;
    }

}
