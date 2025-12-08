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

import java.io.File;
import java.io.StringReader;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.kordamp.ikonli.fontawesome5.FontAwesomeBrands;
import org.kordamp.ikonli.fontawesome5.FontAwesomeRegular;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;

import de.meetingapps.meetingclient.meetingClientAllg.CaProxyLieferant01;
import de.meetingapps.meetingclient.meetingClientDialoge.CtrlRoot;
import de.meetingapps.meetingclient.meetingClientOberflaechen.CustomAlert;
import de.meetingapps.meetingclient.meetingClientOberflaechen.LoadingScreen;
import de.meetingapps.meetingclient.meetingClientOberflaechen.ObjectActions;
import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaDateiWrite;
import de.meetingapps.meetingportal.meetComAllg.CaDatumZeit;
import de.meetingapps.meetingportal.meetComAllg.CaString;
import de.meetingapps.meetingportal.meetComAllg.CaXML;
import de.meetingapps.meetingportal.meetComBl.BlAbstimmung;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclAbstimmung;
import de.meetingapps.meetingportal.meetComEntities.EclIsin;
import de.meetingapps.meetingportal.meetComEntities.EclSRDMessage;
import de.meetingapps.meetingportal.meetComEntities.EclStaaten;
import de.meetingapps.meetingportal.meetComEntitiesXml.AdditionalRight1Code;
import de.meetingapps.meetingportal.meetComEntitiesXml.AdditionalRightCode1Choice;
import de.meetingapps.meetingportal.meetComEntitiesXml.AdditionalRightThreshold1Choice;
import de.meetingapps.meetingportal.meetComEntitiesXml.AdditionalRights3;
import de.meetingapps.meetingportal.meetComEntitiesXml.AddressType2Code;
import de.meetingapps.meetingportal.meetComEntitiesXml.AgentRole1Code;
import de.meetingapps.meetingportal.meetComEntitiesXml.DateAndDateTime2Choice;
import de.meetingapps.meetingportal.meetComEntitiesXml.DateFormat58Choice;
import de.meetingapps.meetingportal.meetComEntitiesXml.DateType10Code;
import de.meetingapps.meetingportal.meetComEntitiesXml.Document;
import de.meetingapps.meetingportal.meetComEntitiesXml.EventCompletenessStatus1Code;
import de.meetingapps.meetingportal.meetComEntitiesXml.EventConfirmationStatus1Code;
import de.meetingapps.meetingportal.meetComEntitiesXml.IndividualPerson43;
import de.meetingapps.meetingportal.meetComEntitiesXml.ItemDescription2;
import de.meetingapps.meetingportal.meetComEntitiesXml.MeetingDateStatus2Code;
import de.meetingapps.meetingportal.meetComEntitiesXml.MeetingType4Code;
import de.meetingapps.meetingportal.meetComEntitiesXml.NaturalPersonIdentification1;
import de.meetingapps.meetingportal.meetComEntitiesXml.NotificationType3Code;
import de.meetingapps.meetingportal.meetComEntitiesXml.ParticipationMethod2;
import de.meetingapps.meetingportal.meetComEntitiesXml.ParticipationMethod3Choice;
import de.meetingapps.meetingportal.meetComEntitiesXml.PartyIdentification232Choice;
import de.meetingapps.meetingportal.meetComEntitiesXml.PartyIdentification238;
import de.meetingapps.meetingportal.meetComEntitiesXml.PersonName3;
import de.meetingapps.meetingportal.meetComEntitiesXml.Proxy11;
import de.meetingapps.meetingportal.meetComEntitiesXml.ProxyType3Code;
import de.meetingapps.meetingportal.meetComEntitiesXml.Resolution7;
import de.meetingapps.meetingportal.meetComEntitiesXml.ResolutionStatus1Code;
import de.meetingapps.meetingportal.meetComEntitiesXml.VoteChannel1Code;
import de.meetingapps.meetingportal.meetComEntitiesXml.VoteInstruction6Code;
import de.meetingapps.meetingportal.meetComEntitiesXml.VoteInstructionType1;
import de.meetingapps.meetingportal.meetComEntitiesXml.VoteInstructionType1Choice;
import de.meetingapps.meetingportal.meetComEntitiesXml.VoteType1Code;
import de.meetingapps.meetingportal.meetComEntitiesXml.VotingParticipationMethod3Code;
import de.meetingapps.meetingportal.meetComHVParam.ParamS;
import de.meetingapps.meetingportal.streaming.srdII.proxyLieferant01.entities.Response.RespMessage;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.Unmarshaller;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.robot.Robot;
import javafx.stage.DirectoryChooser;

public class CtrlSRDNotification extends CtrlRoot {

    public final int width = 1200;
    public final int height = 700;

    @FXML
    private AnchorPane rootPane;
    @FXML
    private ComboBox<String> cbIsin;
    @FXML
    private Button btnGeneralInformation;
    @FXML
    private Button btnMeeting;
    @FXML
    private Button btnParticipation;
    @FXML
    private Button btnProxyChoice;
    @FXML
    private Button btnMeetingDetails;
    @FXML
    private Button btnIssuer;
    @FXML
    private Button btnIssuerAgent;
    @FXML
    private Button btnResolution;
    @FXML
    private Button btnVote;
    @FXML
    private ScrollPane scrlPane;
    @FXML
    private GridPane gridPane;
    @FXML
    private ComboBox<String> cbNtfctnTp;
    @FXML
    private Button btnNtfctnTp;
    @FXML
    private ComboBox<String> cbEvtCmpltnsSts;
    @FXML
    private Button btnEvtCmpltnsSts;
    @FXML
    private ComboBox<String> cbEvtConfSts;
    @FXML
    private Button btnEvtConfSts;
    @FXML
    private CheckBox checkShrhldrRghtsDrctvInd;
    @FXML
    private Button btnShrhldrRghtsDrctvInd;
    @FXML
    private CheckBox checkConfOfHldgReqrd;
    @FXML
    private Button btnConfOfHldgReqrd;
    @FXML
    private TextField tfPrvsNtfctnId;
    @FXML
    private CheckBox checkRcnfrmInstrs;
    @FXML
    private TextField tfMtgId;
    @FXML
    private TextField tfIssrMtgId;
    @FXML
    private ComboBox<String> cbTp;
    @FXML
    private Button btnTp;
    @FXML
    private DatePicker dpAnncmntDt;
    @FXML
    private Button btnAnncmntDt;
    @FXML
    private Button btnAddParticipation;
    @FXML
    private VBox boxParticipation;
    @FXML
    private DatePicker dpConfirmationMarketDeadline;
    @FXML
    private TextField tfConfirmationMarketDeadlineTime;
    @FXML
    private Button btnConfirmationMarketDeadline;
    @FXML
    private TextField tfAdditionalDocumentationURLAddress;
    @FXML
    private Button btnAdditionalDocumentationURLAddress;
    @FXML
    private Button btnAddAdditionalProcedureDetails;
    @FXML
    private VBox boxAdditionalProcedureDetails;
    @FXML
    private DatePicker dpMarketDeadline;
    @FXML
    private TextField tfMarketDeadlineTime;
    @FXML
    private Button btnMarketDeadline;
    @FXML
    private Button btnAuthorisedProxy;
    @FXML
    private VBox boxAuthorisedProxy;
    @FXML
    private DatePicker dpEntitlementFixingDate;
    @FXML
    private Button btnEntitlementFixingDate;;
    @FXML
    private ComboBox<String> cbSecuritiesBlockingPeriod;
    @FXML
    private Button btnSecuritiesBlockingPeriod;
    @FXML
    private TextField tfRegnSctiesMktDdln;
    @FXML
    private CheckBox checkRegnSctiesMktDdln;
    @FXML
    private Button btnRegnSctiesMktDdln;
    @FXML
    private DatePicker dpMeetingDate;
    @FXML
    private TextField tfMeetingTime;
    @FXML
    private Button btnMeetingDate;
    @FXML
    private ComboBox<String> cbDateStatus;
    @FXML
    private Button btnDateStatus;
    @FXML
    private ComboBox<String> cbMeetingAddressType;
    @FXML
    private Button btnMeetingAddressType;
    @FXML
    private TextField tfAddressLine1;
    @FXML
    private Button btnAddressLine1;
    @FXML
    private TextField tfAddressLine2;
    @FXML
    private Button btnAddressLine2;
    @FXML
    private TextField tfAddressLine3;
    @FXML
    private Button btnAddressLine3;
    @FXML
    private TextField tfMeetingStreet;
    @FXML
    private Button btnMeetingStreet;
    @FXML
    private TextField tfMeetingBuilding;
    @FXML
    private Button btnMeetingBuilding;
    @FXML
    private TextField tfMeetingPost;
    @FXML
    private Button btnMeetingPost;
    @FXML
    private TextField tfMeetingTown;
    @FXML
    private Button btnMeetingTown;
    @FXML
    private TextField tfMeetingCountry;
    @FXML
    private Button btnMeetingCountry;
    @FXML
    private TextField tfIssuerName;
    @FXML
    private Button btnIssuerName;
    @FXML
    private ComboBox<String> cbIssuerAddressType;
    @FXML
    private Button btnIssuerAddressType;
    @FXML
    private TextField tfIssuerStreet;
    @FXML
    private Button btnIssuerStreet;
    @FXML
    private TextField tfIssuerBuilding;
    @FXML
    private Button btnIssuerBuilding;
    @FXML
    private TextField tfIssuerPost;
    @FXML
    private Button btnIssuerPost;
    @FXML
    private TextField tfIssuerTown;
    @FXML
    private Button btnIssuerTown;
    @FXML
    private TextField tfIssuerCountry;
    @FXML
    private Button btnIssuerCountry;
    @FXML
    private TextField tfIssuerAgentName;
    @FXML
    private Button btnIssuerAgentName;
    @FXML
    private ComboBox<String> cbIssuerAgentRole;
    @FXML
    private Button btnIssuerAgentRole;
    @FXML
    private TextField tfIsin;
    @FXML
    private Button btnEditResolution;
    @FXML
    private VBox boxResolution;
    @FXML
    private CheckBox checkPartialVoteAllowed;
    @FXML
    private Button btnPartialVoteAllowed;
    @FXML
    private CheckBox checkSplitVoteAllowed;
    @FXML
    private Button btnSplitVoteAllowed;
    @FXML
    private DatePicker dpVoteMarketDeadline;
    @FXML
    private TextField tfVoteMarketDeadlineTime;
    @FXML
    private Button btnVoteMarketDeadline;
    @FXML
    private ComboBox<String> cbVoteChannel;
    @FXML
    private Button btnVoteChannel;
    @FXML
    private CheckBox checkBeneficialOwnerDisclosure;
    @FXML
    private Button btnBeneficialOwnerDisclosure;
    @FXML
    private TextArea taDisclaimer;
    @FXML
    private Button btnDisclaimer;
    @FXML
    private Label lblSentTime;
    @FXML
    private Button btnChange;
    @FXML
    private VBox boxHistory;
    @FXML
    private Button btnResponse;
    @FXML
    private Button btnClose;
    @FXML
    private Button btnParameter;
    @FXML
    private Button btnSave;
    @FXML
    private Button btnSend;

