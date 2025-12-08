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
import de.meetingapps.meetingportal.meetComEclM.EclTeilnehmerLoginM;
import de.meetingapps.meetingportal.meetComEntities.EclTLoginDatenM;
import de.meetingapps.meetingportal.meetComHVParam.ParamInterneKommunikation;
import de.meetingapps.meetingportal.meetComHVParam.ParamS;
import de.meetingapps.meetingportal.meetComWE.*;
import de.meetingapps.meetingportal.meetingVote.WEStimmabgabe;
import de.meetingapps.meetingportal.meetingVote.WEStimmabgabeRC;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.MediaType;

/*TODO _WebService Konsolidierung: immer wieder selbe Verbindung verwenden, auch bei Programmwechsel / Funktionswechsel!*/

public class WSClient {

    private static /*volatile*/ Client client = null;

    /** Initialisieren client */
    private void initialisiereVerbindungsOjekt() {
        if (client == null) {
            synchronized (this) {
                // We are using here a variant of the Double-Check-Idiom
                if (client == null) {
                    CaBug.out("****************new client*************************");
                    ClientBuilder builder = ClientBuilder.newBuilder();
                    if (this.IstHttps()) {
                        builder.sslContext(ConnectionFactory.getSslContext());
                    }
                    client = builder.build();
                }
            }
        }
        CaBug.out(">>> WsClient (New version) >>> Using client "+ client.toString());
    }

    private void initialisiereVerbindungsOjektVote() {
        initialisiereVerbindungsOjekt();
    }

    private String internerUser = "todo";
    private String internesPasswort = "todo";

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

    @Deprecated
    public WELoginVerify fuelleLoginVerify(EclTeilnehmerLoginM pEclTeilnehmerLoginM) {
        WELoginVerify lWELoginVerify = new WELoginVerify();

        lWELoginVerify.setAnmeldeKennungArt(pEclTeilnehmerLoginM.getAnmeldeKennungArt());
        lWELoginVerify.setAnmeldeIdentAktienregister(pEclTeilnehmerLoginM.getAnmeldeIdentAktienregister());
        lWELoginVerify.setAnmeldeIdentPersonenNatJur(pEclTeilnehmerLoginM.getAnmeldeIdentPersonenNatJur());
        return lWELoginVerify;
    }

    public WELoginVerify fuelleLoginVerify(EclTLoginDatenM eclTLoginDatenM) {
        WELoginVerify lWELoginVerify = new WELoginVerify();

        lWELoginVerify.setKennung(eclTLoginDatenM.eclLoginDaten.loginKennung);
        return lWELoginVerify;
    }

    /**
     * Belegen WELogin mit den allgemeingültigen Parametern Hinweis: eingabeQuelle
     * wird vom aufrufenden Programm belegt
     **/
    private void vvorbereitenWELogin(WELogin pWELogin) {
        pWELogin.setMandant(ParamS.clGlobalVar.mandant);
        pWELogin.setHvJahr(ParamS.clGlobalVar.hvJahr);
        pWELogin.setHvNummer(ParamS.clGlobalVar.hvNummer);
        pWELogin.setDatenbereich(ParamS.clGlobalVar.datenbereich);

        // hvNr wird noch nicht verwendet
        pWELogin.setUser(1);
        pWELogin.setuKennung(internerUser);
        pWELogin.setuPasswort(internesPasswort);
        pWELogin.setBenutzernr(ParamS.clGlobalVar.benutzernr);
        pWELogin.setArbeitsplatz(ParamS.clGlobalVar.arbeitsplatz);
    }

    public String getPfad(String dienst) {
        return ParamInterneKommunikation.webServicePfadZurAuswahl[ParamS.clGlobalVar.webServicePfadNr] + dienst;
    }

    public boolean IstHttps() {
        if (ParamInterneKommunikation.webServicePfadZurAuswahl[ParamS.clGlobalVar.webServicePfadNr].startsWith("https")) {
            return true;
        }
        // if (CInjects.pfad.startsWith("https")){return true;}
        return false;
    }

    public void initClient() {
        client=null;
    }

    // @Deprecated
    // public EclTeilnehmerLoginM loginCheck(WELogin pWELogin)
    // {
    // /*Wenn eliminiert, dann auch aus EclTeilnehmerLoginM noch die Variable rc
    // entfernen*/
    // WELoginCheck weLoginCheck=new WELoginCheck();
    // weLoginCheck.weLogin=pWELogin;
    // WELoginCheckRC weLoginCheckRC=loginCheck(weLoginCheck);
    // return weLoginCheckRC.eclTeilnehmerLoginM;
    // }
    //

    public WEVersionsabgleichRC versionsabgleich(WEVersionsabgleich pWEVersionsabgleich) {

        ClientLog.ausgabeNl("**********versionsabgleich");

        WEVersionsabgleichRC weVersionsabgleichRC = null;

        initialisiereVerbindungsOjekt();
        if (this.IstHttps()) {
            weVersionsabgleichRC = client.target(getPfad("versionsabgleich")).request(MediaType.APPLICATION_JSON)
                    .post(Entity.json(pWEVersionsabgleich), WEVersionsabgleichRC.class);
        } else {
            try {
                weVersionsabgleichRC = client.target(getPfad("versionsabgleich")).request(MediaType.APPLICATION_JSON)
                        .post(Entity.json(pWEVersionsabgleich), WEVersionsabgleichRC.class);
            } catch (Exception e2) {
                CaBug.drucke("Verbindung unterbrochen");
                weVersionsabgleichRC = new WEVersionsabgleichRC();
                weVersionsabgleichRC.rc = CaFehler.teVerbindungsabbruchWebService;
            }
        }

        ClientLog.ausgabeNl("rc=" + weVersionsabgleichRC.getRc() + CaFehler.getFehlertext(weVersionsabgleichRC.getRc(), 0));
        return weVersionsabgleichRC;
    }

    public WELoginCheckRC loginCheck(WELoginCheck pWELoginCheck) {

        ClientLog.ausgabeNl("**********loginCheck");
        // vorbereitenWELogin(pWELoginCheck.weLogin);
        pWELoginCheck.weLoginVerify = new WELoginVerify();
        vorbereitenWELoginVerify(pWELoginCheck.weLoginVerify);
        WELoginCheckRC weLoginCheckRC = null;

        initialisiereVerbindungsOjekt();
        if (this.IstHttps()) {
            weLoginCheckRC = client.target(getPfad("loginCheck")).request(MediaType.APPLICATION_JSON)
                    .post(Entity.json(pWELoginCheck), WELoginCheckRC.class);
            // weLoginCheckRC.rc = weLoginCheckRC.eclTeilnehmerLoginM.getRc(); //Noch
            // Eliminieren!
        } else {
            try {
                weLoginCheckRC = client.target(getPfad("loginCheck")).request(MediaType.APPLICATION_JSON)
                        .post(Entity.json(pWELoginCheck), WELoginCheckRC.class);
                // weLoginCheckRC.rc = weLoginCheckRC.eclTeilnehmerLoginM.getRc(); //Noch
                // Eliminieren!
            } catch (Exception e2) {
                CaBug.drucke("Verbindung unterbrochen");
                weLoginCheckRC = new WELoginCheckRC();
                weLoginCheckRC.rc = CaFehler.teVerbindungsabbruchWebService;
            }
        }

        ClientLog.ausgabeNl("rc=" + weLoginCheckRC.getRc() + CaFehler.getFehlertext(weLoginCheckRC.getRc(), 0));
        return weLoginCheckRC;
    }

    /*
     * hvDatenGet nicht mehr verwendent - liefert immer nur Standard-Mandanten-Daten
     */
    // public WEHVDatenRC hvDatenGet(WELoginVerify pWELoginVerify)
    // {
    // ClientLog.ausgabeNl("**********hvDatenGet");
    // vorbereitenWELoginVerify(pWELoginVerify);
    // WEHVDatenRC lWEHVDatenRC=null;
    //
    // initialisiereVerbindungsOjekt();
    // System.out.println(getPfad("hvDatenGet"));
    // if (this.IstHttps()){
    //
    // lWEHVDatenRC =
    // client.target(getPfad("hvDatenGet")).request(MediaType.APPLICATION_JSON).post(Entity.json(pWELoginVerify),
    // WEHVDatenRC.class);
    // }
    // else{
    // try {
    // lWEHVDatenRC=
    // client.target(getPfad("hvDatenGet"))
    // .request(MediaType.APPLICATION_JSON)
    // .post(Entity.json(pWELoginVerify), WEHVDatenRC.class);
    // } catch (Exception e2){
    // CaBug.drucke("Verbindung unterbrochen");
    // lWEHVDatenRC=new WEHVDatenRC();
    // lWEHVDatenRC.rc=CaFehler.teVerbindungsabbruchWebService;
    // }
    // }
    //
    // ClientLog.ausgabeNl("rc="+lWEHVDatenRC.getRc()+CaFehler.getFehlertext(lWEHVDatenRC.getRc(),
    // 0));
    // return lWEHVDatenRC;
    // }

    /*Wird ersetzt durch teilnehmerStatusClientGet*/
    @Deprecated
    public WETeilnehmerStatusGetRC teilnehmerStatusGet(WELoginVerify pWELoginVerify) {
        ClientLog.ausgabeNl("**********teilnehmerStatusGet");
        vorbereitenWELoginVerify(pWELoginVerify);
        WETeilnehmerStatusGetRC lWETeilnehmerStatusGetRC = null;

        initialisiereVerbindungsOjekt();
        if (this.IstHttps()) {
            lWETeilnehmerStatusGetRC = client.target(getPfad("teilnehmerStatusGet")).request(MediaType.APPLICATION_JSON)
                    .post(Entity.json(pWELoginVerify), WETeilnehmerStatusGetRC.class);
        } else {
            try {
                lWETeilnehmerStatusGetRC = client.target(getPfad("teilnehmerStatusGet"))
                        .request(MediaType.APPLICATION_JSON)
                        .post(Entity.json(pWELoginVerify), WETeilnehmerStatusGetRC.class);
            } catch (Exception e2) {
                CaBug.drucke("Verbindung unterbrochen");
                lWETeilnehmerStatusGetRC = new WETeilnehmerStatusGetRC();
                lWETeilnehmerStatusGetRC.rc = CaFehler.teVerbindungsabbruchWebService;
            }
        }

        ClientLog.ausgabeNl(
                "rc=" + lWETeilnehmerStatusGetRC.getRc() + CaFehler.getFehlertext(lWETeilnehmerStatusGetRC.getRc(), 0));
        return lWETeilnehmerStatusGetRC;
    }

    public WETeilnehmerStatusClientGetRC teilnehmerStatusClientGet(WETeilnehmerStatusClientGet pWETeilnehmerStatusClientGet) {
        ClientLog.ausgabeNl("**********teilnehmerStatusClientGet");
        vorbereitenWELoginVerify(pWETeilnehmerStatusClientGet.getWeLoginVerify());
        WETeilnehmerStatusClientGetRC lWETeilnehmerStatusClientGetRC = null;

        initialisiereVerbindungsOjekt();
        if (this.IstHttps()) {
            lWETeilnehmerStatusClientGetRC = client.target(getPfad("teilnehmerStatusClientGet")).request(MediaType.APPLICATION_JSON)
                    .post(Entity.json(pWETeilnehmerStatusClientGet), WETeilnehmerStatusClientGetRC.class);
        } else {
            try {
                lWETeilnehmerStatusClientGetRC = client.target(getPfad("teilnehmerStatusClientGet"))
                        .request(MediaType.APPLICATION_JSON)
                        .post(Entity.json(pWETeilnehmerStatusClientGet), WETeilnehmerStatusClientGetRC.class);
            } catch (Exception e2) {
                CaBug.drucke("Verbindung unterbrochen");
                lWETeilnehmerStatusClientGetRC = new WETeilnehmerStatusClientGetRC();
                lWETeilnehmerStatusClientGetRC.rc = CaFehler.teVerbindungsabbruchWebService;
            }
        }

        ClientLog.ausgabeNl(
                "rc=" + lWETeilnehmerStatusClientGetRC.getRc() + CaFehler.getFehlertext(lWETeilnehmerStatusClientGetRC.getRc(), 0));
        return lWETeilnehmerStatusClientGetRC;
    }

    public WEAnmeldungRC anmeldung(WEAnmeldung pWEAnmeldung) {
        ClientLog.ausgabeNl("**********anmeldung");
        vorbereitenWELoginVerify(pWEAnmeldung.getWeLoginVerify());

        WEAnmeldungRC lWEAnmeldungRC = null;

        initialisiereVerbindungsOjekt();
        if (this.IstHttps()) {
            lWEAnmeldungRC = client.target(getPfad("anmeldung")).request(MediaType.APPLICATION_JSON)
                    .post(Entity.json(pWEAnmeldung), WEAnmeldungRC.class);
        } else {
            try {
                lWEAnmeldungRC = client.target(getPfad("anmeldung")).request(MediaType.APPLICATION_JSON)
                        .post(Entity.json(pWEAnmeldung), WEAnmeldungRC.class);
            } catch (Exception e2) {
                CaBug.drucke("Verbindung unterbrochen");
                lWEAnmeldungRC = new WEAnmeldungRC();
                lWEAnmeldungRC.rc = CaFehler.teVerbindungsabbruchWebService;
            }
        }

        ClientLog.ausgabeNl("rc=" + lWEAnmeldungRC.getRc() + CaFehler.getFehlertext(lWEAnmeldungRC.getRc(), 0));
        return lWEAnmeldungRC;
    }

    public WEEintrittskarteRC eintrittskarte(WEEintrittskarte pWEEintrittskarte) {
        ClientLog.ausgabeNl("**********eintrittskarte");
        vorbereitenWELoginVerify(pWEEintrittskarte.getWeLoginVerify());
        WEEintrittskarteRC lWEEintrittskarteRC = null;

        initialisiereVerbindungsOjekt();
        if (this.IstHttps()) {
            lWEEintrittskarteRC = client.target(getPfad("eintrittskarte")).request(MediaType.APPLICATION_JSON)
                    .post(Entity.json(pWEEintrittskarte), WEEintrittskarteRC.class);
        } else {
            try {
                lWEEintrittskarteRC = client.target(getPfad("eintrittskarte")).request(MediaType.APPLICATION_JSON)
                        .post(Entity.json(pWEEintrittskarte), WEEintrittskarteRC.class);
            } catch (Exception e2) {
                CaBug.drucke("Verbindung unterbrochen");
                lWEEintrittskarteRC = new WEEintrittskarteRC();
                lWEEintrittskarteRC.rc = CaFehler.teVerbindungsabbruchWebService;
            }
        }

        ClientLog.ausgabeNl(
                "rc=" + lWEEintrittskarteRC.getRc() + CaFehler.getFehlertext(lWEEintrittskarteRC.getRc(), 0));
        return lWEEintrittskarteRC;
    }

    public WEEintrittskartePrintGetRC eintrittskartePrintGet(WEEintrittskartePrintGet weEintrittskartePrintGet) {
        ClientLog.ausgabeNl("**********eintrittskartePrintGet");
        vorbereitenWELoginVerify(weEintrittskartePrintGet.getWeLoginVerify());
        WEEintrittskartePrintGetRC lWEEintrittskartePrintGetRC = null;

        initialisiereVerbindungsOjekt();
        if (this.IstHttps()) {
            lWEEintrittskartePrintGetRC = client.target(getPfad("eintrittskartePrintGet"))
                    .request(MediaType.APPLICATION_JSON)
                    .post(Entity.json(weEintrittskartePrintGet), WEEintrittskartePrintGetRC.class);
        } else {
            try {
                lWEEintrittskartePrintGetRC = client.target(getPfad("eintrittskartePrintGet"))
                        .request(MediaType.APPLICATION_JSON)
                        .post(Entity.json(weEintrittskartePrintGet), WEEintrittskartePrintGetRC.class);
            } catch (Exception e2) {
                CaBug.drucke("Verbindung unterbrochen");
                lWEEintrittskartePrintGetRC = new WEEintrittskartePrintGetRC();
                lWEEintrittskartePrintGetRC.rc = CaFehler.teVerbindungsabbruchWebService;
            }
        }

        ClientLog.ausgabeNl("rc=" + lWEEintrittskartePrintGetRC.getRc()
                + CaFehler.getFehlertext(lWEEintrittskartePrintGetRC.getRc(), 0));
        return lWEEintrittskartePrintGetRC;
    }

    public WEAbstimmungsListeGetRC abstimmungsListeGet(WELoginVerify pWELoginVerify) {

        ClientLog.ausgabeNl("**********abstimmungsListeGet");
        vorbereitenWELoginVerify(pWELoginVerify);
        WEAbstimmungsListeGetRC lWEAbstimmungsListeGetRC = null;

        initialisiereVerbindungsOjekt();
        if (this.IstHttps()) {
            lWEAbstimmungsListeGetRC = client.target(getPfad("abstimmungsListeGet")).request(MediaType.APPLICATION_JSON)
                    .post(Entity.json(pWELoginVerify), WEAbstimmungsListeGetRC.class);
        } else {
            try {
                lWEAbstimmungsListeGetRC = client.target(getPfad("abstimmungsListeGet"))
                        .request(MediaType.APPLICATION_JSON)
                        .post(Entity.json(pWELoginVerify), WEAbstimmungsListeGetRC.class);
            } catch (Exception e2) {
                CaBug.drucke("Verbindung unterbrochen");
                lWEAbstimmungsListeGetRC = new WEAbstimmungsListeGetRC();
                lWEAbstimmungsListeGetRC.rc = CaFehler.teVerbindungsabbruchWebService;
            }
        }

        ClientLog.ausgabeNl(
                "rc=" + lWEAbstimmungsListeGetRC.getRc() + CaFehler.getFehlertext(lWEAbstimmungsListeGetRC.getRc(), 0));
        return lWEAbstimmungsListeGetRC;
    }

