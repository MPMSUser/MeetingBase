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
package de.meetingapps.meetingportal.meetComEclM;

import java.io.Serializable;

import de.meetingapps.meetingportal.meetComEntities.EclUserNachrichtEmpfaenger;

public class EclNachrichtEmpfaengerUserM implements Serializable {
    private static final long serialVersionUID = -544440441953339006L;

    private int identUserLogin = 0;
    private String name = "";
    private boolean ausgewaehlt = false;

    public EclNachrichtEmpfaengerUserM() {
        return;
    }

    public EclNachrichtEmpfaengerUserM(EclUserNachrichtEmpfaenger lUserNachrichtEmpfaenger) {
        identUserLogin = lUserNachrichtEmpfaenger.userLoginIdent;
        name = lUserNachrichtEmpfaenger.name;
        ausgewaehlt = false;
        return;
    }

    /**********Standard getter/Setter*************************/
    public int getIdentUserLogin() {
        return identUserLogin;
    }

    public void setIdentUserLogin(int identUserLogin) {
        this.identUserLogin = identUserLogin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isAusgewaehlt() {
        return ausgewaehlt;
    }

    public void setAusgewaehlt(boolean ausgewaehlt) {
        this.ausgewaehlt = ausgewaehlt;
    }

}
