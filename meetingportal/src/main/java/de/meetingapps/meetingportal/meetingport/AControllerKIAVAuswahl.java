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

import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEclM.EclAbstimmungM;
import de.meetingapps.meetingportal.meetComEclM.EclAbstimmungenListeM;
import de.meetingapps.meetingportal.meetComEclM.EclDbM;
import de.meetingapps.meetingportal.meetComEclM.EclKIAVM;
import de.meetingapps.meetingportal.meetComEclM.EclTeilnehmerLoginM;
import de.meetingapps.meetingportal.meetComKonst.KonstStimmart;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@RequestScoped
@Named
@Deprecated
public class AControllerKIAVAuswahl {

    @Inject
    ADlgVariablen aDlgVariablen;
    @Inject
    AFunktionen aFunktionen;
    @Inject
    private EclDbM eclDbM;

    @Inject
    EclTeilnehmerLoginM eclTeilnehmerLoginM;
    @Inject
    EclKIAVM eclKIAVM;

    @Inject
    EclAbstimmungenListeM eclAbstimmungenListeM;

    private void setzeGewaehltesKIAV(EclKIAVM kiav) {

        eclKIAVM.copyFromM(kiav);
        //		eclKIAVM.setMeldeIdent(kiav.getMeldeIdent());
        //		eclKIAVM.setKurzText(kiav.getKurzText());
        //		eclKIAVM.setMehrereVarianten(kiav.isMehrereVarianten());
        //		eclKIAVM.setAkzeptiertOhneWeisung(kiav.isAkzeptiertOhneWeisung());
        //		eclKIAVM.setAkzeptiertDedizierteWeisung(kiav.isAkzeptiertDedizierteWeisung());
        //		eclKIAVM.setAkzeptiertWieVorschlag(kiav.isAkzeptiertWieVorschlag());

    }

