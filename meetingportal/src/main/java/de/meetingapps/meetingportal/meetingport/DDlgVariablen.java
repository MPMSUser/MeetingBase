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
package de.meetingapps.meetingportal.meetingport;

import java.io.Serializable;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

@SessionScoped
@Named
public class DDlgVariablen implements Serializable {
    private static final long serialVersionUID = 6183611722011831894L;

    /******Mandante und Sprache***************************/
    private String sprache = "";
    private String test = ""; /*1 => Testmodus ist an*/

    private int eingabeQuelle = 51;
    /*
    .pErteiltAufWeg=aDlgVariablen.getEingabeQuelle();
    */

    public String getSprache() {
        return sprache;
    }

    public void setSprache(String sprache) {
        if (sprache.compareTo("DE") != 0 && sprache.compareTo("EN") != 0) {
            sprache = "DE";
        }
        this.sprache = sprache;
    }

    /*******************************Fehlermeldung******************************************************/
    private String fehlerMeldung = "";
    private int fehlerNr = 1;

    public void clearFehlerMeldung() {
        fehlerMeldung = "";
        fehlerNr = 1;
    }

    public String getFehlerMeldung() {
        return fehlerMeldung;
    }

    public void setFehlerMeldung(String fehlerMeldung) {
        this.fehlerMeldung = fehlerMeldung;
    }

    /***************************Login-Daten**********************************************************/
    private String loginKennung = "";
    private String loginPasswort = "";

    public void clearLogin() {
        loginKennung = "";
        loginPasswort = "";
    }

    public String getLoginKennung() {
        return loginKennung;
    }

    public void setLoginKennung(String loginKennung) {
        this.loginKennung = loginKennung;
    }

    public String getLoginPasswort() {
        return loginPasswort;
    }

    public void setLoginPasswort(String loginPasswort) {
        this.loginPasswort = loginPasswort;
    }

    /*********************Zurücksetzen aller Variablen (mit Ausnahme von Login)**********************/
    public void clearStart() {

        fehlerMeldung = "";

    }

    /*****************Zurücksetzen aller Variablen, die im Laufe der Dialog verwendet werden****************/
    public void clearDlgVariablen() {

        fehlerMeldung = "";

    }

    public int getFehlerNr() {
        return fehlerNr;
    }

    public void setFehlerNr(int fehlerNr) {
        this.fehlerNr = fehlerNr;
    }

    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }

}