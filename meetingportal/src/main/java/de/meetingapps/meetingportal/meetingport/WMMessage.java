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
package de.meetingapps.meetingportal.meetingport;

import jakarta.xml.bind.annotation.XmlRootElement;

/**Model für Kommunikation über Web-Service*/

/*XmlRootElement wird benötigt, wenn auch über XML kommuniziert werden soll. Kann also eigentlich raus :-) */

@XmlRootElement
public class WMMessage {

    private String message;

    /*Standard-Setter und getter*/
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    /*Aus Bequemlichkeit*/
    public WMMessage() {
    }

    public WMMessage(String message) {
        this.message = message;
    }

}
