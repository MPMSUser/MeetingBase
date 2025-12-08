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
import de.meetingapps.meetingportal.meetComBl.BlVeranstaltungen;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclVeranstaltungenReportElement;
import de.meetingapps.meetingportal.meetComReports.RepVeranstaltungsManagement;
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
public class BlMbReportVeranstaltungsManagement {

    private int logDrucken=3;

    @Asynchronous
    public Future<String> erzeugeReport(DbBundle pDbBundle) {
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

        RepVeranstaltungsManagement repVeranstaltungsManagement = new RepVeranstaltungsManagement(true, pDbBundle);
        repVeranstaltungsManagement.init(rpDrucken);
        repVeranstaltungsManagement.auswerten(pDbBundle);


        pDbBundle.closeAll();
        CaBug.druckeLog("Job-Ende", logDrucken, 3);

        AsyncResult<String> asyncResultString=new AsyncResult<String>(rpDrucken.drucklaufDatei);
        return asyncResultString;
    }
    
    @Asynchronous
    public Future<String> erzeugeReportDetail(DbBundle pDbBundle, EclVeranstaltungenReportElement pVeranstaltungenReportElement) {
        CaBug.druckeLog("Job-Anfang", logDrucken, 3);

//        try {
//            Thread.sleep(12000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }


        CaBug.druckeLog("A", logDrucken, 10);

        pDbBundle.openEE();
        CaBug.druckeLog("B", logDrucken, 10);

        BlVeranstaltungen blVeranstaltungen=new BlVeranstaltungen(true, pDbBundle);
        blVeranstaltungen.exportDetailTeilnehmerliste(pVeranstaltungenReportElement);
 
        pDbBundle.closeAll();

        AsyncResult<String> asyncResultString=new AsyncResult<String>(blVeranstaltungen.rcExportDateiname);
        return asyncResultString;
    }

}
