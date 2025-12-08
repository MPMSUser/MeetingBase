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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaString;
import de.meetingapps.meetingportal.meetComBa.BaMailM;
import de.meetingapps.meetingportal.meetComBl.BlMitteilungen;
import de.meetingapps.meetingportal.meetComEclM.EclDbM;
import de.meetingapps.meetingportal.meetComEclM.EclParamM;
import de.meetingapps.meetingportal.meetComEntities.EclMitteilung;
import de.meetingapps.meetingportal.meetComKonst.KonstPortalFunktionen;
import de.meetingapps.meetingportal.meetingCoreReport.RpBrowserAnzeigen;
import de.meetingapps.meetingportal.meetingCoreReport.RpDownloadDatei;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.Part;

@RequestScoped
@Named
public class UBotschaftenVerarbeiten {

    private @Inject EclDbM eclDbM;
    private @Inject EclParamM eclParamM;
    private @Inject XSessionVerwaltung xSessionVerwaltung;
    private @Inject BaMailM baMailm;

    private @Inject USession uSession;
    private @Inject UBotschaftenVerarbeitenSession uBotschaftenVerarbeitenSession;

    private int logDrucken = 10;

    /**Benötigt weitere*/
    public void init(boolean pBereitsOffen) {

        if (pBereitsOffen == false) {
            eclDbM.openAll();
            eclDbM.openWeitere();
        }

        BlMitteilungen blMitteilungen = new BlMitteilungen(true, eclDbM.getDbBundle(), KonstPortalFunktionen.botschaftenEinreichen);
        blMitteilungen.botschaftenListeAufbereiten();

        uBotschaftenVerarbeitenSession.setBotschaftenListe(blMitteilungen.rcBotschaftenListe);

        if (pBereitsOffen == false) {
            eclDbM.closeAll();
        }

    }


    public String doRefresh() {
        if (!xSessionVerwaltung.pruefeUStart("uBotschaftenVerarbeiten", "")) {
            xSessionVerwaltung.setzeEnde();
            return "uDlgFehler";
        }
        uSession.clearFehlermeldung();
        init(false);
        xSessionVerwaltung.setzeUEnde();
        return "";

    }

    public String doLangtextAnzeigen(EclMitteilung pBotschaft) {
        if (!xSessionVerwaltung.pruefeUStart("uBotschaftenVerarbeiten", "")) {
            xSessionVerwaltung.setzeEnde();
            return "uDlgFehler";
        }
        uSession.clearFehlermeldung();
        pBotschaft.langTextAnzeigen=true;
        xSessionVerwaltung.setzeUEnde();
        return "";

    }

    public void doPdfAnzeigen(EclMitteilung pBotschaftHeader) {
        if (!xSessionVerwaltung.pruefeUStart("uBotschaftenVerarbeiten", "")) {
            xSessionVerwaltung.setzeEnde();
            return ;
        }
        uSession.clearFehlermeldung();

        String dateiPfad = eclParamM.getParamGeraet().lwPfadGrossdokumente + "\\" + eclParamM.getMandantPfad()+"\\";
        String dateiname=pBotschaftHeader.zugeordnetePortalUnterlage.dateiname;

        RpBrowserAnzeigen rpBrowserAnzeigen = new RpBrowserAnzeigen();
        rpBrowserAnzeigen.zeigen(dateiPfad+dateiname+".pdf");

        xSessionVerwaltung.setzeUEnde();

    }

    /**pBotschaft = "Header-Mitteilung"  (mit Version==0)*/
    public String doUnterlageErstellen(EclMitteilung pBotschaft) {
        if (!xSessionVerwaltung.pruefeUStart("uBotschaftenVerarbeiten", "")) {
            xSessionVerwaltung.setzeEnde();
            return "uDlgFehler";
        }
        uSession.clearFehlermeldung();
        eclDbM.openAll();
        eclDbM.openWeitere();

        BlMitteilungen blMitteilungen=new BlMitteilungen(true, eclDbM.getDbBundle(), KonstPortalFunktionen.botschaftenEinreichen);
        blMitteilungen.botschaftenUnterlageErstellen(pBotschaft);

        /*Refresh durchführen*/
        init(true);

        eclDbM.closeAll();
        xSessionVerwaltung.setzeUEnde();
        return "";

    }

