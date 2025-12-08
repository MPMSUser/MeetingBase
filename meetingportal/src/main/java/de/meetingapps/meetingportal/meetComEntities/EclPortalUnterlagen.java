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

/**Klasse für ParamPortal: "sonstige" Unterlagen (also nicht Teilnehmerverzeichnis und Abstimmungsergebnisse)
 * für die virtuellen HV.
 */
public class EclPortalUnterlagen implements Serializable {
    private static final long serialVersionUID = -5506644304488443343L;

    /*
     * Noch anderweitig zu tun:
     * > Datenbank für tbl_portalunterlagen
     * > Je User: Berechtigung für Preview-Unterlagen
     * > Analog Mitteilungen: Einreichung (VIdeo- oder Textbotschaft)
     * > Pflegeprogramm
     */
    
    /**Ident ist primary Key. Wird beim Zurückschreiben in table einfach laufend vergeben.
     * Bereits vergebene bleiben allerdings erhalten*/
    public int ident=0;

    /**Anzeige im jeweiligen Bereich, wenn reihenfolge<bereich>!=0*/
    public int reihenfolgeLoginOben=0;
    public int reihenfolgeLoginUnten=0;
    public int reihenfolgeExterneSeite=0;
    public int reihenfolgeUnterlagen=0;
    public int reihenfolgeBotschaften=0;
    
    /**Für Mitgliederportal*/
    public int unterlagenbereichMenueNr=0;
    /**Für Mitgliederportal*/
    public int reihenfolgeUnterlagenbereich=0;

   
    /**z.B. 0=Button; 1=Überschrift; 2=Text, siehe KonstPortalUnterlagen.ART_
     * 
     * Spezialfall: 5=Verweis:
     * Diese Art gibt es in der Datenbank und beim Einlesen.
     * In diesem Fall steht in der Datenbank im Feld dateiname eine Zahl, die auf ident eines anderen Unterlagen-Satzes verweist.
     * Beim Aufbereiten in eine Anzeigeliste werden die Felder umgesetzt:
     * > ursprungIstEinVerweis wird auf true gesetzt (sonst false)
     * > verweisAufIdent wird mit der Nummer des Ursprung-Satzes gefüllt
     * > die Felder 
     *      art, artStyle, dateiname, dateiMehrsprachigVorhanden, beizeichnungDE und bezeichnung EN
     *      des Objekts werden mit den Feldern des Objekts, auf das Verwiesen wird, gefüllt.*/
    public int art = 0;
    
    /**abhängig von art - ggf. eine Style-Info dazu, siehe KonstPortalUnterlagen.ARTSTYLE_BUTTON_ALS_BUTTON
     * 
     * Wird immer dann verwendet, wenn ein Inhalt dargestellt wird, nicht jedoch bei Texten
     * */
    public int artStyle=0;

    /**Falls 1, dann wird der Punkt nur bei Benutzern mit der Preview-Berechtigung angezeigt.
     * 
     * Ist problematisch bei login und externe-Seite, da hier noch kein User angemeldet. In diesem Fall
     * ist die Anzeige abhhängig vom Test-Parameter - problematisch, da "erratbar". Also nur für letzten
     * Check kurz vor Freischaltung sinnvoll. Möglicherweise zukünftige Lösung mit separatem Passwort im Link ...
     */
    public int previewLogin=0;
    public int previewExterneSeite=0;
    public int previewIntern=0;
    
    /**Preview für Mitgliederportal*/
    public int previewUnterlagenbereich=0;
    
    /**LEN=100, ohne Zusatz (.*), wird mit DE / EN ergänzt, wenn zweisprachige Datei, sonst nicht.
     * 
     * Bei Text-Botschaften: ist der eindeutige Key, der beim Hochladen vergeben wird.
     * Bei Video-Botschaften: ist der Key, der zur Referenzierung beim Videostreamer verwendet wird.
     * Bei Link-Button: absoluter Link
     * Bei Verweis: Nr, die auf andere Unterlagen-Ident zeigt.
     */
    public String dateiname = "";
    
