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

public class EclNachrichtAnhang {

    /**Eindeutige Ident der hochgeladenen Datei*/
    public int ident = 0;

    /**Siehe ParamServer
     * Gehört zu ident und identMail*/
    public int dbServerIdent=0;

    /**Ident der Mail, zu der der Anhang gehört*/
    public int identMail = 0;

    public int mandant = 0;
    public int hvJahr = 0;
    public String hvNummer = "A";
    public String dbArt = "P";

    /**Dateiname (ohne Pfad!)
     * LEN=100*/
    public String dateiname = "";

    /**LEN=80*/
    public String beschreibung = "";


 }
