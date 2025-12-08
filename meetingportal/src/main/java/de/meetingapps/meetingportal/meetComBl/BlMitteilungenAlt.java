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

import java.util.LinkedList;
import java.util.List;

import de.meetingapps.meetingportal.meetComAllg.CaDateiWrite;
import de.meetingapps.meetingportal.meetComAllg.CaDatumZeit;
import de.meetingapps.meetingportal.meetComAllg.CaString;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclAktienregister;
import de.meetingapps.meetingportal.meetComEntities.EclMitteilungenAlt;
import de.meetingapps.meetingportal.meetComEntities.EclPersonenNatJur;
import de.meetingapps.meetingportal.meetComEntities.EclVirtuellerTeilnehmer;
import de.meetingapps.meetingportal.meetComKonst.KonstVerarbeitungslaufArt;
import de.meetingapps.meetingportal.meetComStub.StubRoot;
import de.meetingapps.meetingportal.meetingCoreReport.RpDrucken;
import de.meetingapps.meetingportal.meetingCoreReport.RpVariablen;

@Deprecated
public class BlMitteilungenAlt extends StubRoot {

    /**true => der Aktionär ist eine juristische Person, es liegt keine Vollmacht vor=>
     * Fragen stellen und Mitteilungen machen nicht möglich
     */
    public boolean rcKeineVollmachtFuerMitteilungen = false;

    public List<EclVirtuellerTeilnehmer> rcVirtuelleTeilnehmerList = null;

    public List<EclMitteilungenAlt> rcMitteilungenZuAktionaer = null;

    public List<EclMitteilungenAlt> rcMitteilungen = null;

    public String rcDateiName = "";
    public String rcDateiNamePur = "";

    public EclMitteilungenAlt rcNeueMitteilung = null;

    public BlMitteilungenAlt(boolean pIstServer, DbBundle pDbBundle) {
        super(pIstServer, pDbBundle);
    }

    public int mitteilungenMaxZeichen() {
        int maxZeichen = lDbBundle.param.paramPortal.widerspruecheLaenge;
        return maxZeichen;
    }

    @Deprecated
    public int mitteilungenMaxZeichen(long pAktien, boolean pIstInsti) {
        int maxZeichen = lDbBundle.param.paramPortal.widerspruecheLaenge;
        return maxZeichen;
    }

    public int mitteilungenMaxAnzahl() {
        int maxAnzahl = lDbBundle.param.paramPortal.widerspruecheAnzahlJeAktionaer;
        return maxAnzahl;
    }

    @Deprecated
    public int mitteilungenMaxAnzahl(long pAktien, boolean pIstInsti) {
        int maxAnzahl = lDbBundle.param.paramPortal.widerspruecheAnzahlJeAktionaer;
        return maxAnzahl;
    }

    /**Belegt:
     * rcKeineVollmachtFuerMitteilungen
     * rcVirtuelleTeilnehmerList
     */
    @Deprecated
    public int ermittleMoeglicheMitteilungSteller(int pAktienregisterIdent) {

        dbOpenUndWeitere();

        BlFragen blFragen = new BlFragen(true, lDbBundle);
        blFragen.ermittleMoeglicheFragenSteller(pAktienregisterIdent, false);

        rcKeineVollmachtFuerMitteilungen = blFragen.rcKeineVollmachtFuerFragen;
        rcVirtuelleTeilnehmerList = blFragen.rcVirtuelleTeilnehmerList;
        dbClose();
        return 1;
    }

