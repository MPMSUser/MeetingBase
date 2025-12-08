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
package de.meetingapps.meetingclient.meetingHVMaster;

import java.net.URL;
import java.util.ResourceBundle;

import de.meetingapps.meetingclient.meetingClientDialoge.CaZeigeHinweis;
import de.meetingapps.meetingclient.meetingClientDialoge.CtrlRoot;
import de.meetingapps.meetingclient.meetingClientOberflaechen.CbAllgemein;
import de.meetingapps.meetingclient.meetingClientOberflaechen.CbElement;
import de.meetingapps.meetingportal.meetComAllg.CaString;
import de.meetingapps.meetingportal.meetComBl.BlPraesenzlistenDruck;
import de.meetingapps.meetingportal.meetComBl.BlPraesenzlistenNummer;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclKonfigAuswertung;
import de.meetingapps.meetingportal.meetComKonst.KonstKonfigAuswertungArt;
import de.meetingapps.meetingportal.meetComKonst.KonstKonfigAuswertungAusgabeWeg;
import de.meetingapps.meetingportal.meetComKonst.KonstKonfigAuswertungFunktion;
import de.meetingapps.meetingportal.meetComKonst.KonstKonfigAuswertungSortierung;
import de.meetingapps.meetingportal.meetingCoreReport.RpDrucken;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

/**
 * The Class CtrlPraesenzlisteDrucken.
 */
public class CtrlPraesenzlisteDrucken extends CtrlRoot {

    /** The resources. */
    @FXML
    private ResourceBundle resources;

    /** The location. */
    @FXML
    private URL location;

    /** The btn praesenz drucken. */
    @FXML
    private Button btnPraesenzDrucken;

    /** The btn abbrechen. */
    @FXML
    private Button btnAbbrechen;

    /** The lbl praesenz nr drucken. */
    @FXML
    private Label lblPraesenzNrDrucken;

    /** The lbl meldung. */
    @FXML
    private Label lblMeldung;

    /** The lbl aktuelle praesenz. */
    @FXML
    private Label lblAktuellePraesenz;

    /** The tf format nummer. */
    @FXML
    private TextField tfFormatNummer;

    /** The tf format nummer virtuell. */
    @FXML
    private TextField tfFormatNummerVirtuell;

    /** The tf format nummer zusammenstellung. */
    @FXML
    private TextField tfFormatNummerZusammenstellung;

    /** The rb sort name. */
    @FXML
    private RadioButton rbSortName;

    /** The tgl sortierung. */
    @FXML
    private ToggleGroup tglSortierung;

    /** The rb sort EK. */
    @FXML
    private RadioButton rbSortEK;

    /** The rb sort SK. */
    @FXML
    private RadioButton rbSortSK;

    /** The rb sort aktien. */
    @FXML
    private RadioButton rbSortAktien;

    /** The rb G alle. */
    @FXML
    private RadioButton rbGAlle;

    /** The tg gattung. */
    @FXML
    private ToggleGroup tgGattung;

    /** The rb G 1. */
    @FXML
    private RadioButton rbG1;

    /** The rb G 2. */
    @FXML
    private RadioButton rbG2;

    /** The rb G 3. */
    @FXML
    private RadioButton rbG3;

    /** The rb G 4. */
    @FXML
    private RadioButton rbG4;

    /** The rb G 5. */
    @FXML
    private RadioButton rbG5;

    /** The lbl letzte feststellung. */
    @FXML
    private Label lblLetzteFeststellung;

    /** The cb liste drucken. */
    @FXML
    private CheckBox cbListeDrucken;

    /** The cb liste drucken virtuell. */
    @FXML
    private CheckBox cbListeDruckenVirtuell;

    /** The cb zusammenfassung drucken. */
    @FXML
    private CheckBox cbZusammenfassungDrucken;

    /** The rb druck wie eingaben. */
    @FXML
    private RadioButton rbDruckWieEingaben;

    /** The tg drucken. */
    @FXML
    private ToggleGroup tgDrucken;

    /** The rb PD ffuer gewaehlte liste. */
    @FXML
    private RadioButton rbPDFfuerGewaehlteListe;

    /** The rb drucklauf fuer liste. */
    @FXML
    private RadioButton rbDrucklaufFuerListe;

    /** The rb PDF fuer alle listen. */
    @FXML
    private RadioButton rbPDFFuerAlleListen;

