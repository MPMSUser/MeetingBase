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
 * <p>Java-Klasse für VoteParameters8 complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="VoteParameters8"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="SctiesQtyReqrdToVote" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}FinancialInstrumentQuantity18Choice" minOccurs="0"/&gt;
 *         &lt;element name="PrtlVoteAllwd" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}YesNoIndicator"/&gt;
 *         &lt;element name="SpltVoteAllwd" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}YesNoIndicator"/&gt;
 *         &lt;element name="VoteDdln" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}DateFormat58Choice" minOccurs="0"/&gt;
 *         &lt;element name="VoteMktDdln" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}DateFormat58Choice" minOccurs="0"/&gt;
 *         &lt;element name="VoteMthds" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}VoteMethods5" minOccurs="0"/&gt;
 *         &lt;element name="VtngBlltElctrncAdr" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}CommunicationAddress11" minOccurs="0"/&gt;
 *         &lt;element name="VtngBlltReqAdr" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}PostalAddress1" minOccurs="0"/&gt;
 *         &lt;element name="RvcbltyDdln" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}DateFormat58Choice" minOccurs="0"/&gt;
 *         &lt;element name="RvcbltyMktDdln" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}DateFormat58Choice" minOccurs="0"/&gt;
 *         &lt;element name="BnfclOwnrDsclsr" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}YesNoIndicator" minOccurs="0"/&gt;
 *         &lt;element name="EarlyIncntivPrm" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}IncentivePremium5" minOccurs="0"/&gt;
 *         &lt;element name="IncntivPrm" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}IncentivePremium5" minOccurs="0"/&gt;
 *         &lt;element name="EarlyVoteWthPrmDdln" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}DateFormat58Choice" minOccurs="0"/&gt;
 *         &lt;element name="VoteWthPrmDdln" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}DateFormat58Choice" minOccurs="0"/&gt;
 *         &lt;element name="VoteWthPrmMktDdln" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}DateFormat58Choice" minOccurs="0"/&gt;
 *         &lt;element name="AddtlVtngRqrmnts" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}Max350Text" minOccurs="0"/&gt;
 *         &lt;element name="PrvsInstrInvldtyInd" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}YesNoIndicator" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "VoteParameters8", propOrder = {
    "sctiesQtyReqrdToVote",
    "prtlVoteAllwd",
    "spltVoteAllwd",
    "voteDdln",
    "voteMktDdln",
    "voteMthds",
    "vtngBlltElctrncAdr",
    "vtngBlltReqAdr",
    "rvcbltyDdln",
    "rvcbltyMktDdln",
    "bnfclOwnrDsclsr",
    "earlyIncntivPrm",
    "incntivPrm",
    "earlyVoteWthPrmDdln",
    "voteWthPrmDdln",
    "voteWthPrmMktDdln",
    "addtlVtngRqrmnts",
    "prvsInstrInvldtyInd"
})
public class VoteParameters8 {

    @XmlElement(name = "SctiesQtyReqrdToVote")
    protected FinancialInstrumentQuantity18Choice sctiesQtyReqrdToVote;
    @XmlElement(name = "PrtlVoteAllwd")
    protected boolean prtlVoteAllwd;
    @XmlElement(name = "SpltVoteAllwd")
    protected boolean spltVoteAllwd;
    @XmlElement(name = "VoteDdln")
    protected DateFormat58Choice voteDdln;
    @XmlElement(name = "VoteMktDdln")
    protected DateFormat58Choice voteMktDdln;
    @XmlElement(name = "VoteMthds")
    protected VoteMethods5 voteMthds;
    @XmlElement(name = "VtngBlltElctrncAdr")
    protected CommunicationAddress11 vtngBlltElctrncAdr;
    @XmlElement(name = "VtngBlltReqAdr")
    protected PostalAddress1 vtngBlltReqAdr;
    @XmlElement(name = "RvcbltyDdln")
    protected DateFormat58Choice rvcbltyDdln;
    @XmlElement(name = "RvcbltyMktDdln")
    protected DateFormat58Choice rvcbltyMktDdln;
    @XmlElement(name = "BnfclOwnrDsclsr")
    protected Boolean bnfclOwnrDsclsr;
    @XmlElement(name = "EarlyIncntivPrm")
    protected IncentivePremium5 earlyIncntivPrm;
    @XmlElement(name = "IncntivPrm")
    protected IncentivePremium5 incntivPrm;
    @XmlElement(name = "EarlyVoteWthPrmDdln")
    protected DateFormat58Choice earlyVoteWthPrmDdln;
    @XmlElement(name = "VoteWthPrmDdln")
    protected DateFormat58Choice voteWthPrmDdln;
    @XmlElement(name = "VoteWthPrmMktDdln")
    protected DateFormat58Choice voteWthPrmMktDdln;
    @XmlElement(name = "AddtlVtngRqrmnts")
    protected String addtlVtngRqrmnts;
    @XmlElement(name = "PrvsInstrInvldtyInd")
    protected Boolean prvsInstrInvldtyInd;

