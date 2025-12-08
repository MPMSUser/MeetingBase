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

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaDatumZeit;
import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComAllg.CaPasswortErzeugen;
import de.meetingapps.meetingportal.meetComAllg.CaPasswortVerschluesseln;
import de.meetingapps.meetingportal.meetComAllg.CaString;
import de.meetingapps.meetingportal.meetComBl.BlWillenserklaerung;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclAbstimmung;
import de.meetingapps.meetingportal.meetComEntities.EclAbstimmungenZuStimmkarte;
import de.meetingapps.meetingportal.meetComEntities.EclAktienregister;
import de.meetingapps.meetingportal.meetComEntities.EclAktienregisterErgaenzung;
import de.meetingapps.meetingportal.meetComEntities.EclLoginDaten;
import de.meetingapps.meetingportal.meetComEntities.EclMeldung;
import de.meetingapps.meetingportal.meetComEntities.EclPersonenNatJur;
import de.meetingapps.meetingportal.meetComEntities.EclPublikation;
import de.meetingapps.meetingportal.meetComEntities.EclStimmkarteInhalt;
import de.meetingapps.meetingportal.meetComHVParam.SParamProgramm;
import de.meetingapps.meetingportal.meetComKonst.KonstAktienregisterErgaenzung;
import de.meetingapps.meetingportal.meetComKonst.KonstEingabeQuelle;
import de.meetingapps.meetingportal.meetComKonst.KonstKartenklasse;
import de.meetingapps.meetingportal.meetComKonst.KonstLoginKennungArt;
import de.meetingapps.meetingportal.meetComKonst.KonstWillenserklaerungWeg;

/**Div. Funktionen zum Erzeugen von Testdaten.
 * Voraussetzung: Datenbank ist vorbereitet.
 * 
 * Wichtig: pDbBundle muß offen sein - einschließlich weitere*/

public class BvTestdaten {

    public boolean gruppenVerteilen = false;
    public boolean ergaenzungAnlegen=false;
    
    /**Maximalzahl bestimmt durch miniLaengeNr*/
    public int anzahlAnlegenJeGattung = 200;

    public String passwortVergeben="PW"; //P20!9Z1!R0NE

    public String beschreibung=
    
     "Anlegen von Testfällen:"
     + "<br /><br />"
     + ""
     + "Falls nur Namensaktien aktiv: <br />"
     + "> Aktionärsnummern-Länge wird aus Parameter bestimmt.<br />"
     + "> Daten werden beginnend ab Aktionärsnummer 1 angelegt.<br />"
     + "<br />"
     + "Falls nur Inhaberaktien aktiv:<br />"
     + "> Für jede aktive Gattung werden Aktionäre angelegt und angemeldet<br/>"
     + "> Aktionärsnummer wird wie EK-Nr vergeben<br/>"
     + "> EK-Nr wird vergeben aus Auto-Nummernkreise der jeweiligen Gattung<br/>"
     + "> Es ist darauf zu achten, dass (bei Massenanlage) die Nummernkreise entsprechend ausreichend dimensioniert sind!<br/>"
     + "<br />" 
     + "Falls Inhaberaktien und Namensaktien aktiv:<br />"
     + "> Inhaberaktien wie oben<br />"
     + "> Namensaktien beginnend ab 1000xxx<br />"
     + "<br/>"
     + "Falls Namensaktien aktiv, je 99::<br />"
     + "> 20001xx: normaler Aktionär natürliche Person <br />"
     + "> 20002xx: juristische Person <br />"
     + "> 20003xx: juristische Person mit E und F <br />"
     + "> 20004xx: Personengemeinschaft <br />"
    + "";
    
    /*Weitere Parameter, in Oberfläche einzugeben:
     * anzahlAnlegenJeGattung: Anzahl der Aktionäre, die je Gattung angelegt werden (normale Testfälle)
     * 
     * Ergänzung anlegen: für ku178 und Mitgliederversammlungen: aktienregisterErgaenzung wird mit gefüllt
     * 
     * Gruppe in Aktienregister verteilen: gruppenVerteilen
     */
    
    
    public void anlegenBestand(DbBundle pDbBundle) {
        boolean inhaberaktienAktiv=pDbBundle.param.paramBasis.inhaberaktienAktiv;
        boolean namensaktienAktiv=pDbBundle.param.paramBasis.namensaktienAktiv;
        
        if (namensaktienAktiv && inhaberaktienAktiv==false) {
            /*Nur Namensaktien anlegen*/
            for (int i = 1; i <= anzahlAnlegenJeGattung; i++) {
                anlegenBestandEinzelnerAktionaer(i, pDbBundle, 1 /*Namensaktien*/, 1 /*Gattung*/);
            }
        }
        
        if (inhaberaktienAktiv) {
            /*Inhaberaktien anlegen*/
            for (int iGattung=1;iGattung<=SParamProgramm.anzahlGattungen;iGattung++) {
                if (pDbBundle.param.paramBasis.getGattungAktiv(iGattung)) {
                    for (int i = 1; i <= anzahlAnlegenJeGattung; i++) {
                        anlegenBestandEinzelnerAktionaer(i+pDbBundle.param.paramNummernkreise.vonSubEintrittskartennummer[iGattung][2], pDbBundle, 2 /*Inhaberaktien*/, iGattung);
                    }
                }
            }
        }
        
        if (namensaktienAktiv && inhaberaktienAktiv) {
            /*Zusätzlich Namensaktien anlegen, ab 1.000.000*/
            for (int i = 1; i <= anzahlAnlegenJeGattung; i++) {
                anlegenBestandEinzelnerAktionaer(i+1000000, pDbBundle, 1 /*Namensaktien*/, 1 /*Gattung*/);
            }
        }

        if (namensaktienAktiv) {
            anlegenDetailTest(pDbBundle);
        }
    }
 
    
    
    private void anlegenBestandEinzelnerAktionaer(int i, DbBundle pDbBundle, int namensaktienOderInhaberaktien, int pGattung) {
        
            EclAktienregister eintrag = new EclAktienregister();
            
            BlWillenserklaerung lWillenserklaerung = null;
            int ekNr=0;

            
            if (namensaktienOderInhaberaktien==1) {
                eintrag.aktionaersnummer = CaString.fuelleLinksNull(Integer.toString(i), pDbBundle.param.paramBasis.laengeAktionaersnummer) + "0";
            }
            else {
                lWillenserklaerung=new BlWillenserklaerung();
                lWillenserklaerung.setzeDbBundle(pDbBundle);
                ekNr=lWillenserklaerung.getNeueKarteAuto(KonstKartenklasse.eintrittskartennummer, pGattung, false);
                eintrag.aktionaersnummer = Integer.toString(ekNr);
            }
    
            fuelleAktionaerDaten(eintrag, i, namensaktienOderInhaberaktien);
    
            eintrag.gattungId = pGattung;
    
            if (eintrag.titel.isEmpty()) {
                eintrag.nameKomplett = eintrag.nachname + ", " + eintrag.vorname;
            }
            else {
                eintrag.nameKomplett = eintrag.nachname + ", " + eintrag.titel+" "+eintrag.vorname;
            }
    
            if (rcVersandAbweichend == 0) {
                switch (eintrag.anredeId) {
                case 1:
                    eintrag.adresszeile1 = "Herr";
                    break;
                case 2:
                    eintrag.adresszeile1 = "";
                    break;
                case 3:
                    eintrag.adresszeile1 = "Frau";
                    break;
                }
    
                if (rcPersonenArt == 0) {
                    eintrag.adresszeile2 = eintrag.vorname + " " + eintrag.nachname;
                } else {
                    eintrag.adresszeile2 = eintrag.nachname;
                }
    
                eintrag.adresszeile3 = eintrag.strasse;
                eintrag.adresszeile4 = eintrag.postleitzahl + " " + eintrag.ort;
            } else { /*Abweichende Versandadresse*/
                switch (eintrag.anredeIdVersand) {
                case 1:
                    eintrag.adresszeile1 = "Herr";
                    break;
                case 2:
                    eintrag.adresszeile1 = "";
                    break;
                case 3:
                    eintrag.adresszeile1 = "Frau";
                    break;
                case 4:
                    eintrag.adresszeile1 = "";
                    break;
                }
    
                if (rcPersonenArt == 0) {
                    eintrag.adresszeile2 = eintrag.vornameVersand + " " + eintrag.nachnameVersand;
                } else {
                    eintrag.adresszeile2 = eintrag.nachnameVersand;
                }
    
                eintrag.adresszeile3 = eintrag.strasseVersand;
                eintrag.adresszeile4 = eintrag.postleitzahlVersand + " " + eintrag.ortVersand;
            }
    
            int rc=pDbBundle.dbAktienregister.insert(eintrag);
            if (rc!=1) {
                CaBug.drucke("002 "+rc+CaFehler.getFehlertext(rc, 0));
               
            }
            
            anlegenLoginKennung(pDbBundle, eintrag);
            if (ergaenzungAnlegen) {
                fuelleErgaenzung(i, pDbBundle, eintrag.aktienregisterIdent);
            }
            
            if (namensaktienOderInhaberaktien==2) {
                /*Anmelden*/
                lWillenserklaerung = new BlWillenserklaerung();
                lWillenserklaerung.pErteiltAufWeg = KonstEingabeQuelle.sonstigesAnmeldestelle;
                lWillenserklaerung.pErteiltZeitpunkt = CaDatumZeit.DatumZeitStringFuerDatenbank();

                lWillenserklaerung.pEclAktienregisterEintrag = eintrag;

                lWillenserklaerung.pAktienAnmelden = -1; /*Alle Aktien anmelden*/
                lWillenserklaerung.pAnmeldungFix = false; /*Nicht "Fix" anmelden*/
                lWillenserklaerung.pAnzahlAnmeldungen = 1;
                lWillenserklaerung.pWillenserklaerungGeberIdent = -1; /*Aktionär gibt in diesem Fall*/

                lWillenserklaerung.anmeldungAusAktienregister(pDbBundle);
                if (lWillenserklaerung.rcIstZulaessig==false) {
                    CaBug.drucke("001 "+lWillenserklaerung.rcGrundFuerUnzulaessig+CaFehler.getFehlertext(lWillenserklaerung.rcGrundFuerUnzulaessig, 0));
                }
                
                /*Vorher reservierte Eintrittskartennummer vergeben*/
                lWillenserklaerung.piMeldungsIdentAktionaer = lWillenserklaerung.rcMeldungen[0];
                lWillenserklaerung.pZutrittsIdent.zutrittsIdent=Integer.toString(ekNr);
                lWillenserklaerung.pVersandartEK=1;
                lWillenserklaerung.pWillenserklaerungGeberIdent = -1; /* Aktionär */
                lWillenserklaerung.neueZutrittsIdentZuMeldung(pDbBundle, true);
                
            }

            
            if (i % 1000 == 0) {
                CaBug.druckeInfo("BvTestdaten.anlegenMassenAktienregisterNeu " + i + " erzeugt "
                        + CaDatumZeit.DatumZeitStringFuerDatenbank());
            }
    }


