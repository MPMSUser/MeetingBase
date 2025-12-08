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
package de.meetingapps.meetingclient.meetingBestand;

import de.meetingapps.meetingclient.meetingClientDialoge.CtrlRootDrucklauf;
import de.meetingapps.meetingportal.meetComAllg.CaDatumZeit;
import de.meetingapps.meetingportal.meetComBl.BlAufgaben;
import de.meetingapps.meetingportal.meetComEntities.EclAktienregister;
import de.meetingapps.meetingportal.meetComEntities.EclPersonenNatJur;
import de.meetingapps.meetingportal.meetComKonst.KonstVerarbeitungslaufArt;
import de.meetingapps.meetingportal.meetingCoreReport.RpDrucken;
import de.meetingapps.meetingportal.meetingCoreReport.RpVariablen;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;

/**
 * The Class CtrlPasswortDrucken.
 */
public class CtrlPasswortDrucken extends CtrlRootDrucklauf {

    /**
     * Initialize.
     */
    @FXML
    void initialize() {

        /*Masken-spezifische Auswahl setzen*/
        auswahlReportausgabe = true;
        auswahlReportausgabeVarianten = false;
        auswahlCSV = false;
        auswahlCSVVarianten = false;

        auswahlDrucklaufAlleZulaessig = false;

        verarbeitungslaufArt = KonstVerarbeitungslaufArt.neuesPasswort;

        /*Allgemeine Initialisierung*/

        initializeRootDrucklaufFxml();

    }

