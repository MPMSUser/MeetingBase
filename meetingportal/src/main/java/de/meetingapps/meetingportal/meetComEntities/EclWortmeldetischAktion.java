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
package de.meetingapps.meetingportal.meetComEntities;

import java.io.Serializable;
import java.util.Map;

import jakarta.validation.constraints.Size;

public class EclWortmeldetischAktion implements Serializable {
    private static final long serialVersionUID = 1130175527173617805L;

    public int setNr = 0;

    /**Kann leer sein - dann gilt dies für alle Views - (auch für Detail-View)*/
    @Size(min = 0, max = 60, message = "viewBezeichnung maximale Länge: {max}")
    public String viewBezeichnung = "";

    /**LEN=60
     * Wenn leer, dann jeder Wechsel der zu zuStatusBezeichnung führt*/
    @Size(min = 0, max = 60, message = "vonStatusBezeichnung maximale Länge: {max}")
    public String vonStatusBezeichnung = "";

    /**LEN=60
     * Wenn leer, dann jeder Wechsel der von vonStatusBezeichnung führt*/
    @Size(min = 0, max = 60, message = "zuStatusBezeichnung maximale Länge: {max}")
    public String zuStatusBezeichnung = "";

    /**LEN=60
     * Aktion, die ausgeführt werden soll
     * SETZE_TESTSTATUS_TEILNEHMER_ERFOLGREICH
     * SETZE_TESTSTATUS_TEILNEHMER_NICHT_ERFOLGREICH
     * 
     * FORDERE_AUF_IN_TESTRAUM_KOMMEN
     * FORDERE_LINK_FUER_TESTRAUM_AN
     * FORDERE_AUF_TESTRAUM_VERLASSEN
     * FORDERE_AUF_TESTRAUM_VERLASSEN_NICHT_OK
     * 
     * FORDERE_AUF_IN_REDERAUM_KOMMEN
     * FORDERE_LINK_FUER_REDERAUM_AN
     * FORDERE_AUF_REDERAUM_VERLASSEN
     * FORDERE_AUF_SPRECHEN
     * 
     * AKTUALISIERE_VL_VIEW
     * 
     * FORDERE_AUF_TEST_NICHT_ERREICHT
     * FORDERE_AUF_REDE_NICHT_ERREICHT
     * */
    @Size(min = 0, max = 60, message = "aktionBezeichnung maximale Länge: {max}")
    public String aktionBezeichnung = "";

    /**LEN=60
     * Bedingung, die erfüllt sein muß
     *
     * Derzeit nur vorsorglich enthalten - keine Bedingungen bekannt / implementiert
     * */
    @Size(min = 0, max = 60, message = "nurWennBedingungErfuellt maximale Länge: {max}")
    public String nurWennBedingungErfuellt = "";

    public int reihenfolge = 0;

//  Funktion fuer Excel Import
    public static EclWortmeldetischAktion create(Map<String, String> map) {

        EclWortmeldetischAktion o = new EclWortmeldetischAktion();

        for (var entry : map.entrySet()) {

            final String dbColumn = entry.getKey().replaceAll("[0-9]", "");
            final String toInt = entry.getValue().matches("\\d+") ? entry.getValue() : "-1";

            switch (dbColumn) {
            case "setNr":
                o.setNr = Integer.parseInt(toInt);
                break;
            case "viewBezeichnung":
                o.viewBezeichnung = entry.getValue();
                break;
            case "vonStatusBezeichnung":
                o.vonStatusBezeichnung = entry.getValue();
                break;
            case "zuStatusBezeichnung":
                o.zuStatusBezeichnung = entry.getValue();
                break;
            case "aktionBezeichnung":
                o.aktionBezeichnung = entry.getValue();
                break;
            case "nurWennBedingungErfuellt":
                o.nurWennBedingungErfuellt = entry.getValue();
                break;
            case "reihenfolge":
                o.reihenfolge = Integer.parseInt(toInt);
                break;
            default:
                break;
            }
        }
        return o;
    }

    public int getSetNr() {
        return setNr;
    }

    public void setSetNr(int setNr) {
        this.setNr = setNr;
    }

    public String getViewBezeichnung() {
        return viewBezeichnung;
    }

    public void setViewBezeichnung(String viewBezeichnung) {
        this.viewBezeichnung = viewBezeichnung;
    }

    public String getVonStatusBezeichnung() {
        return vonStatusBezeichnung;
    }

    public void setVonStatusBezeichnung(String vonStatusBezeichnung) {
        this.vonStatusBezeichnung = vonStatusBezeichnung;
    }

    public String getZuStatusBezeichnung() {
        return zuStatusBezeichnung;
    }

    public void setZuStatusBezeichnung(String zuStatusBezeichnung) {
        this.zuStatusBezeichnung = zuStatusBezeichnung;
    }

    public String getAktionBezeichnung() {
        return aktionBezeichnung;
    }

    public void setAktionBezeichnung(String aktionBezeichnung) {
        this.aktionBezeichnung = aktionBezeichnung;
    }

    public String getNurWennBedingungErfuellt() {
        return nurWennBedingungErfuellt;
    }

    public void setNurWennBedingungErfuellt(String nurWennBedingungErfuellt) {
        this.nurWennBedingungErfuellt = nurWennBedingungErfuellt;
    }

    public int getReihenfolge() {
        return reihenfolge;
    }

    public void setReihenfolge(int reihenfolge) {
        this.reihenfolge = reihenfolge;
    }

}
