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

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaString;
import de.meetingapps.meetingportal.meetComEh.EhJsfSelectItem;

public class EclVeranstaltungenElement  implements Serializable  {
    private static final long serialVersionUID = 6144393074700784880L;

    private int logDrucken=8;

    public int identElement=0;
    
    
    /**Verweis auf Veranstaltung.
     */
    public int identVeranstaltung=0;
    
    /**Verweis auf Element.
     * Wenn ==0, dann untergeordnet zu Veranstaltung.
     * Ansonsten untergeordnet zu dem Element mit der Ident.
     */
    public int gehoertZuElement=0;
    public int position=0;

    
    
    /**Len=1000*/
    public String beschreibung="";
    
    /**FÜr Reports.
     * LEN=100
     */
    public String textIntern="";
    
    /**Gibt an, wieviele "Einheiten" das Element eingerückt werden soll*/
    public int einrueckungsEbene=0;
    
    /**Verschiedene Status-Werte, die in den Sub-Elementen abgefragt werden können.
     * Siehe EclVeranstaltungenVeranstaltungen.aktivierungsStatus
     * 0= immer aktiv
     */
    public int aktivierungsStatus=0;

    /**=0 => wird immer verwendet*/
    public int wirdVerwendetWennVorgaengerAktivierungsStatusGleich=0;
    
    /**Verfügbar, wenn Vorgänger Radiobuttons oder Checkbox ist.
     * Für RadioButtons:
     * =1 => vergleichswertVorgaenger wird mit dem Eingabefeld des Vorgängers überprüft 
     *      (und damit mit den Eingabewerten der Radiobuttons)
     * FÜr Checkbuttons:
     * > 0 => wird überprüft, Checkbutton an erzeugtWert true ist
     */
    public int wirdVerwendetWennVorgaengerGleich=0;
    
    /*TODO noch nicht implementiert*/
    public int wirdVerwendetWennGattung=0;
    
    /**Mit diesem Wert wird der Vorgänger verglichen,
     * wenn wirdVerwendetWennVorgaengerGleich=1;
     */
    public String vergleichswertVorgaenger="";
    
    /**1=Überschrift
     * 2=Text
     * 3=Eingabegfeld der Art "Anzahl" (kann in Sub-Elementen referenziert werden)
     * 4=Eingabefeld String (z.B. Name, Vorname, E-Mail-Adresse).
     * 5=Checkbox
     * 6=Radiobutton
     * 7=Select-Drop-Down
     * 8=TextArea:
     * 9=reserviert (gleichlauf mit EclVeranstaltungenQuittungElement)
     * 
     * 1001=Steuerungselement für "Weiter-Button".
     *  Wenn für eine Veranstaltung mit entsprechendem Aktivierungsstatus, dann wird Weiter-Button mit Beschriftung textExtern angeboten.
     *  Hinweis: Das wird möglicherweise verwirrend, wenn mehrere Veranstaltungen auf auf einer Seite angeboten werden. Dann wird der Button
     *  angeboten, wenn eine der Veranstaltungen den Button aktiviert. Anzeige-Text des Buttons dann mehr oder weniger "zufällig" ...
     *  
     * Hinweis zum Wert, den Checkbox/Radiobutton/Select-Drop-Down liefern:
     * > Bei "nur eine Selektion": identDetail wird als String-Wert geliefert
     * > Bei "Mehrfachauswahl möglich": identDetails, getrennt durch , werden als String-Wert geliefert
     */
    public int elementTyp=0;
    
    /**Für Radiobutton:
     * 1=Horiontale Anordnung
     * 2=Vertikale Anordnung
     */
    public int elementDesign=0;
    
    /**0 = keine Vorbelegung
     * 1 = Vorbelegung mit Defaultwert
     *      Für Checkboxes / Radiobuttons: Wert entspricht dem Ergbeniswert aus ElementDetail
     * Ab 2 aus defaultWerte mit entsprechendem Offset
     * 2 = Vorbelegung mit E-Mail-Adresse der Login-Kennung (nur für Textfelder)
     */
    public int vorbelegenMit=0;
    
