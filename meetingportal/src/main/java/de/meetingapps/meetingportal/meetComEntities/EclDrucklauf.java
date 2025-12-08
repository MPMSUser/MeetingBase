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

public class EclDrucklauf {

    /**Nur noch in Datenbank-Schema, aber darf nicht mehr "relevant" verwendet werden*/
    @Deprecated
    public int mandant = 0;

    public int drucklaufNr = 0;

    public long db_version = 0;

    public int durchgefuehrtArbeitsplatzNr = 0;
    public int durchgefuehrtBenutzerNr = 0;

    /**Datum Uhrzeit erzeugt, 
     * Length=19
     * YYYY-MM-DD HH:MM:SS
     */
    public String erzeugtAm = "";

    /**Siehe KonstVerarbeitungslaufArt*/
    public int drucklaufArt = 0;

    /**Dient zur Untergliederung innerhalb einer KonstVerarbeitungslaufArt.
     * Beispiel: bei Sammelanmeldebogen wird in drucklaufSubArt die ident
     * der Insti gespeichert.
     */
    public int drucklaufSubArt = 0;

    /**Speziell für EK-Druck: 0=alle*/
    public int nurGepruefteVersandadressen = 1;

    /**Speziell für EK-Druck: 0=alle, 1=nur Inland, 2=nur Ausland*/
    public int landSelektion = 0;

    /**Speziell für EK-Druck: 0=alle, 1=nur Portal, 2=nur Anmeldestelle*/
    public int wegSelektion = 0;

    /**Speziell für EK-Druck: 1=Gäste, 2=Aktionäre*/
    public int gaesteOderAktionaereSelektion = 0;

    /**Anzahl der in diesem Lauf verarbeiteten Sätze*/
    public int anzahlSaetze = 0;

    /**Speziell für EK-Druck 
     * Len=20*/
    public String ersterAktionaer = "";
    /**Speziell für EK-Druck
     * Len=20*/
    public String letzterAktionaer = "";

    /**Speziell für EK-Druck: 0=wurde nur reserviert; 1=wurde auch gedruckt*/
    public int gedruckt = 0;

}
