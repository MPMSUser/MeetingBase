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
import de.meetingapps.meetingportal.meetComHVParam.ParamStruktPresetArt;
import de.meetingapps.meetingportal.meetComHVParam.ParamStruktVersammlungstyp;
import de.meetingapps.meetingportal.meetComWE.WERootRC;

public class WEStubParamStruktRC extends WERootRC {

    public List<ParamStruktVersammlungstyp> paramStruktVersammlungstypListe=null;

    public List<ParamStruktPresetArt> paramStruktPresetArtListe=null;

    
    public ParamStruktGruppenHeader paramStruktGruppeHeader=null;
    public EclEmittenten emittent=null;


    public List<ParamStrukt> paramStruktListe=null;

    public List<ParamStruktGruppenHeader> paramStruktGruppenHeaderListe=null;
    
    
    /***********Standard Getter und Setter********************/

    
    public List<ParamStruktVersammlungstyp> getParamStruktVersammlungstypListe() {
        return paramStruktVersammlungstypListe;
    }

    public void setParamStruktVersammlungstypListe(List<ParamStruktVersammlungstyp> paramStruktVersammlungstypListe) {
        this.paramStruktVersammlungstypListe = paramStruktVersammlungstypListe;
    }

    public ParamStruktGruppenHeader getParamStruktGruppeHeader() {
        return paramStruktGruppeHeader;
    }

    public void setParamStruktGruppeHeader(ParamStruktGruppenHeader paramStruktGruppeHeader) {
        this.paramStruktGruppeHeader = paramStruktGruppeHeader;
    }

    public EclEmittenten getEmittent() {
        return emittent;
    }

    public void setEmittent(EclEmittenten emittent) {
        this.emittent = emittent;
    }

    public List<ParamStrukt> getParamStruktListe() {
        return paramStruktListe;
    }

    public void setParamStruktListe(List<ParamStrukt> paramStruktListe) {
        this.paramStruktListe = paramStruktListe;
    }

    public List<ParamStruktPresetArt> getParamStruktPresetArtListe() {
        return paramStruktPresetArtListe;
    }

    public void setParamStruktPresetArtListe(List<ParamStruktPresetArt> paramStruktPresetArtListe) {
        this.paramStruktPresetArtListe = paramStruktPresetArtListe;
    }

    public List<ParamStruktGruppenHeader> getParamStruktGruppenHeaderListe() {
        return paramStruktGruppenHeaderListe;
    }

    public void setParamStruktGruppenHeaderListe(List<ParamStruktGruppenHeader> paramStruktGruppenHeaderListe) {
        this.paramStruktGruppenHeaderListe = paramStruktGruppenHeaderListe;
    }



}
