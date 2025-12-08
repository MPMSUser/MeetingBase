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
package de.meetingapps.meetingportal.meetComEclM;

import java.io.Serializable;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@SessionScoped
@Named
public class EclParameterM implements Serializable {
    private static final long serialVersionUID = -2057401579695464623L;

    @Inject
    EclParamM eclParamM;

    /**Alles Deprecated - nur noch wg. XHTMLs - Zugriff zukünftig über EclParamM!*/
    /********************Parameter zum Zugriff über JSF**********************************/
    /**0 => Portal ist nur in Deutsch verfügbar 1=> Portal ist in Deutsch und Englisch verfügbar*/
    public int getPortalAuchInEnglischVerfuegbar() {
        if ((eclParamM.getEclEmittent().portalSprache & 2) == 2) {
            return 1;
        }
        return 0;

    }

    /** Darstellung der Sprachumschaltung (wenn englisch aktiv): 1=Texte, 2=Flaggen*/
    public int getArtSprachumschaltung() {
        return eclParamM.getParam().paramPortal.artSprachumschaltung;
    }

    /**Falls 1, wird auf der Login-Seite der Text geteilt in
     * > Textbereich 1
     * > Button IOS
     * > Button Android
     * > Textbereich 2 
     */
    public int getAppInstallButtonsAnzeigen() {
        return eclParamM.getParam().paramPortalServer.appInstallButtonsAnzeigen;
    }

    public int getpLfdHVPortalErstanmeldungIstMoeglich() {
        return eclParamM.getParam().paramPortal.lfdHVPortalErstanmeldungIstMoeglich;
    }

    public int getpLfdHVPortalInBetrieb() {
        return eclParamM.getParam().paramPortal.lfdHVPortalInBetrieb;
    }

    public int getpLfdPortalStartNr() {
        return eclParamM.getParam().paramPortalServer.pLfdPortalStartNr;
    }

    public int getpLfdVorDerHVNachDerHV() {
        return eclParamM.getParam().paramPortal.lfdVorDerHVNachDerHV;
    }

    public int getpLfdHVPortalBriefwahlIstMoeglich() {
        return eclParamM.getParam().paramPortal.lfdHVPortalBriefwahlIstMoeglich;
    }

}