    private Document defaultDoc;
    private Document currentDoc;

    private DbBundle dbBundle;
    private EclSRDMessage srdMessage;

    private Resolution7[] agenda;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'.000Z'");

    private List<EclIsin> isinList;
    private List<EclSRDMessage> messageList;
    private List<EclStaaten> staatenList;

    private Set<Button> changeButtons = new HashSet<>();

    private StackPane loading;
    private Button selectedButton = null;
    private Label notification;

    final int idLength = 30;

    @FXML
    private void initialize() {

        lblSentTime.textProperty().addListener((o, oV, nV) -> {
            final Boolean sent = !nV.isBlank();

            btnChange.setDisable(!sent);
            btnSave.setDisable(sent);
            btnSend.setDisable(sent);
        });

        dbBundle = new DbBundle();
        dbBundle.openAll();
        dbBundle.openWeitere();
        dbBundle.dbIsin.readAll();

        isinList = dbBundle.dbIsin.ergebnis();
        checkDefault();

        messageList = dbBundle.dbSRDMessage.readAll();
        dbBundle.dbStaaten.readAll(0);
        staatenList = dbBundle.dbStaaten.ergebnis();

        dbBundle.closeAll();

        readDoc(messageList.get(0));

        Set<String> set = new HashSet<>();
        isinList.forEach(e -> set.add(e.isin));

        configNotificationGeneralInformation();
        configMeeting();
        configProxyChoice();
        configMeetingDetails();
        configIssuer();
        configIssuerAgent();
        configSecurity();
        configVote();

        cbIsin.getItems().setAll(set);
        cbIsin.valueProperty().addListener((o, oV, nV) -> {
            if (nV != null && !nV.isBlank()) {
                final List<EclSRDMessage> tmpList = messageList.stream()
                        .filter(e -> e.getIsin().equals(nV) && e.getSeev().equals(dbBundle.dbSRDMessage.defaultSeev))
                        .collect(Collectors.toList());

                srdMessage = tmpList.get(0);
                configIsin(srdMessage);

                System.out.println(srdMessage.toString());

                boxHistory.getChildren().clear();

                for (EclSRDMessage v : tmpList) {
                    createBox(v);
                }

                selectedButton = (Button) boxHistory.getChildren().get(0);
                ObjectActions.styleSelectedButton(selectedButton, null);
            }
        });
        cbIsin.setValue(cbIsin.getItems().get(0));

        btnClose.setOnAction(e -> eigeneStage.hide());

        btnAddParticipation.setGraphic(new FontIcon(FontAwesomeSolid.PLUS));
        btnAddAdditionalProcedureDetails.setGraphic(new FontIcon(FontAwesomeSolid.PLUS));
        btnAuthorisedProxy.setGraphic(new FontIcon(FontAwesomeSolid.PLUS));
        btnEditResolution.setGraphic(new FontIcon(FontAwesomeSolid.EDIT));

        btnEditResolution.setOnAction(e -> gewAbstimmungen());

        jumpTo(btnGeneralInformation, btnClose);
        jumpTo(btnMeeting, checkConfOfHldgReqrd);
        jumpTo(btnParticipation, btnAddParticipation);
        jumpTo(btnProxyChoice, tfAdditionalDocumentationURLAddress);
        jumpTo(btnMeetingDetails, tfRegnSctiesMktDdln);
        jumpTo(btnIssuer, tfMeetingCountry);
        jumpTo(btnIssuerAgent, btnIssuerCountry);
        jumpTo(btnResolution, btnEditResolution);
        jumpTo(btnVote, checkPartialVoteAllowed);

        btnChange.setOnAction(e -> onChange());
        btnSend.setOnAction(e -> onSend());

        notification = ObjectActions.createNotification(rootPane, -1, 165, 5, -1);
        loading = LoadingScreen.createLoadingScreen(rootPane);
    }

    private void configIsin(EclSRDMessage message) {

        readDoc(message);

        boxParticipation.getChildren().clear();
        boxAdditionalProcedureDetails.getChildren().clear();
        boxAuthorisedProxy.getChildren().clear();
        boxResolution.getChildren().clear();

        final var defGnlInf = defaultDoc.getMtgNtfctn().getNtfctnGnlInf();

        cbNtfctnTp.setValue(NotificationType3Code.toLabel(defGnlInf.getNtfctnTp()));
        cbEvtCmpltnsSts.setValue(EventCompletenessStatus1Code.toLabel(defGnlInf.getNtfctnSts().getEvtCmpltnsSts()));
        cbEvtConfSts.setValue(EventConfirmationStatus1Code.toLabel(defGnlInf.getNtfctnSts().getEvtConfSts()));
        checkShrhldrRghtsDrctvInd.setSelected(defGnlInf.isShrhldrRghtsDrctvInd());
        checkConfOfHldgReqrd.setSelected(defGnlInf.isConfOfHldgReqrd());

        final Boolean updateMessage = srdMessage.getPrevMessageId() != null;

        tfPrvsNtfctnId.setDisable(!updateMessage);
        tfPrvsNtfctnId.setText(updateMessage ? defaultDoc.getMtgNtfctn().getNtfctnUpd().getPrvsNtfctnId() : "");

        checkRcnfrmInstrs.setDisable(!updateMessage);
        checkRcnfrmInstrs
                .setSelected(updateMessage ? defaultDoc.getMtgNtfctn().getNtfctnUpd().isRcnfrmInstrs() : false);

        final var defMtg = defaultDoc.getMtgNtfctn().getMtg();
        final var curMtg = currentDoc.getMtgNtfctn().getMtg();

        tfMtgId.setText(defMtg.getMtgId());
        tfIssrMtgId.setText(defMtg.getIssrMtgId());
        cbTp.setValue(MeetingType4Code.toLabel(defMtg.getTp()));
        dpAnncmntDt.setValue(LocalDate.parse(defMtg.getAnncmntDt().getDt()));

        boxParticipation.getChildren().clear();
        for (ParticipationMethod2 method : curMtg.getPrtcptn()) {
            buildParticipation(method);
        }

        final var confMktDdln = defMtg.getAttndnc().getConfMktDdln().getDtOrDtTm();

        dpConfirmationMarketDeadline.setValue(convertToDate(confMktDdln.getDtTm()));
        tfConfirmationMarketDeadlineTime.setText(convertToTime(confMktDdln.getDtTm()));
        tfAdditionalDocumentationURLAddress.setText(defMtg.getAddtlDcmnttnURLAdr().get(0));

        boxAdditionalProcedureDetails.getChildren().clear();
        for (AdditionalRights3 right : curMtg.getAddtlPrcdrDtls()) {
            buildAdditionalProcedureDetails(right);
        }

        final var marketDdln = defMtg.getPrxyChc().getPrxy().getMktDdln().getDtOrDtTm();

        dpMarketDeadline.setValue(convertToDate(marketDdln.getDtTm()));
        tfMarketDeadlineTime.setText(convertToTime(marketDdln.getDtTm()));

        for (Proxy11 proxy : curMtg.getPrxyChc().getPrxy().getAuthrsdPrxy()) {
            buildAuthorisedProxy(proxy);
        }

        dpEntitlementFixingDate.setValue(LocalDate.parse(defMtg.getEntitlmntFxgDt().getDt().getDt()));
        cbSecuritiesBlockingPeriod.setValue(DateType10Code.toLabel(defMtg.getSctiesBlckgPrdEndDt().getDtCd().getCd()));

        checkRegnSctiesMktDdln.setSelected(defMtg.getRegnSctiesMktDdln() != null);

        final var mtgDtls = defaultDoc.getMtgNtfctn().getMtgDtls().get(0);
        final var mtgLoc = mtgDtls.getLctn().get(0).getAdr();

        dpMeetingDate.setValue(convertToDate(mtgDtls.getDtAndTm().getDtOrDtTm().getDtTm()));
        tfMeetingTime.setText(convertToTime(mtgDtls.getDtAndTm().getDtOrDtTm().getDtTm()));
        cbDateStatus.setValue(MeetingDateStatus2Code.toLabel(mtgDtls.getDtSts()));
        cbMeetingAddressType.setValue(AddressType2Code.toLabel(mtgLoc.getAdrTp()));
        tfAddressLine1.setText(mtgLoc.getAdrLine().get(0));
        tfMeetingStreet.setText(mtgLoc.getStrtNm());
        tfMeetingBuilding.setText(mtgLoc.getBldgNb());
        tfMeetingPost.setText(mtgLoc.getPstCd());
        tfMeetingTown.setText(mtgLoc.getTwnNm());
        tfMeetingCountry.setText(mtgLoc.getCtry());

        final var issuer = defaultDoc.getMtgNtfctn().getIssr().getId().getNmAndAdr();

        tfIssuerName.setText(issuer.getNm());
        cbIssuerAddressType.setValue(AddressType2Code.toLabel(issuer.getAdr().getAdrTp()));
        tfIssuerStreet.setText(issuer.getAdr().getStrtNm());
        tfIssuerBuilding.setText(issuer.getAdr().getBldgNb());
        tfIssuerPost.setText(issuer.getAdr().getPstCd());
        tfIssuerTown.setText(issuer.getAdr().getTwnNm());
        tfIssuerCountry.setText(issuer.getAdr().getCtry());

        final var issuerAgent = defaultDoc.getMtgNtfctn().getIssrAgt().get(0);

        tfIssuerAgentName.setText(issuerAgent.getId().getNmAndAdr().getNm());
        cbIssuerAgentRole.setValue(AgentRole1Code.toLabel(issuerAgent.getRole()));

        for (int i = 0; i < currentDoc.getMtgNtfctn().getRsltn().size(); i++) {
            buildResolution(currentDoc.getMtgNtfctn().getRsltn().get(i), defaultDoc.getMtgNtfctn().getRsltn().get(i));
        }

        tfIsin.setText(message.getIsin());

        final var vote = defaultDoc.getMtgNtfctn().getVote();

        checkPartialVoteAllowed.setSelected(vote.isPrtlVoteAllwd());
        checkSplitVoteAllowed.setSelected(vote.isSpltVoteAllwd());
        dpVoteMarketDeadline.setValue(convertToDate(vote.getVoteMktDdln().getDtOrDtTm().getDtTm()));
        tfVoteMarketDeadlineTime.setText(convertToTime(vote.getVoteMktDdln().getDtOrDtTm().getDtTm()));
        cbVoteChannel.setValue(VoteChannel1Code.toLabel(vote.getVoteMthds().getVoteThrghNtwk().getVoteChanl()));
        checkBeneficialOwnerDisclosure.setSelected(vote.isBnfclOwnrDsclsr());

        setSentTime(srdMessage.getSent_at());
    }

