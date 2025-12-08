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
package de.meetingapps.meetingportal.meetComBl;

import java.util.LinkedList;
import java.util.List;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaString;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclAbstimmung;
import de.meetingapps.meetingportal.meetComEntities.EclAbstimmungZuAbstimmungsblock;
import de.meetingapps.meetingportal.meetComEntities.EclAbstimmungsblock;
import de.meetingapps.meetingportal.meetComEntities.EclMeldung;
import de.meetingapps.meetingportal.meetComKonst.KonstKartenart;
import de.meetingapps.meetingportal.meetComKonst.KonstKartenklasse;
import de.meetingapps.meetingportal.meetComStub.StubRoot;
import de.meetingapps.meetingportal.meetComStub.WEStubBlStimmkartendruck;
import de.meetingapps.meetingportal.meetComStub.WEStubBlStimmkartendruckRC;
import de.meetingapps.meetingportal.meetComWE.WELoginVerify;
import de.meetingapps.meetingportal.meetingCoreReport.RpDrucken;
import de.meetingapps.meetingportal.meetingCoreReport.RpVariablen;

/**Ausdruck von Stimmkarten
 * 
 * Es können bis zu 4 Stimmkarten je Meldung gedruckt werden (analog zu den 4 zuordenbaren Stimmkartennummern)
 * 
 * Je Stimmkarte werden benötigt:
 * > Wird überhaupt zugeordnet:
 *      pPraesenzStimmkartenZuordnenGattung
 *      pPraesenzStimmkartenZuordnenAppGattung
 * > Text zum Anzeigen:
 *      pPraesenzStimmkartenZuordnenGattungText
 * > Formularnummer (01 bis 99):
 *       pPraesenzStimmkarteFormularnummer
 * > Tausch 1:1 oder beliebig 
 *      pPraesenzStimmkarteTauschBeliebig
 * > Beim Nachdruck über Service-Desk-Tool aktiviert 
 *      pPraesenzStimmkarteNachdruckServiceDesk
 * 
 *      
 * Selektiongsmöglichkeiten beim Druck:     
 * ------------------------------------     
 * Gattung
 * 
 * Stimmkarte (aktive werden angeboten)
 * 
 * Falls Tausch 1:1: Von EK bis EK
 * Falls Tausch beliebig: von SK bis SK
 * 
 * Falls Tausch 1:1: Druck-Reihenfolge:
 * > Aufsteigend nach EK
 * > Aufsteigend nach Alphabet (Achtung, Umlaute ...)
 * 
 * Falls Tausch 1:1: Druck-Selektion - Art:
 * > Für Sammelkarten
 * > Für Aktionäre in Sammelkarten
 * > Für Aktionäre nicht in Sammelkarten
 * 
 */

/**Ideen:
 * > Schalteranzahl => Trennbuchstaben ermitteln
 */

/**Stub-fähig*/
public class BlStimmkartendruck extends StubRoot {

    private int logDrucken=10;

    static public final int KONST_SELEKTION_AKTIONAERE_NICHT_IN_SAMMELKARTEN=1;
    static public final int KONST_SELEKTION_AKTIONAERE_IN_SAMMELKARTEN=2;
    static public final int KONST_SELEKTION_SAMMELKARTEN=4;

    static public final int KONST_DRUCKLAUF_ALLE=1;
    /**Noch nicht unterstützt*/
    static public final int KONST_DRUCKLAUF_WIEDERHOLUNG=2;
    /**Noch nicht unterstützt*/
    static public final int KONST_ALLE_UNGEDRUCKTEN=3;

    public List<EclMeldung> rcMeldungsliste=null;

    
    /**Abstimmungsblock für Stimmkartendruck*/
    public EclAbstimmungsblock aktiverAbstimmungsblock = null;
    public EclAbstimmungZuAbstimmungsblock[] abstimmungenZuAktivenBlock = null;
    public EclAbstimmung[] abstimmungen = null;

    public  boolean stimmkartenUeberschriftenStehenZurVerfuegung=false;
    
