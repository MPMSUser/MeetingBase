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
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComBVerwaltung.BvReload;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEclM.EclDbM;
import de.meetingapps.meetingportal.meetComEclM.EclParamM;
import de.meetingapps.meetingportal.meetComEntities.EclMenueEintrag;
import de.meetingapps.meetingportal.meetComEntities.EclPortalUnterlagen;
import de.meetingapps.meetingportal.meetComKonst.KonstPMenueFunktionscode;
import de.meetingapps.meetingportal.meetingCoreReport.RpBrowserAnzeigen;
import de.meetingapps.meetingportal.meetingCoreReport.RpDownloadDatei;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.Part;

@RequestScoped
@Named
public class UUnterlagenPdfs {

    private @Inject EclParamM eclParamM;
    private @Inject XSessionVerwaltung xSessionVerwaltung;
    private @Inject EclDbM eclDbM;

    private @Inject USession uSession;
    private @Inject UUnterlagenPdfsSession uUnterlagenPdfsSession;

    private int logDrucken = 10;

    /**Benötigt weitere*/
    public void init(boolean pBereitsOffen) {

        String pfad = eclParamM.getParamGeraet().lwPfadGrossdokumente + "\\" + eclParamM.getMandantPfad();

        File verzeichnis = new File(pfad);
        verzeichnis.mkdir();

        File[] quelldateiArray = verzeichnis.listFiles();

        List<String> zieldateiArray = new LinkedList<String>();

        if (quelldateiArray != null) {
            for (int i = 0; i < quelldateiArray.length; i++) {
                if (!quelldateiArray[i].isDirectory()) {
                    String lDatei = quelldateiArray[i].getName();
                    String hDatei = lDatei.toUpperCase();
                    if (hDatei.endsWith(".PDF")) {
                        zieldateiArray.add(lDatei);
                    }
                }
            }
        }

        uUnterlagenPdfsSession.setDateienListe(zieldateiArray);

        eclDbM.openAll();
        eclDbM.openWeitere();

        eclDbM.getDbBundle().dbPortalUnterlagen.readAll();
        uUnterlagenPdfsSession.setPortalUnterlagenListe(eclDbM.getDbBundle().dbPortalUnterlagen.ergebnis());

        eclDbM.getDbBundle().dbMenueEintrag.readAll();
        List<EclMenueEintrag> menuListe = new ArrayList<EclMenueEintrag>();
        for (EclMenueEintrag eclMenueEintrag : eclDbM.getDbBundle().dbMenueEintrag.ergebnis()) {
            if (eclMenueEintrag.funktionscode == KonstPMenueFunktionscode.UNTERLAGEN) {
                menuListe.add(eclMenueEintrag);
            }
        }
        uUnterlagenPdfsSession.setMenuListe(menuListe);

        eclDbM.closeAll();
        resetModals();
    }

    public String doRefresh() {
        if (!xSessionVerwaltung.pruefeUStart("uUnterlagenPdfs", "")) {
            xSessionVerwaltung.setzeEnde();
            return "uDlgFehler";
        }
        uSession.clearFehlermeldung();
        init(false);
        xSessionVerwaltung.setzeUEnde();
        return "";
    }

    public String doDownload(String pDatei) {
        if (!xSessionVerwaltung.pruefeUStart("uUnterlagenPdfs", "")) {
            xSessionVerwaltung.setzeEnde();
            return "uDlgFehler";
        }
        uSession.clearFehlermeldung();

        String dateiPfad = eclParamM.getParamGeraet().lwPfadGrossdokumente + "\\" + eclParamM.getMandantPfad() + "\\";

        String dateiKomplett = dateiPfad + pDatei;

        RpDownloadDatei rpDownloadDatei = new RpDownloadDatei();
        try {
            rpDownloadDatei.download(dateiKomplett);
        } catch (IOException e) {
            CaBug.drucke("001");
        }

        xSessionVerwaltung.setzeUEnde();
        return "";

    }

