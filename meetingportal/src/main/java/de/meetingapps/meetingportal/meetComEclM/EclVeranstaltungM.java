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

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComEntities.EclVeranstaltung;

public class EclVeranstaltungM implements Serializable {
    private static final long serialVersionUID = 1068341875837539324L;

    private int logDrucken = 3;

    private int mandant = 0;

    private int ident = 0;

    /**Wird manuell vergeben, kann z.B. für Reihenfolge verwendet werden*/
    private int veranstaltungsnummer = 0;

    /**1 => wird angeboten
     * 2 => anzeigen, aber derzeit ausgebucht*/
    private int aktiv = 0;

    /**Verwendung für Bestätigung, Übersicht etc.
     * LEN=800*/
    private String kurzText = "";

    /**LEN jeweils 200*/
    /**ku178: Region*/
    private String text1 = "";
    /**ku178: Wo*/
    private String text2 = "";
    /**ku178: Wann*/
    private String text3 = "";
    /**ku178: Beginn*/
    private String text4 = "";
    /**ku178: Lokation*/
    private String text5 = "";
    private String text6 = "";

    private int maximaleAnzahlAnmeldungen = 0;

    private int anzahlAnmeldungenIst = 0;

    private int standardFuerBundesland = 0;

    /***************Nicht in Datenbank**************************/
    /**Wird gefüllt mit der "Standard"-Veranstaltung für den angemeldeten Aktionär*/
    private boolean ausgewaehlt = false;

    private boolean ausgebucht = false;
    
    /**Für Mehrfachauswahl über SelectRadioButtons. 1=Angemeldet, 2=Abgemeldet*/
    private String anAbgemeldet="";

    public EclVeranstaltungM(EclVeranstaltung pVeranstaltung) {
        copyFrom(pVeranstaltung);
    }

    public void copyFrom(EclVeranstaltung pVeranstaltung) {
        mandant = pVeranstaltung.mandant;
        ident = pVeranstaltung.ident;
        veranstaltungsnummer = pVeranstaltung.veranstaltungsnummer;
        aktiv = pVeranstaltung.aktiv;
        kurzText = pVeranstaltung.kurzText;
        text1 = pVeranstaltung.text1;
        text2 = pVeranstaltung.text2;
        text3 = pVeranstaltung.text3;
        text4 = pVeranstaltung.text4;
        text5 = pVeranstaltung.text5;
        text6 = pVeranstaltung.text6;
        maximaleAnzahlAnmeldungen = pVeranstaltung.maximaleAnzahlAnmeldungen;
        anzahlAnmeldungenIst = pVeranstaltung.istAnzahlAnmeldungen;
        standardFuerBundesland = pVeranstaltung.istStandardFuerBundesland;
        ausgewaehlt = pVeranstaltung.ausgewaehlt;

        CaBug.druckeLog("ident=" + ident, logDrucken, 10);
        CaBug.druckeLog("maximaleAnzahlAnmeldungen=" + maximaleAnzahlAnmeldungen, 
                logDrucken, 10);
        CaBug.druckeLog("anzahlAnmeldungenIst=" + anzahlAnmeldungenIst, logDrucken, 10);

        if (maximaleAnzahlAnmeldungen <= anzahlAnmeldungenIst) {
            ausgebucht = true;
            System.out.println("ausgebucht=true");
        }
    }

    public boolean detailsAnzeigen() {
        if (text1!=null && (!text1.isBlank())) {return true;}
        if (text2!=null && (!text2.isBlank())) {return true;}
        if (text3!=null && (!text3.isBlank())) {return true;}
        if (text4!=null && (!text4.isBlank())) {return true;}
        if (text5!=null && (!text5.isBlank())) {return true;}
        if (text6!=null && (!text6.isBlank())) {return true;}
        return false;
    }
    /*****************Standard-getter und Setter******************************/

    public int getMandant() {
        return mandant;
    }

    public void setMandant(int mandant) {
        this.mandant = mandant;
    }

    public int getIdent() {
        return ident;
    }

    public void setIdent(int ident) {
        this.ident = ident;
    }

    public int getVeranstaltungsnummer() {
        return veranstaltungsnummer;
    }

    public void setVeranstaltungsnummer(int veranstaltungsnummer) {
        this.veranstaltungsnummer = veranstaltungsnummer;
    }

    public int getAktiv() {
        return aktiv;
    }

    public void setAktiv(int aktiv) {
        this.aktiv = aktiv;
    }

    public String getKurzText() {
        return kurzText;
    }

    public void setKurzText(String kurzText) {
        this.kurzText = kurzText;
    }

    public String getText1() {
        return text1;
    }

    public void setText1(String text1) {
        this.text1 = text1;
    }

    public String getText2() {
        return text2;
    }

    public void setText2(String text2) {
        this.text2 = text2;
    }

    public String getText3() {
        return text3;
    }

    public void setText3(String text3) {
        this.text3 = text3;
    }

    public String getText4() {
        return text4;
    }

    public void setText4(String text4) {
        this.text4 = text4;
    }

    public String getText5() {
        return text5;
    }

    public void setText5(String text5) {
        this.text5 = text5;
    }

    public String getText6() {
        return text6;
    }

    public void setText6(String text6) {
        this.text6 = text6;
    }

    public int getMaximaleAnzahlAnmeldungen() {
        return maximaleAnzahlAnmeldungen;
    }

    public void setMaximaleAnzahlAnmeldungen(int maximaleAnzahlAnmeldungen) {
        this.maximaleAnzahlAnmeldungen = maximaleAnzahlAnmeldungen;
    }

    public boolean isAusgewaehlt() {
        return ausgewaehlt;
    }

    public void setAusgewaehlt(boolean ausgewaehlt) {
        this.ausgewaehlt = ausgewaehlt;
    }

    public int getAnzahlAnmeldungenIst() {
        return anzahlAnmeldungenIst;
    }

    public void setAnzahlAnmeldungenIst(int anzahlAnmeldungenIst) {
        this.anzahlAnmeldungenIst = anzahlAnmeldungenIst;
    }

    public int getStandardFuerBundesland() {
        return standardFuerBundesland;
    }

    public void setStandardFuerBundesland(int standardFuerBundesland) {
        this.standardFuerBundesland = standardFuerBundesland;
    }

    public boolean isAusgebucht() {
        return ausgebucht;
    }

    public void setAusgebucht(boolean ausgebucht) {
        this.ausgebucht = ausgebucht;
    }

    public String getAnAbgemeldet() {
        return anAbgemeldet;
    }

    public void setAnAbgemeldet(String anAbgemeldet) {
        this.anAbgemeldet = anAbgemeldet;
    }

}
