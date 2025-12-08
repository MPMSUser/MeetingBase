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
import de.meetingapps.meetingportal.meetComAllg.CaString;
import de.meetingapps.meetingportal.meetComBa.BaMailM;
import de.meetingapps.meetingportal.meetComBl.BlNachrichten;
import de.meetingapps.meetingportal.meetComBlManaged.BlMNachrichten;
import de.meetingapps.meetingportal.meetComEclM.EclDbM;
import de.meetingapps.meetingportal.meetComEclM.EclNachrichtAnhangM;
import de.meetingapps.meetingportal.meetComEclM.EclNachrichtBasisTextM;
import de.meetingapps.meetingportal.meetComEclM.EclNachrichtEmpfaengerInstiM;
import de.meetingapps.meetingportal.meetComEclM.EclNachrichtEmpfaengerUserM;
import de.meetingapps.meetingportal.meetComEclM.EclNachrichtVerwendungsCodeM;
import de.meetingapps.meetingportal.meetComEclM.EclUserLoginM;
import de.meetingapps.meetingportal.meetComEntities.EclNachrichtAnhang;
import de.meetingapps.meetingportal.meetComEntities.EclNachrichtVerwendungsCode;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.Part;

@RequestScoped
@Named
public class UNachrichtSenden {

    private int logDrucken = 3;

    @Inject
    private EclDbM eclDbM;
    @Inject
    private USession uSession;
    @Inject
    private XSessionVerwaltung xSessionVerwaltung;
    @Inject
    private EclUserLoginM eclUserLoginM;

    @Inject
    private BaMailM baMailM;
    @Inject
    private UNachrichtSendenSession uNachrichtSendenSession;

    @Inject
    private BlMNachrichten blMNachrichten;

    /**Da die Mail-Routine in verschiedenen XHTMLS eingebettet wird, und zwar einschließlich
     * der Hochlade- und Basis-Übernahme-usw. Buttons, muß hier - zur Abprüfung - das
     * Ausgangs-XHTMLs vorbelegt werden.
     * @param pAusgangsXHTML
     */
    public void initialisiereAusgangsXHTML(String pAusgangsXHTML) {
        uNachrichtSendenSession.setAufrufMaske(pAusgangsXHTML);
    }

