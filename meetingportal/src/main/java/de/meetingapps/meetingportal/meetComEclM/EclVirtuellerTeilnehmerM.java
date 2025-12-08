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

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComEntities.EclVirtuellerTeilnehmer;

public class EclVirtuellerTeilnehmerM implements Serializable {
    private static final long serialVersionUID = -7616910980057969253L;

    private int logDrucken = 3;

    /**Für temporäre Identifikation - sprich offset
     * innerhalb der Liste oder des Arrays
     */
    private int laufendeIdent = 0;

    /**1=Aktionär selbst
     * 2=Vertreter
     * 3=Insti
     * 4=Sonstige
     */
    private int art = 0;

    /**Je nach art - identPersonNatJur oder vergleichbares*/
    private int ident = 0;
    /**Aufbereiteter Name, Vorname Nachname bzw. Firmenname*/
    private String name = "";
    private String ort = "";

    public EclVirtuellerTeilnehmerM() {

    }

    public EclVirtuellerTeilnehmerM(EclVirtuellerTeilnehmer pVirtuellerTeilnehmer) {
        this.laufendeIdent = pVirtuellerTeilnehmer.laufendeIdent;
        this.art = pVirtuellerTeilnehmer.art;
        this.ident = pVirtuellerTeilnehmer.ident;
        this.name = pVirtuellerTeilnehmer.name;
        this.ort = pVirtuellerTeilnehmer.ort;
    }

    public EclVirtuellerTeilnehmer copyTo() {
        EclVirtuellerTeilnehmer lVirtuellerTeilnehmer = new EclVirtuellerTeilnehmer();
        lVirtuellerTeilnehmer.laufendeIdent = this.laufendeIdent;
        lVirtuellerTeilnehmer.art = this.art;
        lVirtuellerTeilnehmer.ident = this.ident;
        lVirtuellerTeilnehmer.name = this.name;
        lVirtuellerTeilnehmer.ort = this.ort;
        return lVirtuellerTeilnehmer;
    }

    /*********Standard getter setter**************************/

    public int getLaufendeIdent() {
        return laufendeIdent;
    }

    public void setLaufendeIdent(int laufendeIdent) {
        this.laufendeIdent = laufendeIdent;
    }

    public int getArt() {
        return art;
    }

    public void setArt(int art) {
        this.art = art;
    }

    public int getIdent() {
        return ident;
    }

    public void setIdent(int ident) {
        this.ident = ident;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrt() {
        return ort;
    }

    public void setOrt(String ort) {
        this.ort = ort;
    }

    public String getAnzeigeTextAuswahl() {
        String zusatz = "";
        //		switch (art) {
        //		case 1:{zusatz=" (Selbst)";break;}
        //		case 2:{zusatz=" (Bevollmächtigter)";break;}
        //		}
        String anzeigeText = name;
        //		if (art!=4) {
        if (!ort.isEmpty()) {
            anzeigeText = anzeigeText + ", " + ort + zusatz;
        }
        //		}
        CaBug.druckeLog("anzeigeText=" + anzeigeText, logDrucken, 10);
        return anzeigeText;
    }

    public void setAnzeigeTextAuswahl(String anzeigeText) {
    }

}
