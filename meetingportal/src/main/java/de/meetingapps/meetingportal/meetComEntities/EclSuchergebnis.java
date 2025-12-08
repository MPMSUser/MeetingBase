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

public class EclSuchergebnis implements Serializable {
    private static final long serialVersionUID = 6285962260505137122L;
    
    /*Anzeige Grid
     * Auswählen-Button
     * Aktionärsnummer
     * Ek-Nr
     * SK-Nr
     * Präsent
     * Stimmen
     * NameAktonär
     * VornameAktionär
     * OrtAktionär
     * NameVertreter
     * VornameVertreter
     * OrtVertreter
     */
    public int aktienregisterIdent=0;
    public String aktionaersnummer = "";
    public int meldungKlasse = 0;
    public int meldungstyp = 0;
    public int meldungsIdent = 0;
    public String zutrittsIdent = "";
    public String zutrittsIdentNeben = "";
    public int zutrittsIdentGeperrt = 0;
    public String stimmkartenIdent = "";
    public int praesent = 0;
    public long stimmen = 0;
    public String aktionaerName = "";
    public String aktionaerVorname = "";
    public String aktionaerOrt = "";
    public String vertreterName = "";
    public String vertreterVorname = "";
    public String vertreterOrt = "";
    public String nameKomplett = "";

    public String zusatzfeld2 = "";
    public String zusatzfeld3 = "";
    public String zusatzfeld4 = "";
    
    
    /****************************Standard getter und setter************************************************/
    
    public String getAktionaersnummer() {
        return aktionaersnummer;
    }
    public void setAktionaersnummer(String aktionaersnummer) {
        this.aktionaersnummer = aktionaersnummer;
    }
    public int getMeldungKlasse() {
        return meldungKlasse;
    }
    public void setMeldungKlasse(int meldungKlasse) {
        this.meldungKlasse = meldungKlasse;
    }
    public int getMeldungstyp() {
        return meldungstyp;
    }
    public void setMeldungstyp(int meldungstyp) {
        this.meldungstyp = meldungstyp;
    }
    public int getMeldungsIdent() {
        return meldungsIdent;
    }
    public void setMeldungsIdent(int meldungsIdent) {
        this.meldungsIdent = meldungsIdent;
    }
    public String getZutrittsIdent() {
        return zutrittsIdent;
    }
    public void setZutrittsIdent(String zutrittsIdent) {
        this.zutrittsIdent = zutrittsIdent;
    }
    public String getZutrittsIdentNeben() {
        return zutrittsIdentNeben;
    }
    public void setZutrittsIdentNeben(String zutrittsIdentNeben) {
        this.zutrittsIdentNeben = zutrittsIdentNeben;
    }
    public int getZutrittsIdentGeperrt() {
        return zutrittsIdentGeperrt;
    }
    public void setZutrittsIdentGeperrt(int zutrittsIdentGeperrt) {
        this.zutrittsIdentGeperrt = zutrittsIdentGeperrt;
    }
    public String getStimmkartenIdent() {
        return stimmkartenIdent;
    }
    public void setStimmkartenIdent(String stimmkartenIdent) {
        this.stimmkartenIdent = stimmkartenIdent;
    }
    public int getPraesent() {
        return praesent;
    }
    public void setPraesent(int praesent) {
        this.praesent = praesent;
    }
    public long getStimmen() {
        return stimmen;
    }
    public void setStimmen(long stimmen) {
        this.stimmen = stimmen;
    }
    public String getAktionaerName() {
        return aktionaerName;
    }
    public void setAktionaerName(String aktionaerName) {
        this.aktionaerName = aktionaerName;
    }
    public String getAktionaerVorname() {
        return aktionaerVorname;
    }
    public void setAktionaerVorname(String aktionaerVorname) {
        this.aktionaerVorname = aktionaerVorname;
    }
    public String getAktionaerOrt() {
        return aktionaerOrt;
    }
    public void setAktionaerOrt(String aktionaerOrt) {
        this.aktionaerOrt = aktionaerOrt;
    }
    public String getVertreterName() {
        return vertreterName;
    }
    public void setVertreterName(String vertreterName) {
        this.vertreterName = vertreterName;
    }
    public String getVertreterVorname() {
        return vertreterVorname;
    }
    public void setVertreterVorname(String vertreterVorname) {
        this.vertreterVorname = vertreterVorname;
    }
    public String getVertreterOrt() {
        return vertreterOrt;
    }
    public void setVertreterOrt(String vertreterOrt) {
        this.vertreterOrt = vertreterOrt;
    }
    public String getNameKomplett() {
        return nameKomplett;
    }
    public void setNameKomplett(String nameKomplett) {
        this.nameKomplett = nameKomplett;
    }
    public String getZusatzfeld2() {
        return zusatzfeld2;
    }
    public void setZusatzfeld2(String zusatzfeld2) {
        this.zusatzfeld2 = zusatzfeld2;
    }
    public String getZusatzfeld3() {
        return zusatzfeld3;
    }
    public void setZusatzfeld3(String zusatzfeld3) {
        this.zusatzfeld3 = zusatzfeld3;
    }
    public String getZusatzfeld4() {
        return zusatzfeld4;
    }
    public void setZusatzfeld4(String zusatzfeld4) {
        this.zusatzfeld4 = zusatzfeld4;
    }

}
