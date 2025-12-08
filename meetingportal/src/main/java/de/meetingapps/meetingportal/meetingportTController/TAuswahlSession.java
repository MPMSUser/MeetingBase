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
package de.meetingapps.meetingportal.meetingportTController;

import java.io.Serializable;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

@SessionScoped
@Named
public class TAuswahlSession implements Serializable {
    private static final long serialVersionUID = 5774499306758795865L;

    private String nachricht = "";
    private String kennung = "";

    
    /**Generell gilt: 
     * AngebotenUB = Angeboten, unabhängig von der Berechtigung des eingeloggten Users
     * Berechtigt = Der eingeloggte User ist berechtigt die Funktion zu nutzen (unabhängig ob Angeboten)
     * Angeboten = Die Funktion wird angeboten, und der User ist auch berechtigt sie zu nutzen
     * 
     * Hinweis: wenn AngebotenUB==false, dann ist auch Berechtigt immer false (unabhängig ob tatsächlich berechtigt) 
     * 
     */

    private boolean streamAngebotenUB = false;
    private boolean streamBerechtigt = false;
    private boolean streamAngeboten = false;
    private boolean streamAktivUB = false;
    private boolean streamAktiv = false;

    private boolean fragenAngebotenUB = false;
    private boolean fragenBerechtigt = false;
    private boolean fragenAngeboten = false;
    private boolean fragenAktivUB = false;
    private boolean fragenAktiv = false;

    /**Nur belegt, wenn Rückfragen möglich!*/
    private boolean fragenWurdenGestellt=false;
    
    private boolean rueckfragenAngebotenUB = false;
    /**Spezialfall: tatsächlich berechtigt, d.h. dieser Teilnehmer hat Fragen gestellt 
     * und ist deshalb auch berechtigt, Rückfragen zu stellen, oder wurde explizit
     * freigeschaltet
     */
    private boolean rueckfragenBerechtigt = false;
    /**Spezialfall: ist gründsätzlich berechtigt. D.h. true, wenn dieser Teilnehmer Rückfragen stellen darf,
     * noch unabhängig davon ob er welche Gestellt hat oder nicht*/
    private boolean rueckfragenBerechtigtWennGestellt=false;
    private boolean rueckfragenAngeboten = false;
    private boolean rueckfragenAktivUB = false;
    private boolean rueckfragenAktiv = false;

    private boolean wortmeldungenAngebotenUB = false;
    private boolean wortmeldungenBerechtigt = false;
    private boolean wortmeldungenAngeboten = false;
    private boolean wortmeldungenAktivUB = false;
    private boolean wortmeldungenAktiv = false;

    private boolean widerspruecheAngebotenUB = false;
    private boolean widerspruecheBerechtigt = false;
    private boolean widerspruecheAngeboten = false;
    private boolean widerspruecheAktivUB = false;
    private boolean widerspruecheAktiv = false;

    private boolean antraegeAngebotenUB = false;
    private boolean antraegeBerechtigt = false;
    private boolean antraegeAngeboten = false;
    private boolean antraegeAktivUB = false;
    private boolean antraegeAktiv = false;

    private boolean sonstigeMitteilungenAngebotenUB = false;
    private boolean sonstigeMitteilungenBerechtigt = false;
    private boolean sonstigeMitteilungenAngeboten = false;
    private boolean sonstigeMitteilungenAktivUB = false;
    private boolean sonstigeMitteilungenAktiv = false;

    private boolean chatAngebotenUB = false;
    private boolean chatBerechtigt = false;
    private boolean chatAngeboten = false;
    private boolean chatAktivUB = false;
    private boolean chatAktiv = false;

    private boolean chatInstiAngebotenUB = false;
    private boolean chatInstiBerechtigt = false;
    private boolean chatInstiAngeboten = false;
    private boolean chatInstiAktivUB = false;
    private boolean chatInstiAktiv = false;

    private boolean chatVIPAngebotenUB = false;
    private boolean chatVIPBerechtigt = false;
    private boolean chatVIPAngeboten = false;
    private boolean chatVIPAktivUB = false;
    private boolean chatVIPAktiv = false;

    /**unterlagenGruppe1 bis 5 mit oder verknüpft*/
    private boolean unterlagenAngebotenUB = false;
    private boolean unterlagenBerechtigt = false;
    private boolean unterlagenAngeboten = false;
    private boolean unterlagenAktivUB = false;
    private boolean unterlagenAktiv = false;

    private boolean unterlagenGruppe1AngebotenUB = false;
    private boolean unterlagenGruppe1Berechtigt = false;
    private boolean unterlagenGruppe1Angeboten = false;
    private boolean unterlagenGruppe1AktivUB = false;
    private boolean unterlagenGruppe1Aktiv = false;

    private boolean unterlagenGruppe2AngebotenUB = false;
    private boolean unterlagenGruppe2Berechtigt = false;
    private boolean unterlagenGruppe2Angeboten = false;
    private boolean unterlagenGruppe2AktivUB = false;
    private boolean unterlagenGruppe2Aktiv = false;

    private boolean unterlagenGruppe3AngebotenUB = false;
    private boolean unterlagenGruppe3Berechtigt = false;
    private boolean unterlagenGruppe3Angeboten = false;
    private boolean unterlagenGruppe3AktivUB = false;
    private boolean unterlagenGruppe3Aktiv = false;

    private boolean unterlagenGruppe4AngebotenUB = false;
    private boolean unterlagenGruppe4Berechtigt = false;
    private boolean unterlagenGruppe4Angeboten = false;
    private boolean unterlagenGruppe4AktivUB = false;
    private boolean unterlagenGruppe4Aktiv = false;

    private boolean unterlagenGruppe5AngebotenUB = false;
    private boolean unterlagenGruppe5Berechtigt = false;
    private boolean unterlagenGruppe5Angeboten = false;
    private boolean unterlagenGruppe5AktivUB = false;
    private boolean unterlagenGruppe5Aktiv = false;

    /*++++++++++++++++++Unterlagen++++++++++++++++++++++++*/
    public boolean unterlagenAngeboten() {
        if (unterlagenGruppe1Angeboten || unterlagenGruppe2Angeboten || unterlagenGruppe3Angeboten
                || unterlagenGruppe4Angeboten || unterlagenGruppe5Angeboten) {
            return true;
        }
        return false;
    }

