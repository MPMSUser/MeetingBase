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

import de.meetingapps.meetingportal.meetComAllg.CaDateiWrite;
import de.meetingapps.meetingportal.meetComAllg.CaDatumZeit;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclKTracking;
import de.meetingapps.meetingportal.meetComStub.StubRoot;

public class BlKTracking extends StubRoot {

    /**Alle User, mit komplettem Inhalt; kann User auch mehrfach enthalten, wenn mehrfach geklickt*/
    public EclKTracking[] rcKTrackingArray = null;

    /**Anzahl der verschiedenen User*/
    public int rcAnzahlDistinctUser = 0;

    /**rcKTrackingArray in Export*/
    public String rcDateiName = "";
    public String rcDateiNamePur = "";

    public BlKTracking(boolean pIstServer, DbBundle pDbBundle) {
        super(pIstServer, pDbBundle);
    }

    /**pTag="" => alle; ansonsten TT.MM.JJJJ*/
    public int liefereUser(int pFunktion, String pTag) {
        /**Initialisieren für den Fall, dass nichts geliefert wurde*/
        rcKTrackingArray = new EclKTracking[0];
        rcDateiName = "";
        rcDateiNamePur = "";

        dbOpenUndWeitere();

        /*Anzahl der Gesamtuser einlesen*/
        lDbBundle.dbKTracking.readAllDistinct(pFunktion, pTag);
        rcAnzahlDistinctUser = lDbBundle.dbKTracking.anzErgebnis();

        
        /**Export-Datei erzeugen*/
        CaDateiWrite dateiExport = new CaDateiWrite();
        dateiExport.trennzeichen = ';';
        dateiExport.dateiart = ".csv";

        dateiExport.oeffne(lDbBundle, "klickListe");
        dateiExport.ausgabe(lDbBundle.eclEmittent.bezeichnungLang);
        dateiExport.newline();
        dateiExport.ausgabe("Ident");
        //              dateiExport.ausgabe("Funktion");
        dateiExport.ausgabe("Kennung");
        dateiExport.ausgabe("Datum Zeit");
        dateiExport.ausgabe("Datum Zeit Sort");
        dateiExport.newline();

        
        lDbBundle.dbKTracking.readAll(pFunktion, pTag);
        int anzahlUser = lDbBundle.dbKTracking.anzErgebnis();
        if (anzahlUser != 0) {
            rcKTrackingArray = lDbBundle.dbKTracking.ergebnisArray;

            for (int i = 0; i < rcKTrackingArray.length; i++) {
                EclKTracking lKTracking = rcKTrackingArray[i];
                dateiExport.ausgabe(Long.toString(lKTracking.ident));
                dateiExport.ausgabe(lKTracking.loginKennung);
                dateiExport.ausgabe(CaDatumZeit.DatumZeitStringFuerAnzeige(lKTracking.datumZeit));
                dateiExport.ausgabe(lKTracking.datumZeit);
                dateiExport.newline();
            }


        }

        dateiExport.schliessen();
        rcDateiName = dateiExport.dateiname;
        rcDateiNamePur = dateiExport.dateinamePur;

        dbClose();

        return 1;
    }

}
