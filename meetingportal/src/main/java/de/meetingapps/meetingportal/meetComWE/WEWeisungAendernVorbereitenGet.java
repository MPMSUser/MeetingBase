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

import de.meetingapps.meetingportal.meetComEclM.EclWillenserklaerungStatusM;
import de.meetingapps.meetingportal.meetComEclM.EclZugeordneteMeldungM;
import de.meetingapps.meetingportal.meetComEntities.EclWillenserklaerungStatusNeu;
import de.meetingapps.meetingportal.meetComEntities.EclZugeordneteMeldungNeu;

/*Wie WEStornierenVorbereitenGet*/

public class WEWeisungAendernVorbereitenGet {

    private WELoginVerify weLoginVerify = null;

    private int ausgewaehlteHauptAktion = 0;
    private int ausgewaehlteAktion = 0;

    private EclZugeordneteMeldungNeu zugeordneteMeldung=null;
    private EclWillenserklaerungStatusNeu eclWillenserklaerungStatus = null;
   
    /*Beschreibungen siehe ADlgVariablen*/
    @Deprecated
    private String ausgewaehlteHauptAktionString = "";
    @Deprecated
    private String ausgewaehlteAktionString = "";

    @Deprecated
    private EclZugeordneteMeldungM zugeordneteMeldungM = null;
    @Deprecated
    private EclWillenserklaerungStatusM eclWillenserklaerungStatusM = null;

    /*********************Standard Getter und Setter****************************************/

    public WELoginVerify getWeLoginVerify() {
        return weLoginVerify;
    }

    public void setWeLoginVerify(WELoginVerify weLoginVerify) {
        this.weLoginVerify = weLoginVerify;
    }

    @Deprecated
   public String getAusgewaehlteHauptAktionString() {
        return ausgewaehlteHauptAktionString;
    }

    @Deprecated
    public void setAusgewaehlteHauptAktionString(String ausgewaehlteHauptAktion) {
        this.ausgewaehlteHauptAktionString = ausgewaehlteHauptAktion;
    }

    @Deprecated
    public String getAusgewaehlteAktionString() {
        return ausgewaehlteAktionString;
    }

    @Deprecated
    public void setAusgewaehlteAktionString(String ausgewaehlteAktion) {
        this.ausgewaehlteAktionString = ausgewaehlteAktion;
    }

    @Deprecated
    public EclZugeordneteMeldungM getZugeordneteMeldungM() {
        return zugeordneteMeldungM;
    }

    @Deprecated
    public void setZugeordneteMeldungM(EclZugeordneteMeldungM zugeordneteMeldungM) {
        this.zugeordneteMeldungM = zugeordneteMeldungM;
    }

    @Deprecated
    public EclWillenserklaerungStatusM getEclWillenserklaerungStatusM() {
        return eclWillenserklaerungStatusM;
    }

    @Deprecated
    public void setEclWillenserklaerungStatusM(EclWillenserklaerungStatusM eclWillenserklaerungStatusM) {
        this.eclWillenserklaerungStatusM = eclWillenserklaerungStatusM;
    }

    public int getAusgewaehlteHauptAktion() {
        return ausgewaehlteHauptAktion;
    }

    public void setAusgewaehlteHauptAktion(int ausgewaehlteHauptAktion) {
        this.ausgewaehlteHauptAktion = ausgewaehlteHauptAktion;
    }

    public int getAusgewaehlteAktion() {
        return ausgewaehlteAktion;
    }

    public void setAusgewaehlteAktion(int ausgewaehlteAktion) {
        this.ausgewaehlteAktion = ausgewaehlteAktion;
    }

    public EclZugeordneteMeldungNeu getZugeordneteMeldung() {
        return zugeordneteMeldung;
    }

    public void setZugeordneteMeldung(EclZugeordneteMeldungNeu zugeordneteMeldung) {
        this.zugeordneteMeldung = zugeordneteMeldung;
    }

    public EclWillenserklaerungStatusNeu getEclWillenserklaerungStatus() {
        return eclWillenserklaerungStatus;
    }

    public void setEclWillenserklaerungStatus(EclWillenserklaerungStatusNeu eclWillenserklaerungStatus) {
        this.eclWillenserklaerungStatus = eclWillenserklaerungStatus;
    }

}
