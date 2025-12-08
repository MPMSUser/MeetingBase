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

import de.meetingapps.meetingportal.meetComEntities.EclWillenserklaerung;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

@SessionScoped
@Named
@Deprecated
public class EclWillenserklaerungM implements Serializable {
    private static final long serialVersionUID = 7438761678241678662L;

    private int mandant = 0;

    /**Primärschlüssel zusammen mit mandant - wird intern vergeben
     * Früherer Name: veraenderungsIdent*/
    private int willenserklaerungIdent = 0;
    /**Versionsnummerierung zum Erkennen, ob DB-Satz von anderem User upgedatet wurde.
     * Darf nur von Db-Verwaltung selbst verwendet werden!*/
    private long db_version = 0;

    /**Hier wird angegeben, über welchen Schlüssel die Abgabe der
     * Willenserklärung (Identifikation) erfolgte.
     * =0 => piEclMeldung
     * =1 => piMeldungsIdent
     * =2 => piZutrittsIdent und piKlasse
     * =3 => piZutrittsIdent, Klasse wird automatisch ermittelt (piKlasse=-1)
     * =4 => piStimmkarte
     * =5 => piStimmkarteSecond
     */
    private int identifikationDurch = 0;
    private String identifikationZutrittsIdent = ""; /*Length=20*/ /*TODO $ZutrittsIdent Neben*/
    private String identifikationZutrittsIdentNeben = ""; /*Length=2*/
    private int identifikationKlasse = -1; /*in Zusammenhang mit identifkationDurch=2 / piZutrittsIdent*/
    private String identifikationStimmkarte = ""; /*Lenght=20*/
    private String identifikationStimmkarteSecond = ""; /*Length=20*/

    /**Weg, über den Willenserklärung abgegeben wurde
     * =1	Papier (Post), außerhalb der HV (kann aber durchaus während der HV noch sein)
     * =2	Fax , außerhalb der HV (kann aber durchaus während der HV noch sein)
     * 
     * =11	Email , außerhalb der HV (kann aber durchaus während der HV noch sein)
     * 
     * 21 bis 29 wird als "Weg Internet" betrachtet!
     * =21	Portal – außerhalb der HV (kann aber durchaus während der HV noch sein)
     * =22	App – außerhalb der HV (kann aber durchaus während der HV noch sein)
     * 
     * =51  Dienstleister manuell erfaßt
     * 
     * =101	Schnittstelle von Extern
     * 
     * =201	Auf konventionellem Weg - während der HV (Counter, Stimmsammlung Papier ...)
     * =202	Selbstbedienung - während der HV (Terminals, App als präsent aktiviert)
     * =203	Online-Teilnahme
     */
    private int erteiltAufWeg = 0;

    /**Fremdschlüssel zu tbl_meldungen*/
    private int meldungsIdent = 0;
    /**Fremdschlüssel zu tbl_meldungen*/
    private int meldungsIdentGast = 0;

    /**Hinweis zu den folgenden Feldern:
     * in den ersten 4 wird immer eingetragen, zu welcher aktueller Liste der Vorgang erfaßt wurde. Gilt auch für
     * Delayed!
     * Beim Drucken werden alle <aktueller Verzeichnisnummer berücksichtigt, die noch nicht gedruckt wurden, und die noch
     * nicht delayed wurden. In welchem Verzeichnis tatsächlich gedruckt wurde, steht dann in den zweiten 4. Diese können
     * also von den ersten 4 abweichen! (z.B. während 2. Nachtrag delayed erfaßt, ergibt gedruckt im 2. Nachtrag)
     */
    /**-1 = Erstpräsenz, ansonsten Nachtragsnummer zu Versammlung1, zu der der Vorgang ERFASST wurde*/
    private int zuVerzeichnisNr1 = 0;
    /**-1 = Erstpräsenz, ansonsten Nachtragsnummer zu Versammlung2, zu der der Vorgang ERFASST wurde*/
    private int zuVerzeichnisNr2 = 0;
    /**-1 = Erstpräsenz, ansonsten Nachtragsnummer zu Versammlung3, zu der der Vorgang ERFASST wurde*/
    private int zuVerzeichnisNr3 = 0;
    /**-1 = Erstpräsenz, ansonsten Nachtragsnummer zu Versammlung4, zu der der Vorgang ERFASST wurde*/
    private int zuVerzeichnisNr4 = 0;

