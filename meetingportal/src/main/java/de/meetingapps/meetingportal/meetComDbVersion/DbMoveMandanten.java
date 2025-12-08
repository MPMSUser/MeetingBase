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
package de.meetingapps.meetingportal.meetComDbVersion;

import java.sql.Connection;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComDb.DbLowLevel;

public class DbMoveMandanten {
    private Connection verbindung = null;
    //	private DbBasis dbBasis=null;
    private DbBundle dbBundle = null;

    /*************************Initialisierung***************************/
    public DbMoveMandanten(DbBundle pDbBundle) {
        /* Verbindung in lokale Daten eintragen*/
        if (pDbBundle == null) {
            CaBug.drucke("DbDatenbankVerwaltung.init 001 - dbBundle nicht initialisiert");
            return;
        }
        if (pDbBundle.dbBasis == null) {
            CaBug.drucke("DbDatenbankVerwaltung.init 002 - dbBasis nicht initialisiert");
            return;
        }

        //		dbBasis=pDbBundle.dbBasis;
        verbindung = pDbBundle.dbBasis.verbindung;
        dbBundle = pDbBundle;
    }

    public int move(boolean mitDelete) {

        int rc = 0;
        DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);

        /*DbAbstimmungen*/
        rc = lDbLowLevel.rawOperation("INSERT INTO " + dbBundle.getSchemaMandant()
                + "tbl_abstimmungen SELECT * FROM db_meetingcomfort.tbl_abstimmungen tblAlt where tblAlt.mandant="
                + dbBundle.clGlobalVar.mandant);
        if (rc < 0) {
            CaBug.drucke("DbMoveMandanten.move 001");
            return rc;
        }

        dbBundle.dbAbstimmungen.reorgInterneIdent();

        if (mitDelete) {
            dbBundle.mandantenTablesGetrennt = false;
            dbBundle.dbAbstimmungen.deleteAll();
            dbBundle.mandantenTablesGetrennt = true;
        }

        /*tbl_abstimmungenZuStimmkarte*/
        rc = lDbLowLevel.rawOperation("INSERT INTO " + dbBundle.getSchemaMandant()
                + "tbl_abstimmungenZuStimmkarte SELECT * FROM db_meetingcomfort.tbl_abstimmungenZuStimmkarte tblAlt where tblAlt.mandant="
                + dbBundle.clGlobalVar.mandant);
        if (rc < 0) {
            CaBug.drucke("DbMoveMandanten.move 002");
            return rc;
        }

        if (mitDelete) {
            dbBundle.mandantenTablesGetrennt = false;
            dbBundle.dbAbstimmungenZuStimmkarte.deleteAll();
            dbBundle.mandantenTablesGetrennt = true;
        }

        /*DbAbstimmungenEinzelAusschluss*/
        rc = lDbLowLevel.rawOperation("INSERT INTO " + dbBundle.getSchemaMandant()
                + "tbl_abstimmungenEinzelAusschluss SELECT * FROM db_meetingcomfort.tbl_abstimmungenEinzelAusschluss tblAlt where tblAlt.mandant="
                + dbBundle.clGlobalVar.mandant);
        if (rc < 0) {
            CaBug.drucke("DbMoveMandanten.move 002");
            return rc;
        }

        if (mitDelete) {
            dbBundle.mandantenTablesGetrennt = false;
            dbBundle.dbAbstimmungenEinzelAusschluss.deleteAll();
            dbBundle.mandantenTablesGetrennt = true;
        }

        /*DbAbstimmungMeldung*/
        rc = lDbLowLevel.rawOperation("INSERT INTO " + dbBundle.getSchemaMandant()
                + "tbl_abstimmungMeldung SELECT * FROM db_meetingcomfort.tbl_abstimmungMeldung tblAlt where tblAlt.mandant="
                + dbBundle.clGlobalVar.mandant);
        if (rc < 0) {
            CaBug.drucke("DbMoveMandanten.move 004");
            return rc;
        }

        if (mitDelete) {
            dbBundle.mandantenTablesGetrennt = false;
            dbBundle.dbAbstimmungMeldung.deleteAll();
            dbBundle.mandantenTablesGetrennt = true;
        }

        /*tbl_abstimmungmeldungraw*/
        rc = lDbLowLevel.rawOperation("INSERT INTO " + dbBundle.getSchemaMandant()
                + "tbl_abstimmungmeldungraw SELECT * FROM db_meetingcomfort.tbl_abstimmungmeldungraw tblAlt where tblAlt.mandant="
                + dbBundle.clGlobalVar.mandant);
        if (rc < 0) {
            CaBug.drucke("DbMoveMandanten.move 005");
            return rc;
        }

