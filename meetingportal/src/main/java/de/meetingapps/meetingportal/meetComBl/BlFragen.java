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

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaDateiWrite;
import de.meetingapps.meetingportal.meetComAllg.CaDatumZeit;
import de.meetingapps.meetingportal.meetComAllg.CaString;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclAktienregister;
import de.meetingapps.meetingportal.meetComEntities.EclFragen;
import de.meetingapps.meetingportal.meetComEntities.EclPersonenNatJur;
import de.meetingapps.meetingportal.meetComEntities.EclVirtuellerTeilnehmer;
import de.meetingapps.meetingportal.meetComKonst.KonstVerarbeitungslaufArt;
import de.meetingapps.meetingportal.meetComStub.StubRoot;
import de.meetingapps.meetingportal.meetingCoreReport.RpDrucken;
import de.meetingapps.meetingportal.meetingCoreReport.RpVariablen;

@Deprecated
public class BlFragen extends StubRoot {

    private int logDrucken = 10;

    /**true => der Aktionär ist eine juristische Person, es liegt keine Vollmacht vor=>
     * Fragen stellen und Mitteilungen machen nicht möglich
     */
    public boolean rcKeineVollmachtFuerFragen = false;

    public List<EclVirtuellerTeilnehmer> rcVirtuelleTeilnehmerList = null;

    public List<EclFragen> rcFragenZuAktionaer = null;

    public List<EclFragen> rcFragen = null;

    public String rcDateiName = "";
    public String rcDateiNamePur = "";

    public EclFragen rcNeueFrage = null;

    public BlFragen(boolean pIstServer, DbBundle pDbBundle) {
        super(pIstServer, pDbBundle);
    }

    public int fragenMaxZeichen() {
        int maxZeichen = 0;
        maxZeichen = lDbBundle.param.paramPortal.fragenLaenge;
        return maxZeichen;
    }

    @Deprecated
    public int fragenMaxZeichen(long pAktien, boolean pIstInsti) {
        int maxZeichen = 0;

        if (pAktien <= lDbBundle.param.paramPortal.fragenBisAktien) {
            maxZeichen = lDbBundle.param.paramPortal.fragenLaenge;
        }
        if (pAktien > lDbBundle.param.paramPortal.fragenBisAktien
                && pAktien < lDbBundle.param.paramPortal.fragenAbAktien) {
            maxZeichen = lDbBundle.param.paramPortal.fragenLaengeStufe2;
        }
        if (pAktien >= lDbBundle.param.paramPortal.fragenAbAktien) {
            maxZeichen = lDbBundle.param.paramPortal.fragenLaengeStufe3;
        }

        if (pIstInsti) {
            maxZeichen = lDbBundle.param.paramPortal.fragenLaengeStufe4;
        }
        return maxZeichen;
    }

    public int fragenMaxFragen() {
        CaBug.druckeLog("", logDrucken, 10);
        int maxFragen = 0;

        maxFragen = lDbBundle.param.paramPortal.fragenAnzahlJeAktionaer;
        CaBug.druckeLog("maxFragen=" + maxFragen, logDrucken, 10);
        return maxFragen;
    }

    @Deprecated
    public int fragenMaxFragen(long pAktien, boolean pIstInsti) {
        CaBug.druckeLog("", logDrucken, 10);
        CaBug.druckeLog("pAktien=" + pAktien + " pIstInsti=" + pIstInsti, logDrucken, 10);
        int maxFragen = 0;

        if (pAktien <= lDbBundle.param.paramPortal.fragenBisAktien) {
            maxFragen = lDbBundle.param.paramPortal.fragenAnzahlJeAktionaer;
        }
        if (pAktien > lDbBundle.param.paramPortal.fragenBisAktien
                && pAktien < lDbBundle.param.paramPortal.fragenAbAktien) {
            maxFragen = lDbBundle.param.paramPortal.fragenAnzahlStufe2;
        }
        if (pAktien >= lDbBundle.param.paramPortal.fragenAbAktien) {
            maxFragen = lDbBundle.param.paramPortal.fragenAnzahlStufe3;
        }

        if (pIstInsti) {
            maxFragen = lDbBundle.param.paramPortal.fragenAnzahlStufe4;
        }
        CaBug.druckeLog("maxFragen=" + maxFragen, logDrucken, 10);
        return maxFragen;
    }

