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
package de.meetingapps.meetingportal.meetComBl;

import java.util.LinkedList;
import java.util.List;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaString;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclAnrede;
import de.meetingapps.meetingportal.meetComEntities.EclMeldung;
import de.meetingapps.meetingportal.meetComEntities.EclMeldungZuSammelkarte;
import de.meetingapps.meetingportal.meetComEntities.EclPersonenNatJur;
import de.meetingapps.meetingportal.meetComEntities.EclWeisungMeldung;
import de.meetingapps.meetingportal.meetComEntities.EclWillenserklaerung;
import de.meetingapps.meetingportal.meetComEntities.EclWillenserklaerungStatus;
import de.meetingapps.meetingportal.meetComEntities.EclWillenserklaerungZusatz;
import de.meetingapps.meetingportal.meetComEntities.EclZugeordneteMeldung;
import de.meetingapps.meetingportal.meetComEntities.EclZutrittsIdent;
import de.meetingapps.meetingportal.meetComHVParam.ParamSpezial;
import de.meetingapps.meetingportal.meetComKonst.KonstWillenserklaerung;

/**
 * Vollständig Aufwärtskompatibel zu BlWillenserklaerungStatus
 * 
 * Enthält zusätzliche Parameter zur Steuerung, welche bzw. in welcher Form die Willenserklärungen zurückgegeben (selektiert) werden
 * sollen.
 * @author N.N
 *
 */
public class BlWillenserklaerungStatus {

    private int logDrucken = 3;

    private DbBundle lDbBundle = null;

    /**1 = es werden nur die Willenserklärungen zurückgegeben, die der "aktuell eingeloggte" Teilnehmer abgegeben hat
     * 		d.i.: 
     * 		> bei Aktionär angemeldet: alle, die der Aktionär gegeben hat (also z.B. keine Untervollmachten)
     * 		> bei natürlicher Person: alle, die diese natürliche Person gegeben hat
     * (zu V2: Standardoutput bei BlWillenserklaerungStatus)
     * 2 = es werden alle Willenserklärungen zurückgegeben, die zu dieser Anmeldung gehören. Also "über- und unter-Vollmachten". 			
     */
    public int piSelektionGeberOderAlle = 1;

    /**1 = aus Speicherplatzgründen werden die Ecl s selbst nicht mit im Array zurückgeliefert, sondern nur in 
     * "aufbereiteter Form" (=V2)
     * 2 = die vollständigen EclWillenserklarung, EclWillenserklaerungZusatz, EclMeldung werden mit zurückgegeben
     * 
     * Hinweis: 2 ist nur sinnvoll, wenn gleichzeitig auch piAnsichtVerarbeitungOderAnalyse==1. Ansonsten werden beim 
     * "Zusammenfassen" der Willenserklärungen ja alte Verweise auf eclWillenserklaerung überschrieben!
     */
    public int piRueckgabeKurzOderLang = 1;

    /**1 = es wird die "Verarbeitungsansicht" zurückgeliefert:
     * Veränderungen und Stornierungen werden mit der ursprünglichen Willenserklärung zusammengefaßt und tauchen
     * nicht separat in der Willenserklärungsliste auf. Dies ist die Ansicht für den "Aktionär" oder für manuelle
     * zusätzliche Erfassung von Willenserklärungen (=V2)
     * 2 = es wird die Analysesicht zurückgeliefert:
     * Willenserklärungen, die verändert oder storniert wurden, werden zwar als solche Gekennzeichnet. Die
     * "Veränderungs- oder Stornierungs-Willenserklärung" wird jedoch in einer separaten Willenserklärung zurückgeliefert.
     * Ansicht für den Komplett-Ausdruck aller Willenserklärungen z.B. für Analyse oder Übertragungszwecke
     */
    public int piAnsichtVerarbeitungOderAnalyse = 1;

    /**=0 => nur die fürs Portal relevanten Willenserklärungen werden zurückgegeben
     * =1 => auch Präsenzwillenserklärungen werden aufbereitet
     */
    public int piAlleWillenserklaerungen = 0;

    private String bezeichnungEintrittskarteDE = "Eintrittskarte Nr. ";
    private String bezeichnungEintrittskarteEN = "Admission ticket ";

    public BlWillenserklaerungStatus(DbBundle pDbBundle) {
        lDbBundle = pDbBundle;
        bezeichnungEintrittskarteDE = lDbBundle.param.paramPortal.ekText + " Nr. ";
        bezeichnungEintrittskarteEN = lDbBundle.param.paramPortal.ekTextEN + " ";
        //		if (pDbBundle.param.paramPortal.fragenAngeboten==1) {
        //			bezeichnungEintrittskarteDE="HV-Ticket Nr. ";
        //			bezeichnungEintrittskarteEN="Admission ticket ";
        //		}

    }

    /**Ergebnis: alle Anmeldungen mit eigenen Aktien, die auf den Teilnehmer erfolgt sind.
     * Achtung: "eigene" heißt in diesem Fall auch Anmeldungen auf Fremdbesitz.
     * 
     * Hinweis: bei der Suche über den Aktienregistereintrag (also für die Standardportalsicht "Aktionär" sozusagen)
     * sind hierin auch die Gästekarten enthalten, die der Aktionär angefordert hat
     * (=Anmeldung von diesem Aktionär ausgehend, die ja nicht auf ihn ausgehen)
     */
    public EclZugeordneteMeldung[] zugeordneteMeldungenEigeneAktienArray = null;
    /*Anzahl Gastkarten, die ausgehend von den eigenen Meldungen ausgestellt wurden*/
    public int gastKartenGemeldetEigeneAktien = 0;
    /**Wird durch leseMeldungenZuAktienregisterIdent gemeldet - PersonNatJurIdent aus
     * den Aktien-Anmeldungen (für Verkettung von Gastkarten und erhaltenen Vollmachten
     * erforderlich!
     */
    public int aktienregisterPersonNatJurIdent = 0;

    /**Ergebnis: alle Anmeldungen als Gast, die auf den Teilnehmer erfolgt sind.
     * Können auch Gruppenkarten-Ausstellungen sein.
     * 
     * Hinweis: falls eine Gastkarte storniert wurde, ist hier ein Eintrag OHNE Zutrittsident
     * enthalten (da ja der Gast möglicherweise noch andere Aktivitäten durchgeführt hat, bzw. auch
     * Vollmachten erhalten hat).
     * Für Gästeanmeldungen kann KEINE weitere Willenserklärung abgegeben werden, insbesondere KEINE neue
     * ZutrittsIdent ausgestellt werden!
     */
    public EclZugeordneteMeldung[] zugeordneteMeldungenEigeneGastkartenArray = null;

    /**Ergebnis: alle Meldungen, bei denen Vollmachten auf den Teilnehmer ausgestellt sind
     * (direkt oder indirekt).
     * Hierzu sind weitere Willenserklärungen möglicherweise vorhanden, bzw. können diese geändert
     * oder storniert werden (ähnlich wie bei eigenen Meldungen).
     */
    public EclZugeordneteMeldung[] zugeordneteMeldungenBevollmaechtigtArray = null;

    public boolean briefwahlVorhanden = false;
    public boolean srvVorhanden = false;

    /**
     * Gibt an, ob in allen zugeordneten / eingelesenen Sätzen auch ein präsenter vorhanden ist.
     * 
     * Achtung, bei nurRawLiveAbstimmung==1 immer false, da in diesem Fall der Präsenzstatus nicht
     * in die Tables zurückgespeichert wird, sondern nur im Speicher gehalten wird.
     */
    public boolean praesenteVorhanden = false;
    /**
     * Gibt an, ob in allen zugeordneten / eingelesenen Sätzen auch ein präsenter vorhanden ist.
     * 
     * Achtung, bei nurRawLiveAbstimmung==1 immer false, da in diesem Fall der Präsenzstatus nicht
     * in die Tables zurückgespeichert wird, sondern nur im Speicher gehalten wird.
     */
    public boolean nichtPraesenteVorhanden = false;

    public boolean rcHatNichtNurPortalWillenserklaerungen = false;
    public String rcDatumLetzteWillenserklaerung = "";

    public List<EclPersonenNatJur> rcListeVollmachten = null;

    /*Füllen der Daten aktionaer* in EclZugeordneteMeldung*/
    private void fuelleAktionaersdaten(EclZugeordneteMeldung pZugeordneteMeldung, boolean mitStimmen) {

        EclMeldung zMeldung = lDbBundle.dbMeldungen.meldungenArray[0];
        fuelleAktionaersdaten(pZugeordneteMeldung, mitStimmen, zMeldung);
    }

    private void fuelleAktionaersdaten(EclZugeordneteMeldung pZugeordneteMeldung, boolean mitStimmen,
            EclMeldung zMeldung) {

        pZugeordneteMeldung.eclMeldung = zMeldung;
        pZugeordneteMeldung.personNatJurIdent = zMeldung.personenNatJurIdent;

        if (mitStimmen == true) {
            pZugeordneteMeldung.stueckAktien = zMeldung.stueckAktien;

            pZugeordneteMeldung.aktionaerStimmen = zMeldung.stimmen;
            pZugeordneteMeldung.aktionaerStimmenDE = CaString.toStringDE(pZugeordneteMeldung.aktionaerStimmen);
            pZugeordneteMeldung.aktionaerStimmenEN = CaString.toStringEN(pZugeordneteMeldung.aktionaerStimmen);
            pZugeordneteMeldung.aktionaerBesitzArtKuerzel = zMeldung.besitzart;
            pZugeordneteMeldung.gattung = zMeldung.gattung;
            switch (zMeldung.besitzart) {
            case "E": {
                pZugeordneteMeldung.aktionaerBesitzArt = "Eigenbesitz";
                pZugeordneteMeldung.aktionaerBesitzArtEN = "Proprietary Possession";
                break;
            }
            case "F": {
                pZugeordneteMeldung.aktionaerBesitzArt = "Fremdbesitz";
                pZugeordneteMeldung.aktionaerBesitzArtEN = "Minority Interests";
                break;
            }
            case "V": {
                pZugeordneteMeldung.aktionaerBesitzArt = "Vollmachtsbesitz";
                pZugeordneteMeldung.aktionaerBesitzArtEN = "Proxy Possession";
                break;
            }
            }
        }

        pZugeordneteMeldung.aktionaerTitel = zMeldung.titel;
        pZugeordneteMeldung.aktionaerName = zMeldung.name;
        pZugeordneteMeldung.aktionaerVorname = zMeldung.vorname;
        pZugeordneteMeldung.aktionaerPlz = zMeldung.plz;
        pZugeordneteMeldung.aktionaerOrt = zMeldung.ort;
        if (!zMeldung.land.isEmpty()) {
            lDbBundle.dbStaaten.readCode(zMeldung.land);
            if (lDbBundle.dbStaaten.anzErgebnis() > 0) {
                pZugeordneteMeldung.aktionaerLandeskuerzel = lDbBundle.dbStaaten.ergebnisPosition(0).code;
                pZugeordneteMeldung.aktionaerLand = lDbBundle.dbStaaten.ergebnisPosition(0).nameDE;
            }
        }
        pZugeordneteMeldung.aktionaerStrasse = zMeldung.strasse;

        /*Anrede füllen*/
        int anredenNr = zMeldung.anrede;
        EclAnrede hAnrede = new EclAnrede();
        if (anredenNr != 0) {
            lDbBundle.dbAnreden.SetzeSprache(2, 0);
            lDbBundle.dbAnreden.ReadAnrede_Anredennr(anredenNr);
            hAnrede = new EclAnrede();
            if (lDbBundle.dbAnreden.AnzAnredenInReadArray > 0) {
                hAnrede = lDbBundle.dbAnreden.anredenreadarray[0];
            }
            pZugeordneteMeldung.aktionaerAnredeDE = hAnrede.anredentext;
            pZugeordneteMeldung.aktionaerAnredeEN = hAnrede.anredentextfremd;
            pZugeordneteMeldung.aktionaerBriefanredeDE = hAnrede.anredenbrief;
            pZugeordneteMeldung.aktionaerBriefanredeEN = hAnrede.anredenbrieffremd;
        }

        /*Kombi-Felder füllen*/
        pZugeordneteMeldung.aktionaerTitelVornameName = "";
        if (pZugeordneteMeldung.aktionaerTitel.length() != 0) {
            pZugeordneteMeldung.aktionaerTitelVornameName = pZugeordneteMeldung.aktionaerTitelVornameName
                    + pZugeordneteMeldung.aktionaerTitel + " ";
        }
        if (pZugeordneteMeldung.aktionaerVorname.length() != 0) {
            pZugeordneteMeldung.aktionaerTitelVornameName = pZugeordneteMeldung.aktionaerTitelVornameName
                    + pZugeordneteMeldung.aktionaerVorname + " ";
        }
        pZugeordneteMeldung.aktionaerTitelVornameName = pZugeordneteMeldung.aktionaerTitelVornameName
                + pZugeordneteMeldung.aktionaerName;

        pZugeordneteMeldung.aktionaerNameVornameTitel = pZugeordneteMeldung.aktionaerName;
        if (pZugeordneteMeldung.aktionaerTitel.length() != 0 || pZugeordneteMeldung.aktionaerVorname.length() != 0) {
            pZugeordneteMeldung.aktionaerNameVornameTitel = pZugeordneteMeldung.aktionaerNameVornameTitel + ",";
        }
        if (pZugeordneteMeldung.aktionaerTitel.length() != 0) {
            pZugeordneteMeldung.aktionaerNameVornameTitel = pZugeordneteMeldung.aktionaerNameVornameTitel + " "
                    + pZugeordneteMeldung.aktionaerTitel;
        }
        if (pZugeordneteMeldung.aktionaerVorname.length() != 0) {
            pZugeordneteMeldung.aktionaerNameVornameTitel = pZugeordneteMeldung.aktionaerNameVornameTitel + " "
                    + pZugeordneteMeldung.aktionaerVorname;
        }

        pZugeordneteMeldung.aktionaerKompletteAnredeDE = pZugeordneteMeldung.aktionaerBriefanredeDE;
        pZugeordneteMeldung.aktionaerKompletteAnredeEN = pZugeordneteMeldung.aktionaerBriefanredeEN;
        if (hAnrede.istjuristischePerson != 1) {
            if (pZugeordneteMeldung.aktionaerTitel.length() != 0) {
                pZugeordneteMeldung.aktionaerKompletteAnredeDE = pZugeordneteMeldung.aktionaerKompletteAnredeDE + " "
                        + pZugeordneteMeldung.aktionaerTitel;
                pZugeordneteMeldung.aktionaerKompletteAnredeEN = pZugeordneteMeldung.aktionaerKompletteAnredeEN + " "
                        + pZugeordneteMeldung.aktionaerTitel;
            }
            if (pZugeordneteMeldung.aktionaerName.length() != 0) {
                pZugeordneteMeldung.aktionaerKompletteAnredeDE = pZugeordneteMeldung.aktionaerKompletteAnredeDE + " "
                        + pZugeordneteMeldung.aktionaerName;
                pZugeordneteMeldung.aktionaerKompletteAnredeEN = pZugeordneteMeldung.aktionaerKompletteAnredeEN + " "
                        + pZugeordneteMeldung.aktionaerName;
            }
        }

        /*Sonstiges füllen*/
        pZugeordneteMeldung.zusatzfeld3 = zMeldung.zusatzfeld3;

    }

