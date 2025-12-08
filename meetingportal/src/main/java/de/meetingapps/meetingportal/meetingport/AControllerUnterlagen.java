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

import de.meetingapps.meetingportal.meetComAllg.CaLeseDatei;
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
public class AControllerUnterlagen {

    private @Inject EclDbM eclDbM;
    private @Inject EclParamM eclParamM;
    private @Inject AFunktionen aFunktionen;

    public void doUnterlage1DE() {
        doUnterlage("u1de");
    }

    public void doUnterlage1EN() {
        doUnterlage("u1en");
    }

    public void doUnterlage2DE() {
        doUnterlage("u2de");
    }

    public void doUnterlage2EN() {
        doUnterlage("u2en");
    }

    public void doUnterlage3DE() {
        doUnterlage("u3de");
    }

    public void doUnterlage3EN() {
        doUnterlage("u3en");
    }

    public void doUnterlage4DE() {
        doUnterlage("u4de");
    }

    public void doUnterlage4EN() {
        doUnterlage("u4en");
    }

    public void doUnterlage5DE() {
        doUnterlage("u5de");
    }

    public void doUnterlage5EN() {
        doUnterlage("u5en");
    }

    public void doUnterlage6DE() {
        doUnterlage("u6de");
    }

    public void doUnterlage6EN() {
        doUnterlage("u6en");
    }

    public void doUnterlage7DE() {
        doUnterlage("u7de");
    }

    public void doUnterlage7EN() {
        doUnterlage("u7en");
    }

    public void doUnterlage8DE() {
        doUnterlage("u8de");
    }

    public void doUnterlage8EN() {
        doUnterlage("u8en");
    }

    public void doUnterlage9DE() {
        doUnterlage("u9de");
    }

    public void doUnterlage9EN() {
        doUnterlage("u9en");
    }

    public void doUnterlage10DE() {
        doUnterlage("u10de");
    }

    public void doUnterlage10EN() {
        doUnterlage("u10en");
    }

    private void doUnterlage(String pDateiname) {

        eclDbM.openAll();
        eclDbM.closeAll();
        if (eclParamM.isUnterlage1Aktiv() != true) {
            return;
        }

        byte[] bytes = null;
        CaLeseDatei caLeseDatei = new CaLeseDatei();
        bytes = caLeseDatei.alsBytes(eclParamM.getClGlobalVar().lwPfadAllgemein + "\\meetingreports\\"
                + eclParamM.getMandantPfad() + "\\" + pDateiname + ".pdf");

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

}
