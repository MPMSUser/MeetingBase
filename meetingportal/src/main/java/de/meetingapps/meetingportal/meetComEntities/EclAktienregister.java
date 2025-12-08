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
import java.util.Comparator;
import java.util.Objects;

/**
 * Aktienregistereintrag.
 */
public class EclAktienregister implements Serializable {
    private static final long serialVersionUID = 7487854579384497128L;

    /** Mandantennummer */
    public int mandant = 0;

    /**
     * eindeutiger Key für Aktienregistersatz (zusammen mit mandant), der
     * unveränderlich ist.
     */
    public int aktienregisterIdent = 0;

    /**
     * Versionsnummer - manuell nicht verändern! Ausschließlich durch Verwendung in
     * DbMeldungen
     */
    public long db_version;

    /**
     * Aktionärsnummer aus Aktienregister "Originäre" Aktienregisternummer wird
     * immer ergänzt mit 0 (E) und 1 (F) Length=20
     */
    public String aktionaersnummer = "";

    /**
     * Ist die personNatJur, die für Anmeldungen verwendet wird. Wenn noch keine
     * Anmeldung erfolgt ist, dann =0. Sobald Anmeldung erfolgt ist (oder sobald
     * eine Vollmacht empfangen wurde!) wird hier eine personNatJur vergeben, die
     * dann für jeden Meldesatz (auch wenn Anmeldungen komplett gelöscht wurden)
     * wiederverwendet wird.
     */
    public int personNatJur = 0;

    public int anredeId = 0;

    /**
     * Zur Verwendung der folgenden Felder: titel, nachname, vorname sind bei
     * natürlichen Personen (und Personengemeinschaften) gefüllt name1 name2 name3
     * bei juristischen Personen.
     */
    /** Len=80 */
    public String titel = "";

    /** len=200 */
    public String name3 = "";

    /** Len=80 */
    public String nachname = "";

    /** len=200 */
    public String name1 = "";

    /** Len=80 */
    public String vorname = "";

    /** len=200 */
    public String name2 = "";

    /** len=80*/
    public String zusatz = "";

    /** Len=80 */
    public String strasse = "";

    /** Len=20 */
    public String postfach = "";

    /** Len=20 */
    public String postleitzahl = "";

    /** Len=20 */
    public String postleitzahlPostfach = "";

    /** Len=80 */
    public String ort = "";

    public int staatId = 1;

    /**
     * EMail, falls aus Aktienregister geliefert. Wird hier einfach mal
     * reingeschrieben. Len=50
     */
    public String email = "";

    public int anredeIdVersand = 0;

    /** Len=80 */
    public String titelVersand = "";

    /** Len=200 */
    public String name3Versand = "";

    /** Len=80 */
    public String nachnameVersand = "";

    /** Len=200 */
    public String name1Versand = "";

    /** Len=80 */
    public String vornameVersand = "";

    /** Len=200 */
    public String name2Versand = "";

    /** len=80*/
    public String zusatzVersand = "";

    /** Len=80 */
    public String strasseVersand = "";

    /** Len=20 */
    public String postfachVersand = "";

    /** Len=20 */
    public String postleitzahlVersand = "";

    /** Len=20 */
    public String postleitzahlPostfachVersand = "";

    /** Len=80 */
    public String ortVersand = "";

    public int staatIdVersand = 1;

    /** Len=50 
     * "Sonderverwendung":
     * # (ggf. der E-Mail-Adresse vorangestellt): Aktionär ist neu, wurde noch nicht eingeladen*/
    public String emailVersand = "";

    /**
     * Übergreifendes Namensfeld, speziell zum Sortieren: bei natürlichen Personen:
     * Name, Titel Vorname bei juristischen Personen: name1 name2 name3 Len=200
     */
    public String nameKomplett = "";

    /**Wenn istPersonengemeinschaft==1, dann istJuristischePerson=0*/
    public int istPersonengemeinschaft = 0;

    public int istJuristischePerson = 0;

    public int versandAbweichend = 0;

    public int gattungId = 0;

    public int getGattungId() {
        if (gattungId == 0) {
            return 1;
        }
        return gattungId;
    }

    /**Die ISIN der Aktien dieses Aktionärs. Kann leer sein, dann gilt die
     * beim Emittenten als Standard zu dieser Gattung eingetragene ISIN
     * Len=12*/
    public String isin = "";

    /**Immer identisch zu stimmen*/
    public long stueckAktien = 0;