        dbBundle.dbAbstimmungMeldungRaw.reorgInterneIdent();

        if (mitDelete) {
            dbBundle.mandantenTablesGetrennt = false;
            dbBundle.dbAbstimmungMeldungRaw.deleteAll();
            dbBundle.mandantenTablesGetrennt = true;
        }

        /*tbl_abstimmungmeldungsplit*/
        for (int i = 0; i < 4; i++) {
            String lfd = Integer.toString(i);
            if (i == 0) {
                lfd = "";
            }
            rc = lDbLowLevel.rawOperation("INSERT INTO " + dbBundle.getSchemaMandant() + "tbl_abstimmungmeldungsplit"
                    + lfd + " SELECT * FROM db_meetingcomfort.tbl_abstimmungmeldungsplit" + lfd
                    + " tblAlt where tblAlt.mandant=" + dbBundle.clGlobalVar.mandant);
            if (rc < 0) {
                CaBug.drucke("DbMoveMandanten.move " + lfd + " 006");
                return rc;
            }
        }

        if (mitDelete) {
            dbBundle.mandantenTablesGetrennt = false;
            dbBundle.dbAbstimmungMeldungSplit.deleteAll();
            dbBundle.mandantenTablesGetrennt = true;
        }

        /*tbl_abstimmungsblock*/
        rc = lDbLowLevel.rawOperation("INSERT INTO " + dbBundle.getSchemaMandant()
                + "tbl_abstimmungsblock SELECT * FROM db_meetingcomfort.tbl_abstimmungsblock tblAlt where tblAlt.mandant="
                + dbBundle.clGlobalVar.mandant);
        if (rc < 0) {
            CaBug.drucke("DbMoveMandanten.move 007");
            return rc;
        }

        dbBundle.dbAbstimmungsblock.reorgInterneIdent();

        if (mitDelete) {
            dbBundle.mandantenTablesGetrennt = false;
            dbBundle.dbAbstimmungsblock.deleteAll();
            dbBundle.mandantenTablesGetrennt = true;
        }

        /*tbl_abstimmungsmonitorek*/
        rc = lDbLowLevel.rawOperation("INSERT INTO " + dbBundle.getSchemaMandant()
                + "tbl_abstimmungsmonitorek SELECT * FROM db_meetingcomfort.tbl_abstimmungsmonitorek tblAlt where tblAlt.mandant="
                + dbBundle.clGlobalVar.mandant);
        if (rc < 0) {
            CaBug.drucke("DbMoveMandanten.move 008");
            return rc;
        }

        dbBundle.dbAbstimmungsmonitorEK.reorgInterneIdent();

        if (mitDelete) {
            dbBundle.mandantenTablesGetrennt = false;
            dbBundle.dbAbstimmungsmonitorEK.deleteAll();
            dbBundle.mandantenTablesGetrennt = true;
        }

        /*tbl_abstimmungsvorschlag*/
        rc = lDbLowLevel.rawOperation("INSERT INTO " + dbBundle.getSchemaMandant()
                + "tbl_abstimmungsvorschlag SELECT * FROM db_meetingcomfort.tbl_abstimmungsvorschlag tblAlt where tblAlt.mandant="
                + dbBundle.clGlobalVar.mandant);
        if (rc < 0) {
            CaBug.drucke("DbMoveMandanten.move 009");
            return rc;
        }

        dbBundle.dbAbstimmungsVorschlag.reorgInterneIdent();

        if (mitDelete) {
            dbBundle.mandantenTablesGetrennt = false;
            dbBundle.dbAbstimmungsVorschlag.deleteAll();
            dbBundle.mandantenTablesGetrennt = true;
        }

        /*tbl_abstimmungzuabstimmungsblock*/
        rc = lDbLowLevel.rawOperation("INSERT INTO " + dbBundle.getSchemaMandant()
                + "tbl_abstimmungzuabstimmungsblock SELECT * FROM db_meetingcomfort.tbl_abstimmungzuabstimmungsblock tblAlt where tblAlt.mandant="
                + dbBundle.clGlobalVar.mandant);
        if (rc < 0) {
            CaBug.drucke("DbMoveMandanten.move 010");
            return rc;
        }

        dbBundle.dbAbstimmungZuAbstimmungsblock.reorgInterneIdent();

        if (mitDelete) {
            dbBundle.mandantenTablesGetrennt = false;
            dbBundle.dbAbstimmungZuAbstimmungsblock.deleteAll();
            dbBundle.mandantenTablesGetrennt = true;
        }