    private void setzePraesenzFelder(EclZugeordneteMeldung pEclZugeordneteMeldung, int pOffsetInDbMeldungen) {

        EclMeldung lEclMeldung = lDbBundle.dbMeldungen.meldungenArray[pOffsetInDbMeldungen];
        setzePraesenzFelder(pEclZugeordneteMeldung, lEclMeldung);
    }

    private void setzePraesenzFelder(EclZugeordneteMeldung pEclZugeordneteMeldung, EclMeldung lEclMeldung) {

        CaBug.druckeLog("", logDrucken, 10);
        pEclZugeordneteMeldung.istPraesentNeu = lEclMeldung.meldungIstPraesent();
        pEclZugeordneteMeldung.istPraesent = lEclMeldung.meldungIstPraesent();
        pEclZugeordneteMeldung.identPersonNatJur = lEclMeldung.meldungPraesentePerson();

        if (pEclZugeordneteMeldung.istPraesent == 1) {

            praesenteVorhanden = true;
        } else {
            nichtPraesenteVorhanden = true;
        }

        int personNatJurFuerSuche = 0;
        if (pEclZugeordneteMeldung.identPersonNatJur == lEclMeldung.personenNatJurIdent) {
            personNatJurFuerSuche = -1;
        } else {
            personNatJurFuerSuche = pEclZugeordneteMeldung.identPersonNatJur;
        }

        if (lDbBundle.param.paramPortal.nurRawLiveAbstimmung != 1) {
            int rc = lDbBundle.dbZutrittskarten.readGueltigeZuMeldungPerson(lEclMeldung.meldungsIdent,
                    personNatJurFuerSuche);
            if (rc > 0) {
                pEclZugeordneteMeldung.praesenteZutrittsIdent = lDbBundle.dbZutrittskarten
                        .ergebnisPosition(0).zutrittsIdent;
                CaBug.druckeLog("praesenteZutrittsIdent=" + pEclZugeordneteMeldung.praesenteZutrittsIdent,
                        logDrucken, 3);
                pEclZugeordneteMeldung.praesenteZutrittsIdentNeben = lDbBundle.dbZutrittskarten
                        .ergebnisPosition(0).zutrittsIdentNeben;
                CaBug.druckeLog("praesenteZutrittsIdentNeben=" + pEclZugeordneteMeldung.praesenteZutrittsIdentNeben,
                        logDrucken, 3);
            }
        }
        //		zugeordneteMeldungenEigeneAktienArray[i].kartenart=blPraesenz.praesenz.kartenart;
        //		zugeordneteMeldungenEigeneAktienArray[i].kartennr=blPraesenz.praesenz.kartennr;

    }

    /**Liest alle Meldungen in zugeordneteMeldungenEigeneAktienArray, die von dieser AktienregisterIdent aus angemeldet wurden.
     * Sowohl Aktionäre als auch Gastkarten!
     * ????Füllt außerdem aktienregisterPersonNatJurIdent mit der personNatJurIdent aus den direkt aus dem Aktienregister
     * heraus erfolgten Anmeldungen???
     */
    public void leseMeldungenZuAktienregisterIdent(int pAktienregisterIdent) {

        int rc = lDbBundle.dbMeldungen.leseZuAktienregisterIdent(pAktienregisterIdent, true); //ohne Stornos
        zugeordneteMeldungenEigeneAktienArray = new EclZugeordneteMeldung[rc];
        for (int i = 0; i < rc; i++) {
            EclMeldung lMeldung = lDbBundle.dbMeldungen.meldungenArray[i];
            zugeordneteMeldungenEigeneAktienArray[i] = new EclZugeordneteMeldung();
            zugeordneteMeldungenEigeneAktienArray[i].aktienregisterIdent = pAktienregisterIdent;
            zugeordneteMeldungenEigeneAktienArray[i].artBeziehung = 1;
            zugeordneteMeldungenEigeneAktienArray[i].meldungsIdent = lMeldung.meldungsIdent;
            zugeordneteMeldungenEigeneAktienArray[i].klasse = lMeldung.klasse;
            if (lMeldung.fixAnmeldung == 1) {
                zugeordneteMeldungenEigeneAktienArray[i].fixAnmeldung = true;
            }

            fuelleAktionaersdaten(zugeordneteMeldungenEigeneAktienArray[i], true, lMeldung);

            aktienregisterPersonNatJurIdent = zugeordneteMeldungenEigeneAktienArray[i].personNatJurIdent;

            setzePraesenzFelder(zugeordneteMeldungenEigeneAktienArray[i], lMeldung);
        }

        //		int i;
        //		lDbBundle.dbWillenserklaerungZusatz.leseZuAktienregisterIdentOhneStorno(pAktienregisterIdent);
        //		zugeordneteMeldungenEigeneAktienArray=new EclZugeordneteMeldung[lDbBundle.dbWillenserklaerungZusatz.willenserklaerungArray.length];
        //		for (i=0;i<lDbBundle.dbWillenserklaerungZusatz.willenserklaerungArray.length;i++){
        //			zugeordneteMeldungenEigeneAktienArray[i]=new EclZugeordneteMeldung();
        //			zugeordneteMeldungenEigeneAktienArray[i].aktienregisterIdent=pAktienregisterIdent;
        //			zugeordneteMeldungenEigeneAktienArray[i].artBeziehung=1;
        //
        //			if (lDbBundle.dbWillenserklaerungZusatz.willenserklaerungArray[i].meldungsIdent!=0){ /*Aktionär*/
        //				
        //				
        //				/*************Geändert***********************************/
        //				if (lDbBundle.dbWillenserklaerungZusatz.willenserklaerungArray[i].anmeldungFix==1){
        //					zugeordneteMeldungenEigeneAktienArray[i].fixAnmeldung=true;
        //				}
        //				/***********Änderung ende********************************/
        //
        //				
        //				zugeordneteMeldungenEigeneAktienArray[i].meldungsIdent=lDbBundle.dbWillenserklaerungZusatz.willenserklaerungArray[i].meldungsIdent;
        //				zugeordneteMeldungenEigeneAktienArray[i].klasse=1;
        //				/*Aktienanzahl einlesen*/
        //				EclMeldung lMeldung=new EclMeldung();
        //				int erg;
        //				lMeldung.meldungsIdent=zugeordneteMeldungenEigeneAktienArray[i].meldungsIdent;
        //				erg=lDbBundle.dbMeldungen.leseZuMeldungsIdent(lMeldung);
        //
        //				
        //				fuelleAktionaersdaten(zugeordneteMeldungenEigeneAktienArray[i], true);
        //
        //				aktienregisterPersonNatJurIdent=zugeordneteMeldungenEigeneAktienArray[i].personNatJurIdent;
        //				
        //				setzePraesenzFelder(zugeordneteMeldungenEigeneAktienArray[i], 0);
        //				
        //			}
        //			else{ /*Gast*/
        //				zugeordneteMeldungenEigeneAktienArray[i].meldungsIdent=lDbBundle.dbWillenserklaerungZusatz.willenserklaerungArray[i].meldungsIdentGast;
        //				zugeordneteMeldungenEigeneAktienArray[i].klasse=0;
        //				
        //				/*Rest einlesen*/
        //				EclMeldung lMeldung=new EclMeldung();
        //				int erg;
        //				lMeldung.meldungsIdent=zugeordneteMeldungenEigeneAktienArray[i].meldungsIdent;
        //				erg=lDbBundle.dbMeldungen.leseZuMeldungsIdent(lMeldung);
        //				
        //				fuelleAktionaersdaten(zugeordneteMeldungenEigeneAktienArray[i], false);
        //
        //				setzePraesenzFelder(zugeordneteMeldungenEigeneAktienArray[i], 0);
        //
        //			}
        //	
        //		}

    }

    /**Füllt die Liste zugeordneteMeldungenEigeneAktienArray: alle Meldungen mit Aktienbesitz (keine Gäste),
     * die pPersonNatJurIdent "gehören" (auch Fremdbesitz)*/
    public void leseMeldungenEigeneAktienZuPersonNatJur(int pPersonNatJurIdent) {
        int i;

        EclMeldung lMeldung = new EclMeldung();
        lMeldung.personenNatJurIdent = pPersonNatJurIdent;
        lDbBundle.dbMeldungen.leseZuPersonenNatJurIdent(lMeldung, 1);

        zugeordneteMeldungenEigeneAktienArray = new EclZugeordneteMeldung[lDbBundle.dbMeldungen.meldungenArray.length];

        for (i = 0; i < lDbBundle.dbMeldungen.meldungenArray.length; i++) {
            zugeordneteMeldungenEigeneAktienArray[i] = new EclZugeordneteMeldung();
            zugeordneteMeldungenEigeneAktienArray[i].personNatJurIdent = pPersonNatJurIdent;
            zugeordneteMeldungenEigeneAktienArray[i].meldungsIdent = zugeordneteMeldungenEigeneAktienArray[i].meldungsIdent;
            zugeordneteMeldungenEigeneAktienArray[i].artBeziehung = 1;

            fuelleAktionaersdaten(zugeordneteMeldungenEigeneAktienArray[i], true);

            setzePraesenzFelder(zugeordneteMeldungenEigeneAktienArray[i], i);

            if (lDbBundle.dbMeldungen.meldungenArray[i].klasse == 1) {/*Aktionär*/
                zugeordneteMeldungenEigeneAktienArray[i].klasse = 1;
                zugeordneteMeldungenEigeneAktienArray[i].stueckAktien = lDbBundle.dbMeldungen.meldungenArray[i].stueckAktien;
            }
            //			else{ /*Gast*/
            //				zugeordneteMeldungenEigeneAktienArray[i].klasse=0;
            //			}

        }

    }

