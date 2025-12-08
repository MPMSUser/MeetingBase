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
package de.meetingapps.meetingportal.meetComReports;

import java.util.LinkedList;
import java.util.List;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaDatumZeit;
import de.meetingapps.meetingportal.meetComAllg.CaString;
import de.meetingapps.meetingportal.meetComBl.BlDrucklauf;
import de.meetingapps.meetingportal.meetComBl.BlInsti;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclAktienregister;
import de.meetingapps.meetingportal.meetComEntities.EclDrucklauf;
import de.meetingapps.meetingportal.meetComEntities.EclInsti;
import de.meetingapps.meetingportal.meetComEntities.EclInstiBestandsZuordnung;
import de.meetingapps.meetingportal.meetComKonst.KonstVerarbeitungslaufArt;
import de.meetingapps.meetingportal.meetComStub.StubRoot;
import de.meetingapps.meetingportal.meetComStub.WEStubRepSammelAnmeldebogen;
import de.meetingapps.meetingportal.meetComStub.WEStubRepSammelAnmeldebogenRC;
import de.meetingapps.meetingportal.meetComWE.WELoginVerify;
import de.meetingapps.meetingportal.meetingCoreReport.RpDrucken;
import de.meetingapps.meetingportal.meetingCoreReport.RpVariablen;

public class RepSammelAnmeldebogen extends StubRoot {

    private int logDrucken = 3;

    public RepSammelAnmeldebogen(boolean pIstServer, DbBundle pDbBundle) {
        super(pIstServer, pDbBundle);
    }

    private RpVariablen rpVariablen = null;
    private RpDrucken lRpDrucken = null;
    private int lDrucklaufNr;

    private int anzahlSaetzeNeu = 0;
    private int anzahlSaetzeAlt = 0;

    private List<EclInstiBestandsZuordnung> instiBestandsZuordnungNeuArray = null;
    private List<EclAktienregister> aktienregisterNeuArray = null;

    private List<EclInstiBestandsZuordnung> instiBestandsZuordnungAltArray = null;
    private List<EclAktienregister> aktienregisterAltArray = null;

    /**Druck Sammelanmeldebogen - mit Drucklaufverwaltung
     * drucklaufNr==0 => neuen Drucklauf erzeugen
     * 
     *  Return-Wert=Anzahl der Sätze insgesamt
     *  
     *  Sonstige Return-Variablen:
     *  rcListeDerExportDateien=Liste der erzeugten Export-Dateien*/
    public int sammelAnmeldebogenDrucklauf(RpDrucken rpDrucken, String lfdNummer, int drucklaufNr, int arbeitsplatznr,
            int benutzernr, EclInsti pEclInsti) {
        CaBug.druckeLog("RepSammelAnmeldebogen.sammelAnmeldebogenDrucklauf", logDrucken, 3);
        lRpDrucken = rpDrucken;

        if (drucklaufNr == 0) {/*Neuen Drucklauf erzeugen*/
            CaBug.druckeLog("neuen Drucklauf erzeugen", logDrucken, 10);
            anzahlSaetzeNeu = sammelAnmeldebogenDrucklauf_sub_erzeugeNeuenDrucklauf(arbeitsplatznr, benutzernr,
                    pEclInsti);
            drucklaufNr = neuerDrucklaufNr;
        }

        lDrucklaufNr = drucklaufNr;
        CaBug.druckeLog("drucklaufNr=" + drucklaufNr, logDrucken, 10);

        sammelAnmeldebogenDrucklauf_sub_leseDaten(pEclInsti.ident, lDrucklaufNr);
        if (anzahlSaetzeNeu == 0) {
            return 1;
        }

        rpVariablen = new RpVariablen(lDbBundle);
        rpVariablen.sammelAnmeldebogen(lfdNummer, rpDrucken);

        rpVariablen.fuelleVariable(lRpDrucken, "Verarbeitungslauf", Integer.toString(drucklaufNr));
        rpVariablen.fuelleVariable(lRpDrucken, "Verarbeitungsstand", CaDatumZeit.DatumZeitStringFuerAnzeigeNeu());
        rpVariablen.fuelleVariable(lRpDrucken, "InstiBezeichnung", pEclInsti.kurzBezeichnung);

        rpDrucken.startListe();

        if (anzahlSaetzeAlt > 0) {
            druckeZwischenUberschriftAltOderNeu(1);
            druckeElemente(1, instiBestandsZuordnungAltArray, aktienregisterAltArray);
        }

        druckeZwischenUberschriftAltOderNeu(2);
        druckeElemente(2, instiBestandsZuordnungNeuArray, aktienregisterNeuArray);

        rpDrucken.endeListe();

        return 1;
    }

