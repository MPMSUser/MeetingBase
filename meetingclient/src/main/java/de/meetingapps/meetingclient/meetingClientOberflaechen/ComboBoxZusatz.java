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

import java.util.List;

import de.meetingapps.meetingportal.meetComEntities.EclIsin;
import de.meetingapps.meetingportal.meetComEntities.EclMailing;
import de.meetingapps.meetingportal.meetComEntities.EclStaaten;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.util.StringConverter;

/**
 * The Class ComboBoxZusatz.
 */
public class ComboBoxZusatz {

    /**
     * Sperre.
     *
     * @param pComboBox the combo box
     */
    public static void sperre(ComboBox<String> pComboBox) {
        String hString = pComboBox.getValue();
        pComboBox.getItems().clear();
        pComboBox.getItems().addAll(hString);
        pComboBox.setValue(hString);

    }

    /**
     * Sperre cb element.
     *
     * @param pComboBox the combo box
     */
    public static void sperreCbElement(ComboBox<CbElement> pComboBox) {
        CbElement hString = pComboBox.getValue();
        pComboBox.getItems().clear();
        pComboBox.getItems().addAll(hString);
        pComboBox.setValue(hString);

    }

    /**
     * Convert isin.
     *
     * @param cb the cb
     * @return the string converter
     */
    public static StringConverter<EclIsin> convertIsin(ComboBox<EclIsin> cb) {

        StringConverter<EclIsin> converter = new StringConverter<>() {

            @Override
            public String toString(EclIsin object) {
                return object == null ? null : object.isin;
            }

            @Override
            public EclIsin fromString(String string) {
                return cb.getItems().stream().filter(e -> e.isin.equals(string)).findFirst().orElse(null);
            }
        };
        return converter;
    }

    /**
     * Convert staaten.
     *
     * @param cb the cb
     * @return the string converter
     */
    public static StringConverter<EclStaaten> convertStaaten(ComboBox<EclStaaten> cb) {

        StringConverter<EclStaaten> converter = new StringConverter<>() {

            @Override
            public String toString(EclStaaten object) {
                return object == null ? null : object.getNameDE();
            }

            @Override
            public EclStaaten fromString(String string) {
                return cb.getItems().stream().filter(e -> e.getNameDE().equals(string)).findFirst().orElse(null);
            }
        };
        return converter;
    }

    public static StringConverter<CbElement> convertCbElement(ComboBox<CbElement> cb) {

        StringConverter<CbElement> converter = new StringConverter<>() {

            @Override
            public String toString(CbElement object) {
                return object == null ? null : object.anzeige;
            }

            @Override
            public CbElement fromString(String string) {
                return cb.getItems().stream().filter(e -> e.anzeige.equals(string)).findFirst().orElse(null);
            }
        };
        return converter;
    }

    /**
     * Configure staaten box.
     *
     * @param box     the box
     * @param staaten the staaten
     */
    public static void configureStaatenBox(ComboBox<EclStaaten> box, List<EclStaaten> staaten) {

        ObservableList<EclStaaten> list = FXCollections.observableArrayList(staaten);
        FilteredList<EclStaaten> fList = new FilteredList<>(list, p -> true);
        box.setOnKeyPressed(e -> {
            if (e.getCode().equals(KeyCode.ESCAPE)) {
                box.setValue(null);
                box.getEditor().clear();
            }
        });

        box.getEditor().setOnKeyPressed(e -> {
            if (e.getCode().equals(KeyCode.DOWN))
                box.show();
        });

        box.getEditor().textProperty().addListener((obs, oldValue, newValue) -> {
            final TextField editor = box.getEditor();
            final EclStaaten selected = box.getSelectionModel().getSelectedItem();

            if (selected == null || !selected.getNameDE().equals(editor.getText())) {
                fList.setPredicate(item -> {
                    return item.getNameDE().toLowerCase().startsWith(newValue.toLowerCase()) ? true : false;
                });
                box.getEditor().setText(newValue);
                box.show();
            }

            box.getEditor().setText(newValue);
        });

        if (box.isEditable()) {
            box.focusedProperty().addListener((o, oV, nV) -> {
                if (nV)
                    Platform.runLater(() -> box.getEditor().selectAll());
            });
        }

        box.setItems(fList);
        box.setConverter(convertStaaten(box));
    }

    public static void configureCbElement(ComboBox<CbElement> box) {

        box.setEditable(true);

        ObservableList<CbElement> list = box.getItems();
        FilteredList<CbElement> fList = new FilteredList<>(list, p -> true);
        box.setOnKeyPressed(e -> {
            if (e.getCode().equals(KeyCode.ESCAPE))
                box.getEditor().clear();
        });

        box.getEditor().setOnKeyPressed(e -> {
            if (e.getCode().equals(KeyCode.DOWN))
                box.show();
        });

        box.getEditor().textProperty().addListener((obs, oldValue, newValue) -> {
            final TextField editor = box.getEditor();
            final CbElement selected = box.getSelectionModel().getSelectedItem();

            if (selected == null || !selected.anzeige.equals(editor.getText())) {
                fList.setPredicate(item -> {
                    return item.anzeige.toLowerCase().contains(newValue.toLowerCase()) ? true : false;
                });
                box.getEditor().setText(newValue);
                box.show();
            }
            box.getEditor().setText(newValue);
        });

        if (box.isEditable()) {
            box.focusedProperty().addListener((o, oV, nV) -> {
                if (nV)
                    Platform.runLater(() -> box.getEditor().selectAll());
            });
        }

        box.setItems(fList);
        box.setConverter(convertCbElement(box));
    }

    public static StringConverter<EclMailing> convertMailing(ComboBox<EclMailing> cb) {

        StringConverter<EclMailing> converter = new StringConverter<>() {

            @Override
            public String toString(EclMailing object) {
                return object == null ? null : object.name;
            }

            @Override
            public EclMailing fromString(String string) {
                return cb.getItems().stream().filter(e -> e.name.equals(string)).findFirst().orElse(null);
            }
        };
        return converter;
    }
}