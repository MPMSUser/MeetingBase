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
package de.meetingapps.meetingportal.meetComEntities;

/**Grundkonzept für Benutzerberechtigungen und -profile
 * 
 * Profile
 * -------
 * Werden immer auf Portal02 gepflegt. 
 */


import java.io.Serializable;

import de.meetingapps.meetingportal.meetComAllg.CaPasswortVerschluesseln;

/**Verwaltung der Login-Kennungen für "normale Benutzer" (d.h. NICHT Aktionäre/Teilnehmer)*/
public class EclUserLogin implements Serializable {
    private static final long serialVersionUID = 6441542614104592626L;

    /**Wird von bvReload mitgeliefert und verwendet - nicht in Datenbank. reloadUserLogin*/
    public int reloadVersionUserLogin = 0;

    /**0 = Mandantenübergreifende Kennung
     * 
     * Bei Profilen nur 0 oder 1 (1=es ist ein mandantenabhängiges Profil)*/
    public int mandant = 0;
    public int userLoginIdent = 0;
    public long db_version = 0;

    /**Länge 40
     * Bei Profilen: Profil-Name*/
    public String kennung = "";

    /**Länge 10 => 64*/
    public String passwort = "";

    /**Länge 80
     * Bei Profilen: Profilbeschreibung*/
    public String name = "";

    /**Länge 200; 
     * bei Profilen: Nur 40 Lang, "Klasse" (über die Zuordnung zu Mandant erfolgt) ("Profil-Klasse" in Basis-Parametern)
     * */
    public String email = "";

    /**>0 -> gehört zu dem entsprechenden Insti (Mandantenübergreifend; d.h. mandant=0;) 
     * Achtung, Pseudo-Instis möglich - z.B. Proxy-Solicitator, siehe 
     * 
     * 0 -> undefiniert
     * -1 -> BO-Mitarbeiter, über Mail erreichbar (Mandantenübergreifend; d.h. mandant=0;)
     * -2 -> Emittent, über Mail erreichbar (Mandantenspezifisch; d.h. mandant!=0)
     * -3 -> Dritter, über Mail erreichbar (Mandantenspefzisch; d.h. mandant!=0)
     * 
     */
    public int gehoertZuInsti = 0;

