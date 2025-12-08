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

public class EclPersonenNatJurVersandadresse {

    public int mandant = 0;

    /**Primärschlüssel zusammen mit mandant - wird intern vergeben*/
    public int ident = 0;
    /**Versionsnummerierung zum Erkennen, ob DB-Satz von anderem User upgedatet wurde.
     * Darf nur von Db-Verwaltung selbst verwendet werden!*/
    public long db_version = 0;

    @Deprecated
    public int identPersonenNatJur = 0;

    public int versandAbweichend;

    // +++NEU+++NEU+++NEU+++NEU+++NEU+++//
    public int anredeIdVersand = 0;

    public String titelVersand = "";
    // +++++++++++++++++++++++++++++++++//

    public String name3Versand = ""; // +++++++++++++++++++NEU
    public String name2Versand = ""; // +++++++++++++++++++NEU

    public String nameVersand;
    public String vornameVersand;
    public String strasseVersand;
    public String postleitzahlVersand;
    public String ortVersand;
    public int staatIdVersand;

}
