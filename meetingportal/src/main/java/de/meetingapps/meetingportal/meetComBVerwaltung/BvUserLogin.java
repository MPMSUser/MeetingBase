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
import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComAllg.CaPasswortErzeugen;
import de.meetingapps.meetingportal.meetComAllg.CaPasswortVerschluesseln;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclUserLogin;
import de.meetingapps.meetingportal.meetComEntities.EclUserProfile;

/**User-Verwaltung / Login-Prozedere für "normale" User (keine Aktionäre)!*/
public class BvUserLogin {

    int passwortAbgelaufenNachTagen = 40;

    /**Wird von pruefeKennung gefüllt. Achtung: Passwort wird auf leer gesetzt!*/
    public EclUserLogin rcUserLogin = null;

    /**Für passwortVergessen: wenn gefüllt, dann enthält es das neue Passwort,
     * das an die übergebene E-Mail-Adresse verschickt werden muß.
     */
    public String rcNeuesPasswort="";
    
    /**
     * webOderClient
     * =1 => uLogin
     * =2 => betterMeeting
     * Prüfen, ob Kennung vorhanden, und diese einlesen / in rcUserLogin zurückgeben.
     * Mögliche Fehlermeldungen:
     * 
     * afFalscheKennung
     * afPasswortFalsch
     * afKennungGesperrt
     * afNeuesPasswortErforderlich (in diesem Fall ist rcUserLogin gefüllt, bei den anderen Fehlern nicht)
     * */
    public int pruefeKennung(DbBundle pDbBundle, String pKennung, String pPasswort, boolean pPruefePasswort,
            boolean pNurFuerMandant, int webOderClient) {

        int passwortUeberpruefen = 0;
        if (webOderClient == 1) {
            passwortUeberpruefen = pDbBundle.paramServer.pLocalInternPasswort;
        } else {
            passwortUeberpruefen = pDbBundle.paramServer.pLocalInternPasswortMeeting;
        }

        int erg;
        erg = pDbBundle.dbUserLogin.leseZuKennung(pKennung, pNurFuerMandant);

        if (erg < 1) {
            return CaFehler.afFalscheKennung;
        }

        rcUserLogin = pDbBundle.dbUserLogin.userLoginArray[0];
        if (rcUserLogin.kennungGesperrtDurchSoftware || rcUserLogin.kennungGesperrtManuell) {
            rcUserLogin = null;
            return CaFehler.afKennungGesperrt;
        }

        if (pPruefePasswort && rcUserLogin.pruefe_passwort(pPasswort, passwortUeberpruefen) == false) {
            rcUserLogin = null;
            return CaFehler.afPasswortFalsch;
        }

        if (pDbBundle.paramServer.passwortAgingEin == 1) {
            if (!rcUserLogin.passwortZuletztGeaendertAm.isEmpty()) {
                int difTage = CaDatumZeit.tageDifferenz(rcUserLogin.passwortZuletztGeaendertAm,
                        CaDatumZeit.DatumZeitStringFuerDatenbank());
                if (difTage > passwortAbgelaufenNachTagen) {
                    rcUserLogin.neuesPasswortErforderlich = true;
                }

            }
            if (rcUserLogin.neuesPasswortErforderlich) {
                rcUserLogin.passwort = "";
                return CaFehler.afNeuesPasswortErforderlich;
            }
        }

        ergaenzeRcUserLoginUmProfile(pDbBundle);
        
        rcUserLogin.passwort = "";

        return 1;
    }

