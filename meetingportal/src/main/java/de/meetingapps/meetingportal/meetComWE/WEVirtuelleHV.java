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

import de.meetingapps.meetingportal.meetComEclM.EclVirtuellerTeilnehmerM;

/**Alle Funktionen zur virtuellen HV*/
public class WEVirtuelleHV extends WERoot {

    /**1=Frage/Mitteilung/Stream - vorbereiten (liefert alle gestellten Fragen, sowie Fragensteller)
     * 2=Frage - stellen
     * 3=Mitteilung - vorbereiten
     * 4=Mitteilung - stellen
     * 5=Videokonferenz
     * 6=Teilnehmerverzeichnis
     * 7=Abstimmungsergebnisse
     * 8=PDF Anzeigen
     */
    public int funktion = 0;

    /*SpotBugs: Wahrscheinlich nicht verwendet*/
    public EclVirtuellerTeilnehmerM steller = null;

    public String frageZuTop = "";
    public String frageMitteilungText = "";

    public int pdfArt = 0; //KonstPdfArt
    public int pdfLfdNummer = 0;

}
