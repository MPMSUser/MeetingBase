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

public class EclVeranstaltungenWert {

    /**Verweis auf EclLoginDaten*/
    public String loginKennung = "";

    /**Verweise auf die entsprechenden EclVeranstaltungen**/
    public int identVeranstaltung=0;
    public int identElement=0;
    public int identDetail=0;
    public int identAktion=0;

    
    public String eingabeWert="";
    
    /**Fortlaufende Nummer, beginnend bei 0, für z.B. mehrere
     * ausgestellte Gastkarten
     */
    public int ergebnisNrInAKtion=0;
    /**Z.B. Verweis auf Gastkarte
     * LEN=100*/
    public String ergebnisDerAktion="";
    
    /**Weitere Reservefelder, zum Speichern und Finden - über ergebnisDerAktion hinaus
     * LEN=200*/
    public String parameter1="";
    public String parameter2="";
    public String parameter3="";
    public String parameter4="";
    
    public EclVeranstaltungenWert() {
        
    }
    
    public EclVeranstaltungenWert(EclVeranstaltungenWert pVeranstaltungenWert) {
        loginKennung=pVeranstaltungenWert.loginKennung;
        
        identVeranstaltung=pVeranstaltungenWert.identVeranstaltung;
        identElement=pVeranstaltungenWert.identElement;
        identDetail=pVeranstaltungenWert.identDetail;
        identAktion=pVeranstaltungenWert.identAktion;
        
        eingabeWert=pVeranstaltungenWert.eingabeWert;

        ergebnisNrInAKtion=pVeranstaltungenWert.ergebnisNrInAKtion;
        ergebnisDerAktion=pVeranstaltungenWert.ergebnisDerAktion;

        parameter1=pVeranstaltungenWert.parameter1;
        parameter2=pVeranstaltungenWert.parameter2;
        parameter3=pVeranstaltungenWert.parameter3;
        parameter4=pVeranstaltungenWert.parameter4;

    }
}
