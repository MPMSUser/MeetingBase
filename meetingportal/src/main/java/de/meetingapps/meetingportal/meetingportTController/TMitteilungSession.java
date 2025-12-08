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
package de.meetingapps.meetingportal.meetingportTController;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.Future;

import de.meetingapps.meetingportal.meetComEh.EhInhaltsHinweise;
import de.meetingapps.meetingportal.meetComEntities.EclMitteilung;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

@SessionScoped
@Named
public class TMitteilungSession implements Serializable {
    private static final long serialVersionUID = 949180474291938003L;

//    private int logDrucken=10;
    /**Übergreifend für:
     * > Fragen
     * > Widersprüche
     * > Anträge
     * > Wortmeldungen
     * > Sonstige Mitteilungen
     */

    /**Wird gesetzt, wenn gerade der Aufruf aus der Auswahl erfolgte. Anhand dessen kann dann beim eigentlichen
     * Init div. Felder initialisiert werden oder auch nicht (insbesondere Hinweis bestätigen)
     */
    private boolean erstaufrufAusAuswahl = false;

    /******************Parametrisierung - siehe entsprechende Parameter bei ParamPortal*************/
    private int stellerAbfragen = 0;
    private int nameAbfragen = 0;
    private int kontaktdatenAbfragen = 0;
    private int kontaktdatenEMailVorschlagen = 0;
    private int kontaktdatenTelefonAbfragen = 0;
    private int kurztextAbfragen = 0;
    private int topListeAnbieten = 0;
    private int skIstZuTopListe = 0;
    private int langtextAbfragen = 0;
    private int langtextUndDateiNurAlternativ=0;
    private int zurueckziehenMoeglich = 0;
    private int laenge = 10000;
    private int anzahlJeAktionaer = 1000;
    private int stellerZulaessig = 0;
    
    /**Inhalts-Hinweise*/
    private String[] inhaltsHinweiseTextDE=null;
    private String[] inhaltsHinweiseTextEN=null;
    private boolean[] inhaltsHinweiseAktiv=null;
    private boolean inhaltsHinweiseVorhandenundAktiv=false;
    private List<EhInhaltsHinweise> inhaltsHinweiseListe=null;

    private boolean inhaltsHinweiseAnbieten=false;
    
    /**Wortmeldeliste*/
    private int listeAnzeigen = 0;
    private int mailBeiEingang = 0;
    private String mailVerteiler1 = "";
    private String mailVerteiler2 = "";
    private String mailVerteiler3 = "";
    
    /**Websockets*/
    private boolean connectToWebSocket = false;

    /**Entspricht dem Parameter*/
    private int hinweisGelesen = 0;

    public boolean liefereAuswahlLangtextOderDateiAnzeigen() {
        if (dateiHochladenMoeglich==true && langtextAbfragen==1 && langtextUndDateiNurAlternativ==1) {
            return true;
        }
        return false;
    }
    
    private int auswahlLangtextOderDatei=1;
    
    public boolean liefereButtonAbschickenAnzeigen() {
        /*Wenn keine Dateihochladung möglich, dann muß der Button immer angezeigt werden.
         * Damit Fragen etc. abgedeckt
         */
        if (dateiHochladenMoeglich==false) {return true;}
        
        /*Hier: Dateihochladen ist immer zulässig*/
        
        /*Kein Langtext vorhanden => Button nie anzeigen*/
        if (langtextAbfragen==0) {return false;}
        
        if (liefereAuswahlLangtextOderDateiAnzeigen()==true) {
            if (auswahlLangtextOderDatei==1) {return true;}
            if (auswahlLangtextOderDatei==2) {return false;}
        }
        
        return true;
    }
    
    /*++++Parameter speziell (bzw. nur) für Botschaften hochladen. Aber natürlich auch für was anderes denkbar!+++*/
    private boolean dateiHochladenMoeglich = false;
    private int videoDateiMoeglich = 0;
    private String[] videoZusatz = null;
    private int[] videoFormate = null;
    private int videoLaenge = 0;

    private int textDateiMoeglich = 0;
    private String[] textDateiZusatz = null;
    private int[] textFormate = null;
    private int textLaenge = 0;
    
    private String uploadFilenameOriginalHidden = "";
    private String uploadFilenameInternHidden = "";
    private String uploadFileErrorHidden = "";
    private long uploadFilesizeHidden = 0L;

    /**********************Text-Nummern*****************************************************/
    private String textNrStarttextAktiv = "";
    private String textNrStarttextInaktiv = "";

    /**ausgangsView*/
    private String textNrAusgangsViewStarttext = "";
    private String textNrAusgangsViewButtonStart = "";
    private String textNrAusgangsViewEndetext = "";

    /**personBestaetigen*/
    private String textNrPersonBestaetigenStarttext = "";
    private String textNrPersonBestaetigenTextVorName = "";
    private String textNrPersonBestaetigenTextNachName = "";
    private String textNrPersonBestaetigenJaButton = "";
    private String textNrPersonBestaetigenNeinButton = "";

    /**mitteilungNichtMoeglichDaPersonNichtBestaetigt*/
    private String textNrPersonNichtBestaetigt = "";

    /**personenArtNichtZulaessig*/
    private String textNrPersonengemeinschaftNichtZulaessig = "";
    private String textNrJuristischePersonNichtZulaessig = "";

    /**mitteilungEingeben*/
    private String textNrMitteilungStarttext = "";
    private String textNrMitteilungNameAbfragen = "";
    private String textNrMitteilungKontaktAbfragen = "";
    private String textNrMitteilungKontaktTelefonAbfragen = "";
    private String textNrMitteilungHinweisGelesen = "";

    private String textNrVorTOPAuswahl = "1";
    private String textNrNachTOPAuswahl = "1";

    private String textNrVorInhaltsHinweise = "1";

    private String textNrMitteilungKurztext = "";
    private String textNrMitteilungVorKurzText = "";
    private String textNrMitteilungVorLangText = "";
    private String textNrMitteilungLangTextVorZaehler = "";
    private String textNrMitteilungLangTextNachZaehler = "";
    private String textNrStarttextHochladenBereich = "";
    private String textNrNochKeineDateiAusgewaehltHochladenBereich = ""; //1927
    private String textNrButtonDateiAuswaehlenHochladenBereich = ""; //1916
    private String textNrVorDateiNameHochladenBereich = ""; //1928
    private String textNrNachDateiNameHochladenBereich = ""; //1929
    private String textNrEndetextHochladenBereich = "";

    private String textNrMitteilungButtonAbschicken = "";
    private String textNrMitteilungEndetext = "";

    /**keineWeiterenMitteilungenMoeglich*/
    private String textNrKeineWeiterenMitteilungen = "";

    private String textNrEndetextAktiv = "";

    private String textNrListeStarttext = "";
    private String textNrListeButtonAktualisieren = "";
    private String textNrListeEndetext = "";

    private String textNrListeMitteilungenStarttext = "";
    private String textNrListeMitteilungenButtonZurueckziehen = "";
    private String textNrListeMitteilungenEndetext = "";

    /**Für Mail*/
    /**Wird beim Mail tatsächlich verwendet*/
    private String textNrMailBetreff = "";
    private String textNrMailBetreffErteilt = "";
    private String textNrMailBetreffZurueckgezogen = "";
    private String textNrMailLfdIdent = "";
    private String textNrMailKennung = "";
    private String textNrMailSteller = "";
    private String textNrMailNameAbgefragt = "";
    private String textNrMailKontaktdaten = "";
    private String textNrMailKontaktdatenTelefon = "";
    /**Wird nur verwendet, wenn keine Aktien (und damit keine Gattung) vorhanden sind. Ansonsten
     * werden die Gattungsvariablen aller vertretenen Gattungen zusammengesetzt*/
    private String textNrMailAktien = "";
    private String textNrMailKurztext = "";
    private String textNrMailLangtext = "";
    private String textNrMailZeitpunktErteilt = "";
    private String textNrMailZurueckgezogen = "";
    private String textNrMailZeitpuntZurueckgezogen = "";
    private String textNrMailHinweisBestaetigt = "";

