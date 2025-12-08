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

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComBl.BlFragen;
import de.meetingapps.meetingportal.meetComBlManaged.BlMVirtuelleHV;
import de.meetingapps.meetingportal.meetComEclM.EclDbM;
import de.meetingapps.meetingportal.meetComEclM.EclFragenM;
import de.meetingapps.meetingportal.meetComEclM.EclParamM;
import de.meetingapps.meetingportal.meetComEclM.EclPortalTexteM;
import de.meetingapps.meetingportal.meetComEclM.EclTeilnehmerLoginM;
import de.meetingapps.meetingportal.meetComEclM.EclVirtuellerTeilnehmerM;
import de.meetingapps.meetingportal.meetComEntities.EclFragen;
import de.meetingapps.meetingportal.meetComEntities.EclVirtuellerTeilnehmer;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@RequestScoped
@Named
@Deprecated
public class AControllerFragen {

    private @Inject AControllerFragenSession aControllerFragenSession;
    private @Inject EclDbM eclDbM;
    private @Inject EclPortalTexteM eclPortalTexteM;
    private @Inject EclParamM eclParamM;
    private @Inject ADlgVariablen aDlgVariablen;
    private @Inject AFunktionen aFunktionen;
    private @Inject EclTeilnehmerLoginM eclTeilnehmerLoginM;
    private @Inject BlMVirtuelleHV blMVirtuelleHV;

    /**open wird hier in der Funktion gehandelt*/
    public void init() {
        aControllerFragenSession.setPersonBereitsAusgewaehlt(false);
        aControllerFragenSession.setAktuellerFragensteller("");
        aControllerFragenSession.setAusgewaehlterTeilnehmerIdent(-1);

        initNeueFrage();

    }

    public void initNeueFrage() {
        protokoll("initNeueFrage");
        eclDbM.openAll();
        eclDbM.openWeitere();
        aControllerFragenSession.setAnzuzeigenderBereich(1); //Fragen-Button
        aControllerFragenSession.setFrageText("");
        aControllerFragenSession.setFrageZuTop("");

        BlFragen blFragen = new BlFragen(true, eclDbM.getDbBundle());

        /*Potentielle Fragensteller einlesen*/
        if (eclTeilnehmerLoginM.getAnmeldeKennungArt() == 1) {
            blFragen.ermittleMoeglicheFragenSteller(eclTeilnehmerLoginM.getAnmeldeIdentAktienregister(), true);
            aControllerFragenSession.setMaxZeichen(blFragen.fragenMaxZeichen(eclTeilnehmerLoginM.getStimmen(), false));
            aControllerFragenSession.setMaxFragen(blFragen.fragenMaxFragen(eclTeilnehmerLoginM.getStimmen(), false));
            /*Bereits gestellte Fragen einlesen*/
            blFragen.holeFragenZuAktionaersIdent(eclTeilnehmerLoginM.getAnmeldeIdentAktienregister());
        } else {
            blFragen.ermittleMoeglicheFragenSteller(eclTeilnehmerLoginM.getAnmeldeIdentPersonenNatJur() * (-1), true);
            aControllerFragenSession.setMaxZeichen(blFragen.fragenMaxZeichen(eclTeilnehmerLoginM.getStimmen(), true));
            aControllerFragenSession.setMaxFragen(blFragen.fragenMaxFragen(eclTeilnehmerLoginM.getStimmen(), true));
            /*Bereits gestellte Fragen einlesen*/
            blFragen.holeFragenZuAktionaersIdent(eclTeilnehmerLoginM.getAnmeldeIdentPersonenNatJur() * (-1));
        }

        List<EclVirtuellerTeilnehmer> rcVirtuelleTeilnehmerList = blFragen.rcVirtuelleTeilnehmerList;
        List<EclVirtuellerTeilnehmerM> virtuelleTeilnehmerListM = new LinkedList<EclVirtuellerTeilnehmerM>();
        for (int i = 0; i < rcVirtuelleTeilnehmerList.size(); i++) {
            virtuelleTeilnehmerListM.add(new EclVirtuellerTeilnehmerM(rcVirtuelleTeilnehmerList.get(i)));
        }
        aControllerFragenSession.setVirtuelleTeilnehmerListM(virtuelleTeilnehmerListM);

        List<EclFragen> rcFragenGestelltListe = blFragen.rcFragenZuAktionaer;
        List<EclFragenM> fragenGestelltListeM = new LinkedList<EclFragenM>();
        for (int i = 0; i < rcFragenGestelltListe.size(); i++) {
            fragenGestelltListeM.add(new EclFragenM(rcFragenGestelltListe.get(i)));
        }
        aControllerFragenSession.setFragenGestelltListe(fragenGestelltListeM);
        aControllerFragenSession.setAnzahlFragenGestellt(fragenGestelltListeM.size());

        eclDbM.closeAll();
    }

