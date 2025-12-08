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
import java.util.List;

import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclDateiDownload;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

@SessionScoped
@Named
public class UGastkarteSession implements Serializable {
    private static final long serialVersionUID = -6599895420858637724L;

    /**Ehemals eDlgVariablen*/
    private String gastkartePdfNr = "";

    private String hinweisPapierformat = "";

    /**1 = Versand per Email erfolgt
     * 2 = Sofortdruck
     * 3 = Stapeldruck
     */
    private String ausstellungsart = "";

    private boolean gruppenausstellung = false;

    public String getUeberschrift() {
        if (gruppenausstellung) {
            return "Neue Gruppe";
        } else {
            return "Neue Gastkarte";
        }
    }

    private boolean aenderung = false;

    public String getUeberschriftAendern() {
        if (gruppenausstellung) {
            return "Gruppen-Gastkarte überprüfen / korrigieren";
        } else {
            return "Gastkarte überprüfen / korrigieren";
        }
    }

    /**Ehemals UGastkarteAendernSession*/
    private boolean neueNrZulaessig = true;
    private boolean stornierenNrZulaessig = true;

    private String neuerAusstellungsgrundStorno = "_0";
    private String neuerAusstellungsgrundStornoKommentar = "";

    /**Anzahl VOR dem Ändern der Gastkarte; wird benötigt, um neu angefügte zu Speichern*/
    private int anzAusstellungsgrundListe = 0;
    /**Anzahl VOR dem Ändern der Gastkarte; wird benötigt, um neu angefügte zu Speichern*/
    private int anzVipKZListe = 0;

    /**Ehemals EControllerGastkartenListeSession*/
    private String reportName = "";

    /**Download-Datei der Gastkartenliste*/
    private String downloadName = "";

    /**Felder anbieten?
     * 0 = nicht anbieten, 1= anbieten, 2=zwingend auszufüllen
     * Wird aus den entsprechenden Parametern in ParamGaesteModul gefüllt und steht dann in JSF zur Verfügung
     */
    private int feldAnredeVerwenden = 1;
    private int feldTitelVerwenden = 1;
    private int feldAdelstitelVerwenden = 0;
    private int feldNameVerwenden = 2;
    private int feldVornameVerwenden = 1;
    private int feldZuHaendenVerwenden = 1;
    private int feldZusatz1Verwenden = 0;
    private int feldZusatz2Verwenden = 0;
    private int feldStrasseVerwenden = 1;
    private int feldLandVerwenden = 1;
    private int feldPLZVerwenden = 1;
    private int feldOrtVerwenden = 2;
    private int feldMailadresseVerwenden = 1;
    private int feldKommunikationsspracheVerwenden = 0;
    private int feldGruppeVerwenden = 0;
    private int feldAusstellungsgrundVerwenden = 1;
    private int feldVipVerwenden = 0;

    /**Parametrisierbare Feldbezeichnungen.
     * Wird aus den entsprechenden Parametern in ParamGaesteModul gefüllt und steht dann in JSF zur Verfügung*/
    private String feldZuHaendenBezeichnung = "Firma";
    private String feldZusatz1Bezeichnung = "zu Händen";
    private String feldZusatz2Bezeichnung = "Zusatz 2";

    /**Wird aus den entsprechenden Parametern aus ParamGaesteModul gefüllt*/
    /**Anzubietende Buttons beim Anlegen der neuen Gastkarte - Button "Speichern"*/
    private boolean buttonSpeichernAnzeigen = true;
    /**Anzubietende Buttons beim Anlegen der neuen Gastkarte - Button "Speichern" umd Sofortdrucken*/
    private boolean buttonSpeichernDruckenAnzeigen = true;

    /**Für eNeueGastkarteQuittung: wenn true, dann steht ein Link bereit, unter dem der manuelle Versand
     * der Ausstellungsbestätigung (per eigenem Email) abgerufen werden kann - in mailtext*/
    private boolean manuellerMailversand = false;

    /**siehe manuellerMailversand*/
    private String mailtext = "";

    /********************Gastkartensuche************************************/
    /**Felder für Suchmaske*/
    private String sucheGastNr = "";
    private String sucheGastName = "";
    private int anzeigeSeite = 0;
    private boolean vorwaerts = false;
    private boolean rueckwaerts = false;