    private void anlegenLoginKennung(DbBundle pDbBundle, EclAktienregister pAktienregister) {
        EclLoginDaten eintragLoginDaten = new EclLoginDaten();
        eintragLoginDaten.loginKennung = pAktienregister.aktionaersnummer;
        eintragLoginDaten.passwortVerschluesselt = CaPasswortVerschluesseln.verschluesseln(passwortVergeben);
        eintragLoginDaten.passwortInitial = passwortVergeben+passwortVergeben+passwortVergeben;
        eintragLoginDaten.kennungArt = KonstLoginKennungArt.aktienregister;
        eintragLoginDaten.aktienregisterIdent = pAktienregister.aktienregisterIdent;
        
        if (pDbBundle.param.paramPortal.emailVersandRegistrierungOderWiderspruch==2) {
            eintragLoginDaten.eVersandRegistrierung=99;
        }
        
        if ((pAktienregister.aktienregisterIdent % 2)==0) {
            eintragLoginDaten.eMail2FuerVersand="lmsbo@t-"+eintragLoginDaten.loginKennung;
        }
        
        pDbBundle.dbLoginDaten.insert(eintragLoginDaten);
       
    }
    
    
    /**0=normale Person 1=juristische Person*/
    private int rcPersonenArt = 0;
    /**1=>abweichende Versandadresse*/
    private int rcVersandAbweichend = 0;

