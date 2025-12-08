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

public class EclVeranstaltungenQuittungElement implements Serializable  {
    private static final long serialVersionUID = -4746884520230763307L;

    public int identQuittungElement=0;

    /**Verweis auf Veranstaltung.
     */
    public int identVeranstaltung=0;

    /**Verweis auf Element (zwingend falls dortiger Eingabewelt berücksicht werden soll)*/
    public int zuIdentElement=0;
    
    public int zuIdentElementDetail=0;
    
    public int position=0;
   
    /**Len=1000*/
    public String beschreibung="";
   
    /**Gibt an, wieviele "Einheiten" das Element eingerückt werden soll*/
    public int einrueckungsEbene=0;
    
    /**=0 => wird immer verwendet*/
    public int wirdVerwendetWennVeranstaltungAktivierungsStatusGleich=0;
   
    /**0=immer
     * 1=bei "Einstiegsquittung" anzeigen (d.h. wenn man den Menüpunkt anwählt, wird dieses Element angezeigt)
     * 2=bei "Bestätigungsquittung" anzeigen (d.h. wenn man im Änderungsmodus war, und dann auf speichern klickt)
     */
    public int wirdVerwendetBeiQuittung=0;
    
    /**!=0 => wirdVerwendetWennEingabeGleich wird ignoriert.
     * 1=wenn Eingabewert gleich geblieben
     * 2=wenn Eingabewert verändert
     * 3=wenn Eingabewert verändert und ursprünglich "leer" war (bei Anzahl: 0 oder leer)
     * 4=wenn Eingabewert verändert und nun "leer" wurde (bei Anzahl: 0 oder leer)
     * 5=wenn Eingabewert verändert und ursprünglich "nicht leer" war (also Anzahl ursprünglich nicht 0 und nicht leer)
     */
    public int wirdVerwendetWennEingabeWertVeraendert=0;
    
    /**=0 => wird immer verwendet
     * 
     * Verfügbar, wenn Element Radiobuttons oder Checkbox ist.
     * Für RadioButtons:
     * >1 => Wert wird mit dem Eingabefeld des Vorgängers überprüft 
     *      (und damit mit den Eingabewerten der Radiobuttons)
     * FÜr Checkbuttons:
     * =1 => wird überprüft ob Checkbutton des Detailelements selektiert ist
     *      zuIdentEalementDetail muß dabei gefüllt sein.
     * 
     * Jeweils <0: wie oben, nur "Ungleich"
     */
    public int wirdVerwendetWennEingabeGleich=0;

    /**1=Überschrift
     * 2=Text
     * 3=Eingabefeld wird in Quittung übernommen
     * 9=Button zum Ausdruck einer Karte (wird je nach Bezugs-Element, also z.B.
     * bei Anzahl, beliebig oft wiederholt*/
    public int quittungTyp=0;
    
    /**LEN=1000*/
    public String quittungstext="";
    
    /**Wenn sich Quittung auf Eingabefeld bezieht, dann wird anschließend Wert und danach
     * quittungstextNachWert angezeigt.
     */
    
    /**LEN=1000*/
    public String quittungstextNachWert="";

    /**LEN=200
     * Bei quittungTyp==3*/
    public String buttonBezeichnung="";
    
    /************************Nicht in Datenbank*********************************/
    public String eingabeWert="";
    
    /**Wird für Anzeige verwendet. Enthält ggf. Laufende Nummer, z.B. für Ticket*/
    public String buttonBezeichnungKomplett="";
    
    /**Z.B. bei Eintrittskartendruck Verweis auf die Karte*/
    public String wertParameter="";
    
    public EclVeranstaltungenElement verweisAufZugehoerigesElement=null;
    public EclVeranstaltungenElementDetail verweisAufZugehoerigesElementDetail=null;
    

    
    public EclVeranstaltungenQuittungElement() {
        
    }

    public EclVeranstaltungenQuittungElement(EclVeranstaltungenQuittungElement pVeranstaltungenQuittungElement) {
        identQuittungElement=pVeranstaltungenQuittungElement.identQuittungElement;
        identVeranstaltung=pVeranstaltungenQuittungElement.identVeranstaltung;
        zuIdentElement=pVeranstaltungenQuittungElement.zuIdentElement;
        zuIdentElementDetail=pVeranstaltungenQuittungElement.zuIdentElementDetail;
        position=pVeranstaltungenQuittungElement.position;
        beschreibung=pVeranstaltungenQuittungElement.beschreibung;
        einrueckungsEbene=pVeranstaltungenQuittungElement.einrueckungsEbene;
        wirdVerwendetWennVeranstaltungAktivierungsStatusGleich=pVeranstaltungenQuittungElement.wirdVerwendetWennVeranstaltungAktivierungsStatusGleich;
        wirdVerwendetWennEingabeWertVeraendert=pVeranstaltungenQuittungElement.wirdVerwendetWennEingabeWertVeraendert;
        wirdVerwendetWennEingabeGleich=pVeranstaltungenQuittungElement.wirdVerwendetWennEingabeGleich;
        quittungTyp=pVeranstaltungenQuittungElement.quittungTyp;
        quittungstext=pVeranstaltungenQuittungElement.quittungstext;
        quittungstextNachWert=pVeranstaltungenQuittungElement.quittungstextNachWert;
        buttonBezeichnung=pVeranstaltungenQuittungElement.buttonBezeichnung;
    }

