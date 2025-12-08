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
package de.meetingapps.meetingportal.meetingSocket;

import java.io.IOException;
import java.net.URI;

import org.quartz.SchedulerException;

import de.meetingapps.meetingportal.cron.CronJobSocketTrigger;
import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaTokenUtil;
import jakarta.websocket.ClientEndpoint;
import jakarta.websocket.ContainerProvider;
import jakarta.websocket.DeploymentException;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.WebSocketContainer;

@ClientEndpoint
public class BsClient {

    private int logDruckenLogin = 3;
    private int logDrucken = 3;

    private Session session;
    private String urlSocket = "";

    public BsClient(String pUrlSocket) {
        CaBug.druckeLog("Initialisierung mit ConnectTOWebSocket", logDrucken, 10);
        urlSocket = pUrlSocket;
        try {
            connectToWebSocket();
        } catch (Exception ex) {
            System.err.println(ex.toString());
        }
    }
    
    public BsClient(String pUrlSocket, String startedFrom) {
        CaBug.druckeLog("Initialisierung mit ConnectTOWebSocket", logDrucken, 10);
        urlSocket = pUrlSocket;
        try {
            connectToWebSocket(startedFrom);
        } catch (Exception ex) {
            CaBug.druckeLog("Websocket konnte nicht aufegbaut werden: " + urlSocket, logDrucken, 10);
        }
    }

    @OnOpen
    public void onOpen(Session session) throws IOException {
        CaBug.druckeLog("Verbunden zu Server Endpoint", logDruckenLogin, 10);
        this.session = session;
    }

    @OnMessage
    public void onMessage(String message) {
        CaBug.druckeLog("Nachricht von Server Endpoint: " + message, logDruckenLogin, 10);
    }

    @OnClose
    public void onClose() {
        CaBug.druckeLog("Getrennt von Server Endpoint", logDruckenLogin, 10);
    }

    public void senden(String message) throws IOException {
        while (session == null) {
            CaBug.druckeLog("Noch nicht verbunden", logDruckenLogin, 10);
        }
        session.getBasicRemote().sendText(message);
    }

    public void sendPingToAll() throws IOException {
        while (session == null) {
            CaBug.druckeLog("Noch nicht verbunden", logDruckenLogin, 10);
        }
        session.getBasicRemote().sendText("*PingAll*");
    }
    
    /**nachrichtenText==leer, dann Nachricht löschen
     * empfaengerKennung==leer, dann an alle
     * art=1 => Lauftext; 2=statischer Text*/
    public void sendenNachricht(String empfaengerKennung, int art, String nachrichtenText) throws IOException {
        CaBug.druckeLog("empfaengerKennung=" + empfaengerKennung + " Nachricht:" + nachrichtenText, logDrucken, 10);
        while (session == null) {
            CaBug.druckeLog("Noch nicht verbunden", logDruckenLogin, 10);
        }
        String message = "*NEWS*::" + empfaengerKennung + "::" + Integer.toString(art) + "::10000::" + nachrichtenText;
        session.getBasicRemote().sendText(message);
    }

    private void connectToWebSocket() {
        CaTokenUtil caTokenUtil = new CaTokenUtil();
        String jwt = caTokenUtil.generateToken("local", 0);
        CaBug.druckeLog("", logDrucken, 10);
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        try {
            URI url = URI.create(urlSocket + "?jwt=" + jwt); //ws://localhost/meetingportal/meetingSocket?jwt=xxx
            container.connectToServer(this, url);
        } catch (DeploymentException | IOException ex) {
            CaBug.druckeLog("Websocket konnte nicht aufgebaut werden.", logDrucken, 10);
        }
    }
    
    private void connectToWebSocket(String startedFrom) {
        CaTokenUtil caTokenUtil = new CaTokenUtil();
        String jwt = caTokenUtil.generateToken("local", 0);
        CaBug.druckeLog("", logDrucken, 10);
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        try {
            URI url = URI.create(urlSocket + "?jwt=" + jwt); //ws://localhost/meetingportal/meetingSocket?jwt=xxx
            container.connectToServer(this, url);
        } catch (DeploymentException | IOException ex) {
            CaBug.druckeLog("Websocket konnte nicht aufegbaut werden.", logDrucken, 10);
            if(startedFrom.equals("cron")) {
                try {
                    CronJobSocketTrigger.stopJob();
                    CaBug.druckeLog("Cronjob gestoppt.", logDrucken, 10);
                } catch (SchedulerException e) {
                    CaBug.druckeLog("Cronjob konnte nicht gestoppt werden.", logDrucken, 10);
                }
            }
        }
    }

    public void disconnectFromWebSocket() {
        CaBug.druckeLog("", logDrucken, 10);
        while (session == null) {
            CaBug.druckeLog("Noch nicht vorhanden", logDruckenLogin, 10);
        }
        try {
            session.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
