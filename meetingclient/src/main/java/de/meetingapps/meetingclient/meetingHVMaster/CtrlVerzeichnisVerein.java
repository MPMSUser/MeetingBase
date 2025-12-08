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
import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaDateiWrite;
import de.meetingapps.meetingportal.meetComAllg.CaDatumZeit;
import de.meetingapps.meetingportal.meetComAllg.CaString;
import de.meetingapps.meetingportal.meetComBl.BlHybridMitglieder;
import de.meetingapps.meetingportal.meetComBl.BlNummernformBasis;
import de.meetingapps.meetingportal.meetComBl.BlPraesenzlistenDruck;
import de.meetingapps.meetingportal.meetComBl.BlWIllenserklaerungStapel;
import de.meetingapps.meetingportal.meetComBl.BlWillenserklaerung;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclAbstimmungku310;
import de.meetingapps.meetingportal.meetComEntities.EclAbstimmungMeldungSplit;
import de.meetingapps.meetingportal.meetComEntities.EclMeldung;
import de.meetingapps.meetingportal.meetComEntities.EclWeisungMeldung;
import de.meetingapps.meetingportal.meetComHVParam.ParamS;
import de.meetingapps.meetingportal.meetComHVParam.ParamSpezial;
import de.meetingapps.meetingportal.meetComKonst.KonstMeldung;
import de.meetingapps.meetingportal.meetingCoreReport.RpDrucken;
import de.meetingapps.meetingportal.meetingCoreReport.RpVariablen;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * The Class CtrlVerzeichnisVerein.
 */
public class CtrlVerzeichnisVerein {

    /** The log drucken. */
    private int logDrucken = 10;

    /** The resources. */
    @FXML
    private ResourceBundle resources;

    /** The location. */
    @FXML
    private URL location;

    /** The btn verzeichnis verein. */
    @FXML
    private Button btnVerzeichnisVerein;

    /** The btn verzeichnis verein bisher anwesende. */
    @FXML
    private Button btnVerzeichnisVereinBisherAnwesende;

    /** The btn abbrechen. */
    @FXML
    private Button btnAbbrechen;

    /** The tf gattung. */
    @FXML
    private TextField tfGattung;

    /** The tf von. */
    @FXML
    private TextField tfVon;

    /** The tf bis. */
    @FXML
    private TextField tfBis;

    /** The btn drucken. */
    @FXML
    private Button btnDrucken;

    /** The btn hybrid abstimmungen von portal. */
    @FXML
    private Button btnHybridAbstimmungenVonPortal;

    /** The btn hybrid hole teilnehmer. */
    @FXML
    private Button btnHybridHoleTeilnehmer;

    /** The btn hybrid sperre auf portal. */
    @FXML
    private Button btnHybridSperreAufPortal;

    /** The btn ku310 sperre alle teilnehmer. */
    @FXML
    private Button btnku310SperreAlleTeilnehmer;

    /** The btn ku310 abstimmungsauswertung. */
    @FXML
    private Button btnku310Abstimmungsauswertung;

    /** The btn ku310 praesenzkonsolidierung. */
    @FXML
    private Button btnku310Praesenzkonsolidierung;

    /** The btn weisungskonsolidierung. */
    @FXML
    private Button btnWeisungskonsolidierung;

    /** The btn nordz EK. */
    @FXML
    private Button btnNordzEK;

    /**
     * Clicked btn nordz EK.
     *
     * @param event the event
     */
    @FXML
    void clickedBtnNordzEK(ActionEvent event) {
        CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
        boolean brc = caZeigeHinweis.zeige2Buttons(eigeneStage, "EK für ku302_303 zuordnen?", "Weiter", "Abbrechen");
        if (brc == false) {
            return;
        }
        DbBundle lDbBundle = new DbBundle();
        lDbBundle.openAll();

        lDbBundle.dbMeldungen.leseAlleMeldungen();
        EclMeldung[] meldungenArray = lDbBundle.dbMeldungen.meldungenArray;
        for (int i = 0; i < meldungenArray.length; i++) {
            EclMeldung lMeldung = meldungenArray[i];
            if (lMeldung.meldungstyp == KonstMeldung.KARTENART_EINZELKARTE ||
                    lMeldung.meldungstyp == KonstMeldung.KARTENART_IN_SAMMELKARTE) {
                if (lMeldung.meldungAktiv == 1) {
                    int neueEkNr = Integer.parseInt(lMeldung.aktionaersnummer) / 10;
                    String sNeueEkNr = Integer.toString(neueEkNr);
                    sNeueEkNr = CaString.fuelleLinksNull(sNeueEkNr, 5);
                    System.out.println(sNeueEkNr);

                    String alteEkNr = lMeldung.zutrittsIdent;
                    if (alteEkNr.equals(sNeueEkNr) == false) {

                        BlWillenserklaerung ekWillenserklaerung = new BlWillenserklaerung();
                        ekWillenserklaerung.pErteiltAufWeg = 51;

                        ekWillenserklaerung.piMeldungsIdentAktionaer = lMeldung.meldungsIdent;

                        ekWillenserklaerung.pWillenserklaerungGeberIdent = -1; /*Aktionär*/

                        /*Versandart*/
                        ekWillenserklaerung.pVersandartEK = 3;
                        ekWillenserklaerung.pZutrittsIdent.zutrittsIdent = sNeueEkNr;
                        ekWillenserklaerung.pZutrittsIdent.zutrittsIdentNeben = "00";

                        ekWillenserklaerung.neueZutrittsIdentZuMeldung(lDbBundle); /*Früher: V2*/
                        if (ekWillenserklaerung.rcIstZulaessig == false) {
                            System.out.println("Fehler 002 bei " + sNeueEkNr + "="
                                    + ekWillenserklaerung.rcGrundFuerUnzulaessig);
                        }

                    }

                }
            }

        }

        lDbBundle.closeAll();
        caZeigeHinweis.zeige(eigeneStage, "Fertig!");

    }

