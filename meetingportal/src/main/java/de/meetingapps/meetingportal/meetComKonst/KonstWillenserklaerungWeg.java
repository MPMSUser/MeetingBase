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

public class KonstWillenserklaerungWeg {

    /*Vorfeld der HV - Anmeldung und Weisungserteilung etc.*/
    public final static int papierPost = 1;
    public final static int fax = 2;
    public final static int eMail = 11;
    public final static int portal = 21;
    /*TODO Konsolidierung: Willenserkläerungsweg für Portal und App ist derzeit gleich - beide 21*/
    public final static int app = 21;
    public final static int anmeldestelleManuell = 51;
    public final static int schnittstelleExtern = 101;
    public final static int counterAufHV = 201;
    public final static int selbstbedienungAufHV = 202;
    public final static int onlineTeilnahme = 203;

    /*Abstimmungsweg*/
    public final static int abstManuelleEingabe = 1001;
    public final static int abstStapelScanner = 1002;
    public final static int abstTablet = 1003;
    public final static int abstApp = 1004;
    /**wird verwendet, wenn eine Stimmabgabe unabhängig von jeder Priorisierung auf jeden Fall gespeichert werden soll.
     * z.B. erforderlich, wenn Elektronisch vor Papier gilt, und elektronisch manuell korrigiert werden soll
     */
    public final static int bedingungslosesSpeichern = 1005;
    /**Maschinell eingetragen, z.B. Stimmausschluß*/
    public final static int abstMaschinell=1006;
    public final static int abstPortal=1007;

    
    /**Nur für Verwaltung und Abfragen*/
    public final static int ABSTIMMUNGSERGAENZUNG_BEGINN=1100;
    public final static int ABSTIMMUNGSERGAENZUNG_ENDE=1200;
    
    /**"Sonder-Wege" für Abstimmungsergänzung (also nicht direkt erfolgte Stimmabgabe)*/
    public final static int ABSTIMMUNGSERGAENZUNG_SRV=1100;
    public final static int ABSTIMMUNGSERGAENZUNG_BRIEFWAHL=1101;
    public final static int ABSTIMMUNGSERGAENZUNG_KIAV=1102;
    public final static int ABSTIMMUNGSERGAENZUNG_DAUER=1103;
    public final static int ABSTIMMUNGSERGAENZUNG_ORGA=1104;

    /**Stimmabgabe aufgrund einer Sammelkarte, in der die Weisung auf Kopfebene erfolgte*/
    public final static int ABSTIMMUNGSERGAENZUNG_SRV_KOPF=1110;
    public final static int ABSTIMMUNGSERGAENZUNG_BRIEFWAHL_KOPF=1111;
    public final static int ABSTIMMUNGSERGAENZUNG_KIAV_KOPF=1112;
    public final static int ABSTIMMUNGSERGAENZUNG_DAUER_KOPF=1113;
    public final static int ABSTIMMUNGSERGAENZUNG_ORGA_KOPF=1114;
    
    /**Stimmabgabe aufgrund einer Sammelkarte, die durch pure Anwesenheit die Stimme abgegeben hat*/
    public final static int ABSTIMMUNGSERGAENZUNG_SRV_ANWESENHEIT=1120;
    public final static int ABSTIMMUNGSERGAENZUNG_BRIEFWAHL_ANWESENHEIT=1121;
    public final static int ABSTIMMUNGSERGAENZUNG_KIAV_ANWESENHEIT=1122;
    public final static int ABSTIMMUNGSERGAENZUNG_DAUER_ANWESENHEIT=1123;
    public final static int ABSTIMMUNGSERGAENZUNG_ORGA_ANWESENHEIT=1124;

    /**Stimmabgabe "Implizit" (d.h. durch pure Anwesenheit)*/
    public final static int ABSTIMMUNGSERGAENZUNG_ANWESENHEIT=1131;

    public static int getStimmabgabeSammelkarte(int pSkIst) {
        switch (pSkIst) {
        case KonstSkIst.srvHV:
        case KonstSkIst.srv:
            return ABSTIMMUNGSERGAENZUNG_SRV;
        case KonstSkIst.briefwahl:
            return ABSTIMMUNGSERGAENZUNG_BRIEFWAHL;
        case KonstSkIst.kiav:
            return ABSTIMMUNGSERGAENZUNG_KIAV;
        case KonstSkIst.dauervollmacht:
            return ABSTIMMUNGSERGAENZUNG_DAUER;
        case KonstSkIst.organisatorisch:
            return ABSTIMMUNGSERGAENZUNG_ORGA;
        }
        return 0;
    }
 
    public static int getStimmabgabeSammelkarteKopfweisung(int pSkIst) {
        switch (pSkIst) {
        case KonstSkIst.srvHV:
        case KonstSkIst.srv:
            return ABSTIMMUNGSERGAENZUNG_SRV_KOPF;
        case KonstSkIst.briefwahl:
            return ABSTIMMUNGSERGAENZUNG_BRIEFWAHL_KOPF;
        case KonstSkIst.kiav:
            return ABSTIMMUNGSERGAENZUNG_KIAV_KOPF;
        case KonstSkIst.dauervollmacht:
            return ABSTIMMUNGSERGAENZUNG_DAUER_KOPF;
        case KonstSkIst.organisatorisch:
            return ABSTIMMUNGSERGAENZUNG_ORGA_KOPF;
        }
        return 0;
    }

