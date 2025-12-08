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

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaDatumZeit;
import de.meetingapps.meetingportal.meetComAllg.CaPasswortErzeugen;
import de.meetingapps.meetingportal.meetComAllg.CaPasswortVerschluesseln;
import de.meetingapps.meetingportal.meetComDb.DbAnreden;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclAktienregister;
import de.meetingapps.meetingportal.meetComEntities.EclLoginDaten;
import de.meetingapps.meetingportal.meetComEntities.EclMeldung;
import de.meetingapps.meetingportal.meetComEntities.EclMeldungenMeldungen;
import de.meetingapps.meetingportal.meetComEntities.EclZutrittsIdent;
import de.meetingapps.meetingportal.meetComEntities.EclZutrittskarten;
import de.meetingapps.meetingportal.meetComKonst.KonstKartenart;
import de.meetingapps.meetingportal.meetComKonst.KonstKartenklasse;
import de.meetingapps.meetingportal.meetComKonst.KonstPortalFunktionen;
import de.meetingapps.meetingportal.meetingCoreReport.RpDrucken;
import de.meetingapps.meetingportal.meetingCoreReport.RpVariablen;

public class BlGastkarte {

    private int logDrucken = 10;

    private DbBundle lDbBundle = null;

    /** Daten des Gastes (ohne Eintrittskartennummer). 
     * 	Falls kurzName und kurzOrt nicht gefüllt, werden diese aus name, vorname, ort gefüllt.*/
    public EclMeldung pGast = null;
    
    /**wenn nicht leer, dann wird diese Zutrittsident verwendet*/
    public String pZutrittsIdent="";
    
    public long berechtigungsWert=0;
    
    /*TODO # Konsolidierung Da nicht in EclMeldung enthalten, aber für Anrede gebraucht - für Gästemodul speziell Selbstdruck*/
    public String pAnredeKomplett = "";
    public String pAnrede = "";

    /**Formular-Nummer, das für den Druck verwendet werden soll. Wird z.B. aus der zugeordneten Gruppe bestimmt*/
    public int pGastEKFormular = 1;

    /** derjenige, der die Willenserklärung erteilt (siehe BlWillenserklaerung)*/
    public int pWillenserklaerungGeberIdent = 0;

    /** Aktienregistereintrag, mit dem die Gastkarte verknüpft werden soll.
    * 	Kann null sein (dann keine Verknüpfung)
    * 	Muß vollständig gefüllt sein, wenn pVersandAnAdresseAusAktienregister==1 ist!*/
    public EclAktienregister pAktienregisterEintrag = null;

    /**	=2 => Aufname in Batch
    * 		=3 => Online-(Sofort-)Druck erfolgt im Portal (Selbstdruck Aktionär)
    * 		=4 => Online-(Sofort-)Email erfolgt
    * 		=5 => Online-(Sofort-)Druck erfolgt im Portal (durch Emittent)
    * 		=6 => Stapeldruck, d.h. 
    * 				rpVariablen und rpDrucken*/

    public int pVersandart = 2;

    private RpDrucken rpDrucken = null;
    private boolean druckerInitialisiert=false;
    private RpVariablen rpVariablen = null;
    private boolean druckstartErfolgt = false;

    /**Falls String gefüllt, dann wird beim Druck dieser Dateiname anstelle von "zutrittsdokument"
     * bzw. "gastkarten" verwendet*/ 
    public String pAbweichenderDateiname="";
    
    /** Array mit separater Versandadresse (maximal 6 Zeilen), fertig postalisch aufbereitet. Kann null sein.*/
    public String[] pVersandadresse = null;

    /** 	=0 => nein
    * 		=1 => Ja - Versandadresse wird aus Parameter pAktienregisterEintrag geholt*/
    public int pVersandAnAdresseAusAktienregister = 0;

    /*****Eingabe-Parameter für GK-Nr-Storno******/
    public String pZutrittsIdentStorno = "";
    public String pZutrittsIdentNebenStorno = "";

    /*******************************Return-Parameter**************************/
    /**ZutrittsIdent der erzeugten Eintrittskarte*/
    public String rcZutrittsIdent = "";
    public String rcZutrittsIdentNeben = "";

    public String rcKennung = "";
    public String rcKennungAlternativ="";
    public String rcPasswort = "";

    /**Dateinr des erzeugten Formulars für Ausdruck*/
    public int rcGastkartePdfNr = 0;
    
    /**Name der erzeugten PDF-Datei bei pVersandart=6*/
    public String rcNamePDF = "";

    /**Drucken in PDF*/
    public boolean rcDruckenInPDF = true;

    /**Papierformat - Hinweis auf das einzulegende Papierformat*/
    public String rcHinweisPapierformat = "";

    public BlGastkarte(DbBundle pDbBundle) {
        lDbBundle = pDbBundle;
    }

    public void initRpDrucken(RpDrucken pRpDrucken) {
        rpDrucken=pRpDrucken;
        druckerInitialisiert=true;
        
    }
    
