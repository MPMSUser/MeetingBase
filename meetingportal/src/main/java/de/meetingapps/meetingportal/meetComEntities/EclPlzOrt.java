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

public class EclPlzOrt implements Serializable {
    private static final long serialVersionUID = -1387606762513404734L;

    public int id;

    /** LEN=10
     * PLZ */
    public String plz;

    /** Länge 100 */
    public String ort;
    
    /**Als Hilfsfeld während des Updates.
     * Wird zu Beginn auf 0 gesetzt.
     * Bei jedem Insert/Update dann auf 1
     * Anschließend werden alle rausgelöscht, die noch auf 0 stehen.
     * Siehe entsprechende Funktionen in DbPlzOrt
     */
    public int version=0;

    public EclPlzOrt() {

    }

    public EclPlzOrt(int id, String plz, String ort) {
        super();
        this.id = id;
        this.plz = plz;
        this.ort = ort;
    }

    /****************Standard getter und setter*****************************************/

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlz() {
        return plz;
    }

    public void setPlz(String plz) {
        this.plz = plz;
    }

    public String getOrt() {
        return ort;
    }

    public void setOrt(String ort) {
        this.ort = ort;
    }


    

}
