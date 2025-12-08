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

public class KonstPortalView {

    /*+++++++++++++++++++++++++++Fehlersituationen++++++++++++++++++++++++++++++++++++++++++++++++*/
    /**Aufruf-Link ist fehlerhaft, Mandant nicht bestimmbar. Abbruch - Mandantenneutral!*/
    public final static int fehlerLinkAufruf = 0;

    /**Zum Mandant ist kein aktives Portal zu finden. Abbruch - Mandantenneutral!*/
    public final static int fehlerPortalNichtAktiv = 1;

    /**Fehler technischer Art, der eigentlich so nicht auftreten dürfte. Abbruch - Mandantenneutral
     * Text nicht pflegbar*/
    public final static int FEHLER_UNBEKANNT = 2;

    /**Systemfehler technischer Art, der eigentlich so nicht auftreten dürfte - Weiter zur Login-Seite*/
    public final static int fehlerSysLogout = 3;

    /**Dialog-Fehler - weiter Zur Login_Seite
     * 204=Überschrift
     * 205=Text
     * 206=Button "Weiter zum Login"
     * */
    public final static int FEHLER_DIALOG = 4;

    /**Einstellungen: Inhalte mittlerweile von anderem User verändert - Weiter zur Login-Seite
     * Überschrift: 1462
     * Starttext: 1463
     * Button Weiter: 1464
     * */
    public final static int FEHLER_VERAENDERT = 5;

    /**Es wurden zwei Sessions geöffnet - Mandantenneutral*/
    public final static int fehlerZweiSession = 6;

    /**Aufruf-Link ist fehlerhaft, Mandant nicht bestimmbar. Abbruch - Mandantenneutral!*/
    public final static int fehlerLinkAufrufBestaetigen = 7;
    
    /**Anderer Mandant in selber Session aufgerufen*/
    public final static int fehlerZweiterMandant=8;

    /*+++++++++++++Mail-Bestätigung+++++++++++++*/
    public final static int BESTAETIGEN = 75;
    public final static int BESTAETIGEN2 = 76;
    public final static int BESTAETIGT = 77;
    public final static int BESTAETIGT2 = 78;

    /*+++++++++++++Passwort Vergessen - außerhalb des Logins, d.h. ohne Stream++++++++*/
    public final static int PASSWORT_VERGESSEN = 81;
    public final static int PASSWORT_VERGESSEN_QUITTUNG = 82;
    public final static int PW_ZURUECK = 83;
    public final static int pwzurueckapp = 84;

    /*+++++++++++++++++++++++++++++++Login, Logout, Einstellungen, Menü, sonstige Administration+++++++++++++++++++++++++++++++++++*/
    public final static int LOGIN = 101;

    /*Hinweis: bei Views >101 wird der Stream angezeigt*/
    public final static int EINSTELLUNGEN = 102;
    public final static int AUSWAHL = 103;
    public final static int EINSTELLUNGEN_BESTAETIGUNG = 104;

    /**keine eigene Maske, sondern nur zur Protokollierung bei Logout*/
    public final static int logout = 105;

    /**keine eigene Maske, sondern nur zur Protokollierung bei Browserschließen*/
    public final static int browserschliessen = 106;

    public final static int MONITORING = 107;

//    /*ersetzt durch FEHLER_VIEW*/
//    public final static int transaktionsAbbruchAndererUserAktiv = 109;

    /**Textnr. siehe KonstPortalFehlerView*/
    public final static int FEHLER_VIEW = 120;

    public final static int FEHLER_VIEW_TECHNISCH=121;
    
    /**ku178-Auswahl*/
    public final static int AUSWAHL1 = 110;
    public final static int AUSWAHL1_GENERALVERSAMMLUNG = 111;
    public final static int AUSWAHL1_TEILNAHME = 112;
    public final static int AUSWAHL1_TEILNAHMEGAST = 113;
    
    public final static int AUSWAHL1_DIALOGVERANSTALTUNGEN=114;
    public final static int AUSWAHL1_ANMELDEN_DIALOGVERANSTALTUNGEN=115;

    public final static int AUSWAHL1_GENERALVERSAMMLUNG_BRIEFWAHL = 116;

