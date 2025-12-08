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

/**Klasse dient dazu, um aus der App heraus über den Browser mittels Direktlink Aktivitäten im Portal auszulösen, 
 * insbesondere um generierte Dokumente anzuzeigen.
 * 
 * Es sind "Einmal-Sofort-Aktivitäten", d.h.: die Aktivität wird über einen Web-Service aktiviert und anschließend unmittelbar abgerufen und wieder gelöscht
  */
public class EclAppAktivitaeten {

    /**LEN=20 - wird automatisch beim Insert gesetzt*/
    public String eindeutigerKey = "";

    /**Siehe KonstAppAktivitaet*/
    public int funktion = 0;

    /**Zugehöriger Mandant - wird automatisch beim Insert gesetzt*/
    public int mandant = 0;
    public int hvJahr = 0;
    public String hvNummer = "";
    public String datenbereich = "";

    /**Länge jeweils 80 - abhängig von funktion*/
    public String paramter1 = "";
    public String paramter2 = "";
    public String paramter3 = "";
    public String paramter4 = "";
    public String paramter5 = "";

    /**LEN=19 - wird automatisch beim Insert gesetzt*/
    public String datumUhrzeit = "";

}
