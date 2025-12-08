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

import java.util.List;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclBesitzAREintrag;
import de.meetingapps.meetingportal.meetComEntities.EclBesitzJeKennung;
import de.meetingapps.meetingportal.meetComEntities.EclZugeordneteMeldungNeu;
import de.meetingapps.meetingportal.meetComKonst.KonstWillenserklaerungWeg;

/**Funktionen zum Überprüfen und verarbeiten der vorher
 * mit BlWillenserklaerungStatus eingelesen Status-Daten 
 *
 */
public class BlWillenserklaerungStatusNeuVerarbeiten {

    private int logDrucken = 3;

    private DbBundle lDbBundle = null;

    /**Zu verarbeitender / überprüfender Status. Kann entweder den kompletten Status beinhalten,
     * oder besitzJeKennungPraesentListe oder besitzJeKennungNichtPraesentListe
     */
    public List<EclBesitzJeKennung> besitzJeKennungListe = null;

    public boolean rcAusgewaehlteVorhanden = false;
    public boolean rcInSammelkarteVorhanden = false;

    public BlWillenserklaerungStatusNeuVerarbeiten(DbBundle pDbBundle) {
        lDbBundle = pDbBundle;
    }

    /**++++++++Interne Variablen zum Steuern des "Durchlaufens"+++++++++++++*/

    /**1=pruefeAufAusgewaehltUndWeisung
     * 2=storniereSammelkartenEintraegeUndBucheZugang
     * 3=bucheAbgang
     */
    private int funktion = 0;

    private boolean alle = false;

    private int virtuellerTeilnehmer = 0;

    /**Wird ggf. als Bevollmächtigter eingetragen:
     * > Für die "nicht eigenen Bestände": immer
     * > Für die "eigenen Bestände": wenn abweichend von virtueller Teilnehmer
     * 			Ist Spezielle Lösung wg. ku217 - sollte zukünftig über Vertreter-Kennung gelöst werden.
     */

    private int bevollmaechtigter = 0;
    private String bevollmaechtigterName = "";
    private String bevollmaechtigterVorname = "";
    private String bevollmaechtigterOrt = "";
    private boolean zugangAuchFuerEigeneMoeglich = false;

    /**Überprüft, ob überhaupt ein Besitz ausgewählt (soweit nicht pAlle==true übergeben wird),
     * und ob für irgendeinen der Besitze eine Weisung / Briefwahl etc. abgegeben wurde.
     * 
     * Input:
     * > besitzJeKennungListe
     * 
     * Output:
     * > rcAusgewaehlteVorhanden
     * > rcInSammelkarteVorhanden
     */
    public int pruefeAufAusgewaehltUndWeisung(boolean pAlle, boolean pZugangAuchFuerEigeneMoeglich) {

        alle = pAlle;
        funktion = 1;

        rcAusgewaehlteVorhanden = false;
        rcInSammelkarteVorhanden = false;
        zugangAuchFuerEigeneMoeglich = pZugangAuchFuerEigeneMoeglich;
        laufeAlleDurch();

        return 1;
    }

    public int storniereSammelkartenEintraegeUndBucheZugang(boolean pAlle, int pVirtuellerTeilnehmer,
            int pBevollmaechtigter, String pBbevollmaechtigterName, String pBevollmaechtigterVorname,
            String pBevollmaechtigterOrt, boolean pZugangAuchFuerEigeneMoeglich) {

        alle = pAlle;
        funktion = 2;
        virtuellerTeilnehmer = pVirtuellerTeilnehmer;
        bevollmaechtigter = pBevollmaechtigter;
        bevollmaechtigterName = pBbevollmaechtigterName;
        bevollmaechtigterVorname = pBevollmaechtigterVorname;
        bevollmaechtigterOrt = pBevollmaechtigterOrt;
        zugangAuchFuerEigeneMoeglich = pZugangAuchFuerEigeneMoeglich;
        laufeAlleDurch();

        return 1;
    }

    public int bucheAbgang(boolean pAlle, int pVirtuellerTeilnehmer) {
        alle = pAlle;
        funktion = 3;
        virtuellerTeilnehmer = pVirtuellerTeilnehmer;

        laufeAlleDurch();

        return 1;
    }

