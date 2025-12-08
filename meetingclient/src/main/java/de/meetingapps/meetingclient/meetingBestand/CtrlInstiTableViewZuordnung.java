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

import de.meetingapps.meetingclient.meetingClientOberflaechen.TableCellLongMitPunkt;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * The Class CtrlInstiTableViewZuordnung.
 */
public class CtrlInstiTableViewZuordnung {

    /** The table neu. */
    private TableView<MInstiBestandsZuordnung> tableNeu = new TableView<MInstiBestandsZuordnung>();

    /** The l view nr. */
    private int lViewNr = 0;

    /** The l insti bestandszuordnung. */
    private boolean lInstiBestandszuordnung = false;

    /** The l aktienregister. */
    private boolean lAktienregister = false;

    /** The l meldung. */
    private boolean lMeldung = false;

    /** The l user login. */
    private boolean lUserLogin = false;

    /**
     * Unterstützte pViewNr: 1=alle.
     *
     * @param pViewNr                 the view nr
     * @param pInstiBestandszuordnung the insti bestandszuordnung
     * @param pAktienregister         the aktienregister
     * @param pMeldung                the meldung
     * @param pUserLogin              the user login
     * @return the table view
     */
    public TableView<MInstiBestandsZuordnung> baueGrundAnsichtTableViewOhneInhalte(int pViewNr,
            boolean pInstiBestandszuordnung, boolean pAktienregister, boolean pMeldung, boolean pUserLogin) {
        lViewNr = pViewNr;
        lInstiBestandszuordnung = pInstiBestandszuordnung;
        lAktienregister = pAktienregister;
        lMeldung = pMeldung;
        lUserLogin = pUserLogin;

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

            colIdentInstiBestandszuordnung();
            colBeschreibung();
            colRegisterOderMeldung();
            colIdentAktienregister();
            colZugeordneteStimmen();
            colIdentMeldung();
            colIdentInsti();
            colIdentUserLogin();
            colVerarbeitetSammelAnmeldungGedruckt();
            colVerarbeitet2();
            colVerarbeitet3();
            colVerarbeitet4();
            colVerarbeitet5();

            colBenutzerKennung();
            colBenutzerName();

            colLaufendeNummerInArray();
        }

