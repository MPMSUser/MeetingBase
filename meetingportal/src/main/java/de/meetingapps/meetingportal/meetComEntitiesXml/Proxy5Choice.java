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
 * <p>Java-Klasse für Proxy5Choice complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="Proxy5Choice"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;choice&gt;
 *         &lt;element name="Prxy" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}ProxyAppointmentInformation6"/&gt;
 *         &lt;element name="PrxyNotAllwd" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}ProxyNotAllowed1Code"/&gt;
 *       &lt;/choice&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Proxy5Choice", propOrder = {
    "prxy",
    "prxyNotAllwd"
})
public class Proxy5Choice {

    @XmlElement(name = "Prxy")
    protected ProxyAppointmentInformation6 prxy;
    @XmlElement(name = "PrxyNotAllwd")
    @XmlSchemaType(name = "string")
    protected ProxyNotAllowed1Code prxyNotAllwd;

    /**
     * Ruft den Wert der prxy-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link ProxyAppointmentInformation6 }
     *     
     */
    public ProxyAppointmentInformation6 getPrxy() {
        return prxy;
    }

    /**
     * Legt den Wert der prxy-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link ProxyAppointmentInformation6 }
     *     
     */
    public void setPrxy(ProxyAppointmentInformation6 value) {
        this.prxy = value;
    }

    /**
     * Ruft den Wert der prxyNotAllwd-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link ProxyNotAllowed1Code }
     *     
     */
    public ProxyNotAllowed1Code getPrxyNotAllwd() {
        return prxyNotAllwd;
    }

    /**
     * Legt den Wert der prxyNotAllwd-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link ProxyNotAllowed1Code }
     *     
     */
    public void setPrxyNotAllwd(ProxyNotAllowed1Code value) {
        this.prxyNotAllwd = value;
    }

}
