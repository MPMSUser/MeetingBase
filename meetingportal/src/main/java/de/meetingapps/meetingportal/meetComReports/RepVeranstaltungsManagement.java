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

import de.meetingapps.meetingportal.meetComBl.BlVeranstaltungen;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComStub.StubRoot;
import de.meetingapps.meetingportal.meetingCoreReport.RpDrucken;
import de.meetingapps.meetingportal.meetingCoreReport.RpVariablen;

public class RepVeranstaltungsManagement extends StubRoot {

    private RpDrucken rpDrucken = null;
    private RpVariablen rpVariablen = null;


    public RepVeranstaltungsManagement(boolean pIstServer, DbBundle pDbBundle) {
        super(pIstServer, pDbBundle);
    }

    public void init(RpDrucken pRpDrucken) {
        rpDrucken = pRpDrucken;
    }

    /**Einlesen und Drucken aller Aktionäre. Aktuell nicht-Stub-Fähig*/
    public void auswerten(DbBundle pDbBundle) {
        String lfdNummer="01";
        
        BlVeranstaltungen blVeranstaltungen=new BlVeranstaltungen(true, pDbBundle);
        blVeranstaltungen.erzeugeVeranstaltungslisteFuerTeilnehmer(BlVeranstaltungen.LAUT_MENUE_NUMMER, -1);
        
        pDbBundle.dbVeranstaltungenWert.readAll();
        blVeranstaltungen.rcVeranstaltungsWertListeGesamt=pDbBundle.dbVeranstaltungenWert.ergebnis();
        blVeranstaltungen.reportSummenErmitteln();
        
        
        rpDrucken.initListe(pDbBundle);

        rpVariablen = new RpVariablen(pDbBundle);
        rpVariablen.listeVeranstaltungsManagement(lfdNummer, rpDrucken);
        rpDrucken.startListe();

        blVeranstaltungen.reportSummenReportFuellen(rpDrucken, rpVariablen);
        
        rpDrucken.endeListe();
    }

    
//    public void aanmeldeListe(DbBundle pDbBundle, int pVeranstaltungsIdent) {
//        
//        /*Alle Werte, "Unique" für Element-Idents einlesen, um Überschrift bzw. Spalten zu ermitteln*/
//        /*XXX*/
//        
//        /*Alle Teilnehmer, die sich an/abgemeldet haben*/
//        /*XXX*/
//        
//    }
    
    

    
    
}