    public boolean unterlagenAktiv() {
        if (unterlagenGruppe1Aktiv || unterlagenGruppe2Aktiv || unterlagenGruppe3Aktiv || unterlagenGruppe4Aktiv
                || unterlagenGruppe5Aktiv) {
            return true;
        }
        return false;
    }

//    
//    /**zu Botschaften: hier wird auf die Unterlagengruppen als Basis
//     * zurückgegriffen, aber eben dann verknüpft mit "Botschaften"
//     * grundsätzlich aktiv/angeboten ...
//     */
//    /**botschaftenGruppe1 bis 5 mit oder verknüpft*/
//    private boolean botschaftenAngebotenUB = false;
//    private boolean botschaftenBerechtigt = false;
//    private boolean botschaftenAngeboten = false;
//    private boolean botschaftenAktivUB = false;
//    private boolean botschaftenAktiv = false;
//
//    private boolean botschaftenGruppe1AngebotenUB = false;
//    private boolean botschaftenGruppe1Berechtigt = false;
//    private boolean botschaftenGruppe1Angeboten = false;
//    private boolean botschaftenGruppe1AktivUB = false;
//    private boolean botschaftenGruppe1Aktiv = false;
//
//    private boolean botschaftenGruppe2AngebotenUB = false;
//    private boolean botschaftenGruppe2Berechtigt = false;
//    private boolean botschaftenGruppe2Angeboten = false;
//    private boolean botschaftenGruppe2AktivUB = false;
//    private boolean botschaftenGruppe2Aktiv = false;
//
//    private boolean botschaftenGruppe3AngebotenUB = false;
//    private boolean botschaftenGruppe3Berechtigt = false;
//    private boolean botschaftenGruppe3Angeboten = false;
//    private boolean botschaftenGruppe3AktivUB = false;
//    private boolean botschaftenGruppe3Aktiv = false;
//
//    private boolean botschaftenGruppe4AngebotenUB = false;
//    private boolean botschaftenGruppe4Berechtigt = false;
//    private boolean botschaftenGruppe4Angeboten = false;
//    private boolean botschaftenGruppe4AktivUB = false;
//    private boolean botschaftenGruppe4Aktiv = false;
//
//    private boolean botschaftenGruppe5AngebotenUB = false;
//    private boolean botschaftenGruppe5Berechtigt = false;
//    private boolean botschaftenGruppe5Angeboten = false;
//    private boolean botschaftenGruppe5AktivUB = false;
//    private boolean botschaftenGruppe5Aktiv = false;
//
//    /*++++++++++++++++++botschaften++++++++++++++++++++++++*/
//    public boolean botschaftenAngeboten() {
//        if (botschaftenGruppe1Angeboten || botschaftenGruppe2Angeboten || botschaftenGruppe3Angeboten
//                || botschaftenGruppe4Angeboten || botschaftenGruppe5Angeboten) {
//            return true;
//        }
//        return false;
//    }
//
//    public boolean botschaftenAktiv() {
//        if (botschaftenGruppe1Aktiv || botschaftenGruppe2Aktiv || botschaftenGruppe3Aktiv || botschaftenGruppe4Aktiv
//                || botschaftenGruppe5Aktiv) {
//            return true;
//        }
//        return false;
//    }


    
    private boolean teilnehmerverzeichnisAngebotenUB = false;
    private boolean teilnehmerverzeichnisBerechtigt = false;
    private boolean teilnehmerverzeichnisAngeboten = false;
    private boolean teilnehmerverzeichnisAktivUB = false;
    private boolean teilnehmerverzeichnisAktiv = false;

    private boolean abstimmungsergebnisseAngebotenUB = false;
    private boolean abstimmungsergebnisseBerechtigt = false;
    private boolean abstimmungsergebnisseAngeboten = false;
    private boolean abstimmungsergebnisseAktivUB = false;
    private boolean abstimmungsergebnisseAktiv = false;

    private boolean onlineteilnahmeAngebotenUB = false;
    private boolean onlineteilnahmeBerechtigt = false;
    private boolean onlineteilnahmeAngeboten = false;
    private boolean onlineteilnahmeAktivUB = false;
    private boolean onlineteilnahmeAktiv = false;

    private boolean empfangFragenAngebotenUB = false;
    private boolean empfangFragenBerechtigt = false;
    private boolean empfangFragenAngeboten = false;
    private boolean empfangFragenAktivUB = false;
    private boolean empfangFragenAktiv = false;

    private boolean empfangWortmeldungenAngebotenUB = false;
    private boolean empfangWortmeldungenBerechtigt = false;
    private boolean empfangWortmeldungenAngeboten = false;
    private boolean empfangWortmeldungenAktivUB = false;
    private boolean empfangWortmeldungenAktiv = false;

    private boolean empfangWiderspruchAngebotenUB = false;
    private boolean empfangWiderspruchBerechtigt = false;
    private boolean empfangWiderspruchAngeboten = false;
    private boolean empfangWiderspruchAktivUB = false;
    private boolean empfangWiderspruchAktiv = false;

    private boolean empfangAntraegeAngebotenUB = false;
    private boolean empfangAntraegeBerechtigt = false;
    private boolean empfangAntraegeAngeboten = false;
    private boolean empfangAntraegeAktivUB = false;
    private boolean empfangAntraegeAktiv = false;

    private boolean empfangSonstigeMitteilungenAngebotenUB = false;
    private boolean empfangSonstigeMitteilungenBerechtigt = false;
    private boolean empfangSonstigeMitteilungenAngeboten = false;
    private boolean empfangSonstigeMitteilungenAktivUB = false;
    private boolean empfangSonstigeMitteilungenAktiv = false;

    private boolean botschaftenEinreichenAngebotenUB = false;
    private boolean botschaftenEinreichenBerechtigt = false;
    /**Sonderform: 
     * wenn "Botschaft nur nach Anmeldung", dann wird das nur dann auf true
     * gesetzt, wenn explizit diese Kennung berechtigt.
     * 
     * Ansonsten: immer true
     */
    private boolean botschaftenEinreichenTatsaechlichBerechtigt=false;
    private boolean botschaftenEinreichenAngeboten = false;
    private boolean botschaftenEinreichenAktivUB = false;
    private boolean botschaftenEinreichenAktiv = false;

    private boolean botschaftenAnzeigeAngebotenUB = false;
    private boolean botschaftenAnzeigeBerechtigt = false;
    private boolean botschaftenAnzeigeAngeboten = false;
    private boolean botschaftenAnzeigeAktivUB = false;
    private boolean botschaftenAnzeigeAktiv = false;

    private boolean monitoringAngebotenUB = false;
    private boolean monitoringBerechtigt = false;
    private boolean monitoringAngeboten = false;
    private boolean monitoringAktivUB = false;
    private boolean monitoringAktiv = false;

    
    /**Liefert true, wenn irgendeine der HV-Funktionen für den Aktionär angeboten
     * wird (und dementsprechend ein Button in der Auswahl angezeigt wird)
     */
    public boolean hvFunktionWirdAngeboten() {
       return
               streamAktiv ||
               fragenAktiv ||
               rueckfragenAktiv ||
               wortmeldungenAktiv ||
               widerspruecheAktiv ||
               antraegeAktiv ||
               sonstigeMitteilungenAktiv ||
               chatAktiv ||
               chatInstiAktiv ||
               chatVIPAktiv ||
               unterlagenGruppe1Aktiv ||
               unterlagenGruppe2Aktiv ||
               unterlagenGruppe3Aktiv ||
               unterlagenGruppe4Aktiv ||
               unterlagenGruppe5Aktiv ||
               teilnehmerverzeichnisAktiv ||
               abstimmungsergebnisseAktiv ||
               onlineteilnahmeAktiv ||
               botschaftenEinreichenAktiv ||
               botschaftenAnzeigeAktiv
               ;
               
    }
    