    /**Immer identisch zu stueckAktien*/
    public long stimmen = 0;

    /** Len=10 */
    public String besitzart = "";

    /** Len=13 */
    public String stimmausschluss = "";

    /**
     * Div. Verwendungen, u.a. für ku178: 2 + 3 + 4 = zwei Personen möglich
     */
    public int gruppe = 0;

    /**
     * Adresszeilen: eigentlich nur 1 bis 6 belegt. Aber von arfuehrer001 kommen 1 bis 10
     */
    /** Len=200 */
    public String adresszeile1 = "";

    /** Len=200 */
    public String adresszeile2 = "";

    /** Len=200 */
    public String adresszeile3 = "";

    /** Len=200 */
    public String adresszeile4 = "";

    /** Len=200 */
    public String adresszeile5 = "";

    /** Len=200 */
    public String adresszeile6 = "";

    /** Len=200 */
    public String adresszeile7 = "";

    /** Len=200 */
    public String adresszeile8 = "";

    /** Len=200 */
    public String adresszeile9 = "";

    /** Len=200 */
    public String adresszeile10 = "";

    public int englischerVersand = 0;

    /** Gibt an, wie / bzw. aus welcher Quelle dieser Aktionär erzeugt wurde.
     * Also z.B. Aktienregister, InhaberImport, Manuelle Eingabe.
     * herkunftQuelle: Siehe KonstAktienregisterQuelle
     * herkunftQuelleLfd: z.B. Nummer des laufenden Imports, Dateiname, Datum, Uhrzeit o.ä., je Quelle
     * herkunftIdent: Verweis auf den "Quellsatz" in der jeweiligen Quelle.
     */
    public int herkunftQuelle = 0;

    /**1=Namensaktien, 2=Inhaberaktien*/
    /*AAAAA DB Aktienregister*/
    public int namensInhaberAktien = 1;

    /*Len=30*/
    public String herkunftQuelleLfd = "";

    public int herkunftIdent = 0;

    public int versandNummer = 0;

    public int datenUpdate = 0;

    /*
     * --------------- Nicht in Datenbank ---------------
     */
    public String meldungAktiv = "";

    public String zutrittsIdent = "";

    public String inSammelkarte = "";

    public String sammelkarte = "";

    public String statusPraesenz = "";

    public String meldungsIdent = "";

    /*
     * Felder fuer Aktienregisterupdate
     */
    public Boolean bestandGeaendert = false;
    
    public Boolean besitzGeandert = false;

    public Boolean personNatJurGeaendert = false;

    public EclAktienregister() {

    }

    public EclAktienregister(String aktionaersnummer, int anredeId, String nachname, String vorname, String strasse,
            String postleitzahl, String ort, int staatId, int staatIdVersand, String nameKomplett, long stueckAktien,
            long stimmen, String besitzart, String adresszeile1, String adresszeile2, String adresszeile3,
            String adresszeile4) {
        super();
        this.aktionaersnummer = aktionaersnummer;
        this.anredeId = anredeId;
        this.nachname = nachname;
        this.vorname = vorname;
        this.strasse = strasse;
        this.postleitzahl = postleitzahl;
        this.ort = ort;
        this.staatId = staatId;
        this.staatIdVersand = staatIdVersand;
        this.nameKomplett = nameKomplett;
        this.stueckAktien = stueckAktien;
        this.stimmen = stimmen;
        this.besitzart = besitzart;
        this.adresszeile1 = adresszeile1;
        this.adresszeile2 = adresszeile2;
        this.adresszeile3 = adresszeile3;
        this.adresszeile4 = adresszeile4;
    }

