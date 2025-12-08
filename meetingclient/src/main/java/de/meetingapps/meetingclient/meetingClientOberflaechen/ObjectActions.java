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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 */
public class ObjectActions {

    /**
     * Wenn ComboBox selektiert kann diese mit den Buchstaben A-Z gefiltert werden
     * <p>
     * Es kann auch noch eine Funktion hinterlegt werden welche bei drücken von
     * Enter ausgeführt wird.
     *
     * @param box      the box
     * @param enterBtn the enter btn
     */
    public static void filterComboBox(ComboBox<String> box, Button enterBtn) {

        box.setOnKeyPressed(e -> {
            ObservableList<String> filteredList = box.getItems();
            String key = getKey(e.getCode());

            if ((e.getCode() != KeyCode.UP && e.getCode() != KeyCode.DOWN) && !key.isBlank()) {

                Predicate<String> pred = i -> i.toLowerCase().startsWith(key);
                filteredList = filteredList.filtered(pred);

                if (!filteredList.isEmpty()) {
                    if (filteredList.contains(box.getValue())) {
                        int idx = filteredList.indexOf(box.getValue());

                        if (idx < filteredList.size() - 1) {

                            idx++;
                            box.setValue(filteredList.get(idx));
                        } else {
                            box.setValue(filteredList.get(0));
                        }
                    } else {
                        box.setValue(filteredList.get(0));
                    }
                }
            }
            if (e.getCode() == KeyCode.ENTER && enterBtn != null)
                enterBtn.fire();
            if (e.getCode() == KeyCode.DELETE)
                box.setValue(null);

        });
    }

    /**
     * Wenn ComboBox selektiert kann diese mit den Buchstaben A-Z gefiltert werden
     * <p>
     * Es kann auch noch eine Funktion hinterlegt werden welche bei drücken von
     * Enter ausgeführt wird
     * 
     * Extra für LoginNeu da CbElement.
     *
     * @param box      the box
     * @param enterBtn the enter btn
     */
    public static void filterComboBoxLoginNeu(ComboBox<CbElement> box, Button enterBtn) {

        box.setOnKeyPressed(e -> {
            ObservableList<CbElement> filteredList = FXCollections.observableList(
                    box.getItems().stream().filter(x -> x.anzeige.length() > 4).collect(Collectors.toList()));
            final String key = getKey(e.getCode());

            if ((e.getCode() != KeyCode.UP && e.getCode() != KeyCode.DOWN) && !key.isBlank()) {

                Predicate<CbElement> pred = i -> i.anzeige.toLowerCase().charAt(4) == key.charAt(0);
                filteredList = filteredList.filtered(pred);

                if (!filteredList.isEmpty()) {

                    if (filteredList.contains(box.getValue())) {
                        int idx = filteredList.indexOf(box.getValue());

                        if (idx < filteredList.size() - 1) {

                            idx++;
                            box.setValue(filteredList.get(idx));
                        } else {
                            box.setValue(filteredList.get(0));
                        }
                    } else {
                        box.setValue(filteredList.get(0));
                    }
                }
            }
            if (e.getCode() == KeyCode.ENTER && enterBtn != null)
                enterBtn.fire();
        });
    }

    private static String getKey(KeyCode code) {

        final String key = code.toString();

        if (code.isLetterKey())
            return key.toLowerCase();
        else if (key.contains("NUMPAD") || key.contains("DIGIT"))
            return key.substring(key.length() - 1);

        return "";
    }

    /**
     * Evaluate date.
     *
     * @param dp the dp
     */
    public static void evaluateDate(DatePicker dp) {
        //      Checkt ob Änderungen vorhanden und mit Feld kompatibel ohne den Listener werden Änderungen mit mit Enter oder per Maus gesetzt.
        dp.focusedProperty().addListener((o, oV, nV) -> {
            if (!nV) {
                if (dp.getEditor().getText().matches("(3[01]|[12][0-9]|0[1-9])\\.(1[012]|0[1-9])\\.(\\d{4})"))
                    dp.setValue(LocalDate.parse(dp.getEditor().getText(), DateTimeFormatter.ofPattern("dd.MM.yyyy")));
                else
                    dp.getEditor().setText(dp.getConverter().toString(dp.getValue()));
            }
        });

        dp.getEditor().textProperty().addListener((obs, oldText, newText) -> {
            if (!newText.matches("[0-9\\.]*") || newText.length() > 11)
                dp.getEditor().setText(oldText);
            if (newText.isBlank())
                dp.setValue(null);
        });
    }

