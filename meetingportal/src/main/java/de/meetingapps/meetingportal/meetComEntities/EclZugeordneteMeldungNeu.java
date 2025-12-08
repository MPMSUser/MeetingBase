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
import java.util.LinkedList;
import java.util.List;

import de.meetingapps.meetingportal.meetComKonst.KonstPortalTexte;

/**Für Array: Meldung, die zugeordnet wurde. Konkrete:
 * > Meldung, die zu einer AktienregisterIdent generiert wurde
 * > Meldung, die zu einem Bevollmächtigten oder einer Kennung oder ... gehört
 * */

public class EclZugeordneteMeldungNeu implements Serializable {
    private static final long serialVersionUID = 5817041675282093399L;

    /**Gibt an, in welcher "Beziehung" diese Meldung zu dem "aufrufenden" Teilnehmer ist (also sprich:
     * in welchem der 3 Array diese Meldung von BlWillenserklaerungStatus abgelegt wurde). Wird benötigt,
     * da beim Aufruf JSF einer Detailsicher nicht mehr ersichtlich ist, in welchem Array die Meldung drin
     * ist.
     * =1 => eigene Aktien aus Aktienregister (eigenerAREintragListe)
     * =2 => eigene Gastkarten (zugeordneteMeldungenEigeneGastkartenListe)
     * =3 => als Bevollmächtigter erhaltene Meldungen (zugeordneteMeldungenBevollmaechtigtListe)
     * =4 => Insti-Aktienregister (instiAREintraegeListe)
     * =5 => Insti-Meldungen (zugeordneteMeldungenInstiListe)
     * =6 => mit gesetzlicher Vollmacht geerbte Vollmacht
     * 
     * Siehe KonstZugeordneteMeldungArtBeziehung
     */
    public int artBeziehung = 0;

    /**Wird für den ausgewählten "Sammel-Ausführungs-Button" berücksichtigt*/
    public boolean ausgewaehlt = true;

    /*+++++++++++++++++++++++++++++Meldung+++++++++++++++++++++++++++++*/
    public EclMeldung eclMeldung = null;

    /**Art der Vollmacht - nur bei Spezial-Ablauf (ku178)
     * 0=normale Vollmacht
     * 1=gesetzliche Vollmacht
     * 2=mit gesetzlicher Vollmacht "mitgeerbte" Vollmacht
     */
    public int vollmachtsart = 0;

    /**Wird nur bei Spezial-Ablauf ku178, und nur bei erhaltenen Vollmachten, gefüllt*/
    public EclVorlaeufigeVollmacht eclVorlaeufigeVollmacht = null;

    /*+++Redundante Detailfelder zu eclMeldung+++++*/
    /**aktienregisterIdent, zu der diese Meldung zugeordnet wurde (von der diese erzeugt wurde)*/
    public int aktienregisterIdent = 0;

    public String aktionaersnummerFuerAnzeige = "";

    /**Meldung, die zugeordnet wurde*/
    public int meldungsIdent = 0;

    /** 0 Gast, 1 = aktienrechtliche Anmeldung*/
    public int klasse = 0;

    public boolean fixAnmeldung = false;

    
    /**Nur fürs Portal: Die Meldung ist ausgeblendet, weil die eingeloggte Person sie nicht vertritt*/ 
    public boolean ausgeblendet=false;
    

    /**
     * personNatJurIdent, die aktuell (d.h. im derzeitigen Login) Besitzer dieser Meldung ist. Wird verwendet als
     * agierende Person, wenn von dieser Meldung ausgehend von einem Bevollmächtigten weitere Aktionen durchgeführt werden.
     * Wird aktuell nur in der Funktion 
     * 		leseMeldungenBevollmaechtigtZuPersonNatJur und leseMeldungenEigeneGastkartenZuPersonNatJur 
     * gefüllt.
     * 
     * Alter Kommentar, vollkommen unklar:
     * personNatJurIdent, die dieser Meldung direkt zugeordnet ist (d.h. gehört). Wird aktuell nur in der Funktion
     * leseMeldungenBevollmaechtigtZuPersonNatJur und leseMeldungenEigeneGastkartenZuPersonNatJur gefüllt.
     * 
     * Für gesetzliche Vollmacht und mitgeerbte: (Spezialablauf ku178): hier ist es das Mitglied, von dem die gesetzliche Vertretung ausgeht 
     */
    public int personNatJurIdent = 0;

    /**Dr. Hans Müller**/
    public String aktionaerTitelVornameName = "";
    public String aktionaerOrt = "";
    /**String formatiert im Deutschen Format*/
    public String aktionaerStimmenDE = "";
    /**String formatiert im Englischen Format*/
    public String aktionaerStimmenEN = "";
    /**String formatiert im Deutschen Format*/
    public String aktionaerAktienDE = "";
    /**String formatiert im Englischen Format*/
    public String aktionaerAktienEN = "";
    
