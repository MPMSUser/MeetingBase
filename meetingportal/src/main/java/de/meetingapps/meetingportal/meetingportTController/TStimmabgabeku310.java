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

import java.util.LinkedList;
import java.util.List;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComAllg.CaString;
import de.meetingapps.meetingportal.meetComBlManaged.BlMAbstimmung;
import de.meetingapps.meetingportal.meetComBlManaged.BlMWillenserklaerungStatusNeuVerarbeiten;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEclM.EclAbstimmungM;
import de.meetingapps.meetingportal.meetComEclM.EclAbstimmungSetM;
import de.meetingapps.meetingportal.meetComEclM.EclAbstimmungenListeM;
import de.meetingapps.meetingportal.meetComEclM.EclBesitzGesamtAuswahl1M;
import de.meetingapps.meetingportal.meetComEclM.EclBesitzGesamtM;
import de.meetingapps.meetingportal.meetComEclM.EclDbM;
import de.meetingapps.meetingportal.meetComEclM.EclLoginDatenM;
import de.meetingapps.meetingportal.meetComEclM.EclParamM;
import de.meetingapps.meetingportal.meetComEntities.EclAbstimmungku310;
import de.meetingapps.meetingportal.meetComEntities.EclAktienregister;
import de.meetingapps.meetingportal.meetComEntities.EclLoginDaten;
import de.meetingapps.meetingportal.meetComKonst.KonstPortalView;
import de.meetingapps.meetingportal.meetComWE.WEAbstimmungku310AktionaerLesen;
import de.meetingapps.meetingportal.meetComWE.WEAbstimmungku310AktionaerLesenRC;
import de.meetingapps.meetingportal.meetingportTFunktionen.TFunktionen;
import de.meetingapps.meetingportal.meetingportTFunktionen.TPruefeStartNachOpen;
import de.meetingapps.meetingportal.meetingportTFunktionen.TSessionVerwaltung;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@RequestScoped
@Named
public class TStimmabgabeku310 {

    private int logDrucken=10;
    
    private @Inject EclAbstimmungSetM eclAbstimmungSetM;
    private @Inject BlMAbstimmung blMAbstimmung;
    private @Inject TSessionVerwaltung tSessionVerwaltung;
    private @Inject EclDbM eclDbM;
    private @Inject TFunktionen tFunktionen;
    private @Inject EclBesitzGesamtAuswahl1M eclBesitzGesamtAuswahl1M;
    private @Inject TSession tSession;
    private @Inject TAuswahlSession tAuswahlSession;
    private @Inject TStimmabgabeSession tStimmabgabeSession;
    private @Inject TStimmabgabeku310Session tStimmabgabeku310Session;
    private @Inject EclAbstimmungenListeM eclAbstimmungenListeM;
    private @Inject EclParamM eclParamM;
    private @Inject BlMWillenserklaerungStatusNeuVerarbeiten blWillenserklaerungStatusNeuVerarbeitenM;
    private @Inject EclBesitzGesamtM eclBesitzGesamtM;
    private @Inject TFehlerViewSession tFehlerViewSession;
    private @Inject TPruefeStartNachOpen tPruefeStartNachOpen;
    private @Inject EclLoginDatenM eclLoginDatenM;
    
    /**eclDbM öffnen/schließen in aufrufender Funktion*/
    public int startStimmabgabe() {

        /*Abstimmung aktiv?*/
        if (eclAbstimmungSetM.getAbstimmungSet().aktiverAbstimmungsblockIstElektronischAktiv == false) {
            return CaFehler.afDerzeitKeineAbstimmungEroeffnet;
        }

        tStimmabgabeku310Session.setWarnungBeendenBereitsAngezeigt(false);
        
        /*Abstimmungsliste für Abstimmung vorbereiten - immer Gattung 1!*/
        int[] lGattungen= {1, 0, 0, 0, 0};
        blMAbstimmung.leseAbstimmungsliste(lGattungen);

        /*Abstimmungsliste um Daten der LoginKennung ergänzen*/
        for (int i = 0; i < eclAbstimmungenListeM.getAbstimmungenListeM().size(); i++) {
            EclAbstimmungM lAbstimmungM=eclAbstimmungenListeM.getAbstimmungenListeM().get(i);
            
            int anz=eclDbM.getDbBundle().dbAbstimmungku310.read(eclLoginDatenM.getEclAktienregister().aktienregisterIdent, lAbstimmungM.getIdentWeisungssatz());
            
            List<EclAbstimmungku310> lAbstimmungku310List=new LinkedList<EclAbstimmungku310>();
            if (anz>0) {
                for (int i1=0;i1<anz;i1++) {
                    EclAbstimmungku310 lAbstimmungku310=eclDbM.getDbBundle().dbAbstimmungku310.ergebnisPosition(i1);
                    lAbstimmungku310.belegeEingabeFelder();
                    if (lAbstimmungku310.stimmabgabeDurchgefuehrt==1) {
                        lAbstimmungku310.aktuellAbgestimmt=true;
                    }
                    lAbstimmungku310List.add(lAbstimmungku310);
                }
            }
            
             lAbstimmungM.setAbstimmungku310List(lAbstimmungku310List);
            
        }
         
        return 1;
    }

