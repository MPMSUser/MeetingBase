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

import java.util.LinkedList;
import java.util.List;

import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEclM.EclDbM;
import de.meetingapps.meetingportal.meetComEclM.EclKIAVListeM;
import de.meetingapps.meetingportal.meetComEclM.EclKIAVM;
import de.meetingapps.meetingportal.meetComEclM.EclPortalTexteM;
import de.meetingapps.meetingportal.meetComEclM.EclTeilnehmerLoginM;
import de.meetingapps.meetingportal.meetComEclM.EclZugeordneteMeldungListeM;
import de.meetingapps.meetingportal.meetComEclM.EclZugeordneteMeldungM;
import de.meetingapps.meetingportal.meetComEntities.EclAktienregister;
import de.meetingapps.meetingportal.meetComKonst.KonstSkIst;
import de.meetingapps.meetingportal.meetComKonst.KonstWeisungserfassungSicht;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@RequestScoped
@Named
@Deprecated
public class AControllerAnmelden {

    @Inject
    private ADlgVariablen aDlgVariablen;
    @Inject
    private EclPortalTexteM eclTextePortalM;
    @Inject
    private EclDbM eclDbM;

    @Inject
    private EclTeilnehmerLoginM eclTeilnehmerLoginM;
    @Inject
    private EclKIAVListeM eclKIAVListeM;
    @Inject
    private EclZugeordneteMeldungM eclZugeordneteMeldungM;
    @Inject
    private EclZugeordneteMeldungListeM eclZugeordneteMeldungListeM;

    @Inject
    private AFunktionen aFunktionen;

    @Inject
    AControllerKIAVAuswahl aControllerKIAVAuswahl;

    /***********Allgemeine Funktionen*************************************/

    /**Überprüfen, ob Personengemeinschaft vorliegt (Kennzeichen gesetzt; >=2 Aktien).
     * Wenn ja, dann Vollmachtsfelder "nach bestem Wissen und Gewissen"
     * mit erstem und zweitem Namen vorbelegen.
     *
     * Eingabeparameter:
     * 		EclTeilnehmerLoginM.anmeldeAktionaersnummer
     *
     * Ausgabeparameter:
     * 		ADlgVariablen.fehlerMeldung/.fehlerNr
     * 		ADlgVariablen.vollmachtName/.vollmachtVorname/.vollmachtOrt
     * 		ADlgVariablen.vollmachtName2/.vollmachtVorname2/.vollmachtOrt2
     *
     *
     * Voraussetzung: dbBundle geöffnet
     *
     */
    public boolean checkPersonengemeinschaft() {

        EclAktienregister aktienregisterEintrag = null;
        aDlgVariablen.clearFehlerMeldung();

        aktienregisterEintrag = new EclAktienregister();
        aktienregisterEintrag.aktionaersnummer = eclTeilnehmerLoginM.getAnmeldeAktionaersnummer();
        eclDbM.getDbBundle().dbAktienregister.leseZuAktienregisterEintrag(aktienregisterEintrag);
        aktienregisterEintrag = eclDbM.getDbBundle().dbAktienregister.ergebnisPosition(0);

        if (aktienregisterEintrag.pruefePersonengemeinschaft() == false) {
            aDlgVariablen.setFehlerMeldung(eclTextePortalM.getFehlertext(CaFehler.afKeinePersonengemeinschaft));
            aDlgVariablen.setFehlerNr(CaFehler.afKeinePersonengemeinschaft);
            return false;
        }

        if (aktienregisterEintrag.stueckAktien <= 1) {
            aDlgVariablen.setFehlerMeldung(eclTextePortalM.getFehlertext(CaFehler.afMindestens2AktienErforderlich));
            aDlgVariablen.setFehlerNr(CaFehler.afMindestens2AktienErforderlich);
            return false;
        }

        aDlgVariablen.setVollmachtName(aktienregisterEintrag.nachname);
        aDlgVariablen.setVollmachtName2(aktienregisterEintrag.nachname);
        aDlgVariablen.setVollmachtVorname(aktienregisterEintrag.liefereErsterVorname());
        aDlgVariablen.setVollmachtVorname2(aktienregisterEintrag.liefereZweiterVorname());
        aDlgVariablen.setVollmachtOrt(aktienregisterEintrag.ort);
        aDlgVariablen.setVollmachtOrt2(aktienregisterEintrag.ort);

        return true;
    }