    public final static int AUSWAHL1_GENERALVERSAMMLUNG_BEIRATSWAHL = 117;


    
    
    /*120 siehe veraenderterBesitz*/

    /**Masken zur Behandlung von Weisungen, die von einer anderen Person erteilt wurden als die eingeloggte Person*/
    public final static int BESITZ_VERTRETUNG_ABFRAGEN=150;
    
    /*++++++++++++++++++++++++++++++++++++Präsenz Zu- und Abgang++++++++++++++++++++++++++++++++++*/
    /**Abfrage - Wer sind Sie?*/
    public final static int PRAESENZ_ZUGANG_ABFRAGE_PERSON = 201;

    /**Bitte bestätigen, dass Sie sind. Ggf. mit Auswahl der Karten, die Zugehen sollen*/
    public final static int PRASESENZ_ZUGANG_PERSON_BESTAETIGEN = 202;

    /**Abfrage - Wer sind Sie?*/
    public final static int PRAESENZ_ZUGANG_ABFRAGE_STORNIERUNG = 206;

//    /**Abfrage Abgang. Ggf. mit Auswahl der Karten, die Abgehen sollen*/
//    public final static int praesenzAbgang = 203;

    /**Anzeige: Sonstige Person wurde ausgewählt - nicht zulässig - Abbruch*/
    public final static int PRAESENZ_ZUGANG_FALSCHE_PERSON = 204;

    /**Abgang - Bestätigen lassen*/
    public final static int PRAESENZ_ABGANG_BESTAETIGUNG = 205;

    /*206 siehe oben*/

    /*+++++++++++++++++++++++++++++++Abstimmung+++++++++++++++++++++++++++++++++++++++++++++++++*/
    /*Stimmabgabe*/
    public final static int STIMMABGABE = 301;
    public final static int STIMMABGABE_AUSWAHL = 302;
    public final static int STIMMABGABE_QUITTUNG = 303;
    public final static int STIMMABGABE_DERZEIT_NICHT_MOEGLICH = 304;

    public final static int STIMMABGABE_ku310 = 311;

    /*++++++++++++++++++div. Portalfunktionen+++++++++++++++++++++++++++++++++++++*/
    public final static int STREAM_START = 401;
    public final static int UNTERLAGEN = 402;
    public final static int FRAGEN = 403;
    public final static int WORTMELDUNGEN = 404;
    public final static int WIDERSPRUECHE = 405;
    public final static int TEILNEHMERVERZEICHNIS = 406;
    public final static int ANTRAEGE = 407;
    public final static int SONSTIGEMITTEILUNGEN = 408;
    public final static int ABSTIMMUNGSERGEBNISSE = 409;
    public final static int NACHRICHTEN=410;
    public final static int BOTSCHAFTEN_EINREICHEN=411;
    public final static int BOTSCHAFTEN_ANZEIGEN = 412;
    public final static int RUECKFRAGEN = 413;

    /*++++++++++++++++Willenserklärungen+=>Verlagern ab 1.000++++++++++++++++++++*/
//    public final static int neueWillenserklaerung = 501;
    public final static int WEISUNG = 502;
    public final static int WEISUNG_BESTAETIGUNG = 503;
    public final static int WEISUNG_AENDERN = 504;
    public final static int WEISUNG_QUITTUNG = 505;
    public final static int WEISUNG_STORNIEREN = 506;
    public final static int WEISUNG_STORNIEREN_QUITTUNG = 507;

    /*++++++++++++++++ku178 / Spezialablauf++++++++++++++*/
    public final static int GENERALVERSAMMLUNG_ANABMELDEN = 601;
    public final static int GENERALVERSAMMLUNG_VOLLMACHT_STORNIEREN = 602;
//    public final static int DATENPFLEGE = 603;
//    public final static int DATENPFLEGE_QUITTUNG = 604;
    public final static int GENERALVERSAMMLUNG_BRIEFW_MITGLIED_DURCH_BEVOLL_STORNO = 605;