    /**Je nach Art: bei Unterlage wird dieser automatisch auf PDF gesetzt (nichts anderes zulässig); ansonsten
     * die Dateiart wie vorhanden (insbesondere bei Video).
     * LEN=10
     */
//    public String dateinameZusatz="";

    /**Wenn =1, dann steht Datei zweisprachig zur Verfügung und wird mit DE / EN ersetzt. Sonst nur 1 Sprache, ohne DE/EN, nur eine Datei*/
    public int dateiMehrsprachigVorhanden=0;
    
    /**Bei vom Benutzer hochgeladenen Dateien: eindeutiges Kürzel der Quell-Datei (zur Verknüpfung auf die eingereichten Unterlagen
     * LEN=20 (eindeutiger Key=15 Stellen)*/
//    public String dateiKuerzel="";
    
    /**LEN maximal 5000 (in DB: 10.000)*/
    public String bezeichnungDE = "";
    public String bezeichnungEN = "";

    /*++++++++Berechtigt sind++++++*/
    /**werden nur gewertet, wenn aktiv=6 oder 7 ist (also sprich: nicht einer Gruppe zugeordnet sind)*/
    public boolean berechtigtGast1 = false; //2
    public boolean berechtigtGast2 = false; //4
    public boolean berechtigtGast3 = false; //8
    public boolean berechtigtGast4 = false; //16
    public boolean berechtigtGast5 = false; //32
    public boolean berechtigtGast6 = false; //64
    public boolean berechtigtGast7 = false; //128
    public boolean berechtigtGast8 = false; //256
    public boolean berechtigtGast9 = false; //512
    public boolean berechtigtGast10 = false; //1024

    public boolean berechtigtGastOnlineTeilnahmer1 = false; //2048
    public boolean berechtigtGastOnlineTeilnahmer2 = false; //4096
    public boolean berechtigtGastOnlineTeilnahmer3 = false; //8192
    public boolean berechtigtGastOnlineTeilnahmer4 = false; //16384
    public boolean berechtigtGastOnlineTeilnahmer5 = false; //32768
    public boolean berechtigtGastOnlineTeilnahmer6 = false; //65536
    public boolean berechtigtGastOnlineTeilnahmer7 = false; //131072
    public boolean berechtigtGastOnlineTeilnahmer8 = false; //262144
    public boolean berechtigtGastOnlineTeilnahmer9 = false; //524288
    public boolean berechtigtGastOnlineTeilnahmer10 = false; //1048576

    public boolean berechtigtAktionaer = false; //2097152
    public boolean berechtigtAngemeldeterAktionaer = false; //4194304
    public boolean berechtigtOnlineTeilnahmeAktionaer = false; //8388608

    /*++++++++++Aktiv++++++++++++++*/
    /** 
     * 1  .. 5 aktiv gemäß Unterlagengruppe 1 .. 5 (damit auch Berechtigung überprüft!)
     * 6 aktiv Vorrangig vor Phase/Termin
     * 7 inaktiv vorrangig vor Phase/Termin
     */
    public int aktiv = 7;

    public boolean aktivVorrangig() {
        if (aktiv == 6) {
            return true;
        }
        return false;
    }

    public boolean inaktivVorrangig() {
        if (aktiv == 7) {
            return true;
        }
        return false;
    }

    /**0 => individuell, sonst Gruppennummer*/
    public int aktivLtGruppe() {
        if (aktiv >= 1 && aktiv <= 5) {
            return aktiv;
        }
        return 0;
    }

    
    /*********************Nicht in Datenbank**************************************/
    /**Siehe art==5*/
    public boolean ursprungIstEinVerweis=false;
    
    /**Nur gefüllt wenn ursprungIstEinVerweis==false*/
    public int verweisAufIdent=0;
    
    
    /*********************Standard getter und setter*****************************************************/
    public int getIdent() {
        return ident;
    }