    /**Füllt die Liste zugeordneteMeldungenEigeneAktienArray: alle Meldungen mit Aktienbesitz (keine Gäste),
     * die pPersonNatJurIdent "gehören" (auch Fremdbesitz)*/
    public void leseMeldungenEigeneAktienZuInstiIdent(int pInstiIdent) {
        BlInsti blInsti = new BlInsti(true, lDbBundle);
        blInsti.fuelleInstiBestandsZuordnung(pInstiIdent);

        zugeordneteMeldungenEigeneAktienArray = new EclZugeordneteMeldung[blInsti.rcMeldMeldung.length];

        for (int i = 0; i < blInsti.rcMeldMeldung.length; i++) {
            zugeordneteMeldungenEigeneAktienArray[i] = new EclZugeordneteMeldung();
            zugeordneteMeldungenEigeneAktienArray[i].personNatJurIdent = 0; //pPersonNatJurIdent;
            zugeordneteMeldungenEigeneAktienArray[i].meldungsIdent = blInsti.rcMeldMeldung[i].meldungsIdent;
            zugeordneteMeldungenEigeneAktienArray[i].artBeziehung = 1;

            fuelleAktionaersdaten(zugeordneteMeldungenEigeneAktienArray[i], true, blInsti.rcMeldMeldung[i]);

            setzePraesenzFelder(zugeordneteMeldungenEigeneAktienArray[i], blInsti.rcMeldMeldung[i]);

            if (blInsti.rcMeldMeldung[i].klasse == 1) {/*Aktionär*/
                zugeordneteMeldungenEigeneAktienArray[i].klasse = 1;
                zugeordneteMeldungenEigeneAktienArray[i].stueckAktien = blInsti.rcMeldMeldung[i].stueckAktien;
            }
            //			else{ /*Gast*/
            //				zugeordneteMeldungenEigeneAktienArray[i].klasse=0;
            //			}

        }

    }

    /**Füllt die Liste zugeordneteMeldungenEigeneGastkartenArray: alle Gastkarten,
     * die pPersonNatJurIdent "gehören" (können auch Gruppengastkarten sein)*/
    public void leseMeldungenEigeneGastkartenZuPersonNatJur(int pPersonNatJurIdent) {
        int i;
        EclMeldung lMeldung = new EclMeldung();
        lMeldung.personenNatJurIdent = pPersonNatJurIdent;
        lDbBundle.dbMeldungen.leseZuPersonenNatJurIdent(lMeldung, 0);/*Liefert nur aktive Meldungen zurück*/

        zugeordneteMeldungenEigeneGastkartenArray = new EclZugeordneteMeldung[lDbBundle.dbMeldungen.meldungenArray.length];

        for (i = 0; i < lDbBundle.dbMeldungen.meldungenArray.length; i++) {
            zugeordneteMeldungenEigeneGastkartenArray[i] = new EclZugeordneteMeldung();
            zugeordneteMeldungenEigeneGastkartenArray[i].personNatJurIdent = pPersonNatJurIdent;
            zugeordneteMeldungenEigeneGastkartenArray[i].meldungsIdent = lDbBundle.dbMeldungen.meldungenArray[i].meldungsIdent;
            zugeordneteMeldungenEigeneGastkartenArray[i].artBeziehung = 2;

            fuelleAktionaersdaten(zugeordneteMeldungenEigeneGastkartenArray[i], false);

            setzePraesenzFelder(zugeordneteMeldungenEigeneGastkartenArray[i], i);

            if (lDbBundle.dbMeldungen.meldungenArray[i].klasse == 0) {/*Gast*/
                zugeordneteMeldungenEigeneGastkartenArray[i].klasse = 0;
            }

        }

    }

    /**Vollmachten einlesen - füllt die Liste zugeordneteMeldungenBevollmaechtigt: alle Meldungen,
     * für die pPersonNatJurIdent eine gültige Vollmacht besitzt*/

    /*TODO #9*/
    /*Vorgehen:
     * 1.) Alle Willenserklärungen mit Vollmachten auf pPersonNatJur einlesen
     * 2.) Daraus die zu bearbeitenden Meldungen ermitteln
     * 3.) Für jede ermittelte Meldung:
     * 		> Alle Vollmachten zu dieser Meldung einlesen (BLWillenserklaerung.einlesenVollmachtenAnDritte)
     * 		> mit pruefenObVollmachtgeberVollmachtHat prüfen, ob pPersonNatJur noch GÜLTIGE Vollmacht hat
     * 		> Falls ja: Meldung final in Liste speichern
     * 4.) Final die gefundenen Meldungen ins Array übertragen
     * 
     * 
     */
    public void leseMeldungenBevollmaechtigtZuPersonNatJur(int pPersonNatJurIdent) {
        int i, i1, gef;
        /*Willenserklärugnen einlesen, die pPersonNatJurIdent als Bevollmächtigten eingetragen haben*/
        if (pPersonNatJurIdent == 0) {/*INFO - wurde ursprünglich nicht abgeprüft, deshalb ellenlange Antwortzeiten!*/
            zugeordneteMeldungenBevollmaechtigtArray = new EclZugeordneteMeldung[0];
            return;
        }
        lDbBundle.dbWillenserklaerung.leseZuBevollmaechtigten(pPersonNatJurIdent);
        if (lDbBundle.dbWillenserklaerung.willenserklaerungArray.length == 0) {/*Keinerlei Bevollmächtigung gefunden*/
            zugeordneteMeldungenBevollmaechtigtArray = new EclZugeordneteMeldung[0];
            return;
        }

        /*Alle gefundenen Willenserklärungen durcharbeiten und die gefundenen Meldungen (aber jede Meldung nur einmal) in
         * lMeldungenList aufnehmen. Enthält auch Meldungen, für die die Vollmacht später wieder storniert wurde!
         */
        List<Integer> lMeldungenList = new LinkedList<>();

        for (i = 0; i < lDbBundle.dbWillenserklaerung.willenserklaerungArray.length; i++) {

            int hMeldung = 0;
            hMeldung = lDbBundle.dbWillenserklaerung.willenserklaerungArray[i].meldungsIdent;

            /*Überprüfen, ob Meldung schon in Array*/
            gef = -1;
            for (i1 = 0; i1 < lMeldungenList.size(); i1++) {
                if (lMeldungenList.get(i1) == hMeldung) {
                    gef = i1;
                }
            }
            if (gef == -1) {/*Dann lMeldung noch nicht in bisheriger Liste drin*/
                Integer oMeldung = hMeldung;
                lMeldungenList.add(oMeldung);
            }
        }

        /*Alle gefundenen Meldungen durcharbeiten und prüfen, ob die Vollmacht noch gültig ist.
         * Alle Meldungen mit noch gültiger Vollmacht werden in lMeldungenListGueltig übertragen*/
        List<Integer> lMeldungenListGueltig = new LinkedList<>();
        for (i = 0; i < lMeldungenList.size(); i++) {
            /*Alle Vollmachten zur gefundenen Meldung einlesen und prüfen, ob PersonNatJur noch gültige Vollmacht hat.*/
            BlWillenserklaerung lWillenserklaerung = new BlWillenserklaerung();
            lWillenserklaerung.setzeDbBundle(lDbBundle);
            lWillenserklaerung.piMeldungsIdentAktionaer = lMeldungenList.get(i);

            lWillenserklaerung.einlesenVollmachtenAnDritte();
            lWillenserklaerung.rcIstZulaessig = true;
            lWillenserklaerung.pWillenserklaerungGeberIdent = pPersonNatJurIdent;
            lWillenserklaerung.pruefenObVollmachtgeberVollmachtHat();
            if (lWillenserklaerung.rcIstZulaessig) {/*Vollmacht ist vorhanden und noch gültig*/
                lMeldungenListGueltig.add(lMeldungenList.get(i));
            }

        }

        /*Nun final in zugeordneteMeldungenBevollmaechtigt übertragen*/
        zugeordneteMeldungenBevollmaechtigtArray = new EclZugeordneteMeldung[lMeldungenListGueltig.size()];
        for (i = 0; i < lMeldungenListGueltig.size(); i++) {

            zugeordneteMeldungenBevollmaechtigtArray[i] = new EclZugeordneteMeldung();
            zugeordneteMeldungenBevollmaechtigtArray[i].personNatJurIdent = pPersonNatJurIdent;
            zugeordneteMeldungenBevollmaechtigtArray[i].meldungsIdent = lMeldungenListGueltig.get(i);
            zugeordneteMeldungenBevollmaechtigtArray[i].artBeziehung = 3;

            zugeordneteMeldungenBevollmaechtigtArray[i].klasse = 1;
            EclMeldung lMeldung = new EclMeldung();
            lMeldung.meldungsIdent = lMeldungenListGueltig.get(i);
            lDbBundle.dbMeldungen.leseZuMeldungsIdent(lMeldung);

            fuelleAktionaersdaten(zugeordneteMeldungenBevollmaechtigtArray[i], true);

            setzePraesenzFelder(zugeordneteMeldungenBevollmaechtigtArray[i], 0);

        }

    }

