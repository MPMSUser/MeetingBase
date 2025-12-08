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
import de.meetingapps.meetingportal.meetComEntities.EclMeldungVipKZ;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

@SessionScoped
@Named
public class EclMeldungVipKZListeM implements Serializable {
    private static final long serialVersionUID = -4023685088637960573L;

    private List<EclMeldungVipKZM> meldungVipKZListeM;

    public EclMeldungVipKZListeM() {
        init();
    }

    public void init() {
        meldungVipKZListeM = new LinkedList<>();

    }

    public void fuelleListe(DbBundle pDbBundle, EclMeldung pMeldung) {
        init();
        int i;
        EclMeldungVipKZ[] meldungVipKZArray = pDbBundle.dbMeldungVipKZ.leseZuMeldung(pMeldung);
        for (i = 0; i < meldungVipKZArray.length; i++) {
            this.add(meldungVipKZArray[i]);
        }

    }

    public void add(EclMeldungVipKZ pMeldungVipKZ) {
        EclMeldungVipKZM lMeldungVipKZ = new EclMeldungVipKZM();
        lMeldungVipKZ.copyFrom(pMeldungVipKZ);
        meldungVipKZListeM.add(lMeldungVipKZ);
    }

    public List<EclMeldungVipKZM> getMeldungVipKZListeM() {
        return meldungVipKZListeM;
    }

    public void setMeldungVipKZListeM(List<EclMeldungVipKZM> meldungVipKZListeM) {
        this.meldungVipKZListeM = meldungVipKZListeM;
    }

}
