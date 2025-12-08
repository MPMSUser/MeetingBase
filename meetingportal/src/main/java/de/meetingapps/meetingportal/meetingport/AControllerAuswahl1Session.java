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
@Deprecated
public class AControllerAuswahl1Session implements Serializable {
    private static final long serialVersionUID = -1072228811792773733L;

    private boolean beiratswahlAktiv = false;
    private boolean generalversammlungAktiv = false;
    private boolean dialogveranstaltungenAktiv = false;
    private boolean adressaenderungAktiv = false;

    private boolean beiratswahlMoeglich = false;
    private boolean generalversammlungMoeglich = false;
    private boolean dialogveranstaltungenMoeglich = false;
    private boolean adressaenderungMoeglich = false;

    /*******************Standard getter und setter***************************/
    public boolean isBeiratswahlAktiv() {
        return beiratswahlAktiv;
    }

    public void setBeiratswahlAktiv(boolean beiratswahlAktiv) {
        this.beiratswahlAktiv = beiratswahlAktiv;
    }

    public boolean isGeneralversammlungAktiv() {
        return generalversammlungAktiv;
    }

    public void setGeneralversammlungAktiv(boolean generalversammlungAktiv) {
        this.generalversammlungAktiv = generalversammlungAktiv;
    }

    public boolean isDialogveranstaltungenAktiv() {
        return dialogveranstaltungenAktiv;
    }

    public void setDialogveranstaltungenAktiv(boolean dialogveranstaltungenAktiv) {
        this.dialogveranstaltungenAktiv = dialogveranstaltungenAktiv;
    }

    public boolean isGeneralversammlungMoeglich() {
        return generalversammlungMoeglich;
    }

    public void setGeneralversammlungMoeglich(boolean generalversammlungMoeglich) {
        this.generalversammlungMoeglich = generalversammlungMoeglich;
    }

    public boolean isDialogveranstaltungenMoeglich() {
        return dialogveranstaltungenMoeglich;
    }

    public void setDialogveranstaltungenMoeglich(boolean dialogveranstaltungenMoeglich) {
        this.dialogveranstaltungenMoeglich = dialogveranstaltungenMoeglich;
    }

    public boolean isBeiratswahlMoeglich() {
        return beiratswahlMoeglich;
    }

    public void setBeiratswahlMoeglich(boolean beiratswahlMoeglich) {
        this.beiratswahlMoeglich = beiratswahlMoeglich;
    }

    public boolean isAdressaenderungAktiv() {
        return adressaenderungAktiv;
    }

    public void setAdressaenderungAktiv(boolean adressaenderungAktiv) {
        this.adressaenderungAktiv = adressaenderungAktiv;
    }

    public boolean isAdressaenderungMoeglich() {
        return adressaenderungMoeglich;
    }

    public void setAdressaenderungMoeglich(boolean adressaenderungMoeglich) {
        this.adressaenderungMoeglich = adressaenderungMoeglich;
    }

}
