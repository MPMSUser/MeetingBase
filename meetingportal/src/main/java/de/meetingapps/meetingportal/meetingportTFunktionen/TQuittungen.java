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
package de.meetingapps.meetingportal.meetingportTFunktionen;

import java.util.List;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaDatumZeit;
import de.meetingapps.meetingportal.meetComAllg.CaString;
import de.meetingapps.meetingportal.meetComBa.BaMailM;
import de.meetingapps.meetingportal.meetComEclM.EclAbstimmungM;
import de.meetingapps.meetingportal.meetComEclM.EclAbstimmungenListeM;
import de.meetingapps.meetingportal.meetComEclM.EclDbM;
import de.meetingapps.meetingportal.meetComEclM.EclLoginDatenM;
import de.meetingapps.meetingportal.meetComEclM.EclParamM;
import de.meetingapps.meetingportal.meetComEclM.EclPortalTexteM;
import de.meetingapps.meetingportal.meetComEntities.EclAktienregister;
import de.meetingapps.meetingportal.meetComEntities.EclMeldung;
import de.meetingapps.meetingportal.meetComEntities.EclMitteilung;
import de.meetingapps.meetingportal.meetComEntities.EclPersonenNatJur;
import de.meetingapps.meetingportal.meetComEntities.EclZugeordneteMeldungNeu;
import de.meetingapps.meetingportal.meetComEntities.EclZutrittsIdent;
import de.meetingapps.meetingportal.meetComKonst.KonstMitteilungStatus;
import de.meetingapps.meetingportal.meetComKonst.KonstPortalAktion;
import de.meetingapps.meetingportal.meetComKonst.KonstPortalFunktionen;
import de.meetingapps.meetingportal.meetComKonst.KonstPortalTexte;
import de.meetingapps.meetingportal.meetingCoreReport.RpDrucken;
import de.meetingapps.meetingportal.meetingCoreReport.RpVariablen;
import de.meetingapps.meetingportal.meetingportTController.TLanguage;
import de.meetingapps.meetingportal.meetingportTController.TWillenserklaerungSession;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@RequestScoped
@Named
public class TQuittungen {

    private int logDrucken=3;
    
    private @Inject EclDbM eclDbM;
    private @Inject TLanguage tLanguage;

    private @Inject EclLoginDatenM eclLoginDatenM;

    
    private @Inject TWillenserklaerungSession tWillenserklaerungSession;
    private @Inject EclPortalTexteM eclPortalTexteM;
    private @Inject BaMailM baMailM;
    private @Inject EclParamM eclParamM;
    private @Inject EclAbstimmungenListeM eclAbstimmungenListeM;

    /********************************************Weisungsbestätigung*********************************************/
    public int weisungsBestaetigungAufAnforderung(String hMail) {
        
        String hMailText="", hBetreff="";
        if (tWillenserklaerungSession.getArt().contentEquals("1")) {
            /*Briefwahl*/
            hBetreff=eclPortalTexteM.holeIText(KonstPortalTexte.IWEISUNGQUITTUNG_BRIEFWAHL_BESTAETIGUNG_MAILBETREFF);
            hMailText=eclPortalTexteM.holeIText(KonstPortalTexte.IWEISUNGQUITTUNG_BRIEFWAHL_BESTAETIGUNG_MAILTEXT);
        }
        else{
            /*SRV*/
            hBetreff=eclPortalTexteM.holeIText(KonstPortalTexte.IWEISUNGQUITTUNG_SRV_BESTAETIGUNG_MAILBETREFF);
            hMailText=eclPortalTexteM.holeIText(KonstPortalTexte.IWEISUNGQUITTUNG_SRV_BESTAETIGUNG_MAILTEXT);
        }
        baMailM.sendenMitAnhang(hMail, 
                hBetreff,
                hMailText,
                eclParamM.getClGlobalVar().lwPfadAllgemein+"\\"+eclParamM.getParamServer().praefixPfadVerzeichnisse
                        + "ausdrucke\\"+eclParamM.getMandantPfad()+
                "\\bes"+tWillenserklaerungSession.getNummerBestaetigung()+".pdf");

        return 1;
    }
    
    public void weisungsBestaetigungErzeugePDF() {
        if (tWillenserklaerungSession.isPdfWurdeBereitsErzeugt()) {
            return;
        }
        
        if (eclParamM.getParam().paramPortal.bestaetigungBeiWeisungMitTOP==0) {
            weisungsBestaetigungErzeugePDF(false);
        }
        else {
            weisungsBestaetigungErzeugePDF(true); 
        }
        
        tWillenserklaerungSession.setPdfWurdeBereitsErzeugt(true);
    }
    
    private void weisungsBestaetigungErzeugePDF(boolean pMitTOP) {
        RpDrucken rpDrucken=new RpDrucken();
        rpDrucken.initServer();
        rpDrucken.exportFormat=8;
        rpDrucken.exportDatei="bes"+tWillenserklaerungSession.getNummerBestaetigung();
        if (pMitTOP) {
            rpDrucken.initListe(eclDbM.getDbBundle());
        }
        else {
            rpDrucken.initFormular(eclDbM.getDbBundle());
        }

        /*Variablen füllen - sowie Dokumentvorlage*/
        RpVariablen rpVariablen=new RpVariablen(eclDbM.getDbBundle());
        rpVariablen.weisungBestaetigung("01", rpDrucken, pMitTOP);
        
        fuelleAllgemeineVariablen(rpVariablen, rpDrucken);

        if (pMitTOP) {
            rpDrucken.startListe();
        }
        else {
            rpDrucken.startFormular();
        }

        //Start printing
        if (pMitTOP) {
            List<EclAbstimmungM> lAbstimmungenListe = eclAbstimmungenListeM.getAbstimmungenListeM();
            for (int i = 0; i < lAbstimmungenListe.size(); i++) {
                trageInListeEin(lAbstimmungenListe.get(i), rpDrucken, rpVariablen);
            }

            List<EclAbstimmungM> lGegenantraegeListe = eclAbstimmungenListeM.getGegenantraegeListeM();
            for (int i = 0; i < lGegenantraegeListe.size(); i++) {
                trageInListeEin(lGegenantraegeListe.get(i), rpDrucken, rpVariablen);
            }
            rpDrucken.endeListe();
        }
        else {
            rpDrucken.druckenFormular();
            rpDrucken.endeFormular();
        }
    }