    public int neuerDrucklaufNr = 0;

    /*1*/
    public int sammelAnmeldebogenDrucklauf_sub_erzeugeNeuenDrucklauf(int arbeitsplatznr, int benutzernr,
            EclInsti pEclInsti) {

        EclDrucklauf lDrucklauf = null;

        if (verwendeWebService()) {
            WEStubRepSammelAnmeldebogen weStubRepSammelAnmeldebogen = new WEStubRepSammelAnmeldebogen();
            weStubRepSammelAnmeldebogen.stubFunktion = 1;
            weStubRepSammelAnmeldebogen.arbeitsplatznr = arbeitsplatznr;
            weStubRepSammelAnmeldebogen.benutzernr = benutzernr;
            weStubRepSammelAnmeldebogen.pEclInsti = pEclInsti;
            WELoginVerify weLoginVerify = new WELoginVerify();
            weStubRepSammelAnmeldebogen.setWeLoginVerify(weLoginVerify);

            WEStubRepSammelAnmeldebogenRC weStubRepSammelAnmeldebogenRC = wsClient
                    .stubRepSammelAnmeldebogen(weStubRepSammelAnmeldebogen);

            if (weStubRepSammelAnmeldebogenRC.rc < 1) {
                return weStubRepSammelAnmeldebogenRC.rc;
            }

            neuerDrucklaufNr = weStubRepSammelAnmeldebogenRC.neuerDrucklaufNr;
            CaBug.druckeLog(
                    "sammelAnmeldebogenDrucklauf_sub_erzeugeNeuenDrucklauf neuerDrucklaufNr=" + neuerDrucklaufNr,
                    logDrucken, 10);
            return weStubRepSammelAnmeldebogenRC.anzahlSaetze;
        }

        BlDrucklauf blDrucklauf = new BlDrucklauf(istServer, lDbBundle);
        blDrucklauf.erzeugeNeuenDrucklauf(arbeitsplatznr, benutzernr, KonstVerarbeitungslaufArt.sammelAnmeldeBogen,
                pEclInsti.ident);
        lDrucklauf = blDrucklauf.rcDrucklauf;
        neuerDrucklaufNr = lDrucklauf.drucklaufNr;

        dbOpenUndWeitere();
        int anzahl = lDbBundle.dbInstiBestandsZuordnung.updateSammelAnmeldebogenErstellt(pEclInsti.ident,
                neuerDrucklaufNr);
        lDrucklauf.anzahlSaetze = anzahl;
        dbClose();

        blDrucklauf.updateAnzDrucklauf(anzahl);
        return anzahl;
    }

