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

import java.util.LinkedList;
import java.util.List;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEclM.EclKIAVListeM;
import de.meetingapps.meetingportal.meetComEclM.EclKIAVM;
import de.meetingapps.meetingportal.meetComEntities.EclAbstimmung;
import de.meetingapps.meetingportal.meetComEntities.EclAbstimmungZuAbstimmungsblock;
import de.meetingapps.meetingportal.meetComEntities.EclMeldung;
import de.meetingapps.meetingportal.meetComHVParam.ParamS;
import de.meetingapps.meetingportal.meetComKonst.KonstEingabeQuelle;
import de.meetingapps.meetingportal.meetComKonst.KonstSkIst;
import de.meetingapps.meetingportal.meetComWE.WEAbstimmungLeseAktivenAbstimmungsblock;
import de.meetingapps.meetingportal.meetComWE.WEAbstimmungLeseAktivenAbstimmungsblockRC;
import de.meetingapps.meetingportal.meetComWE.WEKIAVFuerVollmachtDritteRC;
import de.meetingapps.meetingportal.meetComWE.WEKIAVListeGet;
import de.meetingapps.meetingportal.meetComWE.WEKIAVListeGetRC;
import de.meetingapps.meetingportal.meetComWE.WELoginVerify;
import de.meetingapps.meetingportal.meetComWE.WEStimmkartenblockGetRC;

/**Verschiedene - Statische - Kapseln für immer wieder verwendete Daten*/
public class CInjects {

    private static int logDrucken=10;
    
    /**noch undokumentiert, wann wo wie eingelesen und verwendet*/
    public static WEKIAVFuerVollmachtDritteRC weKIAVFuerVollmachtDritteRC = null;

    public static boolean aendern = true;

    /**********************************************Präsenz-Abstimmung**********************************************/
    /**Diese Daten müssen Online-Aktualisiert werden, stehen dann aber auch Offline zur Verfügung*/

    /**true => Daten wurden bereits einmal eingelesen.
     * False => Arbeitsplatz ist nicht betriebsbereit, Daten müssen erst eingelesen werden
     * */
    public static boolean praesenzAbstimmungsdatenEingelesen = false;
    
    public static boolean verbindungsabbruch=false;

    public static WEAbstimmungLeseAktivenAbstimmungsblock weAbstimmungLeseAktivenAbstimmungsblock = null;
    public static WEAbstimmungLeseAktivenAbstimmungsblockRC weAbstimmungLeseAktivenAbstimmungsblockRC = null;

    public static WEStimmkartenblockGetRC weStimmkartenblockGetRC = null;

    /********************Aufbereitet für "Tablet-Abstimmung Blättern*************"*/
    /**Anzahl der Blätterseiten*/
    public static int tabletBlaetternSeitenanzahl = 0;
    /**Für jede [Seite] beginnend bei 1: Anzahl der Spalten, die für die Stimm-Buttons Ja/Nein/Enthaltung reserviert sind*/
    public static int[] spaltenFuerStimmabgabeJeSeite = null;
    /**Für jede [Seite] (beginnend bei 1): Verweis auf die komplette Abstimmungsliste*/
    @SuppressWarnings("rawtypes")
    public static LinkedList[] tabletBlaetternVerweis = null;