    public void resetEingabe() {
        for (int i = 0; i < eclAbstimmungenListeM.getAbstimmungenListeM().size(); i++) {
            eclAbstimmungenListeM.getAbstimmungenListeM().get(i).setGewaehlt("");
        }
        for (int i = 0; i < eclAbstimmungenListeM.getGegenantraegeListeM().size(); i++) {
            eclAbstimmungenListeM.getGegenantraegeListeM().get(i).setGewaehlt("");
        }
    }

    public boolean startAusgewaehlte() {

        eclDbM.openAll();
        eclDbM.closeAll();
        if (eclAbstimmungSetM.getAbstimmungSet().aktiverAbstimmungsblockIstElektronischAktiv == false) {
            tFehlerViewSession.setFehlerArt(3);
            tFehlerViewSession.setNextView(tFunktionen.waehleAuswahlNachPraesenzfunktion());
            tSessionVerwaltung.setzeEnde(KonstPortalView.FEHLER_VIEW);
            return false;

        }

        if (eclAbstimmungSetM.getVersionAbstimmungenAktuell() != eclAbstimmungSetM.getVersionAbstimmungenStart()) {
            tFehlerViewSession.setFehlerArt(4);
            tFehlerViewSession.setNextView(tFunktionen.waehleAuswahlNachPraesenzfunktion());
            tSessionVerwaltung.setzeEnde(KonstPortalView.FEHLER_VIEW);
            return false;

        }

        tStimmabgabeSession.setDurchfuehrenFuerAlle(false);
        blWillenserklaerungStatusNeuVerarbeitenM.setBesitzJeKennungListe(eclBesitzGesamtM.getBesitzJeKennungListePraesent());
        blWillenserklaerungStatusNeuVerarbeitenM.pruefeAufAusgewaehltUndBelegeAusgewaehltListe(tStimmabgabeSession.isDurchfuehrenFuerAlle());
        if (blWillenserklaerungStatusNeuVerarbeitenM.isRcAusgewaehlteVorhanden() == false) {
            tSession.trageFehlerEin(CaFehler.afBestandFuerStimmabgabeAuswaehlen);
            return false;
        }
        resetEingabe();
        return true;
    }

    public boolean startAlle() {

        eclDbM.openAll();
        eclDbM.closeAll();
        if (eclAbstimmungSetM.getAbstimmungSet().aktiverAbstimmungsblockIstElektronischAktiv == false) {
            tFehlerViewSession.setFehlerArt(3);
            tFehlerViewSession.setNextView(tFunktionen.waehleAuswahlNachPraesenzfunktion());
            tSessionVerwaltung.setzeEnde(KonstPortalView.FEHLER_VIEW);
            return false;

        }

        if (eclAbstimmungSetM.getVersionAbstimmungenAktuell() != eclAbstimmungSetM.getVersionAbstimmungenStart()) {
            tFehlerViewSession.setFehlerArt(4);
            tFehlerViewSession.setNextView(tFunktionen.waehleAuswahlNachPraesenzfunktion());
            tSessionVerwaltung.setzeEnde(KonstPortalView.FEHLER_VIEW);
            return false;

        }

        tStimmabgabeSession.setDurchfuehrenFuerAlle(true);
        blWillenserklaerungStatusNeuVerarbeitenM.setBesitzJeKennungListe(eclBesitzGesamtM.getBesitzJeKennungListePraesent());
        blWillenserklaerungStatusNeuVerarbeitenM.pruefeAufAusgewaehltUndBelegeAusgewaehltListe(tStimmabgabeSession.isDurchfuehrenFuerAlle());
        resetEingabe();
        return true;
    }

