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
package de.meetingapps.meetingportal.meetingportTController;

import de.meetingapps.meetingportal.meetComAllg.CaDatumZeit;
import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComBa.BaMailM;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEclM.EclAnredeListeM;
import de.meetingapps.meetingportal.meetComEclM.EclDbM;
import de.meetingapps.meetingportal.meetComEclM.EclGastM;
import de.meetingapps.meetingportal.meetComEclM.EclMeldungenMeldungenListeM;
import de.meetingapps.meetingportal.meetComEclM.EclParamM;
import de.meetingapps.meetingportal.meetComEclM.EclPortalTexteM;
import de.meetingapps.meetingportal.meetComEntities.EclMeldung;
import de.meetingapps.meetingportal.meetComEntities.EclPersonenNatJur;
import de.meetingapps.meetingportal.meetComEntities.EclWillenserklaerungStatusNeu;
import de.meetingapps.meetingportal.meetComEntities.EclWillenserklaerungZusatz;
import de.meetingapps.meetingportal.meetComEntities.EclZugeordneteMeldungNeu;
import de.meetingapps.meetingportal.meetComKonst.KonstEintrittskarteDetailArt;
import de.meetingapps.meetingportal.meetComKonst.KonstMeldung;
import de.meetingapps.meetingportal.meetComKonst.KonstPortalAktion;
import de.meetingapps.meetingportal.meetComKonst.KonstPortalView;
import de.meetingapps.meetingportal.meetComKonst.KonstWillenserklaerungVersandartEK;
import de.meetingapps.meetingportal.meetingCoreReport.RpBrowserAnzeigen;
import de.meetingapps.meetingportal.meetingportTFunktionen.TFunktionen;
import de.meetingapps.meetingportal.meetingportTFunktionen.TSessionVerwaltung;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@RequestScoped
@Named
public class TEintrittskarteQuittungUDetail {

    private @Inject EclDbM eclDbM;
    private @Inject TSession tSession;
    private @Inject TSessionVerwaltung tSessionVerwaltung;
    private @Inject TAuswahl tAuswahl;
    private @Inject TFunktionen tFunktionen;
    private @Inject TEintrittskarteQuittungUDetailSession tEintrittskarteQuittungUDetailSession;

    private @Inject TWillenserklaerungSession tWillenserklaerungSession;

    private @Inject EclParamM eclParamM;
    private @Inject EclPortalTexteM eclPortalTexteM;
    private @Inject BaMailM baMailm;

    /*TODO: die folgenden Injects sind nur wg. 
     *initDetailGK eingefügt - noch nicht konsolidiert!*/
    private @Inject EclGastM eclGastM;
    private @Inject EclAnredeListeM eclAnredeListeListeM;
    private @Inject EclMeldungenMeldungenListeM eclMeldungenMeldungenListeM;

    /**********************************************Eintrittskarte_Quittung***************************************************/

