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

/**"Rucksack" für EclWillenserklaerung. 
 * Enthält Felder, die i.d.R. für die "schnelle" Verarbeitung (z.B. auf der HV) nicht mehr benötigt werden.
 * Primärschlüssel ist identisch mit EclWillenserklaerung.
 */
public class EclWillenserklaerungZusatz implements Serializable {
    private static final long serialVersionUID = -8073073618798727202L;

    public int mandant = 0;

    /**Primärschlüssel zusammen mit mandant - wird intern vergeben
     * Identisch mit EclWillenserklaerung.willenserklaerungIdent!*/
    public int willenserklaerungIdent = 0;
    /**Versionsnummerierung zum Erkennen, ob DB-Satz von anderem User upgedatet wurde.
     * Darf nur von Db-Verwaltung selbst verwendet werden!*/
    public long db_version = 0;

    /*Die folgenden beiden Felder sind (meldungsIdent und meldungsIdentGast) wg. Ein-Lese-Funktion
     * redundant vorhanden
     */

    /**Fremdschlüssel zu tbl_meldungen*/
    public int meldungsIdent = 0;
    /**Fremdschlüssel zu tbl_meldungen*/
    public int meldungsIdentGast = 0;

    /**Kennzeichnung der Willenserklärung. Alter Name: veraenderung.
     * In Abhängigkeit der willenserklaerung werden andere Felder gefüllt 
     * oder eben auch nicht.
     * Wenn Willenserklärung=Storno, dann "verweisAufWillenserklaerung*/
    public int willenserklaerung = 0;

    /****************Felder für anmeldungAusAktienregister*****************************************************/
    /**Aktienregister-Eintrag, aus dem heraus die Anmeldung durchgeführt wurde (aus pEclAktienregisterEintrag*/
    public int aktienregisterIdent = 0;

    /**Aktienanzahl, die angemeldet wird = pAktienAnmelden*/
    public long aktienAnmelden = 0;

    /**Anmeldung fix = pAnmeldungFix (1=true, 0 = false)*/
    public int anmeldungFix = 0;

    /**Anzahl Anmeldungen, auf die die Aktienzahl aufgesplittet werden soll = pAnzahlAnmeldungen*/
    public int anzahlAnmeldungen = 0;

    /**=1 Anmeldung ist mittlerweile storniert (wg. einfacher Auswertemöglichkeit bei Namensaktien in diesem Table!)*/
    public int anmeldungIstStorniert = 0;

    /******************************************Felder für Eintrittskartenausstellung*************************/

    /**Versandart
     * 0 = keinerlei Weiterverarbeitung erforderlich (alles bereits vom Programm adhoc erledigt
     * 1 = Aufnahme in Sammelbatch an Adresse im Aktienregister - bei nächstem Drucklauf ausdrucken und versenden
     * 2 = Aufnahme in Sammelbatch an Versandadresse (in versandardresse1 bis 5) - bei nächstem Drucklauf ausdrucken und versenden
     * 3 = Online-Ausdruck (im Portal) erfolgt
     * 4 = Versand per Email (im Portal) erfolgt
     * 5 = automatische Aufnahme in App
     * 6 = selbe Versandadresse wie zeitgleich ausgestellte Eintrittskarte (nur Gastkarte!)
     */
    public int versandartEK = 0;

    /**Versandadresse*/
    public String versandadresse1 = "";
    public String versandadresse2 = "";
    public String versandadresse3 = "";
    public String versandadresse4 = "";
    public String versandadresse5 = "";

    /**Versand an Emailadresse*/
    public String emailAdresseEK = "";

    /**Falls Eintrittskarte mit Vollmacht an Dritte ausgestellt wurde: hier Verweis auf
     * die PersonNatJur, in der der Bevollmächtigte eingetragen ist.
     * Dient der Vereinfachung des Druckvorgangs etc. (ist eigentlich redundant,
     * da noch Folgewillenserklärung für die Vollmacht vorhanden ist)
     */
    public int identVertreterPersonNatJur = 0;

    /**************************************Felder für Eintrittskartendruck***********************************/
    /**=0 => noch keine Überprüfung stattgefunden
     * =1 => die Versandadresse für die Eintrittskarte wurde mit dem System bereits "sichtgeprüft" - gespeicherte
     * (obige) Versanddaten blieben unverändert erhalten
     * =2 => wie 1, aber neue (vorrangige) Versanddaten wie unten
     * */
    public int versandadresseUeberprueft = 0;

    /**wie versandartEK; zusätzlich:
     * 98 = an im Aktienregister ursprünglich eingetragene Register, auch wenn abweichende eingetragen ist - derzeit 
     * 			nicht unterstützt! Muß halt dann manuell übertragen werden!
     * 99 = neue abweichende Versandadresse
     */
    public int versandartEKUeberprueft = 0;
    public String versandadresse1Ueberprueft = "";
    public String versandadresse2Ueberprueft = "";
    public String versandadresse3Ueberprueft = "";
    public String versandadresse4Ueberprueft = "";
    public String versandadresse5Ueberprueft = "";

    /**Nur relevant bei Versandart 1 und 2;
     * 1 = bereits ausgedruckt*/
    public int eintrittskarteWurdeGedruckt = 0;

    public String erstesDruckDatum = "";
    public String letztesDruckDatum = "";

    /***********************************Sonstiges*****************************************************/

    /**Begründung für Abgabe der Willenserklärung - standardisierte Nummer*/
    public int grundNr = 0;

    /**Begründung für Abgabe der Willenserklärung - freier Text (Länge 200)*/
    public String grundText = "";

    /**Verweis auf Willenserklärung (z.B. Scan-Datei) (Länge 100)*/
    public String quelle = "";

}