    /**pVerwendungscode
     * 0 keine Vorbelegung
     * >0 wird mit entsprechendem Verwendungscode und ggf. Basistext vorbelegt
     */
    public void initialisieren(int pMandant, int antwortAufNachricht, int pVerwendungscode) {
        eclDbM.openAll();
        eclDbM.openWeitere();

        BlNachrichten blNachrichten = new BlNachrichten(true, eclDbM.getDbBundle());
        blNachrichten.senden_datenVorbereiten(pMandant);

        uNachrichtSendenSession.clear();

        uNachrichtSendenSession.setMandant(pMandant);
        uNachrichtSendenSession.setAntwortAufNachricht(antwortAufNachricht);

        /*basisTexte*/
        List<EclNachrichtBasisTextM> lNachrichtBasisTextMListe = new LinkedList<EclNachrichtBasisTextM>();
        for (int i = 0; i < blNachrichten.rcNachrichtBasisTextList.size(); i++) {
            lNachrichtBasisTextMListe.add(new EclNachrichtBasisTextM(blNachrichten.rcNachrichtBasisTextList.get(i)));
        }
        uNachrichtSendenSession.setBasisTexte(lNachrichtBasisTextMListe);

        /*verwendungsCodes*/
        List<EclNachrichtVerwendungsCodeM> lNachrichtVerwendungsCodeMListe = new LinkedList<EclNachrichtVerwendungsCodeM>();
        for (int i = 0; i < blNachrichten.rcNachrichtVerwendungsCodeList.size(); i++) {
            EclNachrichtVerwendungsCode lNachrichtVerwendungsCode = blNachrichten.rcNachrichtVerwendungsCodeList.get(i);
            if (lNachrichtVerwendungsCode.ident < 100000) {
                lNachrichtVerwendungsCodeMListe.add(new EclNachrichtVerwendungsCodeM(lNachrichtVerwendungsCode));
            }
        }
        uNachrichtSendenSession.setVerwendungsCodes(lNachrichtVerwendungsCodeMListe);

        /*empfaengerBO*/
        List<EclNachrichtEmpfaengerUserM> lNachrichtEmpfaengerUserMListe = new LinkedList<EclNachrichtEmpfaengerUserM>();
        for (int i = 0; i < blNachrichten.rcEmpfaengerBO.size(); i++) {
            lNachrichtEmpfaengerUserMListe.add(new EclNachrichtEmpfaengerUserM(blNachrichten.rcEmpfaengerBO.get(i)));
        }
        uNachrichtSendenSession.setEmpfaengerBO(lNachrichtEmpfaengerUserMListe);

        /*empfaengerEmittenten*/
        lNachrichtEmpfaengerUserMListe = new LinkedList<EclNachrichtEmpfaengerUserM>();
        if (eclUserLoginM.getGehoertZuInsti() == -1) {
            /**Emittenten nur von BO aus erreichbar*/
            for (int i = 0; i < blNachrichten.rcEmpfaengerEmittent.size(); i++) {
                lNachrichtEmpfaengerUserMListe
                        .add(new EclNachrichtEmpfaengerUserM(blNachrichten.rcEmpfaengerEmittent.get(i)));
            }
        }
        uNachrichtSendenSession.setEmpfaengerEmittenten(lNachrichtEmpfaengerUserMListe);

        /*empfaengerDritte*/
        lNachrichtEmpfaengerUserMListe = new LinkedList<EclNachrichtEmpfaengerUserM>();
        for (int i = 0; i < blNachrichten.rcEmpfaengerDritte.size(); i++) {
            lNachrichtEmpfaengerUserMListe
                    .add(new EclNachrichtEmpfaengerUserM(blNachrichten.rcEmpfaengerDritte.get(i)));
        }
        uNachrichtSendenSession.setEmpfaengerDritte(lNachrichtEmpfaengerUserMListe);

        /*empfaengerInsti*/
        List<EclNachrichtEmpfaengerInstiM> lNachrichtEmpfaengerInstiMListe = new LinkedList<EclNachrichtEmpfaengerInstiM>();
        if (eclUserLoginM.getGehoertZuInsti() == -1) {
            /**Insti nur von BO aus erreichbar*/
            for (int i = 0; i < blNachrichten.rcEmpfaengerInsti.size(); i++) {
                lNachrichtEmpfaengerInstiMListe
                        .add(new EclNachrichtEmpfaengerInstiM(blNachrichten.rcEmpfaengerInsti.get(i)));
            }
        }
        uNachrichtSendenSession.setEmpfaengerInsti(lNachrichtEmpfaengerInstiMListe);

        if (pVerwendungscode != 0) {
            verwendungsCodeUebernehmen(pVerwendungscode);
            uNachrichtSendenSession.setAuswahlVerwendungsCodeMoeglich(false);
        } else {
            uNachrichtSendenSession.setAuswahlVerwendungsCodeMoeglich(true);
        }
        eclDbM.closeAll();
    }