    /** The cb auswertungslauf. */
    @FXML
    private ComboBox<CbElement> cbAuswertungslauf;

    /*Ab hier Individuell*/

    /** The cb praesenz nr drucken. */
    @FXML
    private ComboBox<CbElement> cbPraesenzNrDrucken;

    /** The praesenz liste aktuell. */
    int praesenzListeAktuell = 0;

    /** The konfig auswertung alle. */
    EclKonfigAuswertung[] konfigAuswertungAlle = null;

    /** The konst liste fuer praesenz hv. */
    private final boolean KONST_LISTE_FUER_PRAESENZ_HV = false;

    /** The konst liste fuer virtuelle hv. */
    private final boolean KONST_LISTE_FUER_VIRTUELLE_HV = true;

    /**
     * Initialize.
     */
    @FXML
    void initialize() {
        
        refreshInhalt();
        lblMeldung.setText("");

    }

    /**Hinweis zu Combobox: -2=Gesamtpräsenzliste, -1=Erstpräsenz*/
    private void refreshInhalt() {
        /*Aktuelle Präsenzliste holen*/
        DbBundle lDbBundle = new DbBundle();
        lDbBundle.openAll();
        BlPraesenzlistenNummer lBlPraesenzlistenNummer = new BlPraesenzlistenNummer(lDbBundle);
        lBlPraesenzlistenNummer.leseAktuelleNummernFuerBuchung();

        lDbBundle.dbKonfigAuswertung.readAll_Laeufe(KonstKonfigAuswertungArt.erstpraesenz);
        if (lDbBundle.dbKonfigAuswertung.anzErgebnis() > 0) {
            CbAllgemein cbAllgemein1 = new CbAllgemein(cbAuswertungslauf);
            for (int i = 0; i < lDbBundle.dbKonfigAuswertung.anzErgebnis(); i++) {
                EclKonfigAuswertung lKonfiguAuswertung = lDbBundle.dbKonfigAuswertung.ergebnisPosition(i);
                String hString = Integer.toString(lKonfiguAuswertung.nr) + " "
                        + KonstKonfigAuswertungArt.getText(lKonfiguAuswertung.fuerFunktion);
                CbElement hElement = new CbElement();
                hElement.anzeige = hString;
                hElement.ident1 = lKonfiguAuswertung.nr;
                hElement.ident2 = lKonfiguAuswertung.fuerFunktion;

                cbAllgemein1.addElement(hElement);
            }

            lDbBundle.dbKonfigAuswertung.readAll();
            konfigAuswertungAlle = lDbBundle.dbKonfigAuswertung.ergebnisArray;
        }
        lDbBundle.closeAll();
        /*Hier: aktuelle Listennummern stehen in lDbBundle.clGlobalVar.zuVerzeichnisNr1*/

        /*Aktuelle, letzte Präsenzliste anzeigen, sowie PräsenzlistenDruckauswahlComboBox belegen*/
        String hNaechstePraesenz = "";
        String hLetzteFestgestelltePraesenz = "";
        CbAllgemein cbAllgemein = new CbAllgemein(cbPraesenzNrDrucken);
        praesenzListeAktuell = lDbBundle.clGlobalVar.zuVerzeichnisNr1;
        if (praesenzListeAktuell == -1) {
            hNaechstePraesenz = "Erstpräsenz";
            hLetzteFestgestelltePraesenz = "noch keine Präsenz festgestellt!";
        } else {
            hNaechstePraesenz = Integer.toString(praesenzListeAktuell) + ". Nachtrag";
            if (praesenzListeAktuell == 1) {
                hLetzteFestgestelltePraesenz = "Erstpräsenz";
            } else {
                hLetzteFestgestelltePraesenz = Integer.toString(praesenzListeAktuell - 1) + ". Nachtrag";
            }

            CbElement hElement = null;
            hElement = new CbElement();
            hElement.anzeige = "Gesamtpräsentliste - PräsenzIdent: " + 999;
            hElement.ident1 = -2;
            cbAllgemein.addElement(hElement);

            hElement = new CbElement();
            hElement.anzeige = "Erstpräsenz - PräsenzIdent: " + 0;
            hElement.ident1 = -1;
            if (praesenzListeAktuell == 1) {
                cbAllgemein.addElementAusgewaehlt(hElement);
            } else {
                cbAllgemein.addElement(hElement);
                for (int i = 1; i < praesenzListeAktuell - 1; i++) {
                    hElement = new CbElement();
                    hElement.anzeige = i + ". Nachtrag - PräsenzIdent: " + hElement.ident1;
                    hElement.ident1 = i;
                    cbAllgemein.addElement(hElement);
                }
                if (praesenzListeAktuell > 0) {
                    hElement = new CbElement();
                    hElement.ident1 = praesenzListeAktuell - 1;
                    hElement.anzeige = hElement.ident1 + ". Nachtrag - PräsenzIdent: " + hElement.ident1;
                    cbAllgemein.addElementAusgewaehlt(hElement);
                }
            }
        }
        lblAktuellePraesenz.setText(hNaechstePraesenz);
        lblLetzteFeststellung.setText(hLetzteFestgestelltePraesenz);

        /*Gattungen zum Auswählen anbieten*/
        for (int i = 1; i <= 5; i++) {
            if (lDbBundle.param.paramBasis.getGattungAktiv(i) == true) {
                String hText = lDbBundle.param.paramBasis.getGattungBezeichnung(i);
                switch (i) {
                case 1:
                    rbG1.setVisible(true);
                    rbG1.setText(hText);
                    break;
                case 2:
                    rbG2.setVisible(true);
                    rbG2.setText(hText);
                    break;
                case 3:
                    rbG3.setVisible(true);
                    rbG3.setText(hText);
                    break;
                case 4:
                    rbG4.setVisible(true);
                    rbG4.setText(hText);
                    break;
                case 5:
                    rbG5.setVisible(true);
                    rbG5.setText(hText);
                    break;
                }
            } else {
                switch (i) {
                case 1:
                    rbG1.setVisible(false);
                    break;
                case 2:
                    rbG2.setVisible(false);
                    break;
                case 3:
                    rbG3.setVisible(false);
                    break;
                case 4:
                    rbG4.setVisible(false);
                    break;
                case 5:
                    rbG5.setVisible(false);
                    break;
                }
            }
        }

        if (praesenzListeAktuell == -1) {
            btnPraesenzDrucken.setDisable(true);
        }
        
        switch (lDbBundle.param.paramModuleKonfigurierbar.hvForm) {
        case 0:
            /*undefiniert*/
            cbListeDruckenVirtuell.setSelected(true);
            cbListeDrucken.setSelected(true);
            break;
        case 1:
            /*rein virtuell*/
            cbListeDruckenVirtuell.setSelected(true);
            cbListeDrucken.setSelected(true);
            break;
        case 2:
            /*rein präsenz*/
            cbListeDruckenVirtuell.setSelected(false);
            cbListeDrucken.setSelected(true);
            break;
         }
    }

