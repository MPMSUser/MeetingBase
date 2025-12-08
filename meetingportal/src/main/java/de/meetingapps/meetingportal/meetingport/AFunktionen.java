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
package de.meetingapps.meetingportal.meetingport;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComBl.BlAbstimmungenWeisungen;
import de.meetingapps.meetingportal.meetComBl.BlWillenserklaerung;
import de.meetingapps.meetingportal.meetComBl.BlWillenserklaerungStatus;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEclM.EclAbstimmungM;
import de.meetingapps.meetingportal.meetComEclM.EclAbstimmungenListeM;
import de.meetingapps.meetingportal.meetComEclM.EclParamM;
import de.meetingapps.meetingportal.meetComEclM.EclPortalTexteM;
import de.meetingapps.meetingportal.meetComEclM.EclTeilnehmerLoginM;
import de.meetingapps.meetingportal.meetComEclM.EclWillenserklaerungStatusM;
import de.meetingapps.meetingportal.meetComEclM.EclZugeordneteMeldungListeM;
import de.meetingapps.meetingportal.meetComEclM.EclZugeordneteMeldungM;
import de.meetingapps.meetingportal.meetComEntities.EclAbstimmung;
import de.meetingapps.meetingportal.meetComEntities.EclAktienregister;
import de.meetingapps.meetingportal.meetComEntities.EclMeldung;
import de.meetingapps.meetingportal.meetComKonst.KonstSkIst;
import de.meetingapps.meetingportal.meetComKonst.KonstWeisungserfassungSicht;
import de.meetingapps.meetingportal.meetComKonst.KonstWillenserklaerung;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@RequestScoped
@Named
@Deprecated
public class AFunktionen {

    private int logDrucken = 3;

    
    @Inject
    private ADlgVariablen aDlgVariablen;
    @Inject
    private EclPortalTexteM eclTextePortalM;
    @Inject
    private EclAbstimmungenListeM eclAbstimmungenListeM;
    @Inject
    private EclZugeordneteMeldungListeM eclZugeordneteMeldungListeM;
    @Inject
    private EclZugeordneteMeldungM eclZugeordneteMeldungM;
    @Inject
    private EclTeilnehmerLoginM eclTeilnehmerLoginM;
    @Inject
    private EclParamM eclParamM;
    //	@Inject private XControllerAllgemein xControllerAllgemein;
    @Inject
    private XSessionVerwaltung xSessionVerwaltung;

    /*TODO #Konsolidierung: komplettes Portal / App durcharbeiten, dass nur die Funktionen auslösbar sind, die auch freigegeben sind 
     * (d.h.: nicht nur "Funktion nicht anbieten", sondern bei dennoch-Ausführung verhindern)*/

    private boolean zusammenfassenVonAnmeldungenMoeglich = true;
    private int alleWillenserklaerungen = 0;

