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
 * <p>Java-Klasse für MeetingNotificationV11 complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="MeetingNotificationV11"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="Pgntn" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}Pagination1" minOccurs="0"/&gt;
 *         &lt;element name="NtfctnGnlInf" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}NotificationGeneralInformation4"/&gt;
 *         &lt;element name="NtfctnUpd" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}NotificationUpdate2" minOccurs="0"/&gt;
 *         &lt;element name="EvtsLkg" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}MeetingEventReference1" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="Mtg" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}MeetingNotice8"/&gt;
 *         &lt;element name="MtgDtls" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}Meeting6" maxOccurs="5"/&gt;
 *         &lt;element name="Issr" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}IssuerInformation3"/&gt;
 *         &lt;element name="IssrAgt" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}IssuerAgent3" maxOccurs="10" minOccurs="0"/&gt;
 *         &lt;element name="Scty" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}SecurityPosition16" maxOccurs="200"/&gt;
 *         &lt;element name="Rsltn" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}Resolution7" maxOccurs="1000" minOccurs="0"/&gt;
 *         &lt;element name="Vote" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}VoteParameters8" minOccurs="0"/&gt;
 *         &lt;element name="PwrOfAttnyRqrmnts" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}PowerOfAttorneyRequirements4" minOccurs="0"/&gt;
 *         &lt;element name="AddtlInf" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}CorporateEventNarrative4" minOccurs="0"/&gt;
 *         &lt;element name="SplmtryData" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}SupplementaryData1" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MeetingNotificationV11", propOrder = {
    "pgntn",
    "ntfctnGnlInf",
    "ntfctnUpd",
    "evtsLkg",
    "mtg",
    "mtgDtls",
    "issr",
    "issrAgt",
    "scty",
    "rsltn",
    "vote",
    "pwrOfAttnyRqrmnts",
    "addtlInf",
    "splmtryData"
})
public class MeetingNotificationV11 {

    @XmlElement(name = "Pgntn")
    protected Pagination1 pgntn;
    @XmlElement(name = "NtfctnGnlInf", required = true)
    protected NotificationGeneralInformation4 ntfctnGnlInf;
    @XmlElement(name = "NtfctnUpd")
    protected NotificationUpdate2 ntfctnUpd;
    @XmlElement(name = "EvtsLkg")
    protected List<MeetingEventReference1> evtsLkg;
    @XmlElement(name = "Mtg", required = true)
    protected MeetingNotice8 mtg;
    @XmlElement(name = "MtgDtls", required = true)
    protected List<Meeting6> mtgDtls;
    @XmlElement(name = "Issr", required = true)
    protected IssuerInformation3 issr;
    @XmlElement(name = "IssrAgt")
    protected List<IssuerAgent3> issrAgt;
    @XmlElement(name = "Scty", required = true)
    protected List<SecurityPosition16> scty;
    @XmlElement(name = "Rsltn")
    protected List<Resolution7> rsltn;
    @XmlElement(name = "Vote")
    protected VoteParameters8 vote;
    @XmlElement(name = "PwrOfAttnyRqrmnts")
    protected PowerOfAttorneyRequirements4 pwrOfAttnyRqrmnts;
    @XmlElement(name = "AddtlInf")
    protected CorporateEventNarrative4 addtlInf;
    @XmlElement(name = "SplmtryData")
    protected List<SupplementaryData1> splmtryData;

    /**
     * Ruft den Wert der pgntn-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Pagination1 }
     *     
     */
    public Pagination1 getPgntn() {
        return pgntn;
    }

    /**
     * Legt den Wert der pgntn-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Pagination1 }
     *     
     */
    public void setPgntn(Pagination1 value) {
        this.pgntn = value;
    }

    /**
     * Ruft den Wert der ntfctnGnlInf-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link NotificationGeneralInformation4 }
     *     
     */
    public NotificationGeneralInformation4 getNtfctnGnlInf() {
        return ntfctnGnlInf;
    }

    /**
     * Legt den Wert der ntfctnGnlInf-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link NotificationGeneralInformation4 }
     *     
     */
    public void setNtfctnGnlInf(NotificationGeneralInformation4 value) {
        this.ntfctnGnlInf = value;
    }

    /**
     * Ruft den Wert der ntfctnUpd-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link NotificationUpdate2 }
     *     
     */
    public NotificationUpdate2 getNtfctnUpd() {
        return ntfctnUpd;
    }