    private void fuelleAktionaerDaten(EclAktienregister eintrag, int pLaufendeNummer, int pNamensaktienOderInhaberaktien) {
    
        rcPersonenArt = 0;
        rcVersandAbweichend = 0;
    
        int aktionaerIntern = (pLaufendeNummer % 35) + 1;
        switch (aktionaerIntern) {
        case 1: {
            eintrag.stueckAktien = 1;
            eintrag.stimmen = 1;
            eintrag.besitzart = "E";
            eintrag.stimmausschluss = "";
            eintrag.nachname = "Test";
            eintrag.vorname = "";
            eintrag.ort = "Düsseldorf";
            eintrag.anredeId = 2;
            eintrag.postleitzahl = "";
            eintrag.staatId = 56;
            eintrag.strasse = "";
            eintrag.titel = "";
            eintrag.istPersonengemeinschaft = 0;
            eintrag.istJuristischePerson = 1;
    
            break;
        }
        case 2: {
            eintrag.stueckAktien = 10;
            eintrag.stimmen = 10;
            eintrag.besitzart = "V";
            eintrag.stimmausschluss = "";
            eintrag.nachname = "Test";
            eintrag.vorname = "";
            eintrag.ort = "München";
            eintrag.anredeId = 2;
            eintrag.postleitzahl = "";
            eintrag.staatId = 56;
            eintrag.strasse = "";
            eintrag.titel = "";
            eintrag.istPersonengemeinschaft = 0;
            eintrag.istJuristischePerson = 1;

            break;
        }
        case 3: {
            eintrag.stueckAktien = 100;
            eintrag.stimmen = 100;
            eintrag.besitzart = "E";
            eintrag.stimmausschluss = "";
            eintrag.nachname = "Test";
            eintrag.vorname = "";
            eintrag.ort = "Düsseldorf";
            eintrag.anredeId = 2;
            eintrag.postleitzahl = "";
            eintrag.staatId = 56;
            eintrag.strasse = "";
            eintrag.titel = "";
            eintrag.istPersonengemeinschaft = 0;
            eintrag.istJuristischePerson = 1;
    
            break;
        }
        case 4: {
            eintrag.stueckAktien = 1000;
            eintrag.stimmen = 1000;
            eintrag.besitzart = "V";
            eintrag.stimmausschluss = "";
            eintrag.nachname = "Test";
            eintrag.vorname = "";
            eintrag.ort = "München";
            eintrag.anredeId = 2;
            eintrag.postleitzahl = "";
            eintrag.staatId = 56;
            eintrag.strasse = "";
            eintrag.titel = "";
            eintrag.istPersonengemeinschaft = 0;
            eintrag.istJuristischePerson = 1;
    
            break;
        }
        case 5: {
            eintrag.stueckAktien = 10000;
            eintrag.stimmen = 10000;
            eintrag.besitzart = "E";
            eintrag.stimmausschluss = "";
            eintrag.nachname = "Mustermann";
            eintrag.vorname = "Karl";
            eintrag.ort = "Berlin";
            eintrag.anredeId = 1;
            eintrag.postleitzahl = "80015";
            eintrag.staatId = 56;
            eintrag.strasse = "Haidelweg 4711";
            eintrag.titel = "Dr.";
            eintrag.istPersonengemeinschaft = 0;
            eintrag.istJuristischePerson = 0;
    
            break;
        }
        case 6: {
            eintrag.stueckAktien = 5;
            eintrag.stimmen = 5;
            eintrag.besitzart = "E";
            eintrag.stimmausschluss = "";
            eintrag.nachname = "Schaumamal";
            eintrag.vorname = "Anna";
            eintrag.ort = "Stuttgart";
            eintrag.anredeId = 3;
            eintrag.postleitzahl = "80016";
            eintrag.staatId = 56;
            eintrag.strasse = "Haidelweg 4711";
            eintrag.titel = "";
            eintrag.istPersonengemeinschaft = 0;
            eintrag.istJuristischePerson = 0;
    
            break;
        }
        case 7: { //Abweichende Versandadresse
            eintrag.stueckAktien = 50;
            eintrag.stimmen = 50;
            eintrag.besitzart = "E";
            eintrag.stimmausschluss = "";
            eintrag.nachname = "Mustermeier";
            eintrag.vorname = "Karl";
            eintrag.ort = "München";
            eintrag.anredeId = 1;
            eintrag.postleitzahl = "12345";
            eintrag.staatId = 56;
            eintrag.strasse = "Haidelweg 4711";
            eintrag.titel = "Dr.";
            eintrag.istPersonengemeinschaft = 0;
            eintrag.istJuristischePerson = 0;
    
            eintrag.versandAbweichend = 1;
            eintrag.nachnameVersand = "Fehlerteufel";
            eintrag.vornameVersand = "Helga";
            eintrag.ortVersand = "Berlin";
            eintrag.anredeIdVersand = 3;
            eintrag.strasseVersand = "Programmiererweg 14";
            eintrag.ortVersand = "Amper-Valley";
            eintrag.postleitzahlVersand = "12345";
    
            break;
        }
        case 8: {//Abweichende Versandadresse
            eintrag.stueckAktien = 500;
            eintrag.stimmen = 500;
            eintrag.besitzart = "E";
            eintrag.stimmausschluss = "";
            eintrag.nachname = "Demohuber";
            eintrag.vorname = "Karl";
            eintrag.ort = "München";
            eintrag.anredeId = 1;
            eintrag.postleitzahl = "12345";
            eintrag.staatId = 56;
            eintrag.strasse = "Haidelweg 4711";
            eintrag.titel = "Dr.";
            eintrag.istPersonengemeinschaft = 0;
            eintrag.istJuristischePerson = 0;
    
            eintrag.versandAbweichend = 1;
            eintrag.nachnameVersand = "Fehlerteufel";
            eintrag.vornameVersand = "Helga";
            eintrag.ortVersand = "Berlin";
            eintrag.anredeIdVersand = 3;
            eintrag.strasseVersand = "Programmiererweg 14";
            eintrag.ortVersand = "Amper-Valley";
            eintrag.postleitzahlVersand = "12345";
    
            break;
        }
        case 9: {//Abweichende Versandadresse
            eintrag.stueckAktien = 1;
            eintrag.stimmen = 1;
            eintrag.besitzart = "E";
            eintrag.stimmausschluss = "";
            eintrag.nachname = "Testerich";
            eintrag.vorname = "Hans";
            eintrag.ort = "München";
            eintrag.anredeId = 1;
            eintrag.postleitzahl = "12345";
            eintrag.staatId = 56;
            eintrag.strasse = "Haidelweg 4711";
            eintrag.titel = "Dr.";
            eintrag.istPersonengemeinschaft = 0;
            eintrag.istJuristischePerson = 0;
    
            eintrag.versandAbweichend = 1;
            eintrag.nachnameVersand = "Fehlerteufel";
            eintrag.vornameVersand = "Helga";
            eintrag.ortVersand = "Berlin";
            eintrag.anredeIdVersand = 3;
            eintrag.strasseVersand = "Programmiererweg 14";
            eintrag.ortVersand = "Amper-Valley";
            eintrag.postleitzahlVersand = "12345";
    
            break;
        }
        case 10: {//Abweichende Versandadresse
            eintrag.stueckAktien = 10;
            eintrag.stimmen = 10;
            eintrag.besitzart = "E";
            eintrag.stimmausschluss = "";
            eintrag.nachname = "Demomann";
            eintrag.vorname = "Karl";
            eintrag.ort = "Berlin";
            eintrag.anredeId = 1;
            eintrag.postleitzahl = "12345";
            eintrag.staatId = 56;
            eintrag.strasse = "Haidelweg 4711";
            eintrag.titel = "Dr.";
            eintrag.istPersonengemeinschaft = 0;
            eintrag.istJuristischePerson = 0;
    
            eintrag.versandAbweichend = 1;
            eintrag.nachnameVersand = "Fehlerteufel";
            eintrag.vornameVersand = "Helga";
            eintrag.ortVersand = "Berlin";
            eintrag.anredeIdVersand = 3;
            eintrag.strasseVersand = "Programmiererweg 14";
            eintrag.ortVersand = "Amper-Valley";
            eintrag.postleitzahlVersand = "12345";
    
            break;
        }
        case 11: {//Personengemeinschaft + Abweichende Versandadresse
            eintrag.stueckAktien = 1;
            eintrag.stimmen = 1;
            eintrag.besitzart = "E";
            eintrag.stimmausschluss = "";
            eintrag.nachname = "Ameise";
            if (pNamensaktienOderInhaberaktien==1) {
                eintrag.vorname = "Bärli und Bär";
            }
            else {
                eintrag.vorname = "Bärli";
            }
            eintrag.ort = "München";
            eintrag.anredeId = 4;
            eintrag.postleitzahl = "12345";
            eintrag.staatId = 56;
            eintrag.strasse = "Haidelweg 4711";
            eintrag.titel = "";
            
            eintrag.istPersonengemeinschaft = 1;
            eintrag.istJuristischePerson = 0;
            if (pNamensaktienOderInhaberaktien==1) {
                eintrag.versandAbweichend = 1;
            }
            else {
                eintrag.versandAbweichend = 0;
            }
            eintrag.nachnameVersand = "Fehlerteufel";
            eintrag.vornameVersand = "Helga";
            eintrag.ortVersand = "Berlin";
            eintrag.anredeIdVersand = 3;
            eintrag.strasseVersand = "Programmiererweg 14";
            eintrag.ortVersand = "Amper-Valley";
            eintrag.postleitzahlVersand = "12345";
    
            break;
        }
        case 12: {//Personengemeinschaft + Abweichende Versandadresse
            eintrag.stueckAktien = 2;
            eintrag.stimmen = 2;
            eintrag.besitzart = "E";
            eintrag.stimmausschluss = "";
            eintrag.nachname = "Elefant";
            if (pNamensaktienOderInhaberaktien==1) {
                eintrag.vorname = "Erna und Erik";
            }
            else {
                eintrag.vorname = "Erna";
            }
            eintrag.ort = "München";
            eintrag.anredeId = 4;
            eintrag.postleitzahl = "12345";
            eintrag.staatId = 56;
            eintrag.strasse = "Haidelweg 4711";
            eintrag.titel = "";
            if (pNamensaktienOderInhaberaktien==1) {
                eintrag.versandAbweichend = 1;
            }
            else {
                eintrag.versandAbweichend = 0;
            }
            eintrag.istJuristischePerson = 0;
    
            eintrag.versandAbweichend = 1;
            eintrag.nachnameVersand = "Fehlerteufel";
            eintrag.vornameVersand = "Helga";
            eintrag.ortVersand = "Berlin";
            eintrag.anredeIdVersand = 3;
            eintrag.strasseVersand = "Programmiererweg 14";
            eintrag.ortVersand = "Amper-Valley";
            eintrag.postleitzahlVersand = "12345";
    
            break;
        }
        case 13: {//Personengemeinschaft + Abweichende Versandadresse
            eintrag.stueckAktien = 13;
            eintrag.stimmen = 13;
            eintrag.besitzart = "E";
            eintrag.stimmausschluss = "";
            eintrag.nachname = "Käfer";
            if (pNamensaktienOderInhaberaktien==1) {
                eintrag.vorname = "Carina und Karl";
            }
            else {
                eintrag.vorname = "Carina";
            }
            eintrag.ort = "München";
            eintrag.anredeId = 4;
            eintrag.postleitzahl = "12345";
            eintrag.staatId = 56;
            eintrag.strasse = "Haidelweg 4711";
            eintrag.titel = "";
            if (pNamensaktienOderInhaberaktien==1) {
                eintrag.versandAbweichend = 1;
            }
            else {
                eintrag.versandAbweichend = 0;
            }
            eintrag.istJuristischePerson = 0;
    
            eintrag.versandAbweichend = 1;
            eintrag.nachnameVersand = "Fehlerteufel";
            eintrag.vornameVersand = "Helga";
            eintrag.ortVersand = "Berlin";
            eintrag.anredeIdVersand = 3;
            eintrag.strasseVersand = "Programmiererweg 14";
            eintrag.ortVersand = "Amper-Valley";
            eintrag.postleitzahlVersand = "12345";
    
            break;
        }
        case 14: {//Personengemeinschaft
            eintrag.stueckAktien = 14;
            eintrag.stimmen = 14;
            eintrag.besitzart = "E";
            eintrag.stimmausschluss = "";
            eintrag.nachname = "Frosch";
            if (pNamensaktienOderInhaberaktien==1) {
                eintrag.vorname = "Queen und King";
            }
            else {
                eintrag.vorname = "Queen";
            }
            eintrag.ort = "München";
            eintrag.anredeId = 4;
            eintrag.postleitzahl = "12345";
            eintrag.staatId = 56;
            eintrag.strasse = "Haidelweg 4711";
            eintrag.titel = "";
            if (pNamensaktienOderInhaberaktien==1) {
                eintrag.versandAbweichend = 1;
            }
            else {
                eintrag.versandAbweichend = 0;
            }
            eintrag.istJuristischePerson = 0;
    
            break;
        }
        case 15: {//Personengemeinschaft
            eintrag.stueckAktien = 15;
            eintrag.stimmen = 15;
            eintrag.besitzart = "E";
            eintrag.stimmausschluss = "";
            eintrag.nachname = "Hummerl";
            if (pNamensaktienOderInhaberaktien==1) {
                eintrag.vorname = "Helga und Hugo";
            }
            else {
                eintrag.vorname = "Helga";
            }
            eintrag.ort = "München";
            eintrag.anredeId = 4;
            eintrag.postleitzahl = "12345";
            eintrag.staatId = 56;
            eintrag.strasse = "Haidelweg 4711";
            eintrag.titel = "";
            if (pNamensaktienOderInhaberaktien==1) {
                eintrag.versandAbweichend = 1;
            }
            else {
                eintrag.versandAbweichend = 0;
            }
            eintrag.istJuristischePerson = 0;
    
            break;
        }
        case 16: {//Personengemeinschaft
            eintrag.stueckAktien = 16;
            eintrag.stimmen = 16;
            eintrag.besitzart = "E";
            eintrag.stimmausschluss = "";
            eintrag.nachname = "Brummbär";
            if (pNamensaktienOderInhaberaktien==1) {
                eintrag.vorname = "Berta und Boris";
            }
            else {
                eintrag.vorname = "Berta";
            }
            eintrag.ort = "München";
            eintrag.anredeId = 4;
            eintrag.postleitzahl = "12345";
            eintrag.staatId = 56;
            eintrag.strasse = "Haidelweg 4711";
            eintrag.titel = "";
            if (pNamensaktienOderInhaberaktien==1) {
                eintrag.versandAbweichend = 1;
            }
            else {
                eintrag.versandAbweichend = 0;
            }
            eintrag.istJuristischePerson = 0;
    
            break;
        }
        case 17: {//Personengemeinschaft
            eintrag.stueckAktien = 17;
            eintrag.stimmen = 17;
            eintrag.besitzart = "E";
            eintrag.stimmausschluss = "";
            eintrag.nachname = "Ameise";
            if (pNamensaktienOderInhaberaktien==1) {
                eintrag.vorname = "Andrea und Anton";
            }
            else {
                eintrag.vorname = "Andrea";
            }
            eintrag.ort = "München";
            eintrag.anredeId = 4;
            eintrag.postleitzahl = "12345";
            eintrag.staatId = 56;
            eintrag.strasse = "Haidelweg 4711";
            eintrag.titel = "";
            if (pNamensaktienOderInhaberaktien==1) {
                eintrag.versandAbweichend = 1;
            }
            else {
                eintrag.versandAbweichend = 0;
            }
            eintrag.istJuristischePerson = 0;
    
            break;
        }
        case 18: {//Personengemeinschaft
            eintrag.stueckAktien = 18;
            eintrag.stimmen = 18;
            eintrag.besitzart = "E";
            eintrag.stimmausschluss = "";
            eintrag.nachname = "Dromedar";
            eintrag.vorname = "Dorothea";
            eintrag.ort = "München";
            eintrag.anredeId = 4;
            eintrag.postleitzahl = "12345";
            eintrag.staatId = 56;
            eintrag.strasse = "Haidelweg 4711";
            eintrag.titel = "";
            eintrag.istPersonengemeinschaft = 1;
            eintrag.istJuristischePerson = 0;
    
            break;
        }
        case 19: {//Personengemeinschaft
            eintrag.stueckAktien = 19;
            eintrag.stimmen = 19;
            eintrag.besitzart = "E";
            eintrag.stimmausschluss = "";
            eintrag.nachname = "Frosch";
            eintrag.vorname = "Fritz";
            eintrag.ort = "München";
            eintrag.anredeId = 4;
            eintrag.postleitzahl = "12345";
            eintrag.staatId = 56;
            eintrag.strasse = "Haidelweg 4711";
            eintrag.titel = "";
            eintrag.istPersonengemeinschaft = 1;
            eintrag.istJuristischePerson = 0;
    
            break;
        }
        case 20: {//Personengemeinschaft
            eintrag.stueckAktien = 20;
            eintrag.stimmen = 20;
            eintrag.besitzart = "E";
            eintrag.stimmausschluss = "";
            eintrag.nachname = "Elefant";
            eintrag.vorname = "Eda";
            eintrag.ort = "München";
            eintrag.anredeId = 4;
            eintrag.postleitzahl = "12345";
            eintrag.staatId = 56;
            eintrag.strasse = "Haidelweg 4711";
            eintrag.titel = "";
            eintrag.istPersonengemeinschaft = 1;
            eintrag.istJuristischePerson = 0;
    
            break;
        }
        case 21: {
            eintrag.stueckAktien = 21;
            eintrag.stimmen = 21;
            eintrag.besitzart = "E";
            eintrag.stimmausschluss = "";
            eintrag.nachname = "Giraffe";
            eintrag.vorname = "Gunter";
            eintrag.ort = "München";
            eintrag.anredeId = 1;
            eintrag.postleitzahl = "80011";
            eintrag.staatId = 56;
            eintrag.strasse = "Haidelweg 4711";
            eintrag.titel = "Dr.";
            eintrag.istPersonengemeinschaft = 0;
            eintrag.istJuristischePerson = 0;
    
            break;
        }
        case 22: {
            eintrag.stueckAktien = 22;
            eintrag.stimmen = 22;
            eintrag.besitzart = "E";
            eintrag.stimmausschluss = "";
            eintrag.nachname = "Hummel";
            eintrag.vorname = "Hugo";
            eintrag.ort = "München";
            eintrag.anredeId = 1;
            eintrag.postleitzahl = "80011";
            eintrag.staatId = 56;
            eintrag.strasse = "Haidelweg 4711";
            eintrag.titel = "Dr.";
            eintrag.istPersonengemeinschaft = 0;
            eintrag.istJuristischePerson = 0;
    
            break;
        }
        case 23: {
            eintrag.stueckAktien = 23;
            eintrag.stimmen = 23;
            eintrag.besitzart = "E";
            eintrag.stimmausschluss = "";
            eintrag.nachname = "Löwe";
            eintrag.vorname = "Leonhard";
            eintrag.ort = "München";
            eintrag.anredeId = 1;
            eintrag.postleitzahl = "80011";
            eintrag.staatId = 56;
            eintrag.strasse = "Haidelweg 4711";
            eintrag.titel = "Dr.";
            eintrag.istPersonengemeinschaft = 0;
            eintrag.istJuristischePerson = 0;
    
            break;
        }
        case 24: {
            eintrag.stueckAktien = 24;
            eintrag.stimmen = 24;
            eintrag.besitzart = "E";
            eintrag.stimmausschluss = "";
            eintrag.nachname = "Löwe";
            eintrag.vorname = "Leo";
            eintrag.ort = "München";
            eintrag.anredeId = 1;
            eintrag.postleitzahl = "80011";
            eintrag.staatId = 56;
            eintrag.strasse = "Haidelweg 4711";
            eintrag.titel = "Dr.";
            eintrag.istPersonengemeinschaft = 0;
            eintrag.istJuristischePerson = 0;
    
            break;
        }
        case 25: {
            eintrag.stueckAktien = 25;
            eintrag.stimmen = 25;
            eintrag.besitzart = "E";
            eintrag.stimmausschluss = "";
            eintrag.nachname = "Schluckspecht";
            eintrag.vorname = "Hugo";
            eintrag.ort = "München";
            eintrag.anredeId = 1;
            eintrag.postleitzahl = "80011";
            eintrag.staatId = 56;
            eintrag.strasse = "Haidelweg 4711";
            eintrag.titel = "Dr.";
            eintrag.istPersonengemeinschaft = 0;
            eintrag.istJuristischePerson = 0;
    
            break;
        }
        case 26: {
            eintrag.stueckAktien = 26;
            eintrag.stimmen = 26;
            eintrag.besitzart = "E";
            eintrag.stimmausschluss = "";
            eintrag.nachname = "Uhu";
            eintrag.vorname = "Urs";
            eintrag.ort = "München";
            eintrag.anredeId = 1;
            eintrag.postleitzahl = "80011";
            eintrag.staatId = 56;
            eintrag.strasse = "Haidelweg 4711";
            eintrag.titel = "Dr.";
            eintrag.istPersonengemeinschaft = 0;
            eintrag.istJuristischePerson = 0;
    
            break;
        }
        case 27: {
            eintrag.stueckAktien = 27;
            eintrag.stimmen = 27;
            eintrag.besitzart = "E";
            eintrag.stimmausschluss = "";
            eintrag.nachname = "Specht";
            eintrag.vorname = "Siegfried";
            eintrag.ort = "München";
            eintrag.anredeId = 1;
            eintrag.postleitzahl = "80011";
            eintrag.staatId = 56;
            eintrag.strasse = "Haidelweg 4711";
            eintrag.titel = "Dr.";
            eintrag.istPersonengemeinschaft = 0;
            eintrag.istJuristischePerson = 0;
    
            break;
        }
        case 28: {
            eintrag.stueckAktien = 28;
            eintrag.stimmen = 28;
            eintrag.besitzart = "E";
            eintrag.stimmausschluss = "";
            eintrag.nachname = "Paradiesvogel";
            eintrag.vorname = "Paul";
            eintrag.ort = "München";
            eintrag.anredeId = 1;
            eintrag.postleitzahl = "80011";
            eintrag.staatId = 56;
            eintrag.strasse = "Haidelweg 4711";
            eintrag.titel = "Dr.";
            eintrag.istPersonengemeinschaft = 0;
            eintrag.istJuristischePerson = 0;
    
            break;
        }
        case 29: {
            eintrag.stueckAktien = 29;
            eintrag.stimmen = 29;
            eintrag.besitzart = "E";
            eintrag.stimmausschluss = "";
            eintrag.nachname = "Nilpferd";
            eintrag.vorname = "Nils";
            eintrag.ort = "München";
            eintrag.anredeId = 1;
            eintrag.postleitzahl = "80011";
            eintrag.staatId = 56;
            eintrag.strasse = "Haidelweg 4711";
            eintrag.titel = "Dr.";
            eintrag.istPersonengemeinschaft = 0;
            eintrag.istJuristischePerson = 0;
    
            break;
        }
        case 30: {
            eintrag.stueckAktien = 30;
            eintrag.stimmen = 30;
            eintrag.besitzart = "E";
            eintrag.stimmausschluss = "";
            eintrag.nachname = "Leopard";
            eintrag.vorname = "Lea";
            eintrag.ort = "München";
            eintrag.anredeId = 3;
            eintrag.postleitzahl = "80011";
            eintrag.staatId = 56;
            eintrag.strasse = "Haidelweg 4711";
            eintrag.titel = "Dr.";
            eintrag.istPersonengemeinschaft = 0;
            eintrag.istJuristischePerson = 0;
    
            break;
        }
        case 31: {
            eintrag.stueckAktien = 31;
            eintrag.stimmen = 31;
            eintrag.besitzart = "E";
            eintrag.stimmausschluss = "";
            eintrag.nachname = "Ameise";
            eintrag.vorname = "Andrea";
            eintrag.ort = "München";
            eintrag.anredeId = 3;
            eintrag.postleitzahl = "80011";
            eintrag.staatId = 56;
            eintrag.strasse = "Haidelweg 4711";
            eintrag.titel = "Dr.";
            eintrag.istPersonengemeinschaft = 0;
            eintrag.istJuristischePerson = 0;
    
            break;
        }
        case 32: {
            eintrag.stueckAktien = 32;
            eintrag.stimmen = 32;
            eintrag.besitzart = "E";
            eintrag.stimmausschluss = "";
            eintrag.nachname = "Brummbär";
            eintrag.vorname = "Boris";
            eintrag.ort = "München";
            eintrag.anredeId = 1;
            eintrag.postleitzahl = "80011";
            eintrag.staatId = 56;
            eintrag.strasse = "Haidelweg 4711";
            eintrag.titel = "Dr.";
            eintrag.istPersonengemeinschaft = 0;
            eintrag.istJuristischePerson = 0;
    
            break;
        }
        case 33: {
            eintrag.stueckAktien = 33;
            eintrag.stimmen = 33;
            eintrag.besitzart = "E";
            eintrag.stimmausschluss = "";
            eintrag.nachname = "Käfer";
            eintrag.vorname = "Karla";
            eintrag.ort = "München";
            eintrag.anredeId = 3;
            eintrag.postleitzahl = "80011";
            eintrag.staatId = 56;
            eintrag.strasse = "Haidelweg 4711";
            eintrag.titel = "Dr.";
            eintrag.istPersonengemeinschaft = 0;
            eintrag.istJuristischePerson = 0;
    
            break;
        }
        case 34: {
            eintrag.stueckAktien = 34;
            eintrag.stimmen = 34;
            eintrag.besitzart = "E";
            eintrag.stimmausschluss = "";
            eintrag.nachname = "Dromedarf";
            eintrag.vorname = "Detlef";
            eintrag.ort = "München";
            eintrag.anredeId = 1;
            eintrag.postleitzahl = "80011";
            eintrag.staatId = 56;
            eintrag.strasse = "Haidelweg 4711";
            eintrag.titel = "Dr.";
            eintrag.istPersonengemeinschaft = 0;
            eintrag.istJuristischePerson = 0;
    
            break;
        }
        case 35: {
            eintrag.stueckAktien = 35;
            eintrag.stimmen = 35;
            eintrag.besitzart = "E";
            eintrag.stimmausschluss = "";
            eintrag.nachname = "Frosch";
            eintrag.vorname = "Frieda";
            eintrag.ort = "München";
            eintrag.anredeId = 3;
            eintrag.postleitzahl = "80011";
            eintrag.staatId = 56;
            eintrag.strasse = "Haidelweg 4711";
            eintrag.titel = "Dr.";
            eintrag.istPersonengemeinschaft = 0;
            eintrag.istJuristischePerson = 0;
    
            break;
        }
    
        }
    
        int gruppe = 1;
        if (gruppenVerteilen) {
            gruppe = (pLaufendeNummer % 7) + 1;
        }
    
        eintrag.gruppe = gruppe;
    
        rcPersonenArt = eintrag.istJuristischePerson = 0;
        rcVersandAbweichend = eintrag.versandAbweichend;
        return;
    }

    
    private void fuelleErgaenzung(int i, DbBundle pDbBundle, int pIdent) {
        EclAktienregisterErgaenzung lAktienregisterErgaenzung = new EclAktienregisterErgaenzung();
        lAktienregisterErgaenzung.aktienregisterIdent = pIdent;
        switch (i % 3) {
        case 0:
            break;
        case 1:
            lAktienregisterErgaenzung.setErgaenzungString(KonstAktienregisterErgaenzung.ku178_GeburtsdatumMitglied,
                    "11.11.1111");
            break;
        case 2:
            lAktienregisterErgaenzung.setErgaenzungString(KonstAktienregisterErgaenzung.ku178_GeburtsdatumMitglied,
                    "11.11.1111");
            lAktienregisterErgaenzung.setErgaenzungString(KonstAktienregisterErgaenzung.ku178_GeburtsdatumEhegatte,
                    "12.12.1212");
            break;
        }

        String bundesland = Integer.toString((i % 5) + 1);
        lAktienregisterErgaenzung.setErgaenzungString(KonstAktienregisterErgaenzung.ku178_Bundesland, bundesland);

        int gruppe = 1;
        if (gruppenVerteilen) {
            gruppe = (i % 6) + 1;
        }
        String sGruppe = Integer.toString(gruppe);
        lAktienregisterErgaenzung.setErgaenzungString(KonstAktienregisterErgaenzung.ku178_Gruppe, sGruppe);

        lAktienregisterErgaenzung.mandant = pDbBundle.clGlobalVar.mandant;
        pDbBundle.dbAktienregisterErgaenzung.insert(lAktienregisterErgaenzung);
    }


