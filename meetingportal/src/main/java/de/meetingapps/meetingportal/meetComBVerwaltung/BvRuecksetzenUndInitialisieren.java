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
package de.meetingapps.meetingportal.meetComBVerwaltung;

import java.util.List;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaDatumZeit;
import de.meetingapps.meetingportal.meetComAllg.CaString;
import de.meetingapps.meetingportal.meetComBl.BlSammelkarten;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclEmittenten;
import de.meetingapps.meetingportal.meetComEntities.EclInsti;
import de.meetingapps.meetingportal.meetComEntities.EclMeldung;
import de.meetingapps.meetingportal.meetComKonst.KonstPortalFunktionen;
import de.meetingapps.meetingportal.meetComKonst.KonstSkIst;

/**Nicht Stub-Fähig!*/
public class BvRuecksetzenUndInitialisieren {

    private DbBundle dbBundle = null;

    public BvRuecksetzenUndInitialisieren(DbBundle pDbBundle) {
        dbBundle = pDbBundle;

    }

    /**Aktienregister bleibt grundsätzlich erhalten, aber für alle Aktionäre
     * wird gesetzt, dass sie beim nächsten Portallogin Disclaimer etc. wieder bestätigen müssen
     * (hinweisHVPortalBestaetigt=0, hinweisAktionaersPortalBestaetigt=0)
     */
    public void ruecksetzenHinweisAktionaersPortalBestaetigt() {
        dbBundle.dbLoginDaten.updateSetzeHinweisAktionaersPortalBestaetigtAuf0();
    }
    
    public void ruecksetzenHinweisHVPortalBestaetigt() {
        dbBundle.dbLoginDaten.updateSetzeHinweisHVPortalBestaetigtAuf0();
    }


    /**Aktienregister bleibt grundsätzlich erhalten, aber für alle Aktionäre
     * die dem elektronischen Einladungsversand noch nicht zugestimmt haben, wird
     * gesetzt dass dieser erneut abgefragt werden soll
     */
    public void ruecksetzenNeuAbfrageElektronischerEinladungsversand() {
        dbBundle.dbLoginDaten.updateSetzeEMailRegistrierungAufErneutAbfragen();
    }

    /**Aktienregister bleibt grundsätzlich erhalten, aber für alle Aktionäre
     * wird gesetzt, dass sie beim nächsten Portallogin die sonstigen Hinweise wieder bestätigen müssen.
     * In pRuecksetzen sind dabei die Bits zu übergeben, die auf 0 gesetzt werden sollen! Also z.B.
     * pRuecksetzen=3 => die Bits für 1 und 2 werden zurückgesetzt 
     *
     */
    public void ruecksetzenHinweisWeitereBestaetigg(int pRuecksetzen) {
        dbBundle.dbLoginDaten.updateSetzeHinweisWeitereBestaetigtAuf0(pRuecksetzen);
    }

    
    /**Erfordert DBBundle-Weitere*/
    public void setzenPasswortInitial(String pPasswort) {
        /*Übergebenes Passwort für alle setzen*/
        dbBundle.dbLoginDaten.resetPasswort(pPasswort);
        
        /*Nun noch Demo-Passwörter setzen*/
        setzenPasswortInitialDemo();
    }
    
    /**Erfordert DBBundle-Weitere*/
    public void setzenPasswortInitialDemo() {
        int rc=dbBundle.dbLoginDatenDemo.readAll();
        if (rc<0) {
            CaBug.drucke("001");
        }
        int anz=dbBundle.dbLoginDatenDemo.anzErgebnis();
        for (int i=0;i<anz; i++) {
            rc=dbBundle.dbLoginDaten.resetPasswortDemo(dbBundle.dbLoginDatenDemo.ergebnisPosition(i));
            if (rc<0) {
                CaBug.drucke("002");
            }
        }
    }
    
