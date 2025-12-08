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

import java.util.ArrayList;
import java.util.List;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComAllg.CaString;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComHVParam.ClGlobalVar;
import de.meetingapps.meetingportal.meetComHVParam.ParamNummernformen;
import de.meetingapps.meetingportal.meetComHVParam.ParamNummernkreise;
import de.meetingapps.meetingportal.meetComHVParam.ParamPruefzahlen;
import de.meetingapps.meetingportal.meetComHVParam.ParamSpezial;
import de.meetingapps.meetingportal.meetComKonst.KonstCodeart;
import de.meetingapps.meetingportal.meetComKonst.KonstKartenart;
import de.meetingapps.meetingportal.meetComKonst.KonstKartenklasse;

public class BlNummernformen {

    private int logDruckenInt=10;

    /* 
     * 
     * Appident is derzeit:
     * 51			Aktionsnummer=5; Kartenart =1 (derzeit fest)
     * mmm			Mandantennummer (3-stellig)
     * ;
     * Für alle enthaltenen Elemente:
     * 	Falls nicht am normalen Schalter vertretbar (0-Bestand, nicht angemeldet, EK delegiert, etc.):
     * 		9
     * 		Aktionärsnummer
     * 		;
     * 	EK, die direkt vertreten werden kann (Gast)
     * 		2
     * 		Gattung (wird ignoriert)
     * 		ZutrittsIdent
     * 		ZutrittsIdentNeben
     * 		;
     * 	EK, die direkt vertreten werden kann (Aktionär)
     * 		1
     * 		Gattung (wird ignoriert)
     * 		ZutrittsIdent
     * 		ZutrittsIdentNeben
     * 		;
     * 	
     * 600000;
     * 00000;
     * mmm
     * 0009432ZU
     * 
     * So wars mal geplant :-):
     * 
     * AppIdent ist mindestens:
     * 	2 Aktionsnummer+4 Ident, +4 Prüfziffern etc. d.h. immer Länger als 10,
     * 
     * ++++Aufbau der AppIdent++++
     * 1. Zeichen Kartenklasse - als Identifizierung, dass es eine AppIdent ist
     * 2. Zeichen = Kartenart - um die generelle Bewegung (Zugang oder so) zu identifizieren
     * 	Zulässig (derzeit): 
     * 		1 = erstzugang (auch Wiederzugang) 
     * 		4 = abgang
     * 		7 = stimmabschnittsnummer (für Abstimmung)
     * 	Rest ist in Vorbereitung (also z.B. Vollmacht an Dritte etc.)
     * ;
     * 10 Zeichen AppIdent (Version der App etc.) (derzeit: 0000100001)
     * ;
     * falls kartenart=Stimmabschnittsnummer, dann 
     * > 3 Zeichen "Anzahl Zeichen Abstimmung" rcAgendaZeichenzahl
     * > ;
     * > 3 Zeichen "Agenda-Version" rcAgendaVersion
     * > ;
     * 
     * Dann: mehrere ZutrittsIdents in der Form:
     * 1. Zeichen Kartenklasse, nur 1, 2, 3 oder 9 zulässig (zur Unterscheidung, 
     * 				ob Gastzutrittsident oder AktionärszutrittsIdent, oder Aktionärsnummer, oder Abstimmung)
     * 
     * Bei 1,2, 3:
     * 	n zeichen ZutrittsIdent (n=laengeKartennummer[1 oder 2]
     * 	2 Zeichen IdentNeben
     * 	5 Zeichen PersonenIdent (kann 00000 sein, wenn das von der App noch nicht implementiert)
     * Bei 9: Aktionärsnummer (bis zum nächsten Strichpunkt)
     * ;
     * 
     * falls kartenart=Stimmabschnittsnummer, dann
     * > "Anzahl Zeichen Abstimmung" - Abstimmungsverhalten
     * > ;
     * 
     * 	Anmerkung zur Kartenklasse Abstimmung:hier wird immer der Nummernkreis [5]-virtueller Kreis verwendet. Diese Stimmkarten werden bei App-Nutzung
     * 	(und Online-Teilnahme?) virtuell verwendet und automatisch fortlaufend verwendet.
     * 
     * Dann:
     * 1. Zeichen: Kartenklasse 6 (markiert den Abschluß der Zutrittsidents!)
     * PersonenIdent der Länge 5 = rcPersonenIdent (Länge aktuell in App 5fest codiert)
     * ;
     * PersonenIdent der Länge 5 = rcBevollmaechtigter
     * ;
     * 
     * Dann:
     * mandantennummer, dreistellige Kontrollzahl
     * 
     * Noch nicht implementiert: HashCode zur Verschlüsselung
     * 
     * 
     * Längenabschätzung AppIdent:
     * "Grundrauschen":
     * 38 + Länge Hashcode
     * Je zugeordneter EK:
     * 18+anzahl Abstimnmung
     * 
     * Musterrechnung - Abstimmungsblock 200 Zeichen => maximal 10 zugeordnete Eintrittskarten
     * 
     * */

    /*QR-Code Aufbau für Test:
     *5 1 ; 0000100001 ; 004 ; 001 ; 1 00001 00 ; 1 00002 00 ; 2 80001 00 ; 6 00001 ; 00000 ; 054  101
     */

    /**Die komplette Nummer ist eine AppIdent*/
    public boolean rcIstAppIdent = false;

    /** Code der gelesenen Nummer (AppIdent, Http-Code, ....) - siehe KonstCodeart*/
    public int rcCodeart = 0;

    /**Verwendete Nummernform; 0 = keine Nummernform*/
    public int rcNummernformIdent = 0;

    /**Aktionsnummer - aufgeteilt auf 3 Stellen*/
    public int rcKartenklasse = 0; /*"Global", d.g. für die gesamte Nummernform*/
    public int rcKartenart = 0;
    public int rcStimmkarteSubNummernkreis = 1;
    /**=0 => Nebennummer ist in Nummernform nicht vorhanden, wird dann als "00" zurückgeliefert. =1 => Nebennummer ist in Nummernform vorhanden*/
    public int rcEintrittskarteMitNeben = 0;

    /**2-stellige Nummer des Stimmabschnittbogens*/
    public int rcStimmkartennummer = 0;
    /**Ja/Nein/Enthaltung*/
    public int rcStimmart = 0;

    /**1-stellige Ziffer von 0 bis 5, gibt Gattung wieder. 0 = quasi undefiniert*/
    public int rcGattung = 0;

    /**Für AppIdent: PersonenIdent, die die App nutzt (nur gefüllt, falls AppIdent)*/
    public int rcPersonenIdent = 0;

    /**Für AppIdent: Bevollmächtigter - noch näher zu definieren*/
    public int rcBevollmaechtigter = 0;

    /**TODO Im Barcode enthaltener Barcode. Ist nur temporär drin, noch nicht sauber fertiggestelt. Denn: a) beißt sich ";" mit Appident kodierung.
     * Und b) noch nicht sauber wenn mehrere Karten kodiert sind.
     * 
     * Derzeit wird nur einfach alles nach "$" abgeschnitten und in rcPasswort zurückübergeben. (früher: nach ";")
     */
    public String rcPasswort="";
    
    /**mit führende Nullen*/
    public ArrayList<String> rcIdentifikationsnummer = null;

    /**zweistellig. Defualt (d.h. wenn in Code nicht enthalten) ist 00*/
    public ArrayList<String> rcIdentifikationsnummerNeben = null;

    /**rc / Fehlermeldungen zu jeder Identifikationssnummer*/
    public ArrayList<Integer> rcRcZuIdentifikationsnummer = null;

    /**Kartenklasse zu jeder Identifikationssnummer (insbesondere wichtig bei AppIdent!)*/
    public ArrayList<Integer> rcKartenklasseZuIdentifikationsnummer = null;

    /**AgendaVersion (bei AppIdent)*/
    public int rcAgendaVersion = 0;
    /**Agenda-Zeichenzahl in rcAbstimmung*/
    public int rcAgendaZeichenzahl = 0;

    /**Abstimmung zu jeder Identifikationssnummer (bei AppIdent!)*/
    public ArrayList<String> rcAbstimmung = null;

