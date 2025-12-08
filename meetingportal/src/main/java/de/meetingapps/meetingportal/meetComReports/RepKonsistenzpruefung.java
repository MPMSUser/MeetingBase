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
package de.meetingapps.meetingportal.meetComReports;

import de.meetingapps.meetingportal.meetComAllg.CaString;
import de.meetingapps.meetingportal.meetComBl.BlAbstimmungenWeisungen;
import de.meetingapps.meetingportal.meetComBl.BlSammelkarten;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclAbstimmung;
import de.meetingapps.meetingportal.meetComEntities.EclAktienregister;
import de.meetingapps.meetingportal.meetComEntities.EclMeldung;
import de.meetingapps.meetingportal.meetComEntities.EclMeldungZuSammelkarte;
import de.meetingapps.meetingportal.meetComEntities.EclWeisungMeldung;
import de.meetingapps.meetingportal.meetComKonst.KonstStimmart;
import de.meetingapps.meetingportal.meetingCoreReport.RpDrucken;
import de.meetingapps.meetingportal.meetingCoreReport.RpVariablen;

public class RepKonsistenzpruefung {


    private RpVariablen rpVariablen = null;

    
    private EclMeldung aktuelleSammelMeldung = null;
    private EclWeisungMeldung weisungenSammelkopf = null;
    private EclWeisungMeldung aktionaersSummen = null;
    private boolean sammelFehler = false;
    private BlAbstimmungenWeisungen blAbstimmungenWeisungenErfassen = null;

    

