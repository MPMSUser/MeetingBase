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

import java.util.List;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComBVerwaltung.BvReload;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclAbstimmung;
import de.meetingapps.meetingportal.meetComEntities.EclAbstimmungZuAbstimmungsblock;
import de.meetingapps.meetingportal.meetComEntities.EclAbstimmungenZuStimmkarte;
import de.meetingapps.meetingportal.meetComEntities.EclAbstimmungsblock;
import de.meetingapps.meetingportal.meetComEntities.EclStimmkarteInhalt;
import de.meetingapps.meetingportal.meetComKonst.KonstDBAbstimmungen;
import de.meetingapps.meetingportal.meetComWE.WELoginVerify;

public class StubAbstimmungen extends StubRoot {

    private int logDrucken = 3;

    public StubAbstimmungen(boolean pIstServer, DbBundle pDbBundle) {
        super(pIstServer, pDbBundle);
    }

    /*1*/
    public int speichernVeraenderteAbstimmungenAbhaengigVonFilterTyp(int filterTyp, EclAbstimmung[] pAbstimmungen,
            EclAbstimmungZuAbstimmungsblock[] angezeigteAbstimmungZuAbstimmungsblock,
            EclAbstimmungenZuStimmkarte[] angezeigteAbstimmungZuStimmkarte, boolean[] pAbstimmungWurdeVeraendert) {

        if (pAbstimmungWurdeVeraendert == null) {
            return 1;
        }
        int anz = pAbstimmungWurdeVeraendert.length;

        if (verwendeWebService()) {
            WEStubAbstimmungen weStubAbstimmungen = new WEStubAbstimmungen();
            weStubAbstimmungen.stubFunktion = 1;
            weStubAbstimmungen.filterTyp = filterTyp;
            weStubAbstimmungen.abstimmungenArray = pAbstimmungen;
            weStubAbstimmungen.angezeigteAbstimmungZuAbstimmungsblock = angezeigteAbstimmungZuAbstimmungsblock;
            weStubAbstimmungen.angezeigteAbstimmungZuStimmkarte = angezeigteAbstimmungZuStimmkarte;
            weStubAbstimmungen.abstimmungWurdeVeraendert = pAbstimmungWurdeVeraendert;

            WELoginVerify weLoginVerify = new WELoginVerify();
            weStubAbstimmungen.setWeLoginVerify(weLoginVerify);

            WEStubAbstimmungenRC weStubAbstimmungenRC = wsClient.stubAbstimmungen(weStubAbstimmungen);

            if (weStubAbstimmungenRC.rc < 1) {
                return weStubAbstimmungenRC.rc;
            }

            for (int i = 0; i < anz; i++) {
                pAbstimmungWurdeVeraendert[i] = weStubAbstimmungenRC.abstimmungWurdeVeraendert[i];
            }
            return weStubAbstimmungenRC.rc;
        }

        dbOpen();
        for (int i = 0; i < anz; i++) {
            if (pAbstimmungWurdeVeraendert[i] == true) {
                int rc = 1;
                rc = lDbBundle.dbAbstimmungen.update(pAbstimmungen[i]);
                if (rc < 1) {
                    dbClose();
                    return -1;
                }
                switch (filterTyp) {
                case 1:
                    rc = lDbBundle.dbAbstimmungZuAbstimmungsblock.update(angezeigteAbstimmungZuAbstimmungsblock[i]);
                    break;
                case 2:
                    rc = lDbBundle.dbAbstimmungenZuStimmkarte.update(angezeigteAbstimmungZuStimmkarte[i]);
                    break;
                }
                if (rc < 1) {
                    dbClose();
                    return -1;
                }
                pAbstimmungWurdeVeraendert[i] = false;
            }
        }

        dbClose();
        return 1;
    }

