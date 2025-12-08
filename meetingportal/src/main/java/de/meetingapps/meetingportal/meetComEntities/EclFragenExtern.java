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

public class EclFragenExtern {

    /** Mandantennummer */
    public int mandant = 0;

    /**Eindeutig Ident dieser Frage*/
    public int frageIdent = 0;

 
    /******Mitteilungssteller***************/
    
    /**Kennung des Mitteilungssteller
     * LEN=20
     */
    public String identString="";
    
    /**Mitteilungssteller (aus Kennung)
     * LEN=200
     * */
    public String nameVornameOrtKennung="";
    
    
    /**Mitteilungssteller (manuelle Eingabe).
     * Falls eine manuelle Eingabe des Namens für diese Mitteilung erfolgt ist, dann
     * diese Eingabe. Sonst identisch mit nameVornameOrtKennung.
     * LEN=200.
     * 
     * Bei Wortmeldungen muß dieses Feld wg. Rednerliste bei der Pflege der Rednerliste zwingend gefüllt werden.
     * */
    public String nameVornameOrt="";

    /** LEN=100*/
    public String kontaktDaten="";
    
    /**Anzahl der Aktien, die der Mitteilungssteller zum Zeitpunkt der Mitteilung vertritt*/
    public long anzahlAktienZumZeitpunktderMitteilung=0L;

    /**********Mitteilungsartinhalt********************/
    
    /**Nur bei entsprechender Parameterstellung gefüllt*/
    public int[] mitteilungZuTop = new int[200];

    /**LEN=100
     */
    public String mitteilungKurztext = "";

    /**LEN in Datenbank=21.000;
     * Zulässige Länge in Browser = 10.000
     */
    public String mitteilungLangtext = "";

    /**LEN=19*/
    public String zeitpunktDerMitteilung = "";
    
    /**Falls Mitteilung zurückgezogen wurde
     * LEN=19*/
    public String zeitpunktDesRueckzugs = "";

    public EclFragenExtern() {

    }

    public EclFragenExtern(EclMitteilung pFrage, int pMandant, int pFragenHinweisGelesen) {
        mandant = pMandant;
        frageIdent = pFrage.mitteilungIdent;
        identString = pFrage.identString;
        nameVornameOrtKennung = pFrage.nameVornameOrtKennung;
        nameVornameOrt = pFrage.nameVornameOrt;
        kontaktDaten = pFrage.kontaktDaten;
        anzahlAktienZumZeitpunktderMitteilung = pFrage.anzahlAktienZumZeitpunktderMitteilung;
        mitteilungZuTop = pFrage.mitteilungZuTop;
        mitteilungKurztext = pFrage.mitteilungKurztext;
        mitteilungLangtext = pFrage.mitteilungLangtext;
        zeitpunktDerMitteilung = pFrage.zeitpunktDerMitteilung;
        zeitpunktDesRueckzugs = pFrage.zeitpunktDesRueckzugs;
        if (pFragenHinweisGelesen>0) {
            String ergaenzung="";
            if (pFrage.hinweisWurdeBestaetigt==1) {
                ergaenzung="Namensnennung Ja";
            }
            else {
                ergaenzung="Namensnennung Nein";
            }
            mitteilungLangtext+=" "+ergaenzung;
        }
    }
}
