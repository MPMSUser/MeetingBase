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
package de.meetingapps.meetingclient.meetingClientOberflaechen;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.GridPane;

/**
 * The Class CustomAlert.
 */
public class CustomAlert {

    /**
     * Confirm alert.
     *
     * @param title  the title
     * @param header the header
     * @param text   the text
     * @return the boolean
     */
    public static Boolean confirmAlert(String title, String header, String text) {

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(text);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK)
            return true;
        else
            return false;
    }

    public static void informationAlert(String title, String header, String text) {

        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(text);

        alert.showAndWait();
    }

    public static Boolean passwortConfirmation(String pw, String title, String header, String text) {

        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle(title);
        dialog.setHeaderText(header);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        GridPane grid = new GridPane();

        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 10, 10, 10));

        Label lbl = new Label(text);
        PasswordField tfPasswort = new PasswordField();

        grid.add(lbl, 0, 0);
        GridPane.setColumnSpan(lbl, 2);
        grid.add(new Label("Passwort:"), 0, 1);
        grid.add(tfPasswort, 1, 1);

        Node okButton = dialog.getDialogPane().lookupButton(ButtonType.OK);
        okButton.setDisable(true);

        tfPasswort.textProperty().addListener((o, oV, nV) -> okButton.setDisable(!nV.equals(pw)));

        dialog.getDialogPane().setContent(grid);

        Platform.runLater(() -> tfPasswort.requestFocus());

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                return tfPasswort.getText();
            }
            return null;
        });

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent())
            return true;
        else
            return false;
    }

    public static int comboBoxAlert(Map<String, Integer> map, String title, String header) {

        ArrayList<String> list = new ArrayList<>();
        String defaultItem = null;

        for (var m : map.keySet()) {
            list.add(m);
            if (defaultItem == null)
                defaultItem = m;
        }

        ChoiceDialog<String> dialog = new ChoiceDialog<>(defaultItem, list);

        dialog.setTitle(title);
        dialog.setHeaderText(header);

        dialog.setContentText("Bitte auswählen:");

        Optional<String> result = dialog.showAndWait();

        if (result.isPresent())
            return map.get(result.get());
        else
            return 0;
    }

}
