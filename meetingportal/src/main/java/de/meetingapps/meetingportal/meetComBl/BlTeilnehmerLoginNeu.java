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
package de.meetingapps.meetingportal.meetComBl;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaDatumZeit;
import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComAllg.CaPasswortVerschluesseln;
import de.meetingapps.meetingportal.meetComAllg.CaString;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclAktienregister;
import de.meetingapps.meetingportal.meetComEntities.EclAktienregisterErgaenzung;
import de.meetingapps.meetingportal.meetComEntities.EclAnrede;
import de.meetingapps.meetingportal.meetComEntities.EclAppZugeordneteKennungen;
import de.meetingapps.meetingportal.meetComEntities.EclLoginDaten;
import de.meetingapps.meetingportal.meetComEntities.EclPersonenNatJur;
import de.meetingapps.meetingportal.meetComHVParam.ParamSpezial;
import de.meetingapps.meetingportal.meetComKonst.KonstHinweisWeitere;
import de.meetingapps.meetingportal.meetComKonst.KonstLoginKennungArt;

/**Ersetzt BlTeilnehmerLogin******************/

/** **************************Teilnehmer-Login Übersicht***********************************************
 * Beinhaltet Funktionen für den Login über Teilnehmer-Portal/App 
 * 
 * Grundkonzept - Kennung für Portal:
 * 
 * (Feldnamen beziehen sich auf EclLoginDaten)
 * 
 * Kennung ist grundsätzlich Nicht-Case-Sensitive.
 * 
 * 
 * loginKennung:
 * > KonstNummerBasis.formNummer: "Vorgegebene Kennung aus Aktionärsdaten": Kennung bestehend aus reiner Zahlenreihe, 
 * 	 ggf. gefolgt von Buchstabe (anstelle 1/0 für Unterscheidung E/F-Besitz) 
 * 		> Wenn Inhaberaktien: dann ist es die ZutrittsIdent (Eintrittskarten-Nummer)
 * 		> Wenn Namensaktien: dann ist es die Aktionärsnummer
 * 
 * > KonstNummerBasis.formS: "Neue - vergebene Kennung": "S", gefolgt von Zahlenreihe
 * 		Entweder z.B. für Bevollmächtigten oder Gast vom System vergeben, oder generell vom System generiert.
 * 		> Kennung beginnt mit S und besteht aus einer beliebigen Kombination von Buchstaben und Zahlen, die vom
 * 			System vergeben wurden: => Vom System generierte Kennung, die anstelle Eintrittskartennummer oder Aktionärsnummer
 * 			nach Vergabe verwendet werden kann
 * 
 * loginKennungAlternativ:
 * > KonstNummerBasis.formA: "Automatisch vergebene alternative Kennung": "A", gefolgt von Zahlenreihe
 *  
 * > KonstNummerBasis.formMail: "Mail-Adresse"
 * 		> Kennung enthält "@": dies ist eine Email-Adresse, die auch mit Versand/Bestätigung verifiziert wurde.
 * 		Wurde vom Aktionär als "neue selbstgewählte Kennung" eingegeben.
 * 		Achtung: eine Email-Adresse kann nur EINMAL pro Mandant vergeben werden. D.h. ein Teilnehmer mit mehreren
 * 		Einträgen im Aktienregister muß sich diese Zuordnen, oder Systemgenerierte Kennungen verwenden!
 * 
 * Darüberhinaus sowohl für loginKennung und loginKennungAlternativ beliebige Kennungen möglich, dann ist jedoch keine "Autoerkennung"
 * möglich.
 * 
 * Handhabung von Eintrittskarten - bei Namensaktien generell, bei Inhaberaktien die zusätzlich ausgestellten (also mit lfd.Nummer>0,
 * z.B. Ersatzkarten, oder Karten für Bevollmächtigte)
 * > als Login dient hier nicht die Eintrittskartennummer, sondern die Kennung.
 * > hat den Vorteil, das bei Ersatzkarten z.B. die selbe Kennung / Passwort wiederverwendet wird. Außerdem weniger leicht findbar.
 * > Konsequenz: "Login-Eintrittskartennummer" muß dann "Login-Eintrittskartennummer/Kennung" heißen (muß aber sowieso wg.
 * 		Gästekarten usw. so sein)
 * 
 * Grundkonzept - Passwort
 * > Grundsätzlich wird in jedem Satz, der eine Kennung enthält, auch das jeweils gültige Passwort gespeichert.
 * 
 * > Das gültige Passwort (auch das initiale) steht immer verschüsselt in passwortVerschluesselt.
 * 
 * > Wenn das Initial-Passwort noch gültig ist, steht dieses in passwortInitial
 * 
 * > Wurde ein Passwort-Vergessen-Prozess initialisiert, so wird das neu-vergebene Passwort in passwortAlternativVerschluesselt und passwortAlternativInitial
 * 		gespeichert. Sobald ein erster Login mit diesem Passwort erfolgt, wird dies automatisch gelöscht und in passwortVerschluesselt übertragen.
 * 
 * 
 */
public class BlTeilnehmerLoginNeu {

    public boolean isRcAnzeigeMeldungsText2() {
        return anzeigeMeldungsText2;
    }

    public void setRcAnzeigeMeldungsText2(boolean rcAnzeigeMeldungsText2) {
        this.anzeigeMeldungsText2 = rcAnzeigeMeldungsText2;
    }

    private int logDrucken = 3;

    private DbBundle lDbBundle;

    /*******Neue Parameterwerte*********/

    /**Immer gefüllt - gemäß eingegebener Login-Kennung*/
    public EclLoginDaten eclLoginDaten = null;

    /**Gefüllt, je nachdem welche Kennungart verwendet wurde*/
    public EclAktienregister eclAktienregister = null;
    public EclPersonenNatJur eclPersonenNatJur = null;

    /**Wird nur gefüllt, wenn "Sonderablauf" (ku178)*/
    public EclAktienregisterErgaenzung eclAktienregisterErgaenzung = null;

    public String anmeldeKennungFuerAnzeige = "";

    /**Wird benötigt z.B. für Abfrage der handelnden Person*/
    public String titelVornameName = ""; //Dr. Hans Müller
    public String ort = ""; //eingetragener Ort (nicht Versandadresse!) ohne PLZ

    /**Weitere Felder für Adresse etc.*/
    public String titel = "";
    public String name = "";
    public String vorname = "";
    public String strasse = "";
    public String plzOrt = "";

    /**Wird benötigt z.B. für Anzeige der eingeloggten Kennungsdaten.
     * Hinweis: 
     * > bei Aktienregister entspricht dies der in eclAktienregister in den Versandzeilen gespeicherten Versandadresse, einschließlich Anrede
     * > bei sonstigen Kennungen werden die Adresszeilen zusammengesetzt - ohne Anrede 
     * 		und vereinfacht nur mit Landesschlüssel vor Ort (aus Performancegründen)
     * */
    public List<String> adresszeilen = null;

    public long stimmen = 0;
    public String stimmenDE = "";
    /**String formatiert im Deutschen Format*/
    public String stimmenEN = "";
    /**String formatiert im Englischen Format*/

    /**Wird durch pruefeErstregistrierung gefuellt - 
     * true => Erst-Registrierungsseite muß nach Login aufgerufen werden*/
    public boolean erstregistrierungAufrufen = false;

    /**Wird durch bereiteRegistrierungVor gefüllt*/
    public boolean anzeigeMeldung = false;
    public boolean anzeigeMeldungsText1 = false;
    public boolean anzeigeMeldungsText2 = false;

    public boolean emailbestaetigen = false;
    public boolean email2bestaetigen = false;

    /**Sowohl Ein- als auch Ausgabe. Wenn true, dann muß nach Aufruf
     * der Funktion ein Bestätigungs-Email veschickt werden.
     * Ist entweder der Fall, wenn bereits mit true übergeben (weil
     * "neuen Bestätigungslink verschicken" gedrückt wurde), oder wenn
     * EMail verändert wurde*/
    public boolean emailBestaetigenMailVerschicken = false;
    public boolean email2BestaetigenMailVerschicken = false;
    
    /**True => die (neue) E-Mail-Adresse muß an das Remote-
     * Aktienregister übermittelt werden.
     */
    public boolean emailAnRemoteRegister=false;

    public int kommunikationssprache = 0;

    /**Unverändertes Eingabefeld zum Prüfen/Übernehmen*/
    public String kommunikationsspracheRaw = "";

    public boolean eVersandRegistrierung = false;
    /**AAAAA neuer Parameter Portal Logik !!!!*/
    public boolean eMailBestaetigungBeiAllenWillenserklaerungen=false;

    public boolean passwortBereitsVergeben = false;
    public boolean vergabeEigenesPasswort = false;

    /**Eingabefelder - Passwort wird geändert*/
    public boolean passwortNeuVergeben = false;
    public String neuesPasswort = "";
    public String neuesPasswortBestaetigung = "";

    /**Falls Passwort verändert wurde: Passwort verschlüsselt, für Rückgabe
     * an App
     */
    public String passwortVerschluesselt = "";

    public String eMailFuerVersand = "";
    public String eMailFuerVersandBestaetigen = "";
   
    /**Wird vor dem Aufrufen gefüllt - und dazu verwendet, um ggf.
     * Flags zu setzen, wenn die hinterlegte E-Mail im Remote-Register und
     * im Aktionärsportal voneinander abweichen.
     */
    public String eMailInRemoteRegister="";
    
    /**true, wenn sowohl im Portal als auch im Remote-Register
     * eine (voneinander abweichende) E-Mail-Adresse gespeichert ist.
     */
    public boolean eMailInRemoteRegisterWeichtAb=false;
    
    /**true, wenn nur im Remote-Register eine E-Mail-Adresse
     * gespeichert ist.
     */
    public boolean eMailNurInRemoteRegister=false;
    
    /**Wird vor dem Aufrufen gefüllt*/
    public boolean eMailInRemoteRegisterinArbeit=false;

    public String eMail2FuerVersand = "";
    public String eMail2FuerVersandBestaetigen = "";
    public String eMailBestaetigungsCode = "";
    public String eMail2BestaetigungsCode = "";
    public boolean eMail2Verwenden=false;

    /**Eingabefelder  Bestätigung durchgeführt*/
    public boolean hinweisHVPortalBestaetigt = false;
    public boolean hinweisAktionaersPortalBestaetigt = false;
    /**Eingabefelder  Bestätigung durchgeführt für Permanent-Portal*/
    public boolean hinweisWeitere1Bestaetigt = false;
    public boolean hinweisWeitere2Bestaetigt = false;

    /**Werte nach speicherRegistrierungAb für Quittungsseite*/
    public boolean quittungEMailVersandNeuRegistriert = false;
    public boolean quittungEMailVersandDeRegistriert = false;
    
    /*AAAAA neuer Parameter Portal Logik !!!!*/
    public boolean quittungEMailBestaetigungBeiAllenWillenserklaerungenNeuRegistriert = false;
    public boolean quittungEMailBestaetigungBeiAllenWillenserklaerungenDeRegistriert = false;
    
    
    public boolean quittungEmailNochNichtBestaetigt = false;
    public boolean quittungEmail2NochNichtBestaetigt = false;
    public boolean quittungEmailWurdeBestaetigt=false;
    public boolean quittungEmail2WurdeBestaetigt=false;

    public boolean quittungDauerhaftesPasswortAktiviert = false;
    public boolean quittungDauerhaftesPasswortDeAktiviert = false;
    public boolean quittungDauerhaftesPasswortGeaendert = false;

    public boolean quittungNeueEmailAdresseAusgetragen = false;
    public boolean quittungNeueEmail2AdresseAusgetragen = false;

    public boolean quittungNeueEmailAdresseEingetragen = false;
    public boolean quittungNeueEmail2AdresseEingetragen = false;

    public String quittungNeueEmailAdresse = "";
    public String quittungNeueEmail2Adresse = "";

    /********************************Datenstruktur für die Anmelde-Kennung***************************************/

    /**Return-Code für Web-Services*/
    public int rc = 0;

    /**angemeldete Kennung*/
    public String anmeldeKennung = "";

    /**angemeldete Kennung - aufbereitet für Anzeige (z.B. für ku178)*/
    public String anmeldeKennungAufbereitet = "";

    /**Passwort zur angemeldeten Kennung*/
    public String anmeldePasswort = "";

    /**Return-Wert: der Verschlüsselte Wert des Passworts*/
    public String anmeldePasswortVerschluesselt = "";

    /**Art der anmeldeKennung: 
     * 1 = Aktienregister (aktionärsnummer), 
     * 2 = Aktionärsmeldung ("Eintrittskarte- Inhaberaktien"), - derzeit nicht verwendet
     * 3= PersonNatJur*/
    @Deprecated
    public int anmeldeKennungArt = 0;

