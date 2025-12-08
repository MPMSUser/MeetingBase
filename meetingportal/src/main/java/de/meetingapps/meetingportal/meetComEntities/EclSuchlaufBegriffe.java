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

public class EclSuchlaufBegriffe {

    /**Sowohl Mandantenspezifisch (z.B. Stimmausschluß) als auch
     * Mandantenübergreifend (z.B: für Instis) möglich
     */
    public int mandant = 0;

    public int ident = 0;

    /**LEN=200*/
    public String bezeichnung = "";

    /**Einzelne Begriffe werden mit "oder" verknüpft;
     * Trennung der Begriffe durch ;
     * LEN=800*/
    public String suchbegriffe = "";

}
