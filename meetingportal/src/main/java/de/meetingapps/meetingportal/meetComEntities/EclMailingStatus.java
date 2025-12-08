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

public class EclMailingStatus {

    /**Eindeutige, interne Nummer, Autoincrement*/
    public int mailingStatusIdent = 0;
    public int mandant = 0;
    public long db_version = 0;
    
    public int aktienregisterIdent = 0;
    /**Habe alle String mit varchar(200) gemacht - paßt das?*/
    public String email;
    public String job_id = "";
    public String event_phase = "";
    public String event_state = "";
    public String event_type = "";
    public String event_subType = "";
    public String event_description = "";
    public String timestamp = "";
    
    
    /*****************Standard getter und setter******************************************/
    public int getMailingStatusIdent() {
        return mailingStatusIdent;
    }
    public void setMailingStatusIdent(int mailingStatusIdent) {
        this.mailingStatusIdent = mailingStatusIdent;
    }
    public int getMandant() {
        return mandant;
    }
    public void setMandant(int mandant) {
        this.mandant = mandant;
    }
    public long getDb_version() {
        return db_version;
    }
    public void setDb_version(long db_version) {
        this.db_version = db_version;
    }
    public String getJob_id() {
        return job_id;
    }
    public void setJob_id(String job_id) {
        this.job_id = job_id;
    }
    public String getEvent_phase() {
        return event_phase;
    }
    public void setEvent_phase(String event_phase) {
        this.event_phase = event_phase;
    }
    public String getEvent_state() {
        return event_state;
    }
    public void setEvent_state(String event_state) {
        this.event_state = event_state;
    }
    public String getEvent_type() {
        return event_type;
    }
    public void setEvent_type(String event_type) {
        this.event_type = event_type;
    }
    public String getEvent_subType() {
        return event_subType;
    }
    public void setEvent_subType(String event_subType) {
        this.event_subType = event_subType;
    }
    public String getEvent_description() {
        return event_description;
    }
    public void setEvent_description(String event_description) {
        this.event_description = event_description;
    }
    public String getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
    public int getAktienregisterIdent() {
        return aktienregisterIdent;
    }
    public void setAktienregisterIdent(int aktienregisterIdent) {
        this.aktienregisterIdent = aktienregisterIdent;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    
    
    
    
}
