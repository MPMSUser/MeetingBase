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

import de.meetingapps.meetingportal.meetComEntities.EclEmittenten;

/**Einzelne Elemente für die Liste zum Auswählen der Mandanten*/
public class EclMandantAuswahlElementM implements Serializable {
    private static final long serialVersionUID = 3580706908214118637L;

    private int lfdIdent = 0;
    private int mandant = 0;
    private int hvJahr = 0;
    private String hvNummer = "A";
    private String dbArt = "P";
    private String bezeichnungKurz = "";
    private String bezeichnungLang = "";
    private String hvDatum = "";
    private String hvOrt = "";

    private String anzeigeString = "";

    /**pLfdIdent=Laufende Nummer in der Liste, dient zur Identifikation bei
     * der Auswahl
     */
    public void copyFrom(EclEmittenten pEclEmittenten, int pLfdIdent) {
        mandant = pEclEmittenten.mandant;
        hvJahr = pEclEmittenten.hvJahr;
        hvNummer = pEclEmittenten.hvNummer;
        dbArt = pEclEmittenten.dbArt;
        bezeichnungKurz = pEclEmittenten.bezeichnungKurz;
        bezeichnungLang = pEclEmittenten.bezeichnungLang;
        hvDatum = pEclEmittenten.hvDatum;
        hvOrt = pEclEmittenten.hvOrt;
        lfdIdent = pLfdIdent;
        fuelleAnzeigeString();
    }

    private void fuelleAnzeigeString() {
        anzeigeString = bezeichnungKurz + " - HV am: " + hvDatum + " (" + mandant + "/" + hvJahr + "/" + hvNummer + "/"
                + dbArt + ")";
    }

    /**********Standard getter und setter, aber setter teilweise verändert mit fuelleAnzeigeString******************/
    public int getMandant() {
        return mandant;
    }

    public void setMandant(int mandant) {
        this.mandant = mandant;
        fuelleAnzeigeString();
    }

    public int getHvJahr() {
        return hvJahr;
    }

    public void setHvJahr(int hvJahr) {
        this.hvJahr = hvJahr;
        fuelleAnzeigeString();
    }

    public String getHvNummer() {
        return hvNummer;
    }

    public void setHvNummer(String hvNummer) {
        this.hvNummer = hvNummer;
        fuelleAnzeigeString();
    }

    public String getDbArt() {
        return dbArt;
    }

    public void setDbArt(String dbArt) {
        this.dbArt = dbArt;
        fuelleAnzeigeString();
    }

    public String getBezeichnungKurz() {
        return bezeichnungKurz;
    }

    public void setBezeichnungKurz(String bezeichnungKurz) {
        this.bezeichnungKurz = bezeichnungKurz;
        fuelleAnzeigeString();
    }

    public String getBezeichnungLang() {
        return bezeichnungLang;
    }

    public void setBezeichnungLang(String bezeichnungLang) {
        this.bezeichnungLang = bezeichnungLang;
    }

    public String getHvDatum() {
        return hvDatum;
    }

    public void setHvDatum(String hvDatum) {
        this.hvDatum = hvDatum;
        fuelleAnzeigeString();
    }

    public String getHvOrt() {
        return hvOrt;
    }

    public void setHvOrt(String hvOrt) {
        this.hvOrt = hvOrt;
    }

    public int getLfdIdent() {
        return lfdIdent;
    }

    public void setLfdIdent(int lfdIdent) {
        this.lfdIdent = lfdIdent;
    }

    public String getAnzeigeString() {
        return anzeigeString;
    }

    public void setAnzeigeString(String anzeigeString) {
        this.anzeigeString = anzeigeString;
    }

}
