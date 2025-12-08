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

import de.meetingapps.meetingportal.meetComAllg.CaString;
import de.meetingapps.meetingportal.meetComEntities.EclBestWorkflow;
import de.meetingapps.meetingportal.meetComEntities.EclMeldung;
import de.meetingapps.meetingportal.meetComEntities.EclVorlaeufigeVollmacht;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

@SessionScoped
@Named
public class EclBestWorkflowM implements Serializable {
    private static final long serialVersionUID = 9106705547273330536L;

    /** Mandantennummer */
    private int mandant = 0;

    /**
     * eindeutiger Key. Mit Autoincrement
     */
    private long ident = 0;

    /**orig oder kopie für original oder Kopie
     * LEN=10*/
    private String subverzeichnis = "";
    /**LEN=100*/

    private String dateinameBestaetigung = "";

    /**Zeit, zu der der Import der Datei erfolgte
     * LEN=19*/
    private String dateinameImportAm = "";

    /**LEN=20*/
    private String zuAktionaersnummer = "";

    /**Vollmacht wurde im Original oder in Kopie vorgelegt. Wird (theoretisch)
     * aus subverzeichnis geholt, kann aber überschrieben werden
     */
    private int origOderKopie = 0;

    /**Verweis auf Vorläufige Vollmacht, die durch diese Vollmacht erzeugt wurde*/
    private int vorlVollmachtIdent = 0;

    /**Ist !=0, wenn der Satz gerade in Bearbeitung ist. Achtung, muß dann irgendwie täglich zurückgesetzt
     * werden. ist derzeit nicht die UserNr, sondern eine laufende Id.
     */
    private int inBearbeitungDurchUserNr = 0;

    /**
     * Achtung - muß immer in allen Sätzen mit madant/dateinameBestaetigung/zuAktionaersnummer upgedatet werden!
     * 0= noch vollkommen unbearbeitet
     * 1= geprüft und akzeptiert	Bei Bestandsabgleich: Veränderungen registriert.
     * 2= geprüft und abgelehnt		Bei Bestandsabgleich: keine Veränderungen
     * 3= geprüft und wiedervorlage	Bei Bestandsabgleich: dito
     * 4= Nicht als Nachweis bearbeiten
     * 5= Vollmacht ist bei der Gesellschaft nicht hinterlegt (Spezialfall ku216)
     * (intern: 99 reserviert) 
     */
    private String pruefstatusErgebnis = "0";

    /**Wie pruefstatusErgebnis, aber nicht als "Ergebnis aller Prüfvorgänge", sondern
     * was dieser User gerade konkret in diesem Vorgang mit gespeichert hat
     */
    private String pruefstatusVorgang = "0";

    private String abgelehntWeil = "0";

    /**Vorsorglich mal eingebaut*/
    private int wurdeQualitaetsgesichert = 0;

    /**LEN=400*/
    private String kommentar = "";
    /**LEN=300*/
    private String abgelehntWeilText = "";

    /**LEN=19*/
    private String bearbeitungsZeit = "";

    private int bearbeitungsUser = 0;
    /**LEN=40*/
    private String bearbeitungsUserName = "";

    /**Felder nicht in Datenbank!*/
    private String nameMitglied = "";
    private int identMitglied = 0;
    private boolean unterVorbehalt = false;
    private String pruefStatusErgebnisVolltext = "";
    private String pruefStatusVorgangVolltext = "";

    /*++++++++Bevollmächtigter++++++++++++++++++*/
    /**true => gesetzliche Vertreter*/
    private boolean gesetzlicherVertreter = false;

    /**1=es wird eine Vollamcht hinterlegt mit eigenem Zugang
     * 2=wird nur als Nachweis beachtet (d.h. kein neuer Zugang)
     */
    private String vollmachtOderNachweis = "1";
    private String kennung = "";

    /**Kein Eingabefeld - gibt an, ob Kennung geladen wurde oder nicht*/
    private boolean kennungGeladen = false;
    /**Hinweisfeld, wieviele Vollmachten die geladene Kennung bereits vertritt*/
    private String hinweisAnzahlVollmachten = "";
    private int genommenAnzahlVollmachtenNormal = 0;
    private int genommenAnzahlVollmachtenGesetzlich = 0;

