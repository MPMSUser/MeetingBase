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

import java.util.LinkedList;
import java.util.List;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaString;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclAktienregister;
import de.meetingapps.meetingportal.meetComEntities.EclAnrede;
import de.meetingapps.meetingportal.meetComEntities.EclBesitzAREintrag;
import de.meetingapps.meetingportal.meetComEntities.EclBesitzJeKennung;
import de.meetingapps.meetingportal.meetComEntities.EclLoginDaten;
import de.meetingapps.meetingportal.meetComEntities.EclMeldung;
import de.meetingapps.meetingportal.meetComEntities.EclMeldungZuSammelkarte;
import de.meetingapps.meetingportal.meetComEntities.EclPersonenNatJur;
import de.meetingapps.meetingportal.meetComEntities.EclVorlaeufigeVollmacht;
import de.meetingapps.meetingportal.meetComEntities.EclWeisungMeldung;
import de.meetingapps.meetingportal.meetComEntities.EclWillenserklaerung;
import de.meetingapps.meetingportal.meetComEntities.EclWillenserklaerungStatusNeu;
import de.meetingapps.meetingportal.meetComEntities.EclWillenserklaerungStatusTextElement;
import de.meetingapps.meetingportal.meetComEntities.EclWillenserklaerungZusatz;
import de.meetingapps.meetingportal.meetComEntities.EclZugeordneteMeldungNeu;
import de.meetingapps.meetingportal.meetComEntities.EclZutrittsIdent;
import de.meetingapps.meetingportal.meetComKonst.KonstLoginKennungArt;
import de.meetingapps.meetingportal.meetComKonst.KonstPortalTexte;
import de.meetingapps.meetingportal.meetComKonst.KonstWillenserklaerung;
import de.meetingapps.meetingportal.meetComKonst.KonstWillenserklaerungPortalTexte;

public class BlWillenserklaerungStatusNeu {

    private int logDrucken = 3;

    private DbBundle lDbBundle = null;

    /**+++++++++Eingabe-Werte zur Steuerung++++++++++++*/
    /**false=alle Willenserklärungen werden übertragen; true=nur die nicht stornierten werden übertragen*/
    public boolean nurNichtStornierteWillenserklaerungen = true;

    public boolean umZugeordneteKennungenErgaenzen = true;

    /**1 = aus Speicherplatzgründen werden die Ecl s selbst nicht mit im Array zurückgeliefert, sondern nur in 
     * "aufbereiteter Form" (=V2)
     * 2 = die vollständigen EclWillenserklarung, EclWillenserklaerungZusatz, EclMeldung werden mit zurückgegeben
     * 
     * Hinweis: 2 ist nur sinnvoll, wenn gleichzeitig auch piAnsichtVerarbeitungOderAnalyse==1. Ansonsten werden beim 
     * "Zusammenfassen" der Willenserklärungen ja alte Verweise auf eclWillenserklaerung überschrieben!
     */
    public int piRueckgabeKurzOderLang = 1;

    /**1 = es wird die "Verarbeitungsansicht" zurückgeliefert:
     * Veränderungen und Stornierungen werden mit der ursprünglichen Willenserklärung zusammengefaßt und tauchen
     * nicht separat in der Willenserklärungsliste auf. Dies ist die Ansicht für den "Aktionär" oder für manuelle
     * zusätzliche Erfassung von Willenserklärungen (=V2)
     * 2 = es wird die Analysesicht zurückgeliefert:
     * Willenserklärungen, die verändert oder storniert wurden, werden zwar als solche Gekennzeichnet. Die
     * "Veränderungs- oder Stornierungs-Willenserklärung" wird jedoch in einer separaten Willenserklärung zurückgeliefert.
     * Ansicht für den Komplett-Ausdruck aller Willenserklärungen z.B. für Analyse oder Übertragungszwecke
     */
    public int piAnsichtVerarbeitungOderAnalyse = 1;

    /**=0 => nur die fürs Portal relevanten Willenserklärungen werden zurückgegeben
     * =1 => auch Präsenzwillenserklärungen werden aufbereitet
     */
    public int piAlleWillenserklaerungen = 0;

    /**1 = es werden nur die Willenserklärungen zurückgegeben, die der "aktuell eingeloggte" Teilnehmer abgegeben hat
     * 		d.i.: 
     * 		> bei Aktionär angemeldet: alle, die der Aktionär gegeben hat (also z.B. keine Untervollmachten)
     * 		> bei natürlicher Person: alle, die diese natürliche Person gegeben hat
     * (zu V2: Standardoutput bei BlWillenserklaerungStatus)
     * 2 = es werden alle Willenserklärungen zurückgegeben, die zu dieser Anmeldung gehören. Also "über- und unter-Vollmachten".
     */
    public int piSelektionGeberOderAlle = 1;

    /**Die folgenden Listen enthalten jeweils die Idents, die bei einem
     * Portal-Login nicht vertreten werden. null => es werden alle vertreten
     */
    public List<Integer> piAusblendenMeldungen = null;

    /**+++++++++++++++++Ergebnis-Parameter+++++++++++++++++++++++++++++++*/

    /**Wenn true, dann wurden der eingeloggten Kennung weitere Kennungen zugeordnet und genehmigt - im Portal
     * wird dann vor den Besitzen jeder Kennung ein Trenn-Text angezeigt.
     */
    public boolean weitereKennungen = false;
    
    /**true >= mindestens eine Kennung ist eine Insti-Kennung*/
    public boolean instiKennungZugeordnet=false;

    /**Wenn true, dann wurden der eingeloggten Kennung weitere Kennungen zugeordnet aber noch nicht genehmigt*/
    public boolean weitereKennungenNichtGenehmigt = false;

    /**In besitzeJeKennungListe sind nicht-angemeldete Bestände vorhanden. D.h. dann:
     * > Anzeige eines Starttexte bzgl. Anmeldung noch erforderlich bzw. (nach Anmeldeschluß) Anmeldeschluß abgelaufen, Anmeldungen der Bestände nicht mehr möglich
     * > Ggf. "Sammel-Anmeldebuttons" anbieten.
     */
    public boolean nichtAngemeldeteVorhanden = false;

    /**In besitzeJeKennungListe sind angemeldete Bestände vorhanden. D.h. dann:
     * > Anzeige eines Starttextes, dass ggf. weitere Aktionen möglich sind.
     * > Ggf. "Sammel-Aktionrs-Buttons" anbieten.
     */
    public boolean angemeldeteVorhanden = false;

    /**In besitzeJeKennungListe sind angemeldete Bestände vorhanden, die
     * NICHT ausgeblendet sind. D.h. ausschlaggebender Wert
     * für die Wahrnehmung von Aktionärsrechten mit der Bedingung
     * "Anmeldeter Aktionär".:
     */
    public boolean angemeldeteNichtAusgeblendeteVorhanden = false;

    /**Nur Portal: In besitzeJeKennungListe sind Bestände vorhanden, die ausgeblendet sind,
     * d.h. von eingeloggten Person nicht vertreten werden.
     */
    public boolean ausgeblendeteVorhanden = false;

    /**Für Portal: Es sind Einträge vorhanden, für die von einer anderen als der angemeldeten
     * Person Weisung erteilt wurden
     */
    public boolean bereitsErteiltWeisungBriefwahlAllgemeinDurchAndereVorhanden = false;

    /**Anzahl der Aktien, die insgesamt angemeldet / Vertreten werden. D.h. Summe aus allen Meldungen,
     * die ausgeblendet=false haben
     */
    public long aktienInsgesamtAngemeldet = 0;

    /**Liste aller Kennungen, die zusammengefaßt wurden (und deren
     * Zusammenfassung auch genehmigt ist). [0]=die Kennung, mit der
     * gerade eingeloggt ist
     */
    public List<EclBesitzJeKennung> besitzJeKennungListe = null;

    /**Liste aller Kennungen, die zugeordnet wurden, aber deren Zuordnung
     * noch nicht genehmigt / überprüft wurde. Enthält nur die 
     * Kennungen, nicht jedoch die diesen Kennungen zugeordneten Meldungen
     */
    public List<EclBesitzJeKennung> besitzJeKennungNichtGenehmigtListe = null;

    /**Listen für die Anzeige der bereits präsenten / nicht präsenten
     * Daten für die Online-Teilnahme.
     * Wird gefülltg durch fuellePraesenzList()
     */
    public List<EclBesitzJeKennung> besitzJeKennungPraesentListe = null;
    public List<EclBesitzJeKennung> besitzJeKennungNichtPraesentListe = null;

    /**Portal: Liste aller Meldungen, für die von einer anderen Person bereits Weisung erteilt wurde*/
    public List<EclZugeordneteMeldungNeu> meldungenBereitsErteiltWeisungAllgemeinDurchAndereListe = null;

    /**Wenn irgendwo irgendeine Briefwahl vorhanden ist, dann wird das auf true gesetzt.
     * TODO klären, ob noch gebraucht
     */
    public boolean briefwahlVorhanden = false;

    /**Wenn irgendwo irgendeine Stimmrechtsvertreter vorhanden ist, dann wird das auf true gesetzt.
     * TODO klären, ob noch gebraucht
     */
    public boolean srvVorhanden = false;

    /**
     * Gibt an, ob in allen zugeordneten / eingelesenen Sätzen auch ein präsenter vorhanden ist.
     * 
     * Achtung, bei nurRawLiveAbstimmung==1 immer false, da in diesem Fall der Präsenzstatus nicht
     * in die Tables zurückgespeichert wird, sondern nur im Speicher gehalten wird.
     */
    public int anzPraesenteVorhanden = 0;
    public int anzPraesenteVorhandenEigene = 0;
    public int anzPraesenteVorhandenVertretene = 0;
    /**
     * Gibt an, ob in allen zugeordneten / eingelesenen Sätzen auch ein nicht-präsenter vorhanden ist.
     * 
     * Achtung, bei nurRawLiveAbstimmung==1 immer false, da in diesem Fall der Präsenzstatus nicht
     * in die Tables zurückgespeichert wird, sondern nur im Speicher gehalten wird.
     */
    public int anzNichtPraesenteVorhanden = 0;
    public int anzNichtPraesenteVorhandenEigene = 0;
    public int anzNichtPraesenteVorhandenVertretene = 0;

    public boolean rcHatNichtNurPortalWillenserklaerungen = false;
    public String rcDatumLetzteWillenserklaerung = "";

    /**Gibt an, ob im Besitz die jeweilige Gattung vertreten ist
     * [gattung-1]*/
    public int gattungen[] = { 0, 0, 0, 0, 0 };

    /**Wird von ermittleVollmachtenAusMeldungenUndWillenserklaerungen gefüllt*/
    public List<EclPersonenNatJur> rcListeVollmachten = null;

    public BlWillenserklaerungStatusNeu(DbBundle pDbBundle) {
        lDbBundle = pDbBundle;
        CaBug.druckeLogAufrufsequenz("", logDrucken, 10);
    }

    /*********************Initialiseren der Ausgangsdaten - entweder aus Datenbank, oder übergeben***************************/

    /**Anhand von pKennung wird eclLoginDaten, eclAktienregister, eclPersonenNatJur aus Datenbank eingelesen, dann Initialisierung*/
    public void initAusgangsdaten(String pKennung) {
        besitzJeKennungListe = new LinkedList<EclBesitzJeKennung>();
        besitzJeKennungNichtGenehmigtListe = new LinkedList<EclBesitzJeKennung>();
        EclBesitzJeKennung lBesitzJeKennung = new EclBesitzJeKennung();
        lBesitzJeKennung.kennungIntern = pKennung;
        ergaenzeAusgangsdaten(lBesitzJeKennung);
        besitzJeKennungListe.add(lBesitzJeKennung);
    }

    /**Initialisierung erfolgt anhand der übergebenen Daten*/
    public void initAusgangsdaten(EclLoginDaten pEclLoginDaten, EclAktienregister pEclAktienregister, EclPersonenNatJur pEclPersonenNatJur) {
        besitzJeKennungListe = new LinkedList<EclBesitzJeKennung>();
        besitzJeKennungNichtGenehmigtListe = new LinkedList<EclBesitzJeKennung>();
        EclBesitzJeKennung lBesitzJeKennung = new EclBesitzJeKennung();
        lBesitzJeKennung.eclAktienregister = pEclAktienregister;
        lBesitzJeKennung.eclPersonenNatJur = pEclPersonenNatJur;
        ergaenzeBesitzJeKennung(lBesitzJeKennung, pEclLoginDaten);
        besitzJeKennungListe.add(lBesitzJeKennung);
    }

    private void ergaenzeAusgangsdaten(EclBesitzJeKennung pBesitzJeKennung) {
        BlTeilnehmerLoginNeu blTeilnehmerLoginNeu = new BlTeilnehmerLoginNeu();
        blTeilnehmerLoginNeu.initDB(lDbBundle);
        blTeilnehmerLoginNeu.reloadKennung(pBesitzJeKennung.kennungIntern);
        pBesitzJeKennung.eclAktienregister = blTeilnehmerLoginNeu.eclAktienregister;
        pBesitzJeKennung.eclPersonenNatJur = blTeilnehmerLoginNeu.eclPersonenNatJur;
        ergaenzeBesitzJeKennung(pBesitzJeKennung, blTeilnehmerLoginNeu.eclLoginDaten);
    }

    private void ergaenzeBesitzJeKennung(EclBesitzJeKennung pBesitzJeKennung, EclLoginDaten pEclLoginDaten) {
        pBesitzJeKennung.kennungIntern = pEclLoginDaten.loginKennung;
        pBesitzJeKennung.kennungArt = pEclLoginDaten.kennungArt;
        pBesitzJeKennung.berechtigungPortal = pEclLoginDaten.berechtigungPortal;
        
    }
    
    /***************************************************Komplettablauf********************************************
     * Liste Meldedaten. Präsenzdaten müssen separat gefüllt werden.
     * Mit entsprechender Doku, was ggf. zur Vereinfachung rausgelassen werden kann.
     * Voraussetzung: initAusgangsdaten wurde aufgerufen*/
    public void fuelleAlles(boolean pMitWillenserklaerungen) {
        /*Zusätzliche Kennungen einlesen*/
        if (lDbBundle.param.paramPortal.nurRawLiveAbstimmung == 0 && umZugeordneteKennungenErgaenzen) {
            ergaenzeZugeordneteKennungen();
        }

        /*Initialisieren*/
        for (int i = 0; i < 5; i++) {
            gattungen[i] = 0;
        }

        /*Meldungen füllen*/
        int besitzJeKennungListeLaenge = besitzJeKennungListe.size();
        if (besitzJeKennungListeLaenge > 1) {
            weitereKennungen = true;
        }

        if (besitzJeKennungNichtGenehmigtListe.size() > 0) {
            weitereKennungenNichtGenehmigt = true;
        }

        for (int i = 0; i < besitzJeKennungListeLaenge; i++) {
            EclBesitzJeKennung lBesitzJeKennung = besitzJeKennungListe.get(i);
            lBesitzJeKennung.kennungsArt = lBesitzJeKennung.kennungArt;

            if (lBesitzJeKennung.kennungArt == KonstLoginKennungArt.aktienregister) {
                lBesitzJeKennung.kennungFuerAnzeige = BlNummernformBasis.aufbereitenInternFuerExtern(lBesitzJeKennung.kennungIntern, lDbBundle);
                /**Kennung ist eine AKtionärsnummer => direkt zugeordnete Meldungen einlesen*/
                lBesitzJeKennung.eigenerAREintragVorhanden = true;
                EclBesitzAREintrag lBesitzAREintrag =
                        einlesenBesitzAREintrag("", lBesitzJeKennung.eclAktienregister);
 
                leseMeldungenZuAktienregister(lBesitzAREintrag, 1);
                lBesitzJeKennung.personNatJurIdent = lBesitzJeKennung.eclAktienregister.personNatJur;
                lBesitzJeKennung.eigenerAREintragListe = new LinkedList<EclBesitzAREintrag>();
                lBesitzJeKennung.eigenerAREintragListe.add(lBesitzAREintrag);
                if (pMitWillenserklaerungen) {
                    ergaenzeAREintragUmWillenserklaerungen(lBesitzAREintrag, lBesitzJeKennung.personNatJurIdent);
                    lBesitzAREintrag.gastKartenGemeldetEigeneAktien = lGastKartenGemeldetEigeneAktien;
                }
            }

            if (lBesitzJeKennung.kennungArt == KonstLoginKennungArt.personenNatJur) {
                /**Kennung ist keine Aktionärsnummer => zugeordnete Gastkarten einlesen (falls es eine Gastkarte ist)*/
                leseMeldungenEigeneGastkartenZuPersonNatJur(lBesitzJeKennung);
                lBesitzJeKennung.instiIdent=inMeldungenEigeneGastkartenEnthalteneInstiIdent;
                if (inMeldungenEigeneGastkartenEnthalteneInstiIdent>0) {
                    instiKennungZugeordnet=true;
                }
                if (pMitWillenserklaerungen) {
                    for (int i1 = 0; i1 < lBesitzJeKennung.zugeordneteMeldungenEigeneGastkartenListe.size(); i1++) {
                        ergaenzeMeldungUmWillenserklaerungen(lBesitzJeKennung.zugeordneteMeldungenEigeneGastkartenListe.get(i1), lBesitzJeKennung.personNatJurIdent);
                    }
                }
            }

            if (lDbBundle.param.paramPortal.nurRawLiveAbstimmung == 0) {
                leseMeldungenBevollmaechtigtZuPersonNatJur(lBesitzJeKennung);
                if (pMitWillenserklaerungen) {
                    for (int i1 = 0; i1 < lBesitzJeKennung.zugeordneteMeldungenBevollmaechtigtListe.size(); i1++) {
                        ergaenzeMeldungUmWillenserklaerungen(lBesitzJeKennung.zugeordneteMeldungenBevollmaechtigtListe.get(i1), lBesitzJeKennung.personNatJurIdent);
                    }
                }

                if (lBesitzJeKennung.instiIdent != 0) {
                    leseInstiBestaende(lBesitzJeKennung, pMitWillenserklaerungen);
                }
                if (lDbBundle.param.paramPortal.varianteDialogablauf == 1) {
                    /*Vorläufige Vollmachten möglich - gesetzliche dazulesen*/
                    leseVonGesetzlichenGeerbtZuBevollmaechtigten(lBesitzJeKennung);
                    if (pMitWillenserklaerungen) {
                        for (int i1 = 0; i1 < lBesitzJeKennung.zugeordneteMeldungenBevollmaechtigtMitGesetzlichListe.size(); i1++) {
                            ergaenzeMeldungUmWillenserklaerungen(lBesitzJeKennung.zugeordneteMeldungenBevollmaechtigtMitGesetzlichListe.get(i1),
                                    lBesitzJeKennung.zugeordneteMeldungenBevollmaechtigtMitGesetzlichListe.get(i).personNatJurIdent);
                        }
                    }
                }
            }
        }
        if (CaBug.pruefeLog(logDrucken, 10)) {
            for (int i = 0; i < 5; i++) {
                CaBug.druckeLog("Gattung " + (i + 1) + "=" + gattungen[i], logDrucken, 10);
            }
        }
    }

    public void ergaenzeAllesUmPraesenzdaten(int pPersonNatJur) {
        for (int i = 0; i < besitzJeKennungListe.size(); i++) {
            ergaenzeAllesUmPraesenzdaten_ListeEclBesitzAREintrag(pPersonNatJur, besitzJeKennungListe.get(i).eigenerAREintragListe, true);
            //			ergaenzeAllesUmPraesenzdaten_ListeEclZugeordneteMeldung(pPersonNatJur, besitzJeKennungListe.get(i).zugeordneteMeldungenEigeneGastkartenListe, false);
            ergaenzeAllesUmPraesenzdaten_ListeEclZugeordneteMeldung(pPersonNatJur, besitzJeKennungListe.get(i).zugeordneteMeldungenBevollmaechtigtListe, false);
            ergaenzeAllesUmPraesenzdaten_ListeEclZugeordneteMeldung(pPersonNatJur, besitzJeKennungListe.get(i).zugeordneteMeldungenBevollmaechtigtMitGesetzlichListe, false);
            ergaenzeAllesUmPraesenzdaten_ListeEclBesitzAREintrag(pPersonNatJur, besitzJeKennungListe.get(i).instiAREintraegeListe, true);
            ergaenzeAllesUmPraesenzdaten_ListeEclZugeordneteMeldung(pPersonNatJur, besitzJeKennungListe.get(i).zugeordneteMeldungenInstiListe, false);
        }
    }

