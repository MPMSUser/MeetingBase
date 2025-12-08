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

/** Meldungen, die für das jeweilige VIP-KZ für das Monitoring ausgeblendet sind*/
public class EclMeldungenVipKZAusgeblendet {

    /** Mandanten-Nummer*/
    public int mandant = 0;
    /** Meldungssatz, der ausgeblendet ist*/
    public int meldungsIdent = 0;
    /**VIP-Kürzel, für das dieser Meldungssatz ausgeblendet ist*/
    public String vipKZKuerzel = "";
    /**Benutzernummer, für die der Satz ausgeblendet ist; 0 => für alle Benutzer ausgeblendet*/
    public int benutzernr;

}
