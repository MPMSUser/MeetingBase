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
package de.meetingapps.meetingportal.streaming.srdII.proxyLieferant01.entities.Response;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class AppHdr {

    @XmlElement(name = "Fr")
    private Fr fr;

    @XmlElement(name = "To")
    private To to;

    @XmlElement(name = "BizMsgIdr")
    private String bizMsgIdr;

    @XmlElement(name = "MsgDefIdr")
    private String msgDefIdr;

    @XmlElement(name = "CreDt")
    private String creDt;

    // Getter and Setter
    public Fr getFr() { return fr; }
    public void setFr(Fr fr) { this.fr = fr; }

    public To getTo() { return to; }
    public void setTo(To to) { this.to = to; }

    public String getBizMsgIdr() { return bizMsgIdr; }
    public void setBizMsgIdr(String bizMsgIdr) { this.bizMsgIdr = bizMsgIdr; }

    public String getMsgDefIdr() { return msgDefIdr; }
    public void setMsgDefIdr(String msgDefIdr) { this.msgDefIdr = msgDefIdr; }

    public String getCreDt() { return creDt; }
    public void setCreDt(String creDt) { this.creDt = creDt; }
}
