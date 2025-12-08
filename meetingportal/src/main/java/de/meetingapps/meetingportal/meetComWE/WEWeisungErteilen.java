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

import de.meetingapps.meetingportal.meetComEntities.EclWeisungMeldung;
import de.meetingapps.meetingportal.meetComEntities.EclWeisungMeldungRaw;
import de.meetingapps.meetingportal.meetComEntities.EclZugeordneteMeldungNeu;

public class WEWeisungErteilen extends WERoot  {

    /**Siehe KonstPortalAktion.HAUPT_*/
    private int ausgewaehlteHauptAktion = 0;

    /**Siehe KonstPortalAktion*/
    private int ausgewaehlteAktion = 0;

    /**Muß mit der AnmeldeAktionaersnummer gefüllt werden aus dem "aktuell bearbeiteten Aktionär"*/
    private String anmeldeAktionaersnummer = "";

    
    /**Muß gefüllt werden, wenn ausgewaehlteHauptaktion==HAUPT_BEREITSANGEMELDET ist*/
    private EclZugeordneteMeldungNeu eclZugeordneteMeldung = null;

    /************Alternative 1: zählt immer, wenn Alternative 2 == null ist. Soll langfristig nicht mehr verwendet werden, sondern Alternative 2!*****************/
    /*Enthalten in gleicher Anzahl und gleicher Reihenfolge wie in eclAbstimmungenListeM die erteilten Weisungen
     * in der Form "J" "N" "E" "U"=ungültig "T"=Nichtteilnahme "-"=nicht markiert "X"=Gegenantrag wird unterstützt 
     */
    private List<String> weisungenAgenda = null;
    private List<String> weisungenGegenantraege = null;

    /****************Alternative 2: zählt, wenn !=null; Soll langfristig verwendet werden anstelle Alternative 1******************************/
    /**Enthält fertig aufbereitete Weisungsabgabe*/
    private EclWeisungMeldung weisungMeldung = null;
    private EclWeisungMeldungRaw weisungMeldungRaw = null;


    /**Datei-Name aus Scan-Datei*/
    private String quelle = "";

    private int sammelIdent = -1;

    


    /*********************Standard Getter und Setter****************************************/


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

    public String getAnmeldeAktionaersnummer() {
        return anmeldeAktionaersnummer;
    }

    public void setAnmeldeAktionaersnummer(String anmeldeAktionaersnummer) {
        this.anmeldeAktionaersnummer = anmeldeAktionaersnummer;
    }

    public String getQuelle() {
        return quelle;
    }

    public void setQuelle(String quelle) {
        this.quelle = quelle;
    }

    public int getSammelIdent() {
        return sammelIdent;
    }

    public void setSammelIdent(int sammelIdent) {
        this.sammelIdent = sammelIdent;
    }

    public EclWeisungMeldung getWeisungMeldung() {
        return weisungMeldung;
    }

    public void setWeisungMeldung(EclWeisungMeldung weisungMeldung) {
        this.weisungMeldung = weisungMeldung;
    }

    public EclWeisungMeldungRaw getWeisungMeldungRaw() {
        return weisungMeldungRaw;
    }

    public void setWeisungMeldungRaw(EclWeisungMeldungRaw weisungMeldungRaw) {
        this.weisungMeldungRaw = weisungMeldungRaw;
    }

    public int getAusgewaehlteHauptAktion() {
        return ausgewaehlteHauptAktion;
    }

    public void setAusgewaehlteHauptAktion(int ausgewaehlteHauptAktion) {
        this.ausgewaehlteHauptAktion = ausgewaehlteHauptAktion;
    }

    public int getAusgewaehlteAktion() {
        return ausgewaehlteAktion;
    }

    public void setAusgewaehlteAktion(int ausgewaehlteAktion) {
        this.ausgewaehlteAktion = ausgewaehlteAktion;
    }

    public EclZugeordneteMeldungNeu getEclZugeordneteMeldung() {
        return eclZugeordneteMeldung;
    }

    public void setEclZugeordneteMeldung(EclZugeordneteMeldungNeu eclZugeordneteMeldung) {
        this.eclZugeordneteMeldung = eclZugeordneteMeldung;
    }

}