    /**-1 = Erstpräsenz, ansonsten Nachtragsnummer zu Versammlung1, zu der der Vorgang GEDRUCKT wurde. 0 = noch nicht gedruckt!*/
    private int zuVerzeichnisNr1Gedruckt = 0;
    /**-1 = Erstpräsenz, ansonsten Nachtragsnummer zu Versammlung2, zu der der Vorgang ERFASST wurde. 0 = noch nicht gedruckt!*/
    private int zuVerzeichnisNr2Gedruckt = 0;
    /**-1 = Erstpräsenz, ansonsten Nachtragsnummer zu Versammlung3, zu der der Vorgang ERFASST wurde. 0 = noch nicht gedruckt!*/
    private int zuVerzeichnisNr3Gedruckt = 0;
    /**-1 = Erstpräsenz, ansonsten Nachtragsnummer zu Versammlung4, zu der der Vorgang ERFASST wurde. 0 = noch nicht gedruckt!*/
    private int zuVerzeichnisNr4Gedruckt = 0;

    /**Kennzeichnung der Willenserklärung. Alter Name: veraenderung.
     * In Abhängigkeit der willenserklaerung werden andere Felder gefüllt 
     * oder eben auch nicht.
     * Wenn Willenserklärung=Storno, dann "verweisAufWillenserklaerung*/
    private int willenserklaerung = 0;

    /**Handhabung von Delayed-Willenserklärungen
     * = 0 => ganz normale Willenserklärung
     * = 1 => Willenserklärung ist noch delayed
     * = 2 => Willenserklärung war delayed, ist mittlerweile aufgelöst.
     * Feldname ehemals "delayed"
     */
    private int delayed = 0;

    /**Handhabung von Pending-Willenserklärungen
     * = 0 => ganz normale Willenserklärung
     * = 1 => Willenserklärung ist noch pending
     * = 2 => Willenserklärung war pending, ist mittlerweile aufgelöst.
     */
    private int pending = 0;

    /**Neu vergebene ZutrittsIdent bei neuen ZutrittsIdent-Vergabe
     * Ansonsten Angabe, mit welcher ZutrittsIdent diese Willenserklärung abgegeben wurde
     * Length=20*/
    private String zutrittsIdent = "";
    private String zutrittsIdentNeben = ""; /*TODO $ZutrittsIdent*/
    /**ersetzte ZutrittsIdent, falls Storno einer solchen
     * Length=20*/
    private String zutrittsIdentErsetzt = "";
    private String zutrittsIdentNebenErsetzt = ""; /*TODO $ZutrittsIdent*/

    /**Lenght=20*/
    private String stimmkarte1 = "";
    /**Lenght=20*/
    private String stimmkarte2 = "";
    /**Lenght=20*/
    private String stimmkarte3 = "";
    /**Lenght=20*/
    private String stimmkarte4 = "";
    /**Length=20*/
    private String stimmkarteSecond = "";

    /**Falls Storno, oder "Delayed-Nachbuchung", oder "Pending-Nachbuchung",
     * dann Verweis auf die stornierte/delayete/Pending Willenserklaerung.
     * Verweisart:
     * =1 => Storno: Verweis auf stornierte Willenserklärung
     * =2 => DelayedNachbuchung: Verweis auf delayete Willenserklärung
     * =3 => Pending-Auflösung: Verweis auf Willenserklärung, die im Pending war
     * =4 => Widerruf: Verweis auf widerrufene Willenserklärung
     * früher: verweisAufVeraenderungsIdemt*/
    private int verweisart = 0;
    /**Falls Storno, oder "Delayed-Nachbuchung", oder "Pending-Nachbuchung",
     * dann Verweis auf die stornierte/delayete/Pending Willenserklaerung.
     * Verweisart:
     * =1 => Storno: Verweis auf stornierte Willenserklärung
     * =2 => DelayedNachbuchung: Verweis auf delayete Willenserklärung
     * =3 => Pending-Auflösung: Verweis auf Willenserklärung, die im Pending war
     * =4 => Widerruf: Verweis auf widerrufene Willenserklärung
     * früher: verweisAufVeraenderungsIdemt*/
    private int verweisAufWillenserklaerung = 0;

