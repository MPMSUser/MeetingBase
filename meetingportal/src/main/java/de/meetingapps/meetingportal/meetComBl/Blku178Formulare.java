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
package de.meetingapps.meetingportal.meetComBl;

import de.meetingapps.meetingportal.meetComDb.DbBundle;

public class Blku178Formulare {

    private DbBundle lDbBundle = null;

    public Blku178Formulare(DbBundle pDbBundle) {
        lDbBundle = pDbBundle;
    }

    public String lieferePfadFuerElternvollmachtsformular() {
        String hPfad = lDbBundle.lieferePfadMeetingReports();
        hPfad = hPfad + "\\elternvollmacht.pdf";
        return hPfad;
    }

}
