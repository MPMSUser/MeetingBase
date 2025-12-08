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
import de.meetingapps.meetingportal.meetComAllg.CaDatumZeit;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclMeldung;
import de.meetingapps.meetingportal.meetComEntities.EclStimmkarten;
import de.meetingapps.meetingportal.meetComEntities.EclStimmkartenSecond;
import de.meetingapps.meetingportal.meetComEntities.EclWillenserklaerung;
import de.meetingapps.meetingportal.meetComEntities.EclWillenserklaerungZusatz;
import de.meetingapps.meetingportal.meetComEntities.EclZutrittsIdent;
import de.meetingapps.meetingportal.meetComEntities.EclZutrittskarten;
import de.meetingapps.meetingportal.meetComKonst.KonstWillenserklaerung;

/**Funktionen, mit denen ein Aktionär (samt sämtzlichen Zutritts- und Stimmkartenidents) zum Gast und umgekehrt werden kann.
 * Nutzung z.B.:
 * > beim Entzug einer Vollmacht an einen Dritten
 * > beim Erteilen SRV während der HV, bzw. umgekehrt beim Widerruf auf der HV
 * @author N.N
 *
 */
public class BlAktionaerWechselGast {

    private DbBundle lDbBundle;

    public BlAktionaerWechselGast(DbBundle pDbBundle) {
        lDbBundle = pDbBundle;
    }

    /*+++++++++++++++++Übergabeparameter für diese Klasse global verfügbar+++++++++++++++++++++*/
    private EclMeldung gEclMeldung;
    private int gPersonIdent;
    private int gTatsaechlichePersonIdent = 0;

    /*++++++++++++++++Zwischenergebnisse++++++++++++++++++++++++++++++++*/
    /**Enthält die Gastmeldung, auf die die Zutritts/Stimmkarten zukünftig verweisen sollen*/
    private EclMeldung gEclMeldungGast = new EclMeldung();
    private EclZutrittsIdent gEclZutrittsIdent = new EclZutrittsIdent();

    /**"Umbauen" der ZutrittsKarten*/
    private int wechselZutrittsKartezuGast() {
        int rc;

        rc = lDbBundle.dbZutrittskarten.readGueltigeZuMeldungPerson(gEclMeldung.meldungsIdent, gPersonIdent);
        if (rc < 0) {
            CaBug.drucke("BlAktionaerWechselGast.wechselZutrittsKarte 001");
            return rc;
        }
        if (rc == 0) {
            return 1;
        }
        for (int i = 0; i < lDbBundle.dbZutrittskarten.anzErgebnis(); i++) {
            EclZutrittskarten lZutrittskarte = lDbBundle.dbZutrittskarten.ergebnisPosition(i);
            gEclZutrittsIdent.zutrittsIdent = lZutrittskarte.zutrittsIdent;
            gEclZutrittsIdent.zutrittsIdentNeben = lZutrittskarte.zutrittsIdentNeben;
            lZutrittskarte.gueltigeKlasse = 0;
            if (lDbBundle.param.paramAkkreditierung.plfdHVDelayed == 0) {
                lZutrittskarte.gueltigeKlasse_Delayed = 0;
            } else {
                lZutrittskarte.delayedVorhanden = 1;
            }
            lZutrittskarte.meldungsIdentGast = gEclMeldungGast.meldungsIdent;
            lDbBundle.dbZutrittskarten.update(lZutrittskarte);
        }
        return 1;
    }

    /**"Umbauen" der ZutrittsKarten*/
    private int wechselZutrittsKartezuAktionaer() {
        int rc;

        rc = lDbBundle.dbZutrittskarten.readGueltigeZuMeldungPerson(gEclMeldung.meldungsIdent, gPersonIdent);
        if (rc < 0) {
            CaBug.drucke("BlAktionaerWechselGast.wechselZutrittsKartezuAktionaer 001");
            return rc;
        }
        if (rc == 0) {
            return 1;
        }
        for (int i = 0; i < lDbBundle.dbZutrittskarten.anzErgebnis(); i++) {
            EclZutrittskarten lZutrittskarte = lDbBundle.dbZutrittskarten.ergebnisPosition(i);
            gEclZutrittsIdent.zutrittsIdent = lZutrittskarte.zutrittsIdent;
            gEclZutrittsIdent.zutrittsIdentNeben = lZutrittskarte.zutrittsIdentNeben;
            lZutrittskarte.gueltigeKlasse = 1;
            if (lDbBundle.param.paramAkkreditierung.plfdHVDelayed == 0) {
                lZutrittskarte.gueltigeKlasse_Delayed = 1;
            } else {
                lZutrittskarte.delayedVorhanden = 1;
            }
            lDbBundle.dbZutrittskarten.update(lZutrittskarte);
        }
        return 1;
    }

