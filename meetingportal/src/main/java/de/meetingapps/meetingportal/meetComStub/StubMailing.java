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

import java.util.ArrayList;
import java.util.List;

import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclMailing;
import de.meetingapps.meetingportal.meetComEntities.EclMailingStatus;
import de.meetingapps.meetingportal.meetComWE.WELoginVerify;

public class StubMailing  extends StubRoot {

    public StubMailing(boolean pIstServer, DbBundle pDbBundle) {
        super(pIstServer, pDbBundle);
     }

    
    /*1*/
    public int speichereMailingStatusListe(ArrayList<EclMailingStatus> pStati ) {
        if (verwendeWebService()) {
            WEStubMailing weStubMailing = new WEStubMailing();
            weStubMailing.stubFunktion = 1;
            weStubMailing.stati=pStati;

            WELoginVerify weLoginVerify = new WELoginVerify();
            weStubMailing.setWeLoginVerify(weLoginVerify);

            WEStubMailingRC weStubMailingRC = wsClient.stubMailing(weStubMailing);

            if (weStubMailingRC.rc < 1) {
                return weStubMailingRC.rc;
            }

             return weStubMailingRC.rc;
        }

        dbOpen();
        dbOpenWeitere();

        for (int i=0;i<pStati.size();i++) {
           lDbBundle.dbMailingStatus.insert(pStati.get(i));
        }


        dbClose();

        return 1;
    }
    
    
    public List<EclMailing> rcEclMailingList=null;
    /*2
     * belegt rcEclMailingList*/
    public int holeEclMailingList() {
        if (verwendeWebService()) {
            WEStubMailing weStubMailing = new WEStubMailing();
            weStubMailing.stubFunktion = 2;

            WELoginVerify weLoginVerify = new WELoginVerify();
            weStubMailing.setWeLoginVerify(weLoginVerify);

            WEStubMailingRC weStubMailingRC = wsClient.stubMailing(weStubMailing);

            if (weStubMailingRC.rc < 1) {
                return weStubMailingRC.rc;
            }

            rcEclMailingList=weStubMailingRC.rcEclMailingList;
            
            return weStubMailingRC.rc;
        }

        dbOpen();
        dbOpenWeitere();

        lDbBundle.dbMailing.readAll();
        rcEclMailingList=lDbBundle.dbMailing.ergebnis();
        
        dbClose();

        return 1;
    }

    /*3*/
    public int speichereEclMailingList(List<EclMailing> pEclMailingList) {
        if (verwendeWebService()) {
            WEStubMailing weStubMailing = new WEStubMailing();
            weStubMailing.stubFunktion = 3;

            WELoginVerify weLoginVerify = new WELoginVerify();
            weStubMailing.setWeLoginVerify(weLoginVerify);
            weStubMailing.eclMailingList=pEclMailingList;

            WEStubMailingRC weStubMailingRC = wsClient.stubMailing(weStubMailing);

            if (weStubMailingRC.rc < 1) {
                return weStubMailingRC.rc;
            }

            return weStubMailingRC.rc;
        }

        dbOpen();
        dbOpenWeitere();

        lDbBundle.dbMailing.deleteAll();
        for (int i=0;i<pEclMailingList.size();i++) {
            lDbBundle.dbMailing.insert(pEclMailingList.get(i));
        }
        
        dbClose();

        return 1;
    }

    
}
