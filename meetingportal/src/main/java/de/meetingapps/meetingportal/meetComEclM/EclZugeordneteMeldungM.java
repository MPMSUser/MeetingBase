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
import java.util.LinkedList;
import java.util.List;

import de.meetingapps.meetingportal.meetComEntities.EclZugeordneteMeldung;
import de.meetingapps.meetingportal.meetComHVParam.HVParam;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

@SessionScoped
@Named
@Deprecated
public class EclZugeordneteMeldungM implements Serializable {
    private static final long serialVersionUID = 1833283209853671648L;

    /*Hinweis: in dieser Bean dürfen keine Injects verwendet werden, da diese Bean auch als "normale Klasse" verwendet wird!*/

    /**Gibt an, in welcher "Beziehung" diese Meldung zu dem "aufrufenden" Teilnehmer ist (also sprich:
     * in welchem der 3 Array diese Meldung von BlWillenserklaerungStatus abgelegt wurde). Wird benötigt,
     * da beim Aufruf JSF einer Detailsicher nicht mehr ersichtlich ist, in welchem Array die Meldung drin
     * ist.
     * =1 => zugeordneteMeldungenEigeneAktienArray
     * =2 => zugeordneteMeldungenEigeneGastkartenArray
     * =3 => zugeordneteMeldungenBevollmaechtigtArray
     */
    private int artBeziehung = 0;

    /**aktienregisterIdent, zu der diese Meldung zugeordnet wurde (von der diese erzeugt wurde)*/
    private int aktienregisterIdent = 0;

    /**personNatJurIdent, die dieser Meldung direkt zugeordnet ist (d.h. gehört). Wird aktuell nur in der Funktion
     * leseMeldungenEigeneAktienZuPersonNatJur gefüllt. Wäre in anderen Funktionen sicher einfach füllbar
     */
    private int personNatJurIdent = 0;

    /**Meldung, die zugeordnet wurde*/
    private int meldungsIdent = 0;

    /** 0 Gast, 1 = aktienrechtliche Anmeldung; Achtung, früher umgekehrt!
     * Zusammen mit zutrittsIdent und mandant UNIQUE
     */
    private int klasse = 0;

    /**+++++Präsenzkennzeichen+++++*/
    //	private int kartenart=0;
    //	private int kartennr=0;
    private int identPersonNatJur = 0;

    /**wird aus dem Meldesatz gefüllt. 
     * 
     * Achtung, bei nurRawLiveAbstimmung wird das Präsenzkennzeichen
     * in der Datenbank nie gesetzt - sondern immer nur im Speicher gehalten. D.h. nach dem
     * Einlesen des Status ist istPraesent immer 0!
     * 
     * =1 wenn aktuell präsent
     */
    private int istPraesent = 0;
    private int istPraesentNeu = 0;
    private String praesenteZutrittsIdent = "";
    private String praesenteZutrittsIdentNeben = "";

    /**Angemeldeter Aktionär*/
    private long aktionaerStimmen = 0;
    private String aktionaerStimmenDE = "";
    /**String formatiert im Deutschen Format*/
    private String aktionaerStimmenEN = "";
    /**String formatiert im Englischen Format*/
    private String aktionaerAnredeDE = "";
    /**Herr, Frau etc. gemäß Schlüssel*/
    private String aktionaerAnredeEN = "";
    private String aktionaerTitel = "";
    private String aktionaerName = "";
    private String aktionaerVorname = "";
    private String aktionaerPlz = "";
    private String aktionaerOrt = "";
    private String aktionaerLandeskuerzel = "";
    /**DE*/
    private String aktionaerLand = "";
    /**Deutschland*/
    private String aktionaerStrasse = "";
    private String aktionaerBriefanredeDE = "";
    /**wie aus anreden-Datei*/
    private String aktionaerBriefanredeEN = "";
    /**wie aus Anreden Datei*/
    private String aktionaerTitelVornameName = "";
    /**Dr. Hans Müller**/
    private String aktionaerNameVornameTitel = "";
    /**Müller, Hans Dr.*/
    private String aktionaerKompletteAnredeDE = "";
    /**"Sehr geehrter Herr Dr. Müller" oder bei juristische Person "Sehr geehrte Damen und Herren"*/
    private String aktionaerKompletteAnredeEN = "";
    /**"Sehr geehrter Herr Dr. Müller" oder bei juristische Person "Sehr geehrte Damen und Herren"*/
    private String aktionaerBesitzArtKuerzel = "";
    private String aktionaerBesitzArt = "";
    private String aktionaerBesitzArtEN = "";
    private int gattung = 0;