    /*2*/
    /**Verwendung in CtrlParameterAbstimmung: Speichern aller EclAbstimmung in pAbstimmungen, bei denen das
     * entsprechende pAbstimmungWurdeVeraendert auf true steht (steht anschließend wieder auf false).
     * rc=-1 => wurde von anderem User verändert, sonst 1
     */
    public int speichernVeraenderteAngezeigteAbstimmungen(EclAbstimmung[] pAbstimmungen,
            boolean[] pAbstimmungWurdeVeraendert) {
        if (pAbstimmungWurdeVeraendert == null) {
            return 1;
        }
        int anz = pAbstimmungWurdeVeraendert.length;

        if (verwendeWebService()) {
            WEStubAbstimmungen weStubAbstimmungen = new WEStubAbstimmungen();
            weStubAbstimmungen.stubFunktion = 2;
            weStubAbstimmungen.abstimmungenArray = pAbstimmungen;
            weStubAbstimmungen.abstimmungWurdeVeraendert = pAbstimmungWurdeVeraendert;

            WELoginVerify weLoginVerify = new WELoginVerify();
            weStubAbstimmungen.setWeLoginVerify(weLoginVerify);

            WEStubAbstimmungenRC weStubAbstimmungenRC = wsClient.stubAbstimmungen(weStubAbstimmungen);

            if (weStubAbstimmungenRC.rc < 1) {
                return weStubAbstimmungenRC.rc;
            }

            for (int i = 0; i < anz; i++) {
                pAbstimmungWurdeVeraendert[i] = weStubAbstimmungenRC.abstimmungWurdeVeraendert[i];
                pAbstimmungen[i] = weStubAbstimmungenRC.angezeigteAbstimmungen[i];
            }
            return weStubAbstimmungenRC.rc;
        }

        dbOpen();
        for (int i = 0; i < anz; i++) {
            if (pAbstimmungWurdeVeraendert[i] == true) {
                int rc = 1;
                rc = lDbBundle.dbAbstimmungen.update(pAbstimmungen[i]);
                if (rc < 1) {
                    dbClose();
                    return -1;
                }
                pAbstimmungWurdeVeraendert[i] = false;
            }
        }
        dbClose();
        return 1;
    }

    /*3*/
    /**Verwendung in CtrlParameterAbstimmung: Speichern aller EclAbstimmungsblock in pAbstimmungsblock, bei denen das
     * entsprechende pAbstimmungWurdeVeraendert auf true steht (steht anschließend wieder auf false).
     * rc=-1 => wurde von anderem User verändert, sonst 1
     */
    public int speichernVeraenderteAngezeigteAbstimmungsbloecke(EclAbstimmungsblock[] pAbstimmungsblock,
            boolean[] pAbstimmungWurdeVeraendert) {
        if (pAbstimmungWurdeVeraendert == null) {
            return 1;
        }
        int anz = pAbstimmungWurdeVeraendert.length;

        if (verwendeWebService()) {
            WEStubAbstimmungen weStubAbstimmungen = new WEStubAbstimmungen();
            weStubAbstimmungen.stubFunktion = 3;
            weStubAbstimmungen.abstimmungsblockArray = pAbstimmungsblock;
            weStubAbstimmungen.abstimmungWurdeVeraendert = pAbstimmungWurdeVeraendert;

            WELoginVerify weLoginVerify = new WELoginVerify();
            weStubAbstimmungen.setWeLoginVerify(weLoginVerify);

            WEStubAbstimmungenRC weStubAbstimmungenRC = wsClient.stubAbstimmungen(weStubAbstimmungen);

            if (weStubAbstimmungenRC.rc < 1) {
                return weStubAbstimmungenRC.rc;
            }

            for (int i = 0; i < anz; i++) {
                pAbstimmungWurdeVeraendert[i] = weStubAbstimmungenRC.abstimmungWurdeVeraendert[i];
                pAbstimmungsblock[i] = weStubAbstimmungenRC.abstimmungsblockListe[i];
            }
            return weStubAbstimmungenRC.rc;
        }

        dbOpen();
        for (int i = 0; i < anz; i++) {
            if (pAbstimmungWurdeVeraendert[i] == true) {
                int rc = 1;
                rc = lDbBundle.dbAbstimmungsblock.update(pAbstimmungsblock[i]);
                if (rc < 1) {
                    dbClose();
                    return -1;
                }
                pAbstimmungWurdeVeraendert[i] = false;
            }
        }
        dbClose();
        return 1;
    }

