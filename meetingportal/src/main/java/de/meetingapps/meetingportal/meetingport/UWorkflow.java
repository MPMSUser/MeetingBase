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
import de.meetingapps.meetingportal.meetComAllg.CaString;
import de.meetingapps.meetingportal.meetComBa.BaMailM;
import de.meetingapps.meetingportal.meetComBl.BlBestaetigungsWorkflow;
import de.meetingapps.meetingportal.meetComBl.BlGastkarte;
import de.meetingapps.meetingportal.meetComBl.BlNummernformBasis;
import de.meetingapps.meetingportal.meetComBl.BlVorlaeufigeVollmacht;
import de.meetingapps.meetingportal.meetComBl.BlWillenserklaerung;
import de.meetingapps.meetingportal.meetComEclM.EclBestWorkflowM;
import de.meetingapps.meetingportal.meetComEclM.EclDbM;
import de.meetingapps.meetingportal.meetComEclM.EclParamM;
import de.meetingapps.meetingportal.meetComEclM.EclPortalTexteM;
import de.meetingapps.meetingportal.meetComEclM.EclUserLoginM;
import de.meetingapps.meetingportal.meetComEh.EhVorlaeufigeVollmacht;
import de.meetingapps.meetingportal.meetComEntities.EclAktienregister;
import de.meetingapps.meetingportal.meetComEntities.EclAufgaben;
import de.meetingapps.meetingportal.meetComEntities.EclBestWorkflow;
import de.meetingapps.meetingportal.meetComEntities.EclBestWorkflowVorlVollmacht;
import de.meetingapps.meetingportal.meetComEntities.EclLoginDaten;
import de.meetingapps.meetingportal.meetComEntities.EclMeldung;
import de.meetingapps.meetingportal.meetComEntities.EclPersonenNatJur;
import de.meetingapps.meetingportal.meetComEntities.EclVorlaeufigeVollmacht;
import de.meetingapps.meetingportal.meetComEntities.EclVorlaeufigeVollmachtEingabe;
import de.meetingapps.meetingportal.meetComEntities.EclVorlaeufigeVollmachtFuerAnzeige;
import de.meetingapps.meetingportal.meetComHVParam.ParamSpezial;
import de.meetingapps.meetingportal.meetComKonst.KonstAufgaben;
import de.meetingapps.meetingportal.meetComKonst.KonstAufgabenAnforderer;
import de.meetingapps.meetingportal.meetComKonst.KonstAufgabenStatus;
import de.meetingapps.meetingportal.meetComKonst.KonstEingabeQuelle;
import de.meetingapps.meetingportal.meetComKonst.KonstLoginKennungArt;
import de.meetingapps.meetingportal.meetComKonst.KonstWillenserklaerungWeg;
import de.meetingapps.meetingportal.meetingCoreReport.RpBrowserAnzeigen;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@RequestScoped
@Named
public class UWorkflow {

    private int logDrucken = 10;

    @Inject
    private EclDbM eclDbM;
    @Inject
    private USession uSession;
    @Inject
    private UWorkflowSession uWorkflowSession;
    @Inject
    private UMenueSession uMenueSession;
    @Inject
    private XSessionVerwaltung xSessionVerwaltung;
    @Inject
    private EclBestWorkflowM eclBestWorkflowM;
    @Inject
    private EclParamM eclParamM;
    @Inject
    private EclUserLoginM eclUserLoginM;
    @Inject
    private EclPortalTexteM eclTextePortalM;
    @Inject
    private BaMailM baMailM;

    /*===========================================Vollmachtsprüfung Workflow ku178==============================================*/
    public void init() {
        uWorkflowSession.setBasisStatus(0);
        uWorkflowSession.setAnzeigeInfoBereich(0);
        uWorkflowSession.setBereitsVollmachtVorhanden(false);
        uWorkflowSession.setBestWorkflowVorlVollmachtListe(null);
    }

    /************************************uWorkflowVollmachtenStandard*************************/
    public String doNaechstesDokumentStandard() {
        if (uMenueSession.menueWorkflowBasis() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uWorkflowVollmachtenStandard", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "";
        }
        
        uWorkflowSession.clear();
        
        eclDbM.openAll();
        eclDbM.openWeitere();
        int erg = eclDbM.getDbBundle().dbBestWorkflow.reserviereFuerBasis();
        eclDbM.closeAll();
        if (erg < 1) {
            int erg1=naechstesDokumentStandardOhneDokument();
            if (erg1<1) {
                uSession.setFehlermeldung("Keine weiteren Sätze zur Bearbeitung gefunden!");
                xSessionVerwaltung.setzeUEnde();
                return "";
            }
        }
        else {
            eclBestWorkflowM.copyFrom(eclDbM.getDbBundle().dbBestWorkflow.ergebnisArray[0]);
        }
        
        uWorkflowSession.setBasisStatus(1);
        uWorkflowSession.setWiederVerwenden(false);
        uWorkflowSession.setErweiterteFreigabe(false);
        return xSessionVerwaltung.setzeUEnde("uWorkflowVollmachtenStandard", true, false, eclUserLoginM.getKennung());
    }

    /**Einlesen des nächsten Aktionärs, der noch offene elektronisch übermittelte
     * Vollmachtsdaten hat.
     * 
     * Daüfür eclBestWorkflow erstellen, in eclBestworkflowM übertragen,
     * und in diesem Fall auch den Vollmacht-gebenden Aktionär anzeigen und füllen
     * 
     * DbM muß hier ncoh mal separat geöffnet und geschlossen werden.
     */
    private int naechstesDokumentStandardOhneDokument() {
        eclDbM.openAll();
        eclDbM.openWeitere();
        
        int rc=eclDbM.getDbBundle().dbVorlaeufigeVollmachtEingabe.readNaechstenUngeprueftenAktionaer();
        if (rc<1) {
            /*Keiner mehr gefunden*/
            eclDbM.closeAll();
            return -1;
        }
        String lAktionaersnummer=eclDbM.getDbBundle().dbVorlaeufigeVollmachtEingabe.ergebnisPosition(0).erteilendeAktionaersnummer;
        CaBug.druckeLog("lAktionaersnummer="+lAktionaersnummer, logDrucken, 10);
        
        /*Dummy EclBestWorkslow-Satz erzeugen und einlesen*/
        EclBestWorkflow lBestWorkflow = new EclBestWorkflow();
        lBestWorkflow.subverzeichnis = "";
        lBestWorkflow.origOderKopie = 3;
        lBestWorkflow.dateinameBestaetigung = "";
        lBestWorkflow.dateinameImportAm = CaDatumZeit.DatumZeitStringFuerDatenbank();
        lBestWorkflow.mandant = eclDbM.getDbBundle().clGlobalVar.mandant;
        eclDbM.getDbBundle().dbBestWorkflow.insert(lBestWorkflow);
        eclBestWorkflowM.copyFrom(lBestWorkflow);
        
        /*Gebenden Aktionär aufrufen*/
        eclBestWorkflowM.setZuAktionaersnummer(lAktionaersnummer);
        ausfuehrenGeberHolen();
        
        eclDbM.closeAll();
        return 1;
    }
    
    public String doPDFAnzeigenStandard() {
        if (uMenueSession.menueWorkflowBasis() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uWorkflowVollmachtenStandard", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "";
        }

        RpBrowserAnzeigen rpBrowserAnzeigen = new RpBrowserAnzeigen();
        rpBrowserAnzeigen.zeigen(
                eclParamM.getClGlobalVar().lwPfadAllgemein + "\\"+eclParamM.getParamServer().praefixPfadVerzeichnisse
                        + "reports\\" + eclParamM.getMandantPfad() + "\\"
                        + eclBestWorkflowM.getSubverzeichnis() + "\\" + eclBestWorkflowM.getDateinameBestaetigung());
        return xSessionVerwaltung.setzeUEnde("uWorkflowVollmachtenStandard", true, false, eclUserLoginM.getKennung());
    }

    public String doPDFAnzeigenDetail() {
        if (uMenueSession.menueWorkflowBasis() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uWorkflowVollmachtenDetail", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "";
        }

        RpBrowserAnzeigen rpBrowserAnzeigen = new RpBrowserAnzeigen();
        rpBrowserAnzeigen.zeigen(
                eclParamM.getClGlobalVar().lwPfadAllgemein + "\\"+eclParamM.getParamServer().praefixPfadVerzeichnisse
                + "reports\\" + eclParamM.getMandantPfad() + "\\"
                + eclBestWorkflowM.getSubverzeichnis() + "\\" + eclBestWorkflowM.getDateinameBestaetigung());
        return xSessionVerwaltung.setzeUEnde("uWorkflowVollmachtenDetail", true, false, eclUserLoginM.getKennung());
    }

    public String doGeberHolen() {
        if (uMenueSession.menueWorkflowBasis() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uWorkflowVollmachtenStandard", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "";
        }

        eclDbM.openAll();
        eclDbM.openWeitere();

        int rc=ausfuehrenGeberHolen();
        
        eclDbM.closeAll();

        if (rc==-1) {
            xSessionVerwaltung.setzeUEnde();
            return "";
        }
        
         return xSessionVerwaltung.setzeUEnde("uWorkflowVollmachtenStandard", true, false, eclUserLoginM.getKennung());
    }

