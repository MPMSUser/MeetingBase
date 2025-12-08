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

import java.math.BigDecimal;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für Entitlement1Choice complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="Entitlement1Choice"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;choice&gt;
 *         &lt;element name="EntitlmntRatio" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}DecimalNumber"/&gt;
 *         &lt;element name="EntitlmntDesc" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}Max35Text"/&gt;
 *       &lt;/choice&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Entitlement1Choice", propOrder = {
    "entitlmntRatio",
    "entitlmntDesc"
})
public class Entitlement1Choice {

    @XmlElement(name = "EntitlmntRatio")
    protected BigDecimal entitlmntRatio;
    @XmlElement(name = "EntitlmntDesc")
    protected String entitlmntDesc;

    /**
     * Ruft den Wert der entitlmntRatio-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getEntitlmntRatio() {
        return entitlmntRatio;
    }

    /**
     * Legt den Wert der entitlmntRatio-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setEntitlmntRatio(BigDecimal value) {
        this.entitlmntRatio = value;
    }

    /**
     * Ruft den Wert der entitlmntDesc-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEntitlmntDesc() {
        return entitlmntDesc;
    }

    /**
     * Legt den Wert der entitlmntDesc-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEntitlmntDesc(String value) {
        this.entitlmntDesc = value;
    }

}
