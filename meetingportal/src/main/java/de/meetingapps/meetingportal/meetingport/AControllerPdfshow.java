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

import org.primefaces.model.StreamedContent;

import de.meetingapps.meetingportal.meetComAllg.CaString;
import de.meetingapps.meetingportal.meetComBl.BlPdfInBrowser;
import de.meetingapps.meetingportal.meetComBlManaged.BlMFuelleEclMAusPufferOderDBEE;
import de.meetingapps.meetingportal.meetComEclM.EclDbM;
import de.meetingapps.meetingportal.meetingCoreReport.RpDownloadDatei;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@RequestScoped
@Named
@Deprecated
public class AControllerPdfshow {

    private @Inject AControllerPdfshowSession aControllerPdfshowSession;
    private @Inject BlMFuelleEclMAusPufferOderDBEE blMFuelleEclMAusPufferOderDBEE;
    private @Inject EclDbM eclDbM;

    private StreamedContent test;

    public void doAnzeigen() {

        long ident = -1;
        String hIdent = aControllerPdfshowSession.getIdent();
        if (CaString.isNummern(hIdent)) {
            ident = Long.parseLong(hIdent);
        }

        String einmalkey = aControllerPdfshowSession.getKey();
        System.out.println("hIdent=" + hIdent);
        System.out.println("einmalkey=" + einmalkey);

        eclDbM.openAllOhneParameterCheck();
        blMFuelleEclMAusPufferOderDBEE.fuelleAlleVariablenBeiStart();
        eclDbM.closeAll();

        eclDbM.openAll();
        eclDbM.openWeitere();

        BlPdfInBrowser blPdfInBrowser = new BlPdfInBrowser(eclDbM.getDbBundle());
        String pfad = blPdfInBrowser.liefereVollstaendigerNamePdf(ident, einmalkey);
        System.out.println("pfad=" + pfad);

        RpDownloadDatei rpDownloadDatei = new RpDownloadDatei();
        try {
            rpDownloadDatei.download(pfad);
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        //		File initialFile = new File(pfad);
        //	    InputStream targetStream;
        //		try {
        //			targetStream = new FileInputStream(initialFile);
        //
        //			DefaultStreamedContent dsc=new DefaultStreamedContent(targetStream,"application/pdf");
        //			aControllerPdfshowSession.setPdfZumAnzeigen(dsc);
        //
        //		  /*  targetStream.close(); */
        //		} catch (FileNotFoundException e) {
        //			// TODO Auto-generated catch block
        //			e.printStackTrace();
        //		} catch (IOException e) {
        //			// TODO Auto-generated catch block
        //			e.printStackTrace();
        //		}

        //		eclDbM.closeAll();
        //		if (!pfad.isEmpty()) {
        //			byte[] bytes=null;
        //			CaLeseDatei caLeseDatei=new CaLeseDatei();
        //			bytes=caLeseDatei.alsBytes(pfad);
        //
        //			FacesContext faces = FacesContext.getCurrentInstance();
        //			HttpServletResponse response =(HttpServletResponse)faces.getExternalContext().getResponse();
        //
        //
        //			try {
        //			      ServletOutputStream out = response.getOutputStream();
        //			      out.write(bytes);
        //			      out.flush();
        //			      out.close();
        //			} catch(Exception e) {
        //			      System.out.println("Fehler: Attachment anzeigen: " + e);
        //			}
        //
        //
        //			response.setHeader("Content-Disposition", "attachment; filename=datei.pdf");
        //			faces.responseComplete();
        eclDbM.closeAll();
        aControllerPdfshowSession.setAnzeigen(true);
        return;
    }

}