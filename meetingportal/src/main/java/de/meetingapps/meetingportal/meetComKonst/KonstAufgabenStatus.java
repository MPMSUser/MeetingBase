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

public class KonstAufgabenStatus {

    public static final int gestellt = 1;
    public static final int widerrufen = 2;
    public static final int inArbeit = 3;
    public static final int erledigt = 4;
    public static final int nichtDurchfuehrbar = 5;
    public static final int geprueft = 6;

    public static String getText(int status) {
        switch (status) {
        case 1:
            return "Gestellt";
        case 2:
            return "Widerrufen";
        case 3:
            return "InArbeit";
        case 4:
            return "Erledigt";
        case 5:
            return "NichtDurchführbar";
        case 6:
            return "Geprüft";
        }
        return "";
    }
}
