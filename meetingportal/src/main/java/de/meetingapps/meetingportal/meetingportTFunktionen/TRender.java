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

import de.meetingapps.meetingportal.meetComEclM.EclLoginDatenM;
import de.meetingapps.meetingportal.meetComEclM.EclParamM;
import de.meetingapps.meetingportal.meetComEclM.EclPortalTexteM;
import de.meetingapps.meetingportal.meetComHVParam.ParamSpezial;
import de.meetingapps.meetingportal.meetComKonst.KonstLoginKennungArt;
import de.meetingapps.meetingportal.meetComKonst.KonstPortalAktion;
import de.meetingapps.meetingportal.meetComKonst.KonstPortalView;
import de.meetingapps.meetingportal.meetingportTController.TAuswahlSession;
import de.meetingapps.meetingportal.meetingportTController.TEinstellungenSession;
import de.meetingapps.meetingportal.meetingportTController.TSession;
import de.meetingapps.meetingportal.meetingportTController.TWillenserklaerungSession;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

/**Enthält funktionen für Rendering in der JSF-Oberfläche*/
@SessionScoped
@Named
public class TRender implements Serializable {
    private static final long serialVersionUID = -2493971747733476106L;

    /**Löschen*/
    public boolean logoutAnzeigen() {
        return KonstPortalView.logoutAnzeigen(tSession.getViewnummer());
    }

//    private int logDrucken = 10;

    private @Inject EclParamM eclParamM;
    private @Inject EclLoginDatenM eclLoginDatenM;
    private @Inject TSession tSession;
    private @Inject TAuswahlSession tAuswahlSession;
    private @Inject TWillenserklaerungSession tWillenserklaerungSession;
    private @Inject EclPortalTexteM eclPortalTexteM;
    private @Inject TEinstellungenSession tEinstellungenSession;

    
    
    public boolean menueAnzeigen() {
        return KonstPortalView.menueAnzeigen(tSession.getViewnummer());
    }
    
    public boolean infoAnzeigen() {
        return KonstPortalView.infoAnzeigen(tSession.getViewnummer());
    }
    

    /*************Header*********************/

    /**Anzeige Test-Zeit*/
    public boolean andereZeit() {
        if ((eclParamM.getParam().paramPortal.testModus & 2) != 2 || tSession.getDifZeit() == 0) {
            return false;
        }
        return true;
    }

    /**Anzeige der Aktionärsdaten im Header*/
    public boolean headerAktionaersdaten() {
        return KonstPortalView.aktionaersdatenAnzeigen(tSession.getViewnummer());
    }

    /**Anzeige der Stimmen im Header*/
    public boolean headerStimmenAnzeigen() {
        if (eclParamM.getParam().paramPortal.anzeigeStimmen == 0
                || eclLoginDatenM.getEclLoginDaten().kennungArt == KonstLoginKennungArt.personenNatJur || eclParamM.getParam().paramPortal.anzeigeStimmenKennung==0) {
            return false;
        }
        return true;
    }

    public boolean portalMehrsprachig() {
        return eclParamM.getEclEmittent().lieferePortalIstMehrsprachig();
    }

    /*******************Design************************************/

    public int logoutObenOderUnten() {
        return eclParamM.getParam().paramPortal.logoutObenOderUnten;
    }

    /******************Allgemein Portalfunktionen***********************/
    /**Angeboten unabhängig von der Berechtigung - z.B. für Monitoring*/

    /******************Auswahl***********************************************/

    /*++++++++++++++++++++Einstellungen++++++++++++++++++++++++++++++++++++*/
    public boolean auswahlEinstellungen() {
        if (elekEinladungsversandAngebotenUndZulaessig() || dauerhaftesPasswortAngebotenUndZulaessig()
                || eMailHinterlegungAngebotenUndZulaessig()) {
            return true;
        }
        return false;
    }