    /**
     * Ruft den Wert der sctiesQtyReqrdToVote-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link FinancialInstrumentQuantity18Choice }
     *     
     */
    public FinancialInstrumentQuantity18Choice getSctiesQtyReqrdToVote() {
        return sctiesQtyReqrdToVote;
    }

    /**
     * Legt den Wert der sctiesQtyReqrdToVote-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link FinancialInstrumentQuantity18Choice }
     *     
     */
    public void setSctiesQtyReqrdToVote(FinancialInstrumentQuantity18Choice value) {
        this.sctiesQtyReqrdToVote = value;
    }

    /**
     * Ruft den Wert der prtlVoteAllwd-Eigenschaft ab.
     * 
     */
    public boolean isPrtlVoteAllwd() {
        return prtlVoteAllwd;
    }

    /**
     * Legt den Wert der prtlVoteAllwd-Eigenschaft fest.
     * 
     */
    public void setPrtlVoteAllwd(boolean value) {
        this.prtlVoteAllwd = value;
    }

    /**
     * Ruft den Wert der spltVoteAllwd-Eigenschaft ab.
     * 
     */
    public boolean isSpltVoteAllwd() {
        return spltVoteAllwd;
    }

    /**
     * Legt den Wert der spltVoteAllwd-Eigenschaft fest.
     * 
     */
    public void setSpltVoteAllwd(boolean value) {
        this.spltVoteAllwd = value;
    }

    /**
     * Ruft den Wert der voteDdln-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link DateFormat58Choice }
     *     
     */
    public DateFormat58Choice getVoteDdln() {
        return voteDdln;
    }

    /**
     * Legt den Wert der voteDdln-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link DateFormat58Choice }
     *     
     */
    public void setVoteDdln(DateFormat58Choice value) {
        this.voteDdln = value;
    }

    /**
     * Ruft den Wert der voteMktDdln-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link DateFormat58Choice }
     *     
     */
    public DateFormat58Choice getVoteMktDdln() {
        return voteMktDdln;
    }

    /**
     * Legt den Wert der voteMktDdln-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link DateFormat58Choice }
     *     
     */
    public void setVoteMktDdln(DateFormat58Choice value) {
        this.voteMktDdln = value;
    }

    /**
     * Ruft den Wert der voteMthds-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link VoteMethods5 }
     *     
     */
    public VoteMethods5 getVoteMthds() {
        return voteMthds;
    }

    /**
     * Legt den Wert der voteMthds-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link VoteMethods5 }
     *     
     */
    public void setVoteMthds(VoteMethods5 value) {
        this.voteMthds = value;
    }

    /**
     * Ruft den Wert der vtngBlltElctrncAdr-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CommunicationAddress11 }
     *     
     */
    public CommunicationAddress11 getVtngBlltElctrncAdr() {
        return vtngBlltElctrncAdr;
    }

    /**
     * Legt den Wert der vtngBlltElctrncAdr-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CommunicationAddress11 }
     *     
     */
    public void setVtngBlltElctrncAdr(CommunicationAddress11 value) {
        this.vtngBlltElctrncAdr = value;
    }

    /**
     * Ruft den Wert der vtngBlltReqAdr-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PostalAddress1 }
     *     
     */
    public PostalAddress1 getVtngBlltReqAdr() {
        return vtngBlltReqAdr;
    }

    /**
     * Legt den Wert der vtngBlltReqAdr-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PostalAddress1 }
     *     
     */
    public void setVtngBlltReqAdr(PostalAddress1 value) {
        this.vtngBlltReqAdr = value;
    }

