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

public class EclAbstimmungenEinzelAusschluss {

    public int mandant = 0;

    /**Abstimmung, bei der der Ausschluß durchgeführt werden soll*/
    public int abstimmungsIdent = 0;

    public long db_version = 0;

    /**Meldung, die ausgeschlossen werden soll*/
    public int identMeldung = 0;

    public EclAbstimmungenEinzelAusschluss() {

    }

    public EclAbstimmungenEinzelAusschluss(int abstimmungsIdent, int identMeldung) {
        this.abstimmungsIdent = abstimmungsIdent;
        this.identMeldung = identMeldung;
    }

}
