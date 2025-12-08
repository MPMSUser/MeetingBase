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

import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComAllg.CaLeseDatei;
import de.meetingapps.meetingportal.meetComBa.BaMailM;
import de.meetingapps.meetingportal.meetComEclM.EclDbM;
import de.meetingapps.meetingportal.meetComEclM.EclParamM;
import de.meetingapps.meetingportal.meetComEclM.EclPortalTexteM;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

/*Hinweis: enthält auch Controller-Funktionen für aEintrittskarteDetail.xhtml*/

@RequestScoped
@Named
@Deprecated
public class AControllerEintrittskarteQuittung {

    @Inject
    private AFunktionen aFunktionen;
    @Inject
    private ADlgVariablen aDlgVariablen;
    @Inject
    private EclPortalTexteM eclTextePortalM;
    @Inject
    private EclDbM eclDbM;
    @Inject
    private EclPortalTexteM eclPortalTexteM;

    @Inject
    private BaMailM baMailm;
    @Inject
    EclParamM eclParamM;

    public String doDetailDruckeEintrittskarte() {
        if (!aFunktionen.pruefeStart("aEintrittskarteDetail")) {
            return "aDlgFehler";
        }
        doDruckeEintrittskarte();
        aFunktionen.setzeEnde("aEintrittskarteDetail", false, false);
        return "";
    }

    public String doQuittungDruckeEintrittskarte() {
        if (!aFunktionen.pruefeStart("aEintrittskarteQuittung")) {
            return "aDlgFehler";
        }
        doDruckeEintrittskarte();
        aFunktionen.setzeEnde("aEintrittskarteQuittung", false, false);
        return "";
    }

    private void doDruckeEintrittskarte() {

        int dateinr = aDlgVariablen.getEintrittskartePdfNr();

        byte[] bytes = null;
        CaLeseDatei caLeseDatei = new CaLeseDatei();
        bytes = caLeseDatei.alsBytes(eclParamM.getClGlobalVar().lwPfadAllgemein + "\\meetingausdrucke\\"
                + eclParamM.getMandantPfad() + "\\zutrittsdokumentM" + eclParamM.getClGlobalVar().getMandantString()
                + Integer.toString(dateinr) + ".pdf");

        FacesContext faces = FacesContext.getCurrentInstance();
        HttpServletResponse response = (HttpServletResponse) faces.getExternalContext().getResponse();

        try {
            ServletOutputStream out = response.getOutputStream();
            out.write(bytes);
            out.flush();
            out.close();
        } catch (Exception e) {
            System.out.println("Fehler: Attachment anzeigen: " + e);
        }

        response.setHeader("Content-Disposition", "attachment; filename=zutrittsdokument.pdf");
        faces.responseComplete();
        aFunktionen.setzeEnde();
        return;

    }

    /**Liefert die EKDatei zur dateinr zurück
     * 
     * Verwendung z.B. um das Eintrittskarten-PDF an den Client zu liefern*/
    public byte[] liefereDruckEintrittskarte(int dateinr) {
        byte[] bytes = null;
        CaLeseDatei caLeseDatei = new CaLeseDatei();
        bytes = caLeseDatei.alsBytes(eclParamM.getClGlobalVar().lwPfadAllgemein + "\\meetingausdrucke\\"
                + eclParamM.getMandantPfad() + "\\zutrittsdokumentM" + eclParamM.getClGlobalVar().getMandantString()
                + Integer.toString(dateinr) + ".pdf");

        return bytes;
    }

    public String doDetailDruckeEintrittskarte2() {
        if (!aFunktionen.pruefeStart("aEintrittskarteDetail")) {
            return "aDlgFehler";
        }
        doDruckeEintrittskarte2();
        aFunktionen.setzeEnde("aEintrittskarteDetail", false, false);
        return "";
    }

