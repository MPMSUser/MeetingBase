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
package de.meetingapps.meetingclient.meetingClient;

import java.util.LinkedList;

import de.meetingapps.meetingportal.meetComEntities.EclMeldung;
import de.meetingapps.meetingportal.meetComEntities.EclZutrittsIdent;
import de.meetingapps.meetingportal.meetComKonst.KonstKartenklasse;
import de.meetingapps.meetingportal.meetComKonst.KonstProgrammFunktionHV;
import de.meetingapps.meetingportal.meetComKonst.KonstWillenserklaerung;
import de.meetingapps.meetingportal.meetComStub.WSClient;
import de.meetingapps.meetingportal.meetComWE.WELoginVerify;
import de.meetingapps.meetingportal.meetComWE.WEPraesenzBuchen;
import de.meetingapps.meetingportal.meetComWE.WEPraesenzBuchenRC;

/**
 * The Class BlClientPraesenzBuchen.
 */
public class BlClientPraesenzBuchen {

    /** The ws client. */
    private WSClient wsClient = null;

    /** The l WE praesenz buchen. */
    public WEPraesenzBuchen lWEPraesenzBuchen = null;

    /** The l WE praesenz buchen RC. */
    public WEPraesenzBuchenRC lWEPraesenzBuchenRC = null;

    /**
     * pWSClient muß auf ein gültiges WSClient-Objekt verweisen (d.h. mit new
     * WSClient() vorinitialisiert)
     *
     * @param pWSClient the WS client
     */
    public void initialisieren(WSClient pWSClient) {
        wsClient = pWSClient;
        lWEPraesenzBuchen = new WEPraesenzBuchen(); /*Muß unbedingt hier passieren!*/
    }

    /**
     * Buchen init.
     *
     * @param pEingabequelle the eingabequelle
     */
    public void buchen_init(int pEingabequelle) {
        if (wsClient == null) {
            wsClient = new WSClient();
        }

        WELoginVerify weLoginVerify = new WELoginVerify();
        ;
        weLoginVerify.setEingabeQuelle(pEingabequelle); /*Siehe EclWillenserklaerung - erteiltAufWeg*/
        lWEPraesenzBuchen.setWeLoginVerify(weLoginVerify);

        lWEPraesenzBuchen.programmFunktion = KonstProgrammFunktionHV.akkreditierungsSchalterStandard;

        lWEPraesenzBuchen.zutrittsIdentAktionaerArt = new LinkedList<Integer>();
        lWEPraesenzBuchen.zutrittsIdentAktionaer = new LinkedList<EclZutrittsIdent>();

        lWEPraesenzBuchen.zutrittsIdentGastArt = new LinkedList<Integer>();
        lWEPraesenzBuchen.zutrittsIdentGast = new LinkedList<EclZutrittsIdent>();

        lWEPraesenzBuchen.stimmkartenArt = new LinkedList<int[]>();
        lWEPraesenzBuchen.stimmkarten = new LinkedList<String[]>();

        lWEPraesenzBuchen.stimmkartenSecondArt = new LinkedList<Integer>();
        lWEPraesenzBuchen.stimmkartenSecond = new LinkedList<String>();

        lWEPraesenzBuchen.eclMeldung = new LinkedList<EclMeldung>();

        lWEPraesenzBuchen.istIdentisch = new LinkedList<Integer>();

        lWEPraesenzBuchen.aktionen = new LinkedList<Integer>();
    }

    /**
     * (lEintrittskartennummer) pZutrittsIdent wird in die Liste
     * zutrittsIdentAktionaer eingetragen.
     *
     * @param pZutrittsIdent             the zutritts ident
     * @param pZutrittsIdentKartenklasse the zutritts ident kartenklasse
     */
    public void buchen_zutrittsIdentAktionaer(EclZutrittsIdent pZutrittsIdent, int pZutrittsIdentKartenklasse) {
        System.out.println("zutrittsIdent=" + pZutrittsIdent.zutrittsIdent);
        System.out.println("zutrittsIdentNeben=" + pZutrittsIdent.zutrittsIdentNeben);
        if (pZutrittsIdentKartenklasse == KonstKartenklasse.eintrittskartennummer) {
            System.out.println("buchen_zutrittsIdentAktionaer füllen");
            lWEPraesenzBuchen.zutrittsIdentAktionaerArt.add(2);
            lWEPraesenzBuchen.zutrittsIdentAktionaer.add(pZutrittsIdent);
        } else {
            System.out.println("buchen_zutrittsIdentAktionaer leer");
            lWEPraesenzBuchen.zutrittsIdentAktionaerArt.add(0);
            lWEPraesenzBuchen.zutrittsIdentAktionaer.add(null);
        }

    }