    /**pVirtuellerTeilnehmer kann null sein - wenn keine Abfrage erfolgte.
     * 
     * pAktienregisterIdent und pVirtuellerTeilnehmer werden in dieser Funktion
     * in pMitteilung überführt.
     * 
     * Zeit wird gesetzt
     * 
     * Neue Mitteilung (vollständig) wird abgelegt in rcNeueMitteilung*/
    public int neueMitteilungSpeichern(EclMitteilungenAlt pMitteilung, int pPersonNatJurMitteilungsStellerIdent,
            EclVirtuellerTeilnehmer pVirtuellerTeilnehmer) {

        dbOpenUndWeitere();
        pMitteilung.vertreterIdent = pPersonNatJurMitteilungsStellerIdent;
        //		if (pVirtuellerTeilnehmer==null) {
        //			pMitteilung.instiIdent=0;
        //			pMitteilung.vertreterIdent=0;
        //		}
        //		else {
        //			switch (pVirtuellerTeilnehmer.art) {
        //			case 1:{
        //				pMitteilung.instiIdent=0;
        //				pMitteilung.vertreterIdent=0;
        //				break;
        //			}
        //			case 2:{
        //				pMitteilung.instiIdent=0;
        //				pMitteilung.vertreterIdent=pVirtuellerTeilnehmer.ident;
        //				break;
        //			}
        //			case 3:{
        //				pMitteilung.instiIdent=pVirtuellerTeilnehmer.ident;
        //				pMitteilung.vertreterIdent=0;
        //				break;
        //			}
        //			}
        //		}
        pMitteilung.zeitpunktDerMitteilung = CaDatumZeit.DatumZeitStringFuerDatenbank();
        lDbBundle.dbMitteilungenAlt.insert(pMitteilung);
        ergaenzeMitteilung(pMitteilung);
        rcNeueMitteilung = pMitteilung;
        dbClose();
        return 1;
    }

    /**
     * Belegt:
     * rcMitteilungenZuAktionaer
     */
    @Deprecated
    public int holeMitteilungenZuAktionaersIdent(int pIdent) {

        rcMitteilungenZuAktionaer = new LinkedList<EclMitteilungenAlt>();
        dbOpenUndWeitere();
        if (pIdent > 0) {
            lDbBundle.dbMitteilungenAlt.readAll_aktionaersIdent(pIdent);
        } else {
            lDbBundle.dbMitteilungenAlt.readAll_instiIdent(pIdent * (-1));
        }
        int anz = lDbBundle.dbMitteilungenAlt.anzErgebnis();
        for (int i = 0; i < anz; i++) {
            EclMitteilungenAlt lMitteilung = lDbBundle.dbMitteilungenAlt.ergebnisPosition(i);

            if (lMitteilung.aktienregisterIdent != 0) {
                lDbBundle.dbAktienregister.leseZuAktienregisterIdent(lMitteilung.aktienregisterIdent);
                EclAktienregister lAktienregisterEintrag = lDbBundle.dbAktienregister.ergebnisPosition(0);
                lMitteilung.nameSteller = lAktienregisterEintrag.vorname + " " + lAktienregisterEintrag.nachname;
                lMitteilung.ortSteller = lAktienregisterEintrag.ort;
            }

            if (lMitteilung.vertreterIdent != 0) {
                lDbBundle.dbPersonenNatJur.read(lMitteilung.vertreterIdent);
                EclPersonenNatJur lPersonNatJur = lDbBundle.dbPersonenNatJur.PersonNatJurGefunden(0);
                lMitteilung.nameSteller = lPersonNatJur.vorname + " " + lPersonNatJur.name;
                lMitteilung.ortSteller = lPersonNatJur.ort;
            }

            if (lMitteilung.instiIdent != 0) {
                lDbBundle.dbPersonenNatJur.read(lMitteilung.instiIdent);
                EclPersonenNatJur lPersonNatJur = lDbBundle.dbPersonenNatJur.PersonNatJurGefunden(0);
                lMitteilung.nameSteller = lPersonNatJur.vorname + " " + lPersonNatJur.name;
                lMitteilung.ortSteller = lPersonNatJur.ort;
            }

            rcMitteilungenZuAktionaer.add(lMitteilung);
        }

        dbClose();

        return 1;
    }

