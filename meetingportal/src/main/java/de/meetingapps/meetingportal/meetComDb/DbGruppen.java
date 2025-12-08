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
import de.meetingapps.meetingportal.meetComEntities.EclGruppe;
import de.meetingapps.meetingportal.meetComEntities.EclGruppeMandant;

public class DbGruppen {

    private Connection verbindung = null;
    private DbBasis dbBasis = null;
    private DbBundle dbBundle = null;

    /*TODO _Gruppen: gruppenklassen Verwendung derzeit unklar ....Außerdem Ident-Vergabe noch zu konsolidieren*/

    /**Soll nicht direkt verwendet werden, sondern nur über die Zugriffsfunktionen!
     * Aktuell noch public, da direkter Zugriff über DbMeldungen
     */
    public EclGruppe gruppeArray[] = null;
    public EclGruppeMandant gruppeMandantArray[] = null;

    /*************************Initialisierung***************************/
    /* Verbindung in lokale Daten eintragen*/
    public DbGruppen(DbBundle datenbankbundle) {
        if (datenbankbundle.dbBasis == null) {
            System.err.println("vmcdbBasis nicht initialisiert");
            return;
        }
        dbBasis = datenbankbundle.dbBasis;
        verbindung = datenbankbundle.dbBasis.verbindung;
        dbBundle = datenbankbundle;
    }

    public int anzGruppeGefunden() {
        if (gruppeArray == null) {
            return 0;
        }
        return gruppeArray.length;
    }

    public EclGruppe gruppeGefunden(int lfd) {
        return gruppeArray[lfd];
    }

