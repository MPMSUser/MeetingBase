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

import de.meetingapps.meetingportal.meetComAllg.CaDateiWrite;
import de.meetingapps.meetingportal.meetComAllg.CaDatumZeit;
import de.meetingapps.meetingportal.meetComAllg.CaString;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclAktienregister;
import de.meetingapps.meetingportal.meetComEntities.EclAnrede;
import de.meetingapps.meetingportal.meetComEntities.EclLoginDaten;
import de.meetingapps.meetingportal.meetingCoreReport.RpDrucken;
import de.meetingapps.meetingportal.meetingCoreReport.RpVariablen;

public class RepMailRegistrierung {

    public boolean mitExport = false;
    public String exportDatei = "";

    /**1=alle; 2=alle, die nicht angemeldet sind (zur HV)*/
    public int selektion = 0;

    /**Wenn gefüllt, dann werden nur die ausgegeben, die sich bis zu diesem Stichtag registriert haben.
     * Übergabe in der Form TT.MM.JJJJ.
     * Wird dann intern umgesetzt in die table-Kompatible Form JJJJ.MM.TT
     */
    public String mailRegistrierungBis = "";

    private DbBundle dbBundle = null;

    public void druckeMailRegistrierung(DbBundle pDbBundle, RpDrucken rpDrucken, String pLfdNummer) {

        dbBundle = pDbBundle;

        CaDateiWrite dateiExport = null;
        if (mitExport) {
            dateiExport = new CaDateiWrite();
            dateiExport.trennzeichen = ';';
            dateiExport.dateiart = ".csv";

            if (selektion == 1) {
                dateiExport.oeffne(pDbBundle, "mailRegistrierung");
            } else {
                dateiExport.oeffne(pDbBundle, "mailRegistrierungOhneAnmeldung");
            }

            dateiExport.ausgabe("Aktionärsnummer");
            dateiExport.ausgabe("Name");
            dateiExport.ausgabe("Vorname");
            dateiExport.ausgabe("Ort");
            dateiExport.ausgabe("Aktien");
            dateiExport.ausgabe("Mailadresse");
            dateiExport.ausgabe("DatumRegistrierung");
            dateiExport.ausgabe("KompletteAnrede");
            dateiExport.newline();
        }

        rpDrucken.initListe(pDbBundle);

        RpVariablen rpVariablen = new RpVariablen(pDbBundle);
        rpVariablen.emailVersandListe(pLfdNummer, rpDrucken);
        rpDrucken.startListe();

        String mailRegistrierungBisIntern = "";
        if (mailRegistrierungBis.length() == 10) {
            mailRegistrierungBisIntern = mailRegistrierungBis.substring(6) + "." + mailRegistrierungBis.substring(3, 5)
                    + "." + mailRegistrierungBis.substring(0, 2);
        }

        int rc = dbBundle.dbJoined.read_emailVersandRegistrierung(selektion);

        if (rc > 0) {
            for (int i = 0; i < rc; i++) {
                String hString = dbBundle.dbJoined.ergebnisKeyPosition(i);
                int lAktienregisterIdent = Integer.parseInt(hString);
                
                dbBundle.dbAktienregister.leseZuAktienregisterIdent(lAktienregisterIdent);
                EclAktienregister lAktienregisterEintrag = new EclAktienregister();
                lAktienregisterEintrag = dbBundle.dbAktienregister.ergebnisPosition(0);

                dbBundle.dbLoginDaten.read_aktienregisterIdent(lAktienregisterIdent);
                EclLoginDaten lLoginDaten=dbBundle.dbLoginDaten.ergebnisPosition(0);


                if (lAktienregisterEintrag.stimmen > 0 && (mailRegistrierungBisIntern.isEmpty()
                        || lLoginDaten.eVersandRegistrierungErstZeitpunkt.isEmpty() || mailRegistrierungBisIntern
                                .compareTo(lLoginDaten.eVersandRegistrierungErstZeitpunkt) >= 0)) {
                    String datumRegistrierungExtern = CaDatumZeit
                            .DatumZeitStringFuerAnzeige(lLoginDaten.eVersandRegistrierungErstZeitpunkt);
                    rpVariablen.fuelleFeld(rpDrucken, "Nummern.Aktionaersnummer",
                            lAktienregisterEintrag.aktionaersnummer);
                    rpVariablen.fuelleFeld(rpDrucken, "Aktionaer.Name", lAktienregisterEintrag.nachname);
                    rpVariablen.fuelleFeld(rpDrucken, "Aktionaer.Vorname", lAktienregisterEintrag.vorname);
                    rpVariablen.fuelleFeld(rpDrucken, "Aktionaer.Ort", lAktienregisterEintrag.ort);
                    rpVariablen.fuelleFeld(rpDrucken, "Aktionaer.mail", lLoginDaten.eMailFuerVersand);
                    rpVariablen.fuelleFeld(rpDrucken, "Aktionaer.DatumRegistrierung", datumRegistrierungExtern);
                    rpVariablen.fuelleFeld(rpDrucken, "Besitz.Stimmen", Long.toString(lAktienregisterEintrag.stimmen));
                    rpVariablen.fuelleFeld(rpDrucken, "Besitz.StimmenDE",
                            CaString.toStringDE(lAktienregisterEintrag.stimmen));

                    if (mitExport) {

                        /*Anrede füllen*/
                        String briefanredeDE = "", briefanredeEN = "", kompletteAnredeDE = "", kompletteAnredeEN = "";
                        int anredenNr = lAktienregisterEintrag.anredeId;
                        EclAnrede hAnrede = new EclAnrede();
                        if (anredenNr != 0) {
                            dbBundle.dbAnreden.SetzeSprache(2, 0);
                            dbBundle.dbAnreden.ReadAnrede_Anredennr(anredenNr);
                            hAnrede = new EclAnrede();
                            if (dbBundle.dbAnreden.AnzAnredenInReadArray > 0) {
                                hAnrede = dbBundle.dbAnreden.anredenreadarray[0];
                            }
                            briefanredeDE = hAnrede.anredenbrief;
                            briefanredeEN = hAnrede.anredenbrieffremd;
                        }
                        kompletteAnredeDE = briefanredeDE;
                        kompletteAnredeEN = briefanredeEN;
                        if (hAnrede.istjuristischePerson != 1) {
                            if (lAktienregisterEintrag.titel.length() != 0) {
                                kompletteAnredeDE = kompletteAnredeDE + " " + lAktienregisterEintrag.titel;
                                kompletteAnredeEN = kompletteAnredeEN + " " + lAktienregisterEintrag.titel;
                            }
                            if (lAktienregisterEintrag.nachname.length() != 0) {
                                kompletteAnredeDE = kompletteAnredeDE + " " + lAktienregisterEintrag.nachname;
                                kompletteAnredeEN = kompletteAnredeEN + " " + lAktienregisterEintrag.nachname;
                            }
                        }

                        dateiExport.ausgabe(lAktienregisterEintrag.aktionaersnummer);
                        dateiExport.ausgabe(lAktienregisterEintrag.nachname);
                        dateiExport.ausgabe(lAktienregisterEintrag.vorname);
                        dateiExport.ausgabe(lAktienregisterEintrag.ort);
                        dateiExport.ausgabe(Long.toString(lAktienregisterEintrag.stimmen));
                        dateiExport.ausgabe(lLoginDaten.eMailFuerVersand);
                        dateiExport.ausgabe(datumRegistrierungExtern);
                        dateiExport.ausgabe(kompletteAnredeDE);
                        dateiExport.newline();
                    }

                    rpDrucken.druckenListe();
                }

            }
        }
        rpDrucken.endeListe();

        if (mitExport) {
            dateiExport.schliessen();
            exportDatei = dateiExport.dateiname;
        }
    }

}
