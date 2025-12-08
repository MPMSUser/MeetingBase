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
package de.meetingapps.meetingportal.meetComEclM;

import java.io.Serializable;

import de.meetingapps.meetingportal.meetComEntities.EclUserLogin;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

@SessionScoped
@Named
public class EclUserLoginM implements Serializable {
    private static final long serialVersionUID = 312493037026190010L;

    /**Kopie von EclUserLogin*/

    /**Wird von bvReload mitgeliefert und verwendet - nicht in Datenbank. reloadUserLogin*/
    private int reloadVersionUserLogin = 0;

    /**0 = Mandantenübergreifende Kennung*/
    private int mandant = 0;
    private int userLoginIdent = 0;
    private long db_version = 0;

    /**Länge 40*/
    private String kennung = "";

    /**Länge 10 => 64*/
    private String passwort = "";

    /**Länge 80*/
    private String name = "";

    /**Länge 40*/
    private String email = "";

    /**>0 -> gehört zu dem entsprechenden Insti (Mandantenübergreifend; d.h. mandant=0;) 
     * Achtung, Pseudo-Instis möglich - z.B. Proxy-Solicitator, siehe 
     * 
     * 0 -> undefiniert
     * -1 -> BO-Mitarbeiter, über Mail erreichbar (Mandantenübergreifend; d.h. mandant=0;)
     * -2 -> Emittent, über Mail erreichbar (Mandantenspezifisch; d.h. mandant!=0)
     * -3 -> Dritter, über Mail erreichbar (Mandantenspefzisch; d.h. mandant!=0)
     */
    private int gehoertZuInsti = 0;

    /** siehe EclUserLogin*/
    private int[][] berechtigungen = null;

    /**Neues Passwort muß beim nächsten Login vergeben werden (nicht wenn ServerAging auf aus steht)*/
    private boolean neuesPasswortErforderlich = false;

    /**Kennung wurde durch Mechanismus, z.b. mehrfache falsche Passworteingabe, gesperrt*/
    private boolean kennungGesperrtDurchSoftware = false;

    /**Kennung wurde manuell gesperrt*/
    private boolean kennungGesperrtManuell = false;

    /**Wenn zulässig, dann kein Passwort erforderlich, bzw. Passwort wird nicht auf
     * Trivialitäten abgefragt*/
    private boolean trivialPasswortZulaessig = false;

    /**Wenn wahr, dann handelt es sich um ein "Gruppen-Passwort" für die HV. Dient zur Selektion, um diese auf einem Server per Batch
     * alle zu aktivieren oder zu deaktivieren*/
    private boolean gruppenKennungHV = false;

    /**Len=19*/
    private String passwortZuletztGeaendertAm = "";

    /**Vorheriges Passwort
     * Len=64*/
    private String altesPasswort1 = "";

    /**VorVorheriges Passwort
     * Len=64*/
    private String altesPasswort2 = "";

    /**VorVorVorheriges Passwort
     * Len=64*/
    private String altesPasswort3 = "";

    /**Nicht in Datenbank vorhanden - nur als Selektierungsfeld für die Zuordnung der Profile zu Benutzer*/
    private boolean ausgewaehlt=false;

    
    public EclUserLoginM() {
        berechtigungen = new int[20][20];
    }

    public void copyFromEclUserLogin(EclUserLogin pEclUserLogin) {
        reloadVersionUserLogin = pEclUserLogin.reloadVersionUserLogin;
        mandant = pEclUserLogin.mandant;
        userLoginIdent = pEclUserLogin.userLoginIdent;
        db_version = pEclUserLogin.db_version;
        kennung = pEclUserLogin.kennung;
        passwort = pEclUserLogin.passwort;
        name = pEclUserLogin.name;
        email = pEclUserLogin.email;
        gehoertZuInsti = pEclUserLogin.gehoertZuInsti;
        berechtigungen = pEclUserLogin.berechtigungen;
        neuesPasswortErforderlich = pEclUserLogin.neuesPasswortErforderlich;
        kennungGesperrtDurchSoftware = pEclUserLogin.kennungGesperrtDurchSoftware;
        kennungGesperrtManuell = pEclUserLogin.kennungGesperrtManuell;
        trivialPasswortZulaessig = pEclUserLogin.trivialPasswortZulaessig;
        gruppenKennungHV = pEclUserLogin.gruppenKennungHV;
        passwortZuletztGeaendertAm = pEclUserLogin.passwortZuletztGeaendertAm;
        altesPasswort1 = pEclUserLogin.altesPasswort1;
        altesPasswort2 = pEclUserLogin.altesPasswort2;
        altesPasswort3 = pEclUserLogin.altesPasswort3;
    }

