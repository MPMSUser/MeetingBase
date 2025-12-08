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
package de.meetingapps.meetingportal.meetComDbVersion;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComDb.DbLowLevel;

public class DbGeraeteKlasseSetZuordnungV2 {

    //	private Connection verbindung=null;
    private DbBundle dbBundle = null;

    /*************************Initialisierung***************************/
    public DbGeraeteKlasseSetZuordnungV2(DbBundle pDbBundle) {
        /* Verbindung in lokale Daten eintragen*/
        if (pDbBundle == null) {
            CaBug.drucke("DbGeraeteKlasseSetZuordnungV2.init 001 - dbBundle nicht initialisiert");
            return;
        }
        if (pDbBundle.dbBasis == null) {
            CaBug.drucke("DbGeraeteKlasseSetZuordnungV2.init 002 - dbBasis nicht initialisiert");
            return;
        }

        //		verbindung=pDbBundle.dbBasis.verbindung;
        dbBundle = pDbBundle;
    }

    public int alterTableV2_V3() {
        int rc = 0;
        DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);

        rc = lDbLowLevel.rawOperation("ALTER TABLE `db_meetingcomfort`.`tbl_geraetklassesetzuordnung` "
                + "ADD COLUMN `standortIdent` INT(11) NULL DEFAULT 0 AFTER `geraeteKlasseIdent`;");
        if (rc < 0) {
            return rc;
        }

        rc = lDbLowLevel.rawOperation("ALTER TABLE `db_meetingcomfort`.`tbl_geraetklassesetzuordnung` "
                + "ADD COLUMN `lokaleDatenZuruecksetzenBeimNaechstenStart` INT(11) NULL DEFAULT 0 AFTER `standortIdent`;");
        if (rc < 0) {
            return rc;
        }

        return rc;

    }

}