    /*+++++++++++++Willenserklärungen+++++++++++++++++++++++++++++++*/
    public final static int NUR_ANMELDUNG = 1001;
    public final static int NUR_ANMELDUNG_QUITTUNG = 1002;

    public final static int EINTRITTSKARTE = 1010;
    public final static int EINTRITTSKARTE_QUITTUNG = 1011;
    public final static int EINTRITTSKARTE_DETAIL = 1012;

    public final static int EINTRITTSKARTE_STORNIEREN = 1014;
    public final static int EINTRITTSKARTE_STORNIEREN_QUITTUNG = 1016;

    /**TODO - noch nicht implementiert. Bisheriges xhtml=aGGGastStatus*/
    public final static int GASTKARTE_DETAIL = 1013;

    /**TODO - noch nicht implementiert*/
    public final static int VOLLMACHT_DRITTE_STORNIEREN = 1015;

    /*++++++++++++++++++++++Gastkarten +++++++++++++++++++++++++*/
    public final static int GASTKARTE_UEBERSICHT=1021;
    public final static int GASTKARTE_EINGABE=1022;
    
    /***********************Permanent-Portal********************/
    
    public final static int P_LOGIN=2001;
    public final static int P_EINSTELLUNGEN = 2002;
    public final static int P_EINSTELLUNGEN_BESTAETIGUNG = 2003;
    public final static int P_AUSWAHL = 2004;
    public final static int P_KONTAKTFORMULAR=2005;
    public final static int P_PUBLIKATIONEN=2006;
    public final static int P_BESTAND=2007;
    public final static int P_AKTIONAERSDATEN=2008;
    public final static int P_REGISTRIEREN=2009;
    public final static int P_REGISTRIEREN_QUITTUNG=2010;
    public final static int P_PASSWORT_VERGESSEN=2011;
    public final static int P_PASSWORT_VERGESSEN_QUITTUNG=2012;
    public final static int P_EINSTELLUNG_AENDERN=2013;
    public final static int P_HV_EINLADUNGSVERSAND=2014;
    public final static int P_PW_ZURUECK=2015;
    public final static int P_BESTAND_HISTORIE=2016;
    public final static int P_HV_ZUR_HV=2017;
    public final static int P_BETEILIGUNGSERHOEHUNG=2018;
    public final static int P_UNTERLAGEN=2019;
    public final static int P_HV_ZUR_BEIRATSWAHL=2020;
    public final static int P_VERANSTALTUNGEN_AUSWAHL=2021;
    public final static int P_VERANSTALTUNGEN_DETAIL=2022;
   
    
    /*******************************Verwaltungsfunktionen****************************/
    public final static int ZUORDNUNG_AUFHEBEN=3001;
    public final static int ZUORDNUNG_EINRICHTEN=3002;
    public final static int ZUORDNUNG_EINRICHTEN_QUITTUNG=3003;

    
    static public String getText(int nr) {
        switch (nr) {
        case 0: {
            return "fehlerLinkAufruf";
        }
        case 1: {
            return "fehlerPortalNichtAktiv";
        }
        case 2: {
            return "FEHLER_UNBEKANNT";
        }
        case 3: {
            return "fehlerSysLogout";
        }
        case 4: {
            return "fehlerDialog";
        }
        case 5: {
            return "fehlerVeraendert";
        }
        case 6: 
            return "fehlerZweiSession";
        case 7: 
            return "fehlerLinkAufrufBestaetigen";
        case 8: 
            return "fehlerZweiterMandant";
        

        case 75: {
            return "BESTAETIGEN";
        }
        case 76: {
            return "BESTAETIGEN2";
        }
        case 77: {
            return "BESTAETIGT";
        }
        case 78: {
            return "BESTAETIGT2";
        }

        case 81: {
            return "passwortVergessen";
        }
        case 82: {
            return "passwortVergessenQuittung";
        }
        case 83: {
            return "pwzurueck";
        }
        case 84: {
            return "pwzurueckapp";
        }

        case 101: {
            return "login";
        }
        case 102: {
            return "EINSTELLUNGEN";
        }
        case 103: {
            return "auswahl";
        }
        case 104: {
            return "EINSTELLUNGEN_BESTAETIGUNG";
        }
        case 105: {
            return "logout";
        }
        case 106: {
            return "browserschliessen";
        }
        case 107: {
            return "monitoring";
        }
        case 109: {
            return "transaktionsAbbruchAndererUserAktiv";
        }

        case 110: 
            return "AUSWAHL1";
        case 111: 
            return "AUSWAHL1_GENERALVERSAMMLUNG";
        case 112: {
            return "AUSWAHL1_TEILNAHME";
        }
        case 113: 
            return "AUSWAHL1_TEILNAHMEGAST";
        case 114: 
            return "AUSWAHL1_DIALOGVERANSTALTUNGEN";
        case 115: 
            return "AUSWAHL1_ANMELDEN_DIALOGVERANSTALTUNGEN";
            
        case AUSWAHL1_GENERALVERSAMMLUNG_BRIEFWAHL: 
            return "AUSWAHL1_GENERALVERSAMMLUNG_BRIEFWAHL";
        case AUSWAHL1_GENERALVERSAMMLUNG_BEIRATSWAHL: 
            return "AUSWAHL1_GENERALVERSAMMLUNG_BEIRATSWAHL";

        case 120: {
            return "fehlerView";
        }
        case 121: {
            return "FEHLER_VIEW_TECHNISCH";
        }

        case 150: {
            return "BESITZ_VERTRETUNG_ABFRAGEN";
        }

        case 201: {
            return "PRAESENZ_ZUGANG_ABFRAGE_PERSON";
        }
        case 202: {
            return "PRASESENZ_ZUGANG_PERSON_BESTAETIGEN";
        }
//        case 203: {
//            return "praesenzAbgang";
//        }
        case 204: {
            return "PRAESENZ_ZUGANG_FALSCHE_PERSON";
        }
        case 205: {
            return "PRAESENZ_ABGANG_BESTAETIGUNG";
        }
        case 206: {
            return "PRAESENZ_ZUGANG_ABFRAGE_STORNIERUNG";
        }

        case 301: {
            return "STIMMABGABE";
        }
        case 302: {
            return "STIMMABGABE_AUSWAHL";
        }
        case 303: {
            return "STIMMABGABE_QUITTUNG";
        }
        case 304: 
            return "STIMMABGABE_DERZEIT_NICHT_MOEGLICH";

        case 401: {
            return "STREAM_START";
        }
        case 402: 
            return "UNTERLAGEN";
        case FRAGEN: 
            return "FRAGEN";
        case 404: 
            return "WORTMELDUNGEN";
        case 405: 
            return "WIDERSPRUECHE";
        case 406: {
            return "teilnehmerverzeichnis";
        }
        case 407: 
            return "ANTRAEGE";
        case 408: 
            return "SONSTIGEMITTEILUNGEN";
        case 409: 
            return "ABSTIMMUNGSERGEBNISSE";
        case 410: 
            return "NACHRICHTEN";
        case BOTSCHAFTEN_EINREICHEN: 
            return "BOTSCHAFTEN_EINREICHEN";
        case BOTSCHAFTEN_ANZEIGEN:
            return "BOTSCHAFTEN_ANZEIGEN";
        case RUECKFRAGEN:
            return "RUECKFRAGEN";
       
 
//        case 501: {
//            return "neueWillenserklaerung";
//        }
        case 502: 
            return "WEISUNG";
        case 503: {
            return "WEISUNG_BESTAETIGUNG";
        }
        case 504: {
            return "WEISUNG_AENDERN";
        }
        case 505: {
            return "WEISUNG_QUITTUNG";
        }
        case 506: {
            return "WEISUNG_STORNIEREN";
        }
        case 507: {
            return "weisungStornierenQuittung";
        }

        case 601: 
            return "GENERALVERSAMMLUNG_ANABMELDEN";
        case 602: 
            return "GENERALVERSAMMLUNG_VOLLMACHT_STORNIEREN";
        case 605: 
            return "GENERALVERSAMMLUNG_BRIEFW_MITGLIED_DURCH_BEVOLL_STORNO";

        case 1001:
            return "NUR_ANMELDUNG";
        case 1002:
            return "NUR_ANMELDUNG_QUITTUNG";

        case 1010:
            return "EINTRITTSKARTE";
        case 1011:
            return "EINTRITTSKARTE_QUITTUNG";
        case 1012:
            return "EINTRITTSKARTE_DETAIL";
        case 1013:
            return "GASTKARTE_DETAIL";
        case 1014:
            return "EINTRITTSKARTE_STORNIEREN";
        case 1015:
            return "VOLLMACHT_DRITTE_STORNIEREN";
        case 1016:
            return "EINTRITTSKARTE_STORNIEREN_QUITTUNG";

        case 1021:
            return "GASTKARTE_UEBERSICHT";
        case 1022:
            return "GASTKARTE_EINGABE";
        case 1023:
            return "GASTKARTE_STORNO";
            
            
            
        case 2001:
            return "P_LOGIN";
        case 2002:
            return "P_EINSTELLUNGEN";
        case 2003:
            return "P_EINSTELLUNGEN_BESTAETIGUNG";
        case 2004:
            return "P_AUSWAHL";
        case 2005:
            return "P_KONTAKTFORMULAR";
        case 2006:
            return "P_PUBLIKATIONEN";
        case 2007:
            return "P_BESTAND";
        case 2008:
            return "P_AKTIONAERSDATEN";
        case 2009:
            return "P_REGISTRIEREN";
        case 2010:
            return "P_REGISTRIEREN_QUITTUNG";
        case 2011:
            return "P_PASSWORT_VERGESSEN";
        case 2012:
            return "P_PASSWORT_VERGESSEN_QUITTUNG";
        case 2013:
            return "P_EINSTELLUNG_AENDERN";
        case 2014:
            return "P_HV_EINLADUNGSVERSAND";
        case 2015:
            return "P_PW_ZURUECK";
        case 2016:
            return "P_BESTAND_HISTORIE";
        case P_HV_ZUR_HV:
            return "P_HV_ZUR_HV";
        case P_BETEILIGUNGSERHOEHUNG:
            return "P_BETEILIGUNGSERHOEHUNG";
        case P_UNTERLAGEN:
            return "P_UNTERLAGEN";
        case P_HV_ZUR_BEIRATSWAHL:
            return "P_HV_ZUR_BEIRATSWAHL";
        case P_VERANSTALTUNGEN_AUSWAHL: 
            return "P_VERANSTALTUNGEN_AUSWAHL";
        case P_VERANSTALTUNGEN_DETAIL: 
            return "P_VERANSTALTUNGEN_DETAIL";

        case ZUORDNUNG_AUFHEBEN:
            return "ZUORDNUNG_AUFHEBEN";
        case ZUORDNUNG_EINRICHTEN:
            return "ZUORDNUNG_EINRICHTEN";
        case ZUORDNUNG_EINRICHTEN_QUITTUNG:
            return "ZUORDNUNG_EINRICHTEN_QUITTUNG";

        }
        return "";
    }

