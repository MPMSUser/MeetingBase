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
package de.meetingapps.meetingportal.meetComAllg;

public class CaZerlegString {
    String zuZerlegString;
    int posalt = 0;
    int pos = 0;
    int posoffset = 0;

    String zspeicher = "";

    public CaZerlegString(String paramString) {
        zuZerlegString = paramString;
    }

    public String next() {

        if (posalt == zuZerlegString.length()) {
            zspeicher = "";
            return zspeicher;
        }

        if (zuZerlegString.substring(posalt, posalt + 1).matches("'")) {
            posoffset = 1;
            pos = zuZerlegString.indexOf("',", posalt) + 1;
        } else {
            posoffset = 0;
            pos = zuZerlegString.indexOf(",", posalt);
        }
        if (pos <= 0) {
            pos = zuZerlegString.length();
        }

        /*System.out.println(zuZerlegString.length()+" "+posalt+" "+pos);*/

        zspeicher = zuZerlegString.substring(posalt + posoffset, pos - posoffset);
        posalt = pos + 1;

        return zspeicher;

    }

}
