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
package de.meetingapps.meetingportal.meetingportTFunktionen;

import java.io.Serializable;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComKonst.KonstPortalView;
import de.meetingapps.meetingportal.meetingportTController.TBestaetigenSession;
import de.meetingapps.meetingportal.meetingportTController.TLoginLogoutSession;
import de.meetingapps.meetingportal.meetingportTController.TPasswortVergessenSession;
import de.meetingapps.meetingportal.meetingportTController.TSession;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@SessionScoped
@Named
public class TSeitensteuerungSession implements Serializable {
    private static final long serialVersionUID = -5652407658149061653L;

    private int logDrucken = 3;

    private @Inject TLoginLogoutSession tLoginLogoutSession;
    private @Inject TBestaetigenSession tBestaetigenSession;
    private @Inject TPasswortVergessenSession tPasswortVergessenSession;
    private @Inject TSession tSession;

    /***********Werte für Überschrift und Start-Texte******************/

    private boolean appTitelAnzeigen=false;
    
    /**Nur über holeTextAppTitel in XHTML verwenden*/
    private String textNummerAppTitel = "";

    /**++++++++++Überschrift++++++++++++++*/
    /**Direkt für Render in XHTML-Seite*/
    private boolean ueberschriftAnzeigen = false;

    /**Nur über holeTextUeberschrift in XHTML verwenden*/
    private String textNummerUeberschrift = "";

    /**++++++++++Starttext++++++++++++++*/
    /**Direkt für Render in XHTML-Seite*/
    private boolean starttextAnzeigen = false;

    /**Nur über holeTextStarttext in XHTML verwenden*/
    private String textNummerStarttext = "";

    /*****************Werte für Buttons am Ende der Seite****************/

    /**++++++++++linker Button+++++++++++++++*/
    /**Direkt für Render in XHTML-Seite*/
    private boolean linkenButtonAnzeigen = false;

    /**Nur über holeTextLinkerButton in XHTML verwenden*/
    private String textNummerLinkerButton = "";

    /**+++++++++++mittlerer Button++++++++++++++*/
    /**Es wird überhaupt ein Logout angezeigt*/
    private boolean logoutAnzeigen = false;

    /**Normales Logout - zurück zur Login-Seite*/
    private boolean logoutNormalAnzeigen = false;

    /**Logout - zurück zur Emittenten-Seite*/
    private boolean logoutSeiteVerlassenAnzeigen = false;

    /**Auf Login-Seite: Applikation-Beenden-Button anzeigen, zurück zur Emittenten-Seite*/
    private boolean endeButtonAnzeigen = false;

    /**++++++++++++++++++rechter Button++++++++++++++++++++*/
    private boolean rechtenButtonAnzeigen = false;
    /**true auch Stream-Bereich und Header-Bereich werden gerendered*/
    private boolean rechterButtonRenderKomplett = false;
    private String textNummerRechterButton = "";

    public void logAusgabe() {
        if (CaBug.pruefeLog(logDrucken, 10)) {

            /*Starttext*/
            CaBug.druckeLog("starttextAnzeigen=" + starttextAnzeigen, logDrucken, 10);
            CaBug.druckeLog("textNummerStarttext=" + textNummerStarttext, logDrucken, 10);

            /*Buttons Mitte*/
            CaBug.druckeLog("logoutAnzeigen=" + logoutAnzeigen, logDrucken, 10);
            CaBug.druckeLog("logoutNormalAnzeigen=" + logoutNormalAnzeigen, logDrucken, 10);
            CaBug.druckeLog("logoutSeiteVerlassenAnzeigen=" + logoutSeiteVerlassenAnzeigen, logDrucken, 10);
            CaBug.druckeLog("endeButtonAnzeigen=" + endeButtonAnzeigen, logDrucken, 10);
        }
    }

    
    /**true => Fehlermeldung oder Meldungstext wird angezeigt*/
    public boolean meldungAnzeigen() {
        CaBug.druckeLog("", logDrucken, 10);
        switch (KonstPortalView.fehlertextAus(tSession.getViewnummer())) {
        case KonstPortalView.FEHLER_HOLEN_AUS_STANDARD:
            CaBug.druckeLog("FEHLER_HOLEN_AUS_STANDARD", logDrucken, 10);
            return tSession.pruefeFehlerOderQuittungVorhanden();
        case KonstPortalView.FEHLER_HOLEN_AUS_LOGINLOGOUT:
            CaBug.druckeLog("FEHLER_HOLEN_AUS_LOGINLOGOUT", logDrucken, 10);
            return tLoginLogoutSession.pruefeFehlerOderQuittungVorhanden();
        case KonstPortalView.FEHLER_HOLEN_AUS_BESTAETIGEN:
            CaBug.druckeLog("FEHLER_HOLEN_AUS_BESTAETIGEN", logDrucken, 10);
           return tBestaetigenSession.pruefeFehlerOderQuittungVorhanden();
        case KonstPortalView.FEHLER_HOLEN_AUS_PASSWORTVERGESSEN:
            CaBug.druckeLog("FEHLER_HOLEN_AUS_PASSWORTVERGESSEN", logDrucken, 10);
            return tPasswortVergessenSession.pruefeFehlerOderQuittungVorhanden();
        }
        CaBug.druckeLog("Kein case ausgeführt", logDrucken, 10);
        return false;
    }
    
