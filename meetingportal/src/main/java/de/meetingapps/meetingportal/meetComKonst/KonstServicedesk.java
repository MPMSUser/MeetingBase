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
package de.meetingapps.meetingportal.meetComKonst;

public class KonstServicedesk {

    public final static int PERSONENART_ALLE=0;
    public final static int PERSONENART_AKTIONAER = 1;
    public final static int PERSONENART_VERTRETER = 2;
    public final static int PERSONENART_UNBEKANNT = 3;
    public final static int PERSONENART_GAST_OHNE_ANMELDUNG = 4;
    public final static int PERSONENART_GAST_MIT_ANMELDUNG = 6;
    public final static int PERSONENART_BEGLEITPERSON = 5;

    
    
    
    /***************************************Funktionscodes für Buttons und Funktionsauswahl********************/
//    /**ZU einer bestehenden Gastkarte wird eine Ersatzkarte ausgestellt. Evtl. vorhandene zugeordnete Gastkarten
//     * werden gesperrt.
//     * Falls die Karte präsent ist, wird mit der bisheren Karte ein Abgang gebucht und mit der neuen ein
//     * Wiederzugang.
//     */
//    public final int FUNKTION_GAST_ERSATZKARTE_AUSSTELLEN=1;
//    
//    /**Zu einer bestehenden Gastmeldung wird eine weitere Gastmeldung erstellt und eine Gastkarte zugeordnet. Die bisherige Gastmeldung
//     * bleibt vollkommen unverändert.
//     */
//    public final int FUNKTION_GAST_NEUE_GASTMELDUNG_FUER_GAST_ANLEGEN=2;
//    
    /**Die (zugeordnete) Gastkarte wird nochmals ausgedruckt - alles andere bleibt unverändert (Präsenzstatus etc.) - funktioniert also quasi immer
     * (vorausgesetzt es ist eine Gastkartennummer bereits zugeordnet),
     * ist aber nicht unbedingt sinnvoll (Mißbrauch mit ggf. verlorene Gastkarten*/
    public final static int FUNKTION_GAST_GASTKARTENDRUCK_WIEDERHOLEN=1;
//    
//    
//    
//    public final int KONST_FUNKTION_EK_NEUE_ZUORDNEN_UND_AUSDRUCKEN=3;
//    public final int KONST_FUNKTION_EK_ERSATZ_AUSSTELLEN=4;
    
    /**Die (zugeordnete) Eintrittskarte wird nochmals ausgedruckt - alles andere bleibt unverändert (Präsenzstatus etc.) - funktioniert also quasi immer
     * (vorausgesetzt es ist eine Eintrittskartennummer bereits zugeordnet),
     * ist aber nicht unbedingt sinnvoll (Mißbrauch mit ggf. verlorene Eintrittskarten*/
    public final static int KONST_FUNKTION_EK_DRUCK_WIEDERHOLEN=11;
//    
//    public final int KONST_FUNKTION_SK_NACHDRUCK_VOR_HV=999;
//    public final int KONST_FUNKTION_SK_NACHDRUCK_BEI_ERSTZUGANG=999;
//    /**Bei dieser Funktion müssen bei der Ausführung alle aktiven Stimmkarten der jeweiligen Gattung durchgearbeitet werden, also:
//     * > Falls Tausch 1:1: Ersatz-EK vergeben
//     * > vor der HV gedruckte nachdrucken
//     * > Bei Erstzugang ausgedruckte Nachdrucken
//     * > Tausch beliebig - stornieren der bisherigen, sowie Neuzuordnung einer Stimmkarte
//     */
//    public final int KONST_FUNKTION_SK_ERSETZEN=999;
//   
//    
    public final static int KONST_FUNKTION_SK_DRUCK_ERSTZUGANG_WIEDERHOLEN=21;

}