    @SuppressWarnings("unchecked")
    public static boolean lesePraesenzAbstimmungsDaten(WSClient wsClient) {

        CaBug.druckeLog("Start", logDrucken, 10);
        verbindungsabbruch=false;

        weAbstimmungLeseAktivenAbstimmungsblock = new WEAbstimmungLeseAktivenAbstimmungsblock();
        WELoginVerify weLoginVerify = new WELoginVerify();
        ;
        weLoginVerify.setEingabeQuelle(KonstEingabeQuelle.konventionell_aufHV);
        weAbstimmungLeseAktivenAbstimmungsblock.setWeLoginVerify(weLoginVerify);

        WEAbstimmungLeseAktivenAbstimmungsblockRC hweAbstimmungLeseAktivenAbstimmungsblockRC = wsClient
                .abstimmungLeseAktivenAbstimmungsblock(weAbstimmungLeseAktivenAbstimmungsblock);

        if (hweAbstimmungLeseAktivenAbstimmungsblockRC.rc == CaFehler.teVerbindungsabbruchWebService) {
            verbindungsabbruch=true;
            return false;
        }
        if (hweAbstimmungLeseAktivenAbstimmungsblockRC.abstimmungenZuAktivenBlock == null) {
            praesenzAbstimmungsdatenEingelesen = false;
            return true;
        }

        CaBug.druckeLog("Step 1", logDrucken, 10);

        {
            weAbstimmungLeseAktivenAbstimmungsblockRC = hweAbstimmungLeseAktivenAbstimmungsblockRC;
            CaBug.druckeLog("abstimmungsversion="+weAbstimmungLeseAktivenAbstimmungsblockRC.abstimmungsVersion, logDrucken, 3);

            /*Ggf. tabletBlaetternVerweis aufbereiten*/
            if (ParamS.param.paramAbstimmungParameter.beiTabletAbstimmungBlaettern == 1) {
                /**Höchste verwendete Tablet-Seitennummer ermitteln*/
                tabletBlaetternSeitenanzahl = 0;
                for (int i = 0; i < weAbstimmungLeseAktivenAbstimmungsblockRC.abstimmungenZuAktivenBlock.length; i++) {
                    EclAbstimmungZuAbstimmungsblock eclAbstimmungZuAbstimmungsblock = weAbstimmungLeseAktivenAbstimmungsblockRC.abstimmungenZuAktivenBlock[i];
                    CaBug.druckeLog("eclAbstimmungZuAbstimmungsblock.nummerDerStimmkarte="+eclAbstimmungZuAbstimmungsblock.nummerDerStimmkarte, logDrucken, 10);
                    if (eclAbstimmungZuAbstimmungsblock.seite > tabletBlaetternSeitenanzahl) {
                        tabletBlaetternSeitenanzahl = eclAbstimmungZuAbstimmungsblock.seite;
                    }
                }
                
                if (tabletBlaetternSeitenanzahl == 0) {
                    /**Wenn keine Seitenzahl eingetragen, dann wird Blättern deaktiviert*/
                    ParamS.param.paramAbstimmungParameter.beiTabletAbstimmungBlaettern = 0;
                } else {
                    tabletBlaetternVerweis = new LinkedList[tabletBlaetternSeitenanzahl + 1];
                    spaltenFuerStimmabgabeJeSeite = new int[tabletBlaetternSeitenanzahl + 1];
                    for (int i = 1; i <= tabletBlaetternSeitenanzahl; i++) {
                        tabletBlaetternVerweis[i] = new LinkedList<Integer>();
                        spaltenFuerStimmabgabeJeSeite[i] = 0;
                        int hoechsteNummerVerarbeitet = -1;
                        int gef = 1;
                        while (gef == 1) {
                            int minimumGefunden = 99999;
                            int offsetGefunden = -1;
                            for (int i1 = 0; i1 < weAbstimmungLeseAktivenAbstimmungsblockRC.abstimmungenZuAktivenBlock.length; i1++) {
                                EclAbstimmungZuAbstimmungsblock eclAbstimmungZuAbstimmungsblock = weAbstimmungLeseAktivenAbstimmungsblockRC.abstimmungenZuAktivenBlock[i1];
                                EclAbstimmung eclAbstimmung = weAbstimmungLeseAktivenAbstimmungsblockRC.abstimmungen[i1];
                                if (eclAbstimmungZuAbstimmungsblock.seite == i
                                        && eclAbstimmung.aktiv == 1
                                        && eclAbstimmung.aktivAbstimmungInPortal == 1) {/*Abstimmung gehört zu dieser Seite*/
                                    if (eclAbstimmungZuAbstimmungsblock.position > hoechsteNummerVerarbeitet
                                            && eclAbstimmungZuAbstimmungsblock.position < minimumGefunden) {
                                        offsetGefunden = i1;
                                        minimumGefunden = eclAbstimmungZuAbstimmungsblock.position;
                                    }
                                }
                            }
                            if (offsetGefunden != -1) {
                                tabletBlaetternVerweis[i].add(offsetGefunden);
                                hoechsteNummerVerarbeitet = minimumGefunden;

                                /*Nun noch Anzahl Stimmbuttons für diesen TOP ermitteln*/
                                EclAbstimmung eclAbstimmung = weAbstimmungLeseAktivenAbstimmungsblockRC.abstimmungen[offsetGefunden];
                                int anzButtons = 0;
                                if (eclAbstimmung.tabletJa == 1) {
                                    anzButtons++;
                                }
                                if (eclAbstimmung.tabletNein == 1) {
                                    anzButtons++;
                                }
                                if (eclAbstimmung.tabletEnthaltung == 1) {
                                    anzButtons++;
                                }
                                if (anzButtons > spaltenFuerStimmabgabeJeSeite[i]) {
                                    spaltenFuerStimmabgabeJeSeite[i] = anzButtons;
                                }
                            } else {
                                gef = -1;
                            }
                        }
                    }
                }
            }
            
            if (ParamS.param.paramAbstimmungParameter.beiTabletAbstimmungBlaettern == 0) {//Kein ELSE, da Parameter in vorheriger Routine ggf. neu überschrieben wird!
                tabletBlaetternSeitenanzahl = 1;
                tabletBlaetternVerweis = new LinkedList[tabletBlaetternSeitenanzahl + 1];
                spaltenFuerStimmabgabeJeSeite = new int[tabletBlaetternSeitenanzahl + 1];
                tabletBlaetternVerweis[1] = new LinkedList<Integer>();
                spaltenFuerStimmabgabeJeSeite[1] = 0;
                int hoechsteNummerVerarbeitet = -1;
                int gef = 1;
                while (gef == 1) {
                    int minimumGefunden = 99999;
                    int offsetGefunden = -1;
                    for (int i1 = 0; i1 < weAbstimmungLeseAktivenAbstimmungsblockRC.abstimmungenZuAktivenBlock.length; i1++) {
                        EclAbstimmungZuAbstimmungsblock eclAbstimmungZuAbstimmungsblock = weAbstimmungLeseAktivenAbstimmungsblockRC.abstimmungenZuAktivenBlock[i1];
                        EclAbstimmung eclAbstimmung = weAbstimmungLeseAktivenAbstimmungsblockRC.abstimmungen[i1];
                        if (eclAbstimmungZuAbstimmungsblock.position > 0
                                && eclAbstimmungZuAbstimmungsblock.position > hoechsteNummerVerarbeitet
                                && eclAbstimmungZuAbstimmungsblock.position < minimumGefunden
                                && eclAbstimmung.aktiv == 1 && eclAbstimmung.aktivAbstimmungInPortal == 1) {
                            offsetGefunden = i1;
                            minimumGefunden = eclAbstimmungZuAbstimmungsblock.position;
                        }
                    }
                    if (offsetGefunden != -1) {
                        tabletBlaetternVerweis[1].add(offsetGefunden);
                        hoechsteNummerVerarbeitet = minimumGefunden;

                        /*Nun noch Anzahl Stimmbuttons für diesen TOP ermitteln*/
                        EclAbstimmung eclAbstimmung = weAbstimmungLeseAktivenAbstimmungsblockRC.abstimmungen[offsetGefunden];
                        int anzButtons = 0;
                        if (eclAbstimmung.tabletJa == 1) {
                            anzButtons++;
                        }
                        if (eclAbstimmung.tabletNein == 1) {
                            anzButtons++;
                        }
                        if (eclAbstimmung.tabletEnthaltung == 1) {
                            anzButtons++;
                        }
                        if (anzButtons > spaltenFuerStimmabgabeJeSeite[1]) {
                            spaltenFuerStimmabgabeJeSeite[1] = anzButtons;
                        }
                    } else {
                        gef = -1;
                    }
                }
            }
            for (int i = 1; i <= tabletBlaetternSeitenanzahl; i++) {
                System.out.println("Seite=" + i);
                for (int i1 = 0; i1 < tabletBlaetternVerweis[i].size(); i1++) {
                    System.out.println("Element i1=" + i1 + " Text="
                            + weAbstimmungLeseAktivenAbstimmungsblockRC.abstimmungen[(int) tabletBlaetternVerweis[i]
                                    .get(i1)].anzeigeBezeichnungLang);
                }
            }

            weLoginVerify = new WELoginVerify();
            ;
            weLoginVerify.setEingabeQuelle(KonstEingabeQuelle.konventionell_aufHV);
            WEStimmkartenblockGetRC hweStimmkartenblockGetRC = wsClient.stimmkartenBlockGet(weLoginVerify);

            if (hweStimmkartenblockGetRC.rc == CaFehler.teVerbindungsabbruchWebService) {
                CaBug.druckeLog("Stimmkartenblockeinlesen fehlgeschlagen", logDrucken, 10);
                return false;
            } else {
                weStimmkartenblockGetRC = hweStimmkartenblockGetRC;
            }
        }

        CaBug.druckeLog("Ende", logDrucken, 10);

        praesenzAbstimmungsdatenEingelesen = true;
        return true;
    }

