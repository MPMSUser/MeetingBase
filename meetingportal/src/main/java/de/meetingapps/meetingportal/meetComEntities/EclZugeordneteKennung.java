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

public class EclZugeordneteKennung {
    /**Kennung, der die andere Kennung zugeordnet wird
     * LEN=20*/
    public String loginKennung = "";

    /**Kennung, die zugeordnet wird
     * LEN=20*/
    public String zugeordneteKennung = "";

    /**Zugeordnete Kennung - Art
     * Gemäß KonstLoginKennung*/
    public int kennungArt = 0;

    /**0=noch nicht geprüft
     * 1=Zuordnung ist auch für Präsenz zulässig
     * 2=abgelehnt*/
    public int zuordnungIstFuerPraesenzVerifiziert = 0;
}
