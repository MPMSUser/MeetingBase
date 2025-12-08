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

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaDateiWrite;
import de.meetingapps.meetingportal.meetComAllg.CaDatumZeit;
import de.meetingapps.meetingportal.meetComAllg.CaString;
import de.meetingapps.meetingportal.meetComBVerwaltung.BvDatenbank;
import de.meetingapps.meetingportal.meetComBl.BlNummernformen;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclAktienregister;
import de.meetingapps.meetingportal.meetComEntities.EclMeldung;
import de.meetingapps.meetingportal.meetComEntities.EclScan;
import de.meetingapps.meetingportal.meetComEntities.EclWeisungMeldung;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * The Class CtrlAdminDiv.
 */
public class CtrlAdminDiv {

    /** The resources. */
    @FXML
    private ResourceBundle resources;

    /** The location. */
    @FXML
    private URL location;

    /** The btn tbl portal texte global. */
    @FXML
    private Button btnTblPortalTexteGlobal;

    /** The btn tbl portal texte mandant. */
    @FXML
    private Button btnTblPortalTexteMandant;

    /** The btn tbl parameter lang. */
    @FXML
    private Button btnTblParameterLang;

    /** The btn tbl termine. */
    @FXML
    private Button btnTblTermine;

    /** The btn tbl aufgaben. */
    @FXML
    private Button btnTblAufgaben;

    /** The btn tbl verarbeitungslauf verarbeitungspotokoll. */
    @FXML
    private Button btnTblVerarbeitungslaufVerarbeitungspotokoll;

    /** The btn tbl drucklauf. */
    @FXML
    private Button btnTblDrucklauf;

    /** The btn tbl konfig verarbeitung. */
    @FXML
    private Button btnTblKonfigVerarbeitung;

    /** The btn tbl insti prov. */
    @FXML
    private Button btnTblInstiProv;

    /** The btn spezial. */
    @FXML
    private Button btnSpezial;

    /** The btn tbl ip tracking. */
    @FXML
    private Button btnTblIpTracking;

    /** The btn tbl best workflow. */
    @FXML
    private Button btnTblBestWorkflow;

    /** The btn tbl aktienregister mail ruecklauf. */
    @FXML
    private Button btnTblAktienregisterMailRuecklauf;

    /** The btn tbl H table. */
    @FXML
    private Button btnTblHTable;

    /** The tf H table. */
    @FXML
    private TextField tfHTable;

    /** The btn tbl name nr meldebestand. */
    @FXML
    private Button btnTblNameNrMeldebestand;

    /**
     * On btn tblh table.
     *
     * @param event the event
     */
    @FXML
    void onBtnTblhTable(ActionEvent event) {

        int nachversandNummer = Integer.parseInt(tfHTable.getText());

        lDbBundle = new DbBundle();
        lDbBundle.openAll();
        lDbBundle.openWeitere();

        lDbBundle.dbHTable.readAll();
        if (lDbBundle.dbHTable.anzErgebnis() > 0) {
            for (int i = 0; i < lDbBundle.dbHTable.anzErgebnis(); i++) {
                String nummer = lDbBundle.dbHTable.ergebnisPosition(i);

                BlNummernformen blNummernformen = new BlNummernformen(lDbBundle);
                nummer = blNummernformen.aktienregisterNraufbereitenFuerIntern(nummer);

                System.out.println("nummer=" + nummer);

                EclAktienregister lAktienregisterEintrag = new EclAktienregister();
                lAktienregisterEintrag.aktionaersnummer = nummer;
                lDbBundle.dbAktienregister.leseZuAktienregisterEintrag(lAktienregisterEintrag);

                lAktienregisterEintrag = lDbBundle.dbAktienregister.ergebnisPosition(0);
                lAktienregisterEintrag.versandNummer = nachversandNummer;
                lDbBundle.dbAktienregister.update(lAktienregisterEintrag);

            }
        }

        lDbBundle.closeAll();
    }

    /**
     * Initialize.
     */
    @FXML
    void initialize() {
        assert btnTblPortalTexteGlobal != null : "fx:id=\"btnTblPortalTexteGlobal\" was not injected: check your FXML file 'AdminDiv.fxml'.";
        assert btnTblPortalTexteMandant != null : "fx:id=\"btnTblPortalTexteMandant\" was not injected: check your FXML file 'AdminDiv.fxml'.";
        assert btnTblParameterLang != null : "fx:id=\"btnTblParameterLang\" was not injected: check your FXML file 'AdminDiv.fxml'.";
        assert btnTblTermine != null : "fx:id=\"btnTblTermine\" was not injected: check your FXML file 'AdminDiv.fxml'.";

    }