    /*TODO Text wen Botschaftsdatei gesendet*/

    /**********"Echte" Dialogvariablen********************/
    /**Siehe KonstPortalFunktionen - gibt an, welche Funktion gerade aktiv ist*/
    private int artDerMitteilung = 0;

    /**
     * Siehe KonstMitteilungViewBereich
     */
    private int anzuzeigenderBereich = 0;

    /**Für personenArtNichtZulaessig: Je nachdem wird der entsprechende Text angezeigt*/
    private boolean juristischePersonNichtZulaessig = false;
    private boolean personengemeinschaftNichtZulaessig = false;

    private boolean funktionIstAktiv = false;

    private String kurzTextEingabe = "";
    private String langTextEingabe = "";

    /*++++++++++Angaben zur Person++++++++++++++++++++*/
    private boolean personenDatenBereitsAbgefragt = false;

    private String nameEingabe = "";
    private String kontaktdatenEingabe = "";
    private String kontaktdatenTelefonEingabe = "";

    /**Dienen nur einer Session.
     * D.h.: je Funktion werden die eingegebenen Kontaktdaten für einepermanenten Speicherung der Kontaktdaten während 
     * Session lang gespeichert und bei Wiederholung angeboten*/
    private String nameEingabeFragen = "";
    private String nameEingabeWortmeldungen = "";
    private String nameEingabeWidersprueche = "";
    private String nameEingabeAntraege = "";
    private String nameEingabeSonstigeMitteilungen = "";
    private String nameEingabeBotschaftenEinreichen = "";
    private String nameEingabeRueckfragen = "";

    private String kontaktDatenEingabeFragen = "";
    private String kontaktDatenEingabeWortmeldungen = "";
    private String kontaktDatenEingabeWidersprueche = "";
    private String kontaktDatenEingabeAntraege = "";
    private String kontaktDatenEingabeSonstigeMitteilungen = "";
    private String kontaktDatenEingabeBotschaftenEinreichen = "";
    private String kontaktDatenEingabeRueckfragen = "";

    private String kontaktDatenTelefonEingabeFragen = "";
    private String kontaktDatenTelefonEingabeWortmeldungen = "";
    private String kontaktDatenTelefonEingabeWidersprueche = "";
    private String kontaktDatenTelefonEingabeAntraege = "";
    private String kontaktDatenTelefonEingabeSonstigeMitteilungen = "";
    private String kontaktDatenTelefonEingabeBotschaftenEinreichen = "";
    private String kontaktDatenTelefonEingabeRueckfragen = "";

    /********************Hinweis bestätigt**********************************/
    private boolean hinweisBestaetigt = false;

    /******************Video-Hochladen***********************/
    private boolean fileWurdeAusgewaehlt = false;
    private String uploadFileName = "";

    /**
     * 0 = Text
     * 1 = Video
     */
    private int uploadFileTyp = 0;

    /*****************gestellte Mitteilungen******************************/
    private int anzahlMitteilungenGestellt = 0;
    private int anzahlMitteilungenGestelltOhneZurueckgezogen=0;
    private int anzahlMitteilungenOffenPfandbrief=0;
    
    private List<EclMitteilung> mitteilungenGestelltListe = null;

    /****************"Rednerliste"*************************/
    private List<EclMitteilung> rednerListe = null;
    private int anzahlRednerListe = 0;

    /*Für Background-Reports: wenn stehtZurVerfuegung=false, dann Refresh aufrufen*/
    private boolean stehtZurVerfuegung = false;
    private Future<String> futureRC = null;

    /******************Videokonferenzing Status******************/
    /**Siehe EclLoginDaten*/
    private int konferenzTestDurchgefuehrt=0;
    
    private int konferenzTestRaum=0;
    private int konferenzTestAblauf=0;
    
    private int konferenzSprechenRaum=0;
    private int konferenzSprechen=0;

    private int konferenzTestDurchgefuehrtAlt=0;
    private int konferenzTestAblaufAlt=0;
    private int konferenzSprechenAlt=0;
 
    
    /*++++Status - Test durchgeführt++++*/
    public boolean liefereKonferenzTestNochNichtDurchgefuehrt() {
        return konferenzTestDurchgefuehrt==0;
    }
    public boolean liefereKonferenzTestWarErfolgreich() {
        return konferenzTestDurchgefuehrt==1;
    }
    public boolean liefereKonferenzTestWarNichtErfolgreich() {
        return konferenzTestDurchgefuehrt==-1;
    }

    
    /*++++Status - Test beitreten++++++++++++++*/
    public boolean liefereKonferenzBitteFuerTestBereithalten() {
        return (konferenzTestAblauf & 1024)==1024;
    }

    public int liefereKonferenzTestRaumNr() {
        return konferenzTestRaum;
//        return (konferenzTestAblauf & 255);
    }
    
    public boolean liefereKonferenzInTestraumKommen() {
        return (konferenzTestAblauf & 256)==256;
    }
    
    public boolean liefereKonferenzTestraumVerlassen() {
        return (konferenzTestAblauf & 2048)==2048;
    }

    /*++++Status - Test beitreten++++++++++++++*/
    public boolean liefereKonferenzBitteFuerRedeBereithalten() {
        return (konferenzSprechen & 1024)==1024;
    }

    public int liefereKonferenzRedeRaumNr() {
        return konferenzSprechenRaum;
//        return (konferenzSprechen & 255);
    }
    
    public boolean liefereKonferenzInRederaumKommen() {
        return (konferenzSprechen & 256)==256;
    }

    public boolean liefereKonferenzInRederaumSprechen() {
        return (konferenzSprechen & 512)==512;
    }
    
    public boolean liefereKonferenzRederaumVerlassen() {
        return (konferenzSprechen & 2048)==2048;
    }
    
    /************** KonferenzURL ***************/
    private String konferenzUrl;

    /********Für Ergebnis-Anzeige************/
    private String anzeigeText = "";

    /********************Funktionen********************************/

    /**Aufrufen nach Login*/
    public void initNachLogin() {
        personenDatenBereitsAbgefragt = false;
        nameEingabe = "";
        kontaktdatenEingabe = "";
        kontaktdatenTelefonEingabe = "";
        
        nameEingabeFragen = "";
        nameEingabeWortmeldungen = "";
        nameEingabeWidersprueche = "";
        nameEingabeAntraege = "";
        nameEingabeSonstigeMitteilungen = "";
        nameEingabeBotschaftenEinreichen = "";
        nameEingabeRueckfragen = "";

        
        kontaktDatenEingabeFragen = "";
        kontaktDatenEingabeWortmeldungen = "";
        kontaktDatenEingabeWidersprueche = "";
        kontaktDatenEingabeAntraege = "";
        kontaktDatenEingabeSonstigeMitteilungen = "";
        kontaktDatenEingabeBotschaftenEinreichen = "";
        kontaktDatenEingabeRueckfragen = "";
        
        juristischePersonNichtZulaessig = false;
        personengemeinschaftNichtZulaessig = false;
        hinweisBestaetigt = false;
        
        inhaltsHinweiseTextDE=null;
        inhaltsHinweiseTextEN=null;
        inhaltsHinweiseAktiv=null;
        inhaltsHinweiseVorhandenundAktiv=false;
        inhaltsHinweiseListe=null;
        
        
        konferenzTestDurchgefuehrt=0;
        konferenzTestAblauf=0;
        konferenzSprechen=0;

        konferenzTestDurchgefuehrtAlt=0;
        konferenzTestAblaufAlt=0;
        konferenzSprechenAlt=0;

    }