    /**Für spätere Weiterverarbeitung - wird nach Laden gefüllt
     * Siehe KonstLoginKennungArt*/
    private int kennungGeladenIst = 0;
    /**Ident der geladenen Kennung - für spätere Weiterverarbeitung.
     * Je nach kennungGeladenIst aktienregisterIdent oder personenNatJurIdent
     */
    private int kennungGeladenIdent = 0;
    /**Falls geladene Kennung eine Aktienregisternummer ist,
     * dann steht hier die zugehörige PersonNatJurIdent (für spätere Vollmacht)
     */
    private int personNatJurGeladenIdent = 0;

    private String bevollmaechtigterTitel = "";
    private String bevollmaechtigterName = "";
    private String bevollmaechtigterVorname = "";
    private String bevollmaechtigterStrasse = "";
    private String bevollmaechtigterPlz = "";
    private String bevollmaechtigterOrt = "";
    private String bevollmaechtigterEMail = "";
    private String mitgliedOderSonstiger = "1";
    private String bevollmaechtigterArtText = "";
    private String eingabeDatum = "";
    private String eingabeOrt = "";

    /**Felder nicht in Datenbank! - Daten zum Anzeigen für das Formular*/
    private String fMitgliedsnummer = "";
    private String fNameVorname = "";
    private String fGeburtsdatum = "";
    private String fStrasseHausnummer = "";
    private String fPLZ = "";
    private String fOrt = "";
    private String fAdresszusatz = "";
    private String fSteuerlicheIdent = "";
    private String fTelefon = "";
    private String fMailAdresse = "";
    private String fEheNameVorname = "";
    private String fEheGeburtsdatum = "";
    private String fEheSteuerlicheIdent = "";
    private String fKontoBei = "";
    private String fIBAN = "";
    private String fBIC = "";
    private String fNameVornameKontoinhaber = "";
    private String fLand = "";

    private boolean storniert = false;
    private boolean stornierbar = false;

    private long identVorlauefigeVollmacht = 0;

    /*Für ku216-Ablauf zum Zwischenspeichern der bearbeiteten Meldung*/
    private EclMeldung eclMeldung=null;
    
    public void initAlles() {
        mandant = 0;
        ident = 0;
        subverzeichnis = "";
        dateinameBestaetigung = "";
        dateinameImportAm = "";
        zuAktionaersnummer = "";
        origOderKopie = 0;
        vorlVollmachtIdent = 0;
        inBearbeitungDurchUserNr = 0;
        pruefstatusErgebnis = "0";
        pruefstatusVorgang = "0";
        abgelehntWeil = "0";
        wurdeQualitaetsgesichert = 0;
        kommentar = "";
        abgelehntWeilText = "";
        bearbeitungsZeit = "";
        bearbeitungsUser = 0;
        bearbeitungsUserName = "";
        nameMitglied = "";
        identMitglied = 0;
        unterVorbehalt = false;
        pruefStatusErgebnisVolltext = "";
        pruefStatusVorgangVolltext = "";
        
        gesetzlicherVertreter = false;
        vollmachtOderNachweis = "1";
        kennung = "";
        kennungGeladen = false;
        hinweisAnzahlVollmachten = "";
        genommenAnzahlVollmachtenNormal = 0;
        genommenAnzahlVollmachtenGesetzlich = 0;
        kennungGeladenIst = 0;
        kennungGeladenIdent = 0;
        personNatJurGeladenIdent = 0;
        
        bevollmaechtigterTitel = "";
        bevollmaechtigterName = "";
        bevollmaechtigterVorname = "";
        bevollmaechtigterStrasse = "";
        bevollmaechtigterPlz = "";
        bevollmaechtigterOrt = "";
        bevollmaechtigterEMail = "";
        mitgliedOderSonstiger = "1";
        bevollmaechtigterArtText = "";
        eingabeDatum = "";
        eingabeOrt = "";
        
        initFFelder();
        
        storniert = false;
        stornierbar = false;
        identVorlauefigeVollmacht = 0;
        eclMeldung=null;
    }
    
