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

import java.util.ArrayList;
import java.util.List;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaDatumZeit;
import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComAllg.CaString;
import de.meetingapps.meetingportal.meetComBl.BlNummernformBasis;
import de.meetingapps.meetingportal.meetComEclM.EclDbM;
import de.meetingapps.meetingportal.meetComEntities.EclAktienregisterBestandshistorieeintrag;
import de.meetingapps.meetingportal.meetComEntitiesGenossenschaftSys.EgxGetPersoenlicheDatenRC;
import de.meetingapps.meetingportal.meetComEntitiesGenossenschaftSys.EgxGetUmsaetzeRC;
import de.meetingapps.meetingportal.meetComEntitiesGenossenschaftSys.EgxGetUmsaetzeResult;
import de.meetingapps.meetingportal.meetingportTController.PBestandhistorieSession;
import de.meetingapps.meetingportal.meetingportTController.TPermanentSession;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@RequestScoped
@Named
public class BrMBestandhistorie {
    private int logDrucken = 10;

    private @Inject EclDbM eclDbM;
    private @Inject TPermanentSession tPermanentSession;
    private @Inject PBestandhistorieSession pBestandhistorieSession;

    private @Inject BrMGenossenschaftCall brMGenossenschaftCall;

    /** pAktionaersnummer muß im Format "für intern" sein */
    public int holeAktuellenStand(String pAktionaersnummer) {
        /* Aktionärsnummer aufbereiten */
        BlNummernformBasis blNummernformBasis = new BlNummernformBasis(eclDbM.getDbBundle());
        String aktionaersnummerFuerGenossenschaftSysWebrequest = blNummernformBasis.aufbereitenKennungFuerGenossenschaftSysWebrequest(pAktionaersnummer);

        if (tPermanentSession.isTestModus()) {

        } else {

            /*
             * Persönliche Daten abrufen
             */
            EgxGetPersoenlicheDatenRC egxGetPersoenlicheDatenRC = brMGenossenschaftCall.doGetRequestPersoenlicheDaten(aktionaersnummerFuerGenossenschaftSysWebrequest);
            if (egxGetPersoenlicheDatenRC == null) {
                return CaFehler.perRemoteAktienregisterNichtVerfuegbar;
            }
            CaBug.druckeLog(egxGetPersoenlicheDatenRC.toString(), logDrucken, 10);

            pBestandhistorieSession.setAktionaerSeit(CaDatumZeit.datumJJJJ_MM_TTzuNormal(egxGetPersoenlicheDatenRC.kundeseit));

            holeUmsaetze(pAktionaersnummer);
        }

        return 1;
    }
    
    /*
     * Historie fuer alle abrufen
     */
    public int holeStandAlle(String pAktionaersnummer) {
        /* Aktionärsnummer aufbereiten */
        BlNummernformBasis blNummernformBasis = new BlNummernformBasis(eclDbM.getDbBundle());
        String aktionaersnummerFuerGenossenschaftSysWebrequest = blNummernformBasis.aufbereitenKennungFuerGenossenschaftSysWebrequest(pAktionaersnummer);
        
        EgxGetUmsaetzeRC egxGetUmsaetzeRC = brMGenossenschaftCall.doGetRequestUmsaetzeAlle(aktionaersnummerFuerGenossenschaftSysWebrequest);
        if (egxGetUmsaetzeRC == null) {
            return CaFehler.perRemoteAktienregisterNichtVerfuegbar;
        }
        CaBug.druckeLog(egxGetUmsaetzeRC.toString(), logDrucken, 10);

        if (egxGetUmsaetzeRC.getResult() != null) {
            fuelleBestandshistorieListe(egxGetUmsaetzeRC);
        }

        return 1;
    }

    /*
     * Historie fuer aktuelles Jahr abrufen
     */
    public int holeStandAktuellesJahr(String pAktionaersnummer) {
        /* Aktionärsnummer aufbereiten */
        BlNummernformBasis blNummernformBasis = new BlNummernformBasis(eclDbM.getDbBundle());
        String aktionaersnummerFuerGenossenschaftSysWebrequest = blNummernformBasis.aufbereitenKennungFuerGenossenschaftSysWebrequest(pAktionaersnummer);
        
        EgxGetUmsaetzeRC egxGetUmsaetzeRC = brMGenossenschaftCall.doGetRequestUmsaetzeAj(aktionaersnummerFuerGenossenschaftSysWebrequest);
        if (egxGetUmsaetzeRC == null) {
            return CaFehler.perRemoteAktienregisterNichtVerfuegbar;
        }
        CaBug.druckeLog(egxGetUmsaetzeRC.toString(), logDrucken, 10);

        if (egxGetUmsaetzeRC.getResult() != null) {
            fuelleBestandshistorieListe(egxGetUmsaetzeRC);
        }

        return 1;
    }
    