    public String doFrageStart() {
        if (!aFunktionen.pruefeStart("aFragen")) {
            return "aDlgFehler";
        }
        protokoll("doFrageStart");
        if (aControllerFragenSession.getMaxFragen() <= aControllerFragenSession.getAnzahlFragenGestellt()) {
            aControllerFragenSession.setAnzuzeigenderBereich(6);
            return aFunktionen.setzeEnde("aFragen", true, true);
        }
        if (aControllerFragenSession.isPersonBereitsAusgewaehlt() == false
                && eclParamM.getParam().paramPortal.fragenStellerAbfragen == 1) {
            /*Als nächstes Prozess der Auswahl des Fragenstellers durchlaufen*/
            aControllerFragenSession.setAnzuzeigenderBereich(2);
            return aFunktionen.setzeEnde("aFragen", true, true);
        }

        /*Neue Frage stellen*/
        aControllerFragenSession.setFrageZuTop("");
        aControllerFragenSession.setFrageText("");
        aControllerFragenSession.setAnzuzeigenderBereich(4); //Frage Eingeben

        return aFunktionen.setzeEnde("aFragen", true, true);
    }

    public String doPersonAuswaehlen() {
        if (!aFunktionen.pruefeStart("aFragen")) {
            return "aDlgFehler";
        }
        protokoll("doPersonAuswaehlen");
        if (aControllerFragenSession.getAusgewaehlterTeilnehmerIdent() == -1) {
            aDlgVariablen.setFehlerMeldung(eclPortalTexteM.getFehlertext(CaFehler.afBevollmaechtigterNichtVorhanden));
            aDlgVariablen.setFehlerNr(CaFehler.afBevollmaechtigterNichtVorhanden);
            aFunktionen.setzeEnde();
            return "";
        }

        EclVirtuellerTeilnehmerM virtuellerTeilnehmerM = liefereVirtuellenTeilnehmer(
                aControllerFragenSession.getAusgewaehlterTeilnehmerIdent());
        if (virtuellerTeilnehmerM.getArt() == 4) {
            /*"Sonstige nicht zulässig!*/
            aControllerFragenSession.setAnzuzeigenderBereich(5);
            return aFunktionen.setzeEnde("aFragen", true, true);
        }

        aControllerFragenSession.setAktuellerFragensteller(virtuellerTeilnehmerM.getName());
        aControllerFragenSession.setAnzuzeigenderBereich(3);//Person nochmal bestätigen lassen

        return aFunktionen.setzeEnde("aFragen", true, true);
    }

    public String doPersonBestaetigen() {
        if (!aFunktionen.pruefeStart("aFragen")) {
            return "aDlgFehler";
        }
        protokoll("doPersonBestaetigen");
        aControllerFragenSession.setAnzuzeigenderBereich(4);//Person nochmal bestätigen lassen
        aControllerFragenSession.setPersonBereitsAusgewaehlt(true);

        return aFunktionen.setzeEnde("aFragen", true, true);
    }


    public String doZurueck() {
        if (!aFunktionen.pruefeStart("aFragen")) {
            return "aDlgFehler";
        }
        protokoll("doZurueck");
        eclDbM.openAll();
        String naechsteMaske = aFunktionen.waehleAusgangsmaske(eclDbM.getDbBundle());
        eclDbM.closeAll();
        return aFunktionen.setzeEnde(naechsteMaske, true, true);
    }

    public String doAbmelden() {
        protokoll("doAbmelden");
        aDlgVariablen.clearLogin();
        return aFunktionen.waehleLogout();
    }

    private EclVirtuellerTeilnehmerM liefereVirtuellenTeilnehmer(int pLaufendeIdent) {
        List<EclVirtuellerTeilnehmerM> virtuelleTeilnehmerListM = aControllerFragenSession
                .getVirtuelleTeilnehmerListM();
        EclVirtuellerTeilnehmerM virtuellerTeilnehmerM = null;
        for (int i = 0; i < virtuelleTeilnehmerListM.size(); i++) {
            if (virtuelleTeilnehmerListM.get(i).getLaufendeIdent() == pLaufendeIdent) {
                virtuellerTeilnehmerM = virtuelleTeilnehmerListM.get(i);
            }
        }

        return virtuellerTeilnehmerM;

    }

    private void protokoll(String pText) {
        CaBug.druckeInfo(">>>>AControllerFragen<<<< " + pText + " Mandant=" + eclParamM.getClGlobalVar().mandant
                + " Login=" + eclTeilnehmerLoginM.getAnmeldeKennung());
    }
}
