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
package de.meetingapps.meetingportal.meetComBlManaged;

import java.io.IOException;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComEclM.EclParamM;
import de.meetingapps.meetingportal.meetComHVParam.ParamServerStatic;
import de.meetingapps.meetingportal.meetComStub.WSClientServerVerbund;
import de.meetingapps.meetingportal.meetComWE.WELoginVerify;
import de.meetingapps.meetingportal.meetComWE.WEServerVerbund;
import de.meetingapps.meetingportal.meetComWE.WEServerVerbundRC;
import de.meetingapps.meetingportal.meetingSocket.BsClient;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

@RequestScoped
public class BlMTeilnehmerKommunikation {

    private @Inject EclParamM eclParamM;

    int logDrucken = 10;

    /**Test durchführen an den Server mit der Nummer pServerNr (1 bis 5)
     * 
     * Returnwerte: 
     * 2 = ausgewählter Server ist lokaler Server
     * 1 = ok
     * 
     * -1 = Server ist nicht aktiv
     * -2 = Kommunikationsfehler
     * */
    public int testServer(int pServerNr) {

        if (eclParamM.getBmServernummer() == pServerNr) {
            return 2;
        }
        if (eclParamM.getParamServer().verbundServerAktiv[pServerNr - 1] == false) {
            return -1;
        }

        WEServerVerbund weServerVerbund = new WEServerVerbund();
        weServerVerbund.funktion = 1;

        WELoginVerify weLoginVerify = new WELoginVerify(); /*Mandatennummer wird in WSClient immer gesetzt*/
        weServerVerbund.setWeLoginVerify(weLoginVerify);

        WSClientServerVerbund wsClientServerVerbund = new WSClientServerVerbund(eclParamM.getParamServer(),
                eclParamM.getClGlobalVar());

        WEServerVerbundRC weServerVerbundRC = wsClientServerVerbund.serverVerbund(weServerVerbund, pServerNr);

        if (weServerVerbundRC.rc != 1) {
            return -2;
        }
        return weServerVerbundRC.rcFachlicherRC;
    }

    public int wortmeldungAktionKommeInTestraum(String pLoginKennung, String pMandant) {
        for (int i = 1; i <= 5; i++) {
            if (eclParamM.getBmServernummer() == i) {
                fuehreAusWortmeldungAktionKommeInTestraum(pLoginKennung, pMandant);
            } else {
                if (eclParamM.getParamServer().verbundServerAktiv[i - 1] == true) {
                    WEServerVerbund weServerVerbund = new WEServerVerbund();
                    weServerVerbund.funktion = 2;
                    weServerVerbund.loginKennung = pLoginKennung;
                    weServerVerbund.mandant = pMandant;

                    WELoginVerify weLoginVerify = new WELoginVerify();
                    weServerVerbund.setWeLoginVerify(weLoginVerify);

                    WSClientServerVerbund wsClientServerVerbund = new WSClientServerVerbund(eclParamM.getParamServer(),
                            eclParamM.getClGlobalVar());

                    WEServerVerbundRC weServerVerbundRC = wsClientServerVerbund.serverVerbund(weServerVerbund, i);

                }
            }

        }
        return 1;
    }

    public int wortmeldungAktionVerlasseTestraum(String pLoginKennung, String pMandant) {
        for (int i = 1; i <= 5; i++) {
            if (eclParamM.getBmServernummer() == i) {
                fuehreAusWortmeldungAktionVerlasseTestraum(pLoginKennung, pMandant);
            } else {
                if (eclParamM.getParamServer().verbundServerAktiv[i - 1] == true) {
                    WEServerVerbund weServerVerbund = new WEServerVerbund();
                    weServerVerbund.funktion = 3;
                    weServerVerbund.loginKennung = pLoginKennung;
                    weServerVerbund.mandant = pMandant;

                    WELoginVerify weLoginVerify = new WELoginVerify();
                    weServerVerbund.setWeLoginVerify(weLoginVerify);

                    WSClientServerVerbund wsClientServerVerbund = new WSClientServerVerbund(eclParamM.getParamServer(),
                            eclParamM.getClGlobalVar());

                    WEServerVerbundRC weServerVerbundRC = wsClientServerVerbund.serverVerbund(weServerVerbund, i);

                }
            }

        }
        return 1;
    }

