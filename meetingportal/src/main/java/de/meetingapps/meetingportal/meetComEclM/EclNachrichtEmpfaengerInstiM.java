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

import de.meetingapps.meetingportal.meetComEntities.EclInstiNachrichtEmpfaenger;

public class EclNachrichtEmpfaengerInstiM implements Serializable {
    private static final long serialVersionUID = -6068925054555711187L;

    private int identInsti = 0;
    private String name = "";
    private boolean bestandVorhanden = false;
    private boolean ausgewaehlt = false;

    public EclNachrichtEmpfaengerInstiM() {
        return;
    }

    public EclNachrichtEmpfaengerInstiM(EclInstiNachrichtEmpfaenger pEclInstiNachrichtEmpfaenger) {
        identInsti = pEclInstiNachrichtEmpfaenger.ident;
        name = pEclInstiNachrichtEmpfaenger.kurzBezeichnung;
        if (pEclInstiNachrichtEmpfaenger.bestand == 1) {
            bestandVorhanden = true;
        } else {
            bestandVorhanden = false;
        }
        ausgewaehlt = false;
    }

    /************Standard getter+Setter*******************/
    public int getIdentInsti() {
        return identInsti;
    }

    public void setIdentInsti(int identInsti) {
        this.identInsti = identInsti;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isBestandVorhanden() {
        return bestandVorhanden;
    }

    public void setBestandVorhanden(boolean bestandVorhanden) {
        this.bestandVorhanden = bestandVorhanden;
    }

    public boolean isAusgewaehlt() {
        return ausgewaehlt;
    }

    public void setAusgewaehlt(boolean ausgewaehlt) {
        this.ausgewaehlt = ausgewaehlt;
    }

}
