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

import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclHVDatenLfd;
import de.meetingapps.meetingportal.meetComKonst.KonstWillenserklaerung;

/**Funktionen zum Updaten (und dann auch Auslesen) des Präsenz-Buchungs-Protokolls je Benutzer
 * 
 * Ablauf zum Verwenden:
 * 
 * liefereProtokollNr
 * Speichern der Willenserklärung mit dieser ProtokollNr
 * erhoeheAnzahlEintraege - je nachdem Protokoll drucken /Labeldrucken (protokollnr wie aus obiger liefereProtokollNr*/
public class BlPraesenzProtokoll {

    private DbBundle lDbBundle = null;

    public int buendelnProtokollNr = 0;

    public int[] arbeitsplatznr = null;
    public int[] letztesAbgeschlossenesProtokoll = null;

    public BlPraesenzProtokoll(DbBundle pDbBundle) {
        lDbBundle = pDbBundle;
    }

    private void insertOrUpdate(EclHVDatenLfd lHVDatenLfd, String wert) {

        lDbBundle.dbHVDatenLfd.readForUpdate(lHVDatenLfd);
        if (lDbBundle.dbHVDatenLfd.anzErgebnis() != 0) {/*Bereits ein Eintrag vorhanden*/
            lHVDatenLfd = lDbBundle.dbHVDatenLfd.ergebnisPosition(0);
            lHVDatenLfd.wert = wert;
            lDbBundle.dbHVDatenLfd.update(lHVDatenLfd);
        } else {
            lHVDatenLfd.wert = wert;
            lDbBundle.dbHVDatenLfd.insert(lHVDatenLfd);
        }

    }

    /**Liest laufende Protokollnr in lDbBundle.globalVar ein und führt Sperrung für folgende Buchungen durch*/
    public void leseProtokollNr() {
        /*Summen lesen - immer Delayed-Summen für Feststellung verwenden*/
        EclHVDatenLfd lHVDatenLfd = null;
        int protokollnr;

        lHVDatenLfd = new EclHVDatenLfd();
        lHVDatenLfd.mandant = lDbBundle.clGlobalVar.mandant;
        lHVDatenLfd.ident = 101;
        lHVDatenLfd.benutzer = lDbBundle.clGlobalVar.arbeitsplatz;
        lDbBundle.dbHVDatenLfd.readForUpdate(lHVDatenLfd);
        if (lDbBundle.dbHVDatenLfd.anzErgebnis() != 0) {/*Bereits ein Eintrag vorhanden*/
            lHVDatenLfd = lDbBundle.dbHVDatenLfd.ergebnisPosition(0);
            protokollnr = Integer.parseInt(lHVDatenLfd.wert);
        } else {
            protokollnr = 1;
            lHVDatenLfd.wert = "1";
            lDbBundle.dbHVDatenLfd.insert(lHVDatenLfd);
        }
        lDbBundle.clGlobalVar.protokollnr = protokollnr;
    }

    /**Returnwert ==true => Bündeln erforderlich, in buendelnProtokollNr steht die Nr für das "Bündel-Label"*/
    public boolean erhoeheAnzahlEintrage(int willenserklaerungArt) {
        boolean drucken = false;
        int anz = 0;

        EclHVDatenLfd lHVDatenLfd = null;

        /*Nur Zugänge- Willenserklärungen*/
        if (willenserklaerungArt == KonstWillenserklaerung.erstzugang) {
            lHVDatenLfd = new EclHVDatenLfd();
            lHVDatenLfd.mandant = lDbBundle.clGlobalVar.mandant;
            lHVDatenLfd.ident = 102;
            lHVDatenLfd.benutzer = lDbBundle.clGlobalVar.arbeitsplatz;
            lDbBundle.dbHVDatenLfd.readForUpdate(lHVDatenLfd);
            if (lDbBundle.dbHVDatenLfd.anzErgebnis() != 0) {/*Bereits ein Eintrag vorhanden*/
                lHVDatenLfd = lDbBundle.dbHVDatenLfd.ergebnisPosition(0);
                anz = Integer.parseInt(lHVDatenLfd.wert) + 1;
                lHVDatenLfd.wert = Integer.toString(anz);
                lDbBundle.dbHVDatenLfd.update(lHVDatenLfd);
            } else {
                lHVDatenLfd.wert = "1";
                lDbBundle.dbHVDatenLfd.insert(lHVDatenLfd);
                anz = 1;
            }
            if (anz >= lDbBundle.paramGeraet.protokollAnzMaxZugaenge) {
                drucken = true;
            }
        }

        /*Alle Willenserklärungen*/
        lHVDatenLfd = new EclHVDatenLfd();
        lHVDatenLfd.mandant = lDbBundle.clGlobalVar.mandant;
        lHVDatenLfd.ident = 103;
        lHVDatenLfd.benutzer = lDbBundle.clGlobalVar.arbeitsplatz;
        lDbBundle.dbHVDatenLfd.readForUpdate(lHVDatenLfd);
        if (lDbBundle.dbHVDatenLfd.anzErgebnis() != 0) {/*Bereits ein Eintrag vorhanden*/
            lHVDatenLfd = lDbBundle.dbHVDatenLfd.ergebnisPosition(0);
            anz = Integer.parseInt(lHVDatenLfd.wert) + 1;
            lHVDatenLfd.wert = Integer.toString(anz);
            lDbBundle.dbHVDatenLfd.update(lHVDatenLfd);
        } else {
            lHVDatenLfd.wert = "1";
            lDbBundle.dbHVDatenLfd.insert(lHVDatenLfd);
            anz = 1;
        }
        if (anz >= lDbBundle.paramGeraet.protokollAnzMax) {
            drucken = true;
        }

        if (drucken == true) {
            neuesProtokoll(lDbBundle.clGlobalVar.arbeitsplatz);
        }

        return drucken;
    }

