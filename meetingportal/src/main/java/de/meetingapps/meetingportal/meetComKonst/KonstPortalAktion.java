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

public class KonstPortalAktion {

    /**ausgewehlteHauptAktion: bisher noch keine Anmeldung erfolgt, es wird angemeldet und 
     * ggf. die entsprechende zusätzliche Willenserklärung ausgeführt*/
    public final static int HAUPT_NEUANMELDUNG = 1;

    /**ausgewählteHauptAktion: bereits angemeldet, nur noch zusätzliche Willenserklärung ausführen*/
    public final static int HAUPT_BEREITSANGEMELDET = 2;

    /**Achtung - nur in CtrlRootWeisungenNeu gültig / verwendbar, nicht im Portal oder App!*/
    public final static int HAUPT_AENDERN=3;
    /**Achtung - nur in CtrlRootWeisungenNeu gültig / verwendbar, nicht im Portal oder App!*/
    public final static int HAUPT_HV_VOLLMACHTWEISUNG_SRV=0;
 
    /**Achtung - nur in CtrlRootWeisungenNeu gültig / verwendbar, nicht im Portal oder App!*/
    public static final int HV_VOLLMACHTWEISUNG_SRV=0;
    
    public final static int EINE_EK_SELBST = 1;

    /**Ursprünglich: 2=nur bei Personengemeinschaften, 30 immer. Wird vereint.
     * Dabei überprüfen, ob 2 oder 30 überlebt*/
    public final static int ZWEI_EK_SELBST_PERSGEMEINSCHAFT = 2;
    public final static int EINE_EK_MIT_VOLLMACHT = 3;
    public final static int ZWEI_EK_SELBST_ODER_VOLLMACHT = 28;
    public final static int ZWEI_EK_SELBST_FUER_ALLE = 30;

    public final static int EK_DETAIL_ANZEIGEN = 26;
    public final static int GK_DETAIL_ANZEIGEN = 27;

    public final static int EK_STORNIEREN = 17;
    public final static int EK_MIT_VOLLMACHT_STORNIEREN = 18;
    public final static int GK_STORNIEREN = 16;
    public final static int VOLLMACHT_DRITTE_STORNIEREN = 19;

    public final static int VOLLMACHT_DRITTE=29;
    
    public final static int SRV_NEU = 4;
    public final static int BRIEFWAHL_NEU = 5;
    
    public final static int KIAV_MIT_WEISUNG_NEU = 6;
    /**Aktuell nicht vollständig implementiert*/
    public final static int KIAV_NUR_VOLLMACHT_NEU = 7;
    /**Aktuell nicht vollständig implementiert*/
    public final static int KIAV_VOLLMACHT_UND_WEISUNG_DEDIZIERT_NEU = 8;
    /**Aktuell nicht vollständig implementiert*/
    public final static int KIAV_VOLLMACHT_UND_WEISUNG_GEMAESS_VORSCHLAG_NEU = 9;
    
    public final static int DAUERVOLLMACHT_MIT_WEISUNG_NEU = 31;
    /**Aktuell nicht vollständig implementiert*/
    public final static int DAUERVOLLMACHT_NUR_VOLLMACHT_NEU = 32;
    /**Aktuell nicht vollständig implementiert*/
    public final static int DAUERVOLLMACHT_VOLLMACHT_UND_WEISUNG_DEDIZIERT_NEU = 33;
    /**Aktuell nicht vollständig implementiert*/
    public final static int DAUERVOLLMACHT_VOLLMACHT_UND_WEISUNG_GEMAESS_VORSCHLAG_NEU = 34;
    
    public final static int ORGANISATORISCH_MIT_WEISUNG_NEU = 35;
    /**Aktuell nicht vollständig implementiert*/
    public final static int ORGANISATORISCH_NUR_VOLLMACHT_NEU = 36;
    /**Aktuell nicht vollständig implementiert*/
    public final static int ORGANISATORISCH_VOLLMACHT_UND_WEISUNG_DEDIZIERT_NEU = 37;
    /**Aktuell nicht vollständig implementiert*/
    public final static int ORGANISATORISCH_VOLLMACHT_UND_WEISUNG_GEMAESS_VORSCHLAG_NEU = 38;

    public final static int SRV_AENDERN = 10;
    public final static int BRIEFWAHL_AENDERN = 11;
    public final static int KIAV_WEISUNG_AENDERN = 12;
    public final static int DAUERVOLLMACHT_AENDERN = 39;
    public final static int ORGANISATORISCH_AENDERN = 40;

    public final static int SRV_STORNIEREN = 13;
    public final static int BRIEFWAHL_STORNIEREN = 14;
    public final static int KIAV_STORNIEREN = 15;
    public final static int DAUERVOLLMACHT_STORNIEREN = 41;
    public final static int ORGANISATORISCH_STORNIEREN = 42;

    public final static int NUR_ANMELDUNG_OHNE_WEITERE_WILLENSERKLAERUNG = 43;

    /**true => die übergebene Willenserklärung erfordert zwei Meldungen; d.h. falls bereits 1 Meldung vorhanden,
     * müssen daraus 2 gemacht werden
     * @return
     */
    public static boolean wkErfordertZweiMeldungen(int pAktion) {
        if (pAktion == ZWEI_EK_SELBST_PERSGEMEINSCHAFT || pAktion == ZWEI_EK_SELBST_ODER_VOLLMACHT
                || pAktion == ZWEI_EK_SELBST_FUER_ALLE) {
            return true;
        }
        return false;
    }
}
