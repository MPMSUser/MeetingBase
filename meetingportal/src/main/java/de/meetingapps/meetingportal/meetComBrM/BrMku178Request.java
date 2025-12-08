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
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComEclM.EclParamM;
import de.meetingapps.meetingportal.meetComEntitiesku178.EprPostBeteiligungserhoehung;
import de.meetingapps.meetingportal.meetComEntitiesku178.EprPostBeteiligungserhoehungRC;
import io.jsonwebtoken.security.InvalidKeyException;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@RequestScoped
@Named
public class BrMku178Request {

    private int logDrucken = 10;
    
    private @Inject EclParamM eclParamM;

    private static final String POST_REQUEST_PDF_BETEILIGUNGSERHOEHUNG = "v1/applications/cooperative/increaseShares";
    private static final String AUTHENTICATION_USER_TEST = "betterorange-test";
    private static final String AUTHENTICATION_USER_PROD = "betterorange";
    private static final String AUTHENTICATION_PASSWORD = "Ca-xeHtRT?fF";

    private String doPostRequest(String requestPfad, String requestBody) throws ClientProtocolException, IOException {

        Request request = Request.Post(requestPfad);
        request.setHeader("Authorization", getAuthentication());
        request.bodyString(requestBody, ContentType.APPLICATION_JSON);
        CaBug.druckeLog(requestBody, logDrucken, 10);
        request.setHeader("Accept", "application/json");
        request.setHeader("Content-Type", "application/json");
        HttpResponse httpResponse = request.execute().returnResponse();
        return decodeResponse(httpResponse);

    }

    public EprPostBeteiligungserhoehungRC doPostRequestBeteiligungserhoehung(EprPostBeteiligungserhoehung eprPostBeteiligungserhoehung) {
        EprPostBeteiligungserhoehungRC eprPostBeteiligungserhoehungRC = new EprPostBeteiligungserhoehungRC();
        try {
            Gson gson = new Gson();
            String json = gson.toJson(eprPostBeteiligungserhoehung);
            eprPostBeteiligungserhoehungRC = gson.fromJson(doPostRequest(eclParamM.getParam().paramPortal.apiku178_url + POST_REQUEST_PDF_BETEILIGUNGSERHOEHUNG, json), EprPostBeteiligungserhoehungRC.class);
        } catch (InvalidKeyException | IOException e) {
            e.printStackTrace();
            return null;
        }
        return eprPostBeteiligungserhoehungRC;
    }

    private String decodeResponse(HttpResponse httpResponse) throws UnsupportedOperationException, IOException{
        int statusCode = httpResponse.getStatusLine().getStatusCode();
        CaBug.druckeLog(String.valueOf(statusCode), logDrucken, 10);
        if (statusCode == 201) {
            String html = "";
            if (httpResponse.getEntity() != null) {
                try {
                    html = EntityUtils.toString(httpResponse.getEntity());
                } catch (ParseException e) {
                    CaBug.druckeLog(e.toString().toString(), logDrucken, 10);
                    e.printStackTrace();
                    return null;
                } catch (IOException e) {
                    CaBug.druckeLog(e.toString().toString(), logDrucken, 10);
                    e.printStackTrace();
                    return null;
                }
                return html;
            } else {
                return null;
            }
        } else {
            CaBug.druckeLog(EntityUtils.toString(httpResponse.getEntity()), logDrucken, 10);
            CaBug.druckeLog(httpResponse.getStatusLine().toString(), logDrucken, 10);
            return null;
        }
    }

    private String getAuthentication() {
        String authentication = AUTHENTICATION_USER_TEST + ":" + AUTHENTICATION_PASSWORD;
        if(eclParamM.getParam().paramPortal.apiku178_url.contains("api.ku178.net")) {
            authentication = AUTHENTICATION_USER_PROD + ":" + AUTHENTICATION_PASSWORD;
        }
        byte[] encoded_authentication = Base64.getEncoder().encode(authentication.getBytes(StandardCharsets.UTF_8));
        String autheticationHeader = "Basic " + new String(encoded_authentication);
        return autheticationHeader;
    }

}
