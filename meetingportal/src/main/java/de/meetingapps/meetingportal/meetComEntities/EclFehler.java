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

public class EclFehler implements Serializable {
    private static final long serialVersionUID = -9124284196446184274L;

    /**Mandantennummer; mandant=0 => dieser Fehlertext gilt für alle Mandanten (soweit nicht "überschrieben"), d.h.:
     * z.B. Fehler 1 ist mit Mandant 0 Wert "Test" und mit Mandant 54 mit Wert "Test51" gespeichert =>
     * Für den Mandant 54 hat der Parameter den Wert "Test51", für alle anderen den Wert "Test"*/
    public int mandant = 0;

    public int basisSet = 0;

    /** eindeutiger Key (zusammen mit mandant), der unveränderlich ist. */
    public int ident = 0;

    /** Programm-Konstante für diese Fehlermeldung
     * LEN=70*/
    public String kuerzel = "";

    /**Versionsnummer - manuell nicht verändern! Ausschließlich durch Verwendung in Db*/
    public long db_version;

    /**Sprache, für die die Fehlermeldung erstellt ist.
     * 0 = für alle Sprachen (eher nicht zu verwenden - nur für Ausnahmefälle)
     * 1 = Deutsch
     * 2 = Englsich
     */
    public int sprache = 0;

    /**Wert der Fehlermeldung; Länge=120*/
    public String fehlermeldung = "";

    /**Selektionskriterien für effizientes Einlesen der Fehlermeldungen.
     * =1 => Parameter wird vom jeweiligen Modul benötigt*/
    public int selektionPortal = 0;
    public int selektionApp = 0;

}
