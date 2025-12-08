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
package de.meetingapps.meetingportal.meetingCoreReport;

import de.meetingapps.meetingportal.meetComAllg.CaLeseDatei;
import jakarta.faces.context.FacesContext;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

public class RpBrowserAnzeigen {


    public void zeigen(RpDrucken rpDrucken) {
        zeigen(rpDrucken.drucklaufDatei);
    }

    public void zeigen(String pDateiname) {
        zeigen(pDateiname, "");
    }
 
    
    public void zeigen(String pDateiname, String pZielname) {
        byte[] bytes = null;
        CaLeseDatei caLeseDatei = new CaLeseDatei();
        bytes = caLeseDatei.alsBytes(pDateiname);

        FacesContext faces = FacesContext.getCurrentInstance();
        HttpServletResponse response = (HttpServletResponse) faces.getExternalContext().getResponse();
        response.reset();
        response.setContentType("application/pdf");
        String hZiel="Meeting.pdf";
        if (!pZielname.isEmpty()) {
            hZiel=pZielname;
        }
        response.setHeader("Content-Disposition", "inline; filename=\""+hZiel+"\"");

        try {
            ServletOutputStream out = response.getOutputStream();
            out.write(bytes);
            out.flush();
            out.close();
        } catch (Exception e) {
            System.out.println("Fehler: Attachment anzeigen: " + e);
        }

        //		response.setHeader("Content-Disposition", "filename=\""  + pDateiname + "\"");
        faces.responseComplete();

    }
    
    public void zeigen(byte[] bytes, String pZielname) {

        FacesContext faces = FacesContext.getCurrentInstance();
        HttpServletResponse response = (HttpServletResponse) faces.getExternalContext().getResponse();
        response.reset();
        response.setContentType("application/pdf");
        String hZiel="Meeting.pdf";
        if (!pZielname.isEmpty()) {
            hZiel=pZielname;
        }
        response.setHeader("Content-Disposition", "inline; filename=\""+hZiel+"\"");

        try {
            ServletOutputStream out = response.getOutputStream();
            out.write(bytes);
            out.flush();
            out.close();
        } catch (Exception e) {
            System.out.println("Fehler: Attachment anzeigen: " + e);
        }

        //      response.setHeader("Content-Disposition", "filename=\""  + pDateiname + "\"");
        faces.responseComplete();

    }

}