    public String doFreigabgeGesellschaft(EclMitteilung pBotschaft) {
        if (!xSessionVerwaltung.pruefeUStart("uBotschaftenVerarbeiten", "")) {
            xSessionVerwaltung.setzeEnde();
            return "uDlgFehler";
        }
        uSession.clearFehlermeldung();

        eclDbM.openAll();
        eclDbM.openWeitere();

        BlMitteilungen blMitteilungen=new BlMitteilungen(true, eclDbM.getDbBundle(), KonstPortalFunktionen.botschaftenEinreichen);
        blMitteilungen.botschaftenFreigabeVersionDurchGesellschaft(pBotschaft);

        /*Refresh durchführen*/
        init(true);

        eclDbM.closeAll();

        String hMailText="Stellungnahme Nr "+Integer.toString(pBotschaft.mitteilungIdent) + "\n\r";
        String hBetreff =eclParamM.getEmittentName()+
                " Stellungnahme freigegeben ";

        String empfaengerListe = eclParamM.getParam().paramPortal.botschaftenMailVerteiler1 + ","
                + eclParamM.getParam().paramPortal.botschaftenMailVerteiler2 + ","
                + eclParamM.getParam().paramPortal.botschaftenMailVerteiler3 + ",";

        String[] zeileSplitKomma=empfaengerListe.split(";");
        for (int i1=0;i1<zeileSplitKomma.length;i1++) {
            String empfaenger = zeileSplitKomma[i1].trim();
            if (!empfaenger.isEmpty()) {
                CaBug.druckeLog("Mitteilung senden an:" + empfaenger, logDrucken, 1);
                baMailm.senden(empfaenger, hBetreff, hMailText);
            }
        }


        xSessionVerwaltung.setzeUEnde();
        return "";

    }

    /** Hochladen zum Streamingdienstleister */
    public String doStreamHochladen(EclMitteilung pBotschaftHeader, EclMitteilung pBotschaftVersion) {
        if (!xSessionVerwaltung.pruefeUStart("uBotschaftenVerarbeiten", "")) {
            xSessionVerwaltung.setzeEnde();
            return "uDlgFehler";
        }
        uSession.clearFehlermeldung();

        eclDbM.openAll();
        eclDbM.openWeitere();

        /*Botschaft auf "Wird Hochgeladen" setzen*/
        BlMitteilungen blMitteilungen=new BlMitteilungen(true, eclDbM.getDbBundle(), KonstPortalFunktionen.botschaftenEinreichen);
        blMitteilungen.botschaftenZuStreamHochladenStart(pBotschaftHeader);

        /*Refresh durchführen*/
        init(true);

        eclDbM.closeAll();

        /*Hochladen veranlassen*/
        // uploadVideo(pBotschaftVersion.internerDateiname, pBotschaftVersion.version, pBotschaftVersion.internerDateizusatz, pBotschaftHeader.mitteilungIdent);

        xSessionVerwaltung.setzeUEnde();
        return "";

    }

    /** Bereitstellen einer PDF-Datei*/
    public String doPDFBereitstellen(EclMitteilung pBotschaftHeader, EclMitteilung pBotschaftVersion) {
        if (!xSessionVerwaltung.pruefeUStart("uBotschaftenVerarbeiten", "")) {
            xSessionVerwaltung.setzeEnde();
            return "uDlgFehler";
        }
        uSession.clearFehlermeldung();

        eclDbM.openAll();
        eclDbM.openWeitere();

        /*Botschaft auf "Wird Hochgeladen" setzen*/
        BlMitteilungen blMitteilungen=new BlMitteilungen(true, eclDbM.getDbBundle(), KonstPortalFunktionen.botschaftenEinreichen);
        int rc=blMitteilungen.botschaftenPDFBereitstellen(pBotschaftHeader, pBotschaftVersion);

        if (rc<0) {
            uSession.setFehlermeldung("Nur für PDF-Dokumente zulässig!");
            xSessionVerwaltung.setzeUEnde();
            eclDbM.closeAll();
            return "";
        }
        /*Refresh durchführen*/
        init(true);

        eclDbM.closeAll();

        xSessionVerwaltung.setzeUEnde();
        return "";

    }

    public String doStreamAnschauen(EclMitteilung pBotschaft) {
        if (!xSessionVerwaltung.pruefeUStart("uBotschaftenVerarbeiten", "")) {
            xSessionVerwaltung.setzeEnde();
            return "uDlgFehler";
        }
        uSession.clearFehlermeldung();
        // loadVideo(pBotschaft.zugeordnetePortalUnterlage.dateiname);
        xSessionVerwaltung.setzeUEnde();
        return "";

    }

    public String doOeffentlichenLinkFreigeben(EclMitteilung pBotschaftHeader) {
        if (!xSessionVerwaltung.pruefeUStart("uBotschaftenVerarbeiten", "")) {
            xSessionVerwaltung.setzeEnde();
            return "uDlgFehler";
        }
        uSession.clearFehlermeldung();

        /*Link freischalten*/
        String objekt_id=pBotschaftHeader.zugeordnetePortalUnterlage.dateiname;
        // setVideoPublic(objekt_id);

        eclDbM.openAll();
        eclDbM.openWeitere();

        BlMitteilungen blMitteilungen=new BlMitteilungen(true, eclDbM.getDbBundle(), KonstPortalFunktionen.botschaftenEinreichen);
        blMitteilungen.botschaftenStreamAlsOeffentlichFreigeben(pBotschaftHeader);

        init(true);
        eclDbM.closeAll();
        xSessionVerwaltung.setzeUEnde();
        return "";

    }

