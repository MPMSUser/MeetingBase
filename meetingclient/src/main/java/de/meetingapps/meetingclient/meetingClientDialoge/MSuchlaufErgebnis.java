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
package de.meetingapps.meetingclient.meetingClientDialoge;

import de.meetingapps.meetingportal.meetComEntities.EclAktienregister;
import de.meetingapps.meetingportal.meetComEntities.EclMeldung;
import de.meetingapps.meetingportal.meetComEntities.EclSuchlaufErgebnis;
import de.meetingapps.meetingportal.meetComKonst.KonstPraesenzStatus;
import de.meetingapps.meetingportal.meetComKonst.KonstSkIst;

/**
 * The Class MSuchlaufErgebnis.
 */
public class MSuchlaufErgebnis {

    /** The laufende nummer in array. */
    /*EclSuchlaufErgebnis*/
    public int laufendeNummerInArray = 0;

    /** The ident suchlauf ergebnis. */
    public int identSuchlaufErgebnis = 0;

    /** The entstanden aus string. */
    public String entstandenAusString = "";

    /** The einzel such begriff. */
    public String einzelSuchBegriff = "";

    /** The veraenderung gegenueber letztem suchlauf. */
    public int veraenderungGegenueberLetztemSuchlauf = 0;

    /** The veraenderung gegenueber letztem suchlauf string. */
    public String veraenderungGegenueberLetztemSuchlaufString = "";

    /** The ident aktienregister. */
    public int identAktienregister = 0;

    /** The ident melderegister. */
    public int identMelderegister = 0;

    /** The wurde verarbeitet. */
    public int wurdeVerarbeitet = 0;

    /** The meldungs text. */
    public String meldungsText = "";

    /** The wurde verarbeitet string. */
    public String wurdeVerarbeitetString = "";

    /** The verarbeitet nicht mehr in suchergebnis string. */
    public String verarbeitetNichtMehrInSuchergebnisString = "";

    /** The wurde aus suche ausgegrenzt. */
    public int wurdeAusSucheAusgegrenzt = 0;

    /** The wurde aus suche ausgegrenzt string. */
    public String wurdeAusSucheAusgegrenztString = "";

    /** The ausgegrenzt weil. */
    public String ausgegrenztWeil = "";

    /** The parameter 1. */
    public String parameter1 = "";

    /** The parameter 2. */
    public String parameter2 = "";

    /** The parameter 3. */
    public String parameter3 = "";

    /** The parameter 4. */
    public String parameter4 = "";

    /** The parameter 5. */
    public String parameter5 = "";

    /** The gefundene vollmacht name. */
    public String gefundeneVollmachtName = "";

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

    /** The vollmacht name ort. */
    /*Vollmachten*/
    public String vollmachtNameOrt = "";

    /** The aktionaersnummer. */
    /*Aktienregister und Meldebestand*/
    public String aktionaersnummer = "";

    /** The aktionaersname. */
    public String aktionaersname = "";

    /** The aktionaersort. */
    public String aktionaersort = "";

    /** The besitzart. */
    public String besitzart = "";

    /**
     * Instantiates a new m suchlauf ergebnis.
     *
     * @param pSuchlaufErgebnis the suchlauf ergebnis
     * @param offset            the offset
     */
    public MSuchlaufErgebnis(EclSuchlaufErgebnis pSuchlaufErgebnis, int offset) {
        initSuchlauf(pSuchlaufErgebnis, offset);
    }

    /**
     * Instantiates a new m suchlauf ergebnis.
     *
     * @param pSuchlaufErgebnis the suchlauf ergebnis
     * @param pAktienregister   the aktienregister
     * @param offset            the offset
     */
    public MSuchlaufErgebnis(EclSuchlaufErgebnis pSuchlaufErgebnis, EclAktienregister pAktienregister, int offset) {
        initSuchlauf(pSuchlaufErgebnis, offset);
        initAktienregister(pAktienregister);
    }

    /**
     * Instantiates a new m suchlauf ergebnis.
     *
     * @param pSuchlaufErgebnis the suchlauf ergebnis
     * @param pMeldung          the meldung
     * @param offset            the offset
     */
    public MSuchlaufErgebnis(EclSuchlaufErgebnis pSuchlaufErgebnis, EclMeldung pMeldung, int offset) {
        initSuchlauf(pSuchlaufErgebnis, offset);
        initMeldungen(pMeldung);
    }

    /**
     * Instantiates a new m suchlauf ergebnis.
     *
     * @param pSuchlaufErgebnis the suchlauf ergebnis
     * @param pAktienregister   the aktienregister
     * @param pMeldung          the meldung
     * @param offset            the offset
     */
    public MSuchlaufErgebnis(EclSuchlaufErgebnis pSuchlaufErgebnis, EclAktienregister pAktienregister,
            EclMeldung pMeldung, int offset) {
        initSuchlauf(pSuchlaufErgebnis, offset);
        initAktienregister(pAktienregister);
        initMeldungen(pMeldung);
    }

