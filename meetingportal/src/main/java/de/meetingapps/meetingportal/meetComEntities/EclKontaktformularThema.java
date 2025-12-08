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
package de.meetingapps.meetingportal.meetComEntities;

import java.io.Serializable;
import java.util.List;

public class EclKontaktformularThema  implements Serializable {
    private static final long serialVersionUID = -6999297383955535495L;

    /**Wird frei vergeben - zur Identifikation, welches Thema ausgewählt wurde.
     * Muß eindeutig sein.
     */
    public int ident=0;
    
    public long db_version=0;
    
    /**Lfd.-Nr. Haupteintrag*/
    public int nrHaupt=0;
    
    /**Lfd.Nr. Untereintrag
     * >0 => ist Untereintrag, nrHaupt muß dann ebenfalls gefüllt sein*/
    public int nrSub=0;

    /**Text, der in Aufgabe / E-Mail etc. als "Auftragsthema" weitergegeben
     * wird
     * LEN=150
     * 
     * Wenn leer => Trennzeichen
     */
    public String weitergabeText="";
    
    /**LEN=150*/
    public String spracheDEText="";
    
    /**LEN=150*/
    public String spracheENText="";

    /**LEN=150*/
    public String sprache3Text="";

    /**LEN=150*/
    public String sprache4Text="";
    
    
    /**+++++++++++++++Ab hier nicht in Datenbank++++++++++++++++++++*/
    /**Liste der zu diesem Thema gehörenden
     * Unter-Themas */
    public List<EclKontaktformularThema> subthemenList=null;

    /**+++++++++++++++Spezielle Funktionen++++++++++++++++++++++*/
    public boolean trennzeichen() {
        if (weitergabeText.isEmpty()) {return true;} else {return false;}
    }

    public boolean subthemaVorhanden() {
        if (subthemenList==null || subthemenList.size()==0) {return false;}
        return true;
    }

    /*****************Standard setter und getter******************/

    public int getIdent() {
        return ident;
    }

    public void setIdent(int ident) {
        this.ident = ident;
    }

    public int getNrHaupt() {
        return nrHaupt;
    }

    public void setNrHaupt(int nrHaupt) {
        this.nrHaupt = nrHaupt;
    }

    public int getNrSub() {
        return nrSub;
    }

    public void setNrSub(int nrSub) {
        this.nrSub = nrSub;
    }

    public String getWeitergabeText() {
        return weitergabeText;
    }

    public void setWeitergabeText(String weitergabeText) {
        this.weitergabeText = weitergabeText;
    }

    public String getSpracheDEText() {
        return spracheDEText;
    }

    public void setSpracheDEText(String spracheDEText) {
        this.spracheDEText = spracheDEText;
    }

    public String getSpracheENText() {
        return spracheENText;
    }

    public void setSpracheENText(String spracheENText) {
        this.spracheENText = spracheENText;
    }

    public String getSprache3Text() {
        return sprache3Text;
    }

    public void setSprache3Text(String sprache3Text) {
        this.sprache3Text = sprache3Text;
    }

    public String getSprache4Text() {
        return sprache4Text;
    }

    public void setSprache4Text(String sprache4Text) {
        this.sprache4Text = sprache4Text;
    }

    public List<EclKontaktformularThema> getSubthemenList() {
        return subthemenList;
    }

    public void setSubthemenList(List<EclKontaktformularThema> subthemenList) {
        this.subthemenList = subthemenList;
    }

    
}
