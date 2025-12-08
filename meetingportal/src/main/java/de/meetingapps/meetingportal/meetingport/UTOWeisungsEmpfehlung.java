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

import de.meetingapps.meetingportal.meetComBl.BlAbstimmungsVorschlagEmpfehlung;
import de.meetingapps.meetingportal.meetComEclM.EclAbstimmungMitVorschlagM;
import de.meetingapps.meetingportal.meetComEclM.EclDbM;
import de.meetingapps.meetingportal.meetComEclM.EclParamM;
import de.meetingapps.meetingportal.meetComEclM.EclUserLoginM;
import de.meetingapps.meetingportal.meetComEntities.EclAbstimmungsVorschlagEmpfehlung;
import de.meetingapps.meetingportal.meetComKonst.KonstNachrichtVerwendungsCode;
import de.meetingapps.meetingportal.meetComKonst.KonstStimmart;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

/**Bereiche:
 * 
 * 
 * Mandantenwechseln (gehe zu UControllerMenueMandant)
 */

@RequestScoped
@Named
public class UTOWeisungsEmpfehlung {

    @Inject
    private EclDbM eclDbM;
    @Inject
    private USession uSession;
    @Inject
    private XSessionVerwaltung xSessionVerwaltung;
    @Inject
    private EclParamM eclParamM;

    @Inject
    private UNachrichtSendenSession uNachrichtSendenSession;
    @Inject
    private UNachrichtSenden uNachrichtSenden;
    @Inject
    private EclUserLoginM eclUserLoginM;

    @Inject
    private UTOWeisungsEmpfehlungSession uTOWeisungsEmpfehlungSession;
    @Inject
    private EclAbstimmungMitVorschlagM eclAbstimmungMitVorschlagM;

    /**Initialisieren - vor Aufruf von UTOWeisungsEmpfehlung
     * 
     * Open/Close wird im aufrufenden Modul geregelt.*/
    public void init() {

        /**Anzuzeigende TOP-Liste mit Weisungsvorschlag aufbereiten*/
        List<EclAbstimmungMitVorschlagM> toMitVorschlagListe = new LinkedList<EclAbstimmungMitVorschlagM>();

        BlAbstimmungsVorschlagEmpfehlung blAbstimmungsVorschlagEmpfehlung = new BlAbstimmungsVorschlagEmpfehlung(true,
                eclDbM.getDbBundle());
        blAbstimmungsVorschlagEmpfehlung.erzeugeUebersicht();

        if (blAbstimmungsVorschlagEmpfehlung.pAngezeigteAbstimmungen != null) {
            for (int i = 0; i < blAbstimmungsVorschlagEmpfehlung.pAngezeigteAbstimmungen.length; i++) {
                toMitVorschlagListe
                        .add(new EclAbstimmungMitVorschlagM(blAbstimmungsVorschlagEmpfehlung.pAngezeigteAbstimmungen[i],
                                blAbstimmungsVorschlagEmpfehlung.rcEmpfehlungsArray[i], 0));
            }
        }

        uTOWeisungsEmpfehlungSession.setToMitVorschlagListe(toMitVorschlagListe);

        uTOWeisungsEmpfehlungSession.auswahlInit();
        uTOWeisungsEmpfehlungSession.setAuswahlMoeglich(true);
    }

