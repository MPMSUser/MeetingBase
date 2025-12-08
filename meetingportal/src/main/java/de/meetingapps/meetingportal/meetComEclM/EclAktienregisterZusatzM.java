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

import de.meetingapps.meetingportal.meetComEntities.EclAktienregisterZusatz;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

@SessionScoped
@Named
@Deprecated
public class EclAktienregisterZusatzM implements Serializable {
    private static final long serialVersionUID = -1783898382339870569L;

    /**Mandantennummer*/
    private int mandant = 0;

    /** eindeutiger Key für Aktienregistersatz (zusammen mit mandant), der unveränderlich ist. 
     * Wird in diesem Fall bei DBAktienregisterZusatz.insert nicht neu vergeben, sondern muß bereits gefüllt sein,
     * da dieses Feld der "Foreign-Key" zu EclAktienregisterEintrag ist.
     */
    private int aktienregisterIdent = 0;

    /**Versionsnummer - manuell nicht verändern! Ausschließlich durch Verwendung in DbMeldungen*/
    private long db_version;

    /**Für Portal: Nutzungshinweise für das Aktionärsportal bestätigt
     * 0 = noch nichts bestätigt
     * 1 = bestätigt
     * 2 = zwar schon mal bestätigt, aber Hinweise haben sich geändert, dementsprechend nochmal bestätigen lassen
     */
    private int hinweisAktionaersPortalBestaetigt = 0;
    private boolean hinweisAktionaersPortalBestaetigtAnzeige = false;

    /**Für Portal: Nutzungshinweise für das HVportal bestätigt
     * 0 = noch nichts bestätigt
     * 1 = bestätigt
     * 2 = zwar schon mal bestätigt, aber Hinweise haben sich geändert, dementsprechend nochmal bestätigen lassen
     */
    private int hinweisHVPortalBestaetigt = 0;
    private boolean hinweisHVPortalBestaetigtAnzeige = false;

    /**Für Portal: Zur Email-Registrierung für E-Versand bereits aufgefordert
     * 0 = noch nicht aufgefordert
     * 1 = aufgefordert, nicht registriert
     * 2 = zwar schon mal aufgefordert (z.B. im letzten Jahr), aber jetzt nochmal auffordern
     * (Hinweis zu 2: z.B. können bei einer neuen HV alle "1er" auf "2" gesetzt werden, so dass sie nochmal aufgefordert werden.
     * Damit läßt sich also abbilden, dass sie jährlich einmal zur Registrierung aufgefordert werden)
     * 99 = bereits registriert
     */
    private int eMailRegistrierung = 0;
    private boolean eMailRegistrierungAnzeige = false;

    /**Zeitpunkt der ersten Email-Registrierung für den elektronischen Einladungsversand. Wird benötigt für Verlosung
     * Länge=19*/
    private String eMailRegistrierungErstZeitpunkt = "";

    /*TODO _Portal: was ist, wenn Passwortvergessen, obwohl noch kein eigenes Passwort vergeben?*/
    /**Für Portal: Zur PasswortÄnderung bereits aufgefordert
     * 0 = noch nicht aufgefordert
     * 1 = aufgefordert, nicht geändert
     * 2 = zwar schon mal aufgefordert (z.B. im letzten Jahr), aber jetzt nochmal auffordern
     * (Hinweis zu 2: z.B. können bei einer neuen HV alle "1er" auf "2" gesetzt werden, so dass sie nochmal aufgefordert werden.
     * Damit läßt sich also abbilden, dass sie jährlich einmal zur eigenen Paswwortvergabe aufgefordert werden)
     * 98 = Passwort-Vergessen-Anforderung ist ausstehend
     * 99 = bereits eigenes Passwort
     */
    private int eigenesPasswort = 0;
    private String neuesPasswort = "";
    private String neuesPasswortBestaetigung = "";

    /**Key aus Passwort-Vergessen-Link
     * Länge=30
     */
    private String passwortVergessenLink = "";

    /**Passwort-Vergessen-Link erzeugt zum Zeitpunkt
     * Länge=19
     */
    private String passwortVergessenZeitpunkt = "";

    /**Kommunikationssprache
     * 1 = Deutsch
     * 2 = Englisch
     * weitere auf Anfrage :-)*/
    private String kommunikationssprache = "1";

