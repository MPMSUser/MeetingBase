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
package de.meetingapps.meetingportal.meetComEclM;

import de.meetingapps.meetingportal.meetComAllg.CaDatumZeit;
import de.meetingapps.meetingportal.meetComBVerwaltung.BvReload;
import de.meetingapps.meetingportal.meetComBlManaged.BlMFuelleEclMAusPufferOderDBEE;
import de.meetingapps.meetingportal.meetComBlManaged.BlMFuellePuffer;
import de.meetingapps.meetingportal.meetComBlManaged.BlMPuffer;
import de.meetingapps.meetingportal.meetComDb.DbBasis;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@RequestScoped
@Named
public class EclDbM {

    @Inject
    EclParamM eclParamM;
    @Inject
    EclPortalTexteM eclTextePortalM;
    @Inject
    BlMPuffer blmPuffer;
    @Inject
    BlMFuellePuffer blmFuellePuffer;
    @Inject
    BlMFuelleEclMAusPufferOderDBEE blMFuelleEclMAusPufferOderDBEE;

    private DbBundle dbBundle = null;

    public void refreshParameterAusEclM() {
        dbBundle.eclEmittent = eclParamM.getEclEmittent();
        dbBundle.paramServer = eclParamM.getParamServer();
        dbBundle.eclGeraeteSet = eclParamM.getEclGeraeteSet();
        dbBundle.eclGeraetKlasseSetZuordnung = eclParamM.getEclGeraetKlasseSetZuordnung();
        dbBundle.paramGeraet = eclParamM.getParamGeraet();
        dbBundle.eclUserLogin = eclParamM.getEclUserLogin();

        dbBundle.terminlisteTechnisch = eclParamM.getTerminlisteTechnisch();

        dbBundle.param = eclParamM.getParam();

        dbBundle.fehlerDeutschArray = eclTextePortalM.getFehlerDeutschArray();
        dbBundle.fehlerEnglischArray = eclTextePortalM.getEnglischarray();

        dbBundle.clGlobalVar = eclParamM.getClGlobalVar();
    }

    /**Standard-Öffnung, Mandant ist bereits gesetzt
     * 
     * Voraussetzung: 
     * > eclParamM.getClGlobalVar().arbeitsplatz enthält Arbeitsplatz-Nummer
     * 		(ist im Zweifelsfall mit 9999 vorbelegt ...)
     * 
     * Mandant wird nur gesetzt, wenn:
     * > eclParamM.getClGlobalVar().mandant, hvJahr, hvNummer, datenbereich enthält verwendeten Mandant
     * */
    public void openAll() {
        openAllOhneParameterCheck();

        /*****Prüfen, ob Parameter nachgeladen werden sollen*****/
        BvReload bvReload = new BvReload(dbBundle);
        int mandant = 0;
        if (eclParamM.getClGlobalVar().mandant != 0) {
            mandant = eclParamM.getClGlobalVar().mandant;
        }
        bvReload.checkReload(mandant);

        blMFuelleEclMAusPufferOderDBEE.fuelleGlobalParam(false, bvReload);
        blMFuelleEclMAusPufferOderDBEE.fuelleMandantenParam(false, bvReload);
        blMFuelleEclMAusPufferOderDBEE.fuelleUserLogin(false, bvReload);

    }

    public void openAllOhneParameterCheck() {

        /****Table-Klassen vorbereiten und Verbindung aufbauen*****/
        DbBasis.setIstEE(true);
        dbBundle = new DbBundle(true);
        refreshParameterAusEclM();
        dbBundle.openAllOhneParameterCheck();

        dbBundle.dbBasis.beginTransaction();
    }

    /**Eröffnet nur "Weitere" (d.h. Ergänzend zu openAll)*/
    public void openWeitere() {
        dbBundle.openWeitere();
    }

    public void closeAll() {
        int verzoegerung = 0;
        if (eclParamM.getParam() != null) {
            verzoegerung = eclParamM.getParam().paramBasis.pLocalVerzoegerungOpen;
        }
        if (verzoegerung != 0) {
            System.out.println("Start Wait " + CaDatumZeit.DatumZeitStringFuerDatenbank());
            try {
                Thread.sleep(verzoegerung);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Ende Wait " + CaDatumZeit.DatumZeitStringFuerDatenbank());

        }
        dbBundle.dbBasis.endTransactionFinal();
        dbBundle.closeAll();
    }

    public void closeAllAbbruch() {
//        dbBundle.dbBasis.rollbackTransaction();
        dbBundle.dbBasis.endTransactionFinal();
        dbBundle.closeAll();

    }

    /******************Standard Getter und Setter**************************************/

    public DbBundle getDbBundle() {
        return dbBundle;
    }

}
