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
package de.meetingapps.meetingportal.meetingport;

import java.io.Serializable;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

@SessionScoped
@Named
@Deprecated
public class AControllerPasswortVergessenSession implements Serializable {
    private static final long serialVersionUID = -1072228811792773733L;

    private String loginKennung = "";

    private String emailAdresse = "";

    private String strasse = "";
    private String ort = "";

    private String passwort = "";

    private String passwortBestaetigung = "";
    private String bestaetigungsCode = "";

    /**Für Ablaufvariante 2 - Abfrage des Geburtsdatums als Identifizierung*/
    private String geburtsdatum = "";

    /**Falls sowohl bei E-Mail-Registrierung als auch bei Nicht-Email-Registrierung der (halb)automatische Weg
     * angeboten wird (also Eingabe von Email, oder Eingabe von Straße / Ort), dann muß eine Auswahl für den
     * Aktionär angeboten werden => auswahlAnbieten=true;
     */
    private boolean auswahlAnbieten = false;

    /**Falls rein manuelles Verfahren, dann keinerlei Eingabe ermöglichen.*/
    private boolean eingabeAnbieten = false;

    /**Falls auswahlAnbieten=true, dann wird über dieses Feld gesteuert welche der beiden Varianten (E-Mail oder Straße/Ort)
     * vom Aktionär ausgewählt wurde.
     * 1=Email-Adresse
     * 2=Strasse / Ort
     */
    private String eingabeSelektiert = "0";

    /**1 = Text 231, "nichts einzugeben"
     * 2 = Text 208. "nur Email eingeben"
     * 3 = Text 233, "nur Anschrift einzugeben"
     * 4 = Text 232, ""Auswahl möglich"
     */
    private int textVariante = 0;

    /**1 = Email verschickt, Text 238
     * 2 = Post wird verschickt, Text 239
     * 3 = User ist für das Portal gesperrt
     */
    private int textVarianteQuittung = 0;

    /**Standardwert False. Wird bei Anforderung über App auf true gesetzt - dann wird nur Code verschickt, nicht Link*/
    private boolean wurdeUeberAppAngefordert = false;

    /*Fehlermeldung für aPwZurueck.xhtml*/
    private String fehlerMeldung = "";
    private int fehlerNr = 0;

    public void clearFehlermeldung() {
        fehlerMeldung = "";
        fehlerNr = 0;
    }

    public boolean pruefeFehlerMeldungVorhanden() {
        if (fehlerMeldung.isEmpty()) {
            return false;
        }
        return true;
    }

    public void clearDlgVariablen() {
        this.loginKennung = "";
        this.emailAdresse = "";
        this.strasse = "";
        this.ort = "";

    }

    /*************Standard setter/getter********************************************/

    public String getLoginKennung() {
        return loginKennung;
    }

    public void setLoginKennung(String loginKennung) {
        this.loginKennung = loginKennung;
    }

    public String getEmailAdresse() {
        return emailAdresse;
    }

    public void setEmailAdresse(String emailAdresse) {
        this.emailAdresse = emailAdresse;
    }

    public String getPasswort() {
        return passwort;
    }

    public void setPasswort(String passwort) {
        this.passwort = passwort;
    }

    public String getPasswortBestaetigung() {
        return passwortBestaetigung;
    }

    public void setPasswortBestaetigung(String passwortBestaetigung) {
        this.passwortBestaetigung = passwortBestaetigung;
    }

    public String getBestaetigungsCode() {
        return bestaetigungsCode;
    }

    public void setBestaetigungsCode(String bestaetigungsCode) {
        this.bestaetigungsCode = bestaetigungsCode;
    }

    public String getFehlerMeldung() {
        return fehlerMeldung;
    }

    public void setFehlerMeldung(String fehlerMeldung) {
        this.fehlerMeldung = fehlerMeldung;
    }

    public int getFehlerNr() {
        return fehlerNr;
    }

    public void setFehlerNr(int fehlerNr) {
        this.fehlerNr = fehlerNr;
    }

    public String getStrasse() {
        return strasse;
    }

    public void setStrasse(String strasse) {
        this.strasse = strasse;
    }

    public String getOrt() {
        return ort;
    }

    public void setOrt(String ort) {
        this.ort = ort;
    }

    public boolean isAuswahlAnbieten() {
        return auswahlAnbieten;
    }

    public void setAuswahlAnbieten(boolean auswahlAnbieten) {
        this.auswahlAnbieten = auswahlAnbieten;
    }

    public boolean isEingabeAnbieten() {
        return eingabeAnbieten;
    }

    public void setEingabeAnbieten(boolean eingabeAnbieten) {
        this.eingabeAnbieten = eingabeAnbieten;
    }

    public String getEingabeSelektiert() {
        return eingabeSelektiert;
    }

    public void setEingabeSelektiert(String eingabeSelektiert) {
        this.eingabeSelektiert = eingabeSelektiert;
    }

    public int getTextVariante() {
        return textVariante;
    }

    public void setTextVariante(int textVariante) {
        this.textVariante = textVariante;
    }

    public boolean isWurdeUeberAppAngefordert() {
        return wurdeUeberAppAngefordert;
    }

    public void setWurdeUeberAppAngefordert(boolean wurdeUeberAppAngefordert) {
        this.wurdeUeberAppAngefordert = wurdeUeberAppAngefordert;
    }

    public int getTextVarianteQuittung() {
        return textVarianteQuittung;
    }

    public void setTextVarianteQuittung(int textVarianteQuittung) {
        this.textVarianteQuittung = textVarianteQuittung;
    }

    public String getGeburtsdatum() {
        return geburtsdatum;
    }

    public void setGeburtsdatum(String geburtsdatum) {
        this.geburtsdatum = geburtsdatum;
    }

}