    /*4*/
    /**Verwendung in CtrlParameterAbstimmung: Speichern aller EclAbstimmungsblock in pAbstimmungsblock, bei denen das
     * entsprechende pAbstimmungWurdeVeraendert auf true steht (steht anschließend wieder auf false).
     * rc=-1 => wurde von anderem User verändert, sonst 1
     */
    public int speichernVeraenderteAngezeigteElekStimmkarte(EclStimmkarteInhalt[] pElekStimmkarte,
            boolean[] pAbstimmungWurdeVeraendert) {
        if (pAbstimmungWurdeVeraendert == null) {
            return 1;
        }
        int anz = pAbstimmungWurdeVeraendert.length;

        if (verwendeWebService()) {
            WEStubAbstimmungen weStubAbstimmungen = new WEStubAbstimmungen();
            weStubAbstimmungen.stubFunktion = 4;
            weStubAbstimmungen.elekStimmkarteArray = pElekStimmkarte;
            weStubAbstimmungen.abstimmungWurdeVeraendert = pAbstimmungWurdeVeraendert;

            WELoginVerify weLoginVerify = new WELoginVerify();
            weStubAbstimmungen.setWeLoginVerify(weLoginVerify);

            WEStubAbstimmungenRC weStubAbstimmungenRC = wsClient.stubAbstimmungen(weStubAbstimmungen);

            if (weStubAbstimmungenRC.rc < 1) {
                return weStubAbstimmungenRC.rc;
            }

            for (int i = 0; i < anz; i++) {
                pAbstimmungWurdeVeraendert[i] = weStubAbstimmungenRC.abstimmungWurdeVeraendert[i];
                pElekStimmkarte[i] = weStubAbstimmungenRC.stimmkarteInhalt[i];
            }
            return weStubAbstimmungenRC.rc;
        }

        dbOpen();
        for (int i = 0; i < anz; i++) {
            if (pAbstimmungWurdeVeraendert[i] == true) {
                int rc = 1;
                rc = lDbBundle.dbStimmkarteInhalt.update(pElekStimmkarte[i]);
                if (rc < 1) {
                    lDbBundle.closeAll();
                    return -1;
                }
                pAbstimmungWurdeVeraendert[i] = false;
            }
        }
        dbClose();
        return 1;
    }

    /*5*/
    public int speichernListeNeueAbstimmungZuAbstimmungsblock(List<EclAbstimmungZuAbstimmungsblock> neueZuordnung) {
        if (neueZuordnung != null) {

            if (verwendeWebService()) {
                WEStubAbstimmungen weStubAbstimmungen = new WEStubAbstimmungen();
                weStubAbstimmungen.stubFunktion = 5;
                weStubAbstimmungen.listAbstimmungZuAbstimmungsblock = neueZuordnung;

                WELoginVerify weLoginVerify = new WELoginVerify();
                weStubAbstimmungen.setWeLoginVerify(weLoginVerify);

                WEStubAbstimmungenRC weStubAbstimmungenRC = wsClient.stubAbstimmungen(weStubAbstimmungen);

                if (weStubAbstimmungenRC.rc < 1) {
                    return weStubAbstimmungenRC.rc;
                }

                return weStubAbstimmungenRC.rc;
            }

            dbOpen();
            for (int i = 0; i < neueZuordnung.size(); i++) {
                lDbBundle.dbAbstimmungZuAbstimmungsblock.insert(neueZuordnung.get(i));
            }
            dbClose();
        }
        return 1;
    }

    /*6*/
    public int loeschenListeAbstimmungZuAbstimmungsblock(List<EclAbstimmungZuAbstimmungsblock> neueZuordnung) {
        if (neueZuordnung != null) {

            if (verwendeWebService()) {
                WEStubAbstimmungen weStubAbstimmungen = new WEStubAbstimmungen();
                weStubAbstimmungen.stubFunktion = 6;
                weStubAbstimmungen.listAbstimmungZuAbstimmungsblock = neueZuordnung;

                WELoginVerify weLoginVerify = new WELoginVerify();
                weStubAbstimmungen.setWeLoginVerify(weLoginVerify);

                WEStubAbstimmungenRC weStubAbstimmungenRC = wsClient.stubAbstimmungen(weStubAbstimmungen);

                if (weStubAbstimmungenRC.rc < 1) {
                    return weStubAbstimmungenRC.rc;
                }

                return weStubAbstimmungenRC.rc;
            }

            dbOpen();
            for (int i = 0; i < neueZuordnung.size(); i++) {
                lDbBundle.dbAbstimmungZuAbstimmungsblock.delete(neueZuordnung.get(i).ident);
            }
            dbClose();
        }
        return 1;
    }

