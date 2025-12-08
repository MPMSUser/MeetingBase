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
import java.util.ArrayList;
import java.util.List;

import de.meetingapps.meetingportal.meetComBl.BlAktienregisterNummerAufbereiten;
import de.meetingapps.meetingportal.meetComBl.BlTeilnehmerLogin;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

@SessionScoped
@Named
@Deprecated
public class EclTeilnehmerLoginM implements Serializable {
    private static final long serialVersionUID = 7438761678241678662L;

    /**Return-Code für Web-Services*/
    private int rc = 0;

    /**angemeldete Kennung*/
    private String anmeldeKennung = "";

    /**angemeldete Kennung - aufbereitet für Anzeige (z.B. für ku178)*/
    private String anmeldeKennungAufbereitet = "";

    /**Passwort zur angemeldeten Kennung*/
    private String anmeldePasswort = "";

    private String oeffentlicheID = "";

    /**Art der anmeldeKennung: 
     * 1 = Aktienregister (aktionärsnummer), 
     * 2 = Aktionärsmeldung ("Eintrittskarte- Inhaberaktien"), 
     * 3= PersonNatJur*/
    private int anmeldeKennungArt = 0;

    /**Ident, im Aktienregister, auf den die Anmelde-Kennung verweist*/
    private int anmeldeIdentAktienregister = 0;

    /**Aktionärsnummer / Aktienregister, auf die die Anmelde-Kennung verweist*/
    private String anmeldeAktionaersnummer = "";

    /**Aktionärsnummer / Aktienregister, auf die die Anmelde-Kennung verweist - aufbereitet für externe Anzeige*/
    private String anmeldeAktionaersnummerExtern = "";

    /*TODO: zu klären: ist dies auch bei Anmeldung über Aktienregister gefüllt? Würde benötigt für Untervollmachten etc.!*/
    private int anmeldeIdentPersonenNatJur = 0;

    /**0 => "normale" Person (Bevollmächtigter etc.). 
     * >0 => Insti-Vertreter
     * -1 = Gast
     * Falls Institutioneller Anleger, dann hier die Nummer der - mandantenübergreifenden - instiident
     * 
     * Temporäre Lösung: wird aus kommunikationssprache der Person geholt
     * */
    private int instiIdent = 0;

    private String anmeldeNameVorname = "";
    private String anmeldeOrt = "";

    /**Im folgenden die ausführlichen Bestandteile für den eingeloggten Teilnehmer*/
    /*TODO #1 - wird verwendet, als ob das die Aktionärsdaten sind. Das stimmt aber nicht, wenn Vertreter angemeldet ist*/
    /*TODO #1 - Portaloberfläche: ist nun nur noch darauf aufgebaut, dass sich der Aktionär anmeldet! Und auch keine
     * 				andere Vollmachten bestitz (z.B.: Aktienzahl im Titel)
     */
    private long stimmen = 0;
    private String stimmenDE = "";
    /**String formatiert im Deutschen Format*/
    private String stimmenEN = "";
    /**String formatiert im Englischen Format*/
    private String anredeDE = "";
    /**Herr, Frau etc. gemäß Schlüssel*/
    private String anredeEN = "";
    private String titel = "";
    private String name = "";
    private String vorname = "";
    private String name2 = ""; //NEU
    private String name3 = ""; //NEU
    private String plz = "";
    private String ort = "";
    private String landeskuerzel = "";
    /**DE*/
    private String land = "";
    /**Deutschland*/
    private String strasse = "";
    private String briefanredeDE = "";
    /**wie aus anreden-Datei*/
    private String briefanredeEN = "";
    /**wie aus Anreden Datei*/
    private String titelVornameName = "";
    /**Dr. Hans Müller**/
    private String nameVornameTitel = "";
    /**Müller, Hans Dr.*/
    private String kompletteAnredeDE = "";
    /**"Sehr geehrter Herr Dr. Müller" oder bei juristische Person "Sehr geehrte Damen und Herren"*/
    private String kompletteAnredeEN = "";
    /**"Sehr geehrter Herr Dr. Müller" oder bei juristische Person "Sehr geehrte Damen und Herren"*/
    private String besitzArtKuerzel = "";
    /**E, F, V*/
    private String besitzArt = "";
    private String besitzArtEN = "";
    private int gattung = 0;

    public int getGattung() {
        if (gattung == 0) {
            return 1;
        }
        return gattung;
    }

    /**Abweichende Versandadresse aus Aktienregister*/
    private int versandAbweichend = 0;
    private String nameVersand = "";
    private String vornameVersand = "";
    private String name2Versand = ""; //Neu
    private String name3Versand = ""; //Neu

    private String strasseVersand = "";
    private String postleitzahlVersand = "";
    private String ortVersand = "";
    private String landeskuerzelVersand = "";
    private String landVersand = "";

    private boolean expertenModusAktiv = false;

    private boolean expertenModusMoeglich = false;

    /**wg. "Registrierungsinfo", Hinweise bestätigt, Kommunikationseinstellungen etc.
     * Wird aktuell nur für Aktienregister-User gefüllt, ansonsten "temporär gesetzt"
     * 
     * Für App: auf null gesetzt
     */
    private EclAktienregisterZusatzM aktienregisterZusatzM = null;

    /************Ab hier: neu - nur noch diese verwenden!*********************/

