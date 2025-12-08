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

import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für EligiblePosition13 complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="EligiblePosition13"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="AcctId" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}Max35Text" minOccurs="0"/&gt;
 *         &lt;element name="BlckChainAdrOrWllt" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}Max140Text" minOccurs="0"/&gt;
 *         &lt;element name="AcctOwnr" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}PartyIdentification231Choice" minOccurs="0"/&gt;
 *         &lt;element name="HldgBal" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}HoldingBalance11" maxOccurs="15" minOccurs="0"/&gt;
 *         &lt;element name="RghtsHldr" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}PartyIdentification246Choice" maxOccurs="250" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EligiblePosition13", propOrder = {
    "acctId",
    "blckChainAdrOrWllt",
    "acctOwnr",
    "hldgBal",
    "rghtsHldr"
})
public class EligiblePosition13 {

    @XmlElement(name = "AcctId")
    protected String acctId;
    @XmlElement(name = "BlckChainAdrOrWllt")
    protected String blckChainAdrOrWllt;
    @XmlElement(name = "AcctOwnr")
    protected PartyIdentification231Choice acctOwnr;
    @XmlElement(name = "HldgBal")
    protected List<HoldingBalance11> hldgBal;
    @XmlElement(name = "RghtsHldr")
    protected List<PartyIdentification246Choice> rghtsHldr;

    /**
     * Ruft den Wert der acctId-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAcctId() {
        return acctId;
    }

    /**
     * Legt den Wert der acctId-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAcctId(String value) {
        this.acctId = value;
    }

    /**
     * Ruft den Wert der blckChainAdrOrWllt-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBlckChainAdrOrWllt() {
        return blckChainAdrOrWllt;
    }

    /**
     * Legt den Wert der blckChainAdrOrWllt-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBlckChainAdrOrWllt(String value) {
        this.blckChainAdrOrWllt = value;
    }

    /**
     * Ruft den Wert der acctOwnr-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PartyIdentification231Choice }
     *     
     */
    public PartyIdentification231Choice getAcctOwnr() {
        return acctOwnr;
    }

    /**
     * Legt den Wert der acctOwnr-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PartyIdentification231Choice }
     *     
     */
    public void setAcctOwnr(PartyIdentification231Choice value) {
        this.acctOwnr = value;
    }

    /**
     * Gets the value of the hldgBal property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a <CODE>set</CODE> method for the hldgBal property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getHldgBal().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link HoldingBalance11 }
     * 
     * 
     */
    public List<HoldingBalance11> getHldgBal() {
        if (hldgBal == null) {
            hldgBal = new ArrayList<HoldingBalance11>();
        }
        return this.hldgBal;
    }

    /**
     * Gets the value of the rghtsHldr property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a <CODE>set</CODE> method for the rghtsHldr property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRghtsHldr().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PartyIdentification246Choice }
     * 
     * 
     */
    public List<PartyIdentification246Choice> getRghtsHldr() {
        if (rghtsHldr == null) {
            rghtsHldr = new ArrayList<PartyIdentification246Choice>();
        }
        return this.rghtsHldr;
    }

}