    /**
     * eclDbM wird in aufrufender Funktion gehandled.
     * 
     * returnwert = -1 => Fehlermeldung ist bereits ggf. gefüllt; anschließend noch 
     * close           
     * xSessionVerwaltung.setzeUEnde();
     * return; 
     * ausführen
     */
    private int ausfuehrenGeberHolen() {
        EclAktienregister lAktienregister = new EclAktienregister();

        BlNummernformBasis blNummernformBasis = new BlNummernformBasis(eclDbM.getDbBundle());
        lAktienregister.aktionaersnummer = blNummernformBasis
                .loginKennungAufbereitenFuerIntern(eclBestWorkflowM.getZuAktionaersnummer());
        CaBug.druckeLog("lAktienregister.aktionaersnummer=" + lAktienregister.aktionaersnummer, logDrucken, 10);

        int erg = eclDbM.getDbBundle().dbAktienregister.leseZuAktienregisterEintrag(lAktienregister);
        if (erg < 1) {
            eclBestWorkflowM.setIdentMitglied(0);
            uSession.setFehlermeldung("Nicht gefunden!");
            return -1;
        } else {
            lAktienregister = eclDbM.getDbBundle().dbAktienregister.ergebnisArray[0];
            eclBestWorkflowM.setNameMitglied(lAktienregister.nameKomplett);
            eclBestWorkflowM.setIdentMitglied(lAktienregister.aktienregisterIdent);
        }

        
        /*Bereits vorhandene Datensätze aus Workflow (Papier und Digital) holen - Digital-Sätze reservieren*/
        BlVorlaeufigeVollmacht blVorlaeufigeVollmacht=new BlVorlaeufigeVollmacht(eclDbM.getDbBundle());
        blVorlaeufigeVollmacht.belegeListeVorlaeufigeVollmachten(lAktienregister.aktionaersnummer, lAktienregister.aktienregisterIdent);
        uWorkflowSession.setVorhandeneVorgaenge(blVorlaeufigeVollmacht.rcEhVorlaeufigeVollmachtList);
        
        /**Nun andere Vollmachten holen*/
        boolean bRc = leseAlleVollmachtenZuGeber(lAktienregister.aktienregisterIdent);

        if (bRc == false) {
            uSession.setFehlermeldung(
                    "Vollmachtgeber hat bereits Vollmacht erteilt! Diese muß vor einer weiteren Vollmacht erst storniert werden.");
            uWorkflowSession.setBereitsVollmachtVorhanden(true);
            return -1;
        }
        uWorkflowSession.setBereitsVollmachtVorhanden(false);
        return 1;
        
    }
    
    
    public String doNehmerHolen() {
        if (uMenueSession.menueWorkflowBasis() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uWorkflowVollmachtenStandard", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "";
        }

        eclDbM.openAll();
        eclDbM.openWeitere();
        BlNummernformBasis blNummernformBasis = new BlNummernformBasis(eclDbM.getDbBundle());
        String kennung = blNummernformBasis.loginKennungAufbereitenFuerIntern(eclBestWorkflowM.getKennung());
        if (kennung.isEmpty()) {
            eclDbM.closeAll();
            uSession.setFehlermeldung("Bitte Mitgliedsnummer/Kennung des Bevollmächtigten eingeben");
            xSessionVerwaltung.setzeUEnde();
            return "";
        }
        kennung = BlNummernformBasis.aufbereitenFuerDatenbankZugriff(kennung, eclDbM.getDbBundle());
        eclDbM.getDbBundle().dbLoginDaten.read_loginKennung(kennung);
        if (eclDbM.getDbBundle().dbLoginDaten.anzErgebnis() == 0) {
            eclDbM.closeAll();
            uSession.setFehlermeldung("Mitgliedsnummer/Kennung des Bevollmächtigten nicht gefunden");
            xSessionVerwaltung.setzeUEnde();
            return "";
        }

        EclLoginDaten lLoginDaten = eclDbM.getDbBundle().dbLoginDaten.ergebnisPosition(0);
        if (lLoginDaten.kennungArt == KonstLoginKennungArt.aktienregister) {
            int ident = lLoginDaten.aktienregisterIdent;
            eclDbM.getDbBundle().dbAktienregister.leseZuAktienregisterIdent(ident);
            EclAktienregister lAktienregister = eclDbM.getDbBundle().dbAktienregister.ergebnisPosition(0);
            eclBestWorkflowM.setBevollmaechtigterTitel(lAktienregister.titel);
            eclBestWorkflowM.setBevollmaechtigterName(lAktienregister.liefereName());
            eclBestWorkflowM.setBevollmaechtigterVorname(lAktienregister.vorname);
            eclBestWorkflowM.setBevollmaechtigterStrasse(lAktienregister.strasse);
            eclBestWorkflowM.setBevollmaechtigterPlz(lAktienregister.postleitzahl);
            eclBestWorkflowM.setBevollmaechtigterOrt(lAktienregister.ort);
            eclBestWorkflowM.setBevollmaechtigterEMail(lLoginDaten.eMailFuerVersand);

            eclBestWorkflowM.setMitgliedOderSonstiger("1");
            eclBestWorkflowM.setKennungGeladenIst(KonstLoginKennungArt.aktienregister);
            eclBestWorkflowM.setKennungGeladenIdent(ident);
            eclBestWorkflowM.setPersonNatJurGeladenIdent(lAktienregister.personNatJur);

            /*Nun noch Anzahl Vertretungen einlesen*/
            BlVorlaeufigeVollmacht blVorlaeufigeVollmacht = new BlVorlaeufigeVollmacht(eclDbM.getDbBundle());
            blVorlaeufigeVollmacht.liefereAnzahlVollmachtenZuBevollmaechtigten(ident, 1);
            eclBestWorkflowM.setHinweisAnzahlVollmachten("Anzahl gesetzlicher Vollmachten: "
                    + Integer.toString(blVorlaeufigeVollmacht.rcAnzGesetzlicheVollmachten)
                    + " Anzahl sonstiger Vollmachten: "
                    + Integer.toString(blVorlaeufigeVollmacht.rcAnzSonstigeVollmachten));
            eclBestWorkflowM.setGenommenAnzahlVollmachtenGesetzlich(blVorlaeufigeVollmacht.rcAnzGesetzlicheVollmachten);
            eclBestWorkflowM.setGenommenAnzahlVollmachtenNormal(blVorlaeufigeVollmacht.rcAnzSonstigeVollmachten);

        } else {
            int ident = lLoginDaten.personenNatJurIdent;
            eclDbM.getDbBundle().dbPersonenNatJur.read(ident);
            EclPersonenNatJur lPersonenNatJur = eclDbM.getDbBundle().dbPersonenNatJur.PersonNatJurGefunden(0);
            eclBestWorkflowM.setBevollmaechtigterTitel(lPersonenNatJur.titel);
            eclBestWorkflowM.setBevollmaechtigterName(lPersonenNatJur.name);
            eclBestWorkflowM.setBevollmaechtigterVorname(lPersonenNatJur.vorname);
            eclBestWorkflowM.setBevollmaechtigterStrasse(lPersonenNatJur.strasse);
            eclBestWorkflowM.setBevollmaechtigterPlz(lPersonenNatJur.plz);
            eclBestWorkflowM.setBevollmaechtigterOrt(lPersonenNatJur.ort);
            eclBestWorkflowM.setBevollmaechtigterEMail(lLoginDaten.eMailFuerVersand);

            eclBestWorkflowM.setMitgliedOderSonstiger("2");
            eclBestWorkflowM.setKennungGeladenIst(KonstLoginKennungArt.personenNatJur);
            eclBestWorkflowM.setKennungGeladenIdent(ident);
            eclBestWorkflowM.setPersonNatJurGeladenIdent(0);

            /*Nun noch Anzahl Vertretungen einlesen*/
            BlVorlaeufigeVollmacht blVorlaeufigeVollmacht = new BlVorlaeufigeVollmacht(eclDbM.getDbBundle());
            blVorlaeufigeVollmacht.liefereAnzahlVollmachtenZuBevollmaechtigten(ident, 2);
            eclBestWorkflowM.setHinweisAnzahlVollmachten("Anzahl gesetzlicher Vollmachten: "
                    + Integer.toString(blVorlaeufigeVollmacht.rcAnzGesetzlicheVollmachten)
                    + " Anzahl sonstiger Vollmachten: "
                    + Integer.toString(blVorlaeufigeVollmacht.rcAnzSonstigeVollmachten));
            eclBestWorkflowM.setGenommenAnzahlVollmachtenGesetzlich(blVorlaeufigeVollmacht.rcAnzGesetzlicheVollmachten);
            eclBestWorkflowM.setGenommenAnzahlVollmachtenNormal(blVorlaeufigeVollmacht.rcAnzSonstigeVollmachten);

        }
        eclDbM.closeAll();
        eclBestWorkflowM.setKennungGeladen(true);
        return xSessionVerwaltung.setzeUEnde("uWorkflowVollmachtenStandard", true, false, eclUserLoginM.getKennung());
    }

    public String doNehmerClear() {
        if (uMenueSession.menueWorkflowBasis() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uWorkflowVollmachtenStandard", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "";
        }

        eclBestWorkflowM.setKennung("");
        eclBestWorkflowM.setBevollmaechtigterTitel("");
        eclBestWorkflowM.setBevollmaechtigterName("");
        eclBestWorkflowM.setBevollmaechtigterVorname("");
        eclBestWorkflowM.setBevollmaechtigterStrasse("");
        eclBestWorkflowM.setBevollmaechtigterPlz("");
        eclBestWorkflowM.setBevollmaechtigterOrt("");
        eclBestWorkflowM.setBevollmaechtigterEMail("");

        eclBestWorkflowM.setHinweisAnzahlVollmachten("");
        eclBestWorkflowM.setGenommenAnzahlVollmachtenGesetzlich(0);
        eclBestWorkflowM.setGenommenAnzahlVollmachtenNormal(0);

        eclBestWorkflowM.setKennungGeladen(false);
        return xSessionVerwaltung.setzeUEnde("uWorkflowVollmachtenStandard", true, false, eclUserLoginM.getKennung());
    }

    public String doNameMitgliedSuchen() {
        if (uMenueSession.menueWorkflowBasis() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uWorkflowVollmachtenStandard", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "";
        }
        String nameSuchen = eclBestWorkflowM.getBevollmaechtigterName();
        if (nameSuchen.length() < 3) {
            uSession.setFehlermeldung("Suchen: mindestens 3 Zeichen eingeben");
            xSessionVerwaltung.setzeEnde();
            return "";
        }
        eclDbM.openAll();
        eclDbM.openWeitere();
        EclAktienregister lAktienregister = new EclAktienregister();
        lAktienregister.name1 = nameSuchen;
        uWorkflowSession.setAnzeigeInfoBereich(2);
        eclDbM.getDbBundle().dbAktienregister.leseZuAktienregisterEintrag(lAktienregister);
        for (int i = 0; i < eclDbM.getDbBundle().dbAktienregister.anzErgebnis(); i++) {
            String hNummer = eclDbM.getDbBundle().dbAktienregister.ergebnisArray[i].aktionaersnummer;
            hNummer = BlNummernformBasis.aufbereitenInternFuerExtern(hNummer, eclDbM.getDbBundle());
            eclDbM.getDbBundle().dbAktienregister.ergebnisArray[i].aktionaersnummer = hNummer;
        }
        uWorkflowSession.setAktienregisterArray(eclDbM.getDbBundle().dbAktienregister.ergebnisArray);
        eclDbM.closeAll();
        return xSessionVerwaltung.setzeUEnde("uWorkflowVollmachtenStandard", true, false, eclUserLoginM.getKennung());
    }

    public String doNameSonstigeSuchen() {
        if (uMenueSession.menueWorkflowBasis() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uWorkflowVollmachtenStandard", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "";
        }
        String nameSuchen = eclBestWorkflowM.getBevollmaechtigterName();
        if (nameSuchen.length() < 3) {
            uSession.setFehlermeldung("Suchen: mindestens 3 Zeichen eingeben");
            xSessionVerwaltung.setzeEnde();
            return "";
        }
        eclDbM.openAll();
        eclDbM.openWeitere();
        uWorkflowSession.setAnzeigeInfoBereich(3);
        eclDbM.getDbBundle().dbPersonenNatJur.leseAlleGaesteMitName(nameSuchen);
        uWorkflowSession.setPersonenNatJurArray(eclDbM.getDbBundle().dbPersonenNatJur.personenNatJurArray);
        eclDbM.closeAll();
        return xSessionVerwaltung.setzeUEnde("uWorkflowVollmachtenStandard", true, false, eclUserLoginM.getKennung());
    }

    public String doEmailSuchen() {
        if (uMenueSession.menueWorkflowBasis() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uWorkflowVollmachtenStandard", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "";
        }
        String emailSuchen = eclBestWorkflowM.getBevollmaechtigterEMail();
        if (emailSuchen.length() < 3) {
            uSession.setFehlermeldung("Suchen: mindestens 3 Zeichen eingeben");
            xSessionVerwaltung.setzeEnde();
            return "";
        }
        eclDbM.openAll();
        eclDbM.openWeitere();
        uWorkflowSession.setAnzeigeInfoBereich(4);
        eclDbM.getDbBundle().dbLoginDaten.read_sucheEmail(emailSuchen);
        for (int i = 0; i < eclDbM.getDbBundle().dbLoginDaten.anzErgebnis(); i++) {
            String hNummer = eclDbM.getDbBundle().dbLoginDaten.ergebnisPosition(i).loginKennung;
            hNummer = BlNummernformBasis.aufbereitenInternFuerExtern(hNummer, eclDbM.getDbBundle());
            eclDbM.getDbBundle().dbLoginDaten.ergebnisPosition(i).loginKennung = hNummer;
        }
        uWorkflowSession.setLoginDatenArray(eclDbM.getDbBundle().dbLoginDaten.ergebnis());
        eclDbM.closeAll();
        return xSessionVerwaltung.setzeUEnde("uWorkflowVollmachtenStandard", true, false, eclUserLoginM.getKennung());
    }

