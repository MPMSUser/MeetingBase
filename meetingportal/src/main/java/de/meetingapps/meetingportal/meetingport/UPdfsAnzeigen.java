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
import java.io.FilenameFilter;

import de.meetingapps.meetingportal.meetComAllg.CaDateiVerwaltung;
import de.meetingapps.meetingportal.meetComEclM.EclParamM;
import de.meetingapps.meetingportal.meetComEclM.EclUserLoginM;
import de.meetingapps.meetingportal.meetComEntities.EclDateiDownload;
import de.meetingapps.meetingportal.meetingCoreReport.RpBrowserAnzeigen;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@RequestScoped
@Named
public class UPdfsAnzeigen {

 
    private @Inject EclParamM eclParamM;
    private @Inject UPdfsAnzeigenSession uPdfsAnzeigenSession;
    @Inject
    private XSessionVerwaltung xSessionVerwaltung;
    @Inject
    private EclUserLoginM eclUserLoginM;

    /**Open wird hier durchgeführt.
     * 
     * Initialisiert die PDF-Dateinamen-Listen*/
    public void init() {
        
//        "Abstimmungserg" + Integer.toString(i)
//        eclParamM.getClGlobalVar().lwPfadAllgemein + "\\meetingausdruckeintern\\"
//        + eclParamM.getMandantPfad() + "\\" + dateiName + ".pdf"
//        
//        lAbstTeilButtonM.setDateiname1("Praesenz0");
//
//        lAbstTeilButtonM.setDateiname2("Praesenz0zusammen");
//
//        lAbstTeilButtonM.setDateiname1("Praesenz" + Integer.toString(i));
//
//        lAbstTeilButtonM.setDateiname2("Praesenz" + Integer.toString(i) + "zusammen");

        
        CaDateiVerwaltung caDateiVerwaltung=new CaDateiVerwaltung();
        String suchVerzeichnis="";
        FilenameFilter filter=null;
        
        suchVerzeichnis=eclParamM.getClGlobalVar().lwPfadAllgemein + "\\"+eclParamM.getParamServer().praefixPfadVerzeichnisse
                + "pdf\\"
                + eclParamM.getMandantPfad();
        filter = new FilenameFilter() {
            @Override
            public boolean accept(File f, String name) {
                return name.endsWith(".pdf");
            }

        };
         String[] dateienPDF=caDateiVerwaltung.leseDateienInVerzeichnis(suchVerzeichnis, filter);
        if (dateienPDF==null) {dateienPDF=new String[0];}
        uPdfsAnzeigenSession.setDateienPDF(dateienPDF);

        suchVerzeichnis=eclParamM.getClGlobalVar().lwPfadAllgemein + "\\" + eclParamM.getParamServer().praefixPfadVerzeichnisse
                + "ausdruckeintern\\"
                + eclParamM.getMandantPfad() ;
        filter = new FilenameFilter() {
            @Override
            public boolean accept(File f, String name) {
                if (!name.endsWith(".pdf")){return false;}
                if (!name.startsWith("Praesenz")){return false;}
                return true;
            }

        };
        String[] dateienTeilnahme=caDateiVerwaltung.leseDateienInVerzeichnis(suchVerzeichnis, filter);
        if (dateienTeilnahme==null) {dateienTeilnahme=new String[0];}
        uPdfsAnzeigenSession.setDateienTeilnahme(dateienTeilnahme);
       
        suchVerzeichnis=eclParamM.getClGlobalVar().lwPfadAllgemein + "\\" + eclParamM.getParamServer().praefixPfadVerzeichnisse
                + "ausdruckeintern\\"
                + eclParamM.getMandantPfad();
        filter = new FilenameFilter() {
            @Override
            public boolean accept(File f, String name) {
                if (!name.endsWith(".pdf")){return false;}
                if (!name.startsWith("Abstimmungserg")){return false;}
                return true;
            }

        };
        String[] dateienAbstimmung=caDateiVerwaltung.leseDateienInVerzeichnis(suchVerzeichnis, filter);
        if (dateienAbstimmung==null) {dateienAbstimmung=new String[0];}
        uPdfsAnzeigenSession.setDateienAbstimmung(dateienAbstimmung);
        
                
    }
    
    public String doRefresh() {
        if (eclUserLoginM.pruefe_uportal_virtuelleHV() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uPdfsAnzeigen", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }

        init();
        
       return xSessionVerwaltung.setzeUEnde("uPdfsAnzeigen", true, false, eclUserLoginM.getKennung());
        
    }
    
    public String doAnzeigenTeilnehmerverzeichnis(String pdfElement) {
        return anzeige(pdfElement, eclParamM.getParamServer().praefixPfadVerzeichnisse+"ausdruckeintern", "uPdfsAnzeigen", "");
    }
    
    public String doAnzeigenAbstimmung(String pdfElement) {
        return anzeige(pdfElement, eclParamM.getParamServer().praefixPfadVerzeichnisse+"ausdruckeintern", "uPdfsAnzeigen", "");
    }

    public String doAnzeigenPDF(String pdfElement) {
        return anzeige(pdfElement, eclParamM.getParamServer().praefixPfadVerzeichnisse+"pdf", "uPdfsAnzeigen", "");
    }

    public String doAnzeigenGastkarte(EclDateiDownload pdfElement) {
        return anzeige(pdfElement.quellDateinameAufServer, eclParamM.getParamServer().praefixPfadVerzeichnisse+"ausdrucke", "uGastkarteInsti", pdfElement.zielDateinameAnzeige);
    }

    private String anzeige(String pdfElement, String pfad, String pAusgangsmaske, String pZielDatei) {
        if (!xSessionVerwaltung.pruefeStart(pAusgangsmaske, "")) {
            xSessionVerwaltung.setzeEnde();
            return "uDlgFehler";
        }

        String hDatei = eclParamM.getClGlobalVar().lwPfadAllgemein + "\\"+pfad+"\\" + eclParamM.getMandantPfad() + "\\"+pdfElement;
        RpBrowserAnzeigen rpBrowserAnzeigen = new RpBrowserAnzeigen();
        rpBrowserAnzeigen.zeigen(hDatei, pZielDatei);

        xSessionVerwaltung.setzeEnde();
        return "";
        
    }
    
}
