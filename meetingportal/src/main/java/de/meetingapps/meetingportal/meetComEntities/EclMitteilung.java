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
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import de.meetingapps.meetingportal.meetComEh.EhInhaltsHinweise;
import de.meetingapps.meetingportal.meetComEh.EhJsfButton;
import jakarta.servlet.http.Part;

public class EclMitteilung implements Serializable {
    private static final long serialVersionUID = -4191708820754739778L;


    /**Eindeutig Ident dieser Frage*/
    public int mitteilungIdent = 0;

    /**Zusammen mit mitteilungIdent Primary Key.
     * Versionsnummer. Für Änderungsverfolgung. Derzeit nur bei Botschaften verwendet, sonst immer 0
     * 0 = durch Aktionoär hochgeladene Version
     */
    public int version=0;

    /**
     * Versionsnummer - manuell nicht verändern! Ausschließlich durch Verwendung
     * in DbMeldungen
     */
    public long db_version;

    /******Mitteilungssteller***************/
    /**Ident der Login-Kennung, mit der diese Mitteilung erstellt wurde*/
    public int loginIdent = 0;

    /**Anzahl der Aktionäre (bei Namensaktien) bzw. Eintrittskarten (bei Inhaberaktien), die
     * der Mitteilungssteller zum Zeitpunkt der Mitteilung vertritt
     *
     * Wird aktuell nicht gefüllt
     */
    public int anzahlAktionaereZumZeitpunktDerMitteilung=0;

    /**Anzahl der Aktien, die der Mitteilungssteller zum Zeitpunkt der Mitteilung vertritt*/
    public long anzahlAktienZumZeitpunktderMitteilung=0L;

    /**In Datenbank in einer Integer-Zahl; Bits 1,2,4,8,16; [gattung]*/
    public int gattungen[]= {0, 0, 0, 0, 0, 0};

    /**Kennung des Mitteilungssteller
     * LEN=20
     */
    public String identString="";

    /**Mitteilungssteller (aus Kennung)
     * LEN=200
     * */
    public String nameVornameOrtKennung="";


    /**Mitteilungssteller (manuelle Eingabe).
     * Falls eine manuelle Eingabe des Namens für diese Mitteilung erfolgt ist, dann
     * diese Eingabe. Sonst identisch mit nameVornameOrtKennung.
     * LEN=200.
     *
     * Bei Wortmeldungen muß dieses Feld wg. Rednerliste bei der Pflege der Rednerliste zwingend gefüllt werden.
     * */
    public String nameVornameOrt="";

    /** LEN=100*/
    public String kontaktDaten="";

    /** LEN=100*/
    public String kontaktDatenTelefon="";

    /**********Mitteilungsartinhalt********************/

    /**=1 => Hinweis wurde bestätigt*/
    public int hinweisWurdeBestaetigt=0;

    /**Nur bei entsprechender Parameterstellung gefüllt*/
    public int[] mitteilungZuTop = new int[200];

    /**Bei der Abgabe einer Mitteilung können bis zu 10 Check-Boxes aktiviert werden. Die Beschriftung dieser Check-Boxes ist frei wählbar.
     * Damit ist es möglich, vom Aktionär Hinweise zum Inhalt seiner Mitteilung markieren zu lassen (z.B. "Enhält Antrag").
     * In lesbarer Form (einschließlich Beschriftungen) in inhaltsHinweiseListe enthalten.
     */
    public boolean[] inhaltsHinweis=new boolean[10];

    /**LEN=100
     */
    public String mitteilungKurztext = "";

    /**LEN in Datenbank=21.000;
     * Zulässige Länge in Browser = 10.000
     */
    public String mitteilungLangtext = "";

    /**LEN=19*/
    public String zeitpunktDerMitteilung = "";

    /**Falls Mitteilung zurückgezogen wurde
     * LEN=19*/
    public String zeitpunktDesRueckzugs = "";

    public int drucklaufNr = 0;

    /** für Wortmeldung: siehe KonstMitteilungStatus.
     * Für alle Mitteilungen (so Parametermäßig zugelassen):
     * KonstMitteilungStatus.zurueckgezogen
     * */
    public int status = 0;

    /**Verwendung nur bei Wortmeldungen*/
    public int raumNr = 0;

