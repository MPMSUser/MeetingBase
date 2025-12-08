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
package de.meetingapps.meetingportal.meetComBl;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclHVDatenLfd;

/**Lesen und Setzen der aktuellen Nummer Erstpräsenz / Nachtragen etc.
 * -1 = Erstpräsenz
 * Ansonsten: aktuelle Nachtragsnummer*/
public class BlPraesenzlistenNummer {

    private int logDrucken=3;
    
    /*Hinweis zum Sperren: das aktuelle Verzeichnis wird immer vor dem Update von EclMeldung, dem Erzeugen der Willenserklärung 
     * und dem Update der Summen 
     * mit read_update gelesen. Damit werden andere User solange von diesen Aktionen abgehalten, bis die Transaktion beendet ist.
     * 
     *  Beim Druck der Präsenzliste wird aktuelleVerzeichnis ebenfalls mit read-Update gelesen, dann die Willenserklärungen upgedated,
     *  und dann die Transaktion beendet. Damit wird verhindert, dass aktuelleVerzeichnis upgedatet wird bevor eine bereits laufende
     *  Präsenzbuchung abgeschlossen ist.
     */

    private DbBundle lDbBundle = null;

    public BlPraesenzlistenNummer(DbBundle pDbBundle) {
        lDbBundle = pDbBundle;
    }

    /**Liest die gerade aktuellen Nummern ein in lDbBundle.globalVar; damit wird auch die Sperrung der Präsenzsumme für die folgende Buchung durchgeführt*/
    public void leseAktuelleNummernFuerBuchung() {
        lDbBundle.clGlobalVar.zuVerzeichnisNr1 = 0;
        lDbBundle.clGlobalVar.zuVerzeichnisNr2 = 0;
        lDbBundle.clGlobalVar.zuVerzeichnisNr3 = 0;
        lDbBundle.clGlobalVar.zuVerzeichnisNr4 = 0;
        EclHVDatenLfd lHVDatenLfd = new EclHVDatenLfd();
        for (int i = 51; i <= 54; i++) {
            lHVDatenLfd.mandant = lDbBundle.clGlobalVar.mandant;
            lHVDatenLfd.ident = i;
            lHVDatenLfd.benutzer = 0;
            lDbBundle.dbHVDatenLfd.readForUpdate(lHVDatenLfd);
            if (lDbBundle.dbHVDatenLfd.anzErgebnis() != 0) {
                switch (i) {
                case 51:
                    lDbBundle.clGlobalVar.zuVerzeichnisNr1 = Integer
                            .parseInt(lDbBundle.dbHVDatenLfd.ergebnisPosition(0).wert);
                    break;
                case 52:
                    lDbBundle.clGlobalVar.zuVerzeichnisNr2 = Integer
                            .parseInt(lDbBundle.dbHVDatenLfd.ergebnisPosition(0).wert);
                    break;
                case 53:
                    lDbBundle.clGlobalVar.zuVerzeichnisNr3 = Integer
                            .parseInt(lDbBundle.dbHVDatenLfd.ergebnisPosition(0).wert);
                    break;
                case 54:
                    lDbBundle.clGlobalVar.zuVerzeichnisNr4 = Integer
                            .parseInt(lDbBundle.dbHVDatenLfd.ergebnisPosition(0).wert);
                    break;
                }
            }
        }
        if (lDbBundle.clGlobalVar.zuVerzeichnisNr1 == 0) {
            this.stelleFest(1);
            lDbBundle.clGlobalVar.zuVerzeichnisNr1 = -1;
        }
        if (lDbBundle.clGlobalVar.zuVerzeichnisNr2 == 0) {
            this.stelleFest(2);
            lDbBundle.clGlobalVar.zuVerzeichnisNr2 = -1;
        }
        if (lDbBundle.clGlobalVar.zuVerzeichnisNr3 == 0) {
            this.stelleFest(3);
            lDbBundle.clGlobalVar.zuVerzeichnisNr3 = -1;
        }
        if (lDbBundle.clGlobalVar.zuVerzeichnisNr4 == 0) {
            this.stelleFest(4);
            lDbBundle.clGlobalVar.zuVerzeichnisNr4 = -1;
        }
    }