    public int wortmeldungAktionKommeInRederaum(String pLoginKennung, String pMandant) {
        for (int i = 1; i <= 5; i++) {
            if (eclParamM.getBmServernummer() == i) {
                fuehreAusWortmeldungAktionKommeInRederaum(pLoginKennung, pMandant);
            } else {
                if (eclParamM.getParamServer().verbundServerAktiv[i - 1] == true) {
                    WEServerVerbund weServerVerbund = new WEServerVerbund();
                    weServerVerbund.funktion = 4;
                    weServerVerbund.loginKennung = pLoginKennung;
                    weServerVerbund.mandant = pMandant;

                    WELoginVerify weLoginVerify = new WELoginVerify();
                    weServerVerbund.setWeLoginVerify(weLoginVerify);

                    WSClientServerVerbund wsClientServerVerbund = new WSClientServerVerbund(eclParamM.getParamServer(),
                            eclParamM.getClGlobalVar());

                    WEServerVerbundRC weServerVerbundRC = wsClientServerVerbund.serverVerbund(weServerVerbund, i);

                }
            }

        }
        return 1;
    }

    public int wortmeldungAktionVerlasseRederaum(String pLoginKennung, String pMandant) {
        for (int i = 1; i <= 5; i++) {
            if (eclParamM.getBmServernummer() == i) {
                fuehreAusWortmeldungAktionVerlasseRederaum(pLoginKennung, pMandant);
            } else {
                if (eclParamM.getParamServer().verbundServerAktiv[i - 1] == true) {
                    WEServerVerbund weServerVerbund = new WEServerVerbund();
                    weServerVerbund.funktion = 5;
                    weServerVerbund.loginKennung = pLoginKennung;
                    weServerVerbund.mandant = pMandant;

                    WELoginVerify weLoginVerify = new WELoginVerify();
                    weServerVerbund.setWeLoginVerify(weLoginVerify);

                    WSClientServerVerbund wsClientServerVerbund = new WSClientServerVerbund(eclParamM.getParamServer(),
                            eclParamM.getClGlobalVar());

                    WEServerVerbundRC weServerVerbundRC = wsClientServerVerbund.serverVerbund(weServerVerbund, i);

                }
            }

        }
        return 1;
    }

    public int wortmeldungAktionJetztSprechen(String pLoginKennung, String pMandant) {
        for (int i = 1; i <= 5; i++) {
            if (eclParamM.getBmServernummer() == i) {
                fuehreAusWortmeldungAktionJetztSprechen(pLoginKennung, pMandant);
            } else {
                if (eclParamM.getParamServer().verbundServerAktiv[i - 1] == true) {
                    WEServerVerbund weServerVerbund = new WEServerVerbund();
                    weServerVerbund.funktion = 6;
                    weServerVerbund.loginKennung = pLoginKennung;
                    weServerVerbund.mandant = pMandant;

                    WELoginVerify weLoginVerify = new WELoginVerify();
                    weServerVerbund.setWeLoginVerify(weLoginVerify);

                    WSClientServerVerbund wsClientServerVerbund = new WSClientServerVerbund(eclParamM.getParamServer(),
                            eclParamM.getClGlobalVar());

                    WEServerVerbundRC weServerVerbundRC = wsClientServerVerbund.serverVerbund(weServerVerbund, i);

                }
            }

        }
        return 1;
    }

