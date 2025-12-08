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
import de.meetingapps.meetingportal.meetComAllg.CaString;
import de.meetingapps.meetingportal.meetComBl.BlVeranstaltungen;
import de.meetingapps.meetingportal.meetComEclM.EclDbM;
import de.meetingapps.meetingportal.meetComEclM.EclPortalTexteM;
import de.meetingapps.meetingportal.meetComEclM.EclTeilnehmerLoginM;
import de.meetingapps.meetingportal.meetComEclM.EclVeranstaltungM;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

/**Verwendung der "Text-Nummern" in Portal und App
 *(Gesamt-Ident Portal - AppSeite/AppIdentAufSeite)
 *
 * Nur App:
 * (604-?15/?999) Fenstertitel
 * 
 * (570-/) Überschrift
 * (574-/) Text am Anfang der Seite
 * 
 * (571-/) Text vor Briefwahl (kommt immer!)
 * (572-/) Text vor Ersterteilung Briefwahl (wenn möglich)
 * (573-/) Button Briefwahl erteilen
 * (575-/) Text vor Ändern Briefwahl (wenn möglich)
 * (576-/) Button Briefwahl Ändern
 * (577-/) Text vor Stornierung Brefwahl (wenn möglich)
 * (578-/) Button Briefwahl Stornieren
 *  
 * (579-/) Text vor An-/Abmelden (kommt immer!)
 * (580-/) Text vor Anmelden / Abmelden (wenn möglich)
 * (593-/) Text vor Anmelden / Abmelden - wenn angemeldet
 * (594-/) Text vor Anmelden / Abmelden - wenn abgemeldet
 * (581-/) Button Anmelden / Abmelden
 *  
 * (582-/) Text vor Vollmacht (kommt immer!)
 * (583-/) Text vor Vollmacht erteilen (wenn möglich)
 * (584-/) Button Vollmacht erteilen
 * (585-/) Text vor Vollmacht widerrufen (wenn möglich)
 * (586-/) Button Vollmacht widerrufen
 *  
 * (587-/) Text vor erhaltene Vollmacht (kommt immer!)
 * (588-/) Text vor erhaltene Vollmachten (wenn möglich)
 * (589-/) Button erhaltene Vollmacht einsehen
 *  
 * (590-/) Text vor persönliche Daten (kommt immer!)
 * (591-/) Button persönliche Daten
 * 
 * (592-/) Text am Ende der Seite
 * 
*  
 * 
 */

@RequestScoped
@Named
@Deprecated
public class AControllerDialogveranstaltungen {

    @Inject
    private ADlgVariablen aDlgVariablen;
    @Inject
    private AFunktionen aFunktionen;
    @Inject
    private EclDbM eclDbM;

    @Inject
    private EclTeilnehmerLoginM eclTeilnehmerLoginM;
    @Inject
    private AControllerDialogveranstaltungenSession aControllerDialogveranstaltungenSession;
    @Inject
    private EclPortalTexteM eclTextePortalM;

    /**Initialisierung für Status-Anzeige Dialogveranstaltungen
     * EclDbM wird in init gehandelt*/
    public void init() {
        eclDbM.openAll();
        eclDbM.openWeitere();
        BlVeranstaltungen blVeranstaltungen = new BlVeranstaltungen(true, eclDbM.getDbBundle());
        blVeranstaltungen.liefereAnmeldeStatus(eclTeilnehmerLoginM.getAnmeldeIdentAktienregister());

        aControllerDialogveranstaltungenSession.setIstAngemeldet(blVeranstaltungen.rcAngemeldet);
        if (blVeranstaltungen.rcAngemeldet) {
            aControllerDialogveranstaltungenSession
                    .setAnzPersonen(Integer.toString(blVeranstaltungen.rcAngemeldetAnzahlPersonen));
            aControllerDialogveranstaltungenSession.setZurVeranstaltungAngemeldet(
                    new EclVeranstaltungM(blVeranstaltungen.rcAngemeldetZuVeranstaltung));
        } else {
            aControllerDialogveranstaltungenSession.setAnzPersonen("0");
            aControllerDialogveranstaltungenSession.setZurVeranstaltungAngemeldet(null);
        }
        eclDbM.closeAll();

    }

