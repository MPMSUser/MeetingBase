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
package de.meetingapps.meetingportal.meetComHVParam;

import java.io.Serializable;

public class ParamBestandsverwaltung implements Serializable {
    private static final long serialVersionUID = 2259230834107562353L;

    public int[][] weisungQS = null;

    public ParamBestandsverwaltung() {
        weisungQS = new int[21][4];
        for (int i = 0; i < 21; i++) {
            for (int i1 = 0; i1 < 4; i1++) {
                weisungQS[i][i1] = 0;
            }
        }

    }
}
