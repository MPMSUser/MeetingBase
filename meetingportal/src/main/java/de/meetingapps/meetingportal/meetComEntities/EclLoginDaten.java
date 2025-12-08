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

import de.meetingapps.meetingportal.meetComAllg.CaPasswortVerschluesseln;
import de.meetingapps.meetingportal.meetComKonst.KonstHinweisWeitere;

/**
 * Enthält neue, "allgemein gültige" Login-Daten. Jeweils im Zusammenhang mit
 * Aktienregister und PersonNatJur (Mandant) oder ggf. auch für übergreifende
 * Benutzer (uLogin etc.) verwendbar (noch nicht festgelegt).
 * 
 * Ersetzt EclAktienregisterLoginDaten und EclAktienregisterZusatz
 */
public class EclLoginDaten implements Serializable {
    private static final long serialVersionUID = 8403835834863436225L;

    public long db_version = 0;

    /**
     * Wird über Autoincrement vergeben. Wird nur für temporäre Referenzierungen im
     * Portal verwendet
     */
    public int ident = 0;

    /* ++++++++++++++++++aus EclAktienregisterLoginDaten++++++++++++++++++++++ */

    /**
     * Kennung, mit dem sich Teilnehmer im Portal einloggen kann. Ist i.d.R.
     * identisch mit der Aktionärsnummer bzw. der bei PersonNatJur gespeicherten
     * Kennung, kann aber - auf Kundenwunsch - auch abweichend sein.
     * 
     * LEN=20, UNIQUE
     */
    public String loginKennung = "";

    /**
     * Gibt an, zu welcher "Art" diese Kennung gehört, sprich zu Aktienregister,
     * PersonNatJur etc. Siehe KonstLoginKennungArt NEU
     */
    public int kennungArt = 0;

    /**
     * Servernummer, auf der der letzte Login mit dieser Kennung erfolgte. 9999=App
     * (zusammen mit zeitstempel = Eindeutiges Kennzeichen) 100000 = derzeit durch
     * einen anderen Login-/Logout-Vorgang gesperrt (dann auch zeitstempel gefüllt)
     * 
     * <0 => bei nächstem Lesen muß der zugehörige Besitz upgedated werden, da er
     * sich verändert hat (z.B. wg. Stornierung einer Vollmacht). TODO: Prüfen, ob
     * das auch für Standardablauf funktioniert!
     * 
     * Wird nicht bei Insert/Update gefüllt! Sondern nur update über
     * Spezialfunktion!
     */
    public int letzterLoginAufServer;

    /**
     * Relevant, wenn: letzterLoginAufServer=100000 / gesprrt: dann Zeit, seit dem
     * gesperrt ist. Sperre wird ungültig nach 5 Minuten. letzterLoginAufServer=9999
     * / App: dann Zeitstempel, wann der Login war. Wird auch an App übergeben.
     * Damit verifizierbar, ob noch gleicher Login erfolgte.
     * 
     * Wird nicht bei Insert/Update gefüllt! Sondern nur update über
     * Spezialfunktion!
     */
    public long zeitstempel = 0;

    // /**Gleiche Ident wie Aktienregister-Eintrag bzw. personenNatJur, abhängig von
    // kennungArt
    // * Früher: aktienregisterIdent
    // * HALBNEU*/
    // public int gehoertZuIdent=0;

    /**
     * Falls kennungArt=KonstLoginKennungArt.aktienregister: Verweis auf zugehörigen
     * Aktienregistereintrag. Sonst = 0
     */
    public int aktienregisterIdent = 0;

    /**
     * Falls kennungArt=KonstLoginKennungArt.personenNatJur: Verweis auf zugehörige
     * Person.
     * 
     * Falls kennungArt=KonstLoginKennungArt.aktienregister: 0, wenn keine
     * "Selbstanmeldung" bisher erfolgt ist !=0, wenn bereits irgendwann eine
     * "Selbstanmeldung" erfolgt ist (auch wenn möglicherweise wieder storniert).
     * Dient dazu, um als Ziel für Vollmachten an einen Aktionär zu dienen. Wird bei
     * (erster) Selbstanmeldung gesetzt bzw. (bei erneuter Selbstanmeldung) immer
     * wiederverwendet.
     */
    public int personenNatJurIdent = 0;

