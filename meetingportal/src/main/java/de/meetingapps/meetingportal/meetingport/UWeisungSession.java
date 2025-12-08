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
package de.meetingapps.meetingportal.meetingport;

import java.io.Serializable;
import java.util.List;

import de.meetingapps.meetingportal.meetComEclM.EclAbstimmungMitVorschlagM;
import de.meetingapps.meetingportal.meetComEclM.EclInstiZugeordneterBestandM;
import de.meetingapps.meetingportal.meetComEclM.EclWillenserklaerungStatusM;
import de.meetingapps.meetingportal.meetComEclM.EclZugeordneteMeldungM;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

@SessionScoped
@Named
public class UWeisungSession implements Serializable {
    private static final long serialVersionUID = 3237167062013883431L;

    private EclInstiZugeordneterBestandM eclInstiZugeordneterBestandM = null;

    private EclZugeordneteMeldungM eclZugeordneteMeldungM = null;

    private EclWillenserklaerungStatusM eclWillenserklaerungStatusM = null;

    private List<EclAbstimmungMitVorschlagM> toMitVorschlagListe = null;

    private boolean aendern = false;
    private boolean anzeigen = false;

    private boolean aendernZulaessig = false;

    /**Als Zwischenspeicher zwischen einlesen und Änderungen speichern erforderlich*/
    private int willenserklaerungIdent = 0;
    private int sammelIdent = 0;
    private int weisungsIdent = 0;

    /**************Standard getter und setter************************************/

    public EclInstiZugeordneterBestandM getEclInstiZugeordneterBestandM() {
        return eclInstiZugeordneterBestandM;
    }

    public void setEclInstiZugeordneterBestandM(EclInstiZugeordneterBestandM eclInstiZugeordneterBestandM) {
        this.eclInstiZugeordneterBestandM = eclInstiZugeordneterBestandM;
    }

    public EclZugeordneteMeldungM getEclZugeordneteMeldungM() {
        return eclZugeordneteMeldungM;
    }

    public void setEclZugeordneteMeldungM(EclZugeordneteMeldungM eclZugeordneteMeldungM) {
        this.eclZugeordneteMeldungM = eclZugeordneteMeldungM;
    }

    public EclWillenserklaerungStatusM getEclWillenserklaerungStatusM() {
        return eclWillenserklaerungStatusM;
    }

    public void setEclWillenserklaerungStatusM(EclWillenserklaerungStatusM eclWillenserklaerungStatusM) {
        this.eclWillenserklaerungStatusM = eclWillenserklaerungStatusM;
    }

    public List<EclAbstimmungMitVorschlagM> getToMitVorschlagListe() {
        return toMitVorschlagListe;
    }

    public void setToMitVorschlagListe(List<EclAbstimmungMitVorschlagM> toMitVorschlagListe) {
        this.toMitVorschlagListe = toMitVorschlagListe;
    }

    public boolean isAendern() {
        return aendern;
    }

    public void setAendern(boolean aendern) {
        this.aendern = aendern;
    }

    public boolean isAnzeigen() {
        return anzeigen;
    }

    public void setAnzeigen(boolean anzeigen) {
        this.anzeigen = anzeigen;
    }

    public boolean isAendernZulaessig() {
        return aendernZulaessig;
    }

    public void setAendernZulaessig(boolean aendernZulaessig) {
        this.aendernZulaessig = aendernZulaessig;
    }

    public int getSammelIdent() {
        return sammelIdent;
    }

    public void setSammelIdent(int sammelIdent) {
        this.sammelIdent = sammelIdent;
    }

    public int getWillenserklaerungIdent() {
        return willenserklaerungIdent;
    }

    public void setWillenserklaerungIdent(int willenserklaerungIdent) {
        this.willenserklaerungIdent = willenserklaerungIdent;
    }

    public int getWeisungsIdent() {
        return weisungsIdent;
    }

    public void setWeisungsIdent(int weisungsIdent) {
        this.weisungsIdent = weisungsIdent;
    }

}