    public String doSpeichernStandard() {
        if (uMenueSession.menueWorkflowBasis() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uWorkflowVollmachtenStandard", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "";
        }

        uWorkflowSession.setWiederVerwenden(false);

        eclDbM.openAll();
        eclDbM.openWeitere();
        boolean brc = speichernStandard();

        if (brc == false) {
            eclDbM.closeAll();
            xSessionVerwaltung.setzeEnde();
            return "";
        }

        if (uWorkflowSession.isErweiterteFreigabe() == false) {
            init();
            eclDbM.closeAll();

            return xSessionVerwaltung.setzeUEnde("uWorkflowVollmachtenStandard", true, false,
                    eclUserLoginM.getKennung());
        }

        /*Rückkehr zur Auswahl erweiterteFreigabe*/
        vorbereitenListeFuerAnzeige();
        eclDbM.closeAll();
        uWorkflowSession.clear();
        return xSessionVerwaltung.setzeUEnde("uWorkflowVollmachtenErweitert", true, false, eclUserLoginM.getKennung());
    }

    
    /**Evtl. geladenes Dokument auf Erledigt setzen. Evtl. geladene Elektronische Sachen auf erledigt setzen*/
    public String doSpeichernStandardOhneVeraenderung() {
        if (uMenueSession.menueWorkflowBasis() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uWorkflowVollmachtenStandard", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "";
        }

        uWorkflowSession.setWiederVerwenden(false);

        eclDbM.openAll();
        eclDbM.openWeitere();

        boolean brc=true;
        
        /*Digitale zurücksetzen*/
        updateVorlaeufigeVollmachtEingabe(101);
        if (!eclBestWorkflowM.getDateinameBestaetigung().isEmpty()) {
            eclDbM.getDbBundle().dbBestWorkflow.updateAufNichtVerwenden(eclBestWorkflowM.getIdent());
        }
        
        if (brc == false) {
            eclDbM.closeAll();
            xSessionVerwaltung.setzeEnde();
            return "";
        }

        if (uWorkflowSession.isErweiterteFreigabe() == false) {
            init();
            eclDbM.closeAll();

            return xSessionVerwaltung.setzeUEnde("uWorkflowVollmachtenStandard", true, false,
                    eclUserLoginM.getKennung());
        }

        /*Rückkehr zur Auswahl erweiterteFreigabe*/
        vorbereitenListeFuerAnzeige();
        eclDbM.closeAll();
        uWorkflowSession.clear();
        return xSessionVerwaltung.setzeUEnde("uWorkflowVollmachtenErweitert", true, false, eclUserLoginM.getKennung());
 
    }
    public String doSpeichernFortsetzenStandard() {
        if (uMenueSession.menueWorkflowBasis() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uWorkflowVollmachtenStandard", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "";
        }

        eclDbM.openAll();
        eclDbM.openWeitere();
        boolean brc = speichernStandard();
        eclDbM.closeAll();

        if (brc == false) {
            xSessionVerwaltung.setzeEnde();
            return "";
        }

        /*Nächsten vorbereiten*/
        EclBestWorkflow lBestWorkflow = new EclBestWorkflow();
        lBestWorkflow.mandant = eclBestWorkflowM.getMandant();
        lBestWorkflow.dateinameBestaetigung = eclBestWorkflowM.getDateinameBestaetigung();
        lBestWorkflow.dateinameImportAm = eclBestWorkflowM.getDateinameImportAm();
        lBestWorkflow.origOderKopie=eclBestWorkflowM.getOrigOderKopie();
        lBestWorkflow.subverzeichnis=eclBestWorkflowM.getSubverzeichnis();
        eclBestWorkflowM.copyFrom(lBestWorkflow);
        uWorkflowSession.setWiederVerwenden(true);

        return xSessionVerwaltung.setzeUEnde("uWorkflowVollmachtenStandard", true, false, eclUserLoginM.getKennung());
    }