    /**Parameter pPersonNatJurIdent:
     * = -2 => es wird nicht selektiert, es werden wirklich alle Willenserklärungen (so noch gültig) angegeben.
     * = >0 => für die "eigenen Meldungen" werden alle Willenserklärungen angezeigt, die vom Aktionär ausgehen (geber == -1);
     * 			für die Bevollmächtigten werden die Willenserklärungen angezeigt, die auf den Bevollmächtigten verweisen (nicht änderbar!)
     * 			und die dieser gegeben hat.
     * @param pPersonNatJurIdent
     */
    @SuppressWarnings("incomplete-switch")
    public void ergaenzeZugeordneteMeldungenUmWillenserklaerungen(int pPersonNatJurIdent) {
        int i, i1;

        /***************zugeordneteMeldungenEigeneAktienArray übertragen***********************/
        if (zugeordneteMeldungenEigeneAktienArray != null) {
            for (i = 0; i < zugeordneteMeldungenEigeneAktienArray.length; i++) {
                leseWillenserklaerungenZuMeldung(zugeordneteMeldungenEigeneAktienArray[i].meldungsIdent);
                zugeordneteMeldungenEigeneAktienArray[i].zugeordneteWillenserklaerungenList = new LinkedList<>();
                zugeordneteMeldungenEigeneAktienArray[i].anzZutrittsIdentSelbst = 0;
                zugeordneteMeldungenEigeneAktienArray[i].anzZutrittsIdentVollmacht = 0;
                zugeordneteMeldungenEigeneAktienArray[i].anzVollmachtenDritte = 0;
                zugeordneteMeldungenEigeneAktienArray[i].anzKIAVSRV = 0;
                zugeordneteMeldungenEigeneAktienArray[i].anzSRV = 0;
                zugeordneteMeldungenEigeneAktienArray[i].anzKIAV = 0;
                zugeordneteMeldungenEigeneAktienArray[i].anzBriefwahl = 0;

                for (i1 = 0; i1 < zugeordneteWillenserklaerungenList.size(); i1++) {
                    if (pPersonNatJurIdent == -2 /*Dann alle Willenserklärungen übertragen*/
                            || zugeordneteWillenserklaerungenList.get(
                                    i1).willenserklaerungGeberIdent == -1 /*Willenserklärungen, die vom Aktionär ausgehen*/
                    ) { /*Willenserklärung übertragen*/
                        zugeordneteMeldungenEigeneAktienArray[i].zugeordneteWillenserklaerungenList
                                .add(zugeordneteWillenserklaerungenList.get(i1));
                        switch (zugeordneteWillenserklaerungenList.get(i1).willenserklaerung) {
                        case KonstWillenserklaerung.neueZutrittsIdentZuMeldung: {
                            zugeordneteMeldungenEigeneAktienArray[i].anzZutrittsIdentSelbst++;
                            zugeordneteWillenserklaerungenList.get(i1).detailAnzeigeIstZulaessig = true;
                            break;
                        }

                        case KonstWillenserklaerung.neueZutrittsIdentZuMeldung_VollmachtAnDritte: {
                            zugeordneteMeldungenEigeneAktienArray[i].anzZutrittsIdentVollmacht++;
                            zugeordneteWillenserklaerungenList.get(i1).detailAnzeigeIstZulaessig = true;
                            break;
                        }

                        case KonstWillenserklaerung.vollmachtAnDritte: {
                            zugeordneteMeldungenEigeneAktienArray[i].anzVollmachtenDritte++;
                            break;
                        }

                        case KonstWillenserklaerung.vollmachtUndWeisungAnSRV: {
                            zugeordneteMeldungenEigeneAktienArray[i].anzKIAVSRV++;
                            zugeordneteMeldungenEigeneAktienArray[i].anzSRV++;
                            break;
                        }
                        case KonstWillenserklaerung.vollmachtUndWeisungAnKIAV: {
                            zugeordneteMeldungenEigeneAktienArray[i].anzKIAVSRV++;
                            zugeordneteMeldungenEigeneAktienArray[i].anzKIAV++;
                            break;
                        }
                        case KonstWillenserklaerung.briefwahl: {
                            zugeordneteMeldungenEigeneAktienArray[i].anzKIAVSRV++;
                            zugeordneteMeldungenEigeneAktienArray[i].anzBriefwahl++;
                            break;
                        }
                        }
                    }
                }

                if (zugeordneteMeldungenEigeneAktienArray[i].zugeordneteWillenserklaerungenList.size() > 0) {
                    zugeordneteMeldungenEigeneAktienArray[i].willenserklaerungenVorhanden = true;
                } else {
                    zugeordneteMeldungenEigeneAktienArray[i].willenserklaerungenVorhanden = false;
                }

                /********Nachträglich eingefügt; macht obige Zählung zunichte - ist aber die, die tatsächlic gebraucht wird************/
                zugeordneteMeldungenEigeneAktienArray[i].anzZutrittsIdentSelbst = lAnzZutrittsIdentSelbst;
                zugeordneteMeldungenEigeneAktienArray[i].anzZutrittsIdentVollmacht = lAnzZutrittsIdentVollmacht;
                zugeordneteMeldungenEigeneAktienArray[i].anzVollmachtenDritte = lAnzVollmachtenDritte;
                zugeordneteMeldungenEigeneAktienArray[i].anzKIAVSRV = lAnzKIAVSRV;
                zugeordneteMeldungenEigeneAktienArray[i].anzSRV = lAnzSRV;
                zugeordneteMeldungenEigeneAktienArray[i].anzKIAV = lAnzKIAV;
                zugeordneteMeldungenEigeneAktienArray[i].anzBriefwahl = lAnzBriefwahl;
                if (lAnzBriefwahl > 0) {
                    this.briefwahlVorhanden = true;
                }
                if (lAnzSRV > 0) {
                    this.srvVorhanden = true;
                }
                zugeordneteMeldungenEigeneAktienArray[i].zweiEKMoeglich = false;
                EclMeldung lMeldung = new EclMeldung();
                lMeldung.meldungsIdent = zugeordneteMeldungenEigeneAktienArray[i].meldungsIdent;
                int anz = lDbBundle.dbWillenserklaerung.ermittleHoechsteWillenserklaerungIdentZuMeldung(lMeldung);
                zugeordneteMeldungenEigeneAktienArray[i].identHoechsteWillenserklaerung = anz;
                //				System.out.println("alt: i="+i+" meldungsident="+lMeldung.meldungsIdent+" anz="+anz);

                //				System.out.println("blwillenserklaerung anzZutrittsIdentSelbst="+zugeordneteMeldungenEigeneAktienArray[i].anzZutrittsIdentSelbst);
                //				System.out.println("blwillenserklaerung anzZutrittsIdentVollmacht="+zugeordneteMeldungenEigeneAktienArray[i].anzZutrittsIdentVollmacht);
                //				System.out.println("blwillenserklaerung anzVollmachtenDritte="+zugeordneteMeldungenEigeneAktienArray[i].anzVollmachtenDritte);
                //				System.out.println("blwillenserklaerung anzKIAVSRV="+zugeordneteMeldungenEigeneAktienArray[i].anzKIAVSRV);

            }
            gastKartenGemeldetEigeneAktien = lGastKartenGemeldetEigeneAktien;
            if (zugeordneteMeldungenEigeneAktienArray.length == 1) {
                if (zugeordneteMeldungenEigeneAktienArray[0].anzZutrittsIdentSelbst == 0
                        && zugeordneteMeldungenEigeneAktienArray[0].anzZutrittsIdentVollmacht == 0
                        && zugeordneteMeldungenEigeneAktienArray[0].anzVollmachtenDritte == 0
                        && zugeordneteMeldungenEigeneAktienArray[0].anzKIAVSRV == 0
                        && zugeordneteMeldungenEigeneAktienArray[0].fixAnmeldung == false) {
                    zugeordneteMeldungenEigeneAktienArray[0].zweiEKMoeglich = true;
                } else {
                    zugeordneteMeldungenEigeneAktienArray[0].zweiEKMoeglich = false;
                }
            }

        }

        /***************zugeordneteMeldungenEigeneGastkartenArray übertragen***********************/
        if (zugeordneteMeldungenEigeneGastkartenArray != null) {
            for (i = 0; i < zugeordneteMeldungenEigeneGastkartenArray.length; i++) {
                leseWillenserklaerungenZuMeldung(zugeordneteMeldungenEigeneGastkartenArray[i].meldungsIdent);
                zugeordneteMeldungenEigeneGastkartenArray[i].zugeordneteWillenserklaerungenList = new LinkedList<>();
                zugeordneteMeldungenEigeneGastkartenArray[i].anzZutrittsIdentSelbst = 0;
                zugeordneteMeldungenEigeneGastkartenArray[i].anzZutrittsIdentVollmacht = 0;
                zugeordneteMeldungenEigeneGastkartenArray[i].anzVollmachtenDritte = 0;
                zugeordneteMeldungenEigeneGastkartenArray[i].anzKIAVSRV = 0;
                zugeordneteMeldungenEigeneGastkartenArray[i].anzSRV = 0;
                zugeordneteMeldungenEigeneGastkartenArray[i].anzKIAV = 0;

                for (i1 = 0; i1 < zugeordneteWillenserklaerungenList.size(); i1++) {
                    /*Hier darf / braucht nicht selektiert zu werden, wer sie gegeben hat - kann nur
                     * Anmeldung / Eintrittskartenausstellung sein. Dürfen nie verändert werden, nur Detailsicht*/
                    /*Willenserklärung übertragen*/

                    /*Aber: Änderungs-Zulässigkeit etc. korrigieren!*/
                    zugeordneteWillenserklaerungenList.get(i1).aendernIstZulaessig = false;
                    zugeordneteWillenserklaerungenList.get(i1).stornierenIstZulaessig = false;
                    zugeordneteWillenserklaerungenList.get(i1).detailAnzeigeIstZulaessig = true;

                    zugeordneteMeldungenEigeneGastkartenArray[i].zugeordneteWillenserklaerungenList
                            .add(zugeordneteWillenserklaerungenList.get(i1));
                    switch (zugeordneteWillenserklaerungenList.get(i1).willenserklaerung) {
                    case KonstWillenserklaerung.neueZutrittsIdentZuMeldung: {
                        zugeordneteMeldungenEigeneGastkartenArray[i].anzZutrittsIdentSelbst++;
                        break;
                    }

                    case KonstWillenserklaerung.neueZutrittsIdentZuMeldung_VollmachtAnDritte: {
                        zugeordneteMeldungenEigeneGastkartenArray[i].anzZutrittsIdentVollmacht++;
                        break;
                    }

                    case KonstWillenserklaerung.vollmachtAnDritte: {
                        zugeordneteMeldungenEigeneGastkartenArray[i].anzVollmachtenDritte++;
                        break;
                    }

                    case KonstWillenserklaerung.vollmachtUndWeisungAnSRV: {
                        zugeordneteMeldungenEigeneGastkartenArray[i].anzSRV++;
                        zugeordneteMeldungenEigeneGastkartenArray[i].anzKIAVSRV++;
                        break;
                    }
                    case KonstWillenserklaerung.vollmachtUndWeisungAnKIAV: {
                        zugeordneteMeldungenEigeneGastkartenArray[i].anzKIAV++;
                        zugeordneteMeldungenEigeneGastkartenArray[i].anzKIAVSRV++;
                        break;
                    }
                    case KonstWillenserklaerung.briefwahl: {
                        zugeordneteMeldungenEigeneGastkartenArray[i].anzBriefwahl++;
                        zugeordneteMeldungenEigeneGastkartenArray[i].anzKIAVSRV++;
                        break;
                    }
                    }
                }

                if (zugeordneteMeldungenEigeneGastkartenArray[i].zugeordneteWillenserklaerungenList.size() > 0) {
                    zugeordneteMeldungenEigeneGastkartenArray[i].willenserklaerungenVorhanden = true;
                } else {
                    zugeordneteMeldungenEigeneGastkartenArray[i].willenserklaerungenVorhanden = false;
                }

                /********Nachträglich eingefügt; macht obige Zählung zunichte - ist aber die, die tatsächlic gebraucht wird************/
                /*TODO #9*/
                /*
                zugeordneteMeldungenEigeneGastkartenArray[i].anzZutrittsIdentSelbst=lAnzZutrittsIdentSelbst;
                zugeordneteMeldungenEigeneGastkartenArray[i].anzZutrittsIdentVollmacht=lAnzZutrittsIdentVollmacht;
                zugeordneteMeldungenEigeneGastkartenArray[i].anzVollmachtenDritte=lAnzVollmachtenDritte;
                zugeordneteMeldungenEigeneGastkartenArray[i].anzKIAVSRV=lAnzKIAVSRV;
                Hinweis: die App erwartet derzeit, dass anzZutrittsIdentSelbst auch bei Gastkarten gefüllt sind.
                Deshalb funktioniert es nicht, wenn dieser (jetzt) auskommentierte Bereich aktiviert wird,
                da die Subroutine anzZutrittsIdentSelbst bei Gastkarten nicht füllt! Muß noch mal generell untersucht werden.
                				*/
                zugeordneteMeldungenEigeneGastkartenArray[i].zweiEKMoeglich = false;
                EclMeldung lMeldung = new EclMeldung();
                lMeldung.meldungsIdent = zugeordneteMeldungenEigeneGastkartenArray[i].meldungsIdent;
                int anz = lDbBundle.dbWillenserklaerung.ermittleHoechsteWillenserklaerungIdentZuMeldung(lMeldung);
                zugeordneteMeldungenEigeneGastkartenArray[i].identHoechsteWillenserklaerung = anz;

            }
        }

        /***************zugeordneteMeldungenBevollmaechtigtArray übertragen***********************/
        if (zugeordneteMeldungenBevollmaechtigtArray != null) {
            for (i = 0; i < zugeordneteMeldungenBevollmaechtigtArray.length; i++) {
                leseWillenserklaerungenZuMeldung(zugeordneteMeldungenBevollmaechtigtArray[i].meldungsIdent);
                zugeordneteMeldungenBevollmaechtigtArray[i].zugeordneteWillenserklaerungenList = new LinkedList<>();
                zugeordneteMeldungenBevollmaechtigtArray[i].anzZutrittsIdentSelbst = 0;
                zugeordneteMeldungenBevollmaechtigtArray[i].anzZutrittsIdentVollmacht = 0;
                zugeordneteMeldungenBevollmaechtigtArray[i].anzVollmachtenDritte = 0;
                zugeordneteMeldungenBevollmaechtigtArray[i].anzKIAVSRV = 0;
                zugeordneteMeldungenBevollmaechtigtArray[i].anzSRV = 0;
                zugeordneteMeldungenBevollmaechtigtArray[i].anzKIAV = 0;
                zugeordneteMeldungenBevollmaechtigtArray[i].anzBriefwahl = 0;

                for (i1 = 0; i1 < zugeordneteWillenserklaerungenList.size(); i1++) {
                    if (pPersonNatJurIdent == -2 /*Dann alle Willenserklärungen übertragen*/
                            || zugeordneteWillenserklaerungenList.get(
                                    i1).willenserklaerungGeberIdent == pPersonNatJurIdent /*Willenserklärungen, die selbst gegeben wurde*/
                            || zugeordneteWillenserklaerungenList.get(
                                    i1).bevollmaechtigterDritterIdent == pPersonNatJurIdent /*Vollmacht auf diese Person*/
                    ) { /*Willenserklärung übertragen*/

                        /*Ggf. Änderungs-Zulässigkeit etc. korrigieren!*/
                        if (zugeordneteWillenserklaerungenList
                                .get(i1).willenserklaerungGeberIdent == pPersonNatJurIdent) {
                            /*Willenserklärungen, die selbst gegeben wurde*/
                            zugeordneteWillenserklaerungenList.get(i1).detailAnzeigeIstZulaessig = false;
                        }
                        if (zugeordneteWillenserklaerungenList
                                .get(i1).bevollmaechtigterDritterIdent == pPersonNatJurIdent) {
                            /*Vollmacht auf diese Person*/
                            zugeordneteWillenserklaerungenList.get(i1).aendernIstZulaessig = false;
                            zugeordneteWillenserklaerungenList.get(i1).stornierenIstZulaessig = false;

                            zugeordneteWillenserklaerungenList
                                    .get(i1).detailAnzeigeIstZulaessig = true; /*Siehe unten!!!*/
                        }

                        zugeordneteMeldungenBevollmaechtigtArray[i].zugeordneteWillenserklaerungenList
                                .add(zugeordneteWillenserklaerungenList.get(i1));
                        switch (zugeordneteWillenserklaerungenList.get(i1).willenserklaerung) {
                        case KonstWillenserklaerung.neueZutrittsIdentZuMeldung: {
                            zugeordneteMeldungenBevollmaechtigtArray[i].anzZutrittsIdentSelbst++;
                            break;
                        }

                        case KonstWillenserklaerung.neueZutrittsIdentZuMeldung_VollmachtAnDritte: {
                            zugeordneteMeldungenBevollmaechtigtArray[i].anzZutrittsIdentVollmacht++;
                            break;
                        }

                        case KonstWillenserklaerung.vollmachtAnDritte: {
                            zugeordneteMeldungenBevollmaechtigtArray[i].anzVollmachtenDritte++;
                            zugeordneteWillenserklaerungenList
                                    .get(i1).detailAnzeigeIstZulaessig = false; /**Siehe oben!!! */
                            break;
                        }

                        case KonstWillenserklaerung.vollmachtUndWeisungAnSRV: {
                            zugeordneteMeldungenBevollmaechtigtArray[i].anzKIAVSRV++;
                            zugeordneteMeldungenBevollmaechtigtArray[i].anzSRV++;
                            break;
                        }
                        case KonstWillenserklaerung.vollmachtUndWeisungAnKIAV: {
                            zugeordneteMeldungenBevollmaechtigtArray[i].anzKIAVSRV++;
                            zugeordneteMeldungenBevollmaechtigtArray[i].anzKIAV++;
                            break;
                        }
                        case KonstWillenserklaerung.briefwahl: {
                            zugeordneteMeldungenBevollmaechtigtArray[i].anzKIAVSRV++;
                            zugeordneteMeldungenBevollmaechtigtArray[i].anzBriefwahl++;
                            break;
                        }
                        }
                    }
                }

                if (zugeordneteMeldungenBevollmaechtigtArray[i].zugeordneteWillenserklaerungenList.size() > 0) {
                    zugeordneteMeldungenBevollmaechtigtArray[i].willenserklaerungenVorhanden = true;
                } else {
                    zugeordneteMeldungenBevollmaechtigtArray[i].willenserklaerungenVorhanden = false;
                }

                /********Nachträglich eingefügt; macht obige Zählung zunichte - ist aber die, die tatsächlich gebraucht wird************/
                zugeordneteMeldungenBevollmaechtigtArray[i].anzZutrittsIdentSelbst = lAnzZutrittsIdentSelbst;
                zugeordneteMeldungenBevollmaechtigtArray[i].anzZutrittsIdentVollmacht = lAnzZutrittsIdentVollmacht;
                zugeordneteMeldungenBevollmaechtigtArray[i].anzVollmachtenDritte = lAnzVollmachtenDritte;
                zugeordneteMeldungenBevollmaechtigtArray[i].anzKIAVSRV = lAnzKIAVSRV;
                zugeordneteMeldungenBevollmaechtigtArray[i].anzKIAV = lAnzKIAV;
                zugeordneteMeldungenBevollmaechtigtArray[i].anzSRV = lAnzSRV;
                zugeordneteMeldungenBevollmaechtigtArray[i].anzBriefwahl = lAnzBriefwahl;
                if (lAnzBriefwahl > 0) {
                    this.briefwahlVorhanden = true;
                }
                if (lAnzSRV > 0) {
                    this.srvVorhanden = true;
                }
                zugeordneteMeldungenBevollmaechtigtArray[i].zweiEKMoeglich = false;
                EclMeldung lMeldung = new EclMeldung();
                lMeldung.meldungsIdent = zugeordneteMeldungenBevollmaechtigtArray[i].meldungsIdent;
                int anz = lDbBundle.dbWillenserklaerung.ermittleHoechsteWillenserklaerungIdentZuMeldung(lMeldung);
                zugeordneteMeldungenBevollmaechtigtArray[i].identHoechsteWillenserklaerung = anz;

            }
        }

    }

