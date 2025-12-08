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
 * <p>Java-Klasse für Attendance2 complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="Attendance2"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="AdmssnConds" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}AttendanceAdmissionConditions2" maxOccurs="7" minOccurs="0"/&gt;
 *         &lt;element name="ConfInf" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}Max350Text" minOccurs="0"/&gt;
 *         &lt;element name="ConfDdln" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}DateFormat58Choice" minOccurs="0"/&gt;
 *         &lt;element name="ConfMktDdln" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}DateFormat58Choice" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Attendance2", propOrder = {
    "admssnConds",
    "confInf",
    "confDdln",
    "confMktDdln"
})
public class Attendance2 {

    @XmlElement(name = "AdmssnConds")
    protected List<AttendanceAdmissionConditions2> admssnConds;
    @XmlElement(name = "ConfInf")
    protected String confInf;
    @XmlElement(name = "ConfDdln")
    protected DateFormat58Choice confDdln;
    @XmlElement(name = "ConfMktDdln")
    protected DateFormat58Choice confMktDdln;

    /**
     * Gets the value of the admssnConds property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a <CODE>set</CODE> method for the admssnConds property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAdmssnConds().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AttendanceAdmissionConditions2 }
     * 
     * 
     */
    public List<AttendanceAdmissionConditions2> getAdmssnConds() {
        if (admssnConds == null) {
            admssnConds = new ArrayList<AttendanceAdmissionConditions2>();
        }
        return this.admssnConds;
    }

    /**
     * Ruft den Wert der confInf-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getConfInf() {
        return confInf;
    }

    /**
     * Legt den Wert der confInf-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setConfInf(String value) {
        this.confInf = value;
    }

    /**
     * Ruft den Wert der confDdln-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link DateFormat58Choice }
     *     
     */
    public DateFormat58Choice getConfDdln() {
        return confDdln;
    }

    /**
     * Legt den Wert der confDdln-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link DateFormat58Choice }
     *     
     */
    public void setConfDdln(DateFormat58Choice value) {
        this.confDdln = value;
    }

    /**
     * Ruft den Wert der confMktDdln-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link DateFormat58Choice }
     *     
     */
    public DateFormat58Choice getConfMktDdln() {
        return confMktDdln;
    }

    /**
     * Legt den Wert der confMktDdln-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link DateFormat58Choice }
     *     
     */
    public void setConfMktDdln(DateFormat58Choice value) {
        this.confMktDdln = value;
    }

}
