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

import java.io.Serializable;
import java.util.Objects;

/**Übersicht der Emittenten - speziell auch für Auswahl in der App*/
public class EclEmittenten implements Serializable {
    private static final long serialVersionUID = -8471115133367006611L;

    /**Wird von bvReload mitgeliefert und verwendet - nicht in Datenbank. reloadEmittenten*/
    public int reloadVersionEmittenten = 0;

    public int mandant = 0;

    /**2017, 2018 etc.. Ist redundant zum hvDatum, aber zur leichteren Selektion.
     * Bestandteil des Index
     */
    public int hvJahr = 0;

    /**Lfd. HV innerhalb des Jahres. A-Z zwecks besserer Lesbarkeit.
     * Bestandteil des Index
     * LEN=1
     */
    public String hvNummer = "A";

    /**Datenbank Art - dient zur Unterscheidung z.B. von Test, Produktion etc.
     * P = Produktion
     * S = Schulung
     * T/U/V/W = div. Testdatenbanken
     * Bestandteil des Index 
     * LEN=1
     */
    public String dbArt = "P";

    /**Code, der in App eingegeben werden muß, um diese HV anzuspringen (insbesondere für HVen, die nicht
     * in der App-Auswahl mit angezeigt werden)
     * LEN=10
     */
    public String hvCode = "";

    /**=0 => diese HV ist zwar grundsätzlich irgendwo vorhanden, aber nicht mehr in dieser Datenbank.*/
    public int inDbVorhanden = 1;

    public long db_version = 0;

    /**Len=80*/
    public String bezeichnungKurz = "";

    /**Len=200*/
    public String bezeichnungLang = "";

    /**(siehe auch die folgenden) Gibt an, welche Emittentenbezeichnung im jeweiligen Modul als Standard-Bezeichnung
     * verwendet werden soll.
     * 1 = Kurz verwenden, 2=Lang verwenden*/
    public int bezeichnungsArtApp = 1;
    public int bezeichnungsArtPortal = 1;
    public int bezeichnungsArtFormulare = 1;

    /**Len=10*/
    public String hvDatum = "";

    /**Len=50*/
    public String hvOrt = "";

    /*Die folgenden Parameter sind teilweise parallel zu den Mandanten-Parametern. Das ist aber auch gewollt - wegen schneller Selektion bei Mandantenauswahl*/

    /**Portal ist grundsätzlich vorhanden
     * Wird auch übernommen in paramModuleKonfigurierbar.aktionaersportal*/
    public int portalVorhanden = 0;

    /**Portal ist derzeit aktiv*/
    public int portalAktuellAktiv = 0;

    /**Standard-Mandant für Portal dieser Mandantennummer*/
    public int portalStandard = 0; //tfPortalStandard

    /**Dauerhaftes Portal*/
    public int portalIstDauerhaft = 0; //tfPortalIstDauerhaft

    /**Baiss für Ausgaben / Hinweise / Möglichkeiten, ob Mandant produktiv ist oder nicht. Entsprechend verknüpft:
     * 0/1 Testbestand/Produktionsbestand
     *      Hinweis für ku178: dieser Wert steuert auch, ob E-Mails aus der "Nicht-Test-Liste" ausgeblendet wird (Test)
     *      oder nicht (Produktion)
     *      
     * 2=Test-Funktionen (z.B. Datenmanipulation) sind gesperrt
     * 4=Muster auf Formularen ausgeblendet
     * 8=Muster auf Portal-Start-Seiten ausgeblendet (Login-Seite, Hinweise etc.)
     * 16=Muster auf "inneren" Portal-Seiten ausgeblendet
     * 32=Passwort-Vergessen-Schreiben werden gedruckt
     * 64=
     * 128=
     * 256=
     * 512=
     * 1024=
     * 2048=
     * 4096=
     * 8192=
     * 16384=
     * 32768=
     * 65536="Produktiv vorrangig" (wird nur für Parameter-Pflege benötigt)
     * 
     * Summe: 131071
     * 
     * */
    public int datenbestandIstProduktiv = 131071; //tfDatenbestandIstProduktiv, Basis-Parameter, 93

    public void belegeDatenbestandKomplettProduktiv() {
        datenbestandIstProduktiv=131071;
    }
    public void belegeDatenbestandKomplettTest() {
        datenbestandIstProduktiv=0;
    }
    
