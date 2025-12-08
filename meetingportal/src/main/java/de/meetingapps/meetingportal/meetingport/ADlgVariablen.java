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
package de.meetingapps.meetingportal.meetingport;

import java.io.Serializable;

import de.meetingapps.meetingportal.meetComEclM.EclParamM;
import de.meetingapps.meetingportal.meetComEclM.EclPortalTexteM;
import de.meetingapps.meetingportal.meetComEclM.EclTeilnehmerLoginM;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@SessionScoped
@Named
@Deprecated
public class ADlgVariablen implements Serializable {
    private static final long serialVersionUID = 6183611722011831894L;

    @Inject
    EclPortalTexteM eclPortalTexteM;
    @Inject
    EclParamM eclParamM;
    @Inject
    EclTeilnehmerLoginM eclTeilnehmerLoginM;

    private String dummy = "";

    /**true=> Webservice war Veranlasser. false=Portal war Veranlasser*/
    private boolean aufgerufenDurchWebservice = false;

    public boolean isAufgerufenDurchWebservice() {
        return aufgerufenDurchWebservice;
    }

    public void setAufgerufenDurchWebservice(boolean aufgerufenDurchWebservice) {
        this.aufgerufenDurchWebservice = aufgerufenDurchWebservice;
    }

    /******Mandante und Sprache***************************/
    //	private String sprache="";
    private String test = ""; /*1 => Testmodus ist an*/

    private int eingabeQuelle = 21;
    private String erteiltZeitpunkt = ""; /*JJJJ.MM.TT HH:MM:SS*/
    /*
     * .pErteiltZeitpunkt=aDlgVariablen.getErteiltZeitpunkt();
     */

    /*
    .pErteiltAufWeg=aDlgVariablen.getEingabeQuelle();
    */

    //	public String getSprache() {
    //		return sprache;
    //	}
    //
    //
    //	public void setSprache(String sprache) {
    //		if (sprache.compareTo("DE")!=0 && sprache.compareTo("EN")!=0){sprache="DE";}
    //		this.sprache = sprache;
    //		if (sprache.compareTo("DE")==0){aLanguage.setDE();}
    //		if (sprache.compareTo("EN")==0){aLanguage.setEN();}
    //	}

    /*******************************Fehlermeldung******************************************************/
    private String fehlerMeldung = "";
    private int fehlerNr = 1;

    public boolean pruefeFehlerMeldungVorhanden() {
        if (fehlerMeldung.isEmpty()) {
            return false;
        }
        return true;
    }

    public void clearFehlerMeldung() {
        fehlerMeldung = "";
        fehlerNr = 1;
    }

    /***************************Login-Daten**********************************************************/
    private String loginKennung = "";
    private String loginPasswort = "";

    public void clearLogin() {
        loginKennung = "";
        loginPasswort = "";
    }

    /************************Registrierungsdaten*****************************************************/

    /**Wird vor dem Aufruf von ErstRegistrierung oder Einstellungen gesetzt. In einheitlicher
     * Oberfläche (bzw. über die entsprechenden verarbeitenden Funktionen) kann dann die eine
     * oder andere Variante berücksichtigt werden (nötig, wenn einheitliches xHTML 
     * für aRegistrierung und aEinstellungen verwendet wird, was ja möglich ist).
     */
    private int erstRegistrierungOderEinstellungenAktiv = 0;

    /**Bereich für Meldungen nach dem Login; z.B. - Emailadresse noch zu bestätigen*/
    private boolean anzeigeMeldung = false; /*Registrierung+Einstellung*/
    private boolean anzeigeMeldungsText1 = false; /*Registrierung+Einstellung*/
    private boolean anzeigeMeldungsText2 = false; /*Registrierung+Einstellung*/

    /**Bereich für eMailVersand+Registrierung anzeigen - war früher mal aktiv - mittlerweile inaktiv. Nur noch zu Dokuzwecken drin,
     * bis alle Portale umgestellt sind.*/
    //	private boolean anzeigeEmailVersandRegistrierung=false;		/*Registrierung*/	
    //	private boolean ausgewaehltEmailVersandRegistrierung=false;	/*Registrierung*/

    /**Nur für Maske "Einstellungen" - Buttons für erneutes Verschicken des Email-Bestätigungslinks*/
    private boolean emailbestaetigen = false; /*Einstellung*/
    private boolean email2bestaetigen = false; /*Einstellung*/

    /**Bereich für Passwort-Eigenvergabe anzeigen*/
    //	private boolean anzeigeVergabeEigenesPasswort=false;		/*Registrierung+Einstellung*/
    private boolean ausgewaehltVergabeEigenesPasswort = false; /*Registrierung+Einstellung
                                                               	Checkbox für eigenes Passwort zu vergeben*/

    /**Passwort für "Einstellungen"*/
    private boolean passwortBereitsVergeben = false; /*Registrierung+Einstellung - für den Aktionär ist bereits ein dauerhaftes
                                                     	Passwort vergeben=true*/
    private boolean neuesPasswort = false; /*RegistrierungEinstellung - nur bei passwortBereitsVergeben==true*/

    /**Bereich für Hinweis Datenschutzerklaerung*/
    private boolean anzeigeHinweisDatenschutzerklaerung = false; /*Registrierung+Einstellung*/

    /**Bereich für Hinweis Aktionärsportal*/
    private boolean anzeigeHinweisAktionaersPortalBestaetigen = false; /*Registrierung+Einstellung*/

    /**Bereich für Hinweis HV-Portal*/
    private boolean anzeigeHinweisHVPortalBestaetigen = false; /*Registrierung+Einstellung*/

    /**Link, der in die Email-Bestätigung oder in das Email-Passwort-Vergessen mit einkopiert wird - kompletter Link, falls über Portal verschickt*/
    @Deprecated
    private String einsprungsLinkFuerEmail = "";

    /**Code, der der in die Email-Bestätigung oder in das Email-Passwort-Vergessen mit einkopiert wird - nur Code, falls über App angefordert*/
    @Deprecated
    private String einsprungsLinkNurCode = "";

    /************************Haupt-Auswahl**************************************************************
     * gibt an, welcher "Hauptstrang" des Dialoges gerade bearbeitet werden.
     * 1 = Neuanmeldung (Namensaktien) - bisher waren noch keine Anmeldungen des Aktionärs vorhanden, die ausgewaehlteAktion
     * 		wird zusammen mit der Erstanmeldung des Aktienbesitzes durchgeführt.
     * 
     * 		Wesentliche Auswahlmaske ist aAnmelden.
     * 
     * 2 = Erstanmeldung (Namensaktien) ist bereits früher erfolgt. D.h. nur noch Willenserklärungen speichern, keine
     * 		Erstanmeldung mehr durchführen.
     * 
     * 		Wesentliche Auswahlmaske ist aStatus, und dann aMenue (aMenue entspricht im wesentlichen aAnmelden, siehe oben).
     * 
     * 		Im Fall ==2 gilt: 
     * 			>in EclZugeordneteMeldungM steht (nach Durchlaufen von aStatus) die Meldung zur Verfügung, für 
     * 				die weitere Willenserklärungen bearbeitet werden sollen.
     * 			>Im Falle von Stornieren oder Ändern von Willenserklärungen steht in EclWillenserklaerungStatusM die
     * 				Willenserklärung zur Verfügung, die bearbeitet werden soll.
     * 
     * In Vorbereitung: Inhaberaktien*/
    private String ausgewaehlteHauptAktion = "";

