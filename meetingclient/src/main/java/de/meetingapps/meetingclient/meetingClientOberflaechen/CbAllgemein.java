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

import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;

/**
 * Funktionen für Combo-Box mit CbElement.
 */

public class CbAllgemein {

    /** The combo box. */
    private ComboBox<CbElement> comboBox = null;

    /**
     * Instantiates a new cb allgemein.
     *
     * @param pComboBox the combo box
     */
    public CbAllgemein(ComboBox<CbElement> pComboBox) {
        comboBox = pComboBox;

        StringConverter<CbElement> sc = new StringConverter<CbElement>() {

            @Override
            public String toString(CbElement pCbElement) {
                if (pCbElement == null) {
                    return "";
                }
                return pCbElement.anzeige;
            }

            @Override
            public CbElement fromString(String string) {
                return null;
            }
        };
        comboBox.setConverter(sc);
        //    	comboBox.getSelectionModel().clearSelection();
        comboBox.getItems().clear();

    }

    /**
     * Element zur Auswahlliste hinzufügen.
     *
     * @param pCbElement the cb element
     */
    public void addElement(CbElement pCbElement) {
        comboBox.getItems().add(pCbElement);
    }

    /**
     * Element zur Auswahlliste hinzufügen und gleichzeitig als ausgewählt
     * vermerken.
     *
     * @param pCbElement the cb element
     */
    public void addElementAusgewaehlt(CbElement pCbElement) {
        comboBox.getItems().add(pCbElement);
        comboBox.setValue(pCbElement);
    }

    /**
     * Setzt das bereits eingetragene Element mit ident1=pIdent1 auf ausgewählt.
     *
     * @param pIdent1 the new ausgewaehlt 1
     */
    public void setAusgewaehlt1(int pIdent1) {
        ObservableList<CbElement> elemente = comboBox.getItems();
        for (int i = 0; i < elemente.size(); i++) {
            if (elemente.get(i).ident1 == pIdent1) {
                comboBox.setValue(elemente.get(i));
            }
        }
    }

}
