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

//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;

/**Klasse für ParamPortal: Phase für Anmeldefunktionen und
 * Funktionen der virtuellen HV. Wird im Array der Länge 20 gespeichert,
 * [KonstTermine 101 bis 120]
 */
public class EclPortalPhase implements Serializable {

    /** Our Logger. */
//    private static final Logger LOGGER = LogManager.getFormatterLogger(EclPortalPhase.class.getName());

    private static final long serialVersionUID = -150237876867963211L;

    /**Wird aus EclTermine gefüllt. 0=Beginn immer*/
    public long offenVon = 0;

    public boolean gewinnspielAktiv = false;

    /**Alle Funktionen im Zusammenhang mit einer HV sind enabled/disabled*/
    public boolean lfdHVPortalInBetrieb = false;

    /**1=vor der HV, 2=nach der HV (Parameter Nr. 515)*/
    public int lfdVorDerHVNachDerHV = 0;

    public boolean lfdHVPortalErstanmeldungIstMoeglich = false;
    public boolean lfdHVPortalEKIstMoeglich = false;
    public boolean lfdHVPortalSRVIstMoeglich = false;
    public boolean lfdHVPortalBriefwahlIstMoeglich = false;
    public boolean lfdHVPortalKIAVIstMoeglich = false;
    public boolean lfdHVPortalVollmachtDritteIstMoeglich = false;

    public boolean lfdHVStreamIstMoeglich = false;
    public boolean lfdHVFragenIstMoeglich = false;
    public boolean lfdHVRueckfragenIstMoeglich = false;
    public boolean lfdHVWortmeldungenIstMoeglich = false;
    public boolean lfdHVWiderspruecheIstMoeglich = false;
    public boolean lfdHVAntraegeIstMoeglich = false;
    public boolean lfdHVSonstigeMitteilungenIstMoeglich = false;
    public boolean lfdHVBotschaftenEinreichenIstMoeglich=false;
    public boolean lfdHVBotschaftenIstMoeglich=false;
    public boolean lfdHVChatIstMoeglich = false;
    public boolean lfdHVUnterlagenGruppe1IstMoeglich = false;
    public boolean lfdHVUnterlagenGruppe2IstMoeglich = false;
    public boolean lfdHVUnterlagenGruppe3IstMoeglich = false;
    public boolean lfdHVUnterlagenGruppe4IstMoeglich = false;
    public boolean lfdHVUnterlagenGruppe5IstMoeglich = false;
    public boolean lfdHVTeilnehmerverzIstMoeglich = false;
    public boolean lfdHVAbstimmungsergIstMoeglich = false;

    /**Phase ist manuell auf aktiv gesetzt. Darf nur für eine Phase eingestellt sein. Dann wird
     * Datum ignoeriert*/
    public boolean manuellAktiv = false;

    /**Phase ist komplett deaktiviert - unabhängig vom Zeitdauer. D.h. diese Phase wird
     * beim Suchen nach der aktiven Phase ignoriert*/
    public boolean manuellDeaktiv = false;

    public EclPortalPhase() {
        super();
//        if (LOGGER.isTraceEnabled()) {
//            LOGGER.trace("EclPortalPhase called:");
//        }
    }

    // protected void finalize() {
    //    if (LOGGER.isTraceEnabled()) {
    //        LOGGER.trace("EclPortalPhase destructed:");
    //    }
    //}
}