    public int reihenfolgeEinstellungen() {
        return eclParamM.getParam().paramPortal.reihenfolgeRegistrierung;
    }

    /*++++++++++++++++++Unterlagen++++++++++++++++++++++++*/
    @Deprecated
    public boolean auswahlUnterlagen() {
        if (tAuswahlSession.isUnterlagenGruppe1Angeboten() || tAuswahlSession.isUnterlagenGruppe2Angeboten()
                || tAuswahlSession.isUnterlagenGruppe3Angeboten() || tAuswahlSession.isUnterlagenGruppe4Angeboten()
                || tAuswahlSession.isUnterlagenGruppe5Angeboten()) {
            return true;
        }
        return false;
    }

    @Deprecated
    public boolean unterlagenAktiv() {
        if (tAuswahlSession.isUnterlagenGruppe1Aktiv() || tAuswahlSession.isUnterlagenGruppe2Aktiv()
                || tAuswahlSession.isUnterlagenGruppe3Aktiv() || tAuswahlSession.isUnterlagenGruppe4Aktiv()
                || tAuswahlSession.isUnterlagenGruppe5Aktiv()) {
            return true;
        }
        return false;
    }

    /*++++++++++++++++Videostream++++++++++++++++++++++++*/
    /**Muß hier bleiben, da nur angezeigt wird wenn Stream nicht schon läuft*/
    public boolean auswahlStream() {
        if (!tSession.isStreamshow() && tAuswahlSession.isStreamAngeboten()) {
            return true;
        }
        //		return false;
        return true;
    }

    
    /*+++++++++++++Veranstaltung+++++++++++++++++++++++++*/
    public boolean veranstaltungMailEingeben() {
        return (eclParamM.getParam().paramPortal.veranstaltungMailVerschicken==1 || eclParamM.getParam().paramPortal.veranstaltungMailVerschicken==2 || eclParamM.getParam().paramPortal.veranstaltungMailVerschicken==3);
    }
    
    public boolean veranstaltungTicketErzeugen() {
        return (eclParamM.getParam().paramPortal.veranstaltungMailVerschicken==3);
    }

    public boolean veranstaltungPersonenzahlEingeben() {
        return eclParamM.getParam().paramPortal.veranstaltungPersonenzahlEingeben==1;
    }
    
    public boolean veranstaltungMehrfachAuswaehlbarJeGruppe() {
        return eclParamM.getParam().paramPortal.veranstaltungMehrfachAuswaehlbarJeGruppe==1;
    }
    
    /*++++++++++Freiwillige Anmeldung++++++++++++++++++++++++*/
    public boolean freiwillingAnmeldenPraesenzOderOnline() {
        return eclParamM.getParam().paramPortal.freiwillingAnmeldenPraesenzOderOnline==1;
    }
    
    public boolean freiwilligeAnmeldungEKDruckMoeglich() {
        return (eclParamM.getParam().paramPortal.freiwilligeAnmeldungEKDruckMoeglich==1);    
    }
    
    /*++++++++++++++++++++Online-Teilnahme++++++++++++++++++++++++++++++++++++*/
    /**Button "Zugang" wird angezeigt - es sind noch keine unter dieser Kennung
     * präsent gesetzt, d.h. quasi erstmaliger Zugangs-Button
     */
    public boolean auswahlTeilnahmeZugang() {
        /*XXX UB wieder raus und zu "ohne UB"*/
//        CaBug.druckeLog(
//                "tAuswahlSession.isOnlineteilnahmeAngebotenUB()=" + tAuswahlSession.isOnlineteilnahmeAngeboten(),
//                logDrucken, 10);
//        CaBug.druckeLog("eclZugeordneteMeldungListeM.isPraesenteVorhanden()="
//                + eclZugeordneteMeldungListeM.isPraesenteVorhanden(), logDrucken, 10);
//        CaBug.druckeLog("eclZugeordneteMeldungListeM.isNichtPraesenteVorhanden()="
//                + eclZugeordneteMeldungListeM.isNichtPraesenteVorhanden(), logDrucken, 10);
//        if (tAuswahlSession.isOnlineteilnahmeAngebotenUB()
//                && eclZugeordneteMeldungListeM.isPraesenteVorhanden() == false
//                && eclZugeordneteMeldungListeM.isNichtPraesenteVorhanden() == true) {
//            return true;
//        }
        return false;
    }

