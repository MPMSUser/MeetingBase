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
package de.meetingapps.meetingportal.meetComWE;

import jakarta.xml.bind.annotation.XmlRootElement;

/**Entity für Web-Service - Login-Daten*/
@XmlRootElement
public class WELoginRC {
    private int rc;

    /**1 => es sollte baldmöglichst Version überprüft werden (Texte)*/
    private int pruefeVersion = 0;

    /*Ab hier Standard-Setters und Getters*/

    public int getRc() {
        return rc;
    }

    public void setRc(int rc) {
        this.rc = rc;
    }

    public int getPruefeVersion() {
        return pruefeVersion;
    }

    public void setPruefeVersion(int pruefeVersion) {
        this.pruefeVersion = pruefeVersion;
    }

}