    /*********Gastkarte - Blanko**********/
    private String anzahlKarten = "";

    /************Gastkarten - Insti************/
    
    /**0=Start; 1=PDFs fertig zum Download - Liste ist in instiPDFs*/
    private int instiAblauf=0;
    private List<EclDateiDownload> instiPDFs=null;
    
    /************Gastkarten - Intern*************/
    private boolean rzKartenErstellen=true;
    private boolean rzKartenErstellenErgaenzen=true;
    private String anzPortalTeamKartenErstellen="2";
    private String anzConsultantKartenErstellen="2";
    private String anzahlKundenKartenErstellen="2";
    private String anzahlNotarKartenErstellen="2";
     
    public void clear() {
    }

    public void initSuchen() {
        neuerAusstellungsgrundStorno = "_0";
        neuerAusstellungsgrundStornoKommentar = "";
        sucheGastNr = "";
        sucheGastName = "";
        anzeigeSeite = 0;
        vorwaerts = false;
        rueckwaerts = false;
    }

    public void initAendern() {
        neuerAusstellungsgrundStorno = "_0";
        neuerAusstellungsgrundStornoKommentar = "";
    }

    /**ParamGaesteModul werden in die Session-Werte dieser Bean übertragen und damit für JSF zur Verfügung gestellt.
     * Voraussetzung: dbBundle muß belegt sein.
     */
    public void init(DbBundle dbBundle) {
        feldAnredeVerwenden = dbBundle.param.paramGaesteModul.feldAnredeVerwenden;
        feldTitelVerwenden = dbBundle.param.paramGaesteModul.feldTitelVerwenden;
        feldAdelstitelVerwenden = dbBundle.param.paramGaesteModul.feldAdelstitelVerwenden;
        feldNameVerwenden = dbBundle.param.paramGaesteModul.feldNameVerwenden;
        feldVornameVerwenden = dbBundle.param.paramGaesteModul.feldVornameVerwenden;
        feldZuHaendenVerwenden = dbBundle.param.paramGaesteModul.feldZuHaendenVerwenden;
        feldZusatz1Verwenden = dbBundle.param.paramGaesteModul.feldZusatz1Verwenden;
        feldZusatz2Verwenden = dbBundle.param.paramGaesteModul.feldZusatz2Verwenden;
        feldStrasseVerwenden = dbBundle.param.paramGaesteModul.feldStrasseVerwenden;
        feldLandVerwenden = dbBundle.param.paramGaesteModul.feldLandVerwenden;
        feldPLZVerwenden = dbBundle.param.paramGaesteModul.feldPLZVerwenden;
        feldOrtVerwenden = dbBundle.param.paramGaesteModul.feldOrtVerwenden;
        feldMailadresseVerwenden = dbBundle.param.paramGaesteModul.feldMailadresseVerwenden;
        feldKommunikationsspracheVerwenden = /*dbBundle.param.paramGaesteModul.feldKommunikationsspracheVerwenden;*/1; //Wg. Insti
        feldGruppeVerwenden = dbBundle.param.paramGaesteModul.feldGruppeVerwenden;
        feldAusstellungsgrundVerwenden = dbBundle.param.paramGaesteModul.feldAusstellungsgrundVerwenden;
        feldVipVerwenden = dbBundle.param.paramGaesteModul.feldVipVerwenden;

        feldZuHaendenBezeichnung = dbBundle.param.paramGaesteModul.feldZuHaendenBezeichnung;
        feldZusatz1Bezeichnung = dbBundle.param.paramGaesteModul.feldZusatz1Bezeichnung;
        feldZusatz2Bezeichnung = dbBundle.param.paramGaesteModul.feldZusatz2Bezeichnung;

        buttonSpeichernAnzeigen = false;
        if (dbBundle.param.paramGaesteModul.buttonSpeichernAnzeigen == 1) {
            buttonSpeichernAnzeigen = true;
        }
        buttonSpeichernDruckenAnzeigen = false;
        if (dbBundle.param.paramGaesteModul.buttonSpeichernDruckenAnzeigen == 1) {
            buttonSpeichernDruckenAnzeigen = true;
        }
    }

