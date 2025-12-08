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

import de.meetingapps.meetingportal.meetComAllg.CaDatumZeit;
import de.meetingapps.meetingportal.meetComAllg.CaString;
import de.meetingapps.meetingportal.meetComHVParam.ParamS;
import de.meetingapps.meetingportal.meetComKonst.KonstEingabeQuelle;
import de.meetingapps.meetingportal.meetComWE.WELoginVerify;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

/**
 * The Class SClErfassungsDaten.
 */
public class SClErfassungsDaten {

    /** The erklaerung zeit. */
    public static String erklaerungZeit = "09:00";

    /** The erklaerung datum. */
    public static String erklaerungDatum = "11.11.1111";

    /** The eingangsweg. */
    public static String eingangsweg = "Post";

    /**
     * Inits the.
     */
    public static void init() {

        erklaerungZeit = "09:00";
        erklaerungDatum = CaDatumZeit.DatumZeitStringFuerAnzeige(CaDatumZeit.DatumZeitStringFuerDatenbank())
                .substring(0, 10);
        eingangsweg = KonstEingabeQuelle.getText(KonstEingabeQuelle.papierPost_ausserhalbHV);
    }

    /**
     * Inits the erfassungsfelder.
     *
     * @param tfErklaerungDatum the tf erklaerung datum
     * @param tfErklaerungZeit  the tf erklaerung zeit
     * @param cbEingangsWeg     the cb eingangs weg
     */
    public static void initErfassungsfelder(TextField tfErklaerungDatum, TextField tfErklaerungZeit,
            ComboBox<String> cbEingangsWeg) {

        /*EingangsDatum*/
        tfErklaerungDatum.textProperty().addListener((observable, oldValue, newValue) -> {
            erklaerungDatum = newValue;
        });
        tfErklaerungDatum.setText(erklaerungDatum);

        /*EingangsZeit*/
        tfErklaerungZeit.textProperty().addListener((observable, oldValue, newValue) -> {
            erklaerungZeit = newValue;
        });
        tfErklaerungZeit.setText(erklaerungZeit);

        /*Eingangsweg*/
        cbEingangsWeg.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(@SuppressWarnings("rawtypes") ObservableValue ov, String t, String t1) {
                if (t1 != null) {
                    eingangsweg = t1;
                }
            }
        });

        cbEingangsWeg.getItems().removeAll(cbEingangsWeg.getItems());
        cbEingangsWeg.getItems().add(KonstEingabeQuelle.getText(KonstEingabeQuelle.papierPost_ausserhalbHV));
        cbEingangsWeg.getItems().add(KonstEingabeQuelle.getText(KonstEingabeQuelle.fax_ausserhalbHV));
        cbEingangsWeg.getItems().add(KonstEingabeQuelle.getText(KonstEingabeQuelle.eMail_ausserhalbHV));
        if (ParamS.eclEmittent.appVorhanden > 0) {
            cbEingangsWeg.getItems().add(KonstEingabeQuelle.getText(KonstEingabeQuelle.app));
        }
        if (ParamS.eclEmittent.portalVorhanden > 0) {
            cbEingangsWeg.getItems().add(KonstEingabeQuelle.getText(KonstEingabeQuelle.portal));
        }
        cbEingangsWeg.getItems().add(KonstEingabeQuelle.getText(KonstEingabeQuelle.sonstigesAnmeldestelle));
        cbEingangsWeg.getItems().add(KonstEingabeQuelle.getText(KonstEingabeQuelle.sonstigesVorOrt));
        cbEingangsWeg.getItems().add(KonstEingabeQuelle.getText(KonstEingabeQuelle.importBankenDatei));

        cbEingangsWeg.setValue(eingangsweg);
        System.out.println("Setze Eingangsweg=" + eingangsweg);

    }

    /**
     * Pruefe eingaben.
     *
     * @param tfErklaerungDatum the tf erklaerung datum
     * @param tfErklaerungZeit  the tf erklaerung zeit
     * @return the string
     */
    public static String pruefeEingaben(TextField tfErklaerungDatum, TextField tfErklaerungZeit) {
        if (CaString.isZeit(tfErklaerungZeit.getText()) == false) {
            return ("Bitte Zeit in der Form HH:MM eingeben!");
        }
        if (CaString.isDatum(tfErklaerungDatum.getText()) == false) {
            return ("Bitte Datum in der Form TT.MM.JJJJ eingeben!");
        }

        return null;
    }

    /**
     * Fuelle login verify.
     *
     * @param weLoginVerify     the we login verify
     * @param tfErklaerungDatum the tf erklaerung datum
     * @param tfErklaerungZeit  the tf erklaerung zeit
     * @param cbEingangsWeg     the cb eingangs weg
     */
    public static void fuelleLoginVerify(WELoginVerify weLoginVerify, TextField tfErklaerungDatum,
            TextField tfErklaerungZeit, ComboBox<String> cbEingangsWeg) {
        weLoginVerify.setEingabeQuelle(KonstEingabeQuelle
                .getNummerZuText(cbEingangsWeg.getValue())); /*Siehe EclWillenserklaerung - erteiltAufWeg*/
        weLoginVerify.setErteiltZeitpunkt(CaDatumZeit
                .DatumZeitStringFuerDatenbank(tfErklaerungDatum.getText() + " " + tfErklaerungZeit.getText() + ":00"));
    }

}
