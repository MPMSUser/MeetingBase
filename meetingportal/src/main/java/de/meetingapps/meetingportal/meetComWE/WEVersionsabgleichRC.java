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

public class WEVersionsabgleichRC {

    public int rc=0;
    
    public String aktuelleServerVersion="";
    public boolean serverPasstZuAktuellerClientVersion=false;
    
    
    /***********Standard getter und setter**********************/

    public int getRc() {
        return rc;
    }
    public void setRc(int rc) {
        this.rc = rc;
    }
    public String getAktuelleServerVersion() {
        return aktuelleServerVersion;
    }
    public void setAktuelleServerVersion(String aktuelleServerVersion) {
        this.aktuelleServerVersion = aktuelleServerVersion;
    }
    public boolean isServerPasstZuAktuellerClientVersion() {
        return serverPasstZuAktuellerClientVersion;
    }
    public void setServerPasstZuAktuellerClientVersion(boolean serverPasstZuAktuellerClientVersion) {
        this.serverPasstZuAktuellerClientVersion = serverPasstZuAktuellerClientVersion;
    }
    
    
}
