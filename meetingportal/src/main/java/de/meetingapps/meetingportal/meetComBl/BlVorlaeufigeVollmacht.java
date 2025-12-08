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
import de.meetingapps.meetingportal.meetComEh.EhVorlaeufigeVollmacht;
import de.meetingapps.meetingportal.meetComEntities.EclAktienregister;
import de.meetingapps.meetingportal.meetComEntities.EclBestWorkflow;
import de.meetingapps.meetingportal.meetComEntities.EclMeldung;
import de.meetingapps.meetingportal.meetComEntities.EclPersonenNatJur;
import de.meetingapps.meetingportal.meetComEntities.EclVorlaeufigeVollmacht;
import de.meetingapps.meetingportal.meetComEntities.EclVorlaeufigeVollmachtEingabe;
import de.meetingapps.meetingportal.meetComEntities.EclVorlaeufigeVollmachtFuerAnzeige;

/**Div. Informationen:
 * 
 * Zurücksetzen des Workflows:
 * ===========================
 * tbl_bestworkflow: enthält alle zu verarbeitenden PDFs
 * tbl_vorlaeufigeVollmachten
 * 
 * 
 * Verknüpfung der DBs
 * ===================
 * tbl_bestworkflow:
 * > Enthält alle per Post eingegangenen Dokumente
 * > Über vorlVollmachtIdent verknüpft zu zu tbl_vorlaeufigeVollmacht (auch wenn keinem Mitglied zuordenbar)
 * 
 * tbl_vorlaeufigeVollmacht:
 * > erteiltVonIdent ist das "gebende" Mitglied
 
 
 */


public class BlVorlaeufigeVollmacht {

    private int logDrucken = 10;

    private DbBundle lDbBundle = null;

    public int rcAnzSonstigeVollmachten = 0;
    public int rcAnzGesetzlicheVollmachten = 0;

    /**false => es ist derzeit keine gültige Vollmacht eingetragen*/
    public boolean rcVollmachtGueltigAnDritteEingetragenVorhanden = false;
    /**Aktuell eingetragene Vollmacht*/
    public EclVorlaeufigeVollmachtFuerAnzeige rcVollmachtGueltigeAnDritteEingetragenFuerAnzeige = null;
    public EclVorlaeufigeVollmacht rcVollmachtGueltigAnDritteEingetragen = null;

    /**false => es ist derzeit keine gültige gesetzliche Vollmacht eingetragen*/
    public boolean rcVollmachtGueltigGesetzlichEingetragenVorhanden = false;
    /**Aktuell eingetragene gesetzliche Vollmacht*/
    public EclVorlaeufigeVollmachtFuerAnzeige rcVollmachtGueltigeGesetzlichEingetragenFuerAnzeige = null;
    public EclVorlaeufigeVollmacht rcVollmachtGueltigGesetzlichEingetragen = null;

    /**Alle mit den gesetzlichen eingegangenen Vollmachten automatisch zugeordneten Vollmachten*/
    public List<EclVorlaeufigeVollmachtFuerAnzeige> rcListAllerMitGesetzlichenVollmachtenFuerAnzeige = null;
    public List<EclVorlaeufigeVollmacht> rcListAllerMitGesetzlichenVollmachten = null;

    /**Alle anderen eingegangenen Vollmachten (d.h. außer der aktuell eingetragenen Vollmacht)
     * (gesetzlich + Dritte)*/
    public List<EclVorlaeufigeVollmachtFuerAnzeige> rcListAllerAnderenVollmachtenFuerAnzeige = null;
    public List<EclVorlaeufigeVollmacht> rcListAllerAnderenVollmachten = null;

    /**Alle anderen gesetzlichen eingegangenen Vollmachten (d.h. außer der aktuell eingetragenen Vollmacht)*/
    public List<EclVorlaeufigeVollmachtFuerAnzeige> rcListAllerAnderenGesetzlichenVollmachtenFuerAnzeige = null;
    public List<EclVorlaeufigeVollmacht> rcListAllerAnderenGesetzlichenVollmachten = null;

    /**Alle anderen an Dritte (nicht gesetzliche) eingegangenen Vollmachten (d.h. außer der aktuell eingetragenen Vollmacht)*/
    public List<EclVorlaeufigeVollmachtFuerAnzeige> rcListAllerAnderenAnDritteVollmachtenFuerAnzeige = null;
    public List<EclVorlaeufigeVollmacht> rcListAllerAnderenAnDritteVollmachten = null;

