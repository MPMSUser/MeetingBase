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

public class EclKoordinaten implements Serializable {
    private static final long serialVersionUID = 4312866809111581819L;

    public String plz = "";

    public String ort = "";

    public String bundesland = "";

    public double laenge = 0.00;

    public double breite = 0.00;

    public EclKoordinaten() {

    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        long temp;
        temp = Double.doubleToLongBits(breite);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(laenge);
        result = prime * result + (int) (temp ^ (temp >>> 32));
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
        EclKoordinaten other = (EclKoordinaten) obj;
        if (Double.doubleToLongBits(breite) != Double.doubleToLongBits(other.breite))
            return false;
        if (Double.doubleToLongBits(laenge) != Double.doubleToLongBits(other.laenge))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "EclKoordinaten [plz=" + plz + ", ort=" + ort + ", breite=" + breite + ", " + laenge + "]";
    }

}
