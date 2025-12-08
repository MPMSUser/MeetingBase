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

import java.util.LinkedList;
import java.util.List;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaDatumZeit;
import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComAllg.CaPasswortErzeugen;
import de.meetingapps.meetingportal.meetComAllg.CaString;
import de.meetingapps.meetingportal.meetComBa.BaMailM;
import de.meetingapps.meetingportal.meetComBl.BlEKFreiwilligesAnmelden;
import de.meetingapps.meetingportal.meetComBl.BlGastkarte;
import de.meetingapps.meetingportal.meetComBl.BlNummernformBasis;
import de.meetingapps.meetingportal.meetComBl.BlSuchen;
import de.meetingapps.meetingportal.meetComBl.BlTeilnehmerLoginNeu;
import de.meetingapps.meetingportal.meetComBl.BlTeilnehmerLoginSperre;
import de.meetingapps.meetingportal.meetComBl.BlVeranstaltungen;
import de.meetingapps.meetingportal.meetComBrM.BrMARRemoteZuARLokal;
import de.meetingapps.meetingportal.meetComBrM.BrMAktionaersdaten;
import de.meetingapps.meetingportal.meetComBrM.BrMInit;
import de.meetingapps.meetingportal.meetComEclM.EclAufgabenM;
import de.meetingapps.meetingportal.meetComEclM.EclBesitzGesamtAuswahl1M;
import de.meetingapps.meetingportal.meetComEclM.EclDbM;
import de.meetingapps.meetingportal.meetComEclM.EclLoginDatenM;
import de.meetingapps.meetingportal.meetComEclM.EclParamM;
import de.meetingapps.meetingportal.meetComEclM.EclPortalTexteM;
import de.meetingapps.meetingportal.meetComEclM.EclUserLoginM;
import de.meetingapps.meetingportal.meetComEclM.EclVeranstaltungM;
import de.meetingapps.meetingportal.meetComEclM.EclVeranstaltungenM;
import de.meetingapps.meetingportal.meetComEntities.EclAktienregister;
import de.meetingapps.meetingportal.meetComEntities.EclAktienregisterErgaenzung;
import de.meetingapps.meetingportal.meetComEntities.EclAktienregisterWeiterePerson;
import de.meetingapps.meetingportal.meetComEntities.EclAufgaben;
import de.meetingapps.meetingportal.meetComEntities.EclLoginDaten;
import de.meetingapps.meetingportal.meetComEntities.EclMeldung;
import de.meetingapps.meetingportal.meetComEntities.EclPersonenNatJur;
import de.meetingapps.meetingportal.meetComEntities.EclSuchergebnis;
import de.meetingapps.meetingportal.meetComEntities.EclVeranstaltungenVeranstaltung;
import de.meetingapps.meetingportal.meetComKonst.KonstAktienregisterErgaenzung;
import de.meetingapps.meetingportal.meetComKonst.KonstAufgaben;
import de.meetingapps.meetingportal.meetComKonst.KonstAufgabenAnforderer;
import de.meetingapps.meetingportal.meetComKonst.KonstAufgabenStatus;
import de.meetingapps.meetingportal.meetComKonst.KonstGruppen;
import de.meetingapps.meetingportal.meetComKonst.KonstPortalTexte;
import de.meetingapps.meetingportal.meetComKonst.KonstSuchlaufSuchbegriffArt;
import de.meetingapps.meetingportal.meetingCoreReport.RpBrowserAnzeigen;
import de.meetingapps.meetingportal.meetingportTController.PAktionaersdatenSession;
import de.meetingapps.meetingportal.meetingportTController.TDialogveranstaltungen;
import de.meetingapps.meetingportal.meetingportTController.TDialogveranstaltungenSession;
import de.meetingapps.meetingportal.meetingportTController.TGeneralversammlung;
import de.meetingapps.meetingportal.meetingportTFunktionen.TFunktionen;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

/**Bereiche:
 * 
 * 
 * Mandantenwechseln (gehe zu UControllerMenueMandant)
 */

@RequestScoped
@Named
public class UServicelineAnfrage {

    private int logDrucken = 10;

    @Inject
    private EclDbM eclDbM;
    private @Inject USession uSession;
    @Inject
    private XSessionVerwaltung xSessionVerwaltung;
    @Inject
    private EclParamM eclParamM;

    @Inject
    private TDialogveranstaltungen tDialogveranstaltungen;
    
    private @Inject UBerechtigungenSession uBerechtigungenSession;

    @Inject
    private EclUserLoginM eclUserLoginM;

    @Inject
    private EclPortalTexteM eclTextePortalM;
    @Inject
    private BaMailM baMailM;

    @Inject
    private UServicelineAnfrageSession uServicelineAnfrageSession;
    @Inject
    private TDialogveranstaltungenSession tDialogveranstaltungenSession;

    private @Inject BrMInit brMInit;
    private @Inject BrMAktionaersdaten brMAktionaersdaten;
    private @Inject PAktionaersdatenSession pAktionaersdatenSession;
    private @Inject BrMARRemoteZuARLokal brMARRemoteZuARLokal;
    private @Inject EclLoginDatenM eclLoginDatenM;
    private @Inject TFunktionen tFunktionen;
    private @Inject TGeneralversammlung tGeneralversammlung;
    private @Inject EclBesitzGesamtAuswahl1M eclBesitzGesamtAuswahl1M;
    private @Inject UGastkarte uGastkarte; 
    private @Inject EclVeranstaltungenM eclVeranstaltungenM;
    private @Inject EclPortalTexteM eclPortalTexteM;
    
    /**Initialisieren - vor Aufruf von UTOWeisungsEmpfehlung
     * 
     * Open/Close wird im aufrufenden Modul geregelt.*/
    public void init() {

        uServicelineAnfrageSession.init();
    }

    /**Einlesen der Aktionärsnummer*/
    public String doEinlesen() {
        try {
            if (eclUserLoginM.pruefe_uportal_servicelinetools() == false) {
                return "";
            }
            if (!xSessionVerwaltung.pruefeUStart("uServicelineAnfrage", "")) {
                xSessionVerwaltung.setzeUEnde();
                return "uDlgFehler";
            }

            eclDbM.openAll();
            eclDbM.openWeitere();
            boolean brc = datenEinlesen();
            if (brc == false) {
                eclDbM.closeAll();
                return xSessionVerwaltung.setzeUEnde();
            }

            eclDbM.closeAll();


        } catch (Exception e) {
            CaBug.drucke("Exception");
            System.out.println(e.getMessage());
            e.printStackTrace();
            eclDbM.closeAll();
        }


        return xSessionVerwaltung.setzeUEnde("uServicelineAnfrage", true, false, eclUserLoginM.getKennung());
    }

    private boolean pruefenSucheName() {
        if (uServicelineAnfrageSession.getAktionaersName().trim().length()<2) {
            uSession.setFehlermeldung("Suchbegriff muß mindestens 2 Zeichen lang sein");
            return false;
        }
        return true;
    }
    
    private final int KONST_SUCHERGEBNISART_REGISTER=1;
    private final int KONST_SUCHERGEBNISART_GAST=2;
    private final int KONST_SUCHERGEBNISART_VERTRETER=3;
    private final int KONST_SUCHERGEBNISART_UNGEPRUEFTE_VERTRETER=4;
    