    /**"Umbauen" der StimmKarten*/
    private int wechselStimmKarteZuGast() {
        int rc;

        rc = lDbBundle.dbStimmkarten.readGueltigeZuMeldungPerson(gEclMeldung.meldungsIdent, gPersonIdent);
        if (rc < 0) {
            CaBug.drucke("BlAktionaerWechselGast.wechselStimmKarte 001");
            return rc;
        }
        if (rc == 0) {
            return 1;
        }
        for (int i = 0; i < lDbBundle.dbStimmkarten.anzErgebnis(); i++) {
            EclStimmkarten lStimmkarte = lDbBundle.dbStimmkarten.ergebnisPosition(i);
            lStimmkarte.gueltigeKlasse = 0;
            if (lDbBundle.param.paramAkkreditierung.plfdHVDelayed == 0) {
                lStimmkarte.gueltigeKlasse_Delayed = 0;
            } else {
                lStimmkarte.delayedVorhanden = 1;
            }
            lStimmkarte.meldungsIdentGast = gEclMeldungGast.meldungsIdent;
            lStimmkarte.stimmkarteIstGesperrt = 1;
            lDbBundle.dbStimmkarten.update(lStimmkarte);
        }
        return 1;
    }

    /**"Umbauen" der StimmKarten*/
    private int wechselStimmKarteZuAktionaer() {
        int rc;

        rc = lDbBundle.dbStimmkarten.readGueltigeZuMeldungPerson(gEclMeldung.meldungsIdent, gPersonIdent);
        if (rc < 0) {
            CaBug.drucke("BlAktionaerWechselGast.wechselStimmKarteZuAktionaer 001");
            return rc;
        }
        if (rc == 0) {
            return 1;
        }
        for (int i = 0; i < lDbBundle.dbStimmkarten.anzErgebnis(); i++) {
            EclStimmkarten lStimmkarte = lDbBundle.dbStimmkarten.ergebnisPosition(i);
            lStimmkarte.gueltigeKlasse = 1;
            if (lDbBundle.param.paramAkkreditierung.plfdHVDelayed == 0) {
                lStimmkarte.gueltigeKlasse_Delayed = 1;
            } else {
                lStimmkarte.delayedVorhanden = 1;
            }
            lDbBundle.dbStimmkarten.update(lStimmkarte);
        }
        return 1;
    }

    /**"Umbauen" der StimmKarten*/
    private int wechselStimmKarteSecondZuGast() {
        int rc;

        rc = lDbBundle.dbStimmkartenSecond.readGueltigeZuMeldungPerson(gEclMeldung.meldungsIdent, gPersonIdent);
        if (rc < 0) {
            CaBug.drucke("BlAktionaerWechselGast.wechselStiwechselStimmKarteSecondZuGastmmKarteSecond 001");
            return rc;
        }
        if (rc == 0) {
            return 1;
        }
        for (int i = 0; i < lDbBundle.dbStimmkartenSecond.anzErgebnis(); i++) {
            EclStimmkartenSecond lStimmkarteSecond = lDbBundle.dbStimmkartenSecond.ergebnisPosition(i);
            lStimmkarteSecond.gueltigeKlasse = 0;
            if (lDbBundle.param.paramAkkreditierung.plfdHVDelayed == 0) {
                lStimmkarteSecond.gueltigeKlasse_Delayed = 0;
            } else {
                lStimmkarteSecond.delayedVorhanden = 1;
            }
            lStimmkarteSecond.meldungsIdentGast = gEclMeldungGast.meldungsIdent;
            lStimmkarteSecond.stimmkarteSecondIstGesperrt = 1;
            lDbBundle.dbStimmkartenSecond.update(lStimmkarteSecond);
        }
        return 1;
    }

