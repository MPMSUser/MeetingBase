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
import de.meetingapps.meetingportal.meetComEntities.EclMitteilung;
import de.meetingapps.meetingportal.meetComKonst.KonstMitteilungStatus;
import de.meetingapps.meetingportal.meetComKonst.KonstPortalFunktionen;

/**Hinweis: rueckfragen wird wie fragen behandelt*/

public class  DbMitteilung extends DbRoot<EclMitteilung> {

    private int logDrucken=10;
    
    public DbMitteilung(DbBundle pDbBundle) {
        super(pDbBundle);
    }

    private int lFunktion=0;
    /**Sonderfunktionalität in dieser Klasse:
     * über setzeFunktion und pFunktion laut KonstPortalFunktionen wird gesteuert,
     * welches Table (Mitteilungen, Fragen, Wortmeldungen, Anträge, Widersprüche)
     * verwendet wird.
     * 
     * Umschalten kann jederzeit erfolgen, neuer Open nicht erforderlich
     */
    public void setzeFunktion(int pFunktion) {
        lFunktion=pFunktion;
    }
    
    
    @Override
    String getCreateString() {

//      @formatter:off
       String createString = "CREATE TABLE " + dbBundle.getSchemaMandant() + getTableName()+" ( "
                + "`mitteilungIdent` int(11) NOT NULL, "
                + "`version` int(11) NOT NULL DEFAULT '0', "
                + "`db_version` bigint(20) DEFAULT NULL, " 
                + "`loginIdent` int(11) NOT NULL DEFAULT '0', "
                + "`anzahlAktionaereZumZeitpunktDerMitteilung` int(11) NOT NULL DEFAULT '0', " 
                + "`anzahlAktienZumZeitpunktderMitteilung` int(11) NOT NULL DEFAULT '0', "
                + "`gattungen` int(11) NOT NULL DEFAULT '0', "
                + "`identString` varchar(20) NOT NULL DEFAULT NULL DEFAULT '', " 
                + "`nameVornameOrtKennung` varchar(200) NOT NULL DEFAULT NULL DEFAULT '', " 
                + "`nameVornameOrt` varchar(200) NOT NULL DEFAULT NULL DEFAULT '', " 
                + "`kontaktDaten` varchar(100) NOT NULL DEFAULT NULL DEFAULT '', " 
                + "`kontaktDatenTelefon` varchar(100) NOT NULL DEFAULT NULL DEFAULT '', " 
                + "`hinweisWurdeBestaetigt` int(11) NOT NULL DEFAULT '0', ";

        for (int i = 0; i < 200; i++) {
            createString = createString + "`mitteilungZuTop" + Integer.toString(i) + "` int(11) NOT NULL DEFAULT '0', ";
        }
        createString = createString 
                + "`inhaltsHinweise` int(11) NOT NULL DEFAULT '0', "
                + "`mitteilungKurztext` varchar(100) NOT NULL DEFAULT '', "
                + "`mitteilungLangtext` varchar(18000) NOT NULL DEFAULT '', "
                + "`zeitpunktDerMitteilung` varchar(19) NOT NULL DEFAULT '', " 
                + "`zeitpunktDesRueckzugs` varchar(19) NOT NULL DEFAULT '', " 
               + "`drucklaufNr` int(11) NOT NULL DEFAULT '0', "
               + "`status` int(11) NOT NULL DEFAULT '0', "
               + "`raumNr` int(11) NOT NULL DEFAULT '0', "
               + "`lfdNrInListe` int(11) NOT NULL DEFAULT '0', "
               + "`botschaftsart` int(11) NOT NULL DEFAULT '0', "
               + "`dateiname` varchar(200) NOT NULL DEFAULT '', " 
               + "`freigegeben` int(11) NOT NULL DEFAULT '0', "
               + "`internerDateiname` varchar(20) NOT NULL DEFAULT '', " 
               + "`internerDateizusatz` varchar(20) NOT NULL DEFAULT '', " 
               + "`interneVerarbeitungLaufend` int(11) NOT NULL DEFAULT '0', "
               + "`verweisAufUnterlagenident` int(11) NOT NULL DEFAULT '0', "
               + "`kommentarIntern` varchar(500) NOT NULL DEFAULT NULL DEFAULT '', " 
               + "`kommentarVersammlungsleiter` varchar(500) NOT NULL DEFAULT NULL DEFAULT '', " 
               + "PRIMARY KEY (`mitteilungIdent`, `version`) " + ") ";
//      @formatter:on
        return createString;
    }

    @Override
    String getSchema() {
        return dbBundle.getSchemaMandant();
    }


