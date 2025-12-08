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

import java.util.List;

public class EclVeranstaltungenReportElement {

    /**Key: identReportElement, identReportSubElement, identReport, identVeranstaltung*/
    
    /**Struktur eines Reports:
     * Mehrere Einträge von eclVeranstaltungenReportElement:
     * > Genau 1 Eintrag mit elementTyp=0
     * > Genau 1 Eintrag mit elementTyp=1;
     * > Genau 1 Eintrag mit elementTyp=2;
     * > mehrere Einträge mit elementTyp=3;
     * >> Für Elemente mit wertBerechnung=2: Subelemente für die jeweiligen Werte, über die die Abbildung wertAusQuittung zu wertInReport erfolgt
     */
    
    /**Eindeutige Ident des Elements (innerhalb des Reports)*/
    public int identReportElement=0;
    public int identReportSubElement=0;
    
    /**Identifikationsnunmmer des Reports (innerhalb der Veranstaltung)*/
    public int identReport=0;
    
    /**Report gehört zu dieser Veranstaltung*/
    public int identVeranstaltung=0;
    
    /**0 => nicht aktiv
     * 1 => aktiv, wird in Report-Auswahl angezeigt
     * -1 => ist eine Vorlage zum Erzeugen neuer Reports
     */
    public int aktiv=0;
    
    /**0 = Report-Bezeichnung in Auswahl
     * 1 = Überschrift über Report
     * 2 = Export-Dateiname
     * 3 = Element in Liste
     */
    public int elementTyp=0;

    /**Bei elementTyp=3: Reihenfolge der Felder im Report. 
     * Beginnt bei 1!
     * Sonst: 0*/
    public int offsetInReport=0;
    

    /**
     * elementTyp=0 => Bezeichnung des Reports für Menüauswahl
     * elementTyp=1 => Überschriftstext
     * elementTyp=2 => Dateiname (ohne Pfad und Art, also z.B. reportVeranstaltungDialog
     * elementTyp=3 => Text, das für dieses Element in der Überschrift angezeigt wird.
     * LEN=50
     */
    public String elementBezeichnung="";
    
    /**Für elementTyp=3:
     * =1 => der Wert aus dem Quittungselement wird 1:1 übernommen
     * =2 => Werteabbildung gemäß der Reportabbildung.
     * In diesem Fall gibt es weitere 
     */
    public int wertBerechnung=0;
    
    /**Für elementTyp=3: Verweis auf EclVeranstaltungenWert, der verwendet wird
     * 
     */
    public int identElement=0;
    public int identDetail=0;
    public int identAktion=0;

    
    /**Für Subelemente: LEN jeweils=1000*/
    public String wertAusQuittung="";
    public String wertInReport="";
    
    /**********************Nicht in Datenbank*****************************/
    public List<EclVeranstaltungenReportElement> subElementListe=null;
    
    /*********************Standard getter und setter*********************************/
    
    public int getIdentReportElement() {
        return identReportElement;
    }
    public void setIdentReportElement(int identReportElement) {
        this.identReportElement = identReportElement;
    }
    public int getIdentReportSubElement() {
        return identReportSubElement;
    }
    public void setIdentReportSubElement(int identReportSubElement) {
        this.identReportSubElement = identReportSubElement;
    }
    public int getIdentReport() {
        return identReport;
    }
    public void setIdentReport(int identReport) {
        this.identReport = identReport;
    }
    public int getIdentVeranstaltung() {
        return identVeranstaltung;
    }
    public void setIdentVeranstaltung(int identVeranstaltung) {
        this.identVeranstaltung = identVeranstaltung;
    }
    public int getAktiv() {
        return aktiv;
    }
    public void setAktiv(int aktiv) {
        this.aktiv = aktiv;
    }
    public int getElementTyp() {
        return elementTyp;
    }
    public void setElementTyp(int elementTyp) {
        this.elementTyp = elementTyp;
    }
    public int getOffsetInReport() {
        return offsetInReport;
    }
    public void setOffsetInReport(int offsetInReport) {
        this.offsetInReport = offsetInReport;
    }
    public String getElementBezeichnung() {
        return elementBezeichnung;
    }
    public void setElementBezeichnung(String elementBezeichnung) {
        this.elementBezeichnung = elementBezeichnung;
    }
    public int getWertBerechnung() {
        return wertBerechnung;
    }
    public void setWertBerechnung(int wertBerechnung) {
        this.wertBerechnung = wertBerechnung;
    }
    public int getIdentElement() {
        return identElement;
    }
    public void setIdentElement(int identElement) {
        this.identElement = identElement;
    }
    public int getIdentDetail() {
        return identDetail;
    }
    public void setIdentDetail(int identDetail) {
        this.identDetail = identDetail;
    }
    public int getIdentAktion() {
        return identAktion;
    }
    public void setIdentAktion(int identAktion) {
        this.identAktion = identAktion;
    }
    public String getWertAusQuittung() {
        return wertAusQuittung;
    }
    public void setWertAusQuittung(String wertAusQuittung) {
        this.wertAusQuittung = wertAusQuittung;
    }
    public String getWertInReport() {
        return wertInReport;
    }
    public void setWertInReport(String wertInReport) {
        this.wertInReport = wertInReport;
    }
    
}
