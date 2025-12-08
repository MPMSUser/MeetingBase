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
 * <p>Java-Klasse für MeetingType4Code.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * <pre>
 * &lt;simpleType name="MeetingType4Code"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="XMET"/&gt;
 *     &lt;enumeration value="GMET"/&gt;
 *     &lt;enumeration value="MIXD"/&gt;
 *     &lt;enumeration value="SPCL"/&gt;
 *     &lt;enumeration value="BMET"/&gt;
 *     &lt;enumeration value="CMET"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "MeetingType4Code")
@XmlEnum
public enum MeetingType4Code {

    XMET, GMET, MIXD, SPCL, BMET, CMET;

    public String value() {
        return name();
    }

    public static MeetingType4Code fromValue(String v) {
        return valueOf(v);
    }

    public static String[] getLabels() {
        return new String[] { "Extraordinary Meeting", "General Meeting", "Bond Holder Meeting" };
    }

    public static String toLabel(MeetingType4Code v) {
        return switch (v) {
        case XMET -> "Extraordinary Meeting";
        case GMET -> "General Meeting";
        case BMET -> "Bond Holder Meeting";
        default -> throw new IllegalArgumentException("Unexpected value: " + v);
        };
    }

    public static MeetingType4Code fromLabel(String v) {
        return switch (v) {
        case "Extraordinary Meeting" -> XMET;
        case "General Meeting" -> GMET;
        case "Bond Holder Meeting" -> BMET;
        default -> throw new IllegalArgumentException("Unexpected value: " + v);
        };
    }
}