    static public String getSeitenname(int nr) {
        switch (nr) {
        case 2:
            return "iFehlerUnbekannt";
        case 4:
            return "iFehlerDialog";
        case 5:
            return "iFehlerVeraendert";
        case 6:
            return "iFehlerZweiSession";
        case 7:
            return "iFehlerLinkAufrufBestaetigen";
        case 8:
            return "iFehlerZweiterMandant";
      case 75: 
            return "iBestaetigen";
        case 76: 
            return "iBestaetigen2";
        case 77: 
            return "iBestaetigt";
        case 78: 
            return "iBestaetigt2";
        case 81: 
            return "iPasswortVergessen";
        case 82: 
            return "iPasswortVergessenQuittung";
        case 83: 
            return "iPWZurueck";
        case 101: 
            return "iLogin";
        case 102: 
            return "iEinstellungen";
        case 103: 
            return "iAuswahl";
        case 104: 
            return "iEinstellungenBestaetigung";
        case 107: 
            return "iMonitoring";
        case 110: 
            return "iAuswahl1";
        case 111: 
            return "iAuswahl1Generalversammlung";
        case 112: 
            return "iAuswahl1Teilnahme";
        case 113: 
            return "iAuswahl1TeilnahmeGast";
        case 114: 
            return "iAuswahl1Dialogveranstaltungen";
        case 115: 
            return "iAuswahl1AnmeldenDialogveranstaltungen";
        case AUSWAHL1_GENERALVERSAMMLUNG_BRIEFWAHL: 
            return "iAuswahl1GeneralversammlungBriefwahl";
        case AUSWAHL1_GENERALVERSAMMLUNG_BEIRATSWAHL: 
            return "iAuswahl1GeneralversammlungBeiratswahl";
       case 120: 
            return "iFehlerView";
       case 121: 
           return "iFehlerViewTechnisch";
        case 150: 
            return "iBesitzVertretungAbfragen";
        case 201:
            return "iPraesenzZugangAbfragePerson";
        case 202:
            return "iPraesenzZugangPersonBestaetigen";
        case 204:
            return "iPraesenzZugangFalschePerson";
        case 205:
            return "iPraesenzAbgangBestaetigung";
        case 206:
            return "iPraesenzZugangAbfrageStornierung";
           
            
        case 301: 
            return "iStimmabgabe";
        case 302: 
            return "iStimmabgabeAuswahl";
        case 303: 
            return "iStimmabgabeQuittung";
        case 304: 
            return "iStimmabgabeDerzeitNichtMoeglich";
        case 401: 
            return "iStreamstart";
            /*Hinweis: iStreamshow ist keine separate Seite,
             * aber in Portaltexten als Seitenname verwendet.
             * iStreamshow ist der Bereich, der in portal.xhtml
             * immer dann direkt angezogen wird,
             * wenn Streaming aktiv ist.
             */
        case 402: 
            return "iUnterlagen";
        case FRAGEN: 
            return "iFragen";
        case 404: 
            return "iWortmeldungen";
        case 405: 
            return "iWidersprueche";
        case 406: 
            return "iTeilnehmerverz";
        case 407: 
            return "iAntraege";
        case 408: 
            return "iSonstigeMitteilungen";
        case 409: 
            return "iAbstimmungserg";
        case 410: 
            return "iNachrichten";
        case BOTSCHAFTEN_EINREICHEN: 
            return "iBotschaftenEinreichen";
        case BOTSCHAFTEN_ANZEIGEN:
            return "iBotschaftenAnzeigen";
        case RUECKFRAGEN:
            return "iRueckfragen";
            
        case 502: 
            return "iWeisung";
        case 503: 
            return "iWeisungBestaetigung";
        case 504: 
            return "iWeisungAendern";
        case 505: 
            return "iWeisungQuittung";
        case 506: 
            return "iWeisungStornieren";
        case 507: 
            return "iWeisungStornierenQuittung";
        case 601: 
            return "iGeneralversammlungAnAbmelden";
        case 602: 
            return "iGeneralversammlungVollmachtStornieren";
        case 605: 
            return "iGeneralversammlungBriefwMitgliedDurchBevollStorno";
        case 1001: 
            return "iNurAnmeldung";
        case 1002: 
            return "iNurAnmeldungQuittung";
        case 1010: 
            return "iEintrittskarte";
        case 1011: 
            return "iEintrittskarteQuittung";
        case 1012: 
            return "iEintrittskarteDetail";
        case 1014: 
            return "iEintrittskarteStornieren";
        case 1016: 
            return "iEintrittskarteStornierenQuittung";

        case 1021: 
            return "iGastkarteUebersicht";
        case 1022: 
            return "iGastkarteEingabe";

        case 2001: 
            return "pLogin";
        case 2002: 
            return "pEinstellungen";
        case 2003: 
            return "pEinstellungenBestaetigung";
        case 2004: 
            return "pAuswahl";
        case 2005: 
            return "pKontaktformular";
        case 2006: 
            return "pPublikationen";
        case 2007: 
            return "pBestand";
        case 2008: 
            return "pAktionaersdaten";
        case 2009: 
            return "pRegistrieren";
        case 2010: 
            return "pRegistrierenQuittung";
        case 2011: 
            return "pPasswortVergessen";
        case 2012: 
            return "pPasswortVergessenQuittung";
        case 2013: 
            return "pEinstellungenAendern";
        case 2014: 
            return "pHVEinladungsversand";
        case 2015: 
            return "pPWZurueck";
        case 2016: 
            return "pBestandHistorie";
        case P_HV_ZUR_HV: 
            return "pHVzurHV";
        case P_BETEILIGUNGSERHOEHUNG: 
            return "pBeteiligungserhoehung";
        case P_UNTERLAGEN: 
            return "pUnterlagen";
        case P_HV_ZUR_BEIRATSWAHL: 
            return "pHVzurBeiratswahl";
        case P_VERANSTALTUNGEN_AUSWAHL: 
            return "pVeranstaltungenAuswahl";
        case P_VERANSTALTUNGEN_DETAIL: 
            return "pVeranstaltungenDetail";
        case ZUORDNUNG_AUFHEBEN:
            return "iZuordnungAufheben";
        case ZUORDNUNG_EINRICHTEN:
            return "iZuordnungEinrichten";
        case ZUORDNUNG_EINRICHTEN_QUITTUNG:
            return "iZuordnungEinrichtenQuittung";

      default:
            return "Fehlt noch";
        }
    }
    