    public String doQuittungDruckeEintrittskarte2() {
        if (!aFunktionen.pruefeStart("aEintrittskarteQuittung")) {
            return "aDlgFehler";
        }
        doDruckeEintrittskarte2();
        aFunktionen.setzeEnde("aEintrittskarteQuittung", false, false);
        return "";
    }

    private void doDruckeEintrittskarte2() {

        int dateinr = aDlgVariablen.getEintrittskartePdfNr2();

        byte[] bytes = null;
        CaLeseDatei caLeseDatei = new CaLeseDatei();
        bytes = caLeseDatei.alsBytes(eclParamM.getClGlobalVar().lwPfadAllgemein + "\\meetingausdrucke\\"
                + eclParamM.getMandantPfad() + "\\zutrittsdokumentM" + eclParamM.getClGlobalVar().getMandantString()
                + Integer.toString(dateinr) + ".pdf");

        FacesContext faces = FacesContext.getCurrentInstance();
        HttpServletResponse response = (HttpServletResponse) faces.getExternalContext().getResponse();

        try {
            ServletOutputStream out = response.getOutputStream();
            out.write(bytes);
            out.flush();
            out.close();
        } catch (Exception e) {
            System.out.println("Fehler: Attachment anzeigen: " + e);
        }

        response.setHeader("Content-Disposition", "attachment; filename=zutrittsdokument.pdf");
        faces.responseComplete();
        aFunktionen.setzeEnde();
        return;

    }

    public String doDetailDruckeGastkarte() {
        if (!aFunktionen.pruefeStart("aEintrittskarteDetail")) {
            return "aDlgFehler";
        }
        doDruckeGastkarte();
        aFunktionen.setzeEnde("aEintrittskarteDetail", false, false);
        return "";
    }

    public String doQuittungDruckeGastkarte() {
        if (!aFunktionen.pruefeStart("aEintrittskarteQuittung")) {
            return "aDlgFehler";
        }
        doDruckeGastkarte();
        aFunktionen.setzeEnde("aEintrittskarteQuittung", false, false);
        return "";
    }

    public String doGastDruckeGastkarte() {
        if (!aFunktionen.pruefeStart("aGastkarteQuittung")) {
            return "aDlgFehler";
        }
        doDruckeGastkarte();
        aFunktionen.setzeEnde("aGastkarteQuittung", false, false);
        return "";
    }

    public String doGastStatusDruckeGastkarte() { /*Für aGGGastStatus.xhtml*/
        if (!aFunktionen.pruefeStart("aGGGastStatus")) {
            return "aDlgFehler";
        }
        doDruckeGastkarte();
        aFunktionen.setzeEnde("aGGGastStatus", false, false);
        return "";
    }

    private void doDruckeGastkarte() {

        int dateinr = aDlgVariablen.getGastkartePdfNr();

        byte[] bytes = null;
        CaLeseDatei caLeseDatei = new CaLeseDatei();
        bytes = caLeseDatei.alsBytes(eclParamM.getClGlobalVar().lwPfadAllgemein + "\\meetingausdrucke\\"
                + eclParamM.getMandantPfad() + "\\zutrittsdokumentM" + eclParamM.getClGlobalVar().getMandantString()
                + Integer.toString(dateinr) + ".pdf");

        FacesContext faces = FacesContext.getCurrentInstance();
        HttpServletResponse response = (HttpServletResponse) faces.getExternalContext().getResponse();

        try {
            ServletOutputStream out = response.getOutputStream();
            out.write(bytes);
            out.flush();
            out.close();
        } catch (Exception e) {
            System.out.println("Fehler: Attachment anzeigen: " + e);
        }

        response.setHeader("Content-Disposition", "attachment; filename=zutrittsdokument.pdf");
        faces.responseComplete();
        aFunktionen.setzeEnde();
        return;

    }

    /**Email nochmal verschicken (für erste Eintrittskarte)
     * 
     * Eingabeparameter:
     * 		DlgVariablen.eintrittskartePdfNr
     * 		DldVariablen.eintrittskarteEmail
     * 
     * */

