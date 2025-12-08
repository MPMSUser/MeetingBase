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
package de.meetingapps.meetingportal.meetingportTFunktionen;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComBVerwaltung.BvReload;
import de.meetingapps.meetingportal.meetComBlManaged.BlMFuelleEclMAusPufferOderDBEE;
import de.meetingapps.meetingportal.meetComBlManaged.BlMFuellePuffer;
import de.meetingapps.meetingportal.meetComBlManaged.BlMPuffer;
import de.meetingapps.meetingportal.meetComEclM.EclDbM;
import de.meetingapps.meetingportal.meetComEclM.EclParamM;
import de.meetingapps.meetingportal.meetComEclM.EclPortalTexteM;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

/**Controller für allgemeine, Funktionen für Aktionärsportal*/

@RequestScoped
@Named
public class TAllgemein {

    @Inject
    private TSeitensteuerung tSeitensteuerung;

    @Inject
    private EclParamM eclParamM;
    @Inject
    private BlMPuffer blMPuffer;
    @Inject
    private BlMFuellePuffer blMFuellePuffer;
    @Inject
    private EclDbM eclDbM;
    @Inject
    private BlMFuelleEclMAusPufferOderDBEE blMFuelleEclMAusPufferOderDBEE;
    @Inject
    private EclPortalTexteM eclPortalTexteM;

    /**Für Nummernpflege-Bereich: Die PortalTexte werden neu geladen, die bestehende Seite mit den neuen Texten angezeigt*/
    public void doRefreshTexte() {
        eclDbM.openAll();
        BvReload lBvReload = new BvReload(eclDbM.getDbBundle());
        lBvReload.checkReload(eclDbM.getDbBundle().clGlobalVar.mandant);
        blMFuellePuffer.fuelleMandantenTexte(lBvReload.reloadPortalAppTexte);

        blMFuelleEclMAusPufferOderDBEE.fuelleAlleVariablenBeiStart();

        eclDbM.closeAll();
        return;
    }

    /**Für Nummernpflege-Bereich: die Nummern zu den Portal-Texten werden angezeigt*/
    public void doNummernEin() {
        eclPortalTexteM.setNummernAnzeigen(true);
        tSeitensteuerung.belegeFuerSeite();
        return;
    }

    /**Für Nummernpflege-Bereich: die Nummern zu den Portal-Texten werden ausgeblendet*/
    public void doNummernAus() {
        eclPortalTexteM.setNummernAnzeigen(false);
        return;
    }

    /**Einzige, derzeit sicher funktionierende Reload-Funktion!*/
    public synchronized String doReloadAllePufferAlleMandanten() {
        blMPuffer.clearAllPuffer();
        return "";
    }

    /**Alle Parameter (global + aktueller Mandant) werden auf "Reload" gesetzt - und mit EclOpen auch gleich neu eingelesen.
     * Kann direkt aus JSF-Oberfläche aufgerufen werden*/
    public synchronized String doReloadParameterAlle() {
        doReloadParameterGlobal();
        doReloadParameterAktuellenMandant();
        return "";
    }

    /**Alle globalen Parameter werden auf "Reload" gesetzt - und mit EclOpen auch gleich neu eingelesen.
     * Kann direkt aus JSF-Oberfläche aufgerufen werden*/
    public synchronized String doReloadParameterGlobal() {
        eclDbM.openAllOhneParameterCheck();
        BvReload bvReload = new BvReload(eclDbM.getDbBundle());
        bvReload.setReloadParameterGeraete();
        bvReload.setReloadParameterServer();
        bvReload.setReloadEmittenten();
        bvReload.setReloadUserLogin();
        eclDbM.closeAll();
        /*Nochmal öffnen zum Nachladen*/
        eclDbM.openAll();
        eclDbM.closeAll();

        CaBug.druckeInfo("XControllerAllgemein.doReloadParameterGlobal 001 Reload durchgeführt");
        return "";
    }

    /**Alle globalen Parameter werden auf "Reload" gesetzt - und mit EclOpen auch gleich neu eingelesen.
     * Kann direkt aus JSF-Oberfläche aufgerufen werden*/
    public synchronized String doReloadParameterAktuellenMandant() {
        int lMandantenNr = eclParamM.getClGlobalVar().mandant;
        eclDbM.openAll();
        BvReload bvReload = new BvReload(eclDbM.getDbBundle());
        bvReload.setReloadTexte(lMandantenNr);
        bvReload.setReloadParameter(lMandantenNr);
        bvReload.setReloadPortalAppTexte(lMandantenNr);
        eclDbM.closeAll();
        /*Nochmal öffnen zum Nachladen*/
        eclDbM.openAll();
        eclDbM.closeAll();

        CaBug.druckeInfo("XControllerAllgemein.doReloadParameterAktuellenMandant 001 Mandant " + lMandantenNr
                + "  Reload durchgeführt");
        return "";
    }

    /****************Standard Setter und Getter**********************************/

}