    /**Ausstellen und ggf. Sofortdruck-/Versand einer Gastkarte.
     * Erforderliche / mögliche Parameter:
     * 	Grundsätzliche Daten (Name, Ort etc.):
     * 		EclMeldung pGast
     *  int pWillenserklaerungGeberIdent
     * 	EclAktienregisterEintrag pAktienregisterEintrag (kann null sein)
     * 	int pVersandart
     *  String[] pVersandadresse; Ist nach der Funktion ggf. verändert (insbesondere wenn pVersandAnAdresseAusAktienregister ==1)
     *  int pVersandAnAdresseAusAktienregister
     *  
     *  
     */
    public int ausstellen() {

        lDbBundle.dbBasis.beginTransaction();

        /******Willenserklärung für Anmeldung********************/
        BlWillenserklaerung lWillenserklaerungGast = new BlWillenserklaerung();

        /*Meldung Gast füllen*/
        if (pGast.kurzName.isEmpty()) {
            pGast.kurzName = pGast.name;
            if (!pGast.vorname.isEmpty()) {
                pGast.kurzName = pGast.kurzName + " " + pGast.vorname;
            }
        }
        if (pGast.kurzOrt.isEmpty()) {
            pGast.kurzOrt = pGast.ort;
        }
        lWillenserklaerungGast.pEclMeldungNeu = pGast;

        /*Restliche Parameter füllen*/
        lWillenserklaerungGast.pAnzahlAnmeldungen = 1;
        lWillenserklaerungGast.pWillenserklaerungGeberIdent = pWillenserklaerungGeberIdent;
        lWillenserklaerungGast.pEclAktienregisterEintrag = pAktienregisterEintrag; /*Verknüpfung zum Aktienregistereintrag herstellen. Kann null sein!*/
        lWillenserklaerungGast.pBerechtigungsWert=berechtigungsWert;
        lWillenserklaerungGast.anmeldungGast(lDbBundle);
        
        if (lWillenserklaerungGast.rcIstZulaessig == false) {
            lDbBundle.dbBasis.endTransaction();
            return lWillenserklaerungGast.rcGrundFuerUnzulaessig;
        }

        pGast.meldungsIdent = lWillenserklaerungGast.rcMeldungen[0];

        CaBug.druckeLog("BlGastkarte lMeldung.meldungsIdent="+pGast.meldungsIdent, logDrucken, 10);
        
        rcKennung = lWillenserklaerungGast.rcLoginDaten.loginKennung;
        rcKennungAlternativ=lWillenserklaerungGast.rcLoginDaten.loginKennungAlternativ;
        rcPasswort = CaPasswortVerschluesseln.liefereInitialPasswort(
                lWillenserklaerungGast.rcLoginDaten.passwortInitial, lDbBundle.param.paramPortal.passwortMindestLaenge);
        //		pGast.kommunikationssprache=-1;

        int rc = neueGKNr(lWillenserklaerungGast.rcMeldungen[0], lWillenserklaerungGast.rcWillenserklaerungIdent);
        if (rc < 1) {
            CaBug.druckeLog("Return nach neuGKNr", logDrucken, 10);
            lDbBundle.dbBasis.endTransaction();
            return rc;
        }

        if (pVersandart == 3 || pVersandart == 4 || pVersandart == 5 || pVersandart == 6) {
            int erg = this.drucken(lDbBundle);
            if (erg < 0) {
                return erg;
            }
        }
        lDbBundle.dbBasis.endTransaction();
        CaBug.druckeLog("Return ok", logDrucken, 10);
        return 0;

    }

    /**nochmal Drucken einer EK, für die bereits Daten und Willenserklärungen gespeichert bzw. ZutrittsIdent
     * vergeben wurde. Anwendung z.B.:
     * 		> Daten haben sich leicht geändert, nochmal drucken
     * 		> Ersatzkarte ausdrucken (die bisher noch nicht vorhanden ist)
     * 
     * Offen ist dabei noch einiges (z.B. Handhabung Versandadresse; Speichern der verschiedenen Druckvorgänge etc).
     * 
     * Gefüllt werden müssen auf jeden Fall:
     * 	pVersandart
     * 	rcZutrittsIdent
     * 	pGast
     * 	pVersandadresse[] (wenn leer, dann wird dies hier mit Gastdaten gefüllt)
     * 
     * Returnwerte:
     * 	rcHinweisPapierformat
     * 	rcGastkartePdfNr
     */
    @Deprecated
    public int reDrucken() {

        lDbBundle.dbBasis.beginTransaction();

        /****Willenserklärung für ZutrittsIdent**********************/
        BlWillenserklaerung ekWillenserklaerungGast = new BlWillenserklaerung();
        ekWillenserklaerungGast.piMeldungsIdentGast = pGast.meldungsIdent;

        /*Versandadresse*/
        if (pVersandadresse == null && pVersandAnAdresseAusAktienregister == 0) {
            /*Versandadresse aus Gastdaten zusammensetzen*/
            pVersandadresse = new String[5];
            int ii, zeile;
            for (ii = 0; ii < 5; ii++) {
                pVersandadresse[ii] = "";
            }
            zeile = 0;
            /*Anrede (optional)*/
            if (pGast.anrede != 0) {
                DbAnreden lDbAnreden = new DbAnreden(lDbBundle);
                int erg = lDbAnreden.ReadAnrede_Anredennr(pGast.anrede);
                if (erg < 1) {
                    CaBug.drucke("BlGastkarte.reDrucken 001");
                }
                pVersandadresse[zeile] = lDbAnreden.anredenreadarray[0].anredentext;
                zeile++;
            }
            /*Adelstitel, Titel, Vorname, Name (fest)*/
            if (!pGast.adelstitel.isEmpty()) {
                pVersandadresse[zeile] = pVersandadresse[zeile] + pGast.adelstitel + " ";
            }
            if (!pGast.titel.isEmpty()) {
                pVersandadresse[zeile] = pVersandadresse[zeile] + pGast.titel + " ";
            }
            if (!pGast.vorname.isEmpty()) {
                pVersandadresse[zeile] = pVersandadresse[zeile] + pGast.vorname + " ";
            }
            pVersandadresse[zeile] = pVersandadresse[zeile] + pGast.name;
            zeile++;
            /*Straße (optional)*/
            if (!pGast.strasse.isEmpty()) {
                pVersandadresse[zeile] = pGast.strasse;
                zeile++;
            }
            /*Land PLZ Ort (Fest)*/
            if (!pGast.land.isEmpty()) {
                pVersandadresse[zeile] = pVersandadresse[zeile] + pGast.land + " ";
            }
            if (!pGast.plz.isEmpty()) {
                pVersandadresse[zeile] = pVersandadresse[zeile] + pGast.plz + " ";
            }
            pVersandadresse[zeile] = pVersandadresse[zeile] + pGast.ort + " ";
            zeile++;
        }
        if (pVersandAnAdresseAusAktienregister == 1) {
            /*Versandadresse aus Aktienregister zusammensetzen*/
            pVersandadresse = new String[5];
            int ii;
            for (ii = 0; ii < 5; ii++) {
                pVersandadresse[ii] = "";
            }

            pVersandadresse[0] = "Versand an im Aktienregister hinterlegte";
            pVersandadresse[1] = pAktienregisterEintrag.vorname + pAktienregisterEintrag.nachname;
            pVersandadresse[2] = pAktienregisterEintrag.ort;
            pVersandadresse[3] = "Noch Vervollständigen";
        }

        if (pVersandadresse.length >= 1) {
            ekWillenserklaerungGast.pVersandadresse1 = pVersandadresse[0];
        }
        if (pVersandadresse.length >= 2) {
            ekWillenserklaerungGast.pVersandadresse2 = pVersandadresse[1];
        }
        if (pVersandadresse.length >= 3) {
            ekWillenserklaerungGast.pVersandadresse3 = pVersandadresse[2];
        }
        if (pVersandadresse.length >= 4) {
            ekWillenserklaerungGast.pVersandadresse4 = pVersandadresse[3];
        }
        if (pVersandadresse.length >= 5) {
            ekWillenserklaerungGast.pVersandadresse5 = pVersandadresse[4];
        }

        /*Versandart*/
        switch (pVersandart) {
        case 2:/*Aufnahme in Sammelbatch, bei nächstem Drucklauf ausdrucken und versenden*/
            ekWillenserklaerungGast.pVersandartEK = 2;
            break;
        case 3:/*Online-Ausdruck (im Portal) erfolgt*/
            ekWillenserklaerungGast.pVersandartEK = 3;
            break;
        case 4:/*Versand per Email (im Portal) erfolgt*/
            ekWillenserklaerungGast.pVersandartEK = 4;
            ekWillenserklaerungGast.pEmailAdresseEK = pGast.mailadresse;
            break;
        }

        ekWillenserklaerungGast.pZutrittsIdent.zutrittsIdent = rcZutrittsIdent;
        ekWillenserklaerungGast.zuZutrittsIdentNeuesDokument(lDbBundle);
        if (ekWillenserklaerungGast.rcIstZulaessig == false) {
            lDbBundle.dbBasis.endTransaction();
            return ekWillenserklaerungGast.rcGrundFuerUnzulaessig;
        }

        rcZutrittsIdent = ekWillenserklaerungGast.pZutrittsIdent.zutrittsIdent;

        if (pVersandart == 3 || pVersandart == 4 || pVersandart == 5) {
            rcGastkartePdfNr = ekWillenserklaerungGast.rcWillenserklaerungIdent;

            int erg = this.drucken(lDbBundle);
            if (erg < 0) {
                return erg;
            }
        }
        lDbBundle.dbBasis.endTransaction();
        return 0;
    }

