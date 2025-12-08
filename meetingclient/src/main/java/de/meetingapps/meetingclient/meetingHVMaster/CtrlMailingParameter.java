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

import java.util.List;

import de.meetingapps.meetingclient.meetingClientDialoge.CtrlRoot;
import de.meetingapps.meetingclient.meetingClientOberflaechen.ComboBoxZusatz;
import de.meetingapps.meetingclient.meetingClientOberflaechen.LoadingScreen;
import de.meetingapps.meetingportal.meetComBVerwaltung.BvReload;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclMailing;
import de.meetingapps.meetingportal.meetComHVParam.ParamS;
import de.meetingapps.meetingportal.meetComStub.StubMailing;
import de.meetingapps.meetingportal.meetComStub.StubParameter;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.web.HTMLEditor;
import javafx.scene.web.WebView;

public class CtrlMailingParameter extends CtrlRoot {
    
    public final int width = 1200;
    public final int height = 680;
    
    @FXML
    private AnchorPane rootPane;

    @FXML
    private Button btnSave;

    @FXML
    private ComboBox<EclMailing> comboVorlage;
    
    @FXML
    private TextField tfVorlageName;

    @FXML
    private HTMLEditor htmlEditorMail;

    @FXML
    private SplitPane splitPane;

    @FXML
    private TextArea taAlternativText;

    @FXML
    private TextArea taHtmlMail;

    @FXML
    private TextField txtApiBaseUrl;

    @FXML
    private TextField txtApiPassword;

    @FXML
    private TextField txtApiUsername;

    @FXML
    private TextField txtSubjectMail;

    @FXML
    private TextField txtMailSenderAddress;

    @FXML
    private TextField txtWebhookPassword;

    @FXML
    private TextField txtWebhookUrl;

    @FXML
    private TextField txtWebhookUsername;

    @FXML
    private ToggleButton toggleAlternativtext;
    
    private StackPane loading;

    ChangeListener<EclMailing> comboVorlageValueListener = new ChangeListener<EclMailing>() {
        @Override
        public void changed(ObservableValue<? extends EclMailing> observable, EclMailing oldValue,
                EclMailing newValue) {
            if (newValue != oldValue && newValue != null) {
                loadMailing();
            }
        }
    };

    @FXML
    void initialize() {

        /*Mailing-Liste aus DB holen (restliche Parameter stehen zur Verfügung)*/
        DbBundle lDbBundle = new DbBundle();
        StubMailing stubMailing = new StubMailing(false, lDbBundle);
        stubMailing.holeEclMailingList();
        List<EclMailing> mailings = stubMailing.rcEclMailingList;

        comboVorlage.getItems().addAll(mailings);
        comboVorlage.valueProperty().addListener(comboVorlageValueListener);
        comboVorlage.setConverter(ComboBoxZusatz.convertMailing(comboVorlage));
        comboVorlage.setValue(mailings.get(0));
        
        tfVorlageName.setText(mailings.get(0).name);

        txtApiBaseUrl.setText("");
        txtApiUsername.setText("");
        txtApiPassword.setText("");

        txtMailSenderAddress.setText("");

        txtWebhookUrl.setText("");
        txtWebhookUsername.setText("");
        txtWebhookPassword.setText("");
        
        WebView webview = (WebView) htmlEditorMail.lookup("WebView");
        GridPane.setHgrow(webview, Priority.ALWAYS);
        GridPane.setVgrow(webview, Priority.ALWAYS);
        
        loading = LoadingScreen.createLoadingScreen(rootPane);
    }
    
    private Task<Void> saveTask() {
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                
                 /*Server-Parameter speichern*/
                DbBundle lDbBundle = new DbBundle();
                lDbBundle.refreshParameterAusStatic();
                lDbBundle.openAllOhneParameterCheck();

                lDbBundle.dbParameter.updateServer_all();
                BvReload bvReload = new BvReload(lDbBundle);
                bvReload.setReloadParameterServer();
                lDbBundle.closeAll();

                /*Mandantenabhängige Parameter speichern*/
                StubParameter stubParameter = new StubParameter(false, lDbBundle);
                stubParameter.updateHVParam_all(ParamS.param);

                comboVorlage.getValue().name = tfVorlageName.getText().trim();
                comboVorlage.getValue().betreff = txtSubjectMail.getText().trim();
                comboVorlage.getValue().htmlMail = htmlEditorMail.getHtmlText().trim();
                comboVorlage.getValue().alternativMailText = taAlternativText.getText().trim();

                /*Mailing-Liste speichern*/
                StubMailing stubMailing = new StubMailing(false, lDbBundle);
                /*Und woher kriege ich hier die Mailing-Liste? Ist nur eine lokale Variable?*/
                stubMailing.speichereEclMailingList(comboVorlage.getItems());

                return null;
            }
        };

        task.setOnScheduled(e -> loading.setVisible(true));
        task.setOnSucceeded(e -> loading.setVisible(false));
        task.setOnFailed(e -> loading.setVisible(false));

        return task;
    }

    @FXML
    void doSave(ActionEvent event) {
        new Thread(saveTask()).start();
    }

    private void loadMailing() {
        this.tfVorlageName.setText(this.comboVorlage.getValue().name);
        this.txtSubjectMail.setText(this.comboVorlage.getValue().betreff);
        this.htmlEditorMail.setHtmlText(this.comboVorlage.getValue().htmlMail);
        this.taHtmlMail.setText(this.comboVorlage.getValue().htmlMail);
        this.taAlternativText.setText(this.comboVorlage.getValue().alternativMailText);

    }

    @FXML
    void updatePlainTextMail() {
        this.taHtmlMail.setText(this.htmlEditorMail.getHtmlText());
    }

    @FXML
    void updateHtmlMail() {
        this.htmlEditorMail.setHtmlText(this.taHtmlMail.getText());
    }

    @FXML
    void doToggleAlternativtext(ActionEvent event) {

        taAlternativText.setVisible(!taAlternativText.isVisible());
        splitPane.setVisible(!splitPane.isVisible());

    }

}
