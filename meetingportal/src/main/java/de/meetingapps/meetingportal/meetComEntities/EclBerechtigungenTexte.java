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

public class EclBerechtigungenTexte {

    /**Gibt auch Reihenfolge der Anzeige im Pflegeprogramm an*/
    public int ident = 0;

    /**Text für Anzeige im Pflegeprogramm*/
    public String beschreibung = "";

    /**hauptoffset - [hauptoffset][nebenoffset]*/
    public int hauptOffset = 0;

    /**hauptoffset - [hauptoffset][nebenoffset]*/
    public int nebenOffset = 0;

    /**1 => diese Berechtigung gilt auch für User, die mandantenspezifisch angelegt werden (für mandantenübergreifende sowieso).
     * 0 => diese Berechtigung gilt nur für mandantenübergreifende (interne ...) User*/
    public int mandantenAbhaengig = 0;

}