    public void doQuittungWeiter() {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.EINTRITTSKARTE_QUITTUNG)) {
            return;
        }
        eclDbM.openAll();
        tAuswahl.startAuswahl(true);
        eclDbM.closeAll();
        tSessionVerwaltung.setzeEnde(tFunktionen.waehleAuswahl());
        return;
    }

    public void doDetailWeiter() {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.EINTRITTSKARTE_DETAIL)) {
            return;
        }
        eclDbM.openAll();
        tAuswahl.startAuswahl(true);
        eclDbM.closeAll();
        tSessionVerwaltung.setzeEnde(tFunktionen.waehleAuswahl());
        return;
    }

    public void doQuittungDruckeEintrittskarte() {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.EINTRITTSKARTE_QUITTUNG)) {
            return;
        }
        doDruckeEintrittskarte(1);
        tSessionVerwaltung.setzeEnde(KonstPortalView.EINTRITTSKARTE_QUITTUNG);
        return;
    }

    public void doQuittungDruckeEintrittskarte2() {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.EINTRITTSKARTE_QUITTUNG)) {
            return;
        }
        doDruckeEintrittskarte(2);
        tSessionVerwaltung.setzeEnde(KonstPortalView.EINTRITTSKARTE_QUITTUNG);
        return;
    }

    public void doQuittungMailEintrittskarte() {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.EINTRITTSKARTE_QUITTUNG)) {
            return;
        }
        doMailEintrittskarte(1);
        tSessionVerwaltung.setzeEnde(KonstPortalView.EINTRITTSKARTE_QUITTUNG);
        return;
    }

    public void doQuittungMailEintrittskarte2() {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.EINTRITTSKARTE_QUITTUNG)) {
            return;
        }
        doMailEintrittskarte(2);
        tSessionVerwaltung.setzeEnde(KonstPortalView.EINTRITTSKARTE_QUITTUNG);
        return;
    }

    /*********************************************Eintrittskarte_Detail****************************************************/

    /**Funktion, die aufgerufen wird, wenn aus der Status-Seite heraus Details für eine
     * Eintrittskarte angezeigt werden.
     * 
     * 
     * Übergabeparameter:
     *      EclZugeordneteMeldungM = Meldung, zu der die anzuzeigende Willenserklärung gehört
     *      EclWillenserklaerungStatusM = Willenserklärung, die angezeigt werden soll.
     * 
     * 
     * Bei Ausgabe gefüllt:
     *      DlgVariablen.artEintrittskarte
     *      DlgVariablen.eintrittskarteVersandart
     *      DlgVariablen.eintrittskarteAbweichendeAdresse1/.2/.3/.4/.5
     *      DlgVariablen.eintrittskarteEmail
     *      DlgVariablen.vollmachtName/.vollmachtVorname/.vollmachtOrt
     *      DlgVariablen.eintrittskartePdfNr
     *      EclWillenserklaerungStatusM (=angezeigte Willenserklärung)
     *      EclZugeordneteMeldungM (=Meldung, zu der die angezeigte Willenserklärung gehört)
     * 
     * DbBundle wird als Parameter übergeben.
     */
    public void initDetailEK(DbBundle pDbBundle, EclZugeordneteMeldungNeu pZugeordneteMeldungM,
            EclWillenserklaerungStatusNeu pWillenserklaerungStatusM) {
        if (pZugeordneteMeldungM.getKlasse() == KonstMeldung.MELDUNG_IST_GAST) {
            tEintrittskarteQuittungUDetailSession.setArtEintrittskarte(KonstEintrittskarteDetailArt.GASTKARTE);
        } else {
            if (pWillenserklaerungStatusM.getBevollmaechtigterDritterIdent() != 0) {
                tEintrittskarteQuittungUDetailSession
                        .setArtEintrittskarte(KonstEintrittskarteDetailArt.EINTRITTSKARTE_MIT_VOLLMACHT);
            } else {
                tEintrittskarteQuittungUDetailSession.setArtEintrittskarte(KonstEintrittskarteDetailArt.EINTRITTSKARTE);
            }
        }

        /*WillenserklärungZusatz einlesen*/
        pDbBundle.dbWillenserklaerungZusatz.leseZuIdent(pWillenserklaerungStatusM.getWillenserklaerungIdent());
        EclWillenserklaerungZusatz lWillenserklaerungZusatz = pDbBundle.dbWillenserklaerungZusatz.willenserklaerungArray[0];

        /* aDlgVariablen.eintrittskarteVersandart = Versandart
        *  1 = Auf Aktienregister-Adresse, im Batch ausgedruckt
        *  2 = Auf Versandadresse, im Batch ausgedruckt
        *  3 = Online-Druck
        *  4 = per Email*/
        switch (lWillenserklaerungZusatz.versandartEK) {
        case KonstWillenserklaerungVersandartEK.VERSANDLAUF_ADRESSE_LAUT_AKTIENREGISTER:
            tWillenserklaerungSession.setEintrittskarteVersandart(
                    KonstWillenserklaerungVersandartEK.VERSANDLAUF_ADRESSE_LAUT_AKTIENREGISTER);
            break;
        case KonstWillenserklaerungVersandartEK.VERSANDLAUF_ADRESSE_ABWEICHEND_WIE_EINGEGEBEN:
            tWillenserklaerungSession.setEintrittskarteVersandart(
                    KonstWillenserklaerungVersandartEK.VERSANDLAUF_ADRESSE_ABWEICHEND_WIE_EINGEGEBEN);
            break;
        case KonstWillenserklaerungVersandartEK.ONLINE_AUSDRUCK:
            tWillenserklaerungSession.setEintrittskarteVersandart(KonstWillenserklaerungVersandartEK.ONLINE_AUSDRUCK);
            break;
        case KonstWillenserklaerungVersandartEK.ONLINE_EMAIL:
            tWillenserklaerungSession.setEintrittskarteVersandart(KonstWillenserklaerungVersandartEK.ONLINE_EMAIL);
            break;
        case KonstWillenserklaerungVersandartEK.GASTKARTE_WIE_EINTRITTSKARTE:
            /*Wird hier "umgebogen" zur vereinfachten Anzeige*/
            tWillenserklaerungSession.setEintrittskarteVersandart(
                    KonstWillenserklaerungVersandartEK.VERSANDLAUF_ADRESSE_ABWEICHEND_WIE_EINGEGEBEN);
            break;
        }

        /*Daten für Eintrittskartengedruckt füllen*/
        if (lWillenserklaerungZusatz.eintrittskarteWurdeGedruckt > 0) {
            tEintrittskarteQuittungUDetailSession.setEintrittskarteWurdeGedruckt(true);
            String hString = lWillenserklaerungZusatz.erstesDruckDatum;
            tEintrittskarteQuittungUDetailSession
                    .setEintrittskarteDruckDatum(CaDatumZeit.DatumZeitStringFuerAnzeige(hString).substring(0, 10));

        } else {
            tEintrittskarteQuittungUDetailSession.setEintrittskarteWurdeGedruckt(false);
            tEintrittskarteQuittungUDetailSession.setEintrittskarteDruckDatum("");
        }

        /* aDlgVariablen.eintrittskarteAbweichendeAdresse1 bis 5: ggf. abweichende Versandadresse*/
        tWillenserklaerungSession.setEintrittskarteAbweichendeAdresse1(lWillenserklaerungZusatz.versandadresse1);
        tWillenserklaerungSession.setEintrittskarteAbweichendeAdresse2(lWillenserklaerungZusatz.versandadresse2);
        tWillenserklaerungSession.setEintrittskarteAbweichendeAdresse3(lWillenserklaerungZusatz.versandadresse3);
        tWillenserklaerungSession.setEintrittskarteAbweichendeAdresse4(lWillenserklaerungZusatz.versandadresse4);
        tWillenserklaerungSession.setEintrittskarteAbweichendeAdresse5(lWillenserklaerungZusatz.versandadresse5);

        /* aDlgVariablen.eintrittskarteEmail: ggf. Email-Adresse*/
        tWillenserklaerungSession.setEintrittskarteEmail(lWillenserklaerungZusatz.emailAdresseEK);

        /* aDlgVariablen.vollmachtName, vollmachtVorname, vollmachtOrt: ggf. Bevollmächtigter lt. Eintrittskarte*/
        EclPersonenNatJur lPersonNatJur = new EclPersonenNatJur();
        lPersonNatJur.ident = pWillenserklaerungStatusM.getBevollmaechtigterDritterIdent();

        tWillenserklaerungSession.setVollmachtName("");
        tWillenserklaerungSession.setVollmachtVorname("");
        tWillenserklaerungSession.setVollmachtOrt("");
        if (lPersonNatJur.ident != 0) {
            pDbBundle.dbPersonenNatJur.read(lPersonNatJur.ident);
            lPersonNatJur = pDbBundle.dbPersonenNatJur.personenNatJurArray[0];
            tWillenserklaerungSession.setVollmachtName(lPersonNatJur.name);
            tWillenserklaerungSession.setVollmachtVorname(lPersonNatJur.vorname);
            tWillenserklaerungSession.setVollmachtOrt(lPersonNatJur.ort);
        }

        /* aDlgVariablen.eintrittskartePdfNr: Nr. des auszudruckenden Formulars (=Ident der entsprechenden Willenserklärung)*/
        tWillenserklaerungSession.setEintrittskartePdfNr(pWillenserklaerungStatusM.getWillenserklaerungIdent());
        tWillenserklaerungSession.setZutrittsIdent(pWillenserklaerungStatusM.zutrittsIdent);
        
        tWillenserklaerungSession.initZugeordneteMeldungFuerAusfuehrungListe();
        tWillenserklaerungSession.addZugeordneteMeldungFuerAusfuehrungListe(pZugeordneteMeldungM,
                pWillenserklaerungStatusM);
        tWillenserklaerungSession.ermittleGattungenFuerZugeordneteMeldungFuerAusfuehrungListe();
        tWillenserklaerungSession.ermittleGattungFuerEinzigenBesitzZugeordneteMeldungFuerAusfuehrungList();
    }

    public void initDetailGK(DbBundle pDbBundle, EclZugeordneteMeldungNeu pZugeordneteMeldungM,
            EclWillenserklaerungStatusNeu pWillenserklaerungStatusM) {
        /*TODO: Funktion ist nur umgestellt damit ohne Fehler compiliert wird, noch nicht getestet und konsolidiert*/
        initDetailEK(eclDbM.getDbBundle(), pZugeordneteMeldungM, pWillenserklaerungStatusM);

        tEintrittskarteQuittungUDetailSession.setIdentMasterGast(pZugeordneteMeldungM.getMeldungsIdent());
        EclMeldung lMeldung = new EclMeldung();
        lMeldung.meldungsIdent = pZugeordneteMeldungM.getMeldungsIdent();
        lMeldung.klasse = 0;
        eclDbM.getDbBundle().dbMeldungen.leseZuMeldungsIdent(lMeldung);
        lMeldung = eclDbM.getDbBundle().dbMeldungen.meldungenArray[0];

        eclDbM.getDbBundle().dbZutrittskarten.readZuMeldungsIdentGast(lMeldung.meldungsIdent);
        lMeldung.zutrittsIdent = eclDbM.getDbBundle().dbZutrittskarten.ergebnisPosition(0).zutrittsIdent;

        eclGastM.init();
        eclGastM.copyFrom(eclDbM.getDbBundle().dbMeldungen.meldungenArray[0]);

        /*Anrede*/
        eclAnredeListeListeM.fuelleListe(eclDbM.getDbBundle());
        eclGastM.setAnredeText("");
        if (eclGastM.getAnrede().compareTo("0") != 0) {
            for (int i = 0; i < eclAnredeListeListeM.getAnredeListeM().size(); i++) {
                if (eclAnredeListeListeM.getAnredeListeM().get(i).getAnredennr().equals(eclGastM.getAnrede())) {
                    eclGastM.setAnredeText(eclAnredeListeListeM.getAnredeListeM().get(i).getAnredentext());
                }
            }
        }

        /*Zugeordnete einlesen - Überprüfen ob "Gruppen-Gastanmeldung*/
        eclMeldungenMeldungenListeM.fuelleListe(eclDbM.getDbBundle(), lMeldung);
        eclGastM.setMeldungenMeldungenListe(eclMeldungenMeldungenListeM.getMeldungenMeldungenListeM());
        if (eclGastM.getMeldungenMeldungenListe().size() > 0) {
            tEintrittskarteQuittungUDetailSession.setGruppenausstellung(true);
        } else {
            tEintrittskarteQuittungUDetailSession.setGruppenausstellung(false);
        }

    }

    public void doDetailDruckeEintrittskarte() {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.EINTRITTSKARTE_DETAIL)) {
            return;
        }
        doDruckeEintrittskarte(1);
        tSessionVerwaltung.setzeEnde(KonstPortalView.EINTRITTSKARTE_DETAIL);
        return;
    }

    public void doDetailDruckeEintrittskarte2() {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.EINTRITTSKARTE_DETAIL)) {
            return;
        }
        doDruckeEintrittskarte(2);
        tSessionVerwaltung.setzeEnde(KonstPortalView.EINTRITTSKARTE_DETAIL);
        return;
    }

    public void doDetailMailEintrittskarte() {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.EINTRITTSKARTE_DETAIL)) {
            return;
        }
        doMailEintrittskarte(1);
        tSessionVerwaltung.setzeEnde(KonstPortalView.EINTRITTSKARTE_DETAIL);
        return;
    }

    public void doDetailMailEintrittskarte2() {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.EINTRITTSKARTE_DETAIL)) {
            return;
        }
        doMailEintrittskarte(2);
        tSessionVerwaltung.setzeEnde(KonstPortalView.EINTRITTSKARTE_DETAIL);
        return;
    }

    /***************************Übergreifende Funktionen******************************************************************/

    private void doDruckeEintrittskarte(int nr1oder2) {

        int dateinr = 0;
        if (nr1oder2 == 1) {
            dateinr = tWillenserklaerungSession.getEintrittskartePdfNr();
        } else {
            dateinr = tWillenserklaerungSession.getEintrittskartePdfNr2();
        }
        String dateiName = eclParamM.getClGlobalVar().lwPfadAllgemein + "\\"+eclParamM.getParamServer().praefixPfadVerzeichnisse
                + "ausdrucke\\"
                + eclParamM.getMandantPfad() + "\\zutrittsdokumentM" + eclParamM.getClGlobalVar().getMandantString()
                + Integer.toString(dateinr) + ".pdf";

        RpBrowserAnzeigen rpBrowserAnzeigen = new RpBrowserAnzeigen();
        rpBrowserAnzeigen.zeigen(dateiName);
        return;

    }

    private void doMailEintrittskarte(int nr1oder2) {
        /*Nun per Mail versenden*/
        int dateinr = 0;
        String mailAdresse = "";
        if (nr1oder2 == 1) {
            dateinr = tWillenserklaerungSession.getEintrittskartePdfNr();
            mailAdresse = tWillenserklaerungSession.getEintrittskarteEmail();
        } else {
            dateinr = tWillenserklaerungSession.getEintrittskartePdfNr2();
            if (tWillenserklaerungSession.getIntAusgewaehlteAktion() == KonstPortalAktion.ZWEI_EK_SELBST_FUER_ALLE) {
                mailAdresse = tWillenserklaerungSession.getEintrittskarteEmail();
            } else {
                mailAdresse = tWillenserklaerungSession.getEintrittskarteEmail2();

            }
        }

        String hMailText = eclPortalTexteM.holeText("219");
        String hBetreff = eclPortalTexteM.holeText("218");

        baMailm.sendenMitAnhang(mailAdresse, hBetreff, hMailText,
                eclParamM.getClGlobalVar().lwPfadAllgemein + "\\"+eclParamM.getParamServer().praefixPfadVerzeichnisse
                        + "ausdrucke\\" + eclParamM.getMandantPfad()
                        + "\\zutrittsdokumentM" + eclParamM.getClGlobalVar().getMandantString()
                        + Integer.toString(dateinr) + ".pdf");

        tSession.trageFehlerEin(CaFehler.afEKAktionaerEmailVerschickt);
    }

}
