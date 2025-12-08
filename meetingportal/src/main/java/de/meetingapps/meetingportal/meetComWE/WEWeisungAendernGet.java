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
import de.meetingapps.meetingportal.meetComEntities.EclWillenserklaerungStatusNeu;
import de.meetingapps.meetingportal.meetComEntities.EclZugeordneteMeldungNeu;

public class WEWeisungAendernGet {

    private WELoginVerify weLoginVerify = null;

    /**Siehe KonstPortalAktion.HAUPT_*/
    public int ausgewaehlteHauptAktion = 0;

    /**Siehe KonstPortalAktion*/
    public int ausgewaehlteAktion = 0;

    /**Muß gefüllt werden, wenn ausgewaehlteHauptaktion==HAUPT_BEREITSANGEMELDET ist*/
    public EclZugeordneteMeldungNeu eclZugeordneteMeldung = null;
    public EclWillenserklaerungStatusNeu eclWillenserklaerungStatus = null;

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

    
    public int meldungsIdent = 0;
    public int willenserklaerungIdent = 0;
    public int sammelIdent = 0;
    public int weisungsIdent = 0;


    /*********************Standard Getter und Setter****************************************/

    public WELoginVerify getWeLoginVerify() {
        return weLoginVerify;
    }

    public void setWeLoginVerify(WELoginVerify weLoginVerify) {
        this.weLoginVerify = weLoginVerify;
    }

    public int getMeldungsIdent() {
        return meldungsIdent;
    }

    public void setMeldungsIdent(int meldungsIdent) {
        this.meldungsIdent = meldungsIdent;
    }

    public int getWillenserklaerungIdent() {
        return willenserklaerungIdent;
    }

    public void setWillenserklaerungIdent(int willenserklaerungIdent) {
        this.willenserklaerungIdent = willenserklaerungIdent;
    }

    public int getSammelIdent() {
        return sammelIdent;
    }

    public void setSammelIdent(int sammelIdent) {
        this.sammelIdent = sammelIdent;
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

    public int getWeisungsIdent() {
        return weisungsIdent;
    }

    public void setWeisungsIdent(int weisungsIdent) {
        this.weisungsIdent = weisungsIdent;
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

}
