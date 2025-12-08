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
 * <p>Java-Klasse für VoteTypeAndQuantity1 complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="VoteTypeAndQuantity1"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="VoteInstrTp" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}VoteInstructionType1Choice"/&gt;
 *         &lt;element name="VoteQty" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}Number"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "VoteTypeAndQuantity1", propOrder = {
    "voteInstrTp",
    "voteQty"
})
public class VoteTypeAndQuantity1 {

    @XmlElement(name = "VoteInstrTp", required = true)
    protected VoteInstructionType1Choice voteInstrTp;
    @XmlElement(name = "VoteQty", required = true)
    protected BigDecimal voteQty;

    /**
     * Ruft den Wert der voteInstrTp-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link VoteInstructionType1Choice }
     *     
     */
    public VoteInstructionType1Choice getVoteInstrTp() {
        return voteInstrTp;
    }

    /**
     * Legt den Wert der voteInstrTp-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link VoteInstructionType1Choice }
     *     
     */
    public void setVoteInstrTp(VoteInstructionType1Choice value) {
        this.voteInstrTp = value;
    }

    /**
     * Ruft den Wert der voteQty-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getVoteQty() {
        return voteQty;
    }

    /**
     * Legt den Wert der voteQty-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setVoteQty(BigDecimal value) {
        this.voteQty = value;
    }

}
