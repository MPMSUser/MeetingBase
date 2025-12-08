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

/********************Beschreibung - Geräteabhängige Parameter*****************************
 * Geräteklasse (EclGeraeteKlasse)
 * - enthält eine Zusammenstellung aller geräteabhängigen Parameter
 * - mandantenunabhängig
 * 
 * Set (EclGeraeteSet)
 * - enthält ein "HV-Set" bestehend aus Zuordnungen zwischen Arbeitsplatznummern und Geräteklassen (EclGeraetKlasseSetZuordnung)
 * - mandantenunabhängig
 * 
 * Geräteabhängige Parameter (siehe unten) (EclGeraeteParameter)
 * - je Geräteklasse
 * - mandantenunabhändig
 * 
 *
 *D.h.:
 *Mandant => Geräteset
 *Gerätenummer und EclGeraetKlasseSetZuordnung => Geräteklasse
 *Geräteklasse => geräteabhängige Parameter
 */

public class ParamGeraet implements Serializable {
    private static final long serialVersionUID = 2353092163535201588L;

    /**Siehe ParamServer - wird daraus übernommen*/
    /**40 Stellen
     * Soll zukünftig auch dafür dienen, zu Speichern welcher Server gerade der "Master" eines Mandanten ist.*/
    public String serverBezeichnung = "";

    /**Siehe ParamServer - wird daraus übernommen*/
    /**Serverart (siehe auch KonstServerart):
     * 1 = Produktionsserver Portal (ExternerHoster)
     * 2 = Einrichtungsserver ("Office")
     * 3 = HV-Server
     * 4 = Einzel-Rechner
     */
    public int serverArt = 4;

    /**Siehe EclGerateKlasse*/
    public int identKlasse = 0;

    /**Siehe EclGerateKlasse*/
    public String beschreibungKlasse = "";

    public boolean akkreditierungVertreterErfassungAktiv = true; //cbVertreterErfassungAktiv
    public boolean akkreditierungScanFeldFuerBestaetigungAktiv = true;
    public boolean akkreditierungAnzeigeBelegeBuendeln = true;
    public boolean akkreditierungSammelkartenBuchenMoeglich = true;
    public boolean akkreditierungLabeldruckFuerAktionaer = true; //cbAkkreditierungLabeldruckFuerAktionaer

    /**true => für dieses Gerät werden Delay-Einstellungen immer ignoriert, d.h. alle Buchungen werden immer direkt ausgeführt*/
    public boolean akkreditierungDelayIgnorieren = false;

    /**Im Front-Office-Tool wird ein Suchbutton eingeblendet, mit dem anstelle der EK / SK gesucht werden kann.
     * Suchfunktions-Ergebnis löst Versuch einer Erstzugangsbuchung aus (liefert also die EK-Nummer zurück)*/
    public boolean akkreditierungSuchfunktionAktiv=false; //cbAkkreditierungSuchfunktionAktiv
    
    /**true => das Tablet arbeite bei der Abstimmungsmaske (und nur bei dieser) im Hochformat*/
    public boolean abstimmungTabletHochformat = true; //cbAbstimmungTabletHochformat

    /**1 = erstes Table (wie schon 2017)
     * 
     */
    public int abstimmungTabletTyp = 1;

    /**1 => Die Tablets dieser Geräte werden persönlich einem Teilnehmer zugeordnet. D.h. immer wieder Abstimmungsvorgang möglich,
     * aber nur mit diesem zugeordneten Teilnehmer (der auch angezeigt wird).
     * Bei jedem Abstimmungsvorgang wird aktuelle Agenda neu geholt.
     */
    public boolean abstimmungTabletPersoenlichZugeordnet=false; //cbAbstimmungTabletPersoenlichZugeordnet
    
    /*XXX*/
    public boolean abstimmungTabletTestmodus=false; //49 cbAbstimmungTabletTestmodus
    
    public boolean abtimmungTabletFullScreen=false; //50 cbAbtimmungTabletFullScreen
    
    public String abstimmungTabletXSize=""; //51 tfAbstimmungTabletXSize
    public String abstimmungTabletYSize=""; //52 tfAbstimmungTabletYSize
    
    /**Bei einer Tabletabstimmung wird ggf. überprüft, inwieweit die eingelesene Stimmkartennummer
     * tatsächlich aktiv ist.
     */
    public boolean nurGueltigeStimmkartenNummerBeiTabletAbstimmung = false;

    /**Anzahl Einträge, ab denen ein Protokoll beim Buchen geschlossen wird*/
    public int protokollAnzMaxZugaenge = 50;
    public int protokollAnzMax = 150;