    /**wenn >0, dann wurde diese Willenserklärung automatisch generiert aufgrund der Willenserklärung mit ident folgebuchungFuerIdent*/
    private int folgeBuchungFuerIdent = 0;

    /**Ident dessen, der die Willenserklärung abgibt. Durchgängige Verwendung noch unklar. In jedem Fall jedoch zu verwenden bei:
     * > Vollmacht an Dritte - der Vollmachtsgeber
     * 
     * Ident=	Verweis auf tbl_vertreter-Ident, falls von einem Vertreter ausgehend
     * Ident=   -1, falls von Aktionär selbst ausgehend
     * Ident=	0, falls undefiniert.	
     */
    private int willenserklaerungGeberIdent = 0;

    /**Bei Vollmacht an Dritte: Verweis auf Ident in tbl_vertreter
     * Bei Vollmacht-Storno an sammelkarten: pAufnehmendeSammelkarteIdent (Eingabeparameter übernommen!)*/
    private int bevollmaechtigterDritterIdent = 0;

    /**Bei Vollmachten an Sammelkarten etc.: Verweis auf Ident der Sammelkarte, in der die Aufnahme erfolgt / herausgenommen wird etc.*/
    private int identMeldungZuSammelkarte = 0;

    /**Bei Änderung Vollmachten an Sammelkarten etc.: Verweis auf ident, die geändert wurde
     * (also auf die ursprüngliche, vorhergehende Vollmacht/Weisung*/
    private int identGeaenderteMeldungZuSammelkarte = 0;

    /**Length=19
     * YYYY-MM-DD HH:MM:SS
     */
    private String veraenderungszeit = "";

    /** Ehemals user*/
    private int benutzernr = 0;
    private int arbeitsplatz = 0;

    /** =1 => wurde Qualitätsgesichert*/
    private int istKontrolliert = 0;

    /**=0 => noch nicht im Protokoll gedruckt*/
    private int protokollnr = 0;

    public void copyFrom(EclWillenserklaerung willen) {

        this.mandant = willen.mandant;
        this.willenserklaerungIdent = willen.willenserklaerungIdent;
        this.db_version = willen.db_version;
        this.identifikationDurch = willen.identifikationDurch;
        this.identifikationZutrittsIdent = willen.identifikationZutrittsIdent;
        this.identifikationKlasse = willen.identifikationKlasse;
        this.identifikationStimmkarte = willen.identifikationStimmkarte;
        this.identifikationStimmkarteSecond = willen.identifikationStimmkarteSecond;
        this.erteiltAufWeg = willen.erteiltAufWeg;
        this.meldungsIdent = willen.meldungsIdent;
        this.meldungsIdentGast = willen.meldungsIdentGast;
        this.zuVerzeichnisNr1 = willen.zuVerzeichnisNr1;
        this.zuVerzeichnisNr2 = willen.zuVerzeichnisNr2;
        this.zuVerzeichnisNr3 = willen.zuVerzeichnisNr3;
        this.zuVerzeichnisNr4 = willen.zuVerzeichnisNr4;
        this.zuVerzeichnisNr1Gedruckt = willen.zuVerzeichnisNr1Gedruckt;
        this.zuVerzeichnisNr2Gedruckt = willen.zuVerzeichnisNr2Gedruckt;
        this.zuVerzeichnisNr3Gedruckt = willen.zuVerzeichnisNr3Gedruckt;
        this.zuVerzeichnisNr4Gedruckt = willen.zuVerzeichnisNr4Gedruckt;
        this.willenserklaerung = willen.willenserklaerung;
        this.delayed = willen.delayed;
        this.pending = willen.pending;
        this.zutrittsIdent = willen.zutrittsIdent;
        this.zutrittsIdentErsetzt = willen.zutrittsIdentErsetzt;
        this.stimmkarte1 = willen.stimmkarte1;
        this.stimmkarte2 = willen.stimmkarte2;
        this.stimmkarte3 = willen.stimmkarte3;
        this.stimmkarte4 = willen.stimmkarte4;
        this.stimmkarteSecond = willen.stimmkarteSecond;
        this.verweisart = willen.verweisart;
        this.verweisAufWillenserklaerung = willen.verweisAufWillenserklaerung;
        this.folgeBuchungFuerIdent = willen.folgeBuchungFuerIdent;
        this.willenserklaerungGeberIdent = willen.willenserklaerungGeberIdent;
        this.bevollmaechtigterDritterIdent = willen.bevollmaechtigterDritterIdent;
        this.identMeldungZuSammelkarte = willen.identMeldungZuSammelkarte;
        this.identGeaenderteMeldungZuSammelkarte = willen.identGeaenderteMeldungZuSammelkarte;
        this.veraenderungszeit = willen.veraenderungszeit;
        this.benutzernr = willen.benutzernr;
        this.arbeitsplatz = willen.arbeitsplatz;
        this.istKontrolliert = willen.istKontrolliert;
        this.protokollnr = willen.protokollnr;

    }