    /**Button "Zugang" wird angezeigt - es sind bereits unter dieser Kennung
     * präsent gesetzte, aber auch noch nicht präsente enthalten, d.h. quasi "Zugang für weitere-Button"
     */
    public boolean auswahlTeilnahmeWeitereZugang() {
        /*XXX*/
//        if (tAuswahlSession.isOnlineteilnahmeAngeboten() && eclZugeordneteMeldungListeM.isPraesenteVorhanden() == true
//                && eclZugeordneteMeldungListeM.isNichtPraesenteVorhanden() == true) {
//            return true;
//        }
        return false;
    }

    /**Button "Abgang" wird angezeigt - es sind bereits unter dieser Kennung
     * präsent gesetzte, aber auch noch nicht präsente enthalten, d.h. quasi "Zugang für weitere-Button"
     */
    public boolean auswahlTeilnahmeAbgang() {
        /*XXX UB wieder raus und zu "ohne UB"*/

//        if (tAuswahlSession.isOnlineteilnahmeAngebotenUB()
//                && eclZugeordneteMeldungListeM.isPraesenteVorhanden() == true) {
//            return true;
//        }
        return false;
    }

    /**Button "Stimmabgabe" wird angezeigt - es sind bereits unter dieser Kennung
     * präsent gesetzte vorhanden"
     */
    public boolean auswahlStimmabgabe() {
        /*XXX UB wieder raus und zu "ohne UB"*/

//        if (tAuswahlSession.isOnlineteilnahmeAngebotenUB()
//                && eclZugeordneteMeldungListeM.isPraesenteVorhanden() == true) {
//            return true;
//        }
        return false;
    }

    /******************Registrierung/Einstellungen***********************************************/
    /**elektronischer Einladungsversand wird vom Emittenten angeboten und ist
     * für die Aktionärsklasse auch zulässig
     */
    public boolean elekEinladungsversandAngebotenUndZulaessig() {
        if (eclParamM.getRegistrierungFuerEmailVersandMoeglich() == 1
                && eclLoginDatenM.getEclLoginDaten().dauerhafteRegistrierungUnzulaessig == 0
                && eclLoginDatenM.liefereKennungArt() == 1
                && eclParamM.getParam().paramPortal.inHVPortalKeineEmailUndKeinPasswortFuerAktionaere==0) {
            return true;
        } else {
            return false;
        }
    }

    /**elektronischer Einladungsversand wird vom Emittenten angeboten aber ist
     * für die Aktionärsklasse nicht zulässig
     */
    public boolean elekEinladungsversandAngebotenAberNichtZulaessig() {
        if (eclParamM.getRegistrierungFuerEmailVersandMoeglich() == 1
                && (eclLoginDatenM.getEclLoginDaten().dauerhafteRegistrierungUnzulaessig == 1
                        || eclLoginDatenM.liefereKennungArt() == 2
                        || eclParamM.getParam().paramPortal.inHVPortalKeineEmailUndKeinPasswortFuerAktionaere==1
                        )) {
            return true;
        } else {
            return false;
        }
    }

