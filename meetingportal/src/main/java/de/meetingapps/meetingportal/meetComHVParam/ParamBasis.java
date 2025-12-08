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
package de.meetingapps.meetingportal.meetComHVParam;

import java.io.Serializable;

import de.meetingapps.meetingportal.meetComAllg.CaString;

public class ParamBasis implements Serializable {
    private static final long serialVersionUID = -7728140314465121253L;

    /**-1: nichts ausgewählt, bzw. presetArt nicht vorhanden*/
    public int[] veranstaltungstyp= {0, -1, -1, -1, -1, -1, -1, -1, -1, -1}; //früher: 99; jetzt: 2670 bis 2679
    
    public Boolean inhaberaktienAktiv = true; //60
    public Boolean namensaktienAktiv = true; //61

    /**1=Aktionäre; 2=Mitglieder*/
    public int investorenSind = 1; //78
    public int anzahlGattungen = 5; //kein pflegbarer Parameter, eher Systemkonstante

    /**********************************Kapital und Gattungen einschließlich Rechenfunktionen*********************************************/

    /**Hinweis zu gattungsarray:
     * Prinzipiell werden die Gattungen von 1 bis 5 (siehe EclMeldung) angestrebt / unterstützt (angeblich 2 bis 5 noch nicht implementiert - checken ...).
     * Diese werden hier im Array auf [0] bis [4] abgebildet.
     * Gattung 0 soll automatisch auf Gattung 1 abgebildet werden (default).
     * Der Zugriff sollte deshalb am besten über die Zugriffsfunktionen durchgeführt werden - dort angabe von 1 bis 5 als Gattung, 0 wird umgesetzt.
     * 
       [gattung-1]*/
    public Boolean[] gattungAktiv = { true, false, false, false, false }; //62.0 bis 62.5

    /**gattung=1 bis 5. 0 wird auf 1 umgesetzt.*/
    public boolean getGattungAktiv(int gattung) {
        if (gattung == 0) {
            gattung = 1;
        }
        return gattungAktiv[gattung - 1];
    }

    public boolean mehrereGattungenAktiv() {
        int gef=0;
        for (int i=1;i<=5;i++) {
            if (gattungAktiv[i-1]==true) {gef++;}
        }
        return (gef>1);
    }
   
    /**[gattung-1]*/
    public String[] gattungBezeichnung = { "Stammaktien", "Vorzugsaktien", "", "", "" }; //63, 64, 65, 66, 67

    /**gattung=1 bis 5. 0 wird auf 1 umgesetzt.*/
    public String getGattungBezeichnung(int gattung) {
        if (gattung == 0) {
            gattung = 1;
        }
        return gattungBezeichnung[gattung - 1];
    }

    /**[gattung-1]*/
    public String[] gattungBezeichnungEN = { "Common Shares", "Preferred Stock", "", "", "" }; //93, 94, 95, 96, 97

    /**gattung=1 bis 5. 0 wird auf 1 umgesetzt.*/
    public String getGattungBezeichnungEN(int gattung) {
        if (gattung == 0) {
            gattung = 1;
        }
        return gattungBezeichnungEN[gattung - 1];
    }
    
   
    /**[gattung-1]*/
    public String[] gattungBezeichnungKurz = { "S", "V", "", "", "" }; //68, 69, 70, 71, 72

    /**gattung=1 bis 5. 0 wird auf 1 umgesetzt.*/
    public String getGattungBezeichnungKurz(int gattung) {
        if (gattung == 0) {
            gattung = 1;
        }
        return gattungBezeichnungKurz[gattung - 1];
    }

    /*+++++++++++++++++Grundkapital+++++++++++++++++++++++++++++*/
    /**[gattung-1]*/
    public long[] grundkapitalStueck = { 0, 0, 0, 0, 0 }; //600, 601, 602, 603, 604

    /**gattung=1 bis 5. 0 wird auf 1 umgesetzt.*/
    public long getGrundkapitalStueck(int gattung) {
        if (gattung == 0) {
            gattung = 1;
        }
        return grundkapitalStueck[gattung - 1];
    }

    public long grundkapitalStueckGesamt() {
        long summe = 0;
        for (int i = 0; i < 5; i++) {
            summe += grundkapitalStueck[i];
        }
        return summe;
    }

    /**gattung=1 bis 5. 0 wird auf 1 umgesetzt.*/
    public String getGrundkapitalStueckString(int gattung) {
        if (gattung == 0) {
            gattung = 1;
        }
        return CaString.toStringDE(grundkapitalStueck[gattung - 1]);
    }