    /**z.B. in Rednerliste*/
    public int lfdNrInListe = 0;


    /******************************Inhalte bei Botschaften*********************************/
    /**1=Textbotschaft (in Textfeld), 2=Videobotschaft, 4=Textbotschaft - Datei (PDF ...)*/
    public int botschaftsart=0;
    public boolean liefereLangTextVorhanden() {
        return ((botschaftsart & 1)==1);
    };
    public boolean liefereVideodateiVorhanden() {
        return ((botschaftsart & 2)==2);
    };
    public boolean liefereTextdateiVorhanden() {
        return ((botschaftsart & 4)==4);
    };
    public boolean liefereDateiVorhanden() {
        return (liefereVideodateiVorhanden() || liefereTextdateiVorhanden());
    };


    /**Dateiname, LEN=200*/
    public String dateiname="";

    /**Freigegeben, wenn Wert>0
     * 1 = Freigegeben durch Aktionär       - im Versions-Satz
     * 2 = Freigegeben durch Gesellschaft   - im Versions-Satz
     * 4 = Hochgeladen (Stream) / Bereitgestellt (Textdateien)     - im Versionssatz
     * 8 = Öffentlicher Link erstellt       - im Headersatz*/
    public int freigegeben=0;

    public boolean liefereFreigegebenDurchAktionaer() {
        return (freigegeben & 1) == 1;
    }
    public boolean liefereFreigegebenDurchGesellschaft() {
        return (freigegeben & 2) == 2;
    }
    public boolean liefereFreigegebenHochgeladen() {
        return (freigegeben & 4) == 4;
    }
    public boolean liefereFreigegebenOeffentlicherLinkErstellt() {
        return (freigegeben & 8) == 8;
    }


    public void setzeFreigegebenNichtOeffentlich() {
        freigegeben=(7 & freigegeben);
    }
    public void setzeFreigegebenNichtHochgeladen() {
        freigegeben=(11 & freigegeben);
    }

    /**
     * Handhabung von Dateinamen beim Upload/Download von Botschaften:
     * > Beim Hochladen durch den Aktionär wird ein EclMitteilung für die Botschaft erstellt und ein eindeutiger Key als Dateiname vergeben.
     * Der eigentliche Dateiname wird in dateiname komplett eingetragen (nur zu Dokumentationszwecken bzw. zur Anzeige gegenüber dem Aktionär).
     *
     *  Der Zusatz der datei wird in dateinameZusatz eingetragen. Dieser kann sich über die Versionen ändern, deshalb die
     *  etwas komplizierte Handhabung über version.
     *
     * Eindeutiger Key für Dateiname (siehe auch EclPortalUnterlage
     * LEN=20*/
    public String internerDateiname="";
    /**LEN=20*/
    public String internerDateizusatz="";

    /**Bei Videobotschaften: während Umformung (unmittelbar nach dem Hochladen) bzw. bis zum Abschluß derselben auf 1 =>
     * für sonstige Bearbeitung gerade gesperrt
     *
     * Siehe KonstMitteilungStatus.verarbeitung*:
     * 0 => noch nicht durchgeführt
     * 1 => Shrinking läuft gerade
     * 2 => erledigt
     * 3 => Hochladen gerade in Arbeit
     */
    public int interneVerarbeitungLaufend=0;

    /**Eindeutiger Verweis auf den entsprechenden Satz zur Anzeige in EclPortalUnterlagen*/
    public int verweisAufUnterlagenident=0;

    /**Verwendung bei Wortmeldetisch - einzugeben durch berechtigte Views
     * LEN=500*/
    public String kommentarIntern="";
    /**LEN=500*/
    public String kommentarVersammlungsleiter="";

    /******************Nicht in Datenbank************************/
    /**Gemäß KonstPortalFunktionen*/
    public int artDerMitteilung=0;

    public String loginKennung = "";
    public String loginKennungAnzeige = "";
    public String nameWortmelder = "";
    public String ortWortmelder = "";
    public String zeitpunktFuerAnzeige = "";
    public String zuTOPListe="";

    public boolean inhaltsHinweiseVorhandenundAktiv=false;
    public List<EhInhaltsHinweise> inhaltsHinweiseListe=null;