    static public boolean logoutAnzeigen(int nr) {
        switch (nr) {
        case BESTAETIGEN:
            return false;
        case BESTAETIGEN2:
            return false;
        case BESTAETIGT:
            return false;
        case BESTAETIGT2:
            return false;

        case PASSWORT_VERGESSEN:
        case P_PASSWORT_VERGESSEN:
        case P_REGISTRIEREN:
           return false;
        case PASSWORT_VERGESSEN_QUITTUNG:
        case P_PASSWORT_VERGESSEN_QUITTUNG:
        case P_REGISTRIEREN_QUITTUNG:
            return false;
        case PW_ZURUECK:
        case P_PW_ZURUECK:
            return false;

        case LOGIN:
            return false;
            
        case P_LOGIN:
            return false;

        }
        return true;
    }

    static public boolean menueAnzeigen(int nr) {
        switch (nr) {
        case P_LOGIN:
        case P_EINSTELLUNGEN:
        case P_EINSTELLUNGEN_BESTAETIGUNG:
        case P_REGISTRIEREN:
        case P_REGISTRIEREN_QUITTUNG:
        case P_PASSWORT_VERGESSEN:
        case P_PASSWORT_VERGESSEN_QUITTUNG:
        case P_PW_ZURUECK:
            return false;
        
        }
        return true;
    }
    
