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
package de.meetingapps.meetingclient.meetingClientDialoge;

import de.meetingapps.meetingclient.meetingClientOberflaechen.TableCellLongMitPunkt;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * Verwendung in CtrlSuchen und CtrlSuchlaufDurchfuehren.
 */

public class CtrlSuchenAllgemein {

    /** The table neu. */
    private TableView<MSuchlaufErgebnis> tableNeu = new TableView<MSuchlaufErgebnis>();

    /** The l view nr. */
    private int lViewNr = 0;

    /** The l anzeige suchlauf. */
    private boolean lAnzeigeSuchlauf = false;

    /** The l anzeige register. */
    private boolean lAnzeigeRegister = false;

    /** The l anzeige meldungen. */
    private boolean lAnzeigeMeldungen = false;

    /** The l anzeige sonstie vollmachten. */
    private boolean lAnzeigeSonstieVollmachten = false;

    /**
     * Unterstützte pViewNr: 1=alle.
     *
     * @param pViewNr                    the view nr
     * @param pAnzeigeSuchlauf           the anzeige suchlauf
     * @param pAnzeigeRegister           the anzeige register
     * @param pAnzeigeMeldungen          the anzeige meldungen
     * @param pAnzeigeSonstieVollmachten the anzeige sonstie vollmachten
     * @return the table view
     */
    public TableView<MSuchlaufErgebnis> baueGrundAnsichtTableViewOhneInhalte(int pViewNr, boolean pAnzeigeSuchlauf,
            boolean pAnzeigeRegister, boolean pAnzeigeMeldungen, boolean pAnzeigeSonstieVollmachten) {
        lViewNr = pViewNr;
        lAnzeigeSuchlauf = pAnzeigeSuchlauf;
        lAnzeigeRegister = pAnzeigeRegister;
        lAnzeigeMeldungen = pAnzeigeMeldungen;
        lAnzeigeSonstieVollmachten = pAnzeigeSonstieVollmachten;

        if (lViewNr == 1) {
            colAktionaersnummer();
            colAktionaersname();
            colAktionaersort();
            colBesitzart();
            colStueckAktien();

            colEkNr();
            colSkNr();
            colStueckAktienMeldung();
            colIstOderInSammelkarte();
            colVertreterNameOrt();

            colIstPraesent();
            colIstAngemeldet();
            colIstGesperrtString();

            colMeldeIdent();

            colIdentSuchlaufErgebnis();
            colEntstandenAusString();
            colEinzelSuchBegriff();
            colVeraenderungGegenueberLetztemSuchlaufString();
            colWurdeVerarbeitetString();
            colMeldungsText();
            colVerarbeitetNichtMehrInSuchergebnisString();
            colWurdeAusSucheAusgegrenztString();
            colAusgegrenztWeil();
            colParameter1();
            colParameter2();
            colParameter3();
            colParameter4();
            colParameter5();
            colGefundeneVollmachtName();

            colVollmachtNameOrt();

            colLaufendeNummerInArray();

            col();
        }

        return tableNeu;
    }

    /*****************Einzel-Felder aufbereiten für baueGrundAnsichtTableViewOhneInhalte()************
     * Wenn ein Feld aufgerufen wird, das nicht angezeigt werden darf (lAnzeigeSuchlauf etc.), 
     * erfolgt nichts :-)
     */
    private void colLaufendeNummerInArray() {
        TableColumn<MSuchlaufErgebnis, Integer> laufendeNummerInArray = new TableColumn<MSuchlaufErgebnis, Integer>(
                "LfdNr");
        laufendeNummerInArray.setCellValueFactory(new PropertyValueFactory<>("laufendeNummerInArray"));
        laufendeNummerInArray.setSortType(TableColumn.SortType.DESCENDING);
        tableNeu.getColumns().add(laufendeNummerInArray);

    }

    /**
     * Col ident suchlauf ergebnis.
     */
    private void colIdentSuchlaufErgebnis() {
        if (lAnzeigeSuchlauf == false) {
            return;
        }
        TableColumn<MSuchlaufErgebnis, Integer> identSuchlaufErgebnis = new TableColumn<MSuchlaufErgebnis, Integer>(
                "Suchl.Erg.Ident");
        identSuchlaufErgebnis.setCellValueFactory(new PropertyValueFactory<>("identSuchlaufErgebnis"));
        tableNeu.getColumns().add(identSuchlaufErgebnis);
    }

    /**
     * Col entstanden aus string.
     */
    private void colEntstandenAusString() {
        if (lAnzeigeSuchlauf == false) {
            return;
        }
        TableColumn<MSuchlaufErgebnis, String> entstandenAusString = new TableColumn<MSuchlaufErgebnis, String>(
                "Such-Quelle");
        entstandenAusString.setCellValueFactory(new PropertyValueFactory<>("entstandenAusString"));
        tableNeu.getColumns().add(entstandenAusString);
    }

