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

import de.meetingapps.meetingportal.meetComAllg.CaDatumZeit;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclHVDatenLfd;
import de.meetingapps.meetingportal.meetComEntities.EclMeldung;

/**Lesen und Verändern der aktuellen Präsenzsummen (wie mit Präsenzänderungen laufend mitberechnet)*/
public class BlPraesenzSummen {

    private DbBundle lDbBundle = null;

    /**[gattung-1], gattung=1-5*/
    public long[] praesenzSummeNormalGattung = { 0, 0, 0, 0, 0 };

    /**gattung=1-5*/
    public long getPraesenzSummeNormalGattung(int gattung) {
        return praesenzSummeNormalGattung[gattung - 1];
    }

    /**[gattung-1], gattung=1-5*/
    public long[] praesenzSummeDelayedGattung = { 0, 0, 0, 0, 0 };

    /**gattung=1-5*/
    public long getPraesenzSummeDelayedGattung(int gattung) {
        return praesenzSummeDelayedGattung[gattung - 1];
    }

    /**[gattung-1], gattung=1-5*/
    public long[] praesenzSummeBerechnetGattung = { 0, 0, 0, 0, 0 };

    /**gattung=1-5*/
    public long getPraesenzSummeBerechnetGattung(int gattung) {
        return praesenzSummeBerechnetGattung[gattung - 1];
    }

    /**[gattung-1], gattung=1-5*/
    public long[] praesenzSummeAusListeGattung = { 0, 0, 0, 0, 0 };

    /**gattung=1-5*/
    public long getPraesenzSummeAusListeGattung(int gattung) {
        return praesenzSummeAusListeGattung[gattung - 1];
    }

    /**[gattung-1], gattung=1-5*/
    public long[] briefwahlSummeBerechnetGattung = { 0, 0, 0, 0, 0 };

    /**gattung=1-5*/
    public long getBriefwahlSummeBerechnetGattung(int gattung) {
        return briefwahlSummeBerechnetGattung[gattung - 1];
    }

    public String datumZeitFeststellung = "";

    public BlPraesenzSummen(DbBundle pDbBundle) {
        lDbBundle = pDbBundle;
    }

    /*TODO: diese Funktion ersetzen durch die entsprechende in DbHVDatenLfd*/
    @Deprecated
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

    /**Erhöht die Summen um den aktuellen Wert. delayed==1 => die Buchung wird delayed, deshalb nur "normale" Summen verändern 
     * gattung=1 bis 5*/
    public void zugang(long wert, int gattung, int delayed) {
        veraendere(wert, gattung, delayed);
    }

    /**Erniedrigt die Summen um den aktuellen Wert. delayed==1 => die Buchung wird delayed, deshalb nur "normale" Summen verändern 
     * gattung=1 bis 5*/
    public void abgang(long wert, int gattung, int delayed) {
        veraendere(wert * (-1), gattung, delayed);
    }

    /**Allgemeine Funktion - mit + / -
     * gattung=1 bis 5*/
    private void veraendere(long wert, int gattung, int delayed) {
        veraendere_normalOderDelayed(wert, gattung, 1);
        if (delayed != 1) {
            veraendere_normalOderDelayed(wert, gattung, 2);
        }
    }

    /**delayedOderNormal==1 => Präsenzsummen "normal" werden upgedated
     * ==2 => Präsenzsummen "delayed" werden upgedated
     * gattung=1 bis 5*/
    private void veraendere_normalOderDelayed(long wert, int gattung, int normalOderDelayed) {
        //		int ident=0;
        //		ident=gattung;/*1 bis 5*/
        //		if (normalOderDelayed==2){ident=ident+10;} /*11 bis 15*/
        //		
        //		EclHVDatenLfd lHVDatenLfd=new EclHVDatenLfd();
        //		lHVDatenLfd.mandant=lDbBundle.clGlobalVar.mandant;
        //		lHVDatenLfd.ident=ident;
        //		lHVDatenLfd.benutzer=0;
        //		lDbBundle.dbHVDatenLfd.readForUpdate(lHVDatenLfd);
        //		if (lDbBundle.dbHVDatenLfd.anzErgebnis()!=0){/*Bereits ein Eintrag vorhanden*/
        //			lHVDatenLfd=lDbBundle.dbHVDatenLfd.ergebnisPosition(0);
        //			long summe=Long.parseLong(lHVDatenLfd.wert)+wert;
        //			lHVDatenLfd.wert=Long.toString(summe);
        //			lHVDatenLfd.beschreibung="akt.Präsenz G"+Integer.toString(gattung);
        //			if (normalOderDelayed==2){lHVDatenLfd.beschreibung+=" Delayed";}
        //			lDbBundle.dbHVDatenLfd.update(lHVDatenLfd);
        //		}
        //		else{
        //			lHVDatenLfd.wert=Long.toString(wert);
        //			lHVDatenLfd.beschreibung="akt.Präsenz G"+Integer.toString(gattung);
        //			if (normalOderDelayed==2){lHVDatenLfd.beschreibung+=" Delayed";}
        //			lDbBundle.dbHVDatenLfd.insert(lHVDatenLfd);
        //		}

    }

