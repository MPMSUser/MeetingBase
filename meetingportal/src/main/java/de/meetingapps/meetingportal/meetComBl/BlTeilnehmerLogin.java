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
import de.meetingapps.meetingportal.meetComAllg.CaPasswortVerschluesseln;
import de.meetingapps.meetingportal.meetComAllg.CaString;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclAktienregister;
import de.meetingapps.meetingportal.meetComEntities.EclAktienregisterLoginDaten;
import de.meetingapps.meetingportal.meetComEntities.EclAktienregisterZusatz;
import de.meetingapps.meetingportal.meetComEntities.EclAnrede;
import de.meetingapps.meetingportal.meetComEntities.EclAppZugeordneteKennungen;
import de.meetingapps.meetingportal.meetComEntities.EclPersonenNatJur;
import de.meetingapps.meetingportal.meetComHVParam.ParamSpezial;

@Deprecated
/**Wird ersetzt durch BlTeilnehmerLoginNeu******************/

/** **************************Teilnehmer-Login Übersicht***********************************************
 * Beinhaltet Funktionen für den Login über Portal (später vielleicht auch über App - das ist aber
 * noch nicht berücksichtigt)
 * 
 * Grundkonzept - Kennung für Portal:
 * 
 * > "Vorgegebene Kennung aus Aktionärsdaten": Kennung bestehend aus reiner Zahlenreihe:
 * 		> Wenn Inhaberaktien: dann ist es die ZutrittsIdent (Eintrittskarten-Nummer)
 * 		> Wenn Namensaktien: dann ist es die Aktionärsnummer
 * 
 *  * > "Andere Person" - konkret: Bevollmächtigter:
 * 		> Kennung beginnt mit "V" und besteht aus einer beliebigen Kombination von Buchstaben und Zahlen, die vom
 * 			System vergeben wurden

 * > "Neue - vergebene Kennung":
 * 		Entweder z.B. für Bevollmächtigten vom System vergeben, oder generell vom System generiert.
 * 		> Kennung beginnt mit S und besteht aus einer beliebigen Kombination von Buchstaben und Zahlen, die vom
 * 			System vergeben wurden: => Vom System generierte Kennung, die anstelle Eintrittskartennummer oder Aktionärsnummer
 * 			nach Vergabe verwendet werden kann
 * > "Mail-Adresse"
 * 		> Kennung enthält "@": dies ist eine Email-Adresse, die auch mit Versand/Bestätigung verifiziert wurde.
 * 		Wurde vom Aktionär als "neue selbstgewählte Kennung" eingegeben.
 * 		Achtung: eine Email-Adresse kann nur EINMAL pro Mandant vergeben werden. D.h. ein Teilnehmer mit mehreren
 * 		Einträgen im Aktienregister muß sich diese Zuordnen, oder Systemgenerierte Kennungen verwenden!
 * 		Alternativ-Implementierung: System ordnet über Email-Adresse selbstständig zu. Das ist aber gefährlich,
 * 		hier muß ggf. dann die Bestätigung "ich bin" ebenfalls erfolgen (noch zu durchdenken!)
 * 
 * 
 * Grundkonzept - Passwort
 * > Grundsätzlich wird in jedem Satz, der eine Kennung (als Zugang) enthält, auch das jeweils gültige Passwort gespeichert.
 * > TODOLANG: Für den Fall, dass Passwortänderungen implementiert werden, und beim Passwortwechsel die Verwendung bereits
 * 		vorher aktiver Passworter verhindert werden soll, wird ein separates Table imnplementiert in dem Kennung, Passwort,
 * 		Gültigkeitszeitraum gespeichert werden und die beim Passwortwechsel überprüft wird.
 * 
 * 
 * Grundkonzept - Verbindung - Namensaktien
 * ----------------------------------------
 * Von Aktienregister aus:
 * 		Genau 1 Aktienregistereintrag <-> (0 bis n) Meldungen, die 1 gemeinsame PeronNatJur haben müssen
 * 		Je Meldung => genau 1 direkte PersonenNatJur (wobei mehrere Meldung auf 1 PersonenNatJur verweissen können - also
 * 						Eindeutigkeit nicht umkehrbar!
 * 
 * 
 * Grundkonzept - Verbindung - Inhaberaktien
 * -----------------------------------------
 * Meldung, die 1 zugeordnete PersonNatJur hat. I.d.R. umgkehrbar eindeutig, jedoch nicht zwangsweise.
 * 		D.h. analog zu Namensaktien, nur Verbindung / Ausgangsbasis Aktienregistereintrag fehlt. 
 * 
 * 
 * Immer gilt:
 * -----------
 * Von "Andere Person" aus:
 * 		Genau 1 PersonenNatJur <-> (0 bis n) Meldungen (direkt, oder als Bevollmächtigter)
 * 
 * 
 * 
 */
