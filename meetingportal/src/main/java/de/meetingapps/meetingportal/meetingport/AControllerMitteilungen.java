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
import de.meetingapps.meetingportal.meetComBl.BlMitteilungenAlt;
import de.meetingapps.meetingportal.meetComBlManaged.BlMVirtuelleHV;
import de.meetingapps.meetingportal.meetComEclM.EclDbM;
import de.meetingapps.meetingportal.meetComEclM.EclMitteilungenM;
import de.meetingapps.meetingportal.meetComEclM.EclParamM;
import de.meetingapps.meetingportal.meetComEclM.EclPortalTexteM;
import de.meetingapps.meetingportal.meetComEclM.EclTeilnehmerLoginM;
import de.meetingapps.meetingportal.meetComEclM.EclVirtuellerTeilnehmerM;
import de.meetingapps.meetingportal.meetComEntities.EclMitteilungenAlt;
import de.meetingapps.meetingportal.meetComEntities.EclVirtuellerTeilnehmer;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@RequestScoped
@Named
@Deprecated
public class AControllerMitteilungen {

    private @Inject AControllerMitteilungenSession aControllerMitteilungenSession;
    private @Inject EclDbM eclDbM;
    private @Inject EclPortalTexteM eclPortalTexteM;
    private @Inject EclParamM eclParamM;
    private @Inject ADlgVariablen aDlgVariablen;
    private @Inject AFunktionen aFunktionen;
    private @Inject EclTeilnehmerLoginM eclTeilnehmerLoginM;
    private @Inject BlMVirtuelleHV blMVirtuelleHV;

    /**open wird hier in der Funktion gehandelt*/
    public void init() {
        aControllerMitteilungenSession.setPersonBereitsAusgewaehlt(false);
        aControllerMitteilungenSession.setAktuellerMitteilungensteller("");
        aControllerMitteilungenSession.setAusgewaehlterTeilnehmerIdent(-1);

        initNeueMitteilung();

    }

    public void initNeueMitteilung() {
        protokoll("initNeueMitteilung");
        eclDbM.openAll();
        eclDbM.openWeitere();
        aControllerMitteilungenSession.setAnzuzeigenderBereich(1); //Fragen-Button
        aControllerMitteilungenSession.setMitteilungText("");

        BlMitteilungenAlt blMitteilungen = new BlMitteilungenAlt(true, eclDbM.getDbBundle());

        /*Potentielle Fragensteller einlesen*/
        if (eclTeilnehmerLoginM.getAnmeldeKennungArt() == 1) {
            blMitteilungen.ermittleMoeglicheMitteilungSteller(eclTeilnehmerLoginM.getAnmeldeIdentAktienregister());
            aControllerMitteilungenSession
                    .setMaxZeichen(blMitteilungen.mitteilungenMaxZeichen(eclTeilnehmerLoginM.getStimmen(), false));
            aControllerMitteilungenSession
                    .setMaxMitteilungen(blMitteilungen.mitteilungenMaxAnzahl(eclTeilnehmerLoginM.getStimmen(), false));
            /*Bereits gestellte Fragen einlesen*/
            blMitteilungen.holeMitteilungenZuAktionaersIdent(eclTeilnehmerLoginM.getAnmeldeIdentAktienregister());
        } else {
            blMitteilungen
                    .ermittleMoeglicheMitteilungSteller(eclTeilnehmerLoginM.getAnmeldeIdentPersonenNatJur() * (-1));
            aControllerMitteilungenSession
                    .setMaxZeichen(blMitteilungen.mitteilungenMaxZeichen(eclTeilnehmerLoginM.getStimmen(), true));
            aControllerMitteilungenSession
                    .setMaxMitteilungen(blMitteilungen.mitteilungenMaxAnzahl(eclTeilnehmerLoginM.getStimmen(), true));
            /*Bereits gestellte Fragen einlesen*/
            blMitteilungen
                    .holeMitteilungenZuAktionaersIdent(eclTeilnehmerLoginM.getAnmeldeIdentPersonenNatJur() * (-1));
        }

        List<EclVirtuellerTeilnehmer> rcVirtuelleTeilnehmerList = blMitteilungen.rcVirtuelleTeilnehmerList;
        List<EclVirtuellerTeilnehmerM> virtuelleTeilnehmerListM = new LinkedList<EclVirtuellerTeilnehmerM>();
        for (int i = 0; i < rcVirtuelleTeilnehmerList.size(); i++) {
            virtuelleTeilnehmerListM.add(new EclVirtuellerTeilnehmerM(rcVirtuelleTeilnehmerList.get(i)));
        }
        aControllerMitteilungenSession.setVirtuelleTeilnehmerListM(virtuelleTeilnehmerListM);

        List<EclMitteilungenAlt> rcMitteilungenGestelltListe = blMitteilungen.rcMitteilungenZuAktionaer;
        List<EclMitteilungenM> mitteilungenGestelltListeM = new LinkedList<EclMitteilungenM>();
        for (int i = 0; i < rcMitteilungenGestelltListe.size(); i++) {
            mitteilungenGestelltListeM.add(new EclMitteilungenM(rcMitteilungenGestelltListe.get(i)));
        }
        aControllerMitteilungenSession.setMitteilungenGestelltListe(mitteilungenGestelltListeM);
        aControllerMitteilungenSession.setAnzahlMitteilungenGestellt(mitteilungenGestelltListeM.size());

        eclDbM.closeAll();
    }

