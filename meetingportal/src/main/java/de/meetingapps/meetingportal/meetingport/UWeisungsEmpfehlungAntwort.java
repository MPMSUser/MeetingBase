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

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComBl.BlAbstimmungsVorschlagEmpfehlung;
import de.meetingapps.meetingportal.meetComBl.BlWillenserklaerung;
import de.meetingapps.meetingportal.meetComBlManaged.BlMInsti;
import de.meetingapps.meetingportal.meetComBlManaged.BlMNachrichten;
import de.meetingapps.meetingportal.meetComEclM.EclAbstimmungMitVorschlagM;
import de.meetingapps.meetingportal.meetComEclM.EclDbM;
import de.meetingapps.meetingportal.meetComEclM.EclInstiZugeordneterBestandListeM;
import de.meetingapps.meetingportal.meetComEclM.EclInstiZugeordneterBestandM;
import de.meetingapps.meetingportal.meetComEclM.EclNachrichtM;
import de.meetingapps.meetingportal.meetComEclM.EclParamM;
import de.meetingapps.meetingportal.meetComEclM.EclUserLoginM;
import de.meetingapps.meetingportal.meetComEclM.EclWillenserklaerungStatusM;
import de.meetingapps.meetingportal.meetComEclM.EclZugeordneteMeldungM;
import de.meetingapps.meetingportal.meetComEntities.EclAbstimmungsVorschlagEmpfehlung;
import de.meetingapps.meetingportal.meetComEntities.EclWeisungMeldung;
import de.meetingapps.meetingportal.meetComEntities.EclWeisungMeldungRaw;
import de.meetingapps.meetingportal.meetComKonst.KonstWillenserklaerung;
import de.meetingapps.meetingportal.meetComKonst.KonstWillenserklaerungWeg;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@RequestScoped
@Named
public class UWeisungsEmpfehlungAntwort {

    @Inject
    private EclDbM eclDbM;
    @Inject
    private UWeisungsEmpfehlungAntwortSession uWeisungsEmpfehlungAntwortSession;
    @Inject
    private XSessionVerwaltung xSessionVerwaltung;
    @Inject
    private EclUserLoginM eclUserLoginM;
    @Inject
    private BlMInsti blmInsti;
    @Inject
    private EclInstiZugeordneterBestandListeM eclInstiZugeordneterBestandListeM;
    @Inject
    private AFunktionen aFunktionen;
    @Inject
    private USession uSession;
    @Inject
    private BlMNachrichten blMNachrichten;
    @Inject
    private EclParamM eclParamM;

    /**eclDbM wird in aufrufender Funktion gehandelt*/
    public boolean init(EclNachrichtM pEclNachrichtM, String pAufrufseite) {

        uWeisungsEmpfehlungAntwortSession.setAufrufMaske(pAufrufseite);
        uWeisungsEmpfehlungAntwortSession.setNachrichtM(pEclNachrichtM);

        /*Prüfen, ob Bestände vorhanden*/
        blmInsti.fuelleBestandszuordnungFuerPortal(eclDbM.getDbBundle(), eclUserLoginM.getGehoertZuInsti());
        List<EclInstiZugeordneterBestandM> instiZugeordneterBestandM = eclInstiZugeordneterBestandListeM
                .getInstiZugeordneterBestandM();
        if (instiZugeordneterBestandM == null || instiZugeordneterBestandM.size() == 0) {
            /*Kein Bestand vorhanden*/

            /*Nachricht auf bearbeitet setzen*/
            blMNachrichten.nachrichtSetzeBearbeitetBeimEmpfaenger(eclDbM.getDbBundle(),
                    uWeisungsEmpfehlungAntwortSession.getNachrichtM().getIdent(), uWeisungsEmpfehlungAntwortSession.getNachrichtM().getDbServerIdent());
            blMNachrichten.vorbereitenNachrichtenEmpfangslisteRefresh(eclDbM.getDbBundle());

            return false;
        }

        /**Anzuzeigende TOP-Liste mit Weisungsvorschlag aufbereiten (nur Abstimmungen zum aktuellen Weisungsvorschlag)*/
        List<EclAbstimmungMitVorschlagM> toMitVorschlagListe = new LinkedList<EclAbstimmungMitVorschlagM>();

        int lEmpfehlungsIdent = Integer.parseInt(pEclNachrichtM.getParameter1());
        eclDbM.getDbBundle().dbAbstimmungsVorschlagEmpfehlung.read(lEmpfehlungsIdent);

        EclAbstimmungsVorschlagEmpfehlung lAbstimmungsVorschlagEmpfehlung = eclDbM
                .getDbBundle().dbAbstimmungsVorschlagEmpfehlung.ergebnisPosition(0);
        BlAbstimmungsVorschlagEmpfehlung blAbstimmungsVorschlagEmpfehlung = new BlAbstimmungsVorschlagEmpfehlung(true,
                eclDbM.getDbBundle());
        blAbstimmungsVorschlagEmpfehlung.erzeugeUebersichtFuerEmpfehlung(lAbstimmungsVorschlagEmpfehlung);

        if (blAbstimmungsVorschlagEmpfehlung.rcAngezeigteAbstimmungenListe != null) {
            for (int i = 0; i < blAbstimmungsVorschlagEmpfehlung.rcAngezeigteAbstimmungenListe.size(); i++) {
                toMitVorschlagListe.add(new EclAbstimmungMitVorschlagM(
                        blAbstimmungsVorschlagEmpfehlung.rcAngezeigteAbstimmungenListe.get(i),
                        blAbstimmungsVorschlagEmpfehlung.rcEmpfehlungsListe.get(i),
                        blAbstimmungsVorschlagEmpfehlung.rcEmpfehlungsListe.get(i)));
            }
        }

        uWeisungsEmpfehlungAntwortSession.setToMitVorschlagListe(toMitVorschlagListe);

        uWeisungsEmpfehlungAntwortSession.setEmpfehlungUebernehmen("1");

        return true;
    }