    /**Füllen der EclAbstimmungenListeM mit dem 
     * Abstimmungsvorschlag des Stimmrechtsvertreters der Gesellschaft
     * 
     * Eingabeparameter:
     * 		EclAbstimmungenListeM (bereits gefüllt mit Abstimmungen)
     * 		EclKIAVM
     * 
     * Ausgabeparameter:
     * 		EclAbstimmungenListeM (ergänzt mit AbstimmungsvorschlagGesellschaft)
     */
    public void leseAbstimmungsvorschlagGesellschaft(DbBundle lDbBundle) {
        int i;

        System.out.println("Gattung=" + eclTeilnehmerLoginM.getGattung());
        if (eclTeilnehmerLoginM.getGattung() == 0) {
            eclTeilnehmerLoginM.setGattung(1);
        } /*TODO #KOnsolidierung: wird von App oder so möglicherweise nicht richtig geliefert*/
        lDbBundle.dbMeldungen.leseSammelkarteSRVInternet(eclTeilnehmerLoginM
                .getGattung()); /*Sammelkartennr für Vollmacht/Weisung ermitteln - enthält auch den Abstimmungsvorschlag der Gesellschaft*/

        /*Todo #0 - nochmal überprüfen in Ruhe*/
        if (lDbBundle.dbMeldungen.meldungenArray.length == 0) {
            lDbBundle.dbMeldungen.leseSammelkarteSRVPapier(eclTeilnehmerLoginM
                    .getGattung()); /*Sammelkartennr für Vollmacht/Weisung ermitteln - enthält auch den Abstimmungsvorschlag der Gesellschaft*/

        }

        System.out.println("Anzahl=" + lDbBundle.dbMeldungen.meldungenArray.length);
        lDbBundle.dbAbstimmungsVorschlag.leseZuSammelIdent(lDbBundle.dbMeldungen.meldungenArray[0].meldungsIdent);

        if (lDbBundle.dbAbstimmungsVorschlag.abstimmungsVorschlagArray.length > 0) { /*Abstimmungsvorschlag vorhanden*/
            eclKIAVM.setAbstimmungsVorschlagIdent(
                    lDbBundle.dbAbstimmungsVorschlag.abstimmungsVorschlagArray[0].abstimmungsVorschlagIdent);
        } else {
            eclKIAVM.setAbstimmungsVorschlagIdent(0);
        }

        List<EclAbstimmungM> lAbstimmungenListe = eclAbstimmungenListeM.getAbstimmungenListeM();

        for (i = 0; i < lAbstimmungenListe.size(); i++) {

            int posInWeisung = lAbstimmungenListe.get(i).getIdentWeisungssatz();
            if (posInWeisung != -1) {
                if (lDbBundle.dbAbstimmungsVorschlag.abstimmungsVorschlagArray.length == 0) { /*Kein Abstimmungsvorschlag vorhanden*/
                    lAbstimmungenListe.get(i).setAbstimmungsvorschlagGesellschaft("-");
                } else {/*Abstimmungsvorschlag vorhanden - in lAbstimmungenListe übertragen*/
                    switch (lDbBundle.dbAbstimmungsVorschlag.abstimmungsVorschlagArray[0].abgabe[posInWeisung]) {
                    case KonstStimmart.ja:
                        lAbstimmungenListe.get(i).setAbstimmungsvorschlagGesellschaft("J");
                        break;
                    case KonstStimmart.nein:
                        lAbstimmungenListe.get(i).setAbstimmungsvorschlagGesellschaft("N");
                        break;
                    case KonstStimmart.enthaltung:
                        lAbstimmungenListe.get(i).setAbstimmungsvorschlagGesellschaft("E");
                        break;
                    case KonstStimmart.nichtMarkiert:
                        lAbstimmungenListe.get(i).setAbstimmungsvorschlagGesellschaft("-");
                        break;
                    }
                }
            }
        }

        List<EclAbstimmungM> lGegenantraegeListe = eclAbstimmungenListeM.getGegenantraegeListeM();

        for (i = 0; i < lGegenantraegeListe.size(); i++) {

            int posInWeisung = lGegenantraegeListe.get(i).getIdentWeisungssatz();
            if (posInWeisung != -1) {

                if (lDbBundle.dbAbstimmungsVorschlag.abstimmungsVorschlagArray.length == 0) { /*Kein Abstimmungsvorschlag vorhanden*/
                    lGegenantraegeListe.get(i).setAbstimmungsvorschlagGesellschaft("-");
                } else {/*Abstimmungsvorschlag vorhanden - in lAbstimmungenListe übertragen*/
                    switch (lDbBundle.dbAbstimmungsVorschlag.abstimmungsVorschlagArray[0].abgabe[posInWeisung]) {
                    case KonstStimmart.ja:
                        lGegenantraegeListe.get(i).setAbstimmungsvorschlagGesellschaft("J");
                        break;
                    case KonstStimmart.nein:
                        lGegenantraegeListe.get(i).setAbstimmungsvorschlagGesellschaft("N");
                        break;
                    case KonstStimmart.enthaltung:
                        lGegenantraegeListe.get(i).setAbstimmungsvorschlagGesellschaft("E");
                        break;
                    case KonstStimmart.nichtMarkiert:
                        lGegenantraegeListe.get(i).setAbstimmungsvorschlagGesellschaft("-");
                        break;
                    case KonstStimmart.gegenantragWirdUnterstuetzt:
                        lGegenantraegeListe.get(i).setAbstimmungsvorschlagGesellschaft("X");
                        break;
                    }
                }
            }
        }

    }

    /**Füllen der EclAbstimmungenListeM mit dem 
     * Abstimmungsvorschlag derjenigen KIAV, die in EclKIAVM spezifiziert ist.
     * 
     * Eingabeparameter:
     * 		EclAbstimmungenListeM (bereits gefüllt mit Abstimmungen)
     * 		EclKIAVM
     * 
     * Ausgabeparameter:
     * 		EclAbstimmungenListeM (ergänzt mit Abstimmungsvorschlag)
     * 
     * Hinweis: lDbBundle wird innerhalb dieser Funktion geöffnet/geschlossen
     */

