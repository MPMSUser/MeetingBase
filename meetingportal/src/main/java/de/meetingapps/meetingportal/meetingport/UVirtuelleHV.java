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

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaDatumZeit;
import de.meetingapps.meetingportal.meetComBl.BlKTracking;
import de.meetingapps.meetingportal.meetComBl.BlMitteilungen;
import de.meetingapps.meetingportal.meetComBlManaged.BlMVirtuelleHV;
import de.meetingapps.meetingportal.meetComEclM.EclAbstimmungSetM;
import de.meetingapps.meetingportal.meetComEclM.EclDbM;
import de.meetingapps.meetingportal.meetComEclM.EclUserLoginM;
import de.meetingapps.meetingportal.meetComEntities.EclMitteilung;
import de.meetingapps.meetingportal.meetComEntities.EclMitteilungBestand;
import de.meetingapps.meetingportal.meetComKonst.KonstPortalFunktionen;
import de.meetingapps.meetingportal.meetComKonst.KonstSkIst;
import de.meetingapps.meetingportal.meetComReports.RepWillenserklaerungenAktionaer;
import de.meetingapps.meetingportal.meetingCoreReport.RpBrowserAnzeigen;
import de.meetingapps.meetingportal.meetingCoreReport.RpDownloadDatei;
import de.meetingapps.meetingportal.meetingCoreReport.RpDrucken;
import de.meetingapps.meetingportal.meetingportTController.TMitteilung;
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
public class UVirtuelleHV {

//    private boolean logDrucken = true;

    @Inject
    private EclDbM eclDbM;
    @Inject
    private EclUserLoginM eclUserLoginM;
    @Inject
    private XSessionVerwaltung xSessionVerwaltung;
    private @Inject EclAbstimmungSetM eclAbstimmungSetM;

    @Inject
    private UVirtuelleHVSession uVirtuelleHVSession;

    @Inject private TMitteilung tMitteilung;
    @Inject private BlMVirtuelleHV blMVirtuelleHV;
    
    /**Initialisieren - vor Aufruf von UTOWeisungsEmpfehlung
     *
     * Open/Close wird im aufrufenden Modul geregelt.*/
    public void init() {
        uVirtuelleHVSession.init();
    }

    /** Open/Close wird im aufrufenden Modul geregelt.*/
    public void fragenCSVAlle() {
        erzeugen(true, KonstPortalFunktionen.fragen);
        uVirtuelleHVSession.setPortalFunktion(KonstPortalFunktionen.fragen);
        uVirtuelleHVSession.setSubFunktion(1);
        uVirtuelleHVSession.setAnzeige("alle Fragen als CSV");
    }

    /** Open/Close wird im aufrufenden Modul geregelt.*/
    public void fragenCSVNeue() {
        erzeugen(false, KonstPortalFunktionen.fragen);
        uVirtuelleHVSession.setPortalFunktion(KonstPortalFunktionen.fragen);
        uVirtuelleHVSession.setSubFunktion(2);
       uVirtuelleHVSession.setAnzeige("neue Fragen als CSV");
    }

    /** Open/Close wird im aufrufenden Modul geregelt.*/
    public void fragenReportAlle() {
        uVirtuelleHVSession.setPortalFunktion(KonstPortalFunktionen.fragen);
        uVirtuelleHVSession.setSubFunktion(3);
       uVirtuelleHVSession.setAnzeige("alle Fragen als Report");
    }

    public void fragenTestMail() {
        testMail(KonstPortalFunktionen.fragen);
    }
    
    /** Open/Close wird im aufrufenden Modul geregelt.*/
    public void wortmeldungenCSVAlle() {
        erzeugen(true, KonstPortalFunktionen.wortmeldungen);
        uVirtuelleHVSession.setPortalFunktion(KonstPortalFunktionen.wortmeldungen);
        uVirtuelleHVSession.setSubFunktion(1);
        uVirtuelleHVSession.setAnzeige("alle Wortmeldungen als CSV");
    }

    /** Open/Close wird im aufrufenden Modul geregelt.*/
    public void wortmeldungenCSVNeue() {
        erzeugen(false, KonstPortalFunktionen.wortmeldungen);
        uVirtuelleHVSession.setPortalFunktion(KonstPortalFunktionen.wortmeldungen);
        uVirtuelleHVSession.setSubFunktion(2);
       uVirtuelleHVSession.setAnzeige("neue Wortmeldungen als CSV");
    }

    /** Open/Close wird im aufrufenden Modul geregelt.*/
    public void wortmeldungenReportAlle() {
        uVirtuelleHVSession.setPortalFunktion(KonstPortalFunktionen.wortmeldungen);
        uVirtuelleHVSession.setSubFunktion(3);
       uVirtuelleHVSession.setAnzeige("alle Wortmeldungen als Report");
    }

