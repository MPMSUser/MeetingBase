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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import jakarta.faces.context.FacesContext;
import jakarta.servlet.http.HttpServletResponse;

public class RpDownloadDatei {

    private int logDrucken = 3;

    public void download(String pDateiname) throws IOException {

        CaBug.druckeLog("pDateiname=" + pDateiname, logDrucken, 3);
        File file = new File(pDateiname);

        String dateinameZiel = "";
        int ab = pDateiname.lastIndexOf("\\");
        if (ab < 0) {
            dateinameZiel = pDateiname;
        } else {
            dateinameZiel = pDateiname.substring(ab);
        }

        FacesContext facesContext = FacesContext.getCurrentInstance();

        HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();

        response.reset();
        response.setHeader("Content-Type", "application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;filename=" + dateinameZiel);

        OutputStream responseOutputStream = response.getOutputStream();

        InputStream fileInputStream = new FileInputStream(file);

        byte[] bytesBuffer = new byte[2048];
        int bytesRead;
        while ((bytesRead = fileInputStream.read(bytesBuffer)) > 0) {
            responseOutputStream.write(bytesBuffer, 0, bytesRead);
        }

        responseOutputStream.flush();

        fileInputStream.close();
        responseOutputStream.close();

        facesContext.responseComplete();
    }
}