    /**Belegt:
     * rcKeineVollmachtFuerFragen
     * 
     * pAktienregisterIdent<0 => ist PersonNatJur
     * 
     * pDirektaufruf=true => die Funktion wurde direkt als Fragensteller aufgerufen
     * =false => Funktion wurde zur Ermittlung des Mitteilungsstellers aufgerufen
     */
    @Deprecated
    public int ermittleMoeglicheFragenSteller(int pAktienregisterIdent, boolean pDirektaufruf) {

        dbOpenUndWeitere();

        int pZulaessig = lDbBundle.param.paramPortal.fragenStellerZulaessig;
        if (pDirektaufruf == false) {
            pZulaessig = lDbBundle.param.paramPortal.widerspruecheStellerZulaessig;
        }

        rcKeineVollmachtFuerFragen = false;
        rcVirtuelleTeilnehmerList = new LinkedList<EclVirtuellerTeilnehmer>();

        if (pAktienregisterIdent > 0) {
            /*Aktienregister-Eintrag lesen*/
            lDbBundle.dbAktienregister.leseZuAktienregisterIdent(pAktienregisterIdent);
            EclAktienregister lAktienregisterEintrag = lDbBundle.dbAktienregister.ergebnisPosition(0);

            /*Vollmachten einlesen*/
            BlWillenserklaerungStatus blWillenserklaerungStatus = new BlWillenserklaerungStatus(lDbBundle);
            blWillenserklaerungStatus.piRueckgabeKurzOderLang = 2;
            blWillenserklaerungStatus.piAnsichtVerarbeitungOderAnalyse = 2;
            blWillenserklaerungStatus.leseMeldungenZuAktienregisterIdent(pAktienregisterIdent);
            blWillenserklaerungStatus.ergaenzeZugeordneteMeldungenUmWillenserklaerungen(-2);
            blWillenserklaerungStatus.ermittleVollmachtenAusMeldungenUndWillenserklaerungen();

            /*Falls Aktionär juristische Person oder Personengemeinschaft ist, und keine Vollmachten vorliegen*/
            //		if ((lAktienregisterEintrag.istPersonengemeinschaft==1 || lAktienregisterEintrag.istJuristischePerson==1)
            //				&& blWillenserklaerungStatus.rcListeVollmachten.size()==0) {
            //			rcKeineVollmachtFuerFragen=true;
            //			dbClose();
            //			return 1;
            //		}

            int lfdNummerListe = 0;
            /*Selbst*/
            if ((lAktienregisterEintrag.istPersonengemeinschaft != 1 || (pZulaessig & 1) == 1)
                    && (lAktienregisterEintrag.istJuristischePerson != 1 || (pZulaessig & 2) == 2)) {
                EclVirtuellerTeilnehmer lVirtuellerTeilnehmerSelbst = new EclVirtuellerTeilnehmer();
                lVirtuellerTeilnehmerSelbst.laufendeIdent = lfdNummerListe;
                lVirtuellerTeilnehmerSelbst.art = 1;
                lVirtuellerTeilnehmerSelbst.name = lAktienregisterEintrag.vorname + " "
                        + lAktienregisterEintrag.nachname;
                lVirtuellerTeilnehmerSelbst.ort = lAktienregisterEintrag.ort;
                rcVirtuelleTeilnehmerList.add(lVirtuellerTeilnehmerSelbst);
                lfdNummerListe++;
            }
            /*Vollmachten*/
            for (int i = 0; i < blWillenserklaerungStatus.rcListeVollmachten.size(); i++) {
                EclPersonenNatJur lPersonNatJur = blWillenserklaerungStatus.rcListeVollmachten.get(i);
                EclVirtuellerTeilnehmer lVirtuellerTeilnehmerSelbst = new EclVirtuellerTeilnehmer();
                lVirtuellerTeilnehmerSelbst.laufendeIdent = lfdNummerListe;
                lVirtuellerTeilnehmerSelbst.art = 2;
                lVirtuellerTeilnehmerSelbst.name = lPersonNatJur.vorname + " " + lPersonNatJur.name;
                lVirtuellerTeilnehmerSelbst.ort = lPersonNatJur.ort;
                lVirtuellerTeilnehmerSelbst.ident = lPersonNatJur.ident;
                rcVirtuelleTeilnehmerList.add(lVirtuellerTeilnehmerSelbst);
                lfdNummerListe++;
            }
            /*Sonstige Person*/
            EclVirtuellerTeilnehmer lVirtuellerTeilnehmerSelbst = new EclVirtuellerTeilnehmer();
            lVirtuellerTeilnehmerSelbst.laufendeIdent = lfdNummerListe;
            lVirtuellerTeilnehmerSelbst.art = 4;
            lVirtuellerTeilnehmerSelbst.name = "Sonstige Person";
            rcVirtuelleTeilnehmerList.add(lVirtuellerTeilnehmerSelbst);
            lfdNummerListe++;
        } else {
            lDbBundle.dbPersonenNatJur.read(pAktienregisterIdent * (-1));
            EclPersonenNatJur lPersonNatJur = lDbBundle.dbPersonenNatJur.PersonNatJurGefunden(0);
            int lfdNummerListe = 0;
            EclVirtuellerTeilnehmer lVirtuellerTeilnehmerSelbst = new EclVirtuellerTeilnehmer();
            lVirtuellerTeilnehmerSelbst.laufendeIdent = lfdNummerListe;
            lVirtuellerTeilnehmerSelbst.art = 3;
            if (lPersonNatJur.kommunikationssprache > 0) {
                lVirtuellerTeilnehmerSelbst.art = 3;
            }
            if (lPersonNatJur.kommunikationssprache == -1) {
                lVirtuellerTeilnehmerSelbst.art = 4;
            }
            lVirtuellerTeilnehmerSelbst.name = lPersonNatJur.vorname + " " + lPersonNatJur.name;
            lVirtuellerTeilnehmerSelbst.ort = lPersonNatJur.ort;
            lVirtuellerTeilnehmerSelbst.ident = lPersonNatJur.ident;
            rcVirtuelleTeilnehmerList.add(lVirtuellerTeilnehmerSelbst);
            lfdNummerListe++;

        }

        dbClose();
        return 1;
    }