    public String doDownload(EclMitteilung pBotschaft) {
        if (!xSessionVerwaltung.pruefeUStart("uBotschaftenVerarbeiten", "")) {
            xSessionVerwaltung.setzeEnde();
            return "uDlgFehler";
        }
        uSession.clearFehlermeldung();

        String dateiPfad = eclParamM.getParamGeraet().lwPfadGrossdokumente + "\\" + eclParamM.getMandantPfad()+"\\";

        String hVersion=Integer.toString(pBotschaft.version);
        hVersion=CaString.fuelleLinksNull(hVersion, 2);
        hVersion="_"+hVersion;

        String dateiKomplett=dateiPfad+pBotschaft.internerDateiname+hVersion+"." + pBotschaft.internerDateizusatz;

        RpDownloadDatei rpDownloadDatei = new RpDownloadDatei();
        try {
            rpDownloadDatei.download(dateiKomplett);
        } catch (IOException e) {
            CaBug.drucke("001");
        }

        xSessionVerwaltung.setzeUEnde();
        return "";

    }

    /**pBotschaft=Header-Mitteilung*/
    public String doNeueVersionUploaden(EclMitteilung pBotschaft) {
        if (!xSessionVerwaltung.pruefeUStart("uBotschaftenVerarbeiten", "")) {
            xSessionVerwaltung.setzeEnde();
            CaBug.druckeLog("Dialogfehler", logDrucken, 10);
            return "uDlgFehler";
        }
        uSession.clearFehlermeldung();

        Part pDateiAnhang=pBotschaft.getDateiHochladen();
        if (pDateiAnhang==null) {
            CaBug.druckeLog("Datei nicht ausgewählt", logDrucken, 10);
            uSession.setFehlermeldung("Bitte Datei auswählen!");
            xSessionVerwaltung.setzeUEnde();
            return "";
        }

        /*Datei übertragen - immer internen Dateinamen verwenden. Zusatz aus
         * Quell-Name bestimmen*/
        String rcExternerFilename = pDateiAnhang.getSubmittedFileName();
        int posPunkt = rcExternerFilename.lastIndexOf(".");
        if (posPunkt < 1) {
            CaBug.drucke("001");
        }
        String rcInternerFilenameZusatz = rcExternerFilename.substring(posPunkt + 1);

        eclDbM.openAll();
        eclDbM.openWeitere();

        BlMitteilungen blMitteilungen=new BlMitteilungen(true, eclDbM.getDbBundle(), KonstPortalFunktionen.botschaftenEinreichen);
        blMitteilungen.botschaftenNeueVersionErstellen(pBotschaft, rcInternerFilenameZusatz, rcExternerFilename);

        /*Refresh*/
        init(true);
        eclDbM.closeAll();


        try {
            /*Pfad, abgeschlossen mit \\*/
            String dateiPfad = eclParamM.getParamGeraet().lwPfadGrossdokumente + "\\" + eclParamM.getMandantPfad()+"\\";
            String dateiname=pBotschaft.internerDateiname;
            String hVersion=CaString.fuelleLinksNull(Integer.toString(blMitteilungen.rcNeueVersion), 2);

            InputStream input = pDateiAnhang.getInputStream();
            Files.copy(input, new File(dateiPfad, dateiname+"_"+hVersion+"."+rcInternerFilenameZusatz).toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }

        xSessionVerwaltung.setzeUEnde();
        return "";

    }

    public String doListeOeffentlicherLinks() {
        if (!xSessionVerwaltung.pruefeUStart("uBotschaftenVerarbeiten", "")) {
            xSessionVerwaltung.setzeEnde();
            return "uDlgFehler";
        }
        uSession.clearFehlermeldung();
        
        // publish Link here 
        xSessionVerwaltung.setzeUEnde();
        return "";
    }


    public String doStatusSetzen(EclMitteilung pBotschaftHeader, int pStatus) {
        if (!xSessionVerwaltung.pruefeUStart("uBotschaftenVerarbeiten", "")) {
            xSessionVerwaltung.setzeEnde();
            return "uDlgFehler";
        }
        uSession.clearFehlermeldung();
        eclDbM.openAll();
        eclDbM.openWeitere();

        eclDbM.getDbBundle().dbMitteilung.setzeFunktion(KonstPortalFunktionen.botschaftenEinreichen);
        CaBug.druckeLog("Version="+pBotschaftHeader.db_version, logDrucken, 10);
        int rc=eclDbM.getDbBundle().dbMitteilung.update_status(pBotschaftHeader.mitteilungIdent, pBotschaftHeader.db_version, pStatus, 0);
        if (rc<1) {
            CaBug.drucke("001 rc="+rc);
        }

        init(true);
        eclDbM.closeAll();
        xSessionVerwaltung.setzeUEnde();
        return "";

    }
}
