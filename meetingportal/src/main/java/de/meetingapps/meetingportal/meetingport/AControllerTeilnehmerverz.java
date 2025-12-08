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

import de.meetingapps.meetingportal.meetComAllg.CaLeseDatei;
import de.meetingapps.meetingportal.meetComEclM.EclAbstTeilButtonM;
import de.meetingapps.meetingportal.meetComEclM.EclDbM;
import de.meetingapps.meetingportal.meetComEclM.EclParamM;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

@RequestScoped
@Named
@Deprecated
public class AControllerTeilnehmerverz {

    private @Inject AControllerTeilnehmerverzSession aControllerTeilnehmerverzSession;
    private @Inject EclDbM eclDbM;
    private @Inject EclParamM eclParamM;
    private @Inject ADlgVariablen aDlgVariablen;
    private @Inject AFunktionen aFunktionen;

    /**open wird hier in der Funktion gehandelt*/
    public void init() {
        /**Öffnen wg. Parameternachladen*/
        eclDbM.openAll();
        eclDbM.closeAll();
        List<EclAbstTeilButtonM> absTeilButtonList = new LinkedList<EclAbstTeilButtonM>();
        int lfdNr = 0;
        if (eclParamM.getLfdHVTeilnehmerverzIstMoeglich() == 1) {
            if (eclParamM.getParam().paramPortal.teilnehmerverzLetzteNr > -1) {
                EclAbstTeilButtonM lAbstTeilButtonM = new EclAbstTeilButtonM();
                lAbstTeilButtonM.setLfdNr(lfdNr);
                lfdNr++;
                lAbstTeilButtonM.setButtontext1Vorne("Erstpräsenz");
                lAbstTeilButtonM.setDateiname1("Praesenz0");

                lAbstTeilButtonM.setButtontext2Vorne("Zusammenstellung Erstpräsenz");
                lAbstTeilButtonM.setDateiname2("Praesenz0zusammen");
                absTeilButtonList.add(lAbstTeilButtonM);
            }

            for (int i = 1; i <= eclParamM.getParam().paramPortal.teilnehmerverzLetzteNr; i++) {
                EclAbstTeilButtonM lAbstTeilButtonM = new EclAbstTeilButtonM();
                lAbstTeilButtonM.setLfdNr(lfdNr);
                lfdNr++;
                lAbstTeilButtonM.setButtontext1Vorne("Nachtrag " + Integer.toString(i));
                lAbstTeilButtonM.setDateiname1("Praesenz" + Integer.toString(i));

                lAbstTeilButtonM.setButtontext2Vorne("Zusammenstellung Nachtrag " + Integer.toString(i));
                lAbstTeilButtonM.setDateiname2("Praesenz" + Integer.toString(i) + "zusammen");
                absTeilButtonList.add(lAbstTeilButtonM);
            }
        }

        aControllerTeilnehmerverzSession.setAbsTeilButtonList(absTeilButtonList);

    }

    public void doAnzeige(EclAbstTeilButtonM lAbstTeilButtonM) {

        eclDbM.openAll();
        eclDbM.closeAll();
        if (eclParamM.getLfdHVTeilnehmerverzIstMoeglich() != 1) {
            return;
        }

        byte[] bytes = null;
        CaLeseDatei caLeseDatei = new CaLeseDatei();
        bytes = caLeseDatei.alsBytes(eclParamM.getClGlobalVar().lwPfadAllgemein + "\\meetingausdruckeintern\\"
                + eclParamM.getMandantPfad() + "\\" + lAbstTeilButtonM.getDateiname1() + ".pdf");

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

        response.setHeader("Content-Disposition", "attachment; filename=datei.pdf");
        faces.responseComplete();
        aFunktionen.setzeEnde();
        return;

    }

    public void doAnzeigeZusammen(EclAbstTeilButtonM lAbstTeilButtonM) {
        if (eclParamM.getLfdHVTeilnehmerverzIstMoeglich() != 1) {
            return;
        }

        byte[] bytes = null;
        CaLeseDatei caLeseDatei = new CaLeseDatei();
        bytes = caLeseDatei.alsBytes(eclParamM.getClGlobalVar().lwPfadAllgemein + "\\meetingausdruckeintern\\"
                + eclParamM.getMandantPfad() + "\\" + lAbstTeilButtonM.getDateiname2() + ".pdf");

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

    public String doZurueck() {
        if (!aFunktionen.pruefeStart("aTeilnehmerverz")) {
            return "aDlgFehler";
        }
        eclDbM.openAll();
        String naechsteMaske = aFunktionen.waehleAusgangsmaske(eclDbM.getDbBundle());
        eclDbM.closeAll();
        return aFunktionen.setzeEnde(naechsteMaske, true, true);
    }

    public String doAbmelden() {
        aDlgVariablen.clearLogin();
        return aFunktionen.waehleLogout();
    }

    public String doRefresh() {
        if (!aFunktionen.pruefeStart("aTeilnehmerverz")) {
            return "aDlgFehler";
        }
        init();
        return aFunktionen.setzeEnde("aTeilnehmerverz", true, true);
    }

}
