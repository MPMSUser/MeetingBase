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

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;


@SessionScoped
@Named
public class PBeteiligungserhoehungSession implements Serializable {
    private static final long serialVersionUID = 1305859370443162385L;

    private double erhoehungsbetrag = 0.0;
    private String erhoehungsbetragString = "";
    
    private int weitereAnteile = 0;
    private String weitereAnteileString = "";
    
    
    /** 
     * 1 = digital
     * 2 = analog
     * **/
    private String signaturArt = "0";
    
    /** 
     * 0 = start
     * 1 = Beteiligungserhöhung
     * 2 = Teilauffuellbetrag
     * 3 = Beteiligungserhöhung + Auffüllung
     * 4 = Zahlungsart
     * **/
    private String beteiligungserhoehungStep = "0";
    private String beteiligungserhoehungWeiterStep = "0";
    private String beteiligungserhoehungStepPrev = "0";
    
    private double auffuellBetragZuUeberweisen = 0.0;
    private String auffuellBetragZuUeberweisenString = "0,00";
    
    private double nichtVerwendbarerBetrag = 0.0;
    private String nichtVerwendbarerBetragString = "0,00";
    
    private double verwendbarerBetragAnteile = 0.0;
    private String verwendbarerBetragAnteileString = "0,00";
    
    private double verwendbarerBetragAnteileGesamt = 0.0;
    private String verwendbarerBetragAnteileGesamtString = "0,00";
    
    private double optimalerBetrag = 0.0;
    private String optimalerBetragString = "0,00";
    
    private double verwendbarerBetragGesamt = 0.0;
    private String verwendbarerBetragGesamtString = "0,00";
    
    private double co2gespart = 0.0;
    private String co2gespartString = "0,00";
    
    private Boolean berechneteDatenAnzeigen = false;
    private Boolean berechneteDetailsAnzeigen = false;
    
    private int geschaeftsanteileNeu = 0;
    private String geschaeftsanteileNeuDE = "";
    
    private double geschaeftsguthabenNominalNeu = 0.0;
    private String geschaeftsguthabenNominalNeuDE = "0,00";
    
    private double geschaeftsguthabenAktuellNeu = 0.0;
    private String geschaeftsguthabenAktuellNeuDE = "0,00";
    
    private double auffuellBetragNeu = 0.0;
    private String auffuellBetragNeuDE = "0,00";
    
    private Boolean showHerunterladen = false;
    private Boolean showDigitaleSignaturGestartet = false;
    
    private Boolean confirmPayment = false;
    
    private byte[] pdf = null;
    
    private String btnBerechnenText = "Berechnen";
    
    /**
     * 0 = Ueberweisung
     * 1 = SEPA
     */
    private String zahlungsart = "0";
    
    private Boolean sepaKontoAbweichend = false;
    
    private Boolean sepaEinzugErmaechtigung = false;
    
    private Boolean einwilligungBeteiligung = false;
    
    /*
     * 0 = ""
     * 1 = Herr
     * 2 = Frau
     * 4 = divers
     * 3 = Firma
     * */
    private String sepaKontoAbweichendAnrede = "";
    
    private String sepaKontoAbweichendTitel = "";
    
    private String sepaKontoAbweichendVorname = "";
    
    private String sepaKontoAbweichendNachname = "";
    
    private String sepaKontoAbweichendBank = "";
    
    private String sepaKontoAbweichendIBAN = "";
    
    private String sepaKontoAbweichendBIC = "";
    
    private String sepaKontoAbweichendStrasse = "";  
    
    private String sepaKontoAbweichendPLZ = ""; 
    
    private String sepaKontoAbweichendOrt = ""; 
    
    private String sepaKontoAbweichendLand = "";
    
    private String sepaKontoAbweichendEmail = "";
    
    private String sepaKontoSelfTempVorname = "";
    
    private String sepaKontoSelfTempNachname = "";
    
    private String sepaKontoSelfTempBank = "";
    
    private String sepaKontoSelfTempIBAN = "";
    
