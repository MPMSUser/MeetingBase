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
import de.meetingapps.meetingportal.meetComWE.WERoot;

public class WEStubBlAbstimmungenWeisungenErfassen extends WERoot {

    /**Bisher vergebener Max-Wert = 17*/
    public int stubFunktion = -1;

    public int sicht = 0;
    public boolean gegenantraegeSeparat = false;
    public int aktuellOderPreview=1;
    
    public int[] ausgewaehlteKIAV = null;
    public int[] meldungsIdent = null;
    public String[] quelle = null;

    public int pAusgewaehlteKartenklasse = 0;
    public int[] pZulaessigeKartenarten = null;
    public int[] pSammelkartenFuerGattungen = null;

    public EclScan[] pScanBuendel = null;

    public EclWeisungMeldung[] rcWeisungMeldung = null;
    public EclWeisungMeldungRaw[] rcWeisungMeldungRaw = null;

    public EclAbstimmung[][] rcAgendaArray = null;
    public EclAbstimmung[][] rcGegenantraegeArray = null;
    public int rcAnzAgendaArray[] = null;
    public int rcAnzGegenantraegeArray[] = null;

    /***********Standard Getter und Setter********************/

    public int getStubFunktion() {
        return stubFunktion;
    }

    public void setStubFunktion(int stubFunktion) {
        this.stubFunktion = stubFunktion;
    }

    public int getSicht() {
        return sicht;
    }

    public void setSicht(int sicht) {
        this.sicht = sicht;
    }

    public boolean isGegenantraegeSeparat() {
        return gegenantraegeSeparat;
    }

    public void setGegenantraegeSeparat(boolean gegenantraegeSeparat) {
        this.gegenantraegeSeparat = gegenantraegeSeparat;
    }

    public int[] getAusgewaehlteKIAV() {
        return ausgewaehlteKIAV;
    }

    public void setAusgewaehlteKIAV(int[] ausgewaehlteKIAV) {
        this.ausgewaehlteKIAV = ausgewaehlteKIAV;
    }

    public String[] getQuelle() {
        return quelle;
    }

    public void setQuelle(String[] quelle) {
        this.quelle = quelle;
    }

    public int getpAusgewaehlteKartenklasse() {
        return pAusgewaehlteKartenklasse;
    }

    public void setpAusgewaehlteKartenklasse(int pAusgewaehlteKartenklasse) {
        this.pAusgewaehlteKartenklasse = pAusgewaehlteKartenklasse;
    }

    public int[] getpZulaessigeKartenarten() {
        return pZulaessigeKartenarten;
    }

    public void setpZulaessigeKartenarten(int[] pZulaessigeKartenarten) {
        this.pZulaessigeKartenarten = pZulaessigeKartenarten;
    }

    public int[] getpSammelkartenFuerGattungen() {
        return pSammelkartenFuerGattungen;
    }

    public void setpSammelkartenFuerGattungen(int[] pSammelkartenFuerGattungen) {
        this.pSammelkartenFuerGattungen = pSammelkartenFuerGattungen;
    }

    public EclScan[] getpScanBuendel() {
        return pScanBuendel;
    }

    public void setpScanBuendel(EclScan[] pScanBuendel) {
        this.pScanBuendel = pScanBuendel;
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

}
