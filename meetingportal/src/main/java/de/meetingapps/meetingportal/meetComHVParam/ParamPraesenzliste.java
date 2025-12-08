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
package de.meetingapps.meetingportal.meetComHVParam;

import java.io.Serializable;

public class ParamPraesenzliste implements Serializable {
    private static final long serialVersionUID = 383238810261083380L;

    /**[0=Gesamtliste,1=Erstpräsenz,2=Nachtrag][0=Name,1=Ek,2=SK,3=Aktien Aufsteigend][0=alle/Gattung 1-5]*/
    public int[][][] einzeldruckInPDFaufnehmen = null;
    /**[0=Gesamtliste,1=Erstpräsenz,2=Nachtrag][0=Name,1=Ek,2=SK,3=Aktien Aufsteigend][0=alle/Gattung 1-5]*/
    public String[][][] einzeldruckFormatListe = null;
    /**[0=Gesamtliste,1=Erstpräsenz,2=Nachtrag][0=Name,1=Ek,2=SK,3=Aktien Aufsteigend][0=alle/Gattung 1-5]*/
    public String[][][] einzeldruckFormatZusammenstellung = null;

    public ParamPraesenzliste() {
        einzeldruckInPDFaufnehmen = new int[3][4][6];
        einzeldruckFormatListe = new String[3][4][6];
        einzeldruckFormatZusammenstellung = new String[3][4][6];

        for (int i = 0; i < 3; i++) {
            for (int i1 = 0; i1 < 4; i1++) {
                for (int i2 = 0; i2 < 6; i2++) {
                    einzeldruckInPDFaufnehmen[i][i1][i2] = 1;
                    einzeldruckFormatListe[i][i1][i2] = "01";
                    einzeldruckFormatZusammenstellung[i][i1][i2] = "01";
                }
            }
        }
    }

    /*Hinweise zum Speichern in Parametern:
     * Länge Wert=40.
     * => Wertekombi: 2+2+1=5;
     * => für jeweils 1 Gattung: 5*5 =25 paßt. 
     */
}