    /**Für Eingabefelder*/
    public int eingabezwang=0;
    
    /**LEN=100*/
    public String defaultWert="";
    
    /**FÜr elementTyp 1 und 2: Text zum Anzeigen
     * Für elementTyp 3 und 4: Text vor dem Eingabefeld
     * LEN=1000*/
    public String textExtern="";
    
    /**Für elementTyp 3 und 4: Text nach dem Eingabefeld
     * LEN=1000*/
    public String textExternNach="";
    
    /**Für Anzahl - Integerwert.
     * 
     * Für Checkboxen: Anzahl der minimal/maximal auszuwertenden Werte
     * 
     * Für normales Eingabefeld: 
     * > minimalWert=Feldlänge Eingabe
     * > maximalWert= Länge Maximal (MaxWert=1000)
     */
    public int minimalWert=0;
    public int maximalWert=99999;
    
    /**Nur für Anzahlfelder: Summe über Alle Anmeldungen darf diese
     * Maximalsumme nicht überschreiten.
     */
    public int maximalSumme=-1;
    /**LEN=200*/
    public String meldungWennMaximalSummeUeberschritten="";
    /**
     * EN=200*/
    public String meldungWennUnzulaessigerWert="";

//    /**++++++++++Für Quittungsseite+++++++++++++++++++++++++++++*/
//    /**LEN=1000*/
//    public String quittungstextVorWert="";
//    
//    /**Als Wert wird genommen:
//     * > Eingabefeld bei elementTyp Eingabefeld
//     * > Text aus ausgewähltem ElementDetail, wenn Checkbox / Radiobutton / Select-Drop-Down; falls mehrere ausgewählt: die Texte der entsprechenden hintereinander.
//     */
//    
//    /**LEN=1000*/
//    public String quittungstextNachWert="";
    
    
    /*********************Nicht in Datenbank*************************************/
    
//    public String namensId="";
    
    public List<EclVeranstaltungenElement> veranstaltungenElementListe=new LinkedList<EclVeranstaltungenElement>();
    public boolean pruefeObElementeVorhanden() {
        CaBug.druckeLog("", logDrucken, 10);
        return (veranstaltungenElementListe!=null && veranstaltungenElementListe.size()>0);
    }

//    public List<EclVeranstaltungenElement> veranstaltungenElementAnzeigenListe=new LinkedList<EclVeranstaltungenElement>();
//    public boolean pruefeObElementeAnzeigenVorhanden() {
//        return (veranstaltungenElementAnzeigenListe!=null && veranstaltungenElementAnzeigenListe.size()>0);
//    }

    public String eingabeWert="";
    
    public String eingabeWertAlt="";
    
    /**Wird nach dem Absenden in einem separaten Lauf gefüllt.
     * true => dieses Element wurde in der letzten Bearbeitungsview des Teilnehmers angezeigt und ist deshalb in der QUittungssicht zu berücksichtigen.
     */
    public boolean inLetzterVerarbeitungEnthalten=false;
    
    public List<EclVeranstaltungenElementDetail> veranstaltungenElementDetailListe=new LinkedList<EclVeranstaltungenElementDetail>();

    public List<EclVeranstaltungenAktion> veranstaltungenAktionListe=new LinkedList<EclVeranstaltungenAktion>();
    
//    public List<EclVeranstaltungenElementDetail> veranstaltungenElementDetailAnzeigenListe=new LinkedList<EclVeranstaltungenElementDetail>();

    /**Enthält Element-Liste für elementTyp Radio-Button*/
    public List<EhJsfSelectItem> selectItemListeFuerRadiobutton=null;

    public EclVeranstaltungenVeranstaltung verweisAufVeranstaltung=null;
    /**==null, wenn direkt der Veranstaltung untergeordnet*/
    public EclVeranstaltungenElement verweisAufElement=null;
    
    /**Für Reports*/
    public int summeUeberAlleTeilnehmer=0;
    
    
    
