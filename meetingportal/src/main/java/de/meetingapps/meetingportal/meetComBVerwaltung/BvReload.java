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
package de.meetingapps.meetingportal.meetComBVerwaltung;

import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComDb.DbReload;
import de.meetingapps.meetingportal.meetComEntities.EclReload;

/**Funktionen zum Setzen, Abfragen, Rücksetzen der Versionen zum Reload von Parametern,
 * App-/Portal-Texten etc. (siehe auch EclReload)
 *
 */
public class BvReload {

    private DbBundle dbBundle = null;

    public BvReload(DbBundle pDbBundle) {
        dbBundle = pDbBundle;
    }

    /*****************setzen auf Reload - d.h. ident (als Version) wird um 1 höchgezählt**************************/
    private void setReload(int pMandant, int pIdent) {

        DbReload dbReload = new DbReload(dbBundle);
        EclReload lReload = new EclReload();
        lReload.ident = pIdent;
        lReload.mandant = pMandant;
        dbReload.read(lReload);
        if (dbReload.anzErgebnis() == 0) {
            lReload.reload = 1;
            dbReload.insert(lReload);
        } else {
            lReload = dbReload.ergebnisPosition(0);
            lReload.reload += 1;
            dbReload.update(lReload);
        }

    }

    public void setReloadParameter(int pMandant) {
        setReload(pMandant, 1);
    }

    public void setReloadParameterGeraete() {
        setReload(0, 3);
    }

    public void setReloadTexte(int pMandant) {
        /**Mal so mal so verwendet!*/
        setReload(pMandant, 4);
        setReload(pMandant, 5);
    }

    public void setReloadPortalAppTexte(int pMandant) {
        /**Mal so mal so verwendet!*/
        setReload(pMandant, 4);
        setReload(pMandant, 5);
    }

    public void setReloadParameterServer() {
        setReload(0, 6);
    }

    public void setReloadEmittenten() {
        setReload(0, 7);
    }

    public void setReloadUserLogin() {
        setReload(0, 8);
    }

    public void setReloadWeisungen(int pMandant) {
        setReload(pMandant, 9);
    }

    public void setReloadAbstimmungen(int pMandant) {
        setReload(pMandant, 10);
    }

    public void setReloadAbstimmungenWeisungenOhneAbbruch(int pMandant) {
        setReload(pMandant, 11);
    }

    /************************clear Reload-Kennzeichen*******************/
    private void clearReload(int pMandant, int pIdent) {

//        DbReload dbReload = new DbReload(dbBundle);
//        EclReload lReload = new EclReload();
//        lReload.ident = pIdent;
//        lReload.mandant = pMandant;
//        dbReload.delete(lReload);
        
        setReload(pMandant, pIdent);

    }

    public void clearReloadParameter(int pMandant) {
        clearReload(pMandant, 1);
    }

    public void clearReloadParameterGeraete() {
        clearReload(0, 3);
    }

    public void clearReloadTexte(int pMandant) {
        clearReload(pMandant, 4);
        clearReload(pMandant, 5);
    }

    public void clearReloadPortalAppTexte(int pMandant) {
        clearReload(pMandant, 5);
        clearReload(pMandant, 4);
    }

    public void clearReloadParameterServer() {
        clearReload(0, 6);
    }

    public void clearReloadEmittenten() {
        setReload(0, 7);
    }

    public void clearReloadUeserLogin() {
        setReload(0, 8);
    }

    public void clearReloadWeisungen(int pMandant) {
        setReload(pMandant, 9);
    }

    public void clearReloadAbstimmungen(int pMandant) {
        setReload(pMandant, 10);
    }

    public void clearReloadAbstimmungenWeisungenOhneAbbruch(int pMandant) {
        setReload(pMandant, 11);
    }

    /*****************Abfrage Reload-Kennzeichen***************/
    /**Wird in HVParam übertragen*/
    public int reloadParameter = 0;

    /**Wird in EclGeraeteSet übertragen*/
    public int reloadParameterGeraete = 0;

    public int reloadTexte = 0;

    public int reloadPortalAppTexte = 0;

    /**Wird in ParamServer übertragen*/
    public int reloadParameterServer = 0;

    /**Wird in EclEmittenten übertragen*/
    public int reloadEmittenten = 0;

    /**Wird in EclUserLogin übertragen*/
    public int reloadUserLogin = 0;

    public int reloadWeisungen = 0;

    public int reloadAbstimmungen = 0;

    public int reloadAbstimmungenWeisungenOhneAbbruch = 0;

    /**Nach dieser Funktion reload* gesetzt*/
    public void checkReload(int pMandant) {

        reloadParameter = -1;
        reloadParameterGeraete = -1;
        reloadTexte = -1;
        reloadPortalAppTexte = -1;
        reloadParameterServer = -1;
        reloadEmittenten = -1;
        reloadUserLogin = -1;
        reloadWeisungen = -1;
        reloadAbstimmungen = -1;
        reloadAbstimmungenWeisungenOhneAbbruch = -1;

        DbReload dbReload = new DbReload(dbBundle);
        dbReload.read_all(pMandant);

        EclReload lReload = null;
        for (int i = 0; i < dbReload.anzErgebnis(); i++) {
            lReload = dbReload.ergebnisPosition(i);
            switch (lReload.ident) {
            case 1: {
                reloadParameter = lReload.reload;
                break;
            }
            case 3: {
                reloadParameterGeraete = lReload.reload;
                break;
            }
            case 4: {
//                reloadTexte = lReload.reload;
//                
//                reloadPortalAppTexte = lReload.reload;//mal so mal so verwendet

                break;
            }
            case 5: {
                reloadPortalAppTexte = lReload.reload;
                
                reloadTexte = lReload.reload;//mal so mal so verwendet

                break;
            }
            case 6: {
                reloadParameterServer = lReload.reload;
                break;
            }
            case 7: {
                reloadEmittenten = lReload.reload;
                break;
            }
            case 8: {
                reloadUserLogin = lReload.reload;
                break;
            }
            case 9: {
                reloadWeisungen = lReload.reload;
                break;
            }
            case 10: {
                reloadAbstimmungen = lReload.reload;
                break;
            }
            case 11: {
                reloadAbstimmungenWeisungenOhneAbbruch = lReload.reload;
                break;
            }
            }
        }

    }

}
