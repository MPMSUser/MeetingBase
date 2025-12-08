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
import java.util.LinkedList;
import java.util.List;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaFehler;

abstract public class DbRoot<T>  extends DbRootExecute {

    private int logDrucken=3;
    
    Connection verbindung = null;
    DbBasis dbBasis = null;
    DbBundle dbBundle = null;

    List<T> ergebnisArray = null;

    /*************************Initialisierung***************************/
    public DbRoot(DbBundle pDbBundle) {
        /* Verbindung in lokale Daten eintragen*/
        if (pDbBundle == null) {
            CaBug.drucke("001 - dbBundle nicht initialisiert");
            return;
        }
        if (pDbBundle.dbBasis == null) {
            CaBug.drucke("002 - dbBasis nicht initialisiert");
            return;
        }

        dbBasis = pDbBundle.dbBasis;
        verbindung = pDbBundle.dbBasis.verbindung;
        dbBundle = pDbBundle;
    }

    /**************Anzahl der Ergebnisse der read*-Methoden**************************/
    public int anzErgebnis() {
        if (ergebnisArray == null) {
            return 0;
        }
        return ergebnisArray.size();
    }

    /**************Ergebnisse der read*-Methoden als Array**************************/
    public List<T> ergebnis() {
        return ergebnisArray;
    }

    
     /**********Liefert pN-tes Element des Ergebnisses der read*Methoden**************
     * pN geht von 0 bis anzErgebnis-1*/
    public T ergebnisPosition(int pN) {
        if (ergebnisArray == null) {
            CaBug.drucke("001");
            return null;
        }
        if (pN < 0) {
            CaBug.drucke("002");
            return null;
        }
        if (pN >= ergebnisArray.size()) {
            CaBug.drucke("003");
            return null;
        }
        return ergebnisArray.get(pN);
    }

