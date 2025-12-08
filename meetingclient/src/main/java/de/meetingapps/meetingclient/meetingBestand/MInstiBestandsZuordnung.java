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
package de.meetingapps.meetingclient.meetingBestand;

import de.meetingapps.meetingportal.meetComEntities.EclAktienregister;
import de.meetingapps.meetingportal.meetComEntities.EclInstiBestandsZuordnung;
import de.meetingapps.meetingportal.meetComEntities.EclMeldung;
import de.meetingapps.meetingportal.meetComEntities.EclUserLogin;
import de.meetingapps.meetingportal.meetComKonst.KonstPraesenzStatus;
import de.meetingapps.meetingportal.meetComKonst.KonstSkIst;

/**
 * The Class MInstiBestandsZuordnung.
 */
public class MInstiBestandsZuordnung {

    /** The laufende nummer in array. */
    /*EclInstiBestandsZuordnung*/
    public int laufendeNummerInArray = 0;

    /** The mandant. */
    public int mandant = 0;

    /** The ident. */
    public int ident = 0;

    /** The beschreibung. */
    public String beschreibung = "";

    /** The zugeordnet register oder meldungen. */
    public int zugeordnetRegisterOderMeldungen = 0;

    /** The ident aktienregister. */
    public int identAktienregister = 0;

    /** The zugeordnete stimmen. */
    public long zugeordneteStimmen = 0;

    /** The ident meldung. */
    public int identMeldung = 0;

    /** The ident insti. */
    public int identInsti = 0;

    /** The ident user login. */
    public int identUserLogin = 0;

    /** The verarbeitet sammel anmeldung gedruckt. */
    public int verarbeitetSammelAnmeldungGedruckt = 0;

    /** The verarbeitet 2. */
    public int verarbeitet2 = 0;

    /** The verarbeitet 3. */
    public int verarbeitet3 = 0;

    /** The verarbeitet 4. */
    public int verarbeitet4 = 0;

    /** The verarbeitet 5. */
    public int verarbeitet5 = 0;

    /** The stueck aktien. */
    /*Aktienregister*/
    public Long stueckAktien = (long) 0;

    /*Meldebestand*/
    /** ==0 => kein Meldebestand zugeordnet. */
    public Integer meldeIdent = 0;

    /** The ek nr. */
    public String ekNr = "";

    /** The sk nr. */
    public String skNr = "";

    /** The ist oder in sammelkarte. */
    public String istOderInSammelkarte = "";

    /** The vertreter name ort. */
    public String vertreterNameOrt = "";

    /** The ist praesent. */
    public String istPraesent = "";

    /** The ist angemeldet. */
    public String istAngemeldet = "";

    /** The ist gesperrt. */
    public boolean istGesperrt = false;

    /** The ist gesperrt string. */
    public String istGesperrtString = "";

    /** The stueck aktien meldung. */
    public Long stueckAktienMeldung = (long) 0;

    /** The aktionaersnummer. */
    /*Aktienregister und Meldebestand*/
    public String aktionaersnummer = "";

    /** The aktionaersname. */
    public String aktionaersname = "";

    /** The aktionaersort. */
    public String aktionaersort = "";

    /** The besitzart. */
    public String besitzart = "";

    /** The benutzer kennung. */
    /*EclUserLogin*/
    public String benutzerKennung = "";

    /** The benutzer name. */
    public String benutzerName = "";

    /**
     * Instantiates a new m insti bestands zuordnung.
     *
     * @param pInstiBestandsZuordnung the insti bestands zuordnung
     * @param pAktienregister         the aktienregister
     * @param pMeldung                the meldung
     * @param pUserLogin              the user login
     * @param offset                  the offset
     */
    public MInstiBestandsZuordnung(EclInstiBestandsZuordnung pInstiBestandsZuordnung, EclAktienregister pAktienregister,
            EclMeldung pMeldung, EclUserLogin pUserLogin, int offset) {
        initInstiBestandsZuordnung(pInstiBestandsZuordnung, offset);
        initAktienregister(pAktienregister);
        initMeldungen(pMeldung);
        initUserLogin(pUserLogin);
    }

