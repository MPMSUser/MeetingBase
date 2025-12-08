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

public class DbUserLoginV4 {

    //	private Connection verbindung=null;
    private DbBundle dbBundle = null;

    /*************************Initialisierung***************************/
    public DbUserLoginV4(DbBundle pDbBundle) {
        /* Verbindung in lokale Daten eintragen*/
        if (pDbBundle == null) {
            CaBug.drucke("DbUserLoginV4.init 001 - dbBundle nicht initialisiert");
            return;
        }
        if (pDbBundle.dbBasis == null) {
            CaBug.drucke("DbUserLoginV4.init 002 - dbBasis nicht initialisiert");
            return;
        }

        //		verbindung=pDbBundle.dbBasis.verbindung;
        dbBundle = pDbBundle;
    }

    public int alterTableV4_V5() {
        int rc = 0;
        DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);

        rc = lDbLowLevel.rawOperation("ALTER TABLE `db_meetingcomfort`.`tbl_userlogin` "
                + "ADD COLUMN `neuesPasswortErforderlich` INT(11) NULL AFTER `berechtigungen19`, "
                + "ADD COLUMN `kennungGesperrtDurchSoftware` INT(11) NULL AFTER `neuesPasswortErforderlich`, "
                + "ADD COLUMN `kennungGesperrtManuell` INT(11) NULL AFTER `kennungGesperrtDurchSoftware`, "
                + "ADD COLUMN `passwortZuletztGeaendertAm` CHAR(19) NULL AFTER `kennungGesperrtManuell`, "
                + "ADD COLUMN `altesPasswort1` CHAR(64) NULL AFTER `passwortZuletztGeaendertAm`, "
                + "ADD COLUMN `altesPasswort2` CHAR(64) NULL AFTER `altesPasswort1`, "
                + "ADD COLUMN `altesPasswort3` CHAR(64) NULL AFTER `altesPasswort2`; ");
        if (rc < 0) {
            return rc;
        }

        rc = lDbLowLevel.rawOperation(

                "ALTER TABLE `db_meetingcomfort`.`tbl_userlogin`  "
                        + "ADD COLUMN `trivialPasswortZulaessig` INT(11) NULL AFTER `kennungGesperrtManuell`, "
                        + "ADD COLUMN `gruppenKennungHV` INT(11) NULL AFTER `trivialPasswortZulaessig`, "
                        + "DROP PRIMARY KEY, " + "ADD PRIMARY KEY (`mandant`, `userLoginIdent`); ");
        if (rc < 0) {
            return rc;
        }

        return rc;

    }

}