    private List<EclWillenserklaerungStatus> zugeordneteWillenserklaerungenList = new LinkedList<>();
    private int lAnzZutrittsIdentSelbst = 0;
    private int lAnzZutrittsIdentVollmacht = 0;
    private int lAnzVollmachtenDritte = 0;
    /**Summe aus KIAV, SRV, Briefwahl*/
    private int lAnzKIAVSRV = 0;

    private int lAnzSRV = 0;
    private int lAnzKIAV = 0;
    private int lAnzBriefwahl = 0;
    private int lGastKartenGemeldetEigeneAktien = 0;
    private int lIdentHoechsteWillenserklaerung = 0;

    private EclWillenserklaerungStatus prepareWillenserklaerungStatus(EclWillenserklaerung pWillenserklaerung,
            EclWillenserklaerungZusatz pWillenserklaerungZusatz) {
        EclWillenserklaerungStatus zugeordneteWillenserklaerung = new EclWillenserklaerungStatus();
        zugeordneteWillenserklaerung.willenserklaerungIdent = pWillenserklaerung.willenserklaerungIdent;
        zugeordneteWillenserklaerung.willenserklaerung = pWillenserklaerung.willenserklaerung;
        zugeordneteWillenserklaerung.willenserklaerungGeberIdent = pWillenserklaerung.willenserklaerungGeberIdent;

        if (piRueckgabeKurzOderLang == 2) {
            zugeordneteWillenserklaerung.eclWillenserklaerung = pWillenserklaerung;
            zugeordneteWillenserklaerung.eclWillenserklaerungZusatz = pWillenserklaerungZusatz;
        }
        return zugeordneteWillenserklaerung;
    }

