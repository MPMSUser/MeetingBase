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

/**Zuordnung von Instis als "Sub-Insti" zu einem übergeordneten Insti.
 * 
 * Mandantenübergreifend:
 * > Instis, die üblicherweise auch andere Instis vertreten, werden diese als "Sub" zugeordnet.
 * 
 * Mandantenspezifisch:
 * > Einer Insti werden die Instis zugeordnet, die konkret für diese HV von dieser Vertreten werden.
 * Zuordnung basiert auf der Mandantenübergreifenden Zuordnung.
 *
 * Frage: darf eine Insti mehreren anderen Instis als Sub zugeordnet werden? Oder immer nur einer?
 * Antwort:
 * > Mandantenübergreifend: ja, mehrfach-Zuordnung ist möglich (ist ja nur "potentielle")
 * > Mandantenspezifisch: nein, muß eindeutig sein. Eigentlich. Aber wenn man z.B. an den Plan von
 * 		Governance&Values denkt: also, dann doch mehrfach-Zuordnung zulassen.
 */
public class EclInstiSubZuordnung {

    public int mandant = 0;

    /**Identifikation der "Ober-Insti"*/
    public int identInsti = 0;

    /**Ident der Sub-Insti, die der Ober-Insti zugeordnet ist*/
    public int identSubInsti = 0;

    /*************Nicht in der direkten Datenbank*******************/

    public String bezeichnungSubInsti = "";
}
