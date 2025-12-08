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
package de.meetingapps.meetingclient.meetingFrontOffice;

import java.net.URL;
import java.util.ResourceBundle;

import de.meetingapps.meetingclient.meetingClientDialoge.CaZeigeHinweis;
import de.meetingapps.meetingportal.meetComAllg.CaString;
import de.meetingapps.meetingportal.meetComKonst.KonstStimmart;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * The Class CtrlWechseln.
 */
public class CtrlStimmabgabe {
    
    public final int width = 500;
    public final int height = 300;


    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btn0;

    @FXML
    private Button btn1;

    @FXML
    private Button btn2;

    @FXML
    private Button btn3;

    @FXML
    private Button btn4;

    @FXML
    private Button btn5;

    @FXML
    private Button btn6;

    @FXML
    private Button btn7;

    @FXML
    private Button btn8;

    @FXML
    private Button btn9;

    @FXML
    private Button btnAbbrechen;

    @FXML
    private Button btnAlles;

    @FXML
    private Button btnLoeschen;

    @FXML
    private Button btnStimmenUebernehmen;

    @FXML
    private Label lblAGAN;

    @FXML
    private Label lblStimmen;

    @FXML
    private Label lblStimmenAnzahl;

    @FXML
    private Label lblStimmenGesamtAnzahl;

    @FXML
    private Label lblTOP;

    @FXML
    private Label lblTOPText;

    @FXML
    void onBtn0(ActionEvent event) {
        ergaenzeZiffer("0");

    }

    @FXML
    void onBtn1(ActionEvent event) {
        ergaenzeZiffer("1");

    }

    @FXML
    void onBtn2(ActionEvent event) {
        ergaenzeZiffer("2");

    }

    @FXML
    void onBtn3(ActionEvent event) {
        ergaenzeZiffer("3");

    }

    @FXML
    void onBtn4(ActionEvent event) {
        ergaenzeZiffer("4");

    }

    @FXML
    void onBtn5(ActionEvent event) {
        ergaenzeZiffer("5");

    }

    @FXML
    void onBtn6(ActionEvent event) {
        ergaenzeZiffer("6");

    }

    @FXML
    void onBtn7(ActionEvent event) {
        ergaenzeZiffer("7");

    }

    @FXML
    void onBtn8(ActionEvent event) {
        ergaenzeZiffer("8");

    }

    @FXML
    void onBtn9(ActionEvent event) {
        ergaenzeZiffer("9");
    }

 
    @FXML
    void onBtnAlles(ActionEvent event) {
        gStimmenErgebnis=gStimmenGesamt;
        anzeigenStimmenErgebnis();
    }

    @FXML
    void onBtnLoeschen(ActionEvent event) {
        gStimmenErgebnis="0";
        anzeigenStimmenErgebnis();
    }

    void ergaenzeZiffer(String pZiffer) {
        if (gStimmenErgebnis.isEmpty() || gStimmenErgebnis.equals("0")) {
            gStimmenErgebnis=pZiffer;
            anzeigenStimmenErgebnis();
            return;
        }
        
        gStimmenErgebnis+=pZiffer;
        anzeigenStimmenErgebnis();
    }
    

