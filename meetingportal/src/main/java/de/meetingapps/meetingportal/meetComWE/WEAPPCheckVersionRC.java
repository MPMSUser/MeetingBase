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
package de.meetingapps.meetingportal.meetComWE;

/**Beschreibung der Eigenschaften: siehe BlAppVersionsabgleich*/
public class WEAPPCheckVersionRC extends WERootRC {

    /**0 = kein Text-Update erforderlich; 1=Textupdate erforderlich*/
    public int[] mandantErgebnis = null;
    public int[] updateTextMandantDE = null;
    public int[] updateTextMandantEN = null;

    /**0 = keine neue Version vorhanden; 1=neue Version ist vorhanden, aber nicht zwingend;
     * 2 = Version nicht mehr kompatibel, Einsatz zwingend erforderlich
     */
    public int updateAppVersion = 0;

}
