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

import de.meetingapps.meetingportal.meetComEntities.EclPersonenNatJur;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

@SessionScoped
@Named
public class EclPersonenNatJurM implements Serializable {
    private static final long serialVersionUID = 7438761678241678662L;

    private int mandant = 0;

    /**Primärschlüssel zusammen mit mandant - wird intern vergeben*/
    private int ident = 0;
    /**Versionsnummerierung zum Erkennen, ob DB-Satz von anderem User upgedatet wurde.
     * Darf nur von Db-Verwaltung selbst verwendet werden!*/
    private long db_version = 0;

    /**==1 => Person ist "ausgelagerter" Meldungssatz
     * ==0 => Person ist "Bevollmächtigter Dritter"
     */
    private int gehoertZuMeldung = 0;

    /**Wenn =0, dann juristische Person, d.h. dieser Vertreter ist nicht als "Endvertreter" zulässig*/
    private int istNatuerlichePerson = 1;

    /**13 Stellen: V/A/S/1-9,0*/
    private String stimmausschluss = "";
    /*TODO: klären, wie das dann in EclMeldung gehandhabt wird. Wann wird es dort ein-/und vorallem ausgetragen?
     * Möglicherweise zwei Felder - eines gebunden an Aktionär, eines gebunden an den jeweiligen Vertreter. Achtung, Vollmachtskette!!!
     */

    /**Kurztext - wenn gefüllt, dann über "Schnellsuche" erreichbar. Sollte z.B. auch Hinweis auf Institution enthalten, z.B.
     * Bitte nicht für "normale" Vertreter füllen - da diese sonst auch in der Schnellsuche erscheinen.
     * Length=80
     */
    private String kurztext = "";

    /**Für Teilnehmerverzeichnis. Wird aus Detailfelder zusammengesetzt, wenn diese gefüllt
     * Length=80*/
    private String kurzName = "";

    /**Für Teilnehmerverzeichnis. Wird aus Detailfelder zusammengesetzt, wenn diese gefüllt
     * Length=80*/
    private String kurzOrt = "";

    /**Die folgenden Felder dienen grundsätzlich für Versandaktionen - sind für die Präsenzabwicklung nicht wirklich erforderlich*/

    /**Verweis auf Anredendatei*/
    private int anrede = 0;

    /**Length=30*/
    private String titel = "";

    /**Length=30*/
    private String adelstitel = "";

    /**Length=80*/
    private String name = "";

    /**Length=80*/
    private String vorname = "";

    /**Length=80*/
    private String zuHdCo = "";

    /**Length=80*/
    private String zusatz1 = "";

    /**Length=80*/
    private String zusatz2 = "";

    /**Length=80*/
    private String strasse = "";

    /**Length=4*/
    private String land = "";

    /**Length=20*/
    private String plz = "";

    /**Length=80*/
    private String ort = "";

    /**Verweis auf Satz in tbl_personenNatJurVersandadresse*/
    private int identVersandadresse = 0;

    /**Mailadresse für elektronischen Versand von EKs etc.
     * (nicht zu verwechseln mit der Registrierung im
     * Email-Portal für den elektronischen Einladungsversand)
     * Length=80*/
    @Deprecated
    private String mailadresse = "";

    /**Konkrete Verwendung noch nicht ganz klar*/
    private int kommunikationssprache = 0;

    /** Für den jeweiligen Vertreter selbst. 
     * Length=20
     */
    private String loginKennung = "";

    /**Length=20*/
    private String loginPasswort = "";

    /**Length=30
     * AAA = Mandantenschlüssel
     * BBBBBB = HV-Datum
     * CC=zwei Buchstaben Vorname
     * DD=zwei Buchstaben Ort
     * EEEEE =lfd Nummer*/

    public void copyFrom(EclPersonenNatJur person) {
        this.mandant = person.mandant;
        this.ident = person.ident;
        this.db_version = person.db_version;
        this.gehoertZuMeldung = person.gehoertZuMeldung;
        this.istNatuerlichePerson = person.istNatuerlichePerson;
        this.stimmausschluss = person.stimmausschluss;
        this.kurztext = person.kurztext;
        this.kurzName = person.kurzName;
        this.kurzOrt = person.kurzOrt;
        this.anrede = person.anrede;
        this.titel = person.titel;
        this.adelstitel = person.adelstitel;
        this.name = person.name;
        this.vorname = person.vorname;
        this.zuHdCo = person.zuHdCo;
        this.zusatz1 = person.zusatz1;
        this.zusatz2 = person.zusatz2;
        this.strasse = person.strasse;
        this.land = person.land;
        this.plz = person.plz;
        this.ort = person.ort;
        this.identVersandadresse = person.identVersandadresse;
        this.mailadresse = person.mailadresse;
        this.kommunikationssprache = person.kommunikationssprache;
        this.loginKennung = person.loginKennung;
        this.loginPasswort = person.loginPasswort;

    }

