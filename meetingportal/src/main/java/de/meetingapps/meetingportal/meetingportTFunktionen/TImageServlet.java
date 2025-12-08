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

import java.io.IOException;

import de.meetingapps.meetingportal.meetComEclM.EclParamM;
import de.meetingapps.meetingportal.meetComHVParam.ParamLogo;
import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/images/dummy.png")
public class TImageServlet extends HttpServlet {

    /** Our Logger. */
//    private static final Logger LOGGER = LogManager.getFormatterLogger(TImageServlet.class.getName());

    @Inject
    EclParamM eclParamM;

    private static final long serialVersionUID = -3580401413165127958L;

    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException, IOException {

        final ParamLogo lParamLogo = this.eclParamM.getParamLogo();

        if (lParamLogo!=null) {
            String mimeType = this.getServletContext().getMimeType(lParamLogo.logoPortalDateiname);
            String logoPortalLength = lParamLogo.logoPortalLength;
            String logoPortalDisposition = lParamLogo.logoPortalDisposition;
            response.setHeader("Content-Type", mimeType);
            response.setHeader("Content-Length", logoPortalLength);
            response.setHeader("Content-Disposition", logoPortalDisposition);
        }

        if (lParamLogo!=null && lParamLogo.logoPortal != null) {
            response.getOutputStream().write(lParamLogo.logoPortal);
        } else {
//            if (LOGGER.isWarnEnabled()) {
//                LOGGER.warn("doGet(): Mandantenlogo konnte nicht geladen werden");
//            }
//            if (LOGGER.isDebugEnabled()) {
//                LOGGER.debug("doGet(): " + DebugHelper.getMemoryAllocation());
//            }
        }
    }
}