    private void ergaenzeAllesUmPraesenzdaten_ListeEclBesitzAREintrag(int pPersonNatJur, List<EclBesitzAREintrag> pEigenerAREintragListe, boolean pSindEigeneAktien) {
        for (int i = 0; i < pEigenerAREintragListe.size(); i++) {
            ergaenzeAllesUmPraesenzdaten_ListeEclZugeordneteMeldung(pPersonNatJur, pEigenerAREintragListe.get(i).zugeordneteMeldungenListe, pSindEigeneAktien);
        }
    }

    /**pSindEigeneAktien==true => anz(Nicht)PraesenteVorhandenEigene wird hochgezählt
     * ==false => anz(Nicht)PraesenteVorhandeneVertretene wird hochgezählt
     */
    private void ergaenzeAllesUmPraesenzdaten_ListeEclZugeordneteMeldung(int pPersonNatJur, List<EclZugeordneteMeldungNeu> pZugeordneteMeldungListe, boolean pSindEigeneAktien) {
        /**Felder, die den Präsenzstatus bzw. den Status für den Online-Teilnahme-Start bestimmen (ohne die ganzen Willenserklärungen
         * durchnudeln zu müssen:
         * 
         * EclMeldung.
         * 	meldungEnthaltenInSammelkarte, meldungEnthaltenInSammelkarteArt 
         * 		=> ist in einer Sammelkarte, kann nicht präsent gehen
         * 
         * 	statusPraesenz, virtuellerTeilnehmerIdent
         * 		=> aktueller Präsenzstatus, aktueller virtuellerTeilnehmer
         * 
         * 	db_version
         * 		=> wenn nicht mit der im Speicher gehaltenen Version übereinstimmend, dann wurde 
         * 			am Meldesatz was verändert => komplett neu einlesen
         */

        for (int i = 0; i < pZugeordneteMeldungListe.size(); i++) {
            EclZugeordneteMeldungNeu lZugeordneteMeldung = pZugeordneteMeldungListe.get(i);
            lZugeordneteMeldung.bereitsPraesent = (lZugeordneteMeldung.eclMeldung.meldungIstPraesent() == 1);
            lZugeordneteMeldung.statusPraesenz = lZugeordneteMeldung.eclMeldung.statusPraesenz;

            if (lZugeordneteMeldung.bereitsPraesent == true) {
                int personNatJurIdentPraesent = lZugeordneteMeldung.eclMeldung.virtuellerTeilnehmerIdent;
                if (personNatJurIdentPraesent == pPersonNatJur) {
                    lZugeordneteMeldung.bereitsPraesentDurchKennung = true;
                } else {
                    lZugeordneteMeldung.bereitsPraesentDurchAndere = true;
                }
                anzPraesenteVorhanden++;
                if (pSindEigeneAktien) {
                    anzPraesenteVorhandenEigene++;
                } else {
                    anzPraesenteVorhandenVertretene++;
                }
            } else {
                anzNichtPraesenteVorhanden++;
                if (pSindEigeneAktien) {
                    anzNichtPraesenteVorhandenEigene++;
                } else {
                    anzNichtPraesenteVorhandenVertretene++;
                }
            }
        }
    }

    /**+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
     * Füllt die Listen für die Anzeige der bereits präsenten / nicht präsenten
     * Daten für die Online-Teilnahme.
     */
    public void fuellePraesenzList() {
        besitzJeKennungPraesentListe = new LinkedList<EclBesitzJeKennung>();
        besitzJeKennungNichtPraesentListe = new LinkedList<EclBesitzJeKennung>();

        for (int i = 0; i < besitzJeKennungListe.size(); i++) {
            EclBesitzJeKennung lBesitzJeKennungPraesentNeueListe = new EclBesitzJeKennung();
            EclBesitzJeKennung lBesitzJeKennungNichtPraesentNeueListe = new EclBesitzJeKennung();
            
            lBesitzJeKennungPraesentNeueListe.eclAktienregister=besitzJeKennungListe.get(i).eclAktienregister;
            lBesitzJeKennungPraesentNeueListe.eclPersonenNatJur=besitzJeKennungListe.get(i).eclPersonenNatJur;
 
            lBesitzJeKennungNichtPraesentNeueListe.eclAktienregister=besitzJeKennungListe.get(i).eclAktienregister;
            lBesitzJeKennungNichtPraesentNeueListe.eclPersonenNatJur=besitzJeKennungListe.get(i).eclPersonenNatJur;

            int gefPraesent = 0;
            int gefNichtPraesent = 0;

            fuellePraesenzList_Liste_ListeEclBesitzAREintrag(besitzJeKennungListe.get(i).eigenerAREintragListe);
            if (lBesitzAREintragPraesentNeueListe.size() > 0) {
                gefPraesent++;
            }
            if (lBesitzAREintragNichtPraesentNeueListe.size() > 0) {
                gefNichtPraesent++;
            }
            lBesitzJeKennungPraesentNeueListe.eigenerAREintragListe = lBesitzAREintragPraesentNeueListe;
            lBesitzJeKennungNichtPraesentNeueListe.eigenerAREintragListe = lBesitzAREintragNichtPraesentNeueListe;

            fuellePraesenzList_ListeEclZugeordneteMeldung(besitzJeKennungListe.get(i).zugeordneteMeldungenEigeneGastkartenListe);
            if (lZugeordneteMeldungPraesentNeueListe.size() > 0) {
                gefPraesent++;
            }
            if (lZugeordneteMeldungNichtPraesentNeueListe.size() > 0) {
                gefNichtPraesent++;
            }
            lBesitzJeKennungPraesentNeueListe.zugeordneteMeldungenEigeneGastkartenListe = lZugeordneteMeldungPraesentNeueListe;
            lBesitzJeKennungNichtPraesentNeueListe.zugeordneteMeldungenEigeneGastkartenListe = lZugeordneteMeldungNichtPraesentNeueListe;

            fuellePraesenzList_ListeEclZugeordneteMeldung(besitzJeKennungListe.get(i).zugeordneteMeldungenBevollmaechtigtListe);
            if (lZugeordneteMeldungPraesentNeueListe.size() > 0) {
                gefPraesent++;
            }
            if (lZugeordneteMeldungNichtPraesentNeueListe.size() > 0) {
                gefNichtPraesent++;
            }
            lBesitzJeKennungPraesentNeueListe.zugeordneteMeldungenBevollmaechtigtListe = lZugeordneteMeldungPraesentNeueListe;
            lBesitzJeKennungNichtPraesentNeueListe.zugeordneteMeldungenBevollmaechtigtListe = lZugeordneteMeldungNichtPraesentNeueListe;

            fuellePraesenzList_ListeEclZugeordneteMeldung(besitzJeKennungListe.get(i).zugeordneteMeldungenBevollmaechtigtMitGesetzlichListe);
            if (lZugeordneteMeldungPraesentNeueListe.size() > 0) {
                gefPraesent++;
            }
            if (lZugeordneteMeldungNichtPraesentNeueListe.size() > 0) {
                gefNichtPraesent++;
            }
            lBesitzJeKennungPraesentNeueListe.zugeordneteMeldungenBevollmaechtigtMitGesetzlichListe = lZugeordneteMeldungPraesentNeueListe;
            lBesitzJeKennungNichtPraesentNeueListe.zugeordneteMeldungenBevollmaechtigtMitGesetzlichListe = lZugeordneteMeldungNichtPraesentNeueListe;

            fuellePraesenzList_Liste_ListeEclBesitzAREintrag(besitzJeKennungListe.get(i).instiAREintraegeListe);
            if (lBesitzAREintragPraesentNeueListe.size() > 0) {
                gefPraesent++;
            }
            if (lBesitzAREintragNichtPraesentNeueListe.size() > 0) {
                gefNichtPraesent++;
            }
            lBesitzJeKennungPraesentNeueListe.instiAREintraegeListe = lBesitzAREintragPraesentNeueListe;
            lBesitzJeKennungNichtPraesentNeueListe.instiAREintraegeListe = lBesitzAREintragNichtPraesentNeueListe;

            fuellePraesenzList_ListeEclZugeordneteMeldung(besitzJeKennungListe.get(i).zugeordneteMeldungenInstiListe);
            if (lZugeordneteMeldungPraesentNeueListe.size() > 0) {
                gefPraesent++;
            }
            if (lZugeordneteMeldungNichtPraesentNeueListe.size() > 0) {
                gefNichtPraesent++;
            }
            lBesitzJeKennungPraesentNeueListe.zugeordneteMeldungenInstiListe = lZugeordneteMeldungPraesentNeueListe;
            lBesitzJeKennungNichtPraesentNeueListe.zugeordneteMeldungenInstiListe = lZugeordneteMeldungNichtPraesentNeueListe;

            if (gefPraesent > 0 || i == 0) {
                besitzJeKennungPraesentListe.add(lBesitzJeKennungPraesentNeueListe);
            }
            if (gefNichtPraesent > 0 || i == 0) {
                besitzJeKennungNichtPraesentListe.add(lBesitzJeKennungNichtPraesentNeueListe);
            }
        }

    }

    List<EclBesitzAREintrag> lBesitzAREintragPraesentNeueListe = null;
    List<EclBesitzAREintrag> lBesitzAREintragNichtPraesentNeueListe = null;

    private void fuellePraesenzList_Liste_ListeEclBesitzAREintrag(List<EclBesitzAREintrag> pEigenerAREintragListe) {
        lBesitzAREintragPraesentNeueListe = new LinkedList<EclBesitzAREintrag>();
        lBesitzAREintragNichtPraesentNeueListe = new LinkedList<EclBesitzAREintrag>();
        for (int i = 0; i < pEigenerAREintragListe.size(); i++) {
            fuellePraesenzList_ListeEclZugeordneteMeldung(pEigenerAREintragListe.get(i).zugeordneteMeldungenListe);
            if (lZugeordneteMeldungPraesentNeueListe.size() > 0) {
                EclBesitzAREintrag lBesitzAREintrag = new EclBesitzAREintrag();
                lBesitzAREintrag.zugeordneteMeldungenListe = lZugeordneteMeldungPraesentNeueListe;
                lBesitzAREintragPraesentNeueListe.add(lBesitzAREintrag);
            }
            if (lZugeordneteMeldungNichtPraesentNeueListe.size() > 0) {
                EclBesitzAREintrag lBesitzAREintrag = new EclBesitzAREintrag();
                lBesitzAREintrag.zugeordneteMeldungenListe = lZugeordneteMeldungNichtPraesentNeueListe;
                lBesitzAREintragNichtPraesentNeueListe.add(lBesitzAREintrag);
            }
        }
    }

    List<EclZugeordneteMeldungNeu> lZugeordneteMeldungPraesentNeueListe = null;
    List<EclZugeordneteMeldungNeu> lZugeordneteMeldungNichtPraesentNeueListe = null;

    private void fuellePraesenzList_ListeEclZugeordneteMeldung(List<EclZugeordneteMeldungNeu> pZugeordneteMeldungListe) {
        lZugeordneteMeldungPraesentNeueListe = new LinkedList<EclZugeordneteMeldungNeu>();
        lZugeordneteMeldungNichtPraesentNeueListe = new LinkedList<EclZugeordneteMeldungNeu>();
        for (int i = 0; i < pZugeordneteMeldungListe.size(); i++) {
            EclZugeordneteMeldungNeu lZugeordneteMeldungM = pZugeordneteMeldungListe.get(i);
            if (lZugeordneteMeldungM.bereitsPraesent) {
                lZugeordneteMeldungPraesentNeueListe.add(lZugeordneteMeldungM);
            } else {
                lZugeordneteMeldungNichtPraesentNeueListe.add(lZugeordneteMeldungM);
            }
        }
    }

    /**+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
     * Für Portal: Füllt die Listen mit allen Meldungen, für die bereits eine andere Person Weisung gegeben hat
     */
    public void fuelleMeldungenBereitsErteiltWeisungAllgemeinDurchAndereListe() {
        meldungenBereitsErteiltWeisungAllgemeinDurchAndereListe = new LinkedList<EclZugeordneteMeldungNeu>();

        for (int i = 0; i < besitzJeKennungListe.size(); i++) {

            fuelleMeldungenBereitsErteiltWeisungAllgemeinDurchAndereListe_EclBesitzAREintrag(besitzJeKennungListe.get(i).eigenerAREintragListe);

            fuelleMeldungenBereitsErteiltWeisungAllgemeinDurchAndereListe_ListeEclZugeordneteMeldung(besitzJeKennungListe.get(i).zugeordneteMeldungenEigeneGastkartenListe);

            fuelleMeldungenBereitsErteiltWeisungAllgemeinDurchAndereListe_ListeEclZugeordneteMeldung(besitzJeKennungListe.get(i).zugeordneteMeldungenBevollmaechtigtListe);

            fuelleMeldungenBereitsErteiltWeisungAllgemeinDurchAndereListe_ListeEclZugeordneteMeldung(besitzJeKennungListe.get(i).zugeordneteMeldungenBevollmaechtigtMitGesetzlichListe);

            fuelleMeldungenBereitsErteiltWeisungAllgemeinDurchAndereListe_EclBesitzAREintrag(besitzJeKennungListe.get(i).instiAREintraegeListe);

            fuelleMeldungenBereitsErteiltWeisungAllgemeinDurchAndereListe_ListeEclZugeordneteMeldung(besitzJeKennungListe.get(i).zugeordneteMeldungenInstiListe);
        }

    }

    private void fuelleMeldungenBereitsErteiltWeisungAllgemeinDurchAndereListe_EclBesitzAREintrag(List<EclBesitzAREintrag> pEigenerAREintragListe) {
        for (int i = 0; i < pEigenerAREintragListe.size(); i++) {
            fuelleMeldungenBereitsErteiltWeisungAllgemeinDurchAndereListe_ListeEclZugeordneteMeldung(pEigenerAREintragListe.get(i).zugeordneteMeldungenListe);
        }
    }

    private void fuelleMeldungenBereitsErteiltWeisungAllgemeinDurchAndereListe_ListeEclZugeordneteMeldung(List<EclZugeordneteMeldungNeu> pZugeordneteMeldungListe) {
        for (int i = 0; i < pZugeordneteMeldungListe.size(); i++) {
            EclZugeordneteMeldungNeu lZugeordneteMeldungM = pZugeordneteMeldungListe.get(i);
            if (lZugeordneteMeldungM.bereitsErteiltWeisungBriefwahlAllgemeinDurchAndere) {
                meldungenBereitsErteiltWeisungAllgemeinDurchAndereListe.add(lZugeordneteMeldungM);
            }
        }
    }

    /****************************************************Einzelfunktionen***********************************************/
    /**Ergänzung um zugeordnete Kennungen*/
    public void ergaenzeZugeordneteKennungen() {
        lDbBundle.dbZuordnungKennung.read(besitzJeKennungListe.get(0).kennungIntern);
        for (int i = 0; i < lDbBundle.dbZuordnungKennung.anzErgebnis(); i++) {
            CaBug.druckeLog("i=" + i, logDrucken, 10);
            EclBesitzJeKennung lBesitzJeKennung = new EclBesitzJeKennung();
            lBesitzJeKennung.kennungIntern = lDbBundle.dbZuordnungKennung.ergebnisPosition(i).zugeordneteKennung;
            lBesitzJeKennung.zuordnungFuerPraesenzStatus = lDbBundle.dbZuordnungKennung.ergebnisPosition(i).zuordnungIstFuerPraesenzVerifiziert;
            lBesitzJeKennung.zuordnungIstFuerPraesenzVerifiziert = (lBesitzJeKennung.zuordnungFuerPraesenzStatus == 1);
            lBesitzJeKennung.besitzIstUrsprungVonKennung = false;
            ergaenzeAusgangsdaten(lBesitzJeKennung);
            if (lBesitzJeKennung.zuordnungIstFuerPraesenzVerifiziert) {
                besitzJeKennungListe.add(lBesitzJeKennung);
            } else {
                besitzJeKennungNichtGenehmigtListe.add(lBesitzJeKennung);
            }
        }
    }

    
    
    /**Liest alle Meldungen in pBesitzAREintrag.zugeordneteMeldungenListe, die von dieser AktienregisterIdent aus angemeldet wurden.
     * Sowohl Aktionäre als auch Gastkarten!
     */
    public void leseMeldungenZuAktienregister(EclBesitzAREintrag pBesitzAREintrag, int pArtBeziehung) {
        inMeldungenEigeneGastkartenEnthalteneInstiIdent=0;
        CaBug.druckeLog("", logDrucken, 10);
        int lAktienregisterIdent = pBesitzAREintrag.aktienregisterEintrag.aktienregisterIdent;
        int rc = lDbBundle.dbMeldungen.leseZuAktienregisterIdent(lAktienregisterIdent, true); //ohne Stornos

        List<EclZugeordneteMeldungNeu> lZugeordneteMeldungenListe = new LinkedList<EclZugeordneteMeldungNeu>();
        List<EclZugeordneteMeldungNeu> lZugeordneteMeldungenGaesteListe = new LinkedList<EclZugeordneteMeldungNeu>();

        for (int i = 0; i < rc; i++) {
            EclMeldung lMeldung = lDbBundle.dbMeldungen.meldungenArray[i];

            EclZugeordneteMeldungNeu lZugeordneteMeldung = new EclZugeordneteMeldungNeu();
            lZugeordneteMeldung.artBeziehung = pArtBeziehung;
            lZugeordneteMeldung.eclMeldung = lMeldung;
            lZugeordneteMeldung.aktienregisterIdent = lAktienregisterIdent;
            lZugeordneteMeldung.aktionaersnummerFuerAnzeige = BlNummernformBasis.aufbereitenInternFuerExtern(lMeldung.aktionaersnummer, lDbBundle);
            lZugeordneteMeldung.meldungsIdent = lMeldung.meldungsIdent;
            lZugeordneteMeldung.klasse = lMeldung.klasse;

            if (lMeldung.fixAnmeldung == 1) {
                lZugeordneteMeldung.fixAnmeldung = true;
            }
            //lZugeordneteMeldung.personNatJurIdent hier nicht relevant

            fuelleAktionaersdatenInMeldung(lZugeordneteMeldung, true, lMeldung);
            setzePraesenzFelder(lZugeordneteMeldung, lMeldung);

            if (meldungIstAusgeblendet(lMeldung.meldungsIdent)) {
                lZugeordneteMeldung.ausgeblendet = true;
                ausgeblendeteVorhanden = true;
            }

            if (lMeldung.klasse == 0) {
                /*Meldung Gast, ausgehend von diesem AR-Eintrag*/
                pBesitzAREintrag.gastKartenGemeldetEigeneAktien++;

                lZugeordneteMeldungenGaesteListe.add(lZugeordneteMeldung);
            } else {
                pBesitzAREintrag.angemeldet = true;
                if (lZugeordneteMeldung.ausgeblendet == false) {
                    pBesitzAREintrag.angemeldeteNichtAusgeblendeteVorhanden = true;
                    aktienInsgesamtAngemeldet += lMeldung.stueckAktien;
                }

                lZugeordneteMeldungenListe.add(lZugeordneteMeldung);
                setzeGattungsergebnisFuerMeldung(lMeldung);
            }

        }
        pBesitzAREintrag.zugeordneteMeldungenListe = lZugeordneteMeldungenListe;
        pBesitzAREintrag.zugeordneteMeldungenGaesteListe = lZugeordneteMeldungenGaesteListe;

        /*nichtAngemeldeteVorhanden/angemeldeteVorhanden auf true setzen, falls
         * zutreffend (sprich dieser AREintrag angemeldet oder eben nicht)
         */
        if (pBesitzAREintrag.angemeldet) {
            angemeldeteVorhanden = true;
        } else {
            nichtAngemeldeteVorhanden = true;
        }
        if (pBesitzAREintrag.angemeldeteNichtAusgeblendeteVorhanden) {
            angemeldeteNichtAusgeblendeteVorhanden = true;
        }
    }

