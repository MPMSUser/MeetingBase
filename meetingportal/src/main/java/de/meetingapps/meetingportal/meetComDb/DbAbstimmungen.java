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
import de.meetingapps.meetingportal.meetComEntities.EclAbstimmung;
import de.meetingapps.meetingportal.meetComEntities.EclAbstimmungZuAbstimmungsblock;
import de.meetingapps.meetingportal.meetComEntities.EclAbstimmungenZuStimmkarte;
import de.meetingapps.meetingportal.meetComKonst.KonstDBAbstimmungen;

public class DbAbstimmungen extends DbRootExecute {

    private Connection verbindung = null;
    private DbBasis dbBasis = null;
    private DbBundle dbBundle = null;

    public EclAbstimmung abstimmungenArray[] = null;

    /*************************Initialisierung***************************/
    /* Verbindung in lokale Daten eintragen*/
    public DbAbstimmungen(DbBundle datenbankbundle) {
        if (datenbankbundle.dbBasis == null) {
            System.err.println("vmcdbBasis nicht initialisiert");
            return;
        }
        dbBasis = datenbankbundle.dbBasis;
        verbindung = datenbankbundle.dbBasis.verbindung;
        dbBundle = datenbankbundle;
    }

    public int anzAbstimmungenGefunden() {
        if (abstimmungenArray == null) {
            return 0;
        }
        return abstimmungenArray.length;
    }

    public EclAbstimmung abstimmungengGefunden(int lfd) {
        return abstimmungenArray[lfd];
    }

