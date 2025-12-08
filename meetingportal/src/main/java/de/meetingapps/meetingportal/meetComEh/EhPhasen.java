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
package de.meetingapps.meetingportal.meetComEh;

import java.io.Serializable;

/**Verwendung: uLogin Phasen-Pflegen*/
 public class EhPhasen  implements Serializable {
    private static final long serialVersionUID = 5396067352605939168L;
    
    public int phasenNr=0;
    public String phasenText="";
    public boolean manuellAktiv=false;
    public boolean manuellDeAktiv=false;
    public boolean gewinnspiel=false;
    public boolean hvInBetrieb=false;
    public boolean erstAnmeldungAktiv=false;
    public boolean ekAktiv=false;
    public boolean srvAktiv=false;
    public boolean briefwahlAktiv=false;
    public boolean kiavAktiv=false;
    public boolean vollmacht3Aktiv=false;
    public boolean streamAktiv=false;
    public boolean rueckfragenAktiv=false;
    public boolean fragenAktiv=false;
    public boolean wortmeldungAktiv=false;
    public boolean widerspruechAktiv=false;
    public boolean antraegeAktiv=false;
    public boolean sonstigeMitteilungAktiv=false;
    public boolean botschaftenEinreichenAktiv=false;
    public boolean botschaftenAktiv=false;

    public boolean chatAktiv=false;
    public boolean unterlagen1Aktiv=false;
    public boolean unterlagen2Aktiv=false;
    public boolean unterlagen3Aktiv=false;
    public boolean unterlagen4Aktiv=false;
    public boolean unterlagen5Aktiv=false;
    public boolean teilnehmerverzAktiv=false;
    public boolean abstimmungsergAktiv=false;
    
    /***********************Standard getter und setter*************************/
    
    public int getPhasenNr() {
        return phasenNr;
    }
    public void setPhasenNr(int phasenNr) {
        this.phasenNr = phasenNr;
    }
    public String getPhasenText() {
        return phasenText;
    }
    public void setPhasenText(String phasenText) {
        this.phasenText = phasenText;
    }
    public boolean isManuellAktiv() {
        return manuellAktiv;
    }
    public void setManuellAktiv(boolean manuellAktiv) {
        this.manuellAktiv = manuellAktiv;
    }
    public boolean isManuellDeAktiv() {
        return manuellDeAktiv;
    }
    public void setManuellDeAktiv(boolean manuellDeAktiv) {
        this.manuellDeAktiv = manuellDeAktiv;
    }
    public boolean isGewinnspiel() {
        return gewinnspiel;
    }
    public void setGewinnspiel(boolean gewinnspiel) {
        this.gewinnspiel = gewinnspiel;
    }
    public boolean isHvInBetrieb() {
        return hvInBetrieb;
    }
    public void setHvInBetrieb(boolean hvInBetrieb) {
        this.hvInBetrieb = hvInBetrieb;
    }
    public boolean isErstAnmeldungAktiv() {
        return erstAnmeldungAktiv;
    }
    public void setErstAnmeldungAktiv(boolean erstAnmeldungAktiv) {
        this.erstAnmeldungAktiv = erstAnmeldungAktiv;
    }
    public boolean isEkAktiv() {
        return ekAktiv;
    }
    public void setEkAktiv(boolean ekAktiv) {
        this.ekAktiv = ekAktiv;
    }
    public boolean isSrvAktiv() {
        return srvAktiv;
    }
    public void setSrvAktiv(boolean srvAktiv) {
        this.srvAktiv = srvAktiv;
    }
    public boolean isBriefwahlAktiv() {
        return briefwahlAktiv;
    }
    public void setBriefwahlAktiv(boolean briefwahlAktiv) {
        this.briefwahlAktiv = briefwahlAktiv;
    }
    public boolean isKiavAktiv() {
        return kiavAktiv;
    }
    public void setKiavAktiv(boolean kiavAktiv) {
        this.kiavAktiv = kiavAktiv;
    }
    public boolean isVollmacht3Aktiv() {
        return vollmacht3Aktiv;
    }
    public void setVollmacht3Aktiv(boolean vollmacht3Aktiv) {
        this.vollmacht3Aktiv = vollmacht3Aktiv;
    }
    public boolean isStreamAktiv() {
        return streamAktiv;
    }
    public void setStreamAktiv(boolean streamAktiv) {
        this.streamAktiv = streamAktiv;
    }
    public boolean isFragenAktiv() {
        return fragenAktiv;
    }
    public void setFragenAktiv(boolean fragenAktiv) {
        this.fragenAktiv = fragenAktiv;
    }
    public boolean isWortmeldungAktiv() {
        return wortmeldungAktiv;
    }
    public void setWortmeldungAktiv(boolean wortmeldungAktiv) {
        this.wortmeldungAktiv = wortmeldungAktiv;
    }
    public boolean isWiderspruechAktiv() {
        return widerspruechAktiv;
    }
    public void setWiderspruechAktiv(boolean widerspruechAktiv) {
        this.widerspruechAktiv = widerspruechAktiv;
    }
    public boolean isAntraegeAktiv() {
        return antraegeAktiv;
    }
    public void setAntraegeAktiv(boolean antraegeAktiv) {
        this.antraegeAktiv = antraegeAktiv;
    }
    public boolean isSonstigeMitteilungAktiv() {
        return sonstigeMitteilungAktiv;
    }
    public void setSonstigeMitteilungAktiv(boolean sonstigeMitteilungAktiv) {
        this.sonstigeMitteilungAktiv = sonstigeMitteilungAktiv;
    }
    public boolean isChatAktiv() {
        return chatAktiv;
    }
    public void setChatAktiv(boolean chatAktiv) {
        this.chatAktiv = chatAktiv;
    }
    public boolean isUnterlagen1Aktiv() {
        return unterlagen1Aktiv;
    }
    public void setUnterlagen1Aktiv(boolean unterlagen1Aktiv) {
        this.unterlagen1Aktiv = unterlagen1Aktiv;
    }
    public boolean isUnterlagen2Aktiv() {
        return unterlagen2Aktiv;
    }
    public void setUnterlagen2Aktiv(boolean unterlagen2Aktiv) {
        this.unterlagen2Aktiv = unterlagen2Aktiv;
    }
    public boolean isUnterlagen3Aktiv() {
        return unterlagen3Aktiv;
    }
    public void setUnterlagen3Aktiv(boolean unterlagen3Aktiv) {
        this.unterlagen3Aktiv = unterlagen3Aktiv;
    }
    public boolean isUnterlagen4Aktiv() {
        return unterlagen4Aktiv;
    }
    public void setUnterlagen4Aktiv(boolean unterlagen4Aktiv) {
        this.unterlagen4Aktiv = unterlagen4Aktiv;
    }
    public boolean isUnterlagen5Aktiv() {
        return unterlagen5Aktiv;
    }
    public void setUnterlagen5Aktiv(boolean unterlagen5Aktiv) {
        this.unterlagen5Aktiv = unterlagen5Aktiv;
    }
    public boolean isTeilnehmerverzAktiv() {
        return teilnehmerverzAktiv;
    }
    public void setTeilnehmerverzAktiv(boolean teilnehmerverzAktiv) {
        this.teilnehmerverzAktiv = teilnehmerverzAktiv;
    }
    public boolean isAbstimmungsergAktiv() {
        return abstimmungsergAktiv;
    }
    public void setAbstimmungsergAktiv(boolean abstimmungsergAktiv) {
        this.abstimmungsergAktiv = abstimmungsergAktiv;
    }
    public boolean isBotschaftenEinreichenAktiv() {
        return botschaftenEinreichenAktiv;
    }
    public void setBotschaftenEinreichenAktiv(boolean botschaftenEinreichenAktiv) {
        this.botschaftenEinreichenAktiv = botschaftenEinreichenAktiv;
    }
    public boolean isBotschaftenAktiv() {
        return botschaftenAktiv;
    }
    public void setBotschaftenAktiv(boolean botschaftenAktiv) {
        this.botschaftenAktiv = botschaftenAktiv;
    }
    public boolean isRueckfragenAktiv() {
        return rueckfragenAktiv;
    }
    public void setRueckfragenAktiv(boolean rueckfragenAktiv) {
        this.rueckfragenAktiv = rueckfragenAktiv;
    }

    
    
    
}