    public boolean liefereDatenbestandIstProduktiv() {
        return ((datenbestandIstProduktiv & 1) == 1);
    }
    public void belegeDatenbestandIstProduktiv(boolean pJa) {
        if (pJa) {datenbestandIstProduktiv=(datenbestandIstProduktiv | 1);}
        else {datenbestandIstProduktiv=(datenbestandIstProduktiv & (131071-1));}
    }
    
    public boolean liefereDatenbestandDatenmanipulationIstGesperrt() {
        return ((datenbestandIstProduktiv & 2) == 2);
    }
    public void belegeDatenbestandDatenmanipulationIstGesperrt(boolean pJa) {
        if (pJa) {datenbestandIstProduktiv=(datenbestandIstProduktiv | 2);}
        else {datenbestandIstProduktiv=(datenbestandIstProduktiv & (131071-2));}
    }

    public boolean liefereDatenbestandMusterAufFormularenAusgeblendet() {
        return ((datenbestandIstProduktiv & 4) == 4);
    }
    public void belegeDatenbestandMusterAufFormularenAusgeblendet(boolean pJa) {
        if (pJa) {datenbestandIstProduktiv=(datenbestandIstProduktiv | 4);}
        else {datenbestandIstProduktiv=(datenbestandIstProduktiv & (131071-4));}
    }

    public boolean liefereDatenbestandMusterAufPortalStartseitenAusgeblendet() {
        return ((datenbestandIstProduktiv & 8) == 8);
    }
    public void belegeDatenbestandMusterAufPortalStartseitenAusgeblendet(boolean pJa) {
        if (pJa) {datenbestandIstProduktiv=(datenbestandIstProduktiv | 8);}
        else {datenbestandIstProduktiv=(datenbestandIstProduktiv & (131071-8));}
    }

    public boolean liefereDatenbestandMusterAufPortalInnenseitenAusgeblendet() {
        return ((datenbestandIstProduktiv & 16) == 16);
    }
    public void belegeDatenbestandMusterAufPortalInnenseitenAusgeblendet(boolean pJa) {
        if (pJa) {datenbestandIstProduktiv=(datenbestandIstProduktiv | 16);}
        else {datenbestandIstProduktiv=(datenbestandIstProduktiv & (131071-16));}
    }

    public boolean liefereDatenbestandPasswortVergessenSchreibenDrucken() {
        return ((datenbestandIstProduktiv & 32) == 32);
    }
    public void belegeDatenbestandPasswortVergessenSchreibenDrucken(boolean pJa) {
        if (pJa) {datenbestandIstProduktiv=(datenbestandIstProduktiv | 32);}
        else {datenbestandIstProduktiv=(datenbestandIstProduktiv & (131071-32));}
    }

    public boolean liefereDatenbestandProduktivVorrangig() {
        return ((datenbestandIstProduktiv & 65536) == 65536);
    }

    /**Vorhandene Portal-Sprachen (analog auch für nachfolgende Sprach-Parameter):
     * 1 = Deutsch
     * 2 = Englisch
     * 4 = (etc.)
     */
    public int portalSprache = 1;

    public boolean lieferePortalIstMehrsprachig() {
        int anzPortalsprachen = 0;
        if ((portalSprache & 1) == 1) {
            anzPortalsprachen++;
        }
        if ((portalSprache & 2) == 2) {
            anzPortalsprachen++;
        }
        if (anzPortalsprachen > 1) {
            return true;
        }
        return false;
    }

    public boolean lieferePortalInEnglischVerfuegbar() {
        if ((portalSprache & 2) == 2) {
            return true;
        }
        return false;
    }

//    @Deprecated
//    /**0 = Verwendung individuelle XHTML; >0 => Verwendung der Standard XHTML in Sxxx
//      in table noch enthalten, aber Feld wird nicht mehr gefüllt.
//        */
//    public int vverwendeStandardXHTML = 0;

    /**App ist grundsätzlich verwendbar
     * 1 = App ist grundsätzlich verwendbar
     * 2 = Emittent wird in App-Auswahl angezeigt
     * Wird auch übernommen in paramModuleKonfigurierbar.hvApp*/
    public int appVorhanden = 0;

    /**App ist derzeit aktuell verwendbar*/
    public int appAktiv = 0;

    public int appSprache = 1;

