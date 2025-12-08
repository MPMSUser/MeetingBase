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

@Deprecated
public class EclFragen {

    /** Mandantennummer */
    public int mandant = 0;

    /**Eindeutig Ident dieser Frage*/
    public int frageIdent = 0;

    /**
     * Versionsnummer - manuell nicht verändern! Ausschließlich durch Verwendung
     * in DbMeldungen
     */
    public long db_version;

    /******Fragesteller***************/
    /**Aktionär, unter dessen Besitz diese Frage gestellt wurde*/
    @Deprecated
    public int aktienregisterIdent = 0;

    /**Vorsorglich: falls Frage von Insti gestellt wurde, dann ist hier drin die ident der EclPersonenNatJur*/
    @Deprecated
    public int instiIdent = 0;

    /**Vertreter
     * Im neuen System: Ident der PersonNatJur, die die 
     * Frage gestellt hat*/
    public int vertreterIdent = 0;

    /**Identifikationsstring, z.B. EK-Nr, AktienregisterNummer o.ä.
     * LEN=20
     * Wird beim Einlesen gefüllt mit
     * Aktionär
     * Vertreter
     * Insti
     * */
    @Deprecated
    public String stellerIdent = "";

    /**********Frageninhalt********************/
    /**TOP(e), zu denen die Frage gestellt wird
     * LEN=100*/
    public String zuTop = "";

    /**LEN in Datenbank=30.000;
     * Zulässige Länge in Browser = 10.000
     */
    public String fragentext = "";

    /**LEN=19*/
    public String zeitpunktDerFrage = "";

    public int drucklaufNr = 0;

    /*****************Nicht in Datenbank****************/
    @Deprecated
    public String aktionaerNummer = "";
    @Deprecated
    public String aktionaerName = "";
    @Deprecated
    public String aktionaerOrt = "";
    public String nameSteller = "";
    public String ortSteller = "";
    @Deprecated
    public long aktien = 0;
}
