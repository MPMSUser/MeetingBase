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
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für Resolution7 complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="Resolution7"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="IssrLabl" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}Max35Text"/&gt;
 *         &lt;element name="Desc" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}ItemDescription2" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="ListgGrpRsltnLabl" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}Max35Text" minOccurs="0"/&gt;
 *         &lt;element name="Tp" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}ResolutionType2Code" minOccurs="0"/&gt;
 *         &lt;element name="ForInfOnly" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}YesNoIndicator"/&gt;
 *         &lt;element name="VoteTp" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}VoteType1Code" minOccurs="0"/&gt;
 *         &lt;element name="Sts" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}ResolutionStatus1Code"/&gt;
 *         &lt;element name="SubmittdBySctyHldr" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}YesNoIndicator" minOccurs="0"/&gt;
 *         &lt;element name="RghtToWdrwInd" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}YesNoIndicator" minOccurs="0"/&gt;
 *         &lt;element name="VoteInstrTp" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}VoteInstructionType1" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="MgmtRcmmndtn" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}VoteInstruction5Code" minOccurs="0"/&gt;
 *         &lt;element name="NtifngPtyRcmmndtn" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}VoteInstruction5Code" minOccurs="0"/&gt;
 *         &lt;element name="Entitlmnt" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}Entitlement1Choice" minOccurs="0"/&gt;
 *         &lt;element name="VtngRghtsThrshldForApprvl" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}VotingRightsThreshold1" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="URLAdr" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}Max2048Text" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Resolution7", propOrder = {
    "issrLabl",
    "desc",
    "listgGrpRsltnLabl",
    "tp",
    "forInfOnly",
    "voteTp",
    "sts",
    "submittdBySctyHldr",
    "rghtToWdrwInd",
    "voteInstrTp",
    "mgmtRcmmndtn",
    "ntifngPtyRcmmndtn",
    "entitlmnt",
    "vtngRghtsThrshldForApprvl",
    "urlAdr"
})
public class Resolution7 {

    @XmlElement(name = "IssrLabl", required = true)
    protected String issrLabl = "";
    @XmlElement(name = "Desc")
    protected List<ItemDescription2> desc;
    @XmlElement(name = "ListgGrpRsltnLabl")
    protected String listgGrpRsltnLabl;
    @XmlElement(name = "Tp")
    @XmlSchemaType(name = "string")
    protected ResolutionType2Code tp;
    @XmlElement(name = "ForInfOnly")
    protected boolean forInfOnly;
    @XmlElement(name = "VoteTp")
    @XmlSchemaType(name = "string")
    protected VoteType1Code voteTp = VoteType1Code.BNDG;
    @XmlElement(name = "Sts", required = true)
    @XmlSchemaType(name = "string")
    protected ResolutionStatus1Code sts = ResolutionStatus1Code.ACTV;;
    @XmlElement(name = "SubmittdBySctyHldr")
    protected Boolean submittdBySctyHldr;
    @XmlElement(name = "RghtToWdrwInd")
    protected Boolean rghtToWdrwInd;
    @XmlElement(name = "VoteInstrTp")
    protected List<VoteInstructionType1> voteInstrTp;
    @XmlElement(name = "MgmtRcmmndtn")
    @XmlSchemaType(name = "string")
    protected VoteInstruction5Code mgmtRcmmndtn;
    @XmlElement(name = "NtifngPtyRcmmndtn")
    @XmlSchemaType(name = "string")
    protected VoteInstruction5Code ntifngPtyRcmmndtn;
    @XmlElement(name = "Entitlmnt")
    protected Entitlement1Choice entitlmnt;
    @XmlElement(name = "VtngRghtsThrshldForApprvl")
    protected List<VotingRightsThreshold1> vtngRghtsThrshldForApprvl;
    @XmlElement(name = "URLAdr")
    protected String urlAdr;

    /**
     * Ruft den Wert der issrLabl-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIssrLabl() {
        return issrLabl;
    }

    /**
     * Legt den Wert der issrLabl-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIssrLabl(String value) {
        this.issrLabl = value;
    }

    /**
     * Gets the value of the desc property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a <CODE>set</CODE> method for the desc property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDesc().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ItemDescription2 }
     * 
     * 
     */
    public List<ItemDescription2> getDesc() {
        if (desc == null) {
            desc = new ArrayList<ItemDescription2>();
        }
        return this.desc;
    }

    /**
     * Ruft den Wert der listgGrpRsltnLabl-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getListgGrpRsltnLabl() {
        return listgGrpRsltnLabl;
    }

    /**
     * Legt den Wert der listgGrpRsltnLabl-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setListgGrpRsltnLabl(String value) {
        this.listgGrpRsltnLabl = value;
    }

    /**
     * Ruft den Wert der tp-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link ResolutionType2Code }
     *     
     */
    public ResolutionType2Code getTp() {
        return tp;
    }