    /**
     * wird in die Liste zutrittsIdentGast eingetragen.
     *
     * @param pZutrittsIdent             the zutritts ident
     * @param pZutrittsIdentKartenklasse the zutritts ident kartenklasse
     */
    public void buchen_zutrittsIdentGast(EclZutrittsIdent pZutrittsIdent, int pZutrittsIdentKartenklasse) {
        if (pZutrittsIdentKartenklasse == KonstKartenklasse.gastkartennummer) {
            System.out.println("buchen_zutrittsIdentGast füllen");
            lWEPraesenzBuchen.zutrittsIdentGastArt.add(2);
            lWEPraesenzBuchen.zutrittsIdentGast.add(pZutrittsIdent);
        } else {
            System.out.println("buchen_zutrittsIdentGast leer");
            lWEPraesenzBuchen.zutrittsIdentGastArt.add(0);
            lWEPraesenzBuchen.zutrittsIdentGast.add(null);
        }
    }

    /**
     * Stimmkarten zuordnen.
     *
     * @param pStimmkarteEingebenErforderlich the stimmkarte eingeben erforderlich
     * @param pStimmkarten                    the stimmkarten
     * @param pStimmkartenEingeben            the stimmkarten eingeben
     */
    public void buchen_stimmkarten(boolean pStimmkarteEingebenErforderlich, String[] pStimmkarten,
            int[] pStimmkartenEingeben) {
        //		if (gwePraesenzStatusabfrageRC.kartenklasseZuIdentifikationsnummer.get(0)==KonstKartenklasse.stimmkartennummer){
        //			lStimmkartennummer=gwePraesenzStatusabfrageRC.identifikationsnummer.get(0);
        //		}

        lWEPraesenzBuchen.stimmkartenArt.add(new int[] { 0, 0, 0, 0 });
        lWEPraesenzBuchen.stimmkarten.add(new String[] { "", "", "", "" });

        int offsetEintraege = lWEPraesenzBuchen.stimmkartenArt.size() - 1;
        if (pStimmkarteEingebenErforderlich) {
            for (int i = 0; i < 4; i++) {
                if (pStimmkartenEingeben[i] == 1) {
                    lWEPraesenzBuchen.stimmkartenArt.get(offsetEintraege)[i] = 2;
                    lWEPraesenzBuchen.stimmkarten.get(offsetEintraege)[i] = pStimmkarten[i];
                }
            }
        }

    }

    /**
     * StimmkartenSecond zuordnen (derzeit noch nicht implementiert).
     *
     * @param pStimmkarteEingebenErforderlich the stimmkarte eingeben erforderlich
     * @param pStimmkarten                    the stimmkarten
     * @param pStimmkartenEingeben            the stimmkarten eingeben
     */
    public void buchen_stimmkartenSecond(boolean pStimmkarteEingebenErforderlich, String[] pStimmkarten,
            int[] pStimmkartenEingeben) {
        lWEPraesenzBuchen.stimmkartenSecondArt.add(0);
        lWEPraesenzBuchen.stimmkartenSecond.add("");
        int offsetEintraege = lWEPraesenzBuchen.stimmkartenSecondArt.size() - 1;

        if (pStimmkarteEingebenErforderlich) {
            if (pStimmkartenEingeben[4] == 1) {
                lWEPraesenzBuchen.stimmkartenSecondArt.set(offsetEintraege, 2);
                lWEPraesenzBuchen.stimmkartenSecond.set(offsetEintraege, pStimmkarten[4]);
            }
        }
    }

    /**
     * lEclMeldung in eclMeldung eintragen.
     *
     * @param pEclmeldung the eclmeldung
     */
    public void buchen_eclMeldung(EclMeldung pEclmeldung) {
        lWEPraesenzBuchen.eclMeldung.add(pEclmeldung);
    }

