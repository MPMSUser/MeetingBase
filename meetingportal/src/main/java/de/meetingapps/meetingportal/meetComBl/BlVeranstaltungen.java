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
import de.meetingapps.meetingportal.meetComAllg.CaDateiWrite;
import de.meetingapps.meetingportal.meetComAllg.CaString;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclAktienregister;
import de.meetingapps.meetingportal.meetComEntities.EclAktienregisterErgaenzung;
import de.meetingapps.meetingportal.meetComEntities.EclMeldung;
import de.meetingapps.meetingportal.meetComEntities.EclPersonenNatJur;
import de.meetingapps.meetingportal.meetComEntities.EclVeranstaltung;
import de.meetingapps.meetingportal.meetComEntities.EclVeranstaltungenAktion;
import de.meetingapps.meetingportal.meetComEntities.EclVeranstaltungenElement;
import de.meetingapps.meetingportal.meetComEntities.EclVeranstaltungenElementDetail;
import de.meetingapps.meetingportal.meetComEntities.EclVeranstaltungenMenue;
import de.meetingapps.meetingportal.meetComEntities.EclVeranstaltungenQuittungElement;
import de.meetingapps.meetingportal.meetComEntities.EclVeranstaltungenReportElement;
import de.meetingapps.meetingportal.meetComEntities.EclVeranstaltungenVeranstaltung;
import de.meetingapps.meetingportal.meetComEntities.EclVeranstaltungenWert;
import de.meetingapps.meetingportal.meetComEntities.EclVorlaeufigeVollmachtEingabe;
import de.meetingapps.meetingportal.meetComEntities.EclVorlaeufigeVollmachtFuerAnzeige;
import de.meetingapps.meetingportal.meetComEntities.EclZutrittsIdent;
import de.meetingapps.meetingportal.meetComKonst.KonstAktienregisterErgaenzung;
import de.meetingapps.meetingportal.meetComKonst.KonstGruppen;
import de.meetingapps.meetingportal.meetComKonst.KonstPortalFunktionen;
import de.meetingapps.meetingportal.meetComStub.StubRoot;
import de.meetingapps.meetingportal.meetingCoreReport.RpDrucken;
import de.meetingapps.meetingportal.meetingCoreReport.RpVariablen;

/*************************************************Dokumentation für "neue" Veranstaltungen*********************************
 * 
 * Im BetterSmart-Portal, unter "Anmelden/Abmelden":
 * -------------------------------------------------
 * In TAuswahl1
 * blVeranstaltungen.erzeugeVeranstaltungslisteFuerTeilnehmer(BlVeranstaltungen.LAUT_MENUE_NUMMER, pMenue-Nummer=-1);
 * 
 * Es wird EclVeranstaltungenVeranstaltung durchsucht. 
 * Verglichen wird .menueNummer1, menueNummer2, menueNummer3
 * 
 * 
 * 
 * Im ku178 Mitgliederportal, unter Menüpunkt Veranstaltungen:
 * ------------------------------------------------------------
 * Menüpunkt Veranstaltungen: KonstPMenueFunktionscode.VERANSTALTUNGEN
 * (Menüpunkt .DIALOGVERANSTALTUNGEN wird nicht mehr genutzt)
 * 
 * In TVeranstaltungen.initMenue(true, pFunktionscodeSub):
 * blVeranstaltungen.erzeugeVeranstaltungslisteFuerMenue(pSubFunktion);
 * 
 * gelesen wird veranstaltungenMenue, Menünummer=pSubfunktion
 * 
 * 
 * 
 * Im uLogin, Servicline
 * ---------------------
 * blVeranstaltungen.erzeugeVeranstaltungslisteFuerTeilnehmer(BlVeranstaltungen.LAUT_MENUE_NUMMER, pMenue-Nummer=-2);
 * 
 * 
 **************************************************************************************************************************/


public class BlVeranstaltungen extends StubRoot {

    int logDrucken = 3;

    public EclVeranstaltung[] rcVeranstaltungArray = null;

    /**Anmeldung für mindestens 1 Veranstaltung liegt vor*/
    public boolean rcAngemeldet = false;
    /**Abmeldung für mindestens 1 Veranstaltung liegt vor*/
    public boolean rcAbgemeldet = false;
    public int rcAngemeldetAnzahlPersonen = 0;
    /**Nur verwenden, wenn nur eine Versammlung anmeldbar, ansonsten rcAngemeldetZuVeranstaltungenListe*/
    public EclVeranstaltung rcAngemeldetZuVeranstaltung = null;
    
    /**Enthält alle Veranstaltungen, die gerade aktiv sind und zu denen eine positive Anmeldung vorliegt*/
    public List<EclVeranstaltung> rcAngemeldetZuVeranstaltungenListe=null;

    public boolean rcUeberbucht = false;

    public BlVeranstaltungen(boolean pIstServer, DbBundle pDbBundle) {
        super(pIstServer, pDbBundle);
    }

    /**************************************Dialogveranstaltungen**************************************/
    /*1*/
    /**
     * Falls pIdent!=0, dann wird "ausgewaehlt" gemäß des Bundeslandes
     * des Aktionärs mit pIdent gefüllt.
     * 
     * Füllt:
     * rcVeranstaltungArray
     */
    public int leseAktiveVeranstaltungen(int pIdent) {
        CaBug.druckeLog("BlVeranstaltungen.leseAktiveVeranstaltungen", logDrucken, 10);
        dbOpenUndWeitere();

        lDbBundle.dbVeranstaltung.readAllAktive();
        rcVeranstaltungArray = lDbBundle.dbVeranstaltung.ergebnis();
        if (pIdent != 0) {
            lDbBundle.dbAktienregisterErgaenzung.readZuident(pIdent);
            String hBundesland = lDbBundle.dbAktienregisterErgaenzung
                    .ergebnisPosition(0).ergaenzungLangString[KonstAktienregisterErgaenzung.ku178_Bundesland];
            int bundesland = 0;
            if (!hBundesland.isEmpty()) {
                bundesland = Integer.parseInt(hBundesland);
            }
            CaBug.druckeLog("bundesland=" + bundesland, logDrucken, 10);

            for (int i = 0; i < rcVeranstaltungArray.length; i++) {
                if (rcVeranstaltungArray[i].istStandardFuerBundesland == bundesland) {
                    rcVeranstaltungArray[i].ausgewaehlt = true;
                }
            }
        }

        dbClose();

        return 1;
    }

    /*2*/
    /**Liefert den Status eines Aktionärs bzgl. Anmeldung zu einer Veranstaltung
     * Füllt:
     * rcAngemeldet
     * rcAbgemeldet
     * rcAngemeldetAnzahlPersonen
     * rcAngemeldetZuVeranstaltung
     */
    public int liefereAnmeldeStatus(int pAktionaersIdent) {
        dbOpenUndWeitere();

        rcAngemeldet = false;
        rcAbgemeldet = false;
        rcAngemeldetAnzahlPersonen = 0;
        rcAngemeldetZuVeranstaltung=null;
        rcAngemeldetZuVeranstaltungenListe=new LinkedList<EclVeranstaltung>();
        
        int angemeldet = -1;
        lDbBundle.dbAktienregisterErgaenzung.readZuident(pAktionaersIdent);
        EclAktienregisterErgaenzung lAktienregisterErgaenzung = lDbBundle.dbAktienregisterErgaenzung.ergebnisPosition(0);
        for (int i = 1; i < 20; i++) {
            String anmeldestatus=liefereAnmeldekennzeichen(lAktienregisterErgaenzung.ergaenzungKurzString[i]);
            boolean anOderAbgemeldetFuerDieseVeranstaltung=false;
            if (anmeldestatus.equals("1")) {
                angemeldet = i;
                rcAngemeldetAnzahlPersonen = lAktienregisterErgaenzung.ergaenzungKennzeichen[i];
                rcAngemeldet = true;
                anOderAbgemeldetFuerDieseVeranstaltung=true;
            }
            if (anmeldestatus.equals("2")) {
                angemeldet = i;
                rcAbgemeldet = true;
                anOderAbgemeldetFuerDieseVeranstaltung=true;
           }
            if (anOderAbgemeldetFuerDieseVeranstaltung) {
//              lDbBundle.dbVeranstaltung.readZuBundesland(angemeldet);
                lDbBundle.dbVeranstaltung.read(i);
                rcAngemeldetZuVeranstaltung = lDbBundle.dbVeranstaltung.ergebnisPosition(0);
                if (anmeldestatus.equals("1")){
                    if (rcAngemeldetZuVeranstaltung.aktiv>0) {
                        rcAngemeldetZuVeranstaltungenListe.add(rcAngemeldetZuVeranstaltung);
                    }
                }
            }
        }
        
//        if (angemeldet != -1) {
////            lDbBundle.dbVeranstaltung.readZuBundesland(angemeldet);
//            lDbBundle.dbVeranstaltung.read(angemeldet);
//            rcAngemeldetZuVeranstaltung = lDbBundle.dbVeranstaltung.ergebnisPosition(0);
//        }
        
        dbClose();
        return 1;
    }

    /*3*/
    public int widerrufeAnmeldung(int pAktionaersIdent, int pDurchAktionaerOderAdmin) {
        dbOpenUndWeitere();

        int angemeldet = -1;
        int personenzahl = 0;
        lDbBundle.dbAktienregisterErgaenzung.readZuident(pAktionaersIdent);
        EclAktienregisterErgaenzung lAktienregisterErgaenzung = lDbBundle.dbAktienregisterErgaenzung
                .ergebnisPosition(0);
        for (int i = 1; i < 20; i++) {
            String anmeldestatus=liefereAnmeldekennzeichen(lAktienregisterErgaenzung.ergaenzungKurzString[i]);
            if (anmeldestatus.substring(0,1).equals("1") || anmeldestatus.substring(0,1).equals("2")) {
                angemeldet = i;
                personenzahl = lAktienregisterErgaenzung.ergaenzungKennzeichen[i];

                lAktienregisterErgaenzung.ergaenzungKennzeichen[i] = 0;
                String hVorgang = widerrufVorgang(lAktienregisterErgaenzung.ergaenzungKurzString[i],
                        pDurchAktionaerOderAdmin);
                lAktienregisterErgaenzung.ergaenzungKurzString[i] = hVorgang;
                if (anmeldestatus.substring(0,1).equals("1")) {
                    reduziereAnmeldungen(i, personenzahl);
                    
                }
            }
        }
        CaBug.druckeLog("angemeldet="+angemeldet, logDrucken, 10);
        if (angemeldet != -1) {
            lDbBundle.dbAktienregisterErgaenzung.update(lAktienregisterErgaenzung);
        }
        dbClose();
        return 1;

    }

    /*4*/
    /**Belegt
     * rcUeberbucht
     * 
     * Achtung: Personenzahl bei anderen Veranstaltungen wird hier nicht verändert! D.h. es muß entweder
     * diese Funktion für jede Veranstaltung aufgerufen werden, oder vor einer erneuten Anmeldung muß widerrufeAnmeldung
     * aufgerufen werden.
     */
    public int anmeldung(int pAktionaersIdent, int pVeranstaltung, int pPersonenZahl, int pDurchAktionaerOderAdmin, int anOderAbmeldung) {
        dbOpenUndWeitere();
        rcUeberbucht = false;

        CaBug.druckeLog("pPersonenzahl="+pPersonenZahl, logDrucken, 10);
 
//        lDbBundle.dbVeranstaltung.read(pVeranstaltung);
//        int bundesland = lDbBundle.dbVeranstaltung.ergebnisPosition(0).istStandardFuerBundesland;
//
//        lDbBundle.dbAktienregisterErgaenzung.readZuident(pAktionaersIdent);
//        EclAktienregisterErgaenzung lAktienregisterErgaenzung = lDbBundle.dbAktienregisterErgaenzung.ergebnisPosition(0);
//        lAktienregisterErgaenzung.ergaenzungKennzeichen[bundesland] = pPersonenZahl;
//        String hVorgang = erhoeheVorgang(lAktienregisterErgaenzung.ergaenzungKurzString[bundesland], pDurchAktionaerOderAdmin);
//        lAktienregisterErgaenzung.ergaenzungKurzString[bundesland] = hVorgang;
//        lDbBundle.dbAktienregisterErgaenzung.update(lAktienregisterErgaenzung);


        lDbBundle.dbAktienregisterErgaenzung.readZuident(pAktionaersIdent);
        EclAktienregisterErgaenzung lAktienregisterErgaenzung = lDbBundle.dbAktienregisterErgaenzung.ergebnisPosition(0);
        lAktienregisterErgaenzung.ergaenzungKennzeichen[pVeranstaltung] = pPersonenZahl;
        
        String aktuellerAnmeldestatus=liefereAnmeldekennzeichen(lAktienregisterErgaenzung.ergaenzungKurzString[pVeranstaltung]);

         
        if (anOderAbmeldung==1) {
            if (!aktuellerAnmeldestatus.equals("1")) {
                /**Bisher noch nicht angemeldet*/
                int erg = erhoeheAnmeldungen(pVeranstaltung, pPersonenZahl);
                if (erg != 1) {
                    rcUeberbucht = true;
                    dbClose();
                    return 1;
                }
              
                lAktienregisterErgaenzung.ergaenzungKennzeichen[pVeranstaltung] = pPersonenZahl;
                
                String hVorgang = erhoeheVorgang(lAktienregisterErgaenzung.ergaenzungKurzString[pVeranstaltung], pDurchAktionaerOderAdmin, 1);
                lAktienregisterErgaenzung.ergaenzungKurzString[pVeranstaltung] = hVorgang;
            }
        }
        else {
            if (aktuellerAnmeldestatus.equals("1")) {
                /*Bisher angemeldet*/
                
                /*Dann Personenzahl reduzieren*/
                reduziereAnmeldungen(pVeranstaltung, pPersonenZahl);
            }
            lAktienregisterErgaenzung.ergaenzungKennzeichen[pVeranstaltung] = 0;
            
            String hVorgang = erhoeheVorgang(lAktienregisterErgaenzung.ergaenzungKurzString[pVeranstaltung], pDurchAktionaerOderAdmin, 2);
            lAktienregisterErgaenzung.ergaenzungKurzString[pVeranstaltung] = hVorgang;
        }
          
        
         lDbBundle.dbAktienregisterErgaenzung.update(lAktienregisterErgaenzung);

        dbClose();
        return 1;

    }

    private void reduziereAnmeldungen(int pVeranstaltung, int pPersonenzahl) {
        lDbBundle.dbVeranstaltung.updateReduziere(pVeranstaltung, pPersonenzahl);
    }

    private int erhoeheAnmeldungen(int pVeranstaltung, int pPersonenzahl) {
        return lDbBundle.dbVeranstaltung.updateErhoehe(pVeranstaltung, pPersonenzahl);
    }

    private String widerrufVorgang(String pVorgang, int pDurchAktionaerOderAdmin) {
        String neuVorgang = CaString.fuelleRechtsBlank(pVorgang, 3);
        neuVorgang = "0" + neuVorgang.substring(1);
        if (pDurchAktionaerOderAdmin == 1) {
            neuVorgang = neuVorgang.substring(0, 1) + "0" + neuVorgang.substring(2, 3);
        } else {
            neuVorgang = neuVorgang.substring(0, 2) + "0";
        }
        return neuVorgang;
    }

