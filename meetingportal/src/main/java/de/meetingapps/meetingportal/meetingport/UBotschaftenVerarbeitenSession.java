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
import java.util.List;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComEntities.EclMitteilung;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;

@SessionScoped
@Named
public class UBotschaftenVerarbeitenSession  implements Serializable {
    private static final long serialVersionUID = -8073383512256009764L;

    private int logDrucken=10;
    
    private List<EclMitteilung> botschaftenListe=null;
    
    /**
     * Variablen für Upload und Anzeige Vide
     */
    private String iframe_src = "";


    /**Für Hochladen von Videodateien*/
    public void pruefeDatei(FacesContext ctx, UIComponent comp, Object value) {
        CaBug.druckeLog("UNachrichtSendenSession.pruefeDatei", logDrucken, 10);
    }

    
    /*********************Standard getter und setter*****************************/
    public List<EclMitteilung> getBotschaftenListe() {
        return botschaftenListe;
    }

    public void setBotschaftenListe(List<EclMitteilung> botschaftenListe) {
        this.botschaftenListe = botschaftenListe;
    }

    public String getIframe_src() {
        return iframe_src;
    }

    public void setIframe_src(String iframe_src) {
        this.iframe_src = iframe_src;
    }

    
    
}
