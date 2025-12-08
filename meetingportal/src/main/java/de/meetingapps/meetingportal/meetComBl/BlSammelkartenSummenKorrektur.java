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
import de.meetingapps.meetingportal.meetComEntities.EclAbstimmung;
import de.meetingapps.meetingportal.meetComEntities.EclMeldung;
import de.meetingapps.meetingportal.meetComEntities.EclWeisungMeldung;
import de.meetingapps.meetingportal.meetComKonst.KonstDBAbstimmungen;
import de.meetingapps.meetingportal.meetComKonst.KonstStimmart;

public class BlSammelkartenSummenKorrektur {

    public int logDrucken=10;

    public EclMeldung lSammelMeldung;
    public EclWeisungMeldung lSammelWeisungMeldung = null;

    public EclAbstimmung[] abstimmungsliste = null;
    public long summeAktionaereGesamt = 0;
    public long[][] summenAktionaere = null;

    public EclAbstimmung[] abstimmungslisteGegen = null;
    public long[][] summenAktionaereGegen = null;

    private DbBundle dbBundle=null;

    public BlSammelkartenSummenKorrektur(DbBundle pDbBundle) {
        dbBundle=pDbBundle;
    }


    /**
     * füllt:
     * lSammelMeldung
     * abstimmungsliste
     * abstimmungslisteGegen
     */
    public void einlesenAbstimmungsliste() {
        /*Agenda einlesen*/
//        dbBundle.dbAbstimmungen.leseWeisungenPortalAgenda();
        dbBundle.dbAbstimmungen.leseAlleAbstimmungen(KonstDBAbstimmungen.sortierung_anzeigePosInternWeisungen, KonstDBAbstimmungen.selektion_alleInternAktiv, false);
        abstimmungsliste = dbBundle.dbAbstimmungen.abstimmungenArray;
        CaBug.druckeLog("abstimmungsliste länge="+abstimmungsliste.length, logDrucken, 10);
        
//        dbBundle.dbAbstimmungen.leseWeisungenPortalGegenantraege();
//        abstimmungslisteGegen = dbBundle.dbAbstimmungen.abstimmungenArray;
        abstimmungslisteGegen = new EclAbstimmung[0];
        CaBug.druckeLog("abstimmungslisteGegen länge="+abstimmungslisteGegen.length, logDrucken, 10);

    }