    /**Vollmacht eintragen.
     * Hinweis: es muß / darf genau eine der beiden Funktionen buchen_vertreter oder buchen_vertreterUnveraendert für jede
     * Buchung aufgerufen werden, um das neue Listenelement in lWEPraesenzBuchen.vollmachtPersonenNatJurIdent korrekt anzufügen*/
    public void buchen_vertreter(EclMeldung pEclmeldung, int pPersonNatJurVertreter,
            int pPersonNatJurVertreterNeueVollmacht, String pVertreterName, String pVertreterVorname,
            String pVertreterOrt) {
        if (pEclmeldung.meldungIstEinAktionaer()) { /*Aktionär*/
            lWEPraesenzBuchen.vollmachtPersonenNatJurIdent.add(pPersonNatJurVertreter);
            if (pPersonNatJurVertreterNeueVollmacht > 0) {
                lWEPraesenzBuchen.neueVollmachtPersonenNatJurIdent = pPersonNatJurVertreterNeueVollmacht;
            }
            lWEPraesenzBuchen.vertreterName = pVertreterName;
            lWEPraesenzBuchen.vertreterVorname = pVertreterVorname;
            lWEPraesenzBuchen.vertreterOrt = pVertreterOrt;
        } else {/*Gast*/
            lWEPraesenzBuchen.vollmachtPersonenNatJurIdent.add(-1);
            lWEPraesenzBuchen.vertreterName = "";
            lWEPraesenzBuchen.vertreterVorname = "";
            lWEPraesenzBuchen.vertreterOrt = "";
        }
    }

    /**
     * Buchen vertreter unveraendert.
     */
    public void buchen_vertreterUnveraendert() {
        lWEPraesenzBuchen.vollmachtPersonenNatJurIdent.add(-2);
    }

    /**
     * Buchung auslösen - "oberste Ebene".
     *
     * Buchen Erstzugang
     * 
     * @param pEingabequelle                      the eingabequelle
     * @param pZutrittsIdent                      the zutritts ident
     * @param pZutrittsIdentKartenklasse          the zutritts ident kartenklasse
     * @param pStimmkarteEingebenErforderlich     the stimmkarte eingeben
     *                                            erforderlich
     * @param pStimmkarten                        the stimmkarten
     * @param pStimmkartenEingeben                the stimmkarten eingeben
     * @param pEclmeldung                         the eclmeldung
     * @param pPersonNatJurVertreter              the person nat jur vertreter
     * @param pPersonNatJurVertreterNeueVollmacht the person nat jur vertreter neue
     *                                            vollmacht
     * @param pVertreterName                      the vertreter name
     * @param pVertreterVorname                   the vertreter vorname
     * @param pVertreterOrt                       the vertreter ort
     * @param aktionen                            the aktionen
     */

    public void buchen_erstzugang(int pEingabequelle, EclZutrittsIdent pZutrittsIdent, int pZutrittsIdentKartenklasse,
            boolean pStimmkarteEingebenErforderlich, String[] pStimmkarten, int[] pStimmkartenEingeben,
            EclMeldung pEclmeldung, int pPersonNatJurVertreter, int pPersonNatJurVertreterNeueVollmacht,
            String pVertreterName, String pVertreterVorname, String pVertreterOrt, int aktionen) {
        /*OK*/
        buchen_init(pEingabequelle);
        lWEPraesenzBuchen.funktion.add(KonstWillenserklaerung.erstzugang);
        lWEPraesenzBuchen.aktionen.add(aktionen);
        buchen_zutrittsIdentAktionaer(pZutrittsIdent, pZutrittsIdentKartenklasse);
        buchen_zutrittsIdentGast(pZutrittsIdent, pZutrittsIdentKartenklasse);
        buchen_stimmkarten(pStimmkarteEingebenErforderlich, pStimmkarten, pStimmkartenEingeben);
        buchen_stimmkartenSecond(pStimmkarteEingebenErforderlich, pStimmkarten, pStimmkartenEingeben);
        buchen_eclMeldung(pEclmeldung);
        buchen_vertreter(pEclmeldung, pPersonNatJurVertreter, pPersonNatJurVertreterNeueVollmacht, pVertreterName,
                pVertreterVorname, pVertreterOrt);
        lWEPraesenzBuchenRC = wsClient.praesenzBuchen(lWEPraesenzBuchen);
        System.out.println("Fehlerrc=" + lWEPraesenzBuchenRC.rc);
    }