    private void configNotificationGeneralInformation() {

        cbNtfctnTp.getItems().setAll(NotificationType3Code.getLabels());
        cbNtfctnTp.valueProperty().addListener((o, oV, nV) -> {
            showChange(btnNtfctnTp, nV
                    .equals(NotificationType3Code.toLabel(defaultDoc.getMtgNtfctn().getNtfctnGnlInf().getNtfctnTp())));
            currentDoc.getMtgNtfctn().getNtfctnGnlInf().setNtfctnTp(NotificationType3Code.fromLabel(nV));
        });

        cbEvtCmpltnsSts.getItems().setAll(EventCompletenessStatus1Code.getLabels());
        cbEvtCmpltnsSts.valueProperty().addListener((o, oV, nV) -> {
            showChange(btnEvtCmpltnsSts, nV.equals(EventCompletenessStatus1Code
                    .toLabel(defaultDoc.getMtgNtfctn().getNtfctnGnlInf().getNtfctnSts().getEvtCmpltnsSts())));
            currentDoc.getMtgNtfctn().getNtfctnGnlInf().getNtfctnSts()
                    .setEvtCmpltnsSts(EventCompletenessStatus1Code.fromLabel(nV));
        });

        cbEvtConfSts.getItems().setAll(EventConfirmationStatus1Code.getLabels());
        cbEvtConfSts.valueProperty().addListener((o, oV, nV) -> {
            showChange(btnEvtConfSts, nV.equals(EventConfirmationStatus1Code
                    .toLabel(defaultDoc.getMtgNtfctn().getNtfctnGnlInf().getNtfctnSts().getEvtConfSts())));
            currentDoc.getMtgNtfctn().getNtfctnGnlInf().getNtfctnSts()
                    .setEvtConfSts(EventConfirmationStatus1Code.fromLabel(nV));
        });

        checkShrhldrRghtsDrctvInd.selectedProperty().addListener((o, oV, nV) -> {
            showChange(btnShrhldrRghtsDrctvInd,
                    nV.equals(defaultDoc.getMtgNtfctn().getNtfctnGnlInf().isShrhldrRghtsDrctvInd()));
            currentDoc.getMtgNtfctn().getNtfctnGnlInf().setShrhldrRghtsDrctvInd(nV);
        });

        checkConfOfHldgReqrd.selectedProperty().addListener((o, oV, nV) -> {
            showChange(btnConfOfHldgReqrd, nV.equals(defaultDoc.getMtgNtfctn().getNtfctnGnlInf().isConfOfHldgReqrd()));
            currentDoc.getMtgNtfctn().getNtfctnGnlInf().setConfOfHldgReqrd(nV);
        });

        tfPrvsNtfctnId.textProperty().addListener((o, oV, nV) -> {
            if (currentDoc.getMtgNtfctn().getNtfctnUpd() != null)
                currentDoc.getMtgNtfctn().getNtfctnUpd().setPrvsNtfctnId(nV);
        });

        checkRcnfrmInstrs.selectedProperty().addListener((o, oV, nV) -> {
            if (currentDoc.getMtgNtfctn().getNtfctnUpd() != null)
                currentDoc.getMtgNtfctn().getNtfctnUpd().setRcnfrmInstrs(nV);
        });
    }

    private void configMeeting() {

        tfMtgId.textProperty().addListener((o, oV, nV) -> {
            currentDoc.getMtgNtfctn().getMtg().setMtgId(nV);
        });

        tfIssrMtgId.textProperty().addListener((o, oV, nV) -> {
            currentDoc.getMtgNtfctn().getMtg().setIssrMtgId(nV);
        });

        cbTp.getItems().setAll(MeetingType4Code.getLabels());
        cbTp.valueProperty().addListener((o, oV, nV) -> {
            showChange(btnTp, nV.equals(MeetingType4Code.toLabel(defaultDoc.getMtgNtfctn().getMtg().getTp())));
            currentDoc.getMtgNtfctn().getMtg().setTp(MeetingType4Code.fromLabel(nV));
        });

        dpAnncmntDt.valueProperty().addListener((o, oV, nV) -> {
            showChange(btnAnncmntDt, nV.toString().equals(defaultDoc.getMtgNtfctn().getMtg().getAnncmntDt().getDt()));
            currentDoc.getMtgNtfctn().getMtg().getAnncmntDt().setDt(nV.toString());
        });

        btnAddParticipation.setOnAction(e -> {

            ParticipationMethod2 value = new ParticipationMethod2();
            value.setPrtcptnMtd(new ParticipationMethod3Choice());
            value.setIssrDdlnForVtng(new DateFormat58Choice());
            value.getIssrDdlnForVtng().setDtOrDtTm(new DateAndDateTime2Choice());
            value.getIssrDdlnForVtng().getDtOrDtTm().setDtTm(LocalDateTime.now().toString());

            buildParticipation(value);
            currentDoc.getMtgNtfctn().getMtg().getPrtcptn().add(value);
        });

        dpConfirmationMarketDeadline.valueProperty().addListener((o, oV, nV) -> {
            currentDoc.getMtgNtfctn().getMtg().getAttndnc().getConfMktDdln().getDtOrDtTm()
                    .setDtTm(buildDateTimeString(nV, tfConfirmationMarketDeadlineTime.getText()));
            showChange(btnConfirmationMarketDeadline,
                    currentDoc.getMtgNtfctn().getMtg().getAttndnc().getConfMktDdln().getDtOrDtTm().getDtTm().equals(
                            defaultDoc.getMtgNtfctn().getMtg().getAttndnc().getConfMktDdln().getDtOrDtTm().getDtTm()));
        });

        tfConfirmationMarketDeadlineTime.textProperty().addListener((o, oV, nV) -> {
            currentDoc.getMtgNtfctn().getMtg().getAttndnc().getConfMktDdln().getDtOrDtTm()
                    .setDtTm(buildDateTimeString(dpConfirmationMarketDeadline.getValue(), nV));
            showChange(btnConfirmationMarketDeadline,
                    currentDoc.getMtgNtfctn().getMtg().getAttndnc().getConfMktDdln().getDtOrDtTm().getDtTm().equals(
                            defaultDoc.getMtgNtfctn().getMtg().getAttndnc().getConfMktDdln().getDtOrDtTm().getDtTm()));
        });

        tfAdditionalDocumentationURLAddress.textProperty().addListener((o, oV, nV) -> {
            showChange(btnAdditionalDocumentationURLAddress,
                    nV.equals(defaultDoc.getMtgNtfctn().getMtg().getAddtlDcmnttnURLAdr().get(0)));
            currentDoc.getMtgNtfctn().getMtg().getAddtlDcmnttnURLAdr().set(0, nV);
        });

        btnAddAdditionalProcedureDetails.setOnAction(e -> {

            AdditionalRights3 value = new AdditionalRights3();
            value.setAddtlRght(new AdditionalRightCode1Choice());
            value.setAddtlRghtMktDdln(new DateFormat58Choice());
            value.getAddtlRghtMktDdln().setDtOrDtTm(new DateAndDateTime2Choice());
            value.getAddtlRghtMktDdln().getDtOrDtTm().setDtTm(LocalDateTime.now().toString());
            value.setAddtlRghtThrshld(new AdditionalRightThreshold1Choice());

            buildAdditionalProcedureDetails(value);
            currentDoc.getMtgNtfctn().getMtg().getAddtlPrcdrDtls().add(value);
        });
    }

    private void buildParticipation(ParticipationMethod2 value) {

        final Label lbl = new Label("Participation Method");
        final ComboBox<String> cb = new ComboBox<>();
        final Button btn = new Button("", new FontIcon(FontAwesomeSolid.MINUS));
        final DatePicker dp = new DatePicker();
        final TextField tf = new TextField();

        final Button btnParticipationMethod = new Button();
        configButton(btnParticipationMethod);

        final Button btnIssrDdlnForVtng = new Button();
        configButton(btnIssrDdlnForVtng);

        btnIssrDdlnForVtng.prefHeightProperty().bind(dp.heightProperty());

        final String proxyType = value.getPrtcptnMtd().getCd() == null ? null
                : VotingParticipationMethod3Code.toLabel(value.getPrtcptnMtd().getCd());
        cb.getItems().setAll(VotingParticipationMethod3Code.getLabels());
        cb.valueProperty().addListener((o, oV, nV) -> {
            value.getPrtcptnMtd().setCd(VotingParticipationMethod3Code.fromLabel(nV));
            showChange(btnParticipationMethod, nV.equals(proxyType));
        });
        cb.setValue(proxyType);
        cb.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(cb, Priority.ALWAYS);
        HBox.setMargin(cb, new Insets(0, 0, 5, 0));

        final GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(5);

        grid.add(new Label("IssrDdlnForVtng (Letzter Anmeldetag)"), 0, 0);

        final String issrDdlnForVtng = value.getIssrDdlnForVtng().getDtOrDtTm().getDtTm();
        dp.prefWidthProperty().bind(dpConfirmationMarketDeadline.widthProperty());
        dp.valueProperty().addListener((o, oV, nV) -> {
            value.getIssrDdlnForVtng().getDtOrDtTm().setDtTm(buildDateTimeString(nV, tf.getText()));
            showChange(btnIssrDdlnForVtng, value.getIssrDdlnForVtng().getDtOrDtTm().getDtTm().equals(issrDdlnForVtng));
        });
        dp.setValue(convertToDate(issrDdlnForVtng));
        grid.add(dp, 0, 1);
        GridPane.setHgrow(dp, Priority.ALWAYS);

        grid.add(new Label("Uhrzeit"), 1, 0);

        tf.textProperty().addListener((o, oV, nV) -> {
            value.getIssrDdlnForVtng().getDtOrDtTm().setDtTm(buildDateTimeString(dp.getValue(), nV.trim()));
            showChange(btnIssrDdlnForVtng, value.getIssrDdlnForVtng().getDtOrDtTm().getDtTm().equals(issrDdlnForVtng));
            btnIssrDdlnForVtng.setOpacity(
                    value.getIssrDdlnForVtng().getDtOrDtTm().getDtTm().trim().equals(issrDdlnForVtng.trim()) ? 0 : 1);
        });
        tf.setText(convertToTime(issrDdlnForVtng));
        tf.prefWidthProperty().bind(tfConfirmationMarketDeadlineTime.widthProperty());
        grid.add(tf, 1, 1);
        GridPane.setHgrow(tf, Priority.ALWAYS);
        VBox.setMargin(grid, new Insets(0, 0, 10, 0));

        grid.add(btnIssrDdlnForVtng, 2, 1);

        final HBox hBox = new HBox(10, cb, btn, btnParticipationMethod);
        final VBox vBox = new VBox(5, lbl, hBox, grid);
        boxParticipation.getChildren().add(vBox);

        btn.setOnAction(e -> {
            boxParticipation.getChildren().remove(vBox);
            currentDoc.getMtgNtfctn().getMtg().getPrtcptn().remove(value);
            btnAddParticipation.requestFocus();
        });
    }

