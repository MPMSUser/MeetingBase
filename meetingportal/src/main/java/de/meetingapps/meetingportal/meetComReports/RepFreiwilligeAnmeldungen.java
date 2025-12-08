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

import java.util.ArrayList;
import java.util.List;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaDateiWrite;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclAktienregisterErgaenzung;
import de.meetingapps.meetingportal.meetComEntities.EclMeldung;
import de.meetingapps.meetingportal.meetComHVParam.ParamSpezial;
import de.meetingapps.meetingportal.meetComKonst.KonstAktienregisterErgaenzung;
import de.meetingapps.meetingportal.meetingCoreReport.RpExcel;
import de.meetingapps.meetingportal.meetingCoreReport.RpExcelVariablen;

/**Zum Ermitteln der Summen der eingegangenen Willenserklärungen auf den unterschiedlichen Wegen*/
public class RepFreiwilligeAnmeldungen {

    private int logDrucken = 10;

    private DbBundle dbBundle = null;

    /**Returnwert: Dateiname, in dem der Export liegt*/
    public String rcExportDateiname = "";
    public String rcExportExcelDateiname = "";

    /**pDbBundle muß geöffnet sein*/
    public RepFreiwilligeAnmeldungen(DbBundle pDbBundle) {
        dbBundle = pDbBundle;
    }

    /**Erstellt den Report und legt das Ergebnis in einer Export Datei ab,
     * die in rcExportDateiname zu finden ist.
     * 
     * pSeleketion:
     * 1=alle aktiven; 2=alle angemeldeten; 3=abgemeldete 4=Präsenz-angemeldete; 5=Virtuell-Angemeldete*/
    public void exportTeilnehmer(int pSelektion) {

        CaBug.druckeLog("vor dbMeldungen Read", logDrucken, 10);
        dbBundle.dbMeldungen.leseAlleMeldungenFreiwilligeAnAbmeldung();
        CaBug.druckeLog("nach dbMeldungen Read", logDrucken, 10);

        CaDateiWrite caDateiWrite = new CaDateiWrite();
        caDateiWrite.setzeFuerCSV();
        caDateiWrite.oeffne(dbBundle, "FreiwilligeAnmeldungen");
        rcExportDateiname = caDateiWrite.dateiname;

        caDateiWrite.ausgabe("Freiwillige Anmeldungen");
        caDateiWrite.newline();
        caDateiWrite.ausgabe("Nummer");
        caDateiWrite.ausgabe("Name");
        caDateiWrite.ausgabe("Vorname");
        caDateiWrite.ausgabe("Anmeldestatus");
        caDateiWrite.ausgabe("Vertreter-Name");
        caDateiWrite.ausgabe("Vertreter-Vorname");
        caDateiWrite.newline();

        RpExcel rpExcel = new RpExcel();
        rpExcel.fillHeader(
                new String[] { "Nummer", "Name", "Vorname", "Anmeldestatus", "Vertreter-Name", "Vertreter-Vorname" });

        for (int i = 0; i < dbBundle.dbMeldungen.anzErgebnis(); i++) {
            EclMeldung lMeldung = dbBundle.dbMeldungen.meldungenArray[i];
            int hAnmeldestatus = lMeldung.vorlAnmeldung;
            if (pSelektion == 1 || (pSelektion == 2 && hAnmeldestatus != 1024)
                    || (pSelektion == 3 && hAnmeldestatus == 1024) || (pSelektion == 4 && (hAnmeldestatus & 4096) == 0)
                    || (pSelektion == 5 && (hAnmeldestatus & 4096) == 4096)) {
                int aktienregisterIdent = lMeldung.aktienregisterIdent;

                dbBundle.dbAktienregisterErgaenzung.readZuident(aktienregisterIdent);
                EclAktienregisterErgaenzung lAktienregisterErgaenzung = dbBundle.dbAktienregisterErgaenzung.ergebnisPosition(0);

                String userNummer = lMeldung.aktionaersnummer.substring(0, 10);

                caDateiWrite.ausgabe(userNummer);
                caDateiWrite.ausgabe(lMeldung.name);
                caDateiWrite.ausgabe(lMeldung.vorname);

                int col = 0;
                List<RpExcelVariablen> rpExcelVar = new ArrayList<>();
                rpExcelVar.add(new RpExcelVariablen(userNummer, col++, RpExcel.TEXT));
                rpExcelVar.add(new RpExcelVariablen(lMeldung.name, col++, RpExcel.TEXT));
                rpExcelVar.add(new RpExcelVariablen(lMeldung.vorname, col++, RpExcel.TEXT));

                if (hAnmeldestatus == 1024) {
                    caDateiWrite.ausgabe("Abgemeldet");
                    rpExcelVar.add(new RpExcelVariablen("Abgemeldet", col++, RpExcel.TEXT));
                } else {
                    String hAnmeldetext = "Angemeldet";
                    if ((hAnmeldestatus & 2048) == 2048) {
                        hAnmeldetext = hAnmeldetext + " Bevollmächtigter";
                    }
                    if ((hAnmeldestatus & 4096) == 4096) {
                        hAnmeldetext = hAnmeldetext + " Online ";
                    }
                    caDateiWrite.ausgabe(hAnmeldetext);
                    rpExcelVar.add(new RpExcelVariablen(hAnmeldetext, col++, RpExcel.TEXT));
                }

                if (ParamSpezial.ku216(dbBundle.param.mandant)) {
                    caDateiWrite.ausgabe(lMeldung.vertreterName);
                    caDateiWrite.ausgabe("");
                    rpExcelVar.add(new RpExcelVariablen(
                            lMeldung.vertreterName,
                            col++, RpExcel.TEXT));
                    rpExcelVar.add(new RpExcelVariablen(
                            "",
                            col++, RpExcel.TEXT));
                }
                else {
                    caDateiWrite.ausgabe(lAktienregisterErgaenzung.ergaenzungLangString[KonstAktienregisterErgaenzung.ku178_DirekterVertreterName]);
                    caDateiWrite.ausgabe(lAktienregisterErgaenzung.ergaenzungLangString[KonstAktienregisterErgaenzung.ku178_DirekterVertreterVorname]);
                    rpExcelVar.add(new RpExcelVariablen(
                            lAktienregisterErgaenzung.ergaenzungLangString[KonstAktienregisterErgaenzung.ku178_DirekterVertreterName],
                            col++, RpExcel.TEXT));
                    rpExcelVar.add(new RpExcelVariablen(
                            lAktienregisterErgaenzung.ergaenzungLangString[KonstAktienregisterErgaenzung.ku178_DirekterVertreterVorname],
                            col++, RpExcel.TEXT));
                }
                 
                caDateiWrite.newline();
                rpExcel.content.add(rpExcelVar);

            }
        }

        caDateiWrite.newline();
        caDateiWrite.schliessen();
        
        rpExcel.create(dbBundle, "FreiwilligeAnmeldungen");
        rcExportExcelDateiname = rpExcel.dateiName;
    }
}
