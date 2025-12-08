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

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für DateType10Code.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * <pre>
 * &lt;simpleType name="DateType10Code"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="MEET"/&gt;
 *     &lt;enumeration value="UKWN"/&gt;
 *     &lt;enumeration value="NARR"/&gt;
 *     &lt;enumeration value="RDTE"/&gt;
 *     &lt;enumeration value="PPYD"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "DateType10Code")
@XmlEnum
public enum DateType10Code {

    MEET,
    UKWN,
    NARR,
    RDTE,
    PPYD;

    public String value() {
        return name();
    }

    public static DateType10Code fromValue(String v) {
        return valueOf(v);
    }
    
    public static String[] getLabels() {
        return new String[] { "BlockingTillMeetingDate", "BlockingTillRecordDate" };
    }

    public static String toLabel(DateType10Code v) {
        return switch (v) {
        case MEET -> "BlockingTillMeetingDate";
        case RDTE -> "BlockingTillRecordDate";
        default -> throw new IllegalArgumentException("Unexpected value: " + v);
        };
    }

    public static DateType10Code fromLabel(String v) {
        return switch (v) {
        case "BlockingTillMeetingDate" -> MEET;
        case "BlockingTillRecordDate" -> RDTE;
        default -> throw new IllegalArgumentException("Unexpected value: " + v);
        };
    }


}
