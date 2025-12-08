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
package de.meetingapps.meetingportal.meetComEntities;

import java.io.Serializable;

/**Klasse für BlmUserListen - Informationen, die zu einer Jsf-Session gespeichert werden*/
public class EclSocketJsfToUser implements Serializable {
    private static final long serialVersionUID = -3967888485449489854L;

    public String userKennung = "";
    public int mandant = 0;
    public int hvJahr = 0;
    public String hvNummer = "";
    public String datenbereich = "";

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((datenbereich == null) ? 0 : datenbereich.hashCode());
        result = prime * result + hvJahr;
        result = prime * result + ((hvNummer == null) ? 0 : hvNummer.hashCode());
        result = prime * result + mandant;
        result = prime * result + ((userKennung == null) ? 0 : userKennung.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        EclSocketJsfToUser other = (EclSocketJsfToUser) obj;
        if (datenbereich == null) {
            if (other.datenbereich != null)
                return false;
        } else if (!datenbereich.equals(other.datenbereich))
            return false;
        if (hvJahr != other.hvJahr)
            return false;
        if (hvNummer == null) {
            if (other.hvNummer != null)
                return false;
        } else if (!hvNummer.equals(other.hvNummer))
            return false;
        if (mandant != other.mandant)
            return false;
        if (userKennung == null) {
            if (other.userKennung != null)
                return false;
        } else if (!userKennung.equals(other.userKennung))
            return false;
        return true;
    }

    public String toString() {
        return userKennung + Integer.toString(mandant) + Integer.toString(hvJahr) + hvNummer + datenbereich;
    }
}
