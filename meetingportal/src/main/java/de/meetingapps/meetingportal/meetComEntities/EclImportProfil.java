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
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class EclImportProfil implements Serializable {

    private static final long serialVersionUID = 7456438663493449457L;

    private int ident;

    private String name;

    private String klasse;

    private Timestamp zuletztGeandert;

    private String[] dateiFeld = new String[80];

    private String[] datenbankFeld = new String[80];

    public static int anzFelder = 80;

    public EclImportProfil() {

    }

    public EclImportProfil(int ident, String name, String klasse, Timestamp zuletztGeandert, String[] dateiFeld,
            String[] datenbankFeld) {
        super();
        this.ident = ident;
        this.name = name;
        this.klasse = klasse;
        this.zuletztGeandert = zuletztGeandert;
        this.dateiFeld = dateiFeld;
        this.datenbankFeld = datenbankFeld;
    }

    public int getIdent() {
        return ident;
    }

    public void setIdent(int ident) {
        this.ident = ident;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKlasse() {
        return klasse;
    }

    public void setKlasse(String klasse) {
        this.klasse = klasse;
    }

    public Timestamp getZuletztGeandert() {
        return zuletztGeandert;
    }

    public void setZuletztGeandert(Timestamp zuletztGeandert) {
        this.zuletztGeandert = zuletztGeandert;
    }

    public String[] getDateiFeld() {
        return dateiFeld;
    }

    public void setDateiFeld(String[] dateiFeld) {
        this.dateiFeld = dateiFeld;
    }

    public String[] getDatenbankFeld() {
        return datenbankFeld;
    }

    public void setDatenbankFeld(String[] datenbankFeld) {
        this.datenbankFeld = datenbankFeld;
    }

    public String fullString() {
        return "EclImportProfil [ident=" + ident + ", name=" + name + ", klasse=" + klasse + ", dateiFeld=" + Arrays.toString(dateiFeld)
                + ", datenbankFeld=" + Arrays.toString(datenbankFeld) + ", zuletztGeandert=" + zuletztGeandert + "]";
    }

    @Override
    public String toString() {
        return ident + ", " + name;
    }

    public Map<String, String> createMap() {
        Map<String, String> m = new HashMap<>();

        for (int i = 0; i < this.getDateiFeld().length; i++)
            m.put(this.getDateiFeld()[i], this.getDatenbankFeld()[i]);
        return m;
    }

    //    public static StringConverter getConverter() {
    //
    //        return new StringConverter() {
    //
    //            public String toString(EclImportProfil value) {
    //                return value.getIdent() + " - " + value.getName();
    //            }
    //
    //            public EclImportProfil fromString(String value) {
    //                return new EclImportProfil(value.substring(0), value, null, null, null);
    //            }
    //        };
    //
    //    }

}
