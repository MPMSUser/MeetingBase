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
import de.meetingapps.meetingportal.meetComEntities.EclAktienregister;
import de.meetingapps.meetingportal.meetComEntities.EclAktienregisterZusatz;
import de.meetingapps.meetingportal.meetComEntities.EclAnrede;
import de.meetingapps.meetingportal.meetComEntities.EclMeldung;
import de.meetingapps.meetingportal.meetComEntities.EclPersonenNatJur;
import de.meetingapps.meetingportal.meetComEntities.EclPersonenNatJurVersandadresse;
import de.meetingapps.meetingportal.meetComEntities.EclPraesenzliste;
import de.meetingapps.meetingportal.meetComEntities.EclStaaten;
import de.meetingapps.meetingportal.meetComEntities.EclSuchergebnis;
import de.meetingapps.meetingportal.meetComEntities.EclWeisungMeldung;
import de.meetingapps.meetingportal.meetComEntities.EclWillenserklaerung;
import de.meetingapps.meetingportal.meetComEntities.EclWillenserklaerungZusatz;
import de.meetingapps.meetingportal.meetComKonst.KonstWillenserklaerung;

public class DbStatistik {

    private Connection verbindung = null;
    //	private DbBasis dbBasis=null;
    private DbBundle dbBundle = null;

    private EclMeldung ergebnisArrayMeldung[] = null;
    private EclMeldung ergebnisArrayMeldung1[] = null;
    private EclPersonenNatJur ergebnisArrayPersonenNatJur[] = null;
    private EclPersonenNatJurVersandadresse ergebnisArrayPersonenNatJurVersandadresse[] = null;
    private EclWillenserklaerung ergebnisArrayWillenserklaerung[] = null;
    private EclWillenserklaerungZusatz ergebnisArrayWillenserklaerungZusatz[] = null;
    private EclAnrede ergebnisArrayAnrede[] = null;
    private EclAnrede ergebnisArrayAnredeVersand[] = null;
    private EclStaaten ergebnisArrayStaaten[] = null;
    private EclStaaten ergebnisArrayStaaten1[] = null;
    private EclPersonenNatJur ergebnisArrayPersonenNatJur1[] = null;
    private String ergebnisArrayKey[] = null;
    private EclPraesenzliste ergebnisArrayPraesenzliste[] = null;
    private EclAktienregisterZusatz ergebnisArrayAktienregisterZusatz[] = null;
    private EclAktienregister ergebnisArrayAktienregisterEintrag[] = null;
    private EclWeisungMeldung ergebnisArrayWeisungMeldung[] = null;
    private EclSuchergebnis ergebnisArraySuchergebnis[] = null;

    /*************************Initialisierung***************************/
    public DbStatistik(DbBundle pDbBundle) {
        /* Verbindung in lokale Daten eintragen*/
        if (pDbBundle == null) {
            CaBug.drucke("DbAktienregisterZusatz.init 001 - dbBundle nicht initialisiert");
            return;
        }
        if (pDbBundle.dbBasis == null) {
            CaBug.drucke("DbAktienregisterZusatz.init 002 - dbBasis nicht initialisiert");
            return;
        }

        //		dbBasis=pDbBundle.dbBasis;
        verbindung = pDbBundle.dbBasis.verbindung;
        dbBundle = pDbBundle;
    }

    /**************Anzahl der Ergebnisse der read*-Methoden**************************/
    public int anzErgebnisMeldung() {
        if (ergebnisArrayMeldung == null) {
            return 0;
        }
        return ergebnisArrayMeldung.length;
    }

    public int anzErgebnisMeldung1() {
        if (ergebnisArrayMeldung1 == null) {
            return 0;
        }
        return ergebnisArrayMeldung1.length;
    }

    public int anzErgebnisPersonenNatJur() {
        if (ergebnisArrayPersonenNatJur == null) {
            return 0;
        }
        return ergebnisArrayPersonenNatJur.length;
    }

    public int anzErgebnisPersonenNatJurVersandadresse() {
        if (ergebnisArrayPersonenNatJurVersandadresse == null) {
            return 0;
        }
        return ergebnisArrayPersonenNatJurVersandadresse.length;
    }

    public int anzErgebnisWillenserklaerung() {
        if (ergebnisArrayWillenserklaerung == null) {
            return 0;
        }
        return ergebnisArrayWillenserklaerung.length;
    }

    public int anzErgebnisWillenserklaerungZusatz() {
        if (ergebnisArrayWillenserklaerungZusatz == null) {
            return 0;
        }
        return ergebnisArrayWillenserklaerungZusatz.length;
    }

    public int anzErgebnisAnrede() {
        if (ergebnisArrayAnrede == null) {
            return 0;
        }
        return ergebnisArrayAnrede.length;
    }

    public int anzErgebnisAnredeVersand() {
        if (ergebnisArrayAnredeVersand == null) {
            return 0;
        }
        return ergebnisArrayAnredeVersand.length;
    }

    public int anzErgebnisStaaten() {
        if (ergebnisArrayStaaten == null) {
            return 0;
        }
        return ergebnisArrayStaaten.length;
    }

    public int anzErgebnisStaaten1() {
        if (ergebnisArrayStaaten1 == null) {
            return 0;
        }
        return ergebnisArrayStaaten1.length;
    }

    public int anzErgebnisPersonenNatJur1() {
        if (ergebnisArrayPersonenNatJur1 == null) {
            return 0;
        }
        return ergebnisArrayPersonenNatJur1.length;
    }

    public int anzErgebnisKey() {
        if (ergebnisArrayKey == null) {
            return 0;
        }
        return ergebnisArrayKey.length;
    }

    public int anzErgebnisPraesenzliste() {
        if (ergebnisArrayPraesenzliste == null) {
            return 0;
        }
        return ergebnisArrayPraesenzliste.length;
    }

    public int anzErgebnisAktienregisterZusatz() {
        if (ergebnisArrayAktienregisterZusatz == null) {
            return 0;
        }
        return ergebnisArrayAktienregisterZusatz.length;
    }

    public int anzErgebnisAktienregisterEintrag() {
        if (ergebnisArrayAktienregisterEintrag == null) {
            return 0;
        }
        return ergebnisArrayAktienregisterEintrag.length;
    }

    public int anzErgebnisWeisungMeldung() {
        if (ergebnisArrayWeisungMeldung == null) {
            return 0;
        }
        return ergebnisArrayWeisungMeldung.length;
    }

    public int anzErgebnisSuchergebnis() {
        if (ergebnisArraySuchergebnis == null) {
            return 0;
        }
        return ergebnisArraySuchergebnis.length;
    }

    /**********Liefert pN-tes Element des Ergebnisses der read*Methoden**************
     * pN geht von 0 bis anzErgebnis-1*/
    public EclMeldung ergebnisMeldungPosition(int pN) {
        if (ergebnisArrayMeldung == null) {
            CaBug.drucke("DbJoined.ergebnisMeldungPosition 001");
            return null;
        }
        if (pN < 0) {
            CaBug.drucke("DbJoined.ergebnisMeldungPosition 002");
            return null;
        }
        if (pN >= ergebnisArrayMeldung.length) {
            CaBug.drucke("DbJoined.ergebnisMeldungPosition 003");
            return null;
        }
        return ergebnisArrayMeldung[pN];
    }

    public EclMeldung ergebnisMeldung1Position(int pN) {
        if (ergebnisArrayMeldung1 == null) {
            CaBug.drucke("DbJoined.ergebnisMeldung1Position 001");
            return null;
        }
        if (pN < 0) {
            CaBug.drucke("DbJoined.ergebnisMeldung1Position 002");
            return null;
        }
        if (pN >= ergebnisArrayMeldung.length) {
            CaBug.drucke("DbJoined.ergebnisMeldung1Position 003");
            return null;
        }
        return ergebnisArrayMeldung1[pN];
    }

    public EclPersonenNatJur ergebnisPersonenNatJurPosition(int pN) {
        if (ergebnisArrayPersonenNatJur == null) {
            CaBug.drucke("DbJoined.ergebnisPersonenNatJurPosition 001");
            return null;
        }
        if (pN < 0) {
            CaBug.drucke("DbJoined.ergebnisPersonenNatJurPosition 002");
            return null;
        }
        if (pN >= ergebnisArrayPersonenNatJur.length) {
            CaBug.drucke("DbJoined.ergebnisPersonenNatJurPosition 003");
            return null;
        }
        return ergebnisArrayPersonenNatJur[pN];
    }

    public EclPersonenNatJurVersandadresse ergebnisPersonenNatJurVersandadressePosition(int pN) {
        if (ergebnisArrayPersonenNatJurVersandadresse == null) {
            CaBug.drucke("DbJoined.ergebnisPersonenNatJurVersandadressePosition 001");
            return null;
        }
        if (pN < 0) {
            CaBug.drucke("DbJoined.ergebnisPersonenNatJurVersandadressePosition 002");
            return null;
        }
        if (pN >= ergebnisArrayPersonenNatJurVersandadresse.length) {
            CaBug.drucke("DbJoined.ergebnisPersonenNatJurVersandadressePosition 003");
            return null;
        }
        return ergebnisArrayPersonenNatJurVersandadresse[pN];
    }

    public EclWillenserklaerung ergebnisWillenserklaerungPosition(int pN) {
        if (ergebnisArrayWillenserklaerung == null) {
            CaBug.drucke("DbJoined.ergebnisWillenserklaerung 001");
            return null;
        }
        if (pN < 0) {
            CaBug.drucke("DbJoined.ergebnisWillenserklaerung 002");
            return null;
        }
        if (pN >= ergebnisArrayWillenserklaerung.length) {
            CaBug.drucke("DbJoined.ergebnisWillenserklaerung 003");
            return null;
        }
        return ergebnisArrayWillenserklaerung[pN];
    }

    public EclWillenserklaerungZusatz ergebnisWillenserklaerungZusatzPosition(int pN) {
        if (ergebnisArrayWillenserklaerungZusatz == null) {
            CaBug.drucke("DbJoined.ergebnisWillenserklaerungZusatz 001");
            return null;
        }
        if (pN < 0) {
            CaBug.drucke("DbJoined.ergebnisWillenserklaerungZusatz 002");
            return null;
        }
        if (pN >= ergebnisArrayWillenserklaerungZusatz.length) {
            CaBug.drucke("DbJoined.ergebnisWillenserklaerungZusatz 003");
            return null;
        }
        return ergebnisArrayWillenserklaerungZusatz[pN];
    }

    public EclAnrede ergebnisAnredePosition(int pN) {
        if (ergebnisArrayAnrede == null) {
            CaBug.drucke("DbJoined.ergebnisAnredePosition 001");
            return null;
        }
        if (pN < 0) {
            CaBug.drucke("DbJoined.ergebnisAnredePosition 002");
            return null;
        }
        if (pN >= ergebnisArrayAnrede.length) {
            CaBug.drucke("DbJoined.ergebnisAnredePosition 003");
            return null;
        }
        return ergebnisArrayAnrede[pN];
    }

    public EclAnrede ergebnisAnredeVersandPosition(int pN) {
        if (ergebnisArrayAnredeVersand == null) {
            CaBug.drucke("DbJoined.ergebnisAnredeVersandPosition 001");
            return null;
        }
        if (pN < 0) {
            CaBug.drucke("DbJoined.ergebnisAnredeVersandPosition 002");
            return null;
        }
        if (pN >= ergebnisArrayAnredeVersand.length) {
            CaBug.drucke("DbJoined.ergebnisAnredeVersandPosition 003");
            return null;
        }
        return ergebnisArrayAnredeVersand[pN];
    }

    public EclStaaten ergebnisStaatenPosition(int pN) {
        if (ergebnisArrayStaaten == null) {
            CaBug.drucke("DbJoined.ergebnisStaatenPosition 001");
            return null;
        }
        if (pN < 0) {
            CaBug.drucke("DbJoined.ergebnisStaatenPosition 002");
            return null;
        }
        if (pN >= ergebnisArrayStaaten.length) {
            CaBug.drucke("DbJoined.ergebnisStaatenPosition 003");
            return null;
        }
        return ergebnisArrayStaaten[pN];
    }

    public EclStaaten ergebnisStaaten1Position(int pN) {
        if (ergebnisArrayStaaten1 == null) {
            CaBug.drucke("DbJoined.ergebnisStaaten1Position 001");
            return null;
        }
        if (pN < 0) {
            CaBug.drucke("DbJoined.ergebnisStaaten1Position 002");
            return null;
        }
        if (pN >= ergebnisArrayStaaten.length) {
            CaBug.drucke("DbJoined.ergebnisStaaten1Position 003");
            return null;
        }
        return ergebnisArrayStaaten1[pN];
    }

    public EclPersonenNatJur ergebnisPersonenNatJur1Position(int pN) {
        if (ergebnisArrayPersonenNatJur1 == null) {
            CaBug.drucke("DbJoined.ergebnisPersonenNatJur1Position 001");
            return null;
        }
        if (pN < 0) {
            CaBug.drucke("DbJoined.ergebnisPersonenNatJur1Position 002");
            return null;
        }
        if (pN >= ergebnisArrayPersonenNatJur1.length) {
            CaBug.drucke("DbJoined.ergebnisPersonenNatJur1Position 003");
            return null;
        }
        return ergebnisArrayPersonenNatJur1[pN];
    }

    public String ergebnisKeyPosition(int pN) {
        if (ergebnisArrayKey == null) {
            CaBug.drucke("DbJoined.ergebnisArrayKeyPosition 001");
            return null;
        }
        if (pN < 0) {
            CaBug.drucke("DbJoined.ergebnisArrayKeyPosition 002");
            return null;
        }
        if (pN >= ergebnisArrayKey.length) {
            CaBug.drucke("DbJoined.ergebnisArrayKeyPosition 003");
            return null;
        }
        return ergebnisArrayKey[pN];
    }

    public EclPraesenzliste ergebnisPraesenzlistePosition(int pN) {
        if (ergebnisArrayPraesenzliste == null) {
            CaBug.drucke("DbJoined.ergebnisPraesenzlistePosition 001");
            return null;
        }
        if (pN < 0) {
            CaBug.drucke("DbJoined.ergebnisPraesenzlistePosition 002");
            return null;
        }
        if (pN >= ergebnisArrayPraesenzliste.length) {
            CaBug.drucke("DbJoined.ergebnisPraesenzlistePosition 003");
            return null;
        }
        return ergebnisArrayPraesenzliste[pN];
    }

    public EclAktienregisterZusatz ergebnisAktienregisterZusatzPosition(int pN) {
        if (ergebnisArrayAktienregisterZusatz == null) {
            CaBug.drucke("DbJoined.ergebnisAktienregisterZusatzPosition 001");
            return null;
        }
        if (pN < 0) {
            CaBug.drucke("DbJoined.ergebnisAktienregisterZusatzPosition 002");
            return null;
        }
        if (pN >= ergebnisArrayAktienregisterZusatz.length) {
            CaBug.drucke("DbJoined.ergebnisAktienregisterZusatzPosition 003");
            return null;
        }
        return ergebnisArrayAktienregisterZusatz[pN];
    }

    public EclAktienregister ergebnisAktienregisterEintragPosition(int pN) {
        if (ergebnisArrayAktienregisterEintrag == null) {
            CaBug.drucke("DbJoined.ergebnisAktienregisterEintragPosition 001");
            return null;
        }
        if (pN < 0) {
            CaBug.drucke("DbJoined.ergebnisAktienregisterEintragPosition 002");
            return null;
        }
        if (pN >= ergebnisArrayAktienregisterEintrag.length) {
            CaBug.drucke("DbJoined.ergebnisAktienregisterEintragPosition 003");
            return null;
        }
        return ergebnisArrayAktienregisterEintrag[pN];
    }

    public EclWeisungMeldung ergebnisWeisungMeldungPosition(int pN) {
        if (ergebnisArrayWeisungMeldung == null) {
            CaBug.drucke("DbJoined.ergebnisWeisungMeldungPosition 001");
            return null;
        }
        if (pN < 0) {
            CaBug.drucke("DbJoined.ergebnisWeisungMeldungPosition 002");
            return null;
        }
        if (pN >= ergebnisArrayWeisungMeldung.length) {
            CaBug.drucke("DbJoined.ergebnisWeisungMeldungWeisungMeldungWeisungMeldungPosition 003");
            return null;
        }
        return ergebnisArrayWeisungMeldung[pN];
    }