    /**pVirtuellerTeilnehmer kann null sein - wenn keine Abfrage erfolgte.
     * 
     * pAktienregisterIdent und pVirtuellerTeilnehmer werden in dieser Funktion
     * in pFrage überführt.
     * 
     * Zeit wird gesetzt*/
    public int neueFrageSpeichern(EclFragen pFrage, int pPersonNatJurFragenStellerIdent,
            EclVirtuellerTeilnehmer pVirtuellerTeilnehmer) {

        dbOpenUndWeitere();
        pFrage.vertreterIdent = pPersonNatJurFragenStellerIdent;

        //		if (pVirtuellerTeilnehmer==null) {
        //			pFrage.instiIdent=0;
        //			pFrage.vertreterIdent=0;
        //		}
        //		else {
        //			switch (pVirtuellerTeilnehmer.art) {
        //			case 1:{
        //				pFrage.instiIdent=0;
        //				pFrage.vertreterIdent=0;
        //				break;
        //			}
        //			case 2:{
        //				pFrage.instiIdent=0;
        //				pFrage.vertreterIdent=pVirtuellerTeilnehmer.ident;
        //				break;
        //			}
        //			case 3:{
        //				pFrage.instiIdent=pVirtuellerTeilnehmer.ident;
        //				pFrage.vertreterIdent=0;
        //				break;
        //			}
        //			}
        //		}

        pFrage.zeitpunktDerFrage = CaDatumZeit.DatumZeitStringFuerDatenbank();
        lDbBundle.dbFragen.insert(pFrage);
        ergaenzeFrage(pFrage);
        rcNeueFrage = pFrage;
        dbClose();
        return 1;
    }

    /**
     * Belegt:
     * rcFragenZuAktionaer
     */
    @Deprecated
    public int holeFragenZuAktionaersIdent(int pIdent) {

        rcFragenZuAktionaer = new LinkedList<EclFragen>();
        dbOpenUndWeitere();
        if (pIdent > 0) {
            lDbBundle.dbFragen.readAll_aktionaersIdent(pIdent);
        } else {
            lDbBundle.dbFragen.readAll_instiIdent(pIdent * (-1));
        }
        int anz = lDbBundle.dbFragen.anzErgebnis();
        for (int i = 0; i < anz; i++) {
            EclFragen lFrage = lDbBundle.dbFragen.ergebnisPosition(i);
            ergaenzeFrage(lFrage);
            rcFragenZuAktionaer.add(lFrage);
        }

        dbClose();

        return 1;
    }

