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
import de.meetingapps.meetingportal.meetComHVParam.ParamInterneKommunikation;
import de.meetingapps.meetingportal.meetComHVParam.ParamS;
import de.meetingapps.meetingportal.meetComWE.WEHybridMitglieder;
import de.meetingapps.meetingportal.meetComWE.WEHybridMitgliederRC;
import de.meetingapps.meetingportal.meetComWE.WELoginVerify;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.MediaType;

public class WSClientOnline {

    int logDrucken=10;

    private static /*volatile*/ Client client = null;

    private String internerUser = "todo";
    private String internesPasswort = "todo";

    /** Initialisieren client */
    private void initialisiereVerbindungsOjekt() {
        if (client == null) {
            synchronized (this) {
                // We are using here a variant of the Double-Check-Idiom
                if (client == null) {
                    System.out.println("****************new client*************************");
                    ClientBuilder builder = ClientBuilder.newBuilder();
                    if (this.IstHttps()) {
                        builder.sslContext(ConnectionFactory.getSslContextOnline());
                    }
                    client = builder.build();
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
        pWELoginVerify.setMandant(ParamS.clGlobalVar.mandant);
        pWELoginVerify.setHvJahr(ParamS.clGlobalVar.hvJahr);
        pWELoginVerify.setHvNummer(ParamS.clGlobalVar.hvNummer);
        pWELoginVerify.setDatenbereich(ParamS.clGlobalVar.datenbereich);

        pWELoginVerify.setUser(1);
        pWELoginVerify.setuKennung(internerUser);
        pWELoginVerify.setuPasswort(internesPasswort);
        pWELoginVerify.setBenutzernr(ParamS.clGlobalVar.benutzernr);
        pWELoginVerify.setArbeitsplatz(ParamS.clGlobalVar.arbeitsplatz);
    }

    public String getPfad(String dienst) {
        return ParamInterneKommunikation.webServicePfadZurAuswahl[ParamS.clGlobalVar.onlineWebServicePfadNr] + dienst;
    }

    public boolean IstHttps() {
        if (ParamInterneKommunikation.webServicePfadZurAuswahl[ParamS.clGlobalVar.onlineWebServicePfadNr].startsWith("https")) {
            return true;
        }
        // if (CInjects.pfad.startsWith("https")){return true;}
        return false;
    }

    public WEHybridMitgliederRC hybridMitglieder(WEHybridMitglieder weHybridMitglieder) {
        ClientLog.ausgabeNl("**********hybridMitglieder");
        vorbereitenWELoginVerify(weHybridMitglieder.getWeLoginVerify());
        WEHybridMitgliederRC weHybridMitgliederRC = null;

        initialisiereVerbindungsOjekt();

        if (this.IstHttps()) {
            weHybridMitgliederRC = client.target(getPfad("hybridMitglieder")).request(MediaType.APPLICATION_JSON)
                    .post(Entity.json(weHybridMitglieder), WEHybridMitgliederRC.class);
        } else {
            try {
                weHybridMitgliederRC = client.target(getPfad("hybridMitglieder")).request(MediaType.APPLICATION_JSON)
                        .post(Entity.json(weHybridMitglieder), WEHybridMitgliederRC.class);
            } catch (Exception e2) {
                CaBug.drucke("Verbindung unterbrochen");
                e2.printStackTrace();
                weHybridMitgliederRC = new WEHybridMitgliederRC();
                weHybridMitgliederRC.rc = CaFehler.teVerbindungsabbruchWebService;
            }
        }

        ClientLog.ausgabeNl(
                "rc=" + weHybridMitgliederRC.getRc() + CaFehler.getFehlertext(weHybridMitgliederRC.getRc(), 0));
        return weHybridMitgliederRC;
    }

}
