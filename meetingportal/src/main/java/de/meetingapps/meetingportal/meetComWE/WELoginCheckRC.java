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

import de.meetingapps.meetingportal.meetComEclM.EclTeilnehmerLoginM;
import de.meetingapps.meetingportal.meetComEntities.EclRegistrierungsdaten;
import de.meetingapps.meetingportal.meetComEntities.EclTEinstellungenSession;
import de.meetingapps.meetingportal.meetComEntities.EclTLoginDatenM;

public class WELoginCheckRC extends WERootRC {

    public EclTLoginDatenM eclTLoginDatenM=null;

    public boolean registrierungAufrufen = false;

    public EclTEinstellungenSession eclTEinstellungenSession=null;
    
    /*Ab hier noch nicht überprüft*/
    @Deprecated
    /**Wird zu eclLoginDatenM*/
    public EclTeilnehmerLoginM eclTeilnehmerLoginM = null;

    @Deprecated
    /**Wird zu ecltTeinstellungenSession*/
    public EclRegistrierungsdaten registrierungsdaten = null;

    @Deprecated
    /**in eclTLoginDatenM enthalten*/
    public String passwortVerschluesselt = "";

    public int anzahlAktionaersnummernVorhanden = 1;

}