    private void laufeAlleDurch() {
        for (int i = 0; i < besitzJeKennungListe.size(); i++) {
            ergaenzeAllesUmPraesenzdaten_ListeEclBesitzAREintrag(besitzJeKennungListe.get(i).eigenerAREintragListe,
                    true);
            ergaenzeAllesUmPraesenzdaten_ListeEclZugeordneteMeldung(
                    besitzJeKennungListe.get(i).zugeordneteMeldungenEigeneGastkartenListe, false);
            ergaenzeAllesUmPraesenzdaten_ListeEclZugeordneteMeldung(
                    besitzJeKennungListe.get(i).zugeordneteMeldungenBevollmaechtigtListe, false);
            ergaenzeAllesUmPraesenzdaten_ListeEclZugeordneteMeldung(
                    besitzJeKennungListe.get(i).zugeordneteMeldungenBevollmaechtigtMitGesetzlichListe, false);
            ergaenzeAllesUmPraesenzdaten_ListeEclBesitzAREintrag(besitzJeKennungListe.get(i).instiAREintraegeListe,
                    true);
            ergaenzeAllesUmPraesenzdaten_ListeEclZugeordneteMeldung(
                    besitzJeKennungListe.get(i).zugeordneteMeldungenInstiListe, false);
        }
    }

    private void ergaenzeAllesUmPraesenzdaten_ListeEclBesitzAREintrag(List<EclBesitzAREintrag> pEigenerAREintragListe,
            boolean pSindEigeneAktien) {
        for (int i = 0; i < pEigenerAREintragListe.size(); i++) {
            ergaenzeAllesUmPraesenzdaten_ListeEclZugeordneteMeldung(
                    pEigenerAREintragListe.get(i).zugeordneteMeldungenListe, pSindEigeneAktien);
        }
    }

    private void ergaenzeAllesUmPraesenzdaten_ListeEclZugeordneteMeldung(
            List<EclZugeordneteMeldungNeu> pZugeordneteMeldungListe, boolean pSindEigeneAktien) {
        for (int i = 0; i < pZugeordneteMeldungListe.size(); i++) {
            EclZugeordneteMeldungNeu lZugeordneteMeldung = pZugeordneteMeldungListe.get(i);

            if ((lZugeordneteMeldung.ausgewaehlt == true || alle == true)
                    && (pSindEigeneAktien == false || zugangAuchFuerEigeneMoeglich == true)) {
                /*pruefeAufAusgewaehltUndWeisung*/
                if (funktion == 1) {
                    rcAusgewaehlteVorhanden = true;
                    if (lZugeordneteMeldung.anzAlleKIAVSRV > 0) {
                        rcInSammelkarteVorhanden = true;
                    }
                }

                /*storniereSammelkartenEintraegeUndBucheZugang*/
                if (funktion == 2) {
                    if (lZugeordneteMeldung.anzAlleBriefwahl > 0) {
                        CaBug.druckeLog("lZugeordneteMeldung.anzAlleBriefwahl=" + lZugeordneteMeldung.anzAlleBriefwahl,
                                logDrucken, 10);
                        /*Willenserklärung stornieren*/
                        BlWillenserklaerung vmWillenserklaerung = new BlWillenserklaerung();
                        vmWillenserklaerung.pErteiltAufWeg = KonstWillenserklaerungWeg.onlineTeilnahme;
                        vmWillenserklaerung.pErteiltZeitpunkt = "";
                        vmWillenserklaerung.piMeldungsIdentAktionaer = lZugeordneteMeldung.meldungsIdent;
                        vmWillenserklaerung.pAufnehmendeSammelkarteIdent = lZugeordneteMeldung.eclMeldung.meldungEnthaltenInSammelkarte;
                        CaBug.druckeLog("vmWillenserklaerung.piMeldungsIdentAktionaer="
                                + vmWillenserklaerung.piMeldungsIdentAktionaer, logDrucken, 10);
                        vmWillenserklaerung.pWillenserklaerungGeberIdent = 0; /*Egal wer*/
                        vmWillenserklaerung.widerrufBriefwahl(lDbBundle);
                        if (vmWillenserklaerung.rcIstZulaessig == false) {
                            CaBug.drucke("001 vmWillenserklaerung.rcGrundFuerUnzulaessig="
                                    + vmWillenserklaerung.rcGrundFuerUnzulaessig);
                        }
                    }

                    BlPraesenzVirtuell blPraesenzVirtuell = new BlPraesenzVirtuell(lDbBundle);
                    blPraesenzVirtuell.buchenAktionaer_zugang(lZugeordneteMeldung.eclMeldung, virtuellerTeilnehmer,
                            bevollmaechtigter, pSindEigeneAktien, bevollmaechtigterName, bevollmaechtigterVorname,
                            bevollmaechtigterOrt);
                }
            }

            /*bucheAbgang*/
            if (funktion == 3) {
                if (lZugeordneteMeldung.eclMeldung.statusPraesenz==1) {
                    BlPraesenzVirtuell blPraesenzVirtuell = new BlPraesenzVirtuell(lDbBundle);
                    blPraesenzVirtuell.buchenAktionaer_abgang(lZugeordneteMeldung.eclMeldung, virtuellerTeilnehmer);
                }
            }
        }
    }

}
