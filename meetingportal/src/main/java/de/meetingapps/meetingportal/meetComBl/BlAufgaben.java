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

import de.meetingapps.meetingportal.meetComAllg.CaPasswortErzeugen;
import de.meetingapps.meetingportal.meetComAllg.CaPasswortVerschluesseln;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclAktienregister;
import de.meetingapps.meetingportal.meetComEntities.EclAufgaben;
import de.meetingapps.meetingportal.meetComEntities.EclLoginDaten;
import de.meetingapps.meetingportal.meetComEntities.EclPersonenNatJur;
import de.meetingapps.meetingportal.meetComKonst.KonstAufgabenErledigt;
import de.meetingapps.meetingportal.meetComKonst.KonstAufgabenStatus;
import de.meetingapps.meetingportal.meetComKonst.KonstVerarbeitungslaufArt;
import de.meetingapps.meetingportal.meetComStub.StubParameter;
import de.meetingapps.meetingportal.meetComStub.StubRoot;
import de.meetingapps.meetingportal.meetComStub.WEStubBlAufgaben;
import de.meetingapps.meetingportal.meetComStub.WEStubBlAufgabenRC;
import de.meetingapps.meetingportal.meetComWE.WELoginVerify;

/*TODO BlAufgaben: Achtung - Passwort-Vergessen-Prozesse per Post geht nicht, wenn zwei Portale gleichzeitig für den selben
 * Mandant laufen!
 */

public class BlAufgaben extends StubRoot {

    public List<EclAufgaben> rcAufgabenListe = null;
    public List<EclAktienregister> rcAktienregisterListe = null;
    public List<EclPersonenNatJur> rcPersonNatJurListe = null;
    public List<String> rcPasswortListe = null;
    public List<String> rcKennungListe = null;

    public List<Integer> rcMandantListe = null;
    public List<Integer> rcHvJahrListe = null;
    public List<String> rcHvNummer = null;
    public List<String> rcDatenbereich = null;
    public List<String> rcEmittent = null;
    
    public List<Integer> rcFehlercode=null;

    public BlAufgaben(boolean pIstServer, DbBundle pDbBundle) {
        super(pIstServer, pDbBundle);
    }

    /******************************Passwort-Papierversand***********************************************/

    /**Grundsätzlicher Ablauf:
     * 
     * Schritt 1: Prüfen
     * Alle Aufgaben aktionaerNeuesPasswortAdressePruefen und status ==gestellt einlesen. Sortiert nach Mandanten und parameter0
     * 
     * Aufbereiten:
     * > Doppelte: die ersten mit status=nichtDurchfuerbar und nichtDurchgefuehrtDoppelteAnforderung zurückschreiben
     * > Alle, die zu einem Mandant gehören, wo keine Prüfung erforderlich ist => status=Geprüft
     * > Rest in "zu prüfende-Liste" schreiben.
     * 
     * Prüfen:
     * > Ergebnis 
     * 		entweder nichtDurchfuehrbar/nichtMoeglichFalscheAngaben oder nichtMoeglichWgNullBestand
     * 	Oder:
     * 		status=geprueft
     * 
     * Ausdrucken:
     * > Alle aktionaerNeuesPasswortAdressePruefen und status= geprueft, und alle mit aktionaerNeuesPasswort und status=geprueft
     * > Zurückschreiben mit status=erledigt
     * @return
     */