    public int createTable() {
        int rc = 0;
        DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);
        //@formatter:off
        rc = lDbLowLevel.createTable("CREATE TABLE " + dbBundle.getSchemaMandant() + "tbl_abstimmungen ( "
                + "`mandant` int(11) NOT NULL, " + "`ident` int(11) NOT NULL, "
                + "`db_version` bigint(20) DEFAULT NULL, " + "`nummer` char(10) DEFAULT NULL, "
                + "`nummerEN` char(10) DEFAULT NULL, " + "`nummerindex` char(10) DEFAULT NULL, "
                + "`nummerindexEN` char(10) DEFAULT NULL, " + "`nummerKey` char(10) DEFAULT NULL, "
                + "`nummerUnterdruecken` int(11) DEFAULT NULL, "
                + "`nummerindexKey` char(10) DEFAULT NULL, " + "`nummerFormular` char(10) DEFAULT NULL, "
                + "`nummerindexFormular` char(10) DEFAULT NULL, " + "`kurzBezeichnung` varchar(160) DEFAULT NULL, "
                + "`kurzBezeichnungEN` varchar(160) DEFAULT NULL, "
                + "`anzeigeBezeichnungKurz` varchar(800) DEFAULT NULL, "
                + "`anzeigeBezeichnungKurzEN` varchar(800) DEFAULT NULL, "
                + "`anzeigeBezeichnungLang` varchar(800) DEFAULT NULL, "
                + "`anzeigeBezeichnungLangEN` varchar(800) DEFAULT NULL, " + "`kandidat` varchar(160) DEFAULT NULL, "
                + "`kandidatEN` varchar(160) DEFAULT NULL, " 
                + "`aktiv` int(11) DEFAULT NULL, "
                + "`aktivPreview` int(11) DEFAULT NULL, "
                + "`aktivWeisungenInPortal` int(11) DEFAULT NULL, " 
                + "`aktivWeisungenAufHV` int(11) DEFAULT NULL, "
                + "`aktivWeisungenSchnittstelle` int(11) DEFAULT NULL, "
                + "`aktivWeisungenAnzeige` int(11) DEFAULT NULL, "
                + "`aktivWeisungenInterneAuswertungen` int(11) DEFAULT NULL, "
                + "`aktivWeisungenExterneAuswertungen` int(11) DEFAULT NULL, "
                + "`aktivWeisungenPflegeIntern` int(11) DEFAULT NULL, "
                + "`aktivAbstimmungInPortal` int(11) DEFAULT NULL, " 
                + "`aktivBeiSRV` int(11) DEFAULT NULL, "
                + "`aktivBeiBriefwahl` int(11) DEFAULT NULL, " 
                + "`aktivBeiKIAVDauer` int(11) DEFAULT NULL, "
                + "`aktivFragen` int(11) DEFAULT NULL, "
                + "`aktivAntraege` int(11) DEFAULT NULL, "
                + "`aktivWidersprueche` int(11) DEFAULT NULL, "
                + "`aktivWortmeldungen` int(11) DEFAULT NULL, "
                + "`aktivSonstMitteilungen` int(11) DEFAULT NULL, "
                + "`aktivBotschaftenEinreichen` int(11) DEFAULT NULL, "
                + "`anzeigePositionIntern` int(11) DEFAULT NULL, "
                + "`anzeigePositionExternWeisungen` int(11) DEFAULT NULL, "
                + "`anzeigePositionExternWeisungenHV` int(11) DEFAULT NULL, " + "`externJa` int(11) DEFAULT NULL, "
                + "`externNein` int(11) DEFAULT NULL, " + "`externEnthaltung` int(11) DEFAULT NULL, "
                + "`externUngueltig` int(11) DEFAULT NULL, " + "`externNichtTeilnahme` int(11) DEFAULT NULL, "
                + "`externSonstiges1` int(11) DEFAULT NULL, " + "`externSonstiges2` int(11) DEFAULT NULL, "
                + "`externSonstiges3` int(11) DEFAULT NULL, " + "`externLoeschen` int(11) DEFAULT NULL, "
                + "`externFrei` int(11) DEFAULT NULL, " + "`externFreiText` varchar(40) DEFAULT NULL, "
                + "`internJa` int(11) DEFAULT NULL, " + "`internNein` int(11) DEFAULT NULL, "
                + "`internEnthaltung` int(11) DEFAULT NULL, " + "`internUngueltig` int(11) DEFAULT NULL, "
                + "`internNichtTeilnahme` int(11) DEFAULT NULL, " + "`internSonstiges1` int(11) DEFAULT NULL, "
                + "`internSonstiges2` int(11) DEFAULT NULL, " + "`internSonstiges3` int(11) DEFAULT NULL, "
                + "`internLoeschen` int(11) DEFAULT NULL, " + "`internFrei` int(11) DEFAULT NULL, "
                + "`internFreiText` varchar(40) DEFAULT NULL, " + "`elStiBloJa` int(11) DEFAULT NULL, "
                + "`elStiBloNein` int(11) DEFAULT NULL, " + "`elStiBloEnthaltung` int(11) DEFAULT NULL, "
                + "`elStiBloUngueltig` int(11) DEFAULT NULL, " + "`elStiBloNichtTeilnahme` int(11) DEFAULT NULL, "
                + "`elStiBloSonstiges1` int(11) DEFAULT NULL, " + "`elStiBloSonstiges2` int(11) DEFAULT NULL, "
                + "`elStiBloSonstiges3` int(11) DEFAULT NULL, " + "`elStiBloLoeschen` int(11) DEFAULT NULL, "
                + "`elStiBloFrei` int(11) DEFAULT NULL, " + "`elStiBloFreiText` varchar(40) DEFAULT NULL, "
                + "`tabletJa` int(11) DEFAULT NULL, " + "`tabletNein` int(11) DEFAULT NULL, "
                + "`tabletEnthaltung` int(11) DEFAULT NULL, " + "`tabletUngueltig` int(11) DEFAULT NULL, "
                + "`tabletNichtTeilnahme` int(11) DEFAULT NULL, " + "`tabletSonstiges1` int(11) DEFAULT NULL, "
                + "`tabletSonstiges2` int(11) DEFAULT NULL, " + "`tabletSonstiges3` int(11) DEFAULT NULL, "
                + "`tabletLoeschen` int(11) DEFAULT NULL, " + "`tabletFrei` int(11) DEFAULT NULL, "
                + "`tabletFreiText` varchar(40) DEFAULT NULL, "
                + "`weisungNichtMarkierteSpeichernAlsSRV` int(11) DEFAULT NULL, "
                + "`weisungNichtMarkierteSpeichernAlsBriefwahl` int(11) DEFAULT NULL, "
                + "`weisungNichtMarkierteSpeichernAlsKIAV` int(11) DEFAULT NULL, "
                + "`weisungNichtMarkierteSpeichernAlsDauer` int(11) DEFAULT NULL, "
                + "`weisungNichtMarkierteSpeichernAlsOrg` int(11) DEFAULT NULL, "
                + "`weisungHVNichtMarkierteSpeichernAls` int(11) DEFAULT NULL, "
                + "`abstimmungNichtMarkierteSpeichernAls` int(11) DEFAULT NULL, "
                + "`identWeisungssatz` int(11) DEFAULT NULL, " + "`vonIdentGesamtweisung` int(11) DEFAULT NULL, "
                + "`stimmausschluss` varchar(13) DEFAULT NULL, " + "`pauschalAusschluss0` bigint(20) DEFAULT NULL, "
                + "`pauschalAusschluss1` bigint(20) DEFAULT NULL, " + "`pauschalAusschluss2` bigint(20) DEFAULT NULL, "
                + "`pauschalAusschluss3` bigint(20) DEFAULT NULL, " + "`pauschalAusschluss4` bigint(20) DEFAULT NULL, "
                + "`pauschalAusschlussJN0` int(11) DEFAULT NULL, " + "`pauschalAusschlussJN1` int(11) DEFAULT NULL, "
                + "`pauschalAusschlussJN2` int(11) DEFAULT NULL, " + "`pauschalAusschlussJN3` int(11) DEFAULT NULL, "
                + "`pauschalAusschlussJN4` int(11) DEFAULT NULL, " + "`gegenantrag` int(11) DEFAULT NULL, "
                + "`gegenantraegeGestellt` int(11) DEFAULT NULL, " + "`ergaenzungsantrag` int(11) DEFAULT NULL, "
                + "`beschlussvorschlagGestelltVon` int(11) DEFAULT NULL, "
                + "`beschlussvorschlagGestelltVonSonstige` varchar(200) DEFAULT NULL, "
                + "`zuAbstimmungsgruppe` int(11) DEFAULT NULL, " + "`identErforderlicheMehrheit` int(11) DEFAULT NULL, "
                + "`stimmberechtigteGattungen0` int(11) DEFAULT NULL, "
                + "`stimmberechtigteGattungen1` int(11) DEFAULT NULL, "
                + "`stimmberechtigteGattungen2` int(11) DEFAULT NULL, "
                + "`stimmberechtigteGattungen3` int(11) DEFAULT NULL, "
                + "`stimmberechtigteGattungen4` int(11) DEFAULT NULL, " + "`stimmenAuswerten` int(11) DEFAULT NULL, "
                + "`formularKurz` int(11) DEFAULT NULL, " + "`formularLang` int(11) DEFAULT NULL, "
                + "`formularBuehnenInfo` int(11) DEFAULT NULL, " + "PRIMARY KEY (`mandant`,`ident`) " + ") ");
        //@formatter:on
        return rc;
    }

    /**************************deleteAll Löschen aller Datensätze  eines Mandanten****************/
    public int deleteAll() {
        dbBundle.dbBasis.resetInterneIdentAbstimmung();
        return dbBundle.dbLowLevel
                .deleteMandant("DELETE FROM " + dbBundle.getSchemaMandant() + "tbl_abstimmungen where mandant=?;");
    }

    /**************************setzt aktuelle Mandantennummer bei allen Datensätzen****************/
    public int updateMandant() {
        return dbBundle.dbLowLevel.rawUpdateMandant(dbBundle.getSchemaMandant() + "tbl_abstimmungen");
    }

    public void reorgInterneIdent() {
        int lMax = dbBundle.dbLowLevel.liefereHoechsteIdent(
                "SELECT MAX(ident) FROM " + dbBundle.getSchemaMandant() + "tbl_abstimmungen ab where ab.mandant=? ");
        if (lMax != -1) {
            dbBundle.dbBasis.resetInterneIdentAbstimmung(lMax);
        }
    }

    /** dekodiert die aktuelle Position aus ergebnis in EclAbstimmung und gibt dieses zurück*/
    private EclAbstimmung decodeErgebnis(ResultSet ergebnis) {
        EclAbstimmung lAbstimmung = new EclAbstimmung();

        try {

            lAbstimmung.mandant = ergebnis.getInt("ab.mandant");
            lAbstimmung.ident = ergebnis.getInt("ab.ident");
            lAbstimmung.db_version = ergebnis.getLong("ab.db_version");

            lAbstimmung.nummer = ergebnis.getString("ab.nummer");
            lAbstimmung.nummerEN = ergebnis.getString("ab.nummerEN");
            lAbstimmung.nummerindex = ergebnis.getString("ab.nummerindex");
            lAbstimmung.nummerindexEN = ergebnis.getString("ab.nummerindexEN");
            lAbstimmung.nummerUnterdruecken = ergebnis.getInt("ab.nummerUnterdruecken");
            lAbstimmung.nummerKey = ergebnis.getString("ab.nummerKey");
            if (lAbstimmung.nummerKey == null) {
                lAbstimmung.nummerKey = "";
            }
            lAbstimmung.nummerindexKey = ergebnis.getString("ab.nummerindexKey");
            if (lAbstimmung.nummerindexKey == null) {
                lAbstimmung.nummerindexKey = "";
            }
            lAbstimmung.nummerFormular = ergebnis.getString("ab.nummerFormular");
            if (lAbstimmung.nummerFormular == null) {
                lAbstimmung.nummerFormular = "";
            }
            lAbstimmung.nummerindexFormular = ergebnis.getString("ab.nummerindexFormular");
            if (lAbstimmung.nummerindexFormular == null) {
                lAbstimmung.nummerindexFormular = "";
            }

            lAbstimmung.kurzBezeichnung = ergebnis.getString("ab.kurzBezeichnung");
            lAbstimmung.kurzBezeichnungEN = ergebnis.getString("ab.kurzBezeichnungEN");
            lAbstimmung.anzeigeBezeichnungKurz = ergebnis.getString("ab.anzeigeBezeichnungKurz");
            lAbstimmung.anzeigeBezeichnungKurzEN = ergebnis.getString("ab.anzeigeBezeichnungKurzEN");
            lAbstimmung.anzeigeBezeichnungLang = ergebnis.getString("ab.anzeigeBezeichnungLang");
            lAbstimmung.anzeigeBezeichnungLangEN = ergebnis.getString("ab.anzeigeBezeichnungLangEN");
            lAbstimmung.kandidat = ergebnis.getString("ab.kandidat");
            if (lAbstimmung.kandidat == null) {
                lAbstimmung.kandidat = "";
            }
            lAbstimmung.kandidatEN = ergebnis.getString("ab.kandidatEN");
            if (lAbstimmung.kandidatEN == null) {
                lAbstimmung.kandidatEN = "";
            }

            lAbstimmung.aktiv = ergebnis.getInt("ab.aktiv");
            lAbstimmung.aktivPreview = ergebnis.getInt("ab.aktivPreview");
            lAbstimmung.aktivWeisungenInPortal = ergebnis.getInt("ab.aktivWeisungenInPortal");
            lAbstimmung.aktivWeisungenAufHV = ergebnis.getInt("ab.aktivWeisungenAufHV");
            lAbstimmung.aktivWeisungenSchnittstelle = ergebnis.getInt("ab.aktivWeisungenSchnittstelle");
            lAbstimmung.aktivWeisungenAnzeige = ergebnis.getInt("ab.aktivWeisungenAnzeige");
            lAbstimmung.aktivWeisungenInterneAuswertungen = ergebnis.getInt("ab.aktivWeisungenInterneAuswertungen");
            lAbstimmung.aktivWeisungenExterneAuswertungen = ergebnis.getInt("ab.aktivWeisungenExterneAuswertungen");
            lAbstimmung.aktivWeisungenPflegeIntern = ergebnis.getInt("ab.aktivWeisungenPflegeIntern");
            lAbstimmung.aktivAbstimmungInPortal = ergebnis.getInt("ab.aktivAbstimmungInPortal");
            lAbstimmung.aktivBeiSRV = ergebnis.getInt("ab.aktivBeiSRV");
            lAbstimmung.aktivBeiBriefwahl = ergebnis.getInt("ab.aktivBeiBriefwahl");
            lAbstimmung.aktivBeiKIAVDauer = ergebnis.getInt("ab.aktivBeiKIAVDauer");
            lAbstimmung.aktivFragen = ergebnis.getInt("ab.aktivFragen");
            lAbstimmung.aktivAntraege = ergebnis.getInt("ab.aktivAntraege");
            lAbstimmung.aktivWidersprueche = ergebnis.getInt("ab.aktivWidersprueche");
            lAbstimmung.aktivWortmeldungen = ergebnis.getInt("ab.aktivWortmeldungen");
            lAbstimmung.aktivSonstMitteilungen = ergebnis.getInt("ab.aktivSonstMitteilungen");
            lAbstimmung.aktivBotschaftenEinreichen = ergebnis.getInt("ab.aktivBotschaftenEinreichen");

            lAbstimmung.anzeigePositionIntern = ergebnis.getInt("ab.anzeigePositionIntern");
            lAbstimmung.anzeigePositionExternWeisungen = ergebnis.getInt("ab.anzeigePositionExternWeisungen");
            lAbstimmung.anzeigePositionExternWeisungenHV = ergebnis.getInt("ab.anzeigePositionExternWeisungenHV");

            lAbstimmung.externJa = ergebnis.getInt("ab.externJa");
            lAbstimmung.externNein = ergebnis.getInt("ab.externNein");
            lAbstimmung.externEnthaltung = ergebnis.getInt("ab.externEnthaltung");
            lAbstimmung.externUngueltig = ergebnis.getInt("ab.externUngueltig");
            lAbstimmung.externNichtTeilnahme = ergebnis.getInt("ab.externNichtTeilnahme");
            lAbstimmung.externSonstiges1 = ergebnis.getInt("ab.externSonstiges1");
            lAbstimmung.externSonstiges2 = ergebnis.getInt("ab.externSonstiges2");
            lAbstimmung.externSonstiges3 = ergebnis.getInt("ab.externSonstiges3");
            lAbstimmung.externLoeschen = ergebnis.getInt("ab.externLoeschen");
            lAbstimmung.externFrei = ergebnis.getInt("ab.externFrei");
            lAbstimmung.externFreiText = ergebnis.getString("ab.externFreiText");

            lAbstimmung.internJa = ergebnis.getInt("ab.internJa");
            lAbstimmung.internNein = ergebnis.getInt("ab.internNein");
            lAbstimmung.internEnthaltung = ergebnis.getInt("ab.internEnthaltung");
            lAbstimmung.internUngueltig = ergebnis.getInt("ab.internUngueltig");
            lAbstimmung.internNichtTeilnahme = ergebnis.getInt("ab.internNichtTeilnahme");
            lAbstimmung.internSonstiges1 = ergebnis.getInt("ab.internSonstiges1");
            lAbstimmung.internSonstiges2 = ergebnis.getInt("ab.internSonstiges2");
            lAbstimmung.internSonstiges3 = ergebnis.getInt("ab.internSonstiges3");
            lAbstimmung.internLoeschen = ergebnis.getInt("ab.internLoeschen");
            lAbstimmung.internFrei = ergebnis.getInt("ab.internFrei");
            lAbstimmung.internFreiText = ergebnis.getString("ab.internFreiText");

            lAbstimmung.elStiBloJa = ergebnis.getInt("ab.elStiBloJa");
            lAbstimmung.elStiBloNein = ergebnis.getInt("ab.elStiBloNein");
            lAbstimmung.elStiBloEnthaltung = ergebnis.getInt("ab.elStiBloEnthaltung");
            lAbstimmung.elStiBloUngueltig = ergebnis.getInt("ab.elStiBloUngueltig");
            lAbstimmung.elStiBloNichtTeilnahme = ergebnis.getInt("ab.elStiBloNichtTeilnahme");
            lAbstimmung.elStiBloSonstiges1 = ergebnis.getInt("ab.elStiBloSonstiges1");
            lAbstimmung.elStiBloSonstiges2 = ergebnis.getInt("ab.elStiBloSonstiges2");
            lAbstimmung.elStiBloSonstiges3 = ergebnis.getInt("ab.elStiBloSonstiges3");
            lAbstimmung.elStiBloLoeschen = ergebnis.getInt("ab.elStiBloLoeschen");
            lAbstimmung.elStiBloFrei = ergebnis.getInt("ab.elStiBloFrei");
            lAbstimmung.elStiBloFreiText = ergebnis.getString("ab.elStiBloFreiText");

            lAbstimmung.tabletJa = ergebnis.getInt("ab.tabletJa");
            lAbstimmung.tabletNein = ergebnis.getInt("ab.tabletNein");
            lAbstimmung.tabletEnthaltung = ergebnis.getInt("ab.tabletEnthaltung");
            lAbstimmung.tabletUngueltig = ergebnis.getInt("ab.tabletUngueltig");
            lAbstimmung.tabletNichtTeilnahme = ergebnis.getInt("ab.tabletNichtTeilnahme");
            lAbstimmung.tabletSonstiges1 = ergebnis.getInt("ab.tabletSonstiges1");
            lAbstimmung.tabletSonstiges2 = ergebnis.getInt("ab.tabletSonstiges2");
            lAbstimmung.tabletSonstiges3 = ergebnis.getInt("ab.tabletSonstiges3");
            lAbstimmung.tabletLoeschen = ergebnis.getInt("ab.tabletLoeschen");
            lAbstimmung.tabletFrei = ergebnis.getInt("ab.tabletFrei");
            lAbstimmung.tabletFreiText = ergebnis.getString("ab.tabletFreiText");

            lAbstimmung.weisungNichtMarkierteSpeichernAlsSRV = ergebnis
                    .getInt("ab.weisungNichtMarkierteSpeichernAlsSRV");
            lAbstimmung.weisungNichtMarkierteSpeichernAlsBriefwahl = ergebnis
                    .getInt("ab.weisungNichtMarkierteSpeichernAlsBriefwahl");
            lAbstimmung.weisungNichtMarkierteSpeichernAlsKIAV = ergebnis
                    .getInt("ab.weisungNichtMarkierteSpeichernAlsKIAV");
            lAbstimmung.weisungNichtMarkierteSpeichernAlsDauer = ergebnis
                    .getInt("ab.weisungNichtMarkierteSpeichernAlsDauer");
            lAbstimmung.weisungNichtMarkierteSpeichernAlsOrg = ergebnis
                    .getInt("ab.weisungNichtMarkierteSpeichernAlsOrg");

            lAbstimmung.weisungHVNichtMarkierteSpeichernAls = ergebnis.getInt("ab.weisungHVNichtMarkierteSpeichernAls");

            lAbstimmung.abstimmungNichtMarkierteSpeichernAls = ergebnis
                    .getInt("ab.abstimmungNichtMarkierteSpeichernAls");

            lAbstimmung.identWeisungssatz = ergebnis.getInt("ab.identWeisungssatz");
            lAbstimmung.vonIdentGesamtweisung = ergebnis.getInt("ab.vonIdentGesamtweisung");

            lAbstimmung.stimmausschluss = ergebnis.getString("ab.stimmausschluss");

            lAbstimmung.pauschalAusschluss[0] = ergebnis.getLong("ab.pauschalAusschluss0");
            lAbstimmung.pauschalAusschluss[1] = ergebnis.getLong("ab.pauschalAusschluss1");
            lAbstimmung.pauschalAusschluss[2] = ergebnis.getLong("ab.pauschalAusschluss2");
            lAbstimmung.pauschalAusschluss[3] = ergebnis.getLong("ab.pauschalAusschluss3");
            lAbstimmung.pauschalAusschluss[4] = ergebnis.getLong("ab.pauschalAusschluss4");

            lAbstimmung.pauschalAusschlussJN[0] = ergebnis.getInt("ab.pauschalAusschlussJN0");
            lAbstimmung.pauschalAusschlussJN[1] = ergebnis.getInt("ab.pauschalAusschlussJN1");
            lAbstimmung.pauschalAusschlussJN[2] = ergebnis.getInt("ab.pauschalAusschlussJN2");
            lAbstimmung.pauschalAusschlussJN[3] = ergebnis.getInt("ab.pauschalAusschlussJN3");
            lAbstimmung.pauschalAusschlussJN[4] = ergebnis.getInt("ab.pauschalAusschlussJN4");

            lAbstimmung.gegenantrag = ergebnis.getInt("ab.gegenantrag");
            lAbstimmung.gegenantraegeGestellt = ergebnis.getInt("ab.gegenantraegeGestellt");
            lAbstimmung.ergaenzungsantrag = ergebnis.getInt("ab.ergaenzungsantrag");

            lAbstimmung.beschlussvorschlagGestelltVon = ergebnis.getInt("ab.beschlussvorschlagGestelltVon");
            lAbstimmung.beschlussvorschlagGestelltVonSonstige = ergebnis
                    .getString("ab.beschlussvorschlagGestelltVonSonstige");
            if (lAbstimmung.beschlussvorschlagGestelltVonSonstige == null) {
                lAbstimmung.beschlussvorschlagGestelltVonSonstige = "";
            }

            lAbstimmung.zuAbstimmungsgruppe = ergebnis.getInt("ab.zuAbstimmungsgruppe");
            lAbstimmung.identErforderlicheMehrheit = ergebnis.getInt("ab.identErforderlicheMehrheit");

            lAbstimmung.stimmberechtigteGattungen[0] = ergebnis.getInt("ab.stimmberechtigteGattungen0");
            lAbstimmung.stimmberechtigteGattungen[1] = ergebnis.getInt("ab.stimmberechtigteGattungen1");
            lAbstimmung.stimmberechtigteGattungen[2] = ergebnis.getInt("ab.stimmberechtigteGattungen2");
            lAbstimmung.stimmberechtigteGattungen[3] = ergebnis.getInt("ab.stimmberechtigteGattungen3");
            lAbstimmung.stimmberechtigteGattungen[4] = ergebnis.getInt("ab.stimmberechtigteGattungen4");

            lAbstimmung.stimmenAuswerten = ergebnis.getInt("ab.stimmenAuswerten");
            lAbstimmung.formularKurz = ergebnis.getInt("ab.formularKurz");
            lAbstimmung.formularLang = ergebnis.getInt("ab.formularLang");
            lAbstimmung.formularBuehnenInfo = ergebnis.getInt("ab.formularBuehnenInfo");

        } catch (Exception e) {
            CaBug.drucke("DbAbstimmungen.decodeErgebnis 001");
            System.err.println(" " + e.getMessage());
        }

        return lAbstimmung;
    }

    /** Fuellen Prepared Statement mit allen Feldern, beginnend bei offset.
     * Kann sowohl für Insert, als auch für update verwendet werden.
     * 
     * offset= Startposition des ersten Feldes (also z.B. 1)
     */
    private int anzfelder = 122;

    private void fuellePreparedStatementKomplett(PreparedStatement pstm, int offset, EclAbstimmung lAbstimmung) {

        try {
            pstm.setInt(offset, lAbstimmung.mandant);
            offset++;
            pstm.setInt(offset, lAbstimmung.ident);
            offset++;
            pstm.setLong(offset, lAbstimmung.db_version);
            offset++;

            pstm.setString(offset, lAbstimmung.nummer);
            offset++;
            pstm.setString(offset, lAbstimmung.nummerEN);
            offset++;
            pstm.setString(offset, lAbstimmung.nummerindex);
            offset++;
            pstm.setString(offset, lAbstimmung.nummerindexEN);
            offset++;
            pstm.setInt(offset, lAbstimmung.nummerUnterdruecken);
            offset++;
            pstm.setString(offset, lAbstimmung.nummerKey);
            offset++;
            pstm.setString(offset, lAbstimmung.nummerindexKey);
            offset++;
            pstm.setString(offset, lAbstimmung.nummerFormular);
            offset++;
            pstm.setString(offset, lAbstimmung.nummerindexFormular);
            offset++;

            pstm.setString(offset, lAbstimmung.kurzBezeichnung);
            offset++;
            pstm.setString(offset, lAbstimmung.kurzBezeichnungEN);
            offset++;
            pstm.setString(offset, lAbstimmung.anzeigeBezeichnungKurz);
            offset++;
            pstm.setString(offset, lAbstimmung.anzeigeBezeichnungKurzEN);
            offset++;
            pstm.setString(offset, lAbstimmung.anzeigeBezeichnungLang);
            offset++;
            pstm.setString(offset, lAbstimmung.anzeigeBezeichnungLangEN);
            offset++;
            pstm.setString(offset, lAbstimmung.kandidat);
            offset++;
            pstm.setString(offset, lAbstimmung.kandidatEN);
            offset++;

            pstm.setInt(offset, lAbstimmung.aktiv);
            offset++;
            pstm.setInt(offset, lAbstimmung.aktivPreview);
            offset++;
            pstm.setInt(offset, lAbstimmung.aktivWeisungenInPortal);
            offset++;
            pstm.setInt(offset, lAbstimmung.aktivWeisungenAufHV);
            offset++;
            pstm.setInt(offset, lAbstimmung.aktivWeisungenSchnittstelle);
            offset++;
            pstm.setInt(offset, lAbstimmung.aktivWeisungenAnzeige);
            offset++;
            pstm.setInt(offset, lAbstimmung.aktivWeisungenInterneAuswertungen);
            offset++;
            pstm.setInt(offset, lAbstimmung.aktivWeisungenExterneAuswertungen);
            offset++;
            pstm.setInt(offset, lAbstimmung.aktivWeisungenPflegeIntern);
            offset++;
            pstm.setInt(offset, lAbstimmung.aktivAbstimmungInPortal);
            offset++;
            pstm.setInt(offset, lAbstimmung.aktivBeiSRV);
            offset++;
            pstm.setInt(offset, lAbstimmung.aktivBeiBriefwahl);
            offset++;
            pstm.setInt(offset, lAbstimmung.aktivBeiKIAVDauer);
            offset++;
            pstm.setInt(offset, lAbstimmung.aktivFragen);
            offset++;
            pstm.setInt(offset, lAbstimmung.aktivAntraege);
            offset++;
            pstm.setInt(offset, lAbstimmung.aktivWidersprueche);
            offset++;
            pstm.setInt(offset, lAbstimmung.aktivWortmeldungen);
            offset++;
            pstm.setInt(offset, lAbstimmung.aktivSonstMitteilungen);
            offset++;
            pstm.setInt(offset, lAbstimmung.aktivBotschaftenEinreichen);
            offset++;

            pstm.setInt(offset, lAbstimmung.anzeigePositionIntern);
            offset++;
            pstm.setInt(offset, lAbstimmung.anzeigePositionExternWeisungen);
            offset++;
            pstm.setInt(offset, lAbstimmung.anzeigePositionExternWeisungenHV);
            offset++;

            pstm.setInt(offset, lAbstimmung.externJa);
            offset++;
            pstm.setInt(offset, lAbstimmung.externNein);
            offset++;
            pstm.setInt(offset, lAbstimmung.externEnthaltung);
            offset++;
            pstm.setInt(offset, lAbstimmung.externUngueltig);
            offset++;
            pstm.setInt(offset, lAbstimmung.externNichtTeilnahme);
            offset++;
            pstm.setInt(offset, lAbstimmung.externSonstiges1);
            offset++;
            pstm.setInt(offset, lAbstimmung.externSonstiges2);
            offset++;
            pstm.setInt(offset, lAbstimmung.externSonstiges3);
            offset++;
            pstm.setInt(offset, lAbstimmung.externLoeschen);
            offset++;
            pstm.setInt(offset, lAbstimmung.externFrei);
            offset++;
            pstm.setString(offset, lAbstimmung.externFreiText);
            offset++;

            pstm.setInt(offset, lAbstimmung.internJa);
            offset++;
            pstm.setInt(offset, lAbstimmung.internNein);
            offset++;
            pstm.setInt(offset, lAbstimmung.internEnthaltung);
            offset++;
            pstm.setInt(offset, lAbstimmung.internUngueltig);
            offset++;
            pstm.setInt(offset, lAbstimmung.internNichtTeilnahme);
            offset++;
            pstm.setInt(offset, lAbstimmung.internSonstiges1);
            offset++;
            pstm.setInt(offset, lAbstimmung.internSonstiges2);
            offset++;
            pstm.setInt(offset, lAbstimmung.internSonstiges3);
            offset++;
            pstm.setInt(offset, lAbstimmung.internLoeschen);
            offset++;
            pstm.setInt(offset, lAbstimmung.internFrei);
            offset++;
            pstm.setString(offset, lAbstimmung.internFreiText);
            offset++;

            pstm.setInt(offset, lAbstimmung.elStiBloJa);
            offset++;
            pstm.setInt(offset, lAbstimmung.elStiBloNein);
            offset++;
            pstm.setInt(offset, lAbstimmung.elStiBloEnthaltung);
            offset++;
            pstm.setInt(offset, lAbstimmung.elStiBloUngueltig);
            offset++;
            pstm.setInt(offset, lAbstimmung.elStiBloNichtTeilnahme);
            offset++;
            pstm.setInt(offset, lAbstimmung.elStiBloSonstiges1);
            offset++;
            pstm.setInt(offset, lAbstimmung.elStiBloSonstiges2);
            offset++;
            pstm.setInt(offset, lAbstimmung.elStiBloSonstiges3);
            offset++;
            pstm.setInt(offset, lAbstimmung.elStiBloLoeschen);
            offset++;
            pstm.setInt(offset, lAbstimmung.elStiBloFrei);
            offset++;
            pstm.setString(offset, lAbstimmung.elStiBloFreiText);
            offset++;

            pstm.setInt(offset, lAbstimmung.tabletJa);
            offset++;
            pstm.setInt(offset, lAbstimmung.tabletNein);
            offset++;
            pstm.setInt(offset, lAbstimmung.tabletEnthaltung);
            offset++;
            pstm.setInt(offset, lAbstimmung.tabletUngueltig);
            offset++;
            pstm.setInt(offset, lAbstimmung.tabletNichtTeilnahme);
            offset++;
            pstm.setInt(offset, lAbstimmung.tabletSonstiges1);
            offset++;
            pstm.setInt(offset, lAbstimmung.tabletSonstiges2);
            offset++;
            pstm.setInt(offset, lAbstimmung.tabletSonstiges3);
            offset++;
            pstm.setInt(offset, lAbstimmung.tabletLoeschen);
            offset++;
            pstm.setInt(offset, lAbstimmung.tabletFrei);
            offset++;
            pstm.setString(offset, lAbstimmung.tabletFreiText);
            offset++;

            pstm.setInt(offset, lAbstimmung.weisungNichtMarkierteSpeichernAlsSRV);
            offset++;
            pstm.setInt(offset, lAbstimmung.weisungNichtMarkierteSpeichernAlsBriefwahl);
            offset++;
            pstm.setInt(offset, lAbstimmung.weisungNichtMarkierteSpeichernAlsKIAV);
            offset++;
            pstm.setInt(offset, lAbstimmung.weisungNichtMarkierteSpeichernAlsDauer);
            offset++;
            pstm.setInt(offset, lAbstimmung.weisungNichtMarkierteSpeichernAlsOrg);
            offset++;

            pstm.setInt(offset, lAbstimmung.weisungHVNichtMarkierteSpeichernAls);
            offset++;

            pstm.setInt(offset, lAbstimmung.abstimmungNichtMarkierteSpeichernAls);
            offset++;

            pstm.setInt(offset, lAbstimmung.identWeisungssatz);
            offset++;
            pstm.setInt(offset, lAbstimmung.vonIdentGesamtweisung);
            offset++;

            pstm.setString(offset, lAbstimmung.stimmausschluss);
            offset++;

            pstm.setLong(offset, lAbstimmung.pauschalAusschluss[0]);
            offset++;
            pstm.setLong(offset, lAbstimmung.pauschalAusschluss[1]);
            offset++;
            pstm.setLong(offset, lAbstimmung.pauschalAusschluss[2]);
            offset++;
            pstm.setLong(offset, lAbstimmung.pauschalAusschluss[3]);
            offset++;
            pstm.setLong(offset, lAbstimmung.pauschalAusschluss[4]);
            offset++;

            pstm.setInt(offset, lAbstimmung.pauschalAusschlussJN[0]);
            offset++;
            pstm.setInt(offset, lAbstimmung.pauschalAusschlussJN[1]);
            offset++;
            pstm.setInt(offset, lAbstimmung.pauschalAusschlussJN[2]);
            offset++;
            pstm.setInt(offset, lAbstimmung.pauschalAusschlussJN[3]);
            offset++;
            pstm.setInt(offset, lAbstimmung.pauschalAusschlussJN[4]);
            offset++;

            pstm.setInt(offset, lAbstimmung.gegenantrag);
            offset++;
            pstm.setInt(offset, lAbstimmung.gegenantraegeGestellt);
            offset++;
            pstm.setInt(offset, lAbstimmung.ergaenzungsantrag);
            offset++;

            pstm.setInt(offset, lAbstimmung.beschlussvorschlagGestelltVon);
            offset++;
            pstm.setString(offset, lAbstimmung.beschlussvorschlagGestelltVonSonstige);
            offset++;

            pstm.setInt(offset, lAbstimmung.zuAbstimmungsgruppe);
            offset++;
            pstm.setInt(offset, lAbstimmung.identErforderlicheMehrheit);
            offset++;

            pstm.setInt(offset, lAbstimmung.stimmberechtigteGattungen[0]);
            offset++;
            pstm.setInt(offset, lAbstimmung.stimmberechtigteGattungen[1]);
            offset++;
            pstm.setInt(offset, lAbstimmung.stimmberechtigteGattungen[2]);
            offset++;
            pstm.setInt(offset, lAbstimmung.stimmberechtigteGattungen[3]);
            offset++;
            pstm.setInt(offset, lAbstimmung.stimmberechtigteGattungen[4]);
            offset++;

            pstm.setInt(offset, lAbstimmung.stimmenAuswerten);
            offset++;
            pstm.setInt(offset, lAbstimmung.formularKurz);
            offset++;
            pstm.setInt(offset, lAbstimmung.formularLang);
            offset++;
            pstm.setInt(offset, lAbstimmung.formularBuehnenInfo);
            offset++;

        } catch (SQLException e) {
            CaBug.drucke("DbAbstimmungen.fuellePreparedStatementKomplett 001");
            e.printStackTrace();
        }

    }

    public int insert(EclAbstimmung lAbstimmung) {

        int erg;

        /* Start Transaktion */
        dbBasis.beginTransaction();

        /* neue InterneIdent vergeben */
        erg = dbBasis.getInterneIdentAbstimmung();
        if (erg < 1) {
            CaBug.drucke("DbAbstimmungen.insert 002");
            dbBasis.endTransaction();
            return (erg);
        }

        lAbstimmung.ident = erg;

        lAbstimmung.mandant = dbBundle.clGlobalVar.mandant;

        /* Verarbeitungshinweise: 
         * 	>	nachdem InterneIdent immer eindeutig vergeben werden, ist prinzipiell eine "Doppeleinfügung"
         * 		von InterneIdent nicht möglich
         */

        try {

            /*Felder Neuanlage füllen*/
            //@formatter off
            String sql1 = "INSERT INTO " + dbBundle.getSchemaMandant() + "tbl_abstimmungen " + "("
                    + "mandant, ident, db_version, "
                    + "nummer, nummerEN, nummerindex, nummerindexEN, nummerUnterdruecken, nummerKey, nummerindexKey, nummerFormular, nummerindexFormular, "
                    + "kurzBezeichnung, kurzBezeichnungEN, anzeigeBezeichnungKurz, anzeigeBezeichnungKurzEN, anzeigeBezeichnungLang, anzeigeBezeichnungLangEN, "
                    + "kandidat, kandidatEN, "
                    
                    + "aktiv, aktivPreview, aktivWeisungenInPortal, aktivWeisungenAufHV, aktivWeisungenSchnittstelle, aktivWeisungenAnzeige,  aktivWeisungenInterneAuswertungen, aktivWeisungenExterneAuswertungen, "
                    + "aktivWeisungenPflegeIntern, aktivAbstimmungInPortal, aktivBeiSRV, aktivBeiBriefwahl, aktivBeiKIAVDauer, "
                    + "aktivFragen, aktivAntraege, aktivWidersprueche, aktivWortmeldungen, aktivSonstMitteilungen, aktivBotschaftenEinreichen, "
                    
                    + "anzeigePositionIntern, anzeigePositionExternWeisungen, anzeigePositionExternWeisungenHV, "

                    + "externJa, externNein, externEnthaltung, externUngueltig, externNichtTeilnahme, "
                    + "externSonstiges1, externSonstiges2, externSonstiges3, "
                    + "externLoeschen, externFrei, externFreiText, "

                    + "internJa, internNein, internEnthaltung, internUngueltig, internNichtTeilnahme, "
                    + "internSonstiges1, internSonstiges2, internSonstiges3, "
                    + "internLoeschen, internFrei, internFreiText, "

                    + "elStiBloJa, elStiBloNein, elStiBloEnthaltung, elStiBloUngueltig, elStiBloNichtTeilnahme, "
                    + "elStiBloSonstiges1, elStiBloSonstiges2, elStiBloSonstiges3, "
                    + "elStiBloLoeschen, elStiBloFrei, elStiBloFreiText, "

                    + "tabletJa, tabletNein, tabletEnthaltung, tabletUngueltig, tabletNichtTeilnahme, "
                    + "tabletSonstiges1, tabletSonstiges2, tabletSonstiges3, "
                    + "tabletLoeschen, tabletFrei, tabletFreiText, "

                    + "weisungNichtMarkierteSpeichernAlsSRV, weisungNichtMarkierteSpeichernAlsBriefwahl, weisungNichtMarkierteSpeichernAlsKIAV, weisungNichtMarkierteSpeichernAlsDauer, weisungNichtMarkierteSpeichernAlsOrg, "
                    + "weisungHVNichtMarkierteSpeichernAls, " 
                    + "abstimmungNichtMarkierteSpeichernAls, "

                    + "identWeisungssatz, vonIdentGesamtweisung, stimmausschluss, "
                    + "pauschalAusschluss0, pauschalAusschluss1, pauschalAusschluss2, pauschalAusschluss3, pauschalAusschluss4, "
                    + "pauschalAusschlussJN0, pauschalAusschlussJN1, pauschalAusschlussJN2, pauschalAusschlussJN3, pauschalAusschlussJN4, "
                    + "gegenantrag, gegenantraegeGestellt, ergaenzungsantrag, beschlussvorschlagGestelltVon, beschlussvorschlagGestelltVonSonstige, "

                    + "zuAbstimmungsgruppe, identErforderlicheMehrheit, "
                    + "stimmberechtigteGattungen0, stimmberechtigteGattungen1, stimmberechtigteGattungen2, stimmberechtigteGattungen3, stimmberechtigteGattungen4, "
                    + "stimmenAuswerten, formularKurz, formularLang, formularBuehnenInfo " + ") " 
                    + "VALUES ("
                    + "?, ?, ?, " 
                    + "?, ?, ?, ?, ?, ?, ?, ?, ?, " 
                    + "?, ?, ?, ?, ?, ?, " 
                    + "?, ?, "
                    
                    + "?, ?, ?, ?, ?, ?, ?, ?, " 
                    + "?, ?, ?, ?, ?, " 
                    + "?, ?, ?, ?, ?, ?, " 
                    
                    + "?, ?, ?, "

                    + "?, ?, ?, ?, ?," 
                    + "?, ?, ?, " 
                    + "?, ?, ?, "

                    + "?, ?, ?, ?, ?, " 
                    + "?, ?, ?, " 
                    + "?, ?, ?, "

                    + "?, ?, ?, ?, ?, " + "?, ?, ?, " + "?, ?, ?, "

                    + "?, ?, ?, ?, ?, " + "?, ?, ?, " + "?, ?, ?, "

                    + "?, ?, ?, ?, ?, " + "?, " + "?, "

                    + "?, ?, ?, " + "?, ?, ?, ?, ?," + "?, ?, ?, ?, ?," + "?, ?, ?, ?, ?, "

                    + "?, ?," + "?, ?, ?, ?, ?," + "?, ?, ?, ? " + ")";
            //@formatter on
                       PreparedStatement pstm1 = verbindung.prepareStatement(sql1, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);

            fuellePreparedStatementKomplett(pstm1, 1, lAbstimmung);

            erg = 0; /*Falls Duplicate Key, dann wird exception geschmissen und erg wohl nicht gefüllt!*/
            //			System.out.println("vor Update");
            erg = executeUpdate(pstm1);
            //			System.out.println("nach Update");

            pstm1.close();
        } catch (Exception e2) {
            CaBug.drucke("DbAbstimmungen.insert 001");
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

    /**Für "Abstimmungs-Sicht": Einlesen aller Abstimmungen, die dem sql-Statement genügen,
     * und Ablage in abstimmungenArray.
     */
    private int leseAllgemein(String sql, PreparedStatement pstm1) throws SQLException {
        int anzInArray = 0;

        ResultSet ergebnis;
        ergebnis = executeQuery(pstm1);
        ergebnis.last();
        anzInArray = ergebnis.getRow();
        ergebnis.beforeFirst();

        abstimmungenArray = new EclAbstimmung[anzInArray];

        int i = 0;
        while (ergebnis.next() == true) {
            abstimmungenArray[i] = this.decodeErgebnis(ergebnis);
            i++;
        }
        ergebnis.close();
        pstm1.close();

        return anzInArray;
    }

    /**Einlesen der über lIdent spezifizierten Abstimmung mit Ident lIdent*/
    public int leseIdent(int lIdent) {
        int anzInArray = 0;
        PreparedStatement pstm1 = null;
        try {
            String sql = "SELECT ab.* from " + dbBundle.getSchemaMandant() + "tbl_abstimmungen ab where "
                    + "ab.mandant=? AND " + "ab.ident=?;";

            pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pstm1.setInt(1, dbBundle.clGlobalVar.mandant);
            pstm1.setInt(2, lIdent);

            anzInArray = leseAllgemein(sql, pstm1);
        } catch (Exception e) {
            CaBug.drucke("DbAbstimmungen.leseIdent 001");
            System.err.println(" " + e.getMessage());
        }

        return (anzInArray);
    }

    /**Einlesen aller Abstimmungen, unabhängig von Gattung; primär gedacht für
     * Pflege, sowie Weisungsverarbeitung.
     * 
     * sortierung gemäß KonstDBAbstimmungen
     * Zulässig sind: sortierung_interneIdent, sortierung_anzeigePosInternWeisungen,
     * 		sortierung_anzeigePosExternWeisungen, sortierung_anzeigePosExternWeisungenHV
     * 
     * selektion gemäß KonstDBAbstimmungen
     * 
     * nurGesamtAktive=false => unabhängig von KZ aktiv, sonst nur die die aktiv=1*/
    public int leseAlleAbstimmungen(int sortierung, int selektion, boolean nurGesamtAktive) {
        int anzInArray = 0;
        PreparedStatement pstm1 = null;
        try {

            String sql = "SELECT ab.* from " + dbBundle.getSchemaMandant() + "tbl_abstimmungen ab where "
                    + "ab.mandant=? ";
            if (nurGesamtAktive == true) {
                sql = sql + " AND ab.aktiv=1 ";
            }
            switch (selektion) {
            case KonstDBAbstimmungen.selektion_alleInternAktiv:
                sql = sql
                        + " AND (ab.aktivWeisungenAnzeige=1 OR ab.aktivWeisungenInterneAuswertungen=1 OR ab.aktivWeisungenExterneAuswertungen=1 OR ab.aktivWeisungenPflegeIntern=1) ";
                break;
            case KonstDBAbstimmungen.selektion_allePortalAktiv:
                sql = sql + " AND ab.aktivWeisungenInPortal=1 ";
                break;
            case KonstDBAbstimmungen.selektion_alleAktivAufHV:
                sql = sql + " AND ab.aktivWeisungenAufHV=1 ";
                break;
            }
            switch (sortierung) {
            case KonstDBAbstimmungen.sortierung_interneIdent:
                sql = sql + "ORDER BY ab.ident;";
                break;
            case KonstDBAbstimmungen.sortierung_anzeigePosInternWeisungen:
                sql = sql + "ORDER BY ab.anzeigePositionIntern;";
                break;
            case KonstDBAbstimmungen.sortierung_anzeigePosExternWeisungen:
                sql = sql + "ORDER BY ab.anzeigePositionExternWeisungen;";
                break;
            case KonstDBAbstimmungen.sortierung_anzeigePosExternWeisungenHV:
                sql = sql + "ORDER BY ab.anzeigePositionExternWeisungenHV;";
                break;
            }

            pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pstm1.setInt(1, dbBundle.clGlobalVar.mandant);

            anzInArray = leseAllgemein(sql, pstm1);
        } catch (Exception e) {
            CaBug.drucke("DbAbstimmungen.leseAlleAbstimmungen 001");
            System.err.println(" " + e.getMessage());
        }

        return (anzInArray);

    }

    //	/**Einlesen aller Abstimmungen, je nach sortierung:
    //	 * 0 intere Ident
    //	 * 1 interne Position
    //	 * 2 externe Position (Weisungen)
    //	 * 3 externe Position (Weisungen HV) (!!!!!)*/
    //	@Deprecated
    //	public int leseAlle(int sortierung) {
    //		int anzInArray=0;
    //		PreparedStatement pstm1=null;
    //		try {
    //
    //			String sql="SELECT ab.* from "+dbBundle.getSchemaMandant()+"tbl_abstimmungen ab where "+
    //					"ab.mandant=? ";
    //			switch (sortierung){
    //			case 0:sql=sql+"ORDER BY ab.ident;";break;
    //			case 1:sql=sql+"ORDER BY ab.anzeigePositionIntern;";break;
    //			case 2:sql=sql+"ORDER BY ab.anzeigePositionExternWeisungen;";break;
    //			case 3:sql=sql+"ORDER BY ab.anzeigePositionExternWeisungenHV;";break;
    //			}
    //
    //			pstm1=verbindung.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
    //			pstm1.setInt(1, dbBundle.clGlobalVar.mandant);
    //			
    //			anzInArray=leseAllgemein(sql,pstm1);
    //		} catch (Exception e){
    //			CaBug.drucke("DbAbstimmungen.leseAlle 001");
    //			System.err.println(" "+e.getMessage());
    //		}
    //
    //		return (anzInArray);
    //	}

    //	/**Einlesen der Abstimmungen, die im Portal für Weisungen aktiv sind, sortiert nach
    //	 * anzeigePositionExternWeisungen. Nur die, die auch "Gesamtmäßig" aktiv sind*/
    //	public int leseWeisungenPortalAlle() {
    //		return leseWeisungenPortalAlle(true);
    //	}
    //		
    //	/**Einlesen der Abstimmungen, die im Portal für Weisungen aktiv sind, sortiert nach
    //	 * anzeigePositionExternWeisungen. Falls nurGesamtAktive true, dann nur die, die auch "Gesamtmäßig" aktiv sind.
    //	 * Falls false, dann unabhängig vom "geamtmäßigem" aktiv anzeigen.*/
    //	private int leseWeisungenPortalAlle(boolean nurGesamtAktive) {
    //		int anzInArray=0;
    //		PreparedStatement pstm1=null;
    //		try {
    //
    //			String sql="SELECT ab.* from "+dbBundle.getSchemaMandant()+"tbl_abstimmungen ab where "+
    //					"ab.mandant=? AND "+
    //					"ab.aktivWeisungenInPortal=1 ";
    //			if (nurGesamtAktive==true){
    //				sql=sql+" AND ab.aktiv=1 ";
    //			}
    //			sql=sql+" ORDER BY ab.anzeigePositionExternWeisungen;";
    //
    //			pstm1=verbindung.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
    //			pstm1.setInt(1, dbBundle.clGlobalVar.mandant);
    //
    //			anzInArray=leseAllgemein(sql,pstm1);
    //		} catch (Exception e){
    //			CaBug.drucke("DbAbstimmungen.leseWeisungenPortal 001");
    //			System.err.println(" "+e.getMessage());
    //		}
    //
    //		return (anzInArray);
    //	}

    //	/**Einlesen der Abstimmungen, die intern für Weisungen aktiv sind, sortiert nach
    //	 * anzeigePositionIntern. Falls nurGesamtAktive true, dann nur die, die auch "Gesamtmäßig" aktiv sind.
    //	 * Falls false, dann unabhängig vom "geamtmäßigem" aktiv anzeigen.*/
    //	public int leseWeisungenAnzeige(boolean nurGesamtAktive) {
    //		int anzInArray=0;
    //		PreparedStatement pstm1=null;
    //		try {
    //
    //			String sql="SELECT ab.* from "+dbBundle.getSchemaMandant()+"tbl_abstimmungen ab where "+
    //					"ab.mandant=? AND "+
    //					"ab.aktivWeisungenAnzeige=1 ";
    //			if (nurGesamtAktive==true){
    //				sql=sql+" AND ab.aktiv=1 ";
    //			}
    //			sql=sql+" ORDER BY ab.anzeigePositionIntern;";
    //			
    //			pstm1=verbindung.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
    //			pstm1.setInt(1, dbBundle.clGlobalVar.mandant);
    //
    //			anzInArray=leseAllgemein(sql,pstm1);
    //		} catch (Exception e){
    //			CaBug.drucke("DbAbstimmungen.leseWeisungenAnzeige 001");
    //			System.err.println(" "+e.getMessage());
    //		}
    //
    //		return (anzInArray);
    //	}

    //	
    //	
    //	/**Einlesen der Abstimmungen, die Weisungen an HV-Terminals aktiv sind, sortiert nach
    //	 * anzeigePositionExternWeisungenHV. Falls nurGesamtAktive true, dann nur die, die auch "Gesamtmäßig" aktiv sind.
    //	 * Falls false, dann unabhängig vom "geamtmäßigem" aktiv anzeigen.*/
    //	public int leseWeisungenHVAlle(boolean nurGesamtAktive) {
    //		int anzInArray=0;
    //		PreparedStatement pstm1=null;
    //		try {
    //			String sql="SELECT ab.* from "+dbBundle.getSchemaMandant()+"tbl_abstimmungen ab where "+
    //					"ab.mandant=? AND "+
    //					"ab.aktivWeisungenAufHV=1 ";
    //			if (nurGesamtAktive==true){
    //				sql=sql+" AND ab.aktiv=1 ";
    //			}
    //			sql=sql+" ORDER BY ab.anzeigePositionExternWeisungenHV;";
    //
    //			pstm1=verbindung.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
    //			pstm1.setInt(1, dbBundle.clGlobalVar.mandant);
    //
    //			anzInArray=leseAllgemein(sql,pstm1);
    //		} catch (Exception e){
    //			CaBug.drucke("DbAbstimmungen.leseWeisungenHVAlle 001");
    //			System.err.println(" "+e.getMessage());
    //		}
    //
    //		return (anzInArray);
    //	}
    //

    /**Einlesen der über Abstimmungen (ohne Gegenanträge, also Standardabstimmung), die im Portal für Weisungen aktiv sind, sortiert nach
     * anzeigePositionExternWeisungen - für alle Gattungen*/
    public int leseWeisungenPortalAgenda() {
        return leseWeisungenPortalAgenda(0);
    }

    /***************************************Ab hier neue Funktionen**********************************/

    /**
     * sicht= siehe auch KonstWeisungserfassungSicht
     * 1 => Portal Weisungserfassung
     * 2 => Interne Weisungs-Erfassung
     * 3 => Weisungs-Report Intern
     * 4 => Weisungs-Report Extern
     * 5 => Verlassen der HV Weisungs-Erfassung
     * 
     * alleOderStandardOderGegenantraege=0 => alle, 1=nur die "nicht Gegenanträge", 2=nur Gegenanträge
     * 
     * nurGesamtAktive==true => nur diejenigen werden geliefert, die in jedem Fall "aktiv=1" haben, sonst wird dieses Aktiv-Kennzeichen ignoriert
     * 
     * gattungen=0 => alle Gattungen, sonst nur die entsprechende Gattung*/
    public int leseAgendaAllgemein(int sicht, int alleOderStandardOderGegenantraege, int alleOderNurGesamtAktiveOderNurPreview,
            int gattung) {
        int anzInArray = 0;
        PreparedStatement pstm1 = null;
        try {

            String sql = "SELECT ab.* from " + dbBundle.getSchemaMandant() + "tbl_abstimmungen ab where ab.mandant=? ";

            if (alleOderNurGesamtAktiveOderNurPreview==2) {
                sql = sql + "AND ab.aktiv=1 ";
            }
            if (alleOderNurGesamtAktiveOderNurPreview==3) {
                sql = sql + "AND ab.aktivPreview=1 ";
            }

            switch (sicht) {
            case 1:
                sql = sql + "AND ab.aktivWeisungenInPortal=1 ";
                break;
            case 2:
                sql = sql
                        + "AND (ab.aktivWeisungenAnzeige=1 OR ab.aktivWeisungenInterneAuswertungen=1 OR ab.aktivWeisungenExterneAuswertungen=1 OR ab.aktivWeisungenPflegeIntern=1) ";
                break;
            case 3:
                sql = sql + "AND (ab.aktivWeisungenInterneAuswertungen=1) ";
                break;
            case 4:
                sql = sql + "AND (ab.aktivWeisungenExterneAuswertungen=1) ";
                break;
            case 5:
                sql = sql + "AND (ab.aktivWeisungenAufHV=1) ";
                break;
            }

            switch (alleOderStandardOderGegenantraege) {
            case 0:
                break;
            case 1:
                sql = sql + "AND ab.gegenantrag=0 ";
                break;
            case 2:
                sql = sql + "AND ab.gegenantrag=1 ";
                break;
            }

            if (gattung != 0) {
                sql = sql + "AND ab.stimmberechtigteGattungen" + Integer.toString(gattung - 1) + "=1 ";
            }

            switch (sicht) {
            case 1:
                sql = sql + "ORDER BY ab.anzeigePositionExternWeisungen;";
                break;
            case 2:
                sql = sql + "ORDER BY ab.anzeigePositionIntern;";
                break;
            case 3:
                sql = sql + "ORDER BY ab.anzeigePositionIntern;";
                break;
            case 4:
                sql = sql + "ORDER BY ab.anzeigePositionIntern;";
                break;
            case 5:
                sql = sql + "ORDER BY ab.anzeigePositionExternWeisungenHV;";
                break;
            }

            pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pstm1.setInt(1, dbBundle.clGlobalVar.mandant);

            anzInArray = leseAllgemein(sql, pstm1);
        } catch (Exception e) {
            CaBug.drucke("DbAbstimmungen.leseAgendaAllgemein 001");
            System.err.println(" " + e.getMessage());
        }

        return (anzInArray);
    }

    /***************************************Bis hier neue Funktionen*************************************/

    /**Einlesen der über Abstimmungen (ohne Gegenanträge, also Standardabstimmung), die im Portal für Weisungen aktiv sind, sortiert nach
     * anzeigePositionExternWeisungen - für gattung 1-5, oder 0=für alle Gattungen*/
    public int leseWeisungenPortalAgenda(int gattung) {
        int anzInArray = 0;
        PreparedStatement pstm1 = null;
        try {

            String sql = "SELECT ab.* from " + dbBundle.getSchemaMandant() + "tbl_abstimmungen ab where "
                    + "ab.mandant=? AND " + "ab.aktivWeisungenInPortal=1 AND ab.gegenantrag=0 ";
            switch (gattung) {
            case 0: {
                break;
            }
            case 1: {
                sql += " AND ab.stimmberechtigteGattungen0=1 ";
                break;
            }
            case 2: {
                sql += " AND ab.stimmberechtigteGattungen1=1 ";
                break;
            }
            case 3: {
                sql += " AND ab.stimmberechtigteGattungen2=1 ";
                break;
            }
            case 4: {
                sql += " AND ab.stimmberechtigteGattungen3=1 ";
                break;
            }
            case 5: {
                sql += " AND ab.stimmberechtigteGattungen4=1 ";
                break;
            }
            }
            sql = sql + "ORDER BY ab.anzeigePositionExternWeisungen;";

            pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pstm1.setInt(1, dbBundle.clGlobalVar.mandant);

            anzInArray = leseAllgemein(sql, pstm1);
        } catch (Exception e) {
            CaBug.drucke("DbAbstimmungen.leseWeisungenPortalAgenda 001");
            System.err.println(" " + e.getMessage());
        }

        return (anzInArray);
    }

    /**Einlesen der über Abstimmungen (nur Gegenanträge), die im Portal für Weisungen aktiv sind, sortiert nach
     * anzeigePositionExternWeisungen - für alle Gattungen*/
    public int leseWeisungenPortalGegenantraege() {
        return leseWeisungenPortalGegenantraege(0);
    }

    /**Einlesen der über Abstimmungen (nur Gegenanträge), die im Portal für Weisungen aktiv sind, sortiert nach
     * anzeigePositionExternWeisungen - für gattung 1-5, oder 0=für alle Gattungen*/
    public int leseWeisungenPortalGegenantraege(int gattungen) {

        int anzInArray = 0;
        PreparedStatement pstm1 = null;
        try {

            String sql = "SELECT ab.* from " + dbBundle.getSchemaMandant() + "tbl_abstimmungen ab where "
                    + "ab.mandant=? AND "
                    + "ab.aktivWeisungenInPortal=1 AND ab.gegenantrag=1 ORDER BY ab.anzeigePositionExternWeisungen;";

            pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pstm1.setInt(1, dbBundle.clGlobalVar.mandant);

            anzInArray = leseAllgemein(sql, pstm1);
        } catch (Exception e) {
            CaBug.drucke("DbAbstimmungen.leseWeisungenPortalGegenantraege 001");
            System.err.println(" " + e.getMessage());
        }

        return (anzInArray);
    }

    /**Einlesen der Abstimmungen, die zu einem Abstimmungsblock blockIdent gehören.
     * sortiert nach:  KonstDBAbstimmungen.
     * sortierung_interneIdent, sortierung_anzeigePosInternWeisungen, sortierung_anzeigePosExternWeisungen,
     * sortierung_posStimmkarte,sortierung_posTabletAbstimmung,sortierung_posAusdruck,sortierung_anzeigePosExternWeisungenHV
    
     * Falls nurGesamtAktive true, dann nur die, die auch "Gesamtmäßig" aktiv sind.
     * Falls false, dann unabhängig vom "geamtmäßigem" aktiv anzeigen.
     * 
     * In dbAbstimmungZuAbstimmungsblock.ergebnisarray liegen die zugehörigen EclAbstimmungZuAbstimmungsblock
     * */
    public int leseAbstimmungenZuBlock(int blockIdent, int sortiertNach, boolean nurGesamtAktive) {
        int anzInArray = 0;
        PreparedStatement pstm1 = null;
        try {
            String sql = "SELECT ab.*, abZabb.* from " + dbBundle.getSchemaMandant() + "tbl_abstimmungen ab "
                    + "INNER JOIN " + dbBundle.getSchemaMandant()
                    + "tbl_abstimmungZuAbstimmungsblock abZabb on ab.ident=abZabb.identAbstimmung " + "where "
                    + "ab.mandant=? AND " + "abZabb.mandant=? AND " + "abZabb.identAbstimmungsblock=? ";
            if (nurGesamtAktive == true) {
                sql = sql + " AND ab.aktiv=1 ";
            }

            switch (sortiertNach) {
            case KonstDBAbstimmungen.sortierung_interneIdent:
                sql = sql + "ORDER BY ab.ident;";
                break;
            case KonstDBAbstimmungen.sortierung_anzeigePosInternWeisungen:
                sql = sql + "ORDER BY ab.anzeigePositionIntern;";
                break;
            case KonstDBAbstimmungen.sortierung_anzeigePosExternWeisungen:
                sql = sql + "ORDER BY ab.anzeigePositionExternWeisungen;";
                break;
            case KonstDBAbstimmungen.sortierung_posStimmkarte:
                sql = sql + "ORDER BY abZabb.nummerDerStimmkarte, abZabb.positionAufStimmkarte;";
                break;
            case KonstDBAbstimmungen.sortierung_posTabletAbstimmung:
                sql = sql + "ORDER BY abZabb.seite, abZabb.position;";
                break;
            case KonstDBAbstimmungen.sortierung_posAusdruck:
                sql = sql + "ORDER BY abZabb.positionInAusdruck;";
                break;
            case KonstDBAbstimmungen.sortierung_anzeigePosExternWeisungenHV:
                sql = sql + "ORDER BY ab.anzeigePositionExternWeisungenHV;";
                break;
            }

            pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pstm1.setInt(1, dbBundle.clGlobalVar.mandant);
            pstm1.setInt(2, dbBundle.clGlobalVar.mandant);
            pstm1.setInt(3, blockIdent);

            ResultSet ergebnis = executeQuery(pstm1);
            ergebnis.last();
            anzInArray = ergebnis.getRow();
            ergebnis.beforeFirst();

            abstimmungenArray = new EclAbstimmung[anzInArray];
            dbBundle.dbAbstimmungZuAbstimmungsblock.ergebnisArray = new EclAbstimmungZuAbstimmungsblock[anzInArray];

            int i = 0;
            while (ergebnis.next() == true) {

                abstimmungenArray[i] = this.decodeErgebnis(ergebnis);
                dbBundle.dbAbstimmungZuAbstimmungsblock.ergebnisArray[i] = dbBundle.dbAbstimmungZuAbstimmungsblock
                        .decodeErgebnis(ergebnis);
                i++;

            }
            ergebnis.close();
            pstm1.close();

        } catch (Exception e) {
            CaBug.drucke("DbAbstimmungen.leseAbstimmungenZuBlock 001");
            System.err.println(" " + e.getMessage());
        }

        return (anzInArray);
    }

    /**Einlesen der Abstimmungen, die zu einer Stimmkarte des elektronischen Stimmkartenblocks gehören.
     * sortiert nach:  KonstDBAbstimmungen.
     * sortierung_interneIdent, sortierung_anzeigePosInternWeisungen, sortierung_anzeigePosExternWeisungen,
     * sortierung_posElektronStimmkarte, sortierung_anzeigePosExternWeisungenHV
     * 
     * Falls nurGesamtAktive true, dann nur die, die auch "Gesamtmäßig" aktiv sind.
     * Falls false, dann unabhängig vom "geamtmäßigem" aktiv anzeigen.
     * 
     * In DbAbstimmungenZuStimmkarte.ergebnisarray liegen die zugehörigen EclAbstimmungenZuStimmkarte
     * */
    public int leseAbstimmungenZuStimmkarte(int stimmkartenNr, int sortiertNach, boolean nurGesamtAktive) {
        int anzInArray = 0;
        PreparedStatement pstm1 = null;
        try {
            String sql = "SELECT ab.*, abZski.* from " + dbBundle.getSchemaMandant() + "tbl_abstimmungen ab "
                    + "INNER JOIN " + dbBundle.getSchemaMandant()
                    + "tbl_abstimmungenZuStimmkarte abZski on ab.ident=abZski.identAbstimmungAufKarte " + "where "
                    + "ab.mandant=? AND " + "abZski.mandant=? AND " + "abZski.stimmkartenNr=? ";
            if (nurGesamtAktive == true) {
                sql = sql + " AND ab.aktiv=1 ";
            }

            switch (sortiertNach) {
            case KonstDBAbstimmungen.sortierung_interneIdent:
                sql = sql + "ORDER BY ab.ident;";
                break;
            case KonstDBAbstimmungen.sortierung_anzeigePosInternWeisungen:
                sql = sql + "ORDER BY ab.anzeigePositionIntern;";
                break;
            case KonstDBAbstimmungen.sortierung_anzeigePosExternWeisungen:
                sql = sql + "ORDER BY ab.anzeigePositionExternWeisungen;";
                break;
            case KonstDBAbstimmungen.sortierung_posElektronStimmkarte:
                sql = sql + "ORDER BY abZski.stimmkartenNr, abZski.positionInStimmkarte;";
                break;
            case KonstDBAbstimmungen.sortierung_anzeigePosExternWeisungenHV:
                sql = sql + "ORDER BY ab.anzeigePositionExternWeisungenHV;";
                break;
            }

            pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pstm1.setInt(1, dbBundle.clGlobalVar.mandant);
            pstm1.setInt(2, dbBundle.clGlobalVar.mandant);
            pstm1.setInt(3, stimmkartenNr);

            ResultSet ergebnis = executeQuery(pstm1);
            ergebnis.last();
            anzInArray = ergebnis.getRow();
            ergebnis.beforeFirst();

            abstimmungenArray = new EclAbstimmung[anzInArray];
            dbBundle.dbAbstimmungenZuStimmkarte.ergebnisArray = new EclAbstimmungenZuStimmkarte[anzInArray];

            int i = 0;
            while (ergebnis.next() == true) {

                abstimmungenArray[i] = this.decodeErgebnis(ergebnis);
                dbBundle.dbAbstimmungenZuStimmkarte.ergebnisArray[i] = dbBundle.dbAbstimmungenZuStimmkarte
                        .decodeErgebnis(ergebnis);
                i++;

            }
            ergebnis.close();
            pstm1.close();

        } catch (Exception e) {
            CaBug.drucke("DbAbstimmungen.leseAbstimmungenZuStimmkarte 001");
            System.err.println(" " + e.getMessage());
        }

        return (anzInArray);
    }

    /**Update einer Abstimmung. Versionsnummer wird um 1 hochgezählt
     */
    public int update(EclAbstimmung lAbstimmung) {

        lAbstimmung.db_version++;
        try {

            String sql = "UPDATE " + dbBundle.getSchemaMandant() + "tbl_abstimmungen SET "
                    + "mandant=?, ident=?, db_version=?, "
                    + "nummer=?, nummerEN=?, nummerindex=?, nummerindexEN=?, nummerUnterdruecken=?, nummerKey=?, nummerindexKey=?, nummerFormular=?, nummerindexFormular=?, "
                    + "kurzBezeichnung=?, kurzBezeichnungEN=?, anzeigeBezeichnungKurz=?, anzeigeBezeichnungKurzEN=?, anzeigeBezeichnungLang=?, anzeigeBezeichnungLangEN=?, "
                    + "kandidat=?, kandidatEN=?, "
                    
                    + "aktiv=?, aktivPreview=?, aktivWeisungenInPortal=?, aktivWeisungenAufHV=?, aktivWeisungenSchnittstelle=?, aktivWeisungenAnzeige=?,  aktivWeisungenInterneAuswertungen=?, aktivWeisungenExterneAuswertungen=?, "
                    + "aktivWeisungenPflegeIntern=?, aktivAbstimmungInPortal=?, aktivBeiSRV=?, aktivBeiBriefwahl=?, aktivBeiKIAVDauer=?, "
                    + "aktivFragen=?, aktivAntraege=?, aktivWidersprueche=?, aktivWortmeldungen=?, aktivSonstMitteilungen=?, aktivBotschaftenEinreichen=?, "
                    
                    + "anzeigePositionIntern=?, anzeigePositionExternWeisungen=?, anzeigePositionExternWeisungenHV=?, "

                    + "externJa=?, externNein=?, externEnthaltung=?, externUngueltig=?, externNichtTeilnahme=?, "
                    + "externSonstiges1=?, externSonstiges2=?, externSonstiges3=?, "
                    + "externLoeschen=?, externFrei=?, externFreiText=?, "

                    + "internJa=?, internNein=?, internEnthaltung=?, internUngueltig=?, internNichtTeilnahme=?, "
                    + "internSonstiges1=?, internSonstiges2=?, internSonstiges3=?, "
                    + "internLoeschen=?, internFrei=?, internFreiText=?, "

                    + "elStiBloJa=?, elStiBloNein=?, elStiBloEnthaltung=?, elStiBloUngueltig=?, elStiBloNichtTeilnahme=?, "
                    + "elStiBloSonstiges1=?, elStiBloSonstiges2=?, elStiBloSonstiges3=?, "
                    + "elStiBloLoeschen=?, elStiBloFrei=?, elStiBloFreiText=?, "

                    + "tabletJa=?, tabletNein=?, tabletEnthaltung=?, tabletUngueltig=?, tabletNichtTeilnahme=?, "
                    + "tabletSonstiges1=?, tabletSonstiges2=?, tabletSonstiges3=?, "
                    + "tabletLoeschen=?, tabletFrei=?, tabletFreiText=?, "

                    + "weisungNichtMarkierteSpeichernAlsSRV=?, weisungNichtMarkierteSpeichernAlsBriefwahl=?, weisungNichtMarkierteSpeichernAlsKIAV=?, weisungNichtMarkierteSpeichernAlsDauer=?, weisungNichtMarkierteSpeichernAlsOrg=?, "
                    + "weisungHVNichtMarkierteSpeichernAls=?, " + "abstimmungNichtMarkierteSpeichernAls=?, "

                    + "identWeisungssatz=?, vonIdentGesamtweisung=?, stimmausschluss=?, "
                    + "pauschalAusschluss0=?, pauschalAusschluss1=?, pauschalAusschluss2=?, pauschalAusschluss3=?, pauschalAusschluss4=?, "
                    + "pauschalAusschlussJN0=?, pauschalAusschlussJN1=?, pauschalAusschlussJN2=?, pauschalAusschlussJN3=?, pauschalAusschlussJN4=?, "
                    + "gegenantrag=?, gegenantraegeGestellt=?, ergaenzungsantrag=?, beschlussvorschlagGestelltVon=?, beschlussvorschlagGestelltVonSonstige=?, "

                    + "zuAbstimmungsgruppe=?, identErforderlicheMehrheit=?, "
                    + "stimmberechtigteGattungen0=?, stimmberechtigteGattungen1=?, stimmberechtigteGattungen2=?, stimmberechtigteGattungen3=?, stimmberechtigteGattungen4=?, "
                    + "stimmenAuswerten=?, formularKurz=?, formularLang=?, formularBuehnenInfo=? "

                    + " WHERE " + "mandant=? AND ident=? AND " + "db_version=? ";

            PreparedStatement pstm1 = verbindung.prepareStatement(sql);
            fuellePreparedStatementKomplett(pstm1, 1, lAbstimmung);
            pstm1.setLong(anzfelder + 1, dbBundle.clGlobalVar.mandant);
            pstm1.setInt(anzfelder + 2, lAbstimmung.ident);
            pstm1.setLong(anzfelder + 3, lAbstimmung.db_version - 1);

            int ergebnis1 = executeUpdate(pstm1);
            pstm1.close();
            if (ergebnis1 == 0) {
                return (CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert);
            }
        } catch (Exception e1) {
            CaBug.drucke("DbAbstimmungen.update 001");
            System.err.println(" " + e1.getMessage());
            return (CaFehler.pfdXyBereitsVorhanden);
        }

        return (1);
    }

    public int delete(int ident) {

        try {

            String sql = "DELETE FROM " + dbBundle.getSchemaMandant() + "tbl_abstimmungen WHERE ident=? AND mandant=? ";

            PreparedStatement pstm1 = verbindung.prepareStatement(sql);
            pstm1.setInt(1, ident);
            pstm1.setInt(2, dbBundle.clGlobalVar.mandant);

            int ergebnis1 = executeUpdate(pstm1);
            pstm1.close();
            if (ergebnis1 == 0) {
                return (CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert);
            }
        } catch (Exception e1) {
            CaBug.drucke("DbAbstimmungen.delete 001");
            System.err.println(" " + e1.getMessage());
            return (CaFehler.pfdXyBereitsVorhanden);
        }

        return (1);
    }

}