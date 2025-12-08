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

import java.io.IOException;
import java.util.List;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComBlManaged.BlMAbstimmung;
import de.meetingapps.meetingportal.meetComBlManaged.BlMAbstimmungsvorschlag;
import de.meetingapps.meetingportal.meetComEclM.EclAbstimmungM;
import de.meetingapps.meetingportal.meetComEclM.EclAbstimmungenListeM;
import de.meetingapps.meetingportal.meetComEclM.EclDbM;
import de.meetingapps.meetingportal.meetComEclM.EclKIAVM;
import de.meetingapps.meetingportal.meetComEclM.EclParamM;
import de.meetingapps.meetingportal.meetComEntities.EclBesitzJeKennung;
import de.meetingapps.meetingportal.meetComEntities.EclMeldung;
import de.meetingapps.meetingportal.meetComEntities.EclWeisungMeldung;
import de.meetingapps.meetingportal.meetComEntities.EclWeisungMeldungRaw;
import de.meetingapps.meetingportal.meetComEntities.EclWillenserklaerungStatusNeu;
import de.meetingapps.meetingportal.meetComEntities.EclZugeordneteMeldungNeu;
import de.meetingapps.meetingportal.meetComKonst.KonstMeldung;
import de.meetingapps.meetingportal.meetComKonst.KonstPortalAktion;
import de.meetingapps.meetingportal.meetComKonst.KonstPortalFehlerView;
import de.meetingapps.meetingportal.meetComKonst.KonstPortalFunktionen;
import de.meetingapps.meetingportal.meetComKonst.KonstPortalUnterlagen;
import de.meetingapps.meetingportal.meetComKonst.KonstPortalView;
import de.meetingapps.meetingportal.meetComKonst.KonstSkIst;
import de.meetingapps.meetingportal.meetComKonst.KonstWeisungenSind;
import de.meetingapps.meetingportal.meetComKonst.KonstWeisungserfassungSicht;
import de.meetingapps.meetingportal.meetComKonst.KonstWillenserklaerung;
import de.meetingapps.meetingportal.meetComKonst.KonstZugeordneteMeldungArtBeziehung;
import de.meetingapps.meetingportal.meetingSocket.BsClient;
import de.meetingapps.meetingportal.meetingportTFunktionen.TFunktionen;
import de.meetingapps.meetingportal.meetingportTFunktionen.TPortalFunktionen;
import de.meetingapps.meetingportal.meetingportTFunktionen.TPruefeStartNachOpen;
import de.meetingapps.meetingportal.meetingportTFunktionen.TSessionVerwaltung;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@RequestScoped
@Named
public class TAuswahl {

    private int logDrucken = 3;

    private @Inject EclDbM eclDbM;
    private @Inject TFunktionen tFunktionen;
    private @Inject EclParamM eclParamM;
    private @Inject TSessionVerwaltung tSessionVerwaltung;
    private @Inject TPortalFunktionen tPortalFunktionen;

    private @Inject TSession tSession;

    private @Inject TAuswahlSession tAuswahlSession;
    private @Inject TWillenserklaerungSession tWillenserklaerungSession;

    private @Inject TEintrittskarteQuittungUDetail tEintrittskarteQuittungUDetail;
    private @Inject TEintrittskarteStornierenSession tEintrittskarteStornierenSession;
    private @Inject TEinstellungen tEinstellungen;
    private @Inject TPraesenzZugangAbgang tPraesenzZugangAbgang;
    private @Inject TStimmabgabe tStimmabgabe;
    private @Inject TUnterlagen tUnterlagen;
    private @Inject TMitteilung tMitteilung;
    private @Inject TMonitoring tMonitoring;
    private @Inject TZuordnung tZuordnung;
    private @Inject TGaeste tGaeste;
    private @Inject TFehlerViewSession tFehlerViewSession;

    private @Inject TTeilnehmerverz tTeilnehmerverz;
    private @Inject TAbstimmungserg tAbstimmungserg;
    private @Inject TBesitzVertretungAbfragen tBesitzVertretungAbfragen;
    private @Inject EclKIAVM eclKIAVM;
    private @Inject EclAbstimmungenListeM eclAbstimmungenListeM;
    private @Inject BlMAbstimmung blMAbstimmung;
    private @Inject BlMAbstimmungsvorschlag blMAbstimmungsvorschlag;
    private @Inject TPruefeStartNachOpen tPruefeStartNachOpen;

    /*Temporär/
     * 
     */
    public void doNachrichtSenden() {
        System.out.println("doNachrichtSenden");
        BsClient bsClient = new BsClient(eclParamM.getBmSocketadresse());
        try {
            bsClient.sendenNachricht(tAuswahlSession.getKennung(), 1, tAuswahlSession.getNachricht());
            bsClient.disconnectFromWebSocket();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*+++++++++++++++++++++Start-Sequenzen+++++++++++++++++++++*/
    /**
     * eclDbM-Handling in aufrufender Funktion, falls bereitsOffen==true, sonst wird das
     * hier gehandelt (wg. nurRawLiveAbstimmung)
     * 
     * Return-Wert:
     * <0 => 
     * 		tSession.trageFehlerEin(erg);
     * 		tFunktionen.setzeEnde("tDlgFehlerSysLogout", false, false);
     * >0 => 
     * 		tFunktionen.setzeEnde(rc);
     * */
    public int startAuswahl(boolean bereitsOffen) {
        
        if ( tSession.isPermanentPortal()) {
            return KonstPortalView.P_AUSWAHL;
        }
        if (bereitsOffen == false) {
            eclDbM.openAll();
        }

        angebotenAuswahl();

        int rcStatus = 1;
        if (eclParamM.getParam().paramPortal.nurRawLiveAbstimmung == 0 || tSession.isStatusIstGeladen() == false) {
            rcStatus = tFunktionen.leseStatusPortal(eclDbM.getDbBundle());
        }

        if (bereitsOffen == false) {
            eclDbM.closeAll();
        }

        int rc = 0;
        if (rcStatus == 2) {
            /*Abfrage aufrufen, ob Besitze mit Weisungen durch andere erteilt vertreten werden sollen oder nicht*/
            rc = KonstPortalView.BESITZ_VERTRETUNG_ABFRAGEN;
            tBesitzVertretungAbfragen.doInit();
        } else {
            if (eclParamM.getParam().paramPortal.varianteDialogablauf == 0) {
                rc = KonstPortalView.AUSWAHL;
            } else {
                rc = KonstPortalView.AUSWAHL1;
            }
        }

        return rc;
    }

    /**Wird bei startAuswahl aufgerufen, kann aber auch separat aufgerufen werden.
     * Benötigt "eigentlich" kein offenes EclDbM (nur für Nachladen ...)
     * @return
     */
    public int angebotenAuswahl() {
        CaBug.druckeLog("", logDrucken, 10);
        tPortalFunktionen.belegeAnzahlFragenGestellt(eclDbM.getDbBundle());
        tPortalFunktionen.belegeAngebotenAktivFuerAuswahl();
        tPortalFunktionen.belegeAktivFuerWillenserklaerungen();
        return 1;
    }

    /********************Buttons****************************/
    /*+++++++++++++Für bereits angemeldete Bestände+++++++++++++++++++*/
    public void doStornieren(EclZugeordneteMeldungNeu pZugeordneteMeldung, EclWillenserklaerungStatusNeu pWillenserklaerungStatus) {
        /**Aufruf aus JSF*/
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.AUSWAHL)) {
            return;
        }

        /*Öffnen*/
        eclDbM.openAll();
        
        /*Sicherheitshalber einlesen, damit nicht schon beim Start "beendet" wird*/
        tWillenserklaerungSession.ermittleGattungenFuerBesitzAREintragListe();
        blMAbstimmung.leseWeisungsliste(tWillenserklaerungSession.getGattungVorhanden(), KonstSkIst.srv, KonstWeisungserfassungSicht.portalWeisungserfassung);

        
        if (!tPruefeStartNachOpen.pruefeNachOpenPortalWillenserklaerung(pWillenserklaerungStatus.willenserklaerung, 1)) {
            eclDbM.closeAll();
            return;
        }

        boolean brc = stornierenAusfuehren(pZugeordneteMeldung, pWillenserklaerungStatus, KonstWeisungserfassungSicht.portalWeisungserfassung);

        if (brc == false) {
            eclDbM.closeAllAbbruch();
            tSessionVerwaltung.setzeEnde();
            return;
        }

        eclDbM.closeAll();
        return;
    }