    private String liefereAnmeldekennzeichen(String pVorgang) {
        String neuVorgang=CaString.fuelleRechtsBlank(pVorgang, 3);
        return neuVorgang.substring(0, 1);
    }
    
    /**pVorgang = ergaenzungKurzString[] der Veranstaltung
     * 
     * Return=neuer String für ergaenzungKurzString[] der Veranstaltung
      */
    private String erhoeheVorgang(String pVorgang, int pDurchAktionaerOderAdmin, int pAnOderAbmelden) {
        String neuVorgang = CaString.fuelleRechtsBlank(pVorgang, 3);
        neuVorgang = Integer.toString(pAnOderAbmelden) + neuVorgang.substring(1);
        if (pDurchAktionaerOderAdmin == 1) {
            neuVorgang = neuVorgang.substring(0, 1) + Integer.toString(pAnOderAbmelden) + neuVorgang.substring(2, 3);
        } else {
            neuVorgang = neuVorgang.substring(0, 2) + Integer.toString(pAnOderAbmelden);
        }
        return neuVorgang;
    }

    /*********************************Generalversammlung******************/

    public boolean rcGVzweiPersonenMoeglich = false;
    public boolean rcGVgastkarteFuerMitgliedZulaessig = false;
    public boolean rcGVgastkarteFuerZweitePersonZulaessig=false;
    public boolean rcGVselbstAnmeldungOhneGesetzlichenVertreterMoeglich = false;
    
    /**Siehe KonstGruppen; -1 => nicht belegt*/
    public int rcGVgruppe=-1;

    /**Wert aus EclMeldung:
     * 0=bisher keine Aktion (exclusiv-Wert)
     * >0, <1000: mit x Personen angemeldet
     * 1024: abgemeldet (exclusiv-Wert)
     * 2048: Bevollmächtigten angemeldet
     * (exclusiv bedeutet: nicht in Kombination mit anderen Werten)
     */

    public int rcGVangemeldetNr = 0;

    public String rcNameVertreter1=""; 
    public String rcOrtVertreter1=""; 
    public String rcNameVertreter2=""; 
    public String rcOrtVertreter2="";
    public boolean rcGastkarteFuerMitglieder=false;
    public boolean rcGastkarteFuerZweitePerson=false;

    public int rcStatusPraesenz = 0;
    public int rcStatusPruefung = 0;

    /**Aktionär ist überhaupt an oder abgemeldet.
     * D.h. false=> hat noch keine Aktion gemacht
     */
    public boolean rcGVanOderAbgemeldet = false;

    /**true => ist überhaupt angemeldet (auch mit mehreren Personen, oder mit Bevollmächtigten*/
    public boolean rcGVangemeldet = false;

    /**true => ist online angemeldet (auch mit mehreren Personen; false=Standard bzw. Offline)*/
    public boolean rcGVOnlineangemeldet = false;

    /**Anmeldung ist für zwei Personen erfolgt*/
    public boolean rcGVzweiPersonenAngemeldet = false;

    /**Anmeldung ist für einen Bevollmächtigten erfolgt*/
    public boolean rcGVvertreterAngemeldet = false;

    /**true => hat sich abgemeldet*/
    public boolean rcGVabgemeldet = false;

    /**false => es ist noch keine Vollmacht eingegangen*/
    public boolean rcGVvollmachtAnDritteEingetragen = false;
    /**Aktuell eingetragene Vollmacht*/
    public EclVorlaeufigeVollmachtFuerAnzeige rcGVvollmachtAnDritteEingetragenEcl = null;

    /**false => es ist noch keine Vollmacht eingegangen*/
    public boolean rcGVvollmachtGesetzlichEingetragen = false;
    /**Aktuell eingetragene Vollmacht*/
    public EclVorlaeufigeVollmachtFuerAnzeige rcGVvollmachtGesetzlichEingetragenEcl = null;

    public List<EclVorlaeufigeVollmachtFuerAnzeige> rcGVlisteAllerVollmachten = null;

    public List<EclVorlaeufigeVollmachtFuerAnzeige> rcGVlisteAllerGesetzlichenVollmachten = null;
    public List<EclVorlaeufigeVollmachtFuerAnzeige> rcGVlisteAllerMitGesetzlichenVollmachten = null;
    public List<EclVorlaeufigeVollmachtFuerAnzeige> rcGVlisteAllerAnDritteVollmachten = null;

    /**Siehe Eclmeldung.zusatzfeld1:
     * 0 keine Prüfung erforderlich
     * -1 Prüfung erforderlich
     * -2 keine hinterlegte Vollmacht gefunden
     * 1 geprüft
     */
    public int rcGVvollmachtsPruefStatus=0;

    /**true => gesetzlicher Vertreter wurde im Portal eingetragen, der noch nicht geprüft ist*/
    public boolean rcGVgesetzlVertreterEingetragenNichtGeprueft=false;
    /**true => Bevollmächtigter wurde im Portal eingetragen, der noch nicht geprüft ist*/
    public boolean rcGVvertreterEingetragenNichtGeprueft=false;
    
    
    /**true => Anmeldung ist bereits ordnungsgemäß erfolgt, es ist eine
     * Online-Teilnahme möglich.
     * Ist der Fall, wenn für eine oder zwei Personen angemeldet, aber keine Anmeldung für Vertreter erfolgt
     */
    public boolean rcGVteilnahmeMoeglich = false;

    @Deprecated
    /**Wird nicht mehr gefüllt*/
    public int rcGVBundesland = 0;
    @Deprecated
    public int rcGVAngemeldet = 0;
    @Deprecated
    public boolean rcZweiPersonenMoeglich = false;

    /*5*/
    /**Liefert den Status eines Aktionärs bzgl. Anmeldung zu einer Veranstaltung
     * Füllt:
     * rcAngemeldetGV
     * rcGVOnlineangemeldet
     * rcBundesland
     * rcZweiPersonenMoeglich
     */
    public int gv_liefereAnmeldeStatus(EclAktienregister pEclAktienregister, EclMeldung pEclMeldung) {
        dbOpenUndWeitere();

        lDbBundle.dbAktienregisterErgaenzung.readZuident(pEclMeldung.aktienregisterIdent);
        EclAktienregisterErgaenzung lAktienregisterErgaenzung=lDbBundle.dbAktienregisterErgaenzung.ergebnisPosition(0);
        
        rcGastkarteFuerMitglieder=(lAktienregisterErgaenzung.ergaenzungKennzeichen[25]!=0);
        rcGastkarteFuerZweitePerson=(lAktienregisterErgaenzung.ergaenzungKennzeichen[26]!=0);
        rcNameVertreter1=lAktienregisterErgaenzung.ergaenzungLangString[KonstAktienregisterErgaenzung.ku178_VERTRETER1_NAME];
        rcOrtVertreter1=lAktienregisterErgaenzung.ergaenzungLangString[KonstAktienregisterErgaenzung.ku178_VERTRETER1_ORT];
        rcNameVertreter2=lAktienregisterErgaenzung.ergaenzungLangString[KonstAktienregisterErgaenzung.ku178_VERTRETER2_NAME];
        rcOrtVertreter2=lAktienregisterErgaenzung.ergaenzungLangString[KonstAktienregisterErgaenzung.ku178_VERTRETER2_ORT];
        
        rcStatusPraesenz=pEclMeldung.statusPraesenz;
       
        rcStatusPruefung=0;
        switch (pEclMeldung.zusatzfeld1) {
        case "-1":rcStatusPruefung=-1;break;
        case "-2":rcStatusPruefung=-2;break;
        }
        
        int gruppe = pEclAktienregister.gruppe;
        rcGVgruppe=gruppe;
        
        rcGVangemeldetNr = pEclMeldung.vorlAnmeldung;

        switch (pEclMeldung.zusatzfeld1) {
        case "":rcGVvollmachtsPruefStatus=0;break;
        case "-2":rcGVvollmachtsPruefStatus=-2;break;
        case "-1":rcGVvollmachtsPruefStatus=-1;break;
        case "1":rcGVvollmachtsPruefStatus=1;break;
        }
        
        if (lDbBundle.param.paramPortal.freiwilligeAnmeldungMitVertretereingabe==3) {
            /*Vollmachtserteilung im Portal bei Anmeldung möglich*/
            lDbBundle.dbVorlaeufigeVollmachtEingabe.readArtZuAktionaersnummer(pEclAktienregister.aktionaersnummer, EclVorlaeufigeVollmachtEingabe.ART_DER_EINGABE_GESETZLICHER_VERTRETER);
            int anz=lDbBundle.dbVorlaeufigeVollmachtEingabe.anzErgebnis();
            if (anz>0) {
                for (int i=0;i<anz;i++) {
                    EclVorlaeufigeVollmachtEingabe lVorlaeufigeVollmachtEingabe=lDbBundle.dbVorlaeufigeVollmachtEingabe.ergebnisPosition(i);
                    if (lVorlaeufigeVollmachtEingabe.wurdeStorniert==0 && 
                            lVorlaeufigeVollmachtEingabe.pruefstatus==0) {
                        rcGVgesetzlVertreterEingetragenNichtGeprueft=true;
                    }
                }
            }
            
            lDbBundle.dbVorlaeufigeVollmachtEingabe.readArtZuAktionaersnummer(pEclAktienregister.aktionaersnummer, EclVorlaeufigeVollmachtEingabe.ART_DER_EINGABE_BEVOLLMAECHTIGTER);
            anz=lDbBundle.dbVorlaeufigeVollmachtEingabe.anzErgebnis();
            if (anz>0) {
                for (int i=0;i<anz;i++) {
                    EclVorlaeufigeVollmachtEingabe lVorlaeufigeVollmachtEingabe=lDbBundle.dbVorlaeufigeVollmachtEingabe.ergebnisPosition(i);
                    if (lVorlaeufigeVollmachtEingabe.wurdeStorniert==0 && 
                            lVorlaeufigeVollmachtEingabe.pruefstatus==0) {
                        rcGVvertreterEingetragenNichtGeprueft=true;
                    }
                }
            }
           
            
        }
        
        CaBug.druckeLog("rcGVangemeldetNr="+rcGVangemeldetNr, logDrucken, 10);
        /*rcGVanOderAbgemeldet*/
        if (rcGVangemeldetNr != 0) {
            rcGVanOderAbgemeldet = true;
        } else {
            rcGVanOderAbgemeldet = false;
        }

        /*rcGVangemeldet*/
        if (rcGVangemeldetNr != 0 && rcGVangemeldetNr != 1024) {
            rcGVangemeldet = true;
        } else {
            rcGVangemeldet = false;
        }

        /*rcGVabgemeldet*/
        if (rcGVangemeldetNr == 1024) {
            rcGVabgemeldet = true;
        } else {
            rcGVabgemeldet = false;
        }

        /*rcGVOnlineangemeldet*/
        if ((rcGVangemeldetNr & 4096) == 4096) {
            rcGVOnlineangemeldet = true;
        } else {
            rcGVOnlineangemeldet = false;
        }

        /*rcGVzweiPersonenAngemeldet*/
        if ((rcGVangemeldetNr & 2) == 2) {
            rcGVzweiPersonenAngemeldet = true;
        } else {
            rcGVzweiPersonenAngemeldet = false;
        }

        /*rcGVvertreterAngemeldet*/
        if ((rcGVangemeldetNr & 2048) == 2048) {
            rcGVvertreterAngemeldet = true;
        } else {
            rcGVvertreterAngemeldet = false;
        }

        /*rcGVzweiPersonenMoeglich*/
        rcGVzweiPersonenMoeglich = KonstGruppen.anmeldenZweiPersonenZulaessig(gruppe, lDbBundle.param.paramPortal.freiwilligeAnmeldungZweiPersonenFuerMinderjaehrigeMoeglich);
        CaBug.druckeLog("rcGVzweiPersonenMoeglich="+rcGVzweiPersonenMoeglich, logDrucken, 10);
        rcGVgastkarteFuerMitgliedZulaessig = KonstGruppen.anmeldenGastkarteFuerMitgliedZulaessig(gruppe);
        rcGVgastkarteFuerZweitePersonZulaessig = KonstGruppen.anmeldenGastkarteFuerZweitePersonZulaessig(gruppe);

        /*rcGVselbstAnmeldungOhneGesetzlichenVertreterMoeglich*/
        rcGVselbstAnmeldungOhneGesetzlichenVertreterMoeglich = KonstGruppen
                .anmeldenOhneGesetzlichenVertreterZulaessig(gruppe);

        CaBug.druckeLog("lDbBundle.param.paramPortal.onlineTeilnahmeNurFuerFreiwilligAngemeldete="
                + lDbBundle.param.paramPortal.onlineTeilnahmeNurFuerFreiwilligAngemeldete, logDrucken, 10);
        if (((rcGVangemeldet == true || rcGVzweiPersonenAngemeldet == true)
                && rcGVselbstAnmeldungOhneGesetzlichenVertreterMoeglich && rcGVvertreterAngemeldet == false)
                || lDbBundle.param.paramPortal.onlineTeilnahmeNurFuerFreiwilligAngemeldete == 0) {
            rcGVteilnahmeMoeglich = true;
        }

        /*++++++Vollmachten++++++*/
        BlVorlaeufigeVollmacht blVorlaeufigeVollmacht = new BlVorlaeufigeVollmacht(lDbBundle);
        blVorlaeufigeVollmacht.liefereAusgehendeVollmachtenVonAktionaer(pEclAktienregister.aktienregisterIdent);
        rcGVvollmachtAnDritteEingetragen = blVorlaeufigeVollmacht.rcVollmachtGueltigAnDritteEingetragenVorhanden;
        rcGVvollmachtAnDritteEingetragenEcl = blVorlaeufigeVollmacht.rcVollmachtGueltigeAnDritteEingetragenFuerAnzeige;
        rcGVvollmachtGesetzlichEingetragen = blVorlaeufigeVollmacht.rcVollmachtGueltigGesetzlichEingetragenVorhanden;
        rcGVvollmachtGesetzlichEingetragenEcl = blVorlaeufigeVollmacht.rcVollmachtGueltigeGesetzlichEingetragenFuerAnzeige;

        rcGVlisteAllerVollmachten = blVorlaeufigeVollmacht.rcListAllerAnderenVollmachtenFuerAnzeige;
        rcGVlisteAllerGesetzlichenVollmachten = blVorlaeufigeVollmacht.rcListAllerAnderenGesetzlichenVollmachtenFuerAnzeige;
        rcGVlisteAllerMitGesetzlichenVollmachten = blVorlaeufigeVollmacht.rcListAllerMitGesetzlichenVollmachtenFuerAnzeige;
        rcGVlisteAllerAnDritteVollmachten = blVorlaeufigeVollmacht.rcListAllerAnderenAnDritteVollmachtenFuerAnzeige;

        dbClose();

        return 1;
    }