    public int gattung = 0;

    /**Siehe KonstPortalTexte.IAUSWAHL_GATTUNG_ZUGEORDNETEMELDUNG_* */
    public int textNrVorAktien=1444;

    public int liefereTextNrGattung() {
        int lGattung=gattung;
        switch (lGattung) {
        case 0:
        case 1:return KonstPortalTexte.ALLGEMEIN_GATTUNG1_BESITZ_VOR_AKTIEN;
        case 2:return KonstPortalTexte.ALLGEMEIN_GATTUNG2_BESITZ_VOR_AKTIEN;
        case 3:return KonstPortalTexte.ALLGEMEIN_GATTUNG3_BESITZ_VOR_AKTIEN;
        case 4:return KonstPortalTexte.ALLGEMEIN_GATTUNG4_BESITZ_VOR_AKTIEN;
        case 5:return KonstPortalTexte.ALLGEMEIN_GATTUNG5_BESITZ_VOR_AKTIEN;
        }
        return KonstPortalTexte.ALLGEMEIN_GATTUNG1_KENNUNG_VOR_AKTIEN;
    }

    
    /*++++++++++++++++Präsenz-Felder - teilweise redundant zu EclMeldung, teilweise ergänzt+++++++++++++*/
    /**wird aus dem Meldesatz gefüllt. 
     * 
     * Achtung, bei nurRawLiveAbstimmung wird das Präsenzkennzeichen
     * in der Datenbank nie gesetzt - sondern immer nur im Speicher gehalten. D.h. nach dem
     * Einlesen des Status ist istPraesent immer 0!
     * 
     * =1 wenn aktuell präsent
     */
    public boolean bereitsPraesent = false;
    public boolean bereitsPraesentDurchKennung = false;
    public boolean bereitsPraesentDurchAndere = false;

    /**Redundant zu eclMeldung.statusPraesenz*/
    public int statusPraesenz=0;
    
    /*+++++++++++++Abstimmungsfelder - werden während der Abstimmung im Speicher gefüllt - bei Refresh wieder zurückgesetzt!++++++++++*/
    /**Für diese Meldung wurde im aktuellen Stimmabgabe-Ablauf bereits eine Stimmabgabe durchgeführt.
     * Wird wieder zurückgesetzt, sobal der Aktionär den Stimmabgabe-Ablauf beendet (und ggf. neu startet)
     */
    public boolean bereitsAbgestimmt = false;

    /*++++++++++++++++++++++++++Willenserklärungen+++++++++++++++++++++++*/
    public List<EclWillenserklaerungStatusNeu> zugeordneteWillenserklaerungenList = new LinkedList<EclWillenserklaerungStatusNeu>();

    /**TFunktionen: Button "Weitere Willenserklärungen" wird angezeigt*/
    public boolean weitereWillenserklaerungMoeglich = false; 

    /**TFunktionen: Wird verwendet, um in iNeueWillenserklaerung die Möglichkeit "EK" anzuzeigen (true)*/
    public boolean weitereEKMoeglich = false; 
    
    /**TFunktionen: Wird verwendet, um in iNeueWillenserklaerung die Möglichkeit "SRV" anzuzeigen (true)*/
    public boolean weitereSRVMoeglich = false; 
    /**TFunktionen: Wird verwendet, um in iNeueWillenserklaerung die Möglichkeit "KIAV" anzuzeigen (true)*/
    public boolean weitereKIAVMoeglich = false; 
    /**TFunktionen: Wird verwendet, um in iNeueWillenserklaerung die Möglichkeit "Briefwahl" anzuzeigen (true)*/
    public boolean weitereBriefwahlMoeglich = false;
    /**TFunktionen: Wird verwendet, um in iNeueWillenserklaerung die Möglichkeit "Vollmacht An Dritte" anzuzeigen (true)*/
    public boolean weitereVollmachtDritteMoeglich = false;

    /**BlWillenserklaerungStatus: Die folgenden Werte werden in den obigen "Moeglich"-Werten bereits mit berücksichtigt. Sie dienen nur dazu,
     * um ggf. weitere Infos anzuzeigen warum die Abgabe nicht möglich ist, wenn dies deswegen nicht möglich ist weil bereits
     * durch jemand anders erteilt wurde (was ja in der Willenserklärungsliste des eingeloggen Users nicht sichtbar ist)*/
    public boolean bereitsErteiltWeisungBriefwahlAllgemeinDurchAndere = false;
    public boolean bereitsErteiltSRVDurchAktionär = false;
    public boolean bereitsErteiltSRVDurchVertreter = false;
    public boolean bereitsErteiltKIAVDurchAktionär = false;
    public boolean bereitsErteiltKIAVDurchVertreter = false;
    public boolean bereitsErteiltBriefwahlDurchAktionär = false;
    public boolean bereitsErteiltBriefwahlDurchVertreter = false;

