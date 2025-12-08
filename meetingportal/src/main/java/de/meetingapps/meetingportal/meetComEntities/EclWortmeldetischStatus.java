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

/**Definiert die zur Verfügung stehende Statusse*/
public class EclWortmeldetischStatus implements Serializable {
    private static final long serialVersionUID = -75900947564979386L;

    /**Wird als Verweis in EclMitteilung bei Wortmeldungen verwendet. Darf deshalb - nach Eingabe von
     * Wortmeldungen - nicht mehr verändert werden!
     * 
     * Folgende Nummern müssen fest für diese Stati vergeben werden:
     * 0 GESTELLT
     * 7 ZURUECKGEZOGEN
     */
    public int statusIdent = 0;

    public int setNr = 0;

    /**Wird als Referenz verwendet 
     * LEN=60*/
    @Size(min = 0, max = 60, message = "statusBezeichnung maximale Länge: {max}")
    public String statusBezeichnung = "";

    /**LEN=400*/
    @Size(min = 0, max = 400, message = "statusBeschreibung maximale Länge: {max}")
    public String statusBeschreibung = "";

    /**Rederaum ergänzen in Status in EclMitteilung*/
    public int rederaumErgaenzen = 0;

    /**Testraum ergänzen in Status in EclMitteilung*/
    public int testraumErgaenzen = 0;

    /**Falls 1, dann wird eine Wortmeldung, die diesen Status erhält,
     * ans Ende der Rednerliste gesetzt
     */
    public int beiEintrittAnsEnde = 0;

    /**-------------------Wird verwendet für Anzeige bei Aktionären----------------------------*/
    public int inRednerlisteDesTeilnehmersAnzeigen = 0;
    public int zurueckziehenMoeglichDurchTeilnehmer = 0;

    /**Reservierte Nummern und deren Standardverwendung:
     * 1158
     *  Default-Wert, wenn keine Nummer eingetragen
     *  GESTELLT
     *  WARTEND_FUER_AUFNAHMEN_IN_REDNERLISTE
     * 1159
     *  IN_REDNERLISTE
     *  IN_REDNERLISTE_WIRD_ANGERUFEN
     *  IN_REDNERLISTE_AUFRUFEN_IN_LEITUNG
     *  IN_REDNERLISTE_WIRD_ANGERUFEN_VERSUCH_2
     *  IN_REDNERLISTE_VERKUENDEN_AUFRUF_WIRD_WIEDERHOLT
     * 1160
     *  IN_REDNERLISTE_NICHT_ERREICHT_BEREIT_FUER_VERSUCH_2
     *  IN_REDNERLISTE_AUFRUFEN_NICHT_ERREICHT
     *  IN_REDNERLISTE_AUFRUFEN_NICHT_ERREICHT_VERSUCH_2
     * 1161
     *  TEST_BEREIT_ZUM_TEST
     *  TEST_VERSUCH
     *  TEST_VERSUCH_NICHT_ERREICHT
     *  TEST_VERSUCH_2
     *  TEST_VERSUCH_2_NICHT_ERREICHT
     *  TEST_NICHT_ERFOLGREICH
     * 1162
     *  ERLEDIGT_NICHT_ERREICHT
     * 1163
     *  ERLEDIGT_GESPROCHEN
     * 1164
     *  ERLEDIGT_ABGEWIESEN
     * 1194
     *  ZURUECKGEZOGEN
     * 2081
     *  SPRICHT
     * 2097
     *  ERLEDIGT_TEST_FEHLGESCHLAGEN
     * 
     */
    public int textNummerFuerDiesenStatus = 0;

//  Funktion fuer Excel Import
    public static EclWortmeldetischStatus create(Map<String, String> map) {

        EclWortmeldetischStatus o = new EclWortmeldetischStatus();

        for (var entry : map.entrySet()) {

            final String dbColumn = entry.getKey().replaceAll("[0-9]", "");
            final String toInt = entry.getValue().matches("\\d+") ? entry.getValue() : "-1";

            switch (dbColumn) {
            case "statusIdent":
                o.statusIdent = Integer.parseInt(toInt);
                break;
            case "setNr":
                o.setNr = Integer.parseInt(toInt);
                break;
            case "statusBezeichnung":
                o.statusBezeichnung = entry.getValue();
                break;
            case "statusBeschreibung":
                o.statusBeschreibung = entry.getValue();
                break;
            case "rederaumErgaenzen":
                o.rederaumErgaenzen = Integer.parseInt(toInt);
                break;
            case "testraumErgaenzen":
                o.testraumErgaenzen = Integer.parseInt(toInt);
                break;
            case "beiEintrittAnsEnde":
                o.beiEintrittAnsEnde = Integer.parseInt(toInt);
                break;
            case "inRednerlisteDesTeilnehmersAnzeigen":
                o.inRednerlisteDesTeilnehmersAnzeigen = Integer.parseInt(toInt);
                break;
            case "zurueckziehenMoeglichDurchTeilnehmer":
                o.zurueckziehenMoeglichDurchTeilnehmer = Integer.parseInt(toInt);
                break;
            case "textNummerFuerDiesenStatus":
                o.textNummerFuerDiesenStatus = Integer.parseInt(toInt);
                break;
            default:
                break;
            }
        }
        return o;
    }

    public int getStatusIdent() {
        return statusIdent;
    }

    public void setStatusIdent(int statusIdent) {
        this.statusIdent = statusIdent;
    }

    public int getSetNr() {
        return setNr;
    }

    public void setSetNr(int setNr) {
        this.setNr = setNr;
    }

    public String getStatusBezeichnung() {
        return statusBezeichnung;
    }

    public void setStatusBezeichnung(String statusBezeichnung) {
        this.statusBezeichnung = statusBezeichnung;
    }

    public String getStatusBeschreibung() {
        return statusBeschreibung;
    }

    public void setStatusBeschreibung(String statusBeschreibung) {
        this.statusBeschreibung = statusBeschreibung;
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

    public int getBeiEintrittAnsEnde() {
        return beiEintrittAnsEnde;
    }

    public void setBeiEintrittAnsEnde(int beiEintrittAnsEnde) {
        this.beiEintrittAnsEnde = beiEintrittAnsEnde;
    }

    public int getInRednerlisteDesTeilnehmersAnzeigen() {
        return inRednerlisteDesTeilnehmersAnzeigen;
    }

    public void setInRednerlisteDesTeilnehmersAnzeigen(int inRednerlisteDesTeilnehmersAnzeigen) {
        this.inRednerlisteDesTeilnehmersAnzeigen = inRednerlisteDesTeilnehmersAnzeigen;
    }

    public int getZurueckziehenMoeglichDurchTeilnehmer() {
        return zurueckziehenMoeglichDurchTeilnehmer;
    }

    public void setZurueckziehenMoeglichDurchTeilnehmer(int zurueckziehenMoeglichDurchTeilnehmer) {
        this.zurueckziehenMoeglichDurchTeilnehmer = zurueckziehenMoeglichDurchTeilnehmer;
    }

    public int getTextNummerFuerDiesenStatus() {
        return textNummerFuerDiesenStatus;
    }

    public void setTextNummerFuerDiesenStatus(int textNummerFuerDiesenStatus) {
        this.textNummerFuerDiesenStatus = textNummerFuerDiesenStatus;
    }
}
