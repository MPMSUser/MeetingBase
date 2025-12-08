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

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComAllg.CaString;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclAbstimmung;
import de.meetingapps.meetingportal.meetComEntities.EclMeldung;
import de.meetingapps.meetingportal.meetComEntities.EclScan;
import de.meetingapps.meetingportal.meetComEntities.EclWeisungMeldung;
import de.meetingapps.meetingportal.meetComEntities.EclWeisungMeldungRaw;
import de.meetingapps.meetingportal.meetComKonst.KonstSkIst;
import de.meetingapps.meetingportal.meetComKonst.KonstStimmart;
import de.meetingapps.meetingportal.meetComKonst.KonstWeisungserfassungSicht;
import de.meetingapps.meetingportal.meetComKonst.KonstWillenserklaerung;
import de.meetingapps.meetingportal.meetComStub.StubRoot;
import de.meetingapps.meetingportal.meetComStub.WEStubBlAbstimmungenWeisungenErfassen;
import de.meetingapps.meetingportal.meetComStub.WEStubBlAbstimmungenWeisungenErfassenRC;
import de.meetingapps.meetingportal.meetComWE.WELoginVerify;

/**Routinen zur Erfassung von Abstimmungen und Weisungen.
 * Sollten über übergreifenden Stub angesteuert werden (wenn bei jeweiliger Funktion vermerkt!), 
 * der Dateiverwaltung und ggf. Vorbelegung der benötigten
 * Werte übernimmt
 *
 *Als Stub verwendbar.
 */
public class BlAbstimmungenWeisungen extends StubRoot {

    private int logDrucken=3;
    
    /*Grundsätzliche Logik:
     * 
     * Abstimmungsagenda wird über entsprechende "Sicht" (Weisung intern, Weisung beim Verlassen etc.) eingelesen.
     * 
     * Die Eingaben werden immer in einem Array [200] - analog Datenbank-Table zwischengehalten.
     * Einzelner Wert im Array=-999 => Agendapunkt steht für diese Sicht nicht zur Verfügung
     */

    public BlAbstimmungenWeisungen(boolean pIstServer, DbBundle pDbBundle) {
        super(pIstServer, pDbBundle);
    }

    /********************Werte, die in den Funktionen verwendet werden**********************/

    /************************Einlesen der jeweiligen Sicht - Ergebnis in rcAgendaArray oder rcGegenantraegeArray***************************/

    /**[0]=alle, [1-5] für jeweilige Gattung*/
    public EclAbstimmung[][] rcAgendaArray = null;
    public EclAbstimmung[][] rcGegenantraegeArray = null;

    /**Länge des Arrays. Kann immer verwendet werden (d.h. ist 0, wenn Array==null)
     * Werden beim Einlesen gesetzt*/
    public int rcAnzAgendaArray[] = null;
    public int rcAnzGegenantraegeArray[] = null;

