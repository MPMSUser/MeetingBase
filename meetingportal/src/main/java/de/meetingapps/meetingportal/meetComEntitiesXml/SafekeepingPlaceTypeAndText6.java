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
 * <p>Java-Klasse für SafekeepingPlaceTypeAndText6 complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="SafekeepingPlaceTypeAndText6"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="SfkpgPlcTp" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}SafekeepingPlace2Code"/&gt;
 *         &lt;element name="Id" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}Max35Text" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SafekeepingPlaceTypeAndText6", propOrder = {
    "sfkpgPlcTp",
    "id"
})
public class SafekeepingPlaceTypeAndText6 {

    @XmlElement(name = "SfkpgPlcTp", required = true)
    @XmlSchemaType(name = "string")
    protected SafekeepingPlace2Code sfkpgPlcTp;
    @XmlElement(name = "Id")
    protected String id;

    /**
     * Ruft den Wert der sfkpgPlcTp-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link SafekeepingPlace2Code }
     *     
     */
    public SafekeepingPlace2Code getSfkpgPlcTp() {
        return sfkpgPlcTp;
    }

    /**
     * Legt den Wert der sfkpgPlcTp-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link SafekeepingPlace2Code }
     *     
     */
    public void setSfkpgPlcTp(SafekeepingPlace2Code value) {
        this.sfkpgPlcTp = value;
    }

    /**
     * Ruft den Wert der id-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Legt den Wert der id-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }

}