    /**EMail-Adresse für elektronischen Einladungsversand und ggf. auch sonstige Publikationsverteilungen
     * Länge = 40*/
    private String eMailFuerVersand = "";
    private String eMailFuerVersandBestaetigen = "";
    private String eMailFuerVersandAlt = ""; /*Wird nur beim "Reinkopieren" gefüllt*/

    /**Email-Adresse wurde bereits bestätigt
     * 0 = noch nicht
     * 1 = Ja*/
    private int emailBestaetigt = 0;

    /**Key aus Email-Bestätigen-Link
     * Länge=30
     */
    private String emailBestaetigenLink = "";

    /**2. EMail-Adresse für elektronischen Einladungsversand und ggf. auch sonstige Publikationsverteilungen
     * Genaue Verwendung noch nicht ganz geklärt ... aber sicherheitshalber mal eingebaut, da im ku164-Portal vorhanden
     * Länge = 40*/
    private String eMail2FuerVersand = "";
    private String eMail2FuerVersandBestaetigen = "";
    private String eMail2FuerVersandAlt = ""; /*Wird nur beim "Reinkopieren" gefüllt*/

    /**Email-Adresse wurde bereits bestätigt
     * 0 = noch nicht
     * 1 = Ja*/
    private int email2Bestaetigt = 0;

    /**Key aus Email-Bestätigen-Link
     * Länge=30
     */
    private String email2BestaetigenLink = "";

    /**Publikationen
     * Aktuell für 10 verschiedene Publikationen Platz in Datenbank. Position in Array entspricht der Publikationssnummer.
     * Der Wert jeder Position gibt wieder, ob bzw. wie diese Publikation an diesen Aktionär verschickt werden soll
     * 0 = kein Versand dieser Publikation
     * Bit 1 bis Bit 10: Zustellung über den diesem Bit zugeordneten Versandweg.*/
    private int[] publikationenZustellung = new int[10];

    /**Kontaktdaten - optional
     * Länge =40*/
    private String kontaktTelefonPrivat = "";

    /**Kontaktdaten - optional
     * Länge =40*/
    private String kontaktTelefonGeschaeftlich = "";

    /**Kontaktdaten - optional
     * Länge =40*/
    private String kontaktTelefonMobil = "";

    /**Kontaktdaten - optional
     * Länge =40*/
    private String kontaktTelefonFax = "";

    public void copyFrom(EclAktienregisterZusatz pAktienregisterZusatzAbstimmung) {
        mandant = pAktienregisterZusatzAbstimmung.mandant;
        aktienregisterIdent = pAktienregisterZusatzAbstimmung.aktienregisterIdent;
        db_version = pAktienregisterZusatzAbstimmung.db_version;
        hinweisAktionaersPortalBestaetigt = pAktienregisterZusatzAbstimmung.hinweisAktionaersPortalBestaetigt;
        if (hinweisAktionaersPortalBestaetigt == 1) {
            hinweisAktionaersPortalBestaetigtAnzeige = true;
        }
        hinweisHVPortalBestaetigt = pAktienregisterZusatzAbstimmung.hinweisHVPortalBestaetigt;
        if (hinweisHVPortalBestaetigt == 1) {
            hinweisHVPortalBestaetigtAnzeige = true;
        }
        eMailRegistrierung = pAktienregisterZusatzAbstimmung.eMailRegistrierung;
        if (eMailRegistrierung == 99) {
            eMailRegistrierungAnzeige = true;
        }
        eMailRegistrierungErstZeitpunkt = pAktienregisterZusatzAbstimmung.eMailRegistrierungErstZeitpunkt;
        eigenesPasswort = pAktienregisterZusatzAbstimmung.eigenesPasswort;
        neuesPasswort = "";
        neuesPasswortBestaetigung = "";
        passwortVergessenLink = pAktienregisterZusatzAbstimmung.passwortVergessenLink;
        passwortVergessenZeitpunkt = pAktienregisterZusatzAbstimmung.passwortVergessenZeitpunkt;
        kommunikationssprache = Integer.toString(pAktienregisterZusatzAbstimmung.kommunikationssprache);
        eMailFuerVersand = pAktienregisterZusatzAbstimmung.eMailFuerVersand;
        eMailFuerVersandAlt = pAktienregisterZusatzAbstimmung.eMailFuerVersand;
        eMailFuerVersandBestaetigen = pAktienregisterZusatzAbstimmung.eMailFuerVersand;
        emailBestaetigt = pAktienregisterZusatzAbstimmung.emailBestaetigt;
        emailBestaetigenLink = pAktienregisterZusatzAbstimmung.emailBestaetigenLink;
        eMail2FuerVersand = pAktienregisterZusatzAbstimmung.eMail2FuerVersand;
        eMail2FuerVersandAlt = pAktienregisterZusatzAbstimmung.eMail2FuerVersand;
        eMail2FuerVersandBestaetigen = pAktienregisterZusatzAbstimmung.eMail2FuerVersand;
        email2Bestaetigt = pAktienregisterZusatzAbstimmung.email2Bestaetigt;
        email2BestaetigenLink = pAktienregisterZusatzAbstimmung.email2BestaetigenLink;
        publikationenZustellung = pAktienregisterZusatzAbstimmung.publikationenZustellung;
        kontaktTelefonPrivat = pAktienregisterZusatzAbstimmung.kontaktTelefonPrivat;
        kontaktTelefonGeschaeftlich = pAktienregisterZusatzAbstimmung.kontaktTelefonGeschaeftlich;
        kontaktTelefonMobil = pAktienregisterZusatzAbstimmung.kontaktTelefonMobil;
        kontaktTelefonFax = pAktienregisterZusatzAbstimmung.kontaktTelefonFax;
    }

