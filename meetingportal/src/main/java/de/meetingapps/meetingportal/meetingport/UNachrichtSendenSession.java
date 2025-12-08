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
import java.util.LinkedList;
import java.util.List;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComEclM.EclNachrichtAnhangM;
import de.meetingapps.meetingportal.meetComEclM.EclNachrichtBasisTextM;
import de.meetingapps.meetingportal.meetComEclM.EclNachrichtEmpfaengerInstiM;
import de.meetingapps.meetingportal.meetComEclM.EclNachrichtEmpfaengerUserM;
import de.meetingapps.meetingportal.meetComEclM.EclNachrichtVerwendungsCodeM;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import jakarta.servlet.http.Part;

@SessionScoped
@Named
public class UNachrichtSendenSession implements Serializable {

    private int logDrucken = 3;

    private static final long serialVersionUID = 4691924494668343260L;

    private String aufrufMaske = "";
    private int mandant = 0;
    private int antwortAufNachricht = 0;

    /**Empfänger Auswahl*/
    private boolean empfaengerBOAnzeigen = false;
    private List<EclNachrichtEmpfaengerUserM> empfaengerBO = null;

    private boolean empfaengerEmittentenAnzeigen = false;
    private boolean empfaengerEmittentenAlleAusgewaehlt = false;
    private List<EclNachrichtEmpfaengerUserM> empfaengerEmittenten = null;

    private boolean empfaengerDritteAnzeigen = false;
    private boolean empfaengerDritteAlleAusgewaehlt = false;
    private List<EclNachrichtEmpfaengerUserM> empfaengerDritte = null;

    private boolean empfaengerInstiAnzeigen = false;
    private boolean empfaengerInstiAlleAusgewaehlt = false;
    private boolean empfaengerInstiAlleMitBestandAusgewaehlt = false;
    private List<EclNachrichtEmpfaengerInstiM> empfaengerInsti = null;

    /**Basis-Daten*/
    private List<EclNachrichtBasisTextM> basisTexte = null;
    private List<EclNachrichtVerwendungsCodeM> verwendungsCodes = null;
    private boolean auswahlVerwendungsCodeMoeglich = true;

    /**Sonstige Daten*/
    private String ausgewaehlterBasisText = "";
    private String ausgewaehlterVerwendungsCode = "";
    private String betreff = "";
    private String mailText = "";
    private boolean vollenTextInEMail = false;
    private int mailIdent = 0;

    /**Datei-Anhang*/
    private Part dateiAnhang = null;
    private String dateiAnhangBeschreibung = "";
    private List<EclNachrichtAnhangM> anhangListe = null;

    public boolean anhangListeIstLeer() {
        if (anhangListe == null || anhangListe.size() == 0) {
            return true;
        }
        return false;
    }

    public void pruefeDatei(FacesContext ctx, UIComponent comp, Object value) {
        CaBug.druckeLog("UNachrichtSendenSession.pruefeDatei", logDrucken, 10);
        //		if (((Part)value).getSize()>10000000) {
        //			new ValidatorException(new FacesMessage("Datei zu groß! Maximal 10MB erlaubt"));
        //		}
    }

    /**Sonstige Parameter*/
    /**LEN=20*/
    private String parameter1 = "";
    /**LEN=20*/
    private String parameter2 = "";
    /**LEN=20*/
    private String parameter3 = "";
    /**LEN=20*/
    private String parameter4 = "";
    /**LEN=20*/
    private String parameter5 = "";

    public boolean empfaengerBOVorhanden() {
        if (empfaengerBO == null || empfaengerBO.size() == 0) {
            return false;
        }
        return true;
    }

    public boolean empfaengerEmittentenVorhanden() {
        if (empfaengerEmittenten == null || empfaengerEmittenten.size() == 0) {
            return false;
        }
        return true;
    }

    public boolean empfaengerDritteVorhanden() {
        if (empfaengerDritte == null || empfaengerDritte.size() == 0) {
            return false;
        }
        return true;
    }

    public boolean empfaengerInstiVorhanden() {
        if (empfaengerInsti == null || empfaengerInsti.size() == 0) {
            return false;
        }
        return true;
    }