    /*************Zugriffs-Abfrage - 1:1 Copy aus EclUserLogin-Source**************/

    /*************[0]*****************************/
    public boolean pruefe_emittentenPortal() {
        if (berechtigungen[0][0] == 1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean pruefe_emittentenPortal_anmeldebestand() {
        if (berechtigungen[0][1] == 1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean pruefe_emittentenPortal_gaestemodul() {
        if (berechtigungen[0][2] == 1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean pruefe_emittentenPortal_gaestemodulGruppen() {
        if (berechtigungen[0][3] == 1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean pruefe_emittentenPortal_bestWorkflowBasis() {
        if (berechtigungen[0][4] == 1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean pruefe_emittentenPortal_bestWorkflowSpezial() {
        if (berechtigungen[0][5] == 1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean pruefe_emittentenPortal_bestWorkflowAdmin() {
        if (berechtigungen[0][6] == 1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean pruefe_emittentenPortal_bestBestandsabgleichBasis() {
        if (berechtigungen[0][7] == 1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean pruefe_emittentenPortal_bestBestandsabgleichSpezial() {
        if (berechtigungen[0][8] == 1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean pruefe_uportal_mailSenden() {
        if (berechtigungen[0][9] == 1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean pruefe_uportal_mailEmpfangen() {
        if (berechtigungen[0][10] == 1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean pruefe_uportal_dloginReports() {
        if (berechtigungen[0][11] == 1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean pruefe_uportal_dLoginStandardAdmin() {
        if (berechtigungen[0][12] == 1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean pruefe_uportal_dLoginHighAdmin() {
        if (berechtigungen[0][13] == 1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean pruefe_govVal_admin() {
        if (berechtigungen[0][14] == 1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean pruefe_govVal_insti() {
        if (berechtigungen[0][15] == 1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean pruefe_uportal_servicelinetools() {
        if (berechtigungen[0][16] == 1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean pruefe_uportal_virtuelleHV() {
        if (berechtigungen[0][17] == 1) {
            return true;
        } else {
            return false;
        }
    }

    
    public boolean pruefe_uportal_virtuelleHV_botschaftenLesen() {
        if (pruefe_uportal_virtuelleHV() || 
                pruefe_uportal_virtuelleHV_botschaftenBearbeiten() ||
                berechtigungen[0][18] == 1
                ) {
            return true;
        }
        else {
            return false;
        }
    }

    public boolean pruefe_uportal_virtuelleHV_botschaftenBearbeiten() {
        if (berechtigungen[0][19] == 1) {
            return true;
        } else {
            return false;
        }
    }

    
    /*******************[1]**********************/
    public boolean pruefe_mandantenBearbeiten_mandantenAnlegen() {
        if (berechtigungen[1][0] == 1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean pruefe_mandantenBearbeiten_fuerConsultant() {
        if (berechtigungen[1][1] == 1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean pruefe_mandantenBearbeiten_fuerAnmeldestellenservice() {
        if (berechtigungen[1][2] == 1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean pruefe_mandantenBearbeiten_fuerAnmeldestellenserviceLeitung() {
        if (berechtigungen[1][3] == 1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean pruefe_mandantenBearbeiten_fuerAdministrator() {
        if (berechtigungen[1][4] == 1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean pruefe_mandantenBearbeiten_fuerHotline() {
        if (berechtigungen[1][5] == 1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean pruefe_mandantenBearbeiten_alleMitP() {
        if (berechtigungen[1][9] == 1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean pruefe_mandantenBearbeiten_alle() {
        if (berechtigungen[1][10] == 1) {
            return true;
        } else {
            return false;
        }
    }

    /*******************[2]**********************/
    public boolean pruefe_modul_moduleHVMaster() {
        if (berechtigungen[2][0] == 1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean pruefe_modul_moduleFrontOffice() {
        if (berechtigungen[2][1] == 1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean pruefe_modul_moduleTabletAbstimmung() {
        if (berechtigungen[2][5] == 1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean pruefe_modul_moduleKontrolle() {
        if (berechtigungen[2][2] == 1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean pruefe_modul_moduleServiceDesk() {
        if (berechtigungen[2][3] == 1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean pruefe_modul_moduleTeilnahmeverzeichnis() {
        if (berechtigungen[2][4] == 1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean pruefe_modul_moduleBestandsverwaltung() {
        if (berechtigungen[2][6] == 1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean pruefe_modul_moduleHotline() {
        if (berechtigungen[2][7] == 1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean pruefe_modul_moduleAktienregisterImport() {
        if (berechtigungen[2][8] == 1) {
            return true;
        } else {
            return false;
        }
    }

    /*******************[3]**********************/

    public boolean pruefe_hvMaster_kennungsPflegeEingeschraenkt() {
        if (berechtigungen[3][0] == 1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean pruefe_hvMaster_kennungsPflegeVollstaendig() {
        if (berechtigungen[3][1] == 1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean pruefe_hvMaster_moduleDesigner() {
        if (berechtigungen[3][2] == 1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean pruefe_hvMaster_geraeteParameter() {
        if (berechtigungen[3][3] == 1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean pruefe_hvMaster_nummernformen() {
        if (berechtigungen[3][4] == 1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean pruefe_hvMaster_mandantenParameter() {
        if (berechtigungen[3][5] == 1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean pruefe_hvMaster_pflegeAbstimmungen() {
        if (berechtigungen[3][6] == 1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean pruefe_hvMaster_praesenzAbstimmung() {
        if (berechtigungen[3][7] == 1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean pruefe_hvMaster_entwicklerTools() {
        if (berechtigungen[3][8] == 1) {
            return true;
        } else {
            return false;
        }
    }

    /*******************[4] - Bestandsverwaltung**********************/

    /****************[5] - Div ***************************/
    public boolean pruefe_uportal_servicelinetools_passwort() {
        if (berechtigungen[5][0] == 1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean pruefe_uportal_mailKomplett() {
        if (berechtigungen[5][1] == 1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean pruefe_uportal_pflegePortaltexteMandant() {
        if (berechtigungen[5][2] == 1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean pruefe_uportal_pflegePortaltexteStandard() {
        if (berechtigungen[5][3] == 1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean pruefe_emittentenPortal_gaestemodulZusatzberechtigungen() {
        if (berechtigungen[5][4] == 1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean pruefe_uportal_mandantenauswahlErlaubt() {
        if (berechtigungen[5][5] == 1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean pruefe_uportal_rednerliste() {
        if (berechtigungen[5][6] == 1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean pruefe_uportal_abfrageRedakteur() {
        if (berechtigungen[5][7] == 1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean pruefe_schluessel_mandantenUebergreifend() {
        if (berechtigungen[5][8]==1) {
            return true;
        }
        else {
            return false;
        }
    }
    
    public boolean pruefe_schluessel_mandant() {
        if (berechtigungen[5][9]==1) {
            return true;
        }
        else {
            return false;
        }
    }
    
    public boolean pruefe_mitgliederverwaltung() {
        if (berechtigungen[5][10]==1) {
            return true;
        }
        else {
            return false;
        }
    }

    public boolean pruefe_veranstaltungReporting() {
        if (berechtigungen[5][11]==1) {
            return true;
        }
        else {
            return false;
        }
    }

    public boolean pruefe_veranstaltungAdministration() {
        if (berechtigungen[5][12]==1) {
            return true;
        }
        else {
            return false;
        }
    }

    public boolean pruefe_mandantenKennungen() {
        if (berechtigungen[5][13]==1) {
            return true;
        }
        else {
            return false;
        }
    }

    public boolean pruefe_uPortalSonderFunktionen() {
        if (berechtigungen[5][14]==1) {
            return true;
        }
        else {
            return false;
        }
    }

    public boolean pruefe_uPortalImportExportStandard() {
        if (berechtigungen[5][15]==1) {
            return true;
        }
        else {
            return false;
        }
    }

    public boolean pruefe_uPortalImportExportSonder() {
        if (berechtigungen[5][18]==1) {
            return true;
        }
        else {
            return false;
        }
    }

    public boolean pruefe_uPortalReportsSonder() {
        if (berechtigungen[5][16]==1) {
            return true;
        }
        else {
            return false;
        }
    }

    public boolean pruefe_uPortalDatenmanipulation() {
        if (berechtigungen[5][17]==1) {
            return true;
        }
        else {
            return false;
        }
    }

    public boolean pruefe_uPortalRuecksetzenDemoUser() {
        if (berechtigungen[5][19]==1) {
            return true;
        }
        else {
            return false;
        }
    }
    
    /*******************[6]**********************/
    public boolean pruefe_uportal_servicelinetools_registrierungsdaten() {
        if (berechtigungen[6][0] == 1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean pruefe_uportal_servicelinetools_anAbmeldenDialogveranstaltung() {
        if (berechtigungen[6][1] == 1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean pruefe_uportal_servicelinetools_anAbmeldenVeranstaltung() {
        if (berechtigungen[6][2] == 1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean pruefe_uportal_servicelinetools_auchVertreter() {
        if (berechtigungen[6][3] == 1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean pruefe_uportal_servicelinetools_auchUngepruefteVertreter() {
        if (berechtigungen[6][4] == 1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean pruefe_uportal_veranstaltungsmanagement_pflegen() {
        if (berechtigungen[6][5] == 1) {
            return true;
        } else {
            return false;
        }
    }
    
    public boolean pruefe_uportal_veranstaltungsmanagement_abfragen() {
        if (berechtigungen[6][6] == 1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean pruefe_uportal_verschiedenes_unterlagen() {
        if (berechtigungen[6][7] == 1) {
            return true;
        } else {
            return false;
        }
    }

    
    /******************Standard Getter- und Setter******************************/

    public int getReloadVersionUserLogin() {
        return reloadVersionUserLogin;
    }

    public void setReloadVersionUserLogin(int reloadVersionUserLogin) {
        this.reloadVersionUserLogin = reloadVersionUserLogin;
    }

    public int getMandant() {
        return mandant;
    }

    public void setMandant(int mandant) {
        this.mandant = mandant;
    }

    public int getUserLoginIdent() {
        return userLoginIdent;
    }

    public void setUserLoginIdent(int userLoginIdent) {
        this.userLoginIdent = userLoginIdent;
    }

    public long getDb_version() {
        return db_version;
    }

    public void setDb_version(long db_version) {
        this.db_version = db_version;
    }

    public String getKennung() {
        return kennung;
    }

    public void setKennung(String kennung) {
        this.kennung = kennung;
    }

    public String getPasswort() {
        return passwort;
    }

    public void setPasswort(String passwort) {
        this.passwort = passwort;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getGehoertZuInsti() {
        return gehoertZuInsti;
    }

    public void setGehoertZuInsti(int gehoertZuInsti) {
        this.gehoertZuInsti = gehoertZuInsti;
    }

    public int[][] getBerechtigungen() {
        return berechtigungen;
    }

    public void setBerechtigungen(int[][] berechtigungen) {
        this.berechtigungen = berechtigungen;
    }

    public boolean isNeuesPasswortErforderlich() {
        return neuesPasswortErforderlich;
    }

    public void setNeuesPasswortErforderlich(boolean neuesPasswortErforderlich) {
        this.neuesPasswortErforderlich = neuesPasswortErforderlich;
    }

    public boolean isKennungGesperrtDurchSoftware() {
        return kennungGesperrtDurchSoftware;
    }

    public void setKennungGesperrtDurchSoftware(boolean kennungGesperrtDurchSoftware) {
        this.kennungGesperrtDurchSoftware = kennungGesperrtDurchSoftware;
    }

    public boolean isKennungGesperrtManuell() {
        return kennungGesperrtManuell;
    }

    public void setKennungGesperrtManuell(boolean kennungGesperrtManuell) {
        this.kennungGesperrtManuell = kennungGesperrtManuell;
    }

    public boolean isTrivialPasswortZulaessig() {
        return trivialPasswortZulaessig;
    }

    public void setTrivialPasswortZulaessig(boolean trivialPasswortZulaessig) {
        this.trivialPasswortZulaessig = trivialPasswortZulaessig;
    }

    public boolean isGruppenKennungHV() {
        return gruppenKennungHV;
    }

    public void setGruppenKennungHV(boolean gruppenKennungHV) {
        this.gruppenKennungHV = gruppenKennungHV;
    }

    public String getPasswortZuletztGeaendertAm() {
        return passwortZuletztGeaendertAm;
    }

    public void setPasswortZuletztGeaendertAm(String passwortZuletztGeaendertAm) {
        this.passwortZuletztGeaendertAm = passwortZuletztGeaendertAm;
    }

    public String getAltesPasswort1() {
        return altesPasswort1;
    }

    public void setAltesPasswort1(String altesPasswort1) {
        this.altesPasswort1 = altesPasswort1;
    }

    public String getAltesPasswort2() {
        return altesPasswort2;
    }

    public void setAltesPasswort2(String altesPasswort2) {
        this.altesPasswort2 = altesPasswort2;
    }

    public String getAltesPasswort3() {
        return altesPasswort3;
    }

    public void setAltesPasswort3(String altesPasswort3) {
        this.altesPasswort3 = altesPasswort3;
    }

    public boolean isAusgewaehlt() {
        return ausgewaehlt;
    }

    public void setAusgewaehlt(boolean ausgewaehlt) {
        this.ausgewaehlt = ausgewaehlt;
    }

}