    @Override
    void resetInterneIdent(int pHoechsteIdent) {
        switch (lFunktion) {
        case KonstPortalFunktionen.rueckfragen:
        case KonstPortalFunktionen.fragen:
           dbBundle.dbBasis.resetInterneIdentFrage(pHoechsteIdent);
            return ;
        case KonstPortalFunktionen.wortmeldungen:
            dbBundle.dbBasis.resetInterneIdentWortmeldungen(pHoechsteIdent);
            return ;
        case KonstPortalFunktionen.widersprueche:
            dbBundle.dbBasis.resetInterneIdentWiderspruch(pHoechsteIdent);
            return ;
        case KonstPortalFunktionen.antraege:
            dbBundle.dbBasis.resetInterneIdentAntraege(pHoechsteIdent);
            return ;
        case KonstPortalFunktionen.sonstigeMitteilungen:
            dbBundle.dbBasis.resetInterneIdentSonstMitteilungen(pHoechsteIdent);
            return ;
        case KonstPortalFunktionen.botschaftenEinreichen:
            dbBundle.dbBasis.resetInterneIdentBotschaften(pHoechsteIdent);
            return ;
        }
        CaBug.drucke("001");
    }


    @Override
    String getTableName() {
        switch (lFunktion) {
        case KonstPortalFunktionen.rueckfragen:
        case KonstPortalFunktionen.fragen:
            return "tbl_frage";
        case KonstPortalFunktionen.wortmeldungen:
            return "tbl_wortmeldung";
        case KonstPortalFunktionen.widersprueche:
            return "tbl_widerspruch";
        case KonstPortalFunktionen.antraege:
            return "tbl_antrag";
        case KonstPortalFunktionen.sonstigeMitteilungen:
            return "tbl_sonstMitteilung";
        case KonstPortalFunktionen.botschaftenEinreichen:
            return "tbl_botschaft";
        }
        CaBug.drucke("001");
        return "";
    }


    @Override
    String getFeldFuerInterneIdent() {
        return "mitteilungIdent";
    }

    @Override
    int getAnzFelder() {
        return 231;
    }


    @Override
    EclMitteilung decodeErgebnis(ResultSet pErgebnis) {
        EclMitteilung lEclReturn = new EclMitteilung();

        try {
            lEclReturn.mitteilungIdent = pErgebnis.getInt("mitteilungIdent");
            lEclReturn.version = pErgebnis.getInt("version");
            lEclReturn.db_version = pErgebnis.getLong("db_version");

            lEclReturn.loginIdent = pErgebnis.getInt("loginIdent");
            lEclReturn.anzahlAktionaereZumZeitpunktDerMitteilung = pErgebnis.getInt("anzahlAktionaereZumZeitpunktDerMitteilung");
            lEclReturn.anzahlAktienZumZeitpunktderMitteilung = pErgebnis.getLong("anzahlAktienZumZeitpunktderMitteilung");
            
            int hGattungen  = pErgebnis.getInt("gattungen");
            lEclReturn.gattungen[0]=(hGattungen & 1);
            lEclReturn.gattungen[1]=(hGattungen & 2);
            lEclReturn.gattungen[2]=(hGattungen & 4);
            lEclReturn.gattungen[3]=(hGattungen & 8);
            lEclReturn.gattungen[4]=(hGattungen & 16);
            
            lEclReturn.identString = pErgebnis.getString("identString");
            lEclReturn.nameVornameOrtKennung = pErgebnis.getString("nameVornameOrtKennung");
            lEclReturn.nameVornameOrt = pErgebnis.getString("nameVornameOrt");
            lEclReturn.kontaktDaten = pErgebnis.getString("kontaktDaten");
            lEclReturn.kontaktDatenTelefon = pErgebnis.getString("kontaktDatenTelefon");
            lEclReturn.hinweisWurdeBestaetigt = pErgebnis.getInt("hinweisWurdeBestaetigt");

            for (int i = 0; i < 200; i++) {
                lEclReturn.mitteilungZuTop[i] = pErgebnis.getInt("mitteilungZuTop" + Integer.toString(i));
            }

            int lInhaltsHinweise=pErgebnis.getInt("inhaltsHinweise");
            int offset=1;
            for (int i=0;i<10;i++) {
                lEclReturn.inhaltsHinweis[i]=((lInhaltsHinweise & offset)==offset);
                offset=offset*2;
            }
            
            lEclReturn.mitteilungKurztext = pErgebnis.getString("mitteilungKurztext");
            lEclReturn.mitteilungLangtext = pErgebnis.getString("mitteilungLangtext");
            lEclReturn.zeitpunktDerMitteilung = pErgebnis.getString("zeitpunktDerMitteilung");
            lEclReturn.zeitpunktDesRueckzugs = pErgebnis.getString("zeitpunktDesRueckzugs");
            
             lEclReturn.drucklaufNr = pErgebnis.getInt("drucklaufNr");
             lEclReturn.status = pErgebnis.getInt("status");
             lEclReturn.raumNr = pErgebnis.getInt("raumNr");
             lEclReturn.lfdNrInListe = pErgebnis.getInt("lfdNrInListe");

             lEclReturn.botschaftsart = pErgebnis.getInt("botschaftsart");
             lEclReturn.dateiname = pErgebnis.getString("dateiname");
             lEclReturn.freigegeben = pErgebnis.getInt("freigegeben");
             lEclReturn.internerDateiname = pErgebnis.getString("internerDateiname");
             lEclReturn.internerDateizusatz = pErgebnis.getString("internerDateizusatz");
             lEclReturn.interneVerarbeitungLaufend = pErgebnis.getInt("interneVerarbeitungLaufend");
             lEclReturn.verweisAufUnterlagenident = pErgebnis.getInt("verweisAufUnterlagenident");
          
             lEclReturn.kommentarIntern = pErgebnis.getString("kommentarIntern");
             lEclReturn.kommentarVersammlungsleiter = pErgebnis.getString("kommentarVersammlungsleiter");
             
             /*Nicht in Datenbank*/
             if (lFunktion==KonstPortalFunktionen.rueckfragen) {
                 lEclReturn.artDerMitteilung=KonstPortalFunktionen.fragen;
             }
             else {
                 lEclReturn.artDerMitteilung=lFunktion;
             }
                     
       } catch (Exception e) {
            CaBug.drucke("001");
            System.err.println(" " + e.getMessage());
        }

        return lEclReturn;
    }



