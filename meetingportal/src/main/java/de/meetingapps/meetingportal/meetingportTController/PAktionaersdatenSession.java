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
package de.meetingapps.meetingportal.meetingportTController;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.meetingapps.meetingportal.meetComEntities.EclAktienregisterWeiterePerson;
import de.meetingapps.meetingportal.meetComEntities.EclStaaten;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

@SessionScoped
@Named
public class PAktionaersdatenSession implements Serializable {
    private static final long serialVersionUID = -8731081657014864085L;

    /**===================Mitgliedsstatus==============*/

    /**Nur eines der folgenden darf true sein*/
    private boolean statusFirma = false;
    private boolean statusErbengemeinschaft = false;
    private boolean statusMinderjaehrig = false;
    private boolean statusGesamthans = false;
    private boolean statusEheleuteGbr = false;
    private boolean statusNormalesMitglied = false;

    /**true => Vollmachtsempfänger sind vorhanden*/
    private boolean postempfaengerVorhanden = false;

    /**=============ZulässigeFunktionen aufgrund Mitgliedstatus======================*/
    public boolean renderErstregistrierungPWVergessenMoeglich() {
        if (statusFirma) {
            return false;
        }
        if (statusErbengemeinschaft) {
            return false;
        }
        if (statusMinderjaehrig) {
            return true;
        }
        if (statusGesamthans) {
            return true;
        } //Ggf. mehrere Geburtsdaten hinterlegt
        if (statusEheleuteGbr) {
            return true;
        }
        if (statusNormalesMitglied) {
            return true;
        }

        return false;
    }

    public boolean renderZugangsdatenNurPerPost() {
        /**Grundsätzlich gilt außerdem: vollmachtsempfaengerVorhanden dann 
         *      bei Postversand verschicken an:
         *      > alle Vollmachtsempfänger, wenn mindestens 1 vorhanden
         *      > An Mitglied, wenn keine Vollmachtsempfänger vorhanden 
         */
        if (statusFirma) {
            return true;
        } //egal, da eh nicht über Portal möglich
        if (statusErbengemeinschaft) {
            return true;
        } //egal, da eh nicht über Portal möglich
        if (statusMinderjaehrig) {
            return true;
        }
        if (statusGesamthans) {
            return true;
        }
        if (statusEheleuteGbr) {
            return false;
        }
        if (statusNormalesMitglied) {
            return false;
        }

        return false;
    }

    /**false = dürfen alles ändern*/
    public boolean renderAnschriftUndBankverbindungAendern() {
        if (statusFirma) {
            return true;
        }
        if (statusErbengemeinschaft) {
            return false;
        }
        if (statusMinderjaehrig) {
            return true;
        }
        if (statusGesamthans) {
            return false;
        }
        if (statusEheleuteGbr) {
            return true;
        }
        if (statusNormalesMitglied) {
            return true;
        }

        return false;
    }

    /**"Überstimmt" renderNurKontaktdatenAendern*/
    public boolean renderGeburtsdatumAendernZulaessig() {
        if (statusFirma) {
            return false;
        }
        if (statusErbengemeinschaft) {
            return false;
        }
        if (statusMinderjaehrig) {
            return true;
        }
        if (statusGesamthans) {
            return true;
        }
        if (statusEheleuteGbr) {
            return true;
        }
        if (statusNormalesMitglied) {
            return true;
        }

        return false;

    }

    public boolean renderErinnerungGeburtsdatumFehltAnzeigen() {
        if (statusFirma) {
            return false;
        }
        if (statusErbengemeinschaft) {
            return false;
        }
        if (statusMinderjaehrig) {
            return true;
        }
        if (statusGesamthans) {
            return true;
        }
        if (statusEheleuteGbr) {
            return true;
        }
        if (statusNormalesMitglied) {
            return true;
        }

        return false;
    }

    public boolean renderSteuerIdAendernZulaessig() {
        if (statusFirma) {
            return false;
        }
        if (statusErbengemeinschaft) {
            return false;
        }
        if (statusMinderjaehrig) {
            return true;
        }
        if (statusGesamthans) {
            return true;
        }
        if (statusEheleuteGbr) {
            return true;
        }
        if (statusNormalesMitglied) {
            return true;
        }

        return false;
    }

    public boolean renderErinnerungSteuerIdFehltAnzeigen() {
        if (statusFirma) {
            return false;
        }
        if (statusErbengemeinschaft) {
            return false;
        }
        if (statusMinderjaehrig) {
            return true;
        }
        if (statusGesamthans) {
            return true;
        }
        if (statusEheleuteGbr) {
            return true;
        }
        if (statusNormalesMitglied) {
            return true;
        }

        return false;
    }

    public boolean renderErinnerungBankverbindungFehltAnzeigen() {
        if (statusFirma) {
            return true;
        }
        if (statusErbengemeinschaft) {
            return true;
        }
        if (statusMinderjaehrig) {
            return true;
        }
        if (statusGesamthans) {
            return true;
        }
        if (statusEheleuteGbr) {
            return true;
        }
        if (statusNormalesMitglied) {
            return true;
        }

        return false;
    }

    public boolean renderNewsletterVersandBearbeitungZulaessig() {

        if (postempfaengerVorhanden) {
            return false;
        }
        return true;

    }

