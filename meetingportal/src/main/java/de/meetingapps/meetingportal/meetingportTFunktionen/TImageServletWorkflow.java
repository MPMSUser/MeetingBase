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
package de.meetingapps.meetingportal.meetingportTFunktionen;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetingport.UWorkflowSession;
import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/images/workflowdummy.png")
public class TImageServletWorkflow extends HttpServlet {

    private int logDrucken=10;
    /** Our Logger. */
//    private static final Logger LOGGER = LogManager.getFormatterLogger(TImageServlet.class.getName());

    @Inject UWorkflowSession uWorkflowSession;
    
    private static final long serialVersionUID = -3580401413165127958L;

    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException, IOException {

        String pfad=uWorkflowSession.getAnzuzeigendeDateiPfad();
        String filename=uWorkflowSession.getAnzuzeigendeDatei();

        CaBug.druckeLog("pfad="+pfad, logDrucken, 10);
        CaBug.druckeLog("filename="+filename, logDrucken, 10);
        
        File file = new File(pfad, filename);

        byte[] logoPortalAusFile = null;
        try {
            logoPortalAusFile = Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            CaBug.druckeInfo("001  " );
        }



        String mimeType = this.getServletContext().getMimeType(filename);
        CaBug.druckeLog("mimeType="+mimeType, logDrucken, 10);
        
        String logoPortalLength = String.valueOf(file.length());
        CaBug.druckeLog("logoPortalLength="+logoPortalLength, logDrucken, 10);
        
        String logoPortalDisposition = "inline; filename=\"" + filename + "\"";
        CaBug.druckeLog("logoPortalDisposition="+logoPortalDisposition, logDrucken, 10);
        
        response.setHeader("Content-Type", mimeType);
        response.setHeader("Content-Length", logoPortalLength);
        response.setHeader("Content-Disposition", logoPortalDisposition);

        response.getOutputStream().write(logoPortalAusFile);
    }
}