    /**
     * On btn ausfuehren.
     *
     * @param event the event
     */
    @Override
    @FXML
    public void onBtnAusfuehren(ActionEvent event) {

        boolean brc = doAusfuehrenEingabeVerarbeiten();
        if (!brc) {
            return;
        }

        BlAufgaben blAufgaben = new BlAufgaben(false, lDbBundle);
        blAufgaben.pw_aufbereitenDrucklauf(rcDrucklaufNr, 0, 0);

        int zMandant, zHVJahr;
        String zHVNummer, zDatenbereich;
        zMandant = lDbBundle.clGlobalVar.mandant;
        zHVJahr = lDbBundle.clGlobalVar.hvJahr;
        zHVNummer = lDbBundle.clGlobalVar.hvNummer;
        zDatenbereich = lDbBundle.clGlobalVar.datenbereich;

        RpDrucken rpDrucken = new RpDrucken();
        RpVariablen rpVariablen = new RpVariablen(lDbBundle);

        int alterMandant = -1;
        for (int i = 0; i < blAufgaben.rcMandantListe.size(); i++) {
            int mandant = blAufgaben.rcMandantListe.get(i);
            int fehlercode = blAufgaben.rcFehlercode.get(i);
            if (fehlercode == 1) {
                if (mandant != alterMandant) {
                    if (alterMandant != -1) {
                        rpDrucken.endeFormular();
                    }

                    lDbBundle.clGlobalVar.mandant = blAufgaben.rcMandantListe.get(i);
                    lDbBundle.clGlobalVar.hvJahr = blAufgaben.rcHvJahrListe.get(i);
                    lDbBundle.clGlobalVar.hvNummer = blAufgaben.rcHvNummer.get(i);
                    lDbBundle.clGlobalVar.datenbereich = blAufgaben.rcDatenbereich.get(i);

                    alterMandant = lDbBundle.clGlobalVar.mandant;

                    rpDrucken.initClientDrucke();

                    rpDrucken.initFormular(lDbBundle);
                    rpVariablen = new RpVariablen(lDbBundle);
                    rpVariablen.anschreibenNeuesPasswort(rcDruckvariante, rpDrucken);
                    rpDrucken.startFormular();

                }
                EclAktienregister lAktienregister = blAufgaben.rcAktienregisterListe.get(i);
                if (blAufgaben.rcAufgabenListe.get(i).argument[4].isEmpty()) {
                    /*Dann Versanddaten aus Aktienregister / Personen holen*/
                    if (lAktienregister != null) {
                        rpVariablen.fuelleVariable(rpDrucken, "Art", "1");

                        rpVariablen.fuelleVariable(rpDrucken, "Adresszeile1", lAktienregister.adresszeile1);
                        rpVariablen.fuelleVariable(rpDrucken, "Adresszeile2", lAktienregister.adresszeile2);
                        rpVariablen.fuelleVariable(rpDrucken, "Adresszeile3", lAktienregister.adresszeile3);
                        rpVariablen.fuelleVariable(rpDrucken, "Adresszeile4", lAktienregister.adresszeile4);
                        rpVariablen.fuelleVariable(rpDrucken, "Adresszeile5", lAktienregister.adresszeile5);
                        rpVariablen.fuelleVariable(rpDrucken, "Adresszeile6", lAktienregister.adresszeile6);
                        rpVariablen.fuelleVariable(rpDrucken, "Adresszeile7", lAktienregister.adresszeile7);
                        rpVariablen.fuelleVariable(rpDrucken, "Adresszeile8", lAktienregister.adresszeile8);
                        rpVariablen.fuelleVariable(rpDrucken, "Adresszeile9", lAktienregister.adresszeile9);
                        rpVariablen.fuelleVariable(rpDrucken, "Adresszeile10", lAktienregister.adresszeile10);
                    } else {

                        rpVariablen.fuelleVariable(rpDrucken, "Art", "2");

                        EclPersonenNatJur lPersonenNatJur = blAufgaben.rcPersonNatJurListe.get(i);

                        rpVariablen.fuelleVariable(rpDrucken, "Adresszeile1",
                                lPersonenNatJur.liefereTitelVornameName());
                        rpVariablen.fuelleVariable(rpDrucken, "Adresszeile2", lPersonenNatJur.strasse);
                        rpVariablen.fuelleVariable(rpDrucken, "Adresszeile3",
                                lPersonenNatJur.plz + " " + lPersonenNatJur.ort);
                        rpVariablen.fuelleVariable(rpDrucken, "Adresszeile4", "");
                        rpVariablen.fuelleVariable(rpDrucken, "Adresszeile5", "");
                        rpVariablen.fuelleVariable(rpDrucken, "Adresszeile6", "");
                        rpVariablen.fuelleVariable(rpDrucken, "Adresszeile7", "");
                        rpVariablen.fuelleVariable(rpDrucken, "Adresszeile8", "");
                        rpVariablen.fuelleVariable(rpDrucken, "Adresszeile9", "");
                        rpVariablen.fuelleVariable(rpDrucken, "Adresszeile10", "");

                    }
                } else {
                    rpVariablen.fuelleVariable(rpDrucken, "Art", "1");

                    rpVariablen.fuelleVariable(rpDrucken, "Adresszeile1",
                            blAufgaben.rcAufgabenListe.get(i).argument[4]);
                    rpVariablen.fuelleVariable(rpDrucken, "Adresszeile2",
                            blAufgaben.rcAufgabenListe.get(i).argument[5]);
                    rpVariablen.fuelleVariable(rpDrucken, "Adresszeile3",
                            blAufgaben.rcAufgabenListe.get(i).argument[6]);
                    rpVariablen.fuelleVariable(rpDrucken, "Adresszeile4",
                            blAufgaben.rcAufgabenListe.get(i).argument[7]);
                    rpVariablen.fuelleVariable(rpDrucken, "Adresszeile5", "");
                    rpVariablen.fuelleVariable(rpDrucken, "Adresszeile6", "");
                    rpVariablen.fuelleVariable(rpDrucken, "Adresszeile7", "");
                    rpVariablen.fuelleVariable(rpDrucken, "Adresszeile8", "");
                    rpVariablen.fuelleVariable(rpDrucken, "Adresszeile9", "");
                    rpVariablen.fuelleVariable(rpDrucken, "Adresszeile10", "");

                }
                rpVariablen.fuelleVariable(rpDrucken, "FormularArt", blAufgaben.rcAufgabenListe.get(i).argument[3]);
                String kennung = blAufgaben.rcKennungListe.get(i);
                rpVariablen.fuelleVariable(rpDrucken, "Aktionaersnummer", kennung);

                rpVariablen.fuelleVariable(rpDrucken, "AktuellesDatum", CaDatumZeit.DatumStringFuerAnzeige());
                rpVariablen.fuelleVariable(rpDrucken, "Gesellschaft", blAufgaben.rcEmittent.get(i));

                rpVariablen.fuelleVariable(rpDrucken, "NeuesPasswort", blAufgaben.rcPasswortListe.get(i));
                rpDrucken.druckenFormular();
            }
        }

        if (alterMandant != -1) {
            rpDrucken.endeFormular();
        }

        lDbBundle.clGlobalVar.mandant = zMandant;
        lDbBundle.clGlobalVar.hvJahr = zHVJahr;
        lDbBundle.clGlobalVar.hvNummer = zHVNummer;
        lDbBundle.clGlobalVar.datenbereich = zDatenbereich;

        doAusfuehrenBeendet();
    }

    /**
     * Inits the.
     *
     * @param pEigeneStage the eigene stage
     */
    public void init(Stage pEigeneStage) {
        eigeneStage = pEigeneStage;
    }

}