    /**Ident, im Aktienregister, auf den die Anmelde-Kennung verweist*/
    @Deprecated
    public int anmeldeIdentAktienregister = 0;

    /**Aktionärsnummer / Aktienregister, auf die die Anmelde-Kennung verweist*/
    public String anmeldeAktionaersnummer = "";

    /**Ident der natJur-Person, auf die die Anmelde-Kennung verweist (soweit bereits angelegt!)*/
    public int anmeldeIdentPersonenNatJur = 0;

    /**0 => "normale" Person (Bevollmächtigter etc.). 
     * >0 => Insti-Vertreter
     * -1 = Gast
     * Falls Institutioneller Anleger, dann hier die Nummer der - mandantenübergreifenden - instiident
     * 
     * Temporäre Lösung: wird aus kommunikationssprache der Person geholt
     * */
    public int instiIdent = 0;

    /**Name, auf die die Anmelde-Kennung verweist*/
    public String anmeldeNameVorname = "";

    /**Name, auf die die Anmelde-Kennung verweist*/
    public String anmeldeOrt = "";

    /**Im folgenden die ausführlichen Bestandteile für den eingeloggten Teilnehmer*/

    public String anredeDE = "";
    /**Herr, Frau etc. gemäß Schlüssel*/
    public String anredeEN = "";
    /*Ab hier erst mal alle Deprecated!*/

    public String landeskuerzel = "";
    /**DE*/
    public String land = "";
    /**Deutschland*/
    @Deprecated
    public String briefanredeDE = "";
    /**wie aus anreden-Datei*/
    @Deprecated
    public String briefanredeEN = "";
    /**wie aus Anreden Datei*/
    /**Wird z.B. benötigt für interne Anzeige des Anmeldestellentools*/
    public String nameVornameTitel = "";
    /**Müller, Hans Dr.*/
    public String kompletteAnredeDE = "";
    /**"Sehr geehrter Herr Dr. Müller" oder bei juristische Person "Sehr geehrte Damen und Herren"*/
    public String kompletteAnredeEN = "";
    /**"Sehr geehrter Herr Dr. Müller" oder bei juristische Person "Sehr geehrte Damen und Herren"*/
    public String besitzArtKuerzel = "";
    public String besitzArt = "";
    public String besitzArtEN = "";
    public int gattung = 0;


    /**Expertenmodus: 
     * > kann optional vom Teilnehmer im Portal nur aktiviert werden, wenn expertenModusMoeglich==true.
     * > wird automatisch aktiviert, wenn von diesem Teilnehmer mehrere - parallele - Willenserklärungen vorliegen
     * 		(also z.B. eine Vollmacht an Dritte und gleichzeitig eine Vollmacht/Weisung)
     *  
     * Der ExpertenModus ist aktuell für diesen Teilnehmer aktiviert 
     */
    public boolean expertenModusAktiv = false;

    /**Der Expertenmodus darf aktuell von diesem Teilnehmer selbst aktiviert werden. Ist letztendlich abhängig von
     * der Parameterstellung für diesen Mandanten, und ggf. von der Teilnehmerart (z.B.: normaler Aktionär darf ihn
     * nicht aktivieren, aber Bankenvertreter darf ihn aktivieren*/
    public boolean expertenModusMoeglich = false;

    /**Alle (einschließlich ggf. anmeldeIdentPersonenNatJur) Personen, die mit dieser Kennung verknüpft (zugeordnet) wurden*/
    public int[] alleIdentPersonenNatJur = null;


    /************Ab hier: neu - nur noch diese verwenden!*********************/

    /**Daten der eingeloggten Person - noch nicht fertig, derzeit immer nur der eingetragene Aktionär*/
    /*TODO _Aktionaerskennung: Innovation - aktuell wird in die Daten immer der eingetragene Aktionär eingetragen*/

    /**Daten des Aktionärs, für den gerade Willenserkläerungen bearbeitet werden*/
    public List<String> verarbeiteterAktionaerListe = null;
    public List<String> verarbeiteterAktionaerListeEN = null;

    public List<String> eingeloggtePersonListe = null;
    public List<String> eingeloggtePersonListeEN = null;


    /**true => diese Benutzerkennung darf sich nicht für elektronischen Einladungsversand und nicht für 
     * ein dauerhaftes Passwort registrieren
     */
    public boolean dauerhafteRegistrierungUnzulaessig = false;

    private boolean permPortal=false;
    
    public BlTeilnehmerLoginNeu() {
    }
    
    public BlTeilnehmerLoginNeu(boolean pPermPortal) {
        permPortal=pPermPortal;
    }


    /**Übergeben der Datenbankverbindung zur weiteren Verwendung. Initialisiert müssen mindestens folgende
     * Tables sein:
     * 
     */
    public void initDB(DbBundle pDbBundle) {
        lDbBundle = pDbBundle;
    }

    /*Daten aus Aktienregistereintrag in lokale Daten übertragen*/
    private void decodeAusAktienregister(EclAktienregister pAktienregisterEintrag) {
        CaBug.druckeLog("Aufruf", logDrucken, 10);

        name=pAktienregisterEintrag.liefereName();
        vorname=pAktienregisterEintrag.liefereVorname();
        titel=pAktienregisterEintrag.liefereTitel();
        titelVornameName=pAktienregisterEintrag.liefereTitelVornameName();
        CaBug.druckeLog("titelVornameName="+titelVornameName, logDrucken, 10);

        /*Ort*/
        ort = pAktienregisterEintrag.ort;

        plzOrt = "";
        if (!pAktienregisterEintrag.postleitzahl.isEmpty()) {
            plzOrt = pAktienregisterEintrag.postleitzahl + " ";
        }
        plzOrt = plzOrt + pAktienregisterEintrag.ort;
        strasse = pAktienregisterEintrag.strasse;

        CaBug.druckeLog("vor fuelleAdresszeilenAusAktienregister", logDrucken, 10);
        fuelleAdresszeilenAusAktienregister();
        CaBug.druckeLog("nach fuelleAdresszeilenAusAktienregister", logDrucken, 10);

        stimmen = pAktienregisterEintrag.stimmen;
        stimmenDE = CaString.toStringDE(stimmen);
        stimmenEN = CaString.toStringEN(stimmen);

        /*REENGINEER*/
        //	
        //		lDbBundle.dbAktienregisterLoginDaten.readIdent(pAktienregisterEintrag.aktienregisterIdent);
        //		CaBug.druckeLog("pAktienregisterEintrag.aktienregisterIdent="+pAktienregisterEintrag.aktienregisterIdent, "BlTeilnehmerLogin.decodeAusAktienregister", logDrucken, 10);
        //		if (lDbBundle.dbAktienregisterLoginDaten.anzErgebnis()==0){
        //			CaBug.druckeLog("Return bei 1", logDrucken, 10);
        //			return false;
        //		}
        //		EclAktienregisterLoginDaten lAktienregisterLoginDaten=lDbBundle.dbAktienregisterLoginDaten.ergebnisPosition(0);
        //		
        //		anmeldePasswort=lAktienregisterLoginDaten.passwortVerschluesselt;
        //		
        //		anmeldenUnzulaessig=(lAktienregisterLoginDaten.anmeldenUnzulaessig==1);
        //		dauerhafteRegistrierungUnzulaessig=(lAktienregisterLoginDaten.dauerhafteRegistrierungUnzulaessig==1);
        //		
        //		/*
        //		if (lAktienregisterLoginDaten.passwortVerschluesselt.isEmpty()){
        //			anmeldePasswort=lAktienregisterLoginDaten.passwortInitial;
        //		}
        //		else{
        //			anmeldePasswort=lAktienregisterLoginDaten.passwortVerschluesselt;
        //		}
        //		*/
        //		
        //		anmeldeKennungArt=1;
        //		anmeldeIdentAktienregister=pAktienregisterEintrag.aktienregisterIdent;
        //		anmeldeNameVorname=pAktienregisterEintrag.nachname+" "+pAktienregisterEintrag.vorname;
        //		anmeldeOrt=pAktienregisterEintrag.ort;
        //		anmeldeAktionaersnummer=pAktienregisterEintrag.aktionaersnummer;
        //		
        //		stimmen=pAktienregisterEintrag.stimmen;
        //		stimmenDE=CaString.toStringDE(stimmen);
        //		stimmenEN=CaString.toStringEN(stimmen);
        //		besitzArtKuerzel=pAktienregisterEintrag.besitzart;
        //		switch (besitzArtKuerzel){
        //		case "E":{besitzArt="Eigenbesitz";besitzArtEN="Proprietary Possession";break;}
        //		case "F":{besitzArt="Fremdbesitz";besitzArtEN="Minority Interests";break;}
        //		case "V":{besitzArt="Vollmachtsbesitz";besitzArtEN="Proxy Possession";break;}
        //		}
        //		gattung=pAktienregisterEintrag.getGattungId();
        //		
        //		name2=pAktienregisterEintrag.name2;
        //		name3=pAktienregisterEintrag.name3;
        //		if (!pAktienregisterEintrag.postleitzahlPostfach.isEmpty() && pAktienregisterEintrag.postleitzahlPostfach.compareTo("0")!=0){
        //			plz=pAktienregisterEintrag.postleitzahlPostfach;
        //			strasse="Postfach "+pAktienregisterEintrag.postfach;
        //		}
        //		else{
        //			plz=pAktienregisterEintrag.postleitzahl;
        //			strasse=pAktienregisterEintrag.strasse;
        //		}
        //		if (pAktienregisterEintrag.staatId!=0){
        //			lDbBundle.dbStaaten.readId(pAktienregisterEintrag.staatId);
        //			if (lDbBundle.dbStaaten.anzErgebnis()>0){
        //				landeskuerzel=lDbBundle.dbStaaten.ergebnisPosition(0).code;
        //				land=lDbBundle.dbStaaten.ergebnisPosition(0).nameDE;
        //			}
        //		}
        //		
        //		/*Anrede füllen*/
        //		int anredenNr=pAktienregisterEintrag.anredeId;
        //		EclAnrede hAnrede=fuelleAnredenfelder(anredenNr);
        //
        //		/*Kombi-Felder füllen*/
        //		
        //		if (pAktienregisterEintrag.istJuristischePerson==1){
        //			nameVornameTitel=pAktienregisterEintrag.name1;
        //			if (!pAktienregisterEintrag.name2.isEmpty()){
        //				nameVornameTitel=nameVornameTitel+" "+pAktienregisterEintrag.name2;
        //			}
        //			if (!pAktienregisterEintrag.name3.isEmpty()){
        //				nameVornameTitel=nameVornameTitel+" "+pAktienregisterEintrag.name3;
        //			}
        //		}
        //		else{
        //			nameVornameTitel=pAktienregisterEintrag.nachname;
        //			if (pAktienregisterEintrag.titel.length()!=0 || pAktienregisterEintrag.vorname.length()!=0){nameVornameTitel=nameVornameTitel+",";}
        //			if (pAktienregisterEintrag.titel.length()!=0){nameVornameTitel=nameVornameTitel+" "+pAktienregisterEintrag.titel;}
        //			if (pAktienregisterEintrag.vorname.length()!=0){nameVornameTitel=nameVornameTitel+" "+pAktienregisterEintrag.vorname;}
        //		}
        //
        //		kompletteAnredeDE=briefanredeDE;
        //		kompletteAnredeEN=briefanredeEN;
        //		if (hAnrede.istjuristischePerson!=1){
        //			if (titel.length()!=0){
        //				kompletteAnredeDE=kompletteAnredeDE+" "+titel;
        //				kompletteAnredeEN=kompletteAnredeEN+" "+titel;
        //			}
        //			if (name.length()!=0){
        //				kompletteAnredeDE=kompletteAnredeDE+" "+name;
        //				kompletteAnredeEN=kompletteAnredeEN+" "+name;
        //			}
        //		}
        //		
        //		versandAbweichend=pAktienregisterEintrag.versandAbweichend;
        //		nameVersand=pAktienregisterEintrag.nachnameVersand;
        //		vornameVersand=pAktienregisterEintrag.vornameVersand;
        //		name2Versand=pAktienregisterEintrag.name2Versand;
        //		name3Versand=pAktienregisterEintrag.name3Versand;
        //		
        //		if (!pAktienregisterEintrag.postleitzahlPostfachVersand.isEmpty() && pAktienregisterEintrag.postleitzahlPostfachVersand.compareTo("0")!=0){
        //			postleitzahlVersand=pAktienregisterEintrag.postleitzahlPostfachVersand;
        //			strasseVersand="Postfach "+pAktienregisterEintrag.postfachVersand;
        //		}
        //		else{
        //			postleitzahlVersand=pAktienregisterEintrag.postleitzahlVersand;
        //			strasseVersand=pAktienregisterEintrag.strasseVersand;
        //		}
        //		
        //		ortVersand=pAktienregisterEintrag.ortVersand;
        //		if (pAktienregisterEintrag.staatIdVersand!=0){
        //			lDbBundle.dbStaaten.readId(pAktienregisterEintrag.staatIdVersand);
        //			if (lDbBundle.dbStaaten.anzErgebnis()>0){
        //				landeskuerzelVersand=lDbBundle.dbStaaten.ergebnisPosition(0).code;
        //				landVersand=lDbBundle.dbStaaten.ergebnisPosition(0).nameDE;
        //			}
        //		}
        //		
        //		
        //		fuelleEingeloggtePersonAusAktienregister(pAktienregisterEintrag);
        //		fuelleVerarbeiteterAktionaerAusAktienregister(pAktienregisterEintrag);
        //		
        //		adresszeilen.add(pAktienregisterEintrag.adresszeile1);
        //		adresszeilen.add(pAktienregisterEintrag.adresszeile2);
        //		adresszeilen.add(pAktienregisterEintrag.adresszeile3);
        //		adresszeilen.add(pAktienregisterEintrag.adresszeile4);
        //		adresszeilen.add(pAktienregisterEintrag.adresszeile5);
        //		adresszeilen.add(pAktienregisterEintrag.adresszeile6);
        //		adresszeilen.add(pAktienregisterEintrag.adresszeile7);
        //		adresszeilen.add(pAktienregisterEintrag.adresszeile8);
        //		adresszeilen.add(pAktienregisterEintrag.adresszeile9);
        //		adresszeilen.add(pAktienregisterEintrag.adresszeile10);
        //		return true;
    }

