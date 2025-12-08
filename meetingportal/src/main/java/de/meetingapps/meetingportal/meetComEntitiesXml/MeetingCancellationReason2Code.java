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
package de.meetingapps.meetingportal.meetComEntitiesXml;

public enum MeetingCancellationReason2Code {
    
    QORM, PROC, WITH;
    
    public String value() {
        return name();
    }

    public static MeetingCancellationReason2Code fromValue(String v) {
        return valueOf(v);
    }

    public static String[] getLabels() {
        return new String[] { "Quorum", "Processing", "Withdrawal" };
    }

    public static String toLabel(MeetingCancellationReason2Code v) {
        return switch (v) {
        case QORM -> "Quorum";
        case PROC -> "Processing";
        case WITH -> "Withdrawal";
        default -> throw new IllegalArgumentException("Unexpected value: " + v);
        };
    }

    public static MeetingCancellationReason2Code fromLabel(String v) {
        return switch (v) {
        case "Quorum" -> QORM;
        case "Processing" -> PROC;
        case "Withdrawal" -> WITH;
        default -> throw new IllegalArgumentException("Unexpected value: " + v);
        };
    } 
}