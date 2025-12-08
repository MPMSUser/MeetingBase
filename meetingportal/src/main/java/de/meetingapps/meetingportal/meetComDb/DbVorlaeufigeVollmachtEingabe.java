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
import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComEntities.EclVorlaeufigeVollmachtEingabe;

/**Hinweis: rueckfragen wird wie fragen behandelt*/

public class  DbVorlaeufigeVollmachtEingabe extends DbRoot<EclVorlaeufigeVollmachtEingabe> {

//    private int logDrucken=10;
    
    public DbVorlaeufigeVollmachtEingabe(DbBundle pDbBundle) {
        super(pDbBundle);
    }

    
    @Override
    String getCreateString() {

        //      @formatter:off
        String createString = "CREATE TABLE " + getSchema() + getTableName()+" ( "
                + "`ident` int NOT NULL DEFAULT 0, " 
                
                + "`erteilendeAktionaersnummer` char(20) NOT NULL DEFAULT '', " 
                + "`erteilendeLoginDaten` int NOT NULL DEFAULT 0, " 
                + "`artDerEingabe` int NOT NULL DEFAULT 0, " 
                
                + "`vertreterId` char(20) NOT NULL DEFAULT '', " 

                + "`vertreterTitel` varchar(30) NOT NULL DEFAULT '', " 
                + "`vertreterName` varchar(80) NOT NULL DEFAULT '', " 
                + "`vertreterVorname` varchar(80) NOT NULL DEFAULT '', " 
                + "`vertreterZusatz` varchar(80) NOT NULL DEFAULT '', " 
                + "`vertreterStrasse` varchar(80) NOT NULL DEFAULT '', " 
                + "`vertreterPlz` char(20) NOT NULL DEFAULT '', " 
                + "`vertreterOrt` varchar(80) NOT NULL DEFAULT '', " 

                + "`vertreterMail` varchar(200) NOT NULL DEFAULT '', " 

                + "`vertreterArt` int NOT NULL DEFAULT 0, " 
                + "`vertreterArtBeiSonstige` varchar(100) NOT NULL DEFAULT '', " 

                + "`eingabeDatum` char(19) NOT NULL DEFAULT '', " 

                + "`wurdeStorniert` int NOT NULL DEFAULT 0, " 
                + "`stornierungsDatum` char(19) NOT NULL DEFAULT '', " 

                + "`pruefstatus` int NOT NULL DEFAULT 0, " 
                + "`abgelehntWeil` int NOT NULL DEFAULT 0, " 
                + "`abgelehntWeilText` varchar(300) NOT NULL DEFAULT '', " 

                + "`pruefstatusStorno` int NOT NULL DEFAULT 0, " 

                + "`inBearbeitungDurchUserNr` int NOT NULL DEFAULT 0, " 

                + "PRIMARY KEY (`ident`) " + ") ";
        //      @formatter:on
        return createString;
    }

    @Override
    String getSchema() {
        return dbBundle.getSchemaMandant();
    }


    @Override
    void resetInterneIdent(int pHoechsteIdent) {
        dbBundle.dbBasis.resetInterneIdentVorlaeufigeVollmachtEingabe(pHoechsteIdent);
    }



    @Override
    String getTableName() {
         return "tbl_vorlaeufigeVollmachtEingabe";
    }


    @Override
    String getFeldFuerInterneIdent() {
        return "ident";
    }

    @Override
    int getAnzFelder() {
        return 23;
    }