    /*****************************Standard Setter und Getter***********************************************/

    public int getMandant() {
        return mandant;
    }

    public void setMandant(int mandant) {
        this.mandant = mandant;
    }

    public int getWillenserklaerungIdent() {
        return willenserklaerungIdent;
    }

    public void setWillenserklaerungIdent(int willenserklaerungIdent) {
        this.willenserklaerungIdent = willenserklaerungIdent;
    }

    public long getDb_version() {
        return db_version;
    }

    public void setDb_version(long db_version) {
        this.db_version = db_version;
    }

    public int getIdentifikationDurch() {
        return identifikationDurch;
    }

    public void setIdentifikationDurch(int identifikationDurch) {
        this.identifikationDurch = identifikationDurch;
    }

    public String getIdentifikationZutrittsIdent() {
        return identifikationZutrittsIdent;
    }

    public void setIdentifikationZutrittsIdent(String identifikationZutrittsIdent) {
        this.identifikationZutrittsIdent = identifikationZutrittsIdent;
    }

    public int getIdentifikationKlasse() {
        return identifikationKlasse;
    }

    public void setIdentifikationKlasse(int identifikationKlasse) {
        this.identifikationKlasse = identifikationKlasse;
    }

    public String getIdentifikationStimmkarte() {
        return identifikationStimmkarte;
    }

    public void setIdentifikationStimmkarte(String identifikationStimmkarte) {
        this.identifikationStimmkarte = identifikationStimmkarte;
    }

    public String getIdentifikationStimmkarteSecond() {
        return identifikationStimmkarteSecond;
    }

    public void setIdentifikationStimmkarteSecond(String identifikationStimmkarteSecond) {
        this.identifikationStimmkarteSecond = identifikationStimmkarteSecond;
    }

    public int getErteiltAufWeg() {
        return erteiltAufWeg;
    }

    public void setErteiltAufWeg(int erteiltAufWeg) {
        this.erteiltAufWeg = erteiltAufWeg;
    }

    public int getMeldungsIdent() {
        return meldungsIdent;
    }

    public void setMeldungsIdent(int meldungsIdent) {
        this.meldungsIdent = meldungsIdent;
    }

    public int getMeldungsIdentGast() {
        return meldungsIdentGast;
    }

    public void setMeldungsIdentGast(int meldungsIdentGast) {
        this.meldungsIdentGast = meldungsIdentGast;
    }

    public int getZuVerzeichnisNr1() {
        return zuVerzeichnisNr1;
    }