    /****************************Ausgewählte Aktion******************************************************
     * Gibt an, welche "Detail"-Aktion gerade ausgewählt ist und ausgeführt ist.
     * 0 bzw. leer = keine Aktion ausgewählt
     * 1 = Eine Eintrittskarte auf meinen Namen ausstellen
     * 2 = zwei Eintrittskarten auf unsere Namen ausstellen (nur bei Personengemeinschaften)
     * 3 = eine Eintrittskarte auf einen Bevollmächtigten ausstellen
     * siehe auch 28! 
     * 4 = Vollmacht und Weisung an Stimmrechtsvertreter erteilen
     * 5 = Per Briefwahl abstimmen
     * 6 = Vollmacht (und Weisung) an KI/AV - noch nicht näher qualifiziert
     * 7 = nur Vollmacht an KI/AV
     * 8 = Vollmacht und Weisung (dediziert) an KI/AV
     * 9 = Vollmacht und Weisung gemäß Vorschlag an KI/AV
     * 10 = Vollmacht/Weisung SRV ändern
     * 11 = Briefwahl ändern
     * 12 = KIAV Weisung ändern
     * 13 = Vollmacht/Weisung SRV stornieren
     * 14 = Briefwahl stornieren
     * 15 = KIAV stornieren
     * 16 = Gastkarte stornieren
     * 17 = Aktionärs-Eintrittskarte stornieren
     * 18 = Aktionärs-Eintrittskarte mit Vollmacht stornieren
     * 19 = Vollmacht Dritte stornieren
     * 20 = Gastkarte anlegen
     * 21 = Vollmacht an Dritte - aufruf aus aNeueWillenserklaerung (siehe auch 29!)
     * 25 = zusätzliche Willenserklärung
     * 26 = Detailanzeige Eintrittskarte
     * 27 = Detailanzeige Gastkarte
     * 28 = zwei Eintrittskarten mit oder ohne Vollmacht (bei allen Anmeldungen)
     * 29 = Vollmacht an Dritte - Aufruf aus aStatus (siehe auch 21!)
     * 30 = zwei Eintrittskarten auch mich selbst (bei allen Anmeldungen)
     * 
     * 31 = Vollmacht (und Weisung) Dauervollmacht (noch nicht näher qualifiziert)
     * 32 = nur Vollmacht an Dauervollmacht
     * 33 = Vollmacht und Weisung (dediziert) an Dauervollmacht
     * 34 = Vollmacht und Weisung gemäß Vorschlag an Dauervollmacht
     * 
     * 35 = Vollmacht (und Weisung) Organisatorisch (noch nicht näher qualifiziert)
     * 36 = nur Vollmacht an Organisatorisch
     * 37 = Vollmacht und Weisung (dediziert) an Organisatorisch
     * 38 = Vollmacht und Weisung gemäß Vorschlag an Organisatorisch
     * 
     * 39 = Dauervollmacht ändern
     * 40 = Organisatorisch ändern
     * 
     * 41 = Dauervollmacht stornieren
     * 42 = Organisatorisch stornieren
     * */
    private String ausgewaehlteAktion = "";

    /***********************Auswahl-Maske************************************/
    /**Ansprung-Maske nach erfolgter Weisungserteilung. Verwendet für Sonderabläufe z.B. ku178.
     * Wenn leer, dann "normaler" Ablauf (=> aStatus).
     * Ansonsten wird anstelle von aStatus diese Maske angesprungen
     */
    private String auswahlMaske = "";

    /***************************Zusätzliche Gastkarte******************************************************/
    private boolean gastKarte = false;

    /*******************Virtuelle HV********************************************************************/

    /**Übergreifend, unabhängig ob überhaupt aktiv oder gerade angeboten:
     * werden diese Funktionen für den jeweiligen Benutzer angeboten?
     * 
     * Wird in aFunktionen gesetzt, und in aStatus ausgewertet
     */

    private int mitteilungenAngebotenLogin = 1;
    private int streamAngebotenLogin = 1;
    private int fragenAngebotenLogin = 1;
    private int einstellungenAngebotenLogin = 1;
    private int teilnehmerverzAngebotenLogin = 1;
    private int abstimmungsergAngebotenLogin = 1;

    /**1 => normaler Aktionärslogin
     * sonst: Gast oder Insti (Aktien nicht anzeigen!)
     * 
     * Wird nach dem Login gesetzt
     */
    private int loginArt = 1;

    /**********************Controls für Maske Eintrittskarte************************************************/
    /*****Erste Eintrittskarte*****/
    private String eintrittskarteVersandart = "";
    /**0 = keinerlei Weiterverarbeitung erforderlich (alles bereits vom Programm adhoc erledigt
     * 1 = Aufnahme in Sammelbatch, an Adresse im Aktienregister - bei nächstem Drucklauf ausdrucken und versenden
     * 2 = Aufnahme in Sammelbatch, an Versandadresse - bei nächstem Drucklauf ausdrucken und versenden
     * 3 = Online-Ausdruck (im Portal) erfolgt
     * 4 = Versand per Email (im Portal) erfolgt
     * 5 = automatische Aufnahme in App
     * 6 = selbe Versandadresse wie zeitgleich ausgestellte Eintrittskarte (nur Gastkarte!)
     * 7 = Aufnahme in Sammelbatch, an Adresse im Aktienregister - bei nächstem Drucklauf ausdrucken und versenden (nur Gastkarte!)
     * 
     * Siehe KonstWillenserklaerungVersandartEK
     */

    private int eintrittskartePdfNr = 0;
    /**Dateinummer, unter der das PDF für Emailversand oder Selbstdruck abgelegt wurde*/

    private String zutrittsIdent = "";
    private String zutrittsIdentNeben = "";
    /**Ausgestellte ZutrittsIdent*/

    private String eintrittskarteEmail = "";
    private String eintrittskarteEmailBestaetigen = "";

    private String eintrittskarteAbweichendeAdresse1 = "";
    private String eintrittskarteAbweichendeAdresse2 = "";
    private String eintrittskarteAbweichendeAdresse3 = "";
    private String eintrittskarteAbweichendeAdresse4 = "";
    private String eintrittskarteAbweichendeAdresse5 = "";

    /**Vollmacht*/
    private String vollmachtName = "";
    private String vollmachtVorname = "";
    private String vollmachtOrt = "";

