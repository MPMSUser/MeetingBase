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
package de.meetingapps.meetingportal.meetingVote;

/**Zum Speichern einer reinen Stimmabgabe*/
public class EclStimmabgabe {

    /**Wird über Auto-Increment vergeben*/
    public int ident = 0;

    public int appVersion = 0;

    /**Len=40*/
    public String kennung = "";

    /**Len=40*/
    public String passwort = "";

    /**Wird vom Server ergänzt;*/
    public int identKennung = 0;

    /**Nummer der gerade laufenden Abstimmung - wird vom Server ergänzt*/
    public int abstimmungsnummer = 0;

    /**Raw-Format / System-Zeit. Wird vom Server ergänzt*/
    public long zeitstempelRaw = 0;

    /**0=gültig
     * 1=Kennung nicht gefunden
     * 2=Passwort falsch
     * 3=Kennung gesperrt
     * 4=App-Version falsch
     * 5=Stimmzettelnummer unzulässig
     * 6=Abgabeart unzulässig
     * 
     */
    public int stimmabgabeKZGueltig = 0;

    public int gelesenerStimmzettelnummer = 0;

    /**Gibt an, welche Form die Abstimmung hat*/
    public int abgabeArt = 0;

    public int anzahlStimmMarkierungen = 0;

    /**Maximal 20, beginnend bei 0*/
    public int[] abgabe = null;

    public EclStimmabgabe() {
        abgabe = new int[20];
    }
}
