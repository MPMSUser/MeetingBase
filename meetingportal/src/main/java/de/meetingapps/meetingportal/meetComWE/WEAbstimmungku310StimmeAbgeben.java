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

import de.meetingapps.meetingportal.meetComEntities.EclAbstimmungku310;

public class WEAbstimmungku310StimmeAbgeben {
    public WELoginVerify weLoginVerify = null;

    public EclAbstimmungku310 abstimmungku310 = null;

    /*****************Standard Getter und Setter***********************************************/

    public EclAbstimmungku310 getAbstimmungku310() {
        return abstimmungku310;
    }

    public void setAbstimmungku310(EclAbstimmungku310 abstimmungku310) {
        this.abstimmungku310 = abstimmungku310;
    }

    public WELoginVerify getWeLoginVerify() {
        return weLoginVerify;
    }

    public void setWeLoginVerify(WELoginVerify weLoginVerify) {
        this.weLoginVerify = weLoginVerify;
    }

}