    public int getIdentQuittungElement() {
        return identQuittungElement;
    }

    public void setIdentQuittungElement(int identQuittungElement) {
        this.identQuittungElement = identQuittungElement;
    }

    public int getIdentVeranstaltung() {
        return identVeranstaltung;
    }

    public void setIdentVeranstaltung(int identVeranstaltung) {
        this.identVeranstaltung = identVeranstaltung;
    }

    public int getZuIdentElement() {
        return zuIdentElement;
    }

    public void setZuIdentElement(int zuIdentElement) {
        this.zuIdentElement = zuIdentElement;
    }

    public int getZuIdentElementDetail() {
        return zuIdentElementDetail;
    }

    public void setZuIdentElementDetail(int zuIdentElementDetail) {
        this.zuIdentElementDetail = zuIdentElementDetail;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getBeschreibung() {
        return beschreibung;
    }

    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }

    public int getEinrueckungsEbene() {
        return einrueckungsEbene;
    }

    public void setEinrueckungsEbene(int einrueckungsEbene) {
        this.einrueckungsEbene = einrueckungsEbene;
    }

    public int getWirdVerwendetWennVeranstaltungAktivierungsStatusGleich() {
        return wirdVerwendetWennVeranstaltungAktivierungsStatusGleich;
    }

    public void setWirdVerwendetWennVeranstaltungAktivierungsStatusGleich(int wirdVerwendetWennVeranstaltungAktivierungsStatusGleich) {
        this.wirdVerwendetWennVeranstaltungAktivierungsStatusGleich = wirdVerwendetWennVeranstaltungAktivierungsStatusGleich;
    }

    public int getWirdVerwendetWennEingabeGleich() {
        return wirdVerwendetWennEingabeGleich;
    }

    public void setWirdVerwendetWennEingabeGleich(int wirdVerwendetWennEingabeGleich) {
        this.wirdVerwendetWennEingabeGleich = wirdVerwendetWennEingabeGleich;
    }

    public int getQuittungTyp() {
        return quittungTyp;
    }

    public void setQuittungTyp(int quittungTyp) {
        this.quittungTyp = quittungTyp;
    }

    public String getQuittungstext() {
        return quittungstext;
    }

    public void setQuittungstext(String quittungstext) {
        this.quittungstext = quittungstext;
    }

    public String getQuittungstextNachWert() {
        return quittungstextNachWert;
    }

    public void setQuittungstextNachWert(String quittungstextNachWert) {
        this.quittungstextNachWert = quittungstextNachWert;
    }

    public String getButtonBezeichnung() {
        return buttonBezeichnung;
    }

    public void setButtonBezeichnung(String buttonBezeichnung) {
        this.buttonBezeichnung = buttonBezeichnung;
    }

    public String getEingabeWert() {
        return eingabeWert;
    }

    public void setEingabeWert(String eingabeWert) {
        this.eingabeWert = eingabeWert;
    }

    public String getButtonBezeichnungKomplett() {
        return buttonBezeichnungKomplett;
    }

    public void setButtonBezeichnungKomplett(String buttonBezeichnungKomplett) {
        this.buttonBezeichnungKomplett = buttonBezeichnungKomplett;
    }

    public String getWertParameter() {
        return wertParameter;
    }

    public void setWertParameter(String wertParameter) {
        this.wertParameter = wertParameter;
    }

    public EclVeranstaltungenElement getVerweisAufZugehoerigesElement() {
        return verweisAufZugehoerigesElement;
    }

    public void setVerweisAufZugehoerigesElement(EclVeranstaltungenElement verweisAufZugehoerigesElement) {
        this.verweisAufZugehoerigesElement = verweisAufZugehoerigesElement;
    }

    public EclVeranstaltungenElementDetail getVerweisAufZugehoerigesElementDetail() {
        return verweisAufZugehoerigesElementDetail;
    }

    public void setVerweisAufZugehoerigesElementDetail(EclVeranstaltungenElementDetail verweisAufZugehoerigesElementDetail) {
        this.verweisAufZugehoerigesElementDetail = verweisAufZugehoerigesElementDetail;
    }
}
