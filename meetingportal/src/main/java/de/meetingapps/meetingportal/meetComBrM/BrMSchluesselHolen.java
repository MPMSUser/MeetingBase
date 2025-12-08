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

import java.util.LinkedList;
import java.util.List;

import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComEclM.EclDbM;
import de.meetingapps.meetingportal.meetComEntities.EclBlzBank;
import de.meetingapps.meetingportal.meetComEntities.EclPlzOrt;
import de.meetingapps.meetingportal.meetComEntities.EclStaaten;
import de.meetingapps.meetingportal.meetComEntitiesGenossenschaftSys.EgxGetBankdatenRC;
import de.meetingapps.meetingportal.meetComEntitiesGenossenschaftSys.EgxGetBankdatenResult;
import de.meetingapps.meetingportal.meetComEntitiesGenossenschaftSys.EgxGetLaenderRC;
import de.meetingapps.meetingportal.meetComEntitiesGenossenschaftSys.EgxGetLaenderResult;
import de.meetingapps.meetingportal.meetComEntitiesGenossenschaftSys.EgxGetPlzOrtRC;
import de.meetingapps.meetingportal.meetComEntitiesGenossenschaftSys.EgxGetPlzOrtResult;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@RequestScoped
@Named
public class BrMSchluesselHolen {

	private @Inject EclDbM eclDbM;

//    private @Inject TPermanentSession tPermanentSession;
	private @Inject BrMGenossenschaftCall brMGenossenschaftCall;

	/**
	 * Wird aus uLogin heraus aufgerufen - initialisiert die Länder-Tabelle (Lokal)
	 */
	public int laenderHolen() {

//        if (tPermanentSession.isTestModus() && 1==2) {
//            eclDbM.getDbBundle().dbStaaten.mandantenabhaengig=true;
//            
//            EclStaaten lStaaten=null;
//            lStaaten=new EclStaaten();
//            lStaaten.id=101;
//            lStaaten.nameDE="Deutschland";
//            eclDbM.getDbBundle().dbStaaten.insert(lStaaten);
//
//            lStaaten=new EclStaaten();
//            lStaaten.id=102;
//            lStaaten.nameDE="Bayern";
//            eclDbM.getDbBundle().dbStaaten.insert(lStaaten);
//
//            eclDbM.getDbBundle().dbStaaten.mandantenabhaengig=false;
//
//        }
//        else {
	    EgxGetLaenderRC egxGetLaenderRC = brMGenossenschaftCall.doGetRequestLaender("");
	    if (egxGetLaenderRC==null) {
	        return CaFehler.perRemoteAktienregisterNichtVerfuegbar;
	    }

	    if (egxGetLaenderRC.result != null) {
	        List<EclStaaten> neueStaatenListe = new LinkedList<EclStaaten>();

	        for (EgxGetLaenderResult iEgxGetLaenderResult : egxGetLaenderRC.result) {

	            EclStaaten lStaaten = new EclStaaten();
	            lStaaten.id = iEgxGetLaenderResult.nr;
	            lStaaten.nameDE = iEgxGetLaenderResult.name;
	            neueStaatenListe.add(lStaaten);

	            eclDbM.getDbBundle().dbStaaten.mandantenabhaengig = true;
	            int rc = eclDbM.getDbBundle().dbStaaten.readId(lStaaten.id);
	            if (rc == 0) {
	                eclDbM.getDbBundle().dbStaaten.insert(lStaaten);
	            } else {
	                eclDbM.getDbBundle().dbStaaten.update(lStaaten);
	            }
	            eclDbM.getDbBundle().dbStaaten.mandantenabhaengig = false;

	            System.out.println(iEgxGetLaenderResult.name);
	        }

	        /* Nun noch "alte" rauslöschen */
	        eclDbM.getDbBundle().dbStaaten.mandantenabhaengig = true;
	        eclDbM.getDbBundle().dbStaaten.readAll(0);
	        List<EclStaaten> alteStaatenListe = eclDbM.getDbBundle().dbStaaten.ergebnis();

	        for (int i = 0; i < alteStaatenListe.size(); i++) {
	            int id = alteStaatenListe.get(i).id;
	            int gef = 0;
	            for (int i1 = 0; i1 < neueStaatenListe.size(); i1++) {
	                if (neueStaatenListe.get(i1).id == id) {
	                    gef = 1;
	                }
	            }
	            if (gef == 0) {
	                eclDbM.getDbBundle().dbStaaten.delete(id);
	            }
	        }

	        eclDbM.getDbBundle().dbStaaten.mandantenabhaengig = false;

	    }

		return 1;
	}

