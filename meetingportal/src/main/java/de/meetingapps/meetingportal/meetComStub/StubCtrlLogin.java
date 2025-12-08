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

import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclEmittenten;
import de.meetingapps.meetingportal.meetComWE.WELoginVerify;

public class StubCtrlLogin extends StubRoot {

    public StubCtrlLogin(boolean pIstServer, DbBundle pDbBundle) {
        super(pIstServer, pDbBundle);
    }

    public EclEmittenten[] rcMandantenArray = null;

    /*1*/
    public int liefereMandantenArray() {

        if (verwendeWebService()) {
            WEStubCtrlLogin weStubCtrlLogin = new WEStubCtrlLogin();
            weStubCtrlLogin.stubFunktion = 1;

            WELoginVerify weLoginVerify = new WELoginVerify();
            weStubCtrlLogin.setWeLoginVerify(weLoginVerify);

            WEStubCtrlLoginRC weStubCtrlLoginRC = wsClient.stubCtrlLogin(weStubCtrlLogin);

            if (weStubCtrlLoginRC.rc < 1) {
                return weStubCtrlLoginRC.rc;
            }

            rcMandantenArray = weStubCtrlLoginRC.rcMandantenArray;

            return weStubCtrlLoginRC.rc;
        }

        dbOpen();
        lDbBundle.dbEmittenten.readAll(1);
        rcMandantenArray = lDbBundle.dbEmittenten.ergebnisArray;
        dbClose();

        return 1;
    }

}
