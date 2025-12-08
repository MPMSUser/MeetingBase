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

import de.meetingapps.meetingportal.meetComAllg.CaDatumZeit;

/**Verfahrensdokumentation:
 * 
 * In allen Fällen: Button "Dokumente/Nachweise werden schriftlich nachgereicht".
 * In allen Fällen: Button "Name wird nachgeliefert"
 *  
 * Hochladen von Nachweisen und das Eingeben der Daten ist nicht zwingend, kann ggf. auch noch auf der Versammlung nachgereicht werden
 * oder schriftlich nachgereicht werden.
 * 
 * Normale Vollmacht an einen Dritten:
 * -----------------------------------
 * vertreterName, vertreterVorname, vertreterOrt muß eingegeben werden.
 * Falls vertreterArt "anderes Mitglied", dann ist vertreterID = Mitgliedsnummer einzugeben.
 * Falls anderweitig, dann kann Nachweis hochgeladen werden. Muß aber nicht - ggf. Nachweis auf Versammlung erforderlich. 
 * 
 * Wird gesetzlich vertreten:
 * --------------------------
 * vertreterName, vertreterVorname, vertreterOrt muß eingegeben werden.
 * Nachweise kann hochgeladen werden.
 * 
 * Gesetzlich zu vertretender gibt Vollmacht
 * -----------------------------------------
 * geberName, geberVorname müssen eingegeben werden.
 * Nachweise für Berechtigung des Gebers hochladen
 * Nachweise und Eingaben sonst analog wie normale Vollmacht an Dritten
 * 
 * Erhaltene Vollmachten
 * ---------------------
 * Bei jeder Anmeldung, können zusätzliche Vollmachten über erhaltene Vollmachten hochgeladen werden.
 */



public class EclVorlaeufigeVollmachtEingabe {

    public int ident=0;
    
    /**Aktionärsnummer und Kennung, von denen aus die Eingabe erfolgte
     * LEN=20*/
    public String erteilendeAktionaersnummer="";
    public int erteilendeLoginDaten=0;
    
    /***nur Nachweise*/
    static final public int ART_DER_EINGABE_NUR_NACHWEIS=1;
    
    /**Bevollmächtigter wurde eingegeben*/
    static final public int ART_DER_EINGABE_BEVOLLMAECHTIGTER=2;
    
    /**Gesetzlicher Vertreter wurde eingegeben*/
    static final public int ART_DER_EINGABE_GESETZLICHER_VERTRETER=3;
    
    /**Siehe ART_DER_EINGABE_**/
    public int artDerEingabe=0;

    /**Je nach ART_DER_EINGABE_*: Bevollmächtigter oder gesetzlicher Vertreter*/
    
    /**Mitgliedsnummer, ggf. auch Verweis auf personNatJur o.ä.
     * LEN=20*/
    public String vertreterId="";
    
    /**LEN=30*/
    public String vertreterTitel="";
    /**LEN=80
     * Bei Nachweisen: Interner Dateiname*/
    public String vertreterName="";
    /**LEN=80
     * Bei Nachweisen: Pfad Dateiname*/
    public String vertreterVorname="";
    /**LEN=80
     * Bei Nachweisen: Original-Dateiname*/
    public String vertreterZusatz="";
    /**LEN=80*/
    public String vertreterStrasse="";
    /**LEN=20*/
    public String vertreterPlz="";
    /**LEN=80*/
    public String vertreterOrt="";
    
    /**LEN=200 (Aber nur 100 zulässig - muß irgendwann mal allgemein erhöht werden*/
    public String vertreterMail="";
    
    
    static final public int VERTRETER_ART_ANDERER_AKTIONAER=1;
    static final public int VERTRETER_ART_SONSTIGE=8;
    
    public int vertreterArt=0;
    public String liefereVertreterArtText() {
        switch (vertreterArt) {
        case 1:return "ist Mitglied";
        case 2:return "ist Ehegatte d.M.";
        case 3:return "ist Vater oder Mutter d.M.";
        case 4:return "ist ein Kind d.M.";
        case 5:return "ist Bruder oder Schwester d.M.";
        case 6:return "ist Rechtsanwalt, Steuerberater oder Wirtschaftsprüfer";
        case 7:return "steht in einem Gesellschafts- oder Anstellungsverhältnis z.M.";
        case 8:return "ist sonstiger";
        }
        return "";
    }
    /**LEN=100*/
    public String vertreterArtBeiSonstige="";
    
