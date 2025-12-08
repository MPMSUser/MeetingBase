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

import java.util.concurrent.Future;

import de.meetingapps.meetingportal.meetComAllg.CaDateiWrite;
import de.meetingapps.meetingportal.meetComAllg.CaPasswortErzeugen;
import de.meetingapps.meetingportal.meetComBlMBackground.BlMbGaesteImport;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEclM.EclDbM;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@RequestScoped
@Named
public class UImportExport {

    private @Inject UReportsSession uReportsSession;
    private @Inject USession uSession;

    private @Inject UReports uReports;

    private @Inject BlMbGaesteImport blMbGaesteImport;

    private @Inject EclDbM eclDbM;

     
    
 
   
      

    /*************************Export Passwörter*********************************/
    public void exportPasswoerter() {
        
        eclDbM.openAll();
        DbBundle lDbBundle=eclDbM.getDbBundle();
        
        CaDateiWrite protokollDatei = null;
        protokollDatei = new CaDateiWrite();
        protokollDatei.oeffne(lDbBundle, "passwoerter");
        
        for (int i=0;i<1000;i++) {
            CaPasswortErzeugen caPasswortErzeugen = new CaPasswortErzeugen();
            String ergebnisPasswortInitial = caPasswortErzeugen.generatePW(lDbBundle.param.paramPortal.passwortMindestLaenge, 1, 1,
                    true); ;
            protokollDatei.ausgabePlain(ergebnisPasswortInitial);
            protokollDatei.newline();
            
        }
        protokollDatei.schliessen();
        
        eclDbM.closeAll();
    }
    
    
    
    
    /***************************ImportGaeste*********************************/
    public void importGaesteStart() {
        uReportsSession.clear();
        uReportsSession.setReportNummer(148);
        uReportsSession.setUeberschrift("Import Gäste");
        uReportsSession.setReportAnzeigeSeiteAufrufen(true);
    }
    
    public boolean pruefeImportGaeste() {
        if (uReportsSession.getImportDateiname().trim().isEmpty()) {
            uSession.setFehlermeldung("Bitte Import-Dateiname (einschließlich Pfad) angeben!");
            return false;
        }
        return true;
    }
    
    public void importGaeste() {
        eclDbM.openAll();
        DbBundle lDbBundle=eclDbM.getDbBundle();
        eclDbM.closeAll();
        
        Future<String> future=blMbGaesteImport.importGaeste(lDbBundle, uReportsSession.getImportDateiname().trim());
        
        uReportsSession.setFutureDateiname(future);
        

        uReportsSession.setBackgroundReport(true);
        uReportsSession.setStehtZurVerfuegung(false);
        
        uReports.pruefenObFertig();

    }

    
}
