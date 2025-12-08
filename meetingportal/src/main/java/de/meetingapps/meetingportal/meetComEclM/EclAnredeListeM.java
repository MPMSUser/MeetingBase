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
import de.meetingapps.meetingportal.meetComEntities.EclAnrede;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

@SessionScoped
@Named
public class EclAnredeListeM implements Serializable {
    private static final long serialVersionUID = -4023685088637960573L;

    private List<EclAnredeM> anredeListeM;

    public EclAnredeListeM() {
        init();
    }

    public void init() {
        anredeListeM = new LinkedList<>();
    }

    public void fuelleListe(DbBundle pDbBundle) {
        init();
        pDbBundle.dbAnreden.readInArray();
        EclAnredeM anrede0 = new EclAnredeM();
        anrede0.setAnredennr("0");
        anrede0.setAnredentext("(bitte wählen)");
        anredeListeM.add(anrede0);
        int i;
        for (i = 0; i < pDbBundle.dbAnreden.anredenarray.length; i++) {
            if (pDbBundle.dbAnreden.anredenarray[i].anredennr!=4) {
                this.add(pDbBundle.dbAnreden.anredenarray[i]);
            }
        }

    }

    public void add(EclAnrede pAnrede) {
        EclAnredeM lAnrede = new EclAnredeM();
        lAnrede.copyFrom(pAnrede);
        anredeListeM.add(lAnrede);
    }

    public List<EclAnredeM> getAnredeListeM() {
        return anredeListeM;
    }

    public void setAnredeListeM(List<EclAnredeM> anredeListeM) {
        this.anredeListeM = anredeListeM;
    }

}
