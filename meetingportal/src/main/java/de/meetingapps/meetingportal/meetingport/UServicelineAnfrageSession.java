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

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComEclM.EclAufgabenM;
import de.meetingapps.meetingportal.meetComEntities.EclMeldung;
import de.meetingapps.meetingportal.meetComEntities.EclSuchergebnis;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

@SessionScoped
@Named
public class UServicelineAnfrageSession implements Serializable {
    private static final long serialVersionUID = 9099396139571462572L;

    private int logDrucken=3;
    
    /**true => ein Aktionär wird angezeigt. False => Aktionärsnummer-Eingabe und ggf. Sucheingabe wird angezeigt*/
    private boolean aktionaerWirdAngezeigt = false;
    
    private boolean veranstaltungGespeichert=false;
    
    /**true => Suchergebnisse sind vorhanden. Nur, wenn aktionaerWirdAngezeigt==false*/
    private boolean suchergebnisseWerdenAngezeigt=false;

    private String aktionaersNummer = "";
    /**Suchbegriff*/
    private String aktionaersName = "";

    /**Suchergebnis*/
    private List<EclSuchergebnis> suchergebnisListe=null;
    /**Werte siehe KONST_SUCHERGEBNISART_ in UServiceLineAnfrage*/
    private int suchergebnisArt=0;
    
    /**11stellig - wie aus Aktienregisterkommend. Kann aber auch ein Vertreterzugang sein*/
    private String aktionaersNummerIntern = "";

    private int aktionaersIdent = 0;

    private int aktionaerOderGast = 0;

    private String name = "";
    private String vorname = "";
    private String strasse = "";
    private String plz = "";
    private String ort = "";

    private String aktien = "";

    private String geburtsdatum1 = "";
    private String geburtsdatum2 = "";

    private String ergaenzungGruppe = "";

    private boolean portalNutzungGesperrt = false;
    private boolean eigenesPasswortVergeben = false;
    private boolean passwortVergessenPerEmailNichtAbgeschlossen = false;

    private boolean passwortInitialAnzeigen = false;
    private String passwortInitial = "";

    private boolean dauerhafteRegistrierungUnzulaessig = false;
    private String emailHinterlegt = "";
    private boolean emailWurdeBestaetigt = false;
    private String emailBestaetigungscode = "";
    private boolean fuerEmailVersandRegistriert = false;
    
    private boolean emailEingabeMoeglich=false;
    private boolean emailEingabeAnzeigen=false;
    private String emailEingabeEMail="";
    private boolean emailEingabeRegistrierungVersand=false;
    
    private boolean passwortPerMailDialogAnzeigen=false;
    
    private String neueEmailAdresse="";

    private boolean generalversammlungAktiv = false;
    private boolean dialogveranstaltungAktiv = false;

    private boolean veranstaltungPersonenzahlEingeben=false;
    
    /**Werte siehe EclMeldung.vorlAnmeldung*/
    private int anAbGeneralversammlung = 0;
    private boolean zweiPersonenMoeglichGeneralversammlung = false;
    
    /**Nur zu verwenden im Zusammenhang mit freiwilliger Anmeldung*/
    private EclMeldung eclMeldung=null;
    
    private List<EclAufgabenM> passwortAnforderungen = null;

    /**nur für Gastkarten-Anzeige*/
    private String gastkartenNr="";
    
    public boolean liefereGastkartenNrVorhanden() {
        return !gastkartenNr.isEmpty();
    }
    
    public boolean lieferePasswortPerEmailAnzeigen() {
        return /*aktionaerOderGast == 2 &&*/ !emailHinterlegt.isEmpty();
    }