public class BlTeilnehmerLogin {

    private int logDrucken = 3;

    private DbBundle lDbBundle;

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
    public int anmeldeKennungArt = 0;

    /**Ident, im Aktienregister, auf den die Anmelde-Kennung verweist*/
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
    public long stimmen = 0;
    public String stimmenDE = "";
    /**String formatiert im Deutschen Format*/
    public String stimmenEN = "";
    /**String formatiert im Englischen Format*/

    public String anredeDE = "";
    /**Herr, Frau etc. gemäß Schlüssel*/
    public String anredeEN = "";
    /*Ab hier erst mal alle Deprecated!*/
    @Deprecated
    public String titel = "";
    @Deprecated
    public String name = "";
    @Deprecated
    public String vorname = "";
    @Deprecated
    public String name2 = ""; //NEU
    @Deprecated
    public String name3 = ""; //NEU

    @Deprecated
    public String plz = "";
    @Deprecated
    public String ort = "";
    public String landeskuerzel = "";
    /**DE*/
    public String land = "";
    /**Deutschland*/
    @Deprecated
    public String strasse = "";
    @Deprecated
    public String briefanredeDE = "";
    /**wie aus anreden-Datei*/
    @Deprecated
    public String briefanredeEN = "";
    /**wie aus Anreden Datei*/
    @Deprecated
    public String titelVornameName = "";
    /**Dr. Hans Müller**/
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

    /**Abweichende Versandadresse aus Aktienregister*/
    @Deprecated
    public int versandAbweichend = 0;
    @Deprecated
    public String nameVersand = "";
    @Deprecated
    public String vornameVersand = "";
    @Deprecated
    public String name2Versand = ""; //Neu
    @Deprecated
    public String name3Versand = ""; //Neu
    @Deprecated
    public String strasseVersand = "";
    @Deprecated
    public String postleitzahlVersand = "";
    @Deprecated
    public String ortVersand = "";
    @Deprecated
    public String landeskuerzelVersand = "";
    @Deprecated
    public String landVersand = "";

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

    /**wg. "Registrierungsinfo", Hinweise bestätigt, Kommunikationseinstellungen etc.
     * Wird aktuell nur für Aktienregister-User gefüllt, ansonsten "temporär gesetzt"
     */
    public EclAktienregisterZusatz aktienregisterZusatz = null;

    /************Ab hier: neu - nur noch diese verwenden!*********************/

    /**Daten der eingeloggten Person - noch nicht fertig, derzeit immer nur der eingetragene Aktionär*/
    /*TODO _Aktionaerskennung: Innovation - aktuell wird in die Daten immer der eingetragene Aktionär eingetragen*/
    /**1=Aktionär; 2=sonstige (ggf. noch erweitern)*/
    public int eingeloggtePersonIst = 0;
    public List<String> eingeloggtePersonListe = null;
    public List<String> eingeloggtePersonListeEN = null;

    /**Daten des Aktionärs, für den gerade Willenserkläerungen bearbeitet werden*/
    public List<String> verarbeiteterAktionaerListe = null;
    public List<String> verarbeiteterAktionaerListeEN = null;

    /**Falls Aktionär eingeloggt - dann die Adresszeilen zum Versand*/
    public List<String> adresszeilen = null;

    /**true => diese Benutzerkennung darf sich nicht am Portal anmelden*/
    public boolean anmeldenUnzulaessig = false;

    /**true => diese Benutzerkennung darf sich nicht für elektronischen Einladungsversand und nicht für 
     * ein dauerhaftes Passwort registrieren
     */
    public boolean dauerhafteRegistrierungUnzulaessig = false;

    public BlTeilnehmerLogin() {
    }

    /**Übergeben der Datenbankverbindung zur weiteren Verwendung. Initialisiert müssen mindestens folgende
     * Tables sein:
     * 
     */
    public void initDB(DbBundle pDbBundle) {
        lDbBundle = pDbBundle;
    }