    public WEStimmkartenblockGetRC stimmkartenBlockGet(WELoginVerify pWELoginVerify) {

        ClientLog.ausgabeNl("**********stimmkartenBlockGet");
        vorbereitenWELoginVerify(pWELoginVerify);
        WEStimmkartenblockGetRC lWEStimmkartenblockGetRC = null;

        initialisiereVerbindungsOjekt();
        if (this.IstHttps()) {
            lWEStimmkartenblockGetRC = client.target(getPfad("stimmkartenBlockGet")).request(MediaType.APPLICATION_JSON)
                    .post(Entity.json(pWELoginVerify), WEStimmkartenblockGetRC.class);
        } else {
            try {
                lWEStimmkartenblockGetRC = client.target(getPfad("stimmkartenBlockGet"))
                        .request(MediaType.APPLICATION_JSON)
                        .post(Entity.json(pWELoginVerify), WEStimmkartenblockGetRC.class);
            } catch (Exception e2) {
                CaBug.drucke("Verbindung unterbrochen");
                lWEStimmkartenblockGetRC = new WEStimmkartenblockGetRC();
                lWEStimmkartenblockGetRC.rc = CaFehler.teVerbindungsabbruchWebService;
            }
        }

        ClientLog.ausgabeNl(
                "rc=" + lWEStimmkartenblockGetRC.getRc() + CaFehler.getFehlertext(lWEStimmkartenblockGetRC.getRc(), 0));
        return lWEStimmkartenblockGetRC;
    }

    public WEWeisungErteilenRC weisungErteilen(WEWeisungErteilen weWeisungErteilen) {
        ClientLog.ausgabeNl("**********weisungErteilen");
        vorbereitenWELoginVerify(weWeisungErteilen.getWeLoginVerify());
        WEWeisungErteilenRC lWEWeisungErteilenRC = null;

        initialisiereVerbindungsOjekt();
        if (this.IstHttps()) {
            lWEWeisungErteilenRC = client.target(getPfad("weisungErteilen")).request(MediaType.APPLICATION_JSON)
                    .post(Entity.json(weWeisungErteilen), WEWeisungErteilenRC.class);
        } else {
            try {
                lWEWeisungErteilenRC = client.target(getPfad("weisungErteilen")).request(MediaType.APPLICATION_JSON)
                        .post(Entity.json(weWeisungErteilen), WEWeisungErteilenRC.class);
            } catch (Exception e2) {
                CaBug.drucke("Verbindung unterbrochen");
                lWEWeisungErteilenRC = new WEWeisungErteilenRC();
                lWEWeisungErteilenRC.rc = CaFehler.teVerbindungsabbruchWebService;
                System.out.println(e2);
            }
        }

        ClientLog.ausgabeNl(
                "rc=" + lWEWeisungErteilenRC.getRc() + CaFehler.getFehlertext(lWEWeisungErteilenRC.getRc(), 0));
        return lWEWeisungErteilenRC;

    }

    public WEKIAVListeGetRC kiavListeGet(WEKIAVListeGet pWEKIAVListeGet) {
        ClientLog.ausgabeNl("**********kiavListeGet");
        vorbereitenWELoginVerify(pWEKIAVListeGet.getWeLoginVerify());
        WEKIAVListeGetRC lWEKIAVListeGetRC = null;

        initialisiereVerbindungsOjekt();
        if (this.IstHttps()) {
            lWEKIAVListeGetRC = client.target(getPfad("kiavListeGet")).request(MediaType.APPLICATION_JSON)
                    .post(Entity.json(pWEKIAVListeGet), WEKIAVListeGetRC.class);
        } else {
            try {
                lWEKIAVListeGetRC = client.target(getPfad("kiavListeGet")).request(MediaType.APPLICATION_JSON)
                        .post(Entity.json(pWEKIAVListeGet), WEKIAVListeGetRC.class);
            } catch (Exception e2) {
                CaBug.drucke("Verbindung unterbrochen");
                lWEKIAVListeGetRC = new WEKIAVListeGetRC();
                lWEKIAVListeGetRC.rc = CaFehler.teVerbindungsabbruchWebService;
            }
        }

        ClientLog.ausgabeNl("rc=" + lWEKIAVListeGetRC.getRc() + CaFehler.getFehlertext(lWEKIAVListeGetRC.getRc(), 0));
        return lWEKIAVListeGetRC;
    }

    public WEKIAVAbstimmvorschlagGetRC kiavAbstimmvorschlagGet(WEKIAVAbstimmvorschlagGet weKIAVAbstimmvorschlagGet) {
        ClientLog.ausgabeNl("**********kiavAbstimmvorschlagGet");
        vorbereitenWELoginVerify(weKIAVAbstimmvorschlagGet.getWeLoginVerify());
        WEKIAVAbstimmvorschlagGetRC lWEKIAVAbstimmvorschlagGetRC = null;

        initialisiereVerbindungsOjekt();
        if (this.IstHttps()) {
            lWEKIAVAbstimmvorschlagGetRC = client.target(getPfad("kiavAbstimmvorschlagGet"))
                    .request(MediaType.APPLICATION_JSON)
                    .post(Entity.json(weKIAVAbstimmvorschlagGet), WEKIAVAbstimmvorschlagGetRC.class);
        } else {
            try {
                lWEKIAVAbstimmvorschlagGetRC = client.target(getPfad("kiavAbstimmvorschlagGet"))
                        .request(MediaType.APPLICATION_JSON)
                        .post(Entity.json(weKIAVAbstimmvorschlagGet), WEKIAVAbstimmvorschlagGetRC.class);
            } catch (Exception e2) {
                CaBug.drucke("Verbindung unterbrochen");
                lWEKIAVAbstimmvorschlagGetRC = new WEKIAVAbstimmvorschlagGetRC();
                lWEKIAVAbstimmvorschlagGetRC.rc = CaFehler.teVerbindungsabbruchWebService;
            }
        }

        ClientLog.ausgabeNl("rc=" + lWEKIAVAbstimmvorschlagGetRC.getRc()
                + CaFehler.getFehlertext(lWEKIAVAbstimmvorschlagGetRC.getRc(), 0));
        return lWEKIAVAbstimmvorschlagGetRC;

    }

    public WEKIAVErteilenRC kiavErteilen(WEKIAVErteilen weKIAVErteilen) {
        ClientLog.ausgabeNl("**********kiavErteilen");
        vorbereitenWELoginVerify(weKIAVErteilen.getWeLoginVerify());
        WEKIAVErteilenRC lWEKIAVErteilenRC = null;

        initialisiereVerbindungsOjekt();
        if (this.IstHttps()) {
            lWEKIAVErteilenRC = client.target(getPfad("kiavErteilen")).request(MediaType.APPLICATION_JSON)
                    .post(Entity.json(weKIAVErteilen), WEKIAVErteilenRC.class);
        } else {
            try {
                lWEKIAVErteilenRC = client.target(getPfad("kiavErteilen")).request(MediaType.APPLICATION_JSON)
                        .post(Entity.json(weKIAVErteilen), WEKIAVErteilenRC.class);
            } catch (Exception e2) {
                CaBug.drucke("Verbindung unterbrochen");
                lWEKIAVErteilenRC = new WEKIAVErteilenRC();
                lWEKIAVErteilenRC.rc = CaFehler.teVerbindungsabbruchWebService;
            }
        }

        ClientLog.ausgabeNl("rc=" + lWEKIAVErteilenRC.getRc() + CaFehler.getFehlertext(lWEKIAVErteilenRC.getRc(), 0));
        return lWEKIAVErteilenRC;
    }

    public WEDetailKarteGetRC detailKarteGet(WEDetailKarteGet wEDetailKarteGet) {
        ClientLog.ausgabeNl("**********detailKarteGet");
        vorbereitenWELoginVerify(wEDetailKarteGet.getWeLoginVerify());
        WEDetailKarteGetRC lWEDetailKarteGetRC = null;

        initialisiereVerbindungsOjekt();
        if (this.IstHttps()) {
            lWEDetailKarteGetRC = client.target(getPfad("detailKarteGet")).request(MediaType.APPLICATION_JSON)
                    .post(Entity.json(wEDetailKarteGet), WEDetailKarteGetRC.class);
        } else {
            try {
                lWEDetailKarteGetRC = client.target(getPfad("detailKarteGet")).request(MediaType.APPLICATION_JSON)
                        .post(Entity.json(wEDetailKarteGet), WEDetailKarteGetRC.class);
            } catch (Exception e2) {
                CaBug.drucke("Verbindung unterbrochen");
                lWEDetailKarteGetRC = new WEDetailKarteGetRC();
                lWEDetailKarteGetRC.rc = CaFehler.teVerbindungsabbruchWebService;
            }
        }

        ClientLog.ausgabeNl(
                "rc=" + lWEDetailKarteGetRC.getRc() + CaFehler.getFehlertext(lWEDetailKarteGetRC.getRc(), 0));
        return lWEDetailKarteGetRC;
    }

    public WEStornierenVorbereitenGetRC stornierenVorbereitenGet(
            WEStornierenVorbereitenGet weStornierenVorbereitenGet) {
        ClientLog.ausgabeNl("**********stornierenVorbereitenGet");
        vorbereitenWELoginVerify(weStornierenVorbereitenGet.getWeLoginVerify());
        WEStornierenVorbereitenGetRC lWEStornierenVorbereitenGetRC = null;

        initialisiereVerbindungsOjekt();
        if (this.IstHttps()) {
            lWEStornierenVorbereitenGetRC = client.target(getPfad("stornierenVorbereitenGet"))
                    .request(MediaType.APPLICATION_JSON)
                    .post(Entity.json(weStornierenVorbereitenGet), WEStornierenVorbereitenGetRC.class);
        } else {
            try {
                lWEStornierenVorbereitenGetRC = client.target(getPfad("stornierenVorbereitenGet"))
                        .request(MediaType.APPLICATION_JSON)
                        .post(Entity.json(weStornierenVorbereitenGet), WEStornierenVorbereitenGetRC.class);
            } catch (Exception e2) {
                CaBug.drucke("Verbindung unterbrochen");
                lWEStornierenVorbereitenGetRC = new WEStornierenVorbereitenGetRC();
                lWEStornierenVorbereitenGetRC.rc = CaFehler.teVerbindungsabbruchWebService;
            }
        }

        ClientLog.ausgabeNl("rc=" + lWEStornierenVorbereitenGetRC.getRc()
                + CaFehler.getFehlertext(lWEStornierenVorbereitenGetRC.getRc(), 0));
        return lWEStornierenVorbereitenGetRC;
    }

    public WEEintrittskarteStornierenGetRC eintrittskarteStornierenGet(
            WEEintrittskarteStornierenGet weEintrittskarteStornierenGet) {
        ClientLog.ausgabeNl("**********eintrittskarteStornierenGet");
        vorbereitenWELoginVerify(weEintrittskarteStornierenGet.getWeLoginVerify());
        WEEintrittskarteStornierenGetRC lWEEintrittskarteStornierenGetRC = null;

        initialisiereVerbindungsOjekt();
        if (this.IstHttps()) {
            lWEEintrittskarteStornierenGetRC = client.target(getPfad("eintrittskarteStornierenGet"))
                    .request(MediaType.APPLICATION_JSON)
                    .post(Entity.json(weEintrittskarteStornierenGet), WEEintrittskarteStornierenGetRC.class);
        } else {
            try {
                lWEEintrittskarteStornierenGetRC = client.target(getPfad("eintrittskarteStornierenGet"))
                        .request(MediaType.APPLICATION_JSON)
                        .post(Entity.json(weEintrittskarteStornierenGet), WEEintrittskarteStornierenGetRC.class);
            } catch (Exception e2) {
                CaBug.drucke("Verbindung unterbrochen");
                lWEEintrittskarteStornierenGetRC = new WEEintrittskarteStornierenGetRC();
                lWEEintrittskarteStornierenGetRC.rc = CaFehler.teVerbindungsabbruchWebService;
            }
        }

        ClientLog.ausgabeNl("rc=" + lWEEintrittskarteStornierenGetRC.getRc()
                + CaFehler.getFehlertext(lWEEintrittskarteStornierenGetRC.getRc(), 0));
        return lWEEintrittskarteStornierenGetRC;
    }

    public WEWeisungStornierenGetRC weisungStornierenGet(WEWeisungStornierenGet weWeisungStornierenGet) {
        ClientLog.ausgabeNl("**********weisungStornierenGet");
        vorbereitenWELoginVerify(weWeisungStornierenGet.getWeLoginVerify());
        WEWeisungStornierenGetRC lWEWeisungStornierenGetRC = null;

        initialisiereVerbindungsOjekt();
        if (this.IstHttps()) {
            lWEWeisungStornierenGetRC = client.target(getPfad("weisungStornierenGet"))
                    .request(MediaType.APPLICATION_JSON)
                    .post(Entity.json(weWeisungStornierenGet), WEWeisungStornierenGetRC.class);
        } else {
            try {
                lWEWeisungStornierenGetRC = client.target(getPfad("weisungStornierenGet"))
                        .request(MediaType.APPLICATION_JSON)
                        .post(Entity.json(weWeisungStornierenGet), WEWeisungStornierenGetRC.class);
            } catch (Exception e2) {
                CaBug.drucke("Verbindung unterbrochen");
                lWEWeisungStornierenGetRC = new WEWeisungStornierenGetRC();
                lWEWeisungStornierenGetRC.rc = CaFehler.teVerbindungsabbruchWebService;
            }
        }

        ClientLog.ausgabeNl("rc=" + lWEWeisungStornierenGetRC.getRc()
                + CaFehler.getFehlertext(lWEWeisungStornierenGetRC.getRc(), 0));
        return lWEWeisungStornierenGetRC;
    }

    public WEVollmachtDritteStornierenGetRC vollmachtDritteStornierenGet(
            WEVollmachtDritteStornierenGet weVollmachtDritteStornierenGet) {
        ClientLog.ausgabeNl("**********vollmachtDritteStornierenGet");
        vorbereitenWELoginVerify(weVollmachtDritteStornierenGet.getWeLoginVerify());
        WEVollmachtDritteStornierenGetRC lWEVollmachtDritteStornierenGetRC = null;

        initialisiereVerbindungsOjekt();
        if (this.IstHttps()) {
            lWEVollmachtDritteStornierenGetRC = client.target(getPfad("vollmachtDritteStornierenGet"))
                    .request(MediaType.APPLICATION_JSON)
                    .post(Entity.json(weVollmachtDritteStornierenGet), WEVollmachtDritteStornierenGetRC.class);
        } else {
            try {
                lWEVollmachtDritteStornierenGetRC = client.target(getPfad("vollmachtDritteStornierenGet"))
                        .request(MediaType.APPLICATION_JSON)
                        .post(Entity.json(weVollmachtDritteStornierenGet), WEVollmachtDritteStornierenGetRC.class);
            } catch (Exception e2) {
                CaBug.drucke("Verbindung unterbrochen");
                lWEVollmachtDritteStornierenGetRC = new WEVollmachtDritteStornierenGetRC();
                lWEVollmachtDritteStornierenGetRC.rc = CaFehler.teVerbindungsabbruchWebService;
            }
        }

        ClientLog.ausgabeNl("rc=" + lWEVollmachtDritteStornierenGetRC.getRc()
                + CaFehler.getFehlertext(lWEVollmachtDritteStornierenGetRC.getRc(), 0));
        return lWEVollmachtDritteStornierenGetRC;
    }

    public WEWeisungAendernVorbereitenGetRC weisungAendernVorbereitenGet(
            WEWeisungAendernVorbereitenGet weWeisungAendernVorbereitenGet) {
        ClientLog.ausgabeNl("**********weisungAendernVorbereitenGet");
        vorbereitenWELoginVerify(weWeisungAendernVorbereitenGet.getWeLoginVerify());
        WEWeisungAendernVorbereitenGetRC lWEWeisungAendernVorbereitenGetRC = null;

        initialisiereVerbindungsOjekt();
        if (this.IstHttps()) {
            lWEWeisungAendernVorbereitenGetRC = client.target(getPfad("weisungAendernVorbereitenGet"))
                    .request(MediaType.APPLICATION_JSON)
                    .post(Entity.json(weWeisungAendernVorbereitenGet), WEWeisungAendernVorbereitenGetRC.class);
        } else {
            try {
                lWEWeisungAendernVorbereitenGetRC = client.target(getPfad("weisungAendernVorbereitenGet"))
                        .request(MediaType.APPLICATION_JSON)
                        .post(Entity.json(weWeisungAendernVorbereitenGet), WEWeisungAendernVorbereitenGetRC.class);
            } catch (Exception e2) {
                CaBug.drucke("Verbindung unterbrochen");
                lWEWeisungAendernVorbereitenGetRC = new WEWeisungAendernVorbereitenGetRC();
                lWEWeisungAendernVorbereitenGetRC.rc = CaFehler.teVerbindungsabbruchWebService;
            }
        }

        ClientLog.ausgabeNl("rc=" + lWEWeisungAendernVorbereitenGetRC.getRc()
                + CaFehler.getFehlertext(lWEWeisungAendernVorbereitenGetRC.getRc(), 0));
        return lWEWeisungAendernVorbereitenGetRC;
    }

