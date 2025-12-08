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

import de.meetingapps.meetingportal.meetComAllg.CaDatumZeit;
import de.meetingapps.meetingportal.meetComEntities.EclAufgaben;
import de.meetingapps.meetingportal.meetComKonst.KonstAufgaben;
import de.meetingapps.meetingportal.meetComKonst.KonstAufgabenErledigt;
import de.meetingapps.meetingportal.meetComKonst.KonstAufgabenStatus;

public class EclAufgabenM implements Serializable {
    private static final long serialVersionUID = 1796475996135187833L;

    /**Mandant, für den die Aufgabe angefordert wurden*/
    private int mandant = 0;

    /** eindeutiger Key für die Aufgabe*/
    private int identAufgabe = 0;

    private long db_version = 0;

    /**Aufgaben-Art (aus KonstAufgaben)*/
    private int aufgabe = 0;
    private String aufgabeText = "";

    /**Text, in dem z.B. die Hotline ein Anliegen eines Aktionärs beschreiben kann.
     * Zwingend wenn aufgabe=sonstige
     * LEN=1000*/
    private String freitextBeschreibung = "";

    /**Zeitpunkt der Aufgabenerteilung, in Bildschirmformat
     * LEN=19*/
    private String zeitpunktErteilt = "";

    /**Siehe KonstAufgabenAnforderer*/
    private int anforderer = 0;
    /**LEN=1000*/
    private String angefordertVonBemerkung = "";

    /**Usernummer, der die Aufgabe erfasst hat. <0 oder > 9900 => technische User*/
    private int userNummerErfassung = 0;

    /**Status der Aufgabe siehe KonstAufgabenStatus*/
    private int status = 0;
    private String statusText = "";

    /**der Aufgabe übergebene Argumente - Bedeutung abhängig von der Aufgaben-Art, 
     * Beschreibung in KonstAufgaben bei der jeweiligen Aufgabe
     * LEN=je 200*/
    private String[] argument = null;
    private String argument0 = "";
    private String argument1 = "";
    private String argument2 = "";
    private String argument3 = "";
    private String argument4 = "";
    private String argument5 = "";
    private String argument6 = "";
    private String argument7 = "";
    private String argument8 = "";
    private String argument9 = "";

    /**Siehe KonsAufgabenErledigt*/
    private int erledigtVermerk = 0;
    private String erledigtVermerkText = "";

    /**Len=1000*/
    private String erledigtBemerkung = "";

    /**JLEN=19*/
    private String zeitpunktErledigt = "";

    /**Usernummer, der die Aufgabe erledigt hat. <0 oder > 9900 => technische User*/
    private int userNummerVerarbeitet = 0;

    public EclAufgabenM() {

    }

    public EclAufgabenM(EclAufgaben pAufgabe) {
        copyFrom(pAufgabe);
    }

    public void copyFrom(EclAufgaben pAufgabe) {
        mandant = pAufgabe.mandant;
        identAufgabe = pAufgabe.identAufgabe;
        db_version = pAufgabe.db_version;
        aufgabe = pAufgabe.aufgabe;
        aufgabeText = KonstAufgaben.getText(pAufgabe.aufgabe);
        freitextBeschreibung = pAufgabe.freitextBeschreibung;
        zeitpunktErteilt = CaDatumZeit.DatumZeitStringFuerAnzeige(pAufgabe.zeitpunktErteilt);
        anforderer = pAufgabe.anforderer;
        angefordertVonBemerkung = pAufgabe.angefordertVonBemerkung;
        userNummerErfassung = pAufgabe.userNummerErfassung;
        status = pAufgabe.status;
        statusText = KonstAufgabenStatus.getText(pAufgabe.status);

        argument = new String[10];
        argument[0] = pAufgabe.argument[0];
        argument0 = pAufgabe.argument[0];

        argument[1] = pAufgabe.argument[1];
        argument1 = pAufgabe.argument[1];

        argument[2] = pAufgabe.argument[2];
        argument2 = pAufgabe.argument[2];

        argument[3] = pAufgabe.argument[3];
        argument3 = pAufgabe.argument[3];

        argument[4] = pAufgabe.argument[4];
        argument4 = pAufgabe.argument[4];

        argument[5] = pAufgabe.argument[5];
        argument5 = pAufgabe.argument[5];

        argument[6] = pAufgabe.argument[6];
        argument6 = pAufgabe.argument[6];

        argument[7] = pAufgabe.argument[7];
        argument7 = pAufgabe.argument[7];

        argument[8] = pAufgabe.argument[8];
        argument8 = pAufgabe.argument[8];

        argument[9] = pAufgabe.argument[9];
        argument9 = pAufgabe.argument[9];

        erledigtVermerk = pAufgabe.erledigtVermerk;
        erledigtVermerkText = KonstAufgabenErledigt.getText(pAufgabe.erledigtVermerk);

        erledigtBemerkung = pAufgabe.erledigtBemerkung;
        zeitpunktErledigt = CaDatumZeit.DatumZeitStringFuerAnzeige(pAufgabe.zeitpunktErledigt);

        userNummerVerarbeitet = pAufgabe.userNummerVerarbeitet;
    }

