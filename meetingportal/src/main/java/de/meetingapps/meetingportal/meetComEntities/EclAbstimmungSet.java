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

/**Alle Infos zu Abstimmungen und Weisungen eines Mandanten, für Pufferung. Einschließlich Versionen
 * 
 * Enthält gleichzeitig die Dokumentation zwischen den einzelnen Abstimmungs-Tables*/
public class EclAbstimmungSet implements Serializable {
    private static final long serialVersionUID = 6987467886329195209L;

    /**Portal Weisungserfassung.
     * 
     * Wird über BlAbstimmungenWeisungen.leseAgendaWeisungen eingelesen (rcAgendaArray, rcGegenantraegeArray).
     * Kann dort auch wieder hin kopiert werden, um dann die dortigen weiteren Funktionen
     * verwenden zu können.
     * 
     * [0=alle, 1-5 für jeweilige Gattung][alle Abstimmungen]
     */
    public EclAbstimmung[][] agendaArrayPortalWeisungserfassung;
    public EclAbstimmung[][] gegenantraegeArrayPortalWeisungserfassung;

    public EclAbstimmung[][] agendaArrayInternWeisungserfassung;
    public EclAbstimmung[][] gegenantraegeArrayInternWeisungserfassung;

    public EclAbstimmung[][] agendaArrayPortalWeisungserfassungPreview;
    public EclAbstimmung[][] gegenantraegeArrayPortalWeisungserfassungPreview;


    /**Portal Abstimmung
     * 
     * Wird über BlAbstimmung.leseAktivenAbstimmungsblock eingelesen.
     * Kann dort auch wieder hin kopiert werden, um dann die dortigen weiteren Funktionen
     * verwenden zu können
     */

    public int aktivenAbstimmungsblockSortierenNach = 0;
    public EclAbstimmungsblock aktiverAbstimmungsblock = null;
    public boolean aktiverAbstimmungsblockIstElektronischAktiv = false;
    public EclAbstimmungZuAbstimmungsblock[] abstimmungenZuAktivenBlock = null;
    public EclAbstimmung[] abstimmungen = null;

}
