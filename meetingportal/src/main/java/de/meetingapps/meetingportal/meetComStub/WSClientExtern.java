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
import de.meetingapps.meetingportal.meetComWE.WEExternLogin;
import de.meetingapps.meetingportal.meetComWE.WEFragenResponse;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.MediaType;


public class WSClientExtern {

    private static volatile Client client = null;

    /** Initialisieren client */
    private void initialisiereVerbindungsOjekt() {
        if (client == null) {
            synchronized (this) {
                // We are using here a variant of the Double-Check-Idiom
                if (client == null) {
                    ClientBuilder builder = ClientBuilder.newBuilder();
                    if (this.IstHttps()) {
                        builder.sslContext(ConnectionFactory.getSslContext());
                    }
                    client = builder.build();
                }
            }
        }
        // TODO remove temporary System.out asap
        System.out.printf(">>> WsClientExtern (New version) >>> Using client %s%n", client);
    }


    private String internesPasswort = "todo";

    /**
     * Belegen WELoginVerify mit den allgemeingültigen Parametern Hinweis:
     * eingabeQuelle wird vom aufrufenden Programm belegt
     **/
    private void vorbereitenWEExternLogin(WEExternLogin weExternLogin) {
        weExternLogin.mandant=Integer.toString(ParamS.clGlobalVar.mandant);
        weExternLogin.passwort=internesPasswort;
    }



    public String getPfad(String dienst) {
        return ParamInterneKommunikation.webServicePfadZurAuswahlExtern[ParamS.clGlobalVar.webServicePfadNr] + dienst;
    }

    public boolean IstHttps() {
        if (ParamInterneKommunikation.webServicePfadZurAuswahlExtern[ParamS.clGlobalVar.webServicePfadNr].startsWith("https")) {
            return true;
        }
        // if (CInjects.pfad.startsWith("https")){return true;}
        return false;
    }


    public WEFragenResponse getFragen() {
        WEExternLogin weExternLogin=new WEExternLogin();
        vorbereitenWEExternLogin(weExternLogin);

        ClientLog.ausgabeNl("**********getFragen");

        initialisiereVerbindungsOjekt();
        WEFragenResponse weFragenResponse=new WEFragenResponse();
        if (this.IstHttps()) {
            weFragenResponse = client.target(getPfad("getFragen")).request(MediaType.APPLICATION_JSON)
                    .post(Entity.json(weExternLogin), WEFragenResponse.class);
            // weLoginCheckRC.rc = weLoginCheckRC.eclTeilnehmerLoginM.getRc(); //Noch
            // Eliminieren!
        } else {
            try {
                weFragenResponse = client.target(getPfad("getFragen")).request(MediaType.APPLICATION_JSON)
                        .post(Entity.json(weExternLogin), WEFragenResponse.class);
                // weLoginCheckRC.rc = weLoginCheckRC.eclTeilnehmerLoginM.getRc(); //Noch
                // Eliminieren!
            } catch (Exception e2) {
                CaBug.drucke("Verbindung unterbrochen");
                weFragenResponse = new WEFragenResponse();
                weFragenResponse.statusCode = CaFehler.teVerbindungsabbruchWebService;
            }
        }

        ClientLog.ausgabeNl("rc=" + weFragenResponse.statusCode + " "+weFragenResponse.responseText);
        return weFragenResponse;
    }

}