    static public boolean infoAnzeigen(int nr) {
        switch (nr) {
        case P_LOGIN:
            return false;
        
        }
        return true;
    }

    public final static int FEHLER_HOLEN_AUS_STANDARD = 1;
    public final static int FEHLER_HOLEN_AUS_LOGINLOGOUT = 2;
    public final static int FEHLER_HOLEN_AUS_BESTAETIGEN=3;
    public final static int FEHLER_HOLEN_AUS_PASSWORTVERGESSEN=4;

    static public int fehlertextAus(int nr) {
        switch (nr) {
        case LOGIN:
        case P_LOGIN:
            return FEHLER_HOLEN_AUS_LOGINLOGOUT;
        case BESTAETIGEN:
        case BESTAETIGEN2:
        case BESTAETIGT:
        case BESTAETIGT2:
            return FEHLER_HOLEN_AUS_BESTAETIGEN;
//        case PW_ZURUECK:
//        case P_REGISTRIEREN:
//           return FEHLER_HOLEN_AUS_PASSWORTVERGESSEN;
//        case P_REGISTRIEREN_QUITTUNG:
//            return FEHLER_HOLEN_AUS_PASSWORTVERGESSEN;

//        case P_LOGIN:
//            return FEHLER_HOLEN_AUS_LOGINLOGOUT;

        default:
            return FEHLER_HOLEN_AUS_STANDARD;
        }
    }

    static public boolean aktionaersdatenAnzeigen(int nr) {
        /*Derzeit keine Abweichungen*/
        return logoutAnzeigen(nr);
    }

}
