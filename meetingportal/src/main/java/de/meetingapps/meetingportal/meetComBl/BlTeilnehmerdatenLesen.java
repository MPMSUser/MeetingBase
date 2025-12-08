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
package de.meetingapps.meetingportal.meetComBl;

import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclMeldung;
import de.meetingapps.meetingportal.meetComEntities.EclStimmkarten;
import de.meetingapps.meetingportal.meetComEntities.EclZutrittsIdent;
import de.meetingapps.meetingportal.meetComEntities.EclZutrittskarten;
import de.meetingapps.meetingportal.meetComKonst.KonstKartenklasse;
import de.meetingapps.meetingportal.meetComStub.StubRoot;
import de.meetingapps.meetingportal.meetComStub.WEStubBlTeilnehmerdatenLesen;
import de.meetingapps.meetingportal.meetComStub.WEStubBlTeilnehmerdatenLesenRC;
import de.meetingapps.meetingportal.meetComWE.WELoginVerify;

/******************Funktion für Teilnehmerdaten***************************
 * 
 * Als Stub verwendbar*/
public class BlTeilnehmerdatenLesen extends StubRoot {

    public String rcFehlerMeldungText = "";
    public EclMeldung rcEclMeldung;
    public int rcPersonNatJurIdent;

    public BlTeilnehmerdatenLesen(boolean pIstServer, DbBundle pDbBundle) {
        super(pIstServer, pDbBundle);
    }

