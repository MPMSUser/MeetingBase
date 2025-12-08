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
package de.meetingapps.meetingportal.meetComAllg;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;

import de.meetingapps.meetingportal.meetComDb.DbBundle;

public class CaDateiWrite {

    private FileWriter ausFile = null;
    private PrintWriter ausBuffer = null;
    public char trennzeichen = '#';
    /** mit ., also z.B. .txt */
    public String dateiart = ".txt";

    /** Liefert (nach Öffnen) den vollständigen Pfad und Dateiname des Exports */
    public String dateiname = "";

    /** Reiner Dateiname, ohne Pfad */
    public String dateinamePur = "";

    public final Charset iso8859 = Charset.forName("ISO-8859-1");

    /** Setzt die Basis-Parameter für CSV passend */
    public void setzeFuerCSV() {
        dateiart = ".csv";
        trennzeichen = ';';
    }

    /**
     * Öffnen einer Output-Datei in
     * <ClGlobalvar.laufwerk>\\meetingoutput\\<name><Mandant><DatumZeit><dateiart>
     */
    public void oeffne(DbBundle pDbBundle, String name) {

        dateiname = pDbBundle.lieferePfadMeetingOutput()
                + "\\"
                + name + "M" + pDbBundle.getMandantString() + "_" + CaDatumZeit.DatumZeitStringFuerDateiname()
                + dateiart;
        dateinamePur = name + "M" + pDbBundle.getMandantString() + "_" + CaDatumZeit.DatumZeitStringFuerDateiname()
                + dateiart;

        System.out.println("CaDateiWrite=" + dateiname);

        try {
//          ISO-8859-1 > Für Umlaute
            ausFile = new FileWriter(dateiname, iso8859);
            ausBuffer = new PrintWriter(ausFile);
        } catch (IOException e) {
            CaBug.drucke("CaDateiWrite.oeffne 001");
            e.printStackTrace();
        }
    }

    public void oeffneAktienregister(DbBundle pDbBundle, String name, String pfad) {

        final String pfadOrdner = pfad + "\\"+pDbBundle.paramServer.praefixPfadVerzeichnisse
                + "output\\" + pDbBundle.getMandantPfad();
        final File ordner = new File(pfadOrdner);
        if (!ordner.exists())
            System.out.println("Verzeichnis: " + ordner.mkdirs());

        dateiname = pfadOrdner + "\\" + name + "M" + pDbBundle.getMandantString() + "_"
                + CaDatumZeit.DatumZeitStringFuerDateiname() + dateiart;

        System.out.println("CaDateiWrite=" + dateiname);

        try {
//          ISO-8859-1 > Für Umlaute
            ausFile = new FileWriter(dateiname, iso8859);
            ausBuffer = new PrintWriter(ausFile);
        } catch (IOException e) {
            CaBug.drucke("CaDateiWrite.oeffne 001");
            e.printStackTrace();
        }
    }

    public void oeffneNameExplizit(DbBundle pDbBundle, String name) {

        dateiname = name + dateiart;

        try {
            ausFile = new FileWriter(dateiname);
            ausBuffer = new PrintWriter(ausFile);
        } catch (IOException e) {
            CaBug.drucke("CaDateiWrite.oeffneNameExplizit 001");
            e.printStackTrace();
        }
    }

    public void oeffneNameExplizitAppend(DbBundle pDbBundle, String name) {

        dateiname = name + dateiart;

        try {
            ausFile = new FileWriter(dateiname, true);
            ausBuffer = new PrintWriter(ausFile);
        } catch (IOException e) {
            CaBug.drucke("CaDateiWrite.oeffneNameExplizitAppend 001");
            e.printStackTrace();
        }

    }

    public void oeffneNameExplizitOhneBundle(String name) {

        try {
            ausFile = new FileWriter(name, iso8859);
            ausBuffer = new PrintWriter(ausFile);
        } catch (IOException e) {
            CaBug.drucke("CaDateiWrite.oeffneNameExplizit 001");
            e.printStackTrace();
        }
    }

    public void ausgabe(String pAusgabe) {
        if (pAusgabe == null) {
            pAusgabe = "null";
        }
        pAusgabe = pAusgabe.replace(trennzeichen, ' ');
        ausBuffer.print(pAusgabe + trennzeichen);
    }

    public void ausgabePlain(String pAusgabe) {
        ausBuffer.print(pAusgabe);
    }

    public void newline() {
        ausBuffer.println("");
        ausBuffer.flush();
    }

    public void schliessen() {
        ausBuffer.flush();
        ausBuffer.close();
    }

    public void deleteFile(int count) {

        if (count == 0)
            new File(dateiname).delete();
    }
}
