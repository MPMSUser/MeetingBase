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
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComKonst.KonstSkIst;
import de.meetingapps.meetingportal.meetComKonst.KonstStimmart;

public class BlInhaberImportInfo {

    /**Hinweis: das ganze funktioniert derzeit nur, wenn in einem Import eine nur eine Gattung verarbeitet wird!*/

    private int logDrucken = 3;

    private DbBundle lDbBundle = null;

    /**Gibt an, welche "Buchungsart" beim Einbuchen in Sammelkarte verwendet werden muß. Ist abhängig von der sammelkarte*/
    private int sammelkartenart = 0;
    private int sammelIdent = 0;

    /**Diese Klasswe wird immer wieder benötigt und enthält die aktuellen Abstimmungen*/
    private BlAbstimmungenWeisungen blAbstimmungenWeisungenErfassen = null;

    /**
     * Initialisierung der Klasse
     * @param pSammelIdent ident der Sammelkarte
     * @param dbBundle geoeffnete DbBundle
     */
    public void initialisierungsschritte(int pSammelIdent, DbBundle dbBundle) {
        /*Parameter die benötigt werden*/
        sammelIdent = pSammelIdent; //Nummer der ausgewählten Sammelkarte, in die gebucht werden soll
        lDbBundle = dbBundle;

        /*sammelkartenart bestimmen*/
        int rc = lDbBundle.dbMeldungen.leseZuIdent(pSammelIdent);

        if (rc < 1) {
            /*Fehler Sammelkarte unbekannt*/;
            return;
        }
        /*1=KIAV 2=SRV 3=organisatorisch 4=Briefwahl 5=Dauervollmacht*/
        sammelkartenart = lDbBundle.dbMeldungen.meldungenArray[0].skIst;

        /*Abstimmungen grundsätzlich einlesen*/
        blAbstimmungenWeisungenErfassen = new BlAbstimmungenWeisungen(true, lDbBundle);
        blAbstimmungenWeisungenErfassen.leseAgendaFuerInterneWeisungenErfassung();

    }

