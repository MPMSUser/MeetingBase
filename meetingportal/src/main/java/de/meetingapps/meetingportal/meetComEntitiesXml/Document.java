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
//
// Diese Datei wurde mit der Eclipse Implementation of JAXB, v3.0.0 generiert 
// Siehe https://eclipse-ee4j.github.io/jaxb-ri 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2024.11.15 um 11:09:47 AM CET 
//

package de.meetingapps.meetingportal.meetComEntitiesXml;

import java.io.Serializable;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

/**
 * <p>Java-Klasse für Document complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="Document"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="MtgNtfctn" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}MeetingNotificationV11"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlRootElement(name = "Document")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Document", propOrder = { "mtgNtfctn" })
public class Document implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = "MtgNtfctn", required = true)
    protected MeetingNotificationV11 mtgNtfctn;

    /**
     * Ruft den Wert der mtgNtfctn-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link MeetingNotificationV11 }
     *     
     */
    public MeetingNotificationV11 getMtgNtfctn() {
        return mtgNtfctn;
    }

    /**
     * Legt den Wert der mtgNtfctn-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link MeetingNotificationV11 }
     *     
     */
    public void setMtgNtfctn(MeetingNotificationV11 value) {
        this.mtgNtfctn = value;
    }

}