    /*1*/
    /**Verwendet Datenzugriff
     * Achtung - sollte nicht direkt aufgerufen werden - aber public wegen Aufruf aus WAInern
     * 
     * Sicht siehe KonstWeisungserfassungSicht
     * 
     * Liefert:
     * rcAgendaArray, rcGegenantraegeArray, rcAnzAgendaArray, rcAnzGegenantraegeArray*/
    public int leseAgendaWeisungen(int sicht, boolean gegenantraegeSeparat, int aktuellOderPreview) {

        if (verwendeWebService()) {
            WEStubBlAbstimmungenWeisungenErfassen weStubBlAbstimmungenWeisungenErfassen = new WEStubBlAbstimmungenWeisungenErfassen();
            weStubBlAbstimmungenWeisungenErfassen.stubFunktion = 1;
            weStubBlAbstimmungenWeisungenErfassen.sicht = sicht;
            weStubBlAbstimmungenWeisungenErfassen.gegenantraegeSeparat = gegenantraegeSeparat;
            weStubBlAbstimmungenWeisungenErfassen.aktuellOderPreview = aktuellOderPreview;

            WELoginVerify weLoginVerify = new WELoginVerify();
            weStubBlAbstimmungenWeisungenErfassen.setWeLoginVerify(weLoginVerify);

            WEStubBlAbstimmungenWeisungenErfassenRC weStubBlAbstimmungenWeisungenErfassenRC = wsClient
                    .stubBlAbstimmungenWeisungenErfassen(weStubBlAbstimmungenWeisungenErfassen);

            if (weStubBlAbstimmungenWeisungenErfassenRC.rc < 1) {
                return weStubBlAbstimmungenWeisungenErfassenRC.rc;
            }

            rcAgendaArray = weStubBlAbstimmungenWeisungenErfassenRC.rcAgendaArray;
            rcGegenantraegeArray = weStubBlAbstimmungenWeisungenErfassenRC.rcGegenantraegeArray;
            rcAnzAgendaArray = weStubBlAbstimmungenWeisungenErfassenRC.rcAnzAgendaArray;
            rcAnzGegenantraegeArray = weStubBlAbstimmungenWeisungenErfassenRC.rcAnzGegenantraegeArray;

            return weStubBlAbstimmungenWeisungenErfassenRC.rc;
        }

        dbOpen();
        rcAgendaArray = new EclAbstimmung[6][];
        rcGegenantraegeArray = new EclAbstimmung[6][];
        int alleOderNurGesamtAktiveOderNurPreview=aktuellOderPreview+1;
        
        /*Gesamt-Agenda einlesen*/
        if (gegenantraegeSeparat) {
            lDbBundle.dbAbstimmungen.leseAgendaAllgemein(sicht, 1, alleOderNurGesamtAktiveOderNurPreview, 0);
            rcAgendaArray[0] = lDbBundle.dbAbstimmungen.abstimmungenArray;

            lDbBundle.dbAbstimmungen.leseAgendaAllgemein(sicht, 2, alleOderNurGesamtAktiveOderNurPreview, 0);
            CaBug.druckeLog("Lese Gegenanträge Länge="+lDbBundle.dbAbstimmungen.anzAbstimmungenGefunden(), logDrucken, 10);
            rcGegenantraegeArray[0] = lDbBundle.dbAbstimmungen.abstimmungenArray;
        } else {
            lDbBundle.dbAbstimmungen.leseAgendaAllgemein(sicht, 0, alleOderNurGesamtAktiveOderNurPreview, 0);
            rcAgendaArray[0] = lDbBundle.dbAbstimmungen.abstimmungenArray;
            rcGegenantraegeArray[0] = null;
        }

        /*Nun Länge belegen für Gesamtagenda belegen*/
        rcAnzAgendaArray = new int[6];
        rcAnzGegenantraegeArray = new int[6];

        if (rcAgendaArray[0] == null) {
            rcAnzAgendaArray[0] = 0;
        } else {
            rcAnzAgendaArray[0] = rcAgendaArray[0].length;
        }
        if (rcGegenantraegeArray[0] == null) {
            rcAnzGegenantraegeArray[0] = 0;
        } else {
            rcAnzGegenantraegeArray[0] = rcGegenantraegeArray[0].length;
        }

        /*Nun Gattungen belegen*/
        for (int i = 1; i <= 5; i++) {
            rcAnzAgendaArray[i] = 0;
            rcAnzGegenantraegeArray[i] = 0;
            if (lDbBundle.param.paramBasis.getGattungAktiv(i)) {
                /*Zuerst Anzahl bestimmen*/
                for (int i1 = 0; i1 < rcAnzAgendaArray[0]; i1++) {
                    if (rcAgendaArray[0][i1].stimmberechtigteGattungen[i - 1] == 1) {
                        rcAnzAgendaArray[i]++;
                    }
                }
                for (int i1 = 0; i1 < rcAnzGegenantraegeArray[0]; i1++) {
                    if (rcGegenantraegeArray[0][i1].stimmberechtigteGattungen[i - 1] == 1) {
                        rcAnzGegenantraegeArray[i]++;
                    }
                }
                /*Array initialisieren*/
                rcAgendaArray[i] = new EclAbstimmung[rcAnzAgendaArray[i]];
                rcGegenantraegeArray[i] = new EclAbstimmung[rcAnzGegenantraegeArray[i]];
                /*Nun Arrays füllen*/
                int offset = 0;
                for (int i1 = 0; i1 < rcAnzAgendaArray[0]; i1++) {
                    if (rcAgendaArray[0][i1].stimmberechtigteGattungen[i - 1] == 1) {
                        rcAgendaArray[i][offset] = rcAgendaArray[0][i1];
                        offset++;
                    }
                }
                offset = 0;
                for (int i1 = 0; i1 < rcAnzGegenantraegeArray[0]; i1++) {
                    if (rcGegenantraegeArray[0][i1].stimmberechtigteGattungen[i - 1] == 1) {
                        rcGegenantraegeArray[i][offset] = rcGegenantraegeArray[0][i1];
                        offset++;
                    }
                }

            }
        }
        dbClose();
        return 1;
    }

    /**Verwendet Datenzugriff*/
    public int leseAgendaFuerPortalWeisungen() {
        return leseAgendaWeisungen(KonstWeisungserfassungSicht.portalWeisungserfassung,
                lDbBundle.param.paramAbstimmungParameter.weisungenGegenantraegePortalSeparat, 1);
    }

    /**Verwendet Datenzugriff*/
    public int leseAgendaFuerInterneWeisungenErfassung() {
        return leseAgendaWeisungen(KonstWeisungserfassungSicht.interneWeisungserfassung,
                lDbBundle.param.paramAbstimmungParameter.weisungenGegenantraegeInternSeparat, 1);
    }

    /**Verwendet Datenzugriff*/
    public int leseAgendaFuerWeisungenReportIntern() {
        return leseAgendaWeisungen(KonstWeisungserfassungSicht.internWeisungsreports,
                lDbBundle.param.paramAbstimmungParameter.weisungenGegenantraegeInternSeparat, 1);
    }

    /**Verwendet Datenzugriff*/
    public int leseAgendaFuerWeisungenReportExtern() {
        return leseAgendaWeisungen(KonstWeisungserfassungSicht.externWeisungsreports,
                lDbBundle.param.paramAbstimmungParameter.weisungenGegenantraegeInternSeparat, 1);
    }

    /**Verwendet Datenzugriff*/
    public int leseAgendaFuerWeisungenVerlassenHV() {
        return leseAgendaWeisungen(KonstWeisungserfassungSicht.verlassenHVWeisung,
                lDbBundle.param.paramAbstimmungParameter.weisungenGegenantraegeVerlassenHVSeparat, 1);
    }

    /**Verwendet Datenzugriff*/
    public int leseAgendaFuerSicht(int pSicht) {
        boolean gegenantraegeSeparat = true;
        switch (pSicht) {
        case KonstWeisungserfassungSicht.portalWeisungserfassung:
            gegenantraegeSeparat = lDbBundle.param.paramAbstimmungParameter.weisungenGegenantraegePortalSeparat;
            break;
        case KonstWeisungserfassungSicht.interneWeisungserfassung:
            gegenantraegeSeparat = lDbBundle.param.paramAbstimmungParameter.weisungenGegenantraegeInternSeparat;
            break;
        case KonstWeisungserfassungSicht.internWeisungsreports:
            gegenantraegeSeparat = lDbBundle.param.paramAbstimmungParameter.weisungenGegenantraegeInternSeparat;
            break;
        case KonstWeisungserfassungSicht.externWeisungsreports:
            gegenantraegeSeparat = lDbBundle.param.paramAbstimmungParameter.weisungenGegenantraegeInternSeparat;
            break;
        case KonstWeisungserfassungSicht.verlassenHVWeisung:
            gegenantraegeSeparat = lDbBundle.param.paramAbstimmungParameter.weisungenGegenantraegeVerlassenHVSeparat;
            break;
        }
        return leseAgendaWeisungen(pSicht, gegenantraegeSeparat, 1);
    }