    /*******************************Buttons**************************************************/
    /*++++++++++iStimmabgabe++++++++++++*/

    public void doZurAbstimmung() {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.AUSWAHL)) {
            return;
        }

        eclDbM.openAll();
        eclDbM.openWeitere();

        if (!tPruefeStartNachOpen.pruefeStartNachOpen()) {
            tSessionVerwaltung.setzeEnde();
            eclDbM.closeAll();
            return;
        }

        int rc = startStimmabgabe();

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

        tSessionVerwaltung.setzeEnde(KonstPortalView.STIMMABGABE_ku310);
        return;

    }

    public void doRechterButton(EclAbstimmungku310 pAbstimmungku310){
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.STIMMABGABE_ku310)) {
            return;
        }
        
        eclDbM.openAll();
        eclDbM.openWeitere();
        
        if (!tPruefeStartNachOpen.pruefeStartNachOpen()) {
            tSessionVerwaltung.setzeEnde();
            eclDbM.closeAll();
            return;
        }

        
        if (eclAbstimmungSetM.getAbstimmungSet().aktiverAbstimmungsblockIstElektronischAktiv == false) {
            eclDbM.closeAll();
            tSession.trageFehlerEinMitArt("Dieser Abstimmungsvorgang wurde mittlerweile beendet", 1); 
            tSessionVerwaltung.setzeEnde(KonstPortalView.AUSWAHL);
            return;

        }

        if (eclAbstimmungSetM.getVersionAbstimmungenAktuell() != eclAbstimmungSetM.getVersionAbstimmungenStart()) {
            eclDbM.closeAll();
            tSession.trageFehlerEinMitArt("Dieser Abstimmungsvorgang wurde mittlerweile beendet", 1); 
            tSessionVerwaltung.setzeEnde(KonstPortalView.AUSWAHL);
            return;

        }

        
        String hStimmen="";
        hStimmen=gepruefterStimmenwert(pAbstimmungku310.jaStimmenEingabe);
        if (hStimmen.equals("-1")) {
           eclDbM.closeAll();
           tSession.trageFehlerEinMitArt("Bitte geben Sie eine gültige Zahl bei den JA-Stimmen ein", 1); 
           tSessionVerwaltung.setzeEnde(KonstPortalView.STIMMABGABE_ku310);
           return;
        }
        long lJa=Long.parseLong(hStimmen);
        pAbstimmungku310.jaStimmenEingabe=hStimmen;
        
        hStimmen=gepruefterStimmenwert(pAbstimmungku310.neinStimmenEingabe);
        if (hStimmen.equals("-1")) {
            eclDbM.closeAll();
            tSession.trageFehlerEinMitArt("Bitte geben Sie eine gültige Zahl bei den NEIN-Stimmen ein", 1); 
            tSessionVerwaltung.setzeEnde(KonstPortalView.STIMMABGABE_ku310);
            return;
         }
        long lNein=Long.parseLong(hStimmen);
        pAbstimmungku310.neinStimmenEingabe=hStimmen;
        
        hStimmen=gepruefterStimmenwert(pAbstimmungku310.enthaltungStimmenEingabe);
        if (hStimmen.equals("-1")) {
            eclDbM.closeAll();
           tSession.trageFehlerEinMitArt("Bitte geben Sie eine gültige Zahl bei den Enthaltungen ein", 1); 
            tSessionVerwaltung.setzeEnde(KonstPortalView.STIMMABGABE_ku310);
             return;
         }
        long lEnthaltung=Long.parseLong(hStimmen);
       
        long gesamtStimmenVerteilt=lJa+lNein+lEnthaltung;
        if (gesamtStimmenVerteilt>pAbstimmungku310.gesamtStimmen) {
            eclDbM.closeAll();
            tSession.trageFehlerEinMitArt("Sie haben zu viele Stimmen verteilt - Sie haben "+Long.toString(gesamtStimmenVerteilt)+
                    " verteilt, haben jedoch nur "+Long.toString(pAbstimmungku310.gesamtStimmen)+" zur Verfügung. Bitte reduzieren Sie Ihre Stimmen.", 1); 
            tSessionVerwaltung.setzeEnde(KonstPortalView.STIMMABGABE_ku310);
            return;
        }
        lEnthaltung=pAbstimmungku310.gesamtStimmen-lJa-lNein;
        pAbstimmungku310.enthaltungStimmenEingabe=Long.toString(lEnthaltung);
        
       
        pAbstimmungku310.jaStimmen=lJa;
        pAbstimmungku310.neinStimmen=lNein;
        pAbstimmungku310.enthaltungStimmen=lEnthaltung;

        if (pAbstimmungku310.aktuellAbgestimmt==false && pAbstimmungku310.aktuellBestaetigen==false) {
            pAbstimmungku310.aktuellBestaetigen=true;
        }
        else {
            pAbstimmungku310.stimmabgabeDurchgefuehrt=1;
            pAbstimmungku310.aktuellAbgestimmt=true;
            pAbstimmungku310.aktuellBestaetigen=false;

            int rc=eclDbM.getDbBundle().dbAbstimmungku310.update(pAbstimmungku310);
            if (rc!=1) {
                CaBug.drucke("001");
            }
        }
        
        eclDbM.closeAll();
        
        tSessionVerwaltung.setzeEnde(KonstPortalView.STIMMABGABE_ku310);
        return;

    }
    
    private String gepruefterStimmenwert(String pStimmenwert) {
        String geprStimmenwert=pStimmenwert;
        if (geprStimmenwert==null || geprStimmenwert.isEmpty()) {
            geprStimmenwert="0";
        }
        geprStimmenwert=geprStimmenwert.replace(".", "");
        geprStimmenwert=geprStimmenwert.replace(",", "");
        geprStimmenwert=geprStimmenwert.replace("-", "");
        geprStimmenwert=geprStimmenwert.trim();
        
        CaBug.druckeLog(geprStimmenwert, logDrucken, 10);
        
        if (geprStimmenwert.isEmpty()) {return "-1";}
        if (CaString.isNummern(geprStimmenwert)==false) {
            return "-1";
        }
        long hStimmen=Long.parseLong(geprStimmenwert);
        geprStimmenwert=Long.toString(hStimmen);
        return geprStimmenwert;
    }
    
    public void doLinkerButton(EclAbstimmungku310 pAbstimmungku310){
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.STIMMABGABE_ku310)) {
            return;
        }
        
        eclDbM.openAll();
        
        if (!tPruefeStartNachOpen.pruefeStartNachOpen()) {
            tSessionVerwaltung.setzeEnde();
            eclDbM.closeAll();
            return;
        }

        
        eclDbM.closeAll();
        
        if (eclAbstimmungSetM.getAbstimmungSet().aktiverAbstimmungsblockIstElektronischAktiv == false) {
            tSession.trageFehlerEinMitArt("Dieser Abstimmungsvorgang wurde mittlerweile beendet", 1); 
            tSessionVerwaltung.setzeEnde(KonstPortalView.AUSWAHL);
            return;

        }

        if (eclAbstimmungSetM.getVersionAbstimmungenAktuell() != eclAbstimmungSetM.getVersionAbstimmungenStart()) {
            tSession.trageFehlerEinMitArt("Dieser Abstimmungsvorgang wurde mittlerweile beendet", 1); 
            tSessionVerwaltung.setzeEnde(KonstPortalView.AUSWAHL);
            return;

        }

        
        
        pAbstimmungku310.aktuellBestaetigen=false;
        
        tSessionVerwaltung.setzeEnde(KonstPortalView.STIMMABGABE_ku310);
        return;

    }

    
    public void doAllesJaButton(EclAbstimmungku310 pAbstimmungku310){
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.STIMMABGABE_ku310)) {
            return;
        }
        
        pAbstimmungku310.jaStimmenEingabe=Long.toString(pAbstimmungku310.gesamtStimmen);
        pAbstimmungku310.neinStimmenEingabe="";
        pAbstimmungku310.enthaltungStimmenEingabe="";
         
        tSessionVerwaltung.setzeEnde(KonstPortalView.STIMMABGABE_ku310);
        return;

    }

    public void doAllesNeinButton(EclAbstimmungku310 pAbstimmungku310){
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.STIMMABGABE_ku310)) {
            return;
        }
        
        pAbstimmungku310.neinStimmenEingabe=Long.toString(pAbstimmungku310.gesamtStimmen);
        pAbstimmungku310.jaStimmenEingabe="";
        pAbstimmungku310.enthaltungStimmenEingabe="";
         
        tSessionVerwaltung.setzeEnde(KonstPortalView.STIMMABGABE_ku310);
        return;

    }

    public void doAllesEnthaltungButton(EclAbstimmungku310 pAbstimmungku310){
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.STIMMABGABE_ku310)) {
            return;
        }
        
        pAbstimmungku310.enthaltungStimmenEingabe=Long.toString(pAbstimmungku310.gesamtStimmen);
        pAbstimmungku310.neinStimmenEingabe="";
        pAbstimmungku310.jaStimmenEingabe="";
         
        tSessionVerwaltung.setzeEnde(KonstPortalView.STIMMABGABE_ku310);
        return;

    }
  
    
    public void doWeiter() {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.STIMMABGABE_ku310)) {
            return;
        }

        eclDbM.openAll();
        if (!tPruefeStartNachOpen.pruefeStartNachOpen()) {
            tSessionVerwaltung.setzeEnde();
            eclDbM.closeAll();
            return;
        }

        eclDbM.closeAll();
        if (eclAbstimmungSetM.getAbstimmungSet().aktiverAbstimmungsblockIstElektronischAktiv == false) {
            tSessionVerwaltung.setzeEnde(KonstPortalView.AUSWAHL);
            return;

        }

        if (eclAbstimmungSetM.getVersionAbstimmungenAktuell() != eclAbstimmungSetM.getVersionAbstimmungenStart()) {
            tSessionVerwaltung.setzeEnde(KonstPortalView.AUSWAHL);
            return;

        }
        
         
        if (tStimmabgabeku310Session.isWarnungBeendenBereitsAngezeigt()==false) {
            /*Prüfen, ob alle Stimmabgaben durchgeführt*/
            boolean nichtAbgegeben=false;
            for (int i = 0; i < eclAbstimmungenListeM.getAbstimmungenListeM().size(); i++) {
                EclAbstimmungM lAbstimmungM=eclAbstimmungenListeM.getAbstimmungenListeM().get(i);
                
                List<EclAbstimmungku310> abstimmungku310List=lAbstimmungM.getAbstimmungku310List();
                if (abstimmungku310List!=null) {
                    for (int i1=0;i1<abstimmungku310List.size();i1++) {
                        if (abstimmungku310List.get(i1).aktuellAbgestimmt==false) {
                            nichtAbgegeben=true; 
                        }
                    }
                }
               
            }
            if (nichtAbgegeben==true) {
                tStimmabgabeku310Session.setWarnungBeendenBereitsAngezeigt(true);
                tSession.trageFehlerEinMitArt("Sie haben noch nicht alle Stimmabgaben durchgeführt. Bitte überprüfen Sie Ihre Stimmabgaben, bevor Sie die Abstimmung beenden", 1); 
                tSessionVerwaltung.setzeEnde(KonstPortalView.STIMMABGABE_ku310);
                return;
            }
        }
        
        tSessionVerwaltung.setzeEnde(KonstPortalView.AUSWAHL);
        return;

    }

    /**********************************Für Web-Service für Tablet-Backup********************/
    public boolean abstimmungku310AktionaerLesen(WEAbstimmungku310AktionaerLesen weAbstimmungku310AktionaerLesen, WEAbstimmungku310AktionaerLesenRC weAbstimmungku310AktionaerLesenRC) {
        
        CaBug.druckeLog("Lesen="+weAbstimmungku310AktionaerLesen.aktionaersNummer, logDrucken, 10);
        
        /*Aktionär einlesen*/
        DbBundle lDbBundle=eclDbM.getDbBundle();
        
        String hEKNr=CaString.fuelleLinksNull(weAbstimmungku310AktionaerLesen.aktionaersNummer, 5);
        int rc=lDbBundle.dbMeldungen.leseEkNummer(hEKNr);
        if (rc<1) {
            weAbstimmungku310AktionaerLesenRC.rc=-2;
            return false;
        }
        String hAktionaersnummer=lDbBundle.dbMeldungen.meldungenArray[0].aktionaersnummer;
        CaBug.druckeLog("hAktionaersnummer="+hAktionaersnummer, logDrucken, 10);
        
        
        lDbBundle.dbAktienregister.leseZuAktienregisternummer(hAktionaersnummer);
        if (lDbBundle.dbAktienregister.anzErgebnis()==0) {
            weAbstimmungku310AktionaerLesenRC.rc=-2;
            return false;
        }
        
        EclAktienregister lAktienregister=lDbBundle.dbAktienregister.ergebnisPosition(0);

        
        weAbstimmungku310AktionaerLesenRC.aktienregister=lAktienregister;
        
        /*Überprüfen, ob Präsent (= login nicht mehr gesperrt)*/
        
        lDbBundle.dbLoginDaten.read_loginKennung(hAktionaersnummer);
        if (lDbBundle.dbLoginDaten.anzErgebnis()==0) {
            weAbstimmungku310AktionaerLesenRC.rc=-2;
            return false;
        }
        
        EclLoginDaten lLoginDaten=lDbBundle.dbLoginDaten.ergebnisPosition(0);
        if (lLoginDaten.anmeldenUnzulaessig==1) {
            weAbstimmungku310AktionaerLesenRC.rc=-4;
            return false;
        }
        
        eclLoginDatenM.setEclAktienregister(lAktienregister);
        
        rc=startStimmabgabe();
        weAbstimmungku310AktionaerLesenRC.rcFachlich=rc;
        
        /*Restliche Felder aus Beans füllen*/
        weAbstimmungku310AktionaerLesenRC.versionAbstimmungenStart=eclAbstimmungSetM.getVersionAbstimmungenStart();
        weAbstimmungku310AktionaerLesenRC.versionWeisungenStart=eclAbstimmungSetM.getVersionWeisungenStart();
        
        weAbstimmungku310AktionaerLesenRC.versionAbstimmungenAktuell=eclAbstimmungSetM.getVersionAbstimmungenAktuell();
        weAbstimmungku310AktionaerLesenRC.versionWeisungenAktuell=eclAbstimmungSetM.getVersionWeisungenAktuell();
        weAbstimmungku310AktionaerLesenRC.versionAbstimmungenWeisungenOhneAbbruchAktuell=eclAbstimmungSetM.getVersionAbstimmungenWeisungenOhneAbbruchAktuell();

        weAbstimmungku310AktionaerLesenRC.abstimmungSet=eclAbstimmungSetM.getAbstimmungSet();

        
        weAbstimmungku310AktionaerLesenRC.abstimmungenListeM=eclAbstimmungenListeM.getAbstimmungenListeM();
        weAbstimmungku310AktionaerLesenRC.gegenantraegeListeM=eclAbstimmungenListeM.getGegenantraegeListeM();
       
        weAbstimmungku310AktionaerLesenRC.gegenantraegeVorhanden=eclAbstimmungenListeM.isGegenantraegeVorhanden();
        
        weAbstimmungku310AktionaerLesenRC.alternative=eclAbstimmungenListeM.getAlternative();
        
        weAbstimmungku310AktionaerLesenRC.weisungMeldung=eclAbstimmungenListeM.getWeisungMeldung();
        weAbstimmungku310AktionaerLesenRC.weisungMeldungRaw=eclAbstimmungenListeM.getWeisungMeldungRaw();

        
        weAbstimmungku310AktionaerLesenRC.warnungBeendenBereitsAngezeigt=tStimmabgabeku310Session.isWarnungBeendenBereitsAngezeigt();

        
        return true;
    }


    public boolean abstimmungku310StimmeAbgeben(EclAbstimmungku310 pAbstimmungku310) {
        pAbstimmungku310.stimmabgabeDurchgefuehrt=1;
        pAbstimmungku310.aktuellAbgestimmt=true;
        pAbstimmungku310.aktuellBestaetigen=false;

        if (pAbstimmungku310.jaStimmenEingabe.isEmpty()) {
            pAbstimmungku310.jaStimmenEingabe="0";
        }
        pAbstimmungku310.jaStimmen=Long.parseLong(pAbstimmungku310.jaStimmenEingabe);
        
        if (pAbstimmungku310.neinStimmenEingabe.isEmpty()) {
            pAbstimmungku310.neinStimmenEingabe="0";
        }
         pAbstimmungku310.neinStimmen=Long.parseLong(pAbstimmungku310.neinStimmenEingabe);
        
         if (pAbstimmungku310.enthaltungStimmenEingabe.isEmpty()) {
             pAbstimmungku310.enthaltungStimmenEingabe="0";
         }
          pAbstimmungku310.enthaltungStimmen=Long.parseLong(pAbstimmungku310.enthaltungStimmenEingabe);
       
        
        int rc=eclDbM.getDbBundle().dbAbstimmungku310.update(pAbstimmungku310);
        if (rc!=1) {
            CaBug.drucke("001");
            return false;
        }
        return true;
        
    }
    
}
