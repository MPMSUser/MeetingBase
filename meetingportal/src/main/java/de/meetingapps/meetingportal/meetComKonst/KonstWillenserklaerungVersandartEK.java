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

public class KonstWillenserklaerungVersandartEK {

    /**keinerlei Weiterverarbeitung erforderlich (alles bereits vom Programm adhoc erledigt*/
    public final static int KEINE_WEITERVERARBEITUNG_ERFORDERLICH = 0;

    /**Aufnahme in Sammelbatch, an Adresse im Aktienregister - bei nächstem Drucklauf ausdrucken und versenden*/
    public final static int VERSANDLAUF_ADRESSE_LAUT_AKTIENREGISTER = 1;

    /**Aufnahme in Sammelbatch, an Versandadresse - bei nächstem Drucklauf ausdrucken und versenden*/
    public final static int VERSANDLAUF_ADRESSE_ABWEICHEND_WIE_EINGEGEBEN = 2;

    /**Online-Ausdruck (im Portal) erfolgt*/
    public final static int ONLINE_AUSDRUCK = 3;

    /**Versand per Email (im Portal) erfolgt*/
    public final static int ONLINE_EMAIL = 4;

    /**automatische Aufnahme in App*/
    public final static int IN_APP = 5;

    /**selbe Versandadresse wie zeitgleich ausgestellte Eintrittskarte (nur Gastkarte!)*/
    public final static int GASTKARTE_WIE_EINTRITTSKARTE = 6;

    /**Aufnahme in Sammelbatch, an Adresse im Aktienregister - bei nächstem Drucklauf ausdrucken und versenden (nur Gastkarte!)*/
    public final static int GASTKARTE_ADRESSE_LAUT_AKTIENREGISTER = 7;

    /**Aktienregister (nicht abweichend)*/
    public final static int VERSANDLAUF_ADRESSE_LAUT_AKTIENREGISTER_NICHT_AN_ABWEICHENDE_ADRESSE = 98;

    /**korrigierte*/
    public final static int VERSANDLAUF_ADRESSE_MANUELL_KORRIGIERT = 99;

    public static String getText(int weg) {

        switch (weg) {
        case 0: {
            return "Nichts versenden";
        }
        case 1: {
            return "Post lt. Aktienregister";
        }
        case 2: {
            return "Post lt. man.Versandadresse";
        }
        case 3: {
            return "Online Ticket";
        }
        case 4: {
            return "per E-Mail";
        }
        case 5: {
            return "App";
        }
        case 6: {
            return "Wie EK";
        }
        case 98: {
            return "Aktienregister (nicht abweichend)";
        }
        case 99: {
            return "korrigierte";
        }
        }

        return "";

    }

}