    private long stueckAktien = 0;

    private int anzZutrittsIdentSelbst = 0;
    private int anzZutrittsIdentVollmacht = 0;
    private int anzVollmachtenDritte = 0;
    private int anzKIAVSRV = 0;
    private boolean zweiEKMoeglich = false;

    private boolean willenserklaerungenVorhanden = false;

    private boolean fixAnmeldung = false;

    private String zusatzfeld3 = "";

    /**Wird verwendet, um den Butten "Weitere Willenserklärung" in aStatus anzuzeigen (true) oder nicht (false) */
    private boolean weitereWillenserklaerungMoeglich = false;

    /**Wird verwendet, um den Butten "Weitere Willenserklärung" in aStatus nicht anzuzeigen (true) oder nicht (false), 
     * wenn nur "Vollmacht an Dritte" als weitere Willenserklärung möglich ist*/
    private boolean weitereWillenserklaerungMoeglichNurVollmachtDritte = false;

    /**Wird verwendet, um in aNeueWillenserklaerung die Möglichkeit "EK" anzuzeigen (true)*/
    private boolean weitereEKMoeglich = false;
    /**Wird verwendet, um in aNeueWillenserklaerung die Möglichkeit "SRV" anzuzeigen (true)*/
    private boolean weitereSRVMoeglich = false;
    /**Wird verwendet, um in aNeueWillenserklaerung die Möglichkeit "KIAV" anzuzeigen (true)*/
    private boolean weitereKIAVMoeglich = false;
    /**Wird verwendet, um in aNeueWillenserklaerung die Möglichkeit "Briefwahl" anzuzeigen (true)*/
    private boolean weitereBriefwahlMoeglich = false;
    /**Wird verwendet, um in aNeueWillenserklaerung die Möglichkeit "Vollmacht An Dritte" anzuzeigen (true)*/
    private boolean weitereVollmachtDritteMoeglich = false;

    /**Wird verwendet, um in aNeueWillenserklaerungEK alle EK-Möglichkeiten anzuzeigen (außer für Vollmacht an Dritte - EK!)*/
    private boolean weitereEKSelbstMoeglich = false;
    /**Wird verwendet, um in aNeueWillenserklaerungEK alle EK-Vollmacht-An-Dritte-Möglichkeit anzuzeigen*/
    private boolean weitereEKDritteMoeglich = false;

    private List<EclWillenserklaerungStatusM> zugeordneteWillenserklaerungenListM = new LinkedList<>();

    /**Nr der höchsten vergebenen Willenserklärung für diese Meldung. Dient dazu, um festzustellen, ob seit Einlesen
     * der Willenserklärungen (in einem früheren  Transaktionsschritt) neue Willenserklärungen dazugekommen sind
     */
    private int identHoechsteWillenserklaerung = 0;

    public void copyFromOhneStorno(EclZugeordneteMeldung pMeldungZuAktienregister, HVParam pHVParam) {
        /*Achtung: pHVParam müssen hier übergeben werden, da in "Folgefunktionen" benötigt, und diese Methode auch als POJO
         * verwendet wird und darin keine Injects verarbeitet werden dürfen!
         */
        copyFrom(pMeldungZuAktienregister, false, pHVParam);
    }

    public void copyFromMitStorno(EclZugeordneteMeldung pMeldungZuAktienregister, HVParam pHVParam) {
        /*Achtung: pHVParam müssen hier übergeben werden, da in "Folgefunktionen" benötigt, und diese Methode auch als POJO
         * verwendet wird und darin keine Injects verarbeitet werden dürfen!
         */
        copyFrom(pMeldungZuAktienregister, true, pHVParam);
    }

