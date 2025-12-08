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


/**Klasse für ParamPortal: Aktivierungsparameter und Zugriffsrechte für
 * Funktionen der virtuellen HV. Wird im Array der Länge 30 gespeichert,
 * [KonstTermine 121 bis 150]
 */
public class EclPortalFunktion implements Serializable {

    /** Our Logger. */
//    private static final Logger LOGGER = LogManager.getFormatterLogger(EclPortalFunktion.class.getName());

    private static final long serialVersionUID = 7919445428372610420L;

    /**Funktion wird grundsätzlich angeboten, d.h. Button wird in Auswahl angezeigt (wenn sonstige Rechte erfüllt sind)*/
    public boolean wirdAngeboten = false;

    /**Wird aus EclTermine gefüllt. 0=Beginn immer*/
    public long offenVon = 0;
    /**Wird aus EclTermine gefüllt. 0=Ende nie*/
    public long offenBis = 0;

    /*++++++++Berechtigt sind++++++*/
    public boolean berechtigtGast1 = false;
    public boolean berechtigtGast2 = false;
    public boolean berechtigtGast3 = false;
    public boolean berechtigtGast4 = false;
    public boolean berechtigtGast5 = false;
    public boolean berechtigtGast6 = false;
    public boolean berechtigtGast7 = false;
    public boolean berechtigtGast8 = false;
    public boolean berechtigtGast9 = false;
    public boolean berechtigtGast10 = false;

    public boolean berechtigtGastOnlineTeilnahmer1 = false;
    public boolean berechtigtGastOnlineTeilnahmer2 = false;
    public boolean berechtigtGastOnlineTeilnahmer3 = false;
    public boolean berechtigtGastOnlineTeilnahmer4 = false;
    public boolean berechtigtGastOnlineTeilnahmer5 = false;
    public boolean berechtigtGastOnlineTeilnahmer6 = false;
    public boolean berechtigtGastOnlineTeilnahmer7 = false;
    public boolean berechtigtGastOnlineTeilnahmer8 = false;
    public boolean berechtigtGastOnlineTeilnahmer9 = false;
    public boolean berechtigtGastOnlineTeilnahmer10 = false;

    public boolean berechtigtAktionaer = false;
    public boolean berechtigtAngemeldeterAktionaer = false;
    public boolean berechtigtOnlineTeilnahmeAktionaer = false;

    /*++++++++++Aktiv++++++++++++++*/
    /**
     * 1 aktiv gemäß Phasensteuerung
     * 2 aktiv gemäß Termin
     * 3 aktiv Vorrangig vor Phase/Termin
     * 4 inaktiv vorrangig vor Phase/Termin
     */
    public int aktiv = 4;

    public EclPortalFunktion() {
        super();
//        if (LOGGER.isTraceEnabled()) {
//            LOGGER.trace("EclPortalFunktion called:");
//        }
    }

    // protected void finalize() {
    //    if (LOGGER.isTraceEnabled()) {
    //        LOGGER.trace("EclPortalfunktion destructed:");
    //    }
    //}

    public boolean aktivVorrangig() {
        if (aktiv == 3) {
            return true;
        }
        return false;
    }

    public boolean inaktivVorrangig() {
        if (aktiv == 4) {
            return true;
        }
        return false;
    }

    public boolean aktivLtPhasensteuerung() {
        if (aktiv == 1) {
            return true;
        }
        return false;
    }

    public boolean aktivLtTermin() {
        if (aktiv == 2) {
            return true;
        }
        return false;
    }

}