    /****************************************************KIAV-Liste*********************************************/
    /**Wird eingelesen über leseKIAVListe.
     * Enthält Liste einer bestimmten Art von KIAV-Karten (also z.B. alle Sammelkarten mit Briefwahl)
     */
    public static EclKIAVListeM eclKIAVListeM = null;

    public static void leseKIAVListeUeberWebService(int skIst) {
        /**funktion=
         * "4" => Vollmacht/Weisung an SRV
         * "5" => Briefwahl
         * "6" => KIAV
         * "31" => Dauervollmacht
         * "35" => Organisatorisch
         */
        String funktion = "";

        switch (skIst) {
        case KonstSkIst.kiav: {
            funktion = "6";
            break;
        }
        case KonstSkIst.srv: {
            funktion = "4";
            break;
        }
        case KonstSkIst.organisatorisch: {
            funktion = "35";
            break;
        }
        case KonstSkIst.briefwahl: {
            funktion = "5";
            break;
        }
        case KonstSkIst.dauervollmacht: {
            funktion = "31";
            break;
        }
        }

        WSClient wsClient = new WSClient();

        WELoginVerify weLoginVerify = new WELoginVerify(); /*Mandatennummer wird in WSClient immer gesetzt*/

        WEKIAVListeGet weKIAVLIsteGet = new WEKIAVListeGet();
        weKIAVLIsteGet.setWeLoginVerify(weLoginVerify);
        weKIAVLIsteGet.setArt(funktion);

        /*KIAV-Liste holen und in CInjects ablegen*/
        WEKIAVListeGetRC weKIAVListeGetRC = wsClient.kiavListeGet(weKIAVLIsteGet);
        weKIAVListeGetRC.getRc();
        //		if (rc<1){ /*Fehlerbehandlung*/
        //	   		new DlaMeldung().zeigen(eigeneStage, "Systemfehler", "Fehler "+CaFehler.getFehlertext(rc, 0));
        //	   		return;
        //		}
        CInjects.eclKIAVListeM = weKIAVListeGetRC.getKiavListeM();

    }