        /*tbl_aenderungslog*/
        rc = lDbLowLevel.rawOperation("INSERT INTO " + dbBundle.getSchemaMandant()
                + "tbl_aenderungslog SELECT * FROM db_meetingcomfort.tbl_aenderungslog tblAlt where tblAlt.mandant="
                + dbBundle.clGlobalVar.mandant);
        if (rc < 0) {
            CaBug.drucke("DbMoveMandanten.move 011");
            return rc;
        }

        if (mitDelete) {
            dbBundle.mandantenTablesGetrennt = false;
            dbBundle.dbAenderungslog.deleteAll();
            dbBundle.mandantenTablesGetrennt = true;
        }

        /*tbl_aktienregister*/
        rc = lDbLowLevel.rawOperation("INSERT INTO " + dbBundle.getSchemaMandant()
                + "tbl_aktienregister SELECT * FROM db_meetingcomfort.tbl_aktienregister tblAlt where tblAlt.mandant="
                + dbBundle.clGlobalVar.mandant);
        if (rc < 0) {
            CaBug.drucke("DbMoveMandanten.move 012");
            return rc;
        }

        dbBundle.dbAktienregister.reorgInterneIdent();

        if (mitDelete) {
            dbBundle.mandantenTablesGetrennt = false;
            dbBundle.dbAktienregister.deleteAll();
            dbBundle.mandantenTablesGetrennt = true;
        }

        /*tbl_aktienregisterhistorie*/
        rc = lDbLowLevel.rawOperation("INSERT INTO " + dbBundle.getSchemaMandant()
                + "tbl_aktienregisterhistorie SELECT * FROM db_meetingcomfort.tbl_aktienregisterhistorie tblAlt where tblAlt.mandant="
                + dbBundle.clGlobalVar.mandant);
        if (rc < 0) {
            CaBug.drucke("DbMoveMandanten.move 013");
            return rc;
        }

        if (mitDelete) {
            dbBundle.mandantenTablesGetrennt = false;
            dbBundle.dbAktienregister.deleteAll_aktienregisterHistorie();
            dbBundle.mandantenTablesGetrennt = true;
        }

        /*tbl_aktienregisterlogindaten*/
        rc = lDbLowLevel.rawOperation("INSERT INTO " + dbBundle.getSchemaMandant()
                + "tbl_aktienregisterlogindaten SELECT * FROM db_meetingcomfort.tbl_aktienregisterlogindaten tblAlt where tblAlt.mandant="
                + dbBundle.clGlobalVar.mandant);
        if (rc < 0) {
            CaBug.drucke("DbMoveMandanten.move 014");
            return rc;
        }

        if (mitDelete) {
            dbBundle.mandantenTablesGetrennt = false;
            dbBundle.dbAktienregisterLoginDaten.deleteAll();
            dbBundle.mandantenTablesGetrennt = true;
        }

        /*tbl_aktienregisterzusatz*/
        rc = lDbLowLevel.rawOperation("INSERT INTO " + dbBundle.getSchemaMandant()
                + "tbl_aktienregisterzusatz SELECT * FROM db_meetingcomfort.tbl_aktienregisterzusatz tblAlt where tblAlt.mandant="
                + dbBundle.clGlobalVar.mandant);
        if (rc < 0) {
            CaBug.drucke("DbMoveMandanten.move 015");
            return rc;
        }

        if (mitDelete) {
            dbBundle.mandantenTablesGetrennt = false;
            dbBundle.dbAktienregisterZusatz.deleteAll();
            dbBundle.mandantenTablesGetrennt = true;
        }

        /*tbl_ausstellungsgrundmandant*/
        rc = lDbLowLevel.rawOperation("INSERT INTO " + dbBundle.getSchemaMandant()
                + "tbl_ausstellungsgrundmandant SELECT * FROM db_meetingcomfort.tbl_ausstellungsgrundmandant tblAlt where tblAlt.mandant="
                + dbBundle.clGlobalVar.mandant);
        if (rc < 0) {
            CaBug.drucke("DbMoveMandanten.move 016");
            return rc;
        }

        if (mitDelete) {
            dbBundle.mandantenTablesGetrennt = false;
            dbBundle.dbAusstellungsgrund.deleteAll_Mandant();
            dbBundle.mandantenTablesGetrennt = true;
        }