    /**Setze berechnete Summe für aktuell festgestellte Liste (im Zusammenhang mit Präsenzfeststellung)
     * Hierbei wird die "delayed"-Summe verwendet.
     * 
     * Wichtig: vorher BlPraesenzlistenNummer.leseAktuelleNummernFuerBuchung wg. Sperre aufrufen!
     * 
     * prasenzlistenNr= lft. Nr. der Präsenzfeststellung
     * verzeichnisnr=1 bis 4*/
    public void fixiereAktuelleSummen(int praesenzlistenNr, int verzeichnisnr) {
        EclHVDatenLfd lHVDatenLfd = new EclHVDatenLfd();
        long summe;
        int ident = 0;
        String wert = "";

        for (int i = 1; i <= 5; i++) { /*Für fünf Gattungen*/
            /*Summen lesen - immer Delayed-Summen für Feststellung verwenden*/
            lHVDatenLfd = new EclHVDatenLfd();
            lHVDatenLfd.mandant = lDbBundle.clGlobalVar.mandant;
            lHVDatenLfd.ident = 10 + i;
            lHVDatenLfd.benutzer = 0;
            lDbBundle.dbHVDatenLfd.readForUpdate(lHVDatenLfd);
            if (lDbBundle.dbHVDatenLfd.anzErgebnis() != 0) {/*Bereits ein Eintrag vorhanden*/
                lHVDatenLfd = lDbBundle.dbHVDatenLfd.ergebnisPosition(0);
                summe = Long.parseLong(lHVDatenLfd.wert);
            } else {
                summe = 0;
            }
            /*Summen schreiben*/
            ident = 500 + i + (verzeichnisnr - 1) * 5;
            lHVDatenLfd = new EclHVDatenLfd();
            lHVDatenLfd.mandant = lDbBundle.clGlobalVar.mandant;
            lHVDatenLfd.ident = ident;
            lHVDatenLfd.benutzer = praesenzlistenNr;
            lHVDatenLfd.beschreibung = "Praes Berechn. Liste" + Integer.toString(praesenzlistenNr) + " VerzNr="
                    + Integer.toString(verzeichnisnr) + " G=" + Integer.toString(i);
            insertOrUpdate(lHVDatenLfd, Long.toString(summe));
        }

        /*Aktuelles Feststellungsdatum schreiben*/
        ident = 60 + verzeichnisnr;
        lHVDatenLfd = new EclHVDatenLfd();
        lHVDatenLfd.mandant = lDbBundle.clGlobalVar.mandant;
        lHVDatenLfd.ident = ident;
        lHVDatenLfd.benutzer = praesenzlistenNr;
        lHVDatenLfd.beschreibung = "Praes Festst Liste" + Integer.toString(praesenzlistenNr) + " VerzNr="
                + Integer.toString(verzeichnisnr);
        wert = CaDatumZeit.DatumZeitStringFuerDatenbank();
        insertOrUpdate(lHVDatenLfd, wert);

        /*Aktuelle Briefwahlsummen ermitteln*/
        long[] briefwahlgattung = { 0, 0, 0, 0, 0 };
        lDbBundle.dbMeldungen.leseSammelkartenBriefwahl(-1);
        int anz = lDbBundle.dbMeldungen.meldungenArray.length;
        for (int i = 0; i < anz; i++) {
            EclMeldung lSammelMeldung = lDbBundle.dbMeldungen.meldungenArray[i];
            briefwahlgattung[lSammelMeldung.liefereGattung() - 1] += lSammelMeldung.stimmen;
        }
        /*Summen schreiben*/
        for (int i = 1; i <= 5; i++) {
            ident = 600 + i + (verzeichnisnr - 1) * 5;
            lHVDatenLfd = new EclHVDatenLfd();
            lHVDatenLfd.mandant = lDbBundle.clGlobalVar.mandant;
            lHVDatenLfd.ident = ident;
            lHVDatenLfd.benutzer = praesenzlistenNr;
            lHVDatenLfd.beschreibung = "Praes Briefw. Liste" + Integer.toString(praesenzlistenNr) + " VerzNr="
                    + Integer.toString(verzeichnisnr) + " G=" + Integer.toString(i);
            insertOrUpdate(lHVDatenLfd, Long.toString(briefwahlgattung[i - 1]));
        }
    }