    /*
     * Historie fuer bestimmten Zeitraum
     */
    public int holeStandZeitraum(String pAktionaersnummer, String datumVon, String datumBis) {
        /* Aktionärsnummer aufbereiten */
        BlNummernformBasis blNummernformBasis = new BlNummernformBasis(eclDbM.getDbBundle());
        String aktionaersnummerFuerGenossenschaftSysWebrequest = blNummernformBasis.aufbereitenKennungFuerGenossenschaftSysWebrequest(pAktionaersnummer);
        
        EgxGetUmsaetzeRC egxGetUmsaetzeRC = brMGenossenschaftCall.doGetRequestUmsaetzeZeitraum(aktionaersnummerFuerGenossenschaftSysWebrequest, datumVon, datumBis);
        if (egxGetUmsaetzeRC == null) {
            return CaFehler.perRemoteAktienregisterNichtVerfuegbar;
        }
        CaBug.druckeLog(egxGetUmsaetzeRC.toString(), logDrucken, 10);

        if (egxGetUmsaetzeRC.getResult() != null) {
            fuelleBestandshistorieListe(egxGetUmsaetzeRC);
        }

        return 1;
    }
    
    public int holeUmsaetze(String pAktionaersnummer) {
        switch (pBestandhistorieSession.getUmsatzzeitraum()) {
        case "1":
            holeStandAlle(pAktionaersnummer);
            break;
            
        case "2":
            holeStandAktuellesJahr(pAktionaersnummer);
            break;
            
        case "3":
            holeStandZeitraum(pAktionaersnummer, pBestandhistorieSession.getDatumVonRequest(), pBestandhistorieSession.getDatumBisRequest());
            break;

        default:
            holeStandAlle(pAktionaersnummer);
            break;
        }
        
        return 1;
    }
    
    public int holeUmsaetzeMobil(String pAktionaersnummer) {
        switch (pBestandhistorieSession.getUmsatzzeitraum()) {
        case "1":
            holeStandAlle(pAktionaersnummer);
            break;
            
        case "2":
            holeStandAktuellesJahr(pAktionaersnummer);
            break;
            
        case "3":
            holeStandZeitraum(pAktionaersnummer, pBestandhistorieSession.getDatumVonRequestMobil(), pBestandhistorieSession.getDatumBisRequestMobil());
            break;

        default:
            holeStandAlle(pAktionaersnummer);
            break;
        }
        
        return 1;
    }
    
    private void fuelleBestandshistorieListe(EgxGetUmsaetzeRC egxGetUmsaetzeRC) {
        if (egxGetUmsaetzeRC.getResult() != null) {
            List<EclAktienregisterBestandshistorieeintrag> bestandshistorieEintraege = new ArrayList<EclAktienregisterBestandshistorieeintrag>();
            for (EgxGetUmsaetzeResult egxGetUmsaetzeResult : egxGetUmsaetzeRC.getResult()) {
                EclAktienregisterBestandshistorieeintrag eclAktienregisterBestandshistorieeintrag = new EclAktienregisterBestandshistorieeintrag();
                eclAktienregisterBestandshistorieeintrag.anteile = egxGetUmsaetzeResult.getAnteile();
                eclAktienregisterBestandshistorieeintrag.betrag = CaString.doubleToStringDE(egxGetUmsaetzeResult.getBetrag());
                eclAktienregisterBestandshistorieeintrag.buchungstext = egxGetUmsaetzeResult.getBuchungstext();
                eclAktienregisterBestandshistorieeintrag.datum_der_buchung = CaDatumZeit.datumJJJJ_MM_TTzuNormal(egxGetUmsaetzeResult.getDatum_der_buchung());
                eclAktienregisterBestandshistorieeintrag.datum_der_wirksamkeit = CaDatumZeit.datumJJJJ_MM_TTzuNormal(egxGetUmsaetzeResult.getDatum_der_wirksamkeit());
                bestandshistorieEintraege.add(eclAktienregisterBestandshistorieeintrag);
                pBestandhistorieSession.setBestandshistorie(bestandshistorieEintraege);
            }
        }
    }
}
