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

public class KonstKartenart {

    public final static int unbekannt = 0;
    public final static int erstzugang = 1;
    public final static int stimmkartenEtikett = 2;
    public final static int wiederzugang = 3;
    public final static int abgang = 4;
    public final static int vollmachtAnDritteErteilen = 5;
    public final static int vollmachtAnDritteEmpfangen = 6;
    public final static int stimmabschnittsnummer = 7;
    public final static int vollmachtWeisungSRV = 8;

    static public String getText(int nr) {
        switch (nr) {
        case 0: {
            return "";
        }
        case 1: {
            return "Erstzugang";
        }
        case 2: {
            return "Stimmkartenetikett";
        }
        case 3: {
            return "Wiederzugang";
        }
        case 4: {
            return "Abgang";
        }
        case 5: {
            return "VllmachtAnDritteErteilen";
        }
        case 6: {
            return "VollmachtAnDritteEmpfangen";
        }
        case 7: {
            return "Stimmabschnittsnummer";
        }
        case 8: {
            return "VollmachtWeisungSRV";
        }
        }

        return "";

    }
}
