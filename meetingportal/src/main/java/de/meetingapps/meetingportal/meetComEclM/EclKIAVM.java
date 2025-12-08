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

import de.meetingapps.meetingportal.meetComEntities.EclMeldung;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

@SessionScoped
@Named
public class EclKIAVM implements Serializable {
    private static final long serialVersionUID = -5085633043740460761L;

    private int meldeIdent = 0;
    private String kurzText = "";
    private String zusatzfeld2 = "";
    private boolean mehrereVarianten = false;
    private boolean akzeptiertOhneWeisung = false;
    private boolean akzeptiertDedizierteWeisung = false;
    private boolean akzeptiertWieVorschlag = false;
    private int abstimmungsVorschlagIdent = 0;
    private boolean buchbarInternet = false;
    private boolean buchbarPapier = false;
    private boolean buchbarHV = false;
    private int gattung = 0;
    private int statusPraesenz = 0;

    public int getGattung() {
        if (gattung == 0) {
            return 1;
        }
        return gattung;
    }

    public void copyFromM(EclKIAVM pEclKIAVM) {
        this.meldeIdent = pEclKIAVM.meldeIdent;
        this.kurzText = pEclKIAVM.kurzText;
        this.zusatzfeld2 = pEclKIAVM.zusatzfeld2;
        this.mehrereVarianten = pEclKIAVM.mehrereVarianten;
        this.akzeptiertOhneWeisung = pEclKIAVM.akzeptiertOhneWeisung;
        this.akzeptiertDedizierteWeisung = pEclKIAVM.akzeptiertDedizierteWeisung;
        this.akzeptiertWieVorschlag = pEclKIAVM.akzeptiertWieVorschlag;
        this.abstimmungsVorschlagIdent = pEclKIAVM.abstimmungsVorschlagIdent;
        this.buchbarInternet = pEclKIAVM.buchbarInternet;
        this.buchbarPapier = pEclKIAVM.buchbarPapier;
        this.buchbarHV = pEclKIAVM.buchbarHV;
        this.gattung = pEclKIAVM.gattung;
        this.statusPraesenz = pEclKIAVM.statusPraesenz;
    }

    public void copyToM(EclKIAVM pEclKIAVM) {
        pEclKIAVM.meldeIdent = this.meldeIdent;
        pEclKIAVM.kurzText = this.kurzText;
        pEclKIAVM.zusatzfeld2 = this.zusatzfeld2;
        pEclKIAVM.mehrereVarianten = this.mehrereVarianten;
        pEclKIAVM.akzeptiertOhneWeisung = this.akzeptiertOhneWeisung;
        pEclKIAVM.akzeptiertDedizierteWeisung = this.akzeptiertDedizierteWeisung;
        pEclKIAVM.akzeptiertWieVorschlag = this.akzeptiertWieVorschlag;
        pEclKIAVM.abstimmungsVorschlagIdent = this.abstimmungsVorschlagIdent;
        pEclKIAVM.buchbarInternet = this.buchbarInternet;
        pEclKIAVM.buchbarPapier = this.buchbarPapier;
        pEclKIAVM.buchbarHV = this.buchbarHV;
        pEclKIAVM.gattung = this.gattung;
        pEclKIAVM.statusPraesenz = this.statusPraesenz;
    }

