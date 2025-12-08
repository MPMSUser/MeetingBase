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
package de.meetingapps.meetingportal.meetComBrM;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Request;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComEclM.EclParamM;
import de.meetingapps.meetingportal.meetComEntitiesGenossenschaftSys.EgxGetAdressenRC;
import de.meetingapps.meetingportal.meetComHVParam.ParamS;
import de.meetingapps.meetingportal.meetingportTController.TPermanentSession;
import io.jsonwebtoken.security.InvalidKeyException;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@RequestScoped
@Named
public class BrMGenossenschaftCallClient {

    /*Hier Online-Anbindung zum Mitgliederverzeichnis implementieren*/
    
   
    private int logDrucken = 3;

    private @Inject EclParamM eclParamM;
    private @Inject TPermanentSession tPermanentSession;
    
    private static final String GET_REQUEST_PFAD_ADRESSEN = "betterorange/v1/adressen";

    private String doGetRequest(String requestPfad)
            throws ClientProtocolException, IOException, InvalidKeyException, NoSuchAlgorithmException {
        return null;
    }

    private String decodeResponse(HttpResponse httpResponse) {
        return null;
    }

    /*
     * Start Get-Requests
     */

    public EgxGetAdressenRC doGetRequestAdressen(String page) {
        return null;
    }

    /*
     * Start ping
     */

    public Boolean sendPingRequest(String ipAddress) throws UnknownHostException, IOException {
        return null;
    }

    /*
     * Ende Ping
     */
}