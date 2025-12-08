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
package de.meetingapps.meetingportal.meetComBl;

import java.util.LinkedList;
import java.util.List;

import de.meetingapps.meetingportal.meetComAllg.CaString;
import de.meetingapps.meetingportal.meetComEntities.EclSuchlaufErgebnis;

/**Enthält die "aufgesplitteten Vertreternamen mit Veränderungsstatus" für die gesamt
 * suchlaufErgebnisAlleListe
 */
public class BlSuchlaufVertreterFuerSuchErgebnisListe {
    /**Enthält die "aufgesplitteten Vertreternamen mit Veränderungsstatus" für jeweils
     * ein EclSuchlaufErgebnis in suchlaufErgebnisAlleListe*/
    class VertreterListeFuerEinSuchErgebnis {
        /**Vertreternamen, ohne Zusätze*/
        List<String> namen = null;
        /**
         * +100=unverändert enthalten
         * -100=unverändert nicht mehr enthalten
         * +1=neu aufgenommen
         * -1=neu nicht mehr enthalten
         */
        List<Integer> veraenderung = null;
        /**1 => Vertretername ist in neuem Suchlauf wieder gefunden worden*/
        List<Integer> neueEnthalten = null;

        boolean istUnvollstaendig = false;

        VertreterListeFuerEinSuchErgebnis() {
            namen = new LinkedList<String>();
            veraenderung = new LinkedList<Integer>();
            neueEnthalten = new LinkedList<Integer>();
        }

        void setzeZurueck() {
            if (namen != null) {
                for (int i = 0; i < namen.size(); i++) {
                    if (veraenderung.get(i) == 1) {
                        veraenderung.set(i, 100);
                    }
                    if (veraenderung.get(i) == -1) {
                        veraenderung.set(i, (-100));
                    }
                }
            }
        }

        void aktiviereVertretername(String pVertretername) {
            if (namen != null) {
                for (int i = 0; i < namen.size(); i++) {
                    if (namen.get(i).compareTo(pVertretername) == 0) {
                        neueEnthalten.set(i, 1);
                    }
                }
            }
        }
    }

    List<VertreterListeFuerEinSuchErgebnis> vertreterListeJeSuchErgebnis = null;
    int maximaleLaengeVertreterString = 400;

    BlSuchlaufVertreterFuerSuchErgebnisListe() {
        vertreterListeJeSuchErgebnis = new LinkedList<VertreterListeFuerEinSuchErgebnis>();
    }

    private void addVertreterStringAlsListe(String vertreterString) {
        /**Verarbeitungshinweis: die Abfrage auf length ist erforderlich, da durch das
         * Abschneiden der Vertreterliste bei zu großer Länge möglicherweise unvollständige
         * Ausdrücke entstehen
         */
        if (vertreterString == null) {
            vertreterString = "";
        }
        VertreterListeFuerEinSuchErgebnis lVertreterListeJeSuchErgebnis = new VertreterListeFuerEinSuchErgebnis();
        //		System.out.println("addVertretertSringAlsListe: "+vertreterString);
        String[] vertreterArray = vertreterString.split(";");
        for (int i = 0; i < vertreterArray.length; i++) {
            int veraenderung = 100;
            String vertretername = vertreterArray[i];
            if (vertretername.equals("Unvollständig")) {
                lVertreterListeJeSuchErgebnis.istUnvollstaendig = true;
            } else {
                if (vertretername.length() >= 1) {
                    if (vertretername.startsWith("-")) {
                        veraenderung = -1;
                        vertretername = vertretername.substring(1);
                    }
                    if (vertretername.startsWith("+")) {
                        veraenderung = +1;
                        vertretername = vertretername.substring(1);
                    }
                    if (vertretername.length() >= 2) {
                        if (vertretername.startsWith("(-")) {
                            int ende = vertretername.length() - 1;
                            if (ende < 2) {
                                ende = 2;
                            }
                            veraenderung = -100;
                            vertretername = vertretername.substring(2, ende);
                        }
                    }
                    if (vertretername.equals("(")) {
                        vertretername = "";
                    }
                }
                if (!vertretername.isEmpty()) {
                    //					System.out.println("vertretername="+vertretername+" veraenderung="+veraenderung);
                    lVertreterListeJeSuchErgebnis.namen.add(vertretername);
                    lVertreterListeJeSuchErgebnis.veraenderung.add(veraenderung);
                    lVertreterListeJeSuchErgebnis.neueEnthalten.add(0);
                }
            }
        }
        vertreterListeJeSuchErgebnis.add(lVertreterListeJeSuchErgebnis);
    }