    /** Voraussetzung: dbBundle geöffnet*/
    public boolean check2EK() {

        EclAktienregister aktienregisterEintrag = null;
        aDlgVariablen.clearFehlerMeldung();

        aktienregisterEintrag = new EclAktienregister();
        aktienregisterEintrag.aktionaersnummer = eclTeilnehmerLoginM.getAnmeldeAktionaersnummer();
        eclDbM.getDbBundle().dbAktienregister.leseZuAktienregisterEintrag(aktienregisterEintrag);
        aktienregisterEintrag = eclDbM.getDbBundle().dbAktienregister.ergebnisPosition(0);

        if (aktienregisterEintrag.stueckAktien <= 1) {
            aDlgVariablen.setFehlerMeldung(eclTextePortalM.getFehlertext(CaFehler.afMindestens2AktienErforderlich));
            aDlgVariablen.setFehlerNr(CaFehler.afMindestens2AktienErforderlich);
            return false;
        }

        return true;
    }

    /**Einlesen aller Sammelkarten einer bestimmten Art.
     * Wird u.a. auch verwendet zum Füllen der KIAV-Listen auf Client-Seite.
     *
     * Abhängig von aDlgVariablen.ausgewaehlteAktion Einlesen von SRV, Briefwahl, KIAV, Dauervollmachten, oder Organisatorische
     *
     * Rückgabeparameter:
     * 		EclKIAVListeM
     */
    public boolean leseKIAVListe(DbBundle lDbBundle) {
        int i;

        /*TODO Konsolidieren: Portal KIAV zeigt derzeit Sammelkarten ALLER Gattungen an ... nötig wg. Anmeldestellenfunktionalität betterAnmelde*/

        if (aDlgVariablen.getAusgewaehlteAktion().compareTo(
                "4") == 0) { /*SRV - einlesen immer alle, unabhändig vom Weg. Derzeit nur verwendet von Anmeldestellenclient*/
            lDbBundle.dbMeldungen.leseSammelkarteSRV(-1/*eclTeilnehmerLoginM.getGattung()*/);
        }

        if (aDlgVariablen.getAusgewaehlteAktion().compareTo(
                "5") == 0) { /*Briefwahl - einlesen immer alle, unabhändig vom Weg. Derzeit nur verwendet von Anmeldestellenclient*/
            lDbBundle.dbMeldungen.leseSammelkartenBriefwahl(-1/*eclTeilnehmerLoginM.getGattung()*/);
        }

        if (aDlgVariablen.getAusgewaehlteAktion().compareTo("6") == 0
                || aDlgVariablen.getAusgewaehlteAktion().compareTo("7") == 0
                || aDlgVariablen.getAusgewaehlteAktion().compareTo("8") == 0
                || aDlgVariablen.getAusgewaehlteAktion().compareTo("9") == 0) { /*KIAV*/

            if (aDlgVariablen.getEingabeQuelle() >= 21 && aDlgVariablen.getEingabeQuelle() <= 29) {
                lDbBundle.dbMeldungen.leseSammelkarteKIAVInternet(-1/*eclTeilnehmerLoginM.getGattung()*/);
            } else {
                lDbBundle.dbMeldungen.leseSammelkarteKIAVPapier(-1/*eclTeilnehmerLoginM.getGattung()*/);
            }
        }

        if (aDlgVariablen.getAusgewaehlteAktion().compareTo("31") == 0
                || aDlgVariablen.getAusgewaehlteAktion().compareTo("32") == 0
                || aDlgVariablen.getAusgewaehlteAktion().compareTo("33") == 0
                || aDlgVariablen.getAusgewaehlteAktion().compareTo("34") == 0) { /*Dauervollmacht*/

            if (aDlgVariablen.getEingabeQuelle() >= 21 && aDlgVariablen.getEingabeQuelle() <= 29) {
                lDbBundle.dbMeldungen.leseSammelkarteDauervollmachtInternet(-1/*eclTeilnehmerLoginM.getGattung()*/);
            } else {
                lDbBundle.dbMeldungen.leseSammelkarteDauervollmachtPapier(-1/*eclTeilnehmerLoginM.getGattung()*/);
            }
        }

        if (aDlgVariablen.getAusgewaehlteAktion().compareTo("35") == 0
                || aDlgVariablen.getAusgewaehlteAktion().compareTo("36") == 0
                || aDlgVariablen.getAusgewaehlteAktion().compareTo("37") == 0
                || aDlgVariablen.getAusgewaehlteAktion().compareTo("38") == 0) { /*Organisatorisch*/

            if (aDlgVariablen.getEingabeQuelle() >= 21 && aDlgVariablen.getEingabeQuelle() <= 29) {
                lDbBundle.dbMeldungen.leseSammelkarteOrgaInternet(-1/*eclTeilnehmerLoginM.getGattung()*/);
            } else {
                lDbBundle.dbMeldungen.leseSammelkarteOrgaPapier(-1/*eclTeilnehmerLoginM.getGattung()*/);
            }
        }

        List<EclKIAVM> lkiavmListe = new LinkedList<>();

        for (i = 0; i < lDbBundle.dbMeldungen.meldungenArray.length; i++) {
            EclKIAVM lkiavm = new EclKIAVM();
            lkiavm.copyFrom(lDbBundle.dbMeldungen.meldungenArray[i]);
            lkiavmListe.add(lkiavm);

            lDbBundle.dbAbstimmungsVorschlag.leseZuSammelIdent(lDbBundle.dbMeldungen.meldungenArray[i].meldungsIdent);

            if (lDbBundle.dbAbstimmungsVorschlag.abstimmungsVorschlagArray.length > 0) { /*Abstimmungsvorschlag vorhanden*/
                lkiavm.setAbstimmungsVorschlagIdent(
                        lDbBundle.dbAbstimmungsVorschlag.abstimmungsVorschlagArray[0].abstimmungsVorschlagIdent);
            } else {
                lkiavm.setAbstimmungsVorschlagIdent(0);
            }
        }

        eclKIAVListeM.setKiavListeM(lkiavmListe);
        return true;

    }