    /**Wird vor jeder neuen Frage aufgerufen*/
    public void initNaechsteFrage() {
        kurzTextEingabe = "";
        langTextEingabe = "";
        fileWurdeAusgewaehlt = false;
        uploadFileName = "";
        uploadFilenameOriginalHidden = "";
        uploadFilenameInternHidden = "";
        uploadFilesizeHidden = 0;
        uploadFileErrorHidden = "";
    }

    /***********************Getter für erlaubte Dateiformate als String**********************/

    public String getErlaubteUploadFormateVideo() {
        String formate = "";
        for (int i = 0; i < this.videoZusatz.length; i++) {
            if (this.videoFormate[i] == 1) {
                formate += videoZusatz[i] + ",";
            }
        }
        if (formate.endsWith(",")) {
            formate = formate.substring(0, formate.length() - 1);
        }
        return formate;
    }

    public String getErlaubteUploadFormateText() {
        String formate = "";
        for (int i = 0; i < this.textDateiZusatz.length; i++) {
            if (this.textFormate[i] == 1) {
                formate += textDateiZusatz[i] + ",";
            }
        }
        if (formate.endsWith(",")) {
            formate = formate.substring(0, formate.length() - 1);
        }
        return formate;
    }

    public String getErlaubteUploadFormateVideoJS() {
        String formate = getErlaubteUploadFormateVideo();
        formate = formate.replace("MP4", "mp4");
        formate = formate.replace("AVI-Typ1", "avi");
        formate = formate.replace("AVI-Typ2", "avi");
        formate = formate.replace("MOV", "mov");
        formate = formate.replace("MPEG", "mpeg4,mpeg,mpg");
        formate = formate.replace("WMV", "wmv");
        return formate;
    }

    public String getErlaubteUploadFormateTextJS() {
        String formate = getErlaubteUploadFormateText();
        formate = formate.replace("TXT", "txt");
        formate = formate.replace("PDF", "pdf");
        formate = formate.replace("DOCX", "docx");
        formate = formate.replace("PPTX", "pptx");
        formate = formate.replace("XLSX", "xlsx");
        formate = formate.replace("ODT", "odt");
        formate = formate.replace("CSV", "csv");
        return formate;
    }
    
    public String getErlaubteUploadFormateKomplettJS() {
        String formate = getErlaubteUploadFormateVideoJS() + "," + getErlaubteUploadFormateTextJS();
        return formate;
    }

    /***********************Standard getter und setter***************************************/

    public int getStellerAbfragen() {
        return stellerAbfragen;
    }

    public void setStellerAbfragen(int stellerAbfragen) {
        this.stellerAbfragen = stellerAbfragen;
    }

    public int getNameAbfragen() {
        return nameAbfragen;
    }

    public void setNameAbfragen(int nameAbfragen) {
        this.nameAbfragen = nameAbfragen;
    }

    public int getKontaktdatenAbfragen() {
        return kontaktdatenAbfragen;
    }

    public void setKontaktdatenAbfragen(int kontaktdatenAbfragen) {
        this.kontaktdatenAbfragen = kontaktdatenAbfragen;
    }

    public int getKontaktdatenEMailVorschlagen() {
        return kontaktdatenEMailVorschlagen;
    }

    public void setKontaktdatenEMailVorschlagen(int kontaktdatenEMailVorschlagen) {
        this.kontaktdatenEMailVorschlagen = kontaktdatenEMailVorschlagen;
    }

    public int getKurztextAbfragen() {
        return kurztextAbfragen;
    }

    public void setKurztextAbfragen(int kurztextAbfragen) {
        this.kurztextAbfragen = kurztextAbfragen;
    }

    public int getTopListeAnbieten() {
        return topListeAnbieten;
    }

    public void setTopListeAnbieten(int topListeAnbieten) {
        this.topListeAnbieten = topListeAnbieten;
    }

    public int getLangtextAbfragen() {
        return langtextAbfragen;
    }

    public void setLangtextAbfragen(int langtextAbfragen) {
        this.langtextAbfragen = langtextAbfragen;
    }

    public int getZurueckziehenMoeglich() {
        return zurueckziehenMoeglich;
    }

    public void setZurueckziehenMoeglich(int zurueckziehenMoeglich) {
        this.zurueckziehenMoeglich = zurueckziehenMoeglich;
    }

    public int getLaenge() {
        return laenge;
    }

    public void setLaenge(int laenge) {
        this.laenge = laenge;
    }

    public int getAnzahlJeAktionaer() {
        return anzahlJeAktionaer;
    }

    public void setAnzahlJeAktionaer(int anzahlJeAktionaer) {
        this.anzahlJeAktionaer = anzahlJeAktionaer;
    }

    public int getStellerZulaessig() {
        return stellerZulaessig;
    }

    public void setStellerZulaessig(int stellerZulaessig) {
        this.stellerZulaessig = stellerZulaessig;
    }

    public int getListeAnzeigen() {
        return listeAnzeigen;
    }

    public void setListeAnzeigen(int listeAnzeigen) {
        this.listeAnzeigen = listeAnzeigen;
    }

    public int getMailBeiEingang() {
        return mailBeiEingang;
    }

    public void setMailBeiEingang(int mailBeiEingang) {
        this.mailBeiEingang = mailBeiEingang;
    }

    public String getMailVerteiler1() {
        return mailVerteiler1;
    }

    public void setMailVerteiler1(String mailVerteiler1) {
        this.mailVerteiler1 = mailVerteiler1;
    }

    public String getMailVerteiler2() {
        return mailVerteiler2;
    }

    public void setMailVerteiler2(String mailVerteiler2) {
        this.mailVerteiler2 = mailVerteiler2;
    }

    public String getMailVerteiler3() {
        return mailVerteiler3;
    }

    public void setMailVerteiler3(String mailVerteiler3) {
        this.mailVerteiler3 = mailVerteiler3;
    }

    public boolean isPersonenDatenBereitsAbgefragt() {
        return personenDatenBereitsAbgefragt;
    }

    public void setPersonenDatenBereitsAbgefragt(boolean personenDatenBereitsAbgefragt) {
        this.personenDatenBereitsAbgefragt = personenDatenBereitsAbgefragt;
    }

    public String getNameEingabe() {
        return nameEingabe;
    }

    public void setNameEingabe(String nameEingabe) {
        this.nameEingabe = nameEingabe;
    }

    public String getKontaktdatenEingabe() {
        return kontaktdatenEingabe;
    }

    public void setKontaktdatenEingabe(String kontaktdatenEingabe) {
        this.kontaktdatenEingabe = kontaktdatenEingabe;
    }

    public int getArtDerMitteilung() {
        return artDerMitteilung;
    }

    public void setArtDerMitteilung(int artDerMitteilung) {
        this.artDerMitteilung = artDerMitteilung;
    }

    public String getKontaktDatenEingabeFragen() {
        return kontaktDatenEingabeFragen;
    }

    public void setKontaktDatenEingabeFragen(String kontaktDatenEingabeFragen) {
        this.kontaktDatenEingabeFragen = kontaktDatenEingabeFragen;
    }

    public String getKontaktDatenEingabeWortmeldungen() {
        return kontaktDatenEingabeWortmeldungen;
    }