    /** The weisung meldung array. */
    private EclWeisungMeldung[] weisungMeldungArray = null;

    /**
     * Kopiert die letzte Weisung eines Aktionärs, wenn sie nur einen zu
     * programmierenden TOP enthält, auf die vorletzte Weisung So programmiert für
     * Rocket - und aktuell nur für diese brauchbar.
     *
     * @param event the event
     */
    @FXML
    void clickedBtnWeisungskonsolidierung(ActionEvent event) {
        CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
        boolean brc = caZeigeHinweis.zeige2Buttons(eigeneStage,
                "Weisungskonsolidierung durchführen? (Achtung, individuell je Mandant zu programmieren! Finger weg!)",
                "Weiter", "Abbrechen");
        if (brc == false) {
            return;
        }
        DbBundle lDbBundle = new DbBundle();
        lDbBundle.openAll();
        lDbBundle.dbWeisungMeldung.createTable_Merge();

        /** Alle aus tbl_weisungmeldung einlesen */
        lDbBundle.dbWeisungMeldung.leseAktive_Aktuelle();
        weisungMeldungArray = lDbBundle.dbWeisungMeldung.weisungMeldungArray;
        int weisungIdent = 1;

        for (int i = 0; i < weisungMeldungArray.length; i++) {
            boolean istAusZweitemLauf = pruefeObWeisungAusZweitemGang(i);
            if (istAusZweitemLauf == true) {
                EclWeisungMeldung lWeisungMeldungNeu = weisungMeldungArray[i];
                lWeisungMeldungNeu.weisungIdent = weisungIdent;
                weisungIdent++;

                int lMeldungsIdent = lWeisungMeldungNeu.meldungsIdent;
                /** Aus Speicherung tbl_weisungmeldung_1 einlesen */
                int rc = lDbBundle.dbWeisungMeldung.leseAktiveZuMeldung_1(lMeldungsIdent);
                if (rc > 0) {
                    EclWeisungMeldung lWeisungMeldungErgaenzung = lDbBundle.dbWeisungMeldung.weisungMeldungArray[0];
                    CaBug.druckeLog("Weisungen aus Archiv übertragen rc=" + rc + " lMeldungsIdent=" + lMeldungsIdent,
                            logDrucken, 10);
                    for (int i1 = 1; i1 <= 22; i1++) {
                        if (lMeldungsIdent == 78) {
                            CaBug.druckeLog("i1=" + i1 + " lWeisungMeldungErgaenzung.abgabe[i1]="
                                    + lWeisungMeldungErgaenzung.abgabe[i1], logDrucken, 10);
                        }
                        lWeisungMeldungNeu.abgabe[i1] = lWeisungMeldungErgaenzung.abgabe[i1];

                    }
                }
                lDbBundle.dbWeisungMeldung.insert_Merge(lWeisungMeldungNeu);
            }
        }

        /*Nun noch alle Weisungen aus erstem Gang*/
        lDbBundle.dbWeisungMeldung.leseAktive_1();
        weisungMeldungArray = lDbBundle.dbWeisungMeldung.weisungMeldungArray;
        for (int i = 0; i < weisungMeldungArray.length; i++) {
            EclWeisungMeldung lWeisungMeldungNeu = weisungMeldungArray[i];

            int inMergeVorhanden = lDbBundle.dbWeisungMeldung.pruefeObInMerge(lWeisungMeldungNeu.meldungsIdent);
            if (inMergeVorhanden == 0) {
                lWeisungMeldungNeu.weisungIdent = weisungIdent;
                weisungIdent++;
                lDbBundle.dbWeisungMeldung.insert_Merge(lWeisungMeldungNeu);
            }
        }

        /** Nun Report ausgeben */
        lDbBundle.dbWeisungMeldung.leseAktive_merge();
        weisungMeldungArray = lDbBundle.dbWeisungMeldung.weisungMeldungArray;

        CaDateiWrite dateiExport = new CaDateiWrite();
        dateiExport.trennzeichen = ';';
        dateiExport.dateiart = ".csv";

        dateiExport.oeffne(lDbBundle, "AktionaereMitWeisung");

        dateiExport.ausgabe("EK");
        dateiExport.ausgabe("AktionärsNummer");
        ;
        dateiExport.ausgabe("Name");
        ;
        dateiExport.ausgabe("Vorname");
        ;
        dateiExport.ausgabe("Ort");
        ;
        dateiExport.ausgabe("Aktien");
        dateiExport.newline();

        for (int i = 0; i < weisungMeldungArray.length; i++) {
            EclWeisungMeldung lWeisungMeldungNeu = weisungMeldungArray[i];
            int lMeldungsIdent = lWeisungMeldungNeu.meldungsIdent;
            lDbBundle.dbMeldungen.leseZuMeldungsIdent(lMeldungsIdent);
            EclMeldung lMeldung = lDbBundle.dbMeldungen.meldungenArray[0];

            dateiExport.ausgabe(lMeldung.zutrittsIdent);
            dateiExport.ausgabe(lMeldung.aktionaersnummer);
            dateiExport.ausgabe(lMeldung.name);
            dateiExport.ausgabe(lMeldung.vorname);
            dateiExport.ausgabe(lMeldung.ort);
            dateiExport.ausgabe(Long.toString(lMeldung.stimmen));
            for (int i1 = 1; i1 <= 23; i1++) {
                if (i1 <= 15 || i1 == 23) {
                    int abstimmung = lWeisungMeldungNeu.abgabe[i1];
                    switch (abstimmung) {
                    case 1: {
                        dateiExport.ausgabe("J");
                        break;
                    }
                    case 2: {
                        dateiExport.ausgabe("N");
                        break;
                    }
                    case 3: {
                        dateiExport.ausgabe("E");
                        break;
                    }
                    default: {
                        dateiExport.ausgabe("");
                        break;
                    }
                    }
                }
            }
            dateiExport.newline();

        }
        dateiExport.schliessen();

        lDbBundle.closeAll();
        caZeigeHinweis.zeige(eigeneStage, "Konsolidiert!");

    }

