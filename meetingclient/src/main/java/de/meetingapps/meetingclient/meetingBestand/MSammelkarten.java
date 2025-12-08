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

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclMeldung;
import de.meetingapps.meetingportal.meetComEntities.EclWillensErklVollmachtenAnDritte;

/**
 * The Class MSammelkarten.
 */
public class MSammelkarten {

    /** The log drucken. */
    private int logDrucken = 3;

    /** The meldungs ident. */
    public Integer meldungsIdent = 0;

    /** The meldung aktiv. */
    public Integer meldungAktiv = 1;

    /** The sk ist. */
    public Integer skIst = 0;

    /** The sk weisungsart zulaessig. */
    public Integer skWeisungsartZulaessig = 0;

    /** The stueck aktien. */
    public Long stueckAktien = (long) 0;

    /** The zutritts ident aktuell. */
    public String zutrittsIdentAktuell = "";

    /** The zutritts ident. */
    public String zutrittsIdent = "";

    /** The stimmkarte aktuell. */
    public String stimmkarteAktuell = "";

    /** The stimmkarte. */
    public String stimmkarte = "";

    /** The gruppe. */
    public int gruppe = 0;

    /** The meldung aktiv string. */
    public String meldungAktivString = "";

    /** name+#+vorname+#+ort. */
    public String bezeichnung = "";

    /** The gattung string. */
    public String gattungString = "";

    /** The kommentar. */
    public String kommentar = "";

    /** The sk ist string. */
    public String skIstString = "";

    /** The sk offenlegung string. */
    public String skOffenlegungString = "";

    /** The verdeckt string. */
    public String verdecktString = "";

    /** The sk weisungsart zulaessig string. */
    public String skWeisungsartZulaessigString = "";

    /** The sk buchbar internet string. */
    public String skBuchbarInternetString = "";

    /** The sk buchbar papier string. */
    public String skBuchbarPapierString = "";

    /** The sk buchbar HV string. */
    public String skBuchbarHVString = "";

    /** The sk buchbar vollmacht dritte string. */
    public String skBuchbarVollmachtDritteString = "";

    /** The sk buchbar vollmacht dritte HV string. */
    public String skBuchbarVollmachtDritteHVString = "";

    /** The vollmacht dritte. */
    public String vollmachtDritte = "";

    /** The ist oder war praesent. */
    public String istOderWarPraesent = "";

    /** The sonstiges. */
    public String[] sonstiges = { "0", "1", "2" };

    /**
     * Gets the sonstiges.
     *
     * @param o the o
     * @return the sonstiges
     */
    public String getSonstiges(int o) {
        return sonstiges[o];
    }

    /**
     * Gets the ist oder war praesent.
     *
     * @return the ist oder war praesent
     */
    public String getIstOderWarPraesent() {
        return istOderWarPraesent;
    }

    /**
     * Sets the ist oder war praesent.
     *
     * @param istOderWarPraesent the new ist oder war praesent
     */
    public void setIstOderWarPraesent(String istOderWarPraesent) {
        this.istOderWarPraesent = istOderWarPraesent;
    }

    /**
     * Instantiates a new m sammelkarten.
     *
     * @param pSammelkarte the sammelkarte
     * @param pDbBundle    the db bundle
     */
    public MSammelkarten(EclMeldung pSammelkarte, DbBundle pDbBundle) {
        this.meldungsIdent = pSammelkarte.meldungsIdent;

        belegeMeldungAktiv(pSammelkarte);

        this.skIst = pSammelkarte.skIst;
        this.skWeisungsartZulaessig = pSammelkarte.skWeisungsartZulaessig;
        this.stueckAktien = pSammelkarte.stueckAktien;

        this.zutrittsIdent = pSammelkarte.zutrittsIdent;
        this.zutrittsIdentAktuell = pSammelkarte.zutrittsIdent;

        this.stimmkarte = pSammelkarte.stimmkarte;
        this.stimmkarteAktuell = pSammelkarte.stimmkarte;

        this.gruppe = pSammelkarte.gruppe;

        belegeNameOrt(pSammelkarte);

        this.gattungString = pDbBundle.param.paramBasis.getGattungBezeichnungKurz(pSammelkarte.gattung);
        this.kommentar = pSammelkarte.zusatzfeld2;

        belegeSkIstString(pSammelkarte);
        belegeSkOffenlegungString(pSammelkarte);
        belegeVerdecktString(pSammelkarte);
        belegeSkWeisungsartZulaessigString(pSammelkarte);
        belegeSkBuchbarInternetString(pSammelkarte);
        belegeSkBuchbarPapierString(pSammelkarte);
        belegeSkBuchbarHV(pSammelkarte);
        belegeSkBuchbarVollmachtDritte(pSammelkarte);
        belegeSstOderWarPraesent(pSammelkarte);
    }

