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

import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComAllg.CaString;
import de.meetingapps.meetingportal.meetComAllg.CaVariablenInText;
import de.meetingapps.meetingportal.meetComBa.BaMailM;
import de.meetingapps.meetingportal.meetComBl.BlGastkarte;
import de.meetingapps.meetingportal.meetComEclM.EclAnredeListeM;
import de.meetingapps.meetingportal.meetComEclM.EclDbM;
import de.meetingapps.meetingportal.meetComEclM.EclLoginDatenM;
import de.meetingapps.meetingportal.meetComEclM.EclParamM;
import de.meetingapps.meetingportal.meetComEclM.EclPortalTexteM;
import de.meetingapps.meetingportal.meetComEntities.EclMeldung;
import de.meetingapps.meetingportal.meetComEntities.EclMeldungenMeldungen;
import de.meetingapps.meetingportal.meetComKonst.KonstPortalFunktionen;
import de.meetingapps.meetingportal.meetComKonst.KonstPortalTexte;
import de.meetingapps.meetingportal.meetComKonst.KonstPortalView;
import de.meetingapps.meetingportal.meetingCoreReport.RpBrowserAnzeigen;
import de.meetingapps.meetingportal.meetingportTFunktionen.TPruefeStartNachOpen;
import de.meetingapps.meetingportal.meetingportTFunktionen.TSessionVerwaltung;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@RequestScoped
@Named
public class TGaeste {

    
    private @Inject EclDbM eclDbM;
    private @Inject EclParamM eclParamM;
    private @Inject TGaesteSession tGaesteSession;
    private @Inject EclLoginDatenM eclLoginDatenM;
    private @Inject EclPortalTexteM eclPortalTexteM;
    private @Inject TSessionVerwaltung tSessionVerwaltung;
    private @Inject TPruefeStartNachOpen tPruefeStartNachOpen;
    private @Inject EclAnredeListeM eclAnredeListeM;
    private @Inject TSession tSession;
    private @Inject BaMailM baMailM;
    
    public int init(boolean pOpen) {
        if (pOpen==false) {
            eclDbM.openAll();
        }
        
        leseZugeordneteGaeste();
        setzeBasisWerte();
        belegeTextVariablen();
        
        if (pOpen==false) {
            eclDbM.closeAll();
        }
        
        return 1;
    }
    

    private void leseZugeordneteGaeste() {
        List<EclMeldungenMeldungen> lMeldungenMeldungenList=
        eclDbM.getDbBundle().dbMeldungenMeldungen.leseVonMeldungenZuVerwendung(eclLoginDatenM.getEclAktienregister().aktienregisterIdent, 2);
        
        int anzahl=0;
        for (int i=0;i<lMeldungenMeldungenList.size();i++) {
            if (lMeldungenMeldungenList.get(i).verwendung>0) {
                anzahl++;
            }
        }
        tGaesteSession.setGastkartenListe(lMeldungenMeldungenList);
        tGaesteSession.setAnzahlVorhandeneGaeste(anzahl);
    }
    
    /**Setzt die Basiswerte in TGaesteSession*/
    private void setzeBasisWerte() {
        int parameterGaeste=eclParamM.getParam().paramPortal.gastkartenAnforderungMoeglich;
        if (parameterGaeste==-2) {
            tGaesteSession.setVeraendernIstZulaessig(false);
            tGaesteSession.setNeuerGastZulaessig(false);
        }
        else {
            tGaesteSession.setVeraendernIstZulaessig(true);
            tGaesteSession.setNeuerGastZulaessig(true);
        }
        
        int anzGaesteVorhanden=tGaesteSession.liefereAnzahlVorhandeneGaeste();
        int anzGaesteMaximal=0;
        if (parameterGaeste>0) {
            anzGaesteMaximal=parameterGaeste;
        }
        if (parameterGaeste==-1) {
            String hString=eclLoginDatenM.getEclAktienregister().emailVersand;
            if (CaString.isNummern(hString)) {
                anzGaesteMaximal=Integer.parseInt(hString);
            }
        }
        tGaesteSession.setAnzGaesteInsgesamtZulaessig(anzGaesteMaximal);
        int anzGaesteNochZulaessig=anzGaesteMaximal-anzGaesteVorhanden;
        tGaesteSession.setAnzGaesteNochZulaessig(anzGaesteNochZulaessig);
        if (anzGaesteNochZulaessig<1) {
            tGaesteSession.setNeuerGastZulaessig(false);
        }
    }
    
    

