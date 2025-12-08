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
package de.meetingapps.meetingportal.meetComBrM;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComBl.BlNummernformBasis;
import de.meetingapps.meetingportal.meetComEclM.EclDbM;
import de.meetingapps.meetingportal.meetComEntitiesGenossenschaftSys.EgxPostKontakt;
import de.meetingapps.meetingportal.meetComEntitiesGenossenschaftSys.EgxPostKontaktRC;
import de.meetingapps.meetingportal.meetingportTController.TPermanentSession;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@RequestScoped
@Named
public class BrMKontaktformular {
	private int logDrucken = 10;

	private @Inject EclDbM eclDbM;
    private @Inject TPermanentSession tPermanentSession;
    
    private @Inject BrMGenossenschaftCall brMGenossenschaftCall;

    public String senden(String pAktionaersnummer, int kontaktThemaIdent, String kontaktThemaWeitergabeText, String kontaktThemaText, String mailText) {
    	String lSchluessel="";
    	String aktionaersnummer=pAktionaersnummer.trim();
    	BlNummernformBasis blNummernformBasis = new BlNummernformBasis(eclDbM.getDbBundle());
    	String aktionaersnummerFuerGenossenschaftSysWebrequest = blNummernformBasis.aufbereitenKennungFuerGenossenschaftSysWebrequest(aktionaersnummer);
        
        if (tPermanentSession.isTestModus()) {
        }
        else {
            EgxPostKontakt egxPostKontakt = new EgxPostKontakt();
            egxPostKontakt.nachricht = mailText;
            egxPostKontakt.thema = kontaktThemaText;
            
            EgxPostKontaktRC egxPostKontaktRC = brMGenossenschaftCall.doPostRequestKontakt(aktionaersnummerFuerGenossenschaftSysWebrequest, egxPostKontakt);
            if (egxPostKontaktRC==null) {
                return null;
            }
            lSchluessel = egxPostKontaktRC.lfnr + ";10465";
            CaBug.druckeLog(egxPostKontaktRC.toString(), logDrucken, 10);
        }
        
        
 
        return lSchluessel;
    }
}
