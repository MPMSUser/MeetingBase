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
package de.meetingapps.meetingportal.meetComBVerwaltung;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaDateiWrite;
import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComAllg.CaString;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclAppTexte;
import de.meetingapps.meetingportal.meetComEntities.EclFehler;
import de.meetingapps.meetingportal.meetComEntities.EclPortalText;

/**Verwalten der Portal- und App-Texte*/
public class BvTexte {

    private int logDrucken = 1;

    /************************************Fürs Anzeigen**************************************************/
    public String[] rcPortalTexte = null;
    public String[] rcPortalAdaptivTexte = null;

    /**Liest die Portal-Texte für den Mandant lt. clGlobalVar (falls pStandardLesen==false - ansonsten 
     * den Standard-Mandant 0) - ein, bereitet sie für die Anzeige auf und legt sie in rcPortalTexte
     * und rcPortalAdaptivTexte ab.
     * 
     * Fehler-Return-Codes:
     * sysPortalSpracheNichtVorhanden
     * sysPortalSpracheNichtAktiv
     * */
    public int lesePortalTexteFuerAnzeige(DbBundle pDbBundle, boolean pStandardLesen, int pSprache) {

        EclPortalText aktuellerPortalTextInVerarbeitung = null;
        int anzahlPortalTexte = 0;

        /*Prüfen, ob Sprache zulässig*/
        if (pSprache > 2) {
            CaBug.drucke("001");
            return CaFehler.sysPortalSpracheNichtVorhanden;
        }
        if (pSprache == 2 && (pDbBundle.eclEmittent.portalSprache & 2) != 2) {
            CaBug.drucke("BvTexte.lesePortalTexteFuerAnzeige 002");
            return CaFehler.sysPortalSpracheNichtAktiv;
        }

        int release = pDbBundle.param.paramPortal.basisSetStandardTexteVerwenden;

        /*Alle übergreifenden Texte zu dieser Sprache einlesen - Länge Array ermitteln,
         * Array initialisieren. In Ergebnis-Array eintragen.
         */
        pDbBundle.dbPortalTexte.read_all(pSprache, false, false, release);

        anzahlPortalTexte = pDbBundle.dbPortalTexte.ergebnisMaxIdentGesamt();

        CaBug.druckeLog("BvTexte.lesePortalTexteFuerAnzeige anzahlPortalTexte=" + anzahlPortalTexte, logDrucken, 10);

        rcPortalTexte = new String[anzahlPortalTexte + 1];
        rcPortalAdaptivTexte = new String[anzahlPortalTexte + 1];

        int nr = 0;
        String hTextPortal = "", hTextPortalAdaptivText = "";
        for (int i = 0; i < pDbBundle.dbPortalTexte.anzErgebnis(); i++) {
            EclPortalText hEclPortalText = pDbBundle.dbPortalTexte.ergebnisPosition(i);
            int neueNummer = hEclPortalText.identGesamt;
            if (neueNummer != nr) {
                if (nr != 0) {
                    if (pDbBundle.param.paramPortal.standardTexteBeruecksichtigen == 1) {

                        rcPortalTexte[nr] = hTextPortal;

                        if (aktuellerPortalTextInVerarbeitung.portalAdaptivAbweichend == false) {
                            hTextPortalAdaptivText = hTextPortal;
                        }
                        rcPortalAdaptivTexte[nr] = hTextPortalAdaptivText;
                    } else {
                        rcPortalTexte[nr] = "";
                        rcPortalAdaptivTexte[nr] = "";
                    }
                }
                nr = neueNummer;
                aktuellerPortalTextInVerarbeitung = hEclPortalText;
                hTextPortal = hEclPortalText.portalText;
                hTextPortalAdaptivText = hEclPortalText.portalAdaptivText;
            } else {
                hTextPortal += hEclPortalText.portalText;
                hTextPortalAdaptivText += hEclPortalText.portalAdaptivText;
            }
        }
        if (nr != 0) {
            if (pDbBundle.param.paramPortal.standardTexteBeruecksichtigen == 1) {
                rcPortalTexte[nr] = hTextPortal;

                if (aktuellerPortalTextInVerarbeitung.portalAdaptivAbweichend == false) {
                    hTextPortalAdaptivText = hTextPortal;
                }
                rcPortalAdaptivTexte[nr] = hTextPortalAdaptivText;
            } else {
                rcPortalTexte[nr] = "";
                rcPortalAdaptivTexte[nr] = "";
            }
        }

        /*Alle Texte zu dieser Sprache und zum Mandanten einlesen - und soweit vorhanden und erforderlich
         * (sprich: Standard-Texte werden NICHT verwendet)
         * in Array übertragen
         */
        pDbBundle.dbPortalTexte.read_all(pSprache, true, false, 0);

        nr = 0;
        hTextPortal = "";
        hTextPortalAdaptivText = "";
        for (int i = 0; i < pDbBundle.dbPortalTexte.anzErgebnis(); i++) {
            EclPortalText hEclPortalText = pDbBundle.dbPortalTexte.ergebnisPosition(i);
            int neueNummer = hEclPortalText.identGesamt;
            if (neueNummer != nr) {
                if (nr != 0 && aktuellerPortalTextInVerarbeitung.portalVonStandardVerwenden == false) {
                    if (nr > anzahlPortalTexte + 1) {
                        CaBug.drucke("001 - Portaltexte fehlerhaft nr=" + nr);
                    } else {
                        rcPortalTexte[nr] = hTextPortal;
                        if (aktuellerPortalTextInVerarbeitung.portalAdaptivAbweichend == true) {
                            rcPortalAdaptivTexte[nr] = hTextPortalAdaptivText;
                        } else {
                            rcPortalAdaptivTexte[nr] = hTextPortal;
                        }
                    }

                }
                nr = neueNummer;
                aktuellerPortalTextInVerarbeitung = hEclPortalText;
                hTextPortal = hEclPortalText.portalText;
                hTextPortalAdaptivText = hEclPortalText.portalAdaptivText;
            } else {
                hTextPortal += hEclPortalText.portalText;
                hTextPortalAdaptivText += hEclPortalText.portalAdaptivText;
            }
        }
        if (nr != 0 && aktuellerPortalTextInVerarbeitung.portalVonStandardVerwenden == false) {
            if (nr > anzahlPortalTexte + 1) {
                CaBug.drucke("002 - Portaltexte fehlerhaft nr=" + nr);
            } else {
                rcPortalTexte[nr] = hTextPortal;
                if (aktuellerPortalTextInVerarbeitung.portalAdaptivAbweichend == true) {
                    rcPortalAdaptivTexte[nr] = hTextPortalAdaptivText;
                } else {
                    rcPortalAdaptivTexte[nr] = hTextPortal;
                }
            }

        }

        return 1;
    }

    /***************************Fürs Editieren*********************************************/

    /**mandantspezifisch und uebergreifend werden bei lesePortalTexteFuerBearbeitung belegt
     * und in den Folgefunktionen entsprechend berücksichtigt
     */
    /**true => Mandantentexte sind eingelesen;
     * false=> Standard-Texte sind eingelesen;
     */
    private boolean mandantspezifisch = false;

    /**true => die "Mandantenübergreifenden Texte" (für App) sind eingelesen
     * false => die normalen Portaltexte (mandantenabhängig) sind eingelesen
     */
    private boolean uebergreifend = false;

    /****Aufbau analog EclPortalTexteEditM, plus Zusatzfelder die für App-Text-Erzeugung benötigt werden******/

    public int[] rcNummer=null;
    
    public String[] rcSeitenName = null;
    public String[] rcBeschreibung = null;

    public boolean[] rcTextInPortal = null;
    public boolean[] rcTextInApp = null;

    public int[] rcVerbundenMitIdentGesamt = null;

    public int[] rcSeitennummer = null;
    public int[] rcIdent = null;

    /*************Standard-Texte**************************/
    /*Verarbeitungshinweis: Beim Einlesen eines Sets (auch wenn 
     * nur nicht-mandantenspezifisch eingelesen wird) werden die
     * Standard-Texte hier abelegt.
     * 
     * Beim Bearbeiten der Standard-Texte werden diese dann im
     * Bearbeitungsmodul nach rcPortalTextDE etc. übertragen zur RÜckspeicherung.
     */
    /*DE*/
    public String[] rcPortalStandardTextDE = null;

    public boolean[] rcPortalStandardAdaptivAbweichendDE = null;
    public String[] rcPortalStandardAdaptivTextDE = null;

    public boolean[] rcAppStandardAbweichendDE = null;
    public String[] rcAppStandardTextDE = null;

    /*EN*/
    public String[] rcPortalStandardTextEN = null;

    public boolean[] rcPortalStandardAdaptivAbweichendEN = null;
    public String[] rcPortalStandardAdaptivTextEN = null;

    public boolean[] rcAppStandardAbweichendEN = null;
    public String[] rcAppStandardTextEN = null;

    /**********************Deutsch*************************************/
    /**Hierüber erfolgt der Update/Übertragung zu App. Es werden immer alle Texte zur App
     * übertragen, deren Versionsnummer höher ist als die aktull in der App hinterlegte
     * Versionsnummer.
     */
    public int[] rcLetzteVersionDE = null;

    public boolean[] rcPortalVonStandardVerwendenDE = null;
    public String[] rcPortalTextDE = null;

    public boolean[] rcPortalAdaptivAbweichendDE = null;
    public String[] rcPortalAdaptivTextDE = null;

    public boolean[] rcAppAbweichendDE = null;
    public String[] rcAppTextDE = null;

    /***********************Englisch*****************************************/
    /**Hierüber erfolgt der Update/Übertragung zu App. Es werden immer alle Texte zur App
     * übertragen, deren Versionsnummer höher ist als die aktull in der App hinterlegte
     * Versionsnummer.
     */
    public int[] rcLetzteVersionEN = null;

    public boolean[] rcPortalVonStandardVerwendenEN = null;
    public String[] rcPortalTextEN = null;

    public boolean[] rcPortalAdaptivAbweichendEN = null;
    public String[] rcPortalAdaptivTextEN = null;

    public boolean[] rcAppAbweichendEN = null;
    public String[] rcAppTextEN = null;

    public void initRCLaengeEins() {
        rcNummer=new int[1];
        
        rcSeitenName=new String[1];
        rcBeschreibung=new String[1];
        
        rcTextInPortal=new boolean[1];
        rcTextInApp=new boolean[1];

        rcVerbundenMitIdentGesamt=new int[1];

        rcSeitennummer=new int[1];
        rcIdent=new int[1];

        rcPortalStandardTextDE=new String[1];
        
        rcPortalStandardAdaptivAbweichendDE=new boolean[1];
        rcPortalStandardAdaptivTextDE=new String[1];
        
        rcAppStandardAbweichendDE=new boolean[1];
        rcAppStandardTextDE=new String[1];
        
        rcPortalStandardTextEN=new String[1];
        
        rcPortalStandardAdaptivAbweichendEN=new boolean[1];
        rcPortalStandardAdaptivTextEN=new String[1];
        
        rcAppStandardAbweichendEN=new boolean[1];
        rcAppStandardTextEN=new String[1];
        
        rcLetzteVersionDE=new int[1];
        
        rcPortalVonStandardVerwendenDE=new boolean[1];
        rcPortalTextDE=new String[1];
        
        rcPortalAdaptivAbweichendDE=new boolean[1];
        rcPortalAdaptivTextDE=new String[1];
        
        rcAppAbweichendDE=new boolean[1];
        rcAppTextDE=new String[1];
        
        rcLetzteVersionEN=new int[1];
        
        rcPortalVonStandardVerwendenEN=new boolean[1];
        rcPortalTextEN=new String[1];
        
        rcPortalAdaptivAbweichendEN=new boolean[1];
        rcPortalAdaptivTextEN=new String[1];
        
        rcAppAbweichendEN=new boolean[1];
        rcAppTextEN=new String[1];

    }
    