    private void trageInListeEin(EclAbstimmungM eclAbstimmungM, RpDrucken rpDrucken, RpVariablen rpVariablen) {
        if (eclAbstimmungM.isUeberschrift()) {
            /*0=normaler TOP, 1=Überschrift*/
            rpVariablen.fuelleFeld(rpDrucken, "TOP.istUeberschrift", "1");
            rpVariablen.fuelleFeld(rpDrucken, "TOP.stimmart", "");
        }
        else {
            rpVariablen.fuelleFeld(rpDrucken, "TOP.istUeberschrift", "0");
            if (eclAbstimmungM.getGewaehlt()==null) {
                rpVariablen.fuelleFeld(rpDrucken, "TOP.stimmart", " ");
            }
            else {
                switch (eclAbstimmungM.getGewaehlt()) {
                case "J":
                case "N":
                case "E":            
                    rpVariablen.fuelleFeld(rpDrucken, "TOP.stimmart", eclAbstimmungM.getGewaehlt());
                    break;
                default:
                    rpVariablen.fuelleFeld(rpDrucken, "TOP.stimmart", " ");
                    break;
                }
            }

        }


        rpVariablen.fuelleFeld(rpDrucken, "TOP.nummer", eclAbstimmungM.getNummer());
        rpVariablen.fuelleFeld(rpDrucken, "TOP.nummerindex", eclAbstimmungM.getNummerindex());
        rpVariablen.fuelleFeld(rpDrucken, "TOP.nummerEN", eclAbstimmungM.getNummerEN());
        rpVariablen.fuelleFeld(rpDrucken, "TOP.nummerindexEN", eclAbstimmungM.getNummerindexEN());

        rpVariablen.fuelleFeld(rpDrucken, "TOP.bezeichnung", eclAbstimmungM.getAnzeigeBezeichnungLang());
        rpVariablen.fuelleFeld(rpDrucken, "TOP.bezeichnungEN", eclAbstimmungM.getAnzeigeBezeichnungLangEN());
        
        rpVariablen.fuelleFeld(rpDrucken, "TOP.bezeichnungKurz", eclAbstimmungM.getAnzeigeBezeichnungKurz());
        rpVariablen.fuelleFeld(rpDrucken, "TOP.bezeichnungKurzEN", eclAbstimmungM.getAnzeigeBezeichnungKurzEN());

        rpDrucken.druckenListe();


    }

    
    /*************************************Bestätigungs-Funktionen*************************************************
     * Aufzurufen beim Erzeugen der jeweiligen Willenserklärungen oder Portalfunktionen.
     * Ggf. wird E-Mail-Bestätigung verschickt, ansonsten Vorbereitung für anzuzeigende Bestätigungen
     */
    
    
    
    public void bestaetigenNurAnmeldung(int pWillenserklaerungIdent, EclMeldung pEclMeldung, EclAktienregister pEclAktienregister) {
        tWillenserklaerungSession.clearBestaetigungsfelder();

        boolean durchfuehren=pruefeDurchfuehren();
        if (durchfuehren==false) {
            return;
        }
        
        belegeIsinAusMeldungUndAktienregister(pEclMeldung, pEclAktienregister);
        
        belegeBasis(pWillenserklaerungIdent);
        
        tWillenserklaerungSession.setAnmeldungDurchgefuehrt(true);
        tWillenserklaerungSession.setEkNummer("");
        
        belegeAktionaersdatenAusMeldung(pEclMeldung);
        
        if (tWillenserklaerungSession.isBestaetigenPerMailDurchfuehren()) {
            belegeTextVariablen(null);
            
            versendeMail(KonstPortalTexte.MAILBESTAETIGUNG_BETREFF_NUR_ANMELDUNG, KonstPortalTexte.MAILBESTAETIGUNG_INHALT_NUR_ANMELDUNG);
        }
        
    }

