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
import de.meetingapps.meetingportal.meetComEntities.EclMeldung;
import de.meetingapps.meetingportal.meetComEntities.EclMeldungenMeldungen;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

@SessionScoped
@Named
public class EclMeldungenMeldungenListeM implements Serializable {
    private static final long serialVersionUID = -4023685088637960573L;

    private List<EclMeldungenMeldungenM> meldungenMeldungenListeM;

    public EclMeldungenMeldungenListeM() {
        init();
    }

    public void init() {
        meldungenMeldungenListeM = new LinkedList<>();

    }

    public void fuelleListe(DbBundle pDbBundle, EclMeldung pMeldung) {
        init();
        int i;
        EclMeldungenMeldungen[] meldungenMeldungenArray = pDbBundle.dbMeldungenMeldungen.leseVonMeldungen(pMeldung);
        for (i = 0; i < meldungenMeldungenArray.length; i++) {
            this.add(meldungenMeldungenArray[i]);
        }

    }

    public void add(EclMeldungenMeldungen pMeldungenMeldungen) {
        EclMeldungenMeldungenM lMeldungenMeldungen = new EclMeldungenMeldungenM();
        lMeldungenMeldungen.copyFrom(pMeldungenMeldungen);
        meldungenMeldungenListeM.add(lMeldungenMeldungen);
    }

    public List<EclMeldungenMeldungenM> getMeldungenMeldungenListeM() {
        return meldungenMeldungenListeM;
    }

    public void setMeldungenMeldungenListeM(List<EclMeldungenMeldungenM> meldungenMeldungenListeM) {
        this.meldungenMeldungenListeM = meldungenMeldungenListeM;
    }

}
