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

import java.util.List;

import de.meetingapps.meetingportal.meetComEclM.EclAbstimmungM;
import de.meetingapps.meetingportal.meetComEclM.EclAbstimmungenListeM;
import de.meetingapps.meetingportal.meetComEclM.EclKIAVM;
import de.meetingapps.meetingportal.meetComEclM.EclParamM;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@RequestScoped
@Named
@Deprecated
public class AControllerKIAVVollmacht {

    @Inject
    ADlgVariablen aDlgVariablen;
    @Inject
    AFunktionen aFunktionen;
    @Inject
    EclParamM eclParamM;

    @Inject
    EclKIAVM lkiavm;

    @Inject
    EclAbstimmungenListeM eclAbstimmungenListeM;
    @Inject
    AControllerKIAVBestaetigung aControllerKIAVBestaetigung;

    public String doAllesJa() {
        if (!aFunktionen.pruefeStart("aKiavVollmacht")) {
            return "aDlgFehler";
        }
        setzen("J");

        aFunktionen.setzeEnde();
        return "";
    }

    public String doAllesNein() {
        if (!aFunktionen.pruefeStart("aKiavVollmacht")) {
            return "aDlgFehler";
        }
        setzen("N");

        aFunktionen.setzeEnde();
        return "";
    }

    public String doAllesEnthaltung() {
        if (!aFunktionen.pruefeStart("aKiavVollmacht")) {
            return "aDlgFehler";
        }
        setzen("E");

        aFunktionen.setzeEnde();
        return "";
    }

    public String doAllesLoeschen() {
        if (!aFunktionen.pruefeStart("aKiavVollmacht")) {
            return "aDlgFehler";
        }
        setzen("");

        aFunktionen.setzeEnde();
        return "";
    }

    private void setzen(String pSetzen) {
        List<EclAbstimmungM> lAbstimmungenListe = eclAbstimmungenListeM.getAbstimmungenListeM();
        int i;

        for (i = 0; i < lAbstimmungenListe.size(); i++) {
            if (!lAbstimmungenListe.get(i).isUeberschrift()) {

                lAbstimmungenListe.get(i).setGewaehlt(pSetzen);
            }
        }

        return;
    }

    public String doImSinneGesellschaft() {
        if (!aFunktionen.pruefeStart("aKiavVollmacht")) {
            return "aDlgFehler";
        }
        String lSetzen;
        List<EclAbstimmungM> lAbstimmungenListe = eclAbstimmungenListeM.getAbstimmungenListeM();
        int i;

        for (i = 0; i < lAbstimmungenListe.size(); i++) {
            if (!lAbstimmungenListe.get(i).isUeberschrift()) {
                lSetzen = lAbstimmungenListe.get(i).getAbstimmungsvorschlagGesellschaft();
                if (lSetzen.compareTo("-") == 0) {
                    lSetzen = "";
                }
                lAbstimmungenListe.get(i).setGewaehlt(lSetzen);
            }
        }

        aFunktionen.setzeEnde();
        return "";
    }

    public String doGegenSinneGesellschaft() {
        if (!aFunktionen.pruefeStart("aKiavVollmacht")) {
            return "aDlgFehler";
        }
        String lSetzen;
        List<EclAbstimmungM> lAbstimmungenListe = eclAbstimmungenListeM.getAbstimmungenListeM();
        int i;

        for (i = 0; i < lAbstimmungenListe.size(); i++) {
            if (!lAbstimmungenListe.get(i).isUeberschrift()) {
                lSetzen = lAbstimmungenListe.get(i).getAbstimmungsvorschlagGesellschaft();
                if (lSetzen.compareTo("J") == 0) {
                    lSetzen = "N";
                } else {
                    if (lSetzen.compareTo("N") == 0) {
                        lSetzen = "J";
                    }
                }

                if (lSetzen.compareTo("-") == 0) {
                    lSetzen = "";
                }
                lAbstimmungenListe.get(i).setGewaehlt(lSetzen);
            }
        }

        aFunktionen.setzeEnde();
        return "";
    }

    public String doEinzelLoeschen(EclAbstimmungM abstimmung) {
        abstimmung.setGewaehlt("");
        aFunktionen.setzeEnde();
        return "";
    }

    public String doWeiter() {
        if (!aFunktionen.pruefeStart("aKiavVollmacht")) {
            return "aDlgFehler";
        }

        /*Falls Explizite Weisungen eingegeben wurden: Überprüfen, ob nichtmarkierte ggf. separat zu behandeln sind*/

        int i;

        if (aDlgVariablen.getAusgewaehlteAktion().compareTo("8") == 0) {
            String setzen = "";
            if (eclParamM.getParam().paramPortal.pNichtmarkiertSpeichernAls != 0) {
                switch (eclParamM.getParam().paramPortal.pNichtmarkiertSpeichernAls) {
                case 1: {
                    setzen = "J";
                    break;
                }
                case 2: {
                    setzen = "N";
                    break;
                }
                case 3: {
                    setzen = "E";
                    break;
                }
                }
                List<EclAbstimmungM> lAbstimmungenListe = eclAbstimmungenListeM.getAbstimmungenListeM();

                for (i = 0; i < lAbstimmungenListe.size(); i++) {
                    if (!lAbstimmungenListe.get(i).isUeberschrift()) {

                        if (lAbstimmungenListe.get(i).getGewaehlt() == null
                                || (lAbstimmungenListe.get(i).getGewaehlt().compareTo("J") != 0
                                        && lAbstimmungenListe.get(i).getGewaehlt().compareTo("N") != 0
                                        && lAbstimmungenListe.get(i).getGewaehlt().compareTo("E") != 0)) {
                            lAbstimmungenListe.get(i).setGewaehlt(setzen);
                        }
                    }
                }
            }

        }

        if (eclParamM.getParam().paramPortal.bestaetigenDialog == 1) {
            return aFunktionen.setzeEnde("aKiavBestaetigung", true, false);
        } else {
            aFunktionen.setzeEnde("aKiavBestaetigung", true, false);
            String naechsteMaske = aControllerKIAVBestaetigung.doErteilen();
            if (naechsteMaske.isEmpty()) {
                aFunktionen.setzeEnde("aKiavVollmacht", true, false);
                return "";
            }
            return aFunktionen.setzeEnde(naechsteMaske, true, false);
        }

    }

    public String doZurueck() {
        if (!aFunktionen.pruefeStart("aKiavVollmacht")) {
            return "aDlgFehler";
        }
        return aFunktionen.setzeEnde("aKiavAuswahl", true, false);
    }

    /**Abmelden*/
    public String doAbmelden() {
        aDlgVariablen.clearLogin();
        return aFunktionen.waehleLogout();
    }

}