    /**
     * Initialize.
     */
    @FXML
    void initialize() {
        assert btn0 != null : "fx:id=\"btn0\" was not injected: check your FXML file 'Stimmabgabe.fxml'.";
        assert btn1 != null : "fx:id=\"btn1\" was not injected: check your FXML file 'Stimmabgabe.fxml'.";
        assert btn2 != null : "fx:id=\"btn2\" was not injected: check your FXML file 'Stimmabgabe.fxml'.";
        assert btn3 != null : "fx:id=\"btn3\" was not injected: check your FXML file 'Stimmabgabe.fxml'.";
        assert btn4 != null : "fx:id=\"btn4\" was not injected: check your FXML file 'Stimmabgabe.fxml'.";
        assert btn5 != null : "fx:id=\"btn5\" was not injected: check your FXML file 'Stimmabgabe.fxml'.";
        assert btn6 != null : "fx:id=\"btn6\" was not injected: check your FXML file 'Stimmabgabe.fxml'.";
        assert btn7 != null : "fx:id=\"btn7\" was not injected: check your FXML file 'Stimmabgabe.fxml'.";
        assert btn8 != null : "fx:id=\"btn8\" was not injected: check your FXML file 'Stimmabgabe.fxml'.";
        assert btn9 != null : "fx:id=\"btn9\" was not injected: check your FXML file 'Stimmabgabe.fxml'.";
        assert btnAbbrechen != null : "fx:id=\"btnAbbrechen\" was not injected: check your FXML file 'Stimmabgabe.fxml'.";
        assert btnAlles != null : "fx:id=\"btnAlles\" was not injected: check your FXML file 'Stimmabgabe.fxml'.";
        assert btnLoeschen != null : "fx:id=\"btnLoeschen\" was not injected: check your FXML file 'Stimmabgabe.fxml'.";
        assert btnStimmenUebernehmen != null : "fx:id=\"btnStimmenUebernehmen\" was not injected: check your FXML file 'Stimmabgabe.fxml'.";
        assert lblAGAN != null : "fx:id=\"lblAGAN\" was not injected: check your FXML file 'Stimmabgabe.fxml'.";
        assert lblStimmen != null : "fx:id=\"lblStimmen\" was not injected: check your FXML file 'Stimmabgabe.fxml'.";
        assert lblStimmenAnzahl != null : "fx:id=\"lblStimmenAnzahl\" was not injected: check your FXML file 'Stimmabgabe.fxml'.";
        assert lblStimmenGesamtAnzahl != null : "fx:id=\"lblStimmenGesamtAnzahl\" was not injected: check your FXML file 'Stimmabgabe.fxml'.";
        assert lblTOP != null : "fx:id=\"lblTOP\" was not injected: check your FXML file 'Stimmabgabe.fxml'.";
        assert lblTOPText != null : "fx:id=\"lblTOPText\" was not injected: check your FXML file 'Stimmabgabe.fxml'.";

        /************* Ab hier individuell ********************************************/

        lblTOP.setText(gTop);
        gTopText=gTopText.replace("<br />", "");
        lblTOPText.setText(gTopText);
        lblAGAN.setText(gAGAN);
        
        switch (gStimmartNumerisch) {
        case KonstStimmart.ja:
            lblStimmen.setText("Ja-Stimmen:");
            btnAlles.setText("alle auf JA");
            break;
        case KonstStimmart.nein:
            lblStimmen.setText("Nein-Stimmen:");
            btnAlles.setText("alle auf NEIN");
            break;
        case KonstStimmart.enthaltung:
            lblStimmen.setText("Enthaltungen:");
            btnAlles.setText("alle auf Enthaltung");
            break;
        }
        
        String hStimmenGesamt=CaString.toStringDE(Long.parseLong(gStimmenGesamt));
        lblStimmenGesamtAnzahl.setText(hStimmenGesamt);

        anzeigenStimmenErgebnis();

//        /*Wird hier belegt*/
//        
//        public String gStimmenGesamt="";
//        
//        public String gStimmenErgebnis="";
        

        
//        tfWechseln.setText(wechselText);

    }

    
    private void anzeigenStimmenErgebnis() {
        String hStimmenErgebnis=CaString.toStringDE(Long.parseLong(gStimmenErgebnis));
        lblStimmenAnzahl.setText(hStimmenErgebnis);
    }
    
    /**
     * On btn ja.
     *
     * @param event the event
     */
    @FXML
    void onBtnStimmenUebernehmen(ActionEvent event) {
        
        if (Long.parseLong(gStimmenGesamt)<Long.parseLong(gStimmenErgebnis)) {
            CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
            caZeigeHinweis.zeige(eigeneStage, "Stimmenzahl zu hoch");
            return;

        }
        ausgewaehlt = true;
        eigeneStage.hide();

    }

    /**
     * On btn nein.
     *
     * @param event the event
     */
    @FXML
    void onBtnAbbrechen(ActionEvent event) {
        ausgewaehlt = false;
        eigeneStage.hide();

    }

    /** The eigene stage. */
    private Stage eigeneStage;

 
    /** The ausgewaehlt. */
    public boolean ausgewaehlt = false;

    public String gTop="";
    public String gTopText="";
    public int gStimmartNumerisch=0;
    
    
    public String gStimmart="";//Wird hier belegt
    
    /*ohne "."*/
     public String gStimmenGesamt="";
    public String gStimmenErgebnis="";
    
    public String gAGAN="";

    /**
     * Inits the.
     *
     * @param pEigeneStage the eigene stage
     * @param pWechselText the wechsel text
     */
    public void init(Stage pEigeneStage) {
        eigeneStage = pEigeneStage;

    }

}