    /**Liefert Art der Meldung (d.h. Fehler oder Meldung; Icon)*/
    public int meldungsArt() {
        CaBug.druckeLog("", logDrucken, 10);
        switch (KonstPortalView.fehlertextAus(tSession.getViewnummer())) {
        case KonstPortalView.FEHLER_HOLEN_AUS_STANDARD:
            return tSession.liefereMeldungArt();
        case KonstPortalView.FEHLER_HOLEN_AUS_LOGINLOGOUT:
            return tLoginLogoutSession.liefereMeldungArt();
        case KonstPortalView.FEHLER_HOLEN_AUS_BESTAETIGEN:
            return tBestaetigenSession.liefereMeldungArt();
        case KonstPortalView.FEHLER_HOLEN_AUS_PASSWORTVERGESSEN:
            return tPasswortVergessenSession.liefereMeldungArt();
        }
        return 1;
    }
    
    /**Prüfen, ob Fehlermeldung angezeigt werden soll*/
    public boolean fehlerAnzeigen() {
        CaBug.druckeLog("", logDrucken, 10);
        switch (KonstPortalView.fehlertextAus(tSession.getViewnummer())) {
        case KonstPortalView.FEHLER_HOLEN_AUS_STANDARD:
            return tSession.pruefeFehlerMeldungVorhanden();
        case KonstPortalView.FEHLER_HOLEN_AUS_LOGINLOGOUT:
            return tLoginLogoutSession.pruefeFehlerMeldungVorhanden();
        case KonstPortalView.FEHLER_HOLEN_AUS_BESTAETIGEN:
            return tBestaetigenSession.pruefeFehlerMeldungVorhanden();
        case KonstPortalView.FEHLER_HOLEN_AUS_PASSWORTVERGESSEN:
            return tPasswortVergessenSession.pruefeFehlerMeldungVorhanden();
        }
        return false;
    }

    /**Fehlermeldung Text holen*/
    public String holeFehlerText() {
        CaBug.druckeLog("", logDrucken, 10);
        String erg="";
        switch (KonstPortalView.fehlertextAus(tSession.getViewnummer())) {
        case KonstPortalView.FEHLER_HOLEN_AUS_STANDARD:
            CaBug.druckeLog("FEHLER_HOLEN_AUS_STANDARD", logDrucken, 10);
            erg= tSession.getFehlerMeldung();
            break;
        case KonstPortalView.FEHLER_HOLEN_AUS_LOGINLOGOUT:
            CaBug.druckeLog("FEHLER_HOLEN_AUS_LOGINLOGOUT", logDrucken, 10);
           erg=  tLoginLogoutSession.getFehlerMeldung();
           break;
       case KonstPortalView.FEHLER_HOLEN_AUS_BESTAETIGEN:
            CaBug.druckeLog("FEHLER_HOLEN_AUS_BESTAETIGEN", logDrucken, 10);
            erg=  tBestaetigenSession.getFehlerMeldung();
            break;
        case KonstPortalView.FEHLER_HOLEN_AUS_PASSWORTVERGESSEN:
            CaBug.druckeLog("FEHLER_HOLEN_AUS_PASSWORTVERGESSEN", logDrucken, 10);
            erg=  tPasswortVergessenSession.getFehlerMeldung();
            break;
       }
        //erg="Dies ist ein Test";
        CaBug.druckeLog("erg=<"+erg+"> "+ erg.length(), logDrucken, 10);
        if (erg.length()<1) {erg="   ";}
        
        return erg;
    }
    
