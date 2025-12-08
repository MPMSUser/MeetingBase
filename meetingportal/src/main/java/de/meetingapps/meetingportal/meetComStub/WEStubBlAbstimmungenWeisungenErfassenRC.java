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

import de.meetingapps.meetingportal.meetComEntities.EclAbstimmung;
import de.meetingapps.meetingportal.meetComEntities.EclScan;
import de.meetingapps.meetingportal.meetComEntities.EclWeisungMeldung;
import de.meetingapps.meetingportal.meetComEntities.EclWeisungMeldungRaw;
import de.meetingapps.meetingportal.meetComWE.WERootRC;

public class WEStubBlAbstimmungenWeisungenErfassenRC extends WERootRC {

    public EclAbstimmung[][] rcAgendaArray = null;
    public EclAbstimmung[][] rcGegenantraegeArray = null;

    public int rcAnzAgendaArray[] = null;
    public int rcAnzGegenantraegeArray[] = null;

    public int[] rcArray = null;

    public EclWeisungMeldung[] rcWeisungMeldung = null;
    public EclWeisungMeldungRaw[] rcWeisungMeldungRaw = null;

    public EclScan[] eclScanArray = null;

    public String[] rcFehlerMeldungString = null;
    public String[] rcGeleseneNummer = null;

    /***********Standard Getter und Setter********************/

    public EclAbstimmung[][] getRcAgendaArray() {
        return rcAgendaArray;
    }

    public void setRcAgendaArray(EclAbstimmung[][] rcAgendaArray) {
        this.rcAgendaArray = rcAgendaArray;
    }

    public EclAbstimmung[][] getRcGegenantraegeArray() {
        return rcGegenantraegeArray;
    }

    public void setRcGegenantraegeArray(EclAbstimmung[][] rcGegenantraegeArray) {
        this.rcGegenantraegeArray = rcGegenantraegeArray;
    }

    public int[] getRcAnzAgendaArray() {
        return rcAnzAgendaArray;
    }

    public void setRcAnzAgendaArray(int[] rcAnzAgendaArray) {
        this.rcAnzAgendaArray = rcAnzAgendaArray;
    }

    public int[] getRcAnzGegenantraegeArray() {
        return rcAnzGegenantraegeArray;
    }

    public void setRcAnzGegenantraegeArray(int[] rcAnzGegenantraegeArray) {
        this.rcAnzGegenantraegeArray = rcAnzGegenantraegeArray;
    }

    public int[] getRcArray() {
        return rcArray;
    }

    public void setRcArray(int[] rcArray) {
        this.rcArray = rcArray;
    }

    public EclWeisungMeldung[] getRcWeisungMeldung() {
        return rcWeisungMeldung;
    }

    public void setRcWeisungMeldung(EclWeisungMeldung[] rcWeisungMeldung) {
        this.rcWeisungMeldung = rcWeisungMeldung;
    }

    public EclWeisungMeldungRaw[] getRcWeisungMeldungRaw() {
        return rcWeisungMeldungRaw;
    }

    public void setRcWeisungMeldungRaw(EclWeisungMeldungRaw[] rcWeisungMeldungRaw) {
        this.rcWeisungMeldungRaw = rcWeisungMeldungRaw;
    }

    public EclScan[] getEclScanArray() {
        return eclScanArray;
    }

    public void setEclScanArray(EclScan[] eclScanArray) {
        this.eclScanArray = eclScanArray;
    }

    public String[] getRcFehlerMeldungString() {
        return rcFehlerMeldungString;
    }

    public void setRcFehlerMeldungString(String[] rcFehlerMeldungString) {
        this.rcFehlerMeldungString = rcFehlerMeldungString;
    }

    public String[] getRcGeleseneNummer() {
        return rcGeleseneNummer;
    }

    public void setRcGeleseneNummer(String[] rcGeleseneNummer) {
        this.rcGeleseneNummer = rcGeleseneNummer;
    }

}
