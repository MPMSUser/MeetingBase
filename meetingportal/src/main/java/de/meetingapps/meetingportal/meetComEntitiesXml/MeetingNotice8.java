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
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für MeetingNotice8 complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="MeetingNotice8"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="MtgId" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}Max35Text"/&gt;
 *         &lt;element name="IssrMtgId" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}Max35Text" minOccurs="0"/&gt;
 *         &lt;element name="Tp" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}MeetingType4Code"/&gt;
 *         &lt;element name="Clssfctn" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}MeetingTypeClassification2Choice" minOccurs="0"/&gt;
 *         &lt;element name="AnncmntDt" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}DateAndDateTime2Choice" minOccurs="0"/&gt;
 *         &lt;element name="OneManOneVoteInd" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}YesNoIndicator" minOccurs="0"/&gt;
 *         &lt;element name="Prtcptn" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}ParticipationMethod2" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="Attndnc" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}Attendance2" minOccurs="0"/&gt;
 *         &lt;element name="AddtlDcmnttnURLAdr" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}Max2048Text" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="EvtPrcgWebSiteAdr" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}Max2048Text" minOccurs="0"/&gt;
 *         &lt;element name="AddtlPrcdrDtls" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}AdditionalRights3" maxOccurs="5" minOccurs="0"/&gt;
 *         &lt;element name="TtlNbOfSctiesOutsdng" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}FinancialInstrumentQuantity18Choice" minOccurs="0"/&gt;
 *         &lt;element name="TtlNbOfVtngRghts" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}Number" minOccurs="0"/&gt;
 *         &lt;element name="PrxyAppntmntNtfctnAdr" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}PostalAddress1" minOccurs="0"/&gt;
 *         &lt;element name="PrxyChc" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}Proxy5Choice" minOccurs="0"/&gt;
 *         &lt;element name="CtctPrsnDtls" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}MeetingContactPerson3" maxOccurs="12" minOccurs="0"/&gt;
 *         &lt;element name="RsltPblctnDt" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}DateFormat3Choice" minOccurs="0"/&gt;
 *         &lt;element name="SctiesBlckgPrdEndDt" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}DateFormat60Choice" minOccurs="0"/&gt;
 *         &lt;element name="EntitlmntFxgDt" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}DateFormat1" minOccurs="0"/&gt;
 *         &lt;element name="RegnSctiesDdln" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}DateFormat58Choice" minOccurs="0"/&gt;
 *         &lt;element name="RegnSctiesMktDdln" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}DateFormat58Choice" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MeetingNotice8", propOrder = {
    "mtgId",
    "issrMtgId",
    "tp",
    "clssfctn",
    "anncmntDt",
    "oneManOneVoteInd",
    "prtcptn",
    "attndnc",
    "addtlDcmnttnURLAdr",
    "evtPrcgWebSiteAdr",
    "addtlPrcdrDtls",
    "ttlNbOfSctiesOutsdng",
    "ttlNbOfVtngRghts",
    "prxyAppntmntNtfctnAdr",
    "prxyChc",
    "ctctPrsnDtls",
    "rsltPblctnDt",
    "sctiesBlckgPrdEndDt",
    "entitlmntFxgDt",
    "regnSctiesDdln",
    "regnSctiesMktDdln"
})
public class MeetingNotice8 {

    @XmlElement(name = "MtgId", required = true)
    protected String mtgId;
    @XmlElement(name = "IssrMtgId")
    protected String issrMtgId;
    @XmlElement(name = "Tp", required = true)
    @XmlSchemaType(name = "string")
    protected MeetingType4Code tp;
    @XmlElement(name = "Clssfctn")
    protected MeetingTypeClassification2Choice clssfctn;
    @XmlElement(name = "AnncmntDt")
    protected DateAndDateTime2Choice anncmntDt;
    @XmlElement(name = "OneManOneVoteInd")
    protected Boolean oneManOneVoteInd;
    @XmlElement(name = "Prtcptn")
    protected List<ParticipationMethod2> prtcptn;
    @XmlElement(name = "Attndnc")
    protected Attendance2 attndnc;
    @XmlElement(name = "AddtlDcmnttnURLAdr")
    protected List<String> addtlDcmnttnURLAdr;
    @XmlElement(name = "EvtPrcgWebSiteAdr")
    protected String evtPrcgWebSiteAdr;
    @XmlElement(name = "AddtlPrcdrDtls")
    protected List<AdditionalRights3> addtlPrcdrDtls;
    @XmlElement(name = "TtlNbOfSctiesOutsdng")
    protected FinancialInstrumentQuantity18Choice ttlNbOfSctiesOutsdng;
    @XmlElement(name = "TtlNbOfVtngRghts")
    protected BigDecimal ttlNbOfVtngRghts;
    @XmlElement(name = "PrxyAppntmntNtfctnAdr")
    protected PostalAddress1 prxyAppntmntNtfctnAdr;
    @XmlElement(name = "PrxyChc")
    protected Proxy5Choice prxyChc;
    @XmlElement(name = "CtctPrsnDtls")
    protected List<MeetingContactPerson3> ctctPrsnDtls;
    @XmlElement(name = "RsltPblctnDt")
    protected DateFormat3Choice rsltPblctnDt;
    @XmlElement(name = "SctiesBlckgPrdEndDt")
    protected DateFormat60Choice sctiesBlckgPrdEndDt;
    @XmlElement(name = "EntitlmntFxgDt")
    protected DateFormat1 entitlmntFxgDt;
    @XmlElement(name = "RegnSctiesDdln")
    protected DateFormat58Choice regnSctiesDdln;
    @XmlElement(name = "RegnSctiesMktDdln")
    protected DateFormat58Choice regnSctiesMktDdln;

