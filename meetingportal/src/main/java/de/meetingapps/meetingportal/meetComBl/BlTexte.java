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
package de.meetingapps.meetingportal.meetComBl;

import java.util.List;

import de.meetingapps.meetingportal.meetComAllg.CaString;

/**Low-Level-Funktionen für Texte für Mails, Anzeigen etc.*/
public class BlTexte {

    /**Ersetzt alle Variablen in der Liste in der Form {{{Test}}} durch die Werte in der Liste.
     * Variablen in Liste ohne {{{ }}}. Werte in der Liste fertig formatiert
     * 
     * pAusganstext bleibt unverändert.
     */
    public String ersetzeVariablenInText(String pAusgangsText, List<String> pVariablenListe, List<String> pWerteListe) {
        String ergebnisText=pAusgangsText;
        
        int pos = CaString.indexOf(ergebnisText, "{{{");
        while (pos != -1) {
            int posEnde = CaString.indexOf(ergebnisText, "}}}");
            if (posEnde == -1) {
                ergebnisText = "Fehler im Text: { { { ohne korrespondierende } } } ";
                return ergebnisText;
            }
            String variablenname = CaString.substring(ergebnisText, pos + 3, posEnde);
            String variablenInhaltergebnisText = liefereEinzelneVariable(variablenname, pVariablenListe, pWerteListe);
            ergebnisText = CaString.substring(ergebnisText, 0, pos) + variablenInhaltergebnisText
                    + CaString.substring(ergebnisText, posEnde + 3);
            pos = CaString.indexOf(ergebnisText, "{{{");
        }

        return ergebnisText;
    }
    
    private String liefereEinzelneVariable(String pVariablenname, List<String> pVariablenListe, List<String> pWerteListe) {
        int anzVariablen=pVariablenListe.size();
        int variablenNummer = -1;
        /*Variablennummer bestimmen*/
        for (int i = 0; i < anzVariablen; i++) {
            if (CaString.compareTo(pVariablenname, pVariablenListe.get(i)) == 0) {
                variablenNummer = i;
            }
        }
        if (variablenNummer == -1) {
            return "Fehler im Text: Variable " + pVariablenname + " nicht gefunden!";
        } else {
            /*Variablenwert bestimmen*/
            return pWerteListe.get(variablenNummer);
        }
    }

}
