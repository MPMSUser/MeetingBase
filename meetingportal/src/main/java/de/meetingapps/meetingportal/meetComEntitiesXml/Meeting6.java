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
 * <p>Java-Klasse für Meeting6 complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="Meeting6"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="DtAndTm" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}DateFormat58Choice"/&gt;
 *         &lt;element name="DtSts" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}MeetingDateStatus2Code" minOccurs="0"/&gt;
 *         &lt;element name="QrmReqrd" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}YesNoIndicator" minOccurs="0"/&gt;
 *         &lt;element name="Lctn" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}LocationFormat1Choice" maxOccurs="5"/&gt;
 *         &lt;element name="QrmQty" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}QuorumQuantity1Choice" minOccurs="0"/&gt;
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
@XmlType(name = "Meeting6", propOrder = {
    "dtAndTm",
    "dtSts",
    "qrmReqrd",
    "lctn",
    "qrmQty",
    "urlAdr"
})
public class Meeting6 {

    @XmlElement(name = "DtAndTm", required = true)
    protected DateFormat58Choice dtAndTm;
    @XmlElement(name = "DtSts")
    @XmlSchemaType(name = "string")
    protected MeetingDateStatus2Code dtSts;
    @XmlElement(name = "QrmReqrd")
    protected Boolean qrmReqrd;
    @XmlElement(name = "Lctn", required = true)
    protected List<LocationFormat1Choice> lctn;
    @XmlElement(name = "QrmQty")
    protected QuorumQuantity1Choice qrmQty;
    @XmlElement(name = "URLAdr")
    protected String urlAdr;

    /**
     * Ruft den Wert der dtAndTm-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link DateFormat58Choice }
     *     
     */
    public DateFormat58Choice getDtAndTm() {
        return dtAndTm;
    }

    /**
     * Legt den Wert der dtAndTm-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link DateFormat58Choice }
     *     
     */
    public void setDtAndTm(DateFormat58Choice value) {
        this.dtAndTm = value;
    }

    /**
     * Ruft den Wert der dtSts-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link MeetingDateStatus2Code }
     *     
     */
    public MeetingDateStatus2Code getDtSts() {
        return dtSts;
    }

    /**
     * Legt den Wert der dtSts-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link MeetingDateStatus2Code }
     *     
     */
    public void setDtSts(MeetingDateStatus2Code value) {
        this.dtSts = value;
    }

    /**
     * Ruft den Wert der qrmReqrd-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isQrmReqrd() {
        return qrmReqrd;
    }

    /**
     * Legt den Wert der qrmReqrd-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setQrmReqrd(Boolean value) {
        this.qrmReqrd = value;
    }

    /**
     * Gets the value of the lctn property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a <CODE>set</CODE> method for the lctn property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLctn().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link LocationFormat1Choice }
     * 
     * 
     */
    public List<LocationFormat1Choice> getLctn() {
        if (lctn == null) {
            lctn = new ArrayList<LocationFormat1Choice>();
        }
        return this.lctn;
    }

    /**
     * Ruft den Wert der qrmQty-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link QuorumQuantity1Choice }
     *     
     */
    public QuorumQuantity1Choice getQrmQty() {
        return qrmQty;
    }

    /**
     * Legt den Wert der qrmQty-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link QuorumQuantity1Choice }
     *     
     */
    public void setQrmQty(QuorumQuantity1Choice value) {
        this.qrmQty = value;
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