    private int inMeldungenEigeneGastkartenEnthalteneInstiIdent=0;
    /**Füllt die Liste pBesitzJeKennung.zugeordneteMeldungenEigeneGastkartenListe: alle Gastkarten,
     * die pPersonNatJurIdent "gehören" (können auch Gruppengastkarten sein).
     * Nur verwenden, wenn Kennung keine Aktienregisternummer ist!*/
    public void leseMeldungenEigeneGastkartenZuPersonNatJur(EclBesitzJeKennung pBesitzJeKennung) {
        int lPersonNatJur = pBesitzJeKennung.eclPersonenNatJur.ident;
        pBesitzJeKennung.personNatJurIdent = lPersonNatJur;
        EclMeldung lMeldung = new EclMeldung();
        lMeldung.personenNatJurIdent = lPersonNatJur;
        lDbBundle.dbMeldungen.leseZuPersonenNatJurIdent(lMeldung, 0);/*Liefert nur aktive Meldungen zurück*/

        List<EclZugeordneteMeldungNeu> lZugeordneteMeldungenEigeneGastkartenListe = new LinkedList<EclZugeordneteMeldungNeu>();

        int anzGastkarten = lDbBundle.dbMeldungen.meldungenArray.length;
        for (int i = 0; i < anzGastkarten; i++) {
            lMeldung = lDbBundle.dbMeldungen.meldungenArray[i];

            if (lMeldung.kommunikationssprache>0) {
                inMeldungenEigeneGastkartenEnthalteneInstiIdent=lMeldung.kommunikationssprache;
            }

            EclZugeordneteMeldungNeu lZugeordneteMeldung = new EclZugeordneteMeldungNeu();
            lZugeordneteMeldung.artBeziehung = 2;
            lZugeordneteMeldung.eclMeldung = lMeldung;
            //lZugeordneteMeldung.aktienregisterIdent nicht füllbar
            lZugeordneteMeldung.meldungsIdent = lDbBundle.dbMeldungen.meldungenArray[i].meldungsIdent;
            if (lDbBundle.dbMeldungen.meldungenArray[i].klasse == 0) {/*Gast*/
                lZugeordneteMeldung.klasse = 0;
            } else {
                CaBug.drucke("001");
            }
            //lZugeordneteMeldung.fixAnmeldung nicht relevant
            lZugeordneteMeldung.personNatJurIdent = lPersonNatJur;

            fuelleAktionaersdatenInMeldung(lZugeordneteMeldung, false, lMeldung);
            setzePraesenzFelder(lZugeordneteMeldung, lMeldung);

            if (meldungIstAusgeblendet(lMeldung.meldungsIdent)) {
                lZugeordneteMeldung.ausgeblendet = true;
                ausgeblendeteVorhanden = true;
            }

            lZugeordneteMeldungenEigeneGastkartenListe.add(lZugeordneteMeldung);
        }
        if (anzGastkarten > 0) {
            pBesitzJeKennung.gastkartenVorhanden = true;
        }
        pBesitzJeKennung.zugeordneteMeldungenEigeneGastkartenListe = lZugeordneteMeldungenEigeneGastkartenListe;
    }

    /**Vollmachten einlesen - füllt die Liste zugeordneteMeldungenBevollmaechtigt: alle Meldungen,
     * für die pPersonNatJurIdent eine gültige Vollmacht besitzt
     * 
     *Vorgehen:
     * 1.) Alle Willenserklärungen mit Vollmachten auf pPersonNatJur einlesen
     * 2.) Daraus die zu bearbeitenden Meldungen ermitteln (jede Meldung nur einmal!)
     * 3.) je nach Parameterstellung: für diese meldungen dann vollmachtsart und eclVorlaeufigeVollmacht ergänzen.
     * */
    public void leseMeldungenBevollmaechtigtZuPersonNatJur(EclBesitzJeKennung pBesitzJeKennung) {
        int lPersonNatJurIdent = pBesitzJeKennung.personNatJurIdent;
        CaBug.druckeLog("Start", logDrucken, 10);
        if (lPersonNatJurIdent == 0) {/*INFO - wurde ursprünglich nicht abgeprüft, deshalb ellenlange Antwortzeiten!*/
            CaBug.druckeLog("Return weil lPersonNatJurIdent==0", logDrucken, 10);
            return;
        }

        /*Willenserklärugnen einlesen, die pPersonNatJurIdent als Bevollmächtigten eingetragen haben*/
        CaBug.druckeLog("Lese Willenserklärungen zu Bevollmächtigtem lPersonNatJurIdent=" + lPersonNatJurIdent, logDrucken, 10);
        lDbBundle.dbWillenserklaerung.leseZuBevollmaechtigten(lPersonNatJurIdent, false);
        int anzWillenserklaerungen = lDbBundle.dbWillenserklaerung.willenserklaerungArray.length;
        CaBug.druckeLog("anzWillenserklaerungen=" + anzWillenserklaerungen, logDrucken, 10);
        if (anzWillenserklaerungen == 0) {/*Keinerlei Bevollmächtigung gefunden*/
            return;
        }

        /*Alle gefundenen Willenserklärungen durcharbeiten und die gefundenen Meldungen (aber jede Meldung nur einmal) in
         * lMeldungenList aufnehmen.
         */
        List<Integer> lMeldungenList = new LinkedList<>();

        for (int i = 0; i < anzWillenserklaerungen; i++) {
            int hMeldung = lDbBundle.dbWillenserklaerung.willenserklaerungArray[i].meldungsIdent;

            /*Überprüfen, ob Meldung schon in Array*/
            int gef = -1;
            for (int i1 = 0; i1 < lMeldungenList.size(); i1++) {
                if (lMeldungenList.get(i1) == hMeldung) {
                    gef = i1;
                }
            }
            if (gef == -1) {/*Dann lMeldung noch nicht in bisheriger Liste drin*/
                Integer oMeldung = hMeldung;
                lMeldungenList.add(oMeldung);
            }
        }

        /*Nun alle Meldungen in Liste eintragen*/
        List<EclZugeordneteMeldungNeu> lZugeordneteMeldungenBevollmaechtigtListe = new LinkedList<EclZugeordneteMeldungNeu>();
        for (int i = 0; i < lMeldungenList.size(); i++) {
            int meldungsIdent = lMeldungenList.get(i);
            lDbBundle.dbMeldungen.leseZuMeldungsIdent(meldungsIdent);
            EclMeldung lMeldung = lDbBundle.dbMeldungen.meldungenArray[0];

            EclZugeordneteMeldungNeu lZugeordneteMeldung = new EclZugeordneteMeldungNeu();
            lZugeordneteMeldung.artBeziehung = 3;
            lZugeordneteMeldung.eclMeldung = lMeldung;
            lZugeordneteMeldung.aktienregisterIdent = lMeldung.aktienregisterIdent;
            lZugeordneteMeldung.aktionaersnummerFuerAnzeige = BlNummernformBasis.aufbereitenInternFuerExtern(lMeldung.aktionaersnummer, lDbBundle);
            //lZugeordneteMeldung.aktienregisterIdent
            lZugeordneteMeldung.meldungsIdent = lMeldung.meldungsIdent;
            lZugeordneteMeldung.klasse = 1;
            //lZugeordneteMeldung.fixAnmeldung
            if (lDbBundle.param.paramPortal.varianteDialogablauf == 1) {
                /*Vorläufige Vollmachten möglich - dazuladen*/
                fuelleVorlaeufigeVollmachtZuErhaltenenVollmachten(lZugeordneteMeldung, lMeldung, pBesitzJeKennung);
            }
            if (lZugeordneteMeldung.vollmachtsart == 0) {
                lZugeordneteMeldung.personNatJurIdent = lPersonNatJurIdent;
            } else {//gesetzliche Vollmacht - als handelnde Person den gesetzlich vertretenen "selbst"
                lZugeordneteMeldung.personNatJurIdent = -1;
            }

            fuelleAktionaersdatenInMeldung(lZugeordneteMeldung, true, lMeldung);

            angemeldeteVorhanden = true;

            if (meldungIstAusgeblendet(lMeldung.meldungsIdent)) {
                lZugeordneteMeldung.ausgeblendet = true;
                ausgeblendeteVorhanden = true;
            } else {
                angemeldeteNichtAusgeblendeteVorhanden = true;
                aktienInsgesamtAngemeldet += lMeldung.stueckAktien;
           }

            setzePraesenzFelder(lZugeordneteMeldung, lMeldung);
            lZugeordneteMeldungenBevollmaechtigtListe.add(lZugeordneteMeldung);
            setzeGattungsergebnisFuerMeldung(lMeldung);
        }
        if (lZugeordneteMeldungenBevollmaechtigtListe.size() > 0) {
            pBesitzJeKennung.erhalteneVollmachtenVorhanden = true;
        }
        pBesitzJeKennung.zugeordneteMeldungenBevollmaechtigtListe = lZugeordneteMeldungenBevollmaechtigtListe;

    }

    public void leseInstiBestaende(EclBesitzJeKennung pBesitzJeKennung, boolean pMitWillenserklaerungen) {
        BlInsti blInsti = new BlInsti(true, lDbBundle);
        blInsti.fuelleInstiBestandsZuordnung(pBesitzJeKennung.instiIdent);

        List<EclBesitzAREintrag> lInstiAREintraegeListe = new LinkedList<EclBesitzAREintrag>();
        for (int i = 0; i < blInsti.rcRegAktienregister.length; i++) {
            EclBesitzAREintrag lBesitzAREintrag = new EclBesitzAREintrag();
            lBesitzAREintrag.aktienregisterEintrag = blInsti.rcRegAktienregister[i];
            leseMeldungenZuAktienregister(lBesitzAREintrag, 4);
            fuelleAktionaersdatenInAREintrag(lBesitzAREintrag);
            lInstiAREintraegeListe.add(lBesitzAREintrag);

            if (pMitWillenserklaerungen) {
                ergaenzeAREintragUmWillenserklaerungen(lBesitzAREintrag, pBesitzJeKennung.personNatJurIdent);
            }
        }
        if (lInstiAREintraegeListe.size() > 0) {
            pBesitzJeKennung.instiAREintraegeVorhanden = true;
        }
        pBesitzJeKennung.instiAREintraegeListe = lInstiAREintraegeListe;

        List<EclZugeordneteMeldungNeu> lZugeordneteMeldungenInstiListe = new LinkedList<EclZugeordneteMeldungNeu>();
        for (int i = 0; i < blInsti.rcMeldMeldung.length; i++) {
            if (blInsti.rcMeldMeldung[i].klasse == 1) {/*Aktionär*/

                EclZugeordneteMeldungNeu lZugeordneteMeldung = new EclZugeordneteMeldungNeu();
                lZugeordneteMeldung.artBeziehung = 5;
                lZugeordneteMeldung.eclMeldung = blInsti.rcMeldMeldung[i];
                lZugeordneteMeldung.aktienregisterIdent = blInsti.rcMeldMeldung[i].aktienregisterIdent;
                lZugeordneteMeldung.aktionaersnummerFuerAnzeige = BlNummernformBasis.aufbereitenInternFuerExtern(blInsti.rcMeldMeldung[i].aktionaersnummer, lDbBundle);
                lZugeordneteMeldung.meldungsIdent = blInsti.rcMeldMeldung[i].meldungsIdent;
                lZugeordneteMeldung.klasse = 1;
                //lZugeordneteMeldung.fixAnmeldung
                //lZugeordneteMeldung.personNatJurIdent nicht zuordenbar

                fuelleAktionaersdatenInMeldung(lZugeordneteMeldung, true, blInsti.rcMeldMeldung[i]);
                setzePraesenzFelder(lZugeordneteMeldung, blInsti.rcMeldMeldung[i]);

                if (meldungIstAusgeblendet(lZugeordneteMeldung.meldungsIdent)) {
                    lZugeordneteMeldung.ausgeblendet = true;
                    ausgeblendeteVorhanden = true;
                } else {
                    angemeldeteNichtAusgeblendeteVorhanden = true;
                    aktienInsgesamtAngemeldet += blInsti.rcMeldMeldung[i].stueckAktien;
                }

                angemeldeteVorhanden = true;

                lZugeordneteMeldungenInstiListe.add(lZugeordneteMeldung);
                setzeGattungsergebnisFuerMeldung(blInsti.rcMeldMeldung[i]);
                if (pMitWillenserklaerungen) {
                    ergaenzeMeldungUmWillenserklaerungen(lZugeordneteMeldung, pBesitzJeKennung.personNatJurIdent);
                }
            }
        }
        if (lZugeordneteMeldungenInstiListe.size() > 0) {
            pBesitzJeKennung.instiMeldungenVorhanden = true;
        }
        pBesitzJeKennung.zugeordneteMeldungenInstiListe = lZugeordneteMeldungenInstiListe;

    }

    /**Liest anhand der erteilten Vollmachten die noch dazu, die mit den gesetzlichen Vollmachten
     * "mitvererbt" wurden
     */
    public void leseVonGesetzlichenGeerbtZuBevollmaechtigten(EclBesitzJeKennung pBesitzJeKennung) {
        List<EclZugeordneteMeldungNeu> lZugeordneteMeldungenListe = new LinkedList<EclZugeordneteMeldungNeu>();
        for (int i = 0; i < pBesitzJeKennung.zugeordneteMeldungenBevollmaechtigtListe.size(); i++) {
            if (pBesitzJeKennung.zugeordneteMeldungenBevollmaechtigtListe.get(i).vollmachtsart == 1) {
                /*Diese Vollmacht ist eine gesetzliche Vollmacht => ggf. weitere geerbte einlesen*/
                int aktienregisterIdentDesGesetzlichVertretenen = pBesitzJeKennung.zugeordneteMeldungenBevollmaechtigtListe.get(i).aktienregisterIdent;
                CaBug.druckeLog("aktienregisterIdentDesGesetzlichVertretenen=" + aktienregisterIdentDesGesetzlichVertretenen, logDrucken, 10);

                /*NatJurPerson des gesetzlich vertretenen einlesen, denn in dessen Namen werden weitere Willenserklärungen abgegeben*/
                lDbBundle.dbAktienregister.leseZuAktienregisterIdent(aktienregisterIdentDesGesetzlichVertretenen);
                int personNatJurDesGesetzlichVertretenen = lDbBundle.dbAktienregister.ergebnisPosition(0).personNatJur;

                /*Alle Vollmachten, die der gesetzlich Vertretene erhalten hat, einlesen und ebenfalls hier (=beim gesetzlichen Vertreter) eintragen*/
                lDbBundle.dbVorlaeufigeVollmacht.readGegebenAnAktionaer(aktienregisterIdentDesGesetzlichVertretenen);

                for (int i1 = 0; i1 < lDbBundle.dbVorlaeufigeVollmacht.anzErgebnis(); i1++) {
                    EclVorlaeufigeVollmacht lVorlaeufigeVollmacht = lDbBundle.dbVorlaeufigeVollmacht.ergebnisPosition(i1);
                    if (lVorlaeufigeVollmacht.storniert == 0) {
                        int aktienregisterIdentGeberDerGeerbtenVollmacht = lVorlaeufigeVollmacht.erteiltVonIdent;
                        lDbBundle.dbMeldungen.leseZuAktienregisterIdent(aktienregisterIdentGeberDerGeerbtenVollmacht, true); //ohne Stornos

                        EclMeldung lMeldung = lDbBundle.dbMeldungen.meldungenArray[0];

                        EclZugeordneteMeldungNeu lZugeordneteMeldung = new EclZugeordneteMeldungNeu();
                        lZugeordneteMeldung.artBeziehung = 6;
                        lZugeordneteMeldung.eclMeldung = lMeldung;
                        lZugeordneteMeldung.aktienregisterIdent = aktienregisterIdentGeberDerGeerbtenVollmacht;
                        lZugeordneteMeldung.aktionaersnummerFuerAnzeige = BlNummernformBasis.aufbereitenInternFuerExtern(lMeldung.aktionaersnummer, lDbBundle);
                        lZugeordneteMeldung.meldungsIdent = lMeldung.meldungsIdent;
                        lZugeordneteMeldung.klasse = lMeldung.klasse;

                        lZugeordneteMeldung.vollmachtsart = 2;

                        //lZugeordneteMeldung.fixAnmeldung=true; hier nicht relevant
                        /*ausführende Person ist hier der gesetzlich vertretene - das muß immer ein Aktionär sein*/
                        lZugeordneteMeldung.personNatJurIdent = personNatJurDesGesetzlichVertretenen;

                        fuelleAktionaersdatenInMeldung(lZugeordneteMeldung, true, lMeldung);
                        setzePraesenzFelder(lZugeordneteMeldung, lMeldung);

                        if (meldungIstAusgeblendet(lMeldung.meldungsIdent)) {
                            lZugeordneteMeldung.ausgeblendet = true;
                            ausgeblendeteVorhanden = true;
                        }

                        lZugeordneteMeldungenListe.add(lZugeordneteMeldung);
                        setzeGattungsergebnisFuerMeldung(lMeldung);
                    }

                }
            }
        }
        pBesitzJeKennung.zugeordneteMeldungenBevollmaechtigtMitGesetzlichListe = lZugeordneteMeldungenListe;
        if (lZugeordneteMeldungenListe.size() > 0) {
            pBesitzJeKennung.erhalteneVollmachtenMitGesetzlichVorhanden = true;
        } else {
            pBesitzJeKennung.erhalteneVollmachtenMitGesetzlichVorhanden = false;
        }

    }

    private void setzeGattungsergebnisFuerMeldung(EclMeldung pMeldung) {
        if (pMeldung.meldungAktiv == 1 && pMeldung.klasse == 1) {
            gattungen[pMeldung.liefereGattung() - 1] = 1;
        }
    }

    /*************************Ergänze Meldungen um Willenserklärungen***********************************/
    /**Parameter pPersonNatJurIdent:
     * = -2 => es wird nicht selektiert, es werden wirklich alle Willenserklärungen (so noch gültig) angegeben.
     * = >0 => für die "eigenen Meldungen" werden alle Willenserklärungen angezeigt, die vom Aktionär ausgehen (geber == -1);
     * 			für die Bevollmächtigten werden die Willenserklärungen angezeigt, die auf den Bevollmächtigten verweisen (nicht änderbar!)
     * 			und die dieser gegeben hat.
     */
    private void ergaenzeAREintragUmWillenserklaerungen(EclBesitzAREintrag pBesitzAREintrag, int pPersonNatJurIdent) {
        /**Muß hier initialisiert werden, da diese Summe über alle Meldungen des AREintrags gebildet wird.
         * Wird allerdings nur benötigt, für eigene Aktien (keine Insti-Zuordnungen) - schadet aber auch
         * in den anderen Fällen nicht*/
        lGastKartenGemeldetEigeneAktien = 0;

        /*Anmeldungen aus Aktienregister füllen*/
        for (int i = 0; i < pBesitzAREintrag.zugeordneteMeldungenListe.size(); i++) {
            ergaenzeMeldungUmWillenserklaerungen(pBesitzAREintrag.zugeordneteMeldungenListe.get(i), pPersonNatJurIdent);
        }

        /*Hier zweiEKMoeglich setzen. Nur möglich, wenn nur 1 Meldung vorhanden, und keine
         * weiteren Willenserklärungen vorhanden sind.
         * Basierend auf Alle - da dies immer nur für vollständig zugeordnete Aktienregistereinträge gilt (Insti,
         * oder Aktionärskennung).
         */
        if (pBesitzAREintrag.zugeordneteMeldungenListe.size() == 1) {
            if (pBesitzAREintrag.zugeordneteMeldungenListe.get(0).anzAlleZutrittsIdentSelbst == 0 && pBesitzAREintrag.zugeordneteMeldungenListe.get(0).anzAlleZutrittsIdentVollmacht == 0
                    && pBesitzAREintrag.zugeordneteMeldungenListe.get(0).anzAlleVollmachtenDritte == 0 && pBesitzAREintrag.zugeordneteMeldungenListe.get(0).anzAlleKIAVSRV == 0
                    && pBesitzAREintrag.zugeordneteMeldungenListe.get(0).fixAnmeldung == false) {
                pBesitzAREintrag.zugeordneteMeldungenListe.get(0).zweiEKMoeglich = true;
            }

        }

        /*Anmeldungen angeforderte Gastkarten füllen*/
        /*Anmeldungen aus Aktienregister füllen*/
        for (int i = 0; i < pBesitzAREintrag.zugeordneteMeldungenGaesteListe.size(); i++) {
            ergaenzeMeldungUmWillenserklaerungen(pBesitzAREintrag.zugeordneteMeldungenGaesteListe.get(i), pPersonNatJurIdent);
        }
    }