    /**Spezial
     * 20001xx= normaler Aktionär natürliche Person
     * 20002xx= juristische Person
     * 20003xx= juristische Person mit E und F
     * 20004xx= Personengemeinschaft
     */
    private void anlegenDetailTest(DbBundle pDbBundle) {
        EclAktienregister eintrag = new EclAktienregister();

        /*2000100= normaler Aktionär natürliche Person*/
        for (int i = 1; i <= 99; i++) {
            int iAktioanersnummer=2000100+i;
            eintrag = new EclAktienregister();
            eintrag.aktionaersnummer = CaString.fuelleLinksNull(Integer.toString(iAktioanersnummer), pDbBundle.param.paramBasis.laengeAktionaersnummer) + "0";
            eintrag.stueckAktien = i;
            eintrag.stimmen = i;
            eintrag.besitzart = "E";
            eintrag.stimmausschluss = "";
            eintrag.nachname = "Testerich";
            eintrag.vorname = "Hans";
            eintrag.ort = "München";
            eintrag.anredeId = 1;
            eintrag.postleitzahl = "12345";
            eintrag.staatId = 56;
            eintrag.strasse = "Haidelweg " + Integer.toString(i);
            eintrag.titel = "";
            eintrag.istPersonengemeinschaft = 0;
            eintrag.istJuristischePerson = 0;
            eintrag.nameKomplett = eintrag.nachname + ", " + eintrag.vorname;
            eintrag.adresszeile1 = "Familie";
            eintrag.adresszeile2 = "der von Testerichs";
            eintrag.adresszeile3 = "z.Hd. der Vermögensverwaltungsstelle";
            eintrag.adresszeile4 = "Ebenholzweg " + Integer.toString(i);
            eintrag.adresszeile5 = "83455 Dollershausen";
            pDbBundle.dbAktienregister.insert(eintrag);
            
            anlegenLoginKennung(pDbBundle, eintrag);
            
            if (ergaenzungAnlegen) {
                fuelleErgaenzung(i, pDbBundle, eintrag.aktienregisterIdent);
            }

        }

        /* 20002xx= juristische Person*/
        for (int i = 1; i <= 99; i++) {
            int iAktioanersnummer=2000200+i;
            eintrag = new EclAktienregister();
            eintrag.aktionaersnummer = CaString.fuelleLinksNull(Integer.toString(iAktioanersnummer), pDbBundle.param.paramBasis.laengeAktionaersnummer) + "0";
            eintrag.stueckAktien = i;
            eintrag.stimmen = i;
            eintrag.besitzart = "E";
            eintrag.stimmausschluss = "";
            eintrag.name1 = "Better Orange";
            eintrag.name2 = "IR & HV";
            eintrag.name3 = "AG";
            eintrag.ort = "München";
            eintrag.anredeId = 2;
            eintrag.postleitzahl = "12345";
            eintrag.staatId = 56;
            eintrag.strasse = "Haidelweg " + Integer.toString(i);
            eintrag.titel = "";
            eintrag.istPersonengemeinschaft = 0;
            eintrag.istJuristischePerson = 1;
            eintrag.nameKomplett = eintrag.name1 + " " + eintrag.name2 + " " + eintrag.name3;
            eintrag.adresszeile1 = "Better Orange";
            eintrag.adresszeile2 = "IR & HV AG";
            eintrag.adresszeile3 = "z.Hd. der Vermögensverbratungsstelle";
            eintrag.adresszeile4 = "Ebenholzweg " + Integer.toString(i);
            eintrag.adresszeile5 = "83455 Dollershausen";
            eintrag.adresszeile6 = "Schweiz";
            pDbBundle.dbAktienregister.insert(eintrag);
            
            anlegenLoginKennung(pDbBundle, eintrag);
            
            if (ergaenzungAnlegen) {
                fuelleErgaenzung(i, pDbBundle, eintrag.aktienregisterIdent);
            }

        }

        /* 20003xx= juristische Person mit E und F
         */
        for (int i = 1; i <= 99; i++) {
            int iAktioanersnummer=2000300+i;
            eintrag = new EclAktienregister();
            eintrag.aktionaersnummer = CaString.fuelleLinksNull(Integer.toString(iAktioanersnummer), pDbBundle.param.paramBasis.laengeAktionaersnummer) + "0";
            eintrag.stueckAktien = i;
            eintrag.stimmen = i;
            eintrag.besitzart = "E";
            eintrag.stimmausschluss = "";
            eintrag.name1 = "Better Orange";
            eintrag.name2 = "IR & HV";
            eintrag.name3 = "AG";
            eintrag.ort = "München";
            eintrag.anredeId = 2;
            eintrag.postleitzahl = "12345";
            eintrag.staatId = 56;
            eintrag.strasse = "Haidelweg " + Integer.toString(i);
            eintrag.titel = "";
            eintrag.istPersonengemeinschaft = 0;
            eintrag.istJuristischePerson = 1;
            eintrag.nameKomplett = eintrag.name1 + " " + eintrag.name2 + " " + eintrag.name3;
            eintrag.adresszeile1 = "Better Orange";
            eintrag.adresszeile2 = "IR & HV AG";
            eintrag.adresszeile3 = "z.Hd. der Vermögensverbratungsstelle";
            eintrag.adresszeile4 = "Ebenholzweg " + Integer.toString(i);
            eintrag.adresszeile5 = "83455 Dollershausen";
            eintrag.adresszeile6 = "Caiman-Islands";
            pDbBundle.dbAktienregister.insert(eintrag);
            
            anlegenLoginKennung(pDbBundle, eintrag);
            
            if (ergaenzungAnlegen) {
                fuelleErgaenzung(i, pDbBundle, eintrag.aktienregisterIdent);
            }

            iAktioanersnummer=2000300+i;
            eintrag = new EclAktienregister();
            eintrag.aktionaersnummer = CaString.fuelleLinksNull(Integer.toString(iAktioanersnummer), pDbBundle.param.paramBasis.laengeAktionaersnummer) + "1";
            eintrag.stueckAktien = i * 10;
            eintrag.stimmen = i * 10;
            eintrag.besitzart = "F";
            eintrag.stimmausschluss = "";
            eintrag.name1 = "Better Orange";
            eintrag.name2 = "IR & HV";
            eintrag.name3 = "AG";
            eintrag.ort = "München";
            eintrag.anredeId = 2;
            eintrag.postleitzahl = "12345";
            eintrag.staatId = 56;
            eintrag.strasse = "Haidelweg " + Integer.toString(i);
            eintrag.titel = "";
            eintrag.istPersonengemeinschaft = 0;
            eintrag.istJuristischePerson = 1;
            eintrag.nameKomplett = eintrag.name1 + " " + eintrag.name2 + " " + eintrag.name3;
            eintrag.adresszeile1 = "Better Orange";
            eintrag.adresszeile2 = "IR & HV AG";
            eintrag.adresszeile3 = "z.Hd. der Vermögensverbratungsstelle";
            eintrag.adresszeile4 = "Ebenholzweg " + Integer.toString(i);
            eintrag.adresszeile5 = "83455 Dollershausen";
            eintrag.adresszeile6 = "Caiman-Islands";
            pDbBundle.dbAktienregister.insert(eintrag);
            
            anlegenLoginKennung(pDbBundle, eintrag);
            
            if (ergaenzungAnlegen) {
                fuelleErgaenzung(i, pDbBundle, eintrag.aktienregisterIdent);
            }

        }

        /*20004xx= Personengemeinschaft*/
        for (int i = 1; i <= 99; i++) {
            int iAktioanersnummer=2000400+i;
            eintrag = new EclAktienregister();
            eintrag.aktionaersnummer = CaString.fuelleLinksNull(Integer.toString(iAktioanersnummer), pDbBundle.param.paramBasis.laengeAktionaersnummer) + "0";
            eintrag.stueckAktien = i;
            eintrag.stimmen = i;
            eintrag.besitzart = "E";
            eintrag.stimmausschluss = "";
            eintrag.nachname = "Testerich";
            eintrag.vorname = "Hans und Gisela";
            eintrag.ort = "München";
            eintrag.anredeId = 4;
            eintrag.postleitzahl = "12345";
            eintrag.staatId = 56;
            eintrag.strasse = "Haidelweg " + Integer.toString(i);
            eintrag.titel = "";
            eintrag.istPersonengemeinschaft = 1;
            eintrag.istJuristischePerson = 0;
            eintrag.nameKomplett = eintrag.nachname + ", " + eintrag.vorname;
            eintrag.adresszeile1 = "Familie";
            eintrag.adresszeile2 = "der von Testerichs";
            eintrag.adresszeile3 = "z.Hd. der Vermögensverwaltungsstelle";
            eintrag.adresszeile4 = "Ebenholzweg " + Integer.toString(i);
            eintrag.adresszeile5 = "83455 Dollershausen";
            pDbBundle.dbAktienregister.insert(eintrag);
            
            anlegenLoginKennung(pDbBundle, eintrag);
            
            if (ergaenzungAnlegen) {
                fuelleErgaenzung(i, pDbBundle, eintrag.aktienregisterIdent);
            }

        }
    }

    
    /**Alle Anmelden und eine EK ausstellen, die noch nicht angemeldet sind.
     * 
     * Funktioniert nur für Namensaktien! 
     * 
     * Inhaberaktien werden hier aber ja ignoriert, da diese bereits
     * bei der Initialisierung angemeldet werden.
     */
    public void anmeldenNichtAngemeldete(DbBundle pDbBundle, boolean pMitEintrittskartenausstellung, boolean pEkWieAktionaersnummer, boolean pEkWieAktionaersnummerOhneLetzteZiffer) {

        pDbBundle.dbAktienregister.leseAktienregisterOhneMeldung();

        EclAktienregister[] alleAktienregister = pDbBundle.dbAktienregister.ergebnisArray;
        int anzAktienregister = alleAktienregister.length;
        CaBug.druckeInfo("Anmelden=" + anzAktienregister +" "+CaDatumZeit.DatumZeitStringFuerDatenbank());

        for (int i = 0; i < anzAktienregister; i++) {

            EclAktienregister aktuellAktienregister = alleAktienregister[i];
            if (aktuellAktienregister.stueckAktien!=0) {

                BlWillenserklaerung lWillenserklaerung = new BlWillenserklaerung();
                lWillenserklaerung.pEclAktienregisterEintrag = aktuellAktienregister;
                lWillenserklaerung.pAktienAnmelden = -1;
                lWillenserklaerung.pAnzahlAnmeldungen = 1;

                lWillenserklaerung.anmeldungAusAktienregister(pDbBundle);
                boolean brc = lWillenserklaerung.rcIstZulaessig;
                if (brc==false) {
                    CaBug.drucke("001 "+lWillenserklaerung.rcGrundFuerUnzulaessig+" / "+CaFehler.getFehlertext(lWillenserklaerung.rcGrundFuerUnzulaessig, 0));
                }

                if (pMitEintrittskartenausstellung) {
                    BlWillenserklaerung ekWillenserklaerung = new BlWillenserklaerung();
                    ekWillenserklaerung.pErteiltAufWeg = 51;

                    ekWillenserklaerung.piMeldungsIdentAktionaer = lWillenserklaerung.rcMeldungen[0];

                    ekWillenserklaerung.pFolgeFuerWillenserklaerungIdent = lWillenserklaerung.rcWillenserklaerungIdent;

                    ekWillenserklaerung.pWillenserklaerungGeberIdent = -1; /* Aktionär */

                    /* Versandart */
                    ekWillenserklaerung.pVersandartEK = 1;

                    if (pEkWieAktionaersnummer) {
                        String hString=aktuellAktienregister.aktionaersnummer;
                        ekWillenserklaerung.pZutrittsIdent.zutrittsIdent=hString;
                        if (pEkWieAktionaersnummerOhneLetzteZiffer==true) {
                            hString=hString.substring(0, hString.length()-1);
                        }
                        ekWillenserklaerung.pZutrittsIdent.zutrittsIdent=hString;
                    }
                    ekWillenserklaerung.neueZutrittsIdentZuMeldung(pDbBundle);

                    boolean brcEK = ekWillenserklaerung.rcIstZulaessig;
                    if (brcEK==false) {
                        CaBug.drucke("002 "+ekWillenserklaerung.rcGrundFuerUnzulaessig+" / "+CaFehler.getFehlertext(ekWillenserklaerung.rcGrundFuerUnzulaessig, 0));
                    }
                }
            }
            if (i % 500 == 0) {
                CaBug.druckeInfo(i + " angemeldet " + CaDatumZeit.DatumZeitStringFuerDatenbank());
            }
        }

        CaBug.druckeInfo("Ende=" + CaDatumZeit.DatumZeitStringFuerDatenbank());
    }


     
    public void zusaetzlichesInitialpasswort(DbBundle pDbBundle) {
        pDbBundle.dbLoginDaten.read_all();
        int anz=pDbBundle.dbLoginDaten.anzErgebnis();
        if (anz>0) {
            for (int i=0;i<anz;i++) {
                EclLoginDaten lLoginDaten=pDbBundle.dbLoginDaten.ergebnisPosition(i);
                if (lLoginDaten.passwortInitial.isEmpty()) {
                    CaPasswortErzeugen caPasswortErzeugen=new CaPasswortErzeugen();
                    boolean grossbuchstabenVerwenden=(pDbBundle.param.paramPortal.passwortCaseSensitiv==1);
                    String neuesPW=caPasswortErzeugen.generatePWInitial(pDbBundle.param.paramPortal.passwortMindestLaenge, 0, 2, grossbuchstabenVerwenden);
                    lLoginDaten.passwortInitial=neuesPW;
                    pDbBundle.dbLoginDaten.update(lLoginDaten);
                }
            }
        }
    }
    
    
    public void ku217VertreterVorbereiten(DbBundle pDbBundle) {

        pDbBundle.dbAktienregisterErgaenzung.readAll();
        EclAktienregisterErgaenzung[] aktienregisterErgaenzungArray=pDbBundle.dbAktienregisterErgaenzung.ergebnisArray;
        
        for (int i = 0; i < pDbBundle.dbAktienregisterErgaenzung.anzErgebnis(); i++) {
            EclAktienregisterErgaenzung lAktienregisterErgaenzung = aktienregisterErgaenzungArray[i];
            if (lAktienregisterErgaenzung.ergaenzungKennzeichen[24] == 0
                    && !lAktienregisterErgaenzung.ergaenzungLangString[24].isEmpty()) {
                
                int aktienregisterIdent=lAktienregisterErgaenzung.aktienregisterIdent;
                pDbBundle.dbMeldungen.leseZuAktienregisterIdent(aktienregisterIdent, true);
                int meldungsIdent=pDbBundle.dbMeldungen.meldungenArray[0].meldungsIdent;
                
                BlWillenserklaerung vmWillenserklaerung = new BlWillenserklaerung();
                vmWillenserklaerung.pErteiltAufWeg = KonstWillenserklaerungWeg.counterAufHV;

                vmWillenserklaerung.piMeldungsIdentAktionaer = meldungsIdent;
                vmWillenserklaerung.pWillenserklaerungGeberIdent = -1; /*Aktionär*/

                EclPersonenNatJur personNatJur = new EclPersonenNatJur();

                personNatJur.ident = 0; /*Neue Person*/
                personNatJur.vorname = lAktienregisterErgaenzung.ergaenzungLangString[25];
                personNatJur.name = lAktienregisterErgaenzung.ergaenzungLangString[24];
                personNatJur.ort = lAktienregisterErgaenzung.ergaenzungLangString[26];

                vmWillenserklaerung.pEclPersonenNatJur = personNatJur;

                vmWillenserklaerung.vollmachtAnDritte(pDbBundle);
                if (vmWillenserklaerung.rcIstZulaessig == false) {
                    CaBug.drucke("vmWillenserklaerung.rcGrundFuerUnzulaessig="+vmWillenserklaerung.rcGrundFuerUnzulaessig);
                }
               
                
//                EclPersonenNatJur lPersonNatJur = new EclPersonenNatJur();
//                lPersonNatJur.name = lAktienregisterErgaenzung.ergaenzungLangString[24];
//                lPersonNatJur.vorname = lAktienregisterErgaenzung.ergaenzungLangString[25];
//                lPersonNatJur.ort = lAktienregisterErgaenzung.ergaenzungLangString[26];
//                lPersonNatJur.mandant = lAktienregisterErgaenzung.mandant;
//                eclDbM.getDbBundle().dbPersonenNatJur.insert(lPersonNatJur);
//
//                lAktienregisterErgaenzung.ergaenzungKennzeichen[24] = lPersonNatJur.ident;
//                eclDbM.getDbBundle().dbAktienregisterErgaenzung.update(lAktienregisterErgaenzung);
            }
        }

    }

    
    public void ku217AbstimmendePraesentSetzen(DbBundle pDbBundle, int pAbstimmungsvorgang) {
        
        pDbBundle.dbAbstimmungMeldungRaw.lese_allNichtVerarbeitet(pAbstimmungsvorgang);
        for (int i=0;i<pDbBundle.dbAbstimmungMeldungRaw.anzErgebnisGefunden();i++) {
            int meldungsIdent=pDbBundle.dbAbstimmungMeldungRaw.ergebnisGefunden(i).meldungsIdent;
            if (meldungsIdent!=0) {
                pDbBundle.dbMeldungen.leseZuIdent(meldungsIdent);
                EclMeldung eclMeldung=pDbBundle.dbMeldungen.meldungenArray[0];
                eclMeldung.statusWarPraesenz=1;
                eclMeldung.statusPraesenz=1;
                eclMeldung.statusWarPraesenz_Delayed=1;
                eclMeldung.statusPraesenz_Delayed=1;
                pDbBundle.dbMeldungen.update(eclMeldung, false);
            }
        }
    }
   
    
    public void anlegenPublikationenTest(DbBundle pDbBundle) {
        EclPublikation publikation = null;

        publikation = new EclPublikation();
        publikation.position = 0;
        publikation.bezeichnung = "Newsletter";
        publikation.publikationenZustellung[0] = 1;
        pDbBundle.dbPublikation.insert(publikation);

        publikation = new EclPublikation();
        publikation.position = 1;
        publikation.bezeichnung = "Geschäftsbericht kurz";
        publikation.publikationenZustellung[0] = 1;
        publikation.publikationenZustellung[1] = 1;
        pDbBundle.dbPublikation.insert(publikation);

        publikation = new EclPublikation();
        publikation.position = 0;
        publikation.bezeichnung = "Geschäftsbericht lang";
        publikation.publikationenZustellung[1] = 1;
        pDbBundle.dbPublikation.insert(publikation);

    }

