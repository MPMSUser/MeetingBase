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

import de.meetingapps.meetingportal.meetComEntities.EclAnrede;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

@SessionScoped
@Named
public class EclAnredeM implements Serializable {
    private static final long serialVersionUID = -5085633043740460761L;

    private String anredennr = "";
    private String anredentext = "";
    private String anredenbrief = "";
    private boolean istjuristischePerson = false; /* 1 = ja*/
    private String anredentextfremd = "";
    private String anredenbrieffremd = "";

    public void copyFrom(EclAnrede pAnrede) {
        this.setAnredennr(Integer.toString(pAnrede.anredennr));
        this.setAnredentext(pAnrede.anredentext);
        this.setAnredenbrief(pAnrede.anredenbrief);
        if (pAnrede.istjuristischePerson == 0) {
            this.setIstjuristischePerson(false);
        } else {
            this.setIstjuristischePerson(true);
        }
        this.setAnredentextfremd(pAnrede.anredentextfremd);
        this.setAnredenbrieffremd(pAnrede.anredenbrieffremd);

    }

    public String getAnredennr() {
        return anredennr;
    }

    public void setAnredennr(String anredennr) {
        this.anredennr = anredennr;
    }

    public String getAnredentext() {
        return anredentext;
    }

    public void setAnredentext(String anredentext) {
        this.anredentext = anredentext;
    }

    public String getAnredenbrief() {
        return anredenbrief;
    }

    public void setAnredenbrief(String anredenbrief) {
        this.anredenbrief = anredenbrief;
    }

    public boolean isIstjuristischePerson() {
        return istjuristischePerson;
    }

    public void setIstjuristischePerson(boolean istjuristischePerson) {
        this.istjuristischePerson = istjuristischePerson;
    }

    public String getAnredentextfremd() {
        return anredentextfremd;
    }

    public void setAnredentextfremd(String anredentextfremd) {
        this.anredentextfremd = anredentextfremd;
    }

    public String getAnredenbrieffremd() {
        return anredenbrieffremd;
    }

    public void setAnredenbrieffremd(String anredenbrieffremd) {
        this.anredenbrieffremd = anredenbrieffremd;
    }

}
