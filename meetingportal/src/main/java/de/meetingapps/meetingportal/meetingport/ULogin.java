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

import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComBVerwaltung.BvUserLogin;
import de.meetingapps.meetingportal.meetComBa.BaMailM;
import de.meetingapps.meetingportal.meetComBlManaged.BlMFuelleEclMAusPufferOderDBEE;
import de.meetingapps.meetingportal.meetComBlManaged.BlMPuffer;
import de.meetingapps.meetingportal.meetComEclM.EclDbM;
import de.meetingapps.meetingportal.meetComEclM.EclParamM;
import de.meetingapps.meetingportal.meetComEclM.EclUserLoginM;
import de.meetingapps.meetingportal.meetComEntities.EclEmittenten;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@RequestScoped
@Named
public class ULogin {

    private @Inject EclDbM eclDbM;
    private @Inject USession uSession;
    private @Inject XSessionVerwaltung xSessionVerwaltung;

    private @Inject ULoginSession uLoginSession;

    private @Inject UMenueAuswahlMandant uMenueAuswahlMandant;
    private @Inject UMenue uMenue;
    private @Inject EclUserLoginM eclUserLoginM;
    private @Inject EclParamM eclParamM;

    private @Inject BlMFuelleEclMAusPufferOderDBEE blMFuelleEclMAusPufferOderDBEE;
    private @Inject BlMPuffer blMPuffer;
    private @Inject BaMailM baMailM;

    /*****************************************Ansprung aus JSF heraus*******************************/
    public String doLogin() {
        uSession.aktiviereBO();
        xSessionVerwaltung.setTimeoutLang();
        return ausfuehrenLogin();
    }

    public String doLoginL() {
        uSession.aktiviereLMS();
        return ausfuehrenLogin();
    }

    public String doPasswortVergessen() {
        uSession.aktiviereBO();
        return ausfuehrenPasswortVergessen();
    }

    public String doPasswortVergessenL() {
        uSession.aktiviereLMS();
        return ausfuehrenPasswortVergessen();
    }

