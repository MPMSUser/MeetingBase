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
package de.meetingapps.meetingportal.meetComKonst;


/**
 * Funktionsabhaängigkeiten von Gruppen
 */
public class KonstGruppen {

    public final static int nichtbelegt=-1;
    public final static int unbekannt = 0;

    public final static int einzelmitglied = 1;
    public final static int eheleuteGbR = 2; //Verifiziert
    public final static int minderjaehrigesEinzelmitglied = 3; //verifiziert
    public final static int eheleuteGesamthans = 4; //verifiziert
    public final static int erbengemeinschaft = 5;//früher: 6
    public final static int firmen = 6;//früher: 5
    public final static int betreuungsfall=7;

    
    
    
    
    /**iGeneralversammlungAnAbmelden
     * Text für Select-Button Eine Personen
     * 
     * Ursprüngliche Belegung der Nummern (ku178 2022):
     * 600 = eine Person, Anmeldung ohne gesetzlichen Vertreter möglich
     * 711 = zwei Personen und Anmeldung ohne gesetzlichen Vertreter möglich
     * 1002= nur eine Person zulässig oder zwei Personen möglich; Anmeldung nur mit gesetzlichem Vertreter möglich
     */
    static public int liefereGruppenTextEinePerson(int pGruppenNr) {
        switch (pGruppenNr) {
        case -1:
        case 0: return 600;
        case 1: return 600;
        case 2: return 1002;
        case 7:
        case 3: return 2055;
        case 4: return 1002;
        case 5: return 1002; //Fest verwendet für Erben
        case 6: return 1002; //Früher: 711
        //Text 711 stünde noch zur Verfügung
        }

        return 600;
    }

    /**iGeneralversammlungAnAbmelden
     * Text für Select-Button zwei Personen*/
    static public int liefereGruppenTextZweiPersonen(int pGruppenNr) {
        switch (pGruppenNr) {
        case -1:
        case 0: return 2031;
        case 1: return 2031;
        case 2: return 2050;
        case 7:
        case 3: return 2031;
        case 4: return 2031;
        case 5: return 2031;
        case 6: return 2031;
        }

        return 2031;
    }

    /**iGeneralversammlungAnAbmelden
     * Text für Select-Button Abmelden. Standard = 601.
     * Abweichend für Einzelmitglied ("Ich")
     * Auswahl "2"
     */
    static public int liefereGruppenTextAbmelden(int pGruppenNr) {
        switch (pGruppenNr) {
        case -1:
        case 0: return 601;
        case 1: return 2058;
        case 2: return 601;
        case 7:
        case 3: return 601;
        case 4: return 601;
        case 5: return 2064;
        case 6: return 601;
        }

        return 601;
    }
   
    /**iGeneralversammlungAnAbmelden
     * Text für Select-Button für Vertreter. Standard=988.
     * Abweichend für Einzelmitglied ("Ich")
     * Auswahl "4"
     */
    static public int liefereGruppenTextVertreter(int pGruppenNr) {
        switch (pGruppenNr) {
        case -1:
        case 0: return 988;
        case 1: return 2059;
        case 2: return 988;
        case 7:
        case 3: return 988;
        case 4: return 988;
        case 5: return 2065;
        case 6: return 988;
        }

        return 601;
    }

    /**iGeneralversammlungAnAbmelden
     * Text vor Vertreter 1 - nur gesetzliche Vertretung mit 1 Vertreter
     * Standard bisher 2035, */
    static public int liefereGruppenTextVertreter1nur1(int pGruppenNr) {
        switch (pGruppenNr) {
        case -1:
        case 0: return 2035;
        case 1: return 2035;
        case 2: return 2035;
        case 7:
        case 3: return 2035; //Fest gepflegt
        case 4: return 2035;
        case 5: return 2066;
        case 6: return 2035;
        }

        return 2038;
    }
    
    /**iGeneralversammlungAnAbmelden
     * Text vor Vertreter 1 - nur gesetzliche Vertretung mit 2 Personen
     * Standard bisher 2038, */
    static public int liefereGruppenTextVertreter1(int pGruppenNr) {
        switch (pGruppenNr) {
        case -1:
        case 0: return 2038;
        case 1: return 2038;
        case 2: return 2051;
        case 7:
        case 3: return 2038; //Fest gepflegt
        case 4: return 2038;
        case 5: return 2060;
        case 6: return 2038;
        }

        return 2038;
    }

    /**iGeneralversammlungAnAbmelden
     * Text vor Vertreter 2*/
    static public int liefereGruppenTextVertreter2(int pGruppenNr) {
        switch (pGruppenNr) {
        case -1:
        case 0: return 2041;
        case 1: return 2041;
        case 2: return 2052;
        case 7:
        case 3: return 2041;
        case 4: return 2041;
        case 5: return 2041;
        case 6: return 2041;
        }

        return 2041;
    }

