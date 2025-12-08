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
 * <p>Java-Klasse für LocationFormat1Choice complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="LocationFormat1Choice"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;choice&gt;
 *         &lt;element name="Adr" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}PostalAddress1"/&gt;
 *         &lt;element name="LctnCd" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}PlaceType1Code"/&gt;
 *       &lt;/choice&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "LocationFormat1Choice", propOrder = {
    "adr",
    "lctnCd"
})
public class LocationFormat1Choice {

    @XmlElement(name = "Adr")
    protected PostalAddress1 adr;
    @XmlElement(name = "LctnCd")
    @XmlSchemaType(name = "string")
    protected PlaceType1Code lctnCd;

    /**
     * Ruft den Wert der adr-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PostalAddress1 }
     *     
     */
    public PostalAddress1 getAdr() {
        return adr;
    }

    /**
     * Legt den Wert der adr-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PostalAddress1 }
     *     
     */
    public void setAdr(PostalAddress1 value) {
        this.adr = value;
    }

    /**
     * Ruft den Wert der lctnCd-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PlaceType1Code }
     *     
     */
    public PlaceType1Code getLctnCd() {
        return lctnCd;
    }

    /**
     * Legt den Wert der lctnCd-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PlaceType1Code }
     *     
     */
    public void setLctnCd(PlaceType1Code value) {
        this.lctnCd = value;
    }

}
