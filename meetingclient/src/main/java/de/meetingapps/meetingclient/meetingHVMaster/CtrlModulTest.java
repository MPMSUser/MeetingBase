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

import de.meetingapps.meetingportal.meetComAllg.CaDatumZeit;
import de.meetingapps.meetingportal.meetComAllg.CaString;
import de.meetingapps.meetingportal.meetComBVerwaltung.BvDatenbank;
import de.meetingapps.meetingportal.meetComBVerwaltung.BvMandanten;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclEmittenten;
import de.meetingapps.meetingportal.meetComEntities.EclFragenExtern;
import de.meetingapps.meetingportal.meetComHVParam.ParamS;
import de.meetingapps.meetingportal.meetComStub.WSClient;
import de.meetingapps.meetingportal.meetComStub.WSClientExtern;
import de.meetingapps.meetingportal.meetComWE.WEFragenResponse;
import de.meetingapps.meetingportal.meetingVote.EclStimmabgabe;
import de.meetingapps.meetingportal.meetingVote.WEStimmabgabe;
import de.meetingapps.meetingportal.meetingVote.WEStimmabgabeRC;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * The Class CtrlModulTest.
 */
public class CtrlModulTest {

    /** The resources. */
    @FXML
    private ResourceBundle resources;

    /** The location. */
    @FXML
    private URL location;

    /** The btn funktion 1. */
    @FXML
    private Button btnFunktion1;

    /** The tf ausgabe 1. */
    @FXML
    private TextArea tfAusgabe1;

    /** The tf eingabe 1. */
    @FXML
    private TextField tfEingabe1;

    /** The btn funktion 2. */
    @FXML
    private Button btnFunktion2;

    /** The tf ausgabe 2. */
    @FXML
    private TextArea tfAusgabe2;

    /** The tf eingabe 2. */
    @FXML
    private TextField tfEingabe2;

    /** The btn funktion 3. */
    @FXML
    private Button btnFunktion3;

    /** The tf ausgabe 3. */
    @FXML
    private TextArea tfAusgabe3;

    /** The tf eingabe 3. */
    @FXML
    private TextField tfEingabe3;

    /** The btn funktion 4. */
    @FXML
    private Button btnFunktion4;

    /** The tf ausgabe 4. */
    @FXML
    private TextArea tfAusgabe4;

    /** The tf eingabe 4. */
    @FXML
    private TextField tfEingabe4;

    /**
     * Initialize.
     */
    @FXML
    void initialize() {
        assert btnFunktion1 != null : "fx:id=\"btnFunktion1\" was not injected: check your FXML file 'ModulTest.fxml'.";
        assert tfAusgabe1 != null : "fx:id=\"tfAusgabe1\" was not injected: check your FXML file 'ModulTest.fxml'.";
        assert tfEingabe1 != null : "fx:id=\"tfEingabe1\" was not injected: check your FXML file 'ModulTest.fxml'.";
        assert btnFunktion2 != null : "fx:id=\"btnFunktion2\" was not injected: check your FXML file 'ModulTest.fxml'.";
        assert tfAusgabe2 != null : "fx:id=\"tfAusgabe2\" was not injected: check your FXML file 'ModulTest.fxml'.";
        assert tfEingabe2 != null : "fx:id=\"tfEingabe2\" was not injected: check your FXML file 'ModulTest.fxml'.";
        assert btnFunktion3 != null : "fx:id=\"btnFunktion3\" was not injected: check your FXML file 'ModulTest.fxml'.";
        assert tfAusgabe3 != null : "fx:id=\"tfAusgabe3\" was not injected: check your FXML file 'ModulTest.fxml'.";
        assert tfEingabe3 != null : "fx:id=\"tfEingabe3\" was not injected: check your FXML file 'ModulTest.fxml'.";
        assert btnFunktion4 != null : "fx:id=\"btnFunktion4\" was not injected: check your FXML file 'ModulTest.fxml'.";
        assert tfAusgabe4 != null : "fx:id=\"tfAusgabe4\" was not injected: check your FXML file 'ModulTest.fxml'.";
        assert tfEingabe4 != null : "fx:id=\"tfEingabe4\" was not injected: check your FXML file 'ModulTest.fxml'.";

    }

    /**
     * On btn funktion 1.
     *
     * @param event the event
     */
    @FXML
    void onBtnFunktion1(ActionEvent event) {

        testCaString("", "10", 2, 4);
        testCaString("1", "10", 2, 4);
        testCaString("12","10", 2, 4);
        testCaString("123","10", 2, 4);
        testCaString("1234","10", 2, 4);
        testCaString("12345","10", 2, 4);
        testCaString("123456","10", 2, 4);
        
    }

    
    private void testCaString(String ursprung, String ueberschreiben, int von, int bis) { 
        System.out.println("ursprung=<"+ursprung+"> ueberschreiben=<"+ueberschreiben+"> von="+von+" Enthalten=<"+CaString.substringMitFehlerbehandlung(ursprung, von, bis)+"> Neu=<"+CaString.stringUeberschreibeTeilMitFehlerbehandlung(ursprung, ueberschreiben, von)+">");
    }
    
