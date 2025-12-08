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
package de.meetingapps.meetingclient.meetingBestand;

import de.meetingapps.meetingclient.meetingClientDialoge.CtrlRootDrucklauf;
import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaDatumZeit;
import de.meetingapps.meetingportal.meetComEntities.EclInsti;
import de.meetingapps.meetingportal.meetComHVParam.ParamS;
import de.meetingapps.meetingportal.meetComKonst.KonstVerarbeitungslaufArt;
import de.meetingapps.meetingportal.meetComReports.RepSammelAnmeldebogen;
import de.meetingapps.meetingportal.meetingCoreReport.RpDrucken;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;

/**
 * The Class CtrlInstiPflegeDetailsDruckSammelAnmeldebogen.
 */
public class CtrlInstiPflegeDetailsDruckSammelAnmeldebogen extends CtrlRootDrucklauf {

    /** The log drucken. */
    private int logDrucken = 3;

    /** The aktuelle ecl insti. */
    private EclInsti aktuelleEclInsti = null;

    /**
     * Initialize.
     */
    @FXML
    void initialize() {

        /*Masken-spezifische Auswahl setzen*/
        auswahlReportausgabe = true;
        auswahlReportausgabeVarianten = true;
        auswahlCSV = false;
        auswahlCSVVarianten = false;

        auswahlDrucklaufAlleZulaessig = true;

        verarbeitungslaufArt = KonstVerarbeitungslaufArt.sammelAnmeldeBogen;
        verarbeitungslaufSubArt = aktuelleEclInsti.ident;

        /*Allgemeine Initialisierung*/

        initializeRootDrucklaufFxml();

    }

    /**
     * On btn ausfuehren.
     *
     * @param event the event
     */
    @Override
    @FXML
    public void onBtnAusfuehren(ActionEvent event) {
        CaBug.druckeLog("CtrlInstiPflegeDetailsDruckSammelAnmeldebogen", logDrucken, 10);

        boolean brc = doAusfuehrenEingabeVerarbeiten();
        if (!brc) {
            return;
        }

        RpDrucken rpDrucken = new RpDrucken();
        rpDrucken.initClientDrucke();
        CaBug.druckeLog("rcAusgabeReportVarianteGewaehlt=" + rcAusgabeReportVarianteGewaehlt, logDrucken, 10);
        rpDrucken.exportDatei = "SAM" + Integer.toString(aktuelleEclInsti.ident) + aktuelleEclInsti.kurzBezeichnung
                + "_" + CaDatumZeit.DatumZeitStringFuerDateiname();
        if (rcDrucklaufNr >= 0) {
            rpDrucken.exportDatei += Integer.toString(rcDrucklaufNr);
        }
        switch (rcAusgabeReportVarianteGewaehlt) {
        case 1:/*normale Druckabfrage*/
            rpDrucken.exportFormat = -1;
            rpDrucken.exportDatei = "";
            break;
        case 2:/*PDF in Kundenordner*/
            rpDrucken.exportFormat = 6;
            break;
        case 3:/*PDF in meetingAudruckeIntern - Standard*/
            rpDrucken.exportFormat = 7;
            break;
        default:
            break;
        }
        rpDrucken.initListe(lDbBundle);

        RepSammelAnmeldebogen repSammelAnmeldebogen = new RepSammelAnmeldebogen(false, lDbBundle);
        /*int rc = */repSammelAnmeldebogen.sammelAnmeldebogenDrucklauf(rpDrucken, rcDruckvariante, rcDrucklaufNr,
                ParamS.clGlobalVar.arbeitsplatz, ParamS.clGlobalVar.benutzernr, aktuelleEclInsti);
        //
        //		System.out.println("rc="+rc);
        //		if (rc==0) {
        //			fehlerMeldung("Keine Sätze zum Drucken gefunden!");
        //			eigeneStage.hide();
        //		}
        //

        doAusfuehrenBeendet();
    }

    /**
     * Inits the.
     *
     * @param pEigeneStage      the eigene stage
     * @param pAktuelleEclInsti the aktuelle ecl insti
     */
    public void init(Stage pEigeneStage, EclInsti pAktuelleEclInsti) {
        eigeneStage = pEigeneStage;
        aktuelleEclInsti = pAktuelleEclInsti;
    }

}
