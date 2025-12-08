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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import jakarta.validation.constraints.Size;

/**Steuert,  inwieweit Wortmeldungen mit einem Status in dem aktuellen View angezeigt bzw. summiert werden*/
public class EclWortmeldetischViewStatus implements Serializable {
    private static final long serialVersionUID = 5260485665243358107L;

    public int setNr = 0;

    /**Verweis auf EclWortmeldetischView
     * LEN=60*/
    @Size(min = 0, max = 30, message = "viewBezeichnung maximale Länge: {max}")
    public String viewBezeichnung = "";

    /**Verweis auf EclWortmeldetischStatus*/
    @Size(min = 0, max = 60, message = "statusBezeichnung maximale Länge: {max}")
    public String statusBezeichnung = "";

    /**Wortmeldung mit diesem Status wird in dieser View - im Detail - angezeigt
     * LEN=80*/
    public int anzeigeDesStatusInDieserView = 0;

    /**Anzeige für diesen Status in dieser View
     * LEN=60*/
    @Size(min = 0, max = 60, message = "anzeigeTextInDieserView maximale Länge: {max}")
    public String anzeigeTextInDieserView = "";

    /**Wortmeldungen mit diesem Status wird in der jeweiligen Summe in diesem View berücksichtig (unabhängig davon,
     * ob Wortmeldung in diesem View - im Detail - angezeigt wird)
     * 
     * Summe=0 oder 1
     * 
     * Achtung - wird aus den Status-Beschreibungen des Haupt-Subs gebildet! (Nicht der Subviews ...)
     */
    public int[] aufaddierenInSumme = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };

    /**********************Nicht in Datenbank***********************/
    /**für Aufbereitung in Parameter*/
    public List<EclWortmeldetischFolgeStatus> wortmeldetischFolgeStatusList = new LinkedList<EclWortmeldetischFolgeStatus>();

    /**Übertragen von EclWortmeldungStatus - zum Zugriff aus JSF heraus*/
    public int rederaumErgaenzen = 0;
    public int testraumErgaenzen = 0;

//  Funktion fuer Excel Import
    public static EclWortmeldetischViewStatus create(Map<String, String> map) {

        EclWortmeldetischViewStatus o = new EclWortmeldetischViewStatus();

        for (var entry : map.entrySet()) {

            final String dbColumn = entry.getKey().replaceAll("[0-9]", "");
            final String toInt = entry.getValue().matches("\\d+") ? entry.getValue() : "-1";

            int x = 0;
            final String check = entry.getKey().replaceAll("\\D+", "");
            if (!check.isBlank())
                x = Integer.parseInt(check);

            switch (dbColumn) {
            case "setNr":
                o.setNr = Integer.parseInt(toInt);
                break;
            case "viewBezeichnung":
                o.viewBezeichnung = entry.getValue();
                break;
            case "statusBezeichnung":
                o.statusBezeichnung = entry.getValue();
                break;
            case "anzeigeDesStatusInDieserView":
                o.anzeigeDesStatusInDieserView = Integer.parseInt(toInt);
                break;
            case "anzeigeTextInDieserView":
                o.anzeigeTextInDieserView = entry.getValue();
                break;
            case "aufaddierenInSumme":
                o.aufaddierenInSumme[x] = Integer.parseInt(toInt);
                break;
            default:
                break;
            }
        }
        return o;
    }

    /*****************************Standard getter und setter*******************************************/
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

    public String getStatusBezeichnung() {
        return statusBezeichnung;
    }

    public void setStatusBezeichnung(String statusBezeichnung) {
        this.statusBezeichnung = statusBezeichnung;
    }

    public int getAnzeigeDesStatusInDieserView() {
        return anzeigeDesStatusInDieserView;
    }

    public void setAnzeigeDesStatusInDieserView(int anzeigeDesStatusInDieserView) {
        this.anzeigeDesStatusInDieserView = anzeigeDesStatusInDieserView;
    }

    public String getAnzeigeTextInDieserView() {
        return anzeigeTextInDieserView;
    }

    public void setAnzeigeTextInDieserView(String anzeigeTextInDieserView) {
        this.anzeigeTextInDieserView = anzeigeTextInDieserView;
    }

    public int[] getAufaddierenInSumme() {
        return aufaddierenInSumme;
    }

    public void setAufaddierenInSumme(int[] aufaddierenInSumme) {
        this.aufaddierenInSumme = aufaddierenInSumme;
    }

    public List<EclWortmeldetischFolgeStatus> getWortmeldetischFolgeStatusList() {
        return wortmeldetischFolgeStatusList;
    }

    public void setWortmeldetischFolgeStatusList(List<EclWortmeldetischFolgeStatus> wortmeldetischFolgeStatusList) {
        this.wortmeldetischFolgeStatusList = wortmeldetischFolgeStatusList;
    }

    public int getRederaumErgaenzen() {
        return rederaumErgaenzen;
    }

    public void setRederaumErgaenzen(int rederaumErgaenzen) {
        this.rederaumErgaenzen = rederaumErgaenzen;
    }

    public int getTestraumErgaenzen() {
        return testraumErgaenzen;
    }

    public void setTestraumErgaenzen(int testraumErgaenzen) {
        this.testraumErgaenzen = testraumErgaenzen;
    }

}
