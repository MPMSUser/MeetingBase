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

import de.meetingapps.meetingportal.meetComBlManaged.BlMFuelleEclMAusPufferOderDBEE;
import de.meetingapps.meetingportal.meetComBlManaged.BlMNachrichten;
import de.meetingapps.meetingportal.meetComEclM.EclDbM;
import de.meetingapps.meetingportal.meetComEclM.EclNachrichtAnhangM;
import de.meetingapps.meetingportal.meetComEclM.EclNachrichtM;
import de.meetingapps.meetingportal.meetComEclM.EclParamM;
import de.meetingapps.meetingportal.meetComEclM.EclUserLoginM;
import de.meetingapps.meetingportal.meetComKonst.KonstNachrichtVerwendungsCode;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

/**Allgemeiner Controller für die "Nachrichten-Empfangsbereiche"*/

@RequestScoped
@Named
public class UNachrichtEmpfangen {
    @Inject
    private UNachrichtEmpfangenSession uNachrichtEmpfangenSession;
    @Inject
    private BlMNachrichten blMNachrichten;
    @Inject
    private XSessionVerwaltung xSessionVerwaltung;
    @Inject
    private EclDbM eclDbM;
    @Inject
    private EclUserLoginM eclUserLoginM;
    @Inject
    private UWeisungsEmpfehlungAntwort uWeisungsEmpfehlungAntwort;
    @Inject
    private BlMFuelleEclMAusPufferOderDBEE blMFuelleEclMAusPufferOderDBEE;
    @Inject
    private USession uSession;
    @Inject
    private EclParamM eclParamM;

    /********************Initialisierung*****************************************************************/
    /**EclDbM muß in aufrufender Funktion geöffnet sein*/
    public void init(String pAufrufSeite, boolean pMandantGewaehlt) {
        uNachrichtEmpfangenSession.setAufrufSeite(pAufrufSeite);
        blMNachrichten.vorbereitenNachrichtenEmpfangsliste(eclDbM.getDbBundle());
    }