    public void copyFrom(/*DbBundle pDbBundle, */EclZugeordneteMeldung pMeldungZuAktienregister, boolean komplett,
            HVParam pHVParam) {
        /*Achtung: pHVParam müssen hier übergeben werden, da in "Folgefunktionen" benötigt, und diese Methode auch als POJO
         * verwendet wird und darin keine Injects verarbeitet werden dürfen!
         */

        int i;

        this.artBeziehung = pMeldungZuAktienregister.artBeziehung;
        this.aktienregisterIdent = pMeldungZuAktienregister.aktienregisterIdent;
        this.personNatJurIdent = pMeldungZuAktienregister.personNatJurIdent;
        this.meldungsIdent = pMeldungZuAktienregister.meldungsIdent;
        this.klasse = pMeldungZuAktienregister.klasse;

        //		this.kartenart=pMeldungZuAktienregister.kartenart;
        //		this.kartennr=pMeldungZuAktienregister.kartennr;
        this.identPersonNatJur = pMeldungZuAktienregister.identPersonNatJur;
        this.istPraesent = pMeldungZuAktienregister.istPraesent;
        this.istPraesentNeu = pMeldungZuAktienregister.istPraesentNeu;
        this.praesenteZutrittsIdent = pMeldungZuAktienregister.praesenteZutrittsIdent;
        this.praesenteZutrittsIdentNeben = pMeldungZuAktienregister.praesenteZutrittsIdentNeben;

        this.aktionaerName = pMeldungZuAktienregister.aktionaerName;
        this.aktionaerVorname = pMeldungZuAktienregister.aktionaerVorname;
        this.aktionaerOrt = pMeldungZuAktienregister.aktionaerOrt;

        this.aktionaerStimmen = pMeldungZuAktienregister.aktionaerStimmen;
        this.aktionaerStimmenDE = pMeldungZuAktienregister.aktionaerStimmenDE;
        this.aktionaerStimmenEN = pMeldungZuAktienregister.aktionaerStimmenEN;
        this.aktionaerAnredeDE = pMeldungZuAktienregister.aktionaerAnredeDE;
        this.aktionaerAnredeEN = pMeldungZuAktienregister.aktionaerAnredeEN;
        this.aktionaerTitel = pMeldungZuAktienregister.aktionaerTitel;
        this.aktionaerName = pMeldungZuAktienregister.aktionaerName;
        this.aktionaerVorname = pMeldungZuAktienregister.aktionaerVorname;
        this.aktionaerPlz = pMeldungZuAktienregister.aktionaerPlz;
        this.aktionaerOrt = pMeldungZuAktienregister.aktionaerOrt;
        this.aktionaerLandeskuerzel = pMeldungZuAktienregister.aktionaerLandeskuerzel;
        this.aktionaerLand = pMeldungZuAktienregister.aktionaerLand;
        this.aktionaerStrasse = pMeldungZuAktienregister.aktionaerStrasse;
        this.aktionaerBriefanredeDE = pMeldungZuAktienregister.aktionaerBriefanredeDE;
        this.aktionaerBriefanredeEN = pMeldungZuAktienregister.aktionaerBriefanredeEN;
        this.aktionaerTitelVornameName = pMeldungZuAktienregister.aktionaerTitelVornameName;
        this.aktionaerNameVornameTitel = pMeldungZuAktienregister.aktionaerNameVornameTitel;
        this.aktionaerKompletteAnredeDE = pMeldungZuAktienregister.aktionaerKompletteAnredeDE;
        this.aktionaerKompletteAnredeEN = pMeldungZuAktienregister.aktionaerKompletteAnredeEN;
        this.aktionaerBesitzArtKuerzel = pMeldungZuAktienregister.aktionaerBesitzArtKuerzel;
        this.aktionaerBesitzArt = pMeldungZuAktienregister.aktionaerBesitzArt;
        this.aktionaerBesitzArtEN = pMeldungZuAktienregister.aktionaerBesitzArtEN;
        this.gattung = pMeldungZuAktienregister.gattung;

        this.stueckAktien = pMeldungZuAktienregister.stueckAktien;
        this.anzZutrittsIdentSelbst = pMeldungZuAktienregister.anzZutrittsIdentSelbst;
        this.anzZutrittsIdentVollmacht = pMeldungZuAktienregister.anzZutrittsIdentVollmacht;
        this.anzVollmachtenDritte = pMeldungZuAktienregister.anzVollmachtenDritte;
        this.anzKIAVSRV = pMeldungZuAktienregister.anzKIAVSRV;
        this.zweiEKMoeglich = pMeldungZuAktienregister.zweiEKMoeglich;
        this.willenserklaerungenVorhanden = pMeldungZuAktienregister.willenserklaerungenVorhanden;
        this.fixAnmeldung = pMeldungZuAktienregister.fixAnmeldung;

        this.zugeordneteWillenserklaerungenListM = new LinkedList<>();

        this.zusatzfeld3 = pMeldungZuAktienregister.zusatzfeld3;

        for (i = 0; i < pMeldungZuAktienregister.zugeordneteWillenserklaerungenList.size(); i++) {
            if (pMeldungZuAktienregister.zugeordneteWillenserklaerungenList.get(i).storniert == false
                    || komplett == true) {
                EclWillenserklaerungStatusM lWillenserklaerungStatus = new EclWillenserklaerungStatusM();

                lWillenserklaerungStatus.copyFrom(pMeldungZuAktienregister.zugeordneteWillenserklaerungenList.get(i),
                        pHVParam);
                zugeordneteWillenserklaerungenListM.add(lWillenserklaerungStatus);
            }
        }
        if (zugeordneteWillenserklaerungenListM.size() == 0) {
            EclWillenserklaerungStatusM lWillenserklaerungStatus = new EclWillenserklaerungStatusM();
            lWillenserklaerungStatus.setIstLeerDummy(true);
            zugeordneteWillenserklaerungenListM.add(lWillenserklaerungStatus);
        }
        this.identHoechsteWillenserklaerung = pMeldungZuAktienregister.identHoechsteWillenserklaerung;

        /**True, wenn:
         * Vollmacht an Dritte noch zulässig (denn die sind immer zulässig, wenn dies überhaupt angeboten wird)
         * ODER
         * (keine Willenserklärung vorhanden ist, UND zumindest eine der Willenserklärungsarten überhaupt zulässig ist)
         * ODER
         * (EKs überhaupt zulässig UND (beliebig viele EKs an dritte möglich sind 
         * 								ODER
         * 								(keine EK ausgestellt wurde UND keine KIAVSRV erteilt sind)
         * 								ODER 
         * 								(keine EK ausgestellt wurde UND EK+KIAVSRV gleichzeitig möglich sind)
         * )
         * ODER
         * ((SRV oder Briefwahl oder KIAV überhaupt möglich sind) UND (
         * 								(keine EK ausgestellt wurde UND keine KIAVSRV erteilt sind)
         * 								ODER 
         * 								(keine (SRV oder Briefwahl oder KIAV) ausgestellt wurde und EK+KIAVSRV gleichzeitig möglich sind)
         * 								)
         * )
         * 							
         *   
         */
        this.weitereWillenserklaerungMoeglich = (pHVParam.paramPortal.lfdHVPortalVollmachtDritteIstMoeglich == 1
                || (anzZutrittsIdentSelbst + anzZutrittsIdentVollmacht + anzKIAVSRV == 0
                        && (pHVParam.paramPortal.lfdHVPortalEKIstMoeglich == 1
                                || pHVParam.paramPortal.lfdHVPortalSRVIstMoeglich == 1
                                || pHVParam.paramPortal.lfdHVPortalBriefwahlIstMoeglich == 1
                                || pHVParam.paramPortal.lfdHVPortalKIAVIstMoeglich == 1))
                || (pHVParam.paramPortal.lfdHVPortalEKIstMoeglich == 1
                        && (pHVParam.paramPortal.zusaetzlicheEKDritteMoeglich == 1
                                || (anzZutrittsIdentSelbst + anzZutrittsIdentVollmacht + anzKIAVSRV == 0)
                                || (anzZutrittsIdentSelbst + anzZutrittsIdentVollmacht == 0
                                        && pHVParam.paramPortal.ekUndWeisungGleichzeitigMoeglich == 1)))
                || ((pHVParam.paramPortal.lfdHVPortalSRVIstMoeglich == 1
                        || pHVParam.paramPortal.lfdHVPortalBriefwahlIstMoeglich == 1
                        || pHVParam.paramPortal.lfdHVPortalKIAVIstMoeglich == 1)
                        && ((anzZutrittsIdentSelbst + anzZutrittsIdentVollmacht + anzKIAVSRV == 0) || (

                        /*anzZutrittsIdentVollmacht+*/anzKIAVSRV == 0
                                && pHVParam.paramPortal.ekUndWeisungGleichzeitigMoeglich == 1))));

        this.weitereEKMoeglich = ((anzZutrittsIdentSelbst + anzZutrittsIdentVollmacht + anzKIAVSRV == 0
                && (pHVParam.paramPortal.lfdHVPortalEKIstMoeglich == 1))
                || (pHVParam.paramPortal.lfdHVPortalEKIstMoeglich == 1
                        && (pHVParam.paramPortal.zusaetzlicheEKDritteMoeglich == 1
                                || (anzZutrittsIdentSelbst + anzZutrittsIdentVollmacht + anzKIAVSRV == 0)
                                || (anzZutrittsIdentSelbst + anzZutrittsIdentVollmacht == 0
                                        && pHVParam.paramPortal.ekUndWeisungGleichzeitigMoeglich == 1))));

        weitereSRVMoeglich = (pHVParam.paramPortal.lfdHVPortalSRVIstMoeglich == 1
                && (anzZutrittsIdentSelbst + anzZutrittsIdentVollmacht + anzKIAVSRV == 0
                        || (anzKIAVSRV == 0 && pHVParam.paramPortal.ekUndWeisungGleichzeitigMoeglich == 1) ||
                        /*Singulus - SRV auch möglich wenn Briefwahl bereits erteilt*/
                        (pMeldungZuAktienregister.anzSRV == 0
                                && pHVParam.paramPortal.srvZusaetzlichZuBriefwahlMoeglich == 1)));

        weitereKIAVMoeglich = (pHVParam.paramPortal.lfdHVPortalKIAVIstMoeglich == 1
                && (anzZutrittsIdentSelbst + anzZutrittsIdentVollmacht + anzKIAVSRV == 0
                        || (anzKIAVSRV == 0 && pHVParam.paramPortal.ekUndWeisungGleichzeitigMoeglich == 1)));

        weitereBriefwahlMoeglich = (pHVParam.paramPortal.lfdHVPortalBriefwahlIstMoeglich == 1
                && (anzZutrittsIdentSelbst + anzZutrittsIdentVollmacht + anzKIAVSRV == 0
                        || (anzKIAVSRV == 0 && pHVParam.paramPortal.ekUndWeisungGleichzeitigMoeglich == 1) ||
                        /*ku168*/
                        (pMeldungZuAktienregister.anzBriefwahl == 0
                                && pHVParam.paramPortal.briefwahlZusaetzlichZuSRVMoeglich == 1)));

        weitereVollmachtDritteMoeglich = (pHVParam.paramPortal.lfdHVPortalVollmachtDritteIstMoeglich == 1);
        this.weitereEKSelbstMoeglich = ((pHVParam.paramPortal.lfdHVPortalEKIstMoeglich == 1)
                && ((anzKIAVSRV == 0 || pHVParam.paramPortal.ekUndWeisungGleichzeitigMoeglich == 1)
                        && (anzZutrittsIdentSelbst + anzZutrittsIdentVollmacht == 0
                                || pHVParam.paramPortal.zusaetzlicheEKDritteMoeglich == 1)
                        && (anzZutrittsIdentSelbst == 0)

                ));

        this.weitereEKDritteMoeglich = ((pHVParam.paramPortal.lfdHVPortalEKIstMoeglich == 1)
                && ((anzKIAVSRV == 0 || pHVParam.paramPortal.ekUndWeisungGleichzeitigMoeglich == 1)
                        && (anzZutrittsIdentSelbst + anzZutrittsIdentVollmacht == 0
                                || pHVParam.paramPortal.zusaetzlicheEKDritteMoeglich == 1)
                        && (anzZutrittsIdentVollmacht == 0 || pHVParam.paramPortal.zusaetzlicheEKDritteMoeglich == 1)

                ));

        if (weitereEKMoeglich == true || weitereSRVMoeglich == true || weitereKIAVMoeglich == true
                || weitereBriefwahlMoeglich == true) {
            this.weitereWillenserklaerungMoeglichNurVollmachtDritte = false;
        } else {
            this.weitereWillenserklaerungMoeglichNurVollmachtDritte = true;

        }

    }