    /**iGeneralversammlungAnAbmelden
     * Bei An-/Abmeldung, nach Radiobuttons; bei 602*/
    static public int liefereGruppenTextStartAnAbmeldung(int pGruppenNr) {
        switch (pGruppenNr) {
        case -1:
        case 0: return 1983;
        case 1: return 1984;
        case 2: return 1984;
        case 7:
        case 3: return 1984;
        case 4: return 1984;
        case 5: return 1985;
        case 6: return 1986;
        }

        return 1983;
    }
    
    /**iAuswahl1Generalversammlung
     * Nach erfolgter Anmeldung, wenn Vertreter angemeldet, aber noch keiner vorliegt. Bei 1004.
     * Wird angzeig,t falls noch keine geltende Vollmacht eingetragen ist.*/
    static public int liefereGruppenTextAnmeldungMitVertreter(int pGruppenNr) {
        switch (pGruppenNr) {
        case -1:
        case 0: return 1987;
        case 1: return 1988;
        case 2: return 1988;
        case 7:
        case 3: return 1988;
        case 4:return 1988;
        case 5: return 1989;
        case 6: return 1990;
        }

        return 1987;
    }
    
    /**iAuswahl1Generalversammlung
     * Nach erfolgter Abmeldung, vor dem Button zur An-/Abmeldung, bei 707*/
    static public int liefereGruppenTextAbmeldungBestaetigung(int pGruppenNr) {
        switch (pGruppenNr) {
        case -1:
        case 0: return 1991;
        case 1: return 1992;
        case 2: return 1992;
        case 7:
        case 3: return 1992;
        case 4: return 1992;
        case 5: return 1993;
        case 6: return 1994;
        }

        return 1991;
    }
 
    /**iAuswahl1Generalversammlung
     *Nach erfolgter Anmeldung, vor Vollmachtsformularen, bei 1006
     * Wird angezeigt, wenn noch keine gesetzliche Vollmacht hinterlegt ist*/
    static public int liefereGruppenTextVorVollmachtsformular(int pGruppenNr) {
        switch (pGruppenNr) {
        case -1:
        case 0: return 1995;
        case 1: return 1996;
        case 2: return 1996;
        case 7:
        case 3: return 1996;
        case 4: return 1996;
        case 5: return 1997;
        case 6: return 1998;
        }

        return 1995;
    }

    /**iWeisung*/
    static public int liefereGruppenTextWeisung(int pGruppenNr) {
        switch (pGruppenNr) {
        case -1:
        case 0: return 1999;
        case 1: return 1999;
        case 2: return 1999;
        case 7:
        case 3: return 2000;
        case 4: return 2001;
        case 5: return 2002;
        case 6: return 2003;
        }

        return 1999;
    }

    /**iWeisungAendern*/
    static public int liefereGruppenTextWeisungAendern(int pGruppenNr) {
        switch (pGruppenNr) {
        case -1:
        case 0: return 2004;
        case 1: return 2004;
        case 2: return 2004;
        case 7:
        case 3: return 2005;
        case 4: return 2006;
        case 5: return 2007;
        case 6: return 2008;
        }

        return 2005;
    }

    /**iWeisungQuittung*/
    static public int liefereGruppenTextWeisungQuittung(int pGruppenNr) {
        switch (pGruppenNr) {
        case -1:
        case 0: return 2009;
        case 1: return 2009;
        case 2: return 2009;
        case 7:
        case 3: return 2010;
        case 4: return 2011;
        case 5: return 2012;
        case 6: return 2013;
        }

        return 2009;
    }

    /**iAuswahl1Generalversammlung, iAuswahl1GeneralversammlungBriefwahl*/
    static public boolean liefereGruppenTextVollmachtsButtonAnzeigen(int pGruppenNr) {
        switch (pGruppenNr) {
        case -1:
        case 0: return false;
        case 1: return true;
        case 2: return true;
        case 7:
        case 3: return true;
        case 4: return true;
        case 5: return true;
        case 6: return true;
        }

        return false;
    }

    
    /**iAuswahl1Generalversammlung, iAuswahl1GeneralversammlungBriefwahl*/
    static public int liefereGruppenTextVollmachtsButton(int pGruppenNr) {
        switch (pGruppenNr) {
        case -1:
        case 0: return 2014;
        case 1: return 2014;
        case 2: return 2014;
        case 7:
        case 3: return 2015;
        case 4: return 2016;
        case 5: return 2017;
        case 6: return 2018;
        }

        return 2014;
    }

   /**iGeneralversammlung
     * Button Muster gesetzl. Formular wird angezeigt
     */
    static public boolean vollmachtsFormularGesetzlichZulaessig(int nr) {
        switch (nr) {
        case 0: return false;
        case 1: return false;
        case 2: return false;
        case 3: return true;
        case 4: return false;
        case 5: return false;
        case 6: return true;
        case 7: return false;
        }
        return false;
    }

