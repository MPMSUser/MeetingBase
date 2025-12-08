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
package de.meetingapps.meetingportal.meetComEclM;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclGruppe;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

@SessionScoped
@Named
public class EclGruppeListeM implements Serializable {
    private static final long serialVersionUID = -4023685088637960573L;

    private List<EclGruppeM> gruppeListeM;

    public EclGruppeListeM() {
        init();
    }

    public void init() {
        gruppeListeM = new LinkedList<>();

    }

    public void fuelleListe(DbBundle pDbBundle) {
        init();
        pDbBundle.dbGruppen.leseInArray(2);
        EclGruppeM gruppe0 = new EclGruppeM();
        gruppe0.setGruppenNr("0");
        gruppe0.setGruppenText("(bitte wählen)");
        gruppeListeM.add(gruppe0);
        int i;
        for (i = 0; i < pDbBundle.dbGruppen.gruppeArray.length; i++) {
            this.add(pDbBundle.dbGruppen.gruppeArray[i]);
        }

    }

    public void add(EclGruppe pGruppe) {
        EclGruppeM lGruppe = new EclGruppeM();
        lGruppe.copyFrom(pGruppe);
        gruppeListeM.add(lGruppe);
    }

    public List<EclGruppeM> getGruppeListeM() {
        return gruppeListeM;
    }

    public void setGruppeListeM(List<EclGruppeM> gruppeListeM) {
        this.gruppeListeM = gruppeListeM;
    }

}
