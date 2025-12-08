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

import de.meetingapps.meetingportal.meetComEntities.EclWillenserklaerung;

public class KonstWillenserklaerung {

    /*Undefiniert (0)*/
    public final static int undefiniert = 0;

    @Deprecated
    public final static int wiederzugangAlsGast = 5;
    @Deprecated
    public final static int abgangAlsGast = 6;

    /*Anmeldungen (10-)*/
    public final static int anmeldungAusAktienregister = 10;
    public final static int anmeldungGast = 11;
    public final static int stornoAnmeldungAusAktienregister = 12;
    public final static int veraenderungAktienbestandAktienregister = 13;

    /*ZutrittsIdent (100-)*/
    public final static int neueZutrittsIdentZuMeldung = 100;
    public final static int neueZutrittsIdentZuVertreter = 101; //Fehlt noch
    public final static int sperrenZutrittsIdent = 102;
    public final static int freigebenGesperrteZutrittsIdent = 103;
    public final static int zuZutrittsIdentNeuesDokument = 104;

    /*Stimmkarten (150-)*/
    public final static int neueStimmkarteZuMeldung = 150;
    public final static int neueStimmkarteZuVertreter = 151; //Fehlt noch
    public final static int sperrenStimmkarte = 152;
    public final static int freigebenGesperrteStimmkarte = 153;

    /*StimmkartenSecond (200-)*/
    public final static int neueStimmkarteSecondZuMeldung = 200;
    public final static int neueStimmkarteSecondZuVertreter = 201; //Fehlt noch
    public final static int sperrenStimmkarteSecond = 202;
    public final static int freigebenGesperrteStimmkarteSecond = 203;

    /*Vollmacht an Dritte (300-)*/
    public final static int vollmachtAnDritte = 300;
    public final static int widerrufVollmachtAnDritte = 301;

    /*Vollmacht / Weisung / Briefwahl / KIAV/ etc. (350-)*/
    public final static int vollmachtUndWeisungAnSRV = 350;
    public final static int widerrufVollmachtUndWeisungAnSRV = 351;
    public final static int aendernWeisungAnSRV = 352;
    //	public final static int vollmachtAnKIAV=353;		nicht verwenden!
    //	public final static int widerrufVollmachtAnKIAV=354; 	nicht verwenden!
    public final static int vollmachtUndWeisungAnKIAV = 355;
    public final static int widerrufVollmachtUndWeisungAnKIAV = 356;
    public final static int aendernWeisungAnKIAV = 357;
    public final static int dauervollmachtAnKIAV = 358;
    public final static int widerrufDauervollmachtAnKIAV = 359;
    public final static int briefwahl = 360;
    public final static int widerrufBriefwahl = 361;
    public final static int aendernBriefwahl = 362;
    //	public final static int organisatorischInSammelkarte=363;	nicht verwenden!
    //	public final static int widerrufOrganisatorischInSammelkarte=364;	nicht verwenden!
    public final static int organisatorischMitWeisungInSammelkarte = 365;
    public final static int widerrufOrganisatorischMitWeisungInSammelkarte = 366;
    public final static int aendernWeisungOrganisatorischInSammelkarte = 367;
    public final static int aendernWeisungDauervollmachtAnKIAV = 368;

    /*Präsenzänderungen (1000-)*/
    /*Achtung - wenn hier Willenserklärungen für Präsenzänderungen ergänzt werden, dann unbedingt
     * Präsenzfeststellung etc. ergänzen! =>
     * 	DbJoined.feststellenPraesenz
     */
    public final static int zugangGast = 1000;
    public final static int abgangGast = 1001;

    /*erstzugang - siehe 21000*/
    /*abgang - siehe 21008*/
    public final static int wiederzugang = 1002;
    /*vertreterwechsel siehe 21009*/
    public final static int abgangAusSRV = 1003;
    public final static int abgangAusOrga = 1004;
    public final static int abgangAusDauervollmacht = 1005;
    public final static int abgangAusKIAV = 1006;
    /*1007 reserviert für Briefwahl, für den Fall dass das doch irgendwann noch gebraucht wird*/

    public final static int zugangInSRV = 1013;
    public final static int zugangInOrga = 1014;
    public final static int zugangInDauervollmacht = 1015;
    public final static int zugangInKIAV = 1016;
    /*1017 reserviert für Briefwahl, für den Fall dass das doch irgendwann noch gebraucht wird*/

