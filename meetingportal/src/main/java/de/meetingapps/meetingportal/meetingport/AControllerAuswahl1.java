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

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;



@RequestScoped
@Named
@Deprecated
public class AControllerAuswahl1 {
//
//    private boolean logDrucken = true;
//
//    @Inject
//    private ADlgVariablen aDlgVariablen;
//    @Inject
//    private AFunktionen aFunktionen;
//    @Inject
//    private EclDbM eclDbM;
//    @Inject
//    private EclParamM eclParamM;
//
//    @Inject
//    private EclZugeordneteMeldungM eclZugeordneteMeldungM;
//    @Inject
//    private EclZugeordneteMeldungListeM eclZugeordneteMeldungListeM;
//
//    @Inject
//    private AControllerAuswahl1Session aControllerAuswahl1Session;
//    @Inject
//    private AControllerKIAVAuswahl aControllerKIAVAuswahl;
//    @Inject
//    private AControllerStatus aControllerStatus;
//    @Inject
//    private AControllerRegistrierung aControllerRegistrierung;
//    @Inject
//    private AControllerDialogveranstaltungen aControllerDialogveranstaltungen;
//    @Inject
//    private AControllerGeneralversammlung aControllerGeneralversammlung;
//    @Inject
//    private AControllerBeiratswahl aControllerBeiratswahl;
//
//    public boolean auswahlAnzeigen(String pSNr) {
//        int pNr = Integer.parseInt(pSNr);
//        if (Math.abs(eclParamM.getParam().paramPortal.lfdHVBeiratswahlInMenue) == pNr) {
//            return true;
//        }
//        if (Math.abs(eclParamM.getParam().paramPortal.lfdHVGeneralversammlungInMenue) == pNr) {
//            return true;
//        }
//        if (Math.abs(eclParamM.getParam().paramPortal.lfdHVDialogVeranstaltungenInMenue) == pNr) {
//            return true;
//        }
//        if (Math.abs(eclParamM.getParam().paramPortal.lfdHVEinstellungenInMenue) == pNr) {
//            return true;
//        }
//        if (Math.abs(eclParamM.getParam().paramPortal.lfdMitgliederDatenInMenue) == pNr) {
//            return true;
//        }
//        return false;
//    }
//
//    public String auswahlStart(String pSNr) {
//        int pNr = Integer.parseInt(pSNr);
//        if (Math.abs(eclParamM.getParam().paramPortal.lfdHVBeiratswahlInMenue) == pNr) {
//            return "636";
//        }
//        if (Math.abs(eclParamM.getParam().paramPortal.lfdHVGeneralversammlungInMenue) == pNr) {
//            return "639";
//        }
//        if (Math.abs(eclParamM.getParam().paramPortal.lfdHVDialogVeranstaltungenInMenue) == pNr) {
//            return "642";
//        }
//        if (Math.abs(eclParamM.getParam().paramPortal.lfdHVEinstellungenInMenue) == pNr) {
//            return "645";
//        }
//        if (Math.abs(eclParamM.getParam().paramPortal.lfdMitgliederDatenInMenue) == pNr) {
//            return "648";
//        }
//        return "";
//    }
//
//    public String auswahlButton(String pSNr) {
//        int pNr = Integer.parseInt(pSNr);
//        if (Math.abs(eclParamM.getParam().paramPortal.lfdHVBeiratswahlInMenue) == pNr) {
//            return "637";
//        }
//        if (Math.abs(eclParamM.getParam().paramPortal.lfdHVGeneralversammlungInMenue) == pNr) {
//            return "640";
//        }
//        if (Math.abs(eclParamM.getParam().paramPortal.lfdHVDialogVeranstaltungenInMenue) == pNr) {
//            return "643";
//        }
//        if (Math.abs(eclParamM.getParam().paramPortal.lfdHVEinstellungenInMenue) == pNr) {
//            return "646";
//        }
//        if (Math.abs(eclParamM.getParam().paramPortal.lfdMitgliederDatenInMenue) == pNr) {
//            return "649";
//        }
//        return "";
//    }
//
//    public String auswahlEnde(String pSNr) {
//        int pNr = Integer.parseInt(pSNr);
//        if (Math.abs(eclParamM.getParam().paramPortal.lfdHVBeiratswahlInMenue) == pNr) {
//            return "638";
//        }
//        if (Math.abs(eclParamM.getParam().paramPortal.lfdHVGeneralversammlungInMenue) == pNr) {
//            return "641";
//        }
//        if (Math.abs(eclParamM.getParam().paramPortal.lfdHVDialogVeranstaltungenInMenue) == pNr) {
//            return "644";
//        }
//        if (Math.abs(eclParamM.getParam().paramPortal.lfdHVEinstellungenInMenue) == pNr) {
//            return "647";
//        }
//        if (Math.abs(eclParamM.getParam().paramPortal.lfdMitgliederDatenInMenue) == pNr) {
//            return "650";
//        }
//        return "";
//    }
//
//    public String doButton(String pSNr) {
//        int pNr = Integer.parseInt(pSNr);
//        if (Math.abs(eclParamM.getParam().paramPortal.lfdHVBeiratswahlInMenue) == pNr) {
//            belegeAktiveModule();
//            if (!aFunktionen.pruefeStart("aAuswahl1")) {
//                return "aDlgFehler";
//            }
//            aControllerBeiratswahl.init();
//            return aFunktionen.setzeEnde("aAuswahl1Beiratswahl", true, true);
//        }
//        if (Math.abs(eclParamM.getParam().paramPortal.lfdHVGeneralversammlungInMenue) == pNr) {
//            belegeAktiveModule();
//            if (!aFunktionen.pruefeStart("aAuswahl1")) {
//                return "aDlgFehler";
//            }
//            aControllerGeneralversammlung.init();
//            return aFunktionen.setzeEnde("aAuswahl1Generalversammlung", true, true);
//        }
//        if (Math.abs(eclParamM.getParam().paramPortal.lfdHVDialogVeranstaltungenInMenue) == pNr) {
//            belegeAktiveModule();
//            if (!aFunktionen.pruefeStart("aAuswahl1")) {
//                return "aDlgFehler";
//            }
//            aControllerDialogveranstaltungen.init();
//            return aFunktionen.setzeEnde("aAuswahl1Dialogveranstaltungen", true, true);
//        }
//        if (Math.abs(eclParamM.getParam().paramPortal.lfdHVEinstellungenInMenue) == pNr) {
//            return aControllerRegistrierung.doAuswahl1Einstellungen();
//        }
//        if (Math.abs(eclParamM.getParam().paramPortal.lfdMitgliederDatenInMenue) == pNr) {
//            belegeAktiveModule();
//            if (!aFunktionen.pruefeStart("aAuswahl1")) {
//                return "aDlgFehler";
//            }
////            aControllerDatenpflege.init();
//            return aFunktionen.setzeEnde("aDatenpflege", true, true);
//        }
//        return "";
//    }
//
//    private void belegeAktiveModule() {
//        aControllerAuswahl1Session.setBeiratswahlAktiv(false);
//        aControllerAuswahl1Session.setDialogveranstaltungenAktiv(false);
//        aControllerAuswahl1Session.setGeneralversammlungAktiv(false);
//        aControllerAuswahl1Session.setAdressaenderungAktiv(false);
//
//        aControllerAuswahl1Session.setBeiratswahlMoeglich(false);
//        aControllerAuswahl1Session.setDialogveranstaltungenMoeglich(false);
//        aControllerAuswahl1Session.setGeneralversammlungMoeglich(false);
//        aControllerAuswahl1Session.setAdressaenderungMoeglich(false);
//
//        if (eclParamM.getParam().paramPortal.lfdHVBeiratswahlInMenue != 0) {
//            aControllerAuswahl1Session.setBeiratswahlAktiv(true);
//            if (eclParamM.getParam().paramPortal.lfdHVBeiratswahlInMenue > 0) {
//                aControllerAuswahl1Session.setBeiratswahlMoeglich(true);
//            }
//        }
//        if (eclParamM.getParam().paramPortal.lfdHVGeneralversammlungInMenue != 0) {
//            aControllerAuswahl1Session.setGeneralversammlungAktiv(true);
//            if (eclParamM.getParam().paramPortal.lfdHVGeneralversammlungInMenue > 0) {
//                aControllerAuswahl1Session.setGeneralversammlungMoeglich(true);
//            }
//        }
//        if (eclParamM.getParam().paramPortal.lfdHVDialogVeranstaltungenInMenue != 0) {
//            aControllerAuswahl1Session.setDialogveranstaltungenAktiv(true);
//            if (eclParamM.getParam().paramPortal.lfdHVDialogVeranstaltungenInMenue > 0) {
//                aControllerAuswahl1Session.setDialogveranstaltungenMoeglich(true);
//            }
//        }
//        CaBug.druckeLog(
//                "eclParamM.getParam().paramPortal.lfdMitgliederDatenInMenue="
//                        + eclParamM.getParam().paramPortal.lfdMitgliederDatenInMenue,
//                "AControllerAuswahl1.belegeAktiveModule", logDrucken);
//        if (eclParamM.getParam().paramPortal.lfdMitgliederDatenInMenue != 0) {
//            aControllerAuswahl1Session.setAdressaenderungAktiv(true);
//            if (eclParamM.getParam().paramPortal.lfdMitgliederDatenInMenue > 0) {
//                aControllerAuswahl1Session.setAdressaenderungMoeglich(true);
//            }
//        }
//    }
//
//    public String doBriefwahlErteilen() {
//
//        if (!aFunktionen.pruefeStart("aAuswahl1")) {
//            return "aDlgFehler";
//        }
//
//        aDlgVariablen.setAusgewaehlteAktion("5");
//        aDlgVariablen.setAusgewaehlteHauptAktion("2");
//
//        aDlgVariablen.setZielOeffentlicheID("");
//        aDlgVariablen.setUeberOeffentlicheID(false);
//
//        /*Bereitstellen der Willenserklärungsdaten für Oberfläche*/
//        eclZugeordneteMeldungM
//                .copyFromMOhneStorno(eclZugeordneteMeldungListeM.getZugeordneteMeldungenEigeneAktienListeM().get(0));
//
//        eclDbM.openAll();
//        aFunktionen.leseAbstimmungsliste(eclDbM.getDbBundle(), eclZugeordneteMeldungM.getGattung(),
//                KonstWeisungserfassungSicht.portalWeisungserfassung, KonstSkIst.briefwahl);
//        aControllerKIAVAuswahl.leseAbstimmungsvorschlagGesellschaft(eclDbM.getDbBundle());
//        eclDbM.closeAll();
//        return aFunktionen.setzeEnde("aWeisung", true, true);
//
//    }
//
//    public String doBriefwahlAendern() {
//        if (!aFunktionen.pruefeStart("aAuswahl1")) {
//            return "aDlgFehler";
//        }
//        return belegeMeldungUndWillenserklaerung(1);
//    }
//
//    public String doBriefwahlStornieren() {
//        if (!aFunktionen.pruefeStart("aAuswahl1")) {
//            return "aDlgFehler";
//        }
//        return belegeMeldungUndWillenserklaerung(2);
//    }
//
//    private String belegeMeldungUndWillenserklaerung(int aendernOderStornieren) {
//
//        EclZugeordneteMeldungM pZugeordneteMeldungM = null;
//        EclWillenserklaerungStatusM pWillenserklaerungStatusM = null;
//
//        int gef = -1;
//        for (int i = 0; i < eclZugeordneteMeldungListeM.getZugeordneteMeldungenEigeneAktienListeM().size(); i++) {
//            pZugeordneteMeldungM = eclZugeordneteMeldungListeM.getZugeordneteMeldungenEigeneAktienListeM().get(i);
//            for (int i1 = 0; i1 < pZugeordneteMeldungM.getZugeordneteWillenserklaerungenListM().size(); i1++) {
//                pWillenserklaerungStatusM = eclZugeordneteMeldungListeM.getZugeordneteMeldungenEigeneAktienListeM()
//                        .get(i).getZugeordneteWillenserklaerungenListM().get(i1);
//                if (pWillenserklaerungStatusM.getWillenserklaerung() == KonstWillenserklaerung.briefwahl
//                        || pWillenserklaerungStatusM
//                                .getWillenserklaerung() == KonstWillenserklaerung.aendernBriefwahl) {
//                    gef = 1;
//                }
//
//            }
//
//        }
//        if (gef != 1) {
//            CaBug.drucke("AControllerAuswahl1.belegeMeldungUndWillenserklaerung 001");
//        }
//        if (aendernOderStornieren == 1) {
//            return aControllerStatus.aendernAusfuehren(pZugeordneteMeldungM, pWillenserklaerungStatusM,
//                    KonstWeisungserfassungSicht.portalWeisungserfassung);
//        } else {
//            return aControllerStatus.stornierenAusfuehren(pZugeordneteMeldungM, pWillenserklaerungStatusM,
//                    KonstWeisungserfassungSicht.portalWeisungserfassung);
//        }
//    }
//
//    public String doAnmeldenAbmelden() {
//
//        if (!aFunktionen.pruefeStart("aAuswahl1")) {
//            return "aDlgFehler";
//        }
//
//        return aFunktionen.setzeEnde("aAnmeldenAbmelden", true, true);
//    }
//
//    /*************************aAuswahl1Beiratswahl*************************************/
//
//    public String doBeiratswahlZurueck() {
//        if (!aFunktionen.pruefeStart("aAuswahl1Beiratswahl")) {
//            return "aDlgFehler";
//        }
//
//        return aFunktionen.setzeEnde("aAuswahl1", true, true);
//
//    }
//
//    /*************************aAuswahl1Generalversammlung*************************************/
//
//    public String doGeneralversammlungZurueck() {
//        if (!aFunktionen.pruefeStart("aAuswahl1Generalversammlung")) {
//            return "aDlgFehler";
//        }
//
//        return aFunktionen.setzeEnde("aAuswahl1", true, true);
//
//    }
//
//    /*************************aAuswahl1Dialogveranstaltungen*************************************/
//
//    public String doDialogveranstaltungenZurueck() {
//        if (!aFunktionen.pruefeStart("aAuswahl1Dialogveranstaltungen")) {
//            return "aDlgFehler";
//        }
//
//        return aFunktionen.setzeEnde("aAuswahl1", true, true);
//
//    }
//
//    /********************Übergreifend nutzbar*******************************/
//
//    /**Abmelden*/
//    public String doAbmelden() {
//        aDlgVariablen.clearLogin();
//        return aFunktionen.waehleLogout();
//    }

}
