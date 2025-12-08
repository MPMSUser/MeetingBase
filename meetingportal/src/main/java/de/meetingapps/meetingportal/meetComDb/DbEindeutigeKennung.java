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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComEntities.EclEindeutigeKennung;

/**Hinweis: rueckfragen wird wie fragen behandelt*/

public class  DbEindeutigeKennung extends DbRoot<EclEindeutigeKennung> {

//    private int logDrucken=10;
    
    public DbEindeutigeKennung(DbBundle pDbBundle) {
        super(pDbBundle);
    }

    
    @Override
    String getCreateString() {

//      @formatter:off
       String createString = "CREATE TABLE " + getSchema() + getTableName()+" ( "
                + "`ident` int(11) DEFAULT NULL, "
                + "`eindeutigeKennung` char(20) NOT NULL DEFAULT '', " 
                + "`lfd` int(11) DEFAULT NULL, "
                + "PRIMARY KEY (`eindeutigeKennung`), " 
                + "KEY `x_ident` (`ident`) "
                + ") ";
//      @formatter:on
        return createString;
    }

    @Override
    String getSchema() {
        return dbBundle.getSchemaMandant();
    }


    @Override
    void resetInterneIdent(int pHoechsteIdent) {
    }


    @Override
    String getTableName() {
         return "tbl_eindeutigekennung";
    }


    @Override
    String getFeldFuerInterneIdent() {
        return "";
    }

    @Override
    int getAnzFelder() {
        return 3;
    }


    @Override
    EclEindeutigeKennung decodeErgebnis(ResultSet pErgebnis) {
        EclEindeutigeKennung lEclReturn = new EclEindeutigeKennung();

        try {
            lEclReturn.ident = pErgebnis.getInt("ident");
            lEclReturn.eindeutigeKennung = pErgebnis.getString("eindeutigeKennung");
            lEclReturn.lfd = pErgebnis.getInt("lfd");
                     
       } catch (Exception e) {
            CaBug.drucke("001");
            System.err.println(" " + e.getMessage());
        }

        return lEclReturn;
    }



    @Override
    void fuellePreparedStatementKomplett(PreparedStatement pPStm, int pOffset, EclEindeutigeKennung pEcl) {
        int startOffset = pOffset; /*Nur erforderlich zum Überprüfen von Programmierfehlern - Feldanzahl*/

        try {
//          @formatter:off
            
            pPStm.setInt(pOffset, pEcl.ident);pOffset++;
            pPStm.setString(pOffset, pEcl.eindeutigeKennung);pOffset++;
            pPStm.setInt(pOffset, pEcl.lfd);pOffset++;

//          @formatter:on

            if (pOffset - startOffset != getAnzFelder()) {
                /*Nur wg. Überprüfung auf Programmierfehler - alle Felder berücksichtigt?*/
                CaBug.drucke("002");
            }

        } catch (SQLException e) {
            CaBug.drucke("001");
            e.printStackTrace();
        }
    }

    private String[] felder=null; 
    private boolean initErfolgt=false;
    private void initFelder(){
        if (initErfolgt) {return;}
        felder=new String[getAnzFelder()];
        felder[0]="ident";
        felder[1]="eindeutigeKennung";
        felder[2]="lfd";
        initErfolgt=true;
    }