    public int wortmeldungAktionVerlasseTestraumNichtOK(String pLoginKennung, String pMandant) {
        for (int i = 1; i <= 5; i++) {
            if (eclParamM.getBmServernummer() == i) {
                fuehreAusWortmeldungAktionVerlasseTestraumNichtOK(pLoginKennung, pMandant);
            } else {
                if (eclParamM.getParamServer().verbundServerAktiv[i - 1] == true) {
                    WEServerVerbund weServerVerbund = new WEServerVerbund();
                    weServerVerbund.funktion = 7;
                    weServerVerbund.loginKennung = pLoginKennung;
                    weServerVerbund.mandant = pMandant;

                    WELoginVerify weLoginVerify = new WELoginVerify();
                    weServerVerbund.setWeLoginVerify(weLoginVerify);

                    WSClientServerVerbund wsClientServerVerbund = new WSClientServerVerbund(eclParamM.getParamServer(),
                            eclParamM.getClGlobalVar());

                    WEServerVerbundRC weServerVerbundRC = wsClientServerVerbund.serverVerbund(weServerVerbund, i);

                }
            }

        }
        return 1;
    }

    public int wortmeldungAktionAktualisiereVL(String pLoginKennung, String pMandant) {
        for (int i = 1; i <= 5; i++) {
            if (eclParamM.getBmServernummer() == i) {
                fuehreAuswortmeldungAktionAktualisiereVL(pLoginKennung, pMandant);
            } else {
                if (eclParamM.getParamServer().verbundServerAktiv[i - 1] == true) {
                    WEServerVerbund weServerVerbund = new WEServerVerbund();
                    weServerVerbund.funktion = 8;
                    weServerVerbund.loginKennung = pLoginKennung;
                    weServerVerbund.mandant = pMandant;

                    WELoginVerify weLoginVerify = new WELoginVerify();
                    weServerVerbund.setWeLoginVerify(weLoginVerify);

                    WSClientServerVerbund wsClientServerVerbund = new WSClientServerVerbund(eclParamM.getParamServer(),
                            eclParamM.getClGlobalVar());

                    WEServerVerbundRC weServerVerbundRC = wsClientServerVerbund.serverVerbund(weServerVerbund, i);

                }
            }

        }
        return 1;
    }

    public int wortmeldungAktionTestNichtErreicht(String pLoginKennung, String pMandant) {
        for (int i = 1; i <= 5; i++) {
            if (eclParamM.getBmServernummer() == i) {
                fuehreAuswortmeldungAktionTestNichtErreicht(pLoginKennung, pMandant);
            } else {
                if (eclParamM.getParamServer().verbundServerAktiv[i - 1] == true) {
                    WEServerVerbund weServerVerbund = new WEServerVerbund();
                    weServerVerbund.funktion = 9;
                    weServerVerbund.loginKennung = pLoginKennung;
                    weServerVerbund.mandant = pMandant;

                    WELoginVerify weLoginVerify = new WELoginVerify();
                    weServerVerbund.setWeLoginVerify(weLoginVerify);

                    WSClientServerVerbund wsClientServerVerbund = new WSClientServerVerbund(eclParamM.getParamServer(),
                            eclParamM.getClGlobalVar());

                    WEServerVerbundRC weServerVerbundRC = wsClientServerVerbund.serverVerbund(weServerVerbund, i);

                }
            }

        }
        return 1;
    }

    public int wortmeldungAktionRedeNichtErreicht(String pLoginKennung, String pMandant) {
        for (int i = 1; i <= 5; i++) {
            if (eclParamM.getBmServernummer() == i) {
                fuehreAuswortmeldungAktionRedeNichtErreicht(pLoginKennung, pMandant);
            } else {
                if (eclParamM.getParamServer().verbundServerAktiv[i - 1] == true) {
                    WEServerVerbund weServerVerbund = new WEServerVerbund();
                    weServerVerbund.funktion = 10;
                    weServerVerbund.loginKennung = pLoginKennung;
                    weServerVerbund.mandant = pMandant;

                    WELoginVerify weLoginVerify = new WELoginVerify();
                    weServerVerbund.setWeLoginVerify(weLoginVerify);

                    WSClientServerVerbund wsClientServerVerbund = new WSClientServerVerbund(eclParamM.getParamServer(),
                            eclParamM.getClGlobalVar());

                    WEServerVerbundRC weServerVerbundRC = wsClientServerVerbund.serverVerbund(weServerVerbund, i);

                }
            }

        }
        return 1;
    }

