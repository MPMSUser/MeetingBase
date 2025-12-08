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

import de.meetingapps.meetingportal.meetComBl.BlBestaetigungsWorkflow;
import de.meetingapps.meetingportal.meetComEclM.EclDbM;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@RequestScoped
@Named
public class UWorkflowStatistik {

    @Inject
    private XSessionVerwaltung xSessionVerwaltung;
    @Inject
    private EclDbM eclDbM;
    @Inject
    private UWorkflowSession uWorkflowSession;
    @Inject
    private USession uSession;

    public String doRuecksetzen() {
        if (!xSessionVerwaltung.pruefeUStart("uWorkflowStatistik", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "";
        }

        eclDbM.openAll();
        eclDbM.openWeitere();
        eclDbM.getDbBundle().dbBestWorkflow.reservierungAlleZuruecksetzen();
        eclDbM.getDbBundle().dbVorlaeufigeVollmachtEingabe.reservierungAlleZuruecksetzen();
        eclDbM.closeAll();
        statistikAusfuehren();
        uSession.setFehlermeldung("Alle in Bearbeitung zurückgesetzt");
        xSessionVerwaltung.setzeUEnde();
        return "";
    }

    public void statistikAusfuehren() {

        eclDbM.openAll();
        eclDbM.openWeitere();
        BlBestaetigungsWorkflow blBestaetigungsWorkflow = new BlBestaetigungsWorkflow(eclDbM.getDbBundle());
        blBestaetigungsWorkflow.statistik();
        
        /*Anzahl Vertretungen abgelehnt*/
        uWorkflowSession.setAnzahlAbgelehnt(blBestaetigungsWorkflow.rcAnzahlAbgelehnt);
        
        /*Anzahl Vertretungen akzeptiert*/
        uWorkflowSession.setAnzahlAngenommen(blBestaetigungsWorkflow.rcAnzahlAngenommen);
        
        /*Anzahl offene Prüfungen zur Wiedervorlage (Papier)*/
        uWorkflowSession.setAnzahlOffenWiedervorlage(blBestaetigungsWorkflow.rcAnzahlOffenWiedervorlage);
        
        /*Anzahl offene Prüfungen zur Wiedervorlage (Digital)*/
        uWorkflowSession.setAnzahlOffenWiedervorlageDigital(blBestaetigungsWorkflow.rcAnzahlOffenWiedervorlageDigital);
        
        /*Anzahl offene Erst-Prüfungen (Papier)*/
        uWorkflowSession.setAnzahlOffenBasisPruefen(blBestaetigungsWorkflow.rcAnzahlOffenBasisPruefen);
        
        /*Anzahl offene Erst-Prüfungen (Digital)*/
        uWorkflowSession.setAnzahlOffenBasisPruefenDigital(blBestaetigungsWorkflow.rcAnzahlOffenBasisPruefenDigital);
        
        /*Anzahl Bearbeitung nicht abgeschlossen*/
        uWorkflowSession.setAnzahlInBearbeitung(blBestaetigungsWorkflow.rcAnzahlInBearbeitung);

        /*Anzahl Bearbeitung nicht abgeschlossen (Digital)*/
        uWorkflowSession.setAnzahlInBearbeitungDigital(blBestaetigungsWorkflow.rcAnzahlInBearbeitungDigital);

        /*Anzahl nicht als Nachweis verwenden*/
        uWorkflowSession.setAnzahlAndereDokumente(blBestaetigungsWorkflow.rcAnzahlAndereDokumente);

        //		eControllerWorkflowSession.setAnzahlAnmeldungenE(eclDbM.getDbBundle().dbJoined.read_ku178Anzahl("E"));
        //		eControllerWorkflowSession.setAnzahlAnmeldungenF(eclDbM.getDbBundle().dbJoined.read_ku178Anzahl("F"));
        //		eControllerWorkflowSession.setAnzahlAnmeldungenV(eclDbM.getDbBundle().dbJoined.read_ku178Anzahl("V"));
        eclDbM.closeAll();

    }

}
