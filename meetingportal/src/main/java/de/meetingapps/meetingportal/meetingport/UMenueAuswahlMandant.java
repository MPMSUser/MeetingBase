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
import de.meetingapps.meetingportal.meetComBVerwaltung.BvMandanten;
import de.meetingapps.meetingportal.meetComBlManaged.BlMFuelleEclMAusPufferOderDBEE;
import de.meetingapps.meetingportal.meetComEclM.EclDbM;
import de.meetingapps.meetingportal.meetComEclM.EclMandantAuswahlElementM;
import de.meetingapps.meetingportal.meetComEclM.EclParamM;
import de.meetingapps.meetingportal.meetComEclM.EclUserLoginM;
import de.meetingapps.meetingportal.meetComEntities.EclEmittenten;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

/**Menü zum Mandant-auswählen
 * 
 * Bereiche:
 * > Mailbereich [0,9] oder [0,10]
 * 
 * > Mandanten mit Anstehenden Weisungsbestätigungen
 * 		(nur bei Usern, die zu einem Insti gehören, und die Berechtigung [0,14] haben)
 * 		
 * 
 * > Mandantenauswahl
 * 	Varianten: 
 * 		> Mandant+LaufendeNummer (d.h.: Standard-Mandant + laufende Nummern mit gleichem Jahr und gleicher Art)
 * 		> Mandant+LaufendeNummer+Jahr+"P" [1,9] 
 * 		> Mandant+LaufendeNummer+Jahr+Art [1,10] ([1,9] dann egal)
 * 		> Nur die Mandanten, für die Bestand zugeordnet ist
 * 
 * 
 */
@RequestScoped
@Named
public class UMenueAuswahlMandant {

    private int logDrucken = 3;

    @Inject
    private EclDbM eclDbM;
    @Inject
    private USession uSession;
    @Inject
    private XSessionVerwaltung xSessionVerwaltung;
    @Inject
    private EclParamM eclParamM;
    @Inject
    private BlMFuelleEclMAusPufferOderDBEE blMFuelleEclMAusPufferOderDBEE;

    @Inject
    private UMenueAuswahlMandantSession uMenueAuswahlMandantSession;
    @Inject
    private UNachrichtSenden uNachrichtSenden;
    @Inject
    private UPflegeNachrichtVerwendungsCodeSession uPflegeNachrichtVerwendungsCodeSession;
    @Inject
    private UPflegeNachrichtBasisTextSession uPflegeNachrichtBasisTextSession;
    @Inject
    private UMenue uMenue;
    @Inject
    private EclUserLoginM eclUserLoginM;
    @Inject
    private UNachrichtEmpfangen uNachrichtEmpfangen;
    @Inject
    private UPasswortAendernSession uPasswortAendernSession;
    @Inject
    private UPasswortAendern uPasswortAendern;
    @Inject
    private URedakteur uRedakteur;

    public void init() {
        eclDbM.openAll();
        eclDbM.openWeitere();
        uNachrichtEmpfangen.init("uMenueAuswahlMandant", false);
        erzeugeMandantenListe();
        eclDbM.closeAll();
    }