    /**Wirklich alle Vollmachten*/
    public List<EclVorlaeufigeVollmacht> rcListAlleVollmachten = null;
    public List<EclBestWorkflow> rcListAlleBestWorkflow = null;

    /**Liste aller zum Mitglied zuordenbaren Vorgänge*/
    public List<EhVorlaeufigeVollmacht> rcEhVorlaeufigeVollmachtList=null;
    public BlVorlaeufigeVollmacht(DbBundle pDbBundle) {
        lDbBundle = pDbBundle;
    }

    /**Setzt rcAnzSonstigeVollmachten, rcAnzGesetzlicheVollmachten*/
    public void liefereAnzahlVollmachtenZuBevollmaechtigten(int pIdent, int pAktionaerOderKennung) {
        rcAnzSonstigeVollmachten = 0;
        rcAnzGesetzlicheVollmachten = 0;
        if (pAktionaerOderKennung == 1) {
            lDbBundle.dbVorlaeufigeVollmacht.readGegebenAnAktionaer(pIdent);
        } else {
            lDbBundle.dbVorlaeufigeVollmacht.readGegebenAnPerson(pIdent);
        }
        int anz = lDbBundle.dbVorlaeufigeVollmacht.anzErgebnis();
        CaBug.druckeLog("pAktionaerOderKennung=" + pAktionaerOderKennung + " pIdent=" + pIdent + " anz=" + anz,
                logDrucken, 10);
        for (int i = 0; i < anz; i++) {
            EclVorlaeufigeVollmacht lVorlaeufigeVollmacht = lDbBundle.dbVorlaeufigeVollmacht.ergebnisPosition(i);
            if (lVorlaeufigeVollmacht.storniert == 0) {
                if (lVorlaeufigeVollmacht.bevollmaechtigterAusgefuehrtIstGesetzlich == 0) {
                    rcAnzSonstigeVollmachten++;
                } else {
                    rcAnzGesetzlicheVollmachten++;
                }

            }
        }
    }