    @Override
    EclVorlaeufigeVollmachtEingabe decodeErgebnis(ResultSet pErgebnis) {
        EclVorlaeufigeVollmachtEingabe lEclReturn = new EclVorlaeufigeVollmachtEingabe();

        try {
            lEclReturn.ident = pErgebnis.getInt("ident");

            lEclReturn.erteilendeAktionaersnummer = pErgebnis.getString("erteilendeAktionaersnummer");
            lEclReturn.erteilendeLoginDaten = pErgebnis.getInt("erteilendeLoginDaten");
            
            lEclReturn.artDerEingabe = pErgebnis.getInt("artDerEingabe");

            lEclReturn.vertreterId = pErgebnis.getString("vertreterId");
            lEclReturn.vertreterTitel = pErgebnis.getString("vertreterTitel");
            lEclReturn.vertreterName = pErgebnis.getString("vertreterName");
            lEclReturn.vertreterVorname = pErgebnis.getString("vertreterVorname");
            lEclReturn.vertreterZusatz = pErgebnis.getString("vertreterZusatz");
            lEclReturn.vertreterStrasse = pErgebnis.getString("vertreterStrasse");
            lEclReturn.vertreterPlz = pErgebnis.getString("vertreterPlz");
            lEclReturn.vertreterOrt = pErgebnis.getString("vertreterOrt");
            
            lEclReturn.vertreterMail = pErgebnis.getString("vertreterMail");
            
            lEclReturn.vertreterArt = pErgebnis.getInt("vertreterArt");
            lEclReturn.vertreterArtBeiSonstige = pErgebnis.getString("vertreterArtBeiSonstige");
            
            lEclReturn.eingabeDatum = pErgebnis.getString("eingabeDatum");
            lEclReturn.wurdeStorniert = pErgebnis.getInt("wurdeStorniert");
            lEclReturn.stornierungsDatum = pErgebnis.getString("stornierungsDatum");
            
            lEclReturn.pruefstatus = pErgebnis.getInt("pruefstatus");
            lEclReturn.abgelehntWeil = pErgebnis.getInt("abgelehntWeil");
            lEclReturn.abgelehntWeilText = pErgebnis.getString("abgelehntWeilText");
 
            lEclReturn.pruefstatusStorno = pErgebnis.getInt("pruefstatusStorno");
            lEclReturn.inBearbeitungDurchUserNr = pErgebnis.getInt("inBearbeitungDurchUserNr");

       } catch (Exception e) {
            CaBug.drucke("001");
            System.err.println(" " + e.getMessage());
        }

        return lEclReturn;
    }



