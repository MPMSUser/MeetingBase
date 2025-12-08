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
public class Prtcptn {

    @XmlElement(name = "PrtcptnMtd")
    private PrtcptnMtd prtcptnMtd;

    @XmlElement(name = "IssrDdlnForVtng")
    private DtOrDtTm issrDdlnForVtng;

    // Other elements...
    // Getter and Setter
    public PrtcptnMtd getPrtcptnMtd() { return prtcptnMtd; }
    public void setPrtcptnMtd(PrtcptnMtd prtcptnMtd) { this.prtcptnMtd = prtcptnMtd; }

    public DtOrDtTm getIssrDdlnForVtng() { return issrDdlnForVtng; }
    public void setIssrDdlnForVtng(DtOrDtTm issrDdlnForVtng) { this.issrDdlnForVtng = issrDdlnForVtng; }
}
