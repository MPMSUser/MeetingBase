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
package de.meetingapps.meetingportal.meetComAllg;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Enthält Hilfs-Funktionen für Stringverwaltung / Stringüberprüfung*/
public class CaString {

    private static int logDrucken=3;
    
    static public String ku178InternZuEingabe(String pText) {
        if (!CaString.isNummern(pText)) {
            return "";
        }
        String hString = pText.substring(0, 10);
        hString = Long.toString(Long.parseLong(hString));
        return hString;
    }

    /**false, falls Zeichen außer 0 bis 9 enthalten, oder wenn pruefe leer ist*/
    static public boolean isNummern(String pruefe) {
        if (pruefe.isEmpty())
            return false;

        int i;
        for (i = 0; i < pruefe.length(); i++) {
            if (pruefe.substring(i, i + 1).compareTo("0") < 0 || pruefe.substring(i, i + 1).compareTo("9") > 0) {
                return false;
            }
        }

        return (true);
    }

    /**false, falls Zeichen außer 0 bis 9 , A-Z enthalten; leer=true*/
    static public boolean isNummernAlphaOderLeer(String pruefe) {
        if (pruefe.isEmpty())
            return true;
        
        pruefe=pruefe.toUpperCase();

        int i;
        for (i = 0; i < pruefe.length(); i++) {
            if ((pruefe.substring(i, i + 1).compareTo("0") < 0 || pruefe.substring(i, i + 1).compareTo("9") > 0) && (pruefe.substring(i, i + 1).compareTo("A") < 0 || pruefe.substring(i, i + 1).compareTo("Z") > 0)) {
                return false;
            }
        }

        return (true);
    }

    static public boolean isNummernKomma(String pruefe) {
        if (pruefe.isEmpty())
            return false;

        int i;
        for (i = 0; i < pruefe.length(); i++) {
            if ((pruefe.substring(i, i + 1).compareTo("0") < 0 || pruefe.substring(i, i + 1).compareTo("9") > 0)
                    && pruefe.substring(i, i + 1).compareTo(",") != 0) {
                return false;
            }
        }

        return (true);
    }
    
    static public boolean isNummernKommaPunkt(String pruefe) {
        if (pruefe.isEmpty())
            return false;

        int i;
        for (i = 0; i < pruefe.length(); i++) {
            if ((pruefe.substring(i, i + 1).compareTo("0") < 0 || pruefe.substring(i, i + 1).compareTo("9") > 0)
                    && pruefe.substring(i, i + 1).compareTo(",") != 0 && pruefe.substring(i, i + 1).compareTo(".") != 0) {
                return false;
            }
        }

        return (true);
    }

    static public boolean isNummernNegativ(String pruefe) {
        if (pruefe.isEmpty())
            return false;
        if (pruefe.equals("-"))
            return false;

        int i;
        for (i = 0; i < pruefe.length(); i++) {
            if ((pruefe.substring(i, i + 1).compareTo("0") < 0 || pruefe.substring(i, i + 1).compareTo("9") > 0)
                    && pruefe.substring(i, i + 1).compareTo("-") != 0) {
                return false;
            }
            if (pruefe.substring(i, i + 1).equals("-") && i != 0) {
                return false;
            }
        }

        return (true);
    }

    /**false, gültige Emailadresse (d.h.: @ einmal enthalten, und vorher und hinterher String*/
    static public boolean isMailadresse(String pruefe) {
        int anzahlAt = 0;
        int posAt = -1;

        if (pruefe.isEmpty())
            return false;
        int i;
        for (i = 0; i < pruefe.length(); i++) {
            if (pruefe.substring(i, i + 1).compareTo("@") == 0) {
                anzahlAt++;
                posAt = i;
            }
        }
        if (anzahlAt != 1) {
            return false;
        }
        if (posAt == 0 || posAt == pruefe.length() - 1) {
            return false;
        }

        return true;
    }

