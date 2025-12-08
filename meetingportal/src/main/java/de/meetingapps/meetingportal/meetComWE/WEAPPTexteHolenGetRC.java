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

import de.meetingapps.meetingportal.meetComEntities.EclAppTexte;

/**Beschreibung der Eigenschaften: siehe BlAppVersionsabgleich*/
public class WEAPPTexteHolenGetRC extends WERootRC {

    public int anzahlZeilen = 0;
    public EclAppTexte[] updateTexteArray = null;

    /**Für die folgenden 3 Werte gilt: (siehe: BlAppVersionsabgleich)
     * =0 => kein Update der Texte enthalten
     * =1 => Update der Texte enthalten, Neubestätigung der Texte erforderlich 
     * =3 => nicht aktiv, d.h. kein Handlungsbedarf
     */
    public int disclaimerAppAktivNutzungsbedingungen = 0;
    public int disclaimerAppAktivDatenschutz = 0;
    public int disclaimerAppAktivVerwendungPersoenlicherDaten = 0;

}
