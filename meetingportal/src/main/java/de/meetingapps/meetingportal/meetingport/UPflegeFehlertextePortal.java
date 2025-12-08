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

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaString;
import de.meetingapps.meetingportal.meetComEclM.EclDbM;
import de.meetingapps.meetingportal.meetComEclM.EclParamM;
import de.meetingapps.meetingportal.meetComEclM.EclUserLoginM;
import de.meetingapps.meetingportal.meetComEntities.EclFehler;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@RequestScoped
@Named
public class UPflegeFehlertextePortal {

    private int logDrucken=10;
    private @Inject UPflegeFehlertextePortalSession uPflegeFehlertextePortalSession;
    private @Inject EclParamM eclParamM;
    private @Inject EclUserLoginM eclUserLoginM;
    private @Inject XSessionVerwaltung xSessionVerwaltung;
    private @Inject EclDbM eclDbM;

    public void initStandard() {
        uPflegeFehlertextePortalSession.setMandantenSpezifisch(false);
        uPflegeFehlertextePortalSession.setUeberschrift("Fehlertexte Portal Standard");
        uPflegeFehlertextePortalSession.setBasisSet(Integer.toString(eclParamM.getParam().paramPortal.basisSetStandardTexteVerwenden));
        uPflegeFehlertextePortalSession.clearDaten();
    }
    
    public void initMandant() {
        uPflegeFehlertextePortalSession.setMandantenSpezifisch(true);
        uPflegeFehlertextePortalSession.setUeberschrift("Fehlertexte Portal Mandant");
        uPflegeFehlertextePortalSession.setBasisSet(Integer.toString(eclParamM.getParam().paramPortal.basisSetStandardTexteVerwenden));
        uPflegeFehlertextePortalSession.clearDaten();
    }
    
    public String doWeiter() {
        if (!xSessionVerwaltung.pruefeUStart("uPflegeFehlertextePortal", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }
        if (uPflegeFehlertextePortalSession.getModus()!=0) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }
        
        String hBasisSet=uPflegeFehlertextePortalSession.getBasisSet().trim();
        if (hBasisSet.isEmpty() || !CaString.isNummern(hBasisSet)) {
            return xSessionVerwaltung.setzeUEnde();  
        }
        String hIdent=uPflegeFehlertextePortalSession.getIdent().trim();
        if (hIdent.isEmpty() || !CaString.isNummern(hIdent)) {
            return xSessionVerwaltung.setzeUEnde();  
        }
       
        eclDbM.openAll();
        EclFehler lFehler=null;
        int anz=0;
        
        /*Globale Texte einlesen*/
        lFehler=new EclFehler();
        lFehler.mandant=0;
        lFehler.basisSet=Integer.parseInt(hBasisSet);
        lFehler.ident=Integer.parseInt(hIdent)*(-1);
        anz=eclDbM.getDbBundle().dbFehler.read(lFehler);
        for (int i=0;i<anz;i++) {
            lFehler=eclDbM.getDbBundle().dbFehler.ergebnisPosition(i);
            if (lFehler.sprache==1 || lFehler.sprache==0) {
                if (uPflegeFehlertextePortalSession.isMandantenSpezifisch()) {
                    uPflegeFehlertextePortalSession.setFehlermeldungStandardDE(lFehler.fehlermeldung);
                }
                else {
                    uPflegeFehlertextePortalSession.setFehlermeldungDE(lFehler.fehlermeldung);
                }
                uPflegeFehlertextePortalSession.setKuerzel(lFehler.kuerzel);
            }
            else {
                if (uPflegeFehlertextePortalSession.isMandantenSpezifisch()) {
                    uPflegeFehlertextePortalSession.setFehlermeldungStandardEN(lFehler.fehlermeldung);
                }
                else {
                    uPflegeFehlertextePortalSession.setFehlermeldungEN(lFehler.fehlermeldung);
                }
            }
        }
       
        if (uPflegeFehlertextePortalSession.isMandantenSpezifisch()) {
            /*Mandanten-Spezifische Texte einlesen*/
            lFehler=new EclFehler();
            lFehler.mandant=eclParamM.getClGlobalVar().mandant;
            lFehler.basisSet=Integer.parseInt(hBasisSet);
            lFehler.ident=Integer.parseInt(hIdent)*(-1);
            anz=eclDbM.getDbBundle().dbFehler.read(lFehler);
            for (int i=0;i<anz;i++) {
                lFehler=eclDbM.getDbBundle().dbFehler.ergebnisPosition(i);
                if (lFehler.sprache==1 || lFehler.sprache==0) {
                    uPflegeFehlertextePortalSession.setFehlermeldungDE(lFehler.fehlermeldung);
                }
                else {
                    uPflegeFehlertextePortalSession.setFehlermeldungEN(lFehler.fehlermeldung);
                }
            }
        }

        eclDbM.closeAll();
        
        uPflegeFehlertextePortalSession.setModus(1);
        return xSessionVerwaltung.setzeUEnde();  
    }
    