    /**Daten der eingeloggten Person - noch nicht fertig, derzeit immer nur der eingetragene Aktionär
     * eingeloggtePersonListe : dient dazu, um die Zeilen im JSF in einer einfachen Schleife durchzuarbeiten
     * eingeloggtePersonZeile<x> : dient dazu, um die Zeilen Zeilenweise anzuzeigen
     * anzahlZeilenEingeloggtePerson: von 0 bis 7, um abzufragen wieviele Zeilen angezeigt werden sollen
     * */
    /*TODO _Aktionaerskennung: Innovation - aktuell wird in die Daten immer der eingetragene Aktionär eingetragen*/
    /**1=Aktionär; 2=sonstige (ggf. noch erweitern)*/
    private int eingeloggtePersonIst = 0;
    private List<String> eingeloggtePersonListe = null;
    private List<String> eingeloggtePersonListeEN = null;
    private int anzahlZeilenEingeloggtePerson = 0;
    private String eingeloggtePersonZeile0 = "";
    private String eingeloggtePersonZeile1 = "";
    private String eingeloggtePersonZeile2 = "";
    private String eingeloggtePersonZeile3 = "";
    private String eingeloggtePersonZeile4 = "";
    private String eingeloggtePersonZeile5 = "";
    private String eingeloggtePersonZeile6 = "";
    private String eingeloggtePersonZeile0EN = "";
    private String eingeloggtePersonZeile1EN = "";
    private String eingeloggtePersonZeile2EN = "";
    private String eingeloggtePersonZeile3EN = "";
    private String eingeloggtePersonZeile4EN = "";
    private String eingeloggtePersonZeile5EN = "";
    private String eingeloggtePersonZeile6EN = "";

    /**Daten des Aktionärs, für den gerade Willenserkläerungen bearbeitet werden*/
    private List<String> verarbeiteterAktionaerListe = null;
    private List<String> verarbeiteterAktionaerListeEN = null;
    private int anzahlZeilenVerarbeiteterAktionaerPerson = 0;
    private String verarbeiteterAktionaerZeile0 = "";
    private String verarbeiteterAktionaerZeile1 = "";
    private String verarbeiteterAktionaerZeile2 = "";
    private String verarbeiteterAktionaerZeile3 = "";
    private String verarbeiteterAktionaerZeile4 = "";
    private String verarbeiteterAktionaerZeile5 = "";
    private String verarbeiteterAktionaerZeile6 = "";
    private String verarbeiteterAktionaerZeile0EN = "";
    private String verarbeiteterAktionaerZeile1EN = "";
    private String verarbeiteterAktionaerZeile2EN = "";
    private String verarbeiteterAktionaerZeile3EN = "";
    private String verarbeiteterAktionaerZeile4EN = "";
    private String verarbeiteterAktionaerZeile5EN = "";
    private String verarbeiteterAktionaerZeile6EN = "";

    /**Adresszeilen zum Versand*/
    private List<String> adresszeilen = null;

    /**true => diese Benutzerkennung darf sich nicht am Portal anmelden*/
    private boolean anmeldenUnzulaessig = false;

    /**true => diese Benutzerkennung darf sich nicht für elektronischen Einladungsversand und nicht für 
     * ein dauerhaftes Passwort registrieren
     */
    private boolean dauerhafteRegistrierungUnzulaessig = false;