    /******************Div. Funktionen zum Verwenden in Schleifen für alle Punkte in rcAgendaArray / rcGegenantraegeArray*****************/

    /**+++++++++Liefern Anzahl Abstimmungen+++++++++++++*/
    /**Geht schnell - liefert nur Wert. 0=für alle, sonst gattung*/
    public int liefereAnzAgendaArray(int gattung) {
        return rcAnzAgendaArray[gattung];
    }

    /**Geht schnell - liefert nur Wert. 0=für alle, sonst gattung*/
    public int liefereAnzGegenantraegeArray(int gattung) {
        return rcAnzGegenantraegeArray[gattung];
    }

    public int liefereAnzArray(int pAgendaArt, int pAktuelleGattung) {
        if (pAgendaArt == 1) {
            return liefereAnzAgendaArray(pAktuelleGattung);
        } else {
            return liefereAnzGegenantraegeArray(pAktuelleGattung);
        }
    }

    /**++++++++Holen der kompletten Abstimmungsdefinition+++++++++
     * 
     * Liefert je nach pAgendaArt
     * ==1 => rcAgendaArray
     * ==2 => rcGegemamtraegeArray
     */
    public EclAbstimmung liefereRcAgendaArtArray(int pAgendaArt, int pAktuelleGattung, int pAbstimmungsOffset) {
        if (pAgendaArt == 1) {
            return rcAgendaArray[pAktuelleGattung][pAbstimmungsOffset];
        } else {
            return rcGegenantraegeArray[pAktuelleGattung][pAbstimmungsOffset];
        }
    }

    /**+++++++++Prüfen, ob Überschrift+++++++++*/
    public boolean pruefeObUeberschriftAgenda(int pAbstimmungsOffset, int pAktuelleGattung) {
        return pruefeObUeberschrift(rcAgendaArray[pAktuelleGattung][pAbstimmungsOffset]);
    }

    public boolean pruefeObUeberschriftGegenantraege(int pAbstimmungsOffset, int pAktuelleGattung) {
        return pruefeObUeberschrift(rcGegenantraegeArray[pAktuelleGattung][pAbstimmungsOffset]);
    }

    public boolean pruefeObUeberschrift(int pArt, int pAbstimmungsOffset, int pAktuelleGattung) {
        if (pArt == 1) {
            return pruefeObUeberschriftAgenda(pAbstimmungsOffset, pAktuelleGattung);
        } else {
            return pruefeObUeberschriftGegenantraege(pAbstimmungsOffset, pAktuelleGattung);
        }
    }

    private boolean pruefeObUeberschrift(EclAbstimmung pAbstimmung) {
        int pos = pAbstimmung.identWeisungssatz;
        if (pos < 0) {
            return true;
        }
        return false;
    }

    /**++++Liefert interne TOP-Nummer+++++++*/
    public String liefereTOPNummerIntern(int pArt, int pAbstimmungsOffset, int pAktuelleGattung) {
        if (pArt == 1) {
            return rcAgendaArray[pAktuelleGattung][pAbstimmungsOffset].nummerKey
                    + rcAgendaArray[pAktuelleGattung][pAbstimmungsOffset].nummerindexKey;
        } else {
            return rcGegenantraegeArray[pAktuelleGattung][pAbstimmungsOffset].nummerKey
                    + rcGegenantraegeArray[pAktuelleGattung][pAbstimmungsOffset].nummerindexKey;
        }
    }

    /**+++++Liefert Kurzbezeichnung+++++++++++*/
    public String liefereKurzBezeichnung(int pArt, int pAbstimmungsOffset, int pAktuelleGattung) {
        if (pArt == 1) {
            return rcAgendaArray[pAktuelleGattung][pAbstimmungsOffset].kurzBezeichnung;
        } else {
            return rcGegenantraegeArray[pAktuelleGattung][pAbstimmungsOffset].kurzBezeichnung;
        }
    }

    /**+++++Liefert Kurzbezeichnung Anzeige+++++++++++*/
    public String liefereKurzBezeichnungAnzeige(int pArt, int pAbstimmungsOffset, int pAktuelleGattung) {
        if (pArt == 1) {
            return rcAgendaArray[pAktuelleGattung][pAbstimmungsOffset].liefereAnzeigeBezeichnungKurz();
        } else {
            return rcGegenantraegeArray[pAktuelleGattung][pAbstimmungsOffset].liefereAnzeigeBezeichnungKurz();
        }
    }

    /**+++++Liefert Langbezeichnung Anzeige+++++++++++*/
    public String liefereLangBezeichnungAnzeige(int pArt, int pAbstimmungsOffset, int pAktuelleGattung) {
        if (pArt == 1) {
            return rcAgendaArray[pAktuelleGattung][pAbstimmungsOffset].anzeigeBezeichnungLang;
        } else {
            return rcGegenantraegeArray[pAktuelleGattung][pAbstimmungsOffset].anzeigeBezeichnungLang;
        }
    }

    /**++++++++++Liefert Weisungsposition+++++++++++*/
    public int liefereWeisungsPosition(int pArt, int pAbstimmungsOffset, int pAktuelleGattung) {
        if (pArt == 1) {
            return rcAgendaArray[pAktuelleGattung][pAbstimmungsOffset].identWeisungssatz;
        } else {
            return rcGegenantraegeArray[pAktuelleGattung][pAbstimmungsOffset].identWeisungssatz;
        }

    }

