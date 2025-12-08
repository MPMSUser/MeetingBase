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
package de.meetingapps.meetingclient.meetingBestand;

import java.util.function.Function;

import de.meetingapps.meetingclient.meetingClientOberflaechen.TableCellLongMitPunkt;
import de.meetingapps.meetingclient.meetingClientOberflaechen.TableCellMehrzeilig;
import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComBl.BlAbstimmungenWeisungen;
import de.meetingapps.meetingportal.meetComBl.BlSammelkarten;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclAbstimmung;
import de.meetingapps.meetingportal.meetComEntities.EclMeldung;
import de.meetingapps.meetingportal.meetComEntities.EclMeldungMitAktionaersWeisung;
import de.meetingapps.meetingportal.meetComEntities.EclStimmkarten;
import de.meetingapps.meetingportal.meetComEntities.EclWillensErklVollmachtenAnDritte;
import de.meetingapps.meetingportal.meetComEntities.EclZutrittskarten;
import de.meetingapps.meetingportal.meetComHVParam.ParamS;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

/** Übergreifende Funktionen für die div. Sammelkarten-Controller */
public class CtrlSammelkartenUebergreifend {

    /** The log drucken. */
    private int logDrucken = 10;

    /** The rc list sammelkarten. */
    ObservableList<MSammelkarten> rcListSammelkarten = null;

    /** The rc sammel meldung. */
    public EclMeldung[] rcSammelMeldung = null;

    /** The rc zutrittskarten array. */
    public EclZutrittskarten[][] rcZutrittskartenArray = null;

    /** The rc stimmkarten array. */
    public EclStimmkarten[][] rcStimmkartenArray = null;

    /** The rc willens erkl vollmachten an dritte array. */
    public EclWillensErklVollmachtenAnDritte[][] rcWillensErklVollmachtenAnDritteArray = null;