    private String sepaKontoSelfTempBIC = "";
    
    private Boolean okWeitereAnteile = false;
    
    private Boolean okSatzung = false;
    
    private Boolean okDatenschutz = false;
    
    private Boolean okBeiMinderjaehrig = false;
    
    /**Setzt alle Werte auf Standardwerte zurück*/
    public void clear() {
        erhoehungsbetrag = 0.0;
        erhoehungsbetragString = "";
        signaturArt = "0";
        weitereAnteile = 0;
        weitereAnteileString = "";
        beteiligungserhoehungStep = "0";
        beteiligungserhoehungWeiterStep = "0";
        beteiligungserhoehungStepPrev = "0";
        auffuellBetragZuUeberweisen = 0.0;
        auffuellBetragZuUeberweisenString = "0,00";
        nichtVerwendbarerBetrag = 0.0;
        nichtVerwendbarerBetragString = "0,00";
        verwendbarerBetragAnteile = 0.0;
        verwendbarerBetragAnteileString = "0,00";
        verwendbarerBetragAnteileGesamt = 0.0;
        verwendbarerBetragAnteileGesamtString = "0,00";
        verwendbarerBetragGesamt = 0.0;
        verwendbarerBetragGesamtString = "0,00";
        optimalerBetrag = 0.0;
        optimalerBetragString = "0,00";
        co2gespart = 0.0;
        co2gespartString = "0,00";
        berechneteDatenAnzeigen = false;
        berechneteDetailsAnzeigen = false;
        geschaeftsanteileNeu = 0;
        geschaeftsanteileNeuDE = "";
        
        geschaeftsguthabenNominalNeu = 0;
        geschaeftsguthabenNominalNeuDE = "";
        
        geschaeftsguthabenAktuellNeu = 0;
        geschaeftsguthabenAktuellNeuDE = "";
        
        auffuellBetragNeu = 0;
        auffuellBetragNeuDE = "";
        
        showHerunterladen = false;
        showDigitaleSignaturGestartet = false;
        
        pdf = null;
        
        btnBerechnenText = "Berechnen";
        
        zahlungsart = "0";        
        sepaKontoAbweichend = false;
        sepaKontoAbweichendAnrede = "";
        sepaKontoAbweichendTitel = "";
        sepaKontoAbweichendVorname = "";       
        sepaKontoAbweichendNachname = ""; 
        sepaKontoAbweichendStrasse = "";       
        sepaKontoAbweichendPLZ = ""; 
        sepaKontoAbweichendOrt = ""; 
        sepaKontoAbweichendBank = "";
        sepaKontoAbweichendIBAN = "";  
        sepaKontoAbweichendBIC = "";
        sepaKontoAbweichendLand = "";
        sepaKontoAbweichendEmail = "";
        sepaKontoSelfTempBank = "";
        sepaKontoSelfTempBIC = "";
        sepaKontoSelfTempIBAN = "";
        sepaKontoSelfTempNachname = "";
        sepaKontoSelfTempVorname = "";
        confirmPayment = false;
        okWeitereAnteile = false;
        okSatzung = false;
        okDatenschutz = false;
        okBeiMinderjaehrig = false;
    }
    
    /*******************Spezial getter und setter***************************/
    
    /*******************Standard getter und setter***************************/

    public double getErhoehungsbetrag() {
        return erhoehungsbetrag;
    }

    public void setErhoehungsbetrag(double erhoehungsbetrag) {
        this.erhoehungsbetrag = erhoehungsbetrag;
    }

    public String getBeteiligungserhoehungStep() {
        return beteiligungserhoehungStep;
    }

    public void setBeteiligungserhoehungStep(String beteiligungserhoehungStep) {
        this.beteiligungserhoehungStep = beteiligungserhoehungStep;
    }

    public double getAuffuellBetragZuUeberweisen() {
        return auffuellBetragZuUeberweisen;
    }

    public void setAuffuellBetragZuUeberweisen(double auffuellBetragZuUeberweisen) {
        this.auffuellBetragZuUeberweisen = auffuellBetragZuUeberweisen;
    }

