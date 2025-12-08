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
package de.meetingapps.meetingportal.meetComHVParam;

import de.meetingapps.meetingportal.meetComDb.DbBundle;

public class ParamApp {

    /** Mindestlaenge Passwort */
    public int pPasswortMindestLaenge = 0;

    /*********************Die folgenden Parameter werden (überlicherweise)laufend eingelesen**********/

    public int pLfdHVPortalInBetrieb = 1;
    /**1=vor der HV, 2=nach der HV (Parameter Nr. 15)*/
    public int pLfdVorDerHVNachDerHV = 0;
    public int pLfdHVPortalErstanmeldungIstMoeglich = 1;
    public int pLfdHVPortalEKIstMoeglich = 1;
    public int pLfdHVPortalSRVIstMoeglich = 1;
    public int pLfdHVPortalBriefwahlIstMoeglich = 1;
    public int pLfdHVPortalKIAVIstMoeglich = 1;
    public int pLfdHVPortalVollmachtDritteIstMoeglich = 1;

    public void copyFromClParameterNS(DbBundle lDbBundle) {
        ParamPortal lParamPortal = lDbBundle.param.paramPortal;
        /*TODO #Parameter lDbBundle.hvparam.clParameterNS offensichtlich hier nicht ordentlich gefüllt!!!*!/
         * 
         */
        this.pPasswortMindestLaenge = lParamPortal.passwortMindestLaenge;

        this.pLfdHVPortalInBetrieb = lParamPortal.lfdHVPortalInBetrieb;
        this.pLfdVorDerHVNachDerHV = lParamPortal.lfdVorDerHVNachDerHV;
        this.pLfdHVPortalErstanmeldungIstMoeglich = lParamPortal.lfdHVPortalErstanmeldungIstMoeglich;
        this.pLfdHVPortalEKIstMoeglich = lParamPortal.lfdHVPortalEKIstMoeglich;
        this.pLfdHVPortalSRVIstMoeglich = lParamPortal.lfdHVPortalSRVIstMoeglich;
        this.pLfdHVPortalBriefwahlIstMoeglich = lParamPortal.lfdHVPortalBriefwahlIstMoeglich;
        this.pLfdHVPortalKIAVIstMoeglich = lParamPortal.lfdHVPortalKIAVIstMoeglich;
        this.pLfdHVPortalVollmachtDritteIstMoeglich = lParamPortal.lfdHVPortalVollmachtDritteIstMoeglich;

    }

}