    public BlStimmkartendruck(boolean pIstServer, DbBundle pDbBundle) {
        super(pIstServer, pDbBundle);
        
        holeAbstimmungsblockFuerStimmkartendruck();
    }

    
    /**2
     * 
     * Returnwert -1 => Abstimmungsblock für Stimmkartendruck entweder nicht aktiv, 
     * oder nicht vorhanden, oder keine Druckreihenfolge konfiguriert
     */
    public int holeAbstimmungsblockFuerStimmkartendruck() {
        CaBug.druckeLog("", logDrucken, 10);
        if (verwendeWebService()) {
            WEStubBlStimmkartendruck weStubBlStimmkartendruck = new WEStubBlStimmkartendruck();
            weStubBlStimmkartendruck.stubFunktion = 2;
            
            WELoginVerify weLoginVerify = new WELoginVerify();
            weStubBlStimmkartendruck.setWeLoginVerify(weLoginVerify);

            WEStubBlStimmkartendruckRC weStubBlStimmkartendruckRC = wsClient.stubBlstimmkartendruck(weStubBlStimmkartendruck);
            aktiverAbstimmungsblock=weStubBlStimmkartendruckRC.aktiverAbstimmungsblock;
            abstimmungenZuAktivenBlock=weStubBlStimmkartendruckRC.abstimmungenZuAktivenBlock;
            abstimmungen=weStubBlStimmkartendruckRC.abstimmungen;
            stimmkartenUeberschriftenStehenZurVerfuegung=weStubBlStimmkartendruckRC.stimmkartenUeberschriftenStehenZurVerfuegung;
            
            if (stimmkartenUeberschriftenStehenZurVerfuegung==false) {
                return -1;
            }
           
            if (weStubBlStimmkartendruckRC.rc < 1) {
                return weStubBlStimmkartendruckRC.rc;
            }

            return weStubBlStimmkartendruckRC.rc;
        }

        
        dbOpen();
        int abstimmungsBlockFuerStimmkartendruck=lDbBundle.param.paramAbstimmungParameter.stimmzetteldruckAbstimmungsblockBasis;
        if (abstimmungsBlockFuerStimmkartendruck==0) {
            CaBug.druckeLog("Kein Abstimmungsblock aktiviert", logDrucken, 10);
            /*Dann kein Abstimmungsblock aktiviert*/
            dbClose();
            stimmkartenUeberschriftenStehenZurVerfuegung=false;
            return -1;
        }
        BlAbstimmung blAbstimmung=new BlAbstimmung(lDbBundle);
        blAbstimmung.setzeAktivenAbstimmungsblockSortierenNachStimmzettelDruck();
        boolean brc=blAbstimmung.leseAktivenOderLokalenAbstimmungsblock(abstimmungsBlockFuerStimmkartendruck);
        CaBug.druckeLog("brc="+brc, logDrucken, 10);
        dbClose();
        if (brc==false || blAbstimmung.abstimmungenZuAktivenBlock==null || blAbstimmung.abstimmungenZuAktivenBlock.length==0) {
            if (abstimmungenZuAktivenBlock==null) {
                CaBug.druckeLog("abstimmungenZuAktivenBlock==null", logDrucken, 10);
            }
            else {
                CaBug.druckeLog("abstimmungenZuAktivenBlock.length==0", logDrucken, 10);
            }
            /*Eingestellter Abstimmungsblock konnte nicht gelesen werden*/
            stimmkartenUeberschriftenStehenZurVerfuegung=false;
            return -1;
        }
        aktiverAbstimmungsblock=blAbstimmung.aktiverAbstimmungsblock;
        abstimmungenZuAktivenBlock=blAbstimmung.abstimmungenZuAktivenBlock;
        abstimmungen=blAbstimmung.abstimmungen;
        
        stimmkartenUeberschriftenStehenZurVerfuegung=true;
        return 1;
    }
    