    /*1*/
    /**Benötigt Datenbankzugriff
     * 
     * Liest - je nach pVorbelegungKartenklasse:
     * Auto - EK oder SK
     * nur EK
     * nur SK
     * 
     * Falls Kartenart nicht in pZulaessigeKartenarten enthalten ist, kommt Fehlermeldung.
     * Es kann auch 0 enthalten sein (sonst ist manuelle Eingabe nur z.B. einer EK nicht möglich).
     * pZulaessigeKartenarten==null => alles möglich.
     * 
     * Setzt:
     * > rcFehlerMeldungText
     * > rcEclMeldung
     * > rcPersonNatJurIdent
     */
    public int leseMeldung(int pVorbelegungKartenklasse, String pEingabeString, int[] pZulaessigeKartenarten) {
        if (verwendeWebService()) {
            WEStubBlTeilnehmerdatenLesen weStubBlTeilnehmerdatenLesen = new WEStubBlTeilnehmerdatenLesen();
            weStubBlTeilnehmerdatenLesen.stubFunktion = 1;

            WELoginVerify weLoginVerify = new WELoginVerify();
            weStubBlTeilnehmerdatenLesen.setWeLoginVerify(weLoginVerify);
            weStubBlTeilnehmerdatenLesen.pVorbelegungKartenklasse = pVorbelegungKartenklasse;
            weStubBlTeilnehmerdatenLesen.pEingabeString = pEingabeString;
            weStubBlTeilnehmerdatenLesen.pZulaessigeKartenarten = pZulaessigeKartenarten;

            WEStubBlTeilnehmerdatenLesenRC weStubBlTeilnehmerdatenLesenRC = wsClient
                    .stubBlTeilnehmerdatenLesen(weStubBlTeilnehmerdatenLesen);

            //	    	if (weStubBlTeilnehmerdatenLesenRC.rc<1){
            ////	    		rcFehlerMeldungText="Fehler in der Kommunikation!";
            //	    		return weStubBlTeilnehmerdatenLesenRC.rc;
            //	    	}

            rcFehlerMeldungText = weStubBlTeilnehmerdatenLesenRC.rcFehlerMeldungText;
            rcEclMeldung = weStubBlTeilnehmerdatenLesenRC.rcEclMeldung;
            rcPersonNatJurIdent = weStubBlTeilnehmerdatenLesenRC.rcPersonNatJurIdent;

            return weStubBlTeilnehmerdatenLesenRC.rc;
        }

        rcFehlerMeldungText = "";
        int meldungsIdent = 0;

        dbOpen();

        BlNummernformen blNummernformen = new BlNummernformen(lDbBundle);
        int erg = blNummernformen.dekodiere(pEingabeString, pVorbelegungKartenklasse);
        if (erg < 0) {
            rcFehlerMeldungText = CaFehler.getFehlertext(erg, 0) + ": Bitte Nummer eingeben!";
            dbClose();
            return -1;
        }

        int rcKartenart = blNummernformen.rcKartenart;
        if (pZulaessigeKartenarten != null) {
            int gef = -1;
            for (int i = 0; i < pZulaessigeKartenarten.length; i++) {
                if (pZulaessigeKartenarten[i] == rcKartenart) {
                    gef = 1;
                }
            }
            if (gef == -1) {
                rcFehlerMeldungText = "Eingelesene Nummer: Falsche Kartenart!";
                dbClose();
                return -1;
            }
        }
        if (blNummernformen.rcKartenklasseZuIdentifikationsnummer.get(0) == KonstKartenklasse.stimmkartennummer) {
            String stimmkartennummer = blNummernformen.rcIdentifikationsnummer.get(0);

            lDbBundle.dbStimmkarten.read(stimmkartennummer);
            if (lDbBundle.dbStimmkarten.anzErgebnis() == 0) {
                rcFehlerMeldungText = "Stimmkartennummer nicht vorhanden!";
                dbClose();
                return -1;
            }
            EclStimmkarten eclStimmkarte = lDbBundle.dbStimmkarten.ergebnisPosition(0);
            if (eclStimmkarte.stimmkarteIstGesperrt == 2) {
                rcFehlerMeldungText = "Stimmkartennummer gesperrt!";
                dbClose();
                return -1;
            }
            rcPersonNatJurIdent = eclStimmkarte.personenNatJurIdent;
            meldungsIdent = eclStimmkarte.meldungsIdentAktionaer;
        } else {//Eintrittskartennummer einlesen
            EclZutrittsIdent zutrittsIdent = new EclZutrittsIdent();
            zutrittsIdent.zutrittsIdent = blNummernformen.rcIdentifikationsnummer.get(0);
            zutrittsIdent.zutrittsIdentNeben = blNummernformen.rcIdentifikationsnummerNeben.get(0);

            lDbBundle.dbZutrittskarten.read(zutrittsIdent, 1);
            if (lDbBundle.dbZutrittskarten.anzErgebnis() == 0) {
                rcFehlerMeldungText = "Eintrittskartennummer nicht vorhanden!";
                dbClose();
                return -1;
            }
            EclZutrittskarten eclZutrittskarten = lDbBundle.dbZutrittskarten.ergebnisPosition(0);
            if (eclZutrittskarten.zutrittsIdentWurdeGesperrt()) {
                rcFehlerMeldungText = "Eintrittskartennummer gesperrt!";
                dbClose();
                return -1;
            }
            rcPersonNatJurIdent = eclZutrittskarten.personenNatJurIdent;
            meldungsIdent = eclZutrittskarten.meldungsIdentAktionaer;
        }
        rcEclMeldung = new EclMeldung();
        rcEclMeldung.meldungsIdent = meldungsIdent;

        lDbBundle.dbMeldungen.leseZuMeldungsIdent(rcEclMeldung);
        rcEclMeldung = lDbBundle.dbMeldungen.meldungenArray[0];
        if (rcEclMeldung.meldungstyp == 3) {
            rcFehlerMeldungText = "Meldung in Sammelkarte!";
            dbClose();
            return -1;
        }
        if (rcEclMeldung.meldungstyp == 2) {
            rcFehlerMeldungText = "Meldung ist Sammelkarte!";
            dbClose();
            return -1;
        }
        if (rcEclMeldung.statusPraesenz != 1) {
            rcFehlerMeldungText = "Meldung nicht anwesend!";
            dbClose();
            return -1;
        }

        dbClose();
        return 1;
    }

}