    /**Gattung=1..5
     * pSicht=KonstWeisungserfassungSicht
     * pSkIst=KonstSkIst*/
    public void leseAbstimmungsliste(DbBundle lDbBundle, int gattung, int pSicht, int pSkIst) {

        BlAbstimmungenWeisungen blAbstimmungenWeisungenErfassen = new BlAbstimmungenWeisungen(true, lDbBundle);
        blAbstimmungenWeisungenErfassen.leseAgendaFuerSicht(pSicht);

        /*Agenda*/
        List<EclAbstimmungM> lAbstimmungenListe = new LinkedList<>();
        EclAbstimmungM lAbstimmungM = null;

        for (int i3 = 0; i3 < blAbstimmungenWeisungenErfassen.liefereAnzAgendaArray(gattung); i3++) {
            EclAbstimmung lAbstimmung = blAbstimmungenWeisungenErfassen.rcAgendaArray[gattung][i3];
            boolean uebertragen = false;
            switch (pSkIst) {
            case KonstSkIst.briefwahl:
                if (lAbstimmung.aktivBeiBriefwahl == 1) {
                    uebertragen = true;
                }
                break;
            case KonstSkIst.dauervollmacht:
                if (lAbstimmung.aktivBeiKIAVDauer == 1) {
                    uebertragen = true;
                }
                break;
            case KonstSkIst.kiav:
                if (lAbstimmung.aktivBeiKIAVDauer == 1) {
                    uebertragen = true;
                }
                break;
            case KonstSkIst.organisatorisch:
                if (lAbstimmung.aktivBeiKIAVDauer == 1) {
                    uebertragen = true;
                }
                break;
            case KonstSkIst.srv:
                if (lAbstimmung.aktivBeiSRV == 1) {
                    uebertragen = true;
                }
                break;
            }
            if (uebertragen) {
                lAbstimmungM = new EclAbstimmungM();
                lAbstimmungM.copyFrom(lAbstimmung);
                lAbstimmungenListe.add(lAbstimmungM);

            }
        }

        eclAbstimmungenListeM.setAbstimmungenListeM(lAbstimmungenListe);

        /*Gegenanträge*/
        List<EclAbstimmungM> lGegenantraegeListe = new LinkedList<>();

        boolean gegenantraegeSeparat = false;
        switch (pSicht) {
        case KonstWeisungserfassungSicht.portalWeisungserfassung:
            gegenantraegeSeparat = lDbBundle.param.paramAbstimmungParameter.weisungenGegenantraegePortalSeparat;
            break;
        case KonstWeisungserfassungSicht.externWeisungsreports:
            gegenantraegeSeparat = lDbBundle.param.paramAbstimmungParameter.weisungenGegenantraegeInternSeparat;
            break;
        case KonstWeisungserfassungSicht.interneWeisungserfassung:
            gegenantraegeSeparat = lDbBundle.param.paramAbstimmungParameter.weisungenGegenantraegeInternSeparat;
            break;
        case KonstWeisungserfassungSicht.internWeisungsreports:
            gegenantraegeSeparat = lDbBundle.param.paramAbstimmungParameter.weisungenGegenantraegeInternSeparat;
            break;
        }

        if (gegenantraegeSeparat) {
            for (int i3 = 0; i3 < blAbstimmungenWeisungenErfassen.liefereAnzGegenantraegeArray(gattung); i3++) {
                EclAbstimmung lAbstimmung = blAbstimmungenWeisungenErfassen.rcGegenantraegeArray[gattung][i3];
                boolean uebertragen = false;
                switch (pSkIst) {
                case KonstSkIst.briefwahl:
                    if (lAbstimmung.aktivBeiBriefwahl == 1) {
                        uebertragen = true;
                    }
                    break;
                case KonstSkIst.dauervollmacht:
                    if (lAbstimmung.aktivBeiKIAVDauer == 1) {
                        uebertragen = true;
                    }
                    break;
                case KonstSkIst.kiav:
                    if (lAbstimmung.aktivBeiKIAVDauer == 1) {
                        uebertragen = true;
                    }
                    break;
                case KonstSkIst.organisatorisch:
                    if (lAbstimmung.aktivBeiKIAVDauer == 1) {
                        uebertragen = true;
                    }
                    break;
                case KonstSkIst.srv:
                    if (lAbstimmung.aktivBeiSRV == 1) {
                        uebertragen = true;
                    }
                    break;
                }
                if (uebertragen) {
                    lAbstimmungM = new EclAbstimmungM();
                    lAbstimmungM.copyFrom(lAbstimmung);
                    lGegenantraegeListe.add(lAbstimmungM);

                }

            }
            eclAbstimmungenListeM.setGegenantraegeListeM(lGegenantraegeListe);
        }
    }