    /****************Nachrichten-Liste****************************************/
    public String doEmpfangenNachrichtenRefresh() {
        if (!xSessionVerwaltung.pruefeUStart(uNachrichtEmpfangenSession.getAufrufSeite(), "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }
        eclDbM.openAll();
        eclDbM.openWeitere();
        blMNachrichten.vorbereitenNachrichtenEmpfangslisteRefresh(eclDbM.getDbBundle());
        eclDbM.closeAll();
        return xSessionVerwaltung.setzeUEnde(uNachrichtEmpfangenSession.getAufrufSeite(), true, false,
                eclUserLoginM.getKennung());
    }

    public String doEmpfangenNachrichtenDetails(EclNachrichtM pEclNachrichtM) {
        if (!xSessionVerwaltung.pruefeUStart(uNachrichtEmpfangenSession.getAufrufSeite(), "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }
        eclDbM.openAll();
        eclDbM.openWeitere();
        blMNachrichten.nachrichtDetailEinlesen(eclDbM.getDbBundle(), pEclNachrichtM.getIdent(), pEclNachrichtM.getDbServerIdent());
        eclDbM.closeAll();

        return xSessionVerwaltung.setzeUEnde(uNachrichtEmpfangenSession.getAufrufSeite(), true, false,
                eclUserLoginM.getKennung());
    }

    public String doEmpfangenNachrichtenEinblenden(EclNachrichtM pEclNachrichtM) {
        if (!xSessionVerwaltung.pruefeUStart(uNachrichtEmpfangenSession.getAufrufSeite(), "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }
        eclDbM.openAll();
        eclDbM.openWeitere();
        blMNachrichten.nachrichtEinblendenBeimEmpfaenger(eclDbM.getDbBundle(), pEclNachrichtM.getIdent(), pEclNachrichtM.getDbServerIdent());
        eclDbM.closeAll();

        return xSessionVerwaltung.setzeUEnde(uNachrichtEmpfangenSession.getAufrufSeite(), true, false,
                eclUserLoginM.getKennung());
    }

    public String doEmpfangenNachrichtenAusblenden(EclNachrichtM pEclNachrichtM) {
        if (!xSessionVerwaltung.pruefeUStart(uNachrichtEmpfangenSession.getAufrufSeite(), "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }
        eclDbM.openAll();
        eclDbM.openWeitere();
        blMNachrichten.nachrichtAusblendenBeimEmpfaenger(eclDbM.getDbBundle(), pEclNachrichtM.getIdent(), pEclNachrichtM.getDbServerIdent());
        eclDbM.closeAll();

        return xSessionVerwaltung.setzeUEnde(uNachrichtEmpfangenSession.getAufrufSeite(), true, false,
                eclUserLoginM.getKennung());
    }

    public String doEmpfangenNachrichtenBearbeitet(EclNachrichtM pEclNachrichtM) {
        if (!xSessionVerwaltung.pruefeUStart(uNachrichtEmpfangenSession.getAufrufSeite(), "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }
        eclDbM.openAll();
        eclDbM.openWeitere();
        blMNachrichten.nachrichtSetzeBearbeitetBeimEmpfaenger(eclDbM.getDbBundle(), pEclNachrichtM.getIdent(), pEclNachrichtM.getDbServerIdent());
        eclDbM.closeAll();

        return xSessionVerwaltung.setzeUEnde(uNachrichtEmpfangenSession.getAufrufSeite(), true, false,
                eclUserLoginM.getKennung());
    }

    public String doEmpfangenNachrichtenUnbearbeitet(EclNachrichtM pEclNachrichtM) {
        if (!xSessionVerwaltung.pruefeUStart(uNachrichtEmpfangenSession.getAufrufSeite(), "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }
        eclDbM.openAll();
        eclDbM.openWeitere();
        blMNachrichten.nachrichtSetzeUnbearbeitetBeimEmpfaenger(eclDbM.getDbBundle(), pEclNachrichtM.getIdent(), pEclNachrichtM.getDbServerIdent());
        eclDbM.closeAll();

        return xSessionVerwaltung.setzeUEnde(uNachrichtEmpfangenSession.getAufrufSeite(), true, false,
                eclUserLoginM.getKennung());
    }

    public String doTechnischeNachrichtBearbeiten(EclNachrichtM pEclNachrichtM) {
        if (eclUserLoginM.pruefe_govVal_insti() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart(uNachrichtEmpfangenSession.getAufrufSeite(), "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }

        switch (pEclNachrichtM.getVerwendungsCode()) {
        case KonstNachrichtVerwendungsCode.insti_weisungsEmpfehlung: {
            eclDbM.openAll();
            eclDbM.closeAll();
            /*Mandant vorbelegen*/
            eclDbM.getDbBundle().setzeMandantAusSchemaMandant(pEclNachrichtM.getParameter2());
            eclDbM.openAll();
            blMFuelleEclMAusPufferOderDBEE.fuelleAlleVariablenBeiStart();
            eclDbM.closeAll();

            /*Weisungs-Antwort initialisieren*/
            eclDbM.openAll();
            eclDbM.openWeitere();
            boolean bRC = uWeisungsEmpfehlungAntwort.init(pEclNachrichtM, uNachrichtEmpfangenSession.getAufrufSeite());

            if (bRC == false) {
                uSession.setFehlermeldung("Keine Bestände für diese HV vorhanden");
                String zielSeite = uNachrichtEmpfangenSession.getAufrufSeite();
                if (zielSeite.equals("uMenueAuswahlMandant")) {
                    eclParamM.getClGlobalVar().mandant = 0;
                    eclParamM.getClGlobalVar().hvJahr = 0;
                    eclParamM.getClGlobalVar().hvNummer = "";
                    eclParamM.getClGlobalVar().datenbereich = "";
                    blMNachrichten.vorbereitenNachrichtenEmpfangslisteRefresh(eclDbM.getDbBundle());
                } else {
                    blMNachrichten.vorbereitenNachrichtenEmpfangslisteRefresh(eclDbM.getDbBundle());
                }
                eclDbM.closeAll();
                return xSessionVerwaltung.setzeUEnde(zielSeite, false, false, eclUserLoginM.getKennung());
            }

            eclDbM.closeAll();
            return xSessionVerwaltung.setzeUEnde("uWeisungsEmpfehlungAntwort", true, false, eclUserLoginM.getKennung());
        }
        }

        return xSessionVerwaltung.setzeUEnde(uNachrichtEmpfangenSession.getAufrufSeite(), true, false,
                eclUserLoginM.getKennung());
    }

    /*******************Detailsicht**************************************************************/
    public String doDetailsAusblenden() {
        blMNachrichten.setDetailSichtVerfuegbar(false);
        return "";
    }

    public String doAnzeigenAnhang(EclNachrichtAnhangM pEclNachrichtAnhangM) {
        if (!xSessionVerwaltung.pruefeUStart(uNachrichtEmpfangenSession.getAufrufSeite(), "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }
        eclDbM.openAll();
        eclDbM.openWeitere();
        blMNachrichten.zeigeAnhangAn(eclDbM.getDbBundle(), pEclNachrichtAnhangM);
        eclDbM.closeAll();

        return "";
    }

}