    public void copyFrom(BlTeilnehmerLogin pTeilnehmerLogin) {
        rc = pTeilnehmerLogin.rc;
        anmeldeKennung = pTeilnehmerLogin.anmeldeKennung;
        anmeldeKennungAufbereitet = pTeilnehmerLogin.anmeldeKennungAufbereitet;
        anmeldePasswort = pTeilnehmerLogin.anmeldePasswort;

        anmeldeKennungArt = pTeilnehmerLogin.anmeldeKennungArt;
        anmeldeIdentAktienregister = pTeilnehmerLogin.anmeldeIdentAktienregister;
        setAnmeldeAktionaersnummer(pTeilnehmerLogin.anmeldeAktionaersnummer);
        anmeldeIdentPersonenNatJur = pTeilnehmerLogin.anmeldeIdentPersonenNatJur;
        instiIdent = pTeilnehmerLogin.instiIdent;
        anmeldeNameVorname = pTeilnehmerLogin.anmeldeNameVorname;
        anmeldeOrt = pTeilnehmerLogin.anmeldeOrt;

        stimmen = pTeilnehmerLogin.stimmen;
        stimmenDE = pTeilnehmerLogin.stimmenDE;
        stimmenEN = pTeilnehmerLogin.stimmenEN;
        anredeDE = pTeilnehmerLogin.anredeDE;
        anredeEN = pTeilnehmerLogin.anredeEN;
        titel = pTeilnehmerLogin.titel;
        name = pTeilnehmerLogin.name;
        vorname = pTeilnehmerLogin.vorname;
        name2 = pTeilnehmerLogin.name2;
        name3 = pTeilnehmerLogin.name3;
        plz = pTeilnehmerLogin.plz;
        ort = pTeilnehmerLogin.ort;
        landeskuerzel = pTeilnehmerLogin.landeskuerzel;
        land = pTeilnehmerLogin.land;
        strasse = pTeilnehmerLogin.strasse;
        briefanredeDE = pTeilnehmerLogin.briefanredeDE;
        briefanredeEN = pTeilnehmerLogin.briefanredeEN;
        titelVornameName = pTeilnehmerLogin.titelVornameName;
        nameVornameTitel = pTeilnehmerLogin.nameVornameTitel;
        kompletteAnredeDE = pTeilnehmerLogin.kompletteAnredeDE;
        kompletteAnredeEN = pTeilnehmerLogin.kompletteAnredeEN;
        besitzArtKuerzel = pTeilnehmerLogin.besitzArtKuerzel;
        besitzArt = pTeilnehmerLogin.besitzArt;
        besitzArtEN = pTeilnehmerLogin.besitzArtEN;
        gattung = pTeilnehmerLogin.gattung;

        expertenModusAktiv = pTeilnehmerLogin.expertenModusAktiv;
        expertenModusMoeglich = pTeilnehmerLogin.expertenModusMoeglich;
        if (pTeilnehmerLogin.aktienregisterZusatz == null) {
            aktienregisterZusatzM = null;
        } else {
            aktienregisterZusatzM = new EclAktienregisterZusatzM();
            aktienregisterZusatzM.copyFrom(pTeilnehmerLogin.aktienregisterZusatz);
        }

        versandAbweichend = pTeilnehmerLogin.versandAbweichend;
        nameVersand = pTeilnehmerLogin.nameVersand;
        vornameVersand = pTeilnehmerLogin.vornameVersand;
        name2Versand = pTeilnehmerLogin.name2Versand;
        name3Versand = pTeilnehmerLogin.name3Versand;
        strasseVersand = pTeilnehmerLogin.strasseVersand;
        postleitzahlVersand = pTeilnehmerLogin.postleitzahlVersand;
        ortVersand = pTeilnehmerLogin.ortVersand;
        landeskuerzelVersand = pTeilnehmerLogin.landeskuerzelVersand;
        landVersand = pTeilnehmerLogin.landVersand;

        eingeloggtePersonZeile0 = "";
        eingeloggtePersonZeile1 = "";
        eingeloggtePersonZeile2 = "";
        eingeloggtePersonZeile3 = "";
        eingeloggtePersonZeile4 = "";
        eingeloggtePersonZeile5 = "";
        eingeloggtePersonZeile6 = "";
        eingeloggtePersonZeile0EN = "";
        eingeloggtePersonZeile1EN = "";
        eingeloggtePersonZeile2EN = "";
        eingeloggtePersonZeile3EN = "";
        eingeloggtePersonZeile4EN = "";
        eingeloggtePersonZeile5EN = "";
        eingeloggtePersonZeile6EN = "";

        /**TODO Das Self-Assignment ist natürlich quatsch hier. Deshalb erst mal
         * auskommentiert. Klären, ob aus pTeilnehmerLogin zu kopieren ...*/
        //		eingeloggtePersonIst=this.eingeloggtePersonIst;
        eingeloggtePersonListe = new ArrayList<String>();
        eingeloggtePersonListeEN = new ArrayList<String>();
        anzahlZeilenEingeloggtePerson = -1;
        if (pTeilnehmerLogin.eingeloggtePersonListe != null) {
            for (int i = 0; i < pTeilnehmerLogin.eingeloggtePersonListe.size(); i++) {
                anzahlZeilenEingeloggtePerson++;
                String hString = pTeilnehmerLogin.eingeloggtePersonListe.get(i);
                String hStringEN = pTeilnehmerLogin.eingeloggtePersonListeEN.get(i);
                eingeloggtePersonListe.add(hString);
                eingeloggtePersonListeEN.add(hStringEN);
                switch (anzahlZeilenEingeloggtePerson) {
                case 0:
                    eingeloggtePersonZeile0 = hString;
                    eingeloggtePersonZeile0EN = hStringEN;
                    break;
                case 1:
                    eingeloggtePersonZeile1 = hString;
                    eingeloggtePersonZeile1EN = hStringEN;
                    break;
                case 2:
                    eingeloggtePersonZeile2 = hString;
                    eingeloggtePersonZeile2EN = hStringEN;
                    break;
                case 3:
                    eingeloggtePersonZeile3 = hString;
                    eingeloggtePersonZeile3EN = hStringEN;
                    break;
                case 4:
                    eingeloggtePersonZeile4 = hString;
                    eingeloggtePersonZeile4EN = hStringEN;
                    break;
                case 5:
                    eingeloggtePersonZeile5 = hString;
                    eingeloggtePersonZeile5EN = hStringEN;
                    break;
                case 6:
                    eingeloggtePersonZeile6 = hString;
                    eingeloggtePersonZeile6EN = hStringEN;
                    break;
                }
            }
        }

        verarbeiteterAktionaerZeile0 = "";
        verarbeiteterAktionaerZeile1 = "";
        verarbeiteterAktionaerZeile2 = "";
        verarbeiteterAktionaerZeile3 = "";
        verarbeiteterAktionaerZeile4 = "";
        verarbeiteterAktionaerZeile5 = "";
        verarbeiteterAktionaerZeile6 = "";
        verarbeiteterAktionaerZeile0EN = "";
        verarbeiteterAktionaerZeile1EN = "";
        verarbeiteterAktionaerZeile2EN = "";
        verarbeiteterAktionaerZeile3EN = "";
        verarbeiteterAktionaerZeile4EN = "";
        verarbeiteterAktionaerZeile5EN = "";
        verarbeiteterAktionaerZeile6EN = "";

        verarbeiteterAktionaerListe = new ArrayList<String>();
        verarbeiteterAktionaerListeEN = new ArrayList<String>();
        anzahlZeilenEingeloggtePerson = -1;
        if (pTeilnehmerLogin.verarbeiteterAktionaerListe != null) {
            for (int i = 0; i < pTeilnehmerLogin.verarbeiteterAktionaerListe.size(); i++) {
                anzahlZeilenEingeloggtePerson++;
                String hString = pTeilnehmerLogin.verarbeiteterAktionaerListe.get(i);
                String hStringEN = pTeilnehmerLogin.verarbeiteterAktionaerListeEN.get(i);
                verarbeiteterAktionaerListe.add(hString);
                verarbeiteterAktionaerListeEN.add(hStringEN);
                switch (anzahlZeilenEingeloggtePerson) {
                case 0:
                    verarbeiteterAktionaerZeile0 = hString;
                    verarbeiteterAktionaerZeile0EN = hStringEN;
                    break;
                case 1:
                    verarbeiteterAktionaerZeile1 = hString;
                    verarbeiteterAktionaerZeile1EN = hStringEN;
                    break;
                case 2:
                    verarbeiteterAktionaerZeile2 = hString;
                    verarbeiteterAktionaerZeile2EN = hStringEN;
                    break;
                case 3:
                    verarbeiteterAktionaerZeile3 = hString;
                    verarbeiteterAktionaerZeile3EN = hStringEN;
                    break;
                case 4:
                    verarbeiteterAktionaerZeile4 = hString;
                    verarbeiteterAktionaerZeile4EN = hStringEN;
                    break;
                case 5:
                    verarbeiteterAktionaerZeile5 = hString;
                    verarbeiteterAktionaerZeile5EN = hStringEN;
                    break;
                case 6:
                    verarbeiteterAktionaerZeile6 = hString;
                    verarbeiteterAktionaerZeile6EN = hStringEN;
                    break;
                }
            }
        }

        /*Adresszeilen*/
        adresszeilen = new ArrayList<String>();
        if (pTeilnehmerLogin.adresszeilen != null) {
            for (int i = 0; i < pTeilnehmerLogin.adresszeilen.size(); i++) {
                String hString = pTeilnehmerLogin.adresszeilen.get(i);
                adresszeilen.add(hString);
            }
        }

        anmeldenUnzulaessig = pTeilnehmerLogin.anmeldenUnzulaessig;
        dauerhafteRegistrierungUnzulaessig = pTeilnehmerLogin.dauerhafteRegistrierungUnzulaessig;

    }