    /**Liest die gerade aktuellen Nummern ein - OHNE Update-Sperre*/
    public void leseAktuelleNummern() {
        lDbBundle.clGlobalVar.zuVerzeichnisNr1 = 0;
        lDbBundle.clGlobalVar.zuVerzeichnisNr2 = 0;
        lDbBundle.clGlobalVar.zuVerzeichnisNr3 = 0;
        lDbBundle.clGlobalVar.zuVerzeichnisNr4 = 0;
        EclHVDatenLfd lHVDatenLfd = new EclHVDatenLfd();
        for (int i = 51; i <= 54; i++) {
            lHVDatenLfd.mandant = lDbBundle.clGlobalVar.mandant;
            lHVDatenLfd.ident = i;
            lHVDatenLfd.benutzer = 0;
            lDbBundle.dbHVDatenLfd.read(lHVDatenLfd);
            if (lDbBundle.dbHVDatenLfd.anzErgebnis() != 0) {
                switch (i) {
                case 51:
                    lDbBundle.clGlobalVar.zuVerzeichnisNr1 = Integer
                            .parseInt(lDbBundle.dbHVDatenLfd.ergebnisPosition(0).wert);
                    break;
                case 52:
                    lDbBundle.clGlobalVar.zuVerzeichnisNr2 = Integer
                            .parseInt(lDbBundle.dbHVDatenLfd.ergebnisPosition(0).wert);
                    break;
                case 53:
                    lDbBundle.clGlobalVar.zuVerzeichnisNr3 = Integer
                            .parseInt(lDbBundle.dbHVDatenLfd.ergebnisPosition(0).wert);
                    break;
                case 54:
                    lDbBundle.clGlobalVar.zuVerzeichnisNr4 = Integer
                            .parseInt(lDbBundle.dbHVDatenLfd.ergebnisPosition(0).wert);
                    break;
                }
            }
        }
        if (lDbBundle.clGlobalVar.zuVerzeichnisNr1 == 0) {
            this.stelleFest(1);
            lDbBundle.clGlobalVar.zuVerzeichnisNr1 = -1;
        }
        if (lDbBundle.clGlobalVar.zuVerzeichnisNr2 == 0) {
            this.stelleFest(2);
            lDbBundle.clGlobalVar.zuVerzeichnisNr2 = -1;
        }
        if (lDbBundle.clGlobalVar.zuVerzeichnisNr3 == 0) {
            this.stelleFest(3);
            lDbBundle.clGlobalVar.zuVerzeichnisNr3 = -1;
        }
        if (lDbBundle.clGlobalVar.zuVerzeichnisNr4 == 0) {
            this.stelleFest(4);
            lDbBundle.clGlobalVar.zuVerzeichnisNr4 = -1;
        }
    }

