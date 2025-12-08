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

/**Gültigkeit der jeweiligen - Mandantenübergreifenden - Ausstellungsgründe im jeweiligen Mandanten*/
public class EclAusstellungsgrundMandant {

    public String kuerzel;

    public int mandant = 0;
    public long db_version = 0;

    public int fuerAktionaere = -1;
    public int fuerGaeste = -1;

    /**1 = für Neuausstellung anbieten; 2=für Storno anbieten*/
    public int fuerAusstellungOderStorno = 1;

}