    /**+++Prüfen, ob Weisung für Sammelkartenart aktiviert++++*/
    /**pSkIst grundsätzlich wie KonstSkIst, d.h. SRV, Briefwahl, KIAV. Orga und Dauer wie KIAV*/
    public boolean pruefeObZulaessigFuerSKIstAgenda(int pAbstimmungsOffset, int pAktuelleGattung, int pSkIst) {
        return pruefeObZulaessigFuerSKIst(rcAgendaArray[pAktuelleGattung][pAbstimmungsOffset], pSkIst);
    }

    /**pSkIst grundsätzlich wie KonstSkIst, d.h. SRV, Briefwahl, KIAV. Orga und Dauer wie KIAV*/
    public boolean pruefeObZulaessigFuerSKIstGegenantraege(int pAbstimmungsOffset, int pAktuelleGattung, int pSkIst) {
        return pruefeObZulaessigFuerSKIst(rcGegenantraegeArray[pAktuelleGattung][pAbstimmungsOffset], pSkIst);
    }

    private boolean pruefeObZulaessigFuerSKIst(EclAbstimmung pAbstimmung, int pSkIst) {
        int hSkIst = pSkIst;
        switch (pSkIst) {
        case KonstSkIst.dauervollmacht:
            hSkIst = KonstSkIst.kiav;
            break;
        case KonstSkIst.organisatorisch:
            hSkIst = KonstSkIst.kiav;
            break;
        }

        switch (hSkIst) {
        case KonstSkIst.srv:
            return (pAbstimmung.aktivBeiSRV == 1);
        case KonstSkIst.briefwahl:
            return (pAbstimmung.aktivBeiBriefwahl == 1);
        case KonstSkIst.kiav:
            return (pAbstimmung.aktivBeiKIAVDauer == 1);
        }

        return false;
    }

    /*************************Weisungs-Pflege********************************/
    public EclWeisungMeldung[] rcWeisungMeldung = null;
    public EclWeisungMeldungRaw[] rcWeisungMeldungRaw = null;

    public void initWeisungMeldung(int anz) {
        rcWeisungMeldung = new EclWeisungMeldung[anz];
        rcWeisungMeldungRaw = new EclWeisungMeldungRaw[anz];

        /*Vorinitialisieren, für den Fall dass die Weisung nicht aus DB gelesen wird,
         * sondern als neue Weisung verwendet wird
         * 
         */
        for (int i1 = 0; i1 < anz; i1++) {
            rcWeisungMeldung[i1] = new EclWeisungMeldung();
            rcWeisungMeldungRaw[i1] = new EclWeisungMeldungRaw();
            for (int i = 0; i < 200; i++) {
                rcWeisungMeldung[i1].abgabe[i] = -999;
            }
        }
    }

    public void initWeisungMeldungNichtMarkiert(int anz) {
        rcWeisungMeldung = new EclWeisungMeldung[anz];
        rcWeisungMeldungRaw = new EclWeisungMeldungRaw[anz];

        /*Vorinitialisieren, für den Fall dass die Weisung nicht aus DB gelesen wird,
         * sondern als neue Weisung verwendet wird
         * 
         */
        for (int i1 = 0; i1 < anz; i1++) {
            rcWeisungMeldung[i1] = new EclWeisungMeldung();
            rcWeisungMeldungRaw[i1] = new EclWeisungMeldungRaw();
            for (int i = 0; i < 200; i++) {
                rcWeisungMeldung[i1].abgabe[i] = KonstStimmart.nichtMarkiert;
            }
        }
    }

    /**
     * pMeldeNr=Offset innerhalb der rcAgendaArray, erster Satz hat Offset 0!
     * pAbstimmungsOffset = Offset der Abstimmung im aktuellen Abstimmungsaray von pAktuelleGattung
     * pStimmart = wie KonstStimmart
     * pSkArt = wie KonstSkIst (notwendig um zu bestimmen, wie mit nicht-markierten umgegangen wird)
     * 		einschließlich srvHV
     * 
     * rc: -1 unzulässige Weisungspos (Überschrift oder ähnliches)
     */
    public int speichereAgendaWeisungMeldungPos(int pMeldeNr, int pAbstimmungsOffset, int pAktuelleGattung,
            int pStimmart, int pSkIst) {
        EclAbstimmung pAbstimmung = rcAgendaArray[pAktuelleGattung][pAbstimmungsOffset];
        return speichereInternWeisungMeldungPos(pMeldeNr, pAbstimmung, pAbstimmungsOffset, pAktuelleGattung, pStimmart,
                pSkIst);

    }

    public int speichereGegenantraegeWeisungMeldungPos(int pMeldeNr, int pAbstimmungsOffset, int pAktuelleGattung,
            int pStimmart, int pSkIst) {
        EclAbstimmung pAbstimmung = rcGegenantraegeArray[pAktuelleGattung][pAbstimmungsOffset];
        return speichereInternWeisungMeldungPos(pMeldeNr, pAbstimmung, pAbstimmungsOffset, pAktuelleGattung, pStimmart,
                pSkIst);

    }

