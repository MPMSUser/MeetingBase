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
package de.meetingapps.meetingportal.meetComBl;

import java.util.LinkedList;
import java.util.List;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaDateiVerwaltung;
import de.meetingapps.meetingportal.meetComAllg.CaDatumZeit;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclAktienregisterErgaenzung;
import de.meetingapps.meetingportal.meetComEntities.EclBestWorkflow;
import de.meetingapps.meetingportal.meetComEntities.EclMeldung;
import de.meetingapps.meetingportal.meetComEntities.EclVorlaeufigeVollmachtFuerAnzeige;
import de.meetingapps.meetingportal.meetComKonst.KonstAktienregisterErgaenzung;

/*Infos zum Workflow
 * 
 * Einfügen der PDFs in den Workflow: 
 * > im DLOGIN, ganz unten ...
 * > PDFS müssen sein in D:\\ku178_Scan
 */

public class BlBestaetigungsWorkflow {

    int logDrucken=10;
    
    /**++++++Gefüllt von importiereBestaetigungsPdf+++++++*/
    
    public int rcDateienInsgesamtImVerzeichnis = 0;

    /**Gefüllt von statistik*/
    
    /*Anzahl offene Erst-Prüfungen (Papier)*/
    public int rcAnzahlOffenBasisPruefen = 0;

    /*Anzahl offene Erst-Prüfungen (Digital)*/
    public int rcAnzahlOffenBasisPruefenDigital = 0;

    /*Anzahl offene Prüfungen zur Wiedervorlage (Papier)*/
    public int rcAnzahlOffenWiedervorlage = 0;

    /*Anzahl offene Prüfungen zur Wiedervorlage (Digital)*/
    public int rcAnzahlOffenWiedervorlageDigital = 0;

    /*Anzahl Vertretungen abgelehnt*/
    public int rcAnzahlAbgelehnt = 0;
    
    /*Anzahl Vertretungen akzeptiert*/
    public int rcAnzahlAngenommen = 0;
    
    /*Anzahl Bearbeitung nicht abgeschlossen*/
    public int rcAnzahlInBearbeitung = 0;

    /*Anzahl Bearbeitung nicht abgeschlossen (Digital)*/
    public int rcAnzahlInBearbeitungDigital = 0;

    /*Anzahl nicht als Nachweis verwenden*/
    public int rcAnzahlAndereDokumente = 0;

    
    
    private DbBundle dbBundle = null;

    public BlBestaetigungsWorkflow(DbBundle pDbBundle) {
        dbBundle = pDbBundle;
    }

    /**Direkter Rückgabewert: Anzahl importierter Dateien
     * Ansonsten: 
     * rcDateienInsgesamtImVerzeichnis*/

    public int importiereBestaetigungsPdf() {
        rcDateienInsgesamtImVerzeichnis = 0;
        int anzorig = importiereBestaetigungsPDFVerzeichnis(1);
        int anzkopie = importiereBestaetigungsPDFVerzeichnis(2);
        return anzorig + anzkopie;
    }

    private int importiereBestaetigungsPDFVerzeichnis(int pOrigOderKopie) {
        int anzImportiert = 0;
        String[] dateienIngesamt = null;
        String subverzeichnis = "";
        if (pOrigOderKopie == 1) {
            subverzeichnis = "orig";
        } else {
            subverzeichnis = "kopie";
        }

        CaDateiVerwaltung caDateiVerwaltung = new CaDateiVerwaltung();
        String verzeichnisName = dbBundle.lieferePfadMeetingReports()
                + "\\" + subverzeichnis;
        dateienIngesamt = caDateiVerwaltung.leseDateienInVerzeichnis(verzeichnisName);
        int anzahlDateienVerzeichnis = dateienIngesamt.length;
        rcDateienInsgesamtImVerzeichnis += anzahlDateienVerzeichnis;

        for (int i = 0; i < anzahlDateienVerzeichnis; i++) {
            int rc = dbBundle.dbBestWorkflow.readZuDatei(dateienIngesamt[i]);
            if (rc == 0) {
                /*Dann neu einfügen*/
                EclBestWorkflow lBestWorkflow = new EclBestWorkflow();
                lBestWorkflow.subverzeichnis = subverzeichnis;
                lBestWorkflow.origOderKopie = pOrigOderKopie;
                lBestWorkflow.dateinameBestaetigung = dateienIngesamt[i];
                lBestWorkflow.dateinameImportAm = CaDatumZeit.DatumZeitStringFuerDatenbank();
                lBestWorkflow.mandant = dbBundle.clGlobalVar.mandant;
                dbBundle.dbBestWorkflow.insert(lBestWorkflow);
                anzImportiert++;
            }
        }

        return anzImportiert;

    }

    public void statistik() {
        rcAnzahlOffenBasisPruefen = dbBundle.dbBestWorkflow.readAnzahl(0);
        rcAnzahlOffenWiedervorlage = dbBundle.dbBestWorkflow.readAnzahl(3);
        rcAnzahlAbgelehnt = dbBundle.dbBestWorkflow.readAnzahl(2);
        rcAnzahlAngenommen = dbBundle.dbBestWorkflow.readAnzahl(1);
        rcAnzahlAndereDokumente = dbBundle.dbBestWorkflow.readAnzahl(4);
        rcAnzahlInBearbeitung = dbBundle.dbBestWorkflow.readAnzahlInBearbeitung();
        rcAnzahlOffenBasisPruefenDigital=dbBundle.dbVorlaeufigeVollmachtEingabe.readAnzahl(0);
        rcAnzahlOffenWiedervorlageDigital=dbBundle.dbVorlaeufigeVollmachtEingabe.readAnzahl(102);
        rcAnzahlInBearbeitungDigital=dbBundle.dbVorlaeufigeVollmachtEingabe.readAnzahlInBearbeitung();
    }

    
    /*========================================Workflowprüfung ku216===========================================================
     */
    