    /********************Standard getter und setter**************************************************/

    public boolean isStreamAngebotenUB() {
        return streamAngebotenUB;
    }

    public void setStreamAngebotenUB(boolean streamAngebotenUB) {
        this.streamAngebotenUB = streamAngebotenUB;
    }

    public boolean isStreamBerechtigt() {
        return streamBerechtigt;
    }

    public void setStreamBerechtigt(boolean streamBerechtigt) {
        this.streamBerechtigt = streamBerechtigt;
    }

    public boolean isStreamAngeboten() {
        return streamAngeboten;
    }

    public void setStreamAngeboten(boolean streamAngeboten) {
        this.streamAngeboten = streamAngeboten;
    }

    public boolean isFragenAngebotenUB() {
        return fragenAngebotenUB;
    }

    public void setFragenAngebotenUB(boolean fragenAngebotenUB) {
        this.fragenAngebotenUB = fragenAngebotenUB;
    }

    public boolean isFragenBerechtigt() {
        return fragenBerechtigt;
    }

    public void setFragenBerechtigt(boolean fragenBerechtigt) {
        this.fragenBerechtigt = fragenBerechtigt;
    }

    public boolean isFragenAngeboten() {
        return fragenAngeboten;
    }

    public void setFragenAngeboten(boolean fragenAngeboten) {
        this.fragenAngeboten = fragenAngeboten;
    }

    public boolean isWortmeldungenAngebotenUB() {
        return wortmeldungenAngebotenUB;
    }

    public void setWortmeldungenAngebotenUB(boolean wortmeldungenAngebotenUB) {
        this.wortmeldungenAngebotenUB = wortmeldungenAngebotenUB;
    }

    public boolean isWortmeldungenBerechtigt() {
        return wortmeldungenBerechtigt;
    }

    public void setWortmeldungenBerechtigt(boolean wortmeldungenBerechtigt) {
        this.wortmeldungenBerechtigt = wortmeldungenBerechtigt;
    }

    public boolean isWortmeldungenAngeboten() {
        return wortmeldungenAngeboten;
    }

    public void setWortmeldungenAngeboten(boolean wortmeldungenAngeboten) {
        this.wortmeldungenAngeboten = wortmeldungenAngeboten;
    }

    public boolean isWiderspruecheAngebotenUB() {
        return widerspruecheAngebotenUB;
    }

    public void setWiderspruecheAngebotenUB(boolean widerspruecheAngebotenUB) {
        this.widerspruecheAngebotenUB = widerspruecheAngebotenUB;
    }

    public boolean isWiderspruecheBerechtigt() {
        return widerspruecheBerechtigt;
    }

    public void setWiderspruecheBerechtigt(boolean widerspruecheBerechtigt) {
        this.widerspruecheBerechtigt = widerspruecheBerechtigt;
    }

    public boolean isWiderspruecheAngeboten() {
        return widerspruecheAngeboten;
    }

    public void setWiderspruecheAngeboten(boolean widerspruecheAngeboten) {
        this.widerspruecheAngeboten = widerspruecheAngeboten;
    }

    public boolean isAntraegeAngebotenUB() {
        return antraegeAngebotenUB;
    }

    public void setAntraegeAngebotenUB(boolean antraegeAngebotenUB) {
        this.antraegeAngebotenUB = antraegeAngebotenUB;
    }

    public boolean isAntraegeBerechtigt() {
        return antraegeBerechtigt;
    }

    public void setAntraegeBerechtigt(boolean antraegeBerechtigt) {
        this.antraegeBerechtigt = antraegeBerechtigt;
    }

    public boolean isAntraegeAngeboten() {
        return antraegeAngeboten;
    }

    public void setAntraegeAngeboten(boolean antraegeAngeboten) {
        this.antraegeAngeboten = antraegeAngeboten;
    }

    public boolean isSonstigeMitteilungenAngebotenUB() {
        return sonstigeMitteilungenAngebotenUB;
    }

    public void setSonstigeMitteilungenAngebotenUB(boolean sonstigeMitteilungenAngebotenUB) {
        this.sonstigeMitteilungenAngebotenUB = sonstigeMitteilungenAngebotenUB;
    }

    public boolean isSonstigeMitteilungenBerechtigt() {
        return sonstigeMitteilungenBerechtigt;
    }

    public void setSonstigeMitteilungenBerechtigt(boolean sonstigeMitteilungenBerechtigt) {
        this.sonstigeMitteilungenBerechtigt = sonstigeMitteilungenBerechtigt;
    }

    public boolean isSonstigeMitteilungenAngeboten() {
        return sonstigeMitteilungenAngeboten;
    }

    public void setSonstigeMitteilungenAngeboten(boolean sonstigeMitteilungenAngeboten) {
        this.sonstigeMitteilungenAngeboten = sonstigeMitteilungenAngeboten;
    }

    public boolean isChatAngebotenUB() {
        return chatAngebotenUB;
    }

    public void setChatAngebotenUB(boolean chatAngebotenUB) {
        this.chatAngebotenUB = chatAngebotenUB;
    }

    public boolean isChatBerechtigt() {
        return chatBerechtigt;
    }

    public void setChatBerechtigt(boolean chatBerechtigt) {
        this.chatBerechtigt = chatBerechtigt;
    }

    public boolean isChatAngeboten() {
        return chatAngeboten;
    }

    public void setChatAngeboten(boolean chatAngeboten) {
        this.chatAngeboten = chatAngeboten;
    }

    public boolean isChatInstiAngebotenUB() {
        return chatInstiAngebotenUB;
    }

    public void setChatInstiAngebotenUB(boolean chatInstiAngebotenUB) {
        this.chatInstiAngebotenUB = chatInstiAngebotenUB;
    }

    public boolean isChatInstiBerechtigt() {
        return chatInstiBerechtigt;
    }

    public void setChatInstiBerechtigt(boolean chatInstiBerechtigt) {
        this.chatInstiBerechtigt = chatInstiBerechtigt;
    }

    public boolean isChatInstiAngeboten() {
        return chatInstiAngeboten;
    }

    public void setChatInstiAngeboten(boolean chatInstiAngeboten) {
        this.chatInstiAngeboten = chatInstiAngeboten;
    }

    public boolean isChatVIPAngebotenUB() {
        return chatVIPAngebotenUB;
    }

    public void setChatVIPAngebotenUB(boolean chatVIPAngebotenUB) {
        this.chatVIPAngebotenUB = chatVIPAngebotenUB;
    }