    /************************Funktionen für Sammelkarten-Table*************************************/
    /**Bereitet "das Gerüst" (d.h. einen leeren TableView mit entsprechenden Feldern)
     * für einen TableView für die Sammelkarten, basierend auf MSammelkarten, auf.
     * Anschließend noch z.B.
     *      tableSammelkarten.setPrefHeight(676);
     *      tableSammelkarten.setPrefWidth(1464);
     *  setzen,
     *  sowie die observable List zuordnen, z.B.
     *      tableSammelkarten.setItems(listSammelkarten);
     */
    public TableView<MSammelkarten> vorbereitenTableViewSammelkarten() {

        final double SMALL = 60.0;
        final double MEDIUM = 100.0;

        TableView<MSammelkarten> tableSammelkarten = new TableView<MSammelkarten>();
        tableSammelkarten.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<MSammelkarten, Integer> colMeldeIdent = new TableColumn<MSammelkarten, Integer>("Ident");
        colMeldeIdent.setCellValueFactory(new PropertyValueFactory<>("meldungsIdent"));
        colMeldeIdent.setSortType(TableColumn.SortType.DESCENDING);
        setColumnWidth(colMeldeIdent, tableSammelkarten, SMALL, true);
        tableSammelkarten.getColumns().add(colMeldeIdent);

        TableColumn<MSammelkarten, String> colZutrittsIdent = new TableColumn<MSammelkarten, String>("EK");
        colZutrittsIdent.setCellValueFactory(new PropertyValueFactory<>("zutrittsIdent"));
        colZutrittsIdent.setCellFactory(column -> {
            return new TableCellMehrzeilig<MSammelkarten, String>();
        });
        setColumnWidth(colZutrittsIdent, tableSammelkarten, SMALL, true);
        tableSammelkarten.getColumns().add(colZutrittsIdent);

        TableColumn<MSammelkarten, String> colStimmkarte = new TableColumn<MSammelkarten, String>("SK");
        colStimmkarte.setCellValueFactory(new PropertyValueFactory<>("stimmkarte"));
        setColumnWidth(colStimmkarte, tableSammelkarten, SMALL, true);
        tableSammelkarten.getColumns().add(colStimmkarte);

        TableColumn<MSammelkarten, String> colBezeichnung = new TableColumn<MSammelkarten, String>("Bezeichnung");
        colBezeichnung.setCellValueFactory(new PropertyValueFactory<>("bezeichnung"));
        colBezeichnung.setCellFactory(column -> {
            return new TableCellMehrzeilig<MSammelkarten, String>();
        });
        setColumnWidth(colBezeichnung, tableSammelkarten, MEDIUM, false);
        tableSammelkarten.getColumns().add(colBezeichnung);

        TableColumn<MSammelkarten, String> colKommentar = new TableColumn<MSammelkarten, String>("Kommentar");
        colKommentar.setCellValueFactory(new PropertyValueFactory<>("kommentar"));
        setColumnWidth(colKommentar, tableSammelkarten, MEDIUM, false);
        tableSammelkarten.getColumns().add(colKommentar);

        TableColumn<MSammelkarten, String> colAktien = new TableColumn<MSammelkarten, String>("Aktien");
        colAktien.setCellValueFactory(new PropertyValueFactory<>("stueckAktien"));
        colAktien.setCellFactory(column -> {
            return new TableCellLongMitPunkt<MSammelkarten, String>();
        });
        setColumnWidth(colAktien, tableSammelkarten, MEDIUM, true);
        tableSammelkarten.getColumns().add(colAktien);

        TableColumn<MSammelkarten, String> colGattungString = new TableColumn<MSammelkarten, String>("Gat.");
        colGattungString.setCellValueFactory(new PropertyValueFactory<>("gattungString"));
        setColumnWidth(colGattungString, tableSammelkarten, SMALL, true);
        tableSammelkarten.getColumns().add(colGattungString);

        TableColumn<MSammelkarten, String> colSkIstString = new TableColumn<MSammelkarten, String>("Art");
        colSkIstString.setCellValueFactory(new PropertyValueFactory<>("skIstString"));
        setColumnWidth(colSkIstString, tableSammelkarten, SMALL, true);
        tableSammelkarten.getColumns().add(colSkIstString);

        TableColumn<MSammelkarten, String> colSkOffenlegungString = new TableColumn<MSammelkarten, String>(
                "Offenlegung");
        colSkOffenlegungString.setCellValueFactory(new PropertyValueFactory<>("skOffenlegungString"));
        setColumnWidth(colSkOffenlegungString, tableSammelkarten, SMALL, true);
        tableSammelkarten.getColumns().add(colSkOffenlegungString);

        TableColumn<MSammelkarten, String> colVerdecktString = new TableColumn<MSammelkarten, String>("Weis.Verdeckt");
        colVerdecktString.setCellValueFactory(new PropertyValueFactory<>("verdecktString"));
        setColumnWidth(colVerdecktString, tableSammelkarten, SMALL, true);
        tableSammelkarten.getColumns().add(colVerdecktString);

        TableColumn<MSammelkarten, String> colSkWeisungsartZulaessigString = new TableColumn<MSammelkarten, String>(
                "Weisungsart");
        colSkWeisungsartZulaessigString.setCellValueFactory(new PropertyValueFactory<>("skWeisungsartZulaessigString"));
        setColumnWidth(colSkWeisungsartZulaessigString, tableSammelkarten, MEDIUM, true);
        tableSammelkarten.getColumns().add(colSkWeisungsartZulaessigString);

        /** Zwei Spalten zusammenfassen - Internet */
        TableColumn<MSammelkarten, String> colSkBuchbarInternet = new TableColumn<MSammelkarten, String>(
                "Standard Internet");

        TableColumn<MSammelkarten, String> colSkBuchbarInternetString = new TableColumn<MSammelkarten, String>(
                "Weisungen");
        colSkBuchbarInternetString.setCellValueFactory(new PropertyValueFactory<>("skBuchbarInternetString"));
        setColumnWidth(colSkBuchbarInternetString, tableSammelkarten, SMALL, true);
        colSkBuchbarInternet.getColumns().add(colSkBuchbarInternetString);

        TableColumn<MSammelkarten, String> colSkBuchbarVollmachtDritteString = new TableColumn<MSammelkarten, String>(
                "Vollm.Dritte");
        colSkBuchbarVollmachtDritteString
                .setCellValueFactory(new PropertyValueFactory<>("skBuchbarVollmachtDritteString"));
        setColumnWidth(colSkBuchbarVollmachtDritteString, tableSammelkarten, SMALL, true);
        colSkBuchbarInternet.getColumns().add(colSkBuchbarVollmachtDritteString);

        tableSammelkarten.getColumns().add(colSkBuchbarInternet);
        /** Ende-Zwei Spalten zusammenfassen - Internet */

        /** Dummy-Ober-Überschrift Papier */
        TableColumn<MSammelkarten, String> colSkBuchbarPapier = new TableColumn<MSammelkarten, String>("Stand.Papier");

        TableColumn<MSammelkarten, String> colSkBuchbarPapierString = new TableColumn<MSammelkarten, String>(
                "Weisungen");
        colSkBuchbarPapierString.setCellValueFactory(new PropertyValueFactory<>("skBuchbarPapierString"));
        setColumnWidth(colSkBuchbarPapierString, tableSammelkarten, SMALL, true);
        colSkBuchbarPapier.getColumns().add(colSkBuchbarPapierString);

        tableSammelkarten.getColumns().add(colSkBuchbarPapier);

        /** Ende Dummy-Ober-Überschrift */

        /** Zwei Spalten zusammenfassen - HV */
        TableColumn<MSammelkarten, String> colSkBuchbarHV = new TableColumn<MSammelkarten, String>("Standard Verl.HV");

        TableColumn<MSammelkarten, String> colSkBuchbarHVString = new TableColumn<MSammelkarten, String>("Weisungen");
        colSkBuchbarHVString.setCellValueFactory(new PropertyValueFactory<>("skBuchbarHVString"));
        setColumnWidth(colSkBuchbarHVString, tableSammelkarten, SMALL, true);
        colSkBuchbarHV.getColumns().add(colSkBuchbarHVString);

        TableColumn<MSammelkarten, String> colSkBuchbarVollmachtDritteHVString = new TableColumn<MSammelkarten, String>(
                "Vollm.Dritte");
        colSkBuchbarVollmachtDritteHVString
                .setCellValueFactory(new PropertyValueFactory<>("skBuchbarVollmachtDritteHVString"));
        setColumnWidth(colSkBuchbarVollmachtDritteHVString, tableSammelkarten, SMALL, true);
        colSkBuchbarHV.getColumns().add(colSkBuchbarVollmachtDritteHVString);

        tableSammelkarten.getColumns().add(colSkBuchbarHV);
        /** Ende-Zwei Spalten zusammenfassen - HV */

        TableColumn<MSammelkarten, Integer> colGruppe = new TableColumn<MSammelkarten, Integer>("Gruppe");
        colGruppe.setCellValueFactory(new PropertyValueFactory<>("gruppe"));
        setColumnWidth(colGruppe, tableSammelkarten, SMALL, true);
        tableSammelkarten.getColumns().add(colGruppe);

        TableColumn<MSammelkarten, String> colIstOderWarPraesent = new TableColumn<MSammelkarten, String>("Präsent");
        colIstOderWarPraesent.setCellValueFactory(new PropertyValueFactory<>("istOderWarPraesent"));
        setColumnWidth(colIstOderWarPraesent, tableSammelkarten, SMALL, true);
        tableSammelkarten.getColumns().add(colIstOderWarPraesent);

        TableColumn<MSammelkarten, String> colVollmachtDritte = new TableColumn<MSammelkarten, String>("Vertreter");
        colVollmachtDritte.setCellValueFactory(new PropertyValueFactory<>("vollmachtDritte"));
        colVollmachtDritte.setCellFactory(column -> {
            return new TableCellMehrzeilig<MSammelkarten, String>();
        });
        setColumnWidth(colVollmachtDritte, tableSammelkarten, MEDIUM, false);
        tableSammelkarten.getColumns().add(colVollmachtDritte);

        return tableSammelkarten;

    }