    /**
     * Ruft den Wert der mtgId-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMtgId() {
        return mtgId;
    }

    /**
     * Legt den Wert der mtgId-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMtgId(String value) {
        this.mtgId = value;
    }

    /**
     * Ruft den Wert der issrMtgId-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIssrMtgId() {
        return issrMtgId;
    }

    /**
     * Legt den Wert der issrMtgId-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIssrMtgId(String value) {
        this.issrMtgId = value;
    }

    /**
     * Ruft den Wert der tp-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link MeetingType4Code }
     *     
     */
    public MeetingType4Code getTp() {
        return tp;
    }

    /**
     * Legt den Wert der tp-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link MeetingType4Code }
     *     
     */
    public void setTp(MeetingType4Code value) {
        this.tp = value;
    }

    /**
     * Ruft den Wert der clssfctn-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link MeetingTypeClassification2Choice }
     *     
     */
    public MeetingTypeClassification2Choice getClssfctn() {
        return clssfctn;
    }

    /**
     * Legt den Wert der clssfctn-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link MeetingTypeClassification2Choice }
     *     
     */
    public void setClssfctn(MeetingTypeClassification2Choice value) {
        this.clssfctn = value;
    }

    /**
     * Ruft den Wert der anncmntDt-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link DateAndDateTime2Choice }
     *     
     */
    public DateAndDateTime2Choice getAnncmntDt() {
        return anncmntDt;
    }

    /**
     * Legt den Wert der anncmntDt-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link DateAndDateTime2Choice }
     *     
     */
    public void setAnncmntDt(DateAndDateTime2Choice value) {
        this.anncmntDt = value;
    }

    /**
     * Ruft den Wert der oneManOneVoteInd-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isOneManOneVoteInd() {
        return oneManOneVoteInd;
    }

    /**
     * Legt den Wert der oneManOneVoteInd-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setOneManOneVoteInd(Boolean value) {
        this.oneManOneVoteInd = value;
    }

    /**
     * Gets the value of the prtcptn property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a <CODE>set</CODE> method for the prtcptn property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPrtcptn().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ParticipationMethod2 }
     * 
     * 
     */
    public List<ParticipationMethod2> getPrtcptn() {
        if (prtcptn == null) {
            prtcptn = new ArrayList<ParticipationMethod2>();
        }
        return this.prtcptn;
    }

    /**
     * Ruft den Wert der attndnc-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Attendance2 }
     *     
     */
    public Attendance2 getAttndnc() {
        return attndnc;
    }

    /**
     * Legt den Wert der attndnc-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Attendance2 }
     *     
     */
    public void setAttndnc(Attendance2 value) {
        this.attndnc = value;
    }

    /**
     * Gets the value of the addtlDcmnttnURLAdr property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a <CODE>set</CODE> method for the addtlDcmnttnURLAdr property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAddtlDcmnttnURLAdr().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getAddtlDcmnttnURLAdr() {
        if (addtlDcmnttnURLAdr == null) {
            addtlDcmnttnURLAdr = new ArrayList<String>();
        }
        return this.addtlDcmnttnURLAdr;
    }

    /**
     * Ruft den Wert der evtPrcgWebSiteAdr-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEvtPrcgWebSiteAdr() {
        return evtPrcgWebSiteAdr;
    }

    /**
     * Legt den Wert der evtPrcgWebSiteAdr-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEvtPrcgWebSiteAdr(String value) {
        this.evtPrcgWebSiteAdr = value;
    }

    /**
     * Gets the value of the addtlPrcdrDtls property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a <CODE>set</CODE> method for the addtlPrcdrDtls property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAddtlPrcdrDtls().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AdditionalRights3 }
     * 
     * 
     */
    public List<AdditionalRights3> getAddtlPrcdrDtls() {
        if (addtlPrcdrDtls == null) {
            addtlPrcdrDtls = new ArrayList<AdditionalRights3>();
        }
        return this.addtlPrcdrDtls;
    }