    /**
     * Col einzel such begriff.
     */
    private void colEinzelSuchBegriff() {
        if (lAnzeigeSuchlauf == false) {
            return;
        }
        TableColumn<MSuchlaufErgebnis, String> einzelSuchBegriff = new TableColumn<MSuchlaufErgebnis, String>(
                "Such-Begriff");
        einzelSuchBegriff.setCellValueFactory(new PropertyValueFactory<>("einzelSuchBegriff"));
        tableNeu.getColumns().add(einzelSuchBegriff);
    }

    /**
     * Col veraenderung gegenueber letztem suchlauf string.
     */
    private void colVeraenderungGegenueberLetztemSuchlaufString() {
        if (lAnzeigeSuchlauf == false) {
            return;
        }
        TableColumn<MSuchlaufErgebnis, String> veraenderungGegenueberLetztemSuchlaufString = new TableColumn<MSuchlaufErgebnis, String>(
                "Veränderung");
        veraenderungGegenueberLetztemSuchlaufString
                .setCellValueFactory(new PropertyValueFactory<>("veraenderungGegenueberLetztemSuchlaufString"));
        tableNeu.getColumns().add(veraenderungGegenueberLetztemSuchlaufString);
    }

    /**
     * Col wurde verarbeitet string.
     */
    private void colWurdeVerarbeitetString() {
        if (lAnzeigeSuchlauf == false) {
            return;
        }
        TableColumn<MSuchlaufErgebnis, String> wurdeVerarbeitetString = new TableColumn<MSuchlaufErgebnis, String>(
                "Verarbeitet");
        wurdeVerarbeitetString.setCellValueFactory(new PropertyValueFactory<>("wurdeVerarbeitetString"));
        tableNeu.getColumns().add(wurdeVerarbeitetString);
    }

    /**
     * Col meldungs text.
     */
    private void colMeldungsText() {
        if (lAnzeigeSuchlauf == false) {
            return;
        }
        TableColumn<MSuchlaufErgebnis, String> meldungsText = new TableColumn<MSuchlaufErgebnis, String>("Meldung");
        meldungsText.setCellValueFactory(new PropertyValueFactory<>("meldungsText"));
        tableNeu.getColumns().add(meldungsText);
    }

    /**
     * Col verarbeitet nicht mehr in suchergebnis string.
     */
    private void colVerarbeitetNichtMehrInSuchergebnisString() {
        if (lAnzeigeSuchlauf == false) {
            return;
        }
        TableColumn<MSuchlaufErgebnis, String> verarbeitetNichtMehrInSuchergebnisString = new TableColumn<MSuchlaufErgebnis, String>(
                "Verarb.Nicht mehr enthalten");
        verarbeitetNichtMehrInSuchergebnisString
                .setCellValueFactory(new PropertyValueFactory<>("verarbeitetNichtMehrInSuchergebnisString"));
        tableNeu.getColumns().add(verarbeitetNichtMehrInSuchergebnisString);
    }

    /**
     * Col wurde aus suche ausgegrenzt string.
     */
    private void colWurdeAusSucheAusgegrenztString() {
        if (lAnzeigeSuchlauf == false) {
            return;
        }
        TableColumn<MSuchlaufErgebnis, String> wurdeAusSucheAusgegrenztString = new TableColumn<MSuchlaufErgebnis, String>(
                "Ausgeblendet");
        wurdeAusSucheAusgegrenztString
                .setCellValueFactory(new PropertyValueFactory<>("wurdeAusSucheAusgegrenztString"));
        tableNeu.getColumns().add(wurdeAusSucheAusgegrenztString);
    }

    /**
     * Col ausgegrenzt weil.
     */
    private void colAusgegrenztWeil() {
        if (lAnzeigeSuchlauf == false) {
            return;
        }
        TableColumn<MSuchlaufErgebnis, String> ausgegrenztWeil = new TableColumn<MSuchlaufErgebnis, String>(
                "Ausgeblendet weil");
        ausgegrenztWeil.setCellValueFactory(new PropertyValueFactory<>("ausgegrenztWeil"));
        tableNeu.getColumns().add(ausgegrenztWeil);
    }

    /**
     * Col parameter 1.
     */
    private void colParameter1() {
        if (lAnzeigeSuchlauf == false) {
            return;
        }
        TableColumn<MSuchlaufErgebnis, String> parameter1 = new TableColumn<MSuchlaufErgebnis, String>("Wert 1");
        parameter1.setCellValueFactory(new PropertyValueFactory<>("parameter1"));
        tableNeu.getColumns().add(parameter1);
    }

