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
import de.meetingapps.meetingportal.meetComAllg.CaTest;
import de.meetingapps.meetingportal.meetComBlManaged.BlMInsti;
import de.meetingapps.meetingportal.meetComBlManaged.BlMTeilnehmerKommunikation;
import de.meetingapps.meetingportal.meetComBrM.BrMInit;
import de.meetingapps.meetingportal.meetComBrM.BrMSchluesselHolen;
import de.meetingapps.meetingportal.meetComEclM.EclDbM;
import de.meetingapps.meetingportal.meetComEclM.EclParamM;
import de.meetingapps.meetingportal.meetComEclM.EclUserLoginM;
import de.meetingapps.meetingportal.meetComEh.EhJsfMenue;
import de.meetingapps.meetingportal.meetComEntities.EclWortmeldetischView;
import de.meetingapps.meetingportal.meetComKonst.KonstWillenserklaerung;
import de.meetingapps.meetingportal.meetingCoreReport.RpBrowserAnzeigen;
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
public class UMenue {

    private int logDrucken=3;
    
    @Inject
    private EclDbM eclDbM;
    @Inject
    private XSessionVerwaltung xSessionVerwaltung;
    @Inject
    private EclParamM eclParamM;
    @Inject
    private USession uSession;

    @Inject
    private UNachrichtSenden uNachrichtSenden;
    @Inject
    private EclUserLoginM eclUserLoginM;
    @Inject
    private UTOWeisungsEmpfehlung uTOWeisungsEmpfehlung;
    @Inject
    private BlMInsti blMInsti;
    @Inject
    private AFunktionen aFunktionen;
    @Inject
    private UWeisungSession uWeisungSession;
    @Inject
    private UNachrichtSendenSession uNachrichtSendenSession;
    @Inject
    private UNachrichtEmpfangen uNachrichtEmpfangen;
    @Inject
    private UPasswortAendernSession uPasswortAendernSession;
    @Inject
    private UPasswortAendern uPasswortAendern;
    @Inject
    private UWeisungsEmpfehlungAntwortSession uWeisungsEmpfehlungAntwortSession;
    @Inject
    private UServicelineAnfrage uServicelineAnfrage;
    @Inject
    private UVirtuelleHV uVirtuelleHV;
    @Inject
    private UBotschaftenVerarbeiten uBotschaftenVerarbeiten;
    @Inject
    private UUnterlagenPdfs uUnterlagenPdfs;
   
    private @Inject UVeranstaltungen uVeranstaltungen;
    private @Inject UMandantenKennungSession uMandantenKennungSession;
    private @Inject UMandantenKennung uMandantenKennung;
    
    private @Inject BlMTeilnehmerKommunikation blMTeilnehmerKommunikation;
    
   @Inject
    private UGastkarte uGastkarte;
    @Inject
    private UMenueSession uMenueSession;
    
    private @Inject UDatenmanipulation uDatenmanipulation;
    private @Inject UReports uReports;
    private @Inject UReportsSession uReportsSession;
    private @Inject UImportExport uImportExport;
    
    @Inject
    private UWorkflow uWorkflow;
    @Inject
    private UWorkflowStatistik uWorkflowStatistik;
    @Inject
    private UWorkflowSession uWorkflowSession;
    @Inject
    private UWortmeldungen uWortmeldungen;
    @Inject
    private UPdfsAnzeigen uPdfsAnzeigen;
    @Inject
    private UPortalAktivierung uPortalAktivierung;

    private @Inject UPflegeFehlertextePortal uPflegeFehlertextePortal;
    private @Inject UPflegePortaltexte uPflegePortaltexte;

    @Inject
    private BrMInit brMInit;
    @Inject
    private BrMSchluesselHolen brMSchluesselHolen;

    /**Initialisieren - vor erstmaligem Aufruf von uMenue.
     * Open wird in der aufrufenden Funktion gehandelt.*/
    public void init() {

        /*z.B.: bei Insti: Bestand einlesen und anzeigen*/
        if (eclUserLoginM.getGehoertZuInsti() != 0) {
            int lInsti = eclUserLoginM.getGehoertZuInsti();
            blMInsti.fuelleBestandszuordnungFuerPortal(eclDbM.getDbBundle(), lInsti);

            if (!aFunktionen.pruefeOBWillenserklaerungZulaessig(eclDbM.getDbBundle(), KonstWillenserklaerung.vollmachtUndWeisungAnSRV, "2")) {
                uWeisungSession.setAendernZulaessig(false);
            } else {
                uWeisungSession.setAendernZulaessig(true);
            }
            uNachrichtEmpfangen.init("uMenue", true);
 
        }
        
        List<EhJsfMenue> wortmeldungViews= new LinkedList<EhJsfMenue>();
        EclWortmeldetischView[] wortmeldetischViewArray=eclParamM.getParam().paramPortal.wortmeldetischViewArray;
        for (int i=0;i<wortmeldetischViewArray.length;i++) {
            CaBug.druckeLog("textInMenue="+wortmeldetischViewArray[i].textInMenue+" i="+i, logDrucken, 10);
           if (i>2 && !wortmeldetischViewArray[i].textInMenue.isEmpty()) {
               EhJsfMenue lEhJsfMenue=new EhJsfMenue(wortmeldetischViewArray[i].textInMenue, i);
               wortmeldungViews.add(lEhJsfMenue);
           }
        }
        uMenueSession.setWortmeldungViews(wortmeldungViews);
        
    }

