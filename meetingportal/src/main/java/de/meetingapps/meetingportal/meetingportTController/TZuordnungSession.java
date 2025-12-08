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
package de.meetingapps.meetingportal.meetingportTController;

import java.io.Serializable;

import de.meetingapps.meetingportal.meetComEntities.EclBesitzJeKennung;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;



@SessionScoped
@Named
public class TZuordnungSession implements Serializable {
    private static final long serialVersionUID = -9053543309148435284L;

    /**Für Zuordnung aufheben - Kennung, die nicht mehr zugeordnet werden soll*/
    private EclBesitzJeKennung besitzJeKennung=null;
    
    private String kennung="";
    private String name="";
    private String ort="";
    
    private String passwort="";
    
    
    
    /**************Standard getter und setter******************************/

    public String getKennung() {
        return kennung;
    }
    public void setKennung(String kennung) {
        this.kennung = kennung;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getOrt() {
        return ort;
    }
    public void setOrt(String ort) {
        this.ort = ort;
    }
    public EclBesitzJeKennung getBesitzJeKennung() {
        return besitzJeKennung;
    }
    public void setBesitzJeKennung(EclBesitzJeKennung besitzJeKennung) {
        this.besitzJeKennung = besitzJeKennung;
    }
    public String getPasswort() {
        return passwort;
    }
    public void setPasswort(String passwort) {
        this.passwort = passwort;
    }

    
}