    /**Buttons für Versammlungsleiter-View*/
    public List<EhJsfButton> buttonsVersammlungsleiter=null;

    public ArrayList<LinkedList<EhJsfButton>> buttonKoordination = null;

//    @Deprecated
//    /**Buttons für Test-Liste*/
//    public List<EhJsfButton> buttonsInRednerListeTest=null;
//
//    @Deprecated
//    /**Buttons für Bearbeitungs-Liste Koordination*/
//    public List<EhJsfButton> buttonsInRednerListeBearbeitungKoordination=null;
//
//    @Deprecated
//    /**Buttons für Telefonie-Liste*/
//    public List<EhJsfButton> buttonsInRednerListeTelefonie=null;
//
//    @Deprecated
//    /**Buttons für Telefonie-Liste*/
//    public List<EhJsfButton> buttonsInGesamtListe=null;


    /**Für Verwaltung der hochgeladenen Botschaften*/
    public List<EclMitteilung> weitereVersionen=new LinkedList<EclMitteilung>();
    public EclPortalUnterlagen zugeordnetePortalUnterlage=null;

    /**true => Langtext wird in uBotschaftenVerarbeiten angezeigt*/
    public boolean langTextAnzeigen=false;

    /**Datei-Anhang - für Hochladen in uLogin*/
    public Part dateiHochladen = null;

    public EclMitteilung() {
        for (int i = 0; i < 200; i++) {
            mitteilungZuTop[i] = 0;
        }
        for (int i=0;i<10;i++) {
            inhaltsHinweis[i]=false;
        }

    }

    public boolean zurueckziehenIstMoeglich=false;
    public boolean zurueckziehenMoeglich() {
        return zurueckziehenIstMoeglich;
    }

    public int statusText=0;
    public String liefereStatusText() {
        return Integer.toString(statusText);
    }

    /**Für Wortmeldeablauf - nur gefüllt, wenn für den WortmeldeView die Präsenz-Anzeige aktiviert ist*/
    public int praesent=0;
    
    /*****************************Standard getter und setter***************************************************************/


    public int getMitteilungIdent() {
        return mitteilungIdent;
    }

    public void setMitteilungIdent(int mitteilungIdent) {
        this.mitteilungIdent = mitteilungIdent;
    }

    public long getDb_version() {
        return db_version;
    }

    public void setDb_version(long db_version) {
        this.db_version = db_version;
    }

    public int getLoginIdent() {
        return loginIdent;
    }

    public void setLoginIdent(int loginIdent) {
        this.loginIdent = loginIdent;
    }

    public int getAnzahlAktionaereZumZeitpunktDerMitteilung() {
        return anzahlAktionaereZumZeitpunktDerMitteilung;
    }

    public void setAnzahlAktionaereZumZeitpunktDerMitteilung(int anzahlAktionaereZumZeitpunktDerMitteilung) {
        this.anzahlAktionaereZumZeitpunktDerMitteilung = anzahlAktionaereZumZeitpunktDerMitteilung;
    }

    public long getAnzahlAktienZumZeitpunktderMitteilung() {
        return anzahlAktienZumZeitpunktderMitteilung;
    }

    public void setAnzahlAktienZumZeitpunktderMitteilung(long anzahlAktienZumZeitpunktderMitteilung) {
        this.anzahlAktienZumZeitpunktderMitteilung = anzahlAktienZumZeitpunktderMitteilung;
    }

    public String getIdentString() {
        return identString;
    }

    public void setIdentString(String identString) {
        this.identString = identString;
    }

    public String getNameVornameOrtKennung() {
        return nameVornameOrtKennung;
    }

    public void setNameVornameOrtKennung(String nameVornameOrtKennung) {
        this.nameVornameOrtKennung = nameVornameOrtKennung;
    }

    public String getNameVornameOrt() {
        return nameVornameOrt;
    }

    public void setNameVornameOrt(String nameVornameOrt) {
        this.nameVornameOrt = nameVornameOrt;
    }

    public String getKontaktDaten() {
        return kontaktDaten;
    }

    public void setKontaktDaten(String kontaktDaten) {
        this.kontaktDaten = kontaktDaten;
    }

