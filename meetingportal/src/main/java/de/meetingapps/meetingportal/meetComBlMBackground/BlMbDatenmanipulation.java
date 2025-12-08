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
package de.meetingapps.meetingportal.meetComBlMBackground;

import java.util.concurrent.Future;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComBVerwaltung.BvDatenbank;
import de.meetingapps.meetingportal.meetComBVerwaltung.BvRuecksetzenUndInitialisieren;
import de.meetingapps.meetingportal.meetComBVerwaltung.BvTestdaten;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclLoginDatenDemo;
import de.meetingapps.meetingportal.meetComStub.StubMandantAnlegen;
import jakarta.ejb.AsyncResult;
import jakarta.ejb.Asynchronous;
import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionManagement;
import jakarta.ejb.TransactionManagementType;

@Stateless
@TransactionManagement(value=TransactionManagementType.BEAN)
@LocalBean
public class BlMbDatenmanipulation {

    private int logDrucken=3;

    @Asynchronous
    public Future<String> loescheMeldebestandHV(DbBundle pDbBundle) {
        CaBug.druckeLog("Job-Anfang", logDrucken, 3);

        CaBug.druckeLog("A", logDrucken, 10);

        pDbBundle.openEE();
        CaBug.druckeLog("B", logDrucken, 10);

        BvRuecksetzenUndInitialisieren bvRuecksetzenUndInitialisieren=new BvRuecksetzenUndInitialisieren(pDbBundle);
        bvRuecksetzenUndInitialisieren.ruecksetzenMeldebestandUndHV();

        pDbBundle.closeEEAll();

        CaBug.druckeLog("Job-Ende", logDrucken, 3);


        AsyncResult<String> asyncResultString=new AsyncResult<String>("OK");
        return asyncResultString;
    }

    @Asynchronous
    public Future<String> loescheAktienregister(DbBundle pDbBundle) {
        CaBug.druckeLog("Job-Anfang", logDrucken, 3);

        CaBug.druckeLog("A", logDrucken, 10);

        pDbBundle.openEE();
        pDbBundle.openWeitere();
        CaBug.druckeLog("B", logDrucken, 10);

        BvRuecksetzenUndInitialisieren bvRuecksetzenUndInitialisieren=new BvRuecksetzenUndInitialisieren(pDbBundle);
        bvRuecksetzenUndInitialisieren.loeschenAktienregister();

        pDbBundle.closeEEAll();

        CaBug.druckeLog("Job-Ende", logDrucken, 3);


        AsyncResult<String> asyncResultString=new AsyncResult<String>("OK");
        return asyncResultString;
    }


    @Asynchronous
    public Future<String> zuruecksetzenMandant(DbBundle pDbBundle) {
        CaBug.druckeLog("Job-Anfang", logDrucken, 3);

        CaBug.druckeLog("A", logDrucken, 10);

        pDbBundle.openEE();
        pDbBundle.openWeitere();
        CaBug.druckeLog("B", logDrucken, 10);

        BvDatenbank bvDatenbank=new BvDatenbank(pDbBundle);
        bvDatenbank.deleteAllMandantenTablesInhalte();

        pDbBundle.closeEEAll();

        CaBug.druckeLog("Job-Ende", logDrucken, 3);


        AsyncResult<String> asyncResultString=new AsyncResult<String>("OK");
        return asyncResultString;
    }

    @Asynchronous
    public Future<String> loescheAbstimmungsagenda(DbBundle pDbBundle) {
        CaBug.druckeLog("Job-Anfang", logDrucken, 3);

        CaBug.druckeLog("A", logDrucken, 10);

        pDbBundle.openEE();
        pDbBundle.openWeitere();
        CaBug.druckeLog("B", logDrucken, 10);

        BvRuecksetzenUndInitialisieren bvRuecksetzenUndInitialisieren=new BvRuecksetzenUndInitialisieren(pDbBundle);
        bvRuecksetzenUndInitialisieren.loeschenAgenda();

        pDbBundle.closeEEAll();

        CaBug.druckeLog("Job-Ende", logDrucken, 3);


        AsyncResult<String> asyncResultString=new AsyncResult<String>("OK");
        return asyncResultString;
    }