    /**
     * Buchen Wiederzugang.
     *
     * @param pEingabequelle                      the eingabequelle
     * @param pZutrittsIdent                      the zutritts ident
     * @param pZutrittsIdentKartenklasse          the zutritts ident kartenklasse
     * @param pStimmkarteEingebenErforderlich     the stimmkarte eingeben
     *                                            erforderlich
     * @param pStimmkarten                        the stimmkarten
     * @param pStimmkartenEingeben                the stimmkarten eingeben
     * @param pEclmeldung                         the eclmeldung
     * @param pPersonNatJurVertreter              the person nat jur vertreter
     * @param pPersonNatJurVertreterNeueVollmacht the person nat jur vertreter neue
     *                                            vollmacht
     * @param pVertreterName                      the vertreter name
     * @param pVertreterVorname                   the vertreter vorname
     * @param pVertreterOrt                       the vertreter ort
     * @param pVorbestimmtePersonNatJur           the vorbestimmte person nat jur
     */
    public void buchen_wiederzugang(int pEingabequelle, EclZutrittsIdent pZutrittsIdent, int pZutrittsIdentKartenklasse,
            boolean pStimmkarteEingebenErforderlich, String[] pStimmkarten, int[] pStimmkartenEingeben,
            EclMeldung pEclmeldung, int pPersonNatJurVertreter, int pPersonNatJurVertreterNeueVollmacht,
            String pVertreterName, String pVertreterVorname, String pVertreterOrt, int pVorbestimmtePersonNatJur) {
        buchen_init(pEingabequelle);
        lWEPraesenzBuchen.funktion.add(KonstWillenserklaerung.wiederzugang);
        buchen_zutrittsIdentAktionaer(pZutrittsIdent, pZutrittsIdentKartenklasse);
        buchen_zutrittsIdentGast(pZutrittsIdent, pZutrittsIdentKartenklasse);
        buchen_stimmkarten(pStimmkarteEingebenErforderlich, pStimmkarten, pStimmkartenEingeben);
        buchen_stimmkartenSecond(pStimmkarteEingebenErforderlich, pStimmkarten, pStimmkartenEingeben);
        buchen_eclMeldung(pEclmeldung);
        if (pVorbestimmtePersonNatJur != 0) {
            buchen_vertreterUnveraendert();
        } else {
            buchen_vertreter(pEclmeldung, pPersonNatJurVertreter, pPersonNatJurVertreterNeueVollmacht, pVertreterName,
                    pVertreterVorname, pVertreterOrt);
        }
        lWEPraesenzBuchenRC = wsClient.praesenzBuchen(lWEPraesenzBuchen);
        System.out.println("Fehlerrc=" + lWEPraesenzBuchenRC.rc);
    }

    /**
     * Buchen Vertreterwechsel.
     *
     * @param pEingabequelle                      the eingabequelle
     * @param pZutrittsIdent                      the zutritts ident
     * @param pZutrittsIdentKartenklasse          the zutritts ident kartenklasse
     * @param pStimmkarteEingebenErforderlich     the stimmkarte eingeben
     *                                            erforderlich
     * @param pStimmkarten                        the stimmkarten
     * @param pStimmkartenEingeben                the stimmkarten eingeben
     * @param pEclmeldung                         the eclmeldung
     * @param pPersonNatJurVertreter              the person nat jur vertreter
     * @param pPersonNatJurVertreterNeueVollmacht the person nat jur vertreter neue
     *                                            vollmacht
     * @param pVertreterName                      the vertreter name
     * @param pVertreterVorname                   the vertreter vorname
     * @param pVertreterOrt                       the vertreter ort
     * @param pVorbestimmtePersonNatJur           the vorbestimmte person nat jur
     */
    public void buchen_vertreterwechsel(int pEingabequelle, EclZutrittsIdent pZutrittsIdent,
            int pZutrittsIdentKartenklasse, boolean pStimmkarteEingebenErforderlich, String[] pStimmkarten,
            int[] pStimmkartenEingeben, EclMeldung pEclmeldung, int pPersonNatJurVertreter,
            int pPersonNatJurVertreterNeueVollmacht, String pVertreterName, String pVertreterVorname,
            String pVertreterOrt, int pVorbestimmtePersonNatJur) {
        buchen_init(pEingabequelle);
        lWEPraesenzBuchen.funktion.add(KonstWillenserklaerung.vertreterwechsel);
        buchen_zutrittsIdentAktionaer(pZutrittsIdent, pZutrittsIdentKartenklasse);
        buchen_zutrittsIdentGast(pZutrittsIdent, pZutrittsIdentKartenklasse);
        buchen_stimmkarten(pStimmkarteEingebenErforderlich, pStimmkarten, pStimmkartenEingeben);
        buchen_stimmkartenSecond(pStimmkarteEingebenErforderlich, pStimmkarten, pStimmkartenEingeben);
        buchen_eclMeldung(pEclmeldung);
        buchen_vertreter(pEclmeldung, pPersonNatJurVertreter, pPersonNatJurVertreterNeueVollmacht, pVertreterName,
                pVertreterVorname, pVertreterOrt);
        lWEPraesenzBuchenRC = wsClient.praesenzBuchen(lWEPraesenzBuchen);
        System.out.println("Fehlerrc=" + lWEPraesenzBuchenRC.rc);
    }