        /*tbl_gruppenmandant*/
        rc = lDbLowLevel.rawOperation("INSERT INTO " + dbBundle.getSchemaMandant()
                + "tbl_gruppenmandant SELECT * FROM db_meetingcomfort.tbl_gruppenmandant tblAlt where tblAlt.mandant="
                + dbBundle.clGlobalVar.mandant);
        if (rc < 0) {
            CaBug.drucke("DbMoveMandanten.move 017");
            return rc;
        }

        dbBundle.dbGruppen.reorgInterneIdent_mandant();

        if (mitDelete) {
            dbBundle.mandantenTablesGetrennt = false;
            dbBundle.dbGruppen.deleteAll_mandant();
            dbBundle.mandantenTablesGetrennt = true;
        }

        /*tbl_hvdatenlfd*/
        rc = lDbLowLevel.rawOperation("INSERT INTO " + dbBundle.getSchemaMandant()
                + "tbl_hvdatenlfd SELECT * FROM db_meetingcomfort.tbl_hvdatenlfd tblAlt where tblAlt.mandant="
                + dbBundle.clGlobalVar.mandant);
        if (rc < 0) {
            CaBug.drucke("DbMoveMandanten.move 018");
            return rc;
        }

        if (mitDelete) {
            dbBundle.mandantenTablesGetrennt = false;
            dbBundle.dbHVDatenLfd.deleteAll();
            dbBundle.mandantenTablesGetrennt = true;
        }

        /*tbl_meldungen_ausstellungsgrund*/
        rc = lDbLowLevel.rawOperation("INSERT INTO " + dbBundle.getSchemaMandant()
                + "tbl_meldungen_ausstellungsgrund SELECT * FROM db_meetingcomfort.tbl_meldungen_ausstellungsgrund tblAlt where tblAlt.mandant="
                + dbBundle.clGlobalVar.mandant);
        if (rc < 0) {
            CaBug.drucke("DbMoveMandanten.move 019");
            return rc;
        }

        if (mitDelete) {
            dbBundle.mandantenTablesGetrennt = false;
            dbBundle.dbMeldungAusstellungsgrund.deleteAll();
            dbBundle.mandantenTablesGetrennt = true;
        }

        /*tbl_meldungen, tbl_meldungenProtokoll*/
        rc = lDbLowLevel.rawOperation("INSERT INTO " + dbBundle.getSchemaMandant()
                + "tbl_meldungen SELECT * FROM db_meetingcomfort.tbl_meldungen tblAlt where tblAlt.mandant="
                + dbBundle.clGlobalVar.mandant);
        if (rc < 0) {
            CaBug.drucke("DbMoveMandanten.move 020");
            return rc;
        }
        rc = lDbLowLevel.rawOperation("INSERT INTO " + dbBundle.getSchemaMandant()
                + "tbl_meldungenProtokoll SELECT * FROM db_meetingcomfort.tbl_meldungenProtokoll tblAlt where tblAlt.mandant="
                + dbBundle.clGlobalVar.mandant);
        if (rc < 0) {
            CaBug.drucke("DbMoveMandanten.move 021");
            return rc;
        }

        dbBundle.dbMeldungen.reorgInterneIdent();

        if (mitDelete) {
            dbBundle.mandantenTablesGetrennt = false;
            dbBundle.dbMeldungen.deleteAll();
            dbBundle.mandantenTablesGetrennt = true;
        }

        /*tbl_meldungen_meldungen*/
        rc = lDbLowLevel.rawOperation("INSERT INTO " + dbBundle.getSchemaMandant()
                + "tbl_meldungen_meldungen SELECT * FROM db_meetingcomfort.tbl_meldungen_meldungen tblAlt where tblAlt.mandant="
                + dbBundle.clGlobalVar.mandant);
        if (rc < 0) {
            CaBug.drucke("DbMoveMandanten.move 022");
            return rc;
        }

        if (mitDelete) {
            dbBundle.mandantenTablesGetrennt = false;
            dbBundle.dbMeldungenMeldungen.deleteAll();
            dbBundle.mandantenTablesGetrennt = true;
        }

        /*tbl_meldungenvipkzausgeblendet*/
        rc = lDbLowLevel.rawOperation("INSERT INTO " + dbBundle.getSchemaMandant()
                + "tbl_meldungenvipkzausgeblendet SELECT * FROM db_meetingcomfort.tbl_meldungenvipkzausgeblendet tblAlt where tblAlt.mandant="
                + dbBundle.clGlobalVar.mandant);
        if (rc < 0) {
            CaBug.drucke("DbMoveMandanten.move 023");
            return rc;
        }