    public EclVeranstaltungenElement() {
        
    }
    
    /**Übernimmt die nicht-variablen Felder (einschließlich veranstaltungenElementListe).
     * eingabeWert wird auf Default-Wert gesetzt*/
    public EclVeranstaltungenElement(EclVeranstaltungenElement pEclVeranstaltungenElement, String[] defaultWerte, EclVeranstaltungenVeranstaltung pVeranstaltung, EclVeranstaltungenElement pVorgaengerElement) {
        identElement=pEclVeranstaltungenElement.identElement;
        identVeranstaltung=pEclVeranstaltungenElement.identVeranstaltung;
        gehoertZuElement=pEclVeranstaltungenElement.gehoertZuElement;
        position=pEclVeranstaltungenElement.position;
        beschreibung=pEclVeranstaltungenElement.beschreibung;
        textIntern=pEclVeranstaltungenElement.textIntern;
        einrueckungsEbene=pEclVeranstaltungenElement.einrueckungsEbene;
        aktivierungsStatus=pEclVeranstaltungenElement.aktivierungsStatus;
        wirdVerwendetWennVorgaengerAktivierungsStatusGleich=pEclVeranstaltungenElement.wirdVerwendetWennVorgaengerAktivierungsStatusGleich;
        wirdVerwendetWennVorgaengerGleich=pEclVeranstaltungenElement.wirdVerwendetWennVorgaengerGleich;
        wirdVerwendetWennGattung=pEclVeranstaltungenElement.wirdVerwendetWennGattung;
        vergleichswertVorgaenger=pEclVeranstaltungenElement.vergleichswertVorgaenger;
        elementTyp=pEclVeranstaltungenElement.elementTyp;
        elementDesign=pEclVeranstaltungenElement.elementDesign;
        vorbelegenMit=pEclVeranstaltungenElement.vorbelegenMit;
        eingabezwang=pEclVeranstaltungenElement.eingabezwang;
        defaultWert=pEclVeranstaltungenElement.defaultWert;
        if (vorbelegenMit==1) {
            eingabeWert=defaultWert;            
        }
        if (vorbelegenMit>1) {
            if (defaultWerte!=null && defaultWerte.length>=vorbelegenMit+1) {
                eingabeWert=defaultWerte[vorbelegenMit];
            }
        }
        textExtern=pEclVeranstaltungenElement.textExtern;
        textExternNach=pEclVeranstaltungenElement.textExternNach;
        minimalWert=pEclVeranstaltungenElement.minimalWert;
        maximalWert=pEclVeranstaltungenElement.maximalWert;
        maximalSumme=pEclVeranstaltungenElement.maximalSumme;
        meldungWennMaximalSummeUeberschritten=pEclVeranstaltungenElement.meldungWennMaximalSummeUeberschritten;
        meldungWennUnzulaessigerWert=pEclVeranstaltungenElement.meldungWennUnzulaessigerWert;
        
        verweisAufVeranstaltung=pVeranstaltung;
        verweisAufElement=pVorgaengerElement;
        
//        namensId="n"+Integer.toString(identElement);
        
        
        if (pEclVeranstaltungenElement.veranstaltungenElementListe!=null && pEclVeranstaltungenElement.veranstaltungenElementListe.size()>0) {
            for (int i=0;i<pEclVeranstaltungenElement.veranstaltungenElementListe.size();i++) {
                veranstaltungenElementListe.add(new EclVeranstaltungenElement(pEclVeranstaltungenElement.veranstaltungenElementListe.get(i), defaultWerte, verweisAufVeranstaltung, this));
            }
        }

        if (pEclVeranstaltungenElement.veranstaltungenElementDetailListe!=null && pEclVeranstaltungenElement.veranstaltungenElementDetailListe.size()>0) {
            for (int i=0;i<pEclVeranstaltungenElement.veranstaltungenElementDetailListe.size();i++) {
                CaBug.druckeLog("Kopiere Detail="+pEclVeranstaltungenElement.veranstaltungenElementDetailListe.get(i).identDetail, logDrucken, 10);
                veranstaltungenElementDetailListe.add(new EclVeranstaltungenElementDetail(pEclVeranstaltungenElement.veranstaltungenElementDetailListe.get(i)));
            }
        }

        if (pEclVeranstaltungenElement.veranstaltungenAktionListe!=null && pEclVeranstaltungenElement.veranstaltungenAktionListe.size()>0) {
            for (int i=0;i<pEclVeranstaltungenElement.veranstaltungenAktionListe.size();i++) {
                CaBug.druckeLog("Kopiere Aktion="+pEclVeranstaltungenElement.veranstaltungenAktionListe.get(i).identAktion, logDrucken, 10);
                veranstaltungenAktionListe.add(new EclVeranstaltungenAktion(pEclVeranstaltungenElement.veranstaltungenAktionListe.get(i)));
            }
        }

        
        
        baueRadioButtonListeAuf();

    }
    
