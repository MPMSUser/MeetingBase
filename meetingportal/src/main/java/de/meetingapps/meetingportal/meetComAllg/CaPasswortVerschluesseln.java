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
package de.meetingapps.meetingportal.meetComAllg;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**Unumkehrbare Verschlüsselung (nach derzeitigem besten Wissen und Gewissen) eines
 * Passworts.
 */
public class CaPasswortVerschluesseln {

    /**Neue aktuelle (23.01.2018) Verschlüsselungsroutine.
     * Liefert verschlüsseltes Passwort der Länge 64 Zeichen zurück.
     * Unentschlüsselbar.
     */
    public static String verschluesseln(String pPasswort) {

        /*Zu Implementieren*/
        return "";

    }

    public static String liefereInitialPasswort(String pPasswort, int pLaenge) {
        return pPasswort.substring(pLaenge, pLaenge * 2);

    }

    /**Verschlüsselt nur den mittleren (relevanten) Teil des Initialpasswortes.
     * In laenge muß die Länge des relevanten Passworts übergeben werden, in pPasswort
     * also ein String der 3x die Länge laenge hat.
     * 
     * D.h. Erzeugen des Initialpasswortes:
     * > Erzeugen mit neuesPasswort=CaPasswortErzeugen.generatePWInitial
     * > Abspeichern von neuesPasswort als Initialpasswort in Datenbank (unverschlüsselt)
     * > Erzeugen des verschluesseltenPassworts=verschluesselnAusInitialPW(neuesPasswort ...)
     * > Abspeichern des verschlüsselten Passworts in Datenbank (nur dieses wird für Login-Vorgang verwendet!)
     */
    public static String verschluesselnAusInitialPW(String pPasswort, int laenge) {
        /*Zu Implementieren*/
        return "";

 
    }

    public static String decodeInitialPW(String pInitialPasswort) {
        /*Zu Implementieren*/
        return "";
    }

    /**Das Initialpasswort wird aus pNeuesPasswort erzeugt (
     * sprich einfach 3 mal hintereinander gehängt)
     */
    public static String codeInitialPW(String pNeuesPasswort) {
        return pNeuesPasswort+pNeuesPasswort+pNeuesPasswort;
    }
    
    public static int pruefePasswortZulaessig(String pPasswort) {
        
        int gefZahl=0;
        int gefKlein=0;
        int gefGross=0;
        for (int i=0;i<pPasswort.length();i++) {
            String hZeichen=pPasswort.substring(i,i+1);
            if ("0123456789".contains(hZeichen)) {gefZahl++;}
            if ("abcdefghijklmnopqrstuvwxyzäöüß".contains(hZeichen)) {gefKlein++;}
            if ("ABCDEFGHIJKLMNOPQRSTUVWXYZÄÖÜ".contains(hZeichen)) {gefGross++;}
        }
        
        if (gefZahl==0 || gefKlein==0 || gefGross==0) {
            return CaFehler.afPasswortEnthaeltNichtUnterschiedlicheZeichen;
        }
        return 1;
    }
}
