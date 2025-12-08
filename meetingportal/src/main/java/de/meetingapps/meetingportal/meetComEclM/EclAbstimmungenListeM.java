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

import de.meetingapps.meetingportal.meetComEntities.EclWeisungMeldung;
import de.meetingapps.meetingportal.meetComEntities.EclWeisungMeldungRaw;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

@SessionScoped
@Named
public class EclAbstimmungenListeM implements Serializable {
    private static final long serialVersionUID = -8633261812224339659L;

    /**Wird zu Beginn eines Weisungs-/Abstimmungsvorgang gesetzt, und am Ende mit der aktuellen Version
     * verglichen. Wenn Start-Version != aktuelle Version, dann kein Abschließen mehr möglich!
     */

    private List<EclAbstimmungM> abstimmungenListeM;
    private List<EclAbstimmungM> gegenantraegeListeM;

    /**Muß als Dummy wg. Web-Services vorhanden sein*/
    private boolean gegenantraegeVorhanden = true;

    private int alternative = 1;

    /**Alternativ: fertig aufbereitete Weisung/Abstimmung.
     * Wenn !=null, dann zählt diese Alternative und obige abstimmungenListeM etc. wird ignoriert!
     */
    private EclWeisungMeldung weisungMeldung = null;
    private EclWeisungMeldungRaw weisungMeldungRaw = null;

    public EclAbstimmungenListeM() {
        abstimmungenListeM = null;
        gegenantraegeListeM = null;
        weisungMeldung = null;
        weisungMeldungRaw = null;
    }

    public boolean isGegenantraegeVorhanden() {
        if (gegenantraegeListeM == null || gegenantraegeListeM.size() == 0) {
            return false;
        }
        gegenantraegeVorhanden = true;
        return gegenantraegeVorhanden;
    }

    /*************Standard setter und getter***************************/

    public List<EclAbstimmungM> getAbstimmungenListeM() {
        return abstimmungenListeM;
    }

    public void setAbstimmungenListeM(List<EclAbstimmungM> abstimmungenListeM) {
        this.abstimmungenListeM = abstimmungenListeM;
    }

    public List<EclAbstimmungM> getGegenantraegeListeM() {
        return gegenantraegeListeM;
    }

    public void setGegenantraegeListeM(List<EclAbstimmungM> gegenantraegeListeM) {
        this.gegenantraegeListeM = gegenantraegeListeM;
    }

    public void setGegenantraegeVorhanden(boolean gegenantraegeVorhanden) {
        this.gegenantraegeVorhanden = gegenantraegeVorhanden;
    }

    public EclWeisungMeldung getWeisungMeldung() {
        return weisungMeldung;
    }

    public void setWeisungMeldung(EclWeisungMeldung weisungMeldung) {
        this.weisungMeldung = weisungMeldung;
    }

    public EclWeisungMeldungRaw getWeisungMeldungRaw() {
        return weisungMeldungRaw;
    }

    public void setWeisungMeldungRaw(EclWeisungMeldungRaw weisungMeldungRaw) {
        this.weisungMeldungRaw = weisungMeldungRaw;
    }

    public int getAlternative() {
        return alternative;
    }

    public void setAlternative(int alternative) {
        this.alternative = alternative;
    }

}
