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
package de.meetingapps.meetingportal.meetingport;

import java.io.Serializable;
import java.util.List;

import de.meetingapps.meetingportal.meetComEclM.EclParamM;
import de.meetingapps.meetingportal.meetComEclM.EclUserLoginM;
import de.meetingapps.meetingportal.meetComEh.EhJsfMenue;
import de.meetingapps.meetingportal.meetComHVParam.ParamSpezial;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@SessionScoped
@Named
public class UMenueSession implements Serializable {
    private static final long serialVersionUID = 9099396139571462572L;

    private @Inject EclUserLoginM eclUserLoginM;
    private @Inject EclParamM eclParamM;

    private boolean hvCockpitEin = false;

    /**Konfigurierbare Menüeinträge für Wortmeldeablauf; wird abhängig vom Set
     * konfiguriert
     */
    private List<EhJsfMenue> wortmeldungViews=null;


    /**Funktionen für Menü*/

    public boolean menue() {
        return menueNachrichten() | menueAbstimmung() | menueSonstiges();
    }

    public boolean menueNachrichten() {
        return eclUserLoginM.pruefe_uportal_mailSenden();
    }

    public boolean menueAbstimmung() {
        return eclUserLoginM.pruefe_govVal_admin();
    }

    public boolean menueServicelinetools() {
        return eclUserLoginM.pruefe_uportal_servicelinetools();
    }

    public boolean menueVirtuelleHV() {
        return eclUserLoginM.pruefe_uportal_virtuelleHV() || menueVirtuelleHV_menuePunktBotschaftenLesen() || menueVirtuelleHV_menuePunkte();
    }

    public boolean menueVirtuelleHV_menuePunkte() {
        return eclUserLoginM.pruefe_uportal_virtuelleHV();
    }

    public boolean menueVirtuelleHV_menuePunktBotschaften() {
        return eclUserLoginM.pruefe_uportal_virtuelleHV() || menueVirtuelleHV_menuePunktBotschaftenLesen() || menueVirtuelleHV_menuePunkte() || menueVirtuelleHV_botschaftenBearbeiten();
    }

    public boolean menueVirtuelleHV_menuePunktBotschaftenLesen() {
        return eclUserLoginM.pruefe_uportal_virtuelleHV_botschaftenLesen() || eclUserLoginM.pruefe_uportal_virtuelleHV() || eclUserLoginM.pruefe_uportal_virtuelleHV_botschaftenBearbeiten();
    }

    public boolean menueVirtuelleHV_botschaftenBearbeiten() {
        return eclUserLoginM.pruefe_uportal_virtuelleHV_botschaftenBearbeiten();
    }

    public boolean menueGaeste() {
        return eclUserLoginM.pruefe_emittentenPortal_gaestemodul() || eclUserLoginM.pruefe_uportal_servicelinetools();
    }

    public boolean menueGaestePflege() {
        return eclUserLoginM.pruefe_emittentenPortal_gaestemodul();
    }

    public boolean menueReports() {
        return eclUserLoginM.pruefe_emittentenPortal_anmeldebestand() || eclUserLoginM.pruefe_uportal_dloginReports()
                || eclUserLoginM.pruefe_mitgliederverwaltung() || eclUserLoginM.pruefe_veranstaltungReporting();
    }

    public boolean menueReportsEinzeln(int pReport) {
        boolean kundenReport = false;
        boolean internerReport = false;
        switch (pReport) {
        case 0:
            kundenReport=true;
            internerReport=true;
            break; //pauschal für die, die beide haben dürfen
        case 1:
            kundenReport = true;
            internerReport = false;
            break; // Weisungssummen - alle - für Kunde - ohne geheime
        case 2:
            kundenReport = false;
            internerReport = true;
            break; //Weisungssummen intern - für kunden gesperrt,da sonst Deutsche Bank ausweisbar
        case 3:
            kundenReport = false;
            internerReport = true;
            break; //Stimmausschlussliste
        case 4:
            kundenReport = false;
            internerReport = true;
            break; //Negativliste Präsenz
        case 107:
            kundenReport = false;
            internerReport = true;
            break; //Negativliste Präsenz mit Auswahl
        case 110:
            kundenReport = false;
            internerReport = true;
            break; //Willenserklärungen - Menüpunkt

        case 114:
            kundenReport = true;
            internerReport = false;
            break; //Sammelkarten - kurz ohne Weisung
        case 115:
            kundenReport = false;
            internerReport = true;
            break; //Sammelkarten - kurz ohne Weisung - mit Auswahl

        case 116:
            kundenReport = true;
            internerReport = false;
            break; //Sammelkarten - mit Weisung
        case 117:
            kundenReport = false;
            internerReport = true;
            break; //Sammelkarten - mit Weisung - mit Auswahl
        case 118:
            kundenReport = true;
            internerReport = false;
            break; //Sammelkarten - mit Weisung und Aktionären
        case 119:
            kundenReport = false;
            internerReport = true;
            break; //Sammelkarten - mit Weisung unt Aktionären - mit Auswahl
        case 120:
            kundenReport = true;
            internerReport = false;
            break; //Sammelkarten Export - J/N/E in Spalte
        case 121:
            kundenReport = false;
            internerReport = true;
            break; //Sammelkarten Export - J/N/E in Spalte - mit Auswahl
        case 122:
            kundenReport = true;
            internerReport = false;
            break; //Sammelkarten Export - Stimmen in Spalte
        case 123:
            kundenReport = false;
            internerReport = true;
            break; //Sammelkarten Export - Stimmen in Spalte - mit Auswahl
        case 125:
        case 126:
        case 127:
        case 128:
            kundenReport = false;
            internerReport = true;
            break; //Kontrolle Anmeldungen
        case 129:
            kundenReport = false;
            internerReport = true;
            break; //Kontrollliste Weisungen

        case 130:
        case 131:
            kundenReport = false;
            internerReport = true;
            break; //Elektronischer Einladungsversand
       }

        if (kundenReport) {
            if (eclUserLoginM.pruefe_emittentenPortal_anmeldebestand()) { return true;}
        }
        if (internerReport) {
            if (eclUserLoginM.pruefe_uportal_dloginReports()) {return true;}
        }
        return false;

    }