    /**Ggf. Radio-Button-Liste aufbauen*/
    public void baueRadioButtonListeAuf() {
        if (elementTyp==6) {
            CaBug.druckeLog("RadioButton Items eintragen identElement="+identElement+" veranstaltungenElementDetailListe.size()="+veranstaltungenElementDetailListe.size(), logDrucken, 10);
            selectItemListeFuerRadiobutton=new LinkedList<EhJsfSelectItem>();
            for (int i2=0;i2<veranstaltungenElementDetailListe.size();i2++) {
                CaBug.druckeLog("i2="+i2, logDrucken, 10);
                EclVeranstaltungenElementDetail lVeranstaltungenElementDetail=veranstaltungenElementDetailListe.get(i2);
                EhJsfSelectItem lSelectItem=new EhJsfSelectItem(lVeranstaltungenElementDetail.textExtern, lVeranstaltungenElementDetail.erzeugtWert);
                selectItemListeFuerRadiobutton.add(lSelectItem);
            }
        }
        
    }
        
//    public int belegeMitElementeZumAnzeigenFuerTeilnehmer() {
//        CaBug.druckeLog("", logDrucken, 10);
//        veranstaltungenElementAnzeigenListe=new LinkedList<EclVeranstaltungenElement>();
//        
//        for (int i1=0;i1<veranstaltungenElementListe.size();i1++) {
//            EclVeranstaltungenElement lVeranstaltungenElement=veranstaltungenElementListe.get(i1);
//            if (lVeranstaltungenElement.wirdVerwendetWennVorgaengerAktivierungsStatusGleich==0 || 
//                    lVeranstaltungenElement.wirdVerwendetWennVorgaengerAktivierungsStatusGleich==aktivierungsStatus) {
//                
//                lVeranstaltungenElement.baueRadioButtonListeAuf();
//                veranstaltungenElementAnzeigenListe.add(lVeranstaltungenElement);
//                lVeranstaltungenElement.belegeMitElementeZumAnzeigenFuerTeilnehmer();
//            }
//            
//        }
//        return 1;
//    }
    
