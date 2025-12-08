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

import java.util.List;

import de.meetingapps.meetingportal.meetComAllg.CaDatumZeit;
import de.meetingapps.meetingportal.meetComAllg.CaString;
import de.meetingapps.meetingportal.meetComBl.BlAbstimmungenWeisungen;
import de.meetingapps.meetingportal.meetComBl.BlWillenserklaerungStatus;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclAbstimmung;
import de.meetingapps.meetingportal.meetComEntities.EclAktienregister;
import de.meetingapps.meetingportal.meetComEntities.EclMeldung;
import de.meetingapps.meetingportal.meetComEntities.EclWillenserklaerung;
import de.meetingapps.meetingportal.meetComEntities.EclWillenserklaerungStatus;
import de.meetingapps.meetingportal.meetComEntities.EclWillenserklaerungZusatz;
import de.meetingapps.meetingportal.meetComEntities.EclZugeordneteMeldung;
import de.meetingapps.meetingportal.meetComKonst.KonstStimmart;
import de.meetingapps.meetingportal.meetComKonst.KonstWillenserklaerung;
import de.meetingapps.meetingportal.meetComKonst.KonstWillenserklaerungVersandartEK;
import de.meetingapps.meetingportal.meetComKonst.KonstWillenserklaerungWeg;
import de.meetingapps.meetingportal.meetComStub.StubRoot;
import de.meetingapps.meetingportal.meetingCoreReport.RpDrucken;
import de.meetingapps.meetingportal.meetingCoreReport.RpVariablen;

public class RepWillenserklaerungenAktionaer extends StubRoot {

    private RpDrucken rpDrucken = null;
    private RpVariablen rpVariablen = null;

    private BlAbstimmungenWeisungen blAbstimmungenWeisungenErfassen = null;

    public RepWillenserklaerungenAktionaer(boolean pIstServer, DbBundle pDbBundle) {
        super(pIstServer, pDbBundle);
    }

    public void init(RpDrucken pRpDrucken) {
        rpDrucken = pRpDrucken;

        blAbstimmungenWeisungenErfassen = new BlAbstimmungenWeisungen(istServer, lDbBundle);
        blAbstimmungenWeisungenErfassen.leseAgendaFuerInterneWeisungenErfassung();
    }

    /**Einlesen und Drucken aller Aktionäre. Aktuell nicht-Stub-Fähig*/
    public void druckeAktionaere(DbBundle pDbBundle, String lfdNummer, String abDatum, String nurInternetWK) {
        rpDrucken.initListe(pDbBundle);

        rpVariablen = new RpVariablen(pDbBundle);
        rpVariablen.listeWK(lfdNummer, rpDrucken);
        rpDrucken.startListe();

        int erg;
        if (abDatum.isEmpty()) {
            System.out.println("Empty");
            erg = pDbBundle.dbJoined.read_angemeldeteAktionaersnummern();
        } else {
            System.out.println("not Empty");
            erg = pDbBundle.dbJoined.read_AktionaersnummernAbZeit(abDatum);
        }
        if (erg > 0) {
            for (int i = 0; i < erg; i++) {
                String aktionaersnummer = pDbBundle.dbJoined.ergebnisKeyPosition(i);
                EclAktienregister lAktienregisterEintrag = new EclAktienregister();
                lAktienregisterEintrag.aktionaersnummer = aktionaersnummer;
                pDbBundle.dbAktienregister.leseZuAktienregisterEintrag(lAktienregisterEintrag);
                lAktienregisterEintrag = pDbBundle.dbAktienregister.ergebnisPosition(0);

                BlWillenserklaerungStatus blWillenserklaerungStatus = new BlWillenserklaerungStatus(pDbBundle);
                blWillenserklaerungStatus.piAnsichtVerarbeitungOderAnalyse = 2;
                blWillenserklaerungStatus.piRueckgabeKurzOderLang = 2;
                blWillenserklaerungStatus.piSelektionGeberOderAlle = 2;
                blWillenserklaerungStatus.leseMeldungenZuAktienregisterIdent(lAktienregisterEintrag.aktienregisterIdent);
                blWillenserklaerungStatus.ergaenzeZugeordneteMeldungenUmWillenserklaerungen(-2);

                this.einzelaktionaer(aktionaersnummer, lAktienregisterEintrag, blWillenserklaerungStatus, nurInternetWK, abDatum);
            }

        }
        rpDrucken.endeListe();
    }