    public void anlegenAbstimmungenTest(DbBundle pDbBundle, int pVariante) {
        switch (pVariante) {
        case 1:
            anlegenAbstimmungTestV1(pDbBundle);
            break;
        case 2:
            anlegenAbstimmungTestV1(pDbBundle);
            ergaenzenAbstimmungTestV1(pDbBundle);
            break;
        case 3:
            anlegenAbstimmungTestV3(pDbBundle);
            break;
        }
    }

    /**Variante 1 - relativ normale Tagesordnung*/
    private void anlegenAbstimmungTestV1(DbBundle pDbBundle) {
        EclAbstimmung abstimmung = null;

        abstimmung = new EclAbstimmung();

        abstimmung.nummer = "2.";
        abstimmung.anzeigeBezeichnungKurz = "Beschlussfassung über die Gewinnverwendung";
        abstimmung.kurzBezeichnung = "Gewinnverwendung";
        abstimmung.anzeigeBezeichnungLang = "Beschlussfassung über die Gewinnverwendung";
        abstimmung.nummerEN = "2.";
        abstimmung.anzeigeBezeichnungKurzEN = "Resolution on the appropriation of profits";
        abstimmung.anzeigeBezeichnungLangEN = "Resolution on the appropriation of profits";
        abstimmung.aktiv = 1;
        abstimmung.aktivWeisungenInPortal = 1;
        abstimmung.anzeigePositionExternWeisungen = 2;
        abstimmung.externJa = 1;
        abstimmung.externNein = 1;
        abstimmung.externEnthaltung = 1;
        abstimmung.internJa = 1;
        abstimmung.internNein = 1;
        abstimmung.internEnthaltung = 1;
        abstimmung.internUngueltig = 1;
        abstimmung.identWeisungssatz = 1;
        abstimmung.gegenantrag = 0;
        abstimmung.formularKurz = 1;
        abstimmung.formularLang = 2;
        abstimmung.stimmenAuswerten = -1;
        abstimmung.stimmberechtigteGattungen[0] = 1;
        pDbBundle.dbAbstimmungen.insert(abstimmung);

        abstimmung.nummer = "3.";
        abstimmung.anzeigeBezeichnungKurz = "Entlastung der Mitglieder des Vorstands";
        abstimmung.kurzBezeichnung = abstimmung.anzeigeBezeichnungKurz;
        abstimmung.anzeigeBezeichnungLang = "Beschlussfassung über die Entlastung der Mitglieder des Vorstands";
        abstimmung.nummerEN = "3.";
        abstimmung.anzeigeBezeichnungKurzEN = "Ratification of the actions of the Board of Executive Directors";
        abstimmung.anzeigeBezeichnungLangEN = "Adoption of a resolution on the ratification of the actions of the Board of Executive Directors";
        abstimmung.aktiv = 1;
        abstimmung.aktivWeisungenInPortal = 1;
        abstimmung.anzeigePositionExternWeisungen = 3;
        abstimmung.externJa = 1;
        abstimmung.externNein = 1;
        abstimmung.externEnthaltung = 1;
        abstimmung.internJa = 1;
        abstimmung.internNein = 1;
        abstimmung.internEnthaltung = 1;
        abstimmung.identWeisungssatz = 2;
        abstimmung.gegenantrag = 0;
        abstimmung.formularKurz = 1;
        abstimmung.formularLang = 2;
        abstimmung.stimmenAuswerten = -1;
        pDbBundle.dbAbstimmungen.insert(abstimmung);

        abstimmung.nummer = "4.";
        abstimmung.anzeigeBezeichnungKurz = "Entlastung der Mitglieder des Aufsichtsrats";
        abstimmung.kurzBezeichnung = "Entlastung Aufsichtsrats";
        abstimmung.anzeigeBezeichnungLang = "Beschlussfassung über die Entlastung der Mitglieder des Aufsichtsrats";
        abstimmung.nummerEN = "4.";
        abstimmung.anzeigeBezeichnungKurzEN = "Ratification of the actions of the Supervisory Board";
        abstimmung.anzeigeBezeichnungLangEN = "Adoption of a resolution on the ratification of the actions of the Supervisory Board";
        abstimmung.aktiv = 1;
        abstimmung.aktivWeisungenInPortal = 1;
        abstimmung.anzeigePositionExternWeisungen = 4;
        abstimmung.externJa = 1;
        abstimmung.externNein = 1;
        abstimmung.externEnthaltung = 1;
        abstimmung.internJa = 1;
        abstimmung.internNein = 1;
        abstimmung.internEnthaltung = 1;
        abstimmung.identWeisungssatz = 3;
        abstimmung.gegenantrag = 0;
        abstimmung.formularKurz = 1;
        abstimmung.formularLang = 2;
        abstimmung.stimmenAuswerten = -1;
        pDbBundle.dbAbstimmungen.insert(abstimmung);

        abstimmung.nummer = "5.";
        abstimmung.anzeigeBezeichnungKurz = "Wahl des Abschlussprüfers ";
        abstimmung.kurzBezeichnung = abstimmung.anzeigeBezeichnungKurz;
        abstimmung.anzeigeBezeichnungLang = "Wahl des Abschlussprüfers für das Geschäftsjahr 2017";
        abstimmung.nummerEN = "5.";
        abstimmung.anzeigeBezeichnungKurzEN = "Election of the auditor for the 2017 financial year";
        abstimmung.anzeigeBezeichnungLangEN = "Election of the auditor for the 2017 financial year";
        abstimmung.aktiv = 1;
        abstimmung.aktivWeisungenInPortal = 1;
        abstimmung.anzeigePositionExternWeisungen = 5;
        abstimmung.externJa = 1;
        abstimmung.externNein = 1;
        abstimmung.externEnthaltung = 1;
        abstimmung.internJa = 1;
        abstimmung.internNein = 1;
        abstimmung.internEnthaltung = 1;
        abstimmung.identWeisungssatz = 4;
        abstimmung.gegenantrag = 0;
        abstimmung.formularKurz = 1;
        abstimmung.formularLang = 2;
        abstimmung.stimmenAuswerten = -1;
        pDbBundle.dbAbstimmungen.insert(abstimmung);

        abstimmung.nummer = "6.";
        abstimmung.anzeigeBezeichnungKurz = "Wahl zum Aufsichtsrat";
        abstimmung.kurzBezeichnung = abstimmung.anzeigeBezeichnungKurz;
        abstimmung.anzeigeBezeichnungLang = "Wahl zum Aufsichtsrat";
        abstimmung.nummerEN = "6.";
        abstimmung.anzeigeBezeichnungKurzEN = "Elections to the Supervisory Board";
        abstimmung.anzeigeBezeichnungLangEN = "Elections to the Supervisory Board";
        abstimmung.aktiv = 1;
        abstimmung.aktivWeisungenInPortal = 1;
        abstimmung.anzeigePositionExternWeisungen = 6;
        abstimmung.externJa = 1;
        abstimmung.externNein = 1;
        abstimmung.externEnthaltung = 1;
        abstimmung.internJa = 1;
        abstimmung.internNein = 1;
        abstimmung.internEnthaltung = 1;
        abstimmung.identWeisungssatz = 5;
        abstimmung.gegenantrag = 0;
        abstimmung.formularKurz = 1;
        abstimmung.formularLang = 2;
        abstimmung.stimmenAuswerten = -1;
        pDbBundle.dbAbstimmungen.insert(abstimmung);

        abstimmung.nummer = "7.";
        abstimmung.anzeigeBezeichnungKurz = "Satzungsänderungen in § 11";
        abstimmung.kurzBezeichnung = abstimmung.anzeigeBezeichnungKurz;
        abstimmung.anzeigeBezeichnungLang = "Satzungsänderungen in § 11 Absatz 1 Satz 2 und Absatz 4 sowie in § 12 Absatz 6 und 8";
        abstimmung.nummerEN = "7.";
        abstimmung.anzeigeBezeichnungKurzEN = "Amendments to Article 11";
        abstimmung.anzeigeBezeichnungLangEN = "Amendments to Article 11 Paragraph 1 Sentence 2 and Paragraph 4 and to Article 12 Paragraph 6 and 8 of the Articles of Association";
        abstimmung.aktiv = 1;
        abstimmung.aktivWeisungenInPortal = 1;
        abstimmung.anzeigePositionExternWeisungen = 7;
        abstimmung.externJa = 1;
        abstimmung.externNein = 1;
        abstimmung.externEnthaltung = 1;
        abstimmung.internJa = 1;
        abstimmung.internNein = 1;
        abstimmung.internEnthaltung = 1;
        abstimmung.identWeisungssatz = -1;
        abstimmung.gegenantrag = 0;
        abstimmung.formularKurz = 1;
        abstimmung.formularLang = 2;
        abstimmung.stimmenAuswerten = -1;
        pDbBundle.dbAbstimmungen.insert(abstimmung);

        abstimmung.nummer = "";
        abstimmung.nummerindex = "a)";
        abstimmung.anzeigeBezeichnungKurz = "Absatz 1 Satz 2 der Satzung";
        abstimmung.kurzBezeichnung = abstimmung.anzeigeBezeichnungKurz;
        abstimmung.anzeigeBezeichnungLang = "Beschlussfassung über die Änderung von § 11 Absatz 1 Satz 2 der Satzung";
        abstimmung.nummerEN = "";
        abstimmung.nummerindexEN = "a)";
        abstimmung.anzeigeBezeichnungKurzEN = "Paragraph 1 Sentence 2 of the Articles of Association";
        abstimmung.anzeigeBezeichnungLangEN = "Resolution on the amendment of Article 11 Paragraph 1 Sentence 2 of the Articles of Association";
        abstimmung.aktiv = 1;
        abstimmung.aktivWeisungenInPortal = 1;
        abstimmung.anzeigePositionExternWeisungen = 8;
        abstimmung.externJa = 1;
        abstimmung.externNein = 1;
        abstimmung.externEnthaltung = 1;
        abstimmung.internJa = 1;
        abstimmung.internNein = 1;
        abstimmung.internEnthaltung = 1;
        abstimmung.identWeisungssatz = 7;
        abstimmung.gegenantrag = 0;
        abstimmung.formularKurz = 1;
        abstimmung.formularLang = 2;
        abstimmung.stimmenAuswerten = -1;
        pDbBundle.dbAbstimmungen.insert(abstimmung);

        abstimmung.nummer = "";
        abstimmung.nummerindex = "b)";
        abstimmung.anzeigeBezeichnungKurz = "Absatz 4 der Satzung";
        abstimmung.kurzBezeichnung = abstimmung.anzeigeBezeichnungKurz;
        abstimmung.anzeigeBezeichnungLang = "Beschlussfassung über die Änderung von § 11 Absatz 4 der Satzung";
        abstimmung.nummerEN = "";
        abstimmung.nummerindexEN = "b)";
        abstimmung.anzeigeBezeichnungKurzEN = "Paragraph 4 of the Articles of Association";
        abstimmung.anzeigeBezeichnungLangEN = "Resolution on the amendment of Article 11 Paragraph 4 of the Articles of Association";
        abstimmung.aktiv = 1;
        abstimmung.aktivWeisungenInPortal = 1;
        abstimmung.anzeigePositionExternWeisungen = 9;
        abstimmung.externJa = 1;
        abstimmung.externNein = 1;
        abstimmung.externEnthaltung = 1;
        abstimmung.internJa = 1;
        abstimmung.internNein = 1;
        abstimmung.internEnthaltung = 1;
        abstimmung.identWeisungssatz = 8;
        abstimmung.gegenantrag = 0;
        abstimmung.formularKurz = 1;
        abstimmung.formularLang = 2;
        abstimmung.stimmenAuswerten = -1;
        pDbBundle.dbAbstimmungen.insert(abstimmung);

        abstimmung.nummer = "";
        abstimmung.nummerindex = "c)";
        abstimmung.anzeigeBezeichnungKurz = " Absatz 6 und 8 der Satzung";
        abstimmung.kurzBezeichnung = abstimmung.anzeigeBezeichnungKurz;
        abstimmung.anzeigeBezeichnungLang = "Beschlussfassung über die Änderung von § 12 Absatz 6 und 8 der Satzung";
        abstimmung.nummerEN = "";
        abstimmung.nummerindexEN = "c)";
        abstimmung.anzeigeBezeichnungKurzEN = "Paragraph 6 and 8 of the Articles of Association";
        abstimmung.anzeigeBezeichnungLangEN = "Resolution on the amendment of Article 12 Paragraph 6 and 8 of the Articles of Association";
        abstimmung.aktiv = 1;
        abstimmung.aktivWeisungenInPortal = 1;
        abstimmung.anzeigePositionExternWeisungen = 10;
        abstimmung.externJa = 1;
        abstimmung.externNein = 1;
        abstimmung.externEnthaltung = 1;
        abstimmung.internJa = 1;
        abstimmung.internNein = 1;
        abstimmung.internEnthaltung = 1;
        abstimmung.identWeisungssatz = 9;
        abstimmung.gegenantrag = 0;
        abstimmung.formularKurz = 1;
        abstimmung.formularLang = 2;
        abstimmung.stimmenAuswerten = -1;
        pDbBundle.dbAbstimmungen.insert(abstimmung);

    }