    /**true => Element wird dem Benutzer angezeigt*/
    public boolean elementAnzeigen() {
        CaBug.druckeLog("ident="+identElement+" wirdVerwendetWennVorgaengerGleich="+wirdVerwendetWennVorgaengerGleich, logDrucken, 10);
        if (verweisAufElement==null) {
            CaBug.druckeLog("verweisAufElement==null", logDrucken, 10);
            /**Dann ist Element unmittelbarer Nachfolger einer Veranstaltung*/
            if (wirdVerwendetWennVorgaengerAktivierungsStatusGleich!=0 &&
                    wirdVerwendetWennVorgaengerAktivierungsStatusGleich!=verweisAufVeranstaltung.aktivierungsStatus) {
                return false;
            }
            return true;
        }
        /**Element ist einem anderen Element untergeordnet*/
        /**Aktivierungsstatus prüfen*/
        if (wirdVerwendetWennVorgaengerAktivierungsStatusGleich!=0 &&
                wirdVerwendetWennVorgaengerAktivierungsStatusGleich!=verweisAufElement.aktivierungsStatus) {
            CaBug.druckeLog("Aktivierungsstatus false", logDrucken, 10);
            return false;
        }
        CaBug.druckeLog("vergleichswertVorgaenger="+vergleichswertVorgaenger+" verweisAufElement.eingabeWert="+verweisAufElement.eingabeWert, logDrucken, 10);
        if (wirdVerwendetWennVorgaengerGleich!=0) {
            if (verweisAufElement.elementTyp==6) {
                /**radiobutton*/
                if (vergleichswertVorgaenger.equals(verweisAufElement.eingabeWert)){
                    CaBug.druckeLog("false", logDrucken, 10);
                    return true;
                }
                else {
                    return false;
                }
            }
            if (verweisAufElement.elementTyp==5) {
                for (int i=0;i<verweisAufElement.veranstaltungenElementDetailListe.size();i++) {
                    EclVeranstaltungenElementDetail lVeranstaltungenElementDetail=verweisAufElement.veranstaltungenElementDetailListe.get(i);
                    String hPosition=lVeranstaltungenElementDetail.erzeugtWert;
                    if (CaString.isNummern(hPosition)==false) {
                        return false;
                    }
                    int lPosition=Integer.parseInt(hPosition);
                    if (lPosition==wirdVerwendetWennVorgaengerGleich) {
                        if (lVeranstaltungenElementDetail.geradeSelektiert) {
                            return true;
                        }
                        else {
                            return false;
                        }
                    }
                }
            }
        }
        
        return true;
    }
    
    /***************Standard getter und setter************************************/
    