    /**
     * On btn funktion 2.
     *
     * @param event the event
     */
    @FXML
    void onBtnFunktion2(ActionEvent event) {
        DbBundle lDbBundle = new DbBundle();
        lDbBundle.openAll();

        EclEmittenten lEmittenten = new EclEmittenten();
        lEmittenten.mandant = ParamS.clGlobalVar.mandant;
        lEmittenten.hvJahr = 2018;
        lEmittenten.hvNummer = "A";
        lEmittenten.dbArt = "P";
        BvMandanten lBvMandanten = new BvMandanten();
        lBvMandanten.legeMandantNeuAn(lDbBundle, lEmittenten, true);

        //       String ausgabe="";
        //       tfAusgabe2.setText("");
        //       DbBundle lDbBundle=new DbBundle();
        //       lDbBundle.openAll();
        //       
        //           lDbBundle.closeAll();

    }

    /**
     * On btn funktion 3.
     *
     * @param event the event
     */
    @FXML
    void onBtnFunktion3(ActionEvent event) {
        tfAusgabe3.setText("");
        DbBundle lDbBundle = new DbBundle();
        lDbBundle.openAll();
        BvDatenbank bvDatenbank = new BvDatenbank(lDbBundle);

        bvDatenbank.testDrop();
        tfAusgabe3.setText("Drop fertig");
        lDbBundle.closeAll();

    }

    /**
     * On btn funktion 4.
     *
     * @param event the event
     */
    @FXML
    void onBtnFunktion4(ActionEvent event) {
        tfAusgabe4.setText("");
        System.out.println("Start " + CaDatumZeit.DatumZeitStringFuerAnzeigeNeu());

        //        testFunktion4StimmabgabenDurchfuehren();

        //        testFunktion4fragenExtern();

        testOss();

        tfAusgabe4.setText("Ok");
        System.out.println("Ende " + CaDatumZeit.DatumZeitStringFuerAnzeigeNeu());

    }

    /**
     * Test funktion 4 fragen extern.
     */
    public void testFunktion4fragenExtern() {
        WSClientExtern wsClientExtern = new WSClientExtern();

        WEFragenResponse weFragenResponse = wsClientExtern.getFragen();
        System.out.println("weFragenResponse.statusCode" + weFragenResponse.statusCode);
        System.out.println("weFragenResponse.responseText" + weFragenResponse.responseText);

        EclFragenExtern[] fragenarray = weFragenResponse.fragenArray;
        if (fragenarray != null) {
            for (int i = 0; i < fragenarray.length; i++) {
                System.out.println("i=" + i);
            }
        }

        weFragenResponse.fragenArray = fragenarray;
    }

    /**
     * Test funktion 4 stimmabgaben durchfuehren.
     */
    public void testFunktion4StimmabgabenDurchfuehren() {
        for (int i = 1; i <= 1000; i++) {
            if (i % 100 == 0) {
                System.out.println("Kennungen angelegt " + Integer.toString(i));
            }
            WSClient wsClient = new WSClient();
            EclStimmabgabe lStimmabgabe = new EclStimmabgabe();
            lStimmabgabe.kennung = CaString.fuelleLinksNull(Integer.toString(i), 40);
            lStimmabgabe.passwort = CaString.fuelleLinksNull(Integer.toString(i), 40);
            lStimmabgabe.abgabeArt = 1;
            lStimmabgabe.abstimmungsnummer = 1;
            lStimmabgabe.gelesenerStimmzettelnummer = 1;
            WEStimmabgabe weStimmabgabe = new WEStimmabgabe();
            weStimmabgabe.eclStimmabgabe = lStimmabgabe;
            WEStimmabgabeRC weStimmabgabeRC = wsClient.stimmabgabe(weStimmabgabe);
            if (weStimmabgabeRC.rc != 0) {
                System.out.println("Fehler=" + weStimmabgabeRC.rc);
            }
        }

    }

    /**
     * Test oss.
     */
    public void testOss() {
        System.out.println("Test OSS");
        for (int i = 0; i < 50; i++) {
            WSClient wsClient = new WSClient();
            String rcString = wsClient.osstest(Integer.toString(i));
            System.out.println("i=" + i + " rcString=" + rcString);
            wsClient = new WSClient();
            rcString = wsClient.osstest1(Integer.toString(i));
            wsClient = new WSClient();
            rcString = wsClient.osstest2(Integer.toString(i));
            wsClient = new WSClient();
            rcString = wsClient.osstest3(Integer.toString(i));
            //           try {
            //                Thread.sleep(5000);
            //            } catch (InterruptedException e) {
            //                e.printStackTrace();
            //            }
        }
    }
    //    private Stage eigeneStage;

    /**
     * Inits the.
     *
     * @param pEigeneStage the eigene stage
     */
    public void init(Stage pEigeneStage) {
        //        eigeneStage = pEigeneStage;

    }
}
