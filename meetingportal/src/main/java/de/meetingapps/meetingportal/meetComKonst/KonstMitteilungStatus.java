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


/**Parameter im Hinblick auf Wortmelde-Ablauf:
 * > Testlauf durchführen: 0=nein, 1=Ja (1 Versuch), 2=Ja (2 Versuche)
 * > Wortmeldeaufruf durchführen - 2. Versuch: 0=nein, 1=Ja 
 * > Nicht Erreicht Testlauf in Rednerliste für Versammlungsleiter aufnehmen 
 * > Telefonanbindung oder Videokonferenz
 * 
 * Konferenz-Räume:
 * > Anzahl Test-Räume (maximal 3)
 * > Anzahl Sprech-Räume (maximal 3)
 * > Zu jedem Raum ID (max. 200 Stellen)

 * 
 * Bei allen Mitteilungsarten:
 * PaOK> Eingabe Name zwingend bei juristischer Person
 * PaOK     ToDo: Name Vorschlagen
*  PaOK> Kontaktdaten: Mail, Telefon jeweils anzeigen oder nicht
 * 
  * 
  *  */

public class KonstMitteilungStatus {

    static int logDrucken=3;
    
    /*Verwendet:
     * 0
     * 1
     * 4
     * 5
     * 7
     * 8
     * 9
     * 10
     * 11
     * 12
     * 13
     * 14
     * 15
     * 16
     * 17
     * 18
     * 
     * 21
     * 22
     * 23
     * 24
     * 25
     * 26
     * 27
     * 
     * 31
     * 32
     * 33
     * 34
     * 35
     * 37
     */

    
    
    
    /*=========================Stati für alle Mitteilungen==========================================*/
    /**Unbearbeitet - Status direkt nach Abgabe einer Mitteilung. Keine Anzeige in Rednerliste*/
    public final static int GESTELLT = 0; 

    /**Zurückgezogen*/
    public final static int ZURUECKGEZOGEN = 7;

    /*==============================Stati nur für Wortmeldungen========================================*/
//    
//    /**Für 9, 11, 12, 15 (Rednerstation) 
//     * und 32, 34 (Teststation)
//     * gilt:
//     * Wert in Objekt Modulo (%) = tatsächlicher Statuswert
//     * Wert in Objekt /100 = Stations-Nummer
//     */
//
//    
//    /**Testen*/
//    public final static int TEST_BEREIT_ZUM_TEST=31;
//    public final static int TEST_VERSUCH=32;
//    public final static int TEST_VERSUCH_NICHT_ERREICHT=33;
//    
//    public final static int TEST_VERSUCH_2=34;
//    public final static int TEST_VERSUCH_2_NICHT_ERREICHT=35;
//    
//    public final static int TEST_NICHT_ERFOLGREICH=37;
//
//    
//    /**Dieser Status wird benötigt, wenn Parameter "nicht automatisch in Rednerliste" gesetzt ist.
//     * Verwendung z.B.: am Anfang der HV werden schon Wortmeldungen entgegengenommen, und auch getestet.
//     */
//    public final static int WARTEND_FUER_AUFNAHMEN_IN_REDNERLISTE=16;
//
//    /**Wird in Rednerliste angezeigt. Noch nicht aufgerufen*/
//    public final static int IN_REDNERLISTE = 1;
//
//    /**Eigentlicher Redneraufruf, um Beitrag zu halten*/
//    public final static int IN_REDNERLISTE_WIRD_ANGERUFEN = 11;
//    public final static int IN_REDNERLISTE_AUFRUFEN_IN_LEITUNG = 12;
//    public final static int IN_REDNERLISTE_AUFRUFEN_NICHT_ERREICHT = 13;
//    public final static int IN_REDNERLISTE_NICHT_ERREICHT_BEREIT_FUER_VERSUCH_2 = 14;
//
//    public final static int IN_REDNERLISTE_WIRD_ANGERUFEN_VERSUCH_2 = 15;
//    public final static int IN_REDNERLISTE_AUFRUFEN_NICHT_ERREICHT_VERSUCH_2 = 17;
//
//    public final static int IN_REDNERLISTE_VERKUENDEN_AUFRUF_WIRD_WIEDERHOLT=18;
//    
//    public final static int SPRICHT=9;
//
//    /**Test fehlgeschlagen, wird nicht weiter verfolgt - erst mal erledigt*/
//    public final static int ERLEDIGT_TEST_FEHLGESCHLAGEN=8;
//    
//    /**nicht erreicht, wird nicht weiter verfolgt - erst mal erledigt*/
//    public final static int ERLEDIGT_NICHT_ERREICHT = 4;
//
//    
//    /**Redner hat gesprochen, damit erledigt*/
//    public final static int ERLEDIGT_GESPROCHEN = 5;
//    
//    public final static int ERLEDIGT_ABGEWIESEN=10;

