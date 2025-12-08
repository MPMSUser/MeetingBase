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

import java.util.List;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaDatumZeit;
import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComAllg.CaString;
import de.meetingapps.meetingportal.meetComBa.BaMailM;
import de.meetingapps.meetingportal.meetComBl.BlEKFreiwilligesAnmelden;
import de.meetingapps.meetingportal.meetComBl.BlGastkarte;
import de.meetingapps.meetingportal.meetComBl.BlVeranstaltungen;
import de.meetingapps.meetingportal.meetComBl.BlVorlaeufigeVollmacht;
import de.meetingapps.meetingportal.meetComBl.BlWillenserklaerung;
import de.meetingapps.meetingportal.meetComBlManaged.BlMAbstimmung;
import de.meetingapps.meetingportal.meetComBlManaged.BlMAbstimmungsvorschlag;
import de.meetingapps.meetingportal.meetComEclM.EclBesitzGesamtAuswahl1M;
import de.meetingapps.meetingportal.meetComEclM.EclBesitzGesamtM;
import de.meetingapps.meetingportal.meetComEclM.EclDbM;
import de.meetingapps.meetingportal.meetComEclM.EclLoginDatenM;
import de.meetingapps.meetingportal.meetComEclM.EclParamM;
import de.meetingapps.meetingportal.meetComEclM.EclPortalTexteM;
import de.meetingapps.meetingportal.meetComEclM.EclVeranstaltungenM;
import de.meetingapps.meetingportal.meetComEntities.EclMeldung;
import de.meetingapps.meetingportal.meetComEntities.EclVeranstaltungenQuittungElement;
import de.meetingapps.meetingportal.meetComEntities.EclVorlaeufigeVollmacht;
import de.meetingapps.meetingportal.meetComEntities.EclVorlaeufigeVollmachtEingabe;
import de.meetingapps.meetingportal.meetComEntities.EclWillenserklaerungStatusNeu;
import de.meetingapps.meetingportal.meetComEntities.EclZugeordneteMeldungNeu;
import de.meetingapps.meetingportal.meetComEntities.EclZutrittsIdent;
import de.meetingapps.meetingportal.meetComHVParam.ParamSpezial;
import de.meetingapps.meetingportal.meetComKonst.KonstPortalView;
import de.meetingapps.meetingportal.meetComKonst.KonstSkIst;
import de.meetingapps.meetingportal.meetComKonst.KonstWeisungserfassungSicht;
import de.meetingapps.meetingportal.meetComKonst.KonstWillenserklaerung;
import de.meetingapps.meetingportal.meetComKonst.KonstWillenserklaerungWeg;
import de.meetingapps.meetingportal.meetingCoreReport.RpBrowserAnzeigen;
import de.meetingapps.meetingportal.meetingCoreReport.RpDrucken;
import de.meetingapps.meetingportal.meetingCoreReport.RpVariablen;
import de.meetingapps.meetingportal.meetingportTFunktionen.TFunktionen;
import de.meetingapps.meetingportal.meetingportTFunktionen.TPortalFunktionen;
import de.meetingapps.meetingportal.meetingportTFunktionen.TPruefeStartNachOpen;
import de.meetingapps.meetingportal.meetingportTFunktionen.TSessionVerwaltung;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

/**Bean für iAuswahl1Generalversammlung und iAuswahl1GeneralversammlungBriefwahl*/
@RequestScoped
@Named
public class TGeneralversammlung {

    int logDrucken = 10;

    private @Inject EclDbM eclDbM;
    private @Inject EclLoginDatenM eclLoginDatenM;
    private @Inject EclParamM eclParamM;

    private @Inject TFunktionen tFunktionen;
    private @Inject TPortalFunktionen tPortalFunktionen;
    private @Inject EclBesitzGesamtM eclBesitzGesamtM;
    private @Inject TSessionVerwaltung tSessionVerwaltung;
    private @Inject TSession tSession;
    private @Inject TGeneralversammlungSession tGeneralversammlungSession;
    private @Inject EclBesitzGesamtAuswahl1M eclBesitzGesamtAuswahl1M;
    private @Inject TWillenserklaerungSession tWillenserklaerungSession;
    private @Inject BlMAbstimmung blMAbstimmung;
    private @Inject BlMAbstimmungsvorschlag blMAbstimmungsvorschlag;
    private @Inject TAuswahl tAuswahl;
    private @Inject TLanguage tLanguage;
    @Inject
    private EclPortalTexteM eclTextePortalM;
    @Inject
    private BaMailM baMailM;
    private @Inject TPruefeStartNachOpen tPruefeStartNachOpen;
    private @Inject EclVeranstaltungenM eclVeranstaltungenM;
    private @Inject EclPortalTexteM eclPortalTexteM;

    /**Initialisierung für Status-Anzeige Generalveranstaltungen
     * EclDbM wird in aufrufender Funktion gehandelt*/
    public void init() {

        tGeneralversammlungSession.clear();

        tPortalFunktionen.belegeAnzahlFragenGestellt(eclDbM.getDbBundle());
        tPortalFunktionen.belegeAngebotenAktivFuerAuswahl();
        tPortalFunktionen.belegeAktivFuerWillenserklaerungen();

        tFunktionen.leseStatusPortal(eclDbM.getDbBundle());

        if (eclParamM.getParam().paramPortal.freiwilligeAnmeldungMitVertretereingabe==3) {
            eclBesitzGesamtAuswahl1M.clearGesetzVertreter1();
            eclBesitzGesamtAuswahl1M.clearVertreter1();
           bereiteGesetzlVertretungenFuerAnzeigeAuf();
           bereiteBevollmaechtigtenFuerAnzeigeAuf();
           bereiteNachweiseFuerAnzeigeAuf();
        }
        

        
        
    }

