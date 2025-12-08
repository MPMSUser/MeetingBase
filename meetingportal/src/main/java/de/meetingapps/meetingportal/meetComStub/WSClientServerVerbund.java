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
package de.meetingapps.meetingportal.meetComStub;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComHVParam.ClGlobalVar;
import de.meetingapps.meetingportal.meetComHVParam.ParamServer;
import de.meetingapps.meetingportal.meetComWE.WELoginVerify;
import de.meetingapps.meetingportal.meetComWE.WEServerVerbund;
import de.meetingapps.meetingportal.meetComWE.WEServerVerbundRC;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.MediaType;

public class WSClientServerVerbund {

    int logDrucken=10;

    private static /*volatile*/ Client[] client = {null, null, null, null, null};

    private String internerUser = "todo";
    private String internesPasswort = "todo";

    private ParamServer paramServer=null;
    private ClGlobalVar clGlobalVar=null;

    public WSClientServerVerbund(ParamServer pParamServer, ClGlobalVar pClGlobalVar) {
        paramServer=pParamServer;
        clGlobalVar=pClGlobalVar;
   }


    /** Initialisieren client */
    private void initialisiereVerbindungsOjekt(int pServerNr) {
        if (client[pServerNr-1] == null) {
            synchronized (this) {
                // We are using here a variant of the Double-Check-Idiom
                if (client[pServerNr-1] == null) {
                    System.out.println("****************new client "+pServerNr+"*************************");
                    ClientBuilder builder = ClientBuilder.newBuilder();
                    if (this.IstHttps(pServerNr)) {
                        builder.sslContext(ConnectionFactory.getSslContextServerVerbund());
                    }
                    client[pServerNr-1] = builder.build();
                }
            }
        }
        CaBug.druckeLog("client (New version) "+ client, logDrucken, 10);
    }

    /**
     * Belegen WELoginVerify mit den allgemeingültigen Parametern Hinweis:
     * eingabeQuelle wird vom aufrufenden Programm belegt
     **/
    private void vorbereitenWELoginVerify(WELoginVerify pWELoginVerify) {
        pWELoginVerify.setMandant(clGlobalVar.mandant);
        pWELoginVerify.setHvJahr(clGlobalVar.hvJahr);
        pWELoginVerify.setHvNummer(clGlobalVar.hvNummer);
        pWELoginVerify.setDatenbereich(clGlobalVar.datenbereich);

        pWELoginVerify.setUser(1);
        pWELoginVerify.setuKennung(internerUser);
        pWELoginVerify.setuPasswort(internesPasswort);
        pWELoginVerify.setBenutzernr(clGlobalVar.benutzernr);
        pWELoginVerify.setArbeitsplatz(clGlobalVar.arbeitsplatz);
    }

    public String getPfad(String dienst, int pServerNr) {
        CaBug.druckeLog("Pfad="+paramServer.verbundServerAdresse[pServerNr-1] + dienst, logDrucken, 10);
        return paramServer.verbundServerAdresse[pServerNr-1] + dienst;
    }

    public boolean IstHttps(int pServerNr) {
        if (paramServer.verbundServerAdresse[pServerNr-1].startsWith("https")) {
            return true;
        }
        // if (CInjects.pfad.startsWith("https")){return true;}
        return false;
    }

    /**pServerNr = 1 bis 5*/
    public WEServerVerbundRC serverVerbund(WEServerVerbund weServerVerbund, int pServerNr) {
        ClientLog.ausgabeNl("**********serverVerbund");
        vorbereitenWELoginVerify(weServerVerbund.getWeLoginVerify());
        WEServerVerbundRC weServerVerbundRC = null;

        initialisiereVerbindungsOjekt(pServerNr);

//        if (this.IstHttps(pServerNr)) {
//            weServerVerbundRC = client[pServerNr-1].target(getPfad("serverVerbund", pServerNr)).request(MediaType.APPLICATION_JSON)
//                    .post(Entity.json(weServerVerbund), WEServerVerbundRC.class);
//        } else {
            try {
                CaBug.druckeLog("Vor Aufruf client", logDrucken, 10);
                weServerVerbundRC = client[pServerNr-1].target(getPfad("serverVerbund", pServerNr)).request(MediaType.APPLICATION_JSON)
                        .post(Entity.json(weServerVerbund), WEServerVerbundRC.class);
                CaBug.druckeLog("Nach Aufruf client", logDrucken, 10);
           } catch (Exception e2) {
                CaBug.drucke("Verbindung unterbrochen");
                e2.printStackTrace();
                weServerVerbundRC = new WEServerVerbundRC();
                weServerVerbundRC.rc = CaFehler.teVerbindungsabbruchWebService;
            }
//        }

        ClientLog.ausgabeNl(
                "rc=" + weServerVerbundRC.getRc() + CaFehler.getFehlertext(weServerVerbundRC.getRc(), 0));
        return weServerVerbundRC;
    }

}