    /** The l db bundle. */
    DbBundle lDbBundle = null;

    /**
     * Verarbeiten spezial.
     *
     * @param ident the ident
     */
    private void verarbeitenSpezial(int ident) {
        int erg = lDbBundle.dbScan.read(ident);
        if (erg > 0) {
            EclScan lScan = lDbBundle.dbScan.ergebnisArray[0];
            if (lScan.verarbeitet > 0) {
                if (lScan.istBriefwahl == 1 || lScan.istSRV == 1) {
                    /*Aktionärsnummer bestimmen*/
                    String aktionaersnummer = lScan.barcode;

                    /*aktienregisterIdent holen*/
                    EclAktienregister lAktienregister = new EclAktienregister();
                    lAktienregister.aktionaersnummer = aktionaersnummer;
                    lDbBundle.dbAktienregister.leseZuAktienregisterEintrag(lAktienregister);
                    lAktienregister = lDbBundle.dbAktienregister.ergebnisArray[0];
                    if (lAktienregister.stueckAktien == 0) {
                        System.out.println(aktionaersnummer + " 0 Bestand! " + lScan.verarbeitet);
                        return;
                    }

                    EclMeldung pMeldung = new EclMeldung();
                    pMeldung.aktionaersnummer = aktionaersnummer;

                    int rc = lDbBundle.dbMeldungen.leseZuAktionaersnummer(pMeldung);
                    if (rc != 1) {
                        System.out.println(aktionaersnummer + " ungleich als 1 Meldung!" + lScan.verarbeitet);
                        return;
                    }

                    pMeldung = lDbBundle.dbMeldungen.meldungenArray[0];
                    int meldeIdent = pMeldung.meldungsIdent;

                    int rc1 = lDbBundle.dbWeisungMeldung.leseZuMeldungsIdentNurAktive(meldeIdent, false);
                    if (rc1 != 1) {
                        System.out.println(aktionaersnummer + " zu viele Weisungen " + lScan.verarbeitet);
                        return;
                    }

                    EclWeisungMeldung lWeisungMeldung = lDbBundle.dbWeisungMeldung.weisungMeldungGefunden(0);
                    lWeisungMeldung.abgabe[1] = Integer.parseInt(lScan.pos[5]);
                    lWeisungMeldung.abgabe[2] = Integer.parseInt(lScan.pos[6]);
                    lWeisungMeldung.abgabe[3] = Integer.parseInt(lScan.pos[7]);
                    lWeisungMeldung.abgabe[4] = Integer.parseInt(lScan.pos[8]);

                    lDbBundle.dbWeisungMeldung.update(lWeisungMeldung, null, false);

                }
            }

        }
    }

    /**
     * On btn spezial.
     *
     * @param event the event
     */
    @FXML
    void onBtnSpezial(ActionEvent event) {
        lDbBundle = new DbBundle();
        lDbBundle.openAll();

        for (int ident = 4753; ident <= 6286; ident++) {
            verarbeitenSpezial(ident);
        }

        lDbBundle.closeAll();
        System.out.println("Fertig");

    }

    /**
     * On btn tbl konfig verarbeitung.
     *
     * @param event the event
     */
    @FXML
    void onBtnTblKonfigVerarbeitung(ActionEvent event) {
        DbBundle lDbBundle = new DbBundle();
        lDbBundle.openAll();
        lDbBundle.dbKonfigAuswertung.createTable();
        lDbBundle.closeAll();
    }

    /**
     * On btn tbl insti prov.
     *
     * @param event the event
     */
    @FXML
    void onBtnTblInstiProv(ActionEvent event) {
        DbBundle lDbBundle = new DbBundle();
        lDbBundle.openAll();
        lDbBundle.dbInstiProv.createTable();
        lDbBundle.closeAll();
    }

    /**
     * On btn tbl portal texte global.
     *
     * @param event the event
     */
    @FXML
    void onBtnTblPortalTexteGlobal(ActionEvent event) {
        DbBundle lDbBundle = new DbBundle();
        lDbBundle.openAll();
        lDbBundle.dbPortalTexte.createTable(false);
        lDbBundle.closeAll();
    }