    /**
     * Gefüllt falls kennungArt=KonstLoginKennungArt.personenNatJur: Meldeident der
     * "Gastkarte"
     */
    public int meldeIdent = 0;

    /**
     * Kennung, die zusätzlich vergeben werden kann. Optional. z.B. verwenden, wenn
     * Erst-Login über loginKennung erfolgt, und dann automatisch oder per
     * Aktionärswahl eine weitere Kennung für weitere Tätigkeiten zugeteilt wird.
     * 
     * LEN=100, Auch Eindeutig (kann aber leer sein!) NEU
     */
    public String loginKennungAlternativ = "";

    /**
     * Verschlüsseltes Passwort. Auch bereits das Initial-Passwort.
     * 
     * Entschlüsselung nicht möglich. LEN=64
     */
    public String passwortVerschluesselt = "";

    /**
     * Initialpasswort. Verschlüsselt, aber umkehrbar. Gefüllt, wenn noch kein
     * eigenes Passwort vergeben. Wird geleert, wenn eigenes Passwort vergeben wird.
     * Gilt aber solange parallel zum eigenen Passwort, bis eigenes Passwort verändert wird.
     * 
     * D.h.: kann verwendet werden, um zusätzlich zum eigenen Passwort ein Initial-Passwort zu vergeben,
     * z.B. für Verwendung auf Versammlung.
     * 
     * Die "mittleren" Zeichen sind das Initialpasswort. Lesender Zugriff
     * idealerweise über lieferePasswortInitialClean()
     * 
     * LEN=64
     */
    public String passwortInitial = "";

    /**
     * Zum passwortAlternativ: wird beim Passwort-Vergessen-Prozess eingetragen. Es
     * wird erst aktiviert (d.h. in das eigentliche passwort übertragen), wenn es
     * erstmalig verwendet wurde (d.h. Verbesserung des Prozesses neues Passwort -
     * das alte Passwort gilt weiter, bis das neue erstmalig verwendet wurde)
     */
    /**
     * Verschlüsseltes Passwort. Auch bereits das Initial-Passwort.
     * 
     * Entschlüsselung nicht möglich. LEN=64 NEU
     */
    public String passwortAlternativVerschluesselt = "";

    /**
     * Initialpasswort. Verschlüsselt, aber umkehrbar. Gefüllt, wenn noch kein
     * eigenes Passwort vergeben. Leer, wenn eigenes Passwort vergeben LEN=64 NEU
     */
    public String passwortAlternativInitial = "";

    /**
     * Uhrzeit, zu dem letzter Login erfolgte. JJJJ.MM.TT HH:MM:SS LEN=19; NEU
     */
    public String letzterLoginZeit = "";

    /**
     * Wird beim Login-/Logout erhöht/erniedrigt. Wert immer >=0 (d.h. beim Logout
     * geht er nicht ins negative) NEU
     */
    public int loginZaehler = 0;

    /** =1 => diese Benutzerkennung darf sich nicht am Portal anmelden */
    public int anmeldenUnzulaessig = 0;

    /**
     * =1 => diese Benutzerkennung darf sich nicht für elektronischen
     * Einladungsversand und nicht für ein dauerhaftes Passwort registrieren.
     * 
     * Dies bedeutet (aktuell noch) gleichzeitig, dass der Passwort-Vergessen-Prozess per
     * E-Mail unzulässig ist
     */
    public int dauerhafteRegistrierungUnzulaessig = 0;

    /**
     * Berechtigungen im Portal gemäß KonstPortalFunktionen - Dokumentation siehe
     * dort
     */
    public long berechtigungPortal = 0;

    /**
     * ++++++++++++++++++++++++++++aus
     * EclAktienregisterZusatz+++++++++++++++++++++++++++++++++++++++++++++++++
     */

