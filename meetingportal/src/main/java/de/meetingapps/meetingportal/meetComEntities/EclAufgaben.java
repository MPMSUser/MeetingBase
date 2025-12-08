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
package de.meetingapps.meetingportal.meetComEntities;

/**Mandantenübergreifende Liste für anstehende ToDos
 * 
 * Wird sukzessive ersetzt durch EclAuftrag - nicht mehr verwenden! Abbildung ist bereits in den Kommentaren eingetragen*/
public class EclAufgaben {

    /**Mandant, für den die Aufgabe angefordert wurden*/
    public int mandant = 0; //mandant,  hvJahr, hvNummer, datenbereich

    /** eindeutiger Key für die Aufgabe*/
    public int identAufgabe = 0; //ident

    public long db_version = 0; //db_version

    /**Aufgaben-Art (aus KonstAufgaben)*/
    public int aufgabe = 0; //gehoertZuModul, auftragsArt

    /**Text, in dem z.B. die Hotline ein Anliegen eines Aktionärs beschreiben kann.
     * Zwingend wenn aufgabe=sonstige
     * LEN=1000*/
    public String freitextBeschreibung = "";  //freitextBeschreibung

    /**Zeitpunkt der Aufgabenerteilung, JJJJ-MM-TT HH:MM:SS
     * LEN=19*/
    public String zeitpunktErteilt = ""; //zeitStart

    /**Siehe KonstAufgabenAnforderer*/
    public int anforderer = 0; //eingangsweg
    /**LEN=1000*/
    public String angefordertVonBemerkung = ""; //userIdAuftraggeber, freitextAuftraggeber

    /**Usernummer, der die Aufgabe erfasst hat. <0 oder > 9900 => technische User*/
    public int userNummerErfassung = 0; //userIdErfasser

    /**Status der Aufgabe siehe KonstAufgabenStatus*/
    public int status = 0; //status, statusAuftragsArt

    /**[0] bis [9]
     * der Aufgabe übergebene Argumente - Bedeutung abhängig von der Aufgaben-Art, 
     * Beschreibung in KonstAufgaben bei der jeweiligen Aufgabe
     * LEN=je 200*/
    public String[] argument = null; //parameterTextLang

    /**Siehe KonsAufgabenErledigt*/
    public int erledigtVermerk = 0; //statusInfoDetail

    /**Len=1000*/
    public String erledigtBemerkung = ""; //statusInfoFreitext

    /**JJJJ-MM-TT HH:MM:SS, LEN=19*/
    public String zeitpunktErledigt = ""; //zeitErledigt

    /**Usernummer, der die Aufgabe erledigt hat. <0 oder > 9900 => technische User*/
    public int userNummerVerarbeitet = 0; //istIdVerarbeitet

    /**Drucklaufnummer*/
    public int drucklaufNr = 0; //drucklaufNr

    public EclAufgaben() {
        argument = new String[10];
        for (int i = 0; i < 10; i++) {
            argument[i] = "";
        }
    }

}
