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

import de.meetingapps.meetingportal.meetComEclM.EclZugeordneteMeldungM;

@Deprecated
public class EclZugeordneteMeldungListe {

    /**Aktienregistereintrag, von dem aus die Zuordnung gestartet wurde.
     * = null => andere Ausgangsbasis
     */
    public EclAktienregister aktienregister = null;

    /**Ergebnis: alle Anmeldungen mit eigenen Aktien, die auf den Teilnehmer erfolgt sind.
     * Achtung: "eigene" heißt in diesem Fall auch Anmeldungen auf Fremdbesitz.
     * 
     * Hinweis: bei der Suche über den Aktienregistereintrag (also für die Standardportalsicht "Aktionär" sozusagen)
     * sind hierin auch die Gästekarten enthalten (=Anmeldung von diesem Aktionär ausgehend, die ja nicht auf ihn ausgehen)
     */
    public List<EclZugeordneteMeldung> zugeordneteMeldungenEigeneAktienListe;
    public int anzZugeordneteMeldungenEigeneAktienListe;
    public int gastKartenGemeldet = 0;

    /**Ergebnis: alle Anmeldungen als Gast, die auf den Teilnehmer erfolgt sind.
     * Können auch Gruppenkarten-Ausstellungen sein.
     * 
     * Hinweis: falls eine Gastkarte storniert wurde, ist hier ein Eintrag OHNE Zutrittsident
     * enthalten (da ja der Gast möglicherweise noch andere Aktivitäten durchgeführt hat, bzw. auch
     * Vollmachten erhalten hat).
     * Für Gästeanmeldungen kann KEINE weitere Willenserklärung abgegeben werden, insbesondere KEINE neue
     * ZutrittsIdent ausgestellt werden!
     */
    public List<EclZugeordneteMeldungM> zugeordneteMeldungenEigeneGastkartenListe;
    public int anzZugeordneteMeldungenEigeneGastkartenListe;

    /**Ergebnis: alle Meldungen, bei denen Vollmachten auf den Teilnehmer ausgestellt sind
     * (direkt oder indirekt).
     * Hierzu sind weitere Willenserklärungen möglicherweise vorhanden, bzw. können diese geändert
     * oder storniert werden (ähnlich wie bei eigenen Meldungen).
     */
    public List<EclZugeordneteMeldungM> zugeordneteMeldungenBevollmaechtigtListe;
    public int anzZugeordneteMeldungenBevollmaechtigtListe;

    public boolean briefwahlVorhanden = false;

}