    /**Für AppIdent: je Meldung: die Person, die in der App vom Teilnehmer mit
     * "ja die bin ich" bestätigt wurde. 
     */
    public List<Integer> rcAppVertretendePersonIdent = null;

    /**Für Spezialanwendungen: Information, die in der Nummernform enthalten ist, z.B. zur Anzeige
     * bei Offline-Abstimmung
     */
    public ArrayList<String> rcSonstigeInformation = null;

    /*Wird benötigt, um globale Variablen verarbeiten zu können*/
    private DbBundle lDbBundle = null;
    private ParamNummernformen paramNummernformen = null;
    private ParamPruefzahlen paramPruefzahlen = null;
    private ParamNummernkreise paramNummernkreise = null;
    private ClGlobalVar clGlobalVar = null;

    public BlNummernformen(DbBundle pDbBundle) {
        lDbBundle = pDbBundle;
        if (lDbBundle == null) {
            CaBug.drucke("Blnummernformen.BlNummernformen 001");
            return;
        }
        if (lDbBundle.param == null) {
            CaBug.drucke("Blnummernformen.BlNummernformen 002");
            return;
        }
        if (lDbBundle.param.paramNummernformen == null) {
            CaBug.drucke("Blnummernformen.BlNummernformen 003");
            return;
        }

        paramNummernformen = lDbBundle.param.paramNummernformen;
        paramPruefzahlen = lDbBundle.param.paramPruefzahlen;
        paramNummernkreise = lDbBundle.param.paramNummernkreise;
        clGlobalVar = lDbBundle.clGlobalVar;
    }

    /***********Einfache - Static-Funktionen*****************************/

    /**immer==1 => nebenZutrittsIdent wird immer angehängt, sonst nur wenn !=00*/
    public static String verketteEKMitNeben(String zutrittsIdent, String nebenZutrittsIdent, int immer) {
        String hString = zutrittsIdent;
        if (immer == 1 || nebenZutrittsIdent.compareTo("00") != 0) {
            hString = hString + "-" + nebenZutrittsIdent;
        }

        return hString;
    }

    //	private int dekodiereku036(String nummer, int vorgewaehlteKlasse){
    //		String hString="", hStringIntern="";
    //		int rc;
    //		int hNr=0;
    //		int returnWertGesamt=1;
    //		
    //		int offset=0;
    //		
    //		switch (rcKartenklasse){
    //		case IntKartenklasse.appIdent:
    //		{
    //			int appIdentAktionsklasse=0;
    //			if (nummer.length()<offset+1){return CaFehler.pmNummernformUngueltig;}
    //			hString=nummer.substring(offset, offset+1); offset+=1; /*Kartenklasse innerhalb AppIdent*/
    //			if (!CaString.isNummern(hString)){return CaFehler.pmNummernformAktionsnummerUngueltig;}
    //			appIdentAktionsklasse=Integer.parseInt(hString);
    //			while (appIdentAktionsklasse==IntKartenklasse.eintrittskartennummer || appIdentAktionsklasse==IntKartenklasse.gastkartennummer){
    //				if (nummer.length()<offset+laengeKartennummer[appIdentAktionsklasse]+1 /* plus ; */){return CaFehler.pmNummernformUngueltig;}
    //				hString=nummer.substring(offset,offset+laengeKartennummer[appIdentAktionsklasse]);offset=offset+laengeKartennummer[rcKartenklasse]+1;
    //				
    //				if (!CaString.isNummern(hString)){return CaFehler.pmNummernformUngueltig;}
    //				hNr=Integer.parseInt(hString);
    //				hString=Integer.toString(hNr);
    //				hStringIntern=Integer.toString(hNr);
    //				while (hString.length()<laengeKartennummer[appIdentAktionsklasse]){hString="0"+hString;}
    //				
    //				rcIdentifikationsnummer.add(hString);
    //				rcIdentifikationsnummerIntern.add(hStringIntern);
    //				rcKartenklasseZuIdentifikationsnummer.add(appIdentAktionsklasse);
    //				
    //				if (hNr<vonKartennummer[appIdentAktionsklasse] || hNr>bisKartennummer[appIdentAktionsklasse]){
    //					rcRcZuIdentifikationsnummer.add(CaFehler.pfXyNichtImZulaessigenNummernkreis);
    //					returnWertGesamt=CaFehler.pfXyNichtImZulaessigenNummernkreis;
    //				}
    //				else{
    //					rcRcZuIdentifikationsnummer.add(1);
    //				}
    //
    //				/*Nächste Nummer identifizieren*/
    //				appIdentAktionsklasse=0;
    //				if (nummer.length()<offset+1){return CaFehler.pmNummernformUngueltig;}
    //				hString=nummer.substring(offset, offset+1); offset+=1; /*Kartenklasse innerhalb AppIdent*/
    //				if (!CaString.isNummern(hString)){return CaFehler.pmNummernformAktionsnummerUngueltig;}
    //				appIdentAktionsklasse=Integer.parseInt(hString);
    //			}
    //			if (appIdentAktionsklasse!=IntKartenklasse.personenIdent){
    //				return CaFehler.pmNummernformAktionsnummerUngueltig;
    //			}
    //			if (nummer.length()<offset+laengeKartennummer[appIdentAktionsklasse]+1 /* plus ; */){return CaFehler.pmNummernformUngueltig;}
    //			hString=nummer.substring(offset,offset+laengeKartennummer[appIdentAktionsklasse]);offset=offset+laengeKartennummer[rcKartenklasse]+1;
    //			
    //			if (!CaString.isNummern(hString)){return CaFehler.pmNummernformUngueltig;}
    //			rcPersonNatJurIdent=Integer.parseInt(hString);
    //			break;
    //		}
    //		
    //
    //	}

    /**Formatiert rein nur die Haupt-EK-Nummer (ohne Nebennummer!), ggf.  mit führenden 0*/
    public String formatiereEKNr(String pZutrittsIdent) {
        return formatiereNr(pZutrittsIdent, KonstKartenklasse.eintrittskartennummer);
    }

    /**Formatiert rein nur die Nummer (bei EK: nur Haupt-Nummer, ohne Nebennummer) der pKartenklasse, ggf.  mit führenden 0*/
    public String formatiereNr(String pNummer, int pKartenklasse) {
        String hString = "";
        int hIdent;

        hString = pNummer.trim();

        if (!paramNummernkreise.istNumerisch[pKartenklasse]) {
            /*nummernform ist nicht-numerisch. D.h. keine Formatierung*/
            return hString;
        }

        hIdent = Integer.parseInt(hString);
        hString = Integer.toString(hIdent); /*Enthält nun reine Ziffern ohne Blanks und ohne führende 0*/

        while (hString.length() < paramNummernkreise.laengeKartennummer[pKartenklasse]) {
            hString = "0" + hString;
        }

        return hString;
    }

    public String formatiereEKNrKomplett(String pZutrittsIdent, String pZutrittsIdentNeben) {
        return formatiereNrKomplett(pZutrittsIdent, pZutrittsIdentNeben, KonstKartenklasse.eintrittskartennummerNeben,
                KonstKartenart.erstzugang);
    }

    public String formatiereEKNrKomplettOhneNeben(String pZutrittsIdent) {
        return formatiereNrKomplett(pZutrittsIdent, "00", KonstKartenklasse.eintrittskartennummer,
                KonstKartenart.erstzugang);
    }