    /*7*/
    public int speichernListeNeueAbstimmungZuElekStimmkarte(List<EclAbstimmungenZuStimmkarte> neueZuordnung) {
        if (neueZuordnung != null) {

            if (verwendeWebService()) {
                WEStubAbstimmungen weStubAbstimmungen = new WEStubAbstimmungen();
                weStubAbstimmungen.stubFunktion = 7;
                weStubAbstimmungen.listAbstimmungZuStimmkarte = neueZuordnung;

                WELoginVerify weLoginVerify = new WELoginVerify();
                weStubAbstimmungen.setWeLoginVerify(weLoginVerify);

                WEStubAbstimmungenRC weStubAbstimmungenRC = wsClient.stubAbstimmungen(weStubAbstimmungen);

                if (weStubAbstimmungenRC.rc < 1) {
                    return weStubAbstimmungenRC.rc;
                }

                return weStubAbstimmungenRC.rc;
            }

            dbOpen();
            for (int i = 0; i < neueZuordnung.size(); i++) {
                lDbBundle.dbAbstimmungenZuStimmkarte.insert(neueZuordnung.get(i));
            }
            dbClose();
        }
        return 1;
    }

    /*18*/
    public int speichernReload(boolean reloadWeisungenAbbruch, boolean reloadAbstimmungenAbbruch, boolean reload) {
        if (verwendeWebService()) {
            WEStubAbstimmungen weStubAbstimmungen = new WEStubAbstimmungen();
            weStubAbstimmungen.stubFunktion = 18;

            weStubAbstimmungen.reloadWeisungenAbbruch = reloadWeisungenAbbruch;
            weStubAbstimmungen.reloadAbstimmungenAbbruch = reloadAbstimmungenAbbruch;
            weStubAbstimmungen.reload = reload;

            WELoginVerify weLoginVerify = new WELoginVerify();
            weStubAbstimmungen.setWeLoginVerify(weLoginVerify);

            WEStubAbstimmungenRC weStubAbstimmungenRC = wsClient.stubAbstimmungen(weStubAbstimmungen);

            if (weStubAbstimmungenRC.rc < 1) {
                return weStubAbstimmungenRC.rc;
            }

            return weStubAbstimmungenRC.rc;
        }

        dbOpen();
        CaBug.druckeLog("", logDrucken, 10);
        BvReload bvReload = new BvReload(lDbBundle);
        if (reloadWeisungenAbbruch) {
            CaBug.druckeLog("Speichere reloadWeisungenAbbruch", logDrucken, 10);
            bvReload.setReloadWeisungen(lDbBundle.clGlobalVar.mandant);
        }
        if (reloadAbstimmungenAbbruch) {
            CaBug.druckeLog("Speichere reloadAbstimmungenAbbruch", logDrucken, 10);
            bvReload.setReloadAbstimmungen(lDbBundle.clGlobalVar.mandant);
        }
        if (reload) {
            CaBug.druckeLog("Speichere reloadOhneAbbruch", logDrucken, 10);
            bvReload.setReloadAbstimmungenWeisungenOhneAbbruch(lDbBundle.clGlobalVar.mandant);
        }
        dbClose();
        return 1;
    }

    /*8*/
    public int insertElekStimmkarte(EclStimmkarteInhalt elekStimmkarte) {
        if (verwendeWebService()) {
            WEStubAbstimmungen weStubAbstimmungen = new WEStubAbstimmungen();
            weStubAbstimmungen.stubFunktion = 8;
            weStubAbstimmungen.elekStimmkarte = elekStimmkarte;

            WELoginVerify weLoginVerify = new WELoginVerify();
            weStubAbstimmungen.setWeLoginVerify(weLoginVerify);

            WEStubAbstimmungenRC weStubAbstimmungenRC = wsClient.stubAbstimmungen(weStubAbstimmungen);

            if (weStubAbstimmungenRC.rc < 1) {
                return weStubAbstimmungenRC.rc;
            }

            return weStubAbstimmungenRC.rc;
        }
        dbOpen();
        lDbBundle.dbStimmkarteInhalt.insert(elekStimmkarte);
        dbClose();
        return 1;
    }