	/**
	 * Wird aus uLogin heraus aufgerufen - initialisiert die Länder-Tabelle (Lokal)
	 */
	public int plzOrtHolen() {

	    EgxGetPlzOrtRC egxGetPlzOrtRC = brMGenossenschaftCall.doGetRequestPlzOrt("");
	    if (egxGetPlzOrtRC==null) {
	        return CaFehler.perRemoteAktienregisterNichtVerfuegbar;
	    }

	    eclDbM.getDbBundle().dbPlzOrt.mandantenabhaengig = true;
	    eclDbM.getDbBundle().dbPlzOrt.updateAllUnverarbeitet();
	    eclDbM.getDbBundle().dbPlzOrt.mandantenabhaengig = false;

	    if (egxGetPlzOrtRC.result != null) {

	        for (EgxGetPlzOrtResult iEgxGetPlzOrtResult : egxGetPlzOrtRC.result) {

	            EclPlzOrt lPlzOrt = new EclPlzOrt(); // lPlzOrt.id=iEgxGetPlzOrtResult.nr;
	            lPlzOrt.plz = iEgxGetPlzOrtResult.plz;
	            lPlzOrt.ort = iEgxGetPlzOrtResult.ort;
	            lPlzOrt.version = 1;

	            eclDbM.getDbBundle().dbPlzOrt.mandantenabhaengig = true;
	            int rc = eclDbM.getDbBundle().dbPlzOrt.readPlzOrt(lPlzOrt.plz, lPlzOrt.ort);
	            if (rc == 0) {
	                eclDbM.getDbBundle().dbPlzOrt.insert(lPlzOrt);
	            } else {
	                eclDbM.getDbBundle().dbPlzOrt.update(lPlzOrt);
	            }
	            eclDbM.getDbBundle().dbPlzOrt.mandantenabhaengig = false;

	            System.out.println(iEgxGetPlzOrtResult.ort);

	        }

	        if (egxGetPlzOrtRC._links.next != null && !egxGetPlzOrtRC._links.next.equals("")) {
	            String link_next = egxGetPlzOrtRC._links.next.split("plz_ort")[1];
	            while (link_next != null && !link_next.equals("")) {
	                egxGetPlzOrtRC = brMGenossenschaftCall.doGetRequestPlzOrt("", link_next);
	                if (egxGetPlzOrtRC==null) {
	                    return CaFehler.perRemoteAktienregisterNichtVerfuegbar;
	                }

	                if (egxGetPlzOrtRC.result != null) {

	                    for (EgxGetPlzOrtResult iEgxGetPlzOrtResult : egxGetPlzOrtRC.result) {

	                        EclPlzOrt lPlzOrt = new EclPlzOrt(); // lPlzOrt.id=iEgxGetPlzOrtResult.nr;
	                        lPlzOrt.plz = iEgxGetPlzOrtResult.plz;
	                        lPlzOrt.ort = iEgxGetPlzOrtResult.ort;
	                        lPlzOrt.version = 1;

	                        eclDbM.getDbBundle().dbPlzOrt.mandantenabhaengig = true;
	                        int rc = eclDbM.getDbBundle().dbPlzOrt.readPlzOrt(lPlzOrt.plz, lPlzOrt.ort);
	                        if (rc == 0) {
	                            eclDbM.getDbBundle().dbPlzOrt.insert(lPlzOrt);
	                        } else {
	                            eclDbM.getDbBundle().dbPlzOrt.update(lPlzOrt);
	                        }
	                        eclDbM.getDbBundle().dbPlzOrt.mandantenabhaengig = false;

	                        System.out.println(iEgxGetPlzOrtResult.ort);

	                    }
	                }

	                if (egxGetPlzOrtRC._links.next != null && !egxGetPlzOrtRC._links.next.equals("")) {
	                    link_next = egxGetPlzOrtRC._links.next.split("plz_ort")[1];
	                } else {
	                    link_next = "";
	                }
	            }
	        }

	        /* Nun noch "alte" rauslöschen */
	        eclDbM.getDbBundle().dbPlzOrt.mandantenabhaengig = true;
	        eclDbM.getDbBundle().dbPlzOrt.deleteAllUnverarbeitet();
	        eclDbM.getDbBundle().dbPlzOrt.mandantenabhaengig = false;

	    }

	    return 1;
	}

