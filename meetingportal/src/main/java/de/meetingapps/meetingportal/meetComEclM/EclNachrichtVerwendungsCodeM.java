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

import de.meetingapps.meetingportal.meetComEntities.EclNachrichtVerwendungsCode;

public class EclNachrichtVerwendungsCodeM implements Serializable {
    private static final long serialVersionUID = 7021772390981776149L;

    private int ident = 0;
    private String beschreibung = "";
    private int identNachrichtBasisText = 0;

    public EclNachrichtVerwendungsCodeM() {
        return;
    }

    public EclNachrichtVerwendungsCodeM(EclNachrichtVerwendungsCode lNachrichtVerwendungsCode) {
        ident = lNachrichtVerwendungsCode.ident;
        beschreibung = lNachrichtVerwendungsCode.beschreibung;
        identNachrichtBasisText = lNachrichtVerwendungsCode.identNachrichtBasisText;
    }

    /****************Standard getter und setter*******************/
    public int getIdent() {
        return ident;
    }

    public void setIdent(int ident) {
        this.ident = ident;
    }

    public String getBeschreibung() {
        return beschreibung;
    }

    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }

    public int getIdentNachrichtBasisText() {
        return identNachrichtBasisText;
    }

    public void setIdentNachrichtBasisText(int identNachrichtBasisText) {
        this.identNachrichtBasisText = identNachrichtBasisText;
    }

}