    public static EclAktienregister copyEntity(EclAktienregister eintrag) {

        EclAktienregister tmp = new EclAktienregister();

        tmp.mandant = eintrag.mandant;
        tmp.aktienregisterIdent = eintrag.aktienregisterIdent;
        tmp.db_version = eintrag.db_version;
        tmp.aktionaersnummer = eintrag.aktionaersnummer;
        tmp.personNatJur = eintrag.personNatJur;
        tmp.anredeId = eintrag.anredeId;
        tmp.titel = eintrag.titel;
        tmp.name3 = eintrag.name3;
        tmp.nachname = eintrag.nachname;
        tmp.name1 = eintrag.name1;
        tmp.vorname = eintrag.vorname;
        tmp.name2 = eintrag.name2;
        tmp.zusatz = eintrag.zusatz;
        tmp.strasse = eintrag.strasse;
        tmp.postfach = eintrag.postfach;
        tmp.postleitzahl = eintrag.postleitzahl;
        tmp.postleitzahlPostfach = eintrag.postleitzahlPostfach;
        tmp.ort = eintrag.ort;
        tmp.staatId = eintrag.staatId;
        tmp.email = eintrag.email;
        tmp.anredeIdVersand = eintrag.anredeIdVersand;
        tmp.titelVersand = eintrag.titelVersand;
        tmp.name3Versand = eintrag.name3Versand;
        tmp.nachnameVersand = eintrag.nachnameVersand;
        tmp.name1Versand = eintrag.name1Versand;
        tmp.vornameVersand = eintrag.vornameVersand;
        tmp.name2Versand = eintrag.name2Versand;
        tmp.zusatzVersand = eintrag.zusatzVersand;
        tmp.strasseVersand = eintrag.strasseVersand;
        tmp.postfachVersand = eintrag.postfachVersand;
        tmp.postleitzahlVersand = eintrag.postleitzahlVersand;
        tmp.postleitzahlPostfachVersand = eintrag.postleitzahlPostfachVersand;
        tmp.ortVersand = eintrag.ortVersand;
        tmp.staatIdVersand = eintrag.staatIdVersand;
        tmp.emailVersand = eintrag.emailVersand;
        tmp.nameKomplett = eintrag.nameKomplett;
        tmp.istPersonengemeinschaft = eintrag.istPersonengemeinschaft;
        tmp.istJuristischePerson = eintrag.istJuristischePerson;
        tmp.versandAbweichend = eintrag.versandAbweichend;
        tmp.gattungId = eintrag.gattungId;
        tmp.isin = eintrag.isin;
        tmp.stueckAktien = eintrag.stueckAktien;
        tmp.stimmen = eintrag.stimmen;
        tmp.besitzart = eintrag.besitzart;
        tmp.stimmausschluss = eintrag.stimmausschluss;
        tmp.gruppe = eintrag.gruppe;
        tmp.adresszeile1 = eintrag.adresszeile1;
        tmp.adresszeile2 = eintrag.adresszeile2;
        tmp.adresszeile3 = eintrag.adresszeile3;
        tmp.adresszeile4 = eintrag.adresszeile4;
        tmp.adresszeile5 = eintrag.adresszeile5;
        tmp.adresszeile6 = eintrag.adresszeile6;
        tmp.adresszeile7 = eintrag.adresszeile7;
        tmp.adresszeile8 = eintrag.adresszeile8;
        tmp.adresszeile9 = eintrag.adresszeile9;
        tmp.adresszeile10 = eintrag.adresszeile10;
        tmp.englischerVersand = eintrag.englischerVersand;
        tmp.herkunftQuelle = eintrag.herkunftQuelle;
        tmp.namensInhaberAktien = eintrag.namensInhaberAktien;
        tmp.herkunftQuelleLfd = eintrag.herkunftQuelleLfd;
        tmp.herkunftIdent = eintrag.herkunftIdent;
        tmp.versandNummer = eintrag.versandNummer;
        tmp.datenUpdate = eintrag.datenUpdate;
        tmp.bestandGeaendert = eintrag.bestandGeaendert;
        tmp.besitzGeandert = eintrag.besitzGeandert;
        tmp.personNatJurGeaendert = eintrag.personNatJurGeaendert;

        return tmp;
    }

    /** nachname, wenn gefüllt, sonst name1 */
    public String liefereName() {
        if (nachname.isEmpty()) {
            return name1;
        }
        return nachname;

        //        Alternativ:
        //        if (istJuristischePerson==1) {
        //            return name1;
        //        }
        //        else {
        //            return nachname;
        //        }

    }

    /**Bei juristischen Personen: Leer; sonst
     * vorname
     */
    public String liefereVorname() {
        if (istJuristischePerson == 1) {
            return "";
        } else {
            return vorname;
        }
    }

    /**Bei juristischen Personen: Leer; sonst
     * titel
     */
    public String liefereTitel() {
        if (istJuristischePerson == 1) {
            return "";
        } else {
            return titel;
        }
    }