    /**Für Neuanmeldung: prüft, ob nachwievor keine Anmeldungen für den eingeloggten Aktionär vorhanden sind.
     * Nur für Namensaktien implementiert, und auch nur für Namensaktien erforderlich.
     * 
     * Wird immer nochmal aufgerufen, bevor tatsächlich eine Anmeldung durchgeführt wird (in den Routinen selbst),
     * um sicherzustellen dass zwischen "Ersteingabe" und tatsächlichem Anmelden nicht zwischendurch von
     * einem anderen Arbeitsplatz aus mittlerweile eine Anmeldung erfolgt ist.
     * @return
     */
    public boolean reCheckKeineAktienanmeldungen(DbBundle lDbBundle) {
        BlWillenserklaerungStatus lWillenserklaerungStatus = new BlWillenserklaerungStatus(lDbBundle);
        lWillenserklaerungStatus
                .leseMeldungenZuAktienregisterIdent(eclTeilnehmerLoginM.getAnmeldeIdentAktienregister());
        if (CaBug.pruefeLog(logDrucken, 10)) {
            CaBug.druckeInfo(
                    "AFunktionen.reCheckKeineAktienanmeldungen eclTeilnehmerLoginM.getAnmeldeIdentAktienregister()="
                            + eclTeilnehmerLoginM.getAnmeldeIdentAktienregister());
        }

        if (lWillenserklaerungStatus.zugeordneteMeldungenEigeneAktienArray == null
                || lWillenserklaerungStatus.zugeordneteMeldungenEigeneAktienArray.length == 0) {
            if (CaBug.pruefeLog(logDrucken, 10)) {
                CaBug.druckeInfo("AFunktionen.reCheckKeineAktienanmeldungen return true");
            }
            return true;
        }

        if (CaBug.pruefeLog(logDrucken, 10)) {
            CaBug.druckeInfo("AFunktionen.reCheckKeineAktienanmeldungen return false");
        }
        return false;
    }

    /**Für Neuanmeldung von Gästen: prüft, ob nachwievor keine Gastanmeldungen für den eingeloggten Aktionär vorhanden sind.
     * Nur für Namensaktien implementiert, und auch nur für Namensaktien erforderlich.
     * 
     * Wird immer nochmal aufgerufen, bevor tatsächlich eine Anmeldung durchgeführt wird (in den Routinen selbst),
     * um sicherzustellen dass zwischen "Ersteingabe" und tatsächlichem Anmelden nicht zwischendurch von
     * einem anderen Arbeitsplatz aus mittlerweile eine Anmeldung erfolgt ist.
     * @return
     */
    public boolean reCheckKeineGastanmeldungen(DbBundle lDbBundle, EclAktienregister pAktienregisterEintrag) {
        BlWillenserklaerungStatus lWillenserklaerungStatus = new BlWillenserklaerungStatus(lDbBundle);
        lWillenserklaerungStatus.leseMeldungenZuAktienregisterIdent(pAktienregisterEintrag.aktienregisterIdent);
        if (lWillenserklaerungStatus.zugeordneteMeldungenEigeneAktienArray == null
                || lWillenserklaerungStatus.zugeordneteMeldungenEigeneAktienArray.length == 0) {
            return true;
        }

        lWillenserklaerungStatus
                .ergaenzeZugeordneteMeldungenUmWillenserklaerungen(eclTeilnehmerLoginM.getAnmeldeIdentPersonenNatJur());
        if (lWillenserklaerungStatus.gastKartenGemeldetEigeneAktien == 0) {
            return true;
        }
        return false;
    }

    /**Checken, ob für eine Meldungsident keine höhere Willenserklärung vergeben wurde, als übergeben
     * 
     * Verwendung: wurde die gerade Bearbeitete meldung anderweitig schon verändert?
     * @return
     */
    public boolean reCheckKeineNeueWillenserklaerungen(DbBundle lDbBundle, int meldeIdent,
            int bisherHoechsteWillenserklaerungIdent) {
        int anz;
        EclMeldung lMeldung = new EclMeldung();
        lMeldung.meldungsIdent = meldeIdent;
        anz = lDbBundle.dbWillenserklaerung.ermittleHoechsteWillenserklaerungIdentZuMeldung(lMeldung);
        if (anz == bisherHoechsteWillenserklaerungIdent) {
            return true;
        }

        return false;
    }

