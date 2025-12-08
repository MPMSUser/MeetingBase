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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaDateiWrite;
import de.meetingapps.meetingportal.meetComAllg.CaDatumZeit;
import de.meetingapps.meetingportal.meetComAllg.CaString;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEh.EhInhaltsHinweise;
import de.meetingapps.meetingportal.meetComEh.EhJsfButton;
import de.meetingapps.meetingportal.meetComEh.EhJsfSummeWortmeldeView;
import de.meetingapps.meetingportal.meetComEntities.EclAbstimmung;
import de.meetingapps.meetingportal.meetComEntities.EclAbstimmungSet;
import de.meetingapps.meetingportal.meetComEntities.EclAktienregister;
import de.meetingapps.meetingportal.meetComEntities.EclInfo;
import de.meetingapps.meetingportal.meetComEntities.EclLoginDaten;
import de.meetingapps.meetingportal.meetComEntities.EclMitteilung;
import de.meetingapps.meetingportal.meetComEntities.EclPersonenNatJur;
import de.meetingapps.meetingportal.meetComEntities.EclPortalUnterlagen;
import de.meetingapps.meetingportal.meetComEntities.EclVirtuellerTeilnehmer;
import de.meetingapps.meetingportal.meetComEntities.EclWortmeldetischFolgeStatus;
import de.meetingapps.meetingportal.meetComEntities.EclWortmeldetischView;
import de.meetingapps.meetingportal.meetComHVParam.ParamPortal;
import de.meetingapps.meetingportal.meetComKonst.KonstLoginKennungArt;
import de.meetingapps.meetingportal.meetComKonst.KonstMitteilungStatus;
import de.meetingapps.meetingportal.meetComKonst.KonstPortalFunktionen;
import de.meetingapps.meetingportal.meetComKonst.KonstPortalUnterlagen;
import de.meetingapps.meetingportal.meetComKonst.KonstSkIst;
import de.meetingapps.meetingportal.meetComKonst.KonstVerarbeitungslaufArt;
import de.meetingapps.meetingportal.meetComKonst.KonstWeisungserfassungSicht;
import de.meetingapps.meetingportal.meetComStub.StubRoot;
import de.meetingapps.meetingportal.meetingCoreReport.RpDrucken;
import de.meetingapps.meetingportal.meetingCoreReport.RpVariablen;

/**Hinweis: rueckfragen wird wie fragen behandelt*/

public class BlMitteilungen extends StubRoot {

    private int logDrucken=3;
    
    /**true => der Aktionär ist eine juristische Person, es liegt keine Vollmacht vor=>
     * Fragen stellen und Mitteilungen machen nicht möglich
     */
    public boolean rcKeineVollmachtFuerMitteilungen = false;

    public List<EclVirtuellerTeilnehmer> rcVirtuelleTeilnehmerList = null;

    public List<EclMitteilung> rcMitteilungenZuLoginKennung = null;
    public int rcAnzahlMitteilungenGestelltOhneZureuckgezogenZuLoginKennung=0;
    
    /**Spezial für Pfandbriefbank - nur für diesen Mandanten - Anzahl der offenen Wortmeldungen - 
     * noch nicht wirklich fertig 
     */
    public int rcAnzahlMitteilungenOffenPfandbrief=0;
    
    public List<EclMitteilung> rcRednerliste = null;

    public List<EclMitteilung> rcMitteilungen = null;

    public String rcDateiName = "";
    public String rcDateiNamePur = "";

    public EclMitteilung rcNeueMitteilung = null;

    /**Parameter, passend zu lFunktion belegt*/
    public int rcStellerAbfragen=0;
    public int rcNameAbfragen=0;
    public int rcKontaktdatenAbfragen=0;
    public int rcKontaktdatenTelefonAbfragen=0;
    public int rcKontaktdatenEMailVorschlagen=0;
    public int rcKurztextAbfragen=0;
    public int rcTopListeAnbieten=0;
    /**SkIst ist hier eigentlich komplett falscher Name. Aber da KonstSkIst
     * für die Auswahl der zuständigen Abstimmungen benötigt wird,
     * wird die Namensgebung hier so übernommen ...
     */
    public int rcSkIstZuTopListe=0;
    public int rcLangtextAbfragen=0;
    public int rcLangtextUndDateiNurAlternativ=0;
    public int rcZurueckziehenMoeglich=0;
    public int rcLaenge=0;
    public int rcAnzahlJeAktionaer=0;
    public int rcStellerZulaessig=0;
    
    public String[] rcInhaltsHinweiseTextDE=null;
    public String[] rcInhaltsHinweiseTextEN=null;
    public boolean[] rcInhaltsHinweiseAktiv=null;
    public boolean rcInhaltsHinweiseVorhandenundAktiv=false;
    public List<EhInhaltsHinweise> rcInhaltsHinweiseListe=null;
    
    /**Anzeigen Rednerliste bzw. Anzahl der Redner im Aktionärsportal*/
    public int rcListeAnzeigen=0;
    public int rcMailBeiEingang=0;
    public String rcMailVerteiler1="";
    public String rcMailVerteiler2="";
    public String rcMailVerteiler3="";
    public int rcHinweisGelesen=0;
    public int rcHinweisVorbelegenMit=0;
    
   
    /*++++Parameter speziell (bzw. nur) für Botschaften hochladen. Aber natürlich auch für was anderes denkbar!+++*/
    public boolean rcDateiHochladenMoeglich=false;
    public int rcVideoDateiMoeglich=0;
    public String[] rcVideoZusatz= null;
    public int[] rcVideoFormate= null;
    public int rcVideoLaenge=0;
    public int rcTextDateiMoeglich=0;
    public String[] rcTextDateiZusatz= null;
    public int[] rcTextFormate= null;
    public int rcTextLaenge=0;

    
    /*++++++++Parameter für Botschaften-Bearbeitung++++++++++++++++++++++++++++++*/
    public List<EclMitteilung> rcBotschaftenListe=new LinkedList<EclMitteilung>();
    
    
    /************Lokale Variablen******/
    private int lFunktion=0;
    
    /**pFunktion siehe KonstPortalFunktionen*/
    public BlMitteilungen(boolean pIstServer, DbBundle pDbBundle, int pFunktion) {
        super(pIstServer, pDbBundle);
        lFunktion=pFunktion;
    }



    /**pVirtuellerTeilnehmer kann null sein - wenn keine Abfrage erfolgte.
     * 
     * pAktienregisterIdent und pVirtuellerTeilnehmer werden in dieser Funktion
     * in pMitteilung überführt.
     * 
     * Zeit wird gesetzt
     * 
     * Neue Mitteilung (vollständig) wird abgelegt in rcNeueMitteilung*/
    public int neueMitteilungSpeichern(EclMitteilung pMitteilung) {

        dbOpenUndWeitere();
        lDbBundle.dbMitteilung.setzeFunktion(lFunktion);
//        pMitteilung.vertreterIdent = pPersonNatJurMitteilungsStellerIdent;
        
        //		if (pVirtuellerTeilnehmer==null) {
        //			pMitteilung.instiIdent=0;
        //			pMitteilung.vertreterIdent=0;
        //		}
        //		else {
        //			switch (pVirtuellerTeilnehmer.art) {
        //			case 1:{
        //				pMitteilung.instiIdent=0;
        //				pMitteilung.vertreterIdent=0;
        //				break;
        //			}
        //			case 2:{
        //				pMitteilung.instiIdent=0;
        //				pMitteilung.vertreterIdent=pVirtuellerTeilnehmer.ident;
        //				break;
        //			}
        //			case 3:{
        //				pMitteilung.instiIdent=pVirtuellerTeilnehmer.ident;
        //				pMitteilung.vertreterIdent=0;
        //				break;
        //			}
        //			}
        //		}
        pMitteilung.zeitpunktDerMitteilung = CaDatumZeit.DatumZeitStringFuerDatenbank();
        lDbBundle.dbMitteilung.insert(pMitteilung);
        ergaenzeMitteilung(pMitteilung);
        rcNeueMitteilung=pMitteilung;
        dbClose();
        return 1;
    }

 
    /**
     * Belegt:
     * rcMitteilungenZuAktionaer
     */
    public int holeMitteilungenZuLoginKennung(int pIdent, EclAbstimmungSet pAbstimmungSet, int pSkIstZuTopListe) {

        initAbstimmungslisten(pAbstimmungSet, pSkIstZuTopListe);
        
        rcMitteilungenZuLoginKennung = new LinkedList<EclMitteilung>();
        dbOpenUndWeitere();
        lDbBundle.dbMitteilung.setzeFunktion(lFunktion);
        lDbBundle.dbMitteilung.readAll_loginIdent(pIdent);
        int anz = lDbBundle.dbMitteilung.anzErgebnis();
        int anzOhneZurueckgezogen=0;
        int anzPfandbrief=0;
        
        for (int i = 0; i < anz; i++) {
            EclMitteilung lMitteilung = lDbBundle.dbMitteilung.ergebnisPosition(i);
            ergaenzeMitteilung(lMitteilung);
            ergaenzeMitteilungZuTOPListeUndInhaltsHinweise(lMitteilung);
            rcMitteilungenZuLoginKennung.add(lMitteilung);
            if (lMitteilung.status!=KonstMitteilungStatus.ZURUECKGEZOGEN) {
                anzOhneZurueckgezogen++; 
            }
            
            if (lMitteilung.status!=5 &&
                    lMitteilung.status!=7 &&
                    lMitteilung.status!=10 &&
                    lMitteilung.status!=47 &&
                    lMitteilung.status!=49
                    ) {
                anzPfandbrief++;
            }
        }
        
        rcAnzahlMitteilungenGestelltOhneZureuckgezogenZuLoginKennung=anzOhneZurueckgezogen;
        rcAnzahlMitteilungenOffenPfandbrief=anzPfandbrief;
        dbClose();

        return 1;
    }

    
    /**Für Anzeige beim Aktinäür*/
    public int holeRednerListe() {
        dbOpenUndWeitere();
        holeRednerListeIntern(rcListeAnzeigen);

        dbClose();
        return 1;
    }

    private void holeRednerListeIntern(int pAnzahl) {
        rcRednerliste = new LinkedList<EclMitteilung>();
        if (pAnzahl==0) {return;}
        lDbBundle.dbMitteilung.setzeFunktion(lFunktion);
        lDbBundle.dbMitteilung.readAll_rednerliste();
        int anz = lDbBundle.dbMitteilung.anzErgebnis();
        for (int i = 0; i < anz; i++) {
            if (rcRednerliste.size() < pAnzahl) {
                EclMitteilung lMitteilung = lDbBundle.dbMitteilung.ergebnisPosition(i);
                if (lDbBundle.param.paramPortal.wortmeldetischStatusArray[lMitteilung.status].inRednerlisteDesTeilnehmersAnzeigen==1) {
                    ergaenzeMitteilung(lMitteilung);
                    rcRednerliste.add(lMitteilung);
                }
            }
        }

    }

    
    /**Belegt
     * rcMitteilungen
     */
    public int holeMitteilungen(boolean pEntferneNL, EclAbstimmungSet pAbstimmungSet, int pSkIstZuTopListe) {
        return holeNeueMitteilungenIntern(true, pEntferneNL, pAbstimmungSet, pSkIstZuTopListe);
    }

