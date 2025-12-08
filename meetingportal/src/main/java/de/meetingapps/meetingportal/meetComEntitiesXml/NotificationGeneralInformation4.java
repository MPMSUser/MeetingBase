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

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für NotificationGeneralInformation4 complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="NotificationGeneralInformation4"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="NtfctnId" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}Max35Text" minOccurs="0"/&gt;
 *         &lt;element name="NtfctnTp" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}NotificationType3Code"/&gt;
 *         &lt;element name="NtfctnSts" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}EventStatus1"/&gt;
 *         &lt;element name="ShrhldrRghtsDrctvInd" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}YesNoIndicator" minOccurs="0"/&gt;
 *         &lt;element name="ConfOfHldgReqrd" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}YesNoIndicator" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "NotificationGeneralInformation4", propOrder = {
    "ntfctnId",
    "ntfctnTp",
    "ntfctnSts",
    "shrhldrRghtsDrctvInd",
    "confOfHldgReqrd"
})
public class NotificationGeneralInformation4 {

    @XmlElement(name = "NtfctnId")
    protected String ntfctnId;
    @XmlElement(name = "NtfctnTp", required = true)
    @XmlSchemaType(name = "string")
    protected NotificationType3Code ntfctnTp;
    @XmlElement(name = "NtfctnSts", required = true)
    protected EventStatus1 ntfctnSts;
    @XmlElement(name = "ShrhldrRghtsDrctvInd")
    protected Boolean shrhldrRghtsDrctvInd;
    @XmlElement(name = "ConfOfHldgReqrd")
    protected Boolean confOfHldgReqrd;

    /**
     * Ruft den Wert der ntfctnId-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNtfctnId() {
        return ntfctnId;
    }

    /**
     * Legt den Wert der ntfctnId-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNtfctnId(String value) {
        this.ntfctnId = value;
    }

    /**
     * Ruft den Wert der ntfctnTp-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link NotificationType3Code }
     *     
     */
    public NotificationType3Code getNtfctnTp() {
        return ntfctnTp;
    }

    /**
     * Legt den Wert der ntfctnTp-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link NotificationType3Code }
     *     
     */
    public void setNtfctnTp(NotificationType3Code value) {
        this.ntfctnTp = value;
    }

    /**
     * Ruft den Wert der ntfctnSts-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link EventStatus1 }
     *     
     */
    public EventStatus1 getNtfctnSts() {
        return ntfctnSts;
    }

    /**
     * Legt den Wert der ntfctnSts-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link EventStatus1 }
     *     
     */
    public void setNtfctnSts(EventStatus1 value) {
        this.ntfctnSts = value;
    }

    /**
     * Ruft den Wert der shrhldrRghtsDrctvInd-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isShrhldrRghtsDrctvInd() {
        return shrhldrRghtsDrctvInd;
    }

    /**
     * Legt den Wert der shrhldrRghtsDrctvInd-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setShrhldrRghtsDrctvInd(Boolean value) {
        this.shrhldrRghtsDrctvInd = value;
    }

    /**
     * Ruft den Wert der confOfHldgReqrd-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isConfOfHldgReqrd() {
        return confOfHldgReqrd;
    }

    /**
     * Legt den Wert der confOfHldgReqrd-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setConfOfHldgReqrd(Boolean value) {
        this.confOfHldgReqrd = value;
    }

}