    /**Weitere verwendete Felder als Eingabeparameter:
     * rcStimmkarteSubNummernkreis (1 bis 4)
     * rcStimmkartennummer
     * rcStimmart
     * rcGattung
     */
    public String formatiereNrKomplett(String pIdent, String pNeben, int pKartenklasse, int pKartenart) {

        String lZutrittsIdent = "";
        String hString = "";

        String formatierteNr = "";
        int nummernformIdent = 0;

        if (pNeben.isEmpty()) {
            pNeben = "00";
        } /*Für den Fall, dass eine Eintrittskartennummer fälschlicherweise ohne Nebennummer übergeben wurde*/

        nummernformIdent = paramNummernformen.nummernformZuKlasseArt[pKartenklasse][pKartenart];
        /*>>>>>Neu*/
        if (nummernformIdent == -1 && pKartenklasse == KonstKartenklasse.eintrittskartennummerNeben) {
            pKartenklasse = KonstKartenklasse.eintrittskartennummer;
            nummernformIdent = paramNummernformen.nummernformZuKlasseArt[KonstKartenklasse.eintrittskartennummer][pKartenart];
        }
        /*<<<<Neu*/
        if (nummernformIdent == -1) {
            nummernformIdent = paramNummernformen.nummernformZuKlasseArt[pKartenklasse][KonstKartenart.unbekannt];
        }
        if (nummernformIdent == -1) {
            CaBug.drucke("BlNummernformen.formatiereNrKomplett 001");
        }

        //		System.out.println("pKartenklasse="+pKartenklasse);
        //		System.out.println("pKartenart="+pKartenart);
        //		System.out.println("nummernformIdent="+nummernformIdent);

        if (paramNummernformen != null && paramNummernformen.nummernDefinition != null && nummernformIdent > 0
                && paramNummernformen.nummernDefinition.get(nummernformIdent) != null) {
            for (int i = 0; i < paramNummernformen.nummernDefinition.get(nummernformIdent).size(); i++) {
                //			System.out.println("i="+i+" nummernDefinition="+paramNummernformen.nummernDefinition.get(nummernformIdent).get(i));
                switch (paramNummernformen.nummernDefinition.get(nummernformIdent).get(i)) {
                case 'a': //Kombicode
                    if (paramNummernformen.kombiZuCode[pKartenklasse][pKartenart] == -1) {
                        //					CaBug.drucke("BlNummernformen.formatiereNrKomplett 005"); Ist keine Fehlersituation, da ja nicht alle Kombis definiert werden können! 
                        hString = "-";
                    } else {
                        hString = Integer.toString(paramNummernformen.kombiZuCode[pKartenklasse][pKartenart]);
                    }
                    formatierteNr = formatierteNr + hString;
                    break;
                case 'b': //Kartenklasse
                    if (paramNummernformen.klasseZuCode[pKartenklasse] == -1) {
                        CaBug.drucke("BlNummernformen.formatiereNrKomplett 003");
                        hString = "-";
                    } else {
                        hString = Integer.toString(paramNummernformen.klasseZuCode[pKartenklasse]);
                    }
                    formatierteNr = formatierteNr + hString;
                    break;
                case 'c': //Kartenart
                    if (paramNummernformen.artZuCode[pKartenart] == -1) {
                        CaBug.drucke("BlNummernformen.formatiereNrKomplett 004");
                        hString = "-";
                    } else {
                        hString = Integer.toString(paramNummernformen.artZuCode[pKartenart]);
                    }
                    formatierteNr = formatierteNr + hString;
                    break;
                case 'd': //Subnummernkreis
                    hString = Integer.toString(rcStimmkarteSubNummernkreis);
                    formatierteNr = formatierteNr + hString;
                    break;
                case 'e': //Identifikationsnummer
                    formatierteNr = formatierteNr + paramPruefzahlen.identifikationsNummer;
                    break;
                case 'f': //Mandantennummer
                    formatierteNr = formatierteNr + clGlobalVar.getMandantString();
                    break;
                case 'g'://dreistellige Kontrollzahl
                    formatierteNr = formatierteNr + paramPruefzahlen.dreistelligeKontrollzahl;
                    break;
                case 'h'://zweistellige Kontrollzahl
                    formatierteNr = formatierteNr + paramPruefzahlen.zweistelligeKontrollzahl;
                    break;
                case 'i'://einstellige Kontrollzahl
                    formatierteNr = formatierteNr + paramPruefzahlen.einstelligeKontrollzahl;
                    break;
                case 'j': //zweistellige Stimmkartennummer
                    hString = Integer.toString(rcStimmkartennummer);
                    if (hString.length() < 2) {
                        hString = "0" + hString;
                    }
                    formatierteNr = formatierteNr + hString;
                    break;
                case 'k': //berechnete Prüfziffer
                    hString = berechnePruefziffer(lZutrittsIdent, formatierteNr);
                    formatierteNr = formatierteNr + hString;
                    break;
                case 'l': //Ignorieren
                    formatierteNr = formatierteNr + "0";
                    break;
                case 'm': //eigentliche Identifikationsnummer
                    lZutrittsIdent = formatiereNr(pIdent, pKartenklasse);
                    hString = lZutrittsIdent;
                    if (!paramNummernkreise.istNumerisch[pKartenklasse]) {
                        while (hString.length() < paramNummernkreise.laengeKartennummer[pKartenklasse]) {
                            hString = "0" + hString;
                        }
                    }
                    formatierteNr = formatierteNr + hString;
                    break;
                case 'n': //Nebennummer
                    lZutrittsIdent = formatiereNr(pIdent, pKartenklasse);
                    hString = pNeben;
                    while (hString.length() < 2) {
                        hString = "0" + hString;
                    }
                    formatierteNr = formatierteNr + hString;
                    break;
                case 'o': //Stimmart
                    hString = Integer.toString(paramNummernformen.stimmartZuCode[rcStimmart]);
                    if (hString.length() < 1) {
                        hString = "0" + hString;
                    }
                    formatierteNr = formatierteNr + hString;
                    break;
                case 'p': //Stimmart
                    formatierteNr = formatierteNr + "-";
                    break;
                case 'q': //Gattung
                    hString = Integer.toString(rcGattung);
                    if (hString.length() < 1) {
                        hString = "0" + hString;
                    }
                    formatierteNr = formatierteNr + hString;
                    break;

                }
                //			System.out.println("i="+i+" formatierteNr="+formatierteNr+" nummernform="+paramNummernformen.nummernDefinition.get(nummernformIdent).get(i));

            }
        }

        return formatierteNr;

    }

    /**Berechnen einer Kontrollzahl, abhängig vom Verfahren in paramPruefzahlen.
     * 
     * nummerIdent = wird genommen, wenn Kontrollzahl nur auf die EK-Nr. selbst berechnet werden soll
     * nummerKomplett = wird genommen, wenn Kontrollzahl auf komplette Nummer (einschließlich
     * 		Prüfziffern etc.) berechnet werden soll
     */
    public String berechnePruefziffer(String nummerIdent, String nummerKomplett) {
        String kontrollzahl = "0"; //eigentlich Fehler
        String berechnungsGrundlage = "";
        int verfahren = paramPruefzahlen.berechnungsVerfahrenPruefziffer;

        if (verfahren > 10) {
            berechnungsGrundlage = nummerKomplett;
            verfahren -= 10;
        } else {
            berechnungsGrundlage = nummerIdent;
        }

        if (paramPruefzahlen.berechnungsVerfahrenPruefziffer == 0) {
            kontrollzahl = "0";
            return kontrollzahl; //eigentlich Fehler
        }

        int zaehler = 0;
        int summe = 0;
        for (int i = berechnungsGrundlage.length() - 1; i >= 0; i--) {
            int wert = 0;
            if (CaString.isDatum(berechnungsGrundlage.substring(i, i + 1))) {
                wert = Integer.parseInt(berechnungsGrundlage.substring(i, i + 1));
            }
            int faktor = 0;
            if (verfahren == 1 || verfahren == 3) {//39713
                switch (zaehler) {
                case 0:
                    faktor = 3;
                    break;
                case 1:
                    faktor = 9;
                    break;
                case 2:
                    faktor = 7;
                    break;
                case 3:
                    faktor = 1;
                    break;
                case 4:
                    faktor = 3;
                    break;
                }
                zaehler++;
                if (zaehler == 5) {
                    zaehler = 0;
                }
            }
            if (verfahren == 2 || verfahren == 4) {//31313
                if (zaehler == 0) {
                    faktor = 3;
                    zaehler = 1;
                } else {
                    faktor = 1;
                    zaehler = 0;
                }
            }
            summe += wert * faktor;
        }
        int ergebnis = (summe % 10);
        if (verfahren == 1 || verfahren == 2) {
            ergebnis = 10 - ergebnis;
        }
        kontrollzahl = Integer.toString(ergebnis);

        return kontrollzahl;
    }

