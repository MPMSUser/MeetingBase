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
import de.meetingapps.meetingportal.meetComEntities.EclAuftrag;
import de.meetingapps.meetingportal.meetComKonst.KonstAuftragArt;
import de.meetingapps.meetingportal.meetComKonst.KonstAuftragModul;

/**Zum Ermitteln der Summen der eingegangenen Willenserklärungen auf den unterschiedlichen Wegen*/
public class RepMitgliederportal {

    private DbBundle dbBundle = null;

    /**Returnwert: Dateiname, in dem der Export liegt*/
    public String rcExportDateiname = "";

    /**pDbBundle muß geöffnet sein*/
    public RepMitgliederportal(DbBundle pDbBundle) {
        dbBundle = pDbBundle;
    }

    /**Erstellt den Report und legt das Ergebnis in einer Export Datei ab,
     * die in rcExportDateiname zu finden ist.
     * pVonDatum, pBisDatum 
     * > in der Form TT.MM.JJJJ HH:MM:SS. 
     * > Kann leer sein, dann von beginn bzw. bis Ende
     * > kann ohne Uhrzeit sein*/
    public void exportTickets(String pVonDatum, String pBisDatum) {

        /*Übergabeparameter aufbereiten*/
        if (pVonDatum.length() == 10) {
            pVonDatum = pVonDatum + " 00:00:00";
        }
        if (pBisDatum.length() == 10) {
            pBisDatum = pBisDatum + " 24:00:00";
        }
        String lVonDatum = CaDatumZeit.DatumZeitStringFuerDatenbank(pVonDatum);
        String lBisDatum = CaDatumZeit.DatumZeitStringFuerDatenbank(pBisDatum);

        
        dbBundle.dbAuftrag.read_auftraegeVorhanden(KonstAuftragModul.ANBINDUNG_AKTIENREGISTER, lVonDatum, lBisDatum);
        
        CaDateiWrite caDateiWrite = new CaDateiWrite();
        caDateiWrite.setzeFuerCSV();
        caDateiWrite.oeffne(dbBundle, "RemoteAnbindungTickets");
        rcExportDateiname = caDateiWrite.dateiname;

        caDateiWrite.ausgabe("Tickets");
        caDateiWrite.newline();
        caDateiWrite.ausgabe("User");
        caDateiWrite.ausgabe("BO-Ident");
        caDateiWrite.ausgabe("Erstellung");
        caDateiWrite.ausgabe("Ticket-Art");
        caDateiWrite.ausgabe("Parameter");
        
        caDateiWrite.newline();
        

        for (int i=0;i<dbBundle.dbAuftrag.anzErgebnis();i++) {
            EclAuftrag lAuftrag=dbBundle.dbAuftrag.ergebnisPosition(i);
            String userNummer="";
            
            int userIdent=lAuftrag.userIdAuftraggeber*(-1);
            dbBundle.dbLoginDaten.read_ident(userIdent);
            if (dbBundle.dbLoginDaten.anzErgebnis()>0) {
                userNummer=dbBundle.dbLoginDaten.ergebnisPosition(0).loginKennung.substring(0, 10);
            }
            else {
                userNummer="gelöscht";
            }
            caDateiWrite.ausgabe(userNummer);
            caDateiWrite.ausgabe(Integer.toString(lAuftrag.ident));
            caDateiWrite.ausgabe(lAuftrag.zeitStart);
            caDateiWrite.ausgabe(KonstAuftragArt.getText(lAuftrag.auftragsArt));
            
            caDateiWrite.ausgabe(Integer.toString(lAuftrag.parameterInt[0]));
            
            for (int i1=0;i1<6;i1++) {
                caDateiWrite.ausgabe(lAuftrag.parameterTextLang[i1]);
            }
            
            caDateiWrite.newline();
        }
        
        caDateiWrite.newline();
        caDateiWrite.schliessen();

    }

    /**Erstellt den Report und legt das Ergebnis in einer Export Datei ab,
     * die in rcExportDateiname zu finden ist.
     * pVonDatum, pBisDatum 
     * > in der Form TT.MM.JJJJ HH:MM:SS. 
     * > Kann leer sein, dann von beginn bzw. bis Ende
     * > kann ohne Uhrzeit sein*/
    public void exportKontaktanfragenTickets(String pVonDatum, String pBisDatum) {

        /*Übergabeparameter aufbereiten*/
        if (pVonDatum.length() == 10) {
            pVonDatum = pVonDatum + " 00:00:00";
        }
        if (pBisDatum.length() == 10) {
            pBisDatum = pBisDatum + " 24:00:00";
        }
        String lVonDatum = CaDatumZeit.DatumZeitStringFuerDatenbank(pVonDatum);
        String lBisDatum = CaDatumZeit.DatumZeitStringFuerDatenbank(pBisDatum);

        dbBundle.dbAuftrag.mandantenabhaengig=false;
        dbBundle.dbAuftrag.read_auftraegeVorhanden(KonstAuftragModul.KONTAKT_FORMULAR, lVonDatum, lBisDatum);
        dbBundle.dbAuftrag.mandantenabhaengig=true;
        
        CaDateiWrite caDateiWrite = new CaDateiWrite();
        caDateiWrite.setzeFuerCSV();
        caDateiWrite.oeffne(dbBundle, "KontaktformularTickets");
        rcExportDateiname = caDateiWrite.dateiname;

        caDateiWrite.ausgabe("Kontaktformular-Tickets");
        caDateiWrite.newline();
        caDateiWrite.ausgabe("User");
        caDateiWrite.ausgabe("BO-Ident");
        caDateiWrite.ausgabe("Erstellung");
        caDateiWrite.ausgabe("Ticket-Art");
        caDateiWrite.ausgabe("Parameter");
        
        caDateiWrite.newline();
        

        for (int i=0;i<dbBundle.dbAuftrag.anzErgebnis();i++) {
            EclAuftrag lAuftrag=dbBundle.dbAuftrag.ergebnisPosition(i);
            int userIdent=lAuftrag.userIdAuftraggeber*(-1);
            dbBundle.dbLoginDaten.read_ident(userIdent);
            String userNummer=dbBundle.dbLoginDaten.ergebnisPosition(0).loginKennung.substring(0, 10);
            caDateiWrite.ausgabe(userNummer);
            caDateiWrite.ausgabe(Integer.toString(lAuftrag.ident));
            caDateiWrite.ausgabe(lAuftrag.zeitStart);
            caDateiWrite.ausgabe(KonstAuftragArt.getText(lAuftrag.auftragsArt));

            caDateiWrite.ausgabe(lAuftrag.parameterTextLang[0]);
            caDateiWrite.ausgabe(lAuftrag.parameterTextLang[1]);

            caDateiWrite.ausgabe(CaString.entferneSteuerzeichenKomplett(lAuftrag.freitextBeschreibung));
             
            caDateiWrite.newline();
        }
        
        caDateiWrite.newline();
        caDateiWrite.schliessen();

    }


}
