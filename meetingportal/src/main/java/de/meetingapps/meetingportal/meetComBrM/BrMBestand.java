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
package de.meetingapps.meetingportal.meetComBrM;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaDatumZeit;
import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComAllg.CaString;
import de.meetingapps.meetingportal.meetComBl.BlNummernformBasis;
import de.meetingapps.meetingportal.meetComEclM.EclDbM;
import de.meetingapps.meetingportal.meetComEntities.EclAktienregisterBestandsaenderungen;
import de.meetingapps.meetingportal.meetComEntities.EclAktienregisterWeiterePerson;
import de.meetingapps.meetingportal.meetComEntitiesGenossenschaftSys.EgxGetKuendigungRC;
import de.meetingapps.meetingportal.meetComEntitiesGenossenschaftSys.EgxGetKuendigungResult;
import de.meetingapps.meetingportal.meetComEntitiesGenossenschaftSys.EgxGetPersoenlicheDatenRC;
import de.meetingapps.meetingportal.meetComEntitiesGenossenschaftSys.EgxGetPersonendatenRC;
import de.meetingapps.meetingportal.meetComEntitiesGenossenschaftSys.EgxGetPersonendatenResult;
import de.meetingapps.meetingportal.meetComEntitiesGenossenschaftSys.EgxPostKuendigungsruecknahme;
import de.meetingapps.meetingportal.meetComEntitiesGenossenschaftSys.EgxPostKuendigungsruecknahmeRC;
import de.meetingapps.meetingportal.meetingportTController.PBestandSession;
import de.meetingapps.meetingportal.meetingportTController.TPermanentSession;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@RequestScoped
@Named
public class BrMBestand {
    private int logDrucken = 10;

    private @Inject EclDbM eclDbM;
    private @Inject TPermanentSession tPermanentSession;
    private @Inject PBestandSession pBestandSession;

    private @Inject BrMGenossenschaftCall brMGenossenschaftCall;