    /**
     * Es werden je nach nurNichtStornierteWillenserklaerungen nur noch gültige Willenserklärungen, oder auch
     * bereits stornierte Willenserklärungen geliefert.
     * 
     * Parameter pPersonNatJurIdent:
     * = -2 => es wird nicht selektiert, es werden wirklich alle Willenserklärungen (so noch gültig) angegeben.
     * = >0 => für die "eigenen Meldungen" werden alle Willenserklärungen angezeigt, die vom Aktionär ausgehen (geber == -1);
     * 			für die Bevollmächtigten werden die Willenserklärungen angezeigt, die auf den Bevollmächtigten verweisen (nicht änderbar!)
     * 			und die dieser gegeben hat.
     * 
     * Wenn piSelektionGeberOderAlle==2, dann werden (unbahängig von pPersonNatJurIdent) die Willenserklärungen aller
     * Geber geliefert.
     */
    private void ergaenzeMeldungUmWillenserklaerungen(EclZugeordneteMeldungNeu pZugeordneteMeldung, int pPersonNatJurIdent) {

        /*Es werden hier wirklich alle Willenserklärungen (einzeln) eingelesen*/
        leseWillenserklaerungenZuMeldung(pZugeordneteMeldung.eclMeldung);

        /**Initialisierung von Summen sowie von zugeordneteWillenserklaerungenList erfolgte bereits bei
         * new EclZugeordneteMeldung()
         */

        /**Alle Willenserklärungen durcharbeiten und ggf. übertragen*/
        for (int i1 = 0; i1 < zugeordneteWillenserklaerungenList.size(); i1++) {
            if (
            /*Grundsätzlich alle Willenserklärungen übertragen?*/
            piSelektionGeberOderAlle == 2 ||
            /*Alle übertragen gemäß Aufrufparamter dieser Funktion*/
                    pPersonNatJurIdent == -2 || (/*eigenerAREintragListe, instiAREintraegeListe, zugeordneteMeldungenInstiListe*/
                    (pZugeordneteMeldung.artBeziehung == 1 || pZugeordneteMeldung.artBeziehung == 4 || pZugeordneteMeldung.artBeziehung == 5) &&
                    /*Willenserklärungen, die vom Aktionär ausgehen*/
                            zugeordneteWillenserklaerungenList.get(i1).willenserklaerungGeberIdent == -1)
                    ||
                    /*zugeordneteMeldungenEigeneGastkartenListe
                     *Hier darf / braucht nicht selektiert zu werden, wer sie gegeben hat - kann nur
                     *Anmeldung / Eintrittskartenausstellung sein. Dürfen nie verändert werden, nur Detailsicht*/
                    pZugeordneteMeldung.artBeziehung == 2 || (/*zugeordneteMeldungenBevollmaechtigtListe - normale Vollmacht*/
                    pZugeordneteMeldung.artBeziehung == 3 && pZugeordneteMeldung.vollmachtsart == 0 && (
                    /*Willenserklärungen, die selbst gegeben wurde*/
                    zugeordneteWillenserklaerungenList.get(i1).willenserklaerungGeberIdent == pPersonNatJurIdent ||
                    /*Vollmacht auf diese Person*/
                            zugeordneteWillenserklaerungenList.get(i1).bevollmaechtigterDritterIdent == pPersonNatJurIdent))
                    || (/*zugeordneteMeldungenBevollmaechtigtListe - gesetzliche Vollmacht*/
                    pZugeordneteMeldung.artBeziehung == 3 && pZugeordneteMeldung.vollmachtsart == 1 && (
                    /*Willenserklärungen, die vom Aktionär ausgehen*/
                    zugeordneteWillenserklaerungenList.get(i1).willenserklaerungGeberIdent == -1)) || (/*zugeordneteMeldungenBevollmaechtigtMitGesetzlichListe - mit gesetzlicher Vollmacht geerbte
                                                                                                       	hier muß als pPersonNatJurIdent der gesetzlichvertretene mit angegeben werden*/
                    pZugeordneteMeldung.artBeziehung == 6 && (
                    /*Willenserklärungen, die vom Aktionär ausgehen*/
                    zugeordneteWillenserklaerungenList.get(i1).willenserklaerungGeberIdent == pPersonNatJurIdent))) {

                /**Bei zugeordneteMeldungenBevollmaechtigtListe: die Willenserklärung wurde "empfangen", d.h.
                 * sie zählt nicht zu der Anzahl der jeweiligen Willenserklärungen dazu*/
                boolean istEmpfangeneWillenserklaerung = false;
                if (pZugeordneteMeldung.artBeziehung == 3 && zugeordneteWillenserklaerungenList.get(i1).bevollmaechtigterDritterIdent == pPersonNatJurIdent) {
                    istEmpfangeneWillenserklaerung = true;
                }
                CaBug.druckeLog("i1=" + i1, logDrucken, 10);
                CaBug.druckeLog("zugeordneteWillenserklaerungenList.get(i1).willenserklaerung=" + zugeordneteWillenserklaerungenList.get(i1).willenserklaerung, logDrucken, 10);
                CaBug.druckeLog("zugeordneteWillenserklaerungenList.get(i1).storniert=" + zugeordneteWillenserklaerungenList.get(i1).storniert, logDrucken, 10);
                CaBug.druckeLog("nurNichtStornierteWillenserklaerungen=" + nurNichtStornierteWillenserklaerungen, logDrucken, 10);
                if (zugeordneteWillenserklaerungenList.get(i1).storniert == false || nurNichtStornierteWillenserklaerungen == false) {
                    /*Hier: Willenserklärung muß übertragen werden*/

                    /*Ggf. Änderungs-Zulässigkeit etc. korrigieren!*/
                    if (pZugeordneteMeldung.artBeziehung == 2) {/*zugeordneteMeldungenEigeneGastkartenListe*/
                        zugeordneteWillenserklaerungenList.get(i1).aendernIstZulaessig = false;
                        zugeordneteWillenserklaerungenList.get(i1).stornierenIstZulaessig = false;
                        zugeordneteWillenserklaerungenList.get(i1).detailAnzeigeIstZulaessig = true;
                    }
                    if (pZugeordneteMeldung.artBeziehung == 3) {/*zugeordneteMeldungenBevollmaechtigtListe*/
                        if (zugeordneteWillenserklaerungenList.get(i1).willenserklaerungGeberIdent == pPersonNatJurIdent) {
                            /*Willenserklärungen, die selbst gegeben wurde*/
                            /*?????????????Unklar, warum für selbst erteilte Willenserklärungen auf false gesetzt?*/
                            zugeordneteWillenserklaerungenList.get(i1).detailAnzeigeIstZulaessig = false;
                        }
                        if (zugeordneteWillenserklaerungenList.get(i1).bevollmaechtigterDritterIdent == pPersonNatJurIdent) {
                            /*Vollmacht auf diese Person*/
                            zugeordneteWillenserklaerungenList.get(i1).aendernIstZulaessig = false;
                            zugeordneteWillenserklaerungenList.get(i1).stornierenIstZulaessig = false;

                            /* Zur Frage, warum die von anderem erteilte Willenserklärung angezeigt werden darf:
                             * Das kann eigentlich nur eine vollmachtAnDritte sein, oder eine neueZutrittsIdentZuMeldung_VollmachtAnDritte
                             * oder neueZutrittsIdentZuMeldung. In diesem Fall soll der Bevollmächtigte sein Ticket
                             * selbst ausdrucken / anzeigen können.
                             * ??????????Überflüssig, da bei betreffenden Willenserklärungen explizit auf true gesetzt.*/
                            zugeordneteWillenserklaerungenList.get(i1).detailAnzeigeIstZulaessig = true; /*Siehe unten!!!*/
                        }

                    }

                    CaBug.druckeLog("übertragen i1=" + i1, logDrucken, 10);
                    /*Willenserklärung übertragen*/
                    pZugeordneteMeldung.zugeordneteWillenserklaerungenList.add(zugeordneteWillenserklaerungenList.get(i1));

                    /*Ggf. Summen und zulässige Funktionalität korrigieren*/
                    switch (zugeordneteWillenserklaerungenList.get(i1).willenserklaerung) {

                    case KonstWillenserklaerung.neueZutrittsIdentZuMeldung: {
                        if (istEmpfangeneWillenserklaerung == false) {
                            pZugeordneteMeldung.anzPersZutrittsIdentSelbst++;
                        }
                        zugeordneteWillenserklaerungenList.get(i1).detailAnzeigeIstZulaessig = true;
                        break;
                    }

                    case KonstWillenserklaerung.neueZutrittsIdentZuMeldung_VollmachtAnDritte: {
                        if (istEmpfangeneWillenserklaerung == false) {
                            pZugeordneteMeldung.anzPersZutrittsIdentVollmacht++;
                        }
                        zugeordneteWillenserklaerungenList.get(i1).detailAnzeigeIstZulaessig = true;
                        break;
                    }

                    case KonstWillenserklaerung.vollmachtAnDritte: {
                        if (istEmpfangeneWillenserklaerung == false) {
                            pZugeordneteMeldung.anzPersVollmachtenDritte++;
                        }
                        zugeordneteWillenserklaerungenList.get(i1).detailAnzeigeIstZulaessig = true;
                        /*War ursprünglich bei zugeordneteMeldungenBevollmaechtigtListe mal false. Unklar warum.
                         *Entweder es gibt was anzuzeigen, was von Interesse auch für den Bevollmächtigten ist,
                         * oder die Willenserklärung darf gar nicht übernommen werden in diesem Fall ...
                         */
                        break;
                    }

                    case KonstWillenserklaerung.vollmachtUndWeisungAnSRV: {
                        pZugeordneteMeldung.anzPersKIAVSRV++;
                        pZugeordneteMeldung.anzPersSRV++;
                        break;
                    }

                    case KonstWillenserklaerung.vollmachtUndWeisungAnKIAV: {
                        pZugeordneteMeldung.anzPersKIAVSRV++;
                        pZugeordneteMeldung.anzPersKIAV++;
                        break;
                    }

                    case KonstWillenserklaerung.briefwahl: {
                        pZugeordneteMeldung.anzPersKIAVSRV++;
                        pZugeordneteMeldung.anzPersBriefwahl++;
                        break;
                    }

                    }
                }

            } else { /*Willenswerklärung gehört zu anderer Person und wurde deshalb nicht übertragen
                     Nun ggf. von andern erteilte Briefwahl etc. berücksichtigen*/
                if (zugeordneteWillenserklaerungenList.get(i1).storniert == false || nurNichtStornierteWillenserklaerungen == false) {
                    if (pZugeordneteMeldung.ausgeblendet == false) {
                        CaBug.druckeLog("pZugeordneteMeldung.ausgeblendet=" + pZugeordneteMeldung.ausgeblendet, logDrucken, 10);
                        /*Ggf. Summen und zulässige Funktionalität korrigieren*/
                        switch (zugeordneteWillenserklaerungenList.get(i1).willenserklaerung) {

                        case KonstWillenserklaerung.vollmachtUndWeisungAnSRV:
                            pZugeordneteMeldung.bereitsErteiltWeisungBriefwahlAllgemeinDurchAndere = true;
                            bereitsErteiltWeisungBriefwahlAllgemeinDurchAndereVorhanden = true;
                            if (zugeordneteWillenserklaerungenList.get(i1).willenserklaerungGeberIdent <= 0) {
                                pZugeordneteMeldung.bereitsErteiltSRVDurchAktionär = true;
                            } else {
                                pZugeordneteMeldung.bereitsErteiltSRVDurchVertreter = true;
                            }
                            break;

                        case KonstWillenserklaerung.vollmachtUndWeisungAnKIAV:
                            pZugeordneteMeldung.bereitsErteiltWeisungBriefwahlAllgemeinDurchAndere = true;
                            bereitsErteiltWeisungBriefwahlAllgemeinDurchAndereVorhanden = true;
                            if (zugeordneteWillenserklaerungenList.get(i1).willenserklaerungGeberIdent <= 0) {
                                pZugeordneteMeldung.bereitsErteiltKIAVDurchAktionär = true;
                            } else {
                                pZugeordneteMeldung.bereitsErteiltKIAVDurchVertreter = true;
                            }
                            break;

                        case KonstWillenserklaerung.briefwahl:
                            pZugeordneteMeldung.bereitsErteiltWeisungBriefwahlAllgemeinDurchAndere = true;
                            bereitsErteiltWeisungBriefwahlAllgemeinDurchAndereVorhanden = true;
                            if (zugeordneteWillenserklaerungenList.get(i1).willenserklaerungGeberIdent <= 0) {
                                pZugeordneteMeldung.bereitsErteiltBriefwahlDurchAktionär = true;
                            } else {
                                pZugeordneteMeldung.bereitsErteiltBriefwahlDurchVertreter = true;
                            }
                            break;
                        }

                    }
                }
            }
        }

        CaBug.druckeLog("pZugeordneteMeldung.zugeordneteWillenserklaerungenList.size()=" + pZugeordneteMeldung.zugeordneteWillenserklaerungenList.size(), logDrucken, 10);
        if (pZugeordneteMeldung.zugeordneteWillenserklaerungenList.size() > 0) {
            pZugeordneteMeldung.willenserklaerungenVorhanden = true;
        } else {
            pZugeordneteMeldung.willenserklaerungenVorhanden = false;
        }

        pZugeordneteMeldung.identHoechsteWillenserklaerung = lIdentHoechsteWillenserklaerung;
        /*Wurde ursprünglich mal durch neue Datenabfrage ermittelt, m.E. aber nicht mehr erforderlich,
         * da beim Durcharbeiten der Willenserklärungen dies ja bereits mit ermittelt wurde - folgende
         * Codesequenz deshalb nicht mehr erforderlich
         */
        //		EclMeldung lMeldung=new EclMeldung();lMeldung.meldungsIdent=zugeordneteMeldungenEigeneAktienArray[i].meldungsIdent;
        //		int anz=lDbBundle.dbWillenserklaerung.ermittleHoechsteWillenserklaerungIdentZuMeldung(lMeldung);
        //		zugeordneteMeldungenEigeneAktienArray[i].identHoechsteWillenserklaerung=anz;

        /*Nun die Gesamtsummen übertragen, die in leseWillenserklaerungenZuMeldung ermittelt wurden*/
        if (pZugeordneteMeldung.artBeziehung == 2) {
            /*TODO #9*/
            /*
            zugeordneteMeldungenEigeneGastkartenArray[i].anzZutrittsIdentSelbst=lAnzZutrittsIdentSelbst;
            zugeordneteMeldungenEigeneGastkartenArray[i].anzZutrittsIdentVollmacht=lAnzZutrittsIdentVollmacht;
            zugeordneteMeldungenEigeneGastkartenArray[i].anzVollmachtenDritte=lAnzVollmachtenDritte;
            zugeordneteMeldungenEigeneGastkartenArray[i].anzKIAVSRV=lAnzKIAVSRV;
            Hinweis: die App erwartet derzeit, dass anzZutrittsIdentSelbst auch bei Gastkarten gefüllt sind.
            Deshalb funktioniert es nicht, wenn dieser (jetzt) auskommentierte Bereich aktiviert wird,
            da die Subroutine anzZutrittsIdentSelbst bei Gastkarten nicht füllt! Muß noch mal generell untersucht werden.
            				*/
        } else {
            pZugeordneteMeldung.anzAlleZutrittsIdentSelbst = lAnzZutrittsIdentSelbst;
            pZugeordneteMeldung.anzAlleZutrittsIdentVollmacht = lAnzZutrittsIdentVollmacht;
            pZugeordneteMeldung.anzAlleVollmachtenDritte = lAnzVollmachtenDritte;
            pZugeordneteMeldung.anzAlleKIAVSRV = lAnzKIAVSRV;
            pZugeordneteMeldung.anzAlleSRV = lAnzSRV;
            pZugeordneteMeldung.anzAlleKIAV = lAnzKIAV;
            pZugeordneteMeldung.anzAlleBriefwahl = lAnzBriefwahl;

            /* TODO Unklar, ob das noch gebraucht wird?????????????????????? und wenn wo werden diese Variablen dann gespeichert?*/
            if (lAnzBriefwahl > 0) {
                this.briefwahlVorhanden = true;
            }
            if (lAnzSRV > 0) {
                this.srvVorhanden = true;
            }
        }
    }

    /**+++++++++++++++++++Fülle Vollmachtenliste++++++++++++++++++++++++++++++++++++++++++++++++*/
    /**Voraussetzung:
     * piRueckgabeKurzOderLang=2;
     * piAnsichtVerarbeitungOderAnalyse=2;
     * 
     * ergaenzeZugeordneteMeldungenUmWillenserklaerungen ist bereits aufgerufen
     * 
     * Füllt die Liste der Bevollmächtigten Personen rcListeVollmachten 
     * aus allen Willenserklärungen (so Vollmacht noch gültig) des
     * eigenen Besitzes
     * 
     * Wird verwendet für Ermittlung der möglichen Personen bei Fragen ("Sind Sie ..:"), falls nicht
     * mit eigenen Vollmachts-Kennungen gearbeitet wird.
     */
    public int ermittleVollmachtenAusMeldungenUndWillenserklaerungen() {
        rcListeVollmachten = new LinkedList<EclPersonenNatJur>();
        if (besitzJeKennungListe == null) {
            return 1;
        }
        for (int i = 0; i < besitzJeKennungListe.size(); i++) {
            EclBesitzJeKennung lBesitzJeKennung = besitzJeKennungListe.get(i);
            for (int i1 = 0; i1 < lBesitzJeKennung.eigenerAREintragListe.size(); i1++) {
                for (int i2 = 0; i2 < lBesitzJeKennung.eigenerAREintragListe.get(i1).zugeordneteMeldungenListe.size(); i2++) {
                    EclZugeordneteMeldungNeu lZugeordneteMeldung = lBesitzJeKennung.eigenerAREintragListe.get(i1).zugeordneteMeldungenListe.get(i2);
                    List<EclWillenserklaerungStatusNeu> zugeordneteWillenserklaerungenList = lZugeordneteMeldung.zugeordneteWillenserklaerungenList;
                    if (zugeordneteWillenserklaerungenList != null) {
                        for (int i3 = 0; i3 < zugeordneteWillenserklaerungenList.size(); i3++) {
                            EclWillenserklaerungStatusNeu lWillenserklaerungStatus = zugeordneteWillenserklaerungenList.get(i3);
                            if (lWillenserklaerungStatus.bevollmaechtigterDritterIdent != 0 && lWillenserklaerungStatus.storniert == false) {
                                rcListeVollmachten.add(lWillenserklaerungStatus.eclPersonenNatJurVertreter);
                            }
                        }
                    }

                }
            }
        }
        return 1;
    }

    /*+++++++++++++++++Detailfunktion Willenserklärungen anfügen+++++++++++++++++++++*/

    private List<EclWillenserklaerungStatusNeu> zugeordneteWillenserklaerungenList = new LinkedList<EclWillenserklaerungStatusNeu>();
    private int lAnzZutrittsIdentSelbst = 0;
    private int lAnzZutrittsIdentVollmacht = 0;
    private int lAnzVollmachtenDritte = 0;
    /**Summe aus KIAV, SRV, Briefwahl*/
    private int lAnzKIAVSRV = 0;

    private int lAnzSRV = 0;
    private int lAnzKIAV = 0;
    private int lAnzBriefwahl = 0;
    private int lIdentHoechsteWillenserklaerung = 0;

    /**Achtung, darf nicht in leseWillenserklaerungenZuMeldung initialisiert werden, da diese
     * Summe über alle Meldungen zu einem Aktienregistereintrag gebildet wird (Verwendung jedoch nur
     * bei eigenen Aktien, nicht bei Instis oder Vollmachten oder so)
     */
    private int lGastKartenGemeldetEigeneAktien = 0;

    private EclWillenserklaerungStatusNeu prepareWillenserklaerungStatus(EclWillenserklaerung pWillenserklaerung, EclWillenserklaerungZusatz pWillenserklaerungZusatz) {
        EclWillenserklaerungStatusNeu zugeordneteWillenserklaerung = new EclWillenserklaerungStatusNeu();
        zugeordneteWillenserklaerung.willenserklaerungIdent = pWillenserklaerung.willenserklaerungIdent;
        zugeordneteWillenserklaerung.willenserklaerung = pWillenserklaerung.willenserklaerung;
        zugeordneteWillenserklaerung.willenserklaerungGeberIdent = pWillenserklaerung.willenserklaerungGeberIdent;

        if (piRueckgabeKurzOderLang == 2) {
            zugeordneteWillenserklaerung.eclWillenserklaerung = pWillenserklaerung;
            zugeordneteWillenserklaerung.eclWillenserklaerungZusatz = pWillenserklaerungZusatz;
        }
        return zugeordneteWillenserklaerung;
    }

    private void zuvieleWillenserklaerungen() {
        CaBug.drucke("001 - zu viele Willenserklärungen");
    }

