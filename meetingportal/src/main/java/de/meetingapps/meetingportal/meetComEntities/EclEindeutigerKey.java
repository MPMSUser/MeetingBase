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
package de.meetingapps.meetingportal.meetComEntities;

public class EclEindeutigerKey {

    /**Spezialität:
     * ident=-1 => Anzahl der Keys insgesamt in DB
     * ident=0 => Anzahl der bereits vergebenen Key
     */

    public int ident = 0;

    /**LEN=20*/
    public String eindeutigerKey = "";

    /**Nur zur internen Administration für ident=-1 und ident=0*/
    public int lfd = 0;

}