    /** pAktionaersnummer muß im Format "für intern" sein */
    public int holeAktuellenStand(String pAktionaersnummer) {
        /* Aktionärsnummer aufbereiten */
        BlNummernformBasis blNummernformBasis = new BlNummernformBasis(eclDbM.getDbBundle());
        String aktionaersnummerFuerRegister = blNummernformBasis.aufbereitenKennungFuerRegisterzugriff(pAktionaersnummer);
        String aktionaersnummerFuerGenossenschaftSysWebrequest = blNummernformBasis.aufbereitenKennungFuerGenossenschaftSysWebrequest(pAktionaersnummer);

        String tagesdatum_abfrage = "";
        int geschaeftsanteile_mitglied = 0;
        double geschaeftsguthaben_nominal_mitglied = 0;
        double geschaeftsguthabenAktuell = 0;
        double auffuellbetrag_mitglied = 0;

        List<EclAktienregisterBestandsaenderungen> aktienregisterBestandsaenderungen = new LinkedList<EclAktienregisterBestandsaenderungen>();
        List<EclAktienregisterWeiterePerson> hinterlegteAnprechpartner = new LinkedList<EclAktienregisterWeiterePerson>();
        List<EclAktienregisterWeiterePerson> vollmachten = new LinkedList<EclAktienregisterWeiterePerson>();
        List<EclAktienregisterWeiterePerson> postempfaenger = new LinkedList<EclAktienregisterWeiterePerson>();

        if (tPermanentSession.isTestModus()) {
            tagesdatum_abfrage = "2021-12-21";
            geschaeftsanteile_mitglied = 20;
            geschaeftsguthaben_nominal_mitglied = 2000;
            geschaeftsguthabenAktuell = 2200;
            auffuellbetrag_mitglied = 50;

            EclAktienregisterBestandsaenderungen lAktienregisterBestandsaenderungen = new EclAktienregisterBestandsaenderungen();
            lAktienregisterBestandsaenderungen.artDerAenderung = 1; // Kündigung
            lAktienregisterBestandsaenderungen.veranlasstAm = "2021-09-20"; // Kündigung vom
            lAktienregisterBestandsaenderungen.veraenderungAktien = 20; // gekuendigte_anteile
            lAktienregisterBestandsaenderungen.wirksamZum = "2024-12-31"; // gekuendigt_zum
            aktienregisterBestandsaenderungen.add(lAktienregisterBestandsaenderungen);

            lAktienregisterBestandsaenderungen = new EclAktienregisterBestandsaenderungen();
            lAktienregisterBestandsaenderungen.artDerAenderung = 1; // Kündigung
            lAktienregisterBestandsaenderungen.veranlasstAm = "2022-09-20"; // Kündigung vom
            lAktienregisterBestandsaenderungen.veraenderungAktien = 30; // gekuendigte_anteile
            lAktienregisterBestandsaenderungen.wirksamZum = "2025-12-31"; // gekuendigt_zum
            aktienregisterBestandsaenderungen.add(lAktienregisterBestandsaenderungen);

            EclAktienregisterWeiterePerson lWeiterePerson = new EclAktienregisterWeiterePerson();
            lWeiterePerson.ident = 1;
            lWeiterePerson.vorname = "vorvollmacht";
            lWeiterePerson.nachname = "nachvollmacht";
            hinterlegteAnprechpartner.add(lWeiterePerson);

            lWeiterePerson = new EclAktienregisterWeiterePerson();
            lWeiterePerson.ident = 2;
            lWeiterePerson.vorname = "voransprechpartner";
            lWeiterePerson.nachname = "nachansprechpartner";
            vollmachten.add(lWeiterePerson);

            lWeiterePerson = new EclAktienregisterWeiterePerson();
            lWeiterePerson.ident = 3;
            lWeiterePerson.vorname = "vorpostempfänger";
            lWeiterePerson.nachname = "nachpostempfänger";
            postempfaenger.add(lWeiterePerson);

            pBestandSession.setStandDatum("2021-09-03");
        } else {
            /* Über Schnittstelle Aktionärsdaten (allgemein) holen */
            CaBug.druckeLog("aktionaersnummerFuerRegister=" + aktionaersnummerFuerRegister, logDrucken, 10);
            CaBug.druckeLog("aktionaersnummerFuerGenossenschaftSysWebrequest=" + aktionaersnummerFuerGenossenschaftSysWebrequest, logDrucken, 10);

            EgxGetPersoenlicheDatenRC egxGetPersoenlicheDatenRC = brMGenossenschaftCall.doGetRequestPersoenlicheDaten(aktionaersnummerFuerGenossenschaftSysWebrequest);
            if (egxGetPersoenlicheDatenRC==null) {
                return CaFehler.perRemoteAktienregisterNichtVerfuegbar;
            }
            CaBug.druckeLog(egxGetPersoenlicheDatenRC.toString(), logDrucken, 10);
            
            EgxGetKuendigungRC egxGetKuendigungRC = brMGenossenschaftCall.doGetRequestKuendigung(aktionaersnummerFuerGenossenschaftSysWebrequest);
            if (egxGetKuendigungRC==null) {
                return CaFehler.perRemoteAktienregisterNichtVerfuegbar;
            }
            
            CaBug.druckeLog(egxGetKuendigungRC.toString(), logDrucken, 10);
            EgxGetPersonendatenRC egxGetPersonendatenRC = brMGenossenschaftCall.doGetRequestPersonendaten(aktionaersnummerFuerGenossenschaftSysWebrequest);
            if (egxGetPersonendatenRC==null) {
                return CaFehler.perRemoteAktienregisterNichtVerfuegbar;
            }
            CaBug.druckeLog(egxGetPersonendatenRC.toString(), logDrucken, 10);

            tagesdatum_abfrage = egxGetPersoenlicheDatenRC.tagesdatum;
            geschaeftsanteile_mitglied = egxGetPersoenlicheDatenRC.anteile;
            geschaeftsguthaben_nominal_mitglied = egxGetPersoenlicheDatenRC.guthaben_nominal;
            geschaeftsguthabenAktuell = egxGetPersoenlicheDatenRC.guthaben;
            auffuellbetrag_mitglied = egxGetPersoenlicheDatenRC.wiederauffuelungsbetrag;

            /* Liste der Kündigungen */

            if (egxGetKuendigungRC.result.size() > 0) {
                LocalDate now = LocalDate.now();
                for (EgxGetKuendigungResult result : egxGetKuendigungRC.result) {
                    if (CaDatumZeit.localDateVonDatum(result.wirksamkeit).isAfter(now)) {
                        if (result.anteile > 0) {
                            EclAktienregisterBestandsaenderungen lAktienregisterBestandsaenderungen = new EclAktienregisterBestandsaenderungen();
                            lAktienregisterBestandsaenderungen.artDerAenderung = 1; // Kündigung
                            lAktienregisterBestandsaenderungen.kuendigungId = result.id;
                            lAktienregisterBestandsaenderungen.veranlasstAm = result.leistung; // Kündigung vom
                            lAktienregisterBestandsaenderungen.veraenderungAktien = result.anteile; // gekuendigte_anteile
                            lAktienregisterBestandsaenderungen.wirksamZum = result.wirksamkeit; // gekuendigt_zum
                            lAktienregisterBestandsaenderungen.laufende_ruecknahme = result.laufende_ruecknahme;
                            aktienregisterBestandsaenderungen.add(lAktienregisterBestandsaenderungen);
                        }
                    }
                }
            }

            if (egxGetPersonendatenRC.result.size() > 0) {
                for (EgxGetPersonendatenResult result : egxGetPersonendatenRC.result) {
                    EclAktienregisterWeiterePerson lWeiterePerson = new EclAktienregisterWeiterePerson();
                    lWeiterePerson.ident = result.lfnr;
                    lWeiterePerson.vorname = result.vorname;
                    lWeiterePerson.nachname = result.nachname;
                    if (result.art.equals("natürliche Person (Mitglied / Teil des Mitglieds)") || result.art.equals("juristische Person (Mitglied / Teil des Mitglieds)")) {
                        hinterlegteAnprechpartner.add(lWeiterePerson);
                    } else {
                        if (result.postempfaenger) {
                            postempfaenger.add(lWeiterePerson);
                        } else {
                            vollmachten.add(lWeiterePerson);
                        }
                    }
                }
            }

            pBestandSession.setStandDatum(egxGetPersoenlicheDatenRC.tagesdatum);
        }

        /* Nun noch die formatierten Felder ergänzen */
        pBestandSession.setStandDatum(CaDatumZeit.DatumStringFuerAnzeige(tagesdatum_abfrage));

        pBestandSession.setGeschaeftsanteile(geschaeftsanteile_mitglied);
        pBestandSession.setGeschaeftsanteileDE(CaString.toStringDE(geschaeftsanteile_mitglied));

        pBestandSession.setGeschaeftsguthabenNominal(geschaeftsguthaben_nominal_mitglied);
        pBestandSession.setGeschaeftsguthabenNominalDE(CaString.toEuroStringDE(geschaeftsguthaben_nominal_mitglied));

        pBestandSession.setGeschaeftsguthabenAktuell(geschaeftsguthabenAktuell);
        pBestandSession.setGeschaeftsguthabenAktuellDE(CaString.toEuroStringDE(geschaeftsguthabenAktuell));

        pBestandSession.setAuffuellBetrag(auffuellbetrag_mitglied);
        pBestandSession.setAuffuellBetragDE(CaString.toEuroStringDE(auffuellbetrag_mitglied));

        if (aktienregisterBestandsaenderungen == null || aktienregisterBestandsaenderungen.size() == 0) {/* XXX Bedingung überprüfen! */
            pBestandSession.setKuendigungVorhanden(false);
        } else {
            pBestandSession.setKuendigungVorhanden(true);
        }
        pBestandSession.setKuendigungInBearbeitung(false);
        for (EclAktienregisterBestandsaenderungen iAktienregisterBestandsaenderungen : aktienregisterBestandsaenderungen) {
            iAktienregisterBestandsaenderungen.veranlasstAm = CaDatumZeit.DatumStringFuerAnzeige(iAktienregisterBestandsaenderungen.veranlasstAm);
            iAktienregisterBestandsaenderungen.veraenderungAktienDE = CaString.toStringDE(iAktienregisterBestandsaenderungen.veraenderungAktien);
            iAktienregisterBestandsaenderungen.wirksamZum = CaDatumZeit.DatumStringFuerAnzeige(iAktienregisterBestandsaenderungen.wirksamZum);
            if(iAktienregisterBestandsaenderungen.laufende_ruecknahme) {
                pBestandSession.setKuendigungInBearbeitung(true);
            }
        }

        pBestandSession.setAktienregisterBestandsaenderungen(aktienregisterBestandsaenderungen);

        pBestandSession.setHinterlegteAnprechpartner(hinterlegteAnprechpartner);
        if (hinterlegteAnprechpartner == null || hinterlegteAnprechpartner.size() == 0) {
            pBestandSession.setHinterlegteAnsprechpartnerVorhanden(false);
            CaBug.druckeLog("false", logDrucken, 10);
        } else {
            pBestandSession.setHinterlegteAnsprechpartnerVorhanden(true);
            CaBug.druckeLog("true", logDrucken, 10);
        }

        pBestandSession.setVollmachten(vollmachten);
        if (vollmachten == null || vollmachten.size() == 0) {
            pBestandSession.setVollmachtenVorhanden(false);
            CaBug.druckeLog("false", logDrucken, 10);
        } else {
            pBestandSession.setVollmachtenVorhanden(true);
            CaBug.druckeLog("true", logDrucken, 10);
        }

        pBestandSession.setPostempfaenger(postempfaenger);
        if (postempfaenger == null || postempfaenger.size() == 0) {
            pBestandSession.setPostempfaengerVorhanden(false);
            CaBug.druckeLog("false", logDrucken, 10);
        } else {
            pBestandSession.setPostempfaengerVorhanden(true);
            CaBug.druckeLog("true", logDrucken, 10);
        }

        pBestandSession.setWeiterePersonenVorhanden(pBestandSession.isHinterlegteAnsprechpartnerVorhanden() || pBestandSession.isVollmachtenVorhanden() || pBestandSession.isPostempfaengerVorhanden());
        CaBug.druckeLog("isWeiterePersonenVorhanden=" + pBestandSession.isWeiterePersonenVorhanden(), logDrucken, 10);

        return 1;
    }
    