    /**===========================Felder für große Anzeigemaske=======================================*/
    /******************Aktionär seit*************************/
    private String aktionaerSeit = "";

    /*************************Name/Anschrift*******************/
    /**true => es befindet sich noch ein Änderungsantrag in Arbeit*/
    private boolean aktionaersdatenAenderungInArbeit = false;

    /**true => es liegt eine Statusänderung "nicht ok" für die Aktionärsdaten (Anschrift) vor*/
    private boolean aktionaersdatenAenderungFehlerhaft = false;

    private String aktionaerAnrede = "";
    public String getAktionaerAnrede() {
        return aktionaerAnrede;
    }

    public void setAktionaerAnrede(String aktionaerAnrede) {
        this.aktionaerAnrede = aktionaerAnrede;
    }

    private String aktionaerNachname = "";
    private String aktionaerVorname = "";
    private String aktionaerAdresszusatz = "";
    private String aktionaerStrasse = "";
    private String aktionaerPLZ = "";
    private String aktionaerOrt = "";
    private String aktionaerLand = "";
    private int aktionaerLandNummer = 0;

    /*************************Kontaktdaten*******************/
    /**true => es befindet sich noch ein Änderungsantrag in Arbeit*/
    private boolean telefonAenderungInArbeit = false;
    private boolean emailAenderungInArbeit = false;

    /**true => es gibt bereits einen E-Mail-Änderungsantrag (egal ob abgeschlossen oder nicht)*/
    private boolean emailAenderungVorhanden = false;

    /**true => es liegt eine Statusänderung "nicht ok" für die Kontaktdaten vor*/
    //    private boolean telefonAenderungFehlerhaft=false;

    private boolean telefon1AenderungFehlerhaft = false;
    private boolean telefon2AenderungFehlerhaft = false;
    private boolean telefon3AenderungFehlerhaft = false;
    private boolean telefon4AenderungFehlerhaft = false;

    public boolean isTelefonAenderungFehlerhaft() {
        return telefon1AenderungFehlerhaft || telefon2AenderungFehlerhaft || telefon3AenderungFehlerhaft || telefon4AenderungFehlerhaft;
    }

    private boolean emailAenderungFehlerhaft = false;

    private String aktionaerTelefon1 = ""; //telefon_privat
    private String aktionaerTelefon2 = ""; //telefon_geschaeftlich
    private String aktionaerTelefon3 = ""; //telefon_mobil
    private String aktionaerTelefon4 = ""; //telefon_mobil_geschäftlich
    private String aktionaerEmail = ""; //aus GeDix
    private String aktionaerEmailPortal = ""; //aus eclLoginDatenM
    private boolean emailWeichtAb = false; //true => GeDix und eclLoginDatenM sind unterschiedlich

    /*************************Zugeordnete Personendaten*******************/

    /**true => es befindet sich noch ein Änderungsantrag in Arbeit*/
    private boolean geburtsdatumAenderungInArbeit = false;
    private boolean steuerIdAenderungInArbeit = false;

    /**true => es liegt eine Statusänderung "nicht ok" für die Personendaten vor*/
    private boolean geburtsdatumAenderungFehlerhaft = false;
    private boolean steuerIdAenderungFehlerhaft = false;

    /**false => Geburtsdatum darf generell nicht geändert werden (auch nicht ersteingabe), weil
     * juristische Person und Erbengemeinschaft
     */
    /*Ersetzt durch renderGeburtsdatumAendernZulaessig*/
    //    private boolean geburtsdatumAendernZulaessig=false;

    /**Nur relevant, falls zugeordnetePersonVorhanden==true (sonst die Funktion in EclAktienregisterWeiterePerson nutzen)*/
    private boolean steuerIdDarfGeaendertWerden = false;
    private boolean geburtsdatumDarfGeaendertWerden = false;

    /**true => es gibt zugeordnete Person(en).
     * Kann verwendet werden, um den Stift oben (wenn nur eine Person vorhanden, sprich zugeordnetePersonVorhanden==true),
     * oder je Person (zugeordnetePersonenVorhanden==true) anzuzeigen.
     * Es ist nur ENTWEDEWR zugeordnetePersonVorhanden==true ODER zugeordnetePersonenVorhanden==true*/
    private boolean zugeordnetePersonVorhanden = false;
    private boolean zugeordnetePersonenVorhanden = false;

    /**true => Bei einer der zugeordneten Personen fehlt die SteuerId (oder es ist keine Person zugeordnet)*/
    private boolean steuerIdFehlt = false;
    /**true => Bei einer der zugeordneten Personen fehlt das Geburtsdatum (oder es ist keine Person zugeordnet)*/
    private boolean geburtsdatumFehlt = false;

    private List<EclAktienregisterWeiterePerson> zugeordneteWeiterPersonen = null;

    /*************************Bankverbindung*******************/
    /**true => es befindet sich noch ein Änderungsantrag in Arbeit*/
    private boolean bankverbindungAenderungInArbeit = false;

    /**true => es liegt eine Statusänderung "nicht ok" für die Bankverbindung vor*/
    private boolean bankverbindungAenderungFehlerhaft = false;

    /**true=> es ist keine Bankverbindung eingetragen*/
    private boolean bankverbindungFehlt = false;

    private String bankAktionaersname = "";
    private String bankBankname = "";
    private String bankIban = "";
    private String bankBic = "";

