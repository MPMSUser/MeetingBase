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

/**Für Permanentportal. Kündigungen, Käufe, Verkäufe etc.*/
public class EclAktienregisterBestandsaenderungen implements Serializable {
    private static final long serialVersionUID = 6706589155236333445L;

    /**1 = Kündigung*/
    public int artDerAenderung=0;
    
    /**externe id der Kündigung**/
    public long kuendigungId = 0;
    
    /**kuendigung_vom, im Format TT.MM.JJJJ (bzw. im Anzeige-Format)*/
    public String veranlasstAm="";
    
    /**gekuendigte_Anteile Anteile*/
    public int veraenderungAktien=0;
    public String veraenderungAktienDE="";
    
    /**gekuendigt_zum, im Format TT.MM.JJJJ (bzw. im Anzeige-Format)*/
    public String wirksamZum="";
    
    /**Kündigung zurückgenommen*/
    public Boolean laufende_ruecknahme = false;
    
    
    /***************Standard getter und Setter*************************/

    public int getArtDerAenderung() {
        return artDerAenderung;
    }

    public void setArtDerAenderung(int artDerAenderung) {
        this.artDerAenderung = artDerAenderung;
    }


    public int getVeraenderungAktien() {
        return veraenderungAktien;
    }

    public void setVeraenderungAktien(int veraenderungAktien) {
        this.veraenderungAktien = veraenderungAktien;
    }

    public String getVeraenderungAktienDE() {
        return veraenderungAktienDE;
    }

    public void setVeraenderungAktienDE(String veraenderungAktienDE) {
        this.veraenderungAktienDE = veraenderungAktienDE;
    }

    public String getWirksamZum() {
        return wirksamZum;
    }

    public void setWirksamZum(String wirksamZum) {
        this.wirksamZum = wirksamZum;
    }

    public String getVeranlasstAm() {
        return veranlasstAm;
    }

    public void setVeranlasstAm(String veranlasstAm) {
        this.veranlasstAm = veranlasstAm;
    }

    public long getKuendigungId() {
        return kuendigungId;
    }

    public void setKuendigungId(long kuendigungId) {
        this.kuendigungId = kuendigungId;
    }

    public Boolean getLaufende_ruecknahme() {
        return laufende_ruecknahme;
    }

    public void setLaufende_ruecknahme(Boolean laufende_ruecknahme) {
        this.laufende_ruecknahme = laufende_ruecknahme;
    }
    
    
}