    /*========================================Stati nur für Botschaften==========================*/
    public final static int inArbeit=21;
    public final static int abgelehntZuLang=22;
    public final static int abgelehntUnzulaessigerInhalt=23;
    public final static int abgelehntFormatNichtVerarbeitbar=24;
    public final static int abgelehntSonstiges=25;
    public final static int veroeffentlicht=26;
    public final static int zuSpaetEingegangen=27;
    
    /************************************************************************************************
     **********************************EclMitteilung.interneVerarbeitungLaufend**********************/
    public final static int verarbeitungNichtDurchgefuehrt=0;
    public final static int verarbeitungShrinkingLaeuft=1;
    public final static int verarbeitungErledigt=2;
    public final static int verarbeitungZuStreamLadenLaeuft=3;

    
//    @Deprecated
//    static public String getTextIntern(int nr) {
//        int telefonieNr=nr/100;
//        int status=nr % 100;
//        String ergString="";
//        if (telefonieNr>0) {
//            if (stationTestVerwenden(status)) {
//                ergString=" (Test-Station "+Integer.toString(telefonieNr)+")";
//            }
//            if (stationRedeVerwenden(status)) {
//                ergString=" (Rede-Station "+Integer.toString(telefonieNr)+")";
//            }
//        }
//        
//        switch (status) {
//        case GESTELLT: 
//            return "gestellt - noch unbearbeitet"+ergString;
//        case ZURUECKGEZOGEN: 
//            return "zurückgezogen"+ergString;
//            
//        case TEST_BEREIT_ZUM_TEST: 
//            return "Test - Bereit zum Aufrufen zum Testen"+ergString;
//        case TEST_VERSUCH: 
//            return "Test - wird aktuell durchgeführt"+ergString;
//         case TEST_VERSUCH_NICHT_ERREICHT: 
//            return "Test - nicht erreicht"+ergString;
//        case TEST_VERSUCH_2: 
//            return "Test - 2. Versuch wird durchgeführt"+ergString;
//        case TEST_VERSUCH_2_NICHT_ERREICHT: 
//            return "Test - 2. Versuch nicht erreicht"+ergString;
//        case TEST_NICHT_ERFOLGREICH: 
//            return "Test - nicht erfolgreich durchgeführt"+ergString;
//
//        case WARTEND_FUER_AUFNAHMEN_IN_REDNERLISTE: 
//            return "bereit für Rednerliste"+ergString;
//
//        case IN_REDNERLISTE: 
//            return "in Rednerliste";
//          
//        case IN_REDNERLISTE_WIRD_ANGERUFEN: 
//            return "in Rednerliste - wird gerade angerufen"+ergString;
//        case IN_REDNERLISTE_AUFRUFEN_IN_LEITUNG: 
//            return "in Rednerliste - in Leitung"+ergString;
//        case IN_REDNERLISTE_AUFRUFEN_NICHT_ERREICHT: 
//            return "in Rednerliste - Verkünden dass nicht erreicht"+ergString;
//        case IN_REDNERLISTE_NICHT_ERREICHT_BEREIT_FUER_VERSUCH_2: 
//            return "in Rednerliste - Bereit für 2. Anrufversucht"+ergString;
//
//        case IN_REDNERLISTE_WIRD_ANGERUFEN_VERSUCH_2: 
//            return "in Rednerliste - 2. Versuch wird gerade angerufen"+ergString;
//        case IN_REDNERLISTE_AUFRUFEN_NICHT_ERREICHT_VERSUCH_2: 
//            return "in Rednerliste - 2. Versuch endgültig nicht erreicht"+ergString;
//
//        case IN_REDNERLISTE_VERKUENDEN_AUFRUF_WIRD_WIEDERHOLT: 
//            return "in Rednerliste - Aufruf muß wiederholt werden"+ergString;
//         
//        case SPRICHT: 
//            return "spricht"+ergString;
//
//        case ERLEDIGT_TEST_FEHLGESCHLAGEN: 
//            return "erledigt - Test Fehlgeschlagen"+ergString;
//        case ERLEDIGT_NICHT_ERREICHT: 
//            return "erledigt - nicht erreicht"+ergString;
//        case ERLEDIGT_GESPROCHEN: 
//            return "erledigt - gesprochen"+ergString;
//        case ERLEDIGT_ABGEWIESEN: 
//            return "erledigt - abgewiesen"+ergString;
//
//        }
//        return "";
//    }

//    @Deprecated
//    static public String getTextVorsitz(int nr) {
//        switch (liefereNurStatus(nr)) {
//         case IN_REDNERLISTE_NICHT_ERREICHT_BEREIT_FUER_VERSUCH_2: 
//            return "(2. Versuch)";
//        case IN_REDNERLISTE_WIRD_ANGERUFEN: 
//            return "(wird gerade angerufen)";
//        case IN_REDNERLISTE_AUFRUFEN_IN_LEITUNG: 
//            return "(ist in der Leitung - kann aufgerufen werden)";
//        case IN_REDNERLISTE_AUFRUFEN_NICHT_ERREICHT: 
//            return "(Verkünden dass derzeit nicht erreichbar)";
//       case IN_REDNERLISTE_WIRD_ANGERUFEN_VERSUCH_2: 
//            return "(2. Versuch - wird gerade angerufen)";
//        case IN_REDNERLISTE_AUFRUFEN_NICHT_ERREICHT_VERSUCH_2: 
//            return "(Verkünden dass 2. Versuch vergeblich - ggf. neue Wortmeldung abgeben)";
//        case SPRICHT: 
//            return "(Spricht)";
//        case IN_REDNERLISTE_VERKUENDEN_AUFRUF_WIRD_WIEDERHOLT: 
//            return "(Rede hat nicht funktioniert - Aufruf muß wiederholt werden)";
//
//        }
//        return "";
//    }

//    @Deprecated
//    static public String getTextNr(int nr) {
//        return getTextNr(nr, KonstPortalFunktionen.wortmeldungen);
//    }
    
