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

public enum EnHtmlStatusCodes {

    OK, FORBIDDEN, NOTFOUND, WRONGMANDANTSTRING;

    public int toCode() {

        switch (this) {
        case OK:
            return 200;
        case FORBIDDEN:
            return 400;
        case NOTFOUND:
            return 401;
        case WRONGMANDANTSTRING:
            return 402;
        }
        return 501;

    }

    public String toString() {

        switch (this) {
        case OK:
            return "OK";
        case FORBIDDEN:
            return "Forbidden";
        case NOTFOUND:
            return "notfound - Mandant nicht gefunden";
        case WRONGMANDANTSTRING:
            return "wrong mandant string - Illegal Value in mandant";
        }
        return "Not implemented";

    }
}
