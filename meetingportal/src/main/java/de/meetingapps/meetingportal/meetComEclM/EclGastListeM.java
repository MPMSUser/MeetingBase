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
import java.util.LinkedList;
import java.util.List;

import de.meetingapps.meetingportal.meetComEntities.EclMeldung;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

@SessionScoped
@Named
public class EclGastListeM implements Serializable {
    private static final long serialVersionUID = -4023685088637960573L;

    private List<EclGastM> gastListeM;

    public EclGastListeM() {
        init();
    }

    public void init() {
        gastListeM = new LinkedList<>();

    }

    public void add(EclMeldung pMeldung) {
        EclGastM lGast = new EclGastM();
        lGast.copyFrom(pMeldung);
        gastListeM.add(lGast);
    }

    public List<EclGastM> getGastListeM() {
        return gastListeM;
    }

    public void setGastListeM(List<EclGastM> gastListeM) {
        this.gastListeM = gastListeM;
    }

}
