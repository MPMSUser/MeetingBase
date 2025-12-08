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

import java.util.Arrays;
import java.util.List;

import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.layout.VBox;

/**
 * Implementiert eine mehrzeilige TableCellFactory für JAVAFX TableView. Trennt
 * den Wert an "#" auf mehrere Zeilen auf. Als Wert nur String zulässig.
 *
 * @param <S> the generic type
 * @param <T> the generic type
 */
public class TableCellMehrzeilig<S, T> extends TableCell<S, T> {

    /**
     * Update item.
     *
     * @param item  the item
     * @param empty the empty
     */
    @Override
    protected void updateItem(T item, boolean empty) {
        super.updateItem(item, empty);
        if (item == null || empty) {
            setGraphic(null);
        } else {
            VBox vbox = new VBox();
            List<String> textList = Arrays.asList(item.toString().split("#"));
            for (int i = 0; i < textList.size(); i++) {
                Label lbl = new Label(textList.get(i));
                vbox.getChildren().add(lbl);
            }
            setGraphic(vbox);
        }
    }

}
