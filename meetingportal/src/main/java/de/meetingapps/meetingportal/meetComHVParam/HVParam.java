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
package de.meetingapps.meetingportal.meetComHVParam;

import java.io.Serializable;

public class HVParam implements Serializable {
    private static final long serialVersionUID = -3033432545835574028L;

    /****TEIL 0 - für Reload****/
    /**Wird von bvReload mitgeliefert und verwendet - nicht in Datenbank. reloadParameter*/
    public int reloadVersionParameter = 0;

    /**Wird von bvReload mitgeliefert und verwendet - nicht in Datenbank. reloadParameterLfd*/
    public int reloadVersionFehlermeldungen = 0;

    /****Teil A - diese Felder werden redundant zu ClGlobalVar belegt. Notwendig für die Listen
     * in der Puffer-Verwaltung.
     */
    public int mandant = 0;
    public int hvJahr = 0;
    public String hvNummer = "";
    public String datenbereich = "";

    /******Teil B*******/
    public ParamModuleKonfigurierbar paramModuleKonfigurierbar = null;
    public ParamBasis paramBasis = null;
    public ParamNummernformen paramNummernformen = null;
    public ParamNummernkreise paramNummernkreise = null;
    public ParamPruefzahlen paramPruefzahlen = null;

    public ParamWillenserklaerungen paramWillenserklaerungen=null;
    public ParamAkkreditierung paramAkkreditierung = null;
    public ParamPraesenzliste paramPraesenzliste = null;
    public ParamAbstimmung paramAbstimmung = null;
    public ParamAbstimmungParameter paramAbstimmungParameter = null;

    public ParamPortal paramPortal = null;
    public ParamPortalServer paramPortalServer = null;

    public ParamAppServer paramAppServer = null;

    public ParamGaesteModul paramGaesteModul = null;

    public ParamBestandsverwaltung paramBestandsverwaltung = null;

    public HVParam() {
        /*TODO _Parameter - ParamPortal noch wie Rest handhaben. Stand: hier jetzt komplett einheitlich aufgebaut. In aufrufenden Instanzen noch zu überprüfen.*/
        paramModuleKonfigurierbar = new ParamModuleKonfigurierbar();
        paramBasis = new ParamBasis();
        paramNummernformen = new ParamNummernformen();
        paramNummernkreise = new ParamNummernkreise();
        paramPruefzahlen = new ParamPruefzahlen();
        paramWillenserklaerungen=new ParamWillenserklaerungen();
        paramAkkreditierung = new ParamAkkreditierung();
        paramPraesenzliste = new ParamPraesenzliste();
        paramAbstimmung = new ParamAbstimmung();
        paramAbstimmungParameter = new ParamAbstimmungParameter();
        paramPortal = new ParamPortal();
        paramPortalServer = new ParamPortalServer();
        paramAppServer = new ParamAppServer();
        paramGaesteModul = new ParamGaesteModul();
        paramBestandsverwaltung = new ParamBestandsverwaltung();
    }

    public int liefereBriefwahlAusgebenInAbstimmungsergebnis() {
        if (paramAbstimmungParameter.briefwahlAusgebenInAbstimmungsergebnis==0) {
            return 0;
        }
        if (paramModuleKonfigurierbar.briefwahl==false) {
            return 0;
        }
        return 1;
    }
    
}
