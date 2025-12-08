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

import java.util.List;

/**Nur noch verwendet für elektronischen Stimmkartenblock (Offline-Abstimmungsvariante App).
 * Gibt eine Stimmkarte innerhalb des elektronischen Stimmkartenblocks wieder.
 * 
 * Wird in EclStimnmkartenBlock zum Elektronischen Stimmkartenblock zusammengefaßt.*/
public class EclStimmkarteInhalt {

    public int mandant = 0;

    /**Nummer des Stimmschnipsel für Barcode-Kodierung. Gleichzeitig eindeutige Ident des Stimmschnipsels*/
    public int stimmkartenNr = 0;

    /**Reihenfolge-Position, speziell für elektronischen Stimmkartenblock*/
    public int posInBlock = 0;

    public long db_version = 0;

    /**Länge DB 40*/
    public String kurzBezeichnung = "";

    /**Länge DB 80*/
    public String stimmkartenBezeichnung = "";

    /**Länge DB 80*/
    public String stimmkartenBezeichnungEN = "";

    /**Verwendung: Stimmkarte wird bereits (vorsorglich) in einem (aktiven) Abstimmungsvorgang eingebaut,
     * soll aber noch nicht in Portalen /Apps etc. erscheinen
     */
    public int stimmkarteIstAktiv = 0;

    /**Nicht in DbStimmkarteInhalt, sondern in
     * DbAbstimmungenZuStimmkarte
     */
    public List<EclAbstimmung> abstimmungenListe = null;

}
