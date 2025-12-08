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
package de.meetingapps.meetingportal.meetingportTController;

import java.io.Serializable;
import java.util.List;

import de.meetingapps.meetingportal.meetComAllg.CaDatumZeit;
import de.meetingapps.meetingportal.meetComEntities.EclAktienregisterBestandshistorieeintrag;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

@SessionScoped
@Named
public class PBestandhistorieSession implements Serializable {
    private static final long serialVersionUID = 2667865593496274076L;

    /******************Aktionär seit*************************/
    private String aktionaerSeit = "";

    /******************Bestandhistorie***********************/
    private List<EclAktienregisterBestandshistorieeintrag> bestandshistorie = null;

    /**
     * 1=alle Post 2=aktuelles Jahr 3=zeitraum
     */
    private String umsatzzeitraum = "3";

    /**
     * Zeitram der Abfrage
     */
    private String datumVon = "";
    private String datumBis = "";

    private String datumVonMobil = "";
    private String datumBisMobil = "";

    /**Setzt alle Werte auf Standardwerte zurück*/
    public void clear() {
        aktionaerSeit = "";
        bestandshistorie = null;
        umsatzzeitraum = "3";
        datumVon = CaDatumZeit.DatumStringFuerRequest();
        datumBis = CaDatumZeit.DatumStringFuerRequest();
        datumVonMobil = CaDatumZeit.DatumStringFuerRequest();
        datumBisMobil = CaDatumZeit.DatumStringFuerRequest();
    }

    public Boolean umsaetzeVorhanden() {
        if (bestandshistorie != null) {
            if (bestandshistorie.size() > 0) {
                return true;
            }
        }
        return false;
    }

    /*******************Spezial getter und setter**************************************/

    public String getUmsatzzeitraumText() {
        switch (this.umsatzzeitraum) {
        case "1":
            return "Alle Umsätze";

        case "2":
            return "Aktuelles Jahr";

        case "3":
            return "von " + CaDatumZeit.datumJJJJ_MM_TTzuNormal(this.datumVon) + " bis " + CaDatumZeit.datumJJJJ_MM_TTzuNormal(this.datumBis);

        default:
            return "";
        }
    }

    public String getDatumVon() {
        return CaDatumZeit.datumJJJJ_MM_TTzuNormal(datumVon);
    }

    public String getDatumVonMobil() {
        return CaDatumZeit.datumJJJJ_MM_TTzuNormal(datumVonMobil);
    }

    public String getDatumVonRequest() {
        return datumVon;
    }

    public String getDatumBisRequest() {
        return datumBis;
    }

    public String getDatumVonRequestMobil() {
        return datumVonMobil;
    }

    public String getDatumBisRequestMobil() {
        return datumBisMobil;
    }

    public void setDatumVon(String datumVon) {
        this.datumVon = CaDatumZeit.datumNormalZuJJJJ_MM_TT(datumVon);
    }

    public void setDatumVonMobil(String datumVon) {
        this.datumVonMobil = CaDatumZeit.datumNormalZuJJJJ_MM_TT(datumVon);
    }

    public String getDatumBis() {
        return CaDatumZeit.datumJJJJ_MM_TTzuNormal(datumBis);
    }

    public String getDatumBisMobil() {
        return CaDatumZeit.datumJJJJ_MM_TTzuNormal(datumBisMobil);
    }

    public void setDatumBis(String datumBis) {
        this.datumBis = CaDatumZeit.datumNormalZuJJJJ_MM_TT(datumBis);
    }

    public void setDatumBisMobil(String datumBis) {
        this.datumBisMobil = CaDatumZeit.datumNormalZuJJJJ_MM_TT(datumBis);
    }

    /*******************Standard getter und setter*************************************/

    public String getAktionaerSeit() {
        return aktionaerSeit;
    }

    public void setAktionaerSeit(String aktionaerSeit) {
        this.aktionaerSeit = aktionaerSeit;
    }

    public List<EclAktienregisterBestandshistorieeintrag> getBestandshistorie() {
        return bestandshistorie;
    }

    public void setBestandshistorie(List<EclAktienregisterBestandshistorieeintrag> bestandshistorie) {
        this.bestandshistorie = bestandshistorie;
    }

    public String getUmsatzzeitraum() {
        return umsatzzeitraum;
    }

    public void setUmsatzzeitraum(String umsatzzeitraum) {
        this.umsatzzeitraum = umsatzzeitraum;
    }

}