    public String getErhoehungsbetragString() {
        return erhoehungsbetragString;
    }

    public void setErhoehungsbetragString(String erhoehungsbetragString) {
        this.erhoehungsbetragString = erhoehungsbetragString;
    }

    public String getAuffuellBetragZuUeberweisenString() {
        return auffuellBetragZuUeberweisenString;
    }

    public void setAuffuellBetragZuUeberweisenString(String auffuellBetragZuUeberweisenString) {
        this.auffuellBetragZuUeberweisenString = auffuellBetragZuUeberweisenString;
    }

    public Boolean getBerechneteDatenAnzeigen() {
        return berechneteDatenAnzeigen;
    }

    public void setBerechneteDatenAnzeigen(Boolean berechneteDatenAnzeigen) {
        this.berechneteDatenAnzeigen = berechneteDatenAnzeigen;
    }

    public double getCo2gespart() {
        return co2gespart;
    }

    public void setCo2gespart(double co2gespart) {
        this.co2gespart = co2gespart;
    }

    public String getCo2gespartString() {
        return co2gespartString;
    }

    public void setCo2gespartString(String co2gespartString) {
        this.co2gespartString = co2gespartString;
    }

    public int getGeschaeftsanteileNeu() {
        return geschaeftsanteileNeu;
    }

    public void setGeschaeftsanteileNeu(int geschaeftsanteileNeu) {
        this.geschaeftsanteileNeu = geschaeftsanteileNeu;
    }

    public String getGeschaeftsanteileNeuDE() {
        return geschaeftsanteileNeuDE;
    }

    public void setGeschaeftsanteileNeuDE(String geschaeftsanteileNeuDE) {
        this.geschaeftsanteileNeuDE = geschaeftsanteileNeuDE;
    }

    public double getGeschaeftsguthabenNominalNeu() {
        return geschaeftsguthabenNominalNeu;
    }

    public void setGeschaeftsguthabenNominalNeu(double geschaeftsguthabenNominalNeu) {
        this.geschaeftsguthabenNominalNeu = geschaeftsguthabenNominalNeu;
    }

    public String getGeschaeftsguthabenNominalNeuDE() {
        return geschaeftsguthabenNominalNeuDE;
    }

    public void setGeschaeftsguthabenNominalNeuDE(String geschaeftsguthabenNominalNeuDE) {
        this.geschaeftsguthabenNominalNeuDE = geschaeftsguthabenNominalNeuDE;
    }

    public double getGeschaeftsguthabenAktuellNeu() {
        return geschaeftsguthabenAktuellNeu;
    }

    public void setGeschaeftsguthabenAktuellNeu(double geschaeftsguthabenAktuellNeu) {
        this.geschaeftsguthabenAktuellNeu = geschaeftsguthabenAktuellNeu;
    }

    public String getGeschaeftsguthabenAktuellNeuDE() {
        return geschaeftsguthabenAktuellNeuDE;
    }

    public void setGeschaeftsguthabenAktuellNeuDE(String geschaeftsguthabenAktuellNeuDE) {
        this.geschaeftsguthabenAktuellNeuDE = geschaeftsguthabenAktuellNeuDE;
    }

    public double getAuffuellBetragNeu() {
        return auffuellBetragNeu;
    }

    public void setAuffuellBetragNeu(double auffuellBetragNeu) {
        this.auffuellBetragNeu = auffuellBetragNeu;
    }

    public String getAuffuellBetragNeuDE() {
        return auffuellBetragNeuDE;
    }

    public void setAuffuellBetragNeuDE(String auffuellBetragNeuDE) {
        this.auffuellBetragNeuDE = auffuellBetragNeuDE;
    }

    public double getNichtVerwendbarerBetrag() {
        return nichtVerwendbarerBetrag;
    }

    public void setNichtVerwendbarerBetrag(double nichtVerwendbarerBetrag) {
        this.nichtVerwendbarerBetrag = nichtVerwendbarerBetrag;
    }