    /**
     * Instantiates a new m suchlauf ergebnis.
     *
     * @param pSuchlaufErgebnis  the suchlauf ergebnis
     * @param pAktienregister    the aktienregister
     * @param pMeldung           the meldung
     * @param pSonstigeVollmacht the sonstige vollmacht
     * @param offset             the offset
     */
    public MSuchlaufErgebnis(EclSuchlaufErgebnis pSuchlaufErgebnis, EclAktienregister pAktienregister,
            EclMeldung pMeldung, String pSonstigeVollmacht, int offset) {
        initSuchlauf(pSuchlaufErgebnis, offset);
        initAktienregister(pAktienregister);
        initMeldungen(pMeldung);
        vollmachtNameOrt = pSonstigeVollmacht;
    }

    /**
     * Inits the suchlauf.
     *
     * @param pSuchlaufErgebnis the suchlauf ergebnis
     * @param offset            the offset
     */
    private void initSuchlauf(EclSuchlaufErgebnis pSuchlaufErgebnis, int offset) {
        laufendeNummerInArray = offset;
        identSuchlaufErgebnis = pSuchlaufErgebnis.ident;
        switch (pSuchlaufErgebnis.entstandenAus) {
        case 0:
            entstandenAusString = "Suchlauf";
            break;
        case 1:
            entstandenAusString = "RegisterNr";
            break;
        case 2:
            entstandenAusString = "EK-Nummer";
            break;
        case 3:
            entstandenAusString = "SK-Nummer";
            break;
        case 4:
            entstandenAusString = "Name Aktionär";
            break;
        case 5:
            entstandenAusString = "Name Vertreter";
            break;
        case 6:
            entstandenAusString = "Name Akt/Vertr";
            break;
        case 7:
            entstandenAusString = "MeldeIdent";
            break;
        }

        if (pSuchlaufErgebnis.entstandenAus > 0) {
            einzelSuchBegriff = pSuchlaufErgebnis.einzelSuchBegriff;
        }

        veraenderungGegenueberLetztemSuchlauf = pSuchlaufErgebnis.veraenderungGegenueberLetztemSuchlauf;
        if (veraenderungGegenueberLetztemSuchlauf == -1) {
            veraenderungGegenueberLetztemSuchlaufString = "nicht mehr vorhanden";
        }
        if (veraenderungGegenueberLetztemSuchlauf == 1) {
            veraenderungGegenueberLetztemSuchlaufString = "neu";
        }

        identAktienregister = pSuchlaufErgebnis.identAktienregister;
        identMelderegister = pSuchlaufErgebnis.identMelderegister;

        wurdeVerarbeitet = pSuchlaufErgebnis.wurdeVerarbeitet;
        if (wurdeVerarbeitet == 1) {
            wurdeVerarbeitetString = "Ja";
        }

        if (pSuchlaufErgebnis.verarbeitetNichtMehrInSuchergebnis == 1) {
            verarbeitetNichtMehrInSuchergebnisString = "Nicht Mehr Enthalten Aber Verarbeitet";
        }

        wurdeAusSucheAusgegrenzt = pSuchlaufErgebnis.wurdeAusSucheAusgegrenzt;
        if (wurdeAusSucheAusgegrenzt == 1) {
            wurdeAusSucheAusgegrenztString = "Ja";
        }
        ausgegrenztWeil = pSuchlaufErgebnis.ausgegrenztWeil;
        gefundeneVollmachtName = pSuchlaufErgebnis.gefundeneVollmachtName;
        parameter1 = pSuchlaufErgebnis.parameter1;
        parameter2 = pSuchlaufErgebnis.parameter2;
        parameter3 = pSuchlaufErgebnis.parameter3;
        parameter4 = pSuchlaufErgebnis.parameter4;
        parameter5 = pSuchlaufErgebnis.parameter5;

        meldungsText = pSuchlaufErgebnis.meldungsText;

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
                istAngemeldet="Anmeldung storniert";
            }
        }
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
     * Gets the entstanden aus string.
     *
     * @return the entstanden aus string
     */
    public String getEntstandenAusString() {
        return entstandenAusString;
    }

    /**
     * Sets the entstanden aus string.
     *
     * @param entstandenAusString the new entstanden aus string
     */
    public void setEntstandenAusString(String entstandenAusString) {
        this.entstandenAusString = entstandenAusString;
    }

    /**
     * Gets the einzel such begriff.
     *
     * @return the einzel such begriff
     */
    public String getEinzelSuchBegriff() {
        return einzelSuchBegriff;
    }

    /**
     * Sets the einzel such begriff.
     *
     * @param einzelSuchBegriff the new einzel such begriff
     */
    public void setEinzelSuchBegriff(String einzelSuchBegriff) {
        this.einzelSuchBegriff = einzelSuchBegriff;
    }

    /**
     * Gets the veraenderung gegenueber letztem suchlauf string.
     *
     * @return the veraenderung gegenueber letztem suchlauf string
     */
    public String getVeraenderungGegenueberLetztemSuchlaufString() {
        return veraenderungGegenueberLetztemSuchlaufString;
    }

    /**
     * Sets the veraenderung gegenueber letztem suchlauf string.
     *
     * @param veraenderungGegenueberLetztemSuchlaufString the new veraenderung
     *                                                    gegenueber letztem
     *                                                    suchlauf string
     */
    public void setVeraenderungGegenueberLetztemSuchlaufString(String veraenderungGegenueberLetztemSuchlaufString) {
        this.veraenderungGegenueberLetztemSuchlaufString = veraenderungGegenueberLetztemSuchlaufString;
    }

    /**
     * Gets the wurde verarbeitet string.
     *
     * @return the wurde verarbeitet string
     */
    public String getWurdeVerarbeitetString() {
        return wurdeVerarbeitetString;
    }

    /**
     * Sets the wurde verarbeitet string.
     *
     * @param wurdeVerarbeitetString the new wurde verarbeitet string
     */
    public void setWurdeVerarbeitetString(String wurdeVerarbeitetString) {
        this.wurdeVerarbeitetString = wurdeVerarbeitetString;
    }

    /**
     * Gets the verarbeitet nicht mehr in suchergebnis string.
     *
     * @return the verarbeitet nicht mehr in suchergebnis string
     */
    public String getVerarbeitetNichtMehrInSuchergebnisString() {
        return verarbeitetNichtMehrInSuchergebnisString;
    }

    /**
     * Sets the verarbeitet nicht mehr in suchergebnis string.
     *
     * @param verarbeitetNichtMehrInSuchergebnisString the new verarbeitet nicht
     *                                                 mehr in suchergebnis string
     */
    public void setVerarbeitetNichtMehrInSuchergebnisString(String verarbeitetNichtMehrInSuchergebnisString) {
        this.verarbeitetNichtMehrInSuchergebnisString = verarbeitetNichtMehrInSuchergebnisString;
    }

    /**
     * Gets the wurde aus suche ausgegrenzt string.
     *
     * @return the wurde aus suche ausgegrenzt string
     */
    public String getWurdeAusSucheAusgegrenztString() {
        return wurdeAusSucheAusgegrenztString;
    }

    /**
     * Sets the wurde aus suche ausgegrenzt string.
     *
     * @param wurdeAusSucheAusgegrenztString the new wurde aus suche ausgegrenzt
     *                                       string
     */
    public void setWurdeAusSucheAusgegrenztString(String wurdeAusSucheAusgegrenztString) {
        this.wurdeAusSucheAusgegrenztString = wurdeAusSucheAusgegrenztString;
    }

    /**
     * Gets the ausgegrenzt weil.
     *
     * @return the ausgegrenzt weil
     */
    public String getAusgegrenztWeil() {
        return ausgegrenztWeil;
    }

    /**
     * Sets the ausgegrenzt weil.
     *
     * @param ausgegrenztWeil the new ausgegrenzt weil
     */
    public void setAusgegrenztWeil(String ausgegrenztWeil) {
        this.ausgegrenztWeil = ausgegrenztWeil;
    }

    /**
     * Gets the gefundene vollmacht name.
     *
     * @return the gefundene vollmacht name
     */
    public String getGefundeneVollmachtName() {
        return gefundeneVollmachtName;
    }

    /**
     * Sets the gefundene vollmacht name.
     *
     * @param gefundeneVollmachtName the new gefundene vollmacht name
     */
    public void setGefundeneVollmachtName(String gefundeneVollmachtName) {
        this.gefundeneVollmachtName = gefundeneVollmachtName;
    }

    /**
     * Gets the parameter 1.
     *
     * @return the parameter 1
     */
    public String getParameter1() {
        return parameter1;
    }

    /**
     * Sets the parameter 1.
     *
     * @param parameter1 the new parameter 1
     */
    public void setParameter1(String parameter1) {
        this.parameter1 = parameter1;
    }

    /**
     * Gets the parameter 2.
     *
     * @return the parameter 2
     */
    public String getParameter2() {
        return parameter2;
    }

    /**
     * Sets the parameter 2.
     *
     * @param parameter2 the new parameter 2
     */
    public void setParameter2(String parameter2) {
        this.parameter2 = parameter2;
    }

    /**
     * Gets the parameter 3.
     *
     * @return the parameter 3
     */
    public String getParameter3() {
        return parameter3;
    }

    /**
     * Sets the parameter 3.
     *
     * @param parameter3 the new parameter 3
     */
    public void setParameter3(String parameter3) {
        this.parameter3 = parameter3;
    }

    /**
     * Gets the parameter 4.
     *
     * @return the parameter 4
     */
    public String getParameter4() {
        return parameter4;
    }

    /**
     * Sets the parameter 4.
     *
     * @param parameter4 the new parameter 4
     */
    public void setParameter4(String parameter4) {
        this.parameter4 = parameter4;
    }

    /**
     * Gets the parameter 5.
     *
     * @return the parameter 5
     */
    public String getParameter5() {
        return parameter5;
    }

    /**
     * Sets the parameter 5.
     *
     * @param parameter5 the new parameter 5
     */
    public void setParameter5(String parameter5) {
        this.parameter5 = parameter5;
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
     * Gets the vollmacht name ort.
     *
     * @return the vollmacht name ort
     */
    public String getVollmachtNameOrt() {
        return vollmachtNameOrt;
    }

    /**
     * Sets the vollmacht name ort.
     *
     * @param vollmachtNameOrt the new vollmacht name ort
     */
    public void setVollmachtNameOrt(String vollmachtNameOrt) {
        this.vollmachtNameOrt = vollmachtNameOrt;
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
     * Gets the veraenderung gegenueber letztem suchlauf.
     *
     * @return the veraenderung gegenueber letztem suchlauf
     */
    public int getVeraenderungGegenueberLetztemSuchlauf() {
        return veraenderungGegenueberLetztemSuchlauf;
    }

    /**
     * Sets the veraenderung gegenueber letztem suchlauf.
     *
     * @param veraenderungGegenueberLetztemSuchlauf the new veraenderung gegenueber
     *                                              letztem suchlauf
     */
    public void setVeraenderungGegenueberLetztemSuchlauf(int veraenderungGegenueberLetztemSuchlauf) {
        this.veraenderungGegenueberLetztemSuchlauf = veraenderungGegenueberLetztemSuchlauf;
    }

    /**
     * Gets the wurde verarbeitet.
     *
     * @return the wurde verarbeitet
     */
    public int getWurdeVerarbeitet() {
        return wurdeVerarbeitet;
    }

    /**
     * Sets the wurde verarbeitet.
     *
     * @param wurdeVerarbeitet the new wurde verarbeitet
     */
    public void setWurdeVerarbeitet(int wurdeVerarbeitet) {
        this.wurdeVerarbeitet = wurdeVerarbeitet;
    }

    /**
     * Gets the wurde aus suche ausgegrenzt.
     *
     * @return the wurde aus suche ausgegrenzt
     */
    public int getWurdeAusSucheAusgegrenzt() {
        return wurdeAusSucheAusgegrenzt;
    }

    /**
     * Sets the wurde aus suche ausgegrenzt.
     *
     * @param wurdeAusSucheAusgegrenzt the new wurde aus suche ausgegrenzt
     */
    public void setWurdeAusSucheAusgegrenzt(int wurdeAusSucheAusgegrenzt) {
        this.wurdeAusSucheAusgegrenzt = wurdeAusSucheAusgegrenzt;
    }

    /**
     * Gets the ident suchlauf ergebnis.
     *
     * @return the ident suchlauf ergebnis
     */
    public int getIdentSuchlaufErgebnis() {
        return identSuchlaufErgebnis;
    }

    /**
     * Sets the ident suchlauf ergebnis.
     *
     * @param identSuchlaufErgebnis the new ident suchlauf ergebnis
     */
    public void setIdentSuchlaufErgebnis(int identSuchlaufErgebnis) {
        this.identSuchlaufErgebnis = identSuchlaufErgebnis;
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
     * Gets the ident melderegister.
     *
     * @return the ident melderegister
     */
    public int getIdentMelderegister() {
        return identMelderegister;
    }

    /**
     * Sets the ident melderegister.
     *
     * @param identMelderegister the new ident melderegister
     */
    public void setIdentMelderegister(int identMelderegister) {
        this.identMelderegister = identMelderegister;
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
     * Gets the meldungs text.
     *
     * @return the meldungs text
     */
    public String getMeldungsText() {
        return meldungsText;
    }

    /**
     * Sets the meldungs text.
     *
     * @param meldungsText the new meldungs text
     */
    public void setMeldungsText(String meldungsText) {
        this.meldungsText = meldungsText;
    }

}