    public void copyToM(EclTeilnehmerLoginM pTeilnehmerLogin) {
        pTeilnehmerLogin.rc = rc;
        ;
        pTeilnehmerLogin.anmeldeKennung = anmeldeKennung;
        pTeilnehmerLogin.anmeldePasswort = anmeldePasswort;
        pTeilnehmerLogin.oeffentlicheID = oeffentlicheID;
        pTeilnehmerLogin.anmeldeKennungArt = anmeldeKennungArt;
        pTeilnehmerLogin.anmeldeIdentAktienregister = anmeldeIdentAktienregister;
        //		pTeilnehmerLogin.anmeldeAktionaersnummer=anmeldeAktionaersnummer;
        pTeilnehmerLogin.setAnmeldeAktionaersnummer(anmeldeAktionaersnummer);

        pTeilnehmerLogin.anmeldeIdentPersonenNatJur = anmeldeIdentPersonenNatJur;
        pTeilnehmerLogin.instiIdent = instiIdent;
        pTeilnehmerLogin.anmeldeNameVorname = anmeldeNameVorname;
        pTeilnehmerLogin.anmeldeOrt = anmeldeOrt;

        pTeilnehmerLogin.stimmen = stimmen;
        pTeilnehmerLogin.stimmenDE = stimmenDE;
        pTeilnehmerLogin.stimmenEN = stimmenEN;
        pTeilnehmerLogin.anredeDE = anredeDE;
        pTeilnehmerLogin.anredeEN = anredeEN;
        pTeilnehmerLogin.titel = titel;
        pTeilnehmerLogin.name = name;
        pTeilnehmerLogin.vorname = vorname;
        pTeilnehmerLogin.name2 = name2;
        pTeilnehmerLogin.name3 = name3;
        pTeilnehmerLogin.plz = plz;
        pTeilnehmerLogin.ort = ort;
        pTeilnehmerLogin.landeskuerzel = landeskuerzel;
        pTeilnehmerLogin.land = land;
        pTeilnehmerLogin.strasse = strasse;
        pTeilnehmerLogin.briefanredeDE = briefanredeDE;
        pTeilnehmerLogin.briefanredeEN = briefanredeEN;
        pTeilnehmerLogin.titelVornameName = titelVornameName;
        pTeilnehmerLogin.nameVornameTitel = nameVornameTitel;
        pTeilnehmerLogin.kompletteAnredeDE = kompletteAnredeDE;
        pTeilnehmerLogin.kompletteAnredeEN = kompletteAnredeEN;
        pTeilnehmerLogin.besitzArtKuerzel = besitzArtKuerzel;
        pTeilnehmerLogin.besitzArt = besitzArt;
        pTeilnehmerLogin.besitzArtEN = besitzArtEN;
        pTeilnehmerLogin.gattung = gattung;

        pTeilnehmerLogin.expertenModusAktiv = expertenModusAktiv;
        pTeilnehmerLogin.expertenModusMoeglich = expertenModusMoeglich;
        if (aktienregisterZusatzM == null) {
            pTeilnehmerLogin.aktienregisterZusatzM = null;
        } else {
            pTeilnehmerLogin.aktienregisterZusatzM = new EclAktienregisterZusatzM();
            pTeilnehmerLogin.aktienregisterZusatzM.copyFromM(aktienregisterZusatzM);
        }

        pTeilnehmerLogin.versandAbweichend = versandAbweichend;
        pTeilnehmerLogin.nameVersand = nameVersand;
        pTeilnehmerLogin.vornameVersand = vornameVersand;
        pTeilnehmerLogin.name2Versand = name2Versand;
        pTeilnehmerLogin.name3Versand = name3Versand;
        pTeilnehmerLogin.strasseVersand = strasseVersand;
        pTeilnehmerLogin.postleitzahlVersand = postleitzahlVersand;
        pTeilnehmerLogin.ortVersand = ortVersand;
        pTeilnehmerLogin.landeskuerzelVersand = landeskuerzelVersand;
        landVersand = pTeilnehmerLogin.landVersand;

        /*Adresszeilen*/
        pTeilnehmerLogin.adresszeilen = new ArrayList<String>();
        if (adresszeilen != null) {
            for (int i = 0; i < adresszeilen.size(); i++) {
                String hString = adresszeilen.get(i);
                pTeilnehmerLogin.adresszeilen.add(hString);
            }
        }

        pTeilnehmerLogin.anmeldenUnzulaessig = anmeldenUnzulaessig;
        pTeilnehmerLogin.dauerhafteRegistrierungUnzulaessig = dauerhafteRegistrierungUnzulaessig;

    }