    /**
     * Fügt pZutrittsIdent zu zutrittsIdent hinzu, außer es ist die bereits
     * zugeordnete (denn diese ist bereits enthalten).
     *
     * @param pZutrittsIdent the zutritts ident
     */
    public void addZutrittsIdent(String pZutrittsIdent) {
        if (pZutrittsIdent.equals(this.zutrittsIdentAktuell)) {
            return;
        }
        if (!this.zutrittsIdent.isEmpty()) {
            zutrittsIdent = zutrittsIdent + "#";
        }
        zutrittsIdent = zutrittsIdent + pZutrittsIdent;
    }

    /**
     * Fügt pStimmkarte zu stimmkarte hinzu, außer es ist die bereits zugeordnete
     * (denn diese ist bereits enthalten).
     *
     * @param pStimmkarte the stimmkarte
     */
    public void addStimmkarte(String pStimmkarte) {
        if (pStimmkarte.equals(this.stimmkarteAktuell)) {
            return;
        }
        if (!this.stimmkarte.isEmpty()) {
            stimmkarte = stimmkarte + "#";
        }
        stimmkarte = stimmkarte + pStimmkarte;
    }

    /**
     * Fügt Vollmacht an Dritte zu vollmachten hinzu.
     *
     * @param lVollmachtDritte the l vollmacht dritte
     */
    public void addVollmacht(EclWillensErklVollmachtenAnDritte lVollmachtDritte) {
        if (!vollmachtDritte.isEmpty()) {
            vollmachtDritte = vollmachtDritte + "#";
        }
        vollmachtDritte = vollmachtDritte + lVollmachtDritte.bevollmaechtigtePerson.name + ", "
                + lVollmachtDritte.bevollmaechtigtePerson.vorname + "#" + lVollmachtDritte.bevollmaechtigtePerson.ort;
    }

    /**
     * *********Spezial-Setter*************************.
     *
     * @param pSammelkarte the sammelkarte
     */
    public void belegeMeldungAktiv(EclMeldung pSammelkarte) {
        this.meldungAktiv = pSammelkarte.meldungAktiv;
        if (this.meldungAktiv != 1) {
            this.meldungAktivString = "nein";
        } else {
            this.meldungAktivString = "ja";
        }
    }

    /**
     * Belege name ort.
     *
     * @param pSammelkarte the sammelkarte
     */
    public void belegeNameOrt(EclMeldung pSammelkarte) {
        this.bezeichnung = pSammelkarte.name;
        if (!pSammelkarte.vorname.isEmpty()) {
            this.bezeichnung = this.bezeichnung + "#" + pSammelkarte.vorname;
        }
        if (!pSammelkarte.ort.isEmpty()) {
            this.bezeichnung = this.bezeichnung + "#" + pSammelkarte.ort;
        }
    }

    /**
     * Belege sk ist string.
     *
     * @param pSammelkarte the sammelkarte
     */
    public void belegeSkIstString(EclMeldung pSammelkarte) {
        skIstString = "";
        switch (pSammelkarte.skIst) {
        case 1:
            skIstString = "KIAV";
            break;
        case 2:
            skIstString = "SRV";
            break;
        case 3:
            skIstString = "Orga";
            break;
        case 4:
            skIstString = "Briefwahl";
            break;
        case 5:
            skIstString = "Dauervoll.";
            break;
        }
    }

    /**
     * Belege sk offenlegung string.
     *
     * @param pSammelkarte the sammelkarte
     */
    public void belegeSkOffenlegungString(EclMeldung pSammelkarte) {
        skOffenlegungString = "";
        switch (pSammelkarte.skOffenlegung) {
        case -1:
            skOffenlegungString = "Nein";
            break;
        case 0:
            skOffenlegungString = "wie Par.";
            break;
        case 1:
            skOffenlegungString = "Ja";
            break;
        }
    }