    /****************Standard-Setter und Getter************/
    public int getFeldAnredeVerwenden() {
        return feldAnredeVerwenden;
    }

    public void setFeldAnredeVerwenden(int feldAnredeVerwenden) {
        this.feldAnredeVerwenden = feldAnredeVerwenden;
    }

    public int getFeldTitelVerwenden() {
        return feldTitelVerwenden;
    }

    public void setFeldTitelVerwenden(int feldTitelVerwenden) {
        this.feldTitelVerwenden = feldTitelVerwenden;
    }

    public int getFeldAdelstitelVerwenden() {
        return feldAdelstitelVerwenden;
    }

    public void setFeldAdelstitelVerwenden(int feldAdelstitelVerwenden) {
        this.feldAdelstitelVerwenden = feldAdelstitelVerwenden;
    }

    public int getFeldNameVerwenden() {
        return feldNameVerwenden;
    }

    public void setFeldNameVerwenden(int feldNameVerwenden) {
        this.feldNameVerwenden = feldNameVerwenden;
    }

    public int getFeldVornameVerwenden() {
        return feldVornameVerwenden;
    }

    public void setFeldVornameVerwenden(int feldVornameVerwenden) {
        this.feldVornameVerwenden = feldVornameVerwenden;
    }

    public int getFeldZuHaendenVerwenden() {
        return feldZuHaendenVerwenden;
    }

    public void setFeldZuHaendenVerwenden(int feldZuHaendenVerwenden) {
        this.feldZuHaendenVerwenden = feldZuHaendenVerwenden;
    }

    public int getFeldZusatz1Verwenden() {
        return feldZusatz1Verwenden;
    }

    public void setFeldZusatz1Verwenden(int feldZusatz1Verwenden) {
        this.feldZusatz1Verwenden = feldZusatz1Verwenden;
    }

    public int getFeldZusatz2Verwenden() {
        return feldZusatz2Verwenden;
    }

    public void setFeldZusatz2Verwenden(int feldZusatz2Verwenden) {
        this.feldZusatz2Verwenden = feldZusatz2Verwenden;
    }

    public int getFeldStrasseVerwenden() {
        return feldStrasseVerwenden;
    }

    public void setFeldStrasseVerwenden(int feldStrasseVerwenden) {
        this.feldStrasseVerwenden = feldStrasseVerwenden;
    }

    public int getFeldLandVerwenden() {
        return feldLandVerwenden;
    }

    public void setFeldLandVerwenden(int feldLandVerwenden) {
        this.feldLandVerwenden = feldLandVerwenden;
    }

    public int getFeldPLZVerwenden() {
        return feldPLZVerwenden;
    }

    public void setFeldPLZVerwenden(int feldPLZVerwenden) {
        this.feldPLZVerwenden = feldPLZVerwenden;
    }

    public int getFeldOrtVerwenden() {
        return feldOrtVerwenden;
    }

    public void setFeldOrtVerwenden(int feldOrtVerwenden) {
        this.feldOrtVerwenden = feldOrtVerwenden;
    }

    public int getFeldMailadresseVerwenden() {
        return feldMailadresseVerwenden;
    }

    public void setFeldMailadresseVerwenden(int feldMailadresseVerwenden) {
        this.feldMailadresseVerwenden = feldMailadresseVerwenden;
    }

    public int getFeldKommunikationsspracheVerwenden() {
        return feldKommunikationsspracheVerwenden;
    }

    public void setFeldKommunikationsspracheVerwenden(int feldKommunikationsspracheVerwenden) {
        this.feldKommunikationsspracheVerwenden = feldKommunikationsspracheVerwenden;
    }

    public int getFeldGruppeVerwenden() {
        return feldGruppeVerwenden;
    }

    public void setFeldGruppeVerwenden(int feldGruppeVerwenden) {
        this.feldGruppeVerwenden = feldGruppeVerwenden;
    }

    public int getFeldAusstellungsgrundVerwenden() {
        return feldAusstellungsgrundVerwenden;
    }