    public boolean akkreditierungShortcutsAktiv = true;

    public boolean vertreterKorrekturBeiKontrollerfassungMoeglich = false;

    /********Für Auswahl bei Start*************/
    /**Mandant, der zwangsweise zu verwenden ist. 0 = freie Auswahl*/
    public int festgelegterMandant = 0;

    /**=false => Emittent kann verändert werden, auch wenn festgelegterMandant>0*/
    public boolean festgelegterMandantIstFix = true;

    /**Mandanten-Text - wird beigesteuert aus Emittenten-Table*/
    public String festgelegterMandantText = "";

    /**HV-Jahr, das vorgeschlagen wird. 0 = kein Angebot*/
    public int festgelegtesJahr = 0;

    /**=false => Jahr kann verändert werden, auch wenn festgelegtesJahr>0*/
    public boolean festgelegtesJahrIstFix = true;

    /**HV-Nummer, die vorgeschlagen wird. '' = kein Angebot*/
    public String festgelegteHVNummer = "";

    /**=false => HVNummer kann verändert werden, auch wenn festgelegteHVNummer!=""*/
    public boolean festgelegteHVNummerFix = true;

    /**Datenbank, die vorgeschlagen wird. '' = kein Angebot*/
    public String festgelegteDatenbank = "";

    /**=false => HVNummer kann verändert werden, auch wenn festgelegteDatenbank!=""*/
    public boolean festgelegteDatenbankFix = true;

    /**Benutzername, mit dem "Auto-Login" erfolgt. Sinnvoll zu verwenden z.B. für Front-Office-Geräte.
     * ="" => Auswahl*/
    public String festgelegterBenutzername = "";

    /**=false => Benutzername kann verändert werden, auch wenn festgelegterBenutzername!=""*/
    public boolean festgelegteBenutzernameFix = true;

    /************Laufwerke*****************************/

    /**Laufwerk und Pfad für Dokumente, Ausgabe, Zwischenablage - d.h. für meetingreports, meetingoutput, meetingausdrucke, meetingausdruckeintern*/
    public String lwPfadAllgemein = "";

    /**Laufwerk und Pfad für "Groß-Dokumente" (Videos etc., die nicht gesichert werden müssen)*/
    public String lwPfadGrossdokumente = ""; // tfLwPfadGrossdokumente

    /**Laufwerk und Pfad für Sicherungsdatei 1 für Abstimmungsgeräte. ="" => es wird keine Sicherungsdatei 1 erzeugt*/
    public String lwPfadSicherung1 = "";

    /**Laufwerk und Pfad für Sicherungsdatei 2 für Abstimmungsgeräte. ="" => es wird keine Sicherungsdatei 2 erzeugt*/
    public String lwPfadSicherung2 = "";

    /**Laufwerk und Pfad für Präsentations-Powerpoint/PDF für Abstimmung und Präsenzliste*/
    public String lwPfadExportFuerPraesentation = "";

    /**Laufwerk und Pfad für Bühnensystem für Abstimmung und Präsenzliste*/
    public String lwPfadExportFuerBuehnensystem = "";

    /**Laufwerk und Pfad für Export Excel für Powerpoint für Abstimmung und Präsenzliste*/
    public String lwPfadExportExcelFuerPowerpoint = "";

    /**Laufwerk und Pfad für Kundenordner (Basis); wird dann ergänzt um Kundenspezifischen Teil aus Emittent*/
    public String lwPfadKundenordnerBasis = "";

    /**********Druckerzuordnung************************/

    public boolean bonDruckerIstZugeordnet = false;

    public String labelDruckerIPAdresse = "";

    /********Login-Kontrollscreen anzeigen*************/
    public boolean programmStartKontrollScreenAnzeigen = true;

    /***********Kommunikation***********************/
    /**Falls sowohl Web-Services als auch Datenbank-Verbindung konfiguriert sind, dann gilt vorrangig Datenbank, wenn true*/
    public boolean dbVorrangig = false;

    /************Zulässige Module*************************/
    public boolean moduleHVMaster = true;
    public boolean moduleFrontOffice = true;
    public boolean moduleKontrolle = true;
    public boolean moduleServiceDesk = true;
    public boolean moduleTeilnahmeverzeichnis = true;

    public boolean moduleTabletAbstimmung = true;
    public boolean moduleBestandsverwaltung = true;
    public boolean moduleHotline = true;

    public boolean moduleAktienregisterImport = true;
    public boolean moduleDesigner = true;

    /************Zurücksetzen*********************/
    public boolean lokalZuruecksetzen = false;

}
