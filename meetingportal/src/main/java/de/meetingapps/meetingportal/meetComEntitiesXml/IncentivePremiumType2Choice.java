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
import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für IncentivePremiumType2Choice complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="IncentivePremiumType2Choice"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;choice&gt;
 *         &lt;element name="PerScty" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}Number"/&gt;
 *         &lt;element name="PerVote" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}VoteTypeAndQuantity1" maxOccurs="unbounded"/&gt;
 *         &lt;element name="PerAttndee" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}YesNoIndicator"/&gt;
 *       &lt;/choice&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "IncentivePremiumType2Choice", propOrder = {
    "perScty",
    "perVote",
    "perAttndee"
})
public class IncentivePremiumType2Choice {

    @XmlElement(name = "PerScty")
    protected BigDecimal perScty;
    @XmlElement(name = "PerVote")
    protected List<VoteTypeAndQuantity1> perVote;
    @XmlElement(name = "PerAttndee")
    protected Boolean perAttndee;

    /**
     * Ruft den Wert der perScty-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getPerScty() {
        return perScty;
    }

    /**
     * Legt den Wert der perScty-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setPerScty(BigDecimal value) {
        this.perScty = value;
    }

    /**
     * Gets the value of the perVote property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a <CODE>set</CODE> method for the perVote property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPerVote().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link VoteTypeAndQuantity1 }
     * 
     * 
     */
    public List<VoteTypeAndQuantity1> getPerVote() {
        if (perVote == null) {
            perVote = new ArrayList<VoteTypeAndQuantity1>();
        }
        return this.perVote;
    }

    /**
     * Ruft den Wert der perAttndee-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isPerAttndee() {
        return perAttndee;
    }

    /**
     * Legt den Wert der perAttndee-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setPerAttndee(Boolean value) {
        this.perAttndee = value;
    }

}
