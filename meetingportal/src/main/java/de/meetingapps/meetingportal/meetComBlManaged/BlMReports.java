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
package de.meetingapps.meetingportal.meetComBlManaged;

import de.meetingapps.meetingportal.meetComEclM.EclDbM;
import de.meetingapps.meetingportal.meetComKonst.KonstWeisungserfassungSicht;
import de.meetingapps.meetingportal.meetComReports.RepGastkartenliste;
import de.meetingapps.meetingportal.meetComReports.RepMeldeliste;
import de.meetingapps.meetingportal.meetComReports.RepSammelkarten;
import de.meetingapps.meetingportal.meetComReports.RepStimmausschluss;
import de.meetingapps.meetingportal.meetingCoreReport.RpDrucken;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@RequestScoped
@Named
public class BlMReports {

    private @Inject EclDbM eclDbM;

    /**entweder pGruppe oder pIdent müssen -1 sein, es können aber auch beide -1 sein => alle
     * 
     * open/close wird hier gehandelt*/
    public RpDrucken weisungssummen(int art, int mitVerschlossenen, String lfdNummer, int nurVertreterNr,
            int selektionGruppe, int selektionIdent) {
        eclDbM.openAll();

        RpDrucken rpDrucken = new RpDrucken();
        rpDrucken.initServer();

        RepSammelkarten repAusgabe = new RepSammelkarten(KonstWeisungserfassungSicht.internWeisungsreports);

        repAusgabe.druckeSammelkarten(eclDbM.getDbBundle(), rpDrucken, art, mitVerschlossenen, lfdNummer,
                nurVertreterNr, selektionGruppe, selektionIdent);

        eclDbM.closeAll();
        return rpDrucken;
    }

    public RpDrucken stimmausschlussListe(String lfdNummer) {
        eclDbM.openAll();

        RpDrucken rpDrucken = new RpDrucken();
        rpDrucken.initServer();

        RepStimmausschluss repStimmausschluss = new RepStimmausschluss();
        repStimmausschluss.druckeStimmausschlussliste(eclDbM.getDbBundle(), rpDrucken, lfdNummer);

        eclDbM.closeAll();

        return rpDrucken;
    }

    private String rcMeldelistenExportDatei = "";
    private String rcMeldelistenExportExcelDatei = "";

    /**pNegativHV
     * 	=1 => HV nie präsent
     *  =2 => HV Aktuell nicht präsent
     *  
     *  pSelektion_inSammelkarte default=-1
     */
    public RpDrucken meldeliste(int pSortierung, int pSelektion, int pKlasse, int pNegativHV, int pAnzahlDrucken,
            String pLfdNummer, int pSelektion_inSammelkarte) {
        eclDbM.openAll();

        RpDrucken rpDrucken = new RpDrucken();
        rpDrucken.initServer();

        RepMeldeliste repMeldeliste = new RepMeldeliste();
        repMeldeliste.mitExport = true;
        repMeldeliste.sortierung = pSortierung;
        repMeldeliste.selektion = pSelektion;
        repMeldeliste.selektion_inSammelkarte = pSelektion_inSammelkarte;
        repMeldeliste.klasse = pKlasse;
        repMeldeliste.anzahlDrucken = pAnzahlDrucken;
        if (pNegativHV == 1) {
            repMeldeliste.selektion_praesenz = 2;
            repMeldeliste.selektion_aktionaereInSammelkartenUnterdruecken = 1;
        }
        if (pNegativHV == 2) {
            repMeldeliste.selektion_praesenz = 1;
            repMeldeliste.selektion_aktionaereInSammelkartenUnterdruecken = 1;
        }
        repMeldeliste.druckeMeldeliste(eclDbM.getDbBundle(), rpDrucken, pLfdNummer);

        rcMeldelistenExportDatei = repMeldeliste.exportDatei;
        rcMeldelistenExportExcelDatei = repMeldeliste.exportExcelDatei;

        eclDbM.closeAll();
        return rpDrucken;

    }

    
    private String rcGaestelisteExportDatei = "";

    /**
     * 
     * pListenart: 1=Übersicht, 2=Präsenz
     * pSortierung: 1=nach Zutrittsnummer, 2=nach Name
     * pSelektion: 1=alle, 2=alle gerade präsenten, 3=alle präsenten oder präsent gewesenen
     */
    public RpDrucken gaesteliste(int pListenart, boolean pMitPasswort, int pSortierung, int pSelektion, 
            String pLfdNummer) {
        eclDbM.openAll();

        RpDrucken rpDrucken = new RpDrucken();
        rpDrucken.initServer();


        RepGastkartenliste repGastkartenliste = new RepGastkartenliste();
        if (pListenart==1) { /*Übersicht*/
            repGastkartenliste.pDruckeMitPasswort = pMitPasswort;
            repGastkartenliste.druckeGastkartenUebersichtListe(eclDbM.getDbBundle(), rpDrucken, pSortierung, pLfdNummer);
        }
        else {
            /*Mit Präsenzdaten*/
            repGastkartenliste.druckeGastkartenliste(rpDrucken, eclDbM.getDbBundle(), pSelektion, pSortierung, pLfdNummer, 3);
       }

        rcGaestelisteExportDatei = repGastkartenliste.exportDatei;

        eclDbM.closeAll();
        return rpDrucken;

    }

    
    /******************Standard getter und setter*******************************************/

    public String getRcMeldelistenExportDatei() {
        return rcMeldelistenExportDatei;
    }

    public void setRcMeldelistenExportDatei(String rcMeldelistenExportDatei) {
        this.rcMeldelistenExportDatei = rcMeldelistenExportDatei;
    }

    public String getRcGaestelisteExportDatei() {
        return rcGaestelisteExportDatei;
    }

    public void setRcGaestelisteExportDatei(String rcGaestelisteExportDatei) {
        this.rcGaestelisteExportDatei = rcGaestelisteExportDatei;
    }

    public String getRcMeldelistenExportExcelDatei() {
        return rcMeldelistenExportExcelDatei;
    }

    public void setRcMeldelistenExportExcelDatei(String rcMeldelistenExportExcelDatei) {
        this.rcMeldelistenExportExcelDatei = rcMeldelistenExportExcelDatei;
    }
}
