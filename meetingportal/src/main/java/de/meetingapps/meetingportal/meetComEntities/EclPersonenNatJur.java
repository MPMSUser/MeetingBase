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

/*TODO _DBBereinigung tbl_meldungen: Personendaten sind noch in tableMeldungen vorhanden - dort raus*/
/*TODO _VollmachtenEmpfangen: Vollmachten empfangen für bestehende Person ermöglichen*/
/* 
 * 
 * Zu Personen kann dann gespeichert werden:
 * 	für welche idents sind sie bereit Vollmachten zu empfangen
 * 	welche (sonstigen) Karten sind ihnen noch zugeordnet (z.B. im Rahmen der Smartphone-/Online-Teilnahme
 * 
 * 
 * Vollmachtsempfang:
 * 	Vollmachtsgeber zeigt an:
 * 		ident der EK, wenn er nicht alle ihm zugeordneten übergeben will
 * 		ident seiner Person, wenn er alle ihm zugeordneten übergeben will
 * 
 */

/**Vertreter - i.d.R. natürliche Personen, ausnahmsweise auch juristische Personen, die als Vertreter von 
 * Aktionären tätig sind / sein können 
 * 
 * in EclWillenserklaerung werden sie über EclVertreter.ident mit einer meldung verknüpft (Vollmacht vom Aktionär),
 * bzw. EclVertreter.ident (Vollmachtsgeber) und EclVertreter.ident (Unterbevollmächtigter) mit meldung verknüpft
 * (Untervollmachten).
/**Personen - juristisch und natürlich.
 * Verwendung:
 * > Bevollmächtigte (Dritte) - über DbWillenserklaerung mit DbMeldungen verbunden.
 *  	In EclWillenserklaerung werden sie über EclVertreter.ident mit einer meldung verknüpft (Vollmacht vom Aktionär),
 * 		bzw. EclVertreter.ident (Vollmachtsgeber) und EclVertreter.ident (Unterbevollmächtigter) mit meldung verknüpft
 * 		(Untervollmachten).
 * 
 * > "Ausgelagerte" Adressen / Personen / Institute für DbMeldungen - direkt über Fremdschlüssel
 * 	verbunden
 *  
 *  
 *  Hinweis: Aufbereitungs-Funktionen in BlPersonenNatJur!
 *  
 */
public class EclPersonenNatJur implements Serializable {
    private static final long serialVersionUID = -527796353746446302L;

    public int mandant = 0;

    /**Primärschlüssel zusammen mit mandant - wird intern vergeben*/
    public int ident = 0;
    /**Ident, die auf einem Smartphone aktiv war, und "gegen die" verglichen wurde dass diese ident
     * die selbe Person ist wie diese Entity */
    public int istSelbePersonWieIdent = 0;
    /**=1 => Übereinstimmung von ident und istSelbePersonWieIdent wurde "augenscheinlich" überprüft.
     * =0 => nur vom Aktioär selbst so eingegeben*/
    public int uebereinstimmungSelbePersonWurdeUeberprueft = 0;

    /**Versionsnummerierung zum Erkennen, ob DB-Satz von anderem User upgedatet wurde.
     * Darf nur von Db-Verwaltung selbst verwendet werden!*/
    public long db_version = 0;

    /**==1 => Person ist "ausgelagerter" Meldungssatz
     * ==0 => Person ist "Bevollmächtigter Dritter"
     */
    public int gehoertZuMeldung = 0;

    /**Wenn =0, dann juristische Person, d.h. dieser Vertreter ist nicht als "Endvertreter" zulässig*/
    public int istNatuerlichePerson = 1;

    /**13 Stellen: V/A/S/1-9,0*/
    public String stimmausschluss = "";
    /*TODO _stimmausschluss: klären, wie das dann auf Meldeebene / Personenebene gehandhabt wird. Wann wird es dort ein-/und vorallem ausgetragen? Möglicherweise zwei Felder - eines gebunden an Aktionär, eines gebunden an den jeweiligen Vertreter. Achtung, Vollmachtskette!!!*/

    /**Kurztext - wenn gefüllt, dann über "Schnellsuche" erreichbar. Sollte z.B. auch Hinweis auf Institution enthalten, z.B.
     * Bitte nicht für "normale" Vertreter füllen - da diese sonst auch in der Schnellsuche erscheinen.
     * Length=80
     */
    public String kurztext = "";

    /**Für Teilnehmerverzeichnis. Wird aus Detailfelder zusammengesetzt, wenn diese gefüllt
     * Length=80*/
    public String kurzName = "";

    /**Für Teilnehmerverzeichnis. Wird aus Detailfelder zusammengesetzt, wenn diese gefüllt
     * Length=80*/
    public String kurzOrt = "";