    public void setFeldAusstellungsgrundVerwenden(int feldAusstellungsgrundVerwenden) {
        this.feldAusstellungsgrundVerwenden = feldAusstellungsgrundVerwenden;
    }

    public int getFeldVipVerwenden() {
        return feldVipVerwenden;
    }

    public void setFeldVipVerwenden(int feldVipVerwenden) {
        this.feldVipVerwenden = feldVipVerwenden;
    }

    public String getFeldZuHaendenBezeichnung() {
        return feldZuHaendenBezeichnung;
    }

    public void setFeldZuHaendenBezeichnung(String feldZuHaendenBezeichnung) {
        this.feldZuHaendenBezeichnung = feldZuHaendenBezeichnung;
    }

    public String getFeldZusatz1Bezeichnung() {
        return feldZusatz1Bezeichnung;
    }

    public void setFeldZusatz1Bezeichnung(String feldZusatz1Bezeichnung) {
        this.feldZusatz1Bezeichnung = feldZusatz1Bezeichnung;
    }

    public String getFeldZusatz2Bezeichnung() {
        return feldZusatz2Bezeichnung;
    }

    public void setFeldZusatz2Bezeichnung(String feldZusatz2Bezeichnung) {
        this.feldZusatz2Bezeichnung = feldZusatz2Bezeichnung;
    }

    public String getMailtext() {
        return mailtext;
    }

    public void setMailtext(String mailtext) {
        this.mailtext = mailtext;
    }

    public boolean isManuellerMailversand() {
        return manuellerMailversand;
    }

    public void setManuellerMailversand(boolean manuellerMailversand) {
        this.manuellerMailversand = manuellerMailversand;
    }

    public boolean isButtonSpeichernAnzeigen() {
        return buttonSpeichernAnzeigen;
    }

    public void setButtonSpeichernAnzeigen(boolean buttonSpeichernAnzeigen) {
        this.buttonSpeichernAnzeigen = buttonSpeichernAnzeigen;
    }

    public boolean isButtonSpeichernDruckenAnzeigen() {
        return buttonSpeichernDruckenAnzeigen;
    }

    public void setButtonSpeichernDruckenAnzeigen(boolean buttonSpeichernDruckenAnzeigen) {
        this.buttonSpeichernDruckenAnzeigen = buttonSpeichernDruckenAnzeigen;
    }

    public String getGastkartePdfNr() {
        return gastkartePdfNr;
    }

    public void setGastkartePdfNr(String gastkartePdfNr) {
        this.gastkartePdfNr = gastkartePdfNr;
    }

    public String getHinweisPapierformat() {
        return hinweisPapierformat;
    }

    public void setHinweisPapierformat(String hinweisPapierformat) {
        this.hinweisPapierformat = hinweisPapierformat;
    }

    public String getAusstellungsart() {
        return ausstellungsart;
    }

    public void setAusstellungsart(String ausstellungsart) {
        this.ausstellungsart = ausstellungsart;
    }

    public boolean isGruppenausstellung() {
        return gruppenausstellung;
    }

    public void setGruppenausstellung(boolean gruppenausstellung) {
        this.gruppenausstellung = gruppenausstellung;
    }

    public boolean isAenderung() {
        return aenderung;
    }

    public void setAenderung(boolean aenderung) {
        this.aenderung = aenderung;
    }

    public boolean isNeueNrZulaessig() {
        return neueNrZulaessig;
    }

    public void setNeueNrZulaessig(boolean neueNrZulaessig) {
        this.neueNrZulaessig = neueNrZulaessig;
    }

    public boolean isStornierenNrZulaessig() {
        return stornierenNrZulaessig;
    }

    public void setStornierenNrZulaessig(boolean stornierenNrZulaessig) {
        this.stornierenNrZulaessig = stornierenNrZulaessig;
    }

    public String getNeuerAusstellungsgrundStorno() {
        return neuerAusstellungsgrundStorno;
    }

    public void setNeuerAusstellungsgrundStorno(String neuerAusstellungsgrundStorno) {
        this.neuerAusstellungsgrundStorno = neuerAusstellungsgrundStorno;
    }

    public String getNeuerAusstellungsgrundStornoKommentar() {
        return neuerAusstellungsgrundStornoKommentar;
    }