    /**nicht für wortmeldungen zu verwenden - dort steht Text-Nummer in der Status-Liste*/
    static public int getTextINr(int nr, int pFunktion) {
        switch (pFunktion) {
        case KonstPortalFunktionen.fragen:
            switch (nr % 100) {
            case 7:
                return 1277;
            default:
                return 1278;
            }
//        case KonstPortalFunktionen.wortmeldungen:
//            switch (nr % 100) {
//            case GESTELLT: 
//            case WARTEND_FUER_AUFNAHMEN_IN_REDNERLISTE: 
//                return "1158";
//            case IN_REDNERLISTE: 
//            case IN_REDNERLISTE_WIRD_ANGERUFEN:
//            case IN_REDNERLISTE_AUFRUFEN_IN_LEITUNG:
//            case IN_REDNERLISTE_WIRD_ANGERUFEN_VERSUCH_2:
//                return "1159";
//            case IN_REDNERLISTE_NICHT_ERREICHT_BEREIT_FUER_VERSUCH_2:
//            case IN_REDNERLISTE_AUFRUFEN_NICHT_ERREICHT:
//            case IN_REDNERLISTE_AUFRUFEN_NICHT_ERREICHT_VERSUCH_2:
//                return "1160";
//            case TEST_BEREIT_ZUM_TEST: 
//            case TEST_VERSUCH: 
//            case TEST_VERSUCH_NICHT_ERREICHT: 
//            case TEST_VERSUCH_2: 
//            case TEST_VERSUCH_2_NICHT_ERREICHT: 
//            case TEST_NICHT_ERFOLGREICH: 
//                return "1161";
//            case ERLEDIGT_NICHT_ERREICHT: 
//                return "1162";
//            case ERLEDIGT_GESPROCHEN: 
//                return "1163";
//            case ERLEDIGT_TEST_FEHLGESCHLAGEN: 
//                return "2097";
//           case ERLEDIGT_ABGEWIESEN: 
//                return "1164";
//            case ZURUECKGEZOGEN: 
//                return "1194";
//            case SPRICHT: 
//                return "2081";
//            default:
//                return "1158";
//            }
         case KonstPortalFunktionen.widersprueche:
            switch (nr % 100) {
            case 7:
                return 1405;
            default:
                return 1406;
            }
        case KonstPortalFunktionen.antraege:
            switch (nr % 100) {
            case 7:
                return 1407;
            default:
                return 1408;
            }
        case KonstPortalFunktionen.sonstigeMitteilungen:
            switch (nr % 100) {
            case 7:
                return 1409;
            default:
                return 1410;
            }
        case KonstPortalFunktionen.botschaftenEinreichen:
            switch (nr % 100) {
            case 7:
                return 1914;
            case 21:
                return 1950;
            case 22:
                return 1951;
            case 23:
                return 1952;
            case 24:
                return 1953;
            case 25:
                return 1954;
            case 26:
                return 1955;
            case 27:
                return 1956;
            default:
                return 1915;
            }
        }
        return 0;
    }


//    /**Liefert true, wenn "in Arbeit", d.h.: nicht fertig, und nicht in Rednerliste. D.h. das sind quasi die,
//     * die noch nicht aufgerufen werden können, aber potentiell noch Redner werden können)
//     */
//    @Deprecated
//    static public boolean inArbeit(int nr) {
//        switch (nr % 100) {
//        case GESTELLT: 
//        case TEST_BEREIT_ZUM_TEST:
//        case TEST_VERSUCH:
//        case TEST_VERSUCH_NICHT_ERREICHT:
//        case TEST_VERSUCH_2:
//        case TEST_VERSUCH_2_NICHT_ERREICHT:
//        case TEST_NICHT_ERFOLGREICH: 
//        case WARTEND_FUER_AUFNAHMEN_IN_REDNERLISTE:
//            return true;
//        }
//        return false;
//        
//    }
//    
//    @Deprecated
//    static public boolean zuBearbeitenDurchKoordination(int nr) {
//        switch (nr % 100) {
//        case GESTELLT: 
//        case TEST_VERSUCH_2_NICHT_ERREICHT:
//        case TEST_NICHT_ERFOLGREICH: 
//        case WARTEND_FUER_AUFNAHMEN_IN_REDNERLISTE: 
//            return true;
//        }
//        return false;
//    }
//
//    @Deprecated
//    static public boolean inTestliste(int nr) {
//        switch (nr % 100) {
//        case TEST_BEREIT_ZUM_TEST:
//        case TEST_VERSUCH:
//        case TEST_VERSUCH_NICHT_ERREICHT:
//        case TEST_VERSUCH_2:
//             return true;
//        }
//        return false;
//    }
//
//    @Deprecated
//    static public boolean inRednerlisteTelefonie(int nr) {
//        switch (nr % 100) {
//        case IN_REDNERLISTE:
//        case IN_REDNERLISTE_WIRD_ANGERUFEN:
//        case IN_REDNERLISTE_AUFRUFEN_IN_LEITUNG:
//        case IN_REDNERLISTE_AUFRUFEN_NICHT_ERREICHT:
//        case IN_REDNERLISTE_NICHT_ERREICHT_BEREIT_FUER_VERSUCH_2:
//        case IN_REDNERLISTE_WIRD_ANGERUFEN_VERSUCH_2:
//        case IN_REDNERLISTE_AUFRUFEN_NICHT_ERREICHT_VERSUCH_2:
//        case SPRICHT:
//        case IN_REDNERLISTE_VERKUENDEN_AUFRUF_WIRD_WIEDERHOLT:
//            return true;
//        }
//        return false;
//    }

//    @Deprecated
//    static public boolean inRednerlisteVersammlungsleitung(int nr) {
//        switch (nr % 100) {
//        case IN_REDNERLISTE:
//        case IN_REDNERLISTE_WIRD_ANGERUFEN:
//        case IN_REDNERLISTE_AUFRUFEN_IN_LEITUNG:
//        case IN_REDNERLISTE_AUFRUFEN_NICHT_ERREICHT:
//        case IN_REDNERLISTE_NICHT_ERREICHT_BEREIT_FUER_VERSUCH_2:
//        case IN_REDNERLISTE_WIRD_ANGERUFEN_VERSUCH_2:
//        case IN_REDNERLISTE_AUFRUFEN_NICHT_ERREICHT_VERSUCH_2:
//        case SPRICHT:
//        case IN_REDNERLISTE_VERKUENDEN_AUFRUF_WIRD_WIEDERHOLT:
//            return true;
//        }
//        return false;
//    }

//    @Deprecated
//    static public boolean inRednerlisteGesamt(int nr) {
//        switch (nr % 100) {
//        case IN_REDNERLISTE:
//        case IN_REDNERLISTE_WIRD_ANGERUFEN:
//        case IN_REDNERLISTE_AUFRUFEN_IN_LEITUNG:
//        case IN_REDNERLISTE_AUFRUFEN_NICHT_ERREICHT:
//        case IN_REDNERLISTE_NICHT_ERREICHT_BEREIT_FUER_VERSUCH_2:
//        case IN_REDNERLISTE_WIRD_ANGERUFEN_VERSUCH_2:
//        case IN_REDNERLISTE_AUFRUFEN_NICHT_ERREICHT_VERSUCH_2:
//        case IN_REDNERLISTE_VERKUENDEN_AUFRUF_WIRD_WIEDERHOLT:
//        case SPRICHT:
//            return true;
//        }
//        return false;
//       
//    }


//    @Deprecated
//    static public boolean erledigt(int nr) {
//        switch (nr % 100) {
//        case ZURUECKGEZOGEN: 
//        case ERLEDIGT_TEST_FEHLGESCHLAGEN:
//        case ERLEDIGT_NICHT_ERREICHT: 
//        case ERLEDIGT_GESPROCHEN: 
//        case ERLEDIGT_ABGEWIESEN: 
//            return true;
//        }
//        return false;
//    }

