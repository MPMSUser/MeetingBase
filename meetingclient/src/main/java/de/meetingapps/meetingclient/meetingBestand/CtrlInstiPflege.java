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

import java.net.URL;
import java.util.ResourceBundle;

import de.meetingapps.meetingclient.meetingClientDialoge.CaController;
import de.meetingapps.meetingclient.meetingClientDialoge.CaIcon;
import de.meetingapps.meetingclient.meetingClientDialoge.CaZeigeHinweis;
import de.meetingapps.meetingclient.meetingClientDialoge.CtrlRoot;
import de.meetingapps.meetingportal.meetComHVParam.ParamS;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

/**
 * The Class CtrlInstiPflege.
 */
public class CtrlInstiPflege extends CtrlRoot {

    /** The resources. */
    @FXML
    private ResourceBundle resources;

    /** The location. */
    @FXML
    private URL location;

    /** The btn bestaende insti verwalten. */
    @FXML
    private Button btnBestaendeInstiVerwalten;

    /** The btn insti aendern. */
    @FXML
    private Button btnInstiAendern;

    /** The scpn uebersicht. */
    @FXML
    private ScrollPane scpnUebersicht;

    /** The btn neuen insti anlegen. */
    @FXML
    private Button btnNeuenInstiAnlegen;

    //	@FXML
    //	private ResourceBundle resources;
    //
    //	@FXML
    //	private URL location;
    //
    //	@FXML
    //	private Button btnSammelkartendatenAendern;
    //
    //	@FXML
    //	private ScrollPane scpnUebersicht;
    //
    //	@FXML
    //	private Button btnVertreterEintrittskarten;
    //
    //	@FXML
    //	private CheckBox cbNurAktiveAnzeigen;
    //
    //	@FXML
    //	private Button btnDetailsAnzeigen;
    //
    //	@FXML
    //	private Button btnBearbeitungKopfweisungen;
    //
    //	@FXML
    //	private Button btnKonsistenzpruefungSammelkarten;
    //
    //	@FXML
    //	private Button btnStapelUmbuchungen;
    //
    //	@FXML
    //	private Button btnNeueSammelkarteAnlegen;
    //
    //
    //
    //
    //
    //
    //
    //	@FXML
    //	private Button btnZuordnenEKAlle;
    //
    //
    //
    //	@FXML
    //	void clickedBtnStapelUmbuchungen(ActionEvent event) {
    //		int selectedMeldeIdent=liefereSelektierteSammelkarte();
    //		if (selectedMeldeIdent==-1){return;}
    //
    //		rufeDetailsAuf(5,"Aktionäre Umbuchen", selectedMeldeIdent, true);
    //
    //	}
    //
    //
    //	private EclMeldung aktuelleSammelMeldung=null;
    //	private EclWeisungMeldung weisungenSammelkopf=null;
    //	private EclWeisungMeldung aktionaersSummen=null;
    //	private boolean sammelFehler=false;
    //	@FXML
    //	void clickedBtnKonsistenzpruefungSammelkarten(ActionEvent event) {
    //		/**TODO Konsistenprüfung sammelkarten: Hier derzeit nur eine temporäre Lösung. gehört eigentlich in BlSammelkarten,
    //		 * aber die Tagesordnungsverwaltung ist hierfür noch nicht richtig gelöst.
    //		 */
    //		DbBundle lDbBundle=new DbBundle();
    //
    //		BlSammelkarten blSammelkarten=new BlSammelkarten(false, lDbBundle);
    //		blSammelkarten.holeSammelkartenDaten(true, 0);
    //
    //		EclMeldung[] alleSammelkarten=blSammelkarten.rcSammelMeldung;
    //
    //		int anzahlSammelkarten=alleSammelkarten.length;
    //		for (int i=0;i<anzahlSammelkarten;i++) {
    //			aktuelleSammelMeldung=alleSammelkarten[i];
    //			sammelFehler=false;
    //			if (aktuelleSammelMeldung.meldungAktiv==1) {
    //				blSammelkarten.leseKopfWeisungUndAktionaereZuSammelkarte(aktuelleSammelMeldung);
    //
    //				int aktuelleGattung=aktuelleSammelMeldung.liefereGattung();
    //
    //				weisungenSammelkopf=blSammelkarten.rcWeisungenSammelkopf;
    //				aktionaersSummen=blSammelkarten.rcAktionaersSummen;
    //
    //				fuelleTabWeisungssummenZeigeBereich(CInjects.weisungsAgenda[aktuelleGattung].getAbstimmungenListeM(), 1);
    //				fuelleTabWeisungssummenZeigeBereich(CInjects.weisungsAgenda[aktuelleGattung].getGegenantraegeListeM(), 2);
    //
    //			}
    //			if (sammelFehler) {
    //				CaZeigeHinweis caZeigeHinweis=new CaZeigeHinweis();
    //				caZeigeHinweis.zeige(eigeneStage, "Sammelident "+Integer.toString(aktuelleSammelMeldung.meldungsIdent)+" fehlerhaft!");
    //			}
    //		}
    //
    //	}
    //
    //	/**pArt=
    //	 * 	1 => "Normale" Agenda
    //	 *  2 => Gegenanträge
    //	 */
    //	private void fuelleTabWeisungssummenZeigeBereich(List<EclAbstimmungM> abstimmungenListeM, int pArt) {
    //
    //		int anzahlAbstimmungen=abstimmungenListeM.size();
    //		for (int i=0;i<anzahlAbstimmungen/*CInjects.weisungsAgendaAnzAgenda[aktuelleGattung]*/;i++){
    //			int abstimmungsPosition=abstimmungenListeM.get(i).getIdentWeisungssatz();
    //			if (abstimmungsPosition!=-1) {
    //				/*Erst ermitteln, ob Fehler in gesamter Summe - denn dann ganze Zeile rot!*/
    //				long summeGesamt=0;
    //				for (int i1=0;i1<=9;i1++) {
    //					if (i1!=KonstStimmart.splitLiegtVor) {
    //						summeGesamt+=weisungenSammelkopf.weisungMeldungSplit.abgabe[abstimmungsPosition][i1];
    //					}
    //				}
    //				if (summeGesamt!=aktuelleSammelMeldung.stimmen) {sammelFehler=true;}
    //
    //				for (int i1=0;i1<=9;i1++) {
    //					if (i1!=KonstStimmart.splitLiegtVor) {
    //						long wertSammelkarte=weisungenSammelkopf.weisungMeldungSplit.abgabe[abstimmungsPosition][i1];
    //						long wertAktionaere=aktionaersSummen.weisungMeldungSplit.abgabe[abstimmungsPosition][i1];
    //						if (wertSammelkarte!=wertAktionaere) {sammelFehler=true;}
    //					}
    //				}
    //			}
    //		}
    //
    //	}
    //
    //
    //
    //
    //
    //
    /** Ab hier individuell. */
    TableView<MInsti> tableInsti = null;