    /**Anredenfelder füllen:
     * anredeDE, anredeEN, briefanredeDE, briefanredeEN
     */
    private EclAnrede fuelleAnredenfelder(int pAnredenNr) {
        /*REENGINEER*/
        EclAnrede hAnrede = new EclAnrede();
        if (pAnredenNr != 0) {
            lDbBundle.dbAnreden.SetzeSprache(2, 0);
            lDbBundle.dbAnreden.ReadAnrede_Anredennr(pAnredenNr);
            hAnrede = new EclAnrede();
            if (lDbBundle.dbAnreden.AnzAnredenInReadArray > 0) {
                hAnrede = lDbBundle.dbAnreden.anredenreadarray[0];
            }
            anredeDE = hAnrede.anredentext;
            anredeEN = hAnrede.anredentextfremd;
            briefanredeDE = hAnrede.anredenbrief;
            briefanredeEN = hAnrede.anredenbrieffremd;
        }
        return hAnrede;
    }

    private void fuelleEingeloggtePersonAusAktienregister(EclAktienregister pAktienregister) {
        /*REENGINEER*/
        eingeloggtePersonListe = new ArrayList<String>();
        eingeloggtePersonListeEN = new ArrayList<String>();
        fuelleListenAusAktienregister(eingeloggtePersonListe, eingeloggtePersonListeEN, pAktienregister);
    }

    private void fuelleListenAusAktienregister(List<String> pListeDE, List<String> pListeEN,
            EclAktienregister pAktienregister) {
        /*REENGINEER*/

        /*Titel?*/
        if (lDbBundle.param.paramPortal.personenAnzeigeAnredeMitAufnahmen != 0) {
            if (pAktienregister.istJuristischePerson == 0
                    || lDbBundle.param.paramPortal.personenAnzeigeAnredeMitAufnahmen == 1) {
                pListeDE.add(anredeDE);
                pListeEN.add(anredeEN);
            }
        }
        if ((pAktienregister.istJuristischePerson == 1 && !ParamSpezial.ku178(lDbBundle.clGlobalVar.mandant))
                || (pAktienregister.anredeId == 2
                        && ParamSpezial.ku178(lDbBundle.clGlobalVar.mandant))) {/*Namensfelder juristische Person*/
            String hFeld = pAktienregister.name1;
            if (!hFeld.isEmpty()) {
                pListeDE.add(hFeld);
                pListeEN.add(hFeld);
            }
            hFeld = pAktienregister.name2;
            if (!hFeld.isEmpty()) {
                pListeDE.add(hFeld);
                pListeEN.add(hFeld);
            }
            hFeld = pAktienregister.name3;
            if (!hFeld.isEmpty()) {
                pListeDE.add(hFeld);
                pListeEN.add(hFeld);
            }
        } else {/*Namensfelder natürliche Person*/
            String hFeld = "";
            String hString = pAktienregister.titel;
            if (!hString.isEmpty()) {
                hFeld = hFeld + hString + " ";
            }
            hString = pAktienregister.vorname;
            if (!hString.isEmpty()) {
                hFeld = hFeld + hString + " ";
            }
            hString = pAktienregister.nachname;
            if (!hString.isEmpty()) {
                hFeld = hFeld + hString + " ";
            }
            hFeld = hFeld.trim();
            pListeDE.add(hFeld);
            pListeEN.add(hFeld);
        }
        boolean isPostfach = false;
        if (!pAktienregister.postfach.isEmpty()) {
            isPostfach = true;
        }
        if (isPostfach) {/*Postfach, PLZ Postfach*/
            if (lDbBundle.param.paramPortal.textPostfachMitAufnahmen == 0) {
                pListeDE.add(pAktienregister.postfach);
                pListeEN.add(pAktienregister.postfach);
            } else {
                pListeDE.add("Postfach " + pAktienregister.postfach);
                pListeEN.add("Postfach " + pAktienregister.postfach);
            }
            String hFeld = "";
            String hString = pAktienregister.postleitzahlPostfach;
            if (!hString.isEmpty()) {
                hFeld = hFeld + hString + " ";
            }
            hString = pAktienregister.ort;
            if (!hString.isEmpty()) {
                hFeld = hFeld + hString;
            }
            pListeDE.add(hFeld);
            pListeEN.add(hFeld);
        } else {/*Straße, PLZ Ort*/
            pListeDE.add(pAktienregister.strasse);
            pListeEN.add(pAktienregister.strasse);
            String hFeld = "";
            String hString = pAktienregister.postleitzahl;
            if (!hString.isEmpty()) {
                hFeld = hFeld + hString + " ";
            }
            hString = pAktienregister.ort;
            if (!hString.isEmpty()) {
                hFeld = hFeld + hString;
            }
            pListeDE.add(hFeld);
            pListeEN.add(hFeld);
        }
        if (landeskuerzel.compareTo("DE") != 0) {
            pListeDE.add(land);
            pListeEN.add(land);
        }

    }

    private void fuelleAdresszeilenAusAktienregister() {
        CaBug.druckeLog("Start", logDrucken, 10);
        adresszeilen = new ArrayList<String>();

        CaBug.druckeLog("eclAktienregister.istJuristischePerson=" + eclAktienregister.istJuristischePerson
                + " eclAktienregister.anredeId=" + eclAktienregister.anredeId, logDrucken, 10);
        if (eclAktienregister.istJuristischePerson == 1 || eclAktienregister.anredeId == 2) {
            String hFeld = eclAktienregister.name1;
            if (!hFeld.isEmpty()) {
                adresszeilen.add(hFeld);
            }
            hFeld = eclAktienregister.name2;
            if (!hFeld.isEmpty()) {
                adresszeilen.add(hFeld);
            }
            hFeld = eclAktienregister.name3;
            if (!hFeld.isEmpty()) {
                adresszeilen.add(hFeld);
            } else {
                String hString = eclAktienregister.titel;
                if (!hString.isEmpty()) {
                    hFeld = hFeld + hString + " ";
                }
                hString = eclAktienregister.vorname;
                if (!hString.isEmpty()) {
                    hFeld = hFeld + hString + " ";
                }
                hString = eclAktienregister.nachname;
                if (!hString.isEmpty()) {
                    hFeld = hFeld + hString + " ";
                }
                hFeld = hFeld.trim();
                adresszeilen.add(hFeld);
            }
        } else {/*Namensfelder natürliche Person*/
            String hFeld = "";
            String hString = eclAktienregister.titel;
            if (!hString.isEmpty()) {
                hFeld = hFeld + hString + " ";
            }
            hString = eclAktienregister.vorname;
            if (!hString.isEmpty()) {
                hFeld = hFeld + hString + " ";
            }
            hString = eclAktienregister.nachname;
            if (!hString.isEmpty()) {
                hFeld = hFeld + hString + " ";
            }
            hFeld = hFeld.trim();
            adresszeilen.add(hFeld);
        }
        adresszeilen.add(eclAktienregister.ort);
        //		boolean isPostfach=false;
        //		if (!eclAktienregister.postfach.isEmpty()){isPostfach=true;}
        //		if (isPostfach){/*Postfach, PLZ Postfach*/
        //			if (lDbBundle.param.paramPortal.textPostfachMitAufnahmen==0){
        //				adresszeilen.add(eclAktienregister.postfach);
        //			}
        //			else{
        //				adresszeilen.add("Postfach "+eclAktienregister.postfach);
        //			}
        //			String hFeld="";
        //			String hString=eclAktienregister.postleitzahlPostfach;
        //			if (!hString.isEmpty()){hFeld=hFeld+hString+" ";}
        //			hString=eclAktienregister.ort;
        //			if (!hString.isEmpty()){hFeld=hFeld+hString;}
        //			adresszeilen.add(hFeld);
        //		}
        //		else{/*Straße, PLZ Ort*/
        //			adresszeilen.add(eclAktienregister.strasse);
        //			String hFeld="";
        //			String hString=eclAktienregister.postleitzahl;
        //			if (!hString.isEmpty()){hFeld=hFeld+hString+" ";}
        //			hString=eclAktienregister.ort;
        //			if (!hString.isEmpty()){hFeld=hFeld+hString;}
        //			adresszeilen.add(hFeld);
        //		}
        //		if (landeskuerzel.compareTo("DE")!=0){
        //			adresszeilen.add(land);
        //		}

    }

    private void fuelleAdresszeilenAusPersonNatJur() {
        BlPersonenNatJur blPersonenNatJur = new BlPersonenNatJur();
        adresszeilen = blPersonenNatJur.aufbereitenAdresseOhneAnredeMitLandesschluessel(eclPersonenNatJur);
    }

    private void fuelleVerarbeiteterAktionaerAusAktienregister(EclAktienregister pAktienregister) {
        /*REENGINEER*/
        verarbeiteterAktionaerListe = new ArrayList<String>();
        verarbeiteterAktionaerListeEN = new ArrayList<String>();
        adresszeilen = new ArrayList<String>();
        fuelleListenAusAktienregister(verarbeiteterAktionaerListe, verarbeiteterAktionaerListeEN, pAktienregister);

    }

    /*Daten aus PersonenNatJur in lokale Daten übertragen*/
    private void decodeAusPersonenNatJur(EclPersonenNatJur pPersonenNatJur) {

        /*titelVornameName füllen*/
        titelVornameName = pPersonenNatJur.liefereTitelVornameName();

        ort = pPersonenNatJur.ort;

        name = pPersonenNatJur.name;
        vorname = pPersonenNatJur.vorname;
        titel = pPersonenNatJur.titel;
        plzOrt = "";
        if (!pPersonenNatJur.plz.isEmpty()) {
            plzOrt = pPersonenNatJur.plz + " ";
        }
        plzOrt = plzOrt + pPersonenNatJur.ort;
        strasse = pPersonenNatJur.strasse;

        fuelleAdresszeilenAusPersonNatJur();

        stimmen = 0;
        stimmenDE = "";
        stimmenEN = "";

        /*REENGINEER*/
        //		anmeldePasswort=pPersonenNatJur.loginPasswort;
        //		anmeldeKennungArt=3;
        //		anmeldeIdentPersonenNatJur=pPersonenNatJur.ident;
        //		anmeldeNameVorname=pPersonenNatJur.name+" "+pPersonenNatJur.vorname;
        //		anmeldeOrt=pPersonenNatJur.ort;
        //		instiIdent=pPersonenNatJur.kommunikationssprache;
        //		
        //		stimmen=0;
        //		stimmenDE="";
        //		stimmenEN="";
        //		besitzArtKuerzel="";besitzArt="";
        //		gattung=0;
        //		plz=pPersonenNatJur.plz;
        //		if (!pPersonenNatJur.land.isEmpty()){
        //			lDbBundle.dbStaaten.readCode(pPersonenNatJur.land);
        //			if (lDbBundle.dbStaaten.anzErgebnis()>0){
        //				landeskuerzel=lDbBundle.dbStaaten.ergebnisPosition(0).code;
        //				land=lDbBundle.dbStaaten.ergebnisPosition(0).nameDE;
        //			}
        //		}
        //		
        //		/*Anrede füllen*/
        //		int anredenNr=pPersonenNatJur.anrede;
        //		EclAnrede hAnrede=fuelleAnredenfelder(anredenNr);
        //
        //		/*Kombi-Felder füllen*/
        //		
        //		nameVornameTitel=name;
        //		if (titel.length()!=0 || vorname.length()!=0){nameVornameTitel=nameVornameTitel+",";}
        //		if (titel.length()!=0){nameVornameTitel=nameVornameTitel+" "+titel;}
        //		if (vorname.length()!=0){nameVornameTitel=nameVornameTitel+" "+vorname;}
        //
        //		kompletteAnredeDE=briefanredeDE;
        //		kompletteAnredeEN=briefanredeEN;
        //		if (hAnrede.istjuristischePerson!=1){
        //			if (titel.length()!=0){
        //				kompletteAnredeDE=kompletteAnredeDE+" "+titel;
        //				kompletteAnredeEN=kompletteAnredeEN+" "+titel;
        //			}
        //			if (name.length()!=0){
        //				kompletteAnredeDE=kompletteAnredeDE+" "+name;
        //				kompletteAnredeEN=kompletteAnredeEN+" "+name;
        //			}
        //		}
        //		
        //		fuelleEingeloggtePersonAusPersonenNatJur(pPersonenNatJur);
    }