    /**SRV / Briefwahl / KIAV nicht möglich, weil Vollmacht dritte erteilt.
     * Achtung, berücksichtig nicht ob VollmachtAnDritte überhaupt möglich - d.h. nur in "Sub-Abfrage" zu verwenden*/
    public boolean weisungNichtMoeglichWeilVollmachtDritte=false;
    
    /**Vollmacht dritte nicht möglich, weil SRV/Briefwahl/KIAV erteilt
     * Achtung, berücksichtig nicht ob VollmachtAnDritte überhaupt möglich - d.h. nur in "Sub-Abfrage" zu verwenden*/
    public boolean vollmachtDritteNichtMoeglichWeilWeisungErteilt=false;
    
    public boolean vollmachtDritteNichtMoeglichWeilBereitsVollmacht=false;
    
    /**TFunktionen: Wird verwendet, um in iNeueWillenserklaerung alle EK-Möglichkeiten anzuzeigen (außer für Vollmacht an Dritte - EK!)*/
    public boolean weitereEKSelbstMoeglich = false;
    /**TFunktionen: Wird verwendet, um in iNeueWillenserklaerung alle EK-Vollmacht-An-Dritte-Möglichkeit anzuzeigen*/
    public boolean weitereEKDritteMoeglich = false;

    /**TFunktionen: Wird verwendet, um den Butten "Weitere Willenserklärung" in aStatus nicht anzuzeigen (true) oder nicht (false), 
     * wenn nur "Vollmacht an Dritte" als weitere Willenserklärung möglich ist*/
    public boolean weitereWillenserklaerungMoeglichNurVollmachtDritte = false;

    /**Nr der höchsten vergebenen Willenserklärung für diese Meldung. Dient dazu, um festzustellen, ob seit Einlesen
     * der Willenserklärungen (in einem früheren  Transaktionsschritt) neue Willenserklärungen dazugekommen sind
     */
    public int identHoechsteWillenserklaerung = 0;

    /**+BlWillenserklaerungStatus: ++Summierung aus Willenserklärungen - alle+++++*/
    public int anzAlleZutrittsIdentSelbst = 0;
    public int anzAlleZutrittsIdentVollmacht = 0;
    public int anzAlleVollmachtenDritte = 0;

    /**BlWillenserklaerungStatus: Summe aus KIAV, SRV, Briefwahl*/
    public int anzAlleKIAVSRV = 0;

    public int anzAlleSRV = 0;
    public int anzAlleKIAV = 0;
    public int anzAlleBriefwahl = 0;

    /**+++BlWillenserklaerungStatus: Summierung aus Willenserklärungen - nur die Person die im Portal eingeloggt ist+++++*/
    public int anzPersZutrittsIdentSelbst = 0;
    public int anzPersZutrittsIdentVollmacht = 0;
    public int anzPersVollmachtenDritte = 0;

    /**Summe aus KIAV, SRV, Briefwahl*/
    public int anzPersKIAVSRV = 0;

    public int anzPersSRV = 0;
    public int anzPersKIAV = 0;
    public int anzPersBriefwahl = 0;

    /*+++++++++++++++++++++++Ab hier alt+++++++++++++++++++++++++++++++++++++*/

    /**Präsenzkennzeichen*/
    //	public int kartenart=0;
    //	public int kartennr=0;
    public int identPersonNatJur = 0;

    @Deprecated
    public int istPraesentNeu = 0; // istPraesent wg. Kompatibilität zur App. Neu geht bereits auf neue Mechanismen
    /*TODO $App Das ganze funktioniert nur, wenn nciht zwischendurch ein anderer Teilnehmer mit dieser Meldung präsent war
     *  vermutlich muß hier eine Liste der ZutrittsIdents zu dieser Meldung mit der jeweiligen zugeordneten Person rein
     *  
     *  Wird bei nurRawLiveAbstimmung==1 nicht gefüllt!
     *  */
    public String praesenteZutrittsIdent = "";
    public String praesenteZutrittsIdentNeben = "";

    /**Angemeldeter Aktionär*/

    public long aktionaerStimmen = 0;
    public String aktionaerAnredeDE = "";
    /**Herr, Frau etc. gemäß Schlüssel*/
    public String aktionaerAnredeEN = "";
    public String aktionaerTitel = "";
    public String aktionaerName = "";
    public String aktionaerVorname = "";
    public String aktionaerPlz = "";
    public String aktionaerLandeskuerzel = "";
    /**DE*/
    public String aktionaerLand = "";
    /**Deutschland*/
    public String aktionaerStrasse = "";
    public String aktionaerBriefanredeDE = "";
    /**wie aus anreden-Datei*/
    public String aktionaerBriefanredeEN = "";
    /**wie aus Anreden Datei*/
    public String aktionaerNameVornameTitel = "";
    /**Müller, Hans Dr.*/
    public String aktionaerKompletteAnredeDE = "";
    /**"Sehr geehrter Herr Dr. Müller" oder bei juristische Person "Sehr geehrte Damen und Herren"*/
    public String aktionaerKompletteAnredeEN = "";
    /**"Sehr geehrter Herr Dr. Müller" oder bei juristische Person "Sehr geehrte Damen und Herren"*/
    public String aktionaerBesitzArtKuerzel = "";
    public String aktionaerBesitzArt = "";
    public String aktionaerBesitzArtEN = "";