    public String getNichtVerwendbarerBetragString() {
        return nichtVerwendbarerBetragString;
    }

    public void setNichtVerwendbarerBetragString(String nichtVerwendbarerBetragString) {
        this.nichtVerwendbarerBetragString = nichtVerwendbarerBetragString;
    }

    public String getBeteiligungserhoehungWeiterStep() {
        return beteiligungserhoehungWeiterStep;
    }

    public void setBeteiligungserhoehungWeiterStep(String beteiligungserhoehungWeiterStep) {
        this.beteiligungserhoehungWeiterStep = beteiligungserhoehungWeiterStep;
    }

    public int getWeitereAnteile() {
        return weitereAnteile;
    }

    public void setWeitereAnteile(int weitereAnteile) {
        this.weitereAnteile = weitereAnteile;
    }

    public String getSignaturArt() {
        return signaturArt;
    }

    public void setSignaturArt(String signaturArt) {
        this.signaturArt = signaturArt;
    }

    public String getBeteiligungserhoehungStepPrev() {
        return beteiligungserhoehungStepPrev;
    }

    public void setBeteiligungserhoehungStepPrev(String beteiligungserhoehungStepPrev) {
        this.beteiligungserhoehungStepPrev = beteiligungserhoehungStepPrev;
    }

    public Boolean getShowHerunterladen() {
        return showHerunterladen;
    }

    public void setShowHerunterladen(Boolean showHerunterladen) {
        this.showHerunterladen = showHerunterladen;
    }

    public Boolean getBerechneteDetailsAnzeigen() {
        return berechneteDetailsAnzeigen;
    }

    public void setBerechneteDetailsAnzeigen(Boolean berechneteDetailsAnzeigen) {
        this.berechneteDetailsAnzeigen = berechneteDetailsAnzeigen;
    }

    public double getVerwendbarerBetragAnteile() {
        return verwendbarerBetragAnteile;
    }

    public void setVerwendbarerBetragAnteile(double verwendbarerBetragAnteile) {
        this.verwendbarerBetragAnteile = verwendbarerBetragAnteile;
    }

    public String getVerwendbarerBetragAnteileString() {
        return verwendbarerBetragAnteileString;
    }

    public void setVerwendbarerBetragAnteileString(String verwendbarerBetragAnteileString) {
        this.verwendbarerBetragAnteileString = verwendbarerBetragAnteileString;
    }

    public double getVerwendbarerBetragGesamt() {
        return verwendbarerBetragGesamt;
    }

    public void setVerwendbarerBetragGesamt(double verwendbarerBetragGesamt) {
        this.verwendbarerBetragGesamt = verwendbarerBetragGesamt;
    }

    public String getVerwendbarerBetragGesamtString() {
        return verwendbarerBetragGesamtString;
    }

    public void setVerwendbarerBetragGesamtString(String verwendbarerBetragGesamtString) {
        this.verwendbarerBetragGesamtString = verwendbarerBetragGesamtString;
    }

    public double getOptimalerBetrag() {
        return optimalerBetrag;
    }

    public void setOptimalerBetrag(double optimalerBetrag) {
        this.optimalerBetrag = optimalerBetrag;
    }

    public String getOptimalerBetragString() {
        return optimalerBetragString;
    }

    public void setOptimalerBetragString(String optimalerBetragString) {
        this.optimalerBetragString = optimalerBetragString;
    }

    public byte[] getPdf() {
        return pdf;
    }

    public void setPdf(byte[] pdf) {
        this.pdf = pdf;
    }

    public double getVerwendbarerBetragAnteileGesamt() {
        return verwendbarerBetragAnteileGesamt;
    }

    public void setVerwendbarerBetragAnteileGesamt(double verwendbarerBetragAnteileGesamt) {
        this.verwendbarerBetragAnteileGesamt = verwendbarerBetragAnteileGesamt;
    }

    public String getVerwendbarerBetragAnteileGesamtString() {
        return verwendbarerBetragAnteileGesamtString;
    }