    public void setNeuerAusstellungsgrundStornoKommentar(String neuerAusstellungsgrundStornoKommentar) {
        this.neuerAusstellungsgrundStornoKommentar = neuerAusstellungsgrundStornoKommentar;
    }

    public int getAnzAusstellungsgrundListe() {
        return anzAusstellungsgrundListe;
    }

    public void setAnzAusstellungsgrundListe(int anzAusstellungsgrundListe) {
        this.anzAusstellungsgrundListe = anzAusstellungsgrundListe;
    }

    public int getAnzVipKZListe() {
        return anzVipKZListe;
    }

    public void setAnzVipKZListe(int anzVipKZListe) {
        this.anzVipKZListe = anzVipKZListe;
    }

    public String getSucheGastNr() {
        return sucheGastNr;
    }

    public void setSucheGastNr(String sucheGastNr) {
        this.sucheGastNr = sucheGastNr;
    }

    public String getSucheGastName() {
        return sucheGastName;
    }

    public void setSucheGastName(String sucheGastName) {
        this.sucheGastName = sucheGastName;
    }

    public int getAnzeigeSeite() {
        return anzeigeSeite;
    }

    public void setAnzeigeSeite(int anzeigeSeite) {
        this.anzeigeSeite = anzeigeSeite;
    }

    public boolean isVorwaerts() {
        return vorwaerts;
    }

    public void setVorwaerts(boolean vorwaerts) {
        this.vorwaerts = vorwaerts;
    }

    public boolean isRueckwaerts() {
        return rueckwaerts;
    }

    public void setRueckwaerts(boolean rueckwaerts) {
        this.rueckwaerts = rueckwaerts;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public String getDownloadName() {
        return downloadName;
    }

    public void setDownloadName(String downloadName) {
        this.downloadName = downloadName;
    }

    public String getAnzahlKarten() {
        return anzahlKarten;
    }

    public void setAnzahlKarten(String anzahlKarten) {
        this.anzahlKarten = anzahlKarten;
    }

    public boolean isRzKartenErstellen() {
        return rzKartenErstellen;
    }

    public void setRzKartenErstellen(boolean rzKartenErstellen) {
        this.rzKartenErstellen = rzKartenErstellen;
    }

    public String getAnzPortalTeamKartenErstellen() {
        return anzPortalTeamKartenErstellen;
    }

    public void setAnzPortalTeamKartenErstellen(String anzPortalTeamKartenErstellen) {
        this.anzPortalTeamKartenErstellen = anzPortalTeamKartenErstellen;
    }

    public String getAnzConsultantKartenErstellen() {
        return anzConsultantKartenErstellen;
    }

    public void setAnzConsultantKartenErstellen(String anzConsultantKartenErstellen) {
        this.anzConsultantKartenErstellen = anzConsultantKartenErstellen;
    }

    public String getAnzahlKundenKartenErstellen() {
        return anzahlKundenKartenErstellen;
    }

    public void setAnzahlKundenKartenErstellen(String anzahlKundenKartenErstellen) {
        this.anzahlKundenKartenErstellen = anzahlKundenKartenErstellen;
    }

    public String getAnzahlNotarKartenErstellen() {
        return anzahlNotarKartenErstellen;
    }

    public void setAnzahlNotarKartenErstellen(String anzahlNotarKartenErstellen) {
        this.anzahlNotarKartenErstellen = anzahlNotarKartenErstellen;
    }

    public int getInstiAblauf() {
        return instiAblauf;
    }

    public void setInstiAblauf(int instiAblauf) {
        this.instiAblauf = instiAblauf;
    }

    public List<EclDateiDownload> getInstiPDFs() {
        return instiPDFs;
    }

    public void setInstiPDFs(List<EclDateiDownload> instiPDFs) {
        this.instiPDFs = instiPDFs;
    }

    public boolean isRzKartenErstellenErgaenzen() {
        return rzKartenErstellenErgaenzen;
    }

    public void setRzKartenErstellenErgaenzen(boolean rzKartenErstellenErgaenzen) {
        this.rzKartenErstellenErgaenzen = rzKartenErstellenErgaenzen;
    }


 
}