    public void setKontaktDatenEingabeWortmeldungen(String kontaktDatenEingabeWortmeldungen) {
        this.kontaktDatenEingabeWortmeldungen = kontaktDatenEingabeWortmeldungen;
    }

    public String getKontaktDatenEingabeWidersprueche() {
        return kontaktDatenEingabeWidersprueche;
    }

    public void setKontaktDatenEingabeWidersprueche(String kontaktDatenEingabeWidersprueche) {
        this.kontaktDatenEingabeWidersprueche = kontaktDatenEingabeWidersprueche;
    }

    public String getKontaktDatenEingabeAntraege() {
        return kontaktDatenEingabeAntraege;
    }

    public void setKontaktDatenEingabeAntraege(String kontaktDatenEingabeAntraege) {
        this.kontaktDatenEingabeAntraege = kontaktDatenEingabeAntraege;
    }

    public String getKontaktDatenEingabeSonstigeMitteilungen() {
        return kontaktDatenEingabeSonstigeMitteilungen;
    }

    public void setKontaktDatenEingabeSonstigeMitteilungen(String kontaktDatenEingabeSonstigeMitteilungen) {
        this.kontaktDatenEingabeSonstigeMitteilungen = kontaktDatenEingabeSonstigeMitteilungen;
    }

    public int getAnzuzeigenderBereich() {
        return anzuzeigenderBereich;
    }

    public void setAnzuzeigenderBereich(int anzuzeigenderBereich) {
        this.anzuzeigenderBereich = anzuzeigenderBereich;
    }

    public String getKurzTextEingabe() {
        return kurzTextEingabe;
    }

    public void setKurzTextEingabe(String kurzTextEingabe) {
        this.kurzTextEingabe = kurzTextEingabe;
    }

    public String getLangTextEingabe() {
        return langTextEingabe;
    }

    public void setLangTextEingabe(String langTextEingabe) {
        this.langTextEingabe = langTextEingabe;
    }

    public boolean isFunktionIstAktiv() {
        return funktionIstAktiv;
    }

    public void setFunktionIstAktiv(boolean funktionIstAktiv) {
        this.funktionIstAktiv = funktionIstAktiv;
    }

    public String getTextNrStarttextAktiv() {
        return textNrStarttextAktiv;
    }

    public void setTextNrStarttextAktiv(String textNrStarttextAktiv) {
        this.textNrStarttextAktiv = textNrStarttextAktiv;
    }

    public String getTextNrStarttextInaktiv() {
        return textNrStarttextInaktiv;
    }

    public void setTextNrStarttextInaktiv(String textNrStarttextInaktiv) {
        this.textNrStarttextInaktiv = textNrStarttextInaktiv;
    }

    public String getTextNrAusgangsViewStarttext() {
        return textNrAusgangsViewStarttext;
    }

    public void setTextNrAusgangsViewStarttext(String textNrAusgangsViewStarttext) {
        this.textNrAusgangsViewStarttext = textNrAusgangsViewStarttext;
    }

    public String getTextNrAusgangsViewButtonStart() {
        return textNrAusgangsViewButtonStart;
    }

    public void setTextNrAusgangsViewButtonStart(String textNrAusgangsViewButtonStart) {
        this.textNrAusgangsViewButtonStart = textNrAusgangsViewButtonStart;
    }

    public String getTextNrAusgangsViewEndetext() {
        return textNrAusgangsViewEndetext;
    }

    public void setTextNrAusgangsViewEndetext(String textNrAusgangsViewEndetext) {
        this.textNrAusgangsViewEndetext = textNrAusgangsViewEndetext;
    }

    public int getAnzahlMitteilungenGestellt() {
        return anzahlMitteilungenGestellt;
    }

    public void setAnzahlMitteilungenGestellt(int anzahlMitteilungenGestellt) {
        this.anzahlMitteilungenGestellt = anzahlMitteilungenGestellt;
    }

    public String getTextNrPersonBestaetigenTextVorName() {
        return textNrPersonBestaetigenTextVorName;
    }

    public void setTextNrPersonBestaetigenTextVorName(String textNrPersonBestaetigenTextVorName) {
        this.textNrPersonBestaetigenTextVorName = textNrPersonBestaetigenTextVorName;
    }

    public String getTextNrPersonBestaetigenTextNachName() {
        return textNrPersonBestaetigenTextNachName;
    }

    public void setTextNrPersonBestaetigenTextNachName(String textNrPersonBestaetigenTextNachName) {
        this.textNrPersonBestaetigenTextNachName = textNrPersonBestaetigenTextNachName;
    }

    public String getTextNrPersonBestaetigenJaButton() {
        return textNrPersonBestaetigenJaButton;
    }

    public void setTextNrPersonBestaetigenJaButton(String textNrPersonBestaetigenJaButton) {
        this.textNrPersonBestaetigenJaButton = textNrPersonBestaetigenJaButton;
    }

    public String getTextNrPersonBestaetigenNeinButton() {
        return textNrPersonBestaetigenNeinButton;
    }

    public void setTextNrPersonBestaetigenNeinButton(String textNrPersonBestaetigenNeinButton) {
        this.textNrPersonBestaetigenNeinButton = textNrPersonBestaetigenNeinButton;
    }

    public String getTextNrPersonBestaetigenStarttext() {
        return textNrPersonBestaetigenStarttext;
    }

    public void setTextNrPersonBestaetigenStarttext(String textNrPersonBestaetigenStarttext) {
        this.textNrPersonBestaetigenStarttext = textNrPersonBestaetigenStarttext;
    }

    public String getTextNrPersonNichtBestaetigt() {
        return textNrPersonNichtBestaetigt;
    }

    public void setTextNrPersonNichtBestaetigt(String textNrPersonNichtBestaetigt) {
        this.textNrPersonNichtBestaetigt = textNrPersonNichtBestaetigt;
    }

    public boolean isJuristischePersonNichtZulaessig() {
        return juristischePersonNichtZulaessig;
    }

    public void setJuristischePersonNichtZulaessig(boolean juristischePersonNichtZulaessig) {
        this.juristischePersonNichtZulaessig = juristischePersonNichtZulaessig;
    }

    public boolean isPersonengemeinschaftNichtZulaessig() {
        return personengemeinschaftNichtZulaessig;
    }

    public void setPersonengemeinschaftNichtZulaessig(boolean personengemeinschaftNichtZulaessig) {
        this.personengemeinschaftNichtZulaessig = personengemeinschaftNichtZulaessig;
    }

    public String getTextNrPersonengemeinschaftNichtZulaessig() {
        return textNrPersonengemeinschaftNichtZulaessig;
    }

    public void setTextNrPersonengemeinschaftNichtZulaessig(String textNrPersonengemeinschaftNichtZulaessig) {
        this.textNrPersonengemeinschaftNichtZulaessig = textNrPersonengemeinschaftNichtZulaessig;
    }

    public String getTextNrJuristischePersonNichtZulaessig() {
        return textNrJuristischePersonNichtZulaessig;
    }

    public void setTextNrJuristischePersonNichtZulaessig(String textNrJuristischePersonNichtZulaessig) {
        this.textNrJuristischePersonNichtZulaessig = textNrJuristischePersonNichtZulaessig;
    }

    public List<EclMitteilung> getMitteilungenGestelltListe() {
        return mitteilungenGestelltListe;
    }

    public void setMitteilungenGestelltListe(List<EclMitteilung> mitteilungenGestelltListe) {
        this.mitteilungenGestelltListe = mitteilungenGestelltListe;
    }

    public String getTextNrMitteilungStarttext() {
        return textNrMitteilungStarttext;
    }

    public void setTextNrMitteilungStarttext(String textNrMitteilungStarttext) {
        this.textNrMitteilungStarttext = textNrMitteilungStarttext;
    }