    public long stueckAktien = 0;

    public boolean zweiEKMoeglich = false;

    public boolean willenserklaerungenVorhanden = false;

    /**
     * Für Portal (speziell ku178):
     * Div. Infos für Anmeldung.
     * Pos 1 = Anmelden (1), Abmelden (2), sonst undefiniert
     * 
     * Für Verein: Bonuskartennummer
     * 
     *Length=40*/
    public String zusatzfeld3 = "";

    public int getArtBeziehung() {
        return artBeziehung;
    }

    public void setArtBeziehung(int artBeziehung) {
        this.artBeziehung = artBeziehung;
    }

    public EclMeldung getEclMeldung() {
        return eclMeldung;
    }

    public void setEclMeldung(EclMeldung eclMeldung) {
        this.eclMeldung = eclMeldung;
    }

    public int getAktienregisterIdent() {
        return aktienregisterIdent;
    }

    public void setAktienregisterIdent(int aktienregisterIdent) {
        this.aktienregisterIdent = aktienregisterIdent;
    }

    public int getMeldungsIdent() {
        return meldungsIdent;
    }

    public void setMeldungsIdent(int meldungsIdent) {
        this.meldungsIdent = meldungsIdent;
    }

    public int getKlasse() {
        return klasse;
    }

    public void setKlasse(int klasse) {
        this.klasse = klasse;
    }

    public boolean isFixAnmeldung() {
        return fixAnmeldung;
    }

    public void setFixAnmeldung(boolean fixAnmeldung) {
        this.fixAnmeldung = fixAnmeldung;
    }

    public int getPersonNatJurIdent() {
        return personNatJurIdent;
    }

    public void setPersonNatJurIdent(int personNatJurIdent) {
        this.personNatJurIdent = personNatJurIdent;
    }

    public String getAktionaerTitelVornameName() {
        return aktionaerTitelVornameName;
    }

    public void setAktionaerTitelVornameName(String aktionaerTitelVornameName) {
        this.aktionaerTitelVornameName = aktionaerTitelVornameName;
    }

    public String getAktionaerOrt() {
        return aktionaerOrt;
    }

    public void setAktionaerOrt(String aktionaerOrt) {
        this.aktionaerOrt = aktionaerOrt;
    }

    public String getAktionaerStimmenDE() {
        return aktionaerStimmenDE;
    }

    public void setAktionaerStimmenDE(String aktionaerStimmenDE) {
        this.aktionaerStimmenDE = aktionaerStimmenDE;
    }

    public String getAktionaerStimmenEN() {
        return aktionaerStimmenEN;
    }

    public void setAktionaerStimmenEN(String aktionaerStimmenEN) {
        this.aktionaerStimmenEN = aktionaerStimmenEN;
    }

    public List<EclWillenserklaerungStatusNeu> getZugeordneteWillenserklaerungenList() {
        return zugeordneteWillenserklaerungenList;
    }

    public void setZugeordneteWillenserklaerungenList(
            List<EclWillenserklaerungStatusNeu> zugeordneteWillenserklaerungenList) {
        this.zugeordneteWillenserklaerungenList = zugeordneteWillenserklaerungenList;
    }

    public int getIdentPersonNatJur() {
        return identPersonNatJur;
    }

    public void setIdentPersonNatJur(int identPersonNatJur) {
        this.identPersonNatJur = identPersonNatJur;
    }

    @Deprecated
    public int getIstPraesentNeu() {
        return istPraesentNeu;
    }

    @Deprecated
    public void setIstPraesentNeu(int istPraesentNeu) {
        this.istPraesentNeu = istPraesentNeu;
    }

    public String getPraesenteZutrittsIdent() {
        return praesenteZutrittsIdent;
    }

    public void setPraesenteZutrittsIdent(String praesenteZutrittsIdent) {
        this.praesenteZutrittsIdent = praesenteZutrittsIdent;
    }

    public String getPraesenteZutrittsIdentNeben() {
        return praesenteZutrittsIdentNeben;
    }

    public void setPraesenteZutrittsIdentNeben(String praesenteZutrittsIdentNeben) {
        this.praesenteZutrittsIdentNeben = praesenteZutrittsIdentNeben;
    }

    public long getAktionaerStimmen() {
        return aktionaerStimmen;
    }

    public void setAktionaerStimmen(long aktionaerStimmen) {
        this.aktionaerStimmen = aktionaerStimmen;
    }