    private void fuelleEingeloggtePersonAusPersonenNatJur(EclPersonenNatJur pPersonenNatJur) {
        /*REENGINEER*/
        eingeloggtePersonListe = new ArrayList<String>();
        eingeloggtePersonListeEN = new ArrayList<String>();
        adresszeilen = new LinkedList<String>();
        fuelleListenAusPersonenNatJur(eingeloggtePersonListe, eingeloggtePersonListeEN, pPersonenNatJur);
    }

    private void fuelleListenAusPersonenNatJur(List<String> pListeDE, List<String> pListeEN,
            EclPersonenNatJur pPersonenNatJur) {
        /*REENGINEER*/

        /*Titel?*/
        if (lDbBundle.param.paramPortal.personenAnzeigeAnredeMitAufnahmen != 0) {
            pListeDE.add(anredeDE);
            pListeEN.add(anredeEN);
            adresszeilen.add(anredeDE);
        }
        adresszeilen.add(anredeDE);

        /*Namensfelder natürliche Person*/
        String hFeld = "";
        String hString = pPersonenNatJur.titel;
        if (!hString.isEmpty()) {
            hFeld = hFeld + hString + " ";
        }
        hString = pPersonenNatJur.vorname;
        if (!hString.isEmpty()) {
            hFeld = hFeld + hString + " ";
        }
        hString = pPersonenNatJur.name;
        if (!hString.isEmpty()) {
            hFeld = hFeld + hString + " ";
        }
        hFeld = hFeld.trim();
        pListeDE.add(hFeld);
        pListeEN.add(hFeld);
        adresszeilen.add(hFeld);

        /*Straße, PLZ Ort*/
        pListeDE.add(pPersonenNatJur.strasse);
        pListeEN.add(pPersonenNatJur.strasse);
        adresszeilen.add(pPersonenNatJur.strasse);

        hFeld = "";
        hString = pPersonenNatJur.plz;
        if (!hString.isEmpty()) {
            hFeld = hFeld + hString + " ";
        }
        hString = pPersonenNatJur.ort;
        if (!hString.isEmpty()) {
            hFeld = hFeld + hString;
        }
        pListeDE.add(hFeld);
        pListeEN.add(hFeld);
        adresszeilen.add(hFeld);

        if (landeskuerzel.compareTo("DE") != 0) {
            pListeDE.add(land);
            pListeEN.add(land);
            adresszeilen.add(land);
        }

    }

    /**Findet (so vorhanden) die eingegeben Kennung und legt diese - mit Kennzeichen, Passwort etc. ab:
     * eclLoginDaten
     * eclAktienregister
     * eclPersonenNatJur
     * anmeldeKennungFuerAnzeige
     * 
     * Return-Werte:
     * 1 = alles ok
     * afKennungUnbekannt
     * afBerechtigungFuerAktionaersportalFehlt
     * afPasswortFalsch
     * pfXyWurdeVonAnderemBenutzerVeraendert
     * afUserLoginTemporaerGesperrt
     * 
     * falls pAutoAuswahl==false:
     * 	pKennungAlternativVerwenden=true => loginKennungAlternativ wird verwendet
     * 
     * Früher wurde auch belegt:
     *      * erstregistrierungAufrufen
     * aber hierfür muß anschließend nunr pruefeErstregistrierung aufgerufen werden
     * */
    public int findeUndPruefeKennung(String pKennung, String pPasswort, boolean pPasswortPruefen/*, boolean pAutoAuswahl,
            boolean pKennungAlternativVerwenden*/) {
        int erg=0;

        CaBug.druckeLog("pKennung=" + pKennung, logDrucken, 3);
        if (pKennung.isEmpty()) {
            return (CaFehler.afKennungUnbekannt);
        } /*Leerstring ist nicht zulässig*/

        BlNummernformBasis blNummernformBasis = new BlNummernformBasis(lDbBundle);
        String lAnmeldeKennung = blNummernformBasis.loginKennungAufbereitenFuerIntern(pKennung);
        int nummernformBasis = blNummernformBasis.rcNummernformBasis;

        CaBug.druckeLog("nummernformBasis=" + nummernformBasis + " lAnmeldeKennung=" + lAnmeldeKennung, logDrucken, 3);
        
        boolean alternativeKennungGelesen=false;
        
        if (lDbBundle.param.paramPortal.alternativeLoginKennung==0 || lDbBundle.param.paramPortal.alternativeLoginKennung==1) {
            /*über loginKennung suchen*/
            erg = lDbBundle.dbLoginDaten.read_loginKennung(lAnmeldeKennung);
        }
        if (erg==0 && (lDbBundle.param.paramPortal.alternativeLoginKennung==1 || lDbBundle.param.paramPortal.alternativeLoginKennung==2)) {
            /*über loginKennungAlternative suchen*/
            erg = lDbBundle.dbLoginDaten.read_loginKennungAlternative(lAnmeldeKennung);
            alternativeKennungGelesen=true;
        }
        
        
        if (erg <= 0) {
            CaBug.druckeLog("In dbLoginDaten nicht gefunden", logDrucken, 3);
            return (CaFehler.afKennungUnbekannt);
        }
        eclLoginDaten = lDbBundle.dbLoginDaten.ergebnisPosition(0);
        if (alternativeKennungGelesen==true) {
            lAnmeldeKennung=eclLoginDaten.loginKennung;
        }

        if (eclLoginDaten.anmeldenUnzulaessig == 1) {
            return CaFehler.afBerechtigungFuerAktionaersportalFehlt;
        }

        CaBug.druckeLog("vor leseSatzZuEclLoginDaten", logDrucken, 10);
        erg = leseSatzZuEclLoginDaten();
        if (erg < 0) {
            return erg;
        }

        /**anmeldeKennungFuerAnzeige belegen (z.B. um führende 0 abzuschneiden)*/
//        anmeldeKennungFuerAnzeige = blNummernformBasis.wandleZuExtern(lAnmeldeKennung); deprecated Code eliminiert, 22.11.2023
        anmeldeKennungFuerAnzeige = BlNummernformBasis.aufbereitenInternFuerExtern(lAnmeldeKennung, lDbBundle);

        CaBug.druckeLog("anmeldeKennungFuerAnzeige=" + anmeldeKennungFuerAnzeige, logDrucken, 3);
        if (pPasswortPruefen) {
            CaBug.druckeLog("Passwort Pruefen Start", logDrucken, 10);
            erg = pruefePasswort(pPasswort);
            if (erg < 0) {
                CaBug.druckeLog("Passwort Pruefen erg <0", logDrucken, 10);
                return erg;
            }
            CaBug.druckeLog("Passwort Pruefen ok", logDrucken, 10);

        }

        if (eclLoginDaten.letzterLoginAufServer == 100000) {
            if (CaDatumZeit.difMinutenZuStempel(eclLoginDaten.zeitstempel) < 1) {
                return CaFehler.afUserLoginTemporaerGesperrt;
            }
        }

//        /**Prüfen, ob nach Login zwangsweise Erstregistrierung aufzurufen ist*/
//        pruefeErstregistrierung();
//        nun separat aufrufen!!!

        return 1;
    }

    /**liest anhand eclLogindaten bzw. der übergebenen Kennung
     * eclLoginDaten, eclAktienregister, eclPersonenNatJur
     * erneut aus Datenbank ein.
     * 
     * Mögliche Fehlermeldungen:
     * 1 = alles ok
     * afKennungUnbekannt
     * afBerechtigungFuerAktionaersportalFehlt
     * 
     */
    public int reloadKennung() {
        return reloadKennung(eclLoginDaten.loginKennung);
    }

    public int reloadKennung(String pKennungIntern) {
        int erg = 0;
        erg = lDbBundle.dbLoginDaten.read_loginKennung(pKennungIntern);
        if (erg <= 0) {
            CaBug.drucke("001");
            return (CaFehler.afKennungUnbekannt);
        }
        eclLoginDaten = lDbBundle.dbLoginDaten.ergebnisPosition(0);

        erg = leseSatzZuEclLoginDaten();
        if (erg < 0) {
            return erg;
        }

        return 1;
    }

    /**Liest Kennungssatz ein und überprüft, ob User mittlerweile gesperrt,
     * bzw. ob auf anderem Server eingeloggt.
     * 1= ok
     * afBerechtigungFuerAktionaersportalFehlt
     * afUserAusgeloggtWgParallelLogin
     * afBesitzReloadDurchfuehren
     */
    public int pruefeRefresh(int pIdent, int pServernummer, boolean pPruefenDoppeltLogin) {
        EclLoginDaten lLoginDaten = holeAktuelleLogiNDaten(pIdent);
        if (pPruefenDoppeltLogin && Math.abs(lLoginDaten.letzterLoginAufServer) != pServernummer) {
            return CaFehler.afUserAusgeloggtWgParallelLogin;
        }
        if (lLoginDaten.anmeldenUnzulaessig == 1) {
            return CaFehler.afBerechtigungFuerAktionaersportalFehlt;
        }
        if (lLoginDaten.letzterLoginAufServer < 0) {
            return CaFehler.afBesitzReloadDurchfuehren;
        }
        return 1;
    }

    
    /**Liest Kennungssatz ein und liefert ihn zurück. Z.B. zum Status-Überprüfen bei Wortmeldung*/
    public EclLoginDaten holeAktuelleLogiNDaten(int pIdent) {
        lDbBundle.dbLoginDaten.read_ident(pIdent);
        EclLoginDaten lLoginDaten = lDbBundle.dbLoginDaten.ergebnisPosition(0);
        return lLoginDaten;
    }
    
    int tempLetzterLoginAufServer = 0;
    long tempZeitstempel = 0;

    /**Sperrt den temporären Login für den User (für die Dauer des Login-Vorgangs).
     * 
     * 1 => ok, konnte gesperrt werden
     * afUserLoginTemporaerGesperrt => derzeit kein Sperren möglich, bitte warten
     * @return
     */
    public int sperreTempLogin() {
        tempZeitstempel = CaDatumZeit.zeitStempelMS();
        int rc = lDbBundle.dbLoginDaten.update_letzterLoginAufServer(eclLoginDaten.ident,
                eclLoginDaten.letzterLoginAufServer, eclLoginDaten.zeitstempel, 100000, tempZeitstempel);
        if (rc < 1) {
            return CaFehler.afUserLoginTemporaerGesperrt;
        }
        tempLetzterLoginAufServer = 100000;
        return 1;
    }

    /**eclLoginDaten muß gefüllt sein.
     * sperreTempLogin muß vorher aufgerufen sein (ohne die Klasse neu zu initialisieren)
     * 
     * 
     * Returnwert: >1=Zeistempel
     * <0 => technischer schwerwiegender Fehler*/
    public long updateLetzterLoginAufServer(int serverNr) {
        //int pIdent, int pServerNummerAlt, long pZeitstempelAlt, int pServerNummerNeu, long pZeitstempelNeu
        long neuerZeitstempel = CaDatumZeit.zeitStempelMS();
        int rc = lDbBundle.dbLoginDaten.update_letzterLoginAufServer(eclLoginDaten.ident, tempLetzterLoginAufServer,
                tempZeitstempel, serverNr, neuerZeitstempel);
        if (rc < 0) {
            return (long) rc;
        }
        return neuerZeitstempel;
    }