    /**Liest die gerade aktuellen Nummern ein - OHNE Update-Sperrem unbd ohne Update.
     * Speziell gedacht für Portal-Zu-/Abgang, um Sperren nach Absturz zu vermeiden
     * Geht gut, da bei Einlesen eines Leer-Satzes ja immer der Default-Wert gesetzt wird.*/
    public void leseAktuelleNummernOhneUpdate() {
        lDbBundle.clGlobalVar.zuVerzeichnisNr1 = 0;
        lDbBundle.clGlobalVar.zuVerzeichnisNr2 = 0;
        lDbBundle.clGlobalVar.zuVerzeichnisNr3 = 0;
        lDbBundle.clGlobalVar.zuVerzeichnisNr4 = 0;
        EclHVDatenLfd lHVDatenLfd = new EclHVDatenLfd();
        for (int i = 51; i <= 54; i++) {
            lHVDatenLfd.mandant = lDbBundle.clGlobalVar.mandant;
            lHVDatenLfd.ident = i;
            lHVDatenLfd.benutzer = 0;
            lDbBundle.dbHVDatenLfd.read(lHVDatenLfd);
            if (lDbBundle.dbHVDatenLfd.anzErgebnis() != 0) {
                switch (i) {
                case 51:
                    lDbBundle.clGlobalVar.zuVerzeichnisNr1 = Integer
                            .parseInt(lDbBundle.dbHVDatenLfd.ergebnisPosition(0).wert);
                    break;
                case 52:
                    lDbBundle.clGlobalVar.zuVerzeichnisNr2 = Integer
                            .parseInt(lDbBundle.dbHVDatenLfd.ergebnisPosition(0).wert);
                    break;
                case 53:
                    lDbBundle.clGlobalVar.zuVerzeichnisNr3 = Integer
                            .parseInt(lDbBundle.dbHVDatenLfd.ergebnisPosition(0).wert);
                    break;
                case 54:
                    lDbBundle.clGlobalVar.zuVerzeichnisNr4 = Integer
                            .parseInt(lDbBundle.dbHVDatenLfd.ergebnisPosition(0).wert);
                    break;
                }
            }
        }
        if (lDbBundle.clGlobalVar.zuVerzeichnisNr1 == 0) {
            lDbBundle.clGlobalVar.zuVerzeichnisNr1 = -1;
        }
        if (lDbBundle.clGlobalVar.zuVerzeichnisNr2 == 0) {
             lDbBundle.clGlobalVar.zuVerzeichnisNr2 = -1;
        }
        if (lDbBundle.clGlobalVar.zuVerzeichnisNr3 == 0) {
             lDbBundle.clGlobalVar.zuVerzeichnisNr3 = -1;
        }
        if (lDbBundle.clGlobalVar.zuVerzeichnisNr4 == 0) {
            lDbBundle.clGlobalVar.zuVerzeichnisNr4 = -1;
        }
    }

    /**Erhöht die aktuelle Liste; Parameter: 1 bis 4. Die entsprechende aktuelle Liste wird um 1 erhöht.
     * Rückgabe= Nummer der (Nachtrags)präsenzliste*/
    public int stelleFest(int nr) {
        int listenNummer = 0;
        int feststellungNummer = 0;
        CaBug.druckeLog("stelle Fest A", logDrucken, 10);
        EclHVDatenLfd lHVDatenLfd = new EclHVDatenLfd();
        lHVDatenLfd.mandant = lDbBundle.clGlobalVar.mandant;
        lHVDatenLfd.ident = 50 + nr;
        lHVDatenLfd.benutzer = 0;
        lDbBundle.dbHVDatenLfd.readForUpdate(lHVDatenLfd);
        if (lDbBundle.dbHVDatenLfd.anzErgebnis() != 0) {/*Bereits ein Eintrag vorhanden*/
            CaBug.druckeLog("stelle Fest B", logDrucken, 10);
            lHVDatenLfd = lDbBundle.dbHVDatenLfd.ergebnisPosition(0);
            listenNummer = Integer.parseInt(lHVDatenLfd.wert);
            if (listenNummer == 0) {
                listenNummer = -1;
                feststellungNummer = -1;
            } else {
                CaBug.druckeLog("stelle Fest C", logDrucken, 10);

                if (listenNummer == -1) {
                    feststellungNummer = -1;
                    listenNummer = 1;
                } else {
                    feststellungNummer = listenNummer;
                    listenNummer++;
                }
            }
            CaBug.druckeLog("listennummer=" + listenNummer, logDrucken, 10);
            lHVDatenLfd.wert = Integer.toString(listenNummer);
            int rc = lDbBundle.dbHVDatenLfd.update(lHVDatenLfd);
            CaBug.druckeLog("rc=" + rc, logDrucken, 10);
        } else {/*Neuen Eintrag erzeugen*/
            /*Darf nur beim Präsenzstart vorkommen - wenn aus leseAktuelleNummernFuerBuchung aufgerufen.
             * Gehört eigentlich nicht zum Präsenzfeststellen!
             */
            lHVDatenLfd.wert = "-1";
            feststellungNummer = 0;
            lDbBundle.dbHVDatenLfd.insert(lHVDatenLfd);
        }
        return feststellungNummer;

    }
}