    private void buildAdditionalProcedureDetails(AdditionalRights3 value) {

        final VBox box = new VBox(5);

        /*
         * ComboBox AdditionalRight
         */
        final ComboBox<String> cb = new ComboBox<>();
        final Button btnRemove = new Button("", new FontIcon(FontAwesomeSolid.MINUS));
        final Button btnAdditionalRight = new Button();
        configButton(btnAdditionalRight);

        final String additionalRight = value.getAddtlRght().getCd() == null ? null
                : AdditionalRight1Code.toLabel(value.getAddtlRght().getCd());
        cb.getItems().setAll(AdditionalRight1Code.getLabels());
        cb.valueProperty().addListener((o, oV, nV) -> {
            value.getAddtlRght().setCd(AdditionalRight1Code.fromLabel(nV));
            showChange(btnAdditionalRight, nV.equals(additionalRight));
        });
        cb.setValue(additionalRight);
        cb.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(cb, Priority.ALWAYS);
        HBox.setMargin(cb, new Insets(0, 0, 10, 0));
        box.getChildren().addAll(new Label("Additional Right"), new HBox(10, cb, btnRemove, btnAdditionalRight));

        /*
         * TextField AdditionalRightInformationURLAddress
         */
        final TextField tfAddtlRghtInfURLAdr = new TextField();
        final String addtlRghtInfURLAdr = value.getAddtlRghtInfURLAdr();
        final Button btnAddtlRghtInfURLAdr = new Button();
        configButton(btnAddtlRghtInfURLAdr);

        tfAddtlRghtInfURLAdr.textProperty().addListener((o, oV, nV) -> {
            value.setAddtlRghtInfURLAdr(nV);
            if (nV != null) { // TODO ?
                btnAddtlRghtInfURLAdr.setOpacity(nV.equals(addtlRghtInfURLAdr) ? 0 : 1);
            }
        });
        tfAddtlRghtInfURLAdr.setText(value.getAddtlRghtInfURLAdr());
        HBox.setMargin(tfAddtlRghtInfURLAdr, new Insets(0, 0, 10, 0));
        HBox.setHgrow(tfAddtlRghtInfURLAdr, Priority.ALWAYS);
        box.getChildren().addAll(new Label("AdditionalRightInformationURLAddress"),
                new HBox(10, tfAddtlRghtInfURLAdr, btnAddtlRghtInfURLAdr));

        /*
         * DatePicker + TextField AdditionalRightMarketDeadline
         */
        final DatePicker dp = new DatePicker();
        final TextField tf = new TextField();
        final Button btnAddtlRghtMktDdln = new Button();
        btnAddtlRghtMktDdln.prefHeightProperty().bind(dp.heightProperty());
        configButton(btnAddtlRghtMktDdln);

        final String issrDdlnForVtng = value.getAddtlRghtMktDdln().getDtOrDtTm().getDtTm();
        dp.prefWidthProperty().bind(dpConfirmationMarketDeadline.widthProperty());
        dp.valueProperty().addListener((o, oV, nV) -> {
            value.getAddtlRghtMktDdln().getDtOrDtTm().setDtTm(buildDateTimeString(nV, tf.getText()));
            showChange(btnAddtlRghtMktDdln,
                    value.getAddtlRghtMktDdln().getDtOrDtTm().getDtTm().equals(issrDdlnForVtng));
        });
        dp.setValue(convertToDate(issrDdlnForVtng));

        tf.textProperty().addListener((o, oV, nV) -> {
            value.getAddtlRghtMktDdln().getDtOrDtTm().setDtTm(buildDateTimeString(dp.getValue(), nV.trim()));
            showChange(btnAddtlRghtMktDdln,
                    value.getAddtlRghtMktDdln().getDtOrDtTm().getDtTm().trim().equals(issrDdlnForVtng.trim()));
        });
        tf.setText(convertToTime(issrDdlnForVtng));
        tf.prefWidthProperty().bind(tfConfirmationMarketDeadlineTime.widthProperty());

        final GridPane grid = new GridPane();
        VBox.setMargin(grid, new Insets(0, 0, 10, 0));
        grid.setHgap(10);
        grid.setVgap(5);

        grid.add(new Label("AdditionalRightMarketDeadline"), 0, 0);
        grid.add(dp, 0, 1);
        grid.add(new Label("Uhrzeit"), 1, 0);
        grid.add(tf, 1, 1);
        grid.add(btnAddtlRghtMktDdln, 2, 1);

        box.getChildren().add(grid);

        boxAdditionalProcedureDetails.getChildren().add(box);

        btnRemove.setOnAction(e -> {
            boxAdditionalProcedureDetails.getChildren().remove(box);
            currentDoc.getMtgNtfctn().getMtg().getAddtlPrcdrDtls().remove(value);
            btnAddAdditionalProcedureDetails.requestFocus();
        });

    }

    private void configProxyChoice() {

        dpMarketDeadline.valueProperty().addListener((o, oV, nV) -> {
            currentDoc.getMtgNtfctn().getMtg().getPrxyChc().getPrxy().getMktDdln().getDtOrDtTm()
                    .setDtTm(buildDateTimeString(nV, tfMarketDeadlineTime.getText()));
            showChange(btnMarketDeadline,
                    currentDoc.getMtgNtfctn().getMtg().getPrxyChc().getPrxy().getMktDdln().getDtOrDtTm().getDtTm()
                            .equals(defaultDoc.getMtgNtfctn().getMtg().getPrxyChc().getPrxy().getMktDdln().getDtOrDtTm()
                                    .getDtTm()));
        });

        tfMarketDeadlineTime.textProperty().addListener((o, oV, nV) -> {
            currentDoc.getMtgNtfctn().getMtg().getPrxyChc().getPrxy().getMktDdln().getDtOrDtTm()
                    .setDtTm(buildDateTimeString(dpMarketDeadline.getValue(), nV));
            showChange(btnMarketDeadline,
                    currentDoc.getMtgNtfctn().getMtg().getPrxyChc().getPrxy().getMktDdln().getDtOrDtTm().getDtTm()
                            .equals(defaultDoc.getMtgNtfctn().getMtg().getPrxyChc().getPrxy().getMktDdln().getDtOrDtTm()
                                    .getDtTm()));
        });

        btnAuthorisedProxy.setOnAction(e -> {

            Proxy11 value = new Proxy11();

            buildAuthorisedProxy(value);
            currentDoc.getMtgNtfctn().getMtg().getPrxyChc().getPrxy().getAuthrsdPrxy().add(value);
        });

        dpEntitlementFixingDate.valueProperty().addListener((o, oV, nV) -> {
            currentDoc.getMtgNtfctn().getMtg().getEntitlmntFxgDt().getDt().setDt(nV.toString());
            showChange(btnEntitlementFixingDate,
                    nV.toString().equals(defaultDoc.getMtgNtfctn().getMtg().getEntitlmntFxgDt().getDt().getDt()));

            if (checkRegnSctiesMktDdln.isSelected()) {
                final String regnSctiesMktDdln = nV.toString() + "T16:00:00.000Z";
                currentDoc.getMtgNtfctn().getMtg().getRegnSctiesMktDdln().getDtOrDtTm().setDtTm(regnSctiesMktDdln);
                tfRegnSctiesMktDdln.setText(regnSctiesMktDdln);
            }
        });

        final String securities = defaultDoc.getMtgNtfctn().getMtg().getSctiesBlckgPrdEndDt().getDtCd().getCd() == null
                ? null
                : DateType10Code.toLabel(defaultDoc.getMtgNtfctn().getMtg().getSctiesBlckgPrdEndDt().getDtCd().getCd());
        cbSecuritiesBlockingPeriod.getItems().setAll(DateType10Code.getLabels());
        cbSecuritiesBlockingPeriod.valueProperty().addListener((o, oV, nV) -> {
            currentDoc.getMtgNtfctn().getMtg().getSctiesBlckgPrdEndDt().getDtCd().setCd(DateType10Code.fromLabel(nV));
            showChange(btnSecuritiesBlockingPeriod, nV.equals(securities));
        });
        cbSecuritiesBlockingPeriod.setValue(securities);

        checkRegnSctiesMktDdln.selectedProperty().addListener((o, oV, nV) -> {
            if (nV) {
                if (dpEntitlementFixingDate.getValue() != null) {
                    final String regnSctiesMktDdln = dpEntitlementFixingDate.getValue().toString() + "T16:00:00.000Z";
                    currentDoc.getMtgNtfctn().getMtg().setRegnSctiesMktDdln(new DateFormat58Choice());
                    currentDoc.getMtgNtfctn().getMtg().getRegnSctiesMktDdln().setDtOrDtTm(new DateAndDateTime2Choice());
                    currentDoc.getMtgNtfctn().getMtg().getRegnSctiesMktDdln().getDtOrDtTm().setDtTm(regnSctiesMktDdln);
                    tfRegnSctiesMktDdln.setText(regnSctiesMktDdln);
                } else {
                    checkRegnSctiesMktDdln.setSelected(oV);
                }
            } else {
                currentDoc.getMtgNtfctn().getMtg().setRegnSctiesMktDdln(null);
                tfRegnSctiesMktDdln.setText(null);
            }
        });

        tfRegnSctiesMktDdln.textProperty().addListener((o, oV, nV) -> {
            String cur = nV == null ? null : nV;
            String def = defaultDoc.getMtgNtfctn().getMtg().getRegnSctiesMktDdln() == null ? null
                    : defaultDoc.getMtgNtfctn().getMtg().getRegnSctiesMktDdln().getDtOrDtTm().getDtTm();

            showChange(btnRegnSctiesMktDdln, cur == null ? null == def : cur.equals(def));
        });

    }

