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
package de.meetingapps.meetingclient.meetingClientDialoge;

import de.meetingapps.meetingportal.meetComAllg.CaString;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.stage.Stage;

/**
 * The Class CtrlRoot.
 */
public class CtrlRoot {

    /** The eigene stage. */
    public Stage eigeneStage;

    /** The eigener titel. */
    public String eigenerTitel = "";

    /** The eigener titel mit mandant. */
    public boolean eigenerTitelMitMandant = false;

    /** ************Prüf-Routinen********************************************. */

    protected String lFehlertext = "";

    /** The fehlerhaftes feld. */
    protected TextInputControl fehlerhaftesFeld = null;

    /**
     * Pruefe 01.
     *
     * @param tf       the tf
     * @param feldname the feldname
     */
    protected void pruefe01(TextField tf, String feldname) {
        String hString = tf.getText().trim();
        tf.setText(hString);
        if (hString.compareTo("0") != 0 && hString.compareTo("1") != 0) {
            lFehlertext = feldname + ": nur 0 oder 1 zulässig!";
            fehlerhaftesFeld = tf;
        }
    }

    /**
     * Pruefe 12.
     *
     * @param tf       the tf
     * @param feldname the feldname
     */
    protected void pruefe12(TextField tf, String feldname) {
        String hString = tf.getText().trim();
        tf.setText(hString);
        if (hString.compareTo("1") != 0 && hString.compareTo("2") != 0) {
            lFehlertext = feldname + ": nur 1 oder 2 zulässig!";
            fehlerhaftesFeld = tf;
        }
    }

    /**
     * Pruefe 012.
     *
     * @param tf       the tf
     * @param feldname the feldname
     */
    protected void pruefe012(TextField tf, String feldname) {
        String hString = tf.getText().trim();
        tf.setText(hString);
        if (hString.compareTo("0") != 0 && hString.compareTo("1") != 0 && hString.compareTo("2") != 0) {
            lFehlertext = feldname + ": nur 0, 1 oder 2 zulässig!";
            fehlerhaftesFeld = tf;
        }
    }

    /**
     * Pruefe 123.
     *
     * @param tf       the tf
     * @param feldname the feldname
     */
    protected void pruefe123(TextField tf, String feldname) {
        String hString = tf.getText().trim();
        tf.setText(hString);
        if (hString.compareTo("1") != 0 && hString.compareTo("2") != 0 && hString.compareTo("3") != 0) {
            lFehlertext = feldname + ": nur 1, 2 oder 3 zulässig!";
            fehlerhaftesFeld = tf;
        }
    }

    /**
     * Pruefe 0123.
     *
     * @param tf       the tf
     * @param feldname the feldname
     */
    protected void pruefe0123(TextField tf, String feldname) {
        String hString = tf.getText().trim();
        tf.setText(hString);
        if (hString.compareTo("0") != 0 && hString.compareTo("1") != 0 && hString.compareTo("2") != 0
                && hString.compareTo("3") != 0) {
            lFehlertext = feldname + ": nur 0, 1, 2 oder 3 zulässig!";
            fehlerhaftesFeld = tf;
        }
    }

    /**
     * Pruefe 12345.
     *
     * @param tf       the tf
     * @param feldname the feldname
     */
    protected void pruefe12345(TextField tf, String feldname) {
        String hString = tf.getText().trim();
        tf.setText(hString);
        if (hString.compareTo("1") != 0 && hString.compareTo("2") != 0 && hString.compareTo("3") != 0
                && hString.compareTo("4") != 0 && hString.compareTo("5") != 0) {
            lFehlertext = feldname + ": nur 1, 2, 3, 4 oder 5 zulässig!";
            fehlerhaftesFeld = tf;
        }
    }

