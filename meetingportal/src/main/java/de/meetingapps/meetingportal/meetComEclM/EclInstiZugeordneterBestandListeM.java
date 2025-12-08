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
import java.util.List;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

/**Enthält die Liste aller Bestände eines Instis*/
@SessionScoped
@Named
public class EclInstiZugeordneterBestandListeM implements Serializable {
    private static final long serialVersionUID = -7294881382998117854L;

    private List<EclInstiZugeordneterBestandM> instiZugeordneterBestandM = null;

    /********************Standard getter und setter********************************/

    public List<EclInstiZugeordneterBestandM> getInstiZugeordneterBestandM() {
        return instiZugeordneterBestandM;
    }

    public void setInstiZugeordneterBestandM(List<EclInstiZugeordneterBestandM> instiZugeordneterBestandM) {
        this.instiZugeordneterBestandM = instiZugeordneterBestandM;
    }

}
