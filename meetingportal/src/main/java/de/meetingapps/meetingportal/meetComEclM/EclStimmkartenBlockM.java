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
package de.meetingapps.meetingportal.meetComEclM;

import java.io.Serializable;

import de.meetingapps.meetingportal.meetComEntities.EclStimmkartenBlock;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

@SessionScoped
@Named
public class EclStimmkartenBlockM implements Serializable {
    private static final long serialVersionUID = -8633261812224339659L;

    private EclStimmkarteInhaltM[] stimmkartenBlockM = null;

    public void copyFrom(EclStimmkartenBlock pstimmkartenBlock) {
        int i;

        if (pstimmkartenBlock.stimmkartenBlock != null) {

            stimmkartenBlockM = new EclStimmkarteInhaltM[pstimmkartenBlock.stimmkartenBlock.length];
            for (i = 0; i < pstimmkartenBlock.stimmkartenBlock.length; i++) {
                EclStimmkarteInhaltM lStimmkartenBlockInhalt = new EclStimmkarteInhaltM();
                lStimmkartenBlockInhalt.copyFrom(pstimmkartenBlock.stimmkartenBlock[i]);
                this.stimmkartenBlockM[i] = lStimmkartenBlockInhalt;
            }
        }

    }

    /***************Standard Getter/Setter***********************************/

    public EclStimmkarteInhaltM[] getStimmkartenBlockM() {
        return stimmkartenBlockM;
    }

    public void setStimmkartenBlockM(EclStimmkarteInhaltM[] stimmkartenBlockM) {
        this.stimmkartenBlockM = stimmkartenBlockM;
    }

}