    public EclSuchergebnis ergebnisSuchergebnisPosition(int pN) {
        if (ergebnisArraySuchergebnis == null) {
            CaBug.drucke("DbJoined.ergebnisSuchergebnisPosition 001");
            return null;
        }
        if (pN < 0) {
            CaBug.drucke("DbJoined.ergebnisSuchergebnisPosition 002");
            return null;
        }
        if (pN >= ergebnisArraySuchergebnis.length) {
            CaBug.drucke("DbJoined.ergebnisSuchergebnisPosition 003");
            return null;
        }
        return ergebnisArraySuchergebnis[pN];
    }

    /**********Liefert Liste des Ergebnisses der read*Methoden**************/
    public EclMeldung[] ergebnisMeldung() {
        if (ergebnisArrayMeldung == null) {
            CaBug.drucke("DbJoined.ergebnisMeldung 001");
        }
        return ergebnisArrayMeldung;
    }

    public EclMeldung[] ergebnisMeldung1() {
        if (ergebnisArrayMeldung1 == null) {
            CaBug.drucke("DbJoined.ergebnisMeldung1 001");
        }
        return ergebnisArrayMeldung1;
    }

    public EclPersonenNatJur[] ergebnisPersonenNatJur() {
        if (ergebnisArrayPersonenNatJur == null) {
            CaBug.drucke("DbJoined.ergebnisPersonenNatJur 001");
        }
        return ergebnisArrayPersonenNatJur;
    }

    public EclPersonenNatJurVersandadresse[] ergebnisPersonenNatJurVersandadresse() {
        if (ergebnisArrayPersonenNatJurVersandadresse == null) {
            CaBug.drucke("DbJoined.ergebnisPersonenNatJurVersandadresse 001");
        }
        return ergebnisArrayPersonenNatJurVersandadresse;
    }

    public EclWillenserklaerung[] ergebnisWillenserklaerung() {
        if (ergebnisArrayWillenserklaerung == null) {
            CaBug.drucke("DbJoined.ergebnisWillenserklaerung 001");
        }
        return ergebnisArrayWillenserklaerung;
    }

    public EclWillenserklaerungZusatz[] ergebnisWillenserklaerungZusatz() {
        if (ergebnisArrayWillenserklaerungZusatz == null) {
            CaBug.drucke("DbJoined.ergebnisWillenserklaerungZusatz 001");
        }
        return ergebnisArrayWillenserklaerungZusatz;
    }

    public EclAnrede[] ergebnisAnrede() {
        if (ergebnisArrayAnrede == null) {
            CaBug.drucke("DbJoined.ergebnisAnrede 001");
        }
        return ergebnisArrayAnrede;
    }

    public EclAnrede[] ergebnisAnredeVersand() {
        if (ergebnisArrayAnredeVersand == null) {
            CaBug.drucke("DbJoined.ergebnisAnredeVersand 001");
        }
        return ergebnisArrayAnredeVersand;
    }

    public EclStaaten[] ergebnisStaaten() {
        if (ergebnisArrayStaaten == null) {
            CaBug.drucke("DbJoined.ergebnisStaaten 001");
        }
        return ergebnisArrayStaaten;
    }

    public EclStaaten[] ergebnisStaaten1() {
        if (ergebnisArrayStaaten1 == null) {
            CaBug.drucke("DbJoined.ergebnisStaaten1 001");
        }
        return ergebnisArrayStaaten1;
    }

    public EclPersonenNatJur[] ergebnisPersonenNatJur1() {
        if (ergebnisArrayPersonenNatJur1 == null) {
            CaBug.drucke("DbJoined.ergebnisPersonenNatJur1 001");
        }
        return ergebnisArrayPersonenNatJur1;
    }

    public String[] ergebnisKey() {
        if (ergebnisArrayKey == null) {
            CaBug.drucke("DbJoined.ergebnisKey 001");
        }
        return ergebnisArrayKey;
    }

    public EclPraesenzliste[] ergebnisPraesenzliste() {
        if (ergebnisArrayPraesenzliste == null) {
            CaBug.drucke("DbJoined.ergebnisPraesenzliste 001");
        }
        return ergebnisArrayPraesenzliste;
    }

    public EclAktienregisterZusatz[] ergebnisAktienregisterZusatz() {
        if (ergebnisArrayAktienregisterZusatz == null) {
            CaBug.drucke("DbJoined.ergebnisAktienregisterZusatz 001");
        }
        return ergebnisArrayAktienregisterZusatz;
    }

    public EclAktienregister[] ergebnisAktienregisterEintrag() {
        if (ergebnisArrayAktienregisterEintrag == null) {
            CaBug.drucke("DbJoined.ergebnisAktienregisterEintrag 001");
        }
        return ergebnisArrayAktienregisterEintrag;
    }

    public EclWeisungMeldung[] ergebnisWeisungMeldung() {
        if (ergebnisArrayWeisungMeldung == null) {
            CaBug.drucke("DbJoined.ergebnisWeisungMeldung 001");
        }
        return ergebnisArrayWeisungMeldung;
    }

    public EclSuchergebnis[] ergebnisSuchergebnis() {
        if (ergebnisArraySuchergebnis == null) {
            CaBug.drucke("DbJoined.ergebnisSuchergebnis 001");
        }
        return ergebnisArraySuchergebnis;
    }