    /**Liest die Portal-Texte für den Mandant lt. clGlobalVar ein, bereitet sie für die 
     * Bearbeitungauf und legt sie in rc* ab.
     * Return=1 => alles ok.
     * */
    public int lesePortalTexteFuerBearbeitung(DbBundle pDbBundle, boolean pMandantspezifisch, boolean pUebergreifend,
            int pRelease) {
         CaBug.druckeLog("pRelease=" + pRelease, logDrucken, 10);
        mandantspezifisch = pMandantspezifisch;
        uebergreifend = pUebergreifend;

        int anzahlPortalTexteMandant = 0;

        /*Deutsche Sprache übergreifend einlesen - anhand dessen alles initialisieren.
         * (anschließend dann Englisch und Mandanten einlesen)
         */
        pDbBundle.dbPortalTexte.read_all(1, false, false, pRelease);

        anzahlPortalTexteMandant = pDbBundle.dbPortalTexte.ergebnisMaxIdentGesamt() + 1;

        rcNummer=new int[anzahlPortalTexteMandant];
        
        rcSeitenName = new String[anzahlPortalTexteMandant];
        rcBeschreibung = new String[anzahlPortalTexteMandant];

        rcTextInPortal = new boolean[anzahlPortalTexteMandant];
        rcTextInApp = new boolean[anzahlPortalTexteMandant];

        rcVerbundenMitIdentGesamt = new int[anzahlPortalTexteMandant];
        rcSeitennummer = new int[anzahlPortalTexteMandant];
        rcIdent = new int[anzahlPortalTexteMandant];

        rcPortalStandardTextDE = new String[anzahlPortalTexteMandant];

        rcPortalStandardAdaptivAbweichendDE = new boolean[anzahlPortalTexteMandant];
        rcPortalStandardAdaptivTextDE = new String[anzahlPortalTexteMandant];

        rcAppStandardAbweichendDE = new boolean[anzahlPortalTexteMandant];
        rcAppStandardTextDE = new String[anzahlPortalTexteMandant];

        rcPortalStandardTextEN = new String[anzahlPortalTexteMandant];

        rcPortalStandardAdaptivAbweichendEN = new boolean[anzahlPortalTexteMandant];
        rcPortalStandardAdaptivTextEN = new String[anzahlPortalTexteMandant];

        rcAppStandardAbweichendEN = new boolean[anzahlPortalTexteMandant];
        rcAppStandardTextEN = new String[anzahlPortalTexteMandant];

        rcLetzteVersionDE = new int[anzahlPortalTexteMandant];
        rcPortalVonStandardVerwendenDE = new boolean[anzahlPortalTexteMandant];
        rcPortalTextDE = new String[anzahlPortalTexteMandant];
        rcPortalAdaptivAbweichendDE = new boolean[anzahlPortalTexteMandant];
        rcPortalAdaptivTextDE = new String[anzahlPortalTexteMandant];
        rcAppAbweichendDE = new boolean[anzahlPortalTexteMandant];
        rcAppTextDE = new String[anzahlPortalTexteMandant];

        rcLetzteVersionEN = new int[anzahlPortalTexteMandant];
        rcPortalVonStandardVerwendenEN = new boolean[anzahlPortalTexteMandant];
        rcPortalTextEN = new String[anzahlPortalTexteMandant];
        rcPortalAdaptivAbweichendEN = new boolean[anzahlPortalTexteMandant];
        rcPortalAdaptivTextEN = new String[anzahlPortalTexteMandant];
        rcAppAbweichendEN = new boolean[anzahlPortalTexteMandant];
        rcAppTextEN = new String[anzahlPortalTexteMandant];

        int nr = 0;
        String hTextPortal = "", hTextPortalAdaptivText = "", hAppText = "";
        for (int i = 0; i < pDbBundle.dbPortalTexte.anzErgebnis(); i++) {
            EclPortalText hEclPortalText = pDbBundle.dbPortalTexte.ergebnisPosition(i);
            int neueNummer = hEclPortalText.identGesamt;
            if (neueNummer != nr) {
                if (nr != 0) {
                    rcPortalStandardTextDE[nr] = hTextPortal;
                    rcPortalStandardAdaptivTextDE[nr] = hTextPortalAdaptivText;
                    rcAppStandardTextDE[nr] = hAppText;

                    /*Prophylaktisch alle anderen auf "" setzen*/
                    rcPortalStandardTextEN[nr] = "";
                    rcPortalStandardAdaptivTextEN[nr] = "";
                    rcAppStandardTextEN[nr] = "";

                    rcPortalTextDE[nr] = "";
                    rcPortalAdaptivTextDE[nr] = "";
                    rcAppTextDE[nr] = "";

                    rcPortalTextEN[nr] = "";
                    rcPortalAdaptivTextEN[nr] = "";
                    rcAppTextEN[nr] = "";

                }
                nr = neueNummer;
                hTextPortal = hEclPortalText.portalText;
                hTextPortalAdaptivText = hEclPortalText.portalAdaptivText;
                hAppText = hEclPortalText.appText;

                rcNummer[nr]=nr;
                
                rcSeitenName[nr] = hEclPortalText.seitenName;
                rcBeschreibung[nr] = hEclPortalText.beschreibung;
                rcTextInPortal[nr] = hEclPortalText.textInPortal;
                rcTextInApp[nr] = hEclPortalText.textInApp;

                rcVerbundenMitIdentGesamt[nr] = hEclPortalText.verbundenMitIdentGesamt;
                rcSeitennummer[nr] = hEclPortalText.seitennummer;
                rcIdent[nr] = hEclPortalText.ident;
                rcLetzteVersionDE[nr] = hEclPortalText.letzteVersion;
                rcPortalVonStandardVerwendenDE[nr] = true; //True, wenn nicht von mandanten-spezifischem Text überschrieben
                rcPortalStandardAdaptivAbweichendDE[nr] = hEclPortalText.portalAdaptivAbweichend;
                rcAppStandardAbweichendDE[nr] = hEclPortalText.appAbweichend;

            } else {
                hTextPortal += hEclPortalText.portalText;
                hTextPortalAdaptivText += hEclPortalText.portalAdaptivText;
                hAppText += hEclPortalText.appText;
            }
        }
        if (nr != 0) {
            rcPortalStandardTextDE[nr] = hTextPortal;
            rcPortalStandardAdaptivTextDE[nr] = hTextPortalAdaptivText;
            rcAppStandardTextDE[nr] = hAppText;

            /*Prophylaktisch alle anderen auf "" setzen*/
            rcPortalStandardTextEN[nr] = "";
            rcPortalStandardAdaptivTextEN[nr] = "";
            rcAppStandardTextEN[nr] = "";

            rcPortalTextDE[nr] = "";
            rcPortalAdaptivTextDE[nr] = "";
            rcAppTextDE[nr] = "";

            rcPortalTextEN[nr] = "";
            rcPortalAdaptivTextEN[nr] = "";
            rcAppTextEN[nr] = "";
        }

        /*Englische Texte dazulesen*/
        pDbBundle.dbPortalTexte.read_all(2, false, false, pRelease);
        nr = 0;
        hTextPortal = "";
        hTextPortalAdaptivText = "";
        hAppText = "";
        for (int i = 0; i < pDbBundle.dbPortalTexte.anzErgebnis(); i++) {
            EclPortalText hEclPortalText = pDbBundle.dbPortalTexte.ergebnisPosition(i);
            int neueNummer = hEclPortalText.identGesamt;
            if (neueNummer != nr) {
                if (nr != 0) {
                    rcPortalStandardTextEN[nr] = hTextPortal;
                    rcPortalStandardAdaptivTextEN[nr] = hTextPortalAdaptivText;
                    rcAppStandardTextEN[nr] = hAppText;
                }
                nr = neueNummer;
                hTextPortal = hEclPortalText.portalText;
                hTextPortalAdaptivText = hEclPortalText.portalAdaptivText;
                hAppText = hEclPortalText.appText;

                rcLetzteVersionEN[nr] = hEclPortalText.letzteVersion;
                rcPortalVonStandardVerwendenEN[nr] = true; //true, wenn nicht von mandanten-spezifischem Text überschrieben
                rcPortalStandardAdaptivAbweichendEN[nr] = hEclPortalText.portalAdaptivAbweichend;
                rcAppStandardAbweichendEN[nr] = hEclPortalText.appAbweichend;

            } else {
                hTextPortal += hEclPortalText.portalText;
                hTextPortalAdaptivText += hEclPortalText.portalAdaptivText;
                hAppText += hEclPortalText.appText;
            }
        }
        if (nr != 0) {
            rcPortalStandardTextEN[nr] = hTextPortal;
            rcPortalStandardAdaptivTextEN[nr] = hTextPortalAdaptivText;
            rcAppStandardTextEN[nr] = hAppText;
        }

        if (mandantspezifisch) {
            /*Mandant-Deutsche Texte dazulesen*/
            pDbBundle.dbPortalTexte.read_all(1, mandantspezifisch, uebergreifend, 0);
            nr = 0;
            hTextPortal = "";
            hTextPortalAdaptivText = "";
            hAppText = "";
            for (int i = 0; i < pDbBundle.dbPortalTexte.anzErgebnis(); i++) {
                EclPortalText hEclPortalText = pDbBundle.dbPortalTexte.ergebnisPosition(i);
                int neueNummer = hEclPortalText.identGesamt;
                if (neueNummer != nr) {
                    if (nr != 0) {
                        rcPortalTextDE[nr] = hTextPortal;
                        rcPortalAdaptivTextDE[nr] = hTextPortalAdaptivText;
                        rcAppTextDE[nr] = hAppText;
                    }
                    nr = neueNummer;
                    hTextPortal = hEclPortalText.portalText;
                    hTextPortalAdaptivText = hEclPortalText.portalAdaptivText;
                    hAppText = hEclPortalText.appText;

                    if (hEclPortalText.portalVonStandardVerwenden == false) {
                        rcPortalVonStandardVerwendenDE[nr] = false;
                    }

                    if (rcLetzteVersionDE[nr] < hEclPortalText.letzteVersion) {
                        rcLetzteVersionDE[nr] = hEclPortalText.letzteVersion;
                    }

                    rcPortalAdaptivAbweichendDE[nr] = hEclPortalText.portalAdaptivAbweichend;
                    rcAppAbweichendDE[nr] = hEclPortalText.appAbweichend;

                } else {
                    hTextPortal += hEclPortalText.portalText;
                    hTextPortalAdaptivText += hEclPortalText.portalAdaptivText;
                    hAppText += hEclPortalText.appText;
                }
            }
            if (nr != 0) {
                rcPortalTextDE[nr] = hTextPortal;
                rcPortalAdaptivTextDE[nr] = hTextPortalAdaptivText;
                rcAppTextDE[nr] = hAppText;
            }

            /*Standard-Englische Texte dazulesen*/
            pDbBundle.dbPortalTexte.read_all(2, mandantspezifisch, uebergreifend, 0);
            nr = 0;
            hTextPortal = "";
            hTextPortalAdaptivText = "";
            hAppText = "";
            for (int i = 0; i < pDbBundle.dbPortalTexte.anzErgebnis(); i++) {
                EclPortalText hEclPortalText = pDbBundle.dbPortalTexte.ergebnisPosition(i);
                int neueNummer = hEclPortalText.identGesamt;
                if (neueNummer != nr) {
                    if (nr != 0) {
                        rcPortalTextEN[nr] = hTextPortal;
                        rcPortalAdaptivTextEN[nr] = hTextPortalAdaptivText;
                        rcAppTextEN[nr] = hAppText;
                    }
                    nr = neueNummer;
                    hTextPortal = hEclPortalText.portalText;
                    hTextPortalAdaptivText = hEclPortalText.portalAdaptivText;
                    hAppText = hEclPortalText.appText;

                    if (hEclPortalText.portalVonStandardVerwenden == false) {
                        rcPortalVonStandardVerwendenEN[nr] = false;
                    }

                    if (rcLetzteVersionEN[nr] < hEclPortalText.letzteVersion) {
                        rcLetzteVersionEN[nr] = hEclPortalText.letzteVersion;
                    }

                    rcPortalAdaptivAbweichendEN[nr] = hEclPortalText.portalAdaptivAbweichend;
                    rcAppAbweichendEN[nr] = hEclPortalText.appAbweichend;

                } else {
                    hTextPortal += hEclPortalText.portalText;
                    hTextPortalAdaptivText += hEclPortalText.portalAdaptivText;
                    hAppText += hEclPortalText.appText;
                }
            }
            if (nr != 0) {
                rcPortalTextEN[nr] = hTextPortal;
                rcPortalAdaptivTextEN[nr] = hTextPortalAdaptivText;
                rcAppTextEN[nr] = hAppText;
            }
        }

        return 1;
    }

    
    