    public boolean stornierenAusfuehren(EclZugeordneteMeldungNeu pZugeordneteMeldungM, EclWillenserklaerungStatusNeu pWillenserklaerungStatusM, int pSicht) {
        int lSkIst = 0;

        tWillenserklaerungSession.clear();

        tWillenserklaerungSession.initZugeordneteMeldungFuerAusfuehrungListe();
        tWillenserklaerungSession.addZugeordneteMeldungFuerAusfuehrungListe(pZugeordneteMeldungM, pWillenserklaerungStatusM);
        tWillenserklaerungSession.ermittleGattungenFuerZugeordneteMeldungFuerAusfuehrungListe();
        tWillenserklaerungSession.ermittleGattungFuerEinzigenBesitzZugeordneteMeldungFuerAusfuehrungList();

        CaBug.druckeLog("pWillenserklaerungStatusM.getWillenserklaerung()=" + pWillenserklaerungStatusM.getWillenserklaerung(), logDrucken, 5);

        switch (pWillenserklaerungStatusM.getWillenserklaerung()) {

        case KonstWillenserklaerung.briefwahl:
        case KonstWillenserklaerung.aendernBriefwahl:

        case KonstWillenserklaerung.vollmachtUndWeisungAnKIAV:
        case KonstWillenserklaerung.aendernWeisungAnKIAV:

        case KonstWillenserklaerung.aendernWeisungAnSRV:
        case KonstWillenserklaerung.vollmachtUndWeisungAnSRV:

        case KonstWillenserklaerung.dauervollmachtAnKIAV:

        case KonstWillenserklaerung.organisatorischMitWeisungInSammelkarte:
        case KonstWillenserklaerung.aendernWeisungOrganisatorischInSammelkarte:

        {

            tWillenserklaerungSession.setLastElementOfWeisungenSindListe(KonstWeisungenSind.DEDIZIERTE_VORHANDEN);

            eclDbM.getDbBundle().dbWeisungMeldung.leseZuWillenserklaerungIdent(pWillenserklaerungStatusM.getWillenserklaerungIdent(), true);
            EclWeisungMeldung lWeisungMeldung = eclDbM.getDbBundle().dbWeisungMeldung.weisungMeldungGefunden(0);

            tWillenserklaerungSession.setLastElementOfMeldungsIdentListe(lWeisungMeldung.meldungsIdent);
            tWillenserklaerungSession.setLastElementOfWillenserklaerungIdentListe(lWeisungMeldung.willenserklaerungIdent);
            tWillenserklaerungSession.setLastElementOfSammelIdentListe(lWeisungMeldung.sammelIdent);
            tWillenserklaerungSession.setLastElementOfWeisungsIdentListe(lWeisungMeldung.weisungIdent);
            switch (pWillenserklaerungStatusM.getWillenserklaerung()) {
            case KonstWillenserklaerung.aendernWeisungAnSRV:
            case KonstWillenserklaerung.vollmachtUndWeisungAnSRV: {
                lSkIst = KonstSkIst.srv;
                tWillenserklaerungSession.setAusgewaehlteAktion(KonstPortalAktion.SRV_STORNIEREN);
                break;
            }
            case KonstWillenserklaerung.briefwahl:
            case KonstWillenserklaerung.aendernBriefwahl: {
                lSkIst = KonstSkIst.briefwahl;
                tWillenserklaerungSession.setAusgewaehlteAktion(KonstPortalAktion.BRIEFWAHL_STORNIEREN);
                break;
            }
            case KonstWillenserklaerung.vollmachtUndWeisungAnKIAV:
            case KonstWillenserklaerung.aendernWeisungAnKIAV: {
                lSkIst = KonstSkIst.kiav;
                tWillenserklaerungSession.setAusgewaehlteAktion(KonstPortalAktion.KIAV_STORNIEREN);
                /*Nun noch KIAV-Bezeichnung einlesen*/
                EclMeldung lMeldung = new EclMeldung();
                lMeldung.meldungsIdent = lWeisungMeldung.sammelIdent;
                eclDbM.getDbBundle().dbMeldungen.leseZuMeldungsIdent(lMeldung);
                eclKIAVM.setMeldeIdent(eclDbM.getDbBundle().dbMeldungen.meldungenArray[0].meldungsIdent);
                eclKIAVM.setKurzText(eclDbM.getDbBundle().dbMeldungen.meldungenArray[0].kurzName);
                eclKIAVM.setGattung(eclDbM.getDbBundle().dbMeldungen.meldungenArray[0].liefereGattung());

                /*Für KIAV: übernehmen, inwieweit nur Vollmacht, Vollmacht gemäß Vorschlag etc.*/
                tWillenserklaerungSession.setLastElementOfWeisungenSindListe(pWillenserklaerungStatusM.getWeisungenSind());

                break;
            }
            case KonstWillenserklaerung.dauervollmachtAnKIAV: {
                lSkIst = KonstSkIst.kiav;
                tWillenserklaerungSession.setAusgewaehlteAktion(KonstPortalAktion.DAUERVOLLMACHT_STORNIEREN);
                /*Nun noch KIAV-Bezeichnung einlesen*/
                EclMeldung lMeldung = new EclMeldung();
                lMeldung.meldungsIdent = lWeisungMeldung.sammelIdent;
                eclDbM.getDbBundle().dbMeldungen.leseZuMeldungsIdent(lMeldung);
                eclKIAVM.setMeldeIdent(eclDbM.getDbBundle().dbMeldungen.meldungenArray[0].meldungsIdent);
                eclKIAVM.setKurzText(eclDbM.getDbBundle().dbMeldungen.meldungenArray[0].kurzName);
                eclKIAVM.setGattung(eclDbM.getDbBundle().dbMeldungen.meldungenArray[0].liefereGattung());

                /*Für KIAV: übernehmen, inwieweit nur Vollmacht, Vollmacht gemäß Vorschlag etc.*/
                tWillenserklaerungSession.setLastElementOfWeisungenSindListe(pWillenserklaerungStatusM.getWeisungenSind());

                break;
            }
            case KonstWillenserklaerung.organisatorischMitWeisungInSammelkarte:
            case KonstWillenserklaerung.aendernWeisungOrganisatorischInSammelkarte: {
                lSkIst = KonstSkIst.kiav;
                tWillenserklaerungSession.setAusgewaehlteAktion(KonstPortalAktion.ORGANISATORISCH_STORNIEREN);
                /*Nun noch KIAV-Bezeichnung einlesen*/
                EclMeldung lMeldung = new EclMeldung();
                lMeldung.meldungsIdent = lWeisungMeldung.sammelIdent;
                eclDbM.getDbBundle().dbMeldungen.leseZuMeldungsIdent(lMeldung);
                eclKIAVM.setMeldeIdent(eclDbM.getDbBundle().dbMeldungen.meldungenArray[0].meldungsIdent);
                eclKIAVM.setKurzText(eclDbM.getDbBundle().dbMeldungen.meldungenArray[0].kurzName);
                eclKIAVM.setGattung(eclDbM.getDbBundle().dbMeldungen.meldungenArray[0].liefereGattung());

                /*Für KIAV: übernehmen, inwieweit nur Vollmacht, Vollmacht gemäß Vorschlag etc.*/
                tWillenserklaerungSession.setLastElementOfWeisungenSindListe(pWillenserklaerungStatusM.getWeisungenSind());

                break;
            }
            default: {
            }
            }

            tWillenserklaerungSession.ermittleGattungenFuerZugeordneteMeldungFuerAusfuehrungListe();
            blMAbstimmung.leseWeisungsliste(tWillenserklaerungSession.getGattungVorhanden(), lSkIst, KonstWeisungserfassungSicht.portalWeisungserfassung);

            List<EclAbstimmungM> lAbstimmungenListe = eclAbstimmungenListeM.getAbstimmungenListeM();

            for (int i = 0; i < lAbstimmungenListe.size(); i++) {

                int posInWeisung = lAbstimmungenListe.get(i).getIdentWeisungssatz();
                if (!lAbstimmungenListe.get(i).isUeberschrift()) {
                    switch (lWeisungMeldung.abgabe[posInWeisung]) {
                    case 1:
                        lAbstimmungenListe.get(i).setGewaehlt("J");
                        break;
                    case 2:
                        lAbstimmungenListe.get(i).setGewaehlt("N");
                        break;
                    case 3:
                        lAbstimmungenListe.get(i).setGewaehlt("E");
                        break;
                    default:
                        lAbstimmungenListe.get(i).setGewaehlt("");
                        break;
                    }
                }
            }

            List<EclAbstimmungM> lGegenantraegeListe = eclAbstimmungenListeM.getGegenantraegeListeM();

            for (int i = 0; i < lGegenantraegeListe.size(); i++) {

                int posInWeisung = lGegenantraegeListe.get(i).getIdentWeisungssatz();

                if (!lGegenantraegeListe.get(i).isUeberschrift()) {
                    switch (lWeisungMeldung.abgabe[posInWeisung]) {
                    case 1:
                        lGegenantraegeListe.get(i).setGewaehlt("J");
                        break;
                    case 2:
                        lGegenantraegeListe.get(i).setGewaehlt("N");
                        break;
                    case 3:
                        lGegenantraegeListe.get(i).setGewaehlt("E");
                        break;
                    default:
                        lGegenantraegeListe.get(i).setGewaehlt("");
                        break;
                    }
                    if (lWeisungMeldung.abgabe[posInWeisung] == 12) {
                        lGegenantraegeListe.get(i).setMarkiert(true);
                    } else {
                        lGegenantraegeListe.get(i).setMarkiert(false);
                    }
                }
            }

            tSessionVerwaltung.setzeEnde(KonstPortalView.WEISUNG_STORNIEREN);
            return true;
        }

        case KonstWillenserklaerung.neueZutrittsIdentZuMeldung:
        case KonstWillenserklaerung.neueZutrittsIdentZuMeldung_VollmachtAnDritte: {

            /*Bereitstellen, ob Aktionärs- oder Gastkarte storniert wird*/
            if (pZugeordneteMeldungM.getKlasse() == KonstMeldung.MELDUNG_IST_AKTIONAER) {
                tWillenserklaerungSession.setAusgewaehlteAktion(KonstPortalAktion.EK_STORNIEREN);
                if (pWillenserklaerungStatusM.getWillenserklaerung() == KonstWillenserklaerung.neueZutrittsIdentZuMeldung_VollmachtAnDritte) {
                    tWillenserklaerungSession.setAusgewaehlteAktion(KonstPortalAktion.EK_MIT_VOLLMACHT_STORNIEREN);
                }
            } else {
                tWillenserklaerungSession.setAusgewaehlteAktion(KonstPortalAktion.GK_STORNIEREN);
            }

            //        			aDlgVariablen.setMeldungsKlasse(pZugeordneteMeldungM.getKlasse());   wird vermutlich nicht mehr gebraucht - abwarten, ob noch in Storno-Ausführen benötigt

            /*Bereitstellen der Willenserklärungsdaten für Oberfläche*/
            /*Benötigt werden: 
             * textListe
             */

            tEintrittskarteStornierenSession.setTextListe(pWillenserklaerungStatusM.textListe);
            //        			eclWillenserklaerungStatusM.copyFromM(pWillenserklaerungStatusM); war früher so

            //        			/*Bereitstellen der Willenserklärungsdaten für Oberfläche*/
            //        			eclZugeordneteMeldungM.copyFromMOhneStorno(pZugeordneteMeldungM); wird vermutlich nicht mehr gebraucht - abwarten, ob noch in Storno-Ausführen benötigt

            tSessionVerwaltung.setzeEnde(KonstPortalView.EINTRITTSKARTE_STORNIEREN);
            return true;
        }
        case KonstWillenserklaerung.vollmachtAnDritte: {

            /*War hier ursprünglich mal 25 - zusätzliche Willenserklärung. offensichtlicher Quatsch. Prüfen im Rahmen
             * der weiteren Implementierung
             */
            tWillenserklaerungSession.setAusgewaehlteAktion(KonstPortalAktion.VOLLMACHT_DRITTE_STORNIEREN);

            /*Bei endgültier Implementierung klären, was noch erforderlich ist*/
            //        			/*Bereitstellen der Willenserklärungsdaten für Oberfläche*/
            //        			eclWillenserklaerungStatusM.copyFromM(pWillenserklaerungStatusM);
            //        
            //        			/*Bereitstellen der Willenserklärungsdaten für Oberfläche*/
            //        			eclZugeordneteMeldungM.copyFromMOhneStorno(pZugeordneteMeldungM);

            tSessionVerwaltung.setzeEnde(KonstPortalView.VOLLMACHT_DRITTE_STORNIEREN);
            return true;
        }
        default: {
        }
        }

        CaBug.drucke("001");
        return false;
    }