    /**Belegt
     * rcFragen
     */
    public int holeNeueMitteilungen(boolean pEntferneNL, EclAbstimmungSet pAbstimmungSet, int pSkIstZuTopListe) {
        return holeNeueMitteilungenIntern(false, pEntferneNL, pAbstimmungSet, pSkIstZuTopListe);
    }

    private int holeNeueMitteilungenIntern(boolean pAlle, boolean pEntferneNL, EclAbstimmungSet pAbstimmungSet, int pSkIstZuTopListe) {
        rcMitteilungen = new LinkedList<EclMitteilung>();
        initAbstimmungslisten(pAbstimmungSet, pSkIstZuTopListe);
        
        dbOpenUndWeitere();
        lDbBundle.dbMitteilung.setzeFunktion(lFunktion);
        int drucklaufNr = 0;

        if (pAlle == false) {
            /*Neuen Drucklauf erzeugen*/
            BlDrucklauf blDrucklauf = new BlDrucklauf(true, lDbBundle);
            blDrucklauf.erzeugeNeuenDrucklauf(lDbBundle.clGlobalVar.arbeitsplatz, lDbBundle.clGlobalVar.benutzernr,
                    KonstVerarbeitungslaufArt.mitteilungenListe, 0);
            drucklaufNr = blDrucklauf.rcDrucklauf.drucklaufNr;

            int anzMitteilungen = 0;
            anzMitteilungen = lDbBundle.dbMitteilung.updateNeuerDrucklauf(drucklaufNr);

            /*Drucklauf-Anzahl updaten*/
            blDrucklauf.updateAnzDrucklauf(anzMitteilungen);
        }

        /*Hier: drucklaufNr=0 (alle) oder >0 (nur Update)*/
        lDbBundle.dbMitteilung.readAll_mitteilungen(drucklaufNr);
        int anz = lDbBundle.dbMitteilung.anzErgebnis();
        for (int i = 0; i < anz; i++) {
            EclMitteilung lMitteilung = lDbBundle.dbMitteilung.ergebnisPosition(i);
            ergaenzeMitteilung(lMitteilung);
            ergaenzeMitteilungZuTOPListeUndInhaltsHinweise(lMitteilung);
            if (pEntferneNL) {
                entferneNL(lMitteilung);
            }
            rcMitteilungen.add(lMitteilung);
        }

        dbClose();
        return 1;
    }

    /**Ergänzt die Mitteilung um die Login-Daten:
     *  loginKennung, nameWortmelder, ortWortmelder, loginKennungAnzeige, zeitpunktFuerAnzeige,
     *  statusTextExtern.
     * Allerdings nur für Wortmeldungen, nicht für andere Mitteilungsarten
     */
    public void ergaenzeMitteilung(EclMitteilung lMitteilung) {
        CaBug.druckeLog("", logDrucken, 10);
        if (lMitteilung.artDerMitteilung==KonstPortalFunktionen.wortmeldungen) {
            ergaenzeEinzelneMitteilungImmer(lMitteilung);
            lMitteilung.statusText=lDbBundle.param.paramPortal.wortmeldetischStatusArray[KonstMitteilungStatus.liefereNurStatus(
                    lMitteilung.status)].textNummerFuerDiesenStatus;
            lMitteilung.zurueckziehenIstMoeglich=(lDbBundle.param.paramPortal.wortmeldetischStatusArray[KonstMitteilungStatus.liefereNurStatus(
                    lMitteilung.status)].zurueckziehenMoeglichDurchTeilnehmer==1);
        }
        else {
            lMitteilung.statusText=KonstMitteilungStatus.getTextINr(lMitteilung.status, lMitteilung.artDerMitteilung);
            CaBug.druckeLog("lMitteilung.status="+lMitteilung.status+" lMitteilung.artDerMitteilung="+lMitteilung.artDerMitteilung+" lMitteilung.statusText="+lMitteilung.statusText, logDrucken, 8);
            lMitteilung.zurueckziehenIstMoeglich=KonstMitteilungStatus.zurueckziehenMoeglich(lMitteilung.status);
       }
        
        /**Sonst: Derzeit nichts zu tun - aber möglicherweise noch was zu ergänzen*/
        return;
    }

    /**Ergänzt die Mitteilung um die Login-Daten:
     *  loginKennung, nameWortmelder, ortWortmelder, loginKennungAnzeige, zeitpunktFuerAnzeige,
     *  statusTextExtern.
     * Immer, unabhängig von Mitteilungsarten
     */
    public void ergaenzeEinzelneMitteilungImmer(EclMitteilung lMitteilung) {
        lDbBundle.dbLoginDaten.read_ident(lMitteilung.loginIdent);
        EclLoginDaten lLoginDaten = lDbBundle.dbLoginDaten.ergebnisPosition(0);
        lMitteilung.loginKennung = lLoginDaten.loginKennung;
        if (lLoginDaten.kennungArt == KonstLoginKennungArt.aktienregister) {
            lDbBundle.dbAktienregister.leseZuAktienregisterIdent(lLoginDaten.aktienregisterIdent);
            EclAktienregister lAktienregisterEintrag = lDbBundle.dbAktienregister.ergebnisPosition(0);
            if (lAktienregisterEintrag == null) {
                CaBug.drucke("001");
            } else {
                lMitteilung.nameWortmelder = lAktienregisterEintrag.nameKomplett;
                lMitteilung.ortWortmelder = lAktienregisterEintrag.ort;
                CaBug.druckeLog("lWortmeldungen.nameWortmelder=" + lMitteilung.nameWortmelder, logDrucken, 10);
            }
            lMitteilung.loginKennungAnzeige = BlNummernformBasis
                    .aufbereitenInternFuerExtern(lMitteilung.loginKennung, lDbBundle);
        } else {
            lDbBundle.dbPersonenNatJur.read(lLoginDaten.personenNatJurIdent);
            EclPersonenNatJur lPersonNatJur = lDbBundle.dbPersonenNatJur.PersonNatJurGefunden(0);
            lMitteilung.nameWortmelder = lPersonNatJur.vorname + " " + lPersonNatJur.name;
            lMitteilung.ortWortmelder = lPersonNatJur.ort;
            lMitteilung.loginKennungAnzeige = lMitteilung.loginKennung;
            CaBug.druckeLog("lWortmeldungen.nameWortmelder=" + lMitteilung.nameWortmelder, logDrucken, 10);
        }
        lMitteilung.zeitpunktFuerAnzeige = CaDatumZeit
                .ZeitStringFuerAnzeige(lMitteilung.zeitpunktDerMitteilung);
//        lMitteilung.statusTextIntern = KonstMitteilungStatus.getTextIntern(lMitteilung.status);

    }
    
    
    
    private void entferneNL(EclMitteilung lMitteilung) {
        lMitteilung.mitteilungLangtext = lMitteilung.mitteilungLangtext.replaceAll("\n", "");
        lMitteilung.mitteilungLangtext = lMitteilung.mitteilungLangtext.replaceAll("\r", "");

    }

    /**Verwendet
     * rcMitteilungen
     * 
     * Belegt
     * rcDateiName
     */
    public int schreibeMitteilungenListeInCSV() {
        dbOpenUndWeitere();
        paramBelegen();
        rcDateiName = "";

        String lExportDateiname="";
        switch (lFunktion) {
        case KonstPortalFunktionen.fragen:
            lExportDateiname="fragenListe";
            break;
        case KonstPortalFunktionen.wortmeldungen:
            lExportDateiname="wortmeldungenListe";
            break;
         case KonstPortalFunktionen.widersprueche:
             lExportDateiname="widerspruecheListe";
            break;
         case KonstPortalFunktionen.antraege:
             lExportDateiname="antraegeListe";
             break;
         case KonstPortalFunktionen.sonstigeMitteilungen:
             lExportDateiname="sonstMitteilungenListe";
             break;
         case KonstPortalFunktionen.botschaftenEinreichen:
             lExportDateiname="botschaftenListe";
             break;
        }
        CaDateiWrite dateiExport = new CaDateiWrite();
        dateiExport.trennzeichen = ';';
        dateiExport.dateiart = ".csv";

        dateiExport.oeffne(lDbBundle, lExportDateiname);

        dateiExport.ausgabe("Lfd.Ident");
        dateiExport.ausgabe("Kennung");
        dateiExport.ausgabe("NameSteller");
        if (rcNameAbfragen>0) {
            dateiExport.ausgabe("NameStellerEingegeben");
        }
        if (rcKontaktdatenAbfragen>0) {
            dateiExport.ausgabe("Kontaktdaten");
        }
        if (rcKontaktdatenTelefonAbfragen>0) {
            dateiExport.ausgabe("Kontaktdaten Telefon");
        }
        if (rcHinweisGelesen>0) {
            dateiExport.ausgabe("Hinweis bestätigt");
        }
        dateiExport.ausgabe("Aktien");
        if (rcTopListeAnbieten>0) {
            dateiExport.ausgabe("zu TOP");
        }
        if (rcInhaltsHinweiseVorhandenundAktiv==true) {
            for (int i=0;i<10;i++) {
                if (rcInhaltsHinweiseAktiv[i]==true) {
                    dateiExport.ausgabe(rcInhaltsHinweiseTextDE[i]);
                }
            }
        }
        if (rcKurztextAbfragen>0) {
            dateiExport.ausgabe("KurzText");
        }
        if (rcLangtextAbfragen>0) {
            dateiExport.ausgabe("LangText");
        }
        dateiExport.ausgabe("Zeitpunkt");
        if (rcZurueckziehenMoeglich>0) {
            dateiExport.ausgabe("Zurückgezogen");
            dateiExport.ausgabe("ZurückgezogenZeitpunkt");
        }

        dateiExport.newline();

        for (int i = 0; i < rcMitteilungen.size(); i++) {
            EclMitteilung lMitteilung = rcMitteilungen.get(i);
            dateiExport.ausgabe(Integer.toString(lMitteilung.mitteilungIdent));
            dateiExport.ausgabe(lMitteilung.identString);
            dateiExport.ausgabe(lMitteilung.nameVornameOrtKennung);
            if (rcNameAbfragen>0) {
                dateiExport.ausgabe(lMitteilung.nameVornameOrt);
            }
            if (rcKontaktdatenAbfragen>0) {
                dateiExport.ausgabe(lMitteilung.kontaktDaten);
            }
            if (rcKontaktdatenTelefonAbfragen>0) {
                dateiExport.ausgabe(lMitteilung.kontaktDatenTelefon);
            }
            if (rcHinweisGelesen>0) {
                if (lMitteilung.hinweisWurdeBestaetigt==1) {
                    dateiExport.ausgabe("Ja");
                }
                else {
                    dateiExport.ausgabe("Nein");
                }
            }
             dateiExport.ausgabe(Long.toString(lMitteilung.anzahlAktienZumZeitpunktderMitteilung));
             if (rcTopListeAnbieten>0) {
                 dateiExport.ausgabe(lMitteilung.zuTOPListe);
             }
             if (rcInhaltsHinweiseVorhandenundAktiv==true) {
                 for (int i1=0;i1<10;i1++) {
                     if (rcInhaltsHinweiseAktiv[i1]==true) {
                         if (lMitteilung.inhaltsHinweis[i1]) {
                             dateiExport.ausgabe("Markiert");
                         }
                         else {
                             dateiExport.ausgabe("");
                         }
                     }
                 }
             }
            if (rcKurztextAbfragen>0) {
                dateiExport.ausgabe(lMitteilung.mitteilungKurztext);
            }
            if (rcLangtextAbfragen>0) {
                dateiExport.ausgabe(lMitteilung.mitteilungLangtext);
            }
            dateiExport.ausgabe(lMitteilung.zeitpunktDerMitteilung);
            if (rcZurueckziehenMoeglich>0) {
                if (lMitteilung.status==KonstMitteilungStatus.ZURUECKGEZOGEN) {
                    dateiExport.ausgabe("Ja");
                }
                else {
                    dateiExport.ausgabe("");
                }
                dateiExport.ausgabe(lMitteilung.zeitpunktDesRueckzugs);
            }
            dateiExport.newline();
        }

        dateiExport.schliessen();
        rcDateiName = dateiExport.dateiname;
        rcDateiNamePur = dateiExport.dateinamePur;
        dbClose();

        return 1;
    }