    /**
     * Col parameter 2.
     */
    private void colParameter2() {
        if (lAnzeigeSuchlauf == false) {
            return;
        }
        TableColumn<MSuchlaufErgebnis, String> parameter2 = new TableColumn<MSuchlaufErgebnis, String>("Wert 2");
        parameter2.setCellValueFactory(new PropertyValueFactory<>("parameter2"));
        tableNeu.getColumns().add(parameter2);
    }

    /**
     * Col parameter 3.
     */
    private void colParameter3() {
        if (lAnzeigeSuchlauf == false) {
            return;
        }
        TableColumn<MSuchlaufErgebnis, String> parameter3 = new TableColumn<MSuchlaufErgebnis, String>("Wert 3");
        parameter3.setCellValueFactory(new PropertyValueFactory<>("parameter3"));
        tableNeu.getColumns().add(parameter3);
    }

    /**
     * Col parameter 4.
     */
    private void colParameter4() {
        if (lAnzeigeSuchlauf == false) {
            return;
        }
        TableColumn<MSuchlaufErgebnis, String> parameter4 = new TableColumn<MSuchlaufErgebnis, String>("Wert 4");
        parameter4.setCellValueFactory(new PropertyValueFactory<>("parameter4"));
        tableNeu.getColumns().add(parameter4);
    }

    /**
     * Col parameter 5.
     */
    private void colParameter5() {
        if (lAnzeigeSuchlauf == false) {
            return;
        }
        TableColumn<MSuchlaufErgebnis, String> parameter5 = new TableColumn<MSuchlaufErgebnis, String>("Wert 5");
        parameter5.setCellValueFactory(new PropertyValueFactory<>("parameter5"));
        tableNeu.getColumns().add(parameter5);
    }

    /**
     * Col aktionaersnummer.
     */
    private void colAktionaersnummer() {
        TableColumn<MSuchlaufErgebnis, String> aktionaersnummer = new TableColumn<MSuchlaufErgebnis, String>(
                "Aktionärsnummer");
        aktionaersnummer.setCellValueFactory(new PropertyValueFactory<>("aktionaersnummer"));
        tableNeu.getColumns().add(aktionaersnummer);
    }

    /**
     * Col aktionaersname.
     */
    private void colAktionaersname() {
        TableColumn<MSuchlaufErgebnis, String> aktionaersname = new TableColumn<MSuchlaufErgebnis, String>(
                "Aktionärs-Name");
        aktionaersname.setCellValueFactory(new PropertyValueFactory<>("aktionaersname"));
        tableNeu.getColumns().add(aktionaersname);
    }

    /**
     * Col aktionaersort.
     */
    private void colAktionaersort() {
        TableColumn<MSuchlaufErgebnis, String> aktionaersort = new TableColumn<MSuchlaufErgebnis, String>(
                "Aktionärs-Ort");
        aktionaersort.setCellValueFactory(new PropertyValueFactory<>("aktionaersort"));
        tableNeu.getColumns().add(aktionaersort);
    }

    /**
     * Col besitzart.
     */
    private void colBesitzart() {
        TableColumn<MSuchlaufErgebnis, String> besitzart = new TableColumn<MSuchlaufErgebnis, String>("Besitz");
        besitzart.setCellValueFactory(new PropertyValueFactory<>("besitzart"));
        tableNeu.getColumns().add(besitzart);
    }

    /**
     * Col stueck aktien.
     */
    private void colStueckAktien() {
        TableColumn<MSuchlaufErgebnis, String> stueckAktien = new TableColumn<MSuchlaufErgebnis, String>("Aktien");
        stueckAktien.setCellValueFactory(new PropertyValueFactory<>("stueckAktien"));
        stueckAktien.setCellFactory(column -> {
            return new TableCellLongMitPunkt<MSuchlaufErgebnis, String>();
        });
        tableNeu.getColumns().add(stueckAktien);
    }

    /**
     * Col melde ident.
     */
    private void colMeldeIdent() {
        if (lAnzeigeMeldungen == false) {
            return;
        }
        TableColumn<MSuchlaufErgebnis, Integer> meldeIdent = new TableColumn<MSuchlaufErgebnis, Integer>("MeldeIdent");
        meldeIdent.setCellValueFactory(new PropertyValueFactory<>("meldeIdent"));
        tableNeu.getColumns().add(meldeIdent);
    }

    /**
     * Col ek nr.
     */
    private void colEkNr() {
        if (lAnzeigeMeldungen == false) {
            return;
        }
        TableColumn<MSuchlaufErgebnis, String> ekNr = new TableColumn<MSuchlaufErgebnis, String>("EK-Nr");
        ekNr.setCellValueFactory(new PropertyValueFactory<>("ekNr"));
        tableNeu.getColumns().add(ekNr);
    }