    /**
     * Pruefe zahl oder leer.
     *
     * @param tf       the tf
     * @param feldname the feldname
     */
    protected void pruefeZahlOderLeer(TextField tf, String feldname) {
        String hString = tf.getText().trim();
        tf.setText(hString);

        if (tf.getText().isEmpty()) {
            return;
        }
        if (!CaString.isNummern(tf.getText())) {
            lFehlertext = feldname + ": nur Ziffern zulässig!";
            fehlerhaftesFeld = tf;
        }
    }

    /**
     * Pruefe zahl auch negativ oder leer.
     *
     * @param tf       the tf
     * @param feldname the feldname
     */
    protected void pruefeZahlAuchNegativOderLeer(TextField tf, String feldname) {
        String hString = tf.getText().trim();
        tf.setText(hString);

        if (tf.getText().isEmpty()) {
            return;
        }
        if (!CaString.isNummernNegativ(tf.getText())) {
            lFehlertext = feldname + ": nur Ziffern und - zulässig!";
            fehlerhaftesFeld = tf;
        }
    }

    /**
     * Pruefe zahl von bis.
     *
     * @param tf       the tf
     * @param feldname the feldname
     * @param pVon     the von
     * @param pBis     the bis
     */
    protected void pruefeZahlVonBis(TextField tf, String feldname, int pVon, int pBis) {
        String hString = tf.getText().trim();
        tf.setText(hString);

        if (tf.getText().isEmpty() || !CaString.isNummern(tf.getText())) {
            lFehlertext = feldname + ": nur Ziffern zulässig!";
            fehlerhaftesFeld = tf;
            return;
        }
        int hInt = Integer.parseInt(tf.getText());
        if (hInt < pVon || hInt > pBis) {
            lFehlertext = feldname + ": Zahl muß zwischen " + Integer.toString(pVon) + " und " + Integer.toString(pBis)
                    + " liegen!";
        }

    }

    /**
     * Pruefe zahl nicht leer laenge.
     *
     * @param tf             the tf
     * @param feldname       the feldname
     * @param maximaleLaenge the maximale laenge
     */
    protected void pruefeZahlNichtLeerLaenge(TextField tf, String feldname, int maximaleLaenge) {
        String hString = tf.getText().trim();
        tf.setText(hString);

        if (tf.getText().isEmpty() || !CaString.isNummern(tf.getText()) || tf.getText().length() > maximaleLaenge) {
            lFehlertext = feldname + ": nur Ziffern zulässig!";
            fehlerhaftesFeld = tf;
        }
    }

    /**
     * Pruefe zahl nicht leer laenge.
     *
     * @param ta             the ta
     * @param feldname       the feldname
     * @param maximaleLaenge the maximale laenge
     */
    protected void pruefeZahlNichtLeerLaenge(TextArea ta, String feldname, int maximaleLaenge) {
        String hString = ta.getText().trim();
        ta.setText(hString);

        if (ta.getText().isEmpty() || !CaString.isNummern(ta.getText()) || ta.getText().length() > maximaleLaenge) {
            lFehlertext = feldname + ": nur Ziffern zulässig!";
            fehlerhaftesFeld = ta;
        }
    }

    /**
     * Pruefe zahl auch negativ nicht leer laenge.
     *
     * @param tfDialogveranstaltung the tf dialogveranstaltung
     * @param feldname              the feldname
     * @param maximaleLaenge        the maximale laenge
     */
    protected void pruefeZahlAuchNegativNichtLeerLaenge(TextField tfDialogveranstaltung, String feldname,
            int maximaleLaenge) {
        String hString = tfDialogveranstaltung.getText().trim();
        tfDialogveranstaltung.setText(hString);

        if (tfDialogveranstaltung.getText().isEmpty() || !CaString.isNummernNegativ(tfDialogveranstaltung.getText())
                || tfDialogveranstaltung.getText().length() > maximaleLaenge) {
            lFehlertext = feldname + ": nur Ziffern zulässig!";
            fehlerhaftesFeld = tfDialogveranstaltung;
        }
    }