    private boolean speichernStandard() {
        CaBug.druckeLog("Start", logDrucken, 10);
        EclBestWorkflow lBestWorkflow = new EclBestWorkflow();
        eclBestWorkflowM.copyTo(lBestWorkflow);

        /*Bei der Standard-Prozedur gibts noch keinen Eintrag im VorlBevollmächtigten -> immer neu erzeugen*/
        EclVorlaeufigeVollmacht lVorlaeufigeVollmacht = new EclVorlaeufigeVollmacht();

        int geprueft = lBestWorkflow.pruefstatusErgebnis;

        lVorlaeufigeVollmacht.bevollmaechtigterArt = Integer.parseInt(eclBestWorkflowM.getMitgliedOderSonstiger());
        lVorlaeufigeVollmacht.bevollmaechtigterArtText = eclBestWorkflowM.getBevollmaechtigterArtText();
        if (eclBestWorkflowM.isGesetzlicherVertreter()) {
            lVorlaeufigeVollmacht.bevollmaechtigterAusgefuehrtIstGesetzlich = 1;
        } else {
            lVorlaeufigeVollmacht.bevollmaechtigterAusgefuehrtIstGesetzlich = 0;
        }

        String abgelehntWeil = eclBestWorkflowM.getAbgelehntWeil();
        if (abgelehntWeil != null && !abgelehntWeil.isEmpty()) {
            lVorlaeufigeVollmacht.abgelehntWeil = Integer.parseInt(abgelehntWeil);
        } else {
            lVorlaeufigeVollmacht.abgelehntWeil = 0;
        }
        if (geprueft == 1) {
            lVorlaeufigeVollmacht.abgelehntWeil = 0;
            lVorlaeufigeVollmacht.abgelehntWeilText = "";
        } else {
            lVorlaeufigeVollmacht.abgelehntWeilText = eclBestWorkflowM.getAbgelehntWeilText();
        }

        String kennung = eclBestWorkflowM.getKennung();
        lVorlaeufigeVollmacht.bevollmaechtigterTitel = eclBestWorkflowM.getBevollmaechtigterTitel();
        lVorlaeufigeVollmacht.bevollmaechtigterName = eclBestWorkflowM.getBevollmaechtigterName();
        lVorlaeufigeVollmacht.bevollmaechtigterVorname = eclBestWorkflowM.getBevollmaechtigterVorname();
        lVorlaeufigeVollmacht.bevollmaechtigterStrasse = eclBestWorkflowM.getBevollmaechtigterStrasse();
        lVorlaeufigeVollmacht.bevollmaechtigterPlz = eclBestWorkflowM.getBevollmaechtigterPlz();
        lVorlaeufigeVollmacht.bevollmaechtigterOrt = eclBestWorkflowM.getBevollmaechtigterOrt();
        lVorlaeufigeVollmacht.bevollmaechtigterEMail = eclBestWorkflowM.getBevollmaechtigterEMail();

        lVorlaeufigeVollmacht.eingabeDatum = eclBestWorkflowM.getEingabeDatum();
        lVorlaeufigeVollmacht.eingabeOrt = eclBestWorkflowM.getEingabeOrt();

        if (geprueft == 0) {
            uSession.setFehlermeldung("Bitte Aktion setzen");
            return false;
        }

        if (lBestWorkflow.kommentar.length() > 390) {
            uSession.setFehlermeldung(
                    "Kommentar zu lang - maximal 390 Zeichen - Ist " + lBestWorkflow.kommentar.length() + " Zeichen");
            return false;
        }
        if (lVorlaeufigeVollmacht.abgelehntWeilText.length() > 290) {
            uSession.setFehlermeldung("Begründung für Mitglied - maximal 290 Zeichen - Ist "
                    + lVorlaeufigeVollmacht.abgelehntWeilText.length() + " Zeichen");
            return false;
        }
        if (geprueft == 3 && lBestWorkflow.kommentar.isEmpty()) {
            uSession.setFehlermeldung("Bitte Kommentar eingeben");
            return false;
        }
        if ((geprueft == 2 || geprueft == 4) && lVorlaeufigeVollmacht.abgelehntWeil == 0) {
            uSession.setFehlermeldung("Bitte Abgelehnt weil angeben");
            return false;
        }

        if (eclBestWorkflowM.isKennungGeladen() == false && !uMenueSession.menueWorkflowSpezial() && geprueft == 1) {
            uSession.setFehlermeldung("Vertreter-Neuanlage mit dieser Berechtigung nicht möglich");
            return false;
        }

        if (ParamSpezial.ku178(eclDbM.getDbBundle().clGlobalVar.mandant) && geprueft == 1
                && lVorlaeufigeVollmacht.bevollmaechtigterAusgefuehrtIstGesetzlich == 0
                && eclBestWorkflowM.getGenommenAnzahlVollmachtenNormal() >= 2) {
            uSession.setFehlermeldung(
                    "Vertreter hat bereits 2 'normale' Vollmachten - weitere Vollmacht nicht zulässig");
            return false;
        }
        /*Vollmachtgeber prüfen und in lAktienregisterVollmachtGeber einlesen*/
        String aktionaersnummerVollmachtgeber = lBestWorkflow.zuAktionaersnummer.trim();
        if (aktionaersnummerVollmachtgeber.isEmpty() && geprueft == 1) {
            uSession.setFehlermeldung("Bitte Mitgliedsnummer des Vollmachtgebers eingeben");
            return false;
        }
        EclAktienregister lAktienregisterVollmachtGeber = null;
        EclLoginDaten lLoginDatenVollmachtGeber = null;
        EclMeldung lMeldungVollmachtGeber = null;

        if (!aktionaersnummerVollmachtgeber.isEmpty()) {
            BlNummernformBasis blNummernformBasis = new BlNummernformBasis(eclDbM.getDbBundle());
            aktionaersnummerVollmachtgeber = blNummernformBasis
                    .loginKennungAufbereitenFuerIntern(aktionaersnummerVollmachtgeber);
            aktionaersnummerVollmachtgeber = BlNummernformBasis
                    .aufbereitenFuerDatenbankZugriff(aktionaersnummerVollmachtgeber, eclDbM.getDbBundle());

            int erg = eclDbM.getDbBundle().dbAktienregister.leseZuAktienregisternummer(aktionaersnummerVollmachtgeber);
            if (erg < 1) {
                uSession.setFehlermeldung("Mitglied nicht gefunden!");
                return false;
            }
            lAktienregisterVollmachtGeber = eclDbM.getDbBundle().dbAktienregister.ergebnisPosition(0);

            /*Zugehörige Meldung einlesen (für spätere Vollmachtserteilung)*/
            eclDbM.getDbBundle().dbMeldungen
                    .leseZuAktienregisterIdent(lAktienregisterVollmachtGeber.aktienregisterIdent, true);
            lMeldungVollmachtGeber = eclDbM.getDbBundle().dbMeldungen.meldungenArray[0];

            /*Zugehörige Logindaten einlesen, für E-Mail-Versand*/
            eclDbM.getDbBundle().dbLoginDaten
                    .read_aktienregisterIdent(lAktienregisterVollmachtGeber.aktienregisterIdent);
            lLoginDatenVollmachtGeber = eclDbM.getDbBundle().dbLoginDaten.ergebnisPosition(0);

        }

        int behandelnAlsVollmachtOderNachweis = Integer.parseInt(eclBestWorkflowM.getVollmachtOderNachweis());
        if (geprueft == 1) {
            if (behandelnAlsVollmachtOderNachweis == 1) {
                /*Dann müssen die Felder zwangsweise eingegeben werden*/
                if (lVorlaeufigeVollmacht.bevollmaechtigterArt == 1) {
                    /*Ist Aktionär*/
                    if (eclBestWorkflowM.isKennungGeladen() == false) {
                        uSession.setFehlermeldung(
                                "Bitte Mitgliedsnummer des Bevollmächtigten eingeben und Daten laden");
                        return false;
                    }
                    if (kennung.startsWith("S")) {
                        uSession.setFehlermeldung(
                                "Sonstige Kennung als Bevollmächtigter nicht zulässig, wenn Bevollmächtigter Mitglied ist");
                        return false;
                    }
                } else {
                    /*Sonstiger*/
                    if (eclBestWorkflowM.isKennungGeladen() == true) {
                        if (!kennung.startsWith("S")) {
                            uSession.setFehlermeldung(
                                    "Mitgliedsnummer als Bevollmächtigter nicht zulässig, wenn Bevollmächtigter Sonstiger ist");
                            return false;
                        }
                    }
                    if (lVorlaeufigeVollmacht.bevollmaechtigterArtText.isEmpty()) {
                        uSession.setFehlermeldung("Bitte 'Sonstiger wie folgt' angeben");
                        return false;
                    }
                    if (lVorlaeufigeVollmacht.bevollmaechtigterName.isEmpty()) {
                        uSession.setFehlermeldung("Bitte Name des Bevollmächtigten eingeben");
                        return false;
                    }
                    if (lVorlaeufigeVollmacht.bevollmaechtigterOrt.isEmpty()) {
                        uSession.setFehlermeldung("Bitte Ort des Bevollmächtigten eingeben");
                        return false;
                    }

                }
            }

            //				if (lVorlaeufigeVollmacht.bevollmaechtigterEMail.isEmpty()) {
            //					uSession.setFehlermeldung("Bitte E-Mail des Bevollmächtigten eingeben");
            //					return false;
            //				}
        }

        /*+++++Falls keine Kennung eingegeben, dann neuer Person anlegen. Aber nur, wenn geprüft und akzeptiert++++*/
        int personNatJurNeu = 0;
        if (geprueft == 1) {
            if (!eclBestWorkflowM.isKennungGeladen()) {
                EclMeldung lMeldung = new EclMeldung();
                lMeldung.titel = lVorlaeufigeVollmacht.bevollmaechtigterTitel;
                lMeldung.name = lVorlaeufigeVollmacht.bevollmaechtigterName;
                lMeldung.vorname = lVorlaeufigeVollmacht.bevollmaechtigterVorname;
                lMeldung.strasse = lVorlaeufigeVollmacht.bevollmaechtigterStrasse;
                lMeldung.plz = lVorlaeufigeVollmacht.bevollmaechtigterPlz;
                lMeldung.ort = lVorlaeufigeVollmacht.bevollmaechtigterOrt;
                lMeldung.mailadresse = lVorlaeufigeVollmacht.bevollmaechtigterEMail;
                BlGastkarte blGastkarte = new BlGastkarte(eclDbM.getDbBundle());
                blGastkarte.pGast = lMeldung;
                blGastkarte.pVersandart = 3;
                blGastkarte.ausstellen();
                personNatJurNeu = blGastkarte.pGast.personenNatJurIdent;

                String mailEmpfaenger = lVorlaeufigeVollmacht.bevollmaechtigterEMail;
                if (!mailEmpfaenger.isEmpty()) {
                    /*Kennung und Passwort-Mail*/
                    /*Mail für Kennung*/
                    String mailBetreff = eclTextePortalM.holeText("1026");
                    String hKennung=blGastkarte.rcKennung;
                    if (blGastkarte.rcKennungAlternativ.isEmpty()==false) {
                        hKennung=blGastkarte.rcKennungAlternativ;
                    }
                    String mailText = eclTextePortalM.holeText("1027") + hKennung
                            + eclTextePortalM.holeText("1028");
                    baMailM.senden(mailEmpfaenger, mailBetreff, mailText);

                    /*Mail für Passwort*/
                    mailBetreff = eclTextePortalM.holeText("1029");
                    mailText = eclTextePortalM.holeText("1030") + blGastkarte.rcPasswort
                            + eclTextePortalM.holeText("1031");
                    baMailM.senden(mailEmpfaenger, mailBetreff, mailText);
                } else {
                    /*Kennungs-/Passwort-Anschreiben*/
                    EclAufgaben lAufgabe = new EclAufgaben();
                    lAufgabe.mandant = eclDbM.getDbBundle().clGlobalVar.mandant;
                    lAufgabe.aufgabe = KonstAufgaben.aktionaerNeuesPasswortAdressePruefen;
                    lAufgabe.zeitpunktErteilt = CaDatumZeit.DatumZeitStringFuerDatenbank();
                    lAufgabe.anforderer = KonstAufgabenAnforderer.neueVollmacht;
                    lAufgabe.status = KonstAufgabenStatus.geprueft;
                    lAufgabe.argument[0] = blGastkarte.rcKennung;
                    eclDbM.getDbBundle().dbAufgaben.insert(lAufgabe);
                }

            }
        }

        /**+++++Nun vorläufige Vollmacht speichern - ident wird für EclBestWorkflow benötigt++++++*/
        /*storniert*/
        if (geprueft == 1) {
            if (behandelnAlsVollmachtOderNachweis == 1) {
                lVorlaeufigeVollmacht.storniert = 0;
            } else {
                lVorlaeufigeVollmacht.storniert = 2;
            }
        } else {
            lVorlaeufigeVollmacht.storniert = 4;
        }
        /*erteiltVonArt = Mitglied*/
        lVorlaeufigeVollmacht.erteiltVonArt = 1;
        /*erteiltVonIdent. Kann 0 sein, wenn noch keine Mitgliedsnummer eingegeben wurde*/
        if (lAktienregisterVollmachtGeber != null) {
            lVorlaeufigeVollmacht.erteiltVonIdent = lAktienregisterVollmachtGeber.aktienregisterIdent;
        } else {
            lVorlaeufigeVollmacht.erteiltVonIdent = 0;
        }
        /*eMailVollmachtgeber*/
        if (lVorlaeufigeVollmacht.erteiltVonIdent != 0) {
            eclDbM.getDbBundle().dbLoginDaten.read_loginKennung(lAktienregisterVollmachtGeber.aktionaersnummer);
            lVorlaeufigeVollmacht.eMailVollmachtgeber = eclDbM.getDbBundle().dbLoginDaten
                    .ergebnisPosition(0).eMailFuerVersand;
        }

        /*pruefstatus*/
        if (geprueft == 1) {
            lVorlaeufigeVollmacht.pruefstatus = 2;
        }
        if (geprueft == 2) {
            lVorlaeufigeVollmacht.pruefstatus = 3;
            lVorlaeufigeVollmacht.storniert = 3;
        }
        if (geprueft == 3) {
            lVorlaeufigeVollmacht.pruefstatus = 1;
        }
        if (geprueft == 4) {
            lVorlaeufigeVollmacht.pruefstatus = 3;
        }

        /*bevollmaechtigterAusgefuehrtArt*/
        lVorlaeufigeVollmacht.bevollmaechtigterAusgefuehrtArt = lVorlaeufigeVollmacht.bevollmaechtigterArt;

        /*bevollmaechtigterAusgefuehrtIdent*/
        if (lVorlaeufigeVollmacht.bevollmaechtigterAusgefuehrtArt == 1) {
            /*AktienregisterIdent*/
            lVorlaeufigeVollmacht.bevollmaechtigterAusgefuehrtIdent = eclBestWorkflowM.getKennungGeladenIdent();
        } else {
            /*PersonNAtJur*/
            if (personNatJurNeu == 0) {
                lVorlaeufigeVollmacht.bevollmaechtigterAusgefuehrtIdent = eclBestWorkflowM.getKennungGeladenIdent();
            } else {
                lVorlaeufigeVollmacht.bevollmaechtigterAusgefuehrtIdent = personNatJurNeu;
            }
        }
        if (geprueft != 3) {
            eclDbM.getDbBundle().dbVorlaeufigeVollmacht.insert(lVorlaeufigeVollmacht);
        }

        /*Vollmacht als Willenserklärung speichern*/
        if (geprueft == 1 && behandelnAlsVollmachtOderNachweis == 1) {
            int vollmachtEmpfaengerPersonIdent = 0;
            if (personNatJurNeu != 0) {
                vollmachtEmpfaengerPersonIdent = personNatJurNeu;
            } else {
                if (lVorlaeufigeVollmacht.bevollmaechtigterAusgefuehrtArt == 1) {
                    /*AktienregisterIdent*/
                    vollmachtEmpfaengerPersonIdent = eclBestWorkflowM.getPersonNatJurGeladenIdent();
                } else {
                    /*PersonNAtJur*/
                    vollmachtEmpfaengerPersonIdent = eclBestWorkflowM.getKennungGeladenIdent();
                }
            }
            BlWillenserklaerung vmWillenserklaerung = new BlWillenserklaerung();
            vmWillenserklaerung.pQuelle = eclBestWorkflowM.getSubverzeichnis() + "//"
                    + eclBestWorkflowM.getDateinameBestaetigung();
            vmWillenserklaerung.pErteiltAufWeg = KonstEingabeQuelle.papierPost_ausserhalbHV;
            vmWillenserklaerung.pErteiltZeitpunkt = "";

            vmWillenserklaerung.piMeldungsIdentAktionaer = lMeldungVollmachtGeber.meldungsIdent;
            vmWillenserklaerung.pWillenserklaerungGeberIdent = -1; /*Aktionär*/

            EclPersonenNatJur personNatJur = new EclPersonenNatJur();
            ;
            personNatJur.ident = vollmachtEmpfaengerPersonIdent;
            vmWillenserklaerung.pEclPersonenNatJur = personNatJur;

            vmWillenserklaerung.vollmachtAnDritte(eclDbM.getDbBundle());

            if (vmWillenserklaerung.rcIstZulaessig == false) {
                CaBug.drucke(
                        "001 vmWillenserklaerung.rcGrundFuerUnzulaessig=" + vmWillenserklaerung.rcGrundFuerUnzulaessig);
            }
        }

        CaBug.druckeLog("geprueft=" + geprueft, logDrucken, 10);
        if (geprueft != 3) {
            CaBug.druckeLog("im if geprueft!=3", logDrucken, 10);
            lBestWorkflow.vorlVollmachtIdent = lVorlaeufigeVollmacht.ident;
            lBestWorkflow.zuAktionaersnummer = aktionaersnummerVollmachtgeber; //Aufbereitet für Intern
        }
        CaBug.druckeLog("nach if", logDrucken, 10);

        lBestWorkflow.inBearbeitungDurchUserNr = 0;
        lBestWorkflow.pruefstatusVorgang = lBestWorkflow.pruefstatusErgebnis;
        lBestWorkflow.bearbeitungsZeit = CaDatumZeit.DatumZeitStringFuerDatenbank();
        lBestWorkflow.bearbeitungsUser = eclUserLoginM.getUserLoginIdent();
        lBestWorkflow.bearbeitungsUserName = eclUserLoginM.getKennung();

        if (uWorkflowSession.isWiederVerwenden()) {
            eclDbM.getDbBundle().dbBestWorkflow.insert(lBestWorkflow);
        } else {
            eclDbM.getDbBundle().dbBestWorkflow.update(lBestWorkflow);
        }

        /*Besitzart updaten - nur bei gesetzlicher Vollmacht
         * V=Es wurde eine gültige gesetzliche Vertretungsvollmacht eingegeben
         * F=es wurde eine gesetzliche Vertretungsvollmacht eingegeben aber abgelehnt*/
        if (!aktionaersnummerVollmachtgeber.isEmpty()) {
            if (lVorlaeufigeVollmacht.bevollmaechtigterAusgefuehrtIstGesetzlich == 1) {
                if (lBestWorkflow.pruefstatusErgebnis == 1) {
                    lAktienregisterVollmachtGeber.besitzart = "V";
                    eclDbM.getDbBundle().dbAktienregister.update(lAktienregisterVollmachtGeber);
                }
                if (lBestWorkflow.pruefstatusErgebnis == 2) {
                    if (lAktienregisterVollmachtGeber.besitzart.equals("E")) {
                        lAktienregisterVollmachtGeber.besitzart = "F";
                        eclDbM.getDbBundle().dbAktienregister.update(lAktienregisterVollmachtGeber);
                    }
                }
            }

        }

//        if (geprueft == 1 || geprueft == 2) {
//            if (ParamSpezial.ku178(eclDbM.getDbBundle().clGlobalVar.mandant)) {
//                /*Mails verschicken an Bevollmächtigten und Aktionär, soweit E-Mail-Adressen bekannt*/
//                String mailEmpfaengerBevollmaechtiger = lVorlaeufigeVollmacht.bevollmaechtigterEMail;
//                if (!mailEmpfaengerBevollmaechtiger.isEmpty()) {
//                    /*Mail an Bevollmächtigtem*/
//                    if (geprueft == 1) {
//                        String mailBetreff = eclTextePortalM.holeText("1114");
//                        String mailText = eclTextePortalM.holeText("1115");
//                        baMailM.senden(mailEmpfaengerBevollmaechtiger, mailBetreff, mailText);
//                    } else {
//                        String mailBetreff = eclTextePortalM.holeText("1116");
//                        String mailText = eclTextePortalM.holeText("1117");
//                        baMailM.senden(mailEmpfaengerBevollmaechtiger, mailBetreff, mailText);
//                    }
//                }
//                if (lLoginDatenVollmachtGeber != null) {
//                    String mailEmpfaengerVollmachtGeber = lLoginDatenVollmachtGeber.eMailFuerVersand;
//                    if (!mailEmpfaengerVollmachtGeber.isEmpty()) {
//                        /*Mail an Vollmachtgeber*/
//                        if (geprueft == 1) {
//                            String mailBetreff = eclTextePortalM.holeText("1118");
//                            String mailText = eclTextePortalM.holeText("1119");
//                            baMailM.senden(mailEmpfaengerVollmachtGeber, mailBetreff, mailText);
//                        } else {
//                            String mailBetreff = eclTextePortalM.holeText("1120");
//                            String mailText = eclTextePortalM.holeText("1121");
//                            baMailM.senden(mailEmpfaengerVollmachtGeber, mailBetreff, mailText);
//                        }
//                    }
//                }
//            }
//        }
        
        
        /*Nun noch die elektronisch erteilten und in Bearbeitung befindlichen Sätze freigeben und Prüfstatus setzen*/
        if (lBestWorkflow.pruefstatusVorgang==3) {
            updateVorlaeufigeVollmachtEingabe(102);
        }
        else {
            updateVorlaeufigeVollmachtEingabe(101);
        }
        
        CaBug.druckeLog("Ende", logDrucken, 10);
        return true;

    }

    
    /**pNeuerStatus=101 oder 103;
     * inBearbeitungDurchUserNr wird wieder auf 0 gesetzt
     */
    private void updateVorlaeufigeVollmachtEingabe(int pNeuerStatus) {
        List<EhVorlaeufigeVollmacht> ehVorlaeufigeVollmachtList=uWorkflowSession.getVorhandeneVorgaenge();
        if (ehVorlaeufigeVollmachtList!=null) {
            for (EhVorlaeufigeVollmacht iEhVorlaeufigeVollmacht:ehVorlaeufigeVollmachtList) {
                if (iEhVorlaeufigeVollmacht.art!=0) {
                    eclDbM.getDbBundle().dbVorlaeufigeVollmachtEingabe.updateUndGebeFrei(iEhVorlaeufigeVollmacht.eclVorlaeufigeVollmachtEingabe, pNeuerStatus);
                }
            }
        }
    }
    
    
    public String doAbbrechenStandard() {
        if (uMenueSession.menueWorkflowBasis() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uWorkflowVollmachtenStandard", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "";
        }

        eclDbM.openAll();
        eclDbM.openWeitere();
        EclBestWorkflow lBestWorkflow = new EclBestWorkflow();
        eclBestWorkflowM.copyTo(lBestWorkflow);
        eclDbM.getDbBundle().dbBestWorkflow.readZuident(lBestWorkflow.ident);
        lBestWorkflow = eclDbM.getDbBundle().dbBestWorkflow.ergebnisArray[0];
        lBestWorkflow.inBearbeitungDurchUserNr = 0;
        eclDbM.getDbBundle().dbBestWorkflow.update(lBestWorkflow);
        

        
        
        if (uWorkflowSession.isErweiterteFreigabe() == false) {
            /*Nun noch die elektronisch erteilten und in Bearbeitung befindlichen Sätze freigeben und Prüfstatus setzen*/
            updateVorlaeufigeVollmachtEingabe(0);
            
            eclDbM.closeAll();
            uWorkflowSession.clear();
            uWorkflowSession.setBasisStatus(0);
            return xSessionVerwaltung.setzeUEnde("uWorkflowVollmachtenStandard", true, false,
                    eclUserLoginM.getKennung());
        }

        /*Rückkehr zur Auswahl erweiterteFreigabe*/
        /*Nun noch die elektronisch erteilten und in Bearbeitung befindlichen Sätze freigeben und Prüfstatus setzen*/
        updateVorlaeufigeVollmachtEingabe(102);

        vorbereitenListeFuerAnzeige();
        eclDbM.closeAll();
        uWorkflowSession.clear();
        return xSessionVerwaltung.setzeUEnde("uWorkflowVollmachtenErweitert", true, false, eclUserLoginM.getKennung());
    }