    public void doAnAbmelden() {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.AUSWAHL1_GENERALVERSAMMLUNG)) {
            return;
        }
        
        tSessionVerwaltung.setzeEnde(KonstPortalView.GENERALVERSAMMLUNG_ANABMELDEN);
        return;
    }

    public void doEKAnzeigen() {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.AUSWAHL1_GENERALVERSAMMLUNG)) {
            return;
        }
         ausfuehrenEKAnzeigen(1);
        tSessionVerwaltung.setzeEnde(KonstPortalView.AUSWAHL1_GENERALVERSAMMLUNG);
   }
 
    
    public void doEK2Anzeigen() {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.AUSWAHL1_GENERALVERSAMMLUNG)) {
            return;
        }
         ausfuehrenEKAnzeigen(2);
        tSessionVerwaltung.setzeEnde(KonstPortalView.AUSWAHL1_GENERALVERSAMMLUNG);
   }
 
    public void doEK3Anzeigen() {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.AUSWAHL1_GENERALVERSAMMLUNG)) {
            return;
        }
         ausfuehrenEKAnzeigen(3);
        tSessionVerwaltung.setzeEnde(KonstPortalView.AUSWAHL1_GENERALVERSAMMLUNG);
   }
 

    public void doGastkarteVeranstaltungAnzeigen(EclVeranstaltungenQuittungElement pQuittungElement) {
        int[] ausgangsseiten= {KonstPortalView.AUSWAHL1_GENERALVERSAMMLUNG, KonstPortalView.GENERALVERSAMMLUNG_ANABMELDEN};
        if (!tSessionVerwaltung.pruefeStart(ausgangsseiten)) {
            return;
        }
        
        eclDbM.openAll();

        EclZutrittsIdent lZutrittsIdent=new EclZutrittsIdent();
        lZutrittsIdent.zutrittsIdent=pQuittungElement.wertParameter;
        lZutrittsIdent.zutrittsIdentNeben="00";
        eclDbM.getDbBundle().dbZutrittskarten.readGast(lZutrittsIdent, 1);
        int meldungsIdent=eclDbM.getDbBundle().dbZutrittskarten.ergebnisPosition(0).meldungsIdentGast;
        
        eclDbM.getDbBundle().dbMeldungen.leseZuIdent(meldungsIdent);
        int personNatJurIdent=eclDbM.getDbBundle().dbMeldungen.meldungenArray[0].personenNatJurIdent;

        eclDbM.getDbBundle().dbLoginDaten.read_personNatJurIdent(personNatJurIdent);
        
        BlGastkarte blGastkarte=new BlGastkarte(eclDbM.getDbBundle());
        blGastkarte.rcDruckenInPDF=true;
        blGastkarte.pVersandart=6;
        blGastkarte.berechtigungsWert=eclDbM.getDbBundle().dbLoginDaten.ergebnisPosition(0).berechtigungPortal;
        
        blGastkarte.rcZutrittsIdent=pQuittungElement.wertParameter;
        blGastkarte.pGast=eclDbM.getDbBundle().dbMeldungen.meldungenArray[0];
        blGastkarte.drucken(eclDbM.getDbBundle());
        blGastkarte.druckenEnde();
        eclDbM.closeAll();
        
        CaBug.druckeLog("rcNamePDF="+blGastkarte.rcNamePDF, logDrucken, 10);
        
        RpBrowserAnzeigen rpBrowserAnzeigen = new RpBrowserAnzeigen();
        rpBrowserAnzeigen.zeigen(eclParamM.getClGlobalVar().lwPfadAllgemein+"\\"+eclParamM.getParamServer().praefixPfadVerzeichnisse
                + "ausdrucke\\"+eclParamM.getClGlobalVar().getMandantPfad()+"\\"+blGastkarte.rcNamePDF+".pdf");
        tSessionVerwaltung.setzeEnde();
   }

    public void doAnmeldeformularAnzeigen() {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.GENERALVERSAMMLUNG_ANABMELDEN)) {
            return;
        }
         ausfuehrenEKAnzeigen(1);
        tSessionVerwaltung.setzeEnde(KonstPortalView.GENERALVERSAMMLUNG_ANABMELDEN);
   }

    public void ausfuehrenEKAnzeigen(int buttonNr) {
        eclDbM.openAll();

        BlEKFreiwilligesAnmelden blEKFreiwilligesAnmelden=new BlEKFreiwilligesAnmelden(eclDbM.getDbBundle()); 

        EclZugeordneteMeldungNeu lZugeordneteMeldungNeu=eclBesitzGesamtAuswahl1M.liefereMeldungEigenerBestand();
        EclMeldung lMeldung=lZugeordneteMeldungNeu.eclMeldung;

        blEKFreiwilligesAnmelden.ausfuehrenEKAnzeigenAllgemein(buttonNr, eclLoginDatenM.getAnmeldeKennungFuerAnzeige(), tLanguage.getLang(), lMeldung);
        eclDbM.closeAll();
        
        RpBrowserAnzeigen rpBrowserAnzeigen = new RpBrowserAnzeigen();
        rpBrowserAnzeigen.zeigen(blEKFreiwilligesAnmelden.rcDateiname);

    }
    
    
    /*Normales Formular zur Bevollmächtigung. Formulare 11 / 12*/
    public void doVollmachtsformular() {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.AUSWAHL1_GENERALVERSAMMLUNG)) {
            return;
        }
        eclDbM.openAll();
        erzeugePDF(eclBesitzGesamtAuswahl1M.getGruppenTextVollmachtsFormularFuerVertreterNr());
        eclDbM.closeAll();

        String dateiname = eclParamM.getClGlobalVar().lwPfadAllgemein + "\\"+eclParamM.getParamServer().praefixPfadVerzeichnisse
                + "ausdrucke\\"
                + eclParamM.getMandantPfad() + "\\vollmachtsformular"
                + Integer.toString(eclLoginDatenM.getEclLoginDaten().ident) + ".pdf";
        RpBrowserAnzeigen rpBrowserAnzeigen = new RpBrowserAnzeigen();
        rpBrowserAnzeigen.zeigen(dateiname);
        tSessionVerwaltung.setzeEnde(KonstPortalView.AUSWAHL1_GENERALVERSAMMLUNG);
        return;
    }

    @Deprecated
    public void doVollmachtGesetzlichformular() {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.AUSWAHL1_GENERALVERSAMMLUNG)) {
            return;
        }
        eclDbM.openAll();
        erzeugePDF(2);
        eclDbM.closeAll();

        String dateiname = eclParamM.getClGlobalVar().lwPfadAllgemein + "\\"+eclParamM.getParamServer().praefixPfadVerzeichnisse
                + "ausdrucke\\"
                + eclParamM.getMandantPfad() + "\\vollmachtsformular"
                + Integer.toString(eclLoginDatenM.getEclLoginDaten().ident) + ".pdf";
        RpBrowserAnzeigen rpBrowserAnzeigen = new RpBrowserAnzeigen();
        rpBrowserAnzeigen.zeigen(dateiname);
        tSessionVerwaltung.setzeEnde(KonstPortalView.AUSWAHL1_GENERALVERSAMMLUNG);
        return;
    }

    @Deprecated
    public void doVollmachtElternformular() {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.AUSWAHL1_GENERALVERSAMMLUNG)) {
            return;
        }
        eclDbM.openAll();
        erzeugePDF(3);
        eclDbM.closeAll();

        String dateiname = eclParamM.getClGlobalVar().lwPfadAllgemein + "\\"+eclParamM.getParamServer().praefixPfadVerzeichnisse
                + "ausdrucke\\"
                + eclParamM.getMandantPfad() + "\\vollmachtsformular"
                + Integer.toString(eclLoginDatenM.getEclLoginDaten().ident) + ".pdf";
        RpBrowserAnzeigen rpBrowserAnzeigen = new RpBrowserAnzeigen();
        rpBrowserAnzeigen.zeigen(dateiname);
        tSessionVerwaltung.setzeEnde(KonstPortalView.AUSWAHL1_GENERALVERSAMMLUNG);
        return;
    }

    /**Wird aufgerufen bei gesetzlicher Vertretung - Nachweis
     * Formularnummern 1 bis 6*/
    public void doVollmachtsformularNr() {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.AUSWAHL1_GENERALVERSAMMLUNG)) {
            return;
        }
        vollmachtsFormularNr();
        tSessionVerwaltung.setzeEnde(KonstPortalView.AUSWAHL1_GENERALVERSAMMLUNG);
        return;
    }

    public void doVollmachtsformularNrBriefwahl() {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.AUSWAHL1_GENERALVERSAMMLUNG_BRIEFWAHL)) {
            return;
        }
        vollmachtsFormularNr();
        tSessionVerwaltung.setzeEnde(KonstPortalView.AUSWAHL1_GENERALVERSAMMLUNG_BRIEFWAHL);
        return;
    }

    public void doVollmachtsformularNrWeisungQuittung() {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.WEISUNG_QUITTUNG)) {
            return;
        }
        vollmachtsFormularNr();
        tSessionVerwaltung.setzeEnde(KonstPortalView.WEISUNG_QUITTUNG);
        return;
    }


    private void vollmachtsFormularNr() {
        eclDbM.openAll();
        erzeugePDF(eclBesitzGesamtAuswahl1M.getGruppenTextVollmachtsFormularNr());
        eclDbM.closeAll();

        String dateiname = eclParamM.getClGlobalVar().lwPfadAllgemein + "\\"+eclParamM.getParamServer().praefixPfadVerzeichnisse
                + "ausdrucke\\"
                + eclParamM.getMandantPfad() + "\\vollmachtsformular"
                + Integer.toString(eclLoginDatenM.getEclLoginDaten().ident) + ".pdf";
        RpBrowserAnzeigen rpBrowserAnzeigen = new RpBrowserAnzeigen();
        rpBrowserAnzeigen.zeigen(dateiname);
     }
    
    private void erzeugePDF(int vollmachtOderGesetzlicherNachweis) {
        String formularNummer = "";
        if (vollmachtOderGesetzlicherNachweis == 1) {
            formularNummer = "01";
        }
        if (vollmachtOderGesetzlicherNachweis == 2) {
            formularNummer = "02";
        }
        if (vollmachtOderGesetzlicherNachweis == 3) {
            formularNummer = "03";
        }
        if (vollmachtOderGesetzlicherNachweis == 4) {
            formularNummer = "04";
        }
        if (vollmachtOderGesetzlicherNachweis == 5) {
            formularNummer = "05";
        }
        if (vollmachtOderGesetzlicherNachweis == 11) {
            formularNummer = "11";
        }
        if (vollmachtOderGesetzlicherNachweis == 12) {
            formularNummer = "12";
        }
        if (vollmachtOderGesetzlicherNachweis == 13) {
            formularNummer = "13";
        }
       
        RpDrucken rpDrucken = new RpDrucken();
        rpDrucken.initServer();
        rpDrucken.exportFormat = 8;
        rpDrucken.exportDatei = "vollmachtsformular" + Integer.toString(eclLoginDatenM.getEclLoginDaten().ident);
        rpDrucken.initFormular(eclDbM.getDbBundle());

        /*Variablen füllen - sowie Dokumentvorlage*/
        RpVariablen rpVariablen = new RpVariablen(eclDbM.getDbBundle());
        rpVariablen.vollmachtsFormular(formularNummer, rpDrucken);
        rpDrucken.startFormular();

        rpVariablen.fuelleVariable(rpDrucken, "MitgliedsNr", eclLoginDatenM.getAnmeldeKennungFuerAnzeige());
        rpVariablen.fuelleVariable(rpDrucken, "Titel", eclLoginDatenM.getTitel());
        rpVariablen.fuelleVariable(rpDrucken, "Name", eclLoginDatenM.getName());
        rpVariablen.fuelleVariable(rpDrucken, "Vorname", eclLoginDatenM.getVorname());

        String hString = eclLoginDatenM.getStrasse();
        if (!hString.isEmpty()) {
            hString = hString + ", ";
        }
        hString = hString + eclLoginDatenM.getPlzOrt();
        rpVariablen.fuelleVariable(rpDrucken, "Adresse", hString);

        //Start printing
        rpDrucken.druckenFormular();
        rpDrucken.endeFormular();
    }

    public void doVollmachtStornieren() {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.AUSWAHL1_GENERALVERSAMMLUNG)) {
            return;
        }

        tSessionVerwaltung.setzeEnde(KonstPortalView.GENERALVERSAMMLUNG_VOLLMACHT_STORNIEREN);
        return;
    }

    public void doBriefwahlErteilen() {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.AUSWAHL1_GENERALVERSAMMLUNG_BRIEFWAHL)) {
            return;
        }

        /*Öffnen*/
        eclDbM.openAll();

        if (!tPruefeStartNachOpen.pruefeStartNachOpen(KonstPortalView.AUSWAHL1)) {
            eclDbM.closeAll();
            return;
        }

        tWillenserklaerungSession.clear();
        tWillenserklaerungSession.setAusgewaehlteHauptAktion(2);
        tWillenserklaerungSession.setAusgewaehlteAktion(5);

        /*Bereitstellen der Willenserklärungsdaten für Oberfläche*/
        tWillenserklaerungSession.initZugeordneteMeldungFuerAusfuehrungListe();
        tWillenserklaerungSession.addZugeordneteMeldungFuerAusfuehrungListe(eclBesitzGesamtM.getBesitzJeKennungListe().get(0).eigenerAREintragListe.get(0).zugeordneteMeldungenListe
                .get(0), null);
        tWillenserklaerungSession.ermittleGattungenFuerZugeordneteMeldungFuerAusfuehrungListe();
        tWillenserklaerungSession.ermittleGattungFuerEinzigenBesitzZugeordneteMeldungFuerAusfuehrungList();
        