    /**Einlesen und Drucken aller Aktionäre. Aktuell nicht-Stub-Fähig
     * pAktionaersnummern muß bereits formatmäßig fertig aufbereitet sein*/
    public void druckeAktionaersListe(DbBundle pDbBundle, String lfdNummer, List<String> pAktionaersnummern) {
        rpDrucken.initListe(pDbBundle);

        rpVariablen = new RpVariablen(pDbBundle);
        rpVariablen.listeWK(lfdNummer, rpDrucken);
        rpDrucken.startListe();

        for (int i = 0; i < pAktionaersnummern.size(); i++) {
            String aktionaersnummer = pAktionaersnummern.get(i);
            EclAktienregister lAktienregisterEintrag = new EclAktienregister();
            lAktienregisterEintrag.aktionaersnummer = aktionaersnummer;
            pDbBundle.dbAktienregister.leseZuAktienregisterEintrag(lAktienregisterEintrag);
            lAktienregisterEintrag = pDbBundle.dbAktienregister.ergebnisPosition(0);

            BlWillenserklaerungStatus blWillenserklaerungStatus = new BlWillenserklaerungStatus(pDbBundle);
            blWillenserklaerungStatus.piAnsichtVerarbeitungOderAnalyse = 2;
            blWillenserklaerungStatus.piRueckgabeKurzOderLang = 2;
            blWillenserklaerungStatus.piSelektionGeberOderAlle = 2;
            blWillenserklaerungStatus.leseMeldungenZuAktienregisterIdent(lAktienregisterEintrag.aktienregisterIdent);
            blWillenserklaerungStatus.ergaenzeZugeordneteMeldungenUmWillenserklaerungen(-2);

            this.einzelaktionaer(aktionaersnummer, lAktienregisterEintrag, blWillenserklaerungStatus, "1", "");
        }

        rpDrucken.endeListe();
    }

    /**Kann auch "Standalone" - mit entsprechender Vorbereitung - aufgerufen werden
     * Kein Datenzugriff!*/
    public void druckeEinzelnenAktionaer(DbBundle pDbBundle, String aktionaersnummer, EclAktienregister lAktienregisterEintrag, BlWillenserklaerungStatus blWillenserklaerungStatus) {
        rpDrucken.initListe(pDbBundle);

        rpVariablen = new RpVariablen(pDbBundle);
        rpVariablen.listeWK("01", rpDrucken);

        rpDrucken.startListe();

        this.einzelaktionaer(aktionaersnummer, lAktienregisterEintrag, blWillenserklaerungStatus, "1", "");

    }

