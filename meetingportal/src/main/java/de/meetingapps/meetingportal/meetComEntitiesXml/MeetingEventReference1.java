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
 * <p>Java-Klasse für MeetingEventReference1 complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="MeetingEventReference1"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="EvtId" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}MeetingEventReference1Choice"/&gt;
 *         &lt;element name="LkgTp" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}ProcessingPosition3Code" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MeetingEventReference1", propOrder = {
    "evtId",
    "lkgTp"
})
public class MeetingEventReference1 {

    @XmlElement(name = "EvtId", required = true)
    protected MeetingEventReference1Choice evtId;
    @XmlElement(name = "LkgTp")
    @XmlSchemaType(name = "string")
    protected ProcessingPosition3Code lkgTp;

    /**
     * Ruft den Wert der evtId-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link MeetingEventReference1Choice }
     *     
     */
    public MeetingEventReference1Choice getEvtId() {
        return evtId;
    }

    /**
     * Legt den Wert der evtId-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link MeetingEventReference1Choice }
     *     
     */
    public void setEvtId(MeetingEventReference1Choice value) {
        this.evtId = value;
    }

    /**
     * Ruft den Wert der lkgTp-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link ProcessingPosition3Code }
     *     
     */
    public ProcessingPosition3Code getLkgTp() {
        return lkgTp;
    }

    /**
     * Legt den Wert der lkgTp-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link ProcessingPosition3Code }
     *     
     */
    public void setLkgTp(ProcessingPosition3Code value) {
        this.lkgTp = value;
    }

}