    public String doDetailMailEintrittskarte() {
        if (!aFunktionen.pruefeStart("aEintrittskarteDetail")) {
            return "aDlgFehler";
        }
        doMailEintrittskarte();
        aFunktionen.setzeEnde("aEintrittskarteDetail", false, false);
        return "";
    }

    public String doQuittungMailEintrittskarte() {
        if (!aFunktionen.pruefeStart("aEintrittskarteQuittung")) {
            return "aDlgFehler";
        }
        doMailEintrittskarte();
        aFunktionen.setzeEnde("aEintrittskarteQuittung", false, false);
        return "";
    }

    private void doMailEintrittskarte() {
        /*Nun per Mail versenden*/
        int dateinr = aDlgVariablen.getEintrittskartePdfNr();

        String hMailText = "", hBetreff = "";
        if (eclPortalTexteM.portalNichtUeberNeueTexte()) {
        } else {
            hBetreff = eclPortalTexteM.holeText("218");
            hMailText = eclPortalTexteM.holeText("219");

        }

        baMailm.sendenMitAnhang(aDlgVariablen.getEintrittskarteEmail(), hBetreff, hMailText,
                eclParamM.getClGlobalVar().lwPfadAllgemein + "\\meetingausdrucke\\" + eclParamM.getMandantPfad()
                        + "\\zutrittsdokumentM" + eclParamM.getClGlobalVar().getMandantString()
                        + Integer.toString(dateinr) + ".pdf");

        //		baMailm.sendenMitAnhang(, "Ihr Zutrittsdokument", "Anbei erhalten Sie Ihr Zutrittsdokument", "D:\\meetingausdrucke\\zutrittsdokumentM"+ClGlobalVar.getMandantString()+Integer.toString(dateinr)+".pdf");
        aDlgVariablen.setFehlerMeldung(eclTextePortalM.getFehlertext(CaFehler.afEKAktionaerEmailVerschickt));
        aDlgVariablen.setFehlerNr(1); /*Positiv => kein Fehler*/

    }

    /**Email nochmal verschicken (für zweite Eintrittskarte)
     * 
     * Eingabeparameter:
     * 		DlgVariablen.eintrittskartePdfNr2
     * 		DldVariablen.eintrittskarteEmail2
     * 
     * */

    public String doDetailMailEintrittskarte2() {
        if (!aFunktionen.pruefeStart("aEintrittskarteDetail")) {
            return "aDlgFehler";
        }
        doMailEintrittskarte2();
        aFunktionen.setzeEnde("aEintrittskarteDetail", false, false);
        return "";
    }

    public String doQuittungMailEintrittskarte2() {
        if (!aFunktionen.pruefeStart("aEintrittskarteQuittung")) {
            return "aDlgFehler";
        }
        doMailEintrittskarte2();
        aFunktionen.setzeEnde("aEintrittskarteQuittung", false, false);
        return "";
    }

    private void doMailEintrittskarte2() {
        /*Nun per Mail versenden*/
        int dateinr = aDlgVariablen.getEintrittskartePdfNr2();

        String email = "";
        if (aDlgVariablen.getAusgewaehlteAktion().compareTo("30") == 0) {
            email = aDlgVariablen.getEintrittskarteEmail();
        } else {
            email = aDlgVariablen.getEintrittskarteEmail2();

        }

        baMailm.sendenMitAnhang(email, "Ihr Zutrittsdokument", "Anbei erhalten Sie Ihr Zutrittsdokument",
                eclParamM.getClGlobalVar().lwPfadAllgemein + "\\meetingausdrucke\\" + eclParamM.getMandantPfad()
                        + "\\zutrittsdokumentM" + eclParamM.getClGlobalVar().getMandantString()
                        + Integer.toString(dateinr) + ".pdf");
        aDlgVariablen.setFehlerMeldung(eclTextePortalM.getFehlertext(CaFehler.afEK2AktionaerEmailVerschickt));
        aDlgVariablen.setFehlerNr(1); /*Positiv => kein Fehler*/

    }

