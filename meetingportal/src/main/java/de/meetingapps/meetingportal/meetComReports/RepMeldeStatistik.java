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
package de.meetingapps.meetingportal.meetComReports;

import de.meetingapps.meetingportal.meetComAllg.CaString;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclLoginDaten;
import de.meetingapps.meetingportal.meetingCoreReport.RpDrucken;
import de.meetingapps.meetingportal.meetingCoreReport.RpVariablen;

public class RepMeldeStatistik {

    public String anzahlMeldungen = "";

    public String anzahlEK = "";
    public String anzahlEKVersand = "";
    public String anzahlEKSelbstdruck = "";
    public String anzahlEKMail = "";

    public String anzahlAktionaerePortalBestaetigt = "";
    public String anzahlAktionaereEmailVersand = "";

    public String anzahlBriefwahl = "";
    public String anzahlSRV = "";
    public String anzahlKIAV = "";
    public String anzahlDauer = "";
    public String anzahlOrga = "";

    public String anzahlStimmenBriefwahl = "";
    public String anzahlStimmenSRV = "";
    public String anzahlStimmenKIAV = "";
    public String anzahlStimmenDauer = "";
    public String anzahlStimmenOrga = "";

    public String anzahlAktionaereInsgesamt = "";

    public String anzahlStimmen = "";
    public String anzahlStimmenE = "";
    public String anzahlStimmenF = "";
    public String anzahlStimmenV = "";
    public String anzahlStimmenSonstige = "";

    public String anzahlStimmenAktienregister = "";
    public String anzahlStimmenAktienregisterE = "";
    public String anzahlStimmenAktienregisterF = "";
    public String anzahlStimmenAktienregisterV = "";
    public String anzahlStimmenAktienregisterSonstige = "";