    @Override
    void fuellePreparedStatementKomplett(PreparedStatement pPStm, int pOffset, EclVorlaeufigeVollmachtEingabe pEcl) {
        int startOffset = pOffset; /*Nur erforderlich zum Überprüfen von Programmierfehlern - Feldanzahl*/

        try {
//          @formatter:off
            
            pPStm.setInt(pOffset, pEcl.ident);pOffset++;

            pPStm.setString(pOffset, pEcl.erteilendeAktionaersnummer);pOffset++;
            pPStm.setInt(pOffset, pEcl.erteilendeLoginDaten);pOffset++;
           
            pPStm.setInt(pOffset, pEcl.artDerEingabe);pOffset++;
            
            pPStm.setString(pOffset, pEcl.vertreterId);pOffset++;
            pPStm.setString(pOffset, pEcl.vertreterTitel);pOffset++;
            pPStm.setString(pOffset, pEcl.vertreterName);pOffset++;
            pPStm.setString(pOffset, pEcl.vertreterVorname);pOffset++;
            pPStm.setString(pOffset, pEcl.vertreterZusatz);pOffset++;
            pPStm.setString(pOffset, pEcl.vertreterStrasse);pOffset++;
            pPStm.setString(pOffset, pEcl.vertreterPlz);pOffset++;
            pPStm.setString(pOffset, pEcl.vertreterOrt);pOffset++;

            pPStm.setString(pOffset, pEcl.vertreterMail);pOffset++;

            pPStm.setInt(pOffset, pEcl.vertreterArt);pOffset++;
            pPStm.setString(pOffset, pEcl.vertreterArtBeiSonstige);pOffset++;

            pPStm.setString(pOffset, pEcl.eingabeDatum);pOffset++;
            pPStm.setInt(pOffset, pEcl.wurdeStorniert);pOffset++;
            pPStm.setString(pOffset, pEcl.stornierungsDatum);pOffset++;
           
            pPStm.setInt(pOffset, pEcl.pruefstatus);pOffset++;
            pPStm.setInt(pOffset, pEcl.abgelehntWeil);pOffset++;
            pPStm.setString(pOffset, pEcl.abgelehntWeilText);pOffset++;

            pPStm.setInt(pOffset, pEcl.pruefstatusStorno);pOffset++;
            pPStm.setInt(pOffset, pEcl.inBearbeitungDurchUserNr);pOffset++;

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

//  @formatter:off
    private final String[] felder= {"ident", 
            "erteilendeAktionaersnummer", "erteilendeLoginDaten", 
            "artDerEingabe", 
            "vertreterId", "vertreterTitel", "vertreterName", "vertreterVorname",
            "vertreterZusatz", "vertreterStrasse", "vertreterPlz", "vertreterOrt",
            "vertreterMail", 
            "vertreterArt", "vertreterArtBeiSonstige", 
            "eingabeDatum", "wurdeStorniert", "stornierungsDatum", 
            "pruefstatus", "abgelehntWeil", "abgelehntWeilText", "pruefstatusStorno", "inBearbeitungDurchUserNr"
                    + ""
                    + ""
             }; 
//  @formatter:on
    private boolean initErfolgt=false;
    private void initFelder(){
        if (initErfolgt) {return;}
        initErfolgt=true;
    }

    @Override
    public int insert(EclVorlaeufigeVollmachtEingabe pEcl) {
        /* neue InterneIdent vergeben */
        int erg=0;
        erg = dbBasis.getInterneIdentVorlaeufigeVollmachtEingabe();
        if (erg < 1) {
            CaBug.drucke("002");
            return (erg);
        }

        pEcl.ident = erg;
        initFelder();
        return insertIntern(felder, pEcl);
    }


//     public int readAll() {
//        PreparedStatement lPStm = null;
//        int anzInArray = 0;
//        try {
//            String lSql = "SELECT * from " + getSchema() + getTableName()+" "
//                    + "ORDER BY gehoertZuElement, gehoertZuElementDetail, position";
//            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
//            anzInArray = readIntern(lPStm);
//
//        } catch (Exception e) {
//            CaBug.drucke("003");
//            System.err.println(" " + e.getMessage());
//            return (-1);
//        }
//        return (anzInArray);
//    }
//
    
    /**Achtung, der Prozess in dem diese Funktion verwendet wird, ist nicht 100% mehrbenutzerfähig!
     * 
     * Liefert alle ungeprüften Sätze. Der erste ist dann der, der als nächstes bearbeitet werden wird
      */
    public int readNaechstenUngeprueftenAktionaer() {
        PreparedStatement lPStm = null;
        int anzInArray = 0;
        try {
            String lSql = "SELECT * from " + getSchema() + getTableName()+" ";
            lSql = lSql + " WHERE (pruefstatus=0 OR (wurdeStorniert=1 AND pruefStatusStorno=0)) AND inBearbeitungDurchUserNr=0 ";
            lSql=lSql+ "ORDER BY ident;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
           anzInArray = readIntern(lPStm);

        } catch (Exception e) {
            CaBug.drucke("003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
        
    }
    
    /**pArt==0 => es werden alle Arten gelesen*/
   public int readArtZuAktionaersnummerUndReserviere(String pAktionaersnummer, int pArt) {
        int rc=readArtZuAktionaersnummer(pAktionaersnummer, pArt);
        if (rc>0) {
            int reservierungsnummer=dbBundle.dbBasis.getInterneIdentBestWorkflowBasis();
            for (int i=0;i<rc;i++) {
                EclVorlaeufigeVollmachtEingabe lVorlaeufigeVollmachtEingabe=this.ergebnisPosition(i);
                updateReserviere(lVorlaeufigeVollmachtEingabe, reservierungsnummer);
            }
        }
        
        
        return rc;
    }
    
    /**pArt==0 => es werden alle Arten gelesen*/
     public int readArtZuAktionaersnummer(String pAktionaersnummer, int pArt) {
         PreparedStatement lPStm = null;
         int anzInArray = 0;
         try {
             String lSql = "SELECT * from " + getSchema() + getTableName()+" ";
             lSql = lSql + " WHERE erteilendeAktionaersnummer=? ";
             if (pArt!=0) {
                 lSql=lSql+ "AND artDerEingabe=? ";
             }
             lSql=lSql+ "ORDER BY ident;";
             lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
             lPStm.setString(1, pAktionaersnummer);
             if (pArt!=0) {
                 lPStm.setInt(2, pArt);
             }
            anzInArray = readIntern(lPStm);

         } catch (Exception e) {
             CaBug.drucke("003");
             System.err.println(" " + e.getMessage());
             return (-1);
         }
         return (anzInArray);
     }
    
     /**0=ungeprüft
      * 101=Geprüft
      * 102=Wiedervorlage
      */
     public int readAnzahl(int pPruefStatusErgebnis) {
         PreparedStatement lPStm = null;
         int anzInArray = 0;
         try {
             String lSql = "SELECT * from " + getSchema() + getTableName()+" ";
             lSql = lSql + " WHERE pruefstatus=? ";
             if (pPruefStatusErgebnis==0) {
                 lSql=lSql+" AND (wurdeStorniert=1 AND pruefStatusStorno=0)";
             }
             lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
             lPStm.setInt(1, pPruefStatusErgebnis);
             anzInArray = readIntern(lPStm);

         } catch (Exception e) {
             CaBug.drucke("003");
             System.err.println(" " + e.getMessage());
             return (-1);
         }
         return (anzInArray);
     }

     public int readAnzahlInBearbeitung() {
         PreparedStatement lPStm = null;
         int anzInArray = 0;
         try {
             String lSql = "SELECT * from " + getSchema() + getTableName()+" ";
             lSql = lSql + " WHERE inBearbeitungDurchUserNr!=0 ";
             lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
             anzInArray = readIntern(lPStm);

         } catch (Exception e) {
             CaBug.drucke("003");
             System.err.println(" " + e.getMessage());
             return (-1);
         }
         return (anzInArray);
     }

     /**Update - setze auf Storniert. 
       */
     public int updateStorniert(EclVorlaeufigeVollmachtEingabe pEcl) {
         int ergebnis=0;
         initFelder();

         try {

             String lSql="UPDATE "+getSchema()+getTableName()
                     +" SET wurdeStorniert=1, "
                     +" stornierungsDatum=? "
                     + " WHERE "
                     + "ident=? ";

             PreparedStatement lPStm = verbindung.prepareStatement(lSql);
//             fuellePreparedStatementKomplett(lPStm, 1, pEcl);
             lPStm.setString(1, pEcl.stornierungsDatum);
             lPStm.setInt(2, pEcl.ident);

             ergebnis = updateIntern(lPStm);
         } catch (Exception e1) {
             CaBug.drucke("001");
             System.err.println(" " + e1.getMessage());
             return (-1);
         }

         return ergebnis;
     }

     /**Update - setze auf in Bearbeitung. 
      */
    public int updateReserviere(EclVorlaeufigeVollmachtEingabe pEcl, int pReservierungsnummer) {
        int ergebnis=0;
        initFelder();

        try {

            String lSql="UPDATE "+getSchema()+getTableName()
                    +" SET inBearbeitungDurchUserNr=? "
                    + " WHERE "
                    + "ident=? ";

            PreparedStatement lPStm = verbindung.prepareStatement(lSql);
//            fuellePreparedStatementKomplett(lPStm, 1, pEcl);
            lPStm.setInt(1, pReservierungsnummer);
            lPStm.setInt(2, pEcl.ident);

            ergebnis = updateIntern(lPStm);
        } catch (Exception e1) {
            CaBug.drucke("001");
            System.err.println(" " + e1.getMessage());
            return (-1);
        }

        return ergebnis;
    }
     
    
    /**Update - setze auf in Bearbeitung wieder auf 0. Update Status 
     */
   public int updateUndGebeFrei(EclVorlaeufigeVollmachtEingabe pEcl, int pNeuerStatus) {
       int ergebnis=0;
       initFelder();

       try {

           String lSql="UPDATE "+getSchema()+getTableName()
                   +" SET inBearbeitungDurchUserNr=0, pruefstatus=? "
                   + " WHERE "
                   + "ident=? ";

           PreparedStatement lPStm = verbindung.prepareStatement(lSql);
//           fuellePreparedStatementKomplett(lPStm, 1, pEcl);
           lPStm.setInt(1, pNeuerStatus);
           lPStm.setInt(2, pEcl.ident);

           ergebnis = updateIntern(lPStm);

           
           lSql="UPDATE "+getSchema()+getTableName()
           +" SET inBearbeitungDurchUserNr=0, pruefstatusStorno=? "
           + " WHERE "
           + "ident=? and wurdeStorniert!=0";

           lPStm = verbindung.prepareStatement(lSql);
//          fuellePreparedStatementKomplett(lPStm, 1, pEcl);
            lPStm.setInt(1, pNeuerStatus);
            lPStm.setInt(2, pEcl.ident);

            int ergebnis1 = updateIntern(lPStm);
            
            ergebnis+=ergebnis1;

           
       } catch (Exception e1) {
           CaBug.drucke("001");
           System.err.println(" " + e1.getMessage());
           return (-1);
       }

       return ergebnis;
   }
    
   
   
   /**0, wenn nichts mehr in Bearbeitung; ansonsten der Satz in bearebtigun ganz normal als Ergebnis*/
   public int reservierungAlleZuruecksetzen() {

       int ergebnis;

       try {
           String lSql = "UPDATE " + getSchema()+getTableName()+" SET "
                   + "inBearbeitungDurchUserNr=0 " + "WHERE " + "inBearbeitungDurchUserNr!=0";

           PreparedStatement lPStm = verbindung.prepareStatement(lSql);

           ergebnis = lPStm.executeUpdate();
           lPStm.close();
           if (ergebnis == 0) {
               return (CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert);
           }
       } catch (Exception e1) {
           CaBug.drucke("DbBestWorkflow.reservierungAlleZuruecksetzen 001");
           System.err.println(" " + e1.getMessage());
           return (CaFehler.pfdXyBereitsVorhanden);
       }

       return (1);
   }

   
//    
//    /**Update. 
//     * 
//     * Returnwert:
//     * pfXyWurdeVonAnderemBenutzerVeraendert
//     * -1 => unbekannter Fehler
//     * 1 = Update wurde durchgeführt.
//     * 
//     */
//    public int update(EclVorlaeufigeVollmachtEingabe pEcl) {
//        int ergebnis=0;
//        initFelder();
//
//        try {
//
//            String lSql = setzeUpdateBasisStringZusammen(felder)
//                    + " WHERE "
//                    + "setIdent=? ";
//
//            PreparedStatement lPStm = verbindung.prepareStatement(lSql);
//            fuellePreparedStatementKomplett(lPStm, 1, pEcl);
//            lPStm.setInt(getAnzFelder() + 1, pEcl.setIdent);
//
//            ergebnis = updateIntern(lPStm);
//        } catch (Exception e1) {
//            CaBug.drucke("001");
//            System.err.println(" " + e1.getMessage());
//            return (-1);
//        }
//
//        return ergebnis;
//    }
//
//
// 
//    /**Return-Werte:
//     * pfXyWurdeVonAnderemBenutzerVeraendert
//     * -1 => undefinierter Fehler
//     * 1 => Löschen erfolgreich
//     */
//    public int delete(int pIdent) {
//        int ergebnis=0;
//        try {
//            String sql = "DELETE FROM " + getSchema()+getTableName()+
//                    " WHERE setIdent=? ";
//            PreparedStatement pstm1 = verbindung.prepareStatement(sql);
//            pstm1.setInt(1, pIdent);
//            ergebnis = deleteIntern(pstm1);
//        } catch (Exception e1) {
//            CaBug.drucke("001");
//            System.err.println(" " + e1.getMessage());
//            return (-1);
//        }
//
//        return ergebnis;
//    }
//


}
