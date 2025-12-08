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
package de.meetingapps.meetingportal.meetComWE;

import de.meetingapps.meetingportal.meetComEntities.EclVeranstaltung;

/**Status-Abfrage für alle "Sonder-Anmeldungen" von ku178 (also Generalversammlung und Dialogveranstaltung)*/
public class WEBlVeranstaltungenRC extends WERootRC {

    public EclVeranstaltung[] rcVeranstaltungArray = null;

    public boolean rcAngemeldet = false;
    public int rcAngemeldetAnzahlPersonen = 0;
    public EclVeranstaltung rcAngemeldetZuVeranstaltung = null;

    public boolean rcUeberbucht = false;

    public int rcGVAngemeldet = 0;
    public int rcGVBundesland = 0;
    public boolean rcZweiPersonenMoeglich = false;

}