    public void setIdent(int ident) {
        this.ident = ident;
    }

    public int getReihenfolgeLoginOben() {
        return reihenfolgeLoginOben;
    }

    public void setReihenfolgeLoginOben(int reihenfolgeLoginOben) {
        this.reihenfolgeLoginOben = reihenfolgeLoginOben;
    }

    public int getReihenfolgeLoginUnten() {
        return reihenfolgeLoginUnten;
    }

    public void setReihenfolgeLoginUnten(int reihenfolgeLoginUnten) {
        this.reihenfolgeLoginUnten = reihenfolgeLoginUnten;
    }

    public int getReihenfolgeExterneSeite() {
        return reihenfolgeExterneSeite;
    }

    public void setReihenfolgeExterneSeite(int reihenfolgeExterneSeite) {
        this.reihenfolgeExterneSeite = reihenfolgeExterneSeite;
    }

    public int getReihenfolgeUnterlagen() {
        return reihenfolgeUnterlagen;
    }

    public void setReihenfolgeUnterlagen(int reihenfolgeUnterlagen) {
        this.reihenfolgeUnterlagen = reihenfolgeUnterlagen;
    }

    public int getReihenfolgeBotschaften() {
        return reihenfolgeBotschaften;
    }

    public void setReihenfolgeBotschaften(int reihenfolgeBotschaften) {
        this.reihenfolgeBotschaften = reihenfolgeBotschaften;
    }

    public int getArt() {
        return art;
    }

    public void setArt(int art) {
        this.art = art;
    }

    public int getArtStyle() {
        return artStyle;
    }

    public void setArtStyle(int artStyle) {
        this.artStyle = artStyle;
    }

    public int getPreviewLogin() {
        return previewLogin;
    }

    public void setPreviewLogin(int previewLogin) {
        this.previewLogin = previewLogin;
    }

    public int getPreviewExterneSeite() {
        return previewExterneSeite;
    }

    public void setPreviewExterneSeite(int previewExterneSeite) {
        this.previewExterneSeite = previewExterneSeite;
    }

    public int getPreviewIntern() {
        return previewIntern;
    }

    public void setPreviewIntern(int previewIntern) {
        this.previewIntern = previewIntern;
    }

    public String getDateiname() {
        return dateiname;
    }

    public void setDateiname(String dateiname) {
        this.dateiname = dateiname;
    }

    public int getDateiMehrsprachigVorhanden() {
        return dateiMehrsprachigVorhanden;
    }

    public void setDateiMehrsprachigVorhanden(int dateiMehrsprachigVorhanden) {
        this.dateiMehrsprachigVorhanden = dateiMehrsprachigVorhanden;
    }

    public String getBezeichnungDE() {
        return bezeichnungDE;
    }

    public void setBezeichnungDE(String bezeichnungDE) {
        this.bezeichnungDE = bezeichnungDE;
    }

    public String getBezeichnungEN() {
        return bezeichnungEN;
    }

    public void setBezeichnungEN(String bezeichnungEN) {
        this.bezeichnungEN = bezeichnungEN;
    }

    public boolean isBerechtigtGast1() {
        return berechtigtGast1;
    }

    public void setBerechtigtGast1(boolean berechtigtGast1) {
        this.berechtigtGast1 = berechtigtGast1;
    }

    public boolean isBerechtigtGast2() {
        return berechtigtGast2;
    }

    public void setBerechtigtGast2(boolean berechtigtGast2) {
        this.berechtigtGast2 = berechtigtGast2;
    }

    public boolean isBerechtigtGast3() {
        return berechtigtGast3;
    }

    public void setBerechtigtGast3(boolean berechtigtGast3) {
        this.berechtigtGast3 = berechtigtGast3;
    }

    public boolean isBerechtigtGast4() {
        return berechtigtGast4;
    }