    public String getAnmeldeKennung() {
        return anmeldeKennung;
    }

    public void setAnmeldeKennung(String anmeldeKennung) {
        this.anmeldeKennung = anmeldeKennung;
    }

    public String getAnmeldePasswort() {
        return anmeldePasswort;
    }

    public void setAnmeldePasswort(String anmeldePasswort) {
        this.anmeldePasswort = anmeldePasswort;
    }

    public int getAnmeldeKennungArt() {
        return anmeldeKennungArt;
    }

    public void setAnmeldeKennungArt(int anmeldeKennungArt) {
        this.anmeldeKennungArt = anmeldeKennungArt;
    }

    public int getAnmeldeIdentAktienregister() {
        return anmeldeIdentAktienregister;
    }

    public void setAnmeldeIdentAktienregister(int anmeldeIdentAktienregister) {
        this.anmeldeIdentAktienregister = anmeldeIdentAktienregister;
    }

    public int getAnmeldeIdentPersonenNatJur() {
        return anmeldeIdentPersonenNatJur;
    }

    public void setAnmeldeIdentPersonenNatJur(int anmeldeIdentPersonenNatJur) {
        this.anmeldeIdentPersonenNatJur = anmeldeIdentPersonenNatJur;
    }

    public String getAnmeldeNameVorname() {
        return anmeldeNameVorname;
    }

    public void setAnmeldeNameVorname(String anmeldeNameVorname) {
        this.anmeldeNameVorname = anmeldeNameVorname;
    }

    public String getAnmeldeOrt() {
        return anmeldeOrt;
    }

    public void setAnmeldeOrt(String anmeldeOrt) {
        this.anmeldeOrt = anmeldeOrt;
    }

    public boolean isExpertenModusAktiv() {
        return expertenModusAktiv;
    }

    public void setExpertenModusAktiv(boolean expertenModusAktiv) {
        this.expertenModusAktiv = expertenModusAktiv;
    }

    public boolean isExpertenModusMoeglich() {
        return expertenModusMoeglich;
    }

    public void setExpertenModusMoeglich(boolean expertenModusMoeglich) {
        this.expertenModusMoeglich = expertenModusMoeglich;
    }

    public String getAnmeldeAktionaersnummer() {
        return anmeldeAktionaersnummer;
    }

    public void setAnmeldeAktionaersnummer(String anmeldeAktionaersnummer) {
        this.anmeldeAktionaersnummer = anmeldeAktionaersnummer;
        this.anmeldeAktionaersnummerExtern = BlAktienregisterNummerAufbereiten
                .aufbereitenFuerKlarschrift(anmeldeAktionaersnummer);
    }

    public String getOeffentlicheID() {
        return oeffentlicheID;
    }

    public void setOeffentlicheID(String oeffentlicheID) {
        this.oeffentlicheID = oeffentlicheID;
    }

    /*TODO $KONS Returncode entfernen bzw. nicht mehr verwenden (auch in App!) */
    public int getRc() {
        return rc;
    }

    public void setRc(int rc) {
        this.rc = rc;
    }

    public EclAktienregisterZusatzM getAktienregisterZusatzM() {
        return aktienregisterZusatzM;
    }

    public void setAktienregisterZusatzM(EclAktienregisterZusatzM aktienregisterZusatzM) {
        this.aktienregisterZusatzM = aktienregisterZusatzM;
    }

    public long getStimmen() {
        return stimmen;
    }

    public void setStimmen(long stimmen) {
        this.stimmen = stimmen;
    }

    public String getStimmenDE() {
        return stimmenDE;
    }

    public void setStimmenDE(String stimmenDE) {
        this.stimmenDE = stimmenDE;
    }

    public String getStimmenEN() {
        return stimmenEN;
    }

    public void setStimmenEN(String stimmenEN) {
        this.stimmenEN = stimmenEN;
    }

    public String getAnredeDE() {
        return anredeDE;
    }

    public void setAnredeDE(String anredeDE) {
        this.anredeDE = anredeDE;
    }

    public String getAnredeEN() {
        return anredeEN;
    }

    public void setAnredeEN(String anredeEN) {
        this.anredeEN = anredeEN;
    }

    public String getTitel() {
        return titel;
    }