    public int getIdentElement() {
        return identElement;
    }
    public void setIdentElement(int identElement) {
        this.identElement = identElement;
    }
    public int getIdentVeranstaltung() {
        return identVeranstaltung;
    }
    public void setIdentVeranstaltung(int identVeranstaltung) {
        this.identVeranstaltung = identVeranstaltung;
    }
    public int getGehoertZuElement() {
        return gehoertZuElement;
    }
    public void setGehoertZuElement(int gehoertZuElement) {
        this.gehoertZuElement = gehoertZuElement;
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
    public String getTextIntern() {
        return textIntern;
    }
    public void setTextIntern(String textIntern) {
        this.textIntern = textIntern;
    }
    public int getEinrueckungsEbene() {
        return einrueckungsEbene;
    }
    public void setEinrueckungsEbene(int einrueckungsEbene) {
        this.einrueckungsEbene = einrueckungsEbene;
    }
    public int getAktivierungsStatus() {
        return aktivierungsStatus;
    }
    public void setAktivierungsStatus(int aktivierungsStatus) {
        this.aktivierungsStatus = aktivierungsStatus;
    }
    public int getWirdVerwendetWennVorgaengerAktivierungsStatusGleich() {
        return wirdVerwendetWennVorgaengerAktivierungsStatusGleich;
    }
    public void setWirdVerwendetWennVorgaengerAktivierungsStatusGleich(int wirdVerwendetWennVorgaengerAktivierungsStatusGleich) {
        this.wirdVerwendetWennVorgaengerAktivierungsStatusGleich = wirdVerwendetWennVorgaengerAktivierungsStatusGleich;
    }
    public int getWirdVerwendetWennGattung() {
        return wirdVerwendetWennGattung;
    }
    public void setWirdVerwendetWennGattung(int wirdVerwendetWennGattung) {
        this.wirdVerwendetWennGattung = wirdVerwendetWennGattung;
    }
    public int getElementTyp() {
        return elementTyp;
    }
    public void setElementTyp(int elementTyp) {
        this.elementTyp = elementTyp;
    }
    public int getElementDesign() {
        return elementDesign;
    }
    public void setElementDesign(int elementDesign) {
        this.elementDesign = elementDesign;
    }
    public int getVorbelegenMit() {
        return vorbelegenMit;
    }
    public void setVorbelegenMit(int vorbelegenMit) {
        this.vorbelegenMit = vorbelegenMit;
    }
    public int getEingabezwang() {
        return eingabezwang;
    }
    public void setEingabezwang(int eingabezwang) {
        this.eingabezwang = eingabezwang;
    }
    public String getDefaultWert() {
        return defaultWert;
    }
    public void setDefaultWert(String defaultWert) {
        this.defaultWert = defaultWert;
    }
    public String getTextExtern() {
        return textExtern;
    }
    public void setTextExtern(String textExtern) {
        this.textExtern = textExtern;
    }
    public int getMinimalWert() {
        return minimalWert;
    }
    public void setMinimalWert(int minimalWert) {
        this.minimalWert = minimalWert;
    }
    public int getMaximalWert() {
        return maximalWert;
    }
    public void setMaximalWert(int maximalWert) {
        this.maximalWert = maximalWert;
    }
    public int getMaximalSumme() {
        return maximalSumme;
    }
    public void setMaximalSumme(int maximalSumme) {
        this.maximalSumme = maximalSumme;
    }
    public String getMeldungWennMaximalSummeUeberschritten() {
        return meldungWennMaximalSummeUeberschritten;
    }
    public void setMeldungWennMaximalSummeUeberschritten(String meldungWennMaximalSummeUeberschritten) {
        this.meldungWennMaximalSummeUeberschritten = meldungWennMaximalSummeUeberschritten;
    }
    public List<EclVeranstaltungenElement> getVeranstaltungenElementListe() {
        CaBug.druckeLog("Länge Liste="+veranstaltungenElementDetailListe.size(), logDrucken, 10);
        return veranstaltungenElementListe;
    }
    public void setVeranstaltungenElementListe(List<EclVeranstaltungenElement> veranstaltungenElementListe) {
        this.veranstaltungenElementListe = veranstaltungenElementListe;
    }
    public List<EclVeranstaltungenElementDetail> getVeranstaltungenElementDetailListe() {
        CaBug.druckeLog("Element="+this.identElement+" Länge Liste="+veranstaltungenElementDetailListe.size(), logDrucken, 10);
        return veranstaltungenElementDetailListe;
    }
    public void setVeranstaltungenElementDetailListe(List<EclVeranstaltungenElementDetail> veranstaltungenElementDetailListe) {
        this.veranstaltungenElementDetailListe = veranstaltungenElementDetailListe;
    }

//    public List<EclVeranstaltungenElement> getVeranstaltungenElementAnzeigenListe() {
//        return veranstaltungenElementAnzeigenListe;
//    }
//
//    public void setVeranstaltungenElementAnzeigenListe(List<EclVeranstaltungenElement> veranstaltungenElementAnzeigenListe) {
//        this.veranstaltungenElementAnzeigenListe = veranstaltungenElementAnzeigenListe;
//    }

    public String getEingabeWert() {
        return eingabeWert;
    }

    public void setEingabeWert(String eingabeWert) {
        this.eingabeWert = eingabeWert;
    }

    public List<EhJsfSelectItem> getSelectItemListeFuerRadiobutton() {
        if (selectItemListeFuerRadiobutton!=null) {
            CaBug.druckeLog("Element="+this.identElement+" Länge Liste="+selectItemListeFuerRadiobutton.size(), logDrucken, 10);
        }
        return selectItemListeFuerRadiobutton;
    }

    public void setSelectItemListeFuerRadiobutton(List<EhJsfSelectItem> selectItemListeFuerRadiobutton) {
        this.selectItemListeFuerRadiobutton = selectItemListeFuerRadiobutton;
    }

    public int getWirdVerwendetWennVorgaengerGleich() {
        return wirdVerwendetWennVorgaengerGleich;
    }

    public void setWirdVerwendetWennVorgaengerGleich(int wirdVerwendetWennVorgaengerGleich) {
        this.wirdVerwendetWennVorgaengerGleich = wirdVerwendetWennVorgaengerGleich;
    }

    public String getTextExternNach() {
        return textExternNach;
    }

    public void setTextExternNach(String textExternNach) {
        this.textExternNach = textExternNach;
    }



}
