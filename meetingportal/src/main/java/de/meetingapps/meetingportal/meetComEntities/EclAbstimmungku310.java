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

/**Allgemeine Info zum ku310-Ablauf:
 * Meldungen je Mitglied:
 * Erste Meldung = Gattung 1 = UK Unterstützungskasse= ku310 Versorgungskasse des Bankgewerbes e.V.
 * Zweite Meldung = Gattung 2 = PK Pensionskasse = ku310 Versicherungsverein des Bankgewerbes a.G.
 * 
 * 
 * Besitzart:
 * 1=V bei Arbeitgeber
 * 2=V bei Arbeitnehmer
 * 3=V bei beiden
 * 
 * Stimmen=Arbeitgeber (bei Abstimmungsauswertung: Gattung 1)
 * Aktien = Arbeitnehmer (bei Abstimmungsauswertung: Gattung 2)
 * 
 * @author N.N
 * VVBLinkBO!2o24
 *
 * https://ku310.better-orange.de
 *
 * Änderungen:
 * 
 * Neue Mitgliedsnummer:
 * > Auf Server importieren. Achtung! DB-Rücksetzungen müssen abends vorher deaktiviert eingespielt werden!
 * 
 * Mitgliedsnummer verändern:
 * > tbl_logindaten
 * > tbl_aktienregister
 * > tbl_meldungen
 * 
 * Stimmenzahlen verändern:
 * > mitgliedsnummer => AktienregisterIdent
 * > tbl_meldungen Stimmen verändern (Stimmen/Aktien; ggf. 1 und 2 Gattungen)
 * > tbl_abstimmungku310 (alle mit aktienregisterIdent, getrennt nach geberOderNehmer und identWeisungssatz (1-7 = UKJ., 8-12=PK
 * 
 * Mitglied löschen:
 * > Am besten: Abgang buchen, Zugang sperren, zutrittsident rauslöschen und gut ist.
 * > detailliert:
 * >> tbl_logindaten
 * >> tbl_aktienregister
 * >> tbl_meldungen
 * >> tbl_abstimmungku310
 * 
 * Name ändern
 * >> tbl_aktienregister
 * >> personNatJur 
 *
 *
 */


public class EclAbstimmungku310  implements Serializable {
    private static final long serialVersionUID = 509368058541831124L;

    /**Verweis auf Aktionär*/
    public int aktienregisterIdent=0;
    
    /**Verweis auf Abstimmungspunkt*/
    public int identWeisungssatz=0;
    
    /**1=Stimmabgabe wurde durchgeführt*/
    public int stimmabgabeDurchgefuehrt=0;
    
    /**1 = Die Stimmen von der anderen Versammlung wurden übernommen - keine abweichende Stimmabgabe möglich*/
    public int stimmenUebernehmen=0;
    
    /**1=Arbeitgeber, 2=Arbeitnehmer, 3=beide zusammen*/
    public int geberOderNehmer=0;
    
    /**Stimmen für diesen TOP*/
    public long gesamtStimmen=0;
    public long jaStimmen=0;
    public long neinStimmen=0;
    public long enthaltungStimmen=0;
    
    /******************Nicht in Datenbank*******************************/
    public String gesamtStimmenEingabe="";
    public String jaStimmenEingabe="";
    public String neinStimmenEingabe="";
    public String enthaltungStimmenEingabe="";
    
    /**Bereits abgestimmt => true*/
    public boolean aktuellAbgestimmt=false;
    
    /**True => Bestätigungsseite wird angezeigt*/
    public boolean aktuellBestaetigen=false;
    
    
    public void belegeEingabeFelder() {
        gesamtStimmenEingabe=Long.toString(gesamtStimmen);
        jaStimmenEingabe=Long.toString(jaStimmen);
        if (jaStimmenEingabe.equals("0")) {
            jaStimmenEingabe="";
        }
        neinStimmenEingabe=Long.toString(neinStimmen);
        if (neinStimmenEingabe.equals("0")) {
            neinStimmenEingabe="";
        }
        enthaltungStimmenEingabe=Long.toString(enthaltungStimmen);
        if (enthaltungStimmenEingabe.equals("0")) {
            enthaltungStimmenEingabe="";
        }
    }
    
    
    /*********************************Standard getter und setter************************************************/
    