    void fuerKompletteListeSplitteVertreterAuf(List<EclSuchlaufErgebnis> pSuchlaufErgebnisAlleListe) {
        for (int i = 0; i < pSuchlaufErgebnisAlleListe.size(); i++) {
            //			System.out.println("fuerKompletteListeSplitteVertreterAuf i="+i);
            addVertreterStringAlsListe(pSuchlaufErgebnisAlleListe.get(i).gefundeneVollmachtName);
        }
    }

    void setzeVeraenderungenZurueck() {
        if (vertreterListeJeSuchErgebnis != null) {
            for (int i = 0; i < vertreterListeJeSuchErgebnis.size(); i++) {
                vertreterListeJeSuchErgebnis.get(i).setzeZurueck();
            }
        }
    }

    private String liefereVertreterStringAusListe(
            VertreterListeFuerEinSuchErgebnis pVertreterListeFuerEinSuchErgebnis) {
        String vertreterString = "";
        if (pVertreterListeFuerEinSuchErgebnis.istUnvollstaendig) {
            vertreterString += "Unvollständig";
        }
        for (int i = 0; i < pVertreterListeFuerEinSuchErgebnis.namen.size(); i++) {
            String vertreterName = pVertreterListeFuerEinSuchErgebnis.namen.get(i);
            //			System.out.println("liefereVertreterStringAusListe i="+i+" vertreterName="+vertreterName);
            int veraenderung = pVertreterListeFuerEinSuchErgebnis.veraenderung.get(i);
            int neueEnthalten = pVertreterListeFuerEinSuchErgebnis.neueEnthalten.get(i);
            switch (veraenderung) {
            case 100:
                if (neueEnthalten == 0) {
                    vertreterName = "-" + vertreterName;
                }
                break;
            case -100:
                if (neueEnthalten == 1) {
                    vertreterName = "+" + vertreterName;
                } else {
                    vertreterName = "(-" + vertreterName + ")";
                }
                break;
            case 1:
                vertreterName = "+" + vertreterName;
                break;
            case -1:
                vertreterName = "-" + vertreterName;
                break;
            }
            if (!vertreterString.isEmpty()) {
                vertreterString = vertreterString + ";";
            }
            vertreterString = vertreterString + vertreterName;
        }
        if (vertreterString.length() > maximaleLaengeVertreterString) {
            if (!vertreterString.startsWith("Unvollständig")) {
                vertreterString = "Unvollständig;" + vertreterString;
                vertreterString = CaString.trunc(vertreterString, maximaleLaengeVertreterString);
            }
        }
        return vertreterString;
    }

    void fuerKompletteListeSetzeVertreterZusammen(List<EclSuchlaufErgebnis> pSuchlaufErgebnisAlleListe) {
        for (int i = 0; i < pSuchlaufErgebnisAlleListe.size(); i++) {
            String neuerVertreter = liefereVertreterStringAusListe(vertreterListeJeSuchErgebnis.get(i));
            if (neuerVertreter == null) {
                neuerVertreter = "";
            }
            String alterVertreter = pSuchlaufErgebnisAlleListe.get(i).gefundeneVollmachtName;
            if (alterVertreter == null) {
                alterVertreter = "";
            }
            if (neuerVertreter.compareTo(alterVertreter) != 0) {
                pSuchlaufErgebnisAlleListe.get(i).seitLetztemSpeichernVeraendert = true;
            }
            pSuchlaufErgebnisAlleListe.get(i).gefundeneVollmachtName = neuerVertreter;
        }
    }

    void trageNeuenVertreternameInBestehendemErgebnisEin(int pPositionInListe, String pVertretername) {
        vertreterListeJeSuchErgebnis.get(pPositionInListe).aktiviereVertretername(pVertretername);
    }

    void trageNeuenVertreternameMitNeuemErgebnisEin(String pVertretername) {
        addVertreterStringAlsListe("+" + pVertretername);
    }

}
