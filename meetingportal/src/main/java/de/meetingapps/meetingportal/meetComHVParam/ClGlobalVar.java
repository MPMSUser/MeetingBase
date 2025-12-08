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

/**In neuer Version: führende Klasse für diese Inhalte*/
public class ClGlobalVar implements Serializable {
    private static final long serialVersionUID = -4047793689079630885L;

    /************Teil A*********************************************
     * Wird - bei Web-Services - immer mit den Werten gefüllt, die der
     * benutzende Arbeitsplatz hat (nicht der Server!)
     */
    public int mandant = 0;
    public int hvJahr = 0;
    public String hvNummer = "";
    public String datenbereich = "";

    /**Präsenzlistennummer*/
    public int zuVerzeichnisNr1 = 0;
    public int zuVerzeichnisNr2 = 0;
    public int zuVerzeichnisNr3 = 0;
    public int zuVerzeichnisNr4 = 0;

    /**Aktive Protokollnummer*/
    public int protokollnr = 0;

    public String user = "Akionärsportal";
    public int benutzernr = 9999; /*Portal*/

    /**Vordefinierte benutzernr / arbeitsplatz:
     * Ab 9000: reserviert für technische User
     * 9999 = Aktionärs-Portal, ist vorbelegt da im Portal derzeit keine Initialisierung stattfindet
     * 9998 = HV-App BetterSmart
     */
    public int arbeitsplatz = 9999; /*Portal*/

    /**Sprache der Oberfläche
     * 1= Deutsch
     * 2= Englisch
     * Wird auch für EK-Druck, Mail-Versand etc. genutzt.
     */
    public int sprache = 1;

    /**************************Teil B******************************************
     * Wird - auch bei Web-Services - auf dem Server immer mit den für den Server
     * zutreffenden Werten (arbeitsplatz=9999) gefüllt. Die für den jeweils nutzenden
     * Arbeitsplatz (bei Web-Service) geltenden Werte stehen dann in paramGeraet*/

    /**Default-Werte für Client siehe CaProgrammStart.dekodiere*/

    /**Laufwerke in der Form
     * D:\Meeting_Root
     * angeben.
     */

    /**Laufwerk und Pfad für Dokumente, Ausgabe, Zwischenablage - d.h. für meetingreports, meetingoutput, meetingausdrucke, meetingausdruckeintern*/
    public String lwPfadAllgemein = "";

    /**Laufwerk und Pfad für "Groß-Dokumente" (Videos etc., die nicht gesichert werden müssen)*/
    public String lwPfadGrossdokumente = "";

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

    /**Laufwerk und Pfad für https-Zertifikat. Wird nur auf Client-Seite verwendet (getestet),
     * wenn der Client eine https-Verbindung zum Server aufbaut.
     */
    public String lwPfadZertifikat = "";

    /**Laufwerk und Pfad für Kundenordner (Basis); wird dann ergänzt um Kundenspezifischen Teil aus Emittent*/
    public String lwPfadKundenordnerBasis = "";

    /**********************TEIL C*************************************************************
     * Für Kommunikation - nur auf dem Client-Teil verfügbar!*/

    // @formatter:off
    /**==-1 => steht nicht zur Verfügung*/
    public int webServicePfadNr=6;
 
    /**Wie webServicePfadNr, nur für Online-Server für Hybrid-Veranstaltungen*/
    public int onlineWebServicePfadNr=6;


     /**==-1 => steht nicht zur Verfügung*/
    public int datenbankPfadNr=0;
    
    /**Routinen zum glei  chzeitigen Versorgen von mehreren Datenbanken,
     * konkret Benutzerpflege
     */
    public boolean datenbankPfadNrIstP02() {
        return datenbankPfadNr==5;
    }

    public void setzeDatenbankPfadNrAufP01() {
        datenbankPfadNr=0;
    }

    public void setzeDatenbankPfadNrAufP02() {
        datenbankPfadNr=5;
    }

    public void setzeDatenbankPfadNrAufP03() {
        datenbankPfadNr=6;
    }
    
    /**wie datenbankPfadNr, nur für Online-Server für Hybrid-Versammlungen*/
    public int onlineDatenbankPfadNr=0;
    

    
    /*******************TEIL D ***************************************
     * Parameter, die hier definitiv raus müssen!
     */
    /*TODO _Parameter: sammelkartenVorrangVerfahren gehört nicht in ClGlobalVar!*/
    public int sammelkartenVorrangVerfahren = 1;

    /**Liefert mandanten-Nr. (nur Mandant!) als String 3 stellig mit führenden 0*/
    public String getMandantString() {
        String h = Integer.toString(mandant);
        while (h.length() < 3) {
            h = "0" + h;
        }
        return h;
    }
    
    /**Liefert "M"+Mandantennummer + j + HVJAhr + hvnummer+datenbereich, um dies in den Pfad aufzunehmen*/
   public String getMandantPfad() {
        return "M" + getMandantString() + "J" + CaString.fuelleLinksNull(Integer.toString(hvJahr), 4)
        + hvNummer + datenbereich;
    }

}