    public int erzeugeReport(RpDrucken rpDrucken, String lfdNummer) {
        dbOpenUndWeitere();
        paramBelegen();

        RpVariablen rpVariablen = new RpVariablen(lDbBundle);
        rpDrucken.initFormular(lDbBundle);
        rpVariablen.mitteilung(lfdNummer, rpDrucken);
        rpDrucken.startFormular();

        for (int i = 0; i < rcMitteilungen.size(); i++) {
            EclMitteilung lMitteilung = rcMitteilungen.get(i);
            rpVariablen.fuelleVariable(rpDrucken, "Art", Integer.toString(lFunktion));
            rpVariablen.fuelleVariable(rpDrucken, "LfdIdent", Integer.toString(lMitteilung.mitteilungIdent));
            rpVariablen.fuelleVariable(rpDrucken, "Kennung", lMitteilung.identString);
            rpVariablen.fuelleVariable(rpDrucken, "NameSteller", lMitteilung.nameVornameOrtKennung);
            
            rpVariablen.fuelleVariable(rpDrucken, "paramNameAbfragen", Integer.toString(rcNameAbfragen));
            if (rcNameAbfragen>0) {
                rpVariablen.fuelleVariable(rpDrucken, "NameStellerEingegeben", lMitteilung.nameVornameOrt);
            }
            else {
                rpVariablen.fuelleVariable(rpDrucken, "NameStellerEingegeben", "");
            }

            rpVariablen.fuelleVariable(rpDrucken, "paramKontaktdatenAbfragen", Integer.toString(rcKontaktdatenAbfragen));
            if (rcKontaktdatenAbfragen>0) {
                rpVariablen.fuelleVariable(rpDrucken, "Kontaktdaten", lMitteilung.kontaktDaten);
            }
            else {
                rpVariablen.fuelleVariable(rpDrucken, "Kontaktdaten", "");
            }
            
            rpVariablen.fuelleVariable(rpDrucken, "paramKontaktdatenTelefonAbfragen", Integer.toString(rcKontaktdatenTelefonAbfragen));
           if (rcKontaktdatenTelefonAbfragen>0) {
                rpVariablen.fuelleVariable(rpDrucken, "KontaktdatenTelefon", lMitteilung.kontaktDatenTelefon);
            }
            else {
                rpVariablen.fuelleVariable(rpDrucken, "KontaktdatenTelefon", "");
            }

            rpVariablen.fuelleVariable(rpDrucken, "paramHinweisGelesen", Integer.toString(rcHinweisGelesen));
            if (rcKontaktdatenAbfragen>0) {
                rpVariablen.fuelleVariable(rpDrucken, "HinweisGelesen", Integer.toString(lMitteilung.hinweisWurdeBestaetigt));
            }
            else {
                rpVariablen.fuelleVariable(rpDrucken, "HinweisGelesen", "");
            }

            rpVariablen.fuelleVariable(rpDrucken, "Aktien", CaString.toStringDE(lMitteilung.anzahlAktienZumZeitpunktderMitteilung));
            
            rpVariablen.fuelleVariable(rpDrucken, "paramTOPListeAbfragen", Integer.toString(rcTopListeAnbieten));
            if (rcTopListeAnbieten>0) {
                rpVariablen.fuelleVariable(rpDrucken, "TOPListe", lMitteilung.zuTOPListe);
            }
            else {
                rpVariablen.fuelleVariable(rpDrucken, "TOPListe", "");
            }
            
            rpVariablen.fuelleVariable(rpDrucken, "inhaltsHinweisLfd1", "");
            rpVariablen.fuelleVariable(rpDrucken, "inhaltsHinweisLfd2", "");
            rpVariablen.fuelleVariable(rpDrucken, "inhaltsHinweisLfd3", "");
            rpVariablen.fuelleVariable(rpDrucken, "inhaltsHinweisLfd4", "");
            rpVariablen.fuelleVariable(rpDrucken, "inhaltsHinweisLfd5", "");
            rpVariablen.fuelleVariable(rpDrucken, "inhaltsHinweisLfd6", "");
            rpVariablen.fuelleVariable(rpDrucken, "inhaltsHinweisLfd7", "");
            rpVariablen.fuelleVariable(rpDrucken, "inhaltsHinweisLfd8", "");
            rpVariablen.fuelleVariable(rpDrucken, "inhaltsHinweisLfd9", "");
            rpVariablen.fuelleVariable(rpDrucken, "inhaltsHinweisLfd10", "");

            if (rcInhaltsHinweiseVorhandenundAktiv) {
                rpVariablen.fuelleVariable(rpDrucken, "inhaltsHinweisListeVorhanden", "1");
                int offset=1;
                for (int i1=0;i1<10;i1++) {
                    if (lMitteilung.inhaltsHinweis[i1]) {
                        rpVariablen.fuelleVariable(rpDrucken, "inhaltsHinweisLfd"+Integer.toString(offset), rcInhaltsHinweiseTextDE[i1]);
                    }
                }
            }
            else {
                rpVariablen.fuelleVariable(rpDrucken, "inhaltsHinweisListeVorhanden", "0");
            }
            
            rpVariablen.fuelleVariable(rpDrucken, "paramKurztextAbfragen", Integer.toString(rcKurztextAbfragen));
            if (rcKurztextAbfragen>0) {
                rpVariablen.fuelleVariable(rpDrucken, "KurzText", lMitteilung.mitteilungKurztext);
            }
            else {
                rpVariablen.fuelleVariable(rpDrucken, "KurzText", "");
            }
           
            rpVariablen.fuelleVariable(rpDrucken, "paramLangtextAbfragen", Integer.toString(rcLangtextAbfragen));
            if (rcLangtextAbfragen>0) {
                rpVariablen.fuelleVariable(rpDrucken, "LangText", lMitteilung.mitteilungLangtext);
            }
            else {
                rpVariablen.fuelleVariable(rpDrucken, "LangText", "");
            }
            
            rpVariablen.fuelleVariable(rpDrucken, "Zeitpunkt",
                    CaDatumZeit.DatumZeitStringFuerAnzeige(lMitteilung.zeitpunktDerMitteilung));
            
            rpVariablen.fuelleVariable(rpDrucken, "paramZurueckziehenMoeglich", Integer.toString(rcZurueckziehenMoeglich));
            if (rcZurueckziehenMoeglich>0) {
                if (lMitteilung.status==KonstMitteilungStatus.ZURUECKGEZOGEN) {
                    rpVariablen.fuelleVariable(rpDrucken, "Zurueckgezogen", "Ja");
                    rpVariablen.fuelleVariable(rpDrucken, "ZurueckgezogenZeitpunkt",
                            CaDatumZeit.DatumZeitStringFuerAnzeige(lMitteilung.zeitpunktDesRueckzugs));
                         }
                else {
                    rpVariablen.fuelleVariable(rpDrucken, "Zurueckgezogen", "");
                    rpVariablen.fuelleVariable(rpDrucken, "ZurueckgezogenZeitpunkt", "");
                }
            }
            else {
                rpVariablen.fuelleVariable(rpDrucken, "Zurueckgezogen", "");
                rpVariablen.fuelleVariable(rpDrucken, "ZurueckgezogenZeitpunkt", "");
            }
            
            rpDrucken.druckenFormular();
        }

        rpDrucken.endeFormular();

        dbClose();
        return 1;
    }

    /**Offset: 0=Fragen, 1=Wortmeldungen, 2=Widersprüche, 3=Anträge, 4=Sonst. Mitteilungen, 5=Botschaften*/
    private void belegeInhaltsHinweise(int offset) {
       ParamPortal lParamPortal=lDbBundle.param.paramPortal;
       rcInhaltsHinweiseTextDE=lParamPortal.inhaltsHinweiseTextDE;
       rcInhaltsHinweiseTextEN=lParamPortal.inhaltsHinweiseTextEN;
       
       rcInhaltsHinweiseVorhandenundAktiv=false;
       rcInhaltsHinweiseAktiv=new boolean[10];
       rcInhaltsHinweiseListe=new LinkedList<EhInhaltsHinweise>();
       
       for (int i=0;i<10;i++) {
           rcInhaltsHinweiseAktiv[i]=lParamPortal.inhaltsHinweiseAktiv[i][offset];
           if (rcInhaltsHinweiseAktiv[i]) {
              rcInhaltsHinweiseVorhandenundAktiv=true;
              EhInhaltsHinweise lEhInhaltsHinweise=new EhInhaltsHinweise();
              lEhInhaltsHinweise.lfdNummer=i;
              lEhInhaltsHinweise.textDE=rcInhaltsHinweiseTextDE[i];
              lEhInhaltsHinweise.textEN=rcInhaltsHinweiseTextEN[i];
              lEhInhaltsHinweise.selektiert=false;
              rcInhaltsHinweiseListe.add(lEhInhaltsHinweise);
           }
       }
        
    }
    
