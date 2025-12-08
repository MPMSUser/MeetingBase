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
import de.meetingapps.meetingportal.meetComBa.BaMailM;
import de.meetingapps.meetingportal.meetComBl.BlGastkarte;
import de.meetingapps.meetingportal.meetComEclM.EclAnredeListeM;
import de.meetingapps.meetingportal.meetComEclM.EclDbM;
import de.meetingapps.meetingportal.meetComEclM.EclGastM;
import de.meetingapps.meetingportal.meetComEclM.EclMeldungenMeldungenListeM;
import de.meetingapps.meetingportal.meetComEclM.EclPortalTexteM;
import de.meetingapps.meetingportal.meetComEntities.EclMeldung;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@RequestScoped
@Named
@Deprecated
public class AControllerGGNeuerGastRegistrieren {

    @Inject
    BaMailM baMailm;
    @Inject
    ADlgVariablen aDlgVariablen;
    @Inject
    AFunktionen aFunktionen;
    @Inject
    EclPortalTexteM eclTextePortalM;
    @Inject
    private EclDbM eclDbM;
    @Inject
    EclAnredeListeM lAnredeListeM;
    @Inject
    EclGastM lGastM;
    @Inject
    EclMeldungenMeldungenListeM lMeldungenMeldungenListeM;

    public String doSpeichern() {
        if (!aFunktionen.pruefeStart("aGGNeuerGastRegistrieren")) {
            return "aDlgFehler";
        }

        int erg;
        int i;

        /*TODO: es fehlt noch die Multiuserfähigkeit! Auto-Update wie bisher vorgesehen mit db_version-Überprüfung und Fehlermeldung funktioniert bei
         * Web-Anwendung nicht!!
         */
        /*Plausibilitätsprüfung*/
        aDlgVariablen.clearFehlerMeldung();
        if (lGastM.getName() == null || lGastM.getName().isEmpty()) {
            aDlgVariablen.setFehlerMeldung(eclTextePortalM.getFehlertext(CaFehler.afGastNameFehlt));
            aDlgVariablen.setFehlerNr(CaFehler.afGastNameFehlt);
            aFunktionen.setzeEnde();
            return "";
        }
        if (lGastM.getVorname() == null || lGastM.getVorname().isEmpty()) {
            aDlgVariablen.setFehlerMeldung(eclTextePortalM.getFehlertext(CaFehler.afGastVornameFehlt));
            aDlgVariablen.setFehlerNr(CaFehler.afGastVornameFehlt);
            aFunktionen.setzeEnde();
            return "";
        }
        if (lGastM.getOrt() == null || lGastM.getOrt().isEmpty()) {
            aDlgVariablen.setFehlerMeldung(eclTextePortalM.getFehlertext(CaFehler.afGastOrtFehlt));
            aDlgVariablen.setFehlerNr(CaFehler.afGastOrtFehlt);
            aFunktionen.setzeEnde();
            return "";
        }

        /*Abspeichern*/
        eclDbM.openAll();

        EclMeldung lGastMeldung = new EclMeldung();
        lGastMeldung.meldungsIdent = lGastM.getMeldeIdent();

        eclDbM.getDbBundle().dbMeldungen.leseZuMeldungsIdent(lGastMeldung);
        lGastMeldung = eclDbM.getDbBundle().dbMeldungen.meldungenArray[0];

        lGastMeldung.anrede = Integer.parseInt(lGastM.getAnrede());
        lGastMeldung.titel = lGastM.getTitel();
        lGastMeldung.adelstitel = lGastM.getAdelstitel();
        lGastMeldung.name = lGastM.getName();
        lGastMeldung.vorname = lGastM.getVorname();
        lGastMeldung.zuHdCo = lGastM.getZuHdCo();
        lGastMeldung.zusatz1 = lGastM.getZusatz1();
        lGastMeldung.zusatz2 = lGastM.getZusatz2();
        lGastMeldung.strasse = lGastM.getStrasse();
        lGastMeldung.land = lGastM.getLand();
        lGastMeldung.plz = lGastM.getPlz();
        lGastMeldung.ort = lGastM.getOrt();
        lGastMeldung.mailadresse = lGastM.getMailadresse();

        eclDbM.getDbBundle().dbMeldungen.update(lGastMeldung);

        BlGastkarte blGastkarte = new BlGastkarte(eclDbM.getDbBundle());

        /*Versandart*/
        blGastkarte.pVersandart = 5;
        aDlgVariablen.setAusstellungsart("2");
        if (!lGastM.getMailadresse().isEmpty()) {
            blGastkarte.pVersandart = 4;
            aDlgVariablen.setAusstellungsart("1");
        }

        blGastkarte.pGast = lGastMeldung;
        blGastkarte.pWillenserklaerungGeberIdent = aDlgVariablen.getIdentMasterGast(); /*Eigentümer der Gruppenkarte*/
        blGastkarte.rcZutrittsIdent = lGastM.getNummer();

        erg = blGastkarte.reDrucken();

        if (erg < 0) {
            aDlgVariablen.setFehlerMeldung(eclTextePortalM.getFehlertext(erg));
            aDlgVariablen.setFehlerNr(erg);
            eclDbM.closeAllAbbruch();
            aFunktionen.setzeEnde();
            return "";
        }

        aDlgVariablen.setGastkartePdfNrS(Integer.toString(blGastkarte.rcGastkartePdfNr));

        /*Gastkarten-PDF erzeugen*/
        if (!lGastM.getMailadresse().isEmpty()) {
            /*Nun per Mail versenden*/
            baMailm.sendenMitAnhang(lGastM.getMailadresse(), "Ihr Zutrittsdokument",
                    "Anbei erhalten Sie Ihr Zutrittsdokument",
                    "D:\\meetingausdrucke\\zutrittsdokument" + Integer.toString(blGastkarte.rcGastkartePdfNr) + ".pdf");
        }

        /*******Nun noch aufbereiten für Anzeige********/
        /*Anrede*/
        lGastM.setAnredeText("");
        if (lGastM.getAnrede().compareTo("0") != 0) {
            for (i = 0; i < lAnredeListeM.getAnredeListeM().size(); i++) {
                if (lAnredeListeM.getAnredeListeM().get(i).getAnredennr().equals(lGastM.getAnrede())) {
                    lGastM.setAnredeText(lAnredeListeM.getAnredeListeM().get(i).getAnredentext());
                }
            }
        }

        eclDbM.closeAll();

        return aFunktionen.setzeEnde("aGGNeuerGastQuittung", true, false);
    }

