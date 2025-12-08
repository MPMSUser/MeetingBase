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
package de.meetingapps.meetingclient.meetingClient;

import de.meetingapps.meetingportal.meetComBl.BlFuelleStaticAusDBAufClient;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComHVParam.HVParam;
import de.meetingapps.meetingportal.meetComHVParam.ParamS;
import de.meetingapps.meetingportal.meetComStub.CInjects;
import de.meetingapps.meetingportal.meetComStub.WSClient;
import de.meetingapps.meetingportal.meetComWE.WEGeraeteParameterGet;
import de.meetingapps.meetingportal.meetComWE.WEGeraeteParameterGetRC;
import de.meetingapps.meetingportal.meetComWE.WEHVParameterGet;
import de.meetingapps.meetingportal.meetComWE.WEHVParameterGetRC;
import de.meetingapps.meetingportal.meetComWE.WEKIAVFuerVollmachtDritte;
import de.meetingapps.meetingportal.meetComWE.WEKIAVFuerVollmachtDritteRC;
import de.meetingapps.meetingportal.meetComWE.WELoginVerify;

/**
 * Initialisieren / holen der HV-Daten vom Server über Web-Services.
 */
public class BlParameterUeberWebService {

    /**
     * Holt / legt ab: ParamS.setParamGeraet ParamS.setEclGeraeteSet
     * 
     * Initialisiert auf Leer!: ParamS.setParam
     *
     * @return the int
     */
    public int holeGeraeteParameter() {
        int rc;

        WELoginVerify weLoginVerify = null;

        /***** Nun Geräteübergreifende Parameter holen ******/

        WSClient wsClient = new WSClient();

        /*HVParameter*/
        weLoginVerify = new WELoginVerify();
        WEGeraeteParameterGet weGeraeteParameterGet = new WEGeraeteParameterGet();
        weGeraeteParameterGet.weLoginVerify = weLoginVerify;
        WEGeraeteParameterGetRC weGeraeteParameterGetRC = wsClient.geraeteParameterGet(weGeraeteParameterGet);

        ParamS.setParam(new HVParam());
        ParamS.setParamGeraet(weGeraeteParameterGetRC.paramGeraet);
        ParamS.setEclGeraeteSet(weGeraeteParameterGetRC.eclGeraeteSet);

        rc = weGeraeteParameterGetRC.getRc();
        if (rc < 1) {
            return rc;
        }

        DbBundle lDbBundle = new DbBundle(); //Nur zur Variablenübergabe
        BlFuelleStaticAusDBAufClient blFuelleStatusAusDBAufClient = new BlFuelleStaticAusDBAufClient(lDbBundle);
        blFuelleStatusAusDBAufClient.fuelleClGLobalVarAusParamGeraet();
        return 1;
    }

    /**Holt:
     * HVParameter
     * ParameterGerät
     * Abstimmungsliste (über CInjects.leseWeisungsAgendaWeb)
     * KIAVVollmachtFuerDritte
     * lesePraesenzAbstimmungsDaten (über CInjects.lesePraesenzAbstimmungsDaten) - ggf. hier noch was zusammenlegbar!
     *
     * und legt diese ab in
     * ParamS.setParam
     * ParamS.setParamGeraet
     * CInjects.weisungsAgenda*
     * CInjects.weKIAVFuerVollmachtDritteRC
     */
    public int holeHVParameter() {
        int rc = 1;
        WELoginVerify weLoginVerify = null;

        /***** Nun übergreifende Daten initialisieren ******/

        WSClient wsClient = new WSClient();

        /*HVParameter*/
        weLoginVerify = new WELoginVerify(); /*Mandatennummer wird in WSClient immer gesetzt*/
        WEHVParameterGet weHVParameterGet = new WEHVParameterGet();
        weHVParameterGet.weLoginVerify = weLoginVerify;
        WEHVParameterGetRC weHVParameterGetRC = wsClient.hvParameterGet(weHVParameterGet);
        if (weHVParameterGetRC.rc < 1) {
            return weHVParameterGetRC.rc;
        }

        ParamS.setParam(weHVParameterGetRC.hvParam);
        ParamS.setParamGeraet(weHVParameterGetRC.paramGeraet);
        ParamS.setEclEmittent(weHVParameterGetRC.emittent);
        ParamS.setTerminlisteTechnisch(weHVParameterGetRC.terminlisteTechnisch);

        /** WEKIAVFuerVollmachtenDritte holen und in CINJects ablegen */
        weLoginVerify = new WELoginVerify(); /*Mandatennummer wird in WSClient immer gesetzt*/
        WEKIAVFuerVollmachtDritte weKIAVFuerVollmachtDritte = new WEKIAVFuerVollmachtDritte();
        weKIAVFuerVollmachtDritte.weLoginVerify = weLoginVerify;

        WEKIAVFuerVollmachtDritteRC weKIAVFuerVollmachtDritteGetRC = wsClient
                .kiavFuerVollmachtDritteGet(weKIAVFuerVollmachtDritte);
        rc = weKIAVFuerVollmachtDritteGetRC.getRc();
        if (rc < 1) {
            return rc;
        }
        CInjects.weKIAVFuerVollmachtDritteRC = weKIAVFuerVollmachtDritteGetRC;

        boolean brc = CInjects.lesePraesenzAbstimmungsDaten(wsClient);
        if (brc == false) {
            return -1;
        }

        return 1;
    }
    
    public int holeAbstimmungen(WSClient pWSClient) {
        boolean brc = CInjects.lesePraesenzAbstimmungsDaten(pWSClient);
        if (brc == false) {
            return -1;
        }

        return 1;
       
    }

}
