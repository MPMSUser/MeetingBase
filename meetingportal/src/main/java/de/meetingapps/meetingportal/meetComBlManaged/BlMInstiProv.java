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

import java.util.List;

import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComBl.BlAbstimmungenWeisungen;
import de.meetingapps.meetingportal.meetComBl.BlWillenserklaerung;
import de.meetingapps.meetingportal.meetComBl.BlWillenserklaerungStatus;
import de.meetingapps.meetingportal.meetComEclM.EclDbM;
import de.meetingapps.meetingportal.meetComEntities.EclAktienregister;
import de.meetingapps.meetingportal.meetComEntities.EclInstiProv;
import de.meetingapps.meetingportal.meetComEntities.EclZugeordneteMeldung;
import de.meetingapps.meetingportal.meetComKonst.KonstSkIst;
import de.meetingapps.meetingportal.meetComKonst.KonstStimmart;
import de.meetingapps.meetingportal.meetComWE.WELaufInstiProvStarten;
import de.meetingapps.meetingportal.meetComWE.WELaufInstiProvStartenRC;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

/********Hinweis für Datenaufbau Import:
 * Überschrifts-TOPe dürfen nicht in Import-Liste enthalten sein.
 * 
 * TOPe, die nur bei Briefwahl oder bei SRV, aber nicht beim anderen abgegeben werden dürfen,
 * müssen in Import-liste enthalten sein. Und werden ggf. auf Stimmabgabe "nicht definiert" gesetzt.
 */

@RequestScoped
public class BlMInstiProv {

    @Inject
    EclDbM eclDbM;

    public WELaufInstiProvStartenRC verarbeiten(WELaufInstiProvStarten pWELaufInstiProvStarten) {

        weLaufInstiProvStartenRC = new WELaufInstiProvStartenRC();

        weLaufInstiProvStarten = pWELaufInstiProvStarten;
        switch (weLaufInstiProvStarten.funktion) {
        case 1:
            einspielenListe(weLaufInstiProvStarten);
            break;
        case 2:
            verarbeiten = false;
            durchfuerenlauf();
            break;
        case 3:
            verarbeiten = true;
            nurWeisung=false;
            durchfuerenlauf();
            break;
        case 4:
            verarbeiten = true;
            nurWeisung=true;
            durchfuerenlauf();
            break;
        }

        return weLaufInstiProvStartenRC;

    }

    private void einspielenListe(WELaufInstiProvStarten weLaufInstiProvStarten) {
        weLaufInstiProvStartenRC = new WELaufInstiProvStartenRC();

        List<EclInstiProv> listInstiProv = weLaufInstiProvStarten.listInstiProv;
        if (listInstiProv == null) {
            return;
        }

        /**Neue Lauf-Nummer vergeben*/
        int verarbeitungslauf = eclDbM.getDbBundle().dbBasis.getInterneIdentInstiProvLauf();
        weLaufInstiProvStartenRC.laufNummer = verarbeitungslauf;

        for (int i = 0; i < listInstiProv.size(); i++) {
            EclInstiProv lInstiProv = listInstiProv.get(i);
            lInstiProv.verarbeitungslauf = verarbeitungslauf;
            eclDbM.getDbBundle().dbInstiProv.insert(lInstiProv);
        }
        return;
    }

    private EclInstiProv aktuelleInstiProv = null;
    private boolean verarbeiten = false;
    private boolean nurWeisung=false;
    private WELaufInstiProvStarten weLaufInstiProvStarten = null;
    private WELaufInstiProvStartenRC weLaufInstiProvStartenRC = null;

    private BlAbstimmungenWeisungen blAbstimmungenWeisungenErfassen = null;