    /**Initialisierung für eigentliche Anmelde-Maske
     * EclDbM wird in initAnmelden gehandelt*/
    public void initAnmelden() {
        eclDbM.openAll();
        eclDbM.openWeitere();
        BlVeranstaltungen blVeranstaltungen = new BlVeranstaltungen(true, eclDbM.getDbBundle());
        blVeranstaltungen.leseAktiveVeranstaltungen(eclTeilnehmerLoginM.getAnmeldeIdentAktienregister());
        List<EclVeranstaltungM> veranstaltungListe = new LinkedList<EclVeranstaltungM>();
        aControllerDialogveranstaltungenSession.setAusgewaehlt("");
        if (blVeranstaltungen.rcVeranstaltungArray != null) {
            for (int i = 0; i < blVeranstaltungen.rcVeranstaltungArray.length; i++) {
                EclVeranstaltungM lVeranstaltungM = new EclVeranstaltungM(blVeranstaltungen.rcVeranstaltungArray[i]);
                veranstaltungListe.add(lVeranstaltungM);
                if (!lVeranstaltungM.isAusgebucht() && lVeranstaltungM.isAusgewaehlt()) {
                    aControllerDialogveranstaltungenSession.setAusgewaehlt(Integer.toString(i));
                }
            }
        }
        aControllerDialogveranstaltungenSession.setVeranstaltungen(veranstaltungListe);
        aControllerDialogveranstaltungenSession.setAnzPersonen("1");
        eclDbM.closeAll();

    }

    public String doAnmelden() {
        if (!aFunktionen.pruefeStart("aAuswahl1Dialogveranstaltungen")) {
            return "aDlgFehler";
        }
        initAnmelden();
        return aFunktionen.setzeEnde("aAuswahl1AnmeldenDialogveranstaltungen", true, true);
    }

    public String doZurueck() {
        if (!aFunktionen.pruefeStart("aAuswahl1AnmeldenDialogveranstaltungen")) {
            return "aDlgFehler";
        }
        return aFunktionen.setzeEnde("aAuswahl1Dialogveranstaltungen", true, true);
    }

    public String doAnmeldenAusfuehren(EclVeranstaltungM lVeranstaltung) {
        if (!aFunktionen.pruefeStart("aAuswahl1AnmeldenDialogveranstaltungen")) {
            return "aDlgFehler";
        }

        int fehler = 0;
        String hAnzahlPersonen = aControllerDialogveranstaltungenSession.getAnzPersonen();
        int anzahlPersonen = 0;
        if (!CaString.isNummern(hAnzahlPersonen) || hAnzahlPersonen.length() > 4) {
            fehler = CaFehler.afNurZahlenZulaessig;
        } else {
            anzahlPersonen = Integer.parseInt(hAnzahlPersonen);
            if (anzahlPersonen < 1) {
                fehler = CaFehler.afNullPersonenAngemeldet;
            }
            if (anzahlPersonen > 5) {
                fehler = CaFehler.afMaxPersonenUeberschritten;
            }
        }
        if (fehler == 0) {
            if (lVeranstaltung.getAnzahlAnmeldungenIst() + anzahlPersonen > lVeranstaltung
                    .getMaximaleAnzahlAnmeldungen()) {
                fehler = CaFehler.afAusgebucht;
            }
        }
        if (fehler != 0) {
            aDlgVariablen.setFehlerNr(fehler);
            aDlgVariablen.setFehlerMeldung(eclTextePortalM.getFehlertext(fehler));
            aFunktionen.setzeEnde();
            return "";
        }

        eclDbM.openAll();
        eclDbM.openWeitere();
        BlVeranstaltungen blVeranstaltungen = new BlVeranstaltungen(true, eclDbM.getDbBundle());
        blVeranstaltungen.anmeldung(eclTeilnehmerLoginM.getAnmeldeIdentAktienregister(), lVeranstaltung.getIdent(),
                anzahlPersonen, 1, 1);
        eclDbM.closeAll();
        if (blVeranstaltungen.rcUeberbucht) {
            initAnmelden();
            aDlgVariablen.setFehlerNr(CaFehler.afAusgebucht);
            aDlgVariablen.setFehlerMeldung(eclTextePortalM.getFehlertext(CaFehler.afAusgebucht));
            aFunktionen.setzeEnde();
            return "";
        }
        init();

        return aFunktionen.setzeEnde("aAuswahl1Dialogveranstaltungen", true, true);
    }

    /*************************aAuswahl1Dialogveranstaltungen*************************************/

    public String doStornieren() {
        if (!aFunktionen.pruefeStart("aAuswahl1Dialogveranstaltungen")) {
            return "aDlgFehler";
        }
        eclDbM.openAll();
        eclDbM.openWeitere();
        BlVeranstaltungen blVeranstaltungen = new BlVeranstaltungen(true, eclDbM.getDbBundle());
        blVeranstaltungen.widerrufeAnmeldung(eclTeilnehmerLoginM.getAnmeldeIdentAktienregister(), 1);
        eclDbM.closeAll();
        init();
        return aFunktionen.setzeEnde("aAuswahl1Dialogveranstaltungen", true, true);
    }

    public String doDialogveranstaltungenZurueck() {
        if (!aFunktionen.pruefeStart("aAuswahl1Dialogveranstaltungen")) {
            return "aDlgFehler";
        }

        return aFunktionen.setzeEnde("aAuswahl1", true, true);

    }

    /********************Übergreifend nutzbar*******************************/

    /**Abmelden*/
    public String doAbmelden() {
        aDlgVariablen.clearLogin();
        return aFunktionen.waehleLogout();
    }

}