    @Asynchronous
    public Future<String> zuruecksetzenHinweiseGelesen(DbBundle pDbBundle, boolean hinweisAktionaersPortalBestaetigtZuruecksetzen, boolean hinweisHVPortalBestaetigtZuruecksetzen,
            int hinweisWeitereBestaetigtZuruecksetzen) {
        CaBug.druckeLog("Job-Anfang", logDrucken, 3);

        CaBug.druckeLog("A", logDrucken, 10);

        pDbBundle.openEE();
        pDbBundle.openWeitere();
        CaBug.druckeLog("B", logDrucken, 10);

        BvRuecksetzenUndInitialisieren bvRuecksetzenUndInitialisieren=new BvRuecksetzenUndInitialisieren(pDbBundle);
        if (hinweisAktionaersPortalBestaetigtZuruecksetzen) {
            CaBug.druckeLog("hinweisAktionaersPortalBestaetigtZuruecksetzen", logDrucken, 3);
            bvRuecksetzenUndInitialisieren.ruecksetzenHinweisAktionaersPortalBestaetigt();
        }
        if (hinweisHVPortalBestaetigtZuruecksetzen) {
            CaBug.druckeLog("hinweisHVPortalBestaetigtZuruecksetzen", logDrucken, 3);
            bvRuecksetzenUndInitialisieren.ruecksetzenHinweisHVPortalBestaetigt();
        }
        if (hinweisWeitereBestaetigtZuruecksetzen!=0) {
            CaBug.druckeLog("hinweisWeitereBestaetigtZuruecksetzen="+hinweisWeitereBestaetigtZuruecksetzen, logDrucken, 3);
            bvRuecksetzenUndInitialisieren.ruecksetzenHinweisWeitereBestaetigg(hinweisWeitereBestaetigtZuruecksetzen);
        }

        pDbBundle.closeEEAll();

        CaBug.druckeLog("Job-Ende", logDrucken, 3);


        AsyncResult<String> asyncResultString=new AsyncResult<String>("OK");
        return asyncResultString;
    }

    @Asynchronous
    public Future<String> zuruecksetzenHinweisElekVersand(DbBundle pDbBundle) {
        CaBug.druckeLog("Job-Anfang", logDrucken, 3);

        CaBug.druckeLog("A", logDrucken, 10);

        pDbBundle.openEE();
        pDbBundle.openWeitere();
        CaBug.druckeLog("B", logDrucken, 10);

        BvRuecksetzenUndInitialisieren bvRuecksetzenUndInitialisieren=new BvRuecksetzenUndInitialisieren(pDbBundle);
        bvRuecksetzenUndInitialisieren.ruecksetzenNeuAbfrageElektronischerEinladungsversand();

        pDbBundle.closeEEAll();

        CaBug.druckeLog("Job-Ende", logDrucken, 3);


        AsyncResult<String> asyncResultString=new AsyncResult<String>("OK");
        return asyncResultString;
    }

    @Asynchronous
    public Future<String> zuruecksetzenPasswort(DbBundle pDbBundle, String pPasswort) {
        CaBug.druckeLog("Job-Anfang", logDrucken, 3);

        CaBug.druckeLog("A", logDrucken, 10);

        pDbBundle.openEE();
        pDbBundle.openWeitere();
        CaBug.druckeLog("B", logDrucken, 10);

        BvRuecksetzenUndInitialisieren bvRuecksetzenUndInitialisieren=new BvRuecksetzenUndInitialisieren(pDbBundle);
        bvRuecksetzenUndInitialisieren.setzenPasswortInitial(pPasswort);

        pDbBundle.closeEEAll();

        CaBug.druckeLog("Job-Ende", logDrucken, 3);


        AsyncResult<String> asyncResultString=new AsyncResult<String>("OK");
        return asyncResultString;
    }


    @Asynchronous
    public Future<String> zusaetzlichesInitialpasswort(DbBundle pDbBundle) {

        CaBug.druckeLog("Job-Anfang", logDrucken, 3);

        CaBug.druckeLog("A", logDrucken, 10);

        pDbBundle.openEE();
        pDbBundle.openWeitere();
        CaBug.druckeLog("B", logDrucken, 10);

        BvTestdaten bvTestdaten=new BvTestdaten();
        bvTestdaten.zusaetzlichesInitialpasswort(pDbBundle);

        pDbBundle.closeEEAll();

        CaBug.druckeLog("Job-Ende", logDrucken, 3);


        AsyncResult<String> asyncResultString=new AsyncResult<String>("OK");
        return asyncResultString;
    }