    private String zielOeffentlicheID = "";
    private int personNatJurOeffentlicheID = 0;

    /**Falls True, dann wird die EK nicht versandt, sondern intern übermittelt*/
    private boolean ueberOeffentlicheID = false;

    /**Verwendung als reines "Render"-Feld (für zwei Eintrittskarten) mit/ohne Vollmacht*/
    private boolean vollmachtEingeben = false;

    /*******Zusatzfelder für EintrittskarteDetails*******/
    /**=1 => Eintrittskarte wurde bereits gedruckt*/
    private int eintrittskarteWurdeGedruckt = 0;
    /**Datum, zu dem Eintrittskarte gedruckt wurde*/
    private String eintrittskarteDruckDatum = "";

    /*****Zweite Eintrittskarte*****/
    private String eintrittskarteVersandart2 = "";
    /**0 = keinerlei Weiterverarbeitung erforderlich (alles bereits vom Programm adhoc erledigt
     * 1 = Aufnahme in Sammelbatch, an Adresse im Aktienregister - bei nächstem Drucklauf ausdrucken und versenden
     * 2 = Aufnahme in Sammelbatch, an Versandadresse - bei nächstem Drucklauf ausdrucken und versenden
     * 3 = Online-Ausdruck (im Portal) erfolgt
     * 4 = Versand per Email (im Portal) erfolgt
     * 5 = automatische Aufnahme in App
     * 6 = selbe Versandadresse wie zeitgleich ausgestellte Eintrittskarte (nur Gastkarte!)
     */

    /**Dateinummer, unter der das PDF für Emailversand oder Selbstdruck abgelegt wurde*/
    private int eintrittskartePdfNr2 = 0;

    private String zutrittsIdent2 = "";
    private String zutrittsIdentNeben2 = "";
    /**Ausgestellte ZutrittsIdent*/

    private String eintrittskarteEmail2 = "";
    private String eintrittskarteEmail2Bestaetigen = "";

    private String eintrittskarteAbweichendeAdresse12 = "";
    private String eintrittskarteAbweichendeAdresse22 = "";
    private String eintrittskarteAbweichendeAdresse32 = "";
    private String eintrittskarteAbweichendeAdresse42 = "";
    private String eintrittskarteAbweichendeAdresse52 = "";

    /**Vollmacht*/

    private String vollmachtName2 = "";
    private String vollmachtVorname2 = "";
    private String vollmachtOrt2 = "";

    /**Verwendung als reines "Render"-Feld (für zwei Eintrittskarten) mit/ohne Vollmacht*/
    private boolean vollmachtEingeben2 = false;

    /***************************Gastkarte******************************/
    private String gastkarteName = "";
    private String gastkarteVorname = "";
    private String gastkarteOrt = "";
    private String gastkarteVersandart = "";
    /**Falls !=0, dann wird eine bestehende persNatJur verwendet, die name-etc-Felder werden dann ignoriert*/
    private int gastkarteNrPersNatJur = 0;
    /**Dateinummer, unter der das PDF für Emailversand oder Selbstdruck abgelegt wurde*/
    private int gastkartePdfNr = 0;
    private String gastkarteEmail = "";
    private String gastkarteEmailBestaetigen = "";
    private String gastkarteAbweichendeAdresse1 = "";
    private String gastkarteAbweichendeAdresse2 = "";
    private String gastkarteAbweichendeAdresse3 = "";
    private String gastkarteAbweichendeAdresse4 = "";
    private String gastkarteAbweichendeAdresse5 = "";
    /**Wird gesetzt nach dem Anlegen einer neuen Gastkarte*/
    private int gastkarteMeldeIdent = 0;
    private String gastkartenZutrittsIdent = "";
    private String gastkartenZutrittsIdentNeben = "";

    /************************Gastkartenausstellung**********************/
    private boolean gruppenausstellung = false;
    private int identMasterGast = 0;
    @Deprecated
    private String gastkartePdfNrS = ""; /*Zusammenführen irgendwann mit gastkartePdfNr!*/

    /**1 = Versand per Email erfolgt
     * 2 = Sofortdruck
     * 3 = Stapeldruck
     */
    private String ausstellungsart = "";

    /*********************Für Eintrittskarten-Status**********************/

    /** 	1	= Eintrittskarte
     * 	2	= Eintrittskarte mit Vollmacht
     * 	3	= Gastkarte*/
    private int artEintrittskarte = 0;

    /*****************Weisungen****************************/
    private boolean bestaetigtDassBerechtigt = false;

    /**************Für Weisungsänderung******************************/
    /**Meldungsident des Aktionärs, für den gerade die Weisung geändert wird*/
    private int meldungsIdent = 0;

    /**WillenserklärungIdent der ursprüngliche Vollmacht/Weisung*/
    private int willenserklaerungIdent = 0;

    /**SammelkartenIdent, für die die ursprüngliche Vollmacht/Weisung erteilt wurde*/
    private int sammelIdent = 0;

    /**Ident der ursprünglichen Weisung*/
    private int weisungsIdent = 0;

    /**Siehe EclWillenserklaerungStatusM:
     * =1 => dedizierte vorhanden
     * =2 => gemäß Vorschlag vorhanden
     * =3 => nur freie
     */
    private int weisungenSind = 0;

    /*********************Für Eintrittskartenstornierung************************/
    /**Klasse - 0 = Aktionär, 1=Gast*/
    private int meldungsKlasse = 0;

    /**********************Div. Return-Werte, insbesondere auch für WAServices / WAIntern*************/

    /**WillenserkärungsIdent für die durchgeführte Willenserklärung (also z.B. Weisung, EK etc.)
     * Wichtig: wenn 2 EKs ausgestellt werden, dann werden diese Variablen nicht korrekt / vollständig gefüllt - 
     * Verwendung dafür dann nicht sinnvoll!*/
    private int rcWillenserklaerungIdentAusgefuehrt = 0;

    /**WillenserkärungsIdent für die durchgeführte Zweit-Willenserklärung (also z.B. VollmachtDritte zu EK)*/
    private int rcWillenserklaerungIdentAusgefuehrtZweit = 0;

    public void clearRCWillenserklaerung() {
        rcWillenserklaerungIdentAusgefuehrt = 0;
        rcWillenserklaerungIdentAusgefuehrtZweit = 0;
    }

    /**************Setzt alle Variablen innerhalb eines Dialogs (also nicht auswahl etc.) auf Default****/
    public void clearDlg() {
        gastKarte = false;
        clearDlgOhneGastkarteAnforderung();
    }

