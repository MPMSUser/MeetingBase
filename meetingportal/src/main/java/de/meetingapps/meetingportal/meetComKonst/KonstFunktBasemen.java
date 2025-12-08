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
package de.meetingapps.meetingportal.meetComKonst;

public class KonstFunktBasemen {

    /**Sonstiges, nicht als Aufgabe definiert*/
    public static final int sonstige = 0;

    /**Aktionär hat ein neues Passwort angefordert, mit Angabe von Strasse und Ort.
     * Argument0=Aktionärsnummer
     * Argument1=Strasse
     * Argument2=Ort
     * Strasse und Ort müssen mit Aktienregistereintrag überprüft werden, dann muss neues Passwort Post an die
     * Versandadresse verschickt wer
     */
    public static final int aktionaerNeuesPasswortAdressePruefen = 1;

    /**Aktionär hat ein neues Passwort angefordert, ohne weitere Angabe.
     * Argument0=Aktionärsnummer
     * Ein neues Passwort muß an die Versandadresse verschickt werden.
     */
    public static final int aktionaerNeuesPasswort = 2;

}