    /**Vorbereitung Ändern Weisungen (SRV, Briefwahl, KIAV) (d.h. Aufbereiten aller Variablen für den
     * Änderungsdialog) - einer Willenserklärung, die in EclZugeordneteMeldungNeu und EclWillenserklaerungStatusNeu
     * enthalten ist.
     * 
     * In der Funktion wird blMAbstimmung.leseWeisungsliste aufgerufen (insofern: Abstimmungen anschließend
     * eingelesen).
     * 
     * Sonstige Eingabeparameter:
     * 
     * Nach der Funktion ist gefüllt:
     *      tWillenserklaerungSession.ausgewaehlteAktion
     *      
     * 		tWillenserklaerungSession.meldungsIdentListe
     * 		tWillenserklaerungSession.willenserklaerungIdentListe
     * 		tWillenserklaerungSession.sammelIdentListe
     * 		tWillenserklaerungSession.WeisungsIdentListe
     * 
     * 		EclAbstimmungenListeM	Ist gefüllt mit Abstimmungen, und auch mit abgegebenen Weisungen
     * 			(in .gewaehlt)
     * 		EclKIAVM
     * 
     * DbBundle wird innerhalb der Funktion gehandhabt.
     */
    public void doAendern(EclZugeordneteMeldungNeu pZugeordneteMeldung, EclWillenserklaerungStatusNeu pWillenserklaerungStatus) {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.AUSWAHL)) {
            return;
        }

        /*Öffnen*/
        eclDbM.openAll();

        /*Sicherheitshalber einlesen, damit nicht schon beim Start "beendet" wird*/
        tWillenserklaerungSession.ermittleGattungenFuerBesitzAREintragListe();
        blMAbstimmung.leseWeisungsliste(tWillenserklaerungSession.getGattungVorhanden(), KonstSkIst.srv, KonstWeisungserfassungSicht.portalWeisungserfassung);

        if (!tPruefeStartNachOpen.pruefeNachOpenPortalWillenserklaerung(pWillenserklaerungStatus.willenserklaerung, 2)) {
            eclDbM.closeAll();
            return;
        }

        boolean brc = aendernAusfuehren(pZugeordneteMeldung, pWillenserklaerungStatus, KonstWeisungserfassungSicht.portalWeisungserfassung);

        if (brc == false) {
            eclDbM.closeAllAbbruch();
            tSessionVerwaltung.setzeEnde();
            return;
        }

        eclDbM.closeAll();
        return;
    }

    public boolean aendernAusfuehren(EclZugeordneteMeldungNeu pZugeordneteMeldungM, EclWillenserklaerungStatusNeu pWillenserklaerungStatusM, int pSicht) {
        int i;
        int lSkIst = 0;

        tWillenserklaerungSession.clear();

        tWillenserklaerungSession.initZugeordneteMeldungFuerAusfuehrungListe();
        tWillenserklaerungSession.addZugeordneteMeldungFuerAusfuehrungListe(pZugeordneteMeldungM, pWillenserklaerungStatusM);

        switch (pWillenserklaerungStatusM.getWillenserklaerung()) {

        case KonstWillenserklaerung.briefwahl:
        case KonstWillenserklaerung.aendernBriefwahl:

        case KonstWillenserklaerung.vollmachtUndWeisungAnKIAV:
        case KonstWillenserklaerung.aendernWeisungAnKIAV:

        case KonstWillenserklaerung.vollmachtUndWeisungAnSRV:
        case KonstWillenserklaerung.aendernWeisungAnSRV:

        case KonstWillenserklaerung.organisatorischMitWeisungInSammelkarte:
        case KonstWillenserklaerung.aendernWeisungOrganisatorischInSammelkarte:

        case KonstWillenserklaerung.dauervollmachtAnKIAV:
        case KonstWillenserklaerung.aendernWeisungDauervollmachtAnKIAV:

        {

            /*Allgemeiner Teil - gleich für Briefwahl, SRV, Weisung*/

            eclDbM.getDbBundle().dbWeisungMeldung.leseZuWillenserklaerungIdent(pWillenserklaerungStatusM.getWillenserklaerungIdent(), true);
            EclWeisungMeldung lWeisungMeldung = eclDbM.getDbBundle().dbWeisungMeldung.weisungMeldungGefunden(0);
            EclWeisungMeldungRaw lWeisungMeldungRaw = eclDbM.getDbBundle().dbWeisungMeldung.weisungMeldungRawGefunden(0);

            tWillenserklaerungSession.setLastElementOfMeldungsIdentListe(lWeisungMeldung.meldungsIdent);
            tWillenserklaerungSession.setLastElementOfWillenserklaerungIdentListe(lWeisungMeldung.willenserklaerungIdent);
            tWillenserklaerungSession.setLastElementOfSammelIdentListe(lWeisungMeldung.sammelIdent);
            tWillenserklaerungSession.setLastElementOfWeisungsIdentListe(lWeisungMeldung.weisungIdent);

            switch (pWillenserklaerungStatusM.getWillenserklaerung()) {
            case KonstWillenserklaerung.aendernWeisungAnSRV:
            case KonstWillenserklaerung.vollmachtUndWeisungAnSRV: {
                lSkIst = KonstSkIst.srv;
                tWillenserklaerungSession.setAusgewaehlteAktion(KonstPortalAktion.SRV_AENDERN);
                break;
            }
            case KonstWillenserklaerung.briefwahl:
            case KonstWillenserklaerung.aendernBriefwahl: {
                lSkIst = KonstSkIst.briefwahl;
                tWillenserklaerungSession.setAusgewaehlteAktion(KonstPortalAktion.BRIEFWAHL_AENDERN);
                break;
            }
            case KonstWillenserklaerung.vollmachtUndWeisungAnKIAV:
            case KonstWillenserklaerung.aendernWeisungAnKIAV:
            case KonstWillenserklaerung.organisatorischMitWeisungInSammelkarte:
            case KonstWillenserklaerung.aendernWeisungOrganisatorischInSammelkarte:
            case KonstWillenserklaerung.dauervollmachtAnKIAV:
            case KonstWillenserklaerung.aendernWeisungDauervollmachtAnKIAV: {
                lSkIst = KonstSkIst.kiav;
                switch (pWillenserklaerungStatusM.getWillenserklaerung()) {
                case KonstWillenserklaerung.vollmachtUndWeisungAnKIAV:
                case KonstWillenserklaerung.aendernWeisungAnKIAV:
                    tWillenserklaerungSession.setAusgewaehlteAktion(KonstPortalAktion.KIAV_WEISUNG_AENDERN);
                    break;
                case KonstWillenserklaerung.organisatorischMitWeisungInSammelkarte:
                case KonstWillenserklaerung.aendernWeisungOrganisatorischInSammelkarte:
                    tWillenserklaerungSession.setAusgewaehlteAktion(KonstPortalAktion.ORGANISATORISCH_AENDERN);
                    break;
                case KonstWillenserklaerung.dauervollmachtAnKIAV:
                case KonstWillenserklaerung.aendernWeisungDauervollmachtAnKIAV:
                    tWillenserklaerungSession.setAusgewaehlteAktion(KonstPortalAktion.DAUERVOLLMACHT_AENDERN);
                    break;
                }

                /*Nun noch KIAV-Bezeichnung einlesen*/
                EclMeldung lMeldung = new EclMeldung();
                lMeldung.meldungsIdent = lWeisungMeldung.sammelIdent;
                eclDbM.getDbBundle().dbMeldungen.leseZuMeldungsIdent(lMeldung);
                eclKIAVM.setMeldeIdent(eclDbM.getDbBundle().dbMeldungen.meldungenArray[0].meldungsIdent);
                eclKIAVM.setKurzText(eclDbM.getDbBundle().dbMeldungen.meldungenArray[0].kurzName);
                eclKIAVM.setGattung(eclDbM.getDbBundle().dbMeldungen.meldungenArray[0].liefereGattung());
                break;
            }
            default: {
            }
            }

            eclAbstimmungenListeM.setWeisungMeldung(lWeisungMeldung);
            eclAbstimmungenListeM.setWeisungMeldungRaw(lWeisungMeldungRaw);

            tWillenserklaerungSession.ermittleGattungenFuerZugeordneteMeldungFuerAusfuehrungListe();
            blMAbstimmung.leseWeisungsliste(tWillenserklaerungSession.getGattungVorhanden(), lSkIst, KonstWeisungserfassungSicht.portalWeisungserfassung);
            
            List<EclAbstimmungM> lAbstimmungenListe = eclAbstimmungenListeM.getAbstimmungenListeM();

            if (lAbstimmungenListe.size()==0 && eclAbstimmungenListeM.getGegenantraegeListeM().size()==0) {
                
                if (
                        pWillenserklaerungStatusM.getWillenserklaerung()==KonstWillenserklaerung.briefwahl
                        ||
                        pWillenserklaerungStatusM.getWillenserklaerung()==KonstWillenserklaerung.aendernBriefwahl
                        ) {
                    tFehlerViewSession.setFehlerArt(KonstPortalFehlerView.BRIEFWAHL_NICHT_MOEGLICH_KEINE_ABSTIMMUNGEN__ABBRUCH);
                }
                else {
                    tFehlerViewSession.setFehlerArt(KonstPortalFehlerView.SRV_NICHT_MOEGLICH_KEINE_ABSTIMMUNGEN__ABBRUCH);
                }
                tFehlerViewSession.setNextView(tPruefeStartNachOpen.liefereAuswahlView());
                tSessionVerwaltung.setzeEnde(KonstPortalView.FEHLER_VIEW);
                return false;
               
            }
            
            
            for (i = 0; i < lAbstimmungenListe.size(); i++) {

                int posInWeisung = lAbstimmungenListe.get(i).getIdentWeisungssatz();
                if (!lAbstimmungenListe.get(i).isUeberschrift()) {
                    switch (lWeisungMeldung.abgabe[posInWeisung]) {
                    case 1:
                        lAbstimmungenListe.get(i).setGewaehlt("J");
                        break;
                    case 2:
                        lAbstimmungenListe.get(i).setGewaehlt("N");
                        break;
                    case 3:
                        lAbstimmungenListe.get(i).setGewaehlt("E");
                        break;
                    case 4:
                        lAbstimmungenListe.get(i).setGewaehlt("U");
                        break;
                    default:
                        lAbstimmungenListe.get(i).setGewaehlt("");
                        break;
                    }
                }
            }

            List<EclAbstimmungM> lGegenantraegeListe = eclAbstimmungenListeM.getGegenantraegeListeM();

            for (i = 0; i < lGegenantraegeListe.size(); i++) {

                int posInWeisung = lGegenantraegeListe.get(i).getIdentWeisungssatz();

                if (!lGegenantraegeListe.get(i).isUeberschrift()) {
                    switch (lWeisungMeldung.abgabe[posInWeisung]) {
                    case 1:
                        lGegenantraegeListe.get(i).setGewaehlt("J");
                        break;
                    case 2:
                        lGegenantraegeListe.get(i).setGewaehlt("N");
                        break;
                    case 3:
                        lGegenantraegeListe.get(i).setGewaehlt("E");
                        break;
                    case 4:
                        lGegenantraegeListe.get(i).setGewaehlt("U");
                        break;
                    default:
                        lGegenantraegeListe.get(i).setGewaehlt("");
                        break;
                    }
                    if (lWeisungMeldung.abgabe[posInWeisung] == 12) {
                        lGegenantraegeListe.get(i).setMarkiert(true);
                    } else {
                        lGegenantraegeListe.get(i).setMarkiert(false);
                    }
                }

            }

            blMAbstimmungsvorschlag.leseAbstimmungsvorschlagGesellschaft(eclDbM.getDbBundle());

            tSessionVerwaltung.setzeEnde(KonstPortalView.WEISUNG_AENDERN);
            return true;
        }

        default: {
        }
        }
        return false;
    }

    /**Funktion, die aufgerufen wird, wenn aus der Status-Seite heraus Details angezeigt werden.
     * 
     * Diese Funktion ermittelt, ob die anzuzeigenden Details eine Eintrittskarte oder eine Gastkarte ist.
     * (Details für andere Willenserklärungen sind derzeit nicht möglich - da gibts den Button nicht!)
     * 
     * Übergabeparameter:
     *      EclZugeordneteMeldungM = Meldung, zu der die anzuzeigende Willenserklärung gehört
     *      EclWillenserklaerungStatusM = Willenserklärung, die angezeigt werden soll.
     * 
     * Bei Ausgabe gefüllt:  
     *      ADlgVariablen.ausgewaehlteAktion
     *      siehe Funktion ekDateils
     *      Bei Gastkarten zusätzlich:
     *          ADlgVariablen.IdentMasterGast
     *          EclGastM (einschließlich EclGastM.meldungenMeldungenListe)
     *          ADlgVariablen.gruppenausstellung
     * 
     * Bei Ausgabe immer gefüllt:
     * 
     * DbBundle wird innerhalb der Funktion gehandled.
     */
    public void doDetails(EclZugeordneteMeldungNeu pZugeordneteMeldungM, EclWillenserklaerungStatusNeu pWillenserklaerungStatusM) {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.AUSWAHL)) {
            return;
        }

        eclDbM.openAll();

        if (pZugeordneteMeldungM.getArtBeziehung() == KonstZugeordneteMeldungArtBeziehung.EIGENE_AKTIEN_AUS_AKTIENREGISTER
                || pZugeordneteMeldungM.getArtBeziehung() == KonstZugeordneteMeldungArtBeziehung.ALS_BEVOLLMAECHTIGTER_ERHALTENE_MELDUNG 
                || pZugeordneteMeldungM.getArtBeziehung() == KonstZugeordneteMeldungArtBeziehung.INSTI_AKTIENREGISTER
                || pZugeordneteMeldungM.getArtBeziehung() == KonstZugeordneteMeldungArtBeziehung.INSTI_MELDUNG) {
            /****Detailanzeige für auf den angemeldeten ausgestellte Eintrittskarten*****/
            tEintrittskarteQuittungUDetail.initDetailEK(eclDbM.getDbBundle(), pZugeordneteMeldungM, pWillenserklaerungStatusM);
            eclDbM.closeAll();
            tWillenserklaerungSession.setAusgewaehlteAktion(KonstPortalAktion.EK_DETAIL_ANZEIGEN);
            tSessionVerwaltung.setzeEnde(KonstPortalView.EINTRITTSKARTE_DETAIL);
            return;
        }

        if (pZugeordneteMeldungM.getArtBeziehung() == 2) {/****Detailanzeige für Gastkarte, die auf die eingeloggte Person ausgestellt ist****/

            tEintrittskarteQuittungUDetail.initDetailGK(eclDbM.getDbBundle(), pZugeordneteMeldungM, pWillenserklaerungStatusM);
            eclDbM.closeAll();
            tWillenserklaerungSession.setAusgewaehlteAktion(KonstPortalAktion.GK_DETAIL_ANZEIGEN);
            tSessionVerwaltung.setzeEnde(KonstPortalView.GASTKARTE_DETAIL);
        }

        eclDbM.closeAll();
        tSessionVerwaltung.setzeEnde();
        return;
    }

    //    public void doWeitereWillenserklaerung(EclZugeordneteMeldungNeu pZugeordneteMeldung) {
    // Nicht mehr benötigt wahrscheinlich ...
    //        /*Prüfen, ob von richtiger Seite aus aufgerufen*/
    //        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.AUSWAHL)) {
    //            return;
    //        }
    //
    //        /*Öffnen*/
    //        eclDbM.openAll();
    //
    //        if (!tPruefeStartNachOpen.pruefeStartNachOpen()) {
    //            tSessionVerwaltung.setzeEnde();
    //            eclDbM.closeAll();
    //            return;
    //        }
    //
    //        eclDbM.closeAll();
    //
    //        tWillenserklaerungSession.clear();
    //
    //        /*Bereitstellen der Willenserklärungsdaten für Oberfläche*/
    //        tWillenserklaerungSession.setZugeordneteMeldungFuerAusfuehrung(pZugeordneteMeldung);
    //
    //        tSessionVerwaltung.setzeEnde(KonstPortalView.neueWillenserklaerung);
    //        return;
    //    }

    /*++++++++++++++++++Online-Teilnahme+++++++++++++++++++++*/
    public void doTeilnahmeZugang() {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.AUSWAHL)) {
            return;
        }
 
        try {

        eclDbM.openAll();
        boolean brc = tPruefeStartNachOpen.pruefeNachOpenPortalFunktion(KonstPortalFunktionen.onlineteilnahme, true);
        if (brc == false) {
            eclDbM.closeAll();
            return;
        }

        int rc=tPraesenzZugangAbgang.initVirtuelleHV();
        
        if (rc>=0) {
            rc=tPraesenzZugangAbgang.zugangBuchenVirtuelleHV();
        }
        
        startAuswahl(true);

        eclDbM.closeAll();

        if (rc < 0) {
            switch (rc) {
            case CaFehler.afFunktionNichtAuswaehlbar:
                tSession.trageFehlerEin(rc);
                tSessionVerwaltung.setzeEnde(KonstPortalView.AUSWAHL);
                return;
            case CaFehler.afFunktionDerzeitNichtAktiv:
                tSession.trageFehlerEin(CaFehler.afVersammlungsraumNichtGeoeffnetBeiZugang);
                tSessionVerwaltung.setzeEnde(KonstPortalView.AUSWAHL);
                return;
            default:
                tSession.trageFehlerEin(rc);
                tSessionVerwaltung.setzeEnde(KonstPortalView.fehlerSysLogout);
                return;
            }
        }
        else {
            /*TODO VidKonf  Videostream starten*/
        }
        } catch (Exception e) {
            CaBug.drucke("Exception");
            System.out.println(e.getMessage());
            e.printStackTrace();
            eclDbM.closeAll();
        }

        tSessionVerwaltung.setzeEnde(KonstPortalView.AUSWAHL);
        return;
    }

    public void doTeilnahmeWeitereZugang() {
        return;
    }

    public void doTeilnahmeAbgang() {
        /*Prüfen, ob von richtiger Seite aus aufgerufen*/
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.AUSWAHL)) {
            return;
        }
        try {

        
        eclDbM.openAll();
        int rc = tPraesenzZugangAbgang.initVirtuelleHV();
        if (rc>=0) {
            
            rc=tPraesenzZugangAbgang.abgangBuchenVirtuelleHV();
        }
        
        startAuswahl(true);
        
        eclDbM.closeAll();
        
         
        if (rc < 0) {
            switch (rc) {
            case CaFehler.afFunktionNichtAuswaehlbar:
                tSession.trageFehlerEin(rc);
                tSessionVerwaltung.setzeEnde(KonstPortalView.AUSWAHL);
                return;
            case CaFehler.afFunktionDerzeitNichtAktiv:
                tSession.trageFehlerEin(CaFehler.afVersammlungsraumNichtGeoeffnetBeiAbgang);
                tSessionVerwaltung.setzeEnde(KonstPortalView.AUSWAHL);
                return;
            default:
                tSession.trageFehlerEin(rc);
                tSessionVerwaltung.setzeEnde(KonstPortalView.fehlerSysLogout);
                return;
            }
        }
        else {
            /*TODO VidKonf  Videostream beenden*/
        }
        } catch (Exception e) {
            CaBug.drucke("Exception");
            System.out.println(e.getMessage());
            e.printStackTrace();
            eclDbM.closeAll();
        }
       
        tSessionVerwaltung.setzeEnde(KonstPortalView.AUSWAHL /*KonstPortalView.PRAESENZ_ABGANG_BESTAETIGUNG*/);
        return;
    }

    public void doStimmabgabe() {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.AUSWAHL)) {
            return;
        }

        eclDbM.openAll();

        if (!tPruefeStartNachOpen.pruefeStartNachOpen()) {
            tSessionVerwaltung.setzeEnde();
            eclDbM.closeAll();
            return;
        }

        int rc = tStimmabgabe.startStimmabgabe();

        eclDbM.closeAll();
        if (rc < 0) {
            switch (rc) {
            case CaFehler.afNichtStimmberechtigt:
            case CaFehler.afFunktionNichtAuswaehlbar:
            case CaFehler.afDerzeitKeineAbstimmungEroeffnet:
                tSession.trageFehlerEin(rc);
                tSessionVerwaltung.setzeEnde(KonstPortalView.AUSWAHL);
                return;
            default:
                tSession.trageFehlerEin(rc);
                tSessionVerwaltung.setzeEnde(KonstPortalView.fehlerSysLogout);
                return;
            }
        }

        tSessionVerwaltung.setzeEnde(KonstPortalView.STIMMABGABE);
        return;

    }

    public void doZuordnungAufheben(EclBesitzJeKennung pBesitzJeKennung) {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.AUSWAHL)) {
            return;
        }
        
        eclDbM.openAll();

        if (!tPruefeStartNachOpen.pruefeStartNachOpen()) {
            tSessionVerwaltung.setzeEnde();
            eclDbM.closeAll();
            return;
        }

        eclDbM.closeAll();

        tZuordnung.initZuordnungAufheben(pBesitzJeKennung);
        
        tSessionVerwaltung.setzeEnde(KonstPortalView.ZUORDNUNG_AUFHEBEN);
        return;
    }
    
    public void doZuordnungEinrichten() {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.AUSWAHL)) {
            return;
        }
        
        eclDbM.openAll();

        if (!tPruefeStartNachOpen.pruefeStartNachOpen()) {
            tSessionVerwaltung.setzeEnde();
            eclDbM.closeAll();
            return;
        }

        eclDbM.closeAll();

        tZuordnung.initZuordnungEinrichten();
        
        tSessionVerwaltung.setzeEnde(KonstPortalView.ZUORDNUNG_EINRICHTEN);
        return;
    }

    /*++++++++++++++++++++++Videostream++++++++++++++++++++++++++*/
    public void doStream() {
        funktionAufrufen(KonstPortalView.AUSWAHL, KonstPortalView.STREAM_START);
    }

    /*++++++++++++++++++++++Unterlagen++++++++++++++++++++++++++*/
    public void doUnterlagen() {
        funktionAufrufen(KonstPortalView.AUSWAHL, KonstPortalView.UNTERLAGEN);
    }

    /*++++++++++++++++++++++Unterlagen++++++++++++++++++++++++++*/
    public void doBotschaftenAnzeigen() {
        funktionAufrufen(KonstPortalView.AUSWAHL, KonstPortalView.BOTSCHAFTEN_ANZEIGEN);
    }

    /*++++++++++++++++++++++Botschaften einreichen++++++++++++++++++++++++++*/
    public void doBotschaftenEinreichen() {
        funktionAufrufen(KonstPortalView.AUSWAHL, KonstPortalView.BOTSCHAFTEN_EINREICHEN);
    }

    /*++++++++++++++++++++++Fragen++++++++++++++++++++++++++*/
    public void doFragen() {
        funktionAufrufen(KonstPortalView.AUSWAHL, KonstPortalView.FRAGEN);
    }

    /*++++++++++++++++++++++Fragen++++++++++++++++++++++++++*/
    public void doRueckfragen() {
        funktionAufrufen(KonstPortalView.AUSWAHL, KonstPortalView.RUECKFRAGEN);
    }

    /*++++++++++++++++++++++Wortmeldungen++++++++++++++++++++++++++*/
    public void doWortmeldungen() {
        funktionAufrufen(KonstPortalView.AUSWAHL, KonstPortalView.WORTMELDUNGEN);
    }

    /*++++++++++++++++++++++Widersprüche++++++++++++++++++++++++++*/
    public void doWidersprueche() {
        //        funktionAufrufen(KonstPortalView.AUSWAHL, KonstPortalView.FEHLER_DIALOG);
        funktionAufrufen(KonstPortalView.AUSWAHL, KonstPortalView.WIDERSPRUECHE);
    }

    /*++++++++++++++++++++++Anträge+++++++++++++++++++++++++*/
    public void doAntraege() {
        funktionAufrufen(KonstPortalView.AUSWAHL, KonstPortalView.ANTRAEGE);
    }

    /*++++++++++++++++++++++Sonstige Mitteilungen++++++++++++++++++++++++++*/
    public void doSonstigeMitteilungen() {
        funktionAufrufen(KonstPortalView.AUSWAHL, KonstPortalView.SONSTIGEMITTEILUNGEN);
    }

    /*++++++++++++++++++++Monitoring++++++++++++++++++++*/
    public void doMonitoring() {
        funktionAufrufen(KonstPortalView.AUSWAHL, KonstPortalView.MONITORING);
    }

    
    /*+++++++++++++++++Gäste++++++++++++++++++++++++++++++++*/
    public void doGastkarten() {
        funktionAufrufen(KonstPortalView.AUSWAHL, KonstPortalView.GASTKARTE_UEBERSICHT);
    }
    
    
    /*++++++++++++++++++++Einstellungen++++++++++++++++++++*/
    public void doEinstellungen() {
        funktionAufrufen(KonstPortalView.AUSWAHL, KonstPortalView.EINSTELLUNGEN);
    }

    public void doTeilnehmerverz() {
        funktionAufrufen(KonstPortalView.AUSWAHL, KonstPortalView.TEILNEHMERVERZEICHNIS);
    }

    public void doAbstimmungserg() {
        funktionAufrufen(KonstPortalView.AUSWAHL, KonstPortalView.ABSTIMMUNGSERGEBNISSE);
    }

    /**
     * pAufrufendeSeite: z.B. KonstPortalView.AUSWAHL
     * pFunktionSeite: Seite, die als nächstes aufgerufen werden soll, spezifiziert gleichzeitig
     *      die zu startende Funktion
     */
    public void funktionAufrufen(int pAufrufendeSeite, int pFunktionSeite) {
        if (!tSessionVerwaltung.pruefeStart(pAufrufendeSeite)) {
            return;
        }

        int lPortalFunktion = KonstPortalFunktionen.portalFunktionZuView(pFunktionSeite);
        eclDbM.openAll();
        boolean brc = tPruefeStartNachOpen.pruefeNachOpenPortalFunktion(lPortalFunktion, true);
        if (brc == false) {
            eclDbM.closeAll();
            return;
        }

        int rc = 0;
        switch (pFunktionSeite) {
        case KonstPortalView.EINSTELLUNGEN:
            rc = tEinstellungen.startRegistrierungAusAuswahl();
            break;
        case KonstPortalView.GASTKARTE_UEBERSICHT:
            rc = tGaeste.init(true);
            break;
        case KonstPortalView.MONITORING: //*
            rc = tMonitoring.startMonitoring();
            break;
        case KonstPortalView.STREAM_START:
            break;
        case KonstPortalView.UNTERLAGEN:
            rc = tUnterlagen.init(KonstPortalUnterlagen.ANZEIGEN_UNTERLAGEN, 0);
            break;
        case KonstPortalView.BOTSCHAFTEN_ANZEIGEN:
            rc = tUnterlagen.init(KonstPortalUnterlagen.ANZEIGEN_BOTSCHAFTEN, 0);
            break;
        case KonstPortalView.BOTSCHAFTEN_EINREICHEN:
            rc = tMitteilung.initAusAuswahl(KonstPortalFunktionen.botschaftenEinreichen);
            break;
        case KonstPortalView.FRAGEN:
            rc = tMitteilung.initAusAuswahl(KonstPortalFunktionen.fragen);
            break;
        case KonstPortalView.RUECKFRAGEN:
            rc = tMitteilung.initAusAuswahl(KonstPortalFunktionen.rueckfragen);
            break;
        case KonstPortalView.WORTMELDUNGEN:
            rc = tMitteilung.initAusAuswahl(KonstPortalFunktionen.wortmeldungen);
            break;
        case KonstPortalView.WIDERSPRUECHE:
            rc = tMitteilung.initAusAuswahl(KonstPortalFunktionen.widersprueche);
            break;
        case KonstPortalView.ANTRAEGE:
            rc = tMitteilung.initAusAuswahl(KonstPortalFunktionen.antraege);
            break;
        case KonstPortalView.SONSTIGEMITTEILUNGEN:
            rc = tMitteilung.initAusAuswahl(KonstPortalFunktionen.sonstigeMitteilungen);
            break;

        case KonstPortalView.TEILNEHMERVERZEICHNIS:
            tTeilnehmerverz.init(false);
            break;
        case KonstPortalView.ABSTIMMUNGSERGEBNISSE:
            tAbstimmungserg.init(false);
            break;
        }

        eclDbM.closeAll();
        if (rc < 0) {
            switch (rc) {
            case CaFehler.afFunktionNichtAuswaehlbar:
                tSession.trageFehlerEin(rc);
                tSessionVerwaltung.setzeEnde(pAufrufendeSeite);
                return;
            case CaFehler.afFunktionDerzeitNichtAktiv:
                /*Hinweis: nicht alle obigen init-Funktionen liefern diese Fehlersituation. Wenn
                 * diese Fehlersituation nicht geliefert, dann wird auch bei "nicht-aktiv" die
                 * Funktionsseite aufgerufen. Dies ist z.B. bei Fragen erforderlich, wo auf
                 * der Fnktionsseite bereits gestellte Fragen angezeigt werden - und dies auch
                 * möglich sein soll, wenn eben gerade keine Fragen mehr gestellt werden können.
                 */
                tSession.trageFehlerEin(rc);
                tSessionVerwaltung.setzeEnde(pAufrufendeSeite);
                return;
            default:
                tSession.trageFehlerEin(rc);
                tSessionVerwaltung.setzeEnde(KonstPortalView.fehlerSysLogout);
                return;
            }
        }

        tSession.setRueckkehrZuMenue(pAufrufendeSeite);
        tSessionVerwaltung.setzeEnde(pFunktionSeite);
        return;

    }

}
