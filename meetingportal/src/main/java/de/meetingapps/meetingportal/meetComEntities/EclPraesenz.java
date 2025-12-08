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

/**Präsenz-Kennzeichen.
 * 
 * Nur temporär, für die Belange des Prototypen ausgelegt!
 *
 */
public class EclPraesenz {

    public int mandant = 0;

    /**Primärschlüssel zusammen mit mandant - wird intern vergeben*/
    public int meldungsIdent = 0;
    /**Versionsnummerierung zum Erkennen, ob DB-Satz von anderem User upgedatet wurde.
     * Darf nur von Db-Verwaltung selbst verwendet werden!*/
    public long db_version = 0;

    /**Kartenart, mit die Präsenzakkreditierung erfolgte
     * 1 = Stimmkarte
     * 2 = Eintrittskarte
     * 3 = Gastkarte
     */
    @Deprecated
    public int kartenart = 0;

    /**Kartennummer, mit die Präsenzakkreditierung erfolgte*/
    @Deprecated
    public int kartennr = 0;

    /**Person, die die Karte aktuell vertritt*/
    public int identPersonNatJur = 0;

    /**Flag: ist gerade präsent
     * 1= ja
     * 0=nein
     */
    public int istPraesent = 0;

}