    void leseAbstimmungsvorschlag(DbBundle lDbBundle) {
        int i;

        lDbBundle.dbAbstimmungsVorschlag.leseZuSammelIdent(eclKIAVM.getMeldeIdent());

        if (lDbBundle.dbAbstimmungsVorschlag.abstimmungsVorschlagArray.length > 0) { /*Abstimmungsvorschlag vorhanden*/
            eclKIAVM.setAbstimmungsVorschlagIdent(
                    lDbBundle.dbAbstimmungsVorschlag.abstimmungsVorschlagArray[0].abstimmungsVorschlagIdent);
        } else {
            eclKIAVM.setAbstimmungsVorschlagIdent(0);
        }

        List<EclAbstimmungM> lAbstimmungenListe = eclAbstimmungenListeM.getAbstimmungenListeM();

        for (i = 0; i < lAbstimmungenListe.size(); i++) {

            int posInWeisung = lAbstimmungenListe.get(i).getIdentWeisungssatz();
            if (posInWeisung != -1) {
                if (lDbBundle.dbAbstimmungsVorschlag.abstimmungsVorschlagArray.length == 0) { /*Kein Abstimmungsvorschlag vorhanden*/
                    lAbstimmungenListe.get(i).setAbstimmungsvorschlag("-");
                } else {/*Abstimmungsvorschlag vorhanden - in lAbstimmungenListe übertragen*/
                    switch (lDbBundle.dbAbstimmungsVorschlag.abstimmungsVorschlagArray[0].abgabe[posInWeisung]) {
                    case KonstStimmart.ja:
                        lAbstimmungenListe.get(i).setAbstimmungsvorschlag("J");
                        break;
                    case KonstStimmart.nein:
                        lAbstimmungenListe.get(i).setAbstimmungsvorschlag("N");
                        break;
                    case KonstStimmart.enthaltung:
                        lAbstimmungenListe.get(i).setAbstimmungsvorschlag("E");
                        break;
                    case KonstStimmart.nichtMarkiert:
                        lAbstimmungenListe.get(i).setAbstimmungsvorschlag("-");
                        break;
                    }
                }
            }
        }

        List<EclAbstimmungM> lGegenantraegeListe = eclAbstimmungenListeM.getGegenantraegeListeM();

        for (i = 0; i < lGegenantraegeListe.size(); i++) {

            int posInWeisung = lGegenantraegeListe.get(i).getIdentWeisungssatz();
            if (posInWeisung != -1) {

                if (lDbBundle.dbAbstimmungsVorschlag.abstimmungsVorschlagArray.length == 0) { /*Kein Abstimmungsvorschlag vorhanden*/
                    lGegenantraegeListe.get(i).setAbstimmungsvorschlag("-");
                } else {/*Abstimmungsvorschlag vorhanden - in lAbstimmungenListe übertragen*/
                    switch (lDbBundle.dbAbstimmungsVorschlag.abstimmungsVorschlagArray[0].abgabe[posInWeisung]) {
                    case KonstStimmart.ja:
                        lGegenantraegeListe.get(i).setAbstimmungsvorschlag("J");
                        break;
                    case KonstStimmart.nein:
                        lGegenantraegeListe.get(i).setAbstimmungsvorschlag("N");
                        break;
                    case KonstStimmart.enthaltung:
                        lGegenantraegeListe.get(i).setAbstimmungsvorschlag("E");
                        break;
                    case KonstStimmart.nichtMarkiert:
                        lGegenantraegeListe.get(i).setAbstimmungsvorschlag("-");
                        break;
                    case KonstStimmart.gegenantragWirdUnterstuetzt:
                        lGegenantraegeListe.get(i).setAbstimmungsvorschlag("X");
                        break;
                    }
                }
            }
        }

    }

    public String doVollmacht(EclKIAVM kiav) {
        if (!aFunktionen.pruefeStart("aKiavAuswahl")) {
            return "aDlgFehler";
        }
        aDlgVariablen.setAusgewaehlteAktion("7");
        setzeGewaehltesKIAV(kiav);
        return aFunktionen.setzeEnde("aKiavVollmacht", true, false);
    }

    public String doVollmachtWeisung(EclKIAVM kiav) {
        if (!aFunktionen.pruefeStart("aKiavAuswahl")) {
            return "aDlgFehler";
        }
        aDlgVariablen.setAusgewaehlteAktion("8");
        setzeGewaehltesKIAV(kiav);
        eclDbM.openAll();
        leseAbstimmungsvorschlag(eclDbM.getDbBundle());
        leseAbstimmungsvorschlagGesellschaft(eclDbM.getDbBundle());
        eclDbM.closeAll();
        return aFunktionen.setzeEnde("aKiavVollmacht", true, false);
    }

    public String doVollmachtWeisungVorschlag(EclKIAVM kiav) {
        if (!aFunktionen.pruefeStart("aKiavAuswahl")) {
            return "aDlgFehler";
        }
        aDlgVariablen.setAusgewaehlteAktion("9");
        setzeGewaehltesKIAV(kiav);
        eclDbM.openAll();
        leseAbstimmungsvorschlag(eclDbM.getDbBundle());
        eclDbM.closeAll();
        return aFunktionen.setzeEnde("aKiavVollmacht", true, false);
    }

    public String doZurueck() {
        if (!aFunktionen.pruefeStart("aKiavAuswahl")) {
            return "aDlgFehler";
        }
        if (aDlgVariablen.getAusgewaehlteHauptAktion().compareTo("1") == 0) {
            return aFunktionen.setzeEnde("aAnmelden", true, true);
        } else {
            return aFunktionen.setzeEnde("aStatus", true, true);
        }
    }

    /**Abmelden*/
    public String doAbmelden() {
        aDlgVariablen.clearLogin();
        return aFunktionen.waehleLogout();
    }

}