    public static void leseKIAVListeUeberDB(DbBundle pDbBundle, int skIst) {
        if (skIst == KonstSkIst.srv) {
            pDbBundle.dbMeldungen.leseSammelkarteSRV(-1);
        }
        if (skIst == KonstSkIst.briefwahl) {
            pDbBundle.dbMeldungen.leseSammelkartenBriefwahl(-1);
        }
        if (skIst == KonstSkIst.kiav) {
            pDbBundle.dbMeldungen.leseSammelkarteKIAV(-1);
        }
        if (skIst == KonstSkIst.dauervollmacht) {
            pDbBundle.dbMeldungen.leseSammelkarteDauervollmacht(-1);
        }
        if (skIst == KonstSkIst.organisatorisch) {
            pDbBundle.dbMeldungen.leseSammelkarteOrga(-1);
        }

        EclMeldung[] geleseneSammelkarten = null;
        geleseneSammelkarten = pDbBundle.dbMeldungen.meldungenArray;

        List<EclKIAVM> kiavListeM = new LinkedList<EclKIAVM>();
        if (geleseneSammelkarten != null) {
            for (int i = 0; i < geleseneSammelkarten.length; i++) {
                EclKIAVM lKIAVM = new EclKIAVM();
                lKIAVM.copyFrom(pDbBundle.dbMeldungen.meldungenArray[i]);
                kiavListeM.add(lKIAVM);
            }
        }

        CInjects.eclKIAVListeM = new EclKIAVListeM();
        CInjects.eclKIAVListeM.setKiavListeM(kiavListeM);
    }

}