    /*************************Vollmacht, Postempfänger*******************/
    private boolean vollmachtenPostempfaengerVorhanden = false;

    private List<EclAktienregisterWeiterePerson> vollmachtenPostempfaenger = null;

    private List<EclAktienregisterWeiterePerson> postempfaenger = null;

    /**=========================Felder für Detail-Änderungsmasken========================
     * Bei dem Klick auf einen Änderungsbutton werden diese Felder gefüllt. Beim Klick auf einen
     * entsprechenden Speicherbutton werden diese Felder mit den Original-Feldern oben abgeglichen - bei
     * Änderungen werden dann die entsprechenden Requests an das Aktionenregister geschickt.
     */

    /**Bei Sondergruppen Änderung nur mit Vollmacht etc. möglich - dann ist dies auf true gesetzt.
     * Oberfläche muß dann ggf. entsprechenden Hinweis anzeigen
     */
    private boolean hinweisAenderungNurMitNachweisMoeglich = false;

    /*************************Name/Anschrift*******************/
    private boolean aendAnschriftAktiv = false;

    private String aendAdresszusatz = "";
    private String aendStrasse = "";
    private String aendPLZ = "";
    private String aendOrt = "";
    private String aendPLZGeprueft = "";
    private String aendLand = "";
    private int aendLandNummer = 0;
    private List<EclStaaten> aendLandListe = null;

    /*************************Kontaktdaten*******************/
    private boolean aendKontaktdatenAktiv = false;

    private String aendTelefon1 = ""; //telefon_privat
    private String aendTelefon2 = ""; //telefon_geschaeftlich
    private String aendTelefon3 = ""; //telefon_mobil
    private String aendTelefon4 = ""; //telefon_mobil_geschäftlicvh
    private String aendEmail = "";

    private boolean aendBestaetigungscodeEingeben = false;
    private String aendBestaetigungscode = "";
    private String aendVorgegebenerBestaetigungscode = "";

    /**Wenn true, dann wird im Bestätigungscodefenster zusätzlich der Text
     * mit der Nr. aendBestaetigungscodeZusatzText angezeigt
     */
    private boolean aendBestaetigungscodeZusatztextAnzeigen = false;
    /**i.d.R. 1654*/
    private int aendBestaetigungscodeZusatztext = 0;
    private String aendGeburtsdatum = "";

    /*******************Geburstdatum / SteuerId***********/
    /**Name und Vorname nicht zum Ändern, nur zur Info welche
     * Person sich gerade im Änderungsmodus befindet*/
    private String aendName = "";
    private String aendVorname = "";
    /**Hier wird die komplette Entity der zu ändernden Person
     * zwischengespeichert, für späteres Auslösen des Requests
     * ans Aktienregister
     */
    private EclAktienregisterWeiterePerson aendPerson = null;

    /*+++++++++Geburtsdatum+++++++++++++++++*/
    private boolean aendGeburtsdatumAktiv = false;
    /*+++++++++SteuerId+++++++++++++++++*/
    private boolean aendSteuerIdAktiv = false;
    private String aendSteuerId = "";

    /*************************Bankverbindung*******************/
    private boolean aendBankverbindungAktiv = false;

    private String aendAktionaersname = "";
    private String aendBankname = "";
    private String aendIban = "";
    /**Internes Format!*/
    private String aendIbanGeprueft = "";
    private String aendBic = "";

    
    /*************************benötigt fuer Beteiligungserhoehung*******************/
    private String allSteuerId = "";
    private String allTelefon = "";
    private String allGeburtsdatum = "";

    
    /**Setzt alle Werte auf Standardwerte zurück*/
    public void clear() {

        statusFirma = false;
        statusErbengemeinschaft = false;
        statusMinderjaehrig = false;
        statusGesamthans = false;
        statusEheleuteGbr = false;
        statusNormalesMitglied = false;

        postempfaengerVorhanden = false;

        aktionaerSeit = "";
        aktionaersdatenAenderungInArbeit = false;
        aktionaersdatenAenderungFehlerhaft = false;

        aktionaerAnrede = "";
        aktionaerNachname = "";
        aktionaerVorname = "";
        aktionaerAdresszusatz = "";
        aktionaerStrasse = "";
        aktionaerPLZ = "";
        aktionaerOrt = "";
        aktionaerLand = "";
        aktionaerLandNummer = 0;

        telefonAenderungInArbeit = false;
        emailAenderungInArbeit = false;

        telefon1AenderungFehlerhaft = false;
        telefon2AenderungFehlerhaft = false;
        telefon3AenderungFehlerhaft = false;
        telefon4AenderungFehlerhaft = false;
        emailAenderungFehlerhaft = false;

        aktionaerTelefon1 = ""; //telefon_privat
        aktionaerTelefon2 = ""; //telefon_geschaeftlich
        aktionaerTelefon3 = ""; //telefon_mobil
        aktionaerTelefon3 = ""; //telefon_mobil_geschaeftlich
        aktionaerEmail = "";

        geburtsdatumAenderungInArbeit = false;
        steuerIdAenderungInArbeit = false;

        geburtsdatumAenderungFehlerhaft = false;
        steuerIdAenderungFehlerhaft = false;

        zugeordnetePersonVorhanden = false;
        zugeordnetePersonenVorhanden = false;

        steuerIdFehlt = false;
        geburtsdatumFehlt = false;

        zugeordneteWeiterPersonen = null;

        bankverbindungAenderungInArbeit = false;

        bankverbindungAenderungFehlerhaft = false;

        bankverbindungFehlt = false;

        bankAktionaersname = "";
        bankBankname = "";
        bankIban = "";
        bankBic = "";

        vollmachtenPostempfaengerVorhanden = false;

        vollmachtenPostempfaenger = null;

        hinweisAenderungNurMitNachweisMoeglich = false;

        aendAnschriftAktiv = false;

        aendAdresszusatz = "";
        aendStrasse = "";
        aendPLZ = "";
        aendPLZGeprueft = "";
        aendOrt = "";
        aendLand = "";
        aendLandNummer = 0;
        aendLandListe = null;

        aendKontaktdatenAktiv = false;

        aendTelefon1 = ""; //telefon_privat
        aendTelefon2 = ""; //telefon_geschaeftlich
        aendTelefon3 = ""; //telefon_mobil
        aendTelefon4 = ""; //telefon_mobil_geschaeftlich
        aendEmail = "";

        aendBestaetigungscodeEingeben = false;
        aendBestaetigungscode = "";
        aendBestaetigungscodeZusatztextAnzeigen = false;
        aendBestaetigungscodeZusatztext = 0;

        aendGeburtsdatumAktiv = false;
        aendSteuerIdAktiv = false;

        aendName = "";
        aendVorname = "";
        aendGeburtsdatum = "";
        aendSteuerId = "";

        aendPerson = null;

        aendBankverbindungAktiv = false;

        aendAktionaersname = "";
        aendBankname = "";
        aendIban = "";
        aendIbanGeprueft = "";
        aendBic = "";

    }

