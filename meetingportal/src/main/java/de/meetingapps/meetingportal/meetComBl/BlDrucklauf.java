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

import de.meetingapps.meetingportal.meetComAllg.CaDatumZeit;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclDrucklauf;
import de.meetingapps.meetingportal.meetComStub.StubRoot;
import de.meetingapps.meetingportal.meetComStub.WEStubBlDrucklauf;
import de.meetingapps.meetingportal.meetComStub.WEStubBlDrucklaufRC;
import de.meetingapps.meetingportal.meetComWE.WELoginVerify;

public class BlDrucklauf extends StubRoot {

    public BlDrucklauf(boolean pIstServer, DbBundle pDbBundle) {
        super(pIstServer, pDbBundle);
    }

    public EclDrucklauf rcDrucklauf = null;

    /*1*/
    /**Rückgabe:
     * > rcDrucklauf
     * - Mandantenabhängig 
     */
    public int erzeugeNeuenDrucklauf(int arbeitsplatznr, int benutzernr, int pVerarbeitungslaufArt,
            int pVerarbeitungslaufSubArt) {
        return erzeugeNeuenDrucklauf(arbeitsplatznr, benutzernr, pVerarbeitungslaufArt,
                pVerarbeitungslaufSubArt, 1);
    }
    
    public int erzeugeNeuenDrucklauf(int arbeitsplatznr, int benutzernr, int pVerarbeitungslaufArt,
                int pVerarbeitungslaufSubArt, int pMandantenabhaengigOderGlobal) {

        if (verwendeWebService()) {
            WEStubBlDrucklauf weStubBlDrucklauf = new WEStubBlDrucklauf();
            weStubBlDrucklauf.stubFunktion = 1;
            weStubBlDrucklauf.arbeitsplatznr = arbeitsplatznr;
            weStubBlDrucklauf.benutzernr = benutzernr;
            weStubBlDrucklauf.pVerarbeitungslaufArt = pVerarbeitungslaufArt;
            weStubBlDrucklauf.pVerarbeitungslaufSubArt = pVerarbeitungslaufSubArt;
            weStubBlDrucklauf.pMandantenabhaengigOderGlobal = pMandantenabhaengigOderGlobal;

            WELoginVerify weLoginVerify = new WELoginVerify();
            weStubBlDrucklauf.setWeLoginVerify(weLoginVerify);

            WEStubBlDrucklaufRC weStubBlDrucklaufRC = wsClient.stubBlDrucklauf(weStubBlDrucklauf);

            if (weStubBlDrucklaufRC.rc < 1) {
                return weStubBlDrucklaufRC.rc;
            }

            rcDrucklauf = weStubBlDrucklaufRC.rcDrucklauf;
            return weStubBlDrucklaufRC.rc;
        }

        rcDrucklauf = new EclDrucklauf();
        rcDrucklauf.durchgefuehrtArbeitsplatzNr = arbeitsplatznr;
        rcDrucklauf.durchgefuehrtBenutzerNr = benutzernr;
        rcDrucklauf.erzeugtAm = CaDatumZeit.DatumZeitStringFuerDatenbank();
        rcDrucklauf.drucklaufArt = pVerarbeitungslaufArt;
        rcDrucklauf.drucklaufSubArt = pVerarbeitungslaufSubArt;
        rcDrucklauf.anzahlSaetze = 0;

        dbOpenUndWeitere();
        if (pMandantenabhaengigOderGlobal==2) {
            lDbBundle.dbDrucklauf.mandantenabhaengig=false;
        }
        lDbBundle.dbDrucklauf.insert(rcDrucklauf);
        lDbBundle.dbDrucklauf.mandantenabhaengig=true;
        dbClose();
        return 1;
    }

    /*2*/
    /**Input:
     * > rcDrucklauf
     * 
     * Rückgabe:
     * > aktualisierter rcDrucklauf
     */
    public int updateAnzDrucklauf(int pAnzahl) {
        return updateAnzDrucklauf(pAnzahl, 1);
    }
    

    public int updateAnzDrucklauf(int pAnzahl, int pMandantenabhaengigOderGlobal) {

        if (verwendeWebService()) {
            WEStubBlDrucklauf weStubBlDrucklauf = new WEStubBlDrucklauf();
            weStubBlDrucklauf.stubFunktion = 2;
            weStubBlDrucklauf.rcDrucklauf = rcDrucklauf;
            weStubBlDrucklauf.pAnzahl = pAnzahl;
            weStubBlDrucklauf.pMandantenabhaengigOderGlobal = pMandantenabhaengigOderGlobal;

            WELoginVerify weLoginVerify = new WELoginVerify();
            weStubBlDrucklauf.setWeLoginVerify(weLoginVerify);

            WEStubBlDrucklaufRC weStubBlDrucklaufRC = wsClient.stubBlDrucklauf(weStubBlDrucklauf);

            if (weStubBlDrucklaufRC.rc < 1) {
                return weStubBlDrucklaufRC.rc;
            }

            rcDrucklauf = weStubBlDrucklaufRC.rcDrucklauf;
            return weStubBlDrucklaufRC.rc;
        }

        dbOpenUndWeitere();
        rcDrucklauf.anzahlSaetze = pAnzahl;
        if (pMandantenabhaengigOderGlobal==2) {
            lDbBundle.dbDrucklauf.mandantenabhaengig=false;
        }
        lDbBundle.dbDrucklauf.update(rcDrucklauf);
        lDbBundle.dbDrucklauf.mandantenabhaengig=true;
        dbClose();
        return 1;
    }
}