    public void copyFrom(EclMeldung pEclMeldung) {
        int anzAngebot = 0;
        this.meldeIdent = pEclMeldung.meldungsIdent;
        this.kurzText = pEclMeldung.kurzName;
        this.zusatzfeld2 = pEclMeldung.zusatzfeld2;
        this.gattung = pEclMeldung.liefereGattung();
        int lskWeisungsartZulaessig = pEclMeldung.skWeisungsartZulaessig;
        if ((lskWeisungsartZulaessig & 2) == 2) {
            this.akzeptiertOhneWeisung = true;
            anzAngebot++;
        }
        if ((lskWeisungsartZulaessig & 4) == 4) {
            this.akzeptiertDedizierteWeisung = true;
            anzAngebot++;
        }
        if ((lskWeisungsartZulaessig & 8) == 8) {
            this.akzeptiertWieVorschlag = true;
            anzAngebot++;
        }
        if (anzAngebot > 1) {
            this.mehrereVarianten = true;
        }

        if (pEclMeldung.skBuchbarInternet == 1) {
            this.buchbarInternet = true;
        } else {
            this.buchbarInternet = false;
        }
        if (pEclMeldung.skBuchbarPapier == 1) {
            this.buchbarPapier = true;
        } else {
            this.buchbarPapier = false;
        }
        if (pEclMeldung.skBuchbarHV == 1) {
            this.buchbarHV = true;
        } else {
            this.buchbarHV = false;
        }

        this.statusPraesenz = pEclMeldung.statusPraesenz;

    }

    public String liefereSammelkartenBezeichnungKomplettIntern() {
        return this.kurzText + " " + this.zusatzfeld2;
    }

    /***********Standard Setter Getter*******************************/

    public int getMeldeIdent() {
        return meldeIdent;
    }

    public void setMeldeIdent(int meldeIdent) {
        this.meldeIdent = meldeIdent;
    }

    public String getKurzText() {
        return kurzText;
    }

    public void setKurzText(String kurzText) {
        this.kurzText = kurzText;
    }

    public boolean isMehrereVarianten() {
        return mehrereVarianten;
    }

    public void setMehrereVarianten(boolean mehrereVarianten) {
        this.mehrereVarianten = mehrereVarianten;
    }

    public boolean isAkzeptiertOhneWeisung() {
        return akzeptiertOhneWeisung;
    }

    public void setAkzeptiertOhneWeisung(boolean akzeptiertOhneWeisung) {
        this.akzeptiertOhneWeisung = akzeptiertOhneWeisung;
    }

    public boolean isAkzeptiertDedizierteWeisung() {
        return akzeptiertDedizierteWeisung;
    }

    public void setAkzeptiertDedizierteWeisung(boolean akzeptiertDedizierteWeisung) {
        this.akzeptiertDedizierteWeisung = akzeptiertDedizierteWeisung;
    }

    public boolean isAkzeptiertWieVorschlag() {
        return akzeptiertWieVorschlag;
    }

    public void setAkzeptiertWieVorschlag(boolean akzeptiertWieVorschlag) {
        this.akzeptiertWieVorschlag = akzeptiertWieVorschlag;
    }

    public int getAbstimmungsVorschlagIdent() {
        return abstimmungsVorschlagIdent;
    }

    public void setAbstimmungsVorschlagIdent(int abstimmungsVorschlagIdent) {
        this.abstimmungsVorschlagIdent = abstimmungsVorschlagIdent;
    }

    public boolean isBuchbarInternet() {
        return buchbarInternet;
    }

    public void setBuchbarInternet(boolean buchbarInternet) {
        this.buchbarInternet = buchbarInternet;
    }

    public boolean isBuchbarPapier() {
        return buchbarPapier;
    }

    public void setBuchbarPapier(boolean buchbarPapier) {
        this.buchbarPapier = buchbarPapier;
    }

    public boolean isBuchbarHV() {
        return buchbarHV;
    }

    public void setBuchbarHV(boolean buchbarHV) {
        this.buchbarHV = buchbarHV;
    }

    public void setGattung(int gattung) {
        this.gattung = gattung;
    }

    public int getStatusPraesenz() {
        return statusPraesenz;
    }

    public void setStatusPraesenz(int statusPraesenz) {
        this.statusPraesenz = statusPraesenz;
    }

    public String getZusatzfeld2() {
        return zusatzfeld2;
    }

    public void setZusatzfeld2(String zusatzfeld2) {
        this.zusatzfeld2 = zusatzfeld2;
    }

}