    private void einzelaktionaer(String aktionaersnummer, EclAktienregister lAktienregisterEintrag, BlWillenserklaerungStatus blWillenserklaerungStatus, String nurInternetWK, String druckenAb) {

        /*Notwendige Parameter
         * 
         * Input:
         * String aktionaersnummer
         * 
         * 
         * Output:
         * EclAktienregisterEintrag lAktienregisterEintrag
         * BlWillenserklaerungStatus blWillenserklaerungStatus
         * 
         */

        if ((nurInternetWK.compareTo("1") == 0 || blWillenserklaerungStatus.rcHatNichtNurPortalWillenserklaerungen == true)
                && (druckenAb.isEmpty() || druckenAb.compareTo(blWillenserklaerungStatus.rcDatumLetzteWillenserklaerung) < 0)

        ) {

            rpVariablen.fuelleFeld(rpDrucken, "Verwaltung.Zeilenart", "01");

            rpVariablen.fuelleFeld(rpDrucken, "Aktionaer.Aktionaersnummer", lAktienregisterEintrag.aktionaersnummer);
            rpVariablen.fuelleFeld(rpDrucken, "Aktionaer.Stimmen", Long.toString(lAktienregisterEintrag.stimmen));
            rpVariablen.fuelleFeld(rpDrucken, "Aktionaer.StimmenDE", CaString.toStringDE(lAktienregisterEintrag.stimmen));
            rpVariablen.fuelleFeld(rpDrucken, "Aktionaer.Titel", lAktienregisterEintrag.titel);
            if (lAktienregisterEintrag.istJuristischePerson == 1) {
                rpVariablen.fuelleFeld(rpDrucken, "Aktionaer.Name", CaString.trunc(lAktienregisterEintrag.name1 + " " + lAktienregisterEintrag.name2 + " " + lAktienregisterEintrag.name3, 80).trim());
            } else {/*Natürliche Person*/
                rpVariablen.fuelleFeld(rpDrucken, "Aktionaer.Name", lAktienregisterEintrag.nachname);
            }

            rpVariablen.fuelleFeld(rpDrucken, "Aktionaer.Vorname", lAktienregisterEintrag.vorname);
            rpVariablen.fuelleFeld(rpDrucken, "Aktionaer.Plz", lAktienregisterEintrag.postleitzahl);
            rpVariablen.fuelleFeld(rpDrucken, "Aktionaer.Ort", lAktienregisterEintrag.ort);
            rpVariablen.fuelleFeld(rpDrucken, "Aktionaer.Strasse", lAktienregisterEintrag.strasse);
            rpVariablen.fuelleFeld(rpDrucken, "Aktionaer.BesitzArtKuerzel", lAktienregisterEintrag.besitzart);
            rpDrucken.druckenListe();

            int i1;
            for (i1 = 0; i1 < blWillenserklaerungStatus.zugeordneteMeldungenEigeneAktienArray.length; i1++) {
                EclZugeordneteMeldung eclZugeordneteMeldung = blWillenserklaerungStatus.zugeordneteMeldungenEigeneAktienArray[i1];
                rpVariablen.fuelleFeld(rpDrucken, "Verwaltung.Zeilenart", "02");
                EclMeldung eclMeldung = eclZugeordneteMeldung.eclMeldung;
                rpVariablen.fuelleFeld(rpDrucken, "Meldung.Ident", Integer.toString(eclMeldung.meldungsIdent));
                rpVariablen.fuelleFeld(rpDrucken, "Meldung.PersonIdent", Integer.toString(eclMeldung.personenNatJurIdent));
                rpVariablen.fuelleFeld(rpDrucken, "Meldung.Stimmen", Long.toString(eclMeldung.stimmen));
                rpVariablen.fuelleFeld(rpDrucken, "Meldung.StimmenDE", CaString.toStringDE(eclMeldung.stimmen));
                rpDrucken.druckenListe();

                int i2;
                if (eclZugeordneteMeldung.zugeordneteWillenserklaerungenList != null) {
                    for (i2 = 0; i2 < eclZugeordneteMeldung.zugeordneteWillenserklaerungenList.size(); i2++) {
                        EclWillenserklaerungStatus eclWillenserklaerungStatus = eclZugeordneteMeldung.zugeordneteWillenserklaerungenList.get(i2);
                        EclWillenserklaerung eclWillenserklaerung = eclWillenserklaerungStatus.eclWillenserklaerung;
                        EclWillenserklaerungZusatz eclWillenserklaerungZusatz = eclWillenserklaerungStatus.eclWillenserklaerungZusatz;
                        rpVariablen.fuelleFeld(rpDrucken, "Verwaltung.Zeilenart", "03");
                        rpVariablen.fuelleFeld(rpDrucken, "Willenserkl.Lfd", Integer.toString(i2 + 1));
                        rpVariablen.fuelleFeld(rpDrucken, "Willenserkl.Ident", Integer.toString(eclWillenserklaerung.willenserklaerungIdent));
                        rpVariablen.fuelleFeld(rpDrucken, "Willenserkl.ArtNr", Integer.toString(eclWillenserklaerung.willenserklaerung));
                        rpVariablen.fuelleFeld(rpDrucken, "Willenserkl.ArtText", KonstWillenserklaerung.getText(eclWillenserklaerung.willenserklaerung));
                        rpVariablen.fuelleFeld(rpDrucken, "Willenserkl.GeberIdent", Integer.toString(eclWillenserklaerung.willenserklaerungGeberIdent));
                        rpVariablen.fuelleFeld(rpDrucken, "Willenserkl.ErteiltAufWeg", KonstWillenserklaerungWeg.getText(eclWillenserklaerung.erteiltAufWeg));

                        if (eclWillenserklaerungStatus.veraendert) {
                            rpVariablen.fuelleFeld(rpDrucken, "Willenserkl.Veraendert", "Verändert");
                        } else {
                            rpVariablen.fuelleFeld(rpDrucken, "Willenserkl.Veraendert", "");
                        }

                        if (eclWillenserklaerungStatus.storniert) {
                            rpVariablen.fuelleFeld(rpDrucken, "Willenserkl.Storniert", "Storniert");
                        } else {
                            rpVariablen.fuelleFeld(rpDrucken, "Willenserkl.Storniert", "");
                        }

                        if (eclWillenserklaerungStatus.verweisAufVorgaenger != -1) {
                            rpVariablen.fuelleFeld(rpDrucken, "Willenserkl.Vorgaenger", Integer.toString(eclWillenserklaerungStatus.verweisAufVorgaenger + 1));
                        } else {
                            rpVariablen.fuelleFeld(rpDrucken, "Willenserkl.Vorgaenger", "");
                        }
                        if (eclWillenserklaerungStatus.verweisAufNachfolger != -1) {
                            rpVariablen.fuelleFeld(rpDrucken, "Willenserkl.Nachfolger", Integer.toString(eclWillenserklaerungStatus.verweisAufNachfolger + 1));
                        } else {
                            rpVariablen.fuelleFeld(rpDrucken, "Willenserkl.Nachfolger", "");
                        }
                        rpVariablen.fuelleFeld(rpDrucken, "Willenserkl.Erteilungszeit", CaDatumZeit.DatumZeitStringFuerAnzeige(eclWillenserklaerung.veraenderungszeit));
                        rpVariablen.fuelleFeld(rpDrucken, "Willenserkl.Quelle", eclWillenserklaerungStatus.eclWillenserklaerungZusatz.quelle);
                        rpDrucken.druckenListe();

                        /*Zurücksetzen für einzelne Willenserklärungen*/
                        rpVariablen.fuelleFeld(rpDrucken, "WillenserklEK.ZutrittsIdent", "");
                        rpVariablen.fuelleFeld(rpDrucken, "WillenserklEK.VersandartNr", "");
                        rpVariablen.fuelleFeld(rpDrucken, "WillenserklEK.VersandartText", "");
                        rpVariablen.fuelleFeld(rpDrucken, "WillenserklEK.VersandAdresse1", "");
                        rpVariablen.fuelleFeld(rpDrucken, "WillenserklEK.VersandAdresse2", "");
                        rpVariablen.fuelleFeld(rpDrucken, "WillenserklEK.VersandAdresse3", "");
                        rpVariablen.fuelleFeld(rpDrucken, "WillenserklEK.VersandAdresse4", "");
                        rpVariablen.fuelleFeld(rpDrucken, "WillenserklEK.VersandAdresse5", "");
                        rpVariablen.fuelleFeld(rpDrucken, "WillenserklEK.EmailAdresseEK", "");
                        rpVariablen.fuelleFeld(rpDrucken, "WillenserklEK.VersandAdresseUeberprueft", "");
                        rpVariablen.fuelleFeld(rpDrucken, "WillenserklEK.VersandartUeberprueftNr", "");
                        rpVariablen.fuelleFeld(rpDrucken, "WillenserklEK.VersandartUeberprueftText", "");
                        rpVariablen.fuelleFeld(rpDrucken, "WillenserklEK.EintrittskarteWurdeGedruckt", "");
                        rpVariablen.fuelleFeld(rpDrucken, "WillenserklEK.ErstesDruckDatum", "");
                        rpVariablen.fuelleFeld(rpDrucken, "WillenserklEK.LetztesDruckDatum", "");

                        rpVariablen.fuelleFeld(rpDrucken, "WillenserklVert.VertreterIdent", "");
                        rpVariablen.fuelleFeld(rpDrucken, "WillenserklVert.VertreterName", "");
                        rpVariablen.fuelleFeld(rpDrucken, "WillenserklVert.VertreterVorname", "");
                        rpVariablen.fuelleFeld(rpDrucken, "WillenserklVert.VertreterOrt", "");

                        rpVariablen.fuelleFeld(rpDrucken, "WillenserklWei.Nummer", "");
                        rpVariablen.fuelleFeld(rpDrucken, "WillenserklWei.NummerIndex", "");
                        rpVariablen.fuelleFeld(rpDrucken, "WillenserklWei.AnzeigeBezeichnungLang", "");
                        rpVariablen.fuelleFeld(rpDrucken, "WillenserklWei.IstUeberschrift", "");
                        rpVariablen.fuelleFeld(rpDrucken, "WillenserklWei.Stimmart", "");

                        switch (eclWillenserklaerung.willenserklaerung) {
                        case KonstWillenserklaerung.neueZutrittsIdentZuMeldung: {
                            rpVariablen.fuelleFeld(rpDrucken, "Verwaltung.Zeilenart", "03-01");
                            rpVariablen.fuelleFeld(rpDrucken, "WillenserklEK.ZutrittsIdent", eclWillenserklaerung.zutrittsIdent + "-" + eclWillenserklaerung.zutrittsIdentNeben);
                            rpVariablen.fuelleFeld(rpDrucken, "WillenserklEK.VersandartNr", Integer.toString(eclWillenserklaerungZusatz.versandartEK));
                            rpVariablen.fuelleFeld(rpDrucken, "WillenserklEK.VersandartText", KonstWillenserklaerungVersandartEK.getText(eclWillenserklaerungZusatz.versandartEK));
                            rpVariablen.fuelleFeld(rpDrucken, "WillenserklEK.VersandAdresse1", eclWillenserklaerungZusatz.versandadresse1);
                            rpVariablen.fuelleFeld(rpDrucken, "WillenserklEK.VersandAdresse2", eclWillenserklaerungZusatz.versandadresse2);
                            rpVariablen.fuelleFeld(rpDrucken, "WillenserklEK.VersandAdresse3", eclWillenserklaerungZusatz.versandadresse3);
                            rpVariablen.fuelleFeld(rpDrucken, "WillenserklEK.VersandAdresse4", eclWillenserklaerungZusatz.versandadresse4);
                            rpVariablen.fuelleFeld(rpDrucken, "WillenserklEK.VersandAdresse5", eclWillenserklaerungZusatz.versandadresse5);
                            rpVariablen.fuelleFeld(rpDrucken, "WillenserklEK.EmailAdresseEK", eclWillenserklaerungZusatz.emailAdresseEK);
                            if (eclWillenserklaerungZusatz.versandadresseUeberprueft != 0) {
                                rpVariablen.fuelleFeld(rpDrucken, "WillenserklEK.VersandAdresseUeberprueft", Integer.toString(eclWillenserklaerungZusatz.versandadresseUeberprueft));
                                rpVariablen.fuelleFeld(rpDrucken, "WillenserklEK.VersandartUeberprueftNr", Integer.toString(eclWillenserklaerungZusatz.versandartEKUeberprueft));
                                rpVariablen.fuelleFeld(rpDrucken, "WillenserklEK.VersandartUeberprueftText",
                                        KonstWillenserklaerungVersandartEK.getText(eclWillenserklaerungZusatz.versandartEKUeberprueft));
                            }
                            rpVariablen.fuelleFeld(rpDrucken, "WillenserklEK.EintrittskarteWurdeGedruckt", Integer.toString(eclWillenserklaerungZusatz.eintrittskarteWurdeGedruckt));
                            rpVariablen.fuelleFeld(rpDrucken, "WillenserklEK.ErstesDruckDatum", eclWillenserklaerungZusatz.erstesDruckDatum);
                            rpVariablen.fuelleFeld(rpDrucken, "WillenserklEK.LetztesDruckDatum", eclWillenserklaerungZusatz.letztesDruckDatum);
                            rpDrucken.druckenListe();

                            break;
                        }
                        case KonstWillenserklaerung.vollmachtAnDritte: {
                            rpVariablen.fuelleFeld(rpDrucken, "Verwaltung.Zeilenart", "03-02");
                            rpVariablen.fuelleFeld(rpDrucken, "WillenserklVert.VertreterIdent", Integer.toString(eclWillenserklaerungStatus.eclPersonenNatJurVertreter.ident));
                            rpVariablen.fuelleFeld(rpDrucken, "WillenserklVert.VertreterName", eclWillenserklaerungStatus.eclPersonenNatJurVertreter.name);
                            rpVariablen.fuelleFeld(rpDrucken, "WillenserklVert.VertreterVorname", eclWillenserklaerungStatus.eclPersonenNatJurVertreter.vorname);
                            rpVariablen.fuelleFeld(rpDrucken, "WillenserklVert.VertreterOrt", eclWillenserklaerungStatus.eclPersonenNatJurVertreter.ort);
                            rpDrucken.druckenListe();

                            break;
                        }
                        case KonstWillenserklaerung.vollmachtUndWeisungAnSRV:
                        case KonstWillenserklaerung.aendernWeisungAnSRV:
                        case KonstWillenserklaerung.briefwahl:
                        case KonstWillenserklaerung.aendernBriefwahl:
                        case KonstWillenserklaerung.vollmachtUndWeisungAnKIAV:
                        case KonstWillenserklaerung.aendernWeisungAnKIAV:
                        case KonstWillenserklaerung.organisatorischMitWeisungInSammelkarte:
                        case KonstWillenserklaerung.aendernWeisungOrganisatorischInSammelkarte:
                        case KonstWillenserklaerung.dauervollmachtAnKIAV: {
                            rpVariablen.fuelleFeld(rpDrucken, "Verwaltung.Zeilenart", "03-03U");
                            rpDrucken.druckenListe();

                            rpVariablen.fuelleFeld(rpDrucken, "Verwaltung.Zeilenart", "03-03");
                            for (int i3 = 0; i3 < blAbstimmungenWeisungenErfassen.liefereAnzAgendaArray(lAktienregisterEintrag.getGattungId()); i3++) {
                                EclAbstimmung lAbstimmung = blAbstimmungenWeisungenErfassen.rcAgendaArray[lAktienregisterEintrag.getGattungId()][i3];
                                rpVariablen.fuelleFeld(rpDrucken, "WillenserklWei.Nummer", lAbstimmung.nummerKey);
                                rpVariablen.fuelleFeld(rpDrucken, "WillenserklWei.NummerIndex", lAbstimmung.nummerindexKey);
                                rpVariablen.fuelleFeld(rpDrucken, "WillenserklWei.AnzeigeBezeichnungLang", lAbstimmung.kurzBezeichnung);

                                if (!lAbstimmung.liefereIstUeberschift()) {
                                    int posInWeisung = lAbstimmung.identWeisungssatz;
                                    int abstimmung = eclWillenserklaerungStatus.eclWeisungMeldung.abgabe[posInWeisung];
                                    String abstimmungString = KonstStimmart.getText(abstimmung);
                                    rpVariablen.fuelleFeld(rpDrucken, "WillenserklWei.Stimmart", abstimmungString);
                                    rpVariablen.fuelleFeld(rpDrucken, "WillenserklWei.IstUeberschrift", "0");
                                } else {
                                    rpVariablen.fuelleFeld(rpDrucken, "WillenserklWei.Stimmart", "");
                                    rpVariablen.fuelleFeld(rpDrucken, "WillenserklWei.IstUeberschrift", "1");
                                }
                                rpDrucken.druckenListe();

                            }

                            if (lDbBundle.param.paramAbstimmungParameter.weisungenGegenantraegeInternSeparat) {

                                for (int i3 = 0; i3 < blAbstimmungenWeisungenErfassen.liefereAnzGegenantraegeArray(lAktienregisterEintrag.getGattungId()); i3++) {
                                    EclAbstimmung lAbstimmung = blAbstimmungenWeisungenErfassen.rcGegenantraegeArray[lAktienregisterEintrag.getGattungId()][i3];
                                    rpVariablen.fuelleFeld(rpDrucken, "WillenserklWei.Nummer", lAbstimmung.nummerKey);
                                    rpVariablen.fuelleFeld(rpDrucken, "WillenserklWei.NummerIndex", lAbstimmung.nummerindexKey);
                                    rpVariablen.fuelleFeld(rpDrucken, "WillenserklWei.AnzeigeBezeichnungLang", lAbstimmung.kurzBezeichnung);

                                    if (!lAbstimmung.liefereIstUeberschift()) {
                                        int posInWeisung = lAbstimmung.identWeisungssatz;
                                        int abstimmung = eclWillenserklaerungStatus.eclWeisungMeldung.abgabe[posInWeisung];
                                        String abstimmungString = KonstStimmart.getText(abstimmung);
                                        rpVariablen.fuelleFeld(rpDrucken, "WillenserklWei.Stimmart", abstimmungString);
                                        rpVariablen.fuelleFeld(rpDrucken, "WillenserklWei.IstUeberschrift", "0");
                                    } else {
                                        rpVariablen.fuelleFeld(rpDrucken, "WillenserklWei.Stimmart", "");
                                        rpVariablen.fuelleFeld(rpDrucken, "WillenserklWei.IstUeberschrift", "1");
                                    }
                                    rpDrucken.druckenListe();

                                }
                            }

                            break;
                        }
                        }

                    }

                }

            }
            rpDrucken.newPageListe();
        }
    }

}