    public String doStornierenStandard(EclBestWorkflowVorlVollmacht pBestWorkflowVorlVollmacht) {
        if (uMenueSession.menueWorkflowBasis() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uWorkflowVollmachtenStandard", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "";
        }
        eclDbM.openAll();
        eclDbM.openWeitere();

        if (ParamSpezial.ku178(eclDbM.getDbBundle().clGlobalVar.mandant)) {
            /*Mail an Vollmacht-Empfänger, so eingetragen*/
            String mailEmpfaengerBevollmaechtiger = pBestWorkflowVorlVollmacht.eclVorlVollmacht.bevollmaechtigterEMail;
            if (!mailEmpfaengerBevollmaechtiger.isEmpty()) {
                /*Mail an Bevollmächtigtem*/
                String mailBetreff = eclTextePortalM.holeText("1122");
                String mailText = eclTextePortalM.holeText("1123");
                baMailM.senden(mailEmpfaengerBevollmaechtiger, mailBetreff, mailText);
            }
        }

        BlVorlaeufigeVollmacht blVorlaeufigeVollmacht = new BlVorlaeufigeVollmacht(eclDbM.getDbBundle());
        blVorlaeufigeVollmacht.storniere(pBestWorkflowVorlVollmacht.eclVorlVollmacht,
                KonstWillenserklaerungWeg.anmeldestelleManuell);

        /*Nun aktuellen Stand wieder einlesen*/
        boolean bRc = leseAlleVollmachtenZuGeber(eclBestWorkflowM.getIdentMitglied());
        eclDbM.closeAll();

        if (bRc == false) {
            uSession.setFehlermeldung(
                    "Vollmachtgeber hat bereits Vollmacht erteilt! Diese muß vor einer weiteren Vollmacht erst storniert werden.");
            uWorkflowSession.setBereitsVollmachtVorhanden(true);
            xSessionVerwaltung.setzeUEnde();
            return "";
        }
        uWorkflowSession.setBereitsVollmachtVorhanden(false);
        return xSessionVerwaltung.setzeUEnde("uWorkflowVollmachtenStandard", true, false, eclUserLoginM.getKennung());
    }

    private boolean leseAlleVollmachtenZuGeber(int pAktienregisterIdent) {
        BlVorlaeufigeVollmacht blVorlaeufigeVollmacht = new BlVorlaeufigeVollmacht(eclDbM.getDbBundle());
        blVorlaeufigeVollmacht.liefereAusgehendeVollmachtenVonAktionaer(pAktienregisterIdent);
        blVorlaeufigeVollmacht.ergaenzeWorkflowDaten();
        List<EclBestWorkflowVorlVollmacht> bestWorkflowVorlVollmachtListe = new LinkedList<EclBestWorkflowVorlVollmacht>();
        for (int i = 0; i < blVorlaeufigeVollmacht.rcListAlleVollmachten.size(); i++) {
            EclBestWorkflowVorlVollmacht bestWorkflowVorlVollmacht = new EclBestWorkflowVorlVollmacht();
            bestWorkflowVorlVollmacht.eclBestWorkflow = blVorlaeufigeVollmacht.rcListAlleBestWorkflow.get(i);
            bestWorkflowVorlVollmacht.eclVorlVollmacht = blVorlaeufigeVollmacht.rcListAlleVollmachten.get(i);
            bestWorkflowVorlVollmachtListe.add(bestWorkflowVorlVollmacht);
        }
        uWorkflowSession.setBestWorkflowVorlVollmachtListe(bestWorkflowVorlVollmachtListe);
        uWorkflowSession.setAnzeigeInfoBereich(1);

        if (blVorlaeufigeVollmacht.rcVollmachtGueltigAnDritteEingetragenVorhanden) {
            return false;
        }
        return true;
    }

    /************************************doWorkflowVollmachtspruefungErweitert (Spezial)*************************/
    public void initSpezial() {
        uWorkflowSession.setAnzeigeErwWorkflow("3");
        uWorkflowSession.setAnzeigeNurUnQS(true);
        uWorkflowSession.setSucheMitgliedsnummer("");
        vorbereitenListeFuerAnzeige();
    }

    public void vorbereitenListeFuerAnzeige() {

        List<EclBestWorkflowM> listSuchergebnis = new LinkedList<EclBestWorkflowM>();
        String sucheAktionaersnummer = uWorkflowSession.getSucheMitgliedsnummer().trim();
        boolean notQS = false;
        if (uWorkflowSession.isAnzeigeNurUnQS()) {
            notQS = true;
        }

        if (sucheAktionaersnummer.isEmpty()) {
            /*Suche nach Status*/
            int hStatus = Integer.parseInt(uWorkflowSession.getAnzeigeErwWorkflow());
            eclDbM.getDbBundle().dbBestWorkflow.readVorgaengeZuident(hStatus, notQS, "");
        } else {
            /*Suche nach Mitgliedsnummer*/
            int hStatus = 99;
            if (!CaString.isNummern(sucheAktionaersnummer)) {
                uSession.setFehlermeldung("Bitte gültige Mitgliedsnummer eingeben");
                return;
            }
            BlNummernformBasis blNummernformBasis = new BlNummernformBasis(eclDbM.getDbBundle());
            sucheAktionaersnummer = blNummernformBasis.loginKennungAufbereitenFuerIntern(sucheAktionaersnummer);
            CaBug.druckeLog("sucheAktionaersnummer=" + sucheAktionaersnummer, logDrucken, 10);

            eclDbM.getDbBundle().dbBestWorkflow.readVorgaengeZuident(hStatus, notQS, sucheAktionaersnummer);
        }

        for (int i = 0; i < eclDbM.getDbBundle().dbBestWorkflow.anzErgebnis(); i++) {
            EclBestWorkflow lBestWorkFlow = eclDbM.getDbBundle().dbBestWorkflow.ergebnisArray[i];

            boolean storniert = false;
            CaBug.druckeLog("lBestWorkFlow.ident=" + lBestWorkFlow.ident, logDrucken, 10);
            CaBug.druckeLog("lBestWorkFlow.vorlVollmachtIdent=" + lBestWorkFlow.vorlVollmachtIdent, logDrucken, 10);
            CaBug.druckeLog("lBestWorkFlow.dateinameBestaetigung=" + lBestWorkFlow.dateinameBestaetigung, logDrucken, 10);
            if (lBestWorkFlow.vorlVollmachtIdent != 0) {
                eclDbM.getDbBundle().dbVorlaeufigeVollmacht.readIdent(lBestWorkFlow.vorlVollmachtIdent);
                if (eclDbM.getDbBundle().dbVorlaeufigeVollmacht.ergebnisPosition(0).storniert == 1) {
                    storniert = true;
                }
            }

            EclBestWorkflowM lBestWorkFlowM = new EclBestWorkflowM();
            lBestWorkFlowM.copyFrom(lBestWorkFlow);
            if (lBestWorkFlow.vorlVollmachtIdent != 0) {
                lBestWorkFlowM.setZuAktionaersnummer(CaString.ku178InternZuEingabe(lBestWorkFlow.zuAktionaersnummer));
            } else {
                lBestWorkFlowM.setZuAktionaersnummer("");
            }
            lBestWorkFlowM.setStorniert(storniert);
            CaBug.druckeLog("i=" + i + " storniert=" + storniert, logDrucken, 10);
            listSuchergebnis.add(lBestWorkFlowM);

        }

        uWorkflowSession.setListSuchergebnis(listSuchergebnis);

    }

