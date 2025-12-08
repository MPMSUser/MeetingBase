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

/**"Kopfsatz" für einen Abstimmungsvorgang (Einsammelvorgang).
 * 
 * Über EclAbstimmungZuAbstimmungsblock wird EclAbstimmungsblock.ident mit EclAbstimmung.ident verbunden
 */
public class EclAbstimmungsblock implements Serializable {
    private static final long serialVersionUID = -7625255112452749598L;

    public int mandant = 0;

    /**Eindeutige, interne Nummer*/
    public int ident = 0;

    public long db_version = 0;

    /**Reihenfolge zum Anzeige bei externen Geräten, z.B. Tablets*/
    public int position = 0;

    /**Interne Beschreibung
     * L=40*/
    public String kurzBeschreibung = "";

    /**L=80*/
    public String beschreibung = "";
    /**L=80*/
    public String beschreibungEN = "";

    /**1 = Zur Verwendung grundsätzlich freigegeben ("Anzeige")
     * 2 = Abstimmung läuft gerade - Einsammeln und einlesen möglich
     * 3 = Abstimmung läuft gerade - Einsammeln beendet, nur noch internes einlesen (im Backoffice) möglich
     * 
     * Hinweis: Aktuell nur EIN Block zulässig, der auf 2 oder 3 stehen darf. Alle anderen müssen auf 1 oder 0 stehen.
     * */
    public int aktiv = 0;

}
