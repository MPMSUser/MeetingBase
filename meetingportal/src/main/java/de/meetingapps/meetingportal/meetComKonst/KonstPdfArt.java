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
package de.meetingapps.meetingportal.meetComKonst;

/**Art des an PDFs, das über diesen Button-Druck angefordert wurde*/
public class KonstPdfArt {

    public final static int unbekannt = 0;
    public final static int teilnehmerverzeichnis = 1;
    public final static int teilnehmerverzeichnisZusammenstellung = 2;
    public final static int abstimmungsergebnis = 3;
    public final static int sonstigesDokumentAufHV = 4;

    public static String getText(int pPdfArt) {
        switch (pPdfArt) {
        case 0:
            return "Sonstiges";
        case 1:
            return "Teilnehmerverzeichnis";
        case 2:
            return "Teilnehmerverzeichnis Zusammenstellung";
        case 3:
            return "Abstimmungsergebnis";
        case 4:
            return "Sonstiges Dokument auf HV";
        }
        return "";
    }

}
