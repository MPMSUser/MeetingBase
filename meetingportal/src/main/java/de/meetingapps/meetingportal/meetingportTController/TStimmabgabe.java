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

import java.util.List;

import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComBlManaged.BlMAbstimmung;
import de.meetingapps.meetingportal.meetComBlManaged.BlMWillenserklaerungStatusNeuVerarbeiten;
import de.meetingapps.meetingportal.meetComEclM.EclAbstimmungM;
import de.meetingapps.meetingportal.meetComEclM.EclAbstimmungSetM;
import de.meetingapps.meetingportal.meetComEclM.EclAbstimmungenListeM;
import de.meetingapps.meetingportal.meetComEclM.EclBesitzGesamtAuswahl1M;
import de.meetingapps.meetingportal.meetComEclM.EclBesitzGesamtM;
import de.meetingapps.meetingportal.meetComEclM.EclDbM;
import de.meetingapps.meetingportal.meetComEclM.EclParamM;
import de.meetingapps.meetingportal.meetComKonst.KonstPortalFehlerView;
import de.meetingapps.meetingportal.meetComKonst.KonstPortalView;
import de.meetingapps.meetingportal.meetingportTFunktionen.TFunktionen;
import de.meetingapps.meetingportal.meetingportTFunktionen.TPruefeStartNachOpen;
import de.meetingapps.meetingportal.meetingportTFunktionen.TSessionVerwaltung;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@RequestScoped
@Named
public class TStimmabgabe {

    private @Inject EclAbstimmungSetM eclAbstimmungSetM;
    private @Inject BlMAbstimmung blMAbstimmung;
    private @Inject TSessionVerwaltung tSessionVerwaltung;
    private @Inject EclDbM eclDbM;
    private @Inject TFunktionen tFunktionen;
    private @Inject EclBesitzGesamtAuswahl1M eclBesitzGesamtAuswahl1M;
    private @Inject TSession tSession;
    private @Inject TAuswahlSession tAuswahlSession;
    private @Inject TStimmabgabeSession tStimmabgabeSession;
    private @Inject EclAbstimmungenListeM eclAbstimmungenListeM;
    private @Inject EclParamM eclParamM;
    private @Inject BlMWillenserklaerungStatusNeuVerarbeiten blWillenserklaerungStatusNeuVerarbeitenM;
    private @Inject EclBesitzGesamtM eclBesitzGesamtM;
    private @Inject TFehlerViewSession tFehlerViewSession;
    private @Inject TPruefeStartNachOpen tPruefeStartNachOpen;

    /**eclDbM öffnen/schließen in aufrufender Funktion*/
    public int startStimmabgabe() {
        /*Funktion vorhanden?*/
        if (eclBesitzGesamtAuswahl1M.getAnzPraesenteVorhanden() <= 0
                || tAuswahlSession.isOnlineteilnahmeAktivUB() == false) {
            return CaFehler.afFunktionNichtAuswaehlbar;
        }

        /*Abstimmung aktiv?*/
        if (eclAbstimmungSetM.getAbstimmungSet().aktiverAbstimmungsblockIstElektronischAktiv == false) {
            return CaFehler.afDerzeitKeineAbstimmungEroeffnet;
        }

        /*Abstimmungssperre Hybridveranstaltung aktiv?*/
        if (eclParamM.getParam().paramPortal.onlineAbstimmungBerechtigungSeparatPruefen==1) {
            blWillenserklaerungStatusNeuVerarbeitenM.setBesitzJeKennungListe(eclBesitzGesamtM.getBesitzJeKennungListePraesent());
            blWillenserklaerungStatusNeuVerarbeitenM.pruefeObGesperrt();
            if (blWillenserklaerungStatusNeuVerarbeitenM.isRcStimmabgabeFuerMeldungGesperrt()) {
                return CaFehler.afMeldungFuerAbstimmungWgVorOrtPraesenzGesperrt;
            }
        }
        
        /*Abstimmungsliste für Abstimmung vorbereiten*/
        blMAbstimmung.leseAbstimmungsliste(eclBesitzGesamtM.getGattungen());

        if (eclAbstimmungenListeM.getAbstimmungenListeM().size() == 0
                && eclAbstimmungenListeM.getGegenantraegeListeM().size() == 0) {
            return CaFehler.afNichtStimmberechtigt;
        }

        return 1;
    }

    public void resetEingabe() {
        for (int i = 0; i < eclAbstimmungenListeM.getAbstimmungenListeM().size(); i++) {
            eclAbstimmungenListeM.getAbstimmungenListeM().get(i).setGewaehlt("");
        }
        for (int i = 0; i < eclAbstimmungenListeM.getGegenantraegeListeM().size(); i++) {
            eclAbstimmungenListeM.getGegenantraegeListeM().get(i).setGewaehlt("");
        }
    }

