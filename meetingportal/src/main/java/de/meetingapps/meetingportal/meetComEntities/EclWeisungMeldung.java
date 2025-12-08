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

import de.meetingapps.meetingportal.meetComKonst.KonstStimmart;

/*TODOLANG*/
/*  * 
 * Sollte beim "Raw-Interpretieren" gleich mitgemacht werden.
 * > Übertragen der Abstimmungsempfehlung/Gesmatmarkierung in Weisung.
 * > Konsistenzprüfung der Weisung
 * 
 * Wenn sich Abstimmvorschlag ändert => Weisungen anpassen
 */

/**Für Einzelaktionäre und Sammelkarten.
 * 
 * Bei Sammelkarten immer Splitting!
 */

public class EclWeisungMeldung implements Serializable {
    private static final long serialVersionUID = -7642268613249596272L;

    /**Mandant**/
    public int mandant = 0;

    /**Ident dieser Weisung**/
    public int weisungIdent = 0;

    /**Ident der Meldung, zu der die Weisung gehört**/
    public int meldungsIdent = 0;

    /**Ident der Sammelkarte, der die Meldung zugeordnet ist
     * Für den "Summenkopf" einer Sammelkarte ist dies 0. D.h. Gefüllt wird dieses Feld nur bei Einzelaktionären, die einer
     * Sammelkarte zugeordnet sind**/
    public int sammelIdent = 0;

    /**die Sammelkarte in sammelIdent ist eine:
     *1=KIAV
     *2=SRV
     *3=organisatorisch
     *4=Briefwahl
     *5=Dauervollmacht*/
    public int skIst = 0;

    /**Ident der Willenserklärung, die diese Zuordnung ausgelöst hat.
     * Bei Sammelkarten: 0**/
    public int willenserklaerungIdent = 0;

    /**0=> kein Weisungssplit-Satz vorhanden; 1=> vorhanden (mit gleicher weisungIdent)
     * Falls Split-Weisung vorhanden, dann sind keine Gesamt-Markierungen (weder JN etc
     * noch gemäßEmpfehlung) zulässig!**/
    public int weisungSplit = 0;

    /**Status der Zuordnung
     * =1 => aktiv
     * =0 => vorhanden, aber nicht aktiv (z.B. auch wg. Pending)
     * =-1 => widerrufen
     * =-2 => mittlerweile geändert
     * Hinweis: bei Sammelkarten gibt es immer nur einen einzigen Satz weisungMeldung in der Datenbank.
     */
    public int aktiv = 0;

    /**Ist Weisung =1 / Ist Briefwahl =2**/
    public int istWeisungBriefwahl = 0;

    /**Gemäß fremder Empfehlung - soweit nicht einzeln markiert.
     * Negative Zahl = Immer gegen die Empfehlung.
     * 
     * Hier wird immer der Verweis auf den zugrunde liegenden Empfehlungssatzes eingetragen. Ggf.
     * wird dieser Verweis angepaßt, falls sich ein Folge-Empfehlungssatz ergibt. Historie ist in diesem
     * Fall nur nachvollziehbar zusammen mit Raw-Satz (=ursprüngliche Abgabe) und Veränderungen der 
     * Empfehlungssätze
     * 
     * Damit hier ein "negativer" Satz (also "gegen") erlaubt ist, muß der Vollmachtnehmer sowohl "gemäß
     * fremder Empfehlung" als auch "gemäß dedizierter Weisung" zulassen. Hintergrund: er muß beim
     * Kontrollieren ja auf fremde Empfehlung zugreifen. Und "Gegen" ist eher Dediziert.*/
    public int gemaessFremdenAbstimmungsVorschlagIdent = 0;

    /**Gemäß eigener Empfehlung - soweit nicht einzeln markiert.
     * Negative Zahl = Immer gegen die Empfehlung.
     * 
     * Hier wird immer der Verweis auf den zugrunde liegenden Empfehlungssatzes eingetragen. Ggf.
     * wird dieser Verweis angepaßt, falls sich ein Folge-Empfehlungssatz ergibt. Historie ist in diesem
     * Fall nur nachvollziehbar zusammen mit Raw-Satz (=ursprüngliche Abgabe) und Veränderungen der 
     * Empfehlungssätze
     * 
     * Damit hier ein "negativer" Satz (also "gegen") erlaubt ist, muß der Vollmachtnehmer sowohl "gemäß
     * fremder Empfehlung" als auch "gemäß dedizierter Weisung" zulassen. Hintergrund: er muß beim
     * Kontrollieren ja auf fremde Empfehlung zugreifen. Und "Gegen" ist eher Dediziert.*/
    public int gemaessEigenemAbstimmungsVorschlagIdent = 0;

    /**"Alle Ja, Alle Nein etc.
     * Integer-Wert = Wert der Stimmart wie in EnStimmart definiert**/
    public int stimmartGesamt = 0;

    /**Aktualisierung mit Empfehlungsveränderung / neuen Beschlußvorschlägen
     * 0 = nein
     * 1 = Ja bei Änderungen von bestehenden Vorschlägen
     * 2 = Ja bei neuen Beschlußvorschlägen
     * 3 = in beiden Fällen**/
    public int aktualisieren = 0;

    /**Einzele Weisungsabgaben - bereits interpretiert
     * Integer-Wert = Wert der Stimmart wie in EnStimmart definiert**/
    public int[] abgabe = new int[200];

    /**Falls ==1, dann wurde diese Weisung durch Interpretation eine Gesamtmarkierung eingetragen (und wird
     * ggf. angepaßt)*/
    public int[] abgabeLautGesamt = new int[200];

