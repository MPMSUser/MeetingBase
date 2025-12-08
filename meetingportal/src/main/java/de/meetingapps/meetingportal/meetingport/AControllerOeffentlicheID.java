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

import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComBa.BaMailM;
import de.meetingapps.meetingportal.meetComEclM.EclAnredeListeM;
import de.meetingapps.meetingportal.meetComEclM.EclDbM;
import de.meetingapps.meetingportal.meetComEclM.EclGastM;
import de.meetingapps.meetingportal.meetComEclM.EclPortalTexteM;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@RequestScoped
@Named
@Deprecated
public class AControllerOeffentlicheID {

    @Inject
    ADlgVariablen aDlgVariablen;
    @Inject
    AFunktionen aFunktionen;
    @Inject
    EclPortalTexteM eclTextePortalM;
    @Inject
    private EclDbM eclDbM;
    @Inject
    EclAnredeListeM lAnredeListeM;
    @Inject
    EclGastM lGastM;
    @Inject
    private BaMailM baMailm;
    @Inject
    AControllerOeffentlicheIDSession aControllerOeffentlicheIDSession;

    //	/**Ich glaube diese Funktion hier (doRegistrieren) kann hier gelöscht werden ....*/
    //	public String doRegistrieren(String pGastIdent){
    //		int erg;
    //		
    //		lDbBundle=new DbBundle();
    //		lDbBundle.openAll();
    //	
    //		lGastM.init();
    //	
    //		/*Achtung: noch nicht berücksichtigt, dass storniert, mehrere ZutrittsIdents, etc. ....*/
    //		erg=lDbBundle.dbZutrittskarten.readZuMeldungsIdentGast(Integer.parseInt(pGastIdent));
    //
    //		lGastM.setNummer(lDbBundle.dbZutrittskarten.zutrittskartenGefunden(0).zutrittsIdent);
    //		lGastM.setMeldeIdent(Integer.parseInt(pGastIdent));
    //
    //		lDbBundle.closeAll();
    //		
    //		return "aGGNeuerGastRegistrieren";
    //
    //	}

    public String doSenden() {
        if (!aFunktionen.pruefeStart("aOeffentlicheID")) {
            return "aDlgFehler";
        }
        baMailm.senden(aControllerOeffentlicheIDSession.getMailAdresse(),
                aControllerOeffentlicheIDSession.getMailBetreff(), aControllerOeffentlicheIDSession.getMailText());
        aDlgVariablen.setFehlerMeldung(eclTextePortalM.getFehlertext(CaFehler.afOeffentlicheIDGesendet));
        aDlgVariablen.setFehlerNr(CaFehler.afOeffentlicheIDGesendet);
        aFunktionen.setzeEnde("aOeffentlicheID", true, false);
        return "";
    }

    public String doZurueck() {
        if (!aFunktionen.pruefeStart("aOeffentlicheID")) {
            return "aDlgFehler";
        }
        eclDbM.openAll();
        String naechsteMaske = aFunktionen.waehleAusgangsmaske(eclDbM.getDbBundle());
        eclDbM.closeAll();
        return aFunktionen.setzeEnde(naechsteMaske, true, true);
    }

    public String doAbmelden() {
        aDlgVariablen.clearLogin();
        return aFunktionen.setzeEnde("aLogin", true, true);
    }

    /**************Standard Getter und Setter************************/
    public String getMailAdresse() {
        return aControllerOeffentlicheIDSession.getMailAdresse();
    }

    public void setMailAdresse(String mailAdresse) {
        aControllerOeffentlicheIDSession.setMailAdresse(mailAdresse);
    }

    public String getMailBetreff() {
        return aControllerOeffentlicheIDSession.getMailBetreff();
    }

    public void setMailBetreff(String mailBetreff) {
        aControllerOeffentlicheIDSession.setMailBetreff(mailBetreff);
    }

    public String getMailText() {
        return aControllerOeffentlicheIDSession.getMailText();
    }

    public void setMailText(String mailText) {
        aControllerOeffentlicheIDSession.setMailText(mailText);
    }

}