    public boolean startAusgewaehlte() {

        eclDbM.openAll();
        eclDbM.closeAll();
        if (eclAbstimmungSetM.getAbstimmungSet().aktiverAbstimmungsblockIstElektronischAktiv == false) {
            tFehlerViewSession.setFehlerArt(3);
            tFehlerViewSession.setNextView(tFunktionen.waehleAuswahlNachPraesenzfunktion());
            tSessionVerwaltung.setzeEnde(KonstPortalView.FEHLER_VIEW);
            return false;

        }

        if (eclAbstimmungSetM.getVersionAbstimmungenAktuell() != eclAbstimmungSetM.getVersionAbstimmungenStart()) {
            tFehlerViewSession.setFehlerArt(4);
            tFehlerViewSession.setNextView(tFunktionen.waehleAuswahlNachPraesenzfunktion());
            tSessionVerwaltung.setzeEnde(KonstPortalView.FEHLER_VIEW);
            return false;

        }

        tStimmabgabeSession.setDurchfuehrenFuerAlle(false);
        blWillenserklaerungStatusNeuVerarbeitenM.setBesitzJeKennungListe(eclBesitzGesamtM.getBesitzJeKennungListePraesent());
        blWillenserklaerungStatusNeuVerarbeitenM.pruefeAufAusgewaehltUndBelegeAusgewaehltListe(tStimmabgabeSession.isDurchfuehrenFuerAlle());
        if (blWillenserklaerungStatusNeuVerarbeitenM.isRcAusgewaehlteVorhanden() == false) {
            tSession.trageFehlerEin(CaFehler.afBestandFuerStimmabgabeAuswaehlen);
            return false;
        }
        resetEingabe();
        return true;
    }

    public boolean startAlle() {

        eclDbM.openAll();
        eclDbM.closeAll();
        if (eclAbstimmungSetM.getAbstimmungSet().aktiverAbstimmungsblockIstElektronischAktiv == false) {
            tFehlerViewSession.setFehlerArt(3);
            tFehlerViewSession.setNextView(tFunktionen.waehleAuswahlNachPraesenzfunktion());
            tSessionVerwaltung.setzeEnde(KonstPortalView.FEHLER_VIEW);
            return false;

        }

        if (eclAbstimmungSetM.getVersionAbstimmungenAktuell() != eclAbstimmungSetM.getVersionAbstimmungenStart()) {
            tFehlerViewSession.setFehlerArt(4);
            tFehlerViewSession.setNextView(tFunktionen.waehleAuswahlNachPraesenzfunktion());
            tSessionVerwaltung.setzeEnde(KonstPortalView.FEHLER_VIEW);
            return false;

        }

        tStimmabgabeSession.setDurchfuehrenFuerAlle(true);
        blWillenserklaerungStatusNeuVerarbeitenM.setBesitzJeKennungListe(eclBesitzGesamtM.getBesitzJeKennungListePraesent());
        blWillenserklaerungStatusNeuVerarbeitenM.pruefeAufAusgewaehltUndBelegeAusgewaehltListe(tStimmabgabeSession.isDurchfuehrenFuerAlle());
        resetEingabe();
        return true;
    }

    /*******************************Buttons**************************************************/
    /*++++++++++iStimmabgabe++++++++++++*/

