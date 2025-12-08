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
import java.util.List;

import de.meetingapps.meetingportal.meetComEclM.EclAbstimmungMitVorschlagM;
import de.meetingapps.meetingportal.meetComEclM.EclNachrichtM;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

@SessionScoped
@Named
public class UWeisungsEmpfehlungAntwortSession implements Serializable {
    private static final long serialVersionUID = -1816175206135017570L;

    private String aufrufMaske;

    private EclNachrichtM nachrichtM;

    private List<EclAbstimmungMitVorschlagM> toMitVorschlagListe = null;

    /**1=ja, 0=nein*/
    private String empfehlungUebernehmen = "1";

    /********************Standard getter und setter*****************************/

    public List<EclAbstimmungMitVorschlagM> getToMitVorschlagListe() {
        return toMitVorschlagListe;
    }

    public void setToMitVorschlagListe(List<EclAbstimmungMitVorschlagM> toMitVorschlagListe) {
        this.toMitVorschlagListe = toMitVorschlagListe;
    }

    public String getEmpfehlungUebernehmen() {
        return empfehlungUebernehmen;
    }

    public void setEmpfehlungUebernehmen(String empfehlungUebernehmen) {
        this.empfehlungUebernehmen = empfehlungUebernehmen;
    }

    public EclNachrichtM getNachrichtM() {
        return nachrichtM;
    }

    public void setNachrichtM(EclNachrichtM nachrichtM) {
        this.nachrichtM = nachrichtM;
    }

    public String getAufrufMaske() {
        return aufrufMaske;
    }

    public void setAufrufMaske(String aufrufMaske) {
        this.aufrufMaske = aufrufMaske;
    }

}
