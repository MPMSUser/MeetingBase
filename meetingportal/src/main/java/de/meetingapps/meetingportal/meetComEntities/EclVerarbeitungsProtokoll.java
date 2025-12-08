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

/**Zur Speicherung von Protokoll-Daten des Batch-Betriebs*/
public class EclVerarbeitungsProtokoll {

    public int mandant = 0;

    /**ident und verarbeitungslauf=primary Key; ident bezieht sich auf die Input-Datei, abhängig von art des verarbeitungslaufs*/
    public int ident = 0;
    public int verarbeitungslauf = 0;

    /**1 = ordnungsgemäß verarbeitet; -1=Fehler, nicht verarbeitet; 0=verarbeitet, aber mit Warnung*/
    public int ergebnis = 0;

    public int codeVerarbeitet = 0;
    public String textVerarbeitet = "";

    /**Fehler oder Warnung*/
    public int codeFehler = 0;
    public String textFehler = "";
}