    /**gattung=1 bis 5. 0 wird auf 1 umgesetzt.*/
    public double getGrundkapitalEuro(int gattung) {
        if (gattung == 0) {
            gattung = 1;
        }
        return getKapitalWertFuerAktienzahl(grundkapitalStueck[gattung - 1], gattung);
    }

    /**gattung=1 bis 5. 0 wird auf 1 umgesetzt.*/
    public String getGrundkapitalStueckEuroString(int gattung) {
        if (gattung == 0) {
            gattung = 1;
        }
        return getKapitalWertFuerAktienzahlString(grundkapitalStueck[gattung - 1], gattung);
    }

    /*+++++++++++++++++Grundkapital Vermindert+++++++++++++++++++++++++++++*/

    /**[gattung-1]*/
    public long[] grundkapitalVermindertStueck = { 0, 0, 0, 0, 0 }; //605, 606, 607, 608, 609

    /**gattung=1 bis 5. 0 wird auf 1 umgesetzt.*/
    public long getGrundkapitalVermindertStueck(int gattung) {
        if (gattung == 0) {
            gattung = 1;
        }
        return grundkapitalVermindertStueck[gattung - 1];
    }

    public long grundkapitalVermindertStueckGesamt() {
        long summe = 0;
        for (int i = 0; i < 5; i++) {
            summe += grundkapitalVermindertStueck[i];
        }
        return summe;
    }

    /**gattung=1 bis 5. 0 wird auf 1 umgesetzt.*/
    public String getGrundkapitalVermindertStueckString(int gattung) {
        if (gattung == 0) {
            gattung = 1;
        }
        return CaString.toStringDE(grundkapitalVermindertStueck[gattung - 1]);
    }

    /**gattung=1 bis 5. 0 wird auf 1 umgesetzt.*/
    public double getGrundkapitalVermindertEuro(int gattung) {
        if (gattung == 0) {
            gattung = 1;
        }
        return getKapitalWertFuerAktienzahl(grundkapitalVermindertStueck[gattung - 1], gattung);
    }

    /**gattung=1 bis 5. 0 wird auf 1 umgesetzt.*/
    public String getGrundkapitalVermindertEuroString(int gattung) {
        if (gattung == 0) {
            gattung = 1;
        }
        return getKapitalWertFuerAktienzahlString(grundkapitalVermindertStueck[gattung - 1], gattung);
    }

    /*+++++++++++++++++Grundkapital Eigene Aktien+++++++++++++++++++++++++++++*/

    /**[gattung-1]*/
    public long[] grundkapitalEigeneAktienStueck = { 0, 0, 0, 0, 0 }; //630, 631, 632, 633, 634

    /**gattung=1 bis 5. 0 wird auf 1 umgesetzt.*/
    public long getGrundkapitalEigeneAktienStueck(int gattung) {
        if (gattung == 0) {
            gattung = 1;
        }
        return grundkapitalEigeneAktienStueck[gattung - 1];
    }

    public long grundkapitalEigeneAktienStueckGesamt() {
        long summe = 0;
        for (int i = 0; i < 5; i++) {
            summe += grundkapitalEigeneAktienStueck[i];
        }
        return summe;
    }

    /**gattung=1 bis 5. 0 wird auf 1 umgesetzt.*/
    public String getGrundkapitalEigeneAktienStueckString(int gattung) {
        if (gattung == 0) {
            gattung = 1;
        }
        return CaString.toStringDE(grundkapitalEigeneAktienStueck[gattung - 1]);
    }

    /**gattung=1 bis 5. 0 wird auf 1 umgesetzt.*/
    public double getGrundkapitalEigeneAktienEuro(int gattung) {
        if (gattung == 0) {
            gattung = 1;
        }
        return getKapitalWertFuerAktienzahl(grundkapitalEigeneAktienStueck[gattung - 1], gattung);
    }

    /**gattung=1 bis 5. 0 wird auf 1 umgesetzt.*/
    public String getGrundkapitalEigeneAktienEuroString(int gattung) {
        if (gattung == 0) {
            gattung = 1;
        }
        return getKapitalWertFuerAktienzahlString(grundkapitalEigeneAktienStueck[gattung - 1], gattung);
    }

    /*++++++++++++++++Weitere Kapital-Parameter++++++++++++++++++*/
    
    /**[gattung-1]*/
    public double[] wertEinerAktie = { 0.0, 0.0, 0.0, 0.0, 0.0 }; //610, 611, 612, 613, 614