    /*******************Spezial getter und setter*************************************/
    public String getAllGeburtsdatum() {
        String geburtsdatum = "";
        ArrayList<String> geburtsdatumListe = new ArrayList<>();
        for (EclAktienregisterWeiterePerson eclAktienregisterWeiterePerson : zugeordneteWeiterPersonen) {
            if (!eclAktienregisterWeiterePerson.geburtsdatum.equals("")) {
                geburtsdatumListe.add(eclAktienregisterWeiterePerson.geburtsdatum);
            }
            geburtsdatum += eclAktienregisterWeiterePerson.geburtsdatum + ",";
            geburtsdatum = geburtsdatum.substring(0, geburtsdatum.length() - 1);
        }
        while (geburtsdatum.startsWith(",")) {
            geburtsdatum = geburtsdatum.substring(1);
        }
        if (aktionaerNachname.contains("GbR") || aktionaerNachname.contains("Gesamthandsgemeinschaft")) {
            if (zugeordneteWeiterPersonen.size() > geburtsdatumListe.size()) {
                return "fehlt";
            } else {
                return "liegt vor";
            }
        }
        return geburtsdatum;
    }

    public String getAllSteuerId() {
        String steuerids = "";
        ArrayList<String> steueridsListe = new ArrayList<>();
        for (EclAktienregisterWeiterePerson eclAktienregisterWeiterePerson : zugeordneteWeiterPersonen) {
            if (!eclAktienregisterWeiterePerson.steuerId.equals("")) {
                steueridsListe.add(eclAktienregisterWeiterePerson.steuerId);
            }
            steuerids += eclAktienregisterWeiterePerson.steuerId + ",";
            steuerids = steuerids.substring(0, steuerids.length() - 1);
        }
        while (steuerids.startsWith(",")) {
            steuerids = steuerids.substring(1);
        }
        if (aktionaerNachname.contains("GbR") || aktionaerNachname.contains("Gesamthandsgemeinschaft")) {
            if (zugeordneteWeiterPersonen.size() > steueridsListe.size()) {
                return "fehlt";
            } else {
                return "liegt vor";
            }
        }
        return steuerids;
    }

    public String getAllTelefon() {
        String telefon = aktionaerTelefon1 + "," + aktionaerTelefon2 + "," + aktionaerTelefon3 + "," + aktionaerTelefon4;
        while (telefon.startsWith(",")) {
            telefon = telefon.substring(1);
        }
        while (telefon.endsWith(",")) {
            telefon = telefon.substring(0, telefon.length() - 1);
        }
        return telefon;
    }

    /*******************Standard getter und setter*************************************/

    public String getAktionaerSeit() {
        return aktionaerSeit;
    }

    public void setAktionaerSeit(String aktionaerSeit) {
        this.aktionaerSeit = aktionaerSeit;
    }

    public boolean isAktionaersdatenAenderungInArbeit() {
        return aktionaersdatenAenderungInArbeit;
    }

    public void setAktionaersdatenAenderungInArbeit(boolean aktionaersdatenAenderungInArbeit) {
        this.aktionaersdatenAenderungInArbeit = aktionaersdatenAenderungInArbeit;
    }

    public boolean isAktionaersdatenAenderungFehlerhaft() {
        return aktionaersdatenAenderungFehlerhaft;
    }

    public void setAktionaersdatenAenderungFehlerhaft(boolean aktionaersdatenAenderungFehlerhaft) {
        this.aktionaersdatenAenderungFehlerhaft = aktionaersdatenAenderungFehlerhaft;
    }

    public String getAktionaerNachname() {
        return aktionaerNachname;
    }

