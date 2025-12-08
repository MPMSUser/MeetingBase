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
package de.meetingapps.meetingclient.meetingClientAllg;

import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComBVerwaltung.BvMandanten;
import de.meetingapps.meetingportal.meetComBVerwaltung.BvUserLogin;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclEmittenten;
import de.meetingapps.meetingportal.meetComEntities.EclUserLogin;
import de.meetingapps.meetingportal.meetComHVParam.ParamS;
import de.meetingapps.meetingportal.meetComStub.WSClient;
import de.meetingapps.meetingportal.meetComWE.WELoginPasswortAendern;
import de.meetingapps.meetingportal.meetComWE.WELoginPasswortAendernRC;
import de.meetingapps.meetingportal.meetComWE.WELoginVerify;
import de.meetingapps.meetingportal.meetComWE.WEPruefeLoginUser;
import de.meetingapps.meetingportal.meetComWE.WEPruefeLoginUserRC;

/**
 * The Class CaPruefeLogin.
 */
public class CaPruefeLogin {

    /** Eingeloggter Benutzer - ohne Passwort. */
    public EclUserLogin rcUserLogin = null;

    /** Ausgewählte HV. */
    public EclEmittenten rcEmittent = null;

    /**
     * Abgeprüft wird: > ist der User bekannt, und darf er sich einloggen? > ist der
     * ausgewählte Mandant angelegt, und darf er verwendet werden?
     * 
     * Achtung: pNurMandantPruefen=true und gleichzeitig
     * pNurKennungPasswortPruefen=true ist sinnlos/unzulässig!
     * 
     * Gefüllt sein müssen folgende globale Variablen:: >
     * ClGlobalVar.datenbankPfadNr, etc. > ClGlobalVar.mandant, ClGlobalVar.hvJahr,
     * ClGlobalVar.hvNummer, ClGlobalVar.datenbereich > SHVParam.paramGeraet
     * 
     * Rückgabewerte (Returncode): afFalscheKennung afPasswortFalsch
     * afKennungGesperrt afNeuesPasswortErforderlich (in diesem Fall ist rcUserLogin
     * gefüllt, bei den anderen Fehlern wg. Kennung nicht - Mandant wird aber auch
     * bei dieser Fehlermeldung nicht überprüft. D.h. erst Passwort ändern, dann neu
     * prüfen)
     * 
     * afHVNichtVorhanden afHVMitDieserKennungNichtZulaessig
     * 
     * 1 = erfolgreich
     * 
     * rcUserLogin = eingeloggter User
     *
     * @param eingabeKennung             the eingabe kennung
     * @param eingabePasswort            the eingabe passwort
     * @param pNurMandantPruefen         the nur mandant pruefen
     * @param pNurKennungPasswortPruefen the nur kennung passwort pruefen
     * @return the int
     */
    public int pruefeLogin(String eingabeKennung, String eingabePasswort, boolean pNurMandantPruefen,
            boolean pNurKennungPasswortPruefen) {
        int rc = 1;
        boolean passwortPruefen = true;
        boolean mandantPruefen = true;

        if (pNurKennungPasswortPruefen) {
            mandantPruefen = false;
        }

        if (pNurMandantPruefen) {
            passwortPruefen = false;
        } else {
            if (!ParamS.paramGeraet.festgelegterBenutzername.isEmpty()) {
                eingabeKennung = ParamS.paramGeraet.festgelegterBenutzername;
                passwortPruefen = false;
            }
        }

        if (ParamS.clGlobalVar.webServicePfadNr >= 0) {
            WELoginVerify weLoginVerify = null;
            WSClient wsClient = new WSClient();
            weLoginVerify = new WELoginVerify();
            WEPruefeLoginUser wePruefeLoginUser = new WEPruefeLoginUser();
            wePruefeLoginUser.weLoginVerify = weLoginVerify;

            /** Für BvUserLogin.pruefeKennung() */
            wePruefeLoginUser.pKennung = eingabeKennung;
            wePruefeLoginUser.pPasswort = eingabePasswort;
            wePruefeLoginUser.pPruefePasswort = passwortPruefen;
            wePruefeLoginUser.pNurFuerMandant = false;

            /** Für BvMandanten.pruefeHVVorhanden */
            wePruefeLoginUser.mandantPruefen = mandantPruefen;
            wePruefeLoginUser.pMandant = ParamS.clGlobalVar.mandant;
            wePruefeLoginUser.pHVJahr = ParamS.clGlobalVar.hvJahr;
            wePruefeLoginUser.pHVNummer = ParamS.clGlobalVar.hvNummer;
            wePruefeLoginUser.pDatenbereich = ParamS.clGlobalVar.datenbereich;

            WEPruefeLoginUserRC wePruefeLoginRC = wsClient.pruefeLoginUser(wePruefeLoginUser);

            rcUserLogin = wePruefeLoginRC.eclUserLogin;
            if (wePruefeLoginRC.rc < 0) {
                return wePruefeLoginRC.rc;
            }

            if (mandantPruefen == true) {
                rcEmittent = wePruefeLoginRC.eclEmittenten;
            }
        } else {
            DbBundle lDbBundle = new DbBundle();
            lDbBundle.openAllOhneParameterCheck();

            /*User prüfen*/
            BvUserLogin bvUserLogin = new BvUserLogin();
            rc = bvUserLogin.pruefeKennung(lDbBundle, eingabeKennung, eingabePasswort, passwortPruefen, false, 2);
            rcUserLogin = bvUserLogin.rcUserLogin;
            if (rc < 0) {
                lDbBundle.closeAll();
                return rc;
            }

            if (mandantPruefen) {
                /*Mandant vorhanden?*/
                BvMandanten bvMandanten = new BvMandanten();
                EclEmittenten eclEmittenten = bvMandanten.pruefeHVVorhanden(lDbBundle, ParamS.clGlobalVar.mandant,
                        ParamS.clGlobalVar.hvJahr, ParamS.clGlobalVar.hvNummer, ParamS.clGlobalVar.datenbereich);
                if (eclEmittenten == null) {
                    lDbBundle.closeAll();
                    return CaFehler.afHVNichtVorhanden;
                }
                if (rcUserLogin.pruefe_userDarfMandantBearbeiten(eclEmittenten) == false) {
                    lDbBundle.closeAll();
                    return CaFehler.afHVMitDieserKennungNichtZulaessig;
                }
                rcEmittent = eclEmittenten;
            }
            lDbBundle.closeAll();
        }

        return rc;
    }