    private int speichereInternWeisungMeldungPos(int pMeldeNr, EclAbstimmung pAbstimmung, int pAbstimmungsOffset,
            int pAktuelleGattung, int pStimmart, int pSkIst) {
        /*pos der Weisung bestimmen*/
        int pos = pAbstimmung.identWeisungssatz;
        if (pos < 0) {
            return -1;
        }

        /*Raw-String erzeugen*/
        String rawString = "";
        if (pStimmart != KonstStimmart.nichtMarkiert) {
            rawString = CaString.fuelleLinksBlank("X", pStimmart + 1);
        }

        /*Ggf. Nicht-Markierte umsetzen*/
        if (pStimmart == KonstStimmart.nichtMarkiert) {
            /*"Wie Parameter"*/
            switch (pSkIst) {
            case KonstSkIst.kiav:
                pStimmart = lDbBundle.param.paramAbstimmungParameter.weisungNichtMarkierteSpeichernAlsKIAV;
                break;
            case KonstSkIst.srv:
                pStimmart = lDbBundle.param.paramAbstimmungParameter.weisungNichtMarkierteSpeichernAlsSRV;
                break;
            case KonstSkIst.organisatorisch:
                pStimmart = lDbBundle.param.paramAbstimmungParameter.weisungNichtMarkierteSpeichernAlsOrg;
                break;
            case KonstSkIst.briefwahl:
                pStimmart = lDbBundle.param.paramAbstimmungParameter.weisungNichtMarkierteSpeichernAlsBriefwahl;
                break;
            case KonstSkIst.dauervollmacht:
                pStimmart = lDbBundle.param.paramAbstimmungParameter.weisungNichtMarkierteSpeichernAlsDauer;
                break;
            case KonstSkIst.srvHV:
                pStimmart = lDbBundle.param.paramAbstimmungParameter.weisungHVNichtMarkierteSpeichernAls;
                break;
            }
            /*"Wie in Abstimmung"*/
            int hStimmart = 0;
            switch (pSkIst) {
            case KonstSkIst.kiav:
                pStimmart = pAbstimmung.weisungNichtMarkierteSpeichernAlsKIAV;
                break;
            case KonstSkIst.srv:
                pStimmart = pAbstimmung.weisungNichtMarkierteSpeichernAlsSRV;
                break;
            case KonstSkIst.organisatorisch:
                pStimmart = pAbstimmung.weisungNichtMarkierteSpeichernAlsOrg;
                break;
            case KonstSkIst.briefwahl:
                pStimmart = pAbstimmung.weisungNichtMarkierteSpeichernAlsBriefwahl;
                break;
            case KonstSkIst.dauervollmacht:
                pStimmart = pAbstimmung.weisungNichtMarkierteSpeichernAlsDauer;
                break;
            case KonstSkIst.srvHV:
                pStimmart = pAbstimmung.weisungHVNichtMarkierteSpeichernAls;
                break;
            }
            if (hStimmart != -1) {
                pStimmart = hStimmart;
            }
        }

        /*Abspeichern*/
        rcWeisungMeldung[pMeldeNr].abgabe[pos] = pStimmart;
        rcWeisungMeldungRaw[pMeldeNr].abgabe[pos] = rawString;

        return 1;
    }

    public int holeAgendaArtWeisungMeldungPos(int pAgendaArt, int pMeldeNr, int pAbstimmungsOffset,
            int pAktuelleGattung) {
        EclAbstimmung pAbstimmung = null;
        if (pAgendaArt == 1) {
            pAbstimmung = rcAgendaArray[pAktuelleGattung][pAbstimmungsOffset];
        } else {
            pAbstimmung = rcGegenantraegeArray[pAktuelleGattung][pAbstimmungsOffset];
        }
        return holeInternWeisungMeldungPos(pMeldeNr, pAbstimmung);

    }

    public int holeAgendaWeisungMeldungPos(int pMeldeNr, int pAbstimmungsOffset, int pAktuelleGattung) {
        EclAbstimmung pAbstimmung = rcAgendaArray[pAktuelleGattung][pAbstimmungsOffset];
        return holeInternWeisungMeldungPos(pMeldeNr, pAbstimmung);

    }

    public int holeGegenantraegeWeisungMeldungPos(int pMeldeNr, int pAbstimmungsOffset, int pAktuelleGattung) {
        EclAbstimmung pAbstimmung = rcGegenantraegeArray[pAktuelleGattung][pAbstimmungsOffset];
        return holeInternWeisungMeldungPos(pMeldeNr, pAbstimmung);

    }

    private int holeInternWeisungMeldungPos(int pMeldeNr, EclAbstimmung pAbstimmung) {
        /*pos der Weisung bestimmen*/
        int pos = pAbstimmung.identWeisungssatz;
        if (pos < 0) {
            return -1;
        }

        return rcWeisungMeldung[pMeldeNr].abgabe[pos];
    }

    /**Anzahl der in der jeweiligen Gruppe [1-10] maximal zulässigen Stimmabgaben*/
    public int[] rcGruppenMax = null;
    /**Anzahl der in der jeweiligen Gruppe [1-10] tatsächlich vorhandenen Stimmabgaben (d.h. J)*/
    public int[] rcGruppenIst = null;

    /**Prüft, ob maximale Gruppen-Anzahl überschritten.
     * pGattung=1 bis 5
     * Füllt rcGruppenMax, rcGruppenIst.
     * @return
     */
    public void pruefeGruppen(int pMeldeNr, int pGattung) {
        rcGruppenMax = new int[11];
        rcGruppenIst = new int[11];

        /*Anzahl der Ist-Werte auf 0 setzen*/
        for (int i = 0; i <= 10; i++) {
            rcGruppenIst[i] = 0;
            rcGruppenMax[i] = lDbBundle.param.paramAbstimmungParameter.anzahlJaJeAbstimmungsgruppe[i];
        }

        /*Alle Agenda-Punkte durcharbeiten*/
        for (int i = 0; i < rcAnzAgendaArray[pGattung]; i++) {
            int gruppe = rcAgendaArray[pGattung][i].zuAbstimmungsgruppe;
            if (gruppe != 0) {
                if (rcWeisungMeldung[pMeldeNr].abgabe[rcAgendaArray[pGattung][i].identWeisungssatz] == KonstStimmart.ja) {
                    rcGruppenIst[gruppe]++;
                }
            }
        }

        /*Alle Gegenanträge-Punkte durcharbeiten*/
        for (int i = 0; i < rcAnzGegenantraegeArray[pGattung]; i++) {
            int gruppe = rcGegenantraegeArray[pGattung][i].zuAbstimmungsgruppe;
            if (gruppe != 0) {
                if (rcWeisungMeldung[pMeldeNr].abgabe[rcGegenantraegeArray[pGattung][i].identWeisungssatz] == KonstStimmart.ja) {
                    rcGruppenIst[gruppe]++;
                }
            }
        }

    }