    /*9*/
    public int loeschenListeAbstimmungZuElekStimmkarte(List<EclAbstimmungenZuStimmkarte> neueZuordnung) {
        if (neueZuordnung != null) {
            if (verwendeWebService()) {
                WEStubAbstimmungen weStubAbstimmungen = new WEStubAbstimmungen();
                weStubAbstimmungen.stubFunktion = 9;
                weStubAbstimmungen.listAbstimmungZuStimmkarte = neueZuordnung;

                WELoginVerify weLoginVerify = new WELoginVerify();
                weStubAbstimmungen.setWeLoginVerify(weLoginVerify);

                WEStubAbstimmungenRC weStubAbstimmungenRC = wsClient.stubAbstimmungen(weStubAbstimmungen);

                if (weStubAbstimmungenRC.rc < 1) {
                    return weStubAbstimmungenRC.rc;
                }

                return weStubAbstimmungenRC.rc;
            }

            dbOpen();
            for (int i = 0; i < neueZuordnung.size(); i++) {
                lDbBundle.dbAbstimmungenZuStimmkarte.delete(neueZuordnung.get(i).stimmkartenNr,
                        neueZuordnung.get(i).identAbstimmungAufKarte);
            }
            dbClose();
        }
        return 1;
    }

    /*17*/
    /**abstimmungZuAbstimmungsblock kann null sein, dann keine Aufnahme in einen Abstimmungsblock*/
    public int insertAbstimmung(EclAbstimmung abstimmung,
            EclAbstimmungZuAbstimmungsblock abstimmungZuAbstimmungsblock) {
        if (verwendeWebService()) {
            WEStubAbstimmungen weStubAbstimmungen = new WEStubAbstimmungen();
            weStubAbstimmungen.stubFunktion = 17;
            weStubAbstimmungen.abstimmung = abstimmung;
            weStubAbstimmungen.abstimmungZuAbstimmungsblock = abstimmungZuAbstimmungsblock;

            WELoginVerify weLoginVerify = new WELoginVerify();
            weStubAbstimmungen.setWeLoginVerify(weLoginVerify);

            WEStubAbstimmungenRC weStubAbstimmungenRC = wsClient.stubAbstimmungen(weStubAbstimmungen);

            if (weStubAbstimmungenRC.rc < 1) {
                return weStubAbstimmungenRC.rc;
            }

            return weStubAbstimmungenRC.rc;
        }
        dbOpen();
        /*Neuen Abstimmungssatz speichern*/
        lDbBundle.dbAbstimmungen.insert(abstimmung);

        if (abstimmungZuAbstimmungsblock != null) {
            abstimmungZuAbstimmungsblock.identAbstimmung = abstimmung.ident;
            lDbBundle.dbAbstimmungZuAbstimmungsblock.insert(abstimmungZuAbstimmungsblock);
        }
        dbClose();
        return 1;
    }

    /*10*/
    /**Returnwert:
     * pmAbstimmungNichtLoeschbarStimmkarteZugeordnet, pmAbstimmungNichtLoeschbarAbstimmvorgangZugeordnet
      */
    public int loescheAbstimmung(int ident) {

        if (verwendeWebService()) {
            WEStubAbstimmungen weStubAbstimmungen = new WEStubAbstimmungen();
            weStubAbstimmungen.stubFunktion = 10;
            weStubAbstimmungen.ident = ident;

            WELoginVerify weLoginVerify = new WELoginVerify();
            weStubAbstimmungen.setWeLoginVerify(weLoginVerify);

            WEStubAbstimmungenRC weStubAbstimmungenRC = wsClient.stubAbstimmungen(weStubAbstimmungen);

            if (weStubAbstimmungenRC.rc < 1) {
                return weStubAbstimmungenRC.rc;
            }

            return weStubAbstimmungenRC.rc;
        }

        dbOpen();

        /*Ist diese Ident einer Stimmkarte oder einer Abstimmung zugeordnet?*/
        /*Stimmkarte?*/
        lDbBundle.dbAbstimmungenZuStimmkarte.readZuIdentAbstimmung(ident);
        if (lDbBundle.dbAbstimmungenZuStimmkarte.anzErgebnis() > 0) {
            dbClose();
            return CaFehler.pmAbstimmungNichtLoeschbarStimmkarteZugeordnet;
        }
        /*Abstimmungsblock?*/
        lDbBundle.dbAbstimmungZuAbstimmungsblock.read_zuZuIdentAbstimmung(ident);
        if (lDbBundle.dbAbstimmungZuAbstimmungsblock.anzErgebnis() > 0) {
            dbClose();
            return CaFehler.pmAbstimmungNichtLoeschbarAbstimmvorgangZugeordnet;
        }

        /*Durchführung*/
        lDbBundle.dbAbstimmungen.delete(ident);
        lDbBundle.dbAbstimmungenEinzelAusschluss.deleteAlleZuAbstimmung(ident);

        dbClose();
        return 1;
    }