    public void clear() {
        empfaengerBOAnzeigen = false;
        empfaengerEmittentenAnzeigen = false;
        empfaengerEmittentenAlleAusgewaehlt = false;
        empfaengerDritteAnzeigen = false;
        empfaengerDritteAlleAusgewaehlt = false;
        empfaengerInstiAnzeigen = false;
        empfaengerInstiAlleAusgewaehlt = false;
        empfaengerInstiAlleMitBestandAusgewaehlt = false;
        auswahlVerwendungsCodeMoeglich = true;
        ausgewaehlterBasisText = "";
        ausgewaehlterVerwendungsCode = "";
        betreff = "";
        mailText = "";
        vollenTextInEMail = false;
        mailIdent = 0;
        dateiAnhang = null;
        dateiAnhangBeschreibung = "";
        anhangListe = new LinkedList<EclNachrichtAnhangM>();

        parameter1 = "";
        parameter2 = "";
        parameter3 = "";
        parameter4 = "";
        parameter5 = "";
    }

    /**************Standard getter und setter****************************/
    public List<EclNachrichtEmpfaengerUserM> getEmpfaengerBO() {
        return empfaengerBO;
    }

    public void setEmpfaengerBO(List<EclNachrichtEmpfaengerUserM> empfaengerBO) {
        this.empfaengerBO = empfaengerBO;
    }

    public List<EclNachrichtEmpfaengerUserM> getEmpfaengerEmittenten() {
        return empfaengerEmittenten;
    }

    public void setEmpfaengerEmittenten(List<EclNachrichtEmpfaengerUserM> empfaengerEmittenten) {
        this.empfaengerEmittenten = empfaengerEmittenten;
    }

    public List<EclNachrichtEmpfaengerUserM> getEmpfaengerDritte() {
        return empfaengerDritte;
    }

    public void setEmpfaengerDritte(List<EclNachrichtEmpfaengerUserM> empfaengerDritte) {
        this.empfaengerDritte = empfaengerDritte;
    }

    public List<EclNachrichtEmpfaengerInstiM> getEmpfaengerInsti() {
        return empfaengerInsti;
    }

    public void setEmpfaengerInsti(List<EclNachrichtEmpfaengerInstiM> empfaengerInsti) {
        this.empfaengerInsti = empfaengerInsti;
    }

    public List<EclNachrichtBasisTextM> getBasisTexte() {
        return basisTexte;
    }

    public void setBasisTexte(List<EclNachrichtBasisTextM> basisTexte) {
        this.basisTexte = basisTexte;
    }

    public List<EclNachrichtVerwendungsCodeM> getVerwendungsCodes() {
        return verwendungsCodes;
    }

    public void setVerwendungsCodes(List<EclNachrichtVerwendungsCodeM> verwendungsCodes) {
        this.verwendungsCodes = verwendungsCodes;
    }

    public String getAusgewaehlterBasisText() {
        return ausgewaehlterBasisText;
    }

    public void setAusgewaehlterBasisText(String ausgewaehlterBasisText) {
        this.ausgewaehlterBasisText = ausgewaehlterBasisText;
    }

    public String getAusgewaehlterVerwendungsCode() {
        return ausgewaehlterVerwendungsCode;
    }

    public void setAusgewaehlterVerwendungsCode(String ausgewaehlterVerwendungsCode) {
        this.ausgewaehlterVerwendungsCode = ausgewaehlterVerwendungsCode;
    }

    public String getBetreff() {
        return betreff;
    }

    public void setBetreff(String betreff) {
        this.betreff = betreff;
    }

    public String getMailText() {
        return mailText;
    }

    public void setMailText(String mailText) {
        this.mailText = mailText;
    }

    public boolean isEmpfaengerBOAnzeigen() {
        return empfaengerBOAnzeigen;
    }

    public void setEmpfaengerBOAnzeigen(boolean empfaengerBOAnzeigen) {
        this.empfaengerBOAnzeigen = empfaengerBOAnzeigen;
    }

    public boolean isEmpfaengerEmittentenAnzeigen() {
        return empfaengerEmittentenAnzeigen;
    }

    public void setEmpfaengerEmittentenAnzeigen(boolean empfaengerEmittentenAnzeigen) {
        this.empfaengerEmittentenAnzeigen = empfaengerEmittentenAnzeigen;
    }

    public boolean isEmpfaengerEmittentenAlleAusgewaehlt() {
        return empfaengerEmittentenAlleAusgewaehlt;
    }

    public void setEmpfaengerEmittentenAlleAusgewaehlt(boolean empfaengerEmittentenAlleAusgewaehlt) {
        this.empfaengerEmittentenAlleAusgewaehlt = empfaengerEmittentenAlleAusgewaehlt;
    }

    public boolean isEmpfaengerDritteAnzeigen() {
        return empfaengerDritteAnzeigen;
    }