    public boolean menueReportsSonder() {
        return eclUserLoginM.pruefe_uPortalReportsSonder();
    }

    public boolean menueMitgliederverwaltung() {
        return eclUserLoginM.pruefe_mitgliederverwaltung();
    }

    public boolean menueErstregistrierungenPortalReporting() {
        return true;
    }

    public boolean menueVeranstaltungReporting() {
        return eclUserLoginM.pruefe_veranstaltungReporting();
    }


    public boolean menueWorkflow() {
        return eclUserLoginM.pruefe_emittentenPortal_bestWorkflowBasis()
                || eclUserLoginM.pruefe_emittentenPortal_bestWorkflowSpezial()
                || eclUserLoginM.pruefe_emittentenPortal_bestWorkflowAdmin();
    }

    public boolean menueWorkflowBasis() {
        return (eclUserLoginM.pruefe_emittentenPortal_bestWorkflowBasis() && !ParamSpezial.ku216(eclParamM.getParam().mandant));
    }

    public boolean menueWorkflowSpezial() {
        return eclUserLoginM.pruefe_emittentenPortal_bestWorkflowSpezial();
    }

    public boolean menueWorkflowAdmin() {
        return eclUserLoginM.pruefe_emittentenPortal_bestWorkflowAdmin();
    }

    public boolean menueWorkflowAuswahl() {
        return (eclUserLoginM.pruefe_emittentenPortal_bestWorkflowBasis() && ParamSpezial.ku216(eclParamM.getParam().mandant));
    }

    public boolean menueKonfiguration() {
        return eclUserLoginM.pruefe_uportal_pflegePortaltexteStandard() || eclUserLoginM.pruefe_uportal_pflegePortaltexteMandant() || eclUserLoginM.pruefe_schluessel_mandant()
                || eclUserLoginM.pruefe_schluessel_mandantenUebergreifend();
    }

    public boolean menueKonfiguration_schluessel() {
        return eclUserLoginM.pruefe_schluessel_mandant()
                || eclUserLoginM.pruefe_schluessel_mandantenUebergreifend();
    }

    public boolean menueKonfiguration_schluessel_laenderHolen() {
        return (eclParamM.getParam().paramPortal.registerAnbindung==1) //GenossenschaftDat/ku178-Anbindung
                && eclUserLoginM.pruefe_schluessel_mandant()
                ;
    }
    public boolean menuePflegePortaltexteStandard() {
        return eclUserLoginM.pruefe_uportal_pflegePortaltexteStandard();
    }

    public boolean menuePflegePortaltexteMandant() {
        return eclUserLoginM.pruefe_uportal_pflegePortaltexteMandant();
    }

    public boolean menueSonstiges() {
        return true;
    }

    public boolean menueSonstiges_mandantenKennungen() {
        return eclUserLoginM.pruefe_mandantenKennungen();
    }

    public boolean menueImportExport() {
        return eclUserLoginM.pruefe_uPortalImportExportStandard() || eclUserLoginM.pruefe_uPortalImportExportSonder();
    }


 

    public boolean menueDatenmanipulation() {
        return eclUserLoginM.pruefe_uPortalDatenmanipulation();
    }

    public boolean menueDatenmanipulationMenue() {
        return eclUserLoginM.pruefe_uPortalDatenmanipulation() || eclUserLoginM.pruefe_uPortalRuecksetzenDemoUser();
    }
    public boolean menueSonderFunktionen() {
        return eclUserLoginM.pruefe_uPortalSonderFunktionen();
    }

    public boolean menueDemoAktionaereZuruecksetzen() {
        return eclUserLoginM.pruefe_uPortalRuecksetzenDemoUser();
    }

    public boolean menueVeranstaltungsmanagement() {
        return eclUserLoginM.pruefe_uportal_veranstaltungsmanagement_pflegen() || eclUserLoginM.pruefe_uportal_veranstaltungsmanagement_abfragen();
    }

    public boolean menueVeranstaltungsmanagementPflegen() {
        return eclUserLoginM.pruefe_uportal_veranstaltungsmanagement_pflegen();
    }

    public boolean menueVeranstaltungsmanagementAbfragen() {
        return eclUserLoginM.pruefe_uportal_veranstaltungsmanagement_pflegen() || eclUserLoginM.pruefe_uportal_veranstaltungsmanagement_abfragen();
    }

    public boolean menueVerschiedenes() {
        return menueVerschiedenesUnterlagen();
    }

    public boolean menueVerschiedenesUnterlagen() {
        return eclUserLoginM.pruefe_uportal_verschiedenes_unterlagen();
    }
    
    public boolean menueEntwickler() {
        
        return true;
    }

    /*************Standard-Getter und Setter**************************/

    public boolean isHvCockpitEin() {
        return hvCockpitEin;
    }

    public void setHvCockpitEin(boolean hvCockpitEin) {
        this.hvCockpitEin = hvCockpitEin;
    }

    public List<EhJsfMenue> getWortmeldungViews() {
        return wortmeldungViews;
    }

    public void setWortmeldungViews(List<EhJsfMenue> wortmeldungViews) {
        this.wortmeldungViews = wortmeldungViews;
    }

}