    /**pDbBundle muß geöffnet sein.
     * pRpDrucken muß mit "new" erzeugt sein, und Input-Parameter zur Ausgabe entsprechend vorbelegt werden
     */
    public void druckeStatistik(DbBundle pDbBundle, RpDrucken rpDrucken, String pLfdNummer) {

        rpDrucken.initFormular(pDbBundle);

        RpVariablen rpVariablen = new RpVariablen(pDbBundle);
        rpVariablen.statistikMeldung(pLfdNummer, rpDrucken);
        rpDrucken.startFormular();

        if (pDbBundle.param.paramBasis.namensaktienAktiv) {
            rpVariablen.fuelleVariable(rpDrucken, "NamensaktienAktiv", "1");
        }
        else {
            rpVariablen.fuelleVariable(rpDrucken, "NamensaktienAktiv", "0");
        }
        
        if (pDbBundle.param.paramBasis.inhaberaktienAktiv) {
            rpVariablen.fuelleVariable(rpDrucken, "InhaberaktienAktiv", "1");
        }
        else {
            rpVariablen.fuelleVariable(rpDrucken, "InhaberaktienAktiv", "0");
        }
        

        
        /*****/
        pDbBundle.dbJoined.read_angemeldeteAktionaersnummern(); //Liest tbl_meldungen, DISTINCT Aktionärsnummern
        int lAnzahlMeldungen = pDbBundle.dbJoined.anzErgebnisKey();
        anzahlMeldungen = CaString.toStringDE(lAnzahlMeldungen);
        /**Beinhaltet auch Null-Bestände*/
        rpVariablen.fuelleVariable(rpDrucken, "Angemeldet.AnzahlAnmeldungen", anzahlMeldungen);

        /*****/
        pDbBundle.dbJoined.read_eintrittskartennummernVersandart(); //Liest alle Willenserklärungen + Willenserklärungenzusatz mit Eintrittskartenbestellungen
        int lAnzahlEK = pDbBundle.dbJoined.anzErgebnisKey();
        anzahlEK = CaString.toStringDE(lAnzahlEK);
        int lAnzahlEKVersand = 0;
        int lAnzahlEKSelbstdruck = 0;
        int lAnzahlEKMail = 0;
        for (int i = 0; i < lAnzahlEK; i++) {
            String hString = pDbBundle.dbJoined.ergebnisKeyPosition(i);
            switch (hString) {
            case "1":
            case "2": {
                lAnzahlEKVersand++;
                break;
            }
            case "3": {
                lAnzahlEKSelbstdruck++;
                break;
            }
            case "4": {
                lAnzahlEKMail++;
                break;
            }
            }
        }

        anzahlEKVersand = CaString.toStringDE(lAnzahlEKVersand);
        anzahlEKSelbstdruck = CaString.toStringDE(lAnzahlEKSelbstdruck);
        anzahlEKMail = CaString.toStringDE(lAnzahlEKMail);

        rpVariablen.fuelleVariable(rpDrucken, "Angemeldet.AnzahlEintrittskartenGesamt", anzahlEK);
        rpVariablen.fuelleVariable(rpDrucken, "Angemeldet.AnzahlEintrittskartenPapierVersand", anzahlEKVersand);
        rpVariablen.fuelleVariable(rpDrucken, "Angemeldet.AnzahlEintrittskartenSelbstdruck", anzahlEKSelbstdruck);
        rpVariablen.fuelleVariable(rpDrucken, "Angemeldet.AnzahlEintrittskartenMail", anzahlEKMail);

        /***/
        int lAnzahlAktionaerePortalBestaetigt = 0;
        int lAnzahlAktionaereEmailVersand = 0;

        pDbBundle.dbJoined.read_aktionaerePortalHinweiseBestaetigt(); 
        int lAnzahl = pDbBundle.dbJoined.anzErgebnisLoginDaten();
        for (int i = 0; i < lAnzahl; i++) {
            EclLoginDaten eclLoginDaten = pDbBundle.dbJoined
                    .ergebnisLoginDatenPosition(i);
            int eVersandRegistrierung = eclLoginDaten.eVersandRegistrierung;
            int emailBestaetigt = eclLoginDaten.emailBestaetigt;
            if (eVersandRegistrierung == 99 && emailBestaetigt==1) {
                lAnzahlAktionaereEmailVersand++;
            }

            int hinweisAktionaersPortalBestaetigt = eclLoginDaten.hinweisAktionaersPortalBestaetigt;
            int hinweisHVPortalBestaetigt = eclLoginDaten.hinweisHVPortalBestaetigt;
            if (hinweisAktionaersPortalBestaetigt == 1 || hinweisHVPortalBestaetigt == 1) {
                lAnzahlAktionaerePortalBestaetigt++;
            }
        }

        anzahlAktionaereEmailVersand = CaString.toStringDE(lAnzahlAktionaereEmailVersand);
        anzahlAktionaerePortalBestaetigt = CaString.toStringDE(lAnzahlAktionaerePortalBestaetigt);

        /**Achtung - Zählung beginnt, wenn Bestätigungen zurückgesetzt wurden. D.h. eigentlich keine vernünftige Zahl.
         * Deshalb derzeit in Statistik-Formular entfernt.
         */
        rpVariablen.fuelleVariable(rpDrucken, "Portal.AnzahlAktionaereHinweiseBestaetigt",
                anzahlAktionaerePortalBestaetigt);

        rpVariablen.fuelleVariable(rpDrucken, "Portal.AnzahlAktionaereRegistriertEmailVersand",
                anzahlAktionaereEmailVersand);

        /***/
        long lAnzahlKIAV = 0;
        long lAnzahlDauer = 0;
        long lAnzahlSRV = 0;
        long lAnzahlBriefwahl = 0;
        long lAnzahlOrga = 0;

        long lAnzahlStimmenKIAV = 0;
        long lAnzahlStimmenDauer = 0;
        long lAnzahlStimmenSRV = 0;
        long lAnzahlStimmenBriefwahl = 0;
        long lAnzahlStimmenOrga = 0;

        int lanzSammel = 0;
        pDbBundle.dbJoined.read_aktionaereInSammelkarten(); //Liest alle Meldungen, die in Sammelkarte enthalten sind
        lanzSammel = pDbBundle.dbJoined.anzErgebnisMeldung();
        for (int i = 0; i < lanzSammel; i++) {
            int hMeldungEnthaltenInSammelkarteArt = pDbBundle.dbJoined
                    .ergebnisMeldungPosition(i).meldungEnthaltenInSammelkarteArt;
            long hStimmen = pDbBundle.dbJoined.ergebnisMeldungPosition(i).stimmen;
            switch (hMeldungEnthaltenInSammelkarteArt) {
            /*	 *  1=KIAV; 2=SRV; 3=organisatorisch; 4=Briefwahl 5=Dauervollmacht*/

            case 1: {
                lAnzahlKIAV++;
                lAnzahlStimmenKIAV += hStimmen;
                break;
            }
            case 2: {
                lAnzahlSRV++;
                lAnzahlStimmenSRV += hStimmen;
                break;
            }
            case 3: {
                lAnzahlOrga++;
                lAnzahlStimmenOrga += hStimmen;
                break;
            }
            case 4: {
                lAnzahlBriefwahl++;
                lAnzahlStimmenBriefwahl += hStimmen;
                break;
            }
            case 5: {
                lAnzahlDauer++;
                lAnzahlStimmenDauer += hStimmen;
                break;
            }
            }
        }

        anzahlBriefwahl = CaString.toStringDE(lAnzahlBriefwahl);
        anzahlKIAV = CaString.toStringDE(lAnzahlKIAV);
        anzahlDauer = CaString.toStringDE(lAnzahlDauer);
        anzahlOrga = CaString.toStringDE(lAnzahlOrga);
        anzahlSRV = CaString.toStringDE(lAnzahlSRV);

        anzahlStimmenBriefwahl = CaString.toStringDE(lAnzahlStimmenBriefwahl);
        anzahlStimmenKIAV = CaString.toStringDE(lAnzahlStimmenKIAV);
        anzahlStimmenDauer = CaString.toStringDE(lAnzahlStimmenDauer);
        anzahlStimmenOrga = CaString.toStringDE(lAnzahlStimmenOrga);
        anzahlStimmenSRV = CaString.toStringDE(lAnzahlStimmenSRV);

        rpVariablen.fuelleVariable(rpDrucken, "Angemeldet.AnzahlAktionaereBriefwahl", anzahlBriefwahl);
        rpVariablen.fuelleVariable(rpDrucken, "Angemeldet.AnzahlAktionaereSRV", anzahlSRV);
        rpVariablen.fuelleVariable(rpDrucken, "Angemeldet.AnzahlAktionaereKIAV", anzahlKIAV);
        rpVariablen.fuelleVariable(rpDrucken, "Angemeldet.AnzahlAktionaereDauer", anzahlDauer);
        rpVariablen.fuelleVariable(rpDrucken, "Angemeldet.AnzahlAktionaereOrga", anzahlOrga);

        rpVariablen.fuelleVariable(rpDrucken, "Angemeldet.StimmenAktionaereBriefwahl", anzahlStimmenBriefwahl);
        rpVariablen.fuelleVariable(rpDrucken, "Angemeldet.StimmenAktionaereSRV", anzahlStimmenSRV);
        rpVariablen.fuelleVariable(rpDrucken, "Angemeldet.StimmenAktionaereKIAV", anzahlStimmenKIAV);
        rpVariablen.fuelleVariable(rpDrucken, "Angemeldet.StimmenAktionaereDauer", anzahlStimmenDauer);
        rpVariablen.fuelleVariable(rpDrucken, "Angemeldet.StimmenAktionaereOrga", anzahlStimmenOrga);

        /***/
        long lAnzahlStimmen = 0;
        long lAnzahlStimmenE = 0;
        long lAnzahlStimmenF = 0;
        long lAnzahlStimmenV = 0;
        long lAnzahlStimmenSonstige = 0;
        int lanz = 0;
        pDbBundle.dbJoined.read_angemeldeteStimmen(); //Liest alle Meldungen ohne die Sammelkarten selbst
        lanz = pDbBundle.dbJoined.anzErgebnisMeldung();
        for (int i = 0; i < lanz; i++) {
            long hStimmen = pDbBundle.dbJoined.ergebnisMeldungPosition(i).stimmen;
            String hBesitzart = pDbBundle.dbJoined.ergebnisMeldungPosition(i).besitzart;
            lAnzahlStimmen += hStimmen;
            switch (hBesitzart) {
            case "E": {
                lAnzahlStimmenE += hStimmen;
                break;
            }
            case "F": {
                lAnzahlStimmenF += hStimmen;
                break;
            }
            case "V": {
                lAnzahlStimmenV += hStimmen;
                break;
            }
            default: {
                lAnzahlStimmenSonstige += hStimmen;
                break;
            }
            }
        }

        anzahlStimmen = CaString.toStringDE(lAnzahlStimmen);
        anzahlStimmenE = CaString.toStringDE(lAnzahlStimmenE);
        anzahlStimmenF = CaString.toStringDE(lAnzahlStimmenF);
        anzahlStimmenV = CaString.toStringDE(lAnzahlStimmenV);
        anzahlStimmenSonstige = CaString.toStringDE(lAnzahlStimmenSonstige);
        rpVariablen.fuelleVariable(rpDrucken, "Angemeldet.StimmenGesamt", anzahlStimmen);
        rpVariablen.fuelleVariable(rpDrucken, "Angemeldet.StimmenE", anzahlStimmenE);
        rpVariablen.fuelleVariable(rpDrucken, "Angemeldet.StimmenF", anzahlStimmenF);
        rpVariablen.fuelleVariable(rpDrucken, "Angemeldet.StimmenV", anzahlStimmenV);
        rpVariablen.fuelleVariable(rpDrucken, "Angemeldet.StimmenSonstige", anzahlStimmenSonstige);

        /*TODO Konsolidieren warum doppelt?*/
        long lAnzahlStimmenAktienregister = 0;
        long lAnzahlStimmenAktienregisterE = 0;
        long lAnzahlStimmenAktienregisterF = 0;
        long lAnzahlStimmenAktienregisterV = 0;
        long lAnzahlStimmenAktienregisterSonstige = 0;
        int lanzAktienregister = 0;
        pDbBundle.dbJoined.read_AktienregisterAktienDetail(); //Liest alle Aktienregistereinträge
        lanzAktienregister = pDbBundle.dbJoined.anzErgebnisAktienregisterEintrag();
        for (int i = 0; i < lanzAktienregister; i++) {
            long hStimmen = pDbBundle.dbJoined.ergebnisAktienregisterEintragPosition(i).stimmen;
            String hBesitzart = pDbBundle.dbJoined.ergebnisAktienregisterEintragPosition(i).besitzart;
            lAnzahlStimmenAktienregister += hStimmen;
            switch (hBesitzart) {
            case "E": {
                lAnzahlStimmenAktienregisterE += hStimmen;
                break;
            }
            case "F": {
                lAnzahlStimmenAktienregisterF += hStimmen;
                break;
            }
            case "V": {
                lAnzahlStimmenAktienregisterV += hStimmen;
                break;
            }
            default: {
                lAnzahlStimmenAktienregisterSonstige += hStimmen;
                break;
            }
            }
        }

        anzahlStimmenAktienregister = CaString.toStringDE(lAnzahlStimmenAktienregister);
        anzahlStimmenAktienregisterE = CaString.toStringDE(lAnzahlStimmenAktienregisterE);
        anzahlStimmenAktienregisterF = CaString.toStringDE(lAnzahlStimmenAktienregisterF);
        anzahlStimmenAktienregisterV = CaString.toStringDE(lAnzahlStimmenAktienregisterV);
        anzahlStimmenAktienregisterSonstige = CaString.toStringDE(lAnzahlStimmenAktienregisterSonstige);
        anzahlAktionaereInsgesamt = CaString.toStringDE(lanzAktienregister);
        rpVariablen.fuelleVariable(rpDrucken, "Aktienregister.StimmenGesamt", anzahlStimmenAktienregister);
        rpVariablen.fuelleVariable(rpDrucken, "Aktienregister.StimmenE", anzahlStimmenAktienregisterE);
        rpVariablen.fuelleVariable(rpDrucken, "Aktienregister.StimmenF", anzahlStimmenAktienregisterF);
        rpVariablen.fuelleVariable(rpDrucken, "Aktienregister.StimmenV", anzahlStimmenAktienregisterV);
        rpVariablen.fuelleVariable(rpDrucken, "Aktienregister.StimmenSonstige", anzahlStimmenAktienregisterSonstige);

        rpVariablen.fuelleVariable(rpDrucken, "Aktienregister.AnzahlAktionaere", anzahlAktionaereInsgesamt);

        for (int i = 1; i <= 5; i++) {
            String hI = Integer.toString(i);
            if (pDbBundle.param.paramBasis.getGattungAktiv(1)) {
                rpVariablen.fuelleVariable(rpDrucken, "Kapital.Gattung" + hI + "Bezeichnung",
                        pDbBundle.param.paramBasis.getGattungBezeichnung(i));
                rpVariablen.fuelleVariable(rpDrucken, "Kapital.Gattung" + hI + "Kapital",
                        pDbBundle.param.paramBasis.getGrundkapitalStueckString(i));
                rpVariablen.fuelleVariable(rpDrucken, "Kapital.WerteEinerAktie" + hI,
                        pDbBundle.param.paramBasis.getWertEinerAktieString(i));
            } else {
                rpVariablen.fuelleVariable(rpDrucken, "Kapital.Gattung" + hI + "Bezeichnung", "");
                rpVariablen.fuelleVariable(rpDrucken, "Kapital.Gattung" + hI + "Kapital", "");
                rpVariablen.fuelleVariable(rpDrucken, "Kapital.WerteEinerAktie" + hI, "");
            }
        }

        rpDrucken.druckenFormular();
        rpDrucken.endeFormular();

    }

}