    /**1
     * 
     * Gattung kann 0 sein - dient dazu eine spezielle Meldung mit einer speziellen EK zu holen, ohne zu wissen
     * welche Gattung das ist
     * 
     * pDrucklauf= KONST_DRUCKLAUF_*
     * pSelektion: bei pDrucklauf===KONST_DRUCKLAUF_ALLE oder ==KONST_ALLE_UNGEDRUCKTEN: (mit oder verknüpft) KONST_SELEKTION_*
     * pVonNr / pBisNr: bei pDrucklauf==KONST_DRUCKLAUF_ALLE;
     * pDrucklaufNr: bei pDrucklauf==KONST_DRUCKLAUF_WIEDERHOLUNG
     * pSortierung 1=zutrittsIdent, 2=Nachname*/
    public int holeMeldedatenFuerTausch1Zu1(int pGattung, int pDrucklauf, int pSelektion, int pVonNr, int pBisNr, int pDrucklaufNr, int pSortierung) {
        if (verwendeWebService()) {
            WEStubBlStimmkartendruck weStubBlStimmkartendruck = new WEStubBlStimmkartendruck();
            weStubBlStimmkartendruck.stubFunktion = 1;
            weStubBlStimmkartendruck.pGattung=pGattung;
            weStubBlStimmkartendruck.pDrucklauf=pDrucklauf;
            weStubBlStimmkartendruck.pSelektion=pSelektion;
            weStubBlStimmkartendruck.pVonNr=pVonNr;
            weStubBlStimmkartendruck.pBisNr=pBisNr;
            weStubBlStimmkartendruck.pDrucklaufNr=pDrucklaufNr;
            weStubBlStimmkartendruck.pSortierung=pSortierung;
            
            WELoginVerify weLoginVerify = new WELoginVerify();
            weStubBlStimmkartendruck.setWeLoginVerify(weLoginVerify);

            WEStubBlStimmkartendruckRC weStubBlStimmkartendruckRC = wsClient.stubBlstimmkartendruck(weStubBlStimmkartendruck);
            rcMeldungsliste=weStubBlStimmkartendruckRC.rcMeldungsliste;
            
            if (weStubBlStimmkartendruckRC.rc < 1) {
                return weStubBlStimmkartendruckRC.rc;
            }

            return weStubBlStimmkartendruckRC.rc;
        }

        
        if (pDrucklauf==KONST_DRUCKLAUF_ALLE) {
            CaBug.druckeLog("pDrucklauf==KONST_DRUCKLAUF_ALLE", logDrucken, 10);
            dbOpen();

            boolean aktionaereNichtInSammelkarte=false;
            boolean aktionaereInSammelkarte=false;
            boolean sammelkarten=false;
            
            if ((pSelektion & KONST_SELEKTION_AKTIONAERE_NICHT_IN_SAMMELKARTEN)==KONST_SELEKTION_AKTIONAERE_NICHT_IN_SAMMELKARTEN) {
                aktionaereNichtInSammelkarte=true;
            }
            if ((pSelektion & KONST_SELEKTION_AKTIONAERE_IN_SAMMELKARTEN)==KONST_SELEKTION_AKTIONAERE_IN_SAMMELKARTEN) {
                aktionaereInSammelkarte=true;
            }
            if ((pSelektion & KONST_SELEKTION_SAMMELKARTEN)==KONST_SELEKTION_SAMMELKARTEN) {
                sammelkarten=true;
            }
            
            CaBug.druckeLog("aktionaereNichtInSammelkarte="+aktionaereNichtInSammelkarte+" aktionaereInSammelkarte="+aktionaereInSammelkarte
                    +" sammelkarten="+sammelkarten, pBisNr, pDrucklaufNr);
            
            lDbBundle.dbMeldungen.leseMeldungenMitEKSelektionGattung(pGattung, aktionaereNichtInSammelkarte, aktionaereInSammelkarte, sammelkarten, pSortierung);
            int anzMeldungen=lDbBundle.dbMeldungen.anzErgebnis();
            
            CaBug.druckeLog("anzMeldungen="+anzMeldungen+" pVonNr="+pVonNr+" pBisNr="+pBisNr, logDrucken, 10);
            
           
            rcMeldungsliste=new LinkedList<EclMeldung>();
            if (anzMeldungen>0) {
                for (int i=0;i<anzMeldungen;i++) {
                    EclMeldung lMeldung=lDbBundle.dbMeldungen.meldungenArray[i];
                    String hEk=lMeldung.zutrittsIdent;
                    int ekNr=Integer.parseInt(hEk);
                    CaBug.druckeLog("ekNr="+ekNr, logDrucken, 10);
                    if ((pVonNr==0 || ekNr>=pVonNr) && (pBisNr==0 || ekNr<=pBisNr)) {
                        CaBug.druckeLog("vor Add", logDrucken, 10);
                        rcMeldungsliste.add(lMeldung);
                    }
                }
            }
            
            dbClose();
            return 1;
        }
        
        return 1;
    }
    
