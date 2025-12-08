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

import java.util.LinkedList;
import java.util.List;

/**Für Statusabfrage der Willenserklärungen (BlWillenserklaerungStatus): "interpretierte" Willenserklärung, d.h. für
 * Oberfläche (Aktionärssicht) weiterverarbeitbar.
 * 
 * Zusammengefaßt werden:
 * @author N.N
 *
 */
public class EclWillenserklaerungStatus {

    public int willenserklaerungIdent = 0;

    /**Zweit-Willenserklärung-ID für "Kombi-Willenserklärung", aktuell nur für
     * Eintrittskarte und Vollmacht - in diesem Fall enthält willenserklaerungIdent2 die ident der
     * Vollmachts-Willenserklärung (wird für Storno benötigt!)
     */
    public int willenserklaerungIdent2 = 0;

    /**Art der Willenserklärung, die durch diesen Satz verkörpert wird*/
    public int willenserklaerung = 0;

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

    public List<String> textListe;
    public List<String> textListeEN; /*Englische Version der Textliste. Achtung, derzeit nicht mandantenabhängig übersetzbar!*/

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

    public boolean aendernIstZulaessig = false;
    public boolean stornierenIstZulaessig = false;

    /**z.B. für Eintrittskarten, die auf den "Anzeiger" ausgestellt sind; diese dürfen von ihm zwar nicht
     * verändert / storniert werden, aber durchaus in der Detailanzeige erneut ausgedruckt werden
     */
    public boolean detailAnzeigeIstZulaessig = false;

    /**Ein Satz mit istLeerDummy=true wird in die Willenserklärungsliste eingefügt, wenn keinerlei andere Willenserkläerungen zu
     * dieser Anmeldung gegeben wurden. 
     */
    public boolean istLeerDummy = false;

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

    public EclWillenserklaerungStatus() {
        textListe = new LinkedList<>();
        textListeEN = new LinkedList<>();
    }

}