    /**Die folgenden Felder dienen grundsätzlich für Versandaktionen - sind für die Präsenzabwicklung nicht wirklich erforderlich*/

    /**Verweis auf Anredendatei*/
    public int anrede = 0;

    /**Length=30*/
    public String titel = "";

    /**Length=30*/
    public String adelstitel = "";

    /**Length=80*/
    public String name = "";

    /**Length=80*/
    public String vorname = "";

    /**Length=80*/
    public String zuHdCo = "";

    /**Length=80*/
    public String zusatz1 = "";

    /**Length=80*/
    public String zusatz2 = "";

    /**Length=80*/
    public String strasse = "";

    /**Length=4*/
    public String land = "";

    /**Length=20*/
    public String plz = "";

    /**Length=80*/
    public String ort = "";

    /**Verweis auf Satz in tbl_personenNatJurVersandadresse*/
    public int identVersandadresse = 0;

    /**Mailadresse für elektronischen Versand von EKs etc.
     * (nicht zu verwechseln mit der Registrierung im
     * Email-Portal für den elektronischen Einladungsversand)
     * 
     * Für ku302_303:
     * 01 = Aktionär (gefolgt von GP-Nummer)
     * 02 = gesetzlicher Vertreter
     * 03 = Angestellte/r
     * 04 = Ehegatte
     * 05 = Eltern
     * 06 = Ur-/Großeltern
     * 07 = Schwiegertochter/-sohn
     * 08 = Kinder
     * 09 = Ur-/Enkelkinder
     * 10 = Verwandte in gerader Linie oder deren Ehegatten
     * 
     * Length=80*/
    @Deprecated
    public String mailadresse = "";

    /**Konkrete Verwendung noch nicht ganz klar
     * 
     * Temporäre Verwendung: > 0 ist Insti, enthält dann instiident (nur bei PersonNatJur zu Gästen)
     * */
    public int kommunikationssprache = 0;

    /** Für den jeweiligen Vertreter selbst. 
     * Daten sind bei Gästen und Aktionären redundant zu EclLoginDaten, damit Suche und Anzeige leichter ist 
     * Length=20
     */
    public String loginKennung = "";

    @Deprecated
    /**Length=20*/
    public String loginPasswort = "";

    /**Length=30
     * AAA = Mandantenschlüssel
     * BBBBBB = HV-Datum
     * CC=zwei Buchstaben Vorname
     * DD=zwei Buchstaben Ort
     * EEEEE =lfd Nummer*/
    public String oeffentlicheID = "";

    /*TODO _VollmachtenEmpfangen: "öffentliche Schlüssel etc." für die elektronische Übermittlung
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

    public String liefereTitelVornameName() {
        String titelVornameName = "";
        if (!titel.isEmpty()) {
            titelVornameName = titel + " ";
        }
        if (!vorname.isEmpty()) {
            titelVornameName = titelVornameName + vorname + " ";
        }
        titelVornameName = titelVornameName + name;
        return titelVornameName;
    }

 
    public String liefereNameKommaTitelVorname() {
        String nameKommaTitelVorname="";
        boolean kommaEingefuegt=false;

        nameKommaTitelVorname = name;
        if (titel.length() != 0) {
            if (kommaEingefuegt==false) {
                nameKommaTitelVorname+=",";
                kommaEingefuegt=true;
            }
            nameKommaTitelVorname = nameKommaTitelVorname+" "+titel;
        }
        if (vorname.length() != 0) {
            if (kommaEingefuegt==false) {
                nameKommaTitelVorname+=",";
                kommaEingefuegt=true;
            }
            nameKommaTitelVorname = nameKommaTitelVorname+" "+vorname;
        }
        return nameKommaTitelVorname;
    }

    /**************Standard getter und setter******************************/
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

    public int getIstSelbePersonWieIdent() {
        return istSelbePersonWieIdent;
    }

    public void setIstSelbePersonWieIdent(int istSelbePersonWieIdent) {
        this.istSelbePersonWieIdent = istSelbePersonWieIdent;
    }

    public int getUebereinstimmungSelbePersonWurdeUeberprueft() {
        return uebereinstimmungSelbePersonWurdeUeberprueft;
    }

    public void setUebereinstimmungSelbePersonWurdeUeberprueft(int uebereinstimmungSelbePersonWurdeUeberprueft) {
        this.uebereinstimmungSelbePersonWurdeUeberprueft = uebereinstimmungSelbePersonWurdeUeberprueft;
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

    public int getIdentVersandadresse() {
        return identVersandadresse;
    }

    public void setIdentVersandadresse(int identVersandadresse) {
        this.identVersandadresse = identVersandadresse;
    }

    public String getMailadresse() {
        return mailadresse;
    }

    public void setMailadresse(String mailadresse) {
        this.mailadresse = mailadresse;
    }

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
}
