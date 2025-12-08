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

public class EclReload {

    /**Gibt an, für welchen Mandanten Reload erfolgen soll. 0 = Mandantenübergreifend*/
    public int mandant = 0;

    /**Gibt aktuellen Versionsstand des Puffers an:
     * 1 = Parameter
     * 2 = lfd. Parameter
     * 3 = Geräteabhängige Parameter (mandant=0)
     * 4 = Fehlermeldungen
     * 5 = Portal-/App-Texte
     * 6 = Server-Parameter (mandant=0)
     * 7 = Emittenten (mandant=0)
     * 8 = User-Login (mandant=0 - gilt immer für alle, auch für die madanten-abhängigen)
     * 
     * Achtung - 9 und 10 haben unmittelbare Auswirkung auf das Portal beim Weisungserfassen / Abstimmen. Wird während
     * eines "Stimmabgabevorangs" im Portal die Version erhöht, kann die Stimmabgabe nicht mehr gespeichert werden.
     * D.h. die Versionen dürfen nicht bei jedem Speichern einfach so erhöht werden, sondern müssen dediziert gesetzt werden.
     * 9 = Weisungen
     * 10= Abstimmungen
     * 
     * 11=AbstimmungenWeisungen-Reload (stoppt nicht laufende Weisung/Stimmabgabe, sondern aktualisiert - ist dann bei der nächsten
     * 		Eingabe aktuell)
     * 
     */
    public int ident = 0;

    /**Reload erforderlich =1*/
    public int reload = 0;

}