    /**
     * Belege verdeckt string.
     *
     * @param pSammelkarte the sammelkarte
     */
    public void belegeVerdecktString(EclMeldung pSammelkarte) {
        switch (pSammelkarte.zusatzfeld1) {
        case "1":
            verdecktString = "Ja-in Gesamtsumme erlaubt";
            break;
        case "2":
            verdecktString = "Ja-in Summe Liste erlaubt";
            break;
        case "3":
            verdecktString = "Ja-immer";
            break;
        default:
            verdecktString = "Nein";
            break;
        }
    }

    /**
     * Belege sk weisungsart zulaessig string.
     *
     * @param pSammelkarte the sammelkarte
     */
    public void belegeSkWeisungsartZulaessigString(EclMeldung pSammelkarte) {
        this.skWeisungsartZulaessigString = "";
        int eingefuegt = 0;
        if ((pSammelkarte.skWeisungsartZulaessig & 2) == 2) {
            if (eingefuegt == 1) {
                skWeisungsartZulaessigString = skWeisungsartZulaessigString + "#";
            }
            skWeisungsartZulaessigString = skWeisungsartZulaessigString + "ohne Weisung";
            eingefuegt = 1;
        }
        if ((pSammelkarte.skWeisungsartZulaessig & 4) == 4) {
            if (eingefuegt == 1) {
                skWeisungsartZulaessigString = skWeisungsartZulaessigString + "#";
            }
            skWeisungsartZulaessigString = skWeisungsartZulaessigString + "Dedizierte Weisung";
            eingefuegt = 1;
        }
    }

    /**
     * Belege sk buchbar internet string.
     *
     * @param pSammelkarte the sammelkarte
     */
    public void belegeSkBuchbarInternetString(EclMeldung pSammelkarte) {
        if (pSammelkarte.skBuchbarInternet == 1) {
            skBuchbarInternetString = "Ja";
        }
    }

    /**
     * Belege sk buchbar papier string.
     *
     * @param pSammelkarte the sammelkarte
     */
    public void belegeSkBuchbarPapierString(EclMeldung pSammelkarte) {
        if (pSammelkarte.skBuchbarPapier == 1) {
            skBuchbarPapierString = "Ja";
        }
    }

    /**
     * Belege sk buchbar HV.
     *
     * @param pSammelkarte the sammelkarte
     */
    public void belegeSkBuchbarHV(EclMeldung pSammelkarte) {
        if (pSammelkarte.skBuchbarHV == 1) {
            skBuchbarHVString = "Ja";
        }
    }

    /**
     * Belege sk buchbar vollmacht dritte.
     *
     * @param pSammelkarte the sammelkarte
     */
    public void belegeSkBuchbarVollmachtDritte(EclMeldung pSammelkarte) {
        if (pSammelkarte.skBuchbarVollmachtDritte == 1) {
            skBuchbarVollmachtDritteString = "Ja";
        }
    }

    /**
     * Belege sk buchbar vollmacht dritte HV.
     *
     * @param pSammelkarte the sammelkarte
     */
    public void belegeSkBuchbarVollmachtDritteHV(EclMeldung pSammelkarte) {
        CaBug.druckeLog("Start", logDrucken, 10);
        if (pSammelkarte.skBuchbarVollmachtDritteHV == 1) {
            skBuchbarVollmachtDritteHVString = "Ja";
        }
        CaBug.druckeLog("Ende", logDrucken, 10);
    }

    /**
     * Belege sst oder war praesent.
     *
     * @param pSammelkarte the sammelkarte
     */
    public void belegeSstOderWarPraesent(EclMeldung pSammelkarte) {
        switch (pSammelkarte.statusPraesenz) {
        case 0:
            istOderWarPraesent = "Nein";
            break;
        case 1:
            istOderWarPraesent = "Ja";
            break;
        case 2:
            istOderWarPraesent = "War";
            break;
        }
    }

    /**
     * ***********Standard Getter und Setter**********************.
     *
     * @return the meldungs ident
     */

    public Integer getMeldungsIdent() {
        return meldungsIdent;
    }

    /**
     * Sets the meldungs ident.
     *
     * @param meldungsIdent the new meldungs ident
     */
    public void setMeldungsIdent(Integer meldungsIdent) {
        this.meldungsIdent = meldungsIdent;
    }

    /**
     * Gets the meldung aktiv.
     *
     * @return the meldung aktiv
     */
    public Integer getMeldungAktiv() {
        return meldungAktiv;
    }