    /**Lese die aktuell gültigen Präsenzsummen in praesenzSumme<Normal/Delayed>Gattung*/
    public void leseAktuelleSummen() {
        EclHVDatenLfd lHVDatenLfd = new EclHVDatenLfd();

        for (int i = 1; i <= 5; i++) { /*Für fünf Gattungen*/

            /*Summen lesen - Normal*/
            lHVDatenLfd = new EclHVDatenLfd();
            lHVDatenLfd.mandant = lDbBundle.clGlobalVar.mandant;
            lHVDatenLfd.ident = 0 + i;
            lHVDatenLfd.benutzer = 0;
            lDbBundle.dbHVDatenLfd.read(lHVDatenLfd);
            if (lDbBundle.dbHVDatenLfd.anzErgebnis() != 0) {/*Bereits ein Eintrag vorhanden*/
                lHVDatenLfd = lDbBundle.dbHVDatenLfd.ergebnisPosition(0);
                praesenzSummeNormalGattung[i - 1] = Long.parseLong(lHVDatenLfd.wert);
            } else {
                praesenzSummeNormalGattung[i - 1] = 0;
            }

            /*Summen lesen - Delayed*/
            lHVDatenLfd = new EclHVDatenLfd();
            lHVDatenLfd.mandant = lDbBundle.clGlobalVar.mandant;
            lHVDatenLfd.ident = 10 + i;
            lHVDatenLfd.benutzer = 0;
            lDbBundle.dbHVDatenLfd.read(lHVDatenLfd);
            if (lDbBundle.dbHVDatenLfd.anzErgebnis() != 0) {/*Bereits ein Eintrag vorhanden*/
                lHVDatenLfd = lDbBundle.dbHVDatenLfd.ergebnisPosition(0);
                praesenzSummeDelayedGattung[i - 1] = Long.parseLong(lHVDatenLfd.wert);
            } else {
                praesenzSummeDelayedGattung[i - 1] = 0;
            }

        }
    }

    /**Speichere ermittelte Summe Liste*/
    public void speichereSummenAusListe(int praesenzlistenNr, int verzeichnisnr, long[] wertGattungen,
            int gattungSpeichern) {
        EclHVDatenLfd lHVDatenLfd = new EclHVDatenLfd();
        long summe;

        for (int i = 1; i <= 5; i++) { /*Für fünf Gattungen*/
            if (gattungSpeichern == 0
                    || gattungSpeichern == i) {/*Nur dann speichern, wenn diese Gattung - oder alle - gedruckt wurden*/
                summe = wertGattungen[i - 1];
                /*Summen schreiben*/
                int ident = 550 + i + (verzeichnisnr - 1) * 5;
                lHVDatenLfd = new EclHVDatenLfd();
                lHVDatenLfd.mandant = lDbBundle.clGlobalVar.mandant;
                lHVDatenLfd.ident = ident;
                lHVDatenLfd.benutzer = praesenzlistenNr;
                lDbBundle.dbHVDatenLfd.readForUpdate(lHVDatenLfd);
                if (lDbBundle.dbHVDatenLfd.anzErgebnis() != 0) {/*Bereits ein Eintrag vorhanden*/
                    lHVDatenLfd = lDbBundle.dbHVDatenLfd.ergebnisPosition(0);
                    lHVDatenLfd.wert = Long.toString(summe);
                    lHVDatenLfd.beschreibung = "Praes Gedru. Liste" + Integer.toString(praesenzlistenNr) + " VerzNr="
                            + Integer.toString(verzeichnisnr) + " G=" + Integer.toString(i);
                    lDbBundle.dbHVDatenLfd.update(lHVDatenLfd);

                } else {
                    lHVDatenLfd.wert = Long.toString(summe);
                    lHVDatenLfd.beschreibung = "Praes Gedru. Liste" + Integer.toString(praesenzlistenNr) + " VerzNr="
                            + Integer.toString(verzeichnisnr) + " G=" + Integer.toString(i);
                    lDbBundle.dbHVDatenLfd.insert(lHVDatenLfd);
                }
            }
        }
    }

