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
import de.meetingapps.meetingportal.meetComEntities.EclWeisungMeldung;
import de.meetingapps.meetingportal.meetComEntities.EclWeisungMeldungRaw;

/*Abgeleitet von WEStornierenVorbereitenGetRC*/

public class WEWeisungAendernVorbereitenGetRC extends WERootRC {

    private int meldungsIdent = 0;
    private int willenserklaerungIdent = 0;
    private int sammelIdent = 0;
    private int weisungsIdent = 0;

    /**Alternative 1 - wird nur noch bei App verwendet!*/
    private EclAbstimmungenListeM abstimmungenListeM = null;
    private EclKIAVM kiavM = null;

    /**Alternative 2 - bei allen anderen*/
    private EclWeisungMeldung weisungMeldung = null;
    private EclWeisungMeldungRaw weisungMeldungRaw = null;

    /******************************Standard Getter/Setter*************************************/


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

    public EclWeisungMeldung getWeisungMeldung() {
        return weisungMeldung;
    }

    public void setWeisungMeldung(EclWeisungMeldung weisungMeldung) {
        this.weisungMeldung = weisungMeldung;
    }

    public EclWeisungMeldungRaw getWeisungMeldungRaw() {
        return weisungMeldungRaw;
    }

    public void setWeisungMeldungRaw(EclWeisungMeldungRaw weisungMeldungRaw) {
        this.weisungMeldungRaw = weisungMeldungRaw;
    }

}
