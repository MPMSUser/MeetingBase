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

/**Struktur für das Monitoren von einzelnen Aktionären während der Abstimmung.
 * Achtung - aus Performancegründen enthält diese Strukturen einige
 * redundante Informationen, die (vor der Abstimmung) über eine spezielle
 * Funktion gefüllt werden müssen.
 */
public class EclAbstimmungsmonitorEK {

    public int mandant = 0;;

    /**Key
     * Wird derzeit manuell vergeben*/
    public int ident = 0;

    public long db_version = 0;

    /**Wird manuell eingetragen. Anhand dieses Strings werden dann die restlichen Daten gefüllt*/
    public String zutrittsIdent = "";
    public String zutrittsIdentNeben = "";

    /*redundant*/
    public String stimmkarte = "";

    /**Redundant*/
    public int meldeIdent = 0;

    /**redundant*/
    public String meldeAktionaer = "";
    public String meldeVertreter = "";
    public long meldeStimmen = 0;
    public int statusPraesenz = 0;

}
