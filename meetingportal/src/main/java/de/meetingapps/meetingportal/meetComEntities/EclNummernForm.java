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

/**DB-Abbildung für eine einzelne Nummernform. Achtung - teilweise Mandantenübergreifend geplant!
 * 
 * Grundsätzlicher Verarbeitungshinweis:
 * EclNummernForm wird immer global gepflegt (mandant=0). Wenn auf HVen lokal Nummernformen angelegt
 * werden, werden diese mandantenspezifisch angelegt.
 * Um Wildwuchs zu vermeiden, ist zukünftig ein Mechanismus geplant, um diese "zu konsolidieren", sprich
 * die mandantenspezifischen zu löschen und auf globale umzurouten.
 * 
 * Gedanken zur "Table-Trennung": auch die mandantenspezifischen Nummernformen werden in der 
 * geplanten globalen Table gespeichert ...
 * */
public class EclNummernForm {

    public int mandant = 0;

    /**Es gibt zwei Zählweisen:
     * > Mandantenübergreifend für die "global verfügbaren" (Mandant=0)
     * > je Mandant für ggf. lokal angefügte Nummernformen (noch nicht implementiert!)
     */
    public int ident = 0;
    /**1 => wurde gelöscht. In diesem Fall ist i.d.R. in ersetztDurch die ident der
     * Nummernform eingetragen, auf die die gelöschte Nummernform "umgeroutet" wurde
     * (muß immer vom mandant 0 sein)
     */
    public int geloescht = 0;
    /**Bei gelöscht-gekennzeichneten Nummernformen: hier wird die Ident eingetragen,
     * die verwendet wird statt der gelöschten
     */
    public int ersetztDurch = 0;
    /**LEN=20*/
    public String kodierung = "";
    /**LEN=120*/
    public String beschreibung = "";

}
