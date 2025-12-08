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
package de.meetingapps.meetingportal.meetComEntities;

import java.sql.Timestamp;

public class EclSRDMessage {

    private int id;

    private String seev;

    private String isin;

    private String appHeader;

    private String document;

    private String meetingId;

    private String issuerMeetingId;

    private String mtgDtAndTime;

    private String type;

    private String messageId;

    private String prevMessageId;

    private String responseMessage;

    private Timestamp sent_at;

    private Timestamp updated_at;

    public EclSRDMessage() {

    }

    public EclSRDMessage(int id, String seev, String isin, String appHeader, String document, String meetingId,
            String issuerMeetingId, String mtgDtAndTime, String type, String messageId, String prevMessageId,
            String responseMessage, Timestamp sent_at, Timestamp updated_at) {
        super();
        this.id = id;
        this.seev = seev;
        this.isin = isin;
        this.appHeader = appHeader;
        this.document = document;
        this.meetingId = meetingId;
        this.issuerMeetingId = issuerMeetingId;
        this.mtgDtAndTime = mtgDtAndTime;
        this.type = type;
        this.messageId = messageId;
        this.prevMessageId = prevMessageId;
        this.responseMessage = responseMessage;
        this.sent_at = sent_at;
        this.updated_at = updated_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSeev() {
        return seev;
    }

    public void setSeev(String seev) {
        this.seev = seev;
    }

    public String getIsin() {
        return isin;
    }

    public void setIsin(String isin) {
        this.isin = isin;
    }

    public String getAppHeader() {
        return appHeader;
    }

    public void setAppHeader(String appHeader) {
        this.appHeader = appHeader;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public String getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(String meetingId) {
        this.meetingId = meetingId;
    }

    public String getIssuerMeetingId() {
        return issuerMeetingId;
    }

    public void setIssuerMeetingId(String issuerMeetingId) {
        this.issuerMeetingId = issuerMeetingId;
    }

    public String getMtgDtAndTime() {
        return mtgDtAndTime;
    }

    public void setMtgDtAndTime(String mtgDtAndTime) {
        this.mtgDtAndTime = mtgDtAndTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getPrevMessageId() {
        return prevMessageId;
    }

    public void setPrevMessageId(String prevMessageId) {
        this.prevMessageId = prevMessageId;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public Timestamp getSent_at() {
        return sent_at;
    }

    public void setSent_at(Timestamp sent_at) {
        this.sent_at = sent_at;
    }

    public Timestamp getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Timestamp updated_at) {
        this.updated_at = updated_at;
    }
    
    

    @Override
    public String toString() {
        return "EclSRDMessage [id=" + id + ", seev=" + seev + ", isin=" + isin + ", meetingId=" + meetingId
                + ", issuerMeetingId=" + issuerMeetingId + ", mtgDtAndTime=" + mtgDtAndTime + ", type=" + type
                + ", messageId=" + messageId + ", prevMessageId=" + prevMessageId + ", responseMessage="
                + responseMessage + ", sent_at=" + sent_at + ", updated_at=" + updated_at + "]";
    }

    public void replaceAppHeader(String bizMsgIdr, String msgDefIdr, String sent_at) {
        appHeader = appHeader.replace("$BizMsgIdr", bizMsgIdr);
        appHeader = appHeader.replace("$MsgDefIdr", msgDefIdr);
        appHeader = appHeader.replace("$CreDt", sent_at);
        appHeader = appHeader.replaceAll("(?<=>)(\\s+)(?=<)", "");
    }

    public void replaceDocument(String mtgId, String issuerMtgId, String mtgDtAndTm, String tp, String isin) {
        document = document.replace("$MtgId", mtgId);
        document = document.replace("$IssuerMtgId", issuerMtgId);
        document = document.replace("$MtgDtAndTm", mtgDtAndTm);
        document = document.replace("$Tp", tp);
        document = document.replace("$Isin", isin);
    }

    public void prepDocument() {
        document = document.replaceAll("(?<=>)(\\s+)(?=<)", "");
        document = document.replace("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>", "").trim();
    }
    
    public String proximityMessage() {
        return appHeader.trim() + document.trim().replaceAll("<", "&lt;") + "</Document></Envelope></RequestPayload>";
    }
}