    /*16*/
    public int insertAbstimmVorgang(EclAbstimmungsblock abstimmungsvorgang) {
        if (verwendeWebService()) {
            WEStubAbstimmungen weStubAbstimmungen = new WEStubAbstimmungen();
            weStubAbstimmungen.stubFunktion = 16;
            weStubAbstimmungen.abstimmungsvorgang = abstimmungsvorgang;

            WELoginVerify weLoginVerify = new WELoginVerify();
            weStubAbstimmungen.setWeLoginVerify(weLoginVerify);

            WEStubAbstimmungenRC weStubAbstimmungenRC = wsClient.stubAbstimmungen(weStubAbstimmungen);

            if (weStubAbstimmungenRC.rc < 1) {
                return weStubAbstimmungenRC.rc;
            }

            return weStubAbstimmungenRC.rc;
        }
        dbOpen();
        lDbBundle.dbAbstimmungsblock.insert(abstimmungsvorgang);
        dbClose();
        return 1;
    }

    /*11*
    /**Returnwert:
     * pmAbstimmungVorgangNichtLoeschbarAbstimmvorgangZugeordnet
      */
    public int loescheAbstimmVorgang(int ident) {
        if (verwendeWebService()) {
            WEStubAbstimmungen weStubAbstimmungen = new WEStubAbstimmungen();
            weStubAbstimmungen.stubFunktion = 11;
            weStubAbstimmungen.ident = ident;

            WELoginVerify weLoginVerify = new WELoginVerify();
            weStubAbstimmungen.setWeLoginVerify(weLoginVerify);

            WEStubAbstimmungenRC weStubAbstimmungenRC = wsClient.stubAbstimmungen(weStubAbstimmungen);

            if (weStubAbstimmungenRC.rc < 1) {
                return weStubAbstimmungenRC.rc;
            }

            return weStubAbstimmungenRC.rc;
        }

        dbOpen();

        /*Ist dieser Ident eine Abstimmung zugeordnet?*/
        lDbBundle.dbAbstimmungZuAbstimmungsblock.read_zuAbstimmungsblock(ident, 1);
        if (lDbBundle.dbAbstimmungZuAbstimmungsblock.anzErgebnis() > 0) {
            dbClose();
            return CaFehler.pmAbstimmungVorgangNichtLoeschbarAbstimmvorgangZugeordnet;
        }

        /*Durchführung*/
        lDbBundle.dbAbstimmungsblock.delete(ident);

        dbClose();
        return 1;
    }

    /*12*/
    /**Returnwert:
     * pmElekStimmkarteNichtLoeschbarAbstimmvorgangZugeordnet
      */
    public int loescheElekStimmkarte(int ident) {

        if (verwendeWebService()) {
            WEStubAbstimmungen weStubAbstimmungen = new WEStubAbstimmungen();
            weStubAbstimmungen.stubFunktion = 12;
            weStubAbstimmungen.ident = ident;

            WELoginVerify weLoginVerify = new WELoginVerify();
            weStubAbstimmungen.setWeLoginVerify(weLoginVerify);

            WEStubAbstimmungenRC weStubAbstimmungenRC = wsClient.stubAbstimmungen(weStubAbstimmungen);

            if (weStubAbstimmungenRC.rc < 1) {
                return weStubAbstimmungenRC.rc;
            }

            return weStubAbstimmungenRC.rc;
        }

        dbOpen();

        /*Ist dieser Ident eine Abstimmung zugeordnet?*/
        lDbBundle.dbAbstimmungenZuStimmkarte.readZuStimmkartenNr(ident);
        if (lDbBundle.dbAbstimmungenZuStimmkarte.anzErgebnis() > 0) {
            dbClose();
            return CaFehler.pmElekStimmkarteNichtLoeschbarAbstimmvorgangZugeordnet;
        }

        /*Durchführung*/
        lDbBundle.dbStimmkarteInhalt.delete(ident);

        dbClose();
        return 1;
    }

    /*13*/
    /**Ein-/Ausgabeparamter für zeigeSortierungAbstimmungeInit*/
    public EclAbstimmung[] angezeigteAbstimmungen = null;
    public boolean[] abstimmungWurdeVeraendert = null;
    public EclAbstimmungZuAbstimmungsblock[] angezeigteAbstimmungZuAbstimmungsblock = null;
    public EclAbstimmungenZuStimmkarte[] angezeigteAbstimmungZuStimmkarte = null;