    /*Daten aus Aktienregistereintrag in lokale Daten übertragen*/
    private boolean decodeAusAktienregister(EclAktienregister pAktienregisterEintrag) {

        //		lDbBundle.dbAktienregisterLoginDaten.readIdent(pAktienregisterEintrag.aktienregisterIdent);
        //		CaBug.druckeLog("pAktienregisterEintrag.aktienregisterIdent="+pAktienregisterEintrag.aktienregisterIdent, "BlTeilnehmerLogin.decodeAusAktienregister", logDrucken);
        //		if (lDbBundle.dbAktienregisterLoginDaten.anzErgebnis()==0){
        //			CaBug.druckeLog("Return bei 1", logDrucken);
        //			return false;
        //		}
        //		EclAktienregisterLoginDaten lAktienregisterLoginDaten=lDbBundle.dbAktienregisterLoginDaten.ergebnisPosition(0);
        EclAktienregisterLoginDaten lAktienregisterLoginDaten = new EclAktienregisterLoginDaten();
        ;

        anmeldePasswort = lAktienregisterLoginDaten.passwortVerschluesselt;

        anmeldenUnzulaessig = (lAktienregisterLoginDaten.anmeldenUnzulaessig == 1);
        dauerhafteRegistrierungUnzulaessig = (lAktienregisterLoginDaten.dauerhafteRegistrierungUnzulaessig == 1);

        /*
        if (lAktienregisterLoginDaten.passwortVerschluesselt.isEmpty()){
        	anmeldePasswort=lAktienregisterLoginDaten.passwortInitial;
        }
        else{
        	anmeldePasswort=lAktienregisterLoginDaten.passwortVerschluesselt;
        }
        */

        anmeldeKennungArt = 1;
        anmeldeIdentAktienregister = pAktienregisterEintrag.aktienregisterIdent;
        anmeldeNameVorname = pAktienregisterEintrag.nachname + " " + pAktienregisterEintrag.vorname;
        anmeldeOrt = pAktienregisterEintrag.ort;
        anmeldeAktionaersnummer = pAktienregisterEintrag.aktionaersnummer;

        stimmen = pAktienregisterEintrag.stimmen;
        stimmenDE = CaString.toStringDE(stimmen);
        stimmenEN = CaString.toStringEN(stimmen);
        besitzArtKuerzel = pAktienregisterEintrag.besitzart;
        switch (besitzArtKuerzel) {
        case "E": {
            besitzArt = "Eigenbesitz";
            besitzArtEN = "Proprietary Possession";
            break;
        }
        case "F": {
            besitzArt = "Fremdbesitz";
            besitzArtEN = "Minority Interests";
            break;
        }
        case "V": {
            besitzArt = "Vollmachtsbesitz";
            besitzArtEN = "Proxy Possession";
            break;
        }
        }
        gattung = pAktienregisterEintrag.getGattungId();

        titel = pAktienregisterEintrag.titel;
        name = pAktienregisterEintrag.nachname;
        vorname = pAktienregisterEintrag.vorname;
        name2 = pAktienregisterEintrag.name2;
        name3 = pAktienregisterEintrag.name3;
        ort = pAktienregisterEintrag.ort;
        if (!pAktienregisterEintrag.postleitzahlPostfach.isEmpty()
                && pAktienregisterEintrag.postleitzahlPostfach.compareTo("0") != 0) {
            plz = pAktienregisterEintrag.postleitzahlPostfach;
            strasse = "Postfach " + pAktienregisterEintrag.postfach;
        } else {
            plz = pAktienregisterEintrag.postleitzahl;
            strasse = pAktienregisterEintrag.strasse;
        }
        if (pAktienregisterEintrag.staatId != 0) {
            lDbBundle.dbStaaten.readId(pAktienregisterEintrag.staatId);
            if (lDbBundle.dbStaaten.anzErgebnis() > 0) {
                landeskuerzel = lDbBundle.dbStaaten.ergebnisPosition(0).code;
                land = lDbBundle.dbStaaten.ergebnisPosition(0).nameDE;
            }
        }

        /*Anrede füllen*/
        int anredenNr = pAktienregisterEintrag.anredeId;
        EclAnrede hAnrede = fuelleAnredenfelder(anredenNr);

        /*Kombi-Felder füllen*/
        titelVornameName = "";
        if (titel.length() != 0) {
            titelVornameName = titelVornameName + titel + " ";
        }
        if (vorname.length() != 0) {
            titelVornameName = titelVornameName + vorname + " ";
        }
        titelVornameName = titelVornameName + name;

        if (pAktienregisterEintrag.istJuristischePerson == 1) {
            nameVornameTitel = pAktienregisterEintrag.name1;
            if (!pAktienregisterEintrag.name2.isEmpty()) {
                nameVornameTitel = nameVornameTitel + " " + pAktienregisterEintrag.name2;
            }
            if (!pAktienregisterEintrag.name3.isEmpty()) {
                nameVornameTitel = nameVornameTitel + " " + pAktienregisterEintrag.name3;
            }
        } else {
            nameVornameTitel = pAktienregisterEintrag.nachname;
            if (pAktienregisterEintrag.titel.length() != 0 || pAktienregisterEintrag.vorname.length() != 0) {
                nameVornameTitel = nameVornameTitel + ",";
            }
            if (pAktienregisterEintrag.titel.length() != 0) {
                nameVornameTitel = nameVornameTitel + " " + pAktienregisterEintrag.titel;
            }
            if (pAktienregisterEintrag.vorname.length() != 0) {
                nameVornameTitel = nameVornameTitel + " " + pAktienregisterEintrag.vorname;
            }
        }

        kompletteAnredeDE = briefanredeDE;
        kompletteAnredeEN = briefanredeEN;
        if (hAnrede.istjuristischePerson != 1) {
            if (titel.length() != 0) {
                kompletteAnredeDE = kompletteAnredeDE + " " + titel;
                kompletteAnredeEN = kompletteAnredeEN + " " + titel;
            }
            if (name.length() != 0) {
                kompletteAnredeDE = kompletteAnredeDE + " " + name;
                kompletteAnredeEN = kompletteAnredeEN + " " + name;
            }
        }

        versandAbweichend = pAktienregisterEintrag.versandAbweichend;
        nameVersand = pAktienregisterEintrag.nachnameVersand;
        vornameVersand = pAktienregisterEintrag.vornameVersand;
        name2Versand = pAktienregisterEintrag.name2Versand;
        name3Versand = pAktienregisterEintrag.name3Versand;

        if (!pAktienregisterEintrag.postleitzahlPostfachVersand.isEmpty()
                && pAktienregisterEintrag.postleitzahlPostfachVersand.compareTo("0") != 0) {
            postleitzahlVersand = pAktienregisterEintrag.postleitzahlPostfachVersand;
            strasseVersand = "Postfach " + pAktienregisterEintrag.postfachVersand;
        } else {
            postleitzahlVersand = pAktienregisterEintrag.postleitzahlVersand;
            strasseVersand = pAktienregisterEintrag.strasseVersand;
        }

        ortVersand = pAktienregisterEintrag.ortVersand;
        if (pAktienregisterEintrag.staatIdVersand != 0) {
            lDbBundle.dbStaaten.readId(pAktienregisterEintrag.staatIdVersand);
            if (lDbBundle.dbStaaten.anzErgebnis() > 0) {
                landeskuerzelVersand = lDbBundle.dbStaaten.ergebnisPosition(0).code;
                landVersand = lDbBundle.dbStaaten.ergebnisPosition(0).nameDE;
            }
        }

        fuelleEingeloggtePersonAusAktienregister(pAktienregisterEintrag);
        fuelleVerarbeiteterAktionaerAusAktienregister(pAktienregisterEintrag);

        adresszeilen.add(pAktienregisterEintrag.adresszeile1);
        adresszeilen.add(pAktienregisterEintrag.adresszeile2);
        adresszeilen.add(pAktienregisterEintrag.adresszeile3);
        adresszeilen.add(pAktienregisterEintrag.adresszeile4);
        adresszeilen.add(pAktienregisterEintrag.adresszeile5);
        adresszeilen.add(pAktienregisterEintrag.adresszeile6);
        adresszeilen.add(pAktienregisterEintrag.adresszeile7);
        adresszeilen.add(pAktienregisterEintrag.adresszeile8);
        adresszeilen.add(pAktienregisterEintrag.adresszeile9);
        adresszeilen.add(pAktienregisterEintrag.adresszeile10);
        return true;
    }

