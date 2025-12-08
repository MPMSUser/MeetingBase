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

import de.meetingapps.meetingportal.meetComEclM.EclVeranstaltungM;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

@SessionScoped
@Named
@Deprecated
public class AControllerDialogveranstaltungenSession implements Serializable {
    private static final long serialVersionUID = -1072228811792773733L;

    private boolean istAngemeldet = false;

    private String anzPersonen = "0";

    private EclVeranstaltungM zurVeranstaltungAngemeldet = null;

    private List<EclVeranstaltungM> veranstaltungen = null;

    private String ausgewaehlt = "";

    /*******************Standard getter und setter***************************/

    public boolean isIstAngemeldet() {
        return istAngemeldet;
    }

    public void setIstAngemeldet(boolean istAngemeldet) {
        this.istAngemeldet = istAngemeldet;
    }

    public String getAnzPersonen() {
        return anzPersonen;
    }

    public void setAnzPersonen(String anzPersonen) {
        this.anzPersonen = anzPersonen;
    }

    public EclVeranstaltungM getZurVeranstaltungAngemeldet() {
        return zurVeranstaltungAngemeldet;
    }

    public void setZurVeranstaltungAngemeldet(EclVeranstaltungM zurVeranstaltungAngemeldet) {
        this.zurVeranstaltungAngemeldet = zurVeranstaltungAngemeldet;
    }

    public List<EclVeranstaltungM> getVeranstaltungen() {
        return veranstaltungen;
    }

    public void setVeranstaltungen(List<EclVeranstaltungM> veranstaltungen) {
        this.veranstaltungen = veranstaltungen;
    }

    public String getAusgewaehlt() {
        return ausgewaehlt;
    }

    public void setAusgewaehlt(String ausgewaehlt) {
        this.ausgewaehlt = ausgewaehlt;
    }

}
