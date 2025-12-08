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
import java.util.Locale;

import de.meetingapps.meetingportal.meetComEclM.EclParamM;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;

/**Auch für t* zu verwenden!*/
@SessionScoped
@Named
public class TLanguage implements Serializable {

    private static final long serialVersionUID = -5975699016117900105L;

    @Inject
    EclParamM eclParamM;

    private Locale locale = null;
    private String sprache = ""; /*DE oder EN*/
    /**
     * lang:
     * 1 = deutsch
     * 2 = englisch
     */

    private int lang = 1;

    /*Allgemeiner Hinweis für diese Klasse:
     * Wenn aus JSF heraus verwendet, dann wird locale mit gesetzt.
     * Wenn das auf Fehler läuft, ist das offensichtlich z.B. über Webservice aufgerufen - dann steht
     * locale nicht zur Verfügung!
     */

    @PostConstruct
    public void init() {
        try {
            locale = FacesContext.getCurrentInstance().getExternalContext().getRequestLocale();
        } catch (Exception e) {
        }

    }

    public void setEN() {
        if (eclParamM.getEclEmittent().portalSprache == 0) {return ;}
        
        /*Abfangen, falls Englisch nicht angeboten wird*/
        if ((eclParamM.getEclEmittent().portalSprache & 2) == 0) {
            setDE();
            return;
        }

        try {
            FacesContext.getCurrentInstance().getViewRoot().setLocale(Locale.ENGLISH);
            this.locale = Locale.ENGLISH;
        } catch (Exception e) {
        }

        this.lang = 2;
        eclParamM.getClGlobalVar().sprache = 2;
        sprache = "EN";
    }

    public void setDE() {
        if (eclParamM.getEclEmittent().portalSprache == 0) {return ;}

        /*Abfangen, falls Deutsch nicht angeboten wird*/
        if ((eclParamM.getEclEmittent().portalSprache & 1) == 0) {
            setEN();
            return;
        }

        try {
            FacesContext.getCurrentInstance().getViewRoot().setLocale(Locale.GERMAN);
            this.locale = Locale.GERMAN;
        } catch (Exception e) {
        }

        this.lang = 1;
        eclParamM.getClGlobalVar().sprache = 1;
        sprache = "DE";

    }

    public Locale getLocale() {
        return locale;
    }

    public String getLanguage() {
        return locale.getLanguage();
    }

    public int getLang() {
        return lang;
    }

    public String getSprache() {
        return sprache;
    }

    public void setSprache(String sprache) {
        if (sprache.compareTo("DE") != 0 && sprache.compareTo("EN") != 0) {
            sprache = "DE";
        }
        if ((eclParamM.getEclEmittent().portalSprache & 2) == 0) {
            sprache = "DE";
        }
        this.sprache = sprache;
        if (sprache.compareTo("DE") == 0) {
            setDE();
        }
        if (sprache.compareTo("EN") == 0) {
            setEN();
        }
    }

    public void setSpracheNummer(int sprache) {
        if (sprache != 1 && sprache != 2) {
            sprache = 1;
        }
        if ((eclParamM.getEclEmittent().portalSprache & 2) == 0) {
            sprache = 1;
        }
        if (sprache == 1) {
            setDE();
        }
        if (sprache == 2) {
            setEN();
        }
    }

    public void setLang(int lang) {
        this.lang = lang;
    }

}