    /**
     * Belegt:
     * rcMitteilungenZuAktionaer
     */
    public int holeMitteilungenZuTeilnehmer(int pIdent) {

        rcMitteilungenZuAktionaer = new LinkedList<EclMitteilungenAlt>();
        dbOpenUndWeitere();
        lDbBundle.dbMitteilungenAlt.readAll_personNatJur(pIdent);
        int anz = lDbBundle.dbMitteilungenAlt.anzErgebnis();
        for (int i = 0; i < anz; i++) {
            EclMitteilungenAlt lMitteilung = lDbBundle.dbMitteilungenAlt.ergebnisPosition(i);
            ergaenzeMitteilung(lMitteilung);
            rcMitteilungenZuAktionaer.add(lMitteilung);

            //		if (lMitteilung.aktienregisterIdent!=0) {
            //			lDbBundle.dbAktienregister.leseZuAktienregisterIdent(lMitteilung.aktienregisterIdent);
            //			EclAktienregister lAktienregisterEintrag=lDbBundle.dbAktienregister.ergebnisPosition(0);
            //			lMitteilung.nameSteller=lAktienregisterEintrag.vorname+" "+lAktienregisterEintrag.nachname;
            //			lMitteilung.ortSteller=lAktienregisterEintrag.ort;
            //		}
            //		
            //		if (lMitteilung.vertreterIdent!=0) {
            //			lDbBundle.dbPersonenNatJur.read(lMitteilung.vertreterIdent);
            //			EclPersonenNatJur lPersonNatJur=lDbBundle.dbPersonenNatJur.PersonNatJurGefunden(0);
            //			lMitteilung.nameSteller=lPersonNatJur.vorname+" "+lPersonNatJur.name;
            //			lMitteilung.ortSteller=lPersonNatJur.ort;
            //		}
            //		
            //		if (lMitteilung.instiIdent!=0) {
            //			lDbBundle.dbPersonenNatJur.read(lMitteilung.instiIdent);
            //			EclPersonenNatJur lPersonNatJur=lDbBundle.dbPersonenNatJur.PersonNatJurGefunden(0);
            //			lMitteilung.nameSteller=lPersonNatJur.vorname+" "+lPersonNatJur.name;
            //			lMitteilung.ortSteller=lPersonNatJur.ort;
            //		}

        }

        dbClose();

        return 1;
    }

    /**Belegt
     * rcMitteilungen
     */
    public int holeMitteilungen(boolean pEntferneNL) {
        return holeNeueMitteilungenIntern(true, pEntferneNL);
    }

    /**Belegt
     * rcFragen
     */
    public int holeNeueMitteilungen(boolean pEntferneNL) {
        return holeNeueMitteilungenIntern(false, pEntferneNL);
    }

    private int holeNeueMitteilungenIntern(boolean pAlle, boolean pEntferneNL) {
        rcMitteilungen = new LinkedList<EclMitteilungenAlt>();

        dbOpenUndWeitere();
        int drucklaufNr = 0;

        if (pAlle == false) {
            /*Neuen Drucklauf erzeugen*/
            BlDrucklauf blDrucklauf = new BlDrucklauf(true, lDbBundle);
            blDrucklauf.erzeugeNeuenDrucklauf(lDbBundle.clGlobalVar.arbeitsplatz, lDbBundle.clGlobalVar.benutzernr,
                    KonstVerarbeitungslaufArt.mitteilungenListe, 0);
            drucklaufNr = blDrucklauf.rcDrucklauf.drucklaufNr;

            int anzMitteilungen = 0;
            anzMitteilungen = lDbBundle.dbMitteilungenAlt.updateNeuerDrucklauf(drucklaufNr);

            /*Drucklauf-Anzahl updaten*/
            blDrucklauf.updateAnzDrucklauf(anzMitteilungen);
        }

        /*Hier: drucklaufNr=0 (alle) oder >0 (nur Update)*/
        lDbBundle.dbMitteilungenAlt.readAll_fragen(drucklaufNr);
        int anz = lDbBundle.dbMitteilungenAlt.anzErgebnis();
        for (int i = 0; i < anz; i++) {
            EclMitteilungenAlt lMitteilung = lDbBundle.dbMitteilungenAlt.ergebnisPosition(i);
            ergaenzeMitteilung(lMitteilung);
            if (pEntferneNL) {
                entferneNL(lMitteilung);
            }
            rcMitteilungen.add(lMitteilung);
        }

        dbClose();
        return 1;
    }

