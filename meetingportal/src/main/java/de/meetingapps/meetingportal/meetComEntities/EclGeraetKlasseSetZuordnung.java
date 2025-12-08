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

public class EclGeraetKlasseSetZuordnung implements Serializable {
    private static final long serialVersionUID = -955808843853233742L;

    /**Verweis auf EclGeraeteSet*/
    public int geraeteSetIdent = 0;

    /**Gerätenummer*/
    public int geraeteNummer = 0;

    /**Geräteklasse*/
    public int geraeteKlasseIdent = 0;

    /**Standort des Gerätes. Verweis auf die noch nicht existierende Standortklasse.
     * Verwendung derzeit nur für Statistiken (Ku254 ...)
     */
    public int standortIdent = 0;

    /**=1 => bei diesem Start müssen alle lokalen Daten zurückgesetzt werden - 
     * und anschließend dieser Parameter wieder auf 0 gesetzt werden.
     * 
     * Verwendung:
     * > Löschen lokale Sicherungsdateien Tablets
     */
    public int lokaleDatenZuruecksetzenBeimNaechstenStart = 0;

}