    /**Vollmachten leer => keine Vollmacht
     * pEclAktienregister kann null sein
     * pVollmachtPersonNatJurIdent!=0 => pVollmachtVorname/Name/Ort werden aus DB eingelesen
     * pStornieren=true => Es handelt sich um eine Eintrittskarten-Stornierung (ggf. mit Vollmacht)*/
    public void bestaetigenEintrittskarte(int pWillenserklaerungIdent, EclMeldung pEclMeldung, EclAktienregister pEclAktienregister,
            EclZutrittsIdent pZutrittsIdentAktionaer,
            int pVollmachtPersonNatJurIdent, String pVollmachtVorname, String pVollmachtName, String pVollmachtOrt,
            boolean pStornieren
            ) {
        tWillenserklaerungSession.clearBestaetigungsfelder();

        boolean durchfuehren=pruefeDurchfuehren();
        if (durchfuehren==false) {
            return;
        }
        
        belegeIsinAusMeldungUndAktienregister(pEclMeldung, pEclAktienregister);
        
        belegeBasis(pWillenserklaerungIdent);
        
        tWillenserklaerungSession.setAnmeldungDurchgefuehrt(true); /*Ist hier möglicherweise falsch ...*/
        tWillenserklaerungSession.setEkNummer(pZutrittsIdentAktionaer.zutrittsIdent);

        if (pVollmachtPersonNatJurIdent!=0) {
            int rc=eclDbM.getDbBundle().dbPersonenNatJur.read(pVollmachtPersonNatJurIdent);
            if (rc>0) {
                EclPersonenNatJur lPersonNatJur= eclDbM.getDbBundle().dbPersonenNatJur.PersonNatJurGefunden(0);
                pVollmachtVorname=lPersonNatJur.vorname;
                pVollmachtName=lPersonNatJur.name;
                pVollmachtOrt=lPersonNatJur.ort;
            }
        }
        tWillenserklaerungSession.setBevollmaechtigterVorname(pVollmachtVorname);
        tWillenserklaerungSession.setBevollmaechtigterName(pVollmachtName);
        tWillenserklaerungSession.setBevollmaechtigterOrt(pVollmachtOrt);

        belegeAktionaersdatenAusMeldung(pEclMeldung);
        
        if (tWillenserklaerungSession.isBestaetigenPerMailDurchfuehren()) {
            belegeTextVariablen(null);

            if (pStornieren==false) {
                if (pVollmachtName.isEmpty()) {
                    versendeMail(KonstPortalTexte.MAILBESTAETIGUNG_BETREFF_EK, KonstPortalTexte.MAILBESTAETIGUNG_INHALT_EK);
                }
                else {
                    versendeMail(KonstPortalTexte.MAILBESTAETIGUNG_BETREFF_EK_UND_VOLLMACHT, KonstPortalTexte.MAILBESTAETIGUNG_INHALT_EK_UND_VOLLMACHT);
                }
            }
            else {
                if (pVollmachtName.isEmpty()) {
                    versendeMail(KonstPortalTexte.MAILBESTAETIGUNG_BETREFF_EK_STORNO, KonstPortalTexte.MAILBESTAETIGUNG_INHALT_EK_STORNO);
                }
                else {
                    versendeMail(KonstPortalTexte.MAILBESTAETIGUNG_BETREFF_EK_UND_VOLLMACHT_STORNO, KonstPortalTexte.MAILBESTAETIGUNG_INHALT_EK_UND_VOLLMACHT_STORNO);
                }
            }
        }
    }
    
    
    /**
     * pVollmachtPersonNatJurIdent!=0 => pVollmachtVorname/Name/Ort werden aus DB eingelesen
     * pStornieren=true => Es handelt sich um eine Eintrittskarten-Stornierung (ggf. mit Vollmacht)*/
    public void bestaetigenVollmachtDritte(int pWillenserklaerungIdent, EclMeldung pEclMeldung, 
            int pVollmachtPersonNatJurIdent, String pVollmachtVorname, String pVollmachtName, String pVollmachtOrt,
            boolean pStornieren
            ) {
        tWillenserklaerungSession.clearBestaetigungsfelder();

        boolean durchfuehren=pruefeDurchfuehren();
        if (durchfuehren==false) {
            return;
        }
        
        belegeIsinAusMeldung(pEclMeldung);
        
        belegeBasis(pWillenserklaerungIdent);
        
        tWillenserklaerungSession.setAnmeldungDurchgefuehrt(true); /*Ist hier möglicherweise falsch ...*/

        if (pVollmachtPersonNatJurIdent!=0) {
            int rc=eclDbM.getDbBundle().dbPersonenNatJur.read(pVollmachtPersonNatJurIdent);
            if (rc>0) {
                EclPersonenNatJur lPersonNatJur= eclDbM.getDbBundle().dbPersonenNatJur.PersonNatJurGefunden(0);
                pVollmachtVorname=lPersonNatJur.vorname;
                pVollmachtName=lPersonNatJur.name;
                pVollmachtOrt=lPersonNatJur.ort;
            }
        }
        tWillenserklaerungSession.setBevollmaechtigterVorname(pVollmachtVorname);
        tWillenserklaerungSession.setBevollmaechtigterName(pVollmachtName);
        tWillenserklaerungSession.setBevollmaechtigterOrt(pVollmachtOrt);

        belegeAktionaersdatenAusMeldung(pEclMeldung);
        
        if (tWillenserklaerungSession.isBestaetigenPerMailDurchfuehren()) {
            belegeTextVariablen(null);

            if (pStornieren==false) {
                versendeMail(KonstPortalTexte.MAILBESTAETIGUNG_BETREFF_VOLLMACHT_DRITTE, KonstPortalTexte.MAILBESTAETIGUNG_INHALT_VOLLMACHT_DRITTE);
            }
            else {
                versendeMail(KonstPortalTexte.MAILBESTAETIGUNG_BETREFF_VOLLMACHT_DRITTE_STORNO, KonstPortalTexte.MAILBESTAETIGUNG_INHALT_VOLLMACHT_DRITTE_STORNO);
            }
        }
    }

    
    /**pEclAktienregister kann null sein!*/
    public void bestaetigenSrvBriefwahl(int pWillenserklaerungIdent, boolean pMitErstanmeldung, EclMeldung pEclMeldung, EclAktienregister pEclAktienregister) {
        tWillenserklaerungSession.clearBestaetigungsfelder();

        vorbereitenPruefeDurchfuehrenSrvBriefwahl();
        boolean durchfuehren=pruefeDurchfuehren();
        if (durchfuehren==false) {
            return;
        }
        
        belegeIsinAusMeldungUndAktienregister(pEclMeldung, pEclAktienregister);
        
        belegeBasis(pWillenserklaerungIdent);
        
        tWillenserklaerungSession.setAnmeldungDurchgefuehrt(pMitErstanmeldung);
        tWillenserklaerungSession.setEkNummer("");
        
        belegeAktionaersdatenAusMeldung(pEclMeldung);
        
        belegeWeisungsliste();
        
        if (tWillenserklaerungSession.isBestaetigenPerMailDurchfuehren()) {
            belegeTextVariablen(null);

            if (eclParamM.getParam().paramPortal.bestaetigungPerEmailUeberallZulassen==2) {

                weisungsBestaetigungErzeugePDF();

                if (tWillenserklaerungSession.getArt().equals("1")) {
                    /*Neu Briefwahl*/
                    versendeMailMitAnhang(KonstPortalTexte.MAILBESTAETIGUNG_BETREFF_BRIEFWAHL_NEU, KonstPortalTexte.MAILBESTAETIGUNG_INHALT_BRIEFWAHL_NEU);
                }
                else {
                    /*Neu SRV*/
                    versendeMailMitAnhang(KonstPortalTexte.MAILBESTAETIGUNG_BETREFF_SRV_NEU, KonstPortalTexte.MAILBESTAETIGUNG_INHALT_SRV_NEU);
                }
            }
            else {
                if (tWillenserklaerungSession.getArt().equals("1")) {
                    /*Neu Briefwahl*/
                    versendeMail(KonstPortalTexte.MAILBESTAETIGUNG_BETREFF_BRIEFWAHL_NEU, KonstPortalTexte.MAILBESTAETIGUNG_INHALT_BRIEFWAHL_NEU);
                }
                else {
                    /*Neu SRV*/
                    versendeMail(KonstPortalTexte.MAILBESTAETIGUNG_BETREFF_SRV_NEU, KonstPortalTexte.MAILBESTAETIGUNG_INHALT_SRV_NEU);
                }

            }
        }

    }
    