    /**Hinweis:
     * > hinweisAktionaersPortalBestaetigtAnzeige
     * > hinweisHVPortalBestaetigtAnzeige
     * > eMailRegistrierungAnzeige
     * > neuesPasswort
     * > neuesPasswortBestaetigung
     * werden nicht ausgewertet/übertragen - dies muß in der übergreifenden Logik getan werden! 
     */
    public void copyTo(EclAktienregisterZusatz pAktienregisterZusatzAbstimmung) {
        pAktienregisterZusatzAbstimmung.mandant = mandant;
        pAktienregisterZusatzAbstimmung.aktienregisterIdent = aktienregisterIdent;
        pAktienregisterZusatzAbstimmung.db_version = db_version;
        pAktienregisterZusatzAbstimmung.hinweisAktionaersPortalBestaetigt = hinweisAktionaersPortalBestaetigt;
        pAktienregisterZusatzAbstimmung.hinweisHVPortalBestaetigt = hinweisHVPortalBestaetigt;
        pAktienregisterZusatzAbstimmung.eMailRegistrierung = eMailRegistrierung;
        pAktienregisterZusatzAbstimmung.eMailRegistrierungErstZeitpunkt = eMailRegistrierungErstZeitpunkt;
        pAktienregisterZusatzAbstimmung.eigenesPasswort = eigenesPasswort;
        pAktienregisterZusatzAbstimmung.passwortVergessenLink = passwortVergessenLink;
        pAktienregisterZusatzAbstimmung.passwortVergessenZeitpunkt = passwortVergessenZeitpunkt;
        pAktienregisterZusatzAbstimmung.kommunikationssprache = Integer.parseInt(kommunikationssprache);
        pAktienregisterZusatzAbstimmung.eMailFuerVersand = eMailFuerVersand;
        //		pAktienregisterZusatzAbstimmung.eMailFuerVersand=eMailFuerVersandBestaetigen;
        pAktienregisterZusatzAbstimmung.emailBestaetigt = emailBestaetigt;
        pAktienregisterZusatzAbstimmung.emailBestaetigenLink = emailBestaetigenLink;
        pAktienregisterZusatzAbstimmung.eMail2FuerVersand = eMail2FuerVersand;
        //		pAktienregisterZusatzAbstimmung.eMail2FuerVersand=eMail2FuerVersandBestaetigen;
        pAktienregisterZusatzAbstimmung.email2Bestaetigt = email2Bestaetigt;
        pAktienregisterZusatzAbstimmung.email2BestaetigenLink = email2BestaetigenLink;
        pAktienregisterZusatzAbstimmung.publikationenZustellung = publikationenZustellung;
        pAktienregisterZusatzAbstimmung.kontaktTelefonPrivat = kontaktTelefonPrivat;
        pAktienregisterZusatzAbstimmung.kontaktTelefonGeschaeftlich = kontaktTelefonGeschaeftlich;
        pAktienregisterZusatzAbstimmung.kontaktTelefonMobil = kontaktTelefonMobil;
        pAktienregisterZusatzAbstimmung.kontaktTelefonFax = kontaktTelefonFax;
    }