    /**"Umbauen" der StimmKarten*/
    private int wechselStimmKarteSecondZuAktionaer() {
        int rc;

        rc = lDbBundle.dbStimmkartenSecond.readGueltigeZuMeldungPerson(gEclMeldung.meldungsIdent, gPersonIdent);
        if (rc < 0) {
            CaBug.drucke("BlAktionaerWechselGast.wechselStimmKarteSecondZuAktionaer 001");
            return rc;
        }
        if (rc == 0) {
            return 1;
        }
        for (int i = 0; i < lDbBundle.dbStimmkartenSecond.anzErgebnis(); i++) {
            EclStimmkartenSecond lStimmkarteSecond = lDbBundle.dbStimmkartenSecond.ergebnisPosition(i);
            lStimmkarteSecond.gueltigeKlasse = 1;
            if (lDbBundle.param.paramAkkreditierung.plfdHVDelayed == 0) {
                lStimmkarteSecond.gueltigeKlasse_Delayed = 1;
            } else {
                lStimmkarteSecond.delayedVorhanden = 1;
            }
            lDbBundle.dbStimmkartenSecond.update(lStimmkarteSecond);
        }
        return 1;
    }

    /**Speichern Willenserklaerung*/
    private int speichereWillenserklaerung(int willenserklaerungArt) {
        EclWillenserklaerung lWillenserklaerung = new EclWillenserklaerung();
        EclWillenserklaerungZusatz lWillenserklaerungZusatz = new EclWillenserklaerungZusatz();
        lWillenserklaerung.willenserklaerung = willenserklaerungArt;
        lWillenserklaerung.meldungsIdent = gEclMeldung.meldungsIdent;
        lWillenserklaerung.meldungsIdentGast = gEclMeldungGast.meldungsIdent;

        lWillenserklaerung.identifikationDurch = 2;
        lWillenserklaerung.identifikationKlasse = 1;
        lWillenserklaerung.identifikationZutrittsIdent = gEclZutrittsIdent.zutrittsIdent;
        lWillenserklaerung.identifikationZutrittsIdentNeben = gEclZutrittsIdent.zutrittsIdentNeben;

        lWillenserklaerung.veraenderungszeit = CaDatumZeit.DatumZeitStringFuerDatenbank();

        lDbBundle.dbWillenserklaerung.insert(lWillenserklaerung, lWillenserklaerungZusatz);

        return 1;

    }

    /**Ermitteln: gibt es schon eine Gastmeldung für diese gPersonIdent? Falls nein, Erzeugen
     * In jedem Fall steht die Gastmeldung, die verwendet werden soll, anschließend in
     * gEclMeldungGast*/
    private int festlegenGastMeldung() {
        int erg;
        gEclMeldungGast = new EclMeldung();
        gEclMeldungGast.personenNatJurIdent = gTatsaechlichePersonIdent;
        erg = lDbBundle.dbMeldungen.leseZuPersonenNatJurIdent(gEclMeldungGast, 0);
        if (erg < 0) {
            return erg;
        }
        if (erg > 0) { /*Bereits Gastmeldung zu dieser Person vorhanden*/
            gEclMeldungGast = lDbBundle.dbMeldungen.meldungenArray[0];
        } else { /*Neue Gastmeldung anlegen*/
            BlWillenserklaerung lBlWillenserklaerung = new BlWillenserklaerung();
            lBlWillenserklaerung.pEclMeldungNeu = new EclMeldung();
            lBlWillenserklaerung.pEclMeldungNeu.personenNatJurIdent = gTatsaechlichePersonIdent;
            lBlWillenserklaerung.anmeldungGast(lDbBundle);
            if (!lBlWillenserklaerung.rcIstZulaessig) {
                return lBlWillenserklaerung.rcGrundFuerUnzulaessig;
            }
            EclMeldung neueMeldung = new EclMeldung();
            neueMeldung.meldungsIdent = lBlWillenserklaerung.rcMeldungen[0];
            lDbBundle.dbMeldungen.leseZuMeldungsIdent(neueMeldung);
            gEclMeldungGast = lDbBundle.dbMeldungen.meldungenArray[0];
        }

        return 1;
    }