    /**Setzt alle Auswahl-Werte auf false*/
    public void init() {
        aktionaerWirdAngezeigt = false;
        veranstaltungGespeichert=false;
        suchergebnisseWerdenAngezeigt=false;
        suchergebnisListe=null;
        suchergebnisArt=0;
        
        aktionaersNummer = "";
        aktionaersName = "";
        aktionaerOderGast = 0;

        aktionaersNummerIntern = "";
        aktionaersIdent = 0;

        name = "";
        vorname = "";
        strasse = "";
        plz = "";
        ort = "";

        aktien = "";

        geburtsdatum1 = "";
        geburtsdatum2 = "";

        ergaenzungGruppe = "";

        portalNutzungGesperrt = false;
        eigenesPasswortVergeben = false;
        passwortVergessenPerEmailNichtAbgeschlossen = false;

        passwortInitialAnzeigen = false;
        passwortInitial = "";

        emailEingabeMoeglich=false;
        emailEingabeAnzeigen=false;
        emailEingabeEMail="";
        emailEingabeRegistrierungVersand=false;
        
        dauerhafteRegistrierungUnzulaessig = false;
        emailHinterlegt = "";
        emailWurdeBestaetigt = false;
        emailBestaetigungscode = "";
        fuerEmailVersandRegistriert = false;

        passwortPerMailDialogAnzeigen=false;
        
        generalversammlungAktiv = false;

        anAbGeneralversammlung = 0;
        zweiPersonenMoeglichGeneralversammlung = false;
        eclMeldung=null;
        
        gastkartenNr="";
    }

    /*************Standard-Getter und Setter**************************/

    public boolean isAktionaerWirdAngezeigt() {
        return aktionaerWirdAngezeigt;
    }

    public void setAktionaerWirdAngezeigt(boolean mitgliedWirdAngezeigt) {
        this.aktionaerWirdAngezeigt = mitgliedWirdAngezeigt;
    }

    public String getAktionaersNummer() {
        return aktionaersNummer;
    }