    /**Belegt 
     * rcVollmachtGueltigEingetragenVorhanden, 
     * rcVollmachtGueltigEingetragen, rcVollmachtGueltigeEingetragenFuerAnzeige,
     * rcListAllerAnderenVollmachten, rcListAllerAnderenVollmachtenFuerAnzeige
     * rcListAlleVollmachten, rcListAlleVollmachtenFuerAnzeige
     */
    public void liefereAusgehendeVollmachtenVonAktionaer(int pAktienregisterIdent) {
        rcListAllerAnderenVollmachtenFuerAnzeige = new LinkedList<EclVorlaeufigeVollmachtFuerAnzeige>();
        rcListAllerAnderenVollmachten = new LinkedList<EclVorlaeufigeVollmacht>();

        rcListAllerAnderenGesetzlichenVollmachtenFuerAnzeige = new LinkedList<EclVorlaeufigeVollmachtFuerAnzeige>();
        rcListAllerAnderenGesetzlichenVollmachten = new LinkedList<EclVorlaeufigeVollmacht>();

        rcListAllerMitGesetzlichenVollmachtenFuerAnzeige = new LinkedList<EclVorlaeufigeVollmachtFuerAnzeige>();
        rcListAllerMitGesetzlichenVollmachten = new LinkedList<EclVorlaeufigeVollmacht>();

        rcListAllerAnderenAnDritteVollmachtenFuerAnzeige = new LinkedList<EclVorlaeufigeVollmachtFuerAnzeige>();
        rcListAllerAnderenAnDritteVollmachten = new LinkedList<EclVorlaeufigeVollmacht>();

        rcListAlleVollmachten = new LinkedList<EclVorlaeufigeVollmacht>();

        rcVollmachtGueltigAnDritteEingetragenVorhanden = false;
        rcVollmachtGueltigGesetzlichEingetragenVorhanden = false;

        /*Alle vorläufigen Vollmachten, die von diesem Aktionär ausgehen, einlesen*/
        lDbBundle.dbVorlaeufigeVollmacht.readErteiltVonAktionaer(pAktienregisterIdent);
        int anzVorlVollmachten = lDbBundle.dbVorlaeufigeVollmacht.anzErgebnis();

        /*Alle vorläufigen Vollmachten durcharbeiten im Hinblick auf gültige etc.*/
        for (int i = 0; i < anzVorlVollmachten; i++) {
            EclVorlaeufigeVollmacht lVorlaeufigeVollmacht = lDbBundle.dbVorlaeufigeVollmacht.ergebnisPosition(i);
            EclAktienregister lAktienregister = null;
            if (lVorlaeufigeVollmacht.erteiltVonArt == 1) {
                lDbBundle.dbAktienregister.leseZuAktienregisterIdent(lVorlaeufigeVollmacht.erteiltVonIdent);
                lAktienregister = lDbBundle.dbAktienregister.ergebnisPosition(0);
            }
            lVorlaeufigeVollmacht.eclAktienregister = lAktienregister;
            if (lVorlaeufigeVollmacht.pruefstatus == 2 && lVorlaeufigeVollmacht.storniert == 0) {
                /*Eine gültige Vollmacht wurde gefunden*/
                if (lVorlaeufigeVollmacht.bevollmaechtigterAusgefuehrtIstGesetzlich == 1) {
                    /*rcGVvollmachtEingetragen*/
                    rcVollmachtGueltigGesetzlichEingetragenVorhanden = true;

                    /*Übertragen in aktuelle Vollmacht*/
                    rcVollmachtGueltigeGesetzlichEingetragenFuerAnzeige = new EclVorlaeufigeVollmachtFuerAnzeige(
                            lVorlaeufigeVollmacht, lDbBundle);
                    rcVollmachtGueltigGesetzlichEingetragen = lVorlaeufigeVollmacht;
                } else {
                    /*rcVollmachtGueltigAnDritteEingetragenVorhanden*/
                    rcVollmachtGueltigAnDritteEingetragenVorhanden = true;

                    /*Übertragen in aktuelle Vollmacht*/
                    rcVollmachtGueltigeAnDritteEingetragenFuerAnzeige = new EclVorlaeufigeVollmachtFuerAnzeige(
                            lVorlaeufigeVollmacht, lDbBundle);
                    rcVollmachtGueltigAnDritteEingetragen = lVorlaeufigeVollmacht;
                }

                /*Übertragen in alle Vollmachten*/
                rcListAlleVollmachten.add(lVorlaeufigeVollmacht);
            } else {
                /*Übertragen in sonstige Vollmachten*/
                EclVorlaeufigeVollmachtFuerAnzeige lVorlaeufigeVollmachtFuerAnzeige = new EclVorlaeufigeVollmachtFuerAnzeige(
                        lVorlaeufigeVollmacht, lDbBundle);
                CaBug.druckeLog("in rcListAllerAnderenVollmachtenFuerAnzeige eingetragen i=" + i, logDrucken, 10);
                rcListAllerAnderenVollmachtenFuerAnzeige.add(lVorlaeufigeVollmachtFuerAnzeige);
                rcListAllerAnderenVollmachten.add(lVorlaeufigeVollmacht);

                /*Übertragen in alle Vollmachten*/
                rcListAlleVollmachten.add(lVorlaeufigeVollmacht);

                /*Liste der gesetzlichen / Sonstigen füllen*/
                if (lVorlaeufigeVollmacht.bevollmaechtigterAusgefuehrtIstGesetzlich == 1) {
                    rcListAllerAnderenGesetzlichenVollmachtenFuerAnzeige.add(lVorlaeufigeVollmachtFuerAnzeige);
                    rcListAllerAnderenGesetzlichenVollmachten.add(lVorlaeufigeVollmacht);
                } else {
                    rcListAllerAnderenAnDritteVollmachtenFuerAnzeige.add(lVorlaeufigeVollmachtFuerAnzeige);
                    rcListAllerAnderenAnDritteVollmachten.add(lVorlaeufigeVollmacht);
                }

            }
        }

        /*ggf. mit der gesetzlichen Vollmach weitergegebenen Vollmachten einlesen*/
        if (rcVollmachtGueltigGesetzlichEingetragenVorhanden == true) {
            lDbBundle.dbVorlaeufigeVollmacht.readGegebenAnAktionaer(pAktienregisterIdent);
            anzVorlVollmachten = lDbBundle.dbVorlaeufigeVollmacht.anzErgebnis();

            /*Alle vorläufigen Vollmachten durcharbeiten im Hinblick auf gültige etc.*/
            for (int i = 0; i < anzVorlVollmachten; i++) {
                EclVorlaeufigeVollmacht lVorlaeufigeVollmacht = lDbBundle.dbVorlaeufigeVollmacht.ergebnisPosition(i);
                EclAktienregister lAktienregister = null;
                if (lVorlaeufigeVollmacht.erteiltVonArt == 1) {
                    lDbBundle.dbAktienregister.leseZuAktienregisterIdent(lVorlaeufigeVollmacht.erteiltVonIdent);
                    lAktienregister = lDbBundle.dbAktienregister.ergebnisPosition(0);
                }
                lVorlaeufigeVollmacht.eclAktienregister = lAktienregister;
                if (lVorlaeufigeVollmacht.pruefstatus == 2 && lVorlaeufigeVollmacht.storniert == 0) {
                    /*Eine gültige Vollmacht wurde gefunden*/
                    /*Übertragen in gesetzliche mitgegebenen Vollmachten*/
                    rcListAllerMitGesetzlichenVollmachtenFuerAnzeige
                            .add(new EclVorlaeufigeVollmachtFuerAnzeige(lVorlaeufigeVollmacht, lDbBundle));
                    rcListAllerMitGesetzlichenVollmachten.add(lVorlaeufigeVollmacht);
                }
            }
        }

    }