    private int ergaenzeMitteilung(EclMitteilungenAlt lMitteilung) {

        if (lMitteilung.vertreterIdent != 0) {
            lDbBundle.dbPersonenNatJur.read(lMitteilung.vertreterIdent);
            EclPersonenNatJur lPersonNatJur = lDbBundle.dbPersonenNatJur.PersonNatJurGefunden(0);
            lMitteilung.nameSteller = lPersonNatJur.vorname + " " + lPersonNatJur.name;
            lMitteilung.ortSteller = lPersonNatJur.ort;
        }

        //		if (lMitteilung.aktienregisterIdent!=0) {
        //			lDbBundle.dbAktienregister.leseZuAktienregisterIdent(lMitteilung.aktienregisterIdent);
        //			EclAktienregister lAktienregisterEintrag=lDbBundle.dbAktienregister.ergebnisPosition(0);
        //			if (lAktienregisterEintrag==null) {
        //				CaBug.drucke("BlMitteilungen.ergaenzeFrage 001");
        //			}
        //			else {
        //				lMitteilung.aktionaerNummer=lAktienregisterEintrag.aktionaersnummer;
        //				lMitteilung.aktionaerName=lAktienregisterEintrag.nameKomplett;
        //				lMitteilung.aktionaerOrt=lAktienregisterEintrag.ort;
        //				lMitteilung.aktien=lAktienregisterEintrag.stueckAktien;
        //			
        //				lMitteilung.nameSteller=lAktienregisterEintrag.vorname+" "+lAktienregisterEintrag.nachname;
        //				lMitteilung.ortSteller=lAktienregisterEintrag.ort;
        //			}
        //			lMitteilung.stellerIdent="Aktionär";
        //		}
        //		
        //		if (lMitteilung.vertreterIdent!=0) {
        //			lDbBundle.dbPersonenNatJur.read(lMitteilung.vertreterIdent);
        //			EclPersonenNatJur lPersonNatJur=lDbBundle.dbPersonenNatJur.PersonNatJurGefunden(0);
        //			lMitteilung.nameSteller=lPersonNatJur.vorname+" "+lPersonNatJur.name;
        //			lMitteilung.ortSteller=lPersonNatJur.ort;
        //			lMitteilung.stellerIdent="Vertreter";
        //		}
        //
        //		if (lMitteilung.instiIdent!=0) {
        //			lDbBundle.dbPersonenNatJur.read(lMitteilung.instiIdent);
        //			EclPersonenNatJur lPersonNatJur=lDbBundle.dbPersonenNatJur.PersonNatJurGefunden(0);
        //			lMitteilung.nameSteller=lPersonNatJur.vorname+" "+lPersonNatJur.name;
        //			lMitteilung.ortSteller=lPersonNatJur.ort;
        //			lMitteilung.stellerIdent="Insti";
        //		}

        return 1;
    }

    private void entferneNL(EclMitteilungenAlt lMitteilung) {
        lMitteilung.mitteilungtext = lMitteilung.mitteilungtext.replaceAll("\n", "");
        lMitteilung.mitteilungtext = lMitteilung.mitteilungtext.replaceAll("\r", "");

    }