    /**Füllen von arbeitsplatznr[] und letztesAbgeschlossenesProtokoll[]*/
    public void fuelleLetztesAbgeschlossenesProtokoll() {
        arbeitsplatznr = null;
        letztesAbgeschlossenesProtokoll = null;

        int rc = lDbBundle.dbHVDatenLfd.readLetzteProtokolle();
        if (rc > 0) {
            arbeitsplatznr = new int[rc];
            letztesAbgeschlossenesProtokoll = new int[rc];
            for (int i = 0; i < rc; i++) {
                arbeitsplatznr[i] = lDbBundle.dbHVDatenLfd.ergebnisPosition(i).benutzer;
                letztesAbgeschlossenesProtokoll[i] = Integer.parseInt(lDbBundle.dbHVDatenLfd.ergebnisPosition(i).wert);

            }
        }
    }

    /**Füllen von arbeitsplatznr[] mit Arbeitsplätzen, die schon mal aktiv waren*/
    public void fuelleArbeitsplaetze() {
        arbeitsplatznr = null;
        letztesAbgeschlossenesProtokoll = null;

        int rc = lDbBundle.dbHVDatenLfd.readArbeitsplaetzeMitProtokollen();
        if (rc > 0) {
            arbeitsplatznr = new int[rc];
            letztesAbgeschlossenesProtokoll = new int[rc];
            for (int i = 0; i < rc; i++) {
                arbeitsplatznr[i] = lDbBundle.dbHVDatenLfd.ergebnisPosition(i).benutzer;
                letztesAbgeschlossenesProtokoll[i] = Integer.parseInt(lDbBundle.dbHVDatenLfd.ergebnisPosition(i).wert);

            }
        }
    }