    /**Liest die Portal-Texte für den Mandant lt. clGlobalVar ein, bereitet sie für die 
     * Bearbeitungauf und legt sie in rc*[0] ab.
     * Return=1 => alles ok.
     * Return -1 => Nr. nicht gefunden
     * */
    public int leseEinzelnenPortalTextFuerBearbeitung(int pIdentGesamt, DbBundle pDbBundle, boolean pMandantspezifisch, boolean pUebergreifend,
            int pRelease) {
         CaBug.druckeLog("pRelease=" + pRelease, logDrucken, 10);
        mandantspezifisch = pMandantspezifisch;
        uebergreifend = pUebergreifend;

        int anzahlPortalTexteMandant = 0;

        /*Deutsche Sprache übergreifend einlesen - anhand dessen alles initialisieren.
         * (anschließend dann Englisch und Mandanten einlesen)
         */
        pDbBundle.dbPortalTexte.read_identGesamt(pIdentGesamt, 1, false, false, pRelease);
        if (pDbBundle.dbPortalTexte.anzErgebnis()<1) {return -1;}

        anzahlPortalTexteMandant = 1;

        rcNummer=new int[anzahlPortalTexteMandant];
        
        rcSeitenName = new String[anzahlPortalTexteMandant];
        rcBeschreibung = new String[anzahlPortalTexteMandant];

        rcTextInPortal = new boolean[anzahlPortalTexteMandant];
        rcTextInApp = new boolean[anzahlPortalTexteMandant];

        rcVerbundenMitIdentGesamt = new int[anzahlPortalTexteMandant];
        rcSeitennummer = new int[anzahlPortalTexteMandant];
        rcIdent = new int[anzahlPortalTexteMandant];

        rcPortalStandardTextDE = new String[anzahlPortalTexteMandant];

        rcPortalStandardAdaptivAbweichendDE = new boolean[anzahlPortalTexteMandant];
        rcPortalStandardAdaptivTextDE = new String[anzahlPortalTexteMandant];

        rcAppStandardAbweichendDE = new boolean[anzahlPortalTexteMandant];
        rcAppStandardTextDE = new String[anzahlPortalTexteMandant];

        rcPortalStandardTextEN = new String[anzahlPortalTexteMandant];

        rcPortalStandardAdaptivAbweichendEN = new boolean[anzahlPortalTexteMandant];
        rcPortalStandardAdaptivTextEN = new String[anzahlPortalTexteMandant];

        rcAppStandardAbweichendEN = new boolean[anzahlPortalTexteMandant];
        rcAppStandardTextEN = new String[anzahlPortalTexteMandant];

        rcLetzteVersionDE = new int[anzahlPortalTexteMandant];
        rcPortalVonStandardVerwendenDE = new boolean[anzahlPortalTexteMandant];
        rcPortalTextDE = new String[anzahlPortalTexteMandant];
        rcPortalAdaptivAbweichendDE = new boolean[anzahlPortalTexteMandant];
        rcPortalAdaptivTextDE = new String[anzahlPortalTexteMandant];
        rcAppAbweichendDE = new boolean[anzahlPortalTexteMandant];
        rcAppTextDE = new String[anzahlPortalTexteMandant];

        rcLetzteVersionEN = new int[anzahlPortalTexteMandant];
        rcPortalVonStandardVerwendenEN = new boolean[anzahlPortalTexteMandant];
        rcPortalTextEN = new String[anzahlPortalTexteMandant];
        rcPortalAdaptivAbweichendEN = new boolean[anzahlPortalTexteMandant];
        rcPortalAdaptivTextEN = new String[anzahlPortalTexteMandant];
        rcAppAbweichendEN = new boolean[anzahlPortalTexteMandant];
        rcAppTextEN = new String[anzahlPortalTexteMandant];

        int nr = 0;
        String hTextPortal = "", hTextPortalAdaptivText = "", hAppText = "";
        for (int i = 0; i < pDbBundle.dbPortalTexte.anzErgebnis(); i++) {
            EclPortalText hEclPortalText = pDbBundle.dbPortalTexte.ergebnisPosition(i);
            int neueNummer = hEclPortalText.identGesamt;
            if (neueNummer != nr) {
                nr = neueNummer;
                hTextPortal = hEclPortalText.portalText;
                hTextPortalAdaptivText = hEclPortalText.portalAdaptivText;
                hAppText = hEclPortalText.appText;

                rcNummer[0]=nr;
                
                rcSeitenName[0] = hEclPortalText.seitenName;
                rcBeschreibung[0] = hEclPortalText.beschreibung;
                rcTextInPortal[0] = hEclPortalText.textInPortal;
                rcTextInApp[0] = hEclPortalText.textInApp;

                rcVerbundenMitIdentGesamt[0] = hEclPortalText.verbundenMitIdentGesamt;
                rcSeitennummer[0] = hEclPortalText.seitennummer;
                rcIdent[0] = hEclPortalText.ident;
                rcLetzteVersionDE[0] = hEclPortalText.letzteVersion;
                rcPortalVonStandardVerwendenDE[0] = true; //True, wenn nicht von mandanten-spezifischem Text überschrieben
                rcPortalStandardAdaptivAbweichendDE[0] = hEclPortalText.portalAdaptivAbweichend;
                rcAppStandardAbweichendDE[0] = hEclPortalText.appAbweichend;

            } else {
                hTextPortal += hEclPortalText.portalText;
                hTextPortalAdaptivText += hEclPortalText.portalAdaptivText;
                hAppText += hEclPortalText.appText;
            }
        }
        if (nr != 0) {
            rcPortalStandardTextDE[0] = hTextPortal;
            rcPortalStandardAdaptivTextDE[0] = hTextPortalAdaptivText;
            rcAppStandardTextDE[0] = hAppText;

            /*Prophylaktisch alle anderen auf "" setzen*/
            rcPortalStandardTextEN[0] = "";
            rcPortalStandardAdaptivTextEN[0] = "";
            rcAppStandardTextEN[0] = "";

            rcPortalTextDE[0] = "";
            rcPortalAdaptivTextDE[0] = "";
            rcAppTextDE[0] = "";

            rcPortalTextEN[0] = "";
            rcPortalAdaptivTextEN[0] = "";
            rcAppTextEN[0] = "";
        }

        /*Englische Texte dazulesen*/
        pDbBundle.dbPortalTexte.read_identGesamt(pIdentGesamt, 2, false, false, pRelease);
        nr = 0;
        hTextPortal = "";
        hTextPortalAdaptivText = "";
        hAppText = "";
        for (int i = 0; i < pDbBundle.dbPortalTexte.anzErgebnis(); i++) {
            EclPortalText hEclPortalText = pDbBundle.dbPortalTexte.ergebnisPosition(i);
            int neueNummer = hEclPortalText.identGesamt;
            if (neueNummer != nr) {
                nr = neueNummer;
                hTextPortal = hEclPortalText.portalText;
                hTextPortalAdaptivText = hEclPortalText.portalAdaptivText;
                hAppText = hEclPortalText.appText;

                rcLetzteVersionEN[0] = hEclPortalText.letzteVersion;
                rcPortalVonStandardVerwendenEN[0] = true; //true, wenn nicht von mandanten-spezifischem Text überschrieben
                rcPortalStandardAdaptivAbweichendEN[0] = hEclPortalText.portalAdaptivAbweichend;
                rcAppStandardAbweichendEN[0] = hEclPortalText.appAbweichend;

            } else {
                hTextPortal += hEclPortalText.portalText;
                hTextPortalAdaptivText += hEclPortalText.portalAdaptivText;
                hAppText += hEclPortalText.appText;
            }
        }
        if (nr != 0) {
            rcPortalStandardTextEN[0] = hTextPortal;
            rcPortalStandardAdaptivTextEN[0] = hTextPortalAdaptivText;
            rcAppStandardTextEN[0] = hAppText;
        }

        if (mandantspezifisch) {
            /*Mandant-Deutsche Texte dazulesen*/
            pDbBundle.dbPortalTexte.read_identGesamt(pIdentGesamt, 1, mandantspezifisch, uebergreifend, 0);
            nr = 0;
            hTextPortal = "";
            hTextPortalAdaptivText = "";
            hAppText = "";
            for (int i = 0; i < pDbBundle.dbPortalTexte.anzErgebnis(); i++) {
                EclPortalText hEclPortalText = pDbBundle.dbPortalTexte.ergebnisPosition(i);
                int neueNummer = hEclPortalText.identGesamt;
                if (neueNummer != nr) {
                    nr = neueNummer;
                    hTextPortal = hEclPortalText.portalText;
                    hTextPortalAdaptivText = hEclPortalText.portalAdaptivText;
                    hAppText = hEclPortalText.appText;

                    if (hEclPortalText.portalVonStandardVerwenden == false) {
                        rcPortalVonStandardVerwendenDE[0] = false;
                    }

                    if (rcLetzteVersionDE[0] < hEclPortalText.letzteVersion) {
                        rcLetzteVersionDE[0] = hEclPortalText.letzteVersion;
                    }

                    rcPortalAdaptivAbweichendDE[0] = hEclPortalText.portalAdaptivAbweichend;
                    rcAppAbweichendDE[0] = hEclPortalText.appAbweichend;

                } else {
                    hTextPortal += hEclPortalText.portalText;
                    hTextPortalAdaptivText += hEclPortalText.portalAdaptivText;
                    hAppText += hEclPortalText.appText;
                }
            }
            if (nr != 0) {
                rcPortalTextDE[0] = hTextPortal;
                rcPortalAdaptivTextDE[0] = hTextPortalAdaptivText;
                rcAppTextDE[0] = hAppText;
            }

            /*Standard-Englische Texte dazulesen*/
            pDbBundle.dbPortalTexte.read_identGesamt(pIdentGesamt, 2, mandantspezifisch, uebergreifend, 0);
            nr = 0;
            hTextPortal = "";
            hTextPortalAdaptivText = "";
            hAppText = "";
            for (int i = 0; i < pDbBundle.dbPortalTexte.anzErgebnis(); i++) {
                EclPortalText hEclPortalText = pDbBundle.dbPortalTexte.ergebnisPosition(i);
                int neueNummer = hEclPortalText.identGesamt;
                if (neueNummer != nr) {
                    nr = neueNummer;
                    hTextPortal = hEclPortalText.portalText;
                    hTextPortalAdaptivText = hEclPortalText.portalAdaptivText;
                    hAppText = hEclPortalText.appText;

                    if (hEclPortalText.portalVonStandardVerwenden == false) {
                        rcPortalVonStandardVerwendenEN[0] = false;
                    }

                    if (rcLetzteVersionEN[0] < hEclPortalText.letzteVersion) {
                        rcLetzteVersionEN[0] = hEclPortalText.letzteVersion;
                    }

                    rcPortalAdaptivAbweichendEN[0] = hEclPortalText.portalAdaptivAbweichend;
                    rcAppAbweichendEN[0] = hEclPortalText.appAbweichend;

                } else {
                    hTextPortal += hEclPortalText.portalText;
                    hTextPortalAdaptivText += hEclPortalText.portalAdaptivText;
                    hAppText += hEclPortalText.appText;
                }
            }
            if (nr != 0) {
                rcPortalTextEN[0] = hTextPortal;
                rcPortalAdaptivTextEN[0] = hTextPortalAdaptivText;
                rcAppTextEN[0] = hAppText;
            }
        }

        return 1;
    }

    
    //	private EclPortalText altPortalText=null;
    /**Subroutine für schreibePortalTexte; einmal für pSprache=1 und einmal für =2 aufzurufen
     * Hinweis: beim Bearbeiten der übergreifenden Texte werden diese (von den übergreifenden Routinen)
     * in die Mandantenspezifischen Arrays gespeichert - deshalb beim Speichern immer diese verwenden*/
    private void schreibePortalTextSprache(DbBundle pDbBundle, int pNummer, int pSprache, int pBasisSet) {
        int hlfdNummer = 1;
        boolean weiterSpeichern = true; //1 Satz wird mindestens gespeichert; danach nur noch dann, wenn eines der Textfelder noch nicht leer ist
        String hText = "";
        String hPortalText = "";
        String hPortalAdaptivText = "";
        String hAppText = "";

        hlfdNummer = 1;
        weiterSpeichern = true;
        if (pSprache == 1) {
            hPortalText = rcPortalTextDE[pNummer];
            hPortalAdaptivText = rcPortalAdaptivTextDE[pNummer];
            hAppText = rcAppTextDE[pNummer];
        } else {
            hPortalText = rcPortalTextEN[pNummer];
            hPortalAdaptivText = rcPortalAdaptivTextEN[pNummer];
            hAppText = rcAppTextEN[pNummer];
        }
        if (hPortalText == null) {
            hPortalText = "";
        }
        if (hPortalAdaptivText == null) {
            hPortalAdaptivText = "";
        }
        if (hAppText == null) {
            hAppText = "";
        }

        while (weiterSpeichern) {
            weiterSpeichern = false; //Erstmal auf False setzen; wird wieder true, wenn ein Textbaustein zu lang ist
            EclPortalText lPortalText = new EclPortalText();

            lPortalText.mandant = pDbBundle.clGlobalVar.mandant;
            lPortalText.basisSet = pBasisSet;
            lPortalText.db_version = 0; //Derzeit keine Mehrbenutzerfähigkeit
            lPortalText.identGesamt = pNummer;
            lPortalText.lfdNummer = hlfdNummer;

            lPortalText.textInPortal = rcTextInPortal[pNummer];
            lPortalText.textInApp = rcTextInApp[pNummer];

            lPortalText.verbundenMitIdentGesamt = rcVerbundenMitIdentGesamt[pNummer];
            lPortalText.seitennummer = rcSeitennummer[pNummer];
            lPortalText.ident = rcIdent[pNummer];
            lPortalText.sprache = pSprache;
            lPortalText.seitenName = rcSeitenName[pNummer];
            lPortalText.beschreibung = rcBeschreibung[pNummer];
            lPortalText.letzteVersion = -1; //Veränderungen in App-Texte einfach auf -1 setzen; dann Freigabe mit anderer Funktion

            /*Portaltext*/
            if (pSprache == 1) {
                lPortalText.portalVonStandardVerwenden = rcPortalVonStandardVerwendenDE[pNummer];
            } else {
                lPortalText.portalVonStandardVerwenden = rcPortalVonStandardVerwendenEN[pNummer];
            }
            hText = hPortalText;
            if (hText.length() > 400) {
                weiterSpeichern = true;
                hText = hText.substring(0, 400);
                hPortalText = hPortalText.substring(400);
            } else {
                hPortalText = "";
            }
            lPortalText.portalText = hText;

            /*Adaptiv*/
            if (pSprache == 1) {
                lPortalText.portalAdaptivAbweichend = rcPortalAdaptivAbweichendDE[pNummer];
            } else {
                lPortalText.portalAdaptivAbweichend = rcPortalAdaptivAbweichendEN[pNummer];
            }
            hText = hPortalAdaptivText;
            if (hText.length() > 400) {
                weiterSpeichern = true;
                hText = hText.substring(0, 400);
                hPortalAdaptivText = hPortalAdaptivText.substring(400);
            } else {
                hPortalAdaptivText = "";
            }
            lPortalText.portalAdaptivText = hText;

            /*App-Text*/
            if (pSprache == 1) {
                lPortalText.appAbweichend = rcAppAbweichendDE[pNummer];
            } else {
                lPortalText.appAbweichend = rcAppAbweichendEN[pNummer];
            }
            hText = hAppText;
            if (hText.length() > 400) {
                weiterSpeichern = true;
                hText = hText.substring(0, 400);
                hAppText = hAppText.substring(400);
            } else {
                hAppText = "";
            }
            lPortalText.appText = hText;

            pDbBundle.dbPortalTexte.insert(lPortalText, mandantspezifisch, uebergreifend);

            hlfdNummer++;

        }

    }