    public void bestaetigenSrvBriefwahlAendern(int identWillenserklaerung, EclZugeordneteMeldungNeu pEclZugeordneteMeldung) {
        tWillenserklaerungSession.clearBestaetigungsfelder();

        vorbereitenPruefeDurchfuehrenSrvBriefwahl();
        boolean durchfuehren=pruefeDurchfuehren();
        if (durchfuehren==false) {
            return;
        }
        
        belegeIsinAusMeldung(pEclZugeordneteMeldung.eclMeldung);
        
        belegeBasis(identWillenserklaerung);
        
        tWillenserklaerungSession.setAnmeldungDurchgefuehrt(false);
        tWillenserklaerungSession.setEkNummer("");
        
        belegeAktionaersdatenAusMeldung(pEclZugeordneteMeldung.eclMeldung);
        
        belegeWeisungsliste();
        
        if (tWillenserklaerungSession.isBestaetigenPerMailDurchfuehren()) {
            belegeTextVariablen(null);

            if (eclParamM.getParam().paramPortal.bestaetigungPerEmailUeberallZulassen==2) {
                weisungsBestaetigungErzeugePDF();

                if (tWillenserklaerungSession.getArt().equals("1")) {
                    /*Ändern Briefwahl*/
                    versendeMailMitAnhang(KonstPortalTexte.MAILBESTAETIGUNG_BETREFF_BRIEFWAHL_AENDERN, KonstPortalTexte.MAILBESTAETIGUNG_INHALT_BRIEFWAHL_AENDERN);
                }
                else {
                    /*Ändern SRV*/
                    versendeMailMitAnhang(KonstPortalTexte.MAILBESTAETIGUNG_BETREFF_SRV_AENDERN, KonstPortalTexte.MAILBESTAETIGUNG_INHALT_SRV_AENDERN);
                }
            }
            else {
                if (tWillenserklaerungSession.getArt().equals("1")) {
                    /*Ändern Briefwahl*/
                    versendeMail(KonstPortalTexte.MAILBESTAETIGUNG_BETREFF_BRIEFWAHL_AENDERN, KonstPortalTexte.MAILBESTAETIGUNG_INHALT_BRIEFWAHL_AENDERN);
                }
                else {
                    /*Ändern SRV*/
                    versendeMail(KonstPortalTexte.MAILBESTAETIGUNG_BETREFF_SRV_AENDERN, KonstPortalTexte.MAILBESTAETIGUNG_INHALT_SRV_AENDERN);
                }
           }
        }
    }