    public WEWeisungAendernGetRC weisungAendernGet(WEWeisungAendernGet weWeisungAendernGet) {
        ClientLog.ausgabeNl("**********weisungAendernGet");
        vorbereitenWELoginVerify(weWeisungAendernGet.getWeLoginVerify());
        WEWeisungAendernGetRC lWEWeisungAendernGetRC = null;

        initialisiereVerbindungsOjekt();
        if (this.IstHttps()) {
            lWEWeisungAendernGetRC = client.target(getPfad("weisungAendernGet")).request(MediaType.APPLICATION_JSON)
                    .post(Entity.json(weWeisungAendernGet), WEWeisungAendernGetRC.class);
        } else {
            try {
                lWEWeisungAendernGetRC = client.target(getPfad("weisungAendernGet")).request(MediaType.APPLICATION_JSON)
                        .post(Entity.json(weWeisungAendernGet), WEWeisungAendernGetRC.class);
            } catch (Exception e2) {
                CaBug.drucke("Verbindung unterbrochen");
                lWEWeisungAendernGetRC = new WEWeisungAendernGetRC();
                lWEWeisungAendernGetRC.rc = CaFehler.teVerbindungsabbruchWebService;
            }
        }

        ClientLog.ausgabeNl(
                "rc=" + lWEWeisungAendernGetRC.getRc() + CaFehler.getFehlertext(lWEWeisungAendernGetRC.getRc(), 0));
        return lWEWeisungAendernGetRC;
    }

    public WEVollmachtDritteGetRC vollmachtDritteGet(WEVollmachtDritteGet weVollmachtDritteGet) {
        ClientLog.ausgabeNl("**********vollmachtDritteGet");
        vorbereitenWELoginVerify(weVollmachtDritteGet.getWeLoginVerify());
        WEVollmachtDritteGetRC lWEVollmachtDritteGetRC = null;

        initialisiereVerbindungsOjekt();
        if (this.IstHttps()) {
            lWEVollmachtDritteGetRC = client.target(getPfad("vollmachtDritteGet")).request(MediaType.APPLICATION_JSON)
                    .post(Entity.json(weVollmachtDritteGet), WEVollmachtDritteGetRC.class);
        } else {
            try {
                lWEVollmachtDritteGetRC = client.target(getPfad("vollmachtDritteGet"))
                        .request(MediaType.APPLICATION_JSON)
                        .post(Entity.json(weVollmachtDritteGet), WEVollmachtDritteGetRC.class);
            } catch (Exception e2) {
                CaBug.drucke("Verbindung unterbrochen");
                lWEVollmachtDritteGetRC = new WEVollmachtDritteGetRC();
                lWEVollmachtDritteGetRC.rc = CaFehler.teVerbindungsabbruchWebService;
            }
        }

        ClientLog.ausgabeNl(
                "rc=" + lWEVollmachtDritteGetRC.getRc() + CaFehler.getFehlertext(lWEVollmachtDritteGetRC.getRc(), 0));
        return lWEVollmachtDritteGetRC;
    }

    public WEVersandAdressePruefenGetRC versandAdressePruefenGet(
            WEVersandAdressePruefenGet weVersandAdressePruefenGet) {
        ClientLog.ausgabeNl("**********versandAdressePruefenGet");
        vorbereitenWELoginVerify(weVersandAdressePruefenGet.getWeLoginVerify());
        WEVersandAdressePruefenGetRC lWEVersandAdressePruefenGetRC = null;

        initialisiereVerbindungsOjekt();
        if (this.IstHttps()) {
            lWEVersandAdressePruefenGetRC = client.target(getPfad("versandAdressePruefenGet"))
                    .request(MediaType.APPLICATION_JSON)
                    .post(Entity.json(weVersandAdressePruefenGet), WEVersandAdressePruefenGetRC.class);
        } else {
            try {
                lWEVersandAdressePruefenGetRC = client.target(getPfad("versandAdressePruefenGet"))
                        .request(MediaType.APPLICATION_JSON)
                        .post(Entity.json(weVersandAdressePruefenGet), WEVersandAdressePruefenGetRC.class);
            } catch (Exception e2) {
                CaBug.drucke("Verbindung unterbrochen");
                lWEVersandAdressePruefenGetRC = new WEVersandAdressePruefenGetRC();
                lWEVersandAdressePruefenGetRC.rc = CaFehler.teVerbindungsabbruchWebService;
            }
        }

        ClientLog.ausgabeNl("rc=" + lWEVersandAdressePruefenGetRC.getRc()
                + CaFehler.getFehlertext(lWEVersandAdressePruefenGetRC.getRc(), 0));
        return lWEVersandAdressePruefenGetRC;
    }

    public WEVersandAdresseKorrigierenRC versandAdresseKorrigieren(
            WEVersandAdresseKorrigieren weVersandAdresseKorrigieren) {
        ClientLog.ausgabeNl("**********versandAdresseKorrigieren");
        vorbereitenWELoginVerify(weVersandAdresseKorrigieren.getWeLoginVerify());
        WEVersandAdresseKorrigierenRC lWEVersandAdresseKorrigierenRC = null;

        initialisiereVerbindungsOjekt();
        if (this.IstHttps()) {
            lWEVersandAdresseKorrigierenRC = client.target(getPfad("versandAdresseKorrigieren"))
                    .request(MediaType.APPLICATION_JSON)
                    .post(Entity.json(weVersandAdresseKorrigieren), WEVersandAdresseKorrigierenRC.class);
        } else {
            try {
                lWEVersandAdresseKorrigierenRC = client.target(getPfad("versandAdresseKorrigieren"))
                        .request(MediaType.APPLICATION_JSON)
                        .post(Entity.json(weVersandAdresseKorrigieren), WEVersandAdresseKorrigierenRC.class);
            } catch (Exception e2) {
                CaBug.drucke("Verbindung unterbrochen");
                lWEVersandAdresseKorrigierenRC = new WEVersandAdresseKorrigierenRC();
                lWEVersandAdresseKorrigierenRC.rc = CaFehler.teVerbindungsabbruchWebService;
            }
        }

        ClientLog.ausgabeNl("rc=" + lWEVersandAdresseKorrigierenRC.getRc()
                + CaFehler.getFehlertext(lWEVersandAdresseKorrigierenRC.getRc(), 0));
        return lWEVersandAdresseKorrigierenRC;
    }

    public WEEintrittskartenDrucklaufGetRC eintrittskartenDrucklaufGet(
            WEEintrittskartenDrucklaufGet weEintrittskartenDrucklaufGet) {
        ClientLog.ausgabeNl("**********eintrittskartenDrucklaufGet");
        vorbereitenWELoginVerify(weEintrittskartenDrucklaufGet.getWeLoginVerify());
        WEEintrittskartenDrucklaufGetRC lWEEintrittskartenDrucklaufGetRC = null;

        initialisiereVerbindungsOjekt();
        if (this.IstHttps()) {
            lWEEintrittskartenDrucklaufGetRC = client.target(getPfad("eintrittskartenDrucklaufGet"))
                    .request(MediaType.APPLICATION_JSON)
                    .post(Entity.json(weEintrittskartenDrucklaufGet), WEEintrittskartenDrucklaufGetRC.class);
        } else {
            try {
                lWEEintrittskartenDrucklaufGetRC = client.target(getPfad("eintrittskartenDrucklaufGet"))
                        .request(MediaType.APPLICATION_JSON)
                        .post(Entity.json(weEintrittskartenDrucklaufGet), WEEintrittskartenDrucklaufGetRC.class);
            } catch (Exception e2) {
                CaBug.drucke("Verbindung unterbrochen");
                lWEEintrittskartenDrucklaufGetRC = new WEEintrittskartenDrucklaufGetRC();
                lWEEintrittskartenDrucklaufGetRC.rc = CaFehler.teVerbindungsabbruchWebService;
            }
        }

        ClientLog.ausgabeNl("rc=" + lWEEintrittskartenDrucklaufGetRC.getRc()
                + CaFehler.getFehlertext(lWEEintrittskartenDrucklaufGetRC.getRc(), 0));
        return lWEEintrittskartenDrucklaufGetRC;
    }

    public WEEintrittskartenDrucklaufGetRC eintrittskartenDrucklaufEinzelGet(
            WEEintrittskartenDrucklaufEinzelGet weEintrittskartenDrucklaufEinzelGet) {
        ClientLog.ausgabeNl("**********eintrittskartenDrucklaufEinzelGet");
        vorbereitenWELoginVerify(weEintrittskartenDrucklaufEinzelGet.getWeLoginVerify());
        WEEintrittskartenDrucklaufGetRC lWEEintrittskartenDrucklaufGetRC = null;

        initialisiereVerbindungsOjekt();
        if (this.IstHttps()) {
            lWEEintrittskartenDrucklaufGetRC = client.target(getPfad("eintrittskartenDrucklaufEinzelGet"))
                    .request(MediaType.APPLICATION_JSON)
                    .post(Entity.json(weEintrittskartenDrucklaufEinzelGet), WEEintrittskartenDrucklaufGetRC.class);
        } else {
            try {
                lWEEintrittskartenDrucklaufGetRC = client.target(getPfad("eintrittskartenDrucklaufEinzelGet"))
                        .request(MediaType.APPLICATION_JSON)
                        .post(Entity.json(weEintrittskartenDrucklaufEinzelGet), WEEintrittskartenDrucklaufGetRC.class);
            } catch (Exception e2) {
                CaBug.drucke("Verbindung unterbrochen");
                lWEEintrittskartenDrucklaufGetRC = new WEEintrittskartenDrucklaufGetRC();
                lWEEintrittskartenDrucklaufGetRC.rc = CaFehler.teVerbindungsabbruchWebService;
            }
        }

        ClientLog.ausgabeNl("rc=" + lWEEintrittskartenDrucklaufGetRC.getRc()
                + CaFehler.getFehlertext(lWEEintrittskartenDrucklaufGetRC.getRc(), 0));
        return lWEEintrittskartenDrucklaufGetRC;
    }

    public WEEintrittskarteGedrucktRC eintrittskarteGedruckt(WEEintrittskarteGedruckt weEintrittskarteGedruckt) {
        ClientLog.ausgabeNl("**********eintrittskarteGedruckt");
        vorbereitenWELoginVerify(weEintrittskarteGedruckt.getWeLoginVerify());
        WEEintrittskarteGedrucktRC lWEEintrittskarteGedrucktRC = null;

        initialisiereVerbindungsOjekt();
        if (this.IstHttps()) {
            lWEEintrittskarteGedrucktRC = client.target(getPfad("eintrittskarteGedruckt"))
                    .request(MediaType.APPLICATION_JSON)
                    .post(Entity.json(weEintrittskarteGedruckt), WEEintrittskarteGedrucktRC.class);
        } else {
            try {
                lWEEintrittskarteGedrucktRC = client.target(getPfad("eintrittskarteGedruckt"))
                        .request(MediaType.APPLICATION_JSON)
                        .post(Entity.json(weEintrittskarteGedruckt), WEEintrittskarteGedrucktRC.class);
            } catch (Exception e2) {
                CaBug.drucke("Verbindung unterbrochen");
                lWEEintrittskarteGedrucktRC = new WEEintrittskarteGedrucktRC();
                lWEEintrittskarteGedrucktRC.rc = CaFehler.teVerbindungsabbruchWebService;
            }
        }

        ClientLog.ausgabeNl("rc=" + lWEEintrittskarteGedrucktRC.getRc()
                + CaFehler.getFehlertext(lWEEintrittskarteGedrucktRC.getRc(), 0));
        return lWEEintrittskarteGedrucktRC;
    }

    public WEDrucklaeufeGetRC drucklaeufeGet(WEDrucklaeufeGet weDrucklaeufeGet) {
        ClientLog.ausgabeNl("**********drucklaeufeGet");
        vorbereitenWELoginVerify(weDrucklaeufeGet.getWeLoginVerify());
        WEDrucklaeufeGetRC lWEDrucklaeufeGetRC = null;

        initialisiereVerbindungsOjekt();
        if (this.IstHttps()) {
            lWEDrucklaeufeGetRC = client.target(getPfad("drucklaeufeGet")).request(MediaType.APPLICATION_JSON)
                    .post(Entity.json(weDrucklaeufeGet), WEDrucklaeufeGetRC.class);
        } else {
            try {
                lWEDrucklaeufeGetRC = client.target(getPfad("drucklaeufeGet")).request(MediaType.APPLICATION_JSON)
                        .post(Entity.json(weDrucklaeufeGet), WEDrucklaeufeGetRC.class);
            } catch (Exception e2) {
                CaBug.drucke("Verbindung unterbrochen");
                lWEDrucklaeufeGetRC = new WEDrucklaeufeGetRC();
                lWEDrucklaeufeGetRC.rc = CaFehler.teVerbindungsabbruchWebService;
            }
        }

        ClientLog.ausgabeNl(
                "rc=" + lWEDrucklaeufeGetRC.getRc() + CaFehler.getFehlertext(lWEDrucklaeufeGetRC.getRc(), 0));
        return lWEDrucklaeufeGetRC;
    }

    public WEBestandAnmeldenRC bestandAnmelden(WEBestandAnmelden weBestandAnmelden) {
        ClientLog.ausgabeNl("**********bestandAnmelden");
        vorbereitenWELoginVerify(weBestandAnmelden.getWeLoginVerify());
        WEBestandAnmeldenRC lWEBestandAnmeldenRC = null;

        initialisiereVerbindungsOjekt();
        if (this.IstHttps()) {
            lWEBestandAnmeldenRC = client.target(getPfad("bestandAnmelden")).request(MediaType.APPLICATION_JSON)
                    .post(Entity.json(weBestandAnmelden), WEBestandAnmeldenRC.class);
        } else {
            try {
                lWEBestandAnmeldenRC = client.target(getPfad("bestandAnmelden")).request(MediaType.APPLICATION_JSON)
                        .post(Entity.json(weBestandAnmelden), WEBestandAnmeldenRC.class);
            } catch (Exception e2) {
                CaBug.drucke("Verbindung unterbrochen");
                lWEBestandAnmeldenRC = new WEBestandAnmeldenRC();
                lWEBestandAnmeldenRC.rc = CaFehler.teVerbindungsabbruchWebService;
            }
        }

        ClientLog.ausgabeNl(
                "rc=" + lWEBestandAnmeldenRC.getRc() + CaFehler.getFehlertext(lWEBestandAnmeldenRC.getRc(), 0));
        return lWEBestandAnmeldenRC;
    }

    public WEBestandAnmeldenRC bestandFixAnmelden(WEBestandAnmelden weBestandAnmelden) {
        ClientLog.ausgabeNl("**********bestandFixAnmelden");
        vorbereitenWELoginVerify(weBestandAnmelden.getWeLoginVerify());
        WEBestandAnmeldenRC lWEBestandAnmeldenRC = null;

        initialisiereVerbindungsOjekt();
        if (this.IstHttps()) {
            lWEBestandAnmeldenRC = client.target(getPfad("bestandFixAnmelden")).request(MediaType.APPLICATION_JSON)
                    .post(Entity.json(weBestandAnmelden), WEBestandAnmeldenRC.class);
        } else {
            try {
                lWEBestandAnmeldenRC = client.target(getPfad("bestandFixAnmelden")).request(MediaType.APPLICATION_JSON)
                        .post(Entity.json(weBestandAnmelden), WEBestandAnmeldenRC.class);
            } catch (Exception e2) {
                CaBug.drucke("Verbindung unterbrochen");
                lWEBestandAnmeldenRC = new WEBestandAnmeldenRC();
                lWEBestandAnmeldenRC.rc = CaFehler.teVerbindungsabbruchWebService;
            }
        }

        ClientLog.ausgabeNl(
                "rc=" + lWEBestandAnmeldenRC.getRc() + CaFehler.getFehlertext(lWEBestandAnmeldenRC.getRc(), 0));
        return lWEBestandAnmeldenRC;
    }

    public WEBestandFixAendernRC bestandFixAendern(WEBestandFixAendern weBestandFixAendern) {
        ClientLog.ausgabeNl("**********bestandFixAendern");
        vorbereitenWELoginVerify(weBestandFixAendern.getWeLoginVerify());
        WEBestandFixAendernRC lWEBestandFixAendernRC = null;

        initialisiereVerbindungsOjekt();
        if (this.IstHttps()) {
            lWEBestandFixAendernRC = client.target(getPfad("bestandFixAendern")).request(MediaType.APPLICATION_JSON)
                    .post(Entity.json(weBestandFixAendern), WEBestandFixAendernRC.class);
        } else {
            try {
                lWEBestandFixAendernRC = client.target(getPfad("bestandFixAendern")).request(MediaType.APPLICATION_JSON)
                        .post(Entity.json(weBestandFixAendern), WEBestandFixAendernRC.class);
            } catch (Exception e2) {
                CaBug.drucke("Verbindung unterbrochen");
                lWEBestandFixAendernRC = new WEBestandFixAendernRC();
                lWEBestandFixAendernRC.rc = CaFehler.teVerbindungsabbruchWebService;
            }
        }

        ClientLog.ausgabeNl(
                "rc=" + lWEBestandFixAendernRC.getRc() + CaFehler.getFehlertext(lWEBestandFixAendernRC.getRc(), 0));
        return lWEBestandFixAendernRC;
    }