    public boolean isChatVIPBerechtigt() {
        return chatVIPBerechtigt;
    }

    public void setChatVIPBerechtigt(boolean chatVIPBerechtigt) {
        this.chatVIPBerechtigt = chatVIPBerechtigt;
    }

    public boolean isChatVIPAngeboten() {
        return chatVIPAngeboten;
    }

    public void setChatVIPAngeboten(boolean chatVIPAngeboten) {
        this.chatVIPAngeboten = chatVIPAngeboten;
    }

    public boolean isUnterlagenGruppe1AngebotenUB() {
        return unterlagenGruppe1AngebotenUB;
    }

    public void setUnterlagenGruppe1AngebotenUB(boolean unterlagenGruppe1AngebotenUB) {
        this.unterlagenGruppe1AngebotenUB = unterlagenGruppe1AngebotenUB;
    }

    public boolean isUnterlagenGruppe1Berechtigt() {
        return unterlagenGruppe1Berechtigt;
    }

    public void setUnterlagenGruppe1Berechtigt(boolean unterlagenGruppe1Berechtigt) {
        this.unterlagenGruppe1Berechtigt = unterlagenGruppe1Berechtigt;
    }

    public boolean isUnterlagenGruppe1Angeboten() {
        return unterlagenGruppe1Angeboten;
    }

    public void setUnterlagenGruppe1Angeboten(boolean unterlagenGruppe1Angeboten) {
        this.unterlagenGruppe1Angeboten = unterlagenGruppe1Angeboten;
    }

    public boolean isUnterlagenGruppe2AngebotenUB() {
        return unterlagenGruppe2AngebotenUB;
    }

    public void setUnterlagenGruppe2AngebotenUB(boolean unterlagenGruppe2AngebotenUB) {
        this.unterlagenGruppe2AngebotenUB = unterlagenGruppe2AngebotenUB;
    }

    public boolean isUnterlagenGruppe2Berechtigt() {
        return unterlagenGruppe2Berechtigt;
    }

    public void setUnterlagenGruppe2Berechtigt(boolean unterlagenGruppe2Berechtigt) {
        this.unterlagenGruppe2Berechtigt = unterlagenGruppe2Berechtigt;
    }

    public boolean isUnterlagenGruppe2Angeboten() {
        return unterlagenGruppe2Angeboten;
    }

    public void setUnterlagenGruppe2Angeboten(boolean unterlagenGruppe2Angeboten) {
        this.unterlagenGruppe2Angeboten = unterlagenGruppe2Angeboten;
    }

    public boolean isUnterlagenGruppe3AngebotenUB() {
        return unterlagenGruppe3AngebotenUB;
    }

    public void setUnterlagenGruppe3AngebotenUB(boolean unterlagenGruppe3AngebotenUB) {
        this.unterlagenGruppe3AngebotenUB = unterlagenGruppe3AngebotenUB;
    }

    public boolean isUnterlagenGruppe3Berechtigt() {
        return unterlagenGruppe3Berechtigt;
    }

    public void setUnterlagenGruppe3Berechtigt(boolean unterlagenGruppe3Berechtigt) {
        this.unterlagenGruppe3Berechtigt = unterlagenGruppe3Berechtigt;
    }

    public boolean isUnterlagenGruppe3Angeboten() {
        return unterlagenGruppe3Angeboten;
    }

    public void setUnterlagenGruppe3Angeboten(boolean unterlagenGruppe3Angeboten) {
        this.unterlagenGruppe3Angeboten = unterlagenGruppe3Angeboten;
    }

    public boolean isUnterlagenGruppe4AngebotenUB() {
        return unterlagenGruppe4AngebotenUB;
    }

    public void setUnterlagenGruppe4AngebotenUB(boolean unterlagenGruppe4AngebotenUB) {
        this.unterlagenGruppe4AngebotenUB = unterlagenGruppe4AngebotenUB;
    }

    public boolean isUnterlagenGruppe4Berechtigt() {
        return unterlagenGruppe4Berechtigt;
    }

    public void setUnterlagenGruppe4Berechtigt(boolean unterlagenGruppe4Berechtigt) {
        this.unterlagenGruppe4Berechtigt = unterlagenGruppe4Berechtigt;
    }

    public boolean isUnterlagenGruppe4Angeboten() {
        return unterlagenGruppe4Angeboten;
    }

    public void setUnterlagenGruppe4Angeboten(boolean unterlagenGruppe4Angeboten) {
        this.unterlagenGruppe4Angeboten = unterlagenGruppe4Angeboten;
    }

    public boolean isUnterlagenGruppe5AngebotenUB() {
        return unterlagenGruppe5AngebotenUB;
    }

    public void setUnterlagenGruppe5AngebotenUB(boolean unterlagenGruppe5AngebotenUB) {
        this.unterlagenGruppe5AngebotenUB = unterlagenGruppe5AngebotenUB;
    }

    public boolean isUnterlagenGruppe5Berechtigt() {
        return unterlagenGruppe5Berechtigt;
    }

    public void setUnterlagenGruppe5Berechtigt(boolean unterlagenGruppe5Berechtigt) {
        this.unterlagenGruppe5Berechtigt = unterlagenGruppe5Berechtigt;
    }

    public boolean isUnterlagenGruppe5Angeboten() {
        return unterlagenGruppe5Angeboten;
    }

    public void setUnterlagenGruppe5Angeboten(boolean unterlagenGruppe5Angeboten) {
        this.unterlagenGruppe5Angeboten = unterlagenGruppe5Angeboten;
    }

    public boolean isTeilnehmerverzeichnisAngebotenUB() {
        return teilnehmerverzeichnisAngebotenUB;
    }

    public void setTeilnehmerverzeichnisAngebotenUB(boolean teilnehmerverzeichnisAngebotenUB) {
        this.teilnehmerverzeichnisAngebotenUB = teilnehmerverzeichnisAngebotenUB;
    }

    public boolean isTeilnehmerverzeichnisBerechtigt() {
        return teilnehmerverzeichnisBerechtigt;
    }

    public void setTeilnehmerverzeichnisBerechtigt(boolean teilnehmerverzeichnisBerechtigt) {
        this.teilnehmerverzeichnisBerechtigt = teilnehmerverzeichnisBerechtigt;
    }

    public boolean isTeilnehmerverzeichnisAngeboten() {
        return teilnehmerverzeichnisAngeboten;
    }

    public void setTeilnehmerverzeichnisAngeboten(boolean teilnehmerverzeichnisAngeboten) {
        this.teilnehmerverzeichnisAngeboten = teilnehmerverzeichnisAngeboten;
    }

    public boolean isAbstimmungsergebnisseAngebotenUB() {
        return abstimmungsergebnisseAngebotenUB;
    }

