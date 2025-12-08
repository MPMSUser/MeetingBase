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

import java.io.Serializable;

/**Verbindet EclAbstimmung und EclAbstimmungsblock. Enthält die entsprechenden
 * Infos zur tatsächlichen Stimmabgabe in der Abstimmung.
 */
public class EclAbstimmungZuAbstimmungsblock implements Serializable {
    private static final long serialVersionUID = 6527167245432630629L;

    public int mandant = 0;

    /**Eindeutige, interne Nummer*/
    public int ident = 0;

    public long db_version = 0;

    /**Reihenfolge zum Anzeige bei externen Geräten, z.B. Tablets
     * position=0 => wird auf Tablets etc. nicht angezeigt*/
    public int position = 0;

    /**Für Anzeige bei Tablets: 
     * Seitennummer falls Blättern aktiv
     */
    public int seite = 0;

    /**Abstimmung gehört zu diesem Abstimmungsblock*/
    public int identAbstimmungsblock;

    /**Ident der zugeordneten Abstimmung
     * 
     * Wichtiger Hinweis: zu einem Abstimmungsblock kann eine einzelne Abstimmung MEHRFACH zugeordnet werden.
     * Hintergrund: die selbe Abstimmung kann auf mehreren Stimmkarten vorkommen. 
     * Bsp: bei einer Abstimmung sind sowohl Vorzugsaktionäre als auch Stammaktionäre stimmberechtigt. Die selbe Abstimmung muß
     * also sowohl auf einer Stimmkarte für die Vorzugsaktionäre als auch für die Stammaktionäre aufgeführt werden. Das geht dann
     * über so eine zweifache Zuordnung.
     * 
     * Wie wird das dann bei einer Tablet-Abstimmung und beim Ausdruck mit parallelen Stimmkarten gemacht? 
     * Die zweite zugeordnete Abstimmung wird dann mit positionInAusdruck=0 und position=0 zugeordnet und dementsprechend dann
     * nicht berücksichtigt.
     * 
     * */
    public int identAbstimmung;

    /**Nr. der Stimmkarte, für die diese Abstimmung entgegengenommen wird*/
    public int nummerDerStimmkarte = 0;
    
    /**Position auf der Stimmkarte für diese Abstimmung*/
    public int positionAufStimmkarte = 0;

    /**==0 => wird nicht ausgedruckt*/
    public int positionInAusdruck = 0;

    /**Stimmart, die ausgewertet werden soll. -1 = Additionsverfahren*/
    @Deprecated
    public int fstimmenAuswerten = 0;

    /************* Folgende Felder nur zur effizienten Verarbeitung hier redundant. Aus anderer Tabelle!*********************/

    /*****Aus EclAbstimmung****/

    /**Nr,, mit der die Abstimmung auf die Array-Position im Weisungssatz verweist
     * -1 == Satz dienst nur als "Zwischenüberschrift" (z.B. im Portal)*/
    public int identWeisungssatz = 0;

    /**"Übergeordnete" Gesamtweisung, die ggf. übernommen wird - soweit keine Detailweisung vorliegt
     * =-1 => keine Übernahme*/
    public int vonIdentGesamtweisung = -1;

}