    public int[] getMitteilungZuTop() {
        return mitteilungZuTop;
    }

    public void setMitteilungZuTop(int[] mitteilungZuTop) {
        this.mitteilungZuTop = mitteilungZuTop;
    }

    public String getMitteilungKurztext() {
        return mitteilungKurztext;
    }

    public void setMitteilungKurztext(String mitteilungKurztext) {
        this.mitteilungKurztext = mitteilungKurztext;
    }

    public String getMitteilungLangtext() {
        return mitteilungLangtext;
    }

    public void setMitteilungLangtext(String mitteilungLangtext) {
        this.mitteilungLangtext = mitteilungLangtext;
    }

    public String getZeitpunktDerMitteilung() {
        return zeitpunktDerMitteilung;
    }

    public void setZeitpunktDerMitteilung(String zeitpunktDerMitteilung) {
        this.zeitpunktDerMitteilung = zeitpunktDerMitteilung;
    }

    public String getZeitpunktDesRueckzugs() {
        return zeitpunktDesRueckzugs;
    }

    public void setZeitpunktDesRueckzugs(String zeitpunktDesRueckzugs) {
        this.zeitpunktDesRueckzugs = zeitpunktDesRueckzugs;
    }

    public int getDrucklaufNr() {
        return drucklaufNr;
    }