    public void copyFromMOhneStorno(EclZugeordneteMeldungM pMeldungZuAktienregister) {
        copyFromM(pMeldungZuAktienregister, false);
    }

    public void copyFromMMitStorno(EclZugeordneteMeldungM pMeldungZuAktienregister) {
        copyFromM(pMeldungZuAktienregister, true);
    }

    public void copyFromM(EclZugeordneteMeldungM pMeldungZuAktienregister, boolean komplett) {
        int i;

        this.artBeziehung = pMeldungZuAktienregister.artBeziehung;
        this.aktienregisterIdent = pMeldungZuAktienregister.aktienregisterIdent;
        this.personNatJurIdent = pMeldungZuAktienregister.personNatJurIdent;
        this.meldungsIdent = pMeldungZuAktienregister.meldungsIdent;
        this.klasse = pMeldungZuAktienregister.klasse;

        //		this.kartenart=pMeldungZuAktienregister.kartenart;
        //		this.kartennr=pMeldungZuAktienregister.kartennr;
        this.identPersonNatJur = pMeldungZuAktienregister.identPersonNatJur;
        this.istPraesent = pMeldungZuAktienregister.istPraesent;
        this.istPraesentNeu = pMeldungZuAktienregister.istPraesentNeu;
        this.praesenteZutrittsIdent = pMeldungZuAktienregister.praesenteZutrittsIdent;
        this.praesenteZutrittsIdentNeben = pMeldungZuAktienregister.praesenteZutrittsIdentNeben;

        this.aktionaerStimmen = pMeldungZuAktienregister.aktionaerStimmen;
        this.aktionaerStimmenDE = pMeldungZuAktienregister.aktionaerStimmenDE;
        this.aktionaerStimmenEN = pMeldungZuAktienregister.aktionaerStimmenEN;
        this.aktionaerAnredeDE = pMeldungZuAktienregister.aktionaerAnredeDE;
        this.aktionaerAnredeEN = pMeldungZuAktienregister.aktionaerAnredeEN;
        this.aktionaerTitel = pMeldungZuAktienregister.aktionaerTitel;
        this.aktionaerName = pMeldungZuAktienregister.aktionaerName;
        this.aktionaerVorname = pMeldungZuAktienregister.aktionaerVorname;
        this.aktionaerPlz = pMeldungZuAktienregister.aktionaerPlz;
        this.aktionaerOrt = pMeldungZuAktienregister.aktionaerOrt;
        this.aktionaerLandeskuerzel = pMeldungZuAktienregister.aktionaerLandeskuerzel;
        this.aktionaerLand = pMeldungZuAktienregister.aktionaerLand;
        this.aktionaerStrasse = pMeldungZuAktienregister.aktionaerStrasse;
        this.aktionaerBriefanredeDE = pMeldungZuAktienregister.aktionaerBriefanredeDE;
        this.aktionaerBriefanredeEN = pMeldungZuAktienregister.aktionaerBriefanredeEN;
        this.aktionaerTitelVornameName = pMeldungZuAktienregister.aktionaerTitelVornameName;
        this.aktionaerNameVornameTitel = pMeldungZuAktienregister.aktionaerNameVornameTitel;
        this.aktionaerKompletteAnredeDE = pMeldungZuAktienregister.aktionaerKompletteAnredeDE;
        this.aktionaerKompletteAnredeEN = pMeldungZuAktienregister.aktionaerKompletteAnredeEN;
        this.aktionaerBesitzArtKuerzel = pMeldungZuAktienregister.aktionaerBesitzArtKuerzel;
        this.aktionaerBesitzArt = pMeldungZuAktienregister.aktionaerBesitzArt;
        this.aktionaerBesitzArtEN = pMeldungZuAktienregister.aktionaerBesitzArtEN;

        this.gattung = pMeldungZuAktienregister.gattung;

        this.stueckAktien = pMeldungZuAktienregister.stueckAktien;
        this.anzZutrittsIdentSelbst = pMeldungZuAktienregister.anzZutrittsIdentSelbst;
        this.anzZutrittsIdentVollmacht = pMeldungZuAktienregister.anzZutrittsIdentVollmacht;
        this.anzVollmachtenDritte = pMeldungZuAktienregister.anzVollmachtenDritte;
        this.anzKIAVSRV = pMeldungZuAktienregister.anzKIAVSRV;
        this.zweiEKMoeglich = pMeldungZuAktienregister.zweiEKMoeglich;
        this.willenserklaerungenVorhanden = pMeldungZuAktienregister.willenserklaerungenVorhanden;
        this.fixAnmeldung = pMeldungZuAktienregister.fixAnmeldung;

        this.weitereWillenserklaerungMoeglich = pMeldungZuAktienregister.weitereWillenserklaerungMoeglich;
        this.weitereEKMoeglich = pMeldungZuAktienregister.weitereEKMoeglich;
        this.weitereSRVMoeglich = pMeldungZuAktienregister.weitereSRVMoeglich;
        this.weitereKIAVMoeglich = pMeldungZuAktienregister.weitereKIAVMoeglich;
        this.weitereBriefwahlMoeglich = pMeldungZuAktienregister.weitereBriefwahlMoeglich;
        this.weitereVollmachtDritteMoeglich = pMeldungZuAktienregister.weitereVollmachtDritteMoeglich;
        this.weitereEKSelbstMoeglich = pMeldungZuAktienregister.weitereEKSelbstMoeglich;
        this.weitereEKDritteMoeglich = pMeldungZuAktienregister.weitereEKDritteMoeglich;

        this.weitereWillenserklaerungMoeglichNurVollmachtDritte = pMeldungZuAktienregister.weitereWillenserklaerungMoeglichNurVollmachtDritte;

        this.zugeordneteWillenserklaerungenListM = new LinkedList<>();

        for (i = 0; i < pMeldungZuAktienregister.zugeordneteWillenserklaerungenListM.size(); i++) {
            if (pMeldungZuAktienregister.zugeordneteWillenserklaerungenListM.get(i).isStorniert() == false
                    || komplett == true) {
                EclWillenserklaerungStatusM lWillenserklaerungStatus = new EclWillenserklaerungStatusM();
                lWillenserklaerungStatus.copyFromM(pMeldungZuAktienregister.zugeordneteWillenserklaerungenListM.get(i));
                zugeordneteWillenserklaerungenListM.add(lWillenserklaerungStatus);
            }
        }

        if (zugeordneteWillenserklaerungenListM.size() == 0) {
            EclWillenserklaerungStatusM lWillenserklaerungStatus = new EclWillenserklaerungStatusM();
            lWillenserklaerungStatus.setIstLeerDummy(true);
            zugeordneteWillenserklaerungenListM.add(lWillenserklaerungStatus);
        }
        this.identHoechsteWillenserklaerung = pMeldungZuAktienregister.identHoechsteWillenserklaerung;
    }

