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

import java.util.LinkedList;

import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclAbstimmung;
import de.meetingapps.meetingportal.meetComEntities.EclStimmkarteInhalt;
import de.meetingapps.meetingportal.meetComEntities.EclStimmkartenBlock;

public class BlAbstimmungTools {

    private DbBundle lDbBundle = null;

    public EclStimmkartenBlock rcEclStimmkartenBlock = null;

    public void initialisieren(DbBundle pDbBundle) {
        lDbBundle = pDbBundle;
    }

    /**liest rcEclStimmkartenBlock aus Datenbank ein
     * Returnwert -1 => keine Stimmkartenblöcke vorhanden vorhanden.*/
    public int leseStimmkartenblock() {
        rcEclStimmkartenBlock = new EclStimmkartenBlock();
        lDbBundle.dbStimmkarteInhalt.readAll();
        int anzStimmkarten = lDbBundle.dbStimmkarteInhalt.anzErgebnis();
        if (anzStimmkarten == 0) {
            return -1;
        }
        rcEclStimmkartenBlock.stimmkartenBlock = new EclStimmkarteInhalt[anzStimmkarten];

        for (int i = 0; i < anzStimmkarten; i++) {
            rcEclStimmkartenBlock.stimmkartenBlock[i] = lDbBundle.dbStimmkarteInhalt.ergebnisPosition(i);
            rcEclStimmkartenBlock.stimmkartenBlock[i].abstimmungenListe = new LinkedList<EclAbstimmung>();

            lDbBundle.dbAbstimmungenZuStimmkarte
                    .readZuStimmkartenNr(rcEclStimmkartenBlock.stimmkartenBlock[i].stimmkartenNr);
            int anzAbstimmungenZuStimmkarte = lDbBundle.dbAbstimmungenZuStimmkarte.anzErgebnis();
            for (int i1 = 0; i1 < anzAbstimmungenZuStimmkarte; i1++) {
                int nrAbstimmung = lDbBundle.dbAbstimmungenZuStimmkarte.ergebnisPosition(i1).identAbstimmungAufKarte;
                lDbBundle.dbAbstimmungen.leseIdent(nrAbstimmung);
                //				System.out.println("BlAbstimmungTools i="+i+" i1="+i1+" nrAbstimmung="+nrAbstimmung);
                rcEclStimmkartenBlock.stimmkartenBlock[i].abstimmungenListe
                        .add(lDbBundle.dbAbstimmungen.abstimmungengGefunden(0));
            }

        }

        return 1;
    }
}