    public String holeQuittungText() {
        CaBug.druckeLog("", logDrucken, 10);
        String erg="";
       switch (KonstPortalView.fehlertextAus(tSession.getViewnummer())) {
        case KonstPortalView.FEHLER_HOLEN_AUS_STANDARD:
            erg= tSession.getFehlerText();break;
        case KonstPortalView.FEHLER_HOLEN_AUS_LOGINLOGOUT:
            erg=  tLoginLogoutSession.getFehlerText();break;
        case KonstPortalView.FEHLER_HOLEN_AUS_BESTAETIGEN:
            erg=  tBestaetigenSession.getFehlerText();break;
        case KonstPortalView.FEHLER_HOLEN_AUS_PASSWORTVERGESSEN:
            erg= tPasswortVergessenSession.getFehlerText();break;
        }
       if (erg.length()==0) {erg="   ";}
       return erg;
    }

    /***************Werte für Eintrittskarten-Masken*******************************/

    private boolean ekvVollmachtEingeben = false;

    private boolean ekvVollmachtStarttextAnzeigen = false;
    private String ekvTextNummerVollmachtStarttext = "";

    private String ekvTextNummerVorVollmachtName = "";
    private String ekvTextNummerVorVollmachtVorname = "";
    private String ekvTextNummerVorVollmachtOrt = "";

    private boolean ekvVollmachtEndetextAnzeigen = false;
    private String ekvTextNummerVollmachtEndetext = "";

    private boolean ekvStarttextAnzeigen = false;
    private String ekvTextNummerStarttext = "";

    private String ekvTextNummerVersandLautAktienregister = ""; //1
    private String ekvTextNummerVersandAbweichendeAdresse = ""; //2
    private String ekvTextNummerVersandOnlineAusdruck = ""; //3
    private String ekvTextNummerVersandOnlineEmail = ""; //4

    private String ekvTextNummerVorEmail = "";
    private String ekvTextNummerVorEmailBestaetigen = "";

    private String ekvTextNummerVorAbweichenderAdresse = "";
    private String ekvTextNummerNachAbweichenderAdresse = "";

    /**Starttext auf iEintrittskarteDetail, abhängig von Art der Karte 
     * (Eintrittskarte, Eintrittskarte mit Vollmacht, Gastkarte)
      */
    private boolean ekvDetailArtStarttextAnzeigen = false;
    private String ekvTextNummerDetailArtStarttext = "";

    /**Starttext auf iEintrittskarteQuittung, abhängig von Art der angeforderten EK
     * (1 EK, 2 EK etc.)
     */
    private boolean ekvArtStarttextAnzeigen = false;
    private String ekvTextNummerArtStarttext = "";

    /**Quittungstext auf iEintrittskarteQuittung. Abhängig von Art der angeforderten EK,
     * und der ausgewählten Versandart
     */
    private boolean ekvQuittungVersandartAnzeigen = false;
    private String ekvTextNummerQuittungVersandart = "";

    private boolean ekvVersandadresseAnzeigen = false;
    private boolean ekvEKDruck1ButtonAnzeigen = false;
    private boolean ekvEKDruck2ButtonAnzeigen = false;
    private boolean ekvEKMail1ButtonAnzeigen = false;
    private boolean ekvEKMail2ButtonAnzeigen = false;

    /*****************Standard getter und setter***********************/

    public boolean isUeberschriftAnzeigen() {
        return ueberschriftAnzeigen;
    }

    public void setUeberschriftAnzeigen(boolean ueberschriftAnzeigen) {
        this.ueberschriftAnzeigen = ueberschriftAnzeigen;
    }

    public String getTextNummerUeberschrift() {
        return textNummerUeberschrift;
    }

    public void setTextNummerUeberschrift(String textNummerUeberschrift) {
        this.textNummerUeberschrift = textNummerUeberschrift;
    }

    public boolean isStarttextAnzeigen() {
        return starttextAnzeigen;
    }

    public void setStarttextAnzeigen(boolean starttextAnzeigen) {
        this.starttextAnzeigen = starttextAnzeigen;
    }

    public String getTextNummerStarttext() {
        return textNummerStarttext;
    }