    public String getAktionaerAnredeDE() {
        return aktionaerAnredeDE;
    }

    public void setAktionaerAnredeDE(String aktionaerAnredeDE) {
        this.aktionaerAnredeDE = aktionaerAnredeDE;
    }

    public String getAktionaerAnredeEN() {
        return aktionaerAnredeEN;
    }

    public void setAktionaerAnredeEN(String aktionaerAnredeEN) {
        this.aktionaerAnredeEN = aktionaerAnredeEN;
    }

    public String getAktionaerTitel() {
        return aktionaerTitel;
    }

    public void setAktionaerTitel(String aktionaerTitel) {
        this.aktionaerTitel = aktionaerTitel;
    }

    public String getAktionaerName() {
        return aktionaerName;
    }

    public void setAktionaerName(String aktionaerName) {
        this.aktionaerName = aktionaerName;
    }

    public String getAktionaerVorname() {
        return aktionaerVorname;
    }

    public void setAktionaerVorname(String aktionaerVorname) {
        this.aktionaerVorname = aktionaerVorname;
    }

    public String getAktionaerPlz() {
        return aktionaerPlz;
    }

    public void setAktionaerPlz(String aktionaerPlz) {
        this.aktionaerPlz = aktionaerPlz;
    }

    public String getAktionaerLandeskuerzel() {
        return aktionaerLandeskuerzel;
    }

    public void setAktionaerLandeskuerzel(String aktionaerLandeskuerzel) {
        this.aktionaerLandeskuerzel = aktionaerLandeskuerzel;
    }

    public String getAktionaerLand() {
        return aktionaerLand;
    }

    public void setAktionaerLand(String aktionaerLand) {
        this.aktionaerLand = aktionaerLand;
    }

    public String getAktionaerStrasse() {
        return aktionaerStrasse;
    }

    public void setAktionaerStrasse(String aktionaerStrasse) {
        this.aktionaerStrasse = aktionaerStrasse;
    }

    public String getAktionaerBriefanredeDE() {
        return aktionaerBriefanredeDE;
    }

    public void setAktionaerBriefanredeDE(String aktionaerBriefanredeDE) {
        this.aktionaerBriefanredeDE = aktionaerBriefanredeDE;
    }

    public String getAktionaerBriefanredeEN() {
        return aktionaerBriefanredeEN;
    }

    public void setAktionaerBriefanredeEN(String aktionaerBriefanredeEN) {
        this.aktionaerBriefanredeEN = aktionaerBriefanredeEN;
    }

    public String getAktionaerNameVornameTitel() {
        return aktionaerNameVornameTitel;
    }

    public void setAktionaerNameVornameTitel(String aktionaerNameVornameTitel) {
        this.aktionaerNameVornameTitel = aktionaerNameVornameTitel;
    }

    public String getAktionaerKompletteAnredeDE() {
        return aktionaerKompletteAnredeDE;
    }

    public void setAktionaerKompletteAnredeDE(String aktionaerKompletteAnredeDE) {
        this.aktionaerKompletteAnredeDE = aktionaerKompletteAnredeDE;
    }

    public String getAktionaerKompletteAnredeEN() {
        return aktionaerKompletteAnredeEN;
    }

    public void setAktionaerKompletteAnredeEN(String aktionaerKompletteAnredeEN) {
        this.aktionaerKompletteAnredeEN = aktionaerKompletteAnredeEN;
    }

    public String getAktionaerBesitzArtKuerzel() {
        return aktionaerBesitzArtKuerzel;
    }

    public void setAktionaerBesitzArtKuerzel(String aktionaerBesitzArtKuerzel) {
        this.aktionaerBesitzArtKuerzel = aktionaerBesitzArtKuerzel;
    }

    public String getAktionaerBesitzArt() {
        return aktionaerBesitzArt;
    }

    public void setAktionaerBesitzArt(String aktionaerBesitzArt) {
        this.aktionaerBesitzArt = aktionaerBesitzArt;
    }

    public String getAktionaerBesitzArtEN() {
        return aktionaerBesitzArtEN;
    }

    public void setAktionaerBesitzArtEN(String aktionaerBesitzArtEN) {
        this.aktionaerBesitzArtEN = aktionaerBesitzArtEN;
    }

    public int getGattung() {
        return gattung;
    }

    public void setGattung(int gattung) {
        this.gattung = gattung;
    }

    public long getStueckAktien() {
        return stueckAktien;
    }

    public void setStueckAktien(long stueckAktien) {
        this.stueckAktien = stueckAktien;
    }

    public int getAnzAlleZutrittsIdentSelbst() {
        return anzAlleZutrittsIdentSelbst;
    }

    public void setAnzAlleZutrittsIdentSelbst(int anzAlleZutrittsIdentSelbst) {
        this.anzAlleZutrittsIdentSelbst = anzAlleZutrittsIdentSelbst;
    }

