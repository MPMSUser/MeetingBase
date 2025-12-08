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

import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclMeldung;
import de.meetingapps.meetingportal.meetComEntities.EclPraesenz;
import de.meetingapps.meetingportal.meetComEntities.EclWillensErklVollmachtenAnDritte;

/**Nur vorläufig - für Seminar-Pilot!*/
public class BlPraesenzPROV {

    public EclPraesenz praesenz = null;

    public int meldeIdentAktionaer = 0;
    public int meldeIdentGast = 0;
    public int meldeIdent = 0;

    /**Die Daten der Person der Anmeldung*/
    public int meldePersonNatJur = 0;
    public String meldeName = "";
    public String meldeVorname = "";
    public String meldeOrt = "";

    public long meldeAktien = 0;
    public int meldeKlasse = 0;

    /**Die Daten des tatsächlichen Teilnehmers (d.h: falls <> meldePersonNatJur, dann Vertreter)*/
    public int vertreterPersonNatJur = 0;
    public String vertreterName = "";
    public String vertreterVorname = "";
    public String vertreterOrt = "";

    /**Z.B. bei Personengemeinschaften! 0=nein, 1=ja*/
    public int meldePersonDarfVertreten = 0;

    /**Array mit den dem Aktionär meldeIdent zugeordneten Vollmachten. Sowohl
     * Gültige als auch Widerrufene!
     */
    public List<EclWillensErklVollmachtenAnDritte> rcVollmachtenAnDritte = null;
    public List<EclWillensErklVollmachtenAnDritte> rcVollmachtenAnDritteStorniert = null;

    /*Bereits ersetzt*/
    @Deprecated
    public int leseStatusMeldeIdent(DbBundle pDbBundle, int pMeldungsIdent) {
        int erg;
        erg = pDbBundle.dbPraesenz.read(pMeldungsIdent);
        if (erg < 1) {
            praesenz = new EclPraesenz();
            praesenz.meldungsIdent = pMeldungsIdent;
            praesenz.istPraesent = 0;
        } else {
            praesenz = pDbBundle.dbPraesenz.praesenzArray[0];
        }

        /*Anmeldedaten füllen*/
        meldeIdent = praesenz.meldungsIdent;
        /*PersonNatJur direkt zu dieser Meldung ermitteln*/
        EclMeldung lMeldung = new EclMeldung();
        lMeldung.meldungsIdent = meldeIdent;
        pDbBundle.dbMeldungen.leseZuMeldungsIdent(lMeldung);
        meldePersonNatJur = pDbBundle.dbMeldungen.meldungenArray[0].personenNatJurIdent;
        meldeName = pDbBundle.dbMeldungen.meldungenArray[0].name;
        meldeVorname = pDbBundle.dbMeldungen.meldungenArray[0].vorname;
        meldeOrt = pDbBundle.dbMeldungen.meldungenArray[0].ort;
        meldeAktien = pDbBundle.dbMeldungen.meldungenArray[0].stimmen;
        meldeKlasse = pDbBundle.dbMeldungen.meldungenArray[0].klasse;
        if (meldeVorname.contains(" und ")) {
            meldePersonDarfVertreten = 0;
        } else {
            meldePersonDarfVertreten = 1;
        }

        if (praesenz.istPraesent == 1) { /*Noch natPerson einlesen, für Anzeige "Teilnehmer"*/
            pDbBundle.dbPersonenNatJur.read(praesenz.identPersonNatJur);
            vertreterPersonNatJur = praesenz.identPersonNatJur;
            vertreterName = pDbBundle.dbPersonenNatJur.PersonNatJurGefunden(0).name;
            vertreterVorname = pDbBundle.dbPersonenNatJur.PersonNatJurGefunden(0).vorname;
            vertreterOrt = pDbBundle.dbPersonenNatJur.PersonNatJurGefunden(0).ort;
        }

        return praesenz.istPraesent;

    }

    @Deprecated
    public int leseStatusEK(DbBundle pDbBundle, int pEK) {
        int erg;

        int hMeldeIdentAktionaer = 0, hMeldeIdentGast = 0, hMeldeIdent = 0;

        /*TODO #lahmgelegt temporär*/
        //		pDbBundle.dbZutrittskarten.read_alleVersionen(Integer.toString(pEK)); 
        if (pDbBundle.dbZutrittskarten.anzErgebnis() == 0) {
            return (CaFehler.pmZutrittsIdentNichtVorhanden);
        }

        hMeldeIdentAktionaer = pDbBundle.dbZutrittskarten.ergebnisPosition(0).meldungsIdentAktionaer;
        if (hMeldeIdentAktionaer != 0) {
            hMeldeIdent = hMeldeIdentAktionaer;
        }

        hMeldeIdentGast = pDbBundle.dbZutrittskarten.ergebnisPosition(0).meldungsIdentGast;
        if (hMeldeIdentGast != 0) {
            hMeldeIdent = hMeldeIdentGast;
        }

        erg = this.leseStatusMeldeIdent(pDbBundle, hMeldeIdent);
        return erg;
        /*
        erg=pDbBundle.dbPraesenz.read(hMeldeIdent);
        System.out.println("leseStatus erg="+erg);
        if (erg<1){
        	praesenz=new EclPraesenz();
        	return 0;}
        
        praesenz=pDbBundle.dbPraesenz.praesenzArray[0];
        return praesenz.istPraesent;
        */
    }

