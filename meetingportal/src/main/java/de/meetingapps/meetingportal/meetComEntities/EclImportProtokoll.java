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

import java.sql.Timestamp;

public class EclImportProtokoll {

    private int ident = 0;

    private int userLoginIdent;

    private int ekVon;

    private int ekBis;

    private String name;

    private String datei;

    private Timestamp update;

    public EclImportProtokoll() {

    }

    public EclImportProtokoll(int ident, int userLoginIdent, int ekVon, int ekBis, String name, String datei,
            Timestamp update) {
        super();
        this.ident = ident;
        this.userLoginIdent = userLoginIdent;
        this.ekVon = ekVon;
        this.ekBis = ekBis;
        this.name = name;
        this.datei = datei;
        this.update = update;
    }

    public int getIdent() {
        return ident;
    }

    public void setIdent(int ident) {
        this.ident = ident;
    }

    public int getUserLoginIdent() {
        return userLoginIdent;
    }

    public void setUserLoginIdent(int userLoginIdent) {
        this.userLoginIdent = userLoginIdent;
    }

    public int getEkVon() {
        return ekVon;
    }

    public void setEkVon(int ekVon) {
        this.ekVon = ekVon;
    }

    public int getEkBis() {
        return ekBis;
    }

    public void setEkBis(int ekBis) {
        this.ekBis = ekBis;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDatei() {
        return datei;
    }

    public void setDatei(String datei) {
        this.datei = datei;
    }

    public Timestamp getUpdate() {
        return update;
    }

    public void setUpdate(Timestamp update) {
        this.update = update;
    }

    @Override
    public String toString() {
        return "EclImportProtokoll [ident=" + ident + ", userLoginIdent=" + userLoginIdent + ", ekBankStart="
                + ekVon + ", ekBankEnde=" + ekBis + ", name=" + name + ", datei=" + datei + ", update="
                + update + "]";
    }

    public String searchString() {
        return userLoginIdent + " " + ekVon + " " + ekBis + " " + name;
    }

}