    /**Dauerhafte Passwortvergabe wird vom Emittenten angeboten und ist
     * für die Aktionärsklasse auch zulässig - für (Standard)-HV-Portal
     */
    public boolean dauerhaftesPasswortAngebotenUndZulaessig() {
        if ((eclParamM.getDauerhaftesPasswortMoeglich() == 1 || eclParamM.getDauerhaftesPasswortMoeglich() == 2)
                && 
                (       /*Im Standardportal ist Mail und Passwort _nicht_ deaktiviert*/
                        eclParamM.getParam().paramPortal.inHVPortalKeineEmailUndKeinPasswortFuerAktionaere==0
                        ||
                        /*Kennung ist keine Aktionärskennung, sondern Gast*/
                        eclLoginDatenM.getEclLoginDaten().kennungArt==KonstLoginKennungArt.personenNatJur
                 )
                /*&& eclLoginDatenM.getEclLoginDaten().dauerhafteRegistrierungUnzulaessig == 0*/) {
            return true;
        } else {
            return false;
        }
    }

    /**Dauerhafte Passwortvergabe wird vom Emittenten angeboten und ist
     * für die Aktionärsklasse auch zulässig - für Permanentportal
     */
    public boolean pDauerhaftesPasswortAngebotenUndZulaessig() {
        if ((eclParamM.getDauerhaftesPasswortMoeglich() == 1 || eclParamM.getDauerhaftesPasswortMoeglich() == 2)
                /*&& eclLoginDatenM.getEclLoginDaten().dauerhafteRegistrierungUnzulaessig == 0*/) {
            return true;
        } else {
            return false;
        }
    }

    /**Dauerhafte Passwortvergabe wird vom Emittenten angeboten aber ist
     * für die Aktionärsklasse nicht zulässig
     */
    public boolean dauerhaftesPasswortAngebotenAberNichtZulaessig() {
        if ((eclParamM.getDauerhaftesPasswortMoeglich() == 1 || eclParamM.getDauerhaftesPasswortMoeglich() == 2)
                &&
                /*Im Standardportal ist Mail und Passwort _nicht_ deaktiviert*/
                eclParamM.getParam().paramPortal.inHVPortalKeineEmailUndKeinPasswortFuerAktionaere==0
                &&
                /*Kennung ist Aktionärskennung, nicht Gast*/
                eclLoginDatenM.getEclLoginDaten().kennungArt==KonstLoginKennungArt.aktienregister
                 
                /*&& eclLoginDatenM.getEclLoginDaten().dauerhafteRegistrierungUnzulaessig == 1*/) {
            return true;
        } else {
            return false;
        }
    }

    /**E-Mail-Hinterlegung wird angeboten (nur falls dauerhaftes Passwort oder
     * Elektronischer Einladungsversand angeboten und möglich)
     */
    public boolean eMailHinterlegungAngebotenUndZulaessig() {
        return ((/*Einladungsversand angeboten und zulässig => immer Passwort*/
                elekEinladungsversandAngebotenUndZulaessig() ||
                /*Passwort ist angeboten und zulässig*/
                dauerhaftesPasswortAngebotenUndZulaessig()
                ||
                /*Email wird immer angeboten*/
                eclParamM.getParam().paramPortal.emailNurBeiEVersandOderPasswort == 0)
                &&
                (       /*Im Standardportal ist Mail und Passwort _nicht_ deaktiviert*/
                        eclParamM.getParam().paramPortal.inHVPortalKeineEmailUndKeinPasswortFuerAktionaere==0
                        ||
                        /*Kennung ist keine Aktionärskennung, sondern Gast*/
                        eclLoginDatenM.getEclLoginDaten().kennungArt==KonstLoginKennungArt.personenNatJur
                 )
                );
    }

    /**Hinweis Aktionärs Portal anzuzeigen?*/
    public boolean hinweisAktionaersportalAnzeigen() {
        if ((eclParamM.getParam().paramPortal.bestaetigenHinweisAktionaersportal != 0
                && eclLoginDatenM.getEclLoginDaten().hinweisAktionaersPortalBestaetigt == 0)
                || eclParamM.getParam().paramPortal.bestaetigenHinweisAktionaersportal == 2 /*Dann immer bestätigen bei jeder Anmeldung!*/
        ) {
            return true;
        }
        return false;
    }