    public void setTextNummerStarttext(String textNummerStarttext) {
        this.textNummerStarttext = textNummerStarttext;
    }

    public boolean isLinkenButtonAnzeigen() {
        return linkenButtonAnzeigen;
    }

    public void setLinkenButtonAnzeigen(boolean linkenButtonAnzeigen) {
        this.linkenButtonAnzeigen = linkenButtonAnzeigen;
    }

    public String getTextNummerLinkerButton() {
        return textNummerLinkerButton;
    }

    public void setTextNummerLinkerButton(String textNummerLinkerButton) {
        this.textNummerLinkerButton = textNummerLinkerButton;
    }

    public boolean isLogoutAnzeigen() {
        return logoutAnzeigen;
    }

    public void setLogoutAnzeigen(boolean logoutAnzeigen) {
        this.logoutAnzeigen = logoutAnzeigen;
    }

    public boolean isLogoutNormalAnzeigen() {
        return logoutNormalAnzeigen;
    }

    public void setLogoutNormalAnzeigen(boolean logoutNormalAnzeigen) {
        this.logoutNormalAnzeigen = logoutNormalAnzeigen;
    }

    public boolean isLogoutSeiteVerlassenAnzeigen() {
        return logoutSeiteVerlassenAnzeigen;
    }

    public void setLogoutSeiteVerlassenAnzeigen(boolean logoutSeiteVerlassenAnzeigen) {
        this.logoutSeiteVerlassenAnzeigen = logoutSeiteVerlassenAnzeigen;
    }

    public boolean isEndeButtonAnzeigen() {
        return endeButtonAnzeigen;
    }

    public void setEndeButtonAnzeigen(boolean endeButtonAnzeigen) {
        this.endeButtonAnzeigen = endeButtonAnzeigen;
    }

    public boolean isRechtenButtonAnzeigen() {
        return rechtenButtonAnzeigen;
    }

    public void setRechtenButtonAnzeigen(boolean rechtenButtonAnzeigen) {
        this.rechtenButtonAnzeigen = rechtenButtonAnzeigen;
    }

    public boolean isRechterButtonRenderKomplett() {
        return rechterButtonRenderKomplett;
    }

    public void setRechterButtonRenderKomplett(boolean rechterButtonRenderKomplett) {
        this.rechterButtonRenderKomplett = rechterButtonRenderKomplett;
    }

    public String getTextNummerRechterButton() {
        return textNummerRechterButton;
    }

    public void setTextNummerRechterButton(String textNummerRechterButton) {
        this.textNummerRechterButton = textNummerRechterButton;
    }

    public boolean isEkvStarttextAnzeigen() {
        return ekvStarttextAnzeigen;
    }

    public void setEkvStarttextAnzeigen(boolean ekvStarttextAnzeigen) {
        this.ekvStarttextAnzeigen = ekvStarttextAnzeigen;
    }

    public String getEkvTextNummerStarttext() {
        return ekvTextNummerStarttext;
    }

    public void setEkvTextNummerStarttext(String ekvTextNummerStarttext) {
        this.ekvTextNummerStarttext = ekvTextNummerStarttext;
    }

    public String getEkvTextNummerVersandLautAktienregister() {
        return ekvTextNummerVersandLautAktienregister;
    }

    public void setEkvTextNummerVersandLautAktienregister(String ekvTextNummerVersandLautAktienregister) {
        this.ekvTextNummerVersandLautAktienregister = ekvTextNummerVersandLautAktienregister;
    }

    public String getEkvTextNummerVersandAbweichendeAdresse() {
        return ekvTextNummerVersandAbweichendeAdresse;
    }

    public void setEkvTextNummerVersandAbweichendeAdresse(String ekvTextNummerVersandAbweichendeAdresse) {
        this.ekvTextNummerVersandAbweichendeAdresse = ekvTextNummerVersandAbweichendeAdresse;
    }

    public String getEkvTextNummerVersandOnlineAusdruck() {
        return ekvTextNummerVersandOnlineAusdruck;
    }

    public void setEkvTextNummerVersandOnlineAusdruck(String ekvTextNummerVersandOnlineAusdruck) {
        this.ekvTextNummerVersandOnlineAusdruck = ekvTextNummerVersandOnlineAusdruck;
    }