    /**
     * Legt den Wert der ntfctnUpd-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link NotificationUpdate2 }
     *     
     */
    public void setNtfctnUpd(NotificationUpdate2 value) {
        this.ntfctnUpd = value;
    }

    /**
     * Gets the value of the evtsLkg property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a <CODE>set</CODE> method for the evtsLkg property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getEvtsLkg().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link MeetingEventReference1 }
     * 
     * 
     */
    public List<MeetingEventReference1> getEvtsLkg() {
        if (evtsLkg == null) {
            evtsLkg = new ArrayList<MeetingEventReference1>();
        }
        return this.evtsLkg;
    }

    /**
     * Ruft den Wert der mtg-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link MeetingNotice8 }
     *     
     */
    public MeetingNotice8 getMtg() {
        return mtg;
    }

    /**
     * Legt den Wert der mtg-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link MeetingNotice8 }
     *     
     */
    public void setMtg(MeetingNotice8 value) {
        this.mtg = value;
    }

    /**
     * Gets the value of the mtgDtls property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a <CODE>set</CODE> method for the mtgDtls property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMtgDtls().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Meeting6 }
     * 
     * 
     */
    public List<Meeting6> getMtgDtls() {
        if (mtgDtls == null) {
            mtgDtls = new ArrayList<Meeting6>();
        }
        return this.mtgDtls;
    }

    /**
     * Ruft den Wert der issr-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link IssuerInformation3 }
     *     
     */
    public IssuerInformation3 getIssr() {
        return issr;
    }

    /**
     * Legt den Wert der issr-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link IssuerInformation3 }
     *     
     */
    public void setIssr(IssuerInformation3 value) {
        this.issr = value;
    }

    /**
     * Gets the value of the issrAgt property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a <CODE>set</CODE> method for the issrAgt property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getIssrAgt().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link IssuerAgent3 }
     * 
     * 
     */
    public List<IssuerAgent3> getIssrAgt() {
        if (issrAgt == null) {
            issrAgt = new ArrayList<IssuerAgent3>();
        }
        return this.issrAgt;
    }

    /**
     * Gets the value of the scty property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a <CODE>set</CODE> method for the scty property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getScty().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SecurityPosition16 }
     * 
     * 
     */
    public List<SecurityPosition16> getScty() {
        if (scty == null) {
            scty = new ArrayList<SecurityPosition16>();
        }
        return this.scty;
    }

    /**
     * Gets the value of the rsltn property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a <CODE>set</CODE> method for the rsltn property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRsltn().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Resolution7 }
     * 
     * 
     */
    public List<Resolution7> getRsltn() {
        if (rsltn == null) {
            rsltn = new ArrayList<Resolution7>();
        }
        return this.rsltn;
    }

    /**
     * Ruft den Wert der vote-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link VoteParameters8 }
     *     
     */
    public VoteParameters8 getVote() {
        return vote;
    }

    /**
     * Legt den Wert der vote-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link VoteParameters8 }
     *     
     */
    public void setVote(VoteParameters8 value) {
        this.vote = value;
    }

    /**
     * Ruft den Wert der pwrOfAttnyRqrmnts-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PowerOfAttorneyRequirements4 }
     *     
     */
    public PowerOfAttorneyRequirements4 getPwrOfAttnyRqrmnts() {
        return pwrOfAttnyRqrmnts;
    }

    /**
     * Legt den Wert der pwrOfAttnyRqrmnts-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PowerOfAttorneyRequirements4 }
     *     
     */
    public void setPwrOfAttnyRqrmnts(PowerOfAttorneyRequirements4 value) {
        this.pwrOfAttnyRqrmnts = value;
    }

    /**
     * Ruft den Wert der addtlInf-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CorporateEventNarrative4 }
     *     
     */
    public CorporateEventNarrative4 getAddtlInf() {
        return addtlInf;
    }

    /**
     * Legt den Wert der addtlInf-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CorporateEventNarrative4 }
     *     
     */
    public void setAddtlInf(CorporateEventNarrative4 value) {
        this.addtlInf = value;
    }

    /**
     * Gets the value of the splmtryData property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a <CODE>set</CODE> method for the splmtryData property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSplmtryData().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SupplementaryData1 }
     * 
     * 
     */
    public List<SupplementaryData1> getSplmtryData() {
        if (splmtryData == null) {
            splmtryData = new ArrayList<SupplementaryData1>();
        }
        return this.splmtryData;
    }

}