    /**
     * pVollmachtPersonNatJurIdent!=0 => pVollmachtVorname/Name/Ort werden aus DB eingelesen
     * pStornieren=true => Es handelt sich um eine Eintrittskarten-Stornierung (ggf. mit Vollmacht)*/
    public void bestaetigenSrvBriefwahlStornieren(int pWillenserklaerungIdent, EclMeldung pEclMeldung 
           ) {
        tWillenserklaerungSession.clearBestaetigungsfelder();

        vorbereitenPruefeDurchfuehrenSrvBriefwahl();
        boolean durchfuehren=pruefeDurchfuehren();
        if (durchfuehren==false) {
            return;
        }
        
        belegeIsinAusMeldung(pEclMeldung);
        
        belegeBasis(pWillenserklaerungIdent);
        
        tWillenserklaerungSession.setAnmeldungDurchgefuehrt(false); 

        belegeAktionaersdatenAusMeldung(pEclMeldung);
 
        if (tWillenserklaerungSession.isBestaetigenPerMailDurchfuehren()) {
            belegeTextVariablen(null);
            if (tWillenserklaerungSession.getArt().equals("1")) {
                /*Briefwahl*/
                versendeMail(KonstPortalTexte.MAILBESTAETIGUNG_BETREFF_BRIEFWAHL_STORNO, KonstPortalTexte.MAILBESTAETIGUNG_INHALT_BRIEFWAHL_STORNO);
            }
            if (tWillenserklaerungSession.getArt().equals("2")) {
                /*SRV*/
                versendeMail(KonstPortalTexte.MAILBESTAETIGUNG_BETREFF_SRV_STORNO, KonstPortalTexte.MAILBESTAETIGUNG_INHALT_SRV_STORNO);
            }
            if (tWillenserklaerungSession.getArt().equals("3")) {
                /*KIAV*/
                versendeMail(KonstPortalTexte.MAILBESTAETIGUNG_BETREFF_KIAV_STORNO, KonstPortalTexte.MAILBESTAETIGUNG_INHALT_KIAV_STORNO);
            }
            if (tWillenserklaerungSession.getArt().equals("4")) {
                /*Dauervollmacht*/
                versendeMail(KonstPortalTexte.MAILBESTAETIGUNG_BETREFF_DAUER_STORNO, KonstPortalTexte.MAILBESTAETIGUNG_INHALT_DAUER_STORNO);
            }
            if (tWillenserklaerungSession.getArt().equals("5")) {
                /*Organisatorisch*/
                versendeMail(KonstPortalTexte.MAILBESTAETIGUNG_BETREFF_ORGA_STORNO, KonstPortalTexte.MAILBESTAETIGUNG_INHALT_ORGA_STORNO);
            }
        }

    }

    
    /**
     * pVollmachtPersonNatJurIdent!=0 => pVollmachtVorname/Name/Ort werden aus DB eingelesen
     * pStornieren=true => Es handelt sich um eine Eintrittskarten-Stornierung (ggf. mit Vollmacht)*/
    public void bestaetigungMitteilung(EclMitteilung pMitteilung) {
        tWillenserklaerungSession.clearBestaetigungsfelder();

        boolean durchfuehren=pruefeDurchfuehren();
        if (durchfuehren==false) {
            return;
        }
        
        belegeBasis(0);
        
        tWillenserklaerungSession.setAnmeldungDurchgefuehrt(false); /*Ist hier möglicherweise falsch ...*/

        
        if (tWillenserklaerungSession.isBestaetigenPerMailDurchfuehren()) {
            belegeTextVariablen(pMitteilung);

            if (pMitteilung.status==KonstMitteilungStatus.ZURUECKGEZOGEN) {
                switch (pMitteilung.artDerMitteilung) {
                case KonstPortalFunktionen.fragen:
                    versendeMail(KonstPortalTexte.MAILBESTAETIGUNG_BETREFF_FRAGEN, KonstPortalTexte.MAILBESTAETIGUNG_INHALT_FRAGEN);
                    break;
                case KonstPortalFunktionen.wortmeldungen:
                    versendeMail(KonstPortalTexte.MAILBESTAETIGUNG_BETREFF_WORTMELDUNGEN, KonstPortalTexte.MAILBESTAETIGUNG_INHALT_WORTMELDUNGEN);
                    break;
                case KonstPortalFunktionen.widersprueche:
                    versendeMail(KonstPortalTexte.MAILBESTAETIGUNG_BETREFF_WIDERSPRUECHE, KonstPortalTexte.MAILBESTAETIGUNG_INHALT_WIDERSPRUECHE);
                    break;
                case KonstPortalFunktionen.antraege:
                    versendeMail(KonstPortalTexte.MAILBESTAETIGUNG_BETREFF_ANTRAEGE, KonstPortalTexte.MAILBESTAETIGUNG_INHALT_ANTRAEGE);
                    break;
                case KonstPortalFunktionen.sonstigeMitteilungen:
                    versendeMail(KonstPortalTexte.MAILBESTAETIGUNG_BETREFF_SONSTIGE_MITTEILUNGEN, KonstPortalTexte.MAILBESTAETIGUNG_INHALT_SONSTIGE_MITTEILUNGEN);
                    break;
                case KonstPortalFunktionen.botschaftenEinreichen:
                    versendeMail(KonstPortalTexte.MAILBESTAETIGUNG_BETREFF_BOTSCHAFTEN_EINREICHEN, KonstPortalTexte.MAILBESTAETIGUNG_INHALT_BOTSCHAFTEN_EINREICHEN);
                    break;
                case KonstPortalFunktionen.rueckfragen:
                    versendeMail(KonstPortalTexte.MAILBESTAETIGUNG_BETREFF_RUECKFRAGEN, KonstPortalTexte.MAILBESTAETIGUNG_INHALT_RUECKFRAGEN);
                    break;
                }
            }
            else {
                switch (pMitteilung.artDerMitteilung) {
                case KonstPortalFunktionen.fragen:
                    versendeMail(KonstPortalTexte.MAILBESTAETIGUNG_BETREFF_FRAGEN_STORNO, KonstPortalTexte.MAILBESTAETIGUNG_INHALT_FRAGEN_STORNO);
                    break;
                case KonstPortalFunktionen.wortmeldungen:
                    versendeMail(KonstPortalTexte.MAILBESTAETIGUNG_BETREFF_WORTMELDUNGEN_STORNO, KonstPortalTexte.MAILBESTAETIGUNG_INHALT_WORTMELDUNGEN_STORNO);
                    break;
                case KonstPortalFunktionen.widersprueche:
                    versendeMail(KonstPortalTexte.MAILBESTAETIGUNG_BETREFF_WIDERSPRUECHE_STORNO, KonstPortalTexte.MAILBESTAETIGUNG_INHALT_WIDERSPRUECHE_STORNO);
                    break;
                case KonstPortalFunktionen.antraege:
                    versendeMail(KonstPortalTexte.MAILBESTAETIGUNG_BETREFF_ANTRAEGE_STORNO, KonstPortalTexte.MAILBESTAETIGUNG_INHALT_ANTRAEGE_STORNO);
                    break;
                case KonstPortalFunktionen.sonstigeMitteilungen:
                    versendeMail(KonstPortalTexte.MAILBESTAETIGUNG_BETREFF_SONSTIGE_MITTEILUNGEN_STORNO, KonstPortalTexte.MAILBESTAETIGUNG_INHALT_SONSTIGE_MITTEILUNGEN_STORNO);
                    break;
                case KonstPortalFunktionen.botschaftenEinreichen:
                    versendeMail(KonstPortalTexte.MAILBESTAETIGUNG_BETREFF_BOTSCHAFTEN_EINREICHEN_STORNO, KonstPortalTexte.MAILBESTAETIGUNG_INHALT_BOTSCHAFTEN_EINREICHEN_STORNO);
                case KonstPortalFunktionen.rueckfragen:
                    versendeMail(KonstPortalTexte.MAILBESTAETIGUNG_BETREFF_RUECKFRAGEN_STORNO, KonstPortalTexte.MAILBESTAETIGUNG_INHALT_RUECKFRAGEN_STORNO);
                    break;
                }
              }
        }
    }

    
    
