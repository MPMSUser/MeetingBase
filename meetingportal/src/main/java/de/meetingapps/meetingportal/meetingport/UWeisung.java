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
import de.meetingapps.meetingportal.meetComEclM.EclAbstimmungMitVorschlagM;
import de.meetingapps.meetingportal.meetComEclM.EclDbM;
import de.meetingapps.meetingportal.meetComEclM.EclInstiZugeordneterBestandM;
import de.meetingapps.meetingportal.meetComEclM.EclUserLoginM;
import de.meetingapps.meetingportal.meetComEclM.EclWillenserklaerungStatusM;
import de.meetingapps.meetingportal.meetComEclM.EclZugeordneteMeldungM;
import de.meetingapps.meetingportal.meetComEntities.EclWeisungMeldung;
import de.meetingapps.meetingportal.meetComEntities.EclWeisungMeldungRaw;
import de.meetingapps.meetingportal.meetComKonst.KonstWillenserklaerung;
import de.meetingapps.meetingportal.meetComKonst.KonstWillenserklaerungWeg;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@RequestScoped
@Named
public class UWeisung {

    private int logDrucken = 10;

    @Inject
    private UWeisungSession uWeisungSessionM;
    @Inject
    private XSessionVerwaltung xSessionVerwaltung;
    @Inject
    private EclUserLoginM eclUserLoginM;
    @Inject
    private UMenue uMenue;
    @Inject
    private EclDbM eclDbM;
    @Inject
    private AFunktionen aFunktionen;
    @Inject
    private USession uSession;