    /**
     * Col sk nr.
     */
    private void colSkNr() {
        if (lAnzeigeMeldungen == false) {
            return;
        }
        TableColumn<MSuchlaufErgebnis, String> skNr = new TableColumn<MSuchlaufErgebnis, String>("SK-Nr");
        skNr.setCellValueFactory(new PropertyValueFactory<>("skNr"));
        tableNeu.getColumns().add(skNr);
    }

    /**
     * Col stueck aktien meldung.
     */
    private void colStueckAktienMeldung() {
        if (lAnzeigeMeldungen == false) {
            return;
        }
        TableColumn<MSuchlaufErgebnis, String> stueckAktienMeldung = new TableColumn<MSuchlaufErgebnis, String>(
                "Aktien gemeldet");
        stueckAktienMeldung.setCellValueFactory(new PropertyValueFactory<>("stueckAktienMeldung"));
        stueckAktienMeldung.setCellFactory(column -> {
            return new TableCellLongMitPunkt<MSuchlaufErgebnis, String>();
        });
        tableNeu.getColumns().add(stueckAktienMeldung);
    }

    /**
     * Col ist oder in sammelkarte.
     */
    private void colIstOderInSammelkarte() {
        if (lAnzeigeMeldungen == false) {
            return;
        }
        TableColumn<MSuchlaufErgebnis, String> istOderInSammelkarte = new TableColumn<MSuchlaufErgebnis, String>(
                "Sammelkarte");
        istOderInSammelkarte.setCellValueFactory(new PropertyValueFactory<>("istOderInSammelkarte"));
        tableNeu.getColumns().add(istOderInSammelkarte);
    }

    /**
     * Col vertreter name ort.
     */
    private void colVertreterNameOrt() {
        if (lAnzeigeMeldungen == false) {
            return;
        }
        TableColumn<MSuchlaufErgebnis, String> vertreterNameOrt = new TableColumn<MSuchlaufErgebnis, String>(
                "Vertreter (aktuell)");
        vertreterNameOrt.setCellValueFactory(new PropertyValueFactory<>("vertreterNameOrt"));
        tableNeu.getColumns().add(vertreterNameOrt);
    }

    /**
     * Col ist praesent.
     */
    private void colIstPraesent() {
        if (lAnzeigeMeldungen == false) {
            return;
        }
        TableColumn<MSuchlaufErgebnis, String> istPraesent = new TableColumn<MSuchlaufErgebnis, String>("Präsent");
        istPraesent.setCellValueFactory(new PropertyValueFactory<>("istPraesent"));
        tableNeu.getColumns().add(istPraesent);
    }

    /**
     * Col ist angemeldet.
     */
    private void colIstAngemeldet() {
        if (lAnzeigeMeldungen == false) {
            return;
        }
        TableColumn<MSuchlaufErgebnis, String> istAngemeldet = new TableColumn<MSuchlaufErgebnis, String>("Angemeldet");
        istAngemeldet.setCellValueFactory(new PropertyValueFactory<>("istAngemeldet"));
        tableNeu.getColumns().add(istAngemeldet);
    }

    /**
     * Col ist gesperrt string.
     */
    private void colIstGesperrtString() {
        if (lAnzeigeMeldungen == false) {
            return;
        }
        TableColumn<MSuchlaufErgebnis, String> istGesperrtString = new TableColumn<MSuchlaufErgebnis, String>(
                "Gesperrt");
        istGesperrtString.setCellValueFactory(new PropertyValueFactory<>("istGesperrtString"));
        tableNeu.getColumns().add(istGesperrtString);
    }

    /**
     * Col gefundene vollmacht name.
     */
    private void colGefundeneVollmachtName() {
        if (lAnzeigeSuchlauf == false) {
            return;
        }
        TableColumn<MSuchlaufErgebnis, String> gefundeneVollmachtName = new TableColumn<MSuchlaufErgebnis, String>(
                "Vertreter (alle)");
        gefundeneVollmachtName.setCellValueFactory(new PropertyValueFactory<>("gefundeneVollmachtName"));
        tableNeu.getColumns().add(gefundeneVollmachtName);
    }

    /**
     * Col vollmacht name ort.
     */
    private void colVollmachtNameOrt() {
        if (lAnzeigeSonstieVollmachten == false) {
            return;
        }
        TableColumn<MSuchlaufErgebnis, String> vollmachtNameOrt = new TableColumn<MSuchlaufErgebnis, String>(
                "Vertreter (sonstige)");
        vollmachtNameOrt.setCellValueFactory(new PropertyValueFactory<>("vollmachtNameOrt"));
        tableNeu.getColumns().add(vollmachtNameOrt);
    }

    /**
     * Reine Dummy-Funktion zur Unterdrückung der Warnung von lAnzeigeRegister;.
     */
    private void col() {
        if (lAnzeigeRegister == false) {
            return;
        }
    }

}
