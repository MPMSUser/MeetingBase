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
import java.util.HashMap;
import java.util.List;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComEntities.EclMenueEintrag;
import de.meetingapps.meetingportal.meetComEntities.EclPortalUnterlagen;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import jakarta.servlet.http.Part;

@SessionScoped
@Named
public class UUnterlagenPdfsSession  implements Serializable {
    private static final long serialVersionUID = -8073383512256009764L;

    private int logDrucken=10;

    private List<String> dateienListe=null;
    
    private List<EclPortalUnterlagen> portalUnterlagenListe=null;
    
    private List<EclMenueEintrag> menuListe = null;
    
    private HashMap<EclMenueEintrag, List<EclPortalUnterlagen>> mapMenuUnterlagen = null;
    
    /**Datei-Anhang - für Hochladen in uLogin*/
    private Part dateiHochladen = null;
    
    /**Unterlagen bearbeiten*/
    private Boolean showUnterlageBearbeiten = false;
    
    /** Unterlage anlegen */
    private Boolean showNeueUnterlage = false;
    private String neueUnterlageLink = "";
    private String neueUnterlageBezeichnungDE = "";
    private String neueUnterlageBezeichnungEN = "";
    
    /** Unterlagenreihenfolge für Seite bearbeiten */
    private Boolean showReihenfolgeModal = false;
    private List<EclPortalUnterlagen> unterlagenAufSeiteListe = null;
    
    private EclPortalUnterlagen eclPortalUnterlagenSelected = null;

    /**Für Hochladen von Videodateien*/
    public void pruefeDatei(FacesContext ctx, UIComponent comp, Object value) {
        CaBug.druckeLog("UNachrichtSendenSession.pruefeDatei", logDrucken, 10);
        //      if (((Part)value).getSize()>10000000) {
        //          new ValidatorException(new FacesMessage("Datei zu groß! Maximal 10MB erlaubt"));
        //      }
    }

    /*********************Standard getter und setter*****************************/

    public List<String> getDateienListe() {
        return dateienListe;
    }


    public void setDateienListe(List<String> dateienListe) {
        this.dateienListe = dateienListe;
    }
    
    

    public List<EclPortalUnterlagen> getPortalUnterlagenListe() {
        return portalUnterlagenListe;
    }

    public void setPortalUnterlagenListe(List<EclPortalUnterlagen> portalUnterlagenListe) {
        this.portalUnterlagenListe = portalUnterlagenListe;
    }

    public Part getDateiHochladen() {
        return dateiHochladen;
    }

    public void setDateiHochladen(Part dateiHochladen) {
        this.dateiHochladen = dateiHochladen;
    }

    public Boolean getShowUnterlageBearbeiten() {
        return showUnterlageBearbeiten;
    }

    public void setShowUnterlageBearbeiten(Boolean showUnterlageBearbeiten) {
        this.showUnterlageBearbeiten = showUnterlageBearbeiten;
    }

    public EclPortalUnterlagen getEclPortalUnterlagenSelected() {
        return eclPortalUnterlagenSelected;
    }

    public void setEclPortalUnterlagenSelected(EclPortalUnterlagen eclPortalUnterlagenSelected) {
        this.eclPortalUnterlagenSelected = eclPortalUnterlagenSelected;
    }

    public Boolean getShowNeueUnterlage() {
        return showNeueUnterlage;
    }

    public void setShowNeueUnterlage(Boolean showNeueUnterlage) {
        this.showNeueUnterlage = showNeueUnterlage;
    }

    public String getNeueUnterlageLink() {
        return neueUnterlageLink;
    }

    public void setNeueUnterlageLink(String neueUnterlageLink) {
        this.neueUnterlageLink = neueUnterlageLink;
    }

    public String getNeueUnterlageBezeichnungDE() {
        return neueUnterlageBezeichnungDE;
    }

    public void setNeueUnterlageBezeichnungDE(String neueUnterlageBezeichnungDE) {
        this.neueUnterlageBezeichnungDE = neueUnterlageBezeichnungDE;
    }

    public String getNeueUnterlageBezeichnungEN() {
        return neueUnterlageBezeichnungEN;
    }

    public void setNeueUnterlageBezeichnungEN(String neueUnterlageBezeichnungEN) {
        this.neueUnterlageBezeichnungEN = neueUnterlageBezeichnungEN;
    }

    public List<EclMenueEintrag> getMenuListe() {
        return menuListe;
    }

    public void setMenuListe(List<EclMenueEintrag> menuListe) {
        this.menuListe = menuListe;
    }

    public Boolean getShowReihenfolgeModal() {
        return showReihenfolgeModal;
    }

    public void setShowReihenfolgeModal(Boolean showReihenfolgeModal) {
        this.showReihenfolgeModal = showReihenfolgeModal;
    }

    public List<EclPortalUnterlagen> getUnterlagenAufSeiteListe() {
        return unterlagenAufSeiteListe;
    }

    public void setUnterlagenAufSeiteListe(List<EclPortalUnterlagen> unterlagenAufSeiteListe) {
        this.unterlagenAufSeiteListe = unterlagenAufSeiteListe;
    }

    public HashMap<EclMenueEintrag, List<EclPortalUnterlagen>> getMapMenuUnterlagen() {
        return mapMenuUnterlagen;
    }

    public void setMapMenuUnterlagen(HashMap<EclMenueEintrag, List<EclPortalUnterlagen>> mapMenuUnterlagen) {
        this.mapMenuUnterlagen = mapMenuUnterlagen;
    }




}