    public void setAbstimmungsergebnisseAngebotenUB(boolean abstimmungsergebnisseAngebotenUB) {
        this.abstimmungsergebnisseAngebotenUB = abstimmungsergebnisseAngebotenUB;
    }

    public boolean isAbstimmungsergebnisseBerechtigt() {
        return abstimmungsergebnisseBerechtigt;
    }

    public void setAbstimmungsergebnisseBerechtigt(boolean abstimmungsergebnisseBerechtigt) {
        this.abstimmungsergebnisseBerechtigt = abstimmungsergebnisseBerechtigt;
    }

    public boolean isAbstimmungsergebnisseAngeboten() {
        return abstimmungsergebnisseAngeboten;
    }

    public void setAbstimmungsergebnisseAngeboten(boolean abstimmungsergebnisseAngeboten) {
        this.abstimmungsergebnisseAngeboten = abstimmungsergebnisseAngeboten;
    }

    public boolean isEmpfangFragenAngebotenUB() {
        return empfangFragenAngebotenUB;
    }

    public void setEmpfangFragenAngebotenUB(boolean empfangFragenAngebotenUB) {
        this.empfangFragenAngebotenUB = empfangFragenAngebotenUB;
    }

    public boolean isEmpfangFragenBerechtigt() {
        return empfangFragenBerechtigt;
    }

    public void setEmpfangFragenBerechtigt(boolean empfangFragenBerechtigt) {
        this.empfangFragenBerechtigt = empfangFragenBerechtigt;
    }

    public boolean isEmpfangFragenAngeboten() {
        return empfangFragenAngeboten;
    }

    public void setEmpfangFragenAngeboten(boolean empfangFragenAngeboten) {
        this.empfangFragenAngeboten = empfangFragenAngeboten;
    }

    public boolean isEmpfangWortmeldungenAngebotenUB() {
        return empfangWortmeldungenAngebotenUB;
    }

    public void setEmpfangWortmeldungenAngebotenUB(boolean empfangWortmeldungenAngebotenUB) {
        this.empfangWortmeldungenAngebotenUB = empfangWortmeldungenAngebotenUB;
    }

    public boolean isEmpfangWortmeldungenBerechtigt() {
        return empfangWortmeldungenBerechtigt;
    }

    public void setEmpfangWortmeldungenBerechtigt(boolean empfangWortmeldungenBerechtigt) {
        this.empfangWortmeldungenBerechtigt = empfangWortmeldungenBerechtigt;
    }

    public boolean isEmpfangWortmeldungenAngeboten() {
        return empfangWortmeldungenAngeboten;
    }

    public void setEmpfangWortmeldungenAngeboten(boolean empfangWortmeldungenAngeboten) {
        this.empfangWortmeldungenAngeboten = empfangWortmeldungenAngeboten;
    }

    public boolean isEmpfangWiderspruchAngebotenUB() {
        return empfangWiderspruchAngebotenUB;
    }

    public void setEmpfangWiderspruchAngebotenUB(boolean empfangWiderspruchAngebotenUB) {
        this.empfangWiderspruchAngebotenUB = empfangWiderspruchAngebotenUB;
    }

    public boolean isEmpfangWiderspruchBerechtigt() {
        return empfangWiderspruchBerechtigt;
    }

    public void setEmpfangWiderspruchBerechtigt(boolean empfangWiderspruchBerechtigt) {
        this.empfangWiderspruchBerechtigt = empfangWiderspruchBerechtigt;
    }

    public boolean isEmpfangWiderspruchAngeboten() {
        return empfangWiderspruchAngeboten;
    }

    public void setEmpfangWiderspruchAngeboten(boolean empfangWiderspruchAngeboten) {
        this.empfangWiderspruchAngeboten = empfangWiderspruchAngeboten;
    }

    public boolean isEmpfangAntraegeAngebotenUB() {
        return empfangAntraegeAngebotenUB;
    }

    public void setEmpfangAntraegeAngebotenUB(boolean empfangAntraegeAngebotenUB) {
        this.empfangAntraegeAngebotenUB = empfangAntraegeAngebotenUB;
    }

    public boolean isEmpfangAntraegeBerechtigt() {
        return empfangAntraegeBerechtigt;
    }

    public void setEmpfangAntraegeBerechtigt(boolean empfangAntraegeBerechtigt) {
        this.empfangAntraegeBerechtigt = empfangAntraegeBerechtigt;
    }

    public boolean isEmpfangAntraegeAngeboten() {
        return empfangAntraegeAngeboten;
    }

    public void setEmpfangAntraegeAngeboten(boolean empfangAntraegeAngeboten) {
        this.empfangAntraegeAngeboten = empfangAntraegeAngeboten;
    }

    public boolean isEmpfangSonstigeMitteilungenAngebotenUB() {
        return empfangSonstigeMitteilungenAngebotenUB;
    }

    public void setEmpfangSonstigeMitteilungenAngebotenUB(boolean empfangSonstigeMitteilungenAngebotenUB) {
        this.empfangSonstigeMitteilungenAngebotenUB = empfangSonstigeMitteilungenAngebotenUB;
    }

    public boolean isEmpfangSonstigeMitteilungenBerechtigt() {
        return empfangSonstigeMitteilungenBerechtigt;
    }

    public void setEmpfangSonstigeMitteilungenBerechtigt(boolean empfangSonstigeMitteilungenBerechtigt) {
        this.empfangSonstigeMitteilungenBerechtigt = empfangSonstigeMitteilungenBerechtigt;
    }

    public boolean isEmpfangSonstigeMitteilungenAngeboten() {
        return empfangSonstigeMitteilungenAngeboten;
    }

    public void setEmpfangSonstigeMitteilungenAngeboten(boolean empfangSonstigeMitteilungenAngeboten) {
        this.empfangSonstigeMitteilungenAngeboten = empfangSonstigeMitteilungenAngeboten;
    }

    public boolean isMonitoringAngebotenUB() {
        return monitoringAngebotenUB;
    }

    public void setMonitoringAngebotenUB(boolean monitoringAngebotenUB) {
        this.monitoringAngebotenUB = monitoringAngebotenUB;
    }

    public boolean isMonitoringBerechtigt() {
        return monitoringBerechtigt;
    }

    public void setMonitoringBerechtigt(boolean monitoringBerechtigt) {
        this.monitoringBerechtigt = monitoringBerechtigt;
    }

    public boolean isMonitoringAngeboten() {
        return monitoringAngeboten;
    }

    public void setMonitoringAngeboten(boolean monitoringAngeboten) {
        this.monitoringAngeboten = monitoringAngeboten;
    }

    public boolean isStreamAktivUB() {
        return streamAktivUB;
    }

    public void setStreamAktivUB(boolean streamAktivUB) {
        this.streamAktivUB = streamAktivUB;
    }

    public boolean isStreamAktiv() {
        return streamAktiv;
    }

    public void setStreamAktiv(boolean streamAktiv) {
   		this.streamAktiv = streamAktiv;
    }