    /**Ausdrucken
     * 
     * Verwendete Variablen aus dieser Klasse:
     * 	rcGastkartePdfNr = Nr. der PDF-Datei, die verwendet wird (d.h. die zu druckende Datei wird darin abgespeichert)
     * 		(nur, wenn rcDruckenInPDF=true, Standardwert)
     * 	pVersandart (zusammen mit pGastEKFormular) = Auswahl des Formulars, das verwendet wird
     * 	rcZutrittsIdent, rcZutrittsIdentNeben
     * 	pVersandadresse[]
     *  pGast
     *  pAnrede
     *  pAnredeKomplett
     *  rcKennung
     *  rcPasswort
     * 
     * Variablen, die gefüllt werden:
     * 	rcHinweisPapierformat
     * 
     * Returncode <0 => fehler, ansonsten ok.
     * @return
     */
    public int drucken(DbBundle pDbBundle) {
        /*Gastkarten-PDF erzeugen*/

        if (druckstartErfolgt == false) {
            if (druckerInitialisiert==false) {
                rpDrucken = new RpDrucken();

                if (rcDruckenInPDF) {
                    rpDrucken.initServer();

                    rpDrucken.exportFormat = 1;
                    rpDrucken.exportVerzeichnis = pDbBundle.lieferePfadMeetingAusdrucke();
                    if (pVersandart != 6) {
                        String hName=pAbweichenderDateiname;
                        if (hName.isEmpty()) {hName="zutrittsdokumentM";}
                        rpDrucken.exportDatei = hName + lDbBundle.getMandantString()
                        + Integer.toString(rcGastkartePdfNr);
                    } else {
                        String hName=pAbweichenderDateiname;
                        if (hName.isEmpty()) {hName="gastkartenM";}
                        rpDrucken.exportDatei = hName + lDbBundle.getMandantString()
                        + CaDatumZeit.DatumZeitStringFuerDateiname();
                        rcNamePDF = rpDrucken.exportDatei;
                    }
                }
            }

            rpDrucken.initFormular(pDbBundle);

            /*Variablen füllen - sowie Dokumentvorlage*/
            rpVariablen = new RpVariablen(pDbBundle);
            if (pGastEKFormular == 0) {
                pGastEKFormular = 1;
            }
            String formularNummer = Integer.toString(pGastEKFormular);
            CaBug.druckeLog("pVersandart=" + pVersandart, logDrucken, 10);
            if (pVersandart == 3) {
                rpVariablen.gastkarteSelbstdruck(formularNummer, rpDrucken);
            }
            if (pVersandart == 4) {
                rpVariablen.gastkarteMail(formularNummer, rpDrucken);
            }
            if (pVersandart == 5) {
                rpVariablen.gastkarteDruckEmittent(formularNummer, rpDrucken);
            }
            if (pVersandart == 6) {
                rpVariablen.gastkarteDruckEmittent(formularNummer, rpDrucken);
            }

            druckstartErfolgt = true;
            rpDrucken.startFormular();

        }

        rcHinweisPapierformat = ""; /*ehemals:=RpZugeordnetesFormular.papierformular;*/
        /*TODO _Konsolidierung: Gastkarte Drucken, Papierformat vorsehen!*/
        CaBug.druckeLog("rpDrucken.dateinameLLQuelle=" + rpDrucken.dateinameLLQuelle, logDrucken, 2);

        String hDatum = CaDatumZeit.DatumStringFuerAnzeigeMonatAusgeschrieben();
        rpVariablen.fuelleVariable(rpDrucken, "Allgemein.Datum", hDatum);
        rpVariablen.fuelleVariable(rpDrucken, "Allgemein.Gesellschaft", lDbBundle.eclEmittent.bezeichnungLang);

        BlNummernformen blNummernformen = new BlNummernformen(lDbBundle);
        rpVariablen.fuelleVariable(rpDrucken, "Nummern.ZutrittsIdent",
                BlZutrittsIdent.aufbereitenFuerAnzeige(rcZutrittsIdent, rcZutrittsIdentNeben));
        rpVariablen.fuelleVariable(rpDrucken, "Nummern.ZutrittsIdentKomplett", blNummernformen.formatiereNrKomplett(
                rcZutrittsIdent, rcZutrittsIdentNeben, KonstKartenklasse.gastkartennummer, KonstKartenart.erstzugang));
        rpVariablen.fuelleVariable(rpDrucken, "Gast.Anrede", pAnrede);
        rpVariablen.fuelleVariable(rpDrucken, "Gast.AnredeKomplett", pAnredeKomplett);
        rpVariablen.fuelleVariable(rpDrucken, "Gast.Name", pGast.name);
        rpVariablen.fuelleVariable(rpDrucken, "Gast.Vorname", pGast.vorname);
        rpVariablen.fuelleVariable(rpDrucken, "Gast.NameVorname", pGast.kurzName);
        rpVariablen.fuelleVariable(rpDrucken, "Gast.Ort", pGast.ort);
        rpVariablen.fuelleVariable(rpDrucken, "Gast.Kennung", rcKennung);
        rpVariablen.fuelleVariable(rpDrucken, "Gast.KennungAlternativ", rcKennungAlternativ);
        
        rpVariablen.fuelleVariable(rpDrucken, "Gast.Passwort", rcPasswort);
        rpVariablen.fuelleVariable(rpDrucken, "Gast.Insti", Integer.toString(pGast.kommunikationssprache));

        rpVariablen.fuelleVariable(rpDrucken, "Versandadresse.Zeile1", "");
        rpVariablen.fuelleVariable(rpDrucken, "Versandadresse.Zeile2", "");
        rpVariablen.fuelleVariable(rpDrucken, "Versandadresse.Zeile3", "");
        rpVariablen.fuelleVariable(rpDrucken, "Versandadresse.Zeile4", "");
        rpVariablen.fuelleVariable(rpDrucken, "Versandadresse.Zeile5", "");
        rpVariablen.fuelleVariable(rpDrucken, "Versandadresse.Zeile6", "");
        if (pVersandadresse!=null) {
            if (pVersandadresse.length >= 1) {
                rpVariablen.fuelleVariable(rpDrucken, "Versandadresse.Zeile1", pVersandadresse[0]);
            }
            if (pVersandadresse.length >= 2) {
                rpVariablen.fuelleVariable(rpDrucken, "Versandadresse.Zeile2", pVersandadresse[1]);
            }
            if (pVersandadresse.length >= 3) {
                rpVariablen.fuelleVariable(rpDrucken, "Versandadresse.Zeile3", pVersandadresse[2]);
            }
            if (pVersandadresse.length >= 4) {
                rpVariablen.fuelleVariable(rpDrucken, "Versandadresse.Zeile4", pVersandadresse[3]);
            }
            if (pVersandadresse.length >= 5) {
                rpVariablen.fuelleVariable(rpDrucken, "Versandadresse.Zeile5", pVersandadresse[4]);
            }
            if (pVersandadresse.length >= 6) {
                rpVariablen.fuelleVariable(rpDrucken, "Versandadresse.Zeile6", pVersandadresse[5]);
            }
        }

        CaBug.druckeLog("berechtigungsWert="+berechtigungsWert, logDrucken, 10);
        
        
        if (KonstPortalFunktionen.darfFunktion(KonstPortalFunktionen.gast1, this.berechtigungsWert)) {rpVariablen.fuelleVariable(rpDrucken, "istGast1", "1");}else {rpVariablen.fuelleVariable(rpDrucken, "istGast1", "0");}
        if (KonstPortalFunktionen.darfFunktion(KonstPortalFunktionen.gast2, this.berechtigungsWert)) {rpVariablen.fuelleVariable(rpDrucken, "istGast2", "1");}else {rpVariablen.fuelleVariable(rpDrucken, "istGast2", "0");}
        if (KonstPortalFunktionen.darfFunktion(KonstPortalFunktionen.gast3, this.berechtigungsWert)) {rpVariablen.fuelleVariable(rpDrucken, "istGast3", "1");}else {rpVariablen.fuelleVariable(rpDrucken, "istGast3", "0");}
        if (KonstPortalFunktionen.darfFunktion(KonstPortalFunktionen.gast4, this.berechtigungsWert)) {rpVariablen.fuelleVariable(rpDrucken, "istGast4", "1");}else {rpVariablen.fuelleVariable(rpDrucken, "istGast4", "0");}
        if (KonstPortalFunktionen.darfFunktion(KonstPortalFunktionen.gast5, this.berechtigungsWert)) {rpVariablen.fuelleVariable(rpDrucken, "istGast5", "1");}else {rpVariablen.fuelleVariable(rpDrucken, "istGast5", "0");}
        if (KonstPortalFunktionen.darfFunktion(KonstPortalFunktionen.gast6, this.berechtigungsWert)) {rpVariablen.fuelleVariable(rpDrucken, "istGast6", "1");}else {rpVariablen.fuelleVariable(rpDrucken, "istGast6", "0");}
        if (KonstPortalFunktionen.darfFunktion(KonstPortalFunktionen.gast7, this.berechtigungsWert)) {rpVariablen.fuelleVariable(rpDrucken, "istGast7", "1");}else {rpVariablen.fuelleVariable(rpDrucken, "istGast7", "0");}
        if (KonstPortalFunktionen.darfFunktion(KonstPortalFunktionen.gast8, this.berechtigungsWert)) {rpVariablen.fuelleVariable(rpDrucken, "istGast8", "1");}else {rpVariablen.fuelleVariable(rpDrucken, "istGast8", "0");}
        if (KonstPortalFunktionen.darfFunktion(KonstPortalFunktionen.gast9, this.berechtigungsWert)) {
            CaBug.druckeLog("istGast9 wird 1", logDrucken, 10);
            rpVariablen.fuelleVariable(rpDrucken, "istGast9", "1");}else {
                CaBug.druckeLog("istGast9 wird 0", logDrucken, 10);
                rpVariablen.fuelleVariable(rpDrucken, "istGast9", "0");}
        if (KonstPortalFunktionen.darfFunktion(KonstPortalFunktionen.gast10, this.berechtigungsWert)) {rpVariablen.fuelleVariable(rpDrucken, "istGast10", "1");}else {rpVariablen.fuelleVariable(rpDrucken, "istGast10", "0");}
        
        rpDrucken.druckenFormular();
        if (pVersandart != 6) {
            rpDrucken.endeFormular();
        }

        return 0;

    }

