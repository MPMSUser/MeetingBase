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

/**Verwendung für Permanent-Portal*/
public class EclAktienregisterWeiterePerson  implements Serializable {
    private static final long serialVersionUID = -3724669409956327728L;

    public int ident=0;
    
    public String vorname="";
    public String nachname="";
    
    public String liefereNameKomplett() {
        String h=nachname;
        if (!vorname.isEmpty()) {
            h=vorname+" "+h;
        }
        return h;
    }
    
    /**geburtsdatum und steuerId für Personendaten zum Mitglied*/
    public String geburtsdatum="";
    public String steuerId="";
    
    /**Adresse für Vollmachten/Postempfänger*/
    public String strasse="";
    public String plz="";
    public String ort="";
    public String land="";
    
    public String personenArt="";
    
    public boolean steuerIdDarfGeaendertWerden() {
        return steuerId.trim().isEmpty();
    }
    
    public boolean geburtsdatumDarfGeaendertWerden() {
        return geburtsdatum.trim().isEmpty();
    }

    /*******************************************standard getter und setter**************************/
    
    public int getIdent() {
        return ident;
    }
    public void setIdent(int ident) {
        this.ident = ident;
    }
    public String getVorname() {
        return vorname;
    }
    public void setVorname(String vorname) {
        this.vorname = vorname;
    }
    public String getNachname() {
        return nachname;
    }
    public void setNachname(String nachname) {
        this.nachname = nachname;
    }
    public String getGeburtsdatum() {
        return geburtsdatum;
    }
    public void setGeburtsdatum(String geburtsdatum) {
        this.geburtsdatum = geburtsdatum;
    }
    public String getSteuerId() {
        return steuerId;
    }
    public void setSteuerId(String steuerId) {
        this.steuerId = steuerId;
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
    public String getPersonenArt() {
        return personenArt;
    }
    public void setPersonenArt(String personenArt) {
        this.personenArt = personenArt;
    }

    public String getLand() {
        return land;
    }

    public void setLand(String land) {
        this.land = land;
    }
    
    
    
    
}