    /**Email nochmal verschicken (für Gastkarte)
     * 
     * Eingabeparameter:
     * 		DlgVariablen.gastkartePdfNr
     * 		DldVariablen.gastkarteEmail
     * 
     * */
    public String doDetailMailGastkarte() {
        if (!aFunktionen.pruefeStart("aEintrittskarteDetail")) {
            return "aDlgFehler";
        }
        doMailGastkarte();
        aFunktionen.setzeEnde("aEintrittskarteDetail", false, false);
        return "";
    }

    public String doQuittungMailGastkarte() {
        if (!aFunktionen.pruefeStart("aEintrittskarteQuittung")) {
            return "aDlgFehler";
        }
        doMailGastkarte();
        aFunktionen.setzeEnde("aEintrittskarteQuittung", false, false);
        return "";
    }

    public String doGastMailGastkarte() {
        if (!aFunktionen.pruefeStart("aGastkarteQuittung")) {
            return "aDlgFehler";
        }
        doMailGastkarte();
        aFunktionen.setzeEnde("aGastkarteQuittung", false, false);
        return "";
    }

    public String doGastStatusMailGastkarte() {/*für aGGGastStatus.xhtml*/
        if (!aFunktionen.pruefeStart("aGGGastStatus")) {
            return "aDlgFehler";
        }
        doMailGastkarte();
        aFunktionen.setzeEnde("aGGGastStatus", false, false);
        return "";
    }

    private void doMailGastkarte() {
        /*Nun per Mail versenden*/
        int dateinr = aDlgVariablen.getGastkartePdfNr();

        baMailm.sendenMitAnhang(aDlgVariablen.getGastkarteEmail(), "Ihr Zutrittsdokument",
                "Anbei erhalten Sie Ihr Zutrittsdokument",
                eclParamM.getClGlobalVar().lwPfadAllgemein + "\\meetingausdrucke\\" + eclParamM.getMandantPfad()
                        + "\\zutrittsdokumentM" + eclParamM.getClGlobalVar().getMandantString()
                        + Integer.toString(dateinr) + ".pdf");
        aDlgVariablen.setFehlerMeldung(eclTextePortalM.getFehlertext(CaFehler.afEKGastEmailVerschickt));

    }

    /**Weiter*/
    public String doDetailWeiter() {
        String naechsteMaske = "";
        if (!aFunktionen.pruefeStart("aEintrittskarteDetail")) {
            return "aDlgFehler";
        }
        naechsteMaske = doWeiter();
        return aFunktionen.setzeEnde(naechsteMaske, true, true);
    }

    public String doQuittungWeiter() {

        String naechsteMaske = "";
        if (!aFunktionen.pruefeStart("aEintrittskarteQuittung")) {
            return "aDlgFehler";
        }
        naechsteMaske = doWeiter();
        return aFunktionen.setzeEnde(naechsteMaske, true, true);
    }

    public String doGastWeiter() {
        String naechsteMaske = "";
        if (!aFunktionen.pruefeStart("aGastkarteQuittung")) {
            return "aDlgFehler";
        }
        naechsteMaske = doWeiter();
        return aFunktionen.setzeEnde(naechsteMaske, true, true);
    }

    public String doWeiter() {

        eclDbM.openAll();

        String naechsteMaske = aFunktionen.waehleAusgangsmaske(eclDbM.getDbBundle());
        eclDbM.closeAll();
        aFunktionen.setzeEnde();
        return naechsteMaske;
    }

    /**Abmelden*/
    public String doDetailAbmelden() {
        aFunktionen.setzeEnde();
        return doAbmelden();
    }

    public String doQuittungAbmelden() {
        aFunktionen.setzeEnde();
        return doAbmelden();
    }

    public String doGastAbmelden() {
        aFunktionen.setzeEnde();
        return doAbmelden();
    }

    private String doAbmelden() {
        aDlgVariablen.clearLogin();
        return aFunktionen.waehleLogout();
    }

}