    public int kuendigungZuruecknehmen(String pAktionaersnummer) {
        /* Aktionärsnummer aufbereiten */
        BlNummernformBasis blNummernformBasis = new BlNummernformBasis(eclDbM.getDbBundle());
        String aktionaersnummerFuerGenossenschaftSysWebrequest = blNummernformBasis.aufbereitenKennungFuerGenossenschaftSysWebrequest(pAktionaersnummer);
        
        EgxPostKuendigungsruecknahme egxPostKuendigungsruecknahme = new EgxPostKuendigungsruecknahme();
        egxPostKuendigungsruecknahme.id = pBestandSession.getGewaehlteBestandsaenderung().kuendigungId;
        egxPostKuendigungsruecknahme.leistung = CaDatumZeit.datumNormalZuJJJJ_MM_TT(pBestandSession.getGewaehlteBestandsaenderung().wirksamZum);
        
        EgxPostKuendigungsruecknahmeRC egxPostKuendigungsruecknahmeRC = brMGenossenschaftCall.doPostKuendigungsruecknahme(aktionaersnummerFuerGenossenschaftSysWebrequest, egxPostKuendigungsruecknahme);
        
        if(egxPostKuendigungsruecknahmeRC.getMitgliedsnummer() != 0) {
            return 1;
        } else {
            return 0;
        }
        
    }
}