    public static int getStimmabgabeSammelkarteAnwesenheit(int pSkIst) {
        switch (pSkIst) {
        case KonstSkIst.srvHV:
        case KonstSkIst.srv:
            return ABSTIMMUNGSERGAENZUNG_SRV_ANWESENHEIT;
        case KonstSkIst.briefwahl:
            return ABSTIMMUNGSERGAENZUNG_BRIEFWAHL_ANWESENHEIT;
        case KonstSkIst.kiav:
            return ABSTIMMUNGSERGAENZUNG_KIAV_ANWESENHEIT;
        case KonstSkIst.dauervollmacht:
            return ABSTIMMUNGSERGAENZUNG_DAUER_ANWESENHEIT;
        case KonstSkIst.organisatorisch:
            return ABSTIMMUNGSERGAENZUNG_ORGA_ANWESENHEIT;
        }
        return 0;
    }

    public static int getWeisungsWegPapierOderElektronisch(int pWeg) {
        if (pWeg >= 21 && pWeg <= 29) {
            return 2;
        }
        return 1;
    }

    /**1=Manuell oder Scanner; 2=App oder Tablet oder Portal; 3=bedingungslosesSpeichern*/
    public static int getAbstimmwegPapierOderElektronisch(int pWeg) {
        int ergebnis = 0;
        switch (pWeg) {
        case abstManuelleEingabe:
            ergebnis = 1;
            break;
        case abstStapelScanner:
            ergebnis = 1;
            break;
        case abstTablet:
            ergebnis = 2;
            break;
        case abstApp:
            ergebnis = 2;
            break;
        case abstMaschinell:
            ergebnis = 1;
            break;
        case abstPortal:
            ergebnis = 2;
            break;
        }
        return ergebnis;
    }

    public static String getText(int weg) {

        switch (weg) {
        case 1: 
            return "Papier (Post)";
        case 2: 
            return "Fax";
        case 11: 
            return "E-Mail";
        case 21: 
            return "Portal";
        case 22: 
            return "App";
        case 51: 
            return "Anmeldestelle, manuell";
        case 101: 
            return "Schnittstelle (extern)";
        case 201: 
            return "Counter auf HV";
        case 202: 
            return "Selbstbedienung auf HV";
        case 203: 
            return "Online-Teilnahme";
        case 1001: 
            return "Manuelle Eingabe";
        case 1002: 
            return "Stapel-Scanner";
        case 1003: 
            return "Tablet";
        case 1004: 
            return "App";
        case 1005: 
            return "Vorrang";
        case abstPortal: 
            return "Portal";

        }

        return "";

    }
    
    static public String getTextFuerStimmabgabe(int weg) {
        switch (weg) {
        case ABSTIMMUNGSERGAENZUNG_SRV:return "SRV";
        case ABSTIMMUNGSERGAENZUNG_BRIEFWAHL:return "Briefwahl";
        case ABSTIMMUNGSERGAENZUNG_KIAV:return "KIAV";
        case ABSTIMMUNGSERGAENZUNG_DAUER:return "Dauerv.";
        case ABSTIMMUNGSERGAENZUNG_ORGA:return "sonstigeSammelk";
        
        case ABSTIMMUNGSERGAENZUNG_SRV_KOPF:return "SRV_KOPF";
        case ABSTIMMUNGSERGAENZUNG_BRIEFWAHL_KOPF:return "Briefwahl_KOPF";
        case ABSTIMMUNGSERGAENZUNG_KIAV_KOPF:return "KIAV_KOPF";
        case ABSTIMMUNGSERGAENZUNG_DAUER_KOPF:return "Dauerv._KOPF";
        case ABSTIMMUNGSERGAENZUNG_ORGA_KOPF:return "sonstigeSammelk_KOPF";


        case ABSTIMMUNGSERGAENZUNG_SRV_ANWESENHEIT:return "SRV_ANW";
        case ABSTIMMUNGSERGAENZUNG_BRIEFWAHL_ANWESENHEIT:return "Briefwahl_ANW";
        case ABSTIMMUNGSERGAENZUNG_KIAV_ANWESENHEIT:return "KIAV_ANW";
        case ABSTIMMUNGSERGAENZUNG_DAUER_ANWESENHEIT:return "Dauerv._ANW";
        case ABSTIMMUNGSERGAENZUNG_ORGA_ANWESENHEIT:return "sonstigeSammelk_ANW";
        
        case ABSTIMMUNGSERGAENZUNG_ANWESENHEIT:return "Anwesend";
        }
        return "Abgabe";
    }

    static public boolean wegIstKeineErgaenzung(int pWeg) {
        if (pWeg<ABSTIMMUNGSERGAENZUNG_BEGINN || pWeg>ABSTIMMUNGSERGAENZUNG_ENDE) {
            return true;
        }
        return false;
    }
}