    //
    private void buildAuthorisedProxy(Proxy11 value) {

        final Label lbl = new Label("ProxyType");
        final ComboBox<String> cb = new ComboBox<>();
        final Button btn = new Button("", new FontIcon(FontAwesomeSolid.MINUS));

        final Button btnProxyType = new Button();
        configButton(btnProxyType);

        final VBox vBox = new VBox(5, lbl, new HBox(10, cb, btn, btnProxyType));

        boxAuthorisedProxy.getChildren().add(vBox);

        final String proxyType = value.getPrxyTp() == null ? null : ProxyType3Code.toLabel(value.getPrxyTp());
        cb.getItems().setAll(ProxyType3Code.getLabels());
        cb.valueProperty().addListener((o, oV, nV) -> {
            value.setPrxyTp(ProxyType3Code.fromLabel(nV));
            buildPersonDetails(vBox, !nV.equals(ProxyType3Code.toLabel(ProxyType3Code.DISC)), value);
            showChange(btnProxyType, nV.equals(proxyType));
        });
        cb.setValue(proxyType);
        cb.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(cb, Priority.ALWAYS);
        HBox.setMargin(cb, new Insets(0, 0, 10, 0));

        btn.setOnAction(e -> {
            boxAuthorisedProxy.getChildren().remove(vBox);
            currentDoc.getMtgNtfctn().getMtg().getPrxyChc().getPrxy().getAuthrsdPrxy().remove(value);
            btnAuthorisedProxy.requestFocus();
        });
    }

    private void buildPersonDetails(VBox box, Boolean build, Proxy11 value) {

        if (build && box.getChildren().size() < 5) {

            if (value.getPrsnDtls() == null) {
                value.setPrsnDtls(new IndividualPerson43());
                value.getPrsnDtls().setPrssgndPrxy(new PartyIdentification232Choice());
                value.getPrsnDtls().getPrssgndPrxy().setNtrlPrsn(new PartyIdentification238());
                value.getPrsnDtls().getPrssgndPrxy().getNtrlPrsn().setNmAndAdr(new PersonName3());
                value.getPrsnDtls().getPrssgndPrxy().getNtrlPrsn().setId(new NaturalPersonIdentification1());
            }

            final GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(5);

            grid.add(new Label("FirstName"), 0, 0);

            final TextField tfFirst = new TextField();
            final TextField tfSur = new TextField();
            final Button btnPerson = new Button();
            configButton(btnPerson);

            btnPerson.prefHeightProperty().bind(tfFirst.heightProperty());

            final String person = value.getPrsnDtls().getPrssgndPrxy().getNtrlPrsn().getNmAndAdr().getFrstNm()
                    + value.getPrsnDtls().getPrssgndPrxy().getNtrlPrsn().getNmAndAdr().getSrnm();

            tfFirst.textProperty().addListener((o, oV, nV) -> {
                value.getPrsnDtls().getPrssgndPrxy().getNtrlPrsn().getNmAndAdr().setFrstNm(nV);
                showChange(btnPerson, (nV + tfSur.getText()).equals(person));
            });
            tfFirst.setText(value.getPrsnDtls().getPrssgndPrxy().getNtrlPrsn().getNmAndAdr().getFrstNm());
            tfFirst.setMaxWidth(Double.MAX_VALUE);
            grid.add(tfFirst, 0, 1);
            GridPane.setHgrow(tfFirst, Priority.ALWAYS);

            grid.add(new Label("Surname"), 1, 0);

            tfSur.textProperty().addListener((o, oV, nV) -> {
                value.getPrsnDtls().getPrssgndPrxy().getNtrlPrsn().getNmAndAdr().setSrnm(nV);
                showChange(btnPerson, (tfFirst.getText() + nV).equals(person));
            });
            tfSur.setText(value.getPrsnDtls().getPrssgndPrxy().getNtrlPrsn().getNmAndAdr().getSrnm());
            tfSur.setMaxWidth(Double.MAX_VALUE);
            grid.add(tfSur, 1, 1);
            grid.add(btnPerson, 2, 1);
            GridPane.setHgrow(tfSur, Priority.ALWAYS);
            VBox.setMargin(grid, new Insets(0, 0, 10, 0));

            final TextField tfId = new TextField();
            final Button btnId = new Button();
            configButton(btnId);

            final String id = value.getPrsnDtls().getPrssgndPrxy().getNtrlPrsn().getId().getId();
            tfId.textProperty().addListener((o, oV, nV) -> {
                value.getPrsnDtls().getPrssgndPrxy().getNtrlPrsn().getId().setId(nV);
                if (nV != null) {
                    showChange(btnId, nV.equals(id));
                }
            });
            tfId.setText(value.getPrsnDtls().getPrssgndPrxy().getNtrlPrsn().getId().getId());
            tfId.setMaxWidth(Double.MAX_VALUE);
            VBox.setMargin(tfId, new Insets(0, 0, 10, 0));
            HBox.setHgrow(tfId, Priority.ALWAYS);

            box.getChildren().addAll(grid, new Label("Identification"), new HBox(10, tfId, btnId));

        } else if (!build && box.getChildren().size() == 5) {

            box.getChildren().remove(4);
            box.getChildren().remove(3);
            box.getChildren().remove(2);
        }
    }

    private void configMeetingDetails() {

        dpMeetingDate.valueProperty().addListener((o, oV, nV) -> {
            currentDoc.getMtgNtfctn().getMtgDtls().get(0).getDtAndTm().getDtOrDtTm()
                    .setDtTm(buildDateTimeString(nV, tfMeetingTime.getText()));
            showChange(btnMeetingDate,
                    currentDoc.getMtgNtfctn().getMtgDtls().get(0).getDtAndTm().getDtOrDtTm().getDtTm().equals(
                            defaultDoc.getMtgNtfctn().getMtgDtls().get(0).getDtAndTm().getDtOrDtTm().getDtTm()));
        });

        tfMeetingTime.textProperty().addListener((o, oV, nV) -> {
            currentDoc.getMtgNtfctn().getMtgDtls().get(0).getDtAndTm().getDtOrDtTm()
                    .setDtTm(buildDateTimeString(dpMeetingDate.getValue(), nV));
            showChange(btnMeetingDate,
                    currentDoc.getMtgNtfctn().getMtgDtls().get(0).getDtAndTm().getDtOrDtTm().getDtTm().equals(
                            defaultDoc.getMtgNtfctn().getMtgDtls().get(0).getDtAndTm().getDtOrDtTm().getDtTm()));
        });

        cbDateStatus.getItems().setAll(MeetingDateStatus2Code.getLabels());
        cbDateStatus.valueProperty().addListener((o, oV, nV) -> {
            currentDoc.getMtgNtfctn().getMtgDtls().get(0).setDtSts(MeetingDateStatus2Code.fromLabel(nV));
            showChange(btnDateStatus, nV
                    .equals(MeetingDateStatus2Code.toLabel(defaultDoc.getMtgNtfctn().getMtgDtls().get(0).getDtSts())));
        });

        cbMeetingAddressType.getItems().setAll(AddressType2Code.getLabels());
        cbMeetingAddressType.valueProperty().addListener((o, oV, nV) -> {
            currentDoc.getMtgNtfctn().getMtgDtls().get(0).getLctn().get(0).getAdr()
                    .setAdrTp(AddressType2Code.fromLabel(nV));
            showChange(btnMeetingAddressType, nV.equals(AddressType2Code
                    .toLabel(defaultDoc.getMtgNtfctn().getMtgDtls().get(0).getLctn().get(0).getAdr().getAdrTp())));
        });

        /*
         * TODO Adresszeile nicht in Liste == null
         */
        tfAddressLine1.textProperty().addListener((o, oV, nV) -> {
            final int idx = 0;
            if (nV != null && !nV.isBlank())
                currentDoc.getMtgNtfctn().getMtgDtls().get(0).getLctn().get(0).getAdr().getAdrLine().set(idx, nV);
            else if (idx >= currentDoc.getMtgNtfctn().getMtgDtls().get(0).getLctn().get(0).getAdr().getAdrLine().size())
                currentDoc.getMtgNtfctn().getMtgDtls().get(0).getLctn().get(0).getAdr().getAdrLine().remove(idx);
            showChange(btnAddressLine1, nV.equals(
                    defaultDoc.getMtgNtfctn().getMtgDtls().get(0).getLctn().get(0).getAdr().getAdrLine().get(idx)));
        });
        //        tfAddressLine1.setText(defLoc.getAdrLine().get(0));

        //        tfAddressLine2.textProperty().addListener((o, oV, nV) -> {
        //            final int idx = 1;
        //            if (nV != null && !nV.isBlank())
        //                curLoc.getAdrLine().set(idx, nV);
        //            else if (idx >= curLoc.getAdrLine().size())
        //                curLoc.getAdrLine().remove(idx);
        //            showChange(btnAddressLine2, nV.equals(defLoc.getAdrLine().get(idx)));
        //        });
        //        tfAddressLine2.setText(defLoc.getAdrLine().get(1));
        //
        //        tfAddressLine3.textProperty().addListener((o, oV, nV) -> {
        //            final int idx = 2;
        //            if (nV != null && !nV.isBlank())
        //                curLoc.getAdrLine().set(idx, nV);
        //            else if (idx >= curLoc.getAdrLine().size())
        //                curLoc.getAdrLine().remove(idx);
        //            showChange(btnAddressLine3, nV.equals(defLoc.getAdrLine().get(idx)));
        //        });
        //        tfAddressLine3.setText(defLoc.getAdrLine().get(2));

        tfMeetingStreet.textProperty().addListener((o, oV, nV) -> {
            currentDoc.getMtgNtfctn().getMtgDtls().get(0).getLctn().get(0).getAdr().setStrtNm(nV);
            showChange(btnMeetingStreet,
                    nV.equals(defaultDoc.getMtgNtfctn().getMtgDtls().get(0).getLctn().get(0).getAdr().getStrtNm()));
        });

        tfMeetingBuilding.textProperty().addListener((o, oV, nV) -> {
            currentDoc.getMtgNtfctn().getMtgDtls().get(0).getLctn().get(0).getAdr().setBldgNb(nV);
            showChange(btnMeetingBuilding,
                    nV.equals(defaultDoc.getMtgNtfctn().getMtgDtls().get(0).getLctn().get(0).getAdr().getBldgNb()));
        });

        tfMeetingPost.textProperty().addListener((o, oV, nV) -> {
            currentDoc.getMtgNtfctn().getMtgDtls().get(0).getLctn().get(0).getAdr().setPstCd(nV);
            showChange(btnMeetingPost,
                    nV.equals(defaultDoc.getMtgNtfctn().getMtgDtls().get(0).getLctn().get(0).getAdr().getPstCd()));
        });

        tfMeetingTown.textProperty().addListener((o, oV, nV) -> {
            currentDoc.getMtgNtfctn().getMtgDtls().get(0).getLctn().get(0).getAdr().setTwnNm(nV);
            showChange(btnMeetingTown,
                    nV.equals(defaultDoc.getMtgNtfctn().getMtgDtls().get(0).getLctn().get(0).getAdr().getTwnNm()));
        });

        tfMeetingCountry.textProperty().addListener((o, oV, nV) -> {
            currentDoc.getMtgNtfctn().getMtgDtls().get(0).getLctn().get(0).getAdr().setCtry(nV);
            showChange(btnMeetingCountry,
                    nV.equals(defaultDoc.getMtgNtfctn().getMtgDtls().get(0).getLctn().get(0).getAdr().getCtry()));
        });

    }