    public void paramBelegen() {
        ParamPortal lParamPortal=lDbBundle.param.paramPortal;

        switch (lFunktion) {
        case KonstPortalFunktionen.rueckfragen:
        case KonstPortalFunktionen.fragen:
            rcStellerAbfragen=lParamPortal.fragenStellerAbfragen;
            rcNameAbfragen=lParamPortal.fragenNameAbfragen;
            rcKontaktdatenAbfragen=lParamPortal.fragenKontaktdatenAbfragen;
            rcKontaktdatenEMailVorschlagen=lParamPortal.fragenKontaktdatenEMailVorschlagen;
            rcKontaktdatenTelefonAbfragen=lParamPortal.fragenKontaktdatenTelefonAbfragen;
            rcKurztextAbfragen=lParamPortal.fragenKurztextAbfragen;
            rcTopListeAnbieten=lParamPortal.fragenTopListeAnbieten;
            belegeInhaltsHinweise(0);
            rcSkIstZuTopListe=KonstSkIst.FRAGEN;
            rcLangtextAbfragen=lParamPortal.fragenLangtextAbfragen;
            rcLangtextUndDateiNurAlternativ=0;
            rcZurueckziehenMoeglich=lParamPortal.fragenZurueckziehenMoeglich;
            rcLaenge=lParamPortal.fragenLaenge;
            rcAnzahlJeAktionaer=lParamPortal.fragenAnzahlJeAktionaer;
            rcStellerZulaessig=lParamPortal.fragenStellerZulaessig;
            rcListeAnzeigen=0;
            rcMailBeiEingang=lParamPortal.fragenMailBeiEingang;
            rcMailVerteiler1=lParamPortal.fragenMailVerteiler1;
            rcMailVerteiler2=lParamPortal.fragenMailVerteiler2;
            rcMailVerteiler3=lParamPortal.fragenMailVerteiler3;
            rcHinweisGelesen=lParamPortal.fragenHinweisGelesen;
            rcHinweisVorbelegenMit=lParamPortal.fragenHinweisVorbelegenMit;
            /*++++Parameter speziell (bzw. nur) für Botschaften hochladen. Aber natürlich auch für was anderes denkbar!+++*/
            rcDateiHochladenMoeglich=false;
            rcVideoDateiMoeglich=0;
            rcVideoZusatz= null;
            rcVideoFormate= null;
            rcVideoLaenge=0;
            rcTextDateiMoeglich=0;
            rcTextDateiZusatz= null;
            rcTextFormate= null;
            rcTextLaenge=0;
            break;
        case KonstPortalFunktionen.wortmeldungen:
            rcStellerAbfragen=lParamPortal.wortmeldungStellerAbfragen;
            rcNameAbfragen=lParamPortal.wortmeldungNameAbfragen;
            rcKontaktdatenAbfragen=lParamPortal.wortmeldungKontaktdatenAbfragen;
            rcKontaktdatenEMailVorschlagen=lParamPortal.wortmeldungKontaktdatenEMailVorschlagen;
            rcKontaktdatenTelefonAbfragen=lParamPortal.wortmeldungKontaktdatenTelefonAbfragen;
            rcKurztextAbfragen=lParamPortal.wortmeldungKurztextAbfragen;
            rcTopListeAnbieten=lParamPortal.wortmeldungTopListeAnbieten;
            rcSkIstZuTopListe=KonstSkIst.WORTMELDUNGEN;
            belegeInhaltsHinweise(1);
            rcLangtextAbfragen=lParamPortal.wortmeldungLangtextAbfragen;
            rcLangtextUndDateiNurAlternativ=0;
            rcZurueckziehenMoeglich=lParamPortal.wortmeldungZurueckziehenMoeglich;
            rcLaenge=lParamPortal.wortmeldungLaenge;
            rcAnzahlJeAktionaer=lParamPortal.wortmeldungAnzahlJeAktionaer;
            rcStellerZulaessig=lParamPortal.wortmeldungStellerZulaessig;
            rcListeAnzeigen=lParamPortal.wortmeldungListeAnzeigen;
            rcMailBeiEingang=lParamPortal.wortmeldungMailBeiEingang;
            rcMailVerteiler1=lParamPortal.wortmeldungMailVerteiler1;
            rcMailVerteiler2=lParamPortal.wortmeldungMailVerteiler2;
            rcMailVerteiler3=lParamPortal.wortmeldungMailVerteiler3;
            rcHinweisGelesen=lParamPortal.wortmeldungHinweisGelesen;
            rcHinweisVorbelegenMit=lParamPortal.wortmeldungHinweisVorbelegenMit;
            /*++++Parameter speziell (bzw. nur) für Botschaften hochladen. Aber natürlich auch für was anderes denkbar!+++*/
            rcDateiHochladenMoeglich=false;
            rcVideoDateiMoeglich=0;
            rcVideoZusatz= null;
            rcVideoFormate= null;
            rcVideoLaenge=0;
            rcTextDateiMoeglich=0;
            rcTextDateiZusatz= null;
            rcTextFormate= null;
            rcTextLaenge=0;
           break;
        case KonstPortalFunktionen.widersprueche:
            rcStellerAbfragen=lParamPortal.widerspruecheStellerAbfragen;
            rcNameAbfragen=lParamPortal.widerspruecheNameAbfragen;
            rcKontaktdatenAbfragen=lParamPortal.widerspruecheKontaktdatenAbfragen;
            rcKontaktdatenEMailVorschlagen=lParamPortal.widerspruecheKontaktdatenEMailVorschlagen;
            rcKontaktdatenTelefonAbfragen=lParamPortal.widerspruecheKontaktdatenTelefonAbfragen;
            rcKurztextAbfragen=lParamPortal.widerspruecheKurztextAbfragen;
            rcTopListeAnbieten=lParamPortal.widerspruecheTopListeAnbieten;
            belegeInhaltsHinweise(2);
            rcSkIstZuTopListe=KonstSkIst.WIDERSPRUECHE;
            rcLangtextAbfragen=lParamPortal.widerspruecheLangtextAbfragen;
            rcLangtextUndDateiNurAlternativ=0;
            rcZurueckziehenMoeglich=lParamPortal.widerspruecheZurueckziehenMoeglich;
            rcLaenge=lParamPortal.widerspruecheLaenge;
            rcAnzahlJeAktionaer=lParamPortal.widerspruecheAnzahlJeAktionaer;
            rcStellerZulaessig=lParamPortal.widerspruecheStellerZulaessig;
            rcListeAnzeigen=0;
            rcMailBeiEingang=lParamPortal.widerspruecheMailBeiEingang;
            rcMailVerteiler1=lParamPortal.widerspruecheMailVerteiler1;
            rcMailVerteiler2=lParamPortal.widerspruecheMailVerteiler2;
            rcMailVerteiler3=lParamPortal.widerspruecheMailVerteiler3;
            rcHinweisGelesen=lParamPortal.widerspruecheHinweisVorbelegenMit;
            rcHinweisVorbelegenMit=lParamPortal.wortmeldungHinweisVorbelegenMit;
            /*++++Parameter speziell (bzw. nur) für Botschaften hochladen. Aber natürlich auch für was anderes denkbar!+++*/
            rcDateiHochladenMoeglich=false;
            rcVideoDateiMoeglich=0;
            rcVideoZusatz= null;
            rcVideoFormate= null;
            rcVideoLaenge=0;
            rcTextDateiMoeglich=0;
            rcTextDateiZusatz= null;
            rcTextFormate= null;
            rcTextLaenge=0;
           break;
        case KonstPortalFunktionen.antraege:
            rcStellerAbfragen=lParamPortal.antraegeStellerAbfragen;
            rcNameAbfragen=lParamPortal.antraegeNameAbfragen;
            rcKontaktdatenAbfragen=lParamPortal.antraegeKontaktdatenAbfragen;
            rcKontaktdatenEMailVorschlagen=lParamPortal.antraegeKontaktdatenEMailVorschlagen;
            rcKontaktdatenTelefonAbfragen=lParamPortal.antraegeKontaktdatenTelefonAbfragen;
            rcKurztextAbfragen=lParamPortal.antraegeKurztextAbfragen;
            rcTopListeAnbieten=lParamPortal.antraegeTopListeAnbieten;
            belegeInhaltsHinweise(3);
            rcSkIstZuTopListe=KonstSkIst.ANTRAEGE;
            rcLangtextAbfragen=lParamPortal.antraegeLangtextAbfragen;
            rcLangtextUndDateiNurAlternativ=0;
            rcZurueckziehenMoeglich=lParamPortal.antraegeZurueckziehenMoeglich;
            rcLaenge=lParamPortal.antraegeLaenge;
            rcAnzahlJeAktionaer=lParamPortal.antraegeAnzahlJeAktionaer;
            rcStellerZulaessig=lParamPortal.antraegeStellerZulaessig;
            rcListeAnzeigen=0;
            rcMailBeiEingang=lParamPortal.antraegeMailBeiEingang;
            rcMailVerteiler1=lParamPortal.antraegeMailVerteiler1;
            rcMailVerteiler2=lParamPortal.antraegeMailVerteiler2;
            rcMailVerteiler3=lParamPortal.antraegeMailVerteiler3;
            rcHinweisGelesen=lParamPortal.antraegeHinweisGelesen;
            rcHinweisVorbelegenMit=lParamPortal.antraegeHinweisVorbelegenMit;
            /*++++Parameter speziell (bzw. nur) für Botschaften hochladen. Aber natürlich auch für was anderes denkbar!+++*/
            rcDateiHochladenMoeglich=false;
            rcVideoDateiMoeglich=0;
            rcVideoZusatz= null;
            rcVideoFormate= null;
            rcVideoLaenge=0;
            rcTextDateiMoeglich=0;
            rcTextDateiZusatz= null;
            rcTextFormate= null;
            rcTextLaenge=0;
           break;
        case KonstPortalFunktionen.sonstigeMitteilungen:
            rcStellerAbfragen=lParamPortal.sonstMitteilungenStellerAbfragen;
            rcNameAbfragen=lParamPortal.sonstMitteilungenNameAbfragen;
            rcKontaktdatenAbfragen=lParamPortal.sonstMitteilungenKontaktdatenAbfragen;
            rcKontaktdatenEMailVorschlagen=lParamPortal.sonstMitteilungenKontaktdatenEMailVorschlagen;
            rcKontaktdatenTelefonAbfragen=lParamPortal.sonstMitteilungenKontaktdatenTelefonAbfragen;
            rcKurztextAbfragen=lParamPortal.sonstMitteilungenKurztextAbfragen;
            rcTopListeAnbieten=lParamPortal.sonstMitteilungenTopListeAnbieten;
            belegeInhaltsHinweise(4);
            rcSkIstZuTopListe=KonstSkIst.SONSTMITTEILUNGEN;
            rcLangtextAbfragen=lParamPortal.sonstMitteilungenLangtextAbfragen;
            rcLangtextUndDateiNurAlternativ=0;
            rcZurueckziehenMoeglich=lParamPortal.sonstMitteilungenZurueckziehenMoeglich;
            rcLaenge=lParamPortal.sonstMitteilungenLaenge;
            rcAnzahlJeAktionaer=lParamPortal.sonstMitteilungenAnzahlJeAktionaer;
            rcStellerZulaessig=lParamPortal.sonstMitteilungenStellerZulaessig;
            rcListeAnzeigen=0;
            rcMailBeiEingang=lParamPortal.sonstMitteilungenMailBeiEingang;
            rcMailVerteiler1=lParamPortal.sonstMitteilungenMailVerteiler1;
            rcMailVerteiler2=lParamPortal.sonstMitteilungenMailVerteiler2;
            rcMailVerteiler3=lParamPortal.sonstMitteilungenMailVerteiler3;
            rcHinweisGelesen=lParamPortal.sonstMitteilungenHinweisGelesen;
            rcHinweisVorbelegenMit=lParamPortal.sonstMitteilungenHinweisVorbelegenMit;
            /*++++Parameter speziell (bzw. nur) für Botschaften hochladen. Aber natürlich auch für was anderes denkbar!+++*/
            rcDateiHochladenMoeglich=false;
            rcVideoDateiMoeglich=0;
            rcVideoZusatz= null;
            rcVideoFormate= null;
            rcVideoLaenge=0;
            rcTextDateiMoeglich=0;
            rcTextDateiZusatz= null;
            rcTextFormate= null;
            rcTextLaenge=0;
            break;
        case KonstPortalFunktionen.botschaftenEinreichen:
            rcStellerAbfragen=lParamPortal.botschaftenStellerAbfragen;
            rcNameAbfragen=lParamPortal.botschaftenNameAbfragen;
            rcKontaktdatenAbfragen=lParamPortal.botschaftenKontaktdatenAbfragen;
            rcKontaktdatenEMailVorschlagen=lParamPortal.botschaftenKontaktdatenEMailVorschlagen;
            rcKontaktdatenTelefonAbfragen=lParamPortal.botschaftenKontaktdatenTelefonAbfragen;
            rcKurztextAbfragen=lParamPortal.botschaftenKurztextAbfragen;
            rcTopListeAnbieten=lParamPortal.botschaftenTopListeAnbieten;
            belegeInhaltsHinweise(5);
            rcSkIstZuTopListe=KonstSkIst.BOTSCHAFTEN_EINREICHEN;
            rcLangtextAbfragen=lParamPortal.botschaftenLangtextAbfragen;
            rcLangtextUndDateiNurAlternativ=lParamPortal.botschaftenLangtextUndDateiNurAlternativ;
            rcZurueckziehenMoeglich=lParamPortal.botschaftenZurueckziehenMoeglich;
            rcLaenge=lParamPortal.botschaftenLaenge;
            rcAnzahlJeAktionaer=lParamPortal.botschaftenAnzahlJeAktionaer;
            rcStellerZulaessig=lParamPortal.botschaftenStellerZulaessig;
            rcListeAnzeigen=0;
            rcMailBeiEingang=lParamPortal.botschaftenMailBeiEingang;
            rcMailVerteiler1=lParamPortal.botschaftenMailVerteiler1;
            rcMailVerteiler2=lParamPortal.botschaftenMailVerteiler2;
            rcMailVerteiler3=lParamPortal.botschaftenMailVerteiler3;
            rcHinweisGelesen=lParamPortal.botschaftenHinweisGelesen;
            rcHinweisVorbelegenMit=lParamPortal.botschaftenHinweisVorbelegenMit;
            /*++++Parameter speziell (bzw. nur) für Botschaften hochladen. Aber natürlich auch für was anderes denkbar!+++*/
            if (lParamPortal.botschaftenVideo!=0 || lParamPortal.botschaftenTextDatei!=0) {
                rcDateiHochladenMoeglich=true;
            }
            else {
                rcDateiHochladenMoeglich=false;
            }
            rcVideoDateiMoeglich=lParamPortal.botschaftenVideo;
            rcVideoZusatz= lParamPortal.botschaftenVideoZusatz;
            rcVideoFormate= lParamPortal.botschaftenVideoFormate;
            rcVideoLaenge=lParamPortal.botschaftenVideoLaenge;
            rcTextDateiMoeglich=lParamPortal.botschaftenTextDatei;
            rcTextDateiZusatz= lParamPortal.botschaftenTextZusatz;
            rcTextFormate= lParamPortal.botschaftenTextFormate;
            rcTextLaenge=lParamPortal.botschaftenTextDateiLaenge;
            break;
         }
    }
    
    
    
    
    /***************************************Verwaltung Wortmeldungen*************************************************/

//    /**Alle, die gesprochen haben*/
//    public int alleGesprochenenWortmeldungen = 0;
//
//    /**Alle noch nicht gesprochenen Wortmeldungen in Liste*/
//    public int alleAufzurufendeWortmeldungen = 0;
//
//    /**Alle zu bearbeitenden Wortmeldungen*/
//    public int alleZuBearbeitendenWortmeldungen = 0;
//
// 
//    /**Redner, die für den Test bereit stehen*/
//    public List<EclMitteilung> rednerListeTest = null;
//
//    public List<EclMitteilung> rednerListeTelefonie = null;
//
//    /**Alle Redner, die noch nicht erledigt sind ("Eingreif-Liste" für Koordination)*/
//    public List<EclMitteilung> rednerListeGesamt = null;;
//
//    public List<EclMitteilung> wortmeldungenZuBearbeitenDurchKoordination = null;;
//
//    /**Alle Redner, die erledigt sind*/ 
//    public List<EclMitteilung> wortmeldungenErledigt = null;;

    
    /**+++++++++++++Elemente für "Universal-View"++++++++++++*/
    /**Summen, die in der Versammlungsleiterview einzeln je Zeile angezeigt werden*/
    public List<EhJsfSummeWortmeldeView> summenVersammlungsleiterEinzeln=null;
    public List<EhJsfSummeWortmeldeView> summenVersammlungsleiterZusammen=null;
 