    /**
     * Pruefe ob weisung aus zweitem gang.
     *
     * @param pOffset the offset
     * @return true, if successful
     */
    private boolean pruefeObWeisungAusZweitemGang(int pOffset) {
        EclWeisungMeldung lWeisungMeldung = weisungMeldungArray[pOffset];

        if (lWeisungMeldung.aktiv != 1) {
            return false;
        }
        for (int i = 1; i <= 22; i++) {
            if (lWeisungMeldung.abgabe[i] != 0) {
                return false;
            }
        }

        return true;
    }

    /**
     * Clicked btn ku310 praesenzkonsolidierung.
     *
     * @param event the event
     */
    @FXML
    void clickedBtnku310Praesenzkonsolidierung(ActionEvent event) {
        CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
        boolean brc = caZeigeHinweis.zeige2Buttons(eigeneStage,
                "Achtung - Präsenzdaten werden von Gattung 1 auf Gattung 2 übertragen", "Weiter", "Abbrechen");
        if (brc == false) {
            return;
        }

        DbBundle lDbBundle = new DbBundle();
        lDbBundle.openAll();
        lDbBundle.dbMeldungen.leseAlleMeldungenGattung(1);
        EclMeldung[] meldungenMitGattung1 = lDbBundle.dbMeldungen.meldungenArray;
        for (int i = 0; i < meldungenMitGattung1.length; i++) {
            if (meldungenMitGattung1[i].meldungstyp != KonstMeldung.KARTENART_SAMMELKARTE) {
                lDbBundle.dbMeldungen.leseZuAktionaersnummer(meldungenMitGattung1[i]);
                for (int i1 = 0; i1 < lDbBundle.dbMeldungen.meldungenArray.length; i1++) {
                    if (lDbBundle.dbMeldungen.meldungenArray[i1].gattung == 2) {
                        lDbBundle.dbMeldungen.meldungenArray[i1].statusPraesenz = meldungenMitGattung1[i].statusPraesenz;
                        lDbBundle.dbMeldungen.meldungenArray[i1].statusPraesenz_Delayed = meldungenMitGattung1[i].statusPraesenz_Delayed;
                        lDbBundle.dbMeldungen.meldungenArray[i1].statusWarPraesenz_Delayed = meldungenMitGattung1[i].statusWarPraesenz_Delayed;
                        lDbBundle.dbMeldungen.update(lDbBundle.dbMeldungen.meldungenArray[i1]);
                    }
                }
            }
        }

        lDbBundle.closeAll();
        caZeigeHinweis.zeige(eigeneStage, "Übertragen!");
    }

    /**
     * Clicked btn ku310 abstimmungsauswertung.
     *
     * @param event the event
     */
    @FXML
    void clickedBtnku310Abstimmungsauswertung(ActionEvent event) {
        
        if (1==1) {
            DbBundle lDbBundle=new DbBundle();
            lDbBundle.openAll();
            BlWIllenserklaerungStapel blWillenserklaerungStapel=new BlWIllenserklaerungStapel(lDbBundle);
            blWillenserklaerungStapel.widerrufeInaktiveVollmachtenWeisungen();
            lDbBundle.closeAll();
            return;
        }
        
        
        
        
        
        
        /*Überträgt die Vertreterdaten*/
        CaZeigeHinweis caZeigeHinweis=new CaZeigeHinweis();
        boolean brc=caZeigeHinweis.zeige2Buttons(eigeneStage, "Auf ku310-Vertreter übertragen", "Übertragen", "Abbrechen");
        if (brc==false) {
            return;
        }
        
        int quelleIdentWeisungssatz[]= {1, 2, 4};
        int zielIdentWeisungssatz[]= {8, 9, 11};
        
        int aktienregisterIdentVertreter=160;
        
        DbBundle lDbBundle=new DbBundle();
        lDbBundle.openAll();
        lDbBundle.openWeitere();
        /*AG=Satz 1, AN=Satz 2
         * 
         *  
         */
        EclAbstimmungMeldungSplit[] lAbstimmungMeldungSplit=new EclAbstimmungMeldungSplit[3];
        lDbBundle.dbAbstimmungMeldungSplit.leseIdent((1+10)*(-1));
        lAbstimmungMeldungSplit[1]=lDbBundle.dbAbstimmungMeldungSplit.ergebnisPositionGefunden(0);
        lDbBundle.dbAbstimmungMeldungSplit.leseIdent((2+10)*(-1));
        lAbstimmungMeldungSplit[2]=lDbBundle.dbAbstimmungMeldungSplit.ergebnisPositionGefunden(0);
        
        for (int i=0;i<quelleIdentWeisungssatz.length;i++) {
            lDbBundle.dbAbstimmungku310.read(aktienregisterIdentVertreter, zielIdentWeisungssatz[i]);
            for (int i1=0;i1<lDbBundle.dbAbstimmungku310.anzErgebnis();i1++) {
                EclAbstimmungku310 lAbstimmungku310=lDbBundle.dbAbstimmungku310.ergebnisPosition(i1);
                lAbstimmungku310.stimmenUebernehmen=1;
                lAbstimmungku310.jaStimmen=lAbstimmungMeldungSplit[lAbstimmungku310.geberOderNehmer].
                        abgabe[quelleIdentWeisungssatz[i]][1];
                lAbstimmungku310.neinStimmen=lAbstimmungMeldungSplit[lAbstimmungku310.geberOderNehmer].
                        abgabe[quelleIdentWeisungssatz[i]][2];
                lAbstimmungku310.enthaltungStimmen=lAbstimmungMeldungSplit[lAbstimmungku310.geberOderNehmer].
                        abgabe[quelleIdentWeisungssatz[i]][3];
               
                lAbstimmungku310.gesamtStimmen=lAbstimmungku310.jaStimmen+lAbstimmungku310.neinStimmen+lAbstimmungku310.enthaltungStimmen;
                
                lDbBundle.dbAbstimmungku310.update(lAbstimmungku310);
            }
        }
       
        lDbBundle.closeAll();
        
        caZeigeHinweis.zeige(eigeneStage, "Übertragen");
    
    }