    /**Liest alle Willenserklärungen laut Datei ein - dabei werden Stornierte entsprechend gekennzeichnet und 
     * "High-Level-Willenserklärungskombinationen" zusammengefaßt, z.B.:
     * 	> Eintrittskarte + gleichzeitige Vollmacht
     * 
     * Hinweis: 
     * > es werden ALLE Willenserklärungen zu der angegebenen Meldung eingelesen. und in 
     * 		zugeordneteWillenserklaerungenList abgelegt. 
     * > Eingrenzung auf bestimmte Vollmachten 
     * 		(z.B. auf einen Willenserklärungsgeber, oder auf einen bestimmten Bevollmächtigten)
     * 		müssen dann beim Umkopieren in die endgültige Liste erfolgen. 
     * 
     * @param pMeldungsIdent
     */
    public void leseWillenserklaerungenZuMeldung(int pMeldungsIdent) {

        int i;
        EclWillenserklaerung willenserklaerung = null;
        EclWillenserklaerungZusatz willenserklaerungZusatz = null;
        EclWillenserklaerungStatus zugeordneteWillenserklaerung = null;

        zugeordneteWillenserklaerungenList = new LinkedList<>();
        lAnzZutrittsIdentSelbst = 0;
        lAnzZutrittsIdentVollmacht = 0;
        lAnzVollmachtenDritte = 0;
        lAnzKIAVSRV = 0;
        lAnzSRV = 0;
        lAnzKIAV = 0;
        lAnzBriefwahl = 0;
        lIdentHoechsteWillenserklaerung = 0;

        /*Die EclMeldung pMeldungsIdent einlesen, für die die Willenserklärungen gelesen werden sollen*/
        EclMeldung lMeldung = new EclMeldung();
        lMeldung.meldungsIdent = pMeldungsIdent;
        lDbBundle.dbMeldungen.leseZuMeldungsIdent(lMeldung);
        lMeldung = lDbBundle.dbMeldungen.meldungenArray[0];

        /*Willenserklärungen zur pMeldungsIdent einlesen*/
        if (lMeldung.klasse == 1) {
            lDbBundle.dbWillenserklaerung.leseZuMeldung(lMeldung);
        } else {
            lDbBundle.dbWillenserklaerung.leseZuMeldungGast(lMeldung);
        }

        /*Schleife für alle Willenserklärungen*/
        for (i = 0; i < lDbBundle.dbWillenserklaerung.anzWillenserklaerungGefunden(); i++) {
            willenserklaerung = lDbBundle.dbWillenserklaerung.willenserklaerungGefunden(i);

            /*Ist Weisung über Internet erteilt worden?*/
            if (willenserklaerung.erteiltAufWeg < 21 || willenserklaerung.erteiltAufWeg > 29) {
                this.rcHatNichtNurPortalWillenserklaerungen = true;
            }
            /*Neueste Willenserklärung?*/
            if (this.rcDatumLetzteWillenserklaerung.compareTo(willenserklaerung.veraenderungszeit) < 0) {
                this.rcDatumLetzteWillenserklaerung = willenserklaerung.veraenderungszeit;
            }

            if (piAnsichtVerarbeitungOderAnalyse == 2) { /**Dann willenserklärungZusatz IMMER einlesen
                                                         (bei Verarbeitung  wird diese nur eingelesen, wenn sie gebraucht wird.*/
                lDbBundle.dbWillenserklaerungZusatz.leseZuIdent(willenserklaerung.willenserklaerungIdent);
                willenserklaerungZusatz = lDbBundle.dbWillenserklaerungZusatz.willenserklaerungGefunden(0);

            }
            if (willenserklaerung.willenserklaerungIdent > lIdentHoechsteWillenserklaerung) { /*Höchste vergebene WillenserklärungIdent festhalten*/
                lIdentHoechsteWillenserklaerung = willenserklaerung.willenserklaerungIdent;
            }
            switch (willenserklaerung.willenserklaerung) {

            case KonstWillenserklaerung.neueZutrittsIdentZuMeldung:
            case KonstWillenserklaerung.zuZutrittsIdentNeuesDokument: {
                int i1;
                int veraenderteWK = 0;
                if (piAnsichtVerarbeitungOderAnalyse == 1) { /*Bei Analyse-Ansicht bereits eingelesen!*/
                    lDbBundle.dbWillenserklaerungZusatz.leseZuIdent(willenserklaerung.willenserklaerungIdent);
                    willenserklaerungZusatz = lDbBundle.dbWillenserklaerungZusatz.willenserklaerungGefunden(0);
                }

                if (willenserklaerung.willenserklaerung == KonstWillenserklaerung.neueZutrittsIdentZuMeldung
                        || piAnsichtVerarbeitungOderAnalyse == 2) {
                    /*Neue Willenserklärung erzeugen*/
                    zugeordneteWillenserklaerung = prepareWillenserklaerungStatus(willenserklaerung,
                            willenserklaerungZusatz);
                }
                if (willenserklaerung.willenserklaerung == KonstWillenserklaerung.zuZutrittsIdentNeuesDokument) {
                    for (i1 = 0; i1 < zugeordneteWillenserklaerungenList.size(); i1++) {
                        if (BlZutrittsIdent.compare(zugeordneteWillenserklaerungenList.get(i1).zutrittsIdent,
                                zugeordneteWillenserklaerungenList.get(i1).zutrittsIdentNeben,
                                willenserklaerung.zutrittsIdent, willenserklaerung.zutrittsIdentNeben) == 0
                        //zugeordneteWillenserklaerungenList.get(i1).zutrittsIdent.compareTo(willenserklaerung.zutrittsIdent)==0
                        ) {
                            veraenderteWK = i1;
                        }
                    }
                    if (veraenderteWK != -1) {
                        if (piAnsichtVerarbeitungOderAnalyse == 1) { /*Dann bestehende Willenserkläerung von der ursprünglichen Ausstellung ergänzen / überschreiben*/
                            zugeordneteWillenserklaerung = zugeordneteWillenserklaerungenList.get(veraenderteWK);
                        }
                        zugeordneteWillenserklaerung.textListe = new LinkedList<>();
                        zugeordneteWillenserklaerung.textListeEN = new LinkedList<>();
                    } else {
                        CaBug.drucke("BlWillenserklaerungStatus.leseWillenserklaerungenZuMeldung - 001");
                    }
                }

                if (piRueckgabeKurzOderLang == 2) { /*In diesem Fall nochmal separat füllen für den Fall, dass piAnsichtVerarbeitungOderAnalyse==1 und piRueckgabeKurzOderLang==2.
                                                    Allerdings beachten: da wird das dann nicht so korrekt, da ursprünglicher Verweis auf Willenserklärung überschrieben wird*/
                    zugeordneteWillenserklaerung.eclWillenserklaerung = willenserklaerung;
                    zugeordneteWillenserklaerung.eclWillenserklaerungZusatz = willenserklaerungZusatz;
                }

                zugeordneteWillenserklaerung.willenserklaerungIdent = willenserklaerung.willenserklaerungIdent;
                zugeordneteWillenserklaerung.willenserklaerung = willenserklaerung.willenserklaerung;
                zugeordneteWillenserklaerung.willenserklaerungGeberIdent = willenserklaerung.willenserklaerungGeberIdent;
                String hString = willenserklaerung.zutrittsIdent;
                if (willenserklaerung.zutrittsIdentNeben.compareTo("00") != 0) {
                    hString = hString + "-" + willenserklaerung.zutrittsIdentNeben;
                }
                if (lMeldung.klasse == 1) {
                    zugeordneteWillenserklaerung.textListe.add(bezeichnungEintrittskarteDE + hString);
                    zugeordneteWillenserklaerung.textListeEN.add(bezeichnungEintrittskarteEN + hString);
                } else {
                    zugeordneteWillenserklaerung.textListe.add("Gastkarte Nr. " + hString);
                    zugeordneteWillenserklaerung.textListeEN.add("Guest ticket. " + hString);
                }
                if (lMeldung.titel.isEmpty()) {
                    zugeordneteWillenserklaerung.textListe.add(lMeldung.name + ", " + lMeldung.vorname);
                    zugeordneteWillenserklaerung.textListeEN.add(lMeldung.name + ", " + lMeldung.vorname);
                }
                else {
                    zugeordneteWillenserklaerung.textListe.add(lMeldung.name + ", " + lMeldung.titel+" "+lMeldung.vorname);
                    zugeordneteWillenserklaerung.textListeEN.add(lMeldung.name + ", " + lMeldung.titel+" "+lMeldung.vorname);
                }

                zugeordneteWillenserklaerung.textListe.add(lMeldung.ort);
                zugeordneteWillenserklaerung.textListeEN.add(lMeldung.ort);

                zugeordneteWillenserklaerung.versandart = willenserklaerungZusatz.versandartEK;
                zugeordneteWillenserklaerung.zutrittsIdent = willenserklaerung.zutrittsIdent;
                zugeordneteWillenserklaerung.zutrittsIdentNeben = willenserklaerung.zutrittsIdentNeben;

                /*Je nach Versandart: Daten in textListe ergänzen*/
                switch (zugeordneteWillenserklaerung.versandart) {
                case 1:
                case 2:
                case 6:/*Postversand*/ {
                    if (!willenserklaerungZusatz.versandadresse1.isEmpty()) {
                        zugeordneteWillenserklaerung.textListe.add("Versandadresse:");
                        zugeordneteWillenserklaerung.textListeEN.add("Postal adress:");
                        zugeordneteWillenserklaerung.textListe.add(willenserklaerungZusatz.versandadresse1);
                        zugeordneteWillenserklaerung.textListeEN.add(willenserklaerungZusatz.versandadresse1);
                    }
                    if (!willenserklaerungZusatz.versandadresse2.isEmpty()) {
                        zugeordneteWillenserklaerung.textListe.add(willenserklaerungZusatz.versandadresse2);
                        zugeordneteWillenserklaerung.textListeEN.add(willenserklaerungZusatz.versandadresse2);
                    }
                    if (!willenserklaerungZusatz.versandadresse3.isEmpty()) {
                        zugeordneteWillenserklaerung.textListe.add(willenserklaerungZusatz.versandadresse3);
                        zugeordneteWillenserklaerung.textListeEN.add(willenserklaerungZusatz.versandadresse3);
                    }
                    if (!willenserklaerungZusatz.versandadresse4.isEmpty()) {
                        zugeordneteWillenserklaerung.textListe.add(willenserklaerungZusatz.versandadresse4);
                        zugeordneteWillenserklaerung.textListeEN.add(willenserklaerungZusatz.versandadresse4);
                    }
                    if (!willenserklaerungZusatz.versandadresse5.isEmpty()) {
                        zugeordneteWillenserklaerung.textListe.add(willenserklaerungZusatz.versandadresse5);
                        zugeordneteWillenserklaerung.textListeEN.add(willenserklaerungZusatz.versandadresse5);
                    }
                    break;
                }
                case 3: {
                    int angefuegt = 0;
                    String hTextD = "", hTextE = "";
                    /**TODO _Konsolidierung: Anzeige Willenserklärung Online-Ticket oder App - funktioniert so nicht!*/
                    if ((lDbBundle.param.paramPortalServer.statusOnlineTicket & 1) == 1) {
                        hTextD = "Online-Ausdruck";
                        hTextE = "Online document";
                        angefuegt++;
                    }
                    if ((lDbBundle.param.paramPortalServer.statusOnlineTicket & 2) == 2) {
                        if (angefuegt > 0) {
                            hTextD += "/";
                            hTextE += "/";
                        }
                        hTextD += "App";
                        hTextE += "App";
                    }
                    zugeordneteWillenserklaerung.textListe.add(hTextD);
                    zugeordneteWillenserklaerung.textListeEN.add(hTextE);
                    break;
                }
                case 4: {
                    zugeordneteWillenserklaerung.textListe.add("Versand per E-Mail:");
                    zugeordneteWillenserklaerung.textListeEN.add("Send by E-Mail:");
                    zugeordneteWillenserklaerung.textListe.add(willenserklaerungZusatz.emailAdresseEK);
                    zugeordneteWillenserklaerung.textListeEN.add(willenserklaerungZusatz.emailAdresseEK);
                    break;
                }
                case 5: {
                    zugeordneteWillenserklaerung.textListe.add("In App gespeichert");
                    zugeordneteWillenserklaerung.textListeEN.add("Stored in App");
                    break;
                }
                }

                if (willenserklaerung.willenserklaerung == KonstWillenserklaerung.neueZutrittsIdentZuMeldung
                        || piAnsichtVerarbeitungOderAnalyse == 2) {
                    /*Bei ZuZutrittsIdentNeuesDokument wurde ja die alte überschrieben, deshalb nichts einfügen*/
                    zugeordneteWillenserklaerungenList.add(zugeordneteWillenserklaerung);
                }
                if (willenserklaerung.willenserklaerung == KonstWillenserklaerung.zuZutrittsIdentNeuesDokument
                        && piAnsichtVerarbeitungOderAnalyse == 2) {
                    /*Verknüpfung eintragen*/
                    zugeordneteWillenserklaerung.verweisAufVorgaenger = veraenderteWK;
                    zugeordneteWillenserklaerungenList
                            .get(veraenderteWK).verweisAufNachfolger = zugeordneteWillenserklaerungenList.size() - 1;
                }
                if (willenserklaerung.willenserklaerung == KonstWillenserklaerung.zuZutrittsIdentNeuesDokument) {
                    zugeordneteWillenserklaerungenList.get(veraenderteWK).veraendert = true;
                }

                zugeordneteWillenserklaerung.stornierenIstZulaessig = true;
                if (lMeldung.klasse == 1) {
                    lAnzZutrittsIdentSelbst++;
                } else {
                    lGastKartenGemeldetEigeneAktien++;
                }
                break;
            }
            case KonstWillenserklaerung.vollmachtAnDritte: { /*Prüfen, ob Folge und zu ZutrittsIdent gehörend*/
                int veraenderteWK = -1;
                if (willenserklaerung.folgeBuchungFuerIdent != 0) {/*Folgebuchung => gehört diese zu Eintrittskartenbestellung?*/
                    int ii = 0;
                    for (ii = 0; ii < zugeordneteWillenserklaerungenList.size(); ii++) {
                        if (ii > 1000) {
                            return;
                        } /*TODO #9*/
                        if (zugeordneteWillenserklaerungenList
                                .get(ii).willenserklaerungIdent == willenserklaerung.folgeBuchungFuerIdent) {
                            veraenderteWK = ii;

                            if (piAnsichtVerarbeitungOderAnalyse == 1) {
                                zugeordneteWillenserklaerungenList.get(
                                        ii).willenserklaerung = KonstWillenserklaerung.neueZutrittsIdentZuMeldung_VollmachtAnDritte;
                                zugeordneteWillenserklaerungenList
                                        .get(ii).willenserklaerungIdent2 = willenserklaerung.willenserklaerungIdent;
                                zugeordneteWillenserklaerungenList.get(
                                        ii).bevollmaechtigterDritterIdent = willenserklaerung.bevollmaechtigterDritterIdent;

                                zugeordneteWillenserklaerungenList.get(ii).textListe.add("mit Vollmacht an:");
                                zugeordneteWillenserklaerungenList.get(ii).textListeEN.add("with proxy to:");

                                lDbBundle.dbPersonenNatJur.read(willenserklaerung.bevollmaechtigterDritterIdent);

                                zugeordneteWillenserklaerungenList.get(ii).textListe
                                        .add(lDbBundle.dbPersonenNatJur.personenNatJurArray[0].name + ", "
                                                + lDbBundle.dbPersonenNatJur.personenNatJurArray[0].vorname);
                                zugeordneteWillenserklaerungenList.get(ii).textListeEN
                                        .add(lDbBundle.dbPersonenNatJur.personenNatJurArray[0].name + ", "
                                                + lDbBundle.dbPersonenNatJur.personenNatJurArray[0].vorname);

                                zugeordneteWillenserklaerungenList.get(ii).textListe
                                        .add(lDbBundle.dbPersonenNatJur.personenNatJurArray[0].ort);
                                zugeordneteWillenserklaerungenList.get(ii).textListeEN
                                        .add(lDbBundle.dbPersonenNatJur.personenNatJurArray[0].ort);
                                lAnzZutrittsIdentSelbst--;
                                lAnzZutrittsIdentVollmacht++;
                            } else {
                                zugeordneteWillenserklaerungenList
                                        .get(ii).verweisAufNachfolger = zugeordneteWillenserklaerungenList.size();
                            }
                        }
                    }
                }
                if ((willenserklaerung.folgeBuchungFuerIdent == 0 || veraenderteWK == -1)
                        || piAnsichtVerarbeitungOderAnalyse == 2) {
                    /*separate Vollmacht an Dritte, oder ausführliche Darstellung*/
                    zugeordneteWillenserklaerung = prepareWillenserklaerungStatus(willenserklaerung,
                            willenserklaerungZusatz);

                    zugeordneteWillenserklaerung.bevollmaechtigterDritterIdent = willenserklaerung.bevollmaechtigterDritterIdent;

                    zugeordneteWillenserklaerung.textListe.add("Vollmacht an:");
                    zugeordneteWillenserklaerung.textListeEN.add("Proxy to:");

                    lDbBundle.dbPersonenNatJur.read(willenserklaerung.bevollmaechtigterDritterIdent);
                    if (piRueckgabeKurzOderLang == 2) {
                        zugeordneteWillenserklaerung.eclPersonenNatJurVertreter = lDbBundle.dbPersonenNatJur.personenNatJurArray[0];
                    }
                    zugeordneteWillenserklaerung.textListe.add(lDbBundle.dbPersonenNatJur.personenNatJurArray[0].name
                            + ", " + lDbBundle.dbPersonenNatJur.personenNatJurArray[0].vorname);
                    zugeordneteWillenserklaerung.textListeEN.add(lDbBundle.dbPersonenNatJur.personenNatJurArray[0].name
                            + ", " + lDbBundle.dbPersonenNatJur.personenNatJurArray[0].vorname);

                    zugeordneteWillenserklaerung.textListe.add(lDbBundle.dbPersonenNatJur.personenNatJurArray[0].ort);
                    zugeordneteWillenserklaerung.textListeEN.add(lDbBundle.dbPersonenNatJur.personenNatJurArray[0].ort);

                    if (veraenderteWK != -1) {
                        zugeordneteWillenserklaerung.verweisAufVorgaenger = veraenderteWK;
                    }
                    zugeordneteWillenserklaerungenList.add(zugeordneteWillenserklaerung);
                    zugeordneteWillenserklaerung.stornierenIstZulaessig = true;
                    lAnzVollmachtenDritte++;
                }
                break;
            }

            case KonstWillenserklaerung.vollmachtUndWeisungAnSRV: {
                zugeordneteWillenserklaerung = prepareWillenserklaerungStatus(willenserklaerung,
                        willenserklaerungZusatz);

                zugeordneteWillenserklaerung.textListe.add("Vollmacht und Weisung an Stimmrechtsvertreter");
                zugeordneteWillenserklaerung.textListeEN.add("Proxy and instructions to the proxy of the company");

                if (piRueckgabeKurzOderLang == 2) { /*Ggf Weisung einlesen*/
                    lDbBundle.dbWeisungMeldung.leseZuWillenserklaerungIdent(willenserklaerung.willenserklaerungIdent,
                            false);
                    if (lDbBundle.dbWeisungMeldung.anzWeisungMeldungGefunden() == 1) {
                        zugeordneteWillenserklaerung.eclWeisungMeldung = lDbBundle.dbWeisungMeldung
                                .weisungMeldungGefunden(0);
                    }
                }

                zugeordneteWillenserklaerungenList.add(zugeordneteWillenserklaerung);
                zugeordneteWillenserklaerung.stornierenIstZulaessig = true;
                zugeordneteWillenserklaerung.aendernIstZulaessig = true;
                lAnzKIAVSRV++;
                lAnzSRV++;
                break;
            }

            case KonstWillenserklaerung.vollmachtUndWeisungAnKIAV:
            case KonstWillenserklaerung.dauervollmachtAnKIAV:
            case KonstWillenserklaerung.organisatorischMitWeisungInSammelkarte: {
                zugeordneteWillenserklaerung = prepareWillenserklaerungStatus(willenserklaerung,
                        willenserklaerungZusatz);

                /*Nun zugeordnete Sammelkarte einlesen*/
                lDbBundle.dbMeldungZuSammelkarte.leseZuWillenserklaerung(willenserklaerung.willenserklaerungIdent);
                EclMeldungZuSammelkarte meldungZuSammelkarte = lDbBundle.dbMeldungZuSammelkarte
                        .meldungZuSammelkarteGefunden(0);
                int sammelIdent = meldungZuSammelkarte.sammelIdent;
                EclMeldung sammelMeldung = new EclMeldung();
                sammelMeldung.meldungsIdent = sammelIdent;
                lDbBundle.dbMeldungen.leseZuMeldungsIdent(sammelMeldung);

                /*Weisung einlesen, um z überprüfen ob gebunden/frei/Empfehlung*/
                lDbBundle.dbWeisungMeldung.leseZuWeisungIdent(meldungZuSammelkarte.weisungIdent, false);
                EclWeisungMeldung weisungMeldung = lDbBundle.dbWeisungMeldung.weisungMeldungGefunden(0);

                if (piRueckgabeKurzOderLang == 2) { /*Ggf Weisung eintragen*/
                    if (lDbBundle.dbWeisungMeldung.anzWeisungMeldungGefunden() == 1) {
                        zugeordneteWillenserklaerung.eclWeisungMeldung = lDbBundle.dbWeisungMeldung
                                .weisungMeldungGefunden(0);
                    }
                }

                if (weisungMeldung.hatDedizierteWeisungen()) {
                    /*Vollmacht und Weisung*/ zugeordneteWillenserklaerung.weisungenSind = 1;
                } else {
                    if (weisungMeldung.hatWeisungenGemEigenerEmpfehlungFlexNeu()
                            || weisungMeldung.hatWeisungenGemEigenerEmpfehlungFlexBestehend()
                            || weisungMeldung.hatWeisungenGemEigenerEmpfehlungFlexNeu()) {
                        /*Vollmacht und Weisung gemäß Vorschlag*/
                        zugeordneteWillenserklaerung.weisungenSind = 2;
                    } else {
                        /*Frei*/
                        zugeordneteWillenserklaerung.weisungenSind = 3;
                    }
                }
                switch (willenserklaerung.willenserklaerung) {
                case KonstWillenserklaerung.vollmachtUndWeisungAnKIAV: {
                    switch (zugeordneteWillenserklaerung.weisungenSind) {
                    case 0: {
                        zugeordneteWillenserklaerung.textListe.add("Vollmacht an Kreditinstitut/Aktionärsvereinigung:");
                        zugeordneteWillenserklaerung.textListeEN.add("Proxy to bank / shareholders union:");
                        break;
                    }
                    case 1: {
                        zugeordneteWillenserklaerung.textListe
                                .add("Vollmacht und Weisung an Kreditinstitut/Aktionärsvereinigung:");
                        zugeordneteWillenserklaerung.textListeEN
                                .add("Proxy and instructions to bank / shareholders union:");
                        zugeordneteWillenserklaerung.stornierenIstZulaessig = true;
                        zugeordneteWillenserklaerung.aendernIstZulaessig = true;
                        break;
                    }
                    case 2: {
                        zugeordneteWillenserklaerung.textListe
                                .add("Vollmacht und Weisung gemäß Vorschlag an Kreditinstitut/Aktionärsvereiniung:");
                        zugeordneteWillenserklaerung.textListeEN
                                .add("Proxy and instructions according their opinion to bank / shareholders union:");
                        zugeordneteWillenserklaerung.stornierenIstZulaessig = true;
                        zugeordneteWillenserklaerung.aendernIstZulaessig = false;
                        break;
                    }
                    case 3: {
                        zugeordneteWillenserklaerung.textListe
                                .add("Vollmacht (ohne Weisung) an Kreditinstitut/Aktionärsvereiniung:");
                        zugeordneteWillenserklaerung.textListeEN
                                .add("Proxy (without instructions) to bank / shareholders union:");
                        zugeordneteWillenserklaerung.stornierenIstZulaessig = true;
                        zugeordneteWillenserklaerung.aendernIstZulaessig = false;
                        break;
                    }
                    }
                    break;
                }
                case KonstWillenserklaerung.dauervollmachtAnKIAV: {
                    switch (zugeordneteWillenserklaerung.weisungenSind) {
                    case 0: {
                        zugeordneteWillenserklaerung.textListe.add("Dauervollmacht an Kreditinstitut:");
                        zugeordneteWillenserklaerung.textListeEN.add("Proxy to bank / shareholders union:");
                        break;
                    }
                    case 1: {
                        zugeordneteWillenserklaerung.textListe.add("Dauervollmacht und Weisung an Kreditinstitut:");
                        zugeordneteWillenserklaerung.textListeEN
                                .add("Proxy and instructions to bank / shareholders union:");
                        zugeordneteWillenserklaerung.stornierenIstZulaessig = true;
                        zugeordneteWillenserklaerung.aendernIstZulaessig = true;
                        break;
                    }
                    case 2: {
                        zugeordneteWillenserklaerung.textListe
                                .add("Dauervollmacht und Weisung gemäß Vorschlag an Kreditinstitut:");
                        zugeordneteWillenserklaerung.textListeEN
                                .add("Proxy and instructions according their opinion to bank / shareholders union:");
                        zugeordneteWillenserklaerung.stornierenIstZulaessig = true;
                        zugeordneteWillenserklaerung.aendernIstZulaessig = false;
                        break;
                    }
                    case 3: {
                        zugeordneteWillenserklaerung.textListe.add("Dauervollmacht (ohne Weisung) an Kreditinstitut:");
                        zugeordneteWillenserklaerung.textListeEN
                                .add("Proxy (without instructions) to bank / shareholders union:");
                        zugeordneteWillenserklaerung.stornierenIstZulaessig = true;
                        zugeordneteWillenserklaerung.aendernIstZulaessig = false;
                        break;
                    }
                    }
                    break;
                }
                case KonstWillenserklaerung.organisatorischMitWeisungInSammelkarte: {
                    switch (zugeordneteWillenserklaerung.weisungenSind) {
                    case 0: {
                        zugeordneteWillenserklaerung.textListe.add("Organisatorisch in Sammelkarte:");
                        zugeordneteWillenserklaerung.textListeEN.add("Proxy to bank / shareholders union:");
                        break;
                    }
                    case 1: {
                        zugeordneteWillenserklaerung.textListe.add("Organisatorisch mit Weisung in Sammelkarte:");
                        zugeordneteWillenserklaerung.textListeEN
                                .add("Proxy and instructions to bank / shareholders union:");
                        zugeordneteWillenserklaerung.stornierenIstZulaessig = true;
                        zugeordneteWillenserklaerung.aendernIstZulaessig = true;
                        break;
                    }
                    case 2: {
                        zugeordneteWillenserklaerung.textListe
                                .add("Organisatorisch mit Weisung gemäß Vorschlag in Sammelkarte:");
                        zugeordneteWillenserklaerung.textListeEN
                                .add("Proxy and instructions according their opinion to bank / shareholders union:");
                        zugeordneteWillenserklaerung.stornierenIstZulaessig = true;
                        zugeordneteWillenserklaerung.aendernIstZulaessig = false;
                        break;
                    }
                    case 3: {
                        zugeordneteWillenserklaerung.textListe.add("Organisatorisch (ohne Weisung) in Sammelkarte:");
                        zugeordneteWillenserklaerung.textListeEN
                                .add("Proxy (without instructions) to bank / shareholders union:");
                        zugeordneteWillenserklaerung.stornierenIstZulaessig = true;
                        zugeordneteWillenserklaerung.aendernIstZulaessig = false;
                        break;
                    }
                    }
                    break;
                }
                default:
                    break;

                }
                zugeordneteWillenserklaerung.textListe.add(lDbBundle.dbMeldungen.meldungenArray[0].kurzName);
                zugeordneteWillenserklaerung.textListeEN.add(lDbBundle.dbMeldungen.meldungenArray[0].kurzName);

                zugeordneteWillenserklaerungenList.add(zugeordneteWillenserklaerung);
                lAnzKIAVSRV++;
                lAnzKIAV++;
                break;
            }

            case KonstWillenserklaerung.briefwahl: {
                zugeordneteWillenserklaerung = prepareWillenserklaerungStatus(willenserklaerung,
                        willenserklaerungZusatz);

                if (lDbBundle.clGlobalVar.mandant != 178) {
                    zugeordneteWillenserklaerung.textListe.add("Stimmabgabe per Briefwahl");
                } else {
                    zugeordneteWillenserklaerung.textListe.add("Stimmabgabe");
                }
                if (ParamSpezial.ku152(lDbBundle.clGlobalVar.mandant)) {
                    zugeordneteWillenserklaerung.textListeEN.add("Absentee voting");
                } else {
                    zugeordneteWillenserklaerung.textListeEN.add("Postal voting");
                }

                if (piRueckgabeKurzOderLang == 2) { /*Ggf Weisung einlesen*/
                    lDbBundle.dbWeisungMeldung.leseZuWillenserklaerungIdent(willenserklaerung.willenserklaerungIdent,
                            false);
                    if (lDbBundle.dbWeisungMeldung.anzWeisungMeldungGefunden() == 1) {
                        zugeordneteWillenserklaerung.eclWeisungMeldung = lDbBundle.dbWeisungMeldung
                                .weisungMeldungGefunden(0);
                    }
                }

                zugeordneteWillenserklaerungenList.add(zugeordneteWillenserklaerung);
                zugeordneteWillenserklaerung.stornierenIstZulaessig = true;
                zugeordneteWillenserklaerung.aendernIstZulaessig = true;
                lAnzKIAVSRV++;
                lAnzBriefwahl++;
                break;
            }

            /* Veränderungen */
            /* Verarbeitung je nach piAnsichtVerarbeitungOderAnalyse - siehe dort!
             * Für Verarbeitungs-Ansicht gilt:
             * 		Die Ident der "Änderungs-Willenserklärung" muß als gültige letzte WillensIdent in die Liste eingetragen werden,
             * 		da diese für das Speichern der Änderung gebraucht wird*/
            case KonstWillenserklaerung.aendernWeisungAnSRV:
            case KonstWillenserklaerung.aendernWeisungAnKIAV:
            case KonstWillenserklaerung.aendernBriefwahl:
            case KonstWillenserklaerung.aendernWeisungOrganisatorischInSammelkarte: {
                int veraenderteWK = -1;
                int ii = 0;
                for (ii = 0; ii < zugeordneteWillenserklaerungenList.size(); ii++) {
                    if (ii > 2000) {
                        return;
                    } /*TODO #9*/
                    if (zugeordneteWillenserklaerungenList
                            .get(ii).willenserklaerungIdent == willenserklaerung.verweisAufWillenserklaerung) {
                        veraenderteWK = ii;
                        zugeordneteWillenserklaerungenList.get(ii).veraendert = true;

                        if (piAnsichtVerarbeitungOderAnalyse == 1) { /*Verarbeitungs-Ansicht*/
                            /*Zur Info: zugeordneteWillenserklaerungenList.get(ii).willenserklaerung bleibt unverändert - d.h. Aendern* taucht in der Liste nicht auf.
                             * Aber: die Willenserklärung der geänderten Ident wird eingetragen - für Folgeänderungen!*/
                            zugeordneteWillenserklaerungenList
                                    .get(ii).willenserklaerungIdent = willenserklaerung.willenserklaerungIdent;
                        } else {/*Detail-Analyse*/

                            zugeordneteWillenserklaerung = prepareWillenserklaerungStatus(willenserklaerung,
                                    willenserklaerungZusatz);

                            zugeordneteWillenserklaerung.textListe.add("Änderung Weisung/Stimmabgabe");
                            zugeordneteWillenserklaerung.textListeEN.add("Change of voting");

                            zugeordneteWillenserklaerung.verweisAufVorgaenger = veraenderteWK;

                            if (piRueckgabeKurzOderLang == 2) { /*Ggf Weisung einlesen*/
                                lDbBundle.dbWeisungMeldung
                                        .leseZuWillenserklaerungIdent(willenserklaerung.willenserklaerungIdent, false);
                                if (lDbBundle.dbWeisungMeldung.anzWeisungMeldungGefunden() == 1) {
                                    zugeordneteWillenserklaerung.eclWeisungMeldung = lDbBundle.dbWeisungMeldung
                                            .weisungMeldungGefunden(0);
                                }
                            }

                            zugeordneteWillenserklaerungenList.add(zugeordneteWillenserklaerung);
                            zugeordneteWillenserklaerungenList.get(
                                    veraenderteWK).verweisAufNachfolger = zugeordneteWillenserklaerungenList.size() - 1;

                        }
                    }
                }

                break;
            }

            /*Storno Vollmacht/Weisung*/
            case KonstWillenserklaerung.widerrufVollmachtUndWeisungAnSRV:
            case KonstWillenserklaerung.widerrufVollmachtUndWeisungAnKIAV:
            case KonstWillenserklaerung.widerrufBriefwahl:
            case KonstWillenserklaerung.widerrufDauervollmachtAnKIAV:
            case KonstWillenserklaerung.widerrufOrganisatorischMitWeisungInSammelkarte: {
                int veraenderteWK = -1;
                int ii = 0;
                for (ii = 0; ii < zugeordneteWillenserklaerungenList.size(); ii++) {
                    if (ii > 2000) {
                        return;
                    } /*TODO #9*/
                    if (zugeordneteWillenserklaerungenList
                            .get(ii).willenserklaerungIdent == willenserklaerung.verweisAufWillenserklaerung) {
                        veraenderteWK = ii;
                        //						System.out.println("BlWillenserklaerungStatus gef="+gef);
                        //						System.out.println("Storniert=true");
                        zugeordneteWillenserklaerungenList.get(ii).storniert = true;

                    }
                }
                if (piAnsichtVerarbeitungOderAnalyse == 1) { /*Verarbeitungs-Ansicht*/
                } else {/*Detail-Analyse*/

                    zugeordneteWillenserklaerung = prepareWillenserklaerungStatus(willenserklaerung,
                            willenserklaerungZusatz);

                    zugeordneteWillenserklaerung.textListe.add("Stornierung Weisung/Stimmabgabe");
                    zugeordneteWillenserklaerung.textListeEN.add("Delete of voting");

                    zugeordneteWillenserklaerung.verweisAufVorgaenger = veraenderteWK;

                    zugeordneteWillenserklaerungenList.add(zugeordneteWillenserklaerung);
                    zugeordneteWillenserklaerungenList
                            .get(veraenderteWK).verweisAufNachfolger = zugeordneteWillenserklaerungenList.size() - 1;
                }
                lAnzKIAVSRV--;
                if (willenserklaerung.willenserklaerung == KonstWillenserklaerung.widerrufBriefwahl) {
                    lAnzBriefwahl--;
                }
                if (willenserklaerung.willenserklaerung == KonstWillenserklaerung.widerrufVollmachtUndWeisungAnSRV) {
                    lAnzSRV--;
                }
                if (willenserklaerung.willenserklaerung == KonstWillenserklaerung.widerrufDauervollmachtAnKIAV
                        || willenserklaerung.willenserklaerung == KonstWillenserklaerung.widerrufVollmachtUndWeisungAnKIAV
                        || willenserklaerung.willenserklaerung == KonstWillenserklaerung.widerrufOrganisatorischMitWeisungInSammelkarte) {
                    lAnzKIAV--;
                }
                break;

            }

            /*Stornierungen*/
            case KonstWillenserklaerung.sperrenZutrittsIdent: {
                int veraenderteWK = -1;
                int ii = 0;
                for (ii = 0; ii < zugeordneteWillenserklaerungenList.size(); ii++) {
                    if (ii > 1000) {
                        return;
                    } /*TODO #9*/
                    if (BlZutrittsIdent.compare(zugeordneteWillenserklaerungenList.get(ii).zutrittsIdent,
                            zugeordneteWillenserklaerungenList.get(ii).zutrittsIdentNeben,
                            willenserklaerung.zutrittsIdent, willenserklaerung.zutrittsIdentNeben) == 0
                    //zugeordneteWillenserklaerungenList.get(ii).zutrittsIdent.compareTo(willenserklaerung.zutrittsIdent)==0
                    ) {
                        veraenderteWK = ii;
                        //						System.out.println("BlWillenserklaerungStatus gef="+gef);
                        //						System.out.println("Storniert=true");
                        zugeordneteWillenserklaerungenList.get(ii).storniert = true;
                        if (lMeldung.klasse == 1) {
                            if (zugeordneteWillenserklaerungenList.get(
                                    ii).willenserklaerung == KonstWillenserklaerung.neueZutrittsIdentZuMeldung_VollmachtAnDritte) {
                                lAnzZutrittsIdentVollmacht--;
                            } else {
                                lAnzZutrittsIdentSelbst--;
                            }
                        } else {
                            lGastKartenGemeldetEigeneAktien--;
                        }
                    }
                }
                if (piAnsichtVerarbeitungOderAnalyse == 1) { /*Verarbeitungs-Ansicht*/
                } else {/*Detail-Analyse*/

                    zugeordneteWillenserklaerung = prepareWillenserklaerungStatus(willenserklaerung,
                            willenserklaerungZusatz);

                    zugeordneteWillenserklaerung.textListe.add("Stornierung Weisung/Stimmabgabe");
                    zugeordneteWillenserklaerung.textListeEN.add("Delete of voting");

                    zugeordneteWillenserklaerung.verweisAufVorgaenger = veraenderteWK;

                    zugeordneteWillenserklaerungenList.add(zugeordneteWillenserklaerung);
                    zugeordneteWillenserklaerungenList
                            .get(veraenderteWK).verweisAufNachfolger = zugeordneteWillenserklaerungenList.size() - 1;
                }
                break;

            }

            case KonstWillenserklaerung.widerrufVollmachtAnDritte: {
                int veraenderteWK = -1;
                int ii = 0;
                for (ii = 0; ii < zugeordneteWillenserklaerungenList.size(); ii++) {
                    if (ii > 2000) {
                        return;
                    } /*TODO #9*/
                    if (zugeordneteWillenserklaerungenList
                            .get(ii).willenserklaerungIdent == willenserklaerung.verweisAufWillenserklaerung) {
                        veraenderteWK = ii;
                        //						System.out.println("BlWillenserklaerungStatus gef="+gef);
                        //						System.out.println("Storniert=true");
                        zugeordneteWillenserklaerungenList.get(ii).storniert = true;
                        lAnzVollmachtenDritte--;
                    }
                    if (zugeordneteWillenserklaerungenList
                            .get(ii).willenserklaerungIdent2 == willenserklaerung.verweisAufWillenserklaerung) {
                        /*Hier wird der Fall gesetzt, dass eine Eintrittskarte mit Vollmacht storniert wurde. Das Storno-Kennzeichen wird dabei
                         * schon durch den Widerruf der EK gesetzt. Deshalb hier nur für zukünftige "Fehlerabfangungen" eingebaut.
                         */
                        veraenderteWK = ii;
                    }

                }
                if (piAnsichtVerarbeitungOderAnalyse == 1) { /*Verarbeitungs-Ansicht*/
                } else {/*Detail-Analyse*/

                    zugeordneteWillenserklaerung = prepareWillenserklaerungStatus(willenserklaerung,
                            willenserklaerungZusatz);

                    zugeordneteWillenserklaerung.textListe.add("Stornierung Vollmacht/Dritte");
                    zugeordneteWillenserklaerung.textListeEN.add("Delete of Proxy");

                    zugeordneteWillenserklaerung.verweisAufVorgaenger = veraenderteWK;

                    zugeordneteWillenserklaerungenList.add(zugeordneteWillenserklaerung);
                    zugeordneteWillenserklaerungenList
                            .get(veraenderteWK).verweisAufNachfolger = zugeordneteWillenserklaerungenList.size() - 1;
                }
                break;

            }

            /*Unbehandelte*/
            default: {
                if (piAlleWillenserklaerungen == 1) {

                    if (piAnsichtVerarbeitungOderAnalyse == 1) { /*Bei Analyse-Ansicht bereits eingelesen!*/
                        lDbBundle.dbWillenserklaerungZusatz.leseZuIdent(willenserklaerung.willenserklaerungIdent);
                        willenserklaerungZusatz = lDbBundle.dbWillenserklaerungZusatz.willenserklaerungGefunden(0);
                    }

                    /*Neue Willenserklärung erzeugen*/
                    zugeordneteWillenserklaerung = prepareWillenserklaerungStatus(willenserklaerung,
                            willenserklaerungZusatz);
                    zugeordneteWillenserklaerung.textListe = new LinkedList<>();
                    zugeordneteWillenserklaerung.textListeEN = new LinkedList<>();

                    zugeordneteWillenserklaerung.textListe
                            .add(KonstWillenserklaerung.getText(willenserklaerung.willenserklaerung));

                    zugeordneteWillenserklaerung.stornierenIstZulaessig = false;

                    zugeordneteWillenserklaerungenList.add(zugeordneteWillenserklaerung);

                }
                /*Restliche Willenserklärungen werden aktuell ignoriert*/break;
            }
            }

        }

    }

