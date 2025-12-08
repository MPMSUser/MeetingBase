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

import de.meetingapps.meetingportal.meetComEntities.EclWillenserklaerungStatusNeu;
import de.meetingapps.meetingportal.meetComEntities.EclZugeordneteMeldungNeu;

public class WEWeisungStornierenGet {

    private WELoginVerify weLoginVerify = null;

    /*Beschreibungen siehe ADlgVariablen*/
    private int ausgewaehlteHauptAktion = 0;
    /****************************Ausgewählte Aktion******************************************************
     * 13 = Vollmacht/Weisung SRV stornieren
     * 14 = Briefwahl stornieren
     * 15 = KIAV stornieren ?
     * */
    private int ausgewaehlteAktion = 0;

    private int meldungsIdent = 0;
    private int willenserklaerungIdent = 0;
    private int sammelIdent = 0;

    /*Die folgenden beiden Parameter wurden am 18.02.2017 eingeführt, damit die Meldung "von anderem user verändert"
     * nicht mehr kommt. Muß noch bei App gefüllt werden!
     * TODO #9 App - WeisungStornieren
     */
    private EclZugeordneteMeldungNeu zugeordneteMeldung = null;
    private EclWillenserklaerungStatusNeu eclWillenserklaerungStatus = null;
    
    /*********************Standard Getter und Setter****************************************/

    
    public WELoginVerify getWeLoginVerify() {
        return weLoginVerify;
    }
    public void setWeLoginVerify(WELoginVerify weLoginVerify) {
        this.weLoginVerify = weLoginVerify;
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
    public int getMeldungsIdent() {
        return meldungsIdent;
    }
    public void setMeldungsIdent(int meldungsIdent) {
        this.meldungsIdent = meldungsIdent;
    }
    public int getWillenserklaerungIdent() {
        return willenserklaerungIdent;
    }
    public void setWillenserklaerungIdent(int willenserklaerungIdent) {
        this.willenserklaerungIdent = willenserklaerungIdent;
    }
    public int getSammelIdent() {
        return sammelIdent;
    }
    public void setSammelIdent(int sammelIdent) {
        this.sammelIdent = sammelIdent;
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