    public int getAnzAlleZutrittsIdentVollmacht() {
        return anzAlleZutrittsIdentVollmacht;
    }

    public void setAnzAlleZutrittsIdentVollmacht(int anzAlleZutrittsIdentVollmacht) {
        this.anzAlleZutrittsIdentVollmacht = anzAlleZutrittsIdentVollmacht;
    }

    public int getAnzAlleVollmachtenDritte() {
        return anzAlleVollmachtenDritte;
    }

    public void setAnzAlleVollmachtenDritte(int anzAlleVollmachtenDritte) {
        this.anzAlleVollmachtenDritte = anzAlleVollmachtenDritte;
    }

    public int getAnzAlleKIAVSRV() {
        return anzAlleKIAVSRV;
    }

    public void setAnzAlleKIAVSRV(int anzAlleKIAVSRV) {
        this.anzAlleKIAVSRV = anzAlleKIAVSRV;
    }

    public int getAnzAlleSRV() {
        return anzAlleSRV;
    }

    public void setAnzAlleSRV(int anzAlleSRV) {
        this.anzAlleSRV = anzAlleSRV;
    }

    public int getAnzAlleKIAV() {
        return anzAlleKIAV;
    }

    public void setAnzAlleKIAV(int anzAlleKIAV) {
        this.anzAlleKIAV = anzAlleKIAV;
    }

    public int getAnzAlleBriefwahl() {
        return anzAlleBriefwahl;
    }

    public void setAnzAlleBriefwahl(int anzAlleBriefwahl) {
        this.anzAlleBriefwahl = anzAlleBriefwahl;
    }

    public int getAnzPersZutrittsIdentSelbst() {
        return anzPersZutrittsIdentSelbst;
    }

    public void setAnzPersZutrittsIdentSelbst(int anzPersZutrittsIdentSelbst) {
        this.anzPersZutrittsIdentSelbst = anzPersZutrittsIdentSelbst;
    }

    public int getAnzPersZutrittsIdentVollmacht() {
        return anzPersZutrittsIdentVollmacht;
    }

    public void setAnzPersZutrittsIdentVollmacht(int anzPersZutrittsIdentVollmacht) {
        this.anzPersZutrittsIdentVollmacht = anzPersZutrittsIdentVollmacht;
    }

    public int getAnzPersVollmachtenDritte() {
        return anzPersVollmachtenDritte;
    }

    public void setAnzPersVollmachtenDritte(int anzPersVollmachtenDritte) {
        this.anzPersVollmachtenDritte = anzPersVollmachtenDritte;
    }

    public int getAnzPersKIAVSRV() {
        return anzPersKIAVSRV;
    }

    public void setAnzPersKIAVSRV(int anzPersKIAVSRV) {
        this.anzPersKIAVSRV = anzPersKIAVSRV;
    }

    public int getAnzPersSRV() {
        return anzPersSRV;
    }

    public void setAnzPersSRV(int anzPersSRV) {
        this.anzPersSRV = anzPersSRV;
    }

    public int getAnzPersKIAV() {
        return anzPersKIAV;
    }

    public void setAnzPersKIAV(int anzPersKIAV) {
        this.anzPersKIAV = anzPersKIAV;
    }

    public int getAnzPersBriefwahl() {
        return anzPersBriefwahl;
    }

    public void setAnzPersBriefwahl(int anzPersBriefwahl) {
        this.anzPersBriefwahl = anzPersBriefwahl;
    }

    public boolean isZweiEKMoeglich() {
        return zweiEKMoeglich;
    }

    public void setZweiEKMoeglich(boolean zweiEKMoeglich) {
        this.zweiEKMoeglich = zweiEKMoeglich;
    }

    public boolean isWillenserklaerungenVorhanden() {
        return willenserklaerungenVorhanden;
    }

    public void setWillenserklaerungenVorhanden(boolean willenserklaerungenVorhanden) {
        this.willenserklaerungenVorhanden = willenserklaerungenVorhanden;
    }

    public int getIdentHoechsteWillenserklaerung() {
        return identHoechsteWillenserklaerung;
    }

    public void setIdentHoechsteWillenserklaerung(int identHoechsteWillenserklaerung) {
        this.identHoechsteWillenserklaerung = identHoechsteWillenserklaerung;
    }

    public String getZusatzfeld3() {
        return zusatzfeld3;
    }

    public void setZusatzfeld3(String zusatzfeld3) {
        this.zusatzfeld3 = zusatzfeld3;
    }

    public boolean isWeitereWillenserklaerungMoeglich() {
        return weitereWillenserklaerungMoeglich;
    }

    public void setWeitereWillenserklaerungMoeglich(boolean weitereWillenserklaerungMoeglich) {
        this.weitereWillenserklaerungMoeglich = weitereWillenserklaerungMoeglich;
    }

    public boolean isWeitereEKMoeglich() {
        return weitereEKMoeglich;
    }