    /**Verarbeitung eines Datensatzes.
     * 
     * Aktionärs muss bereits angemeldet sein und alle Parameter müssen gefüllt werden!
     * 
     * @param pGattungDesAktionaers Gattung des Aktionärs 1-5
     * @param pWeisungen Beginnend an Position 0 die Zeichen J N E U sind zulässig.
     * Die Reihenfolge muß EXAKT so sein, wie sie bei der manuellen Weisungserfassung in der Bestandsverwaltung angezeigt wird. 
     * Auch die Anzahl der Weisungen muß exakt so sein. Überschriften werden aktuell ignoriert.
     * @param meldungsIdent MeldungsIdent des Aktionärs
     */
    public void verarbeitenEinesSatzes(int pGattungDesAktionaers, String pWeisungen, int meldungsIdent) {
//        /**Parameter, die für jeden Satz erforderlich sind, und dementsprechend gesetzt werden müssen*/
//        int pGattungDesAktionaers = 0;
//        int pSammelIdent = 0; //Nummer der ausgewählten Sammelkarte, in die gebucht werden soll
//        /*Weisungsstring für den Aktionär.
//         * Beginnend an Position 0 die Zeichen J N E U sind zulässig.
//         * Die Reihenfolge muß EXAKT so sein, wie sie bei der manuellen Weisungserfassung in der Bestandsverwaltung angezeigt wird. Auch die Anzahl der Weisungen
//         * muß exakt so sein. "Überschriftstops" (also z.B. 5 für 5.1, 5.2, 5.3) MÜSSEN enthalten sein (werden aber ignoriert, beliebiges Zeichen möglich).
//         * Also Beispiel für TOP 2, 3, 4, 5 (nur Überschrift) 5.1, 5.2: JNExJJ
//         */
//        String pWeisungen = "";

        /*Schritt 1: Eintragen ins aktienregister und Anmelden über Willenserkärung */

//        BlWillenserklaerung blWillenserklaerungMeldung = new BlWillenserklaerung();
        /*wie bei InhaberImport. Wird hier nicht nochmal erklärt :-) */

        /*Nach dem Durchführen der Anmeldung im schritt 1 steht in blWillenserklaerungMeldung.rcMeldungen[0] die Idents der erzeugten Meldung. 
         * Diese wird dann im zweiten Schritt wieder verwendet*/

        /*Schritt 2: Weisungen zusammensetzen, Willenserklärung in Sammelkarte buchen*/

//        int meldungsIdentAktionaer = blWillenserklaerungMeldung.rcMeldungen[0];

        BlWillenserklaerung blWillenserklaerungWeisung = new BlWillenserklaerung();

        blWillenserklaerungWeisung.piMeldungsIdentAktionaer = meldungsIdent;
        blWillenserklaerungWeisung.pWillenserklaerungGeberIdent = -1; /*Aktionär*/

        /*Sammelkartennr setzen*/
        blWillenserklaerungWeisung.pAufnehmendeSammelkarteIdent = sammelIdent;

        blAbstimmungenWeisungenErfassen.initWeisungMeldungNichtMarkiert(1);

        int skArt = 0;
        switch (sammelkartenart) {
        case 1:
            skArt = KonstSkIst.kiav;
            break;
        case 2:
            skArt = KonstSkIst.srv;
            break;
        case 3:
            skArt = KonstSkIst.organisatorisch;
            break;
        case 4:
            skArt = KonstSkIst.briefwahl;
            break;
        case 5:
            skArt = KonstSkIst.dauervollmacht;
            break;
        }

        final int laenge = pWeisungen.length();

        int offset = 0;
        for (int i = 0; i < blAbstimmungenWeisungenErfassen.liefereAnzAgendaArray(pGattungDesAktionaers); i++) {
            if (!blAbstimmungenWeisungenErfassen.pruefeObUeberschriftAgenda(i, pGattungDesAktionaers)) {
                int stimmart = KonstStimmart.frei;
                String lMarkierung = offset < laenge ? pWeisungen.substring(offset, offset + 1) : "E";
                switch (lMarkierung) {
                case "J":
                    stimmart = KonstStimmart.ja;
                    break;
                case "N":
                    stimmart = KonstStimmart.nein;
                    break;
                case "E":
                    stimmart = KonstStimmart.enthaltung;
                    break;
                case "U":
                    stimmart = KonstStimmart.ungueltig;
                    break;
                default:
                    stimmart = KonstStimmart.nichtMarkiert;
                    break;
                }
                if (!blAbstimmungenWeisungenErfassen.pruefeObZulaessigFuerSKIstAgenda(i, pGattungDesAktionaers,
                        skArt)) {
                    stimmart = KonstStimmart.nichtMarkiert;
                }

                blAbstimmungenWeisungenErfassen.speichereAgendaWeisungMeldungPos(0, i, pGattungDesAktionaers, stimmart,
                        skArt);
                offset++;
            } else {
//                offset++;
            }
        }

        if (lDbBundle.param.paramAbstimmungParameter.weisungenGegenantraegeInternSeparat) {
            for (int i = 0; i < blAbstimmungenWeisungenErfassen
                    .liefereAnzGegenantraegeArray(pGattungDesAktionaers); i++) {
                if (!blAbstimmungenWeisungenErfassen.pruefeObUeberschriftGegenantraege(i, pGattungDesAktionaers)) {
                    int stimmart = KonstStimmart.frei;
                    String lMarkierung = offset < laenge ? pWeisungen.substring(offset, offset + 1) : "E";
                    switch (lMarkierung) {
                    case "J":
                        stimmart = KonstStimmart.ja;
                        break;
                    case "N":
                        stimmart = KonstStimmart.nein;
                        break;
                    case "E":
                        stimmart = KonstStimmart.enthaltung;
                        break;
                    case "U":
                        stimmart = KonstStimmart.ungueltig;
                        break;
                    default:
                        stimmart = KonstStimmart.nichtMarkiert;
                        break;
                    }
                    if (!blAbstimmungenWeisungenErfassen.pruefeObZulaessigFuerSKIstGegenantraege(i,
                            pGattungDesAktionaers, skArt)) {
                        stimmart = KonstStimmart.nichtMarkiert;
                    }
                    blAbstimmungenWeisungenErfassen.speichereGegenantraegeWeisungMeldungPos(0, i, pGattungDesAktionaers,
                            stimmart, skArt);
                    offset++;
                } else {
//                    offset++;
                }
            }

        }

        /*Abgegebene Weisung (uninterpretiert)
            public EclWeisungMeldungRaw pEclWeisungMeldungRaw=null;*/
        blWillenserklaerungWeisung.pEclWeisungMeldungRaw = blAbstimmungenWeisungenErfassen.rcWeisungMeldungRaw[0];
        /*Abgegebene Weisung (interpretiert)
            public EclWeisungMeldung pEclWeisungMeldung=null;*/
        blWillenserklaerungWeisung.pEclWeisungMeldung = blAbstimmungenWeisungenErfassen.rcWeisungMeldung[0];

        /*Willenserklärung speichern*/
        CaBug.druckeLog("sammelkartenart ausführen=" + sammelkartenart, logDrucken, 10);
        switch (sammelkartenart) {
        case 1:
            blWillenserklaerungWeisung.vollmachtUndWeisungAnKIAV(lDbBundle);
            break;
        case 2:
            blWillenserklaerungWeisung.vollmachtUndWeisungAnSRV(lDbBundle);
            break;
        case 3:
            blWillenserklaerungWeisung.organisatorischMitWeisungInSammelkarte(lDbBundle);
            break;
        case 4:
            blWillenserklaerungWeisung.briefwahl(lDbBundle);
            break;
        case 5:
            blWillenserklaerungWeisung.dauervollmachtAnKIAV(lDbBundle);
            break;
        }

        /*Falls nicht möglich: Fehlermeldung, zum Anmelden*/
        if (blWillenserklaerungWeisung.rcIstZulaessig == false) {
            CaBug.drucke("001 Willenserklärung nicht ausführbar " + blWillenserklaerungWeisung.rcGrundFuerUnzulaessig
                    + " " + CaFehler.getFehlertext(blWillenserklaerungWeisung.rcGrundFuerUnzulaessig, 0));
        }
    }
}