    public void setAktionaerNachname(String aktionaerNachname) {
        this.aktionaerNachname = aktionaerNachname;
    }

    public String getAktionaerVorname() {
        return aktionaerVorname;
    }

    public void setAktionaerVorname(String aktionaerVorname) {
        this.aktionaerVorname = aktionaerVorname;
    }

    public String getAktionaerAdresszusatz() {
        return aktionaerAdresszusatz;
    }

    public void setAktionaerAdresszusatz(String aktionaerAdresszusatz) {
        this.aktionaerAdresszusatz = aktionaerAdresszusatz;
    }

    public String getAktionaerStrasse() {
        return aktionaerStrasse;
    }

    public void setAktionaerStrasse(String aktionaerStrasse) {
        this.aktionaerStrasse = aktionaerStrasse;
    }

    public String getAktionaerPLZ() {
        return aktionaerPLZ;
    }

    public void setAktionaerPLZ(String aktionaerPLZ) {
        this.aktionaerPLZ = aktionaerPLZ;
    }

    public String getAktionaerOrt() {
        return aktionaerOrt;
    }

    public void setAktionaerOrt(String aktionaerOrt) {
        this.aktionaerOrt = aktionaerOrt;
    }

    public String getAktionaerLand() {
        return aktionaerLand;
    }

    public void setAktionaerLand(String aktionaerLand) {
        this.aktionaerLand = aktionaerLand;
    }

    public boolean isTelefonAenderungInArbeit() {
        return telefonAenderungInArbeit;
    }

    public void setTelefonAenderungInArbeit(boolean telefonAenderungInArbeit) {
        this.telefonAenderungInArbeit = telefonAenderungInArbeit;
    }

    public boolean isEmailAenderungInArbeit() {
        return emailAenderungInArbeit;
    }

    public void setEmailAenderungInArbeit(boolean emailAenderungInArbeit) {
        this.emailAenderungInArbeit = emailAenderungInArbeit;
    }

    public boolean isEmailAenderungFehlerhaft() {
        return emailAenderungFehlerhaft;
    }

    public void setEmailAenderungFehlerhaft(boolean emailAenderungFehlerhaft) {
        this.emailAenderungFehlerhaft = emailAenderungFehlerhaft;
    }

    public String getAktionaerTelefon1() {
        return aktionaerTelefon1;
    }

    public void setAktionaerTelefon1(String aktionaerTelefon1) {
        this.aktionaerTelefon1 = aktionaerTelefon1;
    }

    public String getAktionaerTelefon2() {
        return aktionaerTelefon2;
    }

    public void setAktionaerTelefon2(String aktionaerTelefon2) {
        this.aktionaerTelefon2 = aktionaerTelefon2;
    }

    public String getAktionaerTelefon3() {
        return aktionaerTelefon3;
    }

    public void setAktionaerTelefon3(String aktionaerTelefon3) {
        this.aktionaerTelefon3 = aktionaerTelefon3;
    }

    public String getAktionaerTelefon4() {
        return aktionaerTelefon4;
    }

    public void setAktionaerTelefon4(String aktionaerTelefon4) {
        this.aktionaerTelefon4 = aktionaerTelefon4;
    }

    public String getAktionaerEmail() {
        return aktionaerEmail;
    }

    public void setAktionaerEmail(String aktionaerEmail) {
        this.aktionaerEmail = aktionaerEmail;
    }

    public boolean isGeburtsdatumAenderungInArbeit() {
        return geburtsdatumAenderungInArbeit;
    }

    public void setGeburtsdatumAenderungInArbeit(boolean geburtsdatumAenderungInArbeit) {
        this.geburtsdatumAenderungInArbeit = geburtsdatumAenderungInArbeit;
    }

    public boolean isSteuerIdAenderungInArbeit() {
        return steuerIdAenderungInArbeit;
    }

    public void setSteuerIdAenderungInArbeit(boolean steuerIdAenderungInArbeit) {
        this.steuerIdAenderungInArbeit = steuerIdAenderungInArbeit;
    }

    public boolean isGeburtsdatumAenderungFehlerhaft() {
        return geburtsdatumAenderungFehlerhaft;
    }

    public void setGeburtsdatumAenderungFehlerhaft(boolean geburtsdatumAenderungFehlerhaft) {
        this.geburtsdatumAenderungFehlerhaft = geburtsdatumAenderungFehlerhaft;
    }

    public boolean isSteuerIdAenderungFehlerhaft() {
        return steuerIdAenderungFehlerhaft;
    }

    public void setSteuerIdAenderungFehlerhaft(boolean steuerIdAenderungFehlerhaft) {
        this.steuerIdAenderungFehlerhaft = steuerIdAenderungFehlerhaft;
    }

    public boolean isZugeordnetePersonVorhanden() {
        return zugeordnetePersonVorhanden;
    }

    public void setZugeordnetePersonVorhanden(boolean zugeordnetePersonVorhanden) {
        this.zugeordnetePersonVorhanden = zugeordnetePersonVorhanden;
    }

    public boolean isZugeordnetePersonenVorhanden() {
        return zugeordnetePersonenVorhanden;
    }

    public void setZugeordnetePersonenVorhanden(boolean zugeordnetePersonenVorhanden) {
        this.zugeordnetePersonenVorhanden = zugeordnetePersonenVorhanden;
    }

