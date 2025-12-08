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
public class Envelope {

    @XmlElement(name = "AppHdr")
    private AppHdr appHdr;
    
    @XmlElement(name = "ErrorDetails")
    private ErrorDetails errorDetails;

    @XmlElement(name = "Document")
    private Document document;

    // Getter and Setter
    public AppHdr getAppHdr() { return appHdr; }
    public void setAppHdr(AppHdr appHdr) { this.appHdr = appHdr; }
    
    public ErrorDetails getErrorDetails() { return errorDetails; }
    public void setErrorDetails(ErrorDetails errorDetails) { this.errorDetails = errorDetails; }

    public Document getDocument() { return document; }
    public void setDocument(Document document) { this.document = document; }
}
