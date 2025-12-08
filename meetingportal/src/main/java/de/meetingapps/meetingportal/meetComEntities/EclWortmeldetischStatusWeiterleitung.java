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

/**Automatische Weiterleitung - Bedingungsabhängig - in anderen Status*/
public class EclWortmeldetischStatusWeiterleitung implements Serializable {
    private static final long serialVersionUID = 4793504137481899322L;

    public int setNr = 0;

    /**Kann leer sein - dann gilt dies für alle Views - mit Ausnahme des Detail-Views*/
    @Size(min = 0, max = 60, message = "viewBezeichnung maximale Länge: {max}")
    public String viewBezeichnung = "";

    @Size(min = 0, max = 60, message = "ursprungsStatusBezeichnung maximale Länge: {max}")
    public String ursprungsStatusBezeichnung = "";

    @Size(min = 0, max = 60, message = "folgeStatusBezeichnung maximale Länge: {max}")
    public String folgeStatusBezeichnung = "";

    /**LEN=60
     * Bedingung, die erfüllt sein muß
     * 
     * Zulässige Bedingungen:
     * KEINEN_TEST_DURCHFUEHREN erfüllt, wenn der Parameter so steht dass keine Tests durchgeführt werden sollen (vollkommen unabhängig
     * vom Teststatus des Teilnehmers)
     * AUTOMATISCH_IN_REDNERLISTE erfüllt, wenn eine Wortmeldung automatisch in Rednerliste aufgenommen werden soll (und nicht erst auf manuelle
     * Freigabe gewartet werden soll)
     * BEREITS_GETESTET erfüllt, wenn der Aktionär bereits im Testraum erfolgreich getestet wurde.
     * */
    @Size(min = 0, max = 60, message = "nurWennBedingungErfuellt maximale Länge: {max}")
    public String nurWennBedingungErfuellt = "";

//  Funktion fuer Excel Import
    public static EclWortmeldetischStatusWeiterleitung create(Map<String, String> map) {

        EclWortmeldetischStatusWeiterleitung o = new EclWortmeldetischStatusWeiterleitung();

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
            case "ursprungsStatusBezeichnung":
                o.ursprungsStatusBezeichnung = entry.getValue();
                break;
            case "folgeStatusBezeichnung":
                o.folgeStatusBezeichnung = entry.getValue();
                break;
            case "nurWennBedingungErfuellt":
                o.nurWennBedingungErfuellt = entry.getValue();
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

    public String getUrsprungsStatusBezeichnung() {
        return ursprungsStatusBezeichnung;
    }

    public void setUrsprungsStatusBezeichnung(String ursprungsStatusBezeichnung) {
        this.ursprungsStatusBezeichnung = ursprungsStatusBezeichnung;
    }

    public String getFolgeStatusBezeichnung() {
        return folgeStatusBezeichnung;
    }

    public void setFolgeStatusBezeichnung(String folgeStatusBezeichnung) {
        this.folgeStatusBezeichnung = folgeStatusBezeichnung;
    }

    public String getNurWennBedingungErfuellt() {
        return nurWennBedingungErfuellt;
    }

    public void setNurWennBedingungErfuellt(String nurWennBedingungErfuellt) {
        this.nurWennBedingungErfuellt = nurWennBedingungErfuellt;
    }

}