    public void druckenEnde() {
        rpDrucken.endeFormular();
    }

    
    
    /**Ausdrucken - nur anhand von pGast, immer in PDF
     *
     *  pGast
     *  rcNamePDF
     * Verwendete Variablen aus dieser Klasse:
     *  pVersandart (zusammen mit pGastEKFormular) = Auswahl des Formulars, das verwendet wird
     *  pVersandadresse[]
     *  pAnrede
     *  pAnredeKomplett
     * 
     * Variablen, die gefüllt werden:
     *  rcHinweisPapierformat
     * 
     * Returncode <0 => fehler, ansonsten ok.
     * @return
     */
    public int druckenWiederholen(DbBundle pDbBundle) {
        /*Gastkarten-PDF erzeugen*/

        rpDrucken = new RpDrucken();

        rpDrucken.initServer();

        rpDrucken.exportFormat = 1;
        rpDrucken.exportVerzeichnis = pDbBundle.lieferePfadMeetingAusdrucke();
        String hName="";
        if (hName.isEmpty()) {hName="gastkarteMW";}
        rpDrucken.exportDatei = hName + lDbBundle.getMandantString()
        + Integer.toString(pGast.meldungsIdent);
        rcNamePDF=rpDrucken.exportVerzeichnis+"\\"
                + rpDrucken.exportDatei+".pdf";

        rpDrucken.initFormular(pDbBundle);

        /*Variablen füllen - sowie Dokumentvorlage*/
        rpVariablen = new RpVariablen(pDbBundle);
        if (pGastEKFormular == 0) {
            pGastEKFormular = 1;
        }
        String formularNummer = Integer.toString(pGastEKFormular);
        CaBug.druckeLog("pVersandart=" + pVersandart, logDrucken, 10);
        if (pVersandart == 3) {
            rpVariablen.gastkarteSelbstdruck(formularNummer, rpDrucken);
        }
        if (pVersandart == 4) {
            rpVariablen.gastkarteMail(formularNummer, rpDrucken);
        }
        if (pVersandart == 5) {
            rpVariablen.gastkarteDruckEmittent(formularNummer, rpDrucken);
        }
        if (pVersandart == 6) {
            rpVariablen.gastkarteDruckEmittent(formularNummer, rpDrucken);
        }

        rpDrucken.startFormular();

        rcHinweisPapierformat = ""; /*ehemals:=RpZugeordnetesFormular.papierformular;*/
        /*TODO _Konsolidierung: Gastkarte Drucken, Papierformat vorsehen!*/
        CaBug.druckeLog("rpDrucken.dateinameLLQuelle=" + rpDrucken.dateinameLLQuelle, logDrucken, 2);

        String hDatum = CaDatumZeit.DatumStringFuerAnzeigeMonatAusgeschrieben();
        rpVariablen.fuelleVariable(rpDrucken, "Allgemein.Datum", hDatum);
        rpVariablen.fuelleVariable(rpDrucken, "Allgemein.Gesellschaft", lDbBundle.eclEmittent.bezeichnungLang);

        BlNummernformen blNummernformen = new BlNummernformen(lDbBundle);
        rpVariablen.fuelleVariable(rpDrucken, "Nummern.ZutrittsIdent",
                BlZutrittsIdent.aufbereitenFuerAnzeige(pGast.zutrittsIdent, "00"));
        rpVariablen.fuelleVariable(rpDrucken, "Nummern.ZutrittsIdentKomplett", blNummernformen.formatiereNrKomplett(
                pGast.zutrittsIdent, "00", KonstKartenklasse.gastkartennummer, KonstKartenart.erstzugang));
        pVersandadresse=null;
        erzeugeVersandadresse();
        rpVariablen.fuelleVariable(rpDrucken, "Gast.Anrede", pAnrede);
        rpVariablen.fuelleVariable(rpDrucken, "Gast.AnredeKomplett", pAnredeKomplett);
        rpVariablen.fuelleVariable(rpDrucken, "Gast.Name", pGast.name);
        rpVariablen.fuelleVariable(rpDrucken, "Gast.Vorname", pGast.vorname);
        rpVariablen.fuelleVariable(rpDrucken, "Gast.NameVorname", pGast.kurzName);
        rpVariablen.fuelleVariable(rpDrucken, "Gast.Ort", pGast.ort);
//        rpVariablen.fuelleVariable(rpDrucken, "Gast.Kennung", rcKennung);
//        rpVariablen.fuelleVariable(rpDrucken, "Gast.KennungAlternativ", rcKennungAlternativ);
//        
//        rpVariablen.fuelleVariable(rpDrucken, "Gast.Passwort", rcPasswort);
//        rpVariablen.fuelleVariable(rpDrucken, "Gast.Insti", Integer.toString(pGast.kommunikationssprache));

        rpVariablen.fuelleVariable(rpDrucken, "Versandadresse.Zeile1", "");
        rpVariablen.fuelleVariable(rpDrucken, "Versandadresse.Zeile2", "");
        rpVariablen.fuelleVariable(rpDrucken, "Versandadresse.Zeile3", "");
        rpVariablen.fuelleVariable(rpDrucken, "Versandadresse.Zeile4", "");
        rpVariablen.fuelleVariable(rpDrucken, "Versandadresse.Zeile5", "");
        rpVariablen.fuelleVariable(rpDrucken, "Versandadresse.Zeile6", "");
        if (pVersandadresse!=null) {
            if (pVersandadresse.length >= 1) {
                rpVariablen.fuelleVariable(rpDrucken, "Versandadresse.Zeile1", pVersandadresse[0]);
            }
            if (pVersandadresse.length >= 2) {
                rpVariablen.fuelleVariable(rpDrucken, "Versandadresse.Zeile2", pVersandadresse[1]);
            }
            if (pVersandadresse.length >= 3) {
                rpVariablen.fuelleVariable(rpDrucken, "Versandadresse.Zeile3", pVersandadresse[2]);
            }
            if (pVersandadresse.length >= 4) {
                rpVariablen.fuelleVariable(rpDrucken, "Versandadresse.Zeile4", pVersandadresse[3]);
            }
            if (pVersandadresse.length >= 5) {
                rpVariablen.fuelleVariable(rpDrucken, "Versandadresse.Zeile5", pVersandadresse[4]);
            }
            if (pVersandadresse.length >= 6) {
                rpVariablen.fuelleVariable(rpDrucken, "Versandadresse.Zeile6", pVersandadresse[5]);
            }
        }

//        CaBug.druckeLog("berechtigungsWert="+berechtigungsWert, logDrucken, 10);
//        
//        
//        if (KonstPortalFunktionen.darfFunktion(KonstPortalFunktionen.gast1, this.berechtigungsWert)) {rpVariablen.fuelleVariable(rpDrucken, "istGast1", "1");}else {rpVariablen.fuelleVariable(rpDrucken, "istGast1", "0");}
//        if (KonstPortalFunktionen.darfFunktion(KonstPortalFunktionen.gast2, this.berechtigungsWert)) {rpVariablen.fuelleVariable(rpDrucken, "istGast2", "1");}else {rpVariablen.fuelleVariable(rpDrucken, "istGast2", "0");}
//        if (KonstPortalFunktionen.darfFunktion(KonstPortalFunktionen.gast3, this.berechtigungsWert)) {rpVariablen.fuelleVariable(rpDrucken, "istGast3", "1");}else {rpVariablen.fuelleVariable(rpDrucken, "istGast3", "0");}
//        if (KonstPortalFunktionen.darfFunktion(KonstPortalFunktionen.gast4, this.berechtigungsWert)) {rpVariablen.fuelleVariable(rpDrucken, "istGast4", "1");}else {rpVariablen.fuelleVariable(rpDrucken, "istGast4", "0");}
//        if (KonstPortalFunktionen.darfFunktion(KonstPortalFunktionen.gast5, this.berechtigungsWert)) {rpVariablen.fuelleVariable(rpDrucken, "istGast5", "1");}else {rpVariablen.fuelleVariable(rpDrucken, "istGast5", "0");}
//        if (KonstPortalFunktionen.darfFunktion(KonstPortalFunktionen.gast6, this.berechtigungsWert)) {rpVariablen.fuelleVariable(rpDrucken, "istGast6", "1");}else {rpVariablen.fuelleVariable(rpDrucken, "istGast6", "0");}
//        if (KonstPortalFunktionen.darfFunktion(KonstPortalFunktionen.gast7, this.berechtigungsWert)) {rpVariablen.fuelleVariable(rpDrucken, "istGast7", "1");}else {rpVariablen.fuelleVariable(rpDrucken, "istGast7", "0");}
//        if (KonstPortalFunktionen.darfFunktion(KonstPortalFunktionen.gast8, this.berechtigungsWert)) {rpVariablen.fuelleVariable(rpDrucken, "istGast8", "1");}else {rpVariablen.fuelleVariable(rpDrucken, "istGast8", "0");}
//        if (KonstPortalFunktionen.darfFunktion(KonstPortalFunktionen.gast9, this.berechtigungsWert)) {
//            CaBug.druckeLog("istGast9 wird 1", logDrucken, 10);
//            rpVariablen.fuelleVariable(rpDrucken, "istGast9", "1");}else {
//                CaBug.druckeLog("istGast9 wird 0", logDrucken, 10);
//                rpVariablen.fuelleVariable(rpDrucken, "istGast9", "0");}
//        if (KonstPortalFunktionen.darfFunktion(KonstPortalFunktionen.gast10, this.berechtigungsWert)) {rpVariablen.fuelleVariable(rpDrucken, "istGast10", "1");}else {rpVariablen.fuelleVariable(rpDrucken, "istGast10", "0");}
        
        rpDrucken.druckenFormular();
        rpDrucken.endeFormular();

        return 0;

    }

    
    
