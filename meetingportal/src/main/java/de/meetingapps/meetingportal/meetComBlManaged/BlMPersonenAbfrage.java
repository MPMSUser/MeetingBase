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

import java.util.LinkedList;
import java.util.List;

import de.meetingapps.meetingportal.meetComEclM.EclLoginDatenM;
import de.meetingapps.meetingportal.meetComEclM.EclParamM;
import de.meetingapps.meetingportal.meetComEclM.EclPersonenListeM;
import de.meetingapps.meetingportal.meetComEclM.EclVirtuellerTeilnehmerM;
import de.meetingapps.meetingportal.meetComKonst.KonstAktienregisterErgaenzung;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

/**Funktionen zur Unterstützung von Abfragen, wer die handelnde Person ist*/
@RequestScoped
public class BlMPersonenAbfrage {

    @Inject
    private EclPersonenListeM eclPersonenListeM;
    @Inject
    private EclLoginDatenM eclLoginDatenM;
    @Inject
    private EclParamM eclParamM;

    /**Belegen */
    public void belegeListe() {

        eclPersonenListeM.clear();

        List<EclVirtuellerTeilnehmerM> listePersonenZurAuswahl = new LinkedList<EclVirtuellerTeilnehmerM>();

        /*Login-Kennung*/
        EclVirtuellerTeilnehmerM lVirtuellerTeilnehmerM = new EclVirtuellerTeilnehmerM();
        int kennungArt = eclLoginDatenM.liefereKennungArt();
        if (kennungArt == 1 && eclParamM.getParam().paramPortal.varianteDialogablauf == 1 && !eclLoginDatenM
                .getEclAktienregisterErgaenzung().ergaenzungLangString[KonstAktienregisterErgaenzung.ku178_DirekterVertreterName]
                        .isEmpty()) {
            /**Dann wurde zu dem Aktionär (Mitglied) ein vorbelegter Vertreter gespeichert, der als Person das Mitglied vertritt*/
            lVirtuellerTeilnehmerM.setLaufendeIdent(1);
            lVirtuellerTeilnehmerM.setArt(1);
            String name = eclLoginDatenM
                    .getEclAktienregisterErgaenzung().ergaenzungLangString[KonstAktienregisterErgaenzung.ku178_DirekterVertreterVorname]
                    + " " + eclLoginDatenM
                            .getEclAktienregisterErgaenzung().ergaenzungLangString[KonstAktienregisterErgaenzung.ku178_DirekterVertreterName];
            lVirtuellerTeilnehmerM.setName(name);
            lVirtuellerTeilnehmerM.setOrt(eclLoginDatenM
                    .getEclAktienregisterErgaenzung().ergaenzungLangString[KonstAktienregisterErgaenzung.ku178_DirekterVertreterOrt]);
        } else {
            lVirtuellerTeilnehmerM.setLaufendeIdent(1);
            lVirtuellerTeilnehmerM.setArt(1);
            lVirtuellerTeilnehmerM.setName(eclLoginDatenM.getTitelVornameName());
            lVirtuellerTeilnehmerM.setOrt(eclLoginDatenM.getOrt());
        }
        listePersonenZurAuswahl.add(lVirtuellerTeilnehmerM);

        /*Sonstige Person*/
        lVirtuellerTeilnehmerM = new EclVirtuellerTeilnehmerM();
        lVirtuellerTeilnehmerM.setLaufendeIdent(2);
        lVirtuellerTeilnehmerM.setArt(4);
        lVirtuellerTeilnehmerM.setName("Sonstige Person");
        listePersonenZurAuswahl.add(lVirtuellerTeilnehmerM);

        eclPersonenListeM.setListePersonenZurAuswahl(listePersonenZurAuswahl);
        return;
    }

    /**return =art der ausgewählten Person*/
    public int belegeAusgewaehltenTeilnehmer() {
        eclPersonenListeM.setAktuellerTeilnehmer(eclPersonenListeM.getListePersonenZurAuswahl()
                .get(eclPersonenListeM.getAusgewaehlterTeilnehmerIdent() - 1));
        return eclPersonenListeM.getAktuellerTeilnehmer().getArt();
    }
}