    public void setWeitereEKMoeglich(boolean weitereEKMoeglich) {
        this.weitereEKMoeglich = weitereEKMoeglich;
    }

    public boolean isWeitereSRVMoeglich() {
        return weitereSRVMoeglich;
    }

    public void setWeitereSRVMoeglich(boolean weitereSRVMoeglich) {
        this.weitereSRVMoeglich = weitereSRVMoeglich;
    }

    public boolean isWeitereKIAVMoeglich() {
        return weitereKIAVMoeglich;
    }

    public void setWeitereKIAVMoeglich(boolean weitereKIAVMoeglich) {
        this.weitereKIAVMoeglich = weitereKIAVMoeglich;
    }

    public boolean isWeitereBriefwahlMoeglich() {
        return weitereBriefwahlMoeglich;
    }

    public void setWeitereBriefwahlMoeglich(boolean weitereBriefwahlMoeglich) {
        this.weitereBriefwahlMoeglich = weitereBriefwahlMoeglich;
    }

    public boolean isWeitereVollmachtDritteMoeglich() {
        return weitereVollmachtDritteMoeglich;
    }

    public void setWeitereVollmachtDritteMoeglich(boolean weitereVollmachtDritteMoeglich) {
        this.weitereVollmachtDritteMoeglich = weitereVollmachtDritteMoeglich;
    }

    public String getAktionaersnummerFuerAnzeige() {
        return aktionaersnummerFuerAnzeige;
    }

    public void setAktionaersnummerFuerAnzeige(String aktionaersnummerFuerAnzeige) {
        this.aktionaersnummerFuerAnzeige = aktionaersnummerFuerAnzeige;
    }

    public boolean isWeitereEKSelbstMoeglich() {
        return weitereEKSelbstMoeglich;
    }

    public void setWeitereEKSelbstMoeglich(boolean weitereEKSelbstMoeglich) {
        this.weitereEKSelbstMoeglich = weitereEKSelbstMoeglich;
    }

    public boolean isWeitereEKDritteMoeglich() {
        return weitereEKDritteMoeglich;
    }

    public void setWeitereEKDritteMoeglich(boolean weitereEKDritteMoeglich) {
        this.weitereEKDritteMoeglich = weitereEKDritteMoeglich;
    }

    public boolean isWeitereWillenserklaerungMoeglichNurVollmachtDritte() {
        return weitereWillenserklaerungMoeglichNurVollmachtDritte;
    }

    public void setWeitereWillenserklaerungMoeglichNurVollmachtDritte(
            boolean weitereWillenserklaerungMoeglichNurVollmachtDritte) {
        this.weitereWillenserklaerungMoeglichNurVollmachtDritte = weitereWillenserklaerungMoeglichNurVollmachtDritte;
    }

    public int getVollmachtsart() {
        return vollmachtsart;
    }

    public void setVollmachtsart(int vollmachtsart) {
        this.vollmachtsart = vollmachtsart;
    }

    public EclVorlaeufigeVollmacht getEclVorlaeufigeVollmacht() {
        return eclVorlaeufigeVollmacht;
    }

    public void setEclVorlaeufigeVollmacht(EclVorlaeufigeVollmacht eclVorlaeufigeVollmacht) {
        this.eclVorlaeufigeVollmacht = eclVorlaeufigeVollmacht;
    }

    public boolean isBereitsErteiltWeisungBriefwahlAllgemeinDurchAndere() {
        return bereitsErteiltWeisungBriefwahlAllgemeinDurchAndere;
    }

    public void setBereitsErteiltWeisungBriefwahlAllgemeinDurchAndere(
            boolean bereitsErteiltWeisungBriefwahlAllgemeinDurchAndere) {
        this.bereitsErteiltWeisungBriefwahlAllgemeinDurchAndere = bereitsErteiltWeisungBriefwahlAllgemeinDurchAndere;
    }

    public boolean isBereitsErteiltSRVDurchAktionär() {
        return bereitsErteiltSRVDurchAktionär;
    }

    public void setBereitsErteiltSRVDurchAktionär(boolean bereitsErteiltSRVDurchAktionär) {
        this.bereitsErteiltSRVDurchAktionär = bereitsErteiltSRVDurchAktionär;
    }

    public boolean isBereitsErteiltSRVDurchVertreter() {
        return bereitsErteiltSRVDurchVertreter;
    }

    public void setBereitsErteiltSRVDurchVertreter(boolean bereitsErteiltSRVDurchVertreter) {
        this.bereitsErteiltSRVDurchVertreter = bereitsErteiltSRVDurchVertreter;
    }

    public boolean isBereitsErteiltKIAVDurchAktionär() {
        return bereitsErteiltKIAVDurchAktionär;
    }

    public void setBereitsErteiltKIAVDurchAktionär(boolean bereitsErteiltKIAVDurchAktionär) {
        this.bereitsErteiltKIAVDurchAktionär = bereitsErteiltKIAVDurchAktionär;
    }

