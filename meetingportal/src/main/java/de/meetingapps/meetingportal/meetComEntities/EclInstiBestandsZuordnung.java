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

/**Grundsätzliches:
 * 
 * Zuordnung generell zu Insti
 * ===========================
 * 1 Aktienregistereintrag kann vollständig einem einzigen Insti zugeordnet werden. (= alle Meldungen dazu)
 * 
 * 1 Aktienregistereintrag kann nur teilweise einem Insti zugeordnet werden.
 * In diesem Fall wird die MeldeIdent mit eingetragen (so schon vorhanden!) - d.h. es darf dann nur eine Meldung zu diesem Bestand geben.
 * Wenn mehrere Meldungen erforderlich sind zu so einem Bestand, dann für jede Meldung eine separate Teil-Zuordnung durchführen! D.h. eine Zuordnung muß ggf.
 * beim Erzeugen von Meldungen aufgesplittet werden und die MeldeIdents eingetragen werden.
 *
 * Zuordnung zu User
 * =================
 * Es gibt auf jeden Fall einen Satz EclInstibestandsZuordnung je Zuordnung - damit wird der Bestand einem Insti zugeordnet.
 * identUserLogin in diesem Satz =0
 * 
 * Es kann weitere Sätze geben (dann alle mit der gleichen ident, und auch sonstige Felder gleich - außer identUserLogin), bei denen
 * dann identUserLogin!=0 ist bzw. auf einen EclUserLogin verweist. Damit wird dann dieser Bestand explizit einem User des Insti
 * zugeordnet.
 * Ob bei einem User dann alle diesem Insti zugeordneten Bestand, oder nur die ihm speziell zugeordneten Bestände angezeigt werden,
 * hängt von der Berechtigung des jeweiligen Users ab (alle oder nur individuell zugeordnete).
 * 
 * Hinweis: diese Userzuordnung bezieht sich auf Mandanten-Übergreifende User der Instis, für den Zugriff der Instis über das Emittentenportal, und ist nicht zu verwechseln
 * mit der Insti-Zuordnung von einer Gastkennung aus (diese läuft unabhängig von der hier beschriebenen User-Zuordnung)
 */
public class EclInstiBestandsZuordnung {

    public int mandant = 0;

    /**Identifikation der Zuordnung. Nicht Eindeutig - siehe Zuordnung zu User*/
    public int ident = 0;

    /**Beschreibung, falls z.B. Insti abweichend vom eingetragenen Aktionär ist
     * LEN=100*/
    public String beschreibung = "";

    /**1=Aktienregisterbestand wurde zugeordnet, 2=Meldungen zugeordnet*/
    public int zugeordnetRegisterOderMeldungen = 0;

    /**Der Bestand, der zugeordnet wird*/
    public int identAktienregister = 0;

    /**-1 => der Gesamtbestand wird zugeordnet; 
     * sonst: nur Teilbestand mit der Stimmenanzahl. In diesem Fall muß dann MeldeIdent berücksichtigt werden! 
     */
    public long zugeordneteStimmen = 0;

    /**=0 => entweder Gesamtbestand zugeordnet (d.h. zugeordneteStimmen==-1); oder noch keine Meldung vorhanden*/
    public int identMeldung = 0;

    /**Verweis auf EclInsti - d.h. Zuordnung zu dieser Insti*/
    public int identInsti = 0;

    /**!=0 => explizite Zuordnung zu einem User des Insti*/
    public int identUserLogin = 0;

    /**Die folgenden Felder werden nur gefüllt im Satz mit identUserLogin=0. Sie beinhalten
     * "Drucklaufnummern" als Verarbeitet-Kennzeichen*/
    /**in Sammelanmeldebogen gedruckt*/
    public int verarbeitetSammelAnmeldungGedruckt = 0;
    public int verarbeitet2 = 0;
    public int verarbeitet3 = 0;
    public int verarbeitet4 = 0;
    public int verarbeitet5 = 0;

}