    public void setZuVerzeichnisNr1(int zuVerzeichnisNr1) {
        this.zuVerzeichnisNr1 = zuVerzeichnisNr1;
    }

    public int getZuVerzeichnisNr2() {
        return zuVerzeichnisNr2;
    }

    public void setZuVerzeichnisNr2(int zuVerzeichnisNr2) {
        this.zuVerzeichnisNr2 = zuVerzeichnisNr2;
    }

    public int getZuVerzeichnisNr3() {
        return zuVerzeichnisNr3;
    }

    public void setZuVerzeichnisNr3(int zuVerzeichnisNr3) {
        this.zuVerzeichnisNr3 = zuVerzeichnisNr3;
    }

    public int getZuVerzeichnisNr4() {
        return zuVerzeichnisNr4;
    }

    public void setZuVerzeichnisNr4(int zuVerzeichnisNr4) {
        this.zuVerzeichnisNr4 = zuVerzeichnisNr4;
    }

    public int getWillenserklaerung() {
        return willenserklaerung;
    }

    public void setWillenserklaerung(int willenserklaerung) {
        this.willenserklaerung = willenserklaerung;
    }

    public int getDelayed() {
        return delayed;
    }

    public void setDelayed(int delayed) {
        this.delayed = delayed;
    }

    public int getPending() {
        return pending;
    }

    public void setPending(int pending) {
        this.pending = pending;
    }

    public String getZutrittsIdent() {
        return zutrittsIdent;
    }

    public void setZutrittsIdent(String zutrittsIdent) {
        this.zutrittsIdent = zutrittsIdent;
    }

    public String getZutrittsIdentErsetzt() {
        return zutrittsIdentErsetzt;
    }

    public void setZutrittsIdentErsetzt(String zutrittsIdentErsetzt) {
        this.zutrittsIdentErsetzt = zutrittsIdentErsetzt;
    }

    public String getStimmkarte1() {
        return stimmkarte1;
    }

    public void setStimmkarte1(String stimmkarte1) {
        this.stimmkarte1 = stimmkarte1;
    }

    public String getStimmkarteSecond() {
        return stimmkarteSecond;
    }

    public void setStimmkarteSecond(String stimmkarteSecond) {
        this.stimmkarteSecond = stimmkarteSecond;
    }

    public int getVerweisart() {
        return verweisart;
    }

    public void setVerweisart(int verweisart) {
        this.verweisart = verweisart;
    }

    public int getVerweisAufWillenserklaerung() {
        return verweisAufWillenserklaerung;
    }

    public void setVerweisAufWillenserklaerung(int verweisAufWillenserklaerung) {
        this.verweisAufWillenserklaerung = verweisAufWillenserklaerung;
    }

    public int getFolgeBuchungFuerIdent() {
        return folgeBuchungFuerIdent;
    }

    public void setFolgeBuchungFuerIdent(int folgeBuchungFuerIdent) {
        this.folgeBuchungFuerIdent = folgeBuchungFuerIdent;
    }

    public int getWillenserklaerungGeberIdent() {
        return willenserklaerungGeberIdent;
    }

    public void setWillenserklaerungGeberIdent(int willenserklaerungGeberIdent) {
        this.willenserklaerungGeberIdent = willenserklaerungGeberIdent;
    }

    public int getBevollmaechtigterDritterIdent() {
        return bevollmaechtigterDritterIdent;
    }

    public void setBevollmaechtigterDritterIdent(int bevollmaechtigterDritterIdent) {
        this.bevollmaechtigterDritterIdent = bevollmaechtigterDritterIdent;
    }

    public int getIdentMeldungZuSammelkarte() {
        return identMeldungZuSammelkarte;
    }

    public void setIdentMeldungZuSammelkarte(int identMeldungZuSammelkarte) {
        this.identMeldungZuSammelkarte = identMeldungZuSammelkarte;
    }

    public int getIdentGeaenderteMeldungZuSammelkarte() {
        return identGeaenderteMeldungZuSammelkarte;
    }