    /**
     * Inits the insti bestands zuordnung.
     *
     * @param pInstiBestandsZuordnung the insti bestands zuordnung
     * @param offset                  the offset
     */
    private void initInstiBestandsZuordnung(EclInstiBestandsZuordnung pInstiBestandsZuordnung, int offset) {
        laufendeNummerInArray = offset;
        mandant = pInstiBestandsZuordnung.mandant;
        ident = pInstiBestandsZuordnung.ident;
        beschreibung = pInstiBestandsZuordnung.beschreibung;
        zugeordnetRegisterOderMeldungen = pInstiBestandsZuordnung.zugeordnetRegisterOderMeldungen;
        identAktienregister = pInstiBestandsZuordnung.identAktienregister;
        zugeordneteStimmen = pInstiBestandsZuordnung.zugeordneteStimmen;
        identMeldung = pInstiBestandsZuordnung.identMeldung;
        identInsti = pInstiBestandsZuordnung.identInsti;
        identUserLogin = pInstiBestandsZuordnung.identUserLogin;
        verarbeitetSammelAnmeldungGedruckt = pInstiBestandsZuordnung.verarbeitetSammelAnmeldungGedruckt;
        verarbeitet2 = pInstiBestandsZuordnung.verarbeitet2;
        verarbeitet3 = pInstiBestandsZuordnung.verarbeitet3;
        verarbeitet4 = pInstiBestandsZuordnung.verarbeitet4;
        verarbeitet5 = pInstiBestandsZuordnung.verarbeitet5;
    }

    /**
     * Inits the aktienregister.
     *
     * @param pAktienregister the aktienregister
     */
    private void initAktienregister(EclAktienregister pAktienregister) {
        aktionaersnummer = pAktienregister.aktionaersnummer;
        aktionaersname = pAktienregister.nameKomplett;
        aktionaersort = pAktienregister.ort;
        besitzart = pAktienregister.besitzart;
        stueckAktien = pAktienregister.stueckAktien;
    }

    /**
     * Inits the meldungen.
     *
     * @param pMeldung the meldung
     */
    private void initMeldungen(EclMeldung pMeldung) {
        if (pMeldung.meldungsIdent != 0) {
            meldeIdent = pMeldung.meldungsIdent;

            if (pMeldung.klasse == 0) {
                istOderInSammelkarte = "Gast";
            } else {
                if (pMeldung.meldungstyp == 1) {
                    istOderInSammelkarte = "Aktionär";
                }
                if (pMeldung.meldungstyp == 2) {
                    istOderInSammelkarte = "Ist Sammelkarte " + KonstSkIst.getText(pMeldung.skIst);
                }
                if (pMeldung.meldungstyp == 3) {
                    istOderInSammelkarte = "In Sammelkarte "
                            + KonstSkIst.getText(pMeldung.meldungEnthaltenInSammelkarteArt);
                }
            }

            aktionaersnummer = pMeldung.aktionaersnummer;
            aktionaersname = pMeldung.kurzName;
            aktionaersort = pMeldung.kurzOrt;
            besitzart = pMeldung.besitzart;
            stueckAktienMeldung = pMeldung.stueckAktien;
            ekNr = pMeldung.zutrittsIdent;
            skNr = pMeldung.stimmkarte;
            if (!pMeldung.vertreterName.isEmpty()) {
                vertreterNameOrt = pMeldung.vertreterName + " " + pMeldung.vertreterOrt;
            }

            istPraesent = KonstPraesenzStatus.getText(pMeldung.statusPraesenz);
            istAngemeldet = "Angemeldet";
            if (pMeldung.meldungAktiv == 2) {
                istGesperrtString = "Meldung ist gesperrt";
            }
        }
    }

    /**
     * Inits the user login.
     *
     * @param pUserLogin the user login
     */
    private void initUserLogin(EclUserLogin pUserLogin) {
        benutzerKennung = pUserLogin.kennung;
        benutzerName = pUserLogin.name;
    }

    /**
     * ******************Standard Getter und Setter*************************.
     *
     * @return the laufende nummer in array
     */

    public int getLaufendeNummerInArray() {
        return laufendeNummerInArray;
    }

