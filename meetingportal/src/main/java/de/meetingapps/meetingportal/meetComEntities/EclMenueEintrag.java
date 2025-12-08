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

public class EclMenueEintrag  implements Serializable {
    private static final long serialVersionUID = 3298917335041067205L;

    /**Wird frei vergeben - zur Identifikation, welcher Menüpunkt ausgewählt wurde.
     * Muß eindeutig sein.
     */
    public int ident=0;
    
    public long db_version=0;

    /**Lfd.-Nr. Haupteintrag*/
    public int nrHaupt=0;
    
    /**Lfd.Nr. Untermenü
     * >0 => ist Untermenü, nrHaupt muß dann ebenfalls gefüllt sein*/
    public int nrSub=0;
    
    /**Lfd.Nr. Unteruntermenü
     * > 0 => ist Unteruntermenü. nrHaupt und nrSub müssen ebenfalls gefüllt sein
     * und geben dann die Zuordnung wieder*/
    public int nrSubSub=0;
    
    /**siehe KonstPMenueFunktionscode
     * -1 = Trennzeichen*/
    public int funktionscode=0;
    
    /**manche Funktionscodes sind mehrfach zulässig - der functionscodeSub dient damit der Unterscheidung
     * der einzelnen Menüpunkte. 
     * 
     * Konkret zulässig bei:
     * UNTERLAGEN
     * => funktionscodeSub gibt den Menüpunkt an, dem die Unterlagen zugeordnet werden (siehe EclPortalUnterlagen.untelagenbereichMenueNr
     * => Speziell funktioncodeSub = 0, 100 etc.: es werden alle Dokumente mit den Funktionscodes 0 bis 99, 100 bis 199 etc. angezeigt
     * 
     * VERANSTALTUNGEN
     * => funktionscodeSub gibt den Menüpunkt an, dem die Veranstaltungen zugeordnet werden.
     */
    public int funktionscodeSub=0;
    
    /**Wenn ==0, dann wird der Text für diesen Menüpunkt über KonstPMenueFunktionscode.textNr() belegt,
     * ansonsten wird die hier gespeicherte Nummer als Text-Nummer verwendet.
     */
    public int textNrGespeichert=0;
    
    /**+++++++++++++++Ab hier nicht in Datenbank++++++++++++++++++++*/
    /**Nr. des anzuzeigenden Textes. Wird über
     * KonstPMenueFunktionscode.textNr() belegt
     */
    public int textnr=0;
    
    /**Nr. des anzuzeigenden Icons. Wird über
     * KonstPMenueFunktionscode.iconNr() belegt
     */
    public int iconCode=0;

    /**Wird situationsbedingt vom Programm gefüllt und muß bei der Anzeige - ohne Punkt und 
     * Komma und ohne Space- an die Text-Nr angehängt werden.
     */
    public String textErgaenzung="";
    
    public boolean aktuellAusgewaehlt=false;


    
    /**Liste der zu diesem Menüpunkt gehörenden
     * Unter-Menüs (bzw. bei Untermenüs: der Unteruntermenüs).*/
    public List<EclMenueEintrag> submenueList=new LinkedList<EclMenueEintrag>();

    
    /*+++++++++++Initialisierungsfunktionen+++++++++++++++++++++++*/
    public EclMenueEintrag() {
        return;
    }
    
    /**Kopiert die aus Datenbank gelesenen Teile in den neuen
     * MenueEintrag
     */
    public EclMenueEintrag(EclMenueEintrag pMenueEintrag) {
        this.ident=pMenueEintrag.ident;
        this.db_version=pMenueEintrag.db_version;
        this.nrHaupt=pMenueEintrag.nrHaupt;
        this.nrSub=pMenueEintrag.nrSub;
        this.nrSubSub=pMenueEintrag.nrSubSub;
        this.funktionscode=pMenueEintrag.funktionscode;
        this.funktionscodeSub=pMenueEintrag.funktionscodeSub;
        this.textNrGespeichert=pMenueEintrag.textNrGespeichert;
        return;
    }

    /**+++++++++++++++Spezielle Funktionen++++++++++++++++++++++*/
    public boolean trennzeichen() {
        if (funktionscode==-1) {return true;} else {return false;}
    }

    public boolean submenueVorhanden() {
        if (submenueList==null || submenueList.size()==0) {return false;}
        return true;
    }

    /*****************Standard setter und getter******************/

    public int getIdent() {
        return ident;
    }


    public void setIdent(int ident) {
        this.ident = ident;
    }


    public int getNrHaupt() {
        return nrHaupt;
    }


    public void setNrHaupt(int nrHaupt) {
        this.nrHaupt = nrHaupt;
    }


    public int getNrSub() {
        return nrSub;
    }


    public void setNrSub(int nrSub) {
        this.nrSub = nrSub;
    }


    public int getNrSubSub() {
        return nrSubSub;
    }


    public void setNrSubSub(int nrSubSub) {
        this.nrSubSub = nrSubSub;
    }


    public int getFunktionscode() {
        return funktionscode;
    }


    public void setFunktionscode(int funktionscode) {
        this.funktionscode = funktionscode;
    }


    public int getTextnr() {
        return textnr;
    }


    public void setTextnr(int textnr) {
        this.textnr = textnr;
    }


    public boolean isAktuellAusgewaehlt() {
        return aktuellAusgewaehlt;
    }


    public void setAktuellAusgewaehlt(boolean aktuellAusgewaehlt) {
        this.aktuellAusgewaehlt = aktuellAusgewaehlt;
    }

    public String getTextErgaenzung() {
        return textErgaenzung;
    }

    public void setTextErgaenzung(String textErgaenzung) {
        this.textErgaenzung = textErgaenzung;
    }

    public List<EclMenueEintrag> getSubmenueList() {
        return submenueList;
    }

    public void setSubmenueList(List<EclMenueEintrag> submenueList) {
        this.submenueList = submenueList;
    }

    public int getIconCode() {
        return iconCode;
    }

    public void setIconCode(int iconCode) {
        this.iconCode = iconCode;
    }

    public int getFunktionscodeSub() {
        return funktionscodeSub;
    }

    public void setFunktionscodeSub(int funktionscodeSub) {
        this.funktionscodeSub = funktionscodeSub;
    }

    public int getTextNrGespeichert() {
        return textNrGespeichert;
    }

    public void setTextNrGespeichert(int textNrGespeichert) {
        this.textNrGespeichert = textNrGespeichert;
    }
    
    
    
}
