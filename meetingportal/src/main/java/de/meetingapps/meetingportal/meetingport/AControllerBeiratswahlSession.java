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
package de.meetingapps.meetingportal.meetingport;

import java.io.Serializable;

import de.meetingapps.meetingportal.meetComEclM.EclZugeordneteMeldungListeM;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@SessionScoped
@Named
@Deprecated
public class AControllerBeiratswahlSession implements Serializable {
    private static final long serialVersionUID = -1072228811792773733L;

    @Inject
    private EclZugeordneteMeldungListeM eclZugeordneteMeldungListeM;

    //	private boolean beiratswahlGemacht=false;

    /*******************Standard getter und setter***************************/

    public boolean isBeiratswahlGemacht() {
        if (eclZugeordneteMeldungListeM.isSrvVorhanden()) {
            setBeiratswahlGemacht(true);
            return true;
        } else {
            setBeiratswahlGemacht(false);
            return false;
        }

    }

    public void setBeiratswahlGemacht(boolean beiratswahlGemacht) {
        //		this.beiratswahlGemacht = beiratswahlGemacht;
    }

}