    /*1*/
    /**Belegt:
     * rcAufgabenListe = Liste zum Prüfen
     * rcAktienregisterListe = Liste mit zugehörigen Aktionärsdaten
     */
    public int pw_vorbereitenPruefen() {

        if (verwendeWebService()) {
            WEStubBlAufgaben weStubBlAufgaben = new WEStubBlAufgaben();
            weStubBlAufgaben.stubFunktion = 1;

            WELoginVerify weLoginVerify = new WELoginVerify();
            weStubBlAufgaben.setWeLoginVerify(weLoginVerify);

            WEStubBlAufgabenRC weStubBlAufgabenRC = wsClient.stubBlAufgaben(weStubBlAufgaben);

            if (weStubBlAufgabenRC.rc < 1) {
                return weStubBlAufgabenRC.rc;
            }

            rcAufgabenListe = weStubBlAufgabenRC.rcAufgabenListe;
            rcAktienregisterListe = weStubBlAufgabenRC.rcAktienregisterListe;
            rcPersonNatJurListe = weStubBlAufgabenRC.rcPersonNatJurListe;

            return weStubBlAufgabenRC.rc;
        }

        dbOpenUndWeitere();

        rcAufgabenListe = new LinkedList<EclAufgaben>();
        rcAktienregisterListe = new LinkedList<EclAktienregister>();
        rcPersonNatJurListe = new LinkedList<EclPersonenNatJur>();

        StubParameter stubParameter = new StubParameter(true, lDbBundle);
        stubParameter.speichereMandantAktuell();

        lDbBundle.dbAufgaben.read_passwortPruefen();
        int anz = lDbBundle.dbAufgaben.anzErgebnis();

        int letzterMandant = 0;
        EclAufgaben letzteAufgabe = null;

        for (int i = 0; i < anz; i++) {
            EclAufgaben lAufgabe = lDbBundle.dbAufgaben.ergebnisPosition(i);
            int hMandant = lAufgabe.mandant;
            if (hMandant != letzterMandant) {
                if (letzteAufgabe != null) {
                    verarbeiteLetzteAufgabe(letzteAufgabe);
                    letzteAufgabe = null;
                }

                /**Mandant einlesen*/
                stubParameter.belegeStandardMandant(hMandant);
                letzterMandant = hMandant;
            }

            if (letzteAufgabe != null) {
                if (letzteAufgabe.argument[0].compareTo(lAufgabe.argument[0]) == 0) {
                    setzeAufgabeAufAbgelehntWgDoppelt(letzteAufgabe,
                            KonstAufgabenErledigt.nichtDurchgefuehrtDoppelteAnforderung);
                    letzteAufgabe = lAufgabe;
                } else {
                    verarbeiteLetzteAufgabe(letzteAufgabe);
                    letzteAufgabe = lAufgabe;
                }
            } else {
                letzteAufgabe = lAufgabe;
            }

        }
        if (letzteAufgabe != null) {
            verarbeiteLetzteAufgabe(letzteAufgabe);
        }

        stubParameter.restoreMandantAktuell();

        dbClose();
        return 1;
    }

    private void verarbeiteLetzteAufgabe(EclAufgaben pAufgabe) {
        if (lDbBundle.param.paramPortal.passwortPerPostPruefen == 1) {

            String aktionaersnummer = pAufgabe.argument[0];
            if (aktionaersnummer.startsWith("S")) {/*Sonstige Kennung*/
                lDbBundle.dbLoginDaten.read_loginKennung(aktionaersnummer);
                EclLoginDaten lLoginDaten = lDbBundle.dbLoginDaten.ergebnisPosition(0);

                int personNatJurIdent = lLoginDaten.personenNatJurIdent;
                lDbBundle.dbPersonenNatJur.read(personNatJurIdent);
                EclPersonenNatJur lPersonenNatJur = lDbBundle.dbPersonenNatJur.PersonNatJurGefunden(0);
                rcAktienregisterListe.add(null);
                rcAufgabenListe.add(pAufgabe);
                rcPersonNatJurListe.add(lPersonenNatJur);
            }
            else {/*Aktionär*/
                if (aktionaersnummer.length() <= 10 && lDbBundle.param.paramBasis.namensaktienAktiv) {
                    aktionaersnummer = aktionaersnummer + "0";
                }
                EclAktienregister lAktienregister = new EclAktienregister();
                lAktienregister.aktionaersnummer = aktionaersnummer;
                lDbBundle.dbAktienregister.leseZuAktienregisterEintrag(lAktienregister);
                if (lDbBundle.dbAktienregister.anzErgebnis() > 0) {
                    lAktienregister = lDbBundle.dbAktienregister.ergebnisPosition(0);
                }
                if (lAktienregister.stueckAktien > 0) {
                    rcAktienregisterListe.add(lAktienregister);
                    rcAufgabenListe.add(pAufgabe);
                    rcPersonNatJurListe.add(null);
                } else {
                    setzeAufgabeAufAbgelehntWgDoppelt(pAufgabe, KonstAufgabenErledigt.nichtMoeglichWgNullBestand);
                }
            }
        } else {
            pAufgabe.status = KonstAufgabenStatus.geprueft;
            lDbBundle.dbAufgaben.update(pAufgabe);
        }
    }