    /**Schreibt den Text mit der Nummer pNummer Portal-Texte für den Mandant lt. clGlobalVar in die Datenbank.
     * Dabei wird auf die Variablen in rc* zurückgegriffen.
     * */
    public void schreibePortalTexte(DbBundle pDbBundle, int pNummer, boolean pMandantspezifisch, boolean pUebergreifend,
            int pBasisSet) {

        mandantspezifisch = pMandantspezifisch;
        uebergreifend = pUebergreifend;

        /*Portal-Text (Ist-Stand) mit laufender Nummer pNummer einlesen (wegen seitennummer und ident - diese stehen
         * nicht in rc* zur Verfügung
         */
        //		altPortalText=new EclPortalText();
        //		altPortalText.identGesamt=pNummer;
        //		altPortalText.sprache=1; //immer Deutsch verwenden zum Re-Lesen
        //		pDbBundle.dbPortalTexte.read(altPortalText, mandantspezifisch, uebergreifend);
        //		altPortalText=pDbBundle.dbPortalTexte.ergebnisPosition(0);

        /*Nun alte Portal-Texte löschen mit pNummer - alle Sprachen*/
        pDbBundle.dbPortalTexte.delete(pNummer, mandantspezifisch, uebergreifend, pBasisSet);

        /*Deutsch speichern*/
        schreibePortalTextSprache(pDbBundle, pNummer, 1, pBasisSet);

        /*Englisch speichern*/
        schreibePortalTextSprache(pDbBundle, pNummer, 2, pBasisSet);

        /*Texte auf Reload setzen*/
        BvReload lBvReload = new BvReload(pDbBundle);
        lBvReload.setReloadPortalAppTexte(pDbBundle.clGlobalVar.mandant);

    }