    /*6*/
    /**pAnOderAbmeldenOderZwei kann auch 0 sein
     * Verändert EclMeldung der meldeIdent in Datenbank - d.h. Status muß anschließend neu eingelesen werden.
     * pFunktion:
     * 1=1 Person angemeldet, 2=abgemeldet, 3=2 Personen angemeldet, 4=Bevollmächtigter angemeldet
     * pAbmeldenOderAnmeldenOderZwei=-1 => Abmelden; 1=Anmelden, 2=Anmelden 2 Personen*/
    @Deprecated
    public int gv_anAbmeldung(int meldeIdent, int pFunktion, int pDurchAktionaerOderAdmin) {
        dbOpen();

        lDbBundle.dbMeldungen.leseZuIdent(meldeIdent);
        EclMeldung lMeldung = lDbBundle.dbMeldungen.meldungenArray[0];
        switch (pFunktion) {
        case 1:
            lMeldung.vorlAnmeldung = 1;
            break; //1 Person angemeldet - Standard bzw. Offline
        case 2:
            lMeldung.vorlAnmeldung = 1024;
            break; //abgemeldet
        case 3:
            lMeldung.vorlAnmeldung = 2;
            break; //2 Personen angemeldet
        case 4:
            lMeldung.vorlAnmeldung = 2048;
            break; //vertreter angemeldet
        case 5:
            lMeldung.vorlAnmeldung = 4096+1;
            break; //Online angemeldet
       }
        if (pDurchAktionaerOderAdmin == 1) {
            lMeldung.vorlAnmeldungAkt = lMeldung.vorlAnmeldung;
        } else {
            lMeldung.vorlAnmeldungSer = lMeldung.vorlAnmeldung;
        }
        lDbBundle.dbMeldungen.update(lMeldung);

        dbClose();
        return 1;

    }

    /*7*/
    /**pAnOderAbmeldenOderZwei kann auch 0 sein
     * Verändert EclMeldung der meldeIdent in Datenbank - d.h. Status muß anschließend neu eingelesen werden.
     * pFunktion:
     * 1=1 Person angemeldet, 2=abgemeldet, 3=2 Personen angemeldet, 4=Bevollmächtigter angemeldet
     * pAbmeldenOderAnmeldenOderZwei=-1 => Abmelden; 1=Anmelden, 2=Anmelden 2 Personen
     * 
     * pVertreterAufGeprueftSetzen==true => keine Prüfung des Vertreters erforderlich (für uLogin z.B.)
     * 
     * Wichtiger Hinweis: funktioniert nur, wenn "immer neue Eintrittskartennummer" aktiviert ist.
     * */
    public int gv_anAbmeldung(int meldeIdent, int pFunktion, int pDurchAktionaerOderAdmin, int pFunktionAlt, boolean pSelbstAnmeldungOhneGesetzlichenVertreterMoeglich,
            String pNameVertreter1, String pOrtVertreter1, String pNameVertreter2, String pOrtVertreter2, boolean pGastkarteFuerMitglied, boolean pGastkarteFuerZweitePerson,
            String pNameVertreter1Alt, String pOrtVertreter1Alt, String pNameVertreter2Alt, String pOrtVertreter2Alt, boolean pGastkarteFuerMitgliedAlt, boolean pGastkarteFuerZweitePersonAlt,
            boolean pVertreterAufGeprueftSetzen
            
            ) {
        
        /*+++++++Übergebene Vertreter-und-Gast-Daten funktionsabhängig resetten, um ggf.+++++++++
         * Unterschiede zu ermitteln
         */
        pNameVertreter1=pNameVertreter1.trim();
        pOrtVertreter1=pOrtVertreter1.trim();
        pNameVertreter2=pNameVertreter2.trim();
        pOrtVertreter2=pOrtVertreter2.trim();
        
        if (pFunktion==4 || pFunktion==2) {
            pGastkarteFuerMitglied=false;
            pGastkarteFuerZweitePerson=false;
        }

        /**Ggf. Vertreterdaten auf leer setzen, wenn Funktionsmäßig vorgegeben*/
        if (pFunktion!=3 && pGastkarteFuerZweitePerson==false) {
            pNameVertreter2="";
            pOrtVertreter2="";
        }
        if (pSelbstAnmeldungOhneGesetzlichenVertreterMoeglich) {
            if (pFunktion!=4) {
                pNameVertreter1="";
                pOrtVertreter1="";
            }
        }
        else {
            /*Gesetzliche Vertreter erforderlich*/
            if (pFunktion!=1 && pFunktion!=3 && pFunktion !=4) {
                pNameVertreter1="";
                pOrtVertreter1="";
            }
        }
        
        
        dbOpen();

        /*Ggf bereits vorhandene Karte stornieren, wenn vorhanden (es ist nicht sichergestellt,
         * dass eine vorhanden ist!
         */
        boolean gastkarteStornieren=false;
        /*Hinweis: wenn vertreter1Stornieren, dann wird automatisch auch
         * die ZutrittsIdent zur eigentlichen Anmeldung storniert.
         * D.h. in diesem Fall muß ggf. dann auch eine neue Meldung erfolgen, wenn
         * nur der Vertreter widerrufen wurde, aber nicht die Anmeldung selbst.
         */
        boolean vertreter1Stornieren=false;
        boolean vertreter2Stornieren=false;
        boolean ekAnmeldungStornieren=false;
        
        boolean gastkarteNeu=false;
        boolean vertreter1Neu=false;
        boolean vertreter2Neu=false;
        boolean ekAnmeldungNeu=false;
        
        /**true => es wird aktuell (oder wurde bisher) ein Vertreter eingetragen,
         * der noch nicht geprüft wurde. D.h. falls pVertreterAufGeprueftSetzen==true,
         * muß in diesem Fall am Ende eine Willenserklärung abgesetzt werden
         */
        boolean bisherUngeprueft=false;
        
        /**Stornieren ermitteln*/
        if (pFunktion==2) {
            /*Nicht mehr Angemeldet*/
            gastkarteStornieren=true;
            ekAnmeldungStornieren=true;
        }
        
        if (!pNameVertreter1Alt.isEmpty() || pOrtVertreter1Alt.isEmpty()) {
            if (!pNameVertreter1.equals(pNameVertreter1Alt) || !pOrtVertreter1.equals(pOrtVertreter1Alt)) {
                vertreter1Stornieren=true;
            }
        }
        if (!pNameVertreter2Alt.isEmpty() || pOrtVertreter2Alt.isEmpty()) {
            if (!pNameVertreter2.equals(pNameVertreter2Alt) || !pOrtVertreter2.equals(pOrtVertreter2Alt)) {
                vertreter2Stornieren=true;
            }
        }
        
        if (pGastkarteFuerMitglied==false) {
            gastkarteStornieren=true;
        }
        
        
        /**Neu Erzeugen ermitteln*/
        if (pGastkarteFuerMitglied==true && pGastkarteFuerMitgliedAlt==false) {
            gastkarteNeu=true;
        }
        
        if (pFunktion==4) {
            /*Normaler Bevollmächtigter*/
            if (!pNameVertreter1.equals(pNameVertreter1Alt) || !pOrtVertreter1.equals(pOrtVertreter1Alt)) {
                vertreter1Neu=true;
            }
        }
        
        if (!pSelbstAnmeldungOhneGesetzlichenVertreterMoeglich) {
            if (pFunktion==1 || pFunktion==3) {
                /*1 oder 2 Personen => Vertreter 1 überprüfen*/
                if (!pNameVertreter1.equals(pNameVertreter1Alt) || !pOrtVertreter1.equals(pOrtVertreter1Alt)) {
                    vertreter1Neu=true;
                }
            }
            if (pFunktion==3 || pGastkarteFuerZweitePerson==true) {
                /*2 Personen => Vertreter 2 überprüfen*/
                if (!pNameVertreter2.equals(pNameVertreter2Alt) || !pOrtVertreter2.equals(pOrtVertreter2Alt)) {
                    vertreter2Neu=true;
                }
            }
        }

        if (pFunktion!=0 && pFunktion!=2) {
            /*Status neu auf jeden Fall angemeldet*/
            if (vertreter1Stornieren /* dann wird die EK storniert ... - neue EK anschließend erzeugen*/
                ||
                pFunktionAlt==0 || pFunktionAlt==2 /*Dann war bestand vorher nicht angemeldet*/)
                ekAnmeldungNeu=true;
        }
        
        lDbBundle.dbMeldungen.leseZuIdent(meldeIdent);
        EclMeldung lMeldung = lDbBundle.dbMeldungen.meldungenArray[0];
        int lAktienregisterIdent=lMeldung.aktienregisterIdent;
        lDbBundle.dbAktienregisterErgaenzung.readZuident(lAktienregisterIdent);
        EclAktienregisterErgaenzung lAktienregisterErgaenzung=lDbBundle.dbAktienregisterErgaenzung.ergebnisPosition(0);
        
        switch (pFunktion) {
        case 1:
            lMeldung.vorlAnmeldung = 1;
            break; //1 Person angemeldet - Standard bzw. Offline
        case 2:
            lMeldung.vorlAnmeldung = 1024;
            break; //abgemeldet
        case 3:
            lMeldung.vorlAnmeldung = 2;
            break; //2 Personen angemeldet
        case 4:
            lMeldung.vorlAnmeldung = 2048;
            break; //vertreter angemeldet
        case 5:
            lMeldung.vorlAnmeldung = 4096+1;
            break; //Online angemeldet
       }
        if (pDurchAktionaerOderAdmin == 1) {
            lMeldung.vorlAnmeldungAkt = lMeldung.vorlAnmeldung;
        } else {
            lMeldung.vorlAnmeldungSer = lMeldung.vorlAnmeldung;
        }
        
        
        /**Checken, ob Vollmacht anschließend geprüft werden muß*/
        if (vertreter1Neu || vertreter2Neu) {
            /*Neuer Vertreter eingetragen => erneute Prüfung erforderlich*/
            lMeldung.zusatzfeld1="-1";
        }
        if (vertreter1Stornieren && vertreter1Neu==false) {
            /*Vertretung storniert, keine neue eingetragen => keine Prüfung mehr erforderlich*/
            lMeldung.zusatzfeld1="";
        }
        
        if (pFunktion==4 && pFunktionAlt!=4) {
            /*Wechsel auf Bevollmächtigter*/
            lMeldung.zusatzfeld1="-1";
        }
        if (pSelbstAnmeldungOhneGesetzlichenVertreterMoeglich==false) {
            /*Wechsel auf Selbst, aber mit gesetzlichem Vertreter*/
            if (pFunktion==1 && pFunktionAlt!=1) {
                lMeldung.zusatzfeld1="-1";
            }
        }
        
        if (lMeldung.zusatzfeld1.equals("-1") || lMeldung.zusatzfeld1.equals("-2")) {
            bisherUngeprueft=true;
        }
        
        if (pVertreterAufGeprueftSetzen) {
            /*Egal was vorher belegt wurde - in diesem Fall erfolgt keine Prüfung.*/
            lMeldung.zusatzfeld1="";
        }
        /*muß an dieser Stelle erfolgen, da in den folgenden Schritten ggf.
         * der Meldungssatz verändert wird (EIntragen von EK ...)
         */
        lDbBundle.dbMeldungen.update(lMeldung);

        
        if (gastkarteStornieren) {
            int lGastNummer=lAktienregisterErgaenzung.ergaenzungKennzeichen[25];
            CaBug.druckeLog("gastkarteStornieren ausführen lGastNummer="+lGastNummer, logDrucken,  10);
            if (lGastNummer!=0) {
                /*Gastkarte stornieren*/
                CaBug.druckeLog("Gastkarte stornieren ="+lGastNummer, logDrucken, 10);
                lDbBundle.dbMeldungen.leseZuMeldungsIdent(lGastNummer);
                EclMeldung lGastMeldung=lDbBundle.dbMeldungen.meldungenArray[0];
                EclZutrittsIdent lZutrittsIdent=new EclZutrittsIdent();
                lZutrittsIdent.zutrittsIdent=lGastMeldung.zutrittsIdent;
                lZutrittsIdent.zutrittsIdentNeben="00";
                CaBug.druckeLog("lZutrittsIdent.zutrittsIdent="+lZutrittsIdent.zutrittsIdent, logDrucken,  10);
                if (!lZutrittsIdent.zutrittsIdent.isEmpty()) {
                    lZutrittsIdent.zutrittsIdentNeben="00";
                    BlWillenserklaerung lWillenserklaerung=new BlWillenserklaerung();
//                    lWillenserklaerung.piEclMeldungGast=lGastMeldung;
                    lWillenserklaerung.piKlasse=0;
                    lWillenserklaerung.piZutrittsIdent=lZutrittsIdent;
                    lWillenserklaerung.sperrenZutrittsIdent(lDbBundle);
                    if (!lWillenserklaerung.rcIstZulaessig) {
                        CaBug.drucke("001 lWillenserklaerung.rcIstZulaessig="+lWillenserklaerung.rcIstZulaessig+" lWillenserklaerung.rcGrundFuerUnzulaessig="+lWillenserklaerung.rcGrundFuerUnzulaessig);
                    }
                }
           }
        }
        if (vertreter2Stornieren) {
            int lGastNummer=lAktienregisterErgaenzung.ergaenzungKennzeichen[26];
            if (lGastNummer!=0) {
                /*2. Vertreter stornieren, ist ja auch eine Gastkarte*/
                lDbBundle.dbMeldungen.leseZuMeldungsIdent(lGastNummer);
                EclMeldung lGastMeldung=lDbBundle.dbMeldungen.meldungenArray[0];
                EclZutrittsIdent lZutrittsIdent=new EclZutrittsIdent();
                lZutrittsIdent.zutrittsIdent=lGastMeldung.zutrittsIdent;
                lZutrittsIdent.zutrittsIdentNeben="00";
                if (!lZutrittsIdent.zutrittsIdent.isEmpty()) {
                    lZutrittsIdent.zutrittsIdentNeben="00";
                    BlWillenserklaerung lWillenserklaerung=new BlWillenserklaerung();
//                    lWillenserklaerung.piEclMeldungGast=lGastMeldung;
                    lWillenserklaerung.piKlasse=0;
                    lWillenserklaerung.piZutrittsIdent=lZutrittsIdent;
                    lWillenserklaerung.sperrenZutrittsIdent(lDbBundle);
                    if (!lWillenserklaerung.rcIstZulaessig) {
                        CaBug.drucke("002 lWillenserklaerung.rcIstZulaessig="+lWillenserklaerung.rcIstZulaessig+" lWillenserklaerung.rcGrundFuerUnzulaessig="+lWillenserklaerung.rcGrundFuerUnzulaessig);
                    }
                }
            }
        }
        if (vertreter1Stornieren || ekAnmeldungStornieren) {
            if (!lMeldung.zutrittsIdent.isEmpty()) {
                CaBug.druckeLog("vertreter1Stornieren || ekAnmeldungStornieren: lMeldung.zutrittsIdent="+lMeldung.zutrittsIdent, pFunktionAlt, lAktienregisterIdent);
                /*Eintrittskarte zu Meldung stornieren - wird dann ggf. neu ausgestellt*/
                EclZutrittsIdent lZutrittsIdent=new EclZutrittsIdent();
                lZutrittsIdent.zutrittsIdent=lMeldung.zutrittsIdent;
                lZutrittsIdent.zutrittsIdentNeben="00";
                BlWillenserklaerung lWillenserklaerung=new BlWillenserklaerung();
                //                    lWillenserklaerung.piEclMeldungAktionaer=lMeldung;
                lWillenserklaerung.pWillenserklaerungGeberIdent=-1;
                lWillenserklaerung.piKlasse=1;
                lWillenserklaerung.piZutrittsIdent=lZutrittsIdent;
                lWillenserklaerung.sperrenZutrittsIdent(lDbBundle);
                if (!lWillenserklaerung.rcIstZulaessig) {
                    CaBug.drucke("003 lWillenserklaerung.rcIstZulaessig="+lWillenserklaerung.rcIstZulaessig+" lWillenserklaerung.rcGrundFuerUnzulaessig="+lWillenserklaerung.rcGrundFuerUnzulaessig);
                }
            }
        }
        
        /*Vertreterdaten und ggf. Gastdaten vorsorglich aus AktienregisterErgaenzung löschen 
         * (anschließend Vertreterdaten ggf. neu eintragen)
         * TODO Achtung - es muß noch überlegt werden, was mit bereits geprüften Vollmachten
         * erfolgt ...*/
        if (gastkarteStornieren) {
            lAktienregisterErgaenzung.ergaenzungKennzeichen[25]=0;
        }
        if (vertreter2Stornieren) {
            lAktienregisterErgaenzung.ergaenzungKennzeichen[26]=0;
            lAktienregisterErgaenzung.ergaenzungLangString[KonstAktienregisterErgaenzung.ku178_VERTRETER2_NAME]="";
            lAktienregisterErgaenzung.ergaenzungLangString[KonstAktienregisterErgaenzung.ku178_VERTRETER2_ORT]="";
        }
        if (vertreter1Stornieren) {
            lAktienregisterErgaenzung.ergaenzungLangString[KonstAktienregisterErgaenzung.ku178_VERTRETER1_NAME]="";
            lAktienregisterErgaenzung.ergaenzungLangString[KonstAktienregisterErgaenzung.ku178_VERTRETER1_ORT]="";
        }
        
 
        /*Nun die neuen Daten eintragen*/
        if (gastkarteNeu) {
            CaBug.druckeLog("gastkarteNeu Eintragen", logDrucken, 10);
            EclMeldung lGastMeldungNeu=new EclMeldung();
            lGastMeldungNeu.name=lMeldung.name;
            lGastMeldungNeu.vorname=lMeldung.vorname;
            lGastMeldungNeu.ort=lMeldung.ort;
      
            BlGastkarte blGastkarte=new BlGastkarte(lDbBundle);
            blGastkarte.pGast=lGastMeldungNeu;
            blGastkarte.pVersandart=3;
            blGastkarte.ausstellen();
            lAktienregisterErgaenzung.ergaenzungKennzeichen[25]=lGastMeldungNeu.meldungsIdent;
            CaBug.druckeLog("lGastMeldungNeu.meldungsIdent="+lGastMeldungNeu.meldungsIdent, logDrucken, 10);
        }
        if (vertreter2Neu) {
            CaBug.druckeLog("vertreter2Neu Eintragen", logDrucken, 10);
            EclMeldung lVertreter2MeldungNeu=new EclMeldung();
            lVertreter2MeldungNeu.name=pNameVertreter2;
            lVertreter2MeldungNeu.vorname="";
            lVertreter2MeldungNeu.ort=pOrtVertreter2;
      
            BlGastkarte blGastkarte=new BlGastkarte(lDbBundle);
            blGastkarte.pGast=lVertreter2MeldungNeu;
            blGastkarte.pVersandart=3;
            blGastkarte.ausstellen();
            lAktienregisterErgaenzung.ergaenzungKennzeichen[26]=lVertreter2MeldungNeu.meldungsIdent;
            CaBug.druckeLog("lVertreter2MeldungNeu.meldungsIdent="+lVertreter2MeldungNeu.meldungsIdent, logDrucken, 10);
            lAktienregisterErgaenzung.ergaenzungLangString[KonstAktienregisterErgaenzung.ku178_VERTRETER2_NAME]=pNameVertreter2;
            lAktienregisterErgaenzung.ergaenzungLangString[KonstAktienregisterErgaenzung.ku178_VERTRETER2_ORT]=pOrtVertreter2;
        }
        if (vertreter1Neu) {
            lAktienregisterErgaenzung.ergaenzungLangString[KonstAktienregisterErgaenzung.ku178_VERTRETER1_NAME]=pNameVertreter1;
            lAktienregisterErgaenzung.ergaenzungLangString[KonstAktienregisterErgaenzung.ku178_VERTRETER1_ORT]=pOrtVertreter1;
        }
        if (ekAnmeldungNeu) {
            BlWillenserklaerung lWillenserklaerung=new BlWillenserklaerung();
            lWillenserklaerung.pWillenserklaerungGeberIdent=-1;
            lWillenserklaerung.piMeldungsIdentAktionaer = lMeldung.meldungsIdent;
            lWillenserklaerung.pVersandartEK=3;
            lWillenserklaerung.neueZutrittsIdentZuMeldung(lDbBundle);
            if (!lWillenserklaerung.rcIstZulaessig) {
                CaBug.drucke("004 lWillenserklaerung.rcIstZulaessig="+lWillenserklaerung.rcIstZulaessig+" lWillenserklaerung.rcGrundFuerUnzulaessig="+lWillenserklaerung.rcGrundFuerUnzulaessig);
            }
         
        }
        
        lDbBundle.dbAktienregisterErgaenzung.update(lAktienregisterErgaenzung);

        CaBug.druckeLog("A pVertreterAufGeprueftSetzen="+pVertreterAufGeprueftSetzen, logDrucken, 10);
        if (pVertreterAufGeprueftSetzen && bisherUngeprueft) { 
             CaBug.druckeLog("B im if", logDrucken, 10);

            BlWillenserklaerung blWillenserklaerung=new BlWillenserklaerung();
            blWillenserklaerung.pWillenserklaerungGeberIdent=-1;
            blWillenserklaerung.piMeldungsIdentAktionaer=lMeldung.meldungsIdent;
            EclPersonenNatJur lPersonenNatJur=new EclPersonenNatJur();
            lPersonenNatJur.name=pNameVertreter1;
            lPersonenNatJur.ort=pOrtVertreter1;
            blWillenserklaerung.pEclPersonenNatJur=lPersonenNatJur;
            blWillenserklaerung.vollmachtAnDritte(lDbBundle);
            if (blWillenserklaerung.rcIstZulaessig==false) {
                CaBug.drucke("001 "+blWillenserklaerung.rcGrundFuerUnzulaessig);
            }
            
        }
        
        dbClose();
        return 1;

    }

