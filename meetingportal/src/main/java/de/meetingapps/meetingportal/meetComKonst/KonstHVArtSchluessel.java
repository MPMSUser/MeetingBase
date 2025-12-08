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

import java.util.ArrayList;

public class KonstHVArtSchluessel {

    /**Diese Konstante kann verwendet werden, um das Drop-Down-Auswahlmenü
     * in einer Schleife zu füllen, z.B.:
     * for (int i=0;i<KonstHVArtSchluessel.anzKonstHVArtSchluessel;i++){
     *   String h=KonstHVArtSchluessel.liefereBezeichnungFuerAuswahl(i);
     *   auswahl.add(h)
     * }
     */
    public final static int anzKonstHVArtSchluessel = 8;

    public final static int sonstige = 0;
    public final static int ordinaryGM = 1;
    public final static int extraordinaryGM = 2;
    public final static int creditorsM = 3;
    public final static int bondholderM = 4;
    public final static int generalM = 5;
    public final static int shareholderM = 6;
    public final static int fair = 7;

    /**Liefert den Text für die Drop-Down-Auswahl im Programm*/
    public static String liefereBezeichnungFuerAuswahl(int p) {
        return switch (p) {
        case sonstige -> "sonstige";
        case ordinaryGM -> "Ordentliche Hauptversammlung";
        case extraordinaryGM -> "Außerordentliche Hauptversammlung";
        case creditorsM -> "Gläubigerversammlung n. Insolvenzordnung";
        case bondholderM -> "Anleihegläuberversammlung";
        case generalM -> "Mitgliederversammlung";
        case shareholderM -> "Gesellschafterversammlung";
        case fair -> "Messe";
        default -> throw new IllegalArgumentException("Unexpected value: " + p);
        };
    }

    public static int liefereIntFuerDatenbank(String s) {
        return switch (s) {
        case "sonstige" -> 0;
        case "Ordentliche Hauptversammlung" -> 1;
        case "Außerordentliche Hauptversammlung" -> 2;
        case "Gläubigerversammlung n. Insolvenzordnung" -> 3;
        case "Anleihegläuberversammlung" -> 4;
        case "Mitgliederversammlung" -> 5;
        case "Gesellschafterversammlung" -> 6;
        case "Messe" -> 7;
        default -> throw new IllegalArgumentException("Unexpected value: " + s);
        };
    }

    /**Liefert den Text für die externe Darstellung - Deutsch*/
    public static String liefereBezeichnungExternDE(int p) {
        return switch (p) {
        case sonstige -> "sonstige";
        case ordinaryGM -> "ordentliche Hauptversammlung";
        case extraordinaryGM -> "außerordentliche Hauptversammlung";
        case creditorsM -> "Gläubigerversammlung n. Insolvenzordnung";
        case bondholderM -> "Anleihegläuberversammlung";
        case generalM -> "Mitgliederversammlung";
        case shareholderM -> "Gesellschafterversammlung";
        case fair -> "Messe";
        default -> throw new IllegalArgumentException("Unexpected value: " + p);
        };
    }

    /**Liefert den Text für die externe Darstellung - Englisch*/
    public static String liefereBezeichnungExternEN(int p) {
        return switch (p) {
        case sonstige -> "";
        case ordinaryGM -> "ordinary General Meeting";
        case extraordinaryGM -> "extraordinary General Meeting";
        case creditorsM -> "Creditors' Meeting";
        case bondholderM -> "Bondholder Meeting";
        case generalM -> "General Meeting";
        case shareholderM -> "Shareholder Meeting";
        case fair -> "Fair";
        default -> throw new IllegalArgumentException("Unexpected value: " + p);
        };
    }

    public static ArrayList<String> liefereListeFuerAnzeige() {
        ArrayList<String> list = new ArrayList<>();

        for (int i = 0; i < anzKonstHVArtSchluessel; i++)
            list.add(liefereBezeichnungFuerAuswahl(i));

        return list;
    }

}
