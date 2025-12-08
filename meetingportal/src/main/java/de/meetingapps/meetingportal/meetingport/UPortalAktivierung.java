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

import java.util.LinkedList;
import java.util.List;

import de.meetingapps.meetingportal.meetComEclM.EclDbM;
import de.meetingapps.meetingportal.meetComEclM.EclParamM;
import de.meetingapps.meetingportal.meetComEh.EhPhasen;
import de.meetingapps.meetingportal.meetComHVParam.HVParam;
import de.meetingapps.meetingportal.meetComStub.StubParameter;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@RequestScoped
@Named
public class UPortalAktivierung {

    private @Inject
    EclDbM eclDbM;
    private @Inject
    EclParamM eclParamM;
    private @Inject
    UPortalAktivierungSession uPortalAktivierungSession;

    /**Open wird in dieser Funktion gemacht*/
    public void init() {
        /**HV-Parameter sicherheitshalber neu holen*/
        eclDbM.openAll(); 
        StubParameter stubParameter = new StubParameter(true, eclDbM.getDbBundle());
        stubParameter.leseHVParam_all(eclParamM.getClGlobalVar().mandant, eclParamM.getClGlobalVar().hvJahr, eclParamM.getClGlobalVar().hvNummer, eclParamM.getClGlobalVar().datenbereich);
        HVParam lParam=stubParameter.rcHVParam;
        eclDbM.closeAll();
        
        uPortalAktivierungSession.setWeisungAktuellNichtMoeglich(lParam.paramPortal.weisungenAktuellNichtMoeglich==1);
        
        List<EhPhasen> phasenListe=new LinkedList<EhPhasen>();
        for (int i = 1; i <= 20; i++) {
            EhPhasen lPhase=new EhPhasen();
            lPhase.phasenNr=i;
            lPhase.phasenText=lParam.paramPortalServer.phasenNamen[i];

            lPhase.manuellAktiv=lParam.paramPortal.eclPortalPhase[i].manuellAktiv;
            lPhase.manuellDeAktiv=lParam.paramPortal.eclPortalPhase[i].manuellDeaktiv;
            lPhase.gewinnspiel=lParam.paramPortal.eclPortalPhase[i].gewinnspielAktiv;
            lPhase.hvInBetrieb=lParam.paramPortal.eclPortalPhase[i].lfdHVPortalInBetrieb;
            lPhase.erstAnmeldungAktiv=lParam.paramPortal.eclPortalPhase[i].lfdHVPortalErstanmeldungIstMoeglich;
            lPhase.ekAktiv=lParam.paramPortal.eclPortalPhase[i].lfdHVPortalEKIstMoeglich;
            lPhase.srvAktiv=lParam.paramPortal.eclPortalPhase[i].lfdHVPortalSRVIstMoeglich;
            lPhase.briefwahlAktiv=lParam.paramPortal.eclPortalPhase[i].lfdHVPortalBriefwahlIstMoeglich;
            lPhase.kiavAktiv=lParam.paramPortal.eclPortalPhase[i].lfdHVPortalKIAVIstMoeglich;
            lPhase.vollmacht3Aktiv=lParam.paramPortal.eclPortalPhase[i].lfdHVPortalVollmachtDritteIstMoeglich;

            lPhase.streamAktiv=lParam.paramPortal.eclPortalPhase[i].lfdHVStreamIstMoeglich;
            lPhase.fragenAktiv=lParam.paramPortal.eclPortalPhase[i].lfdHVFragenIstMoeglich;
            lPhase.wortmeldungAktiv=lParam.paramPortal.eclPortalPhase[i].lfdHVWortmeldungenIstMoeglich;
            lPhase.widerspruechAktiv=lParam.paramPortal.eclPortalPhase[i].lfdHVWiderspruecheIstMoeglich;
            lPhase.antraegeAktiv=lParam.paramPortal.eclPortalPhase[i].lfdHVAntraegeIstMoeglich;
            lPhase.sonstigeMitteilungAktiv=lParam.paramPortal.eclPortalPhase[i].lfdHVSonstigeMitteilungenIstMoeglich;
            lPhase.botschaftenEinreichenAktiv=lParam.paramPortal.eclPortalPhase[i].lfdHVBotschaftenEinreichenIstMoeglich;
            lPhase.botschaftenAktiv=lParam.paramPortal.eclPortalPhase[i].lfdHVBotschaftenIstMoeglich;
            lPhase.chatAktiv=lParam.paramPortal.eclPortalPhase[i].lfdHVChatIstMoeglich;
            lPhase.unterlagen1Aktiv=lParam.paramPortal.eclPortalPhase[i].lfdHVUnterlagenGruppe1IstMoeglich;
            lPhase.unterlagen2Aktiv=lParam.paramPortal.eclPortalPhase[i].lfdHVUnterlagenGruppe2IstMoeglich;
            lPhase.unterlagen3Aktiv=lParam.paramPortal.eclPortalPhase[i].lfdHVUnterlagenGruppe3IstMoeglich;
            lPhase.unterlagen4Aktiv=lParam.paramPortal.eclPortalPhase[i].lfdHVUnterlagenGruppe4IstMoeglich;
            lPhase.unterlagen5Aktiv=lParam.paramPortal.eclPortalPhase[i].lfdHVUnterlagenGruppe5IstMoeglich;
            lPhase.teilnehmerverzAktiv=lParam.paramPortal.eclPortalPhase[i].lfdHVTeilnehmerverzIstMoeglich;
            lPhase.abstimmungsergAktiv=lParam.paramPortal.eclPortalPhase[i].lfdHVAbstimmungsergIstMoeglich;

            phasenListe.add(lPhase);
        }
        uPortalAktivierungSession.setPhasenListe(phasenListe);

    }
}