    public final static int wechselInSRV = 1023;
    public final static int wechselInOrga = 1024;
    public final static int wechselInDauervollmacht = 1025;
    public final static int wechselInKIAV = 1026;
    /*1017 reserviert für Briefwahl, für den Fall dass das doch irgendwann noch gebraucht wird*/

    /*Kombi-Willenserklärungen (10000-)
     * Diese kommen direkt in den Tables und in der Logik nicht vor, da dort als zwei (oder mehr?) Willenserklärungen
     * abgebildet. Jedoch nötig für Anzeige gegenüber Aktionär*/
    public final static int neueZutrittsIdentZuMeldung_VollmachtAnDritte = 10000;

    /*High-Level-Aktionen, die gegenüber dem Aktionär ausgeführt werden müssen. Siehe z.B. BlPraesenz.
     * Nicht mehr in EnWillenserklaerung vorhanden 
     */
    public final static int hinweisSammelkartenZuordnungVorhanden = 20000;
    public final static int sammelkartenZuordnungMussWiderrufenWerden = 20001;
    public final static int sammelkartenZuordnungMussDeaktiviertWerden = 20002;
    public final static int vollmachtenMuessenGgfAutomatischWiderrufenWerden = 20003;
    public final static int vollmachtenMuessenGgfInTextformWiderrufenWerden = 20004;

    /*++++++++++++++++++++++High-Level-Aktionen, die gegenüber dem Aktionär ausgeführt werden können. Siehe z.B. BlPraesenz.+++++++++++
     * Nicht mehr in EnWillenserklaerung vorhanden
     */
    /**Normales Prozedere Erstzugang - jeder beliebige Bevollmächtigte / Aktionär kann dies nutzen*/
    public final static int erstzugang = 21000; //1:1, beliebig

    /**Sowohl die Meldumg, als auch das verwendete Identifikationsmedium war schon mal da. Insofern kann ein Wiederzugang 
     * mit dieser Identifikation nur mit der selben Person (die dies bereits benutzt hatte) erfolgen. 
     * 
     */
    public final static int wiederzugang_nurSelbePerson = 21001; //nur 1:1

    /**Die Meldung war zwar schonmal präsent, aber das verwendete Identifikationsmedium noch nicht. Deshalb Frage auch beim
     * Wiederzugang erforderlich. 
     */
    public final static int wiederzugang_beliebigePerson = 21002; //1:1, beliebig

    /**Bei Tausch beliebig: Zugang mit zutrittsIdent, die bereits verwendet wurde. Nur zulässig,
     * wenn ursprüngliche Person oder Dritter mit ZutrittsIdent zugeht => bestätigen lassen, dass Stimmmaterial noch im Besitz ist
     * (siehe auch Beschreibung zu wiederzugangOhneFrage_SindSie)
     */
    /* TODO $Deprecated*/
    @Deprecated
    public final static int wiederZugangFallsStimmaterialVorhanden = 21003; //nur beliebig

    /**Bei Tausch beliebig: Zugang mit zutrittsIdent, die bereits verwendet wurde - als Alternative zu 
     * wiederZugangFallsStimmaterialVorhanden. Siehe auch Beschreibung von wiederzugangOhneFrage_SindSie
     */
    /* TODO $Deprecated*/
    @Deprecated
    public final static int sonderschalterZutrittsIdentBereitsVerwendetNeuesStimmaterial = 21004; //nur beliebig

    /**Es geht eine Person zu, der die Vollmacht mittlerweile entzogen wurde. Diese Person kann mit dieser ZutrittsIdent zugehen, wenn für sie
     * eine Gastmeldung angelegt wird und dieser Karte zugeordnet wird. I.d.R. in Zusammenhang mit 
     */
    /* TODO $Deprecated*/
    @Deprecated
    public final static int gastAnlegenUndZugangAlsGast = 21005; //1:1, beliebig

    /**Dieser Person wurde mittlerweile die Vollmacht entzogen. Sie kann nur zugehen, wenn sie eine neue Vollmacht vorlegt. 
     * Überprüfen, inwieweit selbe PersonNatJur. Ek ggf. auf andere PersonNatJur übertragen
     */
    public final static int wiederzugang_nurSelbePersonMitNeuerVollmacht = 21006; //1:1, beliebig => Sonderschalter