    public String ausfuehrenPasswortVergessen() {
    
        if (!xSessionVerwaltung.pruefeUStart("uLogin", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }

        String lKennung=uLoginSession.getVergessenKennung().trim();
        String lMail=uLoginSession.getVergessenMail().trim();
        
        if (lKennung.isEmpty()) {
            uSession.setFehlermeldung("Bitte geben Sie eine Kennung ein");
            return "";
        }
        if (lMail.isEmpty()) {
            uSession.setFehlermeldung("Bitte geben Sie eine E-Mail ein");
            return "";
        }
       
        ausfuehrenPasswortVergessen(lKennung, lMail);
        
        uSession.setFehlermeldung("Falls Ihre eingegebenen Daten korrekt waren, erhalten Sie eine E-Mail");
        uLoginSession.clear();
        return xSessionVerwaltung.setzeUEnde("uLogin", false, false, "");
 
    }
    
    
    
    public String doLogout() {

        String ziel = "";
        switch (uSession.getPortalVariante()) {
        case 1:
            ziel = "uLogin";
            break;
        case 2:
            ziel = "libetLogin";
            break;
        case 3:
            ziel = "lLogin";
            break;

        }
        return xSessionVerwaltung.setzeUEnde(ziel, true, false, eclUserLoginM.getKennung());
    }

    private String ausfuehrenLogin() {
        if (!xSessionVerwaltung.pruefeUStart("uLogin", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }

        eclDbM.openAllOhneParameterCheck();
        blMFuelleEclMAusPufferOderDBEE.fuelleAlleVariablenBeiStart();
        eclDbM.closeAll();

        uSession.clearSession();
        uSession.clearRequest();

        eclDbM.openAll();

        BvUserLogin bvUserLogin = new BvUserLogin();

        int rc = bvUserLogin.pruefeKennung(eclDbM.getDbBundle(), uLoginSession.getKennung(),
                uLoginSession.getPasswort(), true, 1);

        if (rc == CaFehler.afFalscheKennung || rc == CaFehler.afPasswortFalsch) {
            uSession.setFehlermeldung(CaFehler.getFehlertext(CaFehler.afFalscheKennungOderPasswort, 0));
            uLoginSession.clear();
            eclDbM.closeAll();
            xSessionVerwaltung.setzeUEnde();
            return "";
        }
        if (rc == CaFehler.afKennungGesperrt) {
            uSession.setFehlermeldung(CaFehler.getFehlertext(CaFehler.afKennungGesperrt, 0));
            uLoginSession.clear();
            eclDbM.closeAll();
            xSessionVerwaltung.setzeUEnde();
            return "";
        }

        if (rc == CaFehler.afKennungGesperrt) {
            /*TODO UPortal: afKennungGesperrt*/

            //			eclDbM.closeAll();
        }

        eclUserLoginM.copyFromEclUserLogin(bvUserLogin.rcUserLogin);
        if (!eclUserLoginM.pruefe_emittentenPortal()) {
            uSession.setFehlermeldung("Keine Zugriffsberechtigung für das Portal");
            uLoginSession.clear();
            eclDbM.closeAll();
            xSessionVerwaltung.setzeUEnde();
            return "";
        }

        if (eclUserLoginM.getMandant() != 0) {
            /*Mandanten-spezifischer User - Zugriff nur auf diesen Mandant zulässig*/

            /*Nun: 
             * > default Jahr etc. raussuchen, dieses initialisieren
             * > zulässige Mandanten mit dem zugeordneten Mandanten belegen
             * > Menü aufrufen aufrufen*/
            EclEmittenten lEmittenten = blMPuffer.getStandardEmittentFuerEmittentenPortal(eclUserLoginM.getMandant());
            if (lEmittenten == null) {
                uSession.setFehlermeldung(CaFehler.getFehlertext(CaFehler.afStandardMandantNichtGepflegt, 0));
                uLoginSession.clear();
                eclDbM.closeAll();
                xSessionVerwaltung.setzeUEnde();
                return "";
            }

            eclParamM.getClGlobalVar().mandant = lEmittenten.mandant;
            eclParamM.getClGlobalVar().hvJahr = lEmittenten.hvJahr;
            eclParamM.getClGlobalVar().hvNummer = lEmittenten.hvNummer;
            eclParamM.getClGlobalVar().datenbereich = lEmittenten.dbArt;
            eclDbM.closeAll();

            eclDbM.openAll();
            blMFuelleEclMAusPufferOderDBEE.fuelleAlleVariablenBeiStart();
            eclDbM.closeAll();

            eclDbM.openAll();
            uMenue.init();
            eclDbM.closeAll();

            uSession.setNurMandant(true);
            return xSessionVerwaltung.setzeUEnde("uMenue", true, false, eclUserLoginM.getKennung());

        } else {
            /*Mandanten-Übergreifender User*/
            eclDbM.closeAll();
            uMenueAuswahlMandant.init();
            uSession.setNurMandant(false);
            return xSessionVerwaltung.setzeUEnde("uMenueAuswahlMandant", true, false, eclUserLoginM.getKennung());

        }

        //		int loginGef=-1;
        //
        //		if (eclDbM.getDbBundle().paramServer.pLocalInternPasswort==1){
        //			if (dControllerLoginSession.getKennung().compareTo("daMeldeGuru")==0 && dControllerLoginSession.getPasswort().compareTo("a23el!e")==0){
        //				dControllerMenue.init();
        //				loginGef=1;
        //			}
        //			if (dControllerLoginSession.getKennung().compareTo("daMehrGuru")==0 && dControllerLoginSession.getPasswort().compareTo("B!auM2er")==0){
        //				dControllerMenue.initMehr();
        //				loginGef=1;
        //			}
        //			if (dControllerLoginSession.getKennung().compareTo("daOberGuru")==0 && dControllerLoginSession.getPasswort().compareTo("d!fSAwg6")==0){
        //				dControllerMenue.initAdmin();
        //				loginGef=1;
        //			}
        //		}
        //		else{
        //			dControllerMenue.initAdmin(); //Standardmäßig maximale Konfiguration
        //			if (dControllerLoginSession.getKennung().compareTo("daMeldeGuru")==0){
        //				dControllerMenue.init();
        //			}
        //			if (dControllerLoginSession.getKennung().compareTo("daMehrGuru")==0){
        //				dControllerMenue.initMehr();
        //			}
        //			
        //			loginGef=1;
        //		}
        //		if (loginGef==-1){
        //			return "";
        //		}
        //		
        //		eclDbM.openAll();
        //		if (eclDbM.getDbBundle().clGlobalVar.mandant>0){
        //			eclDbM.closeAll();
        //			return "dReportsN";
        //		}
        //		eclEmittentenFuerAktionaersPortalAuswahlListeM.fuelleEmittentenListeAdmin(eclDbM.getDbBundle(), dControllerLoginSession.getBaseLink());
        //		eclDbM.closeAll();
        //

        //		return "dAuswahl";
    }

    
    private void ausfuehrenPasswortVergessen(String pKennung, String pMail) {
        
        eclDbM.openAllOhneParameterCheck();
        blMFuelleEclMAusPufferOderDBEE.fuelleAlleVariablenBeiStart();
        eclDbM.closeAll();

        uSession.clearSession();
        uSession.clearRequest();

        eclDbM.openAll();

        BvUserLogin bvUserLogin = new BvUserLogin();
        bvUserLogin.passwortVergessen(eclDbM.getDbBundle(), pKennung, pMail);
        
        String neuesPasswort=bvUserLogin.rcNeuesPasswort;
        eclDbM.closeAll();
        
        if (!neuesPasswort.isEmpty()) {
            /*Mail verschicken*/
            String hText="Sehr geehrte Damen und Herren,\n"
                    + "..."
                    + "Diese Nachricht ist ausschließlich für den Empfänger bestimmt. Sollten Sie nicht der vorgesehene Adressat dieser Nachricht oder dessen Vertreter sein, so bitten wir Sie, sich mit dem Absender der E-Mail in Verbindung zu setzen. This message is intended for the recipient. If you are not the intended recipient of this message, please contact the sender of the email.";
            baMailM.sendenVonAdresse1(pMail, "Emittentenportal für Ihre Versammlung", hText);
        }
    }
}