    /**Nur für Namensaktien: 
     * > Überprüfen, ob aStatus.xhtml (bereits Anmeldungen für diesen Aktienregistereintrag vorhanden)
     * 	oder aAnmelden (noch keine Anmeldung vorhanden) aufgerufen werden soll
     * > ggf. Einlesen aller Daten, die für aStatus.xhtml erforderlich sind
     * 
     * Eingabeparameter:
     * 		EclTeilnehmerLoginM.anmeldeKennungArt
     * 		EclTeilnehmerLoginM.anmeldeIdentAktienregister
     * 
     * Anschließend gefüllt / upgedated:
     * 		EclTeilnehmerLogin.anmeldeIdentPersonenNatJur
     * 		ADlgVariablen.ausgewaehlteHauptAktion ("1" oder "2")
     * 		EclZugeordneteMeldungListeM
     * 
     * */
    public String waehleAusgangsmaske(DbBundle lDbBundle) {
        return this.waehleAusgangsmaske(lDbBundle, false);
    }

    public String waehleAusgangsmaske(DbBundle lDbBundle, boolean mitStorno) {

        int erg;

        BlWillenserklaerungStatus lWillenserklaerungStatus = new BlWillenserklaerungStatus(lDbBundle);
        lWillenserklaerungStatus.piAlleWillenserklaerungen = alleWillenserklaerungen;

        switch (eclTeilnehmerLoginM.getAnmeldeKennungArt()) {
        case 1: {/*Login ist über Aktienregisternummer erfolgt. In diesem Fall "Basis-Meldungen über
                 	leseMeldungenZuAktienregisterIdent ermitteln, anschließend personNatJur aus (Aktionärs-)
                 	Anmeldungen bestimmen*/

            /*Zuerst jedoch prüfen, ob überhaupt noch Bestand vorhanden*/
            /*TODO #App: dieses Verfahren funktioniert nicht, wenn "die innovativen Funktionen des Portals" angeboten werden!
             * Dann nochmal überarbeiten!
             */

            if (eclTeilnehmerLoginM.getStimmen() == 0 && xSessionVerwaltung.getStartPruefen() == 1) {
                return "aStatus0Bestand";
            }

            eclTeilnehmerLoginM.setAnmeldeIdentPersonenNatJur(0);
            lWillenserklaerungStatus
                    .leseMeldungenZuAktienregisterIdent(eclTeilnehmerLoginM.getAnmeldeIdentAktienregister());

            /*Nun noch personNatJur bestimmen*/
            if (lWillenserklaerungStatus.zugeordneteMeldungenEigeneAktienArray == null
                    || lWillenserklaerungStatus.zugeordneteMeldungenEigeneAktienArray.length == 0) {
                /*Dann noch keine Anmeldungen vorhanden! => Erstanmeldung "Aktienregister"*/
                aDlgVariablen.setAusgewaehlteHauptAktion("1");
                return "aAnmelden";
            } else {
                /*TODO #9 bei Aktienregisteranmeldung funktioniert die Zuordnung von Gästekarten und Bevollmächtigten möglicherweise nicht! Denn die erfolgt über PersonNatJur, die in der 
                 * Meldung gespeichert wird. Bei der kompletten Meldungsstornierung verliert man diese
                 * PersonNatJur aber - bzw. deren Zuordnung zum Aktienregister. Außerdem ist eine Gastkartenausstellung oder der Erhalt einer Vollmacht nur möglich, wenn eine
                 * eigene Anmeldung bereits existiert (sonst ist PersonnatJur=0!)
                 */

                /*Entfällt, da nun direkt in BlWillenserklaerung ermittelt!
                for (i=0;i<lWillenserklaerungStatus.zugeordneteMeldungenEigeneAktienArray.length;i++){
                	if (lWillenserklaerungStatus.zugeordneteMeldungenEigeneAktienArray[i].klasse==1){
                		eclTeilnehmerLoginM.setAnmeldeIdentPersonenNatJur(lWillenserklaerungStatus.zugeordneteMeldungenEigeneAktienArray[i].personNatJurIdent);
                	}
                }
                */
                /*Neu :-):*/
                eclTeilnehmerLoginM
                        .setAnmeldeIdentPersonenNatJur(lWillenserklaerungStatus.aktienregisterPersonNatJurIdent);
                /*Neu Ende*/

                /**Nun überprüfen, ob zwei Anmeldungen zu einer Anmeldung zusammengefaßt werden können*/
                BlWillenserklaerung blWillenserklaerung = new BlWillenserklaerung();
                blWillenserklaerung.pErteiltAufWeg = aDlgVariablen.getEingabeQuelle();
                blWillenserklaerung.pErteiltZeitpunkt = aDlgVariablen.getErteiltZeitpunkt();
                blWillenserklaerung.pEclAktienregisterEintrag = new EclAktienregister();
                blWillenserklaerung.pEclAktienregisterEintrag.aktienregisterIdent = eclTeilnehmerLoginM
                        .getAnmeldeIdentAktienregister();
                blWillenserklaerung.anmeldungenAusAktienregisterStornieren_pruefe(lDbBundle);
                if (blWillenserklaerung.rcIstZulaessig && +blWillenserklaerung.rcAnzahlMeldungen == 2
                        && zusammenfassenVonAnmeldungenMoeglich == true) {
                    /*Zwei Anmeldungen existieren, diese können storniert werden => Stornieren und eine neue Anmeldung erzeugen*/
                    /*******Stornieren*******/
                    blWillenserklaerung = new BlWillenserklaerung();
                    blWillenserklaerung.pErteiltAufWeg = aDlgVariablen.getEingabeQuelle();
                    blWillenserklaerung.pErteiltZeitpunkt = aDlgVariablen.getErteiltZeitpunkt();
                    blWillenserklaerung.pEclAktienregisterEintrag = new EclAktienregister();
                    blWillenserklaerung.pEclAktienregisterEintrag.aktienregisterIdent = eclTeilnehmerLoginM
                            .getAnmeldeIdentAktienregister();
                    blWillenserklaerung.anmeldungenAusAktienregisterStornieren(lDbBundle);

                    /******Neu Anmelden mit EINEM Aktienbestand*****/
                    blWillenserklaerung = new BlWillenserklaerung();
                    blWillenserklaerung.pErteiltAufWeg = aDlgVariablen.getEingabeQuelle();
                    blWillenserklaerung.pErteiltZeitpunkt = aDlgVariablen.getErteiltZeitpunkt();
                    /*Aktienregister füllen*/
                    EclAktienregister aktienregisterEintrag = new EclAktienregister();
                    aktienregisterEintrag.aktionaersnummer = eclTeilnehmerLoginM.getAnmeldeAktionaersnummer();
                    erg = lDbBundle.dbAktienregister.leseZuAktienregisterEintrag(aktienregisterEintrag);
                    if (erg <= 0) {/*Aktienregistereintrag nicht mehr vorhanden*/
                        /*Dann schwerwiegender Fehler - denn dann hätte das Programm schon vorher rausfliegen müssen ....*/
                        aDlgVariablen.setFehlerMeldung(
                                eclTextePortalM.getFehlertext(CaFehler.afAktienregisterEintragNichtMehrVorhanden));
                        aDlgVariablen.setFehlerNr(CaFehler.afAktienregisterEintragNichtMehrVorhanden);
                        return "";
                    }

                    aktienregisterEintrag = lDbBundle.dbAktienregister.ergebnisPosition(0);
                    blWillenserklaerung.pEclAktienregisterEintrag = aktienregisterEintrag;

                    /*Restliche Parameter füllen*/
                    blWillenserklaerung.pAktienAnmelden = -1; /*Alle Aktien anmelden*/
                    blWillenserklaerung.pAnmeldungFix = false; /*Nicht "Fix" anmelden*/
                    blWillenserklaerung.pAnzahlAnmeldungen = 1;
                    blWillenserklaerung.pWillenserklaerungGeberIdent = -1; /*Aktionär gibt in diesem Fall*/
                    blWillenserklaerung.pPersonNatJurFuerAnmeldungVerwenden = eclTeilnehmerLoginM
                            .getAnmeldeIdentPersonenNatJur();

                    blWillenserklaerung.anmeldungAusAktienregister(lDbBundle);

                    if (blWillenserklaerung.rcIstZulaessig == false) {
                        aDlgVariablen.setFehlerMeldung(
                                eclTextePortalM.getFehlertext(blWillenserklaerung.rcGrundFuerUnzulaessig));
                        aDlgVariablen.setFehlerNr(blWillenserklaerung.rcGrundFuerUnzulaessig);
                        return "";

                    }

                    /*Willenserklärungsbasis neu einlesen*/
                    lWillenserklaerungStatus
                            .leseMeldungenZuAktienregisterIdent(eclTeilnehmerLoginM.getAnmeldeIdentAktienregister());

                }

            }
            break;
        }
        case 3: {/*Login ist personNatJur*/
            if (eclTeilnehmerLoginM.getInstiIdent() > 0) {
                lWillenserklaerungStatus.leseMeldungenEigeneAktienZuInstiIdent(eclTeilnehmerLoginM.getInstiIdent());
            } else {
                lWillenserklaerungStatus
                        .leseMeldungenEigeneAktienZuPersonNatJur(eclTeilnehmerLoginM.getAnmeldeIdentPersonenNatJur());
            }
            break;
        }

        }

        lWillenserklaerungStatus
                .leseMeldungenEigeneGastkartenZuPersonNatJur(eclTeilnehmerLoginM.getAnmeldeIdentPersonenNatJur());
        lWillenserklaerungStatus
                .leseMeldungenBevollmaechtigtZuPersonNatJur(eclTeilnehmerLoginM.getAnmeldeIdentPersonenNatJur());
        lWillenserklaerungStatus
                .ergaenzeZugeordneteMeldungenUmWillenserklaerungen(eclTeilnehmerLoginM.getAnmeldeIdentPersonenNatJur());

        /*Liste mit zugeordneten Meldungen / Willenserklärungen füllen*/
        eclZugeordneteMeldungListeM.zugeordneteMeldungenEigeneAktienCopyFrom(
                lWillenserklaerungStatus.zugeordneteMeldungenEigeneAktienArray, mitStorno);
        eclZugeordneteMeldungListeM.setGastKartenGemeldet(lWillenserklaerungStatus.gastKartenGemeldetEigeneAktien);
        eclZugeordneteMeldungListeM.zugeordneteMeldungenEigeneGastkartenCopyFrom(
                lWillenserklaerungStatus.zugeordneteMeldungenEigeneGastkartenArray, mitStorno);
        eclZugeordneteMeldungListeM.zugeordneteMeldungenBevollmaechtigtCopyFrom(
                lWillenserklaerungStatus.zugeordneteMeldungenBevollmaechtigtArray, mitStorno);
        eclZugeordneteMeldungListeM.setBriefwahlVorhanden(lWillenserklaerungStatus.briefwahlVorhanden);
        eclZugeordneteMeldungListeM.setSrvVorhanden(lWillenserklaerungStatus.srvVorhanden);
        aDlgVariablen.setAusgewaehlteHauptAktion("2");
        aDlgVariablen.setGastKarte(false);

        /**Nun Möglichkeiten für virtuelle HV in Abhängigkeit vom Login setzen*/
        aDlgVariablen.setMitteilungenAngebotenLogin(1);
        aDlgVariablen.setStreamAngebotenLogin(1);
        aDlgVariablen.setFragenAngebotenLogin(1);
        aDlgVariablen.setEinstellungenAngebotenLogin(1);
        aDlgVariablen.setTeilnehmerverzAngebotenLogin(1);
        aDlgVariablen.setAbstimmungsergAngebotenLogin(1);

        if (eclTeilnehmerLoginM.getAnmeldeKennungArt() == 3) {
            aDlgVariablen.setEinstellungenAngebotenLogin(0);
            if (eclTeilnehmerLoginM.getInstiIdent() == -1) {
                aDlgVariablen.setMitteilungenAngebotenLogin(0);
                aDlgVariablen.setFragenAngebotenLogin(0);
                aDlgVariablen.setTeilnehmerverzAngebotenLogin(0);
                aDlgVariablen.setAbstimmungsergAngebotenLogin(0);
            }
        }

        CaBug.druckeLog("AFunktionen.WaehleAusgangsmaske eclParamM.getParam().paramPortal.varianteDialogablauf="
                + eclParamM.getParam().paramPortal.varianteDialogablauf, logDrucken, 10);
        String auswahlMaske = aDlgVariablen.getAuswahlMaske();
        if (eclParamM.getParam().paramPortal.varianteDialogablauf == 1) {
            /**Bei Dialog-Variante 1 (ku178) - Ausgangsmaske ist immer aAuswahl1*/
            if (auswahlMaske.isEmpty()) {
                return "aAuswahl1";
            } else {
                aDlgVariablen.setAuswahlMaske("");
                return auswahlMaske;
            }
        }

        if (auswahlMaske.isEmpty()) {
            return "aStatus";
        } else {
            aDlgVariablen.setAuswahlMaske("");
            return auswahlMaske;
        }

        //		if (lWillenserklaerungStatus.zugeordneteMeldungenEigeneAktienArray.length!=0){
        //
        //			aDlgVariablen.setAusgewaehlteHauptAktion("2");
        //			aDlgVariablen.setGastKarte(false);
        //			return "aStatus";
        //
        //		}
        //		else{
        //			lDbBundle.closeAll();
        //			aDlgVariablen.setAusgewaehlteHauptAktion("1");
        //			return "aAnmelden";
        //		}

    }