    /******************************Neue Veranstaltungen**********************************************************/
    public List<EclVeranstaltungenVeranstaltung> rcVeranstaltungenListe=null;
    public boolean rcWeiterButtonAktiv=false;
    public String rcWeiterButtonBeschriftung="";
    
    /**1="Einstiegsquittung"
     * 2="Bestätigungsquittung"
     * (siehe auch EclVeranstaltungenQuittungElement)
     */
    public int rcQuittungsArt=2;
    
    public List<EclVeranstaltungenQuittungElement> rcVeranstaltungenQuittungListe=new LinkedList<EclVeranstaltungenQuittungElement>();

    public String rcFehlerText="";
    
    public String[] defaultWerte=null;

    /**Eingabewert*/
    public String loginKennung="SIMULATION";

    /**=true => für loginKennung sind bereits Werte vorhanden*/
    public boolean veranstaltungenWerteVorhanden=false;
    
    /**Wird als Ergebnis erzeugt (speichere*) bzw. als Zwischenspeicher (belege*)*/
    public List<EclVeranstaltungenWert> veranstaltungenWertListe=null;

    /*++++++++++++++++++++++++++++++++Veranstaltungen aufbereiten (ohne Werte für Teilnehmer)+++++++++++++++++++++++++++++*/
    final static public int LAUT_MENUE_NUMMER=1;
    final static public int LAUT_VERANSTALTUNGS_IDENT=2;

    /**Rückgabewert in rcVeranstaltungenListe*/
    public int erzeugeVeranstaltungslisteFuerTeilnehmer(int pMenueNummerOderVeranstaltungident, int pMenueNummer) {

        rcFehlerText="";
        
        rcVeranstaltungenListe=new LinkedList<EclVeranstaltungenVeranstaltung>();
        rcWeiterButtonAktiv=false;
        int gefunden=-1;
        int verarbeitetePosition=0;
        int gefundenePosition=0;
        List<EclVeranstaltungenVeranstaltung> lVeranstaltungenListe=lDbBundle.param.paramPortal.veranstaltungsListe;
        if (lVeranstaltungenListe==null) {
            CaBug.druckeLog("Veranstaltungsliste ist leer", logDrucken, 10);
            return 1;
        }
        
        do {
            gefunden=-1;
            gefundenePosition=99999;
            for (int i=0;i<lVeranstaltungenListe.size();i++) {
                EclVeranstaltungenVeranstaltung lVeranstaltung=lVeranstaltungenListe.get(i);
                if (pMenueNummerOderVeranstaltungident==LAUT_MENUE_NUMMER) {
                    int positionInMenue=pruefeObVeranstaltungInMenueUndAktiv(lVeranstaltung, pMenueNummer);
                    if (positionInMenue!=-1) {
                        if (positionInMenue>verarbeitetePosition && positionInMenue<gefundenePosition) {
                            gefunden=i;
                            gefundenePosition=positionInMenue;
                        }
                    }
                }
                else {
                    /**Wenn schon was verarbeitet, dann wurde die Veranstaltung schon gefunden und verarbeitet -> Abbruchbedingung*/
                    if (lVeranstaltung.identVeranstaltung==pMenueNummer && verarbeitetePosition==0) {
                        CaBug.druckeLog("Veranstaltung mit Ident="+pMenueNummer+" gefunden", logDrucken, 10);
                        gefunden=i;
                        gefundenePosition=1;
                    }
                    
                }
            }
            if (gefunden!=-1) {
                EclVeranstaltungenVeranstaltung lVeranstaltungNeu=new EclVeranstaltungenVeranstaltung(lVeranstaltungenListe.get(gefunden), defaultWerte);
                rcVeranstaltungenListe.add(lVeranstaltungNeu);
                
                /*Nun noch Weiter-Buttons belegen*/
                List<EclVeranstaltungenElement> lVeranstaltungenElementListe=lVeranstaltungNeu.veranstaltungenElementListe;
                if (lVeranstaltungenElementListe!=null) {
                    CaBug.druckeLog("auf Button durchsuchen 001", logDrucken, 10);
                    for (EclVeranstaltungenElement iVeranstaltungenElement:lVeranstaltungenElementListe) {
                        if (iVeranstaltungenElement.elementTyp==1001) {
                            CaBug.druckeLog("Button gefunden", logDrucken, 10);
                            if (iVeranstaltungenElement.wirdVerwendetWennVorgaengerAktivierungsStatusGleich==0 || iVeranstaltungenElement.wirdVerwendetWennVorgaengerAktivierungsStatusGleich==lVeranstaltungNeu.aktivierungsStatus) {
                                CaBug.druckeLog("Button aktiv = true", logDrucken, 10);
                                rcWeiterButtonAktiv=true;
                                rcWeiterButtonBeschriftung=iVeranstaltungenElement.textExtern;
                            }
                        }
                    }
                }
                
                
                
                verarbeitetePosition=gefundenePosition;
            }
            
        } while (gefunden!=-1);
        
        return 1;
    }
    
    private int pruefeObVeranstaltungInMenueUndAktiv(EclVeranstaltungenVeranstaltung pVeranstaltung, int pMenueNummer) {
        if (pVeranstaltung.aktivierungsStatus==0) {return -1;}
        if (pVeranstaltung.menueNummer1==pMenueNummer) {
            return pVeranstaltung.positionInMenue1;
        }
        if (pVeranstaltung.menueNummer2==pMenueNummer) {
            return pVeranstaltung.positionInMenue2;
        }
        if (pVeranstaltung.menueNummer3==pMenueNummer) {
            return pVeranstaltung.positionInMenue3;
        }
        return -1;
    }
    
   
//    /**Bestehende Liste in rcVeranstaltungenListe wird entsprechend ergänzt*/
//    public int belegeVeranstaltungslisteMitElementeZumAnzeigenFuerTeilnehmer() {
//        for (int i=0;i<rcVeranstaltungenListe.size();i++) {
//            EclVeranstaltungenVeranstaltung lVeranstaltungenVeranstaltung=rcVeranstaltungenListe.get(i);
//            int aktivierungsStatus=lVeranstaltungenVeranstaltung.aktivierungsStatus;
//            
//            lVeranstaltungenVeranstaltung.veranstaltungenElementAnzeigenListe=new LinkedList<EclVeranstaltungenElement>();
//            
//            for (int i1=0;i1<lVeranstaltungenVeranstaltung.veranstaltungenElementListe.size();i1++) {
//                EclVeranstaltungenElement lVeranstaltungenElement=lVeranstaltungenVeranstaltung.veranstaltungenElementListe.get(i1);
//                if (lVeranstaltungenElement.wirdVerwendetWennVorgaengerAktivierungsStatusGleich==0 || 
//                        lVeranstaltungenElement.wirdVerwendetWennVorgaengerAktivierungsStatusGleich==aktivierungsStatus) {
//                    
//                    
//                    lVeranstaltungenElement.baueRadioButtonListeAuf();
//
//                    lVeranstaltungenVeranstaltung.veranstaltungenElementAnzeigenListe.add(lVeranstaltungenElement);
//                    lVeranstaltungenElement.belegeMitElementeZumAnzeigenFuerTeilnehmer();
//                }
//                
//            }
//        }
//        return 1;
//    }
    
    
    /*+++++++++++++++++++++++++++++++++++++++++Eingaben prüfen++++++++++++++++++++++++++++++++++++++++*/
    public boolean pruefeVeranstaltungsliste(boolean pPruefeRange) {
        for (EclVeranstaltungenVeranstaltung iVeranstaltungenVeranstaltung: rcVeranstaltungenListe) {
            CaBug.druckeLog("prüfe Veranstaltung ="+iVeranstaltungenVeranstaltung.identVeranstaltung, logDrucken, 10);
            for (EclVeranstaltungenElement iVeranstaltungenElement: iVeranstaltungenVeranstaltung.veranstaltungenElementListe) {
                CaBug.druckeLog("prüfe Element ="+iVeranstaltungenElement.identElement, logDrucken, 10);
                boolean rc=pruefeVeranstaltungsElement(iVeranstaltungenElement, false, pPruefeRange);
                if (rc==false) {return false;}
            }
        }
        return true;
    }
    
    private boolean pruefeVeranstaltungsElement(EclVeranstaltungenElement pVeranstaltungenElement, boolean pFolgeElementeSicherAusgeblendet, boolean pPruefeRange) {
        boolean folgeElementeSicherAusgeblendet=false;
        if (pFolgeElementeSicherAusgeblendet) {
            folgeElementeSicherAusgeblendet=true;
            pVeranstaltungenElement.inLetzterVerarbeitungEnthalten=false;
        }
        else {
            if (pVeranstaltungenElement.elementAnzeigen()) {
                pVeranstaltungenElement.inLetzterVerarbeitungEnthalten=true;
                
                /*Hier nun ggf. Eingabefeld auf Zulässigkeit prüfen*/
                boolean eingabeZulaessig=pruefeVeranstaltungsElementEingabe(pVeranstaltungenElement, pPruefeRange);
                if (eingabeZulaessig==false) {
                    return false;
                }
            }
            else {
                folgeElementeSicherAusgeblendet=true;
                pVeranstaltungenElement.inLetzterVerarbeitungEnthalten=false;
            }
        }
        
        for (EclVeranstaltungenElement iVeranstaltungenElement: pVeranstaltungenElement.veranstaltungenElementListe) {
            CaBug.druckeLog("prüfe Element ="+iVeranstaltungenElement.identElement, logDrucken, 10);
            boolean rc=pruefeVeranstaltungsElement(iVeranstaltungenElement, folgeElementeSicherAusgeblendet, pPruefeRange);
            if (rc==false) {return false;}
        }
       
        return true;
    }
    
