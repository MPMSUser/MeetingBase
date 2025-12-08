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
package de.meetingapps.meetingportal.meetComDb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComEntities.EclPraesenz;

/*TODO _Datenbanken konsolidieren - tbl_prasenz eliminieren!*/

/**Kennzeichen, ob bzw. wodurch die aktuelle Melung präsenz ist.
 * 
 * Nur Temporär! Schnelle Lösung für Seminar-Pilot! Nicht dauerhaft konzipiert!
 *  
 *
 */
public class DbPraesenz {

    private Connection verbindung = null;
    private DbBasis VMcdbBasis = null;
    private DbBundle VMcdbBundle = null;

    /**Zugriff nur über die Zugriffsfunktionen!
     */
    public EclPraesenz praesenzArray[] = null;

    /*************************Initialisierung***************************/
    /* Verbindung in lokale Daten eintragen*/
    public DbPraesenz(DbBundle datenbankbundle) {
        if (datenbankbundle.dbBasis == null) {
            System.err.println("vmcdbBasis nicht initialisiert");
            return;
        }
        VMcdbBasis = datenbankbundle.dbBasis;
        verbindung = datenbankbundle.dbBasis.verbindung;
        VMcdbBundle = datenbankbundle;
    }

    public int anzPrasenzGefunden() {
        if (praesenzArray == null) {
            return 0;
        }
        return praesenzArray.length;
    }

    public EclPraesenz praesenzGefunden(int lfd) {
        return praesenzArray[lfd];
    }

    /**************************deleteAll Löschen aller Datensätze  eines Mandanten****************/
    public int deleteAll() {
        int erg = 0;

        try {
            /*FIXME bei dbPrasenz fehlt noch Schema!*/
            String sql1 = "DELETE FROM tbl_praesenz where mandant=?;";
            PreparedStatement pstm1 = verbindung.prepareStatement(sql1);
            pstm1.setInt(1, VMcdbBundle.clGlobalVar.mandant);

            erg = 0; /*Falls Duplicate Key, dann wird exception geschmissen und erg wohl nicht gefüllt!*/
            erg = pstm1.executeUpdate();

            pstm1.close();

        } catch (Exception e2) {
            CaBug.drucke("DbPraesenz.deleteAll 001");
            System.err.println(" " + e2.getMessage());
            return (erg);
        }

        return 1;
    }

    /** dekodiert die aktuelle Position aus ergebnis in EclPersonenNatJur und gibt dieses zurück*/
    private int anzFelder = 7;

    private EclPraesenz decodeErgebnis(ResultSet ergebnis) {
        EclPraesenz lPraesenz = new EclPraesenz();

        try {

            lPraesenz.mandant = ergebnis.getInt("mandant");
            lPraesenz.meldungsIdent = ergebnis.getInt("meldungsIdent");
            lPraesenz.db_version = ergebnis.getLong("db_version");

            lPraesenz.kartenart = ergebnis.getInt("kartenart");
            lPraesenz.kartennr = ergebnis.getInt("kartennr");

            lPraesenz.identPersonNatJur = ergebnis.getInt("identPersonNatJur");

            lPraesenz.istPraesent = ergebnis.getInt("istPraesent");

        } catch (Exception e) {
            CaBug.drucke("DbPraesenz.decodeErgebnis 001");
            System.err.println(" " + e.getMessage());
        }

        return lPraesenz;
    }

    /** Fuellen Prepared Statement mit allen Feldern, beginnend bei offset.
     * Kann sowohl für Insert, als auch für update verwendet werden.
     * 
     * offset= Startposition des ersten Feldes (also z.B. 1)
     */
    private void fuellePreparedStatementKomplett(PreparedStatement pstm, int offset, EclPraesenz pPraesenz) {

        pPraesenz.mandant = VMcdbBundle.clGlobalVar.mandant;
        try {
            pstm.setInt(offset, pPraesenz.mandant);
            offset++;
            pstm.setInt(offset, pPraesenz.meldungsIdent);
            offset++;
            pstm.setLong(offset, pPraesenz.db_version);
            offset++;

            pstm.setInt(offset, pPraesenz.kartenart);
            offset++;
            pstm.setInt(offset, pPraesenz.kartennr);
            offset++;
            pstm.setInt(offset, pPraesenz.identPersonNatJur);
            offset++;

            pstm.setInt(offset, pPraesenz.istPraesent);
            offset++;

        } catch (SQLException e) {
            CaBug.drucke("DbPraesenz.fuellePreparedStatementKomplett 001");
            e.printStackTrace();
        }

    }

