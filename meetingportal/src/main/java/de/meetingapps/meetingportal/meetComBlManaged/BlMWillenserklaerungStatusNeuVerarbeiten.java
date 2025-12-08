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
package de.meetingapps.meetingportal.meetComBlManaged;

import java.util.LinkedList;
import java.util.List;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComBl.BlAbstimmung;
import de.meetingapps.meetingportal.meetComEclM.EclAbstimmungM;
import de.meetingapps.meetingportal.meetComEclM.EclAbstimmungSetM;
import de.meetingapps.meetingportal.meetComEclM.EclAbstimmungenListeM;
import de.meetingapps.meetingportal.meetComEclM.EclBesitzGesamtM;
import de.meetingapps.meetingportal.meetComEclM.EclDbM;
import de.meetingapps.meetingportal.meetComEntities.EclAbstimmungSet;
import de.meetingapps.meetingportal.meetComEntities.EclBesitzAREintrag;
import de.meetingapps.meetingportal.meetComEntities.EclBesitzJeKennung;
import de.meetingapps.meetingportal.meetComEntities.EclZugeordneteMeldungNeu;
import de.meetingapps.meetingportal.meetComKonst.KonstStimmart;
import de.meetingapps.meetingportal.meetComKonst.KonstWillenserklaerungWeg;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

/**Funktionen zum Überprüfen und verarbeiten der vorher
 * mit BlWillenserklaerungStatus eingelesen Status-Daten 
 *
 */
@RequestScoped
@Named
public class BlMWillenserklaerungStatusNeuVerarbeiten {

    private int logDrucken = 1;

    @Inject
    private EclDbM eclDbM;

    private @Inject EclAbstimmungSetM eclAbstimmungSetM;
    private @Inject EclAbstimmungenListeM eclAbstimmungenListeM;
    private @Inject EclBesitzGesamtM eclBesitzGesamtM;

    /**Zu verarbeitender / überprüfender Status. Kann entweder den kompletten Status beinhalten,
     * oder besitzJeKennungPraesentListe oder besitzJeKennungNichtPraesentListe
     */
    private List<EclBesitzJeKennung> besitzJeKennungListe = null;

    private List<String> ausgewaehlteAktionaere = null;

    private boolean rcAusgewaehlteVorhanden = false;
    private boolean rcInSammelkarteVorhanden = false;

    private boolean rcStimmabgabeFuerMeldungGesperrt=false;
    
    /**++++++++Interne Variablen zum Steuern des "Durchlaufens"+++++++++++++*/

    /**1=pruefeAufAusgewaehltUndWeisung
     * 2=storniereSammelkartenEintraegeUndBucheZugang
     * 3=bucheAbgang
     */
    private int funktion = 0;

    private boolean alle = false;

   
    /**Prüft, ob irgendeine der vertretenen Meldungen für die Stimmabgabe gesperrt ist,
     * weil physikalisch präsent. Für Hybrid-Veranstaltung*
     * @return
     */
    public int pruefeObGesperrt() {
        alle=true;
        funktion=3;
        rcStimmabgabeFuerMeldungGesperrt=false;
        
        laufeAlleDurch();

        return 1;
    }
    
    /**Überprüft, ob überhaupt ein Besitz ausgewählt (soweit nicht pAlle==true übergeben wird),
     * 
     * Input:
     * > besitzJeKennungListe
     * 
     * Output:
     * > rcAusgewaehlteVorhanden
     * > rcInSammelkarteVorhanden
     */
    public int pruefeAufAusgewaehltUndBelegeAusgewaehltListe(boolean pAlle) {

        alle = pAlle;
        funktion = 1;

        rcAusgewaehlteVorhanden = false;
        rcInSammelkarteVorhanden = false;
        ausgewaehlteAktionaere = new LinkedList<String>();
        laufeAlleDurch();
        eclBesitzGesamtM.setAusgewaehlteAktionaere(ausgewaehlteAktionaere);

        return 1;
    }