    public boolean isFragenAktivUB() {
        return fragenAktivUB;
    }

    public void setFragenAktivUB(boolean fragenAktivUB) {
        this.fragenAktivUB = fragenAktivUB;
    }

    public boolean isFragenAktiv() {
        return fragenAktiv;
    }

    public void setFragenAktiv(boolean fragenAktiv) {
        this.fragenAktiv = fragenAktiv;
    }

    public boolean isWortmeldungenAktivUB() {
        return wortmeldungenAktivUB;
    }

    public void setWortmeldungenAktivUB(boolean wortmeldungenAktivUB) {
        this.wortmeldungenAktivUB = wortmeldungenAktivUB;
    }

    public boolean isWortmeldungenAktiv() {
        return wortmeldungenAktiv;
    }

    public void setWortmeldungenAktiv(boolean wortmeldungenAktiv) {
        this.wortmeldungenAktiv = wortmeldungenAktiv;
    }

    public boolean isWiderspruecheAktivUB() {
        return widerspruecheAktivUB;
    }

    public void setWiderspruecheAktivUB(boolean widerspruecheAktivUB) {
        this.widerspruecheAktivUB = widerspruecheAktivUB;
    }

    public boolean isWiderspruecheAktiv() {
        return widerspruecheAktiv;
    }

    public void setWiderspruecheAktiv(boolean widerspruecheAktiv) {
        this.widerspruecheAktiv = widerspruecheAktiv;
    }

    public boolean isAntraegeAktivUB() {
        return antraegeAktivUB;
    }

    public void setAntraegeAktivUB(boolean antraegeAktivUB) {
        this.antraegeAktivUB = antraegeAktivUB;
    }

    public boolean isAntraegeAktiv() {
        return antraegeAktiv;
    }

    public void setAntraegeAktiv(boolean antraegeAktiv) {
        this.antraegeAktiv = antraegeAktiv;
    }

    public boolean isSonstigeMitteilungenAktivUB() {
        return sonstigeMitteilungenAktivUB;
    }

    public void setSonstigeMitteilungenAktivUB(boolean sonstigeMitteilungenAktivUB) {
        this.sonstigeMitteilungenAktivUB = sonstigeMitteilungenAktivUB;
    }

    public boolean isSonstigeMitteilungenAktiv() {
        return sonstigeMitteilungenAktiv;
    }

    public void setSonstigeMitteilungenAktiv(boolean sonstigeMitteilungenAktiv) {
        this.sonstigeMitteilungenAktiv = sonstigeMitteilungenAktiv;
    }

    public boolean isChatAktivUB() {
        return chatAktivUB;
    }

    public void setChatAktivUB(boolean chatAktivUB) {
        this.chatAktivUB = chatAktivUB;
    }

    public boolean isChatAktiv() {
        return chatAktiv;
    }

    public void setChatAktiv(boolean chatAktiv) {
        this.chatAktiv = chatAktiv;
    }

    public boolean isChatInstiAktivUB() {
        return chatInstiAktivUB;
    }

    public void setChatInstiAktivUB(boolean chatInstiAktivUB) {
        this.chatInstiAktivUB = chatInstiAktivUB;
    }

    public boolean isChatInstiAktiv() {
        return chatInstiAktiv;
    }

    public void setChatInstiAktiv(boolean chatInstiAktiv) {
        this.chatInstiAktiv = chatInstiAktiv;
    }

    public boolean isChatVIPAktivUB() {
        return chatVIPAktivUB;
    }

    public void setChatVIPAktivUB(boolean chatVIPAktivUB) {
        this.chatVIPAktivUB = chatVIPAktivUB;
    }

    public boolean isChatVIPAktiv() {
        return chatVIPAktiv;
    }

    public void setChatVIPAktiv(boolean chatVIPAktiv) {
        this.chatVIPAktiv = chatVIPAktiv;
    }

    public boolean isUnterlagenGruppe1AktivUB() {
        return unterlagenGruppe1AktivUB;
    }

    public void setUnterlagenGruppe1AktivUB(boolean unterlagenGruppe1AktivUB) {
        this.unterlagenGruppe1AktivUB = unterlagenGruppe1AktivUB;
    }

    public boolean isUnterlagenGruppe1Aktiv() {
        return unterlagenGruppe1Aktiv;
    }

    public void setUnterlagenGruppe1Aktiv(boolean unterlagenGruppe1Aktiv) {
        this.unterlagenGruppe1Aktiv = unterlagenGruppe1Aktiv;
    }

    public boolean isUnterlagenGruppe2AktivUB() {
        return unterlagenGruppe2AktivUB;
    }

    public void setUnterlagenGruppe2AktivUB(boolean unterlagenGruppe2AktivUB) {
        this.unterlagenGruppe2AktivUB = unterlagenGruppe2AktivUB;
    }

    public boolean isUnterlagenGruppe2Aktiv() {
        return unterlagenGruppe2Aktiv;
    }

    public void setUnterlagenGruppe2Aktiv(boolean unterlagenGruppe2Aktiv) {
        this.unterlagenGruppe2Aktiv = unterlagenGruppe2Aktiv;
    }

    public boolean isUnterlagenGruppe3AktivUB() {
        return unterlagenGruppe3AktivUB;
    }

    public void setUnterlagenGruppe3AktivUB(boolean unterlagenGruppe3AktivUB) {
        this.unterlagenGruppe3AktivUB = unterlagenGruppe3AktivUB;
    }

    public boolean isUnterlagenGruppe3Aktiv() {
        return unterlagenGruppe3Aktiv;
    }

    public void setUnterlagenGruppe3Aktiv(boolean unterlagenGruppe3Aktiv) {
        this.unterlagenGruppe3Aktiv = unterlagenGruppe3Aktiv;
    }

    public boolean isUnterlagenGruppe4AktivUB() {
        return unterlagenGruppe4AktivUB;
    }

    public void setUnterlagenGruppe4AktivUB(boolean unterlagenGruppe4AktivUB) {
        this.unterlagenGruppe4AktivUB = unterlagenGruppe4AktivUB;
    }

    public boolean isUnterlagenGruppe4Aktiv() {
        return unterlagenGruppe4Aktiv;
    }

    public void setUnterlagenGruppe4Aktiv(boolean unterlagenGruppe4Aktiv) {
        this.unterlagenGruppe4Aktiv = unterlagenGruppe4Aktiv;
    }

    public boolean isUnterlagenGruppe5AktivUB() {
        return unterlagenGruppe5AktivUB;
    }

    public void setUnterlagenGruppe5AktivUB(boolean unterlagenGruppe5AktivUB) {
        this.unterlagenGruppe5AktivUB = unterlagenGruppe5AktivUB;
    }

    public boolean isUnterlagenGruppe5Aktiv() {
        return unterlagenGruppe5Aktiv;
    }

