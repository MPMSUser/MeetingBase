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
package de.meetingapps.meetingportal.meetingport;

import java.io.Serializable;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

@SessionScoped
@Named


public class UVeranstaltungenSession implements Serializable {
    private static final long serialVersionUID = -8356732702942252828L;

    
    private boolean zeigeFehlermeldungAn=false;
    private String fehlerText="";
    
    private boolean zeigeQuittungAn=false;

//    private boolean ausgewaehlt=false;
    
    
    /***********************Standard getter und setter***************************************/
 
//    public boolean isAusgewaehlt() {
//        return ausgewaehlt;
//    }
//
//    public void setAusgewaehlt(boolean ausgewaehlt) {
//        this.ausgewaehlt = ausgewaehlt;
//    }


    public boolean isZeigeQuittungAn() {
        return zeigeQuittungAn;
    }

    public void setZeigeQuittungAn(boolean zeigeQuittungAn) {
        this.zeigeQuittungAn = zeigeQuittungAn;
    }

    public boolean isZeigeFehlermeldungAn() {
        return zeigeFehlermeldungAn;
    }

    public void setZeigeFehlermeldungAn(boolean zeigeFehlermeldungAn) {
        this.zeigeFehlermeldungAn = zeigeFehlermeldungAn;
    }

    public String getFehlerText() {
        return fehlerText;
    }

    public void setFehlerText(String fehlerText) {
        this.fehlerText = fehlerText;
    }
}