    /**Wird immer lokal ausgeführt*/
    public int druckeTauschBeliebig(RpDrucken rpDrucken, RpVariablen rpVariablen, int pGewaehltGattung, int pGewaehltStimmkarte, int pVonNr, int pBisNr) {
        meldedatenVariablenLeer(rpDrucken, rpVariablen);
        for (int i=pVonNr;i<=pBisNr;i++) {
            stimmkartenDatenBelegen(rpDrucken, rpVariablen, i, pGewaehltGattung, pGewaehltStimmkarte, true);
            stimmkarteDrucken(rpDrucken);
        }
        return 1;
    }
    
    /**verarbeitet rcMeldungsliste. Kann vorher Manuell gefüllt werden, oder über holeMeldedatenFuerTausch1Zu1
     * 
     * Für die Meldungen gilt: EclMeldung.zutrittsIdent muß belegt sein
     * */
    public int drucke1Zu1(RpDrucken rpDrucken, RpVariablen rpVariablen, int pGewaehltGattung, int pGewaehltStimmkarte) {
        for (int i=0;i<rcMeldungsliste.size();i++) {
            EclMeldung lMeldung=rcMeldungsliste.get(i);
            meldedatenVariablenBelegen(rpDrucken, rpVariablen, lMeldung);
            String hENr=lMeldung.zutrittsIdent;
            int eNr=0;
            if (!hENr.isEmpty()) {
                eNr=Integer.parseInt(hENr);
            }
            stimmkartenDatenBelegen(rpDrucken, rpVariablen, eNr, pGewaehltGattung, pGewaehltStimmkarte, false);
            stimmkarteDrucken(rpDrucken);
        }
        return 1;
    }
    
    
    /**Setzt die Meldedaten-Abhängigen Variablen auf ""*/
    private void meldedatenVariablenLeer(RpDrucken rpDrucken, RpVariablen rpVariablen) {
        rpVariablen.fuelleVariable(rpDrucken, "Nummern.ZutrittsIdent", "");
        rpVariablen.fuelleVariable(rpDrucken, "Nummern.ZutrittsIdentKurz", "");
        rpVariablen.fuelleVariable(rpDrucken, "Nummern.ZutrittsIdentKomplett", "");
        rpVariablen.fuelleVariable(rpDrucken, "Nummern.Aktionaersnummer", "");
        rpVariablen.fuelleVariable(rpDrucken, "Besitz.Stimmen", "");
        rpVariablen.fuelleVariable(rpDrucken, "Besitz.StimmenDE", "");
        rpVariablen.fuelleVariable(rpDrucken, "Besitz.StimmenEN", "");

        rpVariablen.fuelleVariable(rpDrucken, "Aktionaer.Titel", "");
        rpVariablen.fuelleVariable(rpDrucken, "Aktionaer.Name", "");
        rpVariablen.fuelleVariable(rpDrucken, "Aktionaer.Vorname", "");
        rpVariablen.fuelleVariable(rpDrucken, "Aktionaer.Plz", "");
        rpVariablen.fuelleVariable(rpDrucken, "Aktionaer.Ort", "");
        rpVariablen.fuelleVariable(rpDrucken, "Aktionaer.Strasse", "");
        rpVariablen.fuelleVariable(rpDrucken, "Aktionaer.TitelVornameName", "");
        rpVariablen.fuelleVariable(rpDrucken, "Aktionaer.NameVornameTitel", "");
         rpVariablen.fuelleVariable(rpDrucken, "Aktionaer.BesitzArtKuerzel", "");
        rpVariablen.fuelleVariable(rpDrucken, "Aktionaer.GattungKurz", "");
        rpVariablen.fuelleVariable(rpDrucken, "Aktionaer.BesitzArt", "");
        rpVariablen.fuelleVariable(rpDrucken, "Aktionaer.BesitzArtEN", "");
        rpVariablen.fuelleVariable(rpDrucken, "Aktionaer.Zusatzfeld3", "5");


        rpVariablen.fuelleVariable(rpDrucken, "Vertreter.Name", "");
        rpVariablen.fuelleVariable(rpDrucken, "Vertreter.Vorname", "");
        rpVariablen.fuelleVariable(rpDrucken, "Vertreter.Ort", "");
   }
    
