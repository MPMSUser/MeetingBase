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
package de.meetingapps.meetingportal.meetComEclM;

import java.io.Serializable;

public class EclAbstTeilButtonM implements Serializable {
    private static final long serialVersionUID = -799369873459796005L;

    private int lfdNr = 0;
    private String buttontext1Vorne = "";
    private String buttontext1Ziffer = "";
    private String buttontext1Hinten = "";
    private String dateiname1 = "";

    private String buttontext2Vorne = "";
    private String buttontext2Ziffer = "";
    private String buttontext2Hinten = "";
    private String dateiname2 = "";

    /*********************Standard getter und setter****************************/
    public int getLfdNr() {
        return lfdNr;
    }

    public void setLfdNr(int lfdNr) {
        this.lfdNr = lfdNr;
    }

     public String getDateiname1() {
        return dateiname1;
    }

    public void setDateiname1(String dateiname1) {
        this.dateiname1 = dateiname1;
    }


    public String getDateiname2() {
        return dateiname2;
    }

    public void setDateiname2(String dateiname2) {
        this.dateiname2 = dateiname2;
    }

    public String getButtontext1Vorne() {
        return buttontext1Vorne;
    }

    public void setButtontext1Vorne(String buttontext1Vorne) {
        this.buttontext1Vorne = buttontext1Vorne;
    }

    public String getButtontext1Ziffer() {
        return buttontext1Ziffer;
    }

    public void setButtontext1Ziffer(String buttontext1Ziffer) {
        this.buttontext1Ziffer = buttontext1Ziffer;
    }

    public String getButtontext1Hinten() {
        return buttontext1Hinten;
    }

    public void setButtontext1Hinten(String buttontext1Hinten) {
        this.buttontext1Hinten = buttontext1Hinten;
    }

    public String getButtontext2Vorne() {
        return buttontext2Vorne;
    }

    public void setButtontext2Vorne(String buttontext2Vorne) {
        this.buttontext2Vorne = buttontext2Vorne;
    }

    public String getButtontext2Ziffer() {
        return buttontext2Ziffer;
    }

    public void setButtontext2Ziffer(String buttontext2Ziffer) {
        this.buttontext2Ziffer = buttontext2Ziffer;
    }

    public String getButtontext2Hinten() {
        return buttontext2Hinten;
    }

    public void setButtontext2Hinten(String buttontext2Hinten) {
        this.buttontext2Hinten = buttontext2Hinten;
    }

}