    /*+++++++++++++Auswahlmaske++++++ü*/
    public String doRefreshErweitert() {
        if (uMenueSession.menueWorkflowSpezial() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uWorkflowVollmachtenErweitert", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "";
        }
        eclDbM.openAll();
        eclDbM.openWeitere();
        vorbereitenListeFuerAnzeige();
        eclDbM.closeAll();
        return xSessionVerwaltung.setzeUEnde("uWorkflowVollmachtenErweitert", true, false, eclUserLoginM.getKennung());
    }

    public String doDetailsErweitert(EclBestWorkflowM bestWorkflow) {
        if (uMenueSession.menueWorkflowSpezial() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uWorkflowVollmachtenErweitert", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "";
        }
        eclDbM.openAll();
        eclDbM.openWeitere();


        
        
        /*Workflow-Satz einlesen*/
        long identBestWorkflow = (int) bestWorkflow.getIdent();
        eclDbM.getDbBundle().dbBestWorkflow.readZuident(identBestWorkflow);
        EclBestWorkflow lBestWorkflow = eclDbM.getDbBundle().dbBestWorkflow.ergebnisPosition(0);

        
        /*Vorläufige Vollmacht dazu einlesen*/
        EclVorlaeufigeVollmacht lVorlaeufigeVollmacht = null;
        boolean storniert = false;
        boolean stornierbar = false;
        if (lBestWorkflow.vorlVollmachtIdent != 0) {
            eclDbM.getDbBundle().dbVorlaeufigeVollmacht.readIdent(lBestWorkflow.vorlVollmachtIdent);
            lVorlaeufigeVollmacht = eclDbM.getDbBundle().dbVorlaeufigeVollmacht.ergebnisPosition(0);
            if (lVorlaeufigeVollmacht.storniert == 1) {
                storniert = true;
            }
            if (lVorlaeufigeVollmacht.storniert == 0) {
                stornierbar = true;
            }
        }

        /*Geber + Nehmer einlesen, wenn vorläufige Vollmacht schon vorhanden */
        EclAktienregister lAktienregisterGeber = null;
        EclAktienregister lAktienregisterNehmer = null;
        EclPersonenNatJur lPersonenNatJurNehmer = null;
        EclLoginDaten lLoginDatenNehmer = null;

        if (lVorlaeufigeVollmacht != null) {
            /*Geber einlesen*/
            if (lVorlaeufigeVollmacht.erteiltVonIdent != 0) {
                CaBug.druckeLog("lVorlaeufigeVollmacht.erteiltVonIdent=" + lVorlaeufigeVollmacht.erteiltVonIdent,
                        logDrucken, 10);
                eclDbM.getDbBundle().dbAktienregister.leseZuAktienregisterIdent(lVorlaeufigeVollmacht.erteiltVonIdent);
                lAktienregisterGeber = eclDbM.getDbBundle().dbAktienregister.ergebnisPosition(0);
            }
            /*Nehmer einlesen*/
            if (lVorlaeufigeVollmacht.bevollmaechtigterAusgefuehrtArt == 1) {
                if (lVorlaeufigeVollmacht.bevollmaechtigterAusgefuehrtIdent != 0) {
                    CaBug.druckeLog("lVorlaeufigeVollmacht.bevollmaechtigterAusgefuehrtArt="
                            + lVorlaeufigeVollmacht.bevollmaechtigterAusgefuehrtArt, logDrucken, 10);
                    eclDbM.getDbBundle().dbAktienregister
                            .leseZuAktienregisterIdent(lVorlaeufigeVollmacht.bevollmaechtigterAusgefuehrtIdent);
                    lAktienregisterNehmer = eclDbM.getDbBundle().dbAktienregister.ergebnisPosition(0);

                    eclDbM.getDbBundle().dbLoginDaten
                            .read_aktienregisterIdent(lVorlaeufigeVollmacht.bevollmaechtigterAusgefuehrtIdent);
                    lLoginDatenNehmer = eclDbM.getDbBundle().dbLoginDaten.ergebnisPosition(0);
                }
            }
            if (lVorlaeufigeVollmacht.bevollmaechtigterAusgefuehrtArt == 2) {
                if (lVorlaeufigeVollmacht.bevollmaechtigterAusgefuehrtIdent != 0) {
                    eclDbM.getDbBundle().dbPersonenNatJur.read(lVorlaeufigeVollmacht.bevollmaechtigterAusgefuehrtIdent);
                    lPersonenNatJurNehmer = eclDbM.getDbBundle().dbPersonenNatJur.PersonNatJurGefunden(0);

                    eclDbM.getDbBundle().dbLoginDaten
                            .read_personNatJurIdent(lVorlaeufigeVollmacht.bevollmaechtigterAusgefuehrtIdent);
                    lLoginDatenNehmer = eclDbM.getDbBundle().dbLoginDaten.ergebnisPosition(0);
                }
            }
        }

        if (bestWorkflow.getPruefstatusErgebnis().equals("3")) {
            /**Wiedervorlage-Prozess durchführen*/
            /*in lBestWorkflow steht der zu bearbeitende Workflow-Satz*/
            eclBestWorkflowM.copyFrom(eclDbM.getDbBundle().dbBestWorkflow.ergebnisArray[0]);
            uWorkflowSession.setBasisStatus(1);
            uWorkflowSession.setWiederVerwenden(false);
            eclDbM.closeAll();
            uWorkflowSession.setErweiterteFreigabe(true);
            uWorkflowSession.setVorhandeneVorgaenge(null);
            return xSessionVerwaltung.setzeUEnde("uWorkflowVollmachtenStandard", true, false,
                    eclUserLoginM.getKennung());
        }

        /*Detail-Read-Only-Maske anrufen*/
        eclBestWorkflowM.copyFrom(lBestWorkflow);
        if (lAktienregisterGeber != null) {
            eclBestWorkflowM.setZuAktionaersnummer(BlNummernformBasis
                    .aufbereitenInternFuerExtern(lAktienregisterGeber.aktionaersnummer, eclDbM.getDbBundle()));
            eclBestWorkflowM.setNameMitglied(lAktienregisterGeber.nameKomplett);
            eclBestWorkflowM.setIdentMitglied(lAktienregisterGeber.aktienregisterIdent);
        }

        if (lAktienregisterNehmer != null) {
            int ident = lLoginDatenNehmer.aktienregisterIdent;
            eclBestWorkflowM.setBevollmaechtigterTitel(lAktienregisterNehmer.titel);
            eclBestWorkflowM.setBevollmaechtigterName(lAktienregisterNehmer.liefereName());
            eclBestWorkflowM.setBevollmaechtigterVorname(lAktienregisterNehmer.vorname);
            eclBestWorkflowM.setBevollmaechtigterStrasse(lAktienregisterNehmer.strasse);
            eclBestWorkflowM.setBevollmaechtigterPlz(lAktienregisterNehmer.postleitzahl);
            eclBestWorkflowM.setBevollmaechtigterOrt(lAktienregisterNehmer.ort);
            eclBestWorkflowM.setBevollmaechtigterEMail(lLoginDatenNehmer.eMailFuerVersand);

            eclBestWorkflowM.setMitgliedOderSonstiger("1");
            eclBestWorkflowM.setKennungGeladenIst(KonstLoginKennungArt.aktienregister);
            eclBestWorkflowM.setKennungGeladenIdent(ident);
            eclBestWorkflowM.setPersonNatJurGeladenIdent(lAktienregisterNehmer.personNatJur);

            eclBestWorkflowM.setKennung(BlNummernformBasis
                    .aufbereitenInternFuerExtern(lAktienregisterNehmer.aktionaersnummer, eclDbM.getDbBundle()));

            /*Nun noch Anzahl Vertretungen einlesen*/
            BlVorlaeufigeVollmacht blVorlaeufigeVollmacht = new BlVorlaeufigeVollmacht(eclDbM.getDbBundle());
            blVorlaeufigeVollmacht.liefereAnzahlVollmachtenZuBevollmaechtigten(ident, 1);
            eclBestWorkflowM.setHinweisAnzahlVollmachten("Anzahl gesetzlicher Vollmachten: "
                    + Integer.toString(blVorlaeufigeVollmacht.rcAnzGesetzlicheVollmachten)
                    + " Anzahl sonstiger Vollmachten: "
                    + Integer.toString(blVorlaeufigeVollmacht.rcAnzSonstigeVollmachten));
            eclBestWorkflowM.setGenommenAnzahlVollmachtenGesetzlich(blVorlaeufigeVollmacht.rcAnzGesetzlicheVollmachten);
            eclBestWorkflowM.setGenommenAnzahlVollmachtenNormal(blVorlaeufigeVollmacht.rcAnzSonstigeVollmachten);
        }
        if (lPersonenNatJurNehmer != null) {
            int ident = lLoginDatenNehmer.personenNatJurIdent;
            eclBestWorkflowM.setBevollmaechtigterTitel(lPersonenNatJurNehmer.titel);
            eclBestWorkflowM.setBevollmaechtigterName(lPersonenNatJurNehmer.name);
            eclBestWorkflowM.setBevollmaechtigterVorname(lPersonenNatJurNehmer.vorname);
            eclBestWorkflowM.setBevollmaechtigterStrasse(lPersonenNatJurNehmer.strasse);
            eclBestWorkflowM.setBevollmaechtigterPlz(lPersonenNatJurNehmer.plz);
            eclBestWorkflowM.setBevollmaechtigterOrt(lPersonenNatJurNehmer.ort);
            eclBestWorkflowM.setBevollmaechtigterEMail(lLoginDatenNehmer.eMailFuerVersand);

            eclBestWorkflowM.setMitgliedOderSonstiger("2");
            eclBestWorkflowM.setKennungGeladenIst(KonstLoginKennungArt.personenNatJur);
            eclBestWorkflowM.setKennungGeladenIdent(ident);
            eclBestWorkflowM.setPersonNatJurGeladenIdent(0);

            eclBestWorkflowM.setKennung(lLoginDatenNehmer.loginKennung);

            /*Nun noch Anzahl Vertretungen einlesen*/
            BlVorlaeufigeVollmacht blVorlaeufigeVollmacht = new BlVorlaeufigeVollmacht(eclDbM.getDbBundle());
            blVorlaeufigeVollmacht.liefereAnzahlVollmachtenZuBevollmaechtigten(ident, 2);
            eclBestWorkflowM.setHinweisAnzahlVollmachten("Anzahl gesetzlicher Vollmachten: "
                    + Integer.toString(blVorlaeufigeVollmacht.rcAnzGesetzlicheVollmachten)
                    + " Anzahl sonstiger Vollmachten: "
                    + Integer.toString(blVorlaeufigeVollmacht.rcAnzSonstigeVollmachten));
            eclBestWorkflowM.setGenommenAnzahlVollmachtenGesetzlich(blVorlaeufigeVollmacht.rcAnzGesetzlicheVollmachten);
            eclBestWorkflowM.setGenommenAnzahlVollmachtenNormal(blVorlaeufigeVollmacht.rcAnzSonstigeVollmachten);
        }

        if (lVorlaeufigeVollmacht != null) {
            eclBestWorkflowM.copyRestFrom(lVorlaeufigeVollmacht);
        }
        
        if (lAktienregisterGeber!=null) {
            CaBug.druckeLog("lAktienregisterGeber!=null", logDrucken, 10);
            /*Bereits vorhandene Datensätze aus Workflow (Papier und Digital) holen - Digital-Sätze reservieren*/
            BlVorlaeufigeVollmacht blVorlaeufigeVollmacht=new BlVorlaeufigeVollmacht(eclDbM.getDbBundle());
            blVorlaeufigeVollmacht.belegeListeVorlaeufigeVollmachten(lAktienregisterGeber.aktionaersnummer, lAktienregisterGeber.aktienregisterIdent);
            uWorkflowSession.setVorhandeneVorgaenge(blVorlaeufigeVollmacht.rcEhVorlaeufigeVollmachtList);
        }

        eclBestWorkflowM.setStorniert(storniert);
        eclBestWorkflowM.setStornierbar(stornierbar);

        eclDbM.closeAll();
        return xSessionVerwaltung.setzeUEnde("uWorkflowVollmachtenDetail", true, false, eclUserLoginM.getKennung());
    }