    /**pPruefeRange==false => es erfolgt keine Überprüfung von Von Bis Werten*/
    private boolean pruefeVeranstaltungsElementEingabe(EclVeranstaltungenElement pVeranstaltungenElement, boolean pPruefeRange) {
        if (pVeranstaltungenElement.elementTyp==3) {
            String eingabeWert=pVeranstaltungenElement.eingabeWert.trim();
            /*Eingabefeld Anzahl*/
            if (eingabeWert.isEmpty()) {
                if (pVeranstaltungenElement.eingabezwang==1) {
//                    zeigeFehlermeldungAn=true;
                    rcFehlerText=pVeranstaltungenElement.meldungWennUnzulaessigerWert;
                    return false;
                }
                eingabeWert="0";
            }
            if (!CaString.isNummern(eingabeWert)) {
//                zeigeFehlermeldungAn=true;
                rcFehlerText=pVeranstaltungenElement.meldungWennUnzulaessigerWert;
                return false;
            }
            int zahl=Integer.parseInt(eingabeWert);
            if (pPruefeRange==true) {
                if (zahl<pVeranstaltungenElement.minimalWert ||
                        (zahl>pVeranstaltungenElement.maximalWert && pVeranstaltungenElement.maximalWert!=0)) {
                    //                zeigeFehlermeldungAn=true;
                    rcFehlerText=pVeranstaltungenElement.meldungWennUnzulaessigerWert;
                    return false;
                }
            }
        }
        if (pVeranstaltungenElement.elementTyp==6) {
            if (pVeranstaltungenElement.eingabezwang==1) {
                if (pVeranstaltungenElement.eingabeWert==null || pVeranstaltungenElement.eingabeWert.isEmpty()) {
//                    zeigeFehlermeldungAn=true;
                    rcFehlerText=pVeranstaltungenElement.meldungWennUnzulaessigerWert;
                    return false;
                }
            }
        }
        
        return true;
    }
    
    /*+++++++++++++++++++++++++++++++++++Aktionen ausführen++++++++++++++++++++++++++++++++++++++++++++++*/
    public void aktionenAusfuehren(){
        for (EclVeranstaltungenVeranstaltung iVeranstaltungenVeranstaltung: rcVeranstaltungenListe) {
            CaBug.druckeLog("führe aus Veranstaltung ="+iVeranstaltungenVeranstaltung.identVeranstaltung, logDrucken, 10);
            for (EclVeranstaltungenElement iVeranstaltungenElement: iVeranstaltungenVeranstaltung.veranstaltungenElementListe) {
                CaBug.druckeLog("fuehre aus Element ="+iVeranstaltungenElement.identElement, logDrucken, 10);
                aktionenAusfuehrenElement(iVeranstaltungenElement);
            }
        }
        
    }
    
   private void aktionenAusfuehrenElement(EclVeranstaltungenElement pVeranstaltungenElement) {
       /**Aktionen für Element ausführen*/
       if (pVeranstaltungenElement.veranstaltungenAktionListe!=null && pVeranstaltungenElement.veranstaltungenAktionListe.size()!=0) {
           for (EclVeranstaltungenAktion iVeranstaltungenAktion:pVeranstaltungenElement.veranstaltungenAktionListe) {
               if (iVeranstaltungenAktion.aktion==1) {
                   int anzahlGastkarten=0;
                   if (pVeranstaltungenElement.eingabeWert!=null && CaString.isNummern(pVeranstaltungenElement.eingabeWert)) {
                       anzahlGastkarten=Integer.parseInt(pVeranstaltungenElement.eingabeWert);
                   }

                   int anzahlGastkartenAlt=0;
                   if (pVeranstaltungenElement.eingabeWertAlt!=null && CaString.isNummern(pVeranstaltungenElement.eingabeWertAlt)) {
                       anzahlGastkartenAlt=Integer.parseInt(pVeranstaltungenElement.eingabeWertAlt);
                   }
                   CaBug.druckeLog("anzahlGastkarten="+anzahlGastkarten+" anzahlGastkartenAlt="+anzahlGastkartenAlt, logDrucken, 10);

                   if (pVeranstaltungenElement.inLetzterVerarbeitungEnthalten && anzahlGastkartenAlt!=anzahlGastkarten) {
                       /*Neutrale Gastkarte für Anzahl*/
                       iVeranstaltungenAktion.ergebnisDerAktion=new LinkedList<String>();
                       for (int i=0;i<anzahlGastkarten;i++) {
                           String hErgebnis=aktionNeutraleGastkarte();
                           iVeranstaltungenAktion.ergebnisDerAktion.add(hErgebnis);
                       }
                   }
                   if ((anzahlGastkartenAlt!=anzahlGastkarten || pVeranstaltungenElement.inLetzterVerarbeitungEnthalten==false) && anzahlGastkartenAlt!=0) {
                       /*Alte Gastkarten stornieren*/
                       for (String iEk: iVeranstaltungenAktion.ergebnisDerAktionAlt) {
                           aktionNeutraleGastkarteWiderrufen(iEk);
                       }
                   }
               }
           }
       }

       /**Aktionen für Detail-Element ausführen*/
       for (EclVeranstaltungenElementDetail iVeranstaltungenElementDetail: pVeranstaltungenElement.veranstaltungenElementDetailListe) {
           for (EclVeranstaltungenAktion iVeranstaltungenAktion:iVeranstaltungenElementDetail.veranstaltungenAktionListe) {
               if (iVeranstaltungenAktion.aktion==1) {
                   if (pVeranstaltungenElement.inLetzterVerarbeitungEnthalten &&
                           iVeranstaltungenElementDetail.geradeSelektiert && iVeranstaltungenElementDetail.geradeSelektiertAlterWert==false) {
                       /*Neutrale Gastkarte für Anzahl*/
                       iVeranstaltungenAktion.ergebnisDerAktion=new LinkedList<String>();
                       int anzahlGastkarten=Integer.parseInt(iVeranstaltungenAktion.parameter1);
                       for (int i=0;i<anzahlGastkarten;i++) {
                           String hErgebnis=aktionNeutraleGastkarte();
                           iVeranstaltungenAktion.ergebnisDerAktion.add(hErgebnis);
                       }
                   }
                   if ((pVeranstaltungenElement.inLetzterVerarbeitungEnthalten==false &&
                            iVeranstaltungenElementDetail.geradeSelektiertAlterWert==true)   
                           || 
                       (pVeranstaltungenElement.inLetzterVerarbeitungEnthalten==true &&
                           iVeranstaltungenElementDetail.geradeSelektiert==false && iVeranstaltungenElementDetail.geradeSelektiertAlterWert==true)
                           ) {
                       for (String iEk: iVeranstaltungenAktion.ergebnisDerAktionAlt) {
                           aktionNeutraleGastkarteWiderrufen(iEk);
                       }
                       
                   }
               }
           }
       }
      
       for (EclVeranstaltungenElement iVeranstaltungenElement: pVeranstaltungenElement.veranstaltungenElementListe) {
           CaBug.druckeLog("fuehre aus Element ="+iVeranstaltungenElement.identElement, logDrucken, 10);
           aktionenAusfuehrenElement(iVeranstaltungenElement);
       }

   }
    
   private String aktionNeutraleGastkarte() {
       BlGastkarte blGastkarte=new BlGastkarte(lDbBundle);
       EclMeldung meldungNeuerGast=new EclMeldung();
       
       meldungNeuerGast.name="Mitgliedertreffen";
       meldungNeuerGast.vorname=BlNummernformBasis.aufbereitenInternFuerExtern(loginKennung, lDbBundle);
       
       blGastkarte.pGast=meldungNeuerGast;
       blGastkarte.berechtigungsWert=KonstPortalFunktionen.berechtigungsbit(KonstPortalFunktionen.gast9);
       blGastkarte.pVersandart=1;
       int rc=blGastkarte.ausstellen();
       CaBug.druckeLog("Zutrittsident="+blGastkarte.rcZutrittsIdent+ "rc="+rc, logDrucken, 10);
       return blGastkarte.rcZutrittsIdent;
   }
   
   private void aktionNeutraleGastkarteWiderrufen(String pEK) {
       CaBug.druckeLog("pEK="+pEK, logDrucken, 10);
       BlGastkarte blGastkarte=new BlGastkarte(lDbBundle);
       blGastkarte.pZutrittsIdentStorno=pEK;
       blGastkarte.pZutrittsIdentNebenStorno="00";
       
       boolean brc=blGastkarte.pruefeObGKNrVorhandenUndGueltig();
       if (brc) {
       
           int rc=blGastkarte.stornoGKNr();
           CaBug.druckeLog("rc="+rc, logDrucken, 10 );
       }
   }
   
   
   /*+++++++++++++++++++++++++++++Quittung aufbereiten+++++++++++++++++++++++++++++++++++++++++++++++*/
   
   public void aufbereitenQuittung() {
       rcVeranstaltungenQuittungListe=new LinkedList<EclVeranstaltungenQuittungElement>();
       
       List<EclVeranstaltungenQuittungElement> veranstaltungenQuittungListeQuelle=
       lDbBundle.param.paramPortal.veranstaltungenQuittungListe;


       for (EclVeranstaltungenQuittungElement iVeranstaltungenQuittungElement:veranstaltungenQuittungListeQuelle) {
           EclVeranstaltungenQuittungElement lVeranstaltungenQuittungElement=new EclVeranstaltungenQuittungElement(iVeranstaltungenQuittungElement);
           CaBug.druckeLog("Quittungs-Ident="+iVeranstaltungenQuittungElement.identQuittungElement, logDrucken, 10);
           boolean brc=aufbereitenQuittungVeranstaltung(lVeranstaltungenQuittungElement);
           CaBug.druckeLog("brc="+brc, logDrucken, 10);
           if (brc) {
               switch (lVeranstaltungenQuittungElement.quittungTyp) {
               case 9:
                   CaBug.druckeLog("Aktion 9", logDrucken, 10);
                   if (lVeranstaltungenQuittungElement.verweisAufZugehoerigesElementDetail!=null) {
                       CaBug.druckeLog("Aktion 9 - Element Detail", logDrucken, 10);
                       /*Verweis auf Detailelement - Haken oder ähnliches*/
                       for (EclVeranstaltungenAktion iVeranstaltungenAktion:lVeranstaltungenQuittungElement.verweisAufZugehoerigesElementDetail.veranstaltungenAktionListe) {
                           if (iVeranstaltungenAktion.aktion==1) {
                               int anzahlAktionen=iVeranstaltungenAktion.ergebnisDerAktion.size();
                               int aktionsOffset=1;
                               for (String iString:iVeranstaltungenAktion.ergebnisDerAktion) {
                                   EclVeranstaltungenQuittungElement neuesVeranstaltungenQuittungElement=new EclVeranstaltungenQuittungElement(iVeranstaltungenQuittungElement);
                                   neuesVeranstaltungenQuittungElement.wertParameter=iString;
                                   if (anzahlAktionen==1) {
                                       neuesVeranstaltungenQuittungElement.buttonBezeichnungKomplett=neuesVeranstaltungenQuittungElement.buttonBezeichnung;
                                   }
                                   else {
                                       neuesVeranstaltungenQuittungElement.buttonBezeichnungKomplett=neuesVeranstaltungenQuittungElement.buttonBezeichnung
                                               +" "+Integer.toString(aktionsOffset);
                                   }
                                   rcVeranstaltungenQuittungListe.add(neuesVeranstaltungenQuittungElement);
                                   aktionsOffset++;
                               }
                           }
                       }
                   }
                   else {
                       /**Anzahl Element*/
                       CaBug.druckeLog("Aktion 9 - Element", logDrucken, 10);
                      /*Dann gehört das zu einem Element*/
                       for (EclVeranstaltungenAktion iVeranstaltungenAktion:lVeranstaltungenQuittungElement.verweisAufZugehoerigesElement.veranstaltungenAktionListe) {
                           CaBug.druckeLog("iVeranstaltungenAktion.aktion="+iVeranstaltungenAktion.aktion, logDrucken, 10);
                           if (iVeranstaltungenAktion.aktion==1) {
                               CaBug.druckeLog("iVeranstaltungenAktion.aktion==1", logDrucken, 10);
                               int anzahlAktionen=iVeranstaltungenAktion.ergebnisDerAktion.size();
                               int aktionsOffset=1;
                               for (String iString:iVeranstaltungenAktion.ergebnisDerAktion) {
                                   EclVeranstaltungenQuittungElement neuesVeranstaltungenQuittungElement=new EclVeranstaltungenQuittungElement(iVeranstaltungenQuittungElement);
                                   neuesVeranstaltungenQuittungElement.wertParameter=iString;
                                   if (anzahlAktionen==1) {
                                       neuesVeranstaltungenQuittungElement.buttonBezeichnungKomplett=neuesVeranstaltungenQuittungElement.buttonBezeichnung;
                                   }
                                   else {
                                       neuesVeranstaltungenQuittungElement.buttonBezeichnungKomplett=neuesVeranstaltungenQuittungElement.buttonBezeichnung
                                               +" "+Integer.toString(aktionsOffset);
                                   }
                                   rcVeranstaltungenQuittungListe.add(neuesVeranstaltungenQuittungElement);
                                   aktionsOffset++;
                               }
                           }
                       }
                   }
                   break;
               default:
                   CaBug.druckeLog("Standard-Aktion", logDrucken, 10);
                   rcVeranstaltungenQuittungListe.add(lVeranstaltungenQuittungElement);
                   break;
               }
           }
       }
   }
   
   private boolean aufbereitenQuittungVeranstaltung(EclVeranstaltungenQuittungElement pVeranstaltungenQuittungElement) {
       if (pVeranstaltungenQuittungElement.wirdVerwendetBeiQuittung!=0 && pVeranstaltungenQuittungElement.wirdVerwendetBeiQuittung!=rcQuittungsArt) {
           return false;
       }
       for (EclVeranstaltungenVeranstaltung iVeranstaltungenVeranstaltung: rcVeranstaltungenListe) {
           /*Veranstaltung selbst?*/
           if (pVeranstaltungenQuittungElement.zuIdentElement==0 && 
                   pVeranstaltungenQuittungElement.zuIdentElementDetail==0 &&
                   pVeranstaltungenQuittungElement.identVeranstaltung==iVeranstaltungenVeranstaltung.identVeranstaltung
                   ) {
               if (pVeranstaltungenQuittungElement.wirdVerwendetWennVeranstaltungAktivierungsStatusGleich==iVeranstaltungenVeranstaltung.aktivierungsStatus) {
                   return true;
               }
           }
           /*Alle Elemente durch?*/
           for (EclVeranstaltungenElement iVeranstaltungenElement: iVeranstaltungenVeranstaltung.veranstaltungenElementListe) {
               boolean rc=aufbereitenQuittungElement(pVeranstaltungenQuittungElement, iVeranstaltungenElement);
               if (rc==true) {return true;}
           }
       }

       return false;
   }
   
