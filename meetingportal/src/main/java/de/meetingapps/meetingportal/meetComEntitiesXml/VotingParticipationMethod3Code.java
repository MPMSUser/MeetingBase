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
 * <p>Java-Klasse für VotingParticipationMethod3Code.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * <pre>
 * &lt;simpleType name="VotingParticipationMethod3Code"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="MAIL"/&gt;
 *     &lt;enumeration value="EVOT"/&gt;
 *     &lt;enumeration value="PHYS"/&gt;
 *     &lt;enumeration value="PHNV"/&gt;
 *     &lt;enumeration value="PRXY"/&gt;
 *     &lt;enumeration value="VIRT"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "VotingParticipationMethod3Code")
@XmlEnum
public enum VotingParticipationMethod3Code {

    MAIL, EVOT, PHYS, PHNV, PRXY, VIRT;

    public String value() {
        return name();
    }

    public static VotingParticipationMethod3Code fromValue(String v) {
        return valueOf(v);
    }

    public static String[] getLabels() {
        return new String[] { "Electronic Voting", "In Person", "Proxy" };
    }

    public static String toLabel(VotingParticipationMethod3Code v) {
        return switch (v) {
        case EVOT -> "Electronic Voting";
        case PHYS -> "In Person";
        case PRXY -> "Proxy";
        default -> throw new IllegalArgumentException("Unexpected value: " + v);
        };
    }

    public static VotingParticipationMethod3Code fromLabel(String v) {
        return switch (v) {
        case "Electronic Voting" -> EVOT;
        case "In Person" -> PHYS;
        case "Proxy" -> PRXY;
        default -> throw new IllegalArgumentException("Unexpected value: " + v);
        };
    }

}