    private void durchfuerenlauf() {
        weLaufInstiProvStartenRC = new WELaufInstiProvStartenRC();

        eclDbM.getDbBundle().dbInstiProv.readAll(weLaufInstiProvStarten.laufNr);
        int anzahlSaetze = eclDbM.getDbBundle().dbInstiProv.anzErgebnis();
        if (anzahlSaetze == 0) {
            return;
        }

        blAbstimmungenWeisungenErfassen = new BlAbstimmungenWeisungen(true, eclDbM.getDbBundle());
        blAbstimmungenWeisungenErfassen.leseAgendaFuerInterneWeisungenErfassung();

        EclInstiProv[] listeInstiProv = eclDbM.getDbBundle().dbInstiProv.ergebnisArray;

        for (int i = 0; i < anzahlSaetze; i++) {
            aktuelleInstiProv = listeInstiProv[i];
            if (verarbeiten == false || (verarbeiten == true && aktuelleInstiProv.codeVerarbeitetVerarbeitung != 1)) {
                if (verarbeiten == false) {
                    aktuelleInstiProv.codeVerarbeitetTest = 0;
                    aktuelleInstiProv.textVerarbeitetTest = "";
                }

                verarbeitenEinzelSatz();
                eclDbM.getDbBundle().dbInstiProv.update(aktuelleInstiProv);
                eclDbM.closeAll();
                eclDbM.openAll();
            }
        }
        return;
    }

    private void verarbeitenEinzelSatz() {
        int rc = 0;

        /*SammelIdent holen*/
        int sammelIdent = aktuelleInstiProv.istGewaehlteSammelkarteIdent;
        rc = eclDbM.getDbBundle().dbMeldungen.leseZuIdent(sammelIdent);

        if (rc < 1) {
            setzeFehler(-1, "Sammelkarte unbekannt");
            return;
        }
        /*1=KIAV 2=SRV 3=organisatorisch 4=Briefwahl 5=Dauervollmacht*/
        aktuelleInstiProv.sammelkartenart = eclDbM.getDbBundle().dbMeldungen.meldungenArray[0].skIst;

        int rcLauf1=-1;
        if (aktuelleInstiProv.istVorrangFremdbesitz == 0) {
            rcLauf1 = verarbeiteEoderF(1, false);
        } else {
            rcLauf1 = verarbeiteEoderF(2, false);
        }
        int rcLauf2=-1;
        if (rcLauf1 == -1) {/*Nicht verarbeitbar - nun versuchen, den anderen Besitz zu verarbeiten*/
            if (aktuelleInstiProv.istVorrangFremdbesitz == 0) {
                rcLauf2 = verarbeiteEoderF(2, false);
            } else {
                rcLauf2 = verarbeiteEoderF(1, false);
            }
        }
        if (rcLauf1==-1 && rcLauf2==-1) {
            /*Beide Läufe nicht erfolgreich. Nun versuchen mit geringerem Bestand anzumelden*/
            if (aktuelleInstiProv.istVorrangFremdbesitz == 0) {
                rcLauf1 = verarbeiteEoderF(1, true);
            } else {
                rcLauf1 = verarbeiteEoderF(2, true);
            }
            
        }

    }