    /**Achtung - keine Vergabe eigener Ident. D.h. Insert darf nur bei Erstpräsenz-Zugang erfolgen!*/
    public int insert(EclPraesenz pPraesenz) {

        int erg = 0;

        /* Start Transaktion */
        VMcdbBasis.beginTransaction();

        try {

            /*Felder Neuanlage füllen*/
            String sql1 = "INSERT INTO tbl_praesenz " + "(" + "mandant, meldungsIdent, db_version, "
                    + "kartenart, kartennr, identPersonNatJur, istPraesent " + ")" + "VALUES (" + "?, ?, ?, "
                    + "?, ?, ?, ?" + ")";
            PreparedStatement pstm1 = verbindung.prepareStatement(sql1, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);

            fuellePreparedStatementKomplett(pstm1, 1, pPraesenz);

            erg = 0; /*Falls Duplicate Key, dann wird exception geschmissen und erg wohl nicht gefüllt!*/
            erg = pstm1.executeUpdate();
            pstm1.close();
        } catch (Exception e2) {
            CaBug.drucke("DbPraesenz.insert 001");
            System.err.println(" " + e2.getMessage());
        }

        if (erg == 0) {/*Fehler beim Einfügen - d.h. ZutrittsIdent bereits vorhanden*/
            VMcdbBasis.endTransaction();
            return (-1);
        }

        /* Ende Transaktion */
        VMcdbBasis.endTransaction();
        return (1);
    }

    /**Einlesen in array des Präsenzkennzeichens mit MeldungsIdent*/
    public int read(int pMeldungsIdent) {
        int anzInArray = 0;
        try {

            String sql = "SELECT * from tbl_praesenz where " + "mandant=? AND " + "meldungsIdent=?;";

            PreparedStatement pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            pstm1.setInt(1, VMcdbBundle.clGlobalVar.mandant);
            pstm1.setInt(2, pMeldungsIdent);

            ResultSet ergebnis = pstm1.executeQuery();
            ergebnis.last();
            anzInArray = ergebnis.getRow();
            ergebnis.beforeFirst();

            praesenzArray = new EclPraesenz[anzInArray];
            //	System.out.println("anzinarray"+anzInArray);
            int i = 0;
            while (ergebnis.next() == true) {
                praesenzArray[i] = this.decodeErgebnis(ergebnis);
                i++;

            }
            ergebnis.close();
            pstm1.close();

        } catch (Exception e) {
            CaBug.drucke("DbPraesenz.read 001");
            System.err.println(" " + e.getMessage());
        }

        return (anzInArray);
    }

    /**Update einer Präsenz. Versionsnummer wird um 1 hochgezählt
     */
    public int update(EclPraesenz pPraesenz) {

        pPraesenz.db_version++;
        try {

            String sql = "UPDATE tbl_praesenz SET " + "mandant=?, meldungsIdent=?, db_version=?, "
                    + "kartenart=?, kartennr=?, identPersonNatJur=?, istPraesent=? " + " WHERE "
                    + "meldungsIdent=? AND " + "db_version=? AND " + "mandant=?";

            PreparedStatement pstm1 = verbindung.prepareStatement(sql);
            fuellePreparedStatementKomplett(pstm1, 1, pPraesenz);
            pstm1.setInt(this.anzFelder + 1, pPraesenz.meldungsIdent);
            pstm1.setLong(this.anzFelder + 2, pPraesenz.db_version - 1);
            pstm1.setLong(this.anzFelder + 3, VMcdbBundle.clGlobalVar.mandant);

            int ergebnis1 = pstm1.executeUpdate();
            pstm1.close();
            if (ergebnis1 == 0) {
                return (CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert);
            }
        } catch (Exception e1) {
            CaBug.drucke("DbPraesenz.update 001");
            System.err.println(" " + e1.getMessage());
            return (CaFehler.pfdXyBereitsVorhanden);

        }

        return (1);
    }

}