    /**
     * Ruft den Wert der ttlNbOfSctiesOutsdng-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link FinancialInstrumentQuantity18Choice }
     *     
     */
    public FinancialInstrumentQuantity18Choice getTtlNbOfSctiesOutsdng() {
        return ttlNbOfSctiesOutsdng;
    }

    /**
     * Legt den Wert der ttlNbOfSctiesOutsdng-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link FinancialInstrumentQuantity18Choice }
     *     
     */
    public void setTtlNbOfSctiesOutsdng(FinancialInstrumentQuantity18Choice value) {
        this.ttlNbOfSctiesOutsdng = value;
    }

    /**
     * Ruft den Wert der ttlNbOfVtngRghts-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getTtlNbOfVtngRghts() {
        return ttlNbOfVtngRghts;
    }

    /**
     * Legt den Wert der ttlNbOfVtngRghts-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setTtlNbOfVtngRghts(BigDecimal value) {
        this.ttlNbOfVtngRghts = value;
    }

    /**
     * Ruft den Wert der prxyAppntmntNtfctnAdr-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PostalAddress1 }
     *     
     */
    public PostalAddress1 getPrxyAppntmntNtfctnAdr() {
        return prxyAppntmntNtfctnAdr;
    }

    /**
     * Legt den Wert der prxyAppntmntNtfctnAdr-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PostalAddress1 }
     *     
     */
    public void setPrxyAppntmntNtfctnAdr(PostalAddress1 value) {
        this.prxyAppntmntNtfctnAdr = value;
    }

    /**
     * Ruft den Wert der prxyChc-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Proxy5Choice }
     *     
     */
    public Proxy5Choice getPrxyChc() {
        return prxyChc;
    }

    /**
     * Legt den Wert der prxyChc-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Proxy5Choice }
     *     
     */
    public void setPrxyChc(Proxy5Choice value) {
        this.prxyChc = value;
    }

    /**
     * Gets the value of the ctctPrsnDtls property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a <CODE>set</CODE> method for the ctctPrsnDtls property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCtctPrsnDtls().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link MeetingContactPerson3 }
     * 
     * 
     */
    public List<MeetingContactPerson3> getCtctPrsnDtls() {
        if (ctctPrsnDtls == null) {
            ctctPrsnDtls = new ArrayList<MeetingContactPerson3>();
        }
        return this.ctctPrsnDtls;
    }

    /**
     * Ruft den Wert der rsltPblctnDt-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link DateFormat3Choice }
     *     
     */
    public DateFormat3Choice getRsltPblctnDt() {
        return rsltPblctnDt;
    }

    /**
     * Legt den Wert der rsltPblctnDt-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link DateFormat3Choice }
     *     
     */
    public void setRsltPblctnDt(DateFormat3Choice value) {
        this.rsltPblctnDt = value;
    }

    /**
     * Ruft den Wert der sctiesBlckgPrdEndDt-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link DateFormat60Choice }
     *     
     */
    public DateFormat60Choice getSctiesBlckgPrdEndDt() {
        return sctiesBlckgPrdEndDt;
    }

    /**
     * Legt den Wert der sctiesBlckgPrdEndDt-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link DateFormat60Choice }
     *     
     */
    public void setSctiesBlckgPrdEndDt(DateFormat60Choice value) {
        this.sctiesBlckgPrdEndDt = value;
    }

    /**
     * Ruft den Wert der entitlmntFxgDt-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link DateFormat1 }
     *     
     */
    public DateFormat1 getEntitlmntFxgDt() {
        return entitlmntFxgDt;
    }

    /**
     * Legt den Wert der entitlmntFxgDt-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link DateFormat1 }
     *     
     */
    public void setEntitlmntFxgDt(DateFormat1 value) {
        this.entitlmntFxgDt = value;
    }

    /**
     * Ruft den Wert der regnSctiesDdln-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link DateFormat58Choice }
     *     
     */
    public DateFormat58Choice getRegnSctiesDdln() {
        return regnSctiesDdln;
    }

    /**
     * Legt den Wert der regnSctiesDdln-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link DateFormat58Choice }
     *     
     */
    public void setRegnSctiesDdln(DateFormat58Choice value) {
        this.regnSctiesDdln = value;
    }

    /**
     * Ruft den Wert der regnSctiesMktDdln-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link DateFormat58Choice }
     *     
     */
    public DateFormat58Choice getRegnSctiesMktDdln() {
        return regnSctiesMktDdln;
    }

    /**
     * Legt den Wert der regnSctiesMktDdln-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link DateFormat58Choice }
     *     
     */
    public void setRegnSctiesMktDdln(DateFormat58Choice value) {
        this.regnSctiesMktDdln = value;
    }

}