    public void doPdfAnzeigen(String pDatei) {
        if (!xSessionVerwaltung.pruefeUStart("uUnterlagenPdfs", "")) {
            xSessionVerwaltung.setzeEnde();
            return;
        }
        uSession.clearFehlermeldung();

        String dateiPfad = eclParamM.getParamGeraet().lwPfadGrossdokumente + "\\" + eclParamM.getMandantPfad() + "\\";
        if (!pDatei.endsWith(".pdf")) {
            pDatei += ".pdf";
        }
        String dateiKomplett = dateiPfad + pDatei;

        RpBrowserAnzeigen rpBrowserAnzeigen = new RpBrowserAnzeigen();
        rpBrowserAnzeigen.zeigen(dateiKomplett);

        xSessionVerwaltung.setzeUEnde();

    }

    public String doNeueDateiHochladen() {
        if (!xSessionVerwaltung.pruefeUStart("uUnterlagenPdfs", "")) {
            xSessionVerwaltung.setzeEnde();
            return "uDlgFehler";
        }
        uSession.clearFehlermeldung();

        Part pDateiAnhang = uUnterlagenPdfsSession.getDateiHochladen();
        if (pDateiAnhang == null) {
            CaBug.druckeLog("Datei nicht ausgewählt", logDrucken, 10);
            uSession.setFehlermeldung("Bitte Datei auswählen!");
            xSessionVerwaltung.setzeUEnde();
            return "";
        }

        /*Datei übertragen - immer internen Dateinamen verwenden. Zusatz aus
         * Quell-Name bestimmen*/
        String rcExternerFilename = pDateiAnhang.getSubmittedFileName();

        /*Pfad, abgeschlossen mit \\*/
        String dateiPfad = eclParamM.getParamGeraet().lwPfadGrossdokumente + "\\" + eclParamM.getMandantPfad() + "\\";
        String dateiname = rcExternerFilename;

        try {
            Files.delete(Paths.get(dateiPfad + dateiname));
        } catch (IOException e) {
            /*Kein Fehler, da Datei vorgelöscht werden soll aber möglicherweise nicht existiert*/
        }

        try {

            InputStream input = pDateiAnhang.getInputStream();
            Files.copy(input, new File(dateiPfad, dateiname).toPath());
            if (!unterlageZuDateiExists(dateiname)) {
                portalUnterlageZuUploadAnlegen(dateiname);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        init(false);
        xSessionVerwaltung.setzeUEnde();
        return "";
    }

    private void portalUnterlageZuUploadAnlegen(String dateiname) {
        if (dateiname.endsWith(".pdf")) {
            dateiname = dateiname.replace(".pdf", "");
        }
        EclPortalUnterlagen eclPortalUnterlagen = new EclPortalUnterlagen();
        eclPortalUnterlagen.dateiname = dateiname;
        eclPortalUnterlagen.aktiv = 6;
        eclDbM.openAll();
        eclDbM.openWeitere();
        eclDbM.getDbBundle().dbPortalUnterlagen.insert(eclPortalUnterlagen);
        unterlagenAnzeigenAuto(eclDbM.getDbBundle());
        eclDbM.closeAll();
    }

    public void doUnterlageZuDatei(String dateiname) {
        if (!xSessionVerwaltung.pruefeUStart("uUnterlagenPdfs", "")) {
            xSessionVerwaltung.setzeEnde();
            return;
        }

        portalUnterlageZuUploadAnlegen(dateiname);

        init(false);
        xSessionVerwaltung.setzeUEnde();
    }

    private Boolean unterlageZuDateiExists(String dateiname) {
        for (EclPortalUnterlagen portalUnterlage : uUnterlagenPdfsSession.getPortalUnterlagenListe()) {
            if (portalUnterlage.dateiname.equals(dateiname)) {
                return true;
            }
        }
        return false;
    }

    public void doEdit(EclPortalUnterlagen eclPortalUnterlagen) {
        if (!xSessionVerwaltung.pruefeUStart("uUnterlagenPdfs", "")) {
            xSessionVerwaltung.setzeEnde();
            return;
        }
        uUnterlagenPdfsSession.setEclPortalUnterlagenSelected(eclPortalUnterlagen);
        uUnterlagenPdfsSession.setShowUnterlageBearbeiten(true);
        xSessionVerwaltung.setzeUEnde();

    }

    public void doNeueUnterlage() {
        if (!xSessionVerwaltung.pruefeUStart("uUnterlagenPdfs", "")) {
            xSessionVerwaltung.setzeEnde();
            return;
        }
        uUnterlagenPdfsSession.setShowNeueUnterlage(true);
        xSessionVerwaltung.setzeUEnde();
    }

    public void doUnterlageAenderungSpeichern() {
        if (!xSessionVerwaltung.pruefeUStart("uUnterlagenPdfs", "")) {
            xSessionVerwaltung.setzeEnde();
            return;
        }
        if (uUnterlagenPdfsSession.getEclPortalUnterlagenSelected().unterlagenbereichMenueNr > 0) {
            readUnterlagenAufSeite(uUnterlagenPdfsSession.getEclPortalUnterlagenSelected().unterlagenbereichMenueNr);
            if (uUnterlagenPdfsSession.getEclPortalUnterlagenSelected().reihenfolgeUnterlagenbereich == 0) {
                uUnterlagenPdfsSession
                        .getEclPortalUnterlagenSelected().reihenfolgeUnterlagenbereich = maxUnterlagenReiheNummer() + 1;
            }
        } else {
            uUnterlagenPdfsSession.getEclPortalUnterlagenSelected().reihenfolgeUnterlagenbereich = 0;
        }
        eclDbM.openAll();
        eclDbM.openWeitere();
        eclDbM.getDbBundle().dbPortalUnterlagen.update(uUnterlagenPdfsSession.getEclPortalUnterlagenSelected());
        unterlagenAnzeigenAuto(eclDbM.getDbBundle());
        eclDbM.closeAll();
        init(false);
        xSessionVerwaltung.setzeUEnde();
    }

    public void doNeueUnterlageLink() {
        if (!xSessionVerwaltung.pruefeUStart("uUnterlagenPdfs", "")) {
            xSessionVerwaltung.setzeEnde();
            return;
        }
        EclPortalUnterlagen eclPortalUnterlagen = new EclPortalUnterlagen();
        eclPortalUnterlagen.art = 4;
        eclPortalUnterlagen.dateiname = uUnterlagenPdfsSession.getNeueUnterlageLink();
        eclPortalUnterlagen.aktiv = 6;
        eclDbM.openAll();
        eclDbM.openWeitere();
        eclDbM.getDbBundle().dbPortalUnterlagen.insert(eclPortalUnterlagen);
        unterlagenAnzeigenAuto(eclDbM.getDbBundle());
        eclDbM.closeAll();
        init(false);
        xSessionVerwaltung.setzeUEnde();
    }

    public void doNeueUnterlageText() {

        if (!xSessionVerwaltung.pruefeUStart("uUnterlagenPdfs", "")) {
            xSessionVerwaltung.setzeEnde();
            return;
        }
        EclPortalUnterlagen eclPortalUnterlagen = new EclPortalUnterlagen();
        eclPortalUnterlagen.art = 2;
        eclPortalUnterlagen.bezeichnungDE = uUnterlagenPdfsSession.getNeueUnterlageBezeichnungDE();
        eclPortalUnterlagen.bezeichnungEN = uUnterlagenPdfsSession.getNeueUnterlageBezeichnungDE();
        eclPortalUnterlagen.aktiv = 6;
        if (eclPortalUnterlagen != null && !eclPortalUnterlagen.bezeichnungEN.equals("")) {
            eclPortalUnterlagen.dateiMehrsprachigVorhanden = 1;
        }
        eclDbM.openAll();
        eclDbM.openWeitere();
        eclDbM.getDbBundle().dbPortalUnterlagen.insert(eclPortalUnterlagen);
        unterlagenAnzeigenAuto(eclDbM.getDbBundle());
        eclDbM.closeAll();
        init(false);
        xSessionVerwaltung.setzeUEnde();
    }

    public void doNeueUnterlageUeberschrift() {
        if (!xSessionVerwaltung.pruefeUStart("uUnterlagenPdfs", "")) {
            xSessionVerwaltung.setzeEnde();
            return;
        }
        EclPortalUnterlagen eclPortalUnterlagen = new EclPortalUnterlagen();
        eclPortalUnterlagen.art = 1;
        eclPortalUnterlagen.bezeichnungDE = uUnterlagenPdfsSession.getNeueUnterlageBezeichnungDE();
        eclPortalUnterlagen.bezeichnungEN = uUnterlagenPdfsSession.getNeueUnterlageBezeichnungEN();
        eclPortalUnterlagen.aktiv = 6;
        if (!eclPortalUnterlagen.bezeichnungEN.equals("")) {
            eclPortalUnterlagen.dateiMehrsprachigVorhanden = 1;
        }
        eclDbM.openAll();
        eclDbM.openWeitere();
        eclDbM.getDbBundle().dbPortalUnterlagen.insert(eclPortalUnterlagen);
        unterlagenAnzeigenAuto(eclDbM.getDbBundle());
        eclDbM.closeAll();
        init(false);
        xSessionVerwaltung.setzeUEnde();
    }

    public void doMehrsprachigOnOff() {
        if (!xSessionVerwaltung.pruefeUStart("uUnterlagenPdfs", "")) {
            xSessionVerwaltung.setzeEnde();
            return;
        }
        if (uUnterlagenPdfsSession.getEclPortalUnterlagenSelected().dateiMehrsprachigVorhanden == 1) {
            uUnterlagenPdfsSession.getEclPortalUnterlagenSelected().dateiMehrsprachigVorhanden = 0;
        } else {
            uUnterlagenPdfsSession.getEclPortalUnterlagenSelected().dateiMehrsprachigVorhanden = 1;
        }
        xSessionVerwaltung.setzeUEnde();
    }

    public void doAbbrechenModal() {
        if (!xSessionVerwaltung.pruefeUStart("uUnterlagenPdfs", "")) {
            xSessionVerwaltung.setzeEnde();
            return;
        }
        uUnterlagenPdfsSession.setShowReihenfolgeModal(false);
        uUnterlagenPdfsSession.setUnterlagenAufSeiteListe(null);
        resetModals();
        init(false);
        xSessionVerwaltung.setzeUEnde();
    }

    public void doShowReihenfolgeModal(int menuNummer) {
        if (!xSessionVerwaltung.pruefeUStart("uUnterlagenPdfs", "")) {
            xSessionVerwaltung.setzeEnde();
            return;
        }
        readUnterlagenAufSeite(menuNummer);
        uUnterlagenPdfsSession.setShowReihenfolgeModal(true);
        xSessionVerwaltung.setzeUEnde();
    }

    /**
     * Ändert die Reihenfolger der Unterlage in der Seitenansicht
     * 0 = nach unten
     * 1 = nach oben
     * @param eclPortalUnterlagen
     * @param richtung
     */
    public void doReihenfolgeAendern(EclPortalUnterlagen eclPortalUnterlagen, int index, int richtung) {
        int reihenfolgeNummerAlt = eclPortalUnterlagen.getReihenfolgeUnterlagenbereich();
        int reihenfolgeNummerNeu = reihenfolgeNummerAlt;

        if (richtung == 1) {
            reihenfolgeNummerNeu -= 1;
            index -= 1;
        } else {
            reihenfolgeNummerNeu += 1;
            index += 1;
        }
        eclDbM.openAll();
        eclDbM.openWeitere();

        eclPortalUnterlagen.setReihenfolgeUnterlagenbereich(reihenfolgeNummerNeu);

        eclDbM.getDbBundle().dbPortalUnterlagen.update(eclPortalUnterlagen);

        uUnterlagenPdfsSession.getUnterlagenAufSeiteListe()
                .get(index).reihenfolgeUnterlagenbereich = reihenfolgeNummerAlt;
        eclDbM.getDbBundle().dbPortalUnterlagen.update(uUnterlagenPdfsSession.getUnterlagenAufSeiteListe().get(index));
        unterlagenAnzeigenAuto(eclDbM.getDbBundle());
        eclDbM.closeAll();
        uUnterlagenPdfsSession.getUnterlagenAufSeiteListe()
                .sort(Comparator.comparing(EclPortalUnterlagen::getReihenfolgeUnterlagenbereich));
        init(false);
        xSessionVerwaltung.setzeUEnde();

    }

    public void doUnterlageLoeschen(EclPortalUnterlagen eclPortalUnterlagen) {
        if (!xSessionVerwaltung.pruefeUStart("uUnterlagenPdfs", "")) {
            xSessionVerwaltung.setzeEnde();
            return;
        }
        eclDbM.openAll();
        eclDbM.openWeitere();
        eclDbM.getDbBundle().dbPortalUnterlagen.delete(eclPortalUnterlagen.ident);
        unterlagenAnzeigenAuto(eclDbM.getDbBundle());
        eclDbM.closeAll();
        init(false);
        xSessionVerwaltung.setzeUEnde();
    }

    /**
     * Resettet die Felder zum Anlegen und bearbeiten der Unterlagen
     */
    private void resetModals() {
        uUnterlagenPdfsSession.setShowUnterlageBearbeiten(false);
        uUnterlagenPdfsSession.setShowNeueUnterlage(false);
        uUnterlagenPdfsSession.setEclPortalUnterlagenSelected(null);
        uUnterlagenPdfsSession.setNeueUnterlageLink(null);
        uUnterlagenPdfsSession.setNeueUnterlageBezeichnungDE(null);
        uUnterlagenPdfsSession.setNeueUnterlageBezeichnungEN(null);
    }

    private void readUnterlagenAufSeite(int menuNummer) {
        uUnterlagenPdfsSession.setUnterlagenAufSeiteListe(null);
        List<EclPortalUnterlagen> unterlagenAufSeiteListe = new ArrayList<EclPortalUnterlagen>();
        for (EclPortalUnterlagen eclPortalUnterlagen : uUnterlagenPdfsSession.getPortalUnterlagenListe()) {
            if (eclPortalUnterlagen.getUnterlagenbereichMenueNr() == menuNummer) {
                unterlagenAufSeiteListe.add(eclPortalUnterlagen);
            }
        }
        unterlagenAufSeiteListe.sort(Comparator.comparing(EclPortalUnterlagen::getReihenfolgeUnterlagenbereich));
        uUnterlagenPdfsSession.setUnterlagenAufSeiteListe(unterlagenAufSeiteListe);
    }

    private int maxUnterlagenReiheNummer() {
        int max = 0;
        for (EclPortalUnterlagen eclPortalUnterlagen : uUnterlagenPdfsSession.getUnterlagenAufSeiteListe()) {
            if (eclPortalUnterlagen.reihenfolgeUnterlagenbereich > max) {
                max = eclPortalUnterlagen.reihenfolgeUnterlagenbereich;
            }
        }
        return max;
    }

    private void unterlagenAnzeigenAuto(DbBundle dbBundle) {
        BvReload bvReload = new BvReload(dbBundle);
        bvReload.setReloadParameter(dbBundle.clGlobalVar.mandant);
    }
}