    /**gattung=1 bis 5. 0 wird auf 1 umgesetzt.*/
    public double getWertEinerAktie(int gattung) {
        if (gattung == 0) {
            gattung = 1;
        }
        return wertEinerAktie[gattung - 1];
    }

    /**gattung=1 bis 5. 0 wird auf 1 umgesetzt.*/
    public String getWertEinerAktieString(int gattung) {
        if (gattung == 0) {
            gattung = 1;
        }
        return CaString.toEuroStringDE(wertEinerAktie[gattung - 1], getAnzahlNachkommastellenKapital(gattung));
    }

    /**[gattung-1]*/
    public int[] anzahlNachkommastellenKapital = { 2, 2, 2, 2, 2 }; //615, 616, 617, 618, 619

    /**gattung=1 bis 5. 0 wird auf 1 umgesetzt.*/
    public int getAnzahlNachkommastellenKapital(int gattung) {
        if (gattung == 0) {
            gattung = 1;
        }
        return anzahlNachkommastellenKapital[gattung - 1];
    }

    /**Liefert Kapital-Wert einer Aktienzahl für Gattung (1 bis 5)*/
    public double getKapitalWertFuerAktienzahl(long pAktienzahl, int pGattung) {
        if (pGattung == 0) {
            pGattung = 1;
        }
        return ((pAktienzahl) * getWertEinerAktie(pGattung));
    }

    /**Liefert Kapital-Wert einer Aktienzahl für Gattung (1 bis 5) als String*/
    public String getKapitalWertFuerAktienzahlString(long pAktienzahl, int pGattung) {
        if (pGattung == 0) {
            pGattung = 1;
        }
        return (CaString.toEuroStringDE((pAktienzahl) * getWertEinerAktie(pGattung),
                getAnzahlNachkommastellenKapital(pGattung)));
    }

    /**Parameter - je Gattung - ob Eintrittskartennummer (bei Stornierung und Neuaustellung) möglichst wiederverwendet
     * werden soll, oder zwingend neu zu vergeben ist.
     * [gattung-1]*/
    public boolean[] eintrittskarteNeuVergeben = { false, false, false, false, false }; //620, 621, 622, 623, 624

    public boolean liefereEintrittskarteNeuVergeben(int gattung) {
        if (gattung == 0) {
            gattung = 1;
        }
        return eintrittskarteNeuVergeben[gattung - 1];
    }

    /**Parameter - je Gattung - ob für diese Gattung eine Zugangsbuchung zulässig ist
     * [gattung-1]*/
    /*XXX*/
    public boolean[] zugangMoeglich = { true, true, true, true, true }; //625 bis 629 tfZugangMoeglich

