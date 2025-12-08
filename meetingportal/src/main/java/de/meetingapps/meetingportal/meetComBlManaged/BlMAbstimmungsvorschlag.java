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
package de.meetingapps.meetingportal.meetComBlManaged;

import java.util.List;

import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEclM.EclAbstimmungM;
import de.meetingapps.meetingportal.meetComEclM.EclAbstimmungenListeM;
import de.meetingapps.meetingportal.meetComEclM.EclKIAVM;
import de.meetingapps.meetingportal.meetComKonst.KonstStimmart;
import de.meetingapps.meetingportal.meetingportTController.TWillenserklaerungSession;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@RequestScoped
@Named
public class BlMAbstimmungsvorschlag {

    private @Inject TWillenserklaerungSession tWillenserklaerungSession;
    private @Inject EclAbstimmungenListeM eclAbstimmungenListeM;
    private @Inject EclKIAVM eclKIAVM;

    /**Füllen der EclAbstimmungenListeM mit dem 
     * Abstimmungsvorschlag des Stimmrechtsvertreters der Gesellschaft
     * 
     * Eingabeparameter:
     * 		EclAbstimmungenListeM (bereits gefüllt mit Abstimmungen)
     * 		EclKIAVM
     * 
     * Ausgabeparameter:
     * 		EclAbstimmungenListeM (ergänzt mit AbstimmungsvorschlagGesellschaft)
     * 
     * Hinweis: der komplette Abstimmungsvorschlag muß in Sammelkarte mit Gattung 1 stehen!
     */
    public void leseAbstimmungsvorschlagGesellschaft(DbBundle lDbBundle) {

        lDbBundle.dbMeldungen.leseSammelkarteSRVInternet(
                1); /*Sammelkartennr für Vollmacht/Weisung ermitteln - enthält auch den Abstimmungsvorschlag der Gesellschaft*/

        /*Todo #0 - nochmal überprüfen in Ruhe*/
        if (lDbBundle.dbMeldungen.meldungenArray.length == 0) {
            lDbBundle.dbMeldungen.leseSammelkarteSRVPapier(
                    1); /*Sammelkartennr für Vollmacht/Weisung ermitteln - enthält auch den Abstimmungsvorschlag der Gesellschaft*/
        }

        lDbBundle.dbAbstimmungsVorschlag.leseZuSammelIdent(lDbBundle.dbMeldungen.meldungenArray[0].meldungsIdent);

        if (lDbBundle.dbAbstimmungsVorschlag.abstimmungsVorschlagArray.length > 0) { /*Abstimmungsvorschlag vorhanden*/
            tWillenserklaerungSession.setAbstimmungsVorschlagIdent(
                    lDbBundle.dbAbstimmungsVorschlag.abstimmungsVorschlagArray[0].abstimmungsVorschlagIdent);
        } else {
            tWillenserklaerungSession.setAbstimmungsVorschlagIdent(0);
        }

        List<EclAbstimmungM> lAbstimmungenListe = eclAbstimmungenListeM.getAbstimmungenListeM();

        for (int i = 0; i < lAbstimmungenListe.size(); i++) {

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

        for (int i = 0; i < lGegenantraegeListe.size(); i++) {

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

        lDbBundle.dbAbstimmungsVorschlag.leseZuSammelIdent(eclKIAVM.getMeldeIdent());

        if (lDbBundle.dbAbstimmungsVorschlag.abstimmungsVorschlagArray.length > 0) { /*Abstimmungsvorschlag vorhanden*/
            tWillenserklaerungSession.setAbstimmungsVorschlagIdent(
                    lDbBundle.dbAbstimmungsVorschlag.abstimmungsVorschlagArray[0].abstimmungsVorschlagIdent);
        } else {
            tWillenserklaerungSession.setAbstimmungsVorschlagIdent(0);
        }

        List<EclAbstimmungM> lAbstimmungenListe = eclAbstimmungenListeM.getAbstimmungenListeM();

        for (int i = 0; i < lAbstimmungenListe.size(); i++) {

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

        for (int i = 0; i < lGegenantraegeListe.size(); i++) {

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

}