    /**
     * Funktion 2
     */
    public int fuehreAusWortmeldungAktionKommeInTestraum(String pLoginKennung, String pMandant) {

        return sendeSocketBefehl(setzeCommandStringFuerSocket("*WortmeldungAktionTS*", pLoginKennung, pMandant));
    }

    /**
     * Funktion 3
     */
    public int fuehreAusWortmeldungAktionVerlasseTestraum(String pLoginKennung, String pMandant) {

        return sendeSocketBefehl(setzeCommandStringFuerSocket("*WortmeldungAktionTV*", pLoginKennung, pMandant));
    }

    /**
     * Funktion 4
     */
    public int fuehreAusWortmeldungAktionKommeInRederaum(String pLoginKennung, String pMandant) {

        return sendeSocketBefehl(setzeCommandStringFuerSocket("*WortmeldungAktionRS*", pLoginKennung, pMandant));
    }

    /**
     * Funktion 5
     */
    public int fuehreAusWortmeldungAktionVerlasseRederaum(String pLoginKennung, String pMandant) {
        return sendeSocketBefehl(setzeCommandStringFuerSocket("*WortmeldungAktionRV*", pLoginKennung, pMandant));
    }

    /**
     * Funktion 6
     */
    public int fuehreAusWortmeldungAktionJetztSprechen(String pLoginKennung, String pMandant) {

        return sendeSocketBefehl(setzeCommandStringFuerSocket("*WortmeldungAktionSP*", pLoginKennung, pMandant));
    }

    /**
     * Funktion 7
     */
    public int fuehreAusWortmeldungAktionVerlasseTestraumNichtOK(String pLoginKennung, String pMandant) {

        return sendeSocketBefehl(setzeCommandStringFuerSocket("*WortmeldungAktionTVNOK*", pLoginKennung, pMandant));
    }

    /**
     * Funktion 8
     */
    public int fuehreAuswortmeldungAktionAktualisiereVL(String pLoginKennung, String pMandant) {

        return sendeSocketBefehl(
                setzeCommandStringFuerSocket("*WortmeldungAktionAktualisiereVL*", pLoginKennung, pMandant));
    }

    /**
     * Funktion 9
     */
    public int fuehreAuswortmeldungAktionTestNichtErreicht(String pLoginKennung, String pMandant) {

        return sendeSocketBefehl(
                setzeCommandStringFuerSocket("*WortmeldungAktionTestNichtErreicht*", pLoginKennung, pMandant));
    }

    /**
     * Funktion 10
     */
    public int fuehreAuswortmeldungAktionRedeNichtErreicht(String pLoginKennung, String pMandant) {

        return sendeSocketBefehl(
                setzeCommandStringFuerSocket("*WortmeldungAktionRedeNichtErreicht*", pLoginKennung, pMandant));
    }

    private String setzeCommandStringFuerSocket(String befehl, String pLoginKennung, String pMandant) {
        return befehl + "::" + pMandant + "::" + pLoginKennung;
    }

    private int sendeSocketBefehl(String befehl) {
        try {
            CaBug.druckeLog("A", logDrucken, 10);
            /*INFO Umstellen Websockets*/
            BsClient bsClient = null;
            bsClient = new BsClient(ParamServerStatic.webSocketsLocalHost);
            CaBug.druckeLog("B", logDrucken, 10);
            bsClient.senden(befehl);
            CaBug.druckeLog("C", logDrucken, 10);
            bsClient.disconnectFromWebSocket();
            CaBug.druckeLog("D", logDrucken, 10);
            return 1;
        } catch (IOException e) {
            CaBug.druckeLog("001", logDrucken, 10);
            return -1;
        }
//        return 1;
    }

}