    /**Subroutine für schreibePortalTexte; einmal für pSprache=1 und einmal für =2 aufzurufen
     * Hinweis: beim Bearbeiten der übergreifenden Texte werden diese (von den übergreifenden Routinen)
     * in die Mandantenspezifischen Arrays gespeichert - deshalb beim Speichern immer diese verwenden*/
    private void schreibePortalTextSpracheEinzeln(DbBundle pDbBundle, int pNummer, int pSprache, int pBasisSet) {
        int hlfdNummer = 1;
        boolean weiterSpeichern = true; //1 Satz wird mindestens gespeichert; danach nur noch dann, wenn eines der Textfelder noch nicht leer ist
        String hText = "";
        String hPortalText = "";
        String hPortalAdaptivText = "";
        String hAppText = "";

        hlfdNummer = 1;
        weiterSpeichern = true;
        if (pSprache == 1) {
            hPortalText = rcPortalTextDE[0];
            hPortalAdaptivText = rcPortalAdaptivTextDE[0];
            hAppText = rcAppTextDE[0];
        } else {
            hPortalText = rcPortalTextEN[0];
            hPortalAdaptivText = rcPortalAdaptivTextEN[0];
            hAppText = rcAppTextEN[0];
        }
        if (hPortalText == null) {
            hPortalText = "";
        }
        if (hPortalAdaptivText == null) {
            hPortalAdaptivText = "";
        }
        if (hAppText == null) {
            hAppText = "";
        }

        while (weiterSpeichern) {
            weiterSpeichern = false; //Erstmal auf False setzen; wird wieder true, wenn ein Textbaustein zu lang ist
            EclPortalText lPortalText = new EclPortalText();

            lPortalText.mandant = pDbBundle.clGlobalVar.mandant;
            lPortalText.basisSet = pBasisSet;
            lPortalText.db_version = 0; //Derzeit keine Mehrbenutzerfähigkeit
            lPortalText.identGesamt = pNummer;
            lPortalText.lfdNummer = hlfdNummer;

            lPortalText.textInPortal = rcTextInPortal[0];
            lPortalText.textInApp = rcTextInApp[0];

            lPortalText.verbundenMitIdentGesamt = rcVerbundenMitIdentGesamt[0];
            lPortalText.seitennummer = rcSeitennummer[0];
            lPortalText.ident = rcIdent[0];
            lPortalText.sprache = pSprache;
            lPortalText.seitenName = rcSeitenName[0];
            lPortalText.beschreibung = rcBeschreibung[0];
            lPortalText.letzteVersion = -1; //Veränderungen in App-Texte einfach auf -1 setzen; dann Freigabe mit anderer Funktion

            /*Portaltext*/
            if (pSprache == 1) {
                lPortalText.portalVonStandardVerwenden = rcPortalVonStandardVerwendenDE[0];
            } else {
                lPortalText.portalVonStandardVerwenden = rcPortalVonStandardVerwendenEN[0];
            }
            hText = hPortalText;
            if (hText.length() > 400) {
                weiterSpeichern = true;
                hText = hText.substring(0, 400);
                hPortalText = hPortalText.substring(400);
            } else {
                hPortalText = "";
            }
            lPortalText.portalText = hText;

            /*Adaptiv*/
            if (pSprache == 1) {
                lPortalText.portalAdaptivAbweichend = rcPortalAdaptivAbweichendDE[0];
            } else {
                lPortalText.portalAdaptivAbweichend = rcPortalAdaptivAbweichendEN[0];
            }
            hText = hPortalAdaptivText;
            if (hText.length() > 400) {
                weiterSpeichern = true;
                hText = hText.substring(0, 400);
                hPortalAdaptivText = hPortalAdaptivText.substring(400);
            } else {
                hPortalAdaptivText = "";
            }
            lPortalText.portalAdaptivText = hText;

            /*App-Text*/
            if (pSprache == 1) {
                lPortalText.appAbweichend = rcAppAbweichendDE[0];
            } else {
                lPortalText.appAbweichend = rcAppAbweichendEN[0];
            }
            hText = hAppText;
            if (hText.length() > 400) {
                weiterSpeichern = true;
                hText = hText.substring(0, 400);
                hAppText = hAppText.substring(400);
            } else {
                hAppText = "";
            }
            lPortalText.appText = hText;

            pDbBundle.dbPortalTexte.insert(lPortalText, mandantspezifisch, uebergreifend);

            hlfdNummer++;

        }

    }

    
    /**Schreibt den Text mit der Nummer pNummer Portal-Texte für den Mandant lt. clGlobalVar in die Datenbank.
     * Dabei wird auf die Variablen in rc* zurückgegriffen, aber dort steht nur genau dieses Element.
     * */
    public void schreibePortalTexteEinzeln(DbBundle pDbBundle, int pNummer, boolean pMandantspezifisch, boolean pUebergreifend,
            int pBasisSet) {

        mandantspezifisch = pMandantspezifisch;
        uebergreifend = pUebergreifend;

        /*Portal-Text (Ist-Stand) mit laufender Nummer pNummer einlesen (wegen seitennummer und ident - diese stehen
         * nicht in rc* zur Verfügung
         */
        //      altPortalText=new EclPortalText();
        //      altPortalText.identGesamt=pNummer;
        //      altPortalText.sprache=1; //immer Deutsch verwenden zum Re-Lesen
        //      pDbBundle.dbPortalTexte.read(altPortalText, mandantspezifisch, uebergreifend);
        //      altPortalText=pDbBundle.dbPortalTexte.ergebnisPosition(0);

        /*Nun alte Portal-Texte löschen mit pNummer - alle Sprachen*/
        pDbBundle.dbPortalTexte.delete(pNummer, mandantspezifisch, uebergreifend, pBasisSet);

        /*Deutsch speichern*/
        schreibePortalTextSpracheEinzeln(pDbBundle, pNummer, 1, pBasisSet);

        /*Englisch speichern*/
        schreibePortalTextSpracheEinzeln(pDbBundle, pNummer, 2, pBasisSet);

        /*Texte auf Reload setzen*/
        BvReload lBvReload = new BvReload(pDbBundle);
        lBvReload.setReloadPortalAppTexte(pDbBundle.clGlobalVar.mandant);

    }

    
    /*************Umbauen der Texte in das AppTexte-Table******************************
     * pInitial
     * 		=true: alle Texte werden übernommen
     * 		=false: nur die Texte werden übernommen, die als "App-Update" -1 haben
     * 
     * pAuchStandard
     * 		=true: es werden auch die Standardtexte (komplett) übertragen, so sie in diesem
     * 				Mandant verwendet werden.
     * 				Problematik: bei Standardtexten kann nicht automatisch ermittelt werden,
     * 				welche sich geändert haben. D.h. bei einer Standard-Text-Änderung müssen diese
     * 				alle (manuell) an alle mandanten übertragen werden.
     * 
     * pUebergreifendeTexteUebertragen
     * 		=true: es werden die Mandanten-abhängigen Standard Texte übertragen (unabhängig davon,
     * 				ob die betreffenden Textnummern die Version -1 haben oder nicht)
     * 
     * return=1 => ok, sonst Fehler
     * */
    public int freigebenTexteFuerApp(DbBundle pDbBundle, boolean pInitial, boolean pAuchStandard,
            boolean pUebergreifendeTexteUebertragen) {
        int rc;

        /*Texte einlesen - in Tabelle zum Bearbeiten*/
        if (pUebergreifendeTexteUebertragen == false) {
            rc = lesePortalTexteFuerBearbeitung(pDbBundle, true, true,
                    pDbBundle.param.paramPortal.basisSetStandardTexteVerwenden);
            CaBug.druckeLog("pUebergreifendeTexteUebertragen==false", logDrucken, 10);
            CaBug.druckeLog("pDbBundle.param.paramPortal.basisSetStandardTexteVerwenden="
                    + pDbBundle.param.paramPortal.basisSetStandardTexteVerwenden, logDrucken, 10);
        } else {
            rc = lesePortalTexteFuerBearbeitung(pDbBundle, false, false,
                    pDbBundle.param.paramPortal.basisSetStandardTexteVerwenden);
            CaBug.druckeLog("pUebergreifendeTexteUebertragen==true", logDrucken, 10);
            CaBug.druckeLog("pDbBundle.param.paramPortal.basisSetStandardTexteVerwenden="
                    + pDbBundle.param.paramPortal.basisSetStandardTexteVerwenden, logDrucken, 10);
        }
        if (rc < 1) {
            return rc;
        }

        /*Standard-Texte dort, wo sie verwendet werden, übertragen. Ggf.
         * Update setzen, wenn AuchStandard==true.
         * Andernfalls auch die "normalen" Texte in App-Texte übertragen, falls
         * diese verwendet werden. 
         */
        for (int i = 0; i < rcPortalStandardTextDE.length; i++) {
            if (rcTextInApp[i]) {
                /*Deutsch*/
                if (rcPortalVonStandardVerwendenDE[i]) {
                    if (pDbBundle.param.paramPortal.standardTexteBeruecksichtigen == 1) {
                        /*Ggf. Standard-Texte übertragen*/
                        rcPortalTextDE[i] = rcPortalStandardTextDE[i];
                        if (rcPortalStandardAdaptivAbweichendDE[i]) {
                            rcPortalAdaptivTextDE[i] = rcPortalStandardAdaptivTextDE[i];
                        } else {
                            rcPortalAdaptivTextDE[i] = rcPortalStandardTextDE[i];
                        }
                        if (rcAppStandardAbweichendDE[i]) {
                            rcAppTextDE[i] = rcAppStandardTextDE[i];
                        } else {
                            rcAppTextDE[i] = rcPortalStandardTextDE[i];
                        }
                        if (pAuchStandard) {/*Dann App-Texte immer übertragen, unabhängig von letzter Änderung*/
                            rcLetzteVersionDE[i] = -1;
                        }
                    }
                } else {
                    if (rcAppAbweichendDE[i] == false) {
                        rcAppTextDE[i] = rcPortalTextDE[i];
                    }
                }

                /*Englisch*/
                if (rcPortalVonStandardVerwendenEN[i]) {
                    if (pDbBundle.param.paramPortal.standardTexteBeruecksichtigen == 1) {
                        /*Ggf. Standard-Texte übertragen*/
                        rcPortalTextEN[i] = rcPortalStandardTextEN[i];
                        if (rcPortalStandardAdaptivAbweichendEN[i]) {
                            rcPortalAdaptivTextEN[i] = rcPortalStandardAdaptivTextEN[i];
                        } else {
                            rcPortalAdaptivTextEN[i] = rcPortalStandardTextEN[i];
                        }
                        if (rcAppStandardAbweichendEN[i]) {
                            rcAppTextEN[i] = rcAppStandardTextEN[i];
                        } else {
                            rcAppTextEN[i] = rcPortalStandardTextEN[i];
                        }
                        if (pAuchStandard) {/*Dann App-Texte immer übertragen, unabhängig von letzter Änderung*/
                            rcLetzteVersionEN[i] = -1;
                        }
                    }
                } else {
                    if (rcAppAbweichendEN[i] == false) {
                        rcAppTextEN[i] = rcPortalTextEN[i];
                    }
                }
            }
        }

        /*Nächste App-Version vergeben*/

        int naechsteAppVersion = 0;
        if (pUebergreifendeTexteUebertragen) {
            naechsteAppVersion = pDbBundle.dbBasis.getAppVersionUebergreifend();
        } else {
            naechsteAppVersion = pDbBundle.dbBasis.getAppVersionMandant();
        }

        /*Nun alle Texte durcharbeiten und übertragen, die als Version -1 haben*/
        for (int i = 0; i < rcPortalStandardTextDE.length; i++) {
            if (rcTextInApp[i]) {
                if (rcLetzteVersionDE[i] == -1 || pInitial) {/*Deutschen Text zur App übertragen*/
                    speichereTextApp(pDbBundle, rcAppTextDE[i], rcSeitennummer[i], rcIdent[i], 1, naechsteAppVersion,
                            pUebergreifendeTexteUebertragen);
                    /*Nun Version von "-1"en auf die App-Version setzen*/
                    pDbBundle.dbPortalTexte.updateAppVersion(i, 1, naechsteAppVersion, mandantspezifisch,
                            pUebergreifendeTexteUebertragen);
                }
                if (rcLetzteVersionEN[i] == -1 || pInitial) {/*Englischen Text zur App übertragen*/
                    speichereTextApp(pDbBundle, rcAppTextEN[i], rcSeitennummer[i], rcIdent[i], 2, naechsteAppVersion,
                            pUebergreifendeTexteUebertragen);
                    /*Nun Version von "-1"en auf die App-Version setzen*/
                    pDbBundle.dbPortalTexte.updateAppVersion(i, 2, naechsteAppVersion, mandantspezifisch,
                            pUebergreifendeTexteUebertragen);
                }
            }

        }
        return 1;
    }

    private boolean sp_formatAktivBold = false;
    private boolean sp_formatAktivItalic = false;
    private boolean sp_formatAktivMedium = false;
    /**Handhabung sp_newline und sp_letzteZeileMitNewlineAbgeschlossen:
     * sp_newline: wird auf true gesetzt, wenn im durchzuarbeitenden Text "<<<NL>>>" gefunden
     * wird, anschließend wird "sp_speichere" aufgerufen, der bisherige Text auf jeden Fall
     * mit NL-Formatierung abgeschlossen gespeichert, und dann wieder auf false gesetzt.
     */
    private boolean sp_newline = false;
    private boolean sp_letzteZeileMitNewlineAbgeschlossen = false;

    private String sp_arbeitsText = null;
    private int sp_appSeitennummer = 0;
    private int sp_appIdent = 0;
    private int sp_sprache = 0;
    private int sp_appversion = 0;
    private int sp_lfdNummer = 0;

    private boolean sp_fortsetzen = true;
    private int sp_offset = 0;
    private int sp_mandant = 0;
    private DbBundle sp_dbBundle = null;

    private void speichereTextApp(DbBundle pDbBundle, String pAppText, int pAppSeitennummer, int pAppIdent,
            int pSprache, int pAppVersion, boolean pUebergreifendeTexteUebertragen) {
        sp_formatAktivBold = false;
        sp_formatAktivItalic = false;
        sp_formatAktivMedium = false;
        sp_newline = false;
        sp_letzteZeileMitNewlineAbgeschlossen = false;

        sp_arbeitsText = pAppText;
        sp_appSeitennummer = pAppSeitennummer;
        sp_appIdent = pAppIdent;
        sp_sprache = pSprache;
        sp_appversion = pAppVersion;
        sp_lfdNummer = 1;

        sp_fortsetzen = true;
        sp_offset = 0;

        sp_dbBundle = pDbBundle;

        /*Entsprechenden Text aus App-Texte löschen*/
        sp_mandant = 0;
        if (pUebergreifendeTexteUebertragen == false) {
            sp_mandant = pDbBundle.clGlobalVar.mandant;
        }
        pDbBundle.dbAppTexte.delete(sp_sprache, sp_appSeitennummer, sp_appIdent, sp_mandant);

        /**in sp_arbeitsText die in der App "verbotenen" Zeichen rausbauen, also konkret: \n*/
        sp_arbeitsText = sp_arbeitsText.replaceAll("\n", "");
        sp_arbeitsText = sp_arbeitsText.replaceAll("\r", "");

        while (sp_fortsetzen) {
            sp_arbeitsschritt();
        }

    }