    /**Füllt pVersandadresse aus pGast*/
    private void erzeugeVersandadresse() {
        /*Versandadresse*/
        if (pVersandadresse == null && pVersandAnAdresseAusAktienregister == 0) {
            /*Versandadresse aus Gastdaten zusammensetzen*/
            pVersandadresse = new String[6];
            int zeile;
            for (int ii = 0; ii < 6; ii++) {
                pVersandadresse[ii] = "";
            }
            zeile = 0;
            /*Anrede (optional)*/
            if (pGast.anrede != 0) {
                DbAnreden lDbAnreden = new DbAnreden(lDbBundle);
                int erg = lDbAnreden.ReadAnrede_Anredennr(pGast.anrede);
                if (erg < 1) {
                    CaBug.drucke("BlGastakrte.neueGKNr 001");
                }
                pVersandadresse[zeile] = lDbAnreden.anredenreadarray[0].anredentext;
                zeile++;
            }
            /*Adelstitel, Titel, Vorname, Name (fest)*/
            if (!pGast.adelstitel.isEmpty()) {
                pVersandadresse[zeile] = pVersandadresse[zeile] + pGast.adelstitel + " ";
            }
            if (!pGast.titel.isEmpty()) {
                pVersandadresse[zeile] = pVersandadresse[zeile] + pGast.titel + " ";
            }
            if (!pGast.vorname.isEmpty()) {
                pVersandadresse[zeile] = pVersandadresse[zeile] + pGast.vorname + " ";
            }
            pVersandadresse[zeile] = pVersandadresse[zeile] + pGast.name;
            zeile++;
            /*zuHaendenBezeichnung optional*/
            if (!pGast.zuHdCo.isEmpty()) {
                pVersandadresse[zeile] = pGast.zuHdCo;
                zeile++;
            }
            /*Zusatz 1 optional*/
            if (!pGast.zusatz1.isEmpty()) {
                pVersandadresse[zeile] = pGast.zusatz1;
                zeile++;
            }
            /*Straße (optional)*/
            if (!pGast.strasse.isEmpty()) {
                pVersandadresse[zeile] = pGast.strasse;
                zeile++;
            }
            /*Land PLZ Ort (Fest)*/
            if (!pGast.land.isEmpty()) {
                pVersandadresse[zeile] = pVersandadresse[zeile] + pGast.land + " ";
            }
            if (!pGast.plz.isEmpty()) {
                pVersandadresse[zeile] = pVersandadresse[zeile] + pGast.plz + " ";
            }
            pVersandadresse[zeile] = pVersandadresse[zeile] + pGast.ort + " ";
            zeile++;
        }

        if (pVersandAnAdresseAusAktienregister == 1) {
            /*Versandadresse aus Aktienregister zusammensetzen*/
            pVersandadresse = new String[5];
            for (int ii = 0; ii < 5; ii++) {
                pVersandadresse[ii] = "";
            }
            pVersandadresse[0] = "Versand an im Aktienregister hinterlegte";
            pVersandadresse[1] = pAktienregisterEintrag.vorname + pAktienregisterEintrag.nachname;
            pVersandadresse[2] = pAktienregisterEintrag.ort;
            pVersandadresse[3] = "Noch Vervollständigen";
        }

    }

