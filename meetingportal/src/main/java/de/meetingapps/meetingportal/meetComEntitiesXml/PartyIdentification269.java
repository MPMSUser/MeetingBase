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
 * <p>Java-Klasse für PartyIdentification269 complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="PartyIdentification269"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="NmAndAdr" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}PersonName2"/&gt;
 *         &lt;element name="EmailAdr" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}Max256Text" minOccurs="0"/&gt;
 *         &lt;element name="Id" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}PartyIdentification198Choice" minOccurs="0"/&gt;
 *         &lt;element name="CpnyRegrShrhldrId" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}Max35Text" minOccurs="0"/&gt;
 *         &lt;element name="CtryOfIncorprtn" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}CountryCode" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PartyIdentification269", propOrder = {
    "nmAndAdr",
    "emailAdr",
    "id",
    "cpnyRegrShrhldrId",
    "ctryOfIncorprtn"
})
public class PartyIdentification269 {

    @XmlElement(name = "NmAndAdr", required = true)
    protected PersonName2 nmAndAdr;
    @XmlElement(name = "EmailAdr")
    protected String emailAdr;
    @XmlElement(name = "Id")
    protected PartyIdentification198Choice id;
    @XmlElement(name = "CpnyRegrShrhldrId")
    protected String cpnyRegrShrhldrId;
    @XmlElement(name = "CtryOfIncorprtn")
    protected String ctryOfIncorprtn;

    /**
     * Ruft den Wert der nmAndAdr-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PersonName2 }
     *     
     */
    public PersonName2 getNmAndAdr() {
        return nmAndAdr;
    }

    /**
     * Legt den Wert der nmAndAdr-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PersonName2 }
     *     
     */
    public void setNmAndAdr(PersonName2 value) {
        this.nmAndAdr = value;
    }

    /**
     * Ruft den Wert der emailAdr-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEmailAdr() {
        return emailAdr;
    }

    /**
     * Legt den Wert der emailAdr-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEmailAdr(String value) {
        this.emailAdr = value;
    }

    /**
     * Ruft den Wert der id-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PartyIdentification198Choice }
     *     
     */
    public PartyIdentification198Choice getId() {
        return id;
    }

    /**
     * Legt den Wert der id-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PartyIdentification198Choice }
     *     
     */
    public void setId(PartyIdentification198Choice value) {
        this.id = value;
    }

    /**
     * Ruft den Wert der cpnyRegrShrhldrId-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCpnyRegrShrhldrId() {
        return cpnyRegrShrhldrId;
    }

    /**
     * Legt den Wert der cpnyRegrShrhldrId-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCpnyRegrShrhldrId(String value) {
        this.cpnyRegrShrhldrId = value;
    }

    /**
     * Ruft den Wert der ctryOfIncorprtn-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCtryOfIncorprtn() {
        return ctryOfIncorprtn;
    }

    /**
     * Legt den Wert der ctryOfIncorprtn-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCtryOfIncorprtn(String value) {
        this.ctryOfIncorprtn = value;
    }

}
