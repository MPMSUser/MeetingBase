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

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.kordamp.ikonli.fontawesome5.FontAwesomeBrands;
import org.kordamp.ikonli.javafx.FontIcon;

import de.meetingapps.meetingclient.meetingClientAllg.CaProxyLieferant01;
import de.meetingapps.meetingclient.meetingClientDialoge.CtrlRoot;
import de.meetingapps.meetingclient.meetingClientOberflaechen.LoadingScreen;
import de.meetingapps.meetingclient.meetingClientOberflaechen.ObjectActions;
import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaString;
import de.meetingapps.meetingportal.meetComAllg.CaXML;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclIsin;
import de.meetingapps.meetingportal.meetComEntities.EclSRDMessage;
import de.meetingapps.meetingportal.meetComEntitiesXml.MeetingCancellationReason2Code;
import de.meetingapps.meetingportal.streaming.srdII.proxyLieferant01.entities.Response.RespMessage;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class CtrlSRDCancallation extends CtrlRoot {

    public final int width = 720;
    public final int height = 300;

    @FXML
    private AnchorPane rootPane;
    @FXML
    private VBox boxHistory;
    @FXML
    private ComboBox<String> cbCxlRsnCd;
    @FXML
    private TextArea taCxlRsn;
    @FXML
    private Button btnClose;
    @FXML
    private Button btnCreate;
    @FXML
    private Button btnSend;

    private DbBundle dbBundle = new DbBundle();

    private List<EclSRDMessage> sentMessages;
    private EclSRDMessage srdMessage;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'.000Z'");

    private String defaultStyle = "-fx-border-radius: 5; -fx-border-color: #231825";
    private String selectedStyle = "-fx-background-color: #8bbaf9; -fx-background-radius: 3; -fx-background-insets: 1; -fx-border-color: #808080; -fx-border-radius: 3;";

    private String defaultSeev = "seev.002.001.09";

    private HBox selectedBox = null;
    private Label notification;
    private StackPane loading;

//  @formatter:off
    private String defaultCancallation = "" 
            + "<Document xmlns=\"urn:iso:std:iso:20022:tech:xsd:seev.002.001.09\">"
            + "    <MtgCxl>" 
            + "        <MtgRef>" 
            + "            <MtgId>$MtgId</MtgId>"
            + "            <IssrMtgId>$IssuerMtgId</IssrMtgId>"
            + "            <MtgDtAndTm>$MtgDtAndTm</MtgDtAndTm>" 
            + "            <Tp>$Tp</Tp>"
            + "        </MtgRef>" 
            + "        <Scty>" 
            + "            <FinInstrmId>"
            + "                <ISIN>$Isin</ISIN>" 
            + "            </FinInstrmId>" 
            + "        </Scty>"
            + "        <Rsn>" 
            + "            <CxlRsnCd>" 
            + "                <Cd>$Cd</Cd>"
            + "            </CxlRsnCd>" 
            + "            <CxlRsn>$Rsn</CxlRsn>"
            + "        </Rsn>" 
            + "    </MtgCxl>" 
            + "</Document>";
//  @formatter:on

    @FXML
    private void initialize() {

        buildHistory();

        btnClose.setOnAction(e -> eigeneStage.hide());

        cbCxlRsnCd.getItems().setAll(MeetingCancellationReason2Code.getLabels());

        notification = ObjectActions.createNotification(rootPane, -1, 130, 15, -1);
        loading = LoadingScreen.createLoadingScreen(rootPane);

    }

    private void buildHistory() {

        dbBundle.openAll();
        dbBundle.openWeitere();
        sentMessages = dbBundle.dbSRDMessage.readAll().stream().filter(e -> e.getSent_at() != null)
                .collect(Collectors.toList());

        if (!sentMessages.isEmpty()) {

            boxHistory.getChildren().clear();

            dbBundle.dbIsin.readAll();
            List<EclIsin> isinList = dbBundle.dbIsin.ergebnis();

            for (EclIsin isin : isinList) {

                List<EclSRDMessage> tmpList = sentMessages.stream().filter(e -> e.getIsin().equals(isin.isin))
                        .collect(Collectors.toList());

                EclSRDMessage lastMsg = null;
                EclSRDMessage cancelMsg = tmpList.stream().filter(e -> e.getSeev().equals(defaultSeev)).findFirst()
                        .orElse(null);

                for (EclSRDMessage msg : tmpList) {

                    if (lastMsg == null || msg.getSent_at().after(lastMsg.getSent_at())) {
                        lastMsg = msg;
                    }
                }
                if (lastMsg != null) {
                    createHistory(lastMsg, cancelMsg, cancelMsg != null);
                }
            }
        }

        dbBundle.closeAll();
    }

    private void createHistory(EclSRDMessage message, EclSRDMessage cancelMsg, Boolean cancel) {

        final HBox box = new HBox(20);
        box.setAlignment(Pos.CENTER_LEFT);
        box.setMaxWidth(Double.MAX_VALUE);
        box.setPadding(new Insets(0, 5, 0, 5));
        box.setId(String.valueOf(message.getId()));
        box.setStyle(defaultStyle);

        final VBox vBox = new VBox(2);
        final Label lblIsin = new Label("ISIN: " + message.getIsin());
        final Label lblMessageId = new Label("Message ID: " + message.getMessageId());
        final Label lblMeetingId = new Label("Meeting ID: " + message.getMeetingId());
        final Label lblSent_At = new Label("Gesendet am: " + message.getSent_at());

        vBox.getChildren().addAll(lblIsin, lblMessageId, lblMeetingId, lblSent_At);

        box.getChildren().add(vBox);

        if (cancel)
            addSentIcon(box);

        box.setOnMouseClicked(e -> {
            srdMessage = message;
            styleSelectedButton(box, selectedBox);

            selectedBox = box;
            disableEdit(cancel);

            if (cancel) {
                cbCxlRsnCd.setValue(MeetingCancellationReason2Code.toLabel(MeetingCancellationReason2Code
                        .fromValue(CaString.searchBetween(cancelMsg.getDocument(), "<Cd>", "</Cd>"))));
                taCxlRsn.setText(CaString.searchBetween(cancelMsg.getDocument(), "<CxlRsn>", "</CxlRsn>"));
            }

        });

        boxHistory.getChildren().add(box);
    }

    private void disableEdit(Boolean editable) {
        cbCxlRsnCd.setDisable(editable);
        taCxlRsn.setDisable(editable);
        btnCreate.setDisable(editable);
        btnSend.setDisable(editable);
    }

    private void addSentIcon(HBox box) {
        Label icon = new Label("", new FontIcon(FontAwesomeBrands.TELEGRAM_PLANE));
        icon.setStyle("-fx-font-size: 32px;");
        box.getChildren().add(icon);
    }

    private String randomId() {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 30;
        Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1).filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();
    }

    public void styleSelectedButton(HBox newBox, HBox oldBox) {
        if (newBox != oldBox) {
            newBox.setStyle(selectedStyle);

            if (oldBox != null)
                oldBox.setStyle(defaultStyle);
        }
    }

    @FXML
    private void onCreate() {

    }

    private void buildSend() {

        final EclSRDMessage msg = new EclSRDMessage();

        msg.setSeev(defaultSeev);
        msg.setIsin(srdMessage.getIsin());
        msg.setAppHeader(dbBundle.dbSRDMessage.defaultAppHeader);
        msg.setDocument(defaultCancallation);
        msg.setMeetingId(srdMessage.getMeetingId());
        msg.setIssuerMeetingId(srdMessage.getIssuerMeetingId());
        msg.setMtgDtAndTime(srdMessage.getMtgDtAndTime());
        msg.setType(srdMessage.getType());
        msg.setMessageId(randomId());

        final LocalDateTime sent_at = LocalDateTime.now();

        msg.replaceAppHeader(msg.getMeetingId(), defaultSeev, sent_at.format(formatter));
        msg.replaceDocument(srdMessage.getMeetingId(), srdMessage.getIssuerMeetingId(), srdMessage.getMtgDtAndTime(),
                srdMessage.getType(), srdMessage.getIsin());

        msg.setDocument(msg.getDocument().replace("$Cd",
                MeetingCancellationReason2Code.fromLabel(cbCxlRsnCd.getValue()).name()));

        final String reason = taCxlRsn.getText().isBlank() ? "" : "<CxlRsn>" + taCxlRsn.getText() + "</CxlRsn>";
        msg.setDocument(msg.getDocument().replace("<CxlRsn>$Rsn</CxlRsn>", reason));

        msg.prepDocument();

        final String message = msg.proximityMessage();

        CaProxyLieferant01 send = new CaProxyLieferant01();
        RespMessage resp = send.postMessage(message);

        String respString = CaXML.marshal(resp);

        if (respString != null) {

            String errorText = CaString.searchBetween(respString, "<ErrorText>", "</ErrorText>");
            msg.setResponseMessage(CaXML.removeBlanks(respString));

            if (errorText == null) {

                msg.setSent_at(Timestamp.valueOf(sent_at));

                ObjectActions.showNotification(notification, "Nachricht gesendet.");

            } else {
                ObjectActions.showNotification(notification, "Senden fehlgeschlagen.");
                CaBug.druckeLog(respString, 10, 10);
            }

            dbBundle.openAll();
            dbBundle.openWeitere();
            dbBundle.dbSRDMessage.insert(msg);
            dbBundle.closeAll();

            Platform.runLater(() -> {
                buildHistory();
                disableEdit(true);
                cbCxlRsnCd.setValue(null);
                taCxlRsn.setText(null);
            });
        }
    }

    private Task<Void> sendTask() {
        Task<Void> task = new Task<Void>() {
            protected Void call() throws Exception {
                buildSend();
                return null;
            }
        };
        task.setOnScheduled(e -> loading.setVisible(true));
        task.setOnSucceeded(e -> loading.setVisible(false));
        task.setOnFailed(e -> loading.setVisible(false));

        return task;
    }

    @FXML
    private void onSend() {
        new Thread(sendTask()).start();
    }
}