    public int createTable_allgemein() {
        int rc = 0;
        DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);
        rc = lDbLowLevel.createTable("CREATE TABLE " + dbBundle.getSchemaAllgemein() + "tbl_gruppen ( "
                + "`gruppenIdent` int(11) DEFAULT NULL, " + "`gruppenNr` int(11) DEFAULT NULL, "
                + "`db_version` bigint(20) DEFAULT '0', " + "`gruppenklassenNr` int(11) DEFAULT NULL, "
                + "`gruppenText` char(50) DEFAULT NULL, " + "`vertreterZwingend` int(11) DEFAULT NULL, "
                + "`fuerAktionaere` int(11) DEFAULT NULL, " + "`fuerGaeste` int(11) DEFAULT NULL " + ") ");
        return rc;
    }

    public int createTable_mandant() {
        int rc = 0;
        DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);
        rc = lDbLowLevel.createTable("CREATE TABLE " + dbBundle.getSchemaMandant() + "tbl_gruppenmandant ( "
                + "`gruppenIdent` int(11) NOT NULL, " + "`gruppenNr` int(11) DEFAULT NULL, "
                + "`mandant` int(11) NOT NULL, " + "`db_version` bigint(20) DEFAULT '0', "
                + "`vertreterZwingend` int(11) DEFAULT NULL, " + "`fuerAktionaere` int(11) DEFAULT NULL, "
                + "`fuerGaeste` int(11) DEFAULT NULL, " + "`gastEKFormular` int(11) DEFAULT NULL, "
                + "PRIMARY KEY (`gruppenIdent`,`mandant`) " + ") "

        );
        return rc;
    }

    
    public int updateMandant() {
        return dbBundle.dbLowLevel.rawUpdateMandant(dbBundle.getSchemaMandant() + "tbl_gruppenmandant");
    }

    /**************************deleteAll Löschen aller Datensätze  eines Mandanten****************/
    public int deleteAll_mandant() {
        int erg = 0;

        dbBasis.resetInterneIdentGruppen();

        try {

            String sql1 = "DELETE FROM " + dbBundle.getSchemaMandant() + "tbl_gruppenmandant where mandant=?;";
            PreparedStatement pstm1 = verbindung.prepareStatement(sql1);
            pstm1.setInt(1, dbBundle.clGlobalVar.mandant);

            erg = 0; /*Falls Duplicate Key, dann wird exception geschmissen und erg wohl nicht gefüllt!*/
            erg = pstm1.executeUpdate();

            pstm1.close();

        } catch (Exception e2) {
            CaBug.drucke("DbGruppen.deleteAll 001");
            System.err.println(" " + e2.getMessage());
            return (erg);
        }

        return 1;
    }

    public void reorgInterneIdent_mandant() {
        int lMax;
        lMax = 0;

        PreparedStatement pstm1 = null;
        try {
            int anzInArray;
            String sql = "SELECT MAX(gruppenIdent) FROM " + dbBundle.getSchemaMandant() + "tbl_gruppenmandant ab where "
                    + "ab.mandant=? ";
            pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pstm1.setInt(1, dbBundle.clGlobalVar.mandant);

            ResultSet ergebnis = pstm1.executeQuery();
            ergebnis.last();
            anzInArray = ergebnis.getRow();
            ergebnis.beforeFirst();

            if (anzInArray > 0) {
                ergebnis.next();
                lMax = ergebnis.getInt(1);
            }
            ergebnis.close();
            pstm1.close();

        } catch (Exception e) {
            CaBug.drucke("DbGruppen.reorgInterneIdent 001");
            System.err.println(" " + e.getMessage());
        }

        dbBasis.resetInterneIdentGruppen(lMax);

    }

    /** dekodiert die aktuelle Position aus ergebnis in EclGruppe und gibt dieses zurück*/
    private EclGruppe decodeErgebnis(ResultSet ergebnis) {
        EclGruppe lGruppe = new EclGruppe();

        try {

            lGruppe.gruppenNr = ergebnis.getInt("gruppenIdent");
            lGruppe.gruppenNr = ergebnis.getInt("gruppenNr");
            lGruppe.db_version = ergebnis.getLong("db_version");

            lGruppe.gruppenklassenNr = ergebnis.getInt("gruppenklassenNr");
            lGruppe.gruppenText = ergebnis.getString("gruppenText");
            lGruppe.vertreterZwingend = ergebnis.getInt("vertreterZwingend");
            lGruppe.fuerAktionaere = ergebnis.getInt("fuerAktionaere");
            lGruppe.fuerGaeste = ergebnis.getInt("fuerGaeste");

        } catch (Exception e) {
            CaBug.drucke("DbGruppen.decodeErgebnis 001");
            System.err.println(" " + e.getMessage());
        }

        return lGruppe;
    }

    /** dekodiert die aktuelle Position aus ergebnis in EclGruppeMandant und gibt dieses zurück*/
    private EclGruppeMandant decodeErgebnisGruppeMandant(ResultSet ergebnis) {
        EclGruppeMandant lGruppeMandant = new EclGruppeMandant();

        try {

            lGruppeMandant.gruppenNr = ergebnis.getInt("gruppenIdent");
            lGruppeMandant.gruppenNr = ergebnis.getInt("gruppenNr");
            lGruppeMandant.mandant = ergebnis.getInt("mandant");
            lGruppeMandant.db_version = ergebnis.getLong("db_version");

            lGruppeMandant.vertreterZwingend = ergebnis.getInt("vertreterZwingend");
            lGruppeMandant.fuerAktionaere = ergebnis.getInt("fuerAktionaere");
            lGruppeMandant.fuerGaeste = ergebnis.getInt("fuerGaeste");
            lGruppeMandant.gastEKFormular = ergebnis.getInt("gastEKFormular");

        } catch (Exception e) {
            CaBug.drucke("DbGruppen.decodeErgebnisGruppeMandant 001");
            System.err.println(" " + e.getMessage());
        }

        return lGruppeMandant;
    }

    /** Fuellen Prepared Statement mit allen Feldern, beginnend bei offset.
     * Kann sowohl für Insert, als auch für update verwendet werden.
     * 
     * offset= Startposition des ersten Feldes (also z.B. 1)
     */
    private int anzfelder = 8;

    private void fuellePreparedStatementKomplett(PreparedStatement pstm, int offset, EclGruppe lGruppe) {

        try {
            pstm.setInt(offset, lGruppe.gruppenIdent);
            offset++;
            pstm.setInt(offset, lGruppe.gruppenNr);
            offset++;
            pstm.setLong(offset, lGruppe.db_version);
            offset++;
            pstm.setInt(offset, lGruppe.gruppenklassenNr);
            offset++;
            pstm.setString(offset, lGruppe.gruppenText);
            offset++;
            pstm.setInt(offset, lGruppe.vertreterZwingend);
            offset++;
            pstm.setInt(offset, lGruppe.fuerAktionaere);
            offset++;
            pstm.setInt(offset, lGruppe.fuerGaeste);
            offset++;
        } catch (SQLException e) {
            CaBug.drucke("DbGruppen.fuellePreparedStatementKomplett 001");
            e.printStackTrace();
        }

    }

    /**Wichtig: ident wird nicht "neu vergeben", sondern aus lGruppe geholt - sie muß identisch sein
     * mit der von EclGruppe (ohne Zusatz :-) ). Aufrufer ist dafür verantwortlich!
     */
    public int insert(EclGruppe lGruppe) {

        int erg = 0;

        /* Start Transaktion */
        dbBasis.beginTransaction();

        /* neue InterneIdent vergeben */
        erg = dbBasis.getInterneIdentGruppen();
        if (erg < 1) {
            CaBug.drucke("DbGruppen.insert 002");
            dbBasis.endTransaction();
            return (erg);
        }
        lGruppe.gruppenIdent = erg;

        /* VclMeldung einfügen */
        /* Verarbeitungshinweise: 
         * 	>	nachdem InterneIdent immer eindeutig vergeben werden, ist prinzipiell eine "Doppeleinfügung"
         * 		von InterneIdent nicht möglich
         */

        try {

            /*Felder Neuanlage füllen*/
            String sql1 = "INSERT INTO " + dbBundle.getSchemaAllgemein() + "tbl_gruppen " + "("
                    + " gruppenIdent, gruppenNr, db_version, " + "gruppenklassenNr, gruppenText, "
                    + "vertreterZwingend, fuerAktionaere, fuerGaeste" + ")" +

                    "VALUES (" + "?, ?, ?, " + "?, ?, " + "?, ?, ?" + ")";
            PreparedStatement pstm1 = verbindung.prepareStatement(sql1, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);

            fuellePreparedStatementKomplett(pstm1, 1, lGruppe);

            erg = 0; /*Falls Duplicate Key, dann wird exception geschmissen und erg wohl nicht gefüllt!*/
            //				System.out.println("vor Update");
            erg = pstm1.executeUpdate();
            //				System.out.println("nach Update");
            pstm1.close();
        } catch (Exception e2) {
            CaBug.drucke("DbGruppen.insert 001");
            System.err.println(" " + e2.getMessage());
        }

        if (erg == 0) {/*Fehler beim Einfügen - d.h. ZutrittsIdent bereits vorhanden*/
            dbBasis.endTransaction();
            return (-1);
        }

        /* Ende Transaktion */
        dbBasis.endTransaction();

        return (1);
    }

    /**Selektion: 0=alle, 1=nur die Gruppen für Aktionäre, 2=nur die Gruppen für Gäste.
     * Liest nur die für den Mandant passenden ein*/
    public int leseInArray(int selektion) {
        int anzInArray = 0;
        PreparedStatement pstm1 = null;
        try {

            if (selektion == 0) {
                String sql = "SELECT * from " + dbBundle.getSchemaMandant() + "tbl_gruppenmandant "
                        + " AND mandant=? ORDER BY gruppenNr;";
                pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            }
            if (selektion == 1) {
                String sql = "SELECT * from " + dbBundle.getSchemaMandant() + "tbl_gruppenmandant where "
                        + "fuerAktionaere=1 AND mandant=? ORDER BY gruppenNr;";
                pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            }
            if (selektion == 2) {
                String sql = "SELECT * from " + dbBundle.getSchemaMandant() + "tbl_gruppenmandant where "
                        + "fuerGaeste=1 AND mandant=? ORDER BY gruppenNr;";
                pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            }

            pstm1.setInt(1, dbBundle.clGlobalVar.mandant);

            ResultSet ergebnis = pstm1.executeQuery();
            ergebnis.last();
            anzInArray = ergebnis.getRow();
            ergebnis.beforeFirst();

            gruppeMandantArray = new EclGruppeMandant[anzInArray];

            int i = 0;
            while (ergebnis.next() == true) {
                gruppeMandantArray[i] = this.decodeErgebnisGruppeMandant(ergebnis);
                i++;
            }

            EclGruppe[] hGruppeArray = new EclGruppe[anzInArray];
            for (int i1 = 0; i1 < anzInArray; i1++) {
                leseZuGruppenNr(gruppeMandantArray[i1].gruppenNr);
                hGruppeArray[i1] = this.gruppeArray[0];
                if (gruppeMandantArray[i1].vertreterZwingend != -1) {
                    hGruppeArray[i1].vertreterZwingend = gruppeMandantArray[i1].vertreterZwingend;
                }
                hGruppeArray[i1].fuerAktionaere = gruppeMandantArray[i1].fuerAktionaere;
                hGruppeArray[i1].fuerGaeste = gruppeMandantArray[i1].fuerGaeste;
                hGruppeArray[i1].gastEKFormular = gruppeMandantArray[i1].gastEKFormular;
            }
            gruppeArray = hGruppeArray;
            ergebnis.close();
            pstm1.close();

        } catch (Exception e) {
            CaBug.drucke("DbGruppen.leseZuMeldung 001");
            System.err.println(" " + e.getMessage());
        }

        return (anzInArray);
    }

    /**Selektion: 0=alle, 1=nur die Gruppen für Aktionäre, 2=nur die Gruppen für Gäste.
     * Liest die Mandanten-Übergreifenden ein*/
    public int leseInArrayUebergreifend(int selektion) {
        int anzInArray = 0;
        PreparedStatement pstm1 = null;
        try {

            if (selektion == 0) {
                String sql = "SELECT * from " + dbBundle.getSchemaAllgemein() + "tbl_gruppen " + " ORDER BY gruppenNr;";
                pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            }
            if (selektion == 1) {
                String sql = "SELECT * from " + dbBundle.getSchemaAllgemein() + "tbl_gruppen where "
                        + "fuerAktionaere=1 ORDER BY gruppenNr;";
                pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            }
            if (selektion == 2) {
                String sql = "SELECT * from " + dbBundle.getSchemaAllgemein() + "tbl_gruppen where "
                        + "fuerGaeste=1 ORDER BY gruppenNr;";
                pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            }

            ResultSet ergebnis = pstm1.executeQuery();
            ergebnis.last();
            anzInArray = ergebnis.getRow();
            ergebnis.beforeFirst();

            gruppeArray = new EclGruppe[anzInArray];

            int i = 0;
            while (ergebnis.next() == true) {

                gruppeArray[i] = this.decodeErgebnis(ergebnis);
                i++;

            }
            ergebnis.close();
            pstm1.close();

        } catch (Exception e) {
            CaBug.drucke("DbGruppen.leseZuMeldung 001");
            System.err.println(" " + e.getMessage());
        }

        return (anzInArray);
    }

    public int leseZuGruppenNr(int pIdent) {
        int anzInArray = 0;
        try {

            String sql = "SELECT * from " + dbBundle.getSchemaAllgemein() + "tbl_gruppen where " + "gruppenNr=?;";

            PreparedStatement pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            pstm1.setInt(1, pIdent);

            ResultSet ergebnis = pstm1.executeQuery();
            ergebnis.last();
            anzInArray = ergebnis.getRow();
            ergebnis.beforeFirst();

            gruppeArray = new EclGruppe[anzInArray];

            int i = 0;
            while (ergebnis.next() == true) {
                gruppeArray[i] = this.decodeErgebnis(ergebnis);
                i++;

            }
            ergebnis.close();
            pstm1.close();

        } catch (Exception e) {
            CaBug.drucke("DbGruppen.leseZuGruppenNr 001");
            System.err.println(" " + e.getMessage());
        }

        return (anzInArray);
    }

    /**Update einer Willenserklärung. Versionsnummer wird um 1 hochgezählt
     */
    public int update(EclGruppe lGruppe) {
        lGruppe.db_version++;
        try {

            String sql = "UPDATE " + dbBundle.getSchemaAllgemein() + "tbl_gruppen SET "
                    + "gruppenIdent=?, gruppenNr=?, db_version=?, " + "gruppenklassenNr=?, gruppenText=?, "
                    + "vertreterZwingend=?, fuerAktionaere=?, fuerGaeste=? " + "WHERE " + "gruppenIdent=? AND "
                    + "db_version=?";

            PreparedStatement pstm1 = verbindung.prepareStatement(sql);
            fuellePreparedStatementKomplett(pstm1, 1, lGruppe);
            pstm1.setInt(anzfelder + 1, lGruppe.gruppenIdent);
            pstm1.setLong(anzfelder + 2, lGruppe.db_version - 1);

            int ergebnis1 = pstm1.executeUpdate();

            pstm1.close();
            if (ergebnis1 == 0) {
                return (CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert);
            }
        } catch (Exception e1) {
            CaBug.drucke("DbGruppen.update 001");
            System.err.println(" " + e1.getMessage());
            return (CaFehler.pfdXyBereitsVorhanden);

        }
        return (1);
    }

}
