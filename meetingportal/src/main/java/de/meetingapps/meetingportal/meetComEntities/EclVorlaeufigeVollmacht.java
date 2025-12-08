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

public class EclVorlaeufigeVollmacht implements Serializable {
    private static final long serialVersionUID = 8859761718953882902L;

    /**Primärschlüssel - wird intern vergeben*/
    public int ident = 0;

    /**Versionsnummerierung zum Erkennen, ob DB-Satz von anderem User upgedatet wurde.
     * Darf nur von Db-Verwaltung selbst verwendet werden!*/
    public long db_version = 0;

    /*++++++++++++++Storno-Kennzeichen++++++++++++++++*/
    /**0 => diese Vollmacht ist die derzeit geltende
     * 1 => diese Vollmacht wurde storniert
     * 2 => Vollmacht wird nur als Nachweis gewertet
     * 3 => Vollmacht wird nicht gewertet
     * 4 => noch in Arbeit
     */
    public int storniert = 0;

    /*+++++++++++++Erteilt von ++++++++++++++++++++++*/
    /**Erteilt von
     * =1 => aktienregisterIdent
     * =2 => PersonNatJur / sonstige Kennung*/
    public int erteiltVonArt = 0;

    /**Wenn erteiltVonArt==1, dann aktienregisterIdent, ansonsten personNatJurIdent*/
    public int erteiltVonIdent = 0;

    /**E-Mail-Adresse von Vollmachtgeber - für Benachrichtigung bzgl. Prüfung
     * LEN=100*/
    public String eMailVollmachtgeber = "";

    /*++++++++++++++Bevollmächtigter - Eingabedaten++++++++++++++++*/

    /**1=Bevollmächtigter ist Aktionär
     * 2=sonstiger Bevollmächtigter
     */
    public int bevollmaechtigterArt = 0;

    /**Text lt. Vollmacht
     * LEN=100
     */
    public String bevollmaechtigterArtText = "";

    /**Aktionärsnummer*/
    public int bevollmaechtigterAktienregisterIdent = 0;

    /**Titel
     * LEN=30*/
    public String bevollmaechtigterTitel = "";

    /**Nachname
     * LEN=80*/
    public String bevollmaechtigterName = "";

    /**Vorname
     * LEN=80*/
    public String bevollmaechtigterVorname = "";

    /**Zusatzfeld 1, CO etc.
     * LEN=80
     */
    public String bevollmaechtigterZusatz1 = "";

    /**Zusatzfeld 2, CO etc.
     * LEN=80
     */
    public String bevollmaechtigterZusatz2 = "";

    /**Straße
     * LEN=80*/
    public String bevollmaechtigterStrasse = "";

    /**PLZ (ggf. mit Land)
     * LEN=20*/
    public String bevollmaechtigterPlz = "";

    /**Ort
     * LEN=80*/
    public String bevollmaechtigterOrt = "";

    /**
     * E-Mail-Adresse
     * LEN=100*/
    public String bevollmaechtigterEMail = "";

    /**Wie von Vollmachtgeber auf Formular eingetragen - zum Abgleich von "später geltenden"
     * LEN=50*/
    public String eingabeDatum = "";

    /**Wie von Vollmachtgeber auf Formular eingetragen
     * LEN=50*/
    public String eingabeOrt = "";

    /*++++++++++++++Prüfstatus+++++++++++++++++++*/
    /**0=noch nicht geprüft
     * 1=Prüfung läuft
     * 2=geprüft, genehmigt
     * 3=abgelehnt
     * 4=zurückgestellt, schriftliche Vollmacht fehlt
     * */
    public int pruefstatus = 0;

    /**Abgelehnt weil
     * 1=zu viele Vollmachten
     * 		(Der Bevollmächtigte vertritt bereits die max. zulässige Anzahl an Mitgliedern)
     * 2=Daten ungültig (z.B. offenkundig falsche E-Mail-Adresse, Name, Straße)
     * 		(Die Vollmacht enthält nicht plausible Angaben zum bevollmächtigten Vertreter)
     * 3=Bevollmächtigte ist nicht berechtigt (z.kB. keine natürliche Person)
     * 		(Der Bevollmächtigte ist nicht zur Vertretung berechtigt (gemäß § 43 Abs. 5 GenG in Verbindung mit § 21 Abs. 4 der Satzung)
     * 4=Vollmacht unvollständig
     * 		(Unterschrift/en fehlen bzw. nicht korrekt)
     * 5=Vollmacht nicht lesbar
     * 		(Die Vollmacht ist nicht lesbar)
     * 6=E-Mail-Adresse fehlt
     * 7=Sonstiges (=> Mitgliederbetreuung)
     * 		(bitte wenden Sie sich an die Mitgliederbetreuung)
     * 8=Sonstiges (siehe Begründung)
     * 
     */
    public int abgelehntWeil = 0;

    /**LEN=300*/
    public String abgelehntWeilText = "";

    /*++++++++++++Bevollmächtigter - ausgeführt++++++++++++++++++++++*/