    public String getTextNrMitteilungNameAbfragen() {
        return textNrMitteilungNameAbfragen;
    }

    public void setTextNrMitteilungNameAbfragen(String textNrMitteilungNameAbfragen) {
        this.textNrMitteilungNameAbfragen = textNrMitteilungNameAbfragen;
    }

    public String getTextNrMitteilungKontaktAbfragen() {
        return textNrMitteilungKontaktAbfragen;
    }

    public void setTextNrMitteilungKontaktAbfragen(String textNrMitteilungKontaktAbfragen) {
        this.textNrMitteilungKontaktAbfragen = textNrMitteilungKontaktAbfragen;
    }

    public String getTextNrMitteilungKurztext() {
        return textNrMitteilungKurztext;
    }

    public void setTextNrMitteilungKurztext(String textNrMitteilungKurztext) {
        this.textNrMitteilungKurztext = textNrMitteilungKurztext;
    }

    public String getTextNrMitteilungVorKurzText() {
        return textNrMitteilungVorKurzText;
    }

    public void setTextNrMitteilungVorKurzText(String textNrMitteilungVorKurzText) {
        this.textNrMitteilungVorKurzText = textNrMitteilungVorKurzText;
    }

    public String getTextNrMitteilungVorLangText() {
        return textNrMitteilungVorLangText;
    }

    public void setTextNrMitteilungVorLangText(String textNrMitteilungVorLangText) {
        this.textNrMitteilungVorLangText = textNrMitteilungVorLangText;
    }

    public String getTextNrMitteilungLangTextVorZaehler() {
        return textNrMitteilungLangTextVorZaehler;
    }

    public void setTextNrMitteilungLangTextVorZaehler(String textNrMitteilungLangTextVorZaehler) {
        this.textNrMitteilungLangTextVorZaehler = textNrMitteilungLangTextVorZaehler;
    }

    public String getTextNrMitteilungLangTextNachZaehler() {
        return textNrMitteilungLangTextNachZaehler;
    }

    public void setTextNrMitteilungLangTextNachZaehler(String textNrMitteilungLangTextNachZaehler) {
        this.textNrMitteilungLangTextNachZaehler = textNrMitteilungLangTextNachZaehler;
    }

    public String getTextNrMitteilungButtonAbschicken() {
        return textNrMitteilungButtonAbschicken;
    }

    public void setTextNrMitteilungButtonAbschicken(String textNrMitteilungButtonAbschicken) {
        this.textNrMitteilungButtonAbschicken = textNrMitteilungButtonAbschicken;
    }

    public String getTextNrMitteilungEndetext() {
        return textNrMitteilungEndetext;
    }

    public void setTextNrMitteilungEndetext(String textNrMitteilungEndetext) {
        this.textNrMitteilungEndetext = textNrMitteilungEndetext;
    }

    public String getTextNrKeineWeiterenMitteilungen() {
        return textNrKeineWeiterenMitteilungen;
    }

    public void setTextNrKeineWeiterenMitteilungen(String textNrKeineWeiterenMitteilungen) {
        this.textNrKeineWeiterenMitteilungen = textNrKeineWeiterenMitteilungen;
    }

    public String getTextNrEndetextAktiv() {
        return textNrEndetextAktiv;
    }

    public void setTextNrEndetextAktiv(String textNrEndetextAktiv) {
        this.textNrEndetextAktiv = textNrEndetextAktiv;
    }

    public String getTextNrListeStarttext() {
        return textNrListeStarttext;
    }

    public void setTextNrListeStarttext(String textNrListeStarttext) {
        this.textNrListeStarttext = textNrListeStarttext;
    }

    public String getTextNrListeButtonAktualisieren() {
        return textNrListeButtonAktualisieren;
    }

    public void setTextNrListeButtonAktualisieren(String textNrListeButtonAktualisieren) {
        this.textNrListeButtonAktualisieren = textNrListeButtonAktualisieren;
    }

    public String getTextNrListeEndetext() {
        return textNrListeEndetext;
    }

    public void setTextNrListeEndetext(String textNrListeEndetext) {
        this.textNrListeEndetext = textNrListeEndetext;
    }

    public int getAnzahlRednerListe() {
        return anzahlRednerListe;
    }

    public void setAnzahlRednerListe(int anzahlRednerListe) {
        this.anzahlRednerListe = anzahlRednerListe;
    }

    public String getTextNrListeMitteilungenStarttext() {
        return textNrListeMitteilungenStarttext;
    }

    public void setTextNrListeMitteilungenStarttext(String textNrListeMitteilungenStarttext) {
        this.textNrListeMitteilungenStarttext = textNrListeMitteilungenStarttext;
    }

    public String getTextNrListeMitteilungenEndetext() {
        return textNrListeMitteilungenEndetext;
    }

    public void setTextNrListeMitteilungenEndetext(String textNrListeMitteilungenEndetext) {
        this.textNrListeMitteilungenEndetext = textNrListeMitteilungenEndetext;
    }

    public String getTextNrMailBetreff() {
        return textNrMailBetreff;
    }

    public void setTextNrMailBetreff(String textNrMailBetreff) {
        this.textNrMailBetreff = textNrMailBetreff;
    }

    public String getTextNrMailBetreffErteilt() {
        return textNrMailBetreffErteilt;
    }

    public void setTextNrMailBetreffErteilt(String textNrMailBetreffErteilt) {
        this.textNrMailBetreffErteilt = textNrMailBetreffErteilt;
    }

    public String getTextNrMailBetreffZurueckgezogen() {
        return textNrMailBetreffZurueckgezogen;
    }

    public void setTextNrMailBetreffZurueckgezogen(String textNrMailBetreffZurueckgezogen) {
        this.textNrMailBetreffZurueckgezogen = textNrMailBetreffZurueckgezogen;
    }

    public String getTextNrMailLfdIdent() {
        return textNrMailLfdIdent;
    }

    public void setTextNrMailLfdIdent(String textNrMailLfdIdent) {
        this.textNrMailLfdIdent = textNrMailLfdIdent;
    }

    public String getTextNrMailKennung() {
        return textNrMailKennung;
    }

    public void setTextNrMailKennung(String textNrMailKennung) {
        this.textNrMailKennung = textNrMailKennung;
    }

    public String getTextNrMailSteller() {
        return textNrMailSteller;
    }

    public void setTextNrMailSteller(String textNrMailSteller) {
        this.textNrMailSteller = textNrMailSteller;
    }

    public String getTextNrMailNameAbgefragt() {
        return textNrMailNameAbgefragt;
    }

    public void setTextNrMailNameAbgefragt(String textNrMailNameAbgefragt) {
        this.textNrMailNameAbgefragt = textNrMailNameAbgefragt;
    }

    public String getTextNrMailKontaktdaten() {
        return textNrMailKontaktdaten;
    }

    public void setTextNrMailKontaktdaten(String textNrMailKontaktdaten) {
        this.textNrMailKontaktdaten = textNrMailKontaktdaten;
    }

    public String getTextNrMailAktien() {
        return textNrMailAktien;
    }

    public void setTextNrMailAktien(String textNrMailAktien) {
        this.textNrMailAktien = textNrMailAktien;
    }

    public String getTextNrMailZeitpunktErteilt() {
        return textNrMailZeitpunktErteilt;
    }

    public void setTextNrMailZeitpunktErteilt(String textNrMailZeitpunktErteilt) {
        this.textNrMailZeitpunktErteilt = textNrMailZeitpunktErteilt;
    }

    public String getTextNrMailZurueckgezogen() {
        return textNrMailZurueckgezogen;
    }