    /**
     * Ruft den Wert der rvcbltyDdln-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link DateFormat58Choice }
     *     
     */
    public DateFormat58Choice getRvcbltyDdln() {
        return rvcbltyDdln;
    }

    /**
     * Legt den Wert der rvcbltyDdln-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link DateFormat58Choice }
     *     
     */
    public void setRvcbltyDdln(DateFormat58Choice value) {
        this.rvcbltyDdln = value;
    }

    /**
     * Ruft den Wert der rvcbltyMktDdln-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link DateFormat58Choice }
     *     
     */
    public DateFormat58Choice getRvcbltyMktDdln() {
        return rvcbltyMktDdln;
    }

    /**
     * Legt den Wert der rvcbltyMktDdln-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link DateFormat58Choice }
     *     
     */
    public void setRvcbltyMktDdln(DateFormat58Choice value) {
        this.rvcbltyMktDdln = value;
    }

    /**
     * Ruft den Wert der bnfclOwnrDsclsr-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isBnfclOwnrDsclsr() {
        return bnfclOwnrDsclsr;
    }

    /**
     * Legt den Wert der bnfclOwnrDsclsr-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setBnfclOwnrDsclsr(Boolean value) {
        this.bnfclOwnrDsclsr = value;
    }

    /**
     * Ruft den Wert der earlyIncntivPrm-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link IncentivePremium5 }
     *     
     */
    public IncentivePremium5 getEarlyIncntivPrm() {
        return earlyIncntivPrm;
    }

    /**
     * Legt den Wert der earlyIncntivPrm-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link IncentivePremium5 }
     *     
     */
    public void setEarlyIncntivPrm(IncentivePremium5 value) {
        this.earlyIncntivPrm = value;
    }

    /**
     * Ruft den Wert der incntivPrm-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link IncentivePremium5 }
     *     
     */
    public IncentivePremium5 getIncntivPrm() {
        return incntivPrm;
    }

    /**
     * Legt den Wert der incntivPrm-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link IncentivePremium5 }
     *     
     */
    public void setIncntivPrm(IncentivePremium5 value) {
        this.incntivPrm = value;
    }

    /**
     * Ruft den Wert der earlyVoteWthPrmDdln-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link DateFormat58Choice }
     *     
     */
    public DateFormat58Choice getEarlyVoteWthPrmDdln() {
        return earlyVoteWthPrmDdln;
    }

    /**
     * Legt den Wert der earlyVoteWthPrmDdln-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link DateFormat58Choice }
     *     
     */
    public void setEarlyVoteWthPrmDdln(DateFormat58Choice value) {
        this.earlyVoteWthPrmDdln = value;
    }

    /**
     * Ruft den Wert der voteWthPrmDdln-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link DateFormat58Choice }
     *     
     */
    public DateFormat58Choice getVoteWthPrmDdln() {
        return voteWthPrmDdln;
    }

    /**
     * Legt den Wert der voteWthPrmDdln-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link DateFormat58Choice }
     *     
     */
    public void setVoteWthPrmDdln(DateFormat58Choice value) {
        this.voteWthPrmDdln = value;
    }

    /**
     * Ruft den Wert der voteWthPrmMktDdln-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link DateFormat58Choice }
     *     
     */
    public DateFormat58Choice getVoteWthPrmMktDdln() {
        return voteWthPrmMktDdln;
    }

    /**
     * Legt den Wert der voteWthPrmMktDdln-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link DateFormat58Choice }
     *     
     */
    public void setVoteWthPrmMktDdln(DateFormat58Choice value) {
        this.voteWthPrmMktDdln = value;
    }

    /**
     * Ruft den Wert der addtlVtngRqrmnts-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAddtlVtngRqrmnts() {
        return addtlVtngRqrmnts;
    }

    /**
     * Legt den Wert der addtlVtngRqrmnts-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAddtlVtngRqrmnts(String value) {
        this.addtlVtngRqrmnts = value;
    }

    /**
     * Ruft den Wert der prvsInstrInvldtyInd-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isPrvsInstrInvldtyInd() {
        return prvsInstrInvldtyInd;
    }

    /**
     * Legt den Wert der prvsInstrInvldtyInd-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setPrvsInstrInvldtyInd(Boolean value) {
        this.prvsInstrInvldtyInd = value;
    }

}
