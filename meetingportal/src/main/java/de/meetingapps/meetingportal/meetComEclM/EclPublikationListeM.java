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

@SessionScoped
@Named
public class EclPublikationListeM implements Serializable {
    private static final long serialVersionUID = -4023685088637960573L;

    private List<EclPublikationM> publikationListeM;

    public EclPublikationListeM() {
        publikationListeM = null;
    }

    /*************Standard Getter und Setter**************************/
    public List<EclPublikationM> getPublikationListeM() {
        return publikationListeM;
    }

    public void setPublikationListeM(List<EclPublikationM> publikationListeM) {
        this.publikationListeM = publikationListeM;
    }

}
