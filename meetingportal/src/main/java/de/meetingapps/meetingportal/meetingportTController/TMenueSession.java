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
package de.meetingapps.meetingportal.meetingportTController;

import java.io.Serializable;
import java.util.List;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComEntities.EclMenueEintrag;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

@SessionScoped
@Named
public class TMenueSession  implements Serializable {
    private static final long serialVersionUID = 7373177656413372348L;

    private int logDrucken=3;
    
    private List <EclMenueEintrag> auswahlMenue=null;

    private EclMenueEintrag ausgewaehltesMenue=null;
    
    /**Wenn true, dann wurde eine Funktion aufgerufen, über die was verändert wurde.
     * Wenn Menü gewählt, ohne dass diese ordnungsgemäß beendet wurde, dann kommt
     * ein Warnhinweis.
     */
    private boolean aenderungsmodus=false;
    
    /**+++++++++++++++Deep-Link-Ansprung+++++++++++++++++++++++*/
    private int deepLinkPMenueFunktionscode=0;
    private int deepLinkPMenueFunktionscodeSub=0;
    
    
    /********************Standard getter und setter**************************/
    
    public List<EclMenueEintrag> getAuswahlMenue() {
        return auswahlMenue;
    }

    public void setAuswahlMenue(List<EclMenueEintrag> auswahlMenue) {
        this.auswahlMenue = auswahlMenue;
    }

    public EclMenueEintrag getAusgewaehltesMenue() {
        return ausgewaehltesMenue;
    }

    public void setAusgewaehltesMenue(EclMenueEintrag ausgewaehltesMenue) {
        this.ausgewaehltesMenue = ausgewaehltesMenue;
    }

    public boolean isAenderungsmodus() {
        return aenderungsmodus;
    }

    public void setAenderungsmodus(boolean aenderungsmodus) {
        this.aenderungsmodus = aenderungsmodus;
    }


    public int getDeepLinkPMenueFunktionscode() {
        return deepLinkPMenueFunktionscode;
    }

    public void setDeepLinkPMenueFunktionscode(int deepLinkPMenueFunktionscode) {
        this.deepLinkPMenueFunktionscode = deepLinkPMenueFunktionscode;
        CaBug.druckeLog("deepLinkPMenueFunktionscode="+deepLinkPMenueFunktionscode, logDrucken, 10);
    }

    public int getDeepLinkPMenueFunktionscodeSub() {
        return deepLinkPMenueFunktionscodeSub;
    }

    public void setDeepLinkPMenueFunktionscodeSub(int deepLinkPMenueFunktionscodeSub) {
        this.deepLinkPMenueFunktionscodeSub = deepLinkPMenueFunktionscodeSub;
    }
    
    
    
}