    public String getEkvTextNummerVersandOnlineEmail() {
        return ekvTextNummerVersandOnlineEmail;
    }

    public void setEkvTextNummerVersandOnlineEmail(String ekvTextNummerVersandOnlineEmail) {
        this.ekvTextNummerVersandOnlineEmail = ekvTextNummerVersandOnlineEmail;
    }

    public String getEkvTextNummerVorEmail() {
        return ekvTextNummerVorEmail;
    }

    public void setEkvTextNummerVorEmail(String ekvTextNummerVorEmail) {
        this.ekvTextNummerVorEmail = ekvTextNummerVorEmail;
    }

    public String getEkvTextNummerVorEmailBestaetigen() {
        return ekvTextNummerVorEmailBestaetigen;
    }

    public void setEkvTextNummerVorEmailBestaetigen(String ekvTextNummerVorEmailBestaetigen) {
        this.ekvTextNummerVorEmailBestaetigen = ekvTextNummerVorEmailBestaetigen;
    }

    public String getEkvTextNummerVorAbweichenderAdresse() {
        return ekvTextNummerVorAbweichenderAdresse;
    }

    public void setEkvTextNummerVorAbweichenderAdresse(String ekvTextNummerVorAbweichenderAdresse) {
        this.ekvTextNummerVorAbweichenderAdresse = ekvTextNummerVorAbweichenderAdresse;
    }

    public String getEkvTextNummerNachAbweichenderAdresse() {
        return ekvTextNummerNachAbweichenderAdresse;
    }

    public void setEkvTextNummerNachAbweichenderAdresse(String ekvTextNummerNachAbweichenderAdresse) {
        this.ekvTextNummerNachAbweichenderAdresse = ekvTextNummerNachAbweichenderAdresse;
    }

    public boolean isEkvVollmachtEingeben() {
        return ekvVollmachtEingeben;
    }

    public void setEkvVollmachtEingeben(boolean ekvVollmachtEingeben) {
        this.ekvVollmachtEingeben = ekvVollmachtEingeben;
    }

    public String getEkvTextNummerVorVollmachtName() {
        return ekvTextNummerVorVollmachtName;
    }

    public void setEkvTextNummerVorVollmachtName(String ekvTextNummerVorVollmachtName) {
        this.ekvTextNummerVorVollmachtName = ekvTextNummerVorVollmachtName;
    }

    public String getEkvTextNummerVorVollmachtVorname() {
        return ekvTextNummerVorVollmachtVorname;
    }

    public void setEkvTextNummerVorVollmachtVorname(String ekvTextNummerVorVollmachtVorname) {
        this.ekvTextNummerVorVollmachtVorname = ekvTextNummerVorVollmachtVorname;
    }

    public String getEkvTextNummerVorVollmachtOrt() {
        return ekvTextNummerVorVollmachtOrt;
    }

    public void setEkvTextNummerVorVollmachtOrt(String ekvTextNummerVorVollmachtOrt) {
        this.ekvTextNummerVorVollmachtOrt = ekvTextNummerVorVollmachtOrt;
    }

    public boolean isEkvVollmachtStarttextAnzeigen() {
        return ekvVollmachtStarttextAnzeigen;
    }

    public void setEkvVollmachtStarttextAnzeigen(boolean ekvVollmachtStarttextAnzeigen) {
        this.ekvVollmachtStarttextAnzeigen = ekvVollmachtStarttextAnzeigen;
    }

    public String getEkvTextNummerVollmachtStarttext() {
        return ekvTextNummerVollmachtStarttext;
    }

    public void setEkvTextNummerVollmachtStarttext(String ekvTextNummerVollmachtStarttext) {
        this.ekvTextNummerVollmachtStarttext = ekvTextNummerVollmachtStarttext;
    }

    public boolean isEkvVollmachtEndetextAnzeigen() {
        return ekvVollmachtEndetextAnzeigen;
    }

    public void setEkvVollmachtEndetextAnzeigen(boolean ekvVollmachtEndetextAnzeigen) {
        this.ekvVollmachtEndetextAnzeigen = ekvVollmachtEndetextAnzeigen;
    }

    public String getEkvTextNummerVollmachtEndetext() {
        return ekvTextNummerVollmachtEndetext;
    }

    public void setEkvTextNummerVollmachtEndetext(String ekvTextNummerVollmachtEndetext) {
        this.ekvTextNummerVollmachtEndetext = ekvTextNummerVollmachtEndetext;
    }