    /**Nicht tauschbar! Vollmacht entzogen, EK -> EK ist nicht wiederverwendbar mit anderer Vollmacht*/
    /* TODO $Deprecated*/
    @Deprecated
    public final static int wiederzugangNurMitVorgelegterIdentischerVollmacht = 21014; //1:1, beliebig => Sonderschalter

    /**Es wurde eine Stimmkarte(second) eingelesen, die noch nicht einer Meldung zugeordnet ist. => erst ZutrittsIdent einlesen;
     * oder aber: Stimmkarte wurde gesperrt => Sonderschalter
     */
    public final static int erstZutrittsIdentEinlesenOderKarteGesperrt = 21007; //beliebig

    public final static int abgang = 21008;
    public final static int vertreterwechsel = 21009;
    public final static int verlassenUndVollmachtUndWeisungAnSRV = 21010;
    public final static int verlassenUndVollmachtUndWeisungAnKIAV = 21011;
    public final static int stimmkartenwechsel = 21012;
    public final static int stimmkartenwechselNachAbstimmung = 21013;

    /*21014 verwendet - siehe wiederzugangNurMitVorgelegterIdentischerVollmacht*/

    public final static int wiederzugangGast_nurSelbePerson = 21015;

    public final static int wechselAktionaerZuGast = 21016;
    public final static int wechselGastZuAktionaer = 21017;

    /**"High-Level-Willenserklärung" auf "Input-Ebene" (z.B. Scan-Lauf-Verarbeitung)*/
    public final static int eineEKSelbst = 22001;
    public final static int eineEKVollmacht = 22002;
    public final static int zweiEKSelbst = 22003;

    /********************************"Virtuelle Zuschaltung"************************/
    
    public final static int virtZugangSelbst=23001;
    public final static int virtAbgang=23002;
    public final static int virtZugangVollmacht=23003;
    public final static int virtAbgangVollmacht=23004;
    public final static int virtWiederzugangSelbst=23005;
    public final static int virtWiederzugangVollmacht=23006;
   
    
    /*
    case 1: return (Erstzugang);
    case 2: return (Wiederzugang);
    case 3: return (Abgang);
    
    case 4: return (ErstzugangAlsGast);
    case 5: return (WiederzugangAlsGast);
    case 6: return (AbgangAlsGast);
    
    case 7: return (Vertreterwechsel);
    case 8: return (Stimmkartenwechsel);
    case 9: return (StimmkartenwechselSecond);
    case 10: return (VertreterwechselWiderruf);
    case 99: return (Storno);
    */