    /*++++++++++++++++++Detail-Anzeige+++++++++++++++++++++++++++++++++*/
    public String doVollmachtStornierenDetail() {
        if (uMenueSession.menueWorkflowSpezial() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uWorkflowVollmachtenDetail", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "";
        }
        eclDbM.openAll();
        eclDbM.openWeitere();

        int identVorlaeufigeVollmacht = (int) eclBestWorkflowM.getIdentVorlauefigeVollmacht();
        eclDbM.getDbBundle().dbVorlaeufigeVollmacht.readIdent(identVorlaeufigeVollmacht);
        EclVorlaeufigeVollmacht lVorlaeufigeVollmacht = eclDbM.getDbBundle().dbVorlaeufigeVollmacht.ergebnisPosition(0);

        if (ParamSpezial.ku178(eclDbM.getDbBundle().clGlobalVar.mandant)) {
            /*Mail an Vollmacht-Empfänger, so eingetragen*/
            String mailEmpfaengerBevollmaechtiger = lVorlaeufigeVollmacht.bevollmaechtigterEMail;
            if (!mailEmpfaengerBevollmaechtiger.isEmpty()) {
                /*Mail an Bevollmächtigtem*/
                String mailBetreff = eclTextePortalM.holeText("1122");
                String mailText = eclTextePortalM.holeText("1123");
                baMailM.senden(mailEmpfaengerBevollmaechtiger, mailBetreff, mailText);
            }
        }

        BlVorlaeufigeVollmacht blVorlaeufigeVollmacht = new BlVorlaeufigeVollmacht(eclDbM.getDbBundle());
        blVorlaeufigeVollmacht.storniere(lVorlaeufigeVollmacht, KonstWillenserklaerungWeg.anmeldestelleManuell);

        vorbereitenListeFuerAnzeige();
        eclDbM.closeAll();
        return xSessionVerwaltung.setzeUEnde("uWorkflowVollmachtenErweitert", true, false, eclUserLoginM.getKennung());

    }

    /************************************doWorkflowVollmachtspruefungStatistik*************************/
    /*XXX*/

    /**********************Importieren - direkt aus Menü********************************/
    /**Importieren der PDFs zur Weiterverarbeitung*/
    public void importierenPdf() {
        eclDbM.openAll();

        BlBestaetigungsWorkflow blBestaetigungsWorkflow = new BlBestaetigungsWorkflow(eclDbM.getDbBundle());

        int anzImportiert = blBestaetigungsWorkflow.importiereBestaetigungsPdf();
        int anzInsgesamt = blBestaetigungsWorkflow.rcDateienInsgesamtImVerzeichnis;

        eclDbM.closeAll();
        uSession.setFehlermeldung(
                anzImportiert + " Workflow-PDFs importiert (" + anzInsgesamt + " Dateien insgesamt im Verzeichnis)");

    }

    
    /*==========================================================Worflowprüfung ku216===========================================================*/
    /**Open erfolgt in aufrufender Funktion*/
    
    /*uWorkflowGesetzlicheVertreter uWorkflowVollmachten*/
    public void initGesetzlicheVertreter() {
        BlBestaetigungsWorkflow blBestaetigungsWorkflow=new BlBestaetigungsWorkflow(eclDbM.getDbBundle());
        blBestaetigungsWorkflow.initGesetzlicheVertreter(false);
        
        uWorkflowSession.setWorkflowManuellOhneAufHinterlegteGepruefte(false);
        uWorkflowSession.setWorkflowManuellGesetzlich(true);
        uWorkflowSession.setVollmachtenFuerAuswahl(blBestaetigungsWorkflow.rcVollmachtenFuerAuswahl);
    }

    /**Open erfolgt in aufrufender Funktion*/
    public void initVollmachten() {
        BlBestaetigungsWorkflow blBestaetigungsWorkflow=new BlBestaetigungsWorkflow(eclDbM.getDbBundle());
        blBestaetigungsWorkflow.initVollmachten(false);
        
        uWorkflowSession.setWorkflowManuellOhneAufHinterlegteGepruefte(false);
        uWorkflowSession.setWorkflowManuellGesetzlich(false);
        uWorkflowSession.setVollmachtenFuerAuswahl(blBestaetigungsWorkflow.rcVollmachtenFuerAuswahl);
    }


    public String doManuellRefresh() {
        if (uMenueSession.menueWorkflowAuswahl() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uWorkflowManuellSuchen", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "";
        }
        
        eclDbM.openAll();
        eclDbM.openWeitere();
        ausfuehrenRefresh();
         eclDbM.closeAll();
        

        return xSessionVerwaltung.setzeUEnde("uWorkflowManuellSuchen", true, false, eclUserLoginM.getKennung());
    }
    
    private void ausfuehrenRefresh() {
        Boolean lManuellGesetzlich=uWorkflowSession.isWorkflowManuellGesetzlich();
        Boolean lManuellOhneAufHinterlegtePruefte=uWorkflowSession.isWorkflowManuellOhneAufHinterlegteGepruefte();
        
        BlBestaetigungsWorkflow blBestaetigungsWorkflow=new BlBestaetigungsWorkflow(eclDbM.getDbBundle());
        if (lManuellGesetzlich) {
            blBestaetigungsWorkflow.initGesetzlicheVertreter(lManuellOhneAufHinterlegtePruefte);
        }
        else {
            blBestaetigungsWorkflow.initVollmachten(lManuellOhneAufHinterlegtePruefte);
        }
 
        uWorkflowSession.setVollmachtenFuerAuswahl(blBestaetigungsWorkflow.rcVollmachtenFuerAuswahl);

    }
    public String doManuellVerarbeiten(EclVorlaeufigeVollmachtFuerAnzeige vollmacht) {
        if (uMenueSession.menueWorkflowAuswahl() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uWorkflowManuellSuchen", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "";
        }

        eclDbM.openAll();
        eclDbM.openWeitere();

        eclBestWorkflowM.initAlles();

        BlBestaetigungsWorkflow blBestaetigungsWorkflow=new BlBestaetigungsWorkflow(eclDbM.getDbBundle());
        Boolean brc=blBestaetigungsWorkflow.holeVollmachtsfall(vollmacht.meldeIdent,uWorkflowSession.isWorkflowManuellGesetzlich());
        
        eclDbM.closeAll();
        
        if (brc==false) {
            uSession.setFehlermeldung(
                    "Datensatz kann aktuell nicht bearbeitet werden. Bitte Refresh-Drücken und erneut versuchen");
            xSessionVerwaltung.setzeUEnde();
            return "";
           
        }
        
        eclBestWorkflowM.setZuAktionaersnummer(blBestaetigungsWorkflow.rcVorlaeufigeVollmachtFuerAnzeige.aktionaerNummerFuerAnzeige);
        eclBestWorkflowM.setNameMitglied(blBestaetigungsWorkflow.rcVorlaeufigeVollmachtFuerAnzeige.aktionaerName);
        eclBestWorkflowM.setGesetzlicherVertreter(blBestaetigungsWorkflow.rcVorlaeufigeVollmachtFuerAnzeige.bevollmaechtigterIstGesetzlich);

        eclBestWorkflowM.setBevollmaechtigterName(blBestaetigungsWorkflow.rcVorlaeufigeVollmachtFuerAnzeige.bevollmaechtigterName);
        eclBestWorkflowM.setBevollmaechtigterOrt(blBestaetigungsWorkflow.rcVorlaeufigeVollmachtFuerAnzeige.bevollmaechtigterOrt);
        
        eclBestWorkflowM.setEclMeldung(blBestaetigungsWorkflow.rcMeldung);

        return xSessionVerwaltung.setzeUEnde("uWorkflowManuellStandard", true, false, eclUserLoginM.getKennung());
    }
    
    public String doManuellAbbrechenStandard() {
        if (uMenueSession.menueWorkflowAuswahl() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uWorkflowManuellStandard", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "";
        }
        
        eclDbM.openAll();
        eclDbM.openWeitere();

        ausfuehrenRefresh();
        
        eclDbM.closeAll();

        return xSessionVerwaltung.setzeUEnde("uWorkflowManuellSuchen", true, false, eclUserLoginM.getKennung());
   }
    
    public String doManuellSpeichernStandard() {
        if (uMenueSession.menueWorkflowAuswahl() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uWorkflowManuellStandard", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "";
        }
        
        String pruefstatusErgebnis=eclBestWorkflowM.getPruefstatusErgebnis();
        if (pruefstatusErgebnis==null || (!pruefstatusErgebnis.equals("1") && !pruefstatusErgebnis.equals("5"))) {
            uSession.setFehlermeldung(
                    "Bitte Aktion auswählen");
            xSessionVerwaltung.setzeUEnde();
            return "";
       }
        eclDbM.openAll();
        eclDbM.openWeitere();

        EclMeldung lMeldung=eclBestWorkflowM.getEclMeldung();
        if (pruefstatusErgebnis.equals("1")) {
            lMeldung.zusatzfeld1="1";
        }
        if (pruefstatusErgebnis.equals("5")) {
            lMeldung.zusatzfeld1="-2";
        }
        
        int rc=eclDbM.getDbBundle().dbMeldungen.update(lMeldung);
        
        BlWillenserklaerung blWillenserklaerung=new BlWillenserklaerung();
        blWillenserklaerung.pWillenserklaerungGeberIdent=-1;
        blWillenserklaerung.piMeldungsIdentAktionaer=lMeldung.meldungsIdent;
        EclPersonenNatJur lPersonenNatJur=new EclPersonenNatJur();
        lPersonenNatJur.name=eclBestWorkflowM.getBevollmaechtigterName();
        lPersonenNatJur.ort=eclBestWorkflowM.getBevollmaechtigterOrt();
        blWillenserklaerung.pEclPersonenNatJur=lPersonenNatJur;
        blWillenserklaerung.vollmachtAnDritte(eclDbM.getDbBundle());
        if (blWillenserklaerung.rcIstZulaessig==false) {
            CaBug.drucke("001 "+blWillenserklaerung.rcGrundFuerUnzulaessig);
        }
        
        eclDbM.closeAll();
        if (rc!=1) {
            uSession.setFehlermeldung(
                    "Status konnte nicht zurückgespeichert werden - möglicherweise parallel durch anderem User verändert - bitte abbrechen und Vorgang neu starten");
            xSessionVerwaltung.setzeUEnde();
            return "";
        }
        
        eclDbM.openAll();
        eclDbM.openWeitere();

        ausfuehrenRefresh();
        
        eclDbM.closeAll();
      

        return xSessionVerwaltung.setzeUEnde("uWorkflowManuellSuchen", true, false, eclUserLoginM.getKennung());
    }
    