    /**
     * Für Portal: Nutzungshinweise für das Aktionärsportal bestätigt 0 = noch nicht
     * bestätigt (bzw. wieder zurückgesetzt) 1 = bestätigt
     */
    public int hinweisAktionaersPortalBestaetigt = 0;

    /**
     * Für Portal: Nutzungshinweise für das HVportal bestätigt 0 = noch nicht
     * bestätigt (bzw. wieder zurückgesetzt) 1 = bestätigt
     */
    public int hinweisHVPortalBestaetigt = 0;

    /**
     * Für Portal: Sonstige Nutzungshinweise  bestätigt.
     * Bitweise die einzelnen Hinweise (siehe KonstHinweisWeitere)
     * 0 = noch nicht bestätigt (bzw. wieder zurückgesetzt) 
     * 1 = bestätigt
     */
    public int hinweisWeitereBestaetigt = 0;

    /**pOffset=1, 2, 4, ... gemäß KonstHinweisWeitere */
    public int liefereBestaetigenHinweisWeitere(int pOffset) {
        int ergebnis = 0;
        switch (pOffset) {
        case 1:
            ergebnis = hinweisWeitereBestaetigt & 1;
            break;
        case 2:
            ergebnis = hinweisWeitereBestaetigt & 2;
            break;
        case 8:
            ergebnis = hinweisWeitereBestaetigt & 8;
            break;
        }
        if (ergebnis > 0) {
            return 1;
        }
        return 0;
    }

    /**pOffset=1, 2, 4, ...gemäß KonstHinweisWeitere; pWert=0 oder 1*/
    public void schreibeBestaetigenHinweisWeitere(int pOffset, int pWert) {
        int gesamt = KonstHinweisWeitere.MAX_WERT - pOffset;
        hinweisWeitereBestaetigt = hinweisWeitereBestaetigt & gesamt;
        if (pWert > 0) {
            switch (pOffset) {
            case 1:
                hinweisWeitereBestaetigt = hinweisWeitereBestaetigt | 1;
            case 2:
                hinweisWeitereBestaetigt = hinweisWeitereBestaetigt | 2;
            case 8:
                hinweisWeitereBestaetigt = hinweisWeitereBestaetigt | 8;
            }
        }

    }

    public boolean liefereQuittungPerEmailBeiAllenWillenserklaerungen() {
        return (liefereBestaetigenHinweisWeitere(KonstHinweisWeitere.QUITTUNG_PER_EMAIL_BEI_ALLEN_WILLENSERKLAERUNGEN)==1);
    }

    public void setzeQuittungPerEmailBeiAllenWillenserklaerungen(boolean pWert) {
        if (pWert) {
            schreibeBestaetigenHinweisWeitere(KonstHinweisWeitere.QUITTUNG_PER_EMAIL_BEI_ALLEN_WILLENSERKLAERUNGEN, 1);
        }
        else {
            schreibeBestaetigenHinweisWeitere(KonstHinweisWeitere.QUITTUNG_PER_EMAIL_BEI_ALLEN_WILLENSERKLAERUNGEN, 0);
        }
    }

    /**
     * Für Portal: Zur Email-Registrierung für E-Versand bereits aufgefordert 0 =
     * noch nicht aufgefordert 1 = aufgefordert, nicht registriert 99 = bereits
     * registriert
     */
    public int eVersandRegistrierung = 0;

    public boolean eVersandRegistriert() {
        return (eVersandRegistrierung == 99);
    }

    /**
     * Zeitpunkt der ersten Email-Registrierung für den elektronischen
     * Einladungsversand. Wird benötigt für Verlosung Länge=19
     */
    public String eVersandRegistrierungErstZeitpunkt = "";

    /**
     * Für Portal: Zur PasswortÄnderung bereits aufgefordert 0 = noch nicht
     * aufgefordert 1 = aufgefordert, nicht registriert 98 =
     * Passwort-Vergessen-Anforderung ist ausstehend 99 = bereits eigenes Passwort
     * 
     * Eigentlich nicht mehr erforderlich, auch nicht zuverlässig verwendbar!
     */
    public int eigenesPasswort = 0;

