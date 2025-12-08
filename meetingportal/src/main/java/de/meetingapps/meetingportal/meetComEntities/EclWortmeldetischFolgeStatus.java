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

/**Je Status: Folgestatusse - abhängig vom Anzeiger (Koordination etc.)*/
public class EclWortmeldetischFolgeStatus implements Serializable {
    private static final long serialVersionUID = -1184813898086708231L;

    public int setNr = 0;

    /**Kann leer sein - dann gilt dies für alle Views - mit Ausnahme des Detail-Views*/
    @Size(min = 0, max = 60, message = "viewBezeichnung maximale Länge: {max}")
    public String viewBezeichnung = "";

    @Size(min = 0, max = 60, message = "ursprungsStatusBezeichnung maximale Länge: {max}")
    public String ursprungsStatusBezeichnung = "";

    @Size(min = 0, max = 60, message = "folgeStatusBezeichnung maximale Länge: {max}")
    public String folgeStatusBezeichnung = "";

    /**LEN=80*/
    @Size(min = 0, max = 80, message = "buttonBezeichnung maximale Länge: {max}")
    public String buttonBezeichnung = "";

    /**0 = ganz normaler Button. 
     * 1=es wird ein Button je Testraum generiert, Testraum-Nummer wird jeweils hinter buttonbezeichnung angehängt
     * 2=analog 1, nur für Rederaum
     */
    public int buttonJeRaum = 0;

    public int positionInButtonListe = 0;

//  Funktion fuer Excel Import
    public static EclWortmeldetischFolgeStatus create(Map<String, String> map) {

        EclWortmeldetischFolgeStatus o = new EclWortmeldetischFolgeStatus();

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
            case "buttonBezeichnung":
                o.buttonBezeichnung = entry.getValue();
                break;
            case "buttonJeRaum":
                o.buttonJeRaum = Integer.parseInt(toInt);
                break;
            case "positionInButtonListe":
                o.positionInButtonListe = Integer.parseInt(toInt);
                break;
            default:
                break;
            }
        }
        return o;
    }

    /********************Standard getter und setter********************************/
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

    public String getButtonBezeichnung() {
        return buttonBezeichnung;
    }

    public void setButtonBezeichnung(String buttonBezeichnung) {
        this.buttonBezeichnung = buttonBezeichnung;
    }

    public int getButtonJeRaum() {
        return buttonJeRaum;
    }

    public void setButtonJeRaum(int buttonJeRaum) {
        this.buttonJeRaum = buttonJeRaum;
    }

    public int getPositionInButtonListe() {
        return positionInButtonListe;
    }

    public void setPositionInButtonListe(int positionInButtonListe) {
        this.positionInButtonListe = positionInButtonListe;
    }

}