    @Asynchronous
    public Future<String> aktionaereAnmelden(DbBundle pDbBundle, boolean pMitEK, boolean pEkWieAktionaersnummer, boolean pEkWieAktionaersnummerOhneLetzteZiffer) {

        CaBug.druckeLog("Job-Anfang", logDrucken, 3);

        CaBug.druckeLog("A", logDrucken, 10);

        pDbBundle.openEE();
        pDbBundle.openWeitere();
        CaBug.druckeLog("B", logDrucken, 10);

        BvTestdaten bvTestdaten=new BvTestdaten();
        bvTestdaten.anmeldenNichtAngemeldete(pDbBundle, pMitEK, pEkWieAktionaersnummer, pEkWieAktionaersnummerOhneLetzteZiffer);

        pDbBundle.closeEEAll();

        CaBug.druckeLog("Job-Ende", logDrucken, 3);


        AsyncResult<String> asyncResultString=new AsyncResult<String>("OK");
        return asyncResultString;
    }

    @Asynchronous
    public Future<String> ku217VertreterVorbereiten(DbBundle pDbBundle) {

        CaBug.druckeLog("Job-Anfang", logDrucken, 3);

        CaBug.druckeLog("A", logDrucken, 10);

        pDbBundle.openEE();
        pDbBundle.openWeitere();
        CaBug.druckeLog("B", logDrucken, 10);

        BvTestdaten bvTestdaten=new BvTestdaten();
        bvTestdaten.ku217VertreterVorbereiten(pDbBundle);

        pDbBundle.closeEEAll();

        CaBug.druckeLog("Job-Ende", logDrucken, 3);


        AsyncResult<String> asyncResultString=new AsyncResult<String>("OK");
        return asyncResultString;
    }

    @Asynchronous
    public Future<String> ku217AbstimmendePraesentSetzen(DbBundle pDbBundle, int pAbstimmvorgang) {

        CaBug.druckeLog("Job-Anfang", logDrucken, 3);

        CaBug.druckeLog("A", logDrucken, 10);

        pDbBundle.openEE();
        pDbBundle.openWeitere();
        CaBug.druckeLog("B", logDrucken, 10);

        BvTestdaten bvTestdaten=new BvTestdaten();
        bvTestdaten.ku217AbstimmendePraesentSetzen(pDbBundle, pAbstimmvorgang);

        pDbBundle.closeEEAll();

        CaBug.druckeLog("Job-Ende", logDrucken, 3);


        AsyncResult<String> asyncResultString=new AsyncResult<String>("OK");
        return asyncResultString;
    }



    @Asynchronous
    public Future<String> testBestandAnlegen(DbBundle pDbBundle, String pPasswort,
            int pAnzahlAktionaereJeGattung,
            boolean pGruppenVerteilen, boolean pErgaenzungAnlegen) {

        CaBug.druckeLog("Job-Anfang", logDrucken, 3);

        CaBug.druckeLog("A", logDrucken, 10);

        pDbBundle.openEE();
        pDbBundle.openWeitere();
        CaBug.druckeLog("B", logDrucken, 10);

        BvTestdaten bvTestdaten=new BvTestdaten();
        bvTestdaten.anzahlAnlegenJeGattung=pAnzahlAktionaereJeGattung;
        bvTestdaten.ergaenzungAnlegen=pErgaenzungAnlegen;
        bvTestdaten.gruppenVerteilen=pGruppenVerteilen;
        bvTestdaten.passwortVergeben=pPasswort;

        bvTestdaten.anlegenBestand(pDbBundle);

        pDbBundle.closeEEAll();

        CaBug.druckeLog("Job-Ende", logDrucken, 3);


        AsyncResult<String> asyncResultString=new AsyncResult<String>("OK");
        return asyncResultString;
    }


    @Asynchronous
    public Future<String> sammelkartenAnlegen(DbBundle pDbBundle) {

        CaBug.druckeLog("Job-Anfang", logDrucken, 3);

        CaBug.druckeLog("A", logDrucken, 10);

        pDbBundle.openEE();
        pDbBundle.openWeitere();
        CaBug.druckeLog("B", logDrucken, 10);

        StubMandantAnlegen stubMandantAnlegen = new StubMandantAnlegen(true, pDbBundle);
        stubMandantAnlegen.sammelkartenSandardAnlegen();

        pDbBundle.closeEEAll();

        CaBug.druckeLog("Job-Ende", logDrucken, 3);


        AsyncResult<String> asyncResultString=new AsyncResult<String>("OK");
        return asyncResultString;
    }

