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

import de.meetingapps.meetingportal.meetComEntities.EclEmittenten;
import de.meetingapps.meetingportal.meetComEntities.EclTermine;
import de.meetingapps.meetingportal.meetComHVParam.ParamModuleKonfigurierbar_App;
import de.meetingapps.meetingportal.meetComHVParam.ParamPortal;

/**Beschreibung der Eigenschaften: siehe BlAppVersionsabgleich*/
public class WEAPPHVParamAppHolenRC extends WERootRC {

    public ParamPortal paramPortal = null;
    public ParamModuleKonfigurierbar_App paramModuleKonfigurierbar_App = null;

    public EclTermine[] terminlisteTechnisch = null;
    public EclEmittenten aktuellerEmittent = null;

    public String[] gattungBezeichnung = null;

    public int[] anzahlJaJeAbstimmungsgruppe = null;

}