    private void sp_arbeitsschritt() {
        int gefAnfang = 0;
        int gefEnde = 0;

        /*Text leer?*/
        if (sp_arbeitsText.length() == 0) {
            sp_fortsetzen = false;
            return;
        }

        /*<<< suchen*/
        gefAnfang = sp_arbeitsText.indexOf("<<<", sp_offset);
        if (gefAnfang == -1) {
            /*Wenn nicht gefunden, dann restlichen Text "ggf. gesplittet" speichern - Ende*/
            sp_speichere(sp_arbeitsText);
            sp_arbeitsText = "";
            sp_fortsetzen = false;
            return;
        }
        /*Zugehöriges >>> suchen*/
        gefEnde = sp_arbeitsText.indexOf(">>>", sp_offset);
        if (gefEnde == -1) {
            /*Wenn nicht gefunden - Fehler, dann restlichen Text "ggf. gesplittet" speichern - Ende*/
            CaBug.drucke("BvTexte.sp_arbeitsschritt 001 - >>> nicht gefunden");
            sp_speichere(sp_arbeitsText);
            sp_arbeitsText = "";
            sp_fortsetzen = false;
            return;
        }

        String steuerzeichen = "";
        steuerzeichen = sp_arbeitsText.substring(gefAnfang + 3, gefEnde);
        if (steuerzeichen.compareTo("NL") == 0) {
            /*Wenn NL, dann bisherigen Text speichern - mit gesetzter Formatierung - dann weiter*/
            sp_newline = true;
            String hSpeichereText = sp_arbeitsText.substring(0, gefAnfang);
            sp_speichere(hSpeichereText);
            sp_arbeitsText = sp_arbeitsText.substring(gefEnde + 3);
            sp_offset = 0;
            sp_newline = false;
            return;
        }
        /*B /B, I /I, U /U H1 /H1 bis H4 /H4 - wenn was anderes, dann weitersuchen. (Hinweis: U wird wie I behandelt, da von App nicht unterstützt*/
        if (steuerzeichen.compareTo("B") != 0 && steuerzeichen.compareTo("/B") != 0 && steuerzeichen.compareTo("I") != 0
                && steuerzeichen.compareTo("/I") != 0 && steuerzeichen.compareTo("U") != 0
                && steuerzeichen.compareTo("/U") != 0 && steuerzeichen.compareTo("H1") != 0
                && steuerzeichen.compareTo("/H1") != 0 && steuerzeichen.compareTo("H2") != 0
                && steuerzeichen.compareTo("/H2") != 0 && steuerzeichen.compareTo("H3") != 0
                && steuerzeichen.compareTo("/H3") != 0 && steuerzeichen.compareTo("H4") != 0
                && steuerzeichen.compareTo("/H4") != 0) {
            sp_offset = gefEnde + 3;
            return;
        }
        /*Nun in jedem Fall Formatierungswechsel*/
        /* Bisherigen Text mit bisheriger Formatierung abspeichern*/
        String hSpeichereText = sp_arbeitsText.substring(0, gefAnfang);
        sp_speichere(hSpeichereText);
        sp_arbeitsText = sp_arbeitsText.substring(gefEnde + 3);
        sp_offset = 0;
        /*Formatierung ändern*/
        if (steuerzeichen.compareTo("B") == 0) {
            sp_formatAktivBold = true;
        }
        if (steuerzeichen.compareTo("/B") == 0) {
            sp_formatAktivBold = false;
        }
        if (steuerzeichen.compareTo("I") == 0) {
            sp_formatAktivItalic = true;
        }
        if (steuerzeichen.compareTo("/I") == 0) {
            sp_formatAktivItalic = false;
        }
        if (steuerzeichen.compareTo("U") == 0) {
            sp_formatAktivItalic = true;
        }
        if (steuerzeichen.compareTo("/U") == 0) {
            sp_formatAktivItalic = false;
        }
        if (steuerzeichen.compareTo("H1") == 0) {
            sp_formatAktivMedium = true;
        }
        if (steuerzeichen.compareTo("/H1") == 0) {
            sp_formatAktivMedium = false;
        }
        if (steuerzeichen.compareTo("H2") == 0) {
            sp_formatAktivMedium = true;
        }
        if (steuerzeichen.compareTo("/H2") == 0) {
            sp_formatAktivMedium = false;
        }
        if (steuerzeichen.compareTo("H3") == 0) {
            sp_formatAktivMedium = true;
        }
        if (steuerzeichen.compareTo("/H3") == 0) {
            sp_formatAktivMedium = false;
        }
        if (steuerzeichen.compareTo("H4") == 0) {
            sp_formatAktivMedium = true;
        }
        if (steuerzeichen.compareTo("/H4") == 0) {
            sp_formatAktivMedium = false;
        }
    }

    private void sp_speichere(String pSpeichereText) {
        boolean weiterSpeichern = true; //1 Satz wird mindestens gespeichert; danach nur noch dann, wenn eines der Textfelder noch nicht leer ist
        String hText = "";
        String hAppText = "";

        weiterSpeichern = true;
        hAppText = pSpeichereText;
        if (hAppText == null) {
            hAppText = "";
        }
        if (sp_newline && hAppText.isEmpty()) {
            /*Prüfen, ob zwei mal Newline hintereinander*/
            if (sp_letzteZeileMitNewlineAbgeschlossen) {
                sp_newline = false;
            }
        }
        sp_letzteZeileMitNewlineAbgeschlossen = false;

        while (weiterSpeichern) {
            weiterSpeichern = false; //Erstmal auf False setzen; wird wieder true, wenn ein Textbaustein zu lang ist
            EclAppTexte lApplText = new EclAppTexte();

            lApplText.mandant = sp_mandant;
            lApplText.sprache = sp_sprache;
            lApplText.seitennummer = sp_appSeitennummer;
            lApplText.ident = sp_appIdent;
            lApplText.lfdNummer = sp_lfdNummer;
            lApplText.letzteVersion = sp_appversion;
            int lFormatierung = 0;
            if (sp_formatAktivBold) {
                lFormatierung = (lFormatierung | 2);
            }
            if (sp_formatAktivItalic) {
                lFormatierung = (lFormatierung | 4);
            }
            if (sp_formatAktivMedium) {
                lFormatierung = (lFormatierung | 16);
            }
            /*NL nur am Ende setzen*/

            /*anzeigetext*/
            hText = hAppText;
            if (hText.length() > 400) {
                weiterSpeichern = true;
                hText = hText.substring(0, 400);
                hAppText = hAppText.substring(400);
            } else {
                hAppText = "";
            }
            lApplText.anzeigetext = hText;
            if (weiterSpeichern == false) {
                /*Dann ggf. noch NL anhängen*/
                if (sp_newline) {
                    lFormatierung = (lFormatierung | 1);
                }
            }

            /*Nun prüfen, ob zwei mal NL hintereinander - nur wenn nicht dann auch speichern*/
            hText = lApplText.anzeigetext.trim();

            if (!hText.isEmpty() || sp_newline == true) {
                /*Speichern ist immer erforderlich:
                 * > wenn der zu speichernde Text nicht leer ist
                 * > wenn sp_newline=true ist (unabhängig davon, ob der zu speichernde Text leer oder voll ist)
                 */
                lApplText.formatierung = lFormatierung;
                sp_dbBundle.dbAppTexte.insert(lApplText);
            }

            if (sp_newline) {
                sp_letzteZeileMitNewlineAbgeschlossen = true;
            }

            sp_lfdNummer++;
        }
    }

    /**Aktuell werden die Fehler des aktuellen Mandanten übertragen - pUebergreifendeTexteUebertragen derzeit also noch
     * nicht implementiert
     */
    public int freigebenFehlertexteFuerApp(DbBundle pDbBundle, boolean pUebergreifendeTexteUebertragen) {
        /*Nächste Text-App-Version ermitteln und in sp_appversion ablegen*/
        if (pUebergreifendeTexteUebertragen) {
            sp_appversion = pDbBundle.dbBasis.getAppVersionUebergreifend();
        } else {
            sp_appversion = pDbBundle.dbBasis.getAppVersionMandant();
        }

        /*Deutsch*/
        pDbBundle.dbFehler.read_allNurApp("DE");
        EclFehler[] lFehlerArray = pDbBundle.dbFehler.ergebnisArray;
        int anzFehler = pDbBundle.dbFehler.anzErgebnis();
        if (anzFehler > 0) {
            sp_fehlertext(pDbBundle, lFehlerArray, pUebergreifendeTexteUebertragen);
        }

        /*Englisch*/
        pDbBundle.dbFehler.read_allNurApp("EN");
        lFehlerArray = pDbBundle.dbFehler.ergebnisArray;
        anzFehler = pDbBundle.dbFehler.anzErgebnis();
        if (anzFehler > 0) {
            sp_fehlertext(pDbBundle, lFehlerArray, pUebergreifendeTexteUebertragen);
        }

        return 1;

    }

    private int sp_fehlertext(DbBundle pDbBundle, EclFehler[] pFehlerArray, boolean pUebergreifendeTexteUebertragen) {
        for (int i = 0; i < pFehlerArray.length; i++) {

            if (pFehlerArray[i].sprache == 0) {
                pFehlerArray[i].sprache = 1;
            }
            pDbBundle.dbAppTexte.delete(pFehlerArray[i].sprache, 0, pFehlerArray[i].ident,
                    pDbBundle.clGlobalVar.mandant);

            EclAppTexte lApplText = new EclAppTexte();

            lApplText.mandant = pDbBundle.clGlobalVar.mandant;
            lApplText.sprache = pFehlerArray[i].sprache;
            if (lApplText.sprache == 0) {
                lApplText.sprache = 1;
            }
            lApplText.seitennummer = 0;
            lApplText.ident = pFehlerArray[i].ident;
            lApplText.lfdNummer = 1;
            lApplText.letzteVersion = sp_appversion;
            lApplText.formatierung = 0;
            lApplText.anzeigetext = pFehlerArray[i].fehlermeldung.trim();

            CaBug.druckeLog("BvTexte.sp_fehlertext()", logDrucken, 10);
            CaBug.druckeLog("lApplText.ident=" + lApplText.ident, logDrucken, 10);
            CaBug.druckeLog("lApplText.mandant=" + lApplText.mandant, logDrucken, 10);
            CaBug.druckeLog("lApplText.sprache=" + lApplText.sprache, logDrucken, 10);
            CaBug.druckeLog("anzeigetext.sprache=" + lApplText.anzeigetext, logDrucken, 10);
            pDbBundle.dbAppTexte.insert(lApplText);
        }
        return 1;
    }
    
    /**************************************Vergleich / Abgleich basis-Sets******************************************************/
    public String rcDateinameVergleichsergebnis="";
    