    /**
     * Zeige fehler.
     *
     * @param fehlerText the fehler text
     */
    private void zeigeFehler(String fehlerText) {
        CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
        caZeigeHinweis.zeige(eigeneStage, fehlerText);
    }

    /**
     * Clicked btn praesenz drucken.
     *
     * @param event the event
     */
    @FXML
    void clickedBtnPraesenzDrucken(ActionEvent event) {
        String lfdNummer = "";
        String lfdNummerVirtuell = "";
        String lfdNummerZusammenstellung = "";
        CbElement gewaehlteKonfig = null;

        /***** Eingabe verarbeiten und prüfen *****/

        /*Listennummer - ist immer gefüllt, ansonsten Button gar nicht drückbar*/
        int listenNummer = 0;
        listenNummer = cbPraesenzNrDrucken.getValue().ident1;
        int listenart = 0; /*Für Offset in Parameter*/
        /*AAAAA Praesenzliste das folgende ist irgendwie ziemlich schwachsinnig .... war aber schon so*/
        if (listenNummer == -2) {
            listenart = 0;
        }
        if (listenNummer == -2) {
            listenart = 1;
        }
        if (listenNummer == -2) {
            listenart = 2;
        }

        /*Sortierung*/
        int sortierung = 0;
        if (rbSortName.isSelected()) {
            sortierung = 1;
        }
        if (rbSortEK.isSelected()) {
            sortierung = 2;
        }
        if (rbSortSK.isSelected()) {
            sortierung = 3;
        }
        if (rbSortAktien.isSelected()) {
            sortierung = 4;
        }

        /*Gattung*/
        int gattung = 0;
        if (rbG1.isSelected()) {
            gattung = 1;
        }
        if (rbG2.isSelected()) {
            gattung = 2;
        }
        if (rbG3.isSelected()) {
            gattung = 3;
        }
        if (rbG4.isSelected()) {
            gattung = 4;
        }
        if (rbG5.isSelected()) {
            gattung = 5;
        }

        DbBundle lDbBundle = new DbBundle();
        lDbBundle.openAll();

        if (rbDruckWieEingaben.isSelected()) {
            /*Restliche Eingaben prüfen*/

            /*Eine der beiden Ausgaben muß mindestens gewählt sein*/
            if (cbListeDrucken.isSelected() == false && cbListeDruckenVirtuell.isSelected() == false
                    && cbZusammenfassungDrucken.isSelected() == false) {
                zeigeFehler("Mindestens Liste oder Zusammenfassung muß ausgewählt sein");
                lDbBundle.closeAll();
                return;
            }

            if (cbListeDrucken.isSelected() == true) {/*Listen-Format überprüfen*/
                lfdNummer = tfFormatNummer.getText().trim();
                if (!lfdNummer.isEmpty()) {/*Nummer eingegeben - richtig?*/
                    if (!CaString.isNummern(lfdNummer) || lfdNummer.length() > 2) {
                        zeigeFehler("Format für Liste ungültig!");
                        lDbBundle.closeAll();
                        return;
                    }
                    int hNummer = Integer.parseInt(lfdNummer);
                    lfdNummer = CaString.fuelleLinksNull(Integer.toString(hNummer), 2);
                } else {
                    lfdNummer = lDbBundle.param.paramPraesenzliste.einzeldruckFormatListe[listenart][sortierung
                            - 1][gattung];
                    if (lfdNummer.compareTo("00") == 0) {
                        zeigeFehler("Parameter Format Liste für diese Auswahl nicht gesetzt!");
                        lDbBundle.closeAll();
                        return;
                    }
                }
            }
            if (cbListeDruckenVirtuell.isSelected() == true) {/*Listen-Format überprüfen*/
                lfdNummerVirtuell = tfFormatNummerVirtuell.getText().trim();
                if (!lfdNummerVirtuell.isEmpty()) {/*Nummer eingegeben - richtig?*/
                    if (!CaString.isNummern(lfdNummerVirtuell) || lfdNummerVirtuell.length() > 2) {
                        zeigeFehler("Format für Liste Virtuell ungültig!");
                        lDbBundle.closeAll();
                        return;
                    }
                    int hNummer = Integer.parseInt(lfdNummerVirtuell);
                    lfdNummerVirtuell = CaString.fuelleLinksNull(Integer.toString(hNummer), 2);
                } else {
                    /*TODO VidKonf  noch neuen parameter für Format Liste virtuell einführen. Auch die Vorab-Speicherungen noch entsprechend anpassen!*/
                    lfdNummerVirtuell = lDbBundle.param.paramPraesenzliste.einzeldruckFormatListe[listenart][sortierung
                            - 1][gattung];
                    if (lfdNummerVirtuell.compareTo("00") == 0) {
                        zeigeFehler("Parameter Format Liste Virtuell für diese Auswahl nicht gesetzt!");
                        lDbBundle.closeAll();
                        return;
                    }
                }
            }
            if (cbZusammenfassungDrucken.isSelected() == true) {/*Zusammenfassung-Format überprüfen*/
                lfdNummerZusammenstellung = tfFormatNummerZusammenstellung.getText().trim();
                if (!lfdNummerZusammenstellung.isEmpty()) {/*Nummer eingegeben - richtig?*/
                    if (!CaString.isNummern(lfdNummerZusammenstellung) || lfdNummerZusammenstellung.length() > 2) {
                        zeigeFehler("Format für Zusammenstellung ungültig!");
                        lDbBundle.closeAll();
                        return;
                    }
                    int hNummer = Integer.parseInt(lfdNummerZusammenstellung);
                    lfdNummerZusammenstellung = CaString.fuelleLinksNull(Integer.toString(hNummer), 2);
                } else {
                    lfdNummerZusammenstellung = lDbBundle.param.paramPraesenzliste.einzeldruckFormatZusammenstellung[listenart][sortierung
                            - 1][gattung];
                    if (lfdNummerZusammenstellung.compareTo("00") == 0) {
                        zeigeFehler("Parameter Format Zusammenstellung für diese Auswahl nicht gesetzt!");
                        lDbBundle.closeAll();
                        return;
                    }
                }
            }

        }

        if (rbDrucklaufFuerListe.isSelected()) {
            gewaehlteKonfig = cbAuswertungslauf.getValue();
            if (gewaehlteKonfig == null) {
                zeigeFehler("Auswertungslauf nicht ausgewählt!");
                lDbBundle.closeAll();
                return;
            }
        }

        /******************** Drucken - gemäß obigen Eingaben ************************/
        if (rbDruckWieEingaben.isSelected()) {

            boolean rc = true;
            BlPraesenzlistenDruck blPraesenzlistenDruck = new BlPraesenzlistenDruck();
            int druckerverwenden = 0;
            if (!lfdNummer.isEmpty()) {
                RpDrucken rpDrucken = new RpDrucken();
                rpDrucken.initClientDrucke();
                rpDrucken.druckerWiederverwendet = 1;
                rpDrucken.druckerWiederverwendetNummer = 999;
                druckerverwenden = 999;
                rc = blPraesenzlistenDruck.druckePraesenzliste(lDbBundle, rpDrucken, 1, listenNummer, gattung,
                        lfdNummer, sortierung, KONST_LISTE_FUER_PRAESENZ_HV);
            }
            if (!lfdNummerVirtuell.isEmpty()) {
                RpDrucken rpDrucken = new RpDrucken();
                rpDrucken.initClientDrucke();
                rpDrucken.druckerWiederverwendet = 1;
                rpDrucken.druckerWiederverwendetNummer = 999;
                druckerverwenden = 999;
                rc = blPraesenzlistenDruck.druckePraesenzliste(lDbBundle, rpDrucken, 1, listenNummer, gattung,
                        lfdNummerVirtuell, sortierung, KONST_LISTE_FUER_VIRTUELLE_HV);
            }

            if (!lfdNummerZusammenstellung.isEmpty() && rc == true) {
                RpDrucken rpDrucken = new RpDrucken();
                rpDrucken.initClientDrucke();
                if (druckerverwenden == 999) {
                    rpDrucken.druckerWiederverwendet = 2;
                    rpDrucken.druckerWiederverwendetNummer = 999;
                    rpDrucken.druckerAbfragen = false;
                }
                blPraesenzlistenDruck.druckePraesenzlisteZusammenstellung(lDbBundle, rpDrucken, 1, listenNummer,
                        gattung, lfdNummerZusammenstellung, sortierung);
            }
        }

        /*********************** Drucklauf für gewählte Präsenz *********************/
        /*TODO VidKonf  Drucklauf für virtuelle präsenzliste noch nicht implementiert*/
        if (rbDrucklaufFuerListe.isSelected()) {
            BlPraesenzlistenDruck blPraesenzlistenDruck = new BlPraesenzlistenDruck();
            for (int i = 0; i < konfigAuswertungAlle.length; i++) {
                EclKonfigAuswertung aktuelleAuswertung = konfigAuswertungAlle[i];
                if (aktuelleAuswertung.nr == gewaehlteKonfig.ident1
                        && aktuelleAuswertung.fuerFunktion == gewaehlteKonfig.ident2) {
                    String hMeldeText = "Position " + Integer.toString(aktuelleAuswertung.positionInLauf) + " "
                            + KonstKonfigAuswertungFunktion.getText(aktuelleAuswertung.ausgeloesteFunktion);
                    if (aktuelleAuswertung.ausgeloesteFunktion == KonstKonfigAuswertungFunktion.praesenzliste) {
                        hMeldeText += " " + KonstKonfigAuswertungSortierung.getText(aktuelleAuswertung.sortierung);
                    }
                    lblMeldung.setText(hMeldeText);

                    /*Initialisieren*/
                    RpDrucken rpDrucken = new RpDrucken();
                    rpDrucken.initClientDrucke();
                    if (aktuelleAuswertung.ausgabeWeg == KonstKonfigAuswertungAusgabeWeg.druckerAbfragen) {
                        rpDrucken.druckerWiederverwendet = 1;
                        rpDrucken.druckerWiederverwendetNummer = 999;
                    }
                    if (aktuelleAuswertung.ausgabeWeg == KonstKonfigAuswertungAusgabeWeg.wieVorherigerDrucker) {
                        rpDrucken.druckerWiederverwendet = 2;
                        rpDrucken.druckerWiederverwendetNummer = 999;
                        rpDrucken.druckerAbfragen = false;
                    }
                    if (aktuelleAuswertung.ausgabeWeg == KonstKonfigAuswertungAusgabeWeg.pfadMeetingOutput) {
                        rpDrucken.exportFormat = 7;
                        rpDrucken.exportDatei = aktuelleAuswertung.dateinamePdf;
                        rpDrucken.druckerAbfragen = false;
                    }

                    /*Funktion auslösen*/
                    if (aktuelleAuswertung.ausgeloesteFunktion == KonstKonfigAuswertungFunktion.praesenzliste) {
                        /*listennummer - durch ComboBox ausgewählt*/
                        gattung = aktuelleAuswertung.gattung;
                        lfdNummer = Integer.toString(aktuelleAuswertung.ausgeloesteFormNr);
                        lfdNummer = CaString.fuelleLinksNull(lfdNummer, 2);
                        sortierung = aktuelleAuswertung.sortierung;
                        boolean rc = blPraesenzlistenDruck.druckePraesenzliste(lDbBundle, rpDrucken, 1, listenNummer,
                                gattung, lfdNummer, sortierung, false);
                        if (rc == false) {
                            lblMeldung.setText("Druck abgebrochen");
                            lDbBundle.closeAll();
                            return;
                        }
                    }
                    if (aktuelleAuswertung.ausgeloesteFunktion == KonstKonfigAuswertungFunktion.praesenzZusammenstellung) {
                        /*listennummer - durch ComboBox ausgewählt*/
                        gattung = aktuelleAuswertung.gattung;
                        lfdNummerZusammenstellung = Integer.toString(aktuelleAuswertung.ausgeloesteFormNr);
                        lfdNummerZusammenstellung = CaString.fuelleLinksNull(lfdNummerZusammenstellung, 2);
                        String pText1 = aktuelleAuswertung.textFuerFormular1;
                        String pText2 = aktuelleAuswertung.textFuerFormular2;

                        boolean rc = blPraesenzlistenDruck.druckePraesenzlisteZusammenstellung(lDbBundle, rpDrucken, 1,
                                listenNummer, gattung, lfdNummerZusammenstellung, sortierung, pText1, pText2);
                        if (rc == false) {
                            lblMeldung.setText("Druck abgebrochen");
                            lDbBundle.closeAll();
                            return;
                        }
                    }
                }

            }
        }

        /*********************** PDF für gewählte Präsenz *********************/
        if (rbPDFfuerGewaehlteListe.isSelected()) {
            erzeugePDF(lDbBundle, listenNummer);
        }

        /*********************** PDF für alle Präsenzen *********************/
        if (rbPDFFuerAlleListen.isSelected()) {
            erzeugePDF(lDbBundle, -2);
            erzeugePDF(lDbBundle, -1);
            if (praesenzListeAktuell > 0) {
                for (int i = 1; i <= praesenzListeAktuell - 1; i++) {
                    erzeugePDF(lDbBundle, i);
                }
            }

        }

        lblMeldung.setText("Druck ausgeführt");
        lDbBundle.closeAll();
        return;

    }