    /**LEN=19 Format: YYYY.MM.DD HH:MM:SS*/
    public String eingabeDatum="";
    public String eingabeDatumAnzeige() {
        return CaDatumZeit.DatumZeitStringFuerAnzeige(eingabeDatum);
    }
    
    /**1 => wurde vom Aktionär zurückgezogen / verändert / storniert*/
    public int wurdeStorniert=0; 
    /**LEN=19 Format: YYYY.MM.DD HH:MM:SS*/
    public String stornierungsDatum=""; 
    
    public String stornierungsDatumAnzeige() {
        return CaDatumZeit.DatumZeitStringFuerAnzeige(stornierungsDatum);
    }
    
    /*++++++++++++++Prüfstatus+++++++++++++++++++*/
    /**Kommentar / Verwendung noch nicht endgültig
     * 0=noch nicht geprüft
     * 101=Geprüft
     * 103=Wiedervorlage
     * */
    public int pruefstatus = 0;
    public int abgelehntWeil = 0;
    /**LEN=300*/
    public String abgelehntWeilText="";
    
    /**Kennzeichen, inwieweit ein Storno bereits im Vollmachtsprüfungsprozess verarbeitet / approved wurde*/
    public int pruefstatusStorno = 0;
    
    public int inBearbeitungDurchUserNr=0;
    
    
    /*+++++++++++++++++++++++++++++Nicht in Datenbank++++++++++++++++++++++++++++++++++++++*/
    
    public boolean vertreterAusRegisterGeladen=false;
    public String ausRegisterName="";
    public String ausRegisterVorname="";
    public String ausRegisterStrasse="";
    public String ausRegisterPLZ="";
    public String ausRegisterOrt="";
    
