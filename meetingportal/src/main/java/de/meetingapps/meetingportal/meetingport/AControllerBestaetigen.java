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

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComEclM.EclAnredeListeM;
import de.meetingapps.meetingportal.meetComEclM.EclDbM;
import de.meetingapps.meetingportal.meetComEclM.EclGastM;
import de.meetingapps.meetingportal.meetComEclM.EclMeldungenMeldungenListeM;
import de.meetingapps.meetingportal.meetComEclM.EclParamM;
import de.meetingapps.meetingportal.meetComEclM.EclPortalTexteM;
import de.meetingapps.meetingportal.meetComEclM.EclZugeordneteMeldungListeM;
import de.meetingapps.meetingportal.meetComEntities.EclAktienregisterZusatz;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@RequestScoped
@Named
@Deprecated
public class AControllerBestaetigen {

    @Inject
    AControllerBestaetigenSession aControllerBestaetigenSession;

    public void clearFehlermeldung() {
        aControllerBestaetigenSession.clearFehlermeldung();
    }

    public void clearFehlermeldung2() {
        aControllerBestaetigenSession.clearFehlermeldung2();
    }

    public boolean pruefeFehlerMeldungVorhanden() {
        if (aControllerBestaetigenSession.getFehlerMeldung().isEmpty()) {
            return false;
        }
        return true;
    }

    public boolean pruefeFehlerMeldungVorhanden2() {
        if (aControllerBestaetigenSession.getFehlerMeldung2().isEmpty()) {
            return false;
        }
        return true;
    }

    @Inject
    EclDbM eclDbM;
    @Inject
    ADlgVariablen aDlgVariablen;
    @Inject
    AFunktionen aFunktionen;
    @Inject
    EclPortalTexteM eclTextePortalM;
    @Inject
    EclParamM eclParamM;

    @Inject
    EclZugeordneteMeldungListeM eclZugeordneteMeldungListeM;

    @Inject
    EclAnredeListeM lAnredeListeM;
    @Inject
    EclGastM lGastM;
    @Inject
    EclMeldungenMeldungenListeM lMeldungenMeldungenListeM;

    @Inject
    AControllerRegistrierung aControllerRegistrierung;

    /*Bestätigung der E-Mail-Adresse*/
    public String doBestaetigen() {
        clearFehlermeldung();
        /*Keine Überprüfung auf aufrufende Maske erforderlich. Kann beliebig oft aufgerufen werden*/
        eclDbM.openAll();

        if (eclParamM.getClGlobalVar().mandant == 0) {
            CaBug.drucke("Mandant ist 0");
            aFunktionen.setzeEnde();
            return "aDlgFehlerAllgemein";
        }

        EclAktienregisterZusatz lAktienregisterZusatz = new EclAktienregisterZusatz();
        lAktienregisterZusatz.emailBestaetigenLink = aControllerBestaetigenSession.getBestaetigungsCode();
        int erg;
        erg = eclDbM.getDbBundle().dbAktienregisterZusatz.read(lAktienregisterZusatz);
        if (erg == 1) {
            lAktienregisterZusatz = eclDbM.getDbBundle().dbAktienregisterZusatz.ergebnisPosition(0);
            lAktienregisterZusatz.emailBestaetigt = 1;
            eclDbM.getDbBundle().dbAktienregisterZusatz.update(lAktienregisterZusatz);
            this.setFehlerNr(CaFehler.afEmailBestaetigt);
            this.setFehlerMeldung(eclTextePortalM.getFehlertext(CaFehler.afEmailBestaetigt));
        } else {
            this.setFehlerNr(CaFehler.afEmailBestaetigungsCodeUnbekannt);
            this.setFehlerMeldung(eclTextePortalM.getFehlertext(CaFehler.afEmailBestaetigungsCodeUnbekannt));
        }

        eclDbM.closeAll();
        aFunktionen.setzeEnde();
        return "aBestaetigt";
    }

    /*Bestätigung der 2. E-Mail-Adresse*/
    public String doBestaetigen2() {
        /*Keine Überprüfung auf aufrufende Maske erforderlich. Kann beliebig oft aufgerufen werden*/

        eclDbM.openAll();

        if (eclParamM.getClGlobalVar().mandant == 0) {
            CaBug.drucke("Mandant ist 0");
            aFunktionen.setzeEnde();
            return "aDlgFehlerAllgemein";
        }

        EclAktienregisterZusatz lAktienregisterZusatz = new EclAktienregisterZusatz();
        lAktienregisterZusatz.email2BestaetigenLink = aControllerBestaetigenSession.getBestaetigungsCode2();
        int erg;
        erg = eclDbM.getDbBundle().dbAktienregisterZusatz.read(lAktienregisterZusatz);
        if (erg == 1) {
            lAktienregisterZusatz = eclDbM.getDbBundle().dbAktienregisterZusatz.ergebnisPosition(0);
            lAktienregisterZusatz.email2Bestaetigt = 1;
            eclDbM.getDbBundle().dbAktienregisterZusatz.update(lAktienregisterZusatz);
            this.setFehlerNr2(CaFehler.afEmail2Bestaetigt);
            this.setFehlerMeldung2(eclTextePortalM.getFehlertext(CaFehler.afEmail2Bestaetigt));
        } else {
            this.setFehlerNr2(CaFehler.afEmail2BestaetigungsCodeUnbekannt);
            this.setFehlerMeldung2(eclTextePortalM.getFehlertext(CaFehler.afEmail2BestaetigungsCodeUnbekannt));
        }

        eclDbM.closeAll();
        aFunktionen.setzeEnde();
        return "aBestaetigt2";
    }

    /******************Standard Getter und Setter******************************/
    public String getBestaetigungsCode() {
        return aControllerBestaetigenSession.getBestaetigungsCode();
    }

    public void setBestaetigungsCode(String bestaetigungsCode) {
        aControllerBestaetigenSession.setBestaetigungsCode(bestaetigungsCode);
    }

    public String getBestaetigungsCode2() {
        return aControllerBestaetigenSession.getBestaetigungsCode2();
    }

    public void setBestaetigungsCode2(String bestaetigungsCode2) {
        aControllerBestaetigenSession.setBestaetigungsCode2(bestaetigungsCode2);
    }

    public String getFehlerMeldung() {
        return aControllerBestaetigenSession.getFehlerMeldung();
    }

    public void setFehlerMeldung(String fehlerMeldung) {
        aControllerBestaetigenSession.setFehlerMeldung(fehlerMeldung);
    }

    public int getFehlerNr() {
        return aControllerBestaetigenSession.getFehlerNr();
    }

    public void setFehlerNr(int fehlerNr) {
        aControllerBestaetigenSession.setFehlerNr(fehlerNr);
    }

    public String getFehlerMeldung2() {
        return aControllerBestaetigenSession.getFehlerMeldung2();
    }

    public void setFehlerMeldung2(String fehlerMeldung2) {
        aControllerBestaetigenSession.setFehlerMeldung2(fehlerMeldung2);
    }

    public int getFehlerNr2() {
        return aControllerBestaetigenSession.getFehlerNr2();
    }

    public void setFehlerNr2(int fehlerNr2) {
        aControllerBestaetigenSession.setFehlerNr2(fehlerNr2);
    }
}