    /**Verweis auf zugehörigen EclWeisungMeldungSplit. Kann null sein, wenn keine Split-Weisungen vorhanden sind**/
    public EclWeisungMeldungSplit weisungMeldungSplit = null;

    public EclWeisungMeldung() {
        int i;
        for (i = 0; i < 200; i++) {
            abgabe[i] = 0;
            abgabeLautGesamt[i] = 0;
        }
    }

    /**Freie Weisungen vorhanden?**/
    public boolean hatFreieWeisungen() {
        if (this.stimmartGesamt == KonstStimmart.frei) {
            return true;
        }
        int i;
        for (i = 0; i < 200; i++) {
            if (this.abgabeLautGesamt[i] == 0 && this.abgabe[i] == KonstStimmart.frei) {
                return true;
            }
            if (this.abgabe[i] == KonstStimmart.splitLiegtVor) {
                if (this.weisungMeldungSplit.abgabe[i][KonstStimmart.frei] != 0) {
                    return true;
                }
            }
        }
        return false;
    }

    /**Dedizierte Weisungen (voll gebunden) vorhanden?
     * Als dedizierte Weisungen gelten:
     * > Einzelne Weisung (oder Split) Ja, Nein, Enthaltung, Ungueltig, NichtTeilnahme
     * > GegenFremdenAbstimmungsvorschlag oder GegenEigenenAbstimmungsvorschlag
     * > stimmartGesamt verwendet
     * **/
    public boolean hatDedizierteWeisungen() {
        if (this.gemaessFremdenAbstimmungsVorschlagIdent < 0) {
            return true;
        }
        if (this.gemaessEigenemAbstimmungsVorschlagIdent < 0) {
            return true;
        }
        if (this.stimmartGesamt == KonstStimmart.ja || this.stimmartGesamt == KonstStimmart.nein
                || this.stimmartGesamt == KonstStimmart.enthaltung || this.stimmartGesamt == KonstStimmart.ungueltig
                || this.stimmartGesamt == KonstStimmart.nichtTeilnahme) {
            return true;
        }
        int i;
        for (i = 0; i < 200; i++) {
            if (this.abgabeLautGesamt[i] == 0 && (this.abgabe[i] == KonstStimmart.ja
                    || this.abgabe[i] == KonstStimmart.nein || this.abgabe[i] == KonstStimmart.enthaltung
                    || this.abgabe[i] == KonstStimmart.ungueltig || this.abgabe[i] == KonstStimmart.nichtTeilnahme)) {
                return true;
            }
            if (this.abgabe[i] == KonstStimmart.splitLiegtVor) {
                if (this.weisungMeldungSplit.abgabe[i][KonstStimmart.ja] != 0
                        || this.weisungMeldungSplit.abgabe[i][KonstStimmart.nein] != 0
                        || this.weisungMeldungSplit.abgabe[i][KonstStimmart.enthaltung] != 0
                        || this.weisungMeldungSplit.abgabe[i][KonstStimmart.ungueltig] != 0
                        || this.weisungMeldungSplit.abgabe[i][KonstStimmart.nichtTeilnahme] != 0) {
                    return true;
                }
            }
        }
        return false;
    }

    /**Weisung gemäß eigener Empfehlung (fix, ohne Anpassung bei Veränderungen)vorhanden?**/
    public boolean hatWeisungenGemEigenerEmpfehlungFix() {
        if (this.gemaessEigenemAbstimmungsVorschlagIdent > 0 && this.aktualisieren == 0) {
            return true;
        }
        return false;
    }

    /**Weisung gemäß eigener Empfehlung (verändernd bei bestehenden Beschlussvorschlägene)vorhanden?**/
    public boolean hatWeisungenGemEigenerEmpfehlungFlexBestehend() {
        if (this.gemaessEigenemAbstimmungsVorschlagIdent > 0 && (this.aktualisieren & 1) == 1) {
            return true;
        }
        return false;
    }

    /**Weisung gemäß eigener Empfehlung (verändernd bei neuen Beschlussvorschlägen) vorhanden?**/
    public boolean hatWeisungenGemEigenerEmpfehlungFlexNeu() {
        if (this.gemaessEigenemAbstimmungsVorschlagIdent > 0 && (this.aktualisieren & 2) == 2) {
            return true;
        }
        return false;
    }

    /**Weisung gemäß fremder Empfehlung (fix, ohne Anpassung bei Veränderungen)vorhanden?**/
    public boolean hatWeisungenGemFremderEmpfehlungFix() {
        if (this.gemaessFremdenAbstimmungsVorschlagIdent > 0 && this.aktualisieren == 0) {
            return true;
        }
        return false;
    }

    /**Weisung gemäß fremder Empfehlung (verändernd bei bestehenden Beschlussvorschlägene)vorhanden?**/
    public boolean hatWeisungenGemFremderEmpfehlungFlexBestehend() {
        if (this.gemaessFremdenAbstimmungsVorschlagIdent > 0 && (this.aktualisieren & 1) == 1) {
            return true;
        }
        return false;
    }

    /**Weisung gemäß fremder Empfehlung (verändernd bei neuen Beschlussvorschlägen) vorhanden?**/
    public boolean hatWeisungenGemFremderEmpfehlungFlexNeu() {
        if (this.gemaessFremdenAbstimmungsVorschlagIdent > 0 && (this.aktualisieren & 2) == 2) {
            return true;
        }
        return false;
    }

    /*Überprüfen, ob in Einzelweisungen tatsächlich Split-Weisungen vorhanden sind*/
    public boolean hatSplitWeisung() {
        if (this.weisungSplit == 1) {
            return true;
        }
        return false;
    }

}
