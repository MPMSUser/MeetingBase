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
package de.meetingapps.meetingportal.meetingport;

import de.meetingapps.meetingportal.meetComEclM.EclDbM;
import de.meetingapps.meetingportal.meetComEntities.EclMeldung;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@RequestScoped
@Named
public class TController {

    @Inject
    DDlgVariablen dDlgVariablen;
    @Inject
    EclDbM eclDbM;

    /*****************************************Ansprung aus JSF heraus*******************************/
    public String doTest1() {

        int i = 1;

        i = i + 10;
        System.out.println("Anfang " + i);
        i = i + 20;

        System.out.println("Vor Open");
        eclDbM.openAll();
        System.out.println("nach open");
        /*Test1*/
        //		int rc=eclDbM.getDbBundle().dbBasis.getInterneIdentWillenserklaerungForUpdate();
        //		System.out.println("rc="+rc);

        /*Test2*/
        //		int rc=eclDbM.getDbBundle().dbBasis.getInterneIdentMeldungen();
        //		System.out.println("rc="+rc);

        EclMeldung lMeldung = new EclMeldung();
        lMeldung.meldungsIdent = 1;
        eclDbM.getDbBundle().dbMeldungen.leseZuMeldungsIdent(lMeldung);
        lMeldung = eclDbM.getDbBundle().dbMeldungen.meldungenArray[0];
        System.out.println("version=" + lMeldung.db_version);
        //		lMeldung.aktionaersnummer=Long.toString(lMeldung.db_version);
        //		int rc=eclDbM.getDbBundle().dbMeldungen.update(lMeldung);
        //		System.out.println("version="+lMeldung.db_version+" rc="+rc);

        try {
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        i = i + 30;
        lMeldung = new EclMeldung();
        lMeldung.meldungsIdent = 2;
        eclDbM.getDbBundle().dbMeldungen.leseZuMeldungsIdent(lMeldung);
        lMeldung = eclDbM.getDbBundle().dbMeldungen.meldungenArray[0];
        System.out.println("version=" + lMeldung.db_version);
        System.out.println(" Vor Close");
        eclDbM.closeAll();
        System.out.println("Ende" + i);

        return "";
    }

}