    /**Liest alle Willenserklärungen laut Datei ein - dabei werden Stornierte entsprechend gekennzeichnet und 
     * "High-Level-Willenserklärungskombinationen" zusammengefaßt, z.B.:
     * 	> Eintrittskarte + gleichzeitige Vollmacht
     * 
     * Hinweis: 
     * > es werden ALLE Willenserklärungen zu der angegebenen Meldung eingelesen. und in 
     * 		zugeordneteWillenserklaerungenList abgelegt. 
     * > Eingrenzung auf bestimmte Vollmachten 
     * 		(z.B. auf einen Willenserklärungsgeber, oder auf einen bestimmten Bevollmächtigten)
     * 		müssen dann beim Umkopieren in die endgültige Liste erfolgen. 
     * 
     */
    public void leseWillenserklaerungenZuMeldung(EclMeldung lMeldung) {

        EclWillenserklaerung willenserklaerung = null;
        EclWillenserklaerungZusatz willenserklaerungZusatz = null;
        EclWillenserklaerungStatusNeu zugeordneteWillenserklaerung = null;
        EclWillenserklaerungStatusTextElement lWillenserklaerungStatusTextElement = null;

        zugeordneteWillenserklaerungenList = new LinkedList<>();
        lAnzZutrittsIdentSelbst = 0;
        lAnzZutrittsIdentVollmacht = 0;
        lAnzVollmachtenDritte = 0;
        lAnzKIAVSRV = 0;
        lAnzSRV = 0;
        lAnzKIAV = 0;
        lAnzBriefwahl = 0;
        lIdentHoechsteWillenserklaerung = 0;

        /*Willenserklärungen zur pMeldungsIdent einlesen*/
        if (lMeldung.klasse == 1) {
            lDbBundle.dbWillenserklaerung.leseZuMeldung(lMeldung);
        } else {
            lDbBundle.dbWillenserklaerung.leseZuMeldungGast(lMeldung);
        }

        /*Schleife für alle Willenserklärungen*/
        for (int i = 0; i < lDbBundle.dbWillenserklaerung.anzWillenserklaerungGefunden(); i++) {
            willenserklaerung = lDbBundle.dbWillenserklaerung.willenserklaerungGefunden(i);

            /*Ist Weisung über Internet erteilt worden?*/
            if (willenserklaerung.erteiltAufWeg < 21 || willenserklaerung.erteiltAufWeg > 29) {
                this.rcHatNichtNurPortalWillenserklaerungen = true;
            }
            /*Neueste Willenserklärung?*/
            if (this.rcDatumLetzteWillenserklaerung.compareTo(willenserklaerung.veraenderungszeit) < 0) {
                this.rcDatumLetzteWillenserklaerung = willenserklaerung.veraenderungszeit;
            }

            if (piAnsichtVerarbeitungOderAnalyse == 2) { /**Dann willenserklärungZusatz IMMER einlesen
                                                         (bei Verarbeitung  wird diese nur eingelesen, wenn sie gebraucht wird.*/
                lDbBundle.dbWillenserklaerungZusatz.leseZuIdent(willenserklaerung.willenserklaerungIdent);
                willenserklaerungZusatz = lDbBundle.dbWillenserklaerungZusatz.willenserklaerungGefunden(0);

            }
            if (willenserklaerung.willenserklaerungIdent > lIdentHoechsteWillenserklaerung) { /*Höchste vergebene WillenserklärungIdent festhalten*/
                lIdentHoechsteWillenserklaerung = willenserklaerung.willenserklaerungIdent;
            }
            switch (willenserklaerung.willenserklaerung) {

            case KonstWillenserklaerung.neueZutrittsIdentZuMeldung:
            case KonstWillenserklaerung.zuZutrittsIdentNeuesDokument: {
                int i1;
                int veraenderteWK = 0;
                if (piAnsichtVerarbeitungOderAnalyse == 1) { /*Bei Analyse-Ansicht bereits eingelesen!*/
                    lDbBundle.dbWillenserklaerungZusatz.leseZuIdent(willenserklaerung.willenserklaerungIdent);
                    willenserklaerungZusatz = lDbBundle.dbWillenserklaerungZusatz.willenserklaerungGefunden(0);
                }

                if (willenserklaerung.willenserklaerung == KonstWillenserklaerung.neueZutrittsIdentZuMeldung || piAnsichtVerarbeitungOderAnalyse == 2) {
                    /*Neue Willenserklärung erzeugen*/
                    zugeordneteWillenserklaerung = prepareWillenserklaerungStatus(willenserklaerung, willenserklaerungZusatz);
                }
                if (willenserklaerung.willenserklaerung == KonstWillenserklaerung.zuZutrittsIdentNeuesDokument) {
                    for (i1 = 0; i1 < zugeordneteWillenserklaerungenList.size(); i1++) {
                        if (BlZutrittsIdent.compare(zugeordneteWillenserklaerungenList.get(i1).zutrittsIdent, zugeordneteWillenserklaerungenList.get(i1).zutrittsIdentNeben,
                                willenserklaerung.zutrittsIdent, willenserklaerung.zutrittsIdentNeben) == 0
                        //zugeordneteWillenserklaerungenList.get(i1).zutrittsIdent.compareTo(willenserklaerung.zutrittsIdent)==0
                        ) {
                            veraenderteWK = i1;
                        }
                    }
                    if (veraenderteWK != -1) {
                        if (piAnsichtVerarbeitungOderAnalyse == 1) { /*Dann bestehende Willenserkläerung von der ursprünglichen Ausstellung ergänzen / überschreiben*/
                            zugeordneteWillenserklaerung = zugeordneteWillenserklaerungenList.get(veraenderteWK);
                        }
                        zugeordneteWillenserklaerung.textListe = new LinkedList<>();
                        zugeordneteWillenserklaerung.textListeIntern = new LinkedList<>();
                    } else {
                        CaBug.drucke("BlWillenserklaerungStatus.leseWillenserklaerungenZuMeldung - 001");
                    }
                }

                if (piRueckgabeKurzOderLang == 2) { /*In diesem Fall nochmal separat füllen für den Fall, dass piAnsichtVerarbeitungOderAnalyse==1 und piRueckgabeKurzOderLang==2.
                                                    Allerdings beachten: da wird das dann nicht so korrekt, da ursprünglicher Verweis auf Willenserklärung überschrieben wird*/
                    zugeordneteWillenserklaerung.eclWillenserklaerung = willenserklaerung;
                    zugeordneteWillenserklaerung.eclWillenserklaerungZusatz = willenserklaerungZusatz;
                }

                zugeordneteWillenserklaerung.willenserklaerungIdent = willenserklaerung.willenserklaerungIdent;
                zugeordneteWillenserklaerung.willenserklaerung = willenserklaerung.willenserklaerung;
                zugeordneteWillenserklaerung.willenserklaerungGeberIdent = willenserklaerung.willenserklaerungGeberIdent;
                String hString = willenserklaerung.zutrittsIdent;
                if (willenserklaerung.zutrittsIdentNeben.compareTo("00") != 0) {
                    hString = hString + "-" + willenserklaerung.zutrittsIdentNeben;
                }

                lWillenserklaerungStatusTextElement = new EclWillenserklaerungStatusTextElement();
                if (lMeldung.klasse == 1) {
                    lWillenserklaerungStatusTextElement.textNr = KonstWillenserklaerungPortalTexte.neueZutrittsIdentZuMeldung_zuZutrittsIdentNeuesDokument_Aktionaer;
                    zugeordneteWillenserklaerung.textListeIntern.add("Eintrittskarte-Nr. " + hString);
                } else {
                    lWillenserklaerungStatusTextElement.textNr = KonstWillenserklaerungPortalTexte.neueZutrittsIdentZuMeldung_zuZutrittsIdentNeuesDokument_Gast;
                    zugeordneteWillenserklaerung.textListeIntern.add("Gastkarte-Nr. " + hString);
                }
                lWillenserklaerungStatusTextElement.textInhalt = hString;
                lWillenserklaerungStatusTextElement.textInhaltEN = hString;

                if (lMeldung.titel.isEmpty()) {
                    lWillenserklaerungStatusTextElement.textListe.add(lMeldung.name + ", " + lMeldung.vorname);
                    lWillenserklaerungStatusTextElement.textListeEN.add(lMeldung.name + ", " + lMeldung.vorname);
                    zugeordneteWillenserklaerung.textListeIntern.add(lMeldung.name + ", " + lMeldung.vorname);
                }
                else {
                    lWillenserklaerungStatusTextElement.textListe.add(lMeldung.name + ", " + lMeldung.titel+" "+lMeldung.vorname);
                    lWillenserklaerungStatusTextElement.textListeEN.add(lMeldung.name + ", " + lMeldung.titel+" "+lMeldung.vorname);
                    zugeordneteWillenserklaerung.textListeIntern.add(lMeldung.name + ", " + lMeldung.titel+" "+lMeldung.vorname);
                }


                lWillenserklaerungStatusTextElement.textListe.add(lMeldung.ort);
                lWillenserklaerungStatusTextElement.textListeEN.add(lMeldung.ort);
                zugeordneteWillenserklaerung.textListeIntern.add(lMeldung.ort);

                zugeordneteWillenserklaerung.textListe.add(lWillenserklaerungStatusTextElement);

                zugeordneteWillenserklaerung.versandart = willenserklaerungZusatz.versandartEK;
                zugeordneteWillenserklaerung.zutrittsIdent = willenserklaerung.zutrittsIdent;
                zugeordneteWillenserklaerung.zutrittsIdentNeben = willenserklaerung.zutrittsIdentNeben;

                /*Je nach Versandart: Daten in textListe ergänzen*/
                switch (zugeordneteWillenserklaerung.versandart) {
                case 1:
                case 2:
                case 6:/*Postversand*/ {
                    int versandAdresseGefunden = 0;
                    lWillenserklaerungStatusTextElement = new EclWillenserklaerungStatusTextElement();
                    zugeordneteWillenserklaerung.textListeIntern.add("Versandadresse:");

                    if (!willenserklaerungZusatz.versandadresse1.isEmpty()) {
                        versandAdresseGefunden = 1;
                        lWillenserklaerungStatusTextElement.textNr = KonstWillenserklaerungPortalTexte.neueZutrittsIdentZuMeldung_zuZutrittsIdentNeuesDokument_Postversand;
                        lWillenserklaerungStatusTextElement.textListe.add(willenserklaerungZusatz.versandadresse1);
                        lWillenserklaerungStatusTextElement.textListeEN.add(willenserklaerungZusatz.versandadresse1);
                        zugeordneteWillenserklaerung.textListeIntern.add(willenserklaerungZusatz.versandadresse1);
                    }
                    if (!willenserklaerungZusatz.versandadresse2.isEmpty()) {
                        versandAdresseGefunden = 1;
                        lWillenserklaerungStatusTextElement.textNr = KonstWillenserklaerungPortalTexte.neueZutrittsIdentZuMeldung_zuZutrittsIdentNeuesDokument_Postversand;
                        lWillenserklaerungStatusTextElement.textListe.add(willenserklaerungZusatz.versandadresse2);
                        lWillenserklaerungStatusTextElement.textListeEN.add(willenserklaerungZusatz.versandadresse2);
                        zugeordneteWillenserklaerung.textListeIntern.add(willenserklaerungZusatz.versandadresse2);
                    }
                    if (!willenserklaerungZusatz.versandadresse3.isEmpty()) {
                        versandAdresseGefunden = 1;
                        lWillenserklaerungStatusTextElement.textNr = KonstWillenserklaerungPortalTexte.neueZutrittsIdentZuMeldung_zuZutrittsIdentNeuesDokument_Postversand;
                        lWillenserklaerungStatusTextElement.textListe.add(willenserklaerungZusatz.versandadresse3);
                        lWillenserklaerungStatusTextElement.textListeEN.add(willenserklaerungZusatz.versandadresse3);
                        zugeordneteWillenserklaerung.textListeIntern.add(willenserklaerungZusatz.versandadresse3);
                    }
                    if (!willenserklaerungZusatz.versandadresse4.isEmpty()) {
                        versandAdresseGefunden = 1;
                        lWillenserklaerungStatusTextElement.textNr = KonstWillenserklaerungPortalTexte.neueZutrittsIdentZuMeldung_zuZutrittsIdentNeuesDokument_Postversand;
                        lWillenserklaerungStatusTextElement.textListe.add(willenserklaerungZusatz.versandadresse4);
                        lWillenserklaerungStatusTextElement.textListeEN.add(willenserklaerungZusatz.versandadresse4);
                        zugeordneteWillenserklaerung.textListeIntern.add(willenserklaerungZusatz.versandadresse4);
                    }
                    if (!willenserklaerungZusatz.versandadresse5.isEmpty()) {
                        versandAdresseGefunden = 1;
                        lWillenserklaerungStatusTextElement.textNr = KonstWillenserklaerungPortalTexte.neueZutrittsIdentZuMeldung_zuZutrittsIdentNeuesDokument_Postversand;
                        lWillenserklaerungStatusTextElement.textListe.add(willenserklaerungZusatz.versandadresse5);
                        lWillenserklaerungStatusTextElement.textListeEN.add(willenserklaerungZusatz.versandadresse5);
                        zugeordneteWillenserklaerung.textListeIntern.add(willenserklaerungZusatz.versandadresse5);
                    }
                    if (versandAdresseGefunden == 1) {
                        zugeordneteWillenserklaerung.textListe.add(lWillenserklaerungStatusTextElement);
                    }
                    break;
                }
                case 3: {
                    lWillenserklaerungStatusTextElement = new EclWillenserklaerungStatusTextElement();
                    lWillenserklaerungStatusTextElement.textNr = KonstWillenserklaerungPortalTexte.neueZutrittsIdentZuMeldung_zuZutrittsIdentNeuesDokument_OnlineTicket;
                    zugeordneteWillenserklaerung.textListeIntern.add("Online-Ausdruck");
                    zugeordneteWillenserklaerung.textListe.add(lWillenserklaerungStatusTextElement);
                    break;
                }
                case 4: {
                    lWillenserklaerungStatusTextElement = new EclWillenserklaerungStatusTextElement();
                    lWillenserklaerungStatusTextElement.textNr = KonstWillenserklaerungPortalTexte.neueZutrittsIdentZuMeldung_zuZutrittsIdentNeuesDokument_Mail;
                    zugeordneteWillenserklaerung.textListeIntern.add("Versand per E-Mail:");
                    lWillenserklaerungStatusTextElement.textListe.add(willenserklaerungZusatz.emailAdresseEK);
                    lWillenserklaerungStatusTextElement.textListeEN.add(willenserklaerungZusatz.emailAdresseEK);
                    zugeordneteWillenserklaerung.textListeIntern.add(willenserklaerungZusatz.emailAdresseEK);
                    zugeordneteWillenserklaerung.textListe.add(lWillenserklaerungStatusTextElement);
                    break;
                }
                case 5: {
                    lWillenserklaerungStatusTextElement = new EclWillenserklaerungStatusTextElement();
                    lWillenserklaerungStatusTextElement.textNr = KonstWillenserklaerungPortalTexte.neueZutrittsIdentZuMeldung_zuZutrittsIdentNeuesDokument_App;
                    zugeordneteWillenserklaerung.textListeIntern.add("In App gespeichert");
                    zugeordneteWillenserklaerung.textListe.add(lWillenserklaerungStatusTextElement);
                    break;
                }
                }

                if (willenserklaerung.willenserklaerung == KonstWillenserklaerung.neueZutrittsIdentZuMeldung || piAnsichtVerarbeitungOderAnalyse == 2) {
                    /*Bei ZuZutrittsIdentNeuesDokument wurde ja die alte überschrieben, deshalb nichts einfügen*/
                    zugeordneteWillenserklaerungenList.add(zugeordneteWillenserklaerung);
                }
                if (willenserklaerung.willenserklaerung == KonstWillenserklaerung.zuZutrittsIdentNeuesDokument && piAnsichtVerarbeitungOderAnalyse == 2) {
                    /*Verknüpfung eintragen*/
                    zugeordneteWillenserklaerung.verweisAufVorgaenger = veraenderteWK;
                    zugeordneteWillenserklaerungenList.get(veraenderteWK).verweisAufNachfolger = zugeordneteWillenserklaerungenList.size() - 1;
                }
                if (willenserklaerung.willenserklaerung == KonstWillenserklaerung.zuZutrittsIdentNeuesDokument) {
                    zugeordneteWillenserklaerungenList.get(veraenderteWK).veraendert = true;
                }

                zugeordneteWillenserklaerung.stornierenIstZulaessig = true;
                if (lMeldung.klasse == 1) {
                    lAnzZutrittsIdentSelbst++;
                } else {
                    lGastKartenGemeldetEigeneAktien++;
                }
                break;
            }
            case KonstWillenserklaerung.vollmachtAnDritte: { /*Prüfen, ob Folge und zu ZutrittsIdent gehörend*/
                int veraenderteWK = -1;
                if (willenserklaerung.folgeBuchungFuerIdent != 0) {/*Folgebuchung => gehört diese zu Eintrittskartenbestellung?*/
                    int ii = 0;
                    for (ii = 0; ii < zugeordneteWillenserklaerungenList.size(); ii++) {
                        if (ii > 1000) {
                            zuvieleWillenserklaerungen();
                            return;
                        }
                        if (zugeordneteWillenserklaerungenList.get(ii).willenserklaerungIdent == willenserklaerung.folgeBuchungFuerIdent) {
                            veraenderteWK = ii;

                            if (piAnsichtVerarbeitungOderAnalyse == 1) {
                                zugeordneteWillenserklaerungenList.get(ii).willenserklaerung = KonstWillenserklaerung.neueZutrittsIdentZuMeldung_VollmachtAnDritte;
                                zugeordneteWillenserklaerungenList.get(ii).willenserklaerungIdent2 = willenserklaerung.willenserklaerungIdent;
                                zugeordneteWillenserklaerungenList.get(ii).bevollmaechtigterDritterIdent = willenserklaerung.bevollmaechtigterDritterIdent;

                                int vollmachtgeberIdent=willenserklaerung.willenserklaerungGeberIdent;
                                if (vollmachtgeberIdent>0) {
                                    lWillenserklaerungStatusTextElement = new EclWillenserklaerungStatusTextElement();
                                    lWillenserklaerungStatusTextElement.textNr = KonstWillenserklaerungPortalTexte.vollmachtAnDritte_zuEK_Vollmachtgeber;
                                    zugeordneteWillenserklaerung.textListeIntern.add("Vollmachtgeber:");

                                    lDbBundle.dbPersonenNatJur.read(vollmachtgeberIdent);

                                    String hString = lDbBundle.dbPersonenNatJur.personenNatJurArray[0].name + ", " + lDbBundle.dbPersonenNatJur.personenNatJurArray[0].vorname;
                                    lWillenserklaerungStatusTextElement.textListe.add(hString);
                                    lWillenserklaerungStatusTextElement.textListeEN.add(hString);
                                    zugeordneteWillenserklaerung.textListeIntern.add(hString);
                                    
                                    lWillenserklaerungStatusTextElement.textListe.add(lDbBundle.dbPersonenNatJur.personenNatJurArray[0].ort);
                                    lWillenserklaerungStatusTextElement.textListeEN.add(lDbBundle.dbPersonenNatJur.personenNatJurArray[0].ort);
                                    zugeordneteWillenserklaerung.textListeIntern.add(lDbBundle.dbPersonenNatJur.personenNatJurArray[0].ort);
                                    
                                    zugeordneteWillenserklaerungenList.get(ii).textListe.add(lWillenserklaerungStatusTextElement);
                                }
                                
                                
                                lWillenserklaerungStatusTextElement = new EclWillenserklaerungStatusTextElement();
                                lWillenserklaerungStatusTextElement.textNr = KonstWillenserklaerungPortalTexte.vollmachtAnDritte_zuEK;
                                zugeordneteWillenserklaerung.textListeIntern.add("mit Vollmacht an:");

                                lDbBundle.dbPersonenNatJur.read(willenserklaerung.bevollmaechtigterDritterIdent);

                                String hString = lDbBundle.dbPersonenNatJur.personenNatJurArray[0].name + ", " + lDbBundle.dbPersonenNatJur.personenNatJurArray[0].vorname;
                                lWillenserklaerungStatusTextElement.textListe.add(hString);
                                lWillenserklaerungStatusTextElement.textListeEN.add(hString);
                                zugeordneteWillenserklaerung.textListeIntern.add(hString);
                                
                                lWillenserklaerungStatusTextElement.textListe.add(lDbBundle.dbPersonenNatJur.personenNatJurArray[0].ort);
                                lWillenserklaerungStatusTextElement.textListeEN.add(lDbBundle.dbPersonenNatJur.personenNatJurArray[0].ort);
                                zugeordneteWillenserklaerung.textListeIntern.add(lDbBundle.dbPersonenNatJur.personenNatJurArray[0].ort);
                                
                                zugeordneteWillenserklaerungenList.get(ii).textListe.add(lWillenserklaerungStatusTextElement);

                                lAnzZutrittsIdentSelbst--;
                                lAnzZutrittsIdentVollmacht++;
                            } else {
                                zugeordneteWillenserklaerungenList.get(ii).verweisAufNachfolger = zugeordneteWillenserklaerungenList.size();
                            }
                        }
                    }
                }
                if ((willenserklaerung.folgeBuchungFuerIdent == 0 || veraenderteWK == -1) || piAnsichtVerarbeitungOderAnalyse == 2) {
                    /*separate Vollmacht an Dritte, oder ausführliche Darstellung*/
                    zugeordneteWillenserklaerung = prepareWillenserklaerungStatus(willenserklaerung, willenserklaerungZusatz);

                    zugeordneteWillenserklaerung.bevollmaechtigterDritterIdent = willenserklaerung.bevollmaechtigterDritterIdent;

                    lWillenserklaerungStatusTextElement = new EclWillenserklaerungStatusTextElement();
                    lWillenserklaerungStatusTextElement.textNr = KonstWillenserklaerungPortalTexte.vollmachtAnDritte_Alleine;
                    zugeordneteWillenserklaerung.textListeIntern.add("Vollmacht an:");
                    
                    lDbBundle.dbPersonenNatJur.read(willenserklaerung.bevollmaechtigterDritterIdent);
                    if (piRueckgabeKurzOderLang == 2) {
                        zugeordneteWillenserklaerung.eclPersonenNatJurVertreter = lDbBundle.dbPersonenNatJur.personenNatJurArray[0];
                    }

                    String hString=lDbBundle.dbPersonenNatJur.personenNatJurArray[0].name + ", " + lDbBundle.dbPersonenNatJur.personenNatJurArray[0].vorname;
                    lWillenserklaerungStatusTextElement.textListe.add(hString);
                    lWillenserklaerungStatusTextElement.textListeEN.add(hString);
                    zugeordneteWillenserklaerung.textListeIntern.add(hString);
                         
                    hString=lDbBundle.dbPersonenNatJur.personenNatJurArray[0].ort;
                    lWillenserklaerungStatusTextElement.textListe.add(hString);
                    lWillenserklaerungStatusTextElement.textListeEN.add(hString);
                    zugeordneteWillenserklaerung.textListeIntern.add(hString);

                    zugeordneteWillenserklaerung.textListe.add(lWillenserklaerungStatusTextElement);

                    if (veraenderteWK != -1) {
                        zugeordneteWillenserklaerung.verweisAufVorgaenger = veraenderteWK;
                    }
                    zugeordneteWillenserklaerungenList.add(zugeordneteWillenserklaerung);
                    zugeordneteWillenserklaerung.stornierenIstZulaessig = true;
                    lAnzVollmachtenDritte++;
                }
                break;
            }

            case KonstWillenserklaerung.vollmachtUndWeisungAnSRV: {
                zugeordneteWillenserklaerung = prepareWillenserklaerungStatus(willenserklaerung, willenserklaerungZusatz);

                if (lDbBundle.param.paramPortal.sammelkartenFuerAenderungSperren==1) {
                    /*Nun zugeordnete Sammelkarte einlesen*/
                    lDbBundle.dbMeldungZuSammelkarte.leseZuWillenserklaerung(willenserklaerung.willenserklaerungIdent);
                    EclMeldungZuSammelkarte meldungZuSammelkarte = lDbBundle.dbMeldungZuSammelkarte.meldungZuSammelkarteGefunden(0);
                    int sammelIdent = meldungZuSammelkarte.sammelIdent;
                    EclMeldung sammelMeldung = new EclMeldung();
                    sammelMeldung.meldungsIdent = sammelIdent;
                    lDbBundle.dbMeldungen.leseZuMeldungsIdent(sammelMeldung);
                    if (lDbBundle.dbMeldungen.meldungenArray[0].zusatzfeld5.equals("1")){
                        zugeordneteWillenserklaerung.gesperrt=true; 
                    }
                }
                lWillenserklaerungStatusTextElement = new EclWillenserklaerungStatusTextElement();
                lWillenserklaerungStatusTextElement.textNr = KonstWillenserklaerungPortalTexte.vollmachtUndWeisungAnSRV;
                zugeordneteWillenserklaerung.textListeIntern.add("Vollmacht und Weisung an Stimmrechtsvertreter");
                        
                zugeordneteWillenserklaerung.textListe.add(lWillenserklaerungStatusTextElement);

                if (piRueckgabeKurzOderLang == 2) { /*Ggf Weisung einlesen*/
                    lDbBundle.dbWeisungMeldung.leseZuWillenserklaerungIdent(willenserklaerung.willenserklaerungIdent, false);
                    if (lDbBundle.dbWeisungMeldung.anzWeisungMeldungGefunden() == 1) {
                        zugeordneteWillenserklaerung.eclWeisungMeldung = lDbBundle.dbWeisungMeldung.weisungMeldungGefunden(0);
                    }
                }

                zugeordneteWillenserklaerungenList.add(zugeordneteWillenserklaerung);
                zugeordneteWillenserklaerung.stornierenIstZulaessig = true;
                zugeordneteWillenserklaerung.aendernIstZulaessig = true;
                lAnzKIAVSRV++;
                lAnzSRV++;
                break;
            }

            case KonstWillenserklaerung.vollmachtUndWeisungAnKIAV:
            case KonstWillenserklaerung.dauervollmachtAnKIAV:
            case KonstWillenserklaerung.organisatorischMitWeisungInSammelkarte: {
                zugeordneteWillenserklaerung = prepareWillenserklaerungStatus(willenserklaerung, willenserklaerungZusatz);

                /*Nun zugeordnete Sammelkarte einlesen*/
                lDbBundle.dbMeldungZuSammelkarte.leseZuWillenserklaerung(willenserklaerung.willenserklaerungIdent);
                EclMeldungZuSammelkarte meldungZuSammelkarte = lDbBundle.dbMeldungZuSammelkarte.meldungZuSammelkarteGefunden(0);
                int sammelIdent = meldungZuSammelkarte.sammelIdent;
                EclMeldung sammelMeldung = new EclMeldung();
                sammelMeldung.meldungsIdent = sammelIdent;
                lDbBundle.dbMeldungen.leseZuMeldungsIdent(sammelMeldung);
                if (lDbBundle.param.paramPortal.sammelkartenFuerAenderungSperren==1) {
                    if (lDbBundle.dbMeldungen.meldungenArray[0].zusatzfeld5.equals("1")){
                        zugeordneteWillenserklaerung.gesperrt=true; 
                    }
                }

                /*Weisung einlesen, um z überprüfen ob gebunden/frei/Empfehlung*/
                lDbBundle.dbWeisungMeldung.leseZuWeisungIdent(meldungZuSammelkarte.weisungIdent, false);
                EclWeisungMeldung weisungMeldung = lDbBundle.dbWeisungMeldung.weisungMeldungGefunden(0);

                if (piRueckgabeKurzOderLang == 2) { /*Ggf Weisung eintragen*/
                    if (lDbBundle.dbWeisungMeldung.anzWeisungMeldungGefunden() == 1) {
                        zugeordneteWillenserklaerung.eclWeisungMeldung = lDbBundle.dbWeisungMeldung.weisungMeldungGefunden(0);
                    }
                }

                if (weisungMeldung.hatDedizierteWeisungen()) {
                    /*Vollmacht und Weisung*/ zugeordneteWillenserklaerung.weisungenSind = 1;
                } else {
                    if (weisungMeldung.hatWeisungenGemEigenerEmpfehlungFlexNeu() || weisungMeldung.hatWeisungenGemEigenerEmpfehlungFlexBestehend()
                            || weisungMeldung.hatWeisungenGemEigenerEmpfehlungFlexNeu()) {
                        /*Vollmacht und Weisung gemäß Vorschlag*/
                        zugeordneteWillenserklaerung.weisungenSind = 2;
                    } else {
                        /*Frei*/
                        zugeordneteWillenserklaerung.weisungenSind = 3;
                    }
                }
                lWillenserklaerungStatusTextElement = new EclWillenserklaerungStatusTextElement();
                switch (willenserklaerung.willenserklaerung) {
                case KonstWillenserklaerung.vollmachtUndWeisungAnKIAV: {
                    switch (zugeordneteWillenserklaerung.weisungenSind) {
                    case 0: {
                        lWillenserklaerungStatusTextElement.textNr = KonstWillenserklaerungPortalTexte.vollmachtUndWeisungAnKIAV_nurVollmacht;
                        zugeordneteWillenserklaerung.textListeIntern.add("Vollmacht an Kreditinstitut/Aktionärsvereinigung:");
                                
                        zugeordneteWillenserklaerung.stornierenIstZulaessig = true;
                        zugeordneteWillenserklaerung.aendernIstZulaessig = false; // neu eingefügt
                        break;
                    }
                    case 1: {
                        lWillenserklaerungStatusTextElement.textNr = KonstWillenserklaerungPortalTexte.vollmachtUndWeisungAnKIAV_VollmachtUndWeisung;
                        zugeordneteWillenserklaerung.textListeIntern.add("Vollmacht und Weisung an Kreditinstitut/Aktionärsvereinigung:");
                                
                        zugeordneteWillenserklaerung.stornierenIstZulaessig = true;
                        zugeordneteWillenserklaerung.aendernIstZulaessig = true;
                        break;
                    }
                    case 2: {
                        lWillenserklaerungStatusTextElement.textNr = KonstWillenserklaerungPortalTexte.vollmachtUndWeisungAnKIAV_VollmachtUndWeisungGemaessVorschlag;
                        zugeordneteWillenserklaerung.textListeIntern.add("Vollmacht und Weisung gemäß Vorschlag an Kreditinstitut/Aktionärsvereiniung:");
                                
                        zugeordneteWillenserklaerung.stornierenIstZulaessig = true;
                        zugeordneteWillenserklaerung.aendernIstZulaessig = false;
                        break;
                    }
                    case 3: {
                        lWillenserklaerungStatusTextElement.textNr = KonstWillenserklaerungPortalTexte.vollmachtUndWeisungAnKIAV_VollmachtOhneWeisung;
                        zugeordneteWillenserklaerung.textListeIntern.add("Vollmacht (ohne Weisung) an Kreditinstitut/Aktionärsvereiniung:");
                                
                        zugeordneteWillenserklaerung.stornierenIstZulaessig = true;
                        zugeordneteWillenserklaerung.aendernIstZulaessig = false;
                        break;
                    }
                    }
                    break;
                }
                case KonstWillenserklaerung.dauervollmachtAnKIAV: {
                    switch (zugeordneteWillenserklaerung.weisungenSind) {
                    case 0: {
                        lWillenserklaerungStatusTextElement.textNr = KonstWillenserklaerungPortalTexte.dauervollmachtAnKIAV_nurVollmacht;
                        zugeordneteWillenserklaerung.textListeIntern.add("Dauervollmacht an Kreditinstitut:");
                                
                        zugeordneteWillenserklaerung.stornierenIstZulaessig = true;
                        zugeordneteWillenserklaerung.aendernIstZulaessig = false; // neu eingefügt
                        break;
                    }
                    case 1: {
                        lWillenserklaerungStatusTextElement.textNr = KonstWillenserklaerungPortalTexte.dauervollmachtAnKIAV_VollmachtUndWeisung;
                        zugeordneteWillenserklaerung.textListeIntern.add("Dauervollmacht und Weisung an Kreditinstitut:");
                                
                        zugeordneteWillenserklaerung.stornierenIstZulaessig = true;
                        zugeordneteWillenserklaerung.aendernIstZulaessig = true;
                        break;
                    }
                    case 2: {
                        lWillenserklaerungStatusTextElement.textNr = KonstWillenserklaerungPortalTexte.dauervollmachtAnKIAV_VollmachtUndWeisungGemaessVorschlag;
                        zugeordneteWillenserklaerung.textListeIntern.add("Dauervollmacht und Weisung gemäß Vorschlag an Kreditinstitut:");
                                
                        zugeordneteWillenserklaerung.stornierenIstZulaessig = true;
                        zugeordneteWillenserklaerung.aendernIstZulaessig = false;
                        break;
                    }
                    case 3: {
                        lWillenserklaerungStatusTextElement.textNr = KonstWillenserklaerungPortalTexte.dauervollmachtAnKIAV_VollmachtOhneWeisung;
                        zugeordneteWillenserklaerung.textListeIntern.add("Dauervollmacht (ohne Weisung) an Kreditinstitut:");
                                
                        zugeordneteWillenserklaerung.stornierenIstZulaessig = true;
                        zugeordneteWillenserklaerung.aendernIstZulaessig = false;
                        break;
                    }
                    }
                    break;
                }
                case KonstWillenserklaerung.organisatorischMitWeisungInSammelkarte: {
                    switch (zugeordneteWillenserklaerung.weisungenSind) {
                    case 0: {
                        lWillenserklaerungStatusTextElement.textNr = KonstWillenserklaerungPortalTexte.organisatorisch_nurVollmacht;
                        zugeordneteWillenserklaerung.textListeIntern.add("Organisatorisch in Sammelkarte:");
                                
                        zugeordneteWillenserklaerung.stornierenIstZulaessig = true;
                        zugeordneteWillenserklaerung.aendernIstZulaessig = false; // neu eingefügt
                        break;
                    }
                    case 1: {
                        lWillenserklaerungStatusTextElement.textNr = KonstWillenserklaerungPortalTexte.organisatorisch_VollmachtUndWeisung;
                        zugeordneteWillenserklaerung.textListeIntern.add("Organisatorisch mit Weisung in Sammelkarte:");
                                
                        zugeordneteWillenserklaerung.stornierenIstZulaessig = true;
                        zugeordneteWillenserklaerung.aendernIstZulaessig = true;
                        break;
                    }
                    case 2: {
                        lWillenserklaerungStatusTextElement.textNr = KonstWillenserklaerungPortalTexte.organisatorisch_VollmachtUndWeisungGemaessVorschlag;
                        zugeordneteWillenserklaerung.textListeIntern.add("Organisatorisch mit Weisung gemäß Vorschlag in Sammelkarte:");
                                
                       zugeordneteWillenserklaerung.stornierenIstZulaessig = true;
                        zugeordneteWillenserklaerung.aendernIstZulaessig = false;
                        break;
                    }
                    case 3: {
                        lWillenserklaerungStatusTextElement.textNr = KonstWillenserklaerungPortalTexte.organisatorisch_VollmachtOhneWeisung;
                        zugeordneteWillenserklaerung.textListeIntern.add("Organisatorisch (ohne Weisung) in Sammelkarte:");
                                
                       zugeordneteWillenserklaerung.stornierenIstZulaessig = true;
                        zugeordneteWillenserklaerung.aendernIstZulaessig = false;
                        break;
                    }
                    }
                    break;
                }
                default:
                    break;

                }

                String hKurzName=lDbBundle.dbMeldungen.meldungenArray[0].kurzName;
                lWillenserklaerungStatusTextElement.textListe.add(hKurzName);
                lWillenserklaerungStatusTextElement.textListeEN.add(hKurzName);
                zugeordneteWillenserklaerung.textListeIntern.add(hKurzName);
                        

                zugeordneteWillenserklaerung.textListe.add(lWillenserklaerungStatusTextElement);

                zugeordneteWillenserklaerungenList.add(zugeordneteWillenserklaerung);
                lAnzKIAVSRV++;
                lAnzKIAV++;
                break;
            }

            case KonstWillenserklaerung.briefwahl: {
                zugeordneteWillenserklaerung = prepareWillenserklaerungStatus(willenserklaerung, willenserklaerungZusatz);

                if (lDbBundle.param.paramPortal.sammelkartenFuerAenderungSperren==1) {
                    /*Nun zugeordnete Sammelkarte einlesen*/
                    lDbBundle.dbMeldungZuSammelkarte.leseZuWillenserklaerung(willenserklaerung.willenserklaerungIdent);
                    EclMeldungZuSammelkarte meldungZuSammelkarte = lDbBundle.dbMeldungZuSammelkarte.meldungZuSammelkarteGefunden(0);
                    int sammelIdent = meldungZuSammelkarte.sammelIdent;
                    EclMeldung sammelMeldung = new EclMeldung();
                    sammelMeldung.meldungsIdent = sammelIdent;
                    lDbBundle.dbMeldungen.leseZuMeldungsIdent(sammelMeldung);
                    if (lDbBundle.dbMeldungen.meldungenArray[0].zusatzfeld5.equals("1")){
                        zugeordneteWillenserklaerung.gesperrt=true; 
                    }
                }

                lWillenserklaerungStatusTextElement = new EclWillenserklaerungStatusTextElement();
                lWillenserklaerungStatusTextElement.textNr = KonstWillenserklaerungPortalTexte.briefwahl;
                zugeordneteWillenserklaerung.textListe.add(lWillenserklaerungStatusTextElement);
                zugeordneteWillenserklaerung.textListeIntern.add("Stimmabgabe per Briefwahl");
                        

                if (piRueckgabeKurzOderLang == 2) { /*Ggf Weisung einlesen*/
                    lDbBundle.dbWeisungMeldung.leseZuWillenserklaerungIdent(willenserklaerung.willenserklaerungIdent, false);
                    if (lDbBundle.dbWeisungMeldung.anzWeisungMeldungGefunden() == 1) {
                        zugeordneteWillenserklaerung.eclWeisungMeldung = lDbBundle.dbWeisungMeldung.weisungMeldungGefunden(0);
                    }
                }

                zugeordneteWillenserklaerungenList.add(zugeordneteWillenserklaerung);
                zugeordneteWillenserklaerung.stornierenIstZulaessig = true;
                zugeordneteWillenserklaerung.aendernIstZulaessig = true;
                lAnzKIAVSRV++;
                lAnzBriefwahl++;
                break;
            }

            /* Veränderungen */
            /* Verarbeitung je nach piAnsichtVerarbeitungOderAnalyse - siehe dort!
             * Für Verarbeitungs-Ansicht gilt:
             * 		Die Ident der "Änderungs-Willenserklärung" muß als gültige letzte WillensIdent in die Liste eingetragen werden,
             * 		da diese für das Speichern der Änderung gebraucht wird*/
            case KonstWillenserklaerung.aendernWeisungAnSRV:
            case KonstWillenserklaerung.aendernWeisungAnKIAV:
            case KonstWillenserklaerung.aendernBriefwahl:
            case KonstWillenserklaerung.aendernWeisungOrganisatorischInSammelkarte: {
                int veraenderteWK = -1;
                int ii = 0;
                for (ii = 0; ii < zugeordneteWillenserklaerungenList.size(); ii++) {
                    if (ii > 2000) {
                        zuvieleWillenserklaerungen();
                        return;
                    }
                    if (zugeordneteWillenserklaerungenList.get(ii).willenserklaerungIdent == willenserklaerung.verweisAufWillenserklaerung) {
                        veraenderteWK = ii;
                        zugeordneteWillenserklaerungenList.get(ii).veraendert = true;

                        if (piAnsichtVerarbeitungOderAnalyse == 1) { /*Verarbeitungs-Ansicht*/
                            /*Zur Info: zugeordneteWillenserklaerungenList.get(ii).willenserklaerung bleibt unverändert - d.h. Aendern* taucht in der Liste nicht auf.
                             * Aber: die Willenserklärung der geänderten Ident wird eingetragen - für Folgeänderungen!*/
                            zugeordneteWillenserklaerungenList.get(ii).willenserklaerungIdent = willenserklaerung.willenserklaerungIdent;
                        } else {/*Detail-Analyse*/

                            zugeordneteWillenserklaerung = prepareWillenserklaerungStatus(willenserklaerung, willenserklaerungZusatz);

                            lWillenserklaerungStatusTextElement = new EclWillenserklaerungStatusTextElement();
                            lWillenserklaerungStatusTextElement.textNr = KonstWillenserklaerungPortalTexte.aendernWeisung;
                            zugeordneteWillenserklaerung.textListe.add(lWillenserklaerungStatusTextElement);
                            zugeordneteWillenserklaerung.textListeIntern.add("Änderung Weisung/Stimmabgabe");
                                    

                            zugeordneteWillenserklaerung.verweisAufVorgaenger = veraenderteWK;

                            if (piRueckgabeKurzOderLang == 2) { /*Ggf Weisung einlesen*/
                                lDbBundle.dbWeisungMeldung.leseZuWillenserklaerungIdent(willenserklaerung.willenserklaerungIdent, false);
                                if (lDbBundle.dbWeisungMeldung.anzWeisungMeldungGefunden() == 1) {
                                    zugeordneteWillenserklaerung.eclWeisungMeldung = lDbBundle.dbWeisungMeldung.weisungMeldungGefunden(0);
                                }
                            }

                            zugeordneteWillenserklaerungenList.add(zugeordneteWillenserklaerung);
                            zugeordneteWillenserklaerungenList.get(veraenderteWK).verweisAufNachfolger = zugeordneteWillenserklaerungenList.size() - 1;

                        }
                    }
                }

                break;
            }

            /*Storno Vollmacht/Weisung*/
            case KonstWillenserklaerung.widerrufVollmachtUndWeisungAnSRV:
            case KonstWillenserklaerung.widerrufVollmachtUndWeisungAnKIAV:
            case KonstWillenserklaerung.widerrufBriefwahl:
            case KonstWillenserklaerung.widerrufDauervollmachtAnKIAV:
            case KonstWillenserklaerung.widerrufOrganisatorischMitWeisungInSammelkarte: {
                int veraenderteWK = -1;
                int ii = 0;
                for (ii = 0; ii < zugeordneteWillenserklaerungenList.size(); ii++) {
                    if (ii > 2000) {
                        zuvieleWillenserklaerungen();
                        return;
                    }
                    if (zugeordneteWillenserklaerungenList.get(ii).willenserklaerungIdent == willenserklaerung.verweisAufWillenserklaerung) {
                        veraenderteWK = ii;
                        //						System.out.println("BlWillenserklaerungStatus gef="+gef);
                        //						System.out.println("Storniert=true");
                        zugeordneteWillenserklaerungenList.get(ii).storniert = true;

                    }
                }
                if (piAnsichtVerarbeitungOderAnalyse == 1) { /*Verarbeitungs-Ansicht*/
                } else {/*Detail-Analyse*/

                    zugeordneteWillenserklaerung = prepareWillenserklaerungStatus(willenserklaerung, willenserklaerungZusatz);

                    lWillenserklaerungStatusTextElement = new EclWillenserklaerungStatusTextElement();
                    lWillenserklaerungStatusTextElement.textNr = KonstWillenserklaerungPortalTexte.widerrufWeisung;
                    zugeordneteWillenserklaerung.textListe.add(lWillenserklaerungStatusTextElement);
                    zugeordneteWillenserklaerung.textListeIntern.add("Stornierung Weisung/Stimmabgabe");
                            

                    zugeordneteWillenserklaerung.verweisAufVorgaenger = veraenderteWK;

                    zugeordneteWillenserklaerungenList.add(zugeordneteWillenserklaerung);
                    zugeordneteWillenserklaerungenList.get(veraenderteWK).verweisAufNachfolger = zugeordneteWillenserklaerungenList.size() - 1;
                }
                lAnzKIAVSRV--;
                if (willenserklaerung.willenserklaerung == KonstWillenserklaerung.widerrufBriefwahl) {
                    lAnzBriefwahl--;
                }
                if (willenserklaerung.willenserklaerung == KonstWillenserklaerung.widerrufVollmachtUndWeisungAnSRV) {
                    lAnzSRV--;
                }
                if (willenserklaerung.willenserklaerung == KonstWillenserklaerung.widerrufDauervollmachtAnKIAV
                        || willenserklaerung.willenserklaerung == KonstWillenserklaerung.widerrufVollmachtUndWeisungAnKIAV
                        || willenserklaerung.willenserklaerung == KonstWillenserklaerung.widerrufOrganisatorischMitWeisungInSammelkarte) {
                    lAnzKIAV--;
                }
                break;

            }

            /*Stornierungen*/
            case KonstWillenserklaerung.sperrenZutrittsIdent: {
                int veraenderteWK = -1;
                int ii = 0;
                for (ii = 0; ii < zugeordneteWillenserklaerungenList.size(); ii++) {
                    if (ii > 1000) {
                        zuvieleWillenserklaerungen();
                        return;
                    }
                    if (BlZutrittsIdent.compare(zugeordneteWillenserklaerungenList.get(ii).zutrittsIdent, zugeordneteWillenserklaerungenList.get(ii).zutrittsIdentNeben,
                            willenserklaerung.zutrittsIdent, willenserklaerung.zutrittsIdentNeben) == 0
                    //zugeordneteWillenserklaerungenList.get(ii).zutrittsIdent.compareTo(willenserklaerung.zutrittsIdent)==0
                    ) {
                        veraenderteWK = ii;
                        //						System.out.println("BlWillenserklaerungStatus gef="+gef);
                        //						System.out.println("Storniert=true");
                        zugeordneteWillenserklaerungenList.get(ii).storniert = true;
                        if (lMeldung.klasse == 1) {
                            if (zugeordneteWillenserklaerungenList.get(ii).willenserklaerung == KonstWillenserklaerung.neueZutrittsIdentZuMeldung_VollmachtAnDritte) {
                                lAnzZutrittsIdentVollmacht--;
                            } else {
                                lAnzZutrittsIdentSelbst--;
                            }
                        } else {
                            lGastKartenGemeldetEigeneAktien--;
                        }
                    }
                }
                if (piAnsichtVerarbeitungOderAnalyse == 1) { /*Verarbeitungs-Ansicht*/
                } else {/*Detail-Analyse*/

                    zugeordneteWillenserklaerung = prepareWillenserklaerungStatus(willenserklaerung, willenserklaerungZusatz);

                    lWillenserklaerungStatusTextElement = new EclWillenserklaerungStatusTextElement();
                    lWillenserklaerungStatusTextElement.textNr = KonstWillenserklaerungPortalTexte.sperrenZutrittsIdent;
                    zugeordneteWillenserklaerung.textListe.add(lWillenserklaerungStatusTextElement);
                    zugeordneteWillenserklaerung.textListeIntern.add("Stornierung Eintrittskarte");
                            

                    zugeordneteWillenserklaerung.verweisAufVorgaenger = veraenderteWK;

                    zugeordneteWillenserklaerungenList.add(zugeordneteWillenserklaerung);
                    zugeordneteWillenserklaerungenList.get(veraenderteWK).verweisAufNachfolger = zugeordneteWillenserklaerungenList.size() - 1;
                }
                break;

            }

            case KonstWillenserklaerung.widerrufVollmachtAnDritte: {
                CaBug.druckeLog("KonstWillenserklaerung.widerrufVollmachtAnDritte Start", logDrucken, 10);
                int veraenderteWK = -1;
                int ii = 0;
                for (ii = 0; ii < zugeordneteWillenserklaerungenList.size(); ii++) {
                    if (ii > 2000) {
                        zuvieleWillenserklaerungen();
                        return;
                    }
                    if (zugeordneteWillenserklaerungenList.get(ii).willenserklaerungIdent == willenserklaerung.verweisAufWillenserklaerung) {
                        veraenderteWK = ii;
                        //						System.out.println("BlWillenserklaerungStatus gef="+gef);
                        //						System.out.println("Storniert=true");
                        zugeordneteWillenserklaerungenList.get(ii).storniert = true;
                        CaBug.druckeLog("Storniert ist true bei ii=" + ii, logDrucken, 10);
                        lAnzVollmachtenDritte--;
                    }
                    if (zugeordneteWillenserklaerungenList.get(ii).willenserklaerungIdent2 == willenserklaerung.verweisAufWillenserklaerung) {
                        /*Hier wird der Fall gesetzt, dass eine Eintrittskarte mit Vollmacht storniert wurde. Das Storno-Kennzeichen wird dabei
                         * schon durch den Widerruf der EK gesetzt. Deshalb hier nur für zukünftige "Fehlerabfangungen" eingebaut.
                         */
                        veraenderteWK = ii;
                    }

                }
                if (piAnsichtVerarbeitungOderAnalyse == 1) { /*Verarbeitungs-Ansicht*/
                } else {/*Detail-Analyse*/

                    zugeordneteWillenserklaerung = prepareWillenserklaerungStatus(willenserklaerung, willenserklaerungZusatz);

                    lWillenserklaerungStatusTextElement = new EclWillenserklaerungStatusTextElement();
                    lWillenserklaerungStatusTextElement.textNr = KonstWillenserklaerungPortalTexte.widerrufVollmachtAnDritte;
                    zugeordneteWillenserklaerung.textListe.add(lWillenserklaerungStatusTextElement);
                    zugeordneteWillenserklaerung.textListeIntern.add("Stornierung Vollmacht/Dritte");
                            

                    zugeordneteWillenserklaerung.verweisAufVorgaenger = veraenderteWK;

                    zugeordneteWillenserklaerungenList.add(zugeordneteWillenserklaerung);
                    zugeordneteWillenserklaerungenList.get(veraenderteWK).verweisAufNachfolger = zugeordneteWillenserklaerungenList.size() - 1;
                }
                break;

            }

            /*Unbehandelte*/
            default: {
                if (piAlleWillenserklaerungen == 1) {

                    if (piAnsichtVerarbeitungOderAnalyse == 1) { /*Bei Analyse-Ansicht bereits eingelesen!*/
                        lDbBundle.dbWillenserklaerungZusatz.leseZuIdent(willenserklaerung.willenserklaerungIdent);
                        willenserklaerungZusatz = lDbBundle.dbWillenserklaerungZusatz.willenserklaerungGefunden(0);
                    }

                    /*Neue Willenserklärung erzeugen*/
                    zugeordneteWillenserklaerung = prepareWillenserklaerungStatus(willenserklaerung, willenserklaerungZusatz);

                    zugeordneteWillenserklaerung.textListe = new LinkedList<>();
                    lWillenserklaerungStatusTextElement = new EclWillenserklaerungStatusTextElement();
                    lWillenserklaerungStatusTextElement.textNr = KonstWillenserklaerungPortalTexte.sonstiges;
                    lWillenserklaerungStatusTextElement.textListe.add(KonstWillenserklaerung.getText(willenserklaerung.willenserklaerung));
                    lWillenserklaerungStatusTextElement.textListeEN.add(KonstWillenserklaerung.getText(willenserklaerung.willenserklaerung));
                    zugeordneteWillenserklaerung.textListe.add(lWillenserklaerungStatusTextElement);
                    zugeordneteWillenserklaerung.textListeIntern.add("Sonstiges");
                            

                    zugeordneteWillenserklaerung.stornierenIstZulaessig = false;

                    zugeordneteWillenserklaerungenList.add(zugeordneteWillenserklaerung);

                }
                /*Restliche Willenserklärungen werden aktuell ignoriert*/break;
            }
            }

        }

    }