    public String doVerwendungsCodeUebernehmen() {
        if (eclUserLoginM.pruefe_uportal_mailSenden() == false) {
            return "";
        }
        if (uNachrichtSendenSession.getAusgewaehlterVerwendungsCode() == null
                || uNachrichtSendenSession.getAusgewaehlterVerwendungsCode().isEmpty()) {
            uSession.setFehlermeldung("Verwendungscode auswählen");
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uNachrichtSenden", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }

        int lVerwendungsIdent = Integer.parseInt(uNachrichtSendenSession.getAusgewaehlterVerwendungsCode());

        verwendungsCodeUebernehmen(lVerwendungsIdent);

        return xSessionVerwaltung.setzeUEnde("uNachrichtSenden", true, false, eclUserLoginM.getKennung());
    }

    /**Setze Empfänger - "Alle Instis ausgewählt"*/
    public void initialisiereInstiEmpfaengerAlleAusgewaehlt() {
        uNachrichtSendenSession.setEmpfaengerInstiAlleAusgewaehlt(true);
    }

    /**Setze Empfänger - "Alle Instis mit Bestandausgewählt"*/
    public void initialisiereInstiEmpfaengerMitBestandAusgewaehlt() {
        uNachrichtSendenSession.setEmpfaengerInstiAlleMitBestandAusgewaehlt(true);
    }

    /**Setzt Verwendungscode und übernimmt ggf. zugehörigen Basisbetreff/Basistext*/
    private void verwendungsCodeUebernehmen(int lVerwendungsIdent) {
        int lBasisIdent = 0;
        for (int i = 0; i < uNachrichtSendenSession.getVerwendungsCodes().size(); i++) {
            EclNachrichtVerwendungsCodeM lVerwendungsCode = uNachrichtSendenSession.getVerwendungsCodes().get(i);
            if (lVerwendungsCode.getIdent() == lVerwendungsIdent) {
                lBasisIdent = lVerwendungsCode.getIdentNachrichtBasisText();
            }
        }
        CaBug.druckeLog("lVerwendungsIdent=" + lVerwendungsIdent, logDrucken, 10);
        uNachrichtSendenSession.setAusgewaehlterVerwendungsCode(Integer.toString(lVerwendungsIdent));

        if (lBasisIdent != 0) {
            for (int i = 0; i < uNachrichtSendenSession.getBasisTexte().size(); i++) {
                EclNachrichtBasisTextM lBasisText = uNachrichtSendenSession.getBasisTexte().get(i);
                if (lBasisText.getIdent() == lBasisIdent) {
                    uNachrichtSendenSession.setBetreff(lBasisText.getBetreff());
                    uNachrichtSendenSession.setMailText(lBasisText.getMailText());
                }
            }
        }

    }

    public String doBasisTextUebernehmen() {
        if (eclUserLoginM.pruefe_uportal_mailSenden() == false) {
            return "";
        }
        if (uNachrichtSendenSession.getAusgewaehlterBasisText() == null
                || uNachrichtSendenSession.getAusgewaehlterBasisText().isEmpty()) {
            uSession.setFehlermeldung("Basistext auswählen");
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uNachrichtSenden", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }

        int lBasisIdent = Integer.parseInt(uNachrichtSendenSession.getAusgewaehlterBasisText());

        for (int i = 0; i < uNachrichtSendenSession.getBasisTexte().size(); i++) {
            EclNachrichtBasisTextM lBasisText = uNachrichtSendenSession.getBasisTexte().get(i);
            if (lBasisText.getIdent() == lBasisIdent) {
                uNachrichtSendenSession.setBetreff(lBasisText.getBetreff());
                uNachrichtSendenSession.setMailText(lBasisText.getMailText());
            }
        }
        return xSessionVerwaltung.setzeUEnde("uNachrichtSenden", true, false, eclUserLoginM.getKennung());
    }

    public void pruefeDatei(FacesContext ctx, UIComponent comp, Object value) {
        CaBug.druckeLog("", logDrucken, 10);
        //		if (((Part)value).getSize()>10000000) {
        //			new ValidatorException(new FacesMessage("Datei zu groß! Maximal 10MB erlaubt"));
        //		}
    }

    public String doDateiAnhaengen() {
        if (eclUserLoginM.pruefe_uportal_mailSenden() == false) {
            return "";
        }
        if (pruefenDateiAnhang() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uNachrichtSenden", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }
        eclDbM.openAll();
        eclDbM.openWeitere();
        dateiAnhaengen();
        eclDbM.closeAll();
        return xSessionVerwaltung.setzeUEnde("uNachrichtSenden", true, false, eclUserLoginM.getKennung());
    }

    private boolean pruefenDateiAnhang() {
        CaBug.druckeLog("", logDrucken, 10);
        if (uNachrichtSendenSession.getDateiAnhangBeschreibung().length() > 80) {
            uSession.setFehlermeldung("Anhang Beschreibung zu lang - maximal 80 Zeichen zulässig");
            return false;
        }
        if (uNachrichtSendenSession.getDateiAnhang() == null) {
            uSession.setFehlermeldung("Bitte Datei auswählen");
            return false;
        }
        if (uNachrichtSendenSession.getDateiAnhang().getSubmittedFileName().length() > 100) {
            uSession.setFehlermeldung("Dateiname zu lang - maximal 100 Zeichen zulässig");
            return false;
        }
        if (((Part) uNachrichtSendenSession.getDateiAnhang()).getSize() > 10000000) {
            uSession.setFehlermeldung("Datei zu lang - maximal 10 MB zulässig");
            return false;
        }

        return true;
    }

    private void dateiAnhaengen() {
        BlNachrichten blNachricht = new BlNachrichten(true, eclDbM.getDbBundle());
        if (uNachrichtSendenSession.getMailIdent() == 0) {
            uNachrichtSendenSession.setMailIdent(blNachricht.neueMailIdent());
        }
        EclNachrichtAnhang lNachrichtAnhang = blNachricht.erzeugeAnhang(uNachrichtSendenSession.getDateiAnhang(),
                uNachrichtSendenSession.getDateiAnhangBeschreibung(), uNachrichtSendenSession.getMailIdent());

        uNachrichtSendenSession.getAnhangListe().add(new EclNachrichtAnhangM(lNachrichtAnhang));
        uNachrichtSendenSession.setDateiAnhang(null);
        uNachrichtSendenSession.setDateiAnhangBeschreibung("");
    }

    private List<Integer> empfaengerListe = null;
    private List<Integer> empfaengerInstiListe = null;

    /**Belegt empfaengerListe und empfaengerInstiListe*/
    public boolean pruefeNachrichtEingabe() {
        if (eclUserLoginM.pruefe_uportal_mailSenden() == false) {
            return false;
        }
        if (uNachrichtSendenSession.getDateiAnhang() != null) {
            if (pruefenDateiAnhang() == false) {
                return false;
            }
        }
        if (uNachrichtSendenSession.getBetreff().length() > 80) {
            uSession.setFehlermeldung("Betreff zu lang - maximal 80 Zeichen zulässig");
            return false;
        }
        if (uNachrichtSendenSession.getMailText().length() > 2000) {
            uSession.setFehlermeldung("Nachrichten-Text zu lang - maximal 2000 Zeichen zulässig");
            return false;
        }

        /*Empfänger-Liste erstellen und prüfen*/
        empfaengerListe = new LinkedList<Integer>();
        haengeUserAn(empfaengerListe, uNachrichtSendenSession.getEmpfaengerBO(), false);
        haengeUserAn(empfaengerListe, uNachrichtSendenSession.getEmpfaengerEmittenten(),
                uNachrichtSendenSession.isEmpfaengerEmittentenAlleAusgewaehlt());
        haengeUserAn(empfaengerListe, uNachrichtSendenSession.getEmpfaengerDritte(),
                uNachrichtSendenSession.isEmpfaengerDritteAlleAusgewaehlt());

        /*Empfänger-Liste Insti erstellen*/
        empfaengerInstiListe = new LinkedList<Integer>();
        haengeInstiAn(empfaengerInstiListe, uNachrichtSendenSession.getEmpfaengerInsti(),
                uNachrichtSendenSession.isEmpfaengerInstiAlleAusgewaehlt(),
                uNachrichtSendenSession.isEmpfaengerInstiAlleMitBestandAusgewaehlt());

        if (empfaengerListe.size() == 0 && empfaengerInstiListe.size() == 0) {
            uSession.setFehlermeldung("Kein Empfänger ausgewählt");
            return false;
        }

        if (CaBug.pruefeLog(logDrucken, 10)) {
            for (int i = 0; i < empfaengerInstiListe.size(); i++) {
                CaBug.druckeLog("EmpfängerInsti=" + empfaengerInstiListe.get(i),
                        logDrucken, 10);
            }
        }

        return true;
    }

    /**Senden der in uNachrichtSendenSession vorhandenen Nachricht.
     * Prüfung muß vorher mit pruefeNachrichtEingabe erfolgt ein.
     * Open+Weitere Close muß außerhalb erfolgen
     */
    public void senden() {
        if (uNachrichtSendenSession.getDateiAnhang() != null) {
            dateiAnhaengen();
        }

        /*Verwendungscode*/
        String hVerwendungsCode = uNachrichtSendenSession.getAusgewaehlterVerwendungsCode();
        int verwendungsCode = 0;
        if (CaString.isNummern(hVerwendungsCode)) {
            verwendungsCode = Integer.parseInt(hVerwendungsCode);
        }

        /*Betreff/Mailtext auch in EMail aufnehmen*/
        boolean vollenTextInEMail = uNachrichtSendenSession.isVollenTextInEMail();

        /*Dateianhang*/
        boolean dateiAnhangVorhanden = false;
        if (uNachrichtSendenSession.getAnhangListe() != null && uNachrichtSendenSession.getAnhangListe().size() > 0) {
            dateiAnhangVorhanden = true;
        }

        /*MailIdent*/
        int mailIdent = uNachrichtSendenSession.getMailIdent();

        /*MailBetreff*/
        String betreff = uNachrichtSendenSession.getBetreff();

        /*MailInhalt*/
        String inhalt = uNachrichtSendenSession.getMailText();

        /*Mail erzeugen*/
        BlNachrichten blNachricht = new BlNachrichten(true, eclDbM.getDbBundle());
        blNachricht.neueNachrichtSenden(empfaengerListe, empfaengerInstiListe, verwendungsCode, vollenTextInEMail,
                dateiAnhangVorhanden, mailIdent, betreff, inhalt, uNachrichtSendenSession.getMandant(),
                uNachrichtSendenSession.getAntwortAufNachricht(), eclUserLoginM.getUserLoginIdent(),
                uNachrichtSendenSession.isVollenTextInEMail(), uNachrichtSendenSession.getParameter1(),
                uNachrichtSendenSession.getParameter2(), uNachrichtSendenSession.getParameter3(),
                uNachrichtSendenSession.getParameter4(), uNachrichtSendenSession.getParameter5());

        uNachrichtSendenSession.setMailIdent(blNachricht.rcIdentMail);

        /*Tatsächliche Emails verschicken*/
        for (int i = 0; i < blNachricht.rcMailEmpfaenger.size(); i++) {
            baMailM.senden(blNachricht.rcMailEmpfaenger.get(i), blNachricht.rcMailBetreff.get(i),
                    blNachricht.rcMailInhalt.get(i));
        }

        blMNachrichten.vorbereitenNachrichtenEmpfangsliste(eclDbM.getDbBundle());
    }

    public String doSenden() {
        if (pruefeNachrichtEingabe() == false) {
            return "";
        }

        if (!xSessionVerwaltung.pruefeUStart("uNachrichtSenden", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }

        eclDbM.openAll();
        eclDbM.openWeitere();
        senden();

        eclDbM.closeAll();

        uSession.setFehlermeldung("Nachricht erfolgreich versendet");

        return xSessionVerwaltung.setzeUEnde(uNachrichtSendenSession.getAufrufMaske(), false, false,
                eclUserLoginM.getKennung());
    }

    private void haengeUserAn(List<Integer> empfaengerListe, List<EclNachrichtEmpfaengerUserM> empfaengerErgaenzen,
            boolean pAlle) {
        if (empfaengerErgaenzen == null) {
            return;
        }
        for (int i = 0; i < empfaengerErgaenzen.size(); i++) {
            if (pAlle || empfaengerErgaenzen.get(i).isAusgewaehlt()) {
                empfaengerListe.add(empfaengerErgaenzen.get(i).getIdentUserLogin());
            }
        }
    }

    private void haengeInstiAn(List<Integer> empfaengerListe, List<EclNachrichtEmpfaengerInstiM> empfaengerErgaenzen,
            boolean pAlle, boolean pAlleMitBestand) {
        if (empfaengerErgaenzen == null) {
            return;
        }
        for (int i = 0; i < empfaengerErgaenzen.size(); i++) {
            if (pAlle || empfaengerErgaenzen.get(i).isAusgewaehlt()
                    || (pAlleMitBestand && empfaengerErgaenzen.get(i).isBestandVorhanden())) {
                empfaengerListe.add(empfaengerErgaenzen.get(i).getIdentInsti());
            }
        }
    }

    public String doAbbrechen() {
        if (eclUserLoginM.pruefe_uportal_mailSenden() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uNachrichtSenden", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }

        return xSessionVerwaltung.setzeUEnde(uNachrichtSendenSession.getAufrufMaske(), true, false,
                eclUserLoginM.getKennung());

    }
}