    public void setIdentGeaenderteMeldungZuSammelkarte(int identGeaenderteMeldungZuSammelkarte) {
        this.identGeaenderteMeldungZuSammelkarte = identGeaenderteMeldungZuSammelkarte;
    }

    public String getVeraenderungszeit() {
        return veraenderungszeit;
    }

    public void setVeraenderungszeit(String veraenderungszeit) {
        this.veraenderungszeit = veraenderungszeit;
    }

    public int getBenutzernr() {
        return benutzernr;
    }

    public void setBenutzernr(int benutzernr) {
        this.benutzernr = benutzernr;
    }

    public int getArbeitsplatz() {
        return arbeitsplatz;
    }

    public void setArbeitsplatz(int arbeitsplatz) {
        this.arbeitsplatz = arbeitsplatz;
    }

    public int getProtokollnr() {
        return protokollnr;
    }

    public void setProtokollnr(int protokollnr) {
        this.protokollnr = protokollnr;
    }

    public String getStimmkarte2() {
        return stimmkarte2;
    }

    public void setStimmkarte2(String stimmkarte2) {
        this.stimmkarte2 = stimmkarte2;
    }

    public String getStimmkarte3() {
        return stimmkarte3;
    }

    public void setStimmkarte3(String stimmkarte3) {
        this.stimmkarte3 = stimmkarte3;
    }

    public String getStimmkarte4() {
        return stimmkarte4;
    }

    public void setStimmkarte4(String stimmkarte4) {
        this.stimmkarte4 = stimmkarte4;
    }

    public int getZuVerzeichnisNr1Gedruckt() {
        return zuVerzeichnisNr1Gedruckt;
    }

    public void setZuVerzeichnisNr1Gedruckt(int zuVerzeichnisNr1Gedruckt) {
        this.zuVerzeichnisNr1Gedruckt = zuVerzeichnisNr1Gedruckt;
    }

    public int getZuVerzeichnisNr2Gedruckt() {
        return zuVerzeichnisNr2Gedruckt;
    }

    public void setZuVerzeichnisNr2Gedruckt(int zuVerzeichnisNr2Gedruckt) {
        this.zuVerzeichnisNr2Gedruckt = zuVerzeichnisNr2Gedruckt;
    }

    public int getZuVerzeichnisNr3Gedruckt() {
        return zuVerzeichnisNr3Gedruckt;
    }

    public void setZuVerzeichnisNr3Gedruckt(int zuVerzeichnisNr3Gedruckt) {
        this.zuVerzeichnisNr3Gedruckt = zuVerzeichnisNr3Gedruckt;
    }

    public int getZuVerzeichnisNr4Gedruckt() {
        return zuVerzeichnisNr4Gedruckt;
    }

    public void setZuVerzeichnisNr4Gedruckt(int zuVerzeichnisNr4Gedruckt) {
        this.zuVerzeichnisNr4Gedruckt = zuVerzeichnisNr4Gedruckt;
    }

    public int getIstKontrolliert() {
        return istKontrolliert;
    }

    public void setIstKontrolliert(int istKontrolliert) {
        this.istKontrolliert = istKontrolliert;
    }

    public String getZutrittsIdentNeben() {
        return zutrittsIdentNeben;
    }

    public void setZutrittsIdentNeben(String zutrittsIdentNeben) {
        this.zutrittsIdentNeben = zutrittsIdentNeben;
    }

    public String getZutrittsIdentNebenErsetzt() {
        return zutrittsIdentNebenErsetzt;
    }

    public void setZutrittsIdentNebenErsetzt(String zutrittsIdentNebenErsetzt) {
        this.zutrittsIdentNebenErsetzt = zutrittsIdentNebenErsetzt;
    }

    public String getIdentifikationZutrittsIdentNeben() {
        return identifikationZutrittsIdentNeben;
    }

    public void setIdentifikationZutrittsIdentNeben(String identifikationZutrittsIdentNeben) {
        this.identifikationZutrittsIdentNeben = identifikationZutrittsIdentNeben;
    }

}