    public void setAktionaersNummer(String aktionaersNummer) {
        this.aktionaersNummer = aktionaersNummer;
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

    public String getStrasse() {
        return strasse;
    }

    public void setStrasse(String strasse) {
        this.strasse = strasse;
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

    public String getGeburtsdatum1() {
        return geburtsdatum1;
    }

    public void setGeburtsdatum1(String geburtsdatum1) {
        this.geburtsdatum1 = geburtsdatum1;
    }

    public String getGeburtsdatum2() {
        return geburtsdatum2;
    }

    public void setGeburtsdatum2(String geburtsdatum2) {
        this.geburtsdatum2 = geburtsdatum2;
    }

    public String getAktien() {
        return aktien;
    }

    public void setAktien(String aktien) {
        this.aktien = aktien;
    }

    public int getAnAbGeneralversammlung() {
        return anAbGeneralversammlung;
    }

    public void setAnAbGeneralversammlung(int anAbGeneralversammlung) {
        this.anAbGeneralversammlung = anAbGeneralversammlung;
    }

    public boolean isPortalNutzungGesperrt() {
        return portalNutzungGesperrt;
    }

    public void setPortalNutzungGesperrt(boolean portalNutzungGesperrt) {
        this.portalNutzungGesperrt = portalNutzungGesperrt;
    }

    public boolean isDauerhafteRegistrierungUnzulaessig() {
        return dauerhafteRegistrierungUnzulaessig;
    }

    public void setDauerhafteRegistrierungUnzulaessig(boolean dauerhafteRegistrierungUnzulaessig) {
        this.dauerhafteRegistrierungUnzulaessig = dauerhafteRegistrierungUnzulaessig;
    }

    public String getErgaenzungGruppe() {
        return ergaenzungGruppe;
    }

    public void setErgaenzungGruppe(String ergaenzungGruppe) {
        this.ergaenzungGruppe = ergaenzungGruppe;
    }

    public boolean isEigenesPasswortVergeben() {
        return eigenesPasswortVergeben;
    }

    public void setEigenesPasswortVergeben(boolean eigenesPasswortVergeben) {
        this.eigenesPasswortVergeben = eigenesPasswortVergeben;
    }

    public String getEmailHinterlegt() {
        return emailHinterlegt;
    }

    public void setEmailHinterlegt(String emailHinterlegt) {
        this.emailHinterlegt = emailHinterlegt;
    }

    public boolean isEmailWurdeBestaetigt() {
        return emailWurdeBestaetigt;
    }

    public void setEmailWurdeBestaetigt(boolean emailWurdeBestaetigt) {
        this.emailWurdeBestaetigt = emailWurdeBestaetigt;
    }

    public boolean isFuerEmailVersandRegistriert() {
        return fuerEmailVersandRegistriert;
    }

    public void setFuerEmailVersandRegistriert(boolean fuerEmailVersandRegistriert) {
        this.fuerEmailVersandRegistriert = fuerEmailVersandRegistriert;
    }

    public boolean isPasswortVergessenPerEmailNichtAbgeschlossen() {
        return passwortVergessenPerEmailNichtAbgeschlossen;
    }

    public void setPasswortVergessenPerEmailNichtAbgeschlossen(boolean passwortVergessenPerEmailNichtAbgeschlossen) {
        this.passwortVergessenPerEmailNichtAbgeschlossen = passwortVergessenPerEmailNichtAbgeschlossen;
    }

    public boolean isZweiPersonenMoeglichGeneralversammlung() {
        return zweiPersonenMoeglichGeneralversammlung;
    }

    public void setZweiPersonenMoeglichGeneralversammlung(boolean zweiPersonenMoeglichGeneralversammlung) {
        this.zweiPersonenMoeglichGeneralversammlung = zweiPersonenMoeglichGeneralversammlung;
    }

    public List<EclAufgabenM> getPasswortAnforderungen() {
        return passwortAnforderungen;
    }

    public void setPasswortAnforderungen(List<EclAufgabenM> passwortAnforderungen) {
        this.passwortAnforderungen = passwortAnforderungen;
    }

    public String getAktionaersNummerIntern() {
        return aktionaersNummerIntern;
    }

    public void setAktionaersNummerIntern(String aktionaersNummerIntern) {
        this.aktionaersNummerIntern = aktionaersNummerIntern;
    }

    public int getAktionaersIdent() {
        return aktionaersIdent;
    }

    public void setAktionaersIdent(int aktionaersIdent) {
        this.aktionaersIdent = aktionaersIdent;
    }

    public boolean isGeneralversammlungAktiv() {
        return generalversammlungAktiv;
    }

    public void setGeneralversammlungAktiv(boolean generalversammlungAktiv) {
        this.generalversammlungAktiv = generalversammlungAktiv;
    }

    public boolean isPasswortInitialAnzeigen() {
        return passwortInitialAnzeigen;
    }

    public void setPasswortInitialAnzeigen(boolean passwortInitialAnzeigen) {
        this.passwortInitialAnzeigen = passwortInitialAnzeigen;
    }

    public String getPasswortInitial() {
        return passwortInitial;
    }

    public void setPasswortInitial(String passwortInitial) {
        this.passwortInitial = passwortInitial;
    }

    public int getAktionaerOderGast() {
        return aktionaerOderGast;
    }

    public void setAktionaerOderGast(int aktionaerOderGast) {
        this.aktionaerOderGast = aktionaerOderGast;
    }

    public boolean isPasswortPerMailDialogAnzeigen() {
        return passwortPerMailDialogAnzeigen;
    }

    public void setPasswortPerMailDialogAnzeigen(boolean passwortPerMailDialogAnzeigen) {
        this.passwortPerMailDialogAnzeigen = passwortPerMailDialogAnzeigen;
    }

    public String getNeueEmailAdresse() {
        return neueEmailAdresse;
    }

    public void setNeueEmailAdresse(String neueEmailAdresse) {
        this.neueEmailAdresse = neueEmailAdresse;
    }

    public boolean isDialogveranstaltungAktiv() {
        CaBug.druckeLog("dialogveranstaltungAktiv="+dialogveranstaltungAktiv, logDrucken, 10);
        return dialogveranstaltungAktiv;
    }

    public void setDialogveranstaltungAktiv(boolean dialogveranstaltungAktiv) {
        CaBug.druckeLog("dialogveranstaltungAktiv="+dialogveranstaltungAktiv, logDrucken, 10);
        this.dialogveranstaltungAktiv = dialogveranstaltungAktiv;
    }

    public boolean isEmailEingabeMoeglich() {
        return emailEingabeMoeglich;
    }

    public void setEmailEingabeMoeglich(boolean emailEingabeMoeglich) {
        this.emailEingabeMoeglich = emailEingabeMoeglich;
    }

    public String getEmailEingabeEMail() {
        return emailEingabeEMail;
    }

    public void setEmailEingabeEMail(String emailEingabeEMail) {
        this.emailEingabeEMail = emailEingabeEMail;
    }

    public boolean isEmailEingabeRegistrierungVersand() {
        return emailEingabeRegistrierungVersand;
    }

    public void setEmailEingabeRegistrierungVersand(boolean emailEingabeRegistrierungVersand) {
        this.emailEingabeRegistrierungVersand = emailEingabeRegistrierungVersand;
    }

    public boolean isEmailEingabeAnzeigen() {
        return emailEingabeAnzeigen;
    }

    public void setEmailEingabeAnzeigen(boolean emailEingabeAnzeigen) {
        this.emailEingabeAnzeigen = emailEingabeAnzeigen;
    }

    public boolean isVeranstaltungPersonenzahlEingeben() {
        return veranstaltungPersonenzahlEingeben;
    }

    public void setVeranstaltungPersonenzahlEingeben(boolean veranstaltungPersonenzahlEingeben) {
        this.veranstaltungPersonenzahlEingeben = veranstaltungPersonenzahlEingeben;
    }

    public boolean isSuchergebnisseWerdenAngezeigt() {
        return suchergebnisseWerdenAngezeigt;
    }

    public void setSuchergebnisseWerdenAngezeigt(boolean suchergebnisseWerdenAngezeigt) {
        this.suchergebnisseWerdenAngezeigt = suchergebnisseWerdenAngezeigt;
    }

    public String getAktionaersName() {
        return aktionaersName;
    }

    public void setAktionaersName(String aktionaersName) {
        this.aktionaersName = aktionaersName;
    }

    public List<EclSuchergebnis> getSuchergebnisListe() {
        return suchergebnisListe;
    }

    public void setSuchergebnisListe(List<EclSuchergebnis> suchergebnisListe) {
        this.suchergebnisListe = suchergebnisListe;
    }

    public int getSuchergebnisArt() {
        return suchergebnisArt;
    }

    public void setSuchergebnisArt(int suchergebnisArt) {
        this.suchergebnisArt = suchergebnisArt;
    }

    public String getGastkartenNr() {
        return gastkartenNr;
    }

    public void setGastkartenNr(String gastkartenNr) {
        this.gastkartenNr = gastkartenNr;
    }

    public EclMeldung getEclMeldung() {
        return eclMeldung;
    }

    public void setEclMeldung(EclMeldung eclMeldung) {
        this.eclMeldung = eclMeldung;
    }

    public String getEmailBestaetigungscode() {
        return emailBestaetigungscode;
    }

    public void setEmailBestaetigungscode(String emailBestaetigungscode) {
        this.emailBestaetigungscode = emailBestaetigungscode;
    }

    public boolean isVeranstaltungGespeichert() {
        return veranstaltungGespeichert;
    }

    public void setVeranstaltungGespeichert(boolean veranstaltungGespeichert) {
        this.veranstaltungGespeichert = veranstaltungGespeichert;
    }

}