    public String doMitteilungStart() {
        if (!aFunktionen.pruefeStart("aMitteilungen")) {
            return "aDlgFehler";
        }
        protokoll("doMitteilungStart");
        if (aControllerMitteilungenSession.getMaxMitteilungen() <= aControllerMitteilungenSession
                .getAnzahlMitteilungenGestellt()) {
            aControllerMitteilungenSession.setAnzuzeigenderBereich(6);
            return aFunktionen.setzeEnde("aMitteilungen", true, true);
        }
        if (aControllerMitteilungenSession.isPersonBereitsAusgewaehlt() == false
                && eclParamM.getParam().paramPortal.widerspruecheStellerAbfragen == 1) {
            /*Als nächstes Prozess der Auswahl des Fragenstellers durchlaufen*/
            aControllerMitteilungenSession.setAnzuzeigenderBereich(2);
            return aFunktionen.setzeEnde("aMitteilungen", true, true);
        }

        /*Neue Frage stellen*/
        aControllerMitteilungenSession.setMitteilungText("");
        aControllerMitteilungenSession.setAnzuzeigenderBereich(4); //Frage Eingeben

        return aFunktionen.setzeEnde("aMitteilungen", true, true);
    }

    public String doPersonAuswaehlen() {
        if (!aFunktionen.pruefeStart("aMitteilungen")) {
            return "aDlgFehler";
        }
        protokoll("doPersonAuswaehlen");
        if (aControllerMitteilungenSession.getAusgewaehlterTeilnehmerIdent() == -1) {
            aDlgVariablen.setFehlerMeldung(eclPortalTexteM.getFehlertext(CaFehler.afBevollmaechtigterNichtVorhanden));
            aDlgVariablen.setFehlerNr(CaFehler.afBevollmaechtigterNichtVorhanden);
            aFunktionen.setzeEnde();
            return "";
        }

        EclVirtuellerTeilnehmerM virtuellerTeilnehmerM = liefereVirtuellenTeilnehmer(
                aControllerMitteilungenSession.getAusgewaehlterTeilnehmerIdent());
        if (virtuellerTeilnehmerM.getArt() == 4) {
            /*"Sonstige nicht zulässig!*/
            aControllerMitteilungenSession.setAnzuzeigenderBereich(5);
            return aFunktionen.setzeEnde("aMitteilungen", true, true);
        }

        aControllerMitteilungenSession.setAktuellerMitteilungensteller(virtuellerTeilnehmerM.getName());
        aControllerMitteilungenSession.setAnzuzeigenderBereich(3);//Person nochmal bestätigen lassen

        return aFunktionen.setzeEnde("aMitteilungen", true, true);
    }

    public String doPersonBestaetigen() {
        if (!aFunktionen.pruefeStart("aMitteilungen")) {
            return "aDlgFehler";
        }
        protokoll("doPersonBestaetigen");
        aControllerMitteilungenSession.setAnzuzeigenderBereich(4);//Person nochmal bestätigen lassen
        aControllerMitteilungenSession.setPersonBereitsAusgewaehlt(true);

        return aFunktionen.setzeEnde("aMitteilungen", true, true);
    }


    public String doZurueck() {
        if (!aFunktionen.pruefeStart("aMitteilungen")) {
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
        List<EclVirtuellerTeilnehmerM> virtuelleTeilnehmerListM = aControllerMitteilungenSession
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
        CaBug.druckeInfo(">>>>AControllerMitteilungen<<<< " + pText + " Mandant=" + eclParamM.getClGlobalVar().mandant
                + " Login=" + eclTeilnehmerLoginM.getAnmeldeKennung());
    }

}