    public void initFFelder() {
        fMitgliedsnummer = "";
        fNameVorname = "";
        fGeburtsdatum = "";
        fStrasseHausnummer = "";
        fPLZ = "";
        fOrt = "";
        fAdresszusatz = "";
        fSteuerlicheIdent = "";
        fTelefon = "";
        fMailAdresse = "";
        fEheNameVorname = "";
        fEheGeburtsdatum = "";
        fEheSteuerlicheIdent = "";
        fKontoBei = "";
        fIBAN = "";
        fBIC = "";
        fNameVornameKontoinhaber = "";
        fLand = "";
    }

    public void copyFrom(EclBestWorkflow pBestWorkflow) {
        mandant = pBestWorkflow.mandant;
        ident = pBestWorkflow.ident;
        subverzeichnis = pBestWorkflow.subverzeichnis;
        dateinameBestaetigung = pBestWorkflow.dateinameBestaetigung;
        dateinameImportAm = pBestWorkflow.dateinameImportAm;
        zuAktionaersnummer = pBestWorkflow.zuAktionaersnummer;
        origOderKopie = pBestWorkflow.origOderKopie;
        vorlVollmachtIdent = pBestWorkflow.vorlVollmachtIdent;
        inBearbeitungDurchUserNr = pBestWorkflow.inBearbeitungDurchUserNr;
        pruefstatusErgebnis = Integer.toString(pBestWorkflow.pruefstatusErgebnis);
        pruefstatusVorgang = Integer.toString(pBestWorkflow.pruefstatusVorgang);
        /*XXX*/
        abgelehntWeil = "0";
        abgelehntWeilText = "";

        wurdeQualitaetsgesichert = pBestWorkflow.wurdeQualitaetsgesichert;
        kommentar = pBestWorkflow.kommentar;
        bearbeitungsZeit = pBestWorkflow.bearbeitungsZeit;
        bearbeitungsUser = pBestWorkflow.bearbeitungsUser;
        bearbeitungsUserName = pBestWorkflow.bearbeitungsUserName;

        nameMitglied = "";
        unterVorbehalt = false;

        gesetzlicherVertreter = false;
        vollmachtOderNachweis = "1";
        kennung = "";
        kennungGeladen = false;
        hinweisAnzahlVollmachten = "";
        bevollmaechtigterTitel = "";
        bevollmaechtigterName = "";
        bevollmaechtigterVorname = "";
        bevollmaechtigterStrasse = "";
        bevollmaechtigterPlz = "";
        bevollmaechtigterOrt = "";
        bevollmaechtigterEMail = "";
        mitgliedOderSonstiger = "1";
        bevollmaechtigterArtText = "";
        eingabeDatum = "";
        eingabeOrt = "";
    }

    public void copyRestFrom(EclVorlaeufigeVollmacht pVorlaeufigeVollmacht) {
        abgelehntWeil = Integer.toString(pVorlaeufigeVollmacht.abgelehntWeil);
        abgelehntWeilText = pVorlaeufigeVollmacht.abgelehntWeilText;
        identVorlauefigeVollmacht = pVorlaeufigeVollmacht.ident;
        gesetzlicherVertreter = pVorlaeufigeVollmacht.bevollmaechtigterAusgefuehrtIstGesetzlich == 1;
    }