    /**
     * Legt den Wert der tp-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link ResolutionType2Code }
     *     
     */
    public void setTp(ResolutionType2Code value) {
        this.tp = value;
    }

    /**
     * Ruft den Wert der forInfOnly-Eigenschaft ab.
     * 
     */
    public boolean isForInfOnly() {
        return forInfOnly;
    }

    /**
     * Legt den Wert der forInfOnly-Eigenschaft fest.
     * 
     */
    public void setForInfOnly(boolean value) {
        this.forInfOnly = value;
    }

    /**
     * Ruft den Wert der voteTp-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link VoteType1Code }
     *     
     */
    public VoteType1Code getVoteTp() {
        return voteTp;
    }

    /**
     * Legt den Wert der voteTp-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link VoteType1Code }
     *     
     */
    public void setVoteTp(VoteType1Code value) {
        this.voteTp = value;
    }

    /**
     * Ruft den Wert der sts-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link ResolutionStatus1Code }
     *     
     */
    public ResolutionStatus1Code getSts() {
        return sts;
    }

    /**
     * Legt den Wert der sts-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link ResolutionStatus1Code }
     *     
     */
    public void setSts(ResolutionStatus1Code value) {
        this.sts = value;
    }

    /**
     * Ruft den Wert der submittdBySctyHldr-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isSubmittdBySctyHldr() {
        return submittdBySctyHldr;
    }

    /**
     * Legt den Wert der submittdBySctyHldr-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setSubmittdBySctyHldr(Boolean value) {
        this.submittdBySctyHldr = value;
    }

    /**
     * Ruft den Wert der rghtToWdrwInd-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isRghtToWdrwInd() {
        return rghtToWdrwInd;
    }

    /**
     * Legt den Wert der rghtToWdrwInd-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setRghtToWdrwInd(Boolean value) {
        this.rghtToWdrwInd = value;
    }

    /**
     * Gets the value of the voteInstrTp property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a <CODE>set</CODE> method for the voteInstrTp property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getVoteInstrTp().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link VoteInstructionType1 }
     * 
     * 
     */
    public List<VoteInstructionType1> getVoteInstrTp() {
        if (voteInstrTp == null) {
            voteInstrTp = new ArrayList<VoteInstructionType1>();
        }
        return this.voteInstrTp;
    }

    /**
     * Ruft den Wert der mgmtRcmmndtn-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link VoteInstruction5Code }
     *     
     */
    public VoteInstruction5Code getMgmtRcmmndtn() {
        return mgmtRcmmndtn;
    }

    /**
     * Legt den Wert der mgmtRcmmndtn-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link VoteInstruction5Code }
     *     
     */
    public void setMgmtRcmmndtn(VoteInstruction5Code value) {
        this.mgmtRcmmndtn = value;
    }

    /**
     * Ruft den Wert der ntifngPtyRcmmndtn-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link VoteInstruction5Code }
     *     
     */
    public VoteInstruction5Code getNtifngPtyRcmmndtn() {
        return ntifngPtyRcmmndtn;
    }

    /**
     * Legt den Wert der ntifngPtyRcmmndtn-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link VoteInstruction5Code }
     *     
     */
    public void setNtifngPtyRcmmndtn(VoteInstruction5Code value) {
        this.ntifngPtyRcmmndtn = value;
    }

    /**
     * Ruft den Wert der entitlmnt-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Entitlement1Choice }
     *     
     */
    public Entitlement1Choice getEntitlmnt() {
        return entitlmnt;
    }

    /**
     * Legt den Wert der entitlmnt-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Entitlement1Choice }
     *     
     */
    public void setEntitlmnt(Entitlement1Choice value) {
        this.entitlmnt = value;
    }

    /**
     * Gets the value of the vtngRghtsThrshldForApprvl property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a <CODE>set</CODE> method for the vtngRghtsThrshldForApprvl property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getVtngRghtsThrshldForApprvl().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link VotingRightsThreshold1 }
     * 
     * 
     */
    public List<VotingRightsThreshold1> getVtngRghtsThrshldForApprvl() {
        if (vtngRghtsThrshldForApprvl == null) {
            vtngRghtsThrshldForApprvl = new ArrayList<VotingRightsThreshold1>();
        }
        return this.vtngRghtsThrshldForApprvl;
    }

    /**
     * Ruft den Wert der urlAdr-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getURLAdr() {
        return urlAdr;
    }

    /**
     * Legt den Wert der urlAdr-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setURLAdr(String value) {
        this.urlAdr = value;
    }

}
