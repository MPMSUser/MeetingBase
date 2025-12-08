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

import de.meetingapps.meetingclient.meetingClientDialoge.CtrlRoot;
import de.meetingapps.meetingclient.meetingClientOberflaechen.ObjectActions;
import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComAllg.CaString;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclLoginDaten;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class CtrlMailingdaten extends CtrlRoot {

    public final int width = 500;
    public final int height = 250;

    @FXML
    private AnchorPane rootPane;

    @FXML
    private CheckBox cbMailversand;

    @FXML
    private TextField tfEmail1;

    @FXML
    private CheckBox cbEmail1;

    @FXML
    private TextField tfEmail2;

    @FXML
    private CheckBox cbEmail2;

    private Label notification;

    private int aktienregisterIdent = 0;

    private EclLoginDaten loginDaten;
    private DbBundle dbBundle;

    @FXML
    void initialize() {

        eigeneStage.setResizable(false);

        leseLoginDaten();
        ladeLoginDaten();

        tfEmail1.textProperty().addListener((o, oV, nV) -> {
            if (nV.isBlank())
                cbEmail1.setSelected(false);
        });

        notification = ObjectActions.createNotification(rootPane, -1, 150.0, 15.0, -1);

    }

    private void leseLoginDaten() {

        dbBundle = new DbBundle();
        dbBundle.openAll();
        if (dbBundle.dbLoginDaten.read_aktienregisterIdent(aktienregisterIdent) > 0) {
            loginDaten = dbBundle.dbLoginDaten.ergebnisPosition(0);
        }
        dbBundle.closeAll();
    }

    private void ladeLoginDaten() {

        cbMailversand.setSelected(loginDaten.eVersandRegistriert());

        tfEmail1.setText(loginDaten.geteMailFuerVersand());
        cbEmail1.setSelected(loginDaten.getEmailBestaetigt() == 1);

        tfEmail2.setText(loginDaten.geteMail2FuerVersand());
        cbEmail2.setDisable(true);
    }

    @FXML
    private void onBeenden() {
        eigeneStage.hide();
    }

    @FXML
    private void onSpeichern() {

        if (changeCheck()) {

            loginDaten.eVersandRegistrierung = cbMailversand.isSelected() ? 99 : 0;

            loginDaten.eMailFuerVersand = tfEmail1.getText().trim();
            loginDaten.emailBestaetigt = cbEmail1.isSelected() ? 1 : 0;

            loginDaten.eMail2FuerVersand = tfEmail2.getText().trim();

            if (!checkMail(loginDaten.eMailFuerVersand)) {
                ObjectActions.showNotification(notification, "E-Mail 1 ist ungültig.");
                return;
            }

            if (!checkMail(loginDaten.eMail2FuerVersand)) {
                ObjectActions.showNotification(notification, "E-Mail 2 ist ungültig.");
                return;
            }

            dbBundle.openAll();
            int erg = dbBundle.dbLoginDaten.update(loginDaten);
            dbBundle.closeAll();

            if (erg == CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert) {
                leseLoginDaten();
                ladeLoginDaten();
                ObjectActions.showNotification(notification, "Bereits geändert.\nDaten wurden neu geladen.");
                return;
            }
            ObjectActions.showNotification(notification, "Gespeichert.");
        } else {
            ObjectActions.showNotification(notification, "Keine Änderung.");
        }
    }

    private Boolean checkMail(String mail) {
        if (mail.isBlank()) {
            return true;
        } else {
            return CaString.isMailadresse(mail);
        }

    }

    private Boolean changeCheck() {

        return loginDaten.eVersandRegistrierung != (cbMailversand.isSelected() ? 99 : 0)
                || !loginDaten.eMailFuerVersand.equals(tfEmail1.getText().trim())
                || loginDaten.emailBestaetigt != (cbEmail1.isSelected() ? 1 : 0)
                || !loginDaten.eMail2FuerVersand.equals(tfEmail2.getText().trim());

    }

    public void init(Stage eigeneStage, int aktienregisterIdent) {
        this.eigeneStage = eigeneStage;
        this.aktienregisterIdent = aktienregisterIdent;
    }
}
