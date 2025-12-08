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

import java.util.LinkedList;
import java.util.List;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaPasswortVerschluesseln;
import de.meetingapps.meetingportal.meetComEclM.EclDbM;
import de.meetingapps.meetingportal.meetComEclM.EclParamM;
import de.meetingapps.meetingportal.meetComEclM.EclUserLoginM;
import de.meetingapps.meetingportal.meetComEntities.EclUserLogin;
import de.meetingapps.meetingportal.meetComEntities.EclUserProfile;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@RequestScoped
@Named
public class UMandantenKennung {

    private @Inject EclDbM eclDbM;
    
    private @Inject UMandantenKennungSession uMandantenKennungSession;
    
    private @Inject EclParamM eclParamM;
    private @Inject XSessionVerwaltung xSessionVerwaltung;

    private @Inject USession uSession;


    private int logDrucken = 10;

    /**Benötigt weitere*/
    public void init(boolean pBereitsOffen) {
        if (pBereitsOffen==false) {
            eclDbM.openAll();
        }
        
        eclDbM.getDbBundle().dbUserLogin.profileVerarbeiten=true;
        eclDbM.getDbBundle().dbUserLogin.lese_alleProfileZuKlasse(eclParamM.getParam().paramBasis.profileKlasse);
        eclDbM.getDbBundle().dbUserLogin.profileVerarbeiten=false;
       
        if (pBereitsOffen==false) {
            eclDbM.closeAll();
        }
        
        List<EclUserLoginM> userLoginMList=new LinkedList<EclUserLoginM>();
        int anz=eclDbM.getDbBundle().dbUserLogin.anzUserLoginGefunden();
        CaBug.druckeLog("anz="+anz, logDrucken, 10);
        if (anz>0) {
            for (int i=0;i<anz;i++) {
                EclUserLoginM lUserLoginM=new EclUserLoginM();
                lUserLoginM.copyFromEclUserLogin(eclDbM.getDbBundle().dbUserLogin.userLoginGefunden(i));
                userLoginMList.add(lUserLoginM);
            }
        }
        
        uMandantenKennungSession.setProfileList(userLoginMList);
     }


    /********************************Buttons*****************************************/
    
    public String doAnzeigenVorhandeneKennungen() {
        if (!xSessionVerwaltung.pruefeUStart("uMandantenKennung", "")) {
            xSessionVerwaltung.setzeEnde();
            return "uDlgFehler";
        }
        uSession.clearFehlermeldung();
        eclDbM.openAll();
        
        leseMandantenKennungen();
        uMandantenKennungSession.setVorhandeneKennungenAnzeigen(true);
        
        eclDbM.closeAll();
        xSessionVerwaltung.setzeUEnde();
        return "";

    }

    public String doNeueKennung() {
        if (!xSessionVerwaltung.pruefeUStart("uMandantenKennung", "")) {
            xSessionVerwaltung.setzeEnde();
            return "uDlgFehler";
        }
        uSession.clearFehlermeldung();
        
        
        String lKennung=uMandantenKennungSession.getKennungInBearbeitung().trim();
        if (lKennung.isEmpty()) {
            uSession.setFehlermeldung("Bitte Kennung eingegeben, die neu angelegt werden soll!");
            xSessionVerwaltung.setzeUEnde();
            return "";
        }

        uMandantenKennungSession.clearUserLogin();
        
        eclDbM.openAll();
        
        int rc=eclDbM.getDbBundle().dbUserLogin.pruefeKennungVorhanden(lKennung);
        
        int anzUserLoginGefunden=eclDbM.getDbBundle().dbUserLogin.anzUserLoginGefunden();
        EclUserLogin lUserLogin=null;
        
        CaBug.druckeLog("rc="+rc+" anzUserLoginGefunden="+anzUserLoginGefunden, logDrucken, 10);
        
        eclDbM.closeAll();
        
        if (anzUserLoginGefunden!=0) {
            uSession.setFehlermeldung("Kennung bereits vorhanden!");
            xSessionVerwaltung.setzeUEnde();
            return "";
        }
        
        lUserLogin=new EclUserLogin();
        
        uMandantenKennungSession.setUserLoginInBearbeitung(lUserLogin);
        uMandantenKennungSession.setKennungInBearbeitung(lKennung);
        uMandantenKennungSession.setName("");
        uMandantenKennungSession.setEmail("");
        uMandantenKennungSession.setGesperrt(false);
           
        uMandantenKennungSession.setKennungIstInBearbeitung(true);
        uMandantenKennungSession.setKennungIstNeu(true);

        xSessionVerwaltung.setzeUEnde();
        return "";

    }

    
    public String doKennungBearbeiten(EclUserLoginM pUserLoginM) {
        if (!xSessionVerwaltung.pruefeUStart("uMandantenKennung", "")) {
            xSessionVerwaltung.setzeEnde();
            return "uDlgFehler";
        }
        uSession.clearFehlermeldung();
        
        uMandantenKennungSession.setKennungInBearbeitung(pUserLoginM.getKennung());
        ausfuehrenHolenBestehendeKennung();
        
        xSessionVerwaltung.setzeUEnde();
        return "";
    }
    