    /**für pPersonIdent wird - falls noch nicht vorhanden - eine Gastmeldung angelegt (ansonsten bestehende
     * Gastmeldung verwenden).
     * 
     * Alle Zutritts- und Stimm-Idents des Aktionärs pEclMeldungAktionaer, vertreten aktuell durch pPersonIdent,
     * werden auf Gaststatus gesetzt und verweisen dann auf die (ggf. neuangelegte) Gastmeldung.
     * 
     * Willenserklärung für den Wechsel erzeugen.
     * 
     * Eingabeparameter:
     * > pPersonIdent kann auch -1 (=Aktionär selbst) sein.
     *  
     *  
     * Voraussetzungen:
     * > Aktionär ist nicht (mehr) präsent
     * > Es gibt Zutrittskarten und/oder Stimmkarten für pPersonIdent
     * 
     * Zum DELAY: Wechsel von Aktionär auf Gast - und umgekehrt, sowie alle "Höherwertige Funktionen" sind ab Beginn der Abstimmung
     * nicht mehr möglich! Dementsprechend werden alle ggf. dennoch abgesetzten Funktionen Delayed.
     */
    public int nichtAnwesenderAktionaerZuGast(EclMeldung pEclMeldungAktionaer, int pPersonIdent) {
        int erg;

        gEclMeldung = pEclMeldungAktionaer;
        gPersonIdent = pPersonIdent;

        /**in pPersonIdent kann -1 (=Aktionär) oder eine Ident (=Bevollmächtigter) stehen.
         * In tatsaechlichePersonIdent steht immer eine Ident, die auf einen Satz in PersonenNatJur
         * verweist.
         */
        gTatsaechlichePersonIdent = gPersonIdent;
        if (gTatsaechlichePersonIdent == -1) {
            gTatsaechlichePersonIdent = gEclMeldung.personenNatJurIdent;
        }

        /*+++++++Ermitteln: gibt es schon eine Gastmeldung für diese personIdent? Falls nein, Erzeugen+++++
         * In jedem Fall steht die Gastmeldung, die verwendet werden soll, anschließend in
         * gEclMeldungGast*/
        erg = festlegenGastMeldung();
        if (erg < 0) {
            return erg;
        }

        /*ZutrittsIdents "umbiegen"*/
        wechselZutrittsKartezuGast();

        /*Stimmkarten umbiegen*/
        wechselStimmKarteZuGast();

        /*StimmkartenSecond umbiegen*/
        wechselStimmKarteSecondZuGast();

        /*Willenserklärung erzeugen*/
        speichereWillenserklaerung(KonstWillenserklaerung.wechselAktionaerZuGast);

        return 1;
    }

    /**
     * Alle Zutritts- und Stimm-Idents des Aktionärs pEclMeldungAktionaer, vertreten aktuell durch pPersonIdent,
     * werden von Gaststatus auf Aktionärsstatus gesetzt und verweisen dann wieder auf den Aktionär.
     * Voraussetzung: Gastmeldung und Aktionärsmeldung sind in den Idents der Zutritts/Stimmkarten eingetragen.
     * 
     * Willenserklärung für den Wechsel erzeugen.
     * 
     * Eingabeparameter:
     * > pPersonIdent kann auch -1 (=Aktionär selbst) sein.
     *  
     *  
     * Voraussetzungen:
     * > Aktionär und Gast ist nicht (mehr) präsent
     * > Es gibt Zutrittskarten und/oder Stimmkarten für pPersonIdent
     * 
     * Zum DELAY: Wechsel von Aktionär auf Gast - und umgekehrt, sowie alle "Höherwertige Funktionen" sind ab Beginn der Abstimmung
     * nicht mehr möglich! Dementsprechend werden alle ggf. dennoch abgesetzten Funktionen Delayed.
     */
    public int nichtAnwesenderGastZuAktionaer(EclMeldung pEclMeldungAktionaer, int pPersonIdent) {
        gEclMeldung = pEclMeldungAktionaer;
        gPersonIdent = pPersonIdent;

        /**in pPersonIdent kann -1 (=Aktionär) oder eine Ident (=Bevollmächtigter) stehen.
         * In tatsaechlichePersonIdent steht immer eine Ident, die auf einen Satz in PersonenNatJur
         * verweist.
         */
        gTatsaechlichePersonIdent = gPersonIdent;
        if (gTatsaechlichePersonIdent == -1) {
            gTatsaechlichePersonIdent = gEclMeldung.personenNatJurIdent;
        }

        /*ZutrittsIdents "umbiegen"*/
        wechselZutrittsKartezuAktionaer();

        /*Stimmkarten umbiegen*/
        wechselStimmKarteZuAktionaer();

        /*StimmkartenSecond umbiegen*/
        wechselStimmKarteSecondZuAktionaer();

        /*Willenserklärung erzeugen*/
        speichereWillenserklaerung(KonstWillenserklaerung.wechselGastZuAktionaer);

        return 1;
    }

}