    public String doAendern(EclInstiZugeordneterBestandM pInstiZugeordneterBestandM,
            EclZugeordneteMeldungM pZugeordneteMeldungM, EclWillenserklaerungStatusM pWillenserklaerungStatusM) {
        if (eclUserLoginM.pruefe_govVal_insti() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uMenue", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }

        eclDbM.openAll();
        eclDbM.openWeitere();

        vorbelegen(pInstiZugeordneterBestandM, pZugeordneteMeldungM, pWillenserklaerungStatusM);

        eclDbM.closeAll();
        uWeisungSessionM.setAendern(true);
        uWeisungSessionM.setAnzeigen(false);

        return xSessionVerwaltung.setzeUEnde("uWeisung", true, false, eclUserLoginM.getKennung());
    }

    public String doAnzeigen(EclInstiZugeordneterBestandM pInstiZugeordneterBestandM,
            EclZugeordneteMeldungM pZugeordneteMeldungM, EclWillenserklaerungStatusM pWillenserklaerungStatusM) {
        if (eclUserLoginM.pruefe_govVal_insti() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uMenue", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }

        eclDbM.openAll();
        eclDbM.openWeitere();

        vorbelegen(pInstiZugeordneterBestandM, pZugeordneteMeldungM, pWillenserklaerungStatusM);

        eclDbM.closeAll();

        uWeisungSessionM.setAendern(false);
        uWeisungSessionM.setAnzeigen(true);

        return xSessionVerwaltung.setzeUEnde("uWeisung", true, false, eclUserLoginM.getKennung());
    }

    public String doWeiter() {
        if (eclUserLoginM.pruefe_govVal_insti() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uWeisung", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }
        eclDbM.openAll();
        eclDbM.openWeitere();
        if (uWeisungSessionM.isAendern()) {
            /**Änderungsmodus - veränderte Weisung speichern*/
            boolean rc = speichern();
            if (rc == false) {
                uSession.setFehlermeldung("Weisungsänderung nicht mehr zulässig");
                xSessionVerwaltung.setzeEnde();
                eclDbM.closeAll();
                return "";
            }
        }

        uMenue.init();
        eclDbM.closeAll();
        return xSessionVerwaltung.setzeUEnde("uMenue", true, false, eclUserLoginM.getKennung());
    }

    public String doAbbrechen() {
        if (eclUserLoginM.pruefe_govVal_insti() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uWeisung", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }
        eclDbM.openAll();
        uMenue.init();
        eclDbM.closeAll();
        return xSessionVerwaltung.setzeUEnde("uMenue", true, false, eclUserLoginM.getKennung());
    }

    /**Open+Weitere/Close in aufrufender Funktion*/
    private void vorbelegen(EclInstiZugeordneterBestandM pInstiZugeordneterBestandM,
            EclZugeordneteMeldungM pZugeordneteMeldungM, EclWillenserklaerungStatusM pWillenserklaerungStatusM) {
        uWeisungSessionM.setEclInstiZugeordneterBestandM(pInstiZugeordneterBestandM);
        uWeisungSessionM.setEclZugeordneteMeldungM(pZugeordneteMeldungM);
        uWeisungSessionM.setEclWillenserklaerungStatusM(pWillenserklaerungStatusM);

        /**Aktuelle Weisung einlesen*/
        eclDbM.getDbBundle().dbWeisungMeldung
                .leseZuWillenserklaerungIdent(pWillenserklaerungStatusM.getWillenserklaerungIdent(), true);
        EclWeisungMeldung lWeisungMeldung = eclDbM.getDbBundle().dbWeisungMeldung.weisungMeldungGefunden(0);
        //EclWeisungMeldungRaw lWeisungMeldungRaw=eclDbM.getDbBundle().dbWeisungMeldung.weisungMeldungRawGefunden(0);

        uWeisungSessionM.setWillenserklaerungIdent(lWeisungMeldung.willenserklaerungIdent);
        uWeisungSessionM.setSammelIdent(lWeisungMeldung.sammelIdent);
        uWeisungSessionM.setWeisungsIdent(lWeisungMeldung.weisungIdent);

        /**Anzuzeigende TOP-Liste mit Weisungsvorschlag aufbereiten*/
        List<EclAbstimmungMitVorschlagM> toMitVorschlagListe = new LinkedList<EclAbstimmungMitVorschlagM>();

        BlAbstimmungsVorschlagEmpfehlung blAbstimmungsVorschlagEmpfehlung = new BlAbstimmungsVorschlagEmpfehlung(true,
                eclDbM.getDbBundle());
        blAbstimmungsVorschlagEmpfehlung.erzeugeUebersicht();

        if (blAbstimmungsVorschlagEmpfehlung.pAngezeigteAbstimmungen != null) {
            for (int i = 0; i < blAbstimmungsVorschlagEmpfehlung.pAngezeigteAbstimmungen.length; i++) {
                int weisungsIdent = blAbstimmungsVorschlagEmpfehlung.pAngezeigteAbstimmungen[i].identWeisungssatz;
                int vorhandeneWeisung = 0;
                if (weisungsIdent != -1) {
                    vorhandeneWeisung = lWeisungMeldung.abgabe[weisungsIdent];
                }
                toMitVorschlagListe
                        .add(new EclAbstimmungMitVorschlagM(blAbstimmungsVorschlagEmpfehlung.pAngezeigteAbstimmungen[i],
                                blAbstimmungsVorschlagEmpfehlung.rcEmpfehlungsArray[i], vorhandeneWeisung));
            }
        }

        uWeisungSessionM.setToMitVorschlagListe(toMitVorschlagListe);
    }

    /**Open+Weitere/Close in aufrufender Funktion*/
    private boolean speichern() {

        BlWillenserklaerung vwWillenserklaerung = new BlWillenserklaerung();
        vwWillenserklaerung.pQuelle = "";
        vwWillenserklaerung.pErteiltAufWeg = KonstWillenserklaerungWeg.portal;
        vwWillenserklaerung.pErteiltZeitpunkt = "";
        vwWillenserklaerung.piMeldungsIdentAktionaer = uWeisungSessionM.getEclZugeordneteMeldungM().getMeldungsIdent();

        /*Ändern setzt immer voraus, dass vorher schon der Anmeldeprozess durchgelaufen ist. D.h.: hier ist immer
         * ausgewaehlteHauptaktion=2, d.h. in eclZugeordneteMeldung steht zur Verfügung
         */

        if (!aFunktionen.pruefeOBWillenserklaerungZulaessig(eclDbM.getDbBundle(),
                KonstWillenserklaerung.vollmachtUndWeisungAnSRV, "2")) {
            return false;
        }

        vwWillenserklaerung.pWillenserklaerungGeberIdent = -1; /*Aktionär*/

        vwWillenserklaerung.pAufnehmendeSammelkarteIdent = uWeisungSessionM.getSammelIdent();

        /*Abgegebene Weisung (uninterpretiert)
        public EclWeisungMeldungRaw pEclWeisungMeldungRaw=null;*/
        vwWillenserklaerung.pEclWeisungMeldungRaw = new EclWeisungMeldungRaw();
        /*Abgegebene Weisung (interpretiert)
        public EclWeisungMeldung pEclWeisungMeldung=null;*/
        vwWillenserklaerung.pEclWeisungMeldung = new EclWeisungMeldung();
        /*Alte WeisungsIdent, d.h. Ident der Weisung, die geändert wird*/
        vwWillenserklaerung.pEclWeisungMeldung.weisungIdent = uWeisungSessionM.getWeisungsIdent();
        /*Alte Willenserklärung, die zu der zu ändernden Weisung gehört*/
        vwWillenserklaerung.pEclWeisungMeldung.willenserklaerungIdent = uWeisungSessionM.getWillenserklaerungIdent();

        /*Stimmabgabe speichern*/

        List<EclAbstimmungMitVorschlagM> lToMitVorschlagListe = uWeisungSessionM.getToMitVorschlagListe();
        for (int i = 0; i < lToMitVorschlagListe.size(); i++) {
            EclAbstimmungMitVorschlagM lAbstimmungMitVorschlagM = lToMitVorschlagListe.get(i);
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
        CaBug.druckeLog("vor aendernWeisungAnSRV", logDrucken, 10);
        CaBug.druckeLog(
                "vwWillenserklaerung.pAufnehmendeSammelkarteIdent=" + vwWillenserklaerung.pAufnehmendeSammelkarteIdent,
                logDrucken, 10);
        CaBug.druckeLog("uWeisungSessionM.getWillenserklaerungIdent()=" + uWeisungSessionM.getWillenserklaerungIdent(),
               logDrucken, 10);
        CaBug.druckeLog("uWeisungSessionM.getWeisungsIdent()=" + uWeisungSessionM.getWeisungsIdent(), logDrucken, 10);
        vwWillenserklaerung.aendernWeisungAnSRV(eclDbM.getDbBundle());

        /*Falls nicht möglich: Fehlermeldung, zum Anmelden*/
        if (vwWillenserklaerung.rcIstZulaessig == false) {
            CaBug.drucke("UWeisung.speichern 001" + vwWillenserklaerung.rcGrundFuerUnzulaessig);
            return false;
            //			uSession.setFehlermeldung(eclPortalTexteM.getFehlertext(vwWillenserklaerung.rcGrundFuerUnzulaessig));
            //			eclDbM.closeAllAbbruch();
            //			aFunktionen.setzeEnde();return "";
        }

        return true;
    }

}
