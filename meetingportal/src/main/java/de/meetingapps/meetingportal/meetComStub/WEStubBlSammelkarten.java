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
package de.meetingapps.meetingportal.meetComStub;

import de.meetingapps.meetingportal.meetComEntities.EclAbstimmungsVorschlag;
import de.meetingapps.meetingportal.meetComEntities.EclMeldung;
import de.meetingapps.meetingportal.meetComEntities.EclWeisungMeldung;
import de.meetingapps.meetingportal.meetComEntities.EclWeisungMeldungRaw;
import de.meetingapps.meetingportal.meetComEntities.EclWeisungMeldungSplit;
import de.meetingapps.meetingportal.meetComEntities.EclWeisungMeldungSplitRaw;
import de.meetingapps.meetingportal.meetComWE.WERoot;

public class WEStubBlSammelkarten extends WERoot {

    /**Bisher vergebener Max-Wert = 17*/
    public int stubFunktion = -1;

    public int weisungsPos = 0;
    public int artFuerKIAV = 0;
    public int artFuerSRV = 0;
    public int artFuerOrganisatorisch = 0;
    public int artFuerBriefwahl = 0;
    public int artFuerDauervollmacht = 0;

    /**Auch für pSammelMeldung*/
    public EclMeldung meldung = null;
    public EclWeisungMeldung weisungMeldung = null;
    public EclWeisungMeldungSplit weisungMeldungSplit = null;
    public EclWeisungMeldungRaw weisungMeldungRaw = null;
    public EclWeisungMeldungSplitRaw weisungMeldungSplitRaw = null;
    public EclAbstimmungsVorschlag abstimmungsvorschlag = null;

    public boolean pNurAktive = false;
    /**auch sammelIdent*/
    public int pSammelIdent = 0;

    public String vertreterName = "";
    public String vertreterVorname = "";
    public String vertreterOrt = "";
    public int vertreterIdent = 0;

    public String ekNummer = "";

    public int skIst = 0;
    public int meldungIdent = 0;
    public int[] meldungIdentArray = null;

    public int[] abgabe = null;
    public EclWeisungMeldung[] weisungMeldungArray = null;

    public int skWeisungsartZulaessigNeu = 0;

    public int sammelIdentNeu = 0;
    public int skIstNeu = 0;

    public int art = 0;
    public int internetOderPapierOderHV = 0;

    /***********Standard Getter und Setter********************/

    public int getStubFunktion() {
        return stubFunktion;
    }

    public void setStubFunktion(int stubFunktion) {
        this.stubFunktion = stubFunktion;
    }

    public int getWeisungsPos() {
        return weisungsPos;
    }

    public void setWeisungsPos(int weisungsPos) {
        this.weisungsPos = weisungsPos;
    }

    public int getArtFuerKIAV() {
        return artFuerKIAV;
    }

    public void setArtFuerKIAV(int artFuerKIAV) {
        this.artFuerKIAV = artFuerKIAV;
    }

    public int getArtFuerSRV() {
        return artFuerSRV;
    }

    public void setArtFuerSRV(int artFuerSRV) {
        this.artFuerSRV = artFuerSRV;
    }

    public int getArtFuerOrganisatorisch() {
        return artFuerOrganisatorisch;
    }

    public void setArtFuerOrganisatorisch(int artFuerOrganisatorisch) {
        this.artFuerOrganisatorisch = artFuerOrganisatorisch;
    }

    public int getArtFuerBriefwahl() {
        return artFuerBriefwahl;
    }

    public void setArtFuerBriefwahl(int artFuerBriefwahl) {
        this.artFuerBriefwahl = artFuerBriefwahl;
    }

    public int getArtFuerDauervollmacht() {
        return artFuerDauervollmacht;
    }

    public void setArtFuerDauervollmacht(int artFuerDauervollmacht) {
        this.artFuerDauervollmacht = artFuerDauervollmacht;
    }

}