    private int leseSatzZuEclLoginDaten() {
        int erg = 0;
        switch (eclLoginDaten.kennungArt) {
        case KonstLoginKennungArt.aktienregister: {
            erg = lDbBundle.dbAktienregister.leseZuAktienregisterIdent(eclLoginDaten.aktienregisterIdent);
            if (erg <= 0) {
                CaBug.drucke("001");
                return (CaFehler.afKennungUnbekannt);
            }
            eclAktienregister = lDbBundle.dbAktienregister.ergebnisPosition(0);
            CaBug.druckeLog("vor decodeAusAktienregister", logDrucken, 10);
            decodeAusAktienregister(eclAktienregister);

            if (lDbBundle.param.paramPortal.varianteDialogablauf == 1) {
                /*EclAktienregisterErgaenzung zusätzlich füllen*/
                erg = lDbBundle.dbAktienregisterErgaenzung.readZuident(eclLoginDaten.aktienregisterIdent);
                if (erg <= 0) {
                    CaBug.drucke("003");
                    return (CaFehler.afKennungUnbekannt);
                }
                eclAktienregisterErgaenzung = lDbBundle.dbAktienregisterErgaenzung.ergebnisPosition(0);
            }
            break;
        }
        case KonstLoginKennungArt.personenNatJur: {
            erg = lDbBundle.dbPersonenNatJur.read(eclLoginDaten.personenNatJurIdent);
            if (erg <= 0) {
                CaBug.drucke("002");
                return (CaFehler.afKennungUnbekannt);
            }
            eclPersonenNatJur = lDbBundle.dbPersonenNatJur.PersonNatJurGefunden(0);
            decodeAusPersonenNatJur(eclPersonenNatJur);
            break;
        }
        }
        return 1;

    }

    /**Überprüfen, ob Passwort gültig für diese Kennung
     * 
     * pPasswort kann entweder das eingegebene Passwort im Klartext sein 
     * (z.B. für Portal), oder ein bereits verschlüsseltes Passwort
     * (z.B. für App)
     * 
     * 1 => Passwort gültig
     * afPasswortFalsch
     * pfXyWurdeVonAnderemBenutzerVeraendert
     */
    private int pruefePasswort(String pPasswort) {

        String eingegebenesPasswortZumVergleich = pPasswort;
        if (eingegebenesPasswortZumVergleich.isEmpty()) {
            return (CaFehler.afPasswortFalsch);
        }

        if (lDbBundle.param.paramPortal.passwortCaseSensitiv != 1) {
            eingegebenesPasswortZumVergleich = pPasswort.toUpperCase();
        }

        /*Checken: ist das übergebene Passwort bereits verschlüsselt,
         * dann einfach auf Identität prüfen
         */
        if (pPasswort.compareToIgnoreCase(eclLoginDaten.passwortVerschluesselt) == 0) {
            return 1;
        }
        if (pPasswort.compareToIgnoreCase(eclLoginDaten.passwortAlternativVerschluesselt) == 0) {
            int rc = updatePasswortMitAlternativem();
            if (rc < 0) {
                return CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert;
            }
            return 1;
        }

        /*Passwort verschlüsseln und dann vergleichen*/
        String lPasswortVerschluesselt = CaPasswortVerschluesseln.verschluesseln(eingegebenesPasswortZumVergleich);
        if (lPasswortVerschluesselt.compareToIgnoreCase(eclLoginDaten.passwortVerschluesselt) == 0) {
            return 1;
        }
        if (lPasswortVerschluesselt.compareToIgnoreCase(eclLoginDaten.passwortAlternativVerschluesselt) == 0) {
            int rc = updatePasswortMitAlternativem();
            if (rc < 0) {
                return CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert;
            }
            return 1;
        }

        String lPasswortOhneSonderzeichen = eingegebenesPasswortZumVergleich.trim();
        lPasswortOhneSonderzeichen = CaString.entferneSteuerzeichen(lPasswortOhneSonderzeichen);
        lPasswortVerschluesselt = CaPasswortVerschluesseln.verschluesseln(lPasswortOhneSonderzeichen);
        if (lPasswortVerschluesselt.compareToIgnoreCase(eclLoginDaten.passwortVerschluesselt) == 0) {
            return 1;
        }
        if (lPasswortVerschluesselt.compareToIgnoreCase(eclLoginDaten.passwortAlternativVerschluesselt) == 0) {
            int rc = updatePasswortMitAlternativem();
            if (rc < 0) {
                return CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert;
            }
            return 1;
        }

        
        /**Nun noch checken, ob Initialpasswort gefüllt?*/
        String initialPasswort=eclLoginDaten.lieferePasswortInitialClean();
        if (lDbBundle.param.paramPortal.passwortCaseSensitiv != 1) {
            initialPasswort = initialPasswort.toUpperCase();
        }
       if (!initialPasswort.isEmpty()) {
            if (lPasswortOhneSonderzeichen.equals(initialPasswort)) {
                return 1;
            }
        }
        
        return (CaFehler.afPasswortFalsch);
    }

    /**1 =Update erfolgreich
     * pfXyWurdeVonAnderemBenutzerVeraendert
     */
    private int updatePasswortMitAlternativem() {
        eclLoginDaten.passwortVerschluesselt = eclLoginDaten.passwortAlternativVerschluesselt;
        eclLoginDaten.passwortInitial = eclLoginDaten.passwortAlternativInitial;
        eclLoginDaten.passwortAlternativInitial = "";
        eclLoginDaten.passwortAlternativVerschluesselt = "";
        return lDbBundle.dbLoginDaten.update(eclLoginDaten);
    }

    /***********Initialisierung für aRegistrierung und aEinstellungen**************************
     * Return-Wert:
     * > Prüft, ob die Erstregistrierungsseite aufgerufen werden muß, oder direkt zum HV-Teil gegangen werden kann
     * > true=Erstregistrierung
     * Ansonsten: Die für aRegistrierung/aEinstellungen benötigten, unten aufgeführten Variablen, werden
     * gesetzt.
     * 
     * Voraussetzung: eclLoginDaten ist gefüllt.
     * 
     * Abhängig von:
     * > hinweisAktionaersPortalBestaetigt?
     * > hinweisHVPortalBestaetigt?
     * > bereits Mail-Registrierung angeboten?
     * 
     * > E-Mail noch zu bestätigen? (Sonst Hinweis auf Erst-Registrierungsseite)
     * 
     * Gesetzt sind anschließend:
     * > aDlgVariablen.anzeigeMeldung
     * > aDlgVariablen.anzeigeMeldungsText1
     * > aDlgVariablen.anzeigeMeldungsText2
     * 
     * > aDlgVariablen.emailbestaetigen (bei Erstregistrierung nicht gebraucht, dennoch auf false setzen)
     * > aDlgVariablen.email2bestaetigen (bei Erstregistrierung nicht gebraucht, dennoch auf false setzen)
     * 
     * > aDlgVariablen.passwortBereitsVergeben
     * > aDlgVariablen.ausgewaehltVergabeEigenesPasswort
     * > aDlgVariablen.neuesPasswort (ist immer false - Checkbox für Änderung)
     * 
     * > aDlgVariablen.anzeigeHinweisDatenschutzerklaerung  
     * > aDlgVariablen.anzeigeHinweisHVPortalBestaetigen
     * > aDlgVariablen.anzeigeHinweisAktionaersPortalBestaetigen
     * 
     * 
     */
    public void pruefeErstregistrierung() {
        erstregistrierungAufrufen = false;

        /*Meldungen anzeigen?*/
        if (eclLoginDaten.emailBestaetigt == 0 && (!eclLoginDaten.eMailFuerVersand.isEmpty())) {
            /*Email-Adresse 1 vorhanden und noch nicht bestätigt?*/
            erstregistrierungAufrufen = true;
        }

        if (eclLoginDaten.liefereEMail2AdresseBestaetigt() == false 
                && lDbBundle.param.paramPortal.emailVersandZweitEMailAusRegister==0
                && (!eclLoginDaten.eMail2FuerVersand.isEmpty())) {
            /*Email-Adresse 2 vorhanden und noch nicht bestätigt? - wird aber noch
             * abgeprüft, wenn die zweite E-Mail-Adresse nicht aus Register kommt,
             * denn die aus dem Register gelieferte Adresse braucht natürlich nicht
             * bestätigt werden*/
            erstregistrierungAufrufen = true;
        }

        /*Bei Permanent-Portal: E-Mail eingetragen, eigenes Passwort vergeben, Email im Remote-Register abweichend?*/
        if (permPortal==true) {
            /*Kein Passwort vergeben*/
            if (!eclLoginDaten.passwortInitial.isEmpty()) {
                erstregistrierungAufrufen = true;
            }
            
            /*Keine E-Mail eingetragen*/
            if (eclLoginDaten.eMailFuerVersand.isEmpty() && eclLoginDaten.eMail2FuerVersand.isEmpty()) {
                erstregistrierungAufrufen = true;
            }
            
            if (eMailInRemoteRegister.isEmpty()==false && eMailInRemoteRegisterinArbeit==false) {
                if (!eMailInRemoteRegister.equalsIgnoreCase(eclLoginDaten.eMailFuerVersand)) {
                    /*Remote-E-Mail-Adresse weicht von Portal-EMail-Adresse ab*/
                    erstregistrierungAufrufen = true;
                    if (eclLoginDaten.eMailFuerVersand.isEmpty()) {
                        /*Nur Adresse im Remote-Register vorhanden*/
                        eMailNurInRemoteRegister=true;
                    }
                    else {
                        eMailInRemoteRegisterWeichtAb=true;
                    }
                }
            }
        }
        
        
        if (permPortal==false) {
            /*Hinweis Aktionärs Portal anzuzeigen?*/
            if ((lDbBundle.param.paramPortal.bestaetigenHinweisAktionaersportal != 0
                    && eclLoginDaten.hinweisAktionaersPortalBestaetigt == 0)
                    || lDbBundle.param.paramPortal.bestaetigenHinweisAktionaersportal == 2 /*Dann immer bestätigen bei jeder Anmeldung!*/
                    ) {
                erstregistrierungAufrufen = true;
            }

            /*Hinweis HV Portal anzuzeigen?*/
            if ((lDbBundle.param.paramPortal.bestaetigenHinweisHVportal != 0
                    && eclLoginDaten.hinweisHVPortalBestaetigt == 0)
                    || lDbBundle.param.paramPortal.bestaetigenHinweisHVportal == 2 /*Dann immer bestätigen bei jeder Anmeldung!*/
                    ) {
                erstregistrierungAufrufen = true;
            }
        }
        
        if (permPortal==true) {
            /*Hinweis weitere anzuzeigen?*/
            if (lDbBundle.param.paramPortal.liefereBestaetigenHinweisWeiter(KonstHinweisWeitere.PERM_PORTAL_HINWEIS1) !=0
                    && eclLoginDaten.liefereBestaetigenHinweisWeitere(KonstHinweisWeitere.PERM_PORTAL_HINWEIS1)==0 
                    ) {
                erstregistrierungAufrufen=true;
            }
            if (lDbBundle.param.paramPortal.liefereBestaetigenHinweisWeiter(KonstHinweisWeitere.PERM_PORTAL_HINWEIS2) !=0
                    && eclLoginDaten.liefereBestaetigenHinweisWeitere(KonstHinweisWeitere.PERM_PORTAL_HINWEIS2)==0 
                    ) {
                erstregistrierungAufrufen=true;
            }
        }
    }

    /**Eingelesen Datensatz, von pruefeZugeordneteKennung*/
    public EclAktienregister ergebnisAktienregister = null;

    /**Eingelesen Datensatz, von pruefeZugeordneteKennung*/
    public EclPersonenNatJur ergebnisPersonNatJur = null;