    public boolean isSteuerIdFehlt() {
        return steuerIdFehlt;
    }

    public void setSteuerIdFehlt(boolean steuerIdFehlt) {
        this.steuerIdFehlt = steuerIdFehlt;
    }

    public boolean isGeburtsdatumFehlt() {
        return geburtsdatumFehlt;
    }

    public void setGeburtsdatumFehlt(boolean geburtsdatumFehlt) {
        this.geburtsdatumFehlt = geburtsdatumFehlt;
    }

    public List<EclAktienregisterWeiterePerson> getZugeordneteWeiterPersonen() {
        return zugeordneteWeiterPersonen;
    }

    public void setZugeordneteWeiterPersonen(List<EclAktienregisterWeiterePerson> zugeordneteWeiterPersonen) {
        this.zugeordneteWeiterPersonen = zugeordneteWeiterPersonen;
    }

    public boolean isBankverbindungAenderungInArbeit() {
        return bankverbindungAenderungInArbeit;
    }

    public void setBankverbindungAenderungInArbeit(boolean bankverbindungAenderungInArbeit) {
        this.bankverbindungAenderungInArbeit = bankverbindungAenderungInArbeit;
    }

    public boolean isBankverbindungAenderungFehlerhaft() {
        return bankverbindungAenderungFehlerhaft;
    }

    public void setBankverbindungAenderungFehlerhaft(boolean bankverbindungAenderungFehlerhaft) {
        this.bankverbindungAenderungFehlerhaft = bankverbindungAenderungFehlerhaft;
    }

    public boolean isBankverbindungFehlt() {
        return bankverbindungFehlt;
    }

    public void setBankverbindungFehlt(boolean bankverbindungFehlt) {
        this.bankverbindungFehlt = bankverbindungFehlt;
    }

    public String getBankAktionaersname() {
        return bankAktionaersname;
    }

    public void setBankAktionaersname(String bankAktionaersname) {
        this.bankAktionaersname = bankAktionaersname;
    }

    public String getBankBankname() {
        return bankBankname;
    }

    public void setBankBankname(String bankBankname) {
        this.bankBankname = bankBankname;
    }

    public String getBankIban() {
        return bankIban;
    }

    public void setBankIban(String bankIban) {
        this.bankIban = bankIban;
    }

    public String getBankBic() {
        return bankBic;
    }

    public void setBankBic(String bankBic) {
        this.bankBic = bankBic;
    }

    public boolean isVollmachtenPostempfaengerVorhanden() {
        return vollmachtenPostempfaengerVorhanden;
    }

    public void setVollmachtenPostempfaengerVorhanden(boolean vollmachtenPostempfaengerVorhanden) {
        this.vollmachtenPostempfaengerVorhanden = vollmachtenPostempfaengerVorhanden;
    }

    public List<EclAktienregisterWeiterePerson> getVollmachtenPostempfaenger() {
        return vollmachtenPostempfaenger;
    }

    public void setVollmachtenPostempfaenger(List<EclAktienregisterWeiterePerson> vollmachtenPostempfaenger) {
        this.vollmachtenPostempfaenger = vollmachtenPostempfaenger;
    }

    public boolean isHinweisAenderungNurMitNachweisMoeglich() {
        return hinweisAenderungNurMitNachweisMoeglich;
    }

    public void setHinweisAenderungNurMitNachweisMoeglich(boolean hinweisAenderungNurMitNachweisMoeglich) {
        this.hinweisAenderungNurMitNachweisMoeglich = hinweisAenderungNurMitNachweisMoeglich;
    }

    public boolean isAendAnschriftAktiv() {
        return aendAnschriftAktiv;
    }

    public void setAendAnschriftAktiv(boolean aendAnschriftAktiv) {
        this.aendAnschriftAktiv = aendAnschriftAktiv;
    }

    public String getAendAdresszusatz() {
        return aendAdresszusatz;
    }

    public void setAendAdresszusatz(String aendAdresszusatz) {
        this.aendAdresszusatz = aendAdresszusatz;
    }

    public String getAendStrasse() {
        return aendStrasse;
    }

    public void setAendStrasse(String aendStrasse) {
        this.aendStrasse = aendStrasse;
    }

    public String getAendPLZ() {
        return aendPLZ;
    }

    public void setAendPLZ(String aendPLZ) {
        this.aendPLZ = aendPLZ;
    }

    public String getAendOrt() {
        return aendOrt;
    }

    public void setAendOrt(String aendOrt) {
        this.aendOrt = aendOrt;
    }

    public String getAendLand() {
        return aendLand;
    }

    public void setAendLand(String aendLand) {
        this.aendLand = aendLand;
    }

    public int getAendLandNummer() {
        return aendLandNummer;
    }

    public void setAendLandNummer(int aendLandNummer) {
        this.aendLandNummer = aendLandNummer;
    }

    public boolean isAendKontaktdatenAktiv() {
        return aendKontaktdatenAktiv;
    }

    public void setAendKontaktdatenAktiv(boolean aendKontaktdatenAktiv) {
        this.aendKontaktdatenAktiv = aendKontaktdatenAktiv;
    }

    public String getAendTelefon1() {
        return aendTelefon1;
    }

    public void setAendTelefon1(String aendTelefon1) {
        this.aendTelefon1 = aendTelefon1;
    }

