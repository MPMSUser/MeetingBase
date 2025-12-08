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

import java.io.File;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;

import de.meetingapps.meetingclient.meetingClientDialoge.CtrlRoot;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * The Class CtrlEintrittskartenDefinition.
 */
public class CtrlEintrittskartenDefinition extends CtrlRoot {

    /** The ta text 1. */
    @FXML
    private TextArea taText1;

    /** The ta text 1 preview. */
    @FXML
    private TextArea taText1Preview;

    /** The btn preview. */
    @FXML
    private Button btnPreview;

    /** The ta text 2. */
    @FXML
    private TextArea taText2;

    /** The image view. */
    @FXML
    private ImageView imageView;

    /** The btn logo. */
    @FXML
    private Button btnLogo;

    /** The btn logo delete. */
    @FXML
    private Button btnLogoDelete;

    /** The btn save. */
    @FXML
    private Button btnSave;

    /** The variable box. */
    @FXML
    private VBox variableBox;

    /** The map. */
    private Map<String, String> map = new HashMap<>();

    /** The initial text 1. */
    final String initialText1 = "<HV> Hauptversammlung der\n\n<KD>\n\n<DT>\nam <WO>, den <DT>, <BZ> Uhr,\n(Einlass ab <EZ> Uhr)\n<VO>,\n<STR>, <PLZ> <ORT>.";

    /** The initial text 2. */
    final String initialText2 = "Bitte tauschen Sie diese Eintrittskarte an der\nEingangskontrolle gegen einen Stimmbeleg.";

    /**
     * Initialize.
     */
    @FXML
    private void initialize() {

        fillVariables();

        taText1.setText(initialText1);
        taText2.setText(initialText2);

        btnPreview.setGraphic(new FontIcon(FontAwesomeSolid.ANGLE_DOUBLE_RIGHT));

        fillPreview();

        taText1.setOnKeyPressed(e -> {

            if ((e.isShiftDown() || e.isControlDown()) && e.getCode().equals(KeyCode.ENTER))
                fillPreview();

        });
    }

    /**
     * Select logo.
     *
     * @param event the event
     */
    @FXML
    private void selectLogo(ActionEvent event) {

        FileChooser fc = new FileChooser();
        fc.setInitialDirectory(new File("C:"));
        File f = fc.showOpenDialog(null);

        if (f != null)
            imageView.setImage(new Image(f.toURI().toString()));
    }

    /**
     * Delete logo.
     *
     * @param event the event
     */
    @FXML
    private void deleteLogo(ActionEvent event) {
        imageView.setImage(null);
    }

    /**
     * Prepare preview.
     *
     * @param event the event
     */
    @FXML
    private void preparePreview(ActionEvent event) {
        fillPreview();
    }

    /**
     * Close.
     *
     * @param event the event
     */
    @FXML
    private void close(ActionEvent event) {

        if (true) {
            System.out.println("Änderungen speichern?!");
            /*
             * Alert > showAndWait()
             */
        }
        eigeneStage.close();
    }

    /**
     * Save.
     *
     * @param event the event
     */
    @FXML
    private void save(ActionEvent event) {

        System.out.println("Speichern?!");

    }

    /**
     * Adds the variable.
     *
     * @param key         the key
     * @param bezeichnung the bezeichnung
     * @param value       the value
     */
    private void addVariable(String key, String bezeichnung, String value) {

        final String var = "<" + key + ">";
        final String bez = " = " + bezeichnung;

        map.put(var, value);

        variableBox.getChildren().add(new Label(var + "\t" + bez));

    }

    /**
     * Fill variables.
     */
    private void fillVariables() {

        addVariable("HV", "Art der Hauptversammlung", "ordentliche");
        addVariable("KD", "Kundenname", "Musterfirma");
        addVariable("WO", "Wochentag", LocalDate.now().getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.GERMANY));
        addVariable("DT", "Hauptversammlungsdatum", "01.07.2021");
        addVariable("BZ", "Beginn der Hauptversammlung", "10:00");
        addVariable("EZ", "Einlass der Hauptversammlung", "09:00");
        addVariable("VO", "Ort der Hauptversammlung", "Superhalle 3000");
        addVariable("STR", "Straße der Hauptversammlung", "Musterstraße 10");
        addVariable("PLZ", "PLZ der Hauptversammlung", "81379");
        addVariable("ORT", "Ort der Hauptversammlung", "München");

    }

    /**
     * Fill preview.
     */
    private void fillPreview() {

        if (taText1.getText() != null) {
            String previewText = taText1.getText();

            for (String key : map.keySet())
                previewText = previewText.replace(key, map.get(key));

            boolean length = false;

            for (String line : previewText.split("\n")) {
                System.out.println(line.length());
                length = !length ? line.length() > 46 : length;

            }

            taText1Preview.setText(previewText);

            if (length)
                System.out.println("Text zu lang");
            else
                System.out.println("Text passt!");

        }
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