    /*****************Aufruffunktionen aus JSF -Dialog heraus*********************************/

    /** Zwischenmaske Eintrittskarten aufrufen*/
    public String doAnmeldenEK() {
        if (!aFunktionen.pruefeStart("aAnmelden")) {
            return "aDlgFehler";
        }

        aDlgVariablen.setAusgewaehlteAktion("1");

        aDlgVariablen.clearDlgOhneGastkarteAnforderung();
        return aFunktionen.setzeEnde("aAnmeldenEK", true, false);

    }

    public String doNeueWillenserklaerungEK() {
        if (!aFunktionen.pruefeStart("aNeueWillenserklaerung")) {
            return "aDlgFehler";
        }

        aDlgVariablen.clearDlgOhneGastkarteAnforderung();
        return aFunktionen.setzeEnde("aNeueWillenserklaerungEK", true, false);

    }

    /*****ZWischenmaske - zurück*/
    public String doAnmeldenEKZurueck() {
        if (!aFunktionen.pruefeStart("aAnmeldenEK")) {
            return "aDlgFehler";
        }

        aDlgVariablen.setAusgewaehlteAktion("1");

        return aFunktionen.setzeEnde("aAnmelden", true, true);

    }

    /*****ZWischenmaske - zurück*/
    public String doNeueWillenserklaerungEKZurueck() {
        if (!aFunktionen.pruefeStart("aNeueWillenserklaerungEK")) {
            return "aDlgFehler";
        }

        return aFunktionen.setzeEnde("aNeueWillenserklaerung", true, true);

    }

    /** Eine Eintrittskarte "Selbst" ausstellen - aus aAnmelden*/
    public String doAnmeldenEineEKSelbst() {
        if (!aFunktionen.pruefeStart("aAnmeldenEK")) {
            return "aDlgFehler";
        }

        aDlgVariablen.setAusgewaehlteAktion("1");

        aDlgVariablen.clearDlgOhneGastkarteAnforderung();
        return aFunktionen.setzeEnde("aEintrittskarte", true, false);
    }

    /** Eine Eintrittskarte "Selbst" ausstellen - aus aNeueWillenserklaerung*/
    public String doNeueWillenserklaerungEineEKSelbst() {
        if (!aFunktionen.pruefeStart("aNeueWillenserklaerungEK")) {
            return "aDlgFehler";
        }

        aDlgVariablen.setAusgewaehlteAktion("1");

        return aFunktionen.setzeEnde("aEintrittskarte", true, true);
    }

