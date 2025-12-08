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

import java.util.List;

import de.meetingapps.meetingportal.meetComEclM.EclAbstTeilButtonM;
import de.meetingapps.meetingportal.meetComEclM.EclFragenM;
import de.meetingapps.meetingportal.meetComEclM.EclMitteilungenM;
import de.meetingapps.meetingportal.meetComEclM.EclVirtuellerTeilnehmerM;

public class WEVirtuelleHVRC extends WERootRC {

    public int maxZeichen = 0;
    public int maxFragenMitteilungen = 0;

    public List<EclVirtuellerTeilnehmerM> virtuelleTeilnehmerListM = null;
    public List<EclFragenM> fragenGestelltListe = null;
    public List<EclMitteilungenM> mitteilungenGestelltListe = null;

    public List<EclAbstTeilButtonM> absTeilButtonList = null;

    public String linkFuerPdfShow = "";
}
