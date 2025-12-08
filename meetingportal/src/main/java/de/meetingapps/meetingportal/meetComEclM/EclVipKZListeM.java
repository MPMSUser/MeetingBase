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
import de.meetingapps.meetingportal.meetComEntities.EclVipKZ;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

@SessionScoped
@Named
public class EclVipKZListeM implements Serializable {
    private static final long serialVersionUID = -4023685088637960573L;

    private List<EclVipKZM> vipKZListeM;

    public EclVipKZListeM() {
        init();
    }

    public void init() {
        vipKZListeM = new LinkedList<>();

    }

    public void fuelleListe(DbBundle pDbBundle) {
        init();
        pDbBundle.dbVipKZ.readInArray();
        EclVipKZM vipKZ0 = new EclVipKZM();
        vipKZ0.setKuerzel("_0");
        vipKZ0.setBeschreibung("(bitte wählen)");
        vipKZListeM.add(vipKZ0);
        int i;
        for (i = 0; i < pDbBundle.dbVipKZ.vipKZarray.length; i++) {
            this.add(pDbBundle.dbVipKZ.vipKZarray[i]);
        }

    }

    public void add(EclVipKZ pVipKZ) {
        EclVipKZM lVipKZ = new EclVipKZM();
        lVipKZ.copyFrom(pVipKZ);
        vipKZListeM.add(lVipKZ);
    }

    public List<EclVipKZM> getVipKZListeM() {
        return vipKZListeM;
    }

    public void setVipKZListeM(List<EclVipKZM> vipKZListeM) {
        this.vipKZListeM = vipKZListeM;
    }

}
