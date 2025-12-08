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

/**Spezifiziert, welcher Art der Verarbeitungslauf ist*/
public class KonstVerarbeitungslaufArt {

    public final static int unbekannt = 0;

    public final static int eintrittskartendruck = 1;
    public final static int kontrollisteWeisungen = 2;

    /**Sub-Art: Ident der Insti*/
    public final static int sammelAnmeldeBogen = 3;

    public final static int neuesPasswort = 4;

    public final static int fragenListe = 5;

    public final static int mitteilungenListe = 6;

    static public String getText(int nr) {
        switch (nr) {
        case 0: {
            return "";
        }
        case 1: {
            return "Druck Eintrittskarten";
        }
        case 2: {
            return "Druck Kontrolliste Weisungen";
        }
        case 3: {
            return "Druck Sammel-Anmeldebogen";
        }
        case 4: {
            return "Druck Anschreiben neues Passwort";
        }
        case 5: {
            return "Fragenliste";
        }
        case 6: {
            return "Mitteilungenliste";
        }
        }

        return "";

    }

}