    /**Loeschen "rund um den Meldebestand und zugehöriger HV-Daten -d.h.:
     * Meldebestand selbst, Weisungen, Sammelkarten,Präsenzdaten, Abstimmungsverhalten.
     */
    public void ruecksetzenMeldebestandUndHV() {
        dbBundle.openWeitere();

        /*Abstimmungsdaten*/
        dbBundle.dbAbstimmungMeldung.deleteAll();
        dbBundle.dbAbstimmungMeldungRaw.deleteAll();
        dbBundle.dbAbstimmungMeldungSperre.deleteAll();
        dbBundle.dbAbstimmungMeldungSplit.deleteAll();

        /*Präsenz*/
        dbBundle.dbPraesenzliste.deleteAll();
        dbBundle.dbHVDatenLfd.deleteAll();
        dbBundle.dbTeilnehmerStandVerein.deleteAll();

        /*Meldebestand*/
        dbBundle.dbAktienregister.deleteMeldeEintraege();
        dbBundle.dbMeldungen.deleteAll();
        dbBundle.dbMeldungAusstellungsgrund.deleteAll();
        dbBundle.dbMeldungenMeldungen.deleteAll();
        dbBundle.dbMeldungVirtuellePraesenz.deleteAll();
        dbBundle.dbMeldungVipKZ.deleteAll();
        dbBundle.dbMeldungenVipKZAusgeblendet.deleteAll();
        dbBundle.dbMeldungZuSammelkarte.deleteAll();
        dbBundle.dbPersonenNatJur.deleteAll();
        dbBundle.dbPersonenNatJurVersandadresse.deleteAll();

        
        dbBundle.dbLoginDaten.deleteSKennungen();//Alle Zugangsdaten für Gäste oder Vertreter löschen
        dbBundle.dbLoginDaten.update_alleUserResetWortmeldeablauf(); //Wortmeldeablauf für alle User zurücksetzen
        
        dbBundle.dbZutrittskarten.deleteAll();
        dbBundle.dbStimmkarten.deleteAll();
        dbBundle.dbStimmkartenSecond.deleteAll();

        dbBundle.dbAenderungslog.deleteAll();
        dbBundle.dbAbstimmungsVorschlag.deleteAll();

        dbBundle.dbWeisungMeldung.deleteAll();

        dbBundle.dbWillenserklaerung.deleteAll();
        dbBundle.dbWillenserklaerungZusatz.deleteAll();

        dbBundle.dbAbstimmungenEinzelAusschluss.deleteAll();

        dbBundle.dbDrucklauf.deleteAll();

        dbBundle.dbScan.deleteAll();

        /*Workflow, vorläufige Vollmachten*/
        dbBundle.dbBestWorkflow.deleteAll();
        dbBundle.dbVorlaeufigeVollmacht.deleteAll();
        dbBundle.dbVorlaeufigeVollmachtEingabe.deleteAll();

        /*Virtuelle HV*/
        dbBundle.dbMitteilung.setzeFunktion(KonstPortalFunktionen.fragen);
        dbBundle.dbMitteilung.deleteAll();
        dbBundle.dbMitteilung.setzeFunktion(KonstPortalFunktionen.wortmeldungen);
        dbBundle.dbMitteilung.deleteAll();
        dbBundle.dbMitteilung.setzeFunktion(KonstPortalFunktionen.widersprueche);
        dbBundle.dbMitteilung.deleteAll();
        dbBundle.dbMitteilung.setzeFunktion(KonstPortalFunktionen.antraege);
        dbBundle.dbMitteilung.deleteAll();
        dbBundle.dbMitteilung.setzeFunktion(KonstPortalFunktionen.sonstigeMitteilungen);
        dbBundle.dbMitteilung.deleteAll();
        dbBundle.dbMitteilung.setzeFunktion(KonstPortalFunktionen.botschaftenEinreichen);
        dbBundle.dbMitteilung.deleteAll();

        dbBundle.dbMitteilungBestand.deleteAll();
        
        dbBundle.dbInfo.deleteAll();
        dbBundle.dbWortmeldetischProtokoll.deleteAll();

        /*Übergreifende Datenbanken*/
        dbBundle.dbTablet.deleteAll();

        /*Nicht berücksichtigt, da unklar wofür, bzw. ob überhaupt noch verwendet / existent:
         * tbl_personen
         * tbl_personenwar
         * tbl_praesenz
         * tbl_projekte
         * tbl_rkabrechnungen
         * tbl_rktage
         */

    }

    /*Löschen von Aktienregister, Login-Daten, Zusatz*/
    public void loeschenAktienregister() {
        dbBundle.dbAktienregister.deleteAll();
        dbBundle.dbAktienregister.deleteAll_aktienregisterHistorie();
        dbBundle.dbAktienregisterErgaenzung.deleteAll();
        dbBundle.dbSuchlaufErgebnis.deleteAll();
        dbBundle.dbInstiBestandsZuordnung.deleteAll();
        dbBundle.dbLoginDaten.deleteAll();
        dbBundle.dbZuordnungKennung.deleteAll();
        dbBundle.dbInhaberImportAnmeldedaten.deleteAll();
        dbBundle.dbImportProtokoll.deleteAll();
        dbBundle.dbEindeutigeKennung.setzeVerwendetZurueck();
    }

