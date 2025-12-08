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
 * <p>Java-Klasse für PartyIdentification250 complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="PartyIdentification250"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="NmAndAdr" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}PersonName3"/&gt;
 *         &lt;element name="EmailAdr" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}Max256Text" minOccurs="0"/&gt;
 *         &lt;element name="Id" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}NaturalPersonIdentification1" minOccurs="0"/&gt;
 *         &lt;element name="Ntlty" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}CountryCode" minOccurs="0"/&gt;
 *         &lt;element name="DtAndPlcOfBirth" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}DateAndPlaceOfBirth2" minOccurs="0"/&gt;
 *         &lt;element name="CpnyRegrShrhldrId" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}Max35Text" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PartyIdentification250", propOrder = {
    "nmAndAdr",
    "emailAdr",
    "id",
    "ntlty",
    "dtAndPlcOfBirth",
    "cpnyRegrShrhldrId"
})
public class PartyIdentification250 {

    @XmlElement(name = "NmAndAdr", required = true)
    protected PersonName3 nmAndAdr;
    @XmlElement(name = "EmailAdr")
    protected String emailAdr;
    @XmlElement(name = "Id")
    protected NaturalPersonIdentification1 id;
    @XmlElement(name = "Ntlty")
    protected String ntlty;
    @XmlElement(name = "DtAndPlcOfBirth")
    protected DateAndPlaceOfBirth2 dtAndPlcOfBirth;
    @XmlElement(name = "CpnyRegrShrhldrId")
    protected String cpnyRegrShrhldrId;

    /**
     * Ruft den Wert der nmAndAdr-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PersonName3 }
     *     
     */
    public PersonName3 getNmAndAdr() {
        return nmAndAdr;
    }

    /**
     * Legt den Wert der nmAndAdr-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PersonName3 }
     *     
     */
    public void setNmAndAdr(PersonName3 value) {
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
     *     {@link NaturalPersonIdentification1 }
     *     
     */
    public NaturalPersonIdentification1 getId() {
        return id;
    }

    /**
     * Legt den Wert der id-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link NaturalPersonIdentification1 }
     *     
     */
    public void setId(NaturalPersonIdentification1 value) {
        this.id = value;
    }

    /**
     * Ruft den Wert der ntlty-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNtlty() {
        return ntlty;
    }

    /**
     * Legt den Wert der ntlty-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNtlty(String value) {
        this.ntlty = value;
    }

    /**
     * Ruft den Wert der dtAndPlcOfBirth-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link DateAndPlaceOfBirth2 }
     *     
     */
    public DateAndPlaceOfBirth2 getDtAndPlcOfBirth() {
        return dtAndPlcOfBirth;
    }

    /**
     * Legt den Wert der dtAndPlcOfBirth-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link DateAndPlaceOfBirth2 }
     *     
     */
    public void setDtAndPlcOfBirth(DateAndPlaceOfBirth2 value) {
        this.dtAndPlcOfBirth = value;
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

}
