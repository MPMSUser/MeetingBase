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
public class EclAbstimmungenSplitListeM implements Serializable {
    private static final long serialVersionUID = -8633261812224339659L;

    private List<EclAbstimmungSplitM> abstimmungenSplitListeM;
    private List<EclAbstimmungSplitM> gegenantraegeSplitListeM;

    public EclAbstimmungenSplitListeM() {
        abstimmungenSplitListeM = null;
        gegenantraegeSplitListeM = null;
    }

    public List<EclAbstimmungSplitM> getAbstimmungenSplitListeM() {
        return abstimmungenSplitListeM;
    }

    public void setAbstimmungenSplitListeM(List<EclAbstimmungSplitM> abstimmungenSplitListeM) {
        this.abstimmungenSplitListeM = abstimmungenSplitListeM;
    }

    public List<EclAbstimmungSplitM> getGegenantraegeSplitListeM() {
        return gegenantraegeSplitListeM;
    }

    public void setGegenantraegeSplitListeM(List<EclAbstimmungSplitM> gegenantraegeSplitListeM) {
        this.gegenantraegeSplitListeM = gegenantraegeSplitListeM;
    }

}