    /**Löschen der eigentlichen Agenda und alles was dazugehört, also Stimmblockdaten, Abstimmungsblöcke etc.*/
    public void loeschenAgenda() {
        dbBundle.dbAbstimmungen.deleteAll();
        dbBundle.dbAbstimmungZuAbstimmungsblock.deleteAll();
        dbBundle.dbAbstimmungenZuStimmkarte.deleteAll();
        dbBundle.dbStimmkarteInhalt.deleteAll();
        dbBundle.dbAbstimmungsblock.deleteAll();
    }

    private void anlegenEinzelneSammelkarte(String pName, //Namen der Sammelkarte, fix und fertig
            String pZusatz, //Zusatzinformation der Sammelkarte, fix und fertig, max 40 Zeichen
            String pOrt, //Ort
            int pSkIst, //1=KIAV, 2=SRV, 4=Briefwahl,
            int pGattung, //1 oder 2
            int pSkWeisungsartZulaessig, //5=mit Weisung
            int pSkBuchbarPapier, //0 oder 1
            int pSkBuchbarInternet, //0 oder 1
            int pSkBuchbarHV, //0 oder 1
            int pGruppe, int verborgen, //true für Deutsche Bank
            int pInstiIdent, //0 keine Insti
            boolean aktiv //false => die Sammelkarte wird auf inaktiv gesetzt;
    ) {

        BlSammelkarten blSammelkarte = new BlSammelkarten(true, dbBundle);
        blSammelkarte.neueSammelkarte();
        blSammelkarte.meldung.name = pName;
        blSammelkarte.meldung.zusatzfeld2 = CaString.trunc(pZusatz, 40);

        blSammelkarte.meldung.ort = pOrt;
        blSammelkarte.meldung.skIst = pSkIst;
        blSammelkarte.meldung.gattung = pGattung;

        blSammelkarte.meldung.skOffenlegung = 0;

        blSammelkarte.meldung.skWeisungsartZulaessig = pSkWeisungsartZulaessig;
        blSammelkarte.meldung.skBuchbarPapier = pSkBuchbarPapier;
        blSammelkarte.meldung.skBuchbarInternet = pSkBuchbarInternet;
        blSammelkarte.meldung.skBuchbarHV = pSkBuchbarHV;

        blSammelkarte.meldung.gruppe = pGruppe;

        if (verborgen>0) {
            blSammelkarte.meldung.zusatzfeld1 = Integer.toString(verborgen);
            /*Früher immer "2"*/
        }
        else {
            blSammelkarte.meldung.zusatzfeld1="";
        }

        if (pSkIst == 2) {
            for (int i = 0; i <= 100; i++) {
                blSammelkarte.abstimmungsvorschlag.abgabe[i] = 1;
            }
        }

        if (aktiv) {
            blSammelkarte.meldung.meldungAktiv = 1;
        } else {
            blSammelkarte.meldung.meldungAktiv = 0;
        }
        
        blSammelkarte.meldung.instiIdent=pInstiIdent;
        
        blSammelkarte.neueSammelkarteSpeichern();

    }