   private boolean aufbereitenQuittungElement(EclVeranstaltungenQuittungElement pVeranstaltungenQuittungElement, EclVeranstaltungenElement pVeranstaltungenElement) {
       if (pVeranstaltungenElement.inLetzterVerarbeitungEnthalten==false) {return false;}
       if (pVeranstaltungenQuittungElement.zuIdentElementDetail==0 && pVeranstaltungenQuittungElement.zuIdentElement==pVeranstaltungenElement.identElement) {
           /**Quittungselement gehört zu diesem Element*/
           CaBug.druckeLog("Gehört zu diesem Element "+pVeranstaltungenElement.identElement, logDrucken, 10);

           String hEingabewert=pVeranstaltungenElement.eingabeWert;
           int iEingabewert=0;
           if (hEingabewert!=null && CaString.isNummern(hEingabewert)) {
               iEingabewert=Integer.parseInt(hEingabewert);
           }

           CaBug.druckeLog("iEingabewert="+iEingabewert, logDrucken, 10);
           String hEingabewertAlt=pVeranstaltungenElement.eingabeWertAlt;
           int iEingabewertAlt=0;
           if (hEingabewertAlt!=null && CaString.isNummern(hEingabewertAlt)) {
               iEingabewertAlt=Integer.parseInt(hEingabewertAlt);
           }

           if (pVeranstaltungenQuittungElement.wirdVerwendetWennEingabeWertVeraendert!=0) {
               /*Verwendung, wenn sich Eingabewert verändert hat*/
               CaBug.druckeLog("pVeranstaltungenQuittungElement.wirdVerwendetWennEingabeWertVeraendert="+pVeranstaltungenQuittungElement.wirdVerwendetWennEingabeWertVeraendert
                       +" iEingabewert="+iEingabewert+" iEingabewertAlt="+iEingabewertAlt, logDrucken, 10);
               if ( /*Eingabewert gleich geblieben*/
                       (pVeranstaltungenQuittungElement.wirdVerwendetWennEingabeWertVeraendert==1 && iEingabewert==iEingabewertAlt)
                       ||
                       /*Eingabwert verändert*/
                       (pVeranstaltungenQuittungElement.wirdVerwendetWennEingabeWertVeraendert==2 && iEingabewert!=iEingabewertAlt)
                       ||
                       /*Eingabwert von leer auf nicht leer*/
                       (pVeranstaltungenQuittungElement.wirdVerwendetWennEingabeWertVeraendert==3 && iEingabewertAlt==0 && iEingabewert!=0)
                       ||
                       /*Eingabwert von nicht leer auf leer*/
                       (pVeranstaltungenQuittungElement.wirdVerwendetWennEingabeWertVeraendert==4 && iEingabewertAlt!=0 && iEingabewert==0)
                       ||
                       /*Eingabwert von nicht leer auf leer*/
                       (pVeranstaltungenQuittungElement.wirdVerwendetWennEingabeWertVeraendert==5 && iEingabewertAlt!=0 && iEingabewert!=iEingabewertAlt)
                       )
               {


                   pVeranstaltungenQuittungElement.verweisAufZugehoerigesElement=pVeranstaltungenElement;
                   pVeranstaltungenQuittungElement.eingabeWert=pVeranstaltungenElement.eingabeWert;
                   return true;
               }
               return false;
           }
           if (pVeranstaltungenQuittungElement.wirdVerwendetWennEingabeGleich==0) {
               pVeranstaltungenQuittungElement.verweisAufZugehoerigesElement=pVeranstaltungenElement;
               pVeranstaltungenQuittungElement.eingabeWert=pVeranstaltungenElement.eingabeWert;
               return true;
           }
           if ((pVeranstaltungenQuittungElement.wirdVerwendetWennEingabeGleich>0 &&
                   iEingabewert==pVeranstaltungenQuittungElement.wirdVerwendetWennEingabeGleich) ||
                   (pVeranstaltungenQuittungElement.wirdVerwendetWennEingabeGleich<0 &&
                           iEingabewert!=pVeranstaltungenQuittungElement.wirdVerwendetWennEingabeGleich*(-1))
                   ) {
               pVeranstaltungenQuittungElement.verweisAufZugehoerigesElement=pVeranstaltungenElement;
               pVeranstaltungenQuittungElement.eingabeWert=pVeranstaltungenElement.eingabeWert;
               return true;
           }
           /*Quittungselement gehört zu diesem Element, aber paßt nicht*/
           return false;
       }
       if (pVeranstaltungenQuittungElement.zuIdentElementDetail!=0) {
           /*Detail-Elemente überprüfen*/
           for (EclVeranstaltungenElementDetail iVeranstaltungenElementDetail: pVeranstaltungenElement.veranstaltungenElementDetailListe) {
               boolean rc=aufbereitenQuittungElementDetail(pVeranstaltungenQuittungElement, iVeranstaltungenElementDetail);
               if (rc==true) {
                   pVeranstaltungenQuittungElement.verweisAufZugehoerigesElementDetail=iVeranstaltungenElementDetail;
                   return true;}
           }
       }
       /*Sub-Element-Liste durcharbeiten*/
       for (EclVeranstaltungenElement iVeranstaltungenElement: pVeranstaltungenElement.veranstaltungenElementListe) {
           boolean rc=aufbereitenQuittungElement(pVeranstaltungenQuittungElement, iVeranstaltungenElement);
           if (rc==true) {return true;}
       }

       return false;
   }
   
   
   private boolean aufbereitenQuittungElementDetail(EclVeranstaltungenQuittungElement pVeranstaltungenQuittungElement, EclVeranstaltungenElementDetail pVeranstaltungenElementDetail) {
       if (pVeranstaltungenQuittungElement.zuIdentElementDetail!=pVeranstaltungenElementDetail.identDetail) {
           /*Gehört nicht zu diesem Detail*/
           return false;
       }
       if ((pVeranstaltungenQuittungElement.wirdVerwendetWennEingabeGleich==1 && pVeranstaltungenElementDetail.geradeSelektiert)
               ||
               (pVeranstaltungenQuittungElement.wirdVerwendetWennEingabeGleich==-1 && pVeranstaltungenElementDetail.geradeSelektiert==false)   
               ) {
           return true;
       }
       return false;
   }

   /****************************Speichern Wert**********************************************/
   public List<String> rcMailBetreff=null;
   public List<String> rcMailText=null;
   public List<String> rcMailAnhang=null;
   
   public boolean speichereWerteVeranstaltungsliste() {
       veranstaltungenWertListe=new LinkedList<EclVeranstaltungenWert>();
       rcMailBetreff=new LinkedList<String>();
       rcMailText=new LinkedList<String>();
       rcMailAnhang=new LinkedList<String>();
       
       
       for (EclVeranstaltungenVeranstaltung iVeranstaltungenVeranstaltung: rcVeranstaltungenListe) {
           CaBug.druckeLog("speichere Werte Veranstaltung ="+iVeranstaltungenVeranstaltung.identVeranstaltung, logDrucken, 10);
           for (EclVeranstaltungenElement iVeranstaltungenElement: iVeranstaltungenVeranstaltung.veranstaltungenElementListe) {
               CaBug.druckeLog("prüfe Element ="+iVeranstaltungenElement.identElement, logDrucken, 10);
               speichereWerteVeranstaltungsElement(iVeranstaltungenElement, false);
           }
       }
       lDbBundle.dbVeranstaltungenWert.updateLoginKennung(loginKennung, veranstaltungenWertListe);
       return true;
   }
   
   private void speichereWerteVeranstaltungsElement(EclVeranstaltungenElement pVeranstaltungenElement, boolean pFolgeElementeSicherAusgeblendet) {
       boolean folgeElementeSicherAusgeblendet=false;
       if (pFolgeElementeSicherAusgeblendet) {
           folgeElementeSicherAusgeblendet=true;
           pVeranstaltungenElement.inLetzterVerarbeitungEnthalten=false;
       }
       else {
           if (pVeranstaltungenElement.elementAnzeigen()) {
               pVeranstaltungenElement.inLetzterVerarbeitungEnthalten=true;
               
               /*Hier nun ggf. Eingabefeld-Wert auf Zulässigkeit speichern*/
               speichereWerteVeranstaltungsElementEingabe(pVeranstaltungenElement);
           }
           else {
               folgeElementeSicherAusgeblendet=true;
               pVeranstaltungenElement.inLetzterVerarbeitungEnthalten=false;
           }
       }
       
       for (EclVeranstaltungenElement iVeranstaltungenElement: pVeranstaltungenElement.veranstaltungenElementListe) {
           CaBug.druckeLog("prüfe Element ="+iVeranstaltungenElement.identElement, logDrucken, 10);
           speichereWerteVeranstaltungsElement(iVeranstaltungenElement, folgeElementeSicherAusgeblendet);
       }
      
       return;
   }
   
   private void speichereWerteVeranstaltungsElementEingabe(EclVeranstaltungenElement pVeranstaltungenElement) {
       CaBug.druckeLog("pVeranstaltungenElement.elementTyp=="+pVeranstaltungenElement.elementTyp,logDrucken, 10);
       if (pVeranstaltungenElement.elementTyp==3) {
           /*Eingabefeld Anzahl*/
           String eingabeWert=pVeranstaltungenElement.eingabeWert.trim();
           /*Grund-Wert-Satz aufbereiten*/
           EclVeranstaltungenWert lVeranstaltungenWert=new EclVeranstaltungenWert();
           lVeranstaltungenWert.loginKennung=loginKennung;
           lVeranstaltungenWert.identVeranstaltung=pVeranstaltungenElement.identVeranstaltung;
           lVeranstaltungenWert.identElement=pVeranstaltungenElement.identElement;
           lVeranstaltungenWert.eingabeWert=eingabeWert;
           veranstaltungenWertListe.add(lVeranstaltungenWert);
           
           /*Nun ggf. für alle Aktionen entsprechende Ergebniseinträge erzeugen*/
           if (pVeranstaltungenElement.veranstaltungenAktionListe!=null) {
               for (EclVeranstaltungenAktion iVeranstaltungenAktion:pVeranstaltungenElement.veranstaltungenAktionListe) {
                  if (iVeranstaltungenAktion.aktion==1) {
                      int lfdErgebnisnrInAktion=0;
                      for (String iErgebnisDerAktion:iVeranstaltungenAktion.ergebnisDerAktion) {
                          EclVeranstaltungenWert liVeranstaltungenWert=new EclVeranstaltungenWert(lVeranstaltungenWert);
                          liVeranstaltungenWert.identAktion=iVeranstaltungenAktion.identAktion;
                          liVeranstaltungenWert.ergebnisNrInAKtion=lfdErgebnisnrInAktion;
                          liVeranstaltungenWert.ergebnisDerAktion=iErgebnisDerAktion;
                          lfdErgebnisnrInAktion++;
                          veranstaltungenWertListe.add(liVeranstaltungenWert);
                      }
                  }
                  if (iVeranstaltungenAktion.aktion==2) {
                      String hMailBetreff=iVeranstaltungenAktion.parameter1;
                      rcMailBetreff.add(hMailBetreff);
                      String hMailText=iVeranstaltungenAktion.parameter2;
                      rcMailText.add(hMailText);
                      String hMailAnhang=iVeranstaltungenAktion.parameter3;
                      rcMailAnhang.add(hMailAnhang);
                       
                  }
               }
           }
            
       }
       if (pVeranstaltungenElement.elementTyp==5) {
           /*Checkbox*/
           for (EclVeranstaltungenElementDetail iVeranstaltungenElementDetail:pVeranstaltungenElement.veranstaltungenElementDetailListe) {
               EclVeranstaltungenWert lVeranstaltungenWert=new EclVeranstaltungenWert();
               lVeranstaltungenWert.loginKennung=loginKennung;
               lVeranstaltungenWert.identVeranstaltung=pVeranstaltungenElement.identVeranstaltung;
               lVeranstaltungenWert.identElement=pVeranstaltungenElement.identElement;
               lVeranstaltungenWert.identDetail=iVeranstaltungenElementDetail.identDetail;
               if (iVeranstaltungenElementDetail.geradeSelektiert) {
                   lVeranstaltungenWert.eingabeWert="1";
                   
                   /*Nun ggf. für alle Aktionen entsprechende Ergebniseinträge erzeugen*/
                   if (iVeranstaltungenElementDetail.veranstaltungenAktionListe!=null) {
                       for (EclVeranstaltungenAktion iVeranstaltungenAktion:iVeranstaltungenElementDetail.veranstaltungenAktionListe) {
                          if (iVeranstaltungenAktion.aktion==2) {
                              String hMailBetreff=iVeranstaltungenAktion.parameter1;
                              rcMailBetreff.add(hMailBetreff);
                              String hMailText=iVeranstaltungenAktion.parameter2;
                              rcMailText.add(hMailText);
                              String hMailAnhang=iVeranstaltungenAktion.parameter3;
                              rcMailAnhang.add(hMailAnhang);
                          }
                       }
                   }
               }
               else {
                   lVeranstaltungenWert.eingabeWert="0";
               }
               veranstaltungenWertListe.add(lVeranstaltungenWert);
           }
           
       }
      if (pVeranstaltungenElement.elementTyp==6) {
          /*Radiobutton*/
          EclVeranstaltungenWert lVeranstaltungenWert=new EclVeranstaltungenWert();
          lVeranstaltungenWert.loginKennung=loginKennung;
          lVeranstaltungenWert.identVeranstaltung=pVeranstaltungenElement.identVeranstaltung;
          lVeranstaltungenWert.identElement=pVeranstaltungenElement.identElement;
          if (pVeranstaltungenElement.eingabeWert==null) {
              lVeranstaltungenWert.eingabeWert="";
          }
          else {
              CaBug.druckeLog("elementTyp=6, Wert!=null", logDrucken, 10);
              lVeranstaltungenWert.eingabeWert=pVeranstaltungenElement.eingabeWert;
              /*Nun ggf. für alle Aktionen entsprechende Ergebniseinträge erzeugen*/
              if (pVeranstaltungenElement.veranstaltungenAktionListe!=null) {
                  CaBug.druckeLog("pVeranstaltungenElement.veranstaltungenAktionListe!=null", logDrucken, 10);
                  for (EclVeranstaltungenAktion iVeranstaltungenAktion:pVeranstaltungenElement.veranstaltungenAktionListe) {
                      if (iVeranstaltungenAktion.aktion==2) {
                          CaBug.druckeLog("iVeranstaltungenAktion.aktion==2", logDrucken,10);    
                          if (lVeranstaltungenWert.eingabeWert.equals(iVeranstaltungenAktion.parameter4)) {
                              String hMailBetreff=iVeranstaltungenAktion.parameter1;
                              rcMailBetreff.add(hMailBetreff);
                              String hMailText=iVeranstaltungenAktion.parameter2;
                              rcMailText.add(hMailText);
                              String hMailAnhang=iVeranstaltungenAktion.parameter3;
                              rcMailAnhang.add(hMailAnhang);
                          }
                      }
                  }
              }
          }
          veranstaltungenWertListe.add(lVeranstaltungenWert);
       }
       
       return ;
   }

   
   /****************************belege Wert**********************************************/
   /**Wird als zwischenspeicher verwendet*/
//   private List<EclVeranstaltungenWert> veranstaltungenWertListe=null;
   