        if (mitDelete) {
            dbBundle.mandantenTablesGetrennt = false;
            dbBundle.dbMeldungenVipKZAusgeblendet.deleteAll();
            dbBundle.mandantenTablesGetrennt = true;
        }

        /*tbl_meldungenvipkz*/
        rc = lDbLowLevel.rawOperation("INSERT INTO " + dbBundle.getSchemaMandant()
                + "tbl_meldungenvipkz SELECT * FROM db_meetingcomfort.tbl_meldungenvipkz tblAlt where tblAlt.mandant="
                + dbBundle.clGlobalVar.mandant);
        if (rc < 0) {
            CaBug.drucke("DbMoveMandanten.move 024");
            return rc;
        }

        if (mitDelete) {
            dbBundle.mandantenTablesGetrennt = false;
            dbBundle.dbMeldungVipKZ.deleteAll();
            dbBundle.mandantenTablesGetrennt = true;
        }

        /*tbl_meldungzusammelkarte*/
        rc = lDbLowLevel.rawOperation("INSERT INTO " + dbBundle.getSchemaMandant()
                + "tbl_meldungzusammelkarte SELECT * FROM db_meetingcomfort.tbl_meldungzusammelkarte tblAlt where tblAlt.mandant="
                + dbBundle.clGlobalVar.mandant);
        if (rc < 0) {
            CaBug.drucke("DbMoveMandanten.move 025");
            return rc;
        }

        if (mitDelete) {
            dbBundle.mandantenTablesGetrennt = false;
            dbBundle.dbMeldungZuSammelkarte.deleteAll();
            dbBundle.mandantenTablesGetrennt = true;
        }

        /*tbl_parameter*/
        rc = lDbLowLevel.rawOperation("INSERT INTO " + dbBundle.getSchemaMandant()
                + "tbl_parameter SELECT * FROM db_meetingcomfort.tbl_parameter tblAlt where tblAlt.mandant="
                + dbBundle.clGlobalVar.mandant);
        if (rc < 0) {
            CaBug.drucke("DbMoveMandanten.move 026");
            return rc;
        }

        if (mitDelete) {
            dbBundle.mandantenTablesGetrennt = false;
            dbBundle.dbParameter.deleteAll_parameter();
            dbBundle.mandantenTablesGetrennt = true;
        }

        /*tbl_parameterlfd*/
        rc = lDbLowLevel.rawOperation("INSERT INTO " + dbBundle.getSchemaMandant()
                + "tbl_parameterlfd SELECT * FROM db_meetingcomfort.tbl_parameterlfd tblAlt where tblAlt.mandant="
                + dbBundle.clGlobalVar.mandant);
        if (rc < 0) {
            CaBug.drucke("DbMoveMandanten.move 027");
            return rc;
        }

        if (mitDelete) {
            dbBundle.mandantenTablesGetrennt = false;
            dbBundle.dbParameter.deleteAll_parameterLfd();
            dbBundle.mandantenTablesGetrennt = true;
        }

        /*tbl_parameterlocal*/
        rc = lDbLowLevel.rawOperation("INSERT INTO " + dbBundle.getSchemaMandant()
                + "tbl_parameterlocal SELECT * FROM db_meetingcomfort.tbl_parameterlocal tblAlt where tblAlt.mandant="
                + dbBundle.clGlobalVar.mandant);
        if (rc < 0) {
            CaBug.drucke("DbMoveMandanten.move 027");
            return rc;
        }

        if (mitDelete) {
            dbBundle.mandantenTablesGetrennt = false;
            dbBundle.dbParameter.deleteAll_parameterLocal();
            dbBundle.mandantenTablesGetrennt = true;
        }

        /*tbl_personenNatJur, tbl_personenNatJurProtokoll*/
        rc = lDbLowLevel.rawOperation("INSERT INTO " + dbBundle.getSchemaMandant()
                + "tbl_personenNatJur SELECT * FROM db_meetingcomfort.tbl_personenNatJur tblAlt where tblAlt.mandant="
                + dbBundle.clGlobalVar.mandant);
        if (rc < 0) {
            CaBug.drucke("DbMoveMandanten.move 028");
            return rc;
        }

        rc = lDbLowLevel.rawOperation("INSERT INTO " + dbBundle.getSchemaMandant()
                + "tbl_personenNatJurProtokoll SELECT * FROM db_meetingcomfort.tbl_personenNatJurProtokoll tblAlt where tblAlt.mandant="
                + dbBundle.clGlobalVar.mandant);
        if (rc < 0) {
            CaBug.drucke("DbMoveMandanten.move 029");
            return rc;
        }