    private void anlegenEinzelneSammelkarteMitVarianten(String pName, //Namen der Sammelkarte, der ggf. ergänzt wird
            String pZusatz, //Zusatzinformation der Sammelkarte; bereits vorgefüllt "Internet", wird ggf. um "mit/ohne Weisung"+Gattung ergänzt 
            String pOrt, //Ort
            int pSkIst, //1=KIAV, 2=SRV, 4=Briefwahl,
            int pSkBuchbarPapier, //0 oder 1
            int pSkBuchbarInternet, //0 oder 1
            int pSkBuchbarHV, //0 oder 1
            int pGruppe, int verborgen, //2für  Deutsche Bank
            boolean aktiv //false => die Sammelkarte wird auf inaktiv gesetzt;
    ) {
        int anzGattungen = 0;
        for (int i = 1; i <= 5; i++) {
            if (dbBundle.param.paramBasis.getGattungAktiv(i)) {
                anzGattungen++;
            }
        }
        for (int i = 1; i <= 5; i++) {
            if (dbBundle.param.paramBasis.getGattungAktiv(i)) {
                int anzVorhanden=0;
                if (pSkIst==KonstSkIst.briefwahl && pSkBuchbarPapier==1) {
                    anzVorhanden=dbBundle.dbMeldungen.leseSammelkarteBriefwahlPapier(i, true);
                }
                if (pSkIst==KonstSkIst.briefwahl && pSkBuchbarInternet==1) {
                    anzVorhanden=dbBundle.dbMeldungen.leseSammelkarteBriefwahlInternet(i, true);
                }
                if (pSkIst==KonstSkIst.srv && pSkBuchbarPapier==1) {
                    anzVorhanden=dbBundle.dbMeldungen.leseSammelkarteSRVPapier(i, true);
                }
                if (pSkIst==KonstSkIst.srv && pSkBuchbarInternet==1) {
                    anzVorhanden=dbBundle.dbMeldungen.leseSammelkarteSRVInternet(i, true);
                }
                if (pSkIst==KonstSkIst.srv && pSkBuchbarHV==1) {
                    anzVorhanden=dbBundle.dbMeldungen.leseSammelkarteSRVHV(i, true);
                }
                if (anzVorhanden==0) {
                    String hZusatz = pZusatz;
                    if (pSkIst == 1) {
                        hZusatz = hZusatz + "mit Weisung";
                    }
                    if (anzGattungen > 1) {
                        hZusatz = hZusatz + "; " + dbBundle.param.paramBasis.getGattungBezeichnung(i);
                    }
                    anlegenEinzelneSammelkarte(pName, //Namen der Sammelkarte, fix und fertig
                            hZusatz, pOrt, //Ort
                            pSkIst, //1=KIAV, 2=SRV, 4=Briefwahl,
                            i, //Gattung
                            5, //5=mit Weisung
                            pSkBuchbarPapier, //0 oder 1
                            pSkBuchbarInternet, //0 oder 1
                            pSkBuchbarHV, //0 oder 1
                            pGruppe, verborgen, 0, aktiv);
                    if (pSkIst == 1) { /*Bei KIAV: nun noch ohne Weisung anlegen*/
                        hZusatz = pZusatz;
                        hZusatz = hZusatz + "ohne Weisung";
                        if (anzGattungen > 1) {
                            hZusatz = hZusatz + "; " + dbBundle.param.paramBasis.getGattungBezeichnung(i);
                        }
                        anlegenEinzelneSammelkarte(pName, //Namen der Sammelkarte, fix und fertig
                                hZusatz, pOrt, //Ort
                                pSkIst, //1=KIAV, 2=SRV, 4=Briefwahl,
                                i, //Gattung
                                3, //5=mit Weisung, 3=ohne Weisung
                                pSkBuchbarPapier, //0 oder 1
                                pSkBuchbarInternet, //0 oder 1
                                pSkBuchbarHV, //0 oder 1
                                pGruppe, verborgen, 0, aktiv);
                    }
                }
            }

        }
    }

    
    public void anlegenSammelkarteZuInsti(EclInsti pInsti, int pSammelart, boolean pMitWeisung, int pAnlegen) {
        boolean sammelkarteAktiv=true;
        if (pAnlegen==0) { 
            return;
        }
        if (dbBundle.param.paramBasis.namensaktienAktiv==false && pSammelart==KonstSkIst.dauervollmacht) {
            sammelkarteAktiv=false;
        }
        if (dbBundle.param.paramModuleKonfigurierbar.briefwahl==false && pSammelart==KonstSkIst.briefwahl) {
            sammelkarteAktiv=false;;
        }
        int anzGattungen=0;
        for (int iGattung=1;iGattung<=5;iGattung++) {
           if (dbBundle.param.paramBasis.getGattungAktiv(iGattung)) {
                anzGattungen++;
            }
        }
        for (int iGattung=1;iGattung<=5;iGattung++) {
            if (dbBundle.param.paramBasis.getGattungAktiv(iGattung)) {
                boolean sammelkarteBereitsAngelegt=false;
                dbBundle.dbMeldungen.leseSammelkartenArtZuInsti(iGattung, pInsti.ident, pSammelart, true);
                EclMeldung[] lMeldungArray=dbBundle.dbMeldungen.meldungenArray;
                if (lMeldungArray!=null && lMeldungArray.length>0) {
                    if (pSammelart==KonstSkIst.kiav) {
                        for (int i=0;i<lMeldungArray.length;i++) {
                            EclMeldung lMeldung=lMeldungArray[i];
                            if (pMitWeisung && lMeldung.akzeptiertDedizierteWeisung()) {
                                sammelkarteBereitsAngelegt=true;
                            }
                            if (pMitWeisung==false && lMeldung.akzeptiertOhneWeisung()) {
                                sammelkarteBereitsAngelegt=true;
                            }
                        }
                    }
                    else {
                        sammelkarteBereitsAngelegt=true;
                    }
                }
                if (sammelkarteBereitsAngelegt==false) {
                    String hName=pInsti.standardMeldeName;
                    String hZusatz="";
                    String hOrt=pInsti.standardMeldeOrt;
                    int hSkWeisungsartZulaessig=5;//5=mit Weisung, 3=ohne Weisung
                    int hGruppe=pInsti.standardSammelkarteGruppennummer;
                    switch (pSammelart) {
                    case KonstSkIst.kiav:
                        if (pMitWeisung) {
                            hZusatz=hZusatz + "mit Weisung";
                        }
                        else {
                            hZusatz=hZusatz + "ohne Weisung";
                            hSkWeisungsartZulaessig=3;
                        }
                        break;
                    case KonstSkIst.srv:
                        hName="Stimmrechtsvertreter";
                        hOrt=dbBundle.eclEmittent.hvOrt;
                        hGruppe=9001;
                        break;
                    case KonstSkIst.briefwahl:
                        hName="Briefwahl";
                        hOrt=dbBundle.eclEmittent.hvOrt;
                        hGruppe=9002;
                         break;
                    case KonstSkIst.dauervollmacht:
                        hZusatz=hZusatz + "Dauervollmacht";
                       break;
                     }
                   if (anzGattungen>1) {
                       if (!hZusatz.isEmpty()) {
                           hZusatz=hZusatz+"; ";
                       }
                       hZusatz = hZusatz + dbBundle.param.paramBasis.getGattungBezeichnung(iGattung);
                   }
                   if (pSammelart==KonstSkIst.srv || pSammelart==KonstSkIst.briefwahl) {
                       if (!hZusatz.isEmpty()) {
                           hZusatz=hZusatz+"; ";
                       }
                       hZusatz=hZusatz+pInsti.kurzBezeichnung;
                   }
                   
                   anlegenEinzelneSammelkarte(
                           hName, hZusatz, hOrt, pSammelart, iGattung,
                           hSkWeisungsartZulaessig, 1, 0, 0, hGruppe, pInsti.weisungsWeitergabe, pInsti.ident, sammelkarteAktiv);
                }
            }
        }
    }
    