    public void setBerechtigtGast4(boolean berechtigtGast4) {
        this.berechtigtGast4 = berechtigtGast4;
    }

    public boolean isBerechtigtGast5() {
        return berechtigtGast5;
    }

    public void setBerechtigtGast5(boolean berechtigtGast5) {
        this.berechtigtGast5 = berechtigtGast5;
    }

    public boolean isBerechtigtGast6() {
        return berechtigtGast6;
    }

    public void setBerechtigtGast6(boolean berechtigtGast6) {
        this.berechtigtGast6 = berechtigtGast6;
    }

    public boolean isBerechtigtGast7() {
        return berechtigtGast7;
    }

    public void setBerechtigtGast7(boolean berechtigtGast7) {
        this.berechtigtGast7 = berechtigtGast7;
    }

    public boolean isBerechtigtGast8() {
        return berechtigtGast8;
    }

    public void setBerechtigtGast8(boolean berechtigtGast8) {
        this.berechtigtGast8 = berechtigtGast8;
    }

    public boolean isBerechtigtGast9() {
        return berechtigtGast9;
    }

    public void setBerechtigtGast9(boolean berechtigtGast9) {
        this.berechtigtGast9 = berechtigtGast9;
    }

    public boolean isBerechtigtGast10() {
        return berechtigtGast10;
    }

    public void setBerechtigtGast10(boolean berechtigtGast10) {
        this.berechtigtGast10 = berechtigtGast10;
    }

    public boolean isBerechtigtGastOnlineTeilnahmer1() {
        return berechtigtGastOnlineTeilnahmer1;
    }

    public void setBerechtigtGastOnlineTeilnahmer1(boolean berechtigtGastOnlineTeilnahmer1) {
        this.berechtigtGastOnlineTeilnahmer1 = berechtigtGastOnlineTeilnahmer1;
    }

    public boolean isBerechtigtGastOnlineTeilnahmer2() {
        return berechtigtGastOnlineTeilnahmer2;
    }

    public void setBerechtigtGastOnlineTeilnahmer2(boolean berechtigtGastOnlineTeilnahmer2) {
        this.berechtigtGastOnlineTeilnahmer2 = berechtigtGastOnlineTeilnahmer2;
    }

    public boolean isBerechtigtGastOnlineTeilnahmer3() {
        return berechtigtGastOnlineTeilnahmer3;
    }

    public void setBerechtigtGastOnlineTeilnahmer3(boolean berechtigtGastOnlineTeilnahmer3) {
        this.berechtigtGastOnlineTeilnahmer3 = berechtigtGastOnlineTeilnahmer3;
    }

    public boolean isBerechtigtGastOnlineTeilnahmer4() {
        return berechtigtGastOnlineTeilnahmer4;
    }

    public void setBerechtigtGastOnlineTeilnahmer4(boolean berechtigtGastOnlineTeilnahmer4) {
        this.berechtigtGastOnlineTeilnahmer4 = berechtigtGastOnlineTeilnahmer4;
    }

    public boolean isBerechtigtGastOnlineTeilnahmer5() {
        return berechtigtGastOnlineTeilnahmer5;
    }

    public void setBerechtigtGastOnlineTeilnahmer5(boolean berechtigtGastOnlineTeilnahmer5) {
        this.berechtigtGastOnlineTeilnahmer5 = berechtigtGastOnlineTeilnahmer5;
    }

    public boolean isBerechtigtGastOnlineTeilnahmer6() {
        return berechtigtGastOnlineTeilnahmer6;
    }

    public void setBerechtigtGastOnlineTeilnahmer6(boolean berechtigtGastOnlineTeilnahmer6) {
        this.berechtigtGastOnlineTeilnahmer6 = berechtigtGastOnlineTeilnahmer6;
    }

    public boolean isBerechtigtGastOnlineTeilnahmer7() {
        return berechtigtGastOnlineTeilnahmer7;
    }