    public String liefereTitelVornameName() {
        String titelVornameName = "";
        if (istJuristischePerson == 1) {
            titelVornameName = name1;
            if (!name2.isEmpty()) {
                titelVornameName = titelVornameName + " " + name2;
            }
            if (!name3.isEmpty()) {
                titelVornameName = titelVornameName + " " + name3;
            }
        } else {
            titelVornameName = nachname;
            if (vorname.length() != 0) {
                titelVornameName = vorname + " " + titelVornameName;
            }
            if (titel.length() != 0) {
                titelVornameName = titel + " " + titelVornameName;
            }
        }
        return titelVornameName;
    }

    public String liefereNameKommaTitelVorname() {
        String nameKommaTitelVorname = "";
        if (istJuristischePerson == 1) {
            nameKommaTitelVorname = name1;
            if (!name2.isEmpty()) {
                nameKommaTitelVorname = nameKommaTitelVorname + " " + name2;
            }
            if (!name3.isEmpty()) {
                nameKommaTitelVorname = nameKommaTitelVorname + " " + name3;
            }
        } else {
            boolean kommaEingefuegt = false;

            nameKommaTitelVorname = nachname;
            if (titel.length() != 0) {
                if (kommaEingefuegt == false) {
                    nameKommaTitelVorname += ",";
                    kommaEingefuegt = true;
                }
                nameKommaTitelVorname = nameKommaTitelVorname + " " + titel;
            }
            if (vorname.length() != 0) {
                if (kommaEingefuegt == false) {
                    nameKommaTitelVorname += ",";
                    kommaEingefuegt = true;
                }
                nameKommaTitelVorname = nameKommaTitelVorname + " " + vorname;
            }
        }
        return nameKommaTitelVorname;
    }

    public boolean pruefePersonengemeinschaft() {
        if (istPersonengemeinschaft == 1) {
            return true;
        }
        return false;
    }

    public String liefereErsterVorname() {
        int enthalten = 0;

        enthalten = vorname.indexOf(" und ");
        if (enthalten != 0) {
        } else {
            enthalten = vorname.indexOf(" u. ");
            if (enthalten != 0) {
            } else {
                return vorname;
            }

        }

        return vorname.substring(0, enthalten);

    }

    public String liefereZweiterVorname() {
        int enthalten = 0;

        enthalten = vorname.indexOf(" und ");
        if (enthalten != 0) {
        } else {
            enthalten = vorname.indexOf(" u. ");
            if (enthalten != 0) {
            } else {
                return vorname;
            }

        }

        return vorname.substring(enthalten + 5);
    }

    /********************
     * Ab hier Standard-getter und setter
     ************************************************/

    public int getMandant() {
        return mandant;
    }

    public void setMandant(int mandant) {
        this.mandant = mandant;
    }

    public int getAktienregisterIdent() {
        return aktienregisterIdent;
    }

    public void setAktienregisterIdent(int aktienregisterIdent) {
        this.aktienregisterIdent = aktienregisterIdent;
    }

    public long getDb_version() {
        return db_version;
    }

    public void setDb_version(long db_version) {
        this.db_version = db_version;
    }

    public String getAktionaersnummer() {
        return aktionaersnummer;
    }

    public void setAktionaersnummer(String aktionaersnummer) {
        this.aktionaersnummer = aktionaersnummer;
    }

    public int getPersonNatJur() {
        return personNatJur;
    }

    public void setPersonNatJur(int personNatJur) {
        this.personNatJur = personNatJur;
    }

    public int getAnredeId() {
        return anredeId;
    }

    public void setAnredeId(int anredeId) {
        this.anredeId = anredeId;
    }

    public String getTitel() {
        return titel;
    }

    public void setTitel(String titel) {
        this.titel = titel;
    }

    public String getName3() {
        return name3;
    }

    public void setName3(String name3) {
        this.name3 = name3;
    }

    public String getNachname() {
        return nachname;
    }

    public void setNachname(String nachname) {
        this.nachname = nachname;
    }

    public String getName1() {
        return name1;
    }

    public void setName1(String name1) {
        this.name1 = name1;
    }

    public String getVorname() {
        return vorname;
    }

    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

    public String getName2() {
        return name2;
    }

    public void setName2(String name2) {
        this.name2 = name2;
    }

    public String getZusatz() {
        return zusatz;
    }

    public void setZusatz(String zusatz) {
        this.zusatz = zusatz;
    }

    public String getStrasse() {
        return strasse;
    }

    public void setStrasse(String strasse) {
        this.strasse = strasse;
    }

    public String getPostfach() {
        return postfach;
    }

    public void setPostfach(String postfach) {
        this.postfach = postfach;
    }

    public String getPostleitzahl() {
        return postleitzahl;
    }

