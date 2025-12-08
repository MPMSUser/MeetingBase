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

/**Spezial für Gov&Val: abgegebene Weisungsempfehlung*/
public class EclAbstimmungsVorschlagEmpfehlung {

    public int mandant = 0;

    /**Interne Ident*/
    public int ident = 0;

    /**Verweis auf die Nachricht, mit der die Empfehlung "gepushed" wurde*/
    public int identMail = 0;

    /**LEN=19*/
    public String erstellungszeitpunkt = "";

    public int erstelltVonUserId = 0;

    /**LEN=200*/
    public String kurzBeschreibung = "";

    /**entsprechende Pos >0 => empfehlung für die entsprechende Abstimmung*/
    public int[] empfehlungFuerAbstimmungsIdent = null;

    public EclAbstimmungsVorschlagEmpfehlung() {
        empfehlungFuerAbstimmungsIdent = new int[200];
        for (int i = 0; i < 200; i++) {
            empfehlungFuerAbstimmungsIdent[i] = 0;
        }
    }
}