    private void meldedatenVariablenBelegen(RpDrucken rpDrucken, RpVariablen rpVariablen, EclMeldung pEclMeldung) {
        BlNummernformen blNummernform = new BlNummernformen(lDbBundle);
        /*TODO: ZutrittsidentNeben irgendwo herholen ...*/
        rpVariablen.fuelleVariable(rpDrucken, "Nummern.ZutrittsIdent", BlNummernformen
                .verketteEKMitNeben(pEclMeldung.zutrittsIdent, "00", 0));
        rpVariablen.fuelleVariable(rpDrucken, "Nummern.ZutrittsIdentKurz", pEclMeldung.zutrittsIdent);
        rpVariablen.fuelleVariable(rpDrucken, "Nummern.ZutrittsIdentKomplett", blNummernform
                .formatiereEKNrKomplett(pEclMeldung.zutrittsIdent, "00"));
        if (lDbBundle.param.paramBasis.namensaktienAktiv) {
            rpVariablen.fuelleVariable(rpDrucken, "Nummern.Aktionaersnummer",
                    BlAktienregisterNummerAufbereiten.aufbereitenFuerKlarschrift(pEclMeldung.aktionaersnummer));
        } else {
            rpVariablen.fuelleVariable(rpDrucken, "Nummern.Aktionaersnummer", pEclMeldung.aktionaersnummer);
        }
        rpVariablen.fuelleVariable(rpDrucken, "Besitz.Stimmen", Long.toString(pEclMeldung.stimmen));
        rpVariablen.fuelleVariable(rpDrucken, "Besitz.StimmenDE", CaString.toStringDE(pEclMeldung.stimmen));
        rpVariablen.fuelleVariable(rpDrucken, "Besitz.StimmenEN", CaString.toStringEN(pEclMeldung.stimmen));

        rpVariablen.fuelleVariable(rpDrucken, "Aktionaer.Titel", pEclMeldung.titel);
        rpVariablen.fuelleVariable(rpDrucken, "Aktionaer.Name", pEclMeldung.name);
        rpVariablen.fuelleVariable(rpDrucken, "Aktionaer.Vorname", pEclMeldung.vorname);
        rpVariablen.fuelleVariable(rpDrucken, "Aktionaer.Plz", pEclMeldung.plz);
        rpVariablen.fuelleVariable(rpDrucken, "Aktionaer.Ort", pEclMeldung.ort);
        rpVariablen.fuelleVariable(rpDrucken, "Aktionaer.Strasse", pEclMeldung.strasse);
        
        /*Kombi-Felder füllen*/
        String titelVornameName = "";
        if (pEclMeldung.titel.length() != 0) {
            titelVornameName = titelVornameName + pEclMeldung.titel + " ";
        }
        if (pEclMeldung.vorname.length() != 0) {
            titelVornameName = titelVornameName + pEclMeldung.vorname + " ";
        }
        titelVornameName = titelVornameName + pEclMeldung.name;
        rpVariablen.fuelleVariable(rpDrucken, "Aktionaer.TitelVornameName", titelVornameName);

        String nameVornameTitel = "";
        nameVornameTitel = pEclMeldung.name;
        if (pEclMeldung.titel.length() != 0 || pEclMeldung.vorname.length() != 0) {
            nameVornameTitel = nameVornameTitel + ",";
        }
        if (pEclMeldung.titel.length() != 0) {
            nameVornameTitel = nameVornameTitel + " " + pEclMeldung.titel;
        }
        if (pEclMeldung.vorname.length() != 0) {
            nameVornameTitel = nameVornameTitel + " " + pEclMeldung.vorname;
        }
        rpVariablen.fuelleVariable(rpDrucken, "Aktionaer.NameVornameTitel", nameVornameTitel);

        String besitzArtKuerzel = pEclMeldung.besitzart;
        String besitzArt = "", besitzArtEN = "";
        switch (besitzArtKuerzel) {
        case "E":
            besitzArt = "Eigenbesitz";
            besitzArtEN = "Proprietary Possession";
            break;
        case "F":
            besitzArt = "Fremdbesitz";
            besitzArtEN = "Minority Interests";
            break;
        case "V":
            besitzArt = "Vollmachtsbesitz";
            besitzArtEN = "Proxy Possession";
            break;
        default:
            break;
        }
        rpVariablen.fuelleVariable(rpDrucken, "Aktionaer.BesitzArtKuerzel", besitzArtKuerzel);
        rpVariablen.fuelleVariable(rpDrucken, "Aktionaer.BesitzArt", besitzArt);
        rpVariablen.fuelleVariable(rpDrucken, "Aktionaer.BesitzArtEN", besitzArtEN);
        rpVariablen.fuelleVariable(rpDrucken, "Aktionaer.GattungKurz",
                lDbBundle.param.paramBasis.getGattungBezeichnungKurz(pEclMeldung.liefereGattung()));

        rpVariablen.fuelleVariable(rpDrucken, "Aktionaer.Zusatzfeld3", pEclMeldung.zusatzfeld3);


        rpVariablen.fuelleVariable(rpDrucken, "Vertreter.Name", pEclMeldung.vertreterName);
        rpVariablen.fuelleVariable(rpDrucken, "Vertreter.Vorname", pEclMeldung.vertreterVorname);
        rpVariablen.fuelleVariable(rpDrucken, "Vertreter.Ort", pEclMeldung.vertreterOrt);
    }
    
