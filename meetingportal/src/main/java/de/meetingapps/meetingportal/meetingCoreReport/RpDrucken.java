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
package de.meetingapps.meetingportal.meetingCoreReport;

import de.meetingapps.meetingportal.meetComAllg.CaDatumZeit;
import de.meetingapps.meetingportal.meetComDb.DbBundle;

public class RpDrucken {

    /*******************************Aufrufmuster*********************************
    "Server", d.h. Datei wird in PDF erzeugt:
    -----------------------------------------
    Im Dialog ("Server", d.h. Datei wird in PDF erzeugt,:
    	RpDrucken rpDrucken=new RpDrucken();
    	rpDrucken.initServer();
    Im Report dann z.B. ("Formular" ggf. durch "Liste" ersetzen):
    	rpDrucken.initFormular(pDbBundle);
    	
    	RpVariablen rpVariablen=new RpVariablen(pDbBundle);
    	rpVariablen.statistikMeldung(pLfdNummer, rpDrucken);
    	rpDrucken.startFormular();
    	.....
    	rpDrucken.druckenFormular();
    	rpDrucken.endeFormular();
    
    
    
    
    
    
    
    *****************************************************************************/

    private int logDrucken=10;
    
    public int nLLJob_ = -1;
    public int hWnd_ = 0;

    private StringBuffer bufferPrinter = new StringBuffer();
    private StringBuffer bufferPort = new StringBuffer();

    /*************************************Eingabe-Werte**************************************************/

    /**Steuerung des Export-Formats. Muß belegt werden, bevor initFormular aufgerufen wird.
     * -1= Kein Export, normaler Druck
     * 0 = Standard PDF, ablegen in: pDbBundle.clGlobalVar.lwPfadAllgemein+"\\meetingausdruckeintern\\"+pDbBundle.getMandantPfad()+"\\, 
     * 			Dateiname: "reportM"+pDbBundle.getMandantString()+drucklaufnummer+".pdf"
     * 1 = PDF, ablegen in exportVerzeichnis/exportDatei (wie in Variablen übergeben)
     * 2 = RTF-Datei, ablegen in exportVerzeichnis/exportDatei
     * 3 = Powerpoint-Datei, ablegen in exportVerzeichnis/exportDatei
     * 4 = XLS-Datei, ablegen in exportVerzeichnis/exportDatei
     * 5 = DOC-Datei, ablegen in exportVerzeichnis/exportDatei
     * 6 = PDF-Datei in Kundenordner, d.h.:
     * 		ablegen in: pDbBundle.clGlobalVar.lwPfadKundenordnerBasis+pDbBundle.eclEmittent.pfadErgKundenOrdner
     * 		Dateiname: exportDatei+".PDF"
     * 7 = PDF-Datei in meetingoutput, d.h. ablegen in:
     * 			pDbBundle.clGlobalVar.lwPfadAllgemein+"\\meetingoutput\\"+pDbBundle.getMandantPfad()+"\\, 
     * 			Dateiname: exportDatei+".pdf"
     * 8 = PDF-Datei in meetingausdrucke, d.h. ablegen in:
     * 			pDbBundle.clGlobalVar.lwPfadAllgemein+"\\meetingausdrucke\\"+pDbBundle.getMandantPfad()+"\\, 
     * 			Dateiname: exportDatei+".pdf"
     */
    public int exportFormat = -1;
    /**Muß Laufwerk mit erhalten! Form z.B.: D:\testverzeichnis\*/

    /**Falls export-Format!=-1: wenn exportAnzeigen==true, dann wird die erzeugte Datei
     * anschließend angezeigt. Darf nicht auf Server verwendet werden!!!
     */
    public boolean exportAnzeigen = false;

    /**=true => ein Fortschrittsbalken wird automatisch angezeigt.
     * Muß bei Server auf false stehen!
     */
    public boolean druckbalkenAnzeigen = false;

    /**Falls export-Format!=-1: wenn exportDateinameAbfragen==true, dann wird 
     * der Dateiname zum Überschreiben angeboten. Muß auf Server auf false gesetzt werden!
     */
    public boolean exportDateinameAbfragen = false;

    /**Falls export_format==-1: Druckerabfrage durchführen
     * Muß bei Server auf false stehen!*/
    public boolean druckerAbfragen = false;

    /**ohne pdf, rtf etc. Zusatz*/
    public String exportVerzeichnis = "";

    /**Ohne Endung .pdf oder so!*/
    public String exportDatei = "";

    /**-1 => keine Aktion
     * 1 => Druckkonfiguration für spätere Verwendung speichern
     * 2 => Druckkonfiguration wiederherstellen. Falls gewählt, sollte/muß
     * 		druckerAbfragen auf false gestellt werden.
     * Wird in startFormular / startListe "gehandled". D.h. zur Definition: 
     * start und ende aufrufen, mit entsprechender Vorbelegung, aber zwischendurch nix drucken.
     */
    public int druckerWiederverwendet = -1;
    /**Zu druckerWiederverwendet: Übergabe der Druckernummer, die verwendet werden soll. Damit können
     * mehrere unterschiedliche Drucker (z.B. Bühnendrucker, lokaler Drucker) gespeichert und restored
     * werden
     * Nummer 999=reserviert für interne Zwecke (d.h. wenn über einen Vorgang mehrere Ausdrucke
     * gestartet werden)
     */
    public int druckerWiederverwendetNummer = 1;

