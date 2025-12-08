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

/**Sammelkartenart Ist (siehe EclMeldung, aber auch an aneren Stellen*/
public class KonstSkIst {

    public final static int unbekannt = 0;
    public final static int kiav = 1;
    public final static int srv = 2;
    public final static int organisatorisch = 3;
    public final static int briefwahl = 4;
    public final static int dauervollmacht = 5;

    /**Die folgenden Werte werden nicht in EclMeldung verwendet,
     * sondern zur Definition der gerade verwendeten Funktion*/
    public final static int srvHV = 101;
    
    
    /*TODO - ab hier aufräumen, das gehört nicht zu SkIst. Wird aber verwendet!
     * Muß in vollkommener geistiger Umnachtung hier reingerutscht sein ...*/ 
    public final static int FRAGEN=201;
    public final static int ANTRAEGE=202;
    public final static int WIDERSPRUECHE=203;
    public final static int WORTMELDUNGEN=204;
    public final static int SONSTMITTEILUNGEN=205;
    public final static int BOTSCHAFTEN_EINREICHEN=206;

    static public String getText(int nr) {
        switch (nr) {
        case 0: {
            return "";
        }
        case 1: {
            return "KIAV";
        }
        case 2: {
            return "SRV";
        }
        case 3: {
            return "organisatorisch";
        }
        case 4: {
            return "Briefwahl";
        }
        case 5: {
            return "Dauervollmacht";
        }
        case 101: {
            return "SRV HV";
        }
        }

        return "";

    }
    
    static public int liefereSkIstZuportalFunktion(int pPortalFunktion) {
        switch (pPortalFunktion) {
        case KonstPortalFunktionen.fragen:return FRAGEN;
        case KonstPortalFunktionen.rueckfragen:return FRAGEN;
        case KonstPortalFunktionen.antraege:return ANTRAEGE;
        case KonstPortalFunktionen.widersprueche:return WIDERSPRUECHE;
        case KonstPortalFunktionen.wortmeldungen:return WORTMELDUNGEN;
        case KonstPortalFunktionen.sonstigeMitteilungen:return SONSTMITTEILUNGEN;
        case KonstPortalFunktionen.botschaftenEinreichen:return BOTSCHAFTEN_EINREICHEN;
        }
        return 0;
    }
}