    /**
     * Buchen Abgang.
     *
     * @param pEingabequelle                  the eingabequelle
     * @param pZutrittsIdent                  the zutritts ident
     * @param pZutrittsIdentKartenklasse      the zutritts ident kartenklasse
     * @param pStimmkarteEingebenErforderlich the stimmkarte eingeben erforderlich
     * @param pStimmkarten                    the stimmkarten
     * @param pStimmkartenEingeben            the stimmkarten eingeben
     * @param pEclmeldung                     the eclmeldung
     */
    public void buchen_abgang(int pEingabequelle, EclZutrittsIdent pZutrittsIdent, int pZutrittsIdentKartenklasse,
            boolean pStimmkarteEingebenErforderlich, String[] pStimmkarten, int[] pStimmkartenEingeben,
            EclMeldung pEclmeldung) {
        buchen_init(pEingabequelle);
        lWEPraesenzBuchen.funktion.add(KonstWillenserklaerung.abgang);
        buchen_zutrittsIdentAktionaer(pZutrittsIdent, pZutrittsIdentKartenklasse);
        buchen_zutrittsIdentGast(pZutrittsIdent, pZutrittsIdentKartenklasse);
        buchen_stimmkarten(pStimmkarteEingebenErforderlich, pStimmkarten, pStimmkartenEingeben);
        buchen_stimmkartenSecond(pStimmkarteEingebenErforderlich, pStimmkarten, pStimmkartenEingeben);
        buchen_eclMeldung(pEclmeldung);
        buchen_vertreterUnveraendert();
        lWEPraesenzBuchenRC = wsClient.praesenzBuchen(lWEPraesenzBuchen);
        System.out.println("Fehlerrc=" + lWEPraesenzBuchenRC.rc);
    }

