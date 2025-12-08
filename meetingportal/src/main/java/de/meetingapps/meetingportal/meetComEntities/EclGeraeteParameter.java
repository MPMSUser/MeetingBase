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

public class EclGeraeteParameter {

    /**Geräteklasse*/
    public int klasse = 0;

    /** eindeutiger Key (zusammen mit klasse), der unveränderlich ist. */
    public int ident = 0;

    /**Versionsnummer - manuell nicht verändern! Ausschließlich durch Verwendung in Db*/
    public long db_version;

    /**Wert des Parameters; Länge=40*/
    public String wert = "";

    /**Beschreibung des Parameters - nur für leichtere direkte Pflege in Datenbank; Länge=80*/
    public String beschreibung = "";

}