    /**
     * Selektierte Combobox führt mit Enter die Funktion des zugewiesenen Buttons
     * aus.
     *
     * @param box the box
     * @param btn the btn
     */
    public static void cbEnterEvent(ComboBox<?> box, Button btn) {
        box.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER)
                btn.fire();
        });
    }

    /**
     * Selektiertes TextFeld führt mit Enter die Funktion des zugewiesenen Buttons
     * aus.
     *
     * @param tf the tf
     * @param btn the btn
     */
    public static void tfEnterEvent(TextField tf, Button btn) {
        tf.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER)
                btn.fire();
        });
    }

    /**
     * Button reagiert auch auf Enter Klick.
     *
     * @param btn the btn
     */
    public static void buttonEnterEvent(Button btn) {

        btn.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER)
                btn.fire();
        });
    }

    /**
     * Disable button.
     *
     * @param btn  the btn
     * @param time the time
     * @return the thread
     */
    public static Thread disableButton(Button btn, long time) {
        return new Thread() {
            public void run() {
                Platform.runLater(() -> btn.setDisable(true));
                try {
                    Thread.sleep(time); // 1 Sekunde
                } catch (InterruptedException ex) {
                    System.err.println(ex);
                }
                Platform.runLater(() -> btn.setDisable(false));
            }
        };
    }

    public static void tableResizePolicy(Stage stage, TableView<?> tableView, int width) {

        stage.widthProperty().addListener((o, oV, nV) -> {
            final int changeWidth = width;
            if (oV.intValue() >= changeWidth && nV.intValue() <= changeWidth) {
                tableView.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
            } else if (oV.intValue() <= changeWidth && nV.intValue() >= changeWidth) {
                tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
            }
        });
    }

    public static void styleSelectedButton(Button newBtn, Button oldBtn) {
        if (newBtn != oldBtn) {
            final String style = "-fx-background-color: #8bbaf9; -fx-background-radius: 3; -fx-background-insets: 1; -fx-border-color: #808080; -fx-border-radius: 3;";
            newBtn.setStyle(style);

            if (oldBtn != null)
                oldBtn.setStyle(null);
        }
    }

    public static Label createNotification(AnchorPane rootPane, double top, double right, double bottom, double left) {

        Label notification = new Label();

        notification.setPrefWidth(200);
        notification.setPrefHeight(60);
        notification.setAlignment(Pos.CENTER);
        notification.setOpacity(0);
        notification.setVisible(false);
        notification.setCacheShape(false);

        notification.setStyle(
                "-fx-border-color: black; -fx-border-radius: 5; -fx-background-color: #F0F0F0; -fx-background-radius: 5");

        rootPane.getChildren().add(notification);

        if (top > 0)
            AnchorPane.setTopAnchor(notification, top);
        if (right > 0)
            AnchorPane.setRightAnchor(notification, right);
        if (bottom > 0)
            AnchorPane.setBottomAnchor(notification, bottom);
        if (left > 0)
            AnchorPane.setLeftAnchor(notification, left);

        return notification;
    }

    public static void showNotification(Label lbl, String text) {

        final Timeline timeline = new Timeline();

        timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(0), e -> {
            lbl.setText(text);
            lbl.setVisible(true);
            lbl.setOpacity(1);
        }));
        timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(0.5), e -> lbl.setOpacity(0.9)));
        timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(1), e -> lbl.setOpacity(0.8)));
        timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(1.5), e -> lbl.setOpacity(0.7)));
        timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(2), e -> lbl.setOpacity(0.6)));
        timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(2.5), e -> lbl.setOpacity(0.5)));
        timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(3), e -> lbl.setOpacity(0.4)));
        timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(3.5), e -> lbl.setOpacity(0.3)));
        timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(4), e -> lbl.setOpacity(0.2)));
        timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(4.5), e -> lbl.setOpacity(0.1)));
        timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(5), e -> lbl.setOpacity(0)));
        timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(5.5), e -> lbl.setVisible(false)));

        Platform.runLater(timeline::play);
    }

    /**
     * 
     * @param rootPane
     * @param strings   
     * @param anchorTop
     * @param anchorRight
     */
    //    public static void createInfoButton(AnchorPane rootPane, String[] strings, Double anchorTop, Double anchorRight) {
    //        VBox box = new VBox(5);
    //        
    //        box.setVisible(false);
    //        box.setCacheShape(false);
    //        box.setPadding(new Insets(5));
    //        box.setAlignment(Pos.TOP_CENTER);
    //        
    //        final String style = "-fx-background-color: white; -fx-background-radius: 3; -fx-background-insets: 1; -fx-border-color: #808080; -fx-border-radius: 3;";
    //        box.setStyle(style);
    //        
    //        FontIcon icon = new FontIcon(FontAwesomeSolid.INFO_CIRCLE);
    //        icon.setIconColor(Paint.valueOf("#808080"));
    //        
    //        Label infoLabel = new Label("", icon);
    //        infoLabel.setStyle("-fx-font-size: 16px;");
    //        infoLabel.setPrefWidth(25);
    //        infoLabel.setAlignment(Pos.CENTER);
    //        infoLabel.setOnMouseEntered(e -> box.setVisible(true));
    //        infoLabel.setOnMouseExited(e -> box.setVisible(false));
    //        
    //        Label header = new Label("Shortcuts");
    //        header.setFont(Font.font("System", FontWeight.BOLD, 14));
    //        
    //        box.getChildren().add(header);
    //        
    //        for(String str : strings) {
    //            HBox hBox = new HBox(2);
    //            hBox.setAlignment(Pos.CENTER_LEFT);
    //            
    //            String[] split = str.split(" - ");
    //            
    //            Label key = new Label(split[0].trim());
    //            Label info = new Label("- " + split[1].trim());
    //            
    //            key.setPrefWidth(20);
    //            key.setAlignment(Pos.CENTER);
    //            key.setFont(Font.font("System", FontWeight.BOLD, 14));
    //            
    //            hBox.getChildren().addAll(key, info);
    //            box.getChildren().add(hBox);
    //        }
    //        
    //        rootPane.getChildren().add(infoLabel);
    //        AnchorPane.setTopAnchor(infoLabel, anchorTop);
    //        AnchorPane.setRightAnchor(infoLabel, anchorRight);
    //        
    //        rootPane.getChildren().add(box);
    //        AnchorPane.setTopAnchor(box, anchorTop);
    //        AnchorPane.setRightAnchor(box, anchorRight + 25.0);
    //    }

    public static void createInfoButton(AnchorPane rootPane, String[] strings, Double anchorTop, Double anchorRight) {

        VBox box = buildButton("Shortcuts", rootPane, anchorTop, anchorRight);

        for (String str : strings) {
            HBox hBox = new HBox(2);
            hBox.setAlignment(Pos.CENTER_LEFT);

            String[] split = str.split(" - ");

            Label key = new Label(split[0].trim());
            Label info = new Label("- " + split[1].trim());

            key.setAlignment(Pos.CENTER);
            key.setFont(Font.font("System", FontWeight.BOLD, 14));

            hBox.getChildren().addAll(key, info);
            box.getChildren().add(hBox);
        }
    }

    public static void createLegendeButton(AnchorPane rootPane, Map<String, String> map, Double anchorTop,
            Double anchorRight) {

        VBox box = buildButton("Legende", rootPane, anchorTop, anchorRight);
        
        for(var value : map.entrySet()) {
            
            HBox hBox = new HBox(2);
            hBox.setAlignment(Pos.CENTER_LEFT);
            
            FontIcon icon = new FontIcon(FontAwesomeSolid.CIRCLE);
            icon.setIconColor(Paint.valueOf(value.getKey()));

            Label key = new Label("", icon);
            Label info = new Label("- " + value.getValue());

            key.setPrefWidth(20);
            key.setAlignment(Pos.CENTER);
            key.setFont(Font.font("System", FontWeight.BOLD, 14));

            hBox.getChildren().addAll(key, info);
            box.getChildren().add(hBox);
            
        }
    }

    private static VBox buildButton(String title, AnchorPane rootPane, Double anchorTop, Double anchorRight) {

        VBox box = new VBox(5);

        box.setVisible(false);
        box.setCacheShape(false);
        box.setPadding(new Insets(5));
        box.setAlignment(Pos.TOP_CENTER);

        final String style = "-fx-background-color: white; -fx-background-radius: 3; -fx-background-insets: 1; -fx-border-color: #808080; -fx-border-radius: 3;";
        box.setStyle(style);

        FontIcon icon = new FontIcon(FontAwesomeSolid.INFO_CIRCLE);
        icon.setIconColor(Paint.valueOf("#808080"));

        Label infoLabel = new Label("", icon);
        infoLabel.setStyle("-fx-font-size: 16px;");
        infoLabel.setPrefWidth(25);
        infoLabel.setAlignment(Pos.CENTER);
        infoLabel.setOnMouseEntered(e -> box.setVisible(true));
        infoLabel.setOnMouseExited(e -> box.setVisible(false));

        Label header = new Label(title);
        header.setFont(Font.font("System", FontWeight.BOLD, 14));

        box.getChildren().add(header);

        rootPane.getChildren().add(infoLabel);
        AnchorPane.setTopAnchor(infoLabel, anchorTop);
        AnchorPane.setRightAnchor(infoLabel, anchorRight);

        rootPane.getChildren().add(box);
        AnchorPane.setTopAnchor(box, anchorTop);
        AnchorPane.setRightAnchor(box, anchorRight + 25.0);

        return box;

    }

}