    /**Aus Portal heraus*/
    public int stimmAbgabe(boolean pAlle) {

        alle = pAlle;
        funktion = 2;

        laufeAlleDurch();

        return 1;
    }

    private void laufeAlleDurch() {
        for (int i = 0; i < besitzJeKennungListe.size(); i++) {
            ergaenzeAllesUmPraesenzdaten_ListeEclBesitzAREintrag(besitzJeKennungListe.get(i).eigenerAREintragListe, true);
            ergaenzeAllesUmPraesenzdaten_ListeEclZugeordneteMeldung(besitzJeKennungListe.get(i).zugeordneteMeldungenEigeneGastkartenListe, false);
            ergaenzeAllesUmPraesenzdaten_ListeEclZugeordneteMeldung(besitzJeKennungListe.get(i).zugeordneteMeldungenBevollmaechtigtListe, false);
            ergaenzeAllesUmPraesenzdaten_ListeEclZugeordneteMeldung(besitzJeKennungListe.get(i).zugeordneteMeldungenBevollmaechtigtMitGesetzlichListe, false);
            ergaenzeAllesUmPraesenzdaten_ListeEclBesitzAREintrag(besitzJeKennungListe.get(i).instiAREintraegeListe, true);
            ergaenzeAllesUmPraesenzdaten_ListeEclZugeordneteMeldung(besitzJeKennungListe.get(i).zugeordneteMeldungenInstiListe, false);
        }
    }

    private void ergaenzeAllesUmPraesenzdaten_ListeEclBesitzAREintrag(List<EclBesitzAREintrag> pEigenerAREintragListe, boolean pSindEigeneAktien) {
        for (int i = 0; i < pEigenerAREintragListe.size(); i++) {
            ergaenzeAllesUmPraesenzdaten_ListeEclZugeordneteMeldung(pEigenerAREintragListe.get(i).zugeordneteMeldungenListe, pSindEigeneAktien);
        }
    }

