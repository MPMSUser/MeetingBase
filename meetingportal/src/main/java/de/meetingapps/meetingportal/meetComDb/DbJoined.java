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
import de.meetingapps.meetingportal.meetComEntities.EclAbstimmungMeldung;
import de.meetingapps.meetingportal.meetComEntities.EclAbstimmungMeldungSplit;
import de.meetingapps.meetingportal.meetComEntities.EclAktienregister;
import de.meetingapps.meetingportal.meetComEntities.EclAktienregisterZusatz;
import de.meetingapps.meetingportal.meetComEntities.EclAnrede;
import de.meetingapps.meetingportal.meetComEntities.EclInstiBestandsZuordnung;
import de.meetingapps.meetingportal.meetComEntities.EclLoginDaten;
import de.meetingapps.meetingportal.meetComEntities.EclMailingVariablen;
import de.meetingapps.meetingportal.meetComEntities.EclMeldung;
import de.meetingapps.meetingportal.meetComEntities.EclPersonenNatJur;
import de.meetingapps.meetingportal.meetComEntities.EclPersonenNatJurVersandadresse;
import de.meetingapps.meetingportal.meetComEntities.EclPraesenzliste;
import de.meetingapps.meetingportal.meetComEntities.EclStaaten;
import de.meetingapps.meetingportal.meetComEntities.EclStimmkarten;
import de.meetingapps.meetingportal.meetComEntities.EclSuchergebnis;
import de.meetingapps.meetingportal.meetComEntities.EclSuchlaufErgebnis;
import de.meetingapps.meetingportal.meetComEntities.EclUserLogin;
import de.meetingapps.meetingportal.meetComEntities.EclWeisungMeldung;
import de.meetingapps.meetingportal.meetComEntities.EclWillenserklaerung;
import de.meetingapps.meetingportal.meetComEntities.EclWillenserklaerungZusatz;
import de.meetingapps.meetingportal.meetComEntities.EclZutrittskarten;
import de.meetingapps.meetingportal.meetComHVParam.ParamSpezial;
import de.meetingapps.meetingportal.meetComKonst.KonstSuchlaufSuchbegriffArt;
import de.meetingapps.meetingportal.meetComKonst.KonstWillenserklaerung;
import de.meetingapps.meetingportal.meetComKonst.KonstWillenserklaerungVersandartEK;

public class DbJoined {

    int logDruckenInt = 3;

    private Connection verbindung = null;
    // private DbBasis dbBasis=null;
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
    @Deprecated
    private EclAktienregisterZusatz ergebnisArrayAktienregisterZusatz[] = null;
    private EclLoginDaten ergebnisArrayLoginDaten[] = null;
    private EclLoginDaten ergebnisArrayLoginDaten1[] = null;
    private EclAktienregister ergebnisArrayAktienregisterEintrag[] = null;
    private EclWeisungMeldung ergebnisArrayWeisungMeldung[] = null;
    private EclSuchergebnis ergebnisArraySuchergebnis[] = null;
    private EclAbstimmungMeldung ergebnisArrayAbstimmungMeldung[] = null;
    private EclAbstimmungMeldungSplit ergebnisArrayAbstimmungMeldungSplit[] = null;
    private EclSuchlaufErgebnis ergebnisArraySuchlaufErgebnis[] = null;
    private EclUserLogin ergebnisArrayUserLogin[] = null;
    private EclInstiBestandsZuordnung ergebnisInstiBestandsZuordnung[] = null;

    private EclStimmkarten ergebnisArrayStimmkarten[]=null;
    private EclZutrittskarten ergebnisArrayZutrittskarten[]=null;
    
    /************************* Initialisierung ***************************/
    public DbJoined(DbBundle pDbBundle) {
        /* Verbindung in lokale Daten eintragen */
        if (pDbBundle == null) {
            CaBug.drucke("001 - dbBundle nicht initialisiert");
            return;
        }
        if (pDbBundle.dbBasis == null) {
            CaBug.drucke("002 - dbBasis nicht initialisiert");
            return;
        }

        // dbBasis=pDbBundle.dbBasis;
        verbindung = pDbBundle.dbBasis.verbindung;
        dbBundle = pDbBundle;
    }

    /**************
     * Anzahl der Ergebnisse der read*-Methoden
     **************************/
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

    @Deprecated
    public int anzErgebnisAktienregisterZusatz() {
        if (ergebnisArrayAktienregisterZusatz == null) {
            return 0;
        }
        return ergebnisArrayAktienregisterZusatz.length;
    }

    public int anzErgebnisLoginDaten() {
        if (ergebnisArrayLoginDaten == null) {
            return 0;
        }
        return ergebnisArrayLoginDaten.length;
    }