    /**
     * Sets the meldung aktiv.
     *
     * @param meldungAktiv the new meldung aktiv
     */
    public void setMeldungAktiv(Integer meldungAktiv) {
        this.meldungAktiv = meldungAktiv;
    }

    /**
     * Gets the sk ist.
     *
     * @return the sk ist
     */
    public Integer getSkIst() {
        return skIst;
    }

    /**
     * Sets the sk ist.
     *
     * @param skIst the new sk ist
     */
    public void setSkIst(Integer skIst) {
        this.skIst = skIst;
    }

    /**
     * Gets the sk weisungsart zulaessig.
     *
     * @return the sk weisungsart zulaessig
     */
    public Integer getSkWeisungsartZulaessig() {
        return skWeisungsartZulaessig;
    }

    /**
     * Sets the sk weisungsart zulaessig.
     *
     * @param skWeisungsartZulaessig the new sk weisungsart zulaessig
     */
    public void setSkWeisungsartZulaessig(Integer skWeisungsartZulaessig) {
        this.skWeisungsartZulaessig = skWeisungsartZulaessig;
    }

    /**
     * Gets the zutritts ident.
     *
     * @return the zutritts ident
     */
    public String getZutrittsIdent() {
        return zutrittsIdent;
    }

    /**
     * Sets the zutritts ident.
     *
     * @param zutrittsIdent the new zutritts ident
     */
    public void setZutrittsIdent(String zutrittsIdent) {
        this.zutrittsIdent = zutrittsIdent;
    }

    /**
     * Gets the stimmkarte.
     *
     * @return the stimmkarte
     */
    public String getStimmkarte() {
        return stimmkarte;
    }

    /**
     * Sets the stimmkarte.
     *
     * @param stimmkarte the new stimmkarte
     */
    public void setStimmkarte(String stimmkarte) {
        this.stimmkarte = stimmkarte;
    }

    /**
     * Gets the bezeichnung.
     *
     * @return the bezeichnung
     */
    public String getBezeichnung() {
        return bezeichnung;
    }