    private void stimmkartenDatenBelegen(RpDrucken rpDrucken, RpVariablen rpVariablen,  int pStimmkartennr, int pGewaehltGattung, int pGewaehltStimmkarte, boolean pTauschBeliebig) {
        rpVariablen.fuelleAllgemeineVariablen(rpDrucken);
        BlNummernformen blNummernform = new BlNummernformen(lDbBundle);
        
        blNummernform.rcStimmkarteSubNummernkreis=pGewaehltStimmkarte; 
        blNummernform.rcGattung=pGewaehltGattung; 
        
        String stimmkartennr=Integer.toString(pStimmkartennr);
        
        int kartenklasse=0;
        if (pTauschBeliebig) {
            kartenklasse=KonstKartenklasse.stimmkartennummer;
        }
        else {
            kartenklasse=KonstKartenklasse.eintrittskartennummer;
        }
        /*Nur die Stimmkartennummer*/
        rpVariablen.fuelleVariable(rpDrucken, "SNr.SNrKlartext", blNummernform.formatiereNr(stimmkartennr, kartenklasse));

        /*Etikett*/
        rpVariablen.fuelleVariable(rpDrucken, "SNr.Etikett", blNummernform.formatiereNrKomplett(stimmkartennr, "00", kartenklasse, KonstKartenart.stimmkartenEtikett));
        
        /*Stimmkarten Zugang*/
        rpVariablen.fuelleVariable(rpDrucken, "SNr.Zugang", blNummernform.formatiereNrKomplett(stimmkartennr, "00", kartenklasse, KonstKartenart.erstzugang));
       
        /*Stimmkarten Abgang*/
        rpVariablen.fuelleVariable(rpDrucken, "SNr.Abgang", blNummernform.formatiereNrKomplett(stimmkartennr, "00", kartenklasse, KonstKartenart.abgang));
                
        /*Stimmkarten Wiederzugang*/
        rpVariablen.fuelleVariable(rpDrucken, "SNr.Wiederzugang", blNummernform.formatiereNrKomplett(stimmkartennr, "00", kartenklasse, KonstKartenart.wiederzugang));
                
        /*Stimmkarten Vollmacht an Dritte*/
        rpVariablen.fuelleVariable(rpDrucken, "SNr.VollmachtDritte", blNummernform.formatiereNrKomplett(stimmkartennr, "00", kartenklasse, KonstKartenart.vollmachtAnDritteErteilen));
                
        /*Stimmkarten Vollmacht/Weisung SRV*/
        rpVariablen.fuelleVariable(rpDrucken, "SNr.WeisungSRV", blNummernform.formatiereNrKomplett(stimmkartennr, "00", kartenklasse, KonstKartenart.vollmachtWeisungSRV));
              
        /*200 Stimmkarten: Neutral, J, N, E*/
        for (int i=1;i<=200;i++) {
            blNummernform.rcStimmkartennummer=i; 

            blNummernform.rcStimmart=0; 
            rpVariablen.fuelleVariable(rpDrucken, "SNr.SNr"+Integer.toString(i)+"Neutral", blNummernform.formatiereNrKomplett(stimmkartennr, "00", kartenklasse, KonstKartenart.stimmabschnittsnummer));
            blNummernform.rcStimmart=1; 
            rpVariablen.fuelleVariable(rpDrucken, "SNr.SNr"+Integer.toString(i)+"Ja", blNummernform.formatiereNrKomplett(stimmkartennr, "00", kartenklasse, KonstKartenart.stimmabschnittsnummer));
            blNummernform.rcStimmart=2; 
            rpVariablen.fuelleVariable(rpDrucken, "SNr.SNr"+Integer.toString(i)+"Nein", blNummernform.formatiereNrKomplett(stimmkartennr, "00", kartenklasse, KonstKartenart.stimmabschnittsnummer));
            blNummernform.rcStimmart=3; 
            rpVariablen.fuelleVariable(rpDrucken, "SNr.SNr"+Integer.toString(i)+"Enthaltung", blNummernform.formatiereNrKomplett(stimmkartennr, "00", kartenklasse, KonstKartenart.stimmabschnittsnummer));
        }
        
        CaBug.druckeLog("stimmkartenUeberschriftenStehenZurVerfuegung="+stimmkartenUeberschriftenStehenZurVerfuegung, logDrucken, 10);
        if (stimmkartenUeberschriftenStehenZurVerfuegung==false) {
            /**Abstimmungsblock für Sznr-Überschriften konnte nicht eingelesen werden, =>
             * alle Variablen auf leer setzen
             */
            for (int i=1;i<200;i++) {
                rpVariablen.fuelleVariable(rpDrucken, "SNr.SzArt"+Integer.toString(i), "");
                rpVariablen.fuelleVariable(rpDrucken, "SNr.SzKuerzel"+Integer.toString(i), "");
                rpVariablen.fuelleVariable(rpDrucken, "W.Ueberschrift"+Integer.toString(i), "");
            }
        }
        else {
            /**Abstimmungsblock für SzNR-Überschriften vorhanden.*/
            
            /*Schritt 1: die "Standard-Tagesordnung-SZNR" drucken, beginnend bei 1.*/
            CaBug.druckeLog("Schritt 1 abstimmungenZuAktivenBlock.length="+abstimmungenZuAktivenBlock.length, logDrucken, 10);
            int hoechsteVerwendeteStimmzettelNr=0;
            for (int i=0;i<abstimmungenZuAktivenBlock.length;i++) {
                EclAbstimmungZuAbstimmungsblock iAbstimmungZuAbstimmungsblock=abstimmungenZuAktivenBlock[i];
                EclAbstimmung iAbstimmung=abstimmungen[i];
                
                CaBug.druckeLog("iAbstimmungZuAbstimmungsblock.positionInAusdruck="+iAbstimmungZuAbstimmungsblock.positionInAusdruck, logDrucken, 10);
                 
                if (iAbstimmungZuAbstimmungsblock.positionInAusdruck!=0) {
                    CaBug.druckeLog("pGewaehltGattung="+pGewaehltGattung+" iAbstimmung.stimmberechtigteGattungen[pGewaehltGattung-1]="+iAbstimmung.stimmberechtigteGattungen[pGewaehltGattung-1], logDrucken, 10);
                                        
                    if (iAbstimmung.stimmberechtigteGattungen[pGewaehltGattung-1]==1) {
                        hoechsteVerwendeteStimmzettelNr++;
                        String szKuerzel=iAbstimmung.nummerKey+iAbstimmung.nummerindexKey;
                        CaBug.druckeLog("i="+i+" Art = 1 "+szKuerzel, logDrucken, 10);
                        rpVariablen.fuelleVariable(rpDrucken, "SNr.SzArt"+Integer.toString(hoechsteVerwendeteStimmzettelNr), "1");
                        rpVariablen.fuelleVariable(rpDrucken, "SNr.SzKuerzel"+Integer.toString(hoechsteVerwendeteStimmzettelNr), szKuerzel);
                        rpVariablen.fuelleVariable(rpDrucken, "W.Ueberschrift"+Integer.toString(hoechsteVerwendeteStimmzettelNr), szKuerzel);
                    }
                }
             }
            
            /*Schritt 2: nun die restlichen Stimmzettel, rückwärts ab dem höchsten, 
             * mit A bis Z, AA bis ZZ, etc. auffüllen*/
            CaBug.druckeLog("Schritt 2", logDrucken, 10);
            
            /*Gibt an, wieviele Buchstaben hintereinander gerade ausgedruckt werden,
             * 1=A, 2=AA, 3=AAA etc.
             */
            int faktorBuchstaben=1;
            /*Buchstabe, der gedruckt wird; 65=A; 90=Z;*/
            int zuDruckenderBuchstaben=64;
            /*Diese Buchstaben werden beim Druck nicht verwendet.
             * Achtung - Z darf nicht ausgeschlossen werden!*/
            int[] auszuschliessendeBuchstaben= {73, 74};
            
             for (int i=lDbBundle.param.paramAbstimmungParameter.stimmzetteldruckAnzahlStimmzettel;i>hoechsteVerwendeteStimmzettelNr;i--) {
 
                 /*Nächsten zu verwendenden Buchstaben verwenden*/
                 zuDruckenderBuchstaben++;
                 if (zuDruckenderBuchstaben==91) {
                     /*Z 90 erreicht, wieder mit A anfangen*/
                     zuDruckenderBuchstaben=65;
                     faktorBuchstaben++;
                 }
                 
                 
                 boolean gef;
                 do
                 {
                     gef=false;
                     for (int i1=0;i1<auszuschliessendeBuchstaben.length;i1++) {
                         if (auszuschliessendeBuchstaben[i1]==zuDruckenderBuchstaben) {
                             zuDruckenderBuchstaben++;
                             gef=true;
                         }
                     }
                 }
                 while (gef==true);
                 String ueberschrift="";
                 for (int i1=0;i1<faktorBuchstaben;i1++) {
                     ueberschrift=ueberschrift+String.valueOf( (char) zuDruckenderBuchstaben);
                 }
                 
                 CaBug.druckeLog("i="+i+" "+ueberschrift, zuDruckenderBuchstaben, kartenklasse);
                 rpVariablen.fuelleVariable(rpDrucken, "SNr.SzArt"+Integer.toString(i), "2");
                 rpVariablen.fuelleVariable(rpDrucken, "SNr.SzKuerzel"+Integer.toString(hoechsteVerwendeteStimmzettelNr), ueberschrift);
                 rpVariablen.fuelleVariable(rpDrucken, "W.Ueberschrift"+Integer.toString(hoechsteVerwendeteStimmzettelNr), "");
                 
             }
        }
    }
    
    private void stimmkarteDrucken(RpDrucken rpDrucken) {
        rpDrucken.druckenFormular();
        rpDrucken.druckenFormularSatzwechsel();
    }
}