    /**Zwei Eintrittskarte (Personengemeinschaft) ausstellen - aus aAnmelden*/
    public String doAnmeldenZweiEKPersonengemeinschaft() {

        boolean ergBool = false;
        if (!aFunktionen.pruefeStart("aAnmeldenEK")) {
            return "aDlgFehler";
        }

        aFunktionen.setzeEnde("aAnmeldenEK", true, true);

        aDlgVariablen.clearDlgOhneGastkarteAnforderung();

        eclDbM.openAll();
        ergBool = checkPersonengemeinschaft();
        eclDbM.closeAll();

        if (ergBool == false) {
            aFunktionen.setzeEnde();
            return "";
        } else {
            aDlgVariablen.setAusgewaehlteAktion("2");
            return aFunktionen.setzeEnde("aEintrittskarte", true, false);
        }

    }

    /**Zwei Eintrittskarte (Personengemeinschaft) ausstellen aus aNeueWillenserklaerung*/
    public String doNeueWillenserklaerungZweiEKPersonengemeinschaft() {

        boolean ergBool = false;
        if (!aFunktionen.pruefeStart("aNeueWillenserklaerungEK")) {
            return "aDlgFehler";
        }

        eclDbM.openAll();
        ergBool = checkPersonengemeinschaft();
        eclDbM.closeAll();

        if (ergBool == false) {
            aFunktionen.setzeEnde();
            return "";
        } else {
            aDlgVariablen.setAusgewaehlteAktion("2");
            return aFunktionen.setzeEnde("aEintrittskarte", true, true);
        }

    }

    /**Zwei Eintrittskarte (jede EK) mit/ohne Vollmacht ausstellen - aus aAnmelden*/
    public String doAnmeldenZweiEK() {

        boolean ergBool = false;
        if (!aFunktionen.pruefeStart("aAnmeldenEK")) {
            return "aDlgFehler";
        }

        aFunktionen.setzeEnde("aAnmeldenEK", true, false);
        aDlgVariablen.clearDlgOhneGastkarteAnforderung();

        eclDbM.openAll();
        ergBool = check2EK();
        eclDbM.closeAll();

        if (ergBool == false) {
            aFunktionen.setzeEnde();
            return "";
        } else {
            aDlgVariablen.setAusgewaehlteAktion("28");
            return aFunktionen.setzeEnde("aEintrittskarte", true, false);
        }

    }

    /**Zwei Eintrittskarte (jede EK) mit/ohne Vollmacht ausstellen aus aNeueWillenserklaerung*/
    public String doNeueWillenserklaerungZweiEK() {

        boolean ergBool = false;
        if (!aFunktionen.pruefeStart("aNeueWillenserklaerungEK")) {
            return "aDlgFehler";
        }

        eclDbM.openAll();
        ergBool = check2EK();
        eclDbM.closeAll();

        if (ergBool == false) {
            aFunktionen.setzeEnde();
            return "";
        } else {
            aDlgVariablen.setAusgewaehlteAktion("28");
            return aFunktionen.setzeEnde("aEintrittskarte", true, true);
        }

    }

    /**Zwei Eintrittskarte (jede EK) Selbst ausstellen - aus aAnmelden*/
    public String doAnmeldenZweiEKSelbst() {

        boolean ergBool = false;
        if (!aFunktionen.pruefeStart("aAnmeldenEK")) {
            return "aDlgFehler";
        }

        aFunktionen.setzeEnde("aAnmeldenEK", true, false);
        aDlgVariablen.clearDlgOhneGastkarteAnforderung();

        eclDbM.openAll();
        ergBool = check2EK();
        eclDbM.closeAll();

        if (ergBool == false) {
            aFunktionen.setzeEnde();
            return "";
        } else {
            aDlgVariablen.setAusgewaehlteAktion("30");
            return aFunktionen.setzeEnde("aEintrittskarte", true, false);
        }

    }

    /**Zwei Eintrittskarte (jede EK) ausstellen aus aNeueWillenserklaerung*/
    public String doNeueWillenserklaerungZweiEKSelbst() {

        boolean ergBool = false;
        if (!aFunktionen.pruefeStart("aNeueWillenserklaerungEK")) {
            return "aDlgFehler";
        }

        eclDbM.openAll();
        ergBool = check2EK();
        eclDbM.closeAll();

        if (ergBool == false) {
            aFunktionen.setzeEnde();
            return "";
        } else {
            aDlgVariablen.setAusgewaehlteAktion("30");
            return aFunktionen.setzeEnde("aEintrittskarte", true, true);
        }

    }

    /**Eine Eintrittskarte auf Bevollmächtigten ausstellen - aus aAnmelden*/
    public String doAnmeldenEineEKmitVollmacht() {
        if (!aFunktionen.pruefeStart("aAnmeldenEK")) {
            return "aDlgFehler";
        }
        aDlgVariablen.setAusgewaehlteAktion("3");
        aDlgVariablen.clearDlgOhneGastkarteAnforderung();
        return aFunktionen.setzeEnde("aEintrittskarte", true, false);
    }