    /**Nicht für Funktion Wortmeldung zu verwenden - bei Wortmeldungen steht das in der konfigurierbaren Statusliste*/
    static public boolean zurueckziehenMoeglich(int nr) {
        switch (nr % 100) {
//        case TEST_VERSUCH: 
//        case TEST_VERSUCH_2: 
//        case IN_REDNERLISTE_WIRD_ANGERUFEN: 
//        case IN_REDNERLISTE_AUFRUFEN_IN_LEITUNG: 
//        case IN_REDNERLISTE_WIRD_ANGERUFEN_VERSUCH_2: 
//        case SPRICHT: 
//        case ERLEDIGT_GESPROCHEN: 
        case ZURUECKGEZOGEN: 
            return false;
        }
        return true;
   }
    
//    /**Liefert - in Abhängigkeit des Status - die Info, ob der Button in der Testliste angezeigt 
//     * werden soll oder nicht*/
//     static public List<EhJsfButton> buttonInBearbeitungsListeKoordination(int pStatus) {
//         return buttonZuStatus(pStatus, 1);
//     }
//
//    /**Liefert - in Abhängigkeit des Status - die Info, ob der Button in der Testliste angezeigt 
//     * werden soll oder nicht*/
//     static public List<EhJsfButton> buttonInRednerListeTest(int pStatus) {
//         return buttonZuStatus(pStatus, 2);
//     }
//     
//     /**Liefert - in Abhängigkeit des Status - die Info, ob der Button in der Rednerliste der Telefonie angezeigt 
//      * werden soll oder nicht*/
//     static public List<EhJsfButton> buttonInRednerListeTelefonie(int pStatus) {
//         return buttonZuStatus(pStatus, 3);
//     }
//     
//     static public List<EhJsfButton> buttonInGesamtListe(int pStatus){
//         return buttonZuStatus(pStatus, 4);
//     }

//     /**pListe:
//      *     1=buttonInBearbeitungListeKoordination
//      *     2=buttonInRednerListeTest
//      *     3=buttonInRednerListeTelefonie
//      *     4=buttonInGesamtListe
//      */
//     @Deprecated
//     static public List<EhJsfButton> buttonZuStatus(int pStatus, int pListe){
//         List<EhJsfButton> lButtonList=new LinkedList<EhJsfButton>();
//         switch (pStatus % 100) {
//         
//         case GESTELLT: 
//             lButtonList.add(new EhJsfButton("Akzeptieren", TEST_BEREIT_ZUM_TEST));
//             lButtonList.add(new EhJsfButton("Ablehnen", ERLEDIGT_ABGEWIESEN));
//             break;
//         case ZURUECKGEZOGEN: 
//             break;
//          
//         case TEST_BEREIT_ZUM_TEST:
//             lButtonList.add(new EhJsfButton("Redner zum Test aufrufen", TEST_VERSUCH));
//             break;
//         case TEST_VERSUCH:
//             lButtonList.add(new EhJsfButton("Test erfolgreich", WARTEND_FUER_AUFNAHMEN_IN_REDNERLISTE));
//             lButtonList.add(new EhJsfButton("Test nicht erfolgreich", TEST_NICHT_ERFOLGREICH));
//             lButtonList.add(new EhJsfButton("Redner nicht erreicht", TEST_VERSUCH_NICHT_ERREICHT));
//             break;
//         case TEST_VERSUCH_NICHT_ERREICHT:
//             lButtonList.add(new EhJsfButton("Redner zum Test aufrufen", TEST_VERSUCH_2));
//             break;
//    
//         case TEST_VERSUCH_2:
//             lButtonList.add(new EhJsfButton("Test erfolgreich", WARTEND_FUER_AUFNAHMEN_IN_REDNERLISTE));
//             lButtonList.add(new EhJsfButton("Test nicht erfolgreich", TEST_NICHT_ERFOLGREICH));
//             lButtonList.add(new EhJsfButton("Redner nicht erreicht", TEST_VERSUCH_2_NICHT_ERREICHT));
//             break;
//         case TEST_VERSUCH_2_NICHT_ERREICHT:
//             lButtonList.add(new EhJsfButton("Endgültig erledigt", ERLEDIGT_NICHT_ERREICHT));
//             lButtonList.add(new EhJsfButton("Weitere Test-Runde", TEST_VERSUCH_2));
//             break;
//             
//        case TEST_NICHT_ERFOLGREICH: 
//            lButtonList.add(new EhJsfButton("Endgültig erledigt", ERLEDIGT_TEST_FEHLGESCHLAGEN));
//            lButtonList.add(new EhJsfButton("Weitere Test-Runde", TEST_VERSUCH_2));
//            break;
//            
//         case WARTEND_FUER_AUFNAHMEN_IN_REDNERLISTE: 
//             lButtonList.add(new EhJsfButton("In Rednerliste aufnehmen", IN_REDNERLISTE));
//             break;
//         
//         case IN_REDNERLISTE:
//             lButtonList.add(new EhJsfButton("Redner wird angerufen", IN_REDNERLISTE_WIRD_ANGERUFEN));
//             break;
//             
//         case IN_REDNERLISTE_WIRD_ANGERUFEN:
//             lButtonList.add(new EhJsfButton("Redner ist in der Leitung", IN_REDNERLISTE_AUFRUFEN_IN_LEITUNG));
//             lButtonList.add(new EhJsfButton("Redner wurde nicht erreicht", IN_REDNERLISTE_AUFRUFEN_NICHT_ERREICHT));
//             break;
//         case IN_REDNERLISTE_AUFRUFEN_IN_LEITUNG:
//             lButtonList.add(new EhJsfButton("Redner spricht", SPRICHT));
//             break;
//         case IN_REDNERLISTE_AUFRUFEN_NICHT_ERREICHT:
//             lButtonList.add(new EhJsfButton("Verkündet, dass nicht erreicht", IN_REDNERLISTE_NICHT_ERREICHT_BEREIT_FUER_VERSUCH_2));
//             break;
//         case IN_REDNERLISTE_NICHT_ERREICHT_BEREIT_FUER_VERSUCH_2:
//             lButtonList.add(new EhJsfButton("Redner wird angerufen", IN_REDNERLISTE_WIRD_ANGERUFEN_VERSUCH_2));
//             break;
//        case IN_REDNERLISTE_WIRD_ANGERUFEN_VERSUCH_2:
//            lButtonList.add(new EhJsfButton("Redner ist in der Leitung", IN_REDNERLISTE_AUFRUFEN_IN_LEITUNG));
//            lButtonList.add(new EhJsfButton("Redner wurde zum zweiten mal nicht erreicht", IN_REDNERLISTE_AUFRUFEN_NICHT_ERREICHT_VERSUCH_2));
//            break;
//         case IN_REDNERLISTE_AUFRUFEN_NICHT_ERREICHT_VERSUCH_2:
//             lButtonList.add(new EhJsfButton("Verkündet, dass zum zweiten mal nicht erreicht", ERLEDIGT_NICHT_ERREICHT));
//             break;
//         case IN_REDNERLISTE_VERKUENDEN_AUFRUF_WIRD_WIEDERHOLT:
//             lButtonList.add(new EhJsfButton("Verkündet, dass Rede gestört war und Redner nochmals aufgerufen wird", IN_REDNERLISTE));
//             break;
//         case SPRICHT:
//             lButtonList.add(new EhJsfButton("Redner erfolgreich beendet", ERLEDIGT_GESPROCHEN));
//             lButtonList.add(new EhJsfButton("Rede war gestört, wiederholen", IN_REDNERLISTE_VERKUENDEN_AUFRUF_WIRD_WIEDERHOLT));
//             break;
//             
//         case ERLEDIGT_TEST_FEHLGESCHLAGEN:
//         case ERLEDIGT_NICHT_ERREICHT:
//         case ERLEDIGT_GESPROCHEN:
//         case ERLEDIGT_ABGEWIESEN:
//             break;
//         }
//         return lButtonList;
//        
//     }

//     /**Liefert alle Statusse, die in der Einzelfallbearbeitung - in dieser Reihenfolge - als Select-Buttons aufgeführt werden sollen*/
//     public static List<Integer> liefereStatusArrayFuerEinzelfallbearbeitung(DbBundle pDbBundle) {
//         List<Integer> statusList=new LinkedList<Integer>();
//         
//         statusList.add(GESTELLT);
//
//         statusList.add(ZURUECKGEZOGEN);
//
//         if (pDbBundle.param.paramPortal.wortmeldungTestDurchfuehren!=0) {
//             statusList.add(TEST_BEREIT_ZUM_TEST);
//             statusList.add(TEST_VERSUCH);
//             statusList.add(TEST_VERSUCH_NICHT_ERREICHT);
//             statusList.add(TEST_VERSUCH_2);
//             statusList.add(TEST_VERSUCH_2_NICHT_ERREICHT);
//             statusList.add(TEST_NICHT_ERFOLGREICH);
//         }
//         
//         if (pDbBundle.param.paramPortal.wortmeldungNachTestManuellInRednerlisteAufnehmen!=0) {
//             statusList.add(WARTEND_FUER_AUFNAHMEN_IN_REDNERLISTE);
//         }
//
//         statusList.add(IN_REDNERLISTE);
//         statusList.add(IN_REDNERLISTE_WIRD_ANGERUFEN);
//         statusList.add(IN_REDNERLISTE_WIRD_ANGERUFEN_VERSUCH_2);
//         statusList.add(IN_REDNERLISTE_AUFRUFEN_NICHT_ERREICHT_VERSUCH_2);
//         statusList.add(IN_REDNERLISTE_AUFRUFEN_IN_LEITUNG);
//         statusList.add(IN_REDNERLISTE_AUFRUFEN_NICHT_ERREICHT);
//         statusList.add(IN_REDNERLISTE_NICHT_ERREICHT_BEREIT_FUER_VERSUCH_2);
//         statusList.add(IN_REDNERLISTE_AUFRUFEN_NICHT_ERREICHT_VERSUCH_2);
//
//         statusList.add(SPRICHT);
//
//         statusList.add(IN_REDNERLISTE_VERKUENDEN_AUFRUF_WIRD_WIEDERHOLT);
//
//         statusList.add(ERLEDIGT_NICHT_ERREICHT);
//         statusList.add(ERLEDIGT_TEST_FEHLGESCHLAGEN);
//         statusList.add(ERLEDIGT_GESPROCHEN);
//         statusList.add(ERLEDIGT_ABGEWIESEN);
//
//         return statusList;
//     }
     
//     @Deprecated
//     public static int statusWeiterleitung(DbBundle pDbBundle, int pStatus, int pTestStatus) {
//         CaBug.druckeLog("pStatus="+pStatus, logDrucken, 10);
//         int neuerStatus=pStatus;
//         
//         switch (neuerStatus) {
//         case TEST_BEREIT_ZUM_TEST:
//             if (pDbBundle.param.paramPortal.wortmeldungTestDurchfuehren==0) {
//                 neuerStatus=statusWeiterleitung(pDbBundle, WARTEND_FUER_AUFNAHMEN_IN_REDNERLISTE, pTestStatus);
//             }
//             else {
//                 if (pTestStatus==1) {
//                     neuerStatus=statusWeiterleitung(pDbBundle, WARTEND_FUER_AUFNAHMEN_IN_REDNERLISTE, pTestStatus);
//                 }
//             }
//             break;
//         case WARTEND_FUER_AUFNAHMEN_IN_REDNERLISTE:
//             if (pDbBundle.param.paramPortal.wortmeldungNachTestManuellInRednerlisteAufnehmen==0) {
//                 neuerStatus=statusWeiterleitung(pDbBundle, IN_REDNERLISTE, pTestStatus);
//             }
//             break;
//         }
//         CaBug.druckeLog("neuerStatus="+neuerStatus, logDrucken, 10);
//         return neuerStatus;
//     }
     
//     public static boolean stationsNrSollAngehaentWerden(int pStatus) {
//         switch (pStatus) {
//         case TEST_VERSUCH:
//         case TEST_VERSUCH_2:
//         case IN_REDNERLISTE_WIRD_ANGERUFEN:
//         case IN_REDNERLISTE_AUFRUFEN_IN_LEITUNG:
//         case IN_REDNERLISTE_WIRD_ANGERUFEN_VERSUCH_2:
//         case SPRICHT:
//             return true;
//         }
//         return false;
//     }
     
//     /**Liefert Kombination aus Status und ggf. Stationsnummer*/
//     public static int statusUndStationsNr(int pStatus, int pStation) {
//         int neuerStatus=pStatus;
//         if (stationsNrSollAngehaentWerden(pStatus)) {
//             neuerStatus=neuerStatus+pStation*100;
//         }
//         return neuerStatus;
//     }
     
