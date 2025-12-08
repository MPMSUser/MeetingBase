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
 * <p>Java-Klasse für SafekeepingPlaceFormat28Choice complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="SafekeepingPlaceFormat28Choice"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;choice&gt;
 *         &lt;element name="Id" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}SafekeepingPlaceTypeAndText6"/&gt;
 *         &lt;element name="Ctry" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}CountryCode"/&gt;
 *         &lt;element name="TpAndId" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}SafekeepingPlaceTypeAndIdentification1"/&gt;
 *         &lt;element name="Prtry" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}GenericIdentification78"/&gt;
 *       &lt;/choice&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SafekeepingPlaceFormat28Choice", propOrder = {
    "id",
    "ctry",
    "tpAndId",
    "prtry"
})
public class SafekeepingPlaceFormat28Choice {

    @XmlElement(name = "Id")
    protected SafekeepingPlaceTypeAndText6 id;
    @XmlElement(name = "Ctry")
    protected String ctry;
    @XmlElement(name = "TpAndId")
    protected SafekeepingPlaceTypeAndIdentification1 tpAndId;
    @XmlElement(name = "Prtry")
    protected GenericIdentification78 prtry;

    /**
     * Ruft den Wert der id-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link SafekeepingPlaceTypeAndText6 }
     *     
     */
    public SafekeepingPlaceTypeAndText6 getId() {
        return id;
    }

    /**
     * Legt den Wert der id-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link SafekeepingPlaceTypeAndText6 }
     *     
     */
    public void setId(SafekeepingPlaceTypeAndText6 value) {
        this.id = value;
    }

    /**
     * Ruft den Wert der ctry-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCtry() {
        return ctry;
    }

    /**
     * Legt den Wert der ctry-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCtry(String value) {
        this.ctry = value;
    }

    /**
     * Ruft den Wert der tpAndId-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link SafekeepingPlaceTypeAndIdentification1 }
     *     
     */
    public SafekeepingPlaceTypeAndIdentification1 getTpAndId() {
        return tpAndId;
    }

    /**
     * Legt den Wert der tpAndId-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link SafekeepingPlaceTypeAndIdentification1 }
     *     
     */
    public void setTpAndId(SafekeepingPlaceTypeAndIdentification1 value) {
        this.tpAndId = value;
    }

    /**
     * Ruft den Wert der prtry-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link GenericIdentification78 }
     *     
     */
    public GenericIdentification78 getPrtry() {
        return prtry;
    }

    /**
     * Legt den Wert der prtry-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link GenericIdentification78 }
     *     
     */
    public void setPrtry(GenericIdentification78 value) {
        this.prtry = value;
    }

}
