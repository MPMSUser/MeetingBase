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
package de.meetingapps.meetingportal.meetComKonst;

public class KonstMitteilungViewBereich {

    /**Je nachdem - Meldung dass inaktiv, oder Button für Mitteilung stellen.
     * In jedem Fall Anzeige der selbst getätigten Mitteilungen und ggf. der
     * "Rednerliste"
     */
    public static final int ausgangsView = 1;
    
    public static final int personBestaetigen = 3;
    public static final int mitteilungEingeben = 4;
    public static final int mitteilungNichtMoeglichDaPersonNichtBestaetigt = 5;
    public static final int keineWeiterenMitteilungenMoeglich = 6;
    
    /**Juristische Person oder Personengemeinschaft nicht zulässig*/
    public static final int personenArtNichtZulaessig=7;

}
