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
package de.meetingapps.meetingportal.meetComBl;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaPasswortErzeugen;
import de.meetingapps.meetingportal.meetComAllg.CaPasswortVerschluesseln;
import de.meetingapps.meetingportal.meetComDb.DbBundle;

public class BlKennung {

    private int logDrucken=3;
    
    private DbBundle lDbBundle = null;

    public String ergebnisKennung = "";
    
    /**Das Initialpasswort, im Klartext, sonst nichts.*/
    public String ergebnisPasswortInitial = "";
    
    /**Das Initialpasswort in der Mitte; vorne und hinten "Dummy-Passwörter"; das ganze fertig um
     * in die Datenbank als Initial-Passwort gespeichert zu werden
     */
    public String ergebnisPasswortInitialFuerDatenbank="";
    
    /**Initial-Passwort verschlüsselt, um als Passwort in Datenbank gespeichert zu werden*/
    public String ergebnisPasswortInitialVerschluesselt="";
    
    /**M001J2021AP <2ZeichenNachname><2ZeichenVorname><LfdNr>
     * Falls Name / Vorname kürzer, dann wird der String insgesamt kürzer
     */
    public String ergebnisOeffentlicheID = "";

    public BlKennung(DbBundle pDbBundle) {
        lDbBundle = pDbBundle;
    }


    /**Wird auch aufgerufen, wenn "nur" eine öffentlicheID benötigt wird (da die Lfd. Nr. der öffentlichen ID
     * identisch ist mit ID der vergebenen Kennung)
     * 
     * Vornahme kann "" sein, und wird dann mit "JU" belegt.
     * 
     */
    public int neueKennungUndOeffentlicheID(String pName, String pVorname) {
        
        CaPasswortErzeugen caPasswortErzeugen = new CaPasswortErzeugen();
        ergebnisPasswortInitial = caPasswortErzeugen.generatePW(lDbBundle.param.paramPortal.passwortMindestLaenge, 1, 1,
                true); ;
        String passwortInitialVorne = caPasswortErzeugen.generatePW(lDbBundle.param.paramPortal.passwortMindestLaenge, 1, 1,
                true); //passwort vorne;
        String passwortInitialHinten = caPasswortErzeugen.generatePW(lDbBundle.param.paramPortal.passwortMindestLaenge, 1, 1,
                true); //passwort Hinten;
        ergebnisPasswortInitialFuerDatenbank=passwortInitialVorne+ergebnisPasswortInitial+passwortInitialHinten;
        ergebnisPasswortInitialVerschluesselt=CaPasswortVerschluesseln.verschluesseln(ergebnisPasswortInitial);
        
        int kennungNr = lDbBundle.dbBasis.getInterneIdentKennung();
        ergebnisKennung = "S" + Integer.toString(kennungNr);
        
        String hName="", hVorname="";
        if (pName.length()>2){
            hName=pName.substring(0, 2);
        }
        else {
            hName=pName;
        }
        
        if (pVorname.length()>2){
            hVorname=pVorname.substring(0, 2);
        }
        else {
            hVorname=pVorname;
        }
       
        ergebnisOeffentlicheID = lDbBundle.getMandantPfad()+hName+hVorname+Integer.toString(kennungNr);
        CaBug.druckeLog("ergebnisOeffentlicheID="+ergebnisOeffentlicheID, logDrucken, 10);
        return 1;
    }

}