    public int vergleicheSets(DbBundle pDbBundle, int pAusgangsset, int pVergleichsset, boolean pSeiteBezeichnungUebernehmen, boolean pFehlendeEintraegeUebernehmen) {
        
        lesePortalTexteFuerBearbeitung(pDbBundle, false, false, pAusgangsset);
        int[] lAusgangssetNummer=rcNummer;
        String[] lAusgangssetSeitenName = rcSeitenName;
        String[] lAusgangssetBeschreibung = rcBeschreibung;

        boolean[] lAusgangssetTextInPortal = rcTextInPortal;
        boolean[] lAusgangssetTextInApp = rcTextInApp;

        int[] lAusgangssetVerbundenMitIdentGesamt = rcVerbundenMitIdentGesamt;

        int[] lAusgangssetSeitennummer = rcSeitennummer;
        int[] lAusgangssetIdent = rcIdent;

        String[] lAusgangssetPortalStandardTextDE = rcPortalStandardTextDE;

        boolean[] lAusgangssetPortalStandardAdaptivAbweichendDE = rcPortalStandardAdaptivAbweichendDE;
        String[] lAusgangssetPortalStandardAdaptivTextDE = rcPortalStandardAdaptivTextDE;

        boolean[] lAusgangssetAppStandardAbweichendDE = rcAppStandardAbweichendDE;
        String[] lAusgangssetAppStandardTextDE = rcAppStandardTextDE;

        /*EN*/
        String[] lAusgangssetPortalStandardTextEN = rcPortalStandardTextEN;

        boolean[] lAusgangssetPortalStandardAdaptivAbweichendEN = rcPortalStandardAdaptivAbweichendEN;
        String[] lAusgangssetPortalStandardAdaptivTextEN = rcPortalStandardAdaptivTextEN;

        boolean[] lAusgangssetAppStandardAbweichendEN = rcAppStandardAbweichendEN;
        String[] lAusgangssetAppStandardTextEN = rcAppStandardTextEN;

        int[] lAusgangssetLetzteVersionDE = rcLetzteVersionDE;

        boolean[] lAusgangssetPortalVonStandardVerwendenDE = rcPortalVonStandardVerwendenDE;
//        String[] lAusgangssetPortalTextDE = rcPortalTextDE;

//        boolean[] lAusgangssetPortalAdaptivAbweichendDE = rcPortalAdaptivAbweichendDE;
//        String[] lAusgangssetPortalAdaptivTextDE = rcPortalAdaptivTextDE;

//        boolean[] lAusgangssetAppAbweichendDE = rcAppAbweichendDE;
//        String[] lAusgangssetAppTextDE = rcAppTextDE;

        int[] lAusgangssetLetzteVersionEN = rcLetzteVersionEN;

        boolean[] lAusgangssetPortalVonStandardVerwendenEN = rcPortalVonStandardVerwendenEN;
//        String[] lAusgangssetPortalTextEN = rcPortalTextEN;

//        boolean[] lAusgangssetPortalAdaptivAbweichendEN = rcPortalAdaptivAbweichendEN;
//        String[] lAusgangssetPortalAdaptivTextEN = rcPortalAdaptivTextEN;

//        boolean[] lAusgangssetAppAbweichendEN = rcAppAbweichendEN;
//        String[] lAusgangssetAppTextEN = rcAppTextEN;

        
        lesePortalTexteFuerBearbeitung(pDbBundle, false, false, pVergleichsset);
        /*Verarbeitungshinweis: in rc* bleibt das Vergleichsset enthalten und kann
         * damit für das Zurückschreiben von aktualisierten Textbausteinen
         * verwendet werden
         */
        int[] lVergleichssetNummer=rcNummer;
        String[] lVergleichssetSeitenName = rcSeitenName;
        String[] lVergleichssetBeschreibung = rcBeschreibung;

//        boolean[] lVergleichssetTextInPortal = rcTextInPortal;
//        boolean[] lVergleichssetTextInApp = rcTextInApp;

//        int[] lVergleichssetVerbundenMitIdentGesamt = rcVerbundenMitIdentGesamt;

//        int[] lVergleichssetSeitennummer = rcSeitennummer;
//        int[] lVergleichssetIdent = rcIdent;

        String[] lVergleichssetPortalStandardTextDE = rcPortalStandardTextDE;

//        boolean[] lVergleichssetPortalStandardAdaptivAbweichendDE = rcPortalStandardAdaptivAbweichendDE;
//        String[] lVergleichssetPortalStandardAdaptivTextDE = rcPortalStandardAdaptivTextDE;

//        boolean[] lVergleichssetAppStandardAbweichendDE = rcAppStandardAbweichendDE;
//        String[] lVergleichssetAppStandardTextDE = rcAppStandardTextDE;

        /*EN*/
        String[] lVergleichssetPortalStandardTextEN = rcPortalStandardTextEN;

//        boolean[] lVergleichssetPortalStandardAdaptivAbweichendEN = rcPortalStandardAdaptivAbweichendEN;
//        String[] lVergleichssetPortalStandardAdaptivTextEN = rcPortalStandardAdaptivTextEN;

//        boolean[] lVergleichssetAppStandardAbweichendEN = rcAppStandardAbweichendEN;
//        String[] lVergleichssetAppStandardTextEN = rcAppStandardTextEN;

//        int[] lVergleichssetLetzteVersionDE = rcLetzteVersionDE;

//        boolean[] lVergleichssetPortalVonStandardVerwendenDE = rcPortalVonStandardVerwendenDE;
//        String[] lVergleichssetPortalTextDE = rcPortalTextDE;

//        boolean[] lVergleichssetPortalAdaptivAbweichendDE = rcPortalAdaptivAbweichendDE;
//        String[] lVergleichssetPortalAdaptivTextDE = rcPortalAdaptivTextDE;

//        boolean[] lVergleichssetAppAbweichendDE = rcAppAbweichendDE;
//        String[] lVergleichssetAppTextDE = rcAppTextDE;

//        int[] lVergleichssetLetzteVersionEN = rcLetzteVersionEN;

//        boolean[] lVergleichssetPortalVonStandardVerwendenEN = rcPortalVonStandardVerwendenEN;
//        String[] lVergleichssetPortalTextEN = rcPortalTextEN;

//        boolean[] lVergleichssetPortalAdaptivAbweichendEN = rcPortalAdaptivAbweichendEN;
//        String[] lVergleichssetPortalAdaptivTextEN = rcPortalAdaptivTextEN;

//        boolean[] lVergleichssetAppAbweichendEN = rcAppAbweichendEN;
//        String[] lVergleichssetAppTextEN = rcAppTextEN;
      
        CaDateiWrite dateiExport = null;
        dateiExport = new CaDateiWrite();
        dateiExport.trennzeichen = ';';
        dateiExport.dateiart = ".csv";

        dateiExport.oeffne(pDbBundle, "abgleichBasisset");

        dateiExport.ausgabe("Basis-Set");
        dateiExport.ausgabe("Text-Nr");
        dateiExport.ausgabe("Sprache");
        dateiExport.ausgabe("Abweichung");
        dateiExport.ausgabe("Seitenname");
        dateiExport.ausgabe("Bezeichnung");
        dateiExport.ausgabe("Text");
        dateiExport.newline();
 
        int lVergleichssetLaenge=lVergleichssetSeitenName.length-1; //0 nicht belegt
        CaBug.druckeLog("lVergleichssetLaenge="+lVergleichssetLaenge, logDrucken, 10);
        for (int i=1;i<lAusgangssetSeitenName.length;i++) {
            CaBug.druckeLog("i="+i, logDrucken,10);
            boolean ausgabeErfolgt=false;
            if (lVergleichssetLaenge<i) {
                /*Zum Vergleich: Basis hat 5 Elemente damit i=1 bis 4
                 * Vergleichsset hat Länge 4
                 */
                
                /*Texte überhaupt nicht vorhanden*/
                dateiExport.ausgabe(Integer.toString(pAusgangsset));
                dateiExport.ausgabe(Integer.toString(i));
                dateiExport.ausgabe("");
                dateiExport.ausgabe("Textelement fehlt komplett in Vergleichsset");
                dateiExport.ausgabe(lAusgangssetSeitenName[i]);
                dateiExport.ausgabe(lAusgangssetBeschreibung[i]);
                String hString=CaString.entferneSteuerzeichenKomplett(lAusgangssetPortalStandardTextDE[i]);
                dateiExport.ausgabe(hString);
                dateiExport.newline();
                ausgabeErfolgt=true;
                if (pFehlendeEintraegeUebernehmen) {
                    initRCLaengeEins();
                    rcVerbundenMitIdentGesamt[0]=lAusgangssetVerbundenMitIdentGesamt[i];
                    rcSeitennummer[0]=lAusgangssetSeitennummer[i];
                    rcIdent[0]=lAusgangssetIdent[i];
                    rcTextInPortal[0]=lAusgangssetTextInPortal[i];
                    rcTextInApp[0]=lAusgangssetTextInApp[i];
                    
                    rcPortalVonStandardVerwendenDE[0]=lAusgangssetPortalVonStandardVerwendenDE[i];
                    rcPortalTextDE[0]=lAusgangssetPortalStandardTextDE[i];
                    
                    rcPortalAdaptivAbweichendDE[0]=lAusgangssetPortalStandardAdaptivAbweichendDE[i];
                    rcPortalAdaptivTextDE[0]=lAusgangssetPortalStandardAdaptivTextDE[i];
                    rcAppAbweichendDE[0]=lAusgangssetAppStandardAbweichendDE[i];
                    rcAppTextDE[0]=lAusgangssetAppStandardTextDE[i];
                    
                    rcPortalVonStandardVerwendenEN[0]=lAusgangssetPortalVonStandardVerwendenEN[i];
                    rcPortalTextEN[0]=lAusgangssetPortalStandardTextEN[i];
                    
                    rcPortalAdaptivAbweichendEN[0]=lAusgangssetPortalStandardAdaptivAbweichendEN[i];
                    rcPortalAdaptivTextEN[0]=lAusgangssetPortalStandardAdaptivTextEN[i];
                    rcAppAbweichendEN[0]=lAusgangssetAppStandardAbweichendEN[i];
                    rcAppTextEN[0]=lAusgangssetAppStandardTextEN[i];
                    
                    rcSeitenName[0]=lAusgangssetSeitenName[i];
                    rcBeschreibung[0]=lAusgangssetBeschreibung[i];
                    
//                    rcPortalStandardTextDE[0]=lAusgangssetPortalStandardTextDE[i];
//                    rcPortalStandardAdaptivTextDE[0]=lAusgangssetPortalStandardAdaptivTextDE[i];
//                    rcPortalStandardTextEN[0]=lAusgangssetPortalStandardTextEN[i];
//                    rcPortalStandardAdaptivTextEN[0]=lAusgangssetPortalStandardAdaptivTextEN[i];
                    
                    rcAppStandardTextEN[0]=lAusgangssetAppStandardTextEN[i];
                    rcLetzteVersionDE[0]=lAusgangssetLetzteVersionDE[i];
                    rcLetzteVersionEN[0]=lAusgangssetLetzteVersionEN[i];
                    schreibePortalTexteEinzeln(pDbBundle, i, false, false, pVergleichsset);
               }
            }
            else {
                if (lAusgangssetNummer[i]!=i) {
                    dateiExport.ausgabe(Integer.toString(pAusgangsset));
                    dateiExport.ausgabe(Integer.toString(i));
                    dateiExport.ausgabe("");
                    dateiExport.ausgabe("Textelement fehlt komplett in Ausgangsset");
                    dateiExport.ausgabe("");
                    dateiExport.ausgabe("");
                    dateiExport.ausgabe("");
                    dateiExport.newline();
                    ausgabeErfolgt=true;
                    if (lVergleichssetNummer[i]!=i) {
                        dateiExport.ausgabe(Integer.toString(pVergleichsset));
                        dateiExport.ausgabe(Integer.toString(i));
                        dateiExport.ausgabe("");
                        dateiExport.ausgabe("Textelement fehlt komplett in Vergleichsset");
                        dateiExport.ausgabe("");
                        dateiExport.ausgabe("");
                        dateiExport.ausgabe("");
                        dateiExport.newline();
                        ausgabeErfolgt=true;
                    }
                }
                else {
                    boolean vergleichssetAktualisieren=false;
                    String hString="";
                    if (!lAusgangssetSeitenName[i].equals(lVergleichssetSeitenName[i])) {
                        /*Seitenname weicht ab*/
                        dateiExport.ausgabe(Integer.toString(pAusgangsset));
                        dateiExport.ausgabe(Integer.toString(i));
                        dateiExport.ausgabe("");
                        dateiExport.ausgabe("Seitenname weicht ab");
                        dateiExport.ausgabe(lAusgangssetSeitenName[i]);
                        dateiExport.ausgabe(lAusgangssetBeschreibung[i]);
                        hString=CaString.entferneSteuerzeichenKomplett(lAusgangssetPortalStandardTextDE[i]);
                        dateiExport.ausgabe(hString);
                        dateiExport.newline();

                        dateiExport.ausgabe(Integer.toString(pVergleichsset));
                        dateiExport.ausgabe(Integer.toString(i));
                        dateiExport.ausgabe("");
                        dateiExport.ausgabe("Seitenname weicht ab");
                        dateiExport.ausgabe(lVergleichssetSeitenName[i]);
                        dateiExport.ausgabe(lVergleichssetBeschreibung[i]);
                        hString=CaString.entferneSteuerzeichenKomplett(lVergleichssetPortalStandardTextDE[i]);
                        dateiExport.ausgabe(hString);
                        dateiExport.newline();
                        ausgabeErfolgt=true;
                        if (pSeiteBezeichnungUebernehmen) {
                            rcSeitenName[i]=lAusgangssetSeitenName[i];
                             vergleichssetAktualisieren=true;
                        }
                    }
                    if (!lAusgangssetBeschreibung[i].equals(lVergleichssetBeschreibung[i])) {
                        /*Seitenname weicht ab*/
                        dateiExport.ausgabe(Integer.toString(pAusgangsset));
                        dateiExport.ausgabe(Integer.toString(i));
                        dateiExport.ausgabe("");
                        dateiExport.ausgabe("Bezeichnung weicht ab");
                        dateiExport.ausgabe(lAusgangssetSeitenName[i]);
                        dateiExport.ausgabe(lAusgangssetBeschreibung[i]);
                        hString=CaString.entferneSteuerzeichenKomplett(lAusgangssetPortalStandardTextDE[i]);
                        dateiExport.ausgabe(hString);
                        dateiExport.newline();

                        dateiExport.ausgabe(Integer.toString(pVergleichsset));
                        dateiExport.ausgabe(Integer.toString(i));
                        dateiExport.ausgabe("");
                        dateiExport.ausgabe("Bezeichnung weicht ab");
                        dateiExport.ausgabe(lVergleichssetSeitenName[i]);
                        dateiExport.ausgabe(lVergleichssetBeschreibung[i]);
                        hString=CaString.entferneSteuerzeichenKomplett(lVergleichssetPortalStandardTextDE[i]);
                        dateiExport.ausgabe(hString);
                        dateiExport.newline();
                        ausgabeErfolgt=true;
                        if (pSeiteBezeichnungUebernehmen) {
                            rcBeschreibung[i]=lAusgangssetBeschreibung[i];
                            vergleichssetAktualisieren=true;
                        }
                     }


                    if (!lAusgangssetPortalStandardTextDE[i].equals(lVergleichssetPortalStandardTextDE[i])) {
                        /*Text DE weicht ab*/
                        dateiExport.ausgabe(Integer.toString(pAusgangsset));
                        dateiExport.ausgabe(Integer.toString(i));
                        dateiExport.ausgabe("");
                        dateiExport.ausgabe("Text DE weicht ab");
                        dateiExport.ausgabe(lAusgangssetSeitenName[i]);
                        dateiExport.ausgabe(lAusgangssetBeschreibung[i]);
                        hString=CaString.entferneSteuerzeichenKomplett(lAusgangssetPortalStandardTextDE[i]);
                        dateiExport.ausgabe(hString);
                        dateiExport.newline();

                        dateiExport.ausgabe(Integer.toString(pVergleichsset));
                        dateiExport.ausgabe(Integer.toString(i));
                        dateiExport.ausgabe("");
                        dateiExport.ausgabe("Text DE weicht ab");
                        dateiExport.ausgabe(lVergleichssetSeitenName[i]);
                        dateiExport.ausgabe(lVergleichssetBeschreibung[i]);
                        hString=CaString.entferneSteuerzeichenKomplett(lVergleichssetPortalStandardTextDE[i]);
                        dateiExport.ausgabe(hString);
                        dateiExport.newline();
                        ausgabeErfolgt=true;
                   }
                    if (!lAusgangssetPortalStandardTextEN[i].equals(lVergleichssetPortalStandardTextEN[i])) {
                        /*Text EN weicht ab*/
                        dateiExport.ausgabe(Integer.toString(pAusgangsset));
                        dateiExport.ausgabe(Integer.toString(i));
                        dateiExport.ausgabe("");
                        dateiExport.ausgabe("Text EN weicht ab");
                        dateiExport.ausgabe(lAusgangssetSeitenName[i]);
                        dateiExport.ausgabe(lAusgangssetBeschreibung[i]);
                        hString=CaString.entferneSteuerzeichenKomplett(lAusgangssetPortalStandardTextEN[i]);
                        dateiExport.ausgabe(hString);
                        dateiExport.newline();

                        dateiExport.ausgabe(Integer.toString(pVergleichsset));
                        dateiExport.ausgabe(Integer.toString(i));
                        dateiExport.ausgabe("");
                        dateiExport.ausgabe("Text EN weicht ab");
                        dateiExport.ausgabe(lVergleichssetSeitenName[i]);
                        dateiExport.ausgabe(lVergleichssetBeschreibung[i]);
                        hString=CaString.entferneSteuerzeichenKomplett(lVergleichssetPortalStandardTextEN[i]);
                        dateiExport.ausgabe(hString);
                        dateiExport.newline();
                        ausgabeErfolgt=true;
                   }
                    if (vergleichssetAktualisieren) {
                        rcPortalTextDE[i]=rcPortalStandardTextDE[i];
                        
                        rcPortalAdaptivAbweichendDE[0]=rcPortalStandardAdaptivAbweichendDE[i];
                        rcPortalAdaptivTextDE[i]=rcPortalStandardAdaptivTextDE[i];
                        rcAppAbweichendDE[0]=rcAppStandardAbweichendDE[i];
                        rcAppTextDE[i]=rcAppStandardTextDE[i];

                        rcPortalTextEN[i]=rcPortalStandardTextEN[i];
                        
                        rcPortalAdaptivAbweichendEN[0]=rcPortalStandardAdaptivAbweichendEN[i];
                        rcPortalAdaptivTextEN[i]=rcPortalStandardAdaptivTextEN[i];
                        rcAppAbweichendEN[0]=rcAppStandardAbweichendEN[i];
                        rcAppTextEN[i]=rcAppStandardTextEN[i];
 
                       schreibePortalTexte(pDbBundle, i, false, false, pVergleichsset);
                    }
                }

            }
            if (ausgabeErfolgt) {
                /*Zusätzliche Trennzeile ausgeben*/
                dateiExport.newline();
            }
        }
        
        dateiExport.schliessen();
        rcDateinameVergleichsergebnis = dateiExport.dateiname;

        return 1;
        
    }
    
