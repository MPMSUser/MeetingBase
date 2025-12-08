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

import de.meetingapps.meetingportal.meetComKonst.KonstWillenserklaerung;

/**Für Statusabfrage der Willenserklärungen (BlWillenserklaerungStatus): "interpretierte" Willenserklärung, d.h. für
 * Oberfläche (Aktionärssicht) weiterverarbeitbar.
 * 
 * Zusammengefaßt werden:
 * @author N.N
 *
 */
public class EclWillenserklaerungStatusNeu implements Serializable {
    private static final long serialVersionUID = 8496070886800319394L;

    public List<EclWillenserklaerungStatusTextElement> textListe = new LinkedList<>();
    public List<String> textListeIntern = new LinkedList<>();

    /**Art der Willenserklärung, die durch diesen Satz verkörpert wird*/
    public int willenserklaerung = 0;

    public boolean liefereWillenserklaerungIstBriefwahl() {
        if (willenserklaerung==KonstWillenserklaerung.briefwahl) {
            return true;
        }
        return false;
    }
    
    /**Ein Satz mit istLeerDummy=true wird in die Willenserklärungsliste eingefügt, wenn keinerlei andere Willenserkläerungen zu
     * dieser Anmeldung gegeben wurden. 
     */
    public boolean istLeerDummy = false;

    public boolean aendernIstZulaessig = false;
    public boolean stornierenIstZulaessig = false;
    
    /**Falls die zugeordnete Sammelkarte (zusatzfeld 5) für Änderungen / Widerruf gesperrt ist,
     * dann true (abhängig auch von Parameterstellung)
     */
    public boolean gesperrt=false;
    
    /**z.B. für Eintrittskarten, die auf den "Anzeiger" ausgestellt sind; diese dürfen von ihm zwar nicht
     * verändert / storniert werden, aber durchaus in der Detailanzeige erneut ausgedruckt werden
     */
    public boolean detailAnzeigeIstZulaessig = false;

    public boolean stornierenIstZulaessigPortal = false;
    public boolean aendernIstZulaessigPortal = false;

    /*Ab hier noch nicht überprüft*/
    /*XXX*/

    public int willenserklaerungIdent = 0;

    /**Zweit-Willenserklärung-ID für "Kombi-Willenserklärung", aktuell nur für
     * Eintrittskarte und Vollmacht - in diesem Fall enthält willenserklaerungIdent2 die ident der
     * Vollmachts-Willenserklärung (wird für Storno benötigt!)
     */
    public int willenserklaerungIdent2 = 0;

    /**Versandart der Eintrittskarte analog zu EclWillenserklaerungZusatz*/
    public int versandart = 0;

    /**Für Eintrittskarten/Gastkarten*/
    public String zutrittsIdent = "";
    public String zutrittsIdentNeben = "";

    /**Ident der Person, die die Vollmacht gegeben hat*/
    public int willenserklaerungGeberIdent;

    /**Für Vollmachten - wird u.benötigt für spätere Stornierung - wird u.a. benötigt für spätere Stornierung.
     * (Früher: personenNatJurIdent)*/
    public int bevollmaechtigterDritterIdent;

    //	public List<String> textListe;
    //	public List<String>	textListeEN; /*Englische Version der Textliste. Achtung, derzeit nicht mandantenabhängig übersetzbar!*/

    public boolean storniert = false;

    /**V2. Wird aktuell nicht in EclM übertragen!*/
    public boolean veraendert = false;

    /**V2. Wird aktuell nicht in EclM übertragen!
     * Verweis (innerhalb der Liste im Speicher) auf die nachfolgende (ergänzende/korrigierende/stornierende) Willenserklärung*/
    public int verweisAufNachfolger = -1;

    /**V2. Wird aktuell nicht in EclM übertragen!
     * Verweis (innerhalb der Liste im Speicher) auf die vorhergehende (ergänzte/korrigierte/stornierte) Willenserklärung*/
    public int verweisAufVorgaenger = -1;

    /**=1 => dedizierte vorhanden
     * =2 => gemäß Vorschlag vorhanden
     * =3 => nur freie
     */
    public int weisungenSind = 0;

    /**Der folgende Wert wird nur von BlV2WillenserklaerungStatus gefüllt - bei entsprechender Parameterstellung.
     * Sonst ist er null! Wird auch aktuell nicht in EclWillenserklaerungStatusM mit übertragen!!!!
     */
    public EclWillenserklaerung eclWillenserklaerung = null;

    /**Der folgende Wert wird nur von BlV2WillenserklaerungStatus gefüllt - bei entsprechender Parameterstellung.
     * Sonst ist er null! Wird auch aktuell nicht in EclWillenserklaerungStatusM mit übertragen!!!!
     */
    public EclWillenserklaerungZusatz eclWillenserklaerungZusatz = null;

    /**Der folgende Wert wird nur von BlV2WillenserklaerungStatus gefüllt - bei entsprechender Parameterstellung.
     * Sonst ist er null! Wird auch aktuell nicht in EclWillenserklaerungStatusM mit übertragen!!!!
     */
    public EclPersonenNatJur eclPersonenNatJurVertreter = null;
    public EclWeisungMeldung eclWeisungMeldung = null;

    public EclWillenserklaerungStatusNeu() {
    }

    /********************Standard getter und setter********************************/
    public int getWillenserklaerungIdent() {
        return willenserklaerungIdent;
    }

    public void setWillenserklaerungIdent(int willenserklaerungIdent) {
        this.willenserklaerungIdent = willenserklaerungIdent;
    }

    public int getWillenserklaerungIdent2() {
        return willenserklaerungIdent2;
    }