    /**Ermittle Willenserkärungsnummer zu Meldung / Zutrittsident.
     * Wird benötigt, um die PDF-Nr. einer bereits ausgestellten Eintrittskarte (z.B. bei Druckwiederholung, oder Nachdruck)
     * festzulegen*/
    public int ermittleWKNummerZuMeldungZutrittsIdent(int klasse, int pMeldungsIdent, EclZutrittsIdent pEK) {

        if (klasse == 1) {
            lDbBundle.dbWillenserklaerung.leseZuMeldungsIdent(pMeldungsIdent);
        } else {
            lDbBundle.dbWillenserklaerung.leseZuMeldungsIdentGast(pMeldungsIdent);
        }
        int anz = lDbBundle.dbWillenserklaerung.anzWillenserklaerungGefunden();
        //		System.out.println("anz="+anz);

        if (anz == 0) {
            return -1;
        }
        for (int i = 0; i < anz; i++) {
            EclWillenserklaerung lWillenserklaerung = lDbBundle.dbWillenserklaerung.willenserklaerungGefunden(i);
            if (lWillenserklaerung.willenserklaerung == KonstWillenserklaerung.neueZutrittsIdentZuMeldung) {
                //				System.out.println("Willenserklärung = neueZutrittsIdentZuMeldung");
                EclZutrittsIdent wkZutrittsIdent = new EclZutrittsIdent();
                wkZutrittsIdent.zutrittsIdent = lWillenserklaerung.zutrittsIdent;
                wkZutrittsIdent.zutrittsIdentNeben = lWillenserklaerung.zutrittsIdentNeben;
                if (pEK.compareTo(wkZutrittsIdent) == 0) {
                    return lWillenserklaerung.willenserklaerungIdent;
                }
            }
        }

        return -1;
    }

