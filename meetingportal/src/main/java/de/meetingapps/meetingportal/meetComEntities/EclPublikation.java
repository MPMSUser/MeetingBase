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

public class EclPublikation {

    /**Mandantennummer*/
    public int mandant = 0;

    /** eindeutiger Key für Publikation (zusammen mit mandant), der unveränderlich ist. 
     */
    public int ident = 0;

    /**Versionsnummer - manuell nicht verändern! Ausschließlich durch Verwendung in DbMeldungen*/
    public long db_version;

    /**Reihenfolge, in der die Publikationen ausgewiesen werden (in Dialogen).
     * Niedrigster Wert=0, höchster Wert=9*/
    public int position = 0;

    /**Bezeichnung der Publikation
     * Länge=30*/
    public String bezeichnung = "";

    /**Versandweg, über den die Publikation vertrieben wird bzw. angefordert werden kann
     * =0 => Versandweg ist nicht zulässig
     * =1 => Versandweg ist zulässig*/
    public int[] publikationenZustellung = new int[10];

}
