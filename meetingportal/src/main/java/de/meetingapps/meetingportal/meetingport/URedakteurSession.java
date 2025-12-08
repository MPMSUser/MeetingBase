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

import de.meetingapps.meetingportal.meetComEh.EhRedakteur;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

@SessionScoped
@Named
public class URedakteurSession implements Serializable {
    private static final long serialVersionUID = -5986270803165543841L;
    
    private List<EhRedakteur> redakteurListe=null;

    /******************Standard getter und setter*******************/
    public List<EhRedakteur> getRedakteurListe() {
        return redakteurListe;
    }

    public void setRedakteurListe(List<EhRedakteur> redakteurListe) {
        this.redakteurListe = redakteurListe;
    }
    
    
    
}