    static public boolean isZeit(String pruefe) {
        if (pruefe.isEmpty())
            return false;
        if (pruefe.length() != 5)
            return false;
        if (CaString.isNummern(pruefe.substring(0, 2)) == false)
            return false;
        if (CaString.isNummern(pruefe.substring(3, 5)) == false)
            return false;
        if (pruefe.substring(2, 3).compareTo(":") != 0)
            return false;
        return (true);
    }

    static public boolean isDatum(String pruefe) {
        if (pruefe.isEmpty())
            return false;
        if (pruefe.length() != 10)
            return false;
        if (CaString.isNummern(pruefe.substring(0, 2)) == false)
            return false;
        if (CaString.isNummern(pruefe.substring(3, 5)) == false)
            return false;
        if (CaString.isNummern(pruefe.substring(6, 10)) == false)
            return false;
        if (pruefe.substring(2, 3).compareTo(".") != 0)
            return false;
        if (pruefe.substring(5, 6).compareTo(".") != 0)
            return false;
        return (true);

    }
    
    /**Liefert Double-Zahl formatiert mit "." in Deutschem Format*/
    static public String doubleToStringDE(double aktien) {
        String zahl = "";
        zahl = String.format("%,.2f", aktien);
        if (pruefeObKommaAnKomma(zahl,2)==false) {
            zahl=tauscheAufDeutscheTrennzeichen(zahl);
        }
        return zahl;
    }
    
    /**Liefert Double-Zahl formatiert mit "." in Deutschem Format mit nur einer Nachkommastelle*/
    static public String doubleToStringDEKurz(double aktien) {
        String zahl = "";
        zahl = String.format("%,.1f", aktien);
        if (pruefeObKommaAnKomma(zahl,1)==false) {
            zahl=tauscheAufDeutscheTrennzeichen(zahl);
        }
        return zahl;
    }

    /**Liefert Aktien/Stimmen formatiert mit "." in Deutschem Format*/
    static public String toStringDE(long aktien) {
        String zahl = "";
        zahl = String.format("%,d", aktien);
        zahl = zahl.replace(',', '.');
        return zahl;
    }

    /**Liefert Aktien/Stimmen formatiert mit "." in Deutschem Format*/
    static public String toStringDE(int aktien) {
        String zahl = "";
        zahl = String.format("%,d", aktien);
        zahl = zahl.replace(',', '.');
        return zahl;
    }

    static public String toStringEN(long aktien) {
        String zahl = "";
        zahl = String.format("%,d", aktien);
        zahl = zahl.replace('.', ',');
        return zahl;
    }

    static public String toEuroStringDE(double wert) {
//        String zahl = "";
//        zahl = String.format("%.2f", wert);
//        zahl = zahl.replace('.', ',');
//        return zahl;
        return toEuroStringDE(wert, 2);

    }

    static public String toEuroStringDE(double wert, int nachkomma) {
        String zahl = "";
        zahl = String.format("%,." + Integer.toString(nachkomma) + "f", wert);
        if (pruefeObKommaAnKomma(zahl,nachkomma)==false) {
            zahl=tauscheAufDeutscheTrennzeichen(zahl);
        }
        return zahl;
    }

    /**Fragt auch null und length=0 richtig ab*/
    static public int laenge(String pruefeString) {
        if (pruefeString == null)
            return 0;
        if (pruefeString.isEmpty())
            return 0;
        return pruefeString.length();

    }

    static public String trunc(String orig, int laenge) {
        if (orig == null) {
            return "";
        }
        if (orig.isEmpty()) {
            return "";
        }
        if (orig.length() < laenge) {
            return orig;
        }
        return orig.substring(0, laenge);
    }

    static public String trimZahlen(String pOrig) {
        String ergebnis = pOrig;

        if (ergebnis == null || ergebnis.isEmpty()) {
            return ergebnis;
        }

        while (ergebnis.length() > 0 && ergebnis.substring(0, 1).compareTo("0") >= 0
                && ergebnis.substring(0, 1).compareTo("9") <= 0) {
            ergebnis = ergebnis.substring(1);
        }

        while (ergebnis.length() > 0 && ergebnis.substring(ergebnis.length() - 1).compareTo("0") >= 0
                && ergebnis.substring(ergebnis.length() - 1).compareTo("9") <= 0) {
            ergebnis = ergebnis.substring(0, ergebnis.length() - 1);
        }

        return ergebnis;

    }

