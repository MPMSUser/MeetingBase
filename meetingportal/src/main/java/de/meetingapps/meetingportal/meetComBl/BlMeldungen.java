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

import java.util.List;

import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclMeldung;
import de.meetingapps.meetingportal.meetComStub.StubRoot;
import de.meetingapps.meetingportal.meetComStub.WEStubBlMeldungen;
import de.meetingapps.meetingportal.meetComStub.WEStubBlMeldungenRC;
import de.meetingapps.meetingportal.meetComWE.WELoginVerify;

public class BlMeldungen extends StubRoot {

    public EclMeldung eclMeldung = new EclMeldung();
    public List<EclMeldung> praesenzliste = null;
    public int angemeldeteAktien = 0;
    public int mandant = 0;
    public String updateText = "";

    public BlMeldungen(boolean pIstServer, DbBundle pDbBundle) {
        super(pIstServer, pDbBundle);
    }

    public BlMeldungen(boolean pIstServer, DbBundle pDbBundle, int mandant) {
        super(pIstServer, pDbBundle);
        this.mandant = mandant;
    }

    public int updateMeldung(Boolean mitPersonNatJur) {

        if (verwendeWebService()) {

            WEStubBlMeldungen stub = new WEStubBlMeldungen(1, mandant);
            stub.eclMeldung = eclMeldung;
            stub.mitPersonNatJur = mitPersonNatJur;
            WEStubBlMeldungenRC stubRC = verifyLogin(stub);

            if (stubRC.getRc() < 1)
                return stubRC.getRc();

            return stubRC.getRc();

        } else {

            dbOpen();

            int erg = lDbBundle.dbMeldungen.update(eclMeldung, mitPersonNatJur);
            System.out.println(
                    "Update: Meldungen " + updateText + " mIdent:" + eclMeldung.meldungsIdent + " M" + mandant);

            dbClose();

            return erg;

        }
    }

    public int readPraesenz() {

        if (verwendeWebService()) {

            WEStubBlMeldungen stub = new WEStubBlMeldungen(2, mandant);
            WEStubBlMeldungenRC stubRC = verifyLogin(stub);

            if (stubRC.getRc() < 1)
                return stubRC.getRc();

            praesenzliste = stubRC.praesenzliste;
            angemeldeteAktien = stubRC.angemeldeteAktien;

            return stubRC.getRc();

        } else {

            dbOpen();

            praesenzliste = lDbBundle.dbMeldungen.readPraesenz();
            angemeldeteAktien = lDbBundle.dbMeldungen.checkAktienanzahl();

            dbClose();

            return 1;

        }
    }

    public void setParameter(WEStubBlMeldungen stub) {

        eclMeldung = stub.eclMeldung;
        mandant = stub.mandant;

    }

    private WEStubBlMeldungenRC verifyLogin(WEStubBlMeldungen stub) {

        WELoginVerify weLoginVerify = new WELoginVerify();
        stub.setWeLoginVerify(weLoginVerify);

        return wsClient.stubBlMeldungen(stub);
    }

}
