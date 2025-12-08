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

import java.io.Serializable;

/**Aufträge für Hintergrundverwaltung
 * 
 * Doku für die Verwendung der einzelnen Felder je nach Modul
 * ==========================================================
 * 
 * ANBINDUNG_AKTIENREGISTER
 * ----------------------------------------------------------
 * > Werden mandantenabhängig gespeichert
 * > schluessel = Identifikation des Aktienregister-Tickets
 * > statusAuftragsArt
 * > zeitStart, zeitErledigt 
 * > zeitLetzterStatuswechsel (wird als "gesendet"-Datum in Mail verwendet) 
 * > statusAuftragsArtGelesen, statusAuftragsArtGeloescht
 * 
 * ANBINDUNG_AKTIENREGISTER_AENDERN_NEWSLETTER
 * ...........................................
 * parameterInt[0]=pVersandart (1 per Post, 2 per Mail, 3 kein Newsletter)
 * 
 * KONTAKT_FORMULAR
 * -----------------------------------------------------------
 * > Werden mandanten-un-abhängig gespeichert
 * > Falls AnbindungAktienregister aktiviert, und Übergabe an Ticket: schluessel=  Identifikation des Aktienregister-Tickets
 * > zeitStart (wird als "gesendet"-Datum in Mail verwendet)
 * > startAnzeigeGelesen, startAnzeigeGeloescht
 * > freitextBeschreibung= Text der Kontaktanfrage
 * > parameterTextLang[0]=kontaktThemaWeitergabeText
 * > parameterTextLang[1]=kontaktThemaText
 * > parameterInt[0]=kontaktThemaIdent
 * 
 * */
public class EclAuftrag implements Serializable {
    private static final long serialVersionUID = -2603315880712112397L;

    /**Eindeutige, interne Nummer (unique Key mit identVersion und lfd-Nr.)*/
    public int ident = 0;
    
    /**Laufende "Unter"-Nummer für Versionshaltung (d.h.
     * !=0 => historischer Satz)
     */
    public int identVersion=0;

    public long db_version = 0;
    
    /**Siehe ParamServer
     * Gehört zu ident und identMail*/
    public int dbServerIdent=0;

    /**Lfd-Nr für Aufträge, die auf mehrere Sätze aufgeteilt sind (weil z.B. mehrere Felder benötigt*/
    public int lfdNr=0;

    /**Es gibt mandantenabhängige Aufträge (i.d.R. vom Aktionär ausgelöste, automatisch zu verarbeitende
     * Aufträge. In diesem Fall wird dieses Feld nicht verwendet, diese Aufträge werden
     * im Mandantenschema gespeichert.
     * 
     * Es gibt "mandantenabhängige aber übergreifende" Aufträge (z.B. Job-Aufträge in uLogin). In diesem Fall
     * wird dieses Feld mit der Mandantennummer belegt, diese Aufträge werden im übergreifenden Schema gespeichert.
     * 
     * Zweiteres soll immer (und nur dann) verwendet werden, wenn es eine mandanten-übergreifende Abfrage über die
     * aktuellen Jobs geben soll.
     */
    public int mandant=0;
    public int hvJahr=0;
    public String hvNummer="";
    public String datenbereich="";

    /**siehe KonstAuftragModul*/
    public int gehoertZuModul=0;
    
    /**siehe KonstAuftragArt*/
    public int auftragsArt=0;

    /**Gibt Grund-Status innerhalb dieser Auftragsverarbeitung an.
     * Siehe KonstAuftragStatus*/
    public int status=0;
    
    /**Gibt "Detailstatus" - abhängig von Auftragsart - an.
     * Siehe KonstAuftragStatusAuftragsArt*/
    public int statusAuftragsArt=0;
    
    /**Detailerläuterung, z.B. bei Erledigt / abgelehnt Ursachen-Nummer
     * Siehe KonstAuftragStatusInfoDetail */
    public int statusInfoDetail=0;
    
    /**LEN=1000*/
    public String statusInfoFreitext="";
    
    /**siehe KonstAuftragEingangsweg*/
    public int eingangsweg=0;
    
    /**Benutzer, für den der Auftrag erbracht wird
     * >0 => Verweis auf EclUserLogin (Mandantenübergreifend)
     * <0 => Verweis auf EclLoginDaten (des jeweiligen Mandanten)
     * =0 => Systemmail
     * 
     * je nach Modul: 0 = siehe freitextAuftraggeber
     * */
    public int userIdAuftraggeber=0;
    
    /**LEN=1000*/
    public String freitextAuftraggeber="";
    
    /**z.b. Verweis auf Scan-Datei, oder InhaberImport-Import, oder ähnliches
     * LEN=100*/
    public String verweisAufImport="";
    
    /**je nach Eingangsweg zu füllen*/
    public int userIdErfasser=0;
    
    /**Benutzer, der die Aufgabe erledigt (oder zuletzt bearbeitet) hat*/
    public int userIdVerarbeitet=0;
    
    /**Satz ist gerade in Bearbeitung durch diesen User (wird nur gesetzt, wenn Vorgang
     * manuell bearbeitet wird*/
    public int userIdAktuellerBearbeiter=0;
    
    /**bei automatischer Verarbeitung: Nummer des Verarbeitungslaufes*/
    public int drucklaufNr=0;
    
    /**LEN=200
     * Wird innerhalb eines Moduls vergeben / verwendet*/
    public String schluessel="";
    
    /**LEN=19*/
    public String zeitStart="";
    public String zeitErledigt="";
    public String zeitLetzterStatuswechsel="";
    
    public int startAnzeigeGelesen=0;
    public int startAnzeigeGeloescht=0;
    
    public int erledigtAnzeigeGelesen=0;
    public int erledigtAnzeigeGeloescht=0;
    
    public int statusAuftragsArtGelesen=0;
    public int statusAuftragsArtGeloescht=0;

    /**Text, in dem z.B. die Hotline ein Anliegen eines Aktionärs beschreiben kann.
     *
     * Je nach Modul zu verwenden.
     * LEN=10000*/
    public String freitextBeschreibung = "";
   
    
    /**++++++die folgenden Felder werden abhängig von Modul/Auftragsart verwendet+++++++++++*/
    /**5=>10 mal 300 Zeichen*/
    public String parameterTextLang[]=new String[10];
    
    /**5 mal 40 Zeichen*/
    public String parameterTextKurz[]=new String[5];

    /**5 mal int*/
    public int parameterInt[]=new int[5];
    
    /**5=>10 mal 300 Zeichen*/
    public String ergebnisTextLang[]=new String[10];
    
    /**5 mal 40 Zeichen*/
    public String ergebnisTextKurz[]=new String[5];

    /**5 mal int*/
    public int ergebnisInt[]=new int[5];


    public EclAuftrag() {
        for (int i=0;i<10;i++) {
            parameterTextLang[i]="";
            ergebnisTextLang[i]="";
        }

        for (int i=0;i<5;i++) {
            parameterTextKurz[i]="";
            parameterInt[i]=0;
            ergebnisTextKurz[i]="";
            ergebnisInt[i]=0;
        }
    }
}