    /***********ab hier Standard Getters and Setters****************************/

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

    public long getStueckAktien() {
        return stueckAktien;
    }

    public void setStueckAktien(long stueckAktien) {
        this.stueckAktien = stueckAktien;
    }

    public List<EclWillenserklaerungStatusM> getZugeordneteWillenserklaerungenListM() {
        return zugeordneteWillenserklaerungenListM;
    }

    public void setZugeordneteWillenserklaerungenListM(
            List<EclWillenserklaerungStatusM> zugeordneteWillenserklaerungenListM) {
        this.zugeordneteWillenserklaerungenListM = zugeordneteWillenserklaerungenListM;
    }

    public int getAnzVollmachtenDritte() {
        return anzVollmachtenDritte;
    }

    public void setAnzVollmachtenDritte(int anzVollmachtenDritte) {
        this.anzVollmachtenDritte = anzVollmachtenDritte;
    }

    public int getAnzKIAVSRV() {
        return anzKIAVSRV;
    }

    public void setAnzKIAVSRV(int anzKIAVSRV) {
        this.anzKIAVSRV = anzKIAVSRV;
    }

    public int getAnzZutrittsIdentSelbst() {
        return anzZutrittsIdentSelbst;
    }

    public void setAnzZutrittsIdentSelbst(int anzZutrittsIdentSelbst) {
        this.anzZutrittsIdentSelbst = anzZutrittsIdentSelbst;
    }

