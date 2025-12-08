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
 * <p>Java-Klasse für Proxy11 complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="Proxy11"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="PrxyTp" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}ProxyType3Code"/&gt;
 *         &lt;element name="PrsnDtls" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}IndividualPerson43" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Proxy11", propOrder = {
    "prxyTp",
    "prsnDtls"
})
public class Proxy11 {

    @XmlElement(name = "PrxyTp", required = true)
    @XmlSchemaType(name = "string")
    protected ProxyType3Code prxyTp;
    @XmlElement(name = "PrsnDtls")
    protected IndividualPerson43 prsnDtls;

    /**
     * Ruft den Wert der prxyTp-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link ProxyType3Code }
     *     
     */
    public ProxyType3Code getPrxyTp() {
        return prxyTp;
    }

    /**
     * Legt den Wert der prxyTp-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link ProxyType3Code }
     *     
     */
    public void setPrxyTp(ProxyType3Code value) {
        this.prxyTp = value;
    }

    /**
     * Ruft den Wert der prsnDtls-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link IndividualPerson43 }
     *     
     */
    public IndividualPerson43 getPrsnDtls() {
        return prsnDtls;
    }

    /**
     * Legt den Wert der prsnDtls-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link IndividualPerson43 }
     *     
     */
    public void setPrsnDtls(IndividualPerson43 value) {
        this.prsnDtls = value;
    }

}