    /**Belegt die aktuellen Session-Parameter mit der ersten Meldung*/
    public void belegeMitErsterMeldung(DbBundle pDbBundle) {
        waehleAusgangsmaske(pDbBundle, false);
        eclZugeordneteMeldungM.copyFromM(eclZugeordneteMeldungListeM.getZugeordneteMeldungenEigeneAktienListeM().get(0),
                false);

    }

    /**Voraussetzung: eclZugeordneteMeldungM ist bereits belegt.
     * Dann wird die letzte SRV Vollmacht zurückgeliefert
     */
    public EclWillenserklaerungStatusM liefereErsteSRV() {
        List<EclWillenserklaerungStatusM> zugeordneteWillenserklaerungenListM = eclZugeordneteMeldungM
                .getZugeordneteWillenserklaerungenListM();
        EclWillenserklaerungStatusM rcWillenserklaerungStatusM = null;
        for (int i = 0; i < zugeordneteWillenserklaerungenListM.size(); i++) {
            EclWillenserklaerungStatusM lWillenserklaerungStatusM = zugeordneteWillenserklaerungenListM.get(i);
            if (lWillenserklaerungStatusM.getWillenserklaerung() == KonstWillenserklaerung.vollmachtUndWeisungAnSRV
                    || lWillenserklaerungStatusM.getWillenserklaerung() == KonstWillenserklaerung.aendernWeisungAnSRV) {
                rcWillenserklaerungStatusM = lWillenserklaerungStatusM;
            }
        }
        return rcWillenserklaerungStatusM;
    }