    /**Lese die aktuell gültigen Präsenzsummen in praesenzSumme<berechnet/ausListe>Gattung*/
    public void leseSummenFuerListe(int listenNr) {
        EclHVDatenLfd lHVDatenLfd = new EclHVDatenLfd();

        for (int i = 1; i <= 5; i++) { /*Für fünf Gattungen*/

            /*Summen lesen - Berechnet*/
            lHVDatenLfd = new EclHVDatenLfd();
            lHVDatenLfd.mandant = lDbBundle.clGlobalVar.mandant;
            lHVDatenLfd.ident = 500 + i;
            lHVDatenLfd.benutzer = listenNr;
            lDbBundle.dbHVDatenLfd.read(lHVDatenLfd);
            if (lDbBundle.dbHVDatenLfd.anzErgebnis() != 0) {/*Bereits ein Eintrag vorhanden*/
                lHVDatenLfd = lDbBundle.dbHVDatenLfd.ergebnisPosition(0);
                praesenzSummeBerechnetGattung[i - 1] = Long.parseLong(lHVDatenLfd.wert);
            } else {
                praesenzSummeBerechnetGattung[i - 1] = -1;
            }

            /*Summen lesen - aus Liste*/
            lHVDatenLfd = new EclHVDatenLfd();
            lHVDatenLfd.mandant = lDbBundle.clGlobalVar.mandant;
            lHVDatenLfd.ident = 550 + i;
            lHVDatenLfd.benutzer = listenNr;
            lDbBundle.dbHVDatenLfd.read(lHVDatenLfd);
            if (lDbBundle.dbHVDatenLfd.anzErgebnis() != 0) {/*Bereits ein Eintrag vorhanden*/
                lHVDatenLfd = lDbBundle.dbHVDatenLfd.ergebnisPosition(0);
                praesenzSummeAusListeGattung[i - 1] = Long.parseLong(lHVDatenLfd.wert);
            } else {
                praesenzSummeAusListeGattung[i - 1] = -1;
            }

            /*Summen lesen - Briefwahl*/
            lHVDatenLfd = new EclHVDatenLfd();
            lHVDatenLfd.mandant = lDbBundle.clGlobalVar.mandant;
            lHVDatenLfd.ident = 600 + i;
            lHVDatenLfd.benutzer = listenNr;
            lDbBundle.dbHVDatenLfd.read(lHVDatenLfd);
            if (lDbBundle.dbHVDatenLfd.anzErgebnis() != 0) {/*Bereits ein Eintrag vorhanden*/
                lHVDatenLfd = lDbBundle.dbHVDatenLfd.ergebnisPosition(0);
                briefwahlSummeBerechnetGattung[i - 1] = Long.parseLong(lHVDatenLfd.wert);
            } else {
                briefwahlSummeBerechnetGattung[i - 1] = -1;
            }

        }
        /* Feststellungsdatum lesen*/
        lHVDatenLfd = new EclHVDatenLfd();
        lHVDatenLfd.mandant = lDbBundle.clGlobalVar.mandant;
        lHVDatenLfd.ident = 60 + 1;
        lHVDatenLfd.benutzer = listenNr;
        lDbBundle.dbHVDatenLfd.read(lHVDatenLfd);
        if (lDbBundle.dbHVDatenLfd.anzErgebnis() != 0) {/*Bereits ein Eintrag vorhanden*/
            lHVDatenLfd = lDbBundle.dbHVDatenLfd.ergebnisPosition(0);
            datumZeitFeststellung = lHVDatenLfd.wert;
        } else {
            datumZeitFeststellung = "9999.99.99 99:99:99";
        }
    }

    /**Satz 71 lesen - */
    public int leseWillenserklaerungElektronischesTeilnehmerverzeichnis() {
        int letzteNummer = 0;

        EclHVDatenLfd lHVDatenLfd = new EclHVDatenLfd();
        lHVDatenLfd = new EclHVDatenLfd();
        lHVDatenLfd.mandant = lDbBundle.clGlobalVar.mandant;
        lHVDatenLfd.ident = 71;
        lHVDatenLfd.benutzer = 0;
        lDbBundle.dbHVDatenLfd.read(lHVDatenLfd);
        if (lDbBundle.dbHVDatenLfd.anzErgebnis() != 0) {/*Bereits ein Eintrag vorhanden*/
            lHVDatenLfd = lDbBundle.dbHVDatenLfd.ergebnisPosition(0);
            letzteNummer = Integer.parseInt(lHVDatenLfd.wert);
        } else {
            letzteNummer = 0;
        }
        return letzteNummer;
    }

    public void schreibeWillenserklaerungElektronischesTeilnehmerverzeichnis(int letzteNummer) {
        EclHVDatenLfd lHVDatenLfd = new EclHVDatenLfd();
        lHVDatenLfd.mandant = lDbBundle.clGlobalVar.mandant;
        lHVDatenLfd.ident = 71;
        lHVDatenLfd.benutzer = 0;
        String wert = Integer.toString(letzteNummer);
        insertOrUpdate(lHVDatenLfd, wert);

    }
}
