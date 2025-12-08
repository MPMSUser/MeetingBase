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
package de.meetingapps.meetingportal.meetingportTController;

import java.io.Serializable;
import java.util.List;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComEclM.EclPortalUnterlagenM;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

@SessionScoped
@Named
public class TUnterlagenSession implements Serializable {
    private static final long serialVersionUID = -765915485446584898L;

    private int logDrucken=3;

    /**tAuswahlSession.unterlagenAktiv()*/
    private boolean ansehenAktiv=false;
    
    private List<EclPortalUnterlagenM> unterlagenListe = null;

    
    /**Texte für Unterlagen-Seite / Botschaften-Seite*/
    private int starttextAktiv=0;
    private int textWennAktivAberListeLeer=0;
    private int endetextAktiv=0;
    private int textInaktiv=0;
    private int endetextImmer=0;
    
    private int unterlagenbereich=0;
    private int unterlagenSubBereich=0;
    
    public boolean liefereListeIstLeer() {
        if (unterlagenListe==null || unterlagenListe.size()==0) {
            return true;
        }
        return false;
    }
    
    /**************Standard getter und setter******************************/

    public List<EclPortalUnterlagenM> getUnterlagenListe() {
        return unterlagenListe;
    }

    public void setUnterlagenListe(List<EclPortalUnterlagenM> unterlagenListe) {
        this.unterlagenListe = unterlagenListe;
    }

    public int getStarttextAktiv() {
        CaBug.druckeLog("starttextAktiv="+starttextAktiv, logDrucken, 10);
        return starttextAktiv;
    }

    public void setStarttextAktiv(int starttextAktiv) {
        this.starttextAktiv = starttextAktiv;
    }

    public int getEndetextAktiv() {
        return endetextAktiv;
    }

    public void setEndetextAktiv(int endetextAktiv) {
        this.endetextAktiv = endetextAktiv;
    }

    public int getTextInaktiv() {
        return textInaktiv;
    }

    public void setTextInaktiv(int textInaktiv) {
        this.textInaktiv = textInaktiv;
    }

    public int getEndetextImmer() {
        return endetextImmer;
    }

    public void setEndetextImmer(int endetextImmer) {
        this.endetextImmer = endetextImmer;
    }

    public boolean isAnsehenAktiv() {
        return ansehenAktiv;
    }

    public void setAnsehenAktiv(boolean ansehenAktiv) {
        this.ansehenAktiv = ansehenAktiv;
    }

    public int getTextWennAktivAberListeLeer() {
        return textWennAktivAberListeLeer;
    }

    public void setTextWennAktivAberListeLeer(int textWennAktivAberListeLeer) {
        this.textWennAktivAberListeLeer = textWennAktivAberListeLeer;
    }

    public int getUnterlagenbereich() {
        return unterlagenbereich;
    }

    public void setUnterlagenbereich(int unterlagenbereich) {
        this.unterlagenbereich = unterlagenbereich;
    }

    public int getUnterlagenSubBereich() {
        return unterlagenSubBereich;
    }

    public void setUnterlagenSubBereich(int unterlagenSubBereich) {
        this.unterlagenSubBereich = unterlagenSubBereich;
    }


}