    public void setTextNrMailZurueckgezogen(String textNrMailZurueckgezogen) {
        this.textNrMailZurueckgezogen = textNrMailZurueckgezogen;
    }

    public String getTextNrMailZeitpuntZurueckgezogen() {
        return textNrMailZeitpuntZurueckgezogen;
    }

    public void setTextNrMailZeitpuntZurueckgezogen(String textNrMailZeitpuntZurueckgezogen) {
        this.textNrMailZeitpuntZurueckgezogen = textNrMailZeitpuntZurueckgezogen;
    }

    public String getTextNrMailKurztext() {
        return textNrMailKurztext;
    }

    public void setTextNrMailKurztext(String textNrMailKurztext) {
        this.textNrMailKurztext = textNrMailKurztext;
    }

    public String getTextNrMailLangtext() {
        return textNrMailLangtext;
    }

    public void setTextNrMailLangtext(String textNrMailLangtext) {
        this.textNrMailLangtext = textNrMailLangtext;
    }

    public String getTextNrListeMitteilungenButtonZurueckziehen() {
        return textNrListeMitteilungenButtonZurueckziehen;
    }

    public void setTextNrListeMitteilungenButtonZurueckziehen(String textNrListeMitteilungenButtonZurueckziehen) {
        this.textNrListeMitteilungenButtonZurueckziehen = textNrListeMitteilungenButtonZurueckziehen;
    }

    public List<EclMitteilung> getRednerListe() {
        return rednerListe;
    }

    public void setRednerListe(List<EclMitteilung> rednerListe) {
        this.rednerListe = rednerListe;
    }

    public int getHinweisGelesen() {
        return hinweisGelesen;
    }

    public void setHinweisGelesen(int hinweisGelesen) {
        this.hinweisGelesen = hinweisGelesen;
    }

    public boolean isDateiHochladenMoeglich() {
        return dateiHochladenMoeglich;
    }

    public void setDateiHochladenMoeglich(boolean dateiHochladenMoeglich) {
        this.dateiHochladenMoeglich = dateiHochladenMoeglich;
    }

    public int getVideoDateiMoeglich() {
        return videoDateiMoeglich;
    }

    public void setVideoDateiMoeglich(int videoDateiMoeglich) {
        this.videoDateiMoeglich = videoDateiMoeglich;
    }

    public String[] getVideoZusatz() {
        return videoZusatz;
    }

    public void setVideoZusatz(String[] videoZusatz) {
        this.videoZusatz = videoZusatz;
    }

    public int[] getVideoFormate() {
        return videoFormate;
    }

    public void setVideoFormate(int[] videoFormate) {
        this.videoFormate = videoFormate;
    }

    public int getVideoLaenge() {
        return videoLaenge;
    }

    public void setVideoLaenge(int videoLaenge) {
        this.videoLaenge = videoLaenge;
    }

    public int getTextDateiMoeglich() {
        return textDateiMoeglich;
    }

    public void setTextDateiMoeglich(int textDateiMoeglich) {
        this.textDateiMoeglich = textDateiMoeglich;
    }

    public String[] getTextDateiZusatz() {
        return textDateiZusatz;
    }

    public void setTextDateiZusatz(String[] textDateiZusatz) {
        this.textDateiZusatz = textDateiZusatz;
    }

    public int[] getTextFormate() {
        return textFormate;
    }

    public void setTextFormate(int[] textFormate) {
        this.textFormate = textFormate;
    }

    public int getTextLaenge() {
        return textLaenge;
    }

    public void setTextLaenge(int textLaenge) {
        this.textLaenge = textLaenge;
    }

    public String getTextNrMitteilungHinweisGelesen() {
        return textNrMitteilungHinweisGelesen;
    }

    public void setTextNrMitteilungHinweisGelesen(String textNrMitteilungHinweisGelesen) {
        this.textNrMitteilungHinweisGelesen = textNrMitteilungHinweisGelesen;
    }

    public boolean isHinweisBestaetigt() {
        return hinweisBestaetigt;
    }

    public void setHinweisBestaetigt(boolean hinweisBestaetigt) {
        this.hinweisBestaetigt = hinweisBestaetigt;
    }

    public String getTextNrStarttextHochladenBereich() {
        return textNrStarttextHochladenBereich;
    }

    public void setTextNrStarttextHochladenBereich(String textNrStarttextHochladenBereich) {
        this.textNrStarttextHochladenBereich = textNrStarttextHochladenBereich;
    }

    public String getKontaktDatenEingabeBotschaftenEinreichen() {
        return kontaktDatenEingabeBotschaftenEinreichen;
    }

    public void setKontaktDatenEingabeBotschaftenEinreichen(String kontaktDatenEingabeBotschaftenEinreichen) {
        this.kontaktDatenEingabeBotschaftenEinreichen = kontaktDatenEingabeBotschaftenEinreichen;
    }

    public String getKontaktDatenEingabeRueckfragen() {
        return kontaktDatenEingabeRueckfragen;
    }

    public void setKontaktDatenEingabeRueckfragen(String kontaktDatenEingabeRueckfragen) {
        this.kontaktDatenEingabeRueckfragen = kontaktDatenEingabeRueckfragen;
    }

    public int getSkIstZuTopListe() {
        return skIstZuTopListe;
    }

    public void setSkIstZuTopListe(int skIstZuTopListe) {
        this.skIstZuTopListe = skIstZuTopListe;
    }

    public String getTextNrVorTOPAuswahl() {
        return textNrVorTOPAuswahl;
    }

    public void setTextNrVorTOPAuswahl(String textNrVorTOPAuswahl) {
        this.textNrVorTOPAuswahl = textNrVorTOPAuswahl;
    }

    public String getTextNrNachTOPAuswahl() {
        return textNrNachTOPAuswahl;
    }

    public void setTextNrNachTOPAuswahl(String textNrNachTOPAuswahl) {
        this.textNrNachTOPAuswahl = textNrNachTOPAuswahl;
    }

    public String getUploadFileName() {
        return uploadFileName;
    }

    public void setUploadFileName(String uploadFileName) {
        this.uploadFileName = uploadFileName;
    }

    public String getTextNrEndetextHochladenBereich() {
        return textNrEndetextHochladenBereich;
    }

    public void setTextNrEndetextHochladenBereich(String textNrEndetextHochladenBereich) {
        this.textNrEndetextHochladenBereich = textNrEndetextHochladenBereich;
    }

    public boolean isErstaufrufAusAuswahl() {
        return erstaufrufAusAuswahl;
    }

    public void setErstaufrufAusAuswahl(boolean erstaufrufAusAuswahl) {
        this.erstaufrufAusAuswahl = erstaufrufAusAuswahl;
    }

    public String getTextNrMailHinweisBestaetigt() {
        return textNrMailHinweisBestaetigt;
    }

    public void setTextNrMailHinweisBestaetigt(String textNrMailHinweisBestaetigt) {
        this.textNrMailHinweisBestaetigt = textNrMailHinweisBestaetigt;
    }

    public boolean isFileWurdeAusgewaehlt() {
        return fileWurdeAusgewaehlt;
    }

    public void setFileWurdeAusgewaehlt(boolean fileWurdeAusgewaehlt) {
        this.fileWurdeAusgewaehlt = fileWurdeAusgewaehlt;
    }

    public String getTextNrNochKeineDateiAusgewaehltHochladenBereich() {
        return textNrNochKeineDateiAusgewaehltHochladenBereich;
    }

    public void setTextNrNochKeineDateiAusgewaehltHochladenBereich(String textNrNochKeineDateiAusgewaehltHochladenBereich) {
        this.textNrNochKeineDateiAusgewaehltHochladenBereich = textNrNochKeineDateiAusgewaehltHochladenBereich;
    }