    public void doStimmeAbgeben() {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.STIMMABGABE)) {
            return;
        }

        eclDbM.openAll();

        if (!tPruefeStartNachOpen.pruefeStartNachOpen(KonstPortalView.AUSWAHL1)) {
            tSessionVerwaltung.setzeEnde();
            eclDbM.closeAll();
            return;
        }

        if (eclAbstimmungSetM.getAbstimmungSet().aktiverAbstimmungsblockIstElektronischAktiv == false) {
            eclDbM.closeAll();
            tFehlerViewSession.setFehlerArt(3);
            tFehlerViewSession.setNextView(tFunktionen.waehleAuswahlNachPraesenzfunktion());
            tSessionVerwaltung.setzeEnde(KonstPortalView.FEHLER_VIEW);
            return;

        }

        if (eclAbstimmungSetM.getVersionAbstimmungenAktuell() != eclAbstimmungSetM.getVersionAbstimmungenStart()) {
            eclDbM.closeAll();
            tFehlerViewSession.setFehlerArt(4);
            tFehlerViewSession.setNextView(tFunktionen.waehleAuswahlNachPraesenzfunktion());
            tSessionVerwaltung.setzeEnde(KonstPortalView.FEHLER_VIEW);
            return;

        }

        /*Abstimmungssperre Hybridveranstaltung aktiv?*/
        if (eclParamM.getParam().paramPortal.onlineAbstimmungBerechtigungSeparatPruefen==1) {
            blWillenserklaerungStatusNeuVerarbeitenM.setBesitzJeKennungListe(eclBesitzGesamtM.getBesitzJeKennungListePraesent());
            blWillenserklaerungStatusNeuVerarbeitenM.pruefeObGesperrt();
            if (blWillenserklaerungStatusNeuVerarbeitenM.isRcStimmabgabeFuerMeldungGesperrt()) {
                eclDbM.closeAll();
                tFehlerViewSession.setFehlerArt(KonstPortalFehlerView.STIMMABGABE_NICHT_MOEGLICH_DA_VOR_ORT_PRAESENT);
                tFehlerViewSession.setNextView(tFunktionen.waehleAuswahlNachPraesenzfunktion());
                tSessionVerwaltung.setzeEnde(KonstPortalView.FEHLER_VIEW);
                return;
            }
        }

        List<EclAbstimmungM> lAbstimmungenListe = eclAbstimmungenListeM.getAbstimmungenListeM();
        int[] anzahlJeGruppe = new int[11];
        for (int i1 = 1; i1 <= 10; i1++) {
            anzahlJeGruppe[i1] = 0;
        }
        /*Gruppe überprüfen*/
        for (int i = 0; i < lAbstimmungenListe.size(); i++) {
            if (!lAbstimmungenListe.get(i).isUeberschrift()) {

                int gruppe = lAbstimmungenListe.get(i).getZuAbstimmungsgruppe();
                //					System.out.println("Abstimmungsgruppe="+gruppe);
                if (gruppe != 0) {
                    if (lAbstimmungenListe.get(i).getGewaehlt() != null
                            && lAbstimmungenListe.get(i).getGewaehlt().compareTo("J") == 0) {
                        //							System.out.println("Gruppe++");
                        anzahlJeGruppe[gruppe]++;
                    }
                }
            }
        }

        /*Checken: nicht markierte je nach Parameterstellung auf Enthaltung setzen*/
        if (eclParamM.getParam().paramPortal.pNichtmarkiertSpeichernAls != 0) {
            String nichtmarkierteErsetzenDurch = "";
            switch (eclParamM.getParam().paramPortal.pNichtmarkiertSpeichernAls) {
            case 1:
                nichtmarkierteErsetzenDurch = "J";
                break;
            case 2:
                nichtmarkierteErsetzenDurch = "N";
                break;
            case 3:
                nichtmarkierteErsetzenDurch = "E";
                break;
            }
            for (int i = 0; i < lAbstimmungenListe.size(); i++) {
                if (!lAbstimmungenListe.get(i).isUeberschrift()) {
                    if (lAbstimmungenListe.get(i).getGewaehlt() == null
                            || lAbstimmungenListe.get(i).getGewaehlt().isEmpty()) {
                        lAbstimmungenListe.get(i).setGewaehlt(nichtmarkierteErsetzenDurch);
                    }
                }
            }
        }

        for (int i1 = 1; i1 <= 10; i1++) {
            if (anzahlJeGruppe[i1] > eclParamM.getParam().paramAbstimmungParameter.anzahlJaJeAbstimmungsgruppe[i1]) {
                switch (i1) {
                case 1:
                    tSession.trageFehlerEin(CaFehler.afAbstimmgruppe1ZuViel);
                    break;
                case 2:
                    tSession.trageFehlerEin(CaFehler.afAbstimmgruppe2ZuViel);
                    break;
                case 3:
                    tSession.trageFehlerEin(CaFehler.afAbstimmgruppe3ZuViel);
                    break;
                case 4:
                    tSession.trageFehlerEin(CaFehler.afAbstimmgruppe4ZuViel);
                    break;
                case 5:
                    tSession.trageFehlerEin(CaFehler.afAbstimmgruppe5ZuViel);
                    break;
                case 6:
                    tSession.trageFehlerEin(CaFehler.afAbstimmgruppe6ZuViel);
                    break;
                case 7:
                    tSession.trageFehlerEin(CaFehler.afAbstimmgruppe7ZuViel);
                    break;
                case 8:
                    tSession.trageFehlerEin(CaFehler.afAbstimmgruppe8ZuViel);
                    break;
                case 9:
                    tSession.trageFehlerEin(CaFehler.afAbstimmgruppe9ZuViel);
                    break;
                case 10:
                    tSession.trageFehlerEin(CaFehler.afAbstimmgruppe10ZuViel);
                    break;
                }
                tSessionVerwaltung.setzeEnde();
                tSessionVerwaltung.setzeEnde(KonstPortalView.STIMMABGABE);
                return;
            }
        }

        blWillenserklaerungStatusNeuVerarbeitenM.setBesitzJeKennungListe(eclBesitzGesamtM.getBesitzJeKennungListePraesent());
        blWillenserklaerungStatusNeuVerarbeitenM.stimmAbgabe(tStimmabgabeSession.isDurchfuehrenFuerAlle());

        eclDbM.closeAll();

        tSessionVerwaltung.setzeEnde(KonstPortalView.STIMMABGABE_QUITTUNG);
        return;

    }

    public void doEingabeZurueck() {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.STIMMABGABE)) {
            return;
        }

        tSession.trageFehlerEin(CaFehler.afStimmabgabeVomBenutzerAbgebrochen);
        tSessionVerwaltung.setzeEnde(tFunktionen.waehleAuswahlNachPraesenzfunktion());
        return;

    }

    /*++++++++++++++++iStimmabgabeQuittung+++++++++++++++++++*/
    public void doWeiterQuittung() {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.STIMMABGABE_QUITTUNG)) {
            return;
        }

        if (eclBesitzGesamtAuswahl1M.getAnzPraesenteVorhanden() == 1) {
            tSessionVerwaltung.setzeEnde(tFunktionen.waehleAuswahlNachPraesenzfunktion());
        } else {
            tSessionVerwaltung.setzeEnde(KonstPortalView.STIMMABGABE_AUSWAHL);
        }
        return;

    }

    /*+++++++++++++iStimmabgabeAuswahl++++++++++++++++++*/
    public void doStimmabgabeAlle() {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.STIMMABGABE_AUSWAHL)) {
            return;
        }
        boolean brc = startAlle();
        if (brc == false) {
            tSessionVerwaltung.setzeEnde();
            return;
        }
        tSessionVerwaltung.setzeEnde(KonstPortalView.STIMMABGABE);
        return;
    }

    public void doStimmabgabeAusgewaehlte() {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.STIMMABGABE_AUSWAHL)) {
            return;
        }
        boolean brc = startAusgewaehlte();
        if (brc == false) {
            tSessionVerwaltung.setzeEnde();
            return;
        }
        tSessionVerwaltung.setzeEnde(KonstPortalView.STIMMABGABE);
        return;
    }

    public void doBeendenStimmabgabeAuswahl() {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.STIMMABGABE_AUSWAHL)) {
            return;
        }

        tSessionVerwaltung.setzeEnde(tFunktionen.waehleAuswahlNachPraesenzfunktion());
        return;
    }

    /*+++++++++++++iStimmabgabeDerzeitNichtMoeglich++++++++++++++++++++++++*/

    public void doNichtMoeglichZurueck() {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.STIMMABGABE_DERZEIT_NICHT_MOEGLICH)) {
            return;
        }

        tSessionVerwaltung.setzeEnde(tFunktionen.waehleAuswahlNachPraesenzfunktion());
        return;

    }

    public void doNichtMoeglichAktualisieren() {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.STIMMABGABE_DERZEIT_NICHT_MOEGLICH)) {
            return;
        }
        eclDbM.openAll();

        if (!tPruefeStartNachOpen.pruefeStartNachOpen(KonstPortalView.AUSWAHL1)) {
            tSessionVerwaltung.setzeEnde();
            eclDbM.closeAll();
            return;
        }

        int rc = startStimmabgabe();

        eclDbM.closeAll();
        if (rc == CaFehler.afDerzeitKeineAbstimmungEroeffnet || 
                rc==CaFehler.afNichtStimmberechtigt) {
            tSessionVerwaltung.setzeEnde(KonstPortalView.STIMMABGABE_DERZEIT_NICHT_MOEGLICH);
            return;
        }
        if (rc < 0) {
            switch (rc) {
//            case CaFehler.afNichtStimmberechtigt:
            case CaFehler.afFunktionNichtAuswaehlbar:
            default:
                tSession.trageFehlerEin(rc);
                tSessionVerwaltung.setzeEnde(KonstPortalView.fehlerSysLogout);
                return;
            }
        }

        if (eclBesitzGesamtAuswahl1M.getAnzPraesenteVorhanden() == 1) {
            startAlle();
            tSessionVerwaltung.setzeEnde(KonstPortalView.STIMMABGABE);
        } else {
            tSessionVerwaltung.setzeEnde(KonstPortalView.STIMMABGABE_AUSWAHL);
        }
        return;

    }
}