    /**Ergänzt die vorher mit liefereAusgehendeVollmachtenVonAktionaer gelesenen Daten um die Workflow-Daten
     * in rcListAlleBestWorkflow
     */
    public void ergaenzeWorkflowDaten() {
        rcListAlleBestWorkflow = new LinkedList<EclBestWorkflow>();
        for (int i = 0; i < rcListAlleVollmachten.size(); i++) {
            lDbBundle.dbBestWorkflow.readZuVorlVollmacht(rcListAlleVollmachten.get(i).ident);
            rcListAlleBestWorkflow.add(lDbBundle.dbBestWorkflow.ergebnisPosition(0));
        }
    }

    /**pMeldungsIdent=die Ident der (einzigen) Meldung des Aktionärs*/
    public void storniere(EclVorlaeufigeVollmacht pVorlaeufigeVollmacht, int pErteiltAufWeg) {

        int erteiltVonIdent = 0;
        EclMeldung lMeldung = null;
        if (pVorlaeufigeVollmacht.erteiltVonArt == 1) {
            /*Aktienregister*/
            int aktienregisterIdent = pVorlaeufigeVollmacht.erteiltVonIdent;
            lDbBundle.dbMeldungen.leseZuAktienregisterIdent(aktienregisterIdent, true);
            lMeldung = lDbBundle.dbMeldungen.meldungenArray[0];
            erteiltVonIdent = lMeldung.meldungsIdent;
        } else {
            /*PersonNatJur*/
            /*Untervollmachten nicht implementiert*/
        }

        pVorlaeufigeVollmacht.storniert = 1;
        lDbBundle.dbVorlaeufigeVollmacht.update(pVorlaeufigeVollmacht);

        int personNatJurIdent = 0;
        int loginDatenIdent = 0;

        /**Wird hier ermittelt und belegt, um später zu prüfen, ob möglicherweise Vollmacht
         * vererbt wurde
         */
        int aktienregisterIdentDesBevollmaechtigten = 0;
        if (pVorlaeufigeVollmacht.bevollmaechtigterAusgefuehrtArt == 1) {
            /*Aktienregister*/
            lDbBundle.dbAktienregister
                    .leseZuAktienregisterIdent(pVorlaeufigeVollmacht.bevollmaechtigterAusgefuehrtIdent);
            personNatJurIdent = lDbBundle.dbAktienregister.ergebnisPosition(0).personNatJur;
            aktienregisterIdentDesBevollmaechtigten = pVorlaeufigeVollmacht.bevollmaechtigterAusgefuehrtIdent;

            lDbBundle.dbLoginDaten.read_aktienregisterIdent(pVorlaeufigeVollmacht.bevollmaechtigterAusgefuehrtIdent);
            loginDatenIdent = lDbBundle.dbLoginDaten.ergebnisPosition(0).ident;
        } else {
            /*PersonNatJur*/
            personNatJurIdent = pVorlaeufigeVollmacht.bevollmaechtigterAusgefuehrtIdent;

            lDbBundle.dbLoginDaten.read_personNatJurIdent(pVorlaeufigeVollmacht.bevollmaechtigterAusgefuehrtIdent);
            loginDatenIdent = lDbBundle.dbLoginDaten.ergebnisPosition(0).ident;
        }

        /*Willenserklärung stornieren*/
        BlWillenserklaerung vmWillenserklaerung = new BlWillenserklaerung();
        vmWillenserklaerung.pErteiltAufWeg = pErteiltAufWeg;
        vmWillenserklaerung.pErteiltZeitpunkt = "";
        vmWillenserklaerung.piMeldungsIdentAktionaer = erteiltVonIdent;
        EclPersonenNatJur lPersonenNatJur = new EclPersonenNatJur();
        lPersonenNatJur.ident = personNatJurIdent;
        vmWillenserklaerung.pEclPersonenNatJur = lPersonenNatJur;
        vmWillenserklaerung.pWillenserklaerungGeberIdent = -1; /*Aktionär*/
        vmWillenserklaerung.widerrufVollmachtAnDritte(lDbBundle);
        if (vmWillenserklaerung.rcIstZulaessig == false) {
            CaBug.drucke(
                    "001 vmWillenserklaerung.rcGrundFuerUnzulaessig=" + vmWillenserklaerung.rcGrundFuerUnzulaessig);
        }

        if (lMeldung != null && lMeldung.statusPraesenz == 1) {
            /*Vollmacht war bereits präsent - Abgang buchen*/
            BlPraesenzVirtuell blPraesenzVirtuell = new BlPraesenzVirtuell(lDbBundle);
            blPraesenzVirtuell.buchenAktionaer_abgang(lMeldung, 0);
        }

        if (loginDatenIdent != 0) {
            /*"Veränderte" login-Kennung auf "Refresh" setzen*/
            lDbBundle.dbLoginDaten.update_letzterLoginAufServerNegativ(loginDatenIdent);
        }
        /**Bevollmächtigter war ein Aktionär. Nun prüfen, ob möglicherweise Vollmacht
         * vererbt wurde, und dann dort login auf "Refresh" setzen
         */
        if (aktienregisterIdentDesBevollmaechtigten != 0) {
            /*Gültige Vollmachten ausgehend von dem Bevollmächtigten einlesen und prüfen, ob
             * gesetzliche Vertreteung dabei ist
             */
            lDbBundle.dbVorlaeufigeVollmacht.readErteiltUndGueltigVonAktionaer(aktienregisterIdentDesBevollmaechtigten);
            int anzUnterVollmachten = lDbBundle.dbVorlaeufigeVollmacht.anzErgebnis();
            if (anzUnterVollmachten > 0) { /*Es können zwei Vollmachten vorhanden sein - eine gesetzliche und eine normale*/
                for (int i = 0; i < anzUnterVollmachten; i++) {
                    EclVorlaeufigeVollmacht lVorlaeufigeVollmacht = lDbBundle.dbVorlaeufigeVollmacht
                            .ergebnisPosition(i);
                    if (lVorlaeufigeVollmacht.bevollmaechtigterAusgefuehrtIstGesetzlich == 1) {
                        int loginDatenVererbt = 0;
                        if (lVorlaeufigeVollmacht.bevollmaechtigterAusgefuehrtArt == 1) {
                            /*Aktienregister*/
                            lDbBundle.dbLoginDaten
                                    .read_aktienregisterIdent(lVorlaeufigeVollmacht.bevollmaechtigterAusgefuehrtIdent);
                            loginDatenVererbt = lDbBundle.dbLoginDaten.ergebnisPosition(0).ident;
                        } else {
                            /*PersonNatJur*/
                            personNatJurIdent = pVorlaeufigeVollmacht.bevollmaechtigterAusgefuehrtIdent;

                            lDbBundle.dbLoginDaten
                                    .read_personNatJurIdent(lVorlaeufigeVollmacht.bevollmaechtigterAusgefuehrtIdent);
                            loginDatenVererbt = lDbBundle.dbLoginDaten.ergebnisPosition(0).ident;
                        }
                        /*"Veränderte" login-Kennung auf "Refresh" setzen*/
                        lDbBundle.dbLoginDaten.update_letzterLoginAufServerNegativ(loginDatenVererbt);
                    }
                }
            }

        }

    }
    