    private void vorbereitenPruefeDurchfuehrenSrvBriefwahl() {
        switch (tWillenserklaerungSession.getIntAusgewaehlteAktion()) {
        case KonstPortalAktion.SRV_NEU:
        case KonstPortalAktion.SRV_AENDERN:
            tWillenserklaerungSession.setArt("2");
            tWillenserklaerungSession.setArtUmfassend("2");
            if ((eclParamM.getParam().paramPortal.bestaetigungBeiWeisung & 2) ==2) {
                tWillenserklaerungSession.setBestaetigungErmoeglichen(true);
            }
            break;
        case KonstPortalAktion.BRIEFWAHL_NEU:
        case KonstPortalAktion.BRIEFWAHL_AENDERN:
            tWillenserklaerungSession.setArt("1");
            tWillenserklaerungSession.setArtUmfassend("1");
            if ((eclParamM.getParam().paramPortal.bestaetigungBeiWeisung & 1) ==1) {
                tWillenserklaerungSession.setBestaetigungErmoeglichen(true);
            }
            break;
        case KonstPortalAktion.SRV_STORNIEREN:
            tWillenserklaerungSession.setArt("2");
            break;
        case KonstPortalAktion.BRIEFWAHL_STORNIEREN:
            tWillenserklaerungSession.setArt("1");
            break;
        case KonstPortalAktion.KIAV_STORNIEREN:
            tWillenserklaerungSession.setArt("3");
            break;
        case KonstPortalAktion.DAUERVOLLMACHT_STORNIEREN:
            tWillenserklaerungSession.setArt("4");
            break;
        case KonstPortalAktion.ORGANISATORISCH_STORNIEREN:
            tWillenserklaerungSession.setArt("5");
            break;
        }
        
    }
    
    /**Belegt:
     * art
     * artUmfassend
     * bestaetigungErmoeglichen
     * bestaetigungPerMailDurchfuehren
     * emailFuerBestaetigung (aus EclLoginDaten)
     * 
     * False, wenn weder Bestätigung angzeigt noch per Mail verschickt werden soll (oder kann)
     */
    private boolean pruefeDurchfuehren() {
        
        /*E-Mail-Adresse vorbelegen*/
        tWillenserklaerungSession.setEmailFuerBestaetigung("");
        String hEmail=eclLoginDatenM.getEclLoginDaten().eMailFuerVersand;

        if (!hEmail.isEmpty()) {
            tWillenserklaerungSession.setEmailFuerBestaetigung(hEmail);
        }

 
        if (eclParamM.getParam().paramPortal.bestaetigungPerEmailUeberallZulassen!=0 && hEmail.isEmpty()==false && eclLoginDatenM.getEclLoginDaten().liefereQuittungPerEmailBeiAllenWillenserklaerungen()) {
            tWillenserklaerungSession.setBestaetigenPerMailDurchfuehren(true);
        }

        if (tWillenserklaerungSession.isBestaetigungErmoeglichen()==false &&
                tWillenserklaerungSession.isBestaetigenPerMailDurchfuehren()==false
                ){
            return false;
        }

        return true;
    }
    
    
    /**Belegt
     * isin
     * 
     * Liest Aktienregister ein, und holt aus Aktienregister ggf. vorrangig die dort eingetragen ISIN-Nummer 
      */
    private void belegeIsinAusMeldung(EclMeldung pMeldung) {
        String hIsin="";
        int pAktienregisterIdent=pMeldung.aktienregisterIdent;
        /*Isin ggf. aus Aktienregister einlesen*/
        if (pAktienregisterIdent!=0) {
            eclDbM.getDbBundle().dbAktienregister.leseZuAktienregisterIdent(pAktienregisterIdent);
            hIsin=eclDbM.getDbBundle().dbAktienregister.ergebnisPosition(0).isin.trim();
        }
        CaBug.druckeLog("pAktienregisterIdent="+pAktienregisterIdent+" hIsin="+hIsin,  logDrucken, 10);
        
        if (hIsin.isEmpty()) {
            tWillenserklaerungSession.setIsin(eclParamM.getParam().paramBasis.getIsin(pMeldung.getGattung()));
        }
        else {
            tWillenserklaerungSession.setIsin(hIsin);
        }
    }

    /**Belegt
     * isin
     * 
     * Lies aus Aktienregister ggf. vorrangig die dort eingetragen ISIN-Nummer 
      */
    private void belegeIsinAusMeldungUndAktienregister(EclMeldung pMeldung, EclAktienregister pAktienregister) {
        String hIsin="";
        /*Isin ggf. aus Aktienregister einlesen*/
        if (pAktienregister!=null) {
            hIsin=pAktienregister.isin.trim();
        }
        CaBug.druckeLog("pAktienregisterIdent hIsin="+hIsin,  logDrucken, 10);
        
        if (hIsin.isEmpty()) {
            tWillenserklaerungSession.setIsin(eclParamM.getParam().paramBasis.getIsin(pMeldung.getGattung()));
        }
        else {
            tWillenserklaerungSession.setIsin(hIsin);
        }
    }

    /**Belegt:
     * aktionaersnummer
     * aktionaersname
     */
    private void belegeAktionaersdatenAusMeldung(EclMeldung pMeldung) {
        tWillenserklaerungSession.setAktionaersnummer(pMeldung.aktionaersnummer);
        tWillenserklaerungSession.setAktionaersname(pMeldung.liefereVornameName());
    }
    
