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

/**Anmeldung für diese Veranstaltungen (unabhängig von der eigentlichen HV) ist möglich.
 * Speziell für ku178 - Dialogveranstaltungen für Beiratswahl
 */
public class EclVeranstaltung {

    public int mandant = 0;

    public int ident = 0;

    /**Wird manuell vergeben, kann z.B. für Reihenfolge verwendet werden*/
    public int veranstaltungsnummer = 0;

    /**1 => wird angeboten
     * 2 => anzeigen, aber derzeit ausgebucht*/
    public int aktiv = 0;

    /**Verwendung für Bestätigung, Übersicht etc.
     * LEN=800*/
    public String kurzText = "";

    /**LEN jeweils 200*/
    /**ku178: Region*/
    public String text1 = "";
    /**ku178: Wo*/
    public String text2 = "";
    /**ku178: Wann*/
    public String text3 = "";
    /**ku178: Beginn*/
    public String text4 = "";
    /**ku178: Lokation*/
    public String text5 = "";
    /**ku178: Button-Beschriftung*/
    public String text6 = "";

    public int maximaleAnzahlAnmeldungen = 0;

    public int istAnzahlAnmeldungen = 0;

    public int istStandardFuerBundesland = 0;

    /***************Nicht in Datenbank**************************/
    /**Wird gefüllt mit der "Standard"-Veranstaltung für den angemeldeten Aktionär*/
    public boolean ausgewaehlt = false;
}
