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

import de.meetingapps.meetingportal.meetComEclM.EclParamM;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

/**Auch für t* zu verwenden!*/
@SessionScoped
@Named
@Deprecated
public class ALanguage implements Serializable {

    private static final long serialVersionUID = -5975699016117900105L;

    @Inject
    EclParamM eclParamM;

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
    }

    public void setEN() {
        /*Abfangen, falls Englisch nicht angeboten wird*/
        if (eclParamM.getEclEmittent().lieferePortalInEnglischVerfuegbar() == false) {
            setDE();
            return;
        }

        this.lang = 2;
        eclParamM.getClGlobalVar().sprache = 2;
        sprache = "EN";
    }

    public void setDE() {
        this.lang = 1;
        eclParamM.getClGlobalVar().sprache = 1;
        sprache = "DE";

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
        if (eclParamM.getEclEmittent().lieferePortalInEnglischVerfuegbar() == false) {
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
        if (eclParamM.getEclEmittent().lieferePortalInEnglischVerfuegbar() == false) {
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