    public boolean gruppeIstGroesserMax(int pGruppe) {
        if (rcGruppenIst[pGruppe] > rcGruppenMax[pGruppe]) {
            return true;
        } else {
            return false;
        }
    }

    /**Setzt Abstimmungspunkte für die Gruppe pGruppe auf Ungültig*/
    public void korrigiereGruppe(int pMeldeNr, int pGattung, int pGruppe) {
        for (int i = 0; i < rcAnzAgendaArray[pGattung]; i++) {
            int gruppe = rcAgendaArray[pGattung][i].zuAbstimmungsgruppe;
            if (gruppe == pGruppe) {
                rcWeisungMeldung[pMeldeNr].abgabe[rcAgendaArray[pGattung][i].identWeisungssatz] = KonstStimmart.ungueltig;
            }
        }

        for (int i = 0; i < rcAnzGegenantraegeArray[pGattung]; i++) {
            int gruppe = rcGegenantraegeArray[pGattung][i].zuAbstimmungsgruppe;
            if (gruppe == pGruppe) {
                rcWeisungMeldung[pMeldeNr].abgabe[rcGegenantraegeArray[pGattung][i].identWeisungssatz] = KonstStimmart.ungueltig;
            }
        }
    }

    /*2*/
    /**Erteilt eine neue Vollmacht/Weisung an den SRV, mit den in rcWeisungMeldungRaw
     * und rcWeisungMeldung fertig aufbereiteten Weisungen
     * RC<0 => Fehlermeldung
     * 
     * Benötigte Datenzugriff*/
    public int[] erzeugeSRVHV(int[] ausgewaehlteKIAV, int[] meldungsIdent, String[] quelle) {

        if (verwendeWebService()) {
            WEStubBlAbstimmungenWeisungenErfassen weStubBlAbstimmungenWeisungenErfassen = new WEStubBlAbstimmungenWeisungenErfassen();
            weStubBlAbstimmungenWeisungenErfassen.stubFunktion = 2;
            weStubBlAbstimmungenWeisungenErfassen.ausgewaehlteKIAV = ausgewaehlteKIAV;
            weStubBlAbstimmungenWeisungenErfassen.meldungsIdent = meldungsIdent;
            weStubBlAbstimmungenWeisungenErfassen.quelle = quelle;
            weStubBlAbstimmungenWeisungenErfassen.rcWeisungMeldung = rcWeisungMeldung;
            weStubBlAbstimmungenWeisungenErfassen.rcWeisungMeldungRaw = rcWeisungMeldungRaw;

            WELoginVerify weLoginVerify = new WELoginVerify();
            weStubBlAbstimmungenWeisungenErfassen.setWeLoginVerify(weLoginVerify);

            WEStubBlAbstimmungenWeisungenErfassenRC weStubBlAbstimmungenWeisungenErfassenRC = wsClient
                    .stubBlAbstimmungenWeisungenErfassen(weStubBlAbstimmungenWeisungenErfassen);

            if (weStubBlAbstimmungenWeisungenErfassenRC.rc < 1) {
                int[] fehlerArray = new int[meldungsIdent.length];
                for (int i = 0; i < meldungsIdent.length; i++) {
                    fehlerArray[i] = weStubBlAbstimmungenWeisungenErfassenRC.rc;
                }
                return fehlerArray;
            }

            rcWeisungMeldungRaw = weStubBlAbstimmungenWeisungenErfassenRC.rcWeisungMeldungRaw;
            rcWeisungMeldung = weStubBlAbstimmungenWeisungenErfassenRC.rcWeisungMeldung;

            return weStubBlAbstimmungenWeisungenErfassenRC.rcArray;
        }

        int[] fehlerArray = new int[meldungsIdent.length];

        dbOpen();
        for (int i = 0; i < meldungsIdent.length; i++) {
            if (meldungsIdent[i] != 0) {
                /***Vollmacht/Weisung an Stimmrechstvertreter****/
                BlWillenserklaerung vwWillenserklaerung = new BlWillenserklaerung();

                vwWillenserklaerung.piMeldungsIdentAktionaer = meldungsIdent[i];
                vwWillenserklaerung.pWillenserklaerungGeberIdent = -1; /*Aktionär*/
                vwWillenserklaerung.pAufnehmendeSammelkarteIdent = ausgewaehlteKIAV[i];
                if (quelle != null) {
                    vwWillenserklaerung.pQuelle = quelle[i];
                }

                for (int i1 = 0; i1 < 200; i1++) {
                    if (rcWeisungMeldung[i].abgabe[i1] == -999) {
                        rcWeisungMeldung[i].abgabe[i1] = 0;
                    }
                }
                vwWillenserklaerung.pEclWeisungMeldungRaw = rcWeisungMeldungRaw[i];
                vwWillenserklaerung.pEclWeisungMeldung = rcWeisungMeldung[i];

                /*Willenserklärung speichern*/
                vwWillenserklaerung.vollmachtUndWeisungAnSRV(lDbBundle);
                if (vwWillenserklaerung.rcIstZulaessig == false) {
                    fehlerArray[i] = vwWillenserklaerung.rcGrundFuerUnzulaessig;
                } else {
                    fehlerArray[i] = 1;
                }
            }
        }
        dbClose();
        return fehlerArray;
    }

