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
import java.util.ArrayList;
import java.util.List;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComEntities.EclInhaberImportAnmeldedaten;

public class DbInhaberImportAnmeldedaten {

    private Connection verbindung = null;
    private DbBundle dbBundle = null;

    final private int anzFelder = 261;

    /************************* Initialisierung ***************************/
    public DbInhaberImportAnmeldedaten(DbBundle pDbBundle) {
        /* Verbindung in lokale Daten eintragen */
        if (pDbBundle == null) {
            CaBug.drucke("DbInhaberImportAnmeldedaten.init 001 - dbBundle nicht initialisiert");
            return;
        }
        if (pDbBundle.dbBasis == null) {
            CaBug.drucke("DbInhaberImportAnmeldedaten.init 002 - dbBasis nicht initialisiert");
            return;
        }

        verbindung = pDbBundle.dbBasis.verbindung;
        dbBundle = pDbBundle;
    }

    /************ Neuanlegen Table ******************************/
    public int createTable() {
        int rc = 0;
        DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);
//		@formatter:off
		String hString =
				"CREATE TABLE IF NOT EXISTS " + dbBundle.getSchemaMandant() + "tbl_InhaberImport_anmeldedaten ( "
				+ "`ident` int(11) AUTO_INCREMENT NOT NULL, " 
				+ "`eKNr` int(11) NOT NULL, "
				+ "`pruefKennz` varchar(1) NOT NULL, "
				+ "`anredeKzf` int(11) NOT NULL, "
				+ "`fAdrNr` int(11) NOT NULL, "
				+ "`akadGrad` varchar(30) NOT NULL, "
				+ "`adelstitel1` varchar(30) NOT NULL, "
				+ "`adelstitel2` varchar(10) NOT NULL, "
				+ "`vorname` varchar(50) NOT NULL, "
				+ "`nachname` varchar(80) NOT NULL, "
				+ "`anmeldung` varchar(45) NOT NULL, "
				+ "`adresse` varchar(255) NOT NULL, "
				+ "`strasse` varchar(30) NOT NULL, "
				+ "`adresszusatz` varchar(80) NOT NULL, "
				+ "`plz` varchar(10) NOT NULL, "
				+ "`ort` varchar(80) NOT NULL, "
				+ "`version` varchar(3) NOT NULL, "
				+ "`landKzf` varchar(3) NOT NULL, "
				+ "`land` varchar(20) NOT NULL, "
				+ "`vertreterKzf` varchar(1) NOT NULL, "
				+ "`stueck` int(11) NOT NULL, "
				+ "`nominal` int(11) NOT NULL, "
				+ "`stimmenanteil` int(11) NOT NULL, "
				+ "`besitzMm` varchar(1) NOT NULL, "
				+ "`aktiengattung` varchar(1) NOT NULL, "
				+ "`aRVS` varchar(1) NOT NULL, "
				+ "`statistikMm` varchar(2) NOT NULL, "
				+ "`depotNr` varchar(20) NOT NULL, "
				+ "`wKN` varchar(6) NOT NULL, "
				+ "`nennwert` int(11) NOT NULL, "
				+ "`versandKzf` varchar(1) NOT NULL, "
				+ "`satzart` varchar(2) NOT NULL, "
				+ "`erfDat` varchar(8) NOT NULL, "
				+ "`erfBea` varchar(3) NOT NULL, "
				+ "`druckBea` varchar(3) NOT NULL, "
				+ "`weitergabeNr1` int(11) NOT NULL, "
				+ "`weitergabeNr2` int(11) NOT NULL, "
				+ "`druckKzf` varchar(1) NOT NULL, "
				+ "`teilnahmeart` varchar(1) NOT NULL, "
				+ "`satzKzf` varchar(1) NOT NULL, "
				+ "`hstNr` int(11) NOT NULL, "
				+ "`referenzEKNr` int(11) NOT NULL, "
				+ "`hinweis` varchar(20) NOT NULL, "
				+ "`iSIN` varchar(12) NOT NULL, "
				+ "`internetKzf` varchar(1) NOT NULL, "
				+ "`internetAdr` varchar(100) NOT NULL, "
				+ "`hinweisWeitergabe` varchar(30) NOT NULL, "
				+ "`reserve` varchar(30) NOT NULL, "
				+ "`empfAnrede` varchar(1) NOT NULL, "
				+ "`empfKzf` varchar(1) NOT NULL, "
				+ "`empfNachname` varchar(80) NOT NULL, "
				+ "`empfVorname` varchar(50) NOT NULL, "
				+ "`empfAkadGrad` varchar(30) NOT NULL, "
				+ "`empfAdelstitel` varchar(30) NOT NULL, "
				+ "`empfStrasse` varchar(30) NOT NULL, "
				+ "`empfPLZ` varchar(10) NOT NULL, "
				+ "`empfOrt` varchar(80) NOT NULL, "
				+ "`empfLandKzf` varchar(3) NOT NULL, "
				+ "`gattungId` int(11) NOT NULL, "
				+ "`datei` varchar(80) NOT NULL, "
				+ "`dateikuerzel` varchar(15) NOT NULL, ";
				
