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
 * <p>Java-Klasse für AdditionalRights3 complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="AdditionalRights3"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="AddtlRght" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}AdditionalRightCode1Choice"/&gt;
 *         &lt;element name="AddtlRghtInfURLAdr" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}Max2048Text" minOccurs="0"/&gt;
 *         &lt;element name="AddtlRghtDdln" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}DateFormat58Choice" minOccurs="0"/&gt;
 *         &lt;element name="AddtlRghtMktDdln" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}DateFormat58Choice" minOccurs="0"/&gt;
 *         &lt;element name="AddtlRghtThrshld" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}AdditionalRightThreshold1Choice" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AdditionalRights3", propOrder = {
    "addtlRght",
    "addtlRghtInfURLAdr",
    "addtlRghtDdln",
    "addtlRghtMktDdln",
    "addtlRghtThrshld"
})
public class AdditionalRights3 {

    @XmlElement(name = "AddtlRght", required = true)
    protected AdditionalRightCode1Choice addtlRght;
    @XmlElement(name = "AddtlRghtInfURLAdr")
    protected String addtlRghtInfURLAdr;
    @XmlElement(name = "AddtlRghtDdln")
    protected DateFormat58Choice addtlRghtDdln;
    @XmlElement(name = "AddtlRghtMktDdln")
    protected DateFormat58Choice addtlRghtMktDdln;
    @XmlElement(name = "AddtlRghtThrshld")
    protected AdditionalRightThreshold1Choice addtlRghtThrshld;

    /**
     * Ruft den Wert der addtlRght-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link AdditionalRightCode1Choice }
     *     
     */
    public AdditionalRightCode1Choice getAddtlRght() {
        return addtlRght;
    }

    /**
     * Legt den Wert der addtlRght-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link AdditionalRightCode1Choice }
     *     
     */
    public void setAddtlRght(AdditionalRightCode1Choice value) {
        this.addtlRght = value;
    }

    /**
     * Ruft den Wert der addtlRghtInfURLAdr-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAddtlRghtInfURLAdr() {
        return addtlRghtInfURLAdr;
    }

    /**
     * Legt den Wert der addtlRghtInfURLAdr-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAddtlRghtInfURLAdr(String value) {
        this.addtlRghtInfURLAdr = value;
    }

    /**
     * Ruft den Wert der addtlRghtDdln-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link DateFormat58Choice }
     *     
     */
    public DateFormat58Choice getAddtlRghtDdln() {
        return addtlRghtDdln;
    }

    /**
     * Legt den Wert der addtlRghtDdln-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link DateFormat58Choice }
     *     
     */
    public void setAddtlRghtDdln(DateFormat58Choice value) {
        this.addtlRghtDdln = value;
    }

    /**
     * Ruft den Wert der addtlRghtMktDdln-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link DateFormat58Choice }
     *     
     */
    public DateFormat58Choice getAddtlRghtMktDdln() {
        return addtlRghtMktDdln;
    }

    /**
     * Legt den Wert der addtlRghtMktDdln-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link DateFormat58Choice }
     *     
     */
    public void setAddtlRghtMktDdln(DateFormat58Choice value) {
        this.addtlRghtMktDdln = value;
    }

    /**
     * Ruft den Wert der addtlRghtThrshld-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link AdditionalRightThreshold1Choice }
     *     
     */
    public AdditionalRightThreshold1Choice getAddtlRghtThrshld() {
        return addtlRghtThrshld;
    }

    /**
     * Legt den Wert der addtlRghtThrshld-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link AdditionalRightThreshold1Choice }
     *     
     */
    public void setAddtlRghtThrshld(AdditionalRightThreshold1Choice value) {
        this.addtlRghtThrshld = value;
    }

}