    /*************************************Export Texte******************************************************/
    
    public int exportTexte(DbBundle pDbBundle, boolean pMandantentexte) {
        
        int basisSet=pDbBundle.param.paramPortal.basisSetStandardTexteVerwenden;
        lesePortalTexteFuerBearbeitung(pDbBundle, true, false, basisSet);
       
        CaDateiWrite dateiExport = null;
        dateiExport = new CaDateiWrite();
        dateiExport.trennzeichen = ';';
        dateiExport.dateiart = ".csv";

        dateiExport.oeffne(pDbBundle, "abgleichBasisset");

        dateiExport.ausgabe("Basis-Set");
        dateiExport.ausgabe("Text-Nr");
        dateiExport.ausgabe("Sprache");
        dateiExport.ausgabe("Seitenname");
        dateiExport.ausgabe("Bezeichnung");
        dateiExport.ausgabe("Text");
        dateiExport.newline();
 
        for (int i=1;i<rcSeitenName.length;i++) {
            CaBug.druckeLog("i="+i, logDrucken,10);
            String hString="";
            if (pMandantentexte==false) {
                dateiExport.ausgabe(Integer.toString(basisSet));
                dateiExport.ausgabe(Integer.toString(i));
                dateiExport.ausgabe("DE");
                dateiExport.ausgabe(rcSeitenName[i]);
                dateiExport.ausgabe(rcBeschreibung[i]);
                hString=CaString.entferneSteuerzeichenKomplett(rcPortalStandardTextDE[i]);
                dateiExport.ausgabe(hString);
                dateiExport.newline();

                dateiExport.ausgabe(Integer.toString(basisSet));
                dateiExport.ausgabe(Integer.toString(i));
                dateiExport.ausgabe("EN");
                dateiExport.ausgabe(rcSeitenName[i]);
                dateiExport.ausgabe(rcBeschreibung[i]);
                hString=CaString.entferneSteuerzeichenKomplett(rcPortalStandardTextEN[i]);
                dateiExport.ausgabe(hString);
                dateiExport.newline();
            }
            else {
                if (rcPortalVonStandardVerwendenDE[i]==false) {
                    dateiExport.ausgabe(Integer.toString(basisSet));
                    dateiExport.ausgabe(Integer.toString(i));
                    dateiExport.ausgabe("DE");
                    dateiExport.ausgabe(rcSeitenName[i]);
                    dateiExport.ausgabe(rcBeschreibung[i]);
                    hString=CaString.entferneSteuerzeichenKomplett(rcPortalTextDE[i]);
                    dateiExport.ausgabe(hString);
                    dateiExport.newline();
                }

                if (rcPortalVonStandardVerwendenEN[i]==false) {
                    dateiExport.ausgabe(Integer.toString(basisSet));
                    dateiExport.ausgabe(Integer.toString(i));
                    dateiExport.ausgabe("EN");
                    dateiExport.ausgabe(rcSeitenName[i]);
                    dateiExport.ausgabe(rcBeschreibung[i]);
                    hString=CaString.entferneSteuerzeichenKomplett(rcPortalTextEN[i]);
                    dateiExport.ausgabe(hString);
                    dateiExport.newline();
                }
            }
         }
        
        dateiExport.schliessen();
        rcDateinameVergleichsergebnis = dateiExport.dateiname;

        return 1;
        
    }

}
