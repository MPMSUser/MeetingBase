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

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

@SessionScoped
@Named
@Deprecated
public class AControllerWeisungBestaetigungSession implements Serializable {
    private static final long serialVersionUID = -2615488759892657458L;

    private String quelle = "";

    /**Wenn !=-1, dann wird die eingetragene Nummer für Buchung der Weisung verwendet*/
    private int abweichendeSammelkarte = -1;

    /*****************Standard-Getter/setter****************************/

    public String getQuelle() {
        return quelle;
    }

    public void setQuelle(String quelle) {
        this.quelle = quelle;
    }

    public int getAbweichendeSammelkarte() {
        return abweichendeSammelkarte;
    }

    public void setAbweichendeSammelkarte(int abweichendeSammelkarte) {
        this.abweichendeSammelkarte = abweichendeSammelkarte;
    }

}
