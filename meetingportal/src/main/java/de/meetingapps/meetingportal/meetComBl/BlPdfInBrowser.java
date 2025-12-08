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

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclPdfInBrowser;

/**Logiken, um dynamische PDFs "Standalone" im Browser anzuzeigen (Nutzung durch App).
 * 
 * Grundsätzlicher Ablauf:
 * > das PDF wird in der normalen Businesslogik erzeugt und gespeichert
 * > Der Link auf dieses PDF wird zusammen mit einem Einmal-Key und einer Ident gespeichert.
 * > Ident und Einmal-Key wird an die App übermittelt.
 * > App startet Browser (neutral) und übergibt kompletten Link einschließlich Eimalkey und Ident als Parameter
 * > App startet Browser mit diesem Link - es wird eine Seite mit Button "PDF anzeigen / herunterladen" angezeigt
 * > Button überprüft Gültigkeit Einmalkey / Ident, und lädt dann das zugeordnete PDF "down". 
 */
public class BlPdfInBrowser {

    private int logDrucken = 3;

    private DbBundle dbBundle = null;

    public BlPdfInBrowser(DbBundle pDbBundle) {
        dbBundle = pDbBundle;
    }

    public EclPdfInBrowser erzeugeEinmalKey(String pVollstaendigerNamePdf) {
        EclPdfInBrowser lPdfInBrowser = new EclPdfInBrowser();
        lPdfInBrowser.eindeutigerKey = dbBundle.dbEindeutigerKey.getNextFree();
        dbBundle.reOpen();
        
        lPdfInBrowser.pdfLink = pVollstaendigerNamePdf;
        CaBug.druckeLog("A lPdfInBrowser.eindeutigerKey=" + lPdfInBrowser.eindeutigerKey,
                logDrucken, 10);

        dbBundle.dbPdfInBrowser.insert(lPdfInBrowser);
        CaBug.druckeLog("B lPdfInBrowser.eindeutigerKey=" + lPdfInBrowser.eindeutigerKey,
                logDrucken, 10);
        int anz = dbBundle.dbPdfInBrowser.readKey(lPdfInBrowser.eindeutigerKey);

        CaBug.druckeLog("dbBundle.dbPdfInBrowser.anzErgebnis()=" + dbBundle.dbPdfInBrowser.anzErgebnis(),
                logDrucken, 10);
        CaBug.druckeLog("anz=" + anz, logDrucken, 10);

        for (int i = 0; i < anz; i++) {
            System.out.println("i=" + i + " " + dbBundle.dbPdfInBrowser.ergebnisArray[i].ident);
        }
        if (dbBundle.dbPdfInBrowser.anzErgebnis() != 1) {
            CaBug.drucke("BlPdfInBrowser.erzeugeEinmalKey 001");
            return null;
        }
        lPdfInBrowser = dbBundle.dbPdfInBrowser.ergebnisPosition(0);
        return lPdfInBrowser;
    }

    /**Key wird gleichzeitig gelöscht*/
    public String liefereVollstaendigerNamePdf(long pIdent, String pEindeutigerKey) {
        dbBundle.dbPdfInBrowser.readKey(pEindeutigerKey);
        if (dbBundle.dbPdfInBrowser.anzErgebnis() != 1) {
            CaBug.drucke("BlPdfInBrowser.liefereVollstaendigerNamePdf 001");
            return "";
        }
        EclPdfInBrowser lPdfInBrowser = dbBundle.dbPdfInBrowser.ergebnisPosition(0);
        if (lPdfInBrowser.ident != pIdent) {
            CaBug.drucke("BlPdfInBrowser.liefereVollstaendigerNamePdf 002");
            return "";
        }
        //		dbBundle.dbPdfInBrowser.delete(lPdfInBrowser.ident);
        CaBug.druckeLog("lPdfInBrowser.pdfLink" + lPdfInBrowser.pdfLink, 
                logDrucken, 10);
        return lPdfInBrowser.pdfLink;
    }
}