    /** The list insti. */
    ObservableList<MInsti> listInsti = null;

    /**
     * Initialize.
     */
    @FXML
    void initialize() {

        assert btnBestaendeInstiVerwalten != null
                : "fx:id=\"btnBestaendeInstiVerwalten\" was not injected: check your FXML file 'InstiPflege.fxml'.";
        assert btnInstiAendern != null
                : "fx:id=\"btnInstiAendern\" was not injected: check your FXML file 'InstiPflege.fxml'.";
        assert scpnUebersicht != null
                : "fx:id=\"scpnUebersicht\" was not injected: check your FXML file 'InstiPflege.fxml'.";
        assert btnNeuenInstiAnlegen != null
                : "fx:id=\"btnNeuenInstiAnlegen\" was not injected: check your FXML file 'InstiPflege.fxml'.";

        /** Ab hier individuell */

        zeigeInstiInTableView();
    }

    //	/************Click-Events**********************************/
    //
    //	/****Innerhalb der Maske****/
    //	@FXML
    //	void onCbNurAktiveAnzeigen(ActionEvent event) {
    //		zeigeSammelkartenInTableView();
    //	}
    //
    //
    //    @FXML
    //    void clickedZuordnenEKAlle(ActionEvent event) {
    //    	CaZeigeHinweis caZeigeHinweis=new CaZeigeHinweis();
    //    	boolean brc=caZeigeHinweis.zeige2Buttons(eigeneStage,
    //    			"Allen aktiven Sammelkarten wird aus dem Sammelkarten-Nummernkreis automatisch"+
    //    			" eine Eintrittskarte zugeordnet. Achtung, dauert etwas!",
    //    			"Weiter", "Abbrechen");
    //    	if (!brc) {return;}
    //
    //    	DbBundle lDbBundle=new DbBundle();
    //    	BlSammelkarten blSammelkarten=new BlSammelkarten(false, lDbBundle);
    //    	int rc=blSammelkarten.neueEKFuerAlleSammelkarten();
    //    	if (rc==-1) {
    //    		this.fehlerMeldung("Fehler bei Ausführung - bitte Eintrittskarten bei einzelnen Sammelkarten kontrollieren");
    //     	}
    //		zeigeSammelkartenInTableView();
    //    }
    //
    //
    //
    //	/*****Aufruf neuer Masken***/
    //	@FXML
    //	void clickedBtnBearbeitungKopfweisungen(ActionEvent event) {
    //		int selectedMeldeIdent=liefereSelektierteSammelkarte();
    //		if (selectedMeldeIdent==-1){return;}
    //
    //		CtrlSammelkartenKopfWeisungen controllerFenster=
    //				new CtrlSammelkartenKopfWeisungen();
    //		controllerFenster.init(selectedMeldeIdent);
    //
    //		Stage newStage=new Stage();
    //		CaController caController=new CaController();
    //		caController.open(newStage, controllerFenster, "/de/meetingapps/meetingclient/meetingBestand/SammelkartenKopfWeisungen.fxml", 1500, 890, "Sammelkarten Kopfweisungen", true);
    //		zeigeSammelkartenInTableView();
    //
    //	}
    /**
     * Clicked btn insti aendern.
     *
     * @param event the event
     */
    //
    @FXML
    void clickedBtnInstiAendern(ActionEvent event) {
        int selectedMeldeIdent = liefereSelektierteInsti();
        if (selectedMeldeIdent == -1) {
            return;
        }

        if (ParamS.paramServer.instisPflegbar==0) {
            zeigeFehlermeldung("Achtung - Grund-Daten der Instis können auf diesem Server nicht geändert werden!");
        }

        rufeDetailsAuf(2, "Institutionellen Ändern", selectedMeldeIdent, true);

    }

