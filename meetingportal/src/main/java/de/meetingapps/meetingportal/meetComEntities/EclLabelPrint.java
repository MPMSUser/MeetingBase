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

/**EingabeStruktur für Labelprinter*/
public class EclLabelPrint {

    public String ekNummer = "";
    public String skNummer = "";
    public String skNummerBar = "";
    public String vornameAktionaer = "";
    public String nachnameAktionaer = "";
    public String ortAktionaer = "";
    public String vornameVertreter = "";
    public String nameVertreter = "";
    public String ortVertreter = "";
    public String aktienzahl = "";

    public void leeren() {

        ekNummer = "";
        skNummer = "";
        skNummerBar = "";
        vornameAktionaer = "";
        nachnameAktionaer = "";
        ortAktionaer = "";
        vornameVertreter = "";
        nameVertreter = "";
        ortVertreter = "";
        aktienzahl = "";

    }

}
