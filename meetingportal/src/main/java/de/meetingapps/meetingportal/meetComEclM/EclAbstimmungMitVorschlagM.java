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

import de.meetingapps.meetingportal.meetComEntities.EclAbstimmung;
import de.meetingapps.meetingportal.meetComKonst.KonstStimmart;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

@SessionScoped
@Named
public class EclAbstimmungMitVorschlagM implements Serializable {
    private static final long serialVersionUID = -6269311989034993074L;

    private int ident = 0;

    private String nummer = "";
    private String nummerindex = "";
    private String nummerEN = "";
    private String nummerindexEN = "";

    private String anzeigeBezeichnungKurz = "";
    private String anzeigeBezeichnungKurzEN = "";

    private int identWeisungssatz = 0;

    private boolean ueberschrift = false;

    /**Bei der Eingabe der Weisungsempfehlung: Wird als Eingabefeld angeboten.
     * Bei der Eingabe der Weisungen durch den Insti: wird als Info-Feld angezeigt.
     */
    private String vorschlag = ".";

    /**Bei der Eingabe der Weisungen durch den Insti: wird als Eingabefeld angeboten.*/
    private String erteilteWeisung = ".";

    private boolean ausgewaehlt = false;

    public EclAbstimmungMitVorschlagM() {

    }

    public EclAbstimmungMitVorschlagM(EclAbstimmung pAbstimmung, int pEmpfehlung, int pWeisung) {
        copyFrom(pAbstimmung, pEmpfehlung, pWeisung);
    }

    public void copyFrom(EclAbstimmung pAbstimmung, int pEmpfehlung, int pWeisung) {
        ident = pAbstimmung.ident;

        nummer = pAbstimmung.nummer;
        nummerindex = pAbstimmung.nummerindex;
        nummerEN = pAbstimmung.nummerEN;
        nummerindexEN = pAbstimmung.nummerindexEN;

        anzeigeBezeichnungKurz = pAbstimmung.anzeigeBezeichnungKurz;
        anzeigeBezeichnungKurzEN = pAbstimmung.anzeigeBezeichnungKurzEN;

        identWeisungssatz = pAbstimmung.identWeisungssatz;

        if (identWeisungssatz == -1) {
            ueberschrift = true;
        }

        if (pEmpfehlung != 0) {
            vorschlag = KonstStimmart.getTextKurz(pEmpfehlung);
        }
        if (pWeisung != 0) {
            erteilteWeisung = KonstStimmart.getTextKurz(pWeisung);
        }

    }

    public void init() {
        ident = 0;
        nummer = "";
        nummerindex = "";
        nummerEN = "";
        nummerindexEN = "";
        anzeigeBezeichnungKurz = "";
        anzeigeBezeichnungKurzEN = "";
        identWeisungssatz = 0;
        ueberschrift = false;
        vorschlag = ".";
        erteilteWeisung = ".";
        ausgewaehlt = false;

    }

    /***********Standard getter und setter*******************************/

    public int getIdent() {
        return ident;
    }

    public void setIdent(int ident) {
        this.ident = ident;
    }

    public String getNummer() {
        return nummer;
    }

    public void setNummer(String nummer) {
        this.nummer = nummer;
    }

    public String getNummerindex() {
        return nummerindex;
    }

    public void setNummerindex(String nummerindex) {
        this.nummerindex = nummerindex;
    }

    public String getNummerEN() {
        return nummerEN;
    }

    public void setNummerEN(String nummerEN) {
        this.nummerEN = nummerEN;
    }

    public String getAnzeigeBezeichnungKurz() {
        return anzeigeBezeichnungKurz;
    }

    public void setAnzeigeBezeichnungKurz(String anzeigeBezeichnungKurz) {
        this.anzeigeBezeichnungKurz = anzeigeBezeichnungKurz;
    }

    public String getAnzeigeBezeichnungKurzEN() {
        return anzeigeBezeichnungKurzEN;
    }

    public void setAnzeigeBezeichnungKurzEN(String anzeigeBezeichnungKurzEN) {
        this.anzeigeBezeichnungKurzEN = anzeigeBezeichnungKurzEN;
    }

    public int getIdentWeisungssatz() {
        return identWeisungssatz;
    }

    public void setIdentWeisungssatz(int identWeisungssatz) {
        this.identWeisungssatz = identWeisungssatz;
    }

    public boolean isUeberschrift() {
        return ueberschrift;
    }

    public void setUeberschrift(boolean ueberschrift) {
        this.ueberschrift = ueberschrift;
    }

    public String getVorschlag() {
        return vorschlag;
    }

    public void setVorschlag(String vorschlag) {
        this.vorschlag = vorschlag;
    }

    public String getNummerindexEN() {
        return nummerindexEN;
    }

    public void setNummerindexEN(String nummerindexEN) {
        this.nummerindexEN = nummerindexEN;
    }

    public boolean isAusgewaehlt() {
        return ausgewaehlt;
    }

    public void setAusgewaehlt(boolean ausgewaehlt) {
        this.ausgewaehlt = ausgewaehlt;
    }

    public String getErteilteWeisung() {
        return erteilteWeisung;
    }

    public void setErteilteWeisung(String erteilteWeisung) {
        this.erteilteWeisung = erteilteWeisung;
    }

}