    /**
     * Sets the laufende nummer in array.
     *
     * @param laufendeNummerInArray the new laufende nummer in array
     */
    public void setLaufendeNummerInArray(int laufendeNummerInArray) {
        this.laufendeNummerInArray = laufendeNummerInArray;
    }

    /**
     * Gets the mandant.
     *
     * @return the mandant
     */
    public int getMandant() {
        return mandant;
    }

    /**
     * Sets the mandant.
     *
     * @param mandant the new mandant
     */
    public void setMandant(int mandant) {
        this.mandant = mandant;
    }

    /**
     * Gets the ident.
     *
     * @return the ident
     */
    public int getIdent() {
        return ident;
    }

    /**
     * Sets the ident.
     *
     * @param ident the new ident
     */
    public void setIdent(int ident) {
        this.ident = ident;
    }

    /**
     * Gets the beschreibung.
     *
     * @return the beschreibung
     */
    public String getBeschreibung() {
        return beschreibung;
    }

    /**
     * Sets the beschreibung.
     *
     * @param beschreibung the new beschreibung
     */
    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }

    /**
     * Gets the zugeordnet register oder meldungen.
     *
     * @return the zugeordnet register oder meldungen
     */
    public int getZugeordnetRegisterOderMeldungen() {
        return zugeordnetRegisterOderMeldungen;
    }

    /**
     * Sets the zugeordnet register oder meldungen.
     *
     * @param zugeordnetRegisterOderMeldungen the new zugeordnet register oder
     *                                        meldungen
     */
    public void setZugeordnetRegisterOderMeldungen(int zugeordnetRegisterOderMeldungen) {
        this.zugeordnetRegisterOderMeldungen = zugeordnetRegisterOderMeldungen;
    }

    /**
     * Gets the ident aktienregister.
     *
     * @return the ident aktienregister
     */
    public int getIdentAktienregister() {
        return identAktienregister;
    }

    /**
     * Sets the ident aktienregister.
     *
     * @param identAktienregister the new ident aktienregister
     */
    public void setIdentAktienregister(int identAktienregister) {
        this.identAktienregister = identAktienregister;
    }

    /**
     * Gets the zugeordnete stimmen.
     *
     * @return the zugeordnete stimmen
     */
    public long getZugeordneteStimmen() {
        return zugeordneteStimmen;
    }

    /**
     * Sets the zugeordnete stimmen.
     *
     * @param zugeordneteStimmen the new zugeordnete stimmen
     */
    public void setZugeordneteStimmen(long zugeordneteStimmen) {
        this.zugeordneteStimmen = zugeordneteStimmen;
    }

    /**
     * Gets the ident meldung.
     *
     * @return the ident meldung
     */
    public int getIdentMeldung() {
        return identMeldung;
    }

    /**
     * Sets the ident meldung.
     *
     * @param identMeldung the new ident meldung
     */
    public void setIdentMeldung(int identMeldung) {
        this.identMeldung = identMeldung;
    }

    /**
     * Gets the ident insti.
     *
     * @return the ident insti
     */
    public int getIdentInsti() {
        return identInsti;
    }

    /**
     * Sets the ident insti.
     *
     * @param identInsti the new ident insti
     */
    public void setIdentInsti(int identInsti) {
        this.identInsti = identInsti;
    }

    /**
     * Gets the ident user login.
     *
     * @return the ident user login
     */
    public int getIdentUserLogin() {
        return identUserLogin;
    }

    /**
     * Sets the ident user login.
     *
     * @param identUserLogin the new ident user login
     */
    public void setIdentUserLogin(int identUserLogin) {
        this.identUserLogin = identUserLogin;
    }

    /**
     * Gets the verarbeitet sammel anmeldung gedruckt.
     *
     * @return the verarbeitet sammel anmeldung gedruckt
     */
    public int getVerarbeitetSammelAnmeldungGedruckt() {
        return verarbeitetSammelAnmeldungGedruckt;
    }

    /**
     * Sets the verarbeitet sammel anmeldung gedruckt.
     *
     * @param verarbeitetSammelAnmeldungGedruckt the new verarbeitet sammel
     *                                           anmeldung gedruckt
     */
    public void setVerarbeitetSammelAnmeldungGedruckt(int verarbeitetSammelAnmeldungGedruckt) {
        this.verarbeitetSammelAnmeldungGedruckt = verarbeitetSammelAnmeldungGedruckt;
    }

    /**
     * Gets the verarbeitet 2.
     *
     * @return the verarbeitet 2
     */
    public int getVerarbeitet2() {
        return verarbeitet2;
    }

    /**
     * Sets the verarbeitet 2.
     *
     * @param verarbeitet2 the new verarbeitet 2
     */
    public void setVerarbeitet2(int verarbeitet2) {
        this.verarbeitet2 = verarbeitet2;
    }

    /**
     * Gets the verarbeitet 3.
     *
     * @return the verarbeitet 3
     */
    public int getVerarbeitet3() {
        return verarbeitet3;
    }

    /**
     * Sets the verarbeitet 3.
     *
     * @param verarbeitet3 the new verarbeitet 3
     */
    public void setVerarbeitet3(int verarbeitet3) {
        this.verarbeitet3 = verarbeitet3;
    }

    /**
     * Gets the verarbeitet 4.
     *
     * @return the verarbeitet 4
     */
    public int getVerarbeitet4() {
        return verarbeitet4;
    }

    /**
     * Sets the verarbeitet 4.
     *
     * @param verarbeitet4 the new verarbeitet 4
     */
    public void setVerarbeitet4(int verarbeitet4) {
        this.verarbeitet4 = verarbeitet4;
    }

    /**
     * Gets the verarbeitet 5.
     *
     * @return the verarbeitet 5
     */
    public int getVerarbeitet5() {
        return verarbeitet5;
    }

    /**
     * Sets the verarbeitet 5.
     *
     * @param verarbeitet5 the new verarbeitet 5
     */
    public void setVerarbeitet5(int verarbeitet5) {
        this.verarbeitet5 = verarbeitet5;
    }

    /**
     * Gets the stueck aktien.
     *
     * @return the stueck aktien
     */
    public Long getStueckAktien() {
        return stueckAktien;
    }

    /**
     * Sets the stueck aktien.
     *
     * @param stueckAktien the new stueck aktien
     */
    public void setStueckAktien(Long stueckAktien) {
        this.stueckAktien = stueckAktien;
    }

    /**
     * Gets the melde ident.
     *
     * @return the melde ident
     */
    public Integer getMeldeIdent() {
        return meldeIdent;
    }

    /**
     * Sets the melde ident.
     *
     * @param meldeIdent the new melde ident
     */
    public void setMeldeIdent(Integer meldeIdent) {
        this.meldeIdent = meldeIdent;
    }

    /**
     * Gets the ek nr.
     *
     * @return the ek nr
     */
    public String getEkNr() {
        return ekNr;
    }

    /**
     * Sets the ek nr.
     *
     * @param ekNr the new ek nr
     */
    public void setEkNr(String ekNr) {
        this.ekNr = ekNr;
    }

    /**
     * Gets the sk nr.
     *
     * @return the sk nr
     */
    public String getSkNr() {
        return skNr;
    }

    /**
     * Sets the sk nr.
     *
     * @param skNr the new sk nr
     */
    public void setSkNr(String skNr) {
        this.skNr = skNr;
    }

    /**
     * Gets the ist oder in sammelkarte.
     *
     * @return the ist oder in sammelkarte
     */
    public String getIstOderInSammelkarte() {
        return istOderInSammelkarte;
    }

    /**
     * Sets the ist oder in sammelkarte.
     *
     * @param istOderInSammelkarte the new ist oder in sammelkarte
     */
    public void setIstOderInSammelkarte(String istOderInSammelkarte) {
        this.istOderInSammelkarte = istOderInSammelkarte;
    }

    /**
     * Gets the vertreter name ort.
     *
     * @return the vertreter name ort
     */
    public String getVertreterNameOrt() {
        return vertreterNameOrt;
    }

    /**
     * Sets the vertreter name ort.
     *
     * @param vertreterNameOrt the new vertreter name ort
     */
    public void setVertreterNameOrt(String vertreterNameOrt) {
        this.vertreterNameOrt = vertreterNameOrt;
    }

    /**
     * Gets the ist praesent.
     *
     * @return the ist praesent
     */
    public String getIstPraesent() {
        return istPraesent;
    }

    /**
     * Sets the ist praesent.
     *
     * @param istPraesent the new ist praesent
     */
    public void setIstPraesent(String istPraesent) {
        this.istPraesent = istPraesent;
    }

    /**
     * Gets the ist angemeldet.
     *
     * @return the ist angemeldet
     */
    public String getIstAngemeldet() {
        return istAngemeldet;
    }

    /**
     * Sets the ist angemeldet.
     *
     * @param istAngemeldet the new ist angemeldet
     */
    public void setIstAngemeldet(String istAngemeldet) {
        this.istAngemeldet = istAngemeldet;
    }

    /**
     * Checks if is ist gesperrt.
     *
     * @return true, if is ist gesperrt
     */
    public boolean isIstGesperrt() {
        return istGesperrt;
    }

    /**
     * Sets the ist gesperrt.
     *
     * @param istGesperrt the new ist gesperrt
     */
    public void setIstGesperrt(boolean istGesperrt) {
        this.istGesperrt = istGesperrt;
    }

    /**
     * Gets the ist gesperrt string.
     *
     * @return the ist gesperrt string
     */
    public String getIstGesperrtString() {
        return istGesperrtString;
    }

    /**
     * Sets the ist gesperrt string.
     *
     * @param istGesperrtString the new ist gesperrt string
     */
    public void setIstGesperrtString(String istGesperrtString) {
        this.istGesperrtString = istGesperrtString;
    }

    /**
     * Gets the stueck aktien meldung.
     *
     * @return the stueck aktien meldung
     */
    public Long getStueckAktienMeldung() {
        return stueckAktienMeldung;
    }

    /**
     * Sets the stueck aktien meldung.
     *
     * @param stueckAktienMeldung the new stueck aktien meldung
     */
    public void setStueckAktienMeldung(Long stueckAktienMeldung) {
        this.stueckAktienMeldung = stueckAktienMeldung;
    }

    /**
     * Gets the aktionaersnummer.
     *
     * @return the aktionaersnummer
     */
    public String getAktionaersnummer() {
        return aktionaersnummer;
    }

    /**
     * Sets the aktionaersnummer.
     *
     * @param aktionaersnummer the new aktionaersnummer
     */
    public void setAktionaersnummer(String aktionaersnummer) {
        this.aktionaersnummer = aktionaersnummer;
    }

    /**
     * Gets the aktionaersname.
     *
     * @return the aktionaersname
     */
    public String getAktionaersname() {
        return aktionaersname;
    }

    /**
     * Sets the aktionaersname.
     *
     * @param aktionaersname the new aktionaersname
     */
    public void setAktionaersname(String aktionaersname) {
        this.aktionaersname = aktionaersname;
    }

    /**
     * Gets the aktionaersort.
     *
     * @return the aktionaersort
     */
    public String getAktionaersort() {
        return aktionaersort;
    }

    /**
     * Sets the aktionaersort.
     *
     * @param aktionaersort the new aktionaersort
     */
    public void setAktionaersort(String aktionaersort) {
        this.aktionaersort = aktionaersort;
    }

    /**
     * Gets the besitzart.
     *
     * @return the besitzart
     */
    public String getBesitzart() {
        return besitzart;
    }

    /**
     * Sets the besitzart.
     *
     * @param besitzart the new besitzart
     */
    public void setBesitzart(String besitzart) {
        this.besitzart = besitzart;
    }

    /**
     * Gets the benutzer kennung.
     *
     * @return the benutzer kennung
     */
    public String getBenutzerKennung() {
        return benutzerKennung;
    }

    /**
     * Sets the benutzer kennung.
     *
     * @param benutzerKennung the new benutzer kennung
     */
    public void setBenutzerKennung(String benutzerKennung) {
        this.benutzerKennung = benutzerKennung;
    }

    /**
     * Gets the benutzer name.
     *
     * @return the benutzer name
     */
    public String getBenutzerName() {
        return benutzerName;
    }

    /**
     * Sets the benutzer name.
     *
     * @param benutzerName the new benutzer name
     */
    public void setBenutzerName(String benutzerName) {
        this.benutzerName = benutzerName;
    }

}