    public String doAbbrechen() {
        if (!xSessionVerwaltung.pruefeUStart("uWeisungsEmpfehlungAntwort", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }
        String zielSeite = uWeisungsEmpfehlungAntwortSession.getAufrufMaske();
        return xSessionVerwaltung.setzeUEnde(zielSeite, true, false, eclUserLoginM.getKennung());
    }

    public String doWeiter() {
        if (eclUserLoginM.pruefe_govVal_insti() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uWeisungsEmpfehlungAntwort", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }
        eclDbM.openAll();
        eclDbM.openWeitere();
        if (!aFunktionen.pruefeOBWillenserklaerungZulaessig(eclDbM.getDbBundle(),
                KonstWillenserklaerung.vollmachtUndWeisungAnSRV, "2")) {
            eclDbM.closeAll();
            uSession.setFehlermeldung("Weisungserteilung nicht mehr möglich");
            xSessionVerwaltung.setzeEnde();
            return "";
        }

        List<EclAbstimmungMitVorschlagM> lToMitVorschlagListe = uWeisungsEmpfehlungAntwortSession
                .getToMitVorschlagListe();

        /*Alle Bestände des Instis einlesen und in Schleife durchwandern*/
        blmInsti.fuelleBestandszuordnungFuerPortal(eclDbM.getDbBundle(), eclUserLoginM.getGehoertZuInsti());
        List<EclInstiZugeordneterBestandM> instiZugeordneterBestandM = eclInstiZugeordneterBestandListeM
                .getInstiZugeordneterBestandM();
        if (instiZugeordneterBestandM != null) {
            for (int i = 0; i < instiZugeordneterBestandM.size(); i++) {
                EclInstiZugeordneterBestandM lInstiZugeordneterBestandM = instiZugeordneterBestandM.get(i);
                for (int i1 = 0; i1 < lInstiZugeordneterBestandM.getZugeordneteMeldungListeM()
                        .getZugeordneteMeldungenEigeneAktienListeM().size(); i1++) {
                    EclZugeordneteMeldungM lZugeordneteMeldungM = lInstiZugeordneterBestandM
                            .getZugeordneteMeldungListeM().getZugeordneteMeldungenEigeneAktienListeM().get(i1);
                    for (int i2 = 0; i2 < lZugeordneteMeldungM.getZugeordneteWillenserklaerungenListM().size(); i2++) {
                        EclWillenserklaerungStatusM lWillenserklaerungStatusM = lZugeordneteMeldungM
                                .getZugeordneteWillenserklaerungenListM().get(i2);
                        /*Alte Weisung einlesen*/
                        eclDbM.getDbBundle().dbWeisungMeldung.leseZuWillenserklaerungIdent(
                                lWillenserklaerungStatusM.getWillenserklaerungIdent(), true);
                        EclWeisungMeldung lWeisungMeldung = eclDbM.getDbBundle().dbWeisungMeldung
                                .weisungMeldungGefunden(0);

                        /*Neue Willenserklärung erzeigen*/
                        BlWillenserklaerung vwWillenserklaerung = new BlWillenserklaerung();
                        vwWillenserklaerung.pQuelle = "";
                        vwWillenserklaerung.pErteiltAufWeg = KonstWillenserklaerungWeg.portal;
                        vwWillenserklaerung.pErteiltZeitpunkt = "";
                        vwWillenserklaerung.piMeldungsIdentAktionaer = lZugeordneteMeldungM.getMeldungsIdent();

                        /*Ändern setzt immer voraus, dass vorher schon der Anmeldeprozess durchgelaufen ist. D.h.: hier ist immer
                         * ausgewaehlteHauptaktion=2, d.h. in eclZugeordneteMeldung steht zur Verfügung
                         */

                        vwWillenserklaerung.pWillenserklaerungGeberIdent = -1; /*Aktionär*/

                        vwWillenserklaerung.pAufnehmendeSammelkarteIdent = lWeisungMeldung.sammelIdent;

                        /*Abgegebene Weisung (uninterpretiert)
                        public EclWeisungMeldungRaw pEclWeisungMeldungRaw=null;*/
                        vwWillenserklaerung.pEclWeisungMeldungRaw = new EclWeisungMeldungRaw();
                        /*Abgegebene Weisung (interpretiert)
                        public EclWeisungMeldung pEclWeisungMeldung=null;*/
                        vwWillenserklaerung.pEclWeisungMeldung = new EclWeisungMeldung();
                        /*Alte WeisungsIdent, d.h. Ident der Weisung, die geändert wird*/
                        vwWillenserklaerung.pEclWeisungMeldung.weisungIdent = lWeisungMeldung.weisungIdent;
                        /*Alte Willenserklärung, die zu der zu ändernden Weisung gehört*/
                        vwWillenserklaerung.pEclWeisungMeldung.willenserklaerungIdent = lWeisungMeldung.willenserklaerungIdent;

                        /*Stimmabgabe speichern*/
                        /*Alte Abgabe übernehmen*/
                        for (int i0 = 0; i0 < 200; i0++) {
                            vwWillenserklaerung.pEclWeisungMeldung.abgabe[i0] = lWeisungMeldung.abgabe[i0];
                        }
                        /*Neue "darüber" speichern*/
                        for (int i0 = 0; i0 < lToMitVorschlagListe.size(); i0++) {
                            EclAbstimmungMitVorschlagM lAbstimmungMitVorschlagM = lToMitVorschlagListe.get(i0);
                            int posInWeisung = lAbstimmungMitVorschlagM.getIdentWeisungssatz();
                            if (posInWeisung != -1) {
                                switch (lAbstimmungMitVorschlagM.getErteilteWeisung()) {
                                case ".":
                                    vwWillenserklaerung.pEclWeisungMeldungRaw.abgabe[posInWeisung] = " ";
                                    vwWillenserklaerung.pEclWeisungMeldung.abgabe[posInWeisung] = 0;
                                    break;
                                case "J":
                                    vwWillenserklaerung.pEclWeisungMeldungRaw.abgabe[posInWeisung] = " X";
                                    vwWillenserklaerung.pEclWeisungMeldung.abgabe[posInWeisung] = 1;
                                    break;
                                case "N":
                                    vwWillenserklaerung.pEclWeisungMeldungRaw.abgabe[posInWeisung] = "  X";
                                    vwWillenserklaerung.pEclWeisungMeldung.abgabe[posInWeisung] = 2;
                                    break;
                                case "E":
                                    vwWillenserklaerung.pEclWeisungMeldungRaw.abgabe[posInWeisung] = "   X";
                                    vwWillenserklaerung.pEclWeisungMeldung.abgabe[posInWeisung] = 3;
                                    break;
                                }

                            }
                        }

                        /*Sonstige Parameter für Willenserklärung Weisungsänderung speichern*/
                        /*Derzeit nicht erforderlich*/
                        /*Willenserklärung speichern*/
                        vwWillenserklaerung.aendernWeisungAnSRV(eclDbM.getDbBundle());

                        /*Falls nicht möglich: Fehlermeldung, zum Anmelden*/
                        if (vwWillenserklaerung.rcIstZulaessig == false) {
                            CaBug.drucke("UWeisung.speichern 001" + vwWillenserklaerung.rcGrundFuerUnzulaessig);
                            eclDbM.closeAll();
                            uSession.setFehlermeldung("Weisungserteilung nicht mehr möglich - interner Fehler");
                            xSessionVerwaltung.setzeEnde();
                            return "";
                            //							uSession.setFehlermeldung(eclPortalTexteM.getFehlertext(vwWillenserklaerung.rcGrundFuerUnzulaessig));
                            //							eclDbM.closeAllAbbruch();
                            //							aFunktionen.setzeEnde();return "";
                        }
                    }
                }
            }
        }

        /*Nachricht auf bearbeitet setzen*/
        blMNachrichten.nachrichtSetzeBearbeitetBeimEmpfaenger(eclDbM.getDbBundle(),
                uWeisungsEmpfehlungAntwortSession.getNachrichtM().getIdent(), uWeisungsEmpfehlungAntwortSession.getNachrichtM().getDbServerIdent());
        blMNachrichten.vorbereitenNachrichtenEmpfangslisteRefresh(eclDbM.getDbBundle());

        eclDbM.closeAll();

        String zielSeite = uWeisungsEmpfehlungAntwortSession.getAufrufMaske();
        if (zielSeite.equals("uMenueAuswahlMandant")) {
            eclParamM.getClGlobalVar().mandant = 0;
            eclParamM.getClGlobalVar().hvJahr = 0;
            eclParamM.getClGlobalVar().hvNummer = "";
            eclParamM.getClGlobalVar().datenbereich = "";
        } else {
        }

        return xSessionVerwaltung.setzeUEnde(zielSeite, true, false, eclUserLoginM.getKennung());
    }

}
