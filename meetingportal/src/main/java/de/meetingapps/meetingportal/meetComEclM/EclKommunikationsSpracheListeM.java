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
import de.meetingapps.meetingportal.meetComEntities.EclKommunikationsSprache;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

@SessionScoped
@Named
public class EclKommunikationsSpracheListeM implements Serializable {
    private static final long serialVersionUID = -4023685088637960573L;

    private List<EclKommunikationsSpracheM> kommunikationsSpracheListeM;

    public EclKommunikationsSpracheListeM() {
        init();
    }

    public void init() {
        kommunikationsSpracheListeM = new LinkedList<>();
    }

    public void fuelleListe(DbBundle pDbBundle) {
        init();
        pDbBundle.dbKommunikationsSprachen.FuelleKommunikationsSprachenArray();
        int i;
        for (i = 0; i < pDbBundle.dbKommunikationsSprachen.kommunikationssprachenarray.length; i++) {
            this.add(pDbBundle.dbKommunikationsSprachen.kommunikationssprachenarray[i]);
        }

    }

    public void add(EclKommunikationsSprache pKommunikationsSprache) {
        EclKommunikationsSpracheM lKommunikationssprache = new EclKommunikationsSpracheM();
        lKommunikationssprache.copyFrom(pKommunikationsSprache);
        kommunikationsSpracheListeM.add(lKommunikationssprache);
    }

    public List<EclKommunikationsSpracheM> getKommunikationsSpracheListeM() {
        return kommunikationsSpracheListeM;
    }

    public void setKommunikationsSpracheListeM(List<EclKommunikationsSpracheM> kommunikationsSpracheListeM) {
        this.kommunikationsSpracheListeM = kommunikationsSpracheListeM;
    }

}