    public void setEmpfaengerDritteAnzeigen(boolean empfaengerDritteAnzeigen) {
        this.empfaengerDritteAnzeigen = empfaengerDritteAnzeigen;
    }

    public boolean isEmpfaengerInstiAnzeigen() {
        return empfaengerInstiAnzeigen;
    }

    public void setEmpfaengerInstiAnzeigen(boolean empfaengerInstiAnzeigen) {
        this.empfaengerInstiAnzeigen = empfaengerInstiAnzeigen;
    }

    public boolean isEmpfaengerInstiAlleAusgewaehlt() {
        return empfaengerInstiAlleAusgewaehlt;
    }

    public void setEmpfaengerInstiAlleAusgewaehlt(boolean empfaengerInstiAlleAusgewaehlt) {
        this.empfaengerInstiAlleAusgewaehlt = empfaengerInstiAlleAusgewaehlt;
    }

    public String getAufrufMaske() {
        return aufrufMaske;
    }

    public void setAufrufMaske(String aufrufMaske) {
        this.aufrufMaske = aufrufMaske;
    }

    public boolean isEmpfaengerDritteAlleAusgewaehlt() {
        return empfaengerDritteAlleAusgewaehlt;
    }

    public void setEmpfaengerDritteAlleAusgewaehlt(boolean empfaengerDritteAlleAusgewaehlt) {
        this.empfaengerDritteAlleAusgewaehlt = empfaengerDritteAlleAusgewaehlt;
    }

    public Part getDateiAnhang() {
        return dateiAnhang;
    }

    public void setDateiAnhang(Part dateiAnhang) {
        this.dateiAnhang = dateiAnhang;
    }

    public String getDateiAnhangBeschreibung() {
        return dateiAnhangBeschreibung;
    }

    public void setDateiAnhangBeschreibung(String dateiAnhangBeschreibung) {
        this.dateiAnhangBeschreibung = dateiAnhangBeschreibung;
    }

    public List<EclNachrichtAnhangM> getAnhangListe() {
        return anhangListe;
    }

    public void setAnhangListe(List<EclNachrichtAnhangM> anhangListe) {
        this.anhangListe = anhangListe;
    }

    public int getMailIdent() {
        return mailIdent;
    }

    public void setMailIdent(int mailIdent) {
        this.mailIdent = mailIdent;
    }

    public boolean isVollenTextInEMail() {
        return vollenTextInEMail;
    }

    public void setVollenTextInEMail(boolean vollenTextInEMail) {
        this.vollenTextInEMail = vollenTextInEMail;
    }

    public int getMandant() {
        return mandant;
    }

    public void setMandant(int mandant) {
        this.mandant = mandant;
    }

    public int getAntwortAufNachricht() {
        return antwortAufNachricht;
    }

    public void setAntwortAufNachricht(int antwortAufNachricht) {
        this.antwortAufNachricht = antwortAufNachricht;
    }

    public String getParameter1() {
        return parameter1;
    }

    public void setParameter1(String parameter1) {
        this.parameter1 = parameter1;
    }

    public String getParameter2() {
        return parameter2;
    }

    public void setParameter2(String parameter2) {
        this.parameter2 = parameter2;
    }

    public String getParameter3() {
        return parameter3;
    }

    public void setParameter3(String parameter3) {
        this.parameter3 = parameter3;
    }

    public String getParameter4() {
        return parameter4;
    }

    public void setParameter4(String parameter4) {
        this.parameter4 = parameter4;
    }

    public String getParameter5() {
        return parameter5;
    }

    public void setParameter5(String parameter5) {
        this.parameter5 = parameter5;
    }

    public boolean isEmpfaengerInstiAlleMitBestandAusgewaehlt() {
        return empfaengerInstiAlleMitBestandAusgewaehlt;
    }

    public void setEmpfaengerInstiAlleMitBestandAusgewaehlt(boolean empfaengerInstiAlleMitBestandAusgewaehlt) {
        this.empfaengerInstiAlleMitBestandAusgewaehlt = empfaengerInstiAlleMitBestandAusgewaehlt;
    }

    public boolean isAuswahlVerwendungsCodeMoeglich() {
        CaBug.druckeLog("auswahlVerwendungsCodeMoeglich=" + auswahlVerwendungsCodeMoeglich, logDrucken, 10);
        return auswahlVerwendungsCodeMoeglich;
    }

    public void setAuswahlVerwendungsCodeMoeglich(boolean auswahlVerwendungsCodeMoeglich) {
        this.auswahlVerwendungsCodeMoeglich = auswahlVerwendungsCodeMoeglich;
    }

}
