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

import de.meetingapps.meetingportal.meetComEclM.EclAbstimmungenListeM;
import de.meetingapps.meetingportal.meetComEclM.EclKIAVM;

public class WEStornierenVorbereitenGetRC extends WERootRC {

    private int ausgewaehlteAktion = 0;

    @Deprecated
    private String ausgewaehlteAktionString = "";

    private int meldungsIdent = 0;
    private int weisungenSind = 0;
    private int willenserklaerungIdent = 0;
    private int sammelIdent = 0;
    private int weisungsIdent = 0;

 
    private EclAbstimmungenListeM abstimmungenListeM = null;
    private EclKIAVM kiavM = null;

    /******************************Standard Getter/Setter*************************************/

    @Deprecated
   public String getAusgewaehlteAktionString() {
        return ausgewaehlteAktionString;
    }

    @Deprecated
    public void setAusgewaehlteAktionString(String ausgewaehlteAktionString) {
        this.ausgewaehlteAktionString = ausgewaehlteAktionString;
    }

    public int getWeisungenSind() {
        return weisungenSind;
    }

    public void setWeisungenSind(int weisungenSind) {
        this.weisungenSind = weisungenSind;
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

    public int getWeisungsIdent() {
        return weisungsIdent;
    }

    public void setWeisungsIdent(int weisungsIdent) {
        this.weisungsIdent = weisungsIdent;
    }


    public EclAbstimmungenListeM getAbstimmungenListeM() {
        return abstimmungenListeM;
    }

    public void setAbstimmungenListeM(EclAbstimmungenListeM abstimmungenListeM) {
        this.abstimmungenListeM = abstimmungenListeM;
    }

    public EclKIAVM getKiavM() {
        return kiavM;
    }

    public void setKiavM(EclKIAVM kiavM) {
        this.kiavM = kiavM;
    }

    public int getMeldungsIdent() {
        return meldungsIdent;
    }

    public void setMeldungsIdent(int meldungsIdent) {
        this.meldungsIdent = meldungsIdent;
    }

    public int getAusgewaehlteAktion() {
        return ausgewaehlteAktion;
    }

    public void setAusgewaehlteAktion(int ausgewaehlteAktion) {
        this.ausgewaehlteAktion = ausgewaehlteAktion;
    }

}