    public String doNachrichtSenden() {
        if (eclUserLoginM.pruefe_uportal_mailSenden() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uMenue", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }

        uNachrichtSenden.initialisieren(eclParamM.getClGlobalVar().mandant, 0, 0);
        uNachrichtSenden.initialisiereAusgangsXHTML("uMenue");

        return xSessionVerwaltung.setzeUEnde("uNachrichtSenden", true, false, eclUserLoginM.getKennung());
    }



    public String doWortmeldungenView(int pView) {
        if (!xSessionVerwaltung.pruefeUStart("uMenue", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }

        uWortmeldungen.initialisieren(pView);

        return xSessionVerwaltung.setzeUEnde("uWortmeldungenView", true, false, eclUserLoginM.getKennung());

    }

    public String doWortmeldungenVersammlungsleitung() {
        if (!xSessionVerwaltung.pruefeUStart("uMenue", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }

        uWortmeldungen.initialisieren(1);

        return xSessionVerwaltung.setzeUEnde("uWortmeldungenVersammlungsleitung", true, false, eclUserLoginM.getKennung());

    }

    public String doTOWeisungsEmpfehlung() {
        if (eclUserLoginM.pruefe_uportal_dLoginStandardAdmin() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uMenue", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }

        eclDbM.openAll();
        uTOWeisungsEmpfehlung.init();
        eclDbM.closeAll();

        return xSessionVerwaltung.setzeUEnde("uTOWeisungsEmpfehlung", true, false, eclUserLoginM.getKennung());

    }

    /******************************Service-Line************************************/
    public String doServicelineAnfrage() {
        if (eclUserLoginM.pruefe_uportal_servicelinetools() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uMenue", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }

        eclDbM.openAll();
        uServicelineAnfrage.init();
        eclDbM.closeAll();
        return xSessionVerwaltung.setzeUEnde("uServicelineAnfrage", true, false, eclUserLoginM.getKennung());

    }

    /********************Virtuelle HV***************************/

    /**Fragen*/
    public String doVirtuelleHVFragenCSVAlle() {
        if (eclUserLoginM.pruefe_uportal_virtuelleHV() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uMenue", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }

        eclDbM.openAll();
        uVirtuelleHV.init();
        uVirtuelleHV.fragenCSVAlle();
        eclDbM.closeAll();
        return xSessionVerwaltung.setzeUEnde("uVirtuelleHV", true, false, eclUserLoginM.getKennung());
    }

    public String doVirtuelleHVFragenCSVNeue() {
        if (eclUserLoginM.pruefe_uportal_virtuelleHV() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uMenue", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }

        eclDbM.openAll();
        uVirtuelleHV.init();
        uVirtuelleHV.fragenCSVNeue();
        eclDbM.closeAll();
        return xSessionVerwaltung.setzeUEnde("uVirtuelleHV", true, false, eclUserLoginM.getKennung());
    }

    public String doVirtuelleHVFragenReportAlle() {
        if (eclUserLoginM.pruefe_uportal_virtuelleHV() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uMenue", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }

        eclDbM.openAll();
        uVirtuelleHV.init();
        uVirtuelleHV.fragenReportAlle();
        eclDbM.closeAll();
        return xSessionVerwaltung.setzeUEnde("uVirtuelleHV", true, false, eclUserLoginM.getKennung());
    }

    public String doVirtuelleHVFragenTestmail() {
        if (eclUserLoginM.pruefe_uportal_virtuelleHV() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uMenue", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }
        uSession.clearFehlermeldung();
        
        eclDbM.openAll();
        if (eclDbM.getDbBundle().param.paramPortal.fragenMailBeiEingang != 1) {
            uSession.setFehlermeldung("Mail bei Frageneingang nicht aktiviert");
            eclDbM.closeAll();
            return xSessionVerwaltung.setzeUEnde("uMenue", false, false, eclUserLoginM.getKennung());
        }
        uVirtuelleHV.fragenTestMail();
        
        uSession.setFehlermeldung("Test-Frage verschickt an " + eclParamM.getParam().paramPortal.fragenMailVerteiler1 + ";" + eclParamM.getParam().paramPortal.fragenMailVerteiler2 + ";"
                + eclParamM.getParam().paramPortal.fragenMailVerteiler3 + ";");

        eclDbM.closeAll();
        return xSessionVerwaltung.setzeUEnde("uMenue", false, false, eclUserLoginM.getKennung());
    }

    
    /**Wortmeldungen*/
    public String doVirtuelleHVWortmeldungenCSVAlle() {
        if (eclUserLoginM.pruefe_uportal_virtuelleHV() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uMenue", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }

        eclDbM.openAll();
        uVirtuelleHV.init();
        uVirtuelleHV.wortmeldungenCSVAlle();
        eclDbM.closeAll();
        return xSessionVerwaltung.setzeUEnde("uVirtuelleHV", true, false, eclUserLoginM.getKennung());
    }

    public String doVirtuelleHVWortmeldungenCSVNeue() {
        if (eclUserLoginM.pruefe_uportal_virtuelleHV() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uMenue", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }

        eclDbM.openAll();
        uVirtuelleHV.init();
        uVirtuelleHV.wortmeldungenCSVNeue();
        eclDbM.closeAll();
        return xSessionVerwaltung.setzeUEnde("uVirtuelleHV", true, false, eclUserLoginM.getKennung());
    }

    public String doVirtuelleHVWortmeldungenReportAlle() {
        if (eclUserLoginM.pruefe_uportal_virtuelleHV() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uMenue", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }

        eclDbM.openAll();
        uVirtuelleHV.init();
        uVirtuelleHV.wortmeldungenReportAlle();
        eclDbM.closeAll();
        return xSessionVerwaltung.setzeUEnde("uVirtuelleHV", true, false, eclUserLoginM.getKennung());
    }

    public String doVirtuelleHVWortmeldungenTestmail() {
        if (eclUserLoginM.pruefe_uportal_virtuelleHV() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uMenue", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }
        uSession.clearFehlermeldung();
        
        eclDbM.openAll();
        if (eclDbM.getDbBundle().param.paramPortal.wortmeldungMailBeiEingang != 1) {
            uSession.setFehlermeldung("Mail bei Wortmeldungeingang nicht aktiviert");
            eclDbM.closeAll();
            return xSessionVerwaltung.setzeUEnde("uMenue", false, false, eclUserLoginM.getKennung());
        }
        uVirtuelleHV.wortmeldungenTestMail();
        
        uSession.setFehlermeldung("Test-Wortmeldung verschickt an " + eclParamM.getParam().paramPortal.wortmeldungMailVerteiler1 + ";" + eclParamM.getParam().paramPortal.wortmeldungMailVerteiler2 + ";"
                + eclParamM.getParam().paramPortal.wortmeldungMailVerteiler3 + ";");

        eclDbM.closeAll();
        return xSessionVerwaltung.setzeUEnde("uMenue", false, false, eclUserLoginM.getKennung());
    }

    
    /**Widersprüche*/
    public String doVirtuelleHVWiderspruecheCSVAlle() {
        if (eclUserLoginM.pruefe_uportal_virtuelleHV() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uMenue", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }

        eclDbM.openAll();
        uVirtuelleHV.init();
        uVirtuelleHV.widerspruecheCSVAlle();
        eclDbM.closeAll();
        return xSessionVerwaltung.setzeUEnde("uVirtuelleHV", true, false, eclUserLoginM.getKennung());
    }

    public String doVirtuelleHVWiderspruecheCSVNeue() {
        if (eclUserLoginM.pruefe_uportal_virtuelleHV() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uMenue", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }

        eclDbM.openAll();
        uVirtuelleHV.init();
        uVirtuelleHV.widerspruecheCSVNeue();
        eclDbM.closeAll();
        return xSessionVerwaltung.setzeUEnde("uVirtuelleHV", true, false, eclUserLoginM.getKennung());
    }

    public String doVirtuelleHVWiderspruecheReportAlle() {
        if (eclUserLoginM.pruefe_uportal_virtuelleHV() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uMenue", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }

        eclDbM.openAll();
        uVirtuelleHV.init();
        uVirtuelleHV.widerspruecheReportAlle();
        eclDbM.closeAll();
        return xSessionVerwaltung.setzeUEnde("uVirtuelleHV", true, false, eclUserLoginM.getKennung());
    }

    public String doVirtuelleHVWiderspruecheTestmail() {
        if (eclUserLoginM.pruefe_uportal_virtuelleHV() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uMenue", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }
        uSession.clearFehlermeldung();
        
        eclDbM.openAll();
        if (eclDbM.getDbBundle().param.paramPortal.widerspruecheMailBeiEingang != 1) {
            uSession.setFehlermeldung("Mail bei Widerspruchseingang nicht aktiviert");
            eclDbM.closeAll();
            return xSessionVerwaltung.setzeUEnde("uMenue", false, false, eclUserLoginM.getKennung());
        }
        uVirtuelleHV.widerspruecheTestMail();
        
        uSession.setFehlermeldung("Test-Widerspruch verschickt an " + eclParamM.getParam().paramPortal.widerspruecheMailVerteiler1 + ";" + eclParamM.getParam().paramPortal.widerspruecheMailVerteiler2 + ";"
                + eclParamM.getParam().paramPortal.widerspruecheMailVerteiler3 + ";");

        eclDbM.closeAll();
        return xSessionVerwaltung.setzeUEnde("uMenue", false, false, eclUserLoginM.getKennung());
    }

    /**Anträge*/
    public String doVirtuelleHVAntraegeCSVAlle() {
        if (eclUserLoginM.pruefe_uportal_virtuelleHV() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uMenue", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }

        eclDbM.openAll();
        uVirtuelleHV.init();
        uVirtuelleHV.antraegeCSVAlle();
        eclDbM.closeAll();
        return xSessionVerwaltung.setzeUEnde("uVirtuelleHV", true, false, eclUserLoginM.getKennung());
    }

    public String doVirtuelleHVAntraegeCSVNeue() {
        if (eclUserLoginM.pruefe_uportal_virtuelleHV() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uMenue", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }

        eclDbM.openAll();
        uVirtuelleHV.init();
        uVirtuelleHV.antraegeCSVNeue();
        eclDbM.closeAll();
        return xSessionVerwaltung.setzeUEnde("uVirtuelleHV", true, false, eclUserLoginM.getKennung());
    }

    public String doVirtuelleHVAntraegeReportAlle() {
        if (eclUserLoginM.pruefe_uportal_virtuelleHV() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uMenue", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }

        eclDbM.openAll();
        uVirtuelleHV.init();
        uVirtuelleHV.antraegeReportAlle();
        eclDbM.closeAll();
        return xSessionVerwaltung.setzeUEnde("uVirtuelleHV", true, false, eclUserLoginM.getKennung());
    }

    public String doVirtuelleHVAntraegeTestmail() {
        if (eclUserLoginM.pruefe_uportal_virtuelleHV() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uMenue", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }
        uSession.clearFehlermeldung();
        
        eclDbM.openAll();
        if (eclDbM.getDbBundle().param.paramPortal.antraegeMailBeiEingang != 1) {
            uSession.setFehlermeldung("Mail bei Antragseingang nicht aktiviert");
            eclDbM.closeAll();
            return xSessionVerwaltung.setzeUEnde("uMenue", false, false, eclUserLoginM.getKennung());
        }
        uVirtuelleHV.antraegeTestMail();
        
        uSession.setFehlermeldung("Test-Antrag verschickt an " + eclParamM.getParam().paramPortal.antraegeMailVerteiler1 + ";" + eclParamM.getParam().paramPortal.antraegeMailVerteiler2 + ";"
                + eclParamM.getParam().paramPortal.antraegeMailVerteiler3 + ";");

        eclDbM.closeAll();
        return xSessionVerwaltung.setzeUEnde("uMenue", false, false, eclUserLoginM.getKennung());
    }

    /**Sonstige Mitteilungen*/
    public String doVirtuelleHVSonstigeMitteilungenCSVAlle() {
        if (eclUserLoginM.pruefe_uportal_virtuelleHV() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uMenue", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }

        eclDbM.openAll();
        uVirtuelleHV.init();
        uVirtuelleHV.sonstigeMitteilungenCSVAlle();
        eclDbM.closeAll();
        return xSessionVerwaltung.setzeUEnde("uVirtuelleHV", true, false, eclUserLoginM.getKennung());
    }

    public String doVirtuelleHVSonstigeMitteilungenCSVNeue() {
        if (eclUserLoginM.pruefe_uportal_virtuelleHV() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uMenue", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }

        eclDbM.openAll();
        uVirtuelleHV.init();
        uVirtuelleHV.sonstigeMitteilungenCSVNeue();
        eclDbM.closeAll();
        return xSessionVerwaltung.setzeUEnde("uVirtuelleHV", true, false, eclUserLoginM.getKennung());
    }

    public String doVirtuelleHVSonstigeMitteilungenReportAlle() {
        if (eclUserLoginM.pruefe_uportal_virtuelleHV() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uMenue", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }

        eclDbM.openAll();
        uVirtuelleHV.init();
        uVirtuelleHV.sonstigeMitteilungenReportAlle();
        eclDbM.closeAll();
        return xSessionVerwaltung.setzeUEnde("uVirtuelleHV", true, false, eclUserLoginM.getKennung());
    }

    public String doVirtuelleHVSonstigeMitteilungenTestmail() {
        if (eclUserLoginM.pruefe_uportal_virtuelleHV() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uMenue", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }
        uSession.clearFehlermeldung();
        
        eclDbM.openAll();
        if (eclDbM.getDbBundle().param.paramPortal.sonstMitteilungenMailBeiEingang != 1) {
            uSession.setFehlermeldung("Mail bei Mitteilungeingang nicht aktiviert");
            eclDbM.closeAll();
            return xSessionVerwaltung.setzeUEnde("uMenue", false, false, eclUserLoginM.getKennung());
        }
        uVirtuelleHV.sonstigeMitteilungenTestMail();
        
        uSession.setFehlermeldung("Test-Mitteilung verschickt an " + eclParamM.getParam().paramPortal.sonstMitteilungenMailVerteiler1 + ";" + eclParamM.getParam().paramPortal.sonstMitteilungenMailVerteiler2 + ";"
                + eclParamM.getParam().paramPortal.sonstMitteilungenMailVerteiler3 + ";");

        eclDbM.closeAll();
        return xSessionVerwaltung.setzeUEnde("uMenue", false, false, eclUserLoginM.getKennung());
    }

    

    /**Botschaften Einreichen*/
    public String doVirtuelleHVBotschaftenEinreichenCSVAlle() {
        if (eclUserLoginM.pruefe_uportal_virtuelleHV() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uMenue", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }

        eclDbM.openAll();
        uVirtuelleHV.init();
        uVirtuelleHV.botschaftenEinreichenCSVAlle();
        eclDbM.closeAll();
        return xSessionVerwaltung.setzeUEnde("uVirtuelleHV", true, false, eclUserLoginM.getKennung());
    }

    public String doVirtuelleHVBotschaftenEinreichenCSVNeue() {
        if (eclUserLoginM.pruefe_uportal_virtuelleHV() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uMenue", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }

        eclDbM.openAll();
        uVirtuelleHV.init();
        uVirtuelleHV.botschaftenEinreichenCSVNeue();
        eclDbM.closeAll();
        return xSessionVerwaltung.setzeUEnde("uVirtuelleHV", true, false, eclUserLoginM.getKennung());
    }

    public String doVirtuelleHVBotschaftenEinreichenReportAlle() {
        if (eclUserLoginM.pruefe_uportal_virtuelleHV() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uMenue", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }

        eclDbM.openAll();
        uVirtuelleHV.init();
        uVirtuelleHV.botschaftenEinreichenReportAlle();
        eclDbM.closeAll();
        return xSessionVerwaltung.setzeUEnde("uVirtuelleHV", true, false, eclUserLoginM.getKennung());
    }

    public String doVirtuelleHVBotschaftenEinreichenTestmail() {
        if (eclUserLoginM.pruefe_uportal_virtuelleHV() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uMenue", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }
        uSession.clearFehlermeldung();
        
        eclDbM.openAll();
        if (eclDbM.getDbBundle().param.paramPortal.botschaftenMailBeiEingang != 1) {
            uSession.setFehlermeldung("Mail bei Botschafteneingang nicht aktiviert");
            eclDbM.closeAll();
            return xSessionVerwaltung.setzeUEnde("uMenue", false, false, eclUserLoginM.getKennung());
        }
        uVirtuelleHV.botschaftenEinreichenTestMail();
        
        uSession.setFehlermeldung("Test-Mitteilung verschickt an " + eclParamM.getParam().paramPortal.botschaftenMailVerteiler1 + ";" + eclParamM.getParam().paramPortal.botschaftenMailVerteiler2 + ";"
                + eclParamM.getParam().paramPortal.botschaftenMailVerteiler3 + ";");

        eclDbM.closeAll();
        return xSessionVerwaltung.setzeUEnde("uMenue", false, false, eclUserLoginM.getKennung());
    }

    public String doVirtuelleHVBotschaftenEinreichenVerarbeiten() {
        if (eclUserLoginM.pruefe_uportal_virtuelleHV() == false && uMenueSession.menueVirtuelleHV_menuePunktBotschaften()==false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uMenue", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }
        uSession.clearFehlermeldung();
        
        uBotschaftenVerarbeiten.init(false);
        
         return xSessionVerwaltung.setzeUEnde("uBotschaftenVerarbeiten", false, false, eclUserLoginM.getKennung());
    }

    
    public String doVirtuelleHVUnterlagenPdfs() {
        if (eclUserLoginM.pruefe_uportal_virtuelleHV() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uMenue", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }
        uSession.clearFehlermeldung();
        
        uUnterlagenPdfs.init(false);
        
         return xSessionVerwaltung.setzeUEnde("uUnterlagenPdfs", false, false, eclUserLoginM.getKennung());
    }
 
    
 
    /**Sonstige Mitteilungen*/
    public String doVirtuelleHVDownloadPDF() {
        if (eclUserLoginM.pruefe_uportal_virtuelleHV() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uMenue", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }

        uPdfsAnzeigen.init();
        
       return xSessionVerwaltung.setzeUEnde("uPdfsAnzeigen", true, false, eclUserLoginM.getKennung());
    }

    /**Sonstige Mitteilungen*/
    public String doPortalAktivierung() {
        if (eclUserLoginM.pruefe_uportal_virtuelleHV() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uMenue", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }

        uPortalAktivierung.init();
        
       return xSessionVerwaltung.setzeUEnde("uPortalAktivierung", true, false, eclUserLoginM.getKennung());
    }

    /**************Alt*/
    
    public String doVirtuelleHVWillenserklaerungenFuerMitteilungenReport() {
        if (eclUserLoginM.pruefe_uportal_virtuelleHV() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uMenue", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }

        eclDbM.openAll();
        uVirtuelleHV.init();
        uVirtuelleHV.willenserklaerungenFuerMitteilungenReport();
        eclDbM.closeAll();
        return xSessionVerwaltung.setzeUEnde("uVirtuelleHV", true, false, eclUserLoginM.getKennung());
    }

    public void doVirtuelleHVWillenserklaerungenFuerMitteilungenReportCockpit() {
        if (eclUserLoginM.pruefe_uportal_virtuelleHV() == false) {
            return;
        }
        if (!xSessionVerwaltung.pruefeUStart("uMenue", "")) {
            xSessionVerwaltung.setzeUEnde();
            return;
        }

        eclDbM.openAll();
        eclDbM.openWeitere();
        String hAusgabe = uVirtuelleHV.willenserklaerungenFuerWiderspruecheReportErzeugen();
        eclDbM.closeAll();
        RpBrowserAnzeigen rpBrowserAnzeigen = new RpBrowserAnzeigen();
        rpBrowserAnzeigen.zeigen(hAusgabe);

        xSessionVerwaltung.setzeUEnde();
    }

    public String doVirtuelleHVWillenserklaerungenFuerAktionaerCockpit() {
        if (eclUserLoginM.pruefe_uportal_virtuelleHV() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uMenue", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "";
        }

        uReportsSession.clear();
        uReports.willenserklaerungenFuerAktionaerStart();
        return xSessionVerwaltung.setzeUEnde("uReportsStart", true, false, eclUserLoginM.getKennung());
    }


    public String doVirtuelleHVStreamUser() {
        if (eclUserLoginM.pruefe_uportal_virtuelleHV() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uMenue", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }

        eclDbM.openAll();
        eclDbM.openWeitere();
        uVirtuelleHV.init();
        uVirtuelleHV.streamUser();
        eclDbM.closeAll();
        return xSessionVerwaltung.setzeUEnde("uVirtuelleHV", true, false, eclUserLoginM.getKennung());
    }

    public String doVirtuelleHVCockpit() {
        if (eclUserLoginM.pruefe_uportal_virtuelleHV() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uMenue", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }
        boolean hvCockpitEin = uMenueSession.isHvCockpitEin();
        if (hvCockpitEin == false) {
            hvCockpitEin = true;
        } else {
            hvCockpitEin = false;
        }
        uMenueSession.setHvCockpitEin(hvCockpitEin);
        return xSessionVerwaltung.setzeUEnde("uMenue", true, false, eclUserLoginM.getKennung());
    }

    /************************Gäste*************************************/
    public String doGaesteNeueGastkarte() {
        if (eclUserLoginM.pruefe_emittentenPortal_gaestemodul() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uMenue", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }
        uGastkarte.initOhneGruppe();
        return xSessionVerwaltung.setzeUEnde("uGastkarteNeu", true, false, eclUserLoginM.getKennung());
    }

    public String doGaesteKorrigierenUeberpruefen() {
        if (eclUserLoginM.pruefe_emittentenPortal_gaestemodul() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uMenue", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }
        uGastkarte.initSuchen();
        return xSessionVerwaltung.setzeUEnde("uGastkarteSuchen", true, false, eclUserLoginM.getKennung());

    }

    public String doGaesteGruppenkarten() {
        if (eclUserLoginM.pruefe_emittentenPortal_gaestemodulGruppen() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uMenue", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }
        uGastkarte.initMitGruppe();
        return xSessionVerwaltung.setzeUEnde("uGastkarteNeu", false, false, "");

    }

    public String doGaesteBlankoGastkarten() {
        if (eclUserLoginM.pruefe_emittentenPortal_gaestemodul() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uMenue", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }
        uGastkarte.initBlankoGastkarten();
        return xSessionVerwaltung.setzeUEnde("uGastkarteBlanko", true, false, eclUserLoginM.getKennung());
    }

    public String doGaesteInstiGastkarten() {
        if (eclUserLoginM.pruefe_emittentenPortal_gaestemodul() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uMenue", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }
        uGastkarte.initInstiGastkarten();
        return xSessionVerwaltung.setzeUEnde("uGastkarteInsti", true, false, eclUserLoginM.getKennung());
    }

    public String doGaesteInterneGastkarten() {
        if (eclUserLoginM.pruefe_emittentenPortal_gaestemodul() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uMenue", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }
        uGastkarte.initInterneGastkarten();
        return xSessionVerwaltung.setzeUEnde("uGastkarteIntern", true, false, eclUserLoginM.getKennung());
    }

    public String doGaesteListe() {
         if (eclUserLoginM.pruefe_emittentenPortal_gaestemodul() == false && eclUserLoginM.pruefe_uportal_servicelinetools() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uMenue", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }
        
        uReportsSession.clear();
        uReports.gaestelisteStart();
        return xSessionVerwaltung.setzeUEnde("uReportsStart", true, false, eclUserLoginM.getKennung());

    }

 
    /********************Reports****************************************************/
    public String doStatistik() {
        if (!xSessionVerwaltung.pruefeUStart("uMenue", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }

        uReportsSession.clear();
        uReports.statistik();

        return xSessionVerwaltung.setzeUEnde("uReportsAnzeige", true, false, eclUserLoginM.getKennung());
    }

    public String doMeldeliste() {
        if (!xSessionVerwaltung.pruefeUStart("uMenue", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }

        uReportsSession.clear();
        uReports.meldelisteStart();
        return xSessionVerwaltung.setzeUEnde("uReportsStart", true, false, eclUserLoginM.getKennung());
    }

    public String doMeldelisteAlleTop10() {
        if (!xSessionVerwaltung.pruefeUStart("uMenue", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }
        uReportsSession.clear();
        uReports.meldelisteAlleTop10();
        return xSessionVerwaltung.setzeUEnde("uReportsAnzeige", true, false, eclUserLoginM.getKennung());
    }

    public String doMeldelisteAlleAlpha() {
        if (!xSessionVerwaltung.pruefeUStart("uMenue", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }
        uReportsSession.clear();
        uReports.meldelisteAlleAlpha();
        return xSessionVerwaltung.setzeUEnde("uReportsAnzeige", true, false, eclUserLoginM.getKennung());
    }

    public String doMeldelisteNichtInSammelTop10() {
        if (!xSessionVerwaltung.pruefeUStart("uMenue", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }
        uReportsSession.clear();
        uReports.meldelisteNichtInSammelTop10();
        return xSessionVerwaltung.setzeUEnde("uReportsAnzeige", true, false, eclUserLoginM.getKennung());
    }

    public String doMeldelisteNichtInSammelAlpha() {
        if (!xSessionVerwaltung.pruefeUStart("uMenue", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }
        uReportsSession.clear();
        uReports.meldelisteNichtInSammelAlpha();
        return xSessionVerwaltung.setzeUEnde("uReportsAnzeige", true, false, eclUserLoginM.getKennung());
    }

    public String doMeldelisteSRVTop10() {
        if (!xSessionVerwaltung.pruefeUStart("uMenue", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }
        uReportsSession.clear();
        uReports.meldelisteSRVTop10();
        return xSessionVerwaltung.setzeUEnde("uReportsAnzeige", true, false, eclUserLoginM.getKennung());
    }

    public String doMeldelisteSRVAlpha() {
        if (!xSessionVerwaltung.pruefeUStart("uMenue", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }
        uReportsSession.clear();
        uReports.meldelisteSRVAlpha();
        return xSessionVerwaltung.setzeUEnde("uReportsAnzeige", true, false, eclUserLoginM.getKennung());
    }

    public String doMeldelisteBriefwahlTop10() {
        if (!xSessionVerwaltung.pruefeUStart("uMenue", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }
        uReportsSession.clear();
        uReports.meldelisteBriefwahlTop10();
        return xSessionVerwaltung.setzeUEnde("uReportsAnzeige", true, false, eclUserLoginM.getKennung());
    }

    public String doMeldelisteBriefwahlAlpha() {
        if (!xSessionVerwaltung.pruefeUStart("uMenue", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }
        uReportsSession.clear();
        uReports.meldelisteBriefwahlAlpha();
        return xSessionVerwaltung.setzeUEnde("uReportsAnzeige", true, false, eclUserLoginM.getKennung());
    }

    public String doMeldelisteSonstSammelTop10() {
        if (!xSessionVerwaltung.pruefeUStart("uMenue", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }
        uReportsSession.clear();
        uReports.meldelisteSonstSammelTop10();
        return xSessionVerwaltung.setzeUEnde("uReportsAnzeige", true, false, eclUserLoginM.getKennung());
    }

    public String doMeldelisteSonstSammelAlpha() {
        if (!xSessionVerwaltung.pruefeUStart("uMenue", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }
        uReportsSession.clear();
        uReports.meldelisteSonstSammelAlpha();
        return xSessionVerwaltung.setzeUEnde("uReportsAnzeige", true, false, eclUserLoginM.getKennung());
    }

    /*++++Negativliste Anmeldungen+++++*/
    public String doNegativlisteAnmeldungenAuswahl() {
        if (!xSessionVerwaltung.pruefeUStart("uMenue", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }

        uReportsSession.clear();
        uReports.negativlisteAnmeldungenAuswahlStart();
        return xSessionVerwaltung.setzeUEnde("uReportsStart", true, false, eclUserLoginM.getKennung());
    }

    public String doNegativlisteAnmeldungenTop10() {
        if (!xSessionVerwaltung.pruefeUStart("uMenue", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }
        uReportsSession.clear();
        uReports.negativlisteAnmeldungenTop10();
        return xSessionVerwaltung.setzeUEnde("uReportsAnzeige", true, false, eclUserLoginM.getKennung());
    }

    public String doNegativlistePraesenz() {
        if (!xSessionVerwaltung.pruefeUStart("uMenue", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }

        uReportsSession.clear();
        uReports.negativlistePraesenzStart();
        return xSessionVerwaltung.setzeUEnde("uReportsStart", true, false, eclUserLoginM.getKennung());
    }

    /*++++++++++++++++Kontrolle Anmeldungen++++++++++++++++++++++++++++++++++++++*/
    public String doKontrolleAnmeldungenKleinererBestand() {
        if (!xSessionVerwaltung.pruefeUStart("uMenue", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }

        uReportsSession.clear();
        uReports.kontrolleAnmeldungenKleinererBestand();
        return xSessionVerwaltung.setzeUEnde("uReportsAnzeige", true, false, eclUserLoginM.getKennung());
    }
   
    public String doKontrolleAnmeldungenGroessererBestand() {
        if (!xSessionVerwaltung.pruefeUStart("uMenue", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }

        uReportsSession.clear();
        uReports.kontrolleAnmeldungenGroessererBestand();
        return xSessionVerwaltung.setzeUEnde("uReportsAnzeige", true, false, eclUserLoginM.getKennung());
    }

    public String doKontrolleAnmeldungenNullBestand() {
        if (!xSessionVerwaltung.pruefeUStart("uMenue", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }

        uReportsSession.clear();
        uReports.kontrolleAnmeldungenNullBestand();
        return xSessionVerwaltung.setzeUEnde("uReportsAnzeige", true, false, eclUserLoginM.getKennung());
    }

    public String doKontrolleUnbestaetigteEintraegeAktienregister() {
        if (!xSessionVerwaltung.pruefeUStart("uMenue", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }

        uReportsSession.clear();
        uReports.kontrolleUnbestaetigteEintraegeAktienregister();
        return xSessionVerwaltung.setzeUEnde("uReportsAnzeige", true, false, eclUserLoginM.getKennung());
    }

    public String doKontrolleKonsistenzpruefung() {
        if (!xSessionVerwaltung.pruefeUStart("uMenue", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }

        uReportsSession.clear();
        uReports.kontrolleKonsistenzpruefung();
        return xSessionVerwaltung.setzeUEnde("uReportsAnzeige", true, false, eclUserLoginM.getKennung());
    }

    /*+++++++++++++++Kontrolliste Weisungen+++++++++++++++++*/
    public String doKontrolleWeisungen() {
       if (!xSessionVerwaltung.pruefeUStart("uMenue", "")) {
           xSessionVerwaltung.setzeUEnde();
           return "uDlgFehler";
       }
       
       uReportsSession.clear();
       uReports.kontrolleWeisungenStart();
       return xSessionVerwaltung.setzeUEnde("uReportsStart", true, false, eclUserLoginM.getKennung());

   }

    /*+++++++Weisungssummen+++++++++++++++++*/
    public String doWeisungssummenAlleKunde() {
        if (uMenueSession.menueReportsEinzeln(1) == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uMenue", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }

        uReportsSession.clear();
        uReports.weisungsSummenAlleKunde();

        return xSessionVerwaltung.setzeUEnde("uReportsAnzeige", true, false, eclUserLoginM.getKennung());
    }

    public void doWeisungssummenAlleCockpit() {
        if (uMenueSession.menueReportsEinzeln(2) == false) {
            return;
        }
        if (!xSessionVerwaltung.pruefeUStart("uMenue", "")) {
            xSessionVerwaltung.setzeUEnde();
            return;
        }

        uReportsSession.clear();
        uReports.weisungsSummenAlleCockpit();
        xSessionVerwaltung.setzeUEnde();
    }

    public String doWeisungssummenAlle() {
        if (uMenueSession.menueReportsEinzeln(2) == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uMenue", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }

        uReportsSession.clear();
        uReports.weisungsSummenAlle();

        return xSessionVerwaltung.setzeUEnde("uReportsAnzeige", true, false, eclUserLoginM.getKennung());
    }

    public void doWeisungssummenSRVCockpit() {
        if (uMenueSession.menueReportsEinzeln(2) == false) {
            return;
        }
        if (!xSessionVerwaltung.pruefeUStart("uMenue", "")) {
            xSessionVerwaltung.setzeUEnde();
            return;
        }

        uReportsSession.clear();
        uReports.weisungsSummenSRVCockpit();

        xSessionVerwaltung.setzeUEnde();
    }

    public String doWeisungssummenSRV() {
        if (uMenueSession.menueReportsEinzeln(2) == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uMenue", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }

        uReportsSession.clear();
        uReports.weisungsSummenSRV();

        return xSessionVerwaltung.setzeUEnde("uReportsAnzeige", true, false, eclUserLoginM.getKennung());
    }

    public String doWeisungssummenMitAuswahl() {
        if (uMenueSession.menueReportsEinzeln(2) == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uMenue", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }

        uReportsSession.clear();
        uReports.weisungsSummenMitAuswahlStart();
        return xSessionVerwaltung.setzeUEnde("uReportsStart", true, false, eclUserLoginM.getKennung());
    }

    /*++++++++++++++++Sammelkarten++++++++++++++++++++++++*/
    public String doSammelkartenKurzOhneWeisung() {
        if (uMenueSession.menueReportsEinzeln(114) == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uMenue", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }

        uReportsSession.clear();
        uReports.sammelkartenKurzOhneWeisung();

        return xSessionVerwaltung.setzeUEnde("uReportsAnzeige", true, false, eclUserLoginM.getKennung());
    }

    public String doSammelkartenKurzOhneWeisungAuswahl() {
        if (uMenueSession.menueReportsEinzeln(115) == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uMenue", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }

        uReportsSession.clear();
        uReports.sammelkartenKurzOhneWeisungAuswahlStart();
        return xSessionVerwaltung.setzeUEnde("uReportsStart", true, false, eclUserLoginM.getKennung());
    }

    public String doSammelkartenMitWeisung() {
        if (uMenueSession.menueReportsEinzeln(116) == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uMenue", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }

        uReportsSession.clear();
        uReports.sammelkartenMitWeisung();

        return xSessionVerwaltung.setzeUEnde("uReportsAnzeige", true, false, eclUserLoginM.getKennung());
    }

    public String doSammelkartenMitWeisungAuswahl() {
        if (uMenueSession.menueReportsEinzeln(117) == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uMenue", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }

        uReportsSession.clear();
        uReports.sammelkartenMitWeisungAuswahlStart();
        return xSessionVerwaltung.setzeUEnde("uReportsStart", true, false, eclUserLoginM.getKennung());
    }

    public String doSammelkartenMitWeisungUndAktionaere() {
        if (uMenueSession.menueReportsEinzeln(118) == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uMenue", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }

        uReportsSession.clear();
        uReports.sammelkartenMitWeisungUndAktionaere();

        return xSessionVerwaltung.setzeUEnde("uReportsAnzeige", true, false, eclUserLoginM.getKennung());
    }

    public String doSammelkartenMitWeisungUndAktionaereAuswahl() {
        if (uMenueSession.menueReportsEinzeln(119) == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uMenue", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }

        uReportsSession.clear();
        uReports.sammelkartenMitWeisungUndAktionaereAuswahlStart();
        return xSessionVerwaltung.setzeUEnde("uReportsStart", true, false, eclUserLoginM.getKennung());
    }

    public String doSammelkartenExportJNE() {
        if (uMenueSession.menueReportsEinzeln(120) == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uMenue", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }

        uReportsSession.clear();
        uReports.sammelkartenExportJNE();

        return xSessionVerwaltung.setzeUEnde("uReportsAnzeige", true, false, eclUserLoginM.getKennung());
    }

    public String doSammelkartenExportJNEAuswahl() {
        if (uMenueSession.menueReportsEinzeln(121) == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uMenue", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }

        uReportsSession.clear();
        uReports.sammelkartenExportJNEAuswahlStart();
        return xSessionVerwaltung.setzeUEnde("uReportsStart", true, false, eclUserLoginM.getKennung());
    }

    public String doSammelkartenExportStimmen() {
        if (uMenueSession.menueReportsEinzeln(122) == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uMenue", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }

        uReportsSession.clear();
        uReports.sammelkartenExportStimmen();

        return xSessionVerwaltung.setzeUEnde("uReportsAnzeige", true, false, eclUserLoginM.getKennung());
    }

    public String doSammelkartenExportStimmenAuswahl() {
        if (uMenueSession.menueReportsEinzeln(123) == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uMenue", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }

        uReportsSession.clear();
        uReports.sammelkartenExportStimmenAuswahlStart();
        return xSessionVerwaltung.setzeUEnde("uReportsStart", true, false, eclUserLoginM.getKennung());
    }

    /*++++++++++++++Willenserklärungen+++++++++++++++++++*/
    public String doWillenserklaerungenAlle() {
        if (uMenueSession.menueReportsEinzeln(110) == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uMenue", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }

        uReportsSession.clear();
        uReports.willenserklaerungenAlleStart();
        return xSessionVerwaltung.setzeUEnde("uReportsStart", true, false, eclUserLoginM.getKennung());

    }

    public String doWillenserklaerungenEingangswege() {
        if (uMenueSession.menueReportsEinzeln(110) == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uMenue", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }

        uReportsSession.clear();
        uReports.willenserklaerungenEingangswegeStart();
        return xSessionVerwaltung.setzeUEnde("uReportsAnzeige", true, false, eclUserLoginM.getKennung());

    }

    
    
    public void doStimmausschlusslisteCockpit() {
        if (uMenueSession.menueReportsEinzeln(3) == false) {
            return;
        }
        if (!xSessionVerwaltung.pruefeUStart("uMenue", "")) {
            xSessionVerwaltung.setzeUEnde();
            return;
        }

        uReportsSession.clear();
        uReports.stimmausschlusslisteCockpit();

        xSessionVerwaltung.setzeUEnde();
    }

    public String doStimmausschlussliste() {
        if (uMenueSession.menueReportsEinzeln(3) == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uMenue", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }

        uReportsSession.clear();
        uReports.stimmausschlussliste();

        return xSessionVerwaltung.setzeUEnde("uReportsAnzeige", true, false, eclUserLoginM.getKennung());
    }

    public String doElekEinlRegistrierteAktionaere() {
        if (uMenueSession.menueReportsEinzeln(130) == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uMenue", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }

        uReportsSession.clear();
        uReports.elekEinlRegistrierteAktionaereStart();

        return xSessionVerwaltung.setzeUEnde("uReportsStart", true, false, eclUserLoginM.getKennung());
    }

    public String doElekEinlRegistrierteAktionaereNichtHVAngemeldet() {
        if (uMenueSession.menueReportsEinzeln(131) == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uMenue", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }

        uReportsSession.clear();
        uReports.elekEinlRegistrierteAktionaereNichtHVAngemeldetStart();

        return xSessionVerwaltung.setzeUEnde("uReportsStart", true, false, eclUserLoginM.getKennung());
    }

    public void doNegativlisteCockpit() {
        if (uMenueSession.menueReportsEinzeln(4) == false) {
            return;
        }
        if (!xSessionVerwaltung.pruefeUStart("uMenue", "")) {
            xSessionVerwaltung.setzeUEnde();
            return;
        }

        uReportsSession.clear();
        uReports.negativlisteCockpit();

        xSessionVerwaltung.setzeUEnde();
    }

    
    /*++++++++++++++++++++++++Sonder-Reports+++++++++++++++++++++++++++++++++++*/
    public String doArbeitsplatzStatistik() {
        if (!xSessionVerwaltung.pruefeUStart("uMenue", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }
        uReportsSession.clear();
        uReports.arbeitsplatzStatistikStart();
        return xSessionVerwaltung.setzeUEnde("uReportsStart", true, false, eclUserLoginM.getKennung());
    }

    public String doBarcodesEintrittskarten() {
        if (!xSessionVerwaltung.pruefeUStart("uMenue", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }
        uReportsSession.clear();
        uReports.barcodesEintrittskartenStart();
        return xSessionVerwaltung.setzeUEnde("uReportsStart", true, false, eclUserLoginM.getKennung());
    }

    public String dobarcodesEinladungen() {
        if (!xSessionVerwaltung.pruefeUStart("uMenue", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }
        uReportsSession.clear();
        uReports.barcodesEinladungenStart();
        return xSessionVerwaltung.setzeUEnde("uReportsStart", true, false, eclUserLoginM.getKennung());
    }

    /*+++++++++++++++++++remote-Aktienregister+++++++++++++++++++++++++++++++++++++++++*/
    public String doRemoteARTickets() {
        if (!xSessionVerwaltung.pruefeUStart("uMenue", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }
        uReportsSession.clear();
        uReports.remoteARTickets();
        return xSessionVerwaltung.setzeUEnde("uReportsAnzeige", true, false, eclUserLoginM.getKennung());
    }

    public String doRemoteKontaktanfragenTickets() {
        if (!xSessionVerwaltung.pruefeUStart("uMenue", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }
        uReportsSession.clear();
        uReports.remoteKontaktanfragenTickets();
        return xSessionVerwaltung.setzeUEnde("uReportsAnzeige", true, false, eclUserLoginM.getKennung());
    }

    /**++++++++++++++++++++Veranstaltungen++++++++++++++++++++++++++++++++++++++++++++*/
    public String doVeranstaltungTeilnehmerliste() {
        if (!xSessionVerwaltung.pruefeUStart("uMenue", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }
        uReportsSession.clear();
        uReports.veranstaltungTeilnehmerStart();
        return xSessionVerwaltung.setzeUEnde("uReportsStart", true, false, eclUserLoginM.getKennung());
    }

    /**++++++++++++++++++++VeranstaltungsManagement - Zusammenfassung++++++++++++++++++++++++++++++++++++++++++++*/
    public String doVeranstaltungsmanagement() {
        if (!xSessionVerwaltung.pruefeUStart("uMenue", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }
        uReportsSession.clear();
        uReports.veranstaltungsManagementStart();
        return xSessionVerwaltung.setzeUEnde("uReportsStart", true, false, eclUserLoginM.getKennung());
    }

    /**++++++++++++++++++++VeranstaltungsManagement - Detailteilnehmerlisten++++++++++++++++++++++++++++++++++++++++++++*/
    public String doVeranstaltungsmanagementDetail() {
        if (!xSessionVerwaltung.pruefeUStart("uMenue", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }
        uReportsSession.clear();
        uReports.veranstaltungsManagementDetailStart();
        return xSessionVerwaltung.setzeUEnde("uReportsStart", true, false, eclUserLoginM.getKennung());
    }

    /**++++++++++++++++++++ErstregistrierungenPortal++++++++++++++++++++++++++++++++++++++++++++*/
    public String doErstregistrierungenPortalReporting() {
        if (!xSessionVerwaltung.pruefeUStart("uMenue", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }
        uReportsSession.clear();
        uReports.erstregistrierungenPortalStart();
        return xSessionVerwaltung.setzeUEnde("uReportsStart", true, false, eclUserLoginM.getKennung());
    }

    /**++++++++++++++++++++Freiwillige Anmeldungen++++++++++++++++++++++++++++++++++++++++++++*/
    public String doFreiwilligeAnmeldungen() {
        if (!xSessionVerwaltung.pruefeUStart("uMenue", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }
        uReportsSession.clear();
        uReports.freiwilligeAnmeldungenStart();
        return xSessionVerwaltung.setzeUEnde("uReportsStart", true, false, eclUserLoginM.getKennung());
    }
    
    
    /*****************Vollmachten-Workflow für ku178***********************/
    public void doWorkflowImportierenPDF() {
        if (uMenueSession.menueWorkflowAdmin() == false) {
            return;
        }
        if (!xSessionVerwaltung.pruefeUStart("uMenue", "")) {
            xSessionVerwaltung.setzeUEnde();
            return;
        }

        uWorkflow.importierenPdf();
        xSessionVerwaltung.setzeUEnde();
    }

    public String doWorkflowVollmachtspruefungStandard() {
        if (uMenueSession.menueWorkflowBasis() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uMenue", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "";
        }
        eclDbM.openAll();
        eclDbM.openWeitere();
        uWorkflow.init();
        eclDbM.closeAll();
        return xSessionVerwaltung.setzeUEnde("uWorkflowVollmachtenStandard", true, false, eclUserLoginM.getKennung());
    }

    public String doWorkflowVollmachtspruefungErweitert() {
        if (uMenueSession.menueWorkflowSpezial() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uMenue", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "";
        }
        eclDbM.openAll();
        eclDbM.openWeitere();
        uWorkflow.initSpezial();
        eclDbM.closeAll();
        return xSessionVerwaltung.setzeUEnde("uWorkflowVollmachtenErweitert", true, false, eclUserLoginM.getKennung());
    }

    public String doWorkflowVollmachtseingabe() {
        if (uMenueSession.menueWorkflowSpezial() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uMenue", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "";
        }
        eclDbM.openAll();
        eclDbM.openWeitere();
        uWorkflow.init();
        eclDbM.closeAll();
        return xSessionVerwaltung.setzeUEnde("uWorkflowVollmachtseingabe", true, false, eclUserLoginM.getKennung());
    }

    public String doWorkflowVollmachtspruefungStatistik() {
        if (uMenueSession.menueWorkflowSpezial() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uMenue", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "";
        }
        uWorkflowStatistik.statistikAusfuehren();
        return xSessionVerwaltung.setzeUEnde("uWorkflowStatistik", true, false, eclUserLoginM.getKennung());
    }

    
    public String doWorkflowVollmachtspruefungGesetzlicheVertreter() {
        if (uMenueSession.menueWorkflowAuswahl() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uMenue", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "";
        }
        eclDbM.openAll();
        eclDbM.openWeitere();
        uWorkflow.initGesetzlicheVertreter();
        eclDbM.closeAll();
        return xSessionVerwaltung.setzeUEnde("uWorkflowManuellSuchen", true, false, eclUserLoginM.getKennung());
    }

    public String doWorkflowVollmachtspruefungVollmachten() {
        if (uMenueSession.menueWorkflowAuswahl() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uMenue", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "";
        }
        eclDbM.openAll();
        eclDbM.openWeitere();
        uWorkflow.initVollmachten();
        eclDbM.closeAll();
        return xSessionVerwaltung.setzeUEnde("uWorkflowManuellSuchen", true, false, eclUserLoginM.getKennung());
    }

    /**++++++++++++++++++++++VeranstaltungsManagement (Neu)+++++++++++++++++++++++++*/
    public String doVeranstaltungenSimulation() {
        if (!xSessionVerwaltung.pruefeUStart("uMenue", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }
        uVeranstaltungen.simulationInit(false);
        return xSessionVerwaltung.setzeUEnde("uVeranstaltungenSimulation", true, false, eclUserLoginM.getKennung());
    }

    /**+++++++++++++++++++++++Verschiedenes+++++++++++++++++++++++++++++++++++++++++++*/
    public String doVerschiedenesUnterlagen() {
        if (eclUserLoginM.pruefe_uportal_verschiedenes_unterlagen() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uMenue", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }
        uSession.clearFehlermeldung();
        
        uUnterlagenPdfs.init(false);
        
         return xSessionVerwaltung.setzeUEnde("uUnterlagenPdfs", false, false, eclUserLoginM.getKennung());
    }
    
    
    /******************************Import / Export**********************************/
 
  
    public String doExportPasswoerter() {
        if (!xSessionVerwaltung.pruefeUStart("uMenue", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "";
        }
        
        uImportExport.exportPasswoerter();
        
        return xSessionVerwaltung.setzeUEnde("", true, false, eclUserLoginM.getKennung());
        
    }

    public String doImportGaeste() {
        if (!xSessionVerwaltung.pruefeUStart("uMenue", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "";
        }
        
        uImportExport.importGaesteStart();
        
        return xSessionVerwaltung.setzeUEnde("uReportsStart", true, false, eclUserLoginM.getKennung());
        
    }

    /*****************************Datenmanipulation*************************************/
    public String doDatenmanipulationMandantZuruecksetzen() {
        if (uMenueSession.menueDatenmanipulation() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uMenue", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "";
        }
        
        uDatenmanipulation.zuruecksetzenMandantStart();
        
        return xSessionVerwaltung.setzeUEnde("uDatenmanipulationStart", true, false, eclUserLoginM.getKennung());
    }
    
    public String doDatenmanipulationLoeschenMeldebestandHV() {
        if (uMenueSession.menueDatenmanipulation() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uMenue", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "";
        }
        
        uDatenmanipulation.loeschenMeldebestandHVStart();
        
        return xSessionVerwaltung.setzeUEnde("uDatenmanipulationStart", true, false, eclUserLoginM.getKennung());
    }
    
    public String doDatenmanipulationLoeschenAktienregister() {
        if (uMenueSession.menueDatenmanipulation() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uMenue", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "";
        }
        
        uDatenmanipulation.loeschenAktienregisterStart();
        
        return xSessionVerwaltung.setzeUEnde("uDatenmanipulationStart", true, false, eclUserLoginM.getKennung());
    }
    
    public String doDatenmanipulationLoeschenAbstimmungsagenda() {
        if (uMenueSession.menueDatenmanipulation() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uMenue", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "";
        }
        
        uDatenmanipulation.loeschenAbstimmungsagendaStart();
        
        return xSessionVerwaltung.setzeUEnde("uDatenmanipulationStart", true, false, eclUserLoginM.getKennung());
    }
    
    
    public String doDatenmanipulationZuruecksetzenHinweiseGelesen() {
        if (uMenueSession.menueDatenmanipulation() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uMenue", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "";
        }
        
        uDatenmanipulation.zuruecksetzenHinweiseGelesenStart();
        
        return xSessionVerwaltung.setzeUEnde("uDatenmanipulationStart", true, false, eclUserLoginM.getKennung());
    }

    public String doDatenmanipulationZuruecksetzenHinweisElekVersand() {
        if (uMenueSession.menueDatenmanipulation() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uMenue", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "";
        }
        
        uDatenmanipulation.zuruecksetzenHinweisElekVersandStart();
        
        return xSessionVerwaltung.setzeUEnde("uDatenmanipulationStart", true, false, eclUserLoginM.getKennung());
    }

    public String doDatenmanipulationZuruecksetzenPasswort() {
        if (uMenueSession.menueDatenmanipulation() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uMenue", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "";
        }
        
        uDatenmanipulation.zuruecksetzenPasswortStart();
        
        return xSessionVerwaltung.setzeUEnde("uDatenmanipulationStart", true, false, eclUserLoginM.getKennung());
    }

    
    public String doDatenmanipulationZusaetzlichesInitialpasswort() {
        if (uMenueSession.menueDatenmanipulation() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uMenue", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "";
        }
        
        uDatenmanipulation.zusaetzlichesInitialpasswortStart();
        
        return xSessionVerwaltung.setzeUEnde("uDatenmanipulationStart", true, false, eclUserLoginM.getKennung());
    }

    
    public String doDatenmanipulationAktionaereAnmeldenOhneEK() {
        if (uMenueSession.menueDatenmanipulation() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uMenue", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "";
        }
        
        uDatenmanipulation.aktionaereAnmeldenStart(false);
        
        return xSessionVerwaltung.setzeUEnde("uDatenmanipulationStart", true, false, eclUserLoginM.getKennung());
    }

    public String doDatenmanipulationAktionaereAnmeldenMitEK() {
        if (uMenueSession.menueDatenmanipulation() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uMenue", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "";
        }
        
        uDatenmanipulation.aktionaereAnmeldenStart(true);
        
        return xSessionVerwaltung.setzeUEnde("uDatenmanipulationStart", true, false, eclUserLoginM.getKennung());
    }

    public String doDatenmanipulationku217VertreterVorbereiten() {
        if (uMenueSession.menueDatenmanipulation() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uMenue", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "";
        }
        
        uDatenmanipulation.ku217VertreterVorbereitenStart();
        
        return xSessionVerwaltung.setzeUEnde("uDatenmanipulationStart", true, false, eclUserLoginM.getKennung());
    }

    public String doDatenmanipulationku217AbstimmendePraesentSetzen() {
        if (uMenueSession.menueDatenmanipulation() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uMenue", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "";
        }
        
        uDatenmanipulation.ku217AbstimmendePraesentSetzenStart();
        
        return xSessionVerwaltung.setzeUEnde("uDatenmanipulationStart", true, false, eclUserLoginM.getKennung());
    }

    public String doDatenmanipulationMandantenAktionaersdatenLoeschen() {
        if (uMenueSession.menueSonderFunktionen() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uMenue", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "";
        }
        uDatenmanipulation.mandantenAktionaersdatenLoeschenStart();
        
        return xSessionVerwaltung.setzeUEnde("uDatenmanipulationStart", true, false, eclUserLoginM.getKennung());
    }


    public String doDatenmanipulationTestbestandAnlegen() {
        if (uMenueSession.menueDatenmanipulation() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uMenue", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "";
        }
        
        uDatenmanipulation.testbestandAnlegenStart();
        
        return xSessionVerwaltung.setzeUEnde("uDatenmanipulationStart", true, false, eclUserLoginM.getKennung());
    }

    public String doDatenmanipulationSammelkartenAnlegen() {
        if (uMenueSession.menueDatenmanipulation() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uMenue", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "";
        }
        
        uDatenmanipulation.sammelkartenAnlegenStart();
        
        return xSessionVerwaltung.setzeUEnde("uDatenmanipulationStart", true, false, eclUserLoginM.getKennung());
    }
 
    public String doDatenmanipulationDemoAktionaereZuruecksetzen() {
        if (uMenueSession.menueDemoAktionaereZuruecksetzen() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uMenue", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "";
        }
        
        uDatenmanipulation.demoAktionaereZuruecksetzenStart();
        
        return xSessionVerwaltung.setzeUEnde("uDatenmanipulationStart", true, false, eclUserLoginM.getKennung());
    }
 
    /**Noch Datenmanipulation, aber in Spezialfunktionen*/
    public String doDatenmanipulationMandantennummerAendern() {
        if (uMenueSession.menueKonfiguration() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uMenue", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "";
        }
        
        uDatenmanipulation.mandantennummerAendernStart();
        
        return xSessionVerwaltung.setzeUEnde("uDatenmanipulationStart", true, false, eclUserLoginM.getKennung());
    }

    
    /******************************Konfiguration*****************************/
    public String doKonfigurationPflegePortaltexteStandard() {
        if (uMenueSession.menuePflegePortaltexteStandard() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uMenue", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "";
        }

        uPflegePortaltexte.init(1);

        return xSessionVerwaltung.setzeUEnde("uPflegePortaltexte", true, false, eclUserLoginM.getKennung());
    }

    public String doKonfigurationPflegePortaltexteMandant() {
        if (uMenueSession.menuePflegePortaltexteMandant() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uMenue", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "";
        }

        uPflegePortaltexte.init(2);

        return xSessionVerwaltung.setzeUEnde("uPflegePortaltexte", true, false, eclUserLoginM.getKennung());
   }
 
    public String doKonfigurationBasisSetsAbgleichen() {
        if (uMenueSession.menuePflegePortaltexteStandard() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uMenue", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "";
        }
        uReportsSession.clear();
        uReports.basisSetAbgleichStart();

        return xSessionVerwaltung.setzeUEnde("uReportsStart", true, false, eclUserLoginM.getKennung());
   }

    public String doKonfigurationBasisSetAusgeben() {
        if (uMenueSession.menuePflegePortaltexteStandard() == false && uMenueSession.menuePflegePortaltexteMandant() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uMenue", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "";
        }
        uReportsSession.clear();
        uReports.basisSetAusgeben(true);

        return xSessionVerwaltung.setzeUEnde("uReportsAnzeige", true, false, eclUserLoginM.getKennung());
   }

    public String doKonfigurationMandantentexteAusgeben() {
        if (uMenueSession.menuePflegePortaltexteStandard() == false && uMenueSession.menuePflegePortaltexteMandant() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uMenue", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "";
        }
        uReportsSession.clear();
        uReports.basisSetAusgeben(false);

        return xSessionVerwaltung.setzeUEnde("uReportsAnzeige", true, false, eclUserLoginM.getKennung());
   }

   public String doKonfigurationPflegeFehlertextePortalStandard() {
        if (uMenueSession.menuePflegePortaltexteStandard() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uMenue", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "";
        }

        uPflegeFehlertextePortal.initStandard();

        return xSessionVerwaltung.setzeUEnde("uPflegeFehlertextePortal", true, false, eclUserLoginM.getKennung());

    }

    public String doKonfigurationPflegeFehlertextePortalMandant() {
        if (uMenueSession.menuePflegePortaltexteStandard() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uMenue", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "";
        }

        uPflegeFehlertextePortal.initMandant();

        return xSessionVerwaltung.setzeUEnde("uPflegeFehlertextePortal", true, false, eclUserLoginM.getKennung());

    }

    public String doKonfigurationSchluesselLaenderHolen() {
        if (uMenueSession.menueKonfiguration_schluessel_laenderHolen() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uMenue", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "";
        }

        eclDbM.openAll();
        eclDbM.openWeitere();
        brMInit.initOhneMitglied();
        brMSchluesselHolen.laenderHolen();
        brMSchluesselHolen.plzOrtHolen();
        brMSchluesselHolen.blzBankHolen();
        eclDbM.closeAll();
        
        uSession.setFehlermeldung("Länder/Orte/Banken wurden aktualisiert");
        return xSessionVerwaltung.setzeUEnde("uMenue", false, false, eclUserLoginM.getKennung());

    }

    public String doKonfigurationServerVerbundTest1() {
        return ausfuehrenKonfigurationServerVerbundTest(1);
    }

    public String doKonfigurationServerVerbundTest2() {
        return ausfuehrenKonfigurationServerVerbundTest(2);
    }
    public String doKonfigurationServerVerbundTest3() {
        return ausfuehrenKonfigurationServerVerbundTest(3);
    }
    public String doKonfigurationServerVerbundTest4() {
        return ausfuehrenKonfigurationServerVerbundTest(4);
    }
    public String doKonfigurationServerVerbundTest5() {
        return ausfuehrenKonfigurationServerVerbundTest(5);
    }
 
    public String ausfuehrenKonfigurationServerVerbundTest(int pServerNummer) {
        
        if (!xSessionVerwaltung.pruefeUStart("uMenue", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "";
        }

        eclDbM.openAll();
        eclDbM.openWeitere();
        
        int rc=blMTeilnehmerKommunikation.testServer(pServerNummer);
        
        String ergText="";
        switch (rc) {
        case -2:ergText="Kommunikationsfehler";break;
        case -1:ergText="ServerNr in Parametern nicht aktiviert";break;
        case 1:ergText="Über Web-Service erreicht - ok";break;
        case 2:ergText="Server ist lokaler Server";break;
        }
        
        uSession.setFehlermeldung("Server-Verbund Test Server 1 - Ergebnis: "+Integer.toString(rc)+" "+ergText);
        return xSessionVerwaltung.setzeUEnde("uMenue", false, false, eclUserLoginM.getKennung());

    }

    public String doKonfigurationEntwicklerTest1() {
        
        try {
        if (!xSessionVerwaltung.pruefeUStart("uMenue", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "";
        }

        eclDbM.openAll();
        int rc=eclDbM.getDbBundle().dbBasis.getInterneIdentAntraege();
        System.out.println("rc="+rc);
        /*AAAAA Willenserklaerung*/
        CaTest.sleep(20, "Nach Ident-Vergabe");

        eclDbM.closeAll();
        }
     catch (Exception e1) {
         eclDbM.closeAll();
     }
        
        
        return xSessionVerwaltung.setzeUEnde("uMenue", false, false, eclUserLoginM.getKennung());

    }

    /****************************Sonstiges*******************************/
    public String doPasswortAendern() {
        if (!xSessionVerwaltung.pruefeUStart("uMenue", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }
        uPasswortAendernSession.clear();
        uPasswortAendern.init("uMenue");
        return xSessionVerwaltung.setzeUEnde("uPasswortAendern", true, false, eclUserLoginM.getKennung());
    }
    
    
    public String doMandantenKennungen() {
        if (!xSessionVerwaltung.pruefeUStart("uMenue", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }
        uMandantenKennungSession.clear();
        uMandantenKennung.init(false);
        return xSessionVerwaltung.setzeUEnde("uMandantenKennung", true, false, eclUserLoginM.getKennung());
    }
    
    

    public String doZurueck() {
        /*Aktuelle Seite holen*/
        String aktuelleSeite = xSessionVerwaltung.getAktuelleMaske();
        /*In Abhängigkeit dieser Seite dann die Zurückseite bestimmen und ggf. Aktionen durchführen*/
        String zielSeite = "";
        switch (aktuelleSeite) {
        case "uMenue": {
            eclParamM.getClGlobalVar().mandant = 0;
            eclParamM.getClGlobalVar().hvJahr = 0;
            eclParamM.getClGlobalVar().hvNummer = "";
            eclParamM.getClGlobalVar().datenbereich = "";
            zielSeite = "uMenueAuswahlMandant";
            break;
        }
        
        case "uRedakteur":
            zielSeite="uMenueAuswahlMandant";
            break;
        
        case "uNachrichtSenden": {
            zielSeite = uNachrichtSendenSession.getAufrufMaske();
            break;
        }
        case "uWortmeldungen": {
            zielSeite = "uMenue";
            break;
        }
        case "uWortmeldungenView": 
        case "uWortmeldungenTelefonie": 
        case "uWortmeldungenTest": {
            zielSeite = "uMenue";
            break;
        }
        case "uWortmeldungenRegie": {
            zielSeite = "uMenue";
            break;
        }
        case "uPflegeNachrichtVerwendungsCode": {
            zielSeite = "uMenueAuswahlMandant";
            break;
        }
        case "uPflegeNachrichtBasisText": {
            zielSeite = "uMenueAuswahlMandant";
            break;
        }
        case "uTOWeisungsEmpfehlung": {
            zielSeite = "uMenue";
            break;
        }
        case "uPasswortAendern": {
            zielSeite = uPasswortAendernSession.getAufrufSeite();
            break;
        }
        case "uWeisung": {
            zielSeite = "uMenue";
            break;
        }
        case "uWeisungsEmpfehlungAntwort": {
            zielSeite = uWeisungsEmpfehlungAntwortSession.getAufrufMaske();
            if (zielSeite.equals("uMenueAuswahlMandant")) {
                eclParamM.getClGlobalVar().mandant = 0;
                eclParamM.getClGlobalVar().hvJahr = 0;
                eclParamM.getClGlobalVar().hvNummer = "";
                eclParamM.getClGlobalVar().datenbereich = "";
            }
            break;
        }
        case "uGastkarteNeu": 
        case "uGastkarteNeuQuittung": 
        case "uGastkarteSuchen": 
        case "uGastkarteAendern": 
        case "uGastkarteListe": 
        case "uGastkarteIntern": 
        case "uGastkarteInsti": 
        case "uGastkarteBlanko":
            zielSeite = "uMenue";
            break;
            
       case "uServicelineAnfrage": {
            zielSeite = "uMenue";
            break;
        }
        case "uVirtuelleHV": 
        case "uPdfsAnzeigen":
        case "uBotschaftenVerarbeiten":
        case "uUnterlagenPdfs":
            zielSeite = "uMenue";
            break;
        
        case "uReportsStart": {
            zielSeite = "uMenue";
            break;
        }
        case "uReportsAnzeige": {
            zielSeite = "uMenue";
            break;
        }
        
        case "uDatenmanipulationStart": {
            zielSeite = "uMenue";
            break;
        }
        case "uDatenmanipulationAnzeige": {
            zielSeite = "uMenue";
            break;
        }

        case "uWorkflowVollmachtenStandard": {
            if (uWorkflowSession.isErweiterteFreigabe() == false) {
                zielSeite = "uMenue";
            } else {
                eclDbM.openAll();
                eclDbM.openWeitere();
                uWorkflow.vorbereitenListeFuerAnzeige();
                eclDbM.closeAll();
                zielSeite = "uWorkflowVollmachtenErweitert";

            }
            break;
        }
        case "uWorkflowVollmachtenErweitert": {
            zielSeite = "uMenue";
            break;
        }
        case "uWorkflowStatistik": {
            zielSeite = "uMenue";
            break;
        }
        case "uWorkflowVollmachtenDetail": {
            eclDbM.openAll();
            eclDbM.openWeitere();
            uWorkflow.vorbereitenListeFuerAnzeige();
            eclDbM.closeAll();
            zielSeite = "uWorkflowVollmachtenErweitert";
            break;
        }
        case "uMandantenKennung":
        case "uPflegePortaltexte": 
        case "uPflegeFehlertextePortal": 
            zielSeite = "uMenue";
            break;
        case "uWorkflowManuellSuchen": 
        case "uWorkflowManuellStandard":
            zielSeite = "uMenue";
            break;
        case "uVeranstaltungenSimulation":
            zielSeite = "uMenue";
            break;
        }

        return xSessionVerwaltung.setzeUEnde(zielSeite, true, false, eclUserLoginM.getKennung());
    }
}