    public void clearDlgOhneGastkarteAnforderung() {
        anzeigeMeldung = false;
        anzeigeMeldungsText1 = false;
        anzeigeMeldungsText2 = false;
        //		anzeigeEmailVersandRegistrierung=false;
        //		ausgewaehltEmailVersandRegistrierung=false;
        emailbestaetigen = false;
        email2bestaetigen = false;
        //		anzeigeVergabeEigenesPasswort=false;
        ausgewaehltVergabeEigenesPasswort = false;
        passwortBereitsVergeben = false;
        neuesPasswort = false;
        anzeigeHinweisDatenschutzerklaerung = false;

        eintrittskarteVersandart = "";
        eintrittskartePdfNr = 0;
        eintrittskarteEmail = "";
        eintrittskarteEmailBestaetigen = "";
        eintrittskarteAbweichendeAdresse1 = "";
        eintrittskarteAbweichendeAdresse2 = "";
        eintrittskarteAbweichendeAdresse3 = "";
        eintrittskarteAbweichendeAdresse4 = "";
        eintrittskarteAbweichendeAdresse5 = "";

        vollmachtName = "";
        vollmachtVorname = "";
        vollmachtOrt = "";
        vollmachtEingeben = false;

        zielOeffentlicheID = "";
        personNatJurOeffentlicheID = 0;

        ueberOeffentlicheID = false;

        eintrittskarteVersandart2 = "";
        eintrittskartePdfNr2 = 0;
        eintrittskarteEmail2 = "";
        eintrittskarteEmail2Bestaetigen = "";
        eintrittskarteAbweichendeAdresse12 = "";
        eintrittskarteAbweichendeAdresse22 = "";
        eintrittskarteAbweichendeAdresse32 = "";
        eintrittskarteAbweichendeAdresse42 = "";
        eintrittskarteAbweichendeAdresse52 = "";
        vollmachtName2 = "";
        vollmachtVorname2 = "";
        vollmachtOrt2 = "";
        vollmachtEingeben2 = false;

        gastkarteName = "";
        gastkarteVorname = "";
        gastkarteOrt = "";
        gastkarteVersandart = "";
        gastkarteNrPersNatJur = 0;
        gastkartePdfNr = 0;
        gastkarteEmail = "";
        gastkarteEmailBestaetigen = "";
        gastkarteAbweichendeAdresse1 = "";
        gastkarteAbweichendeAdresse2 = "";
        gastkarteAbweichendeAdresse3 = "";
        gastkarteAbweichendeAdresse4 = "";
        gastkarteAbweichendeAdresse5 = "";
        gastkarteMeldeIdent = 0;

        gruppenausstellung = false;
        identMasterGast = 0;
        gastkartePdfNrS = "";
        ausstellungsart = "";

        bestaetigtDassBerechtigt = false;

        artEintrittskarte = 0;

    }

    /**************Clear nach Login - Ablauf*************/
    public void clearNachLogin() {
        auswahlMaske = "";
    }

    /*******************Div. Funktionen zum Rendern************************************************/

    /*++++++++++++++++++Registrierung/Einstellungen+++++++++++++++*/

    /**elektronischer Einladungsversand wird vom Emittenten angeboten und ist
     * für die Aktionärsklasse auch zulässig
     */
    public boolean renderElekEinladungsversandAngebotenUndZulaessig() {
        if (eclParamM.getRegistrierungFuerEmailVersandMoeglich() == 1
                && eclTeilnehmerLoginM.isDauerhafteRegistrierungUnzulaessig() == false) {
            return true;
        } else {
            return false;
        }
    }

    /**elektronischer Einladungsversand wird vom Emittenten angeboten aber ist
     * für die Aktionärsklasse nicht zulässig
     */
    public boolean renderElekEinladungsversandAngebotenAberNichtZulaessig() {
        if (eclParamM.getRegistrierungFuerEmailVersandMoeglich() == 1
                && eclTeilnehmerLoginM.isDauerhafteRegistrierungUnzulaessig() == true) {
            return true;
        } else {
            return false;
        }
    }

    /**Dauerhafte Passwortvergabe wird vom Emittenten angeboten und ist
     * für die Aktionärsklasse auch zulässig
     */
    public boolean renderDauerhaftesPasswortAngebotenUndZulaessig() {
        if ((eclParamM.getDauerhaftesPasswortMoeglich() == 1 || eclParamM.getDauerhaftesPasswortMoeglich() == 2)
                && eclTeilnehmerLoginM.isDauerhafteRegistrierungUnzulaessig() == false) {
            return true;
        } else {
            return false;
        }
    }

    /**Dauerhafte Passwortvergabe wird vom Emittenten angeboten aber ist
     * für die Aktionärsklasse nicht zulässig
     */
    public boolean renderDauerhaftesPasswortAngebotenAberNichtZulaessig() {
        if ((eclParamM.getDauerhaftesPasswortMoeglich() == 1 || eclParamM.getDauerhaftesPasswortMoeglich() == 2)
                && eclTeilnehmerLoginM.isDauerhafteRegistrierungUnzulaessig() == true) {
            return true;
        } else {
            return false;
        }
    }

    /**E-Mail-Hinterlegung wird angeboten (nur falls dauerhaftes Passwort oder
     * Elektronischer Einladungsversand angeboten und möglich)
     */
    public boolean renderEMailHinterlegungAngebotenUndZulaessig() {
        return (renderElekEinladungsversandAngebotenUndZulaessig() || renderDauerhaftesPasswortAngebotenUndZulaessig()
                || eclParamM.getParam().paramPortal.emailNurBeiEVersandOderPasswort == 0);
    }

    /*+++++++++Weisungsabgabe+++++++++++++++++*/
    /**Haken "Ich bestätige, dass ich zur Abgabe der Willenserklärung berechtigt bin"*/
    public boolean renderBerechtigtZurAbgabeBeiErteilen() {
        if (eclParamM.isBestaetigenDialog()) {
            return false;
        }
        return renderBerechtigtZurAbgabeBeiBestaetigung();
    }

    /**Haken "Ich bestätige, dass ich zur Abgabe der Willenserklärung berechtigt bin"*/
    public boolean renderBerechtigtZurAbgabeBeiBestaetigung() {
        if ((eclParamM.isCheckboxBeiSRV() && (getAusgewaehlteAktion().equals("4")
                || getAusgewaehlteAktion().equals("10") || getAusgewaehlteAktion().equals("13")))
                || (eclParamM.isCheckboxBeiBriefwahl() && (getAusgewaehlteAktion().equals("5")
                        || getAusgewaehlteAktion().equals("11") || getAusgewaehlteAktion().equals("14")))
                || (eclParamM.isCheckboxBeiKIAV() && (getAusgewaehlteAktion().equals("6")
                        || getAusgewaehlteAktion().equals("12") || getAusgewaehlteAktion().equals("15")))

        ) {
            return true;
        }
        return false;
    }