    public void setWillenserklaerungIdent2(int willenserklaerungIdent2) {
        this.willenserklaerungIdent2 = willenserklaerungIdent2;
    }

    public int getWillenserklaerung() {
        return willenserklaerung;
    }

    public void setWillenserklaerung(int willenserklaerung) {
        this.willenserklaerung = willenserklaerung;
    }

    public int getVersandart() {
        return versandart;
    }

    public void setVersandart(int versandart) {
        this.versandart = versandart;
    }

    public String getZutrittsIdent() {
        return zutrittsIdent;
    }

    public void setZutrittsIdent(String zutrittsIdent) {
        this.zutrittsIdent = zutrittsIdent;
    }

    public String getZutrittsIdentNeben() {
        return zutrittsIdentNeben;
    }

    public void setZutrittsIdentNeben(String zutrittsIdentNeben) {
        this.zutrittsIdentNeben = zutrittsIdentNeben;
    }

    public int getWillenserklaerungGeberIdent() {
        return willenserklaerungGeberIdent;
    }

    public void setWillenserklaerungGeberIdent(int willenserklaerungGeberIdent) {
        this.willenserklaerungGeberIdent = willenserklaerungGeberIdent;
    }

    public int getBevollmaechtigterDritterIdent() {
        return bevollmaechtigterDritterIdent;
    }

    public void setBevollmaechtigterDritterIdent(int bevollmaechtigterDritterIdent) {
        this.bevollmaechtigterDritterIdent = bevollmaechtigterDritterIdent;
    }

    public List<EclWillenserklaerungStatusTextElement> getTextListe() {
        return textListe;
    }

    public void setTextListe(List<EclWillenserklaerungStatusTextElement> textListe) {
        this.textListe = textListe;
    }

    public boolean isStorniert() {
        return storniert;
    }

    public void setStorniert(boolean storniert) {
        this.storniert = storniert;
    }

    public boolean isVeraendert() {
        return veraendert;
    }

    public void setVeraendert(boolean veraendert) {
        this.veraendert = veraendert;
    }

    public int getVerweisAufNachfolger() {
        return verweisAufNachfolger;
    }

    public void setVerweisAufNachfolger(int verweisAufNachfolger) {
        this.verweisAufNachfolger = verweisAufNachfolger;
    }

    public int getVerweisAufVorgaenger() {
        return verweisAufVorgaenger;
    }

    public void setVerweisAufVorgaenger(int verweisAufVorgaenger) {
        this.verweisAufVorgaenger = verweisAufVorgaenger;
    }

    public int getWeisungenSind() {
        return weisungenSind;
    }

    public void setWeisungenSind(int weisungenSind) {
        this.weisungenSind = weisungenSind;
    }

    public boolean isAendernIstZulaessig() {
        return aendernIstZulaessig;
    }

    public void setAendernIstZulaessig(boolean aendernIstZulaessig) {
        this.aendernIstZulaessig = aendernIstZulaessig;
    }

    public boolean isStornierenIstZulaessig() {
        return stornierenIstZulaessig;
    }

    public void setStornierenIstZulaessig(boolean stornierenIstZulaessig) {
        this.stornierenIstZulaessig = stornierenIstZulaessig;
    }

    public boolean isDetailAnzeigeIstZulaessig() {
        return detailAnzeigeIstZulaessig;
    }

    public void setDetailAnzeigeIstZulaessig(boolean detailAnzeigeIstZulaessig) {
        this.detailAnzeigeIstZulaessig = detailAnzeigeIstZulaessig;
    }

    public boolean isIstLeerDummy() {
        return istLeerDummy;
    }

    public void setIstLeerDummy(boolean istLeerDummy) {
        this.istLeerDummy = istLeerDummy;
    }

    public EclWillenserklaerung getEclWillenserklaerung() {
        return eclWillenserklaerung;
    }

    public void setEclWillenserklaerung(EclWillenserklaerung eclWillenserklaerung) {
        this.eclWillenserklaerung = eclWillenserklaerung;
    }

    public EclWillenserklaerungZusatz getEclWillenserklaerungZusatz() {
        return eclWillenserklaerungZusatz;
    }

    public void setEclWillenserklaerungZusatz(EclWillenserklaerungZusatz eclWillenserklaerungZusatz) {
        this.eclWillenserklaerungZusatz = eclWillenserklaerungZusatz;
    }

    public EclPersonenNatJur getEclPersonenNatJurVertreter() {
        return eclPersonenNatJurVertreter;
    }

    public void setEclPersonenNatJurVertreter(EclPersonenNatJur eclPersonenNatJurVertreter) {
        this.eclPersonenNatJurVertreter = eclPersonenNatJurVertreter;
    }

    public EclWeisungMeldung getEclWeisungMeldung() {
        return eclWeisungMeldung;
    }

    public void setEclWeisungMeldung(EclWeisungMeldung eclWeisungMeldung) {
        this.eclWeisungMeldung = eclWeisungMeldung;
    }

    public boolean isStornierenIstZulaessigPortal() {
        return stornierenIstZulaessigPortal;
    }

    public void setStornierenIstZulaessigPortal(boolean stornierenIstZulaessigPortal) {
        this.stornierenIstZulaessigPortal = stornierenIstZulaessigPortal;
    }

    public boolean isAendernIstZulaessigPortal() {
        return aendernIstZulaessigPortal;
    }

    public void setAendernIstZulaessigPortal(boolean aendernIstZulaessigPortal) {
        this.aendernIstZulaessigPortal = aendernIstZulaessigPortal;
    }

    public boolean isGesperrt() {
        return gesperrt;
    }

    public void setGesperrt(boolean gesperrt) {
        this.gesperrt = gesperrt;
    }


}