        dbBundle.dbPersonenNatJur.reorgInterneIdent();

        if (mitDelete) {
            dbBundle.mandantenTablesGetrennt = false;
            dbBundle.dbPersonenNatJur.deleteAll();
            dbBundle.mandantenTablesGetrennt = true;
        }

        /*tbl_personennatjurversandadresse*/
        rc = lDbLowLevel.rawOperation("INSERT INTO " + dbBundle.getSchemaMandant()
                + "tbl_personennatjurversandadresse SELECT * FROM db_meetingcomfort.tbl_personennatjurversandadresse tblAlt where tblAlt.mandant="
                + dbBundle.clGlobalVar.mandant);
        if (rc < 0) {
            CaBug.drucke("DbMoveMandanten.move 030");
            return rc;
        }

        dbBundle.dbPersonenNatJurVersandadresse.reorgInterneIdent();

        if (mitDelete) {
            dbBundle.mandantenTablesGetrennt = false;
            dbBundle.dbPersonenNatJurVersandadresse.deleteAll();
            dbBundle.mandantenTablesGetrennt = true;
        }

        /*tbl_praesenzliste*/
        rc = lDbLowLevel.rawOperation("INSERT INTO " + dbBundle.getSchemaMandant()
                + "tbl_praesenzliste SELECT * FROM db_meetingcomfort.tbl_praesenzliste tblAlt where tblAlt.mandant="
                + dbBundle.clGlobalVar.mandant);
        if (rc < 0) {
            CaBug.drucke("DbMoveMandanten.move 031");
            return rc;
        }

        if (mitDelete) {
            dbBundle.mandantenTablesGetrennt = false;
            dbBundle.dbPraesenzliste.deleteAll();
            dbBundle.mandantenTablesGetrennt = true;
        }

        /*tbl_publikation*/
        rc = lDbLowLevel.rawOperation("INSERT INTO " + dbBundle.getSchemaMandant()
                + "tbl_publikation SELECT * FROM db_meetingcomfort.tbl_publikation tblAlt where tblAlt.mandant="
                + dbBundle.clGlobalVar.mandant);
        if (rc < 0) {
            CaBug.drucke("DbMoveMandanten.move 032");
            return rc;
        }

        dbBundle.dbPublikation.reorgInterneIdent();

        if (mitDelete) {
            dbBundle.mandantenTablesGetrennt = false;
            dbBundle.dbPublikation.deleteAll();
            dbBundle.mandantenTablesGetrennt = true;
        }

        /*tbl_stimmkarteinhalt*/
        rc = lDbLowLevel.rawOperation("INSERT INTO " + dbBundle.getSchemaMandant()
                + "tbl_stimmkarteinhalt SELECT * FROM db_meetingcomfort.tbl_stimmkarteinhalt tblAlt where tblAlt.mandant="
                + dbBundle.clGlobalVar.mandant);
        if (rc < 0) {
            CaBug.drucke("DbMoveMandanten.move 033");
            return rc;
        }

        if (mitDelete) {
            dbBundle.mandantenTablesGetrennt = false;
            dbBundle.dbStimmkarteInhalt.deleteAll();
            dbBundle.mandantenTablesGetrennt = true;
        }

        /*tbl_stimmkarten*/
        rc = lDbLowLevel.rawOperation("INSERT INTO " + dbBundle.getSchemaMandant()
                + "tbl_stimmkarten SELECT * FROM db_meetingcomfort.tbl_stimmkarten tblAlt where tblAlt.mandant="
                + dbBundle.clGlobalVar.mandant);
        if (rc < 0) {
            CaBug.drucke("DbMoveMandanten.move 034");
            return rc;
        }

        dbBundle.dbStimmkarten.reorgInterneIdent();

        if (mitDelete) {
            dbBundle.mandantenTablesGetrennt = false;
            dbBundle.dbStimmkarten.deleteAll();
            dbBundle.mandantenTablesGetrennt = true;
        }

        /*tbl_stimmkartenSecond*/
        rc = lDbLowLevel.rawOperation("INSERT INTO " + dbBundle.getSchemaMandant()
                + "tbl_stimmkartenSecond SELECT * FROM db_meetingcomfort.tbl_stimmkartenSecond tblAlt where tblAlt.mandant="
                + dbBundle.clGlobalVar.mandant);
        if (rc < 0) {
            CaBug.drucke("DbMoveMandanten.move 035");
            return rc;
        }

        dbBundle.dbStimmkartenSecond.reorgInterneIdent();