    /**
     * On btn tbl portal texte mandant.
     *
     * @param event the event
     */
    @FXML
    void onBtnTblPortalTexteMandant(ActionEvent event) {
        DbBundle lDbBundle = new DbBundle();
        lDbBundle.openAll();
        lDbBundle.dbPortalTexte.createTable(true);
        lDbBundle.closeAll();
    }

    /**
     * On btn tbl parameter lang.
     *
     * @param event the event
     */
    @FXML
    void onBtnTblParameterLang(ActionEvent event) {
        DbBundle lDbBundle = new DbBundle();
        lDbBundle.openAll();
        lDbBundle.dbParameter.createTable_parameterLang();
        lDbBundle.closeAll();
    }

    /**
     * On btn tbl termine.
     *
     * @param event the event
     */
    @FXML
    void onBtnTblTermine(ActionEvent event) {
        DbBundle lDbBundle = new DbBundle();
        lDbBundle.openAll();
        lDbBundle.dbTermine.createTable();
        BvDatenbank lBvDatenbank = new BvDatenbank(lDbBundle);
        lBvDatenbank.initialisiereTermine(1);
        lDbBundle.closeAll();
    }

    /**
     * On btn tbl aufgaben.
     *
     * @param event the event
     */
    @FXML
    void onBtnTblAufgaben(ActionEvent event) {
        DbBundle lDbBundle = new DbBundle();
        lDbBundle.openAll();
        lDbBundle.openWeitere();
        lDbBundle.dbAufgaben.createTable();
        lDbBundle.closeAll();
    }

    /**
     * On btn tbl drucklauf.
     *
     * @param event the event
     */
    @FXML
    void onBtnTblDrucklauf(ActionEvent event) {
        DbBundle lDbBundle = new DbBundle();
        lDbBundle.openAll();
        lDbBundle.openWeitere();
        lDbBundle.dbDrucklauf.createTable();
        lDbBundle.closeAll();
    }

    /**
     * On btn verarbeitungslauf verarbeitungspotokoll.
     *
     * @param event the event
     */
    @FXML
    void onBtnVerarbeitungslaufVerarbeitungspotokoll(ActionEvent event) {
        DbBundle lDbBundle = new DbBundle();
        lDbBundle.openAll();
        lDbBundle.openWeitere();
        lDbBundle.dbVerarbeitungsLauf.createTable();
        lDbBundle.dbVerarbeitungsProtokoll.createTable();
        lDbBundle.closeAll();
    }

    /**
     * On btn tbl ip tracking.
     *
     * @param event the event
     */
    @FXML
    void onBtnTblIpTracking(ActionEvent event) {
        DbBundle lDbBundle = new DbBundle();
        lDbBundle.openAll();
        lDbBundle.openWeitere();
        lDbBundle.dbIpTracking.createTable();
        lDbBundle.closeAll();
    }

    /**
     * On btn tbl best workflow.
     *
     * @param event the event
     */
    @FXML
    void onBtnTblBestWorkflow(ActionEvent event) {
        DbBundle lDbBundle = new DbBundle();
        lDbBundle.openAll();
        lDbBundle.openWeitere();
        lDbBundle.dbBestWorkflow.createTable();
        lDbBundle.closeAll();
    }

    /**
     * On btn tbl aktienregister mail ruecklauf.
     *
     * @param event the event
     */
    @FXML
    void onBtnTblAktienregisterMailRuecklauf(ActionEvent event) {
        DbBundle lDbBundle = new DbBundle();
        lDbBundle.openAll();
        lDbBundle.openWeitere();
        lDbBundle.dbAktienregisterMailRuecklauf.createTable();
        lDbBundle.closeAll();
    }