    private void configIssuer() {

        tfIssuerName.textProperty().addListener((o, oV, nV) -> {
            currentDoc.getMtgNtfctn().getIssr().getId().getNmAndAdr().setNm(nV);
            showChange(btnIssuerName, nV.equals(defaultDoc.getMtgNtfctn().getIssr().getId().getNmAndAdr().getNm()));
        });

        cbIssuerAddressType.getItems().setAll(AddressType2Code.getLabels());
        cbIssuerAddressType.valueProperty().addListener((o, oV, nV) -> {
            currentDoc.getMtgNtfctn().getIssr().getId().getNmAndAdr().getAdr().setAdrTp(AddressType2Code.fromLabel(nV));
            showChange(btnIssuerAddressType, nV.equals(AddressType2Code
                    .toLabel(defaultDoc.getMtgNtfctn().getIssr().getId().getNmAndAdr().getAdr().getAdrTp())));
        });

        tfIssuerStreet.textProperty().addListener((o, oV, nV) -> {
            currentDoc.getMtgNtfctn().getIssr().getId().getNmAndAdr().getAdr().setStrtNm(nV);
            showChange(btnIssuerStreet,
                    nV.equals(defaultDoc.getMtgNtfctn().getIssr().getId().getNmAndAdr().getAdr().getStrtNm()));
        });

        tfIssuerBuilding.textProperty().addListener((o, oV, nV) -> {
            currentDoc.getMtgNtfctn().getIssr().getId().getNmAndAdr().getAdr().setBldgNb(nV);
            showChange(btnIssuerBuilding,
                    nV.equals(defaultDoc.getMtgNtfctn().getIssr().getId().getNmAndAdr().getAdr().getBldgNb()));
        });

        tfIssuerPost.textProperty().addListener((o, oV, nV) -> {
            currentDoc.getMtgNtfctn().getIssr().getId().getNmAndAdr().getAdr().setPstCd(nV);
            showChange(btnIssuerPost,
                    nV.equals(defaultDoc.getMtgNtfctn().getIssr().getId().getNmAndAdr().getAdr().getPstCd()));
        });

        tfIssuerTown.textProperty().addListener((o, oV, nV) -> {
            currentDoc.getMtgNtfctn().getIssr().getId().getNmAndAdr().getAdr().setTwnNm(nV);
            showChange(btnIssuerTown,
                    nV.equals(defaultDoc.getMtgNtfctn().getIssr().getId().getNmAndAdr().getAdr().getTwnNm()));
        });

        tfIssuerCountry.textProperty().addListener((o, oV, nV) -> {
            currentDoc.getMtgNtfctn().getIssr().getId().getNmAndAdr().getAdr().setCtry(nV);
            showChange(btnIssuerCountry,
                    nV.equals(defaultDoc.getMtgNtfctn().getIssr().getId().getNmAndAdr().getAdr().getCtry()));
        });
    }

    private void configIssuerAgent() {

        tfIssuerAgentName.textProperty().addListener((o, oV, nV) -> {
            currentDoc.getMtgNtfctn().getIssrAgt().get(0).getId().getNmAndAdr().setNm(nV);
            showChange(btnIssuerAgentName,
                    nV.equals(defaultDoc.getMtgNtfctn().getIssrAgt().get(0).getId().getNmAndAdr().getNm()));
        });

        cbIssuerAgentRole.getItems().setAll(AgentRole1Code.getLabels());
        cbIssuerAgentRole.valueProperty().addListener((o, oV, nV) -> {
            currentDoc.getMtgNtfctn().getIssrAgt().get(0).setRole(AgentRole1Code.fromLabel(nV));
            showChange(btnIssuerAgentRole,
                    nV.equals(AgentRole1Code.toLabel(defaultDoc.getMtgNtfctn().getIssrAgt().get(0).getRole())));
        });
    }

    private void configSecurity() {
        tfIsin.textProperty()
                .addListener((o, oV, nV) -> currentDoc.getMtgNtfctn().getScty().get(0).getFinInstrmId().setISIN(nV));
    }

    private void buildResolution(Resolution7 curRes, Resolution7 defRes) {

        final int idx = defaultDoc.getMtgNtfctn().getRsltn().indexOf(defRes);

        if (idx >= 0) {
            currentDoc.getMtgNtfctn().getRsltn().set(idx, curRes);
        } else {
            currentDoc.getMtgNtfctn().getRsltn().add(curRes);
        }

        /*
         * Initialize Nodes
         */
        final VBox vBox = new VBox(5);
        final TextField tfIssuerLabel = new TextField();
        final String issuerLabel = defRes.getIssrLabl();
        final Button btnIssuerLabel = new Button();

        /*
         * Config Layout 
         */
        HBox.setHgrow(tfIssuerLabel, Priority.ALWAYS);
        configButton(btnIssuerLabel);

        /*
         * Config Listener
         */
        tfIssuerLabel.textProperty().addListener((o, oV, nV) -> {
            defRes.setIssrLabl(nV);
            showChange(btnIssuerLabel, nV.equals(issuerLabel));
        });

        /*
         * Set Value
         */
        tfIssuerLabel.setText(curRes.getIssrLabl());

        /*
         * Section Add
         */
        vBox.getChildren().add(new Label("Issuer Label"));
        vBox.getChildren().add(new HBox(10, tfIssuerLabel, btnIssuerLabel));

        /*
         * 
         */

        final TextField tfLangDE = new TextField();
        final TextArea taTitleDE = new TextArea();
        final String titleDE = defRes.getDesc().get(0).getTitl();
        final Button btnTitleDE = new Button();
        configButton(btnTitleDE);

        tfLangDE.setMaxWidth(50);
        GridPane.setValignment(tfLangDE, VPos.CENTER);

        taTitleDE.setMaxHeight(55);
        taTitleDE.setWrapText(true);
        GridPane.setHgrow(taTitleDE, Priority.ALWAYS);

        ItemDescription2 curDescDE = curRes.getDesc().get(0);

        tfLangDE.textProperty().addListener((o, oV, nV) -> {
            curDescDE.setLang(nV);
        });
        tfLangDE.setText(curDescDE.getLang());
        tfLangDE.setMaxWidth(50);

        taTitleDE.textProperty().addListener((o, oV, nV) -> {
            curDescDE.setTitl(nV);
            showChange(btnTitleDE, nV.equals(titleDE));
        });
        taTitleDE.setText(curDescDE.getTitl());

        GridPane grid = new GridPane();
        VBox.setMargin(grid, new Insets(0, 0, 0, 10));
        grid.setHgap(10);
        grid.setVgap(5);

        grid.add(new Label("Language"), 0, 0);
        grid.add(tfLangDE, 0, 1);
        grid.add(new Label("Title"), 1, 0);
        grid.add(taTitleDE, 1, 1);
        grid.add(btnTitleDE, 2, 1);

        vBox.getChildren().add(grid);

        final TextField tfLangEN = new TextField();
        final TextArea taTitleEN = new TextArea();
        final String titleEN = defRes.getDesc().get(1).getTitl();
        final Button btnTitleEN = new Button();
        configButton(btnTitleEN);

        tfLangEN.setMaxWidth(50);
        GridPane.setValignment(tfLangEN, VPos.CENTER);

        taTitleEN.setMaxHeight(55);
        taTitleEN.setWrapText(true);
        GridPane.setHgrow(taTitleEN, Priority.ALWAYS);

        ItemDescription2 curDescEN = curRes.getDesc().get(1);

        tfLangEN.textProperty().addListener((o, oV, nV) -> {
            curDescEN.setLang(nV);
        });
        tfLangEN.setText(curDescEN.getLang());

        taTitleEN.textProperty().addListener((o, oV, nV) -> {
            curDescEN.setTitl(nV);
            showChange(btnTitleEN, nV.equals(titleEN));
            btnTitleEN.setOpacity(nV.equals(titleEN) ? 0 : 1);
        });
        taTitleEN.setText(curDescEN.getTitl());

        grid = new GridPane();
        VBox.setMargin(grid, new Insets(0, 0, 0, 10));
        grid.setHgap(10);
        grid.setVgap(5);

        grid.add(new Label("Language"), 0, 0);
        grid.add(tfLangEN, 0, 1);
        grid.add(new Label("Title"), 1, 0);
        grid.add(taTitleEN, 1, 1);
        grid.add(btnTitleEN, 2, 1);

        vBox.getChildren().add(grid);

        /*
         * RadioButtons VotingTypes
         */
        final Button btnTypes = new Button();
        configButton(btnTypes);

        HBox hBox = new HBox(50);
        HBox.setHgrow(hBox, Priority.ALWAYS);
        hBox.setAlignment(Pos.CENTER_LEFT);

        Map<String, RadioButton> map = new HashMap<>();

        Button btnAll = new Button("Alle");
        btnAll.setPrefWidth(100);
        btnAll.setOnAction(e -> map.entrySet().forEach(x -> x.getValue().setSelected(!x.getValue().isSelected())));

        hBox.getChildren().add(btnAll);

        for (String str : VoteInstruction6Code.getLabels()) {

            RadioButton radio = new RadioButton(str);
            map.put(str, radio);
            radio.setPrefWidth(100);
            radio.selectedProperty().addListener((o, oV, nV) -> {

                VoteInstructionType1 type = new VoteInstructionType1();
                type.setVoteInstrTpCd(new VoteInstructionType1Choice());
                type.getVoteInstrTpCd().setTp(VoteInstruction6Code.fromLabel(radio.getText()));

                List<VoteInstructionType1> curVoteInstrTp = curRes.getVoteInstrTp();

                if (nV) {
                    addVoteInstrTypes(curVoteInstrTp, type);
                } else {
                    removeVoteInstrTypes(curVoteInstrTp, type);
                }
                showChange(btnTypes, voteInstrTypes(curVoteInstrTp).equals(voteInstrTypes(defRes.getVoteInstrTp())));
            });

            hBox.getChildren().add(radio);
        }
        for (var x : defRes.getVoteInstrTp())
            map.get(VoteInstruction6Code.toLabel(x.getVoteInstrTpCd().getTp())).setSelected(true);

        HBox tmpBox = new HBox(10, hBox, btnTypes);

        /*
         * CheckBox For Information Only
         */
        final CheckBox checkForInformationOnly = new CheckBox("For Information Only");
        final Boolean forInformationOnly = defRes.isForInfOnly();
        final Button btnForInformationOnly = new Button();

        configButton(btnForInformationOnly);

        checkForInformationOnly.selectedProperty().addListener((o, oV, nV) -> {
            curRes.setForInfOnly(nV);
            showChange(btnForInformationOnly, nV.equals(forInformationOnly));

            for (var entry : map.entrySet()) {
                if (nV) {
                    entry.getValue().setSelected(false);
                    entry.getValue().setDisable(true);
                    btnAll.setDisable(true);
                } else {
                    entry.getValue().setDisable(false);
                    btnAll.setDisable(false);
                }
            }
        });
        checkForInformationOnly.setSelected(defRes.isForInfOnly());
        checkForInformationOnly.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(checkForInformationOnly, Priority.ALWAYS);

        HBox box = new HBox(10, checkForInformationOnly, btnForInformationOnly);
        box.setAlignment(Pos.CENTER_LEFT);

        vBox.getChildren().add(box);

        /*
         * ComboBox VoteType
         */
        final ComboBox<String> cbVoteType = new ComboBox<>();
        final String voteType = defRes.getVoteTp() == null ? null : VoteType1Code.toLabel(defRes.getVoteTp());
        final Button btnVoteType = new Button();

        configButton(btnVoteType);

        cbVoteType.getItems().setAll(VoteType1Code.getLabels());
        cbVoteType.valueProperty().addListener((o, oV, nV) -> {
            curRes.setVoteTp(VoteType1Code.fromLabel(nV));
            showChange(btnVoteType, nV.equals(voteType));
        });
        cbVoteType.setValue(voteType);
        cbVoteType.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(cbVoteType, Priority.ALWAYS);

        vBox.getChildren().add(new Label("Vote Type"));
        vBox.getChildren().add(new HBox(10, cbVoteType, btnVoteType));

        /*
         * ComboBox Status
         */
        final ComboBox<String> cbStatus = new ComboBox<>();
        final String status = ResolutionStatus1Code.toLabel(defRes.getSts());
        final Button btnStatus = new Button();

        configButton(btnStatus);

        cbStatus.getItems().setAll(ResolutionStatus1Code.getLabels());
        cbStatus.valueProperty().addListener((o, oV, nV) -> {
            curRes.setSts(ResolutionStatus1Code.fromLabel(nV));
            showChange(btnStatus, nV.equals(status));
        });
        cbStatus.setValue(status);
        cbStatus.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(cbStatus, Priority.ALWAYS);

        vBox.getChildren().add(new Label("Status"));
        vBox.getChildren().add(new HBox(10, cbStatus, btnStatus));

        HBox.setMargin(cbStatus, new Insets(0, 0, 10, 0));
        VBox.setMargin(tmpBox, new Insets(0, 0, 10, 0));

        vBox.getChildren().add(tmpBox);

        boxResolution.getChildren().add(vBox);

    }