    public String doAbbrechen() {
        if (uPflegeFehlertextePortalSession.getModus() != 1 && uPflegeFehlertextePortalSession.getModus() != 0) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uPflegeFehlertextePortal", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }
        
        if (uPflegeFehlertextePortalSession.getModus()==0) {
            return xSessionVerwaltung.setzeUEnde("uMenue", true, false, eclUserLoginM.getKennung());
           
        }
        
        uPflegeFehlertextePortalSession.clearDaten();
        return xSessionVerwaltung.setzeUEnde("uPflegeFehlertextePortal", true, false, eclUserLoginM.getKennung());
    }
 
    public String doSpeichern() {
        if (!xSessionVerwaltung.pruefeUStart("uPflegeFehlertextePortal", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }
        if (uPflegeFehlertextePortalSession.getModus()!=1) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }
        
        String hBasisSet=uPflegeFehlertextePortalSession.getBasisSet().trim();
        String hIdent=uPflegeFehlertextePortalSession.getIdent().trim();

        eclDbM.openAll();
        if (uPflegeFehlertextePortalSession.isMandantenSpezifisch()) {
            /*Mandantenspezifische Texte*/
            EclFehler lFehler=new EclFehler();
            lFehler.mandant=eclParamM.getClGlobalVar().mandant;
            lFehler.basisSet=Integer.parseInt(hBasisSet);
            lFehler.ident=Integer.parseInt(hIdent)*(-1);
            
            eclDbM.getDbBundle().dbFehler.delete(lFehler);
            String lTextDE=uPflegeFehlertextePortalSession.getFehlermeldungDE().trim();
            if (!lTextDE.isEmpty()) {
                lFehler.mandant=eclParamM.getClGlobalVar().mandant;
                lFehler.basisSet=Integer.parseInt(hBasisSet);
                lFehler.ident=Integer.parseInt(hIdent)*(-1);
                lFehler.kuerzel=uPflegeFehlertextePortalSession.getKuerzel();
                lFehler.sprache=1;
                lFehler.fehlermeldung=lTextDE;
                eclDbM.getDbBundle().dbFehler.insert(lFehler);
            }
            String lTextEN=uPflegeFehlertextePortalSession.getFehlermeldungEN().trim();
            if (!lTextEN.isEmpty()) {
                lFehler.mandant=eclParamM.getClGlobalVar().mandant;
                lFehler.basisSet=Integer.parseInt(hBasisSet);
                lFehler.ident=Integer.parseInt(hIdent)*(-1);
                lFehler.kuerzel=uPflegeFehlertextePortalSession.getKuerzel();
                lFehler.sprache=2;
                lFehler.fehlermeldung=lTextEN;
                eclDbM.getDbBundle().dbFehler.insert(lFehler);
            }
       }
        else {
            /*Globale Texte*/
            CaBug.druckeLog("Globale Texte", logDrucken, 10);
            EclFehler lFehler=new EclFehler();
            lFehler.mandant=0;
            lFehler.basisSet=Integer.parseInt(hBasisSet);
            lFehler.ident=Integer.parseInt(hIdent)*(-1);
            
            eclDbM.getDbBundle().dbFehler.delete(lFehler);
            
            String lTextDE=uPflegeFehlertextePortalSession.getFehlermeldungDE().trim();
            if (!lTextDE.isEmpty()) {
                lFehler.mandant=0;
                lFehler.basisSet=Integer.parseInt(hBasisSet);
                lFehler.ident=Integer.parseInt(hIdent)*(-1);
                lFehler.kuerzel=uPflegeFehlertextePortalSession.getKuerzel();
                lFehler.sprache=1;
                lFehler.fehlermeldung=lTextDE;
                CaBug.druckeLog("lTextDE="+lTextDE, logDrucken, 10);
                eclDbM.getDbBundle().dbFehler.insert(lFehler);
            }
            String lTextEN=uPflegeFehlertextePortalSession.getFehlermeldungEN().trim();
            if (!lTextEN.isEmpty()) {
                lFehler.mandant=0;
                lFehler.basisSet=Integer.parseInt(hBasisSet);
                lFehler.ident=Integer.parseInt(hIdent)*(-1);
                lFehler.kuerzel=uPflegeFehlertextePortalSession.getKuerzel();
                lFehler.sprache=2;
                lFehler.fehlermeldung=lTextEN;
                CaBug.druckeLog("lTextEN="+lTextEN, logDrucken, 10);
                eclDbM.getDbBundle().dbFehler.insert(lFehler);
            }
       }
        eclDbM.closeAll();
        uPflegeFehlertextePortalSession.clearDaten();
        return xSessionVerwaltung.setzeUEnde("uPflegeFehlertextePortal", true, false, eclUserLoginM.getKennung());

        
    }

}
