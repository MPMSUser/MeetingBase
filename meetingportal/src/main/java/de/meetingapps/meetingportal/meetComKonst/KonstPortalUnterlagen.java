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

public class KonstPortalUnterlagen {

    public static final int ART_UNTERLAGE_BUTTON=0;
    
    /**Bei dieser Art wird ARTSTYLE ignoriert*/
    public static final int ART_UEBERSCHRIFT=1;
    
    /**Bei dieser Art wird ARTSTYLE ignoriert*/
    public static final int ART_TEXT=2;
    
    public static final int ART_LINK_BUTTON=4;
    public static final int ART_VIDEO_BUTTON=3;
    
    public static final int ART_VERWEIS=5;
    
    public static final int ARTSTYLE_BUTTON_ALS_BUTTON=1;
    public static final int ARTSTYLE_BUTTON_ALS_LINK=2;
    
    public static final int ANZEIGEN_LOGIN_OBEN=1;
    public static final int ANZEIGEN_LOGIN_UNTEN=2;
    public static final int ANZEIGEN_EXTERNE_SEITE=3;
    public static final int ANZEIGEN_UNTERLAGEN=4;
    public static final int ANZEIGEN_BOTSCHAFTEN=5;
    public static final int ANZEIGEN_PPORTAL=7;
    public static final int ANZEIGEN_LOGIN_UNTENODEROBEN=6;
    
    /**Liefert true für öffentliche Seiten (da die dort eingetragenen Unterlagen nicht
     * Phasenabhängig gesteuert werden)
     */
    public static boolean pruefeObOeffentlicheAnzeige(int pAnzeigenbereich) {
        switch (pAnzeigenbereich) {
        case ANZEIGEN_LOGIN_OBEN:
        case ANZEIGEN_LOGIN_UNTEN:
        case ANZEIGEN_EXTERNE_SEITE:
        case ANZEIGEN_LOGIN_UNTENODEROBEN:
            return true;
        }
        return false;
    }
    
    /**Liefert true für das Mitgliederportal (da die dort eingetragenen Unterlagen nicht
     * Phasenabhängig gesteuert werden)
     */
    public static boolean pruefeObMitgliederportalAnzeige(int pAnzeigenbereich) {
        switch (pAnzeigenbereich) {
        case ANZEIGEN_PPORTAL:
            return true;
        }
        return false;
    }

}