    public WEAnmeldungStornierenRC anmeldungStornieren(WEAnmeldungStornieren weAnmeldungStornieren) {
        ClientLog.ausgabeNl("**********anmeldungStornieren");
        vorbereitenWELoginVerify(weAnmeldungStornieren.getWeLoginVerify());
        WEAnmeldungStornierenRC lWEAnmeldungStornierenRC = null;

        initialisiereVerbindungsOjekt();
        if (this.IstHttps()) {
            lWEAnmeldungStornierenRC = client.target(getPfad("anmeldungStornieren")).request(MediaType.APPLICATION_JSON)
                    .post(Entity.json(weAnmeldungStornieren), WEAnmeldungStornierenRC.class);
        } else {
            try {
                lWEAnmeldungStornierenRC = client.target(getPfad("anmeldungStornieren"))
                        .request(MediaType.APPLICATION_JSON)
                        .post(Entity.json(weAnmeldungStornieren), WEAnmeldungStornierenRC.class);
            } catch (Exception e2) {
                CaBug.drucke("Verbindung unterbrochen");
                lWEAnmeldungStornierenRC = new WEAnmeldungStornierenRC();
                lWEAnmeldungStornierenRC.rc = CaFehler.teVerbindungsabbruchWebService;
            }
        }

        ClientLog.ausgabeNl(
                "rc=" + lWEAnmeldungStornierenRC.getRc() + CaFehler.getFehlertext(lWEAnmeldungStornierenRC.getRc(), 0));
        return lWEAnmeldungStornierenRC;
    }

    public WEAktienregisterNeuerEintragRC aktienregisterNeuerEintrag(
            WEAktienregisterNeuerEintrag weAktienregisterNeuerEintrag) {
        ClientLog.ausgabeNl("**********aktienregisterNeuerEintrag");
        vorbereitenWELoginVerify(weAktienregisterNeuerEintrag.getWeLoginVerify());
        WEAktienregisterNeuerEintragRC lWEAktienregisterNeuerEintragRC = null;

        initialisiereVerbindungsOjekt();
        if (this.IstHttps()) {
            lWEAktienregisterNeuerEintragRC = client.target(getPfad("aktienregisterNeuerEintrag"))
                    .request(MediaType.APPLICATION_JSON)
                    .post(Entity.json(weAktienregisterNeuerEintrag), WEAktienregisterNeuerEintragRC.class);
        } else {
            try {
                lWEAktienregisterNeuerEintragRC = client.target(getPfad("aktienregisterNeuerEintrag"))
                        .request(MediaType.APPLICATION_JSON)
                        .post(Entity.json(weAktienregisterNeuerEintrag), WEAktienregisterNeuerEintragRC.class);
            } catch (Exception e2) {
                CaBug.drucke("Verbindung unterbrochen");
                lWEAktienregisterNeuerEintragRC = new WEAktienregisterNeuerEintragRC();
                lWEAktienregisterNeuerEintragRC.rc = CaFehler.teVerbindungsabbruchWebService;
            }
        }

        ClientLog.ausgabeNl("rc=" + lWEAktienregisterNeuerEintragRC.getRc()
                + CaFehler.getFehlertext(lWEAktienregisterNeuerEintragRC.getRc(), 0));
        return lWEAktienregisterNeuerEintragRC;
    }

    public WEAktionaersStatusDetailGetRC aktionaersStatusDetailGet(
            WEAktionaersStatusDetailGet weAktionaersStatusDetailGet) {
        ClientLog.ausgabeNl("**********aktionaersStatusDetailGet");
        vorbereitenWELoginVerify(weAktionaersStatusDetailGet.getWeLoginVerify());
        WEAktionaersStatusDetailGetRC lWEAktionaersStatusDetailGetRC = null;

        initialisiereVerbindungsOjekt();
        if (this.IstHttps()) {
            lWEAktionaersStatusDetailGetRC = client.target(getPfad("aktionaersStatusDetailGet"))
                    .request(MediaType.APPLICATION_JSON)
                    .post(Entity.json(weAktionaersStatusDetailGet), WEAktionaersStatusDetailGetRC.class);
        } else {
            try {
                lWEAktionaersStatusDetailGetRC = client.target(getPfad("aktionaersStatusDetailGet"))
                        .request(MediaType.APPLICATION_JSON)
                        .post(Entity.json(weAktionaersStatusDetailGet), WEAktionaersStatusDetailGetRC.class);
            } catch (Exception e2) {
                CaBug.drucke("Verbindung unterbrochen");
                System.out.println(e2);
                lWEAktionaersStatusDetailGetRC = new WEAktionaersStatusDetailGetRC();
                lWEAktionaersStatusDetailGetRC.rc = CaFehler.teVerbindungsabbruchWebService;
            }
        }

        ClientLog.ausgabeNl("rc=" + lWEAktionaersStatusDetailGetRC.getRc()
                + CaFehler.getFehlertext(lWEAktionaersStatusDetailGetRC.getRc(), 0));
        return lWEAktionaersStatusDetailGetRC;
    }

    public WEEintrittskarteKorrigieren eintrittskarteKorrigierenGET(
            WEEintrittskarteKorrigieren weEintrittskarteKorrigieren) {
        ClientLog.ausgabeNl("**********eintrittskarteKorrigierenGET");
        vorbereitenWELoginVerify(weEintrittskarteKorrigieren.getWeLoginVerify());
        WEEintrittskarteKorrigieren lWEEintrittskarteKorrigieren = null;

        initialisiereVerbindungsOjekt();
        if (this.IstHttps()) {
            lWEEintrittskarteKorrigieren = client.target(getPfad("eintrittskarteKorrigierenGET"))
                    .request(MediaType.APPLICATION_JSON)
                    .post(Entity.json(weEintrittskarteKorrigieren), WEEintrittskarteKorrigieren.class);
        } else {
            try {
                lWEEintrittskarteKorrigieren = client.target(getPfad("eintrittskarteKorrigierenGET"))
                        .request(MediaType.APPLICATION_JSON)
                        .post(Entity.json(weEintrittskarteKorrigieren), WEEintrittskarteKorrigieren.class);
            } catch (Exception e2) {
                CaBug.drucke("Verbindung unterbrochen");
                lWEEintrittskarteKorrigieren = new WEEintrittskarteKorrigieren();
                lWEEintrittskarteKorrigieren.rc = CaFehler.teVerbindungsabbruchWebService;
            }
        }

        ClientLog.ausgabeNl("rc=" + lWEEintrittskarteKorrigieren.getRc()
                + CaFehler.getFehlertext(lWEEintrittskarteKorrigieren.getRc(), 0));
        return lWEEintrittskarteKorrigieren;
    }

    public WEEintrittskarteKorrigieren eintrittskarteKorrigierenPUT(
            WEEintrittskarteKorrigieren weEintrittskarteKorrigieren) {
        ClientLog.ausgabeNl("**********eintrittskarteKorrigierenPUT");
        vorbereitenWELoginVerify(weEintrittskarteKorrigieren.getWeLoginVerify());
        WEEintrittskarteKorrigieren lWEEintrittskarteKorrigieren = null;

        initialisiereVerbindungsOjekt();
        if (this.IstHttps()) {
            lWEEintrittskarteKorrigieren = client.target(getPfad("eintrittskarteKorrigierenPUT"))
                    .request(MediaType.APPLICATION_JSON)
                    .post(Entity.json(weEintrittskarteKorrigieren), WEEintrittskarteKorrigieren.class);
        } else {
            try {
                lWEEintrittskarteKorrigieren = client.target(getPfad("eintrittskarteKorrigierenPUT"))
                        .request(MediaType.APPLICATION_JSON)
                        .post(Entity.json(weEintrittskarteKorrigieren), WEEintrittskarteKorrigieren.class);
            } catch (Exception e2) {
                CaBug.drucke("Verbindung unterbrochen");
                lWEEintrittskarteKorrigieren = new WEEintrittskarteKorrigieren();
                lWEEintrittskarteKorrigieren.rc = CaFehler.teVerbindungsabbruchWebService;
            }
        }

        ClientLog.ausgabeNl("rc=" + lWEEintrittskarteKorrigieren.getRc()
                + CaFehler.getFehlertext(lWEEintrittskarteKorrigieren.getRc(), 0));
        return lWEEintrittskarteKorrigieren;
    }

    public WEPraesenzStatusabfrageRC praesenzStatusabfrage(WEPraesenzStatusabfrage wePraesenzStatusabfrage) {
        ClientLog.ausgabeNl("**********praesenzStatusabfrage");
        vorbereitenWELoginVerify(wePraesenzStatusabfrage.getWeLoginVerify());
        WEPraesenzStatusabfrageRC lWEPraesenzStatusabfrageRC = null;

        initialisiereVerbindungsOjekt();
        if (this.IstHttps()) {
            lWEPraesenzStatusabfrageRC = client.target(getPfad("praesenzStatusabfrage"))
                    .request(MediaType.APPLICATION_JSON)
                    .post(Entity.json(wePraesenzStatusabfrage), WEPraesenzStatusabfrageRC.class);
        } else {
            try {
                lWEPraesenzStatusabfrageRC = client.target(getPfad("praesenzStatusabfrage"))
                        .request(MediaType.APPLICATION_JSON)
                        .post(Entity.json(wePraesenzStatusabfrage), WEPraesenzStatusabfrageRC.class);
            } catch (Exception e2) {
                CaBug.drucke("Verbindung unterbrochen");
                lWEPraesenzStatusabfrageRC = new WEPraesenzStatusabfrageRC();
                lWEPraesenzStatusabfrageRC.rc = CaFehler.teVerbindungsabbruchWebService;
            }

        }

        ClientLog.ausgabeNl("rc=" + lWEPraesenzStatusabfrageRC.getRc()
                + CaFehler.getFehlertext(lWEPraesenzStatusabfrageRC.getRc(), 0));
        return lWEPraesenzStatusabfrageRC;
    }

    public WEPraesenzBuchenRC praesenzBuchen(WEPraesenzBuchen wePraesenzBuchen) {
        ClientLog.ausgabeNl("**********praesenzBuchen");
        vorbereitenWELoginVerify(wePraesenzBuchen.getWeLoginVerify());
        WEPraesenzBuchenRC lWEPraesenzBuchenRC = null;

        initialisiereVerbindungsOjekt();
        if (this.IstHttps()) {
            lWEPraesenzBuchenRC = client.target(getPfad("praesenzBuchen")).request(MediaType.APPLICATION_JSON)
                    .post(Entity.json(wePraesenzBuchen), WEPraesenzBuchenRC.class);
        } else {
            try {
                lWEPraesenzBuchenRC = client.target(getPfad("praesenzBuchen")).request(MediaType.APPLICATION_JSON)
                        .post(Entity.json(wePraesenzBuchen), WEPraesenzBuchenRC.class);
            } catch (Exception e2) {
                CaBug.drucke("Verbindung unterbrochen");
                lWEPraesenzBuchenRC = new WEPraesenzBuchenRC();
                lWEPraesenzBuchenRC.rc = CaFehler.teVerbindungsabbruchWebService;
            }
        }

        ClientLog.ausgabeNl(
                "rc=" + lWEPraesenzBuchenRC.getRc() + CaFehler.getFehlertext(lWEPraesenzBuchenRC.getRc(), 0));
        return lWEPraesenzBuchenRC;
    }

    public WESuchenRC suchen(WESuchen weSuchen) {
        ClientLog.ausgabeNl("**********suchen");
        vorbereitenWELoginVerify(weSuchen.getWeLoginVerify());
        WESuchenRC lWESuchenRC = null;

        initialisiereVerbindungsOjekt();
        if (this.IstHttps()) {
            lWESuchenRC = client.target(getPfad("suchen")).request(MediaType.APPLICATION_JSON)
                    .post(Entity.json(weSuchen), WESuchenRC.class);
        } else {
            try {
                lWESuchenRC = client.target(getPfad("suchen")).request(MediaType.APPLICATION_JSON)
                        .post(Entity.json(weSuchen), WESuchenRC.class);
            } catch (Exception e2) {
                CaBug.drucke("Verbindung unterbrochen");
                lWESuchenRC = new WESuchenRC();
                lWESuchenRC.rc = CaFehler.teVerbindungsabbruchWebService;
            }
        }

        ClientLog.ausgabeNl("rc=" + lWESuchenRC.getRc() + CaFehler.getFehlertext(lWESuchenRC.getRc(), 0));
        return lWESuchenRC;
    }

    public WEAbstimmungLeseAktivenAbstimmungsblockRC abstimmungLeseAktivenAbstimmungsblock(
            WEAbstimmungLeseAktivenAbstimmungsblock weAbstimmungLeseAktivenAbstimmungsblock) {
        ClientLog.ausgabeNl("**********abstimmungLeseAktivenAbstimmungsblock");
        vorbereitenWELoginVerify(weAbstimmungLeseAktivenAbstimmungsblock.getWeLoginVerify());
        WEAbstimmungLeseAktivenAbstimmungsblockRC lWEAbstimmungLeseAktivenAbstimmungsblockRC = null;

        initialisiereVerbindungsOjekt();
        if (this.IstHttps()) {
            lWEAbstimmungLeseAktivenAbstimmungsblockRC = client.target(getPfad("abstimmungLeseAktivenAbstimmungsblock"))
                    .request(MediaType.APPLICATION_JSON).post(Entity.json(weAbstimmungLeseAktivenAbstimmungsblock),
                            WEAbstimmungLeseAktivenAbstimmungsblockRC.class);
        } else {
            try {
                lWEAbstimmungLeseAktivenAbstimmungsblockRC = client
                        .target(getPfad("abstimmungLeseAktivenAbstimmungsblock")).request(MediaType.APPLICATION_JSON)
                        .post(Entity.json(weAbstimmungLeseAktivenAbstimmungsblock),
                                WEAbstimmungLeseAktivenAbstimmungsblockRC.class);

            } catch (Exception e2) {
                CaBug.drucke("Verbindung unterbrochen");
                lWEAbstimmungLeseAktivenAbstimmungsblockRC = new WEAbstimmungLeseAktivenAbstimmungsblockRC();
                lWEAbstimmungLeseAktivenAbstimmungsblockRC.rc = CaFehler.teVerbindungsabbruchWebService;
            }
        }

        ClientLog.ausgabeNl("rc=" + lWEAbstimmungLeseAktivenAbstimmungsblockRC.getRc()
                + CaFehler.getFehlertext(lWEAbstimmungLeseAktivenAbstimmungsblockRC.getRc(), 0));
        return lWEAbstimmungLeseAktivenAbstimmungsblockRC;
    }

    public WEAbstimmungAktionaerLesenRC abstimmungAktionaerLesen(
            WEAbstimmungAktionaerLesen weAbstimmungAktionaerLesen) {
        ClientLog.ausgabeNl("**********abstimmungAktionaerLesen");
        vorbereitenWELoginVerify(weAbstimmungAktionaerLesen.getWeLoginVerify());
        WEAbstimmungAktionaerLesenRC lWEAbstimmungAktionaerLesenRC = null;

        initialisiereVerbindungsOjekt();
        if (this.IstHttps()) {
            lWEAbstimmungAktionaerLesenRC = client.target(getPfad("abstimmungAktionaerLesen"))
                    .request(MediaType.APPLICATION_JSON)
                    .post(Entity.json(weAbstimmungAktionaerLesen), WEAbstimmungAktionaerLesenRC.class);
        } else {
            try {
                lWEAbstimmungAktionaerLesenRC = client.target(getPfad("abstimmungAktionaerLesen"))
                        .request(MediaType.APPLICATION_JSON)
                        .post(Entity.json(weAbstimmungAktionaerLesen), WEAbstimmungAktionaerLesenRC.class);

            } catch (Exception e2) {
                CaBug.drucke("Verbindung unterbrochen");
                lWEAbstimmungAktionaerLesenRC = new WEAbstimmungAktionaerLesenRC();
                lWEAbstimmungAktionaerLesenRC.rc = CaFehler.teVerbindungsabbruchWebService;
            }
        }

        ClientLog.ausgabeNl("rc=" + lWEAbstimmungAktionaerLesenRC.getRc()
                + CaFehler.getFehlertext(lWEAbstimmungAktionaerLesenRC.getRc(), 0));
        return lWEAbstimmungAktionaerLesenRC;
    }

    public WEAbstimmungku310AktionaerLesenRC abstimmungku310AktionaerLesen(
            WEAbstimmungku310AktionaerLesen weAbstimmungku310AktionaerLesen) {
        ClientLog.ausgabeNl("**********abstimmungku310AktionaerLesen");
        vorbereitenWELoginVerify(weAbstimmungku310AktionaerLesen.getWeLoginVerify());
        WEAbstimmungku310AktionaerLesenRC lWEAbstimmungku310AktionaerLesenRC = null;

        initialisiereVerbindungsOjekt();
        if (this.IstHttps()) {
            lWEAbstimmungku310AktionaerLesenRC = client.target(getPfad("abstimmungku310AktionaerLesen"))
                    .request(MediaType.APPLICATION_JSON)
                    .post(Entity.json(weAbstimmungku310AktionaerLesen), WEAbstimmungku310AktionaerLesenRC.class);
        } else {
            try {
                lWEAbstimmungku310AktionaerLesenRC = client.target(getPfad("abstimmungku310AktionaerLesen"))
                        .request(MediaType.APPLICATION_JSON)
                        .post(Entity.json(weAbstimmungku310AktionaerLesen), WEAbstimmungku310AktionaerLesenRC.class);

            } catch (Exception e2) {
                CaBug.drucke("Verbindung unterbrochen");
                lWEAbstimmungku310AktionaerLesenRC = new WEAbstimmungku310AktionaerLesenRC();
                lWEAbstimmungku310AktionaerLesenRC.rc = CaFehler.teVerbindungsabbruchWebService;
            }
        }

        ClientLog.ausgabeNl("rc=" + lWEAbstimmungku310AktionaerLesenRC.getRc()
                + CaFehler.getFehlertext(lWEAbstimmungku310AktionaerLesenRC.getRc(), 0));
        return lWEAbstimmungku310AktionaerLesenRC;
    }