    /**
     * Clicked btn ku310 sperre alle teilnehmer.
     *
     * @param event the event
     */
    @FXML
    void clickedBtnku310SperreAlleTeilnehmer(ActionEvent event) {
        CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
        boolean brc = caZeigeHinweis.zeige2Buttons(eigeneStage, "Achtung - alle Portalzugänge werden gesperrt",
                "Weiter", "Abbrechen");
        if (brc == false) {
            return;
        }

        DbBundle lDbBundle = new DbBundle();
        lDbBundle.openAll();
        lDbBundle.dbLoginDaten.update_alleUserGesperrt();

        lDbBundle.closeAll();
        caZeigeHinweis.zeige(eigeneStage, "Gesperrt!");

    }

    /**
     * Initialize.
     */
    @FXML
    void initialize() {
        assert btnVerzeichnisVerein != null
                : "fx:id=\"btnPraesenzFeststellen\" was not injected: check your FXML file 'PraesenzFeststellen.fxml'.";
        assert btnAbbrechen != null
                : "fx:id=\"btnAbbrechen\" was not injected: check your FXML file 'PraesenzFeststellen.fxml'.";

        /********************************
         * Ab hier individuell
         *********************************************/
        if (ParamS.param.paramPortal.hybridTeilnahmeAktiv == 0) {
            btnHybridHoleTeilnehmer.setVisible(false);
            btnHybridSperreAufPortal.setVisible(false);
            btnHybridAbstimmungenVonPortal.setVisible(false);
        }

    }

    /**
     * Clicked btn drucken.
     *
     * @param event the event
     */
    @FXML
    void clickedBtnDrucken(ActionEvent event) {
        String von = tfVon.getText();
        String bis = tfBis.getText();

        DbBundle lDbBundle = new DbBundle();
        lDbBundle.openAll();

        RpDrucken rpDrucken = new RpDrucken();
        rpDrucken = new RpDrucken();
        rpDrucken.initClientDrucke();

        BlPraesenzlistenDruck blPraesenzlistenDruck = new BlPraesenzlistenDruck();
        blPraesenzlistenDruck.druckeTeilnehmerverzeichnisZeitraum(lDbBundle, rpDrucken, von, bis, "01", 5);
    }

    /**
     * Clicked btn verzeichnis verein bisher anwesende.
     *
     * @param event the event
     */
    @FXML
    void clickedBtnVerzeichnisVereinBisherAnwesende(ActionEvent event) {
        verzeichnisVereinAusfuehren(1);
    }

    /**
     * Clicked btn verzeichnis verein.
     *
     * @param event the event
     */
    @FXML
    void clickedBtnVerzeichnisVerein(ActionEvent event) {
        verzeichnisVereinAusfuehren(2);
    }

    /**
     * Clicked btn hybrid sperre auf portal.
     *
     * @param event the event
     */
    @FXML
    void clickedBtnHybridSperreAufPortal(ActionEvent event) {
        DbBundle lDbBundle = new DbBundle();

        BlHybridMitglieder blHybridMitglieder = new BlHybridMitglieder(false, lDbBundle);
        int rc = blHybridMitglieder.setztePraesenteFuerAbstimmungGesperrtInPortal();

        CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
        if (rc < 1) {
            caZeigeHinweis.zeige(eigeneStage, "Fehler!");
        } else {
            caZeigeHinweis.zeige(eigeneStage, "Sperre für Abstimmung - Erledigt!");
        }

    }

    /**
     * Clicked btn hybrid hole teilnehmer.
     *
     * @param event the event
     */
    @FXML
    void clickedBtnHybridHoleTeilnehmer(ActionEvent event) {
        DbBundle lDbBundle = new DbBundle();

        BlHybridMitglieder blHybridMitglieder = new BlHybridMitglieder(false, lDbBundle);
        int rc = blHybridMitglieder.holePraesenteVonServer();

        CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
        if (rc < 1) {
            caZeigeHinweis.zeige(eigeneStage, "Fehler!");
        } else {
            caZeigeHinweis.zeige(eigeneStage, "Teilnehmer von Portal - Erledigt!");
        }

    }