    public void drucke(DbBundle pDbBundle, RpDrucken rpDrucken) {

        rpDrucken.initListe(pDbBundle);

        rpVariablen = new RpVariablen(pDbBundle);
        rpVariablen.protokoll("01", rpDrucken);
        rpVariablen.fuelleVariable(rpDrucken, "Ueberschrift", "Konsistenzprüfung");
        rpVariablen.fuelleVariable(rpDrucken, "UeberschriftDetail", "");
        rpDrucken.startListe();

        boolean fehlerGefunden=false;
        
        /*Aktionär mit größerem Bestand als angemeldet*/
        boolean fehlerMeldungenGefunden=false;
        int rc = pDbBundle.dbJoined.read_vergleichAngemeldeteAktienZuAktienregister(2);
        if (rc > 0) {
            for (int i = 0; i < rc; i++) {
                String hString = pDbBundle.dbJoined.ergebnisKeyPosition(i);
                if (!hString.isEmpty()) {
                    fehlerGefunden=true;fehlerMeldungenGefunden=true;
                    EclAktienregister lAktienregisterEintrag = new EclAktienregister();
                    lAktienregisterEintrag.aktienregisterIdent = Integer.parseInt(hString);
                    pDbBundle.dbAktienregister.leseZuAktienregisterEintrag(lAktienregisterEintrag);
                    lAktienregisterEintrag = pDbBundle.dbAktienregister.ergebnisPosition(0);

                    String protokollEintrag="Aktionärsnummer= "+lAktienregisterEintrag.aktionaersnummer+" Stimmen="+CaString.toStringDE(lAktienregisterEintrag.stimmen)
                        + " Angemeldete Aktien größer als Registerbestand";
                    rpVariablen.fuelleFeld(rpDrucken, "Text", protokollEintrag);
                    rpDrucken.druckenListe();
                }
            }
        }
        if (fehlerMeldungenGefunden==false) {
            rpVariablen.fuelleFeld(rpDrucken, "Text", "ok - Keine Aktionäre mit Bestand größer als Aktienregister angemeldet gefunden");
            rpDrucken.druckenListe();
        }

        
        /*Inaktive Weisungen*/
        boolean fehlerInaktiveWeisungenGefunden=false;
        pDbBundle.dbWeisungMeldung.leseInaktive();
        int anz = pDbBundle.dbWeisungMeldung.anzWeisungMeldungGefunden();
        EclWeisungMeldung[] lWeisungMeldungArray = pDbBundle.dbWeisungMeldung.weisungMeldungArray;
        for (int i = 0; i < anz; i++) {
            fehlerGefunden=true;fehlerInaktiveWeisungenGefunden=true;
            int meldeIdent = lWeisungMeldungArray[i].meldungsIdent;
            pDbBundle.dbWeisungMeldung.leseAktiveZuMeldung(meldeIdent);
            int anzAktive = pDbBundle.dbWeisungMeldung.anzWeisungMeldungGefunden();

            pDbBundle.dbMeldungen.leseZuIdent(meldeIdent);
            String protokollEintrag="Inaktive Weisung: MeldeIdent=" + meldeIdent + " anzAktive=" + anzAktive + " AktionärsNr="
                    + pDbBundle.dbMeldungen.meldungenArray[0].aktionaersnummer;
            rpVariablen.fuelleFeld(rpDrucken, "Text", protokollEintrag);
            rpDrucken.druckenListe();

        }
        
        if (fehlerInaktiveWeisungenGefunden==false) {
            rpVariablen.fuelleFeld(rpDrucken, "Text", "ok - keine inaktiven Weisungen gefunden");
            rpDrucken.druckenListe();
        }
       
        
        /*Konsistenz Sammelkarten - Summen*/
        
        blAbstimmungenWeisungenErfassen = new BlAbstimmungenWeisungen(true, pDbBundle);
        blAbstimmungenWeisungenErfassen.leseAgendaFuerInterneWeisungenErfassung();

        
        BlSammelkarten blSammelkarten = new BlSammelkarten(true, pDbBundle);
        blSammelkarten.holeSammelkartenDaten(true, 0);

        EclMeldung[] alleSammelkarten = blSammelkarten.rcSammelMeldung;
        
        int anzahlSammelkarten = alleSammelkarten.length;
        boolean sammelFehlerGefunden=false;
        
        for (int i = 0; i < anzahlSammelkarten; i++) {
            aktuelleSammelMeldung = alleSammelkarten[i];
            sammelFehler = false;
            if (aktuelleSammelMeldung.meldungAktiv == 1) {
                blSammelkarten.leseKopfWeisungUndAktionaereZuSammelkarte(aktuelleSammelMeldung);

                int aktuelleGattung = aktuelleSammelMeldung.liefereGattung();

                weisungenSammelkopf = blSammelkarten.rcWeisungenSammelkopf;
                aktionaersSummen = blSammelkarten.rcAktionaersSummen;

                fuelleTabWeisungssummenZeigeBereich(aktuelleGattung, 1);
                fuelleTabWeisungssummenZeigeBereich(aktuelleGattung, 2);

            }
            if (sammelFehler) {
                fehlerGefunden=true;
                sammelFehlerGefunden=true;
                rpVariablen.fuelleFeld(rpDrucken, "Text", "Sammelkarte Ident "+Integer.toString(aktuelleSammelMeldung.meldungsIdent)+" Summen fehlerhaft");
                rpDrucken.druckenListe();
             }
        }
        if (sammelFehlerGefunden==false) {
            rpVariablen.fuelleFeld(rpDrucken, "Text", "ok - Sammelkarten Summen sind alle korrekt");
            rpDrucken.druckenListe();
        }

        /*Konsistenz Sammelkartenzuordnung*/
        boolean fehlerSammelkartenzuordnungGefunden=false;
        pDbBundle.dbMeldungZuSammelkarte.leseAlleAktiven();
        int anzSammelkartenzuordnungen=pDbBundle.dbMeldungZuSammelkarte.anzMeldungZuSammelkarteGefunden();
        if (anzSammelkartenzuordnungen>0) {
            EclMeldungZuSammelkarte meldungZuSammelkarteVorherige=null;
            for (int i=0;i<anzSammelkartenzuordnungen;i++) {
                EclMeldungZuSammelkarte meldungZuSammelkarteAktuell=pDbBundle.dbMeldungZuSammelkarte.meldungZuSammelkarteGefunden(i);
                if (meldungZuSammelkarteVorherige!=null) {
                    if (meldungZuSammelkarteVorherige.meldungsIdent==meldungZuSammelkarteAktuell.meldungsIdent) {
                        fehlerSammelkartenzuordnungGefunden=true;
                        fehlerGefunden=true;
                        rpVariablen.fuelleFeld(rpDrucken, "Text", "Meldung Ident "+Integer.toString(meldungZuSammelkarteVorherige.meldungsIdent)+
                                " ist mehreren Sammelkarten zugeordnet: SammelIdents "+meldungZuSammelkarteAktuell.sammelIdent+" "+meldungZuSammelkarteVorherige.sammelIdent);
                        rpDrucken.druckenListe();
                    }
                }
                meldungZuSammelkarteVorherige=meldungZuSammelkarteAktuell;
            }
        }
        if (fehlerSammelkartenzuordnungGefunden==false) {
            rpVariablen.fuelleFeld(rpDrucken, "Text", "ok - Sammelkartenzuordnungen sind alle nur einfach und damit korrekt");
            rpDrucken.druckenListe();
        }
       
        
        /*inaktive Sammelkarten mit Bestand*/
        boolean fehlerInaktiveSammelkarteMitBestandGefunden=false;
        pDbBundle.dbMeldungen.leseAlleInaktivenSammelkarten(-1);
        int anzInaktiveSammelkarten=pDbBundle.dbMeldungen.anzErgebnis();
        if (anzInaktiveSammelkarten>0) {
            for (int i=0;i<anzInaktiveSammelkarten;i++) {
                EclMeldung lMeldung=pDbBundle.dbMeldungen.meldungenArray[i];
                if (lMeldung.stueckAktien>0 || lMeldung.stimmen>0) {
                    fehlerInaktiveSammelkarteMitBestandGefunden=true;
                    fehlerGefunden=true;
                    rpVariablen.fuelleFeld(rpDrucken, "Text", "Sammelkarte Ident "+lMeldung.meldungsIdent+
                            " ist inaktiv, hat aber Bestand");
                    rpDrucken.druckenListe();
                }
            }
        }
        if (fehlerInaktiveSammelkarteMitBestandGefunden==false) {
            rpVariablen.fuelleFeld(rpDrucken, "Text", "ok - keine Bestände in inaktiven Sammelkarten");
            rpDrucken.druckenListe();
        }

        
        /*0-Bestände in Sammelkarten*/
        boolean fehlerNullBestandInSammelkartenGefunden=false;
        pDbBundle.dbMeldungen.leseAlleMeldungenInSammelkarteMitNullBestand();
        int anzNullBestaendeInSammelkarten=pDbBundle.dbMeldungen.anzErgebnis();
        if (anzNullBestaendeInSammelkarten>0) {
            for (int i=0;i<anzNullBestaendeInSammelkarten;i++) {
                EclMeldung lMeldung=pDbBundle.dbMeldungen.meldungenArray[i];
                fehlerNullBestandInSammelkartenGefunden=true;
                fehlerGefunden=true;
                rpVariablen.fuelleFeld(rpDrucken, "Text", "Meldung Ident "+lMeldung.meldungsIdent+
                        " ist 0 Bestand in Sammelkarte Ident "+lMeldung.meldungEnthaltenInSammelkarte);
                rpDrucken.druckenListe();
            }
        }
        if (fehlerNullBestandInSammelkartenGefunden==false) {
            rpVariablen.fuelleFeld(rpDrucken, "Text", "ok - keine 0-Bestände in Sammelkarten");
            rpDrucken.druckenListe();
        }
        
        /*Endergebnis anzeigen*/
        if (fehlerGefunden) {
            rpVariablen.fuelleFeld(rpDrucken, "Text", "Fehler!!! Unbedingt vor HV bereinigen!!!");
            rpDrucken.druckenListe();
        }
        else {
            rpVariablen.fuelleFeld(rpDrucken, "Text", "ok - keine Fehler gefunden, HV kann durchgeführt werden");
            rpDrucken.druckenListe();
        }
        
        rpDrucken.endeListe();
    }


