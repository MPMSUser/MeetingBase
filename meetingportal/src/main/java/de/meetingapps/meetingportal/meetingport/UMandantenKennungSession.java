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

import java.io.Serializable;
import java.util.List;

import de.meetingapps.meetingportal.meetComEclM.EclUserLoginM;
import de.meetingapps.meetingportal.meetComEntities.EclUserLogin;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

@SessionScoped
@Named
public class UMandantenKennungSession  implements Serializable {
    private static final long serialVersionUID = -8073383512256009764L;

    private boolean kennungIstInBearbeitung=false; 
    private boolean kennungIstNeu=false;

    private String kennungInBearbeitung="";
    private String name="";
    private String email="";
    private String initialPasswort="";
    private boolean gesperrt=false;
    
    private EclUserLogin userLoginInBearbeitung=null;
    
    private boolean vorhandeneKennungenAnzeigen=false;
    private List<EclUserLoginM> userList=null;

    private List<EclUserLoginM> profileList=null;

    public void clear() {
        kennungIstInBearbeitung=false; 
        kennungIstNeu=false;
        vorhandeneKennungenAnzeigen=false;
        userList=null;
        profileList=null;
        clearUserLogin();
    }

    public void clearUserLogin() {
        userLoginInBearbeitung=null;
        kennungInBearbeitung="";
        name="";
        email="";
        initialPasswort="";
        gesperrt=false;
        
        if (profileList!=null) {
            for (int i1=0;i1<profileList.size();i1++) {
                profileList.get(i1).setAusgewaehlt(false);
            }
        }

    }

    
    /*********************Standard getter und setter*****************************/

    public boolean isVorhandeneKennungenAnzeigen() {
        return vorhandeneKennungenAnzeigen;
    }


    public void setVorhandeneKennungenAnzeigen(boolean vorhandeneKennungenAnzeigen) {
        this.vorhandeneKennungenAnzeigen = vorhandeneKennungenAnzeigen;
    }



    public List<EclUserLoginM> getUserList() {
        return userList;
    }



    public void setUserList(List<EclUserLoginM> userList) {
        this.userList = userList;
    }



    public boolean isKennungIstInBearbeitung() {
        return kennungIstInBearbeitung;
    }



    public void setKennungIstInBearbeitung(boolean kennungIstInBearbeitung) {
        this.kennungIstInBearbeitung = kennungIstInBearbeitung;
    }



    public String getKennungInBearbeitung() {
        return kennungInBearbeitung;
    }



    public void setKennungInBearbeitung(String kennungInBearbeitung) {
        this.kennungInBearbeitung = kennungInBearbeitung;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getInitialPasswort() {
        return initialPasswort;
    }

    public void setInitialPasswort(String initialPasswort) {
        this.initialPasswort = initialPasswort;
    }

    public EclUserLogin getUserLoginInBearbeitung() {
        return userLoginInBearbeitung;
    }

    public void setUserLoginInBearbeitung(EclUserLogin userLoginInBearbeitung) {
        this.userLoginInBearbeitung = userLoginInBearbeitung;
    }

    public List<EclUserLoginM> getProfileList() {
        return profileList;
    }

    public void setProfileList(List<EclUserLoginM> profileList) {
        this.profileList = profileList;
    }

    public boolean isGesperrt() {
        return gesperrt;
    }

    public void setGesperrt(boolean gesperrt) {
        this.gesperrt = gesperrt;
    }

    public boolean isKennungIstNeu() {
        return kennungIstNeu;
    }

    public void setKennungIstNeu(boolean kennungIstNeu) {
        this.kennungIstNeu = kennungIstNeu;
    }



    

    
    
}