    /**Eine Eintrittskarte auf Bevollmächtigten ausstellen - aus aNeueWillenserklaerung*/
    public String doNeueWillenserklaerungEineEKmitVollmacht() {
        if (!aFunktionen.pruefeStart("aNeueWillenserklaerungEK")) {
            return "aDlgFehler";
        }
        aDlgVariablen.setAusgewaehlteAktion("3");
        return aFunktionen.setzeEnde("aEintrittskarte", true, true);
    }

    /**Vollmacht/Weisung auf Stimmrechtsvertreter - aus aAnmelden*/
    public String doAnmeldenVwSrv() {
        if (!aFunktionen.pruefeStart("aAnmelden")) {
            return "aDlgFehler";
        }

        aDlgVariablen.setAusgewaehlteAktion("4");

        eclDbM.openAll();
        aFunktionen.leseAbstimmungsliste(eclDbM.getDbBundle(), eclTeilnehmerLoginM.getGattung(),
                KonstWeisungserfassungSicht.portalWeisungserfassung, KonstSkIst.srv);
        aControllerKIAVAuswahl.leseAbstimmungsvorschlagGesellschaft(eclDbM.getDbBundle());
        eclDbM.closeAll();
        aDlgVariablen.clearDlgOhneGastkarteAnforderung();
        return aFunktionen.setzeEnde("aWeisung", true, false);
    }

    /**Vollmacht/Weisung auf Stimmrechtsvertreter - aus aNeueWillenserklaerung*/
    public String doNeueWillenserklaerungVwSrv() {
        if (!aFunktionen.pruefeStart("aNeueWillenserklaerung")) {
            return "aDlgFehler";
        }

        aDlgVariablen.setAusgewaehlteAktion("4");

        eclDbM.openAll();
        aFunktionen.leseAbstimmungsliste(eclDbM.getDbBundle(), eclZugeordneteMeldungM.getGattung(),
                KonstWeisungserfassungSicht.portalWeisungserfassung, KonstSkIst.srv);
        aControllerKIAVAuswahl.leseAbstimmungsvorschlagGesellschaft(eclDbM.getDbBundle());
        eclDbM.closeAll();

        return aFunktionen.setzeEnde("aWeisung", true, true);
    }

    /**Vollmacht/Weisung auf Stimmrechtsvertreter - aus aStatus - nur für Inhaberaktien!*/
    public String doNeueWillenserklaerungVwSrvAusStatus() {
        if (!aFunktionen.pruefeStart("aStatus")) {
            return "aDlgFehler";
        }

        aDlgVariablen.setAusgewaehlteAktion("4");

        /*Bereitstellen der Willenserklärungsdaten für Oberfläche*/
        eclZugeordneteMeldungM
                .copyFromMOhneStorno(eclZugeordneteMeldungListeM.getZugeordneteMeldungenEigeneAktienListeM().get(0));

        eclDbM.openAll();
        aFunktionen.leseAbstimmungsliste(eclDbM.getDbBundle(), eclZugeordneteMeldungM.getGattung(),
                KonstWeisungserfassungSicht.portalWeisungserfassung, KonstSkIst.srv);
        aControllerKIAVAuswahl.leseAbstimmungsvorschlagGesellschaft(eclDbM.getDbBundle());
        eclDbM.closeAll();

        return aFunktionen.setzeEnde("aWeisung", true, true);
    }

    /**Briefwahl - aus aAnmelden*/
    public String doAnmeldenBriefwahl() {
        if (!aFunktionen.pruefeStart("aAnmelden")) {
            return "aDlgFehler";
        }

        aDlgVariablen.setAusgewaehlteAktion("5");

        eclDbM.openAll();
        aFunktionen.leseAbstimmungsliste(eclDbM.getDbBundle(), eclTeilnehmerLoginM.getGattung(),
                KonstWeisungserfassungSicht.portalWeisungserfassung, KonstSkIst.briefwahl);
        aControllerKIAVAuswahl.leseAbstimmungsvorschlagGesellschaft(eclDbM.getDbBundle());
        eclDbM.closeAll();
        aDlgVariablen.clearDlgOhneGastkarteAnforderung();
        return aFunktionen.setzeEnde("aWeisung", true, false);
    }