    private void belegeTextVariablen() {
//      @formatter:off
        
        String lokaleVariablenNamen[]= {
                "L_AnzGaesteInsgesamtZulaessig",
                "L_AnzGaesteBereitsVorhangen",
                "L_AnzGaesteNochZulaessig"
                };
        
        String lokaleVariablenInhalt[]= new String[lokaleVariablenNamen.length];
        for (int i=0;i<lokaleVariablenNamen.length;i++) {
            lokaleVariablenInhalt[i]="";
        }
        lokaleVariablenInhalt[0]=Integer.toString(tGaesteSession.getAnzGaesteInsgesamtZulaessig());
        lokaleVariablenInhalt[1]=Integer.toString(tGaesteSession.liefereAnzahlVorhandeneGaeste());
        lokaleVariablenInhalt[2]=Integer.toString(tGaesteSession.getAnzGaesteNochZulaessig());
        
//      @formatter:on
        
        eclPortalTexteM.setLokaleVariablenNamen(lokaleVariablenNamen);
        eclPortalTexteM.setLokaleVariablenInhalt(lokaleVariablenInhalt);

    }

    
    public int doZurueckUebersicht() {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.GASTKARTE_UEBERSICHT)) {
            return 1;
        }
        tSessionVerwaltung.setzeEnde(tSession.getRueckkehrZuMenue());
        return 1;
    }

    public void doNeueGastkarte() {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.GASTKARTE_UEBERSICHT)) {
            return;
        }

        eclDbM.openAll();
        boolean brc = tPruefeStartNachOpen.pruefeNachOpenPortalFunktion(KonstPortalFunktionen.gaeste, true);
        if (brc == false) {
            eclDbM.closeAll();
            return;
        }
        
        eclAnredeListeM.fuelleListe(eclDbM.getDbBundle());

        tGaesteSession.initFelder(eclDbM.getDbBundle().param.paramGaesteModul);
        
        
        eclDbM.closeAll();
        tSessionVerwaltung.setzeEnde(KonstPortalView.GASTKARTE_EINGABE);
        return;
    }
    
    public void doPDF(EclMeldungenMeldungen pGast) {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.GASTKARTE_UEBERSICHT)) {
            return;
        }
        eclDbM.openAll();
        
        eclDbM.getDbBundle().dbMeldungen.leseZuIdent(pGast.zuMeldungsIdent);
        
        
        BlGastkarte blGastkarte=new BlGastkarte(eclDbM.getDbBundle());
        blGastkarte.pVersandart=3;
        blGastkarte.pGast=eclDbM.getDbBundle().dbMeldungen.meldungenArray[0];
        blGastkarte.pGastEKFormular=2;
        
        blGastkarte.druckenWiederholen(eclDbM.getDbBundle());
        
        String pdfName=blGastkarte.rcNamePDF;
        eclDbM.closeAll();
        
        
        
        tSessionVerwaltung.setzeEnde(KonstPortalView.GASTKARTE_UEBERSICHT);
        
        RpBrowserAnzeigen rpBrowserAnzeigen = new RpBrowserAnzeigen();
        rpBrowserAnzeigen.zeigen(pdfName);
        return;
    }

    public void doMail(EclMeldungenMeldungen pGast) {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.GASTKARTE_UEBERSICHT)) {
            return;
        }
        eclDbM.openAll();
        
        eclDbM.getDbBundle().dbMeldungen.leseZuIdent(pGast.zuMeldungsIdent);
        
        
        BlGastkarte blGastkarte=new BlGastkarte(eclDbM.getDbBundle());
        blGastkarte.pVersandart=4;
        blGastkarte.pGast=eclDbM.getDbBundle().dbMeldungen.meldungenArray[0];
        blGastkarte.pGastEKFormular=2;
        
        blGastkarte.druckenWiederholen(eclDbM.getDbBundle());
        
        String pdfName=blGastkarte.rcNamePDF;
        eclDbM.closeAll();
        
        String betreffText = eclPortalTexteM.holeIText(KonstPortalTexte.GASTKARTEN_AKTIONAERSPORTAL_MAIL_BETREFF);
        betreffText = CaVariablenInText.aufbereiteFuerNormalesMail(betreffText);

        String inhaltText = eclPortalTexteM.holeIText(KonstPortalTexte.GASTKARTEN_AKTIONAERSPORTAL_MAIL_INHALT);
        inhaltText = CaVariablenInText.aufbereiteFuerNormalesMail(inhaltText);

        
        baMailM.sendenMitAnhang(blGastkarte.pGast.mailadresse, betreffText, inhaltText, pdfName);
        
        tSession.trageFehlerEinMitArt(CaFehler.afGastKartePerMailVerschickt, 2);
        tSessionVerwaltung.setzeEnde(KonstPortalView.GASTKARTE_UEBERSICHT);
        return;
    }

    public void doStornieren(EclMeldungenMeldungen pGast) {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.GASTKARTE_UEBERSICHT)) {
            return;
        }

        eclDbM.openAll();
        boolean brc = tPruefeStartNachOpen.pruefeNachOpenPortalFunktion(KonstPortalFunktionen.gaeste, true);
        if (brc == false) {
            eclDbM.closeAll();
            return;
        }
        
        eclDbM.getDbBundle().dbMeldungenMeldungen.storniere(pGast);
        
        init(true);
        
        eclDbM.closeAll();
        tSession.trageFehlerEinMitArt(CaFehler.afGastWurdeStorniert, 2);
        tSessionVerwaltung.setzeEnde(KonstPortalView.GASTKARTE_UEBERSICHT);
        return;
    }

    
    public int doSpeichernNeuerGast() {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.GASTKARTE_EINGABE)) {
            return 1;
        }

        eclDbM.openAll();
        
        if (!tPruefeStartNachOpen.pruefeStartNachOpen()) {
            tSessionVerwaltung.setzeEnde();
            eclDbM.closeAll();
            return 0;
        }
        
        /*Überprüfen auf korrekte Eingabe*/
        int rc=tGaesteSession.pruefeGasteingabe();
        if (rc<0) {
            /*Fehler aufgetreten*/
            tSessionVerwaltung.setzeEnde();
            eclDbM.closeAll();
            tSession.trageFehlerEin(rc);
            return 0;
        }
         
        /*Speichern*/
        BlGastkarte blGastkarte = new BlGastkarte(eclDbM.getDbBundle());
        EclMeldung lGastMeldung = new EclMeldung();
        tGaesteSession.copyTo(lGastMeldung);
        blGastkarte.pVersandart=3;
        blGastkarte.pGast = lGastMeldung;
        blGastkarte.pWillenserklaerungGeberIdent = 0; /*undefiniert*/
        int erg = blGastkarte.ausstellen();
        if (erg < 0) {
            tSessionVerwaltung.setzeEnde();
            eclDbM.closeAll();
            tSession.trageFehlerEin(erg);
            return 0;
        }
        
        /*Zuordnen zu Aktionär*/
        blGastkarte.ordneZuAktionaerZu(eclLoginDatenM.getEclAktienregister().aktienregisterIdent);
        
        /*Neu einlesen für Übersicht*/
        init(true);
        eclDbM.closeAll();
        
        tSessionVerwaltung.setzeEnde(KonstPortalView.GASTKARTE_UEBERSICHT);

        return 1;
    }
    
    public int doZurueckEingabe() {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.GASTKARTE_EINGABE)) {
            return 1;
        }

        tSessionVerwaltung.setzeEnde(KonstPortalView.GASTKARTE_UEBERSICHT);
        return 1;
    }

}
