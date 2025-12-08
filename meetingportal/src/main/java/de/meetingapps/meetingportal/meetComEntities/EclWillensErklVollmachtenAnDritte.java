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

/**Hilfsklasse für BlWillenserklaerung.
 * Wird verwendet für ein Array, in dem alle Vollmachten an Dritte eingelesen werden,
 * die für einen Aktionär erteilt wurden.
 *
 */
public class EclWillensErklVollmachtenAnDritte {

    /**Willenserklärung, mit der diese Vollmacht erteilt wurde*/
    public EclWillenserklaerung willenserklaerungErteilt = null;

    /**Willenserklärung, mit der diese Vollmacht widerrufen wurde.
     * ==null, => nicht widerrufen sondern gültig
     */
    public EclWillenserklaerung willenserklaerungStorniert = null;

    /**Person, die Bevollmächtigt wurde*/
    public EclPersonenNatJur bevollmaechtigtePerson = null;

    /**Für Schnellzugriff: true => Vollmacht wurde storniert*/
    public boolean wurdeStorniert = false;

    /**"Merker" fürs Durcharbeiten von Vollmachtsketten*/
    public int merker = 0;

}