    public void setPostleitzahl(String postleitzahl) {
        this.postleitzahl = postleitzahl;
    }

    public String getPostleitzahlPostfach() {
        return postleitzahlPostfach;
    }

    public void setPostleitzahlPostfach(String postleitzahlPostfach) {
        this.postleitzahlPostfach = postleitzahlPostfach;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAnredeIdVersand() {
        return anredeIdVersand;
    }

    public void setAnredeIdVersand(int anredeIdVersand) {
        this.anredeIdVersand = anredeIdVersand;
    }

    public String getTitelVersand() {
        return titelVersand;
    }

    public void setTitelVersand(String titelVersand) {
        this.titelVersand = titelVersand;
    }

    public String getName3Versand() {
        return name3Versand;
    }

    public void setName3Versand(String name3Versand) {
        this.name3Versand = name3Versand;
    }

    public String getNachnameVersand() {
        return nachnameVersand;
    }

    public void setNachnameVersand(String nachnameVersand) {
        this.nachnameVersand = nachnameVersand;
    }

    public String getName1Versand() {
        return name1Versand;
    }

    public void setName1Versand(String name1Versand) {
        this.name1Versand = name1Versand;
    }

    public String getVornameVersand() {
        return vornameVersand;
    }

    public void setVornameVersand(String vornameVersand) {
        this.vornameVersand = vornameVersand;
    }

    public String getName2Versand() {
        return name2Versand;
    }

    public void setName2Versand(String name2Versand) {
        this.name2Versand = name2Versand;
    }

    public String getZusatzVersand() {
        return zusatzVersand;
    }

    public void setZusatzVersand(String zusatzVersand) {
        this.zusatzVersand = zusatzVersand;
    }

    public String getStrasseVersand() {
        return strasseVersand;
    }

    public void setStrasseVersand(String strasseVersand) {
        this.strasseVersand = strasseVersand;
    }

    public String getPostfachVersand() {
        return postfachVersand;
    }

    public void setPostfachVersand(String postfachVersand) {
        this.postfachVersand = postfachVersand;
    }

    public String getPostleitzahlVersand() {
        return postleitzahlVersand;
    }

    public void setPostleitzahlVersand(String postleitzahlVersand) {
        this.postleitzahlVersand = postleitzahlVersand;
    }

    public String getPostleitzahlPostfachVersand() {
        return postleitzahlPostfachVersand;
    }

    public void setPostleitzahlPostfachVersand(String postleitzahlPostfachVersand) {
        this.postleitzahlPostfachVersand = postleitzahlPostfachVersand;
    }

    public String getOrtVersand() {
        return ortVersand;
    }

    public void setOrtVersand(String ortVersand) {
        this.ortVersand = ortVersand;
    }

    public int getStaatIdVersand() {
        return staatIdVersand;
    }

    public void setStaatIdVersand(int staatIdVersand) {
        this.staatIdVersand = staatIdVersand;
    }

    public String getEmailVersand() {
        return emailVersand;
    }

    public void setEmailVersand(String emailVersand) {
        this.emailVersand = emailVersand;
    }

    public String getNameKomplett() {
        return nameKomplett;
    }

    public void setNameKomplett(String nameKomplett) {
        this.nameKomplett = nameKomplett;
    }

    public int getIstPersonengemeinschaft() {
        return istPersonengemeinschaft;
    }

    public void setIstPersonengemeinschaft(int istPersonengemeinschaft) {
        this.istPersonengemeinschaft = istPersonengemeinschaft;
    }

    public int getIstJuristischePerson() {
        return istJuristischePerson;
    }

    public void setIstJuristischePerson(int istJuristischePerson) {
        this.istJuristischePerson = istJuristischePerson;
    }

    public int getVersandAbweichend() {
        return versandAbweichend;
    }

    public void setVersandAbweichend(int versandAbweichend) {
        this.versandAbweichend = versandAbweichend;
    }

    public String getIsin() {
        return isin;
    }

    public void setIsin(String isin) {
        this.isin = isin;
    }

    public long getStueckAktien() {
        return stueckAktien;
    }

    public void setStueckAktien(long stueckAktien) {
        this.stueckAktien = stueckAktien;
    }

    public long getStimmen() {
        return stimmen;
    }

    public void setStimmen(long stimmen) {
        this.stimmen = stimmen;
    }

    public String getBesitzart() {
        return besitzart;
    }

    public void setBesitzart(String besitzart) {
        this.besitzart = besitzart;
    }

    public String getStimmausschluss() {
        return stimmausschluss;
    }

    public void setStimmausschluss(String stimmausschluss) {
        this.stimmausschluss = stimmausschluss;
    }

    public int getGruppe() {
        return gruppe;
    }

    public void setGruppe(int gruppe) {
        this.gruppe = gruppe;
    }

    public String getAdresszeile1() {
        return adresszeile1;
    }

    public void setAdresszeile1(String adresszeile1) {
        this.adresszeile1 = adresszeile1;
    }

    public String getAdresszeile2() {
        return adresszeile2;
    }

    public void setAdresszeile2(String adresszeile2) {
        this.adresszeile2 = adresszeile2;
    }

    public String getAdresszeile3() {
        return adresszeile3;
    }

    public void setAdresszeile3(String adresszeile3) {
        this.adresszeile3 = adresszeile3;
    }

    public String getAdresszeile4() {
        return adresszeile4;
    }

    public void setAdresszeile4(String adresszeile4) {
        this.adresszeile4 = adresszeile4;
    }

    public String getAdresszeile5() {
        return adresszeile5;
    }

    public void setAdresszeile5(String adresszeile5) {
        this.adresszeile5 = adresszeile5;
    }

    public String getAdresszeile6() {
        return adresszeile6;
    }

    public void setAdresszeile6(String adresszeile6) {
        this.adresszeile6 = adresszeile6;
    }

    public String getAdresszeile7() {
        return adresszeile7;
    }

    public void setAdresszeile7(String adresszeile7) {
        this.adresszeile7 = adresszeile7;
    }

    public String getAdresszeile8() {
        return adresszeile8;
    }

    public void setAdresszeile8(String adresszeile8) {
        this.adresszeile8 = adresszeile8;
    }

    public String getAdresszeile9() {
        return adresszeile9;
    }

    public void setAdresszeile9(String adresszeile9) {
        this.adresszeile9 = adresszeile9;
    }

    public String getAdresszeile10() {
        return adresszeile10;
    }

    public void setAdresszeile10(String adresszeile10) {
        this.adresszeile10 = adresszeile10;
    }

    public int getEnglischerVersand() {
        return englischerVersand;
    }

    public void setEnglischerVersand(int englischerVersand) {
        this.englischerVersand = englischerVersand;
    }

    public int getHerkunftQuelle() {
        return herkunftQuelle;
    }

    public void setHerkunftQuelle(int herkunftQuelle) {
        this.herkunftQuelle = herkunftQuelle;
    }

    public String getHerkunftQuelleLfd() {
        return herkunftQuelleLfd;
    }

    public void setHerkunftQuelleLfd(String herkunftQuelleLfd) {
        this.herkunftQuelleLfd = herkunftQuelleLfd;
    }

    public int getHerkunftIdent() {
        return herkunftIdent;
    }

    public void setHerkunftIdent(int herkunftIdent) {
        this.herkunftIdent = herkunftIdent;
    }

    public int getVersandNummer() {
        return versandNummer;
    }

    public void setVersandNummer(int versandNummer) {
        this.versandNummer = versandNummer;
    }

    public int getDatenUpdate() {
        return datenUpdate;
    }

    public void setDatenUpdate(int datenUpdate) {
        this.datenUpdate = datenUpdate;
    }

    public String getMeldungAktiv() {
        return meldungAktiv;
    }

    public void setMeldungAktiv(String meldungAktiv) {
        this.meldungAktiv = meldungAktiv;
    }

    public String getZutrittsIdent() {
        return zutrittsIdent;
    }

    public void setZutrittsIdent(String zutrittsIdent) {
        this.zutrittsIdent = zutrittsIdent;
    }

    public String getInSammelkarte() {
        return inSammelkarte;
    }

    public void setInSammelkarte(String inSammelkarte) {
        this.inSammelkarte = inSammelkarte;
    }

    public String getSammelkarte() {
        return sammelkarte;
    }

    public void setSammelkarte(String sammelkarte) {
        this.sammelkarte = sammelkarte;
    }

    public String getStatusPraesenz() {
        return statusPraesenz;
    }

    public void setStatusPraesenz(String statusPraesenz) {
        this.statusPraesenz = statusPraesenz;
    }

    public String getMeldungsIdent() {
        return meldungsIdent;
    }

    public void setMeldungsIdent(String meldungsIdent) {
        this.meldungsIdent = meldungsIdent;
    }

    public Boolean getBestandGeaendert() {
        return bestandGeaendert;
    }

    public void setBestandGeaendert(Boolean bestandGeaendert) {
        this.bestandGeaendert = bestandGeaendert;
    }
    
    public Boolean getBesitzGeaendert() {
        return besitzGeandert;
    }

    public void setBesitzGeaendert(Boolean besitzGeandert) {
        this.besitzGeandert = besitzGeandert;
    }

    public Boolean getPersonNatJurGeaendert() {
        return personNatJurGeaendert;
    }

    public void setPersonNatJurGeaendert(Boolean personNatJurGeaendert) {
        this.personNatJurGeaendert = personNatJurGeaendert;
    }

    public void setGattungId(int gattungId) {
        this.gattungId = gattungId;
    }

    @Override
    public String toString() {
        return "EclAktienregister [mandant=" + mandant + ", aktienregisterIdent=" + aktienregisterIdent
                + ", db_version=" + db_version + ", aktionaersnummer=" + aktionaersnummer + ", personNatJur="
                + personNatJur + ", anredeId=" + anredeId + ", titel=" + titel + ", name3=" + name3 + ", nachname="
                + nachname + ", name1=" + name1 + ", vorname=" + vorname + ", name2=" + name2 + ", zusatz=" + zusatz
                + ", strasse=" + strasse + ", postfach=" + postfach + ", postleitzahl=" + postleitzahl
                + ", postleitzahlPostfach=" + postleitzahlPostfach + ", ort=" + ort + ", staatId=" + staatId
                + ", email=" + email + ", anredeIdVersand=" + anredeIdVersand + ", titelVersand=" + titelVersand
                + ", name3Versand=" + name3Versand + ", nachnameVersand=" + nachnameVersand + ", name1Versand="
                + name1Versand + ", vornameVersand=" + vornameVersand + ", name2Versand=" + name2Versand
                + ", zusatzVersand=" + zusatzVersand + ", strasseVersand=" + strasseVersand + ", postfachVersand="
                + postfachVersand + ", postleitzahlVersand=" + postleitzahlVersand + ", postleitzahlPostfachVersand="
                + postleitzahlPostfachVersand + ", ortVersand=" + ortVersand + ", staatIdVersand=" + staatIdVersand
                + ", emailVersand=" + emailVersand + ", nameKomplett=" + nameKomplett + ", istPersonengemeinschaft="
                + istPersonengemeinschaft + ", istJuristischePerson=" + istJuristischePerson + ", versandAbweichend="
                + versandAbweichend + ", gattungId=" + gattungId + ", isin=" + isin + ", stueckAktien=" + stueckAktien
                + ", stimmen=" + stimmen + ", besitzart=" + besitzart + ", stimmausschluss=" + stimmausschluss
                + ", gruppe=" + gruppe + ", adresszeile1=" + adresszeile1 + ", adresszeile2=" + adresszeile2
                + ", adresszeile3=" + adresszeile3 + ", adresszeile4=" + adresszeile4 + ", adresszeile5=" + adresszeile5
                + ", adresszeile6=" + adresszeile6 + ", adresszeile7=" + adresszeile7 + ", adresszeile8=" + adresszeile8
                + ", adresszeile9=" + adresszeile9 + ", adresszeile10=" + adresszeile10 + ", englischerVersand="
                + englischerVersand + ", herkunftQuelle=" + herkunftQuelle + ", herkunftQuelleLfd=" + herkunftQuelleLfd
                + ", herkunftIdent=" + herkunftIdent + ", versandNummer=" + versandNummer + ", datenUpdate="
                + datenUpdate + ", bestandGeaendert=" + bestandGeaendert + ", personNatJurGeandert=" + personNatJurGeaendert + "]";
    }

    /*
     * hashCode - equals wird in dieser Form nur beim AktienregisterImport verwendent:
     * Es mussen alle Felder verglichen werden welche nicht ausschließlich von uns Vergeben werden und in Import-Dateien enthalten sein können.
     * 
     * Aktuell nicht verglichen wird.
     * aktienregisterIdent - Von uns bei Insert
     * db_version - Bei Update außerhalb von Imports
     * personNatJur - Bei Anmeldung
     * bestandGeaendert - Interne Variable für Updates der Meldungen und evtl. Willenserklärungen
     * datenUpdate - Update durch neuen Import
     * herkunft... - Nur interne Vergabe
     * kennzeichen - Interne Vergabe
     * stimmausschluss - nicht verwendet
     * versandnummer - Für Ausgabe der Versandliste von uns
     */
    @Override
    public int hashCode() {
        return Objects.hash(adresszeile1, adresszeile10, adresszeile2, adresszeile3, adresszeile4, adresszeile5,
                adresszeile6, adresszeile7, adresszeile8, adresszeile9, aktionaersnummer, anredeId, anredeIdVersand,
                besitzart, email, emailVersand, gattungId, gruppe, isin, istJuristischePerson, istPersonengemeinschaft,
                mandant, nachname, nachnameVersand, name1, name1Versand, name2, name2Versand, name3, name3Versand,
                nameKomplett, ort, ortVersand, postfach, postfachVersand, postleitzahl, postleitzahlPostfach,
                postleitzahlPostfachVersand, postleitzahlVersand, staatId, staatIdVersand, stimmen, strasse,
                strasseVersand, stueckAktien, titel, titelVersand, versandAbweichend, vorname, vornameVersand, zusatz,
                zusatzVersand);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        EclAktienregister other = (EclAktienregister) obj;
        return Objects.equals(adresszeile1, other.adresszeile1) && Objects.equals(adresszeile10, other.adresszeile10)
                && Objects.equals(adresszeile2, other.adresszeile2) && Objects.equals(adresszeile3, other.adresszeile3)
                && Objects.equals(adresszeile4, other.adresszeile4) && Objects.equals(adresszeile5, other.adresszeile5)
                && Objects.equals(adresszeile6, other.adresszeile6) && Objects.equals(adresszeile7, other.adresszeile7)
                && Objects.equals(adresszeile8, other.adresszeile8) && Objects.equals(adresszeile9, other.adresszeile9)
                && Objects.equals(aktionaersnummer, other.aktionaersnummer) && anredeId == other.anredeId
                && anredeIdVersand == other.anredeIdVersand && Objects.equals(besitzart, other.besitzart)
                && Objects.equals(email, other.email) && Objects.equals(emailVersand, other.emailVersand)
                && gattungId == other.gattungId && gruppe == other.gruppe && Objects.equals(isin, other.isin)
                && istJuristischePerson == other.istJuristischePerson
                && istPersonengemeinschaft == other.istPersonengemeinschaft && mandant == other.mandant
                && Objects.equals(nachname, other.nachname) && Objects.equals(nachnameVersand, other.nachnameVersand)
                && Objects.equals(name1, other.name1) && Objects.equals(name1Versand, other.name1Versand)
                && Objects.equals(name2, other.name2) && Objects.equals(name2Versand, other.name2Versand)
                && Objects.equals(name3, other.name3) && Objects.equals(name3Versand, other.name3Versand)
                && Objects.equals(nameKomplett, other.nameKomplett) && Objects.equals(ort, other.ort)
                && Objects.equals(ortVersand, other.ortVersand) && Objects.equals(postfach, other.postfach)
                && Objects.equals(postfachVersand, other.postfachVersand)
                && Objects.equals(postleitzahl, other.postleitzahl)
                && Objects.equals(postleitzahlPostfach, other.postleitzahlPostfach)
                && Objects.equals(postleitzahlPostfachVersand, other.postleitzahlPostfachVersand)
                && Objects.equals(postleitzahlVersand, other.postleitzahlVersand) && staatId == other.staatId
                && staatIdVersand == other.staatIdVersand && stimmen == other.stimmen
                && Objects.equals(strasse, other.strasse) && Objects.equals(strasseVersand, other.strasseVersand)
                && stueckAktien == other.stueckAktien && Objects.equals(titel, other.titel)
                && Objects.equals(titelVersand, other.titelVersand) && versandAbweichend == other.versandAbweichend
                && Objects.equals(vorname, other.vorname) && Objects.equals(vornameVersand, other.vornameVersand)
                && Objects.equals(zusatz, other.zusatz) && Objects.equals(zusatzVersand, other.zusatzVersand);
    }

    /*
     * Funktioniert nur beim InhaberImportImport
     */
    public static Comparator<EclAktienregister> AnrComparator = new Comparator<EclAktienregister>() {

        public int compare(EclAktienregister a1, EclAktienregister a2) {
            int anr1 = Integer.parseInt(a1.aktionaersnummer);
            int anr2 = Integer.parseInt(a2.aktionaersnummer);

            return anr1 - anr2;
        }
    };
}
