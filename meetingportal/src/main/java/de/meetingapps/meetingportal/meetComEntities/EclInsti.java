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

/**Institutionelle Anleger - KIAVs, Investoren etc. - Mandantenübergreifende Daten*/
public class EclInsti {

    public int mandant = 0;

    /**Primärschlüssel z- wird intern vergeben*/
    public int ident = 0;

    /**Versionsnummerierung zum Erkennen, ob DB-Satz von anderem User upgedatet wurde.
     * Darf nur von Db-Verwaltung selbst verwendet werden!*/
    public long db_version = 0;

    /**Bezeichnung für allgemeine Anzeige.
     * Len=80
     */
    public String kurzBezeichnung = "";

    /**Verweis auf die Kontaktdaten des Hauptansprechpartners*/
    public int identHauptKontakt = 0;

    //	/**>0 => dies ist ein Pseudo-Institutioneller, d.h. Dritter Dienstleister (z.B.
    //	 * Proxy Solicitator), der mandanten-übergreifenden Zugang hat (primär zu Mails, aber auch
    //	 * Emittentenportal-Abfrage)
    //	 * =1 => Hat automatisch zu allen Mandanten Zugang, wenn Mail oder Bestand zugegangen ist
    //	 * =2 => hat nur Zugang zu den Mandanten, bei dem diesem Insti ein (Pseudo-)Bestand zugeordnet wurde
    //	
    //	Ist nicht fertig durchdacht! Zurückgestellt. Ist mal angedacht für Dritte, die Mandantenübergreifend
    //	arbeiten, z.B. Proxy Solicitator. Da müßte aber dann noch eine Table aufgebaut werden, welche Rechte die
    //	dann pro Mandant im Emittentenportal haben ....
    //	 */
    //	public int pseudoInsti=0;

    /**Verweis auf Suchlauf*/
    public int identSuchlaufBegriffe = 0;

    public int standardSammelkartenAnlageAktiv=1;
    
    /**siehe zusatzfeld1*/
    public int weisungsWeitergabe=0;
     
    /**1 = entsprechende Sammelkarte wird beim Standard-Sammelkartenanlegen angelegt*/
    public int standardSammelkarteMitWeisung = 0; //cbStandardSammelkarteMitWeisung

    /**1 = entsprechende Sammelkarte wird beim Standard-Sammelkartenanlegen angelegt*/
    public int standardSammelkarteOhneWeisung = 0; //cbStandardSammelkarteOhneWeisung
    
    /**1 = entsprechende Sammelkarte wird beim Standard-Sammelkartenanlegen angelegt*/
     public int standardSammelkarteSRV=0; //cbStandardSammelkarteSRV
    
    /**1 = entsprechende Sammelkarte wird beim Standard-Sammelkartenanlegen angelegt*/
    public int standardSammelkarteBriefwahl=0; //cbStandardSammelkarteBriefwahl
    
    /**1 = entsprechende Sammelkarte wird beim Standard-Sammelkartenanlegen angelegt (nur wenn Namensaktien aktiv)*/
    public int standardSammelkarteDauervollmacht=0; //cbStandardSammelkarteDauervollmacht

    /**Gruppennummer, die beim Anlegen der Standard-Sammelkarten für diese Sammelkarten verwendet wird*/
    public int standardSammelkarteGruppennummer = 0;

    /**"Standard"-Name, Verwendung für Meldedaten / Sammelkarten-Anlage
     * LEN=80*/
    public String standardMeldeName = "";

    /**"Standard"-Ort, Verwendung für Meldedaten / Sammelkarten-Anlage
     * LEN=80*/
    public String standardMeldeOrt = "";

    /**Wird wahrscheinlich für InhaberImport benötigt ...*/
    public int festadressenNummer = 0;

    /*************************Nicht in Table, nur zur Vereinfachung beim Bearbeiten**************************/
    public String suchlaufBegriffe = "";
}
