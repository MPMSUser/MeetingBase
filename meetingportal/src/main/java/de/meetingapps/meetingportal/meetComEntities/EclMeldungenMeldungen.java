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

/** Verknüpfung von einer Meldung zu einer anderen Meldung - nicht aktienrechtlich bzw. virtuelle Karten, 
 * sondern z.B. (fachlich formuliert): zweite Gastkarte wurde ausgestellt zu einer Eintrittskarte.
 * Technisch grundsätzlich n:m möglich, im Hinblick auf verschiedene "Zuordnungsgründe" (z.B.
 * einmal für Online-Teilnahme, einmal für gemeinsame Verwaltung im Bankenportal o.ä.) möglicherweise
 * auch durchaus sinnvoll
 */

public class EclMeldungenMeldungen implements Serializable {
    private static final long serialVersionUID = 3234068450970763381L;

    public int mandant = 0;
    public int vonMeldungsIdent = 0;
    public int zuMeldungsIdent = 0;

    /** Art / Verwendung der Zuordnung. Z.B. später für Online-Teilnahme-Bündelung, Bündelung im Bankenportal etc..
     * Inhalt über EnMeldungenMeldungen.
     * ==1 => Gruppengastkarte
     * ==2 => Gastkarte, die vom Aktionär angefordert wurde; in "vonMeldungsIdent" steht AktienregisterIdent des anfordernden Aktionärs.
     * 
     * 
     * Selbe Zahl <0 => wurde storniert
     */
    public int verwendung = 0;

    /** Wird nicht gespeichert  nur zum temporären Halten der Information für die Anzeige*/
    public String zutrittsIdent = "";
    public String name = "";
    public String vorname = "";
    public String ort = "";
    public String mailAdresse="";
    
    public boolean liefereMailAdresseIstVorhanden() {
        return mailAdresse.isEmpty()==false;
    }

    
    /*****************************Standard getter und setter*************************************************/
    
    
    public int getMandant() {
        return mandant;
    }

    public void setMandant(int mandant) {
        this.mandant = mandant;
    }

    public int getVonMeldungsIdent() {
        return vonMeldungsIdent;
    }

    public void setVonMeldungsIdent(int vonMeldungsIdent) {
        this.vonMeldungsIdent = vonMeldungsIdent;
    }

    public int getZuMeldungsIdent() {
        return zuMeldungsIdent;
    }

    public void setZuMeldungsIdent(int zuMeldungsIdent) {
        this.zuMeldungsIdent = zuMeldungsIdent;
    }

    public int getVerwendung() {
        return verwendung;
    }

    public void setVerwendung(int verwendung) {
        this.verwendung = verwendung;
    }

    public String getZutrittsIdent() {
        return zutrittsIdent;
    }

    public void setZutrittsIdent(String zutrittsIdent) {
        this.zutrittsIdent = zutrittsIdent;
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


    public String getMailAdresse() {
        return mailAdresse;
    }


    public void setMailAdresse(String mailAdresse) {
        this.mailAdresse = mailAdresse;
    }

    
    
    
}