    /**Emittenten-Portal grundsätzlich vorhanden
     * Wird auch übernommen in paramModuleKonfigurierbar.emittentenPortalVorhanden*/
    public int emittentenPortalVorhanden = 0;
    public int emittentenPortalAktiv = 0;
    public int emittentenPortalSprache = 0;

    /**Registeranbindung ist grundsätzlich vorhanden
     * Wird auch übernommen in paramModuleKonfigurierbar.registeranbindung*/
    public int registerAnbindungVorhanden = 0;
    public int registerAnbindungAktiv = 0;
    public int registerAnbindungSprache = 0;

    /**Emittent/HV ist für Veränderungen gesperrt*/
    public int dbGesperrt = 0;

    /**Berücksichtigung dieses Emittenten/HV in der Auswahl bei Programmstart
     * 1=Für Consultant-Bearbeitung
     * 2=Für Anmeldestellenservice (Erfassung)
     * 4=Für Anmeldestellenservice (Master)
     * 8=Für Administrator
     * 16=für Hotline
     * etc ...*/
    public int inAuswahl = 1;

    public boolean inAuswahlConsultant() {
        if ((inAuswahl & 1) == 1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean inAuswahlAnmeldestelleErfassung() {
        if ((inAuswahl & 2) == 2) {
            return true;
        } else {
            return false;
        }
    }

    public boolean inAuswahlAnmeldestelleMaster() {
        if ((inAuswahl & 4) == 4) {
            return true;
        } else {
            return false;
        }
    }

    public boolean inAuswahlAdministrator() {
        if ((inAuswahl & 8) == 8) {
            return true;
        } else {
            return false;
        }
    }

    public boolean inAuswahlHotline() {
        if ((inAuswahl & 16) == 16) {
            return true;
        } else {
            return false;
        }
    }

    /**Len=80*/
    public String pfadErgKundenOrdner = "";

    /**1=ExternerHoster alt, 2=ExternerHoster neu*/
    public int serverNummer = 1;

    /*****************************************Gesellschaftsdaten******************************************************/
    /**Len=100*/
    public String abteilung = "";
    /**Len=100*/
    public String strasseGesellschaft = "";
    /**Len=10*/
    public String postleitzahl = "";
    /**Len=100*/
    public String ort = "";
    public int staatId = 0;
    /**Len=100*/
    public String telefon = "";
    /**Len=100*/
    public String fax = "";
    /**Len=100*/
    public String email = "";

    /*****************************************HV-Daten*************************************************************/
    public int hvArtSchluessel = 0;
    /**Len=100*/
    public String veranstaltungStrasse = "";
    /**Len=10*/
    public String veranstaltungPostleitzahl = "";

    /*Ort der Veranstaltung - siehe hvOrt*/

    public int veranstaltungStaatId = 0;
    /**Len=100*/
    public String veranstaltungGebäude = "";
    /**Len=15*/
    public String kuerzelInhaberImportExport = "";

    public String holeBezeichnungFormulare() {
        if (bezeichnungsArtFormulare == 1) {
            return bezeichnungKurz;
        } else {
            return bezeichnungLang;
        }
    }

    public String holeBezeichnungApp() {
        if (bezeichnungsArtApp == 1) {
            return bezeichnungKurz;
        } else {
            return bezeichnungLang;
        }
    }

    
    /***************Felder nicht in Datenbank******************/
    /**Enthält eine Kopie von mandant, zur Abfrage ohne Änderungsmöglichkeit
     * in XHTML
     */
    public int mandantExtern=0;
    
    
    public EclEmittenten() {

    }

    public EclEmittenten(EclEmittenten emittent) {
        this.reloadVersionEmittenten = emittent.reloadVersionEmittenten;
        this.mandant = emittent.mandant;
        this.mandantExtern=emittent.mandant;
        this.hvJahr = emittent.hvJahr;
        this.hvNummer = emittent.hvNummer;
        this.dbArt = emittent.dbArt;
        this.hvCode = emittent.hvCode;
        this.inDbVorhanden = emittent.inDbVorhanden;
        this.db_version = emittent.db_version;
        this.bezeichnungKurz = emittent.bezeichnungKurz;
        this.bezeichnungLang = emittent.bezeichnungLang;
        this.bezeichnungsArtApp = emittent.bezeichnungsArtApp;
        this.bezeichnungsArtPortal = emittent.bezeichnungsArtPortal;
        this.bezeichnungsArtFormulare = emittent.bezeichnungsArtFormulare;
        this.hvDatum = emittent.hvDatum;
        this.hvOrt = emittent.hvOrt;
        this.portalVorhanden = emittent.portalVorhanden;
        this.portalAktuellAktiv = emittent.portalAktuellAktiv;
        this.portalStandard = emittent.portalStandard;
        this.portalIstDauerhaft = emittent.portalIstDauerhaft;
        this.datenbestandIstProduktiv = emittent.datenbestandIstProduktiv;
        this.portalSprache = emittent.portalSprache;
        this.appVorhanden = emittent.appVorhanden;
        this.appAktiv = emittent.appAktiv;
        this.appSprache = emittent.appSprache;
        this.emittentenPortalVorhanden = emittent.emittentenPortalVorhanden;
        this.emittentenPortalAktiv = emittent.emittentenPortalAktiv;
        this.emittentenPortalSprache = emittent.emittentenPortalSprache;
        this.registerAnbindungVorhanden = emittent.registerAnbindungVorhanden;
        this.registerAnbindungAktiv = emittent.registerAnbindungAktiv;
        this.registerAnbindungSprache = emittent.registerAnbindungSprache;
        this.dbGesperrt = emittent.dbGesperrt;
        this.inAuswahl = emittent.inAuswahl;
        this.pfadErgKundenOrdner = emittent.pfadErgKundenOrdner;
        this.serverNummer = emittent.serverNummer;
        this.abteilung = emittent.abteilung;
        this.strasseGesellschaft = emittent.strasseGesellschaft;
        this.postleitzahl = emittent.postleitzahl;
        this.ort = emittent.ort;
        this.staatId = emittent.staatId;
        this.telefon = emittent.telefon;
        this.fax = emittent.fax;
        this.email = emittent.email;
        this.hvArtSchluessel = emittent.hvArtSchluessel;
        this.veranstaltungStrasse = emittent.veranstaltungStrasse;
        this.veranstaltungPostleitzahl = emittent.veranstaltungPostleitzahl;
        this.veranstaltungStaatId = emittent.veranstaltungStaatId;
        this.veranstaltungGebäude = emittent.veranstaltungGebäude;
        this.kuerzelInhaberImportExport = emittent.kuerzelInhaberImportExport;
    }

    public int getReloadVersionEmittenten() {
        return reloadVersionEmittenten;
    }

    public void setReloadVersionEmittenten(int reloadVersionEmittenten) {
        this.reloadVersionEmittenten = reloadVersionEmittenten;
    }

    public int getMandant() {
        return mandant;
    }

    public void setMandant(int mandant) {
        this.mandant = mandant;
    }

    public int getHvJahr() {
        return hvJahr;
    }

    public void setHvJahr(int hvJahr) {
        this.hvJahr = hvJahr;
    }

    public String getHvNummer() {
        return hvNummer;
    }

    public void setHvNummer(String hvNummer) {
        this.hvNummer = hvNummer;
    }

    public String getDbArt() {
        return dbArt;
    }

    public void setDbArt(String dbArt) {
        this.dbArt = dbArt;
    }

    public String getHvCode() {
        return hvCode;
    }

    public void setHvCode(String hvCode) {
        this.hvCode = hvCode;
    }

    public int getInDbVorhanden() {
        return inDbVorhanden;
    }

    public void setInDbVorhanden(int inDbVorhanden) {
        this.inDbVorhanden = inDbVorhanden;
    }

    public long getDb_version() {
        return db_version;
    }

    public void setDb_version(long db_version) {
        this.db_version = db_version;
    }

    public String getBezeichnungKurz() {
        return bezeichnungKurz;
    }

    public void setBezeichnungKurz(String bezeichnungKurz) {
        this.bezeichnungKurz = bezeichnungKurz;
    }

    public String getBezeichnungLang() {
        return bezeichnungLang;
    }

    public void setBezeichnungLang(String bezeichnungLang) {
        this.bezeichnungLang = bezeichnungLang;
    }

    public int getBezeichnungsArtApp() {
        return bezeichnungsArtApp;
    }

    public void setBezeichnungsArtApp(int bezeichnungsArtApp) {
        this.bezeichnungsArtApp = bezeichnungsArtApp;
    }

    public int getBezeichnungsArtPortal() {
        return bezeichnungsArtPortal;
    }

    public void setBezeichnungsArtPortal(int bezeichnungsArtPortal) {
        this.bezeichnungsArtPortal = bezeichnungsArtPortal;
    }

    public int getBezeichnungsArtFormulare() {
        return bezeichnungsArtFormulare;
    }

    public void setBezeichnungsArtFormulare(int bezeichnungsArtFormulare) {
        this.bezeichnungsArtFormulare = bezeichnungsArtFormulare;
    }

    public String getHvDatum() {
        return hvDatum;
    }

    public void setHvDatum(String hvDatum) {
        this.hvDatum = hvDatum;
    }

    public String getHvOrt() {
        return hvOrt;
    }

    public void setHvOrt(String hvOrt) {
        this.hvOrt = hvOrt;
    }

    public int getPortalVorhanden() {
        return portalVorhanden;
    }

    public void setPortalVorhanden(int portalVorhanden) {
        this.portalVorhanden = portalVorhanden;
    }

    public int getPortalAktuellAktiv() {
        return portalAktuellAktiv;
    }

    public void setPortalAktuellAktiv(int portalAktuellAktiv) {
        this.portalAktuellAktiv = portalAktuellAktiv;
    }

    public int getPortalStandard() {
        return portalStandard;
    }

    public void setPortalStandard(int portalStandard) {
        this.portalStandard = portalStandard;
    }

    public int getPortalSprache() {
        return portalSprache;
    }

    public void setPortalSprache(int portalSprache) {
        this.portalSprache = portalSprache;
    }

    public int getAppVorhanden() {
        return appVorhanden;
    }

    public void setAppVorhanden(int appVorhanden) {
        this.appVorhanden = appVorhanden;
    }

    public int getAppAktiv() {
        return appAktiv;
    }

    public void setAppAktiv(int appAktiv) {
        this.appAktiv = appAktiv;
    }

    public int getAppSprache() {
        return appSprache;
    }

    public void setAppSprache(int appSprache) {
        this.appSprache = appSprache;
    }

    public int getEmittentenPortalVorhanden() {
        return emittentenPortalVorhanden;
    }

    public void setEmittentenPortalVorhanden(int emittentenPortalVorhanden) {
        this.emittentenPortalVorhanden = emittentenPortalVorhanden;
    }

    public int getEmittentenPortalAktiv() {
        return emittentenPortalAktiv;
    }

    public void setEmittentenPortalAktiv(int emittentenPortalAktiv) {
        this.emittentenPortalAktiv = emittentenPortalAktiv;
    }

    public int getEmittentenPortalSprache() {
        return emittentenPortalSprache;
    }

    public void setEmittentenPortalSprache(int emittentenPortalSprache) {
        this.emittentenPortalSprache = emittentenPortalSprache;
    }

    public int getRegisterAnbindungVorhanden() {
        return registerAnbindungVorhanden;
    }

    public void setRegisterAnbindungVorhanden(int registerAnbindungVorhanden) {
        this.registerAnbindungVorhanden = registerAnbindungVorhanden;
    }

    public int getRegisterAnbindungAktiv() {
        return registerAnbindungAktiv;
    }

    public void setRegisterAnbindungAktiv(int registerAnbindungAktiv) {
        this.registerAnbindungAktiv = registerAnbindungAktiv;
    }

    public int getRegisterAnbindungSprache() {
        return registerAnbindungSprache;
    }

    public void setRegisterAnbindungSprache(int registerAnbindungSprache) {
        this.registerAnbindungSprache = registerAnbindungSprache;
    }

    public int getDbGesperrt() {
        return dbGesperrt;
    }

    public void setDbGesperrt(int dbGesperrt) {
        this.dbGesperrt = dbGesperrt;
    }

    public int getInAuswahl() {
        return inAuswahl;
    }

    public void setInAuswahl(int inAuswahl) {
        this.inAuswahl = inAuswahl;
    }

    public String getPfadErgKundenOrdner() {
        return pfadErgKundenOrdner;
    }

    public void setPfadErgKundenOrdner(String pfadErgKundenOrdner) {
        this.pfadErgKundenOrdner = pfadErgKundenOrdner;
    }

    public int getServerNummer() {
        return serverNummer;
    }

    public void setServerNummer(int serverNummer) {
        this.serverNummer = serverNummer;
    }

    public String getAbteilung() {
        return abteilung;
    }

    public void setAbteilung(String abteilung) {
        this.abteilung = abteilung;
    }

    public String getStrasseGesellschaft() {
        return strasseGesellschaft;
    }

    public void setStrasseGesellschaft(String strasseGesellschaft) {
        this.strasseGesellschaft = strasseGesellschaft;
    }

    public String getPostleitzahl() {
        return postleitzahl;
    }

    public void setPostleitzahl(String postleitzahl) {
        this.postleitzahl = postleitzahl;
    }

    public String getOrt() {
        return ort;
    }

    public void setOrt(String ort) {
        this.ort = ort;
    }

    public int getStaatId() {
        return staatId;
    }

    public void setStaatId(int staatId) {
        this.staatId = staatId;
    }

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getHvArtSchluessel() {
        return hvArtSchluessel;
    }

    public void setHvArtSchluessel(int hvArtSchluessel) {
        this.hvArtSchluessel = hvArtSchluessel;
    }

    public String getVeranstaltungStrasse() {
        return veranstaltungStrasse;
    }

    public void setVeranstaltungStrasse(String veranstaltungStrasse) {
        this.veranstaltungStrasse = veranstaltungStrasse;
    }

    public String getVeranstaltungPostleitzahl() {
        return veranstaltungPostleitzahl;
    }

    public void setVeranstaltungPostleitzahl(String veranstaltungPostleitzahl) {
        this.veranstaltungPostleitzahl = veranstaltungPostleitzahl;
    }

    public int getVeranstaltungStaatId() {
        return veranstaltungStaatId;
    }

    public void setVeranstaltungStaatId(int veranstaltungStaatId) {
        this.veranstaltungStaatId = veranstaltungStaatId;
    }

    public String getVeranstaltungGebäude() {
        return veranstaltungGebäude;
    }

    public void setVeranstaltungGebäude(String veranstaltungGebäude) {
        this.veranstaltungGebäude = veranstaltungGebäude;
    }

    public String getKuerzelInhaberImportExport() {
        return kuerzelInhaberImportExport;
    }

    public void setKuerzelInhaberImportExport(String kuerzelInhaberImportExport) {
        this.kuerzelInhaberImportExport = kuerzelInhaberImportExport;
    }

    public int getPortalIstDauerhaft() {
        return portalIstDauerhaft;
    }

    public void setPortalIstDauerhaft(int portalIstDauerhaft) {
        this.portalIstDauerhaft = portalIstDauerhaft;
    }

    public int getDatenbestandIstProduktiv() {
        return datenbestandIstProduktiv;
    }

    public void setDatenbestandIstProduktiv(int datenbestandIstProduktiv) {
        this.datenbestandIstProduktiv = datenbestandIstProduktiv;
    }

    @Override
    public int hashCode() {
        return Objects.hash(abteilung, appAktiv, appSprache, appVorhanden, bezeichnungKurz, bezeichnungLang,
                bezeichnungsArtApp, bezeichnungsArtFormulare, bezeichnungsArtPortal, datenbestandIstProduktiv, dbArt,
                dbGesperrt, db_version, email, emittentenPortalAktiv, emittentenPortalSprache,
                emittentenPortalVorhanden, fax, hvArtSchluessel, hvCode, hvDatum, hvJahr, hvNummer, hvOrt, inAuswahl,
                inDbVorhanden, kuerzelInhaberImportExport, mandant, ort, pfadErgKundenOrdner, portalAktuellAktiv,
                portalIstDauerhaft, portalSprache, portalStandard, portalVorhanden, postleitzahl,
                registerAnbindungAktiv, registerAnbindungSprache, registerAnbindungVorhanden, reloadVersionEmittenten,
                serverNummer, staatId, strasseGesellschaft, telefon, veranstaltungGebäude, veranstaltungPostleitzahl,
                veranstaltungStaatId, veranstaltungStrasse);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        EclEmittenten other = (EclEmittenten) obj;
        return Objects.equals(abteilung, other.abteilung) && appAktiv == other.appAktiv
                && appSprache == other.appSprache && appVorhanden == other.appVorhanden
                && Objects.equals(bezeichnungKurz, other.bezeichnungKurz)
                && Objects.equals(bezeichnungLang, other.bezeichnungLang)
                && bezeichnungsArtApp == other.bezeichnungsArtApp
                && bezeichnungsArtFormulare == other.bezeichnungsArtFormulare
                && bezeichnungsArtPortal == other.bezeichnungsArtPortal
                && datenbestandIstProduktiv == other.datenbestandIstProduktiv && Objects.equals(dbArt, other.dbArt)
                && dbGesperrt == other.dbGesperrt && db_version == other.db_version
                && Objects.equals(email, other.email) && emittentenPortalAktiv == other.emittentenPortalAktiv
                && emittentenPortalSprache == other.emittentenPortalSprache
                && emittentenPortalVorhanden == other.emittentenPortalVorhanden && Objects.equals(fax, other.fax)
                && hvArtSchluessel == other.hvArtSchluessel && Objects.equals(hvCode, other.hvCode)
                && Objects.equals(hvDatum, other.hvDatum) && hvJahr == other.hvJahr
                && Objects.equals(hvNummer, other.hvNummer) && Objects.equals(hvOrt, other.hvOrt)
                && inAuswahl == other.inAuswahl && inDbVorhanden == other.inDbVorhanden
                && Objects.equals(kuerzelInhaberImportExport, other.kuerzelInhaberImportExport) && mandant == other.mandant
                && Objects.equals(ort, other.ort) && Objects.equals(pfadErgKundenOrdner, other.pfadErgKundenOrdner)
                && portalAktuellAktiv == other.portalAktuellAktiv && portalIstDauerhaft == other.portalIstDauerhaft
                && portalSprache == other.portalSprache && portalStandard == other.portalStandard
                && portalVorhanden == other.portalVorhanden && Objects.equals(postleitzahl, other.postleitzahl)
                && registerAnbindungAktiv == other.registerAnbindungAktiv
                && registerAnbindungSprache == other.registerAnbindungSprache
                && registerAnbindungVorhanden == other.registerAnbindungVorhanden
                && reloadVersionEmittenten == other.reloadVersionEmittenten && serverNummer == other.serverNummer
                && staatId == other.staatId && Objects.equals(strasseGesellschaft, other.strasseGesellschaft)
                && Objects.equals(telefon, other.telefon)
                && Objects.equals(veranstaltungGebäude, other.veranstaltungGebäude)
                && Objects.equals(veranstaltungPostleitzahl, other.veranstaltungPostleitzahl)
                && veranstaltungStaatId == other.veranstaltungStaatId
                && Objects.equals(veranstaltungStrasse, other.veranstaltungStrasse);
    }

    /**Kopieren:
     * > ohne mandant, hvJahr, hvNummer, dbArt, inDbVorhanden, db_version, serverNummer
     * > Folgende Parameter werden nicht kopiert, sondern auf den jeweiligen Wert gesetzt:
     * portalAktuellAktiv=0
     * portalStandard=0
     * datenbestandIstProduktiv=0;
     * appAktiv=0
     * emittentenPortalAktiv=0;
     * registerAnbindungAktiv=0;
     * dbGesperrt=0;
     * inAuswahl=1;
     * serverNummer=0;
     * */
    public void copyOhneKeyUndKritischeDatenFrom(EclEmittenten pEmittenten) {
        this.hvCode = pEmittenten.hvCode;
        this.bezeichnungKurz = pEmittenten.bezeichnungKurz;
        this.bezeichnungLang = pEmittenten.bezeichnungLang;
        this.bezeichnungsArtApp = pEmittenten.bezeichnungsArtApp;
        this.bezeichnungsArtPortal = pEmittenten.bezeichnungsArtPortal;
        this.bezeichnungsArtFormulare = pEmittenten.bezeichnungsArtFormulare;
        this.hvDatum = pEmittenten.hvDatum;
        this.hvOrt = pEmittenten.hvOrt;
        this.portalVorhanden = pEmittenten.portalVorhanden;
        this.portalAktuellAktiv = 0;
        this.portalStandard = 0;
        this.portalIstDauerhaft = pEmittenten.portalIstDauerhaft;
        this.datenbestandIstProduktiv = 0;
        this.portalSprache = pEmittenten.portalSprache;
//        this.verwendeStandardXHTML = pEmittenten.verwendeStandardXHTML;
        this.appVorhanden = pEmittenten.appVorhanden;
        this.appAktiv = 0;
        this.appSprache = pEmittenten.appSprache;
        this.emittentenPortalVorhanden = pEmittenten.emittentenPortalVorhanden;
        this.emittentenPortalAktiv = 0;
        this.emittentenPortalSprache = pEmittenten.emittentenPortalSprache;
        this.registerAnbindungVorhanden = pEmittenten.registerAnbindungVorhanden;
        this.registerAnbindungAktiv = 0;
        this.registerAnbindungSprache = pEmittenten.registerAnbindungSprache;
        this.dbGesperrt = 0;
        this.inAuswahl = 1;
        this.pfadErgKundenOrdner = pEmittenten.pfadErgKundenOrdner;
        this.serverNummer=0; 
        
        this.abteilung = pEmittenten.abteilung;
        this.strasseGesellschaft = pEmittenten.strasseGesellschaft;
        this.postleitzahl = pEmittenten.postleitzahl;
        this.ort = pEmittenten.ort;
        this.staatId = pEmittenten.staatId;
        this.telefon = pEmittenten.telefon;
        this.fax = pEmittenten.fax;
        this.email = pEmittenten.email;

        this.hvArtSchluessel = pEmittenten.hvArtSchluessel;
        this.veranstaltungStrasse = pEmittenten.veranstaltungStrasse;
        this.veranstaltungPostleitzahl = pEmittenten.veranstaltungPostleitzahl;
        this.veranstaltungStaatId = pEmittenten.veranstaltungStaatId;
        this.veranstaltungGebäude = pEmittenten.veranstaltungGebäude;
        this.kuerzelInhaberImportExport = pEmittenten.kuerzelInhaberImportExport;

    }

    @Override
    public String toString() {
        return "EclEmittenten [reloadVersionEmittenten=" + reloadVersionEmittenten + ", mandant=" + mandant
                + ", hvJahr=" + hvJahr + ", hvNummer=" + hvNummer + ", dbArt=" + dbArt + ", hvCode=" + hvCode
                + ", inDbVorhanden=" + inDbVorhanden + ", db_version=" + db_version + ", bezeichnungKurz="
                + bezeichnungKurz + ", bezeichnungLang=" + bezeichnungLang + ", bezeichnungsArtApp="
                + bezeichnungsArtApp + ", bezeichnungsArtPortal=" + bezeichnungsArtPortal
                + ", bezeichnungsArtFormulare=" + bezeichnungsArtFormulare + ", hvDatum=" + hvDatum + ", hvOrt=" + hvOrt
                + ", portalVorhanden=" + portalVorhanden + ", portalAktuellAktiv=" + portalAktuellAktiv
                + ", portalStandard=" + portalStandard + ", portalSprache=" + portalSprache + ", appVorhanden="
                + appVorhanden + ", appAktiv=" + appAktiv + ", appSprache=" + appSprache
                + ", emittentenPortalVorhanden=" + emittentenPortalVorhanden + ", emittentenPortalAktiv="
                + emittentenPortalAktiv + ", emittentenPortalSprache=" + emittentenPortalSprache
                + ", registerAnbindungVorhanden=" + registerAnbindungVorhanden + ", registerAnbindungAktiv="
                + registerAnbindungAktiv + ", registerAnbindungSprache=" + registerAnbindungSprache + ", dbGesperrt="
                + dbGesperrt + ", inAuswahl=" + inAuswahl + ", pfadErgKundenOrdner=" + pfadErgKundenOrdner
                + ", serverNummer=" + serverNummer + ", abteilung=" + abteilung + ", strasseGesellschaft="
                + strasseGesellschaft + ", postleitzahl=" + postleitzahl + ", ort=" + ort + ", staatId=" + staatId
                + ", telefon=" + telefon + ", fax=" + fax + ", email=" + email + ", hvArtSchluessel=" + hvArtSchluessel
                + ", veranstaltungStrasse=" + veranstaltungStrasse + ", veranstaltungPostleitzahl="
                + veranstaltungPostleitzahl + ", veranstaltungStaatId=" + veranstaltungStaatId
                + ", veranstaltungGebäude=" + veranstaltungGebäude + ", kuerzelInhaberImportExport=" + kuerzelInhaberImportExport + "]";
    }

    public String searchString() {
        return mandant + " " + bezeichnungKurz + " " + hvDatum + " " + veranstaltungGebäude + " " + veranstaltungStrasse
                + " " + veranstaltungPostleitzahl + " " + hvOrt;
    }
    public int getMandantExtern() {
        return mandantExtern;
    }
    public void setMandantExtern(int mandantExtern) {
        this.mandantExtern = mandantExtern;
    }
}
