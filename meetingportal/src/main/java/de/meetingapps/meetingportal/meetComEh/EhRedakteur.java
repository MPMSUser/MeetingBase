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
package de.meetingapps.meetingportal.meetComEh;

import java.io.Serializable;

import de.meetingapps.meetingportal.meetComEntities.EclEmittenten;

public class EhRedakteur  implements Serializable {
    private static final long serialVersionUID = 1747468136481367765L;
    
    public int mandant = 0;
    public int hvJahr = 0;
    public String hvNummer = "A";
    public String dbArt = "P";
    public String bezeichnungKurz = "";
    public String bezeichnungLang = "";
    public String hvDatum = "";

    public String mailConsultant="";
    public int anzahlFragen=0;

    public EhRedakteur(EclEmittenten pEmittent) {
        mandant=pEmittent.mandant; 
        hvJahr=pEmittent.hvJahr; 
        hvNummer=pEmittent.hvNummer; 
        dbArt=pEmittent.dbArt; 
        bezeichnungKurz=pEmittent.bezeichnungKurz; 
        bezeichnungLang=pEmittent.bezeichnungLang; 
        hvDatum=pEmittent.hvDatum; 
    }
    
    /********************Standard setter und getter*************************************/
    public int getMandant() {
        return mandant;
    }

    public void setMandant(int mandant) {
        this.mandant = mandant;
    }

    public int getHvJahr() {
        return hvJahr;
    }

    public void setHvJahr(int hvJahr) {
        this.hvJahr = hvJahr;
    }

    public String getHvNummer() {
        return hvNummer;
    }

    public void setHvNummer(String hvNummer) {
        this.hvNummer = hvNummer;
    }

    public String getDbArt() {
        return dbArt;
    }

    public void setDbArt(String dbArt) {
        this.dbArt = dbArt;
    }

    public String getBezeichnungKurz() {
        return bezeichnungKurz;
    }

    public void setBezeichnungKurz(String bezeichnungKurz) {
        this.bezeichnungKurz = bezeichnungKurz;
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
    }

    public int getAnzahlFragen() {
        return anzahlFragen;
    }

    public void setAnzahlFragen(int anzahlFragen) {
        this.anzahlFragen = anzahlFragen;
    }

    public String getMailConsultant() {
        return mailConsultant;
    }

    public void setMailConsultant(String mailConsultant) {
        this.mailConsultant = mailConsultant;
    }
    
    
    
    
}
