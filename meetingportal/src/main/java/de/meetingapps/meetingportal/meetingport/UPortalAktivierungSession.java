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
import java.util.LinkedList;
import java.util.List;

import de.meetingapps.meetingportal.meetComEh.EhPhasen;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

@SessionScoped
@Named
public class UPortalAktivierungSession implements Serializable {
    private static final long serialVersionUID = -4742838777339656973L;

    private boolean weisungAktuellNichtMoeglich=false;

    private List<EhPhasen> phasenListe=new LinkedList<EhPhasen>();

    /*****************Standard getter und setter***************************************/
    public boolean isWeisungAktuellNichtMoeglich() {
        return weisungAktuellNichtMoeglich;
    }

    public void setWeisungAktuellNichtMoeglich(boolean weisungAktuellNichtMoeglich) {
        this.weisungAktuellNichtMoeglich = weisungAktuellNichtMoeglich;
    }

    public List<EhPhasen> getPhasenListe() {
        return phasenListe;
    }

    public void setPhasenListe(List<EhPhasen> phasenListe) {
        this.phasenListe = phasenListe;
    }
    
    
}