    public void copyFromM(EclAktienregisterZusatzM pAktienregisterZusatzAbstimmung) {
        mandant = pAktienregisterZusatzAbstimmung.mandant;
        aktienregisterIdent = pAktienregisterZusatzAbstimmung.aktienregisterIdent;
        db_version = pAktienregisterZusatzAbstimmung.db_version;
        hinweisAktionaersPortalBestaetigt = pAktienregisterZusatzAbstimmung.hinweisAktionaersPortalBestaetigt;
        hinweisAktionaersPortalBestaetigtAnzeige = pAktienregisterZusatzAbstimmung.hinweisAktionaersPortalBestaetigtAnzeige;
        hinweisHVPortalBestaetigt = pAktienregisterZusatzAbstimmung.hinweisHVPortalBestaetigt;
        hinweisHVPortalBestaetigtAnzeige = pAktienregisterZusatzAbstimmung.hinweisHVPortalBestaetigtAnzeige;
        eMailRegistrierung = pAktienregisterZusatzAbstimmung.eMailRegistrierung;
        eMailRegistrierungAnzeige = pAktienregisterZusatzAbstimmung.eMailRegistrierungAnzeige;
        eMailRegistrierungErstZeitpunkt = pAktienregisterZusatzAbstimmung.eMailRegistrierungErstZeitpunkt;
        eigenesPasswort = pAktienregisterZusatzAbstimmung.eigenesPasswort;
        neuesPasswort = pAktienregisterZusatzAbstimmung.neuesPasswort;
        neuesPasswortBestaetigung = pAktienregisterZusatzAbstimmung.neuesPasswortBestaetigung;
        passwortVergessenLink = pAktienregisterZusatzAbstimmung.passwortVergessenLink;
        passwortVergessenZeitpunkt = pAktienregisterZusatzAbstimmung.passwortVergessenZeitpunkt;
        kommunikationssprache = pAktienregisterZusatzAbstimmung.kommunikationssprache;
        eMailFuerVersand = pAktienregisterZusatzAbstimmung.eMailFuerVersand;
        eMailFuerVersandAlt = pAktienregisterZusatzAbstimmung.eMailFuerVersandAlt;
        eMailFuerVersandBestaetigen = pAktienregisterZusatzAbstimmung.eMailFuerVersandBestaetigen;
        emailBestaetigt = pAktienregisterZusatzAbstimmung.emailBestaetigt;
        emailBestaetigenLink = pAktienregisterZusatzAbstimmung.emailBestaetigenLink;
        eMail2FuerVersand = pAktienregisterZusatzAbstimmung.eMail2FuerVersand;
        eMail2FuerVersandAlt = pAktienregisterZusatzAbstimmung.eMail2FuerVersandAlt;
        eMail2FuerVersandBestaetigen = pAktienregisterZusatzAbstimmung.eMail2FuerVersandBestaetigen;
        email2Bestaetigt = pAktienregisterZusatzAbstimmung.email2Bestaetigt;
        email2BestaetigenLink = pAktienregisterZusatzAbstimmung.email2BestaetigenLink;
        publikationenZustellung = pAktienregisterZusatzAbstimmung.publikationenZustellung;
        kontaktTelefonPrivat = pAktienregisterZusatzAbstimmung.kontaktTelefonPrivat;
        kontaktTelefonGeschaeftlich = pAktienregisterZusatzAbstimmung.kontaktTelefonGeschaeftlich;
        kontaktTelefonMobil = pAktienregisterZusatzAbstimmung.kontaktTelefonMobil;
        kontaktTelefonFax = pAktienregisterZusatzAbstimmung.kontaktTelefonFax;
    }

    /******************************Standard getter und setter**********************************************/
    public int getMandant() {
        return mandant;
    }

    public void setMandant(int mandant) {
        this.mandant = mandant;
    }

    public int getAktienregisterIdent() {
        return aktienregisterIdent;
    }

    public void setAktienregisterIdent(int aktienregisterIdent) {
        this.aktienregisterIdent = aktienregisterIdent;
    }

    public long getDb_version() {
        return db_version;
    }