		for (int i = 0; i < 200; i++) 
            hString += "`abgabe" + i + "` char(1) DEFAULT NULL, ";
				
		hString += "PRIMARY KEY (`ident`, `referenzEKNr`))";	
		rc = lDbLowLevel.createTable(hString);
//		@formatter:on
        return rc;
    }

    public int checkErgaenzung() {

        int erg = -1;

        String sql = "SHOW COLUMNS FROM " + dbBundle.getSchemaMandant() + "tbl_InhaberImport_anmeldedaten LIKE 'abgabe0'";

        try (PreparedStatement ps = verbindung.prepareStatement(sql)) {

            try (ResultSet rs = ps.executeQuery()) {

                if (!rs.next()) {

                    sql = "ALTER TABLE " + dbBundle.getSchemaMandant() + "tbl_InhaberImport_anmeldedaten "
                            + "MODIFY ort varchar(80) NOT NULL, " + "MODIFY empfOrt varchar(80) NOT NULL, ADD ";
                    for (int i = 0; i < 200; i++)
                        sql += "abgabe" + i + " char(1) DEFAULT NULL" + (i == 199 ? ";" : ", ADD ");

                    erg = ps.executeUpdate(sql);

                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return erg;
        }
        return erg;
    }

    public int updateAdresse() {

        int erg = 0;

        DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);

        String sql = "ALTER TABLE " + dbBundle.getSchemaMandant() + "tbl_InhaberImport_anmeldedaten "
                + "MODIFY anmeldung varchar(80) NOT NULL, ADD adresse varchar(255) NOT NULL AFTER anmeldung, ADD adresszusatz varchar(80) NOT NULL AFTER strasse";

        if (lDbLowLevel.pruefeSpalteVorhanden(dbBundle.getSchemaMandant() + "tbl_InhaberImport_anmeldedaten", "adresse"))
            erg = lDbLowLevel.rawOperation(sql);

        return erg;
    }

    public int deleteAll() {
        DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);
        
        if (lDbLowLevel.checkTableVorhandenNew(dbBundle.getSchemaMandantOhnePunkt(), "tbl_InhaberImport_anmeldedaten")) {
            return lDbLowLevel.truncMandant("TRUNCATE " + dbBundle.getSchemaMandant() + "tbl_InhaberImport_anmeldedaten");
        }
        return 1;
    }

    public int insert(EclInhaberImportAnmeldedaten eclInhaberImportAnmeldedaten) {

        int erg = -1;

        String sql = "INSERT INTO " + dbBundle.getSchemaMandant() + "tbl_InhaberImport_anmeldedaten ("
                + "ident, eKNr, pruefKennz, anredeKzf, fAdrNr, akadGrad, adelstitel1, adelstitel2, vorname, nachname, "
                + "anmeldung, adresse, strasse, adresszusatz, pLZ, ort, version, landKzf, land, vertreterKzf, stueck, nominal, stimmenanteil, "
                + "besitzMm, aktiengattung, aRVS, statistikMm, depotNr, wKN, nennwert, versandKzf, satzart, erfDat, "
                + "erfBea, druckBea, weitergabeNr1, weitergabeNr2, druckKzf, teilnahmeart, satzKzf, hstNr, referenzEKNr, "
                + "hinweis, iSIN, internetKzf, internetAdr, hinweisWeitergabe, reserve, empfAnrede, empfKzf, "
                + "empfNachname, empfVorname, empfAkadGrad, empfAdelstitel, empfStrasse, empfPLZ, empfOrt, "
                + "empfLandKzf, gattungId, datei, dateikuerzel";

        for (int i = 0; i < 200; i++)
            sql += ", abgabe" + i;
        sql += ") VALUES ( ?";
        for (int i = 0; i < anzFelder - 1; i++)
            sql += ", ?";
        sql += ")";

        try (PreparedStatement ps = verbindung.prepareStatement(sql)) {

            ps.setInt(1, 0);
            ps.setInt(2, eclInhaberImportAnmeldedaten.getEkNr());
            ps.setString(3, eclInhaberImportAnmeldedaten.getPruefKennz());
            ps.setInt(4, eclInhaberImportAnmeldedaten.getAnredeKzf());
            ps.setInt(5, eclInhaberImportAnmeldedaten.getFadrNr());
            ps.setString(6, eclInhaberImportAnmeldedaten.getAkadGrad());
            ps.setString(7, eclInhaberImportAnmeldedaten.getAdelstitel1());
            ps.setString(8, eclInhaberImportAnmeldedaten.getAdelstitel2());
            ps.setString(9, eclInhaberImportAnmeldedaten.getVorname());
            ps.setString(10, eclInhaberImportAnmeldedaten.getNachname());
            ps.setString(11, eclInhaberImportAnmeldedaten.getAnmeldung());
            ps.setString(12, eclInhaberImportAnmeldedaten.getAdresse());
            ps.setString(13, eclInhaberImportAnmeldedaten.getStrasse());
            ps.setString(14, eclInhaberImportAnmeldedaten.getAdresszusatz());
            ps.setString(15, eclInhaberImportAnmeldedaten.getPlz());
            ps.setString(16, eclInhaberImportAnmeldedaten.getOrt());
            ps.setString(17, eclInhaberImportAnmeldedaten.getVersion());
            ps.setString(18, eclInhaberImportAnmeldedaten.getLandKzf());
            ps.setString(19, eclInhaberImportAnmeldedaten.getLand());
            ps.setString(20, eclInhaberImportAnmeldedaten.getVertreterKzf());
            ps.setInt(21, eclInhaberImportAnmeldedaten.getStueck());
            ps.setInt(22, eclInhaberImportAnmeldedaten.getNominal());
            ps.setInt(23, eclInhaberImportAnmeldedaten.getStimmenanteil());
            ps.setString(24, eclInhaberImportAnmeldedaten.getBesitzMm());
            ps.setString(25, eclInhaberImportAnmeldedaten.getAktiengattung());
            ps.setString(26, eclInhaberImportAnmeldedaten.getArvs());
            ps.setString(27, eclInhaberImportAnmeldedaten.getStatistikMm());
            ps.setString(28, eclInhaberImportAnmeldedaten.getDepotNr());
            ps.setString(29, eclInhaberImportAnmeldedaten.getWkn());
            ps.setInt(30, eclInhaberImportAnmeldedaten.getNennwert());
            ps.setString(31, eclInhaberImportAnmeldedaten.getVersandKzf());
            ps.setString(32, eclInhaberImportAnmeldedaten.getSatzart());
            ps.setString(33, eclInhaberImportAnmeldedaten.getErfDat());
            ps.setString(34, eclInhaberImportAnmeldedaten.getErfBea());
            ps.setString(35, eclInhaberImportAnmeldedaten.getDruckBea());
            ps.setInt(36, eclInhaberImportAnmeldedaten.getWeitergabeNr1());
            ps.setInt(37, eclInhaberImportAnmeldedaten.getWeitergabeNr2());
            ps.setString(38, eclInhaberImportAnmeldedaten.getDruckKzf());
            ps.setString(39, eclInhaberImportAnmeldedaten.getTeilnahmeart());
            ps.setString(40, eclInhaberImportAnmeldedaten.getSatzKzf());
            ps.setInt(41, eclInhaberImportAnmeldedaten.getHstNr());
            ps.setInt(42, eclInhaberImportAnmeldedaten.getReferenzEKNr());
            ps.setString(43, eclInhaberImportAnmeldedaten.getHinweis());
            ps.setString(44, eclInhaberImportAnmeldedaten.getIsin());
            ps.setString(45, eclInhaberImportAnmeldedaten.getInternetKzf());
            ps.setString(46, eclInhaberImportAnmeldedaten.getInternetAdr());
            ps.setString(47, eclInhaberImportAnmeldedaten.getHinweisWeitergabe());
            ps.setString(48, eclInhaberImportAnmeldedaten.getReserve());
            ps.setString(49, eclInhaberImportAnmeldedaten.getEmpfAnrede());
            ps.setString(50, eclInhaberImportAnmeldedaten.getEmpfKzf());
            ps.setString(51, eclInhaberImportAnmeldedaten.getEmpfNachname());
            ps.setString(52, eclInhaberImportAnmeldedaten.getEmpfVorname());
            ps.setString(53, eclInhaberImportAnmeldedaten.getEmpfAkadGrad());
            ps.setString(54, eclInhaberImportAnmeldedaten.getEmpfAdelstitel());
            ps.setString(55, eclInhaberImportAnmeldedaten.getEmpfStrasse());
            ps.setString(56, eclInhaberImportAnmeldedaten.getEmpfPLZ());
            ps.setString(57, eclInhaberImportAnmeldedaten.getEmpfOrt());
            ps.setString(58, eclInhaberImportAnmeldedaten.getEmpfLandKzf());
            ps.setInt(59, eclInhaberImportAnmeldedaten.getGattungId());
            ps.setString(60, eclInhaberImportAnmeldedaten.getDatei());
            ps.setString(61, eclInhaberImportAnmeldedaten.getDateikuerzel());

            erg = 61;

            for (int i = 0; i < 200; i++)
                ps.setString(++erg, eclInhaberImportAnmeldedaten.getAbgabe()[i]);

            erg = ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            return erg;
        }
        return erg;
    }

    public int update(EclInhaberImportAnmeldedaten eclInhaberImportAnmeldedaten) {

        int erg = 0;

//		@formatter:off
		String sql = "UPDATE " + dbBundle.getSchemaMandant() + "tbl_InhaberImport_anmeldedaten SET "
				+ "pruefKennz=?, anredeKzf=?, fAdrNr=?, akadGrad=?, adelstitel1=?, adelstitel2=?, vorname=?, nachname=?, anmeldung=?, adresse=?, strasse=?, adresszusatz=?, pLZ=?, "
				+ "ort=?, version=?, landKzf=?, land=?, vertreterKzf=?, stueck=?, nominal=?, stimmenanteil=?, besitzMm=?, aktiengattung=?, aRVS=?, statistikMm=?, "
				+ "depotNr=?, wKN=?, nennwert=?, versandKzf=?, satzart=?, erfDat=?, erfBea=?, druckBea=?, weitergabeNr1=?, weitergabeNr2=?, druckKzf=?, teilnahmeart=?, "
				+ "satzKzf=?, hstNr=?, referenzEKNr=?, hinweis=?, iSIN=?, internetKzf=?, internetAdr=?, hinweisWeitergabe=?, empfAnrede=?, empfKzf=?, empfNachname=?, "
				+ "empfVorname=?, empfAkadGrad=?, empfAdelstitel=?, empfStrasse=?, empfPLZ=?, empfOrt=?, empfLandKzf=?, reserve=?, gattungId=?, datei=?, dateikuerzel=? "
				+ "WHERE ident=?";
//		@formatter:on

        try (PreparedStatement ps = verbindung.prepareStatement(sql)) {

            ps.setString(1, eclInhaberImportAnmeldedaten.getPruefKennz());
            ps.setInt(2, eclInhaberImportAnmeldedaten.getAnredeKzf());
            ps.setInt(3, eclInhaberImportAnmeldedaten.getFadrNr());
            ps.setString(4, eclInhaberImportAnmeldedaten.getAkadGrad());
            ps.setString(5, eclInhaberImportAnmeldedaten.getAdelstitel1());
            ps.setString(6, eclInhaberImportAnmeldedaten.getAdelstitel2());
            ps.setString(7, eclInhaberImportAnmeldedaten.getVorname());
            ps.setString(8, eclInhaberImportAnmeldedaten.getNachname());
            ps.setString(9, eclInhaberImportAnmeldedaten.getAnmeldung());
            ps.setString(10, eclInhaberImportAnmeldedaten.getAdresse());
            ps.setString(11, eclInhaberImportAnmeldedaten.getStrasse());
            ps.setString(12, eclInhaberImportAnmeldedaten.getAdresszusatz());
            ps.setString(13, eclInhaberImportAnmeldedaten.getPlz());
            ps.setString(14, eclInhaberImportAnmeldedaten.getOrt());
            ps.setString(15, eclInhaberImportAnmeldedaten.getVersion());
            ps.setString(16, eclInhaberImportAnmeldedaten.getLandKzf());
            ps.setString(17, eclInhaberImportAnmeldedaten.getLand());
            ps.setString(18, eclInhaberImportAnmeldedaten.getVertreterKzf());
            ps.setInt(19, eclInhaberImportAnmeldedaten.getStueck());
            ps.setInt(20, eclInhaberImportAnmeldedaten.getNominal());
            ps.setInt(21, eclInhaberImportAnmeldedaten.getStimmenanteil());
            ps.setString(22, eclInhaberImportAnmeldedaten.getBesitzMm());
            ps.setString(23, eclInhaberImportAnmeldedaten.getAktiengattung());
            ps.setString(24, eclInhaberImportAnmeldedaten.getArvs());
            ps.setString(25, eclInhaberImportAnmeldedaten.getStatistikMm());
            ps.setString(26, eclInhaberImportAnmeldedaten.getDepotNr());
            ps.setString(27, eclInhaberImportAnmeldedaten.getWkn());
            ps.setInt(28, eclInhaberImportAnmeldedaten.getNennwert());
            ps.setString(29, eclInhaberImportAnmeldedaten.getVersandKzf());
            ps.setString(30, eclInhaberImportAnmeldedaten.getSatzart());
            ps.setString(31, eclInhaberImportAnmeldedaten.getErfDat());
            ps.setString(32, eclInhaberImportAnmeldedaten.getErfBea());
            ps.setString(33, eclInhaberImportAnmeldedaten.getDruckBea());
            ps.setInt(34, eclInhaberImportAnmeldedaten.getWeitergabeNr1());
            ps.setInt(35, eclInhaberImportAnmeldedaten.getWeitergabeNr2());
            ps.setString(36, eclInhaberImportAnmeldedaten.getDruckKzf());
            ps.setString(37, eclInhaberImportAnmeldedaten.getTeilnahmeart());
            ps.setString(38, eclInhaberImportAnmeldedaten.getSatzKzf());
            ps.setInt(39, eclInhaberImportAnmeldedaten.getHstNr());
            ps.setInt(40, eclInhaberImportAnmeldedaten.getReferenzEKNr());
            ps.setString(41, eclInhaberImportAnmeldedaten.getHinweis());
            ps.setString(42, eclInhaberImportAnmeldedaten.getIsin());
            ps.setString(43, eclInhaberImportAnmeldedaten.getInternetKzf());
            ps.setString(44, eclInhaberImportAnmeldedaten.getInternetAdr());
            ps.setString(45, eclInhaberImportAnmeldedaten.getHinweisWeitergabe());
            ps.setString(46, eclInhaberImportAnmeldedaten.getReserve());
            ps.setString(47, eclInhaberImportAnmeldedaten.getEmpfAnrede());
            ps.setString(48, eclInhaberImportAnmeldedaten.getEmpfKzf());
            ps.setString(49, eclInhaberImportAnmeldedaten.getEmpfNachname());
            ps.setString(50, eclInhaberImportAnmeldedaten.getEmpfVorname());
            ps.setString(51, eclInhaberImportAnmeldedaten.getEmpfAkadGrad());
            ps.setString(52, eclInhaberImportAnmeldedaten.getEmpfAdelstitel());
            ps.setString(53, eclInhaberImportAnmeldedaten.getEmpfStrasse());
            ps.setString(54, eclInhaberImportAnmeldedaten.getEmpfPLZ());
            ps.setString(55, eclInhaberImportAnmeldedaten.getEmpfOrt());
            ps.setString(56, eclInhaberImportAnmeldedaten.getEmpfLandKzf());
            ps.setInt(57, eclInhaberImportAnmeldedaten.getGattungId());
            ps.setString(58, eclInhaberImportAnmeldedaten.getDatei());
            ps.setString(59, eclInhaberImportAnmeldedaten.getDateikuerzel());
            ps.setInt(60, eclInhaberImportAnmeldedaten.getIdent());

            erg = ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            return erg;
        }
        return erg;
    }

    public int delete(EclInhaberImportAnmeldedaten eclInhaberImportAnmeldedaten) {

        int erg = 0;

        String sql = "DELETE FROM " + dbBundle.getSchemaMandant() + "tbl_InhaberImport_anmeldedaten WHERE ident=?";

        try (PreparedStatement ps = verbindung.prepareStatement(sql)) {

            erg = ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return erg;
    }

    private EclInhaberImportAnmeldedaten createData(ResultSet rs) throws SQLException {

        String[] abgabe = new String[200];
        for (int i = 62; i < anzFelder; i++)
            abgabe[i - 62] = rs.getString(i);

        return new EclInhaberImportAnmeldedaten(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getInt(4), rs.getInt(5),
                rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9), rs.getString(10), rs.getString(11),
                rs.getString(12), rs.getString(13), rs.getString(14), rs.getString(15), rs.getString(16),
                rs.getString(17), rs.getString(18), rs.getString(19), rs.getString(20), rs.getInt(21), rs.getInt(22),
                rs.getInt(23), rs.getString(24), rs.getString(25), rs.getString(26), rs.getString(27), rs.getString(28),
                rs.getString(29), rs.getInt(30), rs.getString(31), rs.getString(32), rs.getString(33), rs.getString(34),
                rs.getString(35), rs.getInt(36), rs.getInt(37), rs.getString(38), rs.getString(39), rs.getString(40),
                rs.getInt(41), rs.getInt(42), rs.getString(43), rs.getString(44), rs.getString(45), rs.getString(46),
                rs.getString(47), rs.getString(48), rs.getString(49), rs.getString(50), rs.getString(51),
                rs.getString(52), rs.getString(53), rs.getString(54), rs.getString(55), rs.getString(56),
                rs.getString(57), rs.getString(58), rs.getInt(59), rs.getString(60), rs.getString(61), abgabe);
    }

    public List<EclInhaberImportAnmeldedaten> readAll() {

        List<EclInhaberImportAnmeldedaten> list = new ArrayList<EclInhaberImportAnmeldedaten>();

        String sql = "SELECT * FROM " + dbBundle.getSchemaMandant() + "tbl_InhaberImport_anmeldedaten";

        try (PreparedStatement ps = verbindung.prepareStatement(sql)) {

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next())
                    list.add(createData(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<EclInhaberImportAnmeldedaten> readByFile(String file) {

        List<EclInhaberImportAnmeldedaten> list = new ArrayList<EclInhaberImportAnmeldedaten>();

        String sql = "SELECT * FROM " + dbBundle.getSchemaMandant() + "tbl_InhaberImport_anmeldedaten WHERE datei=?";

        try (PreparedStatement ps = verbindung.prepareStatement(sql)) {

            ps.setString(1, file.trim());

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next())
                    list.add(createData(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;

    }

    public List<EclInhaberImportAnmeldedaten> readByKuerzel(String kuerzel) {

        List<EclInhaberImportAnmeldedaten> list = new ArrayList<EclInhaberImportAnmeldedaten>();

        String sql = "SELECT * FROM " + dbBundle.getSchemaMandant() + "tbl_InhaberImport_anmeldedaten WHERE dateikuerzel=?";

        try (PreparedStatement ps = verbindung.prepareStatement(sql)) {

            ps.setString(1, kuerzel.trim());

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next())
                    list.add(createData(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;

    }

    /*
     * Evtl später über Protokoll Table abfragen
     */
    public List<String> readFiles() {

        List<String> list = new ArrayList<>();

        String sql = "SELECT DISTINCT datei FROM " + dbBundle.getSchemaMandant()
                + "tbl_InhaberImport_anmeldedaten WHERE ident > 1";

        try (PreparedStatement ps = verbindung.prepareStatement(sql)) {

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next())
                    list.add(rs.getString(1));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;

    }

    /*
     * Nur vorläufige Funktion um Nummernkreise zu simulieren
     */
    public int readHighestEkByGattung(int gattung) {

        int ek = 0;

        String sql = "SELECT MAX(referenzEkNr) FROM " + dbBundle.getSchemaMandant()
                + "tbl_InhaberImport_anmeldedaten WHERE gattungId=?";

        try (PreparedStatement ps = verbindung.prepareStatement(sql)) {

            ps.setInt(1, gattung);

            try (ResultSet lErgebnis = ps.executeQuery()) {

                if (lErgebnis.next())
                    ek = lErgebnis.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return ek;
        }
        return ek;
    }

}