   /**Eingabewert*/
//   private String loginKennung="0001";
   
   public boolean belegeWerteVeranstaltungsliste() {

       if (rcVeranstaltungenListe==null || rcVeranstaltungenListe.size()==0) {
           veranstaltungenWerteVorhanden=false;
           return true;
       }
       if (rcVeranstaltungenListe.size()>1) {
           lDbBundle.dbVeranstaltungenWert.readLoginKennung(loginKennung, -1);
           veranstaltungenWertListe=lDbBundle.dbVeranstaltungenWert.ergebnis();
           if (veranstaltungenWertListe==null || veranstaltungenWertListe.size()==0) {
               veranstaltungenWerteVorhanden=false;
               return true;
           }
       }
       else {
           lDbBundle.dbVeranstaltungenWert.readLoginKennung(loginKennung, rcVeranstaltungenListe.get(0).identVeranstaltung);
           veranstaltungenWertListe=lDbBundle.dbVeranstaltungenWert.ergebnis();
           if (veranstaltungenWertListe==null || veranstaltungenWertListe.size()==0) {
               veranstaltungenWerteVorhanden=false;
               return true;
           }
       }
       
       veranstaltungenWerteVorhanden=true;

       CaBug.druckeLog("Anzahl werte="+veranstaltungenWertListe.size(), logDrucken, 10);
       
       for (EclVeranstaltungenVeranstaltung iVeranstaltungenVeranstaltung: rcVeranstaltungenListe) {
           CaBug.druckeLog("belege Werte Veranstaltung ="+iVeranstaltungenVeranstaltung.identVeranstaltung, logDrucken, 10);
           for (EclVeranstaltungenElement iVeranstaltungenElement: iVeranstaltungenVeranstaltung.veranstaltungenElementListe) {
               CaBug.druckeLog("prüfe Element ="+iVeranstaltungenElement.identElement, logDrucken, 10);
               belegeWerteVeranstaltungsElement(iVeranstaltungenElement);
           }
       }
       return true;
   }
   
   private void belegeWerteVeranstaltungsElement(EclVeranstaltungenElement pVeranstaltungenElement) {
       belegeWerteVeranstaltungsElementEingabe(pVeranstaltungenElement);
       
       for (EclVeranstaltungenElement iVeranstaltungenElement: pVeranstaltungenElement.veranstaltungenElementListe) {
           CaBug.druckeLog("prüfe Element ="+iVeranstaltungenElement.identElement, logDrucken, 10);
           belegeWerteVeranstaltungsElement(iVeranstaltungenElement);
       }
      
       return;
   }
   
   private void belegeWerteVeranstaltungsElementEingabe(EclVeranstaltungenElement pVeranstaltungenElement) {
       /*Erst mal alle Werte zurücksetzen*/
       pVeranstaltungenElement.eingabeWert="";
       pVeranstaltungenElement.eingabeWertAlt="";
       for (EclVeranstaltungenAktion iVeranstaltungenAktion:pVeranstaltungenElement.veranstaltungenAktionListe) {
           iVeranstaltungenAktion.ergebnisDerAktion=new LinkedList<String>();
           iVeranstaltungenAktion.ergebnisDerAktionAlt=new LinkedList<String>();
       }
                 
       
       
       if (pVeranstaltungenElement.elementTyp==5) {
           for (EclVeranstaltungenElementDetail iVeranstaltungenElementDetail:pVeranstaltungenElement.veranstaltungenElementDetailListe) {
               iVeranstaltungenElementDetail.geradeSelektiert=false;
               iVeranstaltungenElementDetail.geradeSelektiertAlterWert=false;
               for (EclVeranstaltungenAktion iVeranstaltungenAktion:iVeranstaltungenElementDetail.veranstaltungenAktionListe) {
                   iVeranstaltungenAktion.ergebnisDerAktion=new LinkedList<String>();
                   iVeranstaltungenAktion.ergebnisDerAktionAlt=new LinkedList<String>();
               }
           }
       }
       
       /*Nun Werte eintragen*/
       
       for (EclVeranstaltungenWert iVeranstaltungenWert: veranstaltungenWertListe) {
           if (iVeranstaltungenWert.identVeranstaltung==pVeranstaltungenElement.identVeranstaltung &&
                   iVeranstaltungenWert.identElement==pVeranstaltungenElement.identElement) {
               if (pVeranstaltungenElement.elementTyp==3) {
                   CaBug.druckeLog("Elementtyp=3", logDrucken, 10);
                   /*Eingabefeld Anzahl*/
                   if (iVeranstaltungenWert.identAktion==0) {
                       /*Tatsächlicher Wert der Eingabe*/
                       pVeranstaltungenElement.eingabeWert=iVeranstaltungenWert.eingabeWert;
                       pVeranstaltungenElement.eingabeWertAlt=iVeranstaltungenWert.eingabeWert;
                   }
                   else {
                       /*Gehört als Wert zu Aktion. Betreffende Aktion suchen*/
                       for (EclVeranstaltungenAktion iVeranstaltungenAktion:pVeranstaltungenElement.veranstaltungenAktionListe) {
                           if (iVeranstaltungenAktion.identAktion==iVeranstaltungenWert.identAktion) {
                               iVeranstaltungenAktion.ergebnisDerAktionAlt.add(iVeranstaltungenWert.ergebnisDerAktion);
                               iVeranstaltungenAktion.ergebnisDerAktion.add(iVeranstaltungenWert.ergebnisDerAktion);
                           }
                       }
                   }
               }

               if (pVeranstaltungenElement.elementTyp==5) {
                   CaBug.druckeLog("Elementtyp=5", logDrucken, 10);
                   /*Checkbox*/
                   for (EclVeranstaltungenElementDetail iVeranstaltungenElementDetail:pVeranstaltungenElement.veranstaltungenElementDetailListe) {
                       if (iVeranstaltungenWert.identDetail==iVeranstaltungenElementDetail.identDetail) {
                           if (iVeranstaltungenWert.eingabeWert.equals("1")) {
                               iVeranstaltungenElementDetail.geradeSelektiert=true;
                               iVeranstaltungenElementDetail.geradeSelektiertAlterWert=true;
                           }
                           else {
                               iVeranstaltungenElementDetail.geradeSelektiert=false;
                               iVeranstaltungenElementDetail.geradeSelektiertAlterWert=false;
                           }
                       }
                   }
               }
               if (pVeranstaltungenElement.elementTyp==6) {
                   CaBug.druckeLog("Elementtyp=6", logDrucken, 10);
                   /*Radiobutton*/
                   CaBug.druckeLog("iVeranstaltungenWert.eingabeWert="+iVeranstaltungenWert.eingabeWert, logDrucken, 10);
                   pVeranstaltungenElement.eingabeWert=iVeranstaltungenWert.eingabeWert;
                   pVeranstaltungenElement.eingabeWertAlt=iVeranstaltungenWert.eingabeWert;
               }

           }

       }
       
       
       return ;
   }

   /***************Report-FUnktionen*********************************/
   public List<EclVeranstaltungenWert> rcVeranstaltungsWertListeGesamt=null;

   private int offsetInRcVeranstaltungsWertListeGesamt=0;
   private int anzahlZumRelativieren=0;
   
   RpDrucken rpDrucken;
   RpVariablen rpVariablen;
   
   public void reportSummenErmitteln() {
       offsetInRcVeranstaltungsWertListeGesamt=0;
       while (offsetInRcVeranstaltungsWertListeGesamt<rcVeranstaltungsWertListeGesamt.size()) {
           reportSummenLiefereNextTeilnehmer();
           addiereWerteVeranstaltungsliste();
           relativiereWerteVeranstaltungsliste();
       }
   }

   private void reportSummenLiefereNextTeilnehmer() {
       veranstaltungenWertListe=new LinkedList<EclVeranstaltungenWert>();
       EclVeranstaltungenWert lVeranstaltungenWert=rcVeranstaltungsWertListeGesamt.get(offsetInRcVeranstaltungsWertListeGesamt);
       String loginKennung=lVeranstaltungenWert.loginKennung;
       while (offsetInRcVeranstaltungsWertListeGesamt<rcVeranstaltungsWertListeGesamt.size() &&
               rcVeranstaltungsWertListeGesamt.get(offsetInRcVeranstaltungsWertListeGesamt).loginKennung.equals(loginKennung)  ) {
           veranstaltungenWertListe.add(rcVeranstaltungsWertListeGesamt.get(offsetInRcVeranstaltungsWertListeGesamt));
           offsetInRcVeranstaltungsWertListeGesamt++;
       }
   }
   
   public boolean addiereWerteVeranstaltungsliste() {
       CaBug.druckeLog("Anzahl werte="+veranstaltungenWertListe.size(), logDrucken, 10);
       
       anzahlZumRelativieren=0;
       
       for (EclVeranstaltungenVeranstaltung iVeranstaltungenVeranstaltung: rcVeranstaltungenListe) {
           CaBug.druckeLog("="+iVeranstaltungenVeranstaltung.identVeranstaltung, logDrucken, 10);
           for (EclVeranstaltungenElement iVeranstaltungenElement: iVeranstaltungenVeranstaltung.veranstaltungenElementListe) {
               CaBug.druckeLog(" ="+iVeranstaltungenElement.identElement, logDrucken, 10);
               addiereWerteVeranstaltungsElement(iVeranstaltungenElement);
           }
       }
       return true;
   }
   
   private void addiereWerteVeranstaltungsElement(EclVeranstaltungenElement pVeranstaltungenElement) {
       addiereWerteVeranstaltungsElementEingabe(pVeranstaltungenElement);
       
       for (EclVeranstaltungenElement iVeranstaltungenElement: pVeranstaltungenElement.veranstaltungenElementListe) {
           CaBug.druckeLog(" ="+iVeranstaltungenElement.identElement, logDrucken, 10);
           addiereWerteVeranstaltungsElement(iVeranstaltungenElement);
       }
      
       return;
   }
   
   private void addiereWerteVeranstaltungsElementEingabe(EclVeranstaltungenElement pVeranstaltungenElement) {
       /*Erst mal alle Werte zurücksetzen - wird benötigt, um später ggf. "zu relativieren"*/
       pVeranstaltungenElement.eingabeWert="";
       pVeranstaltungenElement.eingabeWertAlt="";
       for (EclVeranstaltungenAktion iVeranstaltungenAktion:pVeranstaltungenElement.veranstaltungenAktionListe) {
           iVeranstaltungenAktion.ergebnisDerAktion=new LinkedList<String>();
           iVeranstaltungenAktion.ergebnisDerAktionAlt=new LinkedList<String>();
       }
                 
       
       
       if (pVeranstaltungenElement.elementTyp==5 || pVeranstaltungenElement.elementTyp==6) {
           for (EclVeranstaltungenElementDetail iVeranstaltungenElementDetail:pVeranstaltungenElement.veranstaltungenElementDetailListe) {
               iVeranstaltungenElementDetail.geradeSelektiert=false;
               iVeranstaltungenElementDetail.geradeSelektiertAlterWert=false;
               for (EclVeranstaltungenAktion iVeranstaltungenAktion:iVeranstaltungenElementDetail.veranstaltungenAktionListe) {
                   iVeranstaltungenAktion.ergebnisDerAktion=new LinkedList<String>();
                   iVeranstaltungenAktion.ergebnisDerAktionAlt=new LinkedList<String>();
               }
           }
       }
       
       /*Nun Werte eintragen*/
       
       for (EclVeranstaltungenWert iVeranstaltungenWert: veranstaltungenWertListe) {
           if (iVeranstaltungenWert.identVeranstaltung==pVeranstaltungenElement.identVeranstaltung &&
                   iVeranstaltungenWert.identElement==pVeranstaltungenElement.identElement) {
               if (pVeranstaltungenElement.elementTyp==3) {
                   CaBug.druckeLog("Elementtyp=3", logDrucken, 10);
                   /*Eingabefeld Anzahl*/
                   if (iVeranstaltungenWert.identAktion==0) {
                       /*Tatsächlicher Wert der Eingabe*/
                       pVeranstaltungenElement.eingabeWert=iVeranstaltungenWert.eingabeWert;
                       pVeranstaltungenElement.eingabeWertAlt=iVeranstaltungenWert.eingabeWert;
                       pVeranstaltungenElement.summeUeberAlleTeilnehmer+=Integer.parseInt(iVeranstaltungenWert.eingabeWert);
                       anzahlZumRelativieren=Integer.parseInt(iVeranstaltungenWert.eingabeWert);
                   }
                   else {
                       /*Gehört als Wert zu Aktion. Betreffende Aktion suchen*/
                       for (EclVeranstaltungenAktion iVeranstaltungenAktion:pVeranstaltungenElement.veranstaltungenAktionListe) {
                           if (iVeranstaltungenAktion.identAktion==iVeranstaltungenWert.identAktion) {
                               iVeranstaltungenAktion.ergebnisDerAktionAlt.add(iVeranstaltungenWert.ergebnisDerAktion);
                               iVeranstaltungenAktion.ergebnisDerAktion.add(iVeranstaltungenWert.ergebnisDerAktion);
                           }
                       }
                   }
               }

               if (pVeranstaltungenElement.elementTyp==5) {
                   CaBug.druckeLog("Elementtyp=5", logDrucken, 10);
                   /*Checkbox*/
                   for (EclVeranstaltungenElementDetail iVeranstaltungenElementDetail:pVeranstaltungenElement.veranstaltungenElementDetailListe) {
                       if (iVeranstaltungenWert.identDetail==iVeranstaltungenElementDetail.identDetail) {
                           if (iVeranstaltungenWert.eingabeWert.equals("1")) {
                               iVeranstaltungenElementDetail.geradeSelektiert=true;
                               iVeranstaltungenElementDetail.geradeSelektiertAlterWert=true;
                               iVeranstaltungenElementDetail.summeUeberAlleTeilnehmer++;
                           }
                           else {
                               iVeranstaltungenElementDetail.geradeSelektiert=false;
                               iVeranstaltungenElementDetail.geradeSelektiertAlterWert=false;
                           }
                       }
                   }
               }
               if (pVeranstaltungenElement.elementTyp==6) {
                   CaBug.druckeLog("Elementtyp=6", logDrucken, 10);
                   /*Radiobutton*/
                   for (EclVeranstaltungenElementDetail iVeranstaltungenElementDetail:pVeranstaltungenElement.veranstaltungenElementDetailListe) {
                       if (iVeranstaltungenElementDetail.erzeugtWert.equals(iVeranstaltungenWert.eingabeWert)) {
                           iVeranstaltungenElementDetail.geradeSelektiert=true;
                           iVeranstaltungenElementDetail.summeUeberAlleTeilnehmer++;
                       }
                   }
                   
                   CaBug.druckeLog("iVeranstaltungenWert.eingabeWert="+iVeranstaltungenWert.eingabeWert, logDrucken, 10);
                   pVeranstaltungenElement.eingabeWert=iVeranstaltungenWert.eingabeWert;
                   pVeranstaltungenElement.eingabeWertAlt=iVeranstaltungenWert.eingabeWert;
               }

           }

       }
       
       
       return ;
   }