    public void setDb_version(long db_version) {
        this.db_version = db_version;
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

    public int geteMailRegistrierung() {
        return eMailRegistrierung;
    }

    public void seteMailRegistrierung(int eMailRegistrierung) {
        this.eMailRegistrierung = eMailRegistrierung;
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

    public int[] getPublikationenZustellung() {
        return publikationenZustellung;
    }

    public void setPublikationenZustellung(int[] publikationenZustellung) {
        this.publikationenZustellung = publikationenZustellung;
    }

    public String getKontaktTelefonPrivat() {
        return kontaktTelefonPrivat;
    }

    public void setKontaktTelefonPrivat(String kontaktTelefonPrivat) {
        this.kontaktTelefonPrivat = kontaktTelefonPrivat;
    }

    public String getKontaktTelefonGeschaeftlich() {
        return kontaktTelefonGeschaeftlich;
    }

    public void setKontaktTelefonGeschaeftlich(String kontaktTelefonGeschaeftlich) {
        this.kontaktTelefonGeschaeftlich = kontaktTelefonGeschaeftlich;
    }

    public String getKontaktTelefonMobil() {
        return kontaktTelefonMobil;
    }

    public void setKontaktTelefonMobil(String kontaktTelefonMobil) {
        this.kontaktTelefonMobil = kontaktTelefonMobil;
    }

    public String getKontaktTelefonFax() {
        return kontaktTelefonFax;
    }

    public void setKontaktTelefonFax(String kontaktTelefonFax) {
        this.kontaktTelefonFax = kontaktTelefonFax;
    }

    public String getKommunikationssprache() {
        return kommunikationssprache;
    }

    public void setKommunikationssprache(String kommunikationssprache) {
        this.kommunikationssprache = kommunikationssprache;
    }

    public boolean isHinweisAktionaersPortalBestaetigtAnzeige() {
        return hinweisAktionaersPortalBestaetigtAnzeige;
    }

    public void setHinweisAktionaersPortalBestaetigtAnzeige(boolean hinweisAktionaersPortalBestaetigtAnzeige) {
        this.hinweisAktionaersPortalBestaetigtAnzeige = hinweisAktionaersPortalBestaetigtAnzeige;
    }

    public boolean isHinweisHVPortalBestaetigtAnzeige() {
        return hinweisHVPortalBestaetigtAnzeige;
    }

    public void setHinweisHVPortalBestaetigtAnzeige(boolean hinweisHVPortalBestaetigtAnzeige) {
        this.hinweisHVPortalBestaetigtAnzeige = hinweisHVPortalBestaetigtAnzeige;
    }

    public boolean iseMailRegistrierungAnzeige() {
        return eMailRegistrierungAnzeige;
    }

    public void seteMailRegistrierungAnzeige(boolean eMailRegistrierungAnzeige) {
        this.eMailRegistrierungAnzeige = eMailRegistrierungAnzeige;
    }

    public String geteMailFuerVersandBestaetigen() {
        return eMailFuerVersandBestaetigen;
    }

    public void seteMailFuerVersandBestaetigen(String eMailFuerVersandBestaetigen) {
        this.eMailFuerVersandBestaetigen = eMailFuerVersandBestaetigen;
    }

    public String geteMail2FuerVersandBestaetigen() {
        return eMail2FuerVersandBestaetigen;
    }

    public void seteMail2FuerVersandBestaetigen(String eMail2FuerVersandBestaetigen) {
        this.eMail2FuerVersandBestaetigen = eMail2FuerVersandBestaetigen;
    }

    public String getNeuesPasswort() {
        return neuesPasswort;
    }

    public void setNeuesPasswort(String neuesPasswort) {
        this.neuesPasswort = neuesPasswort;
    }

    public String getNeuesPasswortBestaetigung() {
        return neuesPasswortBestaetigung;
    }

    public void setNeuesPasswortBestaetigung(String neuesPasswortBestaetigung) {
        this.neuesPasswortBestaetigung = neuesPasswortBestaetigung;
    }

    public String geteMailRegistrierungErstZeitpunkt() {
        return eMailRegistrierungErstZeitpunkt;
    }

    public void seteMailRegistrierungErstZeitpunkt(String eMailRegistrierungErstZeitpunkt) {
        this.eMailRegistrierungErstZeitpunkt = eMailRegistrierungErstZeitpunkt;
    }

    public String geteMailFuerVersandAlt() {
        return eMailFuerVersandAlt;
    }

    public void seteMailFuerVersandAlt(String eMailFuerVersandAlt) {
        this.eMailFuerVersandAlt = eMailFuerVersandAlt;
    }

    public String geteMail2FuerVersandAlt() {
        return eMail2FuerVersandAlt;
    }

    public void seteMail2FuerVersandAlt(String eMail2FuerVersandAlt) {
        this.eMail2FuerVersandAlt = eMail2FuerVersandAlt;
    }

}