    /****************************Standard Getters/Setters*********************************/

    private String oeffentlicheID = "";

    public int getMandant() {
        return mandant;
    }

    public void setMandant(int mandant) {
        this.mandant = mandant;
    }

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

    public int getGehoertZuMeldung() {
        return gehoertZuMeldung;
    }

    public void setGehoertZuMeldung(int gehoertZuMeldung) {
        this.gehoertZuMeldung = gehoertZuMeldung;
    }

    public int getIstNatuerlichePerson() {
        return istNatuerlichePerson;
    }

    public void setIstNatuerlichePerson(int istNatuerlichePerson) {
        this.istNatuerlichePerson = istNatuerlichePerson;
    }

    public String getStimmausschluss() {
        return stimmausschluss;
    }

    public void setStimmausschluss(String stimmausschluss) {
        this.stimmausschluss = stimmausschluss;
    }

    public String getKurztext() {
        return kurztext;
    }

    public void setKurztext(String kurztext) {
        this.kurztext = kurztext;
    }

    public String getKurzName() {
        return kurzName;
    }

    public void setKurzName(String kurzName) {
        this.kurzName = kurzName;
    }

    public String getKurzOrt() {
        return kurzOrt;
    }

    public void setKurzOrt(String kurzOrt) {
        this.kurzOrt = kurzOrt;
    }

    public int getAnrede() {
        return anrede;
    }

    public void setAnrede(int anrede) {
        this.anrede = anrede;
    }

    public String getTitel() {
        return titel;
    }

    public void setTitel(String titel) {
        this.titel = titel;
    }

    public String getAdelstitel() {
        return adelstitel;
    }

    public void setAdelstitel(String adelstitel) {
        this.adelstitel = adelstitel;
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

    public String getZuHdCo() {
        return zuHdCo;
    }

    public void setZuHdCo(String zuHdCo) {
        this.zuHdCo = zuHdCo;
    }

    public String getZusatz1() {
        return zusatz1;
    }

    public void setZusatz1(String zusatz1) {
        this.zusatz1 = zusatz1;
    }

    public String getZusatz2() {
        return zusatz2;
    }

    public void setZusatz2(String zusatz2) {
        this.zusatz2 = zusatz2;
    }

    public String getStrasse() {
        return strasse;
    }

    public void setStrasse(String strasse) {
        this.strasse = strasse;
    }

    public String getLand() {
        return land;
    }

    public void setLand(String land) {
        this.land = land;
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

    public String ggetMailadresse() {
        return mailadresse;
    }

    @Deprecated
    public void setMailadresse(String mailadresse) {
        this.mailadresse = mailadresse;
    }

    @Deprecated
    public int getKommunikationssprache() {
        return kommunikationssprache;
    }

    public void setKommunikationssprache(int kommunikationssprache) {
        this.kommunikationssprache = kommunikationssprache;
    }

    public String getLoginKennung() {
        return loginKennung;
    }

    public void setLoginKennung(String loginKennung) {
        this.loginKennung = loginKennung;
    }

    public String getLoginPasswort() {
        return loginPasswort;
    }

    public void setLoginPasswort(String loginPasswort) {
        this.loginPasswort = loginPasswort;
    }

    public String getOeffentlicheID() {
        return oeffentlicheID;
    }

    public void setOeffentlicheID(String oeffentlicheID) {
        this.oeffentlicheID = oeffentlicheID;
    }

    public int getIdentVersandadresse() {
        return identVersandadresse;
    }

    public void setIdentVersandadresse(int identVersandadresse) {
        this.identVersandadresse = identVersandadresse;
    }

    /*TODO: "öffentliche Schlüssel etc." für die elektronische Übermittlung
     * >> Öffentlicher Schlüssel
     * wer diesen kennt, kann elektronisch den Bevollmächtigten adressieren.
     * muß automatisch vergeben werden?
     * Eindeutigkeit?
     * nur "einmalgültig"? zur Verhinderung von "Massensammlungen" auf diesem Weg?
     * 
     * >>>Akzeptiere Vollmacht von, z.B. durch Abfotografieren eines QR-Codes vom Handy des Vollmachtgebenden mit dem Handy des VOllmachtnehmenden
     * ZutrittsIdent, Stimmkarte, StimmkarteSecond
     * Diese Variante wahrscheinlich einfacher zu Handlen - und protokolliert am besten die gemeinsamen Absichten:
     * > der Bevollmächtigte trägt bei sich die Ident ein - Zeichen, dass er bereit ist die Vollmacht entgegenzunehmen
     * > der Vollmachtsgeber wählt diese aus und erteilt Vollmacht
     */

}