    public String getAendTelefon2() {
        return aendTelefon2;
    }

    public void setAendTelefon2(String aendTelefon2) {
        this.aendTelefon2 = aendTelefon2;
    }

    public String getAendTelefon3() {
        return aendTelefon3;
    }

    public void setAendTelefon3(String aendTelefon3) {
        this.aendTelefon3 = aendTelefon3;
    }

    public String getAendTelefon4() {
        return aendTelefon4;
    }

    public void setAendTelefon4(String aendTelefon4) {
        this.aendTelefon4 = aendTelefon4;
    }

    public String getAendEmail() {
        return aendEmail;
    }

    public void setAendEmail(String aendEmail) {
        this.aendEmail = aendEmail;
    }

    public String getAendName() {
        return aendName;
    }

    public void setAendName(String aendName) {
        this.aendName = aendName;
    }

    public String getAendVorname() {
        return aendVorname;
    }

    public void setAendVorname(String aendVorname) {
        this.aendVorname = aendVorname;
    }

    public String getAendGeburtsdatum() {
        return aendGeburtsdatum;
    }

    public void setAendGeburtsdatum(String aendGeburtsdatum) {
        this.aendGeburtsdatum = aendGeburtsdatum;
    }

    public String getAendSteuerId() {
        return aendSteuerId;
    }

    public void setAendSteuerId(String aendSteuerId) {
        this.aendSteuerId = aendSteuerId;
    }

    public EclAktienregisterWeiterePerson getAendPerson() {
        return aendPerson;
    }

    public void setAendPerson(EclAktienregisterWeiterePerson aendPerson) {
        this.aendPerson = aendPerson;
    }

    public boolean isAendBankverbindungAktiv() {
        return aendBankverbindungAktiv;
    }

    public void setAendBankverbindungAktiv(boolean aendBankverbindungAktiv) {
        this.aendBankverbindungAktiv = aendBankverbindungAktiv;
    }

    public String getAendAktionaersname() {
        return aendAktionaersname;
    }

    public void setAendAktionaersname(String aendAktionaersname) {
        this.aendAktionaersname = aendAktionaersname;
    }

    public String getAendBankname() {
        return aendBankname;
    }

    public void setAendBankname(String aendBankname) {
        this.aendBankname = aendBankname;
    }

    public String getAendIban() {
        return aendIban;
    }

    public void setAendIban(String aendIban) {
        this.aendIban = aendIban;
    }

    public String getAendBic() {
        return aendBic;
    }

    public void setAendBic(String aendBic) {
        this.aendBic = aendBic;
    }

    public List<EclStaaten> getAendLandListe() {
        return aendLandListe;
    }

    public void setAendLandListe(List<EclStaaten> aendLandListe) {
        this.aendLandListe = aendLandListe;
    }

    public boolean isAendGeburtsdatumAktiv() {
        return aendGeburtsdatumAktiv;
    }

    public void setAendGeburtsdatumAktiv(boolean aendGeburtsdatumAktiv) {
        this.aendGeburtsdatumAktiv = aendGeburtsdatumAktiv;
    }

    public boolean isAendSteuerIdAktiv() {
        return aendSteuerIdAktiv;
    }

    public void setAendSteuerIdAktiv(boolean aendSteuerIdAktiv) {
        this.aendSteuerIdAktiv = aendSteuerIdAktiv;
    }

    public boolean isSteuerIdDarfGeaendertWerden() {
        return steuerIdDarfGeaendertWerden;
    }

    public void setSteuerIdDarfGeaendertWerden(boolean steuerIdDarfGeaendertWerden) {
        this.steuerIdDarfGeaendertWerden = steuerIdDarfGeaendertWerden;
    }

    public int getAktionaerLandNummer() {
        return aktionaerLandNummer;
    }

    public void setAktionaerLandNummer(int aktionaerLandNummer) {
        this.aktionaerLandNummer = aktionaerLandNummer;
    }

    public boolean isAendBestaetigungscodeEingeben() {
        return aendBestaetigungscodeEingeben;
    }

    public void setAendBestaetigungscodeEingeben(boolean aendBestaetigungscodeEingeben) {
        this.aendBestaetigungscodeEingeben = aendBestaetigungscodeEingeben;
    }

    public String getAendBestaetigungscode() {
        return aendBestaetigungscode;
    }

    public void setAendBestaetigungscode(String aendBestaetigungscode) {
        this.aendBestaetigungscode = aendBestaetigungscode;
    }

    public String getAendVorgegebenerBestaetigungscode() {
        return aendVorgegebenerBestaetigungscode;
    }

    public void setAendVorgegebenerBestaetigungscode(String aendVorgegebenerBestaetigungscode) {
        this.aendVorgegebenerBestaetigungscode = aendVorgegebenerBestaetigungscode;
    }

    public String getAktionaerEmailPortal() {
        return aktionaerEmailPortal;
    }

    public void setAktionaerEmailPortal(String aktionaerEmailPortal) {
        this.aktionaerEmailPortal = aktionaerEmailPortal;
    }

    public boolean isEmailWeichtAb() {
        return emailWeichtAb;
    }

    public void setEmailWeichtAb(boolean emailWeichtAb) {
        this.emailWeichtAb = emailWeichtAb;
    }