    public String doBestehendeKennung() {
        if (!xSessionVerwaltung.pruefeUStart("uMandantenKennung", "")) {
            xSessionVerwaltung.setzeEnde();
            return "uDlgFehler";
        }
        uSession.clearFehlermeldung();
      
        ausfuehrenHolenBestehendeKennung();
        
        xSessionVerwaltung.setzeUEnde();
        return "";
    }

    
    private void ausfuehrenHolenBestehendeKennung(){
        eclDbM.openAll();
       
        String lKennung=uMandantenKennungSession.getKennungInBearbeitung().trim();
        eclDbM.getDbBundle().dbUserLogin.leseZuKennung(lKennung, true);
        
        int anzUserLoginGefunden=eclDbM.getDbBundle().dbUserLogin.anzUserLoginGefunden();
        EclUserLogin lUserLogin=null;
        List<EclUserProfile> lZugeordneteProfileList=null;
        
        if (anzUserLoginGefunden>0) {
            lUserLogin=eclDbM.getDbBundle().dbUserLogin.userLoginArray[0];
       
            /*Alle dem User zugeordneten Profile einlesen*/
            eclDbM.getDbBundle().dbUserProfile.readUser(lUserLogin.userLoginIdent);
            lZugeordneteProfileList=eclDbM.getDbBundle().dbUserProfile.ergebnis();
        }
        
        eclDbM.closeAll();
        
        if (anzUserLoginGefunden==0) {
            uSession.setFehlermeldung("Kennung nicht vorhanden!");
            return;
        }
        
        uMandantenKennungSession.setUserLoginInBearbeitung(lUserLogin);
        uMandantenKennungSession.setName(lUserLogin.name);
        uMandantenKennungSession.setEmail(lUserLogin.email);
        uMandantenKennungSession.setGesperrt(lUserLogin.kennungGesperrtManuell);
        
        /*Nun die zugeordneten Profile in listProfile auf ausgewaehlt setzen*/
        List<EclUserLoginM> listProfile=uMandantenKennungSession.getProfileList();

        if (lZugeordneteProfileList!=null) {
            for (int i=0;i<lZugeordneteProfileList.size();i++) {
                EclUserProfile lZugeordnetesProfil=lZugeordneteProfileList.get(i);
                int gef=-1;
                for (int i1=0;i1<listProfile.size();i1++) {
                    if (listProfile.get(i1).getUserLoginIdent()==lZugeordnetesProfil.profilIdent) {
                        gef=i1;
                    }
                }
                if (gef==-1 ) {
                    CaBug.drucke("001 - zugeordnetes Profil nicht gefunden");
                }
                else {
                    listProfile.get(gef).setAusgewaehlt(true);
                }
            }
        }
        
        uMandantenKennungSession.setKennungIstInBearbeitung(true);
        uMandantenKennungSession.setKennungIstNeu(false);
        return;

    }
    