    /**iGeneralversammlung
     * Button Eintrittskartenticket
     */
    static public boolean eintrittskarteFuerGeneralversammlungZulaessig(int nr) {
        switch (nr) {
        case 0: return false;
        case 1: return true;
        case 2: return true;
        case 3: return false;
        case 4: return false;
        case 5: return false;
        case 6: return false;
        case 7: return false;
        }
        return false;
    }

    /**iAuswahl1Generalversammlung, iAuswahl1GeneralversammlungBriefwahl*/
    static public int liefereGruppenTextVollmachtsFormularNr(int pGruppenNr) {
        switch (pGruppenNr) {
        case -1:
        case 0: return 1;
        case 1: return 1;
        case 2: return 1;
        case 3: return 2;
        case 4: return 3;
        case 5: return 4;
        case 6: return 5;
        case 7: return 6;
        }

        return 1;
    }

    /**iAuswahl1Generalversammlung, iAuswahl1GeneralversammlungBriefwahl
     * Alt: alles 11 außer 3: 12*/
    static public int liefereGruppenTextVollmachtsFormularFuerVertreterNr(int pGruppenNr) {
        switch (pGruppenNr) {
        case -1:
        case 0: return 13; //
        case 1: return 13;
        case 2: return 11;
        case 3: return 13; //
        case 4: return 11;
        case 5: return 11; //
        case 6: return 12; //
        case 7: return 13; //
        }

        return 11;
    }

    /**iAuswahl1Generalversammlung, iAuswahl1GeneralversammlungBriefwahl*/
    static public int liefereGruppenTextVollmachtsFormularFuerVertreterButton(int pGruppenNr) {
        switch (pGruppenNr) {
        case -1:
        case 0: return 964;
        case 1: return 964;
        case 2: return 964;
        case 7:
        case 3: return 2021;
        case 4: return 964;
        case 5: return 964;
        case 6: return 964;
        }

        return 964;
    }

    
   /**iGeneralversammlung
     * Text für Teilnehmer mit gesetzlich erforderlicher Vollmacht,
     * wenn keine Vollmacht hinterlegt.
     * Standard bisher =1008
     * Abweichend für jede einzelne zutreffende Personenart
    /**iAuswahl1Generalversammlung, iAuswahl1GeneralversammlungBriefwahl*/
    static public int liefereGruppenTextGesetzlVollmachtFehlt(int pGruppenNr) {
        switch (pGruppenNr) {
        case -1:
        case 0: return 1008;
        case 1: return 1008;
        case 2: return 1008;
        case 7:
        case 3: return 2061;
        case 4: return 1008;
        case 5: return 2062;
        case 6: return 2063;
        }

        return 964;
    }

    static public String getText(int nr) {
        switch (nr) {
        case 0: return "unbekannt";
        case 1: return "Einzelmitglied";
        case 2: return "Eheleute-GbR";
        case 3: return "Minderjähriges Einzelmitglied";
        case 4: return "Eheleute-Gesamthansgemeinschaft";
        case 5: return "Erbengemeinschaft";   //früher: 6
        case 6: return "Firmen und Sonstige"; //früher: 5
        case 7: return "Betreuung";
        }

        return "";

    }

    static public boolean anmeldenOhneGesetzlichenVertreterZulaessig(int nr) {
        switch (nr) {
        case 0: return false;
        case 1: return true;
        case 2: return true; /*ku178: True*/
        case 3: return false;
        case 4: return false;
        case 5: return false;
        case 6: return false;
        case 7: return false;
        }
        return false;
    }

    /**Hinweis: zwei Personen sind derzeit nur für Minderjährige implementiert - hängt mit der Routine beim Anmelden und Vertreterwechsel zusammen;
     * und auch im Hinblick auf Zwangsweise Vertretung am Bildschirm usw. usf.*/
    static public boolean anmeldenZweiPersonenZulaessig(int nr, int parameterMinderjaehrige) {
        switch (nr) {
        case 0: return false;
        case 1: return false;
        case 2: return true; /*ku178: false*/
        case 3:if (parameterMinderjaehrige>0) {return false;} 
            return false;
        case 4: return false;
        case 5: return false;
        case 6: return false;
        case 7: return false;
        }
        return false;
    }

    /**Hinweis: zwei Personen sind derzeit nur für Minderjährige implementiert - hängt mit der Routine beim Anmelden und Vertreterwechsel zusammen;
     * und auch im Hinblick auf Zwangsweise Vertretung am Bildschirm usw. usf.*/
    static public boolean anmeldenGastkarteFuerZweitePersonZulaessig(int nr) {
        switch (nr) {
        case 0: return false;
        case 1: return false;
        case 2: return false;
        case 3: return true;
        case 4: return false;
        case 5: return true;
        case 6: return false;
        case 7: return false;
        }
        return false;
    }

    static public boolean anmeldenGastkarteFuerMitgliedZulaessig(int nr) {
        switch (nr) {
        case 0: return false;
        case 1: return false;
        case 2: return false;
        case 3: return true;
        case 4: return false;
        case 5: return false;
        case 6: return false;
        case 7: return true;
        }
        return false;
    }

}