    /**Für uLogin - da wird jede Kennung akzeptiert*/
    public int pruefeKennung(DbBundle pDbBundle, String pKennung, String pPasswort, boolean pPruefePasswort,
            int webOderClient) {

        int passwortUeberpruefen = 0;
        if (webOderClient == 1) {
            passwortUeberpruefen = pDbBundle.paramServer.pLocalInternPasswort;
        } else {
            passwortUeberpruefen = pDbBundle.paramServer.pLocalInternPasswortMeeting;
        }

        int erg;
        erg = pDbBundle.dbUserLogin.leseZuKennung(pKennung);

        if (erg < 1) {
            return CaFehler.afFalscheKennung;
        }

        rcUserLogin = pDbBundle.dbUserLogin.userLoginArray[0];
        if (rcUserLogin.kennungGesperrtDurchSoftware || rcUserLogin.kennungGesperrtManuell) {
            rcUserLogin = null;
            return CaFehler.afKennungGesperrt;
        }

        if (pPruefePasswort && rcUserLogin.pruefe_passwort(pPasswort, passwortUeberpruefen) == false) {
            rcUserLogin = null;
            return CaFehler.afPasswortFalsch;
        }

        if (pDbBundle.paramServer.passwortAgingEin == 1) {
            if (!rcUserLogin.passwortZuletztGeaendertAm.isEmpty()) {
                int difTage = CaDatumZeit.tageDifferenz(rcUserLogin.passwortZuletztGeaendertAm,
                        CaDatumZeit.DatumZeitStringFuerDatenbank());
                if (difTage > passwortAbgelaufenNachTagen) {
                    rcUserLogin.neuesPasswortErforderlich = true;
                }

            }
            if (rcUserLogin.neuesPasswortErforderlich) {
                rcUserLogin.passwort = "";
                return CaFehler.afNeuesPasswortErforderlich;
            }
        }

        ergaenzeRcUserLoginUmProfile(pDbBundle);

        rcUserLogin.passwort = "";

        return 1;
    }


    /**Kann auch von außen aufgerufen werden, wenn rcUserLogin vorbelegt wird*/
    public void ergaenzeRcUserLoginUmProfile(DbBundle pDbBundle) {
        /*Alle Profile einlesen, die dem User zugeordnet sind*/
        pDbBundle.dbUserProfile.readUser(rcUserLogin.userLoginIdent);
        List<EclUserProfile> zugeordneteUserProfile=pDbBundle.dbUserProfile.ergebnis();
        
        pDbBundle.dbUserLogin.profileVerarbeiten=true;
        if (zugeordneteUserProfile!=null) {
            for (int i=0;i<zugeordneteUserProfile.size();i++) {
                EclUserProfile lUserProfil=zugeordneteUserProfile.get(i);
                int rc=pDbBundle.dbUserLogin.leseZuIdent(lUserProfil.profilIdent);
                if (rc<1) {
                    CaBug.drucke("001");
                }
                else {
                    EclUserLogin lProfil=pDbBundle.dbUserLogin.userLoginGefunden(0);
                    for (int i1=0;i1<20;i1++) {
                        for (int i2=0;i2<20;i2++) {
                            if (lProfil.berechtigungen[i1][i2]==1) {
                                rcUserLogin.berechtigungen[i1][i2]=1;
                            }
                        }
                    }
                }
            }


        }
        pDbBundle.dbUserLogin.profileVerarbeiten=false;

    }
    