    /*************Div. Funktionen zum Anzeigen von Texten*****************/
    /**Haken "Ich bestätige, dass ich zur Abgabe der Willenserklärung berechtigt bin"*/
    public String textBerechtigtZurAbgabe() {
        String hString = "";
        switch (getAusgewaehlteAktion()) {
        case "4":
            hString = eclPortalTexteM.holeText("607");
            break;
        case "5":
            hString = eclPortalTexteM.holeText("608");
            break;
        case "6":
            hString = eclPortalTexteM.holeText("609");
            break;
        case "10":
            hString = eclPortalTexteM.holeText("610");
            break;
        case "11":
            hString = eclPortalTexteM.holeText("611");
            break;
        case "12":
            hString = eclPortalTexteM.holeText("612");
            break;
        case "13":
            hString = eclPortalTexteM.holeText("613");
            break;
        case "14":
            hString = eclPortalTexteM.holeText("614");
            break;
        case "15":
            hString = eclPortalTexteM.holeText("615");
            break;
        }
        return hString;
    }

    public String textBerechtigtZurAbgabeBeiBestaetigung() {
        String hString = "";
        switch (getAusgewaehlteAktion()) {
        case "4":
            hString = eclPortalTexteM.holeText("881");
            break;
        case "5":
            hString = eclPortalTexteM.holeText("882");
            break;
        case "6":
            hString = eclPortalTexteM.holeText("883");
            break;
        }
        return hString;
    }

    /*********************Ab Hier Standard-Getter und -Setter*****************************************/

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

    public String getAusgewaehlteAktion() {
        return ausgewaehlteAktion;
    }

    public void setAusgewaehlteAktion(String ausgewaehlteAktion) {
        this.ausgewaehlteAktion = ausgewaehlteAktion;
    }

    public boolean isGastKarte() {
        return gastKarte;
    }

    public void setGastKarte(boolean gastKarte) {
        this.gastKarte = gastKarte;
    }

    public String getEintrittskarteVersandart() {
        return eintrittskarteVersandart;
    }

    public void setEintrittskarteVersandart(String eintrittskarteVersandart) {
        this.eintrittskarteVersandart = eintrittskarteVersandart;
    }

    public int getEintrittskartePdfNr() {
        return eintrittskartePdfNr;
    }

    public void setEintrittskartePdfNr(int eintrittskartePdfNr) {
        this.eintrittskartePdfNr = eintrittskartePdfNr;
    }

    public String getEintrittskarteEmail() {
        return eintrittskarteEmail;
    }

    public void setEintrittskarteEmail(String eintrittskarteEmail) {
        this.eintrittskarteEmail = eintrittskarteEmail;
    }

    public String getEintrittskarteAbweichendeAdresse1() {
        return eintrittskarteAbweichendeAdresse1;
    }

    public void setEintrittskarteAbweichendeAdresse1(String eintrittskarteAbweichendeAdresse1) {
        this.eintrittskarteAbweichendeAdresse1 = eintrittskarteAbweichendeAdresse1;
    }

    public String getEintrittskarteAbweichendeAdresse2() {
        return eintrittskarteAbweichendeAdresse2;
    }

    public void setEintrittskarteAbweichendeAdresse2(String eintrittskarteAbweichendeAdresse2) {
        this.eintrittskarteAbweichendeAdresse2 = eintrittskarteAbweichendeAdresse2;
    }

    public String getEintrittskarteAbweichendeAdresse3() {
        return eintrittskarteAbweichendeAdresse3;
    }

    public void setEintrittskarteAbweichendeAdresse3(String eintrittskarteAbweichendeAdresse3) {
        this.eintrittskarteAbweichendeAdresse3 = eintrittskarteAbweichendeAdresse3;
    }

    public String getEintrittskarteAbweichendeAdresse4() {
        return eintrittskarteAbweichendeAdresse4;
    }

    public void setEintrittskarteAbweichendeAdresse4(String eintrittskarteAbweichendeAdresse4) {
        this.eintrittskarteAbweichendeAdresse4 = eintrittskarteAbweichendeAdresse4;
    }

    public String getEintrittskarteAbweichendeAdresse5() {
        return eintrittskarteAbweichendeAdresse5;
    }

    public void setEintrittskarteAbweichendeAdresse5(String eintrittskarteAbweichendeAdresse5) {
        this.eintrittskarteAbweichendeAdresse5 = eintrittskarteAbweichendeAdresse5;
    }

    public String getVollmachtName() {
        return vollmachtName;
    }

    public void setVollmachtName(String vollmachtName) {
        this.vollmachtName = vollmachtName;
    }

    public String getVollmachtVorname() {
        return vollmachtVorname;
    }

    public void setVollmachtVorname(String vollmachtVorname) {
        this.vollmachtVorname = vollmachtVorname;
    }

    public String getVollmachtOrt() {
        return vollmachtOrt;
    }

    public void setVollmachtOrt(String vollmachtOrt) {
        this.vollmachtOrt = vollmachtOrt;
    }

    public String getEintrittskarteVersandart2() {
        return eintrittskarteVersandart2;
    }

    public void setEintrittskarteVersandart2(String eintrittskarteVersandart2) {
        this.eintrittskarteVersandart2 = eintrittskarteVersandart2;
    }

    public int getEintrittskartePdfNr2() {
        return eintrittskartePdfNr2;
    }

    public void setEintrittskartePdfNr2(int eintrittskartePdfNr2) {
        this.eintrittskartePdfNr2 = eintrittskartePdfNr2;
    }

    public String getEintrittskarteEmail2() {
        return eintrittskarteEmail2;
    }

    public void setEintrittskarteEmail2(String eintrittskarteEmail2) {
        this.eintrittskarteEmail2 = eintrittskarteEmail2;
    }

    public String getEintrittskarteAbweichendeAdresse12() {
        return eintrittskarteAbweichendeAdresse12;
    }

    public void setEintrittskarteAbweichendeAdresse12(String eintrittskarteAbweichendeAdresse12) {
        this.eintrittskarteAbweichendeAdresse12 = eintrittskarteAbweichendeAdresse12;
    }

    public String getEintrittskarteAbweichendeAdresse22() {
        return eintrittskarteAbweichendeAdresse22;
    }

    public void setEintrittskarteAbweichendeAdresse22(String eintrittskarteAbweichendeAdresse22) {
        this.eintrittskarteAbweichendeAdresse22 = eintrittskarteAbweichendeAdresse22;
    }

    public String getEintrittskarteAbweichendeAdresse32() {
        return eintrittskarteAbweichendeAdresse32;
    }

    public void setEintrittskarteAbweichendeAdresse32(String eintrittskarteAbweichendeAdresse32) {
        this.eintrittskarteAbweichendeAdresse32 = eintrittskarteAbweichendeAdresse32;
    }

    public String getEintrittskarteAbweichendeAdresse42() {
        return eintrittskarteAbweichendeAdresse42;
    }

    public void setEintrittskarteAbweichendeAdresse42(String eintrittskarteAbweichendeAdresse42) {
        this.eintrittskarteAbweichendeAdresse42 = eintrittskarteAbweichendeAdresse42;
    }

    public String getEintrittskarteAbweichendeAdresse52() {
        return eintrittskarteAbweichendeAdresse52;
    }