    /*++++++++++++++++++++++++++++Vorgangsliste abarbeiten++++++++++++++++++++++++++++++++++*/
    public String doVorgangPDFAnzeigen(EhVorlaeufigeVollmacht pEhVorlaeufigeVollmacht) {
        if (uMenueSession.menueWorkflowBasis() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uWorkflowVollmachtenStandard", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "";
        }
        
        ausfuehrenVorgangPDFAnzeigen(pEhVorlaeufigeVollmacht);
        
        return xSessionVerwaltung.setzeUEnde("uWorkflowVollmachtenStandard", true, false, eclUserLoginM.getKennung());

    }
    
    public String doVorgangPDFAnzeigenDetail(EhVorlaeufigeVollmacht pEhVorlaeufigeVollmacht) {
        if (uMenueSession.menueWorkflowBasis() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uWorkflowVollmachtenDetail", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "";
        }
        
        ausfuehrenVorgangPDFAnzeigen(pEhVorlaeufigeVollmacht);

         return xSessionVerwaltung.setzeUEnde("uWorkflowVollmachtenDetail", true, false, eclUserLoginM.getKennung());

    }

    private void ausfuehrenVorgangPDFAnzeigen(EhVorlaeufigeVollmacht pEhVorlaeufigeVollmacht) {
        RpBrowserAnzeigen rpBrowserAnzeigen = new RpBrowserAnzeigen();
        rpBrowserAnzeigen.zeigen(
                eclParamM.getClGlobalVar().lwPfadAllgemein + "\\"+eclParamM.getParamServer().praefixPfadVerzeichnisse
                        + "reports\\" + eclParamM.getMandantPfad() + "\\"
                        + pEhVorlaeufigeVollmacht.eclBestWorkflow.getSubverzeichnis() + "\\" + pEhVorlaeufigeVollmacht.eclBestWorkflow.getDateinameBestaetigung());
    }

    public String doVorgangDateiAnzeigen(EhVorlaeufigeVollmacht pEhVorlaeufigeVollmacht) {
        if (uMenueSession.menueWorkflowBasis() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uWorkflowVollmachtenStandard", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "";
        }

        int rc=ausfuehrenVorgangDateiAnzeigen(pEhVorlaeufigeVollmacht);
        if (rc==1) {
            return xSessionVerwaltung.setzeUEnde("uWorkflowVollmachtenStandard", true, false, eclUserLoginM.getKennung());
        }
         
        xSessionVerwaltung.setzeUEnde();

        return "uWorkflowDateianzeige.xhtml";
    }

    public String doVorgangDateiAnzeigenDetail(EhVorlaeufigeVollmacht pEhVorlaeufigeVollmacht) {
        if (uMenueSession.menueWorkflowBasis() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uWorkflowVollmachtenDetail", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "";
        }

        int rc=ausfuehrenVorgangDateiAnzeigen(pEhVorlaeufigeVollmacht);
        if (rc==1) {
            return xSessionVerwaltung.setzeUEnde("uWorkflowVollmachtenDetail", true, false, eclUserLoginM.getKennung());
        }
        
        xSessionVerwaltung.setzeUEnde();
        return "uWorkflowDateianzeige.xhtml";
    }

    /**Gibt zurück:
     * 1 => xSessionVerwaltung.setzeUEnde("uWorkflowVollmachtenStandard", true, false, eclUserLoginM.getKennung());
     * 2 => xSessionVerwaltung.setzeUEnde(); return "uWorkflowDateianzeige.xhtml";
     */
    public int ausfuehrenVorgangDateiAnzeigen(EhVorlaeufigeVollmacht pEhVorlaeufigeVollmacht) {

        String hPfad=pEhVorlaeufigeVollmacht.eclVorlaeufigeVollmachtEingabe.vertreterVorname;
        String hKompletterDateiname=
                pEhVorlaeufigeVollmacht.eclVorlaeufigeVollmachtEingabe.vertreterName+"_"
                + pEhVorlaeufigeVollmacht.eclVorlaeufigeVollmachtEingabe.vertreterZusatz;

        String hKompletterDateinameGross=hKompletterDateiname.toUpperCase();
        if (hKompletterDateinameGross.endsWith(".PDF")) {
            RpBrowserAnzeigen rpBrowserAnzeigen = new RpBrowserAnzeigen();
            rpBrowserAnzeigen.zeigen(
                    hPfad + "\\"+hKompletterDateiname);
            return 1;
         }
        
        CaBug.druckeLog("hPfad="+hPfad, logDrucken, 10);
        CaBug.druckeLog("hKompletterDateiname="+hKompletterDateiname, logDrucken, 10);
        
        uWorkflowSession.setAnzuzeigendeDateiPfad(hPfad);
        uWorkflowSession.setAnzuzeigendeDatei(hKompletterDateiname);
        
        return 2;
    }

    
    
    public String doVorgangAlsGesetzlUebernehmen(EhVorlaeufigeVollmacht pEhVorlaeufigeVollmacht) {
        if (uMenueSession.menueWorkflowBasis() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uWorkflowVollmachtenStandard", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "";
        }

        eclBestWorkflowM.setGesetzlicherVertreter(true);
        eclBestWorkflowM.setBevollmaechtigterTitel(pEhVorlaeufigeVollmacht.eclVorlaeufigeVollmachtEingabe.vertreterTitel);
        eclBestWorkflowM.setBevollmaechtigterName(pEhVorlaeufigeVollmacht.eclVorlaeufigeVollmachtEingabe.vertreterName);
        eclBestWorkflowM.setBevollmaechtigterVorname(pEhVorlaeufigeVollmacht.eclVorlaeufigeVollmachtEingabe.vertreterVorname);
        eclBestWorkflowM.setBevollmaechtigterStrasse(pEhVorlaeufigeVollmacht.eclVorlaeufigeVollmachtEingabe.vertreterStrasse);
        eclBestWorkflowM.setBevollmaechtigterPlz(pEhVorlaeufigeVollmacht.eclVorlaeufigeVollmachtEingabe.vertreterPlz);
        eclBestWorkflowM.setBevollmaechtigterOrt(pEhVorlaeufigeVollmacht.eclVorlaeufigeVollmachtEingabe.vertreterOrt);
        eclBestWorkflowM.setBevollmaechtigterEMail(pEhVorlaeufigeVollmacht.eclVorlaeufigeVollmachtEingabe.vertreterMail);
        eclBestWorkflowM.setMitgliedOderSonstiger("2");
        
        return xSessionVerwaltung.setzeUEnde("uWorkflowVollmachtenStandard", true, false, eclUserLoginM.getKennung());

    }
    
    public String doVorgangAlsVertreterUebernehmenNr(EhVorlaeufigeVollmacht pEhVorlaeufigeVollmacht) {
        if (uMenueSession.menueWorkflowBasis() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uWorkflowVollmachtenStandard", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "";
        }

        eclBestWorkflowM.setGesetzlicherVertreter(false);
        eclBestWorkflowM.setBevollmaechtigterTitel("");
        eclBestWorkflowM.setBevollmaechtigterName("");
        eclBestWorkflowM.setBevollmaechtigterVorname("");
        eclBestWorkflowM.setBevollmaechtigterStrasse("");
        eclBestWorkflowM.setBevollmaechtigterPlz("");
        eclBestWorkflowM.setBevollmaechtigterOrt("");
        eclBestWorkflowM.setBevollmaechtigterEMail(pEhVorlaeufigeVollmacht.eclVorlaeufigeVollmachtEingabe.vertreterMail);
        eclBestWorkflowM.setMitgliedOderSonstiger("1");
        eclBestWorkflowM.setKennung(pEhVorlaeufigeVollmacht.eclVorlaeufigeVollmachtEingabe.vertreterId);
       
        return xSessionVerwaltung.setzeUEnde("uWorkflowVollmachtenStandard", true, false, eclUserLoginM.getKennung());

    }
 
    public String doVorgangAlsVertreterUebernehmenName(EhVorlaeufigeVollmacht pEhVorlaeufigeVollmacht) {
        if (uMenueSession.menueWorkflowBasis() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uWorkflowVollmachtenStandard", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "";
        }

        eclBestWorkflowM.setGesetzlicherVertreter(false);
        eclBestWorkflowM.setBevollmaechtigterTitel("");
        eclBestWorkflowM.setBevollmaechtigterName(pEhVorlaeufigeVollmacht.eclVorlaeufigeVollmachtEingabe.vertreterName);
        eclBestWorkflowM.setBevollmaechtigterVorname("");
        eclBestWorkflowM.setBevollmaechtigterStrasse("");
        eclBestWorkflowM.setBevollmaechtigterPlz("");
        eclBestWorkflowM.setBevollmaechtigterOrt("");
        eclBestWorkflowM.setBevollmaechtigterEMail(pEhVorlaeufigeVollmacht.eclVorlaeufigeVollmachtEingabe.vertreterMail);
        eclBestWorkflowM.setMitgliedOderSonstiger("1");
        eclBestWorkflowM.setKennung("");
        
        return xSessionVerwaltung.setzeUEnde("uWorkflowVollmachtenStandard", true, false, eclUserLoginM.getKennung());

    }

    public String doVorgangAlsVertreterUebernehmen(EhVorlaeufigeVollmacht pEhVorlaeufigeVollmacht) {
        if (uMenueSession.menueWorkflowBasis() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uWorkflowVollmachtenStandard", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "";
        }

        eclBestWorkflowM.setGesetzlicherVertreter(false);
        eclBestWorkflowM.setBevollmaechtigterTitel(pEhVorlaeufigeVollmacht.eclVorlaeufigeVollmachtEingabe.vertreterTitel);
        eclBestWorkflowM.setBevollmaechtigterName(pEhVorlaeufigeVollmacht.eclVorlaeufigeVollmachtEingabe.vertreterName);
        eclBestWorkflowM.setBevollmaechtigterVorname(pEhVorlaeufigeVollmacht.eclVorlaeufigeVollmachtEingabe.vertreterVorname);
        eclBestWorkflowM.setBevollmaechtigterStrasse(pEhVorlaeufigeVollmacht.eclVorlaeufigeVollmachtEingabe.vertreterStrasse);
        eclBestWorkflowM.setBevollmaechtigterPlz(pEhVorlaeufigeVollmacht.eclVorlaeufigeVollmachtEingabe.vertreterPlz);
        eclBestWorkflowM.setBevollmaechtigterOrt(pEhVorlaeufigeVollmacht.eclVorlaeufigeVollmachtEingabe.vertreterOrt);
        eclBestWorkflowM.setBevollmaechtigterEMail(pEhVorlaeufigeVollmacht.eclVorlaeufigeVollmachtEingabe.vertreterMail);
        eclBestWorkflowM.setMitgliedOderSonstiger("2");
        
        String hBevollmaechtigterArText=pEhVorlaeufigeVollmacht.eclVorlaeufigeVollmachtEingabe.liefereVertreterArtText();
        if (pEhVorlaeufigeVollmacht.eclVorlaeufigeVollmachtEingabe.vertreterArt==EclVorlaeufigeVollmachtEingabe.VERTRETER_ART_SONSTIGE) {
            hBevollmaechtigterArText+=": "+pEhVorlaeufigeVollmacht.eclVorlaeufigeVollmachtEingabe.vertreterArtBeiSonstige;
        }
        eclBestWorkflowM.setBevollmaechtigterArtText(hBevollmaechtigterArText);
        
        
        return xSessionVerwaltung.setzeUEnde("uWorkflowVollmachtenStandard", true, false, eclUserLoginM.getKennung());

    }

}
