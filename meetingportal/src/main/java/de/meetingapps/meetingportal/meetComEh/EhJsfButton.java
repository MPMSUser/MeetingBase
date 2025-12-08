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
package de.meetingapps.meetingportal.meetComEh;

import java.io.Serializable;

/**Element für Button-Element in Button-Liste für JSF-Anzeige*/
public class EhJsfButton  implements Serializable {
    private static final long serialVersionUID = 4122754634454715879L;
    
    /**Beschriftung*/
    public String buttonLabel="";
    
    /**Wert - je nach lokaler Verwendung*/
    public int buttonAktion=0;
    public String buttonWert="";
    public String folgeStatusBezeichnung="";
    
    /**Zusatzinfos*/
    public int redeRaumNr=0;
    public int testRaumNr=0;
    
    public EhJsfButton(String pButtonLabel, int pButtonAktion){
        buttonLabel=pButtonLabel;
        buttonAktion=pButtonAktion;
    }

    public EhJsfButton(String pButtonLabel, String  pFolgeStatusBezeichnung){
        buttonLabel=pButtonLabel;
        folgeStatusBezeichnung=pFolgeStatusBezeichnung;
    }
   

    /************************************************Standard getter und setter*****************************************************/

    public String getButtonLabel() {
        return buttonLabel;
    }

    public void setButtonLabel(String buttonLabel) {
        this.buttonLabel = buttonLabel;
    }

    public int getButtonAktion() {
        return buttonAktion;
    }

    public void setButtonAktion(int buttonAktion) {
        this.buttonAktion = buttonAktion;
    }

    public String getFolgeStatusBezeichnung() {
        return folgeStatusBezeichnung;
    }

    public void setFolgeStatusBezeichnung(String folgeStatusBezeichnung) {
        this.folgeStatusBezeichnung = folgeStatusBezeichnung;
    }

    public int getRedeRaumNr() {
        return redeRaumNr;
    }

    public void setRedeRaumNr(int redeRaumNr) {
        this.redeRaumNr = redeRaumNr;
    }

    public int getTestRaumNr() {
        return testRaumNr;
    }

    public void setTestRaumNr(int testRaumNr) {
        this.testRaumNr = testRaumNr;
    }

    public String getButtonWert() {
        return buttonWert;
    }

    public void setButtonWert(String buttonWert) {
        this.buttonWert = buttonWert;
    }


}