    /**Summen, die der eigentliche View sieht*/
    public List<EhJsfSummeWortmeldeView> summenView=null;
    public List<EhJsfSummeWortmeldeView> summenViewZusammen=null;
 
    public EclWortmeldetischView wortmeldetischView=null;
    public EclWortmeldetischView wortmeldetischViewVersammlungsleiter=null;
    
    public String rednerlisteVersammlungsleitung = "";
    public List<EclMitteilung> rednerlisteVersammlungsleitungAlsListe = null;
    public String infoVersammlungsleiter="";

    public ArrayList<LinkedList<EclMitteilung>> rednerlistenKoordination = null;

    
    public int erzeugeVerwaltungWortmeldungen(EclAbstimmungSet pAbstimmungSet, int pSkIstZuTopListe, int pView) {
        
        initAbstimmungslisten(pAbstimmungSet, pSkIstZuTopListe);
        
//        alleGesprochenenWortmeldungen = 0;
//        alleAufzurufendeWortmeldungen = 0;
//        alleZuBearbeitendenWortmeldungen = 0;

        rednerlisteVersammlungsleitung = "";
        rednerlisteVersammlungsleitungAlsListe = new LinkedList<EclMitteilung>();
        infoVersammlungsleiter="";
//        rednerListeTest = new LinkedList<EclMitteilung>();
//        rednerListeTelefonie = new LinkedList<EclMitteilung>();
//
//        rednerListeGesamt = new LinkedList<EclMitteilung>();
//        wortmeldungenZuBearbeitenDurchKoordination = new LinkedList<EclMitteilung>();
//        wortmeldungenErledigt = new LinkedList<EclMitteilung>();

        dbOpenUndWeitere();
        /**Rednerlisten belegen*/
        rcRednerliste = new LinkedList<EclMitteilung>();
        lDbBundle.dbMitteilung.setzeFunktion(KonstPortalFunktionen.wortmeldungen);
        lDbBundle.dbMitteilung.readAll_rednerliste();
        int anz = lDbBundle.dbMitteilung.anzErgebnis();

        
        /*****************Ab hier Universalview************************************************/
        rednerlisteVersammlungsleitung = "";
        rednerlisteVersammlungsleitungAlsListe = new LinkedList<EclMitteilung>();
        rednerlistenKoordination = new ArrayList<LinkedList<EclMitteilung>>();
        for (int i=0;i<=10;i++) {
            rednerlistenKoordination.add(new LinkedList<EclMitteilung>());
        }
                
        wortmeldetischView=lDbBundle.param.paramPortal.wortmeldetischViewArray[pView];
        wortmeldetischViewVersammlungsleiter=lDbBundle.param.paramPortal.wortmeldetischViewArray[1];
        
        /**Wird nur als Zwischenspeicher verwendet. Am Ende überführen in 
         * summenVersammlungsleiterEinzeln und summenVersammlungsleiterZusammen
         */
        int[] summenWerteVersammlungsleiterView={0,0,0,0,0,0,0,0,0,0};
        int[] summenWerteView={0,0,0,0,0,0,0,0,0,0};

        /**Views, die aufbereitet werden:
         * pView= dargestellter View (wird nicht gefüllt, wenn pView=Versammlungsleiterview, dann wird Versammlungsleiter-View gefüllt).
         * Falls pView==3 (Koordination), dann "anzuzeigende Views" füllen.
         * Fall versammlungsleiterView true, dann wird zusätzliche Versammlungsleiter-View gefüllt
         */
        boolean versammlungsleiterViewFuellen=false;
        if (pView==1 || wortmeldetischView.sichtVersammlungsleiterAnzeigen==1) {
            versammlungsleiterViewFuellen=true;
            rednerlisteVersammlungsleitung="";
            rednerlisteVersammlungsleitungAlsListe=new LinkedList<EclMitteilung>();
            BlInfo blInfo=new BlInfo(lDbBundle);
            EclInfo lInfo=blInfo.liefereNachrichtFuerVersamnmlungsleiter();
            infoVersammlungsleiter=lInfo.infoText;
       }
        
        
        /*1. Durchlauf: Ermitteln der Summen.
         * Separater Durchlauf wird benötigt, da Summen ggf. bei rednerlisteVersammlungsleitung am Anfang
         * eingefügt werden müssen
         */
        for (int i=0;i<anz;i++) {
            CaBug.druckeLog("Schleife 1 i="+i, logDrucken, 10);
            EclMitteilung lWortmeldung = lDbBundle.dbMitteilung.ergebnisPosition(i);
            int statusNr=KonstMitteilungStatus.liefereNurStatus(lWortmeldung.status);
            CaBug.druckeLog("i="+i+" statusNr="+statusNr, logDrucken, 10);
            /*Summen ggf. aufaddieren*/
            for (int i1=0;i1<10;i1++) {
                CaBug.druckeLog("i1="+i1, logDrucken, 10);
                int aufaddierenInSumme=lDbBundle.param.paramPortal.wortmeldetischViewArray[1].statusArray[statusNr].aufaddierenInSumme[i1];
                if (aufaddierenInSumme!=0) {
                    summenWerteVersammlungsleiterView[i1]++;
                }
                aufaddierenInSumme=lDbBundle.param.paramPortal.wortmeldetischViewArray[pView].statusArray[statusNr].aufaddierenInSumme[i1];
                if (aufaddierenInSumme!=0) {
                    summenWerteView[i1]++;
                }
            }
        }
        
        
        /*Summen in Anzeige-Struktur übertragen*/
        summenVersammlungsleiterEinzeln=new LinkedList<EhJsfSummeWortmeldeView>();
        summenVersammlungsleiterZusammen=new LinkedList<EhJsfSummeWortmeldeView>();
        summenView=new LinkedList<EhJsfSummeWortmeldeView>();
        summenViewZusammen=new LinkedList<EhJsfSummeWortmeldeView>();
        
         for (int i=0;i<10;i++) {
            /**Versammlungsleiterview*/
            if (wortmeldetischViewVersammlungsleiter.summeAnzeigen[i]==1) {
                EhJsfSummeWortmeldeView lSummeWortmeldeView=new EhJsfSummeWortmeldeView();
                lSummeWortmeldeView.textVorZahl=wortmeldetischViewVersammlungsleiter.summeTextVorZahl[i];
                lSummeWortmeldeView.textNachZahl=wortmeldetischViewVersammlungsleiter.summeTextNachZahl[i];
                lSummeWortmeldeView.wert=summenWerteVersammlungsleiterView[i];
                if (i<5) {
                    summenVersammlungsleiterEinzeln.add(lSummeWortmeldeView);
                }
                else {
                    summenVersammlungsleiterZusammen.add(lSummeWortmeldeView);
                }
            }
            /**Eigentlicher View*/
            if (wortmeldetischView.summeAnzeigen[i]==1) {
                EhJsfSummeWortmeldeView lSummeWortmeldeView=new EhJsfSummeWortmeldeView();
                lSummeWortmeldeView.textVorZahl=wortmeldetischView.summeTextVorZahl[i];
                lSummeWortmeldeView.textNachZahl=wortmeldetischView.summeTextNachZahl[i];
                lSummeWortmeldeView.wert=summenWerteView[i];
                if (i<5) {
                    summenView.add(lSummeWortmeldeView);
                }
                else {
                    summenViewZusammen.add(lSummeWortmeldeView);
                }
            }
            
        }
        
         /*Summen in rednerlisteVersammlungsleitung-String am Anfang einfügen*/
         rednerlisteVersammlungsleitung ="";
         boolean einzelzeilenGefuellt=false;
         for (int i=0;i<summenVersammlungsleiterEinzeln.size();i++) {
             einzelzeilenGefuellt=true;
             EhJsfSummeWortmeldeView lSummeWortmeldeView=summenVersammlungsleiterEinzeln.get(i);
             rednerlisteVersammlungsleitung+=lSummeWortmeldeView.textVorZahl+Integer.toString(lSummeWortmeldeView.wert)+lSummeWortmeldeView.textNachZahl+"\n\r";
         }
         boolean sammelzeileGefuellt=false;
         for (int i=0;i<summenVersammlungsleiterZusammen.size();i++) {
             sammelzeileGefuellt=true;
             EhJsfSummeWortmeldeView lSummeWortmeldeView=summenVersammlungsleiterZusammen.get(i);
             rednerlisteVersammlungsleitung+=lSummeWortmeldeView.textVorZahl+Integer.toString(lSummeWortmeldeView.wert)+lSummeWortmeldeView.textNachZahl;
         }
         if (sammelzeileGefuellt) {
             /*Neue Zeile nach sammelzeile*/
             rednerlisteVersammlungsleitung+="\n\r";
         }
         if (einzelzeilenGefuellt || sammelzeileGefuellt) {
             /*Leerzeile nach Summen*/
             rednerlisteVersammlungsleitung+="\n\r";
         }
         /*Überschrift Versammlungsleiter*/
         rednerlisteVersammlungsleitung+="Offene Rednerliste (mit Reihenfolge (Nummer), Namen und Status):"+ "\n\r";
                 

         int anzTestRaum=lDbBundle.param.paramPortal.konfRaumAnzahl[0];
         int anzRedeRaum=lDbBundle.param.paramPortal.konfRaumAnzahl[1];
         

        /*2. Durchlauf - Listen füllen*/
        for (int i=0;i<anz;i++) {
            EclMitteilung lWortmeldung = lDbBundle.dbMitteilung.ergebnisPosition(i);
            ergaenzeMitteilung(lWortmeldung);
            ergaenzeMitteilungZuTOPListeUndInhaltsHinweise(lWortmeldung);
            if (wortmeldetischView.praesenzAnzeigeAusfuehren==1) {
                ergaenzeMitteilungPrasenzStatus(lWortmeldung);
            }
            lWortmeldung.buttonsVersammlungsleiter=new LinkedList<EhJsfButton>();
            lWortmeldung.buttonKoordination = new ArrayList<LinkedList<EhJsfButton>>();
            for (int i1=0;i1<=10;i1++) {
                lWortmeldung.buttonKoordination.add(new LinkedList<EhJsfButton>());
            }

            int statusNr=KonstMitteilungStatus.liefereNurStatus(lWortmeldung.status);
            CaBug.druckeLog("Schleife 2 i="+i+" lfdNr="+lWortmeldung.lfdNrInListe+ "statusNr="+statusNr, logDrucken, 8);
            if (versammlungsleiterViewFuellen) {
                CaBug.druckeLog("Versammlungsleiterview füllen", logDrucken, 8);
                if (wortmeldetischViewVersammlungsleiter.statusArray[statusNr].anzeigeDesStatusInDieserView==1 && rednerlisteVersammlungsleitungAlsListe.size() < lDbBundle.param.paramPortal.wortmeldungVLListeAnzeigen) {
                    CaBug.druckeLog("in Versammlungsleiter aufnehmen", logDrucken, 8);
                    int anzButtons=wortmeldetischViewVersammlungsleiter.statusArray[statusNr].wortmeldetischFolgeStatusList.size();
                    for (int i1=0;i1<anzButtons;i1++) {
                        EclWortmeldetischFolgeStatus lWortmeldetischFolgeStatus=wortmeldetischViewVersammlungsleiter.statusArray[statusNr].wortmeldetischFolgeStatusList.get(i1);
                        int buttonJeRaum=lWortmeldetischFolgeStatus.buttonJeRaum;
                        if (buttonJeRaum==0) {
                            EhJsfButton hJsfButton=new EhJsfButton(lWortmeldetischFolgeStatus.buttonBezeichnung, lWortmeldetischFolgeStatus.folgeStatusBezeichnung);
                            lWortmeldung.buttonsVersammlungsleiter.add(hJsfButton);
                        }
                        if (buttonJeRaum==1) {/*Ein Button je Testraum*/
                            for (int i3=1;i3<=anzTestRaum;i3++) {
                                EhJsfButton hJsfButton=new EhJsfButton(lWortmeldetischFolgeStatus.buttonBezeichnung, lWortmeldetischFolgeStatus.folgeStatusBezeichnung);
                                hJsfButton.testRaumNr=i3;
                                hJsfButton.buttonLabel=hJsfButton.buttonLabel+" "+Integer.toString(i3);
                                lWortmeldung.buttonsVersammlungsleiter.add(hJsfButton);
                            }
                        }
                        if (buttonJeRaum==2) {/*Ein Button je Rederaum*/
                            for (int i3=1;i3<=anzRedeRaum;i3++) {
                                EhJsfButton hJsfButton=new EhJsfButton(lWortmeldetischFolgeStatus.buttonBezeichnung, lWortmeldetischFolgeStatus.folgeStatusBezeichnung);
                                hJsfButton.redeRaumNr=i3;
                                hJsfButton.buttonLabel=hJsfButton.buttonLabel+" "+Integer.toString(i3);
                                lWortmeldung.buttonsVersammlungsleiter.add(hJsfButton);
                            }
                        }
                    }
                    rednerlisteVersammlungsleitungAlsListe.add(lWortmeldung);
                    rednerlisteVersammlungsleitung += "(" + Integer.toString(lWortmeldung.lfdNrInListe) + ") "
                            + lWortmeldung.nameVornameOrt/*wortmelder*/ + " "
                            + wortmeldetischViewVersammlungsleiter.statusArray[statusNr].anzeigeTextInDieserView ;
                    if (!lWortmeldung.kommentarVersammlungsleiter.isEmpty()) {
                        rednerlisteVersammlungsleitung+=" ("+lWortmeldung.kommentarVersammlungsleiter+")";
                    }
                    rednerlisteVersammlungsleitung += "\n\r";
                }
            }
            /**SubViews füllen*/
            for (int i1=0;i1<10;i1++) {
                int viewInKoordination=wortmeldetischView.subViewsIdent[i1];
                if (viewInKoordination!=0) {
                    EclWortmeldetischView lWortmeldetischView=lDbBundle.param.paramPortal.wortmeldetischViewArray[viewInKoordination];
                    CaBug.druckeLog(" Viewbezeichnung="+lWortmeldetischView.statusArray[statusNr].anzeigeTextInDieserView, logDrucken, 8);
                    if (lWortmeldetischView.statusArray[statusNr].anzeigeDesStatusInDieserView==1) {
                        int anzButtons=lWortmeldetischView.statusArray[statusNr].wortmeldetischFolgeStatusList.size();
                        CaBug.druckeLog("in View aufnehmen anzButtons="+anzButtons, logDrucken, 8);
                        for (int i2=0;i2<anzButtons;i2++) {
                            EclWortmeldetischFolgeStatus lWortmeldetischFolgeStatus=lWortmeldetischView.statusArray[statusNr].wortmeldetischFolgeStatusList.get(i2);
                            int buttonJeRaum=lWortmeldetischFolgeStatus.buttonJeRaum;
                            CaBug.druckeLog("buttonJeRaum="+buttonJeRaum, logDrucken, 8);
                           if (buttonJeRaum==0) {
                                EhJsfButton hJsfButton=new EhJsfButton(lWortmeldetischFolgeStatus.buttonBezeichnung, lWortmeldetischFolgeStatus.folgeStatusBezeichnung);
                                lWortmeldung.buttonKoordination.get(i1).add(hJsfButton);
                            }
                            if (buttonJeRaum==1) {/*Ein Button je Testraum*/
                                for (int i3=1;i3<=anzTestRaum;i3++) {
                                    EhJsfButton hJsfButton=new EhJsfButton(lWortmeldetischFolgeStatus.buttonBezeichnung, lWortmeldetischFolgeStatus.folgeStatusBezeichnung);
                                    hJsfButton.testRaumNr=i3;
                                    hJsfButton.buttonLabel=hJsfButton.buttonLabel+" "+Integer.toString(i3);
                                    lWortmeldung.buttonKoordination.get(i1).add(hJsfButton);
                                }
                            }
                            if (buttonJeRaum==2) {/*Ein Button je Rederaum*/
                                for (int i3=1;i3<=anzRedeRaum;i3++) {
                                    EhJsfButton hJsfButton=new EhJsfButton(lWortmeldetischFolgeStatus.buttonBezeichnung, lWortmeldetischFolgeStatus.folgeStatusBezeichnung);
                                    hJsfButton.redeRaumNr=i3;
                                    hJsfButton.buttonLabel=hJsfButton.buttonLabel+" "+Integer.toString(i3);
                                    lWortmeldung.buttonKoordination.get(i1).add(hJsfButton);
                                }
                            }
                        }
                        CaBug.druckeLog("add Wortmeldung i1="+i1, logDrucken, 8);
                        rednerlistenKoordination.get(i1).add(lWortmeldung);
                    }



                }
            }   
        }


        
        /**********************************Ab hier alt*************************/
//        /*1. Durchlauf: Ermitteln der Summen*/
//        for (int i = 0; i < anz; i++) {
//            EclMitteilung lWortmeldung = lDbBundle.dbMitteilung.ergebnisPosition(i);
//            ermittleButtons(lWortmeldung);
//            
//            if (KonstMitteilungStatus.inRednerlisteGesamt(lWortmeldung.status)) {
//                alleAufzurufendeWortmeldungen++;
//            }
//            if (lWortmeldung.status == KonstMitteilungStatus.ERLEDIGT_GESPROCHEN) {
//                alleGesprochenenWortmeldungen++;
//            }
//            if (KonstMitteilungStatus.inArbeit(lWortmeldung.status)) {
//                alleZuBearbeitendenWortmeldungen++;
//            }
//        }

//        rednerlisteVersammlungsleitung = "Stand der Wortmeldungen: "+ "\n\r"+"Aktuell vorliegende Wortmeldungen: "+(alleAufzurufendeWortmeldungen+alleZuBearbeitendenWortmeldungen)+ "\n\r"
//                /*+ "Wortmeldungen bereit zum Aufrufen: " + alleAufzurufendeWortmeldungen + "\n\r"*/
//                + "("+alleZuBearbeitendenWortmeldungen+" Wortmeldungen in Arbeit; " + alleGesprochenenWortmeldungen+ " erledigte Wortmeldungen) \n\r" + "\n\r"
//                + "Offene Rednerliste (mit Reihenfolge (Nummer), Namen und Status):"+ "\n\r";

//        CaBug.druckeLog("wortmeldungVLListeAnzeigen="+lDbBundle.param.paramPortal.wortmeldungVLListeAnzeigen, logDrucken, 10);
//        /*2. Durchlauf - Listen füllen*/
//        for (int i = 0; i < anz; i++) {
//            EclMitteilung lWortmeldung = lDbBundle.dbMitteilung.ergebnisPosition(i);
//            ergaenzeMitteilung(lWortmeldung);
//            ergaenzeMitteilungZuTOPListeUndInhaltsHinweise(lWortmeldung);
//            CaBug.druckeLog("i="+i+" lWortmeldung.status="+lWortmeldung.status+" KonstMitteilungStatus.inRednerlisteVersammlungsleitung(lWortmeldung.status)="+KonstMitteilungStatus.inRednerlisteVersammlungsleitung(lWortmeldung.status)+" rednerListeGesamt.size()="+rednerListeGesamt.size(), logDrucken, 10);
            
//            if (KonstMitteilungStatus.inRednerlisteVersammlungsleitung(lWortmeldung.status)) {
//                if (rednerlisteVersammlungsleitungAlsListe.size() < lDbBundle.param.paramPortal.wortmeldungVLListeAnzeigen) {
//                    rednerlisteVersammlungsleitungAlsListe.add(lWortmeldung);
//                    rednerlisteVersammlungsleitung += "(" + Integer.toString(lWortmeldung.lfdNrInListe) + ") "
//                            + lWortmeldung.nameVornameOrt/*wortmelder*/ + " "
//                            + KonstMitteilungStatus.getTextVorsitz(lWortmeldung.status) + "\n\r";
//                    CaBug.druckeLog("Aufgenommen in Versammlungsleiterliste i="+i, logDrucken, 10);
//                }
//            }

//            if (KonstMitteilungStatus.inRednerlisteTelefonie(lWortmeldung.status)) {
//                rednerListeTelefonie.add(lWortmeldung);
//            }
//
//            if (KonstMitteilungStatus.inTestliste(lWortmeldung.status)) {
//                rednerListeTest.add(lWortmeldung);
//            }
//
//            if (KonstMitteilungStatus.zuBearbeitenDurchKoordination(lWortmeldung.status)) {
//                wortmeldungenZuBearbeitenDurchKoordination.add(lWortmeldung);
//            }
//
//            if (KonstMitteilungStatus.erledigt(lWortmeldung.status)) {
//                wortmeldungenErledigt.add(lWortmeldung);
//            }
//            else {
//                rednerListeGesamt.add(lWortmeldung);
//            }
//
//        }

        dbClose();

        return 1;

    }

    
//    private void ermittleButtons(EclMitteilung pWortmeldung) {
//        pWortmeldung.buttonsInRednerListeTest=KonstMitteilungStatus.buttonInRednerListeTest(pWortmeldung.status);
//        pWortmeldung.buttonsInRednerListeTelefonie=KonstMitteilungStatus.buttonInRednerListeTelefonie(pWortmeldung.status);
//        pWortmeldung.buttonsInRednerListeBearbeitungKoordination=KonstMitteilungStatus.buttonInBearbeitungsListeKoordination(pWortmeldung.status);
//        pWortmeldung.buttonsInGesamtListe=KonstMitteilungStatus.buttonInGesamtListe(pWortmeldung.status);
//    }
    
    
    /***************************Routinen für "zuTopListe"**********************/
    