    /**für CtrlParameterAbstimmung
     * 
     * filterTyp:
     * 0 = alle; 1 = Abstimmungsblock; 2 = Karte aus elektronischem Stimmkartenblock
     * 
     * 3=Zuordnung einer Abstimmung zu Abstimmungsblock
     * 4=Zuordnung einer Abstimmung zu einer elektronischen Stimmkarte
     * 5=Löschen einer Abstimmung zu Abstimmungsblock
     * 6=Löschen einer Abstimmung zu einer elektronischen Stimmkarte
     * 
     * filterIdent:
     * Falls Filtertyp 0:
     * 1 wirklich alle
     * 2 alle die intern aktiv
     * 3 alle die im Portal aktiv
     * 4 alle, die bei Weisungsterminals aktiv
     * 
     * Falls Filtertyp sonst: ident des Selektierten Abstimmungsblocks bzw. El.Stimmkarte
     * 
     * sortierung:
     * 0 interne Ident
     * 1 Position Weisungen Intern
     * 2 Position Weisungen Portal
     * 3 Position elektronische Stimmkarte
     * 4 Position Stimmkarte
     * 5 Position Tablet-Abstimmung
     * 6 Position Ausdruck
     * 7 Position Weisungen Verlassen HV
     * 
     * Füllt: angezeigteAbstimmungen, abstimmungWurdeVeraendert, angezeigteAbstimmungZuAbstimmungsblock,
     * angezeigteAbstimmungZuStimmkarte
     * */
    public int zeigeSortierungAbstimmungenInit(int filterTyp, int filterIdent, int sortierung) {

        if (verwendeWebService()) {
            WEStubAbstimmungen weStubAbstimmungen = new WEStubAbstimmungen();
            weStubAbstimmungen.stubFunktion = 13;
            weStubAbstimmungen.filterTyp = filterTyp;
            weStubAbstimmungen.filterIdent = filterIdent;
            weStubAbstimmungen.sortierung = sortierung;

            WELoginVerify weLoginVerify = new WELoginVerify();
            weStubAbstimmungen.setWeLoginVerify(weLoginVerify);

            WEStubAbstimmungenRC weStubAbstimmungenRC = wsClient.stubAbstimmungen(weStubAbstimmungen);

            if (weStubAbstimmungenRC.rc < 1) {
                return weStubAbstimmungenRC.rc;
            }

            angezeigteAbstimmungen = weStubAbstimmungenRC.angezeigteAbstimmungen;
            abstimmungWurdeVeraendert = weStubAbstimmungenRC.abstimmungWurdeVeraendert;
            angezeigteAbstimmungZuAbstimmungsblock = weStubAbstimmungenRC.angezeigteAbstimmungZuAbstimmungsblock;
            angezeigteAbstimmungZuStimmkarte = weStubAbstimmungenRC.angezeigteAbstimmungZuStimmkarte;
            return weStubAbstimmungenRC.rc;
        }

        dbOpen();
        switch (filterTyp) {
        case 0: {
            if (filterIdent == 1) {
                lDbBundle.dbAbstimmungen.leseAlleAbstimmungen(sortierung, KonstDBAbstimmungen.selektion_alle, false);
            }
            if (filterIdent == 2) {
                lDbBundle.dbAbstimmungen.leseAlleAbstimmungen(sortierung, KonstDBAbstimmungen.selektion_alleInternAktiv,
                        false);
            }
            if (filterIdent == 3) {
                lDbBundle.dbAbstimmungen.leseAlleAbstimmungen(sortierung, KonstDBAbstimmungen.selektion_allePortalAktiv,
                        false);
            }
            if (filterIdent == 4) {
                lDbBundle.dbAbstimmungen.leseAlleAbstimmungen(sortierung, KonstDBAbstimmungen.selektion_alleAktivAufHV,
                        false);
            }
            angezeigteAbstimmungen = lDbBundle.dbAbstimmungen.abstimmungenArray;
            break;
        }
        case 5:
        case 1: {/*Abstimmungsblock*/
            lDbBundle.dbAbstimmungen.leseAbstimmungenZuBlock(filterIdent, sortierung, false);
            angezeigteAbstimmungen = lDbBundle.dbAbstimmungen.abstimmungenArray;
            angezeigteAbstimmungZuAbstimmungsblock = lDbBundle.dbAbstimmungZuAbstimmungsblock.ergebnis();
            break;
        }
        case 6:
        case 2: {/*elektronische Stimmkarte*/
            lDbBundle.dbAbstimmungen.leseAbstimmungenZuStimmkarte(filterIdent, sortierung, false);
            angezeigteAbstimmungen = lDbBundle.dbAbstimmungen.abstimmungenArray;
            angezeigteAbstimmungZuStimmkarte = lDbBundle.dbAbstimmungenZuStimmkarte.ergebnis();
            break;
        }
        case 3: {/*Neu-Zuordnung zu Abstimmungsblock*/
            lDbBundle.dbAbstimmungen.leseAlleAbstimmungen(KonstDBAbstimmungen.sortierung_interneIdent,
                    KonstDBAbstimmungen.selektion_alle, false);
            angezeigteAbstimmungen = lDbBundle.dbAbstimmungen.abstimmungenArray;

            lDbBundle.dbAbstimmungen.leseAbstimmungenZuBlock(filterIdent, sortierung, false);
            angezeigteAbstimmungZuAbstimmungsblock = lDbBundle.dbAbstimmungZuAbstimmungsblock.ergebnis();

            break;
        }
        case 4: {/*Neu-Zuordnung zu ElekStimmkarte*/
            lDbBundle.dbAbstimmungen.leseAlleAbstimmungen(KonstDBAbstimmungen.sortierung_interneIdent,
                    KonstDBAbstimmungen.selektion_alle, false);
            angezeigteAbstimmungen = lDbBundle.dbAbstimmungen.abstimmungenArray;

            lDbBundle.dbAbstimmungen.leseAbstimmungenZuStimmkarte(filterIdent, sortierung, false);
            angezeigteAbstimmungZuStimmkarte = lDbBundle.dbAbstimmungenZuStimmkarte.ergebnis();

            break;
        }
        }
        dbClose();
        return 1;
    }