    static public double runde2Stellen(double wert) {
        double ergebnis;
        ergebnis = Math.round(wert * 100) / 100.0;

        return ergebnis;
    }

    static public double berechneProzent(double wert, double vonWert) {
        double ergebnis;
        if (vonWert != 0.00) {
            ergebnis = wert / vonWert * 100;
        } else {
            ergebnis = 0.0;
        }
        return ergebnis;
    }

    static public String prozentToString(double wert) {
        String ergebnis = String.format("%.2f", wert);
        ergebnis = ergebnis.replace('.', ',');
        return ergebnis;
    }

    static public String fuelleLinksNull(String pWert, int pLaenge) {
        while (pWert.length() < pLaenge) {
            pWert = "0" + pWert;
        }
        return pWert;
    }

    static public String fuelleLinksBlank(String pWert, int pLaenge) {
        while (pWert.length() < pLaenge) {
            pWert = " " + pWert;
        }
        return pWert;
    }

    static public String fuelleRechtsBlank(String pWert, int pLaenge) {
        while (pWert.length() < pLaenge) {
            pWert = pWert + " ";
        }
        return pWert;
    }

    static public String entferneCRLF(String pWert) {
         String ergebnis = pWert.replace(Character.toString((char) 10), "");
         ergebnis = ergebnis.replace(Character.toString((char) 13), "");
         return ergebnis;

    }

    static public String entferneBlank(String pWert) {
        String ergebnis = pWert.replace(" ", "");
        return ergebnis;

   }

    
    static public String entferneBlankRechts(String pWert){
        int i = pWert.length() - 1;
        while (i >= 0 && Character.isWhitespace(pWert.charAt(i))) {
            i--;
        }
        return pWert.substring(0, i + 1);
    }
 
    
    /**CR, LF, TAB*/
    static public String entferneSteuerzeichen(String pWert) {
        String ergebnis = pWert;
        if (ergebnis==null) {ergebnis="null";}
        ergebnis = ergebnis.replace(Character.toString((char) 10), "");
        ergebnis = ergebnis.replace(Character.toString((char) 13), "");
        ergebnis = ergebnis.replace(Character.toString((char) 9), "");

        return ergebnis;

    }

    /**CR, LF, TAB, /n \n*/
    static public String entferneSteuerzeichenKomplett(String pWert) {
        String ergebnis = pWert;
        if (ergebnis==null) {ergebnis="null";}
        ergebnis = ergebnis.replace(Character.toString((char) 10), "");
        ergebnis = ergebnis.replace(Character.toString((char) 13), "");
        ergebnis = ergebnis.replace(Character.toString((char) 9), "");

        ergebnis = ergebnis.replace("/n", "");
        ergebnis = ergebnis.replace("\n", "");
        ergebnis = ergebnis.replace("/r", "");
        ergebnis = ergebnis.replace("\r", "");

        return ergebnis;

    }

    /******************Übergreifende Funktionen für gemeinsamen C# und Java-Code************************/

    /**Entfernt "." aus String, und macht "," zu "."*/
    static public String eingabeZuIntern(String pWert) {
        String hString = "";
        hString = pWert.replace(".", "");
        hString = hString.replace(",", ".");
        return hString;
    }

    /**Wie Java Integer.toString*/
    static public String integerToString(int pWert) {
        return Integer.toString(pWert);
    }