    /**Setzen eines neuen Passworts. Mögliche Fehlermeldungen:
     * Mögliche rc:
     * afFalscheKennung (eigentlich nicht möglich, wenn sauber programmiert wurde und vorher die Kennung verifiziert wurde)
     * afPasswortFalsch - das alte Passwort wurde falsch eingegeben
     * afPasswortZuKurz
     * afPasswortNichtSicher
     * afPasswortBereitsVerwendet - das neue Passwort wurde bereits verwendet
     */
    public int setzeNeuesPasswort(EclUserLogin pUserLogin, String pAltesPasswort, String pNeuesPasswort) {
        int rc = 1;

        if (ParamS.clGlobalVar.webServicePfadNr >= 0) {
            WELoginVerify weLoginVerify = null;
            WSClient wsClient = new WSClient();
            weLoginVerify = new WELoginVerify();
            WELoginPasswortAendern weLoginPasswortAendern = new WELoginPasswortAendern();
            weLoginPasswortAendern.weLoginVerify = weLoginVerify;

            /** Für BvUserLogin.setzeNeuesPasswort() */
            weLoginPasswortAendern.userLogin = pUserLogin;
            weLoginPasswortAendern.altesPasswort = pAltesPasswort;
            weLoginPasswortAendern.neuesPasswort = pNeuesPasswort;

            WELoginPasswortAendernRC weLoginPasswortAendernRC = wsClient.loginPasswortAendern(weLoginPasswortAendern);
            rc = weLoginPasswortAendernRC.rc;
            if (rc < 0) {
                return rc;
            }

            /*eclEmittenten=wePruefeLoginRC.eclEmittenten; - derzeit nicht verwendet, stünde aber zur Verfügung*/
        } else {
            DbBundle lDbBundle = new DbBundle();
            lDbBundle.openAllOhneParameterCheck();

            /*Passwort ändern*/
            BvUserLogin bvUserLogin = new BvUserLogin();
            rc = bvUserLogin.setzeNeuesPasswort(lDbBundle, pUserLogin, pAltesPasswort, pNeuesPasswort);
            lDbBundle.closeAll();
            if (rc < 0) {
                return rc;
            }
        }

        return rc;
    }
}