    /**Erzeugt die Willenserklärungsliste, die die jeweilige eingeloggte Person sieht
     * 
     * 
     * 
     * 
     * Welche Willenserklärungen werden übernommen?
     * --------------------------------------------
     * je nachdem wer die eingeloggte Person ist:
     * Fall 1: Aktionär selbst (also eigene Bestände)
     * 		Sieht alle Willenserklärungen ausgehend von sich selbst. Sonst nichts. Gefüllt wird aber, ob ein Vertreter bereits Briefwahl
     * 		o.ä. abgegeben hat, so dass dies dann für den Aktionär gesperrt ist.
     * 
     * Fall 2: Vertreter
     * 
     * Fall 2a: normal erhaltene Vollmacht
     * 		Sieht nur die Willenserklärungen, ausgehend von sich selbst. Sonst nichts. Gefüllt wird aber, ob ein anderer Vertreter oder der Aktionär
     * 		bereits Briefwahl o.ä. abgegeben hat, so dass dies dann für den Vertreter gesperrt ist.
     * 
     * Fall 2b: gesetzlich erhaltene Vollmacht (direkt vom Aktionär)
     * 		Sieht seine eigenen Willenserklärungen und die des Aktionärs (und umgekehrt). D.h. gesetzliche Bevollmächtigte handeln identisch zu
     * 		dem Aktionär, d.h. die von diesem Vertreter abgegebenen Willenserklärungen werden im Namen des Aktionärs wie von diesem selbst aus
     * 		abgegeben.
     * 
     * Fall 2c: mit gesetzlich erhaltenen Vollmacht vererbte Vollmachten
     * 		Sieht seine eigenen Willenserklärungen und die des gesetzlich vertretenen (und umgekehrt). D.h. auch hier wieder: gesetzlich Bevollmächtigte
     * 		handelt identisch zu dem gesetzlich Vertretenen.
     */

