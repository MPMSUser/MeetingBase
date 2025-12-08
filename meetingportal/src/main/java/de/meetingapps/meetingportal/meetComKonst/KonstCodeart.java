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

/**Art des an BlNummernform übergebenen Codes*/
public class KonstCodeart {

    public final static int unbekannt = 0;
    public final static int normalerNummerncode = 1;
    public final static int appIdent = 2;
    public final static int codeInLink = 3;
    /**Es wurde nur eine 4- oder 5-stellige Nummer, ggf. mit Nebencode (durch Blank-Getrennt) abgegeben*/
    public final static int nurNummer = 4;

    static public String getText(int nr) {
        switch (nr) {
        case 0: {
            return "";
        }
        case 1: {
            return "Normaler Nummerncode";
        }
        case 2: {
            return "App Ident";
        }
        case 3: {
            return "Code ist in Link";
        }
        case 4: {
            return "Nur Nummer, ggf. mit Index";
        }
        }

        return "";

    }
}