    private List<EclAbstimmung> agenda=null;
    private List<EclAbstimmung> gegenantraege=null;

    private void initAbstimmungslisten(EclAbstimmungSet pAbstimmungSet, int pSkIstZuTopListe) {
        int gattungen[]= {0,1,1,1,1,1};
        BlAbstimmungsListe blAbstimmungsliste=new BlAbstimmungsListe();
        blAbstimmungsliste.leseWeisungsliste(pAbstimmungSet, gattungen, pSkIstZuTopListe, KonstWeisungserfassungSicht.portalWeisungserfassung, false);
        agenda=blAbstimmungsliste.rcAgenda;
        gegenantraege=blAbstimmungsliste.rcGegenantraege;
    }
    
    private void ergaenzeMitteilungZuTOPListeUndInhaltsHinweise(EclMitteilung pMitteilung) {
        int gef=0;
        String topListe="";
        List<EclAbstimmung> abstimmungenListeM=agenda;
        if (agenda!=null) {
            for (int i=0;i<abstimmungenListeM.size();i++) {
                EclAbstimmung lAbstimmung=abstimmungenListeM.get(i);
                int lIdentWeisungssatz=lAbstimmung.identWeisungssatz;
                if (lIdentWeisungssatz!=-1) {
                    if (pMitteilung.mitteilungZuTop[lIdentWeisungssatz]==1) {
                        if (gef!=0) {topListe+="; ";}
                        topListe+=lAbstimmung.nummer;
                        if (!lAbstimmung.nummerindex.isEmpty()) {
                            topListe+=" "+lAbstimmung.nummerindex;
                        }
                        gef=1;
                    }
                }
            }
        }
        abstimmungenListeM=gegenantraege;
        if (gegenantraege!=null) {
            for (int i=0;i<abstimmungenListeM.size();i++) {
                EclAbstimmung lAbstimmung=abstimmungenListeM.get(i);
                int lIdentWeisungssatz=lAbstimmung.identWeisungssatz;
                if (lIdentWeisungssatz!=-1) {
                    if (pMitteilung.mitteilungZuTop[lIdentWeisungssatz]==1) {
                        if (gef!=0) {topListe+="; ";}
                        topListe+=lAbstimmung.nummer;
                        if (!lAbstimmung.nummerindex.isEmpty()) {
                            topListe+=" "+lAbstimmung.nummerindex;
                        }
                        gef=1;
                    }
                }
            }
        }
        if (topListe.isEmpty() && rcTopListeAnbieten>0) {
            topListe="-";
        }
        pMitteilung.zuTOPListe=topListe;
        
        List<EhInhaltsHinweise> lInhaltsHinweiseListe=new LinkedList<EhInhaltsHinweise>();
        if (rcInhaltsHinweiseListe!=null) {
            for (EhInhaltsHinweise iInhaltsHinweis:rcInhaltsHinweiseListe) {
                if (pMitteilung.inhaltsHinweis[iInhaltsHinweis.lfdNummer]) {
                    EhInhaltsHinweise lInhaltsHinweis=new EhInhaltsHinweise();
                    lInhaltsHinweis.lfdNummer=iInhaltsHinweis.lfdNummer;
                    lInhaltsHinweis.textDE=iInhaltsHinweis.textDE;
                    lInhaltsHinweis.textEN=iInhaltsHinweis.textEN;
                    lInhaltsHinweis.selektiert=true;
                    lInhaltsHinweiseListe.add(lInhaltsHinweis);
                    pMitteilung.inhaltsHinweiseVorhandenundAktiv=true;
                }
            }
        }
        pMitteilung.inhaltsHinweiseListe=lInhaltsHinweiseListe;
    }
    
    
    private void ergaenzeMitteilungPrasenzStatus(EclMitteilung pMitteilung) {
       String lLoginKennung = pMitteilung.loginKennung;
        BlTeilnehmerLoginNeu blTeilnehmerLogin = new BlTeilnehmerLoginNeu();
        blTeilnehmerLogin.initDB(lDbBundle);
        int erg = blTeilnehmerLogin.findeUndPruefeKennung(lLoginKennung, "", false);
        if (erg < 0) {
            return;
        }

        if (lDbBundle.param.paramPortal.varianteDialogablauf == 1) {
            /*Hinweis: für Vereins-Portal noch nicht getestet!*/
            CaBug.druckeLog("Vollen Status einlesen", logDrucken, 5);

            BlWillenserklaerungStatusNeu blWillenserklaerungStatusVoll = new BlWillenserklaerungStatusNeu(lDbBundle);
            
            blWillenserklaerungStatusVoll.nurNichtStornierteWillenserklaerungen=true;
            blWillenserklaerungStatusVoll.umZugeordneteKennungenErgaenzen=true;
            
//            CaBug.druckeLog(eclLoginDatenM.getEclPersonenNatJur().name, logDrucken, 1);
            blWillenserklaerungStatusVoll.initAusgangsdaten(blTeilnehmerLogin.eclLoginDaten, blTeilnehmerLogin.eclAktienregister, blTeilnehmerLogin.eclPersonenNatJur);
            blWillenserklaerungStatusVoll.piAusblendenMeldungen = null; /*??? Wie wird das genutzt / gelöst? Weiß man das hier überhaupt noch?*/

            blWillenserklaerungStatusVoll.fuelleAlles(true);
            int lPersonNatJur = 0;
            if (blTeilnehmerLogin.eclLoginDaten.kennungArt == KonstLoginKennungArt.aktienregister) {
                lPersonNatJur = blTeilnehmerLogin.eclAktienregister.personNatJur;
            } else {
                lPersonNatJur = blTeilnehmerLogin.eclPersonenNatJur.ident;
            }
            blWillenserklaerungStatusVoll.ergaenzeAllesUmPraesenzdaten(lPersonNatJur);
            /*Hat nichts mit Präsenzliste zu tun, sondern mit der Liste aller mit dieser Kennung bereits
             * präsenten / nicht-Präsenten Bestände
             */
            blWillenserklaerungStatusVoll.fuellePraesenzList();
            if (blWillenserklaerungStatusVoll.anzPraesenteVorhanden>0) {
                pMitteilung.praesent=1;
            } else {
                pMitteilung.praesent=0;
            }
        } else {
            lDbBundle.dbMeldungVirtuellePraesenz.read_zuLoginIdent(blTeilnehmerLogin.eclLoginDaten.ident);
            pMitteilung.praesent=0;
            int anz = lDbBundle.dbMeldungVirtuellePraesenz.anzErgebnis();
            for (int i = 0; i < anz; i++) {
                if (lDbBundle.dbMeldungVirtuellePraesenz.ergebnisPosition(i).statusPraesenz == 1) {
                    pMitteilung.praesent=1;
                }
            }
        }
        

    }
    