    public int createTable() {
        int rc = 0;
        DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);
        String createString=getCreateString();
        CaBug.druckeLog("createString="+createString, logDrucken, 10);
        rc = lDbLowLevel.createTable(createString);
        return rc;
    }
    
    public int dropTable() {
        int rc = 0;
        DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);
        String dropString="DROP TABLE " + getSchema() + getTableName()+";";
        CaBug.druckeLog("dropString="+dropString, logDrucken, 10);
        rc = lDbLowLevel.dropTable(dropString);
        return rc;
    }
    /**************************deleteAll Löschen aller Datensätze****************/
    public int deleteAll() {
        resetInterneIdent(0);
        return dbBundle.dbLowLevel.deleteAlle(
                "DELETE FROM " + getSchema() + getTableName()+";");
    }

    /**Reorganisiert die interne Ident.*/
    public void reorgInterneIdent() {
        String feldFuerInterneIdent=getFeldFuerInterneIdent();
        if (feldFuerInterneIdent!=null) {
            int lMax = dbBundle.dbLowLevel.liefereHoechsteIdentOhneMandant("SELECT MAX("+feldFuerInterneIdent+") FROM "
                    + getSchema() + getTableName()+";");
            if (lMax != -1) {
                resetInterneIdent(lMax);
            }
        }
    }

    
    int insertIntern(String[] felder, T pEcl) {
        int erg=0;
        String lSql="";
        try {
            lSql="INSERT INTO "+getSchema()+getTableName()+"(";
            String lFragezeichen="";
            for (int i=0;i<felder.length;i++) {
                if (i!=0) {lSql+=", ";lFragezeichen+=", ";}
                lSql+=felder[i];
                lFragezeichen+="?";
            }
            lSql+=") VALUES ("+lFragezeichen+");";
            PreparedStatement lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);

            fuellePreparedStatementKomplett(lPStm, 1, pEcl);

            erg = executeUpdate(lPStm);
            lPStm.close();
        } catch (Exception e2) {
            CaBug.drucke("001");
            CaBug.out("lSql="+lSql);
            CaBug.out(" " + e2.getMessage());
        }
        if (erg==0) {/*Fehler beim Einfügen - d.h. primaryKey bereits vorhanden*/
            return (-1);
        }

        return (1);
    }
    
    
    int readIntern(PreparedStatement pPStm) {
         try {
            ResultSet lErgebnis = executeQuery(pPStm);
            ergebnisArray = new LinkedList<T>();
            while (lErgebnis.next() == true) {
                ergebnisArray.add(this.decodeErgebnis(lErgebnis));
            }
            lErgebnis.close();
            pPStm.close();

        } catch (Exception e) {
            CaBug.drucke("003");
            CaBug.out(" " + e.getMessage());
            return (-1);
        }
        return (ergebnisArray.size());

    }

    /**Liefert Ergebnis, aus SUM, COUNT o.ä.*/
    long readInternErgebnis(PreparedStatement pPStm) {
        long ergebnis = 0;
        try {
             ResultSet lErgebnis = executeQuery(pPStm);

            lErgebnis.last();
            lErgebnis.beforeFirst();

            while (lErgebnis.next() == true) {
                ergebnis = lErgebnis.getLong(1);
            }

            lErgebnis.close();
            pPStm.close();

        } catch (Exception e) {
            CaBug.drucke("003");
            CaBug.out(" " + e.getMessage());
            return (-1);
        }
        return ergebnis;

    }

    String setzeUpdateBasisStringZusammen(String[] felder) {
        String lSql="UPDATE "+getSchema()+getTableName()+" SET ";
       for (int i=0;i<felder.length;i++) {
            if (i!=0) {lSql+=", ";}
            lSql+=felder[i]+"=?";
        }
       return lSql;
    }
    
    int updateIntern(PreparedStatement lPStm) {
        int ergebnis=0;
        try {
            ergebnis = executeUpdate(lPStm);
            lPStm.close();
        } catch (SQLException e) {
            CaBug.drucke("002");
            e.printStackTrace();
            return (-1);
        }
        if (ergebnis == 0) {
            return (CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert);
        }
        return (1);
    }
    
    int deleteIntern(PreparedStatement lPStm) {
        int ergebnis=0;
        try {
            ergebnis = executeUpdate(lPStm);
            lPStm.close();
        } catch (SQLException e) {
            CaBug.drucke("002");
            e.printStackTrace();
            return (-1);
        }
        if (ergebnis == 0) {
            return (CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert);
        }
        return (1);
       
    }
    
    
    /**************Abstrakte-Methoden "Infrastruktur" für Methoden von DbRoot****************************************/
    /**Liefert den String, mit dem die Table erzeugt wird*/ 
    abstract String getCreateString();
    
    /**Liefert entweder dbBundle.getSchemaMandant() oder dbBundle.getSchemaAllgemein()*/
    abstract String getSchema();
    
    /**Resettet die interne Ident, die zu dieser Table gehört. Wenn keine interneIdent vergeben wird, dann
     * diese Funktion einfach als Leer-Funktion implementieren*/
    abstract void resetInterneIdent(int pHoechsteIdent);
    
    /**Liefert den Table-Name in der Datenbank*/
    abstract String getTableName();
    
    /**Liefert das Feld, über das die interne Ident gezählt wird. Wenn keine interneIdent vorhanden,
     * dann muß diese Funktion null liefern.
     */
    abstract String getFeldFuerInterneIdent();

    /**Liefert die Anzahl der Felder, die in einem Datenbanksatz enthalten sind*/
    abstract int getAnzFelder();
    
    /****************Abstrakte-Methoden komplett zu implementieren******************************/
    
    /**dekodiert die aktuelle Position aus ergebnis  und gibt dieses zurück*/
    abstract T decodeErgebnis(ResultSet pErgebnis);
    
    /**Fuellen Prepared Statement mit allen Feldern, beginnend bei pOffset.
     * Kann sowohl für Insert, als auch für update verwendet werden.
     * 
     * pOffset= Startposition des ersten Feldes (also z.B. 1)
     */
    abstract void fuellePreparedStatementKomplett(PreparedStatement pPStm, int pOffset, T pEcl);
    
    /**Insert
     * 
     * Returnwert:
     * =1 => Insert erfolgreich
     * ansonsten: Fehler
     */
   abstract public int insert(T pEcl);
}
