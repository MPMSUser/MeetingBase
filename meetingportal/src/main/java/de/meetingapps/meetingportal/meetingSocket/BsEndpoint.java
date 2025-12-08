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
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

import org.quartz.SchedulerException;

import com.google.gson.Gson;

import de.meetingapps.meetingportal.cron.CronJobSocketTrigger;
import de.meetingapps.meetingportal.meetComAllg.CaBug;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;

@ServerEndpoint("/wortmeldungSocket")
public class BsEndpoint {

    private static Boolean cronStarted = false;

    private static Set<Session> clients = Collections.synchronizedSet(new HashSet<Session>());

    /*
     * Verknüpft PortalUser mit socketUser
     */
    private static HashMap<String, Session> socketUserToPortalUser = new HashMap<String, Session>();

    @OnMessage
    public void onMessage(String message, Session client) throws IOException {
        synchronized (clients) {

            String[] messageGesplittet = message.split("::");

            if (message.startsWith("*WortmeldungAktion") && !messageGesplittet[0].equals("*WortmeldungAktionAktualisiereVL*")) {
                String mandant = getMandantDreistellig(messageGesplittet[1]);
                String aktionaer = messageGesplittet[2];
                String socketUser = mandant + aktionaer;
                sendToUser(socketUser, messageGesplittet[0]);
            } else if (messageGesplittet[0].equals("*WortmeldungAktionAktualisiereVL*")) {
                String mandant = getMandantDreistellig(messageGesplittet[1]);
                String aktionaer = messageGesplittet[2];
                String socketUser = mandant + aktionaer;
                sendToUserVLMulti(socketUser, messageGesplittet[0]);
            } else if (message.equals("ping")) {
                client.getBasicRemote().sendText("pong");
            } else if (message.equals("*PingAll*")) {
                pingAll();
            } else if (message.startsWith("*AllActive*")) {
                sendMsg(client, getAllActive(messageGesplittet[1]));
            } else if (message.startsWith("*IsActive*")) {
                sendMsg(client, isActive(messageGesplittet[1]).toString());
            }

        }
    }

    @OnOpen
    public void onOpen(Session session) throws IOException {
        clients.add(session);
        String socketUser = session.getUserPrincipal().getName().replace("::", "");
        if (session.getUserPrincipal().getName().contains("::vl")) {
            socketUser = socketUser + getVLNumber(socketUser);
        }

        socketUserToPortalUser.put(socketUser, session);

        CaBug.druckeLog("Verbunden zu WortmeldungSocket: " + session.getUserPrincipal().getName(), 10, 10);
        if (!cronStarted) {
            starteCron();
        }
    }

    @OnClose
    public void onClose(Session session) {
        clients.remove(session);
        removeFromMap(session);
        CaBug.druckeLog("Getrennt von WortmeldungSocket: " + session.getId(), 10, 10);
    }

    private int sendToUser(String socketUser, String msg) {

        if (socketUserToPortalUser.containsKey(socketUser)) {
            Session client = socketUserToPortalUser.get(socketUser);

            sendMsg(client, msg);
            return 1;
        }
        return -1;
    }

    private void sendToUserVLMulti(String socketUser, String msg) {
        for (String key : socketUserToPortalUser.keySet()) {
            if (key.contains(socketUser)) {
                Session client = socketUserToPortalUser.get(key);
                sendMsg(client, msg);
            }
        }
    }

    private void pingAll() throws IOException {
        for (String key : socketUserToPortalUser.keySet()) {
            Session session = socketUserToPortalUser.get(key);
            try {
                CaBug.druckeLog("Sende Ping zu: " + key, 10, 10);
                session.getBasicRemote().sendText("*Ping*");
            } catch (Exception e) {
                CaBug.druckeLog("Pingfehler: " + key, 10, 10);
                clients.remove(session);
                removeFromMap(session);
            }

        }
    }

    private String getMandantDreistellig(String mandant) {
        while (mandant.length() < 3) {
            mandant = "0" + mandant;
        }
        return mandant;
    }

    private void starteCron() {
        try {
            CaBug.druckeLog("Starte Cronjob Ping", 10, 10);
            CronJobSocketTrigger.startJob();
            cronStarted = true;
        } catch (SchedulerException e) {
            try {
                CaBug.druckeLog("Stoppe Cronjob Ping", 10, 10);
                CronJobSocketTrigger.stopJob();
            } catch (SchedulerException e1) {
                CaBug.druckeLog("Cronjob Ping konnte nicht gestoppt werden.", 10, 10);
            }
            cronStarted = false;
        }
    }

    private String getAllActive(String mandant) {
        ArrayList<BsEntAllActive> activeList = new ArrayList<BsEntAllActive>();
        while (mandant.length() < 3) {
            mandant = "0" + mandant;
        }
        for (String zugangskennung : socketUserToPortalUser.keySet()) {
            if (zugangskennung.startsWith(mandant)) {
                BsEntAllActive bsEntAllActive = new BsEntAllActive();
                bsEntAllActive.setLoginKennung(zugangskennung.substring(3));
                activeList.add(bsEntAllActive);
            }
        }
        Gson gson = new Gson();
        return gson.toJson(activeList);
    }

    private Boolean isActive(String mandantaktionaer) {
        return socketUserToPortalUser.containsKey(mandantaktionaer);
    }

    private String getVLNumber(String socketUser) {
        int numberNew = 0;
        for (String key : socketUserToPortalUser.keySet()) {
            if (key.contains(socketUser)) {
                int number = Integer.parseInt(key.replace(socketUser, ""));
                if (number >= numberNew) {
                    numberNew += 1;
                }
            }
        }
        return String.valueOf(numberNew);
    }

    private void removeFromMap(Session session) {
        for (Entry<String, Session> entry : socketUserToPortalUser.entrySet()) {
            if (entry.getValue().getId() == session.getId()) {
                socketUserToPortalUser.remove(entry.getKey());
                break;
            }
        }
    }

    private void sendMsg(Session client, String msg) {
        try {
            client.getBasicRemote().sendText(msg);
        } catch (IOException e) {
            CaBug.druckeLog(msg, 10, 10);
        }
    }

}