    /**Abschließen des aktuellen Protokolls und Eröffnen eines neuen Protokolls für einen Arbeitsplatz*/
    public void neuesProtokoll(int pArbeitsplatzNr) {
        int protokollnr;
        int anzZugaenge = 0, anzAlle = 0;
        EclHVDatenLfd lHVDatenLfd = null;

        /*Ist überhaupt schon ein Protokoll vorhanden? Wenn nein, dann nur "initialisieren" auf 1. Protokoll*/
        lHVDatenLfd = new EclHVDatenLfd();
        lHVDatenLfd.mandant = lDbBundle.clGlobalVar.mandant;
        lHVDatenLfd.ident = 101;
        lHVDatenLfd.benutzer = pArbeitsplatzNr;
        lDbBundle.dbHVDatenLfd.readForUpdate(lHVDatenLfd);
        if (lDbBundle.dbHVDatenLfd.anzErgebnis() == 0) {/*Noch kein Eintrag vorhanden*/
            protokollnr = 1;
            lHVDatenLfd.wert = "1";
            lDbBundle.dbHVDatenLfd.insert(lHVDatenLfd);
            return;
        }

        /*Laufende Protokollnr ermitteln*/
        lHVDatenLfd = lDbBundle.dbHVDatenLfd.ergebnisPosition(0);
        protokollnr = Integer.parseInt(lHVDatenLfd.wert);

        /*Checken - sind in diesem Protokoll bereits Erstzugänge oder allgemeine Willenserklärungen?
         * Wenn nicht, dann nicht erhöhen!*/
        {/*Zugänge*/
            EclHVDatenLfd lHVDatenLfdZugaenge = new EclHVDatenLfd();
            lHVDatenLfdZugaenge.mandant = lDbBundle.clGlobalVar.mandant;
            lHVDatenLfdZugaenge.ident = 102;
            lHVDatenLfdZugaenge.benutzer = pArbeitsplatzNr;
            lDbBundle.dbHVDatenLfd.readForUpdate(lHVDatenLfdZugaenge);
            if (lDbBundle.dbHVDatenLfd.anzErgebnis() != 0) {/*Bereits ein Eintrag vorhanden*/
                lHVDatenLfdZugaenge = lDbBundle.dbHVDatenLfd.ergebnisPosition(0);
                anzZugaenge = Integer.parseInt(lHVDatenLfdZugaenge.wert);
            } else {
                anzZugaenge = 0;
            }
        }

        {/*Alle Willenserklärungen*/
            EclHVDatenLfd lHVDatenLfdAlle = new EclHVDatenLfd();
            lHVDatenLfdAlle.mandant = lDbBundle.clGlobalVar.mandant;
            lHVDatenLfdAlle.ident = 103;
            lHVDatenLfdAlle.benutzer = pArbeitsplatzNr;
            lDbBundle.dbHVDatenLfd.readForUpdate(lHVDatenLfdAlle);
            if (lDbBundle.dbHVDatenLfd.anzErgebnis() != 0) {/*Bereits ein Eintrag vorhanden*/
                lHVDatenLfdAlle = lDbBundle.dbHVDatenLfd.ergebnisPosition(0);
                anzAlle = Integer.parseInt(lHVDatenLfdAlle.wert);
            } else {
                anzAlle = 0;
            }
        }

        if (anzZugaenge == 0 && anzAlle == 0) {
            /*Im bisherigen Protokoll noch keine Einträge => kein neues*/
            return;
        }

        /*Protokollnr erhöhen*/
        protokollnr = protokollnr + 1;
        buendelnProtokollNr = protokollnr - 1;
        lHVDatenLfd.wert = Integer.toString(protokollnr);
        lDbBundle.dbHVDatenLfd.update(lHVDatenLfd);

        /*Zuletzt abgeschlossenes Protokoll*/
        lHVDatenLfd = new EclHVDatenLfd();
        lHVDatenLfd.mandant = lDbBundle.clGlobalVar.mandant;
        lHVDatenLfd.ident = 104;
        lHVDatenLfd.benutzer = pArbeitsplatzNr;
        String wert = Integer.toString(protokollnr - 1);
        this.insertOrUpdate(lHVDatenLfd, wert);

        /*Anz zurücksetzen - Erstzugänge*/
        lHVDatenLfd = new EclHVDatenLfd();
        lHVDatenLfd.mandant = lDbBundle.clGlobalVar.mandant;
        lHVDatenLfd.ident = 102;
        lHVDatenLfd.benutzer = pArbeitsplatzNr;
        lDbBundle.dbHVDatenLfd.readForUpdate(lHVDatenLfd);
        if (lDbBundle.dbHVDatenLfd.anzErgebnis() != 0) {/*Bereits ein Eintrag vorhanden*/
            lHVDatenLfd = lDbBundle.dbHVDatenLfd.ergebnisPosition(0);
            lHVDatenLfd.wert = Integer.toString(0);
            lDbBundle.dbHVDatenLfd.update(lHVDatenLfd);
        } else {
            lHVDatenLfd.wert = Integer.toString(0);
            lDbBundle.dbHVDatenLfd.insert(lHVDatenLfd);
        }

        /*Anz zurücksetzen - Alle*/
        lHVDatenLfd = new EclHVDatenLfd();
        lHVDatenLfd.mandant = lDbBundle.clGlobalVar.mandant;
        lHVDatenLfd.ident = 103;
        lHVDatenLfd.benutzer = pArbeitsplatzNr;
        lDbBundle.dbHVDatenLfd.readForUpdate(lHVDatenLfd);
        if (lDbBundle.dbHVDatenLfd.anzErgebnis() != 0) {/*Bereits ein Eintrag vorhanden*/
            lHVDatenLfd = lDbBundle.dbHVDatenLfd.ergebnisPosition(0);
            lHVDatenLfd.wert = Integer.toString(0);
            lDbBundle.dbHVDatenLfd.update(lHVDatenLfd);
        } else {
            lHVDatenLfd.wert = Integer.toString(0);
            lDbBundle.dbHVDatenLfd.insert(lHVDatenLfd);
        }

    }

    /**Schließt Protokolle für alle Arbeitsplätze*/
    public void neuesProtokollAlle() {
        fuelleArbeitsplaetze();
        if (arbeitsplatznr != null) {
            for (int i = 0; i < arbeitsplatznr.length; i++) {
                neuesProtokoll(arbeitsplatznr[i]);
            }
        }
    }

    /**Drucken des zuletzt abgeschlossenen Protokolls des aktuellen Arbeitsplatzes*/
    public void drucken() {
        /*TODO #9 Protokolle - automatisches Drucken des Protokolls, wenn abgeschlossen*/
    }

}