    /**
     * Pruefe laenge.
     *
     * @param tf       the tf
     * @param feldname the feldname
     * @param laenge   the laenge
     */
    protected void pruefeLaenge(TextField tf, String feldname, int laenge) {
        String hString = tf.getText().trim();
        tf.setText(hString);

        int hLaenge = tf.getText().length();
        if (hLaenge > laenge) {
            lFehlertext = feldname + ": maximale Länge=" + Integer.toString(laenge) + ", derzeit "
                    + Integer.toString(hLaenge);
            fehlerhaftesFeld = tf;
        }
    }

    /**
     * Pruefe laenge.
     *
     * @param tf       the tf
     * @param feldname the feldname
     * @param laenge   the laenge
     */
    protected void pruefeLaenge(TextArea tf, String feldname, int laenge) {
        String hString = tf.getText().trim();
        tf.setText(hString);

        int hLaenge = tf.getText().length();
        if (hLaenge > laenge) {
            lFehlertext = feldname + ": maximale Länge=" + Integer.toString(laenge) + ", derzeit "
                    + Integer.toString(hLaenge);
            fehlerhaftesFeld = tf;
        }
    }

    /**
     * Pruefe nicht leer.
     *
     * @param tf       the tf
     * @param feldname the feldname
     */
    protected void pruefeNichtLeer(TextField tf, String feldname) {
        String hString = tf.getText().trim();
        tf.setText(hString);

        int hLaenge = tf.getText().length();
        if (hLaenge == 0) {
            lFehlertext = feldname + ": darf nicht leer sein";
            fehlerhaftesFeld = tf;
        }
    }

    /**
     * Pruefe nicht leer und laenge.
     *
     * @param tf       the tf
     * @param feldname the feldname
     * @param laenge   the laenge
     */
    protected void pruefeNichtLeerUndLaenge(TextField tf, String feldname, int laenge) {
        String hString = tf.getText().trim();
        tf.setText(hString);

        int hLaenge = tf.getText().length();
        if (hLaenge == 0) {
            lFehlertext = feldname + ": darf nicht leer sein";
            fehlerhaftesFeld = tf;
        }
        if (hLaenge > laenge) {
            lFehlertext = feldname + ": maximale Länge=" + Integer.toString(laenge) + ", derzeit "
                    + Integer.toString(hLaenge);
            fehlerhaftesFeld = tf;
        }
    }

    /**
     * ******Event-Bearbeitungs-Unterstützung***********************.
     * 
     * Ermittelt, welcher Button gedrückt wurde. -1 => nix gefunden
     *
     * @param pButtonArray the button array
     * @param event        the event
     * @return the int
     */
    protected int findeButton(Button[] pButtonArray, ActionEvent event) {
        if (pButtonArray == null) {
            return -1;
        }
        for (int i = 0; i < pButtonArray.length; i++) {
            if (pButtonArray[i] != null && pButtonArray[i] == event.getSource()) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Fehlermeldungsdialog anzeigen
     * 
     * Muster für Listener eintragen.
     *
     * @param fehlertext the fehlertext
     */
    protected void fehlerMeldung(String fehlertext) {
        CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
        caZeigeHinweis.zeige(eigeneStage, fehlertext);
        if (fehlerhaftesFeld != null) {
            fehlerhaftesFeld.requestFocus();
        }
        fehlerhaftesFeld = null;
        return;
    }

    /**
     * ****************ausgeführt anzeigen**************************** Wenn
     * hinweisText="", dann standardmäßig "Ausgeführt".
     *
     * @param hinweisText the hinweis text
     */
    protected void ausgefuehrtMeldung(String hinweisText) {
        CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
        String lHinweisText = hinweisText;
        if (lHinweisText.isEmpty()) {
            lHinweisText = "Ausgeführt!";
        }
        caZeigeHinweis.zeige(eigeneStage, lHinweisText);
        return;
    }

}