    static public String getText(int nr) {
        switch (nr) {
        case 0: {
            return "undefiniert";
        }

        case 5: {
            return "wiederzugangAlsGast";
        }
        case 6: {
            return "abgangAlsGast";
        }

        case 10: {
            return "anmeldungAusAktienregister";
        }
        case 11: {
            return "anmeldungGast";
        }
        case 12: {
            return "stornoAnmeldungAusAktienregister";
        }
        case 13: {
            return "veraenderungAktienbestandAktienregister";
        }

        case 100: {
            return "neueZutrittsIdentZuMeldung";
        }
        case 101: {
            return "neueZutrittsIdentZuVertreter";
        }
        case 102: {
            return "sperrenZutrittsIdent";
        }
        case 103: {
            return "freigebenGesperrteZutrittsIdent";
        }
        case 104: {
            return "zuZutrittsIdentNeuesDokument";
        }

        case 150: {
            return "neueStimmkarteZuMeldung";
        }
        case 151: {
            return "neueStimmkarteZuVertreter";
        }
        case 152: {
            return "sperrenStimmkarte";
        }
        case 153: {
            return "freigebenGesperrteStimmkarte";
        }

        case 200: {
            return "neueStimmkarteSecondZuMeldung";
        }
        case 201: {
            return "neueStimmkarteSecondZuVertreter";
        }
        case 202: {
            return "sperrenStimmkarteSecond";
        }
        case 203: {
            return "PersonenfreigebenGesperrteStimmkarteSecondIdent";
        }

        case 300: {
            return "vollmachtAnDritte";
        }
        case 301: {
            return "widerrufVollmachtAnDritte";
        }

        case 350: {
            return "vollmachtUndWeisungAnSRV";
        }
        case 351: {
            return "widerrufVollmachtUndWeisungAnSRV";
        }
        case 352: {
            return "aendernWeisungAnSRV";
        }
        case 355: {
            return "vollmachtUndWeisungAnKIAV";
        }
        case 356: {
            return "widerrufVollmachtUndWeisungAnKIAV";
        }
        case 357: {
            return "aendernWeisungAnKIAV";
        }
        case 358: {
            return "dauervollmachtAnKIAV";
        }
        case 359: {
            return "widerrufDauervollmachtAnKIAV";
        }
        case 360: {
            return "briefwahl";
        }
        case 361: {
            return "widerrufBriefwahl";
        }
        case 362: {
            return "aendernBriefwahl";
        }
        case 365: {
            return "organisatorischMitWeisungInSammelkarte";
        }
        case 366: {
            return "widerrufOrganisatorischMitWeisungInSammelkarte";
        }
        case 367: {
            return "aendernWeisungOrganisatorischInSammelkarte";
        }
        case 368: {
            return "aendernWeisungDauervollmachtAnKIAV";
        }

        case 1000: {
            return "zugangGast";
        }
        case 1001: {
            return "abgangGast";
        }
        case 1002: {
            return "wiederzugang";
        }

        case 1003: {
            return "abgangAusSRV";
        }
        case 1004: {
            return "abgangAusOrga";
        }
        case 1005: {
            return "abgangAusDauervollmacht";
        }
        case 1006: {
            return "abgangAusKIAV";
        }

        case 1013: {
            return "zugangInSRV";
        }
        case 1014: {
            return "zugangInOrga";
        }
        case 1015: {
            return "zugangInDauervollmacht";
        }
        case 1016: {
            return "zugangInKIAV";
        }

        case 1023: {
            return "wechselInSRV";
        }
        case 1024: {
            return "wechselInOrga";
        }
        case 1025: {
            return "wechselInDauervollmacht";
        }
        case 1026: {
            return "wechselInKIAV";
        }

        case 10000: {
            return "neueZutrittsIdentZuMeldung_VollmachtAnDritte";
        }

        case 20000: {
            return "hinweisSammelkartenZuordnungVorhanden";
        }
        case 20001: {
            return "sammelkartenZuordnungMussWiderrufenWerden";
        }
        case 20002: {
            return "sammelkartenZuordnungMussDeaktiviertWerden";
        }
        case 20003: {
            return "vollmachtenMuessenGgfAutomatischWiderrufenWerden";
        }
        case 20004: {
            return "vollmachtenMuessenGgfInTextformWiderrufenWerden";
        }

        case 21000: {
            return "erstzugang";
        }
        case 21001: {
            return "wiederzugangOhneFrage_SindSie";
        }
        case 21002: {
            return "wiederzugangMitFrage_SindSie";
        }
        case 21003: {
            return "wiederZugangFallsStimmaterialVorhanden";
        }
        case 21004: {
            return "sonderschalterZutrittsIdentBereitsVerwendetNeuesStimmaterial";
        }
        case 21005: {
            return "gastAnlegenUndZugangAlsGast";
        }
        case 21006: {
            return "wiederzugangNurMitVorgelegterVollmacht";
        }
        case 21007: {
            return "erstZutrittsIdentEinlesenOderKarteGesperrt";
        }
        case 21008: {
            return "abgang";
        }
        case 21009: {
            return "vertreterwechsel";
        }
        case 21010: {
            return "verlassenUndVollmachtUndWeisungAnSRV";
        }
        case 21011: {
            return "verlassenUndVollmachtUndWeisungAnKIAV";
        }
        case 21012: {
            return "stimmkartenwechsel";
        }
        case 21013: {
            return "stimmkartenwechselNachAbstimmung";
        }
        case 21015: {
            return "wiederzugangGast_nurSelbePerson";
        }
        case 21016: {
            return "wechselAktionaerZuGast";
        }
        case 21017: {
            return "wechselGastZuAktionaer";
        }

        }

        return "";

    }

    /**Liefert den String, der in einem Select nach "IN" verwendet werden kann,
     * um alle Präsenzbuchungen zu filtern
     */
    static public String getInSelectPraesenzbuchungen() {
        String ergebnis = "(";
        ergebnis = ergebnis + erstzugang + "," + abgang + "," + wiederzugang;

        ergebnis = ergebnis + ")";
        return ergebnis;
    }

    public static int fromClWillenserklaerung(EclWillenserklaerung veraenderung) {
        return (veraenderung.willenserklaerung);
    }

}