    /**
     * Belegt:
     * rcFragenZuAktionaer
     */
    public int holeFragenZuTeilnehmer(int pIdent) {

        rcFragenZuAktionaer = new LinkedList<EclFragen>();
        dbOpenUndWeitere();
        lDbBundle.dbFragen.readAll_personNatJur(pIdent);
        int anz = lDbBundle.dbFragen.anzErgebnis();
        for (int i = 0; i < anz; i++) {
            EclFragen lFrage = lDbBundle.dbFragen.ergebnisPosition(i);
            ergaenzeFrage(lFrage);
            rcFragenZuAktionaer.add(lFrage);
        }

        dbClose();

        return 1;
    }

    /**Belegt
     * rcFragen
     */
    public int holeFragen(boolean pEntferneNL) {
        return holeNeueFragenIntern(true, pEntferneNL);
    }

    /**Belegt
     * rcFragen
     */
    public int holeNeueFragen(boolean pEntferneNL) {
        return holeNeueFragenIntern(false, pEntferneNL);
    }

    private int holeNeueFragenIntern(boolean pAlle, boolean pEntferneNL) {
        rcFragen = new LinkedList<EclFragen>();

        dbOpenUndWeitere();
        int drucklaufNr = 0;

        if (pAlle == false) {
            /*Neuen Drucklauf erzeugen*/
            BlDrucklauf blDrucklauf = new BlDrucklauf(true, lDbBundle);
            blDrucklauf.erzeugeNeuenDrucklauf(lDbBundle.clGlobalVar.arbeitsplatz, lDbBundle.clGlobalVar.benutzernr,
                    KonstVerarbeitungslaufArt.fragenListe, 0);
            drucklaufNr = blDrucklauf.rcDrucklauf.drucklaufNr;

            int anzFragen = 0;
            anzFragen = lDbBundle.dbFragen.updateNeuerDrucklauf(drucklaufNr);

            /*Drucklauf-Anzahl updaten*/
            blDrucklauf.updateAnzDrucklauf(anzFragen);
        }

        /*Hier: drucklaufNr=0 (alle) oder >0 (nur Update)*/
        lDbBundle.dbFragen.readAll_fragen(drucklaufNr);
        int anz = lDbBundle.dbFragen.anzErgebnis();
        for (int i = 0; i < anz; i++) {
            EclFragen lFrage = lDbBundle.dbFragen.ergebnisPosition(i);
            ergaenzeFrage(lFrage);
            if (pEntferneNL) {
                entferneNL(lFrage);
            }
            rcFragen.add(lFrage);
        }

        dbClose();
        return 1;
    }

    private int ergaenzeFrage(EclFragen lFrage) {
        if (lFrage.vertreterIdent != 0) {
            lDbBundle.dbPersonenNatJur.read(lFrage.vertreterIdent);
            EclPersonenNatJur lPersonNatJur = lDbBundle.dbPersonenNatJur.PersonNatJurGefunden(0);
            lFrage.nameSteller = lPersonNatJur.vorname + " " + lPersonNatJur.name;
            lFrage.ortSteller = lPersonNatJur.ort;
        }

        //		if (lFrage.vertreterIdent!=0) {
        //			lDbBundle.dbAktienregister.leseZuAktienregisterIdent(lFrage.aktienregisterIdent);
        //			EclAktienregister lAktienregisterEintrag=lDbBundle.dbAktienregister.ergebnisPosition(0);
        //			if (lAktienregisterEintrag==null) {
        //				CaBug.drucke("BlFragen.ergaenzeFrage 001");
        //			}
        //			else {
        //				lFrage.aktionaerNummer=lAktienregisterEintrag.aktionaersnummer;
        //				lFrage.aktionaerName=lAktienregisterEintrag.nameKomplett;
        //				lFrage.aktionaerOrt=lAktienregisterEintrag.ort;
        //				lFrage.aktien=lAktienregisterEintrag.stueckAktien;
        //			
        //				lFrage.nameSteller=lAktienregisterEintrag.vorname+" "+lAktienregisterEintrag.nachname;
        //				lFrage.ortSteller=lAktienregisterEintrag.ort;
        //			}
        //			lFrage.stellerIdent="Aktionär";
        //		}
        //		
        //		if (lFrage.vertreterIdent!=0) {
        //			lDbBundle.dbPersonenNatJur.read(lFrage.vertreterIdent);
        //			EclPersonenNatJur lPersonNatJur=lDbBundle.dbPersonenNatJur.PersonNatJurGefunden(0);
        //			lFrage.nameSteller=lPersonNatJur.vorname+" "+lPersonNatJur.name;
        //			lFrage.ortSteller=lPersonNatJur.ort;
        //			lFrage.stellerIdent="Vertreter";
        //		}
        //
        //		if (lFrage.instiIdent!=0) {
        //			lDbBundle.dbPersonenNatJur.read(lFrage.instiIdent);
        //			EclPersonenNatJur lPersonNatJur=lDbBundle.dbPersonenNatJur.PersonNatJurGefunden(0);
        //			lFrage.nameSteller=lPersonNatJur.vorname+" "+lPersonNatJur.name;
        //			lFrage.ortSteller=lPersonNatJur.ort;
        //			lFrage.stellerIdent="Insti";
        //		}

        return 1;
    }

