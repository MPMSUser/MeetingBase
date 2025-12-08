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

/**Speichern von Anzuzeigenden Texten in der App*/
public class EclAppTexte {

    /**0 => übergreifende Texte*/
    public int mandant = 0;

    /**Festkodiert: 1=Deutsch, 2=Englisch*/
    public int sprache = 0;

    /**Seiten-Nummer innerhalb der App*/
    public int seitennummer = 0;

    /**Text-Ident auf der entsprechenden Seite*/
    public int ident = 0;

    /**Zu einer Ident kann es bliebig viele Texte geben, die - sortiert nach lfdNummer - beim Anzeigen
     * aneinandergehängt werden. Dies ist der Tatsache geschuldet, dass SQLite nach vorliegenden Informationen
     * maximal 500 Zeichen in einer Text-Spalte kann.
     * Die aufeinanderfolgenden Texte werden strikt hintereinander gehängt. 
     * Achtung: Leerzeichen am Schluß werden <<<<<das wird eliminiert!!!
     * abgeschnitten. Soll also zwischen zwei Texten ein Blank sein, dann muß dies am Anfang des zweiten Textes
     * eingefügt werden.
     */
    public int lfdNummer = 0;

    /**Hierüber erfolgt der Update/Übertragung zu App. Es werden immer alle Texte zur App
     * übertragen, deren Versionsnummer höher ist als die aktull in der App hinterlegte
     * Versionsnummer.
     */
    public int letzteVersion = 0;

    /**Formatierungs-Kennzeichen
     * 1 = New Line
     * 2 = Bold
     * 4 = Italic
     * 8 = FontSize Large		derzeit nicht verwendet
     * 16 = FontSize Medium		H1, H2, H3 wird immer darauf umgesetzt
     * 32 = FontSize Micro		derzeit nicht verwendet
     * 64 = FontSize Small		derzeit nicht verwendet
     * 128 =
     * 256 =
     * 512 =
     * 1024 =
     * 2048 = 
     * 4096 =
     * 8192 =
     * 16384 =
     * 32768 =
     * 65536 =
     * 131072 =
     * 262144 =
     * 524288 =
     * 
     */
    public int formatierung = 0;

    /**
     * Enthält folgende Steuerzeichen/Sonderkodes:
     * \u000A neue Zeile
     * <<<variablenname>>> Wird durch den Inhalt der entsprechenden Variablen zur Verfügung gestellt.
     * Variablen sind derzeit:
     * 
     * 
     * LEN=varchar 400*/
    public String anzeigetext = "";

}
