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
import de.meetingapps.meetingportal.meetComEntities.EclParameterSet;

public class StubParameterSet extends StubRoot {

    public StubParameterSet(boolean pIstServer, DbBundle pDbBundle) {
        super(pIstServer, pDbBundle);
    }

    public EclParameterSet[] leseParameterSets() {
        EclParameterSet[] lParameterSetArray = null;
        dbOpen();
        lDbBundle.openWeitere();
        lDbBundle.dbParameterSet.readAll();
        int anz = lDbBundle.dbParameterSet.anzErgebnis();
        System.out.println("anz=" + anz);
        if (anz == 0) {
            lParameterSetArray = new EclParameterSet[0];
        } else {
            lParameterSetArray = lDbBundle.dbParameterSet.ergebnis();
        }
        dbClose();
        return lParameterSetArray;

    }

    /**Erzeugt neues ParameterSet*/
    public int insertTblParameterSet(EclParameterSet pParameterSet) {
        dbOpen();
        lDbBundle.openWeitere();
        lDbBundle.dbParameterSet.insert(pParameterSet);
        dbClose();
        return pParameterSet.ident;
    }

    /**Update bestehendes ParameterSet*/
    public int updateTblParameterSet(EclParameterSet pParameterSet) {
        dbOpen();
        lDbBundle.openWeitere();
        lDbBundle.dbParameterSet.update(pParameterSet);
        dbClose();
        return 1;
    }

    /**Speichert die Parameter des aktuellen Mandanten als ParameterSet.
     * EclParameterSet muß bereits vorher gespeichert sein!
     */
    public int speichereParameterZuParameterSet(int pParameterSetIdent) {
        dbOpen();
        lDbBundle.dbParameter.ergHVParam = lDbBundle.param;
        lDbBundle.dbParameter.nutzeSetBeginn(pParameterSetIdent);
        lDbBundle.dbParameter.updateHVParam_all();
        lDbBundle.dbParameter.nutzeSetEnde();
        dbClose();
        return 1;
    }

}