    public List<EclVorlaeufigeVollmachtFuerAnzeige> rcVollmachtenFuerAuswahl=null;
    public EclMeldung rcMeldung=null;
    public EclAktienregisterErgaenzung rcAktienregisterErgaenzung=null;
    public EclVorlaeufigeVollmachtFuerAnzeige rcVorlaeufigeVollmachtFuerAnzeige=null;
    public int rcGruppe=0;
    
    public void initGesetzlicheVertreter(boolean pOhneAufHinterlegtGeprueft) {
        initManuellePruefung(true, pOhneAufHinterlegtGeprueft);
     }
    
    public void initVollmachten(boolean pOhneAufHinterlegtGeprueft) {
        initManuellePruefung(false, pOhneAufHinterlegtGeprueft);
    }
    
    private void initManuellePruefung(boolean pGesetzlicheVertreter, boolean pOhneAufHinterlegtGeprueft) {
        rcVollmachtenFuerAuswahl=new LinkedList<EclVorlaeufigeVollmachtFuerAnzeige>();

        /*Lese alle Meldungen, die zu prüfen sind*/
        dbBundle.dbMeldungen.leseAlleUngeprueften();
        int anz=dbBundle.dbMeldungen.meldungenArray.length;
        CaBug.druckeLog("anz="+anz, logDrucken, 10);
        /*Alle gelesenen Meldungen durcharbeiten*/
        for (int i=0;i<anz;i++) {
            CaBug.druckeLog("i="+i, logDrucken, 10);
            rcMeldung=dbBundle.dbMeldungen.meldungenArray[i];

            if (pOhneAufHinterlegtGeprueft==false || rcMeldung.zusatzfeld1.equals("-1")) {
                CaBug.druckeLog("Erstes If erfüllt", logDrucken, 10);

                leseDatenZuMeldung(pGesetzlicheVertreter);
                
                /*Abhgängig von Gruppe und pGesetzlicheVertreter weitermachen oder nicht.*/
                boolean lBevollmächtigterAngemeldet=false;
                if ((rcMeldung.vorlAnmeldung & 2048) == 2048) {
                    lBevollmächtigterAngemeldet=true; 
                }
                if ((!lBevollmächtigterAngemeldet)==pGesetzlicheVertreter) {
                    CaBug.druckeLog("Zweites If erfüllt", logDrucken, 10);
                    /*Lese AktienregisterErgänzung wegen Vollmachtsdaten*/

                    rcVollmachtenFuerAuswahl.add(rcVorlaeufigeVollmachtFuerAnzeige);

                }
            }

        }
        
    }
    
    private void leseDatenZuMeldung(boolean pGesetzlicheVertreter) {
        CaBug.druckeLog("pGesetzlicheVertreter="+pGesetzlicheVertreter, logDrucken, logDrucken);
        /*Lese Aktienregister wegen Gruppe*/
        dbBundle.dbAktienregister.leseZuAktienregisterIdent(rcMeldung.aktienregisterIdent);
        rcGruppe=dbBundle.dbAktienregister.ergebnisPosition(0).gruppe;

        dbBundle.dbAktienregisterErgaenzung.readZuident(rcMeldung.aktienregisterIdent);
        rcAktienregisterErgaenzung=dbBundle.dbAktienregisterErgaenzung.ergebnisPosition(0);

        /*Ziel-Objekt füllen*/
        rcVorlaeufigeVollmachtFuerAnzeige=new EclVorlaeufigeVollmachtFuerAnzeige();

        rcVorlaeufigeVollmachtFuerAnzeige.bevollmaechtigterIstGesetzlich=pGesetzlicheVertreter;

        rcVorlaeufigeVollmachtFuerAnzeige.aktionaerNummerFuerAnzeige=BlNummernformBasis.aufbereitenInternFuerExtern(rcMeldung.aktionaersnummer, dbBundle);
        rcVorlaeufigeVollmachtFuerAnzeige.aktionaerName=rcMeldung.name;
        rcVorlaeufigeVollmachtFuerAnzeige.aktionaerVorname=rcMeldung.vorname;
        rcVorlaeufigeVollmachtFuerAnzeige.aktionaerOrt=rcMeldung.ort;

        rcVorlaeufigeVollmachtFuerAnzeige.bevollmaechtigterName=rcAktienregisterErgaenzung.ergaenzungLangString[KonstAktienregisterErgaenzung.ku178_VERTRETER1_NAME];
        rcVorlaeufigeVollmachtFuerAnzeige.bevollmaechtigterOrt=rcAktienregisterErgaenzung.ergaenzungLangString[KonstAktienregisterErgaenzung.ku178_VERTRETER1_ORT];

        rcVorlaeufigeVollmachtFuerAnzeige.meldeIdent=rcMeldung.meldungsIdent;
        
    }
    
    public boolean holeVollmachtsfall(int pMeldeIdent, boolean pGesetzlicheVertreter) {
        dbBundle.dbMeldungen.leseZuMeldungsIdent(pMeldeIdent);
        rcMeldung=dbBundle.dbMeldungen.meldungenArray[0];
        if (!rcMeldung.zusatzfeld1.equals("-1") && !rcMeldung.zusatzfeld1.equals("-2")) {
            return false;
        }
        leseDatenZuMeldung(pGesetzlicheVertreter);
        return true;
    }
    
    
    
}