    //	@FXML
    //	void clickedBtnVertreterEintrittskarten(ActionEvent event) {
    //		int selectedMeldeIdent=liefereSelektierteSammelkarte();
    //		if (selectedMeldeIdent==-1){return;}
    //
    //		rufeDetailsAuf(3,"Sammelkarten Vertreter/Eintrittskarte zuordnen", selectedMeldeIdent, true);
    //
    //	}
    //
    //
    //	@FXML
    //	void clickedBtnDetailsAnzeigen(ActionEvent event) {
    //		int selectedMeldeIdent=liefereSelektierteSammelkarte();
    //		if (selectedMeldeIdent==-1){return;}
    //		rufeDetailsAuf(4,"Sammelkarten Details anzeigen", selectedMeldeIdent, false);
    //	}
    /**
     * Clicked btn neuen insti anlegen.
     *
     * @param event the event
     */
    //
    @FXML
    void clickedBtnNeuenInstiAnlegen(ActionEvent event) {
        if (ParamS.paramServer.instisPflegbar==0) {
            zeigeFehlermeldung("Grund-Daten der Instis können auf diesem Server nicht geändert werden!");
            return;
        }

        rufeDetailsAuf(1, "Institutionellen Neuanlage", 0, true);
    }

    /**
     * Clicked btn bestaende insti verwalten.
     *
     * @param event the event
     */
    @FXML
    void clickedBtnBestaendeInstiVerwalten(ActionEvent event) {
        int selectedMeldeIdent = liefereSelektierteInsti();
        if (selectedMeldeIdent == -1) {
            return;
        }

        rufeDetailsAuf(3, "Bestände Institutionellen Verwalten", selectedMeldeIdent, true);
    }