    public void belegeListeVorlaeufigeVollmachten(int pAktienregisterIdent) {
        lDbBundle.dbAktienregister.leseZuAktienregisterIdent(pAktienregisterIdent);
        String hAktionaersnummer=lDbBundle.dbAktienregister.ergebnisPosition(0).aktionaersnummer;
        belegeListeVorlaeufigeVollmachten(hAktionaersnummer, pAktienregisterIdent);
        
    }
    
    public void belegeListeVorlaeufigeVollmachten(String pAktionaersnummer, int pAktienregisterIdent) {
        rcEhVorlaeufigeVollmachtList=new LinkedList<EhVorlaeufigeVollmacht>();
        
        /*Alle vorläufigen Vollmachten, die von diesem Aktionär ausgehen, einlesen*/
        lDbBundle.dbVorlaeufigeVollmacht.readErteiltVonAktionaer(pAktienregisterIdent);
        int anzVorlVollmachten = lDbBundle.dbVorlaeufigeVollmacht.anzErgebnis();
        for (int i = 0; i < anzVorlVollmachten; i++) {
            EhVorlaeufigeVollmacht lEhVorlaeufigeVollmacht=new EhVorlaeufigeVollmacht();
            lEhVorlaeufigeVollmacht.eclVorlaeufigeVollmacht=lDbBundle.dbVorlaeufigeVollmacht.ergebnisPosition(i);
            lEhVorlaeufigeVollmacht.art=0;
            
            /*Nun ggf noch Papier-Dokument dazuladen*/
            int vorlaeufigeVollmachtIdent=lEhVorlaeufigeVollmacht.eclVorlaeufigeVollmacht.ident;
            if (vorlaeufigeVollmachtIdent!=0) {
                lDbBundle.dbBestWorkflow.readZuVorlVollmacht(vorlaeufigeVollmachtIdent);
                lEhVorlaeufigeVollmacht.eclBestWorkflow=lDbBundle.dbBestWorkflow.ergebnisPosition(0);
                if (lEhVorlaeufigeVollmacht.eclBestWorkflow.origOderKopie!=3) {
                    /**Nur in Liste aufnehmen, wenn es wirklich ein Dokument ist (nicht nur ein Dummy-Satz für elektronische Dokumente)*/
                    rcEhVorlaeufigeVollmachtList.add(lEhVorlaeufigeVollmacht);
                }
            }

        }
        
        lDbBundle.dbVorlaeufigeVollmachtEingabe.readArtZuAktionaersnummerUndReserviere(pAktionaersnummer, 0);
        int anzVorlVollmachtenEingabe=lDbBundle.dbVorlaeufigeVollmachtEingabe.anzErgebnis();
        CaBug.druckeLog("anzVorlVollmachtenEingabe="+anzVorlVollmachtenEingabe, logDrucken, 10);
        for (int i = 0; i < anzVorlVollmachtenEingabe; i++) {
            EhVorlaeufigeVollmacht lEhVorlaeufigeVollmacht=new EhVorlaeufigeVollmacht();
            EclVorlaeufigeVollmachtEingabe lVorlaeufigeVollamchtEingabe=lDbBundle.dbVorlaeufigeVollmachtEingabe.ergebnisPosition(i);
            lEhVorlaeufigeVollmacht.eclVorlaeufigeVollmachtEingabe=lVorlaeufigeVollamchtEingabe;
            lEhVorlaeufigeVollmacht.art=lEhVorlaeufigeVollmacht.eclVorlaeufigeVollmachtEingabe.artDerEingabe;
            rcEhVorlaeufigeVollmachtList.add(lEhVorlaeufigeVollmacht);
            if (lEhVorlaeufigeVollmacht.art==EclVorlaeufigeVollmachtEingabe.ART_DER_EINGABE_BEVOLLMAECHTIGTER) {
                /*Bei Bevollmächtigtem: Falls Mitgliedsnummer angegeben, dann dazuladen*/
                String hAktionaersnummer=lVorlaeufigeVollamchtEingabe.vertreterId.trim();
                if (CaString.isNummern(hAktionaersnummer)) {
                    BlNummernformBasis blNummernformBasis = new BlNummernformBasis(lDbBundle);
                    String hAktionaersnummerIntern=blNummernformBasis
                            .loginKennungAufbereitenFuerIntern(hAktionaersnummer);
              
                    int erg = lDbBundle.dbAktienregister.leseZuAktienregisternummer(hAktionaersnummerIntern);
                    if (erg > 0) {
                        EclAktienregister lAktienregister = lDbBundle.dbAktienregister.ergebnisArray[0];
                        lVorlaeufigeVollamchtEingabe.vertreterAusRegisterGeladen=true;
                        lVorlaeufigeVollamchtEingabe.ausRegisterName=lAktienregister.nachname;
                        lVorlaeufigeVollamchtEingabe.ausRegisterVorname=lAktienregister.vorname;
                        lVorlaeufigeVollamchtEingabe.ausRegisterStrasse=lAktienregister.strasse;
                        lVorlaeufigeVollamchtEingabe.ausRegisterPLZ=lAktienregister.postleitzahl;
                        lVorlaeufigeVollamchtEingabe.ausRegisterOrt=lAktienregister.ort;
                    }
                }
            }
        }
        
    }
}
