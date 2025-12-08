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

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComAllg.CaString;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEclM.EclDbM;
import de.meetingapps.meetingportal.meetComEclM.EclParamM;
import de.meetingapps.meetingportal.meetComEntities.EclBesitzAREintrag;
import de.meetingapps.meetingportal.meetComEntities.EclZugeordneteMeldungNeu;
import de.meetingapps.meetingportal.meetComHVParam.ParamSpezial;
import de.meetingapps.meetingportal.meetComKonst.KonstPortalAktion;
import de.meetingapps.meetingportal.meetComKonst.KonstPortalView;
import de.meetingapps.meetingportal.meetComKonst.KonstWillenserklaerungVersandartEK;
import de.meetingapps.meetingportal.meetingportTFunktionen.TFunktionen;
import de.meetingapps.meetingportal.meetingportTFunktionen.TPruefeStartNachOpen;
import de.meetingapps.meetingportal.meetingportTFunktionen.TSessionVerwaltung;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@RequestScoped
@Named
public class TEintrittskarte {

    private int logDrucken=3;
    

    private @Inject TSessionVerwaltung tSessionVerwaltung;
    private @Inject TFunktionen tFunktionen;
    private @Inject TPruefeStartNachOpen tPruefeStartNachOpen;

    private @Inject TWillenserklaerung tWillenserklaerung;
    private @Inject TWillenserklaerungSession tWillenserklaerungSession;
    private @Inject TSession tSession;

    private @Inject EclParamM eclParamM;
    private @Inject EclDbM eclDbM;