    public void copyTo(EclBestWorkflow pBestWorkflow) {
        pBestWorkflow.mandant = mandant;
        pBestWorkflow.ident = ident;
        pBestWorkflow.subverzeichnis = subverzeichnis;
        pBestWorkflow.dateinameBestaetigung = dateinameBestaetigung;
        pBestWorkflow.dateinameImportAm = dateinameImportAm;
        pBestWorkflow.zuAktionaersnummer = zuAktionaersnummer;
        pBestWorkflow.origOderKopie = origOderKopie;
        pBestWorkflow.vorlVollmachtIdent = vorlVollmachtIdent;
        pBestWorkflow.inBearbeitungDurchUserNr = inBearbeitungDurchUserNr;

        if (pruefstatusErgebnis == null || !CaString.isNummern(pruefstatusErgebnis)) {
            pruefstatusErgebnis = "0";
        }
        pBestWorkflow.pruefstatusErgebnis = Integer.parseInt(pruefstatusErgebnis);

        if (pruefstatusVorgang == null || !CaString.isNummern(pruefstatusVorgang)) {
            pruefstatusVorgang = "0";
        }
        pBestWorkflow.pruefstatusVorgang = Integer.parseInt(pruefstatusVorgang);

        pBestWorkflow.wurdeQualitaetsgesichert = wurdeQualitaetsgesichert;
        pBestWorkflow.kommentar = kommentar;
        pBestWorkflow.bearbeitungsZeit = bearbeitungsZeit;
        pBestWorkflow.bearbeitungsUser = bearbeitungsUser;
        pBestWorkflow.bearbeitungsUserName = bearbeitungsUserName;
    }

    /*****************Standard getter und setter***********************/

    public int getMandant() {
        return mandant;
    }

    public void setMandant(int mandant) {
        this.mandant = mandant;
    }

    public long getIdent() {
        return ident;
    }

    public void setIdent(long ident) {
        this.ident = ident;
    }

    public String getDateinameBestaetigung() {
        return dateinameBestaetigung;
    }

    public void setDateinameBestaetigung(String dateinameBestaetigung) {
        this.dateinameBestaetigung = dateinameBestaetigung;
    }

    public String getDateinameImportAm() {
        return dateinameImportAm;
    }

    public void setDateinameImportAm(String dateinameImportAm) {
        this.dateinameImportAm = dateinameImportAm;
    }

    public String getZuAktionaersnummer() {
        return zuAktionaersnummer;
    }

    public void setZuAktionaersnummer(String zuAktionaersnummer) {
        this.zuAktionaersnummer = zuAktionaersnummer;
    }

    public int getInBearbeitungDurchUserNr() {
        return inBearbeitungDurchUserNr;
    }

    public void setInBearbeitungDurchUserNr(int inBearbeitungDurchUserNr) {
        this.inBearbeitungDurchUserNr = inBearbeitungDurchUserNr;
    }

    public int getWurdeQualitaetsgesichert() {
        return wurdeQualitaetsgesichert;
    }

    public void setWurdeQualitaetsgesichert(int wurdeQualitaetsgesichert) {
        this.wurdeQualitaetsgesichert = wurdeQualitaetsgesichert;
    }

    public String getKommentar() {
        return kommentar;
    }

    public void setKommentar(String kommentar) {
        this.kommentar = kommentar;
    }

    public String getBearbeitungsZeit() {
        return bearbeitungsZeit;
    }

    public void setBearbeitungsZeit(String bearbeitungsZeit) {
        this.bearbeitungsZeit = bearbeitungsZeit;
    }

    public int getBearbeitungsUser() {
        return bearbeitungsUser;
    }

    public void setBearbeitungsUser(int bearbeitungsUser) {
        this.bearbeitungsUser = bearbeitungsUser;
    }

    public String getBearbeitungsUserName() {
        return bearbeitungsUserName;
    }

    public void setBearbeitungsUserName(String bearbeitungsUserName) {
        this.bearbeitungsUserName = bearbeitungsUserName;
    }

    public String getNameMitglied() {
        return nameMitglied;
    }

    public void setNameMitglied(String nameMitglied) {
        this.nameMitglied = nameMitglied;
    }

    public boolean isUnterVorbehalt() {
        return unterVorbehalt;
    }

    public void setUnterVorbehalt(boolean unterVorbehalt) {
        this.unterVorbehalt = unterVorbehalt;
    }

    public String getPruefstatusErgebnis() {
        return pruefstatusErgebnis;
    }

    public void setPruefstatusErgebnis(String pruefstatusErgebnis) {
        this.pruefstatusErgebnis = pruefstatusErgebnis;
    }

    public String getPruefstatusVorgang() {
        return pruefstatusVorgang;
    }

