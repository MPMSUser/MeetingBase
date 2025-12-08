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

import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComBl.Blku178Formulare;
import de.meetingapps.meetingportal.meetComBl.BlVeranstaltungen;
import de.meetingapps.meetingportal.meetComEclM.EclDbM;
import de.meetingapps.meetingportal.meetComEclM.EclPortalTexteM;
import de.meetingapps.meetingportal.meetComEclM.EclTeilnehmerLoginM;
import de.meetingapps.meetingportal.meetingCoreReport.RpBrowserAnzeigen;
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
public class AControllerGeneralversammlung {

    @Inject
    private ADlgVariablen aDlgVariablen;
    @Inject
    private AFunktionen aFunktionen;
    @Inject
    private EclDbM eclDbM;

    @Inject
    private EclTeilnehmerLoginM eclTeilnehmerLoginM;
    @Inject
    private EclPortalTexteM eclTextePortalM;

    @Inject
    private AControllerGeneralversammlungSession aControllerGeneralversammlungSession;

    /**Initialisierung für Status-Anzeige Dialogveranstaltungen
     * EclDbM wird in init gehandelt*/
    public void init() {
        eclDbM.openAll();
        eclDbM.openWeitere();
        BlVeranstaltungen blVeranstaltungen = new BlVeranstaltungen(true, eclDbM.getDbBundle());
        //		blVeranstaltungen.gv_liefereAnmeldeStatus(eclTeilnehmerLoginM.getAnmeldeIdentAktienregister());

        if (blVeranstaltungen.rcGVAngemeldet != 0) {
            aControllerGeneralversammlungSession.setAnOderAbgemeldet(true);
        } else {
            aControllerGeneralversammlungSession.setAnOderAbgemeldet(false);
        }

        if (blVeranstaltungen.rcGVAngemeldet == 1 || blVeranstaltungen.rcGVAngemeldet == 3) {
            aControllerGeneralversammlungSession.setAngemeldet(true);
        } else {
            aControllerGeneralversammlungSession.setAngemeldet(false);
        }

        if (blVeranstaltungen.rcGVAngemeldet == 2) {
            aControllerGeneralversammlungSession.setAbgemeldet(true);
        } else {
            aControllerGeneralversammlungSession.setAbgemeldet(false);
        }

        aControllerGeneralversammlungSession.setZweiPersonenZulaessig(blVeranstaltungen.rcZweiPersonenMoeglich);
        aControllerGeneralversammlungSession.setAnmeldung(Integer.toString(blVeranstaltungen.rcGVAngemeldet));

        eclDbM.getDbBundle().closeAll();

    }

    public String doAnAbmelden() {
        if (!aFunktionen.pruefeStart("aAuswahl1Generalversammlung")) {
            return "aDlgFehler";
        }
        return aFunktionen.setzeEnde("aAuswahl1GeneralversammlungAnAbmelden", true, true);

    }

    public String doWeiter() {
        if (!aFunktionen.pruefeStart("aAuswahl1GeneralversammlungAnAbmelden")) {
            return "aDlgFehler";
        }
        String auswahl = aControllerGeneralversammlungSession.getAnmeldung();
        if (auswahl == null || auswahl.isEmpty()) {
            aDlgVariablen.setFehlerNr(CaFehler.afBitteAnOderAbmelden);
            aDlgVariablen.setFehlerMeldung(eclTextePortalM.getFehlertext(CaFehler.afBitteAnOderAbmelden));
            aFunktionen.setzeEnde();
            return "";
        }

        eclDbM.openAll();
        eclDbM.openWeitere();
        BlVeranstaltungen blVeranstaltungen = new BlVeranstaltungen(true, eclDbM.getDbBundle());
        blVeranstaltungen.gv_anAbmeldung(eclTeilnehmerLoginM.getAnmeldeIdentAktienregister(), Integer.parseInt(auswahl),
                1);
        eclDbM.closeAll();
        init();

        return aFunktionen.setzeEnde("aAuswahl1Generalversammlung", true, true);

    }

    public String doZurueck() {
        if (!aFunktionen.pruefeStart("aAuswahl1GeneralversammlungAnAbmelden")) {
            return "aDlgFehler";
        }
        return aFunktionen.setzeEnde("aAuswahl1Generalversammlung", true, true);
    }

    public void doButton1() {
        if (!aFunktionen.pruefeStart("aAuswahl1Generalversammlung")) {
            return;
        }
        eclDbM.openAll();
        Blku178Formulare blku178Formulare = new Blku178Formulare(eclDbM.getDbBundle());
        String pfad1 = blku178Formulare.lieferePfadFuerElternvollmachtsformular();
        eclDbM.closeAll();

        RpBrowserAnzeigen rpBrowserAnzeigen = new RpBrowserAnzeigen();
        rpBrowserAnzeigen.zeigen(pfad1);
        aFunktionen.setzeEnde();

    }

    /**Abmelden*/
    public String doAbmelden() {
        aDlgVariablen.clearLogin();
        return aFunktionen.waehleLogout();
    }

}
