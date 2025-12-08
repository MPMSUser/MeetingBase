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
package de.meetingapps.meetingportal.meetComEE;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEclM.EclAbstimmungSplitM;
import de.meetingapps.meetingportal.meetComEclM.EclAbstimmungenSplitListeM;
import de.meetingapps.meetingportal.meetComEntities.EclAbstimmung;
import de.meetingapps.meetingportal.meetComEntities.EclWeisungMeldung;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@SessionScoped
@Named
public class BlAbstimmungenSplitListeM implements Serializable {
    private static final long serialVersionUID = -4004139200446034694L;

    @Inject
    EclAbstimmungenSplitListeM eclAbstimmungenSplitListeM;

    /**Liest alle Abstimmungen aus DB ein und übernimmt diese in eclAbstimmungenSplitListeM**/
    public void leseListe(DbBundle lDbBundle) {

        int i;
//        int erg;

        EclAbstimmung lAbstimmung = null;
        EclAbstimmungSplitM lAbstimmungSplitM = null;

        /*Initialisieren*/

        /*Agenda*/
        /*erg =*/ lDbBundle.dbAbstimmungen.leseWeisungenPortalAgenda();

        List<EclAbstimmungSplitM> lAbstimmungenSplitListe = new LinkedList<>();

        for (i = 0; i < lDbBundle.dbAbstimmungen.abstimmungenArray.length; i++) {
            lAbstimmung = lDbBundle.dbAbstimmungen.abstimmungenArray[i];
            lAbstimmungSplitM = new EclAbstimmungSplitM();
            lAbstimmungSplitM.copyFrom(lAbstimmung);
            lAbstimmungenSplitListe.add(lAbstimmungSplitM);
        }

        eclAbstimmungenSplitListeM.setAbstimmungenSplitListeM(lAbstimmungenSplitListe);

        /*Gegenanträge*/
        /*erg =*/ lDbBundle.dbAbstimmungen.leseWeisungenPortalGegenantraege();

        List<EclAbstimmungSplitM> lGegenantraegeSplitListe = new LinkedList<>();

        for (i = 0; i < lDbBundle.dbAbstimmungen.abstimmungenArray.length; i++) {
            lAbstimmung = lDbBundle.dbAbstimmungen.abstimmungenArray[i];
            lAbstimmungSplitM = new EclAbstimmungSplitM();
            lAbstimmungSplitM.copyFrom(lAbstimmung);
            lGegenantraegeSplitListe.add(lAbstimmungSplitM);
        }

        eclAbstimmungenSplitListeM.setGegenantraegeSplitListeM(lGegenantraegeSplitListe);

    }

    /**Einlesen der Split-Weisungen aus DB und Ergänzen in eclAbstimmungenSplitListeM
     * Voraussetzung: leseListe wurde vorher aufgerufen!*/
    public void leseWeisungen(EclWeisungMeldung pWeisungMeldung) {
        int i;

        List<EclAbstimmungSplitM> lAbstimmungenSplitListe = eclAbstimmungenSplitListeM.getAbstimmungenSplitListeM();

        for (i = 0; i < lAbstimmungenSplitListe.size(); i++) {

            int posInWeisung = lAbstimmungenSplitListe.get(i).getIdentWeisungssatz();
            if (!lAbstimmungenSplitListe.get(i).isUeberschrift()) {
                lAbstimmungenSplitListe.get(i)
                        .setStimmenJa(Long.toString(pWeisungMeldung.weisungMeldungSplit.abgabe[posInWeisung][1]));
                lAbstimmungenSplitListe.get(i)
                        .setStimmenNein(Long.toString(pWeisungMeldung.weisungMeldungSplit.abgabe[posInWeisung][2]));
                lAbstimmungenSplitListe.get(i).setStimmenEnthaltung(
                        Long.toString(pWeisungMeldung.weisungMeldungSplit.abgabe[posInWeisung][3]));
                lAbstimmungenSplitListe.get(i).setStimmenUngueltig(
                        Long.toString(pWeisungMeldung.weisungMeldungSplit.abgabe[posInWeisung][4]));
                lAbstimmungenSplitListe.get(i).setStimmenNichtTeilnahme(
                        Long.toString(pWeisungMeldung.weisungMeldungSplit.abgabe[posInWeisung][5]));
                lAbstimmungenSplitListe.get(i)
                        .setStimmenFrei(Long.toString(pWeisungMeldung.weisungMeldungSplit.abgabe[posInWeisung][7]));
                lAbstimmungenSplitListe.get(i).setStimmenStimmausschluss(
                        Long.toString(pWeisungMeldung.weisungMeldungSplit.abgabe[posInWeisung][9]));

            }
        }

        List<EclAbstimmungSplitM> lGegenantraegeSplitListe = eclAbstimmungenSplitListeM.getGegenantraegeSplitListeM();

        for (i = 0; i < lGegenantraegeSplitListe.size(); i++) {

            int posInWeisung = lGegenantraegeSplitListe.get(i).getIdentWeisungssatz();

            if (!lGegenantraegeSplitListe.get(i).isUeberschrift()) {
                lGegenantraegeSplitListe.get(i)
                        .setStimmenJa(Long.toString(pWeisungMeldung.weisungMeldungSplit.abgabe[posInWeisung][1]));
                lGegenantraegeSplitListe.get(i)
                        .setStimmenNein(Long.toString(pWeisungMeldung.weisungMeldungSplit.abgabe[posInWeisung][2]));
                lGegenantraegeSplitListe.get(i).setStimmenEnthaltung(
                        Long.toString(pWeisungMeldung.weisungMeldungSplit.abgabe[posInWeisung][3]));
                lGegenantraegeSplitListe.get(i).setStimmenUngueltig(
                        Long.toString(pWeisungMeldung.weisungMeldungSplit.abgabe[posInWeisung][4]));
                lGegenantraegeSplitListe.get(i).setStimmenNichtTeilnahme(
                        Long.toString(pWeisungMeldung.weisungMeldungSplit.abgabe[posInWeisung][5]));
                lGegenantraegeSplitListe.get(i)
                        .setStimmenFrei(Long.toString(pWeisungMeldung.weisungMeldungSplit.abgabe[posInWeisung][7]));
                lGegenantraegeSplitListe.get(i).setStimmenStimmausschluss(
                        Long.toString(pWeisungMeldung.weisungMeldungSplit.abgabe[posInWeisung][9]));
            }
        }

    }
}