    //
    //
    //	@FXML
    //	void onBtnSammelkartenNeuStandard(ActionEvent event) {
    //		CaZeigeHinweis caZeigeHinweis=new CaZeigeHinweis();
    //		boolean bRc=caZeigeHinweis.zeige2Buttons(eigeneStage, "Achtung - es werden (zusätzlich) alle Standard-Sammelkarten angelegt!", "Fortsetzen", "Abbrechen");
    //		if (bRc==false) {
    //			return;
    //		}
    //		DbBundle lDbBundle=new DbBundle();
    //		StubMandantAnlegen stubMandantAnlegen=new StubMandantAnlegen(false, lDbBundle);
    //    	stubMandantAnlegen.sammelkartenSandardAnlegen();
    //    	zeigeSammelkartenInTableView();
    //
    //	}
    //
    //
    //
    /**
     * funktion: 1=Neuen Insti anlegen 2=Insti ändern 3=Bestand pflegen
     * 
     * pRefresh=true: Übersichtsanzeige wird nach Rückkehr wieder aufgebaut.
     *
     * @param pFunktion           the funktion
     * @param pHeaderText         the header text
     * @param pSelectedInstiIdent the selected insti ident
     * @param pRefresh            the refresh
     */
    private void rufeDetailsAuf(int pFunktion, String pHeaderText, int pSelectedInstiIdent, boolean pRefresh) {

        Stage newStage = new Stage();
        CaIcon.bestandsverwaltung(newStage);
        CtrlInstiPflegeDetails controllerFenster = new CtrlInstiPflegeDetails();
        controllerFenster.init(newStage, pSelectedInstiIdent, pFunktion);

        CaController caController = new CaController();
        caController.open(newStage, controllerFenster,
                "/de/meetingapps/meetingclient/meetingBestand/InstiPflegeDetails.fxml", 1500, 760, pHeaderText,
                true);

        if (pRefresh) {
            zeigeInstiInTableView();
        }

    }

    //
    //
    //
    //
    /** Bereitet Sammelkarten-Übersicht auf. dbBundle muß geöffnet sein */
    private void zeigeInstiInTableView() {

        CtrlInstiPflegeUebergreifend ctrlInstiPflegeUebergreifend = new CtrlInstiPflegeUebergreifend();

        /** Table-View vorbereiten */
        tableInsti = ctrlInstiPflegeUebergreifend.vorbereitenTableViewInsti();
        tableInsti.setPrefHeight(676);
        tableInsti.setPrefWidth(1464);

        /** Daten einlesen */
        int anzahlInsti = ctrlInstiPflegeUebergreifend.holeInstiDaten(0);

        if (anzahlInsti == 0) {
            fehlerMeldung("Keine Institutionellen vorhanden!");
            scpnUebersicht.setContent(null);
            return;
        } else {
            listInsti = ctrlInstiPflegeUebergreifend.rcListInsti;
            tableInsti.setItems(listInsti);
            scpnUebersicht.setContent(tableInsti);
        }

    }

    /**
     * Prüfen, ob eine Insti selektiert wurde. -1 => keine Selektion, dann auch
     * gleich Fehlermeldung. sonst ident
     *
     * @return the int
     */
    private int liefereSelektierteInsti() {
        int selectedIndex = this.tableInsti.getSelectionModel().getSelectedIndex();
        if (selectedIndex == -1) {
            this.fehlerMeldung("Bitte erst Institutionelle durch Anklicken auswählen!");
            return (-1);
        }

        int selectedInstiIdent = listInsti.get(selectedIndex).getIdent();

        return selectedInstiIdent;
    }

    /**
     * Inits the.
     *
     * @param pEigeneStage the eigene stage
     */
    public void init(Stage pEigeneStage) {
        eigeneStage = pEigeneStage;

    }
    
    private void zeigeFehlermeldung(String meldung) {
        CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
        caZeigeHinweis.zeige(eigeneStage, meldung);
    }

}
