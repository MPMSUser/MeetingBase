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
package de.meetingapps.meetingportal.streaming.srdII.proxyLieferant01.request;

import java.io.IOException;
import java.util.UUID;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaXML;
import de.meetingapps.meetingportal.streaming.srdII.proxyLieferant01.entities.Response.RespAuthentication;
import de.meetingapps.meetingportal.streaming.srdII.proxyLieferant01.entities.Response.RespMessage;
import jakarta.xml.bind.JAXBException;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * The {@code RequMessage} class facilitates sending HTTP POST requests to a specified URL.
 * This class uses the OkHttpClient and CaXML libraries to process and send the request payload.
 * It includes functionalities to set custom URL endpoints and log responses.
 * 
 * <p>Example usage:</p>
 * <pre>{@code
 * RequMessage requMessage = new RequMessage();
 * RespAuthentication respAuth = new RespAuthentication(...);
 * requMessage.postMessage("<your_xml_payload>", respAuth);
 * }</pre>
 */
public class RequMessage {
    
    private String requestUrl = "https://api-uat.proxymity.io/pv/external/iso20022/message";
    int logDrucken = 10;

    /**
     * Sends a POST request with the specified payload and authentication details.
     *
     * @param payload             The XML payload to send in the POST request.
     * @param respAuthentication  The authentication object containing token type and access token.
     * @return A {@link RespMessage} object containing the response message.
     * @throws JAXBException if an error occurs during the XML unmarshalling process.
     */
    public RespMessage postMessage(String payload, RespAuthentication respAuthentication) throws JAXBException {
        OkHttpClient client = new OkHttpClient().newBuilder().build();
        MediaType mediaType = MediaType.parse("application/xml");
        String uuid = UUID.randomUUID().toString();
        RequestBody body = RequestBody.create(payload, mediaType);
        
        Request request = new Request.Builder()
                .url(requestUrl)
                .method("POST", body)
                .addHeader("accept", "application/xml")
                .addHeader("X-Request-ID", uuid)
                .addHeader("Content-Type", "application/xml")
                .addHeader("Authorization", generateAuthToken(respAuthentication))
                .build();
        
        try (Response response = client.newCall(request).execute()) {
            boolean success = response.isSuccessful();
            RespMessage respMessage = parseResponse(response);
            if (!success) {
                // Print Error if request fails
                CaBug.druckeLog(respMessage.getEnvelope().getErrorDetails().getErrorText(), logDrucken, 10);
            }
            return respMessage;
        } catch (IOException e) {
            CaBug.druckeLog(e.toString(), logDrucken, 10);
            e.printStackTrace();
        }
        
        return null;
    }

    /**
     * Generates the authorization token header from the given {@link RespAuthentication} object.
     * 
     * @param respAuthentication The response authentication object containing token type and access token.
     * @return A string representing the authorization token.
     */
    private String generateAuthToken(RespAuthentication respAuthentication) {
        return respAuthentication.getTokenType() + " " + respAuthentication.getAccessToken();
    }

    /**
     * Parses the HTTP response to extract and convert it to a {@link RespMessage} object.
     * 
     * @param response The HTTP response to parse.
     * @return A {@link RespMessage} object containing the parsed response data.
     * @throws JAXBException if an error occurs during the XML unmarshalling process.
     * @throws IOException if an error occurs during reading the response body.
     */
    private RespMessage parseResponse(Response response) throws JAXBException, IOException {
        String responseBody = response.body().string();
        if (responseBody.contains(" xmlns=\"urn:iso:std:iso:20022:tech:xsd:iso20022:ResponsePayload\"")) {
            responseBody = responseBody.replace(" xmlns=\"urn:iso:std:iso:20022:tech:xsd:iso20022:ResponsePayload\"", "");
        }
        CaBug.druckeLog(responseBody, logDrucken, 10);
        return CaXML.unmarshal(responseBody, RespMessage.class);
    }

    /**
     * Gets the URL used for the POST request.
     * 
     * @return The request URL.
     */
    public String getRequestUrl() {
        return requestUrl;
    }

    /**
     * Sets the URL used for the POST request.
     * 
     * @param requestUrl The new request URL.
     */
    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }
}