    /**
     * Sets the bezeichnung.
     *
     * @param bezeichnung the new bezeichnung
     */
    public void setBezeichnung(String bezeichnung) {
        this.bezeichnung = bezeichnung;
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
     * Gets the gattung string.
     *
     * @return the gattung string
     */
    public String getGattungString() {
        return gattungString;
    }

    /**
     * Sets the gattung string.
     *
     * @param gattungString the new gattung string
     */
    public void setGattungString(String gattungString) {
        this.gattungString = gattungString;
    }

    /**
     * Gets the kommentar.
     *
     * @return the kommentar
     */
    public String getKommentar() {
        return kommentar;
    }

    /**
     * Sets the kommentar.
     *
     * @param kommentar the new kommentar
     */
    public void setKommentar(String kommentar) {
        this.kommentar = kommentar;
    }

    /**
     * Gets the sk ist string.
     *
     * @return the sk ist string
     */
    public String getSkIstString() {
        return skIstString;
    }

    /**
     * Sets the sk ist string.
     *
     * @param skIstString the new sk ist string
     */
    public void setSkIstString(String skIstString) {
        this.skIstString = skIstString;
    }

    /**
     * Gets the sk offenlegung string.
     *
     * @return the sk offenlegung string
     */
    public String getSkOffenlegungString() {
        return skOffenlegungString;
    }

    /**
     * Sets the sk offenlegung string.
     *
     * @param skOffenlegungString the new sk offenlegung string
     */
    public void setSkOffenlegungString(String skOffenlegungString) {
        this.skOffenlegungString = skOffenlegungString;
    }

    /**
     * Gets the verdeckt string.
     *
     * @return the verdeckt string
     */
    public String getVerdecktString() {
        return verdecktString;
    }

    /**
     * Sets the verdeckt string.
     *
     * @param verdecktString the new verdeckt string
     */
    public void setVerdecktString(String verdecktString) {
        this.verdecktString = verdecktString;
    }

    /**
     * Gets the sk weisungsart zulaessig string.
     *
     * @return the sk weisungsart zulaessig string
     */
    public String getSkWeisungsartZulaessigString() {
        return skWeisungsartZulaessigString;
    }

    /**
     * Sets the sk weisungsart zulaessig string.
     *
     * @param skWeisungsartZulaessigString the new sk weisungsart zulaessig string
     */
    public void setSkWeisungsartZulaessigString(String skWeisungsartZulaessigString) {
        this.skWeisungsartZulaessigString = skWeisungsartZulaessigString;
    }

    /**
     * Gets the sk buchbar internet string.
     *
     * @return the sk buchbar internet string
     */
    public String getSkBuchbarInternetString() {
        return skBuchbarInternetString;
    }

    /**
     * Sets the sk buchbar internet string.
     *
     * @param skBuchbarInternetString the new sk buchbar internet string
     */
    public void setSkBuchbarInternetString(String skBuchbarInternetString) {
        this.skBuchbarInternetString = skBuchbarInternetString;
    }

    /**
     * Gets the sk buchbar papier string.
     *
     * @return the sk buchbar papier string
     */
    public String getSkBuchbarPapierString() {
        return skBuchbarPapierString;
    }

    /**
     * Sets the sk buchbar papier string.
     *
     * @param skBuchbarPapierString the new sk buchbar papier string
     */
    public void setSkBuchbarPapierString(String skBuchbarPapierString) {
        this.skBuchbarPapierString = skBuchbarPapierString;
    }

    /**
     * Gets the sk buchbar HV string.
     *
     * @return the sk buchbar HV string
     */
    public String getSkBuchbarHVString() {
        return skBuchbarHVString;
    }

    /**
     * Sets the sk buchbar HV string.
     *
     * @param skBuchbarHVString the new sk buchbar HV string
     */
    public void setSkBuchbarHVString(String skBuchbarHVString) {
        this.skBuchbarHVString = skBuchbarHVString;
    }

    /**
     * Gets the sk buchbar vollmacht dritte string.
     *
     * @return the sk buchbar vollmacht dritte string
     */
    public String getSkBuchbarVollmachtDritteString() {
        return skBuchbarVollmachtDritteString;
    }

    /**
     * Sets the sk buchbar vollmacht dritte string.
     *
     * @param skBuchbarVollmachtDritteString the new sk buchbar vollmacht dritte
     *                                       string
     */
    public void setSkBuchbarVollmachtDritteString(String skBuchbarVollmachtDritteString) {
        this.skBuchbarVollmachtDritteString = skBuchbarVollmachtDritteString;
    }

    /**
     * Gets the sk buchbar vollmacht dritte HV string.
     *
     * @return the sk buchbar vollmacht dritte HV string
     */
    public String getSkBuchbarVollmachtDritteHVString() {
        return skBuchbarVollmachtDritteHVString;
    }

    /**
     * Sets the sk buchbar vollmacht dritte HV string.
     *
     * @param skBuchbarVollmachtDritteHVString the new sk buchbar vollmacht dritte
     *                                         HV string
     */
    public void setSkBuchbarVollmachtDritteHVString(String skBuchbarVollmachtDritteHVString) {
        this.skBuchbarVollmachtDritteHVString = skBuchbarVollmachtDritteHVString;
    }

    /**
     * Gets the vollmacht dritte.
     *
     * @return the vollmacht dritte
     */
    public String getVollmachtDritte() {
        return vollmachtDritte;
    }

    /**
     * Sets the vollmacht dritte.
     *
     * @param vollmachtDritte the new vollmacht dritte
     */
    public void setVollmachtDritte(String vollmachtDritte) {
        this.vollmachtDritte = vollmachtDritte;
    }

    /**
     * Gets the meldung aktiv string.
     *
     * @return the meldung aktiv string
     */
    public String getMeldungAktivString() {
        return meldungAktivString;
    }

    /**
     * Sets the meldung aktiv string.
     *
     * @param meldungAktivString the new meldung aktiv string
     */
    public void setMeldungAktivString(String meldungAktivString) {
        this.meldungAktivString = meldungAktivString;
    }

    /**
     * Gets the gruppe.
     *
     * @return the gruppe
     */
    public int getGruppe() {
        return gruppe;
    }

    /**
     * Sets the gruppe.
     *
     * @param gruppe the new gruppe
     */
    public void setGruppe(int gruppe) {
        this.gruppe = gruppe;
    }

    /**
     * Gets the aktiv.
     *
     * @return the aktiv
     */
    public Boolean getAktiv() {
        return meldungAktiv == 1 ? true : false;
    }

}