    /**Anredenfelder füllen:
     * anredeDE, anredeEN, briefanredeDE, briefanredeEN
     */
    private EclAnrede fuelleAnredenfelder(int pAnredenNr) {
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
        eingeloggtePersonIst = 1;
        eingeloggtePersonListe = new ArrayList<String>();
        eingeloggtePersonListeEN = new ArrayList<String>();
        fuelleListenAusAktienregister(eingeloggtePersonListe, eingeloggtePersonListeEN, pAktienregister);
    }

    private void fuelleListenAusAktienregister(List<String> pListeDE, List<String> pListeEN,
            EclAktienregister pAktienregister) {

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

    private void fuelleVerarbeiteterAktionaerAusAktienregister(EclAktienregister pAktienregister) {
        verarbeiteterAktionaerListe = new ArrayList<String>();
        verarbeiteterAktionaerListeEN = new ArrayList<String>();
        adresszeilen = new ArrayList<String>();
        fuelleListenAusAktienregister(verarbeiteterAktionaerListe, verarbeiteterAktionaerListeEN, pAktienregister);

    }

    /*Daten aus PersonenNatJur in lokale Daten übertragen*/
    private void decodeAusPersonenNatJur(EclPersonenNatJur pPersonenNatJur) {
        anmeldePasswort = pPersonenNatJur.loginPasswort;
        anmeldeKennungArt = 3;
        anmeldeIdentPersonenNatJur = pPersonenNatJur.ident;
        anmeldeNameVorname = pPersonenNatJur.name + " " + pPersonenNatJur.vorname;
        anmeldeOrt = pPersonenNatJur.ort;
        instiIdent = pPersonenNatJur.kommunikationssprache;

        stimmen = 0;
        stimmenDE = "";
        stimmenEN = "";
        besitzArtKuerzel = "";
        besitzArt = "";
        gattung = 0;
        titel = pPersonenNatJur.titel;
        name = pPersonenNatJur.name;
        vorname = pPersonenNatJur.vorname;
        plz = pPersonenNatJur.plz;
        ort = pPersonenNatJur.ort;
        if (!pPersonenNatJur.land.isEmpty()) {
            lDbBundle.dbStaaten.readCode(pPersonenNatJur.land);
            if (lDbBundle.dbStaaten.anzErgebnis() > 0) {
                landeskuerzel = lDbBundle.dbStaaten.ergebnisPosition(0).code;
                land = lDbBundle.dbStaaten.ergebnisPosition(0).nameDE;
            }
        }
        strasse = pPersonenNatJur.strasse;

        /*Anrede füllen*/
        int anredenNr = pPersonenNatJur.anrede;
        EclAnrede hAnrede = fuelleAnredenfelder(anredenNr);

        /*Kombi-Felder füllen*/
        titelVornameName = "";
        if (titel.length() != 0) {
            titelVornameName = titelVornameName + titel + " ";
        }
        if (vorname.length() != 0) {
            titelVornameName = titelVornameName + vorname + " ";
        }
        titelVornameName = titelVornameName + name;

        nameVornameTitel = name;
        if (titel.length() != 0 || vorname.length() != 0) {
            nameVornameTitel = nameVornameTitel + ",";
        }
        if (titel.length() != 0) {
            nameVornameTitel = nameVornameTitel + " " + titel;
        }
        if (vorname.length() != 0) {
            nameVornameTitel = nameVornameTitel + " " + vorname;
        }

        kompletteAnredeDE = briefanredeDE;
        kompletteAnredeEN = briefanredeEN;
        if (hAnrede.istjuristischePerson != 1) {
            if (titel.length() != 0) {
                kompletteAnredeDE = kompletteAnredeDE + " " + titel;
                kompletteAnredeEN = kompletteAnredeEN + " " + titel;
            }
            if (name.length() != 0) {
                kompletteAnredeDE = kompletteAnredeDE + " " + name;
                kompletteAnredeEN = kompletteAnredeEN + " " + name;
            }
        }

        fuelleEingeloggtePersonAusPersonenNatJur(pPersonenNatJur);
    }

    private void fuelleEingeloggtePersonAusPersonenNatJur(EclPersonenNatJur pPersonenNatJur) {
        eingeloggtePersonIst = 2;
        eingeloggtePersonListe = new ArrayList<String>();
        eingeloggtePersonListeEN = new ArrayList<String>();
        adresszeilen = new LinkedList<String>();
        fuelleListenAusPersonenNatJur(eingeloggtePersonListe, eingeloggtePersonListeEN, pPersonenNatJur);
    }

    private void fuelleListenAusPersonenNatJur(List<String> pListeDE, List<String> pListeEN,
            EclPersonenNatJur pPersonenNatJur) {

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

    /**Findet (so vorhanden) die eingegeben Kennung und legt diese - mit Kennzeichen, Passwort etc. ab
     * true => Kennung vorhanden
     * false => Kennung nicht gefunden*/
    public boolean findeKennung(String pKennung) {
        int erg;

        CaBug.druckeLog("pKennung=" + pKennung, logDrucken, 3);
        if (pKennung.isEmpty()) {
            return (false);
        } /*Leerstring ist nicht zulässig*/
        anmeldeKennung = pKennung.toUpperCase();
        if (CaString.isNummern(BlAktienregisterNummerAufbereiten.entferneStrichTestweise(anmeldeKennung,
                lDbBundle.param.paramBasis.laengeAktionaersnummer,
                lDbBundle.clGlobalVar.mandant))) { /*Kennung enthält nur Nummern*/
            //			if (lDbBundle.param.paramBasis.namensaktienAktiv  || lDbBundle.param.paramBasis.inhaberaktienAktiv){ /*Aktionär => Verweis auf Aktienregister*/ //Früher: lDbBundle.dbBasis.paramNamensaktien - war immer true
            /*derzeit auch Inhaberaktien auf Namensaktien abgebildet - zumindest beim Login deshalb irrelevant*/
            EclAktienregister lAktienregisterEintrag = new EclAktienregister();
            CaBug.druckeLog("anmeldeKennung=" + anmeldeKennung, logDrucken, 10);
            lAktienregisterEintrag.aktionaersnummer = anmeldeKennung;
            erg = lDbBundle.dbAktienregister.leseZuAktienregisterEintrag(lAktienregisterEintrag);
            if (erg <= 0) {
                CaBug.druckeLog("Return A mit false", logDrucken, 10);
                return false;
            }

            /*Daten aus Aktienregistereintrag in lokale Daten übertragen*/
            boolean ergDecode = decodeAusAktienregister(lDbBundle.dbAktienregister.ergebnisPosition(0));
            if (ergDecode == false) {
                CaBug.druckeLog("Return B mit false", logDrucken, 10);
                return false;
            } //Kennungssatz nicht gefunden
            /*AktienregisterZusatz holen und ablegen*/
            EclAktienregisterZusatz lAktienregisterZusatz = new EclAktienregisterZusatz();
            lAktienregisterZusatz.aktienregisterIdent = lDbBundle.dbAktienregister
                    .ergebnisPosition(0).aktienregisterIdent;
            erg = lDbBundle.dbAktienregisterZusatz.read(lAktienregisterZusatz);
            if (erg < 1) {
                this.aktienregisterZusatz = new EclAktienregisterZusatz();
            } else {
                this.aktienregisterZusatz = lDbBundle.dbAktienregisterZusatz.ergebnisPosition(0);
            }

            if (ParamSpezial.ku178(lDbBundle.clGlobalVar.mandant)) {
                anmeldeKennungAufbereitet = CaString.ku178InternZuEingabe(anmeldeKennung);
            } else {
                anmeldeKennungAufbereitet = anmeldeKennung;
            }

            return true;
            //			}
            //			else{ /*Inhaberaktien => Eintrittskartennummer*/
            //				anmeldeKennungArt=2;
            //				/*TODO _Aktionaerskennung: Inhaberaktien - */
            //			}
        }

        /*In allen anderen Fällen: es liegt eine "neugenerierte Kennung" (oder selbstvergebene in Form
         * einer Email-Adresse) vor, d.h:
         * 	> Suche erst in PersonenNatJur, und wenn dort nichts gefunden wird und Namensaktien sind
         * 		dann im Aktienregister
         */
        EclPersonenNatJur lPersonenNatJur = new EclPersonenNatJur();
        lPersonenNatJur.loginKennung = anmeldeKennung;
        erg = lDbBundle.dbPersonenNatJur.leseZuPersonenNatJur(lPersonenNatJur);
        if (erg > 0) { /*Dann mit dieser Kennung jemand vorhanden - Bearbeiten*/

            /*Daten aus PersonenNatJur in lokale Daten übertragen*/
            decodeAusPersonenNatJur(lDbBundle.dbPersonenNatJur.PersonNatJurGefunden(0));

            /*Zusatz nur temporär gefüllt - muß noch erledigt werden*/
            this.aktienregisterZusatz = new EclAktienregisterZusatz();
            this.aktienregisterZusatz.eMailRegistrierung = 1;
            this.aktienregisterZusatz.eigenesPasswort = 1;
            this.aktienregisterZusatz.hinweisAktionaersPortalBestaetigt = 1;
            this.aktienregisterZusatz.hinweisHVPortalBestaetigt = 1;

            return true;
        }

        /**Eigentlich müßte das folgende überflüssig sein, da Aktienregister bereits
         * vorhin oben "abgefrühstückt" wurde.
         */
        /*Niemand mit dieser Kennung in PersonenNatJur gefunden, nun im Aktienregister suchen*/
        EclAktienregisterLoginDaten lAktienregisterLoginDaten = new EclAktienregisterLoginDaten();
        lAktienregisterLoginDaten.loginKennung = anmeldeKennung;
        erg = lDbBundle.dbAktienregisterLoginDaten.read(lAktienregisterLoginDaten);
        if (erg <= 0) {
            return false;
        }

        EclAktienregister lAktienregisterEintrag = new EclAktienregister();
        lAktienregisterEintrag.aktienregisterIdent = lDbBundle.dbAktienregisterLoginDaten
                .ergebnisPosition(0).aktienregisterIdent;

        erg = lDbBundle.dbAktienregister.leseZuAktienregisterEintrag(lAktienregisterEintrag);
        if (erg <= 0) {
            return false;
        }

        /*Daten aus Aktienregistereintrag in lokale Daten übertragen*/
        boolean ergDecode = decodeAusAktienregister(lDbBundle.dbAktienregister.ergebnisPosition(0));
        if (ergDecode == false) {
            return false;
        } //Kennungssatz nicht gefunden

        /*AktienregisterZusatz holen und ablegen*/
        EclAktienregisterZusatz lAktienregisterZusatz = new EclAktienregisterZusatz();
        lAktienregisterZusatz.aktienregisterIdent = lDbBundle.dbAktienregister.ergebnisPosition(0).aktienregisterIdent;
        erg = lDbBundle.dbAktienregisterZusatz.read(lAktienregisterZusatz);
        if (erg <= 1) {
            this.aktienregisterZusatz = new EclAktienregisterZusatz();
        } else {
            this.aktienregisterZusatz = lDbBundle.dbAktienregisterZusatz.ergebnisPosition(0);
        }

        return true;
    }

    /**Überprüfen, ob Passwort gültig für diese Kennung
     * true => Passwort gültig
     * false => Passwort ungültig
     */
    public boolean pruefePasswort(String pPasswort) {

        String eingegebenesPasswortZumVergleich = pPasswort;

        if (lDbBundle.param.paramPortal.passwortCaseSensitiv != 1) {
            eingegebenesPasswortZumVergleich = pPasswort.toUpperCase();
        }

        /*TODO _Aktionaerskennung: Passwortveschlüsselung: bei Gästen Vertretern etc. noch Passwort verschlüsselt speichern. Aus Kompatibilitätsgründen: erst mal schaun, ob unverschlüsselt gleich.
         * Außerdem wird das für die App gebraucht: hier kommt bei Folgeanmeldungen ja das Passwort bereits verschlüsselt.*/
        if (pPasswort.compareToIgnoreCase(anmeldePasswort) == 0) {
            return true;
        }
        /*Passwort verschlüsselt vergleichen*/

        anmeldePasswortVerschluesselt = CaPasswortVerschluesseln.verschluesseln(eingegebenesPasswortZumVergleich);
        if (anmeldePasswort.compareToIgnoreCase(anmeldePasswortVerschluesselt) == 0) {
            return true;
        } else {
            anmeldePasswortVerschluesselt = "";
        }

        /*
        if (lDbBundle.parameter.pPasswortCaseSensitiv==1){
        	if (pPasswort.compareTo(anmeldePasswort)==0){return true;}
        }
        else{
        	if (pPasswort.compareToIgnoreCase(anmeldePasswort)==0){return true;}
        	
        }
        */
        return (false);
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
            if (pruefePasswort(pZugeordneteKennung.passwort) == false) {
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
            if (pruefePasswort(pZugeordneteKennung.passwort) == false) {
                pZugeordneteKennung.returnVerarbeitung = -2;
                return;
            }
            break;
        }
    }

}
