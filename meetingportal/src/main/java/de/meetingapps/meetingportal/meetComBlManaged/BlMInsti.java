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

import de.meetingapps.meetingportal.meetComBl.BlInsti;
import de.meetingapps.meetingportal.meetComBl.BlWillenserklaerungStatus;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEclM.EclInstiZugeordneterBestandListeM;
import de.meetingapps.meetingportal.meetComEclM.EclInstiZugeordneterBestandM;
import de.meetingapps.meetingportal.meetComEclM.EclParamM;
import de.meetingapps.meetingportal.meetComEclM.EclZugeordneteMeldungListeM;
import de.meetingapps.meetingportal.meetComEntities.EclAktienregister;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

@RequestScoped
public class BlMInsti {

    @Inject
    private EclInstiZugeordneterBestandListeM eclInstiZugeordneterBestandListeM;
    @Inject
    EclParamM eclParamM;

    /**Füllt alle diesem Insti zugeordneten Bestände und legt diese in 
     * EclInstiZugeordneterBestandListeM ab.
     */
    public void fuelleBestandszuordnungFuerPortal(DbBundle pDbBundle, int pInstiIdent) {
        BlInsti blInsti = new BlInsti(true, pDbBundle);
        blInsti.fuelleInstiBestandsZuordnung(pInstiIdent);

        List<EclInstiZugeordneterBestandM> lInstiZugeordneterBestandMListe = new LinkedList<EclInstiZugeordneterBestandM>();

        if (blInsti.rcRegAktienregister != null && blInsti.rcRegAktienregister.length != 0) {
            for (int i = 0; i < blInsti.rcRegAktienregister.length; i++) {
                EclAktienregister lAktienregister = blInsti.rcRegAktienregister[i];

                EclInstiZugeordneterBestandM lInstiZugeordneterBestandM = new EclInstiZugeordneterBestandM();
                lInstiZugeordneterBestandM.setAktienregisterIdent(lAktienregister.aktienregisterIdent);
                lInstiZugeordneterBestandM.setAktionaersnummer(lAktienregister.aktionaersnummer);
                lInstiZugeordneterBestandM.setAktionaersname(lAktienregister.nameKomplett);
                lInstiZugeordneterBestandM.setAktien(lAktienregister.stueckAktien);

                EclZugeordneteMeldungListeM lZugeordneteMeldungListeM = new EclZugeordneteMeldungListeM();

                BlWillenserklaerungStatus lWillenserklaerungStatus = new BlWillenserklaerungStatus(pDbBundle);
                lWillenserklaerungStatus.piAlleWillenserklaerungen = 0;
                /*Login ist über Aktienregisternummer erfolgt. In diesem Fall "Basis-Meldungen über
                 			leseMeldungenZuAktienregisterIdent ermitteln, anschließend personNatJur aus (Aktionärs-)
                 			Anmeldungen bestimmen*/
                if (lAktienregister.stueckAktien != 0) {
                    lWillenserklaerungStatus.leseMeldungenZuAktienregisterIdent(lAktienregister.aktienregisterIdent);

                    lWillenserklaerungStatus.ergaenzeZugeordneteMeldungenUmWillenserklaerungen(-2);

                    /*Liste mit zugeordneten Meldungen / Willenserklärungen füllen*/
                    lZugeordneteMeldungListeM.zugeordneteMeldungenEigeneAktienCopyFromOhneInject(
                            lWillenserklaerungStatus.zugeordneteMeldungenEigeneAktienArray, false,
                            eclParamM.getParam());
                    lZugeordneteMeldungListeM.setBriefwahlVorhanden(lWillenserklaerungStatus.briefwahlVorhanden);
                    lZugeordneteMeldungListeM.setSrvVorhanden(lWillenserklaerungStatus.srvVorhanden);
                }

                lInstiZugeordneterBestandM.setZugeordneteMeldungListeM(lZugeordneteMeldungListeM);
                lInstiZugeordneterBestandMListe.add(lInstiZugeordneterBestandM);
            }
        }

        eclInstiZugeordneterBestandListeM.setInstiZugeordneterBestandM(lInstiZugeordneterBestandMListe);
    }

}
