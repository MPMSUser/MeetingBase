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

public class EclWortmeldetischView implements Serializable {
    private static final long serialVersionUID = -5254454387774008620L;

    public int logDrucken = 10;

    /**Hinweis: diese Klasse wird sowohl für die Definition des "SuperView" (also des aus dem Menü heraus aufgerufenen View, der mehrere Sub-Views beinhaltet) als auch für
     * die Definition des Sub-Views verwendet. Welche Felder vom SuperView gezogen werden, ist jeweils angegeben
     */

    /**Ident - nur wegen eindeutigem Key, und wegen Sortierung
     * 
     * Hinweis: aktuell nur Werte <100 erlaubt (solange noch Übergangsfrist ist wg. früherem Status und Raum in einem Feld)*/
    public int viewIdent = 0;

    public int setNr = 0;

    /**Vorgegebene Views:
     * Teilnehmer (viewIdent 0)
     * Versammlungsleiter (viewIdent 1)
     * Detail (viewIdent 2)
     * 
     * LEN=60
     */
    @Size(min = 0, max = 60, message = "viewBezeichnung maximale Länge: {max}")
    public String viewBezeichnung = "";

    /**LEN=40
     * Wenn leer, dann keine Anzeige in Menü
     * SuperView*/
    @Size(min = 0, max = 40, message = "textInMenue maximale Länge: {max}")
    public String textInMenue = "";

    /**LEN=60
     * SuperView und SubView*/
    @Size(min = 0, max = 60, message = "textUeberschrift maximale Länge: {max}")
    public String textUeberschrift = "";

    /**Summenanzeige
     * Gliederung der Summen in Anzeige:
     * [0] bis [4] werden in separaten Zeilen angezeigt
     * [5] bis [9] in einer Zeile, mit ";" getrennt, in Klammern
     * 
     * SuperView (außer bei Versammlungsleiter - hier wird die Summe im SubView angezeigt
     * */
    public int[] summeAnzeigen = new int[10];
    /**LEN=100*/
    @Size(min = 0, max = 100, message = "summeTextVorZahl maximale Länge: {max}")
    public String[] summeTextVorZahl = new String[10];
    /**LEN=100*/
    @Size(min = 0, max = 100, message = "summeTextNachZahl maximale Länge: {max}")
    public String[] summeTextNachZahl = new String[10];

    /**SuperView*/
    public int aktualisierenButtonAnzeigen = 0;
    /**SuperView*/
    public int zurueckButtonAnzeigen = 0;

    /**1 => Sicht des Versammlungsleiters wird included. Nicht zulässig
     * bei View Versammlungsleiter :-)
     * 
     * SuperView
     */
    public int sichtVersammlungsleiterAnzeigen = 0;

    /**SuperView
     * Enthält die als Subviews anzuzeigenden View-Idents.
     * Wichtig: wenn hier nix eingetragen wird, wird gar nix angezeigt (außer ggf. Versammlungsleiterview).
     * D.h. es muß i.d.R. hier an erster Position die eigene View-Ident eingetragen werden ...
     * */
    public int[] subViewsIdent = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };

    /**SuperView*/
    public int teststationNrAnzeigen = 0;
    /**LEN=40
     * Wird auch verwendet, um Status um Testraum-Nr. zu ergänzen
     * SuperView*/
    @Size(min = 0, max = 40, message = "testStationBegriff maximale Länge: {max}")
    public String testStationBegriff = "";

    /**SuperView*/
    public int redestationNrAnzeigen = 0;
    /**LEN=40
     * Wird auch verwendet, um Status um Testraum-Nr. zu ergänzen
     * SuperView*/
    @Size(min = 0, max = 40, message = "redeStationBegriff maximale Länge: {max}")
    public String redeStationBegriff = "";