    public WEAbstimmungku310StimmeAbgebenRC abstimmungku310StimmeAbgeben(
            WEAbstimmungku310StimmeAbgeben weAbstimmungku310StimmeAbgeben) {
        ClientLog.ausgabeNl("**********abstimmungku310StimmeAbgeben");
        vorbereitenWELoginVerify(weAbstimmungku310StimmeAbgeben.getWeLoginVerify());
        WEAbstimmungku310StimmeAbgebenRC lWEAbstimmungku310StimmeAbgebenRC = null;

        initialisiereVerbindungsOjekt();
        if (this.IstHttps()) {
            lWEAbstimmungku310StimmeAbgebenRC = client.target(getPfad("abstimmungku310StimmeAbgeben"))
                    .request(MediaType.APPLICATION_JSON)
                    .post(Entity.json(weAbstimmungku310StimmeAbgeben), WEAbstimmungku310StimmeAbgebenRC.class);
        } else {
            try {
                lWEAbstimmungku310StimmeAbgebenRC = client.target(getPfad("abstimmungku310StimmeAbgeben"))
                        .request(MediaType.APPLICATION_JSON)
                        .post(Entity.json(weAbstimmungku310StimmeAbgeben), WEAbstimmungku310StimmeAbgebenRC.class);

            } catch (Exception e2) {
                CaBug.drucke("Verbindung unterbrochen");
                lWEAbstimmungku310StimmeAbgebenRC = new WEAbstimmungku310StimmeAbgebenRC();
                lWEAbstimmungku310StimmeAbgebenRC.rc = CaFehler.teVerbindungsabbruchWebService;
            }
        }

        ClientLog.ausgabeNl("rc=" + lWEAbstimmungku310StimmeAbgebenRC.getRc()
                + CaFehler.getFehlertext(lWEAbstimmungku310StimmeAbgebenRC.getRc(), 0));
        return lWEAbstimmungku310StimmeAbgebenRC;
    }

    public WEAbstimmungSpeichernRC abstimmungSpeichern(WEAbstimmungSpeichern weAbstimmungSpeichern) {
        ClientLog.ausgabeNl("**********abstimmungSpeichern");
        vorbereitenWELoginVerify(weAbstimmungSpeichern.getWeLoginVerify());
        WEAbstimmungSpeichernRC lWEAbstimmungSpeichernRC = null;

        initialisiereVerbindungsOjekt();
        if (this.IstHttps()) {
            lWEAbstimmungSpeichernRC = client.target(getPfad("abstimmungSpeichern")).request(MediaType.APPLICATION_JSON)
                    .post(Entity.json(weAbstimmungSpeichern), WEAbstimmungSpeichernRC.class);
        } else {
            try {
                lWEAbstimmungSpeichernRC = client.target(getPfad("abstimmungSpeichern"))
                        .request(MediaType.APPLICATION_JSON)
                        .post(Entity.json(weAbstimmungSpeichern), WEAbstimmungSpeichernRC.class);
            } catch (Exception e2) {
                CaBug.drucke("Verbindung unterbrochen");
                lWEAbstimmungSpeichernRC = new WEAbstimmungSpeichernRC();
                lWEAbstimmungSpeichernRC.rc = CaFehler.teVerbindungsabbruchWebService;
            }
        }

        ClientLog.ausgabeNl(
                "rc=" + lWEAbstimmungSpeichernRC.getRc() + CaFehler.getFehlertext(lWEAbstimmungSpeichernRC.getRc(), 0));
        return lWEAbstimmungSpeichernRC;
    }

    public WETabletSetzeStatusRC tabletSetzeStatusAbstimmungAuf(WETabletSetzeStatus weTabletSetzeStatus) {
        ClientLog.ausgabeNl("**********tabletSetzeStatusAbstimmungAuf");
        vorbereitenWELoginVerify(weTabletSetzeStatus.getWeLoginVerify());
        WETabletSetzeStatusRC weTabletSetzeStatusRC = null;

        initialisiereVerbindungsOjekt();
        if (this.IstHttps()) {
            weTabletSetzeStatusRC = client.target(getPfad("tabletSetzeStatusAbstimmungAuf"))
                    .request(MediaType.APPLICATION_JSON)
                    .post(Entity.json(weTabletSetzeStatus), WETabletSetzeStatusRC.class);
        } else {
            try {
                weTabletSetzeStatusRC = client.target(getPfad("tabletSetzeStatusAbstimmungAuf"))
                        .request(MediaType.APPLICATION_JSON)
                        .post(Entity.json(weTabletSetzeStatus), WETabletSetzeStatusRC.class);
            } catch (Exception e2) {
                CaBug.drucke("Verbindung unterbrochen");
                weTabletSetzeStatusRC = new WETabletSetzeStatusRC();
                weTabletSetzeStatusRC.rc = CaFehler.teVerbindungsabbruchWebService;
            }
        }

        ClientLog.ausgabeNl(
                "rc=" + weTabletSetzeStatusRC.getRc() + CaFehler.getFehlertext(weTabletSetzeStatusRC.getRc(), 0));
        return weTabletSetzeStatusRC;
    }

    public WETabletSetzeStatusRC tabletSetzeStatusAbstimmungBeendet(WETabletSetzeStatus weTabletSetzeStatus) {
        ClientLog.ausgabeNl("**********tabletSetzeStatusAbstimmungBeendet");
        vorbereitenWELoginVerify(weTabletSetzeStatus.getWeLoginVerify());
        WETabletSetzeStatusRC weTabletSetzeStatusRC = null;

        initialisiereVerbindungsOjekt();
        if (this.IstHttps()) {
            weTabletSetzeStatusRC = client.target(getPfad("tabletSetzeStatusAbstimmungBeendet"))
                    .request(MediaType.APPLICATION_JSON)
                    .post(Entity.json(weTabletSetzeStatus), WETabletSetzeStatusRC.class);
        } else {
            try {
                weTabletSetzeStatusRC = client.target(getPfad("tabletSetzeStatusAbstimmungBeendet"))
                        .request(MediaType.APPLICATION_JSON)
                        .post(Entity.json(weTabletSetzeStatus), WETabletSetzeStatusRC.class);
            } catch (Exception e2) {
                CaBug.drucke("Verbindung unterbrochen");
                weTabletSetzeStatusRC = new WETabletSetzeStatusRC();
                weTabletSetzeStatusRC.rc = CaFehler.teVerbindungsabbruchWebService;
            }
        }

        ClientLog.ausgabeNl(
                "rc=" + weTabletSetzeStatusRC.getRc() + CaFehler.getFehlertext(weTabletSetzeStatusRC.getRc(), 0));
        return weTabletSetzeStatusRC;
    }

    public WEHVParameterGetRC hvParameterGet(WEHVParameterGet weHVParameterGet) {
        ClientLog.ausgabeNl("**********hvParameterGet");
        vorbereitenWELoginVerify(weHVParameterGet.getWeLoginVerify());
        WEHVParameterGetRC weHVParameterGetRC = null;

        initialisiereVerbindungsOjekt();
        System.out.println("Pfad=" + getPfad("hvParameterGet"));
        if (this.IstHttps()) {
            weHVParameterGetRC = client.target(getPfad("hvParameterGet")).request(MediaType.APPLICATION_JSON)
                    .post(Entity.json(weHVParameterGet), WEHVParameterGetRC.class);
        } else {
            try {
                weHVParameterGetRC = client.target(getPfad("hvParameterGet")).request(MediaType.APPLICATION_JSON)
                        .post(Entity.json(weHVParameterGet), WEHVParameterGetRC.class);
            } catch (Exception e2) {
                CaBug.drucke("Verbindung unterbrochen");
                System.out.println(e2);
                weHVParameterGetRC = new WEHVParameterGetRC();
                weHVParameterGetRC.rc = CaFehler.teVerbindungsabbruchWebService;
            }
        }

        ClientLog.ausgabeNl("rc=" + weHVParameterGetRC.getRc() + CaFehler.getFehlertext(weHVParameterGetRC.getRc(), 0));
        return weHVParameterGetRC;
    }

    public WEGeraeteParameterGetRC geraeteParameterGet(WEGeraeteParameterGet weGeraeteParameterGet) {
        System.out
                .println("geraeteParameterGet weGeraeteParameterGet=" + weGeraeteParameterGet.weLoginVerify.getUser());
        ClientLog.ausgabeNl("**********geraeteParameterGet");
        vorbereitenWELoginVerify(weGeraeteParameterGet.getWeLoginVerify());
        WEGeraeteParameterGetRC weGeraeteParameterGetRC = null;

        initialisiereVerbindungsOjekt();
        System.out.println("Pfad=" + getPfad("geraeteParameterGet"));
        if (this.IstHttps()) {
            try {
                weGeraeteParameterGetRC = client.target(getPfad("geraeteParameterGet"))
                        .request(MediaType.APPLICATION_JSON)
                        .post(Entity.json(weGeraeteParameterGet), WEGeraeteParameterGetRC.class);
            } catch (Exception e2) {
                CaBug.drucke("Zweiter Versuch");
                try {
                    weGeraeteParameterGetRC = client.target(getPfad("geraeteParameterGet"))
                            .request(MediaType.APPLICATION_JSON)
                            .post(Entity.json(weGeraeteParameterGet), WEGeraeteParameterGetRC.class);
                } catch (Exception e3) {
                    CaBug.drucke("001 Verbindung unterbrochen");
                    System.out.println(e3);
                    weGeraeteParameterGetRC = new WEGeraeteParameterGetRC();
                    weGeraeteParameterGetRC.rc = CaFehler.teVerbindungsabbruchWebService;
                }
            }
        } else {
            try {
                weGeraeteParameterGetRC = client.target(getPfad("geraeteParameterGet"))
                        .request(MediaType.APPLICATION_JSON)

                        .post(Entity.json(weGeraeteParameterGet), WEGeraeteParameterGetRC.class);
            } catch (Exception e2) {
                CaBug.drucke("002 Verbindung unterbrochen");
                System.out.println(e2);
                weGeraeteParameterGetRC = new WEGeraeteParameterGetRC();
                weGeraeteParameterGetRC.rc = CaFehler.teVerbindungsabbruchWebService;
            }
        }

        ClientLog.ausgabeNl(
                "rc=" + weGeraeteParameterGetRC.getRc() + CaFehler.getFehlertext(weGeraeteParameterGetRC.getRc(), 0));
        return weGeraeteParameterGetRC;
    }

    public WELeseEmittentenRC leseEmittenten(WELeseEmittenten weLeseEmittenten) {
        ClientLog.ausgabeNl("**********leseEmittenten");
        vorbereitenWELoginVerify(weLeseEmittenten.getWeLoginVerify());
        WELeseEmittentenRC weLeseEmittentenRC = null;

        initialisiereVerbindungsOjekt();
        System.out.println("Pfad=" + getPfad("leseEmittenten"));
        if (this.IstHttps()) {
            weLeseEmittentenRC = client.target(getPfad("leseEmittenten")).request(MediaType.APPLICATION_JSON)
                    .post(Entity.json(weLeseEmittenten), WELeseEmittentenRC.class);
        } else {
            try {
                weLeseEmittentenRC = client.target(getPfad("leseEmittenten")).request(MediaType.APPLICATION_JSON)
                        .post(Entity.json(weLeseEmittenten), WELeseEmittentenRC.class);
            } catch (Exception e2) {
                CaBug.drucke("Verbindung unterbrochen");
                System.out.println(e2);
                weLeseEmittentenRC = new WELeseEmittentenRC();
                weLeseEmittentenRC.rc = CaFehler.teVerbindungsabbruchWebService;
            }
        }

        ClientLog.ausgabeNl("rc=" + weLeseEmittentenRC.getRc() + CaFehler.getFehlertext(weLeseEmittentenRC.getRc(), 0));
        return weLeseEmittentenRC;
    }

    public WEKIAVFuerVollmachtDritteRC kiavFuerVollmachtDritteGet(WEKIAVFuerVollmachtDritte weKIAVFuerVollmachtDritte) {
        ClientLog.ausgabeNl("**********kiavFuerVollmachtDritteGet");
        vorbereitenWELoginVerify(weKIAVFuerVollmachtDritte.getWeLoginVerify());
        WEKIAVFuerVollmachtDritteRC weKIAVFuerVollmachtDritteRC = null;

        initialisiereVerbindungsOjekt();
        if (this.IstHttps()) {
            weKIAVFuerVollmachtDritteRC = client.target(getPfad("kiavFuerVollmachtDritteGet"))
                    .request(MediaType.APPLICATION_JSON)
                    .post(Entity.json(weKIAVFuerVollmachtDritte), WEKIAVFuerVollmachtDritteRC.class);
        } else {
            try {
                weKIAVFuerVollmachtDritteRC = client.target(getPfad("kiavFuerVollmachtDritteGet"))
                        .request(MediaType.APPLICATION_JSON)
                        .post(Entity.json(weKIAVFuerVollmachtDritte), WEKIAVFuerVollmachtDritteRC.class);
            } catch (Exception e2) {
                CaBug.drucke("Verbindung unterbrochen");
                weKIAVFuerVollmachtDritteRC = new WEKIAVFuerVollmachtDritteRC();
                weKIAVFuerVollmachtDritteRC.rc = CaFehler.teVerbindungsabbruchWebService;
            }
        }
        CInjects.weKIAVFuerVollmachtDritteRC = weKIAVFuerVollmachtDritteRC;
        ClientLog.ausgabeNl("rc=" + weKIAVFuerVollmachtDritteRC.getRc()
                + CaFehler.getFehlertext(weKIAVFuerVollmachtDritteRC.getRc(), 0));
        return weKIAVFuerVollmachtDritteRC;
    }

    /**********************************
     * User-Kommunikation
     *********************************************************/
    /*************************************************************************************************************/

    public WEPruefeLoginUserRC pruefeLoginUser(WEPruefeLoginUser wePruefeLoginUser) {
        ClientLog.ausgabeNl("**********pruefeLoginUser");
        vorbereitenWELoginVerify(wePruefeLoginUser.getWeLoginVerify());
        WEPruefeLoginUserRC wePruefeLoginUserRC = null;

        initialisiereVerbindungsOjekt();
        System.out.println("Pfad=" + getPfad("pruefeLoginUser"));
        if (this.IstHttps()) {
            wePruefeLoginUserRC = client.target(getPfad("pruefeLoginUser")).request(MediaType.APPLICATION_JSON)
                    .post(Entity.json(wePruefeLoginUser), WEPruefeLoginUserRC.class);
        } else {
            try {
                wePruefeLoginUserRC = client.target(getPfad("pruefeLoginUser")).request(MediaType.APPLICATION_JSON)
                        .post(Entity.json(wePruefeLoginUser), WEPruefeLoginUserRC.class);
            } catch (Exception e2) {
                CaBug.drucke("Verbindung unterbrochen");
                System.out.println(e2);
                wePruefeLoginUserRC = new WEPruefeLoginUserRC();
                wePruefeLoginUserRC.rc = CaFehler.teVerbindungsabbruchWebService;
            }
        }

        ClientLog.ausgabeNl(
                "rc=" + wePruefeLoginUserRC.getRc() + CaFehler.getFehlertext(wePruefeLoginUserRC.getRc(), 0));
        return wePruefeLoginUserRC;
    }

    public WELoginPasswortAendernRC loginPasswortAendern(WELoginPasswortAendern weLoginPasswortAendern) {
        ClientLog.ausgabeNl("**********loginPasswortAendern");
        vorbereitenWELoginVerify(weLoginPasswortAendern.getWeLoginVerify());
        WELoginPasswortAendernRC weLoginPasswortAendernRC = null;

        initialisiereVerbindungsOjekt();
        System.out.println("Pfad=" + getPfad("loginPasswortAendern"));
        if (this.IstHttps()) {
            weLoginPasswortAendernRC = client.target(getPfad("loginPasswortAendern"))
                    .request(MediaType.APPLICATION_JSON)
                    .post(Entity.json(weLoginPasswortAendern), WELoginPasswortAendernRC.class);
        } else {
            try {
                weLoginPasswortAendernRC = client.target(getPfad("loginPasswortAendern"))
                        .request(MediaType.APPLICATION_JSON)
                        .post(Entity.json(weLoginPasswortAendern), WELoginPasswortAendernRC.class);
            } catch (Exception e2) {
                CaBug.drucke("Verbindung unterbrochen");
                System.out.println(e2);
                weLoginPasswortAendernRC = new WELoginPasswortAendernRC();
                weLoginPasswortAendernRC.rc = CaFehler.teVerbindungsabbruchWebService;
            }
        }

        ClientLog.ausgabeNl(
                "rc=" + weLoginPasswortAendernRC.getRc() + CaFehler.getFehlertext(weLoginPasswortAendernRC.getRc(), 0));
        return weLoginPasswortAendernRC;
    }