    public String getTextNrNachDateiNameHochladenBereich() {
        return textNrNachDateiNameHochladenBereich;
    }

    public void setTextNrNachDateiNameHochladenBereich(String textNrNachDateiNameHochladenBereich) {
        this.textNrNachDateiNameHochladenBereich = textNrNachDateiNameHochladenBereich;
    }

    public String getTextNrButtonDateiAuswaehlenHochladenBereich() {
        return textNrButtonDateiAuswaehlenHochladenBereich;
    }

    public void setTextNrButtonDateiAuswaehlenHochladenBereich(String textNrButtonDateiAuswaehlenHochladenBereich) {
        this.textNrButtonDateiAuswaehlenHochladenBereich = textNrButtonDateiAuswaehlenHochladenBereich;
    }

    public String getTextNrVorDateiNameHochladenBereich() {
        return textNrVorDateiNameHochladenBereich;
    }

    public void setTextNrVorDateiNameHochladenBereich(String textNrVorDateiNameHochladenBereich) {
        this.textNrVorDateiNameHochladenBereich = textNrVorDateiNameHochladenBereich;
    }

    public boolean isStehtZurVerfuegung() {
        return stehtZurVerfuegung;
    }

    public void setStehtZurVerfuegung(boolean stehtZurVerfuegung) {
        this.stehtZurVerfuegung = stehtZurVerfuegung;
    }

    public Future<String> getFutureRC() {
        return futureRC;
    }

    public void setFutureRC(Future<String> futureRC) {
        this.futureRC = futureRC;
    }

    public String getAnzeigeText() {
        return anzeigeText;
    }

    public void setAnzeigeText(String anzeigeText) {
        this.anzeigeText = anzeigeText;
    }

    public int getUploadFileTyp() {
        return uploadFileTyp;
    }

    public void setUploadFileTyp(int uploadFileTyp) {
        this.uploadFileTyp = uploadFileTyp;
    }

    public String getUploadFilenameOriginalHidden() {
        return uploadFilenameOriginalHidden;
    }

    public void setUploadFilenameOriginalHidden(String uploadFilenameOriginalHidden) {
        this.uploadFilenameOriginalHidden = uploadFilenameOriginalHidden;
    }

    public String getUploadFilenameInternHidden() {
        return uploadFilenameInternHidden;
    }

    public void setUploadFilenameInternHidden(String uploadFilenameInternHidden) {
        this.uploadFilenameInternHidden = uploadFilenameInternHidden;
    }

    public String getUploadFileErrorHidden() {
        return uploadFileErrorHidden;
    }

    public void setUploadFileErrorHidden(String uploadFileErrorHidden) {
        this.uploadFileErrorHidden = uploadFileErrorHidden;
    }

    public long getUploadFilesizeHidden() {
        return uploadFilesizeHidden;
    }