//    @Deprecated
//    /**Maximal 1 bis 10 zulässig.
//     * Muß auch in koordinationsView eingetragen werden - sonst wird der nicht angezeigt!*/
//    public int positionInKoordinationView=0;

    /**Wenn 1, dann ist in diesem View je Status die Detailbearbeitung zulässig
     * 
     * SubView*/
    public int detailBearbeitungZulassen = 0;

    /**Wenn 1, dann ist in diesem View je Status die Bearbeitung der Rednerreihenfolge - unabhängig von
     * der Detail-Bearbeitung -  zulässig
     * 
     * SubView*/
    public int reihenfolgeBearbeitungZulassen = 0;

 
    /**Wenn 1, dann ist in diesem View je Status die Bearbeitung des Redners (Name etc.) - unabhängig von
     * der Detail-Bearbeitung -  zulässig
     * 
     * SubView*/
    public int rednerBearbeitungZulassen = 0;

    /**Wenn 1, dann wird in diesem View angezeigt, ob der Redner präsente Aktionäre vertritt oder nicht.
     * Achtung - Lastintensiv, insbesondere für Insti-Redner oder lange Rednerlisten! Unbedingt nur in den
     * Listen anzeigen, wo es zwingend erforderlich ist!
     * 
     * SubView*/
    public int praesenzAnzeigeAusfuehren = 0;

    /**Push durchführen
     * SuperView*/
    public int pushFuerDiesenView = 0;

    /**Rederaum ergänzen in Status in Anzeige (für die Stati, bei denen dies ergänzt werden kann/soll)
     * 
     * SubView*/
    public int rederaumErgaenzen = 0;

    /**Testraum ergänzen in Status in Anzeige (für die Stati, bei denen dies ergänzt werden kann/soll)
     * SubView*/
    public int testraumErgaenzen = 0;

    /**Uhrzeit der Wortmeldung wird mit angezeigt
     * SubView*/
    public int uhrzeitErgaenzen = 0;

    /**SubView*/
    public int kontaktDatenAnzeigen = 1;
    /**SubView*/
    public int infoFelderMitteilungAnzeigen = 1;

    /**SubView*/
    public int kommentarInternAnzeigen = 1;
    /**SubView*/
    public int kommentarInternEingabeMoeglich = 1;
    /**SubView*/
    public int kommentarVersammlungsleiterAnzeigen = 1;
    /**SubView*/
    public int kommentarVersammlungsleiterEingabeMoeglich = 1;

    /**SubView*/
    public int nachrichtAnVersammlungsleiterMoeglich = 1;

    /**================Nicht in Datenbank========================================================*/
    /** für Aufbereitung in Parameter*/
    public EclWortmeldetischViewStatus[] statusArray = null;

    /**Gibt das Offset der summe[i] an in der Liste, die für die Summenanzeige aufbereitet wird*/
    public int[] summeOffsetInAnzeigeView = new int[10];
    public int[] summeOffsetInAnzeigeVersammlungsleiter = new int[10];

    /*
     * Funktion fuer ExcelImport
     */
    public static EclWortmeldetischView create(Map<String, String> map) {

        EclWortmeldetischView o = new EclWortmeldetischView();

        for (var entry : map.entrySet()) {

            final String dbColumn = entry.getKey().replaceAll("[0-9]", "");
            final String toInt = entry.getValue().matches("\\d+") ? entry.getValue() : "-1";

            int x = 0;
            final String check = entry.getKey().replaceAll("\\D+", "");
            if (!check.isBlank())
                x = Integer.parseInt(check);

            switch (dbColumn) {
            case "viewIdent":
                o.viewIdent = Integer.parseInt(toInt);
                break;
            case "setNr":
                o.setNr = Integer.parseInt(toInt);
                break;
            case "viewBezeichnung":
                o.viewBezeichnung = entry.getValue();
                break;
            case "textInMenue":
                o.textInMenue = entry.getValue();
                break;
            case "textUeberschrift":
                o.textUeberschrift = entry.getValue();
                break;
            case "summeAnzeigen":
                o.summeAnzeigen[x] = Integer.parseInt(toInt);
                break;
            case "summeTextVorZahl":
                o.summeTextVorZahl[x] = entry.getValue();
                break;
            case "summeTextNachZahl":
                o.summeTextNachZahl[x] = entry.getValue();
                break;
            case "aktualisierenButtonAnzeigen":
                o.aktualisierenButtonAnzeigen = Integer.parseInt(toInt);
                break;
            case "zurueckButtonAnzeigen":
                o.zurueckButtonAnzeigen = Integer.parseInt(toInt);
                break;
            case "sichtVersammlungsleiterAnzeigen":
                o.sichtVersammlungsleiterAnzeigen = Integer.parseInt(toInt);
                break;
            case "subViewsIdent":
                o.subViewsIdent[x] = Integer.parseInt(toInt);
                break;
            case "teststationNrAnzeigen":
                o.teststationNrAnzeigen = Integer.parseInt(toInt);
                break;
            case "testStationBegriff":
                o.testStationBegriff = entry.getValue();
                break;
            case "redestationNrAnzeigen":
                o.redestationNrAnzeigen = Integer.parseInt(toInt);
                break;
            case "redeStationBegriff":
                o.redeStationBegriff = entry.getValue();
                break;
            case "detailBearbeitungZulassen":
                o.detailBearbeitungZulassen = Integer.parseInt(toInt);
                break;
            case "pushFuerDiesenView":
                o.pushFuerDiesenView = Integer.parseInt(toInt);
                break;
            case "rederaumErgaenzen":
                o.rederaumErgaenzen = Integer.parseInt(toInt);
                break;
            case "testraumErgaenzen":
                o.testraumErgaenzen = Integer.parseInt(toInt);
                break;
            case "uhrzeitErgaenzen":
                o.uhrzeitErgaenzen = Integer.parseInt(toInt);
                break;
            case "kontaktDatenAnzeigen":
                o.kontaktDatenAnzeigen = Integer.parseInt(toInt);
                break;
            case "infoFelderMitteilungAnzeigen":
                o.infoFelderMitteilungAnzeigen = Integer.parseInt(toInt);
                break;
            case "kommentarInternAnzeigen":
                o.kommentarInternAnzeigen = Integer.parseInt(toInt);
                break;
            case "kommentarInternEingabeMoeglich":
                o.kommentarInternEingabeMoeglich = Integer.parseInt(toInt);
                break;
            case "kommentarVersammlungsleiterAnzeigen":
                o.kommentarVersammlungsleiterAnzeigen = Integer.parseInt(toInt);
                break;
            case "kommentarVersammlungsleiterEingabeMoeglich":
                o.kommentarVersammlungsleiterEingabeMoeglich = Integer.parseInt(toInt);
                break;
            case "nachrichtAnVersammlungsleiterMoeglich":
                o.nachrichtAnVersammlungsleiterMoeglich = Integer.parseInt(toInt);
                break;
            case "reihenfolgeBearbeitungZulassen":
                o.reihenfolgeBearbeitungZulassen = Integer.parseInt(toInt);
                break;
            case "rednerBearbeitungZulassen":
                o.rednerBearbeitungZulassen = Integer.parseInt(toInt);
                break;
            case "praesenzAnzeigeAusfuehren":
                o.praesenzAnzeigeAusfuehren = Integer.parseInt(toInt);
                break;
            default:
                break;
            }
        }
        return o;
    }

    /***********************************Standard getter und setter*****************************************************/

    public int getViewIdent() {
        return viewIdent;
    }

    public void setViewIdent(int viewIdent) {
        this.viewIdent = viewIdent;
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

    public String getTextInMenue() {
        return textInMenue;
    }

    public void setTextInMenue(String textInMenue) {
        this.textInMenue = textInMenue;
    }

    public String getTextUeberschrift() {
        return textUeberschrift;
    }

    public void setTextUeberschrift(String textUeberschrift) {
        this.textUeberschrift = textUeberschrift;
    }

    public int[] getSummeAnzeigen() {
        return summeAnzeigen;
    }

    public void setSummeAnzeigen(int[] summeAnzeigen) {
        this.summeAnzeigen = summeAnzeigen;
    }

    public String[] getSummeTextVorZahl() {
        return summeTextVorZahl;
    }

    public void setSummeTextVorZahl(String[] summeTextVorZahl) {
        this.summeTextVorZahl = summeTextVorZahl;
    }

    public String[] getSummeTextNachZahl() {
        return summeTextNachZahl;
    }

    public void setSummeTextNachZahl(String[] summeTextNachZahl) {
        this.summeTextNachZahl = summeTextNachZahl;
    }

    public int getAktualisierenButtonAnzeigen() {
        return aktualisierenButtonAnzeigen;
    }

    public void setAktualisierenButtonAnzeigen(int aktualisierenButtonAnzeigen) {
        this.aktualisierenButtonAnzeigen = aktualisierenButtonAnzeigen;
    }

    public int getZurueckButtonAnzeigen() {
        return zurueckButtonAnzeigen;
    }

    public void setZurueckButtonAnzeigen(int zurueckButtonAnzeigen) {
        this.zurueckButtonAnzeigen = zurueckButtonAnzeigen;
    }

    public int getSichtVersammlungsleiterAnzeigen() {
        return sichtVersammlungsleiterAnzeigen;
    }

    public void setSichtVersammlungsleiterAnzeigen(int sichtVersammlungsleiterAnzeigen) {
        this.sichtVersammlungsleiterAnzeigen = sichtVersammlungsleiterAnzeigen;
    }

    public int getTeststationNrAnzeigen() {
        return teststationNrAnzeigen;
    }

    public void setTeststationNrAnzeigen(int teststationNrAnzeigen) {
        this.teststationNrAnzeigen = teststationNrAnzeigen;
    }

    public String getTestStationBegriff() {
        return testStationBegriff;
    }

    public void setTestStationBegriff(String testStationBegriff) {
        this.testStationBegriff = testStationBegriff;
    }

    public int getRedestationNrAnzeigen() {
        return redestationNrAnzeigen;
    }

    public void setRedestationNrAnzeigen(int redestationNrAnzeigen) {
        this.redestationNrAnzeigen = redestationNrAnzeigen;
    }

    public String getRedeStationBegriff() {
        return redeStationBegriff;
    }

    public void setRedeStationBegriff(String redeStationBegriff) {
        this.redeStationBegriff = redeStationBegriff;
    }

    public int getDetailBearbeitungZulassen() {
        return detailBearbeitungZulassen;
    }

    public void setDetailBearbeitungZulassen(int detailBearbeitungZulassen) {
        this.detailBearbeitungZulassen = detailBearbeitungZulassen;
    }

    public int getPushFuerDiesenView() {
        return pushFuerDiesenView;
    }

    public void setPushFuerDiesenView(int pushFuerDiesenView) {
        this.pushFuerDiesenView = pushFuerDiesenView;
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

    public int getUhrzeitErgaenzen() {
        return uhrzeitErgaenzen;
    }

    public void setUhrzeitErgaenzen(int uhrzeitErgaenzen) {
        this.uhrzeitErgaenzen = uhrzeitErgaenzen;
    }

    public int getKontaktDatenAnzeigen() {
        return kontaktDatenAnzeigen;
    }

    public void setKontaktDatenAnzeigen(int kontaktDatenAnzeigen) {
        this.kontaktDatenAnzeigen = kontaktDatenAnzeigen;
    }

    public int getInfoFelderMitteilungAnzeigen() {
        return infoFelderMitteilungAnzeigen;
    }

    public void setInfoFelderMitteilungAnzeigen(int infoFelderMitteilungAnzeigen) {
        this.infoFelderMitteilungAnzeigen = infoFelderMitteilungAnzeigen;
    }

    public int getKommentarInternAnzeigen() {
        return kommentarInternAnzeigen;
    }

    public void setKommentarInternAnzeigen(int kommentarInternAnzeigen) {
        this.kommentarInternAnzeigen = kommentarInternAnzeigen;
    }

    public int getKommentarInternEingabeMoeglich() {
        return kommentarInternEingabeMoeglich;
    }

    public void setKommentarInternEingabeMoeglich(int kommentarInternEingabeMoeglich) {
        this.kommentarInternEingabeMoeglich = kommentarInternEingabeMoeglich;
    }

    public int getKommentarVersammlungsleiterAnzeigen() {
        return kommentarVersammlungsleiterAnzeigen;
    }

    public void setKommentarVersammlungsleiterAnzeigen(int kommentarVersammlungsleiterAnzeigen) {
        this.kommentarVersammlungsleiterAnzeigen = kommentarVersammlungsleiterAnzeigen;
    }

    public int getKommentarVersammlungsleiterEingabeMoeglich() {
        return kommentarVersammlungsleiterEingabeMoeglich;
    }

    public void setKommentarVersammlungsleiterEingabeMoeglich(int kommentarVersammlungsleiterEingabeMoeglich) {
        this.kommentarVersammlungsleiterEingabeMoeglich = kommentarVersammlungsleiterEingabeMoeglich;
    }

    public int getNachrichtAnVersammlungsleiterMoeglich() {
        return nachrichtAnVersammlungsleiterMoeglich;
    }

    public void setNachrichtAnVersammlungsleiterMoeglich(int nachrichtAnVersammlungsleiterMoeglich) {
        this.nachrichtAnVersammlungsleiterMoeglich = nachrichtAnVersammlungsleiterMoeglich;
    }

    public EclWortmeldetischViewStatus[] getStatusArray() {
        return statusArray;
    }

    public void setStatusArray(EclWortmeldetischViewStatus[] statusArray) {
        this.statusArray = statusArray;
    }

    public int[] getSummeOffsetInAnzeigeView() {
        return summeOffsetInAnzeigeView;
    }

    public void setSummeOffsetInAnzeigeView(int[] summeOffsetInAnzeigeView) {
        this.summeOffsetInAnzeigeView = summeOffsetInAnzeigeView;
    }

    public int[] getSummeOffsetInAnzeigeVersammlungsleiter() {
        return summeOffsetInAnzeigeVersammlungsleiter;
    }

    public void setSummeOffsetInAnzeigeVersammlungsleiter(int[] summeOffsetInAnzeigeVersammlungsleiter) {
        this.summeOffsetInAnzeigeVersammlungsleiter = summeOffsetInAnzeigeVersammlungsleiter;
    }

    public int[] getSubViewsIdent() {
        return subViewsIdent;
    }

    public void setSubViewsIdent(int[] subViewsIdent) {
        this.subViewsIdent = subViewsIdent;
    }

    public int getReihenfolgeBearbeitungZulassen() {
        return reihenfolgeBearbeitungZulassen;
    }

    public void setReihenfolgeBearbeitungZulassen(int reihenfolgeBearbeitungZulassen) {
        this.reihenfolgeBearbeitungZulassen = reihenfolgeBearbeitungZulassen;
    }

    public int getRednerBearbeitungZulassen() {
        return rednerBearbeitungZulassen;
    }

    public void setRednerBearbeitungZulassen(int rednerBearbeitungZulassen) {
        this.rednerBearbeitungZulassen = rednerBearbeitungZulassen;
    }

    public int getPraesenzAnzeigeAusfuehren() {
        return praesenzAnzeigeAusfuehren;
    }

    public void setPraesenzAnzeigeAusfuehren(int praesenzAnzeigeAusfuehren) {
        this.praesenzAnzeigeAusfuehren = praesenzAnzeigeAusfuehren;
    }
    
    
}