    public boolean eigenesPasswortVergeben() {
        return ((eigenesPasswort == 98) || (eigenesPasswort == 99));
    }

    /**
     * Key aus Passwort-Vergessen-Link Länge=30
     */
    public String passwortVergessenLink = "";

    /**
     * Passwort-Vergessen-Link erzeugt zum Zeitpunkt Länge=19
     */
    public String passwortVergessenZeitpunkt = "";

    /**
     * Kommunikationssprache 1 = Deutsch 2 = Englisch weitere auf Anfrage :-)
     * 
     * TODO: deaktiviert, bzw. Verwendung als Kennzeichen ob Gast schon mal präsent
     * war: 101=präsent 102=war präsent Alle anderen Werte: nie präsent
     * 
     */
    public int kommunikationssprache = 1;

    /**
     * EMail-Adresse für elektronischen Einladungsversand und ggf. auch sonstige
     * Publikationsverteilungen Länge = 100
     */
    public String eMailFuerVersand = "";

    /**
     * Email-Adresse wurde bereits bestätigt 0 = noch nicht 1 = Ja
     */
    public int emailBestaetigt = 0;

    /**
     * Key aus Email-Bestätigen-Link Länge=30
     */
    public String emailBestaetigenLink = "";

    /**
     * 2. EMail-Adresse für elektronischen Einladungsversand und ggf. auch sonstige
     * Publikationsverteilungen Genaue Verwendung noch nicht ganz geklärt ... aber
     * sicherheitshalber mal eingebaut, da im ku164-Portal vorhanden Länge = 100
     * 
     * 
     * 
     */
    public String eMail2FuerVersand = "";

    /**
     * Email-Adresse wurde bereits bestätigt 0 = noch nicht Bit 1 = Ja
     * Zweiter E-Mail-Adresse für Versand verwenden Bit 2=Ja
     */
    public int email2Bestaetigt = 0;

    /**true => zweites Bit von email2Bestaetigt wird gesetzt
     * false => zweites Bit wird gelöscht
     */
    public void setzeEMail2AdresseVerwenden(boolean pAdresseVerwenden) {
        if (pAdresseVerwenden) {
            email2Bestaetigt=(email2Bestaetigt | 2);
        }
        else {
            email2Bestaetigt=(email2Bestaetigt & 253);
        }
    }

    /**true => erstes Bit von email2Bestaetigt wird gesetzt
     * false => erstes Bit wird gelöscht
     */
    public void setzeEMail2AdresseBestaetigt(boolean pAdresseVerwenden) {
        if (pAdresseVerwenden) {
            email2Bestaetigt=(email2Bestaetigt | 1);
        }
        else {
            email2Bestaetigt=(email2Bestaetigt & 254);
        }
    }
    public boolean liefereEMail2AdresseBestaetigt() {
        if ((email2Bestaetigt & 1)==1) {
            return true;
        }
        else {
            return false;
        }
    }
    
    public boolean liefereEMail2AdresseVerwenden() {
        if ((email2Bestaetigt & 2)==2) {
            return true;
        }
        else {
            return false;
        }
     
    }

    /**
     * Key aus Email-Bestätigen-Link
     * 
     * 
     *
     * 
     * Länge=30
     */
    public String email2BestaetigenLink = "";

    /**=1 => zweiFaktor-Authentifizierung über E-Mail ist aktiv.
     * Derzeit nur in Vorbereitung deshalb in Table, Funktion ansonsten noch nicht implementiert
     * */
    public int zweiFaktorAuthentifizierungAktiv=0;
    
    
    /**Hinweise zu den konferenz-Stati:
     * > Falls "Bitte verlassen" als Status, dann wird dies beim Login des Users auf jeden Fall zurückgesetzt (ist ja dann sicher heraus ...)
     * > Die "Kommen- und Sprechen- Stati" werden ja in jedem Fall im uLogin beim entsprechenden Statuswechsel zurückgesetzt.
     */
    