//        tWillenserklaerungSession.setZugeordneteMeldungFuerAusfuehrung(
//                eclBesitzGesamtM.getBesitzJeKennungListe().get(0).eigenerAREintragListe.get(0).zugeordneteMeldungenListe
//                        .get(0));

//        int gattungVorhanden[] = new int[6];
//        gattungVorhanden[tWillenserklaerungSession.getZugeordneteMeldungFuerAusfuehrung().gattung] = 1;

        CaBug.druckeLog("tWillenserklaerungSession.getGattungEinzigeVorhanden()="
                + tWillenserklaerungSession.getGattungEinzigeVorhanden(), logDrucken, 5);
//        blMAbstimmung.leseWeisungsliste(gattungVorhanden, KonstSkIst.briefwahl, KonstWeisungserfassungSicht.portalWeisungserfassung);
        blMAbstimmung.leseWeisungsliste(tWillenserklaerungSession.getGattungVorhanden(), KonstSkIst.briefwahl, KonstWeisungserfassungSicht.portalWeisungserfassung);
        blMAbstimmungsvorschlag.leseAbstimmungsvorschlagGesellschaft(eclDbM.getDbBundle());

        eclDbM.closeAll();
        tWillenserklaerungSession.setGeberIstDerAktionaerSelbst(true);
        tSessionVerwaltung.setzeEnde(KonstPortalView.WEISUNG);
        return;

    }

    public void doBriefwahlAendern() {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.AUSWAHL1_GENERALVERSAMMLUNG_BRIEFWAHL)) {
            return;
        }

        /*Öffnen*/
        eclDbM.openAll();

        if (!tPruefeStartNachOpen.pruefeStartNachOpen(KonstPortalView.AUSWAHL1)) {
            eclDbM.closeAll();
            return;
        }
        tWillenserklaerungSession.setGeberIstDerAktionaerSelbst(true);
        belegeMeldungUndWillenserklaerung(1);
        eclDbM.closeAll();

        return;

    }

    public void doBriefwahlStornieren() {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.AUSWAHL1_GENERALVERSAMMLUNG_BRIEFWAHL)) {
            return;
        }

        /*Öffnen*/
        eclDbM.openAll();

        if (!tPruefeStartNachOpen.pruefeStartNachOpen(KonstPortalView.AUSWAHL1)) {
            eclDbM.closeAll();
            return;
        }
        tWillenserklaerungSession.setGeberIstDerAktionaerSelbst(true);
        belegeMeldungUndWillenserklaerung(2);
        eclDbM.closeAll();

        return;
    }

    private void belegeMeldungUndWillenserklaerung(int aendernOderStornieren) {

        EclZugeordneteMeldungNeu pZugeordneteMeldungFuerAusfuehrung = null;
        EclWillenserklaerungStatusNeu pZugeordneteWillenserklaerungStatus = null;
        EclWillenserklaerungStatusNeu hZugeordneteWillenserklaerungStatus = null;

        int gef = -1;
        for (int i = 0; i < eclBesitzGesamtM.getBesitzJeKennungListe().get(0).eigenerAREintragListe
                .get(0).zugeordneteMeldungenListe.size(); i++) {
            pZugeordneteMeldungFuerAusfuehrung = eclBesitzGesamtM.getBesitzJeKennungListe().get(0).eigenerAREintragListe
                    .get(0).zugeordneteMeldungenListe.get(i);
            for (int i1 = 0; i1 < pZugeordneteMeldungFuerAusfuehrung.zugeordneteWillenserklaerungenList.size(); i1++) {
                hZugeordneteWillenserklaerungStatus = pZugeordneteMeldungFuerAusfuehrung.zugeordneteWillenserklaerungenList
                        .get(i1);
                if (hZugeordneteWillenserklaerungStatus.getWillenserklaerung() == KonstWillenserklaerung.briefwahl
                        || hZugeordneteWillenserklaerungStatus
                                .getWillenserklaerung() == KonstWillenserklaerung.aendernBriefwahl) {
                    gef = 1;
                    pZugeordneteWillenserklaerungStatus = hZugeordneteWillenserklaerungStatus;
                }

            }

        }
        if (gef != 1) {
            CaBug.drucke("001");
        }
        if (aendernOderStornieren == 1) {
            tAuswahl.aendernAusfuehren(pZugeordneteMeldungFuerAusfuehrung, pZugeordneteWillenserklaerungStatus,
                    KonstWeisungserfassungSicht.portalWeisungserfassung);
        } else {
            tAuswahl.stornierenAusfuehren(pZugeordneteMeldungFuerAusfuehrung, pZugeordneteWillenserklaerungStatus,
                    KonstWeisungserfassungSicht.portalWeisungserfassung);
        }
    }

    public void doBriefwahlBevollmaechtigter(EclZugeordneteMeldungNeu pZugeordneteMeldungNeu) {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.AUSWAHL1_GENERALVERSAMMLUNG_BRIEFWAHL)) {
            return;
        }

        /*Öffnen*/
        eclDbM.openAll();

        if (!tPruefeStartNachOpen.pruefeStartNachOpen(KonstPortalView.AUSWAHL1)) {
            eclDbM.closeAll();
            return;
        }

        tWillenserklaerungSession.clear();
        tWillenserklaerungSession.setAusgewaehlteHauptAktion(2);
        tWillenserklaerungSession.setAusgewaehlteAktion(5);

        /*Bereitstellen der Willenserklärungsdaten für Oberfläche*/
        tWillenserklaerungSession.initZugeordneteMeldungFuerAusfuehrungListe();
        tWillenserklaerungSession.addZugeordneteMeldungFuerAusfuehrungListe(pZugeordneteMeldungNeu, null);
        tWillenserklaerungSession.ermittleGattungenFuerZugeordneteMeldungFuerAusfuehrungListe();
        tWillenserklaerungSession.ermittleGattungFuerEinzigenBesitzZugeordneteMeldungFuerAusfuehrungList();
        
//        tWillenserklaerungSession.setZugeordneteMeldungFuerAusfuehrung(pZugeordneteMeldungNeu);

//        int gattungVorhanden[] = new int[6];
        CaBug.druckeLog("tWillenserklaerungSession.getGattungEinzigeVorhanden()="
                + tWillenserklaerungSession.getGattungEinzigeVorhanden(), logDrucken, 5);
//       gattungVorhanden[tWillenserklaerungSession.getZugeordneteMeldungFuerAusfuehrung().gattung] = 1;

        blMAbstimmung.leseWeisungsliste(tWillenserklaerungSession.getGattungVorhanden(), KonstSkIst.briefwahl, KonstWeisungserfassungSicht.portalWeisungserfassung);
        blMAbstimmungsvorschlag.leseAbstimmungsvorschlagGesellschaft(eclDbM.getDbBundle());

        eclDbM.closeAll();
        tWillenserklaerungSession.setGeberIstDerAktionaerSelbst(false);

        tSessionVerwaltung.setzeEnde(KonstPortalView.WEISUNG);
        return;

    }

    public void doBriefwahlAendernBevollmaechtigter(EclZugeordneteMeldungNeu pZugeordneteMeldungNeu) {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.AUSWAHL1_GENERALVERSAMMLUNG_BRIEFWAHL)) {
            return;
        }

        /*Öffnen*/
        eclDbM.openAll();

        if (!tPruefeStartNachOpen.pruefeStartNachOpen(KonstPortalView.AUSWAHL1)) {
            eclDbM.closeAll();
            return;
        }
        belegeMeldungUndWillenserklaerungBevollmaechtigter(1, pZugeordneteMeldungNeu);
        tWillenserklaerungSession.setGeberIstDerAktionaerSelbst(false);
        eclDbM.closeAll();

        return;

    }

    public void doBriefwahlStornierenBevollmaechtigter(EclZugeordneteMeldungNeu pZugeordneteMeldungNeu) {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.AUSWAHL1_GENERALVERSAMMLUNG_BRIEFWAHL)) {
            return;
        }

        /*Öffnen*/
        eclDbM.openAll();

        if (!tPruefeStartNachOpen.pruefeStartNachOpen(KonstPortalView.AUSWAHL1)) {
            eclDbM.closeAll();
            return;
        }
        belegeMeldungUndWillenserklaerungBevollmaechtigter(2, pZugeordneteMeldungNeu);
        tWillenserklaerungSession.setGeberIstDerAktionaerSelbst(false);
        eclDbM.closeAll();

        return;

    }

    public void doBriefwahlStornierenMitgliedDurchBevollmaechtigter(EclZugeordneteMeldungNeu pZugeordneteMeldungNeu) {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.AUSWAHL1_GENERALVERSAMMLUNG_BRIEFWAHL)) {
            return;
        }

        /*Öffnen*/
        eclDbM.openAll();

        if (!tPruefeStartNachOpen.pruefeStartNachOpen(KonstPortalView.AUSWAHL1)) {
            eclDbM.closeAll();
            return;
        }
        eclDbM.closeAll();

        tWillenserklaerungSession.setGeberIstDerAktionaerSelbst(false);