    public WELaufScanStartenRC laufScanStarten(WELaufScanStarten weLaufScanStarten) {
        ClientLog.ausgabeNl("**********laufScanStarten");
        vorbereitenWELoginVerify(weLaufScanStarten.getWeLoginVerify());
        WELaufScanStartenRC lWELaufScanStartenRC = null;

        initialisiereVerbindungsOjekt();
        if (this.IstHttps()) {
            lWELaufScanStartenRC = client.target(getPfad("laufScanStarten")).request(MediaType.APPLICATION_JSON)
                    .post(Entity.json(weLaufScanStarten), WELaufScanStartenRC.class);
        } else {
            try {
                lWELaufScanStartenRC = client.target(getPfad("laufScanStarten")).request(MediaType.APPLICATION_JSON)
                        .post(Entity.json(weLaufScanStarten), WELaufScanStartenRC.class);
            } catch (Exception e2) {
                CaBug.drucke("Verbindung unterbrochen");
                lWELaufScanStartenRC = new WELaufScanStartenRC();
                lWELaufScanStartenRC.rc = CaFehler.teVerbindungsabbruchWebService;
            }
        }

        ClientLog.ausgabeNl(
                "rc=" + lWELaufScanStartenRC.getRc() + CaFehler.getFehlertext(lWELaufScanStartenRC.getRc(), 0));
        return lWELaufScanStartenRC;
    }

    public WEAutoTestRC autoTest(WEAutoTest weAutoTest) {
        ClientLog.ausgabeNl("**********autoTest");
        vorbereitenWELoginVerify(weAutoTest.getWeLoginVerify());
        WEAutoTestRC weAutoTestRC = null;

        initialisiereVerbindungsOjekt();
        if (this.IstHttps()) {
            weAutoTestRC = client.target(getPfad("autoTest")).request(MediaType.APPLICATION_JSON)
                    .post(Entity.json(weAutoTest), WEAutoTestRC.class);
        } else {
            try {
                weAutoTestRC = client.target(getPfad("autoTest")).request(MediaType.APPLICATION_JSON)
                        .post(Entity.json(weAutoTest), WEAutoTestRC.class);
            } catch (Exception e2) {
                CaBug.drucke("Verbindung unterbrochen");
                weAutoTestRC = new WEAutoTestRC();
                weAutoTestRC.rc = CaFehler.teVerbindungsabbruchWebService;
            }
        }

        ClientLog.ausgabeNl("rc=" + weAutoTestRC.getRc() + CaFehler.getFehlertext(weAutoTestRC.getRc(), 0));
        return weAutoTestRC;
    }

    public WETabletRuecksetzenRC tabletRuecksetzenPruefen(WETabletRuecksetzen weTabletRuecksetzen) {
        ClientLog.ausgabeNl("**********tabletRuecksetzenPruefen");
        vorbereitenWELoginVerify(weTabletRuecksetzen.getWeLoginVerify());
        WETabletRuecksetzenRC weTabletRuecksetzenRC = null;

        initialisiereVerbindungsOjekt();
        if (this.IstHttps()) {
            weTabletRuecksetzenRC = client.target(getPfad("tabletRuecksetzenPruefen"))
                    .request(MediaType.APPLICATION_JSON)
                    .post(Entity.json(weTabletRuecksetzen), WETabletRuecksetzenRC.class);
        } else {
            try {
                weTabletRuecksetzenRC = client.target(getPfad("tabletRuecksetzenPruefen"))
                        .request(MediaType.APPLICATION_JSON)
                        .post(Entity.json(weTabletRuecksetzen), WETabletRuecksetzenRC.class);
            } catch (Exception e2) {
                CaBug.drucke("Verbindung unterbrochen");
                weTabletRuecksetzenRC = new WETabletRuecksetzenRC();
                weTabletRuecksetzenRC.rc = CaFehler.teVerbindungsabbruchWebService;
            }
        }

        ClientLog.ausgabeNl(
                "rc=" + weTabletRuecksetzenRC.getRc() + CaFehler.getFehlertext(weTabletRuecksetzenRC.getRc(), 0));
        return weTabletRuecksetzenRC;
    }

    // public WETabletRuecksetzenRC tabletRuecksetzenPruefen(WETabletRuecksetzen
    // weTabletRuecksetzen)
    // {
    // ClientLog.ausgabeNl("**********tabletRuecksetzenPruefen");
    // vorbereitenWELoginVerify(weTabletRuecksetzen.getWeLoginVerify());
    // WETabletRuecksetzenRC weTabletRuecksetzenRC=null;
    //
    // initialisiereVerbindungsOjekt();
    // if (this.IstHttps()){
    // weTabletRuecksetzenRC =
    // client.target(getPfad("tabletRuecksetzenPruefen")).request(MediaType.APPLICATION_JSON)
    // .post(Entity.json(weTabletRuecksetzen), WETabletRuecksetzenRC.class);
    // }
    // else{
    // try {
    // weTabletRuecksetzenRC=
    // client.target(getPfad("tabletRuecksetzenPruefen"))
    // .request(MediaType.APPLICATION_JSON)
    // .post(Entity.json(weTabletRuecksetzen), WETabletRuecksetzenRC.class);
    // } catch (Exception e2){
    // CaBug.drucke("Verbindung unterbrochen");
    // weTabletRuecksetzenRC=new WETabletRuecksetzenRC();
    // weTabletRuecksetzenRC.rc=CaFehler.teVerbindungsabbruchWebService;
    // }
    // }
    //
    // ClientLog.ausgabeNl("rc="+weTabletRuecksetzenRC.getRc()+CaFehler.getFehlertext(weTabletRuecksetzenRC.getRc(),
    // 0));
    // return weTabletRuecksetzenRC;
    // }

    public WETabletRuecksetzenRC tabletRuecksetzen(WETabletRuecksetzen weTabletRuecksetzen) {
        ClientLog.ausgabeNl("**********tabletRuecksetzen");
        vorbereitenWELoginVerify(weTabletRuecksetzen.getWeLoginVerify());
        WETabletRuecksetzenRC weTabletRuecksetzenRC = null;

        initialisiereVerbindungsOjekt();
        if (this.IstHttps()) {
            weTabletRuecksetzenRC = client.target(getPfad("tabletRuecksetzen")).request(MediaType.APPLICATION_JSON)
                    .post(Entity.json(weTabletRuecksetzen), WETabletRuecksetzenRC.class);
        } else {
            try {
                weTabletRuecksetzenRC = client.target(getPfad("tabletRuecksetzen")).request(MediaType.APPLICATION_JSON)
                        .post(Entity.json(weTabletRuecksetzen), WETabletRuecksetzenRC.class);
            } catch (Exception e2) {
                CaBug.drucke("Verbindung unterbrochen");
                weTabletRuecksetzenRC = new WETabletRuecksetzenRC();
                weTabletRuecksetzenRC.rc = CaFehler.teVerbindungsabbruchWebService;
            }
        }

        ClientLog.ausgabeNl(
                "rc=" + weTabletRuecksetzenRC.getRc() + CaFehler.getFehlertext(weTabletRuecksetzenRC.getRc(), 0));
        return weTabletRuecksetzenRC;
    }

    public WELaufInfoGetRC laufInfoGet(WELaufInfoGet weLaufInfoGet) {
        ClientLog.ausgabeNl("**********laufInfoGet");
        vorbereitenWELoginVerify(weLaufInfoGet.getWeLoginVerify());
        WELaufInfoGetRC lWELaufInfoGetRC = null;

        initialisiereVerbindungsOjekt();
        if (this.IstHttps()) {
            lWELaufInfoGetRC = client.target(getPfad("laufInfoGet")).request(MediaType.APPLICATION_JSON)
                    .post(Entity.json(weLaufInfoGet), WELaufInfoGetRC.class);
        } else {
            try {
                lWELaufInfoGetRC = client.target(getPfad("laufInfoGet")).request(MediaType.APPLICATION_JSON)
                        .post(Entity.json(weLaufInfoGet), WELaufInfoGetRC.class);
            } catch (Exception e2) {
                CaBug.drucke("Verbindung unterbrochen");
                lWELaufInfoGetRC = new WELaufInfoGetRC();
                lWELaufInfoGetRC.rc = CaFehler.teVerbindungsabbruchWebService;
            }
        }

        ClientLog.ausgabeNl("rc=" + lWELaufInfoGetRC.getRc() + CaFehler.getFehlertext(lWELaufInfoGetRC.getRc(), 0));
        return lWELaufInfoGetRC;
    }

    public WELaufInstiProvStartenRC laufInstiProvStarten(WELaufInstiProvStarten weLaufInstiProvStarten) {
        ClientLog.ausgabeNl("**********laufInstiProvStarten");
        vorbereitenWELoginVerify(weLaufInstiProvStarten.getWeLoginVerify());
        WELaufInstiProvStartenRC lWELaufInstiProvStartenRC = null;

        initialisiereVerbindungsOjekt();
        if (this.IstHttps()) {
            lWELaufInstiProvStartenRC = client.target(getPfad("laufInstiProvStarten"))
                    .request(MediaType.APPLICATION_JSON)
                    .post(Entity.json(weLaufInstiProvStarten), WELaufInstiProvStartenRC.class);
        } else {
            try {
                lWELaufInstiProvStartenRC = client.target(getPfad("laufInstiProvStarten"))
                        .request(MediaType.APPLICATION_JSON)
                        .post(Entity.json(weLaufInstiProvStarten), WELaufInstiProvStartenRC.class);
            } catch (Exception e2) {
                CaBug.drucke("Verbindung unterbrochen");
                lWELaufInstiProvStartenRC = new WELaufInstiProvStartenRC();
                lWELaufInstiProvStartenRC.rc = CaFehler.teVerbindungsabbruchWebService;
            }
        }

        ClientLog.ausgabeNl("rc=" + lWELaufInstiProvStartenRC.getRc()
                + CaFehler.getFehlertext(lWELaufInstiProvStartenRC.getRc(), 0));
        return lWELaufInstiProvStartenRC;
    }

    public WEStubAbstimmungenRC stubAbstimmungen(WEStubAbstimmungen weStubAbstimmungen) {
        ClientLog.ausgabeNl("**********stubAbstimmungen");
        vorbereitenWELoginVerify(weStubAbstimmungen.getWeLoginVerify());
        WEStubAbstimmungenRC lWEStubAbstimmungenRC = null;

        initialisiereVerbindungsOjekt();
        if (this.IstHttps()) {
            lWEStubAbstimmungenRC = client.target(getPfad("stubAbstimmungen")).request(MediaType.APPLICATION_JSON)
                    .post(Entity.json(weStubAbstimmungen), WEStubAbstimmungenRC.class);
        } else {
            try {
                lWEStubAbstimmungenRC = client.target(getPfad("stubAbstimmungen")).request(MediaType.APPLICATION_JSON)
                        .post(Entity.json(weStubAbstimmungen), WEStubAbstimmungenRC.class);
            } catch (Exception e2) {
                CaBug.drucke("Verbindung unterbrochen");
                lWEStubAbstimmungenRC = new WEStubAbstimmungenRC();
                lWEStubAbstimmungenRC.rc = CaFehler.teVerbindungsabbruchWebService;
            }
        }

        ClientLog.ausgabeNl(
                "rc=" + lWEStubAbstimmungenRC.getRc() + CaFehler.getFehlertext(lWEStubAbstimmungenRC.getRc(), 0));
        return lWEStubAbstimmungenRC;
    }

    public WEStubBlSammelkartenRC stubBlSammelkarten(WEStubBlSammelkarten weStubBlSammelkarten) {
        ClientLog.ausgabeNl("**********stubBlSammelkarten");
        vorbereitenWELoginVerify(weStubBlSammelkarten.getWeLoginVerify());
        WEStubBlSammelkartenRC lWEStubBlSammelkartenRC = null;

        initialisiereVerbindungsOjekt();
        if (this.IstHttps()) {
            lWEStubBlSammelkartenRC = client.target(getPfad("stubBlSammelkarten")).request(MediaType.APPLICATION_JSON)
                    .post(Entity.json(weStubBlSammelkarten), WEStubBlSammelkartenRC.class);
        } else {
            try {
                lWEStubBlSammelkartenRC = client.target(getPfad("stubBlSammelkarten"))
                        .request(MediaType.APPLICATION_JSON)
                        .post(Entity.json(weStubBlSammelkarten), WEStubBlSammelkartenRC.class);
            } catch (Exception e2) {
                CaBug.drucke("Verbindung unterbrochen");
                lWEStubBlSammelkartenRC = new WEStubBlSammelkartenRC();
                lWEStubBlSammelkartenRC.rc = CaFehler.teVerbindungsabbruchWebService;
            }
        }

        ClientLog.ausgabeNl(
                "rc=" + lWEStubBlSammelkartenRC.getRc() + CaFehler.getFehlertext(lWEStubBlSammelkartenRC.getRc(), 0));
        return lWEStubBlSammelkartenRC;
    }

    public WEStubMandantAnlegenRC stubMandantAnlegen(WEStubMandantAnlegen weStubMandantAnlegen) {
        ClientLog.ausgabeNl("**********stubMandantAnlegen");
        vorbereitenWELoginVerify(weStubMandantAnlegen.getWeLoginVerify());
        WEStubMandantAnlegenRC lWEStubMandantAnlegenRC = null;

        initialisiereVerbindungsOjekt();
        if (this.IstHttps()) {
            lWEStubMandantAnlegenRC = client.target(getPfad("stubMandantAnlegen")).request(MediaType.APPLICATION_JSON)
                    .post(Entity.json(weStubMandantAnlegen), WEStubMandantAnlegenRC.class);
        } else {
            try {
                lWEStubMandantAnlegenRC = client.target(getPfad("stubMandantAnlegen"))
                        .request(MediaType.APPLICATION_JSON)
                        .post(Entity.json(weStubMandantAnlegen), WEStubMandantAnlegenRC.class);
            } catch (Exception e2) {
                CaBug.drucke("Verbindung unterbrochen");
                lWEStubMandantAnlegenRC = new WEStubMandantAnlegenRC();
                lWEStubMandantAnlegenRC.rc = CaFehler.teVerbindungsabbruchWebService;
            }
        }

        ClientLog.ausgabeNl(
                "rc=" + lWEStubMandantAnlegenRC.getRc() + CaFehler.getFehlertext(lWEStubMandantAnlegenRC.getRc(), 0));
        return lWEStubMandantAnlegenRC;
    }

    public WEStubCtrlLoginRC stubCtrlLogin(WEStubCtrlLogin weStubCtrlLogin) {
        ClientLog.ausgabeNl("**********stubCtrlLogin");
        vorbereitenWELoginVerify(weStubCtrlLogin.getWeLoginVerify());
        WEStubCtrlLoginRC lWEStubCtrlLoginRC = null;

        initialisiereVerbindungsOjekt();
        if (this.IstHttps()) {
            lWEStubCtrlLoginRC = client.target(getPfad("stubCtrlLogin")).request(MediaType.APPLICATION_JSON)
                    .post(Entity.json(weStubCtrlLogin), WEStubCtrlLoginRC.class);
        } else {
            try {
                lWEStubCtrlLoginRC = client.target(getPfad("stubCtrlLogin")).request(MediaType.APPLICATION_JSON)
                        .post(Entity.json(weStubCtrlLogin), WEStubCtrlLoginRC.class);
            } catch (Exception e2) {
                CaBug.drucke("Verbindung unterbrochen");
                lWEStubCtrlLoginRC = new WEStubCtrlLoginRC();
                lWEStubCtrlLoginRC.rc = CaFehler.teVerbindungsabbruchWebService;
            }
        }

        ClientLog.ausgabeNl("rc=" + lWEStubCtrlLoginRC.getRc() + CaFehler.getFehlertext(lWEStubCtrlLoginRC.getRc(), 0));
        return lWEStubCtrlLoginRC;
    }

    public WEStubBlTeilnehmerdatenLesenRC stubBlTeilnehmerdatenLesen(
            WEStubBlTeilnehmerdatenLesen weStubBlTeilnehmerdatenLesen) {
        ClientLog.ausgabeNl("**********stubBlTeilnehmerdatenLesen");
        vorbereitenWELoginVerify(weStubBlTeilnehmerdatenLesen.getWeLoginVerify());
        WEStubBlTeilnehmerdatenLesenRC lweStubBlTeilnehmerdatenLesenRC = null;

        initialisiereVerbindungsOjekt();
        if (this.IstHttps()) {
            lweStubBlTeilnehmerdatenLesenRC = client.target(getPfad("stubBlTeilnehmerdatenLesen"))
                    .request(MediaType.APPLICATION_JSON)
                    .post(Entity.json(weStubBlTeilnehmerdatenLesen), WEStubBlTeilnehmerdatenLesenRC.class);
        } else {
            try {
                lweStubBlTeilnehmerdatenLesenRC = client.target(getPfad("stubBlTeilnehmerdatenLesen"))
                        .request(MediaType.APPLICATION_JSON)
                        .post(Entity.json(weStubBlTeilnehmerdatenLesen), WEStubBlTeilnehmerdatenLesenRC.class);
            } catch (Exception e2) {
                CaBug.drucke("Verbindung unterbrochen");
                lweStubBlTeilnehmerdatenLesenRC = new WEStubBlTeilnehmerdatenLesenRC();
                lweStubBlTeilnehmerdatenLesenRC.rc = CaFehler.teVerbindungsabbruchWebService;
            }
        }

        ClientLog.ausgabeNl("rc=" + lweStubBlTeilnehmerdatenLesenRC.getRc()
                + CaFehler.getFehlertext(lweStubBlTeilnehmerdatenLesenRC.getRc(), 0));
        return lweStubBlTeilnehmerdatenLesenRC;
    }