    /**0 noch kein Test
     * -1 Test war nicht erfolgreich
     * 1 Test war erfolgreich
     */
    public int konferenzTestDurchgefuehrt=0;
    
    /**1 bis 255 = Konferenzraum, in dem Test durchgeführt wird
     * Bit 256 = Bitte in Konferenzraum kommen
     * Bit 1024 = Bitte bereit halten für den Test (nicht verwendet)
     * Bit 2048 = Bitte Konferenzraum verlassen
     * 
     */
    public int konferenzTestAblauf=0;
    
    /**1 bis 255 = Konferenzraum, in dem gesprochen wird
     * Bit 256 = Bitte in Konferenzraum kommen
     * Bit 512 = Bitte sprechen
     * Bit 1024 = Bitte bereit halten für das Einschalten in den Konferenzraum (nicht verwendet)
     * Bit 2048 = Bitte Konferenzraum verlassen
     * 
     */
    public int konferenzSprechen=0;
    
    public int registrierungAktionaersPortalErfolgt=0;
    public long registrierungAktionaersPortalErfolgtZeitstempel=0;
    public int registrierungMitgliederPortalErfolgt=0;
    public long registrierungMitgliederPortalErfolgtZeitstempel=0;
    
    /***************Felder nicht in Datenbank******************/
    /**Enthält eine Kopie von mandant, zur Abfrage ohne Änderungsmöglichkeit
     * in XHTML
     */
    public String loginKennungExtern="";
    
    public EclLoginDaten() {

    }

    public EclLoginDaten(String loginKennung, String passwortInitial) {
        super();
        this.loginKennung = loginKennung;
        this.loginKennungExtern=loginKennung;
        this.passwortInitial = passwortInitial;
    }

    /**Liefert - soweit vorhanden - das Initial-Passwort im Klartext.
     * Leer, wenn kein Initial-Passwort vorhanden*/
    public String lieferePasswortInitialClean() {
        return CaPasswortVerschluesseln.decodeInitialPW(passwortInitial);
    }

    
    /**************************Standard setter und getter****************************************************/
    
    public long getDb_version() {
        return db_version;
    }

    public void setDb_version(long db_version) {
        this.db_version = db_version;
    }

    public int getIdent() {
        return ident;
    }

    public void setIdent(int ident) {
        this.ident = ident;
    }

    public String getLoginKennung() {
        return loginKennung;
    }

    public void setLoginKennung(String loginKennung) {
        this.loginKennung = loginKennung;
    }

    public int getKennungArt() {
        return kennungArt;
    }

    public void setKennungArt(int kennungArt) {
        this.kennungArt = kennungArt;
    }

    public int getLetzterLoginAufServer() {
        return letzterLoginAufServer;
    }

    public void setLetzterLoginAufServer(int letzterLoginAufServer) {
        this.letzterLoginAufServer = letzterLoginAufServer;
    }

    public long getZeitstempel() {
        return zeitstempel;
    }

    public void setZeitstempel(long zeitstempel) {
        this.zeitstempel = zeitstempel;
    }

    public int getAktienregisterIdent() {
        return aktienregisterIdent;
    }

    public void setAktienregisterIdent(int aktienregisterIdent) {
        this.aktienregisterIdent = aktienregisterIdent;
    }

    public int getPersonenNatJurIdent() {
        return personenNatJurIdent;
    }

    public void setPersonenNatJurIdent(int personenNatJurIdent) {
        this.personenNatJurIdent = personenNatJurIdent;
    }

    public int getMeldeIdent() {
        return meldeIdent;
    }

    public void setMeldeIdent(int meldeIdent) {
        this.meldeIdent = meldeIdent;
    }

    public String getLoginKennungAlternativ() {
        return loginKennungAlternativ;
    }

    public void setLoginKennungAlternativ(String loginKennungAlternativ) {
        this.loginKennungAlternativ = loginKennungAlternativ;
    }

    public String getPasswortVerschluesselt() {
        return passwortVerschluesselt;
    }