    @Asynchronous
    public Future<String> mandantenAktionaersdatenLoeschen(DbBundle pDbBundle, String pDatum) {

        CaBug.druckeLog("Job-Anfang", logDrucken, 3);

        CaBug.druckeLog("A", logDrucken, 10);

        pDbBundle.openEE();
        pDbBundle.openWeitere();
        CaBug.druckeLog("B", logDrucken, 10);

        BvRuecksetzenUndInitialisieren bvRuecksetzenUndInitialisieren=new BvRuecksetzenUndInitialisieren(pDbBundle);
        bvRuecksetzenUndInitialisieren.mandantenAktionaersdatenLoeschen(pDatum);

        pDbBundle.closeEEAll();

        CaBug.druckeLog("Job-Ende", logDrucken, 3);


        AsyncResult<String> asyncResultString=new AsyncResult<String>("OK");
        return asyncResultString;
    }

    @Asynchronous
    public Future<String> demoAktionaereZuruecksetzen(DbBundle pDbBundle) {

        CaBug.druckeLog("Job-Anfang", logDrucken, 3);

        CaBug.druckeLog("A", logDrucken, 10);

        pDbBundle.openEE();
        pDbBundle.openWeitere();
        CaBug.druckeLog("B", logDrucken, 10);

        /**Passwörter zurücksetzen*/
        BvRuecksetzenUndInitialisieren bvRuecksetzenUndInitialisieren=new BvRuecksetzenUndInitialisieren(pDbBundle);
        bvRuecksetzenUndInitialisieren.setzenPasswortInitialDemo();

        /**Bestätigungen zurücksetzen*/
        int rc=pDbBundle.dbLoginDatenDemo.readAll();
        if (rc<0) {
            CaBug.drucke("001");
        }
        int anz=pDbBundle.dbLoginDatenDemo.anzErgebnis();
        for (int i=0;i<anz; i++) {
            EclLoginDatenDemo lLoginDatenDemo=pDbBundle.dbLoginDatenDemo.ergebnisPosition(i);

            /*LoginDaten*/
            rc=pDbBundle.dbLoginDaten.updateSetzeHinweisAktionaersPortalBestaetigtAuf0(lLoginDatenDemo.loginKennung);
            if (rc<0) {
                CaBug.drucke("002");
            }

            rc=pDbBundle.dbLoginDaten.updateSetzeHinweisHVPortalBestaetigtAuf0(lLoginDatenDemo.loginKennung);
            if (rc<0) {
                CaBug.drucke("003");
            }

            rc=pDbBundle.dbLoginDaten.updateSetzeHinweisWeitereBestaetigtAuf0(lLoginDatenDemo.loginKennung, 3);
            if (rc<0) {
                CaBug.drucke("004");
            }

            rc=pDbBundle.dbLoginDaten.updateEmailRegistrierung0(lLoginDatenDemo.loginKennung);
            if (rc<0) {
                CaBug.drucke("004");
            }

            /*AKtienregisterErgaenzung. Keine Fehlerbehandlung, da möglicherweise Satz nicht angelegt
             * (ist ja sogar Standard)
             */
            rc=pDbBundle.dbAktienregister.leseZuAktienregisternummer(lLoginDatenDemo.loginKennung);
            if (rc>0) {
               int aktienregisterIdent=pDbBundle.dbAktienregister.ergebnisPosition(0).aktienregisterIdent;
               pDbBundle.dbAktienregisterErgaenzung.setzeZurueck(aktienregisterIdent);
            }
            else {
                CaBug.drucke("005");
            }

            /*Aufträge löschen*/
            rc=pDbBundle.dbLoginDaten.read_loginKennung(lLoginDatenDemo.loginKennung);
            if (rc>0) {
                pDbBundle.dbAuftrag.deleteBenutzer(pDbBundle.dbLoginDaten.ergebnisPosition(0).ident*(-1));
             }
             else {
                 CaBug.drucke("006");
             }

        }

        pDbBundle.closeEEAll();

        CaBug.druckeLog("Job-Ende", logDrucken, 3);


        AsyncResult<String> asyncResultString=new AsyncResult<String>("OK");
        return asyncResultString;
    }

    @Asynchronous
    public Future<String> mandantennummerAendern(DbBundle pDbBundle) {

        CaBug.druckeLog("Job-Anfang", logDrucken, 3);

        CaBug.druckeLog("A", logDrucken, 10);

        pDbBundle.openEE();
        pDbBundle.openWeitere();
        CaBug.druckeLog("B", logDrucken, 10);

        /**Mandantennummer ändern*/
        BvDatenbank bvDatenbank = new BvDatenbank(pDbBundle);
        bvDatenbank.updateAllMandantenTablesMandantenNummer();

        pDbBundle.closeEEAll();

        CaBug.druckeLog("Job-Ende", logDrucken, 3);

        AsyncResult<String> asyncResultString=new AsyncResult<String>("OK");
        return asyncResultString;
    }

}
