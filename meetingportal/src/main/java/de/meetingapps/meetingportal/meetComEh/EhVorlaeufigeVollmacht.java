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
package de.meetingapps.meetingportal.meetComEh;

import java.io.Serializable;

import de.meetingapps.meetingportal.meetComEntities.EclBestWorkflow;
import de.meetingapps.meetingportal.meetComEntities.EclVorlaeufigeVollmacht;
import de.meetingapps.meetingportal.meetComEntities.EclVorlaeufigeVollmachtEingabe;

/**Anzeige-Liste für Vollmachtsprüfungsprozess.
 * 
 * Enthält alle dem Vollmachtgebenden Mitglied
 * > bereits zugeordnete, in Papierform zugegangene Dokumente
 * > alle elektronisch eingetragenen gesetzlichen Vertreter
 * > alle elektronisch eingetragene bevollmächtigte
 * > alle elektronisch eingereichten Unterlagen
 */

public class EhVorlaeufigeVollmacht implements Serializable {
    private static final long serialVersionUID = -6816454071674018095L;

    /**0 = per Papier eingegangener Nachweis
     * 
     * 
     * Folgendes wieder gelöscht:
     * , bzw. bereits erstelltes tbl_vorlaeufigeVollmacht
     * (zu jedem Papierdokument besteht ja auch eine vorlaeufigeVollmacht; umgekehrt nicht zwangsweise,
     * da über elektronischeEingaben kein Dokument explizit zugeordnet wurde
     *
     * sonst analog EclVorlaeufigeVollmachtEingabe.artDerEingabe:
     * 1 = Nachweis
     * 2 = Bevollmächtigter
     * 3 = Gesetzlicher Vertreter
     */
    public int art=0;
    
    /**Bei art==0*/
    public EclVorlaeufigeVollmacht eclVorlaeufigeVollmacht=null;
    public EclBestWorkflow eclBestWorkflow = null;
    
    /**Bei art!=0*/
    public EclVorlaeufigeVollmachtEingabe eclVorlaeufigeVollmachtEingabe=null;

    
    /************************Standard getter und setter****************************/

    public int getArt() {
        return art;
    }

    public void setArt(int art) {
        this.art = art;
    }

    public EclVorlaeufigeVollmacht getEclVorlaeufigeVollmacht() {
        return eclVorlaeufigeVollmacht;
    }

    public void setEclVorlaeufigeVollmacht(EclVorlaeufigeVollmacht eclVorlaeufigeVollmacht) {
        this.eclVorlaeufigeVollmacht = eclVorlaeufigeVollmacht;
    }

    public EclBestWorkflow getEclBestWorkflow() {
        return eclBestWorkflow;
    }

    public void setEclBestWorkflow(EclBestWorkflow eclBestWorkflow) {
        this.eclBestWorkflow = eclBestWorkflow;
    }

    public EclVorlaeufigeVollmachtEingabe getEclVorlaeufigeVollmachtEingabe() {
        return eclVorlaeufigeVollmachtEingabe;
    }

    public void setEclVorlaeufigeVollmachtEingabe(EclVorlaeufigeVollmachtEingabe eclVorlaeufigeVollmachtEingabe) {
        this.eclVorlaeufigeVollmachtEingabe = eclVorlaeufigeVollmachtEingabe;
    }
    
    
    
    
}