    public void setPruefstatusVorgang(String pruefstatusVorgang) {
        this.pruefstatusVorgang = pruefstatusVorgang;
    }

    public String getPruefStatusErgebnisVolltext() {
        String hText = pruefStatusErgebnisVolltext;
        switch (pruefstatusErgebnis) {
        case "1":
            hText = "geprüft und akzeptiert";
            break;
        case "2":
            hText = "geprüft und abgelehnt";
            break;
        case "3":
            hText = "geprüft und Wiedervorlage";
            break;
        case "4":
            hText = "Nicht als Nachweis verwenden";
            break;

        }
        return hText;
    }

    public void setPruefStatusErgebnisVolltext(String pruefStatusErgebnisVolltext) {
        this.pruefStatusErgebnisVolltext = pruefStatusErgebnisVolltext;
    }

    public String getPruefStatusVorgangVolltext() {
        String hText = pruefStatusVorgangVolltext;
        switch (pruefstatusVorgang) {
        case "1":
            hText = "geprüft und akzeptiert";
            break;
        case "2":
            hText = "geprüft und abgelehnt";
            break;
        case "3":
            hText = "geprüft und wiedervorlage";
            break;
        case "4":
            hText = "Nicht als Nachweis verwenden";
            break;

        }
        return hText;
    }

    public void setPruefStatusVorgangVolltext(String pruefStatusVorgangVolltext) {
        this.pruefStatusVorgangVolltext = pruefStatusVorgangVolltext;
    }

    public String getfNameVorname() {
        return fNameVorname;
    }

    public void setfNameVorname(String fNameVorname) {
        this.fNameVorname = fNameVorname;
    }

    public String getfGeburtsdatum() {
        return fGeburtsdatum;
    }

    public void setfGeburtsdatum(String fGeburtsdatum) {
        this.fGeburtsdatum = fGeburtsdatum;
    }

    public String getfStrasseHausnummer() {
        return fStrasseHausnummer;
    }

    public void setfStrasseHausnummer(String fStrasseHausnummer) {
        this.fStrasseHausnummer = fStrasseHausnummer;
    }

    public String getfPLZ() {
        return fPLZ;
    }

    public void setfPLZ(String fPLZ) {
        this.fPLZ = fPLZ;
    }

    public String getfOrt() {
        return fOrt;
    }

    public void setfOrt(String fOrt) {
        this.fOrt = fOrt;
    }

    public String getfAdresszusatz() {
        return fAdresszusatz;
    }

    public void setfAdresszusatz(String fAdresszusatz) {
        this.fAdresszusatz = fAdresszusatz;
    }

    public String getfSteuerlicheIdent() {
        return fSteuerlicheIdent;
    }

    public void setfSteuerlicheIdent(String fSteuerlicheIdent) {
        this.fSteuerlicheIdent = fSteuerlicheIdent;
    }

    public String getfTelefon() {
        return fTelefon;
    }

    public void setfTelefon(String fTelefon) {
        this.fTelefon = fTelefon;
    }

    public String getfMailAdresse() {
        return fMailAdresse;
    }

    public void setfMailAdresse(String fMailAdresse) {
        this.fMailAdresse = fMailAdresse;
    }

    public String getfEheNameVorname() {
        return fEheNameVorname;
    }

    public void setfEheNameVorname(String fEheNameVorname) {
        this.fEheNameVorname = fEheNameVorname;
    }

    public String getfEheGeburtsdatum() {
        return fEheGeburtsdatum;
    }

    public void setfEheGeburtsdatum(String fEheGeburtsdatum) {
        this.fEheGeburtsdatum = fEheGeburtsdatum;
    }

    public String getfEheSteuerlicheIdent() {
        return fEheSteuerlicheIdent;
    }

    public void setfEheSteuerlicheIdent(String fEheSteuerlicheIdent) {
        this.fEheSteuerlicheIdent = fEheSteuerlicheIdent;
    }

    public String getfKontoBei() {
        return fKontoBei;
    }

    public void setfKontoBei(String fKontoBei) {
        this.fKontoBei = fKontoBei;
    }

    public String getfIBAN() {
        return fIBAN;
    }

