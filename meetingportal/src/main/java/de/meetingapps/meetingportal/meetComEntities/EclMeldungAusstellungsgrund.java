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

public class EclMeldungAusstellungsgrund implements Serializable {
    private static final long serialVersionUID = 4827845334885807203L;

    public int mandant = 0;
    public int meldungsIdent = 0;
    public String ausstellungsGrundKuerzel = "";
    public String kommentar = "";

    /*Nicht Bestandteil dieser Table, aber im Hinblick auf meldung enthalten*/
    public String beschreibung; /*Aus EclAusstellungsgrund*/
}