    private int fuelleIdentifikationsnummer(String pNr) {
        return fuelleIdentifikationsnummer(pNr, true);
    }

    /**Returnwert:
     * <0, wenn grundsätzlich nicht dekodierbar, Nummernformfehler und ähnliches. Auch wenn einzelne Identifikation außerhalb Nummernkreis
     * und ähnliches.
     * Wenn Nummernform grundsätzlich dekodierbar, aber eine einzelne Identifikation nicht paßt, dann wird _zusätzlich_
     * die Fehlermeldung in rcRcZuIdentifikationsnummer zurückgegeben.
     * 
     * Fehlermeldungen und deren Verarbeitung:
     * pmNummernformUngueltig => Nummernform grundsätzlich irgendwie unpassend, keine Fortsetzung möglich
     * pfXyNichtImZulaessigenNummernkreis => Nummernform paßt grundsätzlich, Weiterverarbeitung möglich
     * 
     * Weitere Eingabeparameter:
     * rcKartenklasse
     */
    private int fuelleIdentifikationsnummer(String pNr, boolean mitFormatierung) {
        int hNr = 0;
        String hString = "";
        int returnWertGesamt = 1;

        pNr = pNr.trim();

        if (rcKartenklasse == KonstKartenklasse.appIdent || rcKartenklasse == KonstKartenklasse.unbekannt) {
            CaBug.drucke("BlNummernformen.fuelleIdentifikationsnummer 001");
        }

        if (mitFormatierung == false || !paramNummernkreise.istNumerisch[rcKartenklasse]) { //Nummernkreis ist alpha - keine Formatierung
            hString = pNr;
        } else {//Nummernkreis ist numerisch
            if (!CaString.isNummern(pNr) || pNr.length() > 9) {
                return CaFehler.pmNummernformUngueltig;
            }
            hNr = Integer.parseInt(pNr);
            hString = Integer.toString(hNr);
            while (hString.length() < paramNummernkreise.laengeKartennummer[rcKartenklasse]) {
                hString = "0" + hString;
            }
            if (hNr < paramNummernkreise.vonKartennummerGesamt[rcKartenklasse]
                    || hNr > paramNummernkreise.bisKartennummerGesamt[rcKartenklasse]) {
                returnWertGesamt = CaFehler.pfXyNichtImZulaessigenNummernkreis;
            }

        }

        rcIdentifikationsnummer.add(hString);
        rcIdentifikationsnummerNeben.add("00");//in "Neben"-Füllfunktion wird die letzte vorhandene Nummer ggf. überschrieben
        rcRcZuIdentifikationsnummer.add(returnWertGesamt);
        if (rcKartenklasse == KonstKartenklasse.eintrittskartennummerNeben) {
            rcKartenklasseZuIdentifikationsnummer.add(KonstKartenklasse.eintrittskartennummer);
        } else {
            rcKartenklasseZuIdentifikationsnummer.add(rcKartenklasse);
        }
        return returnWertGesamt;
    }

    private int fuelleIdentifikationsnummerNeben(String pNr) {
        if (!CaString.isNummern(pNr) || pNr.length() > 2) {
            return CaFehler.pmNummernformUngueltig;
        }
        String hString = pNr;
        while (hString.length() < 2) {
            hString = "0" + hString;
        }
        rcIdentifikationsnummerNeben.set(rcIdentifikationsnummerNeben.size() - 1, hString);
        return 1;
    }

    private String pString = "";
    private String pTeilString = "";
    private int pOffset;

    private int dekodiere_hStringNumerisch(int pLaenge) {
        CaBug.druckeLog("pOffset="+pOffset+"", logDruckenInt, 10);
        if (pString.length() < pOffset + pLaenge) {
            return CaFehler.pmNummernformUngueltig;
        }
        pTeilString = pString.substring(pOffset, pOffset + pLaenge);
        CaBug.druckeLog("pTeilString="+pTeilString+"", logDruckenInt, 10);
        
        pOffset += pLaenge;
        if (!CaString.isNummern(pTeilString)) {
            return CaFehler.pmNummernformUngueltig;
        }
        return 1;
    }

    private int dekodiere_hString(int pLaenge) {
        if (pString.length() < pOffset + pLaenge) {
            return CaFehler.pmNummernformUngueltig;
        }
        pTeilString = pString.substring(pOffset, pOffset + pLaenge);
        pOffset += pLaenge;
        return 1;
    }

    private int dekodiere_hStringBisStrichpunkt() {
        String hString = pString.substring(pOffset);
        int offsetStrichpunkt = hString.indexOf(";");
        if (offsetStrichpunkt == -1) {
            return CaFehler.pmNummernformUngueltig;
        }
        pTeilString = hString.substring(0, offsetStrichpunkt);
        pOffset += offsetStrichpunkt;
        return 1;
    }

    private int dekodiere_hStringBisStrichpunktOderEnde() {
        String hString = pString.substring(pOffset);
        int offsetStrichpunkt = hString.indexOf(";");
        if (offsetStrichpunkt == -1) {
            pTeilString = hString;
            pOffset = 999;
            return 1;
        }
        pTeilString = hString.substring(0, offsetStrichpunkt);
        pOffset += offsetStrichpunkt;
        return 1;
    }

    /**Alle-Return-Werte werden auf Ursprungszustand zurückgesetzt*/
    private void clearReturnwerte() {
        rcIstAppIdent = false;
        rcCodeart = 0;
        rcNummernformIdent = 0;
        rcKartenklasse = 0;
        rcKartenart = 0;
        rcStimmkarteSubNummernkreis = 1;
        rcEintrittskarteMitNeben = 0;
        rcStimmkartennummer = 0;
        rcStimmart = 0;
        rcGattung = 0;
        rcPersonenIdent = 0;
        rcBevollmaechtigter = 0;
        rcIdentifikationsnummer = new ArrayList<String>();
        rcIdentifikationsnummerNeben = new ArrayList<String>();
        rcRcZuIdentifikationsnummer = new ArrayList<Integer>();
        rcKartenklasseZuIdentifikationsnummer = new ArrayList<Integer>();
        rcAgendaVersion = 0;
        rcAgendaZeichenzahl = 0;
        rcAbstimmung = new ArrayList<String>();
        rcAppVertretendePersonIdent = new ArrayList<Integer>();
        rcSonstigeInformation = new ArrayList<String>();
        rcPasswort="";
    }

