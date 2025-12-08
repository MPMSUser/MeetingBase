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

/**Separierte Klasse, da hier weitere Willenserklärungsvarianten erforderlich sind.
 * 
 * Über diese Klasse werden die Portal-Text-Nummern definiert, die in iStatus angezeigt werden.
 */
public class KonstWillenserklaerungPortalTexte {

    /*Undefiniert (0)*/
    public final static int undefiniert = 0;

    /*+++++++++++++++++neueZutrittsIdentZuMeldung, zuZutrittsIdentNeuesDokument++++++++++++++++++++++++++++++++++++++++*/
    /**TextNr1
     * bezeichnungEintrittskarteDE=Eintrittskarte Nr. / bezeichnungEintrittskarteEN=Admission ticket
     * Danach EK-Nummer<br> name<br> vorname, ort<br>*/
    public final static int neueZutrittsIdentZuMeldung_zuZutrittsIdentNeuesDokument_Aktionaer = 1418;

    /**TextNr1 
     * Gastkarte Nr. / Guest ticket.
     * Danach EK-Nummer<br> name, vorname<br> ort<br>*/
    public final static int neueZutrittsIdentZuMeldung_zuZutrittsIdentNeuesDokument_Gast = 930;

    /**TextNr2
     * Versandadresse: / Postal Adress:
     * Danach bis zu 5 Zeilen Versandadresse*/
    public final static int neueZutrittsIdentZuMeldung_zuZutrittsIdentNeuesDokument_Postversand = 931;

    /**TextNr2
     * Portal: Online-Ausdruck / Online document; App: App / App (hat wohl so nicht funktioniert - nochmal klären wies ist)*/
    public final static int neueZutrittsIdentZuMeldung_zuZutrittsIdentNeuesDokument_OnlineTicket = 932;

    /**TextNr2
     * Versand per E-Mail: / Send by E-Mail:
     * Danach: Mail-Adresse
     */
    public final static int neueZutrittsIdentZuMeldung_zuZutrittsIdentNeuesDokument_Mail = 933;

    /**TextNr2
     * In App gespeichert / Stored in App"
     */
    public final static int neueZutrittsIdentZuMeldung_zuZutrittsIdentNeuesDokument_App = 934;

    /*+++++++++++++++++++vollmacht an Dritte+++++++++++++++++++++++++++++++++*/
    /**TextNr1
     * mit Vollmacht an: / with proxy to:
     * Danach name, vorname<br> ort<br>*/
    public final static int vollmachtAnDritte_zuEK = 935;
    
    /**
     * Vollmachtgeber:
     * Danach name, vorname<br> ort<br>*/
    public final static int vollmachtAnDritte_zuEK_Vollmachtgeber = 1500;

    /**TextNr1
     * Vollmacht an: / Proxy to:
     * Danach name, vorname<br> ort<br>*/
    public final static int vollmachtAnDritte_Alleine = 936;

    /*+++++++++++++++++++vollmacht und Weisung an SRV+++++++++++++++++++++++++++++++++*/
    /**TextNr1
     * Vollmacht und Weisung an Stimmrechtsvertreter / Proxy and instructions to the proxy of the company*/
    public final static int vollmachtUndWeisungAnSRV = 937;

    /*+++++++++++++++++++vollmacht und Weisung an KIAV+++++++++++++++++++++++++++++++++*/
    /**TextNr1
     * Vollmacht an Kreditinstitut/Aktionärsvereinigung: / Proxy to bank / shareholders union:*/
    public final static int vollmachtUndWeisungAnKIAV_nurVollmacht = 938;

    /**TextNr1
     * Vollmacht und Weisung an Kreditinstitut/Aktionärsvereinigung: / Proxy and instructions to bank / shareholders union:*/
    public final static int vollmachtUndWeisungAnKIAV_VollmachtUndWeisung = 939;

    /**TextNr1
     * Vollmacht und Weisung gemäß Vorschlag an Kreditinstitut/Aktionärsvereiniung: / Proxy and instructions according their opinion to bank / shareholders union:*/
    public final static int vollmachtUndWeisungAnKIAV_VollmachtUndWeisungGemaessVorschlag = 940;

