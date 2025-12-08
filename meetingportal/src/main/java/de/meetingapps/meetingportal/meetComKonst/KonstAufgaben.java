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

public class KonstAufgaben {

    /**Sonstiges, nicht als Aufgabe definiert*/
    public static final int sonstige = 0;

    /**Aktionär hat ein neues Passwort angefordert, mit Angabe von Strasse und Ort.
     * Argument0=Aktionärsnummer
     * Argument1=Strasse
     * Argument2=Ort
     * Strasse und Ort müssen mit Aktienregistereintrag überprüft werden, dann muss neues Passwort Post an die
     * Versandadresse verschickt wer
     * 
     * Argument3=Formularvariante (leer=Standard; 2=Versand bei Erstregistrierung im Permanentportal)
     * 
     * Argument 4, 5, 6, 7 = wenn 4 nicht leer, dann stehen hier bereits die Empfängerdaten (für Versand an mehrere Personen
     * Argument8 = ggf. bereits neu vergebenes Passwort
     */
    public static final int aktionaerNeuesPasswortAdressePruefen = 1;

    /**Aktionär hat ein neues Passwort angefordert, ohne weitere Angabe.
     * Argument0=Aktionärsnummer
     * Ein neues Passwort muß an die Versandadresse verschickt werden.
     */
    public static final int aktionaerNeuesPasswort = 2;

    /**Funktioniert derzeit nicht - stattdessen auch aktionaerNeuesPasswortAdressePruefen verwenden!!!*/
    public static final int gastNeueZugangsdatenPruefen = 3;

    /**Eingang über Kontaktformular.
     * Argument0=Aktionärsnummer (wenn eingegeben)
     * Argument1=Name, Vorname
     * Argument2=Mail
     * Argument3=Telefon
     * Argument4 bis 7=Adresse
     * Argument8=Art des Anliegens
     * 
     * Der "Info-Text" steht in tbl_aufgabenLangtext
     */
    public static final int anfrageUberPortal=4;
    
    public static String getText(int pAufgabe) {
        switch (pAufgabe) {
        case 0:
            return "Sonstiges";
        case 1:
            return "Neues Passwort - mit Prüfung";
        case 2:
            return "Neues Passwort - Versenden";
        case 3:
            return "Gast neue Zugangsdaten - Versenden";
        }
        return "";
    }

}