//        tWillenserklaerungSession.setZugeordneteMeldungFuerAusfuehrung(pZugeordneteMeldungNeu);
        belegeMeldungUndWillenserklaerungBevollmaechtigter(2, pZugeordneteMeldungNeu);
        tSessionVerwaltung.setzeEnde(KonstPortalView.GENERALVERSAMMLUNG_BRIEFW_MITGLIED_DURCH_BEVOLL_STORNO);
        return;

    }

    private void belegeMeldungUndWillenserklaerungBevollmaechtigter(int aendernOderStornieren,
            EclZugeordneteMeldungNeu pZugeordneteMeldungNeu) {
        CaBug.druckeLog("Start aendernOderStornieren=" + aendernOderStornieren, logDrucken, 5);
        EclZugeordneteMeldungNeu pZugeordneteMeldungFuerAusfuehrung = null;
        EclWillenserklaerungStatusNeu pZugeordneteWillenserklaerungStatus = null;
        EclWillenserklaerungStatusNeu hZugeordneteWillenserklaerungStatus = null;

        int gef = -1;
        pZugeordneteMeldungFuerAusfuehrung = pZugeordneteMeldungNeu;
        for (int i1 = 0; i1 < pZugeordneteMeldungFuerAusfuehrung.zugeordneteWillenserklaerungenList.size(); i1++) {
            hZugeordneteWillenserklaerungStatus = pZugeordneteMeldungFuerAusfuehrung.zugeordneteWillenserklaerungenList
                    .get(i1);
            if (hZugeordneteWillenserklaerungStatus.getWillenserklaerung() == KonstWillenserklaerung.briefwahl
                    || hZugeordneteWillenserklaerungStatus
                            .getWillenserklaerung() == KonstWillenserklaerung.aendernBriefwahl) {
                gef = 1;
                pZugeordneteWillenserklaerungStatus = hZugeordneteWillenserklaerungStatus;
            }
        }

        if (gef != 1) {
            CaBug.drucke("001");
        }
        if (aendernOderStornieren == 1) {
            tAuswahl.aendernAusfuehren(pZugeordneteMeldungFuerAusfuehrung, pZugeordneteWillenserklaerungStatus,
                    KonstWeisungserfassungSicht.portalWeisungserfassung);
        } else {
            tAuswahl.stornierenAusfuehren(pZugeordneteMeldungFuerAusfuehrung, pZugeordneteWillenserklaerungStatus,
                    KonstWeisungserfassungSicht.portalWeisungserfassung);
        }
        CaBug.druckeLog("Ende", logDrucken, 5);
    }

    public void doBriefwahlGeerbter(EclZugeordneteMeldungNeu pZugeordneteMeldungNeu) {
        doBriefwahlBevollmaechtigter(pZugeordneteMeldungNeu);
    }

    public void doBriefwahlAendernGeerbter(EclZugeordneteMeldungNeu pZugeordneteMeldungNeu) {
        doBriefwahlAendernBevollmaechtigter(pZugeordneteMeldungNeu);
    }

    public void doBriefwahlStornierenGeerbter(EclZugeordneteMeldungNeu pZugeordneteMeldungNeu) {
        doBriefwahlStornierenBevollmaechtigter(pZugeordneteMeldungNeu);
    }

    
    /**---------------------Beiratswahl-----------------------------------*/
    public void doSRVErteilen() {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.AUSWAHL1_GENERALVERSAMMLUNG_BEIRATSWAHL)) {
            return;
        }

        /*Öffnen*/
        eclDbM.openAll();

        if (!tPruefeStartNachOpen.pruefeStartNachOpen(KonstPortalView.AUSWAHL1)) {
            eclDbM.closeAll();
            return;
        }

        tWillenserklaerungSession.clear();
        tWillenserklaerungSession.setAusgewaehlteHauptAktion(2);
        tWillenserklaerungSession.setAusgewaehlteAktion(4);

        /*Bereitstellen der Willenserklärungsdaten für Oberfläche*/
        tWillenserklaerungSession.initZugeordneteMeldungFuerAusfuehrungListe();
        tWillenserklaerungSession.addZugeordneteMeldungFuerAusfuehrungListe(eclBesitzGesamtM.getBesitzJeKennungListe().get(0).eigenerAREintragListe.get(0).zugeordneteMeldungenListe
                .get(0), null);
        tWillenserklaerungSession.ermittleGattungenFuerZugeordneteMeldungFuerAusfuehrungListe();
        tWillenserklaerungSession.ermittleGattungFuerEinzigenBesitzZugeordneteMeldungFuerAusfuehrungList();
        
//        tWillenserklaerungSession.setZugeordneteMeldungFuerAusfuehrung(
//                eclBesitzGesamtM.getBesitzJeKennungListe().get(0).eigenerAREintragListe.get(0).zugeordneteMeldungenListe
//                        .get(0));

//        int gattungVorhanden[] = new int[6];
//        gattungVorhanden[tWillenserklaerungSession.getZugeordneteMeldungFuerAusfuehrung().gattung] = 1;

        CaBug.druckeLog("tWillenserklaerungSession.getGattungEinzigeVorhanden()="
                + tWillenserklaerungSession.getGattungEinzigeVorhanden(), logDrucken, 5);
