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

public class EclSuchlaufDefinition {

    public int mandant = 0;

    public int ident = 0;

    /**LEN=200*/
    public String bezeichnung = "";

    /**Verwendete Suchbegriffe*/
    public int identSuchlaufBegriffe = 0;

    /**1 => identSuchlaufBegriffe verweist auf mandantenübergreifende Begriffe*/
    public int identSuchlaufBegriffIstGlobal = 0;

    /**
     * Siehe KonstSuchlaufVerwendung.
     * Gibt auch wieder, ob in Register oder Meldebestand durchsucht wird. 
     */
    public int verwendung = 0;

    /**Bestimmt, ob (bei suche in Meldebestand), ob nach Aktionärsname oder/und
     * Vertretername gesucht wird.
     * Bei Suche in Register ohne Bedeutung.
     */
    public int sucheNachAktionaer = 0;
    public int sucheNachVertreter = 0;

    /**Parameter - Verwendung je nach Funktion. Z.B. bei Stimmausschluß: Kennzeichen, das gesetzt wird.
     * Jeweils LEN=100*/
    public String parameter1 = "";
    public String parameter2 = "";
    public String parameter3 = "";
    public String parameter4 = "";
    public String parameter5 = "";
}