    public void setDrucklaufNr(int drucklaufNr) {
        this.drucklaufNr = drucklaufNr;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getLfdNrInListe() {
        return lfdNrInListe;
    }

    public void setLfdNrInListe(int lfdNrInListe) {
        this.lfdNrInListe = lfdNrInListe;
    }

    public int getArtDerMitteilung() {
        return artDerMitteilung;
    }

    public void setArtDerMitteilung(int artDerMitteilung) {
        this.artDerMitteilung = artDerMitteilung;
    }

    public String getLoginKennung() {
        return loginKennung;
    }

    public void setLoginKennung(String loginKennung) {
        this.loginKennung = loginKennung;
    }

    public String getLoginKennungAnzeige() {
        return loginKennungAnzeige;
    }

    public void setLoginKennungAnzeige(String loginKennungAnzeige) {
        this.loginKennungAnzeige = loginKennungAnzeige;
    }

    public String getNameWortmelder() {
        return nameWortmelder;
    }

    public void setNameWortmelder(String nameWortmelder) {
        this.nameWortmelder = nameWortmelder;
    }

    public String getOrtWortmelder() {
        return ortWortmelder;
    }

    public void setOrtWortmelder(String ortWortmelder) {
        this.ortWortmelder = ortWortmelder;
    }

    public String getZeitpunktFuerAnzeige() {
        return zeitpunktFuerAnzeige;
    }

    public void setZeitpunktFuerAnzeige(String zeitpunktFuerAnzeige) {
        this.zeitpunktFuerAnzeige = zeitpunktFuerAnzeige;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getHinweisWurdeBestaetigt() {
        return hinweisWurdeBestaetigt;
    }

    public void setHinweisWurdeBestaetigt(int hinweisWurdeBestaetigt) {
        this.hinweisWurdeBestaetigt = hinweisWurdeBestaetigt;
    }

    public int getBotschaftsart() {
        return botschaftsart;
    }

    public void setBotschaftsart(int botschaftsart) {
        this.botschaftsart = botschaftsart;
    }

    public String getDateiname() {
        return dateiname;
    }

    public void setDateiname(String dateiname) {
        this.dateiname = dateiname;
    }

    public int getFreigegeben() {
        return freigegeben;
    }

    public void setFreigegeben(int freigegeben) {
        this.freigegeben = freigegeben;
    }

    public String getInternerDateiname() {
        return internerDateiname;
    }

    public void setInternerDateiname(String internerDateiname) {
        this.internerDateiname = internerDateiname;
    }

    public String getInternerDateizusatz() {
        return internerDateizusatz;
    }

    public void setInternerDateizusatz(String internerDateizusatz) {
        this.internerDateizusatz = internerDateizusatz;
    }

    public int getVerweisAufUnterlagenident() {
        return verweisAufUnterlagenident;
    }

    public void setVerweisAufUnterlagenident(int verweisAufUnterlagenident) {
        this.verweisAufUnterlagenident = verweisAufUnterlagenident;
    }

    public String getZuTOPListe() {
        return zuTOPListe;
    }

    public void setZuTOPListe(String zuTOPListe) {
        this.zuTOPListe = zuTOPListe;
    }

    public int[] getGattungen() {
        return gattungen;
    }

    public void setGattungen(int[] gattungen) {
        this.gattungen = gattungen;
    }

    public int getInterneVerarbeitungLaufend() {
        return interneVerarbeitungLaufend;
    }

    public void setInterneVerarbeitungLaufend(int interneVerarbeitungLaufend) {
        this.interneVerarbeitungLaufend = interneVerarbeitungLaufend;
    }

    public List<EclMitteilung> getWeitereVersionen() {
        return weitereVersionen;
    }

    public void setWeitereVersionen(List<EclMitteilung> weitereVersionen) {
        this.weitereVersionen = weitereVersionen;
    }
    public EclPortalUnterlagen getZugeordnetePortalUnterlage() {
        return zugeordnetePortalUnterlage;
    }
    public void setZugeordnetePortalUnterlage(EclPortalUnterlagen zugeordnetePortalUnterlage) {
        this.zugeordnetePortalUnterlage = zugeordnetePortalUnterlage;
    }
    public boolean isLangTextAnzeigen() {
        return langTextAnzeigen;
    }
    public void setLangTextAnzeigen(boolean langTextAnzeigen) {
        this.langTextAnzeigen = langTextAnzeigen;
    }
    public Part getDateiHochladen() {
        return dateiHochladen;
    }
    public void setDateiHochladen(Part dateiHochladen) {
        this.dateiHochladen = dateiHochladen;
    }
    public String getKontaktDatenTelefon() {
        return kontaktDatenTelefon;
    }
    public void setKontaktDatenTelefon(String kontaktDatenTelefon) {
        this.kontaktDatenTelefon = kontaktDatenTelefon;
    }
    public boolean[] getInhaltsHinweis() {
        return inhaltsHinweis;
    }
    public void setInhaltsHinweis(boolean[] inhaltsHinweis) {
        this.inhaltsHinweis = inhaltsHinweis;
    }
    public List<EhInhaltsHinweise> getInhaltsHinweiseListe() {
        return inhaltsHinweiseListe;
    }
    public void setInhaltsHinweiseListe(List<EhInhaltsHinweise> inhaltsHinweiseListe) {
        this.inhaltsHinweiseListe = inhaltsHinweiseListe;
    }
    public boolean isInhaltsHinweiseVorhandenundAktiv() {
        return inhaltsHinweiseVorhandenundAktiv;
    }
    public void setInhaltsHinweiseVorhandenundAktiv(boolean inhaltsHinweiseVorhandenundAktiv) {
        this.inhaltsHinweiseVorhandenundAktiv = inhaltsHinweiseVorhandenundAktiv;
    }
    public List<EhJsfButton> getButtonsVersammlungsleiter() {
        return buttonsVersammlungsleiter;
    }
    public void setButtonsVersammlungsleiter(List<EhJsfButton> buttonsVersammlungsleiter) {
        this.buttonsVersammlungsleiter = buttonsVersammlungsleiter;
    }
    public int getRaumNr() {
        return raumNr;
    }
    public void setRaumNr(int raumNr) {
        this.raumNr = raumNr;
    }
    public String getKommentarIntern() {
        return kommentarIntern;
    }
    public void setKommentarIntern(String kommentarIntern) {
        this.kommentarIntern = kommentarIntern;
    }
    public String getKommentarVersammlungsleiter() {
        return kommentarVersammlungsleiter;
    }
    public void setKommentarVersammlungsleiter(String kommentarVersammlungsleiter) {
        this.kommentarVersammlungsleiter = kommentarVersammlungsleiter;
    }
    public ArrayList<LinkedList<EhJsfButton>> getButtonKoordination() {
        return buttonKoordination;
    }
    public void setButtonKoordination(ArrayList<LinkedList<EhJsfButton>> buttonKoordination) {
        this.buttonKoordination = buttonKoordination;
    }
    public int getPraesent() {
        return praesent;
    }
    public void setPraesent(int praesent) {
        this.praesent = praesent;
    }

}