    public boolean isBereitsErteiltKIAVDurchVertreter() {
        return bereitsErteiltKIAVDurchVertreter;
    }

    public void setBereitsErteiltKIAVDurchVertreter(boolean bereitsErteiltKIAVDurchVertreter) {
        this.bereitsErteiltKIAVDurchVertreter = bereitsErteiltKIAVDurchVertreter;
    }

    public boolean isBereitsErteiltBriefwahlDurchAktionär() {
        return bereitsErteiltBriefwahlDurchAktionär;
    }

    public void setBereitsErteiltBriefwahlDurchAktionär(boolean bereitsErteiltBriefwahlDurchAktionär) {
        this.bereitsErteiltBriefwahlDurchAktionär = bereitsErteiltBriefwahlDurchAktionär;
    }

    public boolean isBereitsErteiltBriefwahlDurchVertreter() {
        return bereitsErteiltBriefwahlDurchVertreter;
    }

    public void setBereitsErteiltBriefwahlDurchVertreter(boolean bereitsErteiltBriefwahlDurchVertreter) {
        this.bereitsErteiltBriefwahlDurchVertreter = bereitsErteiltBriefwahlDurchVertreter;
    }

    public boolean isAusgewaehlt() {
        return ausgewaehlt;
    }

    public void setAusgewaehlt(boolean ausgewaehlt) {
        this.ausgewaehlt = ausgewaehlt;
    }

    public boolean isBereitsPraesentDurchAndere() {
        return bereitsPraesentDurchAndere;
    }

    public void setBereitsPraesentDurchAndere(boolean bereitsPraesentDurchAndere) {
        this.bereitsPraesentDurchAndere = bereitsPraesentDurchAndere;
    }

    public boolean isBereitsPraesent() {
        return bereitsPraesent;
    }

    public void setBereitsPraesent(boolean bereitsPraesent) {
        this.bereitsPraesent = bereitsPraesent;
    }

    public boolean isBereitsPraesentDurchKennung() {
        return bereitsPraesentDurchKennung;
    }

    public void setBereitsPraesentDurchKennung(boolean bereitsPraesentDurchKennung) {
        this.bereitsPraesentDurchKennung = bereitsPraesentDurchKennung;
    }

    public boolean isBereitsAbgestimmt() {
        return bereitsAbgestimmt;
    }

    public void setBereitsAbgestimmt(boolean bereitsAbgestimmt) {
        this.bereitsAbgestimmt = bereitsAbgestimmt;
    }

    public boolean isWeisungNichtMoeglichWeilVollmachtDritte() {
        return weisungNichtMoeglichWeilVollmachtDritte;
    }

    public void setWeisungNichtMoeglichWeilVollmachtDritte(boolean weisungNichtMoeglichWeilVollmachtDritte) {
        this.weisungNichtMoeglichWeilVollmachtDritte = weisungNichtMoeglichWeilVollmachtDritte;
    }

    public boolean isVollmachtDritteNichtMoeglichWeilWeisungErteilt() {
        return vollmachtDritteNichtMoeglichWeilWeisungErteilt;
    }

    public void setVollmachtDritteNichtMoeglichWeilWeisungErteilt(boolean vollmachtDritteNichtMoeglichWeilWeisungErteilt) {
        this.vollmachtDritteNichtMoeglichWeilWeisungErteilt = vollmachtDritteNichtMoeglichWeilWeisungErteilt;
    }

    public boolean isVollmachtDritteNichtMoeglichWeilBereitsVollmacht() {
        return vollmachtDritteNichtMoeglichWeilBereitsVollmacht;
    }

    public void setVollmachtDritteNichtMoeglichWeilBereitsVollmacht(boolean vollmachtDritteNichtMoeglichWeilBereitsVollmacht) {
        this.vollmachtDritteNichtMoeglichWeilBereitsVollmacht = vollmachtDritteNichtMoeglichWeilBereitsVollmacht;
    }

    public boolean isAusgeblendet() {
        return ausgeblendet;
    }

    public void setAusgeblendet(boolean ausgeblendet) {
        this.ausgeblendet = ausgeblendet;
    }

    public int getTextNrVorAktien() {
        return textNrVorAktien;
    }

    public void setTextNrVorAktien(int textNrVorAktien) {
        this.textNrVorAktien = textNrVorAktien;
    }

    public String getAktionaerAktienDE() {
        return aktionaerAktienDE;
    }

    public void setAktionaerAktienDE(String aktionaerAktienDE) {
        this.aktionaerAktienDE = aktionaerAktienDE;
    }

    public String getAktionaerAktienEN() {
        return aktionaerAktienEN;
    }

    public void setAktionaerAktienEN(String aktionaerAktienEN) {
        this.aktionaerAktienEN = aktionaerAktienEN;
    }


}