    public void setUnterlagenGruppe5Aktiv(boolean unterlagenGruppe5Aktiv) {
        this.unterlagenGruppe5Aktiv = unterlagenGruppe5Aktiv;
    }

    public boolean isTeilnehmerverzeichnisAktivUB() {
        return teilnehmerverzeichnisAktivUB;
    }

    public void setTeilnehmerverzeichnisAktivUB(boolean teilnehmerverzeichnisAktivUB) {
        this.teilnehmerverzeichnisAktivUB = teilnehmerverzeichnisAktivUB;
    }

    public boolean isTeilnehmerverzeichnisAktiv() {
        return teilnehmerverzeichnisAktiv;
    }

    public void setTeilnehmerverzeichnisAktiv(boolean teilnehmerverzeichnisAktiv) {
        this.teilnehmerverzeichnisAktiv = teilnehmerverzeichnisAktiv;
    }

    public boolean isAbstimmungsergebnisseAktivUB() {
        return abstimmungsergebnisseAktivUB;
    }

    public void setAbstimmungsergebnisseAktivUB(boolean abstimmungsergebnisseAktivUB) {
        this.abstimmungsergebnisseAktivUB = abstimmungsergebnisseAktivUB;
    }

    public boolean isAbstimmungsergebnisseAktiv() {
        return abstimmungsergebnisseAktiv;
    }

    public void setAbstimmungsergebnisseAktiv(boolean abstimmungsergebnisseAktiv) {
        this.abstimmungsergebnisseAktiv = abstimmungsergebnisseAktiv;
    }

    public boolean isEmpfangFragenAktivUB() {
        return empfangFragenAktivUB;
    }

    public void setEmpfangFragenAktivUB(boolean empfangFragenAktivUB) {
        this.empfangFragenAktivUB = empfangFragenAktivUB;
    }

    public boolean isEmpfangFragenAktiv() {
        return empfangFragenAktiv;
    }

    public void setEmpfangFragenAktiv(boolean empfangFragenAktiv) {
        this.empfangFragenAktiv = empfangFragenAktiv;
    }

    public boolean isEmpfangWortmeldungenAktivUB() {
        return empfangWortmeldungenAktivUB;
    }

    public void setEmpfangWortmeldungenAktivUB(boolean empfangWortmeldungenAktivUB) {
        this.empfangWortmeldungenAktivUB = empfangWortmeldungenAktivUB;
    }

    public boolean isEmpfangWortmeldungenAktiv() {
        return empfangWortmeldungenAktiv;
    }

    public void setEmpfangWortmeldungenAktiv(boolean empfangWortmeldungenAktiv) {
        this.empfangWortmeldungenAktiv = empfangWortmeldungenAktiv;
    }

    public boolean isEmpfangWiderspruchAktivUB() {
        return empfangWiderspruchAktivUB;
    }

    public void setEmpfangWiderspruchAktivUB(boolean empfangWiderspruchAktivUB) {
        this.empfangWiderspruchAktivUB = empfangWiderspruchAktivUB;
    }

    public boolean isEmpfangWiderspruchAktiv() {
        return empfangWiderspruchAktiv;
    }

    public void setEmpfangWiderspruchAktiv(boolean empfangWiderspruchAktiv) {
        this.empfangWiderspruchAktiv = empfangWiderspruchAktiv;
    }

    public boolean isEmpfangAntraegeAktivUB() {
        return empfangAntraegeAktivUB;
    }

    public void setEmpfangAntraegeAktivUB(boolean empfangAntraegeAktivUB) {
        this.empfangAntraegeAktivUB = empfangAntraegeAktivUB;
    }

    public boolean isEmpfangAntraegeAktiv() {
        return empfangAntraegeAktiv;
    }

    public void setEmpfangAntraegeAktiv(boolean empfangAntraegeAktiv) {
        this.empfangAntraegeAktiv = empfangAntraegeAktiv;
    }

    public boolean isEmpfangSonstigeMitteilungenAktivUB() {
        return empfangSonstigeMitteilungenAktivUB;
    }

    public void setEmpfangSonstigeMitteilungenAktivUB(boolean empfangSonstigeMitteilungenAktivUB) {
        this.empfangSonstigeMitteilungenAktivUB = empfangSonstigeMitteilungenAktivUB;
    }

    public boolean isEmpfangSonstigeMitteilungenAktiv() {
        return empfangSonstigeMitteilungenAktiv;
    }

    public void setEmpfangSonstigeMitteilungenAktiv(boolean empfangSonstigeMitteilungenAktiv) {
        this.empfangSonstigeMitteilungenAktiv = empfangSonstigeMitteilungenAktiv;
    }

    public boolean isMonitoringAktivUB() {
        return monitoringAktivUB;
    }

    public void setMonitoringAktivUB(boolean monitoringAktivUB) {
        this.monitoringAktivUB = monitoringAktivUB;
    }

    public boolean isMonitoringAktiv() {
        return monitoringAktiv;
    }

    public void setMonitoringAktiv(boolean monitoringAktiv) {
        this.monitoringAktiv = monitoringAktiv;
    }

    public boolean isOnlineteilnahmeAngebotenUB() {
        return onlineteilnahmeAngebotenUB;
    }

    public void setOnlineteilnahmeAngebotenUB(boolean onlineteilnahmeAngebotenUB) {
        this.onlineteilnahmeAngebotenUB = onlineteilnahmeAngebotenUB;
    }

    public boolean isOnlineteilnahmeBerechtigt() {
        return onlineteilnahmeBerechtigt;
    }

    public void setOnlineteilnahmeBerechtigt(boolean onlineteilnahmeBerechtigt) {
        this.onlineteilnahmeBerechtigt = onlineteilnahmeBerechtigt;
    }

    public boolean isOnlineteilnahmeAngeboten() {
        return onlineteilnahmeAngeboten;
    }

    public void setOnlineteilnahmeAngeboten(boolean onlineteilnahmeAngeboten) {
        this.onlineteilnahmeAngeboten = onlineteilnahmeAngeboten;
    }

    public boolean isOnlineteilnahmeAktivUB() {
        return onlineteilnahmeAktivUB;
    }

    public void setOnlineteilnahmeAktivUB(boolean onlineteilnahmeAktivUB) {
        this.onlineteilnahmeAktivUB = onlineteilnahmeAktivUB;
    }

    public boolean isOnlineteilnahmeAktiv() {
        return onlineteilnahmeAktiv;
    }

    public void setOnlineteilnahmeAktiv(boolean onlineteilnahmeAktiv) {
        this.onlineteilnahmeAktiv = onlineteilnahmeAktiv;
    }

    public String getNachricht() {
        return nachricht;
    }

    public void setNachricht(String nachricht) {
        this.nachricht = nachricht;
    }

    public String getKennung() {
        return kennung;
    }

    public void setKennung(String kennung) {
        this.kennung = kennung;
    }

    public boolean isUnterlagenAngebotenUB() {
        return unterlagenAngebotenUB;
    }

