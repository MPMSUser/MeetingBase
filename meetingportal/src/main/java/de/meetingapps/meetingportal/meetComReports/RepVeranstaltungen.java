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

import de.meetingapps.meetingportal.meetComAllg.CaDateiWrite;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclAktienregister;
import de.meetingapps.meetingportal.meetComEntities.EclAktienregisterErgaenzung;
import de.meetingapps.meetingportal.meetComEntities.EclVeranstaltung;
import de.meetingapps.meetingportal.meetingCoreReport.RpExcel;
import de.meetingapps.meetingportal.meetingCoreReport.RpExcelVariablen;

/**Zum Ermitteln der Summen der eingegangenen Willenserklärungen auf den unterschiedlichen Wegen*/
public class RepVeranstaltungen {

    private DbBundle dbBundle = null;

    /**Returnwert: Dateiname, in dem der Export liegt*/
    public String rcExportDateiname = "";
    public String rcExportExcelDateiname = "";

    /**pDbBundle muß geöffnet sein*/
    public RepVeranstaltungen(DbBundle pDbBundle) {
        dbBundle = pDbBundle;
    }

    /**Erstellt den Report und legt das Ergebnis in einer Export Datei ab,
     * die in rcExportDateiname zu finden ist.
     * 
     * pSeleketion:
     * 1=alle aktiven; 2=angemeldete; 3=abgemeldete*/
    public void exportTeilnehmer(int pVeranstaltungsNummer, int pSelektion) {

        String hUberschrift="";
        if (pVeranstaltungsNummer==0) {
            hUberschrift="Alle";
        }
        else {
            hUberschrift=Integer.toString(pVeranstaltungsNummer);
        }

        CaDateiWrite caDateiWrite = new CaDateiWrite();
        caDateiWrite.setzeFuerCSV();
        caDateiWrite.oeffne(dbBundle, "TeilnehmerVeranstaltung" + Integer.toString(pVeranstaltungsNummer));
        rcExportDateiname = caDateiWrite.dateiname;

        caDateiWrite.ausgabe("Teilnehmer Veranstaltung " + hUberschrift);
        caDateiWrite.newline();
        caDateiWrite.ausgabe("Nummer");
        caDateiWrite.ausgabe("Name");
        caDateiWrite.ausgabe("Vorname");
        caDateiWrite.ausgabe("Anmeldestatus");
        caDateiWrite.ausgabe("Anzahl Personen");
        caDateiWrite.ausgabe("Nummer Veranstaltung");
        caDateiWrite.newline();

        RpExcel rpExcel = new RpExcel();
        rpExcel.fillHeader(new String[] { "Nummer", "Name", "Vorname", "Anmeldestatus", "Anzahl Personen" , "Nummer Veranstaltung"});

        int anz=0;
        if (pVeranstaltungsNummer==0) {
            anz=dbBundle.dbVeranstaltung.readAllAktive();
        }
        else {
            anz=dbBundle.dbVeranstaltung.read(pVeranstaltungsNummer);
        }
        
         if (anz!=0) {

            for (int iVeranstaltung=0;iVeranstaltung<anz;iVeranstaltung++) {
                EclVeranstaltung lVeranstaltung=dbBundle.dbVeranstaltung.ergebnisPosition(iVeranstaltung);
                int lVeranstaltungIdent=lVeranstaltung.ident;
                
                if (lVeranstaltung.aktiv==1) {
                    dbBundle.dbAktienregisterErgaenzung.readAngemeldeteDialogveranstaltung(lVeranstaltungIdent);

                    for (int i = 0; i < dbBundle.dbAktienregisterErgaenzung.anzErgebnis(); i++) {
                        EclAktienregisterErgaenzung lAktienregisterErgaenzung = dbBundle.dbAktienregisterErgaenzung.ergebnisPosition(i);
                        String hAnmeldestatus = lAktienregisterErgaenzung.ergaenzungKurzString[lVeranstaltungIdent].substring(0, 1);
                        if (pSelektion == 1 || (pSelektion == 2 && hAnmeldestatus.equals("1")) || (pSelektion == 3 && hAnmeldestatus.equals("2"))) {
                            int aktienregisterIdent = lAktienregisterErgaenzung.aktienregisterIdent;

                            dbBundle.dbAktienregister.leseZuAktienregisterIdent(aktienregisterIdent);

                            EclAktienregister lAktienregister = dbBundle.dbAktienregister.ergebnisPosition(0);
                            String userNummer = lAktienregister.aktionaersnummer.substring(0, 10);
                            caDateiWrite.ausgabe(userNummer);
                            caDateiWrite.ausgabe(lAktienregister.nachname);
                            caDateiWrite.ausgabe(lAktienregister.vorname);

                            if (hAnmeldestatus.equals("1")) {
                                caDateiWrite.ausgabe("Angemeldet");
                            } else {
                                caDateiWrite.ausgabe("Abgemeldet");
                            }

                            caDateiWrite.ausgabe(Integer.toString(lAktienregisterErgaenzung.ergaenzungKennzeichen[lVeranstaltungIdent]));
                            caDateiWrite.ausgabe(Integer.toString(lVeranstaltung.ident));

                            caDateiWrite.newline();
                            int col = 0;
                            List<RpExcelVariablen> rpExcelVar = new ArrayList<>();
                            rpExcelVar.add(new RpExcelVariablen(userNummer, col++, RpExcel.TEXT));
                            rpExcelVar.add(new RpExcelVariablen(lAktienregister.nachname, col++, RpExcel.TEXT));
                            rpExcelVar.add(new RpExcelVariablen(lAktienregister.vorname, col++, RpExcel.TEXT));
                            rpExcelVar.add(new RpExcelVariablen(hAnmeldestatus.equals("1") ? "Angemeldet" : "Abgemeldet", col++, RpExcel.TEXT));
                            rpExcelVar.add(new RpExcelVariablen(Integer.toString(lAktienregisterErgaenzung.ergaenzungKennzeichen[lVeranstaltungIdent]), col++, RpExcel.TEXT));
                            rpExcelVar.add(new RpExcelVariablen(Integer.toString(lVeranstaltungIdent), col++, RpExcel.TEXT));

                            rpExcel.content.add(rpExcelVar);
                        }
                    }
                }
            }

        }
        
        caDateiWrite.newline();
        caDateiWrite.schliessen();

        rpExcel.create(dbBundle, "TeilnehmerVeranstaltung");
        rcExportExcelDateiname = rpExcel.dateiName;
    }
}
