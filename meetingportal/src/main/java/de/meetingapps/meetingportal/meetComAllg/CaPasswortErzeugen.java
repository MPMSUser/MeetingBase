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
package de.meetingapps.meetingportal.meetComAllg;

import java.util.Random;

/**
 * Mit dieser Klassik wird ein "Zufalls-Passwort" erzeugt (sowohl das
 * "eigentliche" Passwort - generatePW, als auch ein Initialpasswort bestehend
 * aus 3 Passwörtern - generatePWInitial
 */
public class CaPasswortErzeugen {

    /**
     * Erzeugt ein Zufalls-Passwort der Länge laenge. laenge=Gesamtlänge des zu
     * erzeugenden Passwortes sonderzeichen=Anzahl der Sonderzeichen, die im
     * Passwort enthalten sein sollen. zahlen=Anzahl der Zahlen, die im Passwort
     * enthalten sein sollen. grossbuchstaben=true => es werden auch
     * Großbuchstaben verwendet
     */
    public String generatePW(int laenge, int sonderzeichen, int zahlen, boolean grossbuchstaben) {
        return generatePW(laenge, sonderzeichen, zahlen, grossbuchstaben, true);
    }

    public String generatePW(int laenge, int sonderzeichen, int zahlen, boolean grossbuchstaben, boolean kleinbuchstaben) {
        if (laenge==0) {
            return "LEN=0";
        }
        
        /*******Das hier ist wichtig, weil es jetzt nur noch ein Sonderzeichen zum Auswählen gibt! Sonst Crash!*****/
        if (sonderzeichen > 1) {
            sonderzeichen = 1;
        }

        String secureChars = ""; //Buchstaben - erst mal leer, je nach Parameter dann um Großbuchstaben und/oder Kleinbuchstaben ergänzen

        String numberChars = "23456789"; // Zahlen
        String specialChars = "*"; // Sonderzeichen
        String stack = "";
        Random r = new Random();

        // Wenn kleinbuchstaben = true --> Kleinbuchstaben anfügen
        if (kleinbuchstaben == true) {
            secureChars += "abcdefghjkmnpqrstuvwxyz";
        }

        // Wenn grossbuchstaben = true --> Großbuchtsben anfügen
        if (grossbuchstaben == true) {
            secureChars += "ABCDEFGHJKMNPQRSTUVWXYZ";
        }

        // Stack für Password-Erzeugung füllen
        stack = secureChars;

        int count = laenge - sonderzeichen - zahlen;
        String temp = shuffle(r, stack);
        stack = temp.substring(0, count);

        // Sonderzeichen einfügen
        if (sonderzeichen > 0) {
            temp = shuffle(r, specialChars);
            stack += temp.substring(0, sonderzeichen);
        }

        // Zahlen einfügen
        if (zahlen > 0) {
            temp = shuffle(r, numberChars);
            stack += temp.substring(0, zahlen);
        }

        stack = shuffle(r, stack);

        return stack;

    }

    // Funktion zu durchmischen der Zeichenkette
    private String shuffle(Random random, String inputString) {
        // String zu Array
        char a[] = inputString.toCharArray();

        for (int i = 0; i < a.length - 1; i++) {
            int j = random.nextInt(a.length - 1);

            char temp = a[i];
            a[i] = a[j];
            a[j] = temp;
        }

        return new String(a);
    }

    /**
     * Erzeugt ein Initialpasswort, das dann unverschlüsselt in die Datenbank
     * geschrieben werden kann (in derzeitiger Lösung): Es werden 3 zufällige
     * Passwörter erzeugt, und aneinandergehängt. Dieser String wird als
     * Initialpasswort in die Datenbank geschreiben. Als tatsächliches Passwort wird
     * nur das mittlere verwendet (das dann mit CaPasswortVerschluesseln
     * verschlüsselt wird)
     * 
     * laenge=laenge des eigentlichen (mittleren) Passwortes - wird auch für das
     * (unbenutzte) erste und letzte Passwort verwendet. Restliche Parameter wie
     * generatePW.
     */
    public String generatePWInitial(int laenge, int sonderzeichen, int zahlen, boolean grossbuchstaben) {
        return null;
    }
}