    /**
     * 1.) Falls nachname im Meldebestand nicht gefüllt, dann übertragen aus Aktienregister
     * 2.) Besitzart aus Aktienregister füllen
     * 3.) Mitgliedsnummer vor Name in Meldung setzen
     * 
     * Darf erst laufen, wenn Bestand ansonsten ziemlich fertig ist, d.h.:
     * > Mitglieds-Liste aktualisiert wurde
     * > EK-Vergabe durchgeführt wurde
     */
    @FXML
    void onBtnTblNameNrMeldebestand(ActionEvent event) {

        DbBundle lDbBundle = new DbBundle();
        lDbBundle.openAll();

        //    	 if (1==1){
        //    		 lDbBundle.dbWeisungMeldung.updateFreiWeisung();
        //    		 return;
        //    	 }
        //    	 

        CaDateiWrite caDateiWrite = new CaDateiWrite();
        caDateiWrite.oeffne(lDbBundle, "stornoBriefwahlF");
        System.out.println("Lese Meldungen Start");
        lDbBundle.dbMeldungen.leseAlleMeldungen();
        System.out.println("Lese Meldungen Ende");

        EclMeldung[] meldungArray = lDbBundle.dbMeldungen.meldungenArray;

        for (int i = 0; i < meldungArray.length; i++) {
            if ((i) % 200 == 0) {
                System.out.println(i + "Import " + CaDatumZeit.DatumZeitStringFuerDatenbank());
            }
            EclMeldung lMeldung = meldungArray[i];
            if (lMeldung.meldungstyp != 2) {

                EclAktienregister lAktienregister = new EclAktienregister();
                lAktienregister.aktionaersnummer = lMeldung.aktionaersnummer;
                lDbBundle.dbAktienregister.leseZuAktienregisterEintrag(lAktienregister);
                if (lDbBundle.dbAktienregister.anzErgebnis() != 1) {
                    CaBug.drucke("CtrlAdminDiv.onBtnTblNameNrMeldebestand 001");
                }
                lAktienregister = lDbBundle.dbAktienregister.ergebnisPosition(0);
                /*1.)*/
                if (lMeldung.name.isEmpty()) {
                    /*Name aus Aktienregister füllen*/
                    lMeldung.name = lAktienregister.name1;
                    lMeldung.kurzName = lMeldung.name;
                }
                /*2.)*/
                String mitgliedsNrExtern = CaString.ku178InternZuEingabe(lMeldung.aktionaersnummer);
                if (!lMeldung.name.startsWith(mitgliedsNrExtern)) {
                    lMeldung.name = mitgliedsNrExtern + " " + lMeldung.name;
                    lMeldung.name = CaString.trunc(lMeldung.name, 80);
                }

                //    			 /*3.)*/
                //    			 lMeldung.besitzart=lAktienregister.besitzart;
                //    			 
                lDbBundle.dbMeldungen.updateNurPersonenNatJur(lMeldung);
                //    			 
                //    			 /*4.) Falls Besitzart==F - Briefwahl stornieren*/
                //    			 if (lMeldung.besitzart.equals("F") && lMeldung.meldungstyp==3){
                //    				 caDateiWrite.ausgabe("Storno "+lMeldung.aktionaersnummer);
                //    				 caDateiWrite.newline();
                //    				 int meldeIdent=lMeldung.meldungsIdent;
                //    				 lDbBundle.dbMeldungen.leseZuIdent(meldeIdent);
                //    				 EclMeldung lMeldungUpdate=lDbBundle.dbMeldungen.meldungenArray[0];
                //
                //    				 int sammelIdent=lMeldungUpdate.meldungEnthaltenInSammelkarte;
                //    				 int sammelArt=lMeldungUpdate.meldungEnthaltenInSammelkarteArt;
                //
                //    				 BlWillenserklaerung lWillenserklaerung=new BlWillenserklaerung();
                //    				 lWillenserklaerung.piEclMeldungAktionaer=lMeldungUpdate;
                //    				 lWillenserklaerung.piMeldungsIdentAktionaer=lMeldungUpdate.meldungsIdent;
                //    				 lWillenserklaerung.pAufnehmendeSammelkarteIdent=sammelIdent;
                //    				 if (sammelArt==4){
                //    					 lWillenserklaerung.widerrufBriefwahl(lDbBundle);
                //    				 }
                //    				 else{
                //    					 caDateiWrite.ausgabe(lMeldung.aktionaersnummer+" onBtnTblNameNrMeldebestand.storniereAusSammelkarten 001");
                //    					 caDateiWrite.newline();
                //    				 }
                //

                //    			 }
            }
        }
        caDateiWrite.schliessen();
        System.out.println("Done");
        lDbBundle.closeAll();
    }

    //	private Stage eigeneStage;

    /**
     * Inits the.
     *
     * @param pEigeneStage the eigene stage
     */
    public void init(Stage pEigeneStage) {
        //		eigeneStage=pEigeneStage;

    }

}