    /**Liest die bereits gedruckten Aktienregisterzuordnungen und die jetzt neu gedruckten
     * Aktienregisterzuordnungen ein.
     * 
     * Füllt
     * anzahlSaetzeNeu, anzahlSaetzeAlt
     * instiBestandsZuordnungNeuArray, aktienregisterNeuArray, instiBestandsZuordnungAltArray, aktienregisterAltArray 
     */
    private int sammelAnmeldebogenDrucklauf_sub_leseDaten(int pInstiIdent, int pDrucklaufNr) {

        BlInsti blInsti = new BlInsti(istServer, lDbBundle);
        blInsti.fuelleInstiBestandsZuordnung(pInstiIdent);
        int anz = blInsti.rcRegInstiBestandsZuordnung.length;

        CaBug.druckeLog("blInsti anz=" + anz, logDrucken, 5);

        anzahlSaetzeNeu = 0;
        instiBestandsZuordnungNeuArray = new LinkedList<EclInstiBestandsZuordnung>();
        aktienregisterNeuArray = new LinkedList<EclAktienregister>();

        anzahlSaetzeAlt = 0;
        instiBestandsZuordnungAltArray = new LinkedList<EclInstiBestandsZuordnung>();
        aktienregisterAltArray = new LinkedList<EclAktienregister>();

        for (int i = 0; i < anz; i++) {
            EclInstiBestandsZuordnung lInstiBestandsZuordnung = blInsti.rcRegInstiBestandsZuordnung[i];
            EclAktienregister lAktienregister = blInsti.rcRegAktienregister[i];
            boolean inNeu = false;
            boolean inAlt = false;
            if (lDrucklaufNr < 0) { //Alle drucken
                inNeu = true;
            } else {
                if (lInstiBestandsZuordnung.verarbeitetSammelAnmeldungGedruckt != 0) {
                    /**Falls noch = 0, dann sind die neu dazugekommen seit der Erzeung und werden nicht berücksichtigt*/
                    if (lInstiBestandsZuordnung.verarbeitetSammelAnmeldungGedruckt == lDrucklaufNr) {
                        inNeu = true;
                    }
                    if (lInstiBestandsZuordnung.verarbeitetSammelAnmeldungGedruckt < lDrucklaufNr) {
                        inAlt = true;
                    }
                }
            }
            if (inNeu) {
                instiBestandsZuordnungNeuArray.add(lInstiBestandsZuordnung);
                aktienregisterNeuArray.add(lAktienregister);
                anzahlSaetzeNeu++;
            }
            if (inAlt) {
                instiBestandsZuordnungAltArray.add(lInstiBestandsZuordnung);
                aktienregisterAltArray.add(lAktienregister);
                anzahlSaetzeAlt++;
            }
        }
        return 1;
    }

    /**Druckt eine Zwischenzeile für "bereits ausgedruckte" oder "neue"*/
    private void druckeZwischenUberschriftAltOderNeu(int pAltOderNeu) {
        String hZeilenart = "";
        if (pAltOderNeu == 1) {
            hZeilenart = "1";
        } else {
            hZeilenart = "3";
        }
        rpVariablen.fuelleFeld(lRpDrucken, "Zeilenart", hZeilenart);
        lRpDrucken.druckenListe();
    }

    private void druckeElemente(int pAltOderNeu, List<EclInstiBestandsZuordnung> instiBestandsZuordnungArray,
            List<EclAktienregister> aktienregisterArray) {
        for (int i = 0; i < instiBestandsZuordnungArray.size(); i++) {
            EclInstiBestandsZuordnung lInstiBestandsZuordnung = instiBestandsZuordnungArray.get(i);
            EclAktienregister lAktienregisterArray = aktienregisterArray.get(i);

            String hZeilenart = "";
            if (pAltOderNeu == 1) {
                hZeilenart = "2";
            } else {
                hZeilenart = "4";
            }
            rpVariablen.fuelleFeld(lRpDrucken, "Zeilenart", hZeilenart);

            rpVariablen.fuelleFeld(lRpDrucken, "Aktionaer.Aktionaersnummer", lAktienregisterArray.aktionaersnummer);
            rpVariablen.fuelleFeld(lRpDrucken, "Aktionaer.NameKomplett", lAktienregisterArray.nameKomplett);
            rpVariablen.fuelleFeld(lRpDrucken, "Aktionaer.Ort", lAktienregisterArray.ort);
            rpVariablen.fuelleFeld(lRpDrucken, "Aktionaer.StimmenGesamt", Long.toString(lAktienregisterArray.stimmen));
            rpVariablen.fuelleFeld(lRpDrucken, "Aktionaer.StimmenGesamtDE",
                    CaString.toStringDE(lAktienregisterArray.stimmen));
            if (lInstiBestandsZuordnung.zugeordneteStimmen == -1) {
                rpVariablen.fuelleFeld(lRpDrucken, "Aktionaer.StimmenAnteil",
                        Long.toString(lAktienregisterArray.stimmen));
                rpVariablen.fuelleFeld(lRpDrucken, "Aktionaer.StimmenAnteilDE",
                        CaString.toStringDE(lAktienregisterArray.stimmen));
            } else {
                rpVariablen.fuelleFeld(lRpDrucken, "Aktionaer.StimmenAnteil",
                        Long.toString(lInstiBestandsZuordnung.zugeordneteStimmen));
                rpVariablen.fuelleFeld(lRpDrucken, "Aktionaer.StimmenAnteilDE",
                        CaString.toStringDE(lInstiBestandsZuordnung.zugeordneteStimmen));
            }

            lRpDrucken.druckenListe();
        }

    }
}