	   /**
     * Wird aus uLogin heraus aufgerufen - initialisiert die Banken-Tabelle (Lokal)
     */
    public int blzBankHolen() {

        EgxGetBankdatenRC egxBankdatenRC = brMGenossenschaftCall.doGetRequestBankdaten("");
        if (egxBankdatenRC==null) {
            return CaFehler.perRemoteAktienregisterNichtVerfuegbar;
        }

        eclDbM.getDbBundle().dbBlzBank.mandantenabhaengig = true;
        eclDbM.getDbBundle().dbBlzBank.updateAllUnverarbeitet();
        eclDbM.getDbBundle().dbBlzBank.mandantenabhaengig = false;

        if (egxBankdatenRC.result != null) {

            for (EgxGetBankdatenResult iEgxGetBankdatenResult : egxBankdatenRC.result) {

                EclBlzBank lBlzBank = new EclBlzBank(); // lPlzOrt.id=iEgxGetPlzOrtResult.nr;
                lBlzBank.blz = String.valueOf(iEgxGetBankdatenResult.blz);
                lBlzBank.bankname = iEgxGetBankdatenResult.bezeichnung;
                lBlzBank.bic = iEgxGetBankdatenResult.bic;
                lBlzBank.version = 1;

                eclDbM.getDbBundle().dbBlzBank.mandantenabhaengig = true;
                int rc = eclDbM.getDbBundle().dbBlzBank.readBlz(lBlzBank.blz);
                if (rc == 0) {
                    eclDbM.getDbBundle().dbBlzBank.insert(lBlzBank);
                } else {
                    eclDbM.getDbBundle().dbBlzBank.update(lBlzBank);
                }
                eclDbM.getDbBundle().dbBlzBank.mandantenabhaengig = false;

                System.out.println(iEgxGetBankdatenResult.bezeichnung);

            }

            if (egxBankdatenRC._links.next != null && !egxBankdatenRC._links.next.equals("")) {
                String link_next = egxBankdatenRC._links.next.split("bankdaten")[1];
                while (link_next != null && !link_next.equals("")) {
                    egxBankdatenRC = brMGenossenschaftCall.doGetRequestBankdaten("", link_next);
                    
                    if (egxBankdatenRC==null) {
                        return CaFehler.perRemoteAktienregisterNichtVerfuegbar;
                    }

                    if (egxBankdatenRC.result != null) {

                        for (EgxGetBankdatenResult iEgxGetBankdatenResult : egxBankdatenRC.result) {

                            EclBlzBank lBlzBank = new EclBlzBank(); // lPlzOrt.id=iEgxGetPlzOrtResult.nr;
                            lBlzBank.blz = String.valueOf(iEgxGetBankdatenResult.blz);
                            lBlzBank.bankname = iEgxGetBankdatenResult.bezeichnung;
                            lBlzBank.bic = iEgxGetBankdatenResult.bic;
                            lBlzBank.version = 1;

                            eclDbM.getDbBundle().dbBlzBank.mandantenabhaengig = true;
                            int rc = eclDbM.getDbBundle().dbBlzBank.readBlz(lBlzBank.blz);
                            if (rc == 0) {
                                eclDbM.getDbBundle().dbBlzBank.insert(lBlzBank);
                            } else {
                                eclDbM.getDbBundle().dbBlzBank.update(lBlzBank);
                            }
                            eclDbM.getDbBundle().dbBlzBank.mandantenabhaengig = false;

                            System.out.println(iEgxGetBankdatenResult.bezeichnung);

                        }
                    }

                    if (egxBankdatenRC._links.next != null && !egxBankdatenRC._links.next.equals("")) {
                        link_next = egxBankdatenRC._links.next.split("bankdaten")[1];
                    } else {
                        link_next = "";
                    }
                }
            }

            /* Nun noch "alte" rauslöschen */
            eclDbM.getDbBundle().dbBlzBank.mandantenabhaengig = true;
            eclDbM.getDbBundle().dbBlzBank.deleteAllUnverarbeitet();
            eclDbM.getDbBundle().dbBlzBank.mandantenabhaengig = false;

        }

        return 1;
    }

}