    private void ergaenzeAllesUmPraesenzdaten_ListeEclZugeordneteMeldung(List<EclZugeordneteMeldungNeu> pZugeordneteMeldungListe, boolean pSindEigeneAktien) {
        for (int i = 0; i < pZugeordneteMeldungListe.size(); i++) {
            EclZugeordneteMeldungNeu lZugeordneteMeldung = pZugeordneteMeldungListe.get(i);

            if (lZugeordneteMeldung.ausgewaehlt == true || alle == true) {
                /*pruefeAufAusgewaehlt*/
                if (funktion == 1) {
                    ausgewaehlteAktionaere.add(lZugeordneteMeldung.aktionaerTitelVornameName);
                    rcAusgewaehlteVorhanden = true;
                }

                /*stimmAbgabe*/
                if (funktion == 2) {
                    int gattungMeldung = lZugeordneteMeldung.gattung;
                    CaBug.druckeLog(lZugeordneteMeldung.aktionaersnummerFuerAnzeige, logDrucken, 1);
                    BlAbstimmung blAbstimmung = new BlAbstimmung(eclDbM.getDbBundle());
                    EclAbstimmungSet lAbstimmungset = eclAbstimmungSetM.getAbstimmungSet();

                    blAbstimmung.aktivenAbstimmungsblockSortierenNach = lAbstimmungset.aktivenAbstimmungsblockSortierenNach;
                    blAbstimmung.aktiverAbstimmungsblock = lAbstimmungset.aktiverAbstimmungsblock;
                    blAbstimmung.aktiverAbstimmungsblockIstElektronischAktiv = lAbstimmungset.aktiverAbstimmungsblockIstElektronischAktiv;
                    blAbstimmung.abstimmungenZuAktivenBlock = lAbstimmungset.abstimmungenZuAktivenBlock;
                    blAbstimmung.abstimmungen = lAbstimmungset.abstimmungen;

                    List<EclAbstimmungM> lAbstimmungenListe = eclAbstimmungenListeM.getAbstimmungenListeM();

                    blAbstimmung.starteSpeichernFuerMeldungsIdent(lZugeordneteMeldung.getMeldungsIdent());
                    for (int i1 = 0; i1 < lAbstimmungenListe.size(); i1++) {
                        EclAbstimmungM lAbstimmungM = lAbstimmungenListe.get(i1);
                        int gattungAbstimmung[] = lAbstimmungM.getStimmberechtigteGattungen();
                        CaBug.druckeLog("gattungMeldung=" + gattungMeldung + " gattungAbstimmung[gattungMeldung-1]=" + gattungAbstimmung[gattungMeldung - 1], logDrucken, 10);
                        if (lAbstimmungM.getGewaehlt() == null) {
                            lAbstimmungM.setGewaehlt("");
                        }
                        if (!lAbstimmungM.isUeberschrift() && lAbstimmungM.getGewaehlt() != null && gattungAbstimmung[gattungMeldung - 1] == 1) {
                            int abstimmungIdent = lAbstimmungM.getIdent();
                            int abstimmungVerhalten = 0;
                            switch (lAbstimmungM.getGewaehlt()) {
                            case "J":
                                abstimmungVerhalten = KonstStimmart.ja;
                                break;
                            case "N":
                                abstimmungVerhalten = KonstStimmart.nein;
                                break;
                            case "E":
                                abstimmungVerhalten = KonstStimmart.enthaltung;
                                break;
                            case "U":
                                abstimmungVerhalten = KonstStimmart.ungueltig;
                                break;
                            }
                            blAbstimmung.setzeMarkierungZuAbstimmungsIdent(abstimmungVerhalten, abstimmungIdent, KonstWillenserklaerungWeg.abstPortal);
                        }
                    }
                    blAbstimmung.beendeSpeichernFuerMeldung();
                    lZugeordneteMeldung.bereitsAbgestimmt = true;
                    lZugeordneteMeldung.ausgewaehlt = false;
                }
            }
            /*Prüfen ob gesperrt*/
            if (funktion==3) {
                int lMeldungsIdent=lZugeordneteMeldung.meldungsIdent;
                eclDbM.getDbBundle().dbAbstimmungMeldungSperre.read(lMeldungsIdent);
                if (eclDbM.getDbBundle().dbAbstimmungMeldungSperre.anzErgebnis()!=0) {
                    rcStimmabgabeFuerMeldungGesperrt=true; 
                }
                
            }

        }
    }

    public List<EclBesitzJeKennung> getBesitzJeKennungListe() {
        return besitzJeKennungListe;
    }

    public void setBesitzJeKennungListe(List<EclBesitzJeKennung> besitzJeKennungListe) {
        this.besitzJeKennungListe = besitzJeKennungListe;
    }

    public boolean isRcAusgewaehlteVorhanden() {
        return rcAusgewaehlteVorhanden;
    }

    public void setRcAusgewaehlteVorhanden(boolean rcAusgewaehlteVorhanden) {
        this.rcAusgewaehlteVorhanden = rcAusgewaehlteVorhanden;
    }

    public boolean isRcInSammelkarteVorhanden() {
        return rcInSammelkarteVorhanden;
    }

    public void setRcInSammelkarteVorhanden(boolean rcInSammelkarteVorhanden) {
        this.rcInSammelkarteVorhanden = rcInSammelkarteVorhanden;
    }

    public boolean isRcStimmabgabeFuerMeldungGesperrt() {
        return rcStimmabgabeFuerMeldungGesperrt;
    }

    public void setRcStimmabgabeFuerMeldungGesperrt(boolean rcStimmabgabeFuerMeldungGesperrt) {
        this.rcStimmabgabeFuerMeldungGesperrt = rcStimmabgabeFuerMeldungGesperrt;
    }

}