    /********************************Für Scann-Lauf**********************************/

    /**Achtung - die folgenden 3 Felder NICHT direkt verwenden. Sind nur public wg. Web-Service*/
    public int ausgewaehlteKartenklasse = 0;
    public int[] zulaessigeKartenarten = null;
    public int[] sammelkartenFuerGattungen = null;

    /*3*/
    /**Scan-Lauf-Initialisieren.
     * Benötigt Datenzugriff
     * 
     * pWillenserklaerung=
     * 		KonstWillenserklaerung.briefwahl
     * 		KonstWillenserklaerung.vollmachtUndWeisungAnSRV
     * 		KonstWillenserklaerung.verlassenUndVollmachtUndWeisungAnSRV
     */
    public EclScan[] scanDateiInit(int pWillenserklaerung, int pAusgewaehlteKartenklasse, int[] pZulaessigeKartenarten,
            int[] pSammelkartenFuerGattungen) {
        zulaessigeKartenarten = pZulaessigeKartenarten;
        ausgewaehlteKartenklasse = pAusgewaehlteKartenklasse;
        sammelkartenFuerGattungen = pSammelkartenFuerGattungen;

        if (verwendeWebService()) {
            WEStubBlAbstimmungenWeisungenErfassen weStubBlAbstimmungenWeisungenErfassen = new WEStubBlAbstimmungenWeisungenErfassen();
            weStubBlAbstimmungenWeisungenErfassen.stubFunktion = 3;
            weStubBlAbstimmungenWeisungenErfassen.pAusgewaehlteKartenklasse = pAusgewaehlteKartenklasse;
            weStubBlAbstimmungenWeisungenErfassen.pZulaessigeKartenarten = pZulaessigeKartenarten;
            weStubBlAbstimmungenWeisungenErfassen.pSammelkartenFuerGattungen = pSammelkartenFuerGattungen;

            WELoginVerify weLoginVerify = new WELoginVerify();
            weStubBlAbstimmungenWeisungenErfassen.setWeLoginVerify(weLoginVerify);

            WEStubBlAbstimmungenWeisungenErfassenRC weStubBlAbstimmungenWeisungenErfassenRC = wsClient
                    .stubBlAbstimmungenWeisungenErfassen(weStubBlAbstimmungenWeisungenErfassen);

            if (weStubBlAbstimmungenWeisungenErfassenRC.rc < 1) {
                return null;
            }

            return weStubBlAbstimmungenWeisungenErfassenRC.eclScanArray;
        }

        dbOpen();

        /*Aktueller Scan-Lauf-Nummer (neue Nummer) definieren und zurückschreiben*/
        int aktuellerScanLauf = lDbBundle.dbScan.holeNeueScanLaufNummer();

        /*Alle noch nicht verarbeiteten Scans mit der aktuellen Scan-Lauf-Nummer updaten*/
        switch (pWillenserklaerung) {
        case KonstWillenserklaerung.briefwahl:
            /*int rc=*/lDbBundle.dbScan.updateUngeleseneScanVorgaenge(aktuellerScanLauf, false, true, false, false, false,
                    false, false, false);
            break;
        case KonstWillenserklaerung.vollmachtUndWeisungAnSRV:
            /*int rc=*/lDbBundle.dbScan.updateUngeleseneScanVorgaenge(aktuellerScanLauf, false, false, true, false, false,
                    false, false, false);
            break;
        case KonstWillenserklaerung.verlassenUndVollmachtUndWeisungAnSRV:
            /*int rc=*/lDbBundle.dbScan.updateUngeleseneScanVorgaenge(aktuellerScanLauf, false, false, false, true, false,
                    false, false, false);
            break;
        }

        /*Ergebnis= aktuellerScanLauf*/
        lDbBundle.dbScan.read_scanVorgaenge(aktuellerScanLauf);
        EclScan[] scanTable = lDbBundle.dbScan.ergebnisArray;

        dbClose();
        return scanTable;
    }

    public String[] rcFehlerMeldungString = null;
    public String[] rcGeleseneNummer = null;