    public void setEintrittskarteAbweichendeAdresse52(String eintrittskarteAbweichendeAdresse52) {
        this.eintrittskarteAbweichendeAdresse52 = eintrittskarteAbweichendeAdresse52;
    }

    public String getVollmachtName2() {
        return vollmachtName2;
    }

    public void setVollmachtName2(String vollmachtName2) {
        this.vollmachtName2 = vollmachtName2;
    }

    public String getVollmachtVorname2() {
        return vollmachtVorname2;
    }

    public void setVollmachtVorname2(String vollmachtVorname2) {
        this.vollmachtVorname2 = vollmachtVorname2;
    }

    public String getVollmachtOrt2() {
        return vollmachtOrt2;
    }

    public void setVollmachtOrt2(String vollmachtOrt2) {
        this.vollmachtOrt2 = vollmachtOrt2;
    }

    public String getGastkarteName() {
        return gastkarteName;
    }

    public void setGastkarteName(String gastkarteName) {
        this.gastkarteName = gastkarteName;
    }

    public String getGastkarteVorname() {
        return gastkarteVorname;
    }

    public void setGastkarteVorname(String gastkarteVorname) {
        this.gastkarteVorname = gastkarteVorname;
    }

    public String getGastkarteOrt() {
        return gastkarteOrt;
    }

    public void setGastkarteOrt(String gastkarteOrt) {
        this.gastkarteOrt = gastkarteOrt;
    }

    public String getGastkarteVersandart() {
        return gastkarteVersandart;
    }

    public void setGastkarteVersandart(String gastkarteVersandart) {
        this.gastkarteVersandart = gastkarteVersandart;
    }

    public int getGastkartePdfNr() {
        return gastkartePdfNr;
    }

    public void setGastkartePdfNr(int gastkartePdfNr) {
        this.gastkartePdfNr = gastkartePdfNr;
    }

    public String getGastkarteEmail() {
        return gastkarteEmail;
    }

    public void setGastkarteEmail(String gastkarteEmail) {
        this.gastkarteEmail = gastkarteEmail;
    }

    public String getGastkarteAbweichendeAdresse1() {
        return gastkarteAbweichendeAdresse1;
    }

    public void setGastkarteAbweichendeAdresse1(String gastkarteAbweichendeAdresse1) {
        this.gastkarteAbweichendeAdresse1 = gastkarteAbweichendeAdresse1;
    }

    public String getGastkarteAbweichendeAdresse2() {
        return gastkarteAbweichendeAdresse2;
    }

    public void setGastkarteAbweichendeAdresse2(String gastkarteAbweichendeAdresse2) {
        this.gastkarteAbweichendeAdresse2 = gastkarteAbweichendeAdresse2;
    }

    public String getGastkarteAbweichendeAdresse3() {
        return gastkarteAbweichendeAdresse3;
    }

    public void setGastkarteAbweichendeAdresse3(String gastkarteAbweichendeAdresse3) {
        this.gastkarteAbweichendeAdresse3 = gastkarteAbweichendeAdresse3;
    }

    public String getGastkarteAbweichendeAdresse4() {
        return gastkarteAbweichendeAdresse4;
    }

    public void setGastkarteAbweichendeAdresse4(String gastkarteAbweichendeAdresse4) {
        this.gastkarteAbweichendeAdresse4 = gastkarteAbweichendeAdresse4;
    }

    public String getGastkarteAbweichendeAdresse5() {
        return gastkarteAbweichendeAdresse5;
    }

    public void setGastkarteAbweichendeAdresse5(String gastkarteAbweichendeAdresse5) {
        this.gastkarteAbweichendeAdresse5 = gastkarteAbweichendeAdresse5;
    }

    public String getFehlerMeldung() {
        return fehlerMeldung;
    }

    public void setFehlerMeldung(String fehlerMeldung) {
        this.fehlerMeldung = fehlerMeldung;
    }

    public boolean isGruppenausstellung() {
        return gruppenausstellung;
    }

    public void setGruppenausstellung(boolean gruppenausstellung) {
        this.gruppenausstellung = gruppenausstellung;
    }

    public int getIdentMasterGast() {
        return identMasterGast;
    }

    public void setIdentMasterGast(int identMasterGast) {
        this.identMasterGast = identMasterGast;
    }

    public String getGastkartePdfNrS() {
        return gastkartePdfNrS;
    }

    public void setGastkartePdfNrS(String gastkartePdfNrS) {
        this.gastkartePdfNrS = gastkartePdfNrS;
    }

    public String getAusstellungsart() {
        return ausstellungsart;
    }

    public void setAusstellungsart(String ausstellungsart) {
        this.ausstellungsart = ausstellungsart;
    }

    public int getMeldungsIdent() {
        return meldungsIdent;
    }

    public void setMeldungsIdent(int meldungsIdent) {
        this.meldungsIdent = meldungsIdent;
    }

    public int getSammelIdent() {
        return sammelIdent;
    }

    public void setSammelIdent(int sammelIdent) {
        this.sammelIdent = sammelIdent;
    }

    public int getWillenserklaerungIdent() {
        return willenserklaerungIdent;
    }

    public void setWillenserklaerungIdent(int willenserklaerungIdent) {
        this.willenserklaerungIdent = willenserklaerungIdent;
    }

    public int getWeisungsIdent() {
        return weisungsIdent;
    }

    public void setWeisungsIdent(int weisungsIdent) {
        this.weisungsIdent = weisungsIdent;
    }

    public int getWeisungenSind() {
        return weisungenSind;
    }

    public void setWeisungenSind(int weisungenSind) {
        this.weisungenSind = weisungenSind;
    }

    public int getMeldungsKlasse() {
        return meldungsKlasse;
    }

    public void setMeldungsKlasse(int meldungsKlasse) {
        this.meldungsKlasse = meldungsKlasse;
    }

    public String getAusgewaehlteHauptAktion() {
        return ausgewaehlteHauptAktion;
    }

    public void setAusgewaehlteHauptAktion(String ausgewaehlteHauptAktion) {
        this.ausgewaehlteHauptAktion = ausgewaehlteHauptAktion;
    }

    public int getArtEintrittskarte() {
        return artEintrittskarte;
    }

    public void setArtEintrittskarte(int artEintrittskarte) {
        this.artEintrittskarte = artEintrittskarte;
    }

    public String getZielOeffentlicheID() {
        return zielOeffentlicheID;
    }

    public void setZielOeffentlicheID(String zielOeffentlicheID) {
        this.zielOeffentlicheID = zielOeffentlicheID;
    }

    public boolean isUeberOeffentlicheID() {
        return ueberOeffentlicheID;
    }

    public void setUeberOeffentlicheID(boolean ueberOeffentlicheID) {
        this.ueberOeffentlicheID = ueberOeffentlicheID;
    }

    public int getPersonNatJurOeffentlicheID() {
        return personNatJurOeffentlicheID;
    }