    /** The meldung text. */
    private String meldungText = "";

    /** The nachtrag nr. */
    private int nachtragNr = 0;

    /** The gattung. */
    private int gattung = 0;

    /** The sortierung. */
    private int sortierung = 0;

    /** The liste zusammenstellung. */
    private int listeZusammenstellung = 0;

    /**
     * Erzeuge meldung text.
     *
     * @param pDbBundle the db bundle
     */
    private void erzeugeMeldungText(DbBundle pDbBundle) {
        switch (nachtragNr) {
        case -2:
            meldungText = "Gesamtteiln.verz. erzeugen";
            break;
        case -1:
            meldungText = "Erstpräsenz erzeugen";
            break;
        default:
            meldungText = "Nachtrag " + Integer.toString(nachtragNr) + " erzeugen";
            break;
        }
        if (gattung == 0) {
            meldungText += " " + "Alle Gattungen ";
        } else {
            meldungText += " " + pDbBundle.param.paramBasis.getGattungBezeichnung(gattung) + " ";
        }
        switch (sortierung) {
        case 1:
            meldungText += "Alpha";
            break;
        case 2:
            meldungText += "Ek";
            break;
        case 3:
            meldungText += "Sk";
            break;
        case 4:
            meldungText += "Aktien";
            break;
        }
        if (listeZusammenstellung == 1) {
            meldungText += " Liste";
        } else {
            meldungText += " Zusammenstellung";
        }
    }