    /**Holt die Sammelkartendaten.
     * 
     * pSammelIdent=0 => alle, sonst übergebene Ident.
     * 
     * Rückgabe = Anzahl der gelesenen Sammelkarten (0 = keine gelesen!)
     * 
     * Ablage erfolgt in:
     * rcListSammelkarten
     * und
     * rcSammelMeldung
     * rcZutrittskartenArray
     * rcStimmkartenArray
     * rcWillensErklVollmachtenAnDritteArray
     */
    public int holeSammelkartenDaten(boolean pNurAktive, int pSammelIdent) {
        CaBug.druckeLog("Start", logDrucken, 10);
        int anz = 0;

        /** Schritt 1: alles von Datenbank laden. */

        DbBundle lDbBundle = new DbBundle();

        BlSammelkarten blSammelkarten = new BlSammelkarten(false, lDbBundle);
        int anzSammelkarten = blSammelkarten.holeSammelkartenDaten(pNurAktive, pSammelIdent);
        rcSammelMeldung = blSammelkarten.rcSammelMeldung;
        rcZutrittskartenArray = blSammelkarten.rcZutrittskartenArray;
        rcStimmkartenArray = blSammelkarten.rcStimmkartenArray;
        rcWillensErklVollmachtenAnDritteArray = blSammelkarten.rcWillensErklVollmachtenAnDritteArray;

        /*Schritt 2: rcSammelMeldung füllen*/
        rcListSammelkarten = FXCollections.observableArrayList();

        for (int i = 0; i < anzSammelkarten; i++) {
            EclMeldung aktuelleSammelMeldung = rcSammelMeldung[i];
            MSammelkarten aktuelleMSammelkarte = new MSammelkarten(aktuelleSammelMeldung, lDbBundle);

            rcListSammelkarten.add(aktuelleMSammelkarte);

            /*Zutrittskarten zusätzlich zuordnen*/
            anz = rcZutrittskartenArray[i].length;
            for (int i1 = 0; i1 < anz; i1++) {
                if (!rcZutrittskartenArray[i][i1].zutrittsIdentWurdeGesperrt()) {
                    aktuelleMSammelkarte.addZutrittsIdent(rcZutrittskartenArray[i][i1].zutrittsIdent);
                }
            }

            /*stimmkarten zusätzlich zuordnen*/
            anz = rcStimmkartenArray[i].length;
            for (int i1 = 0; i1 < anz; i1++) {
                if (rcStimmkartenArray[i][i1].stimmkarteWurdeGesperrt()) {
                    aktuelleMSammelkarte.addStimmkarte(rcStimmkartenArray[i][i1].stimmkarte);
                }
            }

            /*Bevollmächtigte hinzufügen (ohne stornierte!)*/
            if (rcWillensErklVollmachtenAnDritteArray[i] != null) {
                for (int i1 = 0; i1 < rcWillensErklVollmachtenAnDritteArray[i].length; i1++) {
                    if (!rcWillensErklVollmachtenAnDritteArray[i][i1].wurdeStorniert) {
                        aktuelleMSammelkarte.addVollmacht(rcWillensErklVollmachtenAnDritteArray[i][i1]);
                    }
                }
            }

        }
        CaBug.druckeLog("Ende", logDrucken, 10);
        return anzSammelkarten;
    }

