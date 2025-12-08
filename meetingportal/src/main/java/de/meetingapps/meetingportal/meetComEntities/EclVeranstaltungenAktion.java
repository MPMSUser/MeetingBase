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
import java.util.LinkedList;
import java.util.List;

public class EclVeranstaltungenAktion  implements Serializable {
    private static final long serialVersionUID = -6429953506232673506L;

    public int identAktion=0;
    
    /**Nur eines von beiden gefüllt*/
    public int gehoertZuElement=0;
    public int gehoertZuElementDetail=0;
    
    public int position=0;

    /**1 => Gastkartenausstellung "neutrale Gastkarte"
     *  (mit "Anzahl"-Element: entsprechende Anzahl wird ausgestellt; sonst die Anzahl, die in parameter1 aufgeführt ist
     *  
     * 2 => Mail-Quittung (ggf. mit Anhang)
     *   Textnummer Betreff in parameter1, Textnummer Text in parameter2 (beide in 2314 bis 2323), Anhang-Dateiname in parameter 3  
     *   (Mit Checkbox-Element: gehoertZuElementDetail)
     *   (Mit Radiobutton-Element: gehoertZuElement, parameter4 ist identisch mit Wert des Radiobuttons)
     */
    public int aktion=0;
    
    /**LEN=200*/
    public String parameter1="";
    public String parameter2="";
    public String parameter3="";
    public String parameter4="";
    

    /*++++++++++nicht in Datenbank++++++++++++++++*/
    
    /**Je nach Aktion:
     * 1 => Liste der Verweise auf die ausgestellten Gastkarten
     */
    public List<String> ergebnisDerAktion=new LinkedList<String>();
    public List<String> ergebnisDerAktionAlt=new LinkedList<String>();
    
    
    
    public EclVeranstaltungenAktion() {
        
    }
    
    public EclVeranstaltungenAktion(EclVeranstaltungenAktion pEclVeranstaltungenAktion) {
        identAktion=pEclVeranstaltungenAktion.identAktion;
        gehoertZuElement=pEclVeranstaltungenAktion.gehoertZuElement;
        gehoertZuElementDetail=pEclVeranstaltungenAktion.gehoertZuElementDetail;
        position=pEclVeranstaltungenAktion.position;
        aktion=pEclVeranstaltungenAktion.aktion;
        parameter1=pEclVeranstaltungenAktion.parameter1;
        parameter2=pEclVeranstaltungenAktion.parameter2;
        parameter3=pEclVeranstaltungenAktion.parameter3;
        parameter4=pEclVeranstaltungenAktion.parameter4;
     }

    /**************************Standard getter und setter***************************/

    
    public int getIdentAktion() {
        return identAktion;
    }

    public void setIdentAktion(int identAktion) {
        this.identAktion = identAktion;
    }

    public int getGehoertZuElement() {
        return gehoertZuElement;
    }

    public void setGehoertZuElement(int gehoertZuElement) {
        this.gehoertZuElement = gehoertZuElement;
    }

    public int getGehoertZuElementDetail() {
        return gehoertZuElementDetail;
    }

    public void setGehoertZuElementDetail(int gehoertZuElementDetail) {
        this.gehoertZuElementDetail = gehoertZuElementDetail;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getAktion() {
        return aktion;
    }

    public void setAktion(int aktion) {
        this.aktion = aktion;
    }

    public String getParameter1() {
        return parameter1;
    }

    public void setParameter1(String parameter1) {
        this.parameter1 = parameter1;
    }

    public String getParameter2() {
        return parameter2;
    }

    public void setParameter2(String parameter2) {
        this.parameter2 = parameter2;
    }

    public String getParameter3() {
        return parameter3;
    }

    public void setParameter3(String parameter3) {
        this.parameter3 = parameter3;
    }

    public String getParameter4() {
        return parameter4;
    }

    public void setParameter4(String parameter4) {
        this.parameter4 = parameter4;
    }





}
