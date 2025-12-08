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
package de.meetingapps.meetingportal.meetComWE;

public class WEPruefeLoginUser extends WERoot {

    /**Für BvUserLogin.pruefeKennung()*/
    public String pKennung = "";
    public String pPasswort = "";
    public boolean pPruefePasswort = false;
    public boolean pNurFuerMandant = false;

    /**Für BvMandanten.pruefeHVVorhanden*/
    public boolean mandantPruefen = true;
    public int pMandant = 0;
    public int pHVJahr = 0;
    public String pHVNummer = "";
    public String pDatenbereich = "";

}