    /****************************Funktionen für Aktionärs-Table-View**************************************/
    /**Bereitet "das Gerüst" (d.h. einen leeren TableView mit entsprechenden Feldern)
     * für einen TableView für die Aktionäre, basierend auf EclMeldungMitAktionaersWeisung, auf.
     * Anschließend noch z.B.
     *      tableAktionaer.setPrefHeight(676);
     *      tableAktionaere.setPrefWidth(1464);
     *  setzen,
     *  sowie die observable List zuordnen, z.B.
     *      tableAktionaere.setItems(listAktionaere);
     */
    public TableView<EclMeldungMitAktionaersWeisung> vorbereitenTableViewAktionaere(
            BlAbstimmungenWeisungen pBlAbstimmungenWeisungenErfassen, int pGattung) {

        TableView<EclMeldungMitAktionaersWeisung> tableAktionaere = new TableView<EclMeldungMitAktionaersWeisung>();

        TableColumn<EclMeldungMitAktionaersWeisung, Integer> colMeldungsIdent = new TableColumn<EclMeldungMitAktionaersWeisung, Integer>(
                "Ident");
        colMeldungsIdent.setCellValueFactory(new PropertyValueFactory<>("meldungsIdent"));
        colMeldungsIdent.setSortType(TableColumn.SortType.DESCENDING);
        tableAktionaere.getColumns().add(colMeldungsIdent);

        TableColumn<EclMeldungMitAktionaersWeisung, String> colAktionaersnummer = new TableColumn<EclMeldungMitAktionaersWeisung, String>(
                "AktionärsNr");
        colAktionaersnummer.setCellValueFactory(new PropertyValueFactory<>("aktionaersnummer"));
        tableAktionaere.getColumns().add(colAktionaersnummer);

        TableColumn<EclMeldungMitAktionaersWeisung, String> colZutrittsIdent = new TableColumn<EclMeldungMitAktionaersWeisung, String>(
                "EK-Nr");
        colZutrittsIdent.setCellValueFactory(new PropertyValueFactory<>("zutrittsIdent"));
        tableAktionaere.getColumns().add(colZutrittsIdent);

        TableColumn<EclMeldungMitAktionaersWeisung, String> colName = new TableColumn<EclMeldungMitAktionaersWeisung, String>(
                "Name");
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tableAktionaere.getColumns().add(colName);
        
        TableColumn<EclMeldungMitAktionaersWeisung, String> colOrt = new TableColumn<EclMeldungMitAktionaersWeisung, String>(
                "Ort");
        colOrt.setCellValueFactory(new PropertyValueFactory<>("ort"));
        setColumnWidth(colOrt, tableAktionaere, 70.0, false);
        tableAktionaere.getColumns().add(colOrt);

        TableColumn<EclMeldungMitAktionaersWeisung, String> colAktien = new TableColumn<EclMeldungMitAktionaersWeisung, String>(
                "Aktien");
        colAktien.setCellValueFactory(new PropertyValueFactory<>("stueckAktien"));
        colAktien.setCellFactory(column -> {
            return new TableCellLongMitPunkt<EclMeldungMitAktionaersWeisung, String>();
        });
        //        colAktienStimmen.setSortable(false);
        tableAktionaere.getColumns().add(colAktien);

        int anzahlAbstimmungen = pBlAbstimmungenWeisungenErfassen.liefereAnzAgendaArray(pGattung);
        for (int i = 0; i < anzahlAbstimmungen; i++) {
            EclAbstimmung lAbstimmung = pBlAbstimmungenWeisungenErfassen.rcAgendaArray[pGattung][i];

            String hUeberschrift = lAbstimmung.nummerKey + " " + lAbstimmung.nummerindexKey;
            int abstimmungsPosition = lAbstimmung.identWeisungssatz;
            if (abstimmungsPosition == -1) {
                abstimmungsPosition = 200;
            }
            TableColumn<EclMeldungMitAktionaersWeisung, String> colWeisung = new TableColumn<EclMeldungMitAktionaersWeisung, String>(
                    hUeberschrift);
            colWeisung.setCellValueFactory(
                    createArrayValueFactory(EclMeldungMitAktionaersWeisung::getAbgabeText, abstimmungsPosition));
            tableAktionaere.getColumns().add(colWeisung);
        }

        if (ParamS.param.paramAbstimmungParameter.weisungenGegenantraegeInternSeparat) {
            anzahlAbstimmungen = pBlAbstimmungenWeisungenErfassen.liefereAnzGegenantraegeArray(pGattung);
            for (int i = 0; i < anzahlAbstimmungen; i++) {
                EclAbstimmung lAbstimmung = pBlAbstimmungenWeisungenErfassen.rcGegenantraegeArray[pGattung][i];
                String hUeberschrift = lAbstimmung.nummerKey + " " + lAbstimmung.nummerindexKey;
                int abstimmungsPosition = lAbstimmung.identWeisungssatz;
                if (abstimmungsPosition == -1) {
                    abstimmungsPosition = 200;
                }
                TableColumn<EclMeldungMitAktionaersWeisung, String> colWeisung = new TableColumn<EclMeldungMitAktionaersWeisung, String>(
                        hUeberschrift);
                colWeisung.setCellValueFactory(
                        createArrayValueFactory(EclMeldungMitAktionaersWeisung::getAbgabeText, abstimmungsPosition));
                tableAktionaere.getColumns().add(colWeisung);
            }
        }

        TableColumn<EclMeldungMitAktionaersWeisung, String> colStimmausschluss = new TableColumn<EclMeldungMitAktionaersWeisung, String>(
                "StimmAusschl.");
        colStimmausschluss.setCellValueFactory(new PropertyValueFactory<>("stimmausschluss"));
        tableAktionaere.getColumns().add(colStimmausschluss);

        return tableAktionaere;

    }

    /**
     * 
     * @param col
     * @param tableView
     * @param width
     */
    private void setColumnWidth(TableColumn<?, ?> col, TableView<?> tableView, Double width, Boolean maxWidth) {

        col.setMinWidth(width);
        col.setPrefWidth(width);
        if (maxWidth)
            col.setMaxWidth(width);
    }

    /**
     * Creates the array value factory.
     *
     * @param <S>            the generic type
     * @param <T>            the generic type
     * @param arrayExtractor the array extractor
     * @param index          the index
     * @return the callback
     */
    static <S, T> Callback<TableColumn.CellDataFeatures<S, T>, ObservableValue<T>> createArrayValueFactory(
            Function<S, T[]> arrayExtractor, final int index) {
        if (index < 0) {
            return cd -> null;
        }
        return cd -> {
            T[] array = arrayExtractor.apply(cd.getValue());
            return array == null || array.length <= index ? null : new SimpleObjectProperty<>(array[index]);
        };
    }

}