    public void setUploadFilesizeHidden(long uploadFilesizeHidden) {
        this.uploadFilesizeHidden = uploadFilesizeHidden;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public String getNameEingabeFragen() {
        return nameEingabeFragen;
    }

    public void setNameEingabeFragen(String nameEingabeFragen) {
        this.nameEingabeFragen = nameEingabeFragen;
    }

    public String getNameEingabeWortmeldungen() {
        return nameEingabeWortmeldungen;
    }

    public void setNameEingabeWortmeldungen(String nameEingabeWortmeldungen) {
        this.nameEingabeWortmeldungen = nameEingabeWortmeldungen;
    }

    public String getNameEingabeWidersprueche() {
        return nameEingabeWidersprueche;
    }

    public void setNameEingabeWidersprueche(String nameEingabeWidersprueche) {
        this.nameEingabeWidersprueche = nameEingabeWidersprueche;
    }

    public String getNameEingabeAntraege() {
        return nameEingabeAntraege;
    }

    public void setNameEingabeAntraege(String nameEingabeAntraege) {
        this.nameEingabeAntraege = nameEingabeAntraege;
    }

    public String getNameEingabeSonstigeMitteilungen() {
        return nameEingabeSonstigeMitteilungen;
    }

    public void setNameEingabeSonstigeMitteilungen(String nameEingabeSonstigeMitteilungen) {
        this.nameEingabeSonstigeMitteilungen = nameEingabeSonstigeMitteilungen;
    }

    public String getNameEingabeBotschaftenEinreichen() {
        return nameEingabeBotschaftenEinreichen;
    }

    public void setNameEingabeBotschaftenEinreichen(String nameEingabeBotschaftenEinreichen) {
        this.nameEingabeBotschaftenEinreichen = nameEingabeBotschaftenEinreichen;
    }

    public String getNameEingabeRueckfragen() {
        return nameEingabeRueckfragen;
    }

    public void setNameEingabeRueckfragen(String nameEingabeRueckfragen) {
        this.nameEingabeRueckfragen = nameEingabeRueckfragen;
    }

    public int getAnzahlMitteilungenGestelltOhneZurueckgezogen() {
        return anzahlMitteilungenGestelltOhneZurueckgezogen;
    }

    public void setAnzahlMitteilungenGestelltOhneZurueckgezogen(int anzahlMitteilungenGestelltOhneZurueckgezogen) {
        this.anzahlMitteilungenGestelltOhneZurueckgezogen = anzahlMitteilungenGestelltOhneZurueckgezogen;
    }

    public int getLangtextUndDateiNurAlternativ() {
        return langtextUndDateiNurAlternativ;
    }

    public void setLangtextUndDateiNurAlternativ(int langtextUndDateiNurAlternativ) {
        this.langtextUndDateiNurAlternativ = langtextUndDateiNurAlternativ;
    }

    public int getAuswahlLangtextOderDatei() {
        return auswahlLangtextOderDatei;
    }

    public void setAuswahlLangtextOderDatei(int auswahlLangtextOderDatei) {
        this.auswahlLangtextOderDatei = auswahlLangtextOderDatei;
    }

    public int getKontaktdatenTelefonAbfragen() {
        return kontaktdatenTelefonAbfragen;
    }

    public void setKontaktdatenTelefonAbfragen(int kontaktdatenTelefonAbfragen) {
        this.kontaktdatenTelefonAbfragen = kontaktdatenTelefonAbfragen;
    }

    public String[] getInhaltsHinweiseTextDE() {
        return inhaltsHinweiseTextDE;
    }

    public void setInhaltsHinweiseTextDE(String[] inhaltsHinweiseTextDE) {
        this.inhaltsHinweiseTextDE = inhaltsHinweiseTextDE;
    }

    public String[] getInhaltsHinweiseTextEN() {
        return inhaltsHinweiseTextEN;
    }

    public void setInhaltsHinweiseTextEN(String[] inhaltsHinweiseTextEN) {
        this.inhaltsHinweiseTextEN = inhaltsHinweiseTextEN;
    }

 
    public String getTextNrMitteilungKontaktTelefonAbfragen() {
        return textNrMitteilungKontaktTelefonAbfragen;
    }

    public void setTextNrMitteilungKontaktTelefonAbfragen(String textNrMitteilungKontaktTelefonAbfragen) {
        this.textNrMitteilungKontaktTelefonAbfragen = textNrMitteilungKontaktTelefonAbfragen;
    }

    public String getTextNrMailKontaktdatenTelefon() {
        return textNrMailKontaktdatenTelefon;
    }

    public void setTextNrMailKontaktdatenTelefon(int textNrMailKontaktdatenTelefon) {
        this.textNrMailKontaktdatenTelefon = Integer.toString(textNrMailKontaktdatenTelefon);
    }

    public boolean[] getInhaltsHinweiseAktiv() {
        return inhaltsHinweiseAktiv;
    }

    public void setInhaltsHinweiseAktiv(boolean[] inhaltsHinweiseAktiv) {
        this.inhaltsHinweiseAktiv = inhaltsHinweiseAktiv;
    }

    public String getKontaktdatenTelefonEingabe() {
        return kontaktdatenTelefonEingabe;
    }

    public void setKontaktdatenTelefonEingabe(String kontaktdatenTelefonEingabe) {
        this.kontaktdatenTelefonEingabe = kontaktdatenTelefonEingabe;
    }

    public boolean isInhaltsHinweiseVorhandenundAktiv() {
        return inhaltsHinweiseVorhandenundAktiv;
    }

    public void setInhaltsHinweiseVorhandenundAktiv(boolean inhaltsHinweiseVorhandenundAktiv) {
        this.inhaltsHinweiseVorhandenundAktiv = inhaltsHinweiseVorhandenundAktiv;
    }

    public String getTextNrVorInhaltsHinweise() {
        return textNrVorInhaltsHinweise;
    }

    public void setTextNrVorInhaltsHinweise(String textNrVorInhaltsHinweise) {
        this.textNrVorInhaltsHinweise = textNrVorInhaltsHinweise;
    }

    public List<EhInhaltsHinweise> getInhaltsHinweiseListe() {
        return inhaltsHinweiseListe;
    }

    public void setInhaltsHinweiseListe(List<EhInhaltsHinweise> inhaltsHinweiseListe) {
        this.inhaltsHinweiseListe = inhaltsHinweiseListe;
    }

    public String getKontaktDatenTelefonEingabeFragen() {
        return kontaktDatenTelefonEingabeFragen;
    }

    public void setKontaktDatenTelefonEingabeFragen(String kontaktDatenTelefonEingabeFragen) {
        this.kontaktDatenTelefonEingabeFragen = kontaktDatenTelefonEingabeFragen;
    }

    public String getKontaktDatenTelefonEingabeWortmeldungen() {
        return kontaktDatenTelefonEingabeWortmeldungen;
    }

    public void setKontaktDatenTelefonEingabeWortmeldungen(String kontaktDatenTelefonEingabeWortmeldungen) {
        this.kontaktDatenTelefonEingabeWortmeldungen = kontaktDatenTelefonEingabeWortmeldungen;
    }

    public String getKontaktDatenTelefonEingabeWidersprueche() {
        return kontaktDatenTelefonEingabeWidersprueche;
    }

    public void setKontaktDatenTelefonEingabeWidersprueche(String kontaktDatenTelefonEingabeWidersprueche) {
        this.kontaktDatenTelefonEingabeWidersprueche = kontaktDatenTelefonEingabeWidersprueche;
    }

    public String getKontaktDatenTelefonEingabeAntraege() {
        return kontaktDatenTelefonEingabeAntraege;
    }

    public void setKontaktDatenTelefonEingabeAntraege(String kontaktDatenTelefonEingabeAntraege) {
        this.kontaktDatenTelefonEingabeAntraege = kontaktDatenTelefonEingabeAntraege;
    }

    public String getKontaktDatenTelefonEingabeSonstigeMitteilungen() {
        return kontaktDatenTelefonEingabeSonstigeMitteilungen;
    }

    public void setKontaktDatenTelefonEingabeSonstigeMitteilungen(String kontaktDatenTelefonEingabeSonstigeMitteilungen) {
        this.kontaktDatenTelefonEingabeSonstigeMitteilungen = kontaktDatenTelefonEingabeSonstigeMitteilungen;
    }

    public String getKontaktDatenTelefonEingabeBotschaftenEinreichen() {
        return kontaktDatenTelefonEingabeBotschaftenEinreichen;
    }

    public void setKontaktDatenTelefonEingabeBotschaftenEinreichen(String kontaktDatenTelefonEingabeBotschaftenEinreichen) {
        this.kontaktDatenTelefonEingabeBotschaftenEinreichen = kontaktDatenTelefonEingabeBotschaftenEinreichen;
    }

    public String getKontaktDatenTelefonEingabeRueckfragen() {
        return kontaktDatenTelefonEingabeRueckfragen;
    }

    public void setKontaktDatenTelefonEingabeRueckfragen(String kontaktDatenTelefonEingabeRueckfragen) {
        this.kontaktDatenTelefonEingabeRueckfragen = kontaktDatenTelefonEingabeRueckfragen;
    }

    public int getKonferenzTestDurchgefuehrt() {
        return konferenzTestDurchgefuehrt;
    }

    public void setKonferenzTestDurchgefuehrt(int konferenzTestDurchgefuehrt) {
        this.konferenzTestDurchgefuehrt = konferenzTestDurchgefuehrt;
    }

    public int getKonferenzTestAblauf() {
        return konferenzTestAblauf;
    }

    public void setKonferenzTestAblauf(int konferenzTestAblauf) {
        this.konferenzTestAblauf = konferenzTestAblauf;
    }

    public int getKonferenzSprechen() {
        return konferenzSprechen;
    }

    public void setKonferenzSprechen(int konferenzSprechen) {
        this.konferenzSprechen = konferenzSprechen;
    }
    public String getKonferenzUrl() {
        return konferenzUrl;
    }
    public void setKonferenzUrl(String konferenzUrl) {
        this.konferenzUrl = konferenzUrl;
    }
    public int getKonferenzTestDurchgefuehrtAlt() {
        return konferenzTestDurchgefuehrtAlt;
    }
    public void setKonferenzTestDurchgefuehrtAlt(int konferenzTestDurchgefuehrtAlt) {
        this.konferenzTestDurchgefuehrtAlt = konferenzTestDurchgefuehrtAlt;
    }
    public int getKonferenzTestAblaufAlt() {
        return konferenzTestAblaufAlt;
    }
    public void setKonferenzTestAblaufAlt(int konferenzTestAblaufAlt) {
        this.konferenzTestAblaufAlt = konferenzTestAblaufAlt;
    }
    public int getKonferenzSprechenAlt() {
        return konferenzSprechenAlt;
    }
    public void setKonferenzSprechenAlt(int konferenzSprechenAlt) {
        this.konferenzSprechenAlt = konferenzSprechenAlt;
    }
    public int getKonferenzTestRaum() {
        return konferenzTestRaum;
    }
    public void setKonferenzTestRaum(int konferenzTestRaum) {
        this.konferenzTestRaum = konferenzTestRaum;
    }
    public int ggetKonferenzSprechenRaum() {
        return konferenzSprechenRaum;
    }
    public void setKonferenzSprechenRaum(int konferenzSprechenRaum) {
        this.konferenzSprechenRaum = konferenzSprechenRaum;
    }
    public boolean isInhaltsHinweiseAnbieten() {
        return inhaltsHinweiseAnbieten;
    }
    public void setInhaltsHinweiseAnbieten(boolean inhaltsHinweiseAnbieten) {
        this.inhaltsHinweiseAnbieten = inhaltsHinweiseAnbieten;
    }
    public boolean isConnectToWebSocket() {
        return connectToWebSocket;
    }
    public void setConnectToWebSocket(boolean connectToWebSocket) {
        this.connectToWebSocket = connectToWebSocket;
    }
    public int getAnzahlMitteilungenOffenPfandbrief() {
        return anzahlMitteilungenOffenPfandbrief;
    }
    public void setAnzahlMitteilungenOffenPfandbrief(int anzahlMitteilungenOffenPfandbrief) {
        this.anzahlMitteilungenOffenPfandbrief = anzahlMitteilungenOffenPfandbrief;
    }

}