    public String doSpeichern() {
        if (!xSessionVerwaltung.pruefeUStart("uMandantenKennung", "")) {
            xSessionVerwaltung.setzeEnde();
            return "uDlgFehler";
        }
        uSession.clearFehlermeldung();
        
        String lKennungInBearbeitung=uMandantenKennungSession.getKennungInBearbeitung();
        String lName=uMandantenKennungSession.getName();
        String lEmail=uMandantenKennungSession.getEmail();
        String lInitialPasswort=uMandantenKennungSession.getInitialPasswort();
        EclUserLogin lUserLogin=uMandantenKennungSession.getUserLoginInBearbeitung();
        List<EclUserLoginM> profileList=uMandantenKennungSession.getProfileList();
 
        if (lKennungInBearbeitung.length() > 40) {
            uSession.setFehlermeldung("Benutzerkennung zu lang - maximal 40 Stellen zulässig!");
            xSessionVerwaltung.setzeUEnde();
            return "";
        }
        if (lKennungInBearbeitung.isEmpty()) {
            uSession.setFehlermeldung("Benutzerkennung darf nicht leer sein!");
            xSessionVerwaltung.setzeUEnde();
            return "";
        }

        if (lName.length()>200) {
            uSession.setFehlermeldung("Name zu lang - maximal 200 Stellen zulässig!");
            xSessionVerwaltung.setzeUEnde();
            return "";
        }
        if (lName.isEmpty()) {
            uSession.setFehlermeldung("Name darf nicht leer sein!");
            xSessionVerwaltung.setzeUEnde();
            return "";
        }
        
        if (lEmail.length()>200) {
            uSession.setFehlermeldung("E-Mail zu lang - maximal 200 Stellen zulässig!");
            xSessionVerwaltung.setzeUEnde();
            return "";
        }
        if (lEmail.isEmpty()) {
            uSession.setFehlermeldung("E-Mail darf nicht leer sein!");
            xSessionVerwaltung.setzeUEnde();
            return "";
        }
        
        if (!lInitialPasswort.isEmpty() || uMandantenKennungSession.isKennungIstNeu()) {
            if (lInitialPasswort.length()<8) {
                uSession.setFehlermeldung("Bitte Initialpasswort mit mindestens 8 Zeichen vergeben!");
                xSessionVerwaltung.setzeUEnde();
                return "";
            }
        }
        
        if (uMandantenKennungSession.isKennungIstNeu()) {
            lUserLogin=new EclUserLogin();
        }

        lUserLogin.name = lName;
        lUserLogin.email = lEmail;
        if (!lInitialPasswort.isEmpty()) {
            lUserLogin.passwort = CaPasswortVerschluesseln.verschluesseln(lInitialPasswort);
         }
        
        Boolean gesperrt=uMandantenKennungSession.isGesperrt();
        
        lUserLogin.kennungGesperrtManuell=gesperrt;
        
        eclDbM.openAll();

        if (uMandantenKennungSession.isKennungIstNeu()) {
            lUserLogin.kennung=lKennungInBearbeitung;
            
            int rc = eclDbM.getDbBundle().dbUserLogin.insert(lUserLogin, true);
            if (rc < 1) {
                eclDbM.closeAll();
                uSession.setFehlermeldung("Kennung kann nicht gespeichert werden, ggf. Kennung bereits vorhanden! Bitte Eingaben überprüfen / ggf. wiederholen!!");
                xSessionVerwaltung.setzeUEnde();
                return "";
            }
        }
        else {
            lUserLogin.name = lName;
            lUserLogin.email = lEmail;
            int rc = eclDbM.getDbBundle().dbUserLogin.update(lUserLogin);
            if (rc < 1) {
                eclDbM.closeAll();
                uSession.setFehlermeldung("Kennung kann nicht gespeichert werden! Bitte Eingaben überprüfen / ggf. wiederholen!!");
                xSessionVerwaltung.setzeUEnde();
                return "";
           }
        }
        
        /*Zugeordnete Profile speichern: erst bestehende Löschen, dann neue speichern*/
        eclDbM.getDbBundle().dbUserProfile.delete(lUserLogin.userLoginIdent);
        if (profileList!=null) {
            for (int i=0;i<profileList.size();i++) {
                if (profileList.get(i).isAusgewaehlt()) {
                    EclUserProfile lZugeordnetesProfil=new EclUserProfile();
                    lZugeordnetesProfil.userIdent=lUserLogin.userLoginIdent;
                    lZugeordnetesProfil.profilIdent=profileList.get(i).getUserLoginIdent();
                    eclDbM.getDbBundle().dbUserProfile.insert(lZugeordnetesProfil);
                }
            }
        }
        
        uMandantenKennungSession.setKennungIstInBearbeitung(false);
        leseMandantenKennungen();
        
        eclDbM.closeAll();
        
        uMandantenKennungSession.clearUserLogin();
        uMandantenKennungSession.setKennungIstInBearbeitung(false);

        xSessionVerwaltung.setzeUEnde();
        return "";

    }

    
    public String doAbbrechen() {
        if (!xSessionVerwaltung.pruefeUStart("uMandantenKennung", "")) {
            xSessionVerwaltung.setzeEnde();
            return "uDlgFehler";
        }
        uSession.clearFehlermeldung();
        
        uMandantenKennungSession.setKennungIstInBearbeitung(false);
        uMandantenKennungSession.clearUserLogin();
        
        xSessionVerwaltung.setzeUEnde();
        return "";

    }

    
    /********************Logik******************************************/
    private int leseMandantenKennungen() {
        eclDbM.getDbBundle().dbUserLogin.lese_all(true);
        List<EclUserLoginM> userLoginMList=new LinkedList<EclUserLoginM>();
        int anz=eclDbM.getDbBundle().dbUserLogin.anzUserLoginGefunden();
        CaBug.druckeLog("anz="+anz, logDrucken, 10);
        if (anz>0) {
            for (int i=0;i<anz;i++) {
                EclUserLoginM lUserLoginM=new EclUserLoginM();
                lUserLoginM.copyFromEclUserLogin(eclDbM.getDbBundle().dbUserLogin.userLoginGefunden(i));
                userLoginMList.add(lUserLoginM);
            }
        }
        
        uMandantenKennungSession.setUserList(userLoginMList);
       
        return 1;
    }
    
    
   
}