    public void wortmeldungenTestMail() {
        testMail(KonstPortalFunktionen.wortmeldungen);
    }

    /** Open/Close wird im aufrufenden Modul geregelt.*/
    public void widerspruecheCSVAlle() {
        erzeugen(true, KonstPortalFunktionen.widersprueche);
        uVirtuelleHVSession.setPortalFunktion(KonstPortalFunktionen.widersprueche);
        uVirtuelleHVSession.setSubFunktion(1);
        uVirtuelleHVSession.setAnzeige("alle Widersprüche als CSV");
    }

    /** Open/Close wird im aufrufenden Modul geregelt.*/
    public void widerspruecheCSVNeue() {
        erzeugen(false, KonstPortalFunktionen.widersprueche);
        uVirtuelleHVSession.setPortalFunktion(KonstPortalFunktionen.widersprueche);
        uVirtuelleHVSession.setSubFunktion(2);
       uVirtuelleHVSession.setAnzeige("neue Widersprüche als CSV");
    }

    /** Open/Close wird im aufrufenden Modul geregelt.*/
    public void widerspruecheReportAlle() {
        uVirtuelleHVSession.setPortalFunktion(KonstPortalFunktionen.widersprueche);
        uVirtuelleHVSession.setSubFunktion(3);
       uVirtuelleHVSession.setAnzeige("alle Widersprüche als Report");
    }

    public void widerspruecheTestMail() {
        testMail(KonstPortalFunktionen.widersprueche);
    }

    /** Open/Close wird im aufrufenden Modul geregelt.*/
    public void antraegeCSVAlle() {
        erzeugen(true, KonstPortalFunktionen.antraege);
        uVirtuelleHVSession.setPortalFunktion(KonstPortalFunktionen.antraege);
        uVirtuelleHVSession.setSubFunktion(1);
        uVirtuelleHVSession.setAnzeige("alle Anträge als CSV");
    }

    /** Open/Close wird im aufrufenden Modul geregelt.*/
    public void antraegeCSVNeue() {
        erzeugen(false, KonstPortalFunktionen.antraege);
        uVirtuelleHVSession.setPortalFunktion(KonstPortalFunktionen.antraege);
        uVirtuelleHVSession.setSubFunktion(2);
       uVirtuelleHVSession.setAnzeige("neue Anträge als CSV");
    }

    /** Open/Close wird im aufrufenden Modul geregelt.*/
    public void antraegeReportAlle() {
        uVirtuelleHVSession.setPortalFunktion(KonstPortalFunktionen.antraege);
        uVirtuelleHVSession.setSubFunktion(3);
       uVirtuelleHVSession.setAnzeige("alle Anträge als Report");
    }

    public void antraegeTestMail() {
        testMail(KonstPortalFunktionen.antraege);
    }

    /** Open/Close wird im aufrufenden Modul geregelt.*/
    public void sonstigeMitteilungenCSVAlle() {
        erzeugen(true, KonstPortalFunktionen.sonstigeMitteilungen);
        uVirtuelleHVSession.setPortalFunktion(KonstPortalFunktionen.sonstigeMitteilungen);
        uVirtuelleHVSession.setSubFunktion(1);
        uVirtuelleHVSession.setAnzeige("alle Mitteilungen als CSV");
    }

    /** Open/Close wird im aufrufenden Modul geregelt.*/
    public void sonstigeMitteilungenCSVNeue() {
        erzeugen(false, KonstPortalFunktionen.sonstigeMitteilungen);
        uVirtuelleHVSession.setPortalFunktion(KonstPortalFunktionen.sonstigeMitteilungen);
        uVirtuelleHVSession.setSubFunktion(2);
       uVirtuelleHVSession.setAnzeige("neue Mitteilungen als CSV");
    }

    /** Open/Close wird im aufrufenden Modul geregelt.*/
    public void sonstigeMitteilungenReportAlle() {
        uVirtuelleHVSession.setPortalFunktion(KonstPortalFunktionen.sonstigeMitteilungen);
        uVirtuelleHVSession.setSubFunktion(3);
       uVirtuelleHVSession.setAnzeige("alle Mitteilungen als Report");
    }

    public void sonstigeMitteilungenTestMail() {
        testMail(KonstPortalFunktionen.sonstigeMitteilungen);
    }

