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

import de.meetingapps.meetingportal.meetComKonst.KonstWillenserklaerungWeg;

/**Für Einzelaktionäre.
 * 
 * Bei Sammelkarten immer Splitting! (nur so kann dynamische Stimmenänderung der Sammelkarten sauber protokolliert werden auch für Abstimmung!)
 * D.h. für Sammelkarten existiert hier ein Eintrag nur dann, wenn "geworfen" wurde. Bei den "nicht geworfenen" Abstimmungspunkten, 
 * bei denen eine Weisung vorliegt, ist abgabe[] erst mal 0 (und nicht "Split liegt vor"!)
 * 
 * Hinweis:
 * Zu Beginn der Abstimmung werden alle gespeicherten Stimmabgaben, die nicht "explizit geworfen wurden", zurückgesetzt.
 * D.h. bei der Auswertung sind "eigentlich" nur die tatsächlich geworfenen Stimmabgaben gespeichert.
 * Aber: Wg. Stimmausschluß werden (mit entsprechendem abstimmungsweg) auch bereits Stimmausschlüsse 
 * bei Aktionären, die in Sammelkarten sind, gespeichert werden. Diese müssen dann beim Auswerten
 * ignoriert werden.
 */

public class EclAbstimmungMeldung {

    /**Mandant**/
    public int mandant = 0;

    /**Ident der Meldung, zu der die Weisung gehört (=Schlüssel)**/
    public int meldungsIdent = 0;

    /**Werden aus dem Meldungssatz übernommen, um bei der Abstimmungsauswertung schneller addieren zu können*/
    public long stimmen = 0;

    /**1 bis 5. Wird aus dem Meldungssatz übernommen, um bei der Abstimmungsauswertung schneller addieren zu können*/
    public int gattung = 0;

    /**Status der Zuordnung
     * =1 => aktiv
     * =0 => vorhanden, aber nicht aktiv (wg: parallel Splitsatz vorhanden, d.h. für Auswertung: falls =0 => es zählt der Splitsatz)
    // 	 * Wichtig: 0 hat nichts!! mit Löschen oder Stornieren zu tun - das läuft ausschließlich über DbAbstimmungMeldungRaw!
     */
    public int aktiv = 0;

    /**Gemäß KonstWillenserklaerungWeg (hier konkret: auf welchem Weg wurde die aktuelle Willenserklärung erteilt.
     * Erforderlich für ggf. nötige Priorisierungen*/
    public int erteiltAufWeg = 0;

    /**Ist ein "Hilfswert": wird beim "Neuberechnen" auf 0 gesetzt. D.h. welcher Wert da drin steht,
     * kann letztendlich nicht ausgewertet werden. Dient nur dazu um festzustellen, ob eine aktuelle Stimmabgabe
     * "neuer" ist als die bisherigen.
     */
    public long zeitstempelraw = 0;

    /**Einzele Weisungsabgaben - bereits interpretiert
     * Integer-Wert = Wert der Stimmart wie in EnStimmart definiert**/
    public int[] abgabe = new int[200];
    
    /*AAAAA Abstimmungprotokoll DB*/
    /**Entstehen dieser Abstimmung - siehe KonstWillenserklaerungWeg*/
    public int[] abstimmungsweg=new int[200];

    /*AAAAA Abstimmungprotokoll DB*/
    /**Sammelkartenident, falls Abstimmung durch Sammelkarte entstanden ist*/
    public int[] abstimmungDurchSammelkarte=new int[200];

    /*AAAAA Abstimmungprotokoll DB*/
    /**Dient als Selektionskriterium für schnellere Stimmauswertung.
     * Wird auf 1 gesetzt, sobald eine wirkliche - für die Auswertung relevante - Stimmabgabe
     * erfolgt ist.
     * D.h. bei der Auswertung können alle Sätze mit ==0 ignoriert werden
     */
    public int stimmabgabeDirektErfolgt=0;
    
    public EclAbstimmungMeldung() {
        int i;
        for (i = 0; i < 200; i++) {
            abgabe[i] = 0;
        }
        for (i = 0; i < 200; i++) {
            abstimmungsweg[i] = 0;
        }
        for (i = 0; i < 200; i++) {
            abstimmungDurchSammelkarte[i] = 0;
        }
    }
    
    /**Setze Stimmabgabe auf "aktiv auf HV durch den Teilnehmer abgegeben" 
     * (also nicht durch Sammelkarte oder implizite Abgabe)*/
    public void setzeStimmabgabeAktivAbgegeben(int pPosition, int pStimmart, int pAbstimmungsweg){
       abgabe[pPosition]=pStimmart;
       abstimmungsweg[pPosition]=pAbstimmungsweg;
       abstimmungDurchSammelkarte[pPosition]=0;
    }
    
    public void zuruecksetzenStimmabgabe(int pPosition) {
        abgabe[pPosition]=0;
        abstimmungsweg[pPosition]=0;
        abstimmungDurchSammelkarte[pPosition]=0;
   }
    
    public boolean lieferePosAktivAbgegeben(int pPosition) {
        if (abstimmungsweg[pPosition]>=KonstWillenserklaerungWeg.ABSTIMMUNGSERGAENZUNG_BEGINN
                && abstimmungsweg[pPosition]<=KonstWillenserklaerungWeg.ABSTIMMUNGSERGAENZUNG_ENDE) {
            return false;
        }
        return true;
    }

}