    /*4*/
    /**
     * Verwendet:
     * ausgewaehlteKartenklasse, zulaessigeKartenarten, sammelkartenFuerGattungen
     * rcAgendaArray, rcGegenantraegeArray, rcAnzAgendaArray, rcAnzGegenantraegeArray
     * Return-Werte:
     * rcFehlerMeldungString, rcGeleseneNummer
     * return-Wert selbst: Fehlernummern
     */
    public int[] scanDateiVerarbeiten(EclScan[] pScanBuendel) {
        if (verwendeWebService()) {
            WEStubBlAbstimmungenWeisungenErfassen weStubBlAbstimmungenWeisungenErfassen = new WEStubBlAbstimmungenWeisungenErfassen();
            weStubBlAbstimmungenWeisungenErfassen.stubFunktion = 4;
            weStubBlAbstimmungenWeisungenErfassen.pScanBuendel = pScanBuendel;
            weStubBlAbstimmungenWeisungenErfassen.pAusgewaehlteKartenklasse = ausgewaehlteKartenklasse;
            weStubBlAbstimmungenWeisungenErfassen.pZulaessigeKartenarten = zulaessigeKartenarten;
            weStubBlAbstimmungenWeisungenErfassen.pSammelkartenFuerGattungen = sammelkartenFuerGattungen;

            weStubBlAbstimmungenWeisungenErfassen.rcAgendaArray = rcAgendaArray;
            weStubBlAbstimmungenWeisungenErfassen.rcGegenantraegeArray = rcGegenantraegeArray;
            weStubBlAbstimmungenWeisungenErfassen.rcAnzAgendaArray = rcAnzAgendaArray;
            weStubBlAbstimmungenWeisungenErfassen.rcAnzGegenantraegeArray = rcAnzGegenantraegeArray;

            WELoginVerify weLoginVerify = new WELoginVerify();
            weStubBlAbstimmungenWeisungenErfassen.setWeLoginVerify(weLoginVerify);

            WEStubBlAbstimmungenWeisungenErfassenRC weStubBlAbstimmungenWeisungenErfassenRC = wsClient
                    .stubBlAbstimmungenWeisungenErfassen(weStubBlAbstimmungenWeisungenErfassen);

            if (weStubBlAbstimmungenWeisungenErfassenRC.rc < 1) {
                return null;
            }

            rcFehlerMeldungString = weStubBlAbstimmungenWeisungenErfassenRC.rcFehlerMeldungString;
            rcGeleseneNummer = weStubBlAbstimmungenWeisungenErfassenRC.rcGeleseneNummer;
            return weStubBlAbstimmungenWeisungenErfassenRC.rcArray;
        }

        dbOpen();
        int anzahlScans = pScanBuendel.length;
        rcFehlerMeldungString = new String[anzahlScans];
        rcGeleseneNummer = new String[anzahlScans];
        int[] rcCodes = new int[anzahlScans];
        int[] meldeIdentArray = new int[anzahlScans];
        int[] sammelIdentArray = new int[anzahlScans];
        String[] quelleArray = new String[anzahlScans];

        initWeisungMeldung(anzahlScans);

        for (int i = 0; i < anzahlScans; i++) {
            EclScan lScan = pScanBuendel[i];

            /*Meldung einlesen*/
            rcGeleseneNummer[i] = lScan.barcode.trim();
            ;
            BlTeilnehmerdatenLesen blTeilnehmerdatenLesen = new BlTeilnehmerdatenLesen(true, lDbBundle);
            rcCodes[i] = blTeilnehmerdatenLesen.leseMeldung(ausgewaehlteKartenklasse, rcGeleseneNummer[i],
                    zulaessigeKartenarten);
            rcFehlerMeldungString[i] = blTeilnehmerdatenLesen.rcFehlerMeldungText;
            EclMeldung eclMeldung = blTeilnehmerdatenLesen.rcEclMeldung;
            //	    	int personNatJurIdent=blTeilnehmerdatenLesen.rcPersonNatJurIdent;
            if (rcCodes[i] > 0) {
                int aktuelleGattung = eclMeldung.liefereGattung();

                meldeIdentArray[i] = eclMeldung.meldungsIdent;
                sammelIdentArray[i] = sammelkartenFuerGattungen[aktuelleGattung];

                quelleArray[i] = lScan.dateiname;

                uebertrageScanSatzInWeisungMeldung(aktuelleGattung, lScan, i, KonstSkIst.srvHV);

            } else {
                meldeIdentArray[i] = 0;
            }

        }
        int[] rcErzeugenSRVHV = erzeugeSRVHV(sammelIdentArray, meldeIdentArray, quelleArray);
        for (int i = 0; i < anzahlScans; i++) {
            if (meldeIdentArray[i] != 0) {
                rcCodes[i] = rcErzeugenSRVHV[i];
                if (rcCodes[i] < 1) {
                    rcFehlerMeldungString[i] = CaFehler.getFehlertext(rcCodes[i], 0);
                }
            }

        }

        dbClose();
        return rcCodes;
    }

    public void uebertrageScanSatzInWeisungMeldung(int pAktuelleGattung, EclScan pScan, int pSpeichernInOffset,
            int pSkIst) {
        int anzAgenda = liefereAnzAgendaArray(pAktuelleGattung);
        //		int anzGegenantraege=liefereAnzGegenantraegeArray(aktuelleGattung);

        String gesamtmarkierung = pScan.gesamtmarkierung;
        int offsetInScan = 1;
        for (int i1 = 0; i1 < anzAgenda; i1++) {
            if (!rcAgendaArray[pAktuelleGattung][i1].liefereIstUeberschift()) {
                String einzelMarkierung = pScan.pos[offsetInScan];
                int stimmartEingabe = liefereStimmart(gesamtmarkierung, einzelMarkierung);
                speichereAgendaWeisungMeldungPos(pSpeichernInOffset, i1, pAktuelleGattung, stimmartEingabe, pSkIst);
                offsetInScan++;
            }
        }

        /*Gruppen ggf. auf ungültig setzen*/
        pruefeGruppen(pSpeichernInOffset, pAktuelleGattung);
        for (int i1 = 1; i1 <= 10; i1++) {
            if (gruppeIstGroesserMax(i1)) {
                korrigiereGruppe(pSpeichernInOffset, pAktuelleGattung, i1);
            }
        }

    }

    private int liefereStimmart(String pGesamtmarkierung, String pEinzelmarkierung) {
        int stimmart = -1;
        switch (pEinzelmarkierung) {
        case "1":
            stimmart = KonstStimmart.ja;
            break;
        case "2":
            stimmart = KonstStimmart.nein;
            break;
        case "3":
            stimmart = KonstStimmart.enthaltung;
            break;
        }

        if (stimmart == -1) {
            switch (pGesamtmarkierung) {
            case "1":
                stimmart = KonstStimmart.ja;
                break;
            case "2":
                stimmart = KonstStimmart.nein;
                break;
            case "3":
                stimmart = KonstStimmart.enthaltung;
                break;
            }
        }

        if (stimmart == -1) {
            stimmart = KonstStimmart.nichtMarkiert;
        }
        return stimmart;
    }
}