    public void setVerwendbarerBetragAnteileGesamtString(String verwendbarerBetragAnteileGesamtString) {
        this.verwendbarerBetragAnteileGesamtString = verwendbarerBetragAnteileGesamtString;
    }

    public String getBtnBerechnenText() {
        return btnBerechnenText;
    }

    public void setBtnBerechnenText(String btnBerechnenText) {
        this.btnBerechnenText = btnBerechnenText;
    }

    public String getWeitereAnteileString() {
        return weitereAnteileString;
    }

    public void setWeitereAnteileString(String weitereAnteileString) {
        this.weitereAnteileString = weitereAnteileString;
    }

    public Boolean getShowDigitaleSignaturGestartet() {
        return showDigitaleSignaturGestartet;
    }

    public void setShowDigitaleSignaturGestartet(Boolean showDigitaleSignaturGestartet) {
        this.showDigitaleSignaturGestartet = showDigitaleSignaturGestartet;
    }

    public String getZahlungsart() {
        return zahlungsart;
    }

    public void setZahlungsart(String zahlungsart) {
        this.zahlungsart = zahlungsart;
    }

    public Boolean getSepaKontoAbweichend() {
        return sepaKontoAbweichend;
    }

    public void setSepaKontoAbweichend(Boolean sepaKontoAbweichend) {
        this.sepaKontoAbweichend = sepaKontoAbweichend;
    }

    public String getSepaKontoAbweichendNachname() {
        return sepaKontoAbweichendNachname;
    }

    public void setSepaKontoAbweichendNachname(String sepaKontoAbweichendNachname) {
        this.sepaKontoAbweichendNachname = sepaKontoAbweichendNachname;
    }

    public void setSepaKontoAbweichendVorname(String sepaKontoAbweichendVorname) {
        this.sepaKontoAbweichendVorname = sepaKontoAbweichendVorname;
    }

    public String getSepaKontoAbweichendVorname() {
        return sepaKontoAbweichendVorname;
    }

    public String getSepaKontoAbweichendIBAN() {
        return sepaKontoAbweichendIBAN;
    }

    public void setSepaKontoAbweichendIBAN(String sepaKontoAbweichendIBAN) {
        this.sepaKontoAbweichendIBAN = sepaKontoAbweichendIBAN;
    }

    public String getSepaKontoAbweichendBIC() {
        return sepaKontoAbweichendBIC;
    }

    public void setSepaKontoAbweichendBIC(String sepaKontoAbweichendBIC) {
        this.sepaKontoAbweichendBIC = sepaKontoAbweichendBIC;
    }

    public String getSepaKontoAbweichendBank() {
        return sepaKontoAbweichendBank;
    }

    public void setSepaKontoAbweichendBank(String sepaKontoAbweichendBank) {
        this.sepaKontoAbweichendBank = sepaKontoAbweichendBank;
    }

    public String getSepaKontoAbweichendStrasse() {
        return sepaKontoAbweichendStrasse;
    }

    public void setSepaKontoAbweichendStrasse(String sepaKontoAbweichendStrasse) {
        this.sepaKontoAbweichendStrasse = sepaKontoAbweichendStrasse;
    }

    public String getSepaKontoAbweichendPLZ() {
        return sepaKontoAbweichendPLZ;
    }

    public void setSepaKontoAbweichendPLZ(String sepaKontoAbweichendPLZ) {
        this.sepaKontoAbweichendPLZ = sepaKontoAbweichendPLZ;
    }

    public String getSepaKontoAbweichendOrt() {
        return sepaKontoAbweichendOrt;
    }

    public void setSepaKontoAbweichendOrt(String sepaKontoAbweichendOrt) {
        this.sepaKontoAbweichendOrt = sepaKontoAbweichendOrt;
    }

    public String getSepaKontoAbweichendAnrede() {
        return sepaKontoAbweichendAnrede;
    }

    public void setSepaKontoAbweichendAnrede(String sepaKontoAbweichendAnrede) {
        this.sepaKontoAbweichendAnrede = sepaKontoAbweichendAnrede;
    }