    /*
     * pMeldungsIdentGast muß gefüllt sein.
     * pFolgeFuerWillenserklaerung kann 0 sein
     * 
     * Wertet außerdem aus:
     * pZutrittsIdent
     * pWillenserklaerungGeberIdent
     * pVersandadresse
     * pVersandAnAdresseAusAktienregister (0=nein, 1=ja an die hinterlegte Adresse - aber noch nicht fertig!!)
     * pGast
     * pVersandart
     * 
     * Füllt:
     * rcZutrittsIdent
     * rcZutrittsIdentNeben
     * rcGastkartePdfNr (bei entsprechender Versandart, 3, 4, 5)
     */
    public int neueGKNr(int pMeldungsIdentGast, int pFolgeFuerWillenserklaerung) {
        int rc = 1;
        /****Willenserklärung für ZutrittsIdent**********************/
        BlWillenserklaerung ekWillenserklaerungGast = new BlWillenserklaerung();
        ekWillenserklaerungGast.piMeldungsIdentGast = pMeldungsIdentGast;
        ekWillenserklaerungGast.pFolgeFuerWillenserklaerungIdent = pFolgeFuerWillenserklaerung;
        ekWillenserklaerungGast.pWillenserklaerungGeberIdent = pWillenserklaerungGeberIdent;

        erzeugeVersandadresse();

        if (pVersandadresse.length >= 1) {
            ekWillenserklaerungGast.pVersandadresse1 = pVersandadresse[0];
        }
        if (pVersandadresse.length >= 2) {
            ekWillenserklaerungGast.pVersandadresse2 = pVersandadresse[1];
        }
        if (pVersandadresse.length >= 3) {
            ekWillenserklaerungGast.pVersandadresse3 = pVersandadresse[2];
        }
        if (pVersandadresse.length >= 4) {
            ekWillenserklaerungGast.pVersandadresse4 = pVersandadresse[3];
        }
        if (pVersandadresse.length >= 5) {
            ekWillenserklaerungGast.pVersandadresse5 = pVersandadresse[4];
        }

        /*Versandart*/
        switch (pVersandart) {
        case 2:/*Aufnahme in Sammelbatch, bei nächstem Drucklauf ausdrucken und versenden*/
            ekWillenserklaerungGast.pVersandartEK = 2;
            break;
        case 3:/*Online-Ausdruck (im Portal) erfolgt*/
            ekWillenserklaerungGast.pVersandartEK = 3;
            break;
        case 4:/*Versand per Email (im Portal) erfolgt*/
            ekWillenserklaerungGast.pVersandartEK = 4;
            ekWillenserklaerungGast.pEmailAdresseEK = pGast.mailadresse;
            break;
        }

        if (!pZutrittsIdent.isEmpty()) {
            ekWillenserklaerungGast.pZutrittsIdent.zutrittsIdent = pZutrittsIdent;
            ekWillenserklaerungGast.pZutrittsIdent.zutrittsIdentNeben = "00";
        }
        
        ekWillenserklaerungGast.neueZutrittsIdentZuMeldung(lDbBundle);
        if (ekWillenserklaerungGast.rcIstZulaessig == false) {
            lDbBundle.dbBasis.endTransaction();
            return ekWillenserklaerungGast.rcGrundFuerUnzulaessig;
        }

        rcZutrittsIdent = ekWillenserklaerungGast.pZutrittsIdent.zutrittsIdent;
        //		System.out.println("rcZutrittsIdent="+rcZutrittsIdent);
        rcZutrittsIdentNeben = ekWillenserklaerungGast.pZutrittsIdent.zutrittsIdentNeben;
        //		System.out.println("rcZutrittsIdentNeben="+rcZutrittsIdentNeben);
        if (pVersandart == 3 || pVersandart == 4 || pVersandart == 5) {
            rcGastkartePdfNr = ekWillenserklaerungGast.rcWillenserklaerungIdent;
        }

        return rc;
    }

    
    public boolean pruefeObGKNrVorhandenUndGueltig() {
        EclZutrittsIdent lZutrittsIdent=new EclZutrittsIdent();
        lZutrittsIdent.zutrittsIdent=pZutrittsIdentStorno;
        lZutrittsIdent.zutrittsIdentNeben=pZutrittsIdentNebenStorno;
        lDbBundle.dbZutrittskarten.readGast(lZutrittsIdent, 1);
        if (lDbBundle.dbZutrittskarten.anzErgebnis()<1) {return false;}
        EclZutrittskarten lZutrittskarte=lDbBundle.dbZutrittskarten.ergebnisPosition(0);
        if (lZutrittskarte.zutrittsIdentWurdeGesperrt()) {return false;}
        return true;
    }
    