    public String doWeiter() {
        if (eclUserLoginM.pruefe_emittentenPortal() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uMenueAuswahlMandant", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }

        String hMandantIdent = uMenueAuswahlMandantSession.getAusgewaehlterMandant();
        if (hMandantIdent.isEmpty() || !CaString.isNummern(hMandantIdent)) {
            uSession.setFehlermeldung("Bitte Mandant auswählen!");
            return xSessionVerwaltung.setzeUEnde("uMenueAuswahlMandant", false, false, eclUserLoginM.getKennung());
        }
        int mandantIdent = Integer.parseInt(hMandantIdent);
        if (mandantIdent > uMenueAuswahlMandantSession.getMandantenListeZurAuswahl().size()) {
            uSession.setFehlermeldung("Unzulässiger Mandant!");
            return xSessionVerwaltung.setzeUEnde("uMenueAuswahlMandant", false, false, eclUserLoginM.getKennung());
        }
        EclMandantAuswahlElementM lMandantAuswahlElementM = uMenueAuswahlMandantSession.getMandantenListeZurAuswahl()
                .get(mandantIdent);

        eclParamM.getClGlobalVar().mandant = lMandantAuswahlElementM.getMandant();
        eclParamM.getClGlobalVar().hvJahr = lMandantAuswahlElementM.getHvJahr();
        eclParamM.getClGlobalVar().hvNummer = lMandantAuswahlElementM.getHvNummer();
        eclParamM.getClGlobalVar().datenbereich = lMandantAuswahlElementM.getDbArt();

        eclDbM.openAll();
        blMFuelleEclMAusPufferOderDBEE.fuelleAlleVariablenBeiStart();
        eclDbM.closeAll();

        eclDbM.openAll();
        uMenue.init();
        eclDbM.closeAll();
        return xSessionVerwaltung.setzeUEnde("uMenue", true, false, eclUserLoginM.getKennung());
    }

    public String doNachrichtSenden() {
        if (eclUserLoginM.pruefe_uportal_mailSenden() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uMenueAuswahlMandant", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }

        uNachrichtSenden.initialisieren(0, 0, 0);
        uNachrichtSenden.initialisiereAusgangsXHTML("uMenueAuswahlMandant");

        return xSessionVerwaltung.setzeUEnde("uNachrichtSenden", true, false, eclUserLoginM.getKennung());
    }

    public String doPflegeNachrichtBasisText() {
        CaBug.druckeLog("Start doPflegeNachrichtBasisText", logDrucken, 3);
        if (eclUserLoginM.pruefe_uportal_dLoginHighAdmin() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uMenueAuswahlMandant", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }
        uPflegeNachrichtBasisTextSession.clear();

        return xSessionVerwaltung.setzeUEnde("uPflegeNachrichtBasisText", true, false, eclUserLoginM.getKennung());
    }

    public String doPflegeNachrichtVerwendungsCode() {
        if (eclUserLoginM.pruefe_uportal_dLoginHighAdmin() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uMenueAuswahlMandant", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }
        uPflegeNachrichtVerwendungsCodeSession.clear();
        return xSessionVerwaltung.setzeUEnde("uPflegeNachrichtVerwendungsCode", true, false,
                eclUserLoginM.getKennung());
    }

    
    public String doRedakteurUebersicht() {
        if (eclUserLoginM.pruefe_uportal_abfrageRedakteur() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uMenueAuswahlMandant", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }
        
        uRedakteur.init();
         
        return xSessionVerwaltung.setzeUEnde("uRedakteur", true, false,
                eclUserLoginM.getKennung());
    }
    
    
    
    public String doPasswortAendern() {
        if (!xSessionVerwaltung.pruefeUStart("uMenueAuswahlMandant", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }
        uPasswortAendernSession.clear();
        uPasswortAendern.init("uMenueAuswahlMandant");
        return xSessionVerwaltung.setzeUEnde("uPasswortAendern", true, false, eclUserLoginM.getKennung());
    }

    /**eclDbM muß offen sein.
     * 
     * Belegt:
     * uControllerMenueAuswahlMandantSession.mandantenZurAuswahlVorhanden
     * uControllerMenueAuswahlMandantSession.mandantenListeZurAuswahl
     * */
    public void erzeugeMandantenListe() {
        BvMandanten bvMandanten = new BvMandanten();
        List<EclEmittenten> lEmittentenListe = null;
        CaBug.druckeLog("eclUserLoginM.getGehoertZuInsti=" + eclUserLoginM.getGehoertZuInsti(), logDrucken, 10);
        if (eclUserLoginM.getGehoertZuInsti() > 0) {
            /**Institutionelle Kennung - nur die Mandanten, die für diese Insti zugeordnete Bestände haben*/
            lEmittentenListe = bvMandanten.liefereMandantenZuInsti(eclDbM.getDbBundle(),
                    eclUserLoginM.getGehoertZuInsti());
        } else {
            int standardOderNurPOderAlle = 1;
            if (eclUserLoginM.pruefe_mandantenBearbeiten_alleMitP()) {
                standardOderNurPOderAlle = 2;
            }
            if (eclUserLoginM.pruefe_mandantenBearbeiten_alle()) {
                standardOderNurPOderAlle = 3;
            }
            lEmittentenListe = bvMandanten.liefereMandanten(eclDbM.getDbBundle(), standardOderNurPOderAlle);
            if (lEmittentenListe == null || lEmittentenListe.size() == 0) {
                uMenueAuswahlMandantSession.setMandantenZurAuswahlVorhanden(false);
                uMenueAuswahlMandantSession.setMandantenListeZurAuswahl(null);
            }
        }
        if (lEmittentenListe == null || lEmittentenListe.size() == 0) {
            uMenueAuswahlMandantSession.setMandantenZurAuswahlVorhanden(false);
            uMenueAuswahlMandantSession.setMandantenListeZurAuswahl(null);
        } else {
            List<EclMandantAuswahlElementM> mandantenListeZurAuswahl = new LinkedList<EclMandantAuswahlElementM>();
            for (int i = 0; i < lEmittentenListe.size(); i++) {
                EclMandantAuswahlElementM lMandantAuswahlElementM = new EclMandantAuswahlElementM();
                lMandantAuswahlElementM.copyFrom(lEmittentenListe.get(i), i);
                mandantenListeZurAuswahl.add(lMandantAuswahlElementM);
            }
            uMenueAuswahlMandantSession.setMandantenZurAuswahlVorhanden(true);
            uMenueAuswahlMandantSession.setMandantenListeZurAuswahl(mandantenListeZurAuswahl);
        }
    }

}