    //if (!aFunktionen.pruefeStart("aLogin")){return "aDlgFehler";}
    //aFunktionen.setzeEnde("aRegistrierung", true, false);

    public String waehleLogout() {
        String logoutZiel = eclParamM.getParam().paramPortal.logoutZiel;
        if (logoutZiel.isEmpty()) {
            return setzeEnde("aLogin", true, true);
        }

        /**Hier Session Invalidaten*/

        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        try {
            externalContext.redirect(logoutZiel);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public boolean pruefeStart(String pAufrufMaske) {
        return xSessionVerwaltung.pruefeStart(pAufrufMaske, eclTeilnehmerLoginM.getAnmeldeKennung());
    }

    public boolean pruefeStart(String[] pAufrufMaske) {
        return xSessionVerwaltung.pruefeStart(pAufrufMaske, eclTeilnehmerLoginM.getAnmeldeKennung());
    }

    public String setzeEnde() {
        return xSessionVerwaltung.setzeEnde();
    }

    public String setzeEnde(String pNaechsteMaske, boolean pClearFehlermeldung, boolean pClearDlgVariablen) {
        return xSessionVerwaltung.setzeEnde(pNaechsteMaske, pClearFehlermeldung, pClearDlgVariablen,
                eclTeilnehmerLoginM.getAnmeldeKennung());
    }

    /**Prüfen, ob die abgegebene Willenserklärung möglich ist, oder "Zeit bereits abgelaufen
     * Falls "nicht Internet", dann immer möglich
     * 
     * Berücksichtigung von pIstErstanmeldung (Anmeldefrist ...)
     * 
     * Voraussetzung: pDbBundle muß offen sein - damit Parameter ggf. neu eingelesen sind
     * */
    public boolean pruefeOBWillenserklaerungZulaessig(DbBundle pDbBundle, int pWillenserklaerung, String pHauptAktion) {

        //		System.out.println("Hauptaktion="+pHauptAktion);
        //		System.out.println("Quelle="+aDlgVariablen.getEingabeQuelle());
        //		System.out.println("Param="+DbParameter.pLfdHVPortalErstanmeldungIstMoeglich);

        if (pHauptAktion.compareTo("1") == 0) {
            if (eclParamM.getParam().paramPortal.lfdHVPortalErstanmeldungIstMoeglich == 0
                    && (aDlgVariablen.getEingabeQuelle() == 21 || aDlgVariablen.getEingabeQuelle() == 22)) {
                return false;
            }
        }

        if (aDlgVariablen.getEingabeQuelle() != 21 && aDlgVariablen.getEingabeQuelle() != 22) {
            return true;
        }

        if (pWillenserklaerung == KonstWillenserklaerung.vollmachtUndWeisungAnSRV) {
            if (eclParamM.getParam().paramPortal.lfdHVPortalSRVIstMoeglich == 1) {
                return true;
            } else {
                return false;
            }
        }
        if (pWillenserklaerung == KonstWillenserklaerung.briefwahl) {
            if (eclParamM.getParam().paramPortal.lfdHVPortalBriefwahlIstMoeglich == 1) {
                return true;
            } else {
                return false;
            }
        }
        if (pWillenserklaerung == KonstWillenserklaerung.vollmachtUndWeisungAnKIAV) {
            if (eclParamM.getParam().paramPortal.lfdHVPortalKIAVIstMoeglich == 1) {
                return true;
            } else {
                return false;
            }
        }
        if (pWillenserklaerung == KonstWillenserklaerung.neueZutrittsIdentZuMeldung
                || pWillenserklaerung == KonstWillenserklaerung.neueZutrittsIdentZuMeldung_VollmachtAnDritte) {
            if (eclParamM.getParam().paramPortal.lfdHVPortalEKIstMoeglich == 1) {
                return true;
            } else {
                return false;
            }
        }
        if (pWillenserklaerung == KonstWillenserklaerung.vollmachtAnDritte) {
            if (eclParamM.getParam().paramPortal.lfdHVPortalVollmachtDritteIstMoeglich == 1) {
                return true;
            } else {
                return false;
            }
        }

        return true;

    }

    public String getAktuelleMaske() {
        return xSessionVerwaltung.getAktuelleMaske();
    }

    public int getStartPruefen() {
        return xSessionVerwaltung.getStartPruefen();
    }

    public void setStartPruefen(int startPruefen) {
        xSessionVerwaltung.setStartPruefen(startPruefen);
    }

    public boolean isZusammenfassenVonAnmeldungenMoeglich() {
        return zusammenfassenVonAnmeldungenMoeglich;
    }

    public void setZusammenfassenVonAnmeldungenMoeglich(boolean zusammenfassenVonAnmeldungenMoeglich) {
        this.zusammenfassenVonAnmeldungenMoeglich = zusammenfassenVonAnmeldungenMoeglich;
    }

    public int getAlleWillenserklaerungen() {
        return alleWillenserklaerungen;
    }

    public void setAlleWillenserklaerungen(int alleWillenserklaerungen) {
        this.alleWillenserklaerungen = alleWillenserklaerungen;
    }
}