    public void setTitel(String titel) {
        this.titel = titel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVorname() {
        return vorname;
    }

    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

    public String getPlz() {
        return plz;
    }

    public void setPlz(String plz) {
        this.plz = plz;
    }

    public String getOrt() {
        return ort;
    }

    public void setOrt(String ort) {
        this.ort = ort;
    }

    public String getLandeskuerzel() {
        return landeskuerzel;
    }

    public void setLandeskuerzel(String landeskuerzel) {
        this.landeskuerzel = landeskuerzel;
    }

    public String getLand() {
        return land;
    }

    public void setLand(String land) {
        this.land = land;
    }

    public String getStrasse() {
        return strasse;
    }

    public void setStrasse(String strasse) {
        this.strasse = strasse;
    }

    public String getBriefanredeDE() {
        return briefanredeDE;
    }

    public void setBriefanredeDE(String briefanredeDE) {
        this.briefanredeDE = briefanredeDE;
    }

    public String getBriefanredeEN() {
        return briefanredeEN;
    }

    public void setBriefanredeEN(String briefanredeEN) {
        this.briefanredeEN = briefanredeEN;
    }

    public String getTitelVornameName() {
        return titelVornameName;
    }

    public void setTitelVornameName(String titelVornameName) {
        this.titelVornameName = titelVornameName;
    }

    public String getNameVornameTitel() {
        return nameVornameTitel;
    }

    public void setNameVornameTitel(String nameVornameTitel) {
        this.nameVornameTitel = nameVornameTitel;
    }

    public String getKompletteAnredeDE() {
        return kompletteAnredeDE;
    }

    public void setKompletteAnredeDE(String kompletteAnredeDE) {
        this.kompletteAnredeDE = kompletteAnredeDE;
    }

    public String getKompletteAnredeEN() {
        return kompletteAnredeEN;
    }

    public void setKompletteAnredeEN(String kompletteAnredeEN) {
        this.kompletteAnredeEN = kompletteAnredeEN;
    }

    public String getBesitzArtKuerzel() {
        return besitzArtKuerzel;
    }

    public void setBesitzArtKuerzel(String besitzArtKuerzel) {
        this.besitzArtKuerzel = besitzArtKuerzel;
    }

    public String getBesitzArt() {
        return besitzArt;
    }

    public void setBesitzArt(String besitzArt) {
        this.besitzArt = besitzArt;
    }

    public String getBesitzArtEN() {
        return besitzArtEN;
    }

    public void setBesitzArtEN(String besitzArtEN) {
        this.besitzArtEN = besitzArtEN;
    }

    public int getVersandAbweichend() {
        return versandAbweichend;
    }

    public void setVersandAbweichend(int versandAbweichend) {
        this.versandAbweichend = versandAbweichend;
    }

    public String getNameVersand() {
        return nameVersand;
    }

    public void setNameVersand(String nameVersand) {
        this.nameVersand = nameVersand;
    }

    public String getVornameVersand() {
        return vornameVersand;
    }

    public void setVornameVersand(String vornameVersand) {
        this.vornameVersand = vornameVersand;
    }

    public String getStrasseVersand() {
        return strasseVersand;
    }

    public void setStrasseVersand(String strasseVersand) {
        this.strasseVersand = strasseVersand;
    }

    public String getPostleitzahlVersand() {
        return postleitzahlVersand;
    }

    public void setPostleitzahlVersand(String postleitzahlVersand) {
        this.postleitzahlVersand = postleitzahlVersand;
    }

    public String getOrtVersand() {
        return ortVersand;
    }

    public void setOrtVersand(String ortVersand) {
        this.ortVersand = ortVersand;
    }

    public String getLandeskuerzelVersand() {
        return landeskuerzelVersand;
    }

    public void setLandeskuerzelVersand(String landeskuerzelVersand) {
        this.landeskuerzelVersand = landeskuerzelVersand;
    }

    public String getLandVersand() {
        return landVersand;
    }

    public void setLandVersand(String landVersand) {
        this.landVersand = landVersand;
    }

    public String getName2() {
        return name2;
    }

    public void setName2(String name2) {
        this.name2 = name2;
    }

    public String getName3() {
        return name3;
    }

    public void setName3(String name3) {
        this.name3 = name3;
    }

    public String getName2Versand() {
        return name2Versand;
    }

    public void setName2Versand(String name2Versand) {
        this.name2Versand = name2Versand;
    }

    public String getName3Versand() {
        return name3Versand;
    }

    public void setName3Versand(String name3Versand) {
        this.name3Versand = name3Versand;
    }

    public int getEingeloggtePersonIst() {
        return eingeloggtePersonIst;
    }

    public void setEingeloggtePersonIst(int eingeloggtePersonIst) {
        this.eingeloggtePersonIst = eingeloggtePersonIst;
    }

    public List<String> getEingeloggtePersonListe() {
        return eingeloggtePersonListe;
    }

    public void setEingeloggtePersonListe(List<String> eingeloggtePersonListe) {
        this.eingeloggtePersonListe = eingeloggtePersonListe;
    }

    public int getAnzahlZeilenEingeloggtePerson() {
        return anzahlZeilenEingeloggtePerson;
    }

    public void setAnzahlZeilenEingeloggtePerson(int anzahlZeilenEingeloggtePerson) {
        this.anzahlZeilenEingeloggtePerson = anzahlZeilenEingeloggtePerson;
    }

    public String getEingeloggtePersonZeile0() {
        return eingeloggtePersonZeile0;
    }

    public void setEingeloggtePersonZeile0(String eingeloggtePersonZeile0) {
        this.eingeloggtePersonZeile0 = eingeloggtePersonZeile0;
    }

    public String getEingeloggtePersonZeile1() {
        return eingeloggtePersonZeile1;
    }

    public void setEingeloggtePersonZeile1(String eingeloggtePersonZeile1) {
        this.eingeloggtePersonZeile1 = eingeloggtePersonZeile1;
    }

    public String getEingeloggtePersonZeile2() {
        return eingeloggtePersonZeile2;
    }

    public void setEingeloggtePersonZeile2(String eingeloggtePersonZeile2) {
        this.eingeloggtePersonZeile2 = eingeloggtePersonZeile2;
    }

    public String getEingeloggtePersonZeile3() {
        return eingeloggtePersonZeile3;
    }

    public void setEingeloggtePersonZeile3(String eingeloggtePersonZeile3) {
        this.eingeloggtePersonZeile3 = eingeloggtePersonZeile3;
    }

    public String getEingeloggtePersonZeile4() {
        return eingeloggtePersonZeile4;
    }

    public void setEingeloggtePersonZeile4(String eingeloggtePersonZeile4) {
        this.eingeloggtePersonZeile4 = eingeloggtePersonZeile4;
    }

    public String getEingeloggtePersonZeile5() {
        return eingeloggtePersonZeile5;
    }

    public void setEingeloggtePersonZeile5(String eingeloggtePersonZeile5) {
        this.eingeloggtePersonZeile5 = eingeloggtePersonZeile5;
    }

    public String getEingeloggtePersonZeile6() {
        return eingeloggtePersonZeile6;
    }

    public void setEingeloggtePersonZeile6(String eingeloggtePersonZeile6) {
        this.eingeloggtePersonZeile6 = eingeloggtePersonZeile6;
    }

    public List<String> getVerarbeiteterAktionaerListe() {
        return verarbeiteterAktionaerListe;
    }

    public void setVerarbeiteterAktionaerListe(List<String> verarbeiteterAktionaerListe) {
        this.verarbeiteterAktionaerListe = verarbeiteterAktionaerListe;
    }

    public String getVerarbeiteterAktionaerZeile0() {
        return verarbeiteterAktionaerZeile0;
    }

    public void setVerarbeiteterAktionaerZeile0(String verarbeiteterAktionaerZeile0) {
        this.verarbeiteterAktionaerZeile0 = verarbeiteterAktionaerZeile0;
    }

    public String getVerarbeiteterAktionaerZeile1() {
        return verarbeiteterAktionaerZeile1;
    }

    public void setVerarbeiteterAktionaerZeile1(String verarbeiteterAktionaerZeile1) {
        this.verarbeiteterAktionaerZeile1 = verarbeiteterAktionaerZeile1;
    }

    public String getVerarbeiteterAktionaerZeile2() {
        return verarbeiteterAktionaerZeile2;
    }

    public void setVerarbeiteterAktionaerZeile2(String verarbeiteterAktionaerZeile2) {
        this.verarbeiteterAktionaerZeile2 = verarbeiteterAktionaerZeile2;
    }

    public String getVerarbeiteterAktionaerZeile3() {
        return verarbeiteterAktionaerZeile3;
    }

    public void setVerarbeiteterAktionaerZeile3(String verarbeiteterAktionaerZeile3) {
        this.verarbeiteterAktionaerZeile3 = verarbeiteterAktionaerZeile3;
    }

    public String getVerarbeiteterAktionaerZeile4() {
        return verarbeiteterAktionaerZeile4;
    }

    public void setVerarbeiteterAktionaerZeile4(String verarbeiteterAktionaerZeile4) {
        this.verarbeiteterAktionaerZeile4 = verarbeiteterAktionaerZeile4;
    }

    public String getVerarbeiteterAktionaerZeile5() {
        return verarbeiteterAktionaerZeile5;
    }

    public void setVerarbeiteterAktionaerZeile5(String verarbeiteterAktionaerZeile5) {
        this.verarbeiteterAktionaerZeile5 = verarbeiteterAktionaerZeile5;
    }

    public String getVerarbeiteterAktionaerZeile6() {
        return verarbeiteterAktionaerZeile6;
    }

    public void setVerarbeiteterAktionaerZeile6(String verarbeiteterAktionaerZeile6) {
        this.verarbeiteterAktionaerZeile6 = verarbeiteterAktionaerZeile6;
    }

    public int getAnzahlZeilenVerarbeiteterAktionaerPerson() {
        return anzahlZeilenVerarbeiteterAktionaerPerson;
    }

    public void setAnzahlZeilenVerarbeiteterAktionaerPerson(int anzahlZeilenVerarbeiteterAktionaerPerson) {
        this.anzahlZeilenVerarbeiteterAktionaerPerson = anzahlZeilenVerarbeiteterAktionaerPerson;
    }

    public List<String> getEingeloggtePersonListeEN() {
        return eingeloggtePersonListeEN;
    }

    public void setEingeloggtePersonListeEN(List<String> eingeloggtePersonListeEN) {
        this.eingeloggtePersonListeEN = eingeloggtePersonListeEN;
    }

    public String getEingeloggtePersonZeile0EN() {
        return eingeloggtePersonZeile0EN;
    }

    public void setEingeloggtePersonZeile0EN(String eingeloggtePersonZeile0EN) {
        this.eingeloggtePersonZeile0EN = eingeloggtePersonZeile0EN;
    }

    public String getEingeloggtePersonZeile1EN() {
        return eingeloggtePersonZeile1EN;
    }

    public void setEingeloggtePersonZeile1EN(String eingeloggtePersonZeile1EN) {
        this.eingeloggtePersonZeile1EN = eingeloggtePersonZeile1EN;
    }

    public String getEingeloggtePersonZeile2EN() {
        return eingeloggtePersonZeile2EN;
    }

    public void setEingeloggtePersonZeile2EN(String eingeloggtePersonZeile2EN) {
        this.eingeloggtePersonZeile2EN = eingeloggtePersonZeile2EN;
    }

    public String getEingeloggtePersonZeile3EN() {
        return eingeloggtePersonZeile3EN;
    }

    public void setEingeloggtePersonZeile3EN(String eingeloggtePersonZeile3EN) {
        this.eingeloggtePersonZeile3EN = eingeloggtePersonZeile3EN;
    }

    public String getEingeloggtePersonZeile4EN() {
        return eingeloggtePersonZeile4EN;
    }

    public void setEingeloggtePersonZeile4EN(String eingeloggtePersonZeile4EN) {
        this.eingeloggtePersonZeile4EN = eingeloggtePersonZeile4EN;
    }

    public String getEingeloggtePersonZeile5EN() {
        return eingeloggtePersonZeile5EN;
    }

    public void setEingeloggtePersonZeile5EN(String eingeloggtePersonZeile5EN) {
        this.eingeloggtePersonZeile5EN = eingeloggtePersonZeile5EN;
    }

    public String getEingeloggtePersonZeile6EN() {
        return eingeloggtePersonZeile6EN;
    }

    public void setEingeloggtePersonZeile6EN(String eingeloggtePersonZeile6EN) {
        this.eingeloggtePersonZeile6EN = eingeloggtePersonZeile6EN;
    }

    public List<String> getVerarbeiteterAktionaerListeEN() {
        return verarbeiteterAktionaerListeEN;
    }

    public void setVerarbeiteterAktionaerListeEN(List<String> verarbeiteterAktionaerListeEN) {
        this.verarbeiteterAktionaerListeEN = verarbeiteterAktionaerListeEN;
    }

    public String getVerarbeiteterAktionaerZeile0EN() {
        return verarbeiteterAktionaerZeile0EN;
    }

    public void setVerarbeiteterAktionaerZeile0EN(String verarbeiteterAktionaerZeile0EN) {
        this.verarbeiteterAktionaerZeile0EN = verarbeiteterAktionaerZeile0EN;
    }

    public String getVerarbeiteterAktionaerZeile1EN() {
        return verarbeiteterAktionaerZeile1EN;
    }

    public void setVerarbeiteterAktionaerZeile1EN(String verarbeiteterAktionaerZeile1EN) {
        this.verarbeiteterAktionaerZeile1EN = verarbeiteterAktionaerZeile1EN;
    }

    public String getVerarbeiteterAktionaerZeile2EN() {
        return verarbeiteterAktionaerZeile2EN;
    }

    public void setVerarbeiteterAktionaerZeile2EN(String verarbeiteterAktionaerZeile2EN) {
        this.verarbeiteterAktionaerZeile2EN = verarbeiteterAktionaerZeile2EN;
    }

    public String getVerarbeiteterAktionaerZeile3EN() {
        return verarbeiteterAktionaerZeile3EN;
    }

    public void setVerarbeiteterAktionaerZeile3EN(String verarbeiteterAktionaerZeile3EN) {
        this.verarbeiteterAktionaerZeile3EN = verarbeiteterAktionaerZeile3EN;
    }

    public String getVerarbeiteterAktionaerZeile4EN() {
        return verarbeiteterAktionaerZeile4EN;
    }

    public void setVerarbeiteterAktionaerZeile4EN(String verarbeiteterAktionaerZeile4EN) {
        this.verarbeiteterAktionaerZeile4EN = verarbeiteterAktionaerZeile4EN;
    }

    public String getVerarbeiteterAktionaerZeile5EN() {
        return verarbeiteterAktionaerZeile5EN;
    }

    public void setVerarbeiteterAktionaerZeile5EN(String verarbeiteterAktionaerZeile5EN) {
        this.verarbeiteterAktionaerZeile5EN = verarbeiteterAktionaerZeile5EN;
    }

    public String getVerarbeiteterAktionaerZeile6EN() {
        return verarbeiteterAktionaerZeile6EN;
    }

    public void setVerarbeiteterAktionaerZeile6EN(String verarbeiteterAktionaerZeile6EN) {
        this.verarbeiteterAktionaerZeile6EN = verarbeiteterAktionaerZeile6EN;
    }

    public String getAnmeldeAktionaersnummerExtern() {
        return anmeldeAktionaersnummerExtern;
    }

    public void setAnmeldeAktionaersnummerExtern(String anmeldeAktionaersnummerExtern) {
        this.anmeldeAktionaersnummerExtern = anmeldeAktionaersnummerExtern;
    }

    public List<String> getAdresszeilen() {
        return adresszeilen;
    }

    public void setAdresszeilen(List<String> adresszeilen) {
        this.adresszeilen = adresszeilen;
    }

    public void setGattung(int gattung) {
        this.gattung = gattung;
    }

    public String getAnmeldeKennungAufbereitet() {
        return anmeldeKennungAufbereitet;
    }

    public void setAnmeldeKennungAufbereitet(String anmeldeKennungAufbereitet) {
        this.anmeldeKennungAufbereitet = anmeldeKennungAufbereitet;
    }

    public boolean isAnmeldenUnzulaessig() {
        return anmeldenUnzulaessig;
    }

    public void setAnmeldenUnzulaessig(boolean anmeldenUnzulaessig) {
        this.anmeldenUnzulaessig = anmeldenUnzulaessig;
    }

    public boolean isDauerhafteRegistrierungUnzulaessig() {
        return dauerhafteRegistrierungUnzulaessig;
    }

    public void setDauerhafteRegistrierungUnzulaessig(boolean dauerhafteRegistrierungUnzulaessig) {
        this.dauerhafteRegistrierungUnzulaessig = dauerhafteRegistrierungUnzulaessig;
    }

    public int getInstiIdent() {
        return instiIdent;
    }

    public void setInstiIdent(int instiIdent) {
        this.instiIdent = instiIdent;
    }

}
