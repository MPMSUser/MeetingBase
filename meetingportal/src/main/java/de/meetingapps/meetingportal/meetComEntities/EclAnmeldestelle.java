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

public class EclAnmeldestelle {

    private int ident = 0;

    private String name;

    private String strasse;

    private String plz;

    private String ort;

    private String land;

    private String telefon;

    private String fax;

    private String email;

    private String notiz;

    private Timestamp zuletztGeandert;

    public EclAnmeldestelle() {

    }

    public EclAnmeldestelle(int ident, String name, String strasse, String plz, String ort, String land, String telefon,
            String fax, String email, String notiz, Timestamp zuletztGeandert) {
        super();
        this.ident = ident;
        this.name = name;
        this.strasse = strasse;
        this.plz = plz;
        this.ort = ort;
        this.land = land;
        this.telefon = telefon;
        this.fax = fax;
        this.email = email;
        this.notiz = notiz;
        this.zuletztGeandert = zuletztGeandert;
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

    public String getStrasse() {
        return strasse;
    }

    public void setStrasse(String strasse) {
        this.strasse = strasse;
    }

    public String getPlz() {
        return plz;
    }

    public void setPlz(String plz) {
        this.plz = plz;
    }

    public String getOrt() {
        return ort;
    }

    public void setOrt(String ort) {
        this.ort = ort;
    }

    public String getLand() {
        return land;
    }

    public void setLand(String land) {
        this.land = land;
    }

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNotiz() {
        return notiz;
    }

    public void setNotiz(String notiz) {
        this.notiz = notiz;
    }

    public Timestamp getZuletztGeandert() {
        return zuletztGeandert;
    }

    public void setZuletztGeandert(Timestamp zuletztGeandert) {
        this.zuletztGeandert = zuletztGeandert;
    }

    @Override
    public String toString() {
        return "EclAnmeldestelle [ident=" + ident + ", name=" + name + ", strasse=" + strasse + ", plz=" + plz
                + ", ort=" + ort + ", land=" + land + ", telefon=" + telefon + ", fax=" + fax + ", email=" + email
                + ", notiz=" + notiz + ", zuletztGeandert=" + zuletztGeandert + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((email == null) ? 0 : email.hashCode());
        result = prime * result + ((fax == null) ? 0 : fax.hashCode());
        result = prime * result + ident;
        result = prime * result + ((land == null) ? 0 : land.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((notiz == null) ? 0 : notiz.hashCode());
        result = prime * result + ((ort == null) ? 0 : ort.hashCode());
        result = prime * result + ((plz == null) ? 0 : plz.hashCode());
        result = prime * result + ((strasse == null) ? 0 : strasse.hashCode());
        result = prime * result + ((telefon == null) ? 0 : telefon.hashCode());
        result = prime * result + ((zuletztGeandert == null) ? 0 : zuletztGeandert.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        EclAnmeldestelle other = (EclAnmeldestelle) obj;
        if (email == null) {
            if (other.email != null)
                return false;
        } else if (!email.equals(other.email))
            return false;
        if (fax == null) {
            if (other.fax != null)
                return false;
        } else if (!fax.equals(other.fax))
            return false;
        if (ident != other.ident)
            return false;
        if (land == null) {
            if (other.land != null)
                return false;
        } else if (!land.equals(other.land))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (notiz == null) {
            if (other.notiz != null)
                return false;
        } else if (!notiz.equals(other.notiz))
            return false;
        if (ort == null) {
            if (other.ort != null)
                return false;
        } else if (!ort.equals(other.ort))
            return false;
        if (plz == null) {
            if (other.plz != null)
                return false;
        } else if (!plz.equals(other.plz))
            return false;
        if (strasse == null) {
            if (other.strasse != null)
                return false;
        } else if (!strasse.equals(other.strasse))
            return false;
        if (telefon == null) {
            if (other.telefon != null)
                return false;
        } else if (!telefon.equals(other.telefon))
            return false;
        if (zuletztGeandert == null) {
            if (other.zuletztGeandert != null)
                return false;
        } else if (!zuletztGeandert.equals(other.zuletztGeandert))
            return false;
        return true;
    }

}
