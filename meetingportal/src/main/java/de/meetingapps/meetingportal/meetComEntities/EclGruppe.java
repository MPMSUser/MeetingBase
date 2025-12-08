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

public class EclGruppe {

    public int gruppenIdent = 0;
    public int gruppenNr = 0;
    public long db_version = 0;
    public int gruppenklassenNr = 0;
    /**Länge 50*/
    public String gruppenText = "";
    public int vertreterZwingend = 0;
    public int fuerAktionaere = 0;
    public int fuerGaeste = 0;

    /*********Ab hier nicht in Datenbank!*******************/
    /**Formular-Nummer Gast-EK*/
    public int gastEKFormular = 0;

}