    public void setfIBAN(String fIBAN) {
        this.fIBAN = fIBAN;
    }

    public String getfBIC() {
        return fBIC;
    }

    public void setfBIC(String fBIC) {
        this.fBIC = fBIC;
    }

    public String getfNameVornameKontoinhaber() {
        return fNameVornameKontoinhaber;
    }

    public void setfNameVornameKontoinhaber(String fNameVornameKontoinhaber) {
        this.fNameVornameKontoinhaber = fNameVornameKontoinhaber;
    }

    public String getfLand() {
        return fLand;
    }

    public void setfLand(String fLand) {
        this.fLand = fLand;
    }

    public String getfMitgliedsnummer() {
        return fMitgliedsnummer;
    }

    public void setfMitgliedsnummer(String fMitgliedsnummer) {
        this.fMitgliedsnummer = fMitgliedsnummer;
    }

    public String getSubverzeichnis() {
        return subverzeichnis;
    }

    public void setSubverzeichnis(String subverzeichnis) {
        this.subverzeichnis = subverzeichnis;
    }

    public int getOrigOderKopie() {
        return origOderKopie;
    }

    public void setOrigOderKopie(int origOderKopie) {
        this.origOderKopie = origOderKopie;
    }

    public int getVorlVollmachtIdent() {
        return vorlVollmachtIdent;
    }

    public void setVorlVollmachtIdent(int vorlVollmachtIdent) {
        this.vorlVollmachtIdent = vorlVollmachtIdent;
    }

    public String getVollmachtOderNachweis() {
        return vollmachtOderNachweis;
    }

    public void setVollmachtOderNachweis(String vollmachtOderNachweis) {
        this.vollmachtOderNachweis = vollmachtOderNachweis;
    }

    public String getKennung() {
        return kennung;
    }

    public void setKennung(String kennung) {
        this.kennung = kennung;
    }

    public String getBevollmaechtigterName() {
        return bevollmaechtigterName;
    }

    public void setBevollmaechtigterName(String bevollmaechtigterName) {
        this.bevollmaechtigterName = bevollmaechtigterName;
    }

    public String getBevollmaechtigterVorname() {
        return bevollmaechtigterVorname;
    }

    public void setBevollmaechtigterVorname(String bevollmaechtigterVorname) {
        this.bevollmaechtigterVorname = bevollmaechtigterVorname;
    }

    public String getBevollmaechtigterStrasse() {
        return bevollmaechtigterStrasse;
    }

    public void setBevollmaechtigterStrasse(String bevollmaechtigterStrasse) {
        this.bevollmaechtigterStrasse = bevollmaechtigterStrasse;
    }

    public String getBevollmaechtigterPlz() {
        return bevollmaechtigterPlz;
    }

    public void setBevollmaechtigterPlz(String bevollmaechtigterPlz) {
        this.bevollmaechtigterPlz = bevollmaechtigterPlz;
    }

    public String getBevollmaechtigterOrt() {
        return bevollmaechtigterOrt;
    }

    public void setBevollmaechtigterOrt(String bevollmaechtigterOrt) {
        this.bevollmaechtigterOrt = bevollmaechtigterOrt;
    }

    public String getBevollmaechtigterEMail() {
        return bevollmaechtigterEMail;
    }

    public void setBevollmaechtigterEMail(String bevollmaechtigterEMail) {
        this.bevollmaechtigterEMail = bevollmaechtigterEMail;
    }

    public String getBevollmaechtigterArtText() {
        return bevollmaechtigterArtText;
    }

    public void setBevollmaechtigterArtText(String bevollmaechtigterArtText) {
        this.bevollmaechtigterArtText = bevollmaechtigterArtText;
    }

    public String getEingabeDatum() {
        return eingabeDatum;
    }

    public void setEingabeDatum(String eingabeDatum) {
        this.eingabeDatum = eingabeDatum;
    }

    public String getEingabeOrt() {
        return eingabeOrt;
    }

    public void setEingabeOrt(String eingabeOrt) {
        this.eingabeOrt = eingabeOrt;
    }