//        blMAbstimmung.leseWeisungsliste(gattungVorhanden, KonstSkIst.briefwahl, KonstWeisungserfassungSicht.portalWeisungserfassung);
        blMAbstimmung.leseWeisungsliste(tWillenserklaerungSession.getGattungVorhanden(), KonstSkIst.srv, KonstWeisungserfassungSicht.portalWeisungserfassung);
        blMAbstimmungsvorschlag.leseAbstimmungsvorschlagGesellschaft(eclDbM.getDbBundle());

        eclDbM.closeAll();
        String lokaleVariablenNamen[]= {"L_GATTUNGTEXT"};
        String lokaleVariablenInhalt[]= new String[1];
        lokaleVariablenInhalt[0]=eclParamM.getParam().paramBasis.getGattungBezeichnung(tWillenserklaerungSession.getGattungEinzigeVorhanden());
        
        eclPortalTexteM.setLokaleVariablenNamen(lokaleVariablenNamen);
        eclPortalTexteM.setLokaleVariablenInhalt(lokaleVariablenInhalt);
        
        tWillenserklaerungSession.setGeberIstDerAktionaerSelbst(true);
        tSessionVerwaltung.setzeEnde(KonstPortalView.WEISUNG);
        return;

    }

    public void doSRVAendern() {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.AUSWAHL1_GENERALVERSAMMLUNG_BEIRATSWAHL)) {
            return;
        }

        /*Öffnen*/
        eclDbM.openAll();

        if (!tPruefeStartNachOpen.pruefeStartNachOpen(KonstPortalView.AUSWAHL1)) {
            eclDbM.closeAll();
            return;
        }
        tWillenserklaerungSession.setGeberIstDerAktionaerSelbst(true);
        belegeMeldungUndWillenserklaerungSRV(1);
        eclDbM.closeAll();

        return;

    }

    public void doSRVStornieren() {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.AUSWAHL1_GENERALVERSAMMLUNG_BEIRATSWAHL)) {
            return;
        }

        /*Öffnen*/
        eclDbM.openAll();

        if (!tPruefeStartNachOpen.pruefeStartNachOpen(KonstPortalView.AUSWAHL1)) {
            eclDbM.closeAll();
            return;
        }
        tWillenserklaerungSession.setGeberIstDerAktionaerSelbst(true);
        belegeMeldungUndWillenserklaerungSRV(2);
        eclDbM.closeAll();

        return;
    }

    private void belegeMeldungUndWillenserklaerungSRV(int aendernOderStornieren) {

        EclZugeordneteMeldungNeu pZugeordneteMeldungFuerAusfuehrung = null;
        EclWillenserklaerungStatusNeu pZugeordneteWillenserklaerungStatus = null;
        EclWillenserklaerungStatusNeu hZugeordneteWillenserklaerungStatus = null;

        int gef = -1;
        for (int i = 0; i < eclBesitzGesamtM.getBesitzJeKennungListe().get(0).eigenerAREintragListe
                .get(0).zugeordneteMeldungenListe.size(); i++) {
            pZugeordneteMeldungFuerAusfuehrung = eclBesitzGesamtM.getBesitzJeKennungListe().get(0).eigenerAREintragListe
                    .get(0).zugeordneteMeldungenListe.get(i);
            for (int i1 = 0; i1 < pZugeordneteMeldungFuerAusfuehrung.zugeordneteWillenserklaerungenList.size(); i1++) {
                hZugeordneteWillenserklaerungStatus = pZugeordneteMeldungFuerAusfuehrung.zugeordneteWillenserklaerungenList
                        .get(i1);
                if (hZugeordneteWillenserklaerungStatus.getWillenserklaerung() == KonstWillenserklaerung.vollmachtUndWeisungAnSRV
                        || hZugeordneteWillenserklaerungStatus
                                .getWillenserklaerung() == KonstWillenserklaerung.aendernWeisungAnSRV
                                ) {
                    gef = 1;
                    pZugeordneteWillenserklaerungStatus = hZugeordneteWillenserklaerungStatus;
                }

            }

        }
        if (gef != 1) {
            CaBug.drucke("001");
        }
        if (aendernOderStornieren == 1) {
            tAuswahl.aendernAusfuehren(pZugeordneteMeldungFuerAusfuehrung, pZugeordneteWillenserklaerungStatus,
                    KonstWeisungserfassungSicht.portalWeisungserfassung);
        } else {
            tAuswahl.stornierenAusfuehren(pZugeordneteMeldungFuerAusfuehrung, pZugeordneteWillenserklaerungStatus,
                    KonstWeisungserfassungSicht.portalWeisungserfassung);
        }
    }

    
    /*+++++++++++++++++Gäste++++++++++++++++++++++++++++++++*/
    public void doGastkarten() {
        tAuswahl.funktionAufrufen(KonstPortalView.AUSWAHL1_GENERALVERSAMMLUNG, KonstPortalView.GASTKARTE_UEBERSICHT);
    }

    
    /*************************Seite generalversammlungAnAbmelden*******************/

    /**+++++++++++++++++++Vollmachten - ku178++++++++++++++++++*/
    /**Speichern einer gesetzlichen Vertretung - ku178-Ablauf*/
    public void doGesetzlicherVertreterSpeichern() {
        int[] ausgangsmasken= {KonstPortalView.GENERALVERSAMMLUNG_ANABMELDEN, KonstPortalView.AUSWAHL1_GENERALVERSAMMLUNG_BRIEFWAHL};
        if (!tSessionVerwaltung.pruefeStart(ausgangsmasken)) {
            return;
        }
        
        
        EclVorlaeufigeVollmachtEingabe lNeueGesetzlVollmacht=new EclVorlaeufigeVollmachtEingabe();
        
        lNeueGesetzlVollmacht.erteilendeAktionaersnummer=eclBesitzGesamtM.eigenerBestandAktienregister().aktionaersnummer;
        lNeueGesetzlVollmacht.erteilendeLoginDaten=eclLoginDatenM.getEclLoginDaten().ident;
        lNeueGesetzlVollmacht.artDerEingabe=EclVorlaeufigeVollmachtEingabe.ART_DER_EINGABE_GESETZLICHER_VERTRETER;
        lNeueGesetzlVollmacht.vertreterId="";
        lNeueGesetzlVollmacht.vertreterTitel=eclBesitzGesamtAuswahl1M.getTitelGesetzVertreter1().trim();
        lNeueGesetzlVollmacht.vertreterName=eclBesitzGesamtAuswahl1M.getNameGesetzVertreter1().trim();
        lNeueGesetzlVollmacht.vertreterVorname=eclBesitzGesamtAuswahl1M.getVornameGesetzVertreter1().trim();
        lNeueGesetzlVollmacht.vertreterZusatz=eclBesitzGesamtAuswahl1M.getZusatzGesetzVertreter1().trim();
        lNeueGesetzlVollmacht.vertreterStrasse=eclBesitzGesamtAuswahl1M.getStrasseGesetzVertreter1().trim();
        lNeueGesetzlVollmacht.vertreterPlz=eclBesitzGesamtAuswahl1M.getPlzGesetzVertreter1().trim();
        lNeueGesetzlVollmacht.vertreterOrt=eclBesitzGesamtAuswahl1M.getOrtGesetzVertreter1().trim();
        lNeueGesetzlVollmacht.vertreterMail=eclBesitzGesamtAuswahl1M.getMailGesetzVertreter1().trim();
        lNeueGesetzlVollmacht.eingabeDatum=CaDatumZeit.DatumZeitStringFuerDatenbank();
       
        /*Felder prüfen*/
        /*++++Felder prüfen++++*/
        /*Pflichtfelder Bevollmächtigter gefüllt?*/
        if (lNeueGesetzlVollmacht.vertreterName.isEmpty()
                || lNeueGesetzlVollmacht.vertreterVorname.isEmpty()
                || lNeueGesetzlVollmacht.vertreterName.isEmpty()
                || lNeueGesetzlVollmacht.vertreterStrasse.isEmpty()
                || lNeueGesetzlVollmacht.vertreterPlz.isEmpty()
                || lNeueGesetzlVollmacht.vertreterOrt.isEmpty()
                ) {
            tSession.trageFehlerEin(CaFehler.afVollmachtAllePflichtfelderEingeben);
            tSessionVerwaltung.setzeEnde();
            return;
        }
        
        /*Gesetzliche Vollmacht (zusätzlich) speichern*/
        eclDbM.openAll();
        eclDbM.openWeitere();
        
        eclDbM.getDbBundle().dbVorlaeufigeVollmachtEingabe.insert(lNeueGesetzlVollmacht);
        
        bereiteGesetzlVertretungenFuerAnzeigeAuf();
        eclDbM.closeAll();

        tSessionVerwaltung.setzeEnde();
        
    }
    
    public void doGesetzlicherVertreterLoeschen(EclVorlaeufigeVollmachtEingabe pVorlaeufigeVollmachtEingabe) {
        int[] ausgangsmasken= {KonstPortalView.GENERALVERSAMMLUNG_ANABMELDEN, KonstPortalView.AUSWAHL1_GENERALVERSAMMLUNG_BRIEFWAHL};
        if (!tSessionVerwaltung.pruefeStart(ausgangsmasken)) {
            return;
        }
        
        eclBesitzGesamtAuswahl1M.setEingabeStornoGesetzVertreterAnzeigen(true);
        eclBesitzGesamtAuswahl1M.setListeGesetzlVertreterAnzeigen(false);
        eclBesitzGesamtAuswahl1M.setEingabeMaskeGesetzVertreterAnzeigen(false);

        eclBesitzGesamtAuswahl1M.setStornoGesetzVertreter(pVorlaeufigeVollmachtEingabe);
        
        tSessionVerwaltung.setzeEnde();

    }

    public void doGesetzlicherVertreterLoeschenAbbruch() {
        int[] ausgangsmasken= {KonstPortalView.GENERALVERSAMMLUNG_ANABMELDEN, KonstPortalView.AUSWAHL1_GENERALVERSAMMLUNG_BRIEFWAHL};
        if (!tSessionVerwaltung.pruefeStart(ausgangsmasken)) {
            return;
        }
        eclDbM.openAll();
        eclDbM.openWeitere();
        bereiteGesetzlVertretungenFuerAnzeigeAuf();
        eclDbM.closeAll();
        
        tSessionVerwaltung.setzeEnde();
   }
    
    public void doGesetzlicherVertreterLoeschenAusfuehren() {
        int[] ausgangsmasken= {KonstPortalView.GENERALVERSAMMLUNG_ANABMELDEN, KonstPortalView.AUSWAHL1_GENERALVERSAMMLUNG_BRIEFWAHL};
        if (!tSessionVerwaltung.pruefeStart(ausgangsmasken)) {
            return;
        }
        eclDbM.openAll();
        eclDbM.openWeitere();
        
        EclVorlaeufigeVollmachtEingabe stornoGesetzVertreter=eclBesitzGesamtAuswahl1M.getStornoGesetzVertreter();
        stornoGesetzVertreter.wurdeStorniert=1;
        stornoGesetzVertreter.stornierungsDatum=CaDatumZeit.DatumZeitStringFuerDatenbank();
        eclDbM.getDbBundle().dbVorlaeufigeVollmachtEingabe.updateStorniert(stornoGesetzVertreter);
        bereiteGesetzlVertretungenFuerAnzeigeAuf();
        eclDbM.closeAll();
        
        tSessionVerwaltung.setzeEnde();

    }
    
