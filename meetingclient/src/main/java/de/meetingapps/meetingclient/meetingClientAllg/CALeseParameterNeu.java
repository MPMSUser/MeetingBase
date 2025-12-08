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
package de.meetingapps.meetingclient.meetingClientAllg;

import de.meetingapps.meetingclient.meetingClient.BlParameterUeberWebService;
import de.meetingapps.meetingportal.meetComBVerwaltung.BvReload;
import de.meetingapps.meetingportal.meetComBl.BlFuelleStaticAusDBAufClient;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComHVParam.ParamS;

/**
 * The Class CALeseParameterNeu.
 */
public class CALeseParameterNeu {

    /**
     * Liest HV-Parameter entweder über Web-Service oder über Datenbank neu ein.
     * Nach dem Aufruf dieser Funktion muß ggf. lokales DbBundle neu initialisiert
     * werden, damit dort die Parameter richtig zur Verfügung stehen
     *
     * @return the int
     */
    public int leseHVParameter() {
        int rc = 1;
        if (ParamS.clGlobalVar.webServicePfadNr != -1) {
            BlParameterUeberWebService lBlInitHVWebServices = new BlParameterUeberWebService();
            rc = lBlInitHVWebServices.holeHVParameter();
            return rc;
        } else {
            DbBundle dbBundle = new DbBundle();
            dbBundle.openAllOhneParameterCheck();
            BvReload bvReload = new BvReload(dbBundle);
            bvReload.checkReload(dbBundle.clGlobalVar.mandant);

            /*Geräte-Parameter / globale Parameter einlesen*/
            BlFuelleStaticAusDBAufClient blFuellePufferAusDBAufClient = new BlFuelleStaticAusDBAufClient(dbBundle);
            rc = blFuellePufferAusDBAufClient.fuelleMandantenParam(true, bvReload);
            rc = blFuellePufferAusDBAufClient.fuelleUserLogin(true, bvReload);
            dbBundle.closeAll();
            return rc;
        }

    }

}
