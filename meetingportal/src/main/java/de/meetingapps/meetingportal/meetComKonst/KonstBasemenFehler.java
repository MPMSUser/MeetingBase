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

public class KonstBasemenFehler {

    public final static int unbekannt = 0;
    public final static int ok = 1;

    /**uKennung oder uPasswort falsch*/
    public final static int programmIdentifikationFalsch = -1;

    public final static int mandantUnbekannt = -2;
    public final static int hvIdUnbekannt = -3;

    /**ungültiger Wert, oder auf diesem Server nicht unterstützt*/
    public final static int produktionsKennzeichenFalsch = -4;

    /**Für Mandant / hvId gibt es keine zu dem Produktionskennzeichen passende Datenbank*/
    public final static int produktionsKennzeichenUnbekannt = -5;

    public final static int funktionscodeUnbekannt = -6;

    /**Für aufgerufenen mandant / hvId besteht keine Berechtigung für die
     * aufgerufene Funktion*/
    public final static int funktionscodeNichtBerechtigt = -7;

    public final static int aktionaersnummerUnbekant = -8;

    public final static int parameterUnzulaessig = -9;

}