   private void relativiereWerteVeranstaltungsliste() {
       for (EclVeranstaltungenVeranstaltung iVeranstaltungenVeranstaltung: rcVeranstaltungenListe) {
           CaBug.druckeLog(" ="+iVeranstaltungenVeranstaltung.identVeranstaltung, logDrucken, 10);
           for (EclVeranstaltungenElement iVeranstaltungenElement: iVeranstaltungenVeranstaltung.veranstaltungenElementListe) {
               CaBug.druckeLog(" ="+iVeranstaltungenElement.identElement, logDrucken, 10);
               relativiereWerteVeranstaltungsElement(iVeranstaltungenElement);
           }
       }
       return;
   }
   
   private void relativiereWerteVeranstaltungsElement(EclVeranstaltungenElement pVeranstaltungenElement) {
       relativiereWerteVeranstaltungsElementEingabe(pVeranstaltungenElement);
        
       for (EclVeranstaltungenElement iVeranstaltungenElement: pVeranstaltungenElement.veranstaltungenElementListe) {
           CaBug.druckeLog(" ="+iVeranstaltungenElement.identElement, logDrucken, 10);
           relativiereWerteVeranstaltungsElement(iVeranstaltungenElement);
       }
      
       return;
   }
   
   private void relativiereWerteVeranstaltungsElementEingabe(EclVeranstaltungenElement pVeranstaltungenElement) {
       if (pVeranstaltungenElement.elementTyp==5) {
           /*Checkbox*/
           /**Summe der Markierungen ermitteln*/
           int summeMarkierungen=0;
           for (EclVeranstaltungenElementDetail iVeranstaltungenElementDetail:pVeranstaltungenElement.veranstaltungenElementDetailListe) {
               if (iVeranstaltungenElementDetail.geradeSelektiert) {
                   summeMarkierungen++;
               }
           }
           for (EclVeranstaltungenElementDetail iVeranstaltungenElementDetail:pVeranstaltungenElement.veranstaltungenElementDetailListe) {
               if (iVeranstaltungenElementDetail.geradeSelektiert) {
                   iVeranstaltungenElementDetail.summeUeberAlleTeilnehmerRelativiert+=(double)1/(double)summeMarkierungen*(double)anzahlZumRelativieren;
               }

           }
       }
       if (pVeranstaltungenElement.elementTyp==6) {
           /*Radiobutton*/
           for (EclVeranstaltungenElementDetail iVeranstaltungenElementDetail:pVeranstaltungenElement.veranstaltungenElementDetailListe) {
               if (iVeranstaltungenElementDetail.geradeSelektiert) {
                   iVeranstaltungenElementDetail.summeUeberAlleTeilnehmerRelativiert+=(double)anzahlZumRelativieren;
               }

           }
       }

       return ;
   }

   
   public void reportSummenReportFuellen(RpDrucken pRpDrucken, RpVariablen pRpVariablen) {
       rpDrucken=pRpDrucken;
       rpVariablen=pRpVariablen;
       
       for (EclVeranstaltungenVeranstaltung iVeranstaltungenVeranstaltung: rcVeranstaltungenListe) {
           druckeVeranstaltungsUeberschrift(iVeranstaltungenVeranstaltung);
           CaBug.druckeLog(" ="+iVeranstaltungenVeranstaltung.identVeranstaltung, logDrucken, 10);
           for (EclVeranstaltungenElement iVeranstaltungenElement: iVeranstaltungenVeranstaltung.veranstaltungenElementListe) {
               CaBug.druckeLog(" ="+iVeranstaltungenElement.identElement, logDrucken, 10);
               druckeVeranstaltungsElement(iVeranstaltungenElement);
           }
       }
       return;

       
       
       /*AAAAA*/
//     reportPfadDateiname("VeranstaltungsManagement", nr, ".lst");

       
//     fuelleFeld(rpDrucken, "Ident", "1234");
//     fuelleFeld(rpDrucken, "IdentDetail", "1234");
//     fuelleFeld(rpDrucken, "Bezeichnung", "Essensbestellung");
//     fuelleFeld(rpDrucken, "Anzahl", "123456");
//     fuelleFeld(rpDrucken, "AnzahlRelativiert", "234567");
       
//       rpDrucken.druckenListe();

   }
   
   
   private void druckeVeranstaltungsUeberschrift(EclVeranstaltungenVeranstaltung pVeranstaltungenVeranstaltung) {
       rpVariablen.fuelleFeld(rpDrucken, "Ident", Integer.toString(pVeranstaltungenVeranstaltung.identVeranstaltung));
       rpVariablen.fuelleFeld(rpDrucken, "IdentDetail", "");
       rpVariablen.fuelleFeld(rpDrucken, "Bezeichnung", pVeranstaltungenVeranstaltung.textIntern);
       rpVariablen.fuelleFeld(rpDrucken, "Anzahl", "");
       rpVariablen.fuelleFeld(rpDrucken, "AnzahlRelativiert", "");
       rpDrucken.druckenListe();
   }

   
   
   private void druckeVeranstaltungsElement(EclVeranstaltungenElement pVeranstaltungenElement) {
       druckeVeranstaltungsElementEingabe(pVeranstaltungenElement);
        
       for (EclVeranstaltungenElement iVeranstaltungenElement: pVeranstaltungenElement.veranstaltungenElementListe) {
           CaBug.druckeLog(" ="+iVeranstaltungenElement.identElement, logDrucken, 10);
           druckeVeranstaltungsElement(iVeranstaltungenElement);
       }
      
       return;
   }
   
   private void druckeVeranstaltungsElementEingabe(EclVeranstaltungenElement pVeranstaltungenElement) {
       if (pVeranstaltungenElement.elementTyp==3) {
           rpVariablen.fuelleFeld(rpDrucken, "Ident", Integer.toString(pVeranstaltungenElement.identElement));
           rpVariablen.fuelleFeld(rpDrucken, "IdentDetail", "");
           rpVariablen.fuelleFeld(rpDrucken, "Bezeichnung", pVeranstaltungenElement.textIntern);
           rpVariablen.fuelleFeld(rpDrucken, "Anzahl", Integer.toString(pVeranstaltungenElement.summeUeberAlleTeilnehmer));
           rpVariablen.fuelleFeld(rpDrucken, "AnzahlRelativiert", "");
           rpDrucken.druckenListe();
       }

       if (pVeranstaltungenElement.elementTyp==5 || pVeranstaltungenElement.elementTyp==6) {
           /*Checkbox*/
           /**Summe der Markierungen ermitteln*/
           for (EclVeranstaltungenElementDetail iVeranstaltungenElementDetail:pVeranstaltungenElement.veranstaltungenElementDetailListe) {
               rpVariablen.fuelleFeld(rpDrucken, "Ident", Integer.toString(pVeranstaltungenElement.identElement));
               rpVariablen.fuelleFeld(rpDrucken, "IdentDetail", Integer.toString(iVeranstaltungenElementDetail.identDetail));
               rpVariablen.fuelleFeld(rpDrucken, "Bezeichnung", iVeranstaltungenElementDetail.textIntern);
               rpVariablen.fuelleFeld(rpDrucken, "Anzahl", Integer.toString(iVeranstaltungenElementDetail.summeUeberAlleTeilnehmer));
               rpVariablen.fuelleFeld(rpDrucken, "AnzahlRelativiert", CaString.toEuroStringDE(iVeranstaltungenElementDetail.summeUeberAlleTeilnehmerRelativiert, 2));
               rpDrucken.druckenListe();
               
           }
       }

       return ;
   }

   /************************Menü-Anzeige (neu)*********************************/
   
   public List<EclVeranstaltungenMenue> rcVeranstaltungenFuerMenueListe=null;
   
   public boolean erzeugeVeranstaltungslisteFuerMenue(int pMenueNummer) {
       
       lDbBundle.dbVeranstaltungenMenue.readMenue(pMenueNummer);
       rcVeranstaltungenFuerMenueListe=lDbBundle.dbVeranstaltungenMenue.ergebnis();
       
       return true;
   }
   
   
   /***********************Teilnehmer-Detail-Liste****************************/
   public List<EclVeranstaltungenReportElement> rcVeranstaltungenReportUebersicht=null;
   
   public boolean erzeugeReportListeFuerULoginMenue() {
       
       lDbBundle.dbVeranstaltungenReportElement.readAlleAktivenReportsFuerAuswahl();
       rcVeranstaltungenReportUebersicht=lDbBundle.dbVeranstaltungenReportElement.ergebnis();
       
       return true;
   }
   
   /**Komplett, einschließlich Pfad-Name*/
   public String rcExportDateiname="";
 
   private EclVeranstaltungenReportElement[] reportStruktur=null;;
   private CaDateiWrite caDateiWrite =null;
   
   public boolean exportDetailTeilnehmerliste(EclVeranstaltungenReportElement pVeranstaltungenReportElement) {
       
       /*Report-Daten einlesen*/
       lDbBundle.dbVeranstaltungenReportElement.readReportIdent(pVeranstaltungenReportElement.identReport, pVeranstaltungenReportElement.identVeranstaltung);
       List<EclVeranstaltungenReportElement> reportElemente=lDbBundle.dbVeranstaltungenReportElement.ergebnis();
      
       /*Report-Grunddaten ermitteln*/
       String ueberschrift="Überschrift unbekannt";
       String dateiname="DateinameNichtSpezifiziert";
       int hoechstesElement=0;
       for (int i=0;i<reportElemente.size();i++) {
           EclVeranstaltungenReportElement hReportElement=reportElemente.get(i);
           switch (hReportElement.elementTyp) {
           case 1:ueberschrift=hReportElement.elementBezeichnung;break;
           case 2:dateiname=hReportElement.elementBezeichnung;break;
           case 3:hoechstesElement=hReportElement.offsetInReport;
           }
       }
       
       /*Report-Struktur aufbauen*/
       /*Achtung - das Element mit offsetInReport[1] wird in reportStruktur[0] eingetragen!*/
       reportStruktur=new EclVeranstaltungenReportElement[hoechstesElement];
       for (int i=0;i<hoechstesElement;i++) {
           reportStruktur[i]=null;
       }
       for (int i=0;i<reportElemente.size();i++) {
           EclVeranstaltungenReportElement hReportElement=reportElemente.get(i);
           if (hReportElement.elementTyp==3) {
               if (hReportElement.identReportSubElement==0) {
                   /*Hauptelement eintragen*/
                   hReportElement.subElementListe=new LinkedList<EclVeranstaltungenReportElement>();
                   reportStruktur[hReportElement.offsetInReport-1]=hReportElement;
               }
               else {
                   /*Sub-Element ergänzen*/
                   if (reportStruktur[hReportElement.offsetInReport-1]==null) {
                       CaBug.drucke("001 - Hauptelement nicht definiert");
                   }
                   else {
                       reportStruktur[hReportElement.offsetInReport-1].subElementListe.add(hReportElement);
                   }
               }
           }

       }
       
       caDateiWrite = new CaDateiWrite();
       caDateiWrite.setzeFuerCSV();
       caDateiWrite.oeffne(lDbBundle, dateiname + 
               Integer.toString(pVeranstaltungenReportElement.identVeranstaltung)+"_"+Integer.toString(pVeranstaltungenReportElement.identReport));
       rcExportDateiname = caDateiWrite.dateiname;

       caDateiWrite.ausgabe(ueberschrift);
       caDateiWrite.newline();
       caDateiWrite.newline();

       /*Überschrift ausgeben*/
       caDateiWrite.ausgabe("Nummer");
       caDateiWrite.ausgabe("Name");
       caDateiWrite.ausgabe("Ort");
       for (int i=0;i<hoechstesElement;i++) {
           EclVeranstaltungenReportElement hReportElement=reportStruktur[i];
           if (hReportElement==null) {
               caDateiWrite.ausgabe("");
           }
           else {
               caDateiWrite.ausgabe(hReportElement.elementBezeichnung);
           }
                      
       }
       caDateiWrite.newline();
       

       /*TeilnehmerwerteElemente zur Veranstaltung lesen*/
       lDbBundle.dbVeranstaltungenWert.readAllVeranstaltung(pVeranstaltungenReportElement.identVeranstaltung);
       rcVeranstaltungsWertListeGesamt=lDbBundle.dbVeranstaltungenWert.ergebnis();
       
       /*Alle Teilnehmer durchrennen*/
       offsetInRcVeranstaltungsWertListeGesamt=0;
       while (offsetInRcVeranstaltungsWertListeGesamt<rcVeranstaltungsWertListeGesamt.size()) {
           reportSummenLiefereNextTeilnehmer();
           gibTeilnehmerErgebniseInListeAus();
       }

       caDateiWrite.schliessen();

      
       return true;
   }
   
   private void gibTeilnehmerErgebniseInListeAus() {
       String loginKennung=veranstaltungenWertListe.get(0).loginKennung;
       lDbBundle.dbAktienregister.leseZuAktienregisternummer(loginKennung);
       if (lDbBundle.dbAktienregister.anzErgebnis()==0) {
           CaBug.drucke("Aktienregistereintrag "+loginKennung+" nicht gefunden");
           return;
       }
       EclAktienregister lAktienregister=lDbBundle.dbAktienregister.ergebnisPosition(0);
       caDateiWrite.ausgabe(BlNummernformBasis.aufbereitenInternFuerExtern(loginKennung, lDbBundle));
       caDateiWrite.ausgabe(lAktienregister.nachname+", "+lAktienregister.vorname);
       caDateiWrite.ausgabe(lAktienregister.ort);
       CaBug.druckeLog(loginKennung, logDrucken, 10);
       
       for (int i=0;i<reportStruktur.length;i++) {
           CaBug.druckeLog("i="+i, logDrucken, 10);
           EclVeranstaltungenReportElement hReportElement=reportStruktur[i];
           if (hReportElement==null) {
               caDateiWrite.ausgabe("");
           }
           else {
               String wert="";
               String ergebnis="";
               for (int i1=0;i1<veranstaltungenWertListe.size();i1++) {
                   EclVeranstaltungenWert lVeranstaltungenWert=veranstaltungenWertListe.get(i1);
                   if (lVeranstaltungenWert.identElement==hReportElement.identElement &&
                           lVeranstaltungenWert.identDetail==hReportElement.identDetail &&
                           lVeranstaltungenWert.identAktion==hReportElement.identAktion) {
                       wert=lVeranstaltungenWert.eingabeWert;
                   }
               }
               CaBug.druckeLog("wert="+wert, logDrucken, 10);
               if (hReportElement.wertBerechnung==1) {
                   /*Wert 1:1 übernehmen*/
                   ergebnis=wert;
               }
               else {
                   /*Wert gemäß Subabbildung*/
                   if (hReportElement.subElementListe!=null) {
                       for (int i2=0;i2<hReportElement.subElementListe.size();i2++) {
                           if (wert.equals(hReportElement.subElementListe.get(i2).wertAusQuittung)) {
                               ergebnis=hReportElement.subElementListe.get(i2).wertInReport;
                           }
                       }
                   }
               }
               
               caDateiWrite.ausgabe(ergebnis);
           }
                      
       }
       
       caDateiWrite.newline();
   }
   
   
}

