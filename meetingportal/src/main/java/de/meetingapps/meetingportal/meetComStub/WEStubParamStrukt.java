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


import java.util.List;

import de.meetingapps.meetingportal.meetComEntities.EclEmittenten;
import de.meetingapps.meetingportal.meetComHVParam.ParamStrukt;
import de.meetingapps.meetingportal.meetComHVParam.ParamStruktGruppenHeader;
import de.meetingapps.meetingportal.meetComWE.WERoot;

public class WEStubParamStrukt extends WERoot {

    /**Bisher vergebener Max-Wert = 7*/
    public int stubFunktion = -1;

    public int presetArt=0;
    
    public int paramStruktGruppe=0;

    public ParamStruktGruppenHeader[] paramStruktGruppeHeader=null;
    public EclEmittenten emittent=null;

    public int veranstaltungstypNeu;
    

    public List<ParamStrukt> paramStruktListe=null;
    
    public String beschreibungKurz="";
    public String beschreibungLang="";
    public int vererbenVon=0;

    /***********Standard Getter und Setter********************/

    
    public int getStubFunktion() {
        return stubFunktion;
    }

    public void setStubFunktion(int stubFunktion) {
        this.stubFunktion = stubFunktion;
    }

    public int getParamStruktGruppe() {
        return paramStruktGruppe;
    }

    public void setParamStruktGruppe(int paramStruktGruppe) {
        this.paramStruktGruppe = paramStruktGruppe;
    }

    public ParamStruktGruppenHeader[] getParamStruktGruppeHeader() {
        return paramStruktGruppeHeader;
    }

    public void setParamStruktGruppeHeader(ParamStruktGruppenHeader[] paramStruktGruppeHeader) {
        this.paramStruktGruppeHeader = paramStruktGruppeHeader;
    }

    public EclEmittenten getEmittent() {
        return emittent;
    }

    public void setEmittent(EclEmittenten emittent) {
        this.emittent = emittent;
    }

     public int getVeranstaltungstypNeu() {
        return veranstaltungstypNeu;
    }

    public void setVeranstaltungstypNeu(int veranstaltungstypNeu) {
        this.veranstaltungstypNeu = veranstaltungstypNeu;
    }

    public List<ParamStrukt> getParamStruktListe() {
        return paramStruktListe;
    }

    public void setParamStruktListe(List<ParamStrukt> paramStruktListe) {
        this.paramStruktListe = paramStruktListe;
    }

    public int getPresetArt() {
        return presetArt;
    }

    public void setPresetArt(int presetArt) {
        this.presetArt = presetArt;
    }

    public String getBeschreibungKurz() {
        return beschreibungKurz;
    }

    public void setBeschreibungKurz(String beschreibungKurz) {
        this.beschreibungKurz = beschreibungKurz;
    }

    public String getBeschreibungLang() {
        return beschreibungLang;
    }

    public void setBeschreibungLang(String beschreibungLang) {
        this.beschreibungLang = beschreibungLang;
    }

    public int getVererbenVon() {
        return vererbenVon;
    }

    public void setVererbenVon(int vererbenVon) {
        this.vererbenVon = vererbenVon;
    }

}