    public void filtereWillenserklaerungZuPerson(EclBesitzJeKennung pBesitzJeKennung, EclZugeordneteMeldungNeu pZugeordneteMeldung) {
        /*Aufnahmende Liste erzeugen*/
        List<EclWillenserklaerungStatusNeu> zugeordneteWillenserklaerungenListNurUser = new LinkedList<EclWillenserklaerungStatusNeu>();

        /* Wie wird die eingeloggte Person ermittelt (0 wird wie -1 behandelt; -1=Aktionär, 0=undefiniert)?
         * ------------------------------------------------------------------------------------------------
         * Je nach artBeziehung:
         * 1 eigene Aktien: 0 oder -1
         * 2 eigene Gastkarten: 0 oder -1
         * 3 als Bevollmächtigter erhalten:
         * 		normale Vollmacht: EclBesitzJeKennung.personNatJurIdent
         * 		wenn gesetzliche Vollmacht: 0 oder -1
         * 4 als Insti-Aktienregister zugeordnet: 0 oder -1
         * 5 als Insti-Meldung zugeordent: 0 oder -1
         * 6 als gesetzlich geerbte Vollmacht:
         * 		die PersonNatJur des gesetzlich Vertretenen. Die wurde bereits beim Erzeugen der Meldung in 
         * 		EclZugeordneteMeldung.personNatJurIdent abgelegt
         */
        int eingeloggtePerson = 0;
        switch (pZugeordneteMeldung.artBeziehung) {
        case 1:
        case 2:
        case 4:
        case 5:
            eingeloggtePerson = -1;
            break;
        case 3:
            if (pZugeordneteMeldung.vollmachtsart == 0) {//normale Vollmacht
                eingeloggtePerson = pBesitzJeKennung.personNatJurIdent;
            }
            if (pZugeordneteMeldung.vollmachtsart == 1) {//gesetzliche Vollmacht
                eingeloggtePerson = -1;
            }
            break;
        case 6:
            eingeloggtePerson = pZugeordneteMeldung.personNatJurIdent;
            break;
        }

        /*Nun alle vorhandenen Willenserklärungen durchrennen*/
        for (int i = 0; i < pZugeordneteMeldung.zugeordneteWillenserklaerungenList.size(); i++) {
            EclWillenserklaerungStatusNeu lWillenserklaerungStatusNeu = pZugeordneteMeldung.zugeordneteWillenserklaerungenList.get(i);
            int erteiltVonPerson = lWillenserklaerungStatusNeu.willenserklaerungGeberIdent;
            if ((//eingeloggte Person ==-1
            (erteiltVonPerson == 0 || erteiltVonPerson == -1) && eingeloggtePerson == -1) || (//eingeloggte Person ist natürliche Person
            erteiltVonPerson == eingeloggtePerson)) {
                /*Willenserklärung wird übernommen*/
                zugeordneteWillenserklaerungenListNurUser.add(lWillenserklaerungStatusNeu);
                /*Ggf. weitere Boolean-Werte setzen zu Willenserklärungen (vorhanden etc.)*/
                /*XXX*/
            } else {
                /*Nicht übernommen. Nun prüfen, ob ggf. Briefwahl etc, um dann entspechende Werte zu setzen*/
                /*XXX*/
            }

        }
        pZugeordneteMeldung.zugeordneteWillenserklaerungenList = zugeordneteWillenserklaerungenListNurUser;
    }

    /*+++++++++++++++++++++++++Sonderfunktionen "so ähnlich wie Status-Einlesen, werden ggf. auch hier intern verwendet+++++++*/
    
    /**
     * Erzeugen eines EclBesitzAREintrag für Erstanmeldung. Es werden nur die Aktionärsdaten
     * eingelesen und belegt, keine Meldungen etc.
     * 
     * pKennung gefüllt und pAktienregister=null => Einlesen aus Datei.
     * pAktienregister!=null => belegen mit pAktienregister
     */
    public EclBesitzAREintrag einlesenBesitzAREintrag(String pKennung, EclAktienregister pAktienregister) {
        EclBesitzAREintrag lBesitzAREintrag=new EclBesitzAREintrag();
        if (pAktienregister!=null) {
            lBesitzAREintrag.aktienregisterEintrag=pAktienregister;
        }
        else {
            lDbBundle.dbAktienregister.leseZuAktienregisternummer(pKennung);
            if (lDbBundle.dbAktienregister.anzErgebnis()==0) {
                return null;
            }
            else {
                lBesitzAREintrag.aktienregisterEintrag=lDbBundle.dbAktienregister.ergebnisPosition(0);
            }
        }
        fuelleAktionaersdatenInAREintrag(lBesitzAREintrag);
        return lBesitzAREintrag;
    }
    
    
    /*+++++++++++++++++++++++++++++++Test-Funktion+++++++++++++++++++++++++++++++*/
    public void anzeige() {
        if (CaBug.pruefeLog(logDrucken, 10) == false) {
            return;
        }
        CaBug.druckeLog("Ausdruck", logDrucken, 10);
        System.out.println("++++++++++++++++++++++Gesamt-Werte++++++++++++++++++++");
        System.out.println("weitereKennungen=" + weitereKennungen);
        System.out.println("nichtAngemeldeteVorhanden=" + nichtAngemeldeteVorhanden);
        System.out.println("angemeldeteVorhanden=" + angemeldeteVorhanden);
        System.out.println("ausgeblendeteVorhanden=" + ausgeblendeteVorhanden);
        System.out.println("angemeldeteNichtAusgeblendeteVorhanden=" + angemeldeteNichtAusgeblendeteVorhanden);
        System.out.println("bereitsErteiltWeisungBriefwahlAllgemeinDurchAndereVorhanden=" + bereitsErteiltWeisungBriefwahlAllgemeinDurchAndereVorhanden);
        if (piAusblendenMeldungen == null) {
            System.out.println("Ausblendenliste ist leer");
        } else {
            System.out.println("Ausblendenliste:");
            for (Integer pI : piAusblendenMeldungen) {
                System.out.println("pI=" + pI);
            }
        }

        System.out.println("++++++++++++++++++++++Anzeige BesitzJeKennungListe+++++++++++");
        for (int i = 0; i < besitzJeKennungListe.size(); i++) {
            EclBesitzJeKennung lBesitzJeKennung = besitzJeKennungListe.get(i);
            System.out.println("==lBesitzJeKennung.kennungsArt=" + lBesitzJeKennung.kennungsArt);
            System.out.println("==lBesitzJeKennung.kennungIntern=" + lBesitzJeKennung.kennungIntern);
            System.out.println("==lBesitzJeKennung.kennungFuerAnzeige=" + lBesitzJeKennung.kennungFuerAnzeige);
            System.out.println("==lBesitzJeKennung.zuordnungFuerPraesenzStatus=" + lBesitzJeKennung.zuordnungFuerPraesenzStatus);
            System.out.println("==lBesitzJeKennung.zuordnungIstFuerPraesenzVerifiziert=" + lBesitzJeKennung.zuordnungIstFuerPraesenzVerifiziert);

            System.out.println("==lBesitzJeKennung.eigenerAREintragVorhanden=" + lBesitzJeKennung.eigenerAREintragVorhanden);
            for (int i1 = 0; i1 < lBesitzJeKennung.eigenerAREintragListe.size(); i1++) {
                anzeigeAREintrag(lBesitzJeKennung.eigenerAREintragListe.get(i1), "eigenerAREintrag");
            }

            System.out.println("==lBesitzJeKennung.gastkartenVorhanden=" + lBesitzJeKennung.gastkartenVorhanden);

            System.out.println("==lBesitzJeKennung.erhalteneVollmachtenVorhanden=" + lBesitzJeKennung.erhalteneVollmachtenVorhanden);

            System.out.println("==lBesitzJeKennung.instiAREintraegeVorhanden=" + lBesitzJeKennung.instiAREintraegeVorhanden);
            for (int i1 = 0; i1 < lBesitzJeKennung.instiAREintraegeListe.size(); i1++) {
                anzeigeAREintrag(lBesitzJeKennung.instiAREintraegeListe.get(i1), "instiAREintrag");
            }

            System.out.println("==lBesitzJeKennung.instiMeldungenVorhanden=" + lBesitzJeKennung.instiMeldungenVorhanden);

            System.out.println("==lBesitzJeKennung.erhalteneVollmachtenMitGesetzlichVorhanden=" + lBesitzJeKennung.erhalteneVollmachtenMitGesetzlichVorhanden);

        }
    }

    private void anzeigeAREintrag(EclBesitzAREintrag eigenerAREintrag, String pAnzeigetext) {
        System.out.println("====" + "AREintrag");
        System.out.println("====" + pAnzeigetext + " angemeldet=" + eigenerAREintrag.angemeldet);
        System.out.println("====" + pAnzeigetext + " angemeldeteNichtAusgeblendeteVorhanden=" + eigenerAREintrag.angemeldeteNichtAusgeblendeteVorhanden);
        System.out.println("====" + pAnzeigetext + " gastKartenGemeldetEigeneAktien=" + eigenerAREintrag.gastKartenGemeldetEigeneAktien);

        System.out.println("====" + pAnzeigetext + " aktienregisterNummer=" + eigenerAREintrag.aktienregisterNummer);
        System.out.println("====" + pAnzeigetext + " aktionaerNameKomplett=" + eigenerAREintrag.aktionaerNameKomplett);
        System.out.println("====" + pAnzeigetext + " aktionaerOrt=" + eigenerAREintrag.aktionaerOrt);
        System.out.println("====" + pAnzeigetext + " aktionaerStimmenDE=" + eigenerAREintrag.aktionaerStimmenDE);
        System.out.println("====" + pAnzeigetext + " aktionaerStimmenEN=" + eigenerAREintrag.aktionaerStimmenEN);
        if (eigenerAREintrag.zugeordneteMeldungenListe == null) {
            System.out.println("====" + "Keine zugeordneten Meldungen");
        } else {
            for (EclZugeordneteMeldungNeu iZugeordneteMeldung : eigenerAREintrag.zugeordneteMeldungenListe) {
                anzeigeZugeordneteMeldung(iZugeordneteMeldung);
            }
        }
    }