        if (mitDelete) {
            dbBundle.mandantenTablesGetrennt = false;
            dbBundle.dbStimmkartenSecond.deleteAll();
            dbBundle.mandantenTablesGetrennt = true;
        }

        /*FIXME - Teilnehmerstand Verein Table in Test-Daten nicht vorhanden!*/
        //		/*tbl_teilnehmerStandVerein*/
        //		rc=lDbLowLevel.rawOperation("INSERT INTO "+dbBundle.getSchemaMandant()+"tbl_teilnehmerStandVerein SELECT * FROM db_meetingcomfort.tbl_teilnehmerStandVerein tblAlt where tblAlt.mandant="+dbBundle.globalVar.mandant);
        //		if (rc<0){
        //			CaBug.drucke("DbMoveMandanten.move 036");
        //			return rc;
        //		}
        //		
        //		if (mitDelete){
        //			dbBundle.mandantenTablesGetrennt=false;
        //			dbBundle.dbTeilnehmerStandVerein.deleteAll();
        //			dbBundle.mandantenTablesGetrennt=true;
        //		}

        /*tbl_willenserklaerung*/
        rc = lDbLowLevel.rawOperation("INSERT INTO " + dbBundle.getSchemaMandant()
                + "tbl_willenserklaerung SELECT * FROM db_meetingcomfort.tbl_willenserklaerung tblAlt where tblAlt.mandant="
                + dbBundle.clGlobalVar.mandant);
        if (rc < 0) {
            CaBug.drucke("DbMoveMandanten.move 037");
            return rc;
        }

        dbBundle.dbWillenserklaerung.reorgInterneIdent();

        if (mitDelete) {
            dbBundle.mandantenTablesGetrennt = false;
            dbBundle.dbWillenserklaerung.deleteAll();
            dbBundle.mandantenTablesGetrennt = true;
        }

        /*tbl_willenserklaerungzusatz*/
        rc = lDbLowLevel.rawOperation("INSERT INTO " + dbBundle.getSchemaMandant()
                + "tbl_willenserklaerungzusatz SELECT * FROM db_meetingcomfort.tbl_willenserklaerungzusatz tblAlt where tblAlt.mandant="
                + dbBundle.clGlobalVar.mandant);
        if (rc < 0) {
            CaBug.drucke("DbMoveMandanten.move 038");
            return rc;
        }

        if (mitDelete) {
            dbBundle.mandantenTablesGetrennt = false;
            dbBundle.dbWillenserklaerungZusatz.deleteAll();
            dbBundle.mandantenTablesGetrennt = true;
        }

        /*tbl_zutrittskarten*/
        rc = lDbLowLevel.rawOperation("INSERT INTO " + dbBundle.getSchemaMandant()
                + "tbl_zutrittskarten SELECT * FROM db_meetingcomfort.tbl_zutrittskarten tblAlt where tblAlt.mandant="
                + dbBundle.clGlobalVar.mandant);
        if (rc < 0) {
            CaBug.drucke("DbMoveMandanten.move 039");
            return rc;
        }

        dbBundle.dbZutrittskarten.reorgInterneIdent();

        if (mitDelete) {
            dbBundle.mandantenTablesGetrennt = false;
            dbBundle.dbZutrittskarten.deleteAll();
            dbBundle.mandantenTablesGetrennt = true;
        }