    private void belegeWeisungsliste() {
        String topListe="";
        
        List<EclAbstimmungM> lAbstimmungenListe = eclAbstimmungenListeM.getAbstimmungenListeM();
        if (lAbstimmungenListe!=null) {
            for (int i = 0; i < lAbstimmungenListe.size(); i++) {
                topListe=trageInTOPListeEin(lAbstimmungenListe.get(i), topListe);
            }
        }

        List<EclAbstimmungM> lGegenantraegeListe = eclAbstimmungenListeM.getGegenantraegeListeM();
        if (lGegenantraegeListe!=null) {
            for (int i = 0; i < lGegenantraegeListe.size(); i++) {
                topListe=trageInTOPListeEin(lGegenantraegeListe.get(i), topListe);
            }
        }
        
        tWillenserklaerungSession.setTopListe(topListe);
        
    }
    
 
    private String trageInTOPListeEin(EclAbstimmungM eclAbstimmungM, String topListe) {
        if (!topListe.isEmpty()) {
            topListe+="\n\r";
        }
        if (tLanguage.getLang()==1) {
            topListe+=eclAbstimmungM.getNummer()+eclAbstimmungM.getNummerindex()+" ";
        }
        else {
            topListe+=eclAbstimmungM.getNummerEN()+eclAbstimmungM.getNummerindexEN()+" ";
        }
        
        if (eclAbstimmungM.isUeberschrift()==false) {
            String hStimmart="";
            switch (eclAbstimmungM.getGewaehlt()) {
            case "J":
                hStimmart=eclPortalTexteM.holeText("104");
                break;
            case "N":
                hStimmart=eclPortalTexteM.holeText("105");
                break;
            case "E":            
                hStimmart=eclPortalTexteM.holeText("106");
                break;
            case " ":            
                hStimmart=eclPortalTexteM.holeText("2294");
                break;
            default:
                break;
            }
            topListe+=hStimmart+" ";
        }

        if (tLanguage.getLang()==1) {
            topListe+=eclAbstimmungM.getAnzeigeBezeichnungLang();
        }
        else {
            topListe+=eclAbstimmungM.getAnzeigeBezeichnungLangEN();
        }
        return topListe;
    }

//    public void vvorbereitenBestaetigenAllgemein(int identWillenserklaerung, EclZugeordneteMeldungNeu pEclZugeordneteMeldung) {
//        int gattung=pEclZugeordneteMeldung.getGattung();
//        int aktienregisterIdent=pEclZugeordneteMeldung.aktienregisterIdent;
//        vorbereitenBestaetigenAllgemein(identWillenserklaerung, gattung, aktienregisterIdent);
//    }
    
    /**Belegt:
     * kennungVeranstaltung
     * datum
     * nameEmittent
     * nameBestaetigender
     * nameAbstimmender (aus EclLoginDaten)
     * */
    public void belegeBasis(int identWillenserklaerung) {

        tWillenserklaerungSession.setAusstellungsZeit(CaDatumZeit.DatumZeitStringFuerAnzeigeNeu());

        /**Druck-Nummer PDF ermitteln*/
        int pdfNr=identWillenserklaerung;
        tWillenserklaerungSession.setLfdNrPDF(CaString.fuelleLinksNull(Integer.toString(pdfNr), 6));

        /*Nummer der Bestätigung*/
        String hString="";
        /*Letzte beiden Ziffern vom HV-Jahr*/
        hString=hString+CaString.fuelleLinksNull(Integer.toString(eclParamM.getParam().hvJahr),4).substring(2);
        /*Mandant*/
        hString=hString+eclParamM.getMandantString();
        /*LfdHV-Nummer*/
        hString=hString+eclParamM.getParam().hvNummer;
        /*LfdNrPdF*/
        hString=hString+tWillenserklaerungSession.getLfdNrPDF();
        tWillenserklaerungSession.setNummerBestaetigung(hString);

        hString=eclParamM.getParam().paramBasis.eindeutigeHVKennung;
        if (hString.isEmpty()) {
            hString=eclParamM.getMandantPfad();
        }
        tWillenserklaerungSession.setKennungVeranstaltung(hString);

        /*Datum der HV*/
         String hvDatum=eclParamM.getEclEmittent().hvDatum;
        if (hvDatum.length()==10) {
            hvDatum=hvDatum.substring(6)+hvDatum.substring(3, 5)+hvDatum.substring(0,2);
        }
        tWillenserklaerungSession.setDatum(hvDatum);
        
        tWillenserklaerungSession.setNameEmittent(CaString.trunc(eclParamM.getEmittentName(),140));
        tWillenserklaerungSession.setNameBestaetigender(CaString.trunc(eclParamM.getEmittentName(),140));

        /*Name Teilnehmer ("Abstimmender")*/
        hString=eclLoginDatenM.getVorname();
        if (hString.length()>0) {
            hString=hString+" ";
        }
        hString=hString+eclLoginDatenM.getName();
        tWillenserklaerungSession.setNameAbstimmender(CaString.trunc(hString, 140));
    }


