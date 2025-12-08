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

import de.meetingapps.meetingportal.meetComEclM.EclGastM;

public class WEDetailKarteGetRC extends WERootRC {

    private String ausgewaehlteAktion = "";

    private int artEintrittskarte = 0;
    private String eintrittskarteVersandart = "";
    private String eintrittskarteAbweichendeAdresse1 = "";
    private String eintrittskarteAbweichendeAdresse2 = "";
    private String eintrittskarteAbweichendeAdresse3 = "";
    private String eintrittskarteAbweichendeAdresse4 = "";
    private String eintrittskarteAbweichendeAdresse5 = "";
    private String eintrittskarteEmail = "";

    private String vollmachtName = "";
    private String vollmachtVorname = "";
    private String vollmachtOrt = "";

    private int eintrittskartePdfNr = 0;

    private EclGastM gastM = null;
    private int identMasterGast = 0;
    private boolean gruppenausstellung = false;

    /*********************Standard Getter und Setter****************************************/

    public String getAusgewaehlteAktion() {
        return ausgewaehlteAktion;
    }

    public void setAusgewaehlteAktion(String ausgewaehlteAktion) {
        this.ausgewaehlteAktion = ausgewaehlteAktion;
    }

    public int getArtEintrittskarte() {
        return artEintrittskarte;
    }

    public void setArtEintrittskarte(int artEintrittskarte) {
        this.artEintrittskarte = artEintrittskarte;
    }

    public String getEintrittskarteVersandart() {
        return eintrittskarteVersandart;
    }

    public void setEintrittskarteVersandart(String eintrittskarteVersandart) {
        this.eintrittskarteVersandart = eintrittskarteVersandart;
    }

    public String getEintrittskarteAbweichendeAdresse1() {
        return eintrittskarteAbweichendeAdresse1;
    }

    public void setEintrittskarteAbweichendeAdresse1(String eintrittskarteAbweichendeAdresse1) {
        this.eintrittskarteAbweichendeAdresse1 = eintrittskarteAbweichendeAdresse1;
    }

    public String getEintrittskarteAbweichendeAdresse2() {
        return eintrittskarteAbweichendeAdresse2;
    }

    public void setEintrittskarteAbweichendeAdresse2(String eintrittskarteAbweichendeAdresse2) {
        this.eintrittskarteAbweichendeAdresse2 = eintrittskarteAbweichendeAdresse2;
    }

    public String getEintrittskarteAbweichendeAdresse3() {
        return eintrittskarteAbweichendeAdresse3;
    }

    public void setEintrittskarteAbweichendeAdresse3(String eintrittskarteAbweichendeAdresse3) {
        this.eintrittskarteAbweichendeAdresse3 = eintrittskarteAbweichendeAdresse3;
    }

    public String getEintrittskarteAbweichendeAdresse4() {
        return eintrittskarteAbweichendeAdresse4;
    }

    public void setEintrittskarteAbweichendeAdresse4(String eintrittskarteAbweichendeAdresse4) {
        this.eintrittskarteAbweichendeAdresse4 = eintrittskarteAbweichendeAdresse4;
    }

    public String getEintrittskarteAbweichendeAdresse5() {
        return eintrittskarteAbweichendeAdresse5;
    }

    public void setEintrittskarteAbweichendeAdresse5(String eintrittskarteAbweichendeAdresse5) {
        this.eintrittskarteAbweichendeAdresse5 = eintrittskarteAbweichendeAdresse5;
    }

    public String getEintrittskarteEmail() {
        return eintrittskarteEmail;
    }

    public void setEintrittskarteEmail(String eintrittskarteEmail) {
        this.eintrittskarteEmail = eintrittskarteEmail;
    }

    public String getVollmachtName() {
        return vollmachtName;
    }

    public void setVollmachtName(String vollmachtName) {
        this.vollmachtName = vollmachtName;
    }

    public String getVollmachtVorname() {
        return vollmachtVorname;
    }

    public void setVollmachtVorname(String vollmachtVorname) {
        this.vollmachtVorname = vollmachtVorname;
    }

    public String getVollmachtOrt() {
        return vollmachtOrt;
    }

    public void setVollmachtOrt(String vollmachtOrt) {
        this.vollmachtOrt = vollmachtOrt;
    }

    public int getEintrittskartePdfNr() {
        return eintrittskartePdfNr;
    }

    public void setEintrittskartePdfNr(int eintrittskartePdfNr) {
        this.eintrittskartePdfNr = eintrittskartePdfNr;
    }

    public EclGastM getGastM() {
        return gastM;
    }

    public void setGastM(EclGastM gastM) {
        this.gastM = gastM;
    }

    public int getIdentMasterGast() {
        return identMasterGast;
    }

    public void setIdentMasterGast(int identMasterGast) {
        this.identMasterGast = identMasterGast;
    }

    public boolean isGruppenausstellung() {
        return gruppenausstellung;
    }

    public void setGruppenausstellung(boolean gruppenausstellung) {
        this.gruppenausstellung = gruppenausstellung;
    }

}