    /**Hinweis HV Portal anzuzeigen?*/
    public boolean hinweisHVportalAnzeigen() {
        if ((eclParamM.getParam().paramPortal.bestaetigenHinweisHVportal != 0
                && eclLoginDatenM.getEclLoginDaten().hinweisHVPortalBestaetigt == 0)
                || eclParamM
                        .getParam().paramPortal.bestaetigenHinweisHVportal == 2 /*Dann immer bestätigen bei jeder Anmeldung!*/
        ) {
            return true;

        }
        return false;
    }

    /**Permanent-Portal: Hinweis Sonstige 1 anzuzeigen?*/
    public boolean hinweisWeitere1Anzeigen() {
        if (tSession.isPermanentPortal() &&
                eclLoginDatenM.getEclLoginDaten().liefereBestaetigenHinweisWeitere(1)==0 &&
                eclParamM.getParam().paramPortal.liefereBestaetigenHinweisWeiter(1)==1
         ) {
            return true;

        }
        return false;
    }

    /**Permanent-Portal: Hinweis Sonstige 2 anzuzeigen?*/
    public boolean hinweisWeitere2Anzeigen() {
        if (tSession.isPermanentPortal() &&
                eclLoginDatenM.getEclLoginDaten().liefereBestaetigenHinweisWeitere(2)==0 &&
                eclParamM.getParam().paramPortal.liefereBestaetigenHinweisWeiter(2)==1
         ) {
            return true;

        }
        return false;
    }

    /**Falls einer der Hinweise (Portal oder hV) zu bestätigen ist, dann auch Datenschutzerklärungshinweis anzeigen*/
    public boolean hinweisDatenschutzerklaerungAnzeigen() {
        if ((hinweisAktionaersportalAnzeigen() == true || hinweisHVportalAnzeigen() == true)
                && eclParamM.getParam().paramPortal.separateDatenschutzerklaerung == 1) {
            return true;
        }
        return false;
    }
    
    /**true => Die Einstellungsseite hat FUnktionalität. Dies ist der Fall, wenn:
     * > Gast
     * > entsprechender Parameter gesetzt
     * @return
     */
    public boolean einstellungenMoeglich() {
       if (tEinstellungenSession.isErstregistrierung()) {return true;}
       if (eclLoginDatenM.getEclLoginDaten().kennungArt==KonstLoginKennungArt.personenNatJur){return true;}
       if (eclParamM.getParam().paramPortal.inHVPortalKeineEinstellungenFuerAktionaere==0) {return true;}
       return false;
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
        if ((eclParamM.isCheckboxBeiSRV() && (tWillenserklaerungSession.getIntAusgewaehlteAktion()==KonstPortalAktion.SRV_NEU
                || tWillenserklaerungSession.getIntAusgewaehlteAktion()==KonstPortalAktion.SRV_AENDERN
                || tWillenserklaerungSession.getIntAusgewaehlteAktion()==KonstPortalAktion.SRV_STORNIEREN))
                || (eclParamM.isCheckboxBeiBriefwahl() && (tWillenserklaerungSession.getIntAusgewaehlteAktion()==KonstPortalAktion.BRIEFWAHL_NEU
                        || tWillenserklaerungSession.getIntAusgewaehlteAktion()==KonstPortalAktion.BRIEFWAHL_AENDERN
                        || tWillenserklaerungSession.getIntAusgewaehlteAktion()==KonstPortalAktion.BRIEFWAHL_STORNIEREN))
                || (eclParamM.isCheckboxBeiKIAV() && (tWillenserklaerungSession.getIntAusgewaehlteAktion()==KonstPortalAktion.KIAV_MIT_WEISUNG_NEU
                        || tWillenserklaerungSession.getIntAusgewaehlteAktion()==KonstPortalAktion.KIAV_WEISUNG_AENDERN
                        || tWillenserklaerungSession.getIntAusgewaehlteAktion()==KonstPortalAktion.KIAV_STORNIEREN))

        ) {
            return true;
        }
        return false;
    }