    private void entferneNL(EclFragen lFrage) {
        lFrage.fragentext = lFrage.fragentext.replaceAll("\n", "");
        lFrage.fragentext = lFrage.fragentext.replaceAll("\r", "");

        lFrage.zuTop = lFrage.zuTop.replaceAll("\n", "");
        lFrage.zuTop = lFrage.zuTop.replaceAll("\r", "");
    }

    /**Verwendet
     * rcFragen
     * 
     * Belegt
     * rcDateiName
     */
    public int schreibeFragenListeInCSV() {
        rcDateiName = "";

        CaDateiWrite dateiExport = new CaDateiWrite();
        dateiExport.trennzeichen = ';';
        dateiExport.dateiart = ".csv";

        dateiExport.oeffne(lDbBundle, "fragenListe");

        dateiExport.ausgabe("FrageIdent");
        dateiExport.ausgabe("AktNr");
        dateiExport.ausgabe("AktName");
        dateiExport.ausgabe("AktOrt");
        dateiExport.ausgabe("AktAktien");
        dateiExport.ausgabe("GestelltVon");
        dateiExport.ausgabe("StellerName");
        dateiExport.ausgabe("StellerOrt");
        dateiExport.ausgabe("ZuTOP");
        dateiExport.ausgabe("FragenText");
        dateiExport.ausgabe("Zeitpunkt");
        dateiExport.newline();

        for (int i = 0; i < rcFragen.size(); i++) {
            EclFragen lFrage = rcFragen.get(i);
            dateiExport.ausgabe(Integer.toString(lFrage.frageIdent));
            dateiExport.ausgabe(lFrage.aktionaerNummer);
            dateiExport.ausgabe(lFrage.aktionaerName);
            dateiExport.ausgabe(lFrage.aktionaerOrt);
            dateiExport.ausgabe(Long.toString(lFrage.aktien));
            dateiExport.ausgabe(lFrage.stellerIdent);
            dateiExport.ausgabe(lFrage.nameSteller);
            dateiExport.ausgabe(lFrage.ortSteller);
            dateiExport.ausgabe(lFrage.zuTop);
            dateiExport.ausgabe(lFrage.fragentext);
            dateiExport.ausgabe(lFrage.zeitpunktDerFrage);
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

        for (int i = 0; i < rcFragen.size(); i++) {
            EclFragen lFrage = rcFragen.get(i);
            rpVariablen.fuelleVariable(rpDrucken, "Ident", Integer.toString(lFrage.frageIdent));
            rpVariablen.fuelleVariable(rpDrucken, "AktNr", lFrage.aktionaerNummer);
            rpVariablen.fuelleVariable(rpDrucken, "AktName", lFrage.aktionaerName);
            rpVariablen.fuelleVariable(rpDrucken, "AktOrt", lFrage.aktionaerOrt);
            rpVariablen.fuelleVariable(rpDrucken, "AktAktien", CaString.toStringDE(lFrage.aktien));
            rpVariablen.fuelleVariable(rpDrucken, "GestelltVon", lFrage.stellerIdent);
            rpVariablen.fuelleVariable(rpDrucken, "StellerName", lFrage.nameSteller);
            rpVariablen.fuelleVariable(rpDrucken, "StellerOrt", lFrage.ortSteller);
            rpVariablen.fuelleVariable(rpDrucken, "ZuTOP", lFrage.zuTop);
            rpVariablen.fuelleVariable(rpDrucken, "Inhalt", "Frage: " + lFrage.fragentext);
            rpVariablen.fuelleVariable(rpDrucken, "Zeitpunkt",
                    CaDatumZeit.DatumZeitStringFuerAnzeige(lFrage.zeitpunktDerFrage));
            rpDrucken.druckenFormular();
        }

        rpDrucken.endeFormular();

        return 1;
    }
}