    /**nummer=gelesener String, der aufzubereiten ist
     * vorgewaehlteKlasse
     * 		= wie IntKartenklasse: 
     * 			> wenn Nummer "kürzer" als die identifikationslänge ist, dann
     * 				wird nummer rein als String aufbereitet gemäß vorgewaehlteKlasse.
     * 			> wenn Nummer "länger" ist, wird sie zerlegt und im Hinblick auf vorgewaehlteKlasse überprüft. 
     * 		= unbekannt => nummer wird analysiert und dementsprechend aufbereitet, soweit möglich
     * 
     * Returnwerte:
     * Ohne vollständige Verarbeitung:
     * 	pmNummernformUngueltig
     * 	pmNummernformAktionsnummerUngueltig
     *  pfNichtEindeutig
     *  pmNummernformMandantUngueltig
     * 	pfXyNichtImZulaessigenNummernkreis
     * 	pmNummernformStimmkartenSubNummernkreisUngueltig
     * 	pmNummernformHVIdentNrUngueltig
     * 	pmNummernformKontrollzahlFalsch
     * 	pmNummernformPruefzifferFalsch
     * pmNummernformStimmartFalsch
     * 
     */
    public int dekodiere(String nummer, int vorgewaehlteKlasse) {
        clearReturnwerte();
        CaBug.druckeLog("nummer (zum Start)="+nummer, logDruckenInt, 10);

        int strichpunktPos=nummer.indexOf("$");
        if (strichpunktPos!=-1) {
            rcPasswort=nummer.substring(strichpunktPos+1);
            nummer=nummer.substring(0,strichpunktPos);
            CaBug.druckeLog("nummer="+nummer+" rcPasswort="+rcPasswort, logDruckenInt, 10);
        }
        
        
        /*INFO AppIdent: die App liefert fest 51; an den ersten 3 Stellen.
         * Da z.B. bei ku164 die 5 anderweitig vergeben wurde, wird die 5 umgesetzt
         * auf die Ziffer, die in Kombicode an der AppIdent-Definition steht
         */
        if (nummer.length() > 3 && nummer.substring(0, 3).equals("51;")) {
            nummer = Integer.toString(paramNummernformen.kombiZuCode[KonstKartenklasse.appIdent][1])
                    + nummer.substring(1);
        }

        /**INFO ku1007: wg. Kodierungsfehler war EK falsch kodiert. D.h. alles was mit 13519 anfängt, erhält die 7 vorne dran!*/
        if (clGlobalVar.mandant == 116) {
            if (nummer.length() > 7) {
                if (nummer.substring(0, 5).equals("13519")) {
                    nummer = "7" + nummer;
                }
            }
        }

        if (nummer.length() > 10 && nummer.substring(0, 8).compareTo("https://") == 0) {
            /**es wurde ein QR-Code mit dem Internet-Link eingescannt.
             * Daraus Nummer einlesen und dann "normal" weiterverarbeiten.
             */
            int gef = nummer.indexOf("&ek=");
            if (gef > 0) {
                nummer = nummer.substring(gef + 4);
                int gef1 = nummer.indexOf("&");
                if (gef1 > 0) {
                    nummer = nummer.substring(0, gef1);
                }
            } else {
                return CaFehler.pmNummernformUngueltig;
            }
            rcCodeart = KonstCodeart.codeInLink;
        }

        if (ParamSpezial.ku036(lDbBundle.clGlobalVar.mandant)) { /*ku036 - App umsetzen*/
            if (nummer.length() > 6 && nummer.substring(0, 3).compareTo("51;") == 0) {
                nummer = "9" + nummer.substring(15, 20) + "17019";
            }
            System.out.println("nummer=" + nummer);
        }
        //		
        //		if (lDbBundle.clGlobalVar.mandant==999){ /*ku036 - App umsetzen*/
        //			if (nummer.length()>6 && nummer.substring(0,5).compareTo("51999")==0){
        //				nummer="1"+nummer.substring(7,14)+"9990";
        //			}
        //			System.out.println("nummer="+nummer);
        //		}
        //

        int rc;
        int hNr = 0;
        String hString;
        int hAktionsnummer = 0;
        int gef = 0;

        nummer = nummer.trim();

        /*Überprüfen: wenn nummer "kurz" und vorgewaehlteArt vorhanden, dann lediglich aufbereiten*/
        if (vorgewaehlteKlasse != KonstKartenklasse.unbekannt && vorgewaehlteKlasse != KonstKartenklasse.appIdent) {
            int spaceEnthalten = nummer.indexOf(' ');
            if (spaceEnthalten == -1) {//Kein Blank enthalten, d.h. kein Nebencode abgetrennt
                if (nummer.length() <= paramNummernkreise.laengeKartennummer[vorgewaehlteKlasse]
                        || vorgewaehlteKlasse == KonstKartenklasse.stimmkartennummerSecond) {
                    rcKartenklasse = vorgewaehlteKlasse;
                    rc = fuelleIdentifikationsnummer(nummer);
                    if (rc < 1) {
                        return rc;
                    }
                    rc = fuelleIdentifikationsnummerNeben("00");
                    if (rcKartenklasse == KonstKartenklasse.eintrittskartennummerNeben) {
                        rcKartenklasse = KonstKartenklasse.eintrittskartennummer;
                    }
                    rcEintrittskarteMitNeben = 0;
                    if (rcCodeart == 0) {
                        rcCodeart = KonstCodeart.nurNummer;
                    }
                    return rc;
                }
            } else { // Blank enthalten => unabhängig von Nummer aufbereiten
                /*Immer: vor Blank = identifikation, nach Blank Nebennummer*/
                String vorBlank = nummer.substring(0, spaceEnthalten);
                String nachBlank = nummer.substring(spaceEnthalten + 1);
                rcKartenklasse = vorgewaehlteKlasse;
                rc = fuelleIdentifikationsnummer(vorBlank);
                if (rc < 1) {
                    return rc;
                }
                rc = fuelleIdentifikationsnummerNeben(nachBlank);
                if (rcKartenklasse == KonstKartenklasse.eintrittskartennummerNeben) {
                    rcKartenklasse = KonstKartenklasse.eintrittskartennummer;
                }
                rcEintrittskarteMitNeben = 1;
                if (rcCodeart == 0) {
                    rcCodeart = KonstCodeart.nurNummer;
                }
                return rc;
            }
        }

        /*Überprüfen: wenn nummer "kurz", oder ein Blank enthalten (Nebennummer!), dann aus Nummernkreis die Kartenklasse 
         * bestimmen*/
        int spaceEnthalten = nummer.indexOf(' ');
        if (CaBug.pruefeLog(logDruckenInt, 10)) {
            CaBug.druckeLog("BlNummernform.dekodiere paramNummernkreise.laengeOhnePruefziffern="
                    + paramNummernkreise.laengeOhnePruefziffern + " spaceEnthalten=" + spaceEnthalten
                    + " paramNummernkreise.einNummernkreisIstAlpha=" + paramNummernkreise.einNummernkreisIstAlpha, logDruckenInt, 10);
        }
        if ((nummer.length() <= paramNummernkreise.laengeOhnePruefziffern || spaceEnthalten != -1)
                && !paramNummernkreise.einNummernkreisIstAlpha) {
            if (CaBug.pruefeLog(logDruckenInt, 10)) {
                CaBug.druckeInfo("BlNummernform.dekodiere A");
            }
            if (rcCodeart == 0) {
                rcCodeart = KonstCodeart.nurNummer;
            }
            String vorBlank = "", nachBlank = "";
            if (spaceEnthalten != -1) { /*Aufteilen*/
                vorBlank = nummer.substring(0, spaceEnthalten);
                nachBlank = nummer.substring(spaceEnthalten + 1);
                rcEintrittskarteMitNeben = 1;
            } else {/*Normale Kurze*/
                vorBlank = nummer;
                nachBlank = "00";
                rcEintrittskarteMitNeben = 0;
            }
            if (!CaString.isNummern(vorBlank) || vorBlank.length() > 9) {
                return CaFehler.pmNummernformUngueltig;
            }
            hNr = Integer.parseInt(vorBlank);
            gef = 0;
            for (int i = 1; i <= 4; i++) {
                if (hNr >= paramNummernkreise.vonKartennummerGesamt[i]
                        && hNr <= paramNummernkreise.bisKartennummerGesamt[i]) {
                    rcKartenklasse = i;
                    gef++;
                }
            }
            if (gef > 1) {
                return CaFehler.pfNichtEindeutig;
            }
            if (rcKartenklasse > 0) {
                rc = fuelleIdentifikationsnummer(vorBlank);
                if (rc < 1) {
                    return rc;
                }
                rc = fuelleIdentifikationsnummerNeben(nachBlank);
                if (rcKartenklasse == KonstKartenklasse.eintrittskartennummerNeben) {
                    rcKartenklasse = KonstKartenklasse.eintrittskartennummer;
                }
                return rc;
            }

            return CaFehler.pfXyNichtImZulaessigenNummernkreis;
        }

        /*Nun passende Nummernform auswählen*/
        /*- Erster Lauf: anhand 1. Nummer (=Klasse, oder Combicode)*/
        hString = nummer.substring(0, 1);
        if (!CaString.isNummern(hString)) {
            return CaFehler.pmNummernformAktionsnummerUngueltig;
        }
        hAktionsnummer = Integer.parseInt(hString);
        /*Kombicode?*/
        gef = 0;
        for (int i = 1; i < 10; i++) {
            for (int i1 = 1; i1 < 9; i1++) {
                if (paramNummernformen.kombiZuCode[i][i1] == hAktionsnummer) {
                    rcKartenklasse = i;
                    rcKartenart = i1;
                    gef = 1;
                }
            }
        }
        if (gef != 1) { /*kein Kombicode gefunden - nun Suchen nach Kartenklasse / Kartenart*/
            if (CaBug.pruefeLog(logDruckenInt, 10)) {
                CaBug.druckeLog("BlNummernform.dekodiere B", logDruckenInt, 10);
            }

            gef = 0;
            for (int i = 1; i < 9; i++) {
                if (paramNummernformen.klasseZuCode[i] == hAktionsnummer) {
                    rcKartenklasse = i;
                    gef = 1;
                }
            }
            if (gef == 0) {
                return CaFehler.pmNummernformAktionsnummerUngueltig;
            }
            if (rcKartenklasse != KonstKartenklasse.appIdent) {
                /*Nun noch an zweiter Stelle Kartenart holen*/
                hString = nummer.substring(1, 2);
                if (!CaString.isNummern(hString)) {
                    return CaFehler.pmNummernformAktionsnummerUngueltig;
                }
                hAktionsnummer = Integer.parseInt(hString);
                gef = 0;
                for (int i = 1; i < 9; i++) {
                    if (paramNummernformen.artZuCode[i] == hAktionsnummer) {
                        rcKartenart = i;
                        gef = 1;
                    }
                }
                if (gef == 0) {
                    return CaFehler.pmNummernformAktionsnummerUngueltig;
                }
            }
        }

        /*Hier an dieser Stelle:
         * rcKartenklasse ist in jedem Fall gefüllt.
         * Falls keine AppIdent, dann ist hier auch rcKartenart gefüllt.
         */
        if (rcKartenklasse != KonstKartenklasse.appIdent) {
            if (CaBug.pruefeLog(logDruckenInt, 10)) {
                CaBug.druckeInfo("BlNummernform.dekodiere C - normale Nummernform auflösen");
            }

            /*******Nun normale Nummernform auflösen******/
            String lIdent = "";
            rcIstAppIdent = false;
            int nummernformNr = paramNummernformen.nummernformZuKlasseArt[rcKartenklasse][rcKartenart];
            rcNummernformIdent = nummernformNr;
            if (rcCodeart == 0) {
                rcCodeart = KonstCodeart.normalerNummerncode;
            }

            ArrayList<Character> lNummernDefinition = paramNummernformen.nummernDefinition.get(nummernformNr);
            pOffset = 0;
            pString = nummer;
            for (int i = 0; i < lNummernDefinition.size(); i++) {
                char codeBuchstabe = lNummernDefinition.get(i);
                if (CaBug.pruefeLog(logDruckenInt, 10)) {
                    CaBug.druckeInfo("BlNummernform.dekodiere i=" + i + " code=" + codeBuchstabe);
                }
                switch (codeBuchstabe) {
                case 'a':
                    pOffset++;
                    break; //bereits vorher verarbeitet
                case 'b':
                    pOffset++;
                    break; //bereits vorher verarbeitet
                case 'c':
                    pOffset++;
                    break; //bereits vorher verarbeitet
                case 'd': //StimmkartenSubnummernkreis
                    rc = dekodiere_hStringNumerisch(2);
                    if (rc < 1) {
                        return rc;
                    }
                    rcStimmkarteSubNummernkreis = Integer.parseInt(pTeilString);
                    if (rcStimmkarteSubNummernkreis > 5) {
                        return CaFehler.pmNummernformStimmkartenSubNummernkreisUngueltig;
                    }
                    break;
                case 'e': //Identifikationssnummer
                    rc = dekodiere_hStringNumerisch(paramPruefzahlen.laengeIdentifikationsNummer);
                    if (rc < 1) {
                        return rc;
                    }
                    if (pTeilString.compareTo(paramPruefzahlen.identifikationsNummer) != 0) {
                        return CaFehler.pmNummernformHVIdentNrUngueltig;
                    }
                    break;
                case 'f': //Mandanten-Nummer
                    rc = dekodiere_hStringNumerisch(3);
                    if (rc < 1) {
                        return rc;
                    }
                    if (Integer.parseInt(pTeilString) != clGlobalVar.mandant) {
                        return CaFehler.pmNummernformMandantUngueltig;
                    }
                    break;
                case 'g'://dreistellige Kontrollzahl
                    rc = dekodiere_hStringNumerisch(3);
                    if (rc < 1) {
                        return rc;
                    }
                    if (pTeilString.compareTo(paramPruefzahlen.dreistelligeKontrollzahl) != 0) {
                        return CaFehler.pmNummernformKontrollzahlFalsch;
                    }
                    break;
                case 'h'://zweistellige Kontrollzahl
                    rc = dekodiere_hStringNumerisch(2);
                    if (rc < 1) {
                        return rc;
                    }
                    if (pTeilString.compareTo(paramPruefzahlen.zweistelligeKontrollzahl) != 0) {
                        return CaFehler.pmNummernformKontrollzahlFalsch;
                    }
                    break;
                case 'i'://einstellige Kontrollzahl
                    rc = dekodiere_hStringNumerisch(1);
                    if (rc < 1) {
                        return rc;
                    }
                    if (pTeilString.compareTo(paramPruefzahlen.einstelligeKontrollzahl) != 0) {
                        return CaFehler.pmNummernformKontrollzahlFalsch;
                    }
                    break;
                case 'j': //zweistellige Stimmkartennummer
                    rc = dekodiere_hStringNumerisch(2);
                    if (rc < 1) {
                        return rc;
                    }
                    rcStimmkartennummer = Integer.parseInt(pTeilString);
                    break;
                case 'k': //berechnete Prüfziffer
                    rc = dekodiere_hStringNumerisch(1);
                    if (rc < 1) {
                        return rc;
                    }
                    int pruefziffer = Integer.parseInt(pTeilString);
                    if (pruefziffer != Integer.parseInt(berechnePruefziffer(lIdent, pString.substring(0, pOffset)))) {
                        return CaFehler.pmNummernformPruefzifferFalsch;
                    }
                    break;
                case 'l': //Ignorieren
                    pOffset++;
                    break;
                case 'm': //eigentliche Identifikationsnummer
                    rc = dekodiere_hStringNumerisch(paramNummernkreise.laengeKartennummer[rcKartenklasse]);
                    if (rc < 1) {
                        return rc;
                    }
                    rc = fuelleIdentifikationsnummer(pTeilString);
                    if (rc < 1) {
                        return rc;
                    }
                    break;
                case 'n': //Nebennummer
                    rc = dekodiere_hStringNumerisch(2);
                    if (rc < 1) {
                        return rc;
                    }
                    rc = fuelleIdentifikationsnummerNeben(pTeilString);
                    if (rc < 1) {
                        return rc;
                    }
                    rcEintrittskarteMitNeben = 1;
                    break;
                case 'o': //Stimmart
                    rc = dekodiere_hStringNumerisch(1);
                    if (rc < 1) {
                        return rc;
                    }
                    int stimmartCode = Integer.parseInt(pTeilString);
                    rcStimmart = -1;
                    for (int i1 = 0; i1 < 4; i1++) {
                        if (paramNummernformen.stimmartZuCode[i1] == stimmartCode) {
                            rcStimmart = i1;
                        }
                    }
                    if (rcStimmart == -1) {
                        return CaFehler.pmNummernformStimmartFalsch;
                    }
                    break;
                case 'p': //Trennzeichen "-" Ignorieren
                    pOffset++;
                    break;
                case 'q': //Gattung
                    rc = dekodiere_hStringNumerisch(1);
                    if (rc < 1) {
                        return rc;
                    }
                    rcGattung = Integer.parseInt(pTeilString);
                    break;
                }
            }
        } else {
            if (ParamSpezial.ku178(lDbBundle.clGlobalVar.mandant) || 1==1) { /*XXX*/
                /********************AppIdent ku178*****************/
                if (CaBug.pruefeLog(logDruckenInt, 10)) {
                    CaBug.druckeInfo("BlNummernform.dekodiere D - AppIdentku178 auflösen");
                }

                rcIstAppIdent = true;
                /**1 Stelle AppIdent*/
                rcCodeart = KonstCodeart.appIdent;

                pOffset = 1;
                pString = nummer;
                /*1 Stelle: Kartenart*/
                rc = dekodiere_hStringNumerisch(1);
                if (rc < 1) {
                    return rc;
                }
                rcKartenart = Integer.parseInt(pTeilString);

                if (rcKartenart != 1) {
                    return CaFehler.pmNummernformAktionsnummerUngueltig;
                }
                if (CaBug.pruefeLog(logDruckenInt, 10)) {
                    CaBug.druckeInfo("BlNummernform.dekodiere E");
                }
                rc = dekodiere_hString(1);
                if (rc < 1) {
                    return rc;
                } /* ; */
                /*Hier: Startsequenz 51; verarbeitet*/

                System.out.println("0 =" + pTeilString + " <" + pString + " <" + pOffset);

                /*Nun solange Pärchen einlesen, bis EOL erreicht*/
                rc = dekodiere_hStringBisStrichpunktOderEnde();
                boolean erstenIgnorieren=true;
                while (!pTeilString.equals("EOL")) {
                    System.out.println("A =" + pTeilString + " <" + pString + " <" + pOffset);
                    rcKartenklasse = KonstKartenklasse.eintrittskartennummer;
                    if (CaBug.pruefeLog(logDruckenInt, 10)) {
                        CaBug.druckeInfo("BlNummernform.dekodiere H");
                    }

                    /*BO_Ident - bereits gelesen - aber noch nicht ;*/
                    if (erstenIgnorieren==false) {
                        rc = fuelleIdentifikationsnummer(pTeilString);
                        if (rc < 1) {
                            return rc;
                        }
                    }
                    if (CaBug.pruefeLog(logDruckenInt, 10)) {
                        CaBug.druckeInfo("BlNummernform.dekodiere H2");
                    }
                    rc = dekodiere_hString(1);
                    if (rc < 1) {
                        return rc;
                    } /* ; */

                    /*Mitgliedsnummer - als Info abspeichern*/
                    rc = dekodiere_hStringBisStrichpunktOderEnde();
                    if (erstenIgnorieren==false) {
                        rcSonstigeInformation.add(pTeilString);
                    }
                    System.out.println("B =" + pTeilString + " <" + pString + " <" + pOffset);
                    rc = dekodiere_hString(1);
                    if (rc < 1) {
                        return rc;
                    } /* ; */

                    if (erstenIgnorieren==false) {
                        rc = fuelleIdentifikationsnummerNeben("00");
                    }

                    /*Zugeordnete Teilnehmende Person*/
                    if (erstenIgnorieren==false) {
                        rcAppVertretendePersonIdent.add(0);
                    }
                    else {
                        erstenIgnorieren=false;
                    }

                    /*Nächste Nummer zur Verarbeitung bereitstellen*/
                    rc = dekodiere_hStringBisStrichpunktOderEnde();
                    System.out.println("C =" + pTeilString + " <" + pString + " <" + pOffset);

                    if (CaBug.pruefeLog(logDruckenInt, 10)) {
                        CaBug.druckeInfo("BlNummernform.dekodiere H7");
                    }

                }
                if (CaBug.pruefeLog(logDruckenInt, 10)) {
                    CaBug.druckeInfo("BlNummernform.dekodiere I");
                }

                /*PersonNatJur des Smartphones*/
                rcPersonenIdent = 0;

                /*PersonNatJur des Bevollmächtigten*/
                rcBevollmaechtigter = 0;

                /*Mandanten-Nummer*/

                /*dreistellige Kontrollzahl*/

                /*************AppIdent ku178 Ende**********************/
            } else {
                /*********AppIdent Standard auflösen Beginn***************/
                if (CaBug.pruefeLog(logDruckenInt, 10)) {
                    CaBug.druckeInfo("BlNummernform.dekodiere D - AppIdent auflösen");
                }

                rcIstAppIdent = true;
                /**1 Stelle AppIdent*/
                rcCodeart = KonstCodeart.appIdent;

                pOffset = 1;
                pString = nummer;
                /*1 Stelle: Kartenart*/
                rc = dekodiere_hStringNumerisch(1);
                if (rc < 1) {
                    return rc;
                }
                rcKartenart = Integer.parseInt(pTeilString);

                if (rcKartenart != 1 && rcKartenart != 4 && rcKartenart != 7) {
                    return CaFehler.pmNummernformAktionsnummerUngueltig;
                }
                if (CaBug.pruefeLog(logDruckenInt, 10)) {
                    CaBug.druckeInfo("BlNummernform.dekodiere E");
                }

                rc = dekodiere_hString(1);
                if (rc < 1) {
                    return rc;
                } /* ; */

                /*10 stellen AppIdent*/
                rc = dekodiere_hString(10);
                if (rc < 1) {
                    return rc;
                }
                //			if (pTeilString.compareTo(paramPruefzahlen.appVersion)!=0){
                //				return CaFehler.pmAppVersionFalsch;
                //			}
                if (CaBug.pruefeLog(logDruckenInt, 10)) {
                    CaBug.druckeInfo("BlNummernform.dekodiere F");
                }

                rc = dekodiere_hString(1);
                if (rc < 1) {
                    return rc;
                } /* ; */

                if (rcKartenart == 7) {/*Nur bei Abstimmungen*/
                    /*rcAgendaZeichenzahl*/
                    rc = dekodiere_hStringNumerisch(3);
                    if (rc < 1) {
                        return rc;
                    }
                    rcAgendaZeichenzahl = Integer.parseInt(pTeilString);
                    rc = dekodiere_hString(1);
                    if (rc < 1) {
                        return rc;
                    } /* ; */

                    /*rcAgendaVersion*/
                    rc = dekodiere_hStringNumerisch(3);
                    if (rc < 1) {
                        return rc;
                    }
                    rcAgendaVersion = Integer.parseInt(pTeilString);
                    rc = dekodiere_hString(1);
                    if (rc < 1) {
                        return rc;
                    } /* ; */

                }
                if (CaBug.pruefeLog(logDruckenInt, 10)) {
                    CaBug.druckeInfo("BlNummernform.dekodiere G");
                }

                /*Kartenklasse je enthaltener Karte*/
                int hKartenklasse = 0;
                rc = dekodiere_hStringNumerisch(1);
                if (CaBug.pruefeLog(logDruckenInt, 10)) {
                    CaBug.druckeInfo("BlNummernform.dekodiere G1 rc=" + rc + " pTeilstring=" + pTeilString + " pString="
                            + pString);
                }
                if (rc < 1) {
                    return rc;
                }
                hKartenklasse = Integer.parseInt(pTeilString);
                while (hKartenklasse == KonstKartenklasse.eintrittskartennummer
                        || hKartenklasse == KonstKartenklasse.gastkartennummer
                        || hKartenklasse == KonstKartenklasse.stimmkartennummer
                        || hKartenklasse == KonstKartenklasse.aktionaersnummer) {

                    if (CaBug.pruefeLog(logDruckenInt, 10)) {
                        CaBug.druckeInfo("BlNummernform.dekodiere H");
                    }
                    rcKartenklasse = hKartenklasse;
                    if (rcKartenklasse == KonstKartenklasse.aktionaersnummer) {//Aktionärsnummer
                        if (CaBug.pruefeLog(logDruckenInt, 10)) {
                            CaBug.druckeInfo("BlNummernform.dekodiere HA");
                        }
                        /*Identifikationsnummer*/
                        rc = dekodiere_hStringBisStrichpunkt();
                        if (rc < 1) {
                            return rc;
                        }
                        if (CaBug.pruefeLog(logDruckenInt, 10)) {
                            CaBug.druckeInfo("BlNummernform.dekodiere HA1");
                        }
                        rc = fuelleIdentifikationsnummer(pTeilString, false);
                        if (rc < 1) {
                            return rc;
                        }
                        if (CaBug.pruefeLog(logDruckenInt, 10)) {
                            CaBug.druckeInfo("BlNummernform.dekodiere HA2");
                        }
                        /*Zugeordnete Teilnehmende Person*/
                        rcAppVertretendePersonIdent.add(0);

                    } else {//EK-Nummer oder SK-Nummer oder Gast-Nummer
                        /*Identifikationsnummer*/
                        rc = dekodiere_hStringNumerisch(paramNummernkreise.laengeKartennummer[rcKartenklasse]);
                        if (rc < 1) {
                            return rc;
                        }
                        if (CaBug.pruefeLog(logDruckenInt, 10)) {
                            CaBug.druckeInfo("BlNummernform.dekodiere H1");
                        }
                        rc = fuelleIdentifikationsnummer(pTeilString);
                        if (rc < 1) {
                            return rc;
                        }
                        if (CaBug.pruefeLog(logDruckenInt, 10)) {
                            CaBug.druckeInfo("BlNummernform.dekodiere H2");
                        }
                        /*Identifikationsnummer "Neben"*/
                        rc = dekodiere_hStringNumerisch(2);
                        if (rc < 1) {
                            return rc;
                        }
                        if (CaBug.pruefeLog(logDruckenInt, 10)) {
                            CaBug.druckeInfo("BlNummernform.dekodiere H3");
                        }
                        rc = fuelleIdentifikationsnummerNeben(pTeilString);
                        if (rc < 1) {
                            return rc;
                        }
                        if (CaBug.pruefeLog(logDruckenInt, 10)) {
                            CaBug.druckeInfo("BlNummernform.dekodiere H4");
                        }
                        /*Zugeordnete Teilnehmende Person*/
                        rc = dekodiere_hStringNumerisch(5);
                        if (rc < 1) {
                            return rc;
                        }
                        if (CaBug.pruefeLog(logDruckenInt, 10)) {
                            CaBug.druckeInfo("BlNummernform.dekodiere H5");
                        }
                        rcAppVertretendePersonIdent.add(Integer.parseInt(pTeilString));
                    }

                    rc = dekodiere_hString(1);
                    if (rc < 1) {
                        return rc;
                    } /* ; */
                    if (CaBug.pruefeLog(logDruckenInt, 10)) {
                        CaBug.druckeInfo("BlNummernform.dekodiere H6");
                    }

                    if (rcKartenart == 7) {/*Nur bei Abstimmungen -Abstimmungsverhalten*/
                        rc = dekodiere_hString(rcAgendaZeichenzahl);
                        if (rc < 1) {
                            return rc;
                        }
                        rcAbstimmung.add(pTeilString);
                        rc = dekodiere_hString(1);
                        if (rc < 1) {
                            return rc;
                        } /* ; */
                    }
                    if (CaBug.pruefeLog(logDruckenInt, 10)) {
                        CaBug.druckeInfo("BlNummernform.dekodiere H7");
                    }

                    /*Kartenklasse der nächsten enthaltenen Karte, oder "Abschlußzeichen" 6*/
                    rc = dekodiere_hStringNumerisch(1);
                    if (rc < 1) {
                        return rc;
                    }
                    hKartenklasse = Integer.parseInt(pTeilString);
                    if (CaBug.pruefeLog(logDruckenInt, 10)) {
                        CaBug.druckeInfo("BlNummernform.dekodiere H8");
                    }

                }
                if (CaBug.pruefeLog(logDruckenInt, 10)) {
                    CaBug.druckeInfo("BlNummernform.dekodiere I");
                }

                /*Ende erreicht*/
                if (hKartenklasse != 6) {
                    return CaFehler.pmNummernformAktionsnummerUngueltig;
                }
                if (CaBug.pruefeLog(logDruckenInt, 10)) {
                    CaBug.druckeInfo("BlNummernform.dekodiere J");
                }

                /*PersonNatJur des Smartphones*/
                rc = dekodiere_hStringNumerisch(5);
                if (rc < 1) {
                    return rc;
                }
                rcPersonenIdent = Integer.parseInt(pTeilString);
                rc = dekodiere_hString(1);
                if (rc < 1) {
                    return rc;
                } /* ; */

                /*PersonNatJur des Bevollmächtigten*/
                rc = dekodiere_hStringNumerisch(5);
                if (rc < 1) {
                    return rc;
                }
                rcBevollmaechtigter = Integer.parseInt(pTeilString);
                rc = dekodiere_hString(1);
                if (rc < 1) {
                    return rc;
                } /* ; */
                if (CaBug.pruefeLog(logDruckenInt, 10)) {
                    CaBug.druckeInfo("BlNummernform.dekodiere K");
                }

                /*Mandanten-Nummer*/
                rc = dekodiere_hStringNumerisch(3);
                if (rc < 1) {
                    return rc;
                }
                /*INFO 801 und 802 fix einprogrammiert*/
                if (Integer.parseInt(pTeilString) != clGlobalVar.mandant && Integer.parseInt(pTeilString) != 801
                        && Integer.parseInt(pTeilString) != 802) {
                    return CaFehler.pmNummernformMandantUngueltig;
                }
                if (CaBug.pruefeLog(logDruckenInt, 10)) {
                    CaBug.druckeInfo("BlNummernform.dekodiere L");
                }

                /*dreistellige Kontrollzahl*/
                rc = dekodiere_hStringNumerisch(3);
                if (rc < 1) {
                    return rc;
                }
                //			if (pTeilString.compareTo(paramPruefzahlen.dreistelligeKontrollzahl)!=0){
                //				return CaFehler.pmNummernformKontrollzahlFalsch;
                //			}
                if (CaBug.pruefeLog(logDruckenInt, 10)) {
                    CaBug.druckeInfo("BlNummernform.dekodiere M");
                }

                /*Hash-Code*/
                /*TODO $9 App: Hashcode zur Absicherung der Unverfälschtheit der Übertragung*/

                /***********AppIdent Standard Ende************/
            }
        }

        if (rcKartenklasse == KonstKartenklasse.eintrittskartennummerNeben) {
            rcKartenklasse = KonstKartenklasse.eintrittskartennummer;
        }
        if (lDbBundle.param.paramNummernformen.ignoriereKartenart == 1) {
            rcKartenart = 0;
        }
        return 1;
    }

