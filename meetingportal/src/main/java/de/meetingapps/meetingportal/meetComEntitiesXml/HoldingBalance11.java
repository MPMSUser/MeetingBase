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

import javax.xml.datatype.XMLGregorianCalendar;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für HoldingBalance11 complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="HoldingBalance11"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="Bal" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}SignedQuantityFormat14"/&gt;
 *         &lt;element name="BalTp" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}SecuritiesEntryType2Code"/&gt;
 *         &lt;element name="SfkpgPlc" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}SafekeepingPlaceFormat28Choice" minOccurs="0"/&gt;
 *         &lt;element name="Dt" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}ISODate" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "HoldingBalance11", propOrder = {
    "bal",
    "balTp",
    "sfkpgPlc",
    "dt"
})
public class HoldingBalance11 {

    @XmlElement(name = "Bal", required = true)
    protected SignedQuantityFormat14 bal;
    @XmlElement(name = "BalTp", required = true)
    @XmlSchemaType(name = "string")
    protected SecuritiesEntryType2Code balTp;
    @XmlElement(name = "SfkpgPlc")
    protected SafekeepingPlaceFormat28Choice sfkpgPlc;
    @XmlElement(name = "Dt")
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar dt;

    /**
     * Ruft den Wert der bal-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link SignedQuantityFormat14 }
     *     
     */
    public SignedQuantityFormat14 getBal() {
        return bal;
    }

    /**
     * Legt den Wert der bal-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link SignedQuantityFormat14 }
     *     
     */
    public void setBal(SignedQuantityFormat14 value) {
        this.bal = value;
    }

    /**
     * Ruft den Wert der balTp-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link SecuritiesEntryType2Code }
     *     
     */
    public SecuritiesEntryType2Code getBalTp() {
        return balTp;
    }

    /**
     * Legt den Wert der balTp-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link SecuritiesEntryType2Code }
     *     
     */
    public void setBalTp(SecuritiesEntryType2Code value) {
        this.balTp = value;
    }

    /**
     * Ruft den Wert der sfkpgPlc-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link SafekeepingPlaceFormat28Choice }
     *     
     */
    public SafekeepingPlaceFormat28Choice getSfkpgPlc() {
        return sfkpgPlc;
    }

    /**
     * Legt den Wert der sfkpgPlc-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link SafekeepingPlaceFormat28Choice }
     *     
     */
    public void setSfkpgPlc(SafekeepingPlaceFormat28Choice value) {
        this.sfkpgPlc = value;
    }

    /**
     * Ruft den Wert der dt-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDt() {
        return dt;
    }

    /**
     * Legt den Wert der dt-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDt(XMLGregorianCalendar value) {
        this.dt = value;
    }

}