    /**Prüfen, ob Kennung so noch existiert, und Passwort noch zulässig.
     * Ergebnis wird im Ergebnis-Feld von pZugeordneteKennung mit abgelegt.
     * Die eingelesenen Datensätze werden zur weiteren Verarbeitung in ergebnisAktienregister 
     * bzw. ergebnisPersonNatJur abgelegt.
     */
    public void pruefeZugeordneteKennung(EclAppZugeordneteKennungen pZugeordneteKennung) {
        /*REENGINEER*/
        ergebnisAktienregister = null;
        ergebnisPersonNatJur = null;

        switch (pZugeordneteKennung.anmeldeKennungArt) {
        case 1:/*Aktienregister*/
            /*Kennung überprüfen*/
            lDbBundle.dbAktienregister.leseZuAktienregisterIdent(pZugeordneteKennung.aktienregisterIdent);
            if (lDbBundle.dbAktienregister.anzErgebnis() == 0) {/*Satz mit Ident gibts gar nicht*/
                pZugeordneteKennung.returnVerarbeitung = -1;
                return;
            }
            ergebnisAktienregister = lDbBundle.dbAktienregister.ergebnisPosition(0);
            if (ergebnisAktienregister.aktionaersnummer.compareTo(
                    BlAktienregisterNummerAufbereiten.aufbereitenFuerDatenbankZugriff(pZugeordneteKennung.kennung,
                            lDbBundle.param.paramBasis.laengeAktionaersnummer, lDbBundle.clGlobalVar.mandant)) != 0) {
                /*Kennung stimmt nicht mit Satznummer überein*/
                pZugeordneteKennung.returnVerarbeitung = -1;
                return;
            }
            //			if (ergebnisAktienregister.aktionaersnummer.compareTo(
            //					pZugeordneteKennung.kennung)!=0){
            //				System.out.println("Kennung stimmt nicht");
            //				/*Kennung stimmt nicht mit Satznummer überein*/
            //				pZugeordneteKennung.returnVerarbeitung=-1;return;
            //			}

            /*Passwort überprüfen*/
            lDbBundle.dbAktienregisterLoginDaten.readIdent(pZugeordneteKennung.aktienregisterIdent);
            if (lDbBundle.dbAktienregisterLoginDaten.anzErgebnis() == 0) {/*Satz mit Ident gibts gar nicht*/
                pZugeordneteKennung.returnVerarbeitung = -1;
                return;
            }
            anmeldePasswort = lDbBundle.dbAktienregisterLoginDaten.ergebnisPosition(0).passwortVerschluesselt;
            if (pruefePasswort(pZugeordneteKennung.passwort) == -1) {
                pZugeordneteKennung.returnVerarbeitung = -2;
                return;
            }

            break;
        case 2:/*Inhaberaktien*/
            /*TODO _Aktionaerskennung: Inhaberaktien*/
            break;
        case 3:/*PersonNatJur*/
            /*Kennung überprüfen*/
            lDbBundle.dbPersonenNatJur.read(pZugeordneteKennung.personNatJurIdent);
            if (lDbBundle.dbPersonenNatJur.anzPersonenNatJurGefunden() == 0) {/*Satz mit Ident gibts gar nicht*/
                pZugeordneteKennung.returnVerarbeitung = -1;
                return;
            }
            ergebnisPersonNatJur = lDbBundle.dbPersonenNatJur.PersonNatJurGefunden(0);
            if (ergebnisPersonNatJur.loginKennung.compareTo(pZugeordneteKennung.kennung) != 0) {
                /*Kennung stimmt nicht mit Satznummer überein*/
                pZugeordneteKennung.returnVerarbeitung = -1;
                return;
            }
            /*Passwort überprüfen*/
            anmeldePasswort = ergebnisPersonNatJur.loginPasswort;
            if (pruefePasswort(pZugeordneteKennung.passwort) == -1) {
                pZugeordneteKennung.returnVerarbeitung = -2;
                return;
            }
            break;
        }
    }

    /**Bereitet die Werte für die Registrierungsseite / Einstellungsseite auf.
     * 
     * Voraussetzung: 
     * > eclLoginDaten sind belegt.
     * > lDbBundle braucht nicht mehr geöffnet sein (also kein DB-Zugriff mehr in dieser Funktion)
     *  
     * Es werden gefüllt:
     * 
     * anzeigeMeldung
     * anzeigeMeldungsText1
     * anzeigeMeldungsText2
     * 
     * emailbestaetigen
     * email2bestaetigen
     * 
     * kommunikationssprache
     * eVersandRegistrierung
     * 
     * passwortBereitsVergeben
     * vergabeEigenesPasswort
     * 
     * eMailFuerVersand
     * eMail2FuerVersand
     */
    public void bereiteRegistrierungVor() {
        if (mailVerarbeiten()) {
            /*Meldungen anzeigen?*/
            if (eclLoginDaten.emailBestaetigt == 0 &&
                    (
                            (!eclLoginDaten.eMailFuerVersand.isEmpty())
                            || eMailNurInRemoteRegister
                            ) && (!eclLoginDaten.emailBestaetigenLink.isEmpty())
                    ) {
                /*Email-Adresse 1 vorhanden und noch nicht bestätigt?*/
                anzeigeMeldung = true;
                anzeigeMeldungsText1 = true;
                emailbestaetigen = true;
            }

            if (eclLoginDaten.liefereEMail2AdresseBestaetigt() == false 
                    && lDbBundle.param.paramPortal.emailVersandZweitEMailAusRegister==0
                    && (!eclLoginDaten.eMail2FuerVersand.isEmpty())) {
                /*Email-Adresse 2 vorhanden und noch nicht bestätigt?*/
                anzeigeMeldung = true;
                anzeigeMeldungsText2 = true;
                email2bestaetigen = true;
            }
        }

        //		/*Kommunikationssprache*/
        //		kommunikationssprache=eclLoginDaten.kommunikationssprache;

        if (eclLoginDaten.eVersandRegistrierung == 99) {
            eVersandRegistrierung = true;
        } else {
            eVersandRegistrierung = false;
        }

        if (eclLoginDaten.liefereQuittungPerEmailBeiAllenWillenserklaerungen()) {
            eMailBestaetigungBeiAllenWillenserklaerungen=true;
        }
        else {
            eMailBestaetigungBeiAllenWillenserklaerungen=false;
        }
        
        /*Passwort Registrierung*/
        if (eclLoginDaten.eigenesPasswort == 99 || eclLoginDaten.eigenesPasswort == 98) {
            /*Bereits eigenes Passwort vergeben*/
            passwortBereitsVergeben = true;
            vergabeEigenesPasswort = true;
        } else {
            passwortBereitsVergeben = false;
            vergabeEigenesPasswort = false;
        }

        eMailFuerVersand = eclLoginDaten.eMailFuerVersand;
        eMail2FuerVersand = eclLoginDaten.eMail2FuerVersand;
        eMail2Verwenden=eclLoginDaten.liefereEMail2AdresseVerwenden();

    }