    /** Open/Close wird im aufrufenden Modul geregelt.*/
    public void botschaftenEinreichenCSVAlle() {
        erzeugen(true, KonstPortalFunktionen.botschaftenEinreichen);
        uVirtuelleHVSession.setPortalFunktion(KonstPortalFunktionen.botschaftenEinreichen);
        uVirtuelleHVSession.setSubFunktion(1);
        uVirtuelleHVSession.setAnzeige("alle eingereichten Botschaften als CSV");
    }

    /** Open/Close wird im aufrufenden Modul geregelt.*/
    public void botschaftenEinreichenCSVNeue() {
        erzeugen(false, KonstPortalFunktionen.sonstigeMitteilungen);
        uVirtuelleHVSession.setPortalFunktion(KonstPortalFunktionen.botschaftenEinreichen);
        uVirtuelleHVSession.setSubFunktion(2);
       uVirtuelleHVSession.setAnzeige("neue eingereichten Botschaften als CSV");
    }

    /** Open/Close wird im aufrufenden Modul geregelt.*/
    public void botschaftenEinreichenReportAlle() {
        uVirtuelleHVSession.setPortalFunktion(KonstPortalFunktionen.botschaftenEinreichen);
        uVirtuelleHVSession.setSubFunktion(3);
       uVirtuelleHVSession.setAnzeige("alle eingereichten Botschaften als Report");
    }

    public void botschaftenEinreichenTestMail() {
        testMail(KonstPortalFunktionen.botschaftenEinreichen);
    }

    
    /**********************Allgemeine Funktionen************************************************/
    
    private void erzeugen(boolean pAlle, int pPortalFunktion) {

        BlMitteilungen blMitteilungen = new BlMitteilungen(true, eclDbM.getDbBundle(), pPortalFunktion);
        blMitteilungen.paramBelegen();
        int skIst=KonstSkIst.liefereSkIstZuportalFunktion(pPortalFunktion);
        
        if (pAlle) {
            blMitteilungen.holeMitteilungen(true, eclAbstimmungSetM.getAbstimmungSet(), skIst);
        } else {
            blMitteilungen.holeNeueMitteilungen(true, eclAbstimmungSetM.getAbstimmungSet(), skIst);
        }
        blMitteilungen.schreibeMitteilungenListeInCSV();
        uVirtuelleHVSession.setExportDateiname(blMitteilungen.rcDateiName);
    }

    private void testMail(int pPortalFunktion) {
        BlMitteilungen blMitteilungen=new BlMitteilungen(true, eclDbM.getDbBundle(), pPortalFunktion);
        tMitteilung.initSmall(pPortalFunktion, blMitteilungen);
        EclMitteilung lMitteilung = new EclMitteilung();
        lMitteilung.mitteilungIdent = 0;
        lMitteilung.loginIdent = 0;
        lMitteilung.anzahlAktienZumZeitpunktderMitteilung = 0;
        lMitteilung.identString = "999999";
        lMitteilung.nameVornameOrtKennung = "Max Mustermann, Musterort";
        lMitteilung.nameVornameOrt = "Max Musterman, Musterhausen";
        lMitteilung.kontaktDaten = "089 889 69 06 10";
        lMitteilung.mitteilungKurztext = "zu TOP 5";
        switch (pPortalFunktion) {
        //@formatter: off
        case KonstPortalFunktionen.fragen:lMitteilung.mitteilungLangtext = "Testmail - Hier würde dann der Fragentext stehen";break;
        case KonstPortalFunktionen.wortmeldungen:lMitteilung.mitteilungLangtext = "Testmail - Hier würde dann der Wortmeldungstext stehen";break;
        case KonstPortalFunktionen.widersprueche:lMitteilung.mitteilungLangtext = "Testmail - Hier würde dann der Widerspruchstext stehen";break;
        case KonstPortalFunktionen.antraege:lMitteilung.mitteilungLangtext = "Testmail - Hier würde dann der Antragstext stehen";break;
        case KonstPortalFunktionen.sonstigeMitteilungen:lMitteilung.mitteilungLangtext = "Testmail - Hier würde dann der Mitteilungstext stehen";break;
        case KonstPortalFunktionen.botschaftenEinreichen:lMitteilung.mitteilungLangtext = "Testmail - Hier würden dann Infos zur eingereichten Botschaft stehen";break;
         //@formatter: on
        }
        lMitteilung.zeitpunktDerMitteilung = CaDatumZeit.DatumZeitStringFuerDatenbank();
 
        blMVirtuelleHV.schickeMailFuerMitteilung(lMitteilung);
       
    }
    
    
    