//    public void doGesetzlicherVertreterNeu() {
//        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.GENERALVERSAMMLUNG_ANABMELDEN)) {
//            return;
//        }
//        
//        eclBesitzGesamtAuswahl1M.setEingabeMaskeGesetzVertreterAnzeigen(true);
//        eclBesitzGesamtAuswahl1M.clearGesetzVertreter1();
//        
//        tSessionVerwaltung.setzeEnde();
//    }
//
    
    
    
    
    /**Anzeige vorbereiten für Gesetzliche Vertretungen.
     * eclDbM und weitere muß offen sein.
     */
    private void bereiteGesetzlVertretungenFuerAnzeigeAuf() {
        eclBesitzGesamtAuswahl1M.clearGesetzVertreter1();
        CaBug.druckeLog("", logDrucken, 10);
        boolean gesetzlVertreterVorhanden=false;
        boolean gesetzlVertreterNochNichtGeprueftVorhanden=false;
       
        int anz=eclDbM.getDbBundle().dbVorlaeufigeVollmachtEingabe.readArtZuAktionaersnummer(eclBesitzGesamtM.eigenerBestandAktienregister().aktionaersnummer, EclVorlaeufigeVollmachtEingabe.ART_DER_EINGABE_GESETZLICHER_VERTRETER);
        if (anz>0) {
            List<EclVorlaeufigeVollmachtEingabe> listGesetzlVertreter=eclDbM.getDbBundle().dbVorlaeufigeVollmachtEingabe.ergebnis();
            for (int i=0;i<anz;i++) {
                if (listGesetzlVertreter.get(i).wurdeStorniert!=1) {
                    gesetzlVertreterVorhanden=true;
                    if (listGesetzlVertreter.get(i).pruefstatus==0) {
                        gesetzlVertreterNochNichtGeprueftVorhanden=true;
                    }
                }
            }
            eclBesitzGesamtAuswahl1M.setListeGesetzlVertreter(listGesetzlVertreter);
            eclBesitzGesamtAuswahl1M.setListeGesetzlVertreterIstLeer(false);
        }
        else {
            eclBesitzGesamtAuswahl1M.setListeGesetzlVertreter(null);
            eclBesitzGesamtAuswahl1M.setListeGesetzlVertreterIstLeer(true);
        }
        
        if (gesetzlVertreterVorhanden) {
            eclBesitzGesamtAuswahl1M.setEingabeMaskeGesetzVertreterAnzeigen(false);
        }
        else {
            CaBug.druckeLog("setEingabeMaskeGesetzVertreterAnzeigen true", logDrucken, 10);
            eclBesitzGesamtAuswahl1M.setEingabeMaskeGesetzVertreterAnzeigen(true);
        }
        
        eclBesitzGesamtAuswahl1M.setListeGesetzlVertreterAnzeigen(true);
        eclBesitzGesamtAuswahl1M.setEingabeStornoGesetzVertreterAnzeigen(false);
        CaBug.druckeLog("gesetzlVertreterNochNichtGeprueftVorhanden="+gesetzlVertreterNochNichtGeprueftVorhanden, logDrucken, 10);
        eclBesitzGesamtAuswahl1M.setUngepruefteGesetzlVertreterVorhanden(gesetzlVertreterNochNichtGeprueftVorhanden);
    }
    
    
    /**Erteilen einer Vorläufigen Vollmacht - ku178-Ablauf*/
    public void doVollmachtErteilen() {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.GENERALVERSAMMLUNG_ANABMELDEN)) {
            return;
        }
        
        String hArtVertreter=eclBesitzGesamtAuswahl1M.getArtVertreter1();
        if (hArtVertreter==null) {
            hArtVertreter="0";
        }
        int artVertreter=Integer.parseInt(hArtVertreter);
        
        EclVorlaeufigeVollmachtEingabe lNeueVollmacht=new EclVorlaeufigeVollmachtEingabe();
        
        lNeueVollmacht.erteilendeAktionaersnummer=eclBesitzGesamtM.eigenerBestandAktienregister().aktionaersnummer;
        lNeueVollmacht.erteilendeLoginDaten=eclLoginDatenM.getEclLoginDaten().ident;
        lNeueVollmacht.artDerEingabe=EclVorlaeufigeVollmachtEingabe.ART_DER_EINGABE_BEVOLLMAECHTIGTER;
        lNeueVollmacht.vertreterId=eclBesitzGesamtAuswahl1M.getAktienregisternummerVertreter1().trim();
        lNeueVollmacht.vertreterTitel=eclBesitzGesamtAuswahl1M.getTitelVertreter1().trim();
        lNeueVollmacht.vertreterName=eclBesitzGesamtAuswahl1M.getNameVertreter1().trim();
        lNeueVollmacht.vertreterVorname=eclBesitzGesamtAuswahl1M.getVornameVertreter1().trim();
        lNeueVollmacht.vertreterZusatz=eclBesitzGesamtAuswahl1M.getZusatzVertreter1().trim();
        lNeueVollmacht.vertreterStrasse=eclBesitzGesamtAuswahl1M.getStrasseVertreter1().trim();
        lNeueVollmacht.vertreterPlz=eclBesitzGesamtAuswahl1M.getPlzVertreter1().trim();
        lNeueVollmacht.vertreterOrt=eclBesitzGesamtAuswahl1M.getOrtVertreter1().trim();
        lNeueVollmacht.vertreterMail=eclBesitzGesamtAuswahl1M.getMailVertreter1().trim();
        lNeueVollmacht.eingabeDatum=CaDatumZeit.DatumZeitStringFuerDatenbank();

        
        lNeueVollmacht.vertreterArt=artVertreter;
        lNeueVollmacht.vertreterArtBeiSonstige=eclBesitzGesamtAuswahl1M.getSonstigeBeschreibungVertreter1().trim();
        
        /*++++Felder prüfen++++*/
        /*Pflichtfelder Bevollmächtigter gefüllt?*/
        if (lNeueVollmacht.vertreterName.isEmpty()
                || lNeueVollmacht.vertreterVorname.isEmpty()
                || lNeueVollmacht.vertreterName.isEmpty()
                || lNeueVollmacht.vertreterStrasse.isEmpty()
                || lNeueVollmacht.vertreterPlz.isEmpty()
                || lNeueVollmacht.vertreterOrt.isEmpty()
                ) {
            tSession.trageFehlerEin(CaFehler.afVollmachtAllePflichtfelderEingeben);
            tSessionVerwaltung.setzeEnde();
            return;
        }
        
        /*Vertreterart muß markiert sein*/
        if (artVertreter==0) {
            tSession.trageFehlerEin(CaFehler.afVollmachtAllePflichtfelderEingeben);
            tSessionVerwaltung.setzeEnde();
            return;
        }
        
        /*Bei "Sonstige" muß Erläuterungsfeld gefüllt sein*/
        if (artVertreter==EclVorlaeufigeVollmachtEingabe.VERTRETER_ART_SONSTIGE && 
                lNeueVollmacht.vertreterArtBeiSonstige.isEmpty()) {
            tSession.trageFehlerEin(CaFehler.afVollmachtAllePflichtfelderEingeben);
            tSessionVerwaltung.setzeEnde();
            return;
        }
        
        /*Aktionärsnummer darf nur numerisch sein*/
        if (artVertreter==EclVorlaeufigeVollmachtEingabe.VERTRETER_ART_ANDERER_AKTIONAER &&
                lNeueVollmacht.vertreterId.isEmpty()==false &&
                (CaString.isNummern(lNeueVollmacht.vertreterId)==false || 
                lNeueVollmacht.vertreterId.length()>6)) {
            tSession.trageFehlerEin(CaFehler.afVollmachtAktionaersnummerUnzulaessig);
            tSessionVerwaltung.setzeEnde();
            return;
        }
        
        /*Gesetzliche Vollmacht (zusätzlich) speichern)*/
        eclDbM.openAll();
        eclDbM.openWeitere();


        eclDbM.getDbBundle().dbVorlaeufigeVollmachtEingabe.insert(lNeueVollmacht);
        
        bereiteBevollmaechtigtenFuerAnzeigeAuf();
        eclDbM.closeAll();

        tSessionVerwaltung.setzeEnde();
    }

    
    public void doBevollmaechtigtenWiderrufen(EclVorlaeufigeVollmachtEingabe pVorlaeufigeVollmachtEingabe) {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.GENERALVERSAMMLUNG_ANABMELDEN)) {
            return;
        }
        
        eclBesitzGesamtAuswahl1M.setEingabeStornoBevollmaechtigterAnzeigen(true);
        eclBesitzGesamtAuswahl1M.setListeBevollmaechtigterAnzeigen(false);
        eclBesitzGesamtAuswahl1M.setEingabeMaskeBevollmaechtigterAnzeigen(false);

        eclBesitzGesamtAuswahl1M.setStornoBevollmaechtigter(pVorlaeufigeVollmachtEingabe);
        
        tSessionVerwaltung.setzeEnde();

    }

    public void doBevollmaechtigtenWiderrufenAbbruch() {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.GENERALVERSAMMLUNG_ANABMELDEN)) {
            return;
        }
        eclDbM.openAll();
        eclDbM.openWeitere();
        bereiteBevollmaechtigtenFuerAnzeigeAuf();
        eclDbM.closeAll();
        
        tSessionVerwaltung.setzeEnde();
   }
    
    public void doBevollmaechtigtenWiderrufenAusfuehren() {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.GENERALVERSAMMLUNG_ANABMELDEN)) {
            return;
        }
        eclDbM.openAll();
        eclDbM.openWeitere();
        
        EclVorlaeufigeVollmachtEingabe stornoBevollmaechtigter=eclBesitzGesamtAuswahl1M.getStornoBevollmaechtigter();
        stornoBevollmaechtigter.wurdeStorniert=1;
        stornoBevollmaechtigter.stornierungsDatum=CaDatumZeit.DatumZeitStringFuerDatenbank();
        eclDbM.getDbBundle().dbVorlaeufigeVollmachtEingabe.updateStorniert(stornoBevollmaechtigter);
        bereiteBevollmaechtigtenFuerAnzeigeAuf();
        eclBesitzGesamtAuswahl1M.clearVertreter1();
        eclDbM.closeAll();
        
        tSessionVerwaltung.setzeEnde();

    }

    
    /**Anzeige vorbereiten für Bevollmächtigungen
     * eclDbM und weitere muß offen sein.
     */
    private void bereiteBevollmaechtigtenFuerAnzeigeAuf() {
        CaBug.druckeLog("", logDrucken, 10);
        eclBesitzGesamtAuswahl1M.clearVertreter1();
        boolean bevollmaechtigterVorhanden=false;
        boolean bevollmaechtigterNochNichtGeprueftVorhanden=false;
        
        int anz=eclDbM.getDbBundle().dbVorlaeufigeVollmachtEingabe.readArtZuAktionaersnummer(eclBesitzGesamtM.eigenerBestandAktienregister().aktionaersnummer, EclVorlaeufigeVollmachtEingabe.ART_DER_EINGABE_BEVOLLMAECHTIGTER);
        if (anz>0) {
            List<EclVorlaeufigeVollmachtEingabe> listBevollmaechtigte=eclDbM.getDbBundle().dbVorlaeufigeVollmachtEingabe.ergebnis();
            for (int i=0;i<anz;i++) {
                if (listBevollmaechtigte.get(i).wurdeStorniert!=1) {
                    bevollmaechtigterVorhanden=true;
                    if (listBevollmaechtigte.get(i).pruefstatus==0) {
                        bevollmaechtigterNochNichtGeprueftVorhanden=true;
                    }
                }
            }
            eclBesitzGesamtAuswahl1M.setListeBevollmaechtigte(listBevollmaechtigte);
            eclBesitzGesamtAuswahl1M.setListeBevollmaechtigterIstLeer(false);
        }
        else {
            eclBesitzGesamtAuswahl1M.setListeBevollmaechtigte(null);
            eclBesitzGesamtAuswahl1M.setListeBevollmaechtigterIstLeer(true);
        }
        
        if (bevollmaechtigterVorhanden) {
            eclBesitzGesamtAuswahl1M.setEingabeMaskeBevollmaechtigterAnzeigen(false);
        }
        else {
            CaBug.druckeLog("setEingabeMaskeBevollmaechtigterAnzeigen true", logDrucken, 10);
            eclBesitzGesamtAuswahl1M.setEingabeMaskeBevollmaechtigterAnzeigen(true);
        }
        
        eclBesitzGesamtAuswahl1M.setListeBevollmaechtigterAnzeigen(true);
        eclBesitzGesamtAuswahl1M.setEingabeStornoBevollmaechtigterAnzeigen(false);
        eclBesitzGesamtAuswahl1M.setUngepruefteBevollmaechtigterVorhanden(bevollmaechtigterNochNichtGeprueftVorhanden);
        
    }

    
    /**Anzeige vorbereiten für Bevollmächtigungen
     * eclDbM und weitere muß offen sein.
     */
    public void bereiteNachweiseFuerAnzeigeAuf() {
        CaBug.druckeLog("", logDrucken, 10);
        
        boolean nachweiseNochNichtGeprueftVorhanden=false;
        
        int anz=eclDbM.getDbBundle().dbVorlaeufigeVollmachtEingabe.readArtZuAktionaersnummer(eclBesitzGesamtM.eigenerBestandAktienregister().aktionaersnummer, EclVorlaeufigeVollmachtEingabe.ART_DER_EINGABE_NUR_NACHWEIS);
        if (anz>0) {
            List<EclVorlaeufigeVollmachtEingabe> listBevollmaechtigte=eclDbM.getDbBundle().dbVorlaeufigeVollmachtEingabe.ergebnis();
            eclBesitzGesamtAuswahl1M.setListeNachweise(listBevollmaechtigte);
            eclBesitzGesamtAuswahl1M.setListeNachweiseIstLeer(false);
            for (int i=0;i<anz;i++) {
                if (listBevollmaechtigte.get(i).pruefstatus==0) {
                    nachweiseNochNichtGeprueftVorhanden=true;
                }
            }
        }
        else {
            eclBesitzGesamtAuswahl1M.setListeNachweise(null);
            eclBesitzGesamtAuswahl1M.setListeNachweiseIstLeer(true);
        }
        
        eclBesitzGesamtAuswahl1M.setUngepruefteNachweiseVorhanden(nachweiseNochNichtGeprueftVorhanden);

    }
   
    /*++++++++++++++++++++++++++++++++Weiter / An-Abmelden, Veranstaltungen+++++++++++++++++++++++*/
    
    public void doWeiter() {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.GENERALVERSAMMLUNG_ANABMELDEN)) {
            return;
        }
        
        eclDbM.openAll();
        eclDbM.openWeitere();

       int rc=ausfuehrenWeiter(true);

       eclDbM.closeAll();
       
       if (rc<1) {
           if (rc!=CaFehler.generalFreierText) {
               tSession.trageFehlerEin(rc);
           }
           else {
               tSession.trageFehlerEinMitArt(fehlerTextVeranstaltungen, 1);
           }
           tSessionVerwaltung.setzeEnde();
           return;
       }
       
       tSessionVerwaltung.setzeEnde(KonstPortalView.AUSWAHL1_GENERALVERSAMMLUNG);

    }
    
    /**Wird als Fehlertext zurückgegeben, wenn bei Veranstaltungen ein Fehler aufgetreten ist*/
    private String fehlerTextVeranstaltungen="";
    /**openAll und Close in aufrufender Funktion ausführen
     * rc<0 => Fehler aufgetreten*/
    public int ausfuehrenWeiter(boolean pMitVeranstaltungen) {
        
        if (eclParamM.getParam().paramPortal.freiwilligeAnmeldungMitVertretereingabe==3) {
            String lAnmeldung=eclBesitzGesamtAuswahl1M.getAnmeldung();
            /*Nur prüfen, wenn lAnmeldung gewählt, und wenn nicht "Abgemeldet" gewählt wurde*/
            if (lAnmeldung!=null && lAnmeldung.equals("2")==false) {
                /*Beim ku178-Verfahren: prüfen, ob Vollmachten / Gesetzl.Vertreter eingegeben, aber nicht gespeichert ist*/
                if (eclBesitzGesamtAuswahl1M.pruefeObGesetzVertreter1FelderLeer()==false) {
                    return CaFehler.afGesetzlVollmachtNichtGespeichert;
                }
                if (lAnmeldung.equals("4")) {
                    /*Vertreter prüfen nur bei "Es kommt ein Bevollmächtigter"*/
                    if (eclBesitzGesamtAuswahl1M.pruefeObVertreter1FelderLeer()==false) {
                        return CaFehler.afVollmachtNichtErteilt;
                    }
                }
            }
        }

        
        if (eclParamM.getParam().paramPortal.freiwilligeAnmeldungNurPapier==0) {
            String auswahl = eclBesitzGesamtAuswahl1M.getAnmeldung();
            
            BlVeranstaltungen blVeranstaltungen=new BlVeranstaltungen(true, eclDbM.getDbBundle());
            
            if (pMitVeranstaltungen) {
                /*Falls Veranstaltungen, dann Veranstaltungen prüfen*/
                if (eclVeranstaltungenM.pruefeObVeranstaltungenVorhanden()==true) {
                    blVeranstaltungen.rcVeranstaltungenListe=eclVeranstaltungenM.getVeranstaltungenListe();

                    boolean brc=blVeranstaltungen.pruefeVeranstaltungsliste(true);
                    if (brc==false) {
                        fehlerTextVeranstaltungen=blVeranstaltungen.rcFehlerText;
                        return CaFehler.generalFreierText;
                    }
                }
            }
            
            if (auswahl == null || auswahl.isEmpty()) {
                return CaFehler.afBitteAnOderAbmelden;
            }

            
            int anmeldenFunktion = 0;
            switch (auswahl) {
            case "1":
                anmeldenFunktion = 1;
                break;
            case "2":
                anmeldenFunktion = 2;
                break;
            case "3":
                anmeldenFunktion = 3;
                break;
            case "4":
                anmeldenFunktion = 4;
                break;
            case "5":
                anmeldenFunktion = 5;
                break;
            }
            
            String auswahlAlt = eclBesitzGesamtAuswahl1M.getAnmeldungAlt();
            int anmeldenFunktionAlt = 0;
            switch (auswahlAlt) {
            case "1":
                anmeldenFunktionAlt = 1;
                break;
            case "2":
                anmeldenFunktionAlt = 2;
                break;
            case "3":
                anmeldenFunktionAlt = 3;
                break;
            case "4":
                anmeldenFunktionAlt = 4;
                break;
            case "5":
                anmeldenFunktionAlt = 5;
                break;
            }

            String nameVertreter1=eclBesitzGesamtAuswahl1M.getNameVertreter1();
            String ortVertreter1=eclBesitzGesamtAuswahl1M.getOrtVertreter1();
            String nameVertreter2=eclBesitzGesamtAuswahl1M.getNameVertreter2();
            String ortVertreter2=eclBesitzGesamtAuswahl1M.getOrtVertreter2();
            boolean gastkarteFuerMitglied=eclBesitzGesamtAuswahl1M.isGastkarteFuerMitglied();
            boolean gastkarteFuerZweitePerson=eclBesitzGesamtAuswahl1M.isGastkarteFuerZweitePerson();

            String nameVertreter1Alt=eclBesitzGesamtAuswahl1M.getNameVertreter1Alt();
            String ortVertreter1Alt=eclBesitzGesamtAuswahl1M.getOrtVertreter1Alt();
            String nameVertreter2Alt=eclBesitzGesamtAuswahl1M.getNameVertreter2Alt();
            String ortVertreter2Alt=eclBesitzGesamtAuswahl1M.getOrtVertreter2Alt();
            boolean gastkarteFuerMitgliedAlt=eclBesitzGesamtAuswahl1M.isGastkarteFuerMitgliedAlt();
            boolean gastkarteFuerZweitePersonAlt=eclBesitzGesamtAuswahl1M.isGastkarteFuerZweitePersonAlt();


            /*Vertreterdaten überprüfen*/
            if (eclParamM.liefereFreiwilligeAnmeldungMitVertretereingabe()==true) {
                if (eclParamM.getParam().paramPortal.freiwilligeAnmeldungMitVertretereingabe!=3) {
                    boolean vertretereingabeZwingend=(eclParamM.getParam().paramPortal.freiwilligeAnmeldungMitVertretereingabe==2);

                    if (anmeldenFunktion==4 ||
                            ((anmeldenFunktion==1 || anmeldenFunktion ==3) && eclBesitzGesamtAuswahl1M.isSelbstAnmeldungOhneGesetzlichenVertreterMoeglich()==false)
                            ) {
                        /*Vertreter 1 in diesem Fall immer angeboten*/
                        if ((nameVertreter1.isEmpty() && !ortVertreter1.isEmpty()) ||
                                (!nameVertreter1.isEmpty() && ortVertreter1.isEmpty()) ||
                                (vertretereingabeZwingend && nameVertreter1.isEmpty()) ||
                                (vertretereingabeZwingend && ortVertreter1.isEmpty()) 
                                ) {
                            return CaFehler.afVertreterVollstaendigEingeben;
                        }
                        if (anmeldenFunktion==3) {
                            /*Vertreter 2 */
                            if ((nameVertreter2.isEmpty() && !ortVertreter2.isEmpty()) ||
                                    (!nameVertreter2.isEmpty() && ortVertreter2.isEmpty()) ||
                                    (vertretereingabeZwingend && nameVertreter2.isEmpty()) ||
                                    (vertretereingabeZwingend && ortVertreter2.isEmpty()) 
                                    ) {
                                return CaFehler.afVertreterVollstaendigEingeben;
                            }
                        }

                        if (eclParamM.liefereCheckboxBeiVollmacht()==true) {
                            /*Überprüfen, ob Checkbox zu Vertretungsbedingungen angehakt*/
                            if (eclBesitzGesamtAuswahl1M.isBestaetigtDassBerechtigt()==false) {
                                return CaFehler.afHinweisBeiVollmachtBestaetigt;
                            }
                        }

                    }

                    if (gastkarteFuerZweitePerson) {
                        if (nameVertreter2.isEmpty() || ortVertreter2.isEmpty()) {
                            return CaFehler.afGastkarteZweitePersonVollstaendigEingeben;
                        }
                    }
                }
                else {
                    /*+++++++++++ku178-Variante +++++++++++++++++++++++++++*/
                    /*????????????????????????????*/
                }
            }

            boolean vertreterAufGeprueftSetzen=eclBesitzGesamtAuswahl1M.isVertreterAufGeprueftSetzen();

            if (eclParamM.liefereFreiwilligeAnmeldungMitVertretereingabe()==false) {
                blVeranstaltungen.gv_anAbmeldung(eclBesitzGesamtM.eigenerBestandMeldung().meldungsIdent, anmeldenFunktion, 1);
            }
            else {
                if (eclParamM.getParam().paramPortal.freiwilligeAnmeldungMitVertretereingabe!=3) {
                    blVeranstaltungen.gv_anAbmeldung(eclBesitzGesamtM.eigenerBestandMeldung().meldungsIdent, anmeldenFunktion, 1, anmeldenFunktionAlt, eclBesitzGesamtAuswahl1M.isSelbstAnmeldungOhneGesetzlichenVertreterMoeglich(),
                            nameVertreter1, ortVertreter1, nameVertreter2, ortVertreter2,gastkarteFuerMitglied, gastkarteFuerZweitePerson, 
                            nameVertreter1Alt, ortVertreter1Alt, nameVertreter2Alt, ortVertreter2Alt, gastkarteFuerMitgliedAlt, gastkarteFuerZweitePersonAlt,
                            vertreterAufGeprueftSetzen
                            );
                }
                else {
                    /*+++++++++++ku178-Variante +++++++++++++++++++++++++++*/
                    blVeranstaltungen.gv_anAbmeldung(eclBesitzGesamtM.eigenerBestandMeldung().meldungsIdent, anmeldenFunktion, 1);
               }
            }


            if (pMitVeranstaltungen) {
                /**Nun ggf. noch Veranstaltungen abspeichern und für Quittung aufbereiten*/
                if (eclVeranstaltungenM.pruefeObVeranstaltungenVorhanden()==true) {
                    blVeranstaltungen.loginKennung=eclLoginDatenM.getEclLoginDaten().loginKennung;
                    blVeranstaltungen.aktionenAusfuehren();
                    blVeranstaltungen.speichereWerteVeranstaltungsliste();
                    blVeranstaltungen.rcQuittungsArt=2;
                    blVeranstaltungen.aufbereitenQuittung();
                    /*Werte müssen neu eingelesen werden, damit ggf. alte Werte für Veränderung richtig belegt sind.
                     * Muß nach aufbereitenQuittung erfolgen, weil sonst Veränderungen nicht mehr richtig
                     * erkannt werden!
                     */
                    blVeranstaltungen.belegeWerteVeranstaltungsliste();
                    eclVeranstaltungenM.setVeranstaltungenQuittungListe(blVeranstaltungen.rcVeranstaltungenQuittungListe);
                }
            }

            init();
        }

        return 1;
    }

    public void doZurueck() {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.GENERALVERSAMMLUNG_ANABMELDEN)) {
            return;
        }
        tSessionVerwaltung.setzeEnde(KonstPortalView.AUSWAHL1_GENERALVERSAMMLUNG);
        return;
    }

    //
    //	public void doButton1() {
    //		if (!aFunktionen.pruefeStart("aAuswahl1Generalversammlung")){return ;}
    //		eclDbM.openAll();
    //		Blku178Formulare blku178Formulare=new Blku178Formulare(eclDbM.getDbBundle());
    //		String pfad1=blku178Formulare.lieferePfadFuerElternvollmachtsformular();
    //		eclDbM.closeAll();
    //		
    //		RpBrowserAnzeigen rpBrowserAnzeigen=new RpBrowserAnzeigen();
    //		rpBrowserAnzeigen.zeigen(pfad1);
    //		aFunktionen.setzeEnde();
    //
    //	}

    /***********************************Seite iGeneralversammlungVollmachtStornieren+++++++++++++++++++++++++++++++++++*/
    public void doZurueckStornieren() {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.GENERALVERSAMMLUNG_VOLLMACHT_STORNIEREN)) {
            return;
        }
        eclDbM.openAll();
        eclDbM.openWeitere();
        init();
        eclDbM.closeAll();
        tSessionVerwaltung.setzeEnde(KonstPortalView.AUSWAHL1_GENERALVERSAMMLUNG);
        return;
    }

    public void doStornierenBestaetigen() {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.GENERALVERSAMMLUNG_VOLLMACHT_STORNIEREN)) {
            return;
        }
        eclDbM.openAll();
        eclDbM.openWeitere();

        /*Zu stornierende Vollmacht - im Original - einlesen*/
        int zuStornierendeVorlVollmachtIdent = eclBesitzGesamtAuswahl1M.getVollmachtAnDritteEingetragenEcl().ident;
        eclDbM.getDbBundle().dbVorlaeufigeVollmacht.readIdent(zuStornierendeVorlVollmachtIdent);
        EclVorlaeufigeVollmacht eclVorlaeufigeVollmacht = new EclVorlaeufigeVollmacht();
        eclVorlaeufigeVollmacht = eclDbM.getDbBundle().dbVorlaeufigeVollmacht.ergebnisPosition(0);

        if (ParamSpezial.ku178(eclDbM.getDbBundle().clGlobalVar.mandant)) {
            /*Mail an Vollmacht-Empfänger, so eingetragen*/
            String mailEmpfaengerBevollmaechtiger = eclVorlaeufigeVollmacht.bevollmaechtigterEMail;
            if (!mailEmpfaengerBevollmaechtiger.isEmpty()) {
                /*Mail an Bevollmächtigtem*/
                String mailBetreff = eclTextePortalM.holeText("1122");
                String mailText = eclTextePortalM.holeText("1123");
                baMailM.senden(mailEmpfaengerBevollmaechtiger, mailBetreff, mailText);
            }
        }

        /*Storno durchführen*/
        BlVorlaeufigeVollmacht blVorlaeufigeVollmacht = new BlVorlaeufigeVollmacht(eclDbM.getDbBundle());
        blVorlaeufigeVollmacht.storniere(eclVorlaeufigeVollmacht, KonstWillenserklaerungWeg.anmeldestelleManuell);

        init();
        eclDbM.closeAll();
        tSessionVerwaltung.setzeEnde(KonstPortalView.AUSWAHL1_GENERALVERSAMMLUNG);
        return;
    }

    /***************************Seite iGeneralversammlungBriefwMitgliedDurchBevollStorno**********************/
    public void doZurueckBriefwMitglStornoDurchBevoll() {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.GENERALVERSAMMLUNG_BRIEFW_MITGLIED_DURCH_BEVOLL_STORNO)) {
            return;
        }
        eclDbM.openAll();
        eclDbM.openWeitere();
        init();
        eclDbM.closeAll();
        tSessionVerwaltung.setzeEnde(KonstPortalView.AUSWAHL1_GENERALVERSAMMLUNG_BRIEFWAHL);
        return;
    }

    public void doStornierenBestaetigenBriefwMitglStornoDurchBevoll() {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.GENERALVERSAMMLUNG_BRIEFW_MITGLIED_DURCH_BEVOLL_STORNO)) {
            return;
        }
        eclDbM.openAll();
        eclDbM.openWeitere();
        
        for (int i = 0; i < tWillenserklaerungSession.getZugeordneteMeldungFuerAusfuehrungListe().size(); i++) {
            EclZugeordneteMeldungNeu lZugeordneteMeldung = tWillenserklaerungSession
                    .getZugeordneteMeldungFuerAusfuehrungListe().get(i);


            BlWillenserklaerung vmWillenserklaerung = new BlWillenserklaerung();
            vmWillenserklaerung.pErteiltAufWeg = KonstWillenserklaerungWeg.portal;
            vmWillenserklaerung.pErteiltZeitpunkt = "";
            vmWillenserklaerung.piMeldungsIdentAktionaer = lZugeordneteMeldung.meldungsIdent;
            vmWillenserklaerung.pAufnehmendeSammelkarteIdent = lZugeordneteMeldung.eclMeldung.meldungEnthaltenInSammelkarte;
            CaBug.druckeLog("vmWillenserklaerung.piMeldungsIdentAktionaer=" + vmWillenserklaerung.piMeldungsIdentAktionaer,
                    logDrucken, 5);
            vmWillenserklaerung.pWillenserklaerungGeberIdent = 0; /*Egal wer*/
            vmWillenserklaerung.widerrufBriefwahl(eclDbM.getDbBundle());
            if (vmWillenserklaerung.rcIstZulaessig == false) {
                CaBug.drucke(
                        "001 vmWillenserklaerung.rcGrundFuerUnzulaessig=" + vmWillenserklaerung.rcGrundFuerUnzulaessig);
            }
        }

        init();
        eclDbM.closeAll();
        tSessionVerwaltung.setzeEnde(KonstPortalView.AUSWAHL1_GENERALVERSAMMLUNG_BRIEFWAHL);
        return;
    }
    
    public void doVollmachtAbschickenUpload() {
        CaBug.druckeLog("Upload Vollmacht", 10, 10);
    }
    
    public void doAbschickenUploadBriefwahl() {
        CaBug.druckeLog("Upload Briefwahl", 10, 10);
    }


}