    /*+++++++++++++++++++++++++Standard getter und setter+++++++++++++++++++++++++++++++++++++++++*/
    public int getIdent() {
        return ident;
    }
    public void setIdent(int ident) {
        this.ident = ident;
    }
    public String getErteilendeAktionaersnummer() {
        return erteilendeAktionaersnummer;
    }
    public void setErteilendeAktionaersnummer(String erteilendeAktionaersnummer) {
        this.erteilendeAktionaersnummer = erteilendeAktionaersnummer;
    }
    public int getErteilendeLoginDaten() {
        return erteilendeLoginDaten;
    }
    public void setErteilendeLoginDaten(int erteilendeLoginDaten) {
        this.erteilendeLoginDaten = erteilendeLoginDaten;
    }
    public int getArtDerEingabe() {
        return artDerEingabe;
    }
    public void setArtDerEingabe(int artDerEingabe) {
        this.artDerEingabe = artDerEingabe;
    }
    public String getVertreterId() {
        return vertreterId;
    }
    public void setVertreterId(String vertreterId) {
        this.vertreterId = vertreterId;
    }
    public String getVertreterTitel() {
        return vertreterTitel;
    }
    public void setVertreterTitel(String vertreterTitel) {
        this.vertreterTitel = vertreterTitel;
    }
    public String getVertreterName() {
        return vertreterName;
    }
    public void setVertreterName(String vertreterName) {
        this.vertreterName = vertreterName;
    }
    public String getVertreterVorname() {
        return vertreterVorname;
    }
    public void setVertreterVorname(String vertreterVorname) {
        this.vertreterVorname = vertreterVorname;
    }
    public String getVertreterZusatz() {
        return vertreterZusatz;
    }
    public void setVertreterZusatz(String vertreterZusatz) {
        this.vertreterZusatz = vertreterZusatz;
    }
    public String getVertreterStrasse() {
        return vertreterStrasse;
    }
    public void setVertreterStrasse(String vertreterStrasse) {
        this.vertreterStrasse = vertreterStrasse;
    }
    public String getVertreterPlz() {
        return vertreterPlz;
    }
    public void setVertreterPlz(String vertreterPlz) {
        this.vertreterPlz = vertreterPlz;
    }
    public String getVertreterOrt() {
        return vertreterOrt;
    }
    public void setVertreterOrt(String vertreterOrt) {
        this.vertreterOrt = vertreterOrt;
    }
    public String getVertreterMail() {
        return vertreterMail;
    }
    public void setVertreterMail(String vertreterMail) {
        this.vertreterMail = vertreterMail;
    }
    public int getVertreterArt() {
        return vertreterArt;
    }
    public void setVertreterArt(int vertreterArt) {
        this.vertreterArt = vertreterArt;
    }
    public String getVertreterArtBeiSonstige() {
        return vertreterArtBeiSonstige;
    }
    public void setVertreterArtBeiSonstige(String vertreterArtBeiSonstige) {
        this.vertreterArtBeiSonstige = vertreterArtBeiSonstige;
    }
    public String getEingabeDatum() {
        return eingabeDatum;
    }
    public void setEingabeDatum(String eingabeDatum) {
        this.eingabeDatum = eingabeDatum;
    }
    public int getWurdeStorniert() {
        return wurdeStorniert;
    }
    public void setWurdeStorniert(int wurdeStorniert) {
        this.wurdeStorniert = wurdeStorniert;
    }
    public String getStornierungsDatum() {
        return stornierungsDatum;
    }
    public void setStornierungsDatum(String stornierungsDatum) {
        this.stornierungsDatum = stornierungsDatum;
    }
    public int getPruefstatus() {
        return pruefstatus;
    }
    public void setPruefstatus(int pruefstatus) {
        this.pruefstatus = pruefstatus;
    }
    public int getAbgelehntWeil() {
        return abgelehntWeil;
    }
    public void setAbgelehntWeil(int abgelehntWeil) {
        this.abgelehntWeil = abgelehntWeil;
    }
    public String getAbgelehntWeilText() {
        return abgelehntWeilText;
    }
    public void setAbgelehntWeilText(String abgelehntWeilText) {
        this.abgelehntWeilText = abgelehntWeilText;
    }
    public int getPruefstatusStorno() {
        return pruefstatusStorno;
    }
    public void setPruefstatusStorno(int pruefstatusStorno) {
        this.pruefstatusStorno = pruefstatusStorno;
    }
    public int getInBearbeitungDurchUserNr() {
        return inBearbeitungDurchUserNr;
    }
    public void setInBearbeitungDurchUserNr(int inBearbeitungDurchUserNr) {
        this.inBearbeitungDurchUserNr = inBearbeitungDurchUserNr;
    }
    public String getAusRegisterName() {
        return ausRegisterName;
    }
    public void setAusRegisterName(String ausRegisterName) {
        this.ausRegisterName = ausRegisterName;
    }
    public String getAusRegisterVorname() {
        return ausRegisterVorname;
    }
    public void setAusRegisterVorname(String ausRegisterVorname) {
        this.ausRegisterVorname = ausRegisterVorname;
    }
    public String getAusRegisterStrasse() {
        return ausRegisterStrasse;
    }
    public void setAusRegisterStrasse(String ausRegisterStrasse) {
        this.ausRegisterStrasse = ausRegisterStrasse;
    }
    public String getAusRegisterPLZ() {
        return ausRegisterPLZ;
    }
    public void setAusRegisterPLZ(String ausRegisterPLZ) {
        this.ausRegisterPLZ = ausRegisterPLZ;
    }
    public String getAusRegisterOrt() {
        return ausRegisterOrt;
    }
    public void setAusRegisterOrt(String ausRegisterOrt) {
        this.ausRegisterOrt = ausRegisterOrt;
    }
    public boolean isVertreterAusRegisterGeladen() {
        return vertreterAusRegisterGeladen;
    }
    public void setVertreterAusRegisterGeladen(boolean vertreterAusRegisterGeladen) {
        this.vertreterAusRegisterGeladen = vertreterAusRegisterGeladen;
    }

    
    
}
