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

public class DbNummernformSetV6 {

    //	private Connection verbindung=null;
    private DbBundle dbBundle = null;

    /*************************Initialisierung***************************/
    public DbNummernformSetV6(DbBundle pDbBundle) {
        /* Verbindung in lokale Daten eintragen*/
        if (pDbBundle == null) {
            CaBug.drucke("DbNummernformSetV6.init 001 - dbBundle nicht initialisiert");
            return;
        }
        if (pDbBundle.dbBasis == null) {
            CaBug.drucke("DbNummernformSetV6.init 002 - dbBasis nicht initialisiert");
            return;
        }

        //		verbindung=pDbBundle.dbBasis.verbindung;
        dbBundle = pDbBundle;
    }

    public int alterTableGeraeteSetV6_V7() {
        int rc = 0;
        DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);

        rc = lDbLowLevel.rawOperation("ALTER TABLE db_meetingcomfort.tbl_nummernformset "
                + "ADD COLUMN `vonEintrittSub11` INT(11) NULL DEFAULT NULL AFTER `bisStimmSub55`, "
                + "ADD COLUMN `vonEintrittSub12` INT(11) NULL DEFAULT NULL AFTER `vonEintrittSub11`, "
                + "ADD COLUMN `bisEintrittSub11` INT(11) NULL DEFAULT NULL AFTER `vonEintrittSub12`, "
                + "ADD COLUMN `bisEintrittSub12` INT(11) NULL DEFAULT NULL AFTER `bisEintrittSub11`, "
                + "ADD COLUMN `vonEintrittSub21` INT(11) NULL DEFAULT NULL AFTER `bisEintrittSub12`, "
                + "ADD COLUMN `vonEintrittSub22` INT(11) NULL DEFAULT NULL AFTER `vonEintrittSub21`, "
                + "ADD COLUMN `bisEintrittSub21` INT(11) NULL DEFAULT NULL AFTER `vonEintrittSub22`, "
                + "ADD COLUMN `bisEintrittSub22` INT(11) NULL DEFAULT NULL AFTER `bisEintrittSub21`, "
                + "ADD COLUMN `vonEintrittSub31` INT(11) NULL DEFAULT NULL AFTER `bisEintrittSub22`, "
                + "ADD COLUMN `vonEintrittSub32` INT(11) NULL DEFAULT NULL AFTER `vonEintrittSub31`, "
                + "ADD COLUMN `bisEintrittSub31` INT(11) NULL DEFAULT NULL AFTER `vonEintrittSub32`, "
                + "ADD COLUMN `bisEintrittSub32` INT(11) NULL DEFAULT NULL AFTER `bisEintrittSub31`, "
                + "ADD COLUMN `vonEintrittSub41` INT(11) NULL DEFAULT NULL AFTER `bisEintrittSub32`, "
                + "ADD COLUMN `vonEintrittSub42` INT(11) NULL DEFAULT NULL AFTER `vonEintrittSub41`, "
                + "ADD COLUMN `bisEintrittSub41` INT(11) NULL DEFAULT NULL AFTER `vonEintrittSub42`, "
                + "ADD COLUMN `bisEintrittSub42` INT(11) NULL DEFAULT NULL AFTER `bisEintrittSub41`, "
                + "ADD COLUMN `vonEintrittSub51` INT(11) NULL DEFAULT NULL AFTER `bisEintrittSub42`, "
                + "ADD COLUMN `vonEintrittSub52` INT(11) NULL DEFAULT NULL AFTER `vonEintrittSub51`, "
                + "ADD COLUMN `bisEintrittSub51` INT(11) NULL DEFAULT NULL AFTER `vonEintrittSub52`, "
                + "ADD COLUMN `bisEintrittSub52` INT(11) NULL DEFAULT NULL AFTER `bisEintrittSub51`; ");
        if (rc < 0) {
            return rc;
        }

        rc = lDbLowLevel.rawOperation("ALTER TABLE `db_meetingcomfort`.`tbl_nummernformset` "
                + "DROP COLUMN `laengeSK`, " + "DROP COLUMN `laengeEK`; ");
        if (rc < 0) {
            return rc;
        }

        return rc;

    }

}
