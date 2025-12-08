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

public class BlPraesenzFeststellen {

    private int logDrucken = 3;

    /**pVerzeichnisNr= 1 bis 4, Standard=1*/
    public void feststellen(DbBundle pDbBundle, int pVerzeichnisNr) {

        int aktuelleListenNummer = 0;
        BlPraesenzlistenNummer lBlPraesenzlistenNummer = new BlPraesenzlistenNummer(pDbBundle);
        aktuelleListenNummer = lBlPraesenzlistenNummer.stelleFest(1);

        BlPraesenzSummen lBlPraesenzSummen = new BlPraesenzSummen(pDbBundle);
        lBlPraesenzSummen.fixiereAktuelleSummen(aktuelleListenNummer, 1);

        CaBug.druckeLog("aktuelleListenNummer=" + aktuelleListenNummer, logDrucken, 3);
        pDbBundle.dbJoined.feststellenPraesenz(aktuelleListenNummer, 1);

    }
}