    public String getSepaKontoAbweichendTitel() {
        return sepaKontoAbweichendTitel;
    }

    public void setSepaKontoAbweichendTitel(String sepaKontoAbweichendTitel) {
        this.sepaKontoAbweichendTitel = sepaKontoAbweichendTitel;
    }

    public Boolean getSepaEinzugErmaechtigung() {
        return sepaEinzugErmaechtigung;
    }

    public void setSepaEinzugErmaechtigung(Boolean sepaEinzugErmaechtigung) {
        this.sepaEinzugErmaechtigung = sepaEinzugErmaechtigung;
    }

    public Boolean getEinwilligungBeteiligung() {
        return einwilligungBeteiligung;
    }

    public void setEinwilligungBeteiligung(Boolean einwilligungBeteiligung) {
        this.einwilligungBeteiligung = einwilligungBeteiligung;
    }

    public Boolean getConfirmPayment() {
        return confirmPayment;
    }

    public void setConfirmPayment(Boolean confirmPayment) {
        this.confirmPayment = confirmPayment;
    }

    public Boolean getOkWeitereAnteile() {
        return okWeitereAnteile;
    }

    public void setOkWeitereAnteile(Boolean okWeitereAnteile) {
        this.okWeitereAnteile = okWeitereAnteile;
    }

    public Boolean getOkSatzung() {
        return okSatzung;
    }

    public void setOkSatzung(Boolean okSatzung) {
        this.okSatzung = okSatzung;
    }

    public Boolean getOkDatenschutz() {
        return okDatenschutz;
    }

    public void setOkDatenschutz(Boolean okDatenschutz) {
        this.okDatenschutz = okDatenschutz;
    }

    public String getSepaKontoAbweichendLand() {
        return sepaKontoAbweichendLand;
    }

    public void setSepaKontoAbweichendLand(String sepaKontoAbweichendLand) {
        this.sepaKontoAbweichendLand = sepaKontoAbweichendLand;
    }

    public String getSepaKontoAbweichendEmail() {
        return sepaKontoAbweichendEmail;
    }

    public void setSepaKontoAbweichendEmail(String sepaKontoAbweichendEmail) {
        this.sepaKontoAbweichendEmail = sepaKontoAbweichendEmail;
    }

    public Boolean getOkBeiMinderjaehrig() {
        return okBeiMinderjaehrig;
    }

    public void setOkBeiMinderjaehrig(Boolean okBeiMinderjaehrig) {
        this.okBeiMinderjaehrig = okBeiMinderjaehrig;
    }

    public String getSepaKontoSelfTempVorname() {
        return sepaKontoSelfTempVorname;
    }

    public void setSepaKontoSelfTempVorname(String sepaKontoSelfTempVorname) {
        this.sepaKontoSelfTempVorname = sepaKontoSelfTempVorname;
    }

    public String getSepaKontoSelfTempNachname() {
        return sepaKontoSelfTempNachname;
    }

    public void setSepaKontoSelfTempNachname(String sepaKontoSelfTempNachname) {
        this.sepaKontoSelfTempNachname = sepaKontoSelfTempNachname;
    }

    public String getSepaKontoSelfTempBank() {
        return sepaKontoSelfTempBank;
    }

    public void setSepaKontoSelfTempBank(String sepaKontoSelfTempBank) {
        this.sepaKontoSelfTempBank = sepaKontoSelfTempBank;
    }

    public String getSepaKontoSelfTempIBAN() {
        return sepaKontoSelfTempIBAN;
    }

    public void setSepaKontoSelfTempIBAN(String sepaKontoSelfTempIBAN) {
        this.sepaKontoSelfTempIBAN = sepaKontoSelfTempIBAN;
    }

    public String getSepaKontoSelfTempBIC() {
        return sepaKontoSelfTempBIC;
    }

    public void setSepaKontoSelfTempBIC(String sepaKontoSelfTempBIC) {
        this.sepaKontoSelfTempBIC = sepaKontoSelfTempBIC;
    }
    
    
}

