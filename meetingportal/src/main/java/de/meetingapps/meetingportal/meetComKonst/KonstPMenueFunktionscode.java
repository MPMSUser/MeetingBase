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

import de.meetingapps.meetingportal.meetComAllg.CaBug;

/**Funktionscodes für das Menü des permanenten Portals ("P")*/
public class KonstPMenueFunktionscode {

    public final static int TRENNZEICHEN =-1;
    public final static int UNBEKANNT = 0;

    public final static int POSTEINGANG = 1;
    public final static int MEINE_DATEN = 2;
    public final static int DATEN_BESTAND = 3;
    public final static int DATEN_BESTAND_SUB1 = 14;
    public final static int DATEN_BESTAND_SUB2 = 16;
    public final static int KONTAKTFORMULAR = 4;
    public final static int HAUPTVERSAMMLUNG = 5;
    public final static int DIALOGVERANSTALTUNGEN = 6;
    public final static int PUBLIKATIONEN = 7;
    public final static int STARTSEITE = 11;
    public final static int EINSTELLUNGEN = 12;
    public final static int HAUPTVERSAMMLUNG_EINLADUNGSVERSAND = 13;
    public final static int HAUPTVERSAMMLUNG_ZUM_HV_PORTAL = 15;
    
    public final static int UNTERLAGEN=17;
    
    public final static int BEIRATSWAHL_ZUM_HV_PORTAL=18;
    
    public final static int VERANSTALTUNGEN=19;

    public final static int DATEN=8; //Test
    public final static int DATEN_SUB1=9; //Test
    public final static int DATEN_SUB2=10; //Test
    
    
    
    
    public static int textNr(int pFunktionscode) {
        
        switch (pFunktionscode) {
        case TRENNZEICHEN:return 0;
        case POSTEINGANG:return 1510;
        case MEINE_DATEN:return 1511;
        case DATEN_BESTAND:return 1512;
        case DATEN_BESTAND_SUB1:return 1973;
        case DATEN_BESTAND_SUB2:return 2022;
        case KONTAKTFORMULAR:return 1513;
        case HAUPTVERSAMMLUNG:return 1514;
        case DIALOGVERANSTALTUNGEN:return 1515;
        case PUBLIKATIONEN:return 1516;
        case STARTSEITE:return 1613;
        case EINSTELLUNGEN:return 1641;
        case HAUPTVERSAMMLUNG_EINLADUNGSVERSAND:return 1642;
        case HAUPTVERSAMMLUNG_ZUM_HV_PORTAL:return 1976;
        
        case BEIRATSWAHL_ZUM_HV_PORTAL:return 2134;
        case VERANSTALTUNGEN:return 2306;
        
        case DATEN:return 1530;
        case DATEN_SUB1:return 1531;
        case DATEN_SUB2:return 1532;
       }
        CaBug.drucke("unzulässiger Funktionscode="+pFunktionscode);
        return 0;
    }
    
    public static int iconNr(int pFunktionscode) {
        
        switch (pFunktionscode) {
        case TRENNZEICHEN:return 0;
        case POSTEINGANG:return 0;
        case MEINE_DATEN:return 0;
        case DATEN_BESTAND:return 0;
        case DATEN_BESTAND_SUB1:return 0;
        case DATEN_BESTAND_SUB2:return 0;
        case KONTAKTFORMULAR:return 0;
        case HAUPTVERSAMMLUNG:return 0;
        case DIALOGVERANSTALTUNGEN:return 0;
        case PUBLIKATIONEN:return 0;
        case STARTSEITE:return 0;
        case EINSTELLUNGEN:return 0;
        case HAUPTVERSAMMLUNG_EINLADUNGSVERSAND:return 0;
        case HAUPTVERSAMMLUNG_ZUM_HV_PORTAL:return 0;
        
        case UNTERLAGEN:return 0;
        
        case BEIRATSWAHL_ZUM_HV_PORTAL:return 0;
        case VERANSTALTUNGEN:return 0;

        case DATEN:return 0;
        case DATEN_SUB1:return 0;
        case DATEN_SUB2:return 0;

        }
        CaBug.drucke("unzulässiger Funktionscode="+pFunktionscode);
        return 0;
    }
}