    /**
     * pArt= 1 => "Normale" Agenda 2 => Gegenanträge
     */
    private void fuelleTabWeisungssummenZeigeBereich(int aktuelleGattung, int pArt) {

        int anzahlAbstimmungen = 0;
        if (pArt == 1) {
            anzahlAbstimmungen = blAbstimmungenWeisungenErfassen.liefereAnzAgendaArray(aktuelleGattung);
        } else {
            anzahlAbstimmungen = blAbstimmungenWeisungenErfassen.liefereAnzGegenantraegeArray(aktuelleGattung);
        }

        for (int i = 0; i < anzahlAbstimmungen/* CInjects.weisungsAgendaAnzAgenda[aktuelleGattung] */; i++) {
            EclAbstimmung lAbstimmung = null;
            if (pArt == 1) {
                lAbstimmung = blAbstimmungenWeisungenErfassen.rcAgendaArray[aktuelleGattung][i];
            } else {
                lAbstimmung = blAbstimmungenWeisungenErfassen.rcGegenantraegeArray[aktuelleGattung][i];
            }

            int abstimmungsPosition = lAbstimmung.identWeisungssatz;
            if (abstimmungsPosition != -1) {
                /* Erst ermitteln, ob Fehler in gesamter Summe - denn dann ganze Zeile rot! */
                long summeGesamt = 0;
                for (int i1 = 0; i1 <= 9; i1++) {
                    if (i1 != KonstStimmart.splitLiegtVor) {
                        summeGesamt += weisungenSammelkopf.weisungMeldungSplit.abgabe[abstimmungsPosition][i1];
                    }
                }
                if (summeGesamt != aktuelleSammelMeldung.stimmen) {
                    sammelFehler = true;
                }

                for (int i1 = 0; i1 <= 9; i1++) {
                    if (i1 != KonstStimmart.splitLiegtVor) {
                        long wertSammelkarte = weisungenSammelkopf.weisungMeldungSplit.abgabe[abstimmungsPosition][i1];
                        long wertAktionaere = aktionaersSummen.weisungMeldungSplit.abgabe[abstimmungsPosition][i1];
                        if (wertSammelkarte != wertAktionaere) {
                            sammelFehler = true;
                        }
                    }
                }
            }
        }

    }

}