    public int anzErgebnisLoginDaten1() {
        if (ergebnisArrayLoginDaten1 == null) {
            return 0;
        }
        return ergebnisArrayLoginDaten1.length;
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

    public int anzErgebnisAbstimmungMeldung() {
        if (ergebnisArrayAbstimmungMeldung == null) {
            return 0;
        }
        return ergebnisArrayAbstimmungMeldung.length;
    }

    public int anzErgebnisAbstimmungMeldungSplit() {
        if (ergebnisArrayAbstimmungMeldungSplit == null) {
            return 0;
        }
        return ergebnisArrayAbstimmungMeldungSplit.length;
    }

    public int anzErgebnisSuchlaufErgebnis() {
        if (ergebnisArraySuchlaufErgebnis == null) {
            return 0;
        }
        return ergebnisArraySuchlaufErgebnis.length;
    }

    public int anzErgebnisUserLogin() {
        if (ergebnisArrayUserLogin == null) {
            return 0;
        }
        return ergebnisArrayUserLogin.length;
    }

    public int anzInstiBestandsZuordnung() {
        if (ergebnisInstiBestandsZuordnung == null) {
            return 0;
        }
        return ergebnisInstiBestandsZuordnung.length;
    }

    public int anzSimmkarten() {
        if (ergebnisArrayStimmkarten == null) {
            return 0;
        }
        return ergebnisArrayStimmkarten.length;
    }
    public int anzZutrittskarten() {
        if (ergebnisArrayZutrittskarten == null) {
            return 0;
        }
        return ergebnisArrayZutrittskarten.length;
    }

    /**********
     * Liefert pN-tes Element des Ergebnisses der read*Methoden************** pN
     * geht von 0 bis anzErgebnis-1
     */
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

    @Deprecated
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

    public EclLoginDaten ergebnisLoginDatenPosition(int pN) {
        if (ergebnisArrayLoginDaten == null) {
            CaBug.drucke("001");
            return null;
        }
        if (pN < 0) {
            CaBug.drucke("002");
            return null;
        }
        if (pN >= ergebnisArrayLoginDaten.length) {
            CaBug.drucke("003");
            return null;
        }
        return ergebnisArrayLoginDaten[pN];
    }

    public EclLoginDaten ergebnisLoginDatenPosition1(int pN) {
        if (ergebnisArrayLoginDaten1 == null) {
            CaBug.drucke("001");
            return null;
        }
        if (pN < 0) {
            CaBug.drucke("002");
            return null;
        }
        if (pN >= ergebnisArrayLoginDaten1.length) {
            CaBug.drucke("003");
            return null;
        }
        return ergebnisArrayLoginDaten1[pN];
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

    public EclAbstimmungMeldung ergebnisAbstimmungMeldungPosition(int pN) {
        if (ergebnisArrayAbstimmungMeldung == null) {
            CaBug.drucke("DbJoined.ergebnisAbstimmungMeldungPosition 001");
            return null;
        }
        if (pN < 0) {
            CaBug.drucke("DbJoined.ergebnisAbstimmungMeldungPosition 002");
            return null;
        }
        if (pN >= ergebnisArrayAbstimmungMeldung.length) {
            CaBug.drucke("DbJoined.ergebnisAbstimmungMeldungPosition 003");
            return null;
        }
        return ergebnisArrayAbstimmungMeldung[pN];
    }

    public EclAbstimmungMeldungSplit ergebnisAbstimmungMeldungSplitPosition(int pN) {
        if (ergebnisArrayAbstimmungMeldungSplit == null) {
            CaBug.drucke("DbJoined.ergebnisAbstimmungMeldungSplitPosition 001");
            return null;
        }
        if (pN < 0) {
            CaBug.drucke("DbJoined.ergebnisAbstimmungMeldungSplitPosition 002");
            return null;
        }
        if (pN >= ergebnisArrayAbstimmungMeldungSplit.length) {
            CaBug.drucke("DbJoined.ergebnisAbstimmungMeldungSplitPosition 003");
            return null;
        }
        return ergebnisArrayAbstimmungMeldungSplit[pN];
    }

    public EclSuchlaufErgebnis ergebnisSuchlaufErgebnisPosition(int pN) {
        if (ergebnisArraySuchlaufErgebnis == null) {
            CaBug.drucke("DbJoined.ergebnisSuchlaufErgebnisPosition 001");
            return null;
        }
        if (pN < 0) {
            CaBug.drucke("DbJoined.ergebnisSuchlaufErgebnisPosition 002");
            return null;
        }
        if (pN >= ergebnisArraySuchlaufErgebnis.length) {
            CaBug.drucke("DbJoined.ergebnisSuchlaufErgebnisPosition 003");
            return null;
        }
        return ergebnisArraySuchlaufErgebnis[pN];
    }

    public EclUserLogin ergebnisUserLoginPosition(int pN) {
        if (ergebnisArrayUserLogin == null) {
            CaBug.drucke("DbJoined.ergebnisUserLoginPosition 001");
            return null;
        }
        if (pN < 0) {
            CaBug.drucke("DbJoined.ergebnisUserLoginPosition 002");
            return null;
        }
        if (pN >= ergebnisArrayUserLogin.length) {
            CaBug.drucke("DbJoined.ergebnisUserLoginPosition 003");
            return null;
        }
        return ergebnisArrayUserLogin[pN];
    }

    public EclInstiBestandsZuordnung ergebnisInstiBestandsZuordnungPosition(int pN) {
        if (ergebnisInstiBestandsZuordnung == null) {
            CaBug.drucke("DbJoined.ergebnisInstiBestandsZuordnungPosition 001");
            return null;
        }
        if (pN < 0) {
            CaBug.drucke("DbJoined.ergebnisInstiBestandsZuordnungPosition 002");
            return null;
        }
        if (pN >= ergebnisInstiBestandsZuordnung.length) {
            CaBug.drucke("DbJoined.ergebnisInstiBestandsZuordnungPosition 003");
            return null;
        }
        return ergebnisInstiBestandsZuordnung[pN];
    }

    public EclStimmkarten ergebnisStimmkartenPosition(int pN) {
        if (ergebnisArrayStimmkarten == null) {
            CaBug.drucke("001");
            return null;
        }
        if (pN < 0) {
            CaBug.drucke("002");
            return null;
        }
        if (pN >= ergebnisArrayStimmkarten.length) {
            CaBug.drucke("003");
            return null;
        }
        return ergebnisArrayStimmkarten[pN];
    }
    public EclZutrittskarten ergebnisZutrittskartenPosition(int pN) {
        if (ergebnisArrayZutrittskarten == null) {
            CaBug.drucke("001");
            return null;
        }
        if (pN < 0) {
            CaBug.drucke("002");
            return null;
        }
        if (pN >= ergebnisArrayZutrittskarten.length) {
            CaBug.drucke("003");
            return null;
        }
        return ergebnisArrayZutrittskarten[pN];
    }

    /********** Liefert Liste des Ergebnisses der read*Methoden **************/
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

    @Deprecated
    public EclAktienregisterZusatz[] ergebnisAktienregisterZusatz() {
        if (ergebnisArrayAktienregisterZusatz == null) {
            CaBug.drucke("DbJoined.ergebnisAktienregisterZusatz 001");
        }
        return ergebnisArrayAktienregisterZusatz;
    }

    public EclLoginDaten[] ergebnisLoginDaten() {
        if (ergebnisArrayLoginDaten == null) {
            CaBug.drucke("001");
        }
        return ergebnisArrayLoginDaten;
    }

    public EclLoginDaten[] ergebnisLoginDaten1() {
        if (ergebnisArrayLoginDaten1 == null) {
            CaBug.drucke("001");
        }
        return ergebnisArrayLoginDaten1;
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

    public EclAbstimmungMeldung[] ergebnisAbstimmungMeldung() {
        if (ergebnisArrayAbstimmungMeldung == null) {
            CaBug.drucke("DbJoined.ergebnisAbstimmungMeldung 001");
        }
        return ergebnisArrayAbstimmungMeldung;
    }

    public EclAbstimmungMeldungSplit[] ergebnisAbstimmungMeldungSokut() {
        if (ergebnisArrayAbstimmungMeldungSplit == null) {
            CaBug.drucke("DbJoined.ergebnisAbstimmungMeldungSplit 001");
        }
        return ergebnisArrayAbstimmungMeldungSplit;
    }

    public EclSuchlaufErgebnis[] ergebnisSuchlaufErgebnis() {
        if (ergebnisArraySuchlaufErgebnis == null) {
            CaBug.drucke("DbJoined.ergebnisSuchlaufErgebnis 001");
        }
        return ergebnisArraySuchlaufErgebnis;
    }

    public EclUserLogin[] ergebnisUserLogin() {
        if (ergebnisArrayUserLogin == null) {
            CaBug.drucke("DbJoined.ergebnisUserLogin 001");
        }
        return ergebnisArrayUserLogin;
    }

    public EclInstiBestandsZuordnung[] ergebnisInstiBestandsZuordnung() {
        if (ergebnisArrayUserLogin == null) {
            CaBug.drucke("DbJoined.ergebnisInstiBestandsZuordnung 001");
        }
        return ergebnisInstiBestandsZuordnung;
    }

    public EclStimmkarten[] ergebnisStimmkarten() {
        if (ergebnisArrayStimmkarten == null) {
            CaBug.drucke("001");
        }
        return ergebnisArrayStimmkarten;
    }
    public EclZutrittskarten[] ergebnisZutrittskarten() {
        if (ergebnisArrayZutrittskarten == null) {
            CaBug.drucke("001");
        }
        return ergebnisArrayZutrittskarten;
    }

    /**
     * Liefert alle meldungen / Willenserklärungen für noch nicht gedruckte
     * Eintrittskarten (mit Versandart Post) als Basis zum Kontrollieren der
     * Versandadresse
     * 
     * @param nurInternet:             true=liefert nur die über das Internet vom
     *                                 Aktionär selbst eingegebenen Bestellungen
     *                                 false=liefert alle
     * @param nurNochNichtGeprueft     true=liefert nur die noch nicht geprüften
     *                                 Bestellungen false=liefert auch die bereits
     *                                 geprüften Bestellungen (soweit sie noch nicht
     *                                 versandt wurden)
     * @param nurAbweichendeEingegeben true=liefert nur die Bestellungen, bei denen
     *                                 manuell eine abweichende Versandadresse
     *                                 eingegeben wurde false=liefert alle
     * @return
     */
    public int read_versandadressePruefen(boolean nurInternet, boolean nurNochNichtGeprueft,
            boolean nurAbweichendeEingegeben) {

        int anzInArray = 0;
        PreparedStatement lPStm = null;

        String lSql =
                // "SELECT m.*, p.*, st.*, pv.*, st1.*, wz.*, w.*, an.*, anf.* FROM
                // (((tbl_PersonenNatJur p "
                // +"INNER JOIN (tbl_meldungen m INNER JOIN (tbl_willenserklaerungzusatz wz
                // INNER JOIN tbl_willenserklaerung w ON
                // wz.willenserklaerungIdent=w.willenserklaerungIdent) "
                // +"ON m.meldungsIdent=wz.meldungsIdent) ON m.personenNatJurIdent=p.ident) "
                // + "LEFT OUTER JOIN tbl_personenNatJurVersandadresse pv ON
                // (p.identVersandadresse=pv.ident AND p.mandant=pv.mandant)) LEFT OUTER JOIN
                // tbl_anreden an ON p.anrede=an.anredennr) "
                // + "LEFT OUTER JOIN tbl_anredenfremd anf ON an.anredennr=anf.anredennr "
                // + "LEFT OUTER JOIN tbl_staaten st on p.land=st.code "
                // + "LEFT OUTER JOIN tbl_staaten st1 on pv.staatIdVersand=st1.id "
                // +"WHERE m.mandant=? AND m.meldungAktiv=1 AND p.mandant=? AND wz.mandant=? AND
                // w.mandant=? AND wz.willenserklaerung=100 AND wz.anmeldungIstStorniert!=1 AND
                // (wz.versandartEK=1 OR wz.versandartEK=2) AND anf.sprachennr=2 ";
                "SELECT m.*, p.*, st.*, pv.*, are.*, st1.*, wz.*, w.*, an.*, anf.*, anv.*, anfv.* FROM ((("
                        + dbBundle.getSchemaMandant() + "tbl_PersonenNatJur p " + "INNER JOIN ("
                        + dbBundle.getSchemaMandant() + "tbl_meldungen m INNER JOIN (" + dbBundle.getSchemaMandant()
                        + "tbl_willenserklaerungzusatz wz INNER JOIN " + dbBundle.getSchemaMandant()
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
         * "SELECT m.*, p.*, wz.*, w.* FROM tbl_meldungen m "
         * +"INNER JOIN (tbl_PersonenNatJur p INNER JOIN (tbl_willenserklaerungzusatz wz INNER JOIN tbl_willenserklaerung w ON wz.willenserklaerungIdent=w.willenserklaerungIdent) "
         * +"ON p.ident=wz.meldungsIdent) ON m.personenNatJurIdent=p.ident "
         * +"WHERE m.mandant=? AND p.mandant=? AND wz.mandant=? AND w.mandant=? AND wz.willenserklaerung=100 AND wz.anmeldungIstStorniert!=-1 AND (wz.versandartEK=1 OR wz.versandartEK=2) "
         * ;
         */
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
        // System.out.println(lSql);
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

    /**
     * Liefert alle meldungen / Willenserklärungen für den Eintrittskartendruck
     * 
     * @param erstDruck:   true=Erstdruck - es werden alle noch nicht gedruckten
     *                     Eintrittskarten mit Versandart 1 oder 2 geliefert
     *                     false=Druckwiederholung, in drucklauf wird die Nummer des
     *                     zu wiederholenden Laufes übergeben
     * @param drucklauf    Nummer des zu wiederholenden Drucklaufs (falls
     *                     erstDruck==false)
     * @param nurGepruefte true=falls manuell eingegebene Versandadressen vorhanden,
     *                     dann werden die nur gedruckt wenn sie geprüft wurden
     *                     false=auch ungeprüfte werden gedruckt
     * 
     *                     ekNummer: wenn dieser Wert enthält, werden erstDruck,
     *                     drucklauf, nurGepruefte ignoriert, und die Daten für
     *                     exakt diese
     * 
     *                     pVonZutrittsIdent: wenn dieser Wert enthält, dann werden
     *                     nur die Eintrittskarten >=pVonZutrittsIdent und
     *                     pBisZutrittsIdent gedruckt (beide Werte müssen
     *                     aufbereitet sein auf 5-stellig) Eintrittskartennummer
     *                     zurückgeliefert. Dient z.B. zum Nachdruck einer einzelnen
     *                     Eintrittskarte (auch auf HV)
     * @return
     */
    @Deprecated
    public int read_eintrittskartenDruck(boolean erstDruck, int drucklauf, boolean nurGepruefte, String ekNummer,
            String ekNummerNeben, int pGastOderAktionaer, String pVonZutrittsIdent, String pBisVonZutrittsIdent, int pEkVersandFuerAlleImPortalAngeforderten) {

//        int anzInArray = 0;
//        PreparedStatement lPStm = null;
//        String lSql = "";
//
//        if (ekNummer.isEmpty()) {
//            lSql =
//                    // "SELECT m.*, p.*, p1.*, st.*, pv.*, st1.*, wz.*, w.*, an.*, anf.* FROM
//                    // (((tbl_PersonenNatJur p "
//                    // +"INNER JOIN (tbl_meldungen m INNER JOIN (tbl_willenserklaerungzusatz wz
//                    // INNER JOIN tbl_willenserklaerung w ON
//                    // wz.willenserklaerungIdent=w.willenserklaerungIdent) "
//                    // +"ON m.meldungsIdent=wz.meldungsIdent) ON m.personenNatJurIdent=p.ident) "
//                    // + "LEFT OUTER JOIN tbl_personenNatJurVersandadresse pv ON
//                    // (p.identVersandadresse=pv.ident AND p.mandant=pv.mandant)) LEFT OUTER JOIN
//                    // tbl_anreden an ON p.anrede=an.anredennr) "
//                    // + "LEFT OUTER JOIN tbl_anredenfremd anf ON an.anredennr=anf.anredennr "
//                    // + "LEFT OUTER JOIN tbl_staaten st on p.land=st.code "
//                    // + "LEFT OUTER JOIN tbl_staaten st1 on pv.staatIdVersand=st1.id "
//                    // + "LEFT OUTER JOIN tbl_personenNatJur p1 on
//                    // (wz.identVertreterPersonNatJur=p1.ident AND wz.mandant=p1.mandant) "
//                    // +"WHERE m.mandant=? AND p.mandant=? AND m.meldungAktiv=1 AND wz.mandant=? AND
//                    // w.mandant=? AND wz.willenserklaerung=100 AND wz.anmeldungIstStorniert!=1 AND
//                    // (wz.versandartEK=1 OR wz.versandartEK=2) AND anf.sprachennr=2 ";
//                    "SELECT m.*, p.*, p1.*, st.*, pv.*, are.*, st1.*, wz.*, w.*, an.*, anf.*, anv.*, anfv.* FROM ((("
//                            + dbBundle.getSchemaMandant() + "tbl_PersonenNatJur p " + "INNER JOIN ("
//                            + dbBundle.getSchemaMandant() + "tbl_meldungen m " + "INNER JOIN ("
//                            + dbBundle.getSchemaMandant() + "tbl_willenserklaerungzusatz wz " + "INNER JOIN "
//                            + dbBundle.getSchemaMandant()
//                            + "tbl_willenserklaerung w ON wz.willenserklaerungIdent=w.willenserklaerungIdent) ";
//            if (pGastOderAktionaer == 2) {
//                lSql = lSql + "ON m.meldungsIdent=wz.meldungsIdent)";
//            } else {
//                lSql = lSql + "ON m.meldungsIdent=wz.meldungsIdentGast)";
//            }
//            lSql = lSql + " ON m.personenNatJurIdent=p.ident) " + "LEFT OUTER JOIN " + dbBundle.getSchemaMandant()
//                    + "tbl_personenNatJurVersandadresse pv ON (p.identVersandadresse=pv.ident AND p.mandant=pv.mandant)) LEFT OUTER JOIN "
//                    + dbBundle.getSchemaAllgemein() + "tbl_anreden an ON p.anrede=an.anredennr) " + "LEFT OUTER JOIN "
//                    + dbBundle.getSchemaMandant() + "tbl_aktienregister are ON m.aktionaersnummer=are.aktionaersnummer "
//                    + "LEFT OUTER JOIN " + dbBundle.getSchemaAllgemein()
//                    + "tbl_anredenfremd anf ON an.anredennr=anf.anredennr " + " LEFT OUTER JOIN "
//                    + dbBundle.getSchemaAllgemein() + "tbl_anreden anv on pv.anredeIdVersand=anv.anredennr "
//                    + " LEFT OUTER JOIN " + dbBundle.getSchemaAllgemein()
//                    + "tbl_anredenfremd anfv on anv.anredennr=anfv.anredennr " + "LEFT OUTER JOIN "
//                    + dbBundle.getSchemaAllgemein() + "tbl_staaten st on p.land=st.code " + "LEFT OUTER JOIN "
//                    + dbBundle.getSchemaAllgemein() + "tbl_staaten st1 on pv.staatIdVersand=st1.id "
//                    + "LEFT OUTER JOIN " + dbBundle.getSchemaMandant()
//                    + "tbl_personenNatJur p1 on (wz.identVertreterPersonNatJur=p1.ident AND wz.mandant=p1.mandant) "
//                    + "WHERE m.mandant=? AND p.mandant=? AND m.meldungAktiv=1 AND wz.mandant=? AND w.mandant=? AND "
//                    + "(are.mandant=? OR isnull(are.mandant)) AND " /* Geändert! */
//                    + "wz.willenserklaerung=100 AND wz.anmeldungIstStorniert!=1 AND (wz.versandartEK=1 OR wz.versandartEK=2) AND "
//                    + "(anf.sprachennr=2 OR isnull(anf.sprachennr)) AND (anfv.sprachennr=2 OR isnull(anfv.sprachennr)) "; /*
//                                                                                                                           * Geändert
//                                                                                                                           * -
//                                                                                                                           * auch
//                                                                                                                           * ||
//                                                                                                                           * in
//                                                                                                                           * OR
//                                                                                                                           * in
//                                                                                                                           * der
//                                                                                                                           * zweiten
//                                                                                                                           * Bedingung
//                                                                                                                           */
//            if (erstDruck) {
//                lSql = lSql + "AND wz.eintrittskarteWurdeGedruckt=0 ";
//            } else {
//                lSql = lSql + "AND wz.eintrittskarteWurdeGedruckt=? ";
//            }
//            if (nurGepruefte) {
//                lSql = lSql + " AND (wz.versandadresseUeberprueft!=0 OR wz.versandartEK!=2) ";
//            }
//
//            if (!pVonZutrittsIdent.isEmpty()) {
//                lSql = lSql + " AND m.zutrittsIdent>=? AND m.zutrittsIdent<=?";
//            }
//
//            lSql = lSql + " ORDER BY w.benutzernr, w.willenserklaerungIdent;";
//        } else {/* Nur einzelne bestimmte EK drucken */
//            lSql = "SELECT m.*, p.*, p1.*, st.*, pv.*, are.*, st1.*, wz.*, w.*, an.*, anf.*, anv.*, anfv.* FROM ((("
//                    + dbBundle.getSchemaMandant() + "tbl_PersonenNatJur p " + "INNER JOIN ("
//                    + dbBundle.getSchemaMandant() + "tbl_meldungen m INNER JOIN (" + dbBundle.getSchemaMandant()
//                    + "tbl_willenserklaerungzusatz wz INNER JOIN " + dbBundle.getSchemaMandant()
//                    + "tbl_willenserklaerung w ON wz.willenserklaerungIdent=w.willenserklaerungIdent) ";
//            if (pGastOderAktionaer == 2) {
//                lSql = lSql + "ON m.meldungsIdent=wz.meldungsIdent)";
//            } else {
//                lSql = lSql + "ON m.meldungsIdent=wz.meldungsIdentGast)";
//            }
//            lSql = lSql + " ON m.personenNatJurIdent=p.ident) " + "LEFT OUTER JOIN " + dbBundle.getSchemaMandant()
//                    + "tbl_personenNatJurVersandadresse pv ON (p.identVersandadresse=pv.ident AND p.mandant=pv.mandant)) LEFT OUTER JOIN "
//                    + dbBundle.getSchemaAllgemein() + "tbl_anreden an ON p.anrede=an.anredennr) " + "LEFT OUTER JOIN "
//                    + dbBundle.getSchemaMandant() + "tbl_aktienregister are ON m.aktionaersnummer=are.aktionaersnummer "
//                    + "LEFT OUTER JOIN " + dbBundle.getSchemaAllgemein()
//                    + "tbl_anredenfremd anf ON an.anredennr=anf.anredennr " + " LEFT OUTER JOIN "
//                    + dbBundle.getSchemaAllgemein() + "tbl_anreden anv on pv.anredeIdVersand=anv.anredennr "
//                    + " LEFT OUTER JOIN " + dbBundle.getSchemaAllgemein()
//                    + "tbl_anredenfremd anfv on anv.anredennr=anfv.anredennr " + "LEFT OUTER JOIN "
//                    + dbBundle.getSchemaAllgemein() + "tbl_staaten st on p.land=st.code " + "LEFT OUTER JOIN "
//                    + dbBundle.getSchemaAllgemein() + "tbl_staaten st1 on pv.staatIdVersand=st1.id "
//                    + "LEFT OUTER JOIN " + dbBundle.getSchemaMandant()
//                    + "tbl_personenNatJur p1 on (wz.identVertreterPersonNatJur=p1.ident AND wz.mandant=p1.mandant) "
//                    + "WHERE m.mandant=? AND p.mandant=? AND m.meldungAktiv=1 AND wz.mandant=? AND w.mandant=? AND "
//                    + "(are.mandant=?  OR isnull(are.mandant)) AND " /* Geändert */
//                    + "wz.willenserklaerung=100 AND wz.anmeldungIstStorniert!=1 AND w.zutrittsIdent=? AND w.zutrittsIdentNeben=? AND "
//                    + "(anf.sprachennr=2 OR isnull(anf.sprachennr))  AND (anfv.sprachennr=2 OR isnull(anfv.sprachennr)) ";/*
//                                                                                                                           * Geändert
//                                                                                                                           * -
//                                                                                                                           * auch
//                                                                                                                           * ||
//                                                                                                                           * in
//                                                                                                                           * OR
//                                                                                                                           * in
//                                                                                                                           * der
//                                                                                                                           * zweiten
//                                                                                                                           * Bedingung
//                                                                                                                           */
//
//        }
//
//        System.out.println(lSql);
//        try {
//            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
//            int offset = 1;
//            lPStm.setInt(offset, dbBundle.clGlobalVar.mandant);
//            offset++;
//            lPStm.setInt(offset, dbBundle.clGlobalVar.mandant);
//            offset++;
//            lPStm.setInt(offset, dbBundle.clGlobalVar.mandant);
//            offset++;
//            lPStm.setInt(offset, dbBundle.clGlobalVar.mandant);
//            offset++;
//            lPStm.setInt(offset, dbBundle.clGlobalVar.mandant);
//            offset++;
//            if (!ekNummer.isEmpty()) {
//                lPStm.setString(offset, ekNummer);
//                offset++;
//                lPStm.setString(offset, ekNummerNeben);
//                offset++;
//
//            } else {
//                if (erstDruck == false) {
//                    lPStm.setInt(offset, drucklauf);
//                    offset++;
//                }
//                if (!pVonZutrittsIdent.isEmpty()) {
//                    lPStm.setString(offset, pVonZutrittsIdent);
//                    offset++;
//                    lPStm.setString(offset, pBisVonZutrittsIdent);
//                    offset++;
//
//                }
//            }
//
//            ResultSet lErgebnis = lPStm.executeQuery();
//            lErgebnis.last();
//            anzInArray = lErgebnis.getRow();
//            lErgebnis.beforeFirst();
////  System.out.println("Joined anzInArray="+anzInArray);
//            ergebnisArrayMeldung = new EclMeldung[anzInArray];
//            ergebnisArrayPersonenNatJur = new EclPersonenNatJur[anzInArray];
//            ergebnisArrayPersonenNatJurVersandadresse = new EclPersonenNatJurVersandadresse[anzInArray];
//            ergebnisArrayWillenserklaerung = new EclWillenserklaerung[anzInArray];
//            ergebnisArrayWillenserklaerungZusatz = new EclWillenserklaerungZusatz[anzInArray];
//            ergebnisArrayAnrede = new EclAnrede[anzInArray];
//            ergebnisArrayAnredeVersand = new EclAnrede[anzInArray];
//            ergebnisArrayStaaten = new EclStaaten[anzInArray];
//            ergebnisArrayStaaten1 = new EclStaaten[anzInArray];
//            ergebnisArrayPersonenNatJur1 = new EclPersonenNatJur[anzInArray];
//            ergebnisArrayAktienregisterEintrag = new EclAktienregister[anzInArray];
//            ergebnisArrayLoginDaten = new EclLoginDaten[anzInArray];
//            ergebnisArrayLoginDaten1 = new EclLoginDaten[anzInArray];
//
//            int i = 0;
//            while (lErgebnis.next() == true) {
//                ergebnisArrayMeldung[i] = dbBundle.dbMeldungen.decodeErgebnis(lErgebnis);
//                ergebnisArrayPersonenNatJur[i] = dbBundle.dbPersonenNatJur.decodeErgebnis(lErgebnis);
//                ergebnisArrayPersonenNatJurVersandadresse[i] = dbBundle.dbPersonenNatJurVersandadresse
//                        .decodeErgebnis(lErgebnis);
//                ergebnisArrayWillenserklaerung[i] = dbBundle.dbWillenserklaerung.decodeErgebnis(lErgebnis);
//                ergebnisArrayWillenserklaerungZusatz[i] = dbBundle.dbWillenserklaerungZusatz.decodeErgebnis(lErgebnis);
//                ergebnisArrayAnrede[i] = dbBundle.dbAnreden.decodeErgebnis(lErgebnis);
//                ergebnisArrayAnredeVersand[i] = dbBundle.dbAnreden.decodeErgebnisVersand(lErgebnis);
//                ergebnisArrayStaaten[i] = dbBundle.dbStaaten.decodeErgebnis(lErgebnis);
//                ergebnisArrayStaaten1[i] = dbBundle.dbStaaten.decodeErgebnis1(lErgebnis);
//                ergebnisArrayPersonenNatJur1[i] = dbBundle.dbPersonenNatJur.decodeErgebnis1(lErgebnis);
//                ergebnisArrayAktienregisterEintrag[i] = dbBundle.dbAktienregister.decodeErgebnis(lErgebnis);
//
//                String aktionaersnummer = ergebnisArrayAktienregisterEintrag[i].aktionaersnummer;
//                dbBundle.dbLoginDaten.read_loginKennung(aktionaersnummer);
//                ergebnisArrayLoginDaten[i] = dbBundle.dbLoginDaten.ergebnisPosition(0);
//
//                if (ergebnisArrayPersonenNatJur1[i] != null) {
//                    String vertreterKennung = ergebnisArrayPersonenNatJur1[i].loginKennung;
//                    if (vertreterKennung != null && !vertreterKennung.isEmpty()) {
//                        dbBundle.dbLoginDaten.read_loginKennung(vertreterKennung);
//                        ergebnisArrayLoginDaten1[i] = dbBundle.dbLoginDaten.ergebnisPosition(0);
//                    }
//                }
//                i++;
//            }
//            lErgebnis.close();
//            lPStm.close();
//
//        } catch (SQLException e) {
//            CaBug.drucke("DbJoined.read_eintrittskartenDruck 003");
//            System.err.println(" " + e.getMessage());
//            return (-1);
//        }
//        return (anzInArray);
//        
//        
        
        /*Ab hier abgespeckt*/

        int anzInArray = 0;
        PreparedStatement lPStm = null;
        String lSql = "";

        if (ekNummer.isEmpty()) {
            lSql =
                    // "SELECT m.*, p.*, p1.*, st.*, pv.*, st1.*, wz.*, w.*, an.*, anf.* FROM
                    // (((tbl_PersonenNatJur p "
                    // +"INNER JOIN (tbl_meldungen m INNER JOIN (tbl_willenserklaerungzusatz wz
                    // INNER JOIN tbl_willenserklaerung w ON
                    // wz.willenserklaerungIdent=w.willenserklaerungIdent) "
                    // +"ON m.meldungsIdent=wz.meldungsIdent) ON m.personenNatJurIdent=p.ident) "
                    // + "LEFT OUTER JOIN tbl_personenNatJurVersandadresse pv ON
                    // (p.identVersandadresse=pv.ident AND p.mandant=pv.mandant)) LEFT OUTER JOIN
                    // tbl_anreden an ON p.anrede=an.anredennr) "
                    // + "LEFT OUTER JOIN tbl_anredenfremd anf ON an.anredennr=anf.anredennr "
                    // + "LEFT OUTER JOIN tbl_staaten st on p.land=st.code "
                    // + "LEFT OUTER JOIN tbl_staaten st1 on pv.staatIdVersand=st1.id "
                    // + "LEFT OUTER JOIN tbl_personenNatJur p1 on
                    // (wz.identVertreterPersonNatJur=p1.ident AND wz.mandant=p1.mandant) "
                    // +"WHERE m.mandant=? AND p.mandant=? AND m.meldungAktiv=1 AND wz.mandant=? AND
                    // w.mandant=? AND wz.willenserklaerung=100 AND wz.anmeldungIstStorniert!=1 AND
                    // (wz.versandartEK=1 OR wz.versandartEK=2) AND anf.sprachennr=2 ";
                    
    /*Gekürzt:
     * pv
     * anv
     * anfv
     * st1
     * 
     */
                    "SELECT m.*, p.*, p1.*, st.*, are.*, wz.*, w.*, an.*, anf.* FROM ((("
                            + dbBundle.getSchemaMandant() + "tbl_PersonenNatJur p " + "INNER JOIN ("
                            + dbBundle.getSchemaMandant() + "tbl_meldungen m " + "INNER JOIN ("
                            + dbBundle.getSchemaMandant() + "tbl_willenserklaerungzusatz wz " + "INNER JOIN "
                            + dbBundle.getSchemaMandant()
                            + "tbl_willenserklaerung w ON wz.willenserklaerungIdent=w.willenserklaerungIdent) ";
            if (pGastOderAktionaer == 2) {
                lSql = lSql + "ON m.meldungsIdent=wz.meldungsIdent)";
            } else {
                lSql = lSql + "ON m.meldungsIdent=wz.meldungsIdentGast)";
            }
            lSql = lSql + " ON m.personenNatJurIdent=p.ident)) " 
                    + "LEFT OUTER JOIN "
                    + dbBundle.getSchemaAllgemein() + "tbl_anreden an ON p.anrede=an.anredennr) " + "LEFT OUTER JOIN "
                    + dbBundle.getSchemaMandant() + "tbl_aktienregister are ON m.aktionaersnummer=are.aktionaersnummer "
                    + "LEFT OUTER JOIN " + dbBundle.getSchemaAllgemein()
                    + "tbl_anredenfremd anf ON an.anredennr=anf.anredennr " 
                    + "LEFT OUTER JOIN "
                    + dbBundle.getSchemaAllgemein() + "tbl_staaten st on p.land=st.code " 
                    + "LEFT OUTER JOIN " + dbBundle.getSchemaMandant()
                    + "tbl_personenNatJur p1 on (wz.identVertreterPersonNatJur=p1.ident AND wz.mandant=p1.mandant) "
                    + "WHERE m.mandant=? AND p.mandant=? AND m.meldungAktiv=1 AND wz.mandant=? AND w.mandant=? AND "
                    + "(are.mandant=? OR isnull(are.mandant)) AND " /* Geändert! */
                    + "wz.willenserklaerung=100 AND wz.anmeldungIstStorniert!=1 AND "
                    + "(wz.versandartEK="+Integer.toString(KonstWillenserklaerungVersandartEK.VERSANDLAUF_ADRESSE_LAUT_AKTIENREGISTER) //1
                    + " OR wz.versandartEK="+Integer.toString(KonstWillenserklaerungVersandartEK.VERSANDLAUF_ADRESSE_ABWEICHEND_WIE_EINGEGEBEN) //2
                    ;
            if (pEkVersandFuerAlleImPortalAngeforderten==1) {
                lSql = lSql
                        + " OR wz.versandartEK="+Integer.toString(KonstWillenserklaerungVersandartEK.ONLINE_AUSDRUCK) //3
                        + " OR wz.versandartEK="+Integer.toString(KonstWillenserklaerungVersandartEK.ONLINE_EMAIL) //4
                        + " OR wz.versandartEK="+Integer.toString(KonstWillenserklaerungVersandartEK.IN_APP) //5
                        ;
            }
            lSql = lSql
                    + ") AND " //pEkVersandFuerAlleImPortalAngeforderten
                    + "(anf.sprachennr=2 OR isnull(anf.sprachennr)) "; /*
                                                                                                                           * Geändert
                                                                                                                           * -
                                                                                                                           * auch
                                                                                                                           * ||
                                                                                                                           * in
                                                                                                                           * OR
                                                                                                                           * in
                                                                                                                           * der
                                                                                                                           * zweiten
                                                                                                                           * Bedingung
                                                                                                                           */
            if (erstDruck) {
                lSql = lSql + "AND wz.eintrittskarteWurdeGedruckt=0 ";
            } else {
                lSql = lSql + "AND wz.eintrittskarteWurdeGedruckt=? ";
            }
            if (nurGepruefte) {
                lSql = lSql + " AND (wz.versandadresseUeberprueft!=0 OR wz.versandartEK!=2) ";
            }

            if (!pVonZutrittsIdent.isEmpty()) {
                lSql = lSql + " AND m.zutrittsIdent>=? AND m.zutrittsIdent<=?";
            }

            lSql = lSql + " ORDER BY w.benutzernr, w.willenserklaerungIdent;";
        } else {/* Nur einzelne bestimmte EK drucken */
            
            /*Gekürzt:
             * pv
             * anv
             * anfv
             * st1
             * 
             */

            lSql = "SELECT m.*, p.*, p1.*, st.*, are.*, wz.*, w.*, an.*, anf.* FROM ((("
                    + dbBundle.getSchemaMandant() + "tbl_PersonenNatJur p " + "INNER JOIN ("
                    + dbBundle.getSchemaMandant() + "tbl_meldungen m INNER JOIN (" + dbBundle.getSchemaMandant()
                    + "tbl_willenserklaerungzusatz wz INNER JOIN " + dbBundle.getSchemaMandant()
                    + "tbl_willenserklaerung w ON wz.willenserklaerungIdent=w.willenserklaerungIdent) ";
            if (pGastOderAktionaer == 2) {
                lSql = lSql + "ON m.meldungsIdent=wz.meldungsIdent)";
            } else {
                lSql = lSql + "ON m.meldungsIdent=wz.meldungsIdentGast)";
            }
            lSql = lSql + " ON m.personenNatJurIdent=p.ident)) "
                    + "LEFT OUTER JOIN "
                    + dbBundle.getSchemaAllgemein() + "tbl_anreden an ON p.anrede=an.anredennr) " + "LEFT OUTER JOIN "
                    + dbBundle.getSchemaMandant() + "tbl_aktienregister are ON m.aktionaersnummer=are.aktionaersnummer "
                    + "LEFT OUTER JOIN " + dbBundle.getSchemaAllgemein()
                    + "tbl_anredenfremd anf ON an.anredennr=anf.anredennr " 
                     + "LEFT OUTER JOIN "
                    + dbBundle.getSchemaAllgemein() + "tbl_staaten st on p.land=st.code " 
                    + "LEFT OUTER JOIN " + dbBundle.getSchemaMandant()
                    + "tbl_personenNatJur p1 on (wz.identVertreterPersonNatJur=p1.ident AND wz.mandant=p1.mandant) "
                    + "WHERE m.mandant=? AND p.mandant=? AND m.meldungAktiv=1 AND wz.mandant=? AND w.mandant=? AND "
                    + "(are.mandant=?  OR isnull(are.mandant)) AND " /* Geändert */
                    + "wz.willenserklaerung=100 AND wz.anmeldungIstStorniert!=1 AND w.zutrittsIdent=? AND w.zutrittsIdentNeben=? AND "
                    + "(anf.sprachennr=2 OR isnull(anf.sprachennr))  ";/*
                                                                                                                           * Geändert
                                                                                                                           * -
                                                                                                                           * auch
                                                                                                                           * ||
                                                                                                                           * in
                                                                                                                           * OR
                                                                                                                           * in
                                                                                                                           * der
                                                                                                                           * zweiten
                                                                                                                           * Bedingung
                                                                                                                           */

        }

        System.out.println(lSql);
        try {
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            int offset = 1;
            lPStm.setInt(offset, dbBundle.clGlobalVar.mandant);
            offset++;
            lPStm.setInt(offset, dbBundle.clGlobalVar.mandant);
            offset++;
            lPStm.setInt(offset, dbBundle.clGlobalVar.mandant);
            offset++;
            lPStm.setInt(offset, dbBundle.clGlobalVar.mandant);
            offset++;
            lPStm.setInt(offset, dbBundle.clGlobalVar.mandant);
            offset++;
            if (!ekNummer.isEmpty()) {
                lPStm.setString(offset, ekNummer);
                offset++;
                lPStm.setString(offset, ekNummerNeben);
                offset++;

            } else {
                if (erstDruck == false) {
                    lPStm.setInt(offset, drucklauf);
                    offset++;
                }
                if (!pVonZutrittsIdent.isEmpty()) {
                    lPStm.setString(offset, pVonZutrittsIdent);
                    offset++;
                    lPStm.setString(offset, pBisVonZutrittsIdent);
                    offset++;

                }
            }

            ResultSet lErgebnis = lPStm.executeQuery();
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();
//  System.out.println("Joined anzInArray="+anzInArray);
            ergebnisArrayMeldung = new EclMeldung[anzInArray];
            ergebnisArrayPersonenNatJur = new EclPersonenNatJur[anzInArray];
//            ergebnisArrayPersonenNatJurVersandadresse = new EclPersonenNatJurVersandadresse[anzInArray];
            ergebnisArrayWillenserklaerung = new EclWillenserklaerung[anzInArray];
            ergebnisArrayWillenserklaerungZusatz = new EclWillenserklaerungZusatz[anzInArray];
            ergebnisArrayAnrede = new EclAnrede[anzInArray];
//            ergebnisArrayAnredeVersand = new EclAnrede[anzInArray];
            ergebnisArrayStaaten = new EclStaaten[anzInArray];
//            ergebnisArrayStaaten1 = new EclStaaten[anzInArray];
            ergebnisArrayPersonenNatJur1 = new EclPersonenNatJur[anzInArray];
            ergebnisArrayAktienregisterEintrag = new EclAktienregister[anzInArray];
            ergebnisArrayLoginDaten = new EclLoginDaten[anzInArray];
            ergebnisArrayLoginDaten1 = new EclLoginDaten[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {
                ergebnisArrayMeldung[i] = dbBundle.dbMeldungen.decodeErgebnis(lErgebnis);
                ergebnisArrayPersonenNatJur[i] = dbBundle.dbPersonenNatJur.decodeErgebnis(lErgebnis);
//                ergebnisArrayPersonenNatJurVersandadresse[i] = dbBundle.dbPersonenNatJurVersandadresse
//                        .decodeErgebnis(lErgebnis);
                ergebnisArrayWillenserklaerung[i] = dbBundle.dbWillenserklaerung.decodeErgebnis(lErgebnis);
                ergebnisArrayWillenserklaerungZusatz[i] = dbBundle.dbWillenserklaerungZusatz.decodeErgebnis(lErgebnis);
                ergebnisArrayAnrede[i] = dbBundle.dbAnreden.decodeErgebnis(lErgebnis);
//                ergebnisArrayAnredeVersand[i] = dbBundle.dbAnreden.decodeErgebnisVersand(lErgebnis);
                ergebnisArrayStaaten[i] = dbBundle.dbStaaten.decodeErgebnis(lErgebnis);
//                ergebnisArrayStaaten1[i] = dbBundle.dbStaaten.decodeErgebnis1(lErgebnis);
                ergebnisArrayPersonenNatJur1[i] = dbBundle.dbPersonenNatJur.decodeErgebnis1(lErgebnis);
                ergebnisArrayAktienregisterEintrag[i] = dbBundle.dbAktienregister.decodeErgebnis(lErgebnis);

                String aktionaersnummer = ergebnisArrayAktienregisterEintrag[i].aktionaersnummer;
                dbBundle.dbLoginDaten.read_loginKennung(aktionaersnummer);
                ergebnisArrayLoginDaten[i] = dbBundle.dbLoginDaten.ergebnisPosition(0);

                if (ergebnisArrayPersonenNatJur1[i] != null) {
                    String vertreterKennung = ergebnisArrayPersonenNatJur1[i].loginKennung;
                    if (vertreterKennung != null && !vertreterKennung.isEmpty()) {
                        dbBundle.dbLoginDaten.read_loginKennung(vertreterKennung);
                        ergebnisArrayLoginDaten1[i] = dbBundle.dbLoginDaten.ergebnisPosition(0);
                    }
                }
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

    public int read_eintrittskartenDruckNurWillenserklaerungen(boolean erstDruck, int drucklauf, boolean nurGepruefte,
            String ekNummer, String ekNummerNeben, int pGastOderAktionaer, String pVonZutrittsIdent,
            String pBisVonZutrittsIdent) {

        int anzInArray = 0;
        PreparedStatement lPStm = null;
        String lSql = "";

        lSql = "SELECT wz.*, w.* FROM " + dbBundle.getSchemaMandant() + "tbl_willenserklaerungzusatz wz INNER JOIN "
                + dbBundle.getSchemaMandant() + "tbl_willenserklaerung w ON "
                + "wz.willenserklaerungIdent=w.willenserklaerungIdent " + "WHERE "
                + "wz.willenserklaerung=100 AND wz.anmeldungIstStorniert!=1 AND (wz.versandartEK=1 OR wz.versandartEK=2) ";
        if (erstDruck) {
            lSql = lSql + "AND wz.eintrittskarteWurdeGedruckt=0 ";
        } else {
            lSql = lSql + "AND wz.eintrittskarteWurdeGedruckt=? ";
        }

        if (nurGepruefte) {
            lSql = lSql + " AND (wz.versandadresseUeberprueft!=0 OR wz.versandartEK!=2) ";
        }

        if (pGastOderAktionaer == 1) {
            lSql = lSql + " AND w.meldungsIdentGast!=0 ";
        } else {
            lSql = lSql + " AND w.meldungsIdent!=0 ";
        }

        if (ekNummer.isEmpty()) {
            if (!pVonZutrittsIdent.isEmpty()) {
                lSql = lSql + " AND w.zutrittsIdent>=? AND w.zutrittsIdent<=? ";
            }
        } else {
            lSql = lSql + " AND w.zutrittsIdent=? AND w.zutrittsIdentNeben=? ";
        }
        lSql = lSql + " ORDER BY w.benutzernr, w.willenserklaerungIdent;";

        try {
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            int offset = 1;

            if (erstDruck == false) {
                lPStm.setInt(offset, drucklauf);
                offset++;
            }

            if (ekNummer.isEmpty()) {
                if (!pVonZutrittsIdent.isEmpty()) {
                    lPStm.setString(offset, pVonZutrittsIdent);
                    offset++;
                    lPStm.setString(offset, pBisVonZutrittsIdent);
                    offset++;
                }
            } else {
                lPStm.setString(offset, ekNummer);
                offset++;
                lPStm.setString(offset, ekNummerNeben);
                offset++;
            }

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
            CaBug.drucke("003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    /**
     * Einlesen aller Willenserklärungen einer bestimmten Art zu einer Meldung
     * 
     * @param pMeldeIdent           Meldung, zu der die Willenserklärungen
     *                              eingelesen werden sollen
     * @param pWillenserklaerungArt Art der Willenserklärungen, die geliefert werden
     *                              sollen
     * @param selektion             1=nur gültige 2=nur stornierte 3=alle
     * @return
     */
    public int read_willenserklaerungArtZuAktionaer(int pMeldeIdent,
            /* EnWillenserklaerung */ int pWillenserklaerungArt, int selektion) {
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
            lPStm.setInt(3, /* EnWillenserklaerung.toClWillenserklaerung( */pWillenserklaerungArt/* ) */);
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
     * 
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

    /**
     * Einlesen aller Aktionärsnummern, für die Willenserklärungen ab einer
     * bestimmten Veränderungszeit vorliegen
     */
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

    /**************************
     * "Schnelle" Funktionen mit wenige Ergebnisinformation - prinzipiell für
     * Statistiken************** aber auch als "Oberselektion" für Reports
     */

    /**
     * Einlesen der Stimmenzahl aller Meldungen
     * 
     * @return
     */
    public int read_angemeldeteStimmen() {
        int anzInArray = 0;
        PreparedStatement lPStm = null;

        String lSql = "SELECT  m.stimmen, m.stueckaktien, m.besitzart FROM " + dbBundle.getSchemaMandant()
                + "tbl_meldungen m " + "WHERE m.mandant=? AND " + "m.meldungaktiv=1 AND " + "m.klasse=1 AND "
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
                ergebnisArrayMeldung[i].stueckAktien = lErgebnis.getLong("m.stueckAktien");
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
     * 
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
     * 
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
     * 
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
     * 
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
     * Einlesen der Kennzeichen eMailRegistrierung für alle Aktionäre, die im Portal
     * die Registrierung überhaupt bestätigt haben
     * 
     * @return
     */
    public int read_aktionaerePortalHinweiseBestaetigt() {

        int anzInArray = 0;
        PreparedStatement lPStm = null;

        String lSql = "SELECT eVersandRegistrierung, emailBestaetigt, hinweisAktionaersPortalBestaetigt, hinweisHVPortalBestaetigt FROM "
                + dbBundle.getSchemaMandant() + "tbl_loginDaten where "
                        + "(hinweisAktionaersPortalBestaetigt!=0 OR "
                        + "hinweisHVPortalBestaetigt!=0 OR "
                        + "eVersandRegistrierung!=0 OR "
                        + "emailBestaetigt!=0) "
                        + ";";

        try {
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            ResultSet lErgebnis = lPStm.executeQuery();
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();
            ergebnisArrayLoginDaten = new EclLoginDaten[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {
                ergebnisArrayLoginDaten[i] = new EclLoginDaten();
                ergebnisArrayLoginDaten[i].eVersandRegistrierung = lErgebnis.getInt("eVersandRegistrierung");
                ergebnisArrayLoginDaten[i].emailBestaetigt = lErgebnis.getInt("emailBestaetigt");
                ergebnisArrayLoginDaten[i].hinweisAktionaersPortalBestaetigt = lErgebnis
                        .getInt("hinweisAktionaersPortalBestaetigt");
                ergebnisArrayLoginDaten[i].hinweisHVPortalBestaetigt = lErgebnis.getInt("hinweisHVPortalBestaetigt");
                i++;
            }
            lErgebnis.close();
            lPStm.close();

        } catch (SQLException e) {
            CaBug.drucke("003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    /**
     * Negativliste - Alle Aktionäre, die nicht angemeldet sind
     * 
     * aktienwert: alle Aktionäre mit mindestens diesem Aktienwert werden im
     * Ergebnis aufgeführt sortierung: =1 => nach Aktonärsnummer =2 => nach Name =3
     * => absteigend nach Aktienzahl
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
     * selektion =1 alle, bei denen angemeldete Aktien < Aktien im Aktienregister
     * sind =2 alle, bei denen angemeldete Aktien > Aktien im Aktienregister sind
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
                + dbBundle.getSchemaMandant() + "tbl_aktienregister are " + "WHERE are.mandant=? AND are.stimmen>0";

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
     * Basis für Aktienregisterimport: alle Aktionäre, die auf 0 gesetzt werden
     * müssen
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
     * selektion =1 => alle =2 => alle, die nicht angemeldet sind (zur HV)
     *
     * Ergebnis: aktienregisterIdent in ergebnisArrayKey
     * 
     * @return
     */
    public int read_emailVersandRegistrierung(int selektion) {

        int anzInArray = 0;
        PreparedStatement lPStm = null;

        String lSql = "SELECT lo.aktienregisterIdent FROM " + dbBundle.getSchemaMandant() + "tbl_loginDaten lo "
                + "LEFT OUTER JOIN " + dbBundle.getSchemaMandant()
                + "tbl_willenserklaerungzusatz wz ON (lo.aktienregisterident=wz.aktienregisterident) "
                + "WHERE lo.eVersandRegistrierung=99 and lo.emailBestaetigt=1 ";

        if (selektion == 2) {
            lSql = lSql + " AND wz.aktienregisterident IS Null ";
        }
        lSql = lSql + "GROUP BY lo.aktienregisterIdent ORDER BY lo.aktienregisterIdent ";

        try {
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            ResultSet lErgebnis = lPStm.executeQuery();
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();
            ergebnisArrayKey = new String[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {
                ergebnisArrayKey[i] = Integer.toString(lErgebnis.getInt("lo.aktienregisterIdent"));
                i++;
            }
            lErgebnis.close();
            lPStm.close();

        } catch (SQLException e) {
            CaBug.drucke("003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    /*
     * TODO #Konsolidierung: Klären - wie wird denn Versandnummer in den Folgejahren
     * gesetzt? Und: evtl. Verinheitlichung mit EmailVersand-Liste?
     */
    /**
     * 
     * @param selektion     1 = Post // 2 = Email
     * @param versandNummer Jeweilige Versandnummer
     * @return Sortierte Versandliste nach Aktionärsnummer
     */
    public int read_aktienregisterVersandliste(int selektion, int versandNummer) {

        int anzInArray = 0;
        PreparedStatement lPStm = null;
        
        String lSql = "SELECT lo.*, are.* FROM " + dbBundle.getSchemaMandant() + "tbl_loginDaten lo "
                + "LEFT JOIN " + dbBundle.getSchemaMandant() + "tbl_aktienregister are ON (lo.aktienregisterident = are.aktienregisterident) "
                + "WHERE are.mandant = ? AND are.versandNummer = ? AND are.stueckAktien > 0";
        switch (selektion) {
        case 1: {
            if (dbBundle.param.paramPortal.emailVersandZweitEMailAusRegister == 1) {
                lSql += " AND (lo.eVersandRegistrierung != 99 OR are.email = 'PV'"
                        + " OR (lo.eVersandRegistrierung = 99 AND (lo.emailBestaetigt = 0 AND lo.email2Bestaetigt != 2)))";
            } else {
                lSql += " AND (lo.eVersandRegistrierung != 99 OR lo.emailBestaetigt != 1)";
            }
            break;
        }
        case 2: {
            if (dbBundle.param.paramPortal.emailVersandZweitEMailAusRegister == 1) {
                lSql += " AND lo.eVersandRegistrierung = 99 AND are.email != 'PV'"
                        + " AND (lo.emailBestaetigt = 1 OR lo.email2Bestaetigt = 2)";
            } else {
                lSql += " AND lo.eVersandRegistrierung = 99 AND lo.emailBestaetigt = 1";
            }
            break;
        }
        }
        lSql += " ORDER BY are.aktionaersnummer ";

        try {
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, dbBundle.clGlobalVar.mandant);
            lPStm.setInt(2, versandNummer);

            ResultSet lErgebnis = lPStm.executeQuery();
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();
            ergebnisArrayAktienregisterEintrag = new EclAktienregister[anzInArray];
            ergebnisArrayLoginDaten = new EclLoginDaten[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {
                ergebnisArrayAktienregisterEintrag[i] = dbBundle.dbAktienregister.decodeErgebnis(lErgebnis);
                ergebnisArrayLoginDaten[i] = dbBundle.dbLoginDaten.decodeErgebnis(lErgebnis);
                i++;
            }
            lErgebnis.close();
            lPStm.close();

        } catch (SQLException e) {
            CaBug.drucke("003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }
    
    public ArrayList<EclMailingVariablen> read_MailingVersandliste(String where, int versandnummer) {

        ArrayList<EclMailingVariablen> list = new ArrayList<>();

        String sql = "SELECT lo.aktienregisterident, lo.eMailFuerVersand, lo.eMail2FuerVersand, lo.loginKennung, lo.passwortInitial, "
                + "(SELECT anredenbrief FROM db_meetingcomfort.tbl_anreden WHERE anredennr = are.anredeId), "
                + "are.nameKomplett, are.titel, are.vorname, are.nachname "
                + "FROM " + dbBundle.getSchemaMandant() + "tbl_loginDaten lo " + "LEFT JOIN "
                + dbBundle.getSchemaMandant()
                + "tbl_aktienregister are ON lo.aktienregisterident = are.aktienregisterident "
                + "WHERE are.mandant = ? AND are.versandNummer = ? AND are.stueckAktien > 0 ";

        sql += where;
        sql += "ORDER BY are.aktionaersnummer ";

        try (PreparedStatement pstmt = verbindung.prepareStatement(sql)) {

            pstmt.setInt(1, dbBundle.clGlobalVar.mandant);
            pstmt.setInt(2, versandnummer);

            try (ResultSet rs = pstmt.executeQuery()) {

                while (rs.next()) {
                    if (!rs.getString(1).isBlank()) {
                        EclMailingVariablen eintrag = new EclMailingVariablen();
                        eintrag.setAktienregisterIdent(rs.getInt(1));
                        eintrag.setEmail(rs.getString(2));
                        eintrag.setEmail2(rs.getString(3));
                        eintrag.setKennung(rs.getString(4));
                        // TODO ggf. anpasssen Passwortlaenge ist aktuell an vielen Stellen hartkodiert
                        eintrag.setPasswort(rs.getString(5).isBlank() ? "" : rs.getString(5).substring(8, 8 * 2));
                        eintrag.setAnrede((rs.getString(6) + " " + rs.getString(10)).trim() + ",");
                        eintrag.setNameKomplett(rs.getString(7));
                        eintrag.setTitel(rs.getString(8));
                        eintrag.setVorname(rs.getString(9));
                        eintrag.setNachname(rs.getString(10));
                        list.add(eintrag);
                    }
                }
            }
        } catch (Exception e) {
            CaBug.drucke("Versandliste E-Mailversand 001");
            System.err.println(" " + e.getMessage());
        }
        return list;
    }

    /***********************************
     * für Reports
     *********************************************************/
    private ResultSet lErgebnis_meldeliste = null;
    PreparedStatement lPStm_meldeliste = null;

    /**
     * 
     * @param sortierung 1=meldeIdent 2=Name 3=Aktien absteigend 4=Zutrittsident,
     *                   name
     * @param selektion  1=nur EinzelMeldungen 2=nur Sammelkarten 3=alle
     * @param klasse     0=Gäste 1=Aktionäre
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
            case 4: {
                lSql = lSql + " ORDER BY m.zutrittsIdent, p.name ";
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
     * @return true=Satz gelesen false=ende erreicht, kein Satz mehr gelesen
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

    /******
     * Liste aller Aktionäre, die Einzelweisung enthalten haben (und als solche in
     * einer Sammelkarte sind) sammelIdent=0 => egal in welcher Sammelkarte
     * sammelIdent!=0 => nur die, die dieser Sammelkarte zugeordnet sind
     * 
     * mitVerschlossenen=1 => auch "nicht weiterzugebende" werden ausgedruckt
     * 
     * 
     * ergebnisArrayMeldung = Aktionärsmeldung ergebnisArrayMeldung1 = zugehörige
     * Sammelkarte ergebnisArrayWeisungMeldung = Detailweisungen
     */
    public int readinit_aktionaereMitWeisung(int sammelIdent) {

        int anzInArray = 0;

        /*
         * Lesen aller Sammelklarten, die Zusatz1=="" haben (sonst keine Offenlegung)
         * und skWeisungsartZulaessig and 4 == 4 Dazu alle Meldungen, die aktiv sind in
         * dieser Sammelkarte - (inner join) Dazu alle Weisungen mit entsprechender
         * MeldungsIdent
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
     * @return true=Satz gelesen false=ende erreicht, kein Satz mehr gelesen
     */
    public boolean readnext_aktionaereMitWeisung() {
        try {
            if (lErgebnis_meldeliste.next() == true) {

                ergebnisArrayMeldung[0] = new EclMeldung(); /* Aktionär */
                ergebnisArrayMeldung[0].meldungsIdent = lErgebnis_meldeliste.getInt("m.meldungsIdent");
                ergebnisArrayMeldung[0].aktionaersnummer = lErgebnis_meldeliste.getString("m.aktionaersnummer");
                ergebnisArrayMeldung[0].besitzart = lErgebnis_meldeliste.getString("m.besitzart");
                ergebnisArrayMeldung[0].stimmen = lErgebnis_meldeliste.getLong("m.stimmen");
                ergebnisArrayMeldung[0].zutrittsIdent = lErgebnis_meldeliste.getString("m.zutrittsIdent");
                ergebnisArrayMeldung[0].name = lErgebnis_meldeliste.getString("p.name");
                ergebnisArrayMeldung[0].vorname = lErgebnis_meldeliste.getString("p.vorname");
                ergebnisArrayMeldung[0].ort = lErgebnis_meldeliste.getString("p.ort");

                ergebnisArrayMeldung1[0] = new EclMeldung(); /* Sammelkarte */
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

    /******
     * Kontrolliste für Weisungserfassung - Alle unkontrollierten auf neue
     * Lauf-Nummer setzen (d.h. update von istKontrolliert auf >0 für alle passenden
     * Sätze ==0) Return-Wert = Anzahl der gefundenen Sätze
     */
    public int setzeLauf_kontrollisteWeisung(int pLaufNummer) {
        CaBug.druckeLog("DbJoined.setzeLauf_kontrollisteWeisung pLaufNummer=" + pLaufNummer, logDruckenInt, 10);
        int anzUpdate = 0;

        try {

            String lSql = "UPDATE " + dbBundle.getSchemaMandant() + "tbl_willenserklaerung w "
                    + "SET w.istKontrolliert=? " + "WHERE w.mandant=? "
                    + "AND (w.erteiltAufWeg<20 OR w.erteiltAufWeg=51) "
                    + "AND (w.willenserklaerung>=350 AND w.willenserklaerung <=367) " + "AND w.istKontrolliert=0 ";

            PreparedStatement pstm1 = verbindung.prepareStatement(lSql);
            pstm1.setInt(1, pLaufNummer);
            pstm1.setInt(2, dbBundle.clGlobalVar.mandant);

            anzUpdate = pstm1.executeUpdate();
            pstm1.close();

        } catch (Exception e) {
            CaBug.drucke("DbJoined.belege_kontrollisteWeisung 003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzUpdate);
    }

    /******
     * Kontrolliste für Weisungserfassung
     * 
     * ergebnisArrayMeldung; ergebnisArrayWillenserklaerung;
     * ergebnisArrayWeisungMeldung;
     */
    public int read_kontrollisteWeisung(int pDrucklaufNr) {

        int anzInArray = 0;

        PreparedStatement lPStm = null;

        try {

            String lSql = "SELECT w.*, m.*, p.*, wm.* " + "FROM " + dbBundle.getSchemaMandant()
                    + "tbl_willenserklaerung w " + "INNER JOIN " + dbBundle.getSchemaMandant()
                    + "tbl_meldungen m ON w.meldungsIdent=m.meldungsIdent " + "INNER JOIN "
                    + dbBundle.getSchemaMandant() + "tbl_personenNatJur p ON m.personenNatJurIdent=p.ident "
                    + "INNER JOIN " + dbBundle.getSchemaMandant()
                    + "tbl_weisungMeldung wm on wm.willenserklaerungIdent=w.willenserklaerungIdent "
                    + "WHERE w.mandant=? AND m.mandant=? AND p.mandant=? AND wm.mandant=? " + "AND w.istKontrolliert=? "
                    + "ORDER BY w.willenserklaerungIdent ";

            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, dbBundle.clGlobalVar.mandant);
            lPStm.setInt(2, dbBundle.clGlobalVar.mandant);
            lPStm.setInt(3, dbBundle.clGlobalVar.mandant);
            lPStm.setInt(4, dbBundle.clGlobalVar.mandant);
            lPStm.setInt(5, pDrucklaufNr);

            ResultSet lErgebnis = lPStm.executeQuery();
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();

            ergebnisArrayMeldung = new EclMeldung[anzInArray];
            ergebnisArrayWillenserklaerung = new EclWillenserklaerung[anzInArray];
            ergebnisArrayWeisungMeldung = new EclWeisungMeldung[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {
                ergebnisArrayMeldung[i] = dbBundle.dbMeldungen.decodeErgebnis(lErgebnis); /* Aktionär */
                ergebnisArrayWeisungMeldung[i] = dbBundle.dbWeisungMeldung.decodeErgebnis(lErgebnis);
                ergebnisArrayWillenserklaerung[i] = dbBundle.dbWillenserklaerung.decodeErgebnis(lErgebnis);
                i++;
            }

            lErgebnis.close();
            lPStm.close();

        } catch (Exception e) {
            CaBug.drucke("DbJoined.read_kontrollisteWeisung 003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    /******
     * Kontrolliste für Weisungserfassung sammelIdent=0 => egal in welcher
     * Sammelkarte sammelIdent!=0 => nur die, die dieser Sammelkarte zugeordnet sind
     * 
     * mitVerschlossenen=1 => auch "nicht weiterzugebende" werden ausgedruckt
     * 
     * 
     * ergebnisArrayMeldung=new EclMeldung[1]; ergebnisArrayWillenserklaerung=new
     * EclWillenserklaerung[1]; ergebnisArrayWeisungMeldung=new
     * EclWeisungMeldung[1];
     */
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
                    + "AND wm.aktiv=1 " + "ORDER BY m.aktionaersnummer ";
            // + "ORDER BY w.benutzernr, w.willenserklaerung, w.willenserklaerungIdent ";

            // System.out.println(lSql);

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
     * @return true=Satz gelesen false=ende erreicht, kein Satz mehr gelesen
     */
    public boolean readnext_kontrollisteWeisung() {
        try {
            if (lErgebnis_meldeliste.next() == true) {

                ergebnisArrayMeldung[0] = dbBundle.dbMeldungen.decodeErgebnis(lErgebnis_meldeliste); /* Aktionär */
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

    /********************************
     * Funktionen für Präsenzliste
     ***********************************************/
    /**
     * Feststellen der Präsenz: "fixieren" der Willenserklärungen (d.h. Füllen der
     * Variable "zuVerzeichnisNrXGedruckt").
     * 
     * @param listenNummer=alle Willenserklärungen, die kleiner oder gleich dieser
     *                          Präsenzlistennummer sind, werden fixiert, soweit sie
     *                          nicht schon fixiert sind. Diese wird auch als
     *                          "gedruckt" eingetragen
     * @param verzeichnis       = 1 bis 4
     * @return
     */
    public int feststellenPraesenz(int listenNummer, int verzeichnis) {
        CaBug.druckeLog("listenNummer=" + listenNummer + " verzeichnis=" + verzeichnis, logDruckenInt, 10);
        try {

            //@formatter:off
            String sql = "UPDATE " + dbBundle.getSchemaMandant() + "tbl_willenserklaerung SET " 
                    + "zuVerzeichnisNr" + Integer.toString(verzeichnis) + "Gedruckt=" + Integer.toString(listenNummer) + ", "
                    + "db_version=db_version+1 " + 
                    "WHERE " + "mandant=? AND " + "zuVerzeichnisNr"
                    + Integer.toString(verzeichnis) + "<=" + Integer.toString(listenNummer) + " AND "
                    + "zuVerzeichnisNr" + Integer.toString(verzeichnis) + "Gedruckt=0 AND " + "mc_delayed!=1 AND " + "("
                    + "willenserklaerung=" + KonstWillenserklaerung.erstzugang + " OR " 
                    + "willenserklaerung=" + KonstWillenserklaerung.abgang + " OR " 
                    + "willenserklaerung=" + KonstWillenserklaerung.wiederzugang + " OR " 
                    + "willenserklaerung=" + KonstWillenserklaerung.vertreterwechsel + " OR " 
                    + "willenserklaerung=" + KonstWillenserklaerung.abgangAusSRV + " OR " 
                    + "willenserklaerung=" + KonstWillenserklaerung.abgangAusOrga + " OR " 
                    + "willenserklaerung=" + KonstWillenserklaerung.abgangAusDauervollmacht + " OR " 
                    + "willenserklaerung=" + KonstWillenserklaerung.abgangAusKIAV + " OR " 
                    + "willenserklaerung=" + KonstWillenserklaerung.zugangInSRV + " OR " 
                    + "willenserklaerung=" + KonstWillenserklaerung.zugangInOrga + " OR " 
                    + "willenserklaerung=" + KonstWillenserklaerung.zugangInDauervollmacht + " OR " 
                    + "willenserklaerung=" + KonstWillenserklaerung.zugangInKIAV + " OR " 
                    + "willenserklaerung=" + KonstWillenserklaerung.wechselInSRV + " OR " 
                    + "willenserklaerung=" + KonstWillenserklaerung.wechselInOrga + " OR " 
                    + "willenserklaerung=" + KonstWillenserklaerung.wechselInDauervollmacht + " OR " 
                    + "willenserklaerung=" + KonstWillenserklaerung.wechselInKIAV +  " OR " 
                    + "willenserklaerung=" + KonstWillenserklaerung.virtZugangSelbst +  " OR " 
                    + "willenserklaerung=" + KonstWillenserklaerung.virtWiederzugangSelbst +  " OR " 
                    + "willenserklaerung=" + KonstWillenserklaerung.virtAbgang +  " OR " 
                    + "willenserklaerung=" + KonstWillenserklaerung.virtZugangVollmacht +  " OR " 
                    + "willenserklaerung=" + KonstWillenserklaerung.virtWiederzugangVollmacht +  " OR " 
                    + "willenserklaerung=" + KonstWillenserklaerung.virtAbgangVollmacht + 
                    ")";

            //@formatter: on
            CaBug.druckeLog(sql, logDruckenInt, 10);

            PreparedStatement pstm1 = verbindung.prepareStatement(sql);
            pstm1.setInt(1, dbBundle.clGlobalVar.mandant);

            int ergebnis1 = pstm1.executeUpdate();
            CaBug.druckeLog("ergebnis1=" + ergebnis1, logDruckenInt, 10);
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

    /**
     * verzeichnis -1 => einlesen unabhängig alle von Listennummer, Präsenznummer usw.. pListeFuerVirtuelleHV wird
     * aber auch bei -1 ausgewertet. 
     * 
     * abWillenserklaerungsIdent: ab dieser Nummer wird eingelesen. Für
     * Präsenzliste auf 0 setzen!
     */
    public int read_Praesenzliste(int verzeichnis, int listenNummer, int abWillenserklaerungsident, boolean pListeFuerVirtuelleHV) {

        int anzInArray = 0;
        PreparedStatement lPStm = null;

        String lSql = "SELECT w.willenserklaerungIdent, w.willenserklaerung, w.aktien, w.stimmen,"
                + "w.meldungsIdent, w.zutrittsIdent, w.stimmkarte1, w.bevollmaechtigterDritterIdent, "
                + "w.veraenderungszeit, m.aktionaersnummer, "
                + "m.meldungstyp, m.besitzart, m.gattung, m.skOffenlegung, m.skIst, m.zutrittsident, "
                + "p.name, p.vorname, p.ort ," + "p1.name, p1.vorname, p1.ort,"
                + "m1.meldungsIdent, m1.skOffenlegung, m1.skIst, " + "p2.name, p2.vorname, p2.ort " + "FROM "
                + dbBundle.getSchemaMandant() + "tbl_willenserklaerung w " + "INNER JOIN " + dbBundle.getSchemaMandant()
                + "tbl_meldungen m ON m.meldungsIdent=w.meldungsIdent " // Aktionärs-Meldung zu Willenserklärung
                + "INNER JOIN " + dbBundle.getSchemaMandant() + "tbl_personenNatJur p ON m.personenNatJurIdent=p.ident " // Person
                                                                                                                         // zu
                                                                                                                         // Aktionärs-Meldung
                + "LEFT OUTER JOIN " + dbBundle.getSchemaMandant()
                + "tbl_personenNatJur p1 ON (w.bevollmaechtigterDritterIdent=p1.ident AND w.mandant=p1.mandant) " // Vertreter
                                                                                                                  // zu
                                                                                                                  // Willenserklärung
                + "LEFT OUTER JOIN " + dbBundle.getSchemaMandant()
                + "tbl_meldungen m1 ON (m1.meldungsIdent=w.identMeldungZuSammelkarte AND m1.mandant=w.mandant) " // Sammelkarte
                                                                                                                 // zu
                                                                                                                 // Willenserklärung
                + "LEFT OUTER JOIN " + dbBundle.getSchemaMandant()
                + "tbl_personenNatJur p2 ON (m1.personenNatJurIdent=p2.ident AND m1.mandant=p2.mandant) " // natürliche
                                                                                                          // Person der
                                                                                                          // Sammelkarte
                + "WHERE " + "w.mandant=? AND m.mandant=? AND p.mandant=? AND ";
        if (verzeichnis != -1) {
            lSql = lSql + "w.zuVerzeichnisNr" + Integer.toString(verzeichnis) + "Gedruckt="
                    + Integer.toString(listenNummer) + " AND ";
        }
        //@formatter:off
        lSql = lSql + "("  
                + "w.willenserklaerung=" + KonstWillenserklaerung.erstzugang + " OR " 
                + "w.willenserklaerung=" + KonstWillenserklaerung.abgang + " OR " 
                + "w.willenserklaerung=" + KonstWillenserklaerung.wiederzugang + " OR " 
                + "w.willenserklaerung=" + KonstWillenserklaerung.vertreterwechsel + " OR "
                + "w.willenserklaerung=" + KonstWillenserklaerung.abgangAusSRV + " OR " 
                + "w.willenserklaerung=" + KonstWillenserklaerung.abgangAusOrga + " OR " 
                + "w.willenserklaerung=" + KonstWillenserklaerung.abgangAusDauervollmacht + " OR " 
                + "w.willenserklaerung=" + KonstWillenserklaerung.abgangAusKIAV + " OR " 
                + "w.willenserklaerung=" + KonstWillenserklaerung.zugangInSRV + " OR " 
                + "w.willenserklaerung=" + KonstWillenserklaerung.zugangInOrga + " OR " 
                + "w.willenserklaerung=" + KonstWillenserklaerung.zugangInDauervollmacht + " OR " 
                + "w.willenserklaerung=" + KonstWillenserklaerung.zugangInKIAV + " OR " 
                + "w.willenserklaerung=" + KonstWillenserklaerung.wechselInSRV + " OR " 
                + "w.willenserklaerung=" + KonstWillenserklaerung.wechselInOrga + " OR " 
                + "w.willenserklaerung=" + KonstWillenserklaerung.wechselInDauervollmacht + " OR " 
                + "w.willenserklaerung=" + KonstWillenserklaerung.wechselInKIAV;
        if (pListeFuerVirtuelleHV) {
            lSql=lSql+" OR "
                    + "w.willenserklaerung=" + KonstWillenserklaerung.virtZugangSelbst + " OR " 
                    + "w.willenserklaerung=" + KonstWillenserklaerung.virtWiederzugangSelbst + " OR " 
                    + "w.willenserklaerung=" + KonstWillenserklaerung.virtAbgang + " OR " 
                    + "w.willenserklaerung=" + KonstWillenserklaerung.virtZugangVollmacht + " OR " 
                    + "w.willenserklaerung=" + KonstWillenserklaerung.virtWiederzugangVollmacht + " OR " 
                    + "w.willenserklaerung=" + KonstWillenserklaerung.virtAbgangVollmacht;
       }
        lSql = lSql   
                + ") " + "AND w.willenserklaerungIdent>? "
                + "ORDER BY w.meldungsIdent, w. willenserklaerungIdent ";
        //@formatter: on

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
                ergebnisArrayPraesenzliste[i].veraenderungszeit = lErgebnis.getString("w.veraenderungszeit");
                ergebnisArrayPraesenzliste[i].aktionaersnummer = lErgebnis.getString("m.aktionaersnummer");
                ergebnisArrayPraesenzliste[i].meldeIdentAktionaer = lErgebnis.getInt("w.meldungsIdent");
                ergebnisArrayPraesenzliste[i].meldungstyp = lErgebnis.getInt("m.meldungstyp");
                ergebnisArrayPraesenzliste[i].besitzartKuerzel = lErgebnis.getString("m.besitzart");
                ergebnisArrayPraesenzliste[i].gattung = lErgebnis.getInt("m.gattung");
                ergebnisArrayPraesenzliste[i].meldungSkIst = lErgebnis.getInt("m.skIst");
                ergebnisArrayPraesenzliste[i].fuelleMeldungOffenlegungTatsaechlich(dbBundle,
                        lErgebnis.getInt("m.skOffenlegung"));
                ergebnisArrayPraesenzliste[i].zutrittsIdent = lErgebnis.getString("w.zutrittsIdent");
                ergebnisArrayPraesenzliste[i].stimmkarte = lErgebnis.getString("w.stimmkarte1");
                ergebnisArrayPraesenzliste[i].zutrittsIdentMeldung = lErgebnis.getString("m.zutrittsIdent");
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

                // ergebnisArrayPraesenzliste[i].skOffenlegung=lErgebnis.getInt("m1.skOffenlegung");
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
                + "p1.name, p1.vorname, p1.ort," + "m1.meldungsIdent, m1.skOffenlegung, m1.skIst, "
                + "p2.name, p2.vorname, p2.ort " + "FROM " + dbBundle.getSchemaMandant() + "tbl_willenserklaerung w "
                + "INNER JOIN " + dbBundle.getSchemaMandant() + "tbl_meldungen m ON m.meldungsIdent=w.meldungsIdent " // Aktionärs-Meldung
                                                                                                                      // zu
                                                                                                                      // Willenserklärung
                + "INNER JOIN " + dbBundle.getSchemaMandant() + "tbl_personenNatJur p ON m.personenNatJurIdent=p.ident " // Person
                                                                                                                         // zu
                                                                                                                         // Aktionärs-Meldung
                + "LEFT OUTER JOIN " + dbBundle.getSchemaMandant()
                + "tbl_personenNatJur p1 ON (w.bevollmaechtigterDritterIdent=p1.ident AND w.mandant=p1.mandant) " // Vertreter
                                                                                                                  // zu
                                                                                                                  // Willenserklärung
                + "LEFT OUTER JOIN " + dbBundle.getSchemaMandant()
                + "tbl_meldungen m1 ON (m1.meldungsIdent=w.identMeldungZuSammelkarte AND m1.mandant=w.mandant) " // Sammelkarte
                                                                                                                 // zu
                                                                                                                 // Willenserklärung
                + "LEFT OUTER JOIN " + dbBundle.getSchemaMandant()
                + "tbl_personenNatJur p2 ON (m1.personenNatJurIdent=p2.ident AND m1.mandant=p2.mandant) " // natürliche
                                                                                                          // Person der
                                                                                                          // Sammelkarte
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
                // ergebnisArrayPraesenzliste[i].meldungSkOffenlegung=lErgebnis.getInt("m.skOffenlegung");
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

                // ergebnisArrayPraesenzliste[i].skOffenlegung=lErgebnis.getInt("m1.skOffenlegung");
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
                + "INNER JOIN " + dbBundle.getSchemaMandant() + "tbl_meldungen m ON m.meldungsIdent=w.meldungsIdent " // Aktionärs-Meldung
                                                                                                                      // zu
                                                                                                                      // Willenserklärung
                + "INNER JOIN " + dbBundle.getSchemaMandant() + "tbl_personenNatJur p ON m.personenNatJurIdent=p.ident " // Person
                                                                                                                         // zu
                                                                                                                         // Aktionärs-Meldung
                + "LEFT OUTER JOIN " + dbBundle.getSchemaMandant()
                + "tbl_personenNatJur p1 ON (w.willenserklaerungGeberIdent=p1.ident AND w.mandant=p1.mandant) " // Vertreter
                                                                                                                // zu
                                                                                                                // Willenserklärung
                + "LEFT OUTER JOIN " + dbBundle.getSchemaMandant()
                + "tbl_meldungen m1 ON (m1.meldungsIdent=w.identMeldungZuSammelkarte AND m1.mandant=w.mandant) " // Sammelkarte
                                                                                                                 // zu
                                                                                                                 // Willenserklärung
                + "LEFT OUTER JOIN " + dbBundle.getSchemaMandant()
                + "tbl_personenNatJur p2 ON (m1.personenNatJurIdent=p2.ident AND m1.mandant=p2.mandant) " // natürliche
                                                                                                          // Person der
                                                                                                          // Sammelkarte
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
                ergebnisArrayPraesenzliste[i].meldungSkIst = lErgebnis.getInt("m.skIst");
                ergebnisArrayPraesenzliste[i].fuelleMeldungOffenlegungTatsaechlich(dbBundle,
                        lErgebnis.getInt("m.skOffenlegung"));
                // ergebnisArrayPraesenzliste[i].meldungSkOffenlegung=lErgebnis.getInt("m.skOffenlegung");
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

                // ergebnisArrayPraesenzliste[i].skOffenlegung=lErgebnis.getInt("m1.skOffenlegung");
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

    /**
     * Liefert willenserklaerungen und meldungen für alle Meldungen, die mal als
     * einzelne EK (d.h. selbst oder mit Vertreter, aber eben nicht in Sammelkarte)
     * zugegangen und damit mal präsent waren.
     * 
     * Verwendung z.B. Zählung der EKs
     * 
     * Liefert nur Aktionärs-Meldungen, keine Gäste!
     * 
     * @return
     */
    public int read_allePraesentGewesenenEinzelEKs() {
        int anzInArray = 0;
        PreparedStatement lPStm = null;
        String lSql = "SELECT * " + "FROM " + dbBundle.getSchemaMandant() + "tbl_willenserklaerung w " + "INNER JOIN "
                + dbBundle.getSchemaMandant() + "tbl_meldungen m ON m.meldungsIdent=w.meldungsIdent " // Aktionärs-Meldung
                                                                                                      // zu
                                                                                                      // Willenserklärung
                + "INNER JOIN " + dbBundle.getSchemaMandant() + "tbl_personenNatJur p ON m.personenNatJurIdent=p.ident " // Person
                                                                                                                         // zu
                                                                                                                         // Aktionärs-Meldung
                + "LEFT OUTER JOIN " + dbBundle.getSchemaMandant()
                + "tbl_personenNatJur p1 ON (w.bevollmaechtigterDritterIdent=p1.ident AND w.mandant=p1.mandant) " // Vertreter
                                                                                                                  // zu
                                                                                                                  // Willenserklärung
                + "WHERE " + "w.mandant=? AND m.mandant=? AND p.mandant=? AND " + "m.meldungstyp=1 AND m.klasse=1 AND " // Warum?
                                                                                                                        // Dann
                                                                                                                        // nämlich
                                                                                                                        // keine
                                                                                                                        // die
                                                                                                                        // in
                                                                                                                        // Sammelkarte
                                                                                                                        // gegangen
                                                                                                                        // sind
                + "w.willenserklaerung=" + KonstWillenserklaerung.erstzugang + " AND w.identMeldungZuSammelkarte=0 "
                + "ORDER BY w.meldungsIdent, w. willenserklaerungIdent ";
        try {
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, dbBundle.clGlobalVar.mandant);
            lPStm.setInt(2, dbBundle.clGlobalVar.mandant);
            lPStm.setInt(3, dbBundle.clGlobalVar.mandant);

            ResultSet lErgebnis = lPStm.executeQuery();
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();
            ergebnisArrayMeldung = new EclMeldung[anzInArray];
            ergebnisArrayPersonenNatJur = new EclPersonenNatJur[anzInArray];
            ergebnisArrayWillenserklaerung = new EclWillenserklaerung[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {
                ergebnisArrayMeldung[i] = dbBundle.dbMeldungen.decodeErgebnis(lErgebnis);
                ergebnisArrayPersonenNatJur[i] = dbBundle.dbPersonenNatJur.decodeErgebnis(lErgebnis);
                ergebnisArrayWillenserklaerung[i] = dbBundle.dbWillenserklaerung.decodeErgebnis(lErgebnis);

                i++;
            }
            lErgebnis.close();
            lPStm.close();

        } catch (SQLException e) {
            CaBug.drucke("DbJoined.read_allePraesentGewesenenEinzelEKs 003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    // Ausgabe aller angemeldeten Aktionäre außerhalb von Sammelkarten und als Liste
    public List<EclMeldung> readAllMeldungenEKs() {

        List<EclMeldung> list = new ArrayList<>();

        try {
            String lSql = "SELECT * from " + dbBundle.getSchemaMandant() + "tbl_meldungen m " + "INNER JOIN "
                    + dbBundle.getSchemaMandant() + "tbl_personenNatJur p ON m.personenNatJurIdent=p.ident "
                    + "WHERE m.mandant=? and p.mandant=? " + "AND m.meldungAktiv=1 AND m.klasse=1 AND m.meldungstyp=1 AND zutrittsIdent<>''";

            lPStm_meldeliste = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            lPStm_meldeliste.setInt(1, dbBundle.clGlobalVar.mandant);
            lPStm_meldeliste.setInt(2, dbBundle.clGlobalVar.mandant);

            System.out.println(lSql);

            lErgebnis_meldeliste = lPStm_meldeliste.executeQuery();

            while (lErgebnis_meldeliste.next()) {
                list.add(dbBundle.dbMeldungen.decodeErgebnis(lErgebnis_meldeliste));
            }

        } catch (Exception e) {
            CaBug.drucke("DbJoined.readinit_meldeliste 003");
            System.err.println(" " + e.getMessage());
            return null;
        }
        return list;
    }

    public int read_SummeAktien() {

        PreparedStatement lPStm = null;

        int aktien = 0;

        String lSql = "SELECT  SUM(m.stueckaktien) FROM " + dbBundle.getSchemaMandant() + "tbl_meldungen m "
                + "WHERE m.mandant=? AND " + "m.meldungaktiv=1 AND " + "m.klasse=1 AND "
                + "(m.meldungstyp=1 OR m.meldungstyp=3) ";

        try {
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, dbBundle.clGlobalVar.mandant);

            ResultSet lErgebnis = lPStm.executeQuery();

            if (lErgebnis.next())
                aktien = lErgebnis.getInt(1);

            lErgebnis.close();
            lPStm.close();

        } catch (SQLException e) {
            CaBug.drucke("DbJoined.read_angemeldeteStimmen 003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return aktien;
    }

    /**********************************
     * Suchen (für CtrlStatus)
     ****************************************************/

    public int read_suchenAktionaersnummer(String suchName) {
        int anzInArray = 0;
        PreparedStatement lPStm = null;

        CaBug.druckeLog("suchName="+suchName, logDruckenInt, 10);
        
        String lSql = "SELECT * " + "FROM " + dbBundle.getSchemaMandant() + "tbl_aktienregister are "
                + "LEFT OUTER JOIN " + dbBundle.getSchemaMandant()
                + "tbl_meldungen m on (m.aktionaersnummer=are.aktionaersnummer AND m.mandant=are.mandant) "
                + "where (are.aktionaersnummer=? OR  are.aktionaersnummer=? OR are.aktionaersnummer=?) "
                + "AND are.mandant=? ORDER BY are.nameKomplett ";

//        String lSql = "SELECT * " + "FROM " + dbBundle.getSchemaMandant() + "tbl_aktienregister are "
//                + "where (are.aktionaersnummer='?' OR  are.aktionaersnummer='?' OR are.aktionaersnummer='?') "
//                + "LEFT OUTER JOIN " + dbBundle.getSchemaMandant()
//                + "tbl_meldungen m on m.aktionaersnummer=are.aktionaersnummer "
//                + "ORDER BY are.nameKomplett ";

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

    /** Siehe read_suchenErstVertreter */
    @Deprecated
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
                    ergebnisArraySuchergebnis[i].meldungsIdent = lErgebnis.getInt("m.meldungsIdent");
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

        // String lSql="SELECT * "
        // + "FROM tbl_zutrittskarten z "
        // + "INNER JOIN tbl_meldungen m ON (z.meldungsIdentAktionaer=m.meldungsIdent or
        // z.meldungsIdentGast=m.meldungsIdent) "
        // + "INNER JOIN tbl_personenNatJur p ON m.personenNatJurIdent=p.ident "
        // + "WHERE p.name LIKE ? "
        // + "AND z.mandant=? AND m.mandant=? AND p.mandant=? LIMIT 250";

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

    /** Für BlStatistik.praesenzBuchungenProArbeitsplatz */
    public int read_praesenzBuchungenProArbeitsplatz() {

        int anzInArray = 0;
        PreparedStatement lPStm = null;

        String selektionWillenserklaerungen = KonstWillenserklaerung.getInSelectPraesenzbuchungen();

        String lSql = "SELECT m.*, p.*, w.* FROM " + "" + dbBundle.getSchemaMandant()
                + "tbl_willenserklaerung w INNER JOIN " + dbBundle.getSchemaMandant()
                + "tbl_meldungen m ON m.meldungsIdent=w.meldungsIdent " + "INNER JOIN " + dbBundle.getSchemaMandant()
                + "tbl_PersonenNatJur p on m.personenNatJurIdent=p.ident "
                + "WHERE m.mandant=? AND m.meldungAktiv=1 AND p.mandant=? AND w.mandant=? and w.willenserklaerung IN "
                + selektionWillenserklaerungen + " ORDER BY w.arbeitsplatz, w.veraenderungszeit";

        lSql = lSql + ";";
        // System.out.println(lSql);
        try {
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, dbBundle.clGlobalVar.mandant);
            lPStm.setInt(2, dbBundle.clGlobalVar.mandant);
            lPStm.setInt(3, dbBundle.clGlobalVar.mandant);

            ResultSet lErgebnis = lPStm.executeQuery();
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();
            ergebnisArrayMeldung = new EclMeldung[anzInArray];
            ergebnisArrayPersonenNatJur = new EclPersonenNatJur[anzInArray];
            ergebnisArrayWillenserklaerung = new EclWillenserklaerung[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {
                ergebnisArrayMeldung[i] = dbBundle.dbMeldungen.decodeErgebnis(lErgebnis);
                ergebnisArrayPersonenNatJur[i] = dbBundle.dbPersonenNatJur.decodeErgebnis(lErgebnis);
                ergebnisArrayWillenserklaerung[i] = dbBundle.dbWillenserklaerung.decodeErgebnis(lErgebnis);
                i++;
            }
            lErgebnis.close();
            lPStm.close();

        } catch (SQLException e) {
            CaBug.drucke("DbJoined.read_praesenzBuchungenProArbeitsplatz 003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    /********* Abstimmungsverhalten **********************************/
    /**
     * Für Ausgabe des Abstimmungsverhaltens * pSortierung: Sortierung nach 1 =
     * meldeIdent 2 = zutrittsIdent (aktuelle) 3 = StimmkartenIdent (aktuelle) 4 =
     * Name Meldung 5 = Größe Aktienzahl (absteigend)
     */
    public int read_abstimmungsVerhaltenEinzelkarten(int pSortierung) {

        int anzInArray = 0;
        PreparedStatement lPStm = null;

        String lSql = "SELECT m.*, p.*, am.* FROM " + "" + dbBundle.getSchemaMandant()
                + "tbl_abstimmungMeldung am INNER JOIN " + dbBundle.getSchemaMandant()
                + "tbl_meldungen m ON m.meldungsIdent=am.meldungsIdent " + "INNER JOIN " + dbBundle.getSchemaMandant()
                + "tbl_PersonenNatJur p on m.personenNatJurIdent=p.ident "
                + "WHERE m.mandant=? AND m.meldungAktiv=1 AND p.mandant=? AND am.mandant=? and " + "am.aktiv=1 "
                + "ORDER BY ";
        switch (pSortierung) {
        case 1:
            lSql = lSql + " m.meldungsIdent";
            break;
        case 2:
            lSql = lSql + " m.zutrittsIdent";
            break;
        case 3:
            lSql = lSql + " m.stimmkarte";
            break;
        case 4:
            lSql = lSql + " p.name";
            break;
        case 5:
            lSql = lSql + " m.stueckAktien";
            break;
        }
        lSql = lSql + ";";
        // System.out.println(lSql);
        try {
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, dbBundle.clGlobalVar.mandant);
            lPStm.setInt(2, dbBundle.clGlobalVar.mandant);
            lPStm.setInt(3, dbBundle.clGlobalVar.mandant);

            ResultSet lErgebnis = lPStm.executeQuery();
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();
            ergebnisArrayMeldung = new EclMeldung[anzInArray];
            ergebnisArrayPersonenNatJur = new EclPersonenNatJur[anzInArray];
            ergebnisArrayAbstimmungMeldung = new EclAbstimmungMeldung[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {
                ergebnisArrayMeldung[i] = dbBundle.dbMeldungen.decodeErgebnis(lErgebnis);
                ergebnisArrayPersonenNatJur[i] = dbBundle.dbPersonenNatJur.decodeErgebnis(lErgebnis);
                ergebnisArrayAbstimmungMeldung[i] = dbBundle.dbAbstimmungMeldung.decodeErgebnis(lErgebnis);
                i++;
            }
            lErgebnis.close();
            lPStm.close();

        } catch (SQLException e) {
            CaBug.drucke("DbJoined.read_abstimmungsVerhaltenEinzelkarten 003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    /**
     * Für Ausgabe des Abstimmungsverhaltens * pSortierung: Sortierung nach 1 =
     * meldeIdent 2 = zutrittsIdent (aktuelle) 3 = StimmkartenIdent (aktuelle) 4 =
     * Name Meldung 5 = Größe Aktienzahl (absteigend)
     */
    public int read_abstimmungsVerhaltenSammelkarten(int pSortierung) {

        int anzInArray = 0;
        PreparedStatement lPStm = null;

        String lSql = "SELECT m.*, p.*, ams.*, ams1.*, ams2.*, ams3.* FROM " + "" + dbBundle.getSchemaMandant()
                + "tbl_abstimmungMeldungSplit ams " + "INNER JOIN " + dbBundle.getSchemaMandant()
                + "tbl_abstimmungMeldungSplit1 ams1 ON ams.meldungsIdent=ams1.meldungsIdent " + "INNER JOIN "
                + dbBundle.getSchemaMandant()
                + "tbl_abstimmungMeldungSplit2 ams2 ON ams.meldungsIdent=ams2.meldungsIdent " + "INNER JOIN "
                + dbBundle.getSchemaMandant()
                + "tbl_abstimmungMeldungSplit3 ams3 ON ams.meldungsIdent=ams3.meldungsIdent " + "INNER JOIN "
                + dbBundle.getSchemaMandant() + "tbl_meldungen m ON m.meldungsIdent=ams.meldungsIdent " + "INNER JOIN "
                + dbBundle.getSchemaMandant() + "tbl_PersonenNatJur p on m.personenNatJurIdent=p.ident "
                + "WHERE m.mandant=? AND m.meldungAktiv=1 AND p.mandant=? AND ams.mandant=? " + "ORDER BY ";
        switch (pSortierung) {
        case 1:
            lSql = lSql + " m.meldungsIdent";
            break;
        case 2:
            lSql = lSql + " m.zutrittsIdent";
            break;
        case 3:
            lSql = lSql + " m.stimmkarte";
            break;
        case 4:
            lSql = lSql + " p.name";
            break;
        case 5:
            lSql = lSql + " m.stueckAktien";
            break;
        }
        lSql = lSql + ";";
        // System.out.println(lSql);
        try {
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, dbBundle.clGlobalVar.mandant);
            lPStm.setInt(2, dbBundle.clGlobalVar.mandant);
            lPStm.setInt(3, dbBundle.clGlobalVar.mandant);

            ResultSet lErgebnis = lPStm.executeQuery();
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();
            ergebnisArrayMeldung = new EclMeldung[anzInArray];
            ergebnisArrayPersonenNatJur = new EclPersonenNatJur[anzInArray];
            ergebnisArrayAbstimmungMeldungSplit = new EclAbstimmungMeldungSplit[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {
                ergebnisArrayMeldung[i] = dbBundle.dbMeldungen.decodeErgebnis(lErgebnis);
                ergebnisArrayPersonenNatJur[i] = dbBundle.dbPersonenNatJur.decodeErgebnis(lErgebnis);
                ergebnisArrayAbstimmungMeldungSplit[i] = dbBundle.dbAbstimmungMeldungSplit
                        .decodeErgebnisSplit(lErgebnis);
                dbBundle.dbAbstimmungMeldungSplit.decodeErgebnisSplit1(lErgebnis,
                        ergebnisArrayAbstimmungMeldungSplit[i]);
                dbBundle.dbAbstimmungMeldungSplit.decodeErgebnisSplit2(lErgebnis,
                        ergebnisArrayAbstimmungMeldungSplit[i]);
                dbBundle.dbAbstimmungMeldungSplit.decodeErgebnisSplit3(lErgebnis,
                        ergebnisArrayAbstimmungMeldungSplit[i]);
                i++;
            }
            lErgebnis.close();
            lPStm.close();

        } catch (SQLException e) {
            CaBug.drucke("DbJoined.read_abstimmungsVerhaltenSammelkarten 003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    /****************
     * Für Statistik - ku178 - anmeldungen, bedingte anmeldungen, akzeptierte
     * anmeldungen*********** pbesitzart = E, F, V Returnwert = Anzahl
     */
    public int read_ku178Anzahl(String pBesitzart) {

        PreparedStatement lPStm = null;
        int ergebnis = 0;

        String lSql = "SELECT COUNT(*) " + "FROM " + dbBundle.getSchemaMandant() + "tbl_aktienregister are "
                + "INNER JOIN " + dbBundle.getSchemaMandant()
                + "tbl_meldungen m ON (are.aktionaersnummer=m.aktionaersnummer AND are.mandant=m.mandant) "
                + "WHERE are.mandant=? AND are.besitzart=? ";

        try {
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, dbBundle.clGlobalVar.mandant);
            lPStm.setString(2, pBesitzart);

            ResultSet lErgebnis = lPStm.executeQuery();
            lErgebnis.last();
            lErgebnis.beforeFirst();

            while (lErgebnis.next() == true) {
                ergebnis = lErgebnis.getInt(1);
            }

            lErgebnis.close();
            lPStm.close();

        } catch (SQLException e) {
            CaBug.drucke("DbJoined.read_ku178Anzahl 003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (ergebnis);
    }

    /****************
     * Für BlSuchlauf***********
     */
    public int read_suchlaufErgebnisRegister(int suchlaufIdent) {

        int anzInArray = 0;
        PreparedStatement lPStm = null;

        String lSql = "SELECT * " + "FROM " + dbBundle.getSchemaMandant() + "tbl_suchlaufErgebnis sue " + "INNER JOIN "
                + dbBundle.getSchemaMandant()
                + "tbl_aktienregister are ON sue.identAktienregister=are.aktienregisterIdent AND sue.mandant=are.mandant "
                + "WHERE sue.mandant=? AND sue.identSuchlaufDefinition=?; ";

        try {
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, dbBundle.clGlobalVar.mandant);
            lPStm.setInt(2, suchlaufIdent);

            ResultSet lErgebnis = lPStm.executeQuery();
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();
            /*
             * Hinweis: ergebnisArrayMeldung wird eigentlich wg. Datenbank-Ergebnis nicht
             * benötigt, aber zwecks leichterer Weiterverarbeitung in Business-Logik
             * (Einheitlichkein hin zu read_suchlaufErgebnisMeldungen) mit
             * "new EclMeldung"-Elementen aufgefüllt.
             */
            ergebnisArrayMeldung = new EclMeldung[anzInArray];
            ergebnisArrayAktienregisterEintrag = new EclAktienregister[anzInArray];
            ergebnisArraySuchlaufErgebnis = new EclSuchlaufErgebnis[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {
                ergebnisArrayMeldung[i] = new EclMeldung();
                ergebnisArrayAktienregisterEintrag[i] = dbBundle.dbAktienregister.decodeErgebnis(lErgebnis);
                ergebnisArraySuchlaufErgebnis[i] = dbBundle.dbSuchlaufErgebnis.decodeErgebnis(lErgebnis);
                i++;
            }
            lErgebnis.close();
            lPStm.close();

        } catch (SQLException e) {
            CaBug.drucke("DbJoined.read_suchlaufErgebnisRegister 003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    public int read_suchlaufErgebnisMeldungen(int suchlaufIdent) {

        int anzInArray = 0;
        PreparedStatement lPStm = null;

        String lSql = "SELECT * " + "FROM " + dbBundle.getSchemaMandant() + "tbl_suchlaufErgebnis sue " + "INNER JOIN "
                + dbBundle.getSchemaMandant()
                + "tbl_meldungen m ON sue.identMelderegister=m.meldungsIdent AND sue.mandant=m.mandant " + "INNER JOIN "
                + dbBundle.getSchemaMandant()
                + "tbl_aktienregister are ON m.aktionaersnummer=are.aktionaersnummer and m.mandant=are.mandant  "
                + "INNER JOIN " + dbBundle.getSchemaMandant() + "tbl_personenNatJur p ON m.personenNatJurIdent=p.ident "
                + "WHERE sue.mandant=? AND sue.identSuchlaufDefinition=? ; ";

        try {
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, dbBundle.clGlobalVar.mandant);
            lPStm.setInt(2, suchlaufIdent);

            ResultSet lErgebnis = lPStm.executeQuery();
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();
            ergebnisArrayMeldung = new EclMeldung[anzInArray];
            ergebnisArrayAktienregisterEintrag = new EclAktienregister[anzInArray];
            ergebnisArraySuchlaufErgebnis = new EclSuchlaufErgebnis[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {
                ergebnisArrayMeldung[i] = dbBundle.dbMeldungen.decodeErgebnis(lErgebnis);
                ergebnisArrayAktienregisterEintrag[i] = dbBundle.dbAktienregister.decodeErgebnis(lErgebnis);
                ergebnisArraySuchlaufErgebnis[i] = dbBundle.dbSuchlaufErgebnis.decodeErgebnis(lErgebnis);
                i++;
            }
            lErgebnis.close();
            lPStm.close();

        } catch (SQLException e) {
            CaBug.drucke("DbJoined.read_suchlaufErgebnisRegister 003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    /********************
     * Für BlSuchen und BlSuchlauf
     ****************************************/

    private String liefereSuchBegriff(String[] pSuchbegriff) {
        String lSuchbegriff = "";
        for (int i = 0; i < pSuchbegriff.length; i++) {
            if (!pSuchbegriff[i].isEmpty()) {
                if (i > 0) {
                    lSuchbegriff = lSuchbegriff + "|";
                }
                lSuchbegriff = lSuchbegriff + pSuchbegriff[i];
            }
        }
        return lSuchbegriff;
    }

    /**
     * pSuchbegriffArt= KonstSuchlaufSuchbegriffArt.registerNummer (hinten %)
     * KonstSuchlaufSuchbegriffArt.nameAktionaer (vorne und hinten %)
     * 
     */
    public int read_suchenErstAktienregister(int pSuchbegriffArt, String[] pSuchbegriff) {

//        if (CaBug.pruefeLog(logDruckenInt, 10)) {
//            for (int i=0;i<pSuchbegriff.length;i++) {
//                CaBug.druckeLog("pSuchbegriff[]="+pSuchbegriff[i], logDruckenInt, 10);
//            }
//        }
        int anzInArray = 0;
        PreparedStatement lPStm = null;

//        String lSql = "SELECT * " + "FROM " + dbBundle.getSchemaMandant() + "tbl_aktienregister are " + "LEFT JOIN "
//                + dbBundle.getSchemaMandant()
//                + "tbl_meldungen m ON are.aktionaersnummer=m.aktionaersnummer and are.mandant=m.mandant " + "LEFT JOIN "
//                + dbBundle.getSchemaMandant()
//                + "tbl_personennatjur p on m.personenNatJurIdent=p.ident and m.mandant=p.mandant ";
//
//        switch (pSuchbegriffArt) {
//        case KonstSuchlaufSuchbegriffArt.registerNummer:
//            lSql = lSql + "WHERE are.aktionaersnummer REGEXP ? AND are.mandant=?; ";
//            break;
//        case KonstSuchlaufSuchbegriffArt.nameAktionaer:
//            lSql = lSql + "WHERE (are.name1 REGEXP ? OR are.nachname REGEXP ?) AND are.mandant=?; ";
//            break;
//        }

        
        String lSuchbegriff = liefereSuchBegriff(pSuchbegriff);

        
        String lSql = "SELECT * " + "FROM " + dbBundle.getSchemaMandant() + "tbl_aktienregister are ";


        lSql=lSql+ "LEFT JOIN "
                + dbBundle.getSchemaMandant()
                + "tbl_meldungen m ON are.aktionaersnummer=m.aktionaersnummer " + "LEFT JOIN "
                + dbBundle.getSchemaMandant()
                + "tbl_personennatjur p on m.personenNatJurIdent=p.ident ";

        switch (pSuchbegriffArt) {
        case KonstSuchlaufSuchbegriffArt.registerNummer:
            if (ParamSpezial.ku302_303(dbBundle.clGlobalVar.mandant)==true) {
                /*G = GP-Nummer für ku302_303*/
                if (lSuchbegriff.length()>2 && lSuchbegriff.startsWith("G")) {
                    lSql = lSql + "WHERE are.adresszeile10 = ? "; 
                }
                else {
                    lSql = lSql + "WHERE are.aktionaersnummer REGEXP ? ";
                }
            }
            else {
                lSql = lSql + "WHERE are.aktionaersnummer REGEXP ? ";
            }
            break;
        case KonstSuchlaufSuchbegriffArt.nameAktionaer:
            lSql = lSql + "WHERE (are.name1 REGEXP ? OR are.nachname REGEXP ? OR are.vorname REGEXP ?) ";
            break;
        }


        try {
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            switch (pSuchbegriffArt) {
            case KonstSuchlaufSuchbegriffArt.registerNummer:
                if (ParamSpezial.ku302_303(dbBundle.clGlobalVar.mandant)==true) {
                    /*G = GP-Nummer für ku302_303*/
                    if (lSuchbegriff.length()>=2 && lSuchbegriff.startsWith("G")) {
                        lPStm.setString(1, lSuchbegriff.substring(1));
                    }
                    else {
                        lPStm.setString(1, lSuchbegriff);
                    }
                }
                else {
                    lPStm.setString(1, lSuchbegriff);
                }
//                lPStm.setInt(2, dbBundle.clGlobalVar.mandant);
                break;
            case KonstSuchlaufSuchbegriffArt.nameAktionaer:
                lPStm.setString(1, lSuchbegriff);
                lPStm.setString(2, lSuchbegriff);
                lPStm.setString(3, lSuchbegriff);
                break;
            }

            ResultSet lErgebnis = lPStm.executeQuery();
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();
            ergebnisArrayAktienregisterEintrag = new EclAktienregister[anzInArray];
            ergebnisArrayMeldung = new EclMeldung[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {
                ergebnisArrayAktienregisterEintrag[i] = dbBundle.dbAktienregister.decodeErgebnis(lErgebnis);
                ergebnisArrayMeldung[i] = dbBundle.dbMeldungen.decodeErgebnis(lErgebnis);
                if (ParamSpezial.ku302_303(dbBundle.clGlobalVar.mandant)==true) {
                    /*G = GP-Nummer für ku302_303*/
                    ergebnisArrayAktienregisterEintrag[i].nameKomplett=ergebnisArrayAktienregisterEintrag[i].nameKomplett+" "+ergebnisArrayAktienregisterEintrag[i].adresszeile10;
                    ergebnisArrayMeldung[i].kurzName=ergebnisArrayMeldung[i].kurzName+" "+ergebnisArrayAktienregisterEintrag[i].adresszeile10;
                }
                i++;
            }
            lErgebnis.close();
            lPStm.close();

        } catch (SQLException e) {
            CaBug.drucke("DbJoined.read_suchenErstAktienregister 003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    /**
     * pSuchbegriffArt= 
     * > KonstSuchlaufSuchbegriffArt.ekNummer (nur identisch)
     */
    public int read_suchenErstEKNr(int pSuchbegriffArt, String[] pSuchbegriff, boolean pDurchsuchenSammelkarten,
            boolean pDurchsuchenInSammelkarten, boolean pDurchsuchenGaeste) {

        EclAktienregister ergebnisArrayAktienregisterEintrag_Aktionaere[]=null;
        EclMeldung ergebnisArrayMeldung_Aktionaere[]=null;
        EclZutrittskarten ergebnisArrayZutrittskarten_Aktionaere[]=null;

        EclAktienregister ergebnisArrayAktienregisterEintrag_Gaeste[]=null;
        EclMeldung ergebnisArrayMeldung_Gaeste[]=null;
        EclZutrittskarten ergebnisArrayZutrittskarten_Gaeste[]=null;

        int anzInArray_Aktionaere = 0;
        int anzInArray_Gaeste = 0;
        
        PreparedStatement lPStm = null;

        /**Aktionäre einlesen*/
        //@formatter:off
        String lSql = "SELECT * " + "FROM " + dbBundle.getSchemaMandant()+"tbl_zutrittskarten zk "
                + "INNER JOIN " + dbBundle.getSchemaMandant()+"tbl_meldungen m ON zk.meldungsIdentAktionaer=m.meldungsIdent "
                + "LEFT JOIN " + dbBundle.getSchemaMandant()+"tbl_aktienregister are ON m.aktionaersnummer=are.aktionaersnummer and m.mandant=are.mandant "
                + "LEFT JOIN " + dbBundle.getSchemaMandant()+ "tbl_personennatjur p on m.personenNatJurIdent=p.ident and m.mandant=p.mandant " 
                + "WHERE ";
        //@formatter:on

        if (pDurchsuchenSammelkarten == false) {
            lSql = lSql + "m.meldungstyp!=2 AND ";
        }
        if (pDurchsuchenInSammelkarten == false) {
            lSql = lSql + "m.meldungstyp!=3 AND ";
        }
        if (pDurchsuchenGaeste == false) {
            lSql = lSql + "m.klasse!=0 AND ";
        }
        lSql = lSql + "m.zutrittsIdent=? AND m.mandant=?; ";

        try {
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setString(1, pSuchbegriff[0]);
            lPStm.setInt(2, dbBundle.clGlobalVar.mandant);

            ResultSet lErgebnis = lPStm.executeQuery();
            lErgebnis.last();
            anzInArray_Aktionaere = lErgebnis.getRow();
            lErgebnis.beforeFirst();
            ergebnisArrayAktienregisterEintrag_Aktionaere = new EclAktienregister[anzInArray_Aktionaere];
            ergebnisArrayMeldung_Aktionaere = new EclMeldung[anzInArray_Aktionaere];
            ergebnisArrayZutrittskarten_Aktionaere=new EclZutrittskarten[anzInArray_Aktionaere];

            int i = 0;
            while (lErgebnis.next() == true) {
                ergebnisArrayAktienregisterEintrag_Aktionaere[i] = dbBundle.dbAktienregister.decodeErgebnis(lErgebnis);
                ergebnisArrayMeldung_Aktionaere[i] = dbBundle.dbMeldungen.decodeErgebnis(lErgebnis);
                ergebnisArrayZutrittskarten_Aktionaere[i]=dbBundle.dbZutrittskarten.decodeErgebnis(lErgebnis);
                i++;
            }
            lErgebnis.close();
            lPStm.close();

        } catch (SQLException e) {
            CaBug.drucke("003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        
        if (pDurchsuchenGaeste) {
            /**Gäste einlesen*/
            //@formatter:off
            lSql = "SELECT * " + "FROM " + dbBundle.getSchemaMandant()+"tbl_zutrittskarten zk "
                    + "INNER JOIN " + dbBundle.getSchemaMandant()+"tbl_meldungen m ON zk.meldungsIdentGast=m.meldungsIdent "
                    + "LEFT JOIN " + dbBundle.getSchemaMandant()+"tbl_aktienregister are ON m.aktionaersnummer=are.aktionaersnummer and m.mandant=are.mandant "
                    + "LEFT JOIN " + dbBundle.getSchemaMandant()+ "tbl_personennatjur p on m.personenNatJurIdent=p.ident and m.mandant=p.mandant " 
                    + "WHERE ";
            //@formatter:on

            if (pDurchsuchenSammelkarten == false) {
                lSql = lSql + "m.meldungstyp!=2 AND ";
            }
            if (pDurchsuchenInSammelkarten == false) {
                lSql = lSql + "m.meldungstyp!=3 AND ";
            }
            lSql = lSql + "zk.zutrittsIdent=? AND m.mandant=?; ";

            try {
                lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                lPStm.setString(1, pSuchbegriff[0]);
                lPStm.setInt(2, dbBundle.clGlobalVar.mandant);

                ResultSet lErgebnis = lPStm.executeQuery();
                lErgebnis.last();
                anzInArray_Gaeste = lErgebnis.getRow();
                lErgebnis.beforeFirst();
                ergebnisArrayAktienregisterEintrag_Gaeste = new EclAktienregister[anzInArray_Gaeste];
                ergebnisArrayMeldung_Gaeste = new EclMeldung[anzInArray_Gaeste];
                ergebnisArrayZutrittskarten_Gaeste=new EclZutrittskarten[anzInArray_Gaeste];

                int i = 0;
                while (lErgebnis.next() == true) {
                    ergebnisArrayAktienregisterEintrag_Gaeste[i] = dbBundle.dbAktienregister.decodeErgebnis(lErgebnis);
                    ergebnisArrayMeldung_Gaeste[i] = dbBundle.dbMeldungen.decodeErgebnis(lErgebnis);
                    ergebnisArrayZutrittskarten_Gaeste[i]=dbBundle.dbZutrittskarten.decodeErgebnis(lErgebnis);
                    i++;
                }
                lErgebnis.close();
                lPStm.close();

            } catch (SQLException e) {
                CaBug.drucke("003");
                System.err.println(" " + e.getMessage());
                return (-1);
            }
            
        }
        
        /**Zusammenführen*/
        if (pDurchsuchenGaeste==false) {
            /*Nur Aktionäre*/
            ergebnisArrayAktienregisterEintrag=ergebnisArrayAktienregisterEintrag_Aktionaere;
            ergebnisArrayMeldung=ergebnisArrayMeldung_Aktionaere;
            ergebnisArrayZutrittskarten=ergebnisArrayZutrittskarten_Aktionaere;
        }
        else {
            /*Aktionäre und Gäste*/
            ergebnisArrayAktienregisterEintrag = new EclAktienregister[anzInArray_Aktionaere+anzInArray_Gaeste];
            ergebnisArrayMeldung = new EclMeldung[anzInArray_Aktionaere+anzInArray_Gaeste];
            ergebnisArrayZutrittskarten=new EclZutrittskarten[anzInArray_Aktionaere+anzInArray_Gaeste];
            
            for (int i=0;i<anzInArray_Aktionaere;i++) {
                ergebnisArrayAktienregisterEintrag[i]=ergebnisArrayAktienregisterEintrag_Aktionaere[i];
                ergebnisArrayMeldung[i]=ergebnisArrayMeldung_Aktionaere[i];
                ergebnisArrayZutrittskarten[i]=ergebnisArrayZutrittskarten_Aktionaere[i];
            }
            for (int i=0;i<anzInArray_Gaeste;i++) {
                ergebnisArrayAktienregisterEintrag[i+anzInArray_Aktionaere]=ergebnisArrayAktienregisterEintrag_Gaeste[i];
                ergebnisArrayMeldung[i+anzInArray_Aktionaere]=ergebnisArrayMeldung_Gaeste[i];
                ergebnisArrayZutrittskarten[i+anzInArray_Aktionaere]=ergebnisArrayZutrittskarten_Gaeste[i];
            }
        }
        return (anzInArray_Aktionaere+anzInArray_Gaeste);
    }

    /**
     * pSuchbegriffArt= 
     * > KonstSuchlaufSuchbegriffArt.skNummer (nur identisch)
     * 
     */
    public int read_suchenErstSKNr(int pSuchbegriffArt, String[] pSuchbegriff, boolean pDurchsuchenSammelkarten,
            boolean pDurchsuchenInSammelkarten, boolean pDurchsuchenGaeste) {
        EclAktienregister ergebnisArrayAktienregisterEintrag_Aktionaere[]=null;
        EclMeldung ergebnisArrayMeldung_Aktionaere[]=null;
        EclStimmkarten ergebnisArrayStimmkarten_Aktionaere[]=null;

        EclAktienregister ergebnisArrayAktienregisterEintrag_Gaeste[]=null;
        EclMeldung ergebnisArrayMeldung_Gaeste[]=null;
        EclStimmkarten ergebnisArrayStimmkarten_Gaeste[]=null;

        int anzInArray_Aktionaere = 0;
        int anzInArray_Gaeste = 0;
        
        PreparedStatement lPStm = null;

        /**Aktionäre einlesen*/
        //@formatter:off
        String lSql = "SELECT * " + "FROM " + dbBundle.getSchemaMandant()+"tbl_stimmkarten sk "
                + "INNER JOIN " + dbBundle.getSchemaMandant()+"tbl_meldungen m ON sk.meldungsIdentAktionaer=m.meldungsIdent "
                + "LEFT JOIN " + dbBundle.getSchemaMandant()+"tbl_aktienregister are ON m.aktionaersnummer=are.aktionaersnummer and m.mandant=are.mandant "
                + "LEFT JOIN " + dbBundle.getSchemaMandant()+ "tbl_personennatjur p on m.personenNatJurIdent=p.ident and m.mandant=p.mandant " 
                + "WHERE ";
        //@formatter:on

        if (pDurchsuchenSammelkarten == false) {
            lSql = lSql + "m.meldungstyp!=2 AND ";
        }
        if (pDurchsuchenInSammelkarten == false) {
            lSql = lSql + "m.meldungstyp!=3 AND ";
        }
        if (pDurchsuchenGaeste == false) {
            lSql = lSql + "m.klasse!=0 AND ";
        }
        lSql = lSql + "sk.stimmkarte=? AND m.mandant=?; ";

        try {
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setString(1, pSuchbegriff[0]);
            lPStm.setInt(2, dbBundle.clGlobalVar.mandant);

            ResultSet lErgebnis = lPStm.executeQuery();
            lErgebnis.last();
            anzInArray_Aktionaere = lErgebnis.getRow();
            lErgebnis.beforeFirst();
            ergebnisArrayAktienregisterEintrag_Aktionaere = new EclAktienregister[anzInArray_Aktionaere];
            ergebnisArrayMeldung_Aktionaere = new EclMeldung[anzInArray_Aktionaere];
            ergebnisArrayStimmkarten_Aktionaere=new EclStimmkarten[anzInArray_Aktionaere];

            int i = 0;
            while (lErgebnis.next() == true) {
                ergebnisArrayAktienregisterEintrag_Aktionaere[i] = dbBundle.dbAktienregister.decodeErgebnis(lErgebnis);
                ergebnisArrayMeldung_Aktionaere[i] = dbBundle.dbMeldungen.decodeErgebnis(lErgebnis);
                ergebnisArrayStimmkarten_Aktionaere[i]=dbBundle.dbStimmkarten.decodeErgebnis(lErgebnis);
                i++;
            }
            lErgebnis.close();
            lPStm.close();

        } catch (SQLException e) {
            CaBug.drucke("003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        
        if (pDurchsuchenGaeste) {
            /**Gäste einlesen*/
            //@formatter:off
            lSql = "SELECT * " + "FROM " + dbBundle.getSchemaMandant()+"tbl_stimmkarten sk "
                    + "INNER JOIN " + dbBundle.getSchemaMandant()+"tbl_meldungen m ON sk.meldungsIdentGast=m.meldungsIdent "
                    + "LEFT JOIN " + dbBundle.getSchemaMandant()+"tbl_aktienregister are ON m.aktionaersnummer=are.aktionaersnummer and m.mandant=are.mandant "
                    + "LEFT JOIN " + dbBundle.getSchemaMandant()+ "tbl_personennatjur p on m.personenNatJurIdent=p.ident and m.mandant=p.mandant " 
                    + "WHERE ";
            //@formatter:on

            if (pDurchsuchenSammelkarten == false) {
                lSql = lSql + "m.meldungstyp!=2 AND ";
            }
            if (pDurchsuchenInSammelkarten == false) {
                lSql = lSql + "m.meldungstyp!=3 AND ";
            }
            lSql = lSql + "sk.stimmkarte=? AND m.mandant=?; ";

            try {
                lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                lPStm.setString(1, pSuchbegriff[0]);
                lPStm.setInt(2, dbBundle.clGlobalVar.mandant);

                ResultSet lErgebnis = lPStm.executeQuery();
                lErgebnis.last();
                anzInArray_Gaeste = lErgebnis.getRow();
                lErgebnis.beforeFirst();
                ergebnisArrayAktienregisterEintrag_Gaeste = new EclAktienregister[anzInArray_Gaeste];
                ergebnisArrayMeldung_Gaeste = new EclMeldung[anzInArray_Gaeste];
                ergebnisArrayStimmkarten_Gaeste=new EclStimmkarten[anzInArray_Gaeste];

                int i = 0;
                while (lErgebnis.next() == true) {
                    ergebnisArrayAktienregisterEintrag_Gaeste[i] = dbBundle.dbAktienregister.decodeErgebnis(lErgebnis);
                    ergebnisArrayMeldung_Gaeste[i] = dbBundle.dbMeldungen.decodeErgebnis(lErgebnis);
                    ergebnisArrayStimmkarten_Gaeste[i]=dbBundle.dbStimmkarten.decodeErgebnis(lErgebnis);
                    i++;
                }
                lErgebnis.close();
                lPStm.close();

            } catch (SQLException e) {
                CaBug.drucke("003");
                System.err.println(" " + e.getMessage());
                return (-1);
            }
            
        }
        
        /**Zusammenführen*/
        if (pDurchsuchenGaeste==false) {
            /*Nur Aktionäre*/
            ergebnisArrayAktienregisterEintrag=ergebnisArrayAktienregisterEintrag_Aktionaere;
            ergebnisArrayMeldung=ergebnisArrayMeldung_Aktionaere;
            ergebnisArrayStimmkarten=ergebnisArrayStimmkarten_Aktionaere;
        }
        else {
            /*Aktionäre und Gäste*/
            ergebnisArrayAktienregisterEintrag = new EclAktienregister[anzInArray_Aktionaere+anzInArray_Gaeste];
            ergebnisArrayMeldung = new EclMeldung[anzInArray_Aktionaere+anzInArray_Gaeste];
            ergebnisArrayStimmkarten=new EclStimmkarten[anzInArray_Aktionaere+anzInArray_Gaeste];
            
            for (int i=0;i<anzInArray_Aktionaere;i++) {
                ergebnisArrayAktienregisterEintrag[i]=ergebnisArrayAktienregisterEintrag_Aktionaere[i];
                ergebnisArrayMeldung[i]=ergebnisArrayMeldung_Aktionaere[i];
                ergebnisArrayStimmkarten[i]=ergebnisArrayStimmkarten_Aktionaere[i];
            }
            for (int i=0;i<anzInArray_Gaeste;i++) {
                ergebnisArrayAktienregisterEintrag[i+anzInArray_Aktionaere]=ergebnisArrayAktienregisterEintrag_Gaeste[i];
                ergebnisArrayMeldung[i+anzInArray_Aktionaere]=ergebnisArrayMeldung_Gaeste[i];
                ergebnisArrayStimmkarten[i+anzInArray_Aktionaere]=ergebnisArrayStimmkarten_Gaeste[i];
            }
        }
        return (anzInArray_Aktionaere+anzInArray_Gaeste);
    }

    /**
     * pSuchbegriffArt= 
     * > KonstSuchlaufSuchbegriffArt.nameAktionaerOderAktuellerVertreter (vorne und hinten %) 
     * > KonstSuchlaufSuchbegriffArt.nameAktionaer (vorne und hinten %)
     * > KonstSuchlaufSuchbegriffArt.meldeIdent (nur identisch)
     * 
     * falls 
     * pDurchsuchenGaeste=true: dann werden Aktionäre und Gäste durchsucht.
     * pDurchsuchenGaeste=false: dann werden nur Aktionäre durchsucht
     * pDursuchenNurGaeste=true: dann werden nur Gäste durchsucht.
     * 
     */
    public int read_suchenErstMeldungen(int pSuchbegriffArt, String[] pSuchbegriff, boolean pDurchsuchenSammelkarten,
            boolean pDurchsuchenInSammelkarten, boolean pDurchsuchenGaeste, boolean pDurchsuchenNurGaeste) {

        int anzInArray = 0;
        PreparedStatement lPStm = null;

        String lSql = "SELECT * " + "FROM " + dbBundle.getSchemaMandant() + "tbl_meldungen m " + "LEFT JOIN "
                + dbBundle.getSchemaMandant()
                + "tbl_aktienregister are ON m.aktionaersnummer=are.aktionaersnummer and m.mandant=are.mandant "
                + "LEFT JOIN " + dbBundle.getSchemaMandant()
                + "tbl_personennatjur p on m.personenNatJurIdent=p.ident and m.mandant=p.mandant " + "WHERE ";

        if (pDurchsuchenSammelkarten == false) {
            lSql = lSql + "meldungstyp!=2 AND ";
        }
        if (pDurchsuchenInSammelkarten == false) {
            lSql = lSql + "meldungstyp!=3 AND ";
        }
        if (pDurchsuchenGaeste == false) {
            lSql = lSql + "klasse!=0 AND ";
        }
        if (pDurchsuchenNurGaeste == true) {
            lSql = lSql + "klasse=0 AND ";
        }
        String lSuchbegriff = pSuchbegriff[0];
        switch (pSuchbegriffArt) {
        case KonstSuchlaufSuchbegriffArt.ekNummer:
            lSql = lSql + "m.zutrittsIdent=? AND m.mandant=?; ";
            break;
        case KonstSuchlaufSuchbegriffArt.skNummer:
            lSql = lSql + "m.stimmkarte=? AND m.mandant=?; ";
            break;
        case KonstSuchlaufSuchbegriffArt.nameAktionaer:
            lSql = lSql + "(p.name REGEXP ?) AND m.mandant=? ; ";
            lSuchbegriff = liefereSuchBegriff(pSuchbegriff);
            break;
        case KonstSuchlaufSuchbegriffArt.nameAktionaerOderAktuellerVertreter:
            lSql = lSql + "(p.name REGEXP ? OR m.vertreterName REGEXP ?) AND m.mandant=? ; ";
            lSuchbegriff = liefereSuchBegriff(pSuchbegriff);
            break;
        case KonstSuchlaufSuchbegriffArt.meldeIdent:
            lSql = lSql + "m.meldungsIdent=? AND m.mandant=?; ";
            break;
        case KonstSuchlaufSuchbegriffArt.sammelIdent:
            lSql = lSql + "m.meldungEnthaltenInSammelkarte=? AND m.mandant=?; ";
            break;
        }

        try {
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            switch (pSuchbegriffArt) {
            case KonstSuchlaufSuchbegriffArt.ekNummer:
                lPStm.setString(1, lSuchbegriff);
                lPStm.setInt(2, dbBundle.clGlobalVar.mandant);
                break;
            case KonstSuchlaufSuchbegriffArt.skNummer:
                lPStm.setString(1, lSuchbegriff);
                lPStm.setInt(2, dbBundle.clGlobalVar.mandant);
                break;
            case KonstSuchlaufSuchbegriffArt.nameAktionaerOderAktuellerVertreter:
                lPStm.setString(1, lSuchbegriff);
                lPStm.setString(2, lSuchbegriff);
                lPStm.setInt(3, dbBundle.clGlobalVar.mandant);
                break;
            case KonstSuchlaufSuchbegriffArt.nameAktionaer:
                lPStm.setString(1, lSuchbegriff);
                lPStm.setInt(2, dbBundle.clGlobalVar.mandant);
                break;
            case KonstSuchlaufSuchbegriffArt.meldeIdent:
                lPStm.setInt(1, Integer.parseInt(lSuchbegriff));
                lPStm.setInt(2, dbBundle.clGlobalVar.mandant);
                break;
            case KonstSuchlaufSuchbegriffArt.sammelIdent:
                lPStm.setInt(1, Integer.parseInt(lSuchbegriff));
                lPStm.setInt(2, dbBundle.clGlobalVar.mandant);
                break;
            }

            ResultSet lErgebnis = lPStm.executeQuery();
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();
            ergebnisArrayAktienregisterEintrag = new EclAktienregister[anzInArray];
            ergebnisArrayMeldung = new EclMeldung[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {
                ergebnisArrayAktienregisterEintrag[i] = dbBundle.dbAktienregister.decodeErgebnis(lErgebnis);
                ergebnisArrayMeldung[i] = dbBundle.dbMeldungen.decodeErgebnis(lErgebnis);
                i++;
            }
            lErgebnis.close();
            lPStm.close();

        } catch (SQLException e) {
            CaBug.drucke("DbJoined.read_suchenErstMeldungen 003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    /** Zum jeweiligen Satz gefundener Vertreter steht in ergebnisArrayKey[] */
    public int read_suchenErstVertreter(String[] pSuchbegriff, boolean pDurchsuchenSammelkarten,
            boolean pDurchsuchenInSammelkarten) {

        int anzInArray = 0;
        PreparedStatement lPStm = null;

        String lSql = "SELECT * " + "FROM " + dbBundle.getSchemaMandant() + "tbl_personenNatJur p1 " + "INNER JOIN "
                + dbBundle.getSchemaMandant()
                + "tbl_willenserklaerung w ON p1.ident=w.bevollmaechtigterDritterIdent AND p1.mandant=w.mandant "
                + "INNER JOIN " + dbBundle.getSchemaMandant()
                + "tbl_meldungen m ON w.meldungsIdent=m.meldungsIdent AND m.mandant=w.mandant " + "INNER JOIN "
                + dbBundle.getSchemaMandant()
                + "tbl_aktienregister are ON m.aktionaersnummer=are.aktionaersnummer AND m.mandant=are.mandant "
                + "INNER JOIN " + dbBundle.getSchemaMandant()
                + "tbl_personennatjur p on m.personenNatJurIdent=p.ident and m.mandant=p.mandant "
                + "WHERE p1.name REGEXP ? " + "AND p1.mandant=? ";

        if (pDurchsuchenSammelkarten == false) {
            lSql = lSql + "AND m.meldungstyp!=2 ";
        }
        if (pDurchsuchenInSammelkarten == false) {
            lSql = lSql + "AND meldungstyp!=3 ";
        }
        lSql = lSql + "ORDER BY p1.name ";

        String lSuchbegriff = "";
        for (int i = 0; i < pSuchbegriff.length; i++) {
            if (i > 0) {
                lSuchbegriff = lSuchbegriff + "|";
            }
            lSuchbegriff = lSuchbegriff + pSuchbegriff[i];
        }

        try {
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setString(1, lSuchbegriff);
            lPStm.setInt(2, dbBundle.clGlobalVar.mandant);

            ResultSet lErgebnis = lPStm.executeQuery();
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();
            ergebnisArrayAktienregisterEintrag = new EclAktienregister[anzInArray];
            ergebnisArrayMeldung = new EclMeldung[anzInArray];
            ergebnisArrayKey = new String[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {
                ergebnisArrayAktienregisterEintrag[i] = dbBundle.dbAktienregister.decodeErgebnis(lErgebnis);
                ergebnisArrayMeldung[i] = dbBundle.dbMeldungen.decodeErgebnis(lErgebnis);
                ergebnisArrayKey[i] = lErgebnis.getString("p1.name") + " " + lErgebnis.getString("p1.vorname") + ", "
                        + lErgebnis.getString("p1.ort");
                i++;
            }
            lErgebnis.close();
            lPStm.close();

        } catch (SQLException e) {
            CaBug.drucke("DbJoined.read_suchenErstVertreter 003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    /** Zum jeweiligen Satz gefundener Vertreter steht in ergebnisArrayKey[] */
    public int read_suchenErstUngepruefteVertreter(String[] pSuchbegriff, boolean pDurchsuchenSammelkarten,
            boolean pDurchsuchenInSammelkarten) {

        int anzInArray = 0;
        PreparedStatement lPStm = null;
     // @formatter:off
        String lSql = 
                "SELECT * FROM "+ dbBundle.getSchemaMandant() + "tbl_aktienregisterErgaenzung areerg "
                + "INNER JOIN "+ dbBundle.getSchemaMandant()+"tbl_aktienregister are ON areerg.aktienregisterIdent=are.aktienregisterIdent "
                + "INNER JOIN " + dbBundle.getSchemaMandant() + "tbl_meldungen m ON are.aktienregisterIdent=m.aktienregisterIdent " 
                + "INNER JOIN " + dbBundle.getSchemaMandant() + "tbl_personennatjur p on m.personenNatJurIdent=p.ident "
                + "WHERE areerg.ergaenzungLangString0 REGEXP ? AND (m.zusatzfeld1='-1' OR m.zusatzfeld1='-2') ";
     // @formatter:on
        
        if (pDurchsuchenSammelkarten == false) {
            lSql = lSql + "AND m.meldungstyp!=2 ";
        }
        if (pDurchsuchenInSammelkarten == false) {
            lSql = lSql + "AND meldungstyp!=3 ";
        }
        lSql = lSql + "ORDER BY areerg.ergaenzungLangString0 ";

        String lSuchbegriff = "";
        for (int i = 0; i < pSuchbegriff.length; i++) {
            if (i > 0) {
                lSuchbegriff = lSuchbegriff + "|";
            }
            lSuchbegriff = lSuchbegriff + pSuchbegriff[i];
        }

        try {
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setString(1, lSuchbegriff);

            ResultSet lErgebnis = lPStm.executeQuery();
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();
            ergebnisArrayAktienregisterEintrag = new EclAktienregister[anzInArray];
            ergebnisArrayMeldung = new EclMeldung[anzInArray];
            ergebnisArrayKey = new String[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {
                ergebnisArrayAktienregisterEintrag[i] = dbBundle.dbAktienregister.decodeErgebnis(lErgebnis);
                ergebnisArrayMeldung[i] = dbBundle.dbMeldungen.decodeErgebnis(lErgebnis);
                ergebnisArrayKey[i] = lErgebnis.getString("areerg.ergaenzungLangString0") + ", " + lErgebnis.getString("areerg.ergaenzungLangString27");
                i++;
            }
            lErgebnis.close();
            lPStm.close();

        } catch (SQLException e) {
            CaBug.drucke("003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    /* ++++++++++++++++++++++Für InstiBestandsZuordnung+++++++++++++++++++ */
    public int read_instiBestandsZuordnung_Aktienregister(int pInstiIdent) {

        int anzInArray = 0;
        PreparedStatement lPStm = null;

        String lSql = "SELECT * " + "FROM " + dbBundle.getSchemaMandant() + "tbl_instiBestandsZuordnung inbz "
                + "LEFT JOIN " + dbBundle.getSchemaMandant()
                + "tbl_aktienregister are ON inbz.identAktienregister=are.aktienregisterIdent and inbz.mandant=are.mandant "
                + "LEFT JOIN " + dbBundle.getSchemaAllgemein()
                + "tbl_userLogin ul ON inbz.identUserLogin=ul.userLoginIdent and inbz.mandant=ul.mandant ";

        lSql = lSql + "WHERE inbz.identInsti=? AND inbz.zugeordnetRegisterOderMeldungen=1 AND inbz.mandant=?; ";

        try {
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, pInstiIdent);
            lPStm.setInt(2, dbBundle.clGlobalVar.mandant);

            ResultSet lErgebnis = lPStm.executeQuery();
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();
            ergebnisInstiBestandsZuordnung = new EclInstiBestandsZuordnung[anzInArray];
            ergebnisArrayAktienregisterEintrag = new EclAktienregister[anzInArray];
            ergebnisArrayUserLogin = new EclUserLogin[anzInArray];
            ergebnisArrayMeldung = new EclMeldung[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {
                ergebnisInstiBestandsZuordnung[i] = dbBundle.dbInstiBestandsZuordnung.decodeErgebnis(lErgebnis);
                ergebnisArrayAktienregisterEintrag[i] = dbBundle.dbAktienregister.decodeErgebnis(lErgebnis);
                ergebnisArrayUserLogin[i] = dbBundle.dbUserLogin.decodeErgebnis(lErgebnis);
                ergebnisArrayMeldung[i] = new EclMeldung();
                i++;
            }
            lErgebnis.close();
            lPStm.close();

        } catch (SQLException e) {
            CaBug.drucke("DbJoined.read_instiBestandsZuordnung_Aktienregister 003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    public int read_instiBestandsZuordnung_Meldungen(int pInstiIdent) {

        int anzInArray = 0;
        PreparedStatement lPStm = null;

        String lSql = "SELECT * " + "FROM " + dbBundle.getSchemaMandant() + "tbl_instiBestandsZuordnung inbz "
                + "LEFT JOIN " + dbBundle.getSchemaMandant()
                + "tbl_meldungen m ON inbz.identMeldung=m.meldungsIdent and inbz.mandant=m.mandant " + "LEFT JOIN "
                + dbBundle.getSchemaAllgemein()
                + "tbl_userLogin ul ON inbz.identUserLogin=ul.userLoginIdent and inbz.mandant=ul.mandant "
                + "INNER JOIN " + dbBundle.getSchemaMandant()
                + "tbl_personennatjur p on m.personenNatJurIdent=p.ident and m.mandant=p.mandant ";

        lSql = lSql + "WHERE inbz.identInsti=? AND inbz.zugeordnetRegisterOderMeldungen=2 AND inbz.mandant=?; ";

        try {
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, pInstiIdent);
            lPStm.setInt(2, dbBundle.clGlobalVar.mandant);

            ResultSet lErgebnis = lPStm.executeQuery();
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();
            ergebnisInstiBestandsZuordnung = new EclInstiBestandsZuordnung[anzInArray];
            ergebnisArrayAktienregisterEintrag = new EclAktienregister[anzInArray];
            ergebnisArrayUserLogin = new EclUserLogin[anzInArray];
            ergebnisArrayMeldung = new EclMeldung[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {
                ergebnisInstiBestandsZuordnung[i] = dbBundle.dbInstiBestandsZuordnung.decodeErgebnis(lErgebnis);
                ergebnisArrayAktienregisterEintrag[i] = new EclAktienregister();
                ergebnisArrayUserLogin[i] = dbBundle.dbUserLogin.decodeErgebnis(lErgebnis);
                ergebnisArrayMeldung[i] = dbBundle.dbMeldungen.decodeErgebnis(lErgebnis);
                i++;
            }
            lErgebnis.close();
            lPStm.close();

        } catch (SQLException e) {
            CaBug.drucke("DbJoined.read_instiBestandsZuordnung_Aktienregister 003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

}
