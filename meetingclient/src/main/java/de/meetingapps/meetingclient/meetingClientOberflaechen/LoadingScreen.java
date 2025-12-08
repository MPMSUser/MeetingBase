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

import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class LoadingScreen {

    public static StackPane createLoadingScreen(Pane rootPane) {

        final StackPane content = new StackPane();
        final StackPane blend = new StackPane();
        blend.setStyle("-fx-background-color: #e8e9ea");
        blend.setOpacity(0.5);

        final ProgressIndicator progress = new ProgressIndicator();
        progress.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS);

        content.getChildren().setAll(blend, progress);
        content.setVisible(false);
        
        content.prefWidthProperty().bind(rootPane.widthProperty());
        content.prefHeightProperty().bind(rootPane.heightProperty());

        progress.maxWidthProperty().bind(rootPane.widthProperty().divide(5));
        progress.maxHeightProperty().bind(rootPane.heightProperty().divide(5));
        rootPane.getChildren().add(content);

        return content;
    }

}
