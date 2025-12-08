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
package de.meetingapps.meetingportal.meetComEh;

import java.io.Serializable;
import java.util.List;

public class EhGattung implements Serializable {
    private static final long serialVersionUID = -673105169589694054L;

    private int gattungId;

    private String bezeichnung;

    private List<String> isinList;

    public EhGattung() {

    }

    public EhGattung(int gattungId, String bezeichnung, List<String> isinList) {
        super();
        this.gattungId = gattungId;
        this.bezeichnung = bezeichnung;
        this.isinList = isinList;
    }

    public int getGattungId() {
        return gattungId;
    }

    public void setGattungId(int gattungId) {
        this.gattungId = gattungId;
    }

    public String getBezeichnung() {
        return bezeichnung;
    }

    public void setBezeichnung(String bezeichnung) {
        this.bezeichnung = bezeichnung;
    }

    public List<String> getIsinList() {
        return isinList;
    }

    public void setIsinList(List<String> isinList) {
        this.isinList = isinList;
    }

    @Override
    public String toString() {
        return "EhGattung [gattungId=" + gattungId + ", bezeichnung=" + bezeichnung + ", isinList=" + isinList + "]";
    }

}