    public void setPersonNatJurOeffentlicheID(int personNatJurOeffentlicheID) {
        this.personNatJurOeffentlicheID = personNatJurOeffentlicheID;
    }

    public int getFehlerNr() {
        return fehlerNr;
    }

    public void setFehlerNr(int fehlerNr) {
        this.fehlerNr = fehlerNr;
    }

    public int getGastkarteNrPersNatJur() {
        return gastkarteNrPersNatJur;
    }

    public void setGastkarteNrPersNatJur(int gastkarteNrPersNatJur) {
        this.gastkarteNrPersNatJur = gastkarteNrPersNatJur;
    }

    public int getGastkarteMeldeIdent() {
        return gastkarteMeldeIdent;
    }

    public void setGastkarteMeldeIdent(int gastkarteMeldeIdent) {
        this.gastkarteMeldeIdent = gastkarteMeldeIdent;
    }

    public boolean isAnzeigeMeldung() {
        return anzeigeMeldung;
    }

    public void setAnzeigeMeldung(boolean anzeigeMeldung) {
        this.anzeigeMeldung = anzeigeMeldung;
    }

    //	public boolean isAnzeigeEmailVersandRegistrierung() {
    //		return anzeigeEmailVersandRegistrierung;
    //	}
    //	public void setAnzeigeEmailVersandRegistrierung(boolean anzeigeEmailVersandRegistrierung) {
    //		this.anzeigeEmailVersandRegistrierung = anzeigeEmailVersandRegistrierung;
    //	}
    //	public boolean isAnzeigeVergabeEigenesPasswort() {
    //		return anzeigeVergabeEigenesPasswort;
    //	}
    //	public void setAnzeigeVergabeEigenesPasswort(boolean anzeigeVergabeEigenesPasswort) {
    //		this.anzeigeVergabeEigenesPasswort = anzeigeVergabeEigenesPasswort;
    //	}
    public boolean isAnzeigeHinweisAktionaersPortalBestaetigen() {
        return anzeigeHinweisAktionaersPortalBestaetigen;
    }

    public void setAnzeigeHinweisAktionaersPortalBestaetigen(boolean anzeigeHinweisAktionaersPortalBestaetigen) {
        this.anzeigeHinweisAktionaersPortalBestaetigen = anzeigeHinweisAktionaersPortalBestaetigen;
    }

    public boolean isAnzeigeHinweisHVPortalBestaetigen() {
        return anzeigeHinweisHVPortalBestaetigen;
    }

    public void setAnzeigeHinweisHVPortalBestaetigen(boolean anzeigeHinweisHVPortalBestaetigen) {
        this.anzeigeHinweisHVPortalBestaetigen = anzeigeHinweisHVPortalBestaetigen;
    }

    //	public boolean isAusgewaehltEmailVersandRegistrierung() {
    //		return ausgewaehltEmailVersandRegistrierung;
    //	}
    //	public void setAusgewaehltEmailVersandRegistrierung(boolean ausgewaehltEmailVersandRegistrierung) {
    //		this.ausgewaehltEmailVersandRegistrierung = ausgewaehltEmailVersandRegistrierung;
    //	}
    public boolean isAusgewaehltVergabeEigenesPasswort() {
        return ausgewaehltVergabeEigenesPasswort;
    }

    public void setAusgewaehltVergabeEigenesPasswort(boolean ausgewaehltVergabeEigenesPasswort) {
        this.ausgewaehltVergabeEigenesPasswort = ausgewaehltVergabeEigenesPasswort;
    }

    public boolean isAnzeigeHinweisDatenschutzerklaerung() {
        return anzeigeHinweisDatenschutzerklaerung;
    }

    public void setAnzeigeHinweisDatenschutzerklaerung(boolean anzeigeHinweisDatenschutzerklaerung) {
        this.anzeigeHinweisDatenschutzerklaerung = anzeigeHinweisDatenschutzerklaerung;
    }

    public boolean isAnzeigeMeldungsText1() {
        return anzeigeMeldungsText1;
    }

    public void setAnzeigeMeldungsText1(boolean anzeigeMeldungsText1) {
        this.anzeigeMeldungsText1 = anzeigeMeldungsText1;
    }

    public boolean isAnzeigeMeldungsText2() {
        return anzeigeMeldungsText2;
    }

    public void setAnzeigeMeldungsText2(boolean anzeigeMeldungsText2) {
        this.anzeigeMeldungsText2 = anzeigeMeldungsText2;
    }

    public boolean isEmailbestaetigen() {
        return emailbestaetigen;
    }

    public void setEmailbestaetigen(boolean emailbestaetigen) {
        this.emailbestaetigen = emailbestaetigen;
    }

    public boolean isEmail2bestaetigen() {
        return email2bestaetigen;
    }

    public void setEmail2bestaetigen(boolean email2bestaetigen) {
        this.email2bestaetigen = email2bestaetigen;
    }

    public boolean isPasswortBereitsVergeben() {
        return passwortBereitsVergeben;
    }

    public void setPasswortBereitsVergeben(boolean passwortBereitsVergeben) {
        this.passwortBereitsVergeben = passwortBereitsVergeben;
    }

    public boolean isNeuesPasswort() {
        return neuesPasswort;
    }