    /**Wird i.d.R. von rpVariablen gefüllt - vollständiger Pfad und Dateiname 
     * der Definition des Formulars/der Liste*/
    public StringBuffer dateinameLLQuelle = null;

    /**************************Return-Werte**************************/
    /**Mögliche Werte:
     * initFormular:
     * rpJobCantBeInitialized, rpLanguageFileNotFound
     * 
     * startFormular:
     * rpErrorWhilePrinting
     * rpAbbruchBeiAuswahl
     */
    public int rcFehler = 0;
    /**Emthält den LL-Fehler-Code, der in einigen Fällen geliefert wird*/
    public int rcFehlerLLCode = 0;

    /**Hier wird der komplette Pfad-und-Dateiname zurückgelegt, wenn Export gewählt*/
    public String drucklaufDatei = "";

    /**Für exportFormat==0. Wird automatisch generiert. Wenn Datenbank zur Verfügung steht, dann
     * über dbBasis.getInterneIdentDrucklauf. Ansonsten über datum/Zeit*/
    public String drucklaufnummer = "";

    /***********************Interne Variablen****************************/
    private DbBundle dbBundle = null;

    /**Kann auch "bewußt" verwendet werden, z.B. um PDF mit besonderem Namen aber eindeutiger Ident
     * zu erzeugen
     */
    public void holeDrucklaufNummer() {
        if (dbBundle.dbBasis != null) {
            drucklaufnummer = Integer.toString(dbBundle.dbBasis.getInterneIdentDrucklauf());
        } else {
            drucklaufnummer = CaDatumZeit.DatumZeitStringFuerDateiname();
        }
    }

    /**Standard-Initialisierungsroutine für Server:
     * > Erzeugen eines PDFs im Standard-Verzeichnis
     * > keinerlei Bildschirmanzeigen
     */
    public void initServer() {
        exportFormat = 0;
        exportAnzeigen = false;
        druckbalkenAnzeigen = false;
        exportDateinameAbfragen = false;
        druckerAbfragen = false;
    }

    /**Initialisierungsroutine für Client:
     * > Erzeugen eines PDFs im Standard-Verzeichnis
     * > keinerlei weiteren Bildschirmanzeigen
     */
    public void initClientStandardPDF() {
        exportFormat = 0;
        exportAnzeigen = false;
        druckbalkenAnzeigen = true;
        exportDateinameAbfragen = false;
        druckerAbfragen = false;
    }

    /**Aufruf nach initClientDrucke()
     * 
     * pDruckerNr gemäß KonstAutoDrucker*/
    public void setzeAutoDrucker(int pDruckerNr) {
        
        druckerWiederverwendet = 2;
        druckerWiederverwendetNummer = pDruckerNr;

        druckerAbfragen=false;
    }
    
    
    /**Initialisierungsroutine für Client:
     * > Abfragen des Druckers
     * > Direktdruck
     */
    public void initClientDrucke() {
        exportFormat = -1;
        exportAnzeigen = false;
        druckbalkenAnzeigen = true;
        exportDateinameAbfragen = false;
        druckerAbfragen = true;
    }

    public boolean initFormular(DbBundle pDbBundle) {
        return initFormularListe(pDbBundle, 1);
    }

    /**formularListe =1 => formular, 2=Liste
     * 
     * Derzeit kein Unterschied zwischen Formular und Liste!
     * */
    private boolean initFormularListe(DbBundle pDbBundle, int formularListe) {
        // add here your preferred DokumentGenerator
        return true;
    }

    /**Aufrufen nach dem Einlesen / Initialisieren der Variablen und des ll-Dokuments*/
    public boolean startFormular() {
        return startFormularListe(1);
    }

    private boolean startFormularListe(int formularListe) {
        // add here your preferred DokumentGenerator
        return true;
    }

    public boolean druckenFormular() {
        // add here your preferred DokumentGenerator
        return true;
    }

    /**Wird aufgerufen nach jedem "Satz" (also z.B. nach jedem Aktionär), der mit einem Formular
     * gedruckt wurde.
     * Dabei wird das Projekt "zurückgesetzt", d.h. insbesondere Seitennumerierung, Summenvariablen etc.
     * wieder auf 0 gesetzt. Damit auch 2-seitige Formulare machbar.
     * 
     * Achtung!!! Das Zurücksetzen betrifft auch alle Variablen - es muß also für 
     * jeden aufgerufenen Datensatz wieder 
     * rpVariablen.fuelleAllgemeineVariablen(rpDrucken)
     * aufgerufen werden!
     */
    public void druckenFormularSatzwechsel() {
        // add here your preferred DokumentGenerator
    }
    
    
    public boolean endeFormular() {
        // add here your preferred DokumentGenerator
        return true;
    }

    public boolean initListe(DbBundle pDbBundle) {
        return initFormularListe(pDbBundle, 2);
    }

    /**Aufrufen nach dem Einlesen / Initialisieren der Variablen und des ll-Dokuments*/
    public boolean startListe() {
        return startFormularListe(2);
    }

    public void newPageListe() {
        // add here your preferred DokumentGenerator
    }

    public boolean druckenListe() {
        // add here your preferred DokumentGenerator
        return true;
    }

    public boolean endeListe() {
        // add here your preferred DokumentGenerator
        return true;
    }

}