    /**pMitteilung==null => keine Mitteilung, sondern normale Willenserklärung*/
    private void belegeTextVariablen(EclMitteilung pMitteilung) {
//      @formatter:off
        
        String lokaleVariablenNamen[]= {
                "L_AusstellungsZeit", //Nicht bei Mitteilungen
                "L_NummerBestaetigung", //Nicht bei Mitteilungen
                "L_KennungVeranstaltung",
                "L_ISIN", //Nicht bei Mitteilungen
                "L_Datum",
                "L_NameEmittent",
                "L_NameBestaetigender",
                "L_NameAbstimmender", //Entspricht der Login-Kennung
                "L_Aktionaersnummer", //Nicht bei Mitteilungen
                "L_Aktionaersname", //Nicht bei Mitteilungen
                "L_EkNummer", //Nur bei Neuer EK und Storno EK
                "L_BevollmaechtigterVorname", //Nur bei Vollmacht, Dritte oder mit EK
                "L_BevollmaechtigterName",
                "L_BevollmaechtigterOrt",
                "L_TopListe", //Nur bei neuer oder geändeterter Weisung
                /*Ab hier nur für Mitteilungen*/
                "L_M_Kontaktdaten",
                "L_M_KontaktdatenTelefon",
                "L_M_Inhaltshinweise",
                "L_M_Kurztext",
                "L_M_Langtext"
                };
        
        String lokaleVariablenInhalt[]= new String[lokaleVariablenNamen.length];
        for (int i=0;i<lokaleVariablenNamen.length;i++) {
            lokaleVariablenInhalt[i]="";
        }
        if (pMitteilung==null) {
            lokaleVariablenInhalt[0]=tWillenserklaerungSession.getAusstellungsZeit();
            lokaleVariablenInhalt[1]=tWillenserklaerungSession.getNummerBestaetigung();
        }
        lokaleVariablenInhalt[2]=tWillenserklaerungSession.getKennungVeranstaltung();
        if (pMitteilung==null) {
            lokaleVariablenInhalt[3]=tWillenserklaerungSession.getIsin();
        }
        lokaleVariablenInhalt[4]=tWillenserklaerungSession.getDatum();
        lokaleVariablenInhalt[5]=tWillenserklaerungSession.getNameEmittent();
        lokaleVariablenInhalt[6]=tWillenserklaerungSession.getNameBestaetigender();
        lokaleVariablenInhalt[7]=tWillenserklaerungSession.getNameAbstimmender();
        if (pMitteilung==null) {
            lokaleVariablenInhalt[8]=tWillenserklaerungSession.getAktionaersnummer();
            lokaleVariablenInhalt[9]=tWillenserklaerungSession.getAktionaersname();
            lokaleVariablenInhalt[10]=tWillenserklaerungSession.getEkNummer();
            lokaleVariablenInhalt[11]=tWillenserklaerungSession.getBevollmaechtigterVorname();
            lokaleVariablenInhalt[12]=tWillenserklaerungSession.getBevollmaechtigterName();
            lokaleVariablenInhalt[13]=tWillenserklaerungSession.getBevollmaechtigterOrt();
            lokaleVariablenInhalt[14]=tWillenserklaerungSession.getTopListe();
        }

        if (pMitteilung!=null) {
            lokaleVariablenInhalt[15]=pMitteilung.kontaktDaten;
            lokaleVariablenInhalt[16]=pMitteilung.kontaktDatenTelefon;
            
            String hInhaltshinweise="";
            for (int i=0;i<10;i++) {
                if (pMitteilung.inhaltsHinweis[i]) {
                    hInhaltshinweise=hInhaltshinweise+eclParamM.getParam().paramPortal.inhaltsHinweiseTextDE[i]+"\n\r";
                }
            }
            lokaleVariablenInhalt[17]=hInhaltshinweise;
           
            lokaleVariablenInhalt[18]=pMitteilung.mitteilungKurztext;
            lokaleVariablenInhalt[19]=pMitteilung.mitteilungLangtext;
         }
        
//      @formatter:on
        
        eclPortalTexteM.setLokaleVariablenNamen(lokaleVariablenNamen);
        eclPortalTexteM.setLokaleVariablenInhalt(lokaleVariablenInhalt);

    }
    
    
    private void versendeMailMitAnhang(int pBetreff, int pInhalt){
        String hBetreff=eclPortalTexteM.holeIText(pBetreff);
        String hMailText=eclPortalTexteM.holeIText(pInhalt);
        baMailM.sendenMitAnhang(tWillenserklaerungSession.getEmailFuerBestaetigung(), 
                hBetreff,
                hMailText,
                eclParamM.getClGlobalVar().lwPfadAllgemein+"\\"+eclParamM.getParamServer().praefixPfadVerzeichnisse
                        + "ausdrucke\\"+eclParamM.getMandantPfad()+
                "\\bes"+tWillenserklaerungSession.getNummerBestaetigung()+".pdf");
        
    }

    private void versendeMail(int pBetreff, int pInhalt){
        String hBetreff=eclPortalTexteM.holeIText(pBetreff);
        String hMailText=eclPortalTexteM.holeIText(pInhalt);
        baMailM.senden(tWillenserklaerungSession.getEmailFuerBestaetigung(), 
                hBetreff,
                hMailText);
        
    }


    
    /*************************************************Übergreifende Funktionen**********************************/
    private void fuelleAllgemeineVariablen(RpVariablen rpVariablen, RpDrucken rpDrucken) {
        rpVariablen.fuelleVariable(rpDrucken, "Art", tWillenserklaerungSession.getArt());
        rpVariablen.fuelleVariable(rpDrucken, "AusstellungsZeit", tWillenserklaerungSession.getAusstellungsZeit());
        rpVariablen.fuelleVariable(rpDrucken, "NummerBestaetigung", tWillenserklaerungSession.getNummerBestaetigung());
        rpVariablen.fuelleVariable(rpDrucken, "KennungVeranstaltung", tWillenserklaerungSession.getKennungVeranstaltung());
        rpVariablen.fuelleVariable(rpDrucken, "ISIN", tWillenserklaerungSession.getIsin());
        rpVariablen.fuelleVariable(rpDrucken, "Datum", tWillenserklaerungSession.getDatum());
        rpVariablen.fuelleVariable(rpDrucken, "NameEmittent", tWillenserklaerungSession.getNameEmittent());
        rpVariablen.fuelleVariable(rpDrucken, "NameBestaetigender", tWillenserklaerungSession.getNameBestaetigender());
        rpVariablen.fuelleVariable(rpDrucken, "NameAbstimmender", tWillenserklaerungSession.getNameAbstimmender());

        rpVariablen.fuelleVariable(rpDrucken, "Aktionaersnummer", tWillenserklaerungSession.getAktionaersnummer());
        rpVariablen.fuelleVariable(rpDrucken, "Aktionaersname", tWillenserklaerungSession.getAktionaersname());
        rpVariablen.fuelleVariable(rpDrucken, "EkNummer", tWillenserklaerungSession.getEkNummer());
        rpVariablen.fuelleVariable(rpDrucken, "BevollmaechtigterVorname", tWillenserklaerungSession.getBevollmaechtigterVorname());
        rpVariablen.fuelleVariable(rpDrucken, "BevollmaechtigterName", tWillenserklaerungSession.getBevollmaechtigterName());
        rpVariablen.fuelleVariable(rpDrucken, "BevollmaechtigterOrt", tWillenserklaerungSession.getBevollmaechtigterOrt());

        rpVariablen.fuelleVariable(rpDrucken, "TopListe", tWillenserklaerungSession.getTopListe());

        /*0=Aufruf aus Deutschem Portal, 1=Aufruf aus Englischem Portal*/
        if (tLanguage.getLang()==1) {
            rpVariablen.fuelleVariable(rpDrucken, "IstEnglisch", "0");
            CaBug.druckeLog("IstEnglisch=0", logDrucken, 10);
        }
        else {
            rpVariablen.fuelleVariable(rpDrucken, "IstEnglisch", "1");
            CaBug.druckeLog("IstEnglisch=1", logDrucken, 3);
        }

    }
    
}