    public boolean liefereZugangMoeglich(int gattung) {
        if (gattung == 0) {
            gattung = 1;
        }
        return zugangMoeglich[gattung - 1];
    }

    
    /*********************Verknüpfter Mandant - z.B. für ku178 Beiratswahl***************/
    public int zweiterMandantNr = 0; //74
    public int zweiterMandantHVJahr = 0; //75
    public String zweiterMandantHVNummer = ""; //76
    public String zweiterMandantDatenbereich = ""; //77

    
    /****************Höchststimmrecht - Hack****************************************/
    /**Höchststimmrecht ist nur als "Hack" in bestimmten Konfigurationen verwendbar
     * 
     * Beschreibung des Verfahrens:
     * 
     * Einschränkungen:
     * ----------------
     * > Aktionäre mit Höchststimmrecht und mehrere Gattungen funktioniert aktuell nicht bei
     * der Summenkorrektur im Teilnehmerverzeichnis (die Reduktion aus stimmenBeiPreasenzReduzierenUmStimmen
     * erfolgt bei JEDER Gattung)
     * 
     * > Aktionäre mit Höchststimmrecht in Briefwahl funktionert aktuell nicht bei der Summenkorrektur
     * im Teilnehmerverzeichnis (Reduktionswert gibt es nicht für Briefwahlsummen)
     * 
     * > Aktionäre mit Höchststimmrecht dürfen nicht in Sammelkarten enthalten sein.
     * => Falls Aktionäre mit Höchststimmrecht SRV abgeben, geht das nur bei "mit Offenlegung".
     * => Briefwahl geht für diese Aktionäre nicht
     * => Soll es trotzdem mit Briefwahl gehen, bzw. unbedingt in SRV-Sammelkarte rein, dann
     * separate Sammelkarte anlegen, und bei dieser die Kopfweisungen (ja, mit dann abweichender und
     * händisch ausgerechneter Summe) eingegeben werden (Achtung, bei Verwendung von Briefwahl: hier stehen derzeit
     * die reduzierten Summenwerte in der Zusammenstellung fürs Teilnehmerverzeichnis nicht zur Verfügung!).
     * => Sammelkarte, ohne Weisungen, für die geworfen wird, darf keinen Aktionär mit Höchststimmrecht
     * enthalten.
     * 
     * 
     * Parameter: 
     * ----------
     * Die Parameter stehen nur unter den neuen Preset-Parametern zur Verfügung, derzeit abrufbar unter
     * HVMaster -> Konfiguration -> Preset-Parameter ->neue Parameter. Dort "Analyse" auswählen,
     * dann sind die beiden Parameter sichtbar (nicht für Allgemeinheit bestimmt ...)
     * 
     * 
     * Kennzeichnung der Aktionäre mit Höchststimmrecht
     * ------------------------------------------------
     * in eclMeldung.zusatzfeld3: wenn hier eine Zahl eingetragen wird, dann ist das die zulässige
     * (d.h. gekürzte) Stimmenzahl (manueller Eintrag direkt in tbl_meldung)
     * 
     * Summe Teilnehmerverzeichnis
     * ---------------------------
     * Folgende Variablen stehen auch mit der Stimmenreduzierung 
     * (d.h. Wert aus Liste, abzgl. stimmenBeiPraesenzReduzierenUmStimmen)
     * zur Verfügung:
     * Für alle Gattungen (aber jeweils um die selbe Zahl reduziert!)
     * SummeVorherHS.Stimmen<G>
     * SummeVorherHS.StimmenDE<G>
     * 
     * SummeAktuellHS.Stimmen
     * SummeAktuellHS.StimmenDE
     * SummeAktuellHS.StimmenUndBriefwahl
     * SummeAktuellHS.StimmenUndBriefwahlDE
     * 
     * Abstimmung
     * ----------
     * Wenn Parameter eingestellt, dann werden die in eclMeldung.zusatzfeld eingetragenen
     * Stimmen vorrangig automatisch verwendet.
     * */
    public int hoechststimmrechtHackAktiv=0; //2664
    public int stimmenBeiPraesenzReduzierenUmStimmen=0; //2665
    
    /****************Sonstige Basis-Parameter*********************************/

    public int laengeAktionaersnummer = 10; //73

    /**Wirksam bei Namensaktien: die Externe Darstellung wird dann ohne führende "0" aufbereitet.
     * Achtung - nur zulässig für "Vereins-Aktionärsnummern" - intern mit führender 0,
     * extern ohne führende 0
     */
    public int ohneNullAktionaersnummer = 0; //86 tfOhneNullAktionaersnummer

    public String eindeutigeHVKennung = ""; //87 tfEindeutigeHVKennung

    /**[gattung-1]*/
    public String[] isin = {"", "", "", "", ""}; //88, 89, 90, 91, 92 tfIsin1 bis tfIsin5

    /**gattung=1 bis 5. 0 wird auf 1 umgesetzt.*/
    public String getIsin(int gattung) {
        if (gattung == 0) {
            gattung = 1;
        }
        return isin[gattung - 1];
    }

    public String mailConsultant=""; //78Lang tfMailConsultant
    
    
    /**Nicht in Oberfläche. Nur zu Testzwecken. Aktuell wird Close verzögert*/
    public int pLocalVerzoegerungOpen = 0; //19 Local

    /**0=undefiniert; 1= eine Seite; 2=zwei Seiten*/
    public int ekSeitenzahl = 0; //79 tfEkSeitenzahl
    
    /**0 => es wird für Mail, Druck, Selbstdruck immer das selbe Formular verwendet
     * 1 => es werden getrennte Formulare verwendet
     */
    public int ekFormularGetrenntJeVersandweg=1; // tfEkFormularGetrenntJeVersandweg 98
    
    /**Drucker-Nummern, die für die einzelnen Autodruck-Funktionen verwendet werden sollen.
     * Array der Länge 101, von 1 bis 100.
     * 
     * 1 = Ausdruck Formular beim Erstzugang (analog zu Label)
     */
    public int[] autoDruckerVerwendung=new int[101]; //3001 bis 3100
    
    /**LEN=40*/
    /*XXX*/
    public String profileKlasse=""; //tfProfileKlasse 2586
    
}