    /************************************Botschaften-Bearbeitung******************************************/
    
    /**Bereitet Botschaften-Liste mit Unterlagen auf.
     * Ergebnis in rcBotschaftenListe*/
    public int botschaftenListeAufbereiten() {
        
        /*Lese alle EclMitteilungen aus Botschaften ein, und zwar einschließlich Versionen
         * - Eingereichte Botschaften-Liste aufbauen.
         * - Dabei Mehrere Versionen berücksichtigen
         * */
        lDbBundle.dbMitteilung.setzeFunktion(KonstPortalFunktionen.botschaftenEinreichen);
        lDbBundle.dbMitteilung.readAll();
        
        List<EclMitteilung> lQuellListe=lDbBundle.dbMitteilung.ergebnis();
        rcBotschaftenListe=new LinkedList<EclMitteilung>();
        
        EclMitteilung letzteMitteilungVersion0=null;
        
        for (int i=0;i<lQuellListe.size();i++) {
            EclMitteilung lMitteilung=lQuellListe.get(i);
            if (lMitteilung.version==0) {
                rcBotschaftenListe.add(lMitteilung);
                letzteMitteilungVersion0=lMitteilung;
            }
            letzteMitteilungVersion0.weitereVersionen.add(lMitteilung);
            ergaenzeEinzelneMitteilungImmer(lMitteilung);
        }
        
        /*Ergänze Botschaften um zugeordnete Unterlagen - soweit vorhanden*/
        for (int i=0;i<rcBotschaftenListe.size();i++) {
            EclMitteilung lMitteilung=rcBotschaftenListe.get(i);
            if (lMitteilung.verweisAufUnterlagenident!=0) {
                lDbBundle.dbPortalUnterlagen.readId(lMitteilung.verweisAufUnterlagenident);
                lMitteilung.zugeordnetePortalUnterlage=lDbBundle.dbPortalUnterlagen.ergebnisPosition(0);
            }
        }
        
        /*Ggf. Felder für Statusanzeige ergänzen, soweit erforderlich*/

        return 1;
    }
    
