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
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class EclNavToBM implements Serializable {

    private static final long serialVersionUID = 5814802917663282874L;

    public int ident = -1;

    public String description = "Neu";

    public String db_nav;

    public String db_bm;

    //	public Timestamp updated = Timestamp.valueOf(LocalDateTime.now());
    public Timestamp updated = null;

    public Boolean imp = true;

    public EclNavToBM() {

    }

    public EclNavToBM(int ident, String description, String db_nav, String db_bm, Timestamp updated, Boolean imp) {
        super();
        this.ident = ident;
        this.description = description;
        this.db_nav = db_nav;
        this.db_bm = db_bm;
        this.updated = updated;
        this.imp = imp;
    }

    public int getIdent() {
        return ident;
    }

    public void setIdent(int ident) {
        this.ident = ident;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDb_nav() {
        return db_nav;
    }

    public void setDb_nav(String db_nav) {
        this.db_nav = db_nav;
    }

    public String getDb_bm() {
        return db_bm;
    }

    public void setDb_bm(String db_bm) {
        this.db_bm = db_bm;
    }

    public Timestamp getUpdated() {
        return updated;
    }

    public void setUpdated(Timestamp updated) {
        this.updated = updated;
    }

    public Boolean getImp() {
        return imp;
    }

    public void setImp(Boolean imp) {
        this.imp = imp;
    }

    public static String getLocalDateTime(Timestamp stamp) {

        if (stamp == null)
            return null;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

        final ZonedDateTime timeInUTC = stamp.toLocalDateTime().atZone(ZoneId.of("UTC"));
        final LocalDateTime timeInGermany = LocalDateTime.ofInstant(timeInUTC.toInstant(), ZoneId.of("CET"));

        return timeInGermany.format(formatter);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((db_bm == null) ? 0 : db_bm.hashCode());
        result = prime * result + ((db_nav == null) ? 0 : db_nav.hashCode());
        result = prime * result + ((description == null) ? 0 : description.hashCode());
        result = prime * result + ident;
        result = prime * result + ((imp == null) ? 0 : imp.hashCode());
        result = prime * result + ((updated == null) ? 0 : updated.hashCode());
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
        EclNavToBM other = (EclNavToBM) obj;
        if (db_bm == null) {
            if (other.db_bm != null)
                return false;
        } else if (!db_bm.equals(other.db_bm))
            return false;
        if (db_nav == null) {
            if (other.db_nav != null)
                return false;
        } else if (!db_nav.equals(other.db_nav))
            return false;
        if (description == null) {
            if (other.description != null)
                return false;
        } else if (!description.equals(other.description))
            return false;
        if (ident != other.ident)
            return false;
        if (imp == null) {
            if (other.imp != null)
                return false;
        } else if (!imp.equals(other.imp))
            return false;
        if (updated == null) {
            if (other.updated != null)
                return false;
        } else if (!updated.equals(other.updated))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "EclNavToBM [ident=" + ident + ", description=" + description + ", db_nav=" + db_nav + ", db_bm=" + db_bm
                + ", updated=" + updated + ", imp=" + imp + "]";
    }

}
