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

/**Elektronische Stimmkarte - Zuordnung der einzelnen Abstimmungen*/
public class EclAbstimmungenZuStimmkarte {

    public int mandant = 0;

    public int stimmkartenNr = 0;

    public long db_version = 0;

    /**Die "reale" Abstimmung, die mit diesem Punkt durchgeführt wird (und ausgewertet wird)*/
    public int identAbstimmung = 0;

    /**Die Abstimmung, die auf der Stimmblockkarte - in der App - angezeigt wird. Bei HilfsTOPs,
     * bei denen die Verwendung ggf. erst nachträglich zugeordnet wird, bzw. die zur Erstellung 
     * noch nicht vorliegen, abweichend von identAbstimmung!.
     * Die "Übersetzung" erfolgt beim Einscannen der AppIdent bei der Abstimmung.
     */
    public int identAbstimmungAufKarte = 0;

    public int positionInStimmkarte = 0;

}