    private void setzeAufgabeAufAbgelehntWgDoppelt(EclAufgaben pAufgabe, int pVermerk) {
        pAufgabe.status = KonstAufgabenStatus.nichtDurchfuehrbar;
        pAufgabe.erledigtVermerk = pVermerk;
        lDbBundle.dbAufgaben.update(pAufgabe);

    }

    /**Verändert rcAufgabenListe - kein Datenzugriff*/
    public int pw_setzeAufAbgelehnt(int pOffset) {
        rcAufgabenListe.get(pOffset).status = KonstAufgabenStatus.nichtDurchfuehrbar;
        rcAufgabenListe.get(pOffset).erledigtVermerk = KonstAufgabenErledigt.nichtMoeglichFalscheAngaben;
        return 1;
    }

    /**Verändert rcAufgabenListe - kein Datenzugriff*/
    public int pw_setzeAufGeprueft(int pOffset) {
        rcAufgabenListe.get(pOffset).status = KonstAufgabenStatus.geprueft;
        return 1;
    }

    /*3*/
    /**Verwendet:rcAufgabenListe*/
    public int pw_speicherePruefergebnis() {

        if (verwendeWebService()) {
            WEStubBlAufgaben weStubBlAufgaben = new WEStubBlAufgaben();
            weStubBlAufgaben.stubFunktion = 3;

            WELoginVerify weLoginVerify = new WELoginVerify();
            weStubBlAufgaben.setWeLoginVerify(weLoginVerify);

            weStubBlAufgaben.rcAufgabenListe = rcAufgabenListe;

            WEStubBlAufgabenRC weStubBlAufgabenRC = wsClient.stubBlAufgaben(weStubBlAufgaben);

            if (weStubBlAufgabenRC.rc < 1) {
                return weStubBlAufgabenRC.rc;
            }
            return weStubBlAufgabenRC.rc;
        }

        dbOpenUndWeitere();
        for (int i = 0; i < rcAufgabenListe.size(); i++) {
            lDbBundle.dbAufgaben.update(rcAufgabenListe.get(i));
        }
        dbClose();
        return 1;
    }