    /*******************Public getter und setter*****************************/
    public int getMandant() {
        return mandant;
    }

    public void setMandant(int mandant) {
        this.mandant = mandant;
    }

    public int getIdentAufgabe() {
        return identAufgabe;
    }

    public void setIdentAufgabe(int identAufgabe) {
        this.identAufgabe = identAufgabe;
    }

    public long getDb_version() {
        return db_version;
    }

    public void setDb_version(long db_version) {
        this.db_version = db_version;
    }

    public int getAufgabe() {
        return aufgabe;
    }

    public void setAufgabe(int aufgabe) {
        this.aufgabe = aufgabe;
    }

    public String getAufgabeText() {
        return aufgabeText;
    }

    public void setAufgabeText(String aufgabeText) {
        this.aufgabeText = aufgabeText;
    }

    public String getFreitextBeschreibung() {
        return freitextBeschreibung;
    }

    public void setFreitextBeschreibung(String freitextBeschreibung) {
        this.freitextBeschreibung = freitextBeschreibung;
    }

    public String getZeitpunktErteilt() {
        return zeitpunktErteilt;
    }

    public void setZeitpunktErteilt(String zeitpunktErteilt) {
        this.zeitpunktErteilt = zeitpunktErteilt;
    }

    public int getAnforderer() {
        return anforderer;
    }

    public void setAnforderer(int anforderer) {
        this.anforderer = anforderer;
    }

    public String getAngefordertVonBemerkung() {
        return angefordertVonBemerkung;
    }

    public void setAngefordertVonBemerkung(String angefordertVonBemerkung) {
        this.angefordertVonBemerkung = angefordertVonBemerkung;
    }

    public int getUserNummerErfassung() {
        return userNummerErfassung;
    }

    public void setUserNummerErfassung(int userNummerErfassung) {
        this.userNummerErfassung = userNummerErfassung;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStatusText() {
        return statusText;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }

    public String[] getArgument() {
        return argument;
    }

    public void setArgument(String[] argument) {
        this.argument = argument;
    }

    public String getArgument0() {
        return argument0;
    }

    public void setArgument0(String argument0) {
        this.argument0 = argument0;
    }

    public String getArgument1() {
        return argument1;
    }

    public void setArgument1(String argument1) {
        this.argument1 = argument1;
    }

    public String getArgument2() {
        return argument2;
    }

    public void setArgument2(String argument2) {
        this.argument2 = argument2;
    }

    public String getArgument3() {
        return argument3;
    }

    public void setArgument3(String argument3) {
        this.argument3 = argument3;
    }

    public String getArgument4() {
        return argument4;
    }

    public void setArgument4(String argument4) {
        this.argument4 = argument4;
    }

    public String getArgument5() {
        return argument5;
    }

    public void setArgument5(String argument5) {
        this.argument5 = argument5;
    }

    public String getArgument6() {
        return argument6;
    }

    public void setArgument6(String argument6) {
        this.argument6 = argument6;
    }

    public String getArgument7() {
        return argument7;
    }

    public void setArgument7(String argument7) {
        this.argument7 = argument7;
    }

    public String getArgument8() {
        return argument8;
    }

    public void setArgument8(String argument8) {
        this.argument8 = argument8;
    }

    public String getArgument9() {
        return argument9;
    }

    public void setArgument9(String argument9) {
        this.argument9 = argument9;
    }

    public int getErledigtVermerk() {
        return erledigtVermerk;
    }

    public void setErledigtVermerk(int erledigtVermerk) {
        this.erledigtVermerk = erledigtVermerk;
    }

    public String getErledigtVermerkText() {
        return erledigtVermerkText;
    }

    public void setErledigtVermerkText(String erledigtVermerkText) {
        this.erledigtVermerkText = erledigtVermerkText;
    }

    public String getErledigtBemerkung() {
        return erledigtBemerkung;
    }

    public void setErledigtBemerkung(String erledigtBemerkung) {
        this.erledigtBemerkung = erledigtBemerkung;
    }

    public String getZeitpunktErledigt() {
        return zeitpunktErledigt;
    }

    public void setZeitpunktErledigt(String zeitpunktErledigt) {
        this.zeitpunktErledigt = zeitpunktErledigt;
    }

    public int getUserNummerVerarbeitet() {
        return userNummerVerarbeitet;
    }

    public void setUserNummerVerarbeitet(int userNummerVerarbeitet) {
        this.userNummerVerarbeitet = userNummerVerarbeitet;
    }

}