    /**
     * Parameter:
     * pAufrufButton:
     * 0 = Normales Speichern
     * 1/2 = E-Mail Link 1 / 2 erneut verschicken
     * 
     * Gefüllt sein muß:
     * eclLoginDaten (aus Datenbank)
     * 
     * Eingabefelder:
     * kommunikationsspracheRaw
     * eVersandRegistrierung
     * passwortBereitsVergeben
     * vergabeEigenesPasswort
     * passwortNeuVergeben (nur relevant, wenn passwortBereitsVergeben==true)
     * neuesPasswort
     * neuesPasswortBestaetigung
     * eMailFuerVersand
     * eMailFuerVersandBestaetigen
     * eMail2FuerVersand
     * eMail2FuerVersandBestaetigen
     * eMailBestaetigungsCode (speziell für App, sonst leer)
     * eMail2BestaetigungsCode (speziell für App, sonst leer)
     * hinweisHVPortalBestaetigt
     * hinweisAktionaersPortalBestaetigt
     * 
     * Sowohl Ein- als auch Ausgabe. Wenn true, dann muß nach Aufruf
     * der Funktion ein Bestätigungs-Email veschickt werden.
     * Ist entweder der Fall, wenn bereits mit true übergeben (weil
     * "neuen Bestätigungslink verschicken" gedrückt wurde), oder wenn
     * EMail verändert wurde:
     * emailBestaetigenMailVerschicken
     * email2BestaetigenMailVerschicken
     * 
     * Nur Ausgabe:
     * emailAnRemoteRegister
     * 
     * Ergebnis:
     * rc>0 => Registrierung wurde abgespeichert
     * 	1 => Aufruf Bestätigungsseite
     * 	2 => Auswahl
     * passwortVerschluesselt - Falls neues Passwort vergeben, dann verschlüsseltes Passwort für Rückgabe an App
     * 
     * Für anschließende Bestätigungsseite - sowohl Eingabe als auch Ausgabewert
     * (Veränderungen werden fortgeschrieben):
     * Boolean:
     * 	quittungEMailVersandNeuRegistriert
     * 	quittungEMailVersandDeRegistriert
     * 	quittungEmailNochNichtBestaetigt
     * 	quittungEmail2NochNichtBestaetigt
     * 	quittungDauerhaftesPasswortAktiviert
     * 	quittungDauerhaftesPasswortDeAktiviert
     * 	quittungDauerhaftesPasswortGeaendert
     * 	quittungNeueEmailAdresseAusgetragen
     * 	quittungNeueEmail2AdresseAusgetragen
     * 	quittungNeueEmailAdresseEingetragen
     * 	quittungNeueEmail2AdresseEingetragen
     * Für Anzeige:
     * 	quittungNeueEmailAdresse
     * 	quittungNeueEmail2Adresse
     * 
     * Ansonsten folgende Fehlermeldungen:
     * afPasswortEMailNichtZulaessig
     * afKeineGueltigeEmailAdresse
     * afEmailAdresseBestaetigungWeichtAb
     * afKeineGueltigeEmail2Adresse
     * afEmail2AdresseBestaetigungWeichtAb
     * afEVersandErfordertPasswort
     * afEVersandErfordertEMail
     * afPasswortFehlt
     * afPasswortZuKurz
     * afPasswortEnthaeltNichtUnterschiedlicheZeichen
     * afPasswortBestaetigungWeichtAb
     * afPasswortErfordertEMail
     * afBestaetigungNutzungsbedingungenAktionaersPortalFehlt
     * afBestaetigungNutzungsbedingungenHVPortalFehlt
     * afEmailBestaetigungsCodeUnbekannt
     * afEmail2BestaetigungsCodeUnbekannt
     * 
     * afAndererUserAktiv
     * 
     */
    public int speichereRegistrierungAb(int pAufrufButton) {
        boolean altEVersandRegistriert = false;
        boolean altEMailBestaetigungBeiAllenWillenserklaerungenRegistriert = false;
        boolean altEigenesPasswortVergeben = false;
        String altEMailFuerVersand = "";
        String altEMail2FuerVersand = "";

        /*Alte Werte speichern für späteren Abgleich, was sich verändert hat,
         * für Bestätigungsseite 
         */
        altEVersandRegistriert = eclLoginDaten.eVersandRegistriert();
        altEMailBestaetigungBeiAllenWillenserklaerungenRegistriert = eclLoginDaten.liefereQuittungPerEmailBeiAllenWillenserklaerungen();
        altEigenesPasswortVergeben = eclLoginDaten.eigenesPasswortVergeben();
        altEMailFuerVersand = eclLoginDaten.eMailFuerVersand;
        altEMail2FuerVersand = eclLoginDaten.eMail2FuerVersand;

        /*Falls Permanent-Portal: Passwort-Selektionen vorauswählen, da dies
         * in Oberfläche teilweise deaktiviert (gegenüber normal-Portal)
         */
        if (permPortal==true) {
            /*Vergabe eines eigenes Passwortes ist immer zwingend*/
            if (vergabeEigenesPasswort==false) {
                vergabeEigenesPasswort=true;
            }
        }
        
        /*+++++++++++++++Eingaben überprüfen++++++++++++++++++++++*/

        if (mailVerarbeiten()) {
            /*Je nach Parameter: Ggf. E-Mail-Adresse nur zulässig, 
             * wenn Passwort eingegeben oder E-Versand-Registrierung, oder E-Mail-Adresse bereits
             * früher eingegeben*/
            if (lDbBundle.param.paramPortal.emailNurBeiEVersandOderPasswort == 1) {
                if ((!eMailFuerVersand.isEmpty() && altEMailFuerVersand.isEmpty()) || 
                        (!eMail2FuerVersand.isEmpty() && altEMail2FuerVersand.isEmpty())
                        ) {
                    if (eVersandRegistrierung == false && vergabeEigenesPasswort == false && eMailBestaetigungBeiAllenWillenserklaerungen==false) {
                        return CaFehler.afPasswortEMailNichtZulaessig;
                    }
                }
            }
            if (permPortal==true && eMailFuerVersand.isEmpty()) {
                return CaFehler.perEMailZwingend;
            }
            /*Falls Email-Adresse eingegeben, dann überprüfen*/
            if (!eMailFuerVersand.isEmpty()) {
                /*Email-Adresse gültig?*/
                if (CaString.isMailadresse(eMailFuerVersand) == false) {
                    return CaFehler.afKeineGueltigeEmailAdresse;
                }
                /*Email-Bestätigungs-Adresse gleich?*/
                if (!eMailFuerVersand.equalsIgnoreCase(eMailFuerVersandBestaetigen)) {
                    return CaFehler.afEmailAdresseBestaetigungWeichtAb;
                }
            }
            /*Falls Email2-Adresse eingegeben, dann überprüfen*/
            if (!eMail2FuerVersand.isEmpty()) {
                /*Email2-Adresse gültig?*/
                if (CaString.isMailadresse(eMail2FuerVersand) == false) {
                    return CaFehler.afKeineGueltigeEmail2Adresse;
                }
                /*Email-Bestätigungs-Adresse gleich?*/
                if (!eMail2FuerVersand.equalsIgnoreCase(eMail2FuerVersandBestaetigen)) {
                    return CaFehler.afEmail2AdresseBestaetigungWeichtAb;
                }
            }
            /*Falls für Email-Versand registriert - dann Email-Adresse hinterlegt? (nicht beim Widerspruchsverfahren!)*/
            if (eVersandRegistrierung && eMailFuerVersand.isEmpty() && eMail2FuerVersand.isEmpty() && lDbBundle.param.paramPortal.emailVersandRegistrierungOderWiderspruch!=2) {
                return CaFehler.afEVersandErfordertEMail;
            }
            /*Falls für Email-Quittung aktiviert - dann Email-Adresse hinterlegt?*/
            if (eMailBestaetigungBeiAllenWillenserklaerungen && eMailFuerVersand.isEmpty() && eMail2FuerVersand.isEmpty()) {
                return CaFehler.afEBestaetigungErfordertEMail;
            }
            
            /*Falls für Email-Versand registriert, und es erforderlich ist, dann dauerhaftes Passwort vergeben?*/
            if (eVersandRegistrierung && lDbBundle.param.paramPortal.dauerhaftesPasswortMoeglich == 2) {
                if (vergabeEigenesPasswort == false) {
                    return CaFehler.afEVersandErfordertPasswort;
                }
            }
        }
        
        if (passwortVerarbeiten()) {
            /*Falls eigenes Passwort gewählt ...*/
            if (vergabeEigenesPasswort /*Checkbox ist ausgewählt - Grundvoraussetzung*/
                    && (/*Passwort ist bereits vergeben - nur Checken, wenn Änderung*/
                            (passwortBereitsVergeben && passwortNeuVergeben) ||
                            /*Passwort noch nicht vergeben - dann immer prüfen, da neues passwort*/
                            !passwortBereitsVergeben)) {
                int rc=passwortEingabePruefen();
                if (rc!=1) {return rc;}
                /*Email-Adresse hinterlegt?*/
                if (eMailFuerVersand.isEmpty() && eMail2FuerVersand.isEmpty()) {
                    return CaFehler.afPasswortErfordertEMail;
                }

            }
        }

        /*Hinweis Aktionärs Portal anzuzeigen?*/
        if (permPortal==false) {
            if ((lDbBundle.param.paramPortal.bestaetigenHinweisAktionaersportal != 0
                    && eclLoginDaten.hinweisAktionaersPortalBestaetigt == 0)
                    || lDbBundle.param.paramPortal.bestaetigenHinweisAktionaersportal == 2 /*Dann immer bestätigen bei jeder Anmeldung!*/
                    ) {
                /*Aktionärsportal bestätigt?*/
                if (!hinweisAktionaersPortalBestaetigt) {
                    return CaFehler.afBestaetigungNutzungsbedingungenAktionaersPortalFehlt;
                }
            }
            /*Hinweis HV Portal anzuzeigen?*/
            if ((lDbBundle.param.paramPortal.bestaetigenHinweisHVportal != 0
                    && eclLoginDaten.hinweisHVPortalBestaetigt == 0)
                    || lDbBundle.param.paramPortal.bestaetigenHinweisHVportal == 2 /*Dann immer bestätigen bei jeder Anmeldung!*/
                    ) {
                /*HV-Portal bestätigt?*/
                if (!hinweisHVPortalBestaetigt) {
                    return CaFehler.afBestaetigungNutzungsbedingungenHVPortalFehlt;
                }
            }
        }
        /*Hinweise Peramenent-Portal bestätigt?*/
        if (permPortal) {
            if (lDbBundle.param.paramPortal.liefereBestaetigenHinweisWeiter(1) != 0
                    && eclLoginDaten.liefereBestaetigenHinweisWeitere(1) == 0)
            {
                 /*HV-Portal bestätigt?*/
                if (!hinweisWeitere1Bestaetigt) {
                    return CaFehler.perBestaetigungHinweisWeitere1Fehlt;
                }
            }
            if (lDbBundle.param.paramPortal.liefereBestaetigenHinweisWeiter(2) != 0
                    && eclLoginDaten.liefereBestaetigenHinweisWeitere(2) == 0)
            {
                 /*HV-Portal bestätigt?*/
                if (!hinweisWeitere1Bestaetigt) {
                    return CaFehler.perBestaetigungHinweisWeitere2Fehlt;
                }
            }
           
        }
        
        
        if (mailVerarbeiten()) {
            /*E-MailBestätigungscode*/
            String hString = eMailBestaetigungsCode.trim().toUpperCase();
            if (hString.isEmpty() && emailbestaetigen && lDbBundle.param.paramPortal.emailBestaetigenIstZwingend==1 && pAufrufButton==0 && (!eMailFuerVersand.isEmpty())) {
                return CaFehler.afEmailBestaetigungErforderlich;
            }
            if (!hString.isEmpty()) {
                if (!hString.equals(eclLoginDaten.emailBestaetigenLink)) {
                    return CaFehler.afEmailBestaetigungsCodeUnbekannt;
                }
            }
            /*E-MailBestätigungscode 2*/
            hString = eMail2BestaetigungsCode.trim().toUpperCase();
            if (hString.isEmpty() && email2bestaetigen && lDbBundle.param.paramPortal.emailBestaetigenIstZwingend==1 && pAufrufButton==0 && (!eMail2FuerVersand.isEmpty())) {
                return CaFehler.afEmail2BestaetigungErforderlich;
            }
            if (!hString.isEmpty()) {
                if (!hString.equals(eclLoginDaten.email2BestaetigenLink)) {
                    return CaFehler.afEmail2BestaetigungsCodeUnbekannt;
                }
            }
        }

        /*+++++++++++++++Eingaben verarbeiten++++++++++++++++++++++*/

 
        /*Hinweise Aktionärsportal gelesen*/
        if (hinweisAktionaersPortalBestaetigt) {
            eclLoginDaten.hinweisAktionaersPortalBestaetigt = 1;
        }

        /*Hinweise HVPortal gelesen*/
        if (hinweisHVPortalBestaetigt) {
            eclLoginDaten.hinweisHVPortalBestaetigt = 1;
        }

        /*Hinweise PermanentPortal gelesen*/
        if (hinweisWeitere1Bestaetigt) {
            eclLoginDaten.schreibeBestaetigenHinweisWeitere(1, 1);
        }
        if (hinweisWeitere2Bestaetigt) {
            eclLoginDaten.schreibeBestaetigenHinweisWeitere(2, 1);
        }

        /**Erst-Login erfolgt ggf. belegen*/
        long erstLoginZeitstempel = CaDatumZeit.zeitStempelMS();
        if (hinweisAktionaersPortalBestaetigt || hinweisHVPortalBestaetigt) {
            if (eclLoginDaten.registrierungAktionaersPortalErfolgt==0) {
                eclLoginDaten.registrierungAktionaersPortalErfolgt=1;
                eclLoginDaten.registrierungAktionaersPortalErfolgtZeitstempel=erstLoginZeitstempel;
            }
        }
        if (hinweisWeitere1Bestaetigt || hinweisWeitere2Bestaetigt) {
            if (eclLoginDaten.registrierungMitgliederPortalErfolgt==0) {
                eclLoginDaten.registrierungMitgliederPortalErfolgt=1;
                eclLoginDaten.registrierungMitgliederPortalErfolgtZeitstempel=erstLoginZeitstempel;
            }
        }

        CaBug.druckeLog("eMailBestaetigungBeiAllenWillenserklaerungen="+eMailBestaetigungBeiAllenWillenserklaerungen, logDrucken, 10);
       eclLoginDaten.setzeQuittungPerEmailBeiAllenWillenserklaerungen(eMailBestaetigungBeiAllenWillenserklaerungen);
 
        
        if (mailVerarbeiten()) {
            /*Registrierung für E-Einladung*/
            if (lDbBundle.param.paramPortal.registrierungFuerEmailVersandMoeglich == 1) {
                if (eVersandRegistrierung) {
                    eclLoginDaten.eVersandRegistrierung = 99;
                    if (eclLoginDaten.eVersandRegistrierungErstZeitpunkt.isEmpty()) {
                        /*Aktuelle Systemzeit = Erstregistrierungszeitpunkt*/
                        eclLoginDaten.eVersandRegistrierungErstZeitpunkt = CaDatumZeit.DatumZeitStringFuerDatenbank();
                    }
                } else {
                    eclLoginDaten.eVersandRegistrierung = 1;
                }
            }
        }

        if (passwortVerarbeiten()) {
            /*Passwort*/
            if (lDbBundle.param.paramPortal.dauerhaftesPasswortMoeglich == 1 || lDbBundle.param.paramPortal.dauerhaftesPasswortMoeglich == 2) {
                if (vergabeEigenesPasswort) {
                    /*Dann ist weiterhin ein eigenes Passwort gespeichert*/
                    eclLoginDaten.eigenesPasswort = 99;

                    if (/*Passwort ist bereits vergeben - nur neu Speichern, wenn Änderung*/
                            (vergabeEigenesPasswort && passwortNeuVergeben) ||
                            /*Passwort noch nicht vergeben - dann neues passwort*/
                            !passwortBereitsVergeben) {

                        /*Passwort übertragen*/
                        passwortVerschluesselt = CaPasswortVerschluesseln.verschluesseln(neuesPasswort);
                        eclLoginDaten.passwortVerschluesselt = passwortVerschluesselt;
                        eclLoginDaten.passwortInitial = "";
                    }
                } else {/*Kein eigenes Passwort (mehr), aber wurde bereits abgefragt - setzen*/
                    eclLoginDaten.eigenesPasswort = 1;
                }
            }
        }

        /*Email-Bestätigungscode wurde eingegeben?*/
        if (!eMailBestaetigungsCode.isEmpty()) {
            eclLoginDaten.emailBestaetigt = 1;
            quittungEmailWurdeBestaetigt=true;
        }
        else {
            quittungEmailWurdeBestaetigt=false;
        }
        
        /*E-Mail2Bestätigungscode*/
        if (!eMail2BestaetigungsCode.isEmpty()) {
            eclLoginDaten.setzeEMail2AdresseBestaetigt(true);
            quittungEmail2WurdeBestaetigt=true;
        }
        else {
            quittungEmail2WurdeBestaetigt=false;
        }

        if (mailVerarbeiten()) {
            /*Email-Adresse*/
            if (!eMailFuerVersand.isEmpty()) {
                if (!eclLoginDaten.eMailFuerVersand.equals(eMailFuerVersand)) {
                    emailBestaetigenMailVerschicken = true;
                    eclLoginDaten.emailBestaetigt = 0;
                }
                if (emailBestaetigenMailVerschicken == true) {
                    /*Email-Bestätigung veranlassen*/
                    String pwVergessenLink = lDbBundle.dbEindeutigerKey.getNextFree();
                    lDbBundle.reOpen();
                    eclLoginDaten.emailBestaetigenLink = pwVergessenLink;
                }
            }
            else {
                eclLoginDaten.emailBestaetigenLink="";
                eclLoginDaten.emailBestaetigt=0;
            }
            eclLoginDaten.eMailFuerVersand = eMailFuerVersand;

            /*Email-Adresse 2*/
            if (!eMail2FuerVersand.isEmpty()) {
                if (!eclLoginDaten.eMail2FuerVersand.equals(eMail2FuerVersand)) {
                    email2BestaetigenMailVerschicken = true;
                    eclLoginDaten.setzeEMail2AdresseBestaetigt(false);
                }
                if (email2BestaetigenMailVerschicken == true) {
                    /*Email-Bestätigung veranlassen*/
                    String pwVergessenLink = lDbBundle.dbEindeutigerKey.getNextFree();
                    lDbBundle.reOpen();
                    eclLoginDaten.email2BestaetigenLink = pwVergessenLink;
                }
            }
            else {
                eclLoginDaten.email2BestaetigenLink="";
                eclLoginDaten.setzeEMail2AdresseBestaetigt(false);
            }
            eclLoginDaten.eMail2FuerVersand = eMail2FuerVersand;
            eclLoginDaten.setzeEMail2AdresseVerwenden(eMail2Verwenden);
        }
        //		/*Kommunikationssprache*/
        //		if (CaString.isNummern(kommunikationsspracheRaw)) {
        //			eclLoginDaten.kommunikationssprache=Integer.parseInt(kommunikationsspracheRaw);
        //		}
        //		else {
        //			eclLoginDaten.kommunikationssprache=1;
        //		}

        /*++++++++++++Speichern und prüfen, ob von anderem User geändert++++++++++++*/
        int rc = lDbBundle.dbLoginDaten.update(eclLoginDaten);
        if (rc < 1) {
            return CaFehler.afAndererUserAktiv;
        }

        /*++++++++++Nun mit Alt-Daten vergleichen, Variablen für Quittungsseite setzen++++++++
         * Alte Werte bleiben ggf. bewußt erhalten, falls in diesem Schritt keine Veränderung war*/

        /*Alte Werte bleiben ggf. erhalten*/
        CaBug.druckeLog("eclLoginDaten.eVersandRegistrierung=" + eclLoginDaten.eVersandRegistrierung, logDrucken, 10);
        CaBug.druckeLog("altEVersandRegistrierung=" + altEVersandRegistriert, logDrucken, 10);
        if (eclLoginDaten.eVersandRegistriert() != altEVersandRegistriert) {
            if (eclLoginDaten.eVersandRegistrierung == 99) {
                quittungEMailVersandNeuRegistriert = true;
                quittungEMailVersandDeRegistriert = false;
            } else {
                quittungEMailVersandDeRegistriert = true;
                quittungEMailVersandNeuRegistriert = false;
            }
        }
        CaBug.druckeLog("quittungEMailVersandDeRegistriert=" + quittungEMailVersandDeRegistriert, logDrucken, 10);
        CaBug.druckeLog("quittungEMailVersandNeuRegistriert=" + quittungEMailVersandNeuRegistriert, logDrucken, 10);

        if (eclLoginDaten.liefereQuittungPerEmailBeiAllenWillenserklaerungen() != altEMailBestaetigungBeiAllenWillenserklaerungenRegistriert) {
            if (eclLoginDaten.liefereQuittungPerEmailBeiAllenWillenserklaerungen() == true) {
                quittungEMailBestaetigungBeiAllenWillenserklaerungenNeuRegistriert = true;
                quittungEMailBestaetigungBeiAllenWillenserklaerungenDeRegistriert = false;
            } else {
                quittungEMailBestaetigungBeiAllenWillenserklaerungenDeRegistriert = true;
                quittungEMailBestaetigungBeiAllenWillenserklaerungenNeuRegistriert = false;
            }
        }
        CaBug.druckeLog("quittungEMailBestaetigungBeiAllenWillenserklaerungenDeRegistriert=" + quittungEMailBestaetigungBeiAllenWillenserklaerungenDeRegistriert, logDrucken, 10);
        CaBug.druckeLog("quittungEMailBestaetigungBeiAllenWillenserklaerungenNeuRegistriert=" + quittungEMailBestaetigungBeiAllenWillenserklaerungenNeuRegistriert, logDrucken, 10);

        /*nur aktueller Stand zählt*/
        quittungEmailNochNichtBestaetigt = false;
        quittungEmail2NochNichtBestaetigt = false;
//        if (eclLoginDaten.eVersandRegistrierung == 99) {
        if (mailVerarbeiten()) {
            if (eclLoginDaten.emailBestaetigt == 0 && !eclLoginDaten.eMailFuerVersand.isEmpty()) {
                quittungEmailNochNichtBestaetigt = true;
            }
            if (eclLoginDaten.liefereEMail2AdresseBestaetigt() == false 
                    && lDbBundle.param.paramPortal.emailVersandZweitEMailAusRegister==0
                    && (!eclLoginDaten.eMail2FuerVersand.isEmpty())) {
                quittungEmail2NochNichtBestaetigt = true;
            }
         }
//        }

        /*Ggf. alter Stand bleibt erhalten*/
        if (eclLoginDaten.eigenesPasswortVergeben() != altEigenesPasswortVergeben) {
            if (eclLoginDaten.eigenesPasswort == 99) {
                quittungDauerhaftesPasswortAktiviert = true;
                quittungDauerhaftesPasswortDeAktiviert = false;
                quittungDauerhaftesPasswortGeaendert = false;
            } else {
                quittungDauerhaftesPasswortAktiviert = false;
                quittungDauerhaftesPasswortDeAktiviert = true;
                quittungDauerhaftesPasswortGeaendert = false;
            }
        } else {
            if (passwortBereitsVergeben && passwortNeuVergeben) {
                quittungDauerhaftesPasswortAktiviert = false;
                quittungDauerhaftesPasswortDeAktiviert = false;
                quittungDauerhaftesPasswortGeaendert = true;
            }
        }

        /*Ggf. alter Stand bleibt erhalten*/
        if (!eclLoginDaten.eMailFuerVersand.equals(altEMailFuerVersand)) {
            if (eclLoginDaten.eMailFuerVersand.isEmpty()) {
                quittungNeueEmailAdresseAusgetragen = true;
                quittungNeueEmailAdresseEingetragen = false;
            } else {
                quittungNeueEmailAdresseEingetragen = true;
                quittungNeueEmailAdresseAusgetragen = false;
            }
        }

        if (!eclLoginDaten.eMail2FuerVersand.equals(altEMail2FuerVersand)) {
            if (eclLoginDaten.eMail2FuerVersand.isEmpty()) {
                quittungNeueEmail2AdresseAusgetragen = true;
                quittungNeueEmail2AdresseEingetragen = false;
            } else {
                quittungNeueEmail2AdresseEingetragen = true;
                quittungNeueEmail2AdresseAusgetragen = false;
            }
        }

        /*Nur letzter Stand zählt*/
        quittungNeueEmailAdresse = eclLoginDaten.eMailFuerVersand;
        quittungNeueEmail2Adresse = eclLoginDaten.eMail2FuerVersand;

        /*E-Mail abweichend von Remote-Register?*/
        CaBug.druckeLog("quittungNeueEmailAdresse="+quittungNeueEmailAdresse+" eMailInRemoteRegister="+eMailInRemoteRegister, logDrucken, 10);
        if (quittungNeueEmailAdresse.equalsIgnoreCase(eMailInRemoteRegister)==false && quittungEmailNochNichtBestaetigt==false) {
            emailAnRemoteRegister=true;
        }
        else {
            emailAnRemoteRegister=false;
        }

        if (mailVerarbeiten()) {
            /*Wenn E-Mail eingetragen, aber noch nicht bestätigt*/
            if (!eclLoginDaten.eMailFuerVersand.isEmpty() && eclLoginDaten.emailBestaetigt==0) {
                quittungNeueEmailAdresseEingetragen=true;
            }
            if (eclLoginDaten.liefereEMail2AdresseBestaetigt() == false 
                    && lDbBundle.param.paramPortal.emailVersandZweitEMailAusRegister==0
                    && (!eclLoginDaten.eMail2FuerVersand.isEmpty())) {
                quittungNeueEmail2AdresseEingetragen=true;
            }
        }

        if (lDbBundle.param.paramPortal.bestaetigungsseiteEinstellungen == 1 && (quittungEMailVersandNeuRegistriert
                || quittungEMailVersandDeRegistriert || quittungNeueEmailAdresseEingetragen
                || quittungNeueEmailAdresseAusgetragen || quittungNeueEmail2AdresseEingetragen
                || quittungNeueEmail2AdresseAusgetragen || quittungDauerhaftesPasswortAktiviert
                || quittungDauerhaftesPasswortDeAktiviert || quittungDauerhaftesPasswortGeaendert
                || quittungEmailNochNichtBestaetigt || quittungEmail2NochNichtBestaetigt
                || quittungEmailWurdeBestaetigt || quittungEmail2WurdeBestaetigt
                )) {
            return 1; // Quittungsseite aufrufen
        }

        
        return 2;
    }
    