    /*****************************Aktienregister-Nummer bzw. Login-Kennung im Portal*******************************/

    /**Bereitet die als Aktionärsnummer eingegebene Nummer für die interne Verwendung auf.
     * Konkret:
     * > Leerzeichen werden entfernt
     * > Wenn was anderes als Ziffern enthalten sind, dann ansonsten unverändert lassen
     * > Wenn Inhaberaktien aktiv: liefert Ziffer ohne führende Null und ohne angehängte 0 oder 1 (bzw. Original-String, falls Länge>8)
     * > Wenn String kürzer ist als Aktionärsnummerlänge, dann fülle rechtsbündig mit 0 auf bis zur Länge der Aktionärsnummer
     * 
     * D.h.: 0 bzw. 1 anhängen muß nach Aufruf dieser Funktion ggf. noch durchgeführt werden.
     * 
     * Rückgabe:
     * Aufbereiteter String
     * 
     * Neue Funktion BlNummernformenBasis.loginKennungAufbereitenFuerIntern
     */
    @Deprecated
    public String aktienregisterNraufbereitenFuerIntern(String pEingegebeneNummer) {
        String hNummer = pEingegebeneNummer.replace(" ", "");
        CaBug.druckeLog("BlNummernformen.aktienregisterNraufbereitenFuerIntern", logDruckenInt, 10);

        if (CaString.isNummern(hNummer) == false) {
            return hNummer;
        }

        if (lDbBundle.param.paramBasis.inhaberaktienAktiv) {
            if (hNummer.length() > 8) {
                return hNummer;
            }
            hNummer = Integer.toString(Integer.parseInt(hNummer));
            return hNummer;
        }

        if (ParamSpezial.ku097(lDbBundle.clGlobalVar.mandant)) {
            return hNummer;
        }

        if (hNummer.length() < lDbBundle.param.paramBasis.laengeAktionaersnummer) {
            hNummer = CaString.fuelleLinksNull(hNummer, lDbBundle.param.paramBasis.laengeAktionaersnummer);
        }

        //		/*Temporär eingefügt - 0 anhängen. Damit wahrscheinlich "1" hinten nicht mehr aufrufbar*/
        //		hNummer=hNummer+"0";

        CaBug.druckeLog("hNummer=" + hNummer, logDruckenInt, 10);
        return hNummer;
    }
}
