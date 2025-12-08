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

import org.apache.commons.codec.binary.Base64;

import com.google.gson.Gson;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.streaming.srdII.proxyLieferant01.entities.Response.RespAuthentication;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * The {@code RequAuthentication} class handles the authentication process to acquire a Bearer token
 * using client credentials. It interacts with the OAuth authorization server, sending 
 * and receiving HTTP requests and responses.
 */
public class RequAuthentication {
    
    private String requestUrl = "";
    private String client_id = "";
    private String client_secret = "";
    
    int logDrucken = 10;
    
    /**
     * Acquires a Bearer token by making a POST request to the authorization server using 
     * client credentials.
     * 
     * @return A {@link RespAuthentication} object containing the Bearer token.
     */
    public RespAuthentication getBearer() {
      
        OkHttpClient client = new OkHttpClient().newBuilder().build();
        
        Gson gson = new Gson();
        RespAuthentication respAuthentication = new RespAuthentication();

        RequestBody body = new FormBody.Builder()
                               .add("grant_type", "client_credentials")
                               .build();

        Request request = new Request.Builder()
                            .url(requestUrl)
                            .method("POST", body)
                            .addHeader("Authorization", getBasicAuth())
                            .addHeader("Content-Type", "application/x-www-form-urlencoded")
                            .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            String responseBody = response.body().string();
            respAuthentication = gson.fromJson(responseBody, RespAuthentication.class);
            CaBug.druckeLog(respAuthentication.toString(), logDrucken, 10);
        } catch (IOException e) {
            CaBug.druckeLog(e.toString(), logDrucken, 10);
            e.printStackTrace();
        }
        return respAuthentication;
    }
    
    /**
     * Generates the Basic Authorization header using client_id and client_secret.
     * 
     * @return A Base64 encoded string representing the Basic Authorization header.
     */
    private String getBasicAuth() {
        String authString = client_id + ":" + client_secret;
        return "Basic " + Base64.encodeBase64String(authString.getBytes());
    }
    
    /**
     * Gets the URL used for the authentication request.
     * 
     * @return The request URL.
     */
    public String getRequestUrl() {
        return requestUrl;
    }

    /**
     * Sets the URL used for the authentication request.
     * 
     * @param requestUrl The new request URL.
     */
    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    /**
     * Gets the client ID used for authentication.
     * 
     * @return The client ID.
     */
    public String getClient_id() {
        return client_id;
    }

    /**
     * Sets the client ID used for authentication.
     * 
     * @param client_id The new client ID.
     */
    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    /**
     * Gets the client secret used for authentication.
     * 
     * @return The client secret.
     */
    public String getClient_secret() {
        return client_secret;
    }

    /**
     * Sets the client secret used for authentication.
     * 
     * @param client_secret The new client secret.
     */
    public void setClient_secret(String client_secret) {
        this.client_secret = client_secret;
    }
}