    /**Legt Sammelkarten nach Standardschema an.
     * pOrtDerHV wird als Ort für Stimmrechtsvertreter/Briefwahl-Karten eingetragen.
     * pPortalVorhanden=true => auch die Internet-Karten werden angelegt.
     * pBriefwahlVorhanden=true => auch die Briefwahlkarten werden angelegt
     */
    public void anlegenSammelkartenStandard() {
        boolean alt=false;

        boolean pPortalVorhanden = false;
        boolean pBriefwahlVorhanden = false;
        if (dbBundle.param.paramModuleKonfigurierbar.aktionaersportal
                || dbBundle.param.paramModuleKonfigurierbar.hvApp) {
            pPortalVorhanden = true;
        }
        if (dbBundle.param.paramModuleKonfigurierbar.briefwahl) {
            pBriefwahlVorhanden = true;
        }

        /*Stimmrechtsvertreter Papier + Internet*/
        anlegenEinzelneSammelkarteMitVarianten("Stimmrechtsvertreter", "Papier", dbBundle.eclEmittent.hvOrt, 2, //skIst 1=KIAV; 2=SRV, 4=Briefwahl
                1, //Buchbar Papier
                0, //Buchbar Internet
                0, //Buchbar HV
                9001, 0, //verborgen
                true);
        anlegenEinzelneSammelkarteMitVarianten("Stimmrechtsvertreter", "Internet", dbBundle.eclEmittent.hvOrt, 2, //skIst 1=KIAV; 2=SRV, 4=Briefwahl
                0, //Buchbar Papier
                1, //Buchbar Internet
                0, //Buchbar HV
                9001, 0, //verborgen
                pPortalVorhanden);
        anlegenEinzelneSammelkarteMitVarianten("Stimmrechtsvertreter", "HV", dbBundle.eclEmittent.hvOrt, 2, //skIst 1=KIAV; 2=SRV, 4=Briefwahl
                0, //Buchbar Papier
                0, //Buchbar Internet
                1, //Buchbar HV
                9001, 0, //verborgen
                true);

        /*Briefwahl*/
        anlegenEinzelneSammelkarteMitVarianten("Briefwahl", "Papier", dbBundle.eclEmittent.hvOrt, 4, //skIst 1=KIAV; 2=SRV, 4=Briefwahl
                1, //Buchbar Papier
                0, //Buchbar Internet
                0, //Buchbar HV
                9002, 0, //verborgen
                pBriefwahlVorhanden);
        anlegenEinzelneSammelkarteMitVarianten("Briefwahl", "Internet", dbBundle.eclEmittent.hvOrt, 4, //skIst 1=KIAV; 2=SRV, 4=Briefwahl
                0, //Buchbar Papier
                1, //Buchbar Internet
                0, //Buchbar HV
                9002, 0, //verborgen
                pBriefwahlVorhanden & pPortalVorhanden);

        if (!alt) {
            dbBundle.dbInsti.readAlle();
            List<EclInsti> instiListe=dbBundle.dbInsti.ergebnis();
            if (instiListe!=null && instiListe.size()!=0) {
                for (int i=0;i<instiListe.size();i++) {
                    EclInsti lInsti=instiListe.get(i);
                    if (lInsti.standardSammelkartenAnlageAktiv==1 &&
                            (lInsti.standardSammelkarteMitWeisung==1 || lInsti.standardSammelkarteOhneWeisung==1 || 
                            lInsti.standardSammelkarteSRV==1 || lInsti.standardSammelkarteBriefwahl==1 || 
                            lInsti.standardSammelkarteDauervollmacht==1)) {
                        anlegenSammelkarteZuInsti(lInsti, KonstSkIst.kiav, true, lInsti.standardSammelkarteMitWeisung);
                        anlegenSammelkarteZuInsti(lInsti, KonstSkIst.kiav, false, lInsti.standardSammelkarteOhneWeisung);
                        anlegenSammelkarteZuInsti(lInsti, KonstSkIst.srv, true, lInsti.standardSammelkarteSRV);
                        anlegenSammelkarteZuInsti(lInsti, KonstSkIst.briefwahl, true, lInsti.standardSammelkarteBriefwahl);
                        anlegenSammelkarteZuInsti(lInsti, KonstSkIst.dauervollmacht, true, lInsti.standardSammelkarteDauervollmacht);
                    }
                }
            }
        }
        
        
        
        
        if (alt) {
 

            anlegenSammelkartenStandardErgaenzen();
        }

    }

    
    public void anlegenSammelkartenStandardErgaenzen() {
        
 
    }
    
    
    public void anlegenSammelkartenTest() {

 
    }