    public int getAktienregisterIdent() {
        return aktienregisterIdent;
    }
    public void setAktienregisterIdent(int aktienregisterIdent) {
        this.aktienregisterIdent = aktienregisterIdent;
    }
    public int getIdentWeisungssatz() {
        return identWeisungssatz;
    }
    public void setIdentWeisungssatz(int identWeisungssatz) {
        this.identWeisungssatz = identWeisungssatz;
    }
    public int getStimmabgabeDurchgefuehrt() {
        return stimmabgabeDurchgefuehrt;
    }
    public void setStimmabgabeDurchgefuehrt(int stimmabgabeDurchgefuehrt) {
        this.stimmabgabeDurchgefuehrt = stimmabgabeDurchgefuehrt;
    }
    public int getStimmenUebernehmen() {
        return stimmenUebernehmen;
    }
    public void setStimmenUebernehmen(int stimmenUebernehmen) {
        this.stimmenUebernehmen = stimmenUebernehmen;
    }
    public int getGeberOderNehmer() {
        return geberOderNehmer;
    }
    public void setGeberOderNehmer(int geberOderNehmer) {
        this.geberOderNehmer = geberOderNehmer;
    }
    public long getGesamtStimmen() {
        return gesamtStimmen;
    }
    public void setGesamtStimmen(long gesamtStimmen) {
        this.gesamtStimmen = gesamtStimmen;
    }
    public long getJaStimmen() {
        return jaStimmen;
    }
    public void setJaStimmen(long jaStimmen) {
        this.jaStimmen = jaStimmen;
    }
    public long getNeinStimmen() {
        return neinStimmen;
    }
    public void setNeinStimmen(long neinStimmen) {
        this.neinStimmen = neinStimmen;
    }
    public long getEnthaltungStimmen() {
        return enthaltungStimmen;
    }
    public void setEnthaltungStimmen(long enthaltungStimmen) {
        this.enthaltungStimmen = enthaltungStimmen;
    }
    public String getJaStimmenEingabe() {
        return jaStimmenEingabe;
    }
    public void setJaStimmenEingabe(String jaStimmenEingabe) {
        this.jaStimmenEingabe = jaStimmenEingabe;
    }
    public String getNeinStimmenEingabe() {
        return neinStimmenEingabe;
    }
    public void setNeinStimmenEingabe(String neinStimmenEingabe) {
        this.neinStimmenEingabe = neinStimmenEingabe;
    }
    public String getEnthaltungStimmenEingabe() {
        return enthaltungStimmenEingabe;
    }
    public void setEnthaltungStimmenEingabe(String enthaltungStimmenEingabe) {
        this.enthaltungStimmenEingabe = enthaltungStimmenEingabe;
    }


    public String getGesamtStimmenEingabe() {
        return gesamtStimmenEingabe;
    }


    public void setGesamtStimmenEingabe(String gesamtStimmenEingabe) {
        this.gesamtStimmenEingabe = gesamtStimmenEingabe;
    }


    public boolean isAktuellAbgestimmt() {
        return aktuellAbgestimmt;
    }


    public void setAktuellAbgestimmt(boolean aktuellAbgestimmt) {
        this.aktuellAbgestimmt = aktuellAbgestimmt;
    }


    public boolean isAktuellBestaetigen() {
        return aktuellBestaetigen;
    }


    public void setAktuellBestaetigen(boolean aktuellBestaetigen) {
        this.aktuellBestaetigen = aktuellBestaetigen;
    }
    
    
}