    public int getAnzZutrittsIdentVollmacht() {
        return anzZutrittsIdentVollmacht;
    }

    public void setAnzZutrittsIdentVollmacht(int anzZutrittsIdentVollmacht) {
        this.anzZutrittsIdentVollmacht = anzZutrittsIdentVollmacht;
    }

    public int getPersonNatJurIdent() {
        return personNatJurIdent;
    }

    public void setPersonNatJurIdent(int personNatJurIdent) {
        this.personNatJurIdent = personNatJurIdent;
    }

    public boolean isWillenserklaerungenVorhanden() {
        return willenserklaerungenVorhanden;
    }

    public void setWillenserklaerungenVorhanden(boolean willenserklaerungenVorhanden) {
        this.willenserklaerungenVorhanden = willenserklaerungenVorhanden;
    }

    public int getArtBeziehung() {
        return artBeziehung;
    }

    public void setArtBeziehung(int artBeziehung) {
        this.artBeziehung = artBeziehung;
    }

    public int getIstPraesent() {
        return istPraesent;
    }

    public void setIstPraesent(int istPraesent) {
        this.istPraesent = istPraesent;
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

    public String getAktionaerOrt() {
        return aktionaerOrt;
    }

    public void setAktionaerOrt(String aktionaerOrt) {
        this.aktionaerOrt = aktionaerOrt;
    }

    //	public int getKartenart() {
    //		return kartenart;
    //	}
    //
    //	public void setKartenart(int kartenart) {
    //		this.kartenart = kartenart;
    //	}
    //
    //	public int getKartennr() {
    //		return kartennr;
    //	}
    //
    //	public void setKartennr(int kartennr) {
    //		this.kartennr = kartennr;
    //	}

    public int getIdentPersonNatJur() {
        return identPersonNatJur;
    }

    public void setIdentPersonNatJur(int identPersonNatJur) {
        this.identPersonNatJur = identPersonNatJur;
    }

    public boolean isZweiEKMoeglich() {
        return zweiEKMoeglich;
    }

    public void setZweiEKMoeglich(boolean zweiEKMoeglich) {
        this.zweiEKMoeglich = zweiEKMoeglich;
    }

    public int getIdentHoechsteWillenserklaerung() {
        return identHoechsteWillenserklaerung;
    }

    public void setIdentHoechsteWillenserklaerung(int identHoechsteWillenserklaerung) {
        this.identHoechsteWillenserklaerung = identHoechsteWillenserklaerung;
    }

    public long getAktionaerStimmen() {
        return aktionaerStimmen;
    }

    public void setAktionaerStimmen(long aktionaerStimmen) {
        this.aktionaerStimmen = aktionaerStimmen;
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

    public String getAktionaerTitelVornameName() {
        return aktionaerTitelVornameName;
    }

    public void setAktionaerTitelVornameName(String aktionaerTitelVornameName) {
        this.aktionaerTitelVornameName = aktionaerTitelVornameName;
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

    public boolean isFixAnmeldung() {
        return fixAnmeldung;
    }

    public void setFixAnmeldung(boolean fixAnmeldung) {
        this.fixAnmeldung = fixAnmeldung;
    }

    public int getIstPraesentNeu() {
        return istPraesentNeu;
    }

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

    public int getGattung() {
        if (gattung == 0) {
            return 1;
        }
        return gattung;
    }

    public void setGattung(int gattung) {
        this.gattung = gattung;
    }

    public String getZusatzfeld3() {
        return zusatzfeld3;
    }

    public void setZusatzfeld3(String zusatzfeld3) {
        this.zusatzfeld3 = zusatzfeld3;
    }

}
