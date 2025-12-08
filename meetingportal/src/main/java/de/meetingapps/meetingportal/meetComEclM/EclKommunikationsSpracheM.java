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
package de.meetingapps.meetingportal.meetComEclM;

import java.io.Serializable;

import de.meetingapps.meetingportal.meetComEntities.EclKommunikationsSprache;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

@SessionScoped
@Named
public class EclKommunikationsSpracheM implements Serializable {
    private static final long serialVersionUID = -5085633043740460761L;

    private String sprachennr = "";
    private String sprache = "";
    private int iststandard = 0; /* 1 = ja*/

    public void copyFrom(EclKommunikationsSprache pKommunikationsSprache) {
        this.sprachennr = Integer.toString(pKommunikationsSprache.sprachennr);
        this.sprache = pKommunikationsSprache.sprache;
        this.iststandard = pKommunikationsSprache.iststandard;

    }

    public String getSprachennr() {
        return sprachennr;
    }

    public void setSprachennr(String sprachennr) {
        this.sprachennr = sprachennr;
    }

    public String getSprache() {
        return sprache;
    }

    public void setSprache(String sprache) {
        this.sprache = sprache;
    }

    public int getIststandard() {
        return iststandard;
    }

    public void setIststandard(int iststandard) {
        this.iststandard = iststandard;
    }

}
