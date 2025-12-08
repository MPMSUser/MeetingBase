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

import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclScan;
import de.meetingapps.meetingportal.meetComEntities.EclVerarbeitungsLauf;
import de.meetingapps.meetingportal.meetComEntities.EclVerarbeitungsProtokoll;
import de.meetingapps.meetingportal.meetComKonst.KonstVerarbeitungsArt;

public class BlVerarbeitungsLauf {

    /*******Return-Werte*****************/

    public EclVerarbeitungsLauf[] rcVerarbeitungsLaeufe = null;
    public EclVerarbeitungsProtokoll[] rcVerarbeitungsProtokoll = null;
    public EclScan[] rcScanlauf = null;

    private DbBundle dbBundle = null;

    public BlVerarbeitungsLauf(DbBundle pDbBundle) {
        dbBundle = pDbBundle;
    }

    /**Ergebnis in rcVerarbeitungsLaeufe*/
    public void leseAlleVerarbeitungslaeufeZuMandant() {
        dbBundle.dbVerarbeitungsLauf.readAll(dbBundle.clGlobalVar.mandant);
        rcVerarbeitungsLaeufe = dbBundle.dbVerarbeitungsLauf.ergebnisArray;
    }

    /**Ergebnis in rcVerarbeitungsProtokoll und rcScanlauf*/
    public void leseScanLaufZuVerarbeitungslauf(int pVerarbeitungslauf) {
        dbBundle.dbVerarbeitungsLauf.read(pVerarbeitungslauf);
        if (dbBundle.dbVerarbeitungsLauf.anzErgebnis() == 0) {
            return;
        }
        rcVerarbeitungsLaeufe = dbBundle.dbVerarbeitungsLauf.ergebnisArray;
        int art = rcVerarbeitungsLaeufe[0].verarbeitungsArt;
        if (art == KonstVerarbeitungsArt.scanLaufAnmeldestelle) {
            dbBundle.dbVerarbeitungsProtokoll.readAll(pVerarbeitungslauf);
            rcVerarbeitungsProtokoll = dbBundle.dbVerarbeitungsProtokoll.ergebnisArray;

            dbBundle.dbScan.read_scanVorgaenge(pVerarbeitungslauf);
            rcScanlauf = dbBundle.dbScan.ergebnisArray;
        }
    }
}