    private void configVote() {

        checkPartialVoteAllowed.selectedProperty().addListener((o, oV, nV) -> {
            currentDoc.getMtgNtfctn().getVote().setPrtlVoteAllwd(nV);
            showChange(btnPartialVoteAllowed, nV.equals(defaultDoc.getMtgNtfctn().getVote().isPrtlVoteAllwd()));
        });

        checkSplitVoteAllowed.selectedProperty().addListener((o, oV, nV) -> {
            currentDoc.getMtgNtfctn().getVote().setSpltVoteAllwd(nV);
            showChange(btnSplitVoteAllowed, nV.equals(defaultDoc.getMtgNtfctn().getVote().isSpltVoteAllwd()));
        });

        dpVoteMarketDeadline.valueProperty().addListener((o, oV, nV) -> {
            currentDoc.getMtgNtfctn().getVote().getVoteMktDdln().getDtOrDtTm()
                    .setDtTm(buildDateTimeString(nV, tfVoteMarketDeadlineTime.getText()));
            showChange(btnVoteMarketDeadline, currentDoc.getMtgNtfctn().getVote().getVoteMktDdln().getDtOrDtTm()
                    .getDtTm().equals(defaultDoc.getMtgNtfctn().getVote().getVoteMktDdln().getDtOrDtTm().getDtTm()));
        });

        tfVoteMarketDeadlineTime.textProperty().addListener((o, oV, nV) -> {
            currentDoc.getMtgNtfctn().getVote().getVoteMktDdln().getDtOrDtTm()
                    .setDtTm(buildDateTimeString(dpVoteMarketDeadline.getValue(), nV));
            showChange(btnVoteMarketDeadline, currentDoc.getMtgNtfctn().getVote().getVoteMktDdln().getDtOrDtTm()
                    .getDtTm().equals(defaultDoc.getMtgNtfctn().getVote().getVoteMktDdln().getDtOrDtTm().getDtTm()));
        });

        currentDoc.getMtgNtfctn().getVote().getVoteMthds().getVoteThrghNtwk().setVoteChanl(null);

        cbVoteChannel.getItems().setAll(VoteChannel1Code.getLabels());
        cbVoteChannel.valueProperty().addListener((o, oV, nV) -> {
            currentDoc.getMtgNtfctn().getVote().getVoteMthds().getVoteThrghNtwk()
                    .setVoteChanl(VoteChannel1Code.fromLabel(nV));
            showChange(btnVoteChannel, nV.equals(VoteChannel1Code
                    .toLabel(defaultDoc.getMtgNtfctn().getVote().getVoteMthds().getVoteThrghNtwk().getVoteChanl())));
        });

        checkBeneficialOwnerDisclosure.selectedProperty().addListener((o, oV, nV) -> {
            currentDoc.getMtgNtfctn().getVote().setBnfclOwnrDsclsr(nV);
            showChange(btnBeneficialOwnerDisclosure,
                    nV.equals(defaultDoc.getMtgNtfctn().getVote().isBnfclOwnrDsclsr()));
        });
    }

    private void jumpTo(Button btn, Node node) {
        btn.setOnMouseEntered(e -> eigeneStage.getScene().setCursor(Cursor.HAND));
        btn.setOnMouseExited(e -> eigeneStage.getScene().setCursor(Cursor.DEFAULT));
        btn.setOnAction(e -> {
            node.requestFocus();
            Robot robot = new Robot();
            robot.keyPress(KeyCode.TAB);
        });
    }