    /** Open/Close wird im aufrufenden Modul geregelt.*/
    public void streamUser() {
        uVirtuelleHVSession.setPortalFunktion(90003);
        uVirtuelleHVSession.setSubFunktion(0);
        BlKTracking blKTracking = new BlKTracking(true, eclDbM.getDbBundle());
        blKTracking.liefereUser(1, eclDbM.getDbBundle().eclEmittent.hvDatum);

        uVirtuelleHVSession.setAnzeige("Stream-User am HV-Tag: " + Integer.toString(blKTracking.rcAnzahlDistinctUser));
        uVirtuelleHVSession.setExportDateiname(blKTracking.rcDateiName);

    }

    public void willenserklaerungenFuerMitteilungenReport() {
        uVirtuelleHVSession.setPortalFunktion(90002);
        uVirtuelleHVSession.setSubFunktion(0);
        
        uVirtuelleHVSession.setAnzeige(
                "Willenserklärungen für Aktionäre mit Mitteilungen");
    }

    public String willenserklaerungenFuerWiderspruecheReportErzeugen() {
        eclDbM.getDbBundle().dbMitteilungBestand.readAll_portalFunktion(KonstPortalFunktionen.widersprueche);
        List<String> aktionaere = new LinkedList<String>();
        for (int i=0;i<eclDbM.getDbBundle().dbMitteilungBestand.anzErgebnis();i++) {
            EclMitteilungBestand lMitteilungBestand=eclDbM.getDbBundle().dbMitteilungBestand.ergebnisPosition(i);
            String aktionaersnummer=lMitteilungBestand.aktionaersnummer;
            boolean gef=false;
            for (int i1=0;i1<aktionaere.size();i1++) {
                if (aktionaere.get(i1).equals(aktionaersnummer)) {
                    gef=true;
                }
            }
            if (!gef) {
                aktionaere.add(aktionaersnummer);
            }
        }
        

        RpDrucken rpDrucken = new RpDrucken();
        rpDrucken.initServer();

        RepWillenserklaerungenAktionaer repWillenserklaerungenAktionaer = new RepWillenserklaerungenAktionaer(true,
                eclDbM.getDbBundle());
        repWillenserklaerungenAktionaer.init(rpDrucken);
        repWillenserklaerungenAktionaer.druckeAktionaersListe(eclDbM.getDbBundle(), "01", aktionaere);

        return rpDrucken.drucklaufDatei;

    }

    public void doExportDownload() {
        if (eclUserLoginM.pruefe_uportal_virtuelleHV() == false) {
            return;
        }
        if (!xSessionVerwaltung.pruefeUStart("uVirtuelleHV", "")) {
            xSessionVerwaltung.setzeUEnde();
            return;
        }

        int portalFunktion = uVirtuelleHVSession.getPortalFunktion();
        int subFunktion=uVirtuelleHVSession.getSubFunktion();
        if (subFunktion == 1 || subFunktion == 2 || portalFunktion==90003) {
            RpDownloadDatei rpDownloadDatei = new RpDownloadDatei();
            try {
                rpDownloadDatei.download(uVirtuelleHVSession.getExportDateiname());
            } catch (IOException e) {
                CaBug.drucke("UVirtuelleHV.doExportDownload 001");
            }
        }

        if (portalFunktion == 90001) {
            RpBrowserAnzeigen rpBrowserAnzeigen = new RpBrowserAnzeigen();
            rpBrowserAnzeigen.zeigen(uVirtuelleHVSession.getExportDateiname());
        }

        if (subFunktion == 3) {
            eclDbM.openAll();
            RpDrucken rpDrucken = new RpDrucken();
            rpDrucken.initServer();

           int skIst=KonstSkIst.liefereSkIstZuportalFunktion(portalFunktion);
           BlMitteilungen blMitteilungen = new BlMitteilungen(true, eclDbM.getDbBundle(),
                    portalFunktion);
            blMitteilungen.paramBelegen();
            blMitteilungen.holeMitteilungen(false, eclAbstimmungSetM.getAbstimmungSet(), skIst);
            blMitteilungen.erzeugeReport(rpDrucken, "01");

            RpBrowserAnzeigen rpBrowserAnzeigen = new RpBrowserAnzeigen();
            rpBrowserAnzeigen.zeigen(rpDrucken);

            eclDbM.closeAll();
        }
         if (portalFunktion == 90002) {
            eclDbM.openAll();
            eclDbM.openWeitere();
            String hDateiname = willenserklaerungenFuerWiderspruecheReportErzeugen();

            RpBrowserAnzeigen rpBrowserAnzeigen = new RpBrowserAnzeigen();
            rpBrowserAnzeigen.zeigen(hDateiname);

            eclDbM.closeAll();
        }

        xSessionVerwaltung.setzeUEnde();
    }

}