    public void setNeuesPasswort(boolean neuesPasswort) {
        this.neuesPasswort = neuesPasswort;
    }

    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        if (test.compareTo("1") == 0) {
            eclPortalTexteM.setPflegen(true);
        } else {
            eclPortalTexteM.setPflegen(false);
        }
        this.test = test;
    }

    public int getEingabeQuelle() {
        return eingabeQuelle;
    }

    public void setEingabeQuelle(int eingabeQuelle) {
        this.eingabeQuelle = eingabeQuelle;
    }

    public int getRcWillenserklaerungIdentAusgefuehrt() {
        return rcWillenserklaerungIdentAusgefuehrt;
    }

    public void setRcWillenserklaerungIdentAusgefuehrt(int rcWillenserklaerungIdentAusgefuehrt) {
        this.rcWillenserklaerungIdentAusgefuehrt = rcWillenserklaerungIdentAusgefuehrt;
    }

    public int getRcWillenserklaerungIdentAusgefuehrtZweit() {
        return rcWillenserklaerungIdentAusgefuehrtZweit;
    }

    public void setRcWillenserklaerungIdentAusgefuehrtZweit(int rcWillenserklaerungIdentAusgefuehrtZweit) {
        this.rcWillenserklaerungIdentAusgefuehrtZweit = rcWillenserklaerungIdentAusgefuehrtZweit;
    }

    public String getDummy() {
        return dummy;
    }

    public void setDummy(String dummy) {
        this.dummy = dummy;
    }

    public boolean isVollmachtEingeben() {
        return vollmachtEingeben;
    }

    public void setVollmachtEingeben(boolean vollmachtEingeben) {
        this.vollmachtEingeben = vollmachtEingeben;
    }

    public boolean isVollmachtEingeben2() {
        return vollmachtEingeben2;
    }

    public void setVollmachtEingeben2(boolean vollmachtEingeben2) {
        this.vollmachtEingeben2 = vollmachtEingeben2;
    }

    public String getErteiltZeitpunkt() {
        return erteiltZeitpunkt;
    }

    public void setErteiltZeitpunkt(String erteiltZeitpunkt) {
        this.erteiltZeitpunkt = erteiltZeitpunkt;
    }

    public String getEintrittskarteEmailBestaetigen() {
        return eintrittskarteEmailBestaetigen;
    }

    public void setEintrittskarteEmailBestaetigen(String eintrittskarteEmailBestaetigen) {
        this.eintrittskarteEmailBestaetigen = eintrittskarteEmailBestaetigen;
    }

    public String getEintrittskarteEmail2Bestaetigen() {
        return eintrittskarteEmail2Bestaetigen;
    }

    public void setEintrittskarteEmail2Bestaetigen(String eintrittskarteEmail2Bestaetigen) {
        this.eintrittskarteEmail2Bestaetigen = eintrittskarteEmail2Bestaetigen;
    }

    public String getGastkarteEmailBestaetigen() {
        return gastkarteEmailBestaetigen;
    }

    public void setGastkarteEmailBestaetigen(String gastkarteEmailBestaetigen) {
        this.gastkarteEmailBestaetigen = gastkarteEmailBestaetigen;
    }

    public int getEintrittskarteWurdeGedruckt() {
        return eintrittskarteWurdeGedruckt;
    }

    public void setEintrittskarteWurdeGedruckt(int eintrittskarteWurdeGedruckt) {
        this.eintrittskarteWurdeGedruckt = eintrittskarteWurdeGedruckt;
    }

    public String getEintrittskarteDruckDatum() {
        return eintrittskarteDruckDatum;
    }

    public void setEintrittskarteDruckDatum(String eintrittskarteDruckDatum) {
        this.eintrittskarteDruckDatum = eintrittskarteDruckDatum;
    }

    public String getZutrittsIdent() {
        return zutrittsIdent;
    }

    public void setZutrittsIdent(String zutrittsIdent) {
        this.zutrittsIdent = zutrittsIdent;
    }

    public String getZutrittsIdent2() {
        return zutrittsIdent2;
    }

    public void setZutrittsIdent2(String zutrittsIdent2) {
        this.zutrittsIdent2 = zutrittsIdent2;
    }

    public String getZutrittsIdentNeben() {
        return zutrittsIdentNeben;
    }

    public void setZutrittsIdentNeben(String zutrittsIdentNeben) {
        this.zutrittsIdentNeben = zutrittsIdentNeben;
    }

    public String getZutrittsIdentNeben2() {
        return zutrittsIdentNeben2;
    }

    public void setZutrittsIdentNeben2(String zutrittsIdentNeben2) {
        this.zutrittsIdentNeben2 = zutrittsIdentNeben2;
    }

    public String getGastkartenZutrittsIdent() {
        return gastkartenZutrittsIdent;
    }

    public void setGastkartenZutrittsIdent(String gastkartenZutrittsIdent) {
        this.gastkartenZutrittsIdent = gastkartenZutrittsIdent;
    }

    public String getGastkartenZutrittsIdentNeben() {
        return gastkartenZutrittsIdentNeben;
    }

    public void setGastkartenZutrittsIdentNeben(String gastkartenZutrittsIdentNeben) {
        this.gastkartenZutrittsIdentNeben = gastkartenZutrittsIdentNeben;
    }

    @Deprecated
    public String getEinsprungsLinkFuerEmail() {
        return einsprungsLinkFuerEmail;
    }

    @Deprecated
    public void setEinsprungsLinkFuerEmail(String einsprungsLinkFuerEmail) {
        this.einsprungsLinkFuerEmail = einsprungsLinkFuerEmail;
    }

    @Deprecated
    public String getEinsprungsLinkNurCode() {
        return einsprungsLinkNurCode;
    }

    @Deprecated
    public void setEinsprungsLinkNurCode(String einsprungsLinkNurCode) {
        this.einsprungsLinkNurCode = einsprungsLinkNurCode;
    }

    public boolean isBestaetigtDassBerechtigt() {
        return bestaetigtDassBerechtigt;
    }

    public void setBestaetigtDassBerechtigt(boolean bestaetigtDassBerechtigt) {
        this.bestaetigtDassBerechtigt = bestaetigtDassBerechtigt;
    }

    public int getErstRegistrierungOderEinstellungenAktiv() {
        return erstRegistrierungOderEinstellungenAktiv;
    }

    public void setErstRegistrierungOderEinstellungenAktiv(int erstRegistrierungOderEinstellungenAktiv) {
        this.erstRegistrierungOderEinstellungenAktiv = erstRegistrierungOderEinstellungenAktiv;
    }

    public int getMitteilungenAngebotenLogin() {
        return mitteilungenAngebotenLogin;
    }

    public void setMitteilungenAngebotenLogin(int mitteilungenAngebotenLogin) {
        this.mitteilungenAngebotenLogin = mitteilungenAngebotenLogin;
    }

    public int getStreamAngebotenLogin() {
        return streamAngebotenLogin;
    }

    public void setStreamAngebotenLogin(int streamAngebotenLogin) {
        this.streamAngebotenLogin = streamAngebotenLogin;
    }

    public int getFragenAngebotenLogin() {
        return fragenAngebotenLogin;
    }

    public void setFragenAngebotenLogin(int fragenAngebotenLogin) {
        this.fragenAngebotenLogin = fragenAngebotenLogin;
    }

    public int getEinstellungenAngebotenLogin() {
        return einstellungenAngebotenLogin;
    }

    public void setEinstellungenAngebotenLogin(int einstellungenAngebotenLogin) {
        this.einstellungenAngebotenLogin = einstellungenAngebotenLogin;
    }

    public int getLoginArt() {
        return loginArt;
    }

    public void setLoginArt(int loginArt) {
        this.loginArt = loginArt;
    }

    public int getTeilnehmerverzAngebotenLogin() {
        return teilnehmerverzAngebotenLogin;
    }

    public void setTeilnehmerverzAngebotenLogin(int teilnehmerverzAngebotenLogin) {
        this.teilnehmerverzAngebotenLogin = teilnehmerverzAngebotenLogin;
    }

    public int getAbstimmungsergAngebotenLogin() {
        return abstimmungsergAngebotenLogin;
    }

    public void setAbstimmungsergAngebotenLogin(int abstimmungsergAngebotenLogin) {
        this.abstimmungsergAngebotenLogin = abstimmungsergAngebotenLogin;
    }

    public String getAuswahlMaske() {
        return auswahlMaske;
    }

    public void setAuswahlMaske(String auswahlMaske) {
        this.auswahlMaske = auswahlMaske;
    }

}