    public void setUnterlagenAngebotenUB(boolean unterlagenAngebotenUB) {
        this.unterlagenAngebotenUB = unterlagenAngebotenUB;
    }

    public boolean isUnterlagenBerechtigt() {
        return unterlagenBerechtigt;
    }

    public void setUnterlagenBerechtigt(boolean unterlagenBerechtigt) {
        this.unterlagenBerechtigt = unterlagenBerechtigt;
    }

    public boolean isUnterlagenAngeboten() {
        return unterlagenAngeboten;
    }

    public void setUnterlagenAngeboten(boolean unterlagenAngeboten) {
        this.unterlagenAngeboten = unterlagenAngeboten;
    }

    public boolean isUnterlagenAktivUB() {
        return unterlagenAktivUB;
    }

    public void setUnterlagenAktivUB(boolean unterlagenAktivUB) {
        this.unterlagenAktivUB = unterlagenAktivUB;
    }

    public boolean isUnterlagenAktiv() {
        return unterlagenAktiv;
    }

    public void setUnterlagenAktiv(boolean unterlagenAktiv) {
        this.unterlagenAktiv = unterlagenAktiv;
    }

    public boolean isBotschaftenEinreichenAngebotenUB() {
        return botschaftenEinreichenAngebotenUB;
    }

    public void setBotschaftenEinreichenAngebotenUB(boolean botschaftenEinreichenAngebotenUB) {
        this.botschaftenEinreichenAngebotenUB = botschaftenEinreichenAngebotenUB;
    }

    public boolean isBotschaftenEinreichenBerechtigt() {
        return botschaftenEinreichenBerechtigt;
    }

    public void setBotschaftenEinreichenBerechtigt(boolean botschaftenEinreichenBerechtigt) {
        this.botschaftenEinreichenBerechtigt = botschaftenEinreichenBerechtigt;
    }

    public boolean isBotschaftenEinreichenAngeboten() {
        return botschaftenEinreichenAngeboten;
    }

    public void setBotschaftenEinreichenAngeboten(boolean botschaftenEinreichenAngeboten) {
        this.botschaftenEinreichenAngeboten = botschaftenEinreichenAngeboten;
    }

    public boolean isBotschaftenEinreichenAktivUB() {
        return botschaftenEinreichenAktivUB;
    }

    public void setBotschaftenEinreichenAktivUB(boolean botschaftenEinreichenAktivUB) {
        this.botschaftenEinreichenAktivUB = botschaftenEinreichenAktivUB;
    }

    public boolean isBotschaftenEinreichenAktiv() {
        return botschaftenEinreichenAktiv;
    }

    public void setBotschaftenEinreichenAktiv(boolean botschaftenEinreichenAktiv) {
        this.botschaftenEinreichenAktiv = botschaftenEinreichenAktiv;
    }


    public boolean isRueckfragenAngebotenUB() {
        return rueckfragenAngebotenUB;
    }

    public void setRueckfragenAngebotenUB(boolean rueckfragenAngebotenUB) {
        this.rueckfragenAngebotenUB = rueckfragenAngebotenUB;
    }

    public boolean isRueckfragenBerechtigt() {
        return rueckfragenBerechtigt;
    }

    public void setRueckfragenBerechtigt(boolean rueckfragenBerechtigt) {
        this.rueckfragenBerechtigt = rueckfragenBerechtigt;
    }

    public boolean isRueckfragenAngeboten() {
        return rueckfragenAngeboten;
    }

    public void setRueckfragenAngeboten(boolean rueckfragenAngeboten) {
        this.rueckfragenAngeboten = rueckfragenAngeboten;
    }

    public boolean isRueckfragenAktivUB() {
        return rueckfragenAktivUB;
    }

    public void setRueckfragenAktivUB(boolean rueckfragenAktivUB) {
        this.rueckfragenAktivUB = rueckfragenAktivUB;
    }

    public boolean isRueckfragenAktiv() {
        return rueckfragenAktiv;
    }

    public void setRueckfragenAktiv(boolean rueckfragenAktiv) {
        this.rueckfragenAktiv = rueckfragenAktiv;
    }

    public boolean isRueckfragenBerechtigtWennGestellt() {
        return rueckfragenBerechtigtWennGestellt;
    }

    public void setRueckfragenBerechtigtWennGestellt(boolean rueckfragenBerechtigtWennGestellt) {
        this.rueckfragenBerechtigtWennGestellt = rueckfragenBerechtigtWennGestellt;
    }

    public boolean isBotschaftenAnzeigeAngebotenUB() {
        return botschaftenAnzeigeAngebotenUB;
    }

    public void setBotschaftenAnzeigeAngebotenUB(boolean botschaftenAnzeigeAngebotenUB) {
        this.botschaftenAnzeigeAngebotenUB = botschaftenAnzeigeAngebotenUB;
    }

    public boolean isBotschaftenAnzeigeBerechtigt() {
        return botschaftenAnzeigeBerechtigt;
    }

    public void setBotschaftenAnzeigeBerechtigt(boolean botschaftenAnzeigeBerechtigt) {
        this.botschaftenAnzeigeBerechtigt = botschaftenAnzeigeBerechtigt;
    }

    public boolean isBotschaftenAnzeigeAngeboten() {
        return botschaftenAnzeigeAngeboten;
    }

    public void setBotschaftenAnzeigeAngeboten(boolean botschaftenAnzeigeAngeboten) {
        this.botschaftenAnzeigeAngeboten = botschaftenAnzeigeAngeboten;
    }

    public boolean isBotschaftenAnzeigeAktivUB() {
        return botschaftenAnzeigeAktivUB;
    }

    public void setBotschaftenAnzeigeAktivUB(boolean botschaftenAnzeigeAktivUB) {
        this.botschaftenAnzeigeAktivUB = botschaftenAnzeigeAktivUB;
    }

    public boolean isBotschaftenAnzeigeAktiv() {
        return botschaftenAnzeigeAktiv;
    }

    public void setBotschaftenAnzeigeAktiv(boolean botschaftenAnzeigeAktiv) {
        this.botschaftenAnzeigeAktiv = botschaftenAnzeigeAktiv;
    }

    public boolean isBotschaftenEinreichenTatsaechlichBerechtigt() {
        return botschaftenEinreichenTatsaechlichBerechtigt;
    }

    public void setBotschaftenEinreichenTatsaechlichBerechtigt(boolean botschaftenEinreichenTatsaechlichBerechtigt) {
        this.botschaftenEinreichenTatsaechlichBerechtigt = botschaftenEinreichenTatsaechlichBerechtigt;
    }

    public boolean isFragenWurdenGestellt() {
        return fragenWurdenGestellt;
    }

    public void setFragenWurdenGestellt(boolean fragenWurdenGestellt) {
        this.fragenWurdenGestellt = fragenWurdenGestellt;
    }

}