        return tableNeu;
    }

    /*****************Einzel-Felder aufbereiten für baueGrundAnsichtTableViewOhneInhalte()************
     * Wenn ein Feld aufgerufen wird, das nicht angezeigt werden darf (lAnzeigeSuchlauf etc.), 
     * erfolgt nichts :-)
     */
    private void colLaufendeNummerInArray() {
        TableColumn<MInstiBestandsZuordnung, Integer> laufendeNummerInArray = new TableColumn<MInstiBestandsZuordnung, Integer>(
                "LfdNr");
        laufendeNummerInArray.setCellValueFactory(new PropertyValueFactory<>("laufendeNummerInArray"));
        laufendeNummerInArray.setSortType(TableColumn.SortType.DESCENDING);
        tableNeu.getColumns().add(laufendeNummerInArray);

    }

    /**
     * Col ident insti bestandszuordnung.
     */
    private void colIdentInstiBestandszuordnung() {
        if (lInstiBestandszuordnung == false) {
            return;
        }
        TableColumn<MInstiBestandsZuordnung, Integer> identInstiBestandszuordnung = new TableColumn<MInstiBestandsZuordnung, Integer>(
                "Zuordn.Ident");
        identInstiBestandszuordnung
                .setCellValueFactory(new PropertyValueFactory<>("ident")); /*Früher: identInstiBestandszuordnung*/
        tableNeu.getColumns().add(identInstiBestandszuordnung);
    }

    /**
     * Col beschreibung.
     */
    private void colBeschreibung() {
        if (lInstiBestandszuordnung == false) {
            return;
        }
        TableColumn<MInstiBestandsZuordnung, String> beschreibung = new TableColumn<MInstiBestandsZuordnung, String>(
                "Beschreibung");
        beschreibung.setCellValueFactory(new PropertyValueFactory<>("beschreibung"));
        tableNeu.getColumns().add(beschreibung);
    }

    /**
     * Col register oder meldung.
     */
    private void colRegisterOderMeldung() {
        if (lInstiBestandszuordnung == false) {
            return;
        }
        TableColumn<MInstiBestandsZuordnung, Integer> zugeordnetRegisterOderMeldungen = new TableColumn<MInstiBestandsZuordnung, Integer>(
                "Reg/Meld");
        zugeordnetRegisterOderMeldungen
                .setCellValueFactory(new PropertyValueFactory<>("zugeordnetRegisterOderMeldungen"));
        tableNeu.getColumns().add(zugeordnetRegisterOderMeldungen);
    }

    /**
     * Col ident aktienregister.
     */
    private void colIdentAktienregister() {
        if (lInstiBestandszuordnung == false) {
            return;
        }
        TableColumn<MInstiBestandsZuordnung, Integer> identAktienregister = new TableColumn<MInstiBestandsZuordnung, Integer>(
                "IdentReg");
        identAktienregister.setCellValueFactory(new PropertyValueFactory<>("identAktienregister"));
        tableNeu.getColumns().add(identAktienregister);
    }

    /**
     * Col zugeordnete stimmen.
     */
    private void colZugeordneteStimmen() {
        TableColumn<MInstiBestandsZuordnung, String> zugeordneteStimmen = new TableColumn<MInstiBestandsZuordnung, String>(
                "Zug.Aktien");
        zugeordneteStimmen.setCellValueFactory(new PropertyValueFactory<>("zugeordneteStimmen"));
        zugeordneteStimmen.setCellFactory(column -> {
            return new TableCellLongMitPunkt<MInstiBestandsZuordnung, String>();
        });
        tableNeu.getColumns().add(zugeordneteStimmen);
    }

    /**
     * Col ident meldung.
     */
    private void colIdentMeldung() {
        if (lInstiBestandszuordnung == false) {
            return;
        }
        TableColumn<MInstiBestandsZuordnung, Integer> identMeldung = new TableColumn<MInstiBestandsZuordnung, Integer>(
                "Ident Meld.");
        identMeldung.setCellValueFactory(new PropertyValueFactory<>("identMeldung"));
        tableNeu.getColumns().add(identMeldung);
    }

    /**
     * Col ident insti.
     */
    private void colIdentInsti() {
        if (lInstiBestandszuordnung == false) {
            return;
        }
        TableColumn<MInstiBestandsZuordnung, Integer> identInsti = new TableColumn<MInstiBestandsZuordnung, Integer>(
                "Ident Insti.");
        identInsti.setCellValueFactory(new PropertyValueFactory<>("identInsti"));
        tableNeu.getColumns().add(identInsti);
    }

    /**
     * Col ident user login.
     */
    private void colIdentUserLogin() {
        if (lInstiBestandszuordnung == false) {
            return;
        }
        TableColumn<MInstiBestandsZuordnung, Integer> identUserLogin = new TableColumn<MInstiBestandsZuordnung, Integer>(
                "Ident UserLogin");
        identUserLogin.setCellValueFactory(new PropertyValueFactory<>("identUserLogin"));
        tableNeu.getColumns().add(identUserLogin);
    }

    /**
     * Col verarbeitet sammel anmeldung gedruckt.
     */
    private void colVerarbeitetSammelAnmeldungGedruckt() {
        if (lInstiBestandszuordnung == false) {
            return;
        }
        TableColumn<MInstiBestandsZuordnung, Integer> verarbeitetSammelAnmeldungGedruckt = new TableColumn<MInstiBestandsZuordnung, Integer>(
                "Sammelanm.Gedruckt");
        verarbeitetSammelAnmeldungGedruckt
                .setCellValueFactory(new PropertyValueFactory<>("verarbeitetSammelAnmeldungGedruckt"));
        tableNeu.getColumns().add(verarbeitetSammelAnmeldungGedruckt);
    }

    /**
     * Col verarbeitet 2.
     */
    private void colVerarbeitet2() {
        if (lInstiBestandszuordnung == false) {
            return;
        }
        TableColumn<MInstiBestandsZuordnung, Integer> verarbeitet2 = new TableColumn<MInstiBestandsZuordnung, Integer>(
                "Verarbeitet2");
        verarbeitet2.setCellValueFactory(new PropertyValueFactory<>("verarbeitet2"));
        tableNeu.getColumns().add(verarbeitet2);
    }

    /**
     * Col verarbeitet 3.
     */
    private void colVerarbeitet3() {
        if (lInstiBestandszuordnung == false) {
            return;
        }
        TableColumn<MInstiBestandsZuordnung, Integer> verarbeitet3 = new TableColumn<MInstiBestandsZuordnung, Integer>(
                "Verarbeitet3");
        verarbeitet3.setCellValueFactory(new PropertyValueFactory<>("verarbeitet3"));
        tableNeu.getColumns().add(verarbeitet3);
    }

    /**
     * Col verarbeitet 4.
     */
    private void colVerarbeitet4() {
        if (lInstiBestandszuordnung == false) {
            return;
        }
        TableColumn<MInstiBestandsZuordnung, Integer> verarbeitet4 = new TableColumn<MInstiBestandsZuordnung, Integer>(
                "Verarbeitet4");
        verarbeitet4.setCellValueFactory(new PropertyValueFactory<>("verarbeitet4"));
        tableNeu.getColumns().add(verarbeitet4);
    }

    /**
     * Col verarbeitet 5.
     */
    private void colVerarbeitet5() {
        if (lInstiBestandszuordnung == false) {
            return;
        }
        TableColumn<MInstiBestandsZuordnung, Integer> verarbeitet5 = new TableColumn<MInstiBestandsZuordnung, Integer>(
                "verarbeitet5");
        verarbeitet5.setCellValueFactory(new PropertyValueFactory<>("Verarbeitet5"));
        tableNeu.getColumns().add(verarbeitet5);
    }

    /**
     * Col aktionaersnummer.
     */
    private void colAktionaersnummer() {
        //		if (lAktienregister==false) {return;}
        TableColumn<MInstiBestandsZuordnung, String> aktionaersnummer = new TableColumn<MInstiBestandsZuordnung, String>(
                "Aktionärsnummer");
        aktionaersnummer.setCellValueFactory(new PropertyValueFactory<>("aktionaersnummer"));
        tableNeu.getColumns().add(aktionaersnummer);
    }

    /**
     * Col aktionaersname.
     */
    private void colAktionaersname() {
        //		if (lAktienregister==false) {return;}
        TableColumn<MInstiBestandsZuordnung, String> aktionaersname = new TableColumn<MInstiBestandsZuordnung, String>(
                "Aktionärs-Name");
        aktionaersname.setCellValueFactory(new PropertyValueFactory<>("aktionaersname"));
        tableNeu.getColumns().add(aktionaersname);
    }

    /**
     * Col aktionaersort.
     */
    private void colAktionaersort() {
        if (lAktienregister == false) {
            return;
        }
        TableColumn<MInstiBestandsZuordnung, String> aktionaersort = new TableColumn<MInstiBestandsZuordnung, String>(
                "Aktionärs-Ort");
        aktionaersort.setCellValueFactory(new PropertyValueFactory<>("aktionaersort"));
        tableNeu.getColumns().add(aktionaersort);
    }

    /**
     * Col besitzart.
     */
    private void colBesitzart() {
        if (lAktienregister == false) {
            return;
        }
        TableColumn<MInstiBestandsZuordnung, String> besitzart = new TableColumn<MInstiBestandsZuordnung, String>(
                "Besitz");
        besitzart.setCellValueFactory(new PropertyValueFactory<>("besitzart"));
        tableNeu.getColumns().add(besitzart);
    }

    /**
     * Col stueck aktien.
     */
    private void colStueckAktien() {
        if (lAktienregister == false) {
            return;
        }
        TableColumn<MInstiBestandsZuordnung, String> stueckAktien = new TableColumn<MInstiBestandsZuordnung, String>(
                "Aktien");
        stueckAktien.setCellValueFactory(new PropertyValueFactory<>("stueckAktien"));
        stueckAktien.setCellFactory(column -> {
            return new TableCellLongMitPunkt<MInstiBestandsZuordnung, String>();
        });
        tableNeu.getColumns().add(stueckAktien);
    }

    /**
     * Col melde ident.
     */
    private void colMeldeIdent() {
        if (lMeldung == false) {
            return;
        }
        TableColumn<MInstiBestandsZuordnung, Integer> meldeIdent = new TableColumn<MInstiBestandsZuordnung, Integer>(
                "MeldeIdent");
        meldeIdent.setCellValueFactory(new PropertyValueFactory<>("meldeIdent"));
        tableNeu.getColumns().add(meldeIdent);
    }

    /**
     * Col ek nr.
     */
    private void colEkNr() {
        if (lMeldung == false) {
            return;
        }
        TableColumn<MInstiBestandsZuordnung, String> ekNr = new TableColumn<MInstiBestandsZuordnung, String>("EK-Nr");
        ekNr.setCellValueFactory(new PropertyValueFactory<>("ekNr"));
        tableNeu.getColumns().add(ekNr);
    }

    /**
     * Col sk nr.
     */
    private void colSkNr() {
        if (lMeldung == false) {
            return;
        }
        TableColumn<MInstiBestandsZuordnung, String> skNr = new TableColumn<MInstiBestandsZuordnung, String>("SK-Nr");
        skNr.setCellValueFactory(new PropertyValueFactory<>("skNr"));
        tableNeu.getColumns().add(skNr);
    }

    /**
     * Col stueck aktien meldung.
     */
    private void colStueckAktienMeldung() {
        if (lMeldung == false) {
            return;
        }
        TableColumn<MInstiBestandsZuordnung, String> stueckAktienMeldung = new TableColumn<MInstiBestandsZuordnung, String>(
                "Aktien gemeldet");
        stueckAktienMeldung.setCellValueFactory(new PropertyValueFactory<>("stueckAktienMeldung"));
        stueckAktienMeldung.setCellFactory(column -> {
            return new TableCellLongMitPunkt<MInstiBestandsZuordnung, String>();
        });
        tableNeu.getColumns().add(stueckAktienMeldung);
    }

    /**
     * Col ist oder in sammelkarte.
     */
    private void colIstOderInSammelkarte() {
        if (lMeldung == false) {
            return;
        }
        TableColumn<MInstiBestandsZuordnung, String> istOderInSammelkarte = new TableColumn<MInstiBestandsZuordnung, String>(
                "Sammelkarte");
        istOderInSammelkarte.setCellValueFactory(new PropertyValueFactory<>("istOderInSammelkarte"));
        tableNeu.getColumns().add(istOderInSammelkarte);
    }

    /**
     * Col vertreter name ort.
     */
    private void colVertreterNameOrt() {
        if (lMeldung == false) {
            return;
        }
        TableColumn<MInstiBestandsZuordnung, String> vertreterNameOrt = new TableColumn<MInstiBestandsZuordnung, String>(
                "Vertreter (aktuell)");
        vertreterNameOrt.setCellValueFactory(new PropertyValueFactory<>("vertreterNameOrt"));
        tableNeu.getColumns().add(vertreterNameOrt);
    }

    /**
     * Col ist praesent.
     */
    private void colIstPraesent() {
        if (lMeldung == false) {
            return;
        }
        TableColumn<MInstiBestandsZuordnung, String> istPraesent = new TableColumn<MInstiBestandsZuordnung, String>(
                "Präsent");
        istPraesent.setCellValueFactory(new PropertyValueFactory<>("istPraesent"));
        tableNeu.getColumns().add(istPraesent);
    }

    /**
     * Col ist angemeldet.
     */
    private void colIstAngemeldet() {
        if (lMeldung == false) {
            return;
        }
        TableColumn<MInstiBestandsZuordnung, String> istAngemeldet = new TableColumn<MInstiBestandsZuordnung, String>(
                "Angemeldet");
        istAngemeldet.setCellValueFactory(new PropertyValueFactory<>("istAngemeldet"));
        tableNeu.getColumns().add(istAngemeldet);
    }

    /**
     * Col ist gesperrt string.
     */
    private void colIstGesperrtString() {
        if (lMeldung == false) {
            return;
        }
        TableColumn<MInstiBestandsZuordnung, String> istGesperrtString = new TableColumn<MInstiBestandsZuordnung, String>(
                "Gesperrt");
        istGesperrtString.setCellValueFactory(new PropertyValueFactory<>("istGesperrtString"));
        tableNeu.getColumns().add(istGesperrtString);
    }

    /**
     * Col benutzer kennung.
     */
    private void colBenutzerKennung() {
        if (lUserLogin == false) {
            return;
        }
        TableColumn<MInstiBestandsZuordnung, String> benutzerKennung = new TableColumn<MInstiBestandsZuordnung, String>(
                "Ben.Kennung");
        benutzerKennung.setCellValueFactory(new PropertyValueFactory<>("benutzerKennung"));
        tableNeu.getColumns().add(benutzerKennung);
    }

    /**
     * Col benutzer name.
     */
    private void colBenutzerName() {
        if (lUserLogin == false) {
            return;
        }
        TableColumn<MInstiBestandsZuordnung, String> benutzerName = new TableColumn<MInstiBestandsZuordnung, String>(
                "Ben.Name");
        benutzerName.setCellValueFactory(new PropertyValueFactory<>("benutzerName"));
        tableNeu.getColumns().add(benutzerName);
    }

}
