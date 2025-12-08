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

/**Nur mandantenübergreifend verfügbar*/
public class EclParameterSet {

    public int ident = 0;

    /**Len=400*/
    public String beschreibung = "";

    public int angelegtVonUserID = 0;

    /**LEN=19*/
    public String angelegtAm = "";

    public int letzteAenderungVonUserID = 0;

    /**LEN=19*/
    public String letzteAenderungAm = "";

    /*******Nicht in Datenbank-Table, nur zur leichteren Verarbeitung*********/
    public String angelegtVonUserLoginText = "";
    public String letzteAenderungVonUserLoginText = "";

}
