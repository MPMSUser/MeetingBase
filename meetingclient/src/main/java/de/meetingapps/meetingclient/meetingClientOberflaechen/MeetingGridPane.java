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

import javafx.beans.InvalidationListener;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

/**
 * Verwaltet eine GridPane mit Zusatzfunktionen.
 */
public class MeetingGridPane extends GridPane {

    /** The vertikaler abstand. */
    private int vertikalerAbstand = 1;

    /** The horizontaler abstand. */
    private int horizontalerAbstand = 5;

    /** The border oben. */
    private boolean borderOben = true;

    /** The border rechts. */
    private boolean borderRechts = true;

    /** The border unten. */
    private boolean borderUnten = true;

    /** The border links. */
    private boolean borderLinks = true;

    /**
     * Instantiates a new meeting grid pane.
     */
    public MeetingGridPane() {
        super();
    }

    /**
     * Instantiates a new Meeting grid pane.
     *
     * @param pVertikalerAbstand   the vertikaler abstand
     * @param pHorizontalerAbstand the horizontaler abstand
     * @param pBorderOben          the border oben
     * @param pBorderRechts        the border rechts
     * @param pBorderUnten         the border unten
     * @param pBorderLinks         the border links
     */
    public MeetingGridPane(int pVertikalerAbstand, int pHorizontalerAbstand, boolean pBorderOben, boolean pBorderRechts,
            boolean pBorderUnten, boolean pBorderLinks) {
        super();
        vertikalerAbstand = pVertikalerAbstand;
        horizontalerAbstand = pHorizontalerAbstand;
        borderOben = pBorderOben;
        borderRechts = pBorderRechts;
        borderUnten = pBorderUnten;
        borderLinks = pBorderLinks;
    }

    /**
     * Adds the Meeting.
     *
     * @param child  the child
     * @param spalte the spalte
     * @param zeile  the zeile
     */
    public void addMeeting(Node child, int spalte, int zeile) {
        HBox lHBox = new HBox();
        lHBox.setPadding(new Insets(vertikalerAbstand, horizontalerAbstand, vertikalerAbstand, horizontalerAbstand));
        String styleString = "-fx-border-style: solid;";
        styleString += "-fx-border-width: ";
        if (borderOben) {
            styleString += " 1";
        } else {
            styleString += " 0";
        }
        if (borderRechts) {
            styleString += " 1";
        } else {
            styleString += " 0";
        }
        if (borderUnten) {
            styleString += " 1";
        } else {
            styleString += " 0";
        }
        if (borderLinks) {
            styleString += " 1";
        } else {
            styleString += " 0";
        }
        styleString += ";";
        lHBox.setStyle(styleString);
        lHBox.getChildren().add(child);
        this.add(lHBox, spalte, zeile);
    }

    /**
     * Darf erst nach dem Eintragen aller Spalten aufgerufen werden! Setzt
     * automatisch auch den Inhalt der gridPane in pScrollPane.
     *
     * @param ueberschriftSpalten the ueberschrift spalten
     * @param pScrollPane         the scroll pane
     */
    public void setzeUeberschrift(String[] ueberschriftSpalten, ScrollPane pScrollPane) {
        int anzUeberschriftSpalten = ueberschriftSpalten.length;
        Node[] ueberschriftNodes = new Node[anzUeberschriftSpalten];
        for (int i = 0; i < anzUeberschriftSpalten; i++) {

            Label hLabel = new Label(ueberschriftSpalten[i]);
            hLabel.setStyle("-fx-font-weight: bold;");

            HBox lHBox = new HBox();
            lHBox.setPadding(
                    new Insets(vertikalerAbstand, horizontalerAbstand, vertikalerAbstand, horizontalerAbstand));
            lHBox.setStyle("-fx-border-style: solid; -fx-background-color: white;");
            lHBox.getChildren().add(hLabel);

            ueberschriftNodes[i] = lHBox;
        }

        this.addRow(0, ueberschriftNodes);

        pScrollPane.setContent(this);

        // keep header in position on top of the viewport
        InvalidationListener headerUpdater = o -> {
            final double ty = (this.getHeight() - pScrollPane.getViewportBounds().getHeight())
                    * pScrollPane.getVvalue();
            for (Node header : ueberschriftNodes) {
                header.setTranslateY(ty);
            }
        };
        this.heightProperty().addListener(headerUpdater);
        pScrollPane.viewportBoundsProperty().addListener(headerUpdater);
        pScrollPane.vvalueProperty().addListener(headerUpdater);

    }

}