    /*2*/
    /**Erzeugt neuen Drucklauf (pDrucklauf==0) oder verwendet
     * bestehenden Drucklauf.
     * Update Status.
     * Stellt bereit:
     * rcAufgabenListe
     * rcAktienregisterListe
     * rcPersonNatJurListe
     * rcPasswortListe
     * rcMandantListe
     * rcHvJahrListe
     * rcHvNummer
     * rcDatenbereich
     * rcEmittent
     */
    public int pw_aufbereitenDrucklauf(int pDrucklauf, int pArbeitsplatznr, int pBenutzernr) {

        if (verwendeWebService()) {
            WEStubBlAufgaben weStubBlAufgaben = new WEStubBlAufgaben();
            weStubBlAufgaben.stubFunktion = 2;

            WELoginVerify weLoginVerify = new WELoginVerify();
            weStubBlAufgaben.setWeLoginVerify(weLoginVerify);

            weStubBlAufgaben.pDrucklauf = pDrucklauf;
            weStubBlAufgaben.pArbeitsplatznr = pArbeitsplatznr;
            weStubBlAufgaben.pBenutzernr = pBenutzernr;

            WEStubBlAufgabenRC weStubBlAufgabenRC = wsClient.stubBlAufgaben(weStubBlAufgaben);

            if (weStubBlAufgabenRC.rc < 1) {
                return weStubBlAufgabenRC.rc;
            }

            rcAufgabenListe = weStubBlAufgabenRC.rcAufgabenListe;
            rcAktienregisterListe = weStubBlAufgabenRC.rcAktienregisterListe;
            rcPersonNatJurListe = weStubBlAufgabenRC.rcPersonNatJurListe;
            rcPasswortListe = weStubBlAufgabenRC.rcPasswortListe;
            rcKennungListe = weStubBlAufgabenRC.rcKennungListe;

            rcMandantListe = weStubBlAufgabenRC.rcMandantListe;
            rcHvJahrListe = weStubBlAufgabenRC.rcHvJahrListe;
            rcHvNummer = weStubBlAufgabenRC.rcHvNummer;
            rcDatenbereich = weStubBlAufgabenRC.rcDatenbereich;
            rcEmittent = weStubBlAufgabenRC.rcEmittent;

            rcFehlercode = weStubBlAufgabenRC.rcFehlercode;

            return weStubBlAufgabenRC.rc;
        }

        dbOpenUndWeitere();
        /*Ggf. neuen Drucklauf erzeugen*/
        if (pDrucklauf == 0) {
            BlDrucklauf blDrucklauf = new BlDrucklauf(true, lDbBundle);
            blDrucklauf.erzeugeNeuenDrucklauf(pArbeitsplatznr, pBenutzernr, KonstVerarbeitungslaufArt.neuesPasswort, 0, 2);
            pDrucklauf = blDrucklauf.rcDrucklauf.drucklaufNr;

            int anz = lDbBundle.dbAufgaben.update_passwortPruefenDrucken(pDrucklauf);
            blDrucklauf.updateAnzDrucklauf(anz, 2);
        }

        /*Daten zum Verarbeiten einlesen / aufbereiten / neues Passwort erzeugen*/
        lDbBundle.dbAufgaben.read_passwortVerarbeiten(pDrucklauf);

        rcAufgabenListe = new LinkedList<EclAufgaben>();
        rcAktienregisterListe = new LinkedList<EclAktienregister>();
        rcPersonNatJurListe = new LinkedList<EclPersonenNatJur>();
        rcPasswortListe = new LinkedList<String>();
        rcKennungListe = new LinkedList<String>();

        rcMandantListe = new LinkedList<Integer>();
        rcHvJahrListe = new LinkedList<Integer>();
        rcHvNummer = new LinkedList<String>();
        rcDatenbereich = new LinkedList<String>();
        rcEmittent = new LinkedList<String>();

        rcFehlercode = new LinkedList<Integer>();

        StubParameter stubParameter = new StubParameter(true, lDbBundle);
        stubParameter.speichereMandantAktuell();

        int letzterMandant = 0;
        int mandantVorhanden=0;
        int anz = lDbBundle.dbAufgaben.anzErgebnis();
        for (int i = 0; i < anz; i++) {
            EclAufgaben lAufgabe = lDbBundle.dbAufgaben.ergebnisPosition(i);
            int hMandant = lAufgabe.mandant;
            if (hMandant != letzterMandant) {
                /**Mandant einlesen*/
                mandantVorhanden=stubParameter.belegeStandardMandant(hMandant);
                letzterMandant = hMandant;
            }

            if (mandantVorhanden!=-1) {
                int rc1=0;

                EclLoginDaten lLoginDaten = null;
                String aktionaersnummer = lAufgabe.argument[0];
                if (aktionaersnummer.startsWith("S")) {
                    /*Gast*/
                    lDbBundle.dbLoginDaten.read_loginKennung(aktionaersnummer);
                    if (lDbBundle.dbLoginDaten.anzErgebnis()<1) {rc1=-1;}
                    if (rc1!=-1) {
                        lLoginDaten = lDbBundle.dbLoginDaten.ergebnisPosition(0);

                        int personNatJurIdent = lLoginDaten.personenNatJurIdent;
                        lDbBundle.dbPersonenNatJur.read(personNatJurIdent);
                        if (lDbBundle.dbPersonenNatJur.anzPersonenNatJurGefunden()<1) {rc1=-1;}
                        if (rc1!=-1) {
                            EclPersonenNatJur lPersonenNatJur = lDbBundle.dbPersonenNatJur.PersonNatJurGefunden(0);
                            rcAktienregisterListe.add(null);
                            rcPersonNatJurListe.add(lPersonenNatJur);
                            rcKennungListe.add(aktionaersnummer);
                        }
                    }
                } else {
                    /*Aktionär*/
                    if (aktionaersnummer.length() <= 10 && lDbBundle.param.paramBasis.namensaktienAktiv) {
                        aktionaersnummer = aktionaersnummer + "0";
                    }
                    EclAktienregister lAktienregister = new EclAktienregister();
                    lAktienregister.aktionaersnummer = aktionaersnummer;
                    lDbBundle.dbAktienregister.leseZuAktienregisterEintrag(lAktienregister);
                    if (lDbBundle.dbAktienregister.anzErgebnis() > 0) {
                        lAktienregister = lDbBundle.dbAktienregister.ergebnisPosition(0);
                    }
                    else {rc1=-1;}
                    if (rc1!=-1) {

                        lDbBundle.dbLoginDaten.read_aktienregisterIdent(lAktienregister.aktienregisterIdent);
                        if (lDbBundle.dbLoginDaten.anzErgebnis()<1) {rc1=-1;}
                        if (rc1!=-1) {
                            lLoginDaten = lDbBundle.dbLoginDaten.ergebnisPosition(0);
                            String hAktionaersnummerFuerExtern = BlNummernformBasis.aufbereitenInternFuerExtern(aktionaersnummer,
                                    lDbBundle);
                            rcAktienregisterListe.add(lAktienregister);
                            rcPersonNatJurListe.add(null);
                            rcKennungListe.add(hAktionaersnummerFuerExtern);
                        }
                    }

                }

                if (rc1!=-1) {
                    rcAufgabenListe.add(lAufgabe);
                    rcMandantListe.add(stubParameter.rcMandant);
                    rcHvJahrListe.add(stubParameter.rcHvJahr);
                    rcHvNummer.add(stubParameter.rcHvNummer);
                    rcDatenbereich.add(stubParameter.rcDatenbereich);
                    rcEmittent.add(lDbBundle.eclEmittent.bezeichnungLang);

                    String passwort_initial="";
                    // Passwortlänge festlegen (Aktuell bei Namensaktien IMMER auf 8)
                    int passwort_laenge = lDbBundle.param.paramPortal.passwortMindestLaenge;

                    if (lAufgabe.argument[8].isEmpty()) {

                        /*Neues Initial-Passwort generieren*/
                        CaPasswortErzeugen caPasswortErzeugen = new CaPasswortErzeugen();
                        passwort_initial = caPasswortErzeugen.generatePWInitial(passwort_laenge, 1, 1, true);

                    }
                    else {
                        passwort_initial=lAufgabe.argument[8];
                    }
                    // Initialpasswort (nur pppppppp) verschlüsseln -> SHA256 String
                    String passwort_verschluesselt = CaPasswortVerschluesseln.verschluesselnAusInitialPW(passwort_initial,
                            passwort_laenge);
                    lLoginDaten.passwortInitial = passwort_initial;
                    lLoginDaten.passwortVerschluesselt = passwort_verschluesselt;
                    rcPasswortListe.add(passwort_initial.substring(passwort_laenge, passwort_laenge + passwort_laenge));

                    int rcUpdate=lDbBundle.dbLoginDaten.update(lLoginDaten);
                    if (rcUpdate<1) {
                        rcFehlercode.add(-1);
                    }
                    else {
                        rcFehlercode.add(1);
                    }
                }
            }
        }

        stubParameter.restoreMandantAktuell();
        dbClose();
        return 1;
    }

}
