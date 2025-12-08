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

public class EclStaaten implements Serializable {
    private static final long serialVersionUID = 3070139725012226131L;

    public int id;

    /** Ländercode, i.D. 2-stellig; Länge 10 */
    public String code;

    /** Länge 100 */
    public String nameDE;

    /** Länge 100 */
    public String nameEN;

    public EclStaaten() {

    }

    public EclStaaten(int id, String code, String nameDE, String nameEN) {
        super();
        this.id = id;
        this.code = code;
        this.nameDE = nameDE;
        this.nameEN = nameEN;
    }

    /****************Standard getter und setter*****************************************/

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getNameDE() {
        return nameDE;
    }

    public void setNameDE(String nameDE) {
        this.nameDE = nameDE;
    }

    public String getNameEN() {
        return nameEN;
    }

    public void setNameEN(String nameEN) {
        this.nameEN = nameEN;
    }
    

}
