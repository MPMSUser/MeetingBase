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
package de.meetingapps.meetingportal.meetComEntities;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class EclWillenserklaerungStatusTextElement implements Serializable {
    private static final long serialVersionUID = 6625985197939077722L;

    /**Aufbau des Anzeigetextes:
     * Wenn textNr!=0: Text von textNr, kein Zeilenumbruch, kein Blank, dann textInhalt, Zeilenumbruch
     * textListe jeweils in separater Zeile, kein Zeilenumbruch kein Blank vorher.
     * Zeilenumbruch hinterher;
     * 
     * Text-Nr. siehe in KonstWillenserklaerungPortalTexte
     */
    public int textNr = 0;
    public String textInhalt = "";
    public String textInhaltEN = "";
    public List<String> textListe = new LinkedList<>();
    public List<String> textListeEN = new LinkedList<>();

    /*******************Standard getter und setter**********************************/
    public int getTextNr() {
        return textNr;
    }

    public void setTextNr(int textNr) {
        this.textNr = textNr;
    }

    public List<String> getTextListe() {
        return textListe;
    }

    public void setTextListe(List<String> textListe) {
        this.textListe = textListe;
    }

    public List<String> getTextListeEN() {
        return textListeEN;
    }

    public void setTextListeEN(List<String> textListeEN) {
        this.textListeEN = textListeEN;
    }

    public String getTextInhalt() {
        return textInhalt;
    }

    public void setTextInhalt(String textInhalt) {
        this.textInhalt = textInhalt;
    }

    public String getTextInhaltEN() {
        return textInhaltEN;
    }

    public void setTextInhaltEN(String textInhaltEN) {
        this.textInhaltEN = textInhaltEN;
    }

}
