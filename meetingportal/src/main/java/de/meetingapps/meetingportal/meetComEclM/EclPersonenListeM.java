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
public class EclPersonenListeM implements Serializable {
    private static final long serialVersionUID = 3559491330326348943L;

    private List<EclVirtuellerTeilnehmerM> listePersonenZurAuswahl;

    private int ausgewaehlterTeilnehmerIdent = 0;

    private EclVirtuellerTeilnehmerM aktuellerTeilnehmer = null;

    public void clear() {
        ausgewaehlterTeilnehmerIdent = 0;
        aktuellerTeilnehmer = null;
    }

    /******************Standard getter und setter*************************************/

    public List<EclVirtuellerTeilnehmerM> getListePersonenZurAuswahl() {
        return listePersonenZurAuswahl;
    }

    public void setListePersonenZurAuswahl(List<EclVirtuellerTeilnehmerM> listePersonenZurAuswahl) {
        this.listePersonenZurAuswahl = listePersonenZurAuswahl;
    }

    public int getAusgewaehlterTeilnehmerIdent() {
        return ausgewaehlterTeilnehmerIdent;
    }

    public void setAusgewaehlterTeilnehmerIdent(int ausgewaehlterTeilnehmerIdent) {
        this.ausgewaehlterTeilnehmerIdent = ausgewaehlterTeilnehmerIdent;
    }

    public EclVirtuellerTeilnehmerM getAktuellerTeilnehmer() {
        return aktuellerTeilnehmer;
    }

    public void setAktuellerTeilnehmer(EclVirtuellerTeilnehmerM aktuellerTeilnehmer) {
        this.aktuellerTeilnehmer = aktuellerTeilnehmer;
    }

}