    /**Stornieren der Gastkartennummer pZutrittsIdentStorno, pZutrittsIdentNebenStorno*/
    public int stornoGKNr() {
        int rc = 1;

        BlWillenserklaerung willenserklaerung = new BlWillenserklaerung();
        willenserklaerung.piZutrittsIdent.zutrittsIdent = pZutrittsIdentStorno;
        willenserklaerung.piZutrittsIdent.zutrittsIdentNeben = pZutrittsIdentNebenStorno;
        willenserklaerung.piKlasse = 0;

        willenserklaerung.sperrenZutrittsIdent(lDbBundle);

        /*Falls nicht möglich: Fehlermeldung, zum Anmelden*/
        if (willenserklaerung.rcIstZulaessig == false) {
            rc = willenserklaerung.rcGrundFuerUnzulaessig;
        }

        return rc;
    }

    private EclMeldung bisherigeMeldung = null;

    /**Zum Gastkarten ändern:
     * Variante 1 (für "Thick-Client-Systeme"):
     * > Gastkarte mit aendernInit einlesen und merken. 
     * > Darauf Änderungen durchführen.
     * > Zurückspeichern
     * 
     * Variante 2 (Für EE-Systeme, Statuslos):
     * > Gastkarte einlesen (kann mit aendernInit sein, muß aber nicht)
     * > Benutzereingaben durchführen
     * > Gastkarte mit aendernInit erneut einlesen. Geänderte Werte reinspeichern, außerdem auch dbVersion!!
     * > aendernSpeichern
     * 
     * Returnwert "null", wenn pMeldeIdent nicht existiert.
     */
    public EclMeldung aendernInit(int pMeldeIdent) {
        EclMeldung returnMeldung = null;
        lDbBundle.dbMeldungen.leseZuIdent(pMeldeIdent);
        if (lDbBundle.dbMeldungen.meldungenArray.length == 0) {
            bisherigeMeldung = null;
        } else {
            bisherigeMeldung = new EclMeldung();
            returnMeldung = lDbBundle.dbMeldungen.meldungenArray[0];
            returnMeldung.copyTo(bisherigeMeldung);
        }
        return returnMeldung;
    }