    /*14*/
    /**für CtrlParameterAbstimmung*/
    public EclAbstimmungsblock[] abstimmungsblockListe = null;
    public EclStimmkarteInhalt[] stimmkarteInhalt = null;

    public int fuelleFilterFuerAnzeige() {

        if (verwendeWebService()) {
            WEStubAbstimmungen weStubAbstimmungen = new WEStubAbstimmungen();
            weStubAbstimmungen.stubFunktion = 14;

            WELoginVerify weLoginVerify = new WELoginVerify();
            weStubAbstimmungen.setWeLoginVerify(weLoginVerify);

            WEStubAbstimmungenRC weStubAbstimmungenRC = wsClient.stubAbstimmungen(weStubAbstimmungen);

            if (weStubAbstimmungenRC.rc < 1) {
                return weStubAbstimmungenRC.rc;
            }

            abstimmungsblockListe = weStubAbstimmungenRC.abstimmungsblockListe;
            stimmkarteInhalt = weStubAbstimmungenRC.stimmkarteInhalt;
            return weStubAbstimmungenRC.rc;
        }

        dbOpen();

        lDbBundle.dbAbstimmungsblock.read_all();
        abstimmungsblockListe = lDbBundle.dbAbstimmungsblock.ergebnisArray;

        lDbBundle.dbStimmkarteInhalt.readAll();
        stimmkarteInhalt = lDbBundle.dbStimmkarteInhalt.ergebnis();

        dbClose();
        return 1;
    }

    /*15*/
    /**für CtrlParameterAbstimmung*/
    public EclAbstimmung[] abstimmungListe = null;

    public int fuelleComboAbstimmung() {
        if (verwendeWebService()) {
            WEStubAbstimmungen weStubAbstimmungen = new WEStubAbstimmungen();
            weStubAbstimmungen.stubFunktion = 15;

            WELoginVerify weLoginVerify = new WELoginVerify();
            weStubAbstimmungen.setWeLoginVerify(weLoginVerify);

            WEStubAbstimmungenRC weStubAbstimmungenRC = wsClient.stubAbstimmungen(weStubAbstimmungen);

            if (weStubAbstimmungenRC.rc < 1) {
                return weStubAbstimmungenRC.rc;
            }

            abstimmungListe = weStubAbstimmungenRC.angezeigteAbstimmungen;
            return weStubAbstimmungenRC.rc;
        }

        dbOpen();
        lDbBundle.dbAbstimmungen.leseAlleAbstimmungen(KonstDBAbstimmungen.sortierung_interneIdent,
                KonstDBAbstimmungen.selektion_alle, false);
        abstimmungListe = lDbBundle.dbAbstimmungen.abstimmungenArray;
        dbClose();
        return 1;
    }

}