    /**-1 => Sammelkarte nicht gefunden
     * -2 => keine Sammelkarte
     * 
     * füllt:
     * lSammelMeldung
     * abstimmungsliste
     * abstimmungslisteGegen
     * */
    public int einlesenEinerSammelkarte(int aktuelleSammelIdent) {
        int rc=dbBundle.dbMeldungen.leseZuIdent(aktuelleSammelIdent);
        if (rc<1) {return -1;}
        lSammelMeldung = dbBundle.dbMeldungen.meldungenArray[0];
        if (lSammelMeldung.meldungstyp != 2) {
            return -2;
        }
        vorbereitenEinerSammelkarte(aktuelleSammelIdent);
        return 1;
    }
    
    
    /**Benötigt gefüllt:
     * lSammelMeldung
     */
    public void vorbereitenEinerSammelkarte(int aktuelleSammelIdent) {


        /*Weisungssatz zur Sammelkarte einlesen*/
        dbBundle.dbWeisungMeldung.leseZuMeldungsIdent(aktuelleSammelIdent, false);

        lSammelWeisungMeldung = dbBundle.dbWeisungMeldung
                .weisungMeldungGefunden(0); /*Für Sammelkarte nur ein Satz möglich*/

        /*Summe für Aktionäre vorinitialisieren*/
        summeAktionaereGesamt = 0;
        summenAktionaere = new long[abstimmungsliste.length][10];
        for (int i = 0; i < abstimmungsliste.length; i++) {
            for (int i1 = 0; i1 <= 9; i1++) {
                summenAktionaere[i][i1] = 0;
            }
        }

        summenAktionaereGegen = new long[abstimmungslisteGegen.length][10];
        for (int i = 0; i < abstimmungslisteGegen.length; i++) {
            for (int i1 = 0; i1 <= 9; i1++) {
                summenAktionaereGegen[i][i1] = 0;
            }
        }

        /**Aktionäre einlesen - nur aktive*/
        dbBundle.dbWeisungMeldung.leseAktionaersWeisungZuSammelkarte(aktuelleSammelIdent, 1);
        for (int i1 = 0; i1 < dbBundle.dbWeisungMeldung.aktionaersWeisungenZuSammelkarteAnzGefunden(); i1++) {
            long aktionaersStimmen = dbBundle.dbWeisungMeldung.aktionaersWeisungenZuSammelkarteGefunden(i1).stimmen;
            summeAktionaereGesamt += aktionaersStimmen;

            for (int i = 0; i < abstimmungsliste.length; i++) {
                int posInWeisung = abstimmungsliste[i].identWeisungssatz;
                if (posInWeisung != -1) {
                    int abstimmungsArt = dbBundle.dbWeisungMeldung
                            .aktionaersWeisungenZuSammelkarteGefunden(i1).weisungMeldung.abgabe[posInWeisung];
                    summenAktionaere[i][abstimmungsArt] += aktionaersStimmen;
                }
            }

            for (int i = 0; i < abstimmungslisteGegen.length; i++) {
                int posInWeisung = abstimmungslisteGegen[i].identWeisungssatz;
                if (posInWeisung != -1) {
                    int abstimmungsArt = dbBundle.dbWeisungMeldung
                            .aktionaersWeisungenZuSammelkarteGefunden(i1).weisungMeldung.abgabe[posInWeisung];
                    summenAktionaereGegen[i][abstimmungsArt] += aktionaersStimmen;
                }
            }

        }

    }

    
    public void kopiereAktionaersdatenInSammelkartenSummen() {
        
        lSammelMeldung.stueckAktien = summeAktionaereGesamt;
        lSammelMeldung.stimmen = summeAktionaereGesamt;

        /*Weisung*/
        for (int i = 0; i < abstimmungsliste.length; i++) {
            int pos = abstimmungsliste[i].identWeisungssatz;
            if (pos != -1) {
                if (lSammelWeisungMeldung.weisungMeldungSplit.nichtBerechnen[pos]!=1) {
                    for (int stimmart = 0; stimmart <= 9; stimmart++) {
                        if (stimmart != KonstStimmart.splitLiegtVor) {
                            lSammelWeisungMeldung.weisungMeldungSplit.abgabe[pos][stimmart] = summenAktionaere[i][stimmart];
                        }
                    }
                }

            }
        }
        for (int i = 0; i < abstimmungslisteGegen.length; i++) {
            int pos = abstimmungslisteGegen[i].identWeisungssatz;
            if (pos != -1) {
                if (lSammelWeisungMeldung.weisungMeldungSplit.nichtBerechnen[pos]!=1) {
                    for (int stimmart = 0; stimmart <= 9; stimmart++) {
                        if (stimmart != KonstStimmart.splitLiegtVor) {
                            lSammelWeisungMeldung.weisungMeldungSplit.abgabe[pos][stimmart] = summenAktionaereGegen[i][stimmart];
                        }
                    }

                }
            }
        }

    }
    
    /**Zu Belegen sind:
     * in lSammelMeldung: stueckAktien, stimmen
     * 
     * lSammelWeisungMeldung.weisungMeldungSplit.abgabe
     * 
     */
    public void zurueckschreibenEinerSammelkarte() {
        dbBundle.dbMeldungen.update(lSammelMeldung);

        dbBundle.dbWeisungMeldung.update(lSammelWeisungMeldung, null, false);

        
    }
    
    public void verarbeiteAlleSammelkarten() {
        try {
        dbBundle.dbMeldungen.leseAlleAktivenSammelkarten(-1);
        EclMeldung[] sammelkartenListe=dbBundle.dbMeldungen.meldungenArray;
        
        dbBundle.dbBasis.beginTransactionNeu();

        if (sammelkartenListe!=null) {
            for (int i=0;i<sammelkartenListe.length;i++) {
                lSammelMeldung=sammelkartenListe[i];
                int sammelIdent=lSammelMeldung.meldungsIdent;
                vorbereitenEinerSammelkarte(sammelIdent);
                kopiereAktionaersdatenInSammelkartenSummen();
                zurueckschreibenEinerSammelkarte();
                dbBundle.dbBasis.zwischenCommitNeu();
                CaBug.druckeLog("SammelIdent="+sammelIdent, logDrucken, 3);
//              try {
//              Thread.sleep(20000);
//          } catch (InterruptedException e) {
//              e.printStackTrace();
//          }

            }
        }
        
        dbBundle.dbBasis.endTransactionNeu();
        } catch (Exception e2) {
            dbBundle.dbBasis.endTransactionNeu();
            CaBug.drucke("001");
            System.err.println(" " + e2.getMessage());
        }

    }
    
}