    /**
     * Clicked btn hybrid abstimmungen von portal.
     *
     * @param event the event
     */
    @FXML
    void clickedBtnHybridAbstimmungenVonPortal(ActionEvent event) {
        DbBundle lDbBundle = new DbBundle();

        BlHybridMitglieder blHybridMitglieder = new BlHybridMitglieder(false, lDbBundle);
        int rc = blHybridMitglieder.uebertragePortalAbstimmungInLokaleAbstimmung();

        CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
        if (rc < 1) {
            caZeigeHinweis.zeige(eigeneStage, "Fehler!");
        } else {
            caZeigeHinweis.zeige(eigeneStage, "Abstimmungen von Portal - Erledigt!");
        }

    }

    /**
     * Verzeichnis verein ausfuehren.
     *
     * @param alleOderNurAnwesend the alle oder nur anwesend
     */
    private void verzeichnisVereinAusfuehren(int alleOderNurAnwesend) {
        DbBundle lDbBundle = new DbBundle();
        lDbBundle.openAll();

        int gattung = -1;
        String hGattung = tfGattung.getText().trim();
        if (!hGattung.isEmpty()) {
            gattung = Integer.parseInt(hGattung);
        }

        /*+++++++++++++++++++++++++++++++++Briefwähler+++++++++++++++++++++++++++++++++++++++++++/
        /**Anz Briefwähler der Gattung 1
         * Muß als erstes eingelesen werden, da dbMeldungen anschließend wieder benötigt*/
        int anzBriefwahlGattung1 = lDbBundle.dbMeldungen.anzahlMitgliederBriefwahl();

        long anzBriefwahlStimmenGattung1 = lDbBundle.dbMeldungen.anzahlStimmenBriefwahl();

        /*++++++++++++++++++++++++++++++++++präsente+++++++++++++++++++++++++++*/
        boolean mitOnline = (lDbBundle.param.paramPortal.hybridTeilnahmeAktiv == 1);
        if (alleOderNurAnwesend == 1) {
            lDbBundle.dbMeldungen.leseAlleMeldungenIstOderWarAnwesend(gattung, mitOnline);
        } else {
            lDbBundle.dbMeldungen.leseAlleMeldungenAnwesend(gattung, mitOnline);
        }

        /** Anz Vertretene Mitglieder insgesamt, unabhängig von Gattung */
        int anzVertreten = lDbBundle.dbMeldungen.anzErgebnis();
        int anzVertretenPhysikalisch = 0;
        int anzVertretenOnline = 0;

        /*Anz Vertretene Mitglieder der betreffenden Gattung*/
        int anzVertretenGattung1 = 0;
        int anzVertretenGattung2 = 0;
        int anzVertretenGattung3 = 0;

        int anzVertretenGattung1Physikalisch = 0;
        int anzVertretenGattung2Physikalisch = 0;
        int anzVertretenGattung3Physikalisch = 0;

        int anzVertretenGattung1Online = 0;
        int anzVertretenGattung2Online = 0;
        int anzVertretenGattung3Online = 0;

        /** Anzahl Stimmen insgesamt, unabhängig von Gattung */
        long stimmenGesamt = 0;
        long aktienGesamt = 0;
        long stimmenGesamtPhysikalisch = 0;
        long stimmenGesamtOnline = 0;

        /** Anzahl Stimmen der betreffenden Gattung */
        long stimmenGattung1 = 0;
        long aktienGattung1 = 0;
        long stimmenGattung1Physikalisch = 0;
        long stimmenGattung1Online = 0;

        long stimmenGattung2 = 0;
        long aktienGattung2 = 0;
        long stimmenGattung2Physikalisch = 0;
        long stimmenGattung2Online = 0;

        long stimmenGattung3 = 0;
        long aktienGattung3 = 0;
        long stimmenGattung3Physikalisch = 0;
        long stimmenGattung3Online = 0;

        for (int i = 0; i < anzVertreten; i++) {
            EclMeldung lMeldung = lDbBundle.dbMeldungen.meldungenArray[i];

            boolean onlinePraesent = false;
            boolean physikalischPraesent = false;
            boolean zaehltOnline = false;

            if (mitOnline) {
                if (alleOderNurAnwesend == 1) {
                    if (lMeldung.statusPraesenz != 0) {
                        physikalischPraesent = true;
                    } else {
                        zaehltOnline = true;
                    }
                    if (!lMeldung.zusatzfeld4.isEmpty()) {
                        onlinePraesent = true;
                    }
                } else { /*Nur aktuell anwesende*/
                    if (lMeldung.statusPraesenz == 1) {
                        physikalischPraesent = true;
                    } else {
                        zaehltOnline = true;
                    }
                    if (lMeldung.zusatzfeld4.equals("1")) {
                        onlinePraesent = true;
                    }
                }
                if (onlinePraesent && physikalischPraesent) {
                    lMeldung.zusatzfeld4 = "Präsenz-Teilnahme / Online-Teilnahme";
                }
                if (onlinePraesent && physikalischPraesent == false) {
                    lMeldung.zusatzfeld4 = "Online-Teilnahme";
                }
                if (onlinePraesent == false && physikalischPraesent) {
                    lMeldung.zusatzfeld4 = "Präsenz-Teilnahme";
                }
            } else {
                physikalischPraesent = true;
                lMeldung.zusatzfeld4 = "Präsenz-Teilnahme";
            }

            stimmenGesamt += lMeldung.stimmen;
            aktienGesamt += lMeldung.stueckAktien;
            int lGattung = lMeldung.liefereGattung();
            if (lGattung == 1) {
                anzVertretenGattung1++;
                stimmenGattung1 += lMeldung.stimmen;
                aktienGattung1 += lMeldung.stueckAktien;
            }
            if (lGattung == 2) {
                anzVertretenGattung2++;
                stimmenGattung2 += lMeldung.stimmen;
                aktienGattung2 += lMeldung.stueckAktien;
            }
            if (lGattung == 3) {
                anzVertretenGattung3++;
                stimmenGattung3 += lMeldung.stimmen;
                aktienGattung3 += lMeldung.stueckAktien;
            }

            if (zaehltOnline) {
                anzVertretenOnline++;
                stimmenGesamtOnline += lMeldung.stimmen;
                if (lGattung == 1) {
                    anzVertretenGattung1Online++;
                    stimmenGattung1Online += lMeldung.stimmen;
                }
                if (lGattung == 2) {
                    anzVertretenGattung2Online++;
                    stimmenGattung2Online += lMeldung.stimmen;
                }
                if (lGattung == 3) {
                    anzVertretenGattung3Online++;
                    stimmenGattung3Online += lMeldung.stimmen;
                }
            } else {
                anzVertretenPhysikalisch++;
                stimmenGesamtPhysikalisch += lMeldung.stimmen;
                if (lGattung == 1) {
                    anzVertretenGattung1Physikalisch++;
                    stimmenGattung1Physikalisch += lMeldung.stimmen;
                }
                if (lGattung == 2) {
                    anzVertretenGattung2Physikalisch++;
                    stimmenGattung2Physikalisch += lMeldung.stimmen;
                }
                if (lGattung == 3) {
                    anzVertretenGattung3Physikalisch++;
                    stimmenGattung3Physikalisch += lMeldung.stimmen;
                }
            }

        }

        /*+++++++++++++++++++++++++Restliche Variablen füllen++++++++++++++++++++++++++++++++*/

        int anzVertretenG1G3undBriefwahl = anzVertretenGattung1 + anzVertretenGattung3 + anzBriefwahlGattung1;
        long stimmenGattung1Briefwahl = stimmenGattung1 + anzBriefwahlStimmenGattung1;

        String hDatum = CaDatumZeit.DatumStringFuerAnzeige();
        String hZeit = CaDatumZeit.StundenMinutenStringFuerAnzeige();

        /**************** Formular zusammenstellung *************************/
        RpDrucken rpDrucken = new RpDrucken();
        rpDrucken.initClientDrucke();

        rpDrucken.initFormular(lDbBundle);

        RpVariablen rpVariablen = new RpVariablen(lDbBundle);
        rpVariablen.feststellungVereinZusammenstellung("01", rpDrucken);
        rpDrucken.startFormular();

        rpVariablen.fuelleVariable(rpDrucken, "Ueberschrift.Datum", hDatum);
        rpVariablen.fuelleVariable(rpDrucken, "Ueberschrift.Zeit", hZeit);
        rpVariablen.fuelleVariable(rpDrucken, "Ueberschrift.GattungDruck", Integer.toString(gattung));

        rpVariablen.fuelleVariable(rpDrucken, "AnwesendeVertretenMitglieder", CaString.toStringDE(anzVertreten));
        rpVariablen.fuelleVariable(rpDrucken, "VertreteneStimmen", CaString.toStringDE(stimmenGesamt));
        rpVariablen.fuelleVariable(rpDrucken, "VertreteneAktien", CaString.toStringDE(aktienGesamt));

        rpVariablen.fuelleVariable(rpDrucken, "AnwesendeVertretenMitgliederG1",
                CaString.toStringDE(anzVertretenGattung1));
        rpVariablen.fuelleVariable(rpDrucken, "AnwesendeVertretenMitgliederG2",
                CaString.toStringDE(anzVertretenGattung2));
        rpVariablen.fuelleVariable(rpDrucken, "AnwesendeVertretenMitgliederG3",
                CaString.toStringDE(anzVertretenGattung3));
        rpVariablen.fuelleVariable(rpDrucken, "AnwesendeVertretenMitgliederG1_3",
                CaString.toStringDE(anzVertretenGattung1 + anzVertretenGattung3));

        rpVariablen.fuelleVariable(rpDrucken, "AnwesendeVertretenMitgliederG1_Briefwahl",
                CaString.toStringDE(anzBriefwahlGattung1));
        rpVariablen.fuelleVariable(rpDrucken, "AnwesendeVertretenMitgliederG1_3Alle",
                CaString.toStringDE(anzVertretenG1G3undBriefwahl));

        rpVariablen.fuelleVariable(rpDrucken, "VertreteneStimmenG1", CaString.toStringDE(stimmenGattung1));
        rpVariablen.fuelleVariable(rpDrucken, "VertreteneAktienG1", CaString.toStringDE(aktienGattung1));

        rpVariablen.fuelleVariable(rpDrucken, "VertreteneStimmenG1Briefwahl",
                CaString.toStringDE(stimmenGattung1Briefwahl));
        if (anzVertretenG1G3undBriefwahl > 95) {
            rpVariablen.fuelleVariable(rpDrucken, "AnzahlErreicht", "1");
        } else {
            rpVariablen.fuelleVariable(rpDrucken, "AnzahlErreicht", "0");
        }
        rpVariablen.fuelleVariable(rpDrucken, "VertreteneStimmenAktienG1",
                CaString.toStringDE(stimmenGattung1 + aktienGattung1));

        rpVariablen.fuelleVariable(rpDrucken, "VertreteneStimmenG2", CaString.toStringDE(stimmenGattung2));
        rpVariablen.fuelleVariable(rpDrucken, "VertreteneAktienG2", CaString.toStringDE(aktienGattung2));
        rpVariablen.fuelleVariable(rpDrucken, "VertreteneStimmenAktienG2",
                CaString.toStringDE(stimmenGattung2 + aktienGattung2));

        /*Hinweis zu %-Werten: stimmen in Relation zu Kapital-Stück Aktien.
         * aktien in Relation zu Kapital-Stück-Aktien-vermindert
         */
        double hProzentStimmenGattung1 = 0.0;
        double hProzentStimmenGattung2 = 0.0;
        hProzentStimmenGattung1 = CaString.berechneProzent(stimmenGattung1,
                lDbBundle.param.paramBasis.grundkapitalStueck[1 - 1]);
        hProzentStimmenGattung2 = CaString.berechneProzent(stimmenGattung2,
                lDbBundle.param.paramBasis.grundkapitalStueck[2 - 1]);
        String hProzentString = CaString.prozentToString(hProzentStimmenGattung1);
        rpVariablen.fuelleVariable(rpDrucken, "ProzentStimmenG1", hProzentString);
        if (gattung == 1) {
            rpVariablen.fuelleVariable(rpDrucken, "ProzentStimmenGESAMT", hProzentString);
        }
        hProzentString = CaString.prozentToString(hProzentStimmenGattung2);
        rpVariablen.fuelleVariable(rpDrucken, "ProzentStimmenG2", hProzentString);
        if (gattung == 2) {
            rpVariablen.fuelleVariable(rpDrucken, "ProzentStimmenGESAMT", hProzentString);
        }

        double hProzentAktienGattung1 = 0.0;
        double hProzentAktienGattung2 = 0.0;
        hProzentAktienGattung1 = CaString.berechneProzent(aktienGattung1,
                lDbBundle.param.paramBasis.grundkapitalVermindertStueck[1 - 1]);
        hProzentAktienGattung2 = CaString.berechneProzent(aktienGattung2,
                lDbBundle.param.paramBasis.grundkapitalVermindertStueck[2 - 1]);
        hProzentString = CaString.prozentToString(hProzentAktienGattung1);
        rpVariablen.fuelleVariable(rpDrucken, "ProzentAktienG1", hProzentString);
        if (gattung == 1) {
            rpVariablen.fuelleVariable(rpDrucken, "ProzentAktienGESAMT", hProzentString);
        }

        hProzentString = CaString.prozentToString(hProzentAktienGattung2);
        rpVariablen.fuelleVariable(rpDrucken, "ProzentAktienG2", hProzentString);
        if (gattung == 2) {
            rpVariablen.fuelleVariable(rpDrucken, "ProzentAktienGESAMT", hProzentString);
        }

        rpVariablen.fuelleVariable(rpDrucken, "VertreteneStimmenG3", CaString.toStringDE(stimmenGattung3));
        rpVariablen.fuelleVariable(rpDrucken, "VertreteneAktienG3", CaString.toStringDE(aktienGattung3));
        rpVariablen.fuelleVariable(rpDrucken, "VertreteneStimmenAktienG3",
                CaString.toStringDE(stimmenGattung3 + aktienGattung3));

        /*Online*/
        rpVariablen.fuelleVariable(rpDrucken, "AnwesendeVertretenMitgliederOnline",
                CaString.toStringDE(anzVertretenOnline));
        rpVariablen.fuelleVariable(rpDrucken, "VertreteneStimmenOnline", CaString.toStringDE(stimmenGesamtOnline));

        rpVariablen.fuelleVariable(rpDrucken, "AnwesendeVertretenMitgliederG1Online",
                CaString.toStringDE(anzVertretenGattung1Online));
        rpVariablen.fuelleVariable(rpDrucken, "AnwesendeVertretenMitgliederG2Online",
                CaString.toStringDE(anzVertretenGattung2Online));
        rpVariablen.fuelleVariable(rpDrucken, "AnwesendeVertretenMitgliederG3Online",
                CaString.toStringDE(anzVertretenGattung3Online));
        rpVariablen.fuelleVariable(rpDrucken, "AnwesendeVertretenMitgliederG1_3Online",
                CaString.toStringDE(anzVertretenGattung1Online + anzVertretenGattung3Online));

        rpVariablen.fuelleVariable(rpDrucken, "VertreteneStimmenG1Online", CaString.toStringDE(stimmenGattung1Online));
        rpVariablen.fuelleVariable(rpDrucken, "VertreteneStimmenG2Online", CaString.toStringDE(stimmenGattung2Online));
        rpVariablen.fuelleVariable(rpDrucken, "VertreteneStimmenG3Online", CaString.toStringDE(stimmenGattung3Online));

        /*Physikalisch*/
        rpVariablen.fuelleVariable(rpDrucken, "AnwesendeVertretenMitgliederPhysikalisch",
                CaString.toStringDE(anzVertretenPhysikalisch));
        rpVariablen.fuelleVariable(rpDrucken, "VertreteneStimmenPhysikalisch",
                CaString.toStringDE(stimmenGesamtPhysikalisch));

        rpVariablen.fuelleVariable(rpDrucken, "AnwesendeVertretenMitgliederG1Physikalisch",
                CaString.toStringDE(anzVertretenGattung1Physikalisch));
        rpVariablen.fuelleVariable(rpDrucken, "AnwesendeVertretenMitgliederG2Physikalisch",
                CaString.toStringDE(anzVertretenGattung2Physikalisch));
        rpVariablen.fuelleVariable(rpDrucken, "AnwesendeVertretenMitgliederG3Physikalisch",
                CaString.toStringDE(anzVertretenGattung3Physikalisch));
        rpVariablen.fuelleVariable(rpDrucken, "AnwesendeVertretenMitgliederG1_3Physikalisch",
                CaString.toStringDE(anzVertretenGattung1Physikalisch + anzVertretenGattung3Physikalisch));

        rpVariablen.fuelleVariable(rpDrucken, "VertreteneStimmenG1Physikalisch",
                CaString.toStringDE(stimmenGattung1Physikalisch));
        rpVariablen.fuelleVariable(rpDrucken, "VertreteneStimmenG2Physikalisch",
                CaString.toStringDE(stimmenGattung2Physikalisch));
        rpVariablen.fuelleVariable(rpDrucken, "VertreteneStimmenG3Physikalisch",
                CaString.toStringDE(stimmenGattung3Physikalisch));

        rpDrucken.druckenFormular();
        rpDrucken.endeFormular();

        lDbBundle.closeAll();

        /*********************** Liste ****************************/
        /*FeststellungVereinListe01.lst*/
        rpDrucken = new RpDrucken();
        rpDrucken.initClientDrucke();

        rpDrucken.initListe(lDbBundle);

        rpVariablen = new RpVariablen(lDbBundle);
        rpVariablen.feststellungVereinliste("01", rpDrucken);

        rpVariablen.fuelleVariable(rpDrucken, "Ueberschrift.Datum", hDatum);
        rpVariablen.fuelleVariable(rpDrucken, "Ueberschrift.Zeit", hZeit);
        rpVariablen.fuelleVariable(rpDrucken, "Ueberschrift.GattungDruck", Integer.toString(gattung));

        /*boolean brc=*/rpDrucken.startListe();//if (brc==false){return false;}

        for (int i = 0; i < anzVertreten; i++) {
            EclMeldung lMeldung = lDbBundle.dbMeldungen.meldungenArray[i];
            String mitgliedsNr = lMeldung.aktionaersnummer;
            mitgliedsNr = BlNummernformBasis.aufbereitenInternFuerExtern(mitgliedsNr, lDbBundle);
            if (ParamSpezial.ku310(lDbBundle.clGlobalVar.mandant) == true) {
                if (lMeldung.aktionaersnummer.length() >= 8) {
                    String vorne = lMeldung.aktionaersnummer.substring(0, 7);
                    String hinten = lMeldung.aktionaersnummer.substring(7);
                    mitgliedsNr = vorne + "-" + hinten;

                }

            }
            rpVariablen.fuelleFeld(rpDrucken, "Aktionaer.LfdNr", Integer.toString(i + 1));
            rpVariablen.fuelleFeld(rpDrucken, "Aktionaer.Nr", mitgliedsNr);
            rpVariablen.fuelleFeld(rpDrucken, "Aktionaer.Name", lMeldung.name);
            rpVariablen.fuelleFeld(rpDrucken, "Aktionaer.Vorname", lMeldung.vorname);
            rpVariablen.fuelleFeld(rpDrucken, "Aktionaer.Ort", lMeldung.ort);
            rpVariablen.fuelleFeld(rpDrucken, "Vertreter.Name", lMeldung.vertreterName);
            rpVariablen.fuelleFeld(rpDrucken, "Vertreter.Vorname", lMeldung.vertreterVorname);
            rpVariablen.fuelleFeld(rpDrucken, "Vertreter.Ort", lMeldung.vertreterOrt);
            rpVariablen.fuelleFeld(rpDrucken, "Besitz.Stimmen", Long.toString(lMeldung.stimmen));
            rpVariablen.fuelleFeld(rpDrucken, "Besitz.StimmenDE", CaString.toStringDE(lMeldung.stimmen));
            rpVariablen.fuelleFeld(rpDrucken, "Besitz.Aktien", Long.toString(lMeldung.stueckAktien));
            rpVariablen.fuelleFeld(rpDrucken, "Besitz.AktienDE", CaString.toStringDE(lMeldung.stueckAktien));
            rpVariablen.fuelleFeld(rpDrucken, "Besitz.Gattung", Integer.toString(lMeldung.liefereGattung()));
            rpVariablen.fuelleFeld(rpDrucken, "Besitz.Besitz", lMeldung.besitzart);

            rpVariablen.fuelleFeld(rpDrucken, "Besitz.ku310BesitzAG", "");
            if (lMeldung.besitzart.equals("1") || lMeldung.besitzart.equals("3")) {
                rpVariablen.fuelleFeld(rpDrucken, "Besitz.ku310BesitzAG", "V");
            }
            rpVariablen.fuelleFeld(rpDrucken, "Besitz.ku310BesitzAN", "");
            if (lMeldung.besitzart.equals("2") || lMeldung.besitzart.equals("3")) {
                rpVariablen.fuelleFeld(rpDrucken, "Besitz.ku310BesitzAN", "V");
            }

            rpVariablen.fuelleFeld(rpDrucken, "Besitz.PraesenzArt", lMeldung.zusatzfeld4);
            rpDrucken.druckenListe();

        }

        rpDrucken.endeListe();

    }

    /**
     * Clicked btn abbrechen.
     *
     * @param event the event
     */
    @FXML
    void clickedBtnAbbrechen(ActionEvent event) {
        eigeneStage.close();
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
