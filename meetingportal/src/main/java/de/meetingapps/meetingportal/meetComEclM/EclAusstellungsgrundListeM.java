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
import de.meetingapps.meetingportal.meetComEntities.EclAusstellungsgrund;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

@SessionScoped
@Named
public class EclAusstellungsgrundListeM implements Serializable {
    private static final long serialVersionUID = -4023685088637960573L;

    private List<EclAusstellungsgrundM> ausstellungsgrundListeM;
    private List<EclAusstellungsgrundM> ausstellungsgrundStornoListeM;

    public EclAusstellungsgrundListeM() {
        init();
    }

    public void init() {
        ausstellungsgrundListeM = new LinkedList<>();
        ausstellungsgrundStornoListeM = new LinkedList<>();
    }

    public void fuelleListe(DbBundle pDbBundle) {
        init();
        /*Liste für Neuausstellung*/
        pDbBundle.dbAusstellungsgrund.readInArray(2, 1);
        EclAusstellungsgrundM ausstellungsgrund0 = new EclAusstellungsgrundM();
        ausstellungsgrund0.setKuerzel("_0");
        ausstellungsgrund0.setBeschreibung("(bitte wählen)");
        ausstellungsgrundListeM.add(ausstellungsgrund0);
        for (int i = 0; i < pDbBundle.dbAusstellungsgrund.ausstellungsgrundArray.length; i++) {
            this.add(pDbBundle.dbAusstellungsgrund.ausstellungsgrundArray[i], ausstellungsgrundListeM);
        }

        /*Liste für Storno*/
        pDbBundle.dbAusstellungsgrund.readInArray(2, 2);
        ausstellungsgrund0 = new EclAusstellungsgrundM();
        ausstellungsgrund0.setKuerzel("_0");
        ausstellungsgrund0.setBeschreibung("(bitte wählen)");
        ausstellungsgrundStornoListeM.add(ausstellungsgrund0);
        for (int i = 0; i < pDbBundle.dbAusstellungsgrund.ausstellungsgrundArray.length; i++) {
            this.add(pDbBundle.dbAusstellungsgrund.ausstellungsgrundArray[i], ausstellungsgrundStornoListeM);
        }

    }

    public void add(EclAusstellungsgrund pAusstellungsgrund, List<EclAusstellungsgrundM> pAusstellungsgrundListeM) {
        EclAusstellungsgrundM lAusstellungsgrund = new EclAusstellungsgrundM();
        lAusstellungsgrund.copyFrom(pAusstellungsgrund);
        pAusstellungsgrundListeM.add(lAusstellungsgrund);
    }

    public List<EclAusstellungsgrundM> getAusstellungsgrundListeM() {
        return ausstellungsgrundListeM;
    }

    public void setAusstellungsgrundListeM(List<EclAusstellungsgrundM> ausstellungsgrundListeM) {
        this.ausstellungsgrundListeM = ausstellungsgrundListeM;
    }

    public List<EclAusstellungsgrundM> getAusstellungsgrundStornoListeM() {
        return ausstellungsgrundStornoListeM;
    }

    public void setAusstellungsgrundStornoListeM(List<EclAusstellungsgrundM> ausstellungsgrundStornoListeM) {
        this.ausstellungsgrundStornoListeM = ausstellungsgrundStornoListeM;
    }

}