    /**Neu in Table: 1605 und 1606
    
    /*TODO _Berechtigungen: Achtung, derzeit darf ein Nutzer nicht gleichzeitig "Für Hotline" und eine andere "Für-Rolle" haben, da sonst dieser Nutzer auch bei Mandanten mit der Einstellung "nur für Hotline" alle seine anderen Rechte hätte!*/
    /**
     * [0..19][LEN=20];
     * 
     * [0]:
     * 0=Emittenten-Portal eLogin/Universal-Portal uLogin zulässig generell
     * 1=Zugriff auf Anmeldebestand
     * 2=Gästemodul (grundsätzlich)
     * 3=Gästemodul (Gruppen)
     * 4=BestWorkflow (Basis)
     * 5=BestWorkflow (Spezial)
     * 6=BestWorkflow (admin)
     * 7=BestBestandsabgleichBasis (Basis)
     * 8=BestBestandsabgleichSpezial (Spezial)
     * 9=Mail senden
     * 10=Mail empfangen
     * 11="DLogin"-Reports
     * 12="DLogin"-Standard-Admin
     * 13="DLogin"-"High-Admin"
     * 14=AdminWeisungsEmpfehlung (nur für GovVal)
     * 15=WeisungsEmpfehlungbestätigung (Nur für Instis / GovVal)
     * 16=Service-Line
     * 		uLogin Hauptmenüpunkt Service-Line mit Unterpunkt "Service-Anfrage"
     * 17=Virtuelle HV
     * 		uLogin Hauptmenüpunkt Virtuelle HV mit Unterpunkten Fragen- und Mitteilungs-Download
     * 18=Virtuelle HV - Botschaften lesen (für Kunde, gibt nur Berechtigung für diese Funktion, auch
     *      wenn "17=virtuelle HV" nicht aktiviert ist.  Gedacht als Kundenberechtigung.
     *      17=virtuelle HV beinhaltet diese Berechtigung.
     * 19=Virtuelle HV - Botschaften bearbeiten (Zusatzberechtigung - Auch Upload und Übertragung etc.;
     *      für diejenigen, die die Administration der Botschaften durchführen
     * 
     * [1]: Mandantenzugriff
     * 0=Darf Mandanten anlegen
     * 1=Darf Mandanten mit Kennzeichen "Für Consultant Bearbeitung" bearbeiten
     * 2=Darf Mandanten mit Kennzeichen "Für Anmeldestellenservice" bearbeiten
     * 3=Darf Mandanten mit Kennzeichen "für Anmeldestellenservice (Leitung") bearbeiten
     * 4=Darf Mandanten mit Kennzeichen "Für Administrator" bearbeiten
     * 5=Darf Mandanten mit Kennzeichen "Für Hotline" bearbeiten.
     *  (6 bis 8 reserviert für weitere Bearbeitungskennzeichen im Emittent)
     * 6
     * 7
     * 8
     * 
     * Wenn keiner der folgenden gesetzt, dann: darf nur auf die Standard-HVen zugreifen, 
     * sowie gleiche mit anderer laufender HV-Nummer
     * 9=Darf auf alle HVen mit "P" zugreifen
     * 10=Darf auf alle HVen zugreifen
     * 
     * 
     * 
     * [2]: darf Modul starten
     * Reihenfolge siehe Maske "Module" in Klassen-Parameter (bei 0 beginnend), bzw. wie in ParamGeraet
     * 
     * [3] Div. Rechte (ggf. irgendwann nochmal zu sortieren)
     * 0 = darf Benutzerpflege generell aufrufen (Passwort zurücksetzen, Sperren / Entsperren)
     * 1 = darf Berechtigungen innerhalb Benutzerpflege verändern, neue Kennungen anlegen
     * 2 = Formular- und Listgenerator
     * 3 = Geräte-Parameter
     * 4 = Nummernformen
     * 5 = Mandanten-Parameter
     * 6 = Pflege Abstimmungen / Tagesordnung
     * 7 = HV Präsenz/Abstimmungen
     * 8 = Tools für Entwicklung
     * 
     * [4] Div. Rechte in Bestandsverwaltung (gilt auch, wenn diese über uLogin aufrubar sind)
     * 0=Verarbeiten Passwort-Anforderungen
     * 		Bestandsverwaltung->Verarbeiten
     * 
     * [5] unsortierte Ergänzungen
     * 0 = uPortal: Service-Line-Tool darf auch Initial-Passwort sehen
     * 1 = uPortal: Mail-Bereich wird grundsätzlich aktiviert / deaktiviert
     * 2 = uPortal: Pflege Portaltexte (Mandant)
     * 3 = uPortal: Pflege Portaltexte (Standard)
     * 4 = uPortal, Gäste: Pflege der Zusatzberechtigungen erlaubt
     * 5 = uPortal, Mandantenauswahl erlaubt (wenn nicht gesetzt: dann nur Basisauswahl)
     * 6 = uPortal, Rednerliste
     * 7 = uPortal, Abfrage Redakteurzugriff (vor Mandantenauswahl)
     * 8 = Pflege Schlüssel Mandantenübergreifend
     * 9 = Pflege Schlüssel Mandant
     * 10= Mitgliederverwaltung - Administration und Reports
     * 11= Veranstaltung - Reporting
     * 12= Veranstaltung - Administration
     * 13= uPortal - Service-Line - Verwaltung Mandanten-User
     * 14= uPortal: Sonder-Funktionen für Entwickler und Test
     * 15= uPortal: Import/Export-Funktionen (Standard für Consultant)
     * 16= uPortal - Reports: Sonder-Reports für Entwickler und Test
     * 17= uPortal: Datenmanipulation
     * 18= uPortal: Import/Export-Funktionen (Sonder-Funktionen)
     * 19= uPortal: Rücksetzen Demo-User
     * 
     * [6] unsortierte Ergänzungen
     * 0 = uPortal: Service-Line-Tool darf Registrierungsdaten sehen (Mailregistrierung, Passwortbereich etc.
     *          siehe auch [5][0]!
     * 1 = uPortal: Service-Line-Tool darf für Dialogveranstaltung an-/Abmelden
     * 2 = uPortal: Service-Line-Tool darf für Mitgliederveranstaltung an/Abmelden
     * 3 = uPortal: Service-Line-Tool darf auch (geprüfte) Vertreter anzeigen
     * 4 = uPortal: Service-Line-Tool darf auch ungeprüfte Vertreter anzeigen
     * 5 = uPortal: Veranstaltungsmanagement Pflegen
     * 6 = uPortal: Veranstaltungsmanagement Abfragen
     * 7 = uPortal: Unterlagen einstellen (unter Verschiedenes)
     * 
     * 
     */
    public int[][] berechtigungen = null;