    /**Variante 1 - relativ normale Tagesordnung*/
    private void ergaenzenAbstimmungTestV1(DbBundle pDbBundle) {
        
        /*Hier gewünschte Tagesordnung für Test-Standard anlegen*/

    }

    private void anlegenStimmblock54(DbBundle pDbBundle) {
        EclAbstimmungenZuStimmkarte lAbstimmungenZuStimmkarte = null;
        int posOffset = 0;

        /******************Stimmkarte M************************/
        EclStimmkarteInhalt lStimmkarteInhalt = new EclStimmkarteInhalt();
        lStimmkarteInhalt.stimmkartenNr = 1;
        lStimmkarteInhalt.posInBlock = 1;
        lStimmkarteInhalt.stimmkartenBezeichnung = "M";
        lStimmkarteInhalt.stimmkarteIstAktiv = 1;
        pDbBundle.dbStimmkarteInhalt.insert(lStimmkarteInhalt);

        posOffset = 0;
        for (int i = 1; i <= 9; i++) {
            lAbstimmungenZuStimmkarte = new EclAbstimmungenZuStimmkarte();
            lAbstimmungenZuStimmkarte.stimmkartenNr = 1;
            lAbstimmungenZuStimmkarte.identAbstimmung = i;
            lAbstimmungenZuStimmkarte.identAbstimmungAufKarte = i;
            lAbstimmungenZuStimmkarte.positionInStimmkarte = i - posOffset;
            pDbBundle.dbAbstimmungenZuStimmkarte.insert(lAbstimmungenZuStimmkarte);
        }

        /******************Stimmkarte V************************/

        lStimmkarteInhalt = new EclStimmkarteInhalt();
        lStimmkarteInhalt.stimmkartenNr = 2;
        lStimmkarteInhalt.posInBlock = 2;
        lStimmkarteInhalt.stimmkartenBezeichnung = "V";
        lStimmkarteInhalt.stimmkarteIstAktiv = 1;
        pDbBundle.dbStimmkarteInhalt.insert(lStimmkarteInhalt);

        posOffset = 10;
        for (int i = 11; i <= 15; i++) {
            lAbstimmungenZuStimmkarte = new EclAbstimmungenZuStimmkarte();
            lAbstimmungenZuStimmkarte.stimmkartenNr = 2;
            lAbstimmungenZuStimmkarte.identAbstimmung = i;
            lAbstimmungenZuStimmkarte.identAbstimmungAufKarte = i;
            lAbstimmungenZuStimmkarte.positionInStimmkarte = i - posOffset;
            pDbBundle.dbAbstimmungenZuStimmkarte.insert(lAbstimmungenZuStimmkarte);
        }

        /****************Stimmkarte A1*********************************************/
        lStimmkarteInhalt = new EclStimmkarteInhalt();
        lStimmkarteInhalt.stimmkartenNr = 3;
        lStimmkarteInhalt.posInBlock = 3;
        lStimmkarteInhalt.stimmkartenBezeichnung = "A1";
        lStimmkarteInhalt.stimmkarteIstAktiv = 1;
        pDbBundle.dbStimmkarteInhalt.insert(lStimmkarteInhalt);

        posOffset = 16;
        for (int i = 17; i <= 25; i++) {
            lAbstimmungenZuStimmkarte = new EclAbstimmungenZuStimmkarte();
            lAbstimmungenZuStimmkarte.stimmkartenNr = 3;
            lAbstimmungenZuStimmkarte.identAbstimmung = i;
            lAbstimmungenZuStimmkarte.identAbstimmungAufKarte = i;
            lAbstimmungenZuStimmkarte.positionInStimmkarte = i - posOffset;
            pDbBundle.dbAbstimmungenZuStimmkarte.insert(lAbstimmungenZuStimmkarte);
        }

        /****************Stimmkarte A2*********************************************/
        lStimmkarteInhalt = new EclStimmkarteInhalt();
        lStimmkarteInhalt.stimmkartenNr = 4;
        lStimmkarteInhalt.posInBlock = 4;
        lStimmkarteInhalt.stimmkartenBezeichnung = "A2";
        lStimmkarteInhalt.stimmkarteIstAktiv = 1;
        pDbBundle.dbStimmkarteInhalt.insert(lStimmkarteInhalt);

        posOffset = 26;
        for (int i = 26; i <= 33; i++) {
            lAbstimmungenZuStimmkarte = new EclAbstimmungenZuStimmkarte();
            lAbstimmungenZuStimmkarte.stimmkartenNr = 4;
            lAbstimmungenZuStimmkarte.identAbstimmung = i;
            lAbstimmungenZuStimmkarte.identAbstimmungAufKarte = i;
            lAbstimmungenZuStimmkarte.positionInStimmkarte = i - posOffset;
            pDbBundle.dbAbstimmungenZuStimmkarte.insert(lAbstimmungenZuStimmkarte);
        }

        /****************Stimmkarte S1*********************************************/
        lStimmkarteInhalt = new EclStimmkarteInhalt();
        lStimmkarteInhalt.stimmkartenNr = 5;
        lStimmkarteInhalt.posInBlock = 5;
        lStimmkarteInhalt.stimmkartenBezeichnung = "S1";
        lStimmkarteInhalt.stimmkarteIstAktiv = 1;
        pDbBundle.dbStimmkarteInhalt.insert(lStimmkarteInhalt);

        posOffset = 33;
        for (int i = 34; i <= 43; i++) {
            lAbstimmungenZuStimmkarte = new EclAbstimmungenZuStimmkarte();
            lAbstimmungenZuStimmkarte.stimmkartenNr = 5;
            lAbstimmungenZuStimmkarte.identAbstimmung = i;
            lAbstimmungenZuStimmkarte.identAbstimmungAufKarte = i;
            lAbstimmungenZuStimmkarte.positionInStimmkarte = i - posOffset;
            pDbBundle.dbAbstimmungenZuStimmkarte.insert(lAbstimmungenZuStimmkarte);
        }

        /****************Stimmkarte S2*********************************************/
        lStimmkarteInhalt = new EclStimmkarteInhalt();
        lStimmkarteInhalt.stimmkartenNr = 6;
        lStimmkarteInhalt.posInBlock = 6;
        lStimmkarteInhalt.stimmkartenBezeichnung = "S2";
        lStimmkarteInhalt.stimmkarteIstAktiv = 1;
        pDbBundle.dbStimmkarteInhalt.insert(lStimmkarteInhalt);

        posOffset = 43;
        for (int i = 44; i <= 53; i++) {
            lAbstimmungenZuStimmkarte = new EclAbstimmungenZuStimmkarte();
            lAbstimmungenZuStimmkarte.stimmkartenNr = 6;
            lAbstimmungenZuStimmkarte.identAbstimmung = i;
            lAbstimmungenZuStimmkarte.identAbstimmungAufKarte = i;
            lAbstimmungenZuStimmkarte.positionInStimmkarte = i - posOffset;
            pDbBundle.dbAbstimmungenZuStimmkarte.insert(lAbstimmungenZuStimmkarte);
        }

    }