    public WEStubBlAbstimmungenWeisungenErfassenRC stubBlAbstimmungenWeisungenErfassen(
            WEStubBlAbstimmungenWeisungenErfassen weStubBlAbstimmungenWeisungenErfassen) {
        ClientLog.ausgabeNl("**********stubBlAbstimmungenWeisungenErfassen");
        vorbereitenWELoginVerify(weStubBlAbstimmungenWeisungenErfassen.getWeLoginVerify());
        WEStubBlAbstimmungenWeisungenErfassenRC lweStubBlAbstimmungenWeisungenErfassenRC = null;

        initialisiereVerbindungsOjekt();
        if (this.IstHttps()) {
            lweStubBlAbstimmungenWeisungenErfassenRC = client.target(getPfad("stubBlAbstimmungenWeisungenErfassen"))
                    .request(MediaType.APPLICATION_JSON).post(Entity.json(weStubBlAbstimmungenWeisungenErfassen),
                            WEStubBlAbstimmungenWeisungenErfassenRC.class);
        } else {
            try {
                lweStubBlAbstimmungenWeisungenErfassenRC = client.target(getPfad("stubBlAbstimmungenWeisungenErfassen"))
                        .request(MediaType.APPLICATION_JSON).post(Entity.json(weStubBlAbstimmungenWeisungenErfassen),
                                WEStubBlAbstimmungenWeisungenErfassenRC.class);
            } catch (Exception e2) {
                CaBug.drucke("Verbindung stubBlAbstimmungenWeisungenErfassen");
                lweStubBlAbstimmungenWeisungenErfassenRC = new WEStubBlAbstimmungenWeisungenErfassenRC();
                lweStubBlAbstimmungenWeisungenErfassenRC.rc = CaFehler.teVerbindungsabbruchWebService;
            }
        }

        ClientLog.ausgabeNl("rc=" + lweStubBlAbstimmungenWeisungenErfassenRC.getRc()
                + CaFehler.getFehlertext(lweStubBlAbstimmungenWeisungenErfassenRC.getRc(), 0));
        return lweStubBlAbstimmungenWeisungenErfassenRC;
    }

    public WEStubRepSammelkartenRC stubRepSammelkarten(WEStubRepSammelkarten weStubRepSammelkarten) {
        ClientLog.ausgabeNl("**********stubRepSammelkarten");
        vorbereitenWELoginVerify(weStubRepSammelkarten.getWeLoginVerify());
        WEStubRepSammelkartenRC lweStubRepSammelkartenRC = null;

        initialisiereVerbindungsOjekt();
        if (this.IstHttps()) {
            lweStubRepSammelkartenRC = client.target(getPfad("stubRepSammelkarten")).request(MediaType.APPLICATION_JSON)
                    .post(Entity.json(weStubRepSammelkarten), WEStubRepSammelkartenRC.class);
        } else {
            try {
                lweStubRepSammelkartenRC = client.target(getPfad("stubRepSammelkarten"))
                        .request(MediaType.APPLICATION_JSON)
                        .post(Entity.json(weStubRepSammelkarten), WEStubRepSammelkartenRC.class);
            } catch (Exception e2) {
                CaBug.drucke("Verbindung stubBlAbstimmungenWeisungenErfassen");
                lweStubRepSammelkartenRC = new WEStubRepSammelkartenRC();
                lweStubRepSammelkartenRC.rc = CaFehler.teVerbindungsabbruchWebService;
            }
        }

        ClientLog.ausgabeNl(
                "rc=" + lweStubRepSammelkartenRC.getRc() + CaFehler.getFehlertext(lweStubRepSammelkartenRC.getRc(), 0));
        return lweStubRepSammelkartenRC;
    }

    public WEStubBlInstiRC stubBlInsti(WEStubBlInsti weStubBlInsti) {
        ClientLog.ausgabeNl("**********stubBlInsti");
        vorbereitenWELoginVerify(weStubBlInsti.getWeLoginVerify());
        WEStubBlInstiRC lWEStubBlInstiRC = null;

        initialisiereVerbindungsOjekt();
        if (this.IstHttps()) {
            lWEStubBlInstiRC = client.target(getPfad("stubBlInsti")).request(MediaType.APPLICATION_JSON)
                    .post(Entity.json(weStubBlInsti), WEStubBlInstiRC.class);
        } else {
            try {
                lWEStubBlInstiRC = client.target(getPfad("stubBlInsti")).request(MediaType.APPLICATION_JSON)
                        .post(Entity.json(weStubBlInsti), WEStubBlInstiRC.class);
            } catch (Exception e2) {
                CaBug.drucke("Verbindung unterbrochen");
                lWEStubBlInstiRC = new WEStubBlInstiRC();
                lWEStubBlInstiRC.rc = CaFehler.teVerbindungsabbruchWebService;
            }
        }

        ClientLog.ausgabeNl("rc=" + lWEStubBlInstiRC.getRc() + CaFehler.getFehlertext(lWEStubBlInstiRC.getRc(), 0));
        return lWEStubBlInstiRC;
    }

    public WEStubBlSuchlaufRC stubBlSuchlauf(WEStubBlSuchlauf weBlStubBlSuchlauf) {
        ClientLog.ausgabeNl("**********stubBlSuchlauf");
        vorbereitenWELoginVerify(weBlStubBlSuchlauf.getWeLoginVerify());
        WEStubBlSuchlaufRC lWEBlStubSuchlaufRC = null;

        initialisiereVerbindungsOjekt();
        if (this.IstHttps()) {
            lWEBlStubSuchlaufRC = client.target(getPfad("stubBlSuchlauf")).request(MediaType.APPLICATION_JSON)
                    .post(Entity.json(weBlStubBlSuchlauf), WEStubBlSuchlaufRC.class);
        } else {
            try {
                lWEBlStubSuchlaufRC = client.target(getPfad("stubBlSuchlauf")).request(MediaType.APPLICATION_JSON)
                        .post(Entity.json(weBlStubBlSuchlauf), WEStubBlSuchlaufRC.class);
            } catch (Exception e2) {
                CaBug.drucke("Verbindung unterbrochen");
                lWEBlStubSuchlaufRC = new WEStubBlSuchlaufRC();
                lWEBlStubSuchlaufRC.rc = CaFehler.teVerbindungsabbruchWebService;
            }
        }

        ClientLog.ausgabeNl(
                "rc=" + lWEBlStubSuchlaufRC.getRc() + CaFehler.getFehlertext(lWEBlStubSuchlaufRC.getRc(), 0));
        return lWEBlStubSuchlaufRC;
    }

    public WEStubBlSuchenRC stubBlSuchen(WEStubBlSuchen weBlStubBlSuchen) {
        ClientLog.ausgabeNl("**********stubBlSuchen");
        vorbereitenWELoginVerify(weBlStubBlSuchen.getWeLoginVerify());
        WEStubBlSuchenRC lWEStubBlSuchenRC = null;

        initialisiereVerbindungsOjekt();
        if (this.IstHttps()) {
            lWEStubBlSuchenRC = client.target(getPfad("stubBlSuchen")).request(MediaType.APPLICATION_JSON)
                    .post(Entity.json(weBlStubBlSuchen), WEStubBlSuchenRC.class);
        } else {
            try {
                lWEStubBlSuchenRC = client.target(getPfad("stubBlSuchen")).request(MediaType.APPLICATION_JSON)
                        .post(Entity.json(weBlStubBlSuchen), WEStubBlSuchenRC.class);
            } catch (Exception e2) {
                CaBug.drucke("Verbindung unterbrochen");
                lWEStubBlSuchenRC = new WEStubBlSuchenRC();
                lWEStubBlSuchenRC.rc = CaFehler.teVerbindungsabbruchWebService;
            }
        }

        ClientLog.ausgabeNl("rc=" + lWEStubBlSuchenRC.getRc() + CaFehler.getFehlertext(lWEStubBlSuchenRC.getRc(), 0));
        return lWEStubBlSuchenRC;
    }

    public WEStubBlDrucklaufRC stubBlDrucklauf(WEStubBlDrucklauf weStubBlDrucklauf) {
        ClientLog.ausgabeNl("**********stubBlDrucklauf");
        vorbereitenWELoginVerify(weStubBlDrucklauf.getWeLoginVerify());
        WEStubBlDrucklaufRC lWEStubBlDrucklaufRC = null;

        initialisiereVerbindungsOjekt();
        if (this.IstHttps()) {
            lWEStubBlDrucklaufRC = client.target(getPfad("stubBlDrucklauf")).request(MediaType.APPLICATION_JSON)
                    .post(Entity.json(weStubBlDrucklauf), WEStubBlDrucklaufRC.class);
        } else {
            try {
                lWEStubBlDrucklaufRC = client.target(getPfad("stubBlDrucklauf")).request(MediaType.APPLICATION_JSON)
                        .post(Entity.json(weStubBlDrucklauf), WEStubBlDrucklaufRC.class);
            } catch (Exception e2) {
                CaBug.drucke("Verbindung unterbrochen");
                lWEStubBlDrucklaufRC = new WEStubBlDrucklaufRC();
                lWEStubBlDrucklaufRC.rc = CaFehler.teVerbindungsabbruchWebService;
            }
        }

        ClientLog.ausgabeNl(
                "rc=" + lWEStubBlDrucklaufRC.getRc() + CaFehler.getFehlertext(lWEStubBlDrucklaufRC.getRc(), 0));
        return lWEStubBlDrucklaufRC;
    }

    public WEStubRepSammelAnmeldebogenRC stubRepSammelAnmeldebogen(
            WEStubRepSammelAnmeldebogen weStubRepSammelAnmeldebogen) {
        ClientLog.ausgabeNl("**********stubRepSammelAnmeldebogen");
        vorbereitenWELoginVerify(weStubRepSammelAnmeldebogen.getWeLoginVerify());
        WEStubRepSammelAnmeldebogenRC lWEStubRepSammelAnmeldebogenRC = null;

        initialisiereVerbindungsOjekt();
        if (this.IstHttps()) {
            lWEStubRepSammelAnmeldebogenRC = client.target(getPfad("stubRepSammelAnmeldebogen"))
                    .request(MediaType.APPLICATION_JSON)
                    .post(Entity.json(weStubRepSammelAnmeldebogen), WEStubRepSammelAnmeldebogenRC.class);
        } else {
            try {
                lWEStubRepSammelAnmeldebogenRC = client.target(getPfad("stubRepSammelAnmeldebogen"))
                        .request(MediaType.APPLICATION_JSON)
                        .post(Entity.json(weStubRepSammelAnmeldebogen), WEStubRepSammelAnmeldebogenRC.class);
            } catch (Exception e2) {
                CaBug.drucke("Verbindung unterbrochen");
                lWEStubRepSammelAnmeldebogenRC = new WEStubRepSammelAnmeldebogenRC();
                lWEStubRepSammelAnmeldebogenRC.rc = CaFehler.teVerbindungsabbruchWebService;
            }
        }

        ClientLog.ausgabeNl("rc=" + lWEStubRepSammelAnmeldebogenRC.getRc()
                + CaFehler.getFehlertext(lWEStubRepSammelAnmeldebogenRC.getRc(), 0));
        return lWEStubRepSammelAnmeldebogenRC;
    }

    public WEStubParameterRC stubParameter(WEStubParameter weStubParameter) {
        ClientLog.ausgabeNl("**********stubParameter");
        vorbereitenWELoginVerify(weStubParameter.getWeLoginVerify());
        WEStubParameterRC lWEStubParameterRC = null;

        initialisiereVerbindungsOjekt();
        if (this.IstHttps()) {
            lWEStubParameterRC = client.target(getPfad("stubParameter")).request(MediaType.APPLICATION_JSON)
                    .post(Entity.json(weStubParameter), WEStubParameterRC.class);
        } else {
            try {
                lWEStubParameterRC = client.target(getPfad("stubParameter")).request(MediaType.APPLICATION_JSON)
                        .post(Entity.json(weStubParameter), WEStubParameterRC.class);
            } catch (Exception e2) {
                CaBug.drucke("Verbindung unterbrochen");
                lWEStubParameterRC = new WEStubParameterRC();
                lWEStubParameterRC.rc = CaFehler.teVerbindungsabbruchWebService;
            }
        }

        ClientLog.ausgabeNl("rc=" + lWEStubParameterRC.getRc() + CaFehler.getFehlertext(lWEStubParameterRC.getRc(), 0));
        return lWEStubParameterRC;
    }

    public WEStubBlPersonenprognoseRC stubBlPersonenprognose(WEStubBlPersonenprognose weBlStubBlPersonenprognose) {
        ClientLog.ausgabeNl("**********stubBlPersonenprognose");
        vorbereitenWELoginVerify(weBlStubBlPersonenprognose.getWeLoginVerify());
        WEStubBlPersonenprognoseRC lWEBlStubPersonenprognoseRC = null;

        initialisiereVerbindungsOjekt();
        if (this.IstHttps()) {
            lWEBlStubPersonenprognoseRC = client.target(getPfad("stubBlPersonenprognose"))
                    .request(MediaType.APPLICATION_JSON)
                    .post(Entity.json(weBlStubBlPersonenprognose), WEStubBlPersonenprognoseRC.class);
        } else {
            try {
                lWEBlStubPersonenprognoseRC = client.target(getPfad("stubBlPersonenprognose"))
                        .request(MediaType.APPLICATION_JSON)
                        .post(Entity.json(weBlStubBlPersonenprognose), WEStubBlPersonenprognoseRC.class);
            } catch (Exception e2) {
                e2.printStackTrace();
                CaBug.drucke("Verbindung unterbrochen");
                lWEBlStubPersonenprognoseRC = new WEStubBlPersonenprognoseRC();
                lWEBlStubPersonenprognoseRC.rc = CaFehler.teVerbindungsabbruchWebService;
            }
        }

        ClientLog.ausgabeNl("rc=" + lWEBlStubPersonenprognoseRC.getRc()
                + CaFehler.getFehlertext(lWEBlStubPersonenprognoseRC.getRc(), 0));
        return lWEBlStubPersonenprognoseRC;
    }

    public WEStubBlAufgabenRC stubBlAufgaben(WEStubBlAufgaben weStubBlAufgaben) {
        ClientLog.ausgabeNl("**********stubBlAufgaben");
        vorbereitenWELoginVerify(weStubBlAufgaben.getWeLoginVerify());
        WEStubBlAufgabenRC lWEStubBlAufgabenRC = null;

        initialisiereVerbindungsOjekt();
        if (this.IstHttps()) {
            lWEStubBlAufgabenRC = client.target(getPfad("stubBlAufgaben")).request(MediaType.APPLICATION_JSON)
                    .post(Entity.json(weStubBlAufgaben), WEStubBlAufgabenRC.class);
        } else {
            try {
                lWEStubBlAufgabenRC = client.target(getPfad("stubBlAufgaben")).request(MediaType.APPLICATION_JSON)
                        .post(Entity.json(weStubBlAufgaben), WEStubBlAufgabenRC.class);
            } catch (Exception e2) {
                CaBug.drucke("Verbindung unterbrochen");
                lWEStubBlAufgabenRC = new WEStubBlAufgabenRC();
                lWEStubBlAufgabenRC.rc = CaFehler.teVerbindungsabbruchWebService;
            }
        }

        ClientLog.ausgabeNl(
                "rc=" + lWEStubBlAufgabenRC.getRc() + CaFehler.getFehlertext(lWEStubBlAufgabenRC.getRc(), 0));
        return lWEStubBlAufgabenRC;
    }

    public WEMonitorRC monitor(WEMonitor weMonitor) {
        ClientLog.ausgabeNl("**********monitor");
        vorbereitenWELoginVerify(weMonitor.getWeLoginVerify());
        WEMonitorRC lWEMonitorRC = null;

        initialisiereVerbindungsOjekt();
        if (this.IstHttps()) {
            lWEMonitorRC = client.target(getPfad("monitor")).request(MediaType.APPLICATION_JSON)
                    .post(Entity.json(weMonitor), WEMonitorRC.class);
        } else {
            try {
                lWEMonitorRC = client.target(getPfad("monitor")).request(MediaType.APPLICATION_JSON)
                        .post(Entity.json(weMonitor), WEMonitorRC.class);
            } catch (Exception e2) {
                CaBug.drucke("Verbindung unterbrochen");
                lWEMonitorRC = new WEMonitorRC();
                lWEMonitorRC.rc = CaFehler.teVerbindungsabbruchWebService;
            }
        }

        ClientLog.ausgabeNl("rc=" + lWEMonitorRC.getRc() + CaFehler.getFehlertext(lWEMonitorRC.getRc(), 0));
        return lWEMonitorRC;
    }

    /****************************
     * Für betterVote-Test
     **************************************/

    public String getPfadVote(String dienst) {
        // return "http://127.0.0.1:80/bettervote/api/intern/"+dienst; //Lokal
        return "https://www.portal.better-orange.de/bettervote/api/intern/" + dienst; // Produktion
    }

    public boolean IstHttpsVote() {
        if (ParamInterneKommunikation.webServicePfadZurAuswahl[ParamS.clGlobalVar.webServicePfadNr].startsWith("https")) {
            return true;
        }
        // if (CInjects.pfad.startsWith("https")){return true;}
        return false;
    }

    // static private int internerZaehler=0;

