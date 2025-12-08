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
package de.meetingapps.meetingportal.meetComEntities;

import java.io.Serializable;

/**Kombination aus EclBestWorkflow und zugehöriger EclVorlaeufigeVollmacht.
 * 
 * Für die Anzeige in JSF für die Workflow-Oberfläche
 */
public class EclBestWorkflowVorlVollmacht implements Serializable {
    private static final long serialVersionUID = -7203554565767445597L;

    public EclBestWorkflow eclBestWorkflow = null;
    public EclVorlaeufigeVollmacht eclVorlVollmacht = null;

    /*****************Standard getter und setter********************************/
    public EclBestWorkflow getEclBestWorkflow() {
        return eclBestWorkflow;
    }

    public void setEclBestWorkflow(EclBestWorkflow eclBestWorkflow) {
        this.eclBestWorkflow = eclBestWorkflow;
    }

    public EclVorlaeufigeVollmacht getEclVorlVollmacht() {
        return eclVorlVollmacht;
    }

    public void setEclVorlVollmacht(EclVorlaeufigeVollmacht eclVorlVollmacht) {
        this.eclVorlVollmacht = eclVorlVollmacht;
    }

}
