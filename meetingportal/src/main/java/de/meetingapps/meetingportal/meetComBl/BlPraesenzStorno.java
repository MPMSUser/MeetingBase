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
package de.meetingapps.meetingportal.meetComBl;

import de.meetingapps.meetingportal.meetComDb.DbBundle;

/**Enthält Stornierungsfunktion für die Präsenz-Bewegungen
 * 
 * Allgemeine Gedanken, warum das so kompliziert ist:
 * 
 * Zu berücksichtigen ist:
 * > elektronisches Teilnehmerverzeichnis
 * > Wiederherstellen EK, SK, Vertreter in EclMeldung
 * > Korrektur Summen in Präsenzlisten
 * > Einzelkarten, die in Sammelkarten waren oder reingekommen sind
 * > Bei Stornierung von Sammelkarten: Einzelkarten können mittlerweile rausgekommen sein und anderweitig präsenz gesetzt worden sein.
 * > Bei Stornierung von Sammelkarten: mit / ohne Offenlegung 
 * 
 * Explizit nicht berücksichtigt werden:
 * > Stimmabgaben
 * > Delay
 * 
 * 
 * Use-Cases:
 * > Einzelkarte:
 * 
 * */
public class BlPraesenzStorno {

//    private DbBundle dbBundle = null;

    public void BlPraeasenzStorno(DbBundle pDbBundle) {
//        dbBundle = pDbBundle;
    }

}
