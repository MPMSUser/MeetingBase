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
package de.meetingapps.meetingportal.meetComReports;

import de.meetingapps.meetingportal.meetComBl.BlNummernformen;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComKonst.KonstKartenart;
import de.meetingapps.meetingportal.meetComKonst.KonstKartenklasse;
import de.meetingapps.meetingportal.meetingCoreReport.RpDrucken;
import de.meetingapps.meetingportal.meetingCoreReport.RpVariablen;

public class RepTestBarcodes {

    /**Barcodes für EK und SK
     */
    public void druckeAktionaer(DbBundle pDbBundle, RpDrucken rpDrucken, String pLfdNummer, int vonEk, int bisEK,
            int vonNrSK, String nebenNummer) {

        rpDrucken.initFormular(pDbBundle);

        RpVariablen rpVariablen = new RpVariablen(pDbBundle);
        rpVariablen.testBarcodes(pLfdNummer, rpDrucken);
        rpDrucken.startFormular();

        for (int i = vonEk; i <= bisEK; i++) {

            BlNummernformen blNummernformen = new BlNummernformen(pDbBundle);

            String nummer = Integer.toString(i);

            if (nebenNummer.isEmpty()) {
                rpVariablen.fuelleVariable(rpDrucken, "Nummern.ZutrittsIdent", blNummernformen.formatiereEKNr(nummer));

                rpVariablen.fuelleVariable(rpDrucken, "Nummern.ZutrittsIdentErstzugang",
                        blNummernformen.formatiereNrKomplett(nummer, "", KonstKartenklasse.eintrittskartennummer,
                                KonstKartenart.erstzugang));
                rpVariablen.fuelleVariable(rpDrucken, "Nummern.ZutrittsIdentAbgang",
                        blNummernformen.formatiereNrKomplett(nummer, "", KonstKartenklasse.eintrittskartennummer,
                                KonstKartenart.abgang));
                rpVariablen.fuelleVariable(rpDrucken, "Nummern.ZutrittsIdentWiederzugang",
                        blNummernformen.formatiereNrKomplett(nummer, "", KonstKartenklasse.eintrittskartennummer,
                                KonstKartenart.wiederzugang));
                rpVariablen.fuelleVariable(rpDrucken, "Nummern.ZutrittsIdentVollmachtDritte",
                        blNummernformen.formatiereNrKomplett(nummer, "", KonstKartenklasse.eintrittskartennummer,
                                KonstKartenart.vollmachtAnDritteErteilen));
                rpVariablen.fuelleVariable(rpDrucken, "Nummern.ZutrittsIdentSRV", blNummernformen.formatiereNrKomplett(
                        nummer, "", KonstKartenklasse.eintrittskartennummer, KonstKartenart.vollmachtWeisungSRV));
                blNummernformen.rcStimmkartennummer = 1;
                rpVariablen.fuelleVariable(rpDrucken, "Nummern.ZutrittsIdentStimmkarte01",
                        blNummernformen.formatiereNrKomplett(nummer, "", KonstKartenklasse.eintrittskartennummer,
                                KonstKartenart.stimmabschnittsnummer));
                blNummernformen.rcStimmkartennummer = 2;
                rpVariablen.fuelleVariable(rpDrucken, "Nummern.ZutrittsIdentStimmkarte02",
                        blNummernformen.formatiereNrKomplett(nummer, "", KonstKartenklasse.eintrittskartennummer,
                                KonstKartenart.stimmabschnittsnummer));
            } else {
                rpVariablen.fuelleVariable(rpDrucken, "Nummern.ZutrittsIdent",
                        BlNummernformen.verketteEKMitNeben(blNummernformen.formatiereEKNr(nummer), nebenNummer, 1));

                rpVariablen.fuelleVariable(rpDrucken, "Nummern.ZutrittsIdentErstzugang",
                        blNummernformen.formatiereNrKomplett(nummer, nebenNummer,
                                KonstKartenklasse.eintrittskartennummerNeben, KonstKartenart.erstzugang));
                rpVariablen.fuelleVariable(rpDrucken, "Nummern.ZutrittsIdentAbgang",
                        blNummernformen.formatiereNrKomplett(nummer, nebenNummer,
                                KonstKartenklasse.eintrittskartennummerNeben, KonstKartenart.abgang));
                rpVariablen.fuelleVariable(rpDrucken, "Nummern.ZutrittsIdentWiederzugang",
                        blNummernformen.formatiereNrKomplett(nummer, nebenNummer,
                                KonstKartenklasse.eintrittskartennummerNeben, KonstKartenart.wiederzugang));
                rpVariablen.fuelleVariable(rpDrucken, "Nummern.ZutrittsIdentVollmachtDritte",
                        blNummernformen.formatiereNrKomplett(nummer, nebenNummer,
                                KonstKartenklasse.eintrittskartennummerNeben,
                                KonstKartenart.vollmachtAnDritteErteilen));
                rpVariablen.fuelleVariable(rpDrucken, "Nummern.ZutrittsIdentSRV",
                        blNummernformen.formatiereNrKomplett(nummer, nebenNummer,
                                KonstKartenklasse.eintrittskartennummerNeben, KonstKartenart.vollmachtWeisungSRV));
                blNummernformen.rcStimmkartennummer = 1;
                rpVariablen.fuelleVariable(rpDrucken, "Nummern.ZutrittsIdentStimmkarte01",
                        blNummernformen.formatiereNrKomplett(nummer, nebenNummer,
                                KonstKartenklasse.eintrittskartennummerNeben, KonstKartenart.stimmabschnittsnummer));
                blNummernformen.rcStimmkartennummer = 2;
                rpVariablen.fuelleVariable(rpDrucken, "Nummern.ZutrittsIdentStimmkarte02",
                        blNummernformen.formatiereNrKomplett(nummer, nebenNummer,
                                KonstKartenklasse.eintrittskartennummerNeben, KonstKartenart.stimmabschnittsnummer));

            }

            nummer = Integer.toString(vonNrSK);

            rpVariablen.fuelleVariable(rpDrucken, "Nummern.Stimmkarte",
                    blNummernformen.formatiereNr(nummer, KonstKartenklasse.stimmkartennummer));

            rpVariablen.fuelleVariable(rpDrucken, "Nummern.StimmkarteErstzugang", blNummernformen.formatiereNrKomplett(
                    nummer, "", KonstKartenklasse.stimmkartennummer, KonstKartenart.stimmkartenEtikett));
            rpVariablen.fuelleVariable(rpDrucken, "Nummern.StimmkarteAbgang", blNummernformen
                    .formatiereNrKomplett(nummer, "", KonstKartenklasse.stimmkartennummer, KonstKartenart.abgang));
            rpVariablen.fuelleVariable(rpDrucken, "Nummern.StimmkarteWiederzugang",
                    blNummernformen.formatiereNrKomplett(nummer, "", KonstKartenklasse.stimmkartennummer,
                            KonstKartenart.wiederzugang));
            rpVariablen.fuelleVariable(rpDrucken, "Nummern.StimmkarteVollmachtDritte",
                    blNummernformen.formatiereNrKomplett(nummer, "", KonstKartenklasse.stimmkartennummer,
                            KonstKartenart.vollmachtAnDritteErteilen));
            rpVariablen.fuelleVariable(rpDrucken, "Nummern.StimmkarteSRV", blNummernformen.formatiereNrKomplett(nummer,
                    "", KonstKartenklasse.stimmkartennummer, KonstKartenart.vollmachtWeisungSRV));
            blNummernformen.rcStimmkartennummer = 1;
            rpVariablen.fuelleVariable(rpDrucken, "Nummern.StimmkarteStimmkarte01",
                    blNummernformen.formatiereNrKomplett(nummer, "", KonstKartenklasse.stimmkartennummer,
                            KonstKartenart.stimmabschnittsnummer));
            blNummernformen.rcStimmkartennummer = 2;
            rpVariablen.fuelleVariable(rpDrucken, "Nummern.StimmkarteStimmkarte02",
                    blNummernformen.formatiereNrKomplett(nummer, "", KonstKartenklasse.stimmkartennummer,
                            KonstKartenart.stimmabschnittsnummer));

            rpDrucken.druckenFormular();
            vonNrSK++;

        }
        rpDrucken.endeFormular();

    }

    public void druckTestEinladungen(DbBundle pDbBundle, RpDrucken rpDrucken, String pLfdNummer,
            int pVonNrAktionaer, int pBisNrAktionaer) {
        
        rpDrucken.initFormular(pDbBundle);

        RpVariablen rpVariablen = new RpVariablen(pDbBundle);
        rpVariablen.testEinladungen(pLfdNummer, rpDrucken);
        rpDrucken.startFormular();

        for (int i = pVonNrAktionaer; i <= pBisNrAktionaer; i++) {

            String nummer = Integer.toString(i);
            while (nummer.length() < 3) {
                nummer = "0" + nummer;
            }
            // add here your preferred DokumentGenerator
            rpDrucken.druckenFormular();

        }
        rpDrucken.endeFormular();

    }
}
