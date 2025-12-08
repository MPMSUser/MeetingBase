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

import org.primefaces.model.StreamedContent;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

@SessionScoped
@Named
public class UVirtuelleHVSession implements Serializable {
    private static final long serialVersionUID = 9099396139571462572L;

    private String exportDateiname = "";

    private StreamedContent exportDatei = null;

    private String anzeige = "";

    /**Siehe KonstPortalFunktionen.
     * 
     * 90001=Allgemein für Reports (PDF)
     * 90002=Willenserklärungen für Mitteilungen
     * 90003=StreamUser
     */
    private int portalFunktion=0;
    
    /**1=CSV alle
     * 2=CSV neue
     * 3=Report alle
     */
    private int subFunktion=0;
    
    /**
     * 1=fragenCSVAlle
     * 2=fragenCSVNeue
     * 3=fragenReportAlle
     * 
     * 4=MitteilungenCSVAlle
     * 5=MitteilungenCSVNeue
     * 6=MitteilungenReportAlle
     * 
     * 7=Stream User
     * 
     * 8=Allgemein für Reports (PDF)
     * 
     * 9=Willenserklärungen für Mitteilungen
    
     */
    private int funktion = 0;

    /**Setzt alle Auswahl-Werte auf false*/
    public void init() {
    }

    public boolean separatesFenster() {
        if (funktion == 8) {
            return false;
        }
        return true;
    }

    /*************Standard-Getter und Setter**************************/

    public String getExportDateiname() {
        return exportDateiname;
    }

    public void setExportDateiname(String exportDateiname) {
        this.exportDateiname = exportDateiname;
    }

    public String getAnzeige() {
        return anzeige;
    }

    public void setAnzeige(String anzeige) {
        this.anzeige = anzeige;
    }

    public StreamedContent getExportDatei() {
        return exportDatei;
    }

    public void setExportDatei(StreamedContent exportDatei) {
        this.exportDatei = exportDatei;
    }

    public int getFunktion() {
        return funktion;
    }

    public void setFunktion(int funktion) {
        this.funktion = funktion;
    }

    public int getPortalFunktion() {
        return portalFunktion;
    }

    public void setPortalFunktion(int portalFunktion) {
        this.portalFunktion = portalFunktion;
    }

    public int getSubFunktion() {
        return subFunktion;
    }

    public void setSubFunktion(int subFunktion) {
        this.subFunktion = subFunktion;
    }

}