    /**
     * 
     * Neues Passwort muß mindestens 8 Zeilen sein.
     * Neues Passwort muß mindestens Sonderzeichen, Kleinbuchstaben, Großbuchstaben, Zahl beinhalten.
     * Neues Passwort darf nicht bereits verwendet sein (3x gespeichert)
     * 
     * Mögliche rc:
     * afFalscheKennung (eigentlich nicht möglich, wenn sauber programmiert wurde und vorher die Kennung verifiziert wurde)
     * afPasswortFalsch - das alte Passwort wurde falsch eingegeben
     * afPasswortZuKurz
     * afPasswortNichtSicher
     * afPasswortBereitsVerwendet - das neue Passwort wurde bereits verwendet
     * 
     * */
    public int setzeNeuesPasswort(DbBundle pDbBundle, EclUserLogin pUserLogin, String pAltesPasswort,
            String pNeuesPasswort) {

        /*Altes Passwort überprüfen*/
        boolean mitMandant = false;
        if (pUserLogin.mandant != 0) {
            mitMandant = true;
        }

        int erg;
        erg = pDbBundle.dbUserLogin.leseZuKennung(pUserLogin.kennung, mitMandant);
        if (erg < 1) {
            return CaFehler.afFalscheKennung;
        }

        rcUserLogin = pDbBundle.dbUserLogin.userLoginArray[0];

        if (rcUserLogin.pruefe_passwort(pAltesPasswort, pDbBundle.paramServer.pLocalInternPasswort) == false) {
            return CaFehler.afPasswortFalsch;
        }

        String passwortVerschluesselt = CaPasswortVerschluesseln.verschluesseln(pNeuesPasswort);

        if (pUserLogin.trivialPasswortZulaessig == false) {
            if (pNeuesPasswort.length() < 8) {
                return CaFehler.afPasswortZuKurz;
            }

            int gefSonderzeichen = 0;
            int gefGrossbuchstaben = 0;
            int gefKleinbuchsten = 0;
            int gefZahl = 0;

            for (int i = 0; i < pNeuesPasswort.length(); i++) {
                String zeichen = pNeuesPasswort.substring(i, i + 1);
                if ("!\"§$%&/()=?\\+*\'#-_.:,;<>|@".contains(zeichen)) {
                    gefSonderzeichen = 1;
                }
                if ("QWERTZUIOPÜASDFGHJKLÖÄYXCVBNM".contains(zeichen)) {
                    gefGrossbuchstaben = 1;
                }
                if ("qwertzuiopüasdfghjklöäyxcvbnmß".contains(zeichen)) {
                    gefKleinbuchsten = 1;
                }
                if ("1234567890".contains(zeichen)) {
                    gefZahl = 1;
                }
            }
            if (gefSonderzeichen == 0 || gefGrossbuchstaben == 0 || gefKleinbuchsten == 0 || gefZahl == 0) {
                return CaFehler.afPasswortNichtSicher;
            }

            if (passwortVerschluesselt.compareTo(rcUserLogin.passwort) == 0
                    || passwortVerschluesselt.compareTo(rcUserLogin.altesPasswort1) == 0
                    || passwortVerschluesselt.compareTo(rcUserLogin.altesPasswort2) == 0
                    || passwortVerschluesselt.compareTo(rcUserLogin.altesPasswort3) == 0) {
                return CaFehler.afPasswortBereitsVerwendet;
            }
        }

        /*Alles ok - nun verändertes Passwort speichern*/
        rcUserLogin.altesPasswort3 = rcUserLogin.altesPasswort2;
        rcUserLogin.altesPasswort2 = rcUserLogin.altesPasswort1;
        rcUserLogin.altesPasswort1 = rcUserLogin.passwort;
        rcUserLogin.neuesPasswortErforderlich = false;
        rcUserLogin.passwortZuletztGeaendertAm = CaDatumZeit.DatumZeitStringFuerDatenbank();
        rcUserLogin.passwort = passwortVerschluesselt;

        pDbBundle.dbUserLogin.update(rcUserLogin);

        return 1;
    }

    public void passwortVergessen(DbBundle pDbBundle, String pKennung, String pEmail) {
        rcNeuesPasswort="";
        int erg;
        erg = pDbBundle.dbUserLogin.leseZuKennung(pKennung);

        if (erg < 1) {
            return;
        }

        rcUserLogin = pDbBundle.dbUserLogin.userLoginArray[0];
        if (rcUserLogin.kennungGesperrtDurchSoftware || rcUserLogin.kennungGesperrtManuell) {
            return;
        }
        
        if (!rcUserLogin.email.trim().equalsIgnoreCase(pEmail.trim())) {
            return;
        }
        
        CaPasswortErzeugen caPasswortErzeugen=new CaPasswortErzeugen();
        rcNeuesPasswort=caPasswortErzeugen.generatePW(8, 0, 2, true);
        
        String passwortVerschluesselt = CaPasswortVerschluesseln.verschluesseln(rcNeuesPasswort);

        rcUserLogin.altesPasswort3 = rcUserLogin.altesPasswort2;
        rcUserLogin.altesPasswort2 = rcUserLogin.altesPasswort1;
        rcUserLogin.altesPasswort1 = rcUserLogin.passwort;
        rcUserLogin.neuesPasswortErforderlich = false;
        rcUserLogin.passwortZuletztGeaendertAm = CaDatumZeit.DatumZeitStringFuerDatenbank();
        rcUserLogin.passwort = passwortVerschluesselt;

        pDbBundle.dbUserLogin.update(rcUserLogin);
    }
    
    
    
}
