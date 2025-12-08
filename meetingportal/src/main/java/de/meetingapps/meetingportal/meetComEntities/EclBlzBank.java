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

public class EclBlzBank implements Serializable {
    private static final long serialVersionUID = -1387606762513404734L;

    public int id;

    /** LEN=20
     * PLZ */
    public String blz;

    /** Länge 200 */
    public String bankname;
    
    /** Länge 30 */
    public String bic;

    /**Als Hilfsfeld während des Updates.
     * Wird zu Beginn auf 0 gesetzt.
     * Bei jedem Insert/Update dann auf 1
     * Anschließend werden alle rausgelöscht, die noch auf 0 stehen.
     * Siehe entsprechende Funktionen in DbPlzOrt
     */
    public int version=0;

    public EclBlzBank() {

    }

    public EclBlzBank(int id, String blz, String bankname, String bic) {
        super();
        this.id = id;
        this.blz = blz;
        this.bankname = bankname;
        this.bic = bic;
    }

    /****************Standard getter und setter*****************************************/

    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBlz() {
        return blz;
    }

    public void setBlz(String blz) {
        this.blz = blz;
    }

    public String getBankname() {
        return bankname;
    }

    public void setBankname(String bankname) {
        this.bankname = bankname;
    }

    public String getBic() {
        return bic;
    }

    public void setBic(String bic) {
        this.bic = bic;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }


}
