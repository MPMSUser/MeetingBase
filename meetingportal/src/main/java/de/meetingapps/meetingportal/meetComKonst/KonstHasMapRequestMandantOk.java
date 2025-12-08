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

import java.util.HashMap;

public class KonstHasMapRequestMandantOk {

    public static HashMap<String, Boolean> HashMapRequestMandantOk = new HashMap<String, Boolean>() {

        private static final long serialVersionUID = 1L;

        {
            for (int i = 1; i <= 999; i++) {
                String mandant = String.valueOf(i);
                while (mandant.length() < 3) {
                    mandant = "0" + mandant;
                }
                put(mandant, false);
            }
        }
    };

}
