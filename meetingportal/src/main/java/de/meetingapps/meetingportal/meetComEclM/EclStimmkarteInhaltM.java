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

import de.meetingapps.meetingportal.meetComEntities.EclStimmkarteInhalt;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

@SessionScoped
@Named
public class EclStimmkarteInhaltM implements Serializable {
    private static final long serialVersionUID = -8633261812224339659L;

    private List<EclAbstimmungM> abstimmungenListeM = null;
    private String kurzBezeichnung = "";
    private String stimmkartenBezeichnung = "";
    private String stimmkartenBezeichnungEN = "";
    private int stimmkartenNr = 0;
    private int posInBlock = 0;
    private int stimmkarteIstAktiv = 0;

    public void copyFrom(EclStimmkarteInhalt pStimmkarteInhalt) {
        int i;

        abstimmungenListeM = new LinkedList<EclAbstimmungM>();
        if (pStimmkarteInhalt != null) {
            if (pStimmkarteInhalt.abstimmungenListe != null) {
                for (i = 0; i < pStimmkarteInhalt.abstimmungenListe.size(); i++) {
                    EclAbstimmungM abstimmungM = new EclAbstimmungM();
                    abstimmungM.copyFrom(pStimmkarteInhalt.abstimmungenListe.get(i));
                    abstimmungenListeM.add(abstimmungM);

                }
            }
            this.kurzBezeichnung = pStimmkarteInhalt.kurzBezeichnung;
            this.stimmkartenBezeichnung = pStimmkarteInhalt.stimmkartenBezeichnung;
            this.stimmkartenBezeichnungEN = pStimmkarteInhalt.stimmkartenBezeichnungEN;
            this.stimmkartenNr = pStimmkarteInhalt.stimmkartenNr;
            this.posInBlock = pStimmkarteInhalt.posInBlock;
            this.stimmkarteIstAktiv = pStimmkarteInhalt.stimmkarteIstAktiv;
        }

    }

    /***********Standard Getter und Setter********************************/

    public List<EclAbstimmungM> getAbstimmungenListeM() {
        return abstimmungenListeM;
    }

    public void setAbstimmungenListeM(List<EclAbstimmungM> abstimmungenListeM) {
        this.abstimmungenListeM = abstimmungenListeM;
    }

    public String getStimmkartenBezeichnung() {
        return stimmkartenBezeichnung;
    }

    public void setStimmkartenBezeichnung(String stimmkartenBezeichnung) {
        this.stimmkartenBezeichnung = stimmkartenBezeichnung;
    }

    public int getStimmkartenNr() {
        return stimmkartenNr;
    }

    public void setStimmkartenNr(int stimmkartenNr) {
        this.stimmkartenNr = stimmkartenNr;
    }

    public int getStimmkarteIstAktiv() {
        return stimmkarteIstAktiv;
    }

    public void setStimmkarteIstAktiv(int stimmkarteIstAktiv) {
        this.stimmkarteIstAktiv = stimmkarteIstAktiv;
    }

    public int getPosInBlock() {
        return posInBlock;
    }

    public void setPosInBlock(int posInBlock) {
        this.posInBlock = posInBlock;
    }

    public String getKurzBezeichnung() {
        return kurzBezeichnung;
    }

    public void setKurzBezeichnung(String kurzBezeichnung) {
        this.kurzBezeichnung = kurzBezeichnung;
    }

    public String getStimmkartenBezeichnungEN() {
        return stimmkartenBezeichnungEN;
    }

    public void setStimmkartenBezeichnungEN(String stimmkartenBezeichnungEN) {
        this.stimmkartenBezeichnungEN = stimmkartenBezeichnungEN;
    }

}