    /**
     * Erzeuge PDF.
     *
     * @param pDbBundle    the db bundle
     * @param listenNummer the listen nummer
     */
    private void erzeugePDF(DbBundle pDbBundle, int listenNummer) {
        /*TODO VidKonf  erzeugePDF - Virtuelle Liste noch nicht implementiert*/
        int listenart = 0;
        if (listenNummer == -1) {
            listenart = 1;
        }
        if (listenNummer > 0) {
            listenart = 2;
        }
        for (int i1 = 1; i1 <= 4; i1++) {/*Sortierungen*/
            for (int i2 = 0; i2 <= 5; i2++) {/*Gattungen*/
                if (pDbBundle.param.paramBasis.getGattungAktiv(i2) || i2 == 0) {

                    String dateinamePraefix = "";
                    switch (listenart) {
                    case 0:
                        dateinamePraefix = "TeilnehmerverzeichnisGesamt";
                        break;
                    case 1:
                        dateinamePraefix = "Erstpraesenz";
                        break;
                    case 2:
                        dateinamePraefix = "Nachtrag" + Integer.toString(listenNummer);
                        break;
                    }
                    switch (i1) {/*Sortierung*/
                    case 1:
                        dateinamePraefix += "Alpha";
                        break;
                    case 2:
                        dateinamePraefix += "Ek";
                        break;
                    case 3:
                        dateinamePraefix += "Sk";
                        break;
                    case 4:
                        dateinamePraefix += "Aktien";
                        break;
                    }
                    if (i2 != 0) {
                        dateinamePraefix += pDbBundle.param.paramBasis.getGattungBezeichnungKurz(i2);
                    } else {
                        dateinamePraefix += "Alle";
                    }

                    if (pDbBundle.param.paramPraesenzliste.einzeldruckInPDFaufnehmen[listenart][i1 - 1][i2] == 1) {

                        BlPraesenzlistenDruck blPraesenzlistenDruck = new BlPraesenzlistenDruck();

                        String listenNr = pDbBundle.param.paramPraesenzliste.einzeldruckFormatListe[listenart][i1
                                - 1][i2];
                        if (listenNr.compareTo("00") != 0) {
                            nachtragNr = listenNummer;
                            gattung = i2;
                            sortierung = i1;
                            listeZusammenstellung = 1;
                            erzeugeMeldungText(pDbBundle);
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    lblMeldung.setText(meldungText);
                                }
                            });

                            /*Liste in PDF ausgeben*/
                            RpDrucken rpDrucken = new RpDrucken();
                            rpDrucken.exportFormat = 7;
                            rpDrucken.exportDatei = dateinamePraefix + "Liste";

                            blPraesenzlistenDruck.druckePraesenzliste(pDbBundle, rpDrucken, 1, listenNummer, i2,
                                    listenNr, i1, false);
                        }

                        String zusammenfassungNr = pDbBundle.param.paramPraesenzliste.einzeldruckFormatZusammenstellung[listenart][i1
                                - 1][i2];
                        if (zusammenfassungNr.compareTo("00") != 0) {

                            nachtragNr = listenNummer;
                            gattung = i2;
                            sortierung = i1;
                            listeZusammenstellung = 2;
                            erzeugeMeldungText(pDbBundle);
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    lblMeldung.setText(meldungText);
                                }
                            });

                            /*Zusammenstellung in PDF ausgeben*/
                            RpDrucken rpDrucken = new RpDrucken();
                            rpDrucken.exportFormat = 7;
                            rpDrucken.exportDatei = dateinamePraefix + "Zusammenstellung";

                            blPraesenzlistenDruck.druckePraesenzlisteZusammenstellung(pDbBundle, rpDrucken, 1,
                                    listenNummer, i2, zusammenfassungNr, i1);
                        }
                    }
                }

            }
        }

    }

    /**
     * Clicked btn abbrechen.
     *
     * @param event the event
     */
    @FXML
    void clickedBtnAbbrechen(ActionEvent event) {
        eigeneStage.hide();
    }

    /** The eigene stage. */
    private Stage eigeneStage;

    /**
     * Inits the.
     *
     * @param pEigeneStage the eigene stage
     */
    public void init(Stage pEigeneStage) {
        eigeneStage = pEigeneStage;

    }

}
