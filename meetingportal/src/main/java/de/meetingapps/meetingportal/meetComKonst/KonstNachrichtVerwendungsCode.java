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

/**Die folgenden Werte sind als Verwendungscode fest fürs System reserviert*/
public class KonstNachrichtVerwendungsCode {

    public final static int undefiniert = 0;

    /**Zwangsweise mit dieser Nummer, damit dies auch die Vorauswahl beim Mail-Senden ist*/
    public final static int emailText = 1;

    /**Mails mit Verwendungscodes >100.000 werden in den normalen "Empfangslisten" separat
     * behandelt, d.h. dürfen dort nicht als "gelesen / bearbeitet" etc. verarbeitet werden. 
     */
    public final static int insti_weisungsEmpfehlung = 100001;

    /**Nur beispielhaft - unklar ob benötigt!*/
    static public String getText(int nr) {
        switch (nr) {
        case undefiniert: {
            return "undefiniert";
        }
        case insti_weisungsEmpfehlung: {
            return "Abstimmungsempfehlung";
        }
        }
        return "";
    }

}
