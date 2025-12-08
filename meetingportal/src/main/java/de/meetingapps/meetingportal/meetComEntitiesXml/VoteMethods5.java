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

import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für VoteMethods5 complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="VoteMethods5"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="VoteThrghNtwk" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}VoteThroughNetwork1Choice" minOccurs="0"/&gt;
 *         &lt;element name="VoteByMail" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}MailAddress1" minOccurs="0"/&gt;
 *         &lt;element name="ElctrncVote" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}CommunicationAddress12" maxOccurs="5" minOccurs="0"/&gt;
 *         &lt;element name="VoteByTel" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}Max35Text" maxOccurs="5" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "VoteMethods5", propOrder = {
    "voteThrghNtwk",
    "voteByMail",
    "elctrncVote",
    "voteByTel"
})
public class VoteMethods5 {

    @XmlElement(name = "VoteThrghNtwk")
    protected VoteThroughNetwork1Choice voteThrghNtwk;
    @XmlElement(name = "VoteByMail")
    protected MailAddress1 voteByMail;
    @XmlElement(name = "ElctrncVote")
    protected List<CommunicationAddress12> elctrncVote;
    @XmlElement(name = "VoteByTel")
    protected List<String> voteByTel;

    /**
     * Ruft den Wert der voteThrghNtwk-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link VoteThroughNetwork1Choice }
     *     
     */
    public VoteThroughNetwork1Choice getVoteThrghNtwk() {
        return voteThrghNtwk;
    }

    /**
     * Legt den Wert der voteThrghNtwk-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link VoteThroughNetwork1Choice }
     *     
     */
    public void setVoteThrghNtwk(VoteThroughNetwork1Choice value) {
        this.voteThrghNtwk = value;
    }

    /**
     * Ruft den Wert der voteByMail-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link MailAddress1 }
     *     
     */
    public MailAddress1 getVoteByMail() {
        return voteByMail;
    }

    /**
     * Legt den Wert der voteByMail-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link MailAddress1 }
     *     
     */
    public void setVoteByMail(MailAddress1 value) {
        this.voteByMail = value;
    }

    /**
     * Gets the value of the elctrncVote property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a <CODE>set</CODE> method for the elctrncVote property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getElctrncVote().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CommunicationAddress12 }
     * 
     * 
     */
    public List<CommunicationAddress12> getElctrncVote() {
        if (elctrncVote == null) {
            elctrncVote = new ArrayList<CommunicationAddress12>();
        }
        return this.elctrncVote;
    }

    /**
     * Gets the value of the voteByTel property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a <CODE>set</CODE> method for the voteByTel property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getVoteByTel().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getVoteByTel() {
        if (voteByTel == null) {
            voteByTel = new ArrayList<String>();
        }
        return this.voteByTel;
    }

}
