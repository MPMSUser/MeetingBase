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
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für MeetingContactPerson3 complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="MeetingContactPerson3"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="CtctPrsn" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}ContactIdentification1" minOccurs="0"/&gt;
 *         &lt;element name="EmplngPty" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}PartyIdentification129Choice" minOccurs="0"/&gt;
 *         &lt;element name="PlcOfListg" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}MICIdentifier" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MeetingContactPerson3", propOrder = {
    "ctctPrsn",
    "emplngPty",
    "plcOfListg"
})
public class MeetingContactPerson3 {

    @XmlElement(name = "CtctPrsn")
    protected ContactIdentification1 ctctPrsn;
    @XmlElement(name = "EmplngPty")
    protected PartyIdentification129Choice emplngPty;
    @XmlElement(name = "PlcOfListg")
    protected String plcOfListg;

    /**
     * Ruft den Wert der ctctPrsn-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link ContactIdentification1 }
     *     
     */
    public ContactIdentification1 getCtctPrsn() {
        return ctctPrsn;
    }

    /**
     * Legt den Wert der ctctPrsn-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link ContactIdentification1 }
     *     
     */
    public void setCtctPrsn(ContactIdentification1 value) {
        this.ctctPrsn = value;
    }

    /**
     * Ruft den Wert der emplngPty-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PartyIdentification129Choice }
     *     
     */
    public PartyIdentification129Choice getEmplngPty() {
        return emplngPty;
    }

    /**
     * Legt den Wert der emplngPty-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PartyIdentification129Choice }
     *     
     */
    public void setEmplngPty(PartyIdentification129Choice value) {
        this.emplngPty = value;
    }

    /**
     * Ruft den Wert der plcOfListg-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPlcOfListg() {
        return plcOfListg;
    }

    /**
     * Legt den Wert der plcOfListg-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPlcOfListg(String value) {
        this.plcOfListg = value;
    }

}