    /**Briefwahl - aus aNeueWillenserklaerung*/
    public String doNeueWillenserklaerungBriefwahl() {
        if (!aFunktionen.pruefeStart("aNeueWillenserklaerung")) {
            return "aDlgFehler";
        }

        aDlgVariablen.setAusgewaehlteAktion("5");

        eclDbM.openAll();
        aFunktionen.leseAbstimmungsliste(eclDbM.getDbBundle(), eclZugeordneteMeldungM.getGattung(),
                KonstWeisungserfassungSicht.portalWeisungserfassung, KonstSkIst.briefwahl);
        aControllerKIAVAuswahl.leseAbstimmungsvorschlagGesellschaft(eclDbM.getDbBundle());
        eclDbM.closeAll();
        return aFunktionen.setzeEnde("aWeisung", true, true);
    }

    /**Briefwahl - aus aStatus - nur für Inhaberaktien!*/
    public String doNeueWillenserklaerungBriefwahlAusStatus() {
        if (!aFunktionen.pruefeStart("aStatus")) {
            return "aDlgFehler";
        }

        aDlgVariablen.setAusgewaehlteAktion("5");

        /*Bereitstellen der Willenserklärungsdaten für Oberfläche*/
        eclZugeordneteMeldungM
                .copyFromMOhneStorno(eclZugeordneteMeldungListeM.getZugeordneteMeldungenEigeneAktienListeM().get(0));

        eclDbM.openAll();
        aFunktionen.leseAbstimmungsliste(eclDbM.getDbBundle(), eclZugeordneteMeldungM.getGattung(),
                KonstWeisungserfassungSicht.portalWeisungserfassung, KonstSkIst.briefwahl);
        aControllerKIAVAuswahl.leseAbstimmungsvorschlagGesellschaft(eclDbM.getDbBundle());
        eclDbM.closeAll();
        return aFunktionen.setzeEnde("aWeisung", true, true);
    }

    /**Vollmacht/Weisung an KIAV - aus aAnmelden*/
    public String doAnmeldenKIAV() {
        if (!aFunktionen.pruefeStart("aAnmelden")) {
            return "aDlgFehler";
        }

        aDlgVariablen.setAusgewaehlteAktion("6");

        eclDbM.openAll();
        aFunktionen.leseAbstimmungsliste(eclDbM.getDbBundle(), eclTeilnehmerLoginM.getGattung(),
                KonstWeisungserfassungSicht.portalWeisungserfassung, KonstSkIst.kiav);
        leseKIAVListe(eclDbM.getDbBundle());
        eclDbM.closeAll();

        aDlgVariablen.clearDlgOhneGastkarteAnforderung();
        return aFunktionen.setzeEnde("aKiavAuswahl", true, false);
    }

    /**Vollmacht/Weisung an KIAV - aus aNeueWillenserklaerung*/
    public String doNeueWillenserklaerungKIAV() {
        if (!aFunktionen.pruefeStart("aNeueWillenserklaerung")) {
            return "aDlgFehler";
        }

        aDlgVariablen.setAusgewaehlteAktion("6");

        eclDbM.openAll();
        aFunktionen.leseAbstimmungsliste(eclDbM.getDbBundle(), eclZugeordneteMeldungM.getGattung(),
                KonstWeisungserfassungSicht.portalWeisungserfassung, KonstSkIst.kiav);
        leseKIAVListe(eclDbM.getDbBundle());
        eclDbM.closeAll();

        return aFunktionen.setzeEnde("aKiavAuswahl", true, true);
    }

    /**Vollmacht an Dritte - ohne Ausstellen einer neuen Eintrittskarte, nur Eingabe / Speichern des
     * Bevollmächtigten - derzeit nur aus aNeueWillenserklaerung heraus möglich!
     */
    public String doNeueWillenserklaerungVollmachtDritte() {
        if (!aFunktionen.pruefeStart("aNeueWillenserklaerung")) {
            return "aDlgFehler";
        }
        aDlgVariablen.setAusgewaehlteAktion("21");
        return aFunktionen.setzeEnde("aVollmachtDritte", true, true);

    }

    public String doNeueWillenserklaerungZurueck() {
        if (!aFunktionen.pruefeStart("aNeueWillenserklaerung")) {
            return "aDlgFehler";
        }
        return aFunktionen.setzeEnde("aStatus", true, true);

    }

    /**Abmelden*/
    public String doAbmelden() {
        aDlgVariablen.clearLogin();
        return aFunktionen.waehleLogout();
    }

}
