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

import java.util.Objects;

public class EclImportAdresse {

    private int ident = 0;

    private String adresse = "";

    private String strasse = "";

    private String adresszusatz = "";

    private String postleitzahl = "";

    private String ort = "";

    private String staat = "";

    public EclImportAdresse() {

    }

    public EclImportAdresse(int ident, String adresse, String strasse, String adresszusatz, String postleitzahl,
            String ort, String staat) {
        super();
        this.ident = ident;
        this.adresse = adresse;
        this.strasse = strasse;
        this.adresszusatz = adresszusatz;
        this.postleitzahl = postleitzahl;
        this.ort = ort;
        this.staat = staat;
    }

    public int getIdent() {
        return ident;
    }

    public void setIdent(int ident) {
        this.ident = ident;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getStrasse() {
        return strasse;
    }

    public void setStrasse(String strasse) {
        this.strasse = strasse;
    }

    public String getAdresszusatz() {
        return adresszusatz;
    }

    public void setAdresszusatz(String adresszusatz) {
        this.adresszusatz = adresszusatz;
    }

    public String getPostleitzahl() {
        return postleitzahl;
    }

    public void setPostleitzahl(String postleitzahl) {
        this.postleitzahl = postleitzahl;
    }

    public String getOrt() {
        return ort;
    }

    public void setOrt(String ort) {
        this.ort = ort;
    }

    public String getStaat() {
        return staat;
    }

    public void setStaat(String staat) {
        this.staat = staat;
    }

    @Override
    public int hashCode() {
        return Objects.hash(adresse, adresszusatz, ident, ort, postleitzahl, staat, strasse);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        EclImportAdresse other = (EclImportAdresse) obj;
        return Objects.equals(adresse, other.adresse) && Objects.equals(adresszusatz, other.adresszusatz)
                && ident == other.ident && Objects.equals(ort, other.ort)
                && Objects.equals(postleitzahl, other.postleitzahl) && Objects.equals(staat, other.staat)
                && Objects.equals(strasse, other.strasse);
    }

    @Override
    public String toString() {
        return "EclImportAdresse [ident=" + ident + ", adresse=" + adresse + ", strasse=" + strasse + ", adresszusatz="
                + adresszusatz + ", postleitzahl=" + postleitzahl + ", ort=" + ort + ", staat=" + staat + "]";
    }

}