    /**=1 => aktienregisterIdent (d.h. Bevollmächtigter ist ein anderer Aktionär
     * =2 => PersonNatJur / sonstige Kennung*/
    public int bevollmaechtigterAusgefuehrtArt = 0;

    /**Wenn bevollmaechtigterAusgefuehrtArt==1, dann aktienregisterIdent, ansonsten personNatJurIdent*/
    public int bevollmaechtigterAusgefuehrtIdent = 0;

    /**=1 => ist gesetzlicher Vertreter, d.h. zählt nicht für Vollmachten*/
    public int bevollmaechtigterAusgefuehrtIstGesetzlich = 0;

    /********************Nicht in Datenbank*********************************/
    /**automatisch (temporär) erzeugte Untervollmachten wg. Gesetzlicher Vollmacht*/
    public boolean vollmachtIstFolgeVonGesetzlicher = false;

    /**Vollmachtgebendes Mitglied*/
    public EclAktienregister eclAktienregister = null;

    /*************Standard getter und setter******************/

    public int getIdent() {
        return ident;
    }

    public void setIdent(int ident) {
        this.ident = ident;
    }

    public long getDb_version() {
        return db_version;
    }

    public void setDb_version(long db_version) {
        this.db_version = db_version;
    }

    public int getStorniert() {
        return storniert;
    }

    public void setStorniert(int storniert) {
        this.storniert = storniert;
    }

    public int getErteiltVonArt() {
        return erteiltVonArt;
    }

    public void setErteiltVonArt(int erteiltVonArt) {
        this.erteiltVonArt = erteiltVonArt;
    }

    public int getErteiltVonIdent() {
        return erteiltVonIdent;
    }

    public void setErteiltVonIdent(int erteiltVonIdent) {
        this.erteiltVonIdent = erteiltVonIdent;
    }

    public String geteMailVollmachtgeber() {
        return eMailVollmachtgeber;
    }

    public void seteMailVollmachtgeber(String eMailVollmachtgeber) {
        this.eMailVollmachtgeber = eMailVollmachtgeber;
    }

    public int getBevollmaechtigterArt() {
        return bevollmaechtigterArt;
    }

    public void setBevollmaechtigterArt(int bevollmaechtigterArt) {
        this.bevollmaechtigterArt = bevollmaechtigterArt;
    }

    public String getBevollmaechtigterArtText() {
        return bevollmaechtigterArtText;
    }

    public void setBevollmaechtigterArtText(String bevollmaechtigterArtText) {
        this.bevollmaechtigterArtText = bevollmaechtigterArtText;
    }

    public int getBevollmaechtigterAktienregisterIdent() {
        return bevollmaechtigterAktienregisterIdent;
    }

    public void setBevollmaechtigterAktienregisterIdent(int bevollmaechtigterAktienregisterIdent) {
        this.bevollmaechtigterAktienregisterIdent = bevollmaechtigterAktienregisterIdent;
    }

    public String getBevollmaechtigterTitel() {
        return bevollmaechtigterTitel;
    }

    public void setBevollmaechtigterTitel(String bevollmaechtigterTitel) {
        this.bevollmaechtigterTitel = bevollmaechtigterTitel;
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

    public String getBevollmaechtigterZusatz1() {
        return bevollmaechtigterZusatz1;
    }

    public void setBevollmaechtigterZusatz1(String bevollmaechtigterZusatz1) {
        this.bevollmaechtigterZusatz1 = bevollmaechtigterZusatz1;
    }

    public String getBevollmaechtigterZusatz2() {
        return bevollmaechtigterZusatz2;
    }

    public void setBevollmaechtigterZusatz2(String bevollmaechtigterZusatz2) {
        this.bevollmaechtigterZusatz2 = bevollmaechtigterZusatz2;
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

    public int getBevollmaechtigterAusgefuehrtArt() {
        return bevollmaechtigterAusgefuehrtArt;
    }

    public void setBevollmaechtigterAusgefuehrtArt(int bevollmaechtigterAusgefuehrtArt) {
        this.bevollmaechtigterAusgefuehrtArt = bevollmaechtigterAusgefuehrtArt;
    }

    public int getBevollmaechtigterAusgefuehrtIdent() {
        return bevollmaechtigterAusgefuehrtIdent;
    }

    public void setBevollmaechtigterAusgefuehrtIdent(int bevollmaechtigterAusgefuehrtIdent) {
        this.bevollmaechtigterAusgefuehrtIdent = bevollmaechtigterAusgefuehrtIdent;
    }

    public int getBevollmaechtigterAusgefuehrtIstGesetzlich() {
        return bevollmaechtigterAusgefuehrtIstGesetzlich;
    }

    public void setBevollmaechtigterAusgefuehrtIstGesetzlich(int bevollmaechtigterAusgefuehrtIstGesetzlich) {
        this.bevollmaechtigterAusgefuehrtIstGesetzlich = bevollmaechtigterAusgefuehrtIstGesetzlich;
    }
}