    /**Wird vor dem Hochladen eines Videos zum Streamer aufgerufen.
     * Setzt den Status der Verarbeitung auf "wird gerade hochgeladen", und resettet den Status "öffentlicher Link freigegeben"
     */
    public int botschaftenZuStreamHochladenStart(EclMitteilung pBotschaftHeader ) {
        pBotschaftHeader.interneVerarbeitungLaufend = KonstMitteilungStatus.verarbeitungZuStreamLadenLaeuft;
        pBotschaftHeader.setzeFreigegebenNichtOeffentlich();
        lDbBundle.dbMitteilung.setzeFunktion(KonstPortalFunktionen.botschaftenEinreichen);
        lDbBundle.dbMitteilung.update(pBotschaftHeader);
    
        return 1;
    }
    
    /**
     * Setzt alle bisher hochgeladenen Streams in den Mitteilungs-Versionen zurück.
     * Setzt die übergebene Version auf "hochgeladen".
     * Setzt die Hochlade-Verarbeitung auf "Erledigt"
     * 
     * pMitteilungIdent: Ident der Mitteilung, zu der die Botschaft hochgeladen wurde
     * pVersion: Version der gespeicherten Videos, die hochgeladen wurde
     * objekt_id: Id, die vom Streamer geliefert wurde und eingetragen werden muß.*/
    public int botschaftenZuStreamHochgeladeneBotschaftEintragen(int pMitteilungIdent, int pVersion, String objekt_id) {
       /*Alle Mitteilungen mit ident einlesen, durcharbeiten*/
       lDbBundle.dbMitteilung.setzeFunktion(KonstPortalFunktionen.botschaftenEinreichen);
       lDbBundle.dbMitteilung.read(pMitteilungIdent);
       int zugeordneteUnterlage=0;
       for (int i=0;i<lDbBundle.dbMitteilung.anzErgebnis();i++) {
           EclMitteilung lMitteilung=lDbBundle.dbMitteilung.ergebnisPosition(i);
           boolean updateDurchfuehren=false;
           if (lMitteilung.version==pVersion) {
               /**Im Version-Satz pVersion "Hochgeladen" setzen*/
               lMitteilung.freigegeben=(lMitteilung.freigegeben | 4);
               updateDurchfuehren=true;
           }
           else {
               /*Ggf. andere Mitteilungen wieder auf "Nicht-Hochgeladen" setzen*/
               if (lMitteilung.liefereFreigegebenHochgeladen()) {
                   lMitteilung.setzeFreigegebenNichtHochgeladen();
                   updateDurchfuehren=true;
               }
           }
           if (lMitteilung.version==0) {
               /**Im Version-0-Satz Verarbeitung auf erledigt setzen*/
               lMitteilung.interneVerarbeitungLaufend=KonstMitteilungStatus.verarbeitungErledigt;
               updateDurchfuehren=true;
               zugeordneteUnterlage=lMitteilung.verweisAufUnterlagenident;
           }
           
           if (updateDurchfuehren) {
               lDbBundle.dbMitteilung.update(lMitteilung);
           }
       }
       
       
       
       /**Im Unterlagen-Satz objekt_id als Verweis eintragen*/
       lDbBundle.dbPortalUnterlagen.readId(zugeordneteUnterlage);
       EclPortalUnterlagen lPortalUnterlage=lDbBundle.dbPortalUnterlagen.ergebnisPosition(0);
       lPortalUnterlage.dateiname=objekt_id;
       lDbBundle.dbPortalUnterlagen.update(lPortalUnterlage);

       return 1;
   }
    
    
    public int botschaftenPDFBereitstellen(EclMitteilung pBotschaftHeader, EclMitteilung pBotschaftVersion) {
        if (pBotschaftVersion.internerDateizusatz.compareToIgnoreCase("pdf")!=0) {
            /*Nur PDF zulässig*/
            return -1;
        }
        
        String internerDateiname=pBotschaftVersion.internerDateiname;
        String version=CaString.fuelleLinksNull(Integer.toString(pBotschaftVersion.version), 2);
        internerDateiname=internerDateiname+"_"+version;
        
        botschaftenZuStreamHochgeladeneBotschaftEintragen(pBotschaftHeader.mitteilungIdent, pBotschaftVersion.version, internerDateiname);
        return 1;
    }
    
    /**pBotschaft = "Header-Mitteilung"  (mit Version==0)*/
    public int botschaftenUnterlageErstellen(EclMitteilung pBotschaft) {
        
        /*Unterlage vorinitialisieren und Speichern*/
        EclPortalUnterlagen neuePortalUnterlage = new EclPortalUnterlagen();
        if (pBotschaft.liefereVideodateiVorhanden()) {
            neuePortalUnterlage.art = KonstPortalUnterlagen.ART_VIDEO_BUTTON;
        }
        else {
            neuePortalUnterlage.art = KonstPortalUnterlagen.ART_UNTERLAGE_BUTTON;
        }
        neuePortalUnterlage.artStyle=KonstPortalUnterlagen.ARTSTYLE_BUTTON_ALS_BUTTON;
        neuePortalUnterlage.bezeichnungDE = "Automatisch generiert";
        lDbBundle.dbPortalUnterlagen.insert(neuePortalUnterlage);
        int identPortalUnterlage = neuePortalUnterlage.ident;

        /*Verweis auf Unterlage in Botschaft eintragen*/
        pBotschaft.verweisAufUnterlagenident = identPortalUnterlage;
        lDbBundle.dbMitteilung.setzeFunktion(KonstPortalFunktionen.botschaftenEinreichen);
        lDbBundle.dbMitteilung.update(pBotschaft);

        return 1;
    }
    
    public int botschaftenStreamAlsOeffentlichFreigeben(EclMitteilung pBotschaftHeader) {
        /*Eintragen, dass Link freigeschalten ist - erfolgt immer im Satz mit Version 0*/
        int botschaftIdent=pBotschaftHeader.mitteilungIdent;
        lDbBundle.dbMitteilung.setzeFunktion(KonstPortalFunktionen.botschaftenEinreichen);
        lDbBundle.dbMitteilung.read(botschaftIdent);
        EclMitteilung lMitteilung=lDbBundle.dbMitteilung.ergebnisPosition(0);
        lMitteilung.freigegeben=(lMitteilung.freigegeben | 8);
        lDbBundle.dbMitteilung.update(lMitteilung);

        return 1;
    }
     
    
    public int rcNeueVersion=0;
    public int botschaftenNeueVersionErstellen(EclMitteilung pBotschaftHeader, String pInternerFilenameZusatz, String pExternerFilename) {
        
        /*Nächste Version bestimmen*/
        lDbBundle.dbMitteilung.setzeFunktion(KonstPortalFunktionen.botschaftenEinreichen);
        rcNeueVersion=lDbBundle.dbMitteilung.holeMaxVersion(pBotschaftHeader.mitteilungIdent)+1;
        
        /*Nächste Version in Mitteilungen erstellen*/
        EclMitteilung neueMitteilungVersion=new EclMitteilung();
        neueMitteilungVersion.mitteilungIdent=pBotschaftHeader.mitteilungIdent;
        neueMitteilungVersion.internerDateiname=pBotschaftHeader.internerDateiname;
        neueMitteilungVersion.internerDateizusatz=pInternerFilenameZusatz;
        neueMitteilungVersion.dateiname=pExternerFilename;
        neueMitteilungVersion.version=rcNeueVersion;
        neueMitteilungVersion.loginIdent=pBotschaftHeader.loginIdent;
        lDbBundle.dbMitteilung.insertWeitereVersion(neueMitteilungVersion);

        
        return 1;
    }
    
    public int botschaftenFreigabeVersionDurchGesellschaft(EclMitteilung pBotschaftVersion) {
        lDbBundle.dbMitteilung.setzeFunktion(KonstPortalFunktionen.botschaftenEinreichen);
        int anz=lDbBundle.dbMitteilung.read(pBotschaftVersion.mitteilungIdent);
        for (int i=0;i<anz;i++) {
            EclMitteilung iBotschaftVersion=lDbBundle.dbMitteilung.ergebnisPosition(i);
            if (pBotschaftVersion.version==iBotschaftVersion.version) {
                iBotschaftVersion.freigegeben=(iBotschaftVersion.freigegeben | 2);
                lDbBundle.dbMitteilung.update(iBotschaftVersion);
            }
        }
        return 1;
    }
    
    public String rcBotschaftDateiPfadUndName="";
    public int botschaftErzeugeeDateiPfadUndName(EclMitteilung pBotschaftHead) {
        
        /*XXX*/
        return 1;
    }
}
