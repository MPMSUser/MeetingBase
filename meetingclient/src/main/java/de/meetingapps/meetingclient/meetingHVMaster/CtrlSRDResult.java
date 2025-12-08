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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import de.meetingapps.meetingclient.meetingClientDialoge.CtrlRoot;
import de.meetingapps.meetingclient.meetingClientOberflaechen.TableCellLongMitPunkt;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclSRDMessage;
import de.meetingapps.meetingportal.meetComEntitiesXml.Vote20;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class CtrlSRDResult extends CtrlRoot {

    public final int width = 1045;
    public final int height = 500;

    @FXML
    private AnchorPane rootPane;
    @FXML
    private TableView<Vote20> tableView;
    @FXML
    private TableColumn<Vote20, String> colLabel;
    @FXML
    private TableColumn<Vote20, String> colStatus;
    @FXML
    private TableColumn<Vote20, String> colFor;
    @FXML
    private TableColumn<Vote20, String> colAgainst;
    @FXML
    private TableColumn<Vote20, String> colAbstain;
    @FXML
    private VBox boxHistory;
    @FXML
    private Button btnCreate;
    @FXML
    private Button btnSend;
    @FXML
    private Button btnClose;

    private String defaultSeev = "seev.008.001.09";

    private final String replaceString = "<VoteRslt></VoteRslt>";

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'.000Z'");

//  @formatter:off
    private String defaultResult = "" 
            + "<Document xmlns=\"urn:iso:std:iso:20022:tech:xsd:seev.008.001.09\">"
            + "    <MtgRsltDssmntn>"
            + "        <MtgRsltsDssmntnTp>NEWM</MtgRsltsDssmntnTp>" 
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
            + replaceString
            + "    </MtgRsltDssmntn>" 
            + "</Document>";
//  @formatter:on

    private List<Vote20> list = new ArrayList<>();
    private List<EclSRDMessage> sentMessages;

    @FXML
    private void initialize() {

        colLabel.setCellValueFactory(new PropertyValueFactory<>("issuerLabel"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("resolutionStatus"));
        colFor.setCellValueFactory(new PropertyValueFactory<>("forShares"));
        colFor.setCellFactory(e -> new TableCellLongMitPunkt<>());
        colAgainst.setCellValueFactory(new PropertyValueFactory<>("againstShares"));
        colAgainst.setCellFactory(e -> new TableCellLongMitPunkt<>());
        colAbstain.setCellValueFactory(new PropertyValueFactory<>("abstainShares"));
        colAbstain.setCellFactory(e -> new TableCellLongMitPunkt<>());

        list.add(createNewVote("2"));
        list.add(createNewVote("3"));
        list.add(createNewVote("4"));
        list.add(createNewVote("5"));
        list.add(createNewVote("6"));
        list.add(createNewVote("7"));
        list.add(createNewVote("8"));

        tableView.getItems().setAll(list);

        btnClose.setOnAction(e -> eigeneStage.hide());

        btnSend.setOnAction(e -> {

            DbBundle dbBundle = new DbBundle();
            dbBundle.openAll();
            dbBundle.openWeitere();

            sentMessages = dbBundle.dbSRDMessage.readAll().stream()
                    .filter(x -> x.getSent_at() != null && x.getSeev().equals(dbBundle.dbSRDMessage.defaultSeev))
                    .collect(Collectors.toList());

            Set<String> set = new HashSet<>();

            final EclSRDMessage msg = new EclSRDMessage();

            msg.setSeev(defaultSeev);
            msg.setAppHeader(dbBundle.dbSRDMessage.defaultAppHeader);
            msg.setDocument(defaultResult);

            for (EclSRDMessage value : sentMessages) {
                if (!set.contains(value.getMeetingId())) {
                    set.add(value.getMeetingId());

                    msg.setIsin(value.getIsin());
                    msg.setMeetingId(value.getMeetingId());
                    msg.setIssuerMeetingId(value.getIssuerMeetingId());
                    msg.setMtgDtAndTime(value.getMtgDtAndTime());
                    msg.setType(value.getType());
                    msg.setMessageId(randomId());

                    final LocalDateTime sent_at = LocalDateTime.now();
                    msg.setSent_at(Timestamp.valueOf(sent_at));

                    msg.replaceAppHeader(msg.getMeetingId(), defaultSeev, sent_at.format(formatter));
                    msg.replaceDocument(value.getMeetingId(), value.getIssuerMeetingId(), value.getMtgDtAndTime(),
                            value.getType(), value.getIsin());

                    msg.setDocument(msg.getDocument().replace(replaceString, createVoteResults()));
                    
                    msg.prepDocument();
                    
                    dbBundle.dbSRDMessage.insert(msg);

                }
            }
            dbBundle.closeAll();

        });
    }

    private String createVoteResults() {

        StringBuilder sb = new StringBuilder();

        for (Vote20 value : list) {

            sb.append("<VoteRslt>");
            sb.append("<IssrLabl>" + value.getIssuerLabel() + "</IssrLabl>");
            sb.append("<RsltnSts>" + value.getResolutionStatus() + "</RsltnSts>");
            sb.append("<For><Unit>" + value.getForShares() + "</Unit></For>");
            sb.append("<Agnst><Unit>" + value.getAgainstShares() + "</Unit></Agnst>");
            sb.append("<Abstn><Unit>" + value.getAbstainShares() + "</Unit></Abstn>");
            sb.append("</VoteRslt>");

        }

        return sb.toString();
    }

    private int min = 0;
    private int max = 5000001;

    private Vote20 createNewVote(String issuerLabel) {

        max = 5000001;

        String forShares = calcFor();
        String againstShares = calcAgainst();
        String abstainShares = calcAbstain();

        String status = Integer.valueOf(forShares) > Integer.valueOf(againstShares) ? "ACPT" : "REJT";

        return new Vote20(issuerLabel, status, forShares, againstShares, abstainShares);
    }

    private String calcFor() {

        int erg = ThreadLocalRandom.current().nextInt(min, max);

        max -= erg;

        return String.valueOf(erg);

    }

    private String calcAgainst() {

        int erg = ThreadLocalRandom.current().nextInt(min, max);

        max -= erg;

        return String.valueOf(erg);

    }

    private String calcAbstain() {

        return String.valueOf(max);

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

}
