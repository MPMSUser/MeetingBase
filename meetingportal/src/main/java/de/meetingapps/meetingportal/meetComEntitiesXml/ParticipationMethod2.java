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
 * <p>Java-Klasse für ParticipationMethod2 complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="ParticipationMethod2"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="PrtcptnMtd" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}ParticipationMethod3Choice"/&gt;
 *         &lt;element name="IssrDdlnForVtng" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}DateFormat58Choice"/&gt;
 *         &lt;element name="SpprtdByAcctSvcr" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}YesNoIndicator" minOccurs="0"/&gt;
 *         &lt;element name="RspnDdlnForVtng" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}DateFormat58Choice" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ParticipationMethod2", propOrder = {
    "prtcptnMtd",
    "issrDdlnForVtng",
    "spprtdByAcctSvcr",
    "rspnDdlnForVtng"
})
public class ParticipationMethod2 {

    @XmlElement(name = "PrtcptnMtd", required = true)
    protected ParticipationMethod3Choice prtcptnMtd;
    @XmlElement(name = "IssrDdlnForVtng", required = true)
    protected DateFormat58Choice issrDdlnForVtng;
    @XmlElement(name = "SpprtdByAcctSvcr")
    protected Boolean spprtdByAcctSvcr;
    @XmlElement(name = "RspnDdlnForVtng")
    protected DateFormat58Choice rspnDdlnForVtng;

    /**
     * Ruft den Wert der prtcptnMtd-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link ParticipationMethod3Choice }
     *     
     */
    public ParticipationMethod3Choice getPrtcptnMtd() {
        return prtcptnMtd;
    }

    /**
     * Legt den Wert der prtcptnMtd-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link ParticipationMethod3Choice }
     *     
     */
    public void setPrtcptnMtd(ParticipationMethod3Choice value) {
        this.prtcptnMtd = value;
    }

    /**
     * Ruft den Wert der issrDdlnForVtng-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link DateFormat58Choice }
     *     
     */
    public DateFormat58Choice getIssrDdlnForVtng() {
        return issrDdlnForVtng;
    }

    /**
     * Legt den Wert der issrDdlnForVtng-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link DateFormat58Choice }
     *     
     */
    public void setIssrDdlnForVtng(DateFormat58Choice value) {
        this.issrDdlnForVtng = value;
    }

    /**
     * Ruft den Wert der spprtdByAcctSvcr-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isSpprtdByAcctSvcr() {
        return spprtdByAcctSvcr;
    }

    /**
     * Legt den Wert der spprtdByAcctSvcr-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setSpprtdByAcctSvcr(Boolean value) {
        this.spprtdByAcctSvcr = value;
    }

    /**
     * Ruft den Wert der rspnDdlnForVtng-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link DateFormat58Choice }
     *     
     */
    public DateFormat58Choice getRspnDdlnForVtng() {
        return rspnDdlnForVtng;
    }

    /**
     * Legt den Wert der rspnDdlnForVtng-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link DateFormat58Choice }
     *     
     */
    public void setRspnDdlnForVtng(DateFormat58Choice value) {
        this.rspnDdlnForVtng = value;
    }

}
