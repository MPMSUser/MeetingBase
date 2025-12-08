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
package de.meetingapps.meetingclient.meetingClientAllg;

import de.meetingapps.meetingportal.meetComEntities.EclLabelPrint;

/**
 * The Class CALabelPrint.
 */
public class CALabelPrint {

    /**
     * Drucken aktionaer.
     *
     * @param pEclLabelprint the ecl labelprint
     */
    public void druckenAktionaer(EclLabelPrint pEclLabelprint) {
        // add here your preferred PrinterDriver
    }

    /**
     * Drucken QR code.
     *
     * @param pNummer the nummer
     */
    public void druckenQRCode(String pNummer) {
        // add here your preferred PrinterDriver
    }

    /**
     * Drucken buendeln label.
     *
     * @param pArbeitsplatzNr the arbeitsplatz nr
     * @param pProtokollNr    the protokoll nr
     */
    public void druckenBuendelnLabel(int pArbeitsplatzNr, int pProtokollNr) {
        // add here your preferred PrinterDriver
    }
    
    private String replaceVariablen(String pAusgabe, EclLabelPrint pEclLabelprint) {
        String neuString=pAusgabe;
        
        neuString=replaceEinzelneVariable(neuString, "VornameAktionaer", pEclLabelprint.vornameAktionaer);
        neuString=replaceEinzelneVariable(neuString, "NachnameAktionaer", pEclLabelprint.nachnameAktionaer);
        neuString=replaceEinzelneVariable(neuString, "OrtAktionaer", pEclLabelprint.ortAktionaer);
        neuString=replaceEinzelneVariable(neuString, "VornameVertreter", pEclLabelprint.vornameVertreter);
        neuString=replaceEinzelneVariable(neuString, "NachnameVertreter", pEclLabelprint.nameVertreter);
        neuString=replaceEinzelneVariable(neuString, "OrtVertreter", pEclLabelprint.ortVertreter);
        neuString=replaceEinzelneVariable(neuString, "SKNummer", pEclLabelprint.skNummer);
        neuString=replaceEinzelneVariable(neuString, "EKNummer", pEclLabelprint.ekNummer);
        neuString=replaceEinzelneVariable(neuString, "Aktienzahl", pEclLabelprint.aktienzahl);
        neuString=replaceEinzelneVariable(neuString, "SKNummerBar", pEclLabelprint.skNummerBar);
        
        
        return neuString;
    }
    
    private String replaceEinzelneVariable(String pAusgabe, String pVariable, String pWert ) {
        String neuString=pAusgabe;
        neuString=neuString.replace("<<<"+pVariable+">>>", pWert);
        return neuString;
    }
}
