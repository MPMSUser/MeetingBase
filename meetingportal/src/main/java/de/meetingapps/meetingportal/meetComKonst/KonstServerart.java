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

public class KonstServerart {

    public final static int undefiniert = 0;
    public final static int produktionsserverExternerHoster = 1;
    public final static int einrichtungsserverOffice = 2;
    public final static int hvServer = 3;
    public final static int lokaleInstallation = 4;

    static public String getText(int nr) {
        switch (nr) {
        case 0: {
            return "undefiniert";
        }
        case 1: {
            return "ExternerHoster Produktionsserver Portal";
        }
        case 2: {
            return "Office Einrichtungsserver";
        }
        case 3: {
            return "Lokaler HV Server vor Ort";
        }
        case 4: {
            return "Lokale Installation";
        }
        }
        return "";
    }

}
