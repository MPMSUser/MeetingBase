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

import javafx.scene.control.TextField;

/**
 * The Class MaxLengthTextField.
 */
public class MaxLengthTextField extends TextField {

    /** The max laenge. */
    private int maxLaenge = 0;

    /**
     * Instantiates a new max length text field.
     *
     * @param pMaxLaenge the max laenge
     */
    public MaxLengthTextField(int pMaxLaenge) {
        super();
        maxLaenge = pMaxLaenge;
    }

    /**
     * Replace text.
     *
     * @param start the start
     * @param end   the end
     * @param text  the text
     */
    @Override
    public void replaceText(int start, int end, String text) {

        if (getText().length() + text.length() <= maxLaenge || text.isEmpty() /*Ehemals: ==""*/) {
            super.replaceText(start, end, text);
        }
    }

    /**
     * Replace selection.
     *
     * @param text the text
     */
    @Override
    public void replaceSelection(String text) {
        if (getText().length() + text.length() <= maxLaenge || text.isEmpty() /*Ehemals: ==""*/ ) {
            super.replaceSelection(text);
        }
    }

}