    @SuppressWarnings("unchecked")
    private void readDoc(EclSRDMessage message) {

        try {

            JAXBContext jaxbContext = JAXBContext.newInstance(Document.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            JAXBElement<Document> defaultRoot = (JAXBElement<Document>) jaxbUnmarshaller
                    .unmarshal(new StringReader(message.getDocument()));

            JAXBElement<Document> currentRoot = (JAXBElement<Document>) jaxbUnmarshaller
                    .unmarshal(new StringReader(message.getDocument()));

            defaultDoc = defaultRoot.getValue();
            currentDoc = currentRoot.getValue();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void checkDefault() {

        dbBundle.dbSRDMessage.createTable();

        for (EclIsin isin : isinList) {
            List<EclSRDMessage> tmpList = dbBundle.dbSRDMessage.readByIsin(isin.isin);

            if (tmpList.isEmpty()) {
                dbBundle.dbSRDMessage.insertDefault(isin.isin);
            }
        }
    }

    private void showChange(Button btn, Boolean check) {
        if (check) {
            btn.setGraphic(null);
        } else {
            btn.setGraphic(new FontIcon(FontAwesomeSolid.SYNC));
            changeButtons.add(btn);
        }
    }

    private LocalDate convertToDate(String localDateTime) {
        return localDateTime == null ? null : LocalDate.parse(localDateTime.substring(0, 10));
    }

    private String convertToTime(String localDateTime) {
        return localDateTime == null ? null : localDateTime.substring(11, 19);
    }

    private String buildDateTimeString(LocalDate date, String time) {
        // TODO Convert to UTC
        return date.toString() + "T" + time + ".000Z";
    }

    private void configButton(Button btn) {
        btn.setPrefWidth(25);
        btn.setStyle("-fx-background-color: none");
        btn.setFocusTraversable(false);
    }

    private void addVoteInstrTypes(List<VoteInstructionType1> list, VoteInstructionType1 type) {
        if (!voteInstrTypes(list).contains(type.getVoteInstrTpCd().getTp().name()))
            list.add(type);
    }

    private void removeVoteInstrTypes(List<VoteInstructionType1> list, VoteInstructionType1 type) {
        for (var l : list) {
            if (l.getVoteInstrTpCd().getTp().equals(type.getVoteInstrTpCd().getTp())) {
                list.remove(l);
                break;
            }
        }
    }

    private String voteInstrTypes(List<VoteInstructionType1> list) {
        Set<String> set = new HashSet<>();

        for (var l : list) {
            set.add(l.getVoteInstrTpCd().getTp().name());
        }
        return Arrays.toString(set.toArray());
    }

    private Resolution7 createResolution7() {
        final Resolution7 res = new Resolution7();
        res.getDesc().add(0, new ItemDescription2());
        res.getDesc().add(1, new ItemDescription2());

        return res;
    }

    private Resolution7 defResolution7(int idx) {

        if (idx >= defaultDoc.getMtgNtfctn().getRsltn().size() || idx < 0) {
            return createResolution7();
        } else {
            return defaultDoc.getMtgNtfctn().getRsltn().get(idx);
        }
    }

    private void readAgenda() {
        // TODO TO einlesen

        dbBundle = new DbBundle();
        dbBundle.openAll();

        BlAbstimmung blAbstimmung = new BlAbstimmung(dbBundle);
        blAbstimmung.leseAktivenAbstimmungsblock();

        agenda = new Resolution7[blAbstimmung.abstimmungen.length];

        for (int i = 0; i < blAbstimmung.abstimmungen.length; i++) {
            final EclAbstimmung top = blAbstimmung.abstimmungen[i];
            final Resolution7 res = createResolution7();
            // TODO IssuerLabel zusammenbauen?
            res.setIssrLabl(top.nummerFormular + top.nummerindexFormular);
            res.getDesc().get(0).setLang("de");
            res.getDesc().get(0).setTitl(top.anzeigeBezeichnungLang);
            res.getDesc().get(1).setLang("en");
            res.getDesc().get(1).setTitl(top.anzeigeBezeichnungLangEN);

            agenda[i] = res;
        }
        dbBundle.closeAll();
    }

    private void onChange() {

        dbBundle.openAll();
        dbBundle.openWeitere();
        List<EclSRDMessage> tmpList = dbBundle.dbSRDMessage.readByIsin(cbIsin.getValue());

        dbBundle.closeAll();

        for (var value : tmpList) {
            if (value.getSent_at() == null)
                return;

        }
        if (!CustomAlert.confirmAlert("Bitte Bestätigen", "Nachricht ändern?",
                "Soll eine Replace Nachricht erstellt werden?"))
            return;

        //  @formatter:off
        final String replacePart = "</NtfctnGnlInf><Mtg>";
        final String updatePart = ""
                + "</NtfctnGnlInf>"
                + "    <NtfctnUpd>"
                + "        <PrvsNtfctnId>" + srdMessage.getMessageId() + "</PrvsNtfctnId>"
                + "        <RcnfrmInstrs>false</RcnfrmInstrs>"
                + "    </NtfctnUpd>"
                + "<Mtg>";
        //  @formatter:on

        srdMessage.setAppHeader(dbBundle.dbSRDMessage.defaultAppHeader);
        srdMessage.setDocument(
                srdMessage.getDocument().replace("<NtfctnTp>NEWM</NtfctnTp>", "<NtfctnTp>REPL</NtfctnTp>"));
        srdMessage.setDocument(srdMessage.getDocument().replace(replacePart, updatePart));
        srdMessage.setPrevMessageId(srdMessage.getMessageId());
        srdMessage.setMessageId(null);
        srdMessage.setSent_at(null);

        dbBundle.openAll();
        dbBundle.openWeitere();

        dbBundle.dbSRDMessage.insert(srdMessage);
        messageList = dbBundle.dbSRDMessage.readAll();
        tmpList = dbBundle.dbSRDMessage.readByIsin(cbIsin.getValue());
        dbBundle.closeAll();

        boxHistory.getChildren().clear();

        for (EclSRDMessage value : tmpList)
            createBox(value);

    }

    private void createBox(EclSRDMessage message) {

        final Boolean sent = message.getSent_at() != null;
        final FontIcon icon = new FontIcon(sent ? FontAwesomeBrands.TELEGRAM_PLANE : FontAwesomeRegular.EDIT);
        final Button btn = new Button("Nachricht: " + (boxHistory.getChildren().size() + 1), icon);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setId(message.getId() + "");

        btn.setOnAction(e -> {
            srdMessage = messageList.stream().filter(x -> x.getId() == Integer.valueOf(btn.getId())).findFirst()
                    .orElse(null);

            configIsin(srdMessage);

            ObjectActions.styleSelectedButton(btn, selectedButton);
            selectedButton = btn;

        });
        boxHistory.getChildren().add(btn);
    }

    @FXML
    private void onResponse() {

        if (srdMessage != null && srdMessage.getResponseMessage() != null) {

            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Antwort von ProxyLieferant01");
            alert.setHeaderText("Antwort von ProxyLieferant01");

            TextArea ta = new TextArea(srdMessage.getResponseMessage().replace("><", ">\n<"));

            alert.getDialogPane().setContent(ta);

            alert.showAndWait();

        }
    }

    @FXML
    private void onCreateFile() {

        final DirectoryChooser chooser = new DirectoryChooser();
        chooser.setInitialDirectory(new File(System.getProperty("user.home") + "\\Desktop\\"));
        final File f = chooser.showDialog(eigeneStage);

        CaDateiWrite writer = new CaDateiWrite();

        System.out.println(f.toString() + "\\TEST-XML.txt");

        writer.oeffneNameExplizitOhneBundle(f.toString() + "\\TEST-XML.txt");
        writer.ausgabePlain(srdMessage.getAppHeader().trim() + srdMessage.getDocument().trim()
                + "</Document></Envelope></RequestPayload>");
        writer.schliessen();

    }

    @FXML
    private void onParameter() {

        tfAddressLine1.setText(ParamS.eclEmittent.veranstaltungGebäude);
        tfMeetingStreet.setText(ParamS.eclEmittent.veranstaltungStrasse);
        tfMeetingBuilding.setText("");
        tfMeetingPost.setText(ParamS.eclEmittent.veranstaltungPostleitzahl);
        tfMeetingTown.setText(ParamS.eclEmittent.hvOrt);
        EclStaaten tmpStaat = staatenList.stream().filter(e -> e.getId() == ParamS.eclEmittent.veranstaltungStaatId)
                .findFirst().orElse(null);
        tfMeetingCountry.setText(tmpStaat == null ? "DE" : tmpStaat.getCode());

        tfIssuerName.setText(ParamS.eclEmittent.bezeichnungKurz);
        tfIssuerStreet.setText(ParamS.eclEmittent.strasseGesellschaft);
        tfIssuerBuilding.setText("");
        tfIssuerPost.setText(ParamS.eclEmittent.postleitzahl);
        tfIssuerTown.setText(ParamS.eclEmittent.ort);
        tmpStaat = staatenList.stream().filter(e -> e.getId() == ParamS.eclEmittent.staatId).findFirst().orElse(null);
        tfIssuerCountry.setText(tmpStaat == null ? "DE" : tmpStaat.getCode());

        readAgenda();

        boxResolution.getChildren().clear();

        for (int i = 0; i < agenda.length; i++)
            buildResolution(agenda[i], defResolution7(i));

    }

    @FXML
    private void onSave() {

        srdMessage.setDocument(CaXML.marshalAndPrepare(currentDoc));

        dbBundle.openAll();
        dbBundle.openWeitere();
        dbBundle.dbSRDMessage.update(srdMessage);
        dbBundle.closeAll();

        changeButtons.forEach(x -> x.setGraphic(null));
        changeButtons.clear();

        configIsin(srdMessage);
    }

    private void buildSend() {

        dbBundle.openAll();
        dbBundle.openWeitere();

        final String bizMsgIdr = srdMessage.getMessageId() != null ? srdMessage.getMessageId()
                : dbBundle.dbMessageId.insert(idLength);
        srdMessage.setMessageId(bizMsgIdr);

        final LocalDateTime sent_at = LocalDateTime.now();

        srdMessage.replaceAppHeader(bizMsgIdr, srdMessage.getSeev(), sent_at.format(formatter));

        final String meetingId = srdMessage.getMeetingId() != null ? srdMessage.getMeetingId()
                : dbBundle.dbMessageId.insert(idLength);
        srdMessage.setMeetingId(meetingId);

        srdMessage.setIssuerMeetingId(currentDoc.getMtgNtfctn().getMtg().getIssrMtgId());
        srdMessage.setMtgDtAndTime(currentDoc.getMtgNtfctn().getMtgDtls().get(0).getDtAndTm().getDtOrDtTm().getDtTm());
        srdMessage.setType(currentDoc.getMtgNtfctn().getMtg().getTp().name());

        srdMessage.setDocument(srdMessage.getDocument().replace("<MtgId></MtgId>", "<MtgId>" + meetingId + "</MtgId>"));

        final String message = srdMessage.proximityMessage();

        final CaProxyLieferant01 send = new CaProxyLieferant01();
        final RespMessage resp = send.postMessage(message);

        final String respString = CaXML.marshal(resp);

        if (respString != null) {

            final String errorText = CaString.searchBetween(respString, "<ErrorText>", "</ErrorText>");

            if (errorText == null) {

                srdMessage.setResponseMessage(CaXML.removeBlanks(respString));
                srdMessage.setSent_at(Timestamp.valueOf(sent_at));

                Platform.runLater(() -> {
                    selectedButton.setGraphic(new FontIcon(FontAwesomeBrands.TELEGRAM_PLANE));
                    setSentTime(Timestamp.valueOf(sent_at));
                });
                ObjectActions.showNotification(notification, "Nachricht gesendet.");

            } else {
                ObjectActions.showNotification(notification, "Senden fehlgeschlagen.");
                CaBug.druckeLog(respString, 10, 10);
            }
            dbBundle.dbSRDMessage.update(srdMessage);
        }
        dbBundle.closeAll();
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
        if (CustomAlert.confirmAlert("Nachricht senden", "Nachricht wirklich senden?", ""))
            new Thread(sendTask()).start();
    }

    private void setSentTime(Timestamp ts) {

        if (ts != null) {
            final String sent = "Gesendet am:\n" + CaDatumZeit.toGermanDate(srdMessage.getSent_at()) + "\n"
                    + CaDatumZeit.toGermanTime(srdMessage.getSent_at());
            lblSentTime.setText(sent);
        } else {
            lblSentTime.setText("");
        }
    }

    private void gewAbstimmungen() {
        CtrlHauptStage tmp = new CtrlHauptStage();
        tmp.gewParameterAbstimmungen(new ActionEvent());
    }

    public String toGermanTime(String time) {

        LocalDateTime utcTime = LocalDateTime.parse(time, formatter);
        ZonedDateTime zonedTime = utcTime.atZone(ZoneId.of("UTC"));
        ZonedDateTime germanTime = zonedTime.withZoneSameInstant(ZoneId.of("Europe/Berlin"));

        return germanTime.format(formatter);
    }

    public String toUTCTime(String time) {

        LocalDateTime germanTime = LocalDateTime.parse(time, formatter);
        ZonedDateTime zonedTime = germanTime.atZone(ZoneId.of("Europe/Berlin"));
        ZonedDateTime utcTime = zonedTime.withZoneSameInstant(ZoneId.of("UTC"));

        return utcTime.format(formatter);
    }
}