    public boolean isEkvArtStarttextAnzeigen() {
        return ekvArtStarttextAnzeigen;
    }

    public void setEkvArtStarttextAnzeigen(boolean ekvArtStarttextAnzeigen) {
        this.ekvArtStarttextAnzeigen = ekvArtStarttextAnzeigen;
    }

    public String getEkvTextNummerArtStarttext() {
        return ekvTextNummerArtStarttext;
    }

    public void setEkvTextNummerArtStarttext(String ekvTextNummerArtStarttext) {
        this.ekvTextNummerArtStarttext = ekvTextNummerArtStarttext;
    }

    public boolean isEkvQuittungVersandartAnzeigen() {
        return ekvQuittungVersandartAnzeigen;
    }

    public void setEkvQuittungVersandartAnzeigen(boolean ekvQuittungVersandartAnzeigen) {
        this.ekvQuittungVersandartAnzeigen = ekvQuittungVersandartAnzeigen;
    }

    public String getEkvTextNummerQuittungVersandart() {
        return ekvTextNummerQuittungVersandart;
    }

    public void setEkvTextNummerQuittungVersandart(String ekvTextNummerQuittungVersandart) {
        this.ekvTextNummerQuittungVersandart = ekvTextNummerQuittungVersandart;
    }

    public boolean isEkvVersandadresseAnzeigen() {
        return ekvVersandadresseAnzeigen;
    }

    public void setEkvVersandadresseAnzeigen(boolean ekvVersandadresseAnzeigen) {
        this.ekvVersandadresseAnzeigen = ekvVersandadresseAnzeigen;
    }

    public boolean isEkvEKDruck1ButtonAnzeigen() {
        return ekvEKDruck1ButtonAnzeigen;
    }

    public void setEkvEKDruck1ButtonAnzeigen(boolean ekvEKDruck1ButtonAnzeigen) {
        this.ekvEKDruck1ButtonAnzeigen = ekvEKDruck1ButtonAnzeigen;
    }

    public boolean isEkvEKMail1ButtonAnzeigen() {
        return ekvEKMail1ButtonAnzeigen;
    }

    public void setEkvEKMail1ButtonAnzeigen(boolean ekvEKMail1ButtonAnzeigen) {
        this.ekvEKMail1ButtonAnzeigen = ekvEKMail1ButtonAnzeigen;
    }

    public boolean isEkvEKMail2ButtonAnzeigen() {
        return ekvEKMail2ButtonAnzeigen;
    }

    public void setEkvEKMail2ButtonAnzeigen(boolean ekvEKMail2ButtonAnzeigen) {
        this.ekvEKMail2ButtonAnzeigen = ekvEKMail2ButtonAnzeigen;
    }

    public boolean isEkvEKDruck2ButtonAnzeigen() {
        return ekvEKDruck2ButtonAnzeigen;
    }

    public void setEkvEKDruck2ButtonAnzeigen(boolean ekvEKDruck2ButtonAnzeigen) {
        this.ekvEKDruck2ButtonAnzeigen = ekvEKDruck2ButtonAnzeigen;
    }

    public boolean isEkvDetailArtStarttextAnzeigen() {
        return ekvDetailArtStarttextAnzeigen;
    }

    public void setEkvDetailArtStarttextAnzeigen(boolean ekvDetailArtStarttextAnzeigen) {
        this.ekvDetailArtStarttextAnzeigen = ekvDetailArtStarttextAnzeigen;
    }

    public String getEkvTextNummerDetailArtStarttext() {
        return ekvTextNummerDetailArtStarttext;
    }

    public void setEkvTextNummerDetailArtStarttext(String ekvTextNummerDetailArtStarttext) {
        this.ekvTextNummerDetailArtStarttext = ekvTextNummerDetailArtStarttext;
    }

    public boolean isAppTitelAnzeigen() {
        return appTitelAnzeigen;
    }

    public void setAppTitelAnzeigen(boolean appTitelAnzeigen) {
        this.appTitelAnzeigen = appTitelAnzeigen;
    }

    public String getTextNummerAppTitel() {
        return textNummerAppTitel;
    }

    public void setTextNummerAppTitel(String textNummerAppTitel) {
        this.textNummerAppTitel = textNummerAppTitel;
    }

}