    public String doEinlesenRegister() {
        if (eclUserLoginM.pruefe_uportal_servicelinetools() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uServicelineAnfrage", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }
        
        if (pruefenSucheName()==false) {
            return xSessionVerwaltung.setzeUEnde();
        }
        
        eclDbM.openAll();
        eclDbM.openWeitere();

        BlSuchen blSuchen=new BlSuchen(true, eclDbM.getDbBundle());
        blSuchen.sucheAusfuehren(KonstSuchlaufSuchbegriffArt.nameAktionaer, uServicelineAnfrageSession.getAktionaersName(), false, true, false, false);
        int anz=0;
        if (blSuchen.rcAktienregister!=null) {anz=blSuchen.rcAktienregister.length;}
        List<EclSuchergebnis> lSuchergebnisListe=new LinkedList<EclSuchergebnis>();
        for (int i=0;i<anz;i++) {
            EclAktienregister lAktienregister=blSuchen.rcAktienregister[i];
            EclSuchergebnis lSuchergebnis=new EclSuchergebnis(); 
            lSuchergebnis.aktienregisterIdent=lAktienregister.aktienregisterIdent;
            lSuchergebnis.aktionaersnummer=BlNummernformBasis.aufbereitenInternFuerExtern(lAktienregister.aktionaersnummer, eclDbM.getDbBundle());
            lSuchergebnis.nameKomplett=lAktienregister.nameKomplett;
            lSuchergebnis.aktionaerOrt=lAktienregister.ort;
           
            lSuchergebnisListe.add(lSuchergebnis);
        }
        uServicelineAnfrageSession.setSuchergebnisListe(lSuchergebnisListe);
        uServicelineAnfrageSession.setSuchergebnisArt(KONST_SUCHERGEBNISART_REGISTER);
        uServicelineAnfrageSession.setSuchergebnisseWerdenAngezeigt(true);
        eclDbM.closeAll();
        
        return xSessionVerwaltung.setzeUEnde("uServicelineAnfrage", true, false, eclUserLoginM.getKennung());
    }
    
    public String doEinlesenGast() {
        if (eclUserLoginM.pruefe_uportal_servicelinetools() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uServicelineAnfrage", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }
        
        if (pruefenSucheName()==false) {
            return xSessionVerwaltung.setzeUEnde();
        }
        
        eclDbM.openAll();
        eclDbM.openWeitere();

        BlSuchen blSuchen=new BlSuchen(true, eclDbM.getDbBundle());
        blSuchen.sucheAusfuehren(KonstSuchlaufSuchbegriffArt.nameAktionaerOderAktuellerVertreter, uServicelineAnfrageSession.getAktionaersName(), false, false, true, true);
        int anz=0;
        if (blSuchen.rcMeldungen!=null) {anz=blSuchen.rcMeldungen.length;}
        List<EclSuchergebnis> lSuchergebnisListe=new LinkedList<EclSuchergebnis>();
        for (int i=0;i<anz;i++) {
            EclMeldung lMeldung=blSuchen.rcMeldungen[i];
            EclSuchergebnis lSuchergebnis=new EclSuchergebnis(); 
            lSuchergebnis.meldungsIdent=lMeldung.meldungsIdent;
            lSuchergebnis.aktionaerName=lMeldung.name;
            lSuchergebnis.aktionaerVorname=lMeldung.vorname;
            lSuchergebnis.aktionaerOrt=lMeldung.ort;
            
            lSuchergebnis.aktionaersnummer=lMeldung.loginKennung;
            
            lSuchergebnisListe.add(lSuchergebnis);
        }
        uServicelineAnfrageSession.setSuchergebnisListe(lSuchergebnisListe);
        uServicelineAnfrageSession.setSuchergebnisArt(KONST_SUCHERGEBNISART_GAST);
        uServicelineAnfrageSession.setSuchergebnisseWerdenAngezeigt(true);
        eclDbM.closeAll();
        
        return xSessionVerwaltung.setzeUEnde("uServicelineAnfrage", true, false, eclUserLoginM.getKennung());
    }
    
    public String doEinlesenVertreter() {
        if (eclUserLoginM.pruefe_uportal_servicelinetools() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uServicelineAnfrage", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }
        
        if (pruefenSucheName()==false) {
            return xSessionVerwaltung.setzeUEnde();
        }
        
        eclDbM.openAll();
        eclDbM.openWeitere();

        BlSuchen blSuchen=new BlSuchen(true, eclDbM.getDbBundle());
        blSuchen.sucheAusfuehren(KonstSuchlaufSuchbegriffArt.nameVertreter, uServicelineAnfrageSession.getAktionaersName(), true, true, false, false);
        int anz=0;
        if (blSuchen.rcMeldungen!=null) {anz=blSuchen.rcMeldungen.length;}
        List<EclSuchergebnis> lSuchergebnisListe=new LinkedList<EclSuchergebnis>();
        for (int i=0;i<anz;i++) {
            EclAktienregister lAktienregister=blSuchen.rcAktienregister[i];
           
            EclSuchergebnis lSuchergebnis=new EclSuchergebnis(); 
            lSuchergebnis.aktienregisterIdent=lAktienregister.aktienregisterIdent;
            lSuchergebnis.aktionaersnummer=BlNummernformBasis.aufbereitenInternFuerExtern(lAktienregister.aktionaersnummer, eclDbM.getDbBundle());
            lSuchergebnis.nameKomplett=lAktienregister.nameKomplett;
            lSuchergebnis.aktionaerOrt=lAktienregister.ort;
            
            lSuchergebnis.vertreterName=blSuchen.rcSonstigeVollmacht[i];
            
            lSuchergebnisListe.add(lSuchergebnis);
        }
        uServicelineAnfrageSession.setSuchergebnisListe(lSuchergebnisListe);
        uServicelineAnfrageSession.setSuchergebnisArt(KONST_SUCHERGEBNISART_VERTRETER);
        uServicelineAnfrageSession.setSuchergebnisseWerdenAngezeigt(true);
        eclDbM.closeAll();
 
         return xSessionVerwaltung.setzeUEnde("uServicelineAnfrage", true, false, eclUserLoginM.getKennung());
    }

    public String doEinlesenUngeprueftenVertreter() {
        if (eclUserLoginM.pruefe_uportal_servicelinetools() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uServicelineAnfrage", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }
        
        if (pruefenSucheName()==false) {
            return xSessionVerwaltung.setzeUEnde();
        }
        
        eclDbM.openAll();
        eclDbM.openWeitere();

        BlSuchen blSuchen=new BlSuchen(true, eclDbM.getDbBundle());
        blSuchen.sucheAusfuehren(KonstSuchlaufSuchbegriffArt.ungepruefteVertreter, uServicelineAnfrageSession.getAktionaersName(), true, true, false, false);
        int anz=0;
        if (blSuchen.rcMeldungen!=null) {anz=blSuchen.rcMeldungen.length;}
        List<EclSuchergebnis> lSuchergebnisListe=new LinkedList<EclSuchergebnis>();
        for (int i=0;i<anz;i++) {
            EclAktienregister lAktienregister=blSuchen.rcAktienregister[i];
            
            EclSuchergebnis lSuchergebnis=new EclSuchergebnis(); 
            lSuchergebnis.aktienregisterIdent=lAktienregister.aktienregisterIdent;
            lSuchergebnis.aktionaersnummer=BlNummernformBasis.aufbereitenInternFuerExtern(lAktienregister.aktionaersnummer, eclDbM.getDbBundle());
            lSuchergebnis.nameKomplett=lAktienregister.nameKomplett;
            lSuchergebnis.aktionaerOrt=lAktienregister.ort;
            
            lSuchergebnis.vertreterName=blSuchen.rcSonstigeVollmacht[i];
            
            lSuchergebnisListe.add(lSuchergebnis);
        }
        uServicelineAnfrageSession.setSuchergebnisListe(lSuchergebnisListe);
        uServicelineAnfrageSession.setSuchergebnisArt(KONST_SUCHERGEBNISART_UNGEPRUEFTE_VERTRETER);
        uServicelineAnfrageSession.setSuchergebnisseWerdenAngezeigt(true);
        eclDbM.closeAll();
 
        return xSessionVerwaltung.setzeUEnde("uServicelineAnfrage", true, false, eclUserLoginM.getKennung());
    }

    public String doSucheZuruecksetzen() {
        if (eclUserLoginM.pruefe_uportal_servicelinetools() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uServicelineAnfrage", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }
        uServicelineAnfrageSession.init();
        return xSessionVerwaltung.setzeUEnde("uServicelineAnfrage", true, false, eclUserLoginM.getKennung());
    }
    
    public String doAuswaehlen(EclSuchergebnis pSuchergebnis) {
        if (eclUserLoginM.pruefe_uportal_servicelinetools() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uServicelineAnfrage", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }

        uServicelineAnfrageSession.setAktionaersNummer(pSuchergebnis.aktionaersnummer);
        
        eclDbM.openAll();
        eclDbM.openWeitere();
        boolean brc = datenEinlesen();
        if (brc == false) {
            eclDbM.closeAll();
            return xSessionVerwaltung.setzeUEnde();
        }

        eclDbM.closeAll();

        return xSessionVerwaltung.setzeUEnde("uServicelineAnfrage", true, false, eclUserLoginM.getKennung());
   }
    
    
    public String doNeueAnfrage() {
        if (eclUserLoginM.pruefe_uportal_servicelinetools() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uServicelineAnfrage", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }
        uServicelineAnfrageSession.setAktionaerWirdAngezeigt(false);
        uServicelineAnfrageSession.init();

        return xSessionVerwaltung.setzeUEnde("uServicelineAnfrage", true, false, eclUserLoginM.getKennung());
    }

    public String doGastkarteAusstellen() {
        if (eclUserLoginM.pruefe_uportal_servicelinetools() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uServicelineAnfrage", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }
        uGastkarte.initOhneGruppe();
        return xSessionVerwaltung.setzeUEnde("uGastkarteNeu", true, false, eclUserLoginM.getKennung());
      
    }
    /**Portalzugang Entsperren*/
    public String doEntsperren() {
        if (eclUserLoginM.pruefe_uportal_servicelinetools() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uServicelineAnfrage", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }
        eclDbM.openAll();
        eclDbM.openWeitere();

        BlTeilnehmerLoginSperre blTGeilnehmerLoginSperre=new BlTeilnehmerLoginSperre(true, eclDbM.getDbBundle());
        blTGeilnehmerLoginSperre.freigebenKennung(uServicelineAnfrageSession.getAktionaersNummerIntern());
        
        datenEinlesen();

        eclDbM.closeAll();

        return xSessionVerwaltung.setzeUEnde("uServicelineAnfrage", true, false, eclUserLoginM.getKennung());
        
    }
    
    /**Portalzugang sperren*/
    public String doSperren() {
        if (eclUserLoginM.pruefe_uportal_servicelinetools() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uServicelineAnfrage", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }
        eclDbM.openAll();
        eclDbM.openWeitere();

        BlTeilnehmerLoginSperre blTeilnehmerLoginSperre=new BlTeilnehmerLoginSperre(true, eclDbM.getDbBundle());
        blTeilnehmerLoginSperre.sperrenKennung(uServicelineAnfrageSession.getAktionaersNummerIntern());
        
        datenEinlesen();

        eclDbM.closeAll();

        return xSessionVerwaltung.setzeUEnde("uServicelineAnfrage", true, false, eclUserLoginM.getKennung());
        
    }
    
    /**Portalzugang sperren*/
    public String doBerechtigungenSpeichern() {
        if (eclUserLoginM.pruefe_uportal_servicelinetools() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uServicelineAnfrage", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }
        eclDbM.openAll();
        eclDbM.openWeitere();

        String lKennung=uServicelineAnfrageSession.getAktionaersNummerIntern();
        
        eclDbM.getDbBundle().dbLoginDaten.read_loginKennung(lKennung);
        EclLoginDaten lLoginDaten=eclDbM.getDbBundle().dbLoginDaten.ergebnisPosition(0);
        lLoginDaten.berechtigungPortal=uBerechtigungenSession.liefereBerechtigungsWert();
        eclDbM.getDbBundle().dbLoginDaten.update(lLoginDaten);

        
        datenEinlesen();

        eclDbM.closeAll();

        return xSessionVerwaltung.setzeUEnde("uServicelineAnfrage", true, false, eclUserLoginM.getKennung());
        
    }

    public String doNeuesPasswortPerPost() {
        if (eclUserLoginM.pruefe_uportal_servicelinetools() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uServicelineAnfrage", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }
        eclDbM.openAll();
        eclDbM.openWeitere();
        
        ausfuehrenPerPost(1);
//        EclAufgaben lAufgabe = new EclAufgaben();
//        lAufgabe.mandant = eclDbM.getDbBundle().clGlobalVar.mandant;
//        lAufgabe.aufgabe = KonstAufgaben.aktionaerNeuesPasswortAdressePruefen;
//        lAufgabe.zeitpunktErteilt = CaDatumZeit.DatumZeitStringFuerDatenbank();
//        lAufgabe.anforderer = KonstAufgabenAnforderer.aktionaerPortal;
//        lAufgabe.status = KonstAufgabenStatus.geprueft;
//        lAufgabe.argument[0] = uServicelineAnfrageSession.getAktionaersNummerIntern();
//        eclDbM.getDbBundle().dbAufgaben.insert(lAufgabe);

        datenEinlesen();

        eclDbM.closeAll();

        return xSessionVerwaltung.setzeUEnde("uServicelineAnfrage", true, false, eclUserLoginM.getKennung());
    }

    public String doRegistrierungPerPost() {
        if (eclUserLoginM.pruefe_uportal_servicelinetools() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uServicelineAnfrage", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }
        eclDbM.openAll();
        eclDbM.openWeitere();
        ausfuehrenPerPost(2);

        datenEinlesen();

        eclDbM.closeAll();

        return xSessionVerwaltung.setzeUEnde("uServicelineAnfrage", true, false, eclUserLoginM.getKennung());
    }

    /**1 oder 2*/
    private void ausfuehrenPerPost(int neuesPasswortOderRegistierung) {
        boolean remoteAnbindung=false;
        if (eclParamM.getRegisterAnbindung()==1) {
           brMInit.init();
           brMAktionaersdaten.holeAktuellenStand(uServicelineAnfrageSession.getAktionaersNummerIntern());
           remoteAnbindung=true;
        }
        if (remoteAnbindung==false || pAktionaersdatenSession.isPostempfaengerVorhanden()==false) {
            EclAufgaben lAufgabe = new EclAufgaben();
            lAufgabe.mandant = eclDbM.getDbBundle().clGlobalVar.mandant;
            lAufgabe.aufgabe = KonstAufgaben.aktionaerNeuesPasswortAdressePruefen;
            lAufgabe.zeitpunktErteilt = CaDatumZeit.DatumZeitStringFuerDatenbank();
            lAufgabe.anforderer = KonstAufgabenAnforderer.aktionaerPortal;
            lAufgabe.status = KonstAufgabenStatus.geprueft;
            lAufgabe.argument[0] = uServicelineAnfrageSession.getAktionaersNummerIntern();
            lAufgabe.argument[1] = "";
            lAufgabe.argument[2] = "";
            if (neuesPasswortOderRegistierung==1) {
                lAufgabe.argument[3] = "";
            }
            else {
                lAufgabe.argument[3] = "2";
            }
            eclDbM.getDbBundle().dbAufgaben.insert(lAufgabe);
        }
        else {
            List<EclAktienregisterWeiterePerson> postempfaenger=pAktionaersdatenSession.getPostempfaenger();
            /*Neues Initial-Passwort - für alle Anschreiben generieren*/
            CaPasswortErzeugen caPasswortErzeugen = new CaPasswortErzeugen();

            // Passwortlänge festlegen (Aktuell bei Namensaktien IMMER auf 8)
            int passwort_laenge = eclParamM.getParam().paramPortal.passwortMindestLaenge;

            /* Initialpasswort generieren (von uns generiert und unleserlich gemacht)
             * d.h.:
             * Passwort wird generiert
             * -> pppppppp
             * -> Passwort wird vorne und hinten mit einem String ergänzt, damit es zumindest nicht komplett plain in der DB steht.
             * -> ssssssssppppppppssssssss 
             * 
             * Übergeben wird:
             * 1. Passwortlänge hier: 8
             * 2. Anzahl der Sonderzeichen hier: 2
             * 3. Anzahl der Zahlen hier: 2
             * 4. Großbuchstaben true/false hier: true
             */
            String passwort_initial = caPasswortErzeugen.generatePWInitial(passwort_laenge, 1, 1, true);

//            // Initialpasswort (nur pppppppp) verschlüsseln -> SHA256 String
//            String passwort_verschluesselt = CaPasswortVerschluesseln.verschluesselnAusInitialPW(passwort_initial,
//                    passwort_laenge);
            
            for (EclAktienregisterWeiterePerson iEclAktienregisterWeiterePerson:postempfaenger) {

                EclAufgaben lAufgabe = new EclAufgaben();
                lAufgabe.mandant = eclDbM.getDbBundle().clGlobalVar.mandant;
                lAufgabe.aufgabe = KonstAufgaben.aktionaerNeuesPasswortAdressePruefen;
                lAufgabe.zeitpunktErteilt = CaDatumZeit.DatumZeitStringFuerDatenbank();
                lAufgabe.anforderer = KonstAufgabenAnforderer.aktionaerPortal;
                lAufgabe.status = KonstAufgabenStatus.geprueft;
                lAufgabe.argument[0] = uServicelineAnfrageSession.getAktionaersNummerIntern();
                lAufgabe.argument[1] = "";
                lAufgabe.argument[2] = "";
                if (neuesPasswortOderRegistierung==1) {
                    lAufgabe.argument[3] = "";
                }
                else {
                    lAufgabe.argument[3] = "2";
                }

                lAufgabe.argument[4] = iEclAktienregisterWeiterePerson.liefereNameKomplett();
                lAufgabe.argument[5] = iEclAktienregisterWeiterePerson.strasse;
                lAufgabe.argument[6] = iEclAktienregisterWeiterePerson.plz+" "+iEclAktienregisterWeiterePerson.ort;
                if (iEclAktienregisterWeiterePerson.land.equals("Deutschland") || iEclAktienregisterWeiterePerson.land.isEmpty()) {
                    lAufgabe.argument[7] = "";
                }
                else {
                    lAufgabe.argument[7]=iEclAktienregisterWeiterePerson.land.toUpperCase();
                }

                lAufgabe.argument[8]=passwort_initial;
                eclDbM.getDbBundle().dbAufgaben.insert(lAufgabe);

            }

        }
        
    }
    public String doNeuesPasswortPerMail() {
        /*TODO Achtung, hier wurden versehentlich Textnummern doppelt verwendet.
         * Alte Nummern=1029, 1030, 1031. Wurden auch für Mail Zugangsdaten an
         * Vollmacht an Dritte verwendet. Deshalb hier jetzt neue Nummern.
         * d.h. aber umgekehrt, dass im Standardportal hier noch die  Textpflege
         * nicht beendet ist!
         */
        eclDbM.openAll();
        BlGastkarte blGastkarte = new BlGastkarte(eclDbM.getDbBundle());
        blGastkarte.neuesPasswortGenerieren(uServicelineAnfrageSession.getAktionaersNummerIntern(), false, 4);

        eclDbM.closeAll();
        /*Mail für Passwort*/
        String mailBetreff = eclTextePortalM.holeText("1757");
        String mailText = eclTextePortalM.holeText("1758") + blGastkarte.rcPasswortFuerMail
                + eclTextePortalM.holeText("1759");
        baMailM.senden(blGastkarte.rcMailEmpfaenger, mailBetreff, mailText);

        uSession.setFehlermeldung("Mail verschickt");

        return "";
    }

    public String doNeuesZugangsschreibenPerPost() {
        if (eclUserLoginM.pruefe_uportal_servicelinetools() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uServicelineAnfrage", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }
        eclDbM.openAll();
        eclDbM.openWeitere();
        EclAufgaben lAufgabe = new EclAufgaben();
        lAufgabe.mandant = eclDbM.getDbBundle().clGlobalVar.mandant;
        lAufgabe.aufgabe = KonstAufgaben.aktionaerNeuesPasswortAdressePruefen;
        lAufgabe.zeitpunktErteilt = CaDatumZeit.DatumZeitStringFuerDatenbank();
        lAufgabe.anforderer = KonstAufgabenAnforderer.aktionaerPortal;
        lAufgabe.status = KonstAufgabenStatus.geprueft;
        lAufgabe.argument[0] = uServicelineAnfrageSession.getAktionaersNummerIntern();
        eclDbM.getDbBundle().dbAufgaben.insert(lAufgabe);

        datenEinlesen();

        eclDbM.closeAll();

        return xSessionVerwaltung.setzeUEnde("uServicelineAnfrage", true, false, eclUserLoginM.getKennung());
    }

    public String doNeuesPasswortAnNichtHinterlegteEmail() {
        if (eclUserLoginM.pruefe_uportal_servicelinetools() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uServicelineAnfrage", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }
        uServicelineAnfrageSession.setPasswortPerMailDialogAnzeigen(true);
        return xSessionVerwaltung.setzeUEnde("uServicelineAnfrage", true, false, eclUserLoginM.getKennung());
    }
    
    public String doNeuesPasswortAnNichtHinterlegteEmailAusfuehren() {
        if (eclUserLoginM.pruefe_uportal_servicelinetools() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uServicelineAnfrage", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }
        
        String zielEmailAdresse=uServicelineAnfrageSession.getNeueEmailAdresse();
        if (zielEmailAdresse.isEmpty() || !CaString.isMailadresse(zielEmailAdresse)) {
            uSession.setFehlermeldung("Bitte gültige E-Mail-Adresse eingeben");
            return xSessionVerwaltung.setzeUEnde("uServicelineAnfrage", false, false, eclUserLoginM.getKennung());
        }
        
        eclDbM.openAll();
        eclDbM.openWeitere();
        BlGastkarte blGastkarte = new BlGastkarte(eclDbM.getDbBundle());
        blGastkarte.neuesPasswortGenerieren(uServicelineAnfrageSession.getAktionaersNummerIntern(), false, 4);
        
        /*Mail für Kennung*/
        String lKennung=BlNummernformBasis.aufbereitenKennungFuerExtern(uServicelineAnfrageSession.getAktionaersNummerIntern(), eclDbM.getDbBundle());
        String mailBetreff = eclTextePortalM.holeIText(KonstPortalTexte.ZUGANGSDATEN_KENNUNG_BETREFF);
        String mailText = eclTextePortalM.holeIText(KonstPortalTexte.ZUGANGSDATEN_KENNUNG_VORKENNUNG) + 
                lKennung
                + eclTextePortalM.holeIText(KonstPortalTexte.ZUGANGSDATEN_KENNUNG_NACHKENNUNG);
        baMailM.senden(zielEmailAdresse, mailBetreff, mailText);

        /*Mail für Passwort*/
        mailBetreff = eclTextePortalM.holeIText(KonstPortalTexte.ZUGANGSDATEN_PASSWORT_BETREFF);
        mailText = eclTextePortalM.holeIText(KonstPortalTexte.ZUGANGSDATEN_PASSWORT_VORPASSWORT) + 
                blGastkarte.rcPasswortFuerMail
                + eclTextePortalM.holeIText(KonstPortalTexte.ZUGANGSDATEN_PASSWORT_NACHPASSWORT);
        baMailM.senden(zielEmailAdresse, mailBetreff, mailText);

        datenEinlesen();

        
        eclDbM.closeAll();

        uServicelineAnfrageSession.setPasswortPerMailDialogAnzeigen(false);

        uSession.setFehlermeldung("Mail verschickt");

        return xSessionVerwaltung.setzeUEnde("uServicelineAnfrage", false, false, eclUserLoginM.getKennung());
    }
    
    public String doEmailRegistrierungAnzeigen() {
        if (eclUserLoginM.pruefe_uportal_servicelinetools() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uServicelineAnfrage", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }
        uServicelineAnfrageSession.setEmailEingabeAnzeigen(true);
        return xSessionVerwaltung.setzeUEnde("uServicelineAnfrage", false, false, eclUserLoginM.getKennung());
    }

    public String doEmailRegistrierungSpeichern() {
        if (eclUserLoginM.pruefe_uportal_servicelinetools() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uServicelineAnfrage", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }
        eclDbM.openAll();
        eclDbM.openWeitere();

        String lKennung=uServicelineAnfrageSession.getAktionaersNummerIntern();
        
        eclDbM.getDbBundle().dbLoginDaten.read_loginKennung(lKennung);
        EclLoginDaten lLoginDaten=eclDbM.getDbBundle().dbLoginDaten.ergebnisPosition(0);
        lLoginDaten.eMailFuerVersand=uServicelineAnfrageSession.getEmailEingabeEMail().trim();
        if (uServicelineAnfrageSession.isEmailEingabeRegistrierungVersand()) {
            lLoginDaten.eVersandRegistrierung=99;
       }
        else {
            lLoginDaten.eVersandRegistrierung=1;
       }
        if (lLoginDaten.eMailFuerVersand.isEmpty()) {
            lLoginDaten.emailBestaetigt=0;
        }
        else {
            lLoginDaten.emailBestaetigt=1;
       }
        eclDbM.getDbBundle().dbLoginDaten.update(lLoginDaten);

        
        datenEinlesen();

        uServicelineAnfrageSession.setEmailEingabeAnzeigen(false);
        eclDbM.closeAll();
        return xSessionVerwaltung.setzeUEnde("uServicelineAnfrage", false, false, eclUserLoginM.getKennung());
    }

    public String doAnmelden1Person() {
        return anAbmelden(1);
    }

    public String doAnmelden2Personen() {
        return anAbmelden(3);
    }

    public String doAbmelden() {
        return anAbmelden(2);
    }

    public String doAnmeldenZuruecksetzen() {
        return anAbmelden(0);
    }

    public String doEKAnzeigen() {
        if (eclUserLoginM.pruefe_uportal_servicelinetools() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uServicelineAnfrage", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }
        tGeneralversammlung.ausfuehrenEKAnzeigen(1);
         return xSessionVerwaltung.setzeUEnde("uServicelineAnfrage", false, false, eclUserLoginM.getKennung());
   }
 
    
    public String doEK2Anzeigen() {
        if (eclUserLoginM.pruefe_uportal_servicelinetools() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uServicelineAnfrage", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }
        tGeneralversammlung.ausfuehrenEKAnzeigen(2);
         return xSessionVerwaltung.setzeUEnde("uServicelineAnfrage", false, false, eclUserLoginM.getKennung());
   }
 
    public String doEK3Anzeigen() {
        if (eclUserLoginM.pruefe_uportal_servicelinetools() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uServicelineAnfrage", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }
        tGeneralversammlung.ausfuehrenEKAnzeigen(3);
        return xSessionVerwaltung.setzeUEnde("uServicelineAnfrage", false, false, eclUserLoginM.getKennung());
   }

    public String doGastkarteDrucken() {
        if (eclUserLoginM.pruefe_uportal_servicelinetools() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uServicelineAnfrage", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }
        
        eclDbM.openAll();

        BlEKFreiwilligesAnmelden blEKFreiwilligesAnmelden=new BlEKFreiwilligesAnmelden(eclDbM.getDbBundle()); 

        EclMeldung lMeldung=uServicelineAnfrageSession.getEclMeldung();

        blEKFreiwilligesAnmelden.ausfuehrenEKAnzeigenAllgemein(4, uServicelineAnfrageSession.getAktionaersNummer(), 1, lMeldung);
        eclDbM.closeAll();
        
        RpBrowserAnzeigen rpBrowserAnzeigen = new RpBrowserAnzeigen();
        rpBrowserAnzeigen.zeigen(blEKFreiwilligesAnmelden.rcDateiname);
        
        return xSessionVerwaltung.setzeUEnde("uServicelineAnfrage", false, false, eclUserLoginM.getKennung());


    }
    
    public String doVeranstaltungSpeichern(EclVeranstaltungenVeranstaltung pVeranstaltungenVeranstaltung) {
        if (eclUserLoginM.pruefe_uportal_servicelinetools() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uServicelineAnfrage", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }
        
        List<EclVeranstaltungenVeranstaltung> veranstaltungenListe=eclVeranstaltungenM.getVeranstaltungenListe();
        
        List<EclVeranstaltungenVeranstaltung> veranstaltungenListeNeu=new LinkedList<EclVeranstaltungenVeranstaltung>();
        veranstaltungenListeNeu.add(pVeranstaltungenVeranstaltung);
        eclVeranstaltungenM.setVeranstaltungenListe(veranstaltungenListeNeu);
        
        
        eclDbM.openAll();
        eclDbM.openWeitere();
        
        BlVeranstaltungen blVeranstaltungen=new BlVeranstaltungen(true, eclDbM.getDbBundle());
        if (eclVeranstaltungenM.pruefeObVeranstaltungenVorhanden()==true) {
            blVeranstaltungen.rcVeranstaltungenListe=eclVeranstaltungenM.getVeranstaltungenListe();

            boolean brc=blVeranstaltungen.pruefeVeranstaltungsliste(false);
            if (brc==false) {
                eclDbM.closeAll();
                uSession.setFehlermeldung(blVeranstaltungen.rcFehlerText);
                return xSessionVerwaltung.setzeUEnde();
            }
        }

        /**Nun ggf. noch Veranstaltungen abspeichern und für Quittung aufbereiten*/
        if (eclVeranstaltungenM.pruefeObVeranstaltungenVorhanden()==true) {
            blVeranstaltungen.loginKennung=uServicelineAnfrageSession.getAktionaersNummerIntern();
            blVeranstaltungen.aktionenAusfuehren();
            blVeranstaltungen.speichereWerteVeranstaltungsliste();
            
            
            eclVeranstaltungenM.setVeranstaltungenListe(veranstaltungenListe);
            
            
            blVeranstaltungen.rcQuittungsArt=2;
            blVeranstaltungen.aufbereitenQuittung();
            /*Werte müssen neu eingelesen werden, damit ggf. alte Werte für Veränderung richtig belegt sind.
             * Muß nach aufbereitenQuittung erfolgen, weil sonst Veränderungen nicht mehr richtig
             * erkannt werden!
             */
            blVeranstaltungen.belegeWerteVeranstaltungsliste();
            eclVeranstaltungenM.setVeranstaltungenQuittungListe(blVeranstaltungen.rcVeranstaltungenQuittungListe);
            
            /*Nun ggf. noch Mails verschicken*/
            if (blVeranstaltungen.rcMailBetreff!=null && blVeranstaltungen.rcMailBetreff.size()!=0) {
                String mailAdresse=uServicelineAnfrageSession.getEmailHinterlegt();
                if (!mailAdresse.isEmpty()) {
                    for (int i=0;i<blVeranstaltungen.rcMailBetreff.size();i++) {
                        
                        String mailBetreffNr=blVeranstaltungen.rcMailBetreff.get(i);
                        String mailBetreff=eclPortalTexteM.holeText(mailBetreffNr);
                        
                        String mailTextNr=blVeranstaltungen.rcMailText.get(i);
                        String mailText=eclPortalTexteM.holeText(mailTextNr);
                        
                        String mailAnhang="";
                        String rcMailAnhang=blVeranstaltungen.rcMailAnhang.get(i);
                        if (rcMailAnhang.isEmpty()==false) {
                            mailAnhang=eclDbM.getDbBundle().lieferePfadMeetingReports()+"\\"+rcMailAnhang;
                        }

                        baMailM.sendenMitAnhang(mailAdresse, mailBetreff, mailText, mailAnhang);
                    }
                }
            }

            
        }

        eclDbM.closeAll();
        uServicelineAnfrageSession.setVeranstaltungGespeichert(true);
        
        return xSessionVerwaltung.setzeUEnde("uServicelineAnfrage", false, false, eclUserLoginM.getKennung());

    }
    public String doGeneralversammlungAnmeldungSpeichern() {
        if (eclUserLoginM.pruefe_uportal_servicelinetools() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uServicelineAnfrage", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }
        
        eclDbM.openAll();
        eclDbM.openWeitere();

        eclBesitzGesamtAuswahl1M.setBestaetigtDassBerechtigt(true);
        
       int rc=tGeneralversammlung.ausfuehrenWeiter(false); //Ohne Veranstaltungen
       
       eclBesitzGesamtAuswahl1M.setVertreterAufGeprueftSetzen(true);

       eclDbM.closeAll();
       
       if (rc<1) {
           uSession.setFehlermeldung(CaFehler.getFehlertext(rc, 0));
           return xSessionVerwaltung.setzeUEnde();
       }
       
       return xSessionVerwaltung.setzeUEnde("uServicelineAnfrage", false, false, eclUserLoginM.getKennung());
    }

    private String anAbmelden(int pWert) {

        if (eclUserLoginM.pruefe_uportal_servicelinetools() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uServicelineAnfrage", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }
        eclDbM.openAll();
        eclDbM.openWeitere();
        BlVeranstaltungen blVeranstaltungen = new BlVeranstaltungen(true, eclDbM.getDbBundle());
        blVeranstaltungen.gv_anAbmeldung(uServicelineAnfrageSession.getAktionaersIdent(), pWert, 2);

        datenEinlesen();

        eclDbM.closeAll();

        return xSessionVerwaltung.setzeUEnde("uServicelineAnfrage", true, false, eclUserLoginM.getKennung());
    }

    /**open/close in aufrufender Funktion*/
    private boolean datenEinlesen() {
        EclLoginDaten lLoginDaten = null;
        EclAktienregister lAktienregister = null;
        int aktienregisterIdent = 0;
        EclPersonenNatJur lPersonenNatJur = null;

        /**1 oder 2*/
        int aktionaerOderGast = 0;

        String aktionaersnummerEingabe = uServicelineAnfrageSession.getAktionaersNummer().trim();
        if (aktionaersnummerEingabe.isEmpty()) {
            uSession.setFehlermeldung("Bitte " + eclParamM.investorenNr() + " eingeben");
            return false;
        }

        BlNummernformBasis blNummernformBasis = new BlNummernformBasis(eclDbM.getDbBundle());
        String aktionaersnummerAufbereitet = blNummernformBasis
                .loginKennungAufbereitenFuerIntern(aktionaersnummerEingabe);
 
        if (aktionaersnummerAufbereitet.startsWith("S")) {
            /*Vertreter*/
            aktionaerOderGast = 2;
            eclDbM.getDbBundle().dbLoginDaten.read_loginKennung(aktionaersnummerAufbereitet);
            uServicelineAnfrageSession.setAktionaersNummerIntern(aktionaersnummerAufbereitet);
            lLoginDaten = eclDbM.getDbBundle().dbLoginDaten.ergebnisPosition(0);
            if (eclDbM.getDbBundle().dbLoginDaten.anzErgebnis() == 0) {
                uSession.setFehlermeldung("Kennung nicht vorhanden");
                return false;
            }
            int ident = lLoginDaten.personenNatJurIdent;

            eclDbM.getDbBundle().dbPersonenNatJur.read(ident);
            lPersonenNatJur = eclDbM.getDbBundle().dbPersonenNatJur.personenNatJurArray[0];

            uServicelineAnfrageSession.setName(lPersonenNatJur.name);
            uServicelineAnfrageSession.setVorname(lPersonenNatJur.vorname);
            uServicelineAnfrageSession.setStrasse(lPersonenNatJur.strasse);
            uServicelineAnfrageSession.setPlz(lPersonenNatJur.plz);
            uServicelineAnfrageSession.setOrt(lPersonenNatJur.ort);
            uServicelineAnfrageSession.setAktien("");
            
            eclDbM.getDbBundle().dbMeldungen.leseZuMeldungsIdent(lLoginDaten.meldeIdent);
            EclMeldung lMeldung=eclDbM.getDbBundle().dbMeldungen.meldungenArray[0];
            uServicelineAnfrageSession.setGastkartenNr(lMeldung.zutrittsIdent);
            uServicelineAnfrageSession.setEclMeldung(lMeldung);
        } else {/*Aktionär*/
            if (eclParamM.getRegisterAnbindung()==1) {
                /**Aktionärsdaten werden übertragen. Ist unklar, ob das so richtig ist, denn damit wird
                 * das Mitglied "vorregistriert", wenn es bisher noch nicht übertragen war.
                 * Andererseits passiert das ja auch beim import bei der Versammlung ...
                 */
                brMInit.init();
                int rc=brMARRemoteZuARLokal.pruefeAR(aktionaersnummerAufbereitet);
                if (rc==CaFehler.perRemoteAktienregisterNichtVerfuegbar) {
                    uSession.setFehlermeldung("Register derzeit nicht verfügbar");
                    return false;
                }
                if (rc==1) {
                    brMARRemoteZuARLokal.fuelleAR(aktionaersnummerAufbereitet);
                }
            }
            aktionaerOderGast = 1;
            lAktienregister = new EclAktienregister();
            lAktienregister.aktionaersnummer = aktionaersnummerAufbereitet;
            eclDbM.getDbBundle().dbAktienregister.leseZuAktienregisterEintrag(lAktienregister);
            if (eclDbM.getDbBundle().dbAktienregister.anzErgebnis() == 0) {
                uSession.setFehlermeldung("Kennung nicht vorhanden");
                return false;
            }
            lAktienregister = eclDbM.getDbBundle().dbAktienregister.ergebnisPosition(0);
            aktienregisterIdent = lAktienregister.aktienregisterIdent;
            uServicelineAnfrageSession.setAktionaersIdent(aktienregisterIdent);

            uServicelineAnfrageSession.setName(lAktienregister.nachname);
            uServicelineAnfrageSession.setVorname(lAktienregister.vorname);
            uServicelineAnfrageSession.setStrasse(lAktienregister.strasse);
            uServicelineAnfrageSession.setPlz(lAktienregister.postleitzahl);
            uServicelineAnfrageSession.setOrt(lAktienregister.ort);
            uServicelineAnfrageSession.setAktien(CaString.toStringDE(lAktienregister.stueckAktien));

            uServicelineAnfrageSession.setAktionaersNummerIntern(lAktienregister.aktionaersnummer);

            eclDbM.getDbBundle().dbLoginDaten.read_aktienregisterIdent(aktienregisterIdent);
            lLoginDaten = eclDbM.getDbBundle().dbLoginDaten.ergebnisPosition(0);
            
         }

        uServicelineAnfrageSession.setAktionaerOderGast(aktionaerOderGast);

        if (lLoginDaten.anmeldenUnzulaessig == 1) {
            uServicelineAnfrageSession.setPortalNutzungGesperrt(true);
        }
        else {
            uServicelineAnfrageSession.setPortalNutzungGesperrt(false);
        }
        if (lLoginDaten.dauerhafteRegistrierungUnzulaessig == 1) {
            uServicelineAnfrageSession.setDauerhafteRegistrierungUnzulaessig(true);
        }

        if (!lLoginDaten.passwortInitial.isEmpty()) {
            CaBug.druckeLog("!lLoginDaten.passwortInitial.isEmpty()", logDrucken, 10);
            if (eclUserLoginM.pruefe_uportal_servicelinetools_passwort()) {
                CaBug.druckeLog("Berechtigung ist vorhanden", logDrucken, 10);
                String lPWInitial = lLoginDaten.passwortInitial;
                int l = lPWInitial.length() / 3;
                lPWInitial = CaString.substring(lPWInitial, l, 2 * l);

                uServicelineAnfrageSession.setPasswortInitialAnzeigen(true);
                uServicelineAnfrageSession.setPasswortInitial(lPWInitial);
            }
        }

        /*Passwort*/
        if (lLoginDaten.eigenesPasswort == 98) {
            uServicelineAnfrageSession.setPasswortVergessenPerEmailNichtAbgeschlossen(true);

        }
        if (lLoginDaten.eigenesPasswort == 99) {
            uServicelineAnfrageSession.setEigenesPasswortVergeben(true);
        }
        /*Email*/
        uServicelineAnfrageSession.setEmailHinterlegt(lLoginDaten.eMailFuerVersand);
        if (lLoginDaten.emailBestaetigt == 1) {
            uServicelineAnfrageSession.setEmailWurdeBestaetigt(true);
            uServicelineAnfrageSession.setEmailBestaetigungscode("");
        }
        else {
            uServicelineAnfrageSession.setEmailWurdeBestaetigt(false);
            uServicelineAnfrageSession.setEmailBestaetigungscode(lLoginDaten.emailBestaetigenLink);
        }
        if (lLoginDaten.eVersandRegistrierung == 99) {
            uServicelineAnfrageSession.setFuerEmailVersandRegistriert(true);
        }
        else{
            uServicelineAnfrageSession.setFuerEmailVersandRegistriert(false);
        }

        /*Email-Änderung*/
        uServicelineAnfrageSession.setEmailEingabeEMail(lLoginDaten.eMailFuerVersand);
        if (lLoginDaten.eVersandRegistrierung == 99) {
            uServicelineAnfrageSession.setEmailEingabeRegistrierungVersand(true);
        }
        else {
            uServicelineAnfrageSession.setEmailEingabeRegistrierungVersand(false);
        }
        if (eclParamM.getParam().paramPortal.mailEingabeServiceline==1 && eclParamM.getParam().paramPortal.registrierungFuerEmailVersandMoeglich==1
                /*&& eclUserLoginM.pruefe_uportal_servicelinetools_passwort()*/) {
            uServicelineAnfrageSession.setEmailEingabeMoeglich(true);
        }
        
        
        
        List<EclAufgabenM> aufgabenListe = new LinkedList<EclAufgabenM>();
        String hAktionaersnummerOhneZusatzzahl = null;
        if (aktionaerOderGast == 2) {
            /*Vertreter*/
            hAktionaersnummerOhneZusatzzahl = aktionaersnummerAufbereitet;
        } else {/*Aktionär*/
            hAktionaersnummerOhneZusatzzahl = lAktienregister.aktionaersnummer;
            if (hAktionaersnummerOhneZusatzzahl.length() > 10) {
                hAktionaersnummerOhneZusatzzahl = hAktionaersnummerOhneZusatzzahl.substring(0, 10);
            }
        }
        eclDbM.getDbBundle().dbAufgaben.read(hAktionaersnummerOhneZusatzzahl);
        int anz = eclDbM.getDbBundle().dbAufgaben.anzErgebnis();
        CaBug.druckeLog("hAktionaersnummerOhneZusatzzahl=" + hAktionaersnummerOhneZusatzzahl,
                logDrucken, 10);
        CaBug.druckeLog("anz=" + anz, logDrucken, 10);

        for (int i = 0; i < anz; i++) {
            EclAufgabenM lAufgabenM = new EclAufgabenM(eclDbM.getDbBundle().dbAufgaben.ergebnisPosition(i));
            aufgabenListe.add(lAufgabenM);
        }
        uServicelineAnfrageSession.setPasswortAnforderungen(aufgabenListe);

        uServicelineAnfrageSession.setGeneralversammlungAktiv(false);
        uServicelineAnfrageSession.setDialogveranstaltungAktiv(false);
        
        if (eclParamM.getParam().paramPortal.varianteDialogablauf == 1) {
            if (aktionaerOderGast == 1) {
                /*Zusatzdaten für ku178-Ablauf*/
                eclDbM.getDbBundle().dbAktienregisterErgaenzung.readZuident(aktienregisterIdent);
                EclAktienregisterErgaenzung lAktienregisterErgaenzung = eclDbM.getDbBundle().dbAktienregisterErgaenzung
                        .ergebnisPosition(0);
                uServicelineAnfrageSession.setGeburtsdatum1(lAktienregisterErgaenzung
                        .getErgaenzungString(KonstAktienregisterErgaenzung.ku178_GeburtsdatumMitglied));
                uServicelineAnfrageSession.setGeburtsdatum2(lAktienregisterErgaenzung
                        .getErgaenzungString(KonstAktienregisterErgaenzung.ku178_GeburtsdatumEhegatte));

                int ergaenzungGruppe = lAktienregister.gruppe;
                uServicelineAnfrageSession.setErgaenzungGruppe(KonstGruppen.getText(ergaenzungGruppe));

                eclDbM.getDbBundle().dbMeldungen.leseZuAktienregisterIdent(aktienregisterIdent, true);
                if (eclDbM.getDbBundle().dbMeldungen.meldungenArray.length > 0) {
                    uServicelineAnfrageSession.setAnAbGeneralversammlung(
                            eclDbM.getDbBundle().dbMeldungen.meldungenArray[0].vorlAnmeldung);
                }
            }
            
            if ((eclDbM.getDbBundle().param.paramPortal.lfdHVGeneralversammlungInMenue > 0 || eclDbM.getDbBundle().param.paramPortal.dialogveranstaltungAktiv > 0)
                    ) {
                if (eclDbM.getDbBundle().param.paramPortal.lfdHVGeneralversammlungInMenue > 0) {
                    uServicelineAnfrageSession.setGeneralversammlungAktiv(true);
                }
                if (eclDbM.getDbBundle().param.paramPortal.dialogveranstaltungAktiv > 0 && uServicelineAnfrageSession.getAktionaerOderGast()==1) {
                    uServicelineAnfrageSession.setDialogveranstaltungAktiv(true);
                    if (eclParamM.getParam().paramPortal.veranstaltungPersonenzahlEingeben==1) {
                        uServicelineAnfrageSession.setVeranstaltungPersonenzahlEingeben(true);
                    }
                    else {
                        uServicelineAnfrageSession.setVeranstaltungPersonenzahlEingeben(false);
                    }
                    tDialogveranstaltungenSession.setAnzPersonen("1");
                }
               
                /*Dialog-Veranstaltungen einlesen*/
                if (uServicelineAnfrageSession.getAktionaerOderGast()==1) {
                    tDialogveranstaltungen.initAllgemein(uServicelineAnfrageSession.getAktionaersIdent(), uServicelineAnfrageSession.getEmailHinterlegt());
                    tDialogveranstaltungen.initAnmeldenAllgemein(uServicelineAnfrageSession.getAktionaersIdent());
                }
                
            }

        }

        if (uServicelineAnfrageSession.isGeneralversammlungAktiv() && aktionaerOderGast == 1) {
            
            /*"Login-Daten" und Aktionärsstatus für Sonderablauf Mitgliederversammlungen einlesen*/
            BlTeilnehmerLoginNeu blTeilnehmerLogin = new BlTeilnehmerLoginNeu(false);
            blTeilnehmerLogin.initDB(eclDbM.getDbBundle());
            int rc=blTeilnehmerLogin.findeUndPruefeKennung(aktionaersnummerAufbereitet, "", false);
            CaBug.druckeLog("blTeilnehmerLogin rc="+rc, logDrucken, 10);
            eclLoginDatenM.copyFrom(blTeilnehmerLogin);

            tFunktionen.leseStatusPortal(eclDbM.getDbBundle());
            eclBesitzGesamtAuswahl1M.setVertreterAufGeprueftSetzen(true);

        }

        
        /**Berechtigungen*/
        uBerechtigungenSession.belegeBerechtigungen(lLoginDaten.berechtigungPortal);
        
        uServicelineAnfrageSession.setAktionaerWirdAngezeigt(true);

        
        /**Veranstaltungen*/
        BlVeranstaltungen blVeranstaltungen=new BlVeranstaltungen(true, eclDbM.getDbBundle());
        blVeranstaltungen.erzeugeVeranstaltungslisteFuerTeilnehmer(BlVeranstaltungen.LAUT_MENUE_NUMMER, -2);
        eclVeranstaltungenM.copyFromBlVeranstaltungen(blVeranstaltungen);
        blVeranstaltungen.loginKennung=uServicelineAnfrageSession.getAktionaersNummerIntern();
        blVeranstaltungen.belegeWerteVeranstaltungsliste();
        
        /**Prüfe muß durchgeführt werden, damit inLetzterVerarbeitungEnthalten richtig gesetzt wird!*/
        blVeranstaltungen.pruefeVeranstaltungsliste(false);
        
        blVeranstaltungen.rcQuittungsArt=1;
        blVeranstaltungen.aufbereitenQuittung();
        eclVeranstaltungenM.setVeranstaltungenQuittungListe(blVeranstaltungen.rcVeranstaltungenQuittungListe);

 
        
        return true;
    }

    public String doDialogveranstaltungAnmeldungStornieren() {
        if (eclUserLoginM.pruefe_uportal_servicelinetools() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uServicelineAnfrage", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }
        eclDbM.openAll();
        eclDbM.openWeitere();
        
        BlVeranstaltungen blVeranstaltungen=new BlVeranstaltungen(true, eclDbM.getDbBundle());
        blVeranstaltungen.widerrufeAnmeldung(uServicelineAnfrageSession.getAktionaersIdent(), 1);
        
        datenEinlesen();
        eclDbM.closeAll();
        return xSessionVerwaltung.setzeUEnde("uServicelineAnfrage", true, false, eclUserLoginM.getKennung());
    }
    
    public String doAnmeldenDialogveranstaltung(EclVeranstaltungM pVeranstaltung) {
        if (eclUserLoginM.pruefe_uportal_servicelinetools() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uServicelineAnfrage", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }
        
        tDialogveranstaltungen.anmeldenAusfuehren(uServicelineAnfrageSession.getAktionaersIdent(), pVeranstaltung, false, 1);

        eclDbM.openAll();
        eclDbM.openWeitere();
        datenEinlesen();
        eclDbM.closeAll();
        return xSessionVerwaltung.setzeUEnde("uServicelineAnfrage", true, false, eclUserLoginM.getKennung());
    }
    
    
    public String doAnmeldenDialogveranstaltungenMulti() {
        if (eclUserLoginM.pruefe_uportal_servicelinetools() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uServicelineAnfrage", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }

        List<EclVeranstaltungM> veranstaltungen=tDialogveranstaltungenSession.getVeranstaltungen();
        for (EclVeranstaltungM iEclVeranstaltungM: veranstaltungen) {
            if (iEclVeranstaltungM.getAnAbgemeldet()!=null && iEclVeranstaltungM.getAnAbgemeldet().equals("1")) {
                /*boolean brc=*/tDialogveranstaltungen.anmeldenAusfuehren(uServicelineAnfrageSession.getAktionaersIdent(), iEclVeranstaltungM, true, 1); 
            }
            else {
                /*boolean brc=*/tDialogveranstaltungen.anmeldenAusfuehren(uServicelineAnfrageSession.getAktionaersIdent(), iEclVeranstaltungM, true, 2); 
            }
        }

        eclDbM.openAll();
        eclDbM.openWeitere();
        datenEinlesen();
        eclDbM.closeAll();
        return xSessionVerwaltung.setzeUEnde("uServicelineAnfrage", true, false, eclUserLoginM.getKennung());

        
    }
    
   
    
    public String doStornierenDialogveranstaltungenMulti() {
        if (eclUserLoginM.pruefe_uportal_servicelinetools() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uServicelineAnfrage", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }

        eclDbM.openAll();
        eclDbM.openWeitere();
        BlVeranstaltungen blVeranstaltungen=new BlVeranstaltungen(true, eclDbM.getDbBundle());
        blVeranstaltungen.widerrufeAnmeldung(uServicelineAnfrageSession.getAktionaersIdent(), 1);
        eclDbM.closeAll();
        
        eclDbM.openAll();
        eclDbM.openWeitere();
        datenEinlesen();
        eclDbM.closeAll();
        return xSessionVerwaltung.setzeUEnde("uServicelineAnfrage", true, false, eclUserLoginM.getKennung());

        
    }

}