    /**Return-Wert:
     * 0 = keine Speicherung erfolgt, da keine Veränderung
     * rcGastkartePdfNr: ggf. erzeugtes PDF für Gastkartendruck
     */
    public int aendernSpeichern(EclMeldung pVeraenderteMeldung, boolean pDrucken, int versandart,
            EclZutrittsIdent pZutrittsIdent) {
        int rc = 1;
        if (pVeraenderteMeldung.equalsTo(bisherigeMeldung)) {
            rc = 0; //Keine Veränderung
        } else {
            lDbBundle.dbMeldungen.update_MitLog(pVeraenderteMeldung, bisherigeMeldung);
        }
        if (pDrucken) {
            /*rcGastkartePdfNr - Willenserklärung für Ausdruck suchen*/
            BlWillenserklaerungStatus blWillenserklaerungStatus = new BlWillenserklaerungStatus(lDbBundle);
            rcGastkartePdfNr = blWillenserklaerungStatus.ermittleWKNummerZuMeldungZutrittsIdent(0,
                    pVeraenderteMeldung.meldungsIdent, pZutrittsIdent);

            this.pVersandart = versandart;

            rcZutrittsIdent = pZutrittsIdent.zutrittsIdent;
            rcZutrittsIdentNeben = pZutrittsIdent.zutrittsIdentNeben;

            pGast = pVeraenderteMeldung;

            erzeugeVersandadresse();

            drucken(lDbBundle);

        }
        return rc;
    }

    /**Voraussetzung: Gastkarte wurde unmittelbar vorher mit 
     * ausstellen() erzeugt.
     */
    public int ordneZuAktionaerZu(int pAktionaersnummer) {
       EclMeldungenMeldungen lMeldungenMeldung=new EclMeldungenMeldungen();
       lMeldungenMeldung.mandant=lDbBundle.clGlobalVar.mandant;
       lMeldungenMeldung.vonMeldungsIdent=pAktionaersnummer;
       lMeldungenMeldung.zuMeldungsIdent=pGast.meldungsIdent;
       lMeldungenMeldung.verwendung=2;
       lDbBundle.dbMeldungenMeldungen.insert(lMeldungenMeldung);
        return 1;
    }
    
    
     
    
    /*+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
    public String rcPasswortFuerMail = "";
    public String rcMailEmpfaenger = "";

    /**
     * TODO derzeit nur versandart=4 zulässig*/
    public int neuesPasswortGenerieren(String pKennungGast, boolean pDrucken, int versandart) {
        lDbBundle.dbLoginDaten.read_loginKennung(pKennungGast);
        EclLoginDaten lLoginDaten = lDbBundle.dbLoginDaten.ergebnisPosition(0);

        CaPasswortErzeugen caPasswortErzeugen = new CaPasswortErzeugen();
        String ergebnisPasswort = caPasswortErzeugen.generatePW(lDbBundle.param.paramPortal.passwortMindestLaenge, 1, 1,
                false); //"PW";
        lLoginDaten.passwortVerschluesselt = CaPasswortVerschluesseln.verschluesseln(ergebnisPasswort);
        lLoginDaten.passwortInitial = ergebnisPasswort + ergebnisPasswort + ergebnisPasswort;

        lDbBundle.dbLoginDaten.update(lLoginDaten);

        rcPasswortFuerMail = ergebnisPasswort;
        rcMailEmpfaenger = lLoginDaten.eMailFuerVersand;

        return 1;
    }
}