    /**Isolierte Prüfung des eingegebenen Passwort in neuesPasswort und neuesPasswortBestaetigung.
     * Isoliert deshalb, weil auch aus PEinstellungenAendern aufrufbar
     * 1=ok, ansonsten Fehlermeldung*/
    public int passwortEingabePruefen() {
        
        /*Überhaupt Passwort eingegeben?*/
        if (neuesPasswort.isEmpty()) {
            return CaFehler.afPasswortFehlt;
        }
        /*Passwort lang genug*/
        if (neuesPasswort.length() < lDbBundle.param.paramPortal.passwortMindestLaenge) {
            return CaFehler.afPasswortZuKurz;
        }
        /*Passwort Kombination ausreichend?*/
        int rc=CaPasswortVerschluesseln.pruefePasswortZulaessig(neuesPasswort);
        if (rc<0) {return rc;}
         /*Passwort-Bestätigung gleich?*/
        if (!neuesPasswort.equals(neuesPasswortBestaetigung)) {
            return CaFehler.afPasswortBestaetigungWeichtAb;
        }
        
        return 1;
        
    }
    
    /**Funktion zum Aufruf aus PEinstellungenAendern. Liefert neues
     * EclLoginDaten in eclLoginDaten - muß anschließend von aufrufender Funktion nach EclLoginDatenM kopiert werden.
     * 
     * pAktionaersnummer muß im Format "für intern" sein*/
    public int speichereNeuesPasswort(String pAktionaersnummer, String neuesPasswort) {
        
        lDbBundle.dbLoginDaten.read_loginKennung(pAktionaersnummer);
        if (lDbBundle.dbLoginDaten.anzErgebnis()<1) {
            CaBug.drucke("001");
            return -1;
        }
        eclLoginDaten=lDbBundle.dbLoginDaten.ergebnisPosition(0);

        passwortVerschluesselt = CaPasswortVerschluesseln.verschluesseln(neuesPasswort);
        eclLoginDaten.passwortVerschluesselt = passwortVerschluesselt;
        eclLoginDaten.passwortInitial = "";
       
        lDbBundle.dbLoginDaten.update(eclLoginDaten);

        return 1;
    }
    
    public int bestaetigungsCodeBestaetigungsSeite(String pBestaetigungscode1, String pBestaetigungscode2) {
        String lBestaetigungscode1=pBestaetigungscode1.trim().toUpperCase();
        String lBestaetigungscode2=pBestaetigungscode2.trim().toUpperCase();
        if (lBestaetigungscode1.isEmpty() && lBestaetigungscode2.isEmpty()) {
            return 1;
        }
        
        int erg = 0;
        erg = lDbBundle.dbLoginDaten.read_loginKennung(eclLoginDaten.loginKennung);
        if (erg <= 0) {
            CaBug.drucke("001");
            return (CaFehler.afKennungUnbekannt);
        }
        eclLoginDaten = lDbBundle.dbLoginDaten.ergebnisPosition(0);
        if (!lBestaetigungscode1.isEmpty()) {
            if (lBestaetigungscode1.equals(eclLoginDaten.emailBestaetigenLink)) {
                eclLoginDaten.emailBestaetigt=1;
            }
            else {
                return CaFehler.afEmailBestaetigungsCodeUnbekannt;
            }
        }
        if (!lBestaetigungscode2.isEmpty()) {
            if (lBestaetigungscode2.equals(eclLoginDaten.email2BestaetigenLink)) {
                eclLoginDaten.setzeEMail2AdresseBestaetigt(true);
            }
            else {
                return CaFehler.afEmail2BestaetigungsCodeUnbekannt;
            }
        }
        return lDbBundle.dbLoginDaten.update(eclLoginDaten);
    }

    
    private boolean mailVerarbeiten() {
        if (permPortal==true) {return true;}
        if (
                /*Email grundsätzlich nicht ausgeblendet*/
                lDbBundle.param.paramPortal.inHVPortalKeineEmailUndKeinPasswortFuerAktionaere==0
                ||
                /*Kennung ist keine Aktionärskennung, sondern Gast*/
                eclLoginDaten.kennungArt==KonstLoginKennungArt.personenNatJur
                ) {
            return true;
        }

         return false;
    }

    private boolean passwortVerarbeiten() {
        if (permPortal==true) {return true;}
        if (
                /*Email grundsätzlich nicht ausgeblendet*/
                lDbBundle.param.paramPortal.inHVPortalKeineEmailUndKeinPasswortFuerAktionaere==0
                ||
                /*Kennung ist keine Aktionärskennung, sondern Gast*/
                eclLoginDaten.kennungArt==KonstLoginKennungArt.personenNatJur
                ) {
            return true;
        }

         return false;
       
    }

}
