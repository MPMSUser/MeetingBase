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

/**Klasse für jsfToSocket - Informationen, die zu einer Jsf-Session gespeichert werden*/
public class EclSocketJsfToSocketUndUser {

    /**Zugeordnete Socket-Session, z.B. zum Nachrichtensenden*/
    public String socketId = "";

    /**Int Anzahl der Sockets, die für diese Session gemacht wurden.
     * Wird dieser Wert >1, wird die JSF-Session automatisch gesperrt (istgesperrt)
     */
    public int anzahlSockets = 0;

    /**Wird gesperrt, wenn anzahlSockets>1 werden. Bleibt auch gesperrt, selbst wenn anzahlSockets wieder verringert werden*/
    public boolean gesperrt = false;

    /**Wenn nicht leer, dann ist in dieser Session diese Kennung, mit den folgenden Mandantendaten, eingeloggt*/
    public String userKennung = "";
    public int mandant = 0;
    public int hvJahr = 0;
    public String hvNummer = "";
    public String datenbereich = "";

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + anzahlSockets;
        result = prime * result + ((datenbereich == null) ? 0 : datenbereich.hashCode());
        result = prime * result + (gesperrt ? 1231 : 1237);
        result = prime * result + hvJahr;
        result = prime * result + ((hvNummer == null) ? 0 : hvNummer.hashCode());
        result = prime * result + mandant;
        result = prime * result + ((socketId == null) ? 0 : socketId.hashCode());
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
        EclSocketJsfToSocketUndUser other = (EclSocketJsfToSocketUndUser) obj;
        if (anzahlSockets != other.anzahlSockets)
            return false;
        if (datenbereich == null) {
            if (other.datenbereich != null)
                return false;
        } else if (!datenbereich.equals(other.datenbereich))
            return false;
        if (gesperrt != other.gesperrt)
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
        if (socketId == null) {
            if (other.socketId != null)
                return false;
        } else if (!socketId.equals(other.socketId))
            return false;
        if (userKennung == null) {
            if (other.userKennung != null)
                return false;
        } else if (!userKennung.equals(other.userKennung))
            return false;
        return true;
    }

}
