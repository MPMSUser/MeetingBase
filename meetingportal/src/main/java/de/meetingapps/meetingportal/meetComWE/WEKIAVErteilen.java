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
package de.meetingapps.meetingportal.meetComWE;

import java.util.List;

import de.meetingapps.meetingportal.meetComEclM.EclKIAVM;
import de.meetingapps.meetingportal.meetComEclM.EclZugeordneteMeldungM;

public class WEKIAVErteilen {

    private WELoginVerify weLoginVerify = null;

    /*Beschreibungen siehe ADlgVariablen*/
    /**1=Neuanmeldung; 2=zusätzliche EK zu bestehender Anmeldung*/
    private String ausgewaehlteHauptAktion = "";

    /****************************Ausgewählte Aktionärt******************************************************
     * Gibt an, welche "Detail"-Aktion gerade ausgewählt ist und ausgeführt ist.
     * 7 = nur Vollmacht an KI/AV
     * 8 = Vollmacht und Weisung (dediziert) an KI/AV
     * 9 = Vollmacht und Weisung gemäß Vorschlag an KI/AV
     * */
    private String ausgewaehlteAktion = "";

    private String anmeldeAktionaersnummer = "";
    private EclZugeordneteMeldungM zugeordneteMeldungM = null;
    private EclKIAVM kiavM = null;

    /*TODO #9 Weisung gemäß Abstimmungsvorschlag*/
    /**Achtung - muß immer gefüllt werden:
     * auch bei ausgewahelteAktion=7 (dann ist der Inhalt egal, muß aber die erforderliche Anzahl von Strings enthalten).
     * Auch bei ausgewaehlteAktion=9 muß der String RICHTIG gefüllt werden (d.h. die Weisungen aus dem Abstimmungsvorschlag der
     * entsprechenden KIAV müssen übernommen werden). Ja, dies ist nicht schön, aber auf die schnelle ....
     */
    private List<String> weisungenAgenda = null;
    private List<String> weisungenGegenantraege = null;

    /*********************Standard Getter und Setter****************************************/

    public WELoginVerify getWeLoginVerify() {
        return weLoginVerify;
    }

    public void setWeLoginVerify(WELoginVerify weLoginVerify) {
        this.weLoginVerify = weLoginVerify;
    }

    public String getAusgewaehlteHauptAktion() {
        return ausgewaehlteHauptAktion;
    }

    public void setAusgewaehlteHauptAktion(String ausgewaehlteHauptAktion) {
        this.ausgewaehlteHauptAktion = ausgewaehlteHauptAktion;
    }

    public String getAusgewaehlteAktion() {
        return ausgewaehlteAktion;
    }

    public void setAusgewaehlteAktion(String ausgewaehlteAktion) {
        this.ausgewaehlteAktion = ausgewaehlteAktion;
    }

    public String getAnmeldeAktionaersnummer() {
        return anmeldeAktionaersnummer;
    }

    public void setAnmeldeAktionaersnummer(String anmeldeAktionaersnummer) {
        this.anmeldeAktionaersnummer = anmeldeAktionaersnummer;
    }

    public EclZugeordneteMeldungM getZugeordneteMeldungM() {
        return zugeordneteMeldungM;
    }

    public void setZugeordneteMeldungM(EclZugeordneteMeldungM zugeordneteMeldungM) {
        this.zugeordneteMeldungM = zugeordneteMeldungM;
    }

    public List<String> getWeisungenAgenda() {
        return weisungenAgenda;
    }

    public void setWeisungenAgenda(List<String> weisungenAgenda) {
        this.weisungenAgenda = weisungenAgenda;
    }

    public List<String> getWeisungenGegenantraege() {
        return weisungenGegenantraege;
    }

    public void setWeisungenGegenantraege(List<String> weisungenGegenantraege) {
        this.weisungenGegenantraege = weisungenGegenantraege;
    }

    public EclKIAVM getKiavM() {
        return kiavM;
    }

    public void setKiavM(EclKIAVM kiavM) {
        this.kiavM = kiavM;
    }

}