    public boolean isGeburtsdatumDarfGeaendertWerden() {
        return geburtsdatumDarfGeaendertWerden;
    }

    public void setGeburtsdatumDarfGeaendertWerden(boolean geburtsdatumDarfGeaendertWerden) {
        this.geburtsdatumDarfGeaendertWerden = geburtsdatumDarfGeaendertWerden;
    }

    public boolean isAendBestaetigungscodeZusatztextAnzeigen() {
        return aendBestaetigungscodeZusatztextAnzeigen;
    }

    public void setAendBestaetigungscodeZusatztextAnzeigen(boolean aendBestaetigungscodeZusatztextAnzeigen) {
        this.aendBestaetigungscodeZusatztextAnzeigen = aendBestaetigungscodeZusatztextAnzeigen;
    }

    public int getAendBestaetigungscodeZusatztext() {
        return aendBestaetigungscodeZusatztext;
    }

    public void setAendBestaetigungscodeZusatztext(int aendBestaetigungscodeZusatztext) {
        this.aendBestaetigungscodeZusatztext = aendBestaetigungscodeZusatztext;
    }

    public boolean isTelefon1AenderungFehlerhaft() {
        return telefon1AenderungFehlerhaft;
    }

    public void setTelefon1AenderungFehlerhaft(boolean telefon1AenderungFehlerhaft) {
        this.telefon1AenderungFehlerhaft = telefon1AenderungFehlerhaft;
    }

    public boolean isTelefon2AenderungFehlerhaft() {
        return telefon2AenderungFehlerhaft;
    }

    public void setTelefon2AenderungFehlerhaft(boolean telefon2AenderungFehlerhaft) {
        this.telefon2AenderungFehlerhaft = telefon2AenderungFehlerhaft;
    }

    public boolean isTelefon3AenderungFehlerhaft() {
        return telefon3AenderungFehlerhaft;
    }

    public void setTelefon3AenderungFehlerhaft(boolean telefon3AenderungFehlerhaft) {
        this.telefon3AenderungFehlerhaft = telefon3AenderungFehlerhaft;
    }

    public boolean isEmailAenderungVorhanden() {
        return emailAenderungVorhanden;
    }

    public void setEmailAenderungVorhanden(boolean emailAenderungVorhanden) {
        this.emailAenderungVorhanden = emailAenderungVorhanden;
    }

    public boolean isStatusFirma() {
        return statusFirma;
    }

    public void setStatusFirma(boolean statusFirma) {
        this.statusFirma = statusFirma;
    }

    public boolean isStatusErbengemeinschaft() {
        return statusErbengemeinschaft;
    }

    public void setStatusErbengemeinschaft(boolean statusErbengemeinschaft) {
        this.statusErbengemeinschaft = statusErbengemeinschaft;
    }

    public boolean isStatusMinderjaehrig() {
        return statusMinderjaehrig;
    }

    public void setStatusMinderjaehrig(boolean statusMinderjaehrig) {
        this.statusMinderjaehrig = statusMinderjaehrig;
    }

    public boolean isStatusGesamthans() {
        return statusGesamthans;
    }

    public void setStatusGesamthans(boolean statusGesamthans) {
        this.statusGesamthans = statusGesamthans;
    }

    public boolean isStatusNormalesMitglied() {
        return statusNormalesMitglied;
    }

    public void setStatusNormalesMitglied(boolean statusNormalesMitglied) {
        this.statusNormalesMitglied = statusNormalesMitglied;
    }

    public boolean isStatusEheleuteGbr() {
        return statusEheleuteGbr;
    }

    public void setStatusEheleuteGbr(boolean statusEheleuteGbr) {
        this.statusEheleuteGbr = statusEheleuteGbr;
    }

    public boolean isPostempfaengerVorhanden() {
        return postempfaengerVorhanden;
    }

    public void setPostempfaengerVorhanden(boolean postempfaengerVorhanden) {
        this.postempfaengerVorhanden = postempfaengerVorhanden;
    }

    public boolean isTelefon4AenderungFehlerhaft() {
        return telefon4AenderungFehlerhaft;
    }

    public void setTelefon4AenderungFehlerhaft(boolean telefon4AenderungFehlerhaft) {
        this.telefon4AenderungFehlerhaft = telefon4AenderungFehlerhaft;
    }

    public String getAendIbanGeprueft() {
        return aendIbanGeprueft;
    }

    public void setAendIbanGeprueft(String aendIbanGeprueft) {
        this.aendIbanGeprueft = aendIbanGeprueft;
    }

    public String getAendPLZGeprueft() {
        return aendPLZGeprueft;
    }

    public void setAendPLZGeprueft(String aendPLZGeprueft) {
        this.aendPLZGeprueft = aendPLZGeprueft;
    }

    public List<EclAktienregisterWeiterePerson> getPostempfaenger() {
        return postempfaenger;
    }

    public void setPostempfaenger(List<EclAktienregisterWeiterePerson> postempfaenger) {
        this.postempfaenger = postempfaenger;
    }

    public void setAllSteuerId(String allSteuerId) {
        this.allSteuerId = allSteuerId;
    }

    public void setAllTelefon(String allTelefon) {
        this.allTelefon = allTelefon;
    }

    public void setAllGeburtsdatum(String allGeburtsdatum) {
        this.allGeburtsdatum = allGeburtsdatum;
    }

}
