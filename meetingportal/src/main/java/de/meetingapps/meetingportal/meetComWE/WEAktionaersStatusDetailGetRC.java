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

import java.util.List;

import de.meetingapps.meetingportal.meetComEntities.EclAktienregister;
import de.meetingapps.meetingportal.meetComEntities.EclPersonenNatJur;
import de.meetingapps.meetingportal.meetComEntities.EclZugeordneteMeldung;

public class WEAktionaersStatusDetailGetRC extends WERootRC {

    public EclAktienregister eclAktienregisterEintrag = null;

    /**Einzelfelder aus BlWillenserklaerungStatus*/
    public int piSelektionGeberOderAlle = 1;
    public int piRueckgabeKurzOderLang = 1;
    public int piAnsichtVerarbeitungOderAnalyse = 1;
    public int piAlleWillenserklaerungen = 0;

    public EclZugeordneteMeldung[] zugeordneteMeldungenEigeneAktienArray = null;
    public int gastKartenGemeldetEigeneAktien = 0;
    public int aktienregisterPersonNatJurIdent = 0;
    public EclZugeordneteMeldung[] zugeordneteMeldungenEigeneGastkartenArray = null;
    public EclZugeordneteMeldung[] zugeordneteMeldungenBevollmaechtigtArray = null;

    public boolean briefwahlVorhanden = false;
    public boolean srvVorhanden = false;

    public boolean rcHatNichtNurPortalWillenserklaerungen = false;
    public String rcDatumLetzteWillenserklaerung = "";

    public List<EclPersonenNatJur> rcListeVollmachten = null;

  
    /*******************Standard Getter und Setter****************************/

    public EclAktienregister getEclAktienregisterEintrag() {
        return eclAktienregisterEintrag;
    }

    public void setEclAktienregisterEintrag(EclAktienregister eclAktienregisterEintrag) {
        this.eclAktienregisterEintrag = eclAktienregisterEintrag;
    }

    public int getPiSelektionGeberOderAlle() {
        return piSelektionGeberOderAlle;
    }

    public void setPiSelektionGeberOderAlle(int piSelektionGeberOderAlle) {
        this.piSelektionGeberOderAlle = piSelektionGeberOderAlle;
    }

    public int getPiRueckgabeKurzOderLang() {
        return piRueckgabeKurzOderLang;
    }

    public void setPiRueckgabeKurzOderLang(int piRueckgabeKurzOderLang) {
        this.piRueckgabeKurzOderLang = piRueckgabeKurzOderLang;
    }

    public int getPiAnsichtVerarbeitungOderAnalyse() {
        return piAnsichtVerarbeitungOderAnalyse;
    }

    public void setPiAnsichtVerarbeitungOderAnalyse(int piAnsichtVerarbeitungOderAnalyse) {
        this.piAnsichtVerarbeitungOderAnalyse = piAnsichtVerarbeitungOderAnalyse;
    }

    public int getPiAlleWillenserklaerungen() {
        return piAlleWillenserklaerungen;
    }

    public void setPiAlleWillenserklaerungen(int piAlleWillenserklaerungen) {
        this.piAlleWillenserklaerungen = piAlleWillenserklaerungen;
    }

    public EclZugeordneteMeldung[] getZugeordneteMeldungenEigeneAktienArray() {
        return zugeordneteMeldungenEigeneAktienArray;
    }

    public void setZugeordneteMeldungenEigeneAktienArray(
            EclZugeordneteMeldung[] zugeordneteMeldungenEigeneAktienArray) {
        this.zugeordneteMeldungenEigeneAktienArray = zugeordneteMeldungenEigeneAktienArray;
    }

    public int getGastKartenGemeldetEigeneAktien() {
        return gastKartenGemeldetEigeneAktien;
    }

    public void setGastKartenGemeldetEigeneAktien(int gastKartenGemeldetEigeneAktien) {
        this.gastKartenGemeldetEigeneAktien = gastKartenGemeldetEigeneAktien;
    }

    public int getAktienregisterPersonNatJurIdent() {
        return aktienregisterPersonNatJurIdent;
    }

    public void setAktienregisterPersonNatJurIdent(int aktienregisterPersonNatJurIdent) {
        this.aktienregisterPersonNatJurIdent = aktienregisterPersonNatJurIdent;
    }

    public EclZugeordneteMeldung[] getZugeordneteMeldungenEigeneGastkartenArray() {
        return zugeordneteMeldungenEigeneGastkartenArray;
    }

    public void setZugeordneteMeldungenEigeneGastkartenArray(
            EclZugeordneteMeldung[] zugeordneteMeldungenEigeneGastkartenArray) {
        this.zugeordneteMeldungenEigeneGastkartenArray = zugeordneteMeldungenEigeneGastkartenArray;
    }

    public EclZugeordneteMeldung[] getZugeordneteMeldungenBevollmaechtigtArray() {
        return zugeordneteMeldungenBevollmaechtigtArray;
    }

    public void setZugeordneteMeldungenBevollmaechtigtArray(
            EclZugeordneteMeldung[] zugeordneteMeldungenBevollmaechtigtArray) {
        this.zugeordneteMeldungenBevollmaechtigtArray = zugeordneteMeldungenBevollmaechtigtArray;
    }

    public boolean isBriefwahlVorhanden() {
        return briefwahlVorhanden;
    }

    public void setBriefwahlVorhanden(boolean briefwahlVorhanden) {
        this.briefwahlVorhanden = briefwahlVorhanden;
    }

    public boolean isSrvVorhanden() {
        return srvVorhanden;
    }

    public void setSrvVorhanden(boolean srvVorhanden) {
        this.srvVorhanden = srvVorhanden;
    }

    public boolean isRcHatNichtNurPortalWillenserklaerungen() {
        return rcHatNichtNurPortalWillenserklaerungen;
    }

    public void setRcHatNichtNurPortalWillenserklaerungen(boolean rcHatNichtNurPortalWillenserklaerungen) {
        this.rcHatNichtNurPortalWillenserklaerungen = rcHatNichtNurPortalWillenserklaerungen;
    }

    public String getRcDatumLetzteWillenserklaerung() {
        return rcDatumLetzteWillenserklaerung;
    }

    public void setRcDatumLetzteWillenserklaerung(String rcDatumLetzteWillenserklaerung) {
        this.rcDatumLetzteWillenserklaerung = rcDatumLetzteWillenserklaerung;
    }

    public List<EclPersonenNatJur> getRcListeVollmachten() {
        return rcListeVollmachten;
    }

    public void setRcListeVollmachten(List<EclPersonenNatJur> rcListeVollmachten) {
        this.rcListeVollmachten = rcListeVollmachten;
    }


}