    /**
     * Buchen einzelnen Teilnehmer AppIdent Zugang.
     *
     * @param pEingabequelle                              the eingabequelle
     * @param pZutrittsIdent                              the zutritts ident
     * @param pZutrittsIdentKartenklasse                  the zutritts ident
     *                                                    kartenklasse
     * @param pStimmkarteEingebenErforderlich             the stimmkarte eingeben
     *                                                    erforderlich
     * @param pStimmkarten                                the stimmkarten
     * @param pStimmkartenEingeben                        the stimmkarten eingeben
     * @param pEclmeldung                                 the eclmeldung
     * @param pPersonNatJurVertreter                      the person nat jur
     *                                                    vertreter
     * @param pPersonNatJurVertreterNeueVollmacht         the person nat jur
     *                                                    vertreter neue vollmacht
     * @param pVertreterName                              the vertreter name
     * @param pVertreterVorname                           the vertreter vorname
     * @param pVertreterOrt                               the vertreter ort
     * @param pVorbestimmtePersonNatJur                   the vorbestimmte person
     *                                                    nat jur
     * @param pAktion                                     the aktion
     * @param pAppPersonMussMitAppPersonUeberprueftWerden the app person muss mit
     *                                                    app person ueberprueft
     *                                                    werden
     * @param pInDerAppVertretendePerson                  the in der app vertretende
     *                                                    person
     * @param pAppIdentPersonNatJurIdent                  the app ident person nat
     *                                                    jur ident
     * @param aktionen                                    the aktionen
     */
    public void buchen_appIdentZugang(int pEingabequelle, EclZutrittsIdent pZutrittsIdent,
            int pZutrittsIdentKartenklasse, boolean pStimmkarteEingebenErforderlich, String[] pStimmkarten,
            int[] pStimmkartenEingeben, EclMeldung pEclmeldung, int pPersonNatJurVertreter,
            int pPersonNatJurVertreterNeueVollmacht, String pVertreterName, String pVertreterVorname,
            String pVertreterOrt, int pVorbestimmtePersonNatJur, int pAktion,
            boolean pAppPersonMussMitAppPersonUeberprueftWerden, int pInDerAppVertretendePerson,
            int pAppIdentPersonNatJurIdent, int aktionen) {
        lWEPraesenzBuchen.funktion.add(pAktion);
        lWEPraesenzBuchen.aktionen.add(aktionen);
        buchen_zutrittsIdentAktionaer(pZutrittsIdent, pZutrittsIdentKartenklasse);
        buchen_zutrittsIdentGast(pZutrittsIdent, pZutrittsIdentKartenklasse);
        buchen_stimmkarten(pStimmkarteEingebenErforderlich, pStimmkarten, pStimmkartenEingeben);
        buchen_stimmkartenSecond(pStimmkarteEingebenErforderlich, pStimmkarten, pStimmkartenEingeben);
        buchen_eclMeldung(pEclmeldung);
        System.out.println("><<<<<<<<pPersonNatJurVertreter=" + pPersonNatJurVertreter
                + " pPersonNatJurVertreterNeueVollmacht=" + pPersonNatJurVertreterNeueVollmacht);
        buchen_vertreter(pEclmeldung, pPersonNatJurVertreter, pPersonNatJurVertreterNeueVollmacht, pVertreterName,
                pVertreterVorname, pVertreterOrt);

        if (pAppPersonMussMitAppPersonUeberprueftWerden) {
            lWEPraesenzBuchen.istIdentisch.add(pInDerAppVertretendePerson);
        } else {
            lWEPraesenzBuchen.istIdentisch.add(0);
        }

        lWEPraesenzBuchen.appIdentPersonNatJurIdent = pAppIdentPersonNatJurIdent;
        lWEPraesenzBuchen.appIdent = true;

    }

    /**
     * Buchen einzelnen Teilnehmer AppIdent Abgang.
     *
     * @param pEingabequelle                  the eingabequelle
     * @param pZutrittsIdent                  the zutritts ident
     * @param pZutrittsIdentKartenklasse      the zutritts ident kartenklasse
     * @param pStimmkarteEingebenErforderlich the stimmkarte eingeben erforderlich
     * @param pStimmkarten                    the stimmkarten
     * @param pStimmkartenEingeben            the stimmkarten eingeben
     * @param pEclmeldung                     the eclmeldung
     * @param pAppIdentPersonNatJurIdent      the app ident person nat jur ident
     */
    public void buchen_appIdentAbgang(int pEingabequelle, EclZutrittsIdent pZutrittsIdent,
            int pZutrittsIdentKartenklasse, boolean pStimmkarteEingebenErforderlich, String[] pStimmkarten,
            int[] pStimmkartenEingeben, EclMeldung pEclmeldung, int pAppIdentPersonNatJurIdent) {
        lWEPraesenzBuchen.funktion.add(KonstWillenserklaerung.abgang);
        buchen_zutrittsIdentAktionaer(pZutrittsIdent, pZutrittsIdentKartenklasse);
        buchen_zutrittsIdentGast(pZutrittsIdent, pZutrittsIdentKartenklasse);
        buchen_stimmkarten(pStimmkarteEingebenErforderlich, pStimmkarten, pStimmkartenEingeben);
        buchen_stimmkartenSecond(pStimmkarteEingebenErforderlich, pStimmkarten, pStimmkartenEingeben);
        buchen_eclMeldung(pEclmeldung);
        buchen_vertreterUnveraendert();

        lWEPraesenzBuchen.appIdentPersonNatJurIdent = pAppIdentPersonNatJurIdent;
        lWEPraesenzBuchen.appIdent = true;

    }

}