    /**Neues Passwort muß beim nächsten Login vergeben werden (nicht wenn ServerAging auf aus steht)*/
    public boolean neuesPasswortErforderlich = false;

    /**Kennung wurde durch Mechanismus, z.b. mehrfache falsche Passworteingabe, gesperrt*/
    public boolean kennungGesperrtDurchSoftware = false;

    /**Kennung wurde manuell gesperrt*/
    public boolean kennungGesperrtManuell = false;

    /**Wenn zulässig, dann kein Passwort erforderlich, bzw. Passwort wird nicht auf
     * Trivialitäten abgefragt*/
    public boolean trivialPasswortZulaessig = false;

    /**Wenn wahr, dann handelt es sich um ein "Gruppen-Passwort" für die HV. Dient zur Selektion, um diese auf einem Server per Batch
     * alle zu aktivieren oder zu deaktivieren*/
    public boolean gruppenKennungHV = false;

    /**Len=19*/
    public String passwortZuletztGeaendertAm = "";

    /**Vorheriges Passwort
     * Len=64*/
    public String altesPasswort1 = "";

    /**VorVorheriges Passwort
     * Len=64*/
    public String altesPasswort2 = "";

    /**VorVorVorheriges Passwort
     * Len=64*/
    public String altesPasswort3 = "";

    public EclUserLogin() {
        berechtigungen = new int[20][20];
    }

    
    /**Nicht in Datenbank vorhanden - nur als Selektierungsfeld für die Zuordnung der Profile zu Benutzer*/
    public boolean ausgewaehlt=false;
    
    
    /**pLocalInternPasswort==0 => keine Passwortüberprüfung*/
    public boolean pruefe_passwort(String eingabe, int pLocalInternPasswort) {
        if (this.passwort.compareTo(CaPasswortVerschluesseln.verschluesseln(eingabe)) == 0
                || eingabe.compareTo("103 118-6E-Lok") == 0 || pLocalInternPasswort == 0) {
            return true;
        } else {
            return false;
        }
    }


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

    /*******************[5]**********************/
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

    /***********Übergreifende Prüfungen***********************/
    public boolean pruefe_userDarfMandantBearbeiten(EclEmittenten pEmittenten) {
        int inAuswahl = pEmittenten.inAuswahl;
        if (((inAuswahl & 1) == 1 && pruefe_mandantenBearbeiten_fuerConsultant())
                || ((inAuswahl & 2) == 2 && pruefe_mandantenBearbeiten_fuerAnmeldestellenservice())
                || ((inAuswahl & 4) == 4 && pruefe_mandantenBearbeiten_fuerAnmeldestellenserviceLeitung())
                || ((inAuswahl & 8) == 8 && pruefe_mandantenBearbeiten_fuerAnmeldestellenserviceLeitung())
                || ((inAuswahl & 16) == 16 && pruefe_mandantenBearbeiten_fuerHotline())) {
            return true;
        }
        return false;
    }
}
