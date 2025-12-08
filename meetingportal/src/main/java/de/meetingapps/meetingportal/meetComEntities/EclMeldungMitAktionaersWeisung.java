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

import de.meetingapps.meetingportal.meetComKonst.KonstStimmart;

/**Hilfsklasse (kein Pendant in Datenbank): "Kurzfassung" von Aktionärsdaten und zugehörigem Weisungsstring*/
public class EclMeldungMitAktionaersWeisung {

    public int meldungsIdent = 0;
    public String aktionaersnummer = "";
    public int gattung = 0;
    public long stueckAktien = 0;
    public long stimmen = 0;
    public String besitzart = "";
    public String stimmausschluss = "";

    public String name = "";
    public String vorname = "";
    public String ort = "";
    public String zutrittsIdent = "";
    public int[] abgabe = new int[200];

    public String[] abgabeText = new String[201];
    public EclWeisungMeldung eclWeisungMeldung = null;;

    public EclMeldungMitAktionaersWeisung() {
        for (int i = 0; i < 200; i++) {
            abgabe[i] = 0;
        }
    }

    public void belegeAbgabeText() {
        for (int i = 0; i < 200; i++) {
            abgabeText[i] = KonstStimmart.getTextKurz(abgabe[i]);
        }
        abgabeText[200] = "";
    }

    /**********Sonder-Getter******/
    public String[] getAbgabeText() {
        return abgabeText;
    }

    /*************StandardGetterUndSetter****************/
    public int getMeldungsIdent() {
        return meldungsIdent;
    }

    public void setMeldungsIdent(int meldungsIdent) {
        this.meldungsIdent = meldungsIdent;
    }

    public String getAktionaersnummer() {
        return aktionaersnummer;
    }

    public void setAktionaersnummer(String aktionaersnummer) {
        this.aktionaersnummer = aktionaersnummer;
    }

    public int getGattung() {
        return gattung;
    }

    public void setGattung(int gattung) {
        this.gattung = gattung;
    }

    public long getStueckAktien() {
        return stueckAktien;
    }

    public void setStueckAktien(long stueckAktien) {
        this.stueckAktien = stueckAktien;
    }

    public long getStimmen() {
        return stimmen;
    }

    public void setStimmen(long stimmen) {
        this.stimmen = stimmen;
    }

    public String getBesitzart() {
        return besitzart;
    }

    public void setBesitzart(String besitzart) {
        this.besitzart = besitzart;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVorname() {
        return vorname;
    }

    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

    public String getOrt() {
        return ort;
    }

    public void setOrt(String ort) {
        this.ort = ort;
    }

    public String getZutrittsIdent() {
        return zutrittsIdent;
    }

    public void setZutrittsIdent(String zutrittsIdent) {
        this.zutrittsIdent = zutrittsIdent;
    }

    public String getStimmausschluss() {
        return stimmausschluss;
    }

    public void setStimmausschluss(String stimmausschluss) {
        this.stimmausschluss = stimmausschluss;
    }

}