        /*tbl_weisungMeldung,
         * tbl_weisungMeldungRaw,tbl_weisungMeldungRaw1,
         * tbl_weisungMeldungSplit,tbl_weisungMeldungSplit1,tbl_weisungMeldungSplit2,tbl_weisungMeldungSplit3,
         * tbl_weisungMeldungSplitRaw,tbl_weisungMeldungSplitRaw1,tbl_weisungMeldungSplitRaw2,tbl_weisungMeldungSplitRaw3
         * */
        rc = lDbLowLevel.rawOperation("INSERT INTO " + dbBundle.getSchemaMandant()
                + "tbl_weisungMeldung SELECT * FROM db_meetingcomfort.tbl_weisungMeldung tblAlt where tblAlt.mandant="
                + dbBundle.clGlobalVar.mandant);
        if (rc < 0) {
            CaBug.drucke("DbMoveMandanten.move 040");
            return rc;
        }
        rc = lDbLowLevel.rawOperation("INSERT INTO " + dbBundle.getSchemaMandant()
                + "tbl_weisungMeldungRaw SELECT * FROM db_meetingcomfort.tbl_weisungMeldungRaw tblAlt where tblAlt.mandant="
                + dbBundle.clGlobalVar.mandant);
        if (rc < 0) {
            CaBug.drucke("DbMoveMandanten.move 041");
            return rc;
        }
        rc = lDbLowLevel.rawOperation("INSERT INTO " + dbBundle.getSchemaMandant()
                + "tbl_weisungMeldungRaw1 SELECT * FROM db_meetingcomfort.tbl_weisungMeldungRaw1 tblAlt where tblAlt.mandant="
                + dbBundle.clGlobalVar.mandant);
        if (rc < 0) {
            CaBug.drucke("DbMoveMandanten.move 042");
            return rc;
        }
        rc = lDbLowLevel.rawOperation("INSERT INTO " + dbBundle.getSchemaMandant()
                + "tbl_weisungMeldungSplit SELECT * FROM db_meetingcomfort.tbl_weisungMeldungSplit tblAlt where tblAlt.mandant="
                + dbBundle.clGlobalVar.mandant);
        if (rc < 0) {
            CaBug.drucke("DbMoveMandanten.move 044");
            return rc;
        }
        rc = lDbLowLevel.rawOperation("INSERT INTO " + dbBundle.getSchemaMandant()
                + "tbl_weisungMeldungSplit1 SELECT * FROM db_meetingcomfort.tbl_weisungMeldungSplit1 tblAlt where tblAlt.mandant="
                + dbBundle.clGlobalVar.mandant);
        if (rc < 0) {
            CaBug.drucke("DbMoveMandanten.move 045");
            return rc;
        }
        rc = lDbLowLevel.rawOperation("INSERT INTO " + dbBundle.getSchemaMandant()
                + "tbl_weisungMeldungSplit2 SELECT * FROM db_meetingcomfort.tbl_weisungMeldungSplit2 tblAlt where tblAlt.mandant="
                + dbBundle.clGlobalVar.mandant);
        if (rc < 0) {
            CaBug.drucke("DbMoveMandanten.move 046");
            return rc;
        }
        rc = lDbLowLevel.rawOperation("INSERT INTO " + dbBundle.getSchemaMandant()
                + "tbl_weisungMeldungSplit3 SELECT * FROM db_meetingcomfort.tbl_weisungMeldungSplit3 tblAlt where tblAlt.mandant="
                + dbBundle.clGlobalVar.mandant);
        if (rc < 0) {
            CaBug.drucke("DbMoveMandanten.move 047");
            return rc;
        }
        rc = lDbLowLevel.rawOperation("INSERT INTO " + dbBundle.getSchemaMandant()
                + "tbl_weisungMeldungSplitRaw SELECT * FROM db_meetingcomfort.tbl_weisungMeldungSplitRaw tblAlt where tblAlt.mandant="
                + dbBundle.clGlobalVar.mandant);
        if (rc < 0) {
            CaBug.drucke("DbMoveMandanten.move 048");
            return rc;
        }
        rc = lDbLowLevel.rawOperation("INSERT INTO " + dbBundle.getSchemaMandant()
                + "tbl_weisungMeldungSplitRaw1 SELECT * FROM db_meetingcomfort.tbl_weisungMeldungSplitRaw1 tblAlt where tblAlt.mandant="
                + dbBundle.clGlobalVar.mandant);
        if (rc < 0) {
            CaBug.drucke("DbMoveMandanten.move 049");
            return rc;
        }
        rc = lDbLowLevel.rawOperation("INSERT INTO " + dbBundle.getSchemaMandant()
                + "tbl_weisungMeldungSplitRaw2 SELECT * FROM db_meetingcomfort.tbl_weisungMeldungSplitRaw2 tblAlt where tblAlt.mandant="
                + dbBundle.clGlobalVar.mandant);
        if (rc < 0) {
            CaBug.drucke("DbMoveMandanten.move 050");
            return rc;
        }
        rc = lDbLowLevel.rawOperation("INSERT INTO " + dbBundle.getSchemaMandant()
                + "tbl_weisungMeldungSplitRaw3 SELECT * FROM db_meetingcomfort.tbl_weisungMeldungSplitRaw3 tblAlt where tblAlt.mandant="
                + dbBundle.clGlobalVar.mandant);
        if (rc < 0) {
            CaBug.drucke("DbMoveMandanten.move 051");
            return rc;
        }

        dbBundle.dbWeisungMeldung.reorgInterneIdent();

        if (mitDelete) {
            dbBundle.mandantenTablesGetrennt = false;
            dbBundle.dbWeisungMeldung.deleteAll();
            dbBundle.mandantenTablesGetrennt = true;
        }

        return 1;
    }

}