    /**Hinweis: setzePraesenz* wird immer durchgeführt. D.h. z.B. setzePraesent wird auch durchgeführt, wenn der Satz bereits präsent
     * ist. D.h. die Funktionen können auch zum "Umschiesen" z.B. von einer präsenten Person auf die andere präsente Person verwendet werden!
     */
    private int setzePraesenzStatus(int pIstPraesent, DbBundle pDbBundle, int pMeldungsIdent, int pKartenart,
            int pKartennr, int pIdentPersonNatJur) {
        int erg;
        pDbBundle.dbBasis.beginTransaction();
        erg = pDbBundle.dbPraesenz.read(pMeldungsIdent);
        if (erg < 1) {/*Dann noch nicht vorhanden*/

            praesenz = new EclPraesenz();
            praesenz.meldungsIdent = pMeldungsIdent;
            praesenz.kartenart = pKartenart;
            praesenz.kartennr = pKartennr;
            praesenz.identPersonNatJur = pIdentPersonNatJur;
            praesenz.istPraesent = pIstPraesent;
            erg = pDbBundle.dbPraesenz.insert(praesenz);
        } else {
            praesenz = pDbBundle.dbPraesenz.praesenzArray[0];
            praesenz.meldungsIdent = pMeldungsIdent;
            praesenz.kartenart = pKartenart;
            praesenz.kartennr = pKartennr;
            praesenz.identPersonNatJur = pIdentPersonNatJur;
            praesenz.istPraesent = pIstPraesent;
            erg = pDbBundle.dbPraesenz.update(praesenz);

        }
        pDbBundle.dbBasis.endTransaction();

        return 1;
    }

    public int setzePraesent(DbBundle pDbBundle, int pMeldungsIdent, int pKartenart, int pKartennr,
            int pIdentPersonNatJur) {

        int rc;
        rc = this.setzePraesenzStatus(1, pDbBundle, pMeldungsIdent, pKartenart, pKartennr, pIdentPersonNatJur);
        return rc;
    }

    public int ssetzeAbwesend(DbBundle pDbBundle, int pMeldungsIdent) {

        int rc;
        rc = this.setzePraesenzStatus(0, pDbBundle, pMeldungsIdent, 0, 0, 0);
        return rc;
    }

    /****************************************************************
     * Rückgabeparameter:
     * 
     * meldeIdent=Ident der Meldung, die dieser Eintrittskarte zugeordnet ist
    
     */
    @Deprecated
    public int lieferePersonenZuEK(DbBundle pDbBundle, int ekNr) {

        /*MeldeIdent zur Eintrittskarte ermitteln*/
        /*TODO #lahmgelegt temporär*/
        //		pDbBundle.dbZutrittskarten.read_alleVersionen(Integer.toString(ekNr));
        if (pDbBundle.dbZutrittskarten.anzErgebnis() == 0) {
            return (CaFehler.pmZutrittsIdentNichtVorhanden);
        }

        meldeIdentAktionaer = pDbBundle.dbZutrittskarten.ergebnisPosition(0).meldungsIdentAktionaer;
        if (meldeIdentAktionaer != 0) {
            meldeIdent = meldeIdentAktionaer;
        }

        meldeIdentGast = pDbBundle.dbZutrittskarten.ergebnisPosition(0).meldungsIdentGast;
        if (meldeIdentGast != 0) {
            meldeIdent = meldeIdentGast;
        }

        /*PersonNatJur direkt zu dieser Meldung ermitteln*/
        EclMeldung lMeldung = new EclMeldung();
        lMeldung.meldungsIdent = meldeIdent;
        pDbBundle.dbMeldungen.leseZuMeldungsIdent(lMeldung);
        meldePersonNatJur = pDbBundle.dbMeldungen.meldungenArray[0].personenNatJurIdent;
        meldeName = pDbBundle.dbMeldungen.meldungenArray[0].name;
        meldeVorname = pDbBundle.dbMeldungen.meldungenArray[0].vorname;
        meldeOrt = pDbBundle.dbMeldungen.meldungenArray[0].ort;
        if (meldeVorname.contains(" und ")) {
            meldePersonDarfVertreten = 0;
        } else {
            meldePersonDarfVertreten = 1;
        }
        meldeAktien = pDbBundle.dbMeldungen.meldungenArray[0].stimmen;
        meldeKlasse = pDbBundle.dbMeldungen.meldungenArray[0].klasse;

        System.out.println("Liefere d");

        /*Alle Vollmachten zu dieser MeldeIdent ermitteln*/
        if (meldeIdentAktionaer != 0) {
            BlWillenserklaerung lWillenserklaerung = new BlWillenserklaerung();
            lWillenserklaerung.setzeDbBundle(pDbBundle);
            lWillenserklaerung.piMeldungsIdentAktionaer = meldeIdent;

            lWillenserklaerung.einlesenVollmachtenAnDritte();
            int i;
            rcVollmachtenAnDritte = new LinkedList<EclWillensErklVollmachtenAnDritte>();
            rcVollmachtenAnDritteStorniert = new LinkedList<EclWillensErklVollmachtenAnDritte>();
            if (lWillenserklaerung.rcVollmachtenAnDritteAnzahl > 0) {
                for (i = 0; i < lWillenserklaerung.rcVollmachtenAnDritte.length; i++) {
                    if (lWillenserklaerung.rcVollmachtenAnDritte[i].wurdeStorniert == false) {
                        rcVollmachtenAnDritte.add(lWillenserklaerung.rcVollmachtenAnDritte[i]);
                    } else {
                        rcVollmachtenAnDritteStorniert.add(lWillenserklaerung.rcVollmachtenAnDritte[i]);
                    }
                }
            }
        }

        return 0;
    }
}