    public void setPasswortVerschluesselt(String passwortVerschluesselt) {
        this.passwortVerschluesselt = passwortVerschluesselt;
    }

    public String getPasswortInitial() {
        return passwortInitial;
    }

    public void setPasswortInitial(String passwortInitial) {
        this.passwortInitial = passwortInitial;
    }

    public String getPasswortAlternativVerschluesselt() {
        return passwortAlternativVerschluesselt;
    }

    public void setPasswortAlternativVerschluesselt(String passwortAlternativVerschluesselt) {
        this.passwortAlternativVerschluesselt = passwortAlternativVerschluesselt;
    }

    public String getPasswortAlternativInitial() {
        return passwortAlternativInitial;
    }

    public void setPasswortAlternativInitial(String passwortAlternativInitial) {
        this.passwortAlternativInitial = passwortAlternativInitial;
    }

    public String getLetzterLoginZeit() {
        return letzterLoginZeit;
    }

    public void setLetzterLoginZeit(String letzterLoginZeit) {
        this.letzterLoginZeit = letzterLoginZeit;
    }

    public int getLoginZaehler() {
        return loginZaehler;
    }

    public void setLoginZaehler(int loginZaehler) {
        this.loginZaehler = loginZaehler;
    }

    public int getAnmeldenUnzulaessig() {
        return anmeldenUnzulaessig;
    }

    public void setAnmeldenUnzulaessig(int anmeldenUnzulaessig) {
        this.anmeldenUnzulaessig = anmeldenUnzulaessig;
    }

    public int getDauerhafteRegistrierungUnzulaessig() {
        return dauerhafteRegistrierungUnzulaessig;
    }

    public void setDauerhafteRegistrierungUnzulaessig(int dauerhafteRegistrierungUnzulaessig) {
        this.dauerhafteRegistrierungUnzulaessig = dauerhafteRegistrierungUnzulaessig;
    }

    public long getBerechtigungPortal() {
        return berechtigungPortal;
    }

    public void setBerechtigungPortal(long berechtigungPortal) {
        this.berechtigungPortal = berechtigungPortal;
    }

    public int getHinweisAktionaersPortalBestaetigt() {
        return hinweisAktionaersPortalBestaetigt;
    }

    public void setHinweisAktionaersPortalBestaetigt(int hinweisAktionaersPortalBestaetigt) {
        this.hinweisAktionaersPortalBestaetigt = hinweisAktionaersPortalBestaetigt;
    }

    public int getHinweisHVPortalBestaetigt() {
        return hinweisHVPortalBestaetigt;
    }

    public void setHinweisHVPortalBestaetigt(int hinweisHVPortalBestaetigt) {
        this.hinweisHVPortalBestaetigt = hinweisHVPortalBestaetigt;
    }

    public int getHinweisWeitereBestaetigt() {
        return hinweisWeitereBestaetigt;
    }

    public void setHinweisWeitereBestaetigt(int hinweisWeitereBestaetigt) {
        this.hinweisWeitereBestaetigt = hinweisWeitereBestaetigt;
    }

    public int geteVersandRegistrierung() {
        return eVersandRegistrierung;
    }

    public void seteVersandRegistrierung(int eVersandRegistrierung) {
        this.eVersandRegistrierung = eVersandRegistrierung;
    }

    public String geteVersandRegistrierungErstZeitpunkt() {
        return eVersandRegistrierungErstZeitpunkt;
    }

    public void seteVersandRegistrierungErstZeitpunkt(String eVersandRegistrierungErstZeitpunkt) {
        this.eVersandRegistrierungErstZeitpunkt = eVersandRegistrierungErstZeitpunkt;
    }

    public int getEigenesPasswort() {
        return eigenesPasswort;
    }

    public void setEigenesPasswort(int eigenesPasswort) {
        this.eigenesPasswort = eigenesPasswort;
    }

    public String getPasswortVergessenLink() {
        return passwortVergessenLink;
    }

    public void setPasswortVergessenLink(String passwortVergessenLink) {
        this.passwortVergessenLink = passwortVergessenLink;
    }