    public boolean auswahlZuordnung() {
        if (eclParamM.getParam().paramPortal.teilnehmerKannSichWeitereKennungenZuordnen!=0) {
            return true;
        }
        return false;
    }
    
    
    
    /*************Div. Funktionen zum Anzeigen von Texten*****************/
    /**Haken "Ich bestätige, dass ich zur Abgabe der Willenserklärung berechtigt bin"*/
    public String textBerechtigtZurAbgabe() {
        /*TODO: aus Render raus wg. Performance*/
        String hString = "";
        switch (tWillenserklaerungSession.getIntAusgewaehlteAktion()) {
        case KonstPortalAktion.SRV_NEU:
            hString = eclPortalTexteM.holeText("607");
            break;
        case KonstPortalAktion.BRIEFWAHL_NEU:
            hString = eclPortalTexteM.holeText("608");
            break;
        case KonstPortalAktion.KIAV_MIT_WEISUNG_NEU:
            hString = eclPortalTexteM.holeText("609");
            break;
        case KonstPortalAktion.SRV_AENDERN:
            hString = eclPortalTexteM.holeText("610");
            break;
        case KonstPortalAktion.BRIEFWAHL_AENDERN:
            hString = eclPortalTexteM.holeText("611");
            break;
        case KonstPortalAktion.KIAV_WEISUNG_AENDERN:
            hString = eclPortalTexteM.holeText("612");
            break;
        case KonstPortalAktion.SRV_STORNIEREN:
            hString = eclPortalTexteM.holeText("613");
            break;
        case KonstPortalAktion.BRIEFWAHL_STORNIEREN:
            hString = eclPortalTexteM.holeText("614");
            break;
        case KonstPortalAktion.KIAV_STORNIEREN:
            hString = eclPortalTexteM.holeText("615");
            break;
        }
        return hString;
    }

    public String textBerechtigtZurAbgabeBeiBestaetigung() {
        String hString = "";
        switch (tWillenserklaerungSession.getIntAusgewaehlteAktion()) {
        case KonstPortalAktion.SRV_NEU:
        case KonstPortalAktion.SRV_AENDERN:
            hString = eclPortalTexteM.holeText("881");
            break;
        case KonstPortalAktion.BRIEFWAHL_NEU:
        case KonstPortalAktion.BRIEFWAHL_AENDERN:
           hString = eclPortalTexteM.holeText("882");
            break;
        case KonstPortalAktion.KIAV_MIT_WEISUNG_NEU:
        case KonstPortalAktion.KIAV_WEISUNG_AENDERN:
            hString = eclPortalTexteM.holeText("883");
            break;
        }
        return hString;
    }

    /*++++++++++++++++++++++++Einzel-Parameter+++++++++++++++++++++++*/
    /**Anzeige der Stimmen*/
    public boolean stimmenAnzeigen() {
        if (eclParamM.getParam().paramPortal.anzeigeStimmen == 0) {
            return false;
        }
        return true;
    }
    
    /*+++++++++++++++++++Mandantenabhängige Abfragen+++++++++++++++++*/
    public boolean liefereIstku287() {
        return ParamSpezial.ku287(eclParamM.getParam().mandant);
    }

    /*++++++++++++++++++Sonderablauf Briefwahl als Online-Stimmabgabe+++++++++++++++*/
    public boolean liefereBriefwahlStornierenZulaessig() {
        if (eclParamM.getParam().paramPortal.briefwahlAlsOnlineStimmabgabe==1) {
            return false;
        }
        return true;
    }
    public boolean liefereBriefwahlIstStimmabgabe() {
        if (eclParamM.getParam().paramPortal.briefwahlAlsOnlineStimmabgabe==1) {
            return true;
        }
        return false;
        
    }
}
