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

import de.meetingapps.meetingportal.meetComEntities.EclAbstimmung;
import de.meetingapps.meetingportal.meetComEntities.EclAbstimmungZuAbstimmungsblock;
import de.meetingapps.meetingportal.meetComEntities.EclAbstimmungsblock;

public class WEAbstimmungLeseAktivenAbstimmungsblockRC extends WERootRC {

    /*Werte werden gefüllt in BlAbstimmung.leseAktivenAbstimmungsblock*/

    /**Aktiver Abstimmungsblock.
     * Kann leer oder null sein, wenn keiner gefunden wurde.*/
    public EclAbstimmungsblock aktiverAbstimmungsblock = null;
    
    public boolean aktiverAbstimmungsblockIstElektronischAktiv=false;

    /**Enthält jeweils die Abstimmungen, die zu diesem Abstimmungsblock gehören*/
    public EclAbstimmungZuAbstimmungsblock[] abstimmungenZuAktivenBlock = null;
    /**Enthält jeweils die Abstimmungen, die zu diesem Abstimmungsblock gehören*/
    public EclAbstimmung[] abstimmungen = null;

    /**Wird übertragen und lokal im Client gespeichert, um dann bei jedem neuen Aktionär
     * zu überprüfen ob die Version der aktuellen Abstimmung noch stimmt
     */
    public int abstimmungsVersion=0;
    /*******************StandardGetter und Setter********************************/

}