    public String getMitgliedOderSonstiger() {
        return mitgliedOderSonstiger;
    }

    public void setMitgliedOderSonstiger(String mitgliedOderSonstiger) {
        this.mitgliedOderSonstiger = mitgliedOderSonstiger;
    }

    public String getBevollmaechtigterTitel() {
        return bevollmaechtigterTitel;
    }

    public void setBevollmaechtigterTitel(String bevollmaechtigterTitel) {
        this.bevollmaechtigterTitel = bevollmaechtigterTitel;
    }

    public String getAbgelehntWeil() {
        return abgelehntWeil;
    }

    public void setAbgelehntWeil(String abgelehntWeil) {
        this.abgelehntWeil = abgelehntWeil;
    }

    public String getAbgelehntWeilText() {
        return abgelehntWeilText;
    }

    public void setAbgelehntWeilText(String abgelehntWeilText) {
        this.abgelehntWeilText = abgelehntWeilText;
    }

    public boolean isGesetzlicherVertreter() {
        return gesetzlicherVertreter;
    }

    public void setGesetzlicherVertreter(boolean gesetzlicherVertreter) {
        this.gesetzlicherVertreter = gesetzlicherVertreter;
    }

    public boolean isKennungGeladen() {
        return kennungGeladen;
    }

    public void setKennungGeladen(boolean kennungGeladen) {
        this.kennungGeladen = kennungGeladen;
    }

    public String getHinweisAnzahlVollmachten() {
        return hinweisAnzahlVollmachten;
    }

    public void setHinweisAnzahlVollmachten(String hinweisAnzahlVollmachten) {
        this.hinweisAnzahlVollmachten = hinweisAnzahlVollmachten;
    }

    public int getKennungGeladenIst() {
        return kennungGeladenIst;
    }

    public void setKennungGeladenIst(int kennungGeladenIst) {
        this.kennungGeladenIst = kennungGeladenIst;
    }

    public int getKennungGeladenIdent() {
        return kennungGeladenIdent;
    }

    public void setKennungGeladenIdent(int kennungGeladenIdent) {
        this.kennungGeladenIdent = kennungGeladenIdent;
    }

    public int getPersonNatJurGeladenIdent() {
        return personNatJurGeladenIdent;
    }

    public void setPersonNatJurGeladenIdent(int personNatJurGeladenIdent) {
        this.personNatJurGeladenIdent = personNatJurGeladenIdent;
    }

    public int getIdentMitglied() {
        return identMitglied;
    }

    public void setIdentMitglied(int identMitglied) {
        this.identMitglied = identMitglied;
    }

    public int getGenommenAnzahlVollmachtenNormal() {
        return genommenAnzahlVollmachtenNormal;
    }

    public void setGenommenAnzahlVollmachtenNormal(int genommenAnzahlVollmachtenNormal) {
        this.genommenAnzahlVollmachtenNormal = genommenAnzahlVollmachtenNormal;
    }

    public int getGenommenAnzahlVollmachtenGesetzlich() {
        return genommenAnzahlVollmachtenGesetzlich;
    }

    public void setGenommenAnzahlVollmachtenGesetzlich(int genommenAnzahlVollmachtenGesetzlich) {
        this.genommenAnzahlVollmachtenGesetzlich = genommenAnzahlVollmachtenGesetzlich;
    }

    public boolean isStorniert() {
        return storniert;
    }

    public void setStorniert(boolean storniert) {
        this.storniert = storniert;
    }

    public boolean isStornierbar() {
        return stornierbar;
    }

    public void setStornierbar(boolean stornierbar) {
        this.stornierbar = stornierbar;
    }

    public long getIdentVorlauefigeVollmacht() {
        return identVorlauefigeVollmacht;
    }

    public void setIdentVorlauefigeVollmacht(long identVorlauefigeVollmacht) {
        this.identVorlauefigeVollmacht = identVorlauefigeVollmacht;
    }

    public EclMeldung getEclMeldung() {
        return eclMeldung;
    }

    public void setEclMeldung(EclMeldung eclMeldung) {
        this.eclMeldung = eclMeldung;
    }

}