    /**Wie Java Integer.parseInt, aber mit try-Catch*/
    static public int integerParseInt(String pWert) {
        try {
            return Integer.parseInt(pWert);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /**Wie Java Long.parseLong, aber mit try-Catch*/
    static public long longParseLong(String pWert) {
        try {
            return Long.parseLong(pWert);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /**Wie Java Double.parseDouble, aber mit try-Catch*/
    static public double doubleParseDouble(String pWert) {
        try {
            return Double.parseDouble(pWert);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    /**Wie Java indexOf*/
    static public int indexOf(String pZuDurchsuchenderString, String pZuSuchenderString) {
        return pZuDurchsuchenderString.indexOf(pZuSuchenderString);
    }

    /**Wie Java substring(von, bis)*/
    static public String substring(String pString, int pVon, int pBis) {
        return (pString.substring(pVon, pBis));
    }
    
    /**Wie substring; liefert allerdings auch, wenn Ursprungs-String nicht lang genug (wird dann mit " " aufgefüllt*/
    static public String substringMitFehlerbehandlung(String pString, int pVon, int pBis) {
        String hString=pString;
        if (pBis>pString.length()) {
            hString=CaString.fuelleRechtsBlank(hString, pBis);
//            return "";
        }
        return (hString.substring(pVon, pBis));
    }

    
    /**In pZielString werden ab der Position pVon alle Zeichen mit pUeberschreibenString überschrieben. Wenn pZielString zu kurz ist,
     * wird pZielString entsprechend verlängert
     */
    static public String stringUeberschreibeTeilMitFehlerbehandlung(String pZielString, String pUeberschreibenString, int pVon) {
        int zuKurz=pZielString.length()-(pVon+pUeberschreibenString.length());
        while (zuKurz<0) {
            pZielString+=" ";
            zuKurz++;
        }
        
        pZielString=pZielString.substring(0, pVon)+pUeberschreibenString+pZielString.substring(pVon+pUeberschreibenString.length());
        
        
        return pZielString;
    }
    
    /**Wie Java substring(von)*/
    static public String substring(String pString, int pVon) {
        return (pString.substring(pVon));
    }

    /**Wie Java pString1.compareTo(pString2)*/
    static public int compareTo(String pString1, String pString2) {
        return pString1.compareTo(pString2);

    }

    /**Wie Java length()*/
    static public int length(String pString) {
        return pString.length();
    }

    /**Wie Java isempty()*/
    static public boolean isEmpty(String pWert) {
        return pWert.isEmpty();
    }

    /**Liefert aus einem String in der Form "(Zahl) Text" die Zahl. Z.B. für Combo-Box-Ergebnis-Auswertung*/
    static public int liefereKlammerZahlAmAnfang(String pString) {
        int lZahl = 0;
        int hIndex = pString.indexOf('(');
        int hIndexEnde = pString.indexOf(')');
        if (hIndex == -1 || hIndexEnde == -1) {
            return 0;
        }
        String identString = pString.substring(hIndex + 1, hIndexEnde);
        lZahl = Integer.parseInt(identString);
        return lZahl;
    }

    /**Liefert aus einem String in der Form "Text (Zahl)" die Zahl. Z.B. für Combo-Box-Ergebnis-Auswertung*/
    static public int liefereKlammerZahlAmEnde(String pString) {
        int lZahl = 0;
        int hIndex = pString.lastIndexOf('(');
        int hIndexEnde = pString.lastIndexOf(')');
        if (hIndex == -1 || hIndexEnde == -1) {
            return 0;
        }
        String identString = pString.substring(hIndex + 1, hIndexEnde);
        lZahl = Integer.parseInt(identString);
        return lZahl;
    }

    /**Ersetzt alle Vorkommnisse von pAlt in pQuelle durch pNeu*/
    static public String ersetzeString(String pQuelle, String pAlt, String pNeu) {
        String neuerText = pQuelle;
        int pos = neuerText.indexOf(pAlt);
        while (pos != -1) {
            neuerText = neuerText.substring(0, pos) + pNeu + neuerText.substring(pos + pAlt.length());
             pos = neuerText.indexOf(pAlt);
        }
        return neuerText;
    }
    
    static private boolean pruefeObKommaAnKomma(String pEingabe, int anzahlNachkomma) {
        if (anzahlNachkomma==0) {return false;}
        int laenge=pEingabe.length();
        if (laenge<=anzahlNachkomma) {return false;}
        if (pEingabe.substring(laenge-anzahlNachkomma-1, laenge-anzahlNachkomma).equals(",")) {
            return true;
        }
        else {
            return false;
        }
    }
    
    static private String tauscheAufDeutscheTrennzeichen(String pEingabe) {
        pEingabe=pEingabe.replace(',', ';');
        pEingabe=pEingabe.replace('.', ',');
        pEingabe=pEingabe.replace(';', '.');
        return pEingabe;
    }
    
    /**Zerlegt pLangString in lauter Strings mit maximaler Länge je pTeilLaenge.
     * Dabei werden mindesten pMinimumArrayGroesse-Anzahl Strings im Array zurückgegeben (ggf. Leer)*/
    public static String[] zerlegeInStrings(String pLangString, int pTeilLaenge, int pMinimumArrayGroesse) {
        int anz=0;
        int restlaenge=pLangString.length();
        while (restlaenge>0) {anz++;restlaenge-=pTeilLaenge;}
        
        if (anz<pMinimumArrayGroesse) {anz=pMinimumArrayGroesse;}
        
        String[] ergebnis=new String[anz];
        for (int i=0;i<anz;i++) {ergebnis[i]="";}
        
        int offset=0;
        restlaenge=pLangString.length();
        while (restlaenge>0) {
            String hString="";
            if (restlaenge>pTeilLaenge) {
                hString=pLangString.substring(0, pTeilLaenge);
                pLangString=pLangString.substring(pTeilLaenge);
            }
            else {
                hString=pLangString;
                pLangString="";
            }
            ergebnis[offset]=hString;
            
            offset++;
            restlaenge=pLangString.length();
        }
        
        if (CaBug.pruefeLog(logDrucken, 10)) {
            for (int i=0;i<anz;i++) {
                CaBug.druckeLog("i="+i+" ="+ergebnis[i], logDrucken, 10);
            }
        }
        return ergebnis;
    }
    
    /**
     * Format z.B. Musterstr. in Musterstraße um
     * @param input
     * @return
     */
    public static String replaceStreetAbbreviations(String input) {
        // Pattern to match street suffix abbreviations followed by optional spaces and characters like a number
        Pattern pattern = Pattern.compile("(.*)(St|St\\.|Str|Str\\.|Stra|Stra\\.|Sr|Sr\\.)(\\s*\\d+)?$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(input);

        if (matcher.find()) {
            // Determine the case of the last matching abbreviation
            String abbreviation = matcher.group(2);
            boolean isLowerCase = Character.isLowerCase(abbreviation.charAt(0));
            String replacement = isLowerCase ? "straße" : "Straße";
            
            // Preserve any trailing numbers or spaces
            String trailingPart = matcher.group(3) != null ? matcher.group(3) : "";
            
            return matcher.group(1) + replacement + trailingPart;
        }
        
        return input;
    }
    
     /* Durchsucht einen String der Wert zwischen Start und Ende wird zurückgegeben sofern vorhanden 
     * @param value String der durchsucht wird
     * @param start Startwert
     * @param ende Endwert
     * @return Ergebnis 
     */
      public static String searchBetween(String value, String start, String ende) {
          
          String regex = "(?<="+ start+")(.*?)(?="+ende+")";

          Pattern pattern = Pattern.compile(regex);
          Matcher matcher = pattern.matcher(value);
          if (matcher.find()) {
              return matcher.group(1);
          } else {
              return null;
          }
      }
      
      /**
       * Formats a SEPA address (IBAN) by grouping it into blocks of 4 characters.
       *
       * @param iban The IBAN (SEPA address) to format.
       * @return A formatted IBAN with groups of 4 characters separated by spaces.
       * @throws IllegalArgumentException if the input IBAN is null, empty, or improperly formatted.
       */
      public static String formatIban(String iban) {
          if (iban == null || iban.isBlank()) {
              throw new IllegalArgumentException("IBAN cannot be null or blank");
          }

          // Remove any existing spaces or invalid characters (if present)
          iban = iban.replaceAll("\\s+", "").toUpperCase();

          // Validate basic IBAN length (minimum 15 characters, maximum 34 according to IBAN standard)
          if (iban.length() < 15 || iban.length() > 34) {
              throw new IllegalArgumentException("Invalid IBAN length: " + iban.length());
          }

          // Insert spaces every 4 characters
          return iban.replaceAll(".{4}", "$0 ").trim();
      }
      
}