    private void anlegenAbstimmungTestV3(DbBundle pDbBundle) {
        EclAbstimmung abstimmung = null;

        abstimmung = new EclAbstimmung();

        abstimmung.nummer = "2.";
        abstimmung.anzeigeBezeichnungKurz = "Verwendung des Bilanzgewinns";
        abstimmung.anzeigeBezeichnungLang = "Verwendung des Bilanzgewinns";
        abstimmung.nummerEN = "2.";
        abstimmung.anzeigeBezeichnungKurzEN = "Verwendung des Bilanzgewinns EN";
        abstimmung.anzeigeBezeichnungLangEN = "Verwendung des Bilanzgewinns EN";
        abstimmung.aktiv = 1;
        abstimmung.aktivWeisungenInPortal = 1;
        abstimmung.anzeigePositionExternWeisungen = 2;
        abstimmung.externJa = 1;
        abstimmung.externNein = 1;
        abstimmung.externEnthaltung = 1;
        abstimmung.identWeisungssatz = 1;
        abstimmung.gegenantrag = 0;
        pDbBundle.dbAbstimmungen.insert(abstimmung);

        abstimmung.nummer = "3.";
        abstimmung.anzeigeBezeichnungKurz = "Entlastung der Mitglieder des Vorstands";
        abstimmung.anzeigeBezeichnungLang = "Entlastung der Mitglieder des Vorstands";
        abstimmung.nummerEN = "3.";
        abstimmung.anzeigeBezeichnungKurzEN = "Entlastung der Mitglieder des Vorstands";
        abstimmung.anzeigeBezeichnungLangEN = "Entlastung der Mitglieder des Vorstands";
        abstimmung.aktiv = 1;
        abstimmung.aktivWeisungenInPortal = 1;
        abstimmung.anzeigePositionExternWeisungen = 3;
        abstimmung.externJa = 1;
        abstimmung.externNein = 1;
        abstimmung.externEnthaltung = 1;
        abstimmung.identWeisungssatz = 2;
        abstimmung.gegenantrag = 0;
        pDbBundle.dbAbstimmungen.insert(abstimmung);

        abstimmung.nummer = "4.";
        abstimmung.anzeigeBezeichnungKurz = "Entlastung der Mitglieder des Aufsichtsrats";
        abstimmung.anzeigeBezeichnungLang = "Entlastung der Mitglieder des Aufsichtsrats";
        abstimmung.nummerEN = "4.";
        abstimmung.anzeigeBezeichnungKurzEN = "Entlastung der Mitglieder des Aufsichtsrats";
        abstimmung.anzeigeBezeichnungLangEN = "Entlastung der Mitglieder des Aufsichtsrats";
        abstimmung.aktiv = 1;
        abstimmung.aktivWeisungenInPortal = 1;
        abstimmung.anzeigePositionExternWeisungen = 4;
        abstimmung.externJa = 1;
        abstimmung.externNein = 1;
        abstimmung.externEnthaltung = 1;
        abstimmung.identWeisungssatz = 3;
        abstimmung.gegenantrag = 0;
        pDbBundle.dbAbstimmungen.insert(abstimmung);

        abstimmung.nummer = "5.";
        abstimmung.anzeigeBezeichnungKurz = "Wahl des Abschlussprüfers / Konzernabschlussprüfers";
        abstimmung.anzeigeBezeichnungLang = "Wahl des Abschlussprüfers / Konzernabschlussprüfers";
        abstimmung.nummerEN = "5.";
        abstimmung.anzeigeBezeichnungKurzEN = "Wahl des Abschlussprüfers / Konzernabschlussprüfers";
        abstimmung.anzeigeBezeichnungLangEN = "Wahl des Abschlussprüfers / Konzernabschlussprüfers";
        abstimmung.aktiv = 1;
        abstimmung.aktivWeisungenInPortal = 1;
        abstimmung.anzeigePositionExternWeisungen = 5;
        abstimmung.externJa = 1;
        abstimmung.externNein = 1;
        abstimmung.externEnthaltung = 1;
        abstimmung.identWeisungssatz = 4;
        abstimmung.gegenantrag = 0;
        pDbBundle.dbAbstimmungen.insert(abstimmung);

        abstimmung.nummer = "6.";
        abstimmung.anzeigeBezeichnungKurz = "Satzungsänderung bzgl. Online-HV";
        abstimmung.anzeigeBezeichnungLang = "Satzungsänderung bzgl. Online-HV";
        abstimmung.nummerEN = "6.";
        abstimmung.anzeigeBezeichnungKurzEN = "Satzungsänderung bzgl. Online-HV";
        abstimmung.anzeigeBezeichnungLangEN = "Satzungsänderung bzgl. Online-HV";
        abstimmung.aktiv = 1;
        abstimmung.aktivWeisungenInPortal = 1;
        abstimmung.anzeigePositionExternWeisungen = 6;
        abstimmung.externJa = 1;
        abstimmung.externNein = 1;
        abstimmung.externEnthaltung = 1;
        abstimmung.identWeisungssatz = 5;
        abstimmung.gegenantrag = 0;
        pDbBundle.dbAbstimmungen.insert(abstimmung);

        abstimmung.nummer = "7.";
        abstimmung.anzeigeBezeichnungKurz = "Wahl des Aufsichtsrats";
        abstimmung.anzeigeBezeichnungLang = "Wahl des Aufsichtsrats";
        abstimmung.nummerEN = "7.";
        abstimmung.anzeigeBezeichnungKurzEN = "Wahl des Aufsichtsrats";
        abstimmung.anzeigeBezeichnungLangEN = "Wahl des Aufsichtsrats";
        abstimmung.aktiv = 1;
        abstimmung.aktivWeisungenInPortal = 1;
        abstimmung.anzeigePositionExternWeisungen = 7;
        abstimmung.externJa = 1;
        abstimmung.externNein = 1;
        abstimmung.externEnthaltung = 1;
        abstimmung.identWeisungssatz = -1;
        abstimmung.gegenantrag = 0;
        pDbBundle.dbAbstimmungen.insert(abstimmung);

        abstimmung.nummer = "";
        abstimmung.nummerindex = "a)";
        abstimmung.anzeigeBezeichnungKurz = "Frau Demomeier";
        abstimmung.anzeigeBezeichnungLang = "Frau Demomeier";
        abstimmung.nummerEN = "";
        abstimmung.nummerindexEN = "a)";
        abstimmung.anzeigeBezeichnungKurzEN = "Frau Demomeier";
        abstimmung.anzeigeBezeichnungLangEN = "Frau Demomeier";
        abstimmung.aktiv = 1;
        abstimmung.aktivWeisungenInPortal = 1;
        abstimmung.anzeigePositionExternWeisungen = 8;
        abstimmung.externJa = 1;
        abstimmung.externNein = 1;
        abstimmung.externEnthaltung = 1;
        abstimmung.identWeisungssatz = 7;
        abstimmung.gegenantrag = 0;
        pDbBundle.dbAbstimmungen.insert(abstimmung);

        abstimmung.nummer = "";
        abstimmung.nummerindex = "b)";
        abstimmung.anzeigeBezeichnungKurz = "Frau Musterhuber";
        abstimmung.anzeigeBezeichnungLang = "Frau Musterhuber";
        abstimmung.nummerEN = "";
        abstimmung.nummerindexEN = "b)";
        abstimmung.anzeigeBezeichnungKurzEN = "Frau Musterhuber";
        abstimmung.anzeigeBezeichnungLangEN = "Frau Musterhuber";
        abstimmung.aktiv = 1;
        abstimmung.aktivWeisungenInPortal = 1;
        abstimmung.anzeigePositionExternWeisungen = 9;
        abstimmung.externJa = 1;
        abstimmung.externNein = 1;
        abstimmung.externEnthaltung = 1;
        abstimmung.identWeisungssatz = 8;
        abstimmung.gegenantrag = 0;
        pDbBundle.dbAbstimmungen.insert(abstimmung);

        abstimmung.nummer = "";
        abstimmung.nummerindex = "c)";
        abstimmung.anzeigeBezeichnungKurz = "Herr Probehalber";
        abstimmung.anzeigeBezeichnungLang = "Herr Probehalber";
        abstimmung.nummerEN = "";
        abstimmung.nummerindexEN = "c)";
        abstimmung.anzeigeBezeichnungKurzEN = "Herr Probehalber";
        abstimmung.anzeigeBezeichnungLangEN = "Herr Probehalber";
        abstimmung.aktiv = 1;
        abstimmung.aktivWeisungenInPortal = 1;
        abstimmung.anzeigePositionExternWeisungen = 10;
        abstimmung.externJa = 1;
        abstimmung.externNein = 1;
        abstimmung.externEnthaltung = 1;
        abstimmung.identWeisungssatz = 9;
        abstimmung.gegenantrag = 0;
        pDbBundle.dbAbstimmungen.insert(abstimmung);

        abstimmung.nummer = "";
        abstimmung.nummerindex = "d)";
        abstimmung.anzeigeBezeichnungKurz = "Herr Testhausen";
        abstimmung.anzeigeBezeichnungLang = "Herr Testhausen";
        abstimmung.aktiv = 1;
        abstimmung.nummerEN = "";
        abstimmung.nummerindexEN = "d)";
        abstimmung.anzeigeBezeichnungKurzEN = "Herr Testhausen";
        abstimmung.anzeigeBezeichnungLangEN = "Herr Testhausen";
        abstimmung.aktivWeisungenInPortal = 1;
        abstimmung.anzeigePositionExternWeisungen = 11;
        abstimmung.externJa = 1;
        abstimmung.externNein = 1;
        abstimmung.externEnthaltung = 1;
        abstimmung.identWeisungssatz = 10;
        abstimmung.gegenantrag = 0;
        pDbBundle.dbAbstimmungen.insert(abstimmung);

        abstimmung.anzeigeBezeichnungKurz = "Gegenantrag A";
        abstimmung.anzeigeBezeichnungLang = "Gegenantrag A";
        abstimmung.aktiv = 1;
        abstimmung.aktivWeisungenInPortal = 1;
        abstimmung.anzeigePositionExternWeisungen = 12;
        abstimmung.externJa = 1;
        abstimmung.externNein = 1;
        abstimmung.externEnthaltung = 1;
        abstimmung.identWeisungssatz = 11;
        abstimmung.gegenantrag = 1;
        pDbBundle.dbAbstimmungen.insert(abstimmung);

        abstimmung.anzeigeBezeichnungKurz = "Gegenantrag B";
        abstimmung.anzeigeBezeichnungLang = "Gegenantrag B";
        abstimmung.aktiv = 1;
        abstimmung.aktivWeisungenInPortal = 1;
        abstimmung.anzeigePositionExternWeisungen = 13;
        abstimmung.externJa = 1;
        abstimmung.externNein = 1;
        abstimmung.externEnthaltung = 1;
        abstimmung.identWeisungssatz = 12;
        abstimmung.gegenantrag = 1;
        pDbBundle.dbAbstimmungen.insert(abstimmung);

    }

}