    @Override
    void fuellePreparedStatementKomplett(PreparedStatement pPStm, int pOffset, EclMitteilung pEcl) {
        int startOffset = pOffset; /*Nur erforderlich zum Überprüfen von Programmierfehlern - Feldanzahl*/

        try {
//          @formatter:off
            
            pPStm.setInt(pOffset, pEcl.mitteilungIdent);pOffset++;
            pPStm.setInt(pOffset, pEcl.version);pOffset++;
            pPStm.setLong(pOffset, pEcl.db_version);pOffset++;
            pPStm.setInt(pOffset, pEcl.loginIdent);pOffset++;
            pPStm.setInt(pOffset, pEcl.anzahlAktionaereZumZeitpunktDerMitteilung);pOffset++;
            pPStm.setLong(pOffset, pEcl.anzahlAktienZumZeitpunktderMitteilung);pOffset++;
 
            int hGattungen=0;
            if (pEcl.gattungen[0]==1) {hGattungen=(hGattungen | 1);}
            if (pEcl.gattungen[1]==1) {hGattungen=(hGattungen | 2);}
            if (pEcl.gattungen[2]==1) {hGattungen=(hGattungen | 4);}
            if (pEcl.gattungen[3]==1) {hGattungen=(hGattungen | 8);}
            if (pEcl.gattungen[4]==1) {hGattungen=(hGattungen | 16);}
            pPStm.setInt(pOffset, hGattungen);pOffset++;

            pPStm.setString(pOffset, pEcl.identString);pOffset++;
            pPStm.setString(pOffset, pEcl.nameVornameOrtKennung);pOffset++;
            pPStm.setString(pOffset, pEcl.nameVornameOrt);pOffset++;
            pPStm.setString(pOffset, pEcl.kontaktDaten);pOffset++;
            pPStm.setString(pOffset, pEcl.kontaktDatenTelefon);pOffset++;
            pPStm.setInt(pOffset, pEcl.hinweisWurdeBestaetigt);pOffset++;

            for (int i = 0; i < 200; i++) {
                pPStm.setInt(pOffset, pEcl.mitteilungZuTop[i]);pOffset++;
            }
            
            int lInhaltsHinweise=0;
            int offset=1;
            for (int i=0;i<10;i++) {
                if (pEcl.inhaltsHinweis[i]) {
                    lInhaltsHinweise = (lInhaltsHinweise | offset);
                }
                offset=offset*2;
            }
            pPStm.setInt(pOffset, lInhaltsHinweise);pOffset++;

            pPStm.setString(pOffset, pEcl.mitteilungKurztext);pOffset++;
            pPStm.setString(pOffset, pEcl.mitteilungLangtext);pOffset++;
            pPStm.setString(pOffset, pEcl.zeitpunktDerMitteilung);pOffset++;
            pPStm.setString(pOffset, pEcl.zeitpunktDesRueckzugs);pOffset++;

            pPStm.setInt(pOffset, pEcl.drucklaufNr);pOffset++;
            pPStm.setInt(pOffset, pEcl.status);pOffset++;
            pPStm.setInt(pOffset, pEcl.raumNr);pOffset++;
            pPStm.setInt(pOffset, pEcl.lfdNrInListe);pOffset++;
 
            pPStm.setInt(pOffset, pEcl.botschaftsart);pOffset++;
            pPStm.setString(pOffset, pEcl.dateiname);pOffset++;
            pPStm.setInt(pOffset, pEcl.freigegeben);pOffset++;
            pPStm.setString(pOffset, pEcl.internerDateiname);pOffset++;
            pPStm.setString(pOffset, pEcl.internerDateizusatz);pOffset++;
            pPStm.setInt(pOffset, pEcl.interneVerarbeitungLaufend);pOffset++;
            pPStm.setInt(pOffset, pEcl.verweisAufUnterlagenident);pOffset++;

            pPStm.setString(pOffset, pEcl.kommentarIntern);pOffset++;
            pPStm.setString(pOffset, pEcl.kommentarVersammlungsleiter);pOffset++;

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
        felder[0]="mitteilungIdent";
        felder[1]="version";
        felder[2]="db_version";
        felder[3]="loginIdent";
        felder[4]="anzahlAktionaereZumZeitpunktDerMitteilung";
        felder[5]="anzahlAktienZumZeitpunktderMitteilung";
        felder[6]="gattungen";
        felder[7]="identString";
        felder[8]="nameVornameOrtKennung";
        felder[9]="nameVornameOrt";
        felder[10]="kontaktDaten";
        felder[11]="kontaktDatenTelefon";
        felder[12]="hinweisWurdeBestaetigt";
        for (int i=0;i<200;i++) {
            felder[13+i]="mitteilungZuTop"+Integer.toString(i);
        }
        felder[213]="inhaltsHinweise";
        felder[214]="mitteilungKurztext";
        felder[215]="mitteilungLangtext";
        felder[216]="zeitpunktDerMitteilung";
        felder[217]="zeitpunktDesRueckzugs";
        felder[218]="drucklaufNr";
        felder[219]="status";
        felder[220]="raumNr";
        felder[221]="lfdNrInListe";
        felder[222]="botschaftsart";
        felder[223]="dateiname";
        felder[224]="freigegeben";
        felder[225]="internerDateiname";
        felder[226]="internerDateizusatz";
        felder[227]="interneVerarbeitungLaufend";
        felder[228]="verweisAufUnterlagenident";
        felder[229]="kommentarIntern";
        felder[230]="kommentarVersammlungsleiter";
        initErfolgt=true;
    }

    @Override
    public int insert(EclMitteilung pEcl) {

        /* neue InterneIdent vergeben */
        int erg=0;
        switch (lFunktion) {
        case KonstPortalFunktionen.rueckfragen:
        case KonstPortalFunktionen.fragen:
            erg = dbBasis.getInterneIdentFrage();
            break;
        case KonstPortalFunktionen.wortmeldungen:
            erg = dbBasis.getInterneIdentWortmeldungen();
            break;
        case KonstPortalFunktionen.widersprueche:
            erg = dbBasis.getInterneIdentWiderspruch();
            break;
        case KonstPortalFunktionen.antraege:
            erg = dbBasis.getInterneIdentAntraege();
            break;
        case KonstPortalFunktionen.sonstigeMitteilungen:
            erg = dbBasis.getInterneIdentSonstMitteilungen();
            break;
        case KonstPortalFunktionen.botschaftenEinreichen:
            erg = dbBasis.getInterneIdentBotschaften();
            break;
        default:
            CaBug.drucke("001");
        }

        if (erg < 1) {
            CaBug.drucke("002");
            return (erg);
        }

        pEcl.mitteilungIdent = erg;

        initFelder();
        dbBasis.beginTransactionNeu();
        int rc=insertIntern(felder, pEcl);
        dbBasis.endTransactionNeu();
        return rc;
    }

    /**Keine Vergabe einer neuen Ident*/
    public int insertWeitereVersion(EclMitteilung pEcl) {
        initFelder();
        return insertIntern(felder, pEcl);
    }
    
    /** Return-Wert = Anzahl der gefundenen Sätze (oder Fehlermeldung <0)
     * Liefert alle Sätze der versionen, die 0-er Version (also die Einreichung) kommt an erster Position*/
    public int read(int pMitteilungsIdent) {
        int anzInArray = 0;
        PreparedStatement lPStm = null;

        try {
            String lSql = "SELECT * from " + getSchema() + getTableName()+" where " + 
                    "mitteilungIdent=? " + "ORDER BY mitteilungIdent, version;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, pMitteilungsIdent);
            
            anzInArray = readIntern(lPStm);

        } catch (Exception e) {
            CaBug.drucke("003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    public int holeMaxVersion(int pMitteilungsIdent) {
        int anzInArray = 0;
        PreparedStatement lPStm = null;
        int maxVersion=0;
            
        try {
            String lSql = "SELECT * from " + getSchema() + getTableName()+" where " + 
                    "mitteilungIdent=? " + "ORDER BY mitteilungIdent, version;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, pMitteilungsIdent);

            anzInArray = readIntern(lPStm);

        } catch (Exception e) {
            CaBug.drucke("003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        
        for (int i=0;i<anzInArray;i++) {
            if (ergebnisArray.get(i).version>maxVersion) {
                maxVersion=ergebnisArray.get(i).version;
            }
        }
        return (maxVersion);
        
    }
    /** Return-Wert = Anzahl der gefundenen Sätze (oder Fehlermeldung <0)
     * Liefert alle Sätze der versionen, die 0-er Version (also die Einreichung) kommt an erster Position*/
    public int readVerweisAufUnterlagenident(int pVerweisAufUnterlagenident) {
        int anzInArray = 0;
        PreparedStatement lPStm = null;

        try {
            String lSql = "SELECT * from " + getSchema() + getTableName()+" where " + 
                    "verweisAufUnterlagenident=? " + "ORDER BY mitteilungIdent, version;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, pVerweisAufUnterlagenident);

            anzInArray = readIntern(lPStm);

        } catch (Exception e) {
            CaBug.drucke("003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    
    /** Return-Wert = Anzahl der gefundenen Sätze (oder Fehlermeldung <0) (bei Botschaften: nur die "Einreichung", also version=0)*/
    public int readAll_loginIdent(int pLoginIdent) {
        PreparedStatement lPStm = null;
        int anzInArray = 0;
        try {
            String lSql = "SELECT * from " + getSchema() + getTableName()+" where " + 
                    "loginIdent=? AND version=0 " + "ORDER BY mitteilungIdent;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, pLoginIdent);
            anzInArray = readIntern(lPStm);

        } catch (Exception e) {
            CaBug.drucke("003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    /** Return-Wert = Anzahl der gefundenen Sätze (oder Fehlermeldung <0) (bei Botschaften: nur die "Einreichung", also version=0)*/
    public int readVersion0_internerDateiname(String pDateinameIntern) {
        PreparedStatement lPStm = null;
        int anzInArray = 0;
        try {
            String lSql = "SELECT * from " + getSchema() + getTableName()+" where " + 
                    "internerDateiname=? AND version=0 " + "ORDER BY mitteilungIdent;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setString(1, pDateinameIntern);
            anzInArray = readIntern(lPStm);

        } catch (Exception e) {
            CaBug.drucke("003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    /**pDrucklauf kann 0 sein, dann alle
     * 
     * in jedem Fall nur die mit version = 0, also bei Botschaften nur die "Einreichung"*/
    public int readAll_mitteilungen(int pDrucklauf) {
        PreparedStatement lPStm = null;
        int anzInArray = 0;
        try {
            String lSql = "SELECT * from " + getSchema() + getTableName()+" WHERE version=0 ";
            if (pDrucklauf != 0) {
                lSql = lSql + " AND drucklaufNr=? ";
            }
            lSql = lSql + " ORDER BY mitteilungIdent;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            if (pDrucklauf != 0) {
                lPStm.setInt(1, pDrucklauf);
            }
            anzInArray = readIntern(lPStm);

        } catch (Exception e) {
            CaBug.drucke("003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    /**pDrucklauf kann 0 sein, dann alle*/
    public int readAll_rednerliste() {
        PreparedStatement lPStm = null;
        int anzInArray = 0;
        try {
            String lSql = "SELECT * from " + getSchema() + getTableName();
            lSql = lSql + " ORDER BY lfdNrInListe, mitteilungIdent;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            anzInArray = readIntern(lPStm);

        } catch (Exception e) {
            CaBug.drucke("003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    /**Liest alle Mitteilungen einschließlich aller Versionen ein.
     * Sortiert nach MitteilungsIdent, Version*/
     public int readAll() {
        PreparedStatement lPStm = null;
        int anzInArray = 0;
        try {
            String lSql = "SELECT * from " + getSchema() + getTableName()+" ";
            lSql = lSql + " ORDER BY mitteilungIdent, version;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
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
    public int update(EclMitteilung pEcl) {
        int ergebnis=0;
        initFelder();

        pEcl.db_version++;

        try {

            String lSql = setzeUpdateBasisStringZusammen(felder)
                    + " WHERE "
                    + "mitteilungIdent=? AND version=? AND db_version=? ";

            PreparedStatement lPStm = verbindung.prepareStatement(lSql);
            fuellePreparedStatementKomplett(lPStm, 1, pEcl);
            lPStm.setInt(getAnzFelder() + 1, pEcl.mitteilungIdent);
            lPStm.setInt(getAnzFelder() + 2, pEcl.version);
            lPStm.setLong(getAnzFelder() + 3, pEcl.db_version - 1);

            ergebnis = updateIntern(lPStm);
        } catch (Exception e1) {
            CaBug.drucke("001");
            System.err.println(" " + e1.getMessage());
            return (-1);
        }

        return ergebnis;
    }

   
    /**Multiuserfähig
     * Hier werden auch die unterschiedlichen Version in versionen alle gleich upgedated. Für Wortmeldungen spielt das keine Rolle,
     * da nur Sätze mit version==0 existieren.
     * Für alle anderen gilt: Status muß ja in allen gleich aktualisiert werden.
     * */
    public int update_status(int pIdent, long pVersion, int pStatus, int pTelefonieNr) {

        CaBug.druckeLog("pIdent="+pIdent+" pVersion="+pVersion, logDrucken, 10);
        try {

            String lSql = "UPDATE " + getSchema() + getTableName() + " SET "
                    + "db_version=db_version+1, status=?, raumNr=? " + "WHERE " + "mitteilungIdent=?  AND db_version=? ";

            PreparedStatement lPStm = verbindung.prepareStatement(lSql);
            lPStm.setInt(1, pStatus);
            lPStm.setInt(2, pTelefonieNr);
            lPStm.setInt(3, pIdent);
            lPStm.setLong(4, pVersion);

            dbBasis.beginTransactionNeu();
            int ergebnis = lPStm.executeUpdate();
            dbBasis.endTransactionNeu();
            lPStm.close();
            if (ergebnis == 0) {
                return (CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert);
            }
        } catch (Exception e1) {
            CaBug.drucke("001");
            System.err.println(" " + e1.getMessage());
            return (-1);
        }

        return (1);
    }

    public int update_LfdNr(int pLfdNr) {

        try {

            String lSql = "UPDATE " + getSchema() + getTableName() + " SET "
                    + "db_version=db_version+1, lfdNrInListe=lfdNrInListe+1 " + "WHERE lfdNrInListe>=? ";

            PreparedStatement lPStm = verbindung.prepareStatement(lSql);
            lPStm.setInt(1, pLfdNr);

            dbBasis.beginTransactionNeu();
            int ergebnis = lPStm.executeUpdate();
            dbBasis.endTransactionNeu();
            lPStm.close();
            if (ergebnis == 0) {
                return (CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert);
            }
        } catch (Exception e1) {
            CaBug.drucke("001");
            System.err.println(" " + e1.getMessage());
            return (-1);
        }

        return (1);
    }

    /**Returnwert: neue lfdNr, oder <0=Fehler*/
    public int update_statusUndAnsEnde(int pIdent, long pVersion, int pStatus, int pTelefonieNr) {
        
        CaBug.druckeLog("pIdent="+pIdent+" pVersion="+pVersion, logDrucken, 10);
        
        PreparedStatement lPStm = null;
        int ergebnis = 0;
        int maxWert = 0;

        
        String lSql = "SELECT MAX(lfdNrInListe) " + "FROM " + dbBundle.getSchemaMandant() + getTableName();

        try {
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            ResultSet lErgebnis = lPStm.executeQuery();
            lErgebnis.last();
            lErgebnis.beforeFirst();

            while (lErgebnis.next() == true) {
                ergebnis = lErgebnis.getInt(1);
            }
            maxWert = ergebnis;
            lErgebnis.close();
            lPStm.close();

        } catch (SQLException e) {
            CaBug.drucke("003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        
        try {

            lSql = "UPDATE " + getSchema() + getTableName() + " SET "
                    + "db_version=db_version+1, status=?, raumNr=?, lfdNrInListe=? " + "WHERE " + "mitteilungIdent=?  AND db_version=? ";

            lPStm = verbindung.prepareStatement(lSql);
            lPStm.setInt(1, pStatus);
            lPStm.setInt(2, pTelefonieNr);
            lPStm.setInt(3, maxWert+1);
            lPStm.setInt(4, pIdent);
            lPStm.setLong(5, pVersion);

            dbBasis.beginTransactionNeu();
            ergebnis = lPStm.executeUpdate();
            dbBasis.endTransactionNeu();
            lPStm.close();
            if (ergebnis == 0) {
                return (CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert);
            }
        } catch (Exception e1) {
            CaBug.drucke("001");
            System.err.println(" " + e1.getMessage());
            return (-1);
        }

        return (maxWert+1);
    }

//    public int update_status(int pIdent, int pStatus) {
//
//        try {
//
//            String lSql = "UPDATE " + getSchema() + getTableName() + " SET "
//                    + "db_version=db_version+1, status=? " + "WHERE " + "mitteilungIdent=? ";
//
//            PreparedStatement lPStm = verbindung.prepareStatement(lSql);
//            lPStm.setInt(1, pStatus);
//            lPStm.setInt(2, pIdent);
//
//            int ergebnis = lPStm.executeUpdate();
//            lPStm.close();
//            if (ergebnis == 0) {
//                return (CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert);
//            }
//        } catch (Exception e1) {
//            CaBug.drucke("001");
//            System.err.println(" " + e1.getMessage());
//            return (-1);
//        }
//
//        return (1);
//    }

    
    /**Multiuserfähig
     * Hier werden auch die unterschiedlichen Version in versionen alle gleich upgedated. Für alle ohne Botschaften irrelevant,
     * da nur Sätze mit version==0 existieren.
     * Für alle anderen gilt: Status muß ja in allen gleich aktualisiert werden.
     * */
    public int update_statusZurueckgezogen(int pIdent, String pZeitpunkt, long pVersion) {
        int ergebnis=0;

        try {

            String lSql = "UPDATE " + getSchema() + getTableName()+" SET "
                    + "db_version=db_version+1, status=?, zeitpunktDesRueckzugs=? " + "WHERE " + "mitteilungIdent=? AND db_version=? ";

            PreparedStatement lPStm = verbindung.prepareStatement(lSql);
            lPStm.setInt(1, KonstMitteilungStatus.ZURUECKGEZOGEN);
            lPStm.setString(2, pZeitpunkt);
           lPStm.setInt(3, pIdent);
           lPStm.setLong(4, pVersion);

           dbBasis.beginTransactionNeu();
            ergebnis = updateIntern(lPStm);
            dbBasis.endTransactionNeu();
          } catch (Exception e1) {
            CaBug.drucke("001");
            System.err.println(" " + e1.getMessage());
            return (-1);
        }

        return (ergebnis);
    }

    /**Schlägt immer! Auch wenn bereits von anderem geändert wurde.
     * Hier werden auch die unterschiedlichen Version in versionen alle gleich upgedated. Für alle ohne Botschaften irrelevant,
     * da nur Sätze mit version==0 existieren.
     * Für alle anderen gilt: Status muß ja in allen gleich aktualisiert werden.
     * */
    public int update_interneVerarbeitungLaufend(String pDateinameIntern, int pInterneVerarbeitungLaufend) {
        int ergebnis=0;

        try {

            String lSql = "UPDATE " + getSchema() + getTableName()+" SET "
                    + "db_version=db_version+1, interneVerarbeitungLaufend=? " + "WHERE " + "internerDateiname=? ";

            PreparedStatement lPStm = verbindung.prepareStatement(lSql);
            lPStm.setInt(1, pInterneVerarbeitungLaufend);
            lPStm.setString(2, pDateinameIntern);

            dbBasis.beginTransactionNeu();
           ergebnis =  updateIntern(lPStm);
            dbBasis.endTransactionNeu();
          } catch (Exception e1) {
            CaBug.drucke("001");
            System.err.println(" " + e1.getMessage());
            return (-1);
        }

        return (ergebnis);
    }

    /**Schlägt immer! Auch wenn bereits von anderem geändert wurde.
     * Hier werden auch die unterschiedlichen Version in versionen alle gleich upgedated. Für alle ohne Botschaften irrelevant,
     * da nur Sätze mit version==0 existieren.
     * Für alle anderen gilt: Status muß ja in allen gleich aktualisiert werden.
     * */
    public int update_interneVerarbeitungLaufend(String pDateinameIntern, int pVersion, int pInterneVerarbeitungLaufend) {
        int ergebnis=0;

        try {

            String lSql = "UPDATE " + getSchema() + getTableName()+" SET "
                    + "db_version=db_version+1, interneVerarbeitungLaufend=? " + "WHERE " + "internerDateiname=? AND version=? ";

            PreparedStatement lPStm = verbindung.prepareStatement(lSql);
            lPStm.setInt(1, pInterneVerarbeitungLaufend);
            lPStm.setString(2, pDateinameIntern);
            lPStm.setInt(3, pVersion);

            dbBasis.beginTransactionNeu();
           ergebnis =  updateIntern(lPStm);
           dbBasis.endTransactionNeu();
          } catch (Exception e1) {
            CaBug.drucke("001");
            System.err.println(" " + e1.getMessage());
            return (-1);
        }

        return (ergebnis);
    }

//    /**Multiuserfähig
//     * Hier werden auch die unterschiedlichen Version in versionen alle gleich upgedated. Für alle ohne Botschaften irrelevant,
//     * da nur Sätze mit version==0 existieren.
//     * Für alle anderen gilt: Status muß ja in allen gleich aktualisiert werden.
//     * */
//    public int update_nichtErreichtAnsEnde(int pIdent, long pDbVersion) {
//
//        PreparedStatement lPStm = null;
//        int ergebnis = 0;
//        int maxWert = 0;
//
//        String lSql = "SELECT MAX(lfdNrInListe) " + "FROM " + dbBundle.getSchemaMandant() + getTableName();
//
//        try {
//            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
//
//            ResultSet lErgebnis = lPStm.executeQuery();
//            lErgebnis.last();
//            lErgebnis.beforeFirst();
//
//            while (lErgebnis.next() == true) {
//                ergebnis = lErgebnis.getInt(1);
//            }
//            maxWert = ergebnis;
//            lErgebnis.close();
//            lPStm.close();
//
//        } catch (SQLException e) {
//            CaBug.drucke("003");
//            System.err.println(" " + e.getMessage());
//            return (-1);
//        }
//
//        try {
//
//            lSql = "UPDATE " + dbBundle.getSchemaMandant() + getTableName()+ " SET "
//                    + "lfdNrInListe=?, status=2 WHERE mitteilungIdent=?  AND db_version=? ";
//
//            lPStm = verbindung.prepareStatement(lSql);
//            lPStm.setInt(1, maxWert + 1);
//            lPStm.setInt(2, pIdent);
//            lPStm.setLong(3, pDbVersion);
//
//            ergebnis = lPStm.executeUpdate();
//            lPStm.close();
//            if (ergebnis == 0) {
//                return (CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert);
//            }
//        } catch (Exception e1) {
//            CaBug.drucke("001");
//            System.err.println(" " + e1.getMessage());
//            return (-1);
//        }
//
//        return (1);
//    }

//    /**Multiuserfähig
//     * Hier werden auch die unterschiedlichen Version in versionen alle gleich upgedated. Für alle ohne Botschaften irrelevant,
//     * da nur Sätze mit version==0 existieren.
//     * Für alle anderen gilt: Status muß ja in allen gleich aktualisiert werden.
//     * */
//    public int update_statusGesprochen(int pIdent, long pDbVersion) {
//
//        try {
//
//            String lSql = "UPDATE " + dbBundle.getSchemaMandant() + getTableName()+ " SET "
//                    + "db_version=db_version+1, status=5 " + "WHERE " + "mitteilungIdent=? AND db_version=? ";
//
//            PreparedStatement lPStm = verbindung.prepareStatement(lSql);
//            lPStm.setInt(1, pIdent);
//            lPStm.setLong(2, pDbVersion);
//
//            int ergebnis = lPStm.executeUpdate();
//            lPStm.close();
//            if (ergebnis == 0) {
//                return (CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert);
//            }
//        } catch (Exception e1) {
//            CaBug.drucke("001");
//            System.err.println(" " + e1.getMessage());
//            return (-1);
//        }
//
//        return (1);
//    }

    
//    /**Multiuserfähig
//     * Hier werden auch die unterschiedlichen Version in versionen alle gleich upgedated. Für alle ohne Botschaften irrelevant,
//     * da nur Sätze mit version==0 existieren.
//     * Für alle anderen gilt: Status muß ja in allen gleich aktualisiert werden.
//     * */
//    public int update_statusNichtErreichtErledigt(int pIdent, long pDbVersion) {
//
//        try {
//
//            String lSql = "UPDATE " + dbBundle.getSchemaMandant() + getTableName()+ " SET "
//                    + "db_version=db_version+1, status=4 " + "WHERE " + "mitteilungIdent=?  AND db_version=? ";
//
//            PreparedStatement lPStm = verbindung.prepareStatement(lSql);
//            lPStm.setInt(1, pIdent);
//            lPStm.setLong(2, pDbVersion);
//
//            int ergebnis = lPStm.executeUpdate();
//            lPStm.close();
//            if (ergebnis == 0) {
//                return (CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert);
//            }
//        } catch (Exception e1) {
//            CaBug.drucke("001");
//            System.err.println(" " + e1.getMessage());
//            return (-1);
//        }
//
//        return (1);
//    }

    /**Multiuserfähig
     * Darf für Botschaften nicht verwendet werden!
     * */
    public int updateUndAnsEnde(EclMitteilung pEcl) {

        initFelder();

        CaBug.druckeLog("", logDrucken, 10);
        PreparedStatement lPStm = null;
        int ergebnis = 0;
        int maxWert = 0;

        String lSql = "SELECT MAX(lfdNrInListe) " + "FROM " + dbBundle.getSchemaMandant() + getTableName();
        CaBug.druckeLog("1", logDrucken, 10);

        try {
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            ResultSet lErgebnis = lPStm.executeQuery();
            lErgebnis.last();
            lErgebnis.beforeFirst();

            while (lErgebnis.next() == true) {
                ergebnis = lErgebnis.getInt(1);
            }
            maxWert = ergebnis;
            lErgebnis.close();
            lPStm.close();

        } catch (SQLException e) {
            CaBug.drucke("003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        CaBug.druckeLog("2", logDrucken, 10);

        pEcl.db_version++;
        pEcl.lfdNrInListe = maxWert + 1;

        try {
            CaBug.druckeLog("3", logDrucken, 10);

            lSql = setzeUpdateBasisStringZusammen(felder)
                    + " WHERE "
                    + "mitteilungIdent=? AND db_version=? ";
            CaBug.druckeLog("nach lSql="+lSql, logDrucken, 10);
//            lSql = "UPDATE " + dbBundle.getSchemaMandant() + getTableName()+" SET " + "ident=?, db_version=?, "
//                    + "loginKennungIdent=?, melderIdentText=?, "
//                    + "wortmelder=?, telefonNr=?, zuTop=?, wortmeldungtext=?, zeitpunktDerWortmeldung=?, drucklaufNr=?, "
//                    + "status=?, lfdNrInRednerliste=? " + "WHERE " + "mitteilungIdent=? AND db_version=? ";

            lPStm = verbindung.prepareStatement(lSql);
            fuellePreparedStatementKomplett(lPStm, 1, pEcl);
            lPStm.setInt(getAnzFelder() + 1, pEcl.mitteilungIdent);
            lPStm.setLong(getAnzFelder() + 2, pEcl.db_version - 1);

            dbBasis.beginTransactionNeu();
            ergebnis = updateIntern(lPStm);
            dbBasis.endTransactionNeu();
            if (ergebnis == 0) {
                return (CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert);
            }
        } catch (Exception e1) {
            CaBug.drucke("001");
            System.err.println(" " + e1.getMessage());
            return (-1);
        }

        return (ergebnis);
    }
    
    
    public int updateNeuerDrucklauf(int pDrucklaufnr) {

        int ergebnis = 0;
        try {

            String lSql = "UPDATE " + getSchema() + getTableName()+" SET drucklaufNr=? "
                    + "WHERE drucklaufNr=0 ";

            PreparedStatement lPStm = verbindung.prepareStatement(lSql);
            lPStm.setInt(1, pDrucklaufnr);
            dbBasis.beginTransactionNeu();
            ergebnis = updateIntern(lPStm);
            dbBasis.endTransactionNeu();

        } catch (Exception e1) {
            CaBug.drucke("001");
            System.err.println(" " + e1.getMessage());
            return (-1);
        }

        return ergebnis;
    }

    /**Return-Werte:
     * pfXyWurdeVonAnderemBenutzerVeraendert
     * -1 => undefinierter Fehler
     * 1 => Löschen erfolgreich
     */
    public int delete(int pIdent) {
        int ergebnis=0;
        try {
            String sql = "DELETE FROM " + getSchema()+getTableName()+
                    " WHERE mitteilungIdent=? ";
            PreparedStatement pstm1 = verbindung.prepareStatement(sql);
            pstm1.setInt(1, pIdent);
            ergebnis = deleteIntern(pstm1);
        } catch (Exception e1) {
            CaBug.drucke("001");
            System.err.println(" " + e1.getMessage());
            return (-1);
        }

        return ergebnis;
    }



}