    public String doNeuerTOP() {
        if (eclUserLoginM.pruefe_govVal_admin() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uTOWeisungsEmpfehlung", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }

        /**Alle Selektierten zurücksetzen*/
        setzeAusgewaehlteTOPsZurueck();

        uTOWeisungsEmpfehlungSession.auswahlInit();
        uTOWeisungsEmpfehlungSession.setAusgewaehltNeuerTOP(true);

        eclAbstimmungMitVorschlagM.init();
        return xSessionVerwaltung.setzeUEnde("uTOWeisungsEmpfehlung", true, false, eclUserLoginM.getKennung());
    }

    public String doTOPSpeichernOhneNachricht() {
        if (eclUserLoginM.pruefe_govVal_admin() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uTOWeisungsEmpfehlung", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }
        if (pruefeTOPKorrektEingegeben() < 0) {
            uSession.setFehlermeldung("Bitte TOP und Bezeichnung eingeben");
            return xSessionVerwaltung.setzeEnde();
        }
        neuenTOPSpeichern();
        return xSessionVerwaltung.setzeUEnde("uTOWeisungsEmpfehlung", true, false, eclUserLoginM.getKennung());
    }

    public String doTOPSpeichernMitNachricht() {
        if (eclUserLoginM.pruefe_govVal_admin() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uTOWeisungsEmpfehlung", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }
        if (pruefeTOPKorrektEingegeben() < 0) {
            uSession.setFehlermeldung("Bitte TOP und Bezeichnung eingeben");
            return xSessionVerwaltung.setzeEnde();
        }
        neuenTOPSpeichern();
        uTOWeisungsEmpfehlungSession.auswahlInit();
        uTOWeisungsEmpfehlungSession.setAusgewaehltNeuerTOPNachricht(true);

        uNachrichtSenden.initialisieren(eclParamM.getClGlobalVar().mandant, 0,
                KonstNachrichtVerwendungsCode.insti_weisungsEmpfehlung);
        uNachrichtSenden.initialisiereAusgangsXHTML("uTOWeisungsEmpfehlung");
        uNachrichtSenden.initialisiereInstiEmpfaengerMitBestandAusgewaehlt();
        return xSessionVerwaltung.setzeUEnde("uTOWeisungsEmpfehlung", true, false, eclUserLoginM.getKennung());
    }

    public String doTOPEmpfehlungAendernSenden() {
        if (eclUserLoginM.pruefe_govVal_admin() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uTOWeisungsEmpfehlung", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }
        if (uNachrichtSenden.pruefeNachrichtEingabe() == false) {
            return xSessionVerwaltung.setzeEnde();
        }
        eclDbM.openAll();
        eclDbM.openWeitere();

        /**Abstimmungsvorschlagsempfehlung speichern, und dessen ident in Mail-Parameter1 speichern*/
        BlAbstimmungsVorschlagEmpfehlung blAbstimmungsVorschlagEmpfehlung = new BlAbstimmungsVorschlagEmpfehlung(true,
                eclDbM.getDbBundle());
        List<Integer> abstimmungsIdentFuerEmpfehlung = new LinkedList<Integer>();
        List<Integer> empfehlung = new LinkedList<Integer>();
        List<Integer> weisungsIdent = new LinkedList<Integer>();

        List<EclAbstimmungMitVorschlagM> toMitVorschlagListe = uTOWeisungsEmpfehlungSession.getToMitVorschlagListe();
        for (int i = 0; i < toMitVorschlagListe.size(); i++) {
            EclAbstimmungMitVorschlagM lAbstimmungMitVorschlagM = toMitVorschlagListe.get(i);
            if (lAbstimmungMitVorschlagM.isAusgewaehlt()) {
                abstimmungsIdentFuerEmpfehlung.add(lAbstimmungMitVorschlagM.getIdent());
                empfehlung.add(KonstStimmart.getIntVonTextKurz(lAbstimmungMitVorschlagM.getVorschlag()));
                weisungsIdent.add(lAbstimmungMitVorschlagM.getIdentWeisungssatz());
            }
        }

        blAbstimmungsVorschlagEmpfehlung.updateAbstimmungsVorschlagFuerAbstimmungen(abstimmungsIdentFuerEmpfehlung,
                weisungsIdent, empfehlung);

        blAbstimmungsVorschlagEmpfehlung.legeAbstimmungsVorschlagEmpfehlungAn(uNachrichtSendenSession.getBetreff(),
                abstimmungsIdentFuerEmpfehlung, empfehlung, eclUserLoginM.getUserLoginIdent());
        uNachrichtSendenSession.setParameter1(
                Integer.toString(blAbstimmungsVorschlagEmpfehlung.rcAbstimmungsVorschlagEmpfehlung.ident));
        uNachrichtSendenSession.setParameter2(eclDbM.getDbBundle().getSchemaMandant());
        EclAbstimmungsVorschlagEmpfehlung eclAbstimmungsVorschlagEmpfehlung = blAbstimmungsVorschlagEmpfehlung.rcAbstimmungsVorschlagEmpfehlung;

        /**Mails speichern bzw. senden*/
        uNachrichtSenden.senden();

        /**Nun Empfehlung noch mit Mailident Updaten*/
        eclAbstimmungsVorschlagEmpfehlung.identMail = uNachrichtSendenSession.getMailIdent();
        blAbstimmungsVorschlagEmpfehlung.updateAbstimmungsVorschlagEmpfehlung(eclAbstimmungsVorschlagEmpfehlung);

        init();

        eclDbM.closeAll();
        return xSessionVerwaltung.setzeUEnde("uTOWeisungsEmpfehlung", true, false, eclUserLoginM.getKennung());
    }

    private int pruefeTOPKorrektEingegeben() {
        if (eclAbstimmungMitVorschlagM.getNummer().isEmpty()
                || eclAbstimmungMitVorschlagM.getAnzeigeBezeichnungKurz().isEmpty()) {
            return -1;
        }
        return 1;
    }

    private int neuenTOPSpeichern() {
        eclDbM.openAll();
        eclDbM.openWeitere();
        BlAbstimmungsVorschlagEmpfehlung blAbstimmungsVorschlagEmpfehlung = new BlAbstimmungsVorschlagEmpfehlung(true,
                eclDbM.getDbBundle());
        int[] gattungen = { 0, 1, 1, 1, 1 };
        blAbstimmungsVorschlagEmpfehlung.legeNeueAbstimmungAn(eclAbstimmungMitVorschlagM.getNummer(),
                eclAbstimmungMitVorschlagM.getNummerindex(), eclAbstimmungMitVorschlagM.getAnzeigeBezeichnungKurz(),
                eclAbstimmungMitVorschlagM.getAnzeigeBezeichnungKurzEN(), gattungen, false,
                KonstStimmart.getIntVonTextKurz(eclAbstimmungMitVorschlagM.getVorschlag()));
        eclAbstimmungMitVorschlagM.setIdent(blAbstimmungsVorschlagEmpfehlung.rcAbstimmungsIdent);//Wird für doTOPEmpfehlungSenden benötigt

        init();
        eclDbM.closeAll();
        return 1;
    }

    public String doTOPEmpfehlungSenden() {
        if (eclUserLoginM.pruefe_govVal_admin() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uTOWeisungsEmpfehlung", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }
        if (uNachrichtSenden.pruefeNachrichtEingabe() == false) {
            return xSessionVerwaltung.setzeEnde();
        }

        eclDbM.openAll();
        eclDbM.openWeitere();
        /**Abstimmungsvorschlagsempfehlung speichern, und dessen ident in Mail-Parameter1 speichern*/
        BlAbstimmungsVorschlagEmpfehlung blAbstimmungsVorschlagEmpfehlung = new BlAbstimmungsVorschlagEmpfehlung(true,
                eclDbM.getDbBundle());
        List<Integer> abstimmungsIdentFuerEmpfehlung = new LinkedList<Integer>();
        List<Integer> empfehlung = new LinkedList<Integer>();
        abstimmungsIdentFuerEmpfehlung.add(eclAbstimmungMitVorschlagM.getIdent());
        empfehlung.add(KonstStimmart.getIntVonTextKurz(eclAbstimmungMitVorschlagM.getVorschlag()));
        blAbstimmungsVorschlagEmpfehlung.legeAbstimmungsVorschlagEmpfehlungAn(uNachrichtSendenSession.getBetreff(),
                abstimmungsIdentFuerEmpfehlung, empfehlung, eclUserLoginM.getUserLoginIdent());
        uNachrichtSendenSession.setParameter1(
                Integer.toString(blAbstimmungsVorschlagEmpfehlung.rcAbstimmungsVorschlagEmpfehlung.ident));
        uNachrichtSendenSession.setParameter2(eclDbM.getDbBundle().getSchemaMandant());
        EclAbstimmungsVorschlagEmpfehlung eclAbstimmungsVorschlagEmpfehlung = blAbstimmungsVorschlagEmpfehlung.rcAbstimmungsVorschlagEmpfehlung;

        /**Mails speichern bzw. senden*/
        uNachrichtSenden.senden();

        /**Nun Empfehlung noch mit Mailident Updaten*/
        eclAbstimmungsVorschlagEmpfehlung.identMail = uNachrichtSendenSession.getMailIdent();
        blAbstimmungsVorschlagEmpfehlung.updateAbstimmungsVorschlagEmpfehlung(eclAbstimmungsVorschlagEmpfehlung);

        init();

        eclDbM.closeAll();
        return xSessionVerwaltung.setzeUEnde("uTOWeisungsEmpfehlung", true, false, eclUserLoginM.getKennung());
    }

    public String doAendernTOP() {
        if (eclUserLoginM.pruefe_govVal_admin() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uTOWeisungsEmpfehlung", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }

        if (liefereAnzahlAusgewaehlterTope() != 1) {
            uSession.setFehlermeldung("Bitte genau 1 TOP zum Ändern auswählen");
            return xSessionVerwaltung.setzeEnde();
        }

        uTOWeisungsEmpfehlungSession.auswahlInit();
        uTOWeisungsEmpfehlungSession.setAusgewaehltAendernTOP(true);

        eclDbM.openAll();
        eclDbM.openWeitere();
        BlAbstimmungsVorschlagEmpfehlung blAbstimmungsVorschlagEmpfehlung = new BlAbstimmungsVorschlagEmpfehlung(true,
                eclDbM.getDbBundle());
        blAbstimmungsVorschlagEmpfehlung.leseAbstimmung(liefereEinzigenAusgewaehltenTop());
        eclAbstimmungMitVorschlagM.copyFrom(blAbstimmungsVorschlagEmpfehlung.rcAbstimmung, 0, 0);
        eclDbM.closeAll();

        return xSessionVerwaltung.setzeUEnde("uTOWeisungsEmpfehlung", true, false, eclUserLoginM.getKennung());
    }

    public String doAendernTOPSpeichern() {
        if (eclUserLoginM.pruefe_govVal_admin() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uTOWeisungsEmpfehlung", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }

        eclDbM.openAll();
        eclDbM.openWeitere();
        BlAbstimmungsVorschlagEmpfehlung blAbstimmungsVorschlagEmpfehlung = new BlAbstimmungsVorschlagEmpfehlung(true,
                eclDbM.getDbBundle());

        blAbstimmungsVorschlagEmpfehlung.aendereAbstimmung(eclAbstimmungMitVorschlagM.getIdent(),
                eclAbstimmungMitVorschlagM.getNummer(), eclAbstimmungMitVorschlagM.getNummerindex(),
                eclAbstimmungMitVorschlagM.getAnzeigeBezeichnungKurz(),
                eclAbstimmungMitVorschlagM.getAnzeigeBezeichnungKurzEN());

        init();
        eclDbM.closeAll();

        return xSessionVerwaltung.setzeUEnde("uTOWeisungsEmpfehlung", true, false, eclUserLoginM.getKennung());
    }

    public String doEmpfehlungAendern() {
        if (eclUserLoginM.pruefe_govVal_admin() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uTOWeisungsEmpfehlung", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }

        if (liefereAnzahlAusgewaehlterTope() < 1) {
            uSession.setFehlermeldung("Bitte mindestens 1 TOP zum Ändern auswählen");
            return xSessionVerwaltung.setzeEnde();
        }

        uTOWeisungsEmpfehlungSession.auswahlInit();
        uTOWeisungsEmpfehlungSession.setAusgewaehltEmpfehlungAendern(true);

        uNachrichtSenden.initialisieren(eclParamM.getClGlobalVar().mandant, 0,
                KonstNachrichtVerwendungsCode.insti_weisungsEmpfehlung);
        uNachrichtSenden.initialisiereAusgangsXHTML("uTOWeisungsEmpfehlung");
        uNachrichtSenden.initialisiereInstiEmpfaengerMitBestandAusgewaehlt();

        return xSessionVerwaltung.setzeUEnde("uTOWeisungsEmpfehlung", true, false, eclUserLoginM.getKennung());
    }

    public String doAbbrechen() {
        if (eclUserLoginM.pruefe_govVal_admin() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uTOWeisungsEmpfehlung", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }

        eclDbM.openAll();
        init();
        eclDbM.closeAll();
        return xSessionVerwaltung.setzeUEnde("uTOWeisungsEmpfehlung", true, false, eclUserLoginM.getKennung());

    }

    public String doZurueck() {
        if (eclUserLoginM.pruefe_uportal_dLoginStandardAdmin() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uTOWeisungsEmpfehlung", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }

        eclDbM.openAll();
        init();
        eclDbM.closeAll();
        return xSessionVerwaltung.setzeUEnde("uMenue", true, false, eclUserLoginM.getKennung());

    }

    private void setzeAusgewaehlteTOPsZurueck() {
        List<EclAbstimmungMitVorschlagM> toMitVorschlagListe = uTOWeisungsEmpfehlungSession.getToMitVorschlagListe();
        for (int i = 0; i < toMitVorschlagListe.size(); i++) {
            toMitVorschlagListe.get(i).setAusgewaehlt(false);
        }

    }

    private int liefereAnzahlAusgewaehlterTope() {
        int anzGef = 0;
        List<EclAbstimmungMitVorschlagM> toMitVorschlagListe = uTOWeisungsEmpfehlungSession.getToMitVorschlagListe();
        for (int i = 0; i < toMitVorschlagListe.size(); i++) {
            if (toMitVorschlagListe.get(i).isAusgewaehlt()) {
                anzGef++;
            }
        }
        return anzGef;
    }

    /**liefert abstimmungsIdent zurück*/
    private int liefereEinzigenAusgewaehltenTop() {
        List<EclAbstimmungMitVorschlagM> toMitVorschlagListe = uTOWeisungsEmpfehlungSession.getToMitVorschlagListe();
        for (int i = 0; i < toMitVorschlagListe.size(); i++) {
            if (toMitVorschlagListe.get(i).isAusgewaehlt()) {
                return toMitVorschlagListe.get(i).getIdent();
            }
        }
        return -1;
    }

}