    public WEStimmabgabeRC stimmabgabe(WEStimmabgabe weStimmabgabe) {
        // internerZaehler++;
        // if (internerZaehler % 50 ==0) {
        // System.out.println("stimmabgabe "+internerZaehler);
        // }

        WEStimmabgabeRC lWEStimmabgabeRC = null;

        initialisiereVerbindungsOjektVote();
        if (this.IstHttpsVote()) {
            lWEStimmabgabeRC = client.target(getPfadVote("stimmabgabe")).request(MediaType.APPLICATION_JSON)
                    .post(Entity.json(weStimmabgabe), WEStimmabgabeRC.class);
        } else {
            try {
                lWEStimmabgabeRC = client.target(getPfadVote("stimmabgabe")).request(MediaType.APPLICATION_JSON)
                        .post(Entity.json(weStimmabgabe), WEStimmabgabeRC.class);
            } catch (Exception e2) {
                CaBug.drucke("Verbindung unterbrochen");
                lWEStimmabgabeRC = new WEStimmabgabeRC();
                lWEStimmabgabeRC.rc = CaFehler.teVerbindungsabbruchWebService;
            }
        }

        // ClientLog.ausgabeNl("rc="+lWEStimmabgabeRC.rc+CaFehler.getFehlertext(lWEStimmabgabeRC.rc,
        // 0));
        return lWEStimmabgabeRC;
    }

    public WEStubBlAktienregisterRC stubBlAktienregister(WEStubBlAktienregister weStubBlAktienregister) {
        ClientLog.ausgabeNl("**********aktienregister");
        vorbereitenWELoginVerify(weStubBlAktienregister.getWeLoginVerify());
        WEStubBlAktienregisterRC weStubBlAktienregisterRC = null;

        initialisiereVerbindungsOjekt();

        if (this.IstHttps()) {
            weStubBlAktienregisterRC = client.target(getPfad("stubBlAktienregister")).request(MediaType.APPLICATION_JSON)
                    .post(Entity.json(weStubBlAktienregister), WEStubBlAktienregisterRC.class);
        } else {
            try {
                weStubBlAktienregisterRC = client.target(getPfad("stubBlAktienregister")).request(MediaType.APPLICATION_JSON)
                        .post(Entity.json(weStubBlAktienregister), WEStubBlAktienregisterRC.class);
            } catch (Exception e2) {
                CaBug.drucke("Verbindung unterbrochen");
                e2.printStackTrace();
                weStubBlAktienregisterRC = new WEStubBlAktienregisterRC();
                weStubBlAktienregisterRC.rc = CaFehler.teVerbindungsabbruchWebService;
            }
        }

        ClientLog.ausgabeNl(
                "rc=" + weStubBlAktienregisterRC.getRc() + CaFehler.getFehlertext(weStubBlAktienregisterRC.getRc(), 0));
        return weStubBlAktienregisterRC;
    }

    public WEStubBlTeilnehmerLoginSperreRC stubBlTeilnehmerLoginSperre(WEStubBlTeilnehmerLoginSperre weStubBlTeilnehmerLoginSperre) {
        ClientLog.ausgabeNl("**********stubBlTeilnehmerLoginSperre");
        vorbereitenWELoginVerify(weStubBlTeilnehmerLoginSperre.getWeLoginVerify());
        WEStubBlTeilnehmerLoginSperreRC weStubBlTeilnehmerLoginSperreRC = null;

        initialisiereVerbindungsOjekt();

        if (this.IstHttps()) {
            weStubBlTeilnehmerLoginSperreRC = client.target(getPfad("stubBlTeilnehmerLoginSperre")).request(MediaType.APPLICATION_JSON)
                    .post(Entity.json(weStubBlTeilnehmerLoginSperre), WEStubBlTeilnehmerLoginSperreRC.class);
        } else {
            try {
                weStubBlTeilnehmerLoginSperreRC = client.target(getPfad("stubBlTeilnehmerLoginSperre")).request(MediaType.APPLICATION_JSON)
                        .post(Entity.json(weStubBlTeilnehmerLoginSperre), WEStubBlTeilnehmerLoginSperreRC.class);
            } catch (Exception e2) {
                CaBug.drucke("Verbindung unterbrochen");
                e2.printStackTrace();
                weStubBlTeilnehmerLoginSperreRC = new WEStubBlTeilnehmerLoginSperreRC();
                weStubBlTeilnehmerLoginSperreRC.rc = CaFehler.teVerbindungsabbruchWebService;
            }
        }

        ClientLog.ausgabeNl(
                "rc=" + weStubBlTeilnehmerLoginSperreRC.getRc() + CaFehler.getFehlertext(weStubBlTeilnehmerLoginSperreRC.getRc(), 0));
        return weStubBlTeilnehmerLoginSperreRC;
    }

    public String osstest(String pParameter) {
        ClientLog.ausgabeNl("**********osstest");

        String rcString="";
        initialisiereVerbindungsOjekt();

        if (this.IstHttps()) {
            rcString = client.target(getPfad("osstest")).request(MediaType.APPLICATION_JSON)
                    .post(Entity.json(pParameter), String.class);
        } else {
            try {
                rcString = client.target(getPfad("osstest")).request(MediaType.APPLICATION_JSON)
                        .post(Entity.json(pParameter), String.class);
            } catch (Exception e2) {
                CaBug.drucke("Verbindung unterbrochen");
                e2.printStackTrace();
                rcString = "Fehler";
            }
        }

        return rcString;
    }

    public String osstest1(String pParameter) {
        ClientLog.ausgabeNl("**********osstest1");

        String rcString="";
        initialisiereVerbindungsOjekt();

        if (this.IstHttps()) {
            rcString = client.target(getPfad("osstest1")).request(MediaType.APPLICATION_JSON)
                    .post(Entity.json(pParameter), String.class);
        } else {
            try {
                rcString = client.target(getPfad("osstest1")).request(MediaType.APPLICATION_JSON)
                        .post(Entity.json(pParameter), String.class);
            } catch (Exception e2) {
                CaBug.drucke("Verbindung unterbrochen");
                e2.printStackTrace();
                rcString = "Fehler";
            }
        }

        return rcString;
    }

    public String osstest2(String pParameter) {
        ClientLog.ausgabeNl("**********osstest2");

        String rcString="";
        initialisiereVerbindungsOjekt();

        if (this.IstHttps()) {
            rcString = client.target(getPfad("osstest2")).request(MediaType.APPLICATION_JSON)
                    .post(Entity.json(pParameter), String.class);
        } else {
            try {
                rcString = client.target(getPfad("osstest2")).request(MediaType.APPLICATION_JSON)
                        .post(Entity.json(pParameter), String.class);
            } catch (Exception e2) {
                CaBug.drucke("Verbindung unterbrochen");
                e2.printStackTrace();
                rcString = "Fehler";
            }
        }

        return rcString;
    }

    public String osstest3(String pParameter) {
        ClientLog.ausgabeNl("**********osstest3");

        String rcString="";
        initialisiereVerbindungsOjekt();

        if (this.IstHttps()) {
            rcString = client.target(getPfad("osstest3")).request(MediaType.APPLICATION_JSON)
                    .post(Entity.json(pParameter), String.class);
        } else {
            try {
                rcString = client.target(getPfad("osstest3")).request(MediaType.APPLICATION_JSON)
                        .post(Entity.json(pParameter), String.class);
            } catch (Exception e2) {
                CaBug.drucke("Verbindung unterbrochen");
                e2.printStackTrace();
                rcString = "Fehler";
            }
        }

        return rcString;
    }

    public WEStubBlMeldungenRC stubBlMeldungen(WEStubBlMeldungen weStubBlMeldungen) {
        ClientLog.ausgabeNl("**********Meldungen");
        vorbereitenWELoginVerify(weStubBlMeldungen.getWeLoginVerify());
        WEStubBlMeldungenRC weStubBlMeldungenRC = null;

        initialisiereVerbindungsOjekt();

        if (this.IstHttps()) {
            weStubBlMeldungenRC = client.target(getPfad("stubBlMeldungen")).request(MediaType.APPLICATION_JSON)
                    .post(Entity.json(weStubBlMeldungen), WEStubBlMeldungenRC.class);
        } else {
            try {
                weStubBlMeldungenRC = client.target(getPfad("stubBlMeldungen")).request(MediaType.APPLICATION_JSON)
                        .post(Entity.json(weStubBlMeldungen), WEStubBlMeldungenRC.class);
            } catch (Exception e2) {
                CaBug.drucke("Verbindung unterbrochen");
                e2.printStackTrace();
                weStubBlMeldungenRC = new WEStubBlMeldungenRC();
                weStubBlMeldungenRC.rc = CaFehler.teVerbindungsabbruchWebService;
            }
        }

        ClientLog.ausgabeNl(
                "rc=" + weStubBlMeldungenRC.getRc() + CaFehler.getFehlertext(weStubBlMeldungenRC.getRc(), 0));
        return weStubBlMeldungenRC;
    }

    public WEStubBlInhaberImportRC stubBlInhaberImport(WEStubBlInhaberImport weStubBlInhaberImport) {
        ClientLog.ausgabeNl("**********InhaberImport");
        vorbereitenWELoginVerify(weStubBlInhaberImport.getWeLoginVerify());
        WEStubBlInhaberImportRC weStubBlInhaberImportRC = null;

        initialisiereVerbindungsOjekt();

        if (this.IstHttps()) {
            weStubBlInhaberImportRC = client.target(getPfad("stubBlInhaberImport")).request(MediaType.APPLICATION_JSON)
                    .post(Entity.json(weStubBlInhaberImport), WEStubBlInhaberImportRC.class);
        } else {
            try {
                weStubBlInhaberImportRC = client.target(getPfad("stubBlInhaberImport")).request(MediaType.APPLICATION_JSON)
                        .post(Entity.json(weStubBlInhaberImport), WEStubBlInhaberImportRC.class);
            } catch (Exception e2) {
                CaBug.drucke("Verbindung unterbrochen");
                e2.printStackTrace();
                weStubBlInhaberImportRC = new WEStubBlInhaberImportRC();
                weStubBlInhaberImportRC.rc = CaFehler.teVerbindungsabbruchWebService;
            }
        }

        ClientLog.ausgabeNl(
                "rc=" + weStubBlInhaberImportRC.getRc() + CaFehler.getFehlertext(weStubBlInhaberImportRC.getRc(), 0));
        return weStubBlInhaberImportRC;
    }

    public WEStubBlWillenserklaerungBatchRC stubBlWillenserklaerungBatch(WEStubBlWillenserklaerungBatch weStubBlWillenserklaerungBatch) {
        ClientLog.ausgabeNl("**********BlWillenserklaerungBatch");
        vorbereitenWELoginVerify(weStubBlWillenserklaerungBatch.getWeLoginVerify());
        WEStubBlWillenserklaerungBatchRC weStubBlWillenserklaerungBatchRC = null;

        initialisiereVerbindungsOjekt();

        if (this.IstHttps()) {
            weStubBlWillenserklaerungBatchRC = client.target(getPfad("stubBlWillenserklaerungBatch")).request(MediaType.APPLICATION_JSON)
                    .post(Entity.json(weStubBlWillenserklaerungBatch), WEStubBlWillenserklaerungBatchRC.class);
        } else {
            try {
                weStubBlWillenserklaerungBatchRC = client.target(getPfad("stubBlWillenserklaerungBatch")).request(MediaType.APPLICATION_JSON)
                        .post(Entity.json(weStubBlWillenserklaerungBatch), WEStubBlWillenserklaerungBatchRC.class);
            } catch (Exception e2) {
                CaBug.drucke("Verbindung unterbrochen");
                e2.printStackTrace();
                weStubBlWillenserklaerungBatchRC = new WEStubBlWillenserklaerungBatchRC();
                weStubBlWillenserklaerungBatchRC.rc = CaFehler.teVerbindungsabbruchWebService;
            }
        }

        ClientLog.ausgabeNl(
                "rc=" + weStubBlWillenserklaerungBatchRC.getRc() + CaFehler.getFehlertext(weStubBlWillenserklaerungBatchRC.getRc(), 0));
        return weStubBlWillenserklaerungBatchRC;
    }

    public WEStubBlStimmkartendruckRC stubBlstimmkartendruck(WEStubBlStimmkartendruck weStubBlStimmkartendruck) {
        ClientLog.ausgabeNl("**********BlStimmkartendruck");
        vorbereitenWELoginVerify(weStubBlStimmkartendruck.getWeLoginVerify());
        WEStubBlStimmkartendruckRC weStubBlStimmkartendruckRC = null;

        initialisiereVerbindungsOjekt();

        if (this.IstHttps()) {
            weStubBlStimmkartendruckRC = client.target(getPfad("stubBlstimmkartendruck")).request(MediaType.APPLICATION_JSON)
                    .post(Entity.json(weStubBlStimmkartendruck), WEStubBlStimmkartendruckRC.class);
        } else {
            try {
                weStubBlStimmkartendruckRC = client.target(getPfad("stubBlstimmkartendruck")).request(MediaType.APPLICATION_JSON)
                        .post(Entity.json(weStubBlStimmkartendruck), WEStubBlStimmkartendruckRC.class);
            } catch (Exception e2) {
                CaBug.drucke("Verbindung unterbrochen");
                e2.printStackTrace();
                weStubBlStimmkartendruckRC = new WEStubBlStimmkartendruckRC();
                weStubBlStimmkartendruckRC.rc = CaFehler.teVerbindungsabbruchWebService;
            }
        }

        ClientLog.ausgabeNl(
                "rc=" + weStubBlStimmkartendruckRC.getRc() + CaFehler.getFehlertext(weStubBlStimmkartendruckRC.getRc(), 0));
        return weStubBlStimmkartendruckRC;
    }

    public WEStubBlHybridMitgliederRC stubBlHybridMitglieder(WEStubBlHybridMitglieder weStubBlHybridMitglieder) {
        ClientLog.ausgabeNl("**********BlHybridMitglieder");
        vorbereitenWELoginVerify(weStubBlHybridMitglieder.getWeLoginVerify());
        WEStubBlHybridMitgliederRC weStubBlHybridMitgliederRC = null;

        initialisiereVerbindungsOjekt();

        if (this.IstHttps()) {
            weStubBlHybridMitgliederRC = client.target(getPfad("stubBlHybridMitglieder")).request(MediaType.APPLICATION_JSON)
                    .post(Entity.json(weStubBlHybridMitglieder), WEStubBlHybridMitgliederRC.class);
        } else {
            try {
                weStubBlHybridMitgliederRC = client.target(getPfad("stubBlHybridMitglieder")).request(MediaType.APPLICATION_JSON)
                        .post(Entity.json(weStubBlHybridMitglieder), WEStubBlHybridMitgliederRC.class);
            } catch (Exception e2) {
                CaBug.drucke("Verbindung unterbrochen");
                e2.printStackTrace();
                weStubBlHybridMitgliederRC = new WEStubBlHybridMitgliederRC();
                weStubBlHybridMitgliederRC.rc = CaFehler.teVerbindungsabbruchWebService;
            }
        }

        ClientLog.ausgabeNl(
                "rc=" + weStubBlHybridMitgliederRC.getRc() + CaFehler.getFehlertext(weStubBlHybridMitgliederRC.getRc(), 0));
        return weStubBlHybridMitgliederRC;
    }

    
    public WEStubMailingRC stubMailing(WEStubMailing weStubMailing) {
        ClientLog.ausgabeNl("**********stubAbstimmungen");
        vorbereitenWELoginVerify(weStubMailing.getWeLoginVerify());
        WEStubMailingRC lWEStubMailingRC = null;

        initialisiereVerbindungsOjekt();
        if (this.IstHttps()) {
            lWEStubMailingRC = client.target(getPfad("stubMailing")).request(MediaType.APPLICATION_JSON)
                    .post(Entity.json(weStubMailing), WEStubMailingRC.class);
        } else {
            try {
                lWEStubMailingRC = client.target(getPfad("stubMailing")).request(MediaType.APPLICATION_JSON)
                        .post(Entity.json(weStubMailing), WEStubMailingRC.class);
            } catch (Exception e2) {
                CaBug.drucke("Verbindung unterbrochen");
                lWEStubMailingRC = new WEStubMailingRC();
                lWEStubMailingRC.rc = CaFehler.teVerbindungsabbruchWebService;
            }
        }

        ClientLog.ausgabeNl(
                "rc=" + lWEStubMailingRC.getRc() + CaFehler.getFehlertext(lWEStubMailingRC.getRc(), 0));
        return lWEStubMailingRC;
    }

    public WEStubParamStruktRC stubParamStrukt(WEStubParamStrukt weStubParamStrukt) {
        ClientLog.ausgabeNl("**********stubParamStrukt");
        vorbereitenWELoginVerify(weStubParamStrukt.getWeLoginVerify());
        WEStubParamStruktRC lWEStubParamStruktRC = null;

        initialisiereVerbindungsOjekt();
        if (this.IstHttps()) {
            lWEStubParamStruktRC = client.target(getPfad("stubParamStrukt")).request(MediaType.APPLICATION_JSON)
                    .post(Entity.json(weStubParamStrukt), WEStubParamStruktRC.class);
        } else {
            try {
                lWEStubParamStruktRC = client.target(getPfad("stubParamStrukt")).request(MediaType.APPLICATION_JSON)
                        .post(Entity.json(weStubParamStrukt), WEStubParamStruktRC.class);
            } catch (Exception e2) {
                CaBug.drucke("Verbindung unterbrochen");
                lWEStubParamStruktRC = new WEStubParamStruktRC();
                lWEStubParamStruktRC.rc = CaFehler.teVerbindungsabbruchWebService;
            }
        }

        ClientLog.ausgabeNl(
                "rc=" + lWEStubParamStruktRC.getRc() + CaFehler.getFehlertext(lWEStubParamStruktRC.getRc(), 0));
        return lWEStubParamStruktRC;
    }
  
}