    /**Funktion ist nicht multiuserfähig!*/
    @Override
    public int insert(EclEindeutigeKennung pEcl) {
        
        int erg = 0;
        int anzbisher = 0;

        this.readIdent(-1);
        if (this.anzErgebnis() == 0) {
            try {
                String lSql = "INSERT INTO " + getSchema() + getTableName()
                        + " (ident, eindeutigeKennung, lfd)" + "VALUES (" + "-1, '-1', 0 )";
                PreparedStatement lPStm;
                lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                        ResultSet.CONCUR_READ_ONLY);
                erg = executeUpdate(lPStm);

                lSql = "INSERT INTO " +getSchema() + getTableName()
                        + " (ident, eindeutigeKennung, lfd)" + "VALUES (" + "0, '0', 0 )";
                lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                        ResultSet.CONCUR_READ_ONLY);
                erg = executeUpdate(lPStm);

                lPStm.close();

            } catch (SQLException e) {
                e.printStackTrace();
            }
            anzbisher = 0;
        } else {
            anzbisher = ergebnisArray.get(0).lfd;
        }

        anzbisher++;
        pEcl.ident = anzbisher;
        try {

            /*Felder Neuanlage füllen*/
            String lSql = "INSERT INTO " + getSchema() + getTableName() + " ("
                    + "ident, eindeutigeKennung, lfd" + ")" + "VALUES (" + "?, ?, ? " + ")";
            PreparedStatement lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);

            fuellePreparedStatementKomplett(lPStm, 1, pEcl);

            erg = executeUpdate(lPStm);
            lPStm.close();

        } catch (Exception e2) {
            CaBug.drucke("001");
            System.err.println(" " + e2.getMessage());
        }

        if (erg == 0) {/*Fehler beim Einfügen - d.h. primaryKey bereits vorhanden*/
            return (-1);
        }

        /* Ende Transaktion */
        EclEindeutigeKennung lEindeutigerKennung = new EclEindeutigeKennung();
        lEindeutigerKennung.ident = -1;
        lEindeutigerKennung.lfd = anzbisher;
        lEindeutigerKennung.eindeutigeKennung = "-1";
        this.update(lEindeutigerKennung);
        return (1);
       
    }

    
    /**Prüft, ob Insert möglich wäre.*/
    public boolean insertCheck(String pEindeutigeKennung) {
        int anzInArray = 0;
        PreparedStatement lPStm = null;

        if (pEindeutigeKennung == null) {
            CaBug.drucke("001");
            return false;
        }

        try {
            String lSql = "SELECT * from " + getSchema() + getTableName() + " where "
                    + "eindeutigeKennung=? ";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setString(1, pEindeutigeKennung);

            ResultSet lErgebnis = executeQuery(lPStm);
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();
            lErgebnis.close();
            lPStm.close();

            if (anzInArray > 0) {
                return false;
            } else {
                return true;
            }

        } catch (Exception e) {
            CaBug.drucke("003");
            System.err.println(" " + e.getMessage());
            return (false);
        }

    }

    public String getNextFree() {
        this.readIdent(0);
        EclEindeutigeKennung lEindeutigeKennung = this.ergebnisPosition(0);
        int neueID = lEindeutigeKennung.lfd + 1;
        lEindeutigeKennung.lfd = neueID;
        this.update(lEindeutigeKennung);

        this.readIdent(neueID);
        return (this.ergebnisPosition(0).eindeutigeKennung.toUpperCase());
    }
 
    
    /**Liest Satz mit Ident ein*/
    public int readIdent(int pIdent) {
       PreparedStatement lPStm = null;
       int anzInArray = 0;
       try {
           String lSql = "SELECT * from " + getSchema() + getTableName()+" WHERE ident=?";
           lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
           lPStm.setInt(1, pIdent);
           anzInArray = readIntern(lPStm);

       } catch (Exception e) {
           CaBug.drucke("003");
           System.err.println(" " + e.getMessage());
           return (-1);
       }
       return (anzInArray);
   }

    
    /**Update. 
     * 
     * Returnwert:
     * pfXyWurdeVonAnderemBenutzerVeraendert
     * -1 => unbekannter Fehler
     * 1 = Update wurde durchgeführt.
     * 
     */
    public int update(EclEindeutigeKennung pEcl) {
        int ergebnis=0;
        initFelder();

        try {

            String lSql = setzeUpdateBasisStringZusammen(felder)
                    + " WHERE "
                    + "ident=? ";

            PreparedStatement lPStm = verbindung.prepareStatement(lSql);
            fuellePreparedStatementKomplett(lPStm, 1, pEcl);
            lPStm.setInt(getAnzFelder() + 1, pEcl.ident);

            ergebnis = updateIntern(lPStm);
        } catch (Exception e1) {
            CaBug.drucke("001");
            System.err.println(" " + e1.getMessage());
            return (-1);
        }

        return ergebnis;
    }

    
    public int setzeVerwendetZurueck() {
        int ergebnis=0;
        initFelder();

        EclEindeutigeKennung pEcl=new EclEindeutigeKennung();
        pEcl.ident=0;
        pEcl.eindeutigeKennung="";
        pEcl.lfd=0;
        
        try {

            String lSql = setzeUpdateBasisStringZusammen(felder)
                    + " WHERE "
                    + "ident=? ";

            PreparedStatement lPStm = verbindung.prepareStatement(lSql);
            fuellePreparedStatementKomplett(lPStm, 1, pEcl);
            lPStm.setInt(getAnzFelder() + 1, pEcl.ident);

            ergebnis = updateIntern(lPStm);
        } catch (Exception e1) {
            CaBug.drucke("001");
            System.err.println(" " + e1.getMessage());
            return (-1);
        }

        return ergebnis;
    }

    
}