    public void doZurueck() {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.EINTRITTSKARTE)) {
            return;
        }

        tSessionVerwaltung.setzeEnde(tFunktionen.waehleAuswahl());

    }

    public void doAusstellen() {
        CaBug.druckeLog("", logDrucken, 10);
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.EINTRITTSKARTE)) {
            return;
        }
        try {

        boolean ergBool = false;
        /****Überprüfen, ob Eingabe vollständig erfolgt ist für AktionärsEintrittskarte(n)****/
        ergBool = pruefeEingabenFuerAktionaerEintrittskarte();
        if (ergBool == false) {
            CaBug.druckeLog("false Nach pruefeEingabenFuerAktionaerEintrittskarte" , logDrucken, 10);
            return;
        }

        //        /********Überprüfen, ob Eingaben für Gastkarte vollständig ist****************************************/
        //        aDlgVariablen.setGastkarteNrPersNatJur(0);
        //        if (aDlgVariablen.isGastKarte()){
        //            ergBool=pruefeEingabenFuerGastkarte();
        //            if (ergBool==false){aFunktionen.setzeEnde();return "";}
        //        }

        /*********Initialisieren************/
        eclDbM.openAll();
        if (tPruefeStartNachOpen.pruefeNachOpenPortalAktuelleHauptAktionAktion()==false) {
            eclDbM.closeAllAbbruch();
            return;
        }

        

        /***********Eintrittskarte für Aktionär**********/
        CaBug.druckeLog("Vor anlegenAktionaerEK" , logDrucken, 10);
        ergBool = anlegenAktionaerEK(eclDbM.getDbBundle(), true);
        if (ergBool == false) {
            CaBug.druckeLog("anlegenAktionaerEK==false" , logDrucken, 10);
            eclDbM.closeAllAbbruch();
            return;
        }

        //        /****Eintrittskarte für Gast****/
        //        if (aDlgVariablen.isGastKarte()){
        //            ergBool=anlegenGastkarte(aktienregisterEintrag, eclDbM.getDbBundle());
        //            if (ergBool==false){
        //                if (aDlgVariablen.getFehlerNr()==CaFehler.afAndererUserAktiv){
        //                    eclDbM.closeAllAbbruch();
        //                    return aFunktionen.setzeEnde("aDlgAbbruch", true, true);
        //
        //                }
        //                eclDbM.closeAllAbbruch();
        //                aFunktionen.setzeEnde();return "";
        //            }
        //            
        //
        //        }

        eclDbM.closeAll();
        CaBug.druckeLog("Normales Ende" , logDrucken, 10);
        } catch (Exception e) {
            CaBug.drucke("Exception");
            System.out.println(e.getMessage());
            e.printStackTrace();
            eclDbM.closeAll();
        }
       tSessionVerwaltung.setzeEnde(KonstPortalView.EINTRITTSKARTE_QUITTUNG);
        return;
    }

    /*********************Business-Funktionen******************************/
    /****************************Funktionen für Aktionärs-Eintrittskarte*********************************/

    /**Für Eintrittskarte/Aktionär: prüfen, ob alle Eingabefelder richtig und vollständig gefüllt sind
     * 
     * Eingabefelder:
     * 
     *      DlgVariablen.ausgewaehlteAktion (Legt fest, welche Eintrittskarten ausgestellt werden sollen)
     *      DlgVariablen.ueberOeffentlicheID
     *      DlgVariablen.eintrittskarteVersandart (außer bei Übertragung mittels öffentlicher ID)
     *      DlgVariablen.eintrittskarteEmail (je nach Versandart)
     *      DlgVariablen.vollmachtName/.vollmachtVorname/.vollmachtOrt
     * 
     *      Falls 2. Eintrittskarte angefordert (bei Personengemeinschaften ....:
     *      DlgVariablen.eintrittskarteVersandar2 (außer bei Übertragung mittels öffentlicher ID)
     *      DlgVariablen.eintrittskarteEmail2 (je nach Versandart)
     *      DlgVariablen.vollmachtName2/.vollmachtVorname2/.vollmachtOrt2
     * 
     * Returnwert:  true = ok, 
     *              false= Daten sind falsch, Fehlermeldung in DlgVariablen.fehlerMeldung/.fehlerNr
     * */
    public boolean pruefeEingabenFuerAktionaerEintrittskarte() {

        /*1. Eintrittskarte*/
        //        if (aDlgVariablen.isUeberOeffentlicheID()){
        //            aDlgVariablen.setEintrittskarteVersandart("");
        //        }
        //        else{
        if (tWillenserklaerungSession.getEintrittskarteVersandart() == null
                || tWillenserklaerungSession.getEintrittskarteVersandart().isEmpty()) {
            if (zweiEKWerdenAusgestellt()) {
                /*Erste EK*/
                tSession.trageFehlerEin(CaFehler.afAusstellungsArtErsteEKFehlt);
            } else {
                /*Nur eine EK vorhanden*/
                tSession.trageFehlerEin(CaFehler.afAusstellungsArtEKFehlt);
            }
            tSessionVerwaltung.setzeEnde();
            return false;
        }
        //        }

        switch (Integer.parseInt(tWillenserklaerungSession.getEintrittskarteVersandart())) {
        case KonstWillenserklaerungVersandartEK.VERSANDLAUF_ADRESSE_ABWEICHEND_WIE_EINGEGEBEN:
            /*Aufnahme in Sammelbatch, an Versandadresse - bei nächstem Drucklauf ausdrucken und versenden => abweichende Versandadresse gültig?*/
            /*Tja - wie kann man die Versandadresse überprüfen?*/
            break;
        case KonstWillenserklaerungVersandartEK.ONLINE_EMAIL:
            /*Versand per Email (im Portal) erfolgt => Mailadresse gültig?*/
            if (tWillenserklaerungSession.getEintrittskarteEmail() == null
                    || tWillenserklaerungSession.getEintrittskarteEmail().isEmpty()
                    || !CaString.isMailadresse(tWillenserklaerungSession.getEintrittskarteEmail())) {
                if (zweiEKWerdenAusgestellt()) {
                    /*Erste EK*/
                    tSession.trageFehlerEin(CaFehler.afKeineGueltigeEmailErsteEK);
                } else { /*Nur eine EK vorhanden*/
                    tSession.trageFehlerEin(CaFehler.afKeineGueltigeEmailEK);
                }
                tSessionVerwaltung.setzeEnde();
                return false;
            }
            if (tWillenserklaerungSession.getEintrittskarteEmailBestaetigen() == null
                    || tWillenserklaerungSession.getEintrittskarteEmailBestaetigen().isEmpty()
                    || tWillenserklaerungSession.getEintrittskarteEmailBestaetigen()
                            .compareTo(tWillenserklaerungSession.getEintrittskarteEmail()) != 0) {
                if (zweiEKWerdenAusgestellt()) {
                    /*Erste EK*/
                    tSession.trageFehlerEin(CaFehler.afEmailErsteEKBestaetigungFalsch);
                } else { /*Nur eine EK vorhanden*/
                    tSession.trageFehlerEin(CaFehler.afEmailEKBestaetigungFalsch);
                }
                tSessionVerwaltung.setzeEnde();
                return false;
            }
            break;
        }

        if (tWillenserklaerungSession.getIntAusgewaehlteAktion() == KonstPortalAktion.ZWEI_EK_SELBST_PERSGEMEINSCHAFT
                || tWillenserklaerungSession.getIntAusgewaehlteAktion() == KonstPortalAktion.EINE_EK_MIT_VOLLMACHT

                ||

                /*Bei 2 Eintrittskarten für alle: Vollmacht ist optional => Vollamcht nur überprüfen, wenn eines der Vollmachtsfelder gefüllt - war früher, jetzt: falls Häckchen gesetzt*/
                (tWillenserklaerungSession.getIntAusgewaehlteAktion() == KonstPortalAktion.ZWEI_EK_SELBST_ODER_VOLLMACHT
                        &&
                        /*(!aDlgVariablen.getVollmachtName().isEmpty() || !aDlgVariablen.getVollmachtVorname().isEmpty() || !aDlgVariablen.getVollmachtOrt().isEmpty())*/ /*War mal :-) */
                        tWillenserklaerungSession.isVollmachtEingeben())) {
            /*Vollmacht prüfen*/
            CaBug.druckeLog("Vollmacht prüfen", logDrucken, 10);
            if (ParamSpezial.ku303mitVerbaenden(eclParamM.getClGlobalVar().mandant)==false) {
                CaBug.druckeLog("Nicht ku302_303 mit Verbänden", logDrucken, 10);
                if (tWillenserklaerungSession.getVollmachtName().isEmpty()) {
                    if (zweiEKWerdenAusgestellt()) { /*Erste EK*/
                        tSession.trageFehlerEin(CaFehler.afVollmachtNameErsteEKFehlt);
                    } else { /*Nur eine EK vorhanden*/
                        tSession.trageFehlerEin(CaFehler.afVollmachtNameEKFehlt);
                    }
                    tSessionVerwaltung.setzeEnde();
                    return false;
                }
                if (tWillenserklaerungSession.getVollmachtOrt().isEmpty()) {
                    if (zweiEKWerdenAusgestellt()) { /*Erste EK*/
                        tSession.trageFehlerEin(CaFehler.afVollmachtOrtErsteEKFehlt);
                    } else { /*Nur eine EK vorhanden*/
                        tSession.trageFehlerEin(CaFehler.afVollmachtOrtEKFehlt);
                    }
                    tSessionVerwaltung.setzeEnde();
                    return false;
                }
            }
            else {
                CaBug.druckeLog("Spezialbehandlung ku302_303 mit Verbänden", logDrucken, 10);
                /**Spezialbehandlung ku302_303 mit Verbänden*/
                if ((/*Beide ausgewählt?*/
                        tWillenserklaerungSession.getVollmachtName().trim().isEmpty() && tWillenserklaerungSession.pruefeObNzVerbandAusgewaehlt()==false)
                        ||
                        (/*Keiner ausgewählt*/
                                (!tWillenserklaerungSession.getVollmachtName().trim().isEmpty()) && tWillenserklaerungSession.pruefeObNzVerbandAusgewaehlt()==true)

                        ) {
                    tWillenserklaerungSession.setNzVerband(""); /*Verband sicherheitshalber auf leer setzen, da nicht löschbar!*/
                    tSession.trageFehlerEin(CaFehler.nzVollmachtPersonOderVerband);
                    tSessionVerwaltung.setzeEnde();
                    return false;
                }
            }
            if (eclParamM.liefereCheckboxBeiVollmacht()==true) {
                /*Überprüfen, ob Checkbox zu Vertretungsbedingungen angehakt*/
                if (tWillenserklaerungSession.isBestaetigtDassBerechtigt()==false) {
                    tSession.trageFehlerEin(CaFehler.afHinweisBeiVollmachtBestaetigt);
                    tSessionVerwaltung.setzeEnde();
                    return false;
                }
            }
            if (ParamSpezial.ku302_303(eclParamM.getClGlobalVar().mandant)) {
                /*Prüfen, ob Vertretungsart angehakt*/
                if (tWillenserklaerungSession.getVollmachtName().trim().isEmpty()==false && tWillenserklaerungSession.pruefeObNZVertretungsartAusgewaehlt()==false) {
                    tSession.trageFehlerEin(CaFehler.nzVollmachtVertretungsartEingeben);
                    tSessionVerwaltung.setzeEnde();
                    return false;
                }
            }
        }

        /*Ggf.2. Eintrittskarte*/
        if (zweiEKWerdenAusgestellt()) {
            if (tWillenserklaerungSession.getEintrittskarteVersandart2() == null
                    || tWillenserklaerungSession.getEintrittskarteVersandart2().isEmpty()) {
                tSession.trageFehlerEin(CaFehler.afAusstellungsArtZweiteEKFehlt);
                tSessionVerwaltung.setzeEnde();
                return false;
            }

            switch (Integer.parseInt(tWillenserklaerungSession.getEintrittskarteVersandart2())) {
            case KonstWillenserklaerungVersandartEK.VERSANDLAUF_ADRESSE_ABWEICHEND_WIE_EINGEGEBEN:
                /*Aufnahme in Sammelbatch, an Versandadresse - bei nächstem Drucklauf ausdrucken und versenden => abweichende Versandadresse gültig?*/
                /*Tja - wie kann man die Versandadresse überprüfen?*/
                break;
            case KonstWillenserklaerungVersandartEK.ONLINE_EMAIL:
                /*Versand per Email (im Portal) erfolgt => Mailadresse gültig?*/
                if (tWillenserklaerungSession.getEintrittskarteEmail2() == null
                        || tWillenserklaerungSession.getEintrittskarteEmail2().isEmpty()
                        || !CaString.isMailadresse(tWillenserklaerungSession.getEintrittskarteEmail2())) {
                    tSession.trageFehlerEin(CaFehler.afKeineGueltigeEmailZweiteEK);
                    tSessionVerwaltung.setzeEnde();
                    return false;
                }
                if (tWillenserklaerungSession.getEintrittskarteEmail2Bestaetigen() == null
                        || tWillenserklaerungSession.getEintrittskarteEmail2Bestaetigen().isEmpty()
                        || tWillenserklaerungSession.getEintrittskarteEmail2Bestaetigen()
                                .compareTo(tWillenserklaerungSession.getEintrittskarteEmail2()) != 0) {
                    tSession.trageFehlerEin(CaFehler.afEmailZweiteEKBestaetigungFalsch);
                    tSessionVerwaltung.setzeEnde();
                    return false;
                }
                break;
            }

            if (tWillenserklaerungSession
                    .getIntAusgewaehlteAktion() == KonstPortalAktion.ZWEI_EK_SELBST_PERSGEMEINSCHAFT ||

            /*Bei 2 Eintrittskarten für alle: Vollmacht ist optional => Vollamcht nur überprüfen, wenn eines der Vollmachtsfelder gefüllt*/
                    (tWillenserklaerungSession
                            .getIntAusgewaehlteAktion() == KonstPortalAktion.ZWEI_EK_SELBST_ODER_VOLLMACHT
                            && /*(!aDlgVariablen.getVollmachtName2().isEmpty() || !aDlgVariablen.getVollmachtVorname2().isEmpty() || !aDlgVariablen.getVollmachtOrt2().isEmpty())*/
                            tWillenserklaerungSession.isVollmachtEingeben2())) {

                /*Vollmacht prüfen*/
                if (tWillenserklaerungSession.getVollmachtName2().isEmpty()) {
                    tSession.trageFehlerEin(CaFehler.afVollmachtNameZweiteEKFehlt);
                    tSessionVerwaltung.setzeEnde();
                    return false;
                }
                if (tWillenserklaerungSession.getVollmachtOrt2().isEmpty()) {
                    tSession.trageFehlerEin(CaFehler.afVollmachtOrtZweiteEKFehlt);
                    tSessionVerwaltung.setzeEnde();
                    return false;
                }
            }
        }

        return true;
    }

    
    @Deprecated
    public boolean pruefeObZulaessig() {
        /*Prüfen, ob gemäß "Phase" noch zulässig*/
        if (tWillenserklaerungSession.getIntAusgewaehlteHauptAktion() == KonstPortalAktion.HAUPT_NEUANMELDUNG) {
            /*Neuanmeldung - Anmeldephase?*/
            if (tSession.isPortalErstanmeldungIstMoeglich() == false) {
                return false;
            }
        }
 
        boolean zulaessig = tSession.isPortalEKIstMoeglich();
        if (zulaessig) {
            switch (tWillenserklaerungSession.getIntAusgewaehlteAktion()) {
            case KonstPortalAktion.EINE_EK_SELBST:
                zulaessig = (eclParamM.getEkSelbstMoeglich() == 1);
                break;
            case KonstPortalAktion.ZWEI_EK_SELBST_FUER_ALLE:
                zulaessig = (eclParamM.getEk2SelbstMoeglich() == 1);
                break;
            case KonstPortalAktion.EINE_EK_MIT_VOLLMACHT:
                zulaessig = (eclParamM.getEkVollmachtMoeglich() == 1);
                break;
            }
        }

        return zulaessig;
    }
    
    /**Für Aktionärseintrittskarte: Anmelden, Zuordnen zum Aktienregistereintrag, PDF vorbereiten, ggf. Emailen
     * 
     * Parameter:
     * 
     * Eingabefelder:
     * 
     *      EclTeilnehmerLoginM.anmeldeIdentPersonenNatJur
     * 
     *      DlgVariablen.ausgewaehlteHauptAktion (Erstanmeldung oder Folgeausstellungen)
     * 
     *      DlgVariablen.ausgewaehlteAktion (Legt fest, welche Eintrittskarten ausgestellt werden sollen)
     *      DlgVariablen.ueberOeffentlicheID
     *      DlgVariablen.personNatJurOeffentlicheID
     * 
     *      DlgVariablen.eintrittskarteVersandart (außer bei Übertragung mittels öffentlicher ID)
     *      je nach eintrittskarteVersandart:
     *          DlgVariablen.eintrittskarteAbweichendeAdressse1/2/3/4/5
     *          DlgVariablen.eintrittskarteEmail
     *      DlgVariablen.vollmachtName/.vollmachtVorname/.vollmachtOrt
     * 
     *      Falls 2. Eintrittskarte angefordert (bei Personengemeinschaften, oder auch bei allen ....:
     *      DlgVariablen.eintrittskarteVersandart2 (außer bei Übertragung mittels öffentlicher ID)
     *      je nach eintrittskarteVersandart2:
     *          DlgVariablen.eintrittskarteAbweichendeAdressse12/22/32/42/52
     *          DlgVariablen.eintrittskarteEmail2
     *      DlgVariablen.vollmachtName2/.vollmachtVorname2/.vollmachtOrt2
     * 
     *      Für Hauptaktion == 1 (d.h. erstmaliges Anmelden):
     *          EclTeilnehmerLoginM.anmeldeAktionaersnummer (zu dieser Nummer werden die Anmeldungen erstellt)
     * 
     *      Für Hauptaktion ==2 (d.h. Anmeldung existiert bereits)
     *          EclzugeordneteMeldungM    Meldung, von der die Aktion ausgeht
     *          EclTeilnehmerLoginM.anmeldeAktionaersnummer (zu dieser Nummer werden die Anmeldungen erstellt;
     *                  falls "Aussteller" = Aktionär selbst!)
     * 
     * Returnwert:
     *      true => ok
     *      false => DlgVariablen.fehlerMeldung/.fehlerNr gesetzt
     * 
     * Ausgabefelder:
     *      AControllerEintrittskarte.aktienregisterEintrag (wird später noch für Gastkartenausstellung benötigt)
     * 
     *      DlgVariablen.eintrittskartePdfNr
     *      DlgVariablen.eintrittskartePdfNr2
     * 
     * 
     * */
    public boolean anlegenAktionaerEK(DbBundle pDbBundle, boolean pMitBestaetigung) {

        if (tWillenserklaerungSession.getIntAusgewaehlteHauptAktion() == KonstPortalAktion.HAUPT_NEUANMELDUNG) {
            for (EclBesitzAREintrag iEclBesitzAREintrag : tWillenserklaerungSession.getBesitzAREintragListe()) {
                boolean brc = tWillenserklaerung.anmelden1Meldung(iEclBesitzAREintrag, false);
                if (brc == false) {
                    return false;
                }
                brc = tWillenserklaerung.eintrittskarte(null, pMitBestaetigung);
                if (brc == false) {
                    return false;
                }
            }
        } else {
            for (int i = 0; i < tWillenserklaerungSession.getZugeordneteMeldungFuerAusfuehrungListe().size(); i++) {
                EclZugeordneteMeldungNeu iEclZugeordneteMeldungNeu = tWillenserklaerungSession
                        .getZugeordneteMeldungFuerAusfuehrungListe().get(i);
                boolean brc = tWillenserklaerung
                        .checkKeineNeueWillenserklaerungFuerAnmeldung(iEclZugeordneteMeldungNeu);
                if (brc == false) {
                    return false;
                }

                /*Zwar bereits angemeldet, aber nun zwei EKs ausgestellt.
                 * D.h. hier ist Sonderfall: bisherige (1) Anmeldung muß storniert werden, und zwei neue Anmeldungen
                 * erzeugt werden
                 */
                if (KonstPortalAktion.wkErfordertZweiMeldungen(tWillenserklaerungSession.getIntAusgewaehlteAktion())) {
                    brc = tWillenserklaerung.storniereMeldungErzeugeZweiMeldungen(iEclZugeordneteMeldungNeu);
                    if (brc == false) {
                        return false;
                    }
                }
                brc = tWillenserklaerung.eintrittskarte(iEclZugeordneteMeldungNeu, pMitBestaetigung);
                if (brc == false) {
                    return false;
                }
            }
        }

        //        if (aDlgVariablen.getAusgewaehlteHauptAktion().compareTo("1")==0){
        //            /******************************Anmelden - aus Aktienregister***************************************/
        //            lWillenserklaerung=new BlWillenserklaerung();
        // 
        //             /*Login-Daten füllen - wg. Initial-Passwort*/
        //            leseAktienregisterLoginDatenZuAktienregisterEintrag(pDbBundle);
        //
        //            /*Restliche Parameter füllen*/
        //             
        //        } /***********************Anmelden beendet****************************************************/
        //
        //        else{/*Hauptaktion ==2*/
        //
        //
        //            if (KonstPortalAktion.wkErfordertZweiMeldungen(tWillenserklaerungSession.getIntAusgewaehlteAktion())){
        //                
        //                /*Login-Daten füllen - wg. Initial-Passwort*/
        //                leseAktienregisterLoginDatenZuAktienregisterEintrag(pDbBundle);
        //
        //               
        //                
        //            }
        //
        //            else{
        //                /************Anmeldung wurde bereits in früherem Vorgang durchgeführt; entsprechende Variablen füllen********/
        //                if (eclZugeordneteMeldungM.getArtBeziehung()==1){
        //                    aktienregisterEintrag=new EclAktienregister();
        //                    aktienregisterEintrag.aktionaersnummer=eclTeilnehmerLoginM.getAnmeldeAktionaersnummer();
        //                    erg=pDbBundle.dbAktienregister.leseZuAktienregisterEintrag(aktienregisterEintrag);
        //                    if (erg<=0){/*Aktienregistereintrag nicht mehr vorhanden*/
        //
        //                        aDlgVariablen.setFehlerMeldung(eclPortalTexteM.getFehlertext(CaFehler.afAktienregisterEintragNichtMehrVorhanden));
        //                        aDlgVariablen.setFehlerNr(CaFehler.afAktienregisterEintragNichtMehrVorhanden);
        //                        return false;
        //                    }
        //                    aktienregisterEintrag=pDbBundle.dbAktienregister.ergebnisPosition(0);
        //                    /*Login-Daten füllen - wg. Initial-Passwort*/
        //                    leseAktienregisterLoginDatenZuAktienregisterEintrag(pDbBundle);
        //
        //                }
        //                else{
        //                    EclMeldung hMeldung=new EclMeldung();
        //                    hMeldung.meldungsIdent=eclZugeordneteMeldungM.getMeldungsIdent();
        //                    pDbBundle.dbMeldungen.leseZuMeldungsIdent(hMeldung);
        //                    aktienregisterEintrag=new EclAktienregister();
        //                    aktienregisterEintrag.aktionaersnummer=pDbBundle.dbMeldungen.meldungenArray[0].aktionaersnummer;
        //                    erg=pDbBundle.dbAktienregister.leseZuAktienregisterEintrag(aktienregisterEintrag);
        //                    if (erg<=0){/*Aktienregistereintrag nicht mehr vorhanden*/
        //                        aDlgVariablen.setFehlerMeldung(eclPortalTexteM.getFehlertext(CaFehler.afAktienregisterEintragNichtMehrVorhanden));
        //                        aDlgVariablen.setFehlerNr(CaFehler.afAktienregisterEintragNichtMehrVorhanden);
        //                        return false;
        //                    }
        //                    aktienregisterEintrag=pDbBundle.dbAktienregister.ergebnisPosition(0);
        //                    /*Login-Daten füllen - wg. Initial-Passwort*/
        //                    leseAktienregisterLoginDatenZuAktienregisterEintrag(pDbBundle);
        //
        //
        //                }
        //            }
        //
        //        }

        return true;
    }

    /**Liefert true, wenn eine EK-Variante mit 2 EKs gewählt wurde, bei der Versandart, Vollmacht etc. getrennt
     * eingegeben werden können
    */
    private boolean zweiEKWerdenAusgestellt() {
        if (tWillenserklaerungSession.getIntAusgewaehlteAktion() == KonstPortalAktion.ZWEI_EK_SELBST_PERSGEMEINSCHAFT
                || tWillenserklaerungSession
                        .getIntAusgewaehlteAktion() == KonstPortalAktion.ZWEI_EK_SELBST_ODER_VOLLMACHT) {
            return true;
        }

        return false;
    }

}