    public String doAbbrechen() {
        int i;

        if (!aFunktionen.pruefeStart("aGGNeuerGastRegistrieren")) {
            return "aDlgFehler";
        }

        /*MasterGast wieder einlesen für Anzeige*/
        /*TODO _Gastverwalrung: Berücksichtigen, dass sich die Gastdaten möglicherweise mittlerweile verändert haben !!!*/
        EclMeldung lMeldung = new EclMeldung();

        eclDbM.openAll();

        lMeldung.meldungsIdent = aDlgVariablen.getIdentMasterGast();
        eclDbM.getDbBundle().dbMeldungen.leseZuMeldungsIdent(lMeldung);

        lMeldung = eclDbM.getDbBundle().dbMeldungen.meldungenArray[0];

        /*Achtung: noch nicht berücksichtigt, dass storniert, mehrere ZutrittsIdents, etc. ....*/
        eclDbM.getDbBundle().dbZutrittskarten.readZuMeldungsIdentGast(lMeldung.meldungsIdent);
        lMeldung.zutrittsIdent = eclDbM.getDbBundle().dbZutrittskarten.ergebnisPosition(0).zutrittsIdent;

        lGastM.init();
        lGastM.copyFrom(eclDbM.getDbBundle().dbMeldungen.meldungenArray[0]);

        /*Anrede*/
        lGastM.setAnredeText("");
        if (lGastM.getAnrede().compareTo("0") != 0) {
            for (i = 0; i < lAnredeListeM.getAnredeListeM().size(); i++) {
                if (lAnredeListeM.getAnredeListeM().get(i).getAnredennr().equals(lGastM.getAnrede())) {
                    lGastM.setAnredeText(lAnredeListeM.getAnredeListeM().get(i).getAnredentext());
                }
            }
        }

        /*Zugeordnete einlesen - Überprüfen ob "Gruppen-Gastanmeldung*/
        lMeldungenMeldungenListeM.fuelleListe(eclDbM.getDbBundle(), lMeldung);
        lGastM.setMeldungenMeldungenListe(lMeldungenMeldungenListeM.getMeldungenMeldungenListeM());
        if (lGastM.getMeldungenMeldungenListe().size() > 0) {
            aDlgVariablen.setGruppenausstellung(true);
        } else {
            aDlgVariablen.setGruppenausstellung(false);
        }

        eclDbM.closeAll();
        return aFunktionen.setzeEnde("aGGGastStatus", true, false);
    }

    public String doAbmelden() {
        aDlgVariablen.clearLogin();
        return aFunktionen.waehleLogout();
    }

}
