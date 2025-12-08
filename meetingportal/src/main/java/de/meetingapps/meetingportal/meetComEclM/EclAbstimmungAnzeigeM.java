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

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

@SessionScoped
@Named
public class EclAbstimmungAnzeigeM implements Serializable {
    private static final long serialVersionUID = -1783898382339870569L;

    private String stimmkarte = "";
    private String anzeigeBezeichnungKurz = "";

    private int externJa = 0;
    private int externNein = 0;
    private int externEnthaltung = 0;

    private boolean ueberschrift = false;

    /**Nr,, mit der die Abstimmung auf die Array-Position im Weisungssatz verweist
     * -1 == Satz dienst nur als "Zwischenüberschrift" (z.B. im Portal)*/
    private int identWeisungssatz = 0;

    private int stimmblock = 0;
    private int stimmkarteNr = 0;
    private int lftNdr = 0;

    public String getStimmkarte() {
        return stimmkarte;
    }

    public void setStimmkarte(String stimmkarte) {
        this.stimmkarte = stimmkarte;
    }

    public String getAnzeigeBezeichnungKurz() {
        return anzeigeBezeichnungKurz;
    }

    public void setAnzeigeBezeichnungKurz(String anzeigeBezeichnungKurz) {
        this.anzeigeBezeichnungKurz = anzeigeBezeichnungKurz;
    }

    public int getExternJa() {
        return externJa;
    }

    public void setExternJa(int externJa) {
        this.externJa = externJa;
    }

    public int getExternNein() {
        return externNein;
    }

    public void setExternNein(int externNein) {
        this.externNein = externNein;
    }

    public int getExternEnthaltung() {
        return externEnthaltung;
    }

    public void setExternEnthaltung(int externEnthaltung) {
        this.externEnthaltung = externEnthaltung;
    }

    public int getStimmblock() {
        return stimmblock;
    }

    public void setStimmblock(int stimmblock) {
        this.stimmblock = stimmblock;
    }

    public int getStimmkarteNr() {
        return stimmkarteNr;
    }

    public void setStimmkarteNr(int stimmkarteNr) {
        this.stimmkarteNr = stimmkarteNr;
    }

    public int getLftNdr() {
        return lftNdr;
    }

    public void setLftNdr(int lftNdr) {
        this.lftNdr = lftNdr;
    }

    public boolean isUeberschrift() {
        return ueberschrift;
    }

    public void setUeberschrift(boolean ueberschrift) {
        this.ueberschrift = ueberschrift;
    }

    public int getIdentWeisungssatz() {
        return identWeisungssatz;
    }

    public void setIdentWeisungssatz(int identWeisungssatz) {
        this.identWeisungssatz = identWeisungssatz;
    }

}