    /**pfinal = true => Verarbeitung erfolgt mit geringerem Bestand*/
    private int verarbeiteEoderF(int pEoderF, boolean pFinal) {

        String aktionaersnummer = aktuelleInstiProv.aktionaersnummer;
        if (eclDbM.getDbBundle().param.paramBasis.namensaktienAktiv==true) {
            if (pEoderF == 1) {
                aktionaersnummer += "0";
            } else {
                aktionaersnummer += "1";
            }
        }

        EclAktienregister lAktienregister = new EclAktienregister();
        lAktienregister.aktionaersnummer = aktionaersnummer;

        eclDbM.getDbBundle().dbAktienregister.leseZuAktienregisterEintrag(lAktienregister);
        if (eclDbM.getDbBundle().dbAktienregister.anzErgebnis() == 0) {
            setzeFehler(CaFehler.afAktienregisterEintragNichtMehrVorhanden, "Aktionärsnummer unbekannt");
            return -1;
        }

        lAktienregister = eclDbM.getDbBundle().dbAktienregister.ergebnisPosition(0);

        aktuelleInstiProv.aktienzahlAR = lAktienregister.stueckAktien;
        aktuelleInstiProv.aktionaersnameAR = aktionaersnummer + " " + lAktienregister.nameKomplett;
        if (aktuelleInstiProv.aktionaersnameAR.length() > 190) {
            aktuelleInstiProv.aktionaersnameAR = aktuelleInstiProv.aktionaersnameAR.substring(0, 189);
        }

        if (lAktienregister.stueckAktien == 0) {
            setzeFehler(-1, "0-Bestand");
            return -1;
        }

        if (lAktienregister.stueckAktien < aktuelleInstiProv.aktienzahl && aktuelleInstiProv.aktienzahl != 0) {
            aktuelleInstiProv.textVerarbeitetTest = "Aktienzahl im Bestand kleiner als angegeben";
        }
        aktuelleInstiProv.codeVerarbeitetTest = 1;

        if (verarbeiten) {
            
            int meldungsIdentAktionaer=0;

            if (nurWeisung==false) {
                /***********Anmelden*************************/
                BlWillenserklaerung lWillenserklaerung = null;

                lWillenserklaerung = new BlWillenserklaerung();
                lWillenserklaerung.pErteiltAufWeg = weLaufInstiProvStarten.weLoginVerify.getEingabeQuelle();
                lWillenserklaerung.pErteiltZeitpunkt = weLaufInstiProvStarten.weLoginVerify.getErteiltZeitpunkt();

                lWillenserklaerung.pEclAktienregisterEintrag = lAktienregister;

                /*Restliche Parameter füllen*/
                if (aktuelleInstiProv.istFixAnmeldung != 1) {
                    lWillenserklaerung.pAktienAnmelden = -1; /*Alle Aktien anmelden*/
                    lWillenserklaerung.pAnmeldungFix = false; /*Nicht "Fix" anmelden*/
                    lWillenserklaerung.pAnzahlAnmeldungen = 1;
                } else {
                    lWillenserklaerung.pAktienAnmelden = aktuelleInstiProv.aktienzahl; /*Aktien anmelden*/
                    lWillenserklaerung.pAnmeldungFix = true; /*"Fix" anmelden*/
                    if (pFinal) {
                        lWillenserklaerung.pAnmeldungFixRest=true;
                    }
                }
                lWillenserklaerung.pWillenserklaerungGeberIdent = -1; /*Aktionär gibt in diesem Fall*/

                lWillenserklaerung.anmeldungAusAktienregister(eclDbM.getDbBundle());

                if (lWillenserklaerung.rcIstZulaessig == false) {
                    setzeFehler(lWillenserklaerung.rcGrundFuerUnzulaessig,
                            CaFehler.getFehlertext(lWillenserklaerung.rcGrundFuerUnzulaessig, 0));
                    return -1;
                }
                meldungsIdentAktionaer=lWillenserklaerung.rcMeldungen[0];

            }
            else {
                /**********Nicht Anmelden, sondern nur bestehende Anmeldung holen um dort Weisung einzutragen*************/
                BlWillenserklaerungStatus blWillenserklaerungStatus = new BlWillenserklaerungStatus(eclDbM.getDbBundle());
                blWillenserklaerungStatus.piAnsichtVerarbeitungOderAnalyse = 2;
                blWillenserklaerungStatus.piRueckgabeKurzOderLang = 2;
                blWillenserklaerungStatus.piSelektionGeberOderAlle = 2;
                blWillenserklaerungStatus.leseMeldungenZuAktienregisterIdent(lAktienregister.aktienregisterIdent);
                blWillenserklaerungStatus.ergaenzeZugeordneteMeldungenUmWillenserklaerungen(-2);
                if (blWillenserklaerungStatus.zugeordneteMeldungenEigeneAktienArray==null ||
                        blWillenserklaerungStatus.zugeordneteMeldungenEigeneAktienArray.length==0) {
                    setzeFehler(CaFehler.afBestandNichtAngemeldet, CaFehler.getFehlertext(CaFehler.afBestandNichtAngemeldet, 0));
                    return -1;
                }
                EclZugeordneteMeldung lZugeordneteMeldung=blWillenserklaerungStatus.zugeordneteMeldungenEigeneAktienArray[0];
                if (lZugeordneteMeldung.anzKIAVSRV+lZugeordneteMeldung.anzBriefwahl>0) {
                    setzeFehler(CaFehler.afKIAVSRVBriefwahlBereitsVorhanden, CaFehler.getFehlertext(CaFehler.afKIAVSRVBriefwahlBereitsVorhanden, 0));
                    return -1;
                }
                meldungsIdentAktionaer=lZugeordneteMeldung.meldungsIdent;
            }
            
            /*Achtung wichtiger Hinweis: umgestellt auf neue Weisungsverarbeitung (BlAbstimmungenWeisungenErfassen). Noch nicht getestet!*/

            //			Alte Weisungslogik
            //			aFunktionen.leseAbstimmungsliste(eclDbM.getDbBundle(), lAktienregister.getGattungId());
            //			System.out.println("BlMInstiProv Gattung="+lAktienregister.getGattungId());

            /*******In Sammelkarte mit Weisungen speichern********************/

            BlWillenserklaerung vwWillenserklaerung = new BlWillenserklaerung();

            vwWillenserklaerung.piMeldungsIdentAktionaer = meldungsIdentAktionaer;
            vwWillenserklaerung.pWillenserklaerungGeberIdent = -1; /*Aktionär*/

            /*Sammelkartennr setzen*/
            vwWillenserklaerung.pAufnehmendeSammelkarteIdent = aktuelleInstiProv.istGewaehlteSammelkarteIdent;

            int aktuelleGattung = lAktienregister.getGattungId();
            blAbstimmungenWeisungenErfassen.initWeisungMeldungNichtMarkiert(1);

            //			int offset=0;
            //			List<EclAbstimmungM> lAbstimmungenListe=eclAbstimmungenListeM.getAbstimmungenListeM();
            //			for (int i=0;i<lAbstimmungenListe.size();i++){
            //				if (!lAbstimmungenListe.get(i).isUeberschrift()){
            //					int posInWeisung=lAbstimmungenListe.get(i).getIdentWeisungssatz();
            //					int stimmart=KonstStimmart.frei;
            //					if (aktuelleInstiProv.einzelMarkierungen[i+1-offset]!=0){
            //						stimmart=aktuelleInstiProv.einzelMarkierungen[i+1-offset];
            //					}
            //
            //					switch (stimmart){
            //					case KonstStimmart.ja:vwWillenserklaerung.pEclWeisungMeldungRaw.abgabe[posInWeisung]=" X";vwWillenserklaerung.pEclWeisungMeldung.abgabe[posInWeisung]=1;break;
            //					case KonstStimmart.nein:vwWillenserklaerung.pEclWeisungMeldungRaw.abgabe[posInWeisung]="  X";vwWillenserklaerung.pEclWeisungMeldung.abgabe[posInWeisung]=2;break;
            //					case KonstStimmart.enthaltung:vwWillenserklaerung.pEclWeisungMeldungRaw.abgabe[posInWeisung]="   X";vwWillenserklaerung.pEclWeisungMeldung.abgabe[posInWeisung]=3;break;
            //					case KonstStimmart.ungueltig:vwWillenserklaerung.pEclWeisungMeldungRaw.abgabe[posInWeisung]="    X";vwWillenserklaerung.pEclWeisungMeldung.abgabe[posInWeisung]=4;break;
            //					}
            //				}
            //				else{offset++;}
            //			}

            int skArt = 0;
            switch (aktuelleInstiProv.sammelkartenart) {
            case 1:
                skArt = KonstSkIst.kiav;
                break;
            case 2:
                skArt = KonstSkIst.srv;
                break;
            case 3:
                skArt = KonstSkIst.organisatorisch;
                break;
            case 4:
                skArt = KonstSkIst.briefwahl;
                break;
            case 5:
                skArt = KonstSkIst.dauervollmacht;
                break;
            }

            int offset = 0;
            for (int i = 0; i < blAbstimmungenWeisungenErfassen.liefereAnzAgendaArray(aktuelleGattung); i++) {
                if (!blAbstimmungenWeisungenErfassen.pruefeObUeberschriftAgenda(i, aktuelleGattung)) {
                    int stimmart = KonstStimmart.frei;
                    if (aktuelleInstiProv.einzelMarkierungen[i + 1 - offset] != 0) {
                        stimmart = aktuelleInstiProv.einzelMarkierungen[i + 1 - offset];
                    }
                    if (!blAbstimmungenWeisungenErfassen.pruefeObZulaessigFuerSKIstAgenda(i, aktuelleGattung, skArt)) {
                        stimmart = KonstStimmart.nichtMarkiert;
                    }

                    blAbstimmungenWeisungenErfassen.speichereAgendaWeisungMeldungPos(0, i, aktuelleGattung, stimmart,
                            skArt);
                } else {
                    offset++;
                }
            }
            if (eclDbM.getDbBundle().param.paramAbstimmungParameter.weisungenGegenantraegeInternSeparat) {
                for (int i = 0; i < blAbstimmungenWeisungenErfassen
                        .liefereAnzGegenantraegeArray(aktuelleGattung); i++) {
                    if (!blAbstimmungenWeisungenErfassen.pruefeObUeberschriftGegenantraege(i, aktuelleGattung)) {
                        int stimmart = KonstStimmart.frei;
                        if (aktuelleInstiProv.einzelMarkierungen[i + 1 - offset] != 0) {
                            stimmart = aktuelleInstiProv.einzelMarkierungen[i + 1 - offset];
                        }
                        if (blAbstimmungenWeisungenErfassen.pruefeObZulaessigFuerSKIstGegenantraege(i, aktuelleGattung,
                                skArt)) {
                            stimmart = KonstStimmart.nichtMarkiert;
                        }
                        blAbstimmungenWeisungenErfassen.speichereGegenantraegeWeisungMeldungPos(0, i, aktuelleGattung,
                                stimmart, skArt);
                    } else {
                        offset++;
                    }
                }

            }

            /*Abgegebene Weisung (uninterpretiert)
            	public EclWeisungMeldungRaw pEclWeisungMeldungRaw=null;*/
            vwWillenserklaerung.pEclWeisungMeldungRaw = blAbstimmungenWeisungenErfassen.rcWeisungMeldungRaw[0];
            /*Abgegebene Weisung (interpretiert)
            	public EclWeisungMeldung pEclWeisungMeldung=null;*/
            vwWillenserklaerung.pEclWeisungMeldung = blAbstimmungenWeisungenErfassen.rcWeisungMeldung[0];

            /*Willenserklärung speichern*/
            System.out.println("BlMInstiProv sammelkartenart ausführen=" + aktuelleInstiProv.sammelkartenart);
            switch (aktuelleInstiProv.sammelkartenart) {
            case 1:
                vwWillenserklaerung.vollmachtUndWeisungAnKIAV(eclDbM.getDbBundle());
                break;
            case 2:
                vwWillenserklaerung.vollmachtUndWeisungAnSRV(eclDbM.getDbBundle());
                break;
            case 3:
                vwWillenserklaerung.organisatorischMitWeisungInSammelkarte(eclDbM.getDbBundle());
                break;
            case 4:
                vwWillenserklaerung.briefwahl(eclDbM.getDbBundle());
                break;
            case 5:
                vwWillenserklaerung.dauervollmachtAnKIAV(eclDbM.getDbBundle());
                break;
            }

            /*Falls nicht möglich: Fehlermeldung, zum Anmelden*/
            if (vwWillenserklaerung.rcIstZulaessig == false) {
//                setzeFehler(lWillenserklaerung.rcGrundFuerUnzulaessig,
//                        CaFehler.getFehlertext(lWillenserklaerung.rcGrundFuerUnzulaessig, 0)); korrigiert, da so unsinnig!
                setzeFehler(vwWillenserklaerung.rcGrundFuerUnzulaessig,
                        CaFehler.getFehlertext(vwWillenserklaerung.rcGrundFuerUnzulaessig, 0));
                return -1;
            }

            aktuelleInstiProv.codeVerarbeitetVerarbeitung = 1;
            if (pFinal==false) {
                aktuelleInstiProv.textVerarbeitetVerarbeitung = "";
            }
            else{
                aktuelleInstiProv.textVerarbeitetVerarbeitung = "Mit geringerem Bestand angemeldet";
            }
        }

        return 1;
    }

    private void setzeFehler(int pFehler, String pText) {
        if (verarbeiten) {
            aktuelleInstiProv.codeVerarbeitetVerarbeitung = pFehler;
            aktuelleInstiProv.textVerarbeitetVerarbeitung = pText;
        } else {
            aktuelleInstiProv.codeVerarbeitetTest = pFehler;
            aktuelleInstiProv.textVerarbeitetTest = pText;
        }
    }

}