    /**Verwendet
     * rcMitteilungen
     * 
     * Belegt
     * rcDateiName
     */
    public int schreibeMitteilungenListeInCSV() {
        rcDateiName = "";

        CaDateiWrite dateiExport = new CaDateiWrite();
        dateiExport.trennzeichen = ';';
        dateiExport.dateiart = ".csv";

        dateiExport.oeffne(lDbBundle, "mitteilungenListe");

        dateiExport.ausgabe("WiderspruchIdent");
        dateiExport.ausgabe("AktNr");
        dateiExport.ausgabe("AktName");
        dateiExport.ausgabe("AktOrt");
        dateiExport.ausgabe("AktAktien");
        dateiExport.ausgabe("GestelltVon");
        dateiExport.ausgabe("StellerName");
        dateiExport.ausgabe("StellerOrt");
        dateiExport.ausgabe("WiderspruchText");
        dateiExport.ausgabe("Zeitpunkt");
        dateiExport.newline();

        for (int i = 0; i < rcMitteilungen.size(); i++) {
            EclMitteilungenAlt lMitteilung = rcMitteilungen.get(i);
            dateiExport.ausgabe(Integer.toString(lMitteilung.mitteilungIdent));
            dateiExport.ausgabe(lMitteilung.aktionaerNummer);
            dateiExport.ausgabe(lMitteilung.aktionaerName);
            dateiExport.ausgabe(lMitteilung.aktionaerOrt);
            dateiExport.ausgabe(Long.toString(lMitteilung.aktien));
            dateiExport.ausgabe(lMitteilung.stellerIdent);
            dateiExport.ausgabe(lMitteilung.nameSteller);
            dateiExport.ausgabe(lMitteilung.ortSteller);
            dateiExport.ausgabe(lMitteilung.mitteilungtext);
            dateiExport.ausgabe(lMitteilung.zeitpunktDerMitteilung);
            dateiExport.newline();
        }

        dateiExport.schliessen();
        rcDateiName = dateiExport.dateiname;
        rcDateiNamePur = dateiExport.dateinamePur;

        return 1;
    }

    public int erzeugeReport(RpDrucken rpDrucken, String lfdNummer) {

        RpVariablen rpVariablen = new RpVariablen(lDbBundle);
        rpDrucken.initFormular(lDbBundle);
        rpVariablen.mitteilung(lfdNummer, rpDrucken);
        rpDrucken.startFormular();

        for (int i = 0; i < rcMitteilungen.size(); i++) {
            EclMitteilungenAlt lMitteilung = rcMitteilungen.get(i);
            rpVariablen.fuelleVariable(rpDrucken, "Ident", Integer.toString(lMitteilung.mitteilungIdent));
            rpVariablen.fuelleVariable(rpDrucken, "AktNr", lMitteilung.aktionaerNummer);
            rpVariablen.fuelleVariable(rpDrucken, "AktName", lMitteilung.aktionaerName);
            rpVariablen.fuelleVariable(rpDrucken, "AktOrt", lMitteilung.aktionaerOrt);
            rpVariablen.fuelleVariable(rpDrucken, "AktAktien", CaString.toStringDE(lMitteilung.aktien));
            rpVariablen.fuelleVariable(rpDrucken, "GestelltVon", lMitteilung.stellerIdent);
            rpVariablen.fuelleVariable(rpDrucken, "StellerName", lMitteilung.nameSteller);
            rpVariablen.fuelleVariable(rpDrucken, "StellerOrt", lMitteilung.ortSteller);
            rpVariablen.fuelleVariable(rpDrucken, "ZuTOP", "");
            rpVariablen.fuelleVariable(rpDrucken, "Inhalt", "Widerspruch: " + lMitteilung.mitteilungtext);
            rpVariablen.fuelleVariable(rpDrucken, "Zeitpunkt",
                    CaDatumZeit.DatumZeitStringFuerAnzeige(lMitteilung.zeitpunktDerMitteilung));
            rpDrucken.druckenFormular();
        }

        rpDrucken.endeFormular();

        return 1;
    }

}