    /**Voraussetzung:
     * piRueckgabeKurzOderLang=2;
     * piAnsichtVerarbeitungOderAnalyse=2;
     * 
     * ergaenzeZugeordneteMeldungenUmWillenserklaerungen ist bereits aufgerufen
     * 
     * Füllt die Liste der Bevollmächtigten Personen aus allen Willenserklärungen (so Vollmacht noch gültig)
     * 
     */
    public int ermittleVollmachtenAusMeldungenUndWillenserklaerungen() {
        rcListeVollmachten = new LinkedList<EclPersonenNatJur>();
        if (zugeordneteMeldungenEigeneAktienArray == null) {
            return 1;
        }
        for (int i = 0; i < zugeordneteMeldungenEigeneAktienArray.length; i++) {
            EclZugeordneteMeldung lZugeordneteMeldung = zugeordneteMeldungenEigeneAktienArray[i];
            List<EclWillenserklaerungStatus> zugeordneteWillenserklaerungenList = lZugeordneteMeldung.zugeordneteWillenserklaerungenList;
            if (zugeordneteWillenserklaerungenList != null) {
                for (int i1 = 0; i1 < zugeordneteWillenserklaerungenList.size(); i1++) {
                    EclWillenserklaerungStatus lWillenserklaerungStatus = zugeordneteWillenserklaerungenList.get(i1);
                    if (lWillenserklaerungStatus.bevollmaechtigterDritterIdent != 0
                            && lWillenserklaerungStatus.storniert == false) {
                        rcListeVollmachten.add(lWillenserklaerungStatus.eclPersonenNatJurVertreter);
                    }
                }
            }
        }
        return 1;
    }

}