    /**TextNr1
     * Vollmacht (ohne Weisung) an Kreditinstitut/Aktionärsvereiniung: / Proxy (without instructions) to bank / shareholders union:*/
    public final static int vollmachtUndWeisungAnKIAV_VollmachtOhneWeisung = 941;

    /*+++++++++++++++++++Dauervollmacht an KIAV+++++++++++++++++++++++++++++++++*/
    /**TextNr1
     * Dauervollmacht an Kreditinstitut: / Proxy to bank / shareholders union:*/
    public final static int dauervollmachtAnKIAV_nurVollmacht = 942;

    /**TextNr1
     * Dauervollmacht und Weisung an Kreditinstitut: / Proxy and instructions to bank / shareholders union:*/
    public final static int dauervollmachtAnKIAV_VollmachtUndWeisung = 943;

    /**TextNr1
     * Dauervollmacht und Weisung gemäß Vorschlag an Kreditinstitut: / Proxy and instructions according their opinion to bank / shareholders union:*/
    public final static int dauervollmachtAnKIAV_VollmachtUndWeisungGemaessVorschlag = 944;

    /**TextNr1
     * Dauervollmacht (ohne Weisung) an Kreditinstitut: / Proxy (without instructions) to bank / shareholders union:*/
    public final static int dauervollmachtAnKIAV_VollmachtOhneWeisung = 945;

    /*+++++++++++++++++++Organisatorische mit Weisung+++++++++++++++++++++++++++++++++*/
    /**TextNr1
     * Organisatorisch in Sammelkarte: / Proxy to bank / shareholders union:*/
    public final static int organisatorisch_nurVollmacht = 946;

    /**TextNr1
     * Organisatorisch mit Weisung in Sammelkarte: / Proxy and instructions to bank / shareholders union:*/
    public final static int organisatorisch_VollmachtUndWeisung = 947;

    /**TextNr1
     * Organisatorisch mit Weisung gemäß Vorschlag in Sammelkarte: / Proxy and instructions according their opinion to bank / shareholders union:*/
    public final static int organisatorisch_VollmachtUndWeisungGemaessVorschlag = 948;

    /**TextNr1
     * Organisatorisch (ohne Weisung) in Sammelkarte: / Proxy (without instructions) to bank / shareholders union:*/
    public final static int organisatorisch_VollmachtOhneWeisung = 949;

    /*+++++++++++++++++++Briefwahl+++++++++++++++++++++++++++++++++*/
    /**TextNr1
     * Stimmabgabe per Briefwahl (ku178 178: Stimmabgabe) / Postal voting (ku152: Absentee voting)*/
    public final static int briefwahl = 950;

    /*+++++++++++++++++++Ändern Weisung++++++++++++++++++++++++++++++++*/
    /**TextNr1
     * Änderung Weisung/Stimmabgabe / Change of voting*/
    public final static int aendernWeisung = 951;

    /*+++++++++++++++++++Widerruf Vollmacht/Weisung Sammelkarte++++++++++++++++++++++++++++++++*/
    /**TextNr1
     * Stornierung Weisung/Stimmabgabe / Delete of voting*/
    public final static int widerrufWeisung = 952;

    /*+++++++++++++++++++Sperren ZutrittsIdent++++++++++++++++++++++++++++++++*/
    /**TextNr1
     * Stornierung Eintrittskarte*/
    public final static int sperrenZutrittsIdent = 953;

    /*+++++++++++++++++++Sperren Vollmacht Dritte++++++++++++++++++++++++++++++++*/
    /**TextNr1
     * Stornierung Vollmacht/Dritte / Delete of Proxy*/
    public final static int widerrufVollmachtAnDritte = 954;

    /*+++++++++++++++++++Sonstiges++++++++++++++++++++++++++++++++*/
    /**TextNr1
     * */
    public final static int sonstiges = 955;

}