    /**AAAAA Wortmeldungen - wenn mal umgestellt, dann die Funktion liefereNurStatus eliminieren*/
     /**Liefert aus kombiniertem Status/Stationsnummer nur den Status*/
     public static int liefereNurStatus(int pStatus) {
         return (pStatus % 100);
     }
     
//     /***************Station verwenden***********************************************/
//     public static boolean stationTestVerwenden(int pStatus) {
//         switch (pStatus) {
//         case TEST_VERSUCH:
//         case TEST_VERSUCH_2:
//             return true;
//         }
//        return false;
//     }
//     public static boolean stationRedeVerwenden(int pStatus) {
//         switch (pStatus) {
//         case SPRICHT:
//         case IN_REDNERLISTE_WIRD_ANGERUFEN:
//         case IN_REDNERLISTE_AUFRUFEN_IN_LEITUNG:
//         case IN_REDNERLISTE_WIRD_ANGERUFEN_VERSUCH_2:
//             return true;
//         }
//         return false;
//     }
     
//     /***************Status ohne lfd-Nr zulässig?***************/
//     public static boolean statusOhneLfdNrZUlaessig(int pStatus) {
//         switch (pStatus) {
//         case GESTELLT:
//         case ZURUECKGEZOGEN:
//         case ERLEDIGT_TEST_FEHLGESCHLAGEN:
//         case ERLEDIGT_NICHT_ERREICHT:
//         case ERLEDIGT_GESPROCHEN:
//         case ERLEDIGT_ABGEWIESEN:
//             return true;
//         }
//         return false;
//       
//     }
     
//     /************************Prüfen, ob die Aktion ausgeführt werden soll**************************/
//     public static boolean pruefeAktionTestStatusInEclLoginDatenNichtErfolgreich(int pAlterStatus, int pNeuerStatus) {
//         return (pNeuerStatus==KonstMitteilungStatus.TEST_NICHT_ERFOLGREICH);
//     }
//     public static boolean pruefeAktionTestStatusInEclLoginDatenErfolgreich(int pAlterStatus, int pNeuerStatus) {
//         return (pNeuerStatus==KonstMitteilungStatus.WARTEND_FUER_AUFNAHMEN_IN_REDNERLISTE || pNeuerStatus==KonstMitteilungStatus.IN_REDNERLISTE);
//     }
//
//     public static boolean pruefeAktionInTestraumKommen(int pAlterStatus, int pNeuerStatus) {
//         return (pNeuerStatus==KonstMitteilungStatus.TEST_VERSUCH || pNeuerStatus==KonstMitteilungStatus.TEST_VERSUCH_2);
//     }
//     public static boolean pruefeAktionTestraumVerlassen(int pAlterStatus, int pNeuerStatus) {
//         return (pAlterStatus==KonstMitteilungStatus.TEST_VERSUCH || pAlterStatus==KonstMitteilungStatus.TEST_VERSUCH_2);
//     }
//
//     public static boolean pruefeAktionInRedeeraumKommen(int pAlterStatus, int pNeuerStatus) {
//         return (pNeuerStatus==KonstMitteilungStatus.IN_REDNERLISTE_WIRD_ANGERUFEN || pNeuerStatus==KonstMitteilungStatus.IN_REDNERLISTE_WIRD_ANGERUFEN_VERSUCH_2);
//     }
//     public static boolean pruefeAktionSprechen(int pAlterStatus, int pNeuerStatus) {
//         return (false /*pNeuerStatus==KonstMitteilungStatus.SPRICHT*/);
//     }
//     public static boolean pruefeAktionRederaumVerlassen(int pAlterStatus, int pNeuerStatus) {
//         return (pNeuerStatus==KonstMitteilungStatus.IN_REDNERLISTE_AUFRUFEN_NICHT_ERREICHT || 
//                 pNeuerStatus==KonstMitteilungStatus.IN_REDNERLISTE_AUFRUFEN_NICHT_ERREICHT_VERSUCH_2 || 
//                 pAlterStatus==KonstMitteilungStatus.SPRICHT);
//     }

}