    /**Löscht bei allen Mandanetn, die bis einschließlich pDatum die HV hatten,
     * alle Aktionärsabhängigen daten 
     */
    public void mandantenAktionaersdatenLoeschen(String pDatum){
        /*XXX*/
        
        int lMandant=dbBundle.clGlobalVar.mandant;
        int lHvJahr=dbBundle.clGlobalVar.hvJahr;
        String lHvNummer=dbBundle.clGlobalVar.hvNummer;
        String lDbArt=dbBundle.clGlobalVar.datenbereich;
        
        dbBundle.dbEmittenten.readAll(0);
        EclEmittenten[] emittentenListe = dbBundle.dbEmittenten.ergebnisArray;
        int anzEmittenten = dbBundle.dbEmittenten.anzErgebnis();
        for (int i = 0; i < anzEmittenten; i++) {
            
            EclEmittenten lEmittent = emittentenListe[i];
            String lHvDatum=lEmittent.hvDatum.trim();
            
            if (lHvDatum.isEmpty() ||
                    pDatum.isEmpty() ||
                    CaDatumZeit.vergleicheAnzeigeDatums(lHvDatum, pDatum)<=0
                    ) {

                dbBundle.clGlobalVar.mandant = lEmittent.mandant;
                dbBundle.clGlobalVar.hvJahr = lEmittent.hvJahr;
                dbBundle.clGlobalVar.hvNummer = lEmittent.hvNummer;
                dbBundle.clGlobalVar.datenbereich = lEmittent.dbArt;

                CaBug.druckeInfo("mandant="+lEmittent.mandant);
                ruecksetzenMeldebestandUndHV();
                loeschenAktienregister();
                dbBundle.dbAktienregister.deleteAll_aktienregisterHistorie();
            }

        }
        dbBundle.clGlobalVar.mandant=lMandant;
        dbBundle.clGlobalVar.hvJahr=lHvJahr;
        dbBundle.clGlobalVar.hvNummer=lHvNummer;
        dbBundle.clGlobalVar.datenbereich=lDbArt;
    }
}