    private void anzeigeZugeordneteMeldung(EclZugeordneteMeldungNeu plZugeordneteMeldungNeu) {
        System.out.println("======" + "zugeordneteMeldung");
        System.out.println("====== ausgeblendet=" + plZugeordneteMeldungNeu.ausgeblendet);
        System.out.println("====== bereitsErteiltWeisungBriefwahlAllgemeinDurchAndere=" + plZugeordneteMeldungNeu.bereitsErteiltWeisungBriefwahlAllgemeinDurchAndere);

    }

    /**holt die vorläufige Vollmacht dazu.
     * Rahmenbedingungen:
     * > Es gibt keine Untervollmachten
     */
    private void fuelleVorlaeufigeVollmachtZuErhaltenenVollmachten(EclZugeordneteMeldungNeu pZugeordneteMeldung, EclMeldung pMeldung, EclBesitzJeKennung pBesitzJeKennung) {
        int aktienregisterIdent = pMeldung.aktienregisterIdent;
        CaBug.druckeLog("aktienregisterIdent=" + aktienregisterIdent, logDrucken, 10);
        lDbBundle.dbVorlaeufigeVollmacht.readErteiltUndGueltigVonAktionaer(aktienregisterIdent);
        /*Es können zwei Vollmachten als Ergebnis geliefert werden - eine gesetzliche und eine Dritte.
         * Es muß nur drauf geachtet werden, dass diese nicht komplett parallel (vom selben Mitglied an den selben
         * Bevollmächtigten) gehen, sonst gibts hier Ärger ...
         */
        int anz = lDbBundle.dbVorlaeufigeVollmacht.anzErgebnis();
        CaBug.druckeLog("anz=" + anz, logDrucken, 10);
        int gef = 0;
        for (int i = 0; i < anz; i++) {
            EclVorlaeufigeVollmacht lVorlaeufigeVollmacht = lDbBundle.dbVorlaeufigeVollmacht.ergebnisPosition(i);
//            CaBug.druckeLog("i="+i, logDrucken, 10);
//            CaBug.druckeLog("lVorlaeufigeVollmacht.bevollmaechtigterAusgefuehrtArt="+lVorlaeufigeVollmacht.bevollmaechtigterAusgefuehrtArt, logDrucken, 10);
//            CaBug.druckeLog("lVorlaeufigeVollmacht.bevollmaechtigterAusgefuehrtIdent="+lVorlaeufigeVollmacht.bevollmaechtigterAusgefuehrtIdent, logDrucken, 10);
//            CaBug.druckeLog("pZugeordneteMeldung.personNatJurIdent="+pZugeordneteMeldung.personNatJurIdent, logDrucken, 10);
//            CaBug.druckeLog("pBesitzJeKennung.eclPersonenNatJur.ident="+pBesitzJeKennung.eclPersonenNatJur.ident, logDrucken, 10);
//            CaBug.druckeLog("pBesitzJeKennung.kennungArt="+pBesitzJeKennung.kennungArt, logDrucken, 10);
            if (
            /*Vorläufige Vollmacht hat PersonNatJur als Ziel*/
            //	Buggy:			(lVorlaeufigeVollmacht.bevollmaechtigterAusgefuehrtArt==2 && lVorlaeufigeVollmacht.bevollmaechtigterAusgefuehrtIdent==pZugeordneteMeldung.personNatJurIdent)
            (lVorlaeufigeVollmacht.bevollmaechtigterAusgefuehrtArt == 2 && lVorlaeufigeVollmacht.bevollmaechtigterAusgefuehrtIdent == pBesitzJeKennung.eclPersonenNatJur.ident)
                    /*Vorläufige Vollmacht hat AktienregisterIdent als Ziel*/
                    || (lVorlaeufigeVollmacht.bevollmaechtigterAusgefuehrtArt == 1 && pBesitzJeKennung.kennungArt == KonstLoginKennungArt.aktienregister
                            && pBesitzJeKennung.eclAktienregister.aktienregisterIdent == lVorlaeufigeVollmacht.bevollmaechtigterAusgefuehrtIdent)) {
                pZugeordneteMeldung.eclVorlaeufigeVollmacht = lVorlaeufigeVollmacht;
                gef = 1;
                if (lVorlaeufigeVollmacht.bevollmaechtigterAusgefuehrtIstGesetzlich == 1) {
                    pZugeordneteMeldung.vollmachtsart = 1;
                } else {
                    pZugeordneteMeldung.vollmachtsart = 0;
                }
            }
        }
        if (gef == 0) {
            CaBug.drucke("001");
        }
    }

    private boolean meldungIstAusgeblendet(int pMeldungsIdent) {
        CaBug.druckeLog("pMeldungsIdent=" + pMeldungsIdent, logDrucken, 10);
        if (piAusblendenMeldungen == null) {
            return false;
        }
        for (Integer iIdent : piAusblendenMeldungen) {
            CaBug.druckeLog("Prüfe iIdent=" + iIdent, logDrucken, 10);
            if (iIdent == pMeldungsIdent) {
                return true;
            }
        }
        return false;
    }

    /**********************************************Ab hier noch nicht überarbeitet********************************************************************/

    /*Füllen div. Daten in EclBesitzAREintrag*/
    private void fuelleAktionaersdatenInAREintrag(EclBesitzAREintrag pEclBesitzAREintrag) {
        pEclBesitzAREintrag.aktienregisterNummer = BlNummernformBasis.aufbereitenInternFuerExtern(pEclBesitzAREintrag.aktienregisterEintrag.aktionaersnummer, lDbBundle);
        pEclBesitzAREintrag.aktionaerNameKomplett = pEclBesitzAREintrag.aktienregisterEintrag.nameKomplett;
        pEclBesitzAREintrag.aktionaerOrt = pEclBesitzAREintrag.aktienregisterEintrag.ort;
        pEclBesitzAREintrag.aktionaerStimmenDE = CaString.toStringDE(pEclBesitzAREintrag.aktienregisterEintrag.stueckAktien);
        pEclBesitzAREintrag.aktionaerStimmenEN = CaString.toStringEN(pEclBesitzAREintrag.aktienregisterEintrag.stueckAktien);
        int gattung=pEclBesitzAREintrag.aktienregisterEintrag.gattungId;
        if (lDbBundle.param.paramBasis.mehrereGattungenAktiv()) {
            switch (gattung) {
            //@formatter:off
            case 1:pEclBesitzAREintrag.textNrVorAktien=KonstPortalTexte.IAUSWAHL_GATTUNG_AREINTRAG_GATTUNG1;break;
            case 2:pEclBesitzAREintrag.textNrVorAktien=KonstPortalTexte.IAUSWAHL_GATTUNG_AREINTRAG_GATTUNG2;break;
            case 3:pEclBesitzAREintrag.textNrVorAktien=KonstPortalTexte.IAUSWAHL_GATTUNG_AREINTRAG_GATTUNG3;break;
            case 4:pEclBesitzAREintrag.textNrVorAktien=KonstPortalTexte.IAUSWAHL_GATTUNG_AREINTRAG_GATTUNG4;break;
            case 5:pEclBesitzAREintrag.textNrVorAktien=KonstPortalTexte.IAUSWAHL_GATTUNG_AREINTRAG_GATTUNG5;break;
            //@formatter:on
           }
        }
        else {
            pEclBesitzAREintrag.textNrVorAktien=KonstPortalTexte.IAUSWAHL_GATTUNG_AREINTRAG_ALLGEMEIN;
        }
        

    }

    /*Füllen der Daten aktionaer* in EclZugeordneteMeldung*/
    private void fuelleAktionaersdatenInMeldung(EclZugeordneteMeldungNeu pZugeordneteMeldung, boolean mitStimmen, EclMeldung zMeldung) {

        pZugeordneteMeldung.aktionaerOrt = zMeldung.ort;

        if (mitStimmen == true) {
            pZugeordneteMeldung.aktionaerStimmenDE = CaString.toStringDE(zMeldung.stimmen);
            pZugeordneteMeldung.aktionaerStimmenEN = CaString.toStringEN(zMeldung.stimmen);
            pZugeordneteMeldung.aktionaerAktienDE = CaString.toStringDE(zMeldung.stueckAktien);
            pZugeordneteMeldung.aktionaerAktienEN = CaString.toStringEN(zMeldung.stueckAktien);
            pZugeordneteMeldung.gattung = zMeldung.gattung;
            if (lDbBundle.param.paramBasis.mehrereGattungenAktiv()) {
                switch (pZugeordneteMeldung.gattung) {
                //@formatter:off
                case 1:pZugeordneteMeldung.textNrVorAktien=KonstPortalTexte.IAUSWAHL_GATTUNG_ZUGEORDNETEMELDUNG_GATTUNG1;break;
                case 2:pZugeordneteMeldung.textNrVorAktien=KonstPortalTexte.IAUSWAHL_GATTUNG_ZUGEORDNETEMELDUNG_GATTUNG2;break;
                case 3:pZugeordneteMeldung.textNrVorAktien=KonstPortalTexte.IAUSWAHL_GATTUNG_ZUGEORDNETEMELDUNG_GATTUNG3;break;
                case 4:pZugeordneteMeldung.textNrVorAktien=KonstPortalTexte.IAUSWAHL_GATTUNG_ZUGEORDNETEMELDUNG_GATTUNG4;break;
                case 5:pZugeordneteMeldung.textNrVorAktien=KonstPortalTexte.IAUSWAHL_GATTUNG_ZUGEORDNETEMELDUNG_GATTUNG5;break;
                //@formatter:on
               }
            }
            else {
                pZugeordneteMeldung.textNrVorAktien=KonstPortalTexte.IAUSWAHL_GATTUNG_ZUGEORDNETEMELDUNG_ALLGEMEIN;
            }
        }

        /*Kombi-Felder füllen*/
        pZugeordneteMeldung.aktionaerTitelVornameName = "";
        if (zMeldung.titel.length() != 0) {
            pZugeordneteMeldung.aktionaerTitelVornameName = pZugeordneteMeldung.aktionaerTitelVornameName + zMeldung.titel + " ";
        }
        if (zMeldung.vorname.length() != 0) {
            pZugeordneteMeldung.aktionaerTitelVornameName = pZugeordneteMeldung.aktionaerTitelVornameName + zMeldung.vorname + " ";
        }
        pZugeordneteMeldung.aktionaerTitelVornameName = pZugeordneteMeldung.aktionaerTitelVornameName + zMeldung.name;

        /*XXX Ab hier alt*/
        if (mitStimmen == true) {
            pZugeordneteMeldung.stueckAktien = zMeldung.stueckAktien;

            pZugeordneteMeldung.aktionaerStimmen = zMeldung.stimmen;
            pZugeordneteMeldung.aktionaerBesitzArtKuerzel = zMeldung.besitzart;
            switch (zMeldung.besitzart) {
            case "E": {
                pZugeordneteMeldung.aktionaerBesitzArt = "Eigenbesitz";
                pZugeordneteMeldung.aktionaerBesitzArtEN = "Proprietary Possession";
                break;
            }
            case "F": {
                pZugeordneteMeldung.aktionaerBesitzArt = "Fremdbesitz";
                pZugeordneteMeldung.aktionaerBesitzArtEN = "Minority Interests";
                break;
            }
            case "V": {
                pZugeordneteMeldung.aktionaerBesitzArt = "Vollmachtsbesitz";
                pZugeordneteMeldung.aktionaerBesitzArtEN = "Proxy Possession";
                break;
            }
            }
        }

        pZugeordneteMeldung.aktionaerTitel = zMeldung.titel;
        pZugeordneteMeldung.aktionaerName = zMeldung.name;
        pZugeordneteMeldung.aktionaerVorname = zMeldung.vorname;
        pZugeordneteMeldung.aktionaerPlz = zMeldung.plz;
        if (!zMeldung.land.isEmpty()) {
            lDbBundle.dbStaaten.readCode(zMeldung.land);
            if (lDbBundle.dbStaaten.anzErgebnis() > 0) {
                pZugeordneteMeldung.aktionaerLandeskuerzel = lDbBundle.dbStaaten.ergebnisPosition(0).code;
                pZugeordneteMeldung.aktionaerLand = lDbBundle.dbStaaten.ergebnisPosition(0).nameDE;
            }
        }
        pZugeordneteMeldung.aktionaerStrasse = zMeldung.strasse;

        /*Anrede füllen*/
        int anredenNr = zMeldung.anrede;
        EclAnrede hAnrede = new EclAnrede();
        if (anredenNr != 0) {
            lDbBundle.dbAnreden.SetzeSprache(2, 0);
            lDbBundle.dbAnreden.ReadAnrede_Anredennr(anredenNr);
            hAnrede = new EclAnrede();
            if (lDbBundle.dbAnreden.AnzAnredenInReadArray > 0) {
                hAnrede = lDbBundle.dbAnreden.anredenreadarray[0];
            }
            pZugeordneteMeldung.aktionaerAnredeDE = hAnrede.anredentext;
            pZugeordneteMeldung.aktionaerAnredeEN = hAnrede.anredentextfremd;
            pZugeordneteMeldung.aktionaerBriefanredeDE = hAnrede.anredenbrief;
            pZugeordneteMeldung.aktionaerBriefanredeEN = hAnrede.anredenbrieffremd;
        }

        pZugeordneteMeldung.aktionaerNameVornameTitel = pZugeordneteMeldung.aktionaerName;
        if (pZugeordneteMeldung.aktionaerTitel.length() != 0 || pZugeordneteMeldung.aktionaerVorname.length() != 0) {
            pZugeordneteMeldung.aktionaerNameVornameTitel = pZugeordneteMeldung.aktionaerNameVornameTitel + ",";
        }
        if (pZugeordneteMeldung.aktionaerTitel.length() != 0) {
            pZugeordneteMeldung.aktionaerNameVornameTitel = pZugeordneteMeldung.aktionaerNameVornameTitel + " " + pZugeordneteMeldung.aktionaerTitel;
        }
        if (pZugeordneteMeldung.aktionaerVorname.length() != 0) {
            pZugeordneteMeldung.aktionaerNameVornameTitel = pZugeordneteMeldung.aktionaerNameVornameTitel + " " + pZugeordneteMeldung.aktionaerVorname;
        }

        pZugeordneteMeldung.aktionaerKompletteAnredeDE = pZugeordneteMeldung.aktionaerBriefanredeDE;
        pZugeordneteMeldung.aktionaerKompletteAnredeEN = pZugeordneteMeldung.aktionaerBriefanredeEN;
        if (hAnrede.istjuristischePerson != 1) {
            if (pZugeordneteMeldung.aktionaerTitel.length() != 0) {
                pZugeordneteMeldung.aktionaerKompletteAnredeDE = pZugeordneteMeldung.aktionaerKompletteAnredeDE + " " + pZugeordneteMeldung.aktionaerTitel;
                pZugeordneteMeldung.aktionaerKompletteAnredeEN = pZugeordneteMeldung.aktionaerKompletteAnredeEN + " " + pZugeordneteMeldung.aktionaerTitel;
            }
            if (pZugeordneteMeldung.aktionaerName.length() != 0) {
                pZugeordneteMeldung.aktionaerKompletteAnredeDE = pZugeordneteMeldung.aktionaerKompletteAnredeDE + " " + pZugeordneteMeldung.aktionaerName;
                pZugeordneteMeldung.aktionaerKompletteAnredeEN = pZugeordneteMeldung.aktionaerKompletteAnredeEN + " " + pZugeordneteMeldung.aktionaerName;
            }
        }

        /*Sonstiges füllen*/
        pZugeordneteMeldung.zusatzfeld3 = zMeldung.zusatzfeld3;

    }

    private void setzePraesenzFelder(EclZugeordneteMeldungNeu pEclZugeordneteMeldung, EclMeldung lEclMeldung/*, EclBesitzJeKennung pBesitzJeKennung*/) {

        //		pEclZugeordneteMeldung.bereitsPraesent=(lEclMeldung.meldungIstPraesent()==1);
        //		
        //		/*
        //		if (pEclZugeordneteMeldung.bereitsPraesent==true) {
        //			int personNatJurIdentPraesent=pEclZugeordneteMeldung.eclMeldung.virtuellerTeilnehmerIdent;
        //			if (personNatJurIdentPraesent==pBesitzJeKennung.personNatJurIdent) {
        //				pEclZugeordneteMeldung.bereitsPraesentDurchKennung=true;
        //			}
        //			else {
        //				pEclZugeordneteMeldung.bereitsPraesentDurchAndere=true;
        //			}
        //		}
        //		*/
        //		
        //		/*XXX Ab hier alt*/
        //		CaBug.druckeLog("", logDrucken);
        //		pEclZugeordneteMeldung.istPraesentNeu=lEclMeldung.meldungIstPraesent();
        //		pEclZugeordneteMeldung.identPersonNatJur=lEclMeldung.meldungPraesentePerson();
        //
        //		if (pEclZugeordneteMeldung.bereitsPraesent) {
        //			
        //			praesenteVorhanden=true;
        //		}
        //		else {
        //			nichtPraesenteVorhanden=true;
        //		}
        //		
        //		int personNatJurFuerSuche=0;
        //		if (pEclZugeordneteMeldung.identPersonNatJur==lEclMeldung.personenNatJurIdent){
        //			personNatJurFuerSuche=-1;
        //		}
        //		else{
        //			personNatJurFuerSuche=pEclZugeordneteMeldung.identPersonNatJur;
        //		}
        //		
        //		if (lDbBundle.param.paramPortal.nurRawLiveAbstimmung!=1) {
        //			int rc=lDbBundle.dbZutrittskarten.readGueltigeZuMeldungPerson(lEclMeldung.meldungsIdent, personNatJurFuerSuche);
        //			if (rc>0){
        //				pEclZugeordneteMeldung.praesenteZutrittsIdent=lDbBundle.dbZutrittskarten.ergebnisPosition(0).zutrittsIdent;
        //				CaBug.druckeLog("praesenteZutrittsIdent="+pEclZugeordneteMeldung.praesenteZutrittsIdent, "BlWillenserklaerungStatus.setzePraesenzFelder", logDrucken);
        //				pEclZugeordneteMeldung.praesenteZutrittsIdentNeben=lDbBundle.dbZutrittskarten.ergebnisPosition(0).zutrittsIdentNeben;
        //				CaBug.druckeLog("praesenteZutrittsIdentNeben="+pEclZugeordneteMeldung.praesenteZutrittsIdentNeben, "BlWillenserklaerungStatus.setzePraesenzFelder", logDrucken);
        //			}
        //		}
        //		zugeordneteMeldungenEigeneAktienArray[i].kartenart=blPraesenz.praesenz.kartenart;
        //		zugeordneteMeldungenEigeneAktienArray[i].kartennr=blPraesenz.praesenz.kartennr;

    }

    /**Ermittle Willenserkärungsnummer zu Meldung / Zutrittsident.
     * Wird benötigt, um die PDF-Nr. einer bereits ausgestellten Eintrittskarte (z.B. bei Druckwiederholung, oder Nachdruck)
     * festzulegen*/
    public int ermittleWKNummerZuMeldungZutrittsIdent(int klasse, int pMeldungsIdent, EclZutrittsIdent pEK) {

        if (klasse == 1) {
            lDbBundle.dbWillenserklaerung.leseZuMeldungsIdent(pMeldungsIdent);
        } else {
            lDbBundle.dbWillenserklaerung.leseZuMeldungsIdentGast(pMeldungsIdent);
        }
        int anz = lDbBundle.dbWillenserklaerung.anzWillenserklaerungGefunden();
        //		System.out.println("anz="+anz);

        if (anz == 0) {
            return -1;
        }
        for (int i = 0; i < anz; i++) {
            EclWillenserklaerung lWillenserklaerung = lDbBundle.dbWillenserklaerung.willenserklaerungGefunden(i);
            if (lWillenserklaerung.willenserklaerung == KonstWillenserklaerung.neueZutrittsIdentZuMeldung) {
                //				System.out.println("Willenserklärung = neueZutrittsIdentZuMeldung");
                EclZutrittsIdent wkZutrittsIdent = new EclZutrittsIdent();
                wkZutrittsIdent.zutrittsIdent = lWillenserklaerung.zutrittsIdent;
                wkZutrittsIdent.zutrittsIdentNeben = lWillenserklaerung.zutrittsIdentNeben;
                if (pEK.compareTo(wkZutrittsIdent) == 0) {
                    return lWillenserklaerung.willenserklaerungIdent;
                }
            }
        }

        return -1;
    }

}