    public void setBerechtigtGastOnlineTeilnahmer7(boolean berechtigtGastOnlineTeilnahmer7) {
        this.berechtigtGastOnlineTeilnahmer7 = berechtigtGastOnlineTeilnahmer7;
    }

    public boolean isBerechtigtGastOnlineTeilnahmer8() {
        return berechtigtGastOnlineTeilnahmer8;
    }

    public void setBerechtigtGastOnlineTeilnahmer8(boolean berechtigtGastOnlineTeilnahmer8) {
        this.berechtigtGastOnlineTeilnahmer8 = berechtigtGastOnlineTeilnahmer8;
    }

    public boolean isBerechtigtGastOnlineTeilnahmer9() {
        return berechtigtGastOnlineTeilnahmer9;
    }

    public void setBerechtigtGastOnlineTeilnahmer9(boolean berechtigtGastOnlineTeilnahmer9) {
        this.berechtigtGastOnlineTeilnahmer9 = berechtigtGastOnlineTeilnahmer9;
    }

    public boolean isBerechtigtGastOnlineTeilnahmer10() {
        return berechtigtGastOnlineTeilnahmer10;
    }

    public void setBerechtigtGastOnlineTeilnahmer10(boolean berechtigtGastOnlineTeilnahmer10) {
        this.berechtigtGastOnlineTeilnahmer10 = berechtigtGastOnlineTeilnahmer10;
    }

    public boolean isBerechtigtAktionaer() {
        return berechtigtAktionaer;
    }

    public void setBerechtigtAktionaer(boolean berechtigtAktionaer) {
        this.berechtigtAktionaer = berechtigtAktionaer;
    }

    public boolean isBerechtigtAngemeldeterAktionaer() {
        return berechtigtAngemeldeterAktionaer;
    }

    public void setBerechtigtAngemeldeterAktionaer(boolean berechtigtAngemeldeterAktionaer) {
        this.berechtigtAngemeldeterAktionaer = berechtigtAngemeldeterAktionaer;
    }

    public boolean isBerechtigtOnlineTeilnahmeAktionaer() {
        return berechtigtOnlineTeilnahmeAktionaer;
    }

    public void setBerechtigtOnlineTeilnahmeAktionaer(boolean berechtigtOnlineTeilnahmeAktionaer) {
        this.berechtigtOnlineTeilnahmeAktionaer = berechtigtOnlineTeilnahmeAktionaer;
    }

    public int getAktiv() {
        return aktiv;
    }

    public void setAktiv(int aktiv) {
        this.aktiv = aktiv;
    }

    public int getUnterlagenbereichMenueNr() {
        return unterlagenbereichMenueNr;
    }

    public void setUnterlagenbereichMenueNr(int unterlagenbereichMenueNr) {
        this.unterlagenbereichMenueNr = unterlagenbereichMenueNr;
    }

    public int getReihenfolgeUnterlagenbereich() {
        return reihenfolgeUnterlagenbereich;
    }

    public void setReihenfolgeUnterlagenbereich(int reihenfolgeUnterlagenbereich) {
        this.reihenfolgeUnterlagenbereich = reihenfolgeUnterlagenbereich;
    }

    public int getPreviewUnterlagenbereich() {
        return previewUnterlagenbereich;
    }

    public void setPreviewUnterlagenbereich(int previewUnterlagenbereich) {
        this.previewUnterlagenbereich = previewUnterlagenbereich;
    }

    public boolean isUrsprungIstEinVerweis() {
        return ursprungIstEinVerweis;
    }

    public void setUrsprungIstEinVerweis(boolean ursprungIstEinVerweis) {
        this.ursprungIstEinVerweis = ursprungIstEinVerweis;
    }

    public int getVerweisAufIdent() {
        return verweisAufIdent;
    }

    public void setVerweisAufIdent(int verweisAufIdent) {
        this.verweisAufIdent = verweisAufIdent;
    }

}