    /**Liefert alle meldungen / Willenserklärungen für noch nicht gedruckte Eintrittskarten (mit Versandart Post) als
     * Basis zum Kontrollieren der Versandadresse
     * 
     * @param nurInternet: 
     * 		true=liefert nur die über das Internet vom Aktionär selbst eingegebenen Bestellungen
     * 		false=liefert alle
     * @param nurNochNichtGeprueft
     * 		true=liefert nur die noch nicht geprüften Bestellungen
     * 		false=liefert auch die bereits geprüften Bestellungen (soweit sie noch nicht versandt wurden)
     * @param nurAbweichendeEingegeben
     * 		true=liefert nur die Bestellungen, bei denen manuell eine abweichende Versandadresse eingegeben wurde
     * 		false=liefert alle
     * @return
     */
    public int read_versandadressePruefen(boolean nurInternet, boolean nurNochNichtGeprueft,
            boolean nurAbweichendeEingegeben) {

        int anzInArray = 0;
        PreparedStatement lPStm = null;

        String lSql =
                //				"SELECT m.*, p.*, st.*, pv.*, st1.*, wz.*, w.*, an.*, anf.* FROM (((tbl_PersonenNatJur p "
                //				+"INNER JOIN (tbl_meldungen m INNER JOIN (tbl_willenserklaerungzusatz wz INNER JOIN tbl_willenserklaerung w ON wz.willenserklaerungIdent=w.willenserklaerungIdent) " 
                //			    +"ON m.meldungsIdent=wz.meldungsIdent) ON m.personenNatJurIdent=p.ident) "
                //			    + "LEFT OUTER JOIN tbl_personenNatJurVersandadresse pv ON (p.identVersandadresse=pv.ident AND p.mandant=pv.mandant)) LEFT OUTER JOIN tbl_anreden an ON p.anrede=an.anredennr) "
                //			    + "LEFT OUTER JOIN tbl_anredenfremd anf ON an.anredennr=anf.anredennr "
                //			    + "LEFT OUTER JOIN tbl_staaten st on p.land=st.code "
                //			    + "LEFT OUTER JOIN tbl_staaten st1 on pv.staatIdVersand=st1.id "
                //			    +"WHERE m.mandant=? AND m.meldungAktiv=1 AND p.mandant=? AND wz.mandant=? AND w.mandant=? AND wz.willenserklaerung=100 AND wz.anmeldungIstStorniert!=1 AND (wz.versandartEK=1 OR wz.versandartEK=2) AND anf.sprachennr=2 ";
                "SELECT m.*, p.*, st.*, pv.*, are.*, st1.*, wz.*, w.*, an.*, anf.*, anv.*, anfv.* FROM ((("
                        + dbBundle.getSchemaMandant() + "tbl_PersonenNatJur p " + "INNER JOIN ("
                        + dbBundle.getSchemaMandant() + "tbl_meldungen m INNER JOIN (" + dbBundle.getSchemaMandant()
                        + "tbl_willenserklaerungzusatz wz INNER JOIN " + dbBundle.getSchemaMandant() + ""
                        + dbBundle.getSchemaMandant()
                        + "tbl_willenserklaerung w ON wz.willenserklaerungIdent=w.willenserklaerungIdent) "
                        + "ON m.meldungsIdent=wz.meldungsIdent) ON m.personenNatJurIdent=p.ident) " + "LEFT OUTER JOIN "
                        + dbBundle.getSchemaMandant()
                        + "tbl_aktienregister are ON m.aktionaersnummer=are.aktionaersnummer " + "LEFT OUTER JOIN "
                        + dbBundle.getSchemaMandant()
                        + "tbl_personenNatJurVersandadresse pv ON (p.identVersandadresse=pv.ident AND p.mandant=pv.mandant)) LEFT OUTER JOIN "
                        + dbBundle.getSchemaAllgemein() + "tbl_anreden an ON p.anrede=an.anredennr) "
                        + "LEFT OUTER JOIN " + dbBundle.getSchemaAllgemein()
                        + "tbl_anredenfremd anf ON an.anredennr=anf.anredennr " + " LEFT OUTER JOIN "
                        + dbBundle.getSchemaAllgemein() + "tbl_anreden anv on pv.anredeIdVersand=anv.anredennr "
                        + " LEFT OUTER JOIN " + dbBundle.getSchemaAllgemein()
                        + "tbl_anredenfremd anfv on anv.anredennr=anfv.anredennr " + "LEFT OUTER JOIN "
                        + dbBundle.getSchemaAllgemein() + "tbl_staaten st on p.land=st.code " + "LEFT OUTER JOIN "
                        + dbBundle.getSchemaAllgemein() + "tbl_staaten st1 on pv.staatIdVersand=st1.id "
                        + "WHERE m.mandant=? AND m.meldungAktiv=1 AND p.mandant=? AND wz.mandant=? AND w.mandant=? AND are.mandant=? AND wz.willenserklaerung=100 AND wz.anmeldungIstStorniert!=1 AND "
                        + "(wz.versandartEK=1 OR wz.versandartEK=2) AND anf.sprachennr=2 AND (anfv.sprachennr=2  || isnull(anfv.sprachennr))";
        /*
        "SELECT m.*, p.*, wz.*, w.* FROM tbl_meldungen m "
        +"INNER JOIN (tbl_PersonenNatJur p INNER JOIN (tbl_willenserklaerungzusatz wz INNER JOIN tbl_willenserklaerung w ON wz.willenserklaerungIdent=w.willenserklaerungIdent) " 
        +"ON p.ident=wz.meldungsIdent) ON m.personenNatJurIdent=p.ident "
        +"WHERE m.mandant=? AND p.mandant=? AND wz.mandant=? AND w.mandant=? AND wz.willenserklaerung=100 AND wz.anmeldungIstStorniert!=-1 AND (wz.versandartEK=1 OR wz.versandartEK=2) ";*/
        if (nurInternet) {
            lSql = lSql + "AND w.erteiltAufWeg>=21 AND w.erteiltAufWeg<=29 ";
        }
        if (nurNochNichtGeprueft) {
            lSql = lSql + " AND wz.versandadresseUeberprueft=0 ";
        }
        if (nurAbweichendeEingegeben) {
            lSql = lSql + " AND wz.versandartEK=2";
        }
        lSql = lSql + ";";
        //			System.out.println(lSql);
        try {
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, dbBundle.clGlobalVar.mandant);
            lPStm.setInt(2, dbBundle.clGlobalVar.mandant);
            lPStm.setInt(3, dbBundle.clGlobalVar.mandant);
            lPStm.setInt(4, dbBundle.clGlobalVar.mandant);
            lPStm.setInt(5, dbBundle.clGlobalVar.mandant);

            ResultSet lErgebnis = lPStm.executeQuery();
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();
            ergebnisArrayMeldung = new EclMeldung[anzInArray];
            ergebnisArrayPersonenNatJur = new EclPersonenNatJur[anzInArray];
            ergebnisArrayPersonenNatJurVersandadresse = new EclPersonenNatJurVersandadresse[anzInArray];
            ergebnisArrayWillenserklaerung = new EclWillenserklaerung[anzInArray];
            ergebnisArrayWillenserklaerungZusatz = new EclWillenserklaerungZusatz[anzInArray];
            ergebnisArrayAnrede = new EclAnrede[anzInArray];
            ergebnisArrayAnredeVersand = new EclAnrede[anzInArray];
            ergebnisArrayAnredeVersand = new EclAnrede[anzInArray];
            ergebnisArrayStaaten = new EclStaaten[anzInArray];
            ergebnisArrayStaaten1 = new EclStaaten[anzInArray];
            ergebnisArrayAktienregisterEintrag = new EclAktienregister[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {
                ergebnisArrayMeldung[i] = dbBundle.dbMeldungen.decodeErgebnis(lErgebnis);
                ergebnisArrayPersonenNatJur[i] = dbBundle.dbPersonenNatJur.decodeErgebnis(lErgebnis);
                ergebnisArrayPersonenNatJurVersandadresse[i] = dbBundle.dbPersonenNatJurVersandadresse
                        .decodeErgebnis(lErgebnis);
                ergebnisArrayWillenserklaerung[i] = dbBundle.dbWillenserklaerung.decodeErgebnis(lErgebnis);
                ergebnisArrayWillenserklaerungZusatz[i] = dbBundle.dbWillenserklaerungZusatz.decodeErgebnis(lErgebnis);
                ergebnisArrayAnrede[i] = dbBundle.dbAnreden.decodeErgebnis(lErgebnis);
                ergebnisArrayAnredeVersand[i] = dbBundle.dbAnreden.decodeErgebnisVersand(lErgebnis);
                ergebnisArrayStaaten[i] = dbBundle.dbStaaten.decodeErgebnis(lErgebnis);
                ergebnisArrayStaaten1[i] = dbBundle.dbStaaten.decodeErgebnis1(lErgebnis);
                ergebnisArrayAktienregisterEintrag[i] = dbBundle.dbAktienregister.decodeErgebnis(lErgebnis);
                i++;
            }
            lErgebnis.close();
            lPStm.close();

        } catch (SQLException e) {
            CaBug.drucke("DbJoined.read_versandadressePruefen 003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    /**Liefert alle meldungen / Willenserklärungen für den Eintrittskartendruck
     * 
     * @param erstDruck: 
     * 		true=Erstdruck - es werden alle noch nicht gedruckten Eintrittskarten mit Versandart 1 oder 2 geliefert
     * 		false=Druckwiederholung, in drucklauf wird die Nummer des zu wiederholenden Laufes übergeben
     * @param drucklauf
     * 		Nummer des zu wiederholenden Drucklaufs (falls erstDruck==false)
     * @param nurGepruefte
     * 		true=falls manuell eingegebene Versandadressen vorhanden, dann werden die nur gedruckt wenn sie geprüft wurden
     * 		false=auch ungeprüfte werden gedruckt
     * 
     * ekNummer: wenn dieser Wert enthält, werden erstDruck, drucklauf, nurGepruefte ignoriert, und die Daten für exakt diese
     * Eintrittskartennummer zurückgeliefert. Dient z.B. zum Nachdruck einer einzelnen Eintrittskarte (auch auf HV)
     * @return
     */
    public int read_eintrittskartenDruck(boolean erstDruck, int drucklauf, boolean nurGepruefte, String ekNummer,
            String ekNummerNeben, int pGastOderAktionaer) {

        int anzInArray = 0;
        PreparedStatement lPStm = null;
        String lSql = "";

        if (ekNummer.isEmpty()) {
            lSql =
                    //				"SELECT m.*, p.*, p1.*, st.*, pv.*, st1.*, wz.*, w.*, an.*, anf.* FROM (((tbl_PersonenNatJur p "
                    //				+"INNER JOIN (tbl_meldungen m INNER JOIN (tbl_willenserklaerungzusatz wz INNER JOIN tbl_willenserklaerung w ON wz.willenserklaerungIdent=w.willenserklaerungIdent) " 
                    //			    +"ON m.meldungsIdent=wz.meldungsIdent) ON m.personenNatJurIdent=p.ident) "
                    //			    + "LEFT OUTER JOIN tbl_personenNatJurVersandadresse pv ON (p.identVersandadresse=pv.ident AND p.mandant=pv.mandant)) LEFT OUTER JOIN tbl_anreden an ON p.anrede=an.anredennr) "
                    //			    + "LEFT OUTER JOIN tbl_anredenfremd anf ON an.anredennr=anf.anredennr "
                    //			    + "LEFT OUTER JOIN tbl_staaten st on p.land=st.code "
                    //			    + "LEFT OUTER JOIN tbl_staaten st1 on pv.staatIdVersand=st1.id "
                    //			    + "LEFT OUTER JOIN tbl_personenNatJur p1 on (wz.identVertreterPersonNatJur=p1.ident AND wz.mandant=p1.mandant) "
                    //			    +"WHERE m.mandant=? AND p.mandant=? AND m.meldungAktiv=1 AND wz.mandant=? AND w.mandant=? AND wz.willenserklaerung=100 AND wz.anmeldungIstStorniert!=1 AND (wz.versandartEK=1 OR wz.versandartEK=2) AND anf.sprachennr=2 ";
                    "SELECT m.*, p.*, p1.*, st.*, pv.*, are.*, st1.*, wz.*, w.*, an.*, anf.*, anv.*, anfv.* FROM ((("
                            + dbBundle.getSchemaMandant() + "tbl_PersonenNatJur p " + "INNER JOIN ("
                            + dbBundle.getSchemaMandant() + "tbl_meldungen m INNER JOIN (" + dbBundle.getSchemaMandant()
                            + "tbl_willenserklaerungzusatz wz INNER JOIN " + dbBundle.getSchemaMandant()
                            + "tbl_willenserklaerung w ON wz.willenserklaerungIdent=w.willenserklaerungIdent) ";
            if (pGastOderAktionaer == 2) {
                lSql = lSql + "ON m.meldungsIdent=wz.meldungsIdent)";
            } else {
                lSql = lSql + "ON m.meldungsIdent=wz.meldungsIdentGast)";
            }
            lSql = lSql + " ON m.personenNatJurIdent=p.ident) " + "LEFT OUTER JOIN " + dbBundle.getSchemaMandant()
                    + "tbl_personenNatJurVersandadresse pv ON (p.identVersandadresse=pv.ident AND p.mandant=pv.mandant)) LEFT OUTER JOIN "
                    + dbBundle.getSchemaAllgemein() + "tbl_anreden an ON p.anrede=an.anredennr) " + "LEFT OUTER JOIN "
                    + dbBundle.getSchemaMandant() + "tbl_aktienregister are ON m.aktionaersnummer=are.aktionaersnummer "
                    + "LEFT OUTER JOIN " + dbBundle.getSchemaAllgemein()
                    + "tbl_anredenfremd anf ON an.anredennr=anf.anredennr " + " LEFT OUTER JOIN "
                    + dbBundle.getSchemaAllgemein() + "tbl_anreden anv on pv.anredeIdVersand=anv.anredennr "
                    + " LEFT OUTER JOIN " + dbBundle.getSchemaAllgemein()
                    + "tbl_anredenfremd anfv on anv.anredennr=anfv.anredennr " + "LEFT OUTER JOIN "
                    + dbBundle.getSchemaAllgemein() + "tbl_staaten st on p.land=st.code " + "LEFT OUTER JOIN "
                    + dbBundle.getSchemaAllgemein() + "tbl_staaten st1 on pv.staatIdVersand=st1.id "
                    + "LEFT OUTER JOIN " + dbBundle.getSchemaMandant()
                    + "tbl_personenNatJur p1 on (wz.identVertreterPersonNatJur=p1.ident AND wz.mandant=p1.mandant) "
                    + "WHERE m.mandant=? AND p.mandant=? AND m.meldungAktiv=1 AND wz.mandant=? AND w.mandant=? AND "
                    + "(are.mandant=? OR isnull(are.mandant)) AND " /*Geändert!*/
                    + "wz.willenserklaerung=100 AND wz.anmeldungIstStorniert!=1 AND (wz.versandartEK=1 OR wz.versandartEK=2) AND "
                    + "(anf.sprachennr=2 OR isnull(anf.sprachennr)) AND (anfv.sprachennr=2 OR isnull(anfv.sprachennr)) "; /*Geändert - auch || in OR in der zweiten Bedingung*/
            if (erstDruck) {
                lSql = lSql + "AND wz.eintrittskarteWurdeGedruckt=0 ";
            } else {
                lSql = lSql + "AND wz.eintrittskarteWurdeGedruckt=? ";
            }
            if (nurGepruefte) {
                lSql = lSql + " AND (wz.versandadresseUeberprueft!=0 OR wz.versandartEK!=2) ";
            }
            lSql = lSql + " ORDER BY w.benutzernr, w.willenserklaerungIdent;";
        } else {/*Nur einzelne bestimmte EK drucken*/
            lSql = "SELECT m.*, p.*, p1.*, st.*, pv.*, are.*, st1.*, wz.*, w.*, an.*, anf.*, anv.*, anfv.* FROM ((("
                    + dbBundle.getSchemaMandant() + "tbl_PersonenNatJur p " + "INNER JOIN ("
                    + dbBundle.getSchemaMandant() + "tbl_meldungen m INNER JOIN (" + dbBundle.getSchemaMandant()
                    + "tbl_willenserklaerungzusatz wz INNER JOIN " + dbBundle.getSchemaMandant()
                    + "tbl_willenserklaerung w ON wz.willenserklaerungIdent=w.willenserklaerungIdent) ";
            if (pGastOderAktionaer == 2) {
                lSql = lSql + "ON m.meldungsIdent=wz.meldungsIdent)";
            } else {
                lSql = lSql + "ON m.meldungsIdent=wz.meldungsIdentGast)";
            }
            lSql = lSql + " ON m.personenNatJurIdent=p.ident) " + "LEFT OUTER JOIN " + dbBundle.getSchemaMandant()
                    + "tbl_personenNatJurVersandadresse pv ON (p.identVersandadresse=pv.ident AND p.mandant=pv.mandant)) LEFT OUTER JOIN "
                    + dbBundle.getSchemaAllgemein() + "tbl_anreden an ON p.anrede=an.anredennr) " + "LEFT OUTER JOIN "
                    + dbBundle.getSchemaMandant() + "tbl_aktienregister are ON m.aktionaersnummer=are.aktionaersnummer "
                    + "LEFT OUTER JOIN " + dbBundle.getSchemaAllgemein()
                    + "tbl_anredenfremd anf ON an.anredennr=anf.anredennr " + " LEFT OUTER JOIN "
                    + dbBundle.getSchemaAllgemein() + "tbl_anreden anv on pv.anredeIdVersand=anv.anredennr "
                    + " LEFT OUTER JOIN " + dbBundle.getSchemaAllgemein()
                    + "tbl_anredenfremd anfv on anv.anredennr=anfv.anredennr " + "LEFT OUTER JOIN "
                    + dbBundle.getSchemaAllgemein() + "tbl_staaten st on p.land=st.code " + "LEFT OUTER JOIN "
                    + dbBundle.getSchemaAllgemein() + "tbl_staaten st1 on pv.staatIdVersand=st1.id "
                    + "LEFT OUTER JOIN " + dbBundle.getSchemaMandant()
                    + "tbl_personenNatJur p1 on (wz.identVertreterPersonNatJur=p1.ident AND wz.mandant=p1.mandant) "
                    + "WHERE m.mandant=? AND p.mandant=? AND m.meldungAktiv=1 AND wz.mandant=? AND w.mandant=? AND "
                    + "(are.mandant=?  OR isnull(are.mandant)) AND " /*Geändert*/
                    + "wz.willenserklaerung=100 AND wz.anmeldungIstStorniert!=1 AND w.zutrittsIdent=? AND w.zutrittsIdentNeben=? AND "
                    + "(anf.sprachennr=2 OR isnull(anf.sprachennr))  AND (anfv.sprachennr=2 OR isnull(anfv.sprachennr)) ";/*Geändert - auch || in OR in der zweiten Bedingung*/

        }

        System.out.println(lSql);
        try {
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, dbBundle.clGlobalVar.mandant);
            lPStm.setInt(2, dbBundle.clGlobalVar.mandant);
            lPStm.setInt(3, dbBundle.clGlobalVar.mandant);
            lPStm.setInt(4, dbBundle.clGlobalVar.mandant);
            lPStm.setInt(5, dbBundle.clGlobalVar.mandant);
            if (!ekNummer.isEmpty()) {
                lPStm.setString(6, ekNummer);
                lPStm.setString(7, ekNummerNeben);

            } else {
                if (erstDruck == false) {
                    lPStm.setInt(6, drucklauf);
                }
            }

            ResultSet lErgebnis = lPStm.executeQuery();
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();
            //	System.out.println("Joined anzInArray="+anzInArray);
            ergebnisArrayMeldung = new EclMeldung[anzInArray];
            ergebnisArrayPersonenNatJur = new EclPersonenNatJur[anzInArray];
            ergebnisArrayPersonenNatJurVersandadresse = new EclPersonenNatJurVersandadresse[anzInArray];
            ergebnisArrayWillenserklaerung = new EclWillenserklaerung[anzInArray];
            ergebnisArrayWillenserklaerungZusatz = new EclWillenserklaerungZusatz[anzInArray];
            ergebnisArrayAnrede = new EclAnrede[anzInArray];
            ergebnisArrayAnredeVersand = new EclAnrede[anzInArray];
            ergebnisArrayStaaten = new EclStaaten[anzInArray];
            ergebnisArrayStaaten1 = new EclStaaten[anzInArray];
            ergebnisArrayPersonenNatJur1 = new EclPersonenNatJur[anzInArray];
            ergebnisArrayAktienregisterEintrag = new EclAktienregister[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {
                ergebnisArrayMeldung[i] = dbBundle.dbMeldungen.decodeErgebnis(lErgebnis);
                ergebnisArrayPersonenNatJur[i] = dbBundle.dbPersonenNatJur.decodeErgebnis(lErgebnis);
                ergebnisArrayPersonenNatJurVersandadresse[i] = dbBundle.dbPersonenNatJurVersandadresse
                        .decodeErgebnis(lErgebnis);
                ergebnisArrayWillenserklaerung[i] = dbBundle.dbWillenserklaerung.decodeErgebnis(lErgebnis);
                ergebnisArrayWillenserklaerungZusatz[i] = dbBundle.dbWillenserklaerungZusatz.decodeErgebnis(lErgebnis);
                ergebnisArrayAnrede[i] = dbBundle.dbAnreden.decodeErgebnis(lErgebnis);
                ergebnisArrayAnredeVersand[i] = dbBundle.dbAnreden.decodeErgebnisVersand(lErgebnis);
                ergebnisArrayStaaten[i] = dbBundle.dbStaaten.decodeErgebnis(lErgebnis);
                ergebnisArrayStaaten1[i] = dbBundle.dbStaaten.decodeErgebnis1(lErgebnis);
                ergebnisArrayPersonenNatJur1[i] = dbBundle.dbPersonenNatJur.decodeErgebnis1(lErgebnis);
                ergebnisArrayAktienregisterEintrag[i] = dbBundle.dbAktienregister.decodeErgebnis(lErgebnis);
                i++;
            }
            lErgebnis.close();
            lPStm.close();

        } catch (SQLException e) {
            CaBug.drucke("DbJoined.read_eintrittskartenDruck 003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    /**
     * Einlesen aller Willenserklärungen einer bestimmten Art zu einer Meldung
     * @param pMeldeIdent
     * Meldung, zu der die Willenserklärungen eingelesen werden sollen
     * @param pWillenserklaerungArt
     * Art der Willenserklärungen, die geliefert werden sollen
     * @param selektion
     * 1=nur gültige
     * 2=nur stornierte
     * 3=alle
     * @return
     */
    public int read_willenserklaerungArtZuAktionaer(int pMeldeIdent,
            /*EnWillenserklaerung pWillenserklaerungArt*/int pWillenserklaerungArt, int selektion) {
        int anzInArray = 0;
        PreparedStatement lPStm = null;

        String lSql = "SELECT w.*, wz.* FROM " + dbBundle.getSchemaMandant()
                + "tbl_willenserklaerungzusatz wz INNER JOIN " + dbBundle.getSchemaMandant()
                + "tbl_willenserklaerung w ON wz.willenserklaerungIdent=w.willenserklaerungIdent "
                + "WHERE wz.mandant=? AND w.mandant=? AND wz.willenserklaerung=? AND wz.meldungsIdent=? ";
        switch (selektion) {
        case 1: {
            lSql = lSql + "AND wz.anmeldungIstStorniert!=1 ";
            break;
        }
        case 2: {
            lSql = lSql + "AND wz.anmeldungIstStorniert=1 ";
            break;
        }
        }
        lSql = lSql + "ORDER BY wz.willenserklaerungIdent;";
        System.out.println(lSql);
        try {
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, dbBundle.clGlobalVar.mandant);
            lPStm.setInt(2, dbBundle.clGlobalVar.mandant);
            lPStm.setInt(3, pWillenserklaerungArt);
            //			lPStm.setInt(3, EnWillenserklaerung.toClWillenserklaerung(pWillenserklaerungArt));
            lPStm.setInt(4, pMeldeIdent);

            ResultSet lErgebnis = lPStm.executeQuery();
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();
            ergebnisArrayWillenserklaerung = new EclWillenserklaerung[anzInArray];
            ergebnisArrayWillenserklaerungZusatz = new EclWillenserklaerungZusatz[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {
                ergebnisArrayWillenserklaerung[i] = dbBundle.dbWillenserklaerung.decodeErgebnis(lErgebnis);
                ergebnisArrayWillenserklaerungZusatz[i] = dbBundle.dbWillenserklaerungZusatz.decodeErgebnis(lErgebnis);
                i++;
            }
            lErgebnis.close();
            lPStm.close();

        } catch (SQLException e) {
            CaBug.drucke("DbJoined.read_willenserklaerungArtZuAktionaer 003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    /**
     * Einlesen der Aktionärsnummern aller angemeldeten Aktionäre
     * @return
     */
    public int read_angemeldeteAktionaersnummern() {
        int anzInArray = 0;
        PreparedStatement lPStm = null;

        String lSql = "SELECT DISTINCT m.aktionaersnummer FROM " + dbBundle.getSchemaMandant() + "tbl_meldungen m "
                + "WHERE m.mandant=? AND " + "m.aktionaersnummer!='' AND " + "m.meldungaktiv=1 AND " + "m.klasse=1 AND "
                + "(m.meldungstyp=1 OR m.meldungstyp=3) " + "ORDER BY m.aktionaersnummer;";
        try {
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, dbBundle.clGlobalVar.mandant);

            ResultSet lErgebnis = lPStm.executeQuery();
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();
            ergebnisArrayKey = new String[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {
                ergebnisArrayKey[i] = lErgebnis.getString("m.aktionaersnummer");
                i++;
            }
            lErgebnis.close();
            lPStm.close();

        } catch (SQLException e) {
            CaBug.drucke("DbJoined.read_angemeldeteAktionaersnummern 003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    /**Einlesen aller Aktionärsnummern, für die Willenserklärungen ab einer bestimmten Veränderungszeit vorliegen*/
    public int read_AktionaersnummernAbZeit(String veraenderungsZeit) {
        int anzInArray = 0;
        PreparedStatement lPStm = null;

        String lSql = "SELECT DISTINCT m.aktionaersnummer FROM " + dbBundle.getSchemaMandant()
                + "tbl_willenserklaerung wk " + "INNER JOIN " + dbBundle.getSchemaMandant()
                + "tbl_meldungen m ON m.meldungsIdent=wk.meldungsIdent " + "INNER JOIN " + dbBundle.getSchemaMandant()
                + "tbl_aktienregister ar ON m.aktionaersnummer=ar.aktionaersnummer "
                + "WHERE wk.veraenderungszeit>=? and wk.mandant=? and m.mandant=? AND ar.mandant=? "
                + "ORDER BY m.aktionaersnummer ";

        try {
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setString(1, veraenderungsZeit);
            lPStm.setInt(2, dbBundle.clGlobalVar.mandant);
            lPStm.setInt(3, dbBundle.clGlobalVar.mandant);
            lPStm.setInt(4, dbBundle.clGlobalVar.mandant);

            ResultSet lErgebnis = lPStm.executeQuery();
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();
            ergebnisArrayKey = new String[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {
                ergebnisArrayKey[i] = lErgebnis.getString("m.aktionaersnummer");
                i++;
            }
            lErgebnis.close();
            lPStm.close();

        } catch (SQLException e) {
            CaBug.drucke("DbJoined.read_AktionaersnummernAbZeit 003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    /**************************"Schnelle" Funktionen mit wenige Ergebnisinformation - prinzipiell für Statistiken**************
     * aber auch als "Oberselektion" für Reports*/

    /**
     * Einlesen der Stimmenzahl aller Meldungen
     * @return
     */
    public int read_angemeldeteStimmen() {
        int anzInArray = 0;
        PreparedStatement lPStm = null;

        String lSql = "SELECT  m.stimmen, m.besitzart FROM " + dbBundle.getSchemaMandant() + "tbl_meldungen m "
                + "WHERE m.mandant=? AND " + "m.meldungaktiv=1 AND " + "m.klasse=1 AND "
                + "(m.meldungstyp=1 OR m.meldungstyp=3) ";
        try {
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, dbBundle.clGlobalVar.mandant);

            ResultSet lErgebnis = lPStm.executeQuery();
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();
            ergebnisArrayMeldung = new EclMeldung[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {
                ergebnisArrayMeldung[i] = new EclMeldung();
                ergebnisArrayMeldung[i].stimmen = lErgebnis.getLong("m.stimmen");
                ergebnisArrayMeldung[i].besitzart = lErgebnis.getString("m.besitzart");
                i++;
            }
            lErgebnis.close();
            lPStm.close();

        } catch (SQLException e) {
            CaBug.drucke("DbJoined.read_angemeldeteStimmen 003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    /**
     * Einlesen der Anzahl Aktionäre in Sammelkarten
     * @return
     */
    public int read_aktionaereInSammelkarten() {
        int anzInArray = 0;
        PreparedStatement lPStm = null;

        String lSql = "SELECT  m.meldungEnthaltenInSammelkarte, m.meldungEnthaltenInSammelkarteArt, m.stimmen FROM "
                + dbBundle.getSchemaMandant() + "tbl_meldungen m " + "WHERE m.mandant=? AND " + "m.meldungaktiv=1 AND "
                + "m.klasse=1 AND " + "m.meldungEnthaltenInSammelkarte!=0 ";
        try {
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, dbBundle.clGlobalVar.mandant);

            ResultSet lErgebnis = lPStm.executeQuery();
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();
            ergebnisArrayMeldung = new EclMeldung[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {
                ergebnisArrayMeldung[i] = new EclMeldung();
                ergebnisArrayMeldung[i].meldungEnthaltenInSammelkarte = lErgebnis
                        .getInt("m.meldungEnthaltenInSammelkarte");
                ergebnisArrayMeldung[i].meldungEnthaltenInSammelkarteArt = lErgebnis
                        .getInt("m.meldungEnthaltenInSammelkarteArt");
                ergebnisArrayMeldung[i].stimmen = lErgebnis.getLong("m.stimmen");
                i++;
            }
            lErgebnis.close();
            lPStm.close();

        } catch (SQLException e) {
            CaBug.drucke("DbJoined.read_aktionaereInSammelkarten 003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    /**
     * Einlesen der vergebenen Eintrittskartennummern - nur Versandart
     * @return
     */
    public int read_eintrittskartennummernVersandart() {

        int anzInArray = 0;
        PreparedStatement lPStm = null;

        String lSql = "SELECT wz.versandartEK FROM " + dbBundle.getSchemaMandant()
                + "tbl_willenserklaerungzusatz wz INNER JOIN " + dbBundle.getSchemaMandant()
                + "tbl_willenserklaerung w ON wz.willenserklaerungIdent=w.willenserklaerungIdent "
                + "WHERE wz.mandant=? AND w.mandant=? AND wz.willenserklaerung=100 ";

        try {
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, dbBundle.clGlobalVar.mandant);
            lPStm.setInt(2, dbBundle.clGlobalVar.mandant);

            ResultSet lErgebnis = lPStm.executeQuery();
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();
            ergebnisArrayKey = new String[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {
                ergebnisArrayKey[i] = Integer.toString(lErgebnis.getInt("wz.versandartEK"));
                i++;
            }
            lErgebnis.close();
            lPStm.close();

        } catch (SQLException e) {
            CaBug.drucke("DbJoined.read_eintrittskartennummern 003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    /**
     * Einlesen der Anzahl der erteilten Briefwähler
     * @return
     */
    public int read_anzBriefwahl() {

        int anzInArray = 0;
        PreparedStatement lPStm = null;

        String lSql = "SELECT w.willenserklaerungIdent FROM " + dbBundle.getSchemaMandant() + "tbl_willenserklaerung w "
                + "WHERE w.mandant=? AND w.willenserklaerung=360 ";

        try {
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, dbBundle.clGlobalVar.mandant);

            ResultSet lErgebnis = lPStm.executeQuery();
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();
            ergebnisArrayKey = new String[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {
                ergebnisArrayKey[i] = Integer.toString(lErgebnis.getInt("w.willenserklaerungIdent"));
                i++;
            }
            lErgebnis.close();
            lPStm.close();

        } catch (SQLException e) {
            CaBug.drucke("DbJoined.read_anzBriefwahl 003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    /**
     * Einlesen der Anzahl der erteilten SRV
     * @return
     */
    public int read_anzSRV() {

        int anzInArray = 0;
        PreparedStatement lPStm = null;

        String lSql = "SELECT w.willenserklaerungIdent FROM " + dbBundle.getSchemaMandant() + "tbl_willenserklaerung w "
                + "WHERE w.mandant=? AND w.willenserklaerung=350 ";

        try {
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, dbBundle.clGlobalVar.mandant);

            ResultSet lErgebnis = lPStm.executeQuery();
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();
            ergebnisArrayKey = new String[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {
                ergebnisArrayKey[i] = Integer.toString(lErgebnis.getInt("w.willenserklaerungIdent"));
                i++;
            }
            lErgebnis.close();
            lPStm.close();

        } catch (SQLException e) {
            CaBug.drucke("DbJoined.read_anzSRV 003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    /**
     * Einlesen der Kennzeichen eMailRegistrierung für alle Aktionäre, die im Portal die Registrierung überhaupt bestätigt haben
     * @return
     */
    public int read_aktionaerePortalHinweiseBestaetigt() {

        int anzInArray = 0;
        PreparedStatement lPStm = null;

        String lSql = "SELECT eMailRegistrierung, hinweisAktionaersPortalBestaetigt, hinweisHVPortalBestaetigt FROM "
                + dbBundle.getSchemaMandant() + "tbl_aktienregisterzusatz where mandant=?;";

        try {
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, dbBundle.clGlobalVar.mandant);

            ResultSet lErgebnis = lPStm.executeQuery();
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();
            ergebnisArrayAktienregisterZusatz = new EclAktienregisterZusatz[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {
                ergebnisArrayAktienregisterZusatz[i] = new EclAktienregisterZusatz();
                ergebnisArrayAktienregisterZusatz[i].eMailRegistrierung = lErgebnis.getInt("eMailRegistrierung");
                ergebnisArrayAktienregisterZusatz[i].hinweisAktionaersPortalBestaetigt = lErgebnis
                        .getInt("hinweisAktionaersPortalBestaetigt");
                ergebnisArrayAktienregisterZusatz[i].hinweisHVPortalBestaetigt = lErgebnis
                        .getInt("hinweisHVPortalBestaetigt");
                i++;
            }
            lErgebnis.close();
            lPStm.close();

        } catch (SQLException e) {
            CaBug.drucke("DbJoined.read_aktionaerePortalHinweiseBestaetigt 003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    /**
     * Negativliste - Alle Aktionäre, die nicht angemeldet sind
     * 
     * aktienwert: alle Aktionäre mit mindestens diesem Aktienwert werden im Ergebnis aufgeführt
     * sortierung:
     * =1 => nach Aktonärsnummer
     * =2 => nach Name
     * =3 => absteigend nach Aktienzahl
     * 
     * @return
     */
    public int read_negativliste(long aktienwert, int sortierung) {

        int anzInArray = 0;
        PreparedStatement lPStm = null;

        String lSql = "SELECT DISTINCT are.aktienregisterIdent, m.aktionaersnummer, are.aktionaersnummer, are.nameKomplett, are.stimmen "
                + "FROM " + dbBundle.getSchemaMandant() + "tbl_aktienregister are " + "LEFT OUTER JOIN "
                + dbBundle.getSchemaMandant()
                + "tbl_meldungen m ON (are.aktionaersnummer=m.aktionaersnummer AND are.mandant=m.mandant) "
                + "WHERE are.mandant=? AND are.stimmen>=? AND m.aktionaersnummer IS Null ";
        switch (sortierung) {
        case 1: {
            lSql = lSql + "ORDER BY are.aktionaersnummer;";
            break;
        }
        case 2: {
            lSql = lSql + "ORDER BY are.nameKomplett;";
            break;
        }
        case 3: {
            lSql = lSql + "ORDER BY are.stimmen DESC;";
            break;
        }
        }

        try {
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, dbBundle.clGlobalVar.mandant);
            lPStm.setLong(2, aktienwert);

            ResultSet lErgebnis = lPStm.executeQuery();
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();
            ergebnisArrayKey = new String[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {
                ergebnisArrayKey[i] = Integer.toString(lErgebnis.getInt("are.aktienregisterIdent"));
                i++;
            }
            lErgebnis.close();
            lPStm.close();

        } catch (SQLException e) {
            CaBug.drucke("DbJoined.read_negativliste 003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    /**
     * Vergleich Aktien im Aktienregister <-> angemeldete Aktien
     *
     * selektion
     * =1 alle, bei denen angemeldete Aktien < Aktien im Aktienregister sind
     * =2 alle, bei denen angemeldete Aktien > Aktien im Aktienregister sind
     * 
     * @return
     */
    public int read_vergleichAngemeldeteAktienZuAktienregister(int selektion) {

        int anzInArray = 0;
        PreparedStatement lPStm = null;

        String lSql = "SELECT are.aktienregisterIdent, are.stimmen, SUM(me.stueckaktien) " + "FROM "
                + dbBundle.getSchemaMandant() + "tbl_aktienregister are " + "INNER JOIN " + dbBundle.getSchemaMandant()
                + "tbl_meldungen me ON (are.aktionaersnummer=me.aktionaersnummer AND are.mandant=me.mandant) "
                + "WHERE are.mandant=? AND me.meldungAktiv=1 GROUP BY are.aktienregisterIdent ";

        try {
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, dbBundle.clGlobalVar.mandant);

            ResultSet lErgebnis = lPStm.executeQuery();
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();
            ergebnisArrayKey = new String[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {
                long areStimmen, meStimmen;
                areStimmen = lErgebnis.getLong(2);
                meStimmen = lErgebnis.getLong(3);
                if ((selektion == 1 && meStimmen < areStimmen) || (selektion == 2 && meStimmen > areStimmen)) {
                    ergebnisArrayKey[i] = Integer.toString(lErgebnis.getInt("are.aktienregisterIdent"));
                } else {
                    ergebnisArrayKey[i] = "";
                }
                i++;
            }
            lErgebnis.close();
            lPStm.close();

        } catch (SQLException e) {
            CaBug.drucke("DbJoined.read_vergleichAngemeldeteAktienZuAktienregister 003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    /**
     * Liefere 0-Anmeldungen
     *
     * 
     * @return
     */
    public int read_nullAnmeldungen() {

        int anzInArray = 0;
        PreparedStatement lPStm = null;

        String lSql = "SELECT are.aktienregisterIdent " + "FROM " + dbBundle.getSchemaMandant()
                + "tbl_aktienregister are " + "INNER JOIN " + dbBundle.getSchemaMandant()
                + "tbl_meldungen me ON (are.aktionaersnummer=me.aktionaersnummer AND are.mandant=me.mandant) "
                + "WHERE are.mandant=? and me.meldungAktiv=1 AND are.stimmen=0 GROUP BY are.aktienregisterIdent ";

        try {
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, dbBundle.clGlobalVar.mandant);

            ResultSet lErgebnis = lPStm.executeQuery();
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();
            ergebnisArrayKey = new String[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {
                ergebnisArrayKey[i] = Integer.toString(lErgebnis.getInt("are.aktienregisterIdent"));
                i++;
            }
            lErgebnis.close();
            lPStm.close();

        } catch (SQLException e) {
            CaBug.drucke("DbJoined.read_nullAnmeldungen 003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    /**
     * Basis für Gesamtbestand Aktienregister
     *
     * 
     * @return
     */
    public int read_AktienregisterAktien() {

        int anzInArray = 0;
        PreparedStatement lPStm = null;

        String lSql = "SELECT are.aktienregisterIdent, are.stimmen " + "FROM " + dbBundle.getSchemaMandant()
                + "tbl_aktienregister are " + "WHERE are.mandant=? ";

        try {
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, dbBundle.clGlobalVar.mandant);

            ResultSet lErgebnis = lPStm.executeQuery();
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();
            ergebnisArrayKey = new String[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {
                ergebnisArrayKey[i] = Long.toString(lErgebnis.getLong("are.stimmen"));
                i++;
            }
            lErgebnis.close();
            lPStm.close();

        } catch (SQLException e) {
            CaBug.drucke("DbJoined.read_AktienregisterAktien 003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    /**
     * Basis für Gesamtbestand Aktienregister - Detailliert mit Besitzart
     *
     * 
     * @return
     */
    public int read_AktienregisterAktienDetail() {

        int anzInArray = 0;
        PreparedStatement lPStm = null;

        String lSql = "SELECT are.aktienregisterIdent, are.stimmen, are.besitzart " + "FROM "
                + dbBundle.getSchemaMandant() + "tbl_aktienregister are " + "WHERE are.mandant=? ";

        try {
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, dbBundle.clGlobalVar.mandant);

            ResultSet lErgebnis = lPStm.executeQuery();
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();
            ergebnisArrayAktienregisterEintrag = new EclAktienregister[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {
                ergebnisArrayAktienregisterEintrag[i] = new EclAktienregister();
                ergebnisArrayAktienregisterEintrag[i].stimmen = lErgebnis.getLong("are.stimmen");
                ergebnisArrayAktienregisterEintrag[i].besitzart = lErgebnis.getString("are.besitzart");
                i++;
            }
            lErgebnis.close();
            lPStm.close();

        } catch (SQLException e) {
            CaBug.drucke("DbJoined.read_AktienregisterAktienDetail 003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    /**
     * Basis für Aktienregisterimport: alle Aktionäre, die auf 0 gesetzt werden müssen
     *
     * 
     * @return
     */
    public int read_AktienregisterLoeschen() {

        int anzInArray = 0;
        PreparedStatement lPStm = null;

        String lSql = "SELECT are.aktienregisterIdent " + "FROM " + dbBundle.getSchemaMandant()
                + "tbl_aktienregister are " + "LEFT OUTER JOIN " + dbBundle.getSchemaMandant()
                + "tbl_aktienregisterupdate areu "
                + "ON (are.aktienregisterIdent=areu.aktienregisterIdent AND are.mandant=areu.mandant) "
                + "WHERE are.mandant=? and areu.kennzeichen IS Null";

        try {
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, dbBundle.clGlobalVar.mandant);

            ResultSet lErgebnis = lPStm.executeQuery();
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();
            ergebnisArrayKey = new String[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {
                ergebnisArrayKey[i] = Integer.toString(lErgebnis.getInt("are.aktienregisterIdent"));
                i++;
            }
            lErgebnis.close();
            lPStm.close();

        } catch (SQLException e) {
            CaBug.drucke("DbJoined.read_AktienregisterAktien 003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    /**
     * Liefere alle für den Email-Versand registrierte
     * 
     * selektion
     * =1 => alle
     * =2 => alle, die nicht angemeldet sind (zur HV)
     *
     * 
     * @return
     */
    public int read_emailVersandRegistrierung(int selektion) {

        int anzInArray = 0;
        PreparedStatement lPStm = null;

        String lSql = "SELECT arez.aktienregisterIdent FROM " + dbBundle.getSchemaMandant()
                + "tbl_aktienregisterzusatz arez " + "LEFT OUTER JOIN " + dbBundle.getSchemaMandant()
                + "tbl_willenserklaerungzusatz wz ON (arez.aktienregisterident=wz.aktienregisterident AND arez.mandant=wz.mandant) "
                + "WHERE arez.mandant=? AND arez.eMailRegistrierung=99 and arez.emailBestaetigt=1 ";

        if (selektion == 2) {
            lSql = lSql + " AND wz.aktienregisterident IS Null ";
        }
        lSql = lSql + "GROUP BY arez.aktienregisterIdent ORDER BY arez.aktienregisterIdent ";

        try {
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, dbBundle.clGlobalVar.mandant);

            ResultSet lErgebnis = lPStm.executeQuery();
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();
            ergebnisArrayKey = new String[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {
                ergebnisArrayKey[i] = Integer.toString(lErgebnis.getInt("arez.aktienregisterIdent"));
                i++;
            }
            lErgebnis.close();
            lPStm.close();

        } catch (SQLException e) {
            CaBug.drucke("DbJoined.read_emailVersandRegistrierung 003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    /*TODO #Konsolidierung: Klären - wie wird denn Versandnummer in den Folgejahren gesetzt? Und: evtl. Verinheitlichung mit EmailVersand-Liste?*/
    /**
     * Liefere alle Aktionäre, die für den jeweiligen Versandlauf aufzubereiten sind.
     * Wird für Erstversand zur Selektion der Versandläufe verwendet.
     * (Für Nachversand nicht erforderlich, da bei Nachversand noch keine
     * "bereits registrierten für Email-Versand oder so vorhanden sein können)
     * 
     * selektion
     * =1 => für postalischen Versand: alle die, die sich nicht für Email-Versand registriert haben
     * =2 => für Email-Versand: alle die, sich für Email-Versand registriert haben, 
     * 			aber noch kein dauerhaftes Passwort vergeben haben (für Vorabversand neues Passwort per Email)
     * =3 => für Email-Versand: alle die, die sich für Email-Versand registriert haben 
     * 			(für den eigentlichen Email-Versand der Einladungen)
     * 
     * versandnummer: nur diese Versandnummer wird selektiert
     * 
     * Ergebnis steht in:
     * 	ergebnisArrayAktienregisterEintrag
     *  ergebnisArrayAktienregisterZusatz
     *  
     * Ergebnis ist sortiert nach Aktionärsnummer
     */
    public int read_aktienregisterVersandliste(int selektion, int versandNummer) {

        int anzInArray = 0;
        PreparedStatement lPStm = null;

        String lSql = "SELECT arez.*, are.* FROM " + dbBundle.getSchemaMandant() + "tbl_aktienregisterzusatz arez "
                + "LEFT OUTER JOIN " + dbBundle.getSchemaMandant()
                + "tbl_aktienregister are ON (arez.aktienregisterident=are.aktienregisterident AND arez.mandant=are.mandant) "
                + "WHERE arez.mandant=? AND are.mandant=? AND are.versandNummer=? AND are.stueckAktien>0";
        switch (selektion) {
        case 1: {
            lSql = "SELECT arez.*, are.* FROM " + dbBundle.getSchemaMandant() + "tbl_aktienregisterzusatz arez "
                    + "RIGHT OUTER JOIN " + dbBundle.getSchemaMandant()
                    + "tbl_aktienregister are ON (arez.aktienregisterident=are.aktienregisterident AND arez.mandant=are.mandant) "
                    + "WHERE (arez.mandant is Null OR arez.mandant=?) AND are.mandant=? AND are.versandNummer=? AND are.stueckAktien>0"
                    + " AND (arez.eMailRegistrierung is Null OR arez.eMailRegistrierung!=99 OR arez.emailBestaetigt!=1 )";
            break;
        }
        case 2: {
            lSql = lSql + " AND (arez.eMailRegistrierung=99 AND arez.emailBestaetigt=1) AND arez.eigenesPasswort<98";
            break;
        }
        case 3: {
            lSql = lSql + " AND (arez.eMailRegistrierung=99 AND arez.emailBestaetigt=1) ";
            break;
        }
        }
        lSql = lSql + " ORDER BY are.aktionaersnummer ";

        try {
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, dbBundle.clGlobalVar.mandant);
            lPStm.setInt(2, dbBundle.clGlobalVar.mandant);
            lPStm.setInt(3, versandNummer);

            ResultSet lErgebnis = lPStm.executeQuery();
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();
            ergebnisArrayAktienregisterEintrag = new EclAktienregister[anzInArray];
            ergebnisArrayAktienregisterZusatz = new EclAktienregisterZusatz[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {
                ergebnisArrayAktienregisterEintrag[i] = dbBundle.dbAktienregister.decodeErgebnis(lErgebnis);
                ergebnisArrayAktienregisterZusatz[i] = dbBundle.dbAktienregisterZusatz.decodeErgebnis(lErgebnis);
                i++;
            }
            lErgebnis.close();
            lPStm.close();

        } catch (SQLException e) {
            CaBug.drucke("DbJoined.read_aktienregisterVersandliste 003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    /***********************************für Reports*********************************************************/
    private ResultSet lErgebnis_meldeliste = null;
    PreparedStatement lPStm_meldeliste = null;

    /**
     * 
     * @param sortierung
     * 1=meldeIdent
     * 2=Name
     * 3=Aktien absteigend
     * @param selektion
     * 1=nur EinzelMeldungen
     * 2=nur Sammelkarten
     * 3=alle
     * @param klasse
     * 0=Gäste
     * 1=Aktionäre
     * @return
     */
    public int readinit_meldeliste(int sortierung, int selektion, int klasse) {

        int anzInArray = 0;

        try {
            String lSql = "SELECT * from " + dbBundle.getSchemaMandant() + "tbl_meldungen m " + "INNER JOIN "
                    + dbBundle.getSchemaMandant() + "tbl_personenNatJur p ON m.personenNatJurIdent=p.ident "
                    + "WHERE m.mandant=? and p.mandant=? " + "AND m.meldungAktiv=1 ";
            switch (klasse) {
            case 0: {
                lSql = lSql + " AND m.klasse=0 ";
                break;
            }
            case 1: {
                lSql = lSql + " AND m.klasse=1 ";
                break;
            }
            }
            switch (selektion) {
            case 1: {
                lSql = lSql + " AND (m.meldungstyp=1 OR m.meldungstyp=3) ";
                break;
            }
            case 2: {
                lSql = lSql + " AND m.meldungstyp=2 ";
                break;
            }
            }
            switch (sortierung) {
            case 1: {
                lSql = lSql + " ORDER BY m.meldungsIdent ";
                break;
            }
            case 2: {
                lSql = lSql + " ORDER BY p.name ";
                break;
            }
            case 3: {
                lSql = lSql + " ORDER BY m.stueckAktien DESC ";
                break;
            }
            }

            System.out.println(lSql);

            lPStm_meldeliste = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            lPStm_meldeliste.setInt(1, dbBundle.clGlobalVar.mandant);
            lPStm_meldeliste.setInt(2, dbBundle.clGlobalVar.mandant);

            lErgebnis_meldeliste = lPStm_meldeliste.executeQuery();
            lErgebnis_meldeliste.last();
            anzInArray = lErgebnis_meldeliste.getRow();
            lErgebnis_meldeliste.beforeFirst();

            ergebnisArrayMeldung = new EclMeldung[1];

        } catch (Exception e) {
            CaBug.drucke("DbJoined.readinit_meldeliste 003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    /**
     * 
     * @return
     * true=Satz gelesen
     * false=ende erreicht, kein Satz mehr gelesen
     */
    public boolean readnext_meldeliste() {
        try {
            if (lErgebnis_meldeliste.next() == true) {
                ergebnisArrayMeldung[0] = dbBundle.dbMeldungen.decodeErgebnis(lErgebnis_meldeliste);

                return true;
            } else {
                lErgebnis_meldeliste.close();
                lPStm_meldeliste.close();
                return false;
            }

        } catch (SQLException e) {
            CaBug.drucke("DbJoined.readnext_meldeliste 003");
            System.err.println(" " + e.getMessage());
            return false;
        }
    }

    /******Liste aller Aktionäre, die Einzelweisung enthalten haben (und als solche in einer Sammelkarte sind)
     * sammelIdent=0 => egal in welcher Sammelkarte
     * sammelIdent!=0 => nur die, die dieser Sammelkarte zugeordnet sind
     * 
     * mitVerschlossenen=1 => auch "nicht weiterzugebende" werden ausgedruckt
     * 
     * 
     * ergebnisArrayMeldung = Aktionärsmeldung
     * ergebnisArrayMeldung1 = zugehörige Sammelkarte
     * ergebnisArrayWeisungMeldung = Detailweisungen
     * */
    public int readinit_aktionaereMitWeisung(int sammelIdent) {

        int anzInArray = 0;

        /*Lesen aller Sammelklarten, die Zusatz1=="" haben (sonst keine Offenlegung)
         * 		und skWeisungsartZulaessig and 4 == 4
         * Dazu alle Meldungen, die aktiv sind in dieser Sammelkarte - (inner join)
         * Dazu alle Weisungen mit entsprechender MeldungsIdent
         */

        try {
            String lSql = "SELECT m1.meldungsIdent, m1.zusatzfeld1, m1.skIst, "
                    + "m.meldungsIdent, m.aktionaersnummer, m.besitzart, m.stimmen,  m.zutrittsIdent, "
                    + "p.name, p.vorname, p.ort, wm.* " + "FROM " + dbBundle.getSchemaMandant() + "tbl_meldungen m1 "
                    + "INNER JOIN " + dbBundle.getSchemaMandant()
                    + "tbl_meldungen m ON m.meldungEnthaltenInSammelkarte=m1.meldungsIdent " + "INNER JOIN "
                    + dbBundle.getSchemaMandant() + "tbl_personenNatJur p ON m.personenNatJurIdent=p.ident "
                    + "INNER JOIN " + dbBundle.getSchemaMandant()
                    + "tbl_weisungMeldung wm ON m.meldungsIdent=wm.meldungsIdent "
                    + "WHERE m1.mandant=? and m.mandant=? and p.mandant=? and wm.mandant=? "
                    + "AND m.meldungAktiv=1 AND m.klasse=1 " + "AND (m1.skWeisungsartZulaessig & 4 =4 ) ";

            if (sammelIdent > 0) {
                lSql = lSql + " AND m1.meldungsIdent=? ";
            }

            lSql = lSql + "ORDER BY m.aktionaersnummer ";

            System.out.println(lSql);

            lPStm_meldeliste = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            lPStm_meldeliste.setInt(1, dbBundle.clGlobalVar.mandant);
            lPStm_meldeliste.setInt(2, dbBundle.clGlobalVar.mandant);
            lPStm_meldeliste.setInt(3, dbBundle.clGlobalVar.mandant);
            lPStm_meldeliste.setInt(4, dbBundle.clGlobalVar.mandant);

            if (sammelIdent > 0) {
                lPStm_meldeliste.setInt(5, sammelIdent);
            }

            lErgebnis_meldeliste = lPStm_meldeliste.executeQuery();
            lErgebnis_meldeliste.last();
            anzInArray = lErgebnis_meldeliste.getRow();
            lErgebnis_meldeliste.beforeFirst();

            ergebnisArrayMeldung = new EclMeldung[1];
            ergebnisArrayMeldung1 = new EclMeldung[1];
            ergebnisArrayWeisungMeldung = new EclWeisungMeldung[1];

        } catch (Exception e) {
            CaBug.drucke("DbJoined.readinit_aktionaereMitWeisung 003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    /**
     * 
     * @return
     * true=Satz gelesen
     * false=ende erreicht, kein Satz mehr gelesen
     */
    public boolean readnext_aktionaereMitWeisung() {
        try {
            if (lErgebnis_meldeliste.next() == true) {

                ergebnisArrayMeldung[0] = new EclMeldung(); /*Aktionär*/
                ergebnisArrayMeldung[0].meldungsIdent = lErgebnis_meldeliste.getInt("m.meldungsIdent");
                ergebnisArrayMeldung[0].aktionaersnummer = lErgebnis_meldeliste.getString("m.aktionaersnummer");
                ergebnisArrayMeldung[0].besitzart = lErgebnis_meldeliste.getString("m.besitzart");
                ergebnisArrayMeldung[0].stimmen = lErgebnis_meldeliste.getLong("m.stimmen");
                ergebnisArrayMeldung[0].zutrittsIdent = lErgebnis_meldeliste.getString("m.zutrittsIdent");
                ergebnisArrayMeldung[0].name = lErgebnis_meldeliste.getString("p.name");
                ergebnisArrayMeldung[0].vorname = lErgebnis_meldeliste.getString("p.vorname");
                ergebnisArrayMeldung[0].ort = lErgebnis_meldeliste.getString("p.ort");

                ergebnisArrayMeldung1[0] = new EclMeldung(); /*Sammelkarte*/
                ergebnisArrayMeldung1[0].zusatzfeld1 = lErgebnis_meldeliste.getString("m1.zusatzfeld1");
                ergebnisArrayMeldung1[0].meldungsIdent = lErgebnis_meldeliste.getInt("m1.meldungsIdent");
                ergebnisArrayMeldung1[0].skIst = lErgebnis_meldeliste.getInt("m1.skIst");

                ergebnisArrayWeisungMeldung[0] = dbBundle.dbWeisungMeldung.decodeErgebnis(lErgebnis_meldeliste);

                return true;
            } else {
                lErgebnis_meldeliste.close();
                lPStm_meldeliste.close();
                return false;
            }

        } catch (SQLException e) {
            CaBug.drucke("DbJoined.readnext_aktionaereMitWeisung 003");
            System.err.println(" " + e.getMessage());
            return false;
        }
    }

    /******Kontrolliste für Weisungserfassung
     * sammelIdent=0 => egal in welcher Sammelkarte
     * sammelIdent!=0 => nur die, die dieser Sammelkarte zugeordnet sind
     * 
     * mitVerschlossenen=1 => auch "nicht weiterzugebende" werden ausgedruckt
     * 
     * 
    		ergebnisArrayMeldung=new EclMeldung[1];
    		ergebnisArrayWillenserklaerung=new EclWillenserklaerung[1];
    		ergebnisArrayWeisungMeldung=new EclWeisungMeldung[1];
     * */
    public int readinit_kontrollisteWeisung(String abDatum) {

        int anzInArray = 0;

        try {

            String lSql = "SELECT w.*, m.*, p.*, wm.* " + "FROM " + dbBundle.getSchemaMandant()
                    + "tbl_willenserklaerung w " + "INNER JOIN " + dbBundle.getSchemaMandant()
                    + "tbl_meldungen m ON w.meldungsIdent=m.meldungsIdent " + "INNER JOIN "
                    + dbBundle.getSchemaMandant() + "tbl_personenNatJur p ON m.personenNatJurIdent=p.ident "
                    + "INNER JOIN " + dbBundle.getSchemaMandant()
                    + "tbl_weisungMeldung wm on wm.willenserklaerungIdent=w.willenserklaerungIdent "
                    + "WHERE w.mandant=? AND m.mandant=? AND p.mandant=? AND wm.mandant=? "
                    + "AND (w.erteiltAufWeg<20 || w.erteiltAufWeg=51) "
                    + "AND (w.willenserklaerung>=350 AND w.willenserklaerung <=367) " + "AND w.veraenderungszeit>=? "
                    + "ORDER BY w.benutzernr, w.willenserklaerung, w.willenserklaerungIdent ";

            //			System.out.println(lSql);

            lPStm_meldeliste = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            lPStm_meldeliste.setInt(1, dbBundle.clGlobalVar.mandant);
            lPStm_meldeliste.setInt(2, dbBundle.clGlobalVar.mandant);
            lPStm_meldeliste.setInt(3, dbBundle.clGlobalVar.mandant);
            lPStm_meldeliste.setInt(4, dbBundle.clGlobalVar.mandant);
            lPStm_meldeliste.setString(5, abDatum);

            lErgebnis_meldeliste = lPStm_meldeliste.executeQuery();
            lErgebnis_meldeliste.last();
            anzInArray = lErgebnis_meldeliste.getRow();
            lErgebnis_meldeliste.beforeFirst();

            ergebnisArrayMeldung = new EclMeldung[1];
            ergebnisArrayWillenserklaerung = new EclWillenserklaerung[1];
            ergebnisArrayWeisungMeldung = new EclWeisungMeldung[1];

        } catch (Exception e) {
            CaBug.drucke("DbJoined.readinit_kontrollisteWeisung 003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    /**
     * 
     * @return
     * true=Satz gelesen
     * false=ende erreicht, kein Satz mehr gelesen
     */
    public boolean readnext_kontrollisteWeisung() {
        try {
            if (lErgebnis_meldeliste.next() == true) {

                ergebnisArrayMeldung[0] = dbBundle.dbMeldungen.decodeErgebnis(lErgebnis_meldeliste); /*Aktionär*/
                ergebnisArrayWeisungMeldung[0] = dbBundle.dbWeisungMeldung.decodeErgebnis(lErgebnis_meldeliste);
                ergebnisArrayWillenserklaerung[0] = dbBundle.dbWillenserklaerung.decodeErgebnis(lErgebnis_meldeliste);

                return true;
            } else {
                lErgebnis_meldeliste.close();
                lPStm_meldeliste.close();
                return false;
            }

        } catch (SQLException e) {
            CaBug.drucke("DbJoined.readnext_kontrollisteWeisung 003");
            System.err.println(" " + e.getMessage());
            return false;
        }
    }

    /********************************Funktionen für Präsenzliste***********************************************/
    /**Feststellen der Präsenz: "fixieren" der Willenserklärungen (d.h. Füllen der Variable "zuVerzeichnisNrXGedruckt").
     * 
     * @param listenNummer=alle Willenserklärungen, die kleiner oder gleich dieser Präsenzlistennummer sind, werden fixiert,
     * soweit sie nicht schon fixiert sind. Diese wird auch als "gedruckt" eingetragen
     * @param verzeichnis = 1 bis 4
     * @return
     */
    public int feststellenPraesenz(int listenNummer, int verzeichnis) {

        try {

            String sql = "UPDATE " + dbBundle.getSchemaMandant() + "tbl_willenserklaerung SET " + "zuVerzeichnisNr"
                    + Integer.toString(verzeichnis) + "Gedruckt=" + Integer.toString(listenNummer) + ", "
                    + "db_version=db_version+1 " + "WHERE " + "mandant=? AND " + "zuVerzeichnisNr"
                    + Integer.toString(verzeichnis) + "<=" + Integer.toString(listenNummer) + " AND "
                    + "zuVerzeichnisNr" + Integer.toString(verzeichnis) + "Gedruckt=0 AND " + "mc_delayed!=1 AND " + "("
                    + "willenserklaerung=" + KonstWillenserklaerung.erstzugang + " OR " + "willenserklaerung="
                    + KonstWillenserklaerung.abgang + " OR " + "willenserklaerung="
                    + KonstWillenserklaerung.wiederzugang + " OR " + "willenserklaerung="
                    + KonstWillenserklaerung.vertreterwechsel + " OR " + "willenserklaerung="
                    + KonstWillenserklaerung.abgangAusSRV + " OR " + "willenserklaerung="
                    + KonstWillenserklaerung.abgangAusOrga + " OR " + "willenserklaerung="
                    + KonstWillenserklaerung.abgangAusDauervollmacht + " OR " + "willenserklaerung="
                    + KonstWillenserklaerung.abgangAusKIAV + " OR " + "willenserklaerung="
                    + KonstWillenserklaerung.zugangInSRV + " OR " + "willenserklaerung="
                    + KonstWillenserklaerung.zugangInOrga + " OR " + "willenserklaerung="
                    + KonstWillenserklaerung.zugangInDauervollmacht + " OR " + "willenserklaerung="
                    + KonstWillenserklaerung.zugangInKIAV + " OR " + "willenserklaerung="
                    + KonstWillenserklaerung.wechselInSRV + " OR " + "willenserklaerung="
                    + KonstWillenserklaerung.wechselInOrga + " OR " + "willenserklaerung="
                    + KonstWillenserklaerung.wechselInDauervollmacht + " OR " + "willenserklaerung="
                    + KonstWillenserklaerung.wechselInKIAV + ")";

            PreparedStatement pstm1 = verbindung.prepareStatement(sql);
            pstm1.setInt(1, dbBundle.clGlobalVar.mandant);

            int ergebnis1 = pstm1.executeUpdate();
            pstm1.close();
            if (ergebnis1 == 0) {
                return (1);
            }
        } catch (Exception e1) {
            CaBug.drucke("DbJoined.feststellenPraesenz 001");
            System.err.println(" " + e1.getMessage());
            return (-1);

        }

        return (1);
    }

    /**verzeichnis -1 => einlesen unabhängig alle von Listennummer, Präsenznummer usw.
     * abWillenserklaerungsIdent: ab dieser Nummer wird eingelesen. Für Präsenzliste auf 0 setzen!
     */
    public int read_Praesenzliste(int verzeichnis, int listenNummer, int abWillenserklaerungsident) {

        int anzInArray = 0;
        PreparedStatement lPStm = null;

        String lSql = "SELECT w.willenserklaerungIdent, w.willenserklaerung, w.aktien, w.stimmen,"
                + "w.meldungsIdent, w.zutrittsIdent, w.stimmkarte1, w.bevollmaechtigterDritterIdent, "
                + "m.meldungstyp, m.besitzart, m.gattung, m.skOffenlegung, m.skIst, " + "p.name, p.vorname, p.ort ,"
                + "p1.name, p1.vorname, p1.ort," + "m1.meldungsIdent, m1.skOffenlegung, m1.skIst, "
                + "p2.name, p2.vorname, p2.ort " + "FROM " + dbBundle.getSchemaMandant() + "tbl_willenserklaerung w "
                + "INNER JOIN " + dbBundle.getSchemaMandant() + "tbl_meldungen m ON m.meldungsIdent=w.meldungsIdent " //Aktionärs-Meldung zu Willenserklärung
                + "INNER JOIN " + dbBundle.getSchemaMandant() + "tbl_personenNatJur p ON m.personenNatJurIdent=p.ident " //Person zu Aktionärs-Meldung
                + "LEFT OUTER JOIN " + dbBundle.getSchemaMandant()
                + "tbl_personenNatJur p1 ON (w.bevollmaechtigterDritterIdent=p1.ident AND w.mandant=p1.mandant) " //Vertreter zu Willenserklärung
                + "LEFT OUTER JOIN " + dbBundle.getSchemaMandant()
                + "tbl_meldungen m1 ON (m1.meldungsIdent=w.identMeldungZuSammelkarte AND m1.mandant=w.mandant) " //Sammelkarte zu Willenserklärung
                + "LEFT OUTER JOIN " + dbBundle.getSchemaMandant()
                + "tbl_personenNatJur p2 ON (m1.personenNatJurIdent=p2.ident AND m1.mandant=p2.mandant) " //natürliche Person der Sammelkarte
                + "WHERE " + "w.mandant=? AND m.mandant=? AND p.mandant=? AND ";
        if (verzeichnis != -1) {
            lSql = lSql + "w.zuVerzeichnisNr" + Integer.toString(verzeichnis) + "Gedruckt="
                    + Integer.toString(listenNummer) + " AND ";
        }
        lSql = lSql + "(" + "w.willenserklaerung=" + KonstWillenserklaerung.erstzugang + " OR " + "w.willenserklaerung="
                + KonstWillenserklaerung.abgang + " OR " + "w.willenserklaerung=" + KonstWillenserklaerung.wiederzugang
                + " OR " + "w.willenserklaerung=" + KonstWillenserklaerung.vertreterwechsel + " OR "
                + "w.willenserklaerung=" + KonstWillenserklaerung.abgangAusSRV + " OR " + "w.willenserklaerung="
                + KonstWillenserklaerung.abgangAusOrga + " OR " + "w.willenserklaerung="
                + KonstWillenserklaerung.abgangAusDauervollmacht + " OR " + "w.willenserklaerung="
                + KonstWillenserklaerung.abgangAusKIAV + " OR " + "w.willenserklaerung="
                + KonstWillenserklaerung.zugangInSRV + " OR " + "w.willenserklaerung="
                + KonstWillenserklaerung.zugangInOrga + " OR " + "w.willenserklaerung="
                + KonstWillenserklaerung.zugangInDauervollmacht + " OR " + "w.willenserklaerung="
                + KonstWillenserklaerung.zugangInKIAV + " OR " + "w.willenserklaerung="
                + KonstWillenserklaerung.wechselInSRV + " OR " + "w.willenserklaerung="
                + KonstWillenserklaerung.wechselInOrga + " OR " + "w.willenserklaerung="
                + KonstWillenserklaerung.wechselInDauervollmacht + " OR " + "w.willenserklaerung="
                + KonstWillenserklaerung.wechselInKIAV + ") " + "AND w.willenserklaerungIdent>? "
                + "ORDER BY w.meldungsIdent, w. willenserklaerungIdent ";

        System.out.println("ab WillenserklärungIdent=" + abWillenserklaerungsident);
        System.out.println(lSql);

        try {
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, dbBundle.clGlobalVar.mandant);
            lPStm.setInt(2, dbBundle.clGlobalVar.mandant);
            lPStm.setInt(3, dbBundle.clGlobalVar.mandant);
            lPStm.setInt(4, abWillenserklaerungsident);

            ResultSet lErgebnis = lPStm.executeQuery();
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();
            ergebnisArrayPraesenzliste = new EclPraesenzliste[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {
                ergebnisArrayPraesenzliste[i] = new EclPraesenzliste();
                ergebnisArrayPraesenzliste[i].willenserklaerungIdent = lErgebnis.getInt("w.willenserklaerungIdent");
                ergebnisArrayPraesenzliste[i].willenserklaerung = lErgebnis.getInt("w.willenserklaerung");
                ergebnisArrayPraesenzliste[i].aktien = lErgebnis.getLong("w.aktien");
                ergebnisArrayPraesenzliste[i].stimmen = lErgebnis.getLong("w.stimmen");
                ergebnisArrayPraesenzliste[i].meldeIdentAktionaer = lErgebnis.getInt("w.meldungsIdent");
                ergebnisArrayPraesenzliste[i].meldungstyp = lErgebnis.getInt("m.meldungstyp");
                ergebnisArrayPraesenzliste[i].besitzartKuerzel = lErgebnis.getString("m.besitzart");
                ergebnisArrayPraesenzliste[i].gattung = lErgebnis.getInt("m.gattung");
                ergebnisArrayPraesenzliste[i].meldungSkIst = lErgebnis.getInt("m.skIst");
                ergebnisArrayPraesenzliste[i].fuelleMeldungOffenlegungTatsaechlich(dbBundle,
                        lErgebnis.getInt("m.skOffenlegung"));
                //				ergebnisArrayPraesenzliste[i].meldungSkOffenlegung=lErgebnis.getInt("m.skOffenlegung");
                ergebnisArrayPraesenzliste[i].zutrittsIdent = lErgebnis.getString("w.zutrittsIdent");
                ergebnisArrayPraesenzliste[i].stimmkarte = lErgebnis.getString("w.stimmkarte1");
                ergebnisArrayPraesenzliste[i].aktionaerName = lErgebnis.getString("p.name");
                ergebnisArrayPraesenzliste[i].aktionaerVorname = lErgebnis.getString("p.vorname");
                ergebnisArrayPraesenzliste[i].aktionaerOrt = lErgebnis.getString("p.ort");

                ergebnisArrayPraesenzliste[i].vertreterIdent = lErgebnis.getInt("w.bevollmaechtigterDritterIdent");
                ergebnisArrayPraesenzliste[i].vertreterName = lErgebnis.getString("p1.name");
                ergebnisArrayPraesenzliste[i].vertreterVorname = lErgebnis.getString("p1.vorname");
                ergebnisArrayPraesenzliste[i].vertreterOrt = lErgebnis.getString("p1.ort");

                ergebnisArrayPraesenzliste[i].sammelkartenIdent = lErgebnis.getInt("m1.meldungsIdent");
                ergebnisArrayPraesenzliste[i].sammelkartenName = lErgebnis.getString("p2.name");
                ergebnisArrayPraesenzliste[i].sammelkartenVorname = lErgebnis.getString("p2.vorname");
                ergebnisArrayPraesenzliste[i].sammelkartenOrt = lErgebnis.getString("p2.ort");

                //				ergebnisArrayPraesenzliste[i].skOffenlegung=lErgebnis.getInt("m1.skOffenlegung");
                ergebnisArrayPraesenzliste[i].skIst = lErgebnis.getInt("m1.skIst");
                ergebnisArrayPraesenzliste[i].fuelleOffenlegungTatsaechlich(dbBundle,
                        lErgebnis.getInt("m1.skOffenlegung"));

                i++;
            }
            lErgebnis.close();
            lPStm.close();

        } catch (SQLException e) {
            CaBug.drucke("DbJoined.read_Praesenzliste 003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    public int read_Praesenzprotokoll(int arbeitsplatz, int protokoll) {

        int anzInArray = 0;
        PreparedStatement lPStm = null;

        String lSql = "SELECT w.willenserklaerungIdent, w.willenserklaerung, w.aktien, w.stimmen,"
                + "w.meldungsIdent, w.zutrittsIdent, w.stimmkarte1, w.bevollmaechtigterDritterIdent, "
                + "m.meldungstyp, m.besitzart, m.gattung, m.skOffenlegung, m.skIst, " + "p.name, p.vorname, p.ort ,"
                + "p1.name, p1.vorname, p1.ort," + "m1.meldungsIdent, m1.skOffenlegung, m1.skIst,"
                + "p2.name, p2.vorname, p2.ort " + "FROM " + dbBundle.getSchemaMandant() + "tbl_willenserklaerung w "
                + "INNER JOIN " + dbBundle.getSchemaMandant() + "tbl_meldungen m ON m.meldungsIdent=w.meldungsIdent " //Aktionärs-Meldung zu Willenserklärung
                + "INNER JOIN " + dbBundle.getSchemaMandant() + "tbl_personenNatJur p ON m.personenNatJurIdent=p.ident " //Person zu Aktionärs-Meldung
                + "LEFT OUTER JOIN " + dbBundle.getSchemaMandant()
                + "tbl_personenNatJur p1 ON (w.bevollmaechtigterDritterIdent=p1.ident AND w.mandant=p1.mandant) " //Vertreter zu Willenserklärung
                + "LEFT OUTER JOIN " + dbBundle.getSchemaMandant()
                + "tbl_meldungen m1 ON (m1.meldungsIdent=w.identMeldungZuSammelkarte AND m1.mandant=w.mandant) " //Sammelkarte zu Willenserklärung
                + "LEFT OUTER JOIN " + dbBundle.getSchemaMandant()
                + "tbl_personenNatJur p2 ON (m1.personenNatJurIdent=p2.ident AND m1.mandant=p2.mandant) " //natürliche Person der Sammelkarte
                + "WHERE " + "w.mandant=? AND m.mandant=? AND p.mandant=? AND " + "w.arbeitsplatz="
                + Integer.toString(arbeitsplatz) + " AND w.protokollnr=" + Integer.toString(protokoll) + " AND " + "("
                + "w.willenserklaerung=" + KonstWillenserklaerung.erstzugang + " OR " + "w.willenserklaerung="
                + KonstWillenserklaerung.abgang + " OR " + "w.willenserklaerung=" + KonstWillenserklaerung.wiederzugang
                + " OR " + "w.willenserklaerung=" + KonstWillenserklaerung.vertreterwechsel + " OR "
                + "w.willenserklaerung=" + KonstWillenserklaerung.abgangAusSRV + " OR " + "w.willenserklaerung="
                + KonstWillenserklaerung.abgangAusOrga + " OR " + "w.willenserklaerung="
                + KonstWillenserklaerung.abgangAusDauervollmacht + " OR " + "w.willenserklaerung="
                + KonstWillenserklaerung.abgangAusKIAV + " OR " + "w.willenserklaerung="
                + KonstWillenserklaerung.zugangInSRV + " OR " + "w.willenserklaerung="
                + KonstWillenserklaerung.zugangInOrga + " OR " + "w.willenserklaerung="
                + KonstWillenserklaerung.zugangInDauervollmacht + " OR " + "w.willenserklaerung="
                + KonstWillenserklaerung.zugangInKIAV + " OR " + "w.willenserklaerung="
                + KonstWillenserklaerung.wechselInSRV + " OR " + "w.willenserklaerung="
                + KonstWillenserklaerung.wechselInOrga + " OR " + "w.willenserklaerung="
                + KonstWillenserklaerung.wechselInDauervollmacht + " OR " + "w.willenserklaerung="
                + KonstWillenserklaerung.wechselInKIAV + ")";

        try {
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, dbBundle.clGlobalVar.mandant);
            lPStm.setInt(2, dbBundle.clGlobalVar.mandant);
            lPStm.setInt(3, dbBundle.clGlobalVar.mandant);

            ResultSet lErgebnis = lPStm.executeQuery();
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();
            ergebnisArrayPraesenzliste = new EclPraesenzliste[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {
                ergebnisArrayPraesenzliste[i] = new EclPraesenzliste();
                ergebnisArrayPraesenzliste[i].willenserklaerungIdent = lErgebnis.getInt("w.willenserklaerungIdent");
                ergebnisArrayPraesenzliste[i].willenserklaerung = lErgebnis.getInt("w.willenserklaerung");
                ergebnisArrayPraesenzliste[i].aktien = lErgebnis.getLong("w.aktien");
                ergebnisArrayPraesenzliste[i].stimmen = lErgebnis.getLong("w.stimmen");
                ergebnisArrayPraesenzliste[i].meldeIdentAktionaer = lErgebnis.getInt("w.meldungsIdent");
                ergebnisArrayPraesenzliste[i].meldungstyp = lErgebnis.getInt("m.meldungstyp");
                ergebnisArrayPraesenzliste[i].besitzartKuerzel = lErgebnis.getString("m.besitzart");
                ergebnisArrayPraesenzliste[i].gattung = lErgebnis.getInt("m.gattung");
                //				ergebnisArrayPraesenzliste[i].meldungSkOffenlegung=lErgebnis.getInt("m.skOffenlegung");
                ergebnisArrayPraesenzliste[i].meldungSkIst = lErgebnis.getInt("m.skIst");
                ergebnisArrayPraesenzliste[i].fuelleMeldungOffenlegungTatsaechlich(dbBundle,
                        lErgebnis.getInt("m.skOffenlegung"));
                ergebnisArrayPraesenzliste[i].zutrittsIdent = lErgebnis.getString("w.zutrittsIdent");
                ergebnisArrayPraesenzliste[i].stimmkarte = lErgebnis.getString("w.stimmkarte1");
                ergebnisArrayPraesenzliste[i].aktionaerName = lErgebnis.getString("p.name");
                ergebnisArrayPraesenzliste[i].aktionaerVorname = lErgebnis.getString("p.vorname");
                ergebnisArrayPraesenzliste[i].aktionaerOrt = lErgebnis.getString("p.ort");

                ergebnisArrayPraesenzliste[i].vertreterIdent = lErgebnis.getInt("w.bevollmaechtigterDritterIdent");
                ergebnisArrayPraesenzliste[i].vertreterName = lErgebnis.getString("p1.name");
                ergebnisArrayPraesenzliste[i].vertreterVorname = lErgebnis.getString("p1.vorname");
                ergebnisArrayPraesenzliste[i].vertreterOrt = lErgebnis.getString("p1.ort");

                ergebnisArrayPraesenzliste[i].sammelkartenIdent = lErgebnis.getInt("m1.meldungsIdent");
                ergebnisArrayPraesenzliste[i].sammelkartenName = lErgebnis.getString("p2.name");
                ergebnisArrayPraesenzliste[i].sammelkartenVorname = lErgebnis.getString("p2.vorname");
                ergebnisArrayPraesenzliste[i].sammelkartenOrt = lErgebnis.getString("p2.ort");

                //				ergebnisArrayPraesenzliste[i].skOffenlegung=lErgebnis.getInt("m1.skOffenlegung");
                ergebnisArrayPraesenzliste[i].skIst = lErgebnis.getInt("m1.skIst");
                ergebnisArrayPraesenzliste[i].fuelleOffenlegungTatsaechlich(dbBundle,
                        lErgebnis.getInt("m1.skOffenlegung"));

                i++;
            }
            lErgebnis.close();
            lPStm.close();

        } catch (SQLException e) {
            CaBug.drucke("DbJoined.read_Praesenzliste 003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    public int read_PraesenzExport() {

        int anzInArray = 0;
        PreparedStatement lPStm = null;

        String lSql = "SELECT w.willenserklaerungIdent, w.willenserklaerung, w.aktien, w.stimmen,"
                + "w.meldungsIdent, w.zutrittsIdent, w.stimmkarte1, w.willenserklaerungGeberIdent, "
                + "m.meldungstyp, m.besitzart, m.gattung, m.skOffenlegung, m.skIst, " + "p.name, p.vorname, p.ort ,"
                + "p1.name, p1.vorname, p1.ort," + "m1.meldungsIdent, m1.skOffenlegung, m1.skIst, "
                + "p2.name, p2.vorname, p2.ort " + "FROM " + dbBundle.getSchemaMandant() + "tbl_willenserklaerung w "
                + "INNER JOIN " + dbBundle.getSchemaMandant() + "tbl_meldungen m ON m.meldungsIdent=w.meldungsIdent " //Aktionärs-Meldung zu Willenserklärung
                + "INNER JOIN " + dbBundle.getSchemaMandant() + "tbl_personenNatJur p ON m.personenNatJurIdent=p.ident " //Person zu Aktionärs-Meldung
                + "LEFT OUTER JOIN " + dbBundle.getSchemaMandant()
                + "tbl_personenNatJur p1 ON (w.willenserklaerungGeberIdent=p1.ident AND w.mandant=p1.mandant) " //Vertreter zu Willenserklärung
                + "LEFT OUTER JOIN " + dbBundle.getSchemaMandant()
                + "tbl_meldungen m1 ON (m1.meldungsIdent=w.identMeldungZuSammelkarte AND m1.mandant=w.mandant) " //Sammelkarte zu Willenserklärung
                + "LEFT OUTER JOIN " + dbBundle.getSchemaMandant()
                + "tbl_personenNatJur p2 ON (m1.personenNatJurIdent=p2.ident AND m1.mandant=p2.mandant) " //natürliche Person der Sammelkarte
                + "WHERE " + "w.mandant=? AND m.mandant=? AND p.mandant=? AND " + "(" + "w.willenserklaerung="
                + KonstWillenserklaerung.erstzugang + " OR " + "w.willenserklaerung=" + KonstWillenserklaerung.abgang
                + " OR " + "w.willenserklaerung=" + KonstWillenserklaerung.wiederzugang + " OR "
                + "w.willenserklaerung=" + KonstWillenserklaerung.vertreterwechsel + ") "
                + "ORDER BY w.willenserklaerungIdent ";

        try {
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, dbBundle.clGlobalVar.mandant);
            lPStm.setInt(2, dbBundle.clGlobalVar.mandant);
            lPStm.setInt(3, dbBundle.clGlobalVar.mandant);

            ResultSet lErgebnis = lPStm.executeQuery();
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();
            ergebnisArrayPraesenzliste = new EclPraesenzliste[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {
                ergebnisArrayPraesenzliste[i] = new EclPraesenzliste();
                ergebnisArrayPraesenzliste[i].willenserklaerungIdent = lErgebnis.getInt("w.willenserklaerungIdent");
                ergebnisArrayPraesenzliste[i].willenserklaerung = lErgebnis.getInt("w.willenserklaerung");
                ergebnisArrayPraesenzliste[i].aktien = lErgebnis.getLong("w.aktien");
                ergebnisArrayPraesenzliste[i].stimmen = lErgebnis.getLong("w.stimmen");
                ergebnisArrayPraesenzliste[i].meldeIdentAktionaer = lErgebnis.getInt("w.meldungsIdent");
                ergebnisArrayPraesenzliste[i].meldungstyp = lErgebnis.getInt("m.meldungstyp");
                ergebnisArrayPraesenzliste[i].besitzartKuerzel = lErgebnis.getString("m.besitzart");
                ergebnisArrayPraesenzliste[i].gattung = lErgebnis.getInt("m.gattung");
                //				ergebnisArrayPraesenzliste[i].meldungSkOffenlegung=lErgebnis.getInt("m.skOffenlegung");
                ergebnisArrayPraesenzliste[i].meldungSkIst = lErgebnis.getInt("m.skIst");
                ergebnisArrayPraesenzliste[i].fuelleMeldungOffenlegungTatsaechlich(dbBundle,
                        lErgebnis.getInt("m.skOffenlegung"));
                ergebnisArrayPraesenzliste[i].zutrittsIdent = lErgebnis.getString("w.zutrittsIdent");
                ergebnisArrayPraesenzliste[i].stimmkarte = lErgebnis.getString("w.stimmkarte1");
                ergebnisArrayPraesenzliste[i].aktionaerName = lErgebnis.getString("p.name");
                ergebnisArrayPraesenzliste[i].aktionaerVorname = lErgebnis.getString("p.vorname");
                ergebnisArrayPraesenzliste[i].aktionaerOrt = lErgebnis.getString("p.ort");

                ergebnisArrayPraesenzliste[i].vertreterIdent = lErgebnis.getInt("w.willenserklaerungGeberIdent");
                ergebnisArrayPraesenzliste[i].vertreterName = lErgebnis.getString("p1.name");
                ergebnisArrayPraesenzliste[i].vertreterVorname = lErgebnis.getString("p1.vorname");
                ergebnisArrayPraesenzliste[i].vertreterOrt = lErgebnis.getString("p1.ort");

                ergebnisArrayPraesenzliste[i].sammelkartenIdent = lErgebnis.getInt("m1.meldungsIdent");
                ergebnisArrayPraesenzliste[i].sammelkartenName = lErgebnis.getString("p2.name");
                ergebnisArrayPraesenzliste[i].sammelkartenVorname = lErgebnis.getString("p2.vorname");
                ergebnisArrayPraesenzliste[i].sammelkartenOrt = lErgebnis.getString("p2.ort");

                //				ergebnisArrayPraesenzliste[i].skOffenlegung=lErgebnis.getInt("m1.skOffenlegung");
                ergebnisArrayPraesenzliste[i].skIst = lErgebnis.getInt("m1.skIst");
                ergebnisArrayPraesenzliste[i].fuelleOffenlegungTatsaechlich(dbBundle,
                        lErgebnis.getInt("m1.skOffenlegung"));

                i++;
            }
            lErgebnis.close();
            lPStm.close();

        } catch (SQLException e) {
            CaBug.drucke("DbJoined.read_Praesenzliste 003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    /**********************************Suchen (für CtrlStatus)****************************************************/

    public int read_suchenAktionaersnummer(String suchName) {
        int anzInArray = 0;
        PreparedStatement lPStm = null;

        String lSql = "SELECT * " + "FROM " + dbBundle.getSchemaMandant() + "tbl_aktienregister are "
                + "LEFT OUTER JOIN " + dbBundle.getSchemaMandant()
                + "tbl_meldungen m on (m.aktionaersnummer=are.aktionaersnummer AND m.mandant=are.mandant) "
                + "where (are.aktionaersnummer=? OR  are.aktionaersnummer=? OR are.aktionaersnummer=?) "
                + "AND are.mandant=? ORDER BY are.nameKomplett ";

        try {
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setString(1, suchName);
            lPStm.setString(2, suchName + "0");
            lPStm.setString(3, suchName + "1");
            lPStm.setInt(4, dbBundle.clGlobalVar.mandant);

            ResultSet lErgebnis = lPStm.executeQuery();
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();
            ergebnisArraySuchergebnis = new EclSuchergebnis[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {
                ergebnisArraySuchergebnis[i] = new EclSuchergebnis();

                ergebnisArraySuchergebnis[i].aktionaersnummer = lErgebnis.getString("are.aktionaersnummer");
                ergebnisArraySuchergebnis[i].aktionaerName = lErgebnis.getString("are.nachname");
                ergebnisArraySuchergebnis[i].aktionaerVorname = lErgebnis.getString("are.vorname");
                ergebnisArraySuchergebnis[i].aktionaerOrt = lErgebnis.getString("are.ort");
                ergebnisArraySuchergebnis[i].nameKomplett = lErgebnis.getString("are.nameKomplett");
                ergebnisArraySuchergebnis[i].stimmen = lErgebnis.getLong("are.stimmen");
                String hString = lErgebnis.getString("m.aktionaersnummer");
                if (hString != null && !hString.isEmpty()) {
                    int lKlasse = lErgebnis.getInt("m.klasse");
                    ergebnisArraySuchergebnis[i].meldungKlasse = lKlasse;
                    ergebnisArraySuchergebnis[i].stimmen = lErgebnis.getLong("m.stimmen");
                    ergebnisArraySuchergebnis[i].zutrittsIdent = lErgebnis.getString("m.zutrittsIdent");
                    ergebnisArraySuchergebnis[i].zutrittsIdentNeben = "";
                    ergebnisArraySuchergebnis[i].zutrittsIdentGeperrt = 0;
                    ergebnisArraySuchergebnis[i].stimmkartenIdent = lErgebnis.getString("m.stimmkarte");
                    ergebnisArraySuchergebnis[i].praesent = lErgebnis.getInt("m.statusPraesenz");
                    ergebnisArraySuchergebnis[i].vertreterName = lErgebnis.getString("m.vertreterName");
                    ergebnisArraySuchergebnis[i].vertreterVorname = lErgebnis.getString("m.vertreterVorname");
                    ergebnisArraySuchergebnis[i].vertreterOrt = lErgebnis.getString("m.vertreterOrt");
                    ergebnisArraySuchergebnis[i].meldungsIdent = lErgebnis.getInt("m.meldungsIdent");

                    ergebnisArraySuchergebnis[i].zusatzfeld2 = lErgebnis.getString("m.zusatzfeld2");
                    ergebnisArraySuchergebnis[i].zusatzfeld3 = lErgebnis.getString("m.zusatzfeld3");
                    ergebnisArraySuchergebnis[i].zusatzfeld4 = lErgebnis.getString("m.zusatzfeld4");

                } else {
                    ergebnisArraySuchergebnis[i].meldungsIdent = -1;
                    ergebnisArraySuchergebnis[i].meldungKlasse = 1;

                }

                i++;
            }
            lErgebnis.close();
            lPStm.close();

        } catch (SQLException e) {
            CaBug.drucke("DbJoined.read_suchenName 003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    public int read_suchenName(String suchName) {

        int anzInArray = 0;
        PreparedStatement lPStm = null;

        String lSql = "SELECT * " + "FROM " + dbBundle.getSchemaMandant() + "tbl_aktienregister are "
                + "LEFT OUTER JOIN " + dbBundle.getSchemaMandant()
                + "tbl_meldungen m on (m.aktionaersnummer=are.aktionaersnummer AND m.mandant=are.mandant) "
                + "where are.nameKomplett like ? " + "AND are.mandant=? ORDER BY are.nameKomplett LIMIT 250";

        try {
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setString(1, "%" + suchName + "%");
            lPStm.setInt(2, dbBundle.clGlobalVar.mandant);

            ResultSet lErgebnis = lPStm.executeQuery();
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();
            ergebnisArraySuchergebnis = new EclSuchergebnis[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {
                ergebnisArraySuchergebnis[i] = new EclSuchergebnis();

                ergebnisArraySuchergebnis[i].aktionaersnummer = lErgebnis.getString("are.aktionaersnummer");
                ergebnisArraySuchergebnis[i].aktionaerName = lErgebnis.getString("are.nachname");
                ergebnisArraySuchergebnis[i].aktionaerVorname = lErgebnis.getString("are.vorname");
                ergebnisArraySuchergebnis[i].aktionaerOrt = lErgebnis.getString("are.ort");
                ergebnisArraySuchergebnis[i].nameKomplett = lErgebnis.getString("are.nameKomplett");
                ergebnisArraySuchergebnis[i].stimmen = lErgebnis.getLong("are.stimmen");
                String hString = lErgebnis.getString("m.aktionaersnummer");
                if (hString != null && !hString.isEmpty()) {
                    int lKlasse = lErgebnis.getInt("m.klasse");
                    ergebnisArraySuchergebnis[i].meldungKlasse = lKlasse;
                    ergebnisArraySuchergebnis[i].meldungstyp = lErgebnis.getInt("m.meldungstyp");
                    ergebnisArraySuchergebnis[i].stimmen = lErgebnis.getLong("m.stimmen");
                    ergebnisArraySuchergebnis[i].zutrittsIdent = lErgebnis.getString("m.zutrittsIdent");
                    ergebnisArraySuchergebnis[i].zutrittsIdentNeben = "";
                    ergebnisArraySuchergebnis[i].zutrittsIdentGeperrt = 0;
                    ergebnisArraySuchergebnis[i].stimmkartenIdent = lErgebnis.getString("m.stimmkarte");
                    ergebnisArraySuchergebnis[i].praesent = lErgebnis.getInt("m.statusPraesenz");
                    ergebnisArraySuchergebnis[i].vertreterName = lErgebnis.getString("m.vertreterName");
                    ergebnisArraySuchergebnis[i].vertreterVorname = lErgebnis.getString("m.vertreterVorname");
                    ergebnisArraySuchergebnis[i].vertreterOrt = lErgebnis.getString("m.vertreterOrt");
                    ergebnisArraySuchergebnis[i].meldungsIdent = lErgebnis.getInt("m.meldungsIdent");

                    ergebnisArraySuchergebnis[i].zusatzfeld2 = lErgebnis.getString("m.zusatzfeld2");
                    ergebnisArraySuchergebnis[i].zusatzfeld3 = lErgebnis.getString("m.zusatzfeld3");
                    ergebnisArraySuchergebnis[i].zusatzfeld4 = lErgebnis.getString("m.zusatzfeld4");

                } else {
                    ergebnisArraySuchergebnis[i].meldungsIdent = -1;
                    ergebnisArraySuchergebnis[i].meldungKlasse = 1;

                }

                i++;
            }
            lErgebnis.close();
            lPStm.close();

        } catch (SQLException e) {
            CaBug.drucke("DbJoined.read_suchenName 003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    public int read_suchenVertreter(String suchName) {

        int anzInArray = 0;
        PreparedStatement lPStm = null;

        String lSql = "SELECT * " + "FROM " + dbBundle.getSchemaMandant() + "tbl_personenNatJur p " + "INNER JOIN "
                + dbBundle.getSchemaMandant() + "tbl_willenserklaerung w ON p.ident=w.bevollmaechtigterDritterIdent "
                + "INNER JOIN " + dbBundle.getSchemaMandant() + "tbl_meldungen m ON w.meldungsIdent=m.meldungsIdent "
                + "INNER JOIN " + dbBundle.getSchemaMandant()
                + "tbl_aktienregister are ON m.aktionaersnummer=are.aktionaersnummer " + "WHERE p.name like ? "
                + "AND p.mandant=? AND w.mandant=? AND m.mandant=? AND are.mandant=? ORDER BY p.name";

        try {
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setString(1, "%" + suchName + "%");
            lPStm.setInt(2, dbBundle.clGlobalVar.mandant);
            lPStm.setInt(3, dbBundle.clGlobalVar.mandant);
            lPStm.setInt(4, dbBundle.clGlobalVar.mandant);
            lPStm.setInt(5, dbBundle.clGlobalVar.mandant);

            ResultSet lErgebnis = lPStm.executeQuery();
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();
            ergebnisArraySuchergebnis = new EclSuchergebnis[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {
                ergebnisArraySuchergebnis[i] = new EclSuchergebnis();

                ergebnisArraySuchergebnis[i].aktionaersnummer = lErgebnis.getString("are.aktionaersnummer");
                ergebnisArraySuchergebnis[i].aktionaerName = lErgebnis.getString("are.nachname");
                ergebnisArraySuchergebnis[i].aktionaerVorname = lErgebnis.getString("are.vorname");
                ergebnisArraySuchergebnis[i].aktionaerOrt = lErgebnis.getString("are.ort");
                ergebnisArraySuchergebnis[i].stimmen = lErgebnis.getLong("are.stimmen");
                ergebnisArraySuchergebnis[i].nameKomplett = lErgebnis.getString("are.nameKomplett");
                String hString = lErgebnis.getString("m.aktionaersnummer");
                if (hString != null && !hString.isEmpty()) {
                    ergebnisArraySuchergebnis[i].stimmen = lErgebnis.getLong("m.stimmen");
                    ergebnisArraySuchergebnis[i].meldungstyp = lErgebnis.getInt("m.meldungstyp");
                    ergebnisArraySuchergebnis[i].zutrittsIdent = lErgebnis.getString("m.zutrittsIdent");
                    ergebnisArraySuchergebnis[i].stimmkartenIdent = lErgebnis.getString("m.stimmkarte");
                    ergebnisArraySuchergebnis[i].praesent = lErgebnis.getInt("m.statusPraesenz");
                    ergebnisArraySuchergebnis[i].vertreterName = lErgebnis.getString("m.vertreterName");
                    ergebnisArraySuchergebnis[i].vertreterVorname = lErgebnis.getString("m.vertreterVorname");
                    ergebnisArraySuchergebnis[i].vertreterOrt = lErgebnis.getString("m.vertreterOrt");
                }

                i++;
            }
            lErgebnis.close();
            lPStm.close();

        } catch (SQLException e) {
            CaBug.drucke("DbJoined.read_suchenVertreter 003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    public int read_suchenNameMeldungen(String suchName) {

        int anzInArray = 0;
        PreparedStatement lPStm = null;

        //		String lSql="SELECT * "
        //				+ "FROM tbl_zutrittskarten z "
        //				+ "INNER JOIN tbl_meldungen m ON (z.meldungsIdentAktionaer=m.meldungsIdent or z.meldungsIdentGast=m.meldungsIdent) "
        //				+ "INNER JOIN tbl_personenNatJur p ON m.personenNatJurIdent=p.ident "
        //				+ "WHERE p.name LIKE ? "
        //				+ "AND z.mandant=? AND m.mandant=? AND p.mandant=? LIMIT 250";

        String lSql = "SELECT * " + "FROM " + dbBundle.getSchemaMandant() + "tbl_personenNatJur p " + "INNER JOIN "
                + dbBundle.getSchemaMandant()
                + "tbl_meldungen m ON (m.personenNatJurIdent=p.ident or m.vertreterIdent=p.ident)" + "LEFT JOIN "
                + dbBundle.getSchemaMandant()
                + "tbl_zutrittskarten z ON (z.meldungsIdentAktionaer=m.meldungsIdent or z.meldungsIdentGast=m.meldungsIdent) "
                + "WHERE p.name LIKE ? "
                + "AND (z.mandant=? OR isnull(z.mandant)) AND m.mandant=? AND p.mandant=? LIMIT 250";

        try {
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setString(1, "%" + suchName + "%");
            lPStm.setInt(2, dbBundle.clGlobalVar.mandant);
            lPStm.setInt(3, dbBundle.clGlobalVar.mandant);
            lPStm.setInt(4, dbBundle.clGlobalVar.mandant);

            ResultSet lErgebnis = lPStm.executeQuery();
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();
            ergebnisArraySuchergebnis = new EclSuchergebnis[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {
                ergebnisArraySuchergebnis[i] = new EclSuchergebnis();

                ergebnisArraySuchergebnis[i].aktionaersnummer = lErgebnis.getString("m.aktionaersnummer");
                ergebnisArraySuchergebnis[i].aktionaerName = lErgebnis.getString("p.name");
                ergebnisArraySuchergebnis[i].aktionaerVorname = lErgebnis.getString("p.vorname");
                ergebnisArraySuchergebnis[i].aktionaerOrt = lErgebnis.getString("p.ort");
                ergebnisArraySuchergebnis[i].stimmen = lErgebnis.getLong("m.stimmen");
                ergebnisArraySuchergebnis[i].nameKomplett = lErgebnis.getString("p.name") + " "
                        + lErgebnis.getString("p.vorname");
                ergebnisArraySuchergebnis[i].zutrittsIdent = lErgebnis.getString("z.zutrittsIdent");
                ergebnisArraySuchergebnis[i].zutrittsIdentNeben = lErgebnis.getString("z.zutrittsIdentNeben");
                ergebnisArraySuchergebnis[i].zutrittsIdentGeperrt = lErgebnis.getInt("z.zutrittsIdentIstGesperrt");
                ergebnisArraySuchergebnis[i].praesent = lErgebnis.getInt("m.statusPraesenz_Delayed");
                ergebnisArraySuchergebnis[i].meldungstyp = lErgebnis.getInt("m.meldungstyp");

                ergebnisArraySuchergebnis[i].zusatzfeld2 = lErgebnis.getString("m.zusatzfeld2");
                ergebnisArraySuchergebnis[i].zusatzfeld3 = lErgebnis.getString("m.zusatzfeld3");
                ergebnisArraySuchergebnis[i].zusatzfeld4 = lErgebnis.getString("m.zusatzfeld4");

                int lKlasse = lErgebnis.getInt("m.klasse");
                ergebnisArraySuchergebnis[i].meldungKlasse = lKlasse;
                if (lKlasse == 1) {
                    ergebnisArraySuchergebnis[i].stimmen = lErgebnis.getLong("m.stimmen");
                    ergebnisArraySuchergebnis[i].stimmkartenIdent = lErgebnis.getString("m.stimmkarte");
                    ergebnisArraySuchergebnis[i].vertreterName = lErgebnis.getString("m.vertreterName");
                    ergebnisArraySuchergebnis[i].vertreterVorname = lErgebnis.getString("m.vertreterVorname");
                    ergebnisArraySuchergebnis[i].vertreterOrt = lErgebnis.getString("m.vertreterOrt");
                }

                ergebnisArraySuchergebnis[i].meldungsIdent = lErgebnis.getInt("m.meldungsIdent");

                i++;
            }
            lErgebnis.close();
            lPStm.close();

        } catch (SQLException e) {
            CaBug.drucke("DbJoined.read_suchenZutrittsIdent 003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    public int read_suchenZutrittsIdent(String suchName) {

        int anzInArray = 0;
        PreparedStatement lPStm = null;

        String lSql = "SELECT * " + "FROM " + dbBundle.getSchemaMandant() + "tbl_zutrittskarten z " + "INNER JOIN "
                + dbBundle.getSchemaMandant()
                + "tbl_meldungen m ON (z.meldungsIdentAktionaer=m.meldungsIdent or z.meldungsIdentGast=m.meldungsIdent) "
                + "INNER JOIN " + dbBundle.getSchemaMandant() + "tbl_personenNatJur p ON m.personenNatJurIdent=p.ident "
                + "WHERE z.zutrittsIdent=? " + "AND z.mandant=? AND m.mandant=? AND p.mandant=?";

        try {
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setString(1, suchName);
            lPStm.setInt(2, dbBundle.clGlobalVar.mandant);
            lPStm.setInt(3, dbBundle.clGlobalVar.mandant);
            lPStm.setInt(4, dbBundle.clGlobalVar.mandant);

            ResultSet lErgebnis = lPStm.executeQuery();
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();
            ergebnisArraySuchergebnis = new EclSuchergebnis[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {
                ergebnisArraySuchergebnis[i] = new EclSuchergebnis();

                ergebnisArraySuchergebnis[i].aktionaersnummer = lErgebnis.getString("m.aktionaersnummer");
                ergebnisArraySuchergebnis[i].aktionaerName = lErgebnis.getString("p.name");
                ergebnisArraySuchergebnis[i].aktionaerVorname = lErgebnis.getString("p.vorname");
                ergebnisArraySuchergebnis[i].aktionaerOrt = lErgebnis.getString("p.ort");
                ergebnisArraySuchergebnis[i].stimmen = lErgebnis.getLong("m.stimmen");
                ergebnisArraySuchergebnis[i].nameKomplett = lErgebnis.getString("p.name") + " "
                        + lErgebnis.getString("p.vorname");
                ergebnisArraySuchergebnis[i].zutrittsIdent = lErgebnis.getString("z.zutrittsIdent");
                ergebnisArraySuchergebnis[i].zutrittsIdentNeben = lErgebnis.getString("z.zutrittsIdentNeben");
                ergebnisArraySuchergebnis[i].zutrittsIdentGeperrt = lErgebnis.getInt("z.zutrittsIdentIstGesperrt");
                ergebnisArraySuchergebnis[i].stimmkartenIdent = lErgebnis.getString("m.stimmkarte");
                ergebnisArraySuchergebnis[i].praesent = lErgebnis.getInt("m.statusPraesenz_Delayed");
                ergebnisArraySuchergebnis[i].meldungstyp = lErgebnis.getInt("m.meldungstyp");

                ergebnisArraySuchergebnis[i].zusatzfeld2 = lErgebnis.getString("m.zusatzfeld2");
                ergebnisArraySuchergebnis[i].zusatzfeld3 = lErgebnis.getString("m.zusatzfeld3");
                ergebnisArraySuchergebnis[i].zusatzfeld4 = lErgebnis.getString("m.zusatzfeld4");

                int lKlasse = lErgebnis.getInt("m.klasse");
                ergebnisArraySuchergebnis[i].meldungKlasse = lKlasse;
                if (lKlasse == 1) {
                    ergebnisArraySuchergebnis[i].stimmen = lErgebnis.getLong("m.stimmen");
                    ergebnisArraySuchergebnis[i].stimmkartenIdent = lErgebnis.getString("m.stimmkarte");
                    ergebnisArraySuchergebnis[i].vertreterName = lErgebnis.getString("m.vertreterName");
                    ergebnisArraySuchergebnis[i].vertreterVorname = lErgebnis.getString("m.vertreterVorname");
                    ergebnisArraySuchergebnis[i].vertreterOrt = lErgebnis.getString("m.vertreterOrt");
                }

                ergebnisArraySuchergebnis[i].meldungsIdent = lErgebnis.getInt("m.meldungsIdent");

                i++;
            }
            lErgebnis.close();
            lPStm.close();

        } catch (SQLException e) {
            CaBug.drucke("DbJoined.read_suchenZutrittsIdent 003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    public int read_suchenStimmkarte(String suchName) {

        int anzInArray = 0;
        PreparedStatement lPStm = null;

        String lSql = "SELECT * " + "FROM " + dbBundle.getSchemaMandant() + "tbl_stimmkarten s " + "INNER JOIN "
                + dbBundle.getSchemaMandant() + "tbl_meldungen m ON s.meldungsIdentAktionaer=m.meldungsIdent "
                + "INNER JOIN " + dbBundle.getSchemaMandant()
                + "tbl_aktienregister are ON m.aktionaersnummer=are.aktionaersnummer " + "WHERE s.stimmkarte=? "
                + "AND s.mandant=? AND m.mandant=? AND are.mandant=? ";

        try {
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setString(1, suchName);
            lPStm.setInt(2, dbBundle.clGlobalVar.mandant);
            lPStm.setInt(3, dbBundle.clGlobalVar.mandant);
            lPStm.setInt(4, dbBundle.clGlobalVar.mandant);

            ResultSet lErgebnis = lPStm.executeQuery();
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();
            ergebnisArraySuchergebnis = new EclSuchergebnis[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {
                ergebnisArraySuchergebnis[i] = new EclSuchergebnis();

                ergebnisArraySuchergebnis[i].aktionaersnummer = lErgebnis.getString("are.aktionaersnummer");
                ergebnisArraySuchergebnis[i].aktionaerName = lErgebnis.getString("are.nachname");
                ergebnisArraySuchergebnis[i].aktionaerVorname = lErgebnis.getString("are.vorname");
                ergebnisArraySuchergebnis[i].aktionaerOrt = lErgebnis.getString("are.ort");
                ergebnisArraySuchergebnis[i].stimmen = lErgebnis.getLong("are.stimmen");
                ergebnisArraySuchergebnis[i].nameKomplett = lErgebnis.getString("are.nameKomplett");
                String hString = lErgebnis.getString("m.aktionaersnummer");
                if (hString != null && !hString.isEmpty()) {
                    ergebnisArraySuchergebnis[i].meldungstyp = lErgebnis.getInt("m.meldungstyp");
                    ergebnisArraySuchergebnis[i].stimmen = lErgebnis.getLong("m.stimmen");
                    ergebnisArraySuchergebnis[i].zutrittsIdent = lErgebnis.getString("m.zutrittsIdent");
                    ergebnisArraySuchergebnis[i].stimmkartenIdent = lErgebnis.getString("m.stimmkarte");
                    ergebnisArraySuchergebnis[i].praesent = lErgebnis.getInt("m.statusPraesenz");
                    ergebnisArraySuchergebnis[i].vertreterName = lErgebnis.getString("m.vertreterName");
                    ergebnisArraySuchergebnis[i].vertreterVorname = lErgebnis.getString("m.vertreterVorname");
                    ergebnisArraySuchergebnis[i].vertreterOrt = lErgebnis.getString("m.vertreterOrt");
                }

                i++;
            }
            lErgebnis.close();
            lPStm.close();

        } catch (SQLException e) {
            CaBug.drucke("DbJoined.read_suchenStimmkarte 003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

}