    public String getPasswortVergessenZeitpunkt() {
        return passwortVergessenZeitpunkt;
    }

    public void setPasswortVergessenZeitpunkt(String passwortVergessenZeitpunkt) {
        this.passwortVergessenZeitpunkt = passwortVergessenZeitpunkt;
    }

    public int getKommunikationssprache() {
        return kommunikationssprache;
    }

    public void setKommunikationssprache(int kommunikationssprache) {
        this.kommunikationssprache = kommunikationssprache;
    }

    public String geteMailFuerVersand() {
        return eMailFuerVersand;
    }

    public void seteMailFuerVersand(String eMailFuerVersand) {
        this.eMailFuerVersand = eMailFuerVersand;
    }

    public int getEmailBestaetigt() {
        return emailBestaetigt;
    }

    public void setEmailBestaetigt(int emailBestaetigt) {
        this.emailBestaetigt = emailBestaetigt;
    }

    public String getEmailBestaetigenLink() {
        return emailBestaetigenLink;
    }

    public void setEmailBestaetigenLink(String emailBestaetigenLink) {
        this.emailBestaetigenLink = emailBestaetigenLink;
    }

    public String geteMail2FuerVersand() {
        return eMail2FuerVersand;
    }

    public void seteMail2FuerVersand(String eMail2FuerVersand) {
        this.eMail2FuerVersand = eMail2FuerVersand;
    }

    public int getEmail2Bestaetigt() {
        return email2Bestaetigt;
    }

    public void setEmail2Bestaetigt(int email2Bestaetigt) {
        this.email2Bestaetigt = email2Bestaetigt;
    }

    public String getEmail2BestaetigenLink() {
        return email2BestaetigenLink;
    }

    public void setEmail2BestaetigenLink(String email2BestaetigenLink) {
        this.email2BestaetigenLink = email2BestaetigenLink;
    }

    public int getZweiFaktorAuthentifizierungAktiv() {
        return zweiFaktorAuthentifizierungAktiv;
    }

    public void setZweiFaktorAuthentifizierungAktiv(int zweiFaktorAuthentifizierungAktiv) {
        this.zweiFaktorAuthentifizierungAktiv = zweiFaktorAuthentifizierungAktiv;
    }

    public String getLoginKennungExtern() {
        return loginKennungExtern;
    }

    public void setLoginKennungExtern(String loginKennungExtern) {
        this.loginKennungExtern = loginKennungExtern;
    }

    /*
     * TODO Weitere Idee: Kennzeichen hier, ob als Gast angelegt, oder als
     * Bevollmächtigter oder Insti. Dann kann jenachdem bei der Teilnahme ein
     * Hinweis gebracht werden, dass etwaige Vollmachten widerrufen wurden, falls
     * keine mehr da sind.
     */

    /**
     * die folgenden Daten werden erstmal nicht übernommen, da Struktur so nicht
     * sinnvoll und an dieser Stelle auch nicht nötig
     */
    // /**Publikationen
    // * Aktuell für 10 verschiedene Publikationen Platz in Datenbank. Position in
    // Array entspricht der Publikationssnummer.
    // * Der Wert jeder Position gibt wieder, ob bzw. wie diese Publikation an
    // diesen Aktionär verschickt werden soll
    // * 0 = kein Versand dieser Publikation
    // * Bit 1 bis Bit 10: Zustellung über den diesem Bit zugeordneten Versandweg.*/
    // public int[] publikationenZustellung=new int[10];
    //
    // /**Kontaktdaten - optional
    // * Länge =40*/
    // public String kontaktTelefonPrivat="";
    //
    // /**Kontaktdaten - optional
    // * Länge =40*/
    // public String kontaktTelefonGeschaeftlich="";
    //
    // /**Kontaktdaten - optional
    // * Länge =40*/
    // public String kontaktTelefonMobil="";
    //
    // /**Kontaktdaten - optional
    // * Länge =40*/
    // public String kontaktTelefonFax="";

}
