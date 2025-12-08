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
package de.meetingapps.meetingportal.meetComWE;

public class WEBasemenRC {

    /**Return-Code.
     * 1=alles ok.
     * Sonstige: siehe KonstBasemenFehler
     */
    public int rc = 0;

    /**üblicherweise irrelevant. Nur bei
     * rc==KonstBasemenFehler.parameterUnzulaessig enthält
     * rcSub einen Detailcode mit Hinweis auf fehlerhaftem Parameter
     */
    public int rcSub = 0;

    /**Url, über den gewünschte Funktion bereit gestellt wird*/
    public String aufrufURL = "";

}
