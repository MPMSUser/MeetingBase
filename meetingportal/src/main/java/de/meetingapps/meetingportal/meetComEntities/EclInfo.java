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

/**Einfache, eher provisorische Informations-Anzeige an bestimmten Arbeitsplätzen.
 * Grund für Implementierung: Info an Versammlungsleiter ...
 *
 */
public class EclInfo implements Serializable {
    private static final long serialVersionUID = -2921560798489746400L;

    /**Wird nicht automatisch vergeben.
     * Aktuell: infoIdent 1 => Anzeige im Wortmeldeablauf Versammlungsleiter
     */
    public int infoIdent = 0;

    /**
     * Versionsnummer - manuell nicht verändern! Ausschließlich durch Verwendung
     * in DbMeldungen
     */
    public long db_version;

    /**1=Wortmeldeablauf*/
    public int infoKlasse=0;
    
    /**1=Versammlungsleiter*/
    public int empfaenger=0;
    
    /**Len=1000*/
    public String infoText="";

    
    
    /*******************************Standard getter und setter********************************************************/
    public int getInfoIdent() {
        return infoIdent;
    }

    public void setInfoIdent(int infoIdent) {
        this.infoIdent = infoIdent;
    }

    public long getDb_version() {
        return db_version;
    }

    public void setDb_version(long db_version) {
        this.db_version = db_version;
    }

    public int getInfoKlasse() {
        return infoKlasse;
    }

    public void setInfoKlasse(int infoKlasse) {
        this.infoKlasse = infoKlasse;
    }

    public int getEmpfaenger() {
        return empfaenger;
    }

    public void setEmpfaenger(int empfaenger) {
        this.empfaenger = empfaenger;
    }

    public String getInfoText() {
        return infoText;
    }

    public void setInfoText(String infoText) {
        this.infoText = infoText;
    } 
    
    
    
    
}
