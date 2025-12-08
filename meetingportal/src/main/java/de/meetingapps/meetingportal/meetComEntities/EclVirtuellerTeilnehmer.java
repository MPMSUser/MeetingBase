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

/**Kein zugehöriges Table*/
public class EclVirtuellerTeilnehmer {

    /**Für temporäre Identifikation - sprich offset
     * innerhalb der Liste oder des Arrays
     */
    public int laufendeIdent = 0;

    /**1=Aktionär selbst
     * 2=Vertreter
     * 3=Insti
     * 4=Sonstige
     */
    public int art = 0;

    /**Je nach art - identPersonNatJur oder vergleichbares*/
    public int ident = 0;
    /**Aufbereiteter Name, Vorname Nachname bzw. Firmenname*/
    public String name = "";
    public String ort = "";
}
