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

/**Verarbeitungshinweis: ein einmal erteilter Abstimmungsvorschlag kann nicht geändert oder gelöscht, sondern nur
 * durch einen nächsten ersetzt werden*/

public class EclAbstimmungsVorschlag {

    /**Mandant**/
    public int mandant = 0;

    /**Laufende Nummer (Intern) des Abstimmungsvorschlags - primary Key*/
    public int abstimmungsVorschlagIdent;

    /**Kurz-Beschreibung des Abstimmungsvorschlags - maximal 100 Stellen**/
    public String beschreibungKurz = "";

    /**Lang-Beschreibung des Abstimmungsvorschlags - maximal 1000 Stellen**/
    public String beschreibungLang = "";

    /**Link auf nähere Informationen (auch außerhalb des Portals, also z.B. auf Seite der Bank o.ä.
     * maximal 200 Stellen**/
    public String linkExtern = "";

    /**Ident der Sammelkarte, von der aus der Abstimmungsvorschlag erstellt wurde / gepflegt wird*/
    public int sammelIdent = 0;

    /**Gültig ab*/
    public String gueltigAb = "";

    /**Gültig bis.
     * Wird automatisch gesetzt bei Anlegen eines neuen Abstimmungsvorschlags.
     * Wenn "gültig bis" gesetzt wird ohne einen neuen Abstimmungsvorschlag anzulegen, dann können neu-erteilte
     * Weisungen diesem Vorschlag nicht mehr folgen. Bereits bestehende (auch "anzupassende" "Folger-Weisungen" bleiben
     * unverändert bestehen*/
    public String gueltigBis = "";

    /**Verweis auf anderen Abstimmungsvorschlag, dessen Inhalte übernommen wird (d.h. der "Geber" dieses
     * Abstimmvorschlags empfiehlt, sich dem Abstimmvorschlag eines anderen anzuschließen)
     */
    public int verweisAufAbstimmungsvorschlag;

    public int[] abgabe = new int[200];

    public EclAbstimmungsVorschlag() {
        int i;
        for (i = 0; i < 200; i++) {
            abgabe[i] = 0;
        }
    }

}
