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
package de.meetingapps.meetingportal.meetingportTFunktionen;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComEclM.EclBesitzGesamtAuswahl1M;
import de.meetingapps.meetingportal.meetComEclM.EclParamM;
import de.meetingapps.meetingportal.meetComEclM.EclPortalTexteM;
import de.meetingapps.meetingportal.meetComKonst.KonstEintrittskarteDetailArt;
import de.meetingapps.meetingportal.meetComKonst.KonstPortalAktion;
import de.meetingapps.meetingportal.meetComKonst.KonstPortalFehlerView;
import de.meetingapps.meetingportal.meetComKonst.KonstPortalTexte;
import de.meetingapps.meetingportal.meetComKonst.KonstPortalView;
import de.meetingapps.meetingportal.meetComKonst.KonstWillenserklaerungVersandartEK;
import de.meetingapps.meetingportal.meetingportTController.TAbstimmungserg;
import de.meetingapps.meetingportal.meetingportTController.TAnmeldenOhneErklaerung;
import de.meetingapps.meetingportal.meetingportTController.TAuswahl1;
import de.meetingapps.meetingportal.meetingportTController.TAuswahl1Teilnahme;
import de.meetingapps.meetingportal.meetingportTController.TBestaetigen;
import de.meetingapps.meetingportal.meetingportTController.TDialogveranstaltungen;
import de.meetingapps.meetingportal.meetingportTController.TEinstellungen;
import de.meetingapps.meetingportal.meetingportTController.TEinstellungenSession;
import de.meetingapps.meetingportal.meetingportTController.TEintrittskarte;
import de.meetingapps.meetingportal.meetingportTController.TEintrittskarteQuittungUDetail;
import de.meetingapps.meetingportal.meetingportTController.TEintrittskarteQuittungUDetailSession;
import de.meetingapps.meetingportal.meetingportTController.TEintrittskarteStornieren;
import de.meetingapps.meetingportal.meetingportTController.TFehlerView;
import de.meetingapps.meetingportal.meetingportTController.TFehlerViewSession;
import de.meetingapps.meetingportal.meetingportTController.TGaeste;
import de.meetingapps.meetingportal.meetingportTController.TGeneralversammlung;
import de.meetingapps.meetingportal.meetingportTController.TMitteilung;
import de.meetingapps.meetingportal.meetingportTController.TMonitoring;
import de.meetingapps.meetingportal.meetingportTController.TPasswortVergessen;
import de.meetingapps.meetingportal.meetingportTController.TPraesenzZugangAbgang;
import de.meetingapps.meetingportal.meetingportTController.TSession;
import de.meetingapps.meetingportal.meetingportTController.TStimmabgabe;
import de.meetingapps.meetingportal.meetingportTController.TStimmabgabeku310;
import de.meetingapps.meetingportal.meetingportTController.TTeilnehmerverz;
import de.meetingapps.meetingportal.meetingportTController.TUnterlagen;
import de.meetingapps.meetingportal.meetingportTController.TWeisung;
import de.meetingapps.meetingportal.meetingportTController.TWeisungAendern;
import de.meetingapps.meetingportal.meetingportTController.TWeisungBestaetigung;
import de.meetingapps.meetingportal.meetingportTController.TWeisungQuittung;
import de.meetingapps.meetingportal.meetingportTController.TWeisungStornieren;
import de.meetingapps.meetingportal.meetingportTController.TWeisungStornierenQuittung;
import de.meetingapps.meetingportal.meetingportTController.TWillenserklaerungSession;
import de.meetingapps.meetingportal.meetingportTController.TZuordnung;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@RequestScoped
@Named
public class TSeitensteuerung {

    private int logDrucken = 3;

    private @Inject EclPortalTexteM eclPortalTexteM;
    private @Inject EclParamM eclParamM;

    private @Inject TSeitensteuerungSession tSeitensteuerungSession;

    private @Inject TSession tSession;
    private @Inject TWillenserklaerungSession tWillenserklaerungSession;

    private @Inject TBestaetigen tBestaetigen;
    private @Inject TPasswortVergessen tPasswortVergessen;

    private @Inject TEinstellungenSession tEinstellungenSession;
    private @Inject TEinstellungen tEinstellungen;
    private @Inject TAnmeldenOhneErklaerung tAnmeldenOhneErklaerung;
    private @Inject TEintrittskarte tEintrittskarte;
    private @Inject TEintrittskarteQuittungUDetail tEintrittskarteQuittungUDetail;
    private @Inject TEintrittskarteQuittungUDetailSession tEintrittskarteQuittungUDetailSession;
    private @Inject TEintrittskarteStornieren tEintrittskarteStornieren;
    private @Inject TWeisung tWeisung;
    private @Inject TWeisungBestaetigung tWeisungBestaetigung;
    private @Inject TWeisungQuittung tWeisungQuittung;
    private @Inject TWeisungStornieren tWeisungStornieren;
    private @Inject TWeisungStornierenQuittung tWeisungStornierenQuittung;
    private @Inject TWeisungAendern tWeisungAendern;
    private @Inject TUnterlagen tUnterlagen;
    private @Inject TMitteilung tMitteilung;
    private @Inject TTeilnehmerverz tTeilnehmerverz;
    private @Inject TAbstimmungserg tAbstimmungserg;
    private @Inject TMonitoring tMonitoring;
    private @Inject TFehlerViewSession tFehlerViewSession;
    private @Inject TFehlerView tFehlerView;
    private @Inject TAuswahl1 tAuswahl1;
    private @Inject TGeneralversammlung tGeneralversammlung;
    private @Inject TDialogveranstaltungen tDialogveranstaltungen;
    private @Inject TAuswahl1Teilnahme tAuswahl1Teilnahme;
    private @Inject EclBesitzGesamtAuswahl1M eclBesitzGesamtAuswahl1M;
    private @Inject TStimmabgabe tStimmabgabe;
    private @Inject TStimmabgabeku310 tStimmabgabeku310;
    private @Inject TPraesenzZugangAbgang tPraesenzZugangAbgang;

    private @Inject TZuordnung tZuordnung;
    private @Inject TGaeste tGaeste;

    /**********************Funktionen*******************************/
    /**Belegt Render-Werte für Start-Text / Überschrift und Buttons unten*/
    public void belegeFuerSeite() {
        CaBug.druckeLog("tSession.getViewnummer()=" + tSession.getViewnummer(), logDrucken, 5);
        tSeitensteuerungSession.setUeberschriftAnzeigen(false);
        tSeitensteuerungSession.setStarttextAnzeigen(false);
        tSeitensteuerungSession.setLinkenButtonAnzeigen(false);
        tSeitensteuerungSession.setRechtenButtonAnzeigen(false);
        setzeAppTitel("");

        switch (tSession.getViewnummer()) {

        case KonstPortalView.FEHLER_UNBEKANNT:
            /*Keine Texte, sondern fest kodiert*/
            setzeAppTitel("");
            setzeUeberschrift("");
            setzeStarttext("");
            setzeLogoutButtonsOff();
            break;
        case KonstPortalView.FEHLER_VERAENDERT:
            setzeAppTitel("");
            setzeUeberschrift("1462");
            setzeStarttext("1463");
            setzeButtonRechts(false, "1464");
            setzeLogoutButtonsOff();
            break;
        case KonstPortalView.FEHLER_DIALOG:
            setzeAppTitel("");
            setzeUeberschrift("204");
            setzeStarttext("205");
            setzeLogoutButtonsOff();
            setzeButtonRechts(false, "206");
            break;
        case KonstPortalView.FEHLER_VIEW:
            setzeAppTitel("");
            setzeLogoutButtonsOff();
            setzeButtonRechts(false, "1214");
            switch (tFehlerViewSession.getFehlerArt()) {
            case KonstPortalFehlerView.VERAENDERTER_BESITZ__NEUSTART_DER_FUNKTION:
                setzeUeberschrift("1212");
                setzeStarttext("1213");
                break;
            case KonstPortalFehlerView.FUNKTION_NICHT_MEHR_VERFUEGBAR__ABBRUCH:
                setzeUeberschrift("1215");
                setzeStarttext("1216");
                break;
            case KonstPortalFehlerView.STIMMZAEHLUNG_GESCHLOSSEN__ABBRUCH:
                setzeUeberschrift("1217");
                setzeStarttext("1218");
                break;
            case KonstPortalFehlerView.STIMMZAEHLUNG_VERAENDERT__NEUSTART:
                setzeUeberschrift("1219");
                setzeStarttext("1220");
                break;
            case KonstPortalFehlerView.HVPORTAL_GESCHLOSSEN__ABBRUCH:
                setzeUeberschrift("1465");
                setzeStarttext("1466");
                break;
            case KonstPortalFehlerView.KEIN_BESTAND_MEHR_FUER_FUNKTION__ABBRUCH:
                setzeUeberschrift("1467");
                setzeStarttext("1468");
                break;
            case KonstPortalFehlerView.ANMELDEFRIST_ABGELAUFEN:
                setzeUeberschrift("1469");
                setzeStarttext("1470");
                break;
            case KonstPortalFehlerView.WILLENSERKLARUNG_NICHT_MEHR_MOEGLICH__ABBRUCH:
                setzeUeberschrift("1471");
                setzeStarttext("1472");
                break;
            case KonstPortalFehlerView.BRIEFWAHL_NICHT_MOEGLICH_KEINE_ABSTIMMUNGEN__ABBRUCH:
                setzeUeberschrift("1475");
                setzeStarttext("1476");
                break;
            case KonstPortalFehlerView.SRV_NICHT_MOEGLICH_KEINE_ABSTIMMUNGEN__ABBRUCH:
                setzeUeberschrift("1473");
                setzeStarttext("1474");
                break;
            case KonstPortalFehlerView.AUSWERTUNG_LAEUFT_KEINE_WEISUNGSAENDERUNG:
                setzeUeberschrift("1491");
                setzeStarttext("1492");
                break;
            case KonstPortalFehlerView.TRANSAKTIONSABBRUCH_ANDERER_USER_AKTIV:
                setzeUeberschrift("1496");
                setzeStarttext("1497");
                break;
            case KonstPortalFehlerView.AUSWERTUNG_LAEUFT_KEINE_WEISUNGSAENDERUNG_WEISUNG_DURCH_ANDERE_ERTEILT:
                setzeUeberschrift("1498");
                setzeStarttext("1499");
                break;
            case KonstPortalFehlerView.STIMMABGABE_NICHT_MOEGLICH_DA_VOR_ORT_PRAESENT:
                setzeUeberschrift("2023");
                setzeStarttext("2024");
                break;
            default:
                setzeUeberschrift("");
                setzeStarttext("");
                break;
            }
            break;
        case KonstPortalView.BESTAETIGEN:
        case KonstPortalView.BESTAETIGEN2:
            setzeAppTitel("");
            setzeUeberschrift("212");
            setzeStarttext("213");
            setzeLogoutButtonsOff();
            setzeButtonRechts(false, "214");
           break;

        case KonstPortalView.BESTAETIGT:
            setzeAppTitel("");
            setzeUeberschrift("215");
            setzeStarttext("216");
            setzeLogoutButtonsOff();
           break;
 
        case KonstPortalView.P_PASSWORT_VERGESSEN:
        case KonstPortalView.PASSWORT_VERGESSEN:
            setzeAppTitel("");
            setzeUeberschrift("207");
            setzeStarttext("");
            setzeLogoutButtonsOff();
            setzeButtonLinks("557");
            setzeButtonRechts(false, "211");
           break;

        case KonstPortalView.P_PASSWORT_VERGESSEN_QUITTUNG:
        case KonstPortalView.PASSWORT_VERGESSEN_QUITTUNG:
            setzeAppTitel("");
            setzeUeberschrift("243");
            setzeStarttext("");
            setzeLogoutButtonsOff();
            break;

         case KonstPortalView.PW_ZURUECK:
             setzeAppTitel("244");
             setzeUeberschrift("242");
             setzeStarttext("250");
             setzeLogoutButtonsOff();
             setzeButtonRechts(true, "248");
            break;

         case KonstPortalView.P_PW_ZURUECK:
             setzeAppTitel("");
//             setzeUeberschrift("");
             setzeStarttext("");
             setzeLogoutButtonsOff();
             setzeButtonRechts(false, "34");
            break;

        case KonstPortalView.LOGIN:
            setzeAppTitel("224");
            setzeUeberschrift("3");
            setzeStarttext("4");
            setzeLogoutButtonsLoginSeite();
            break;

        case KonstPortalView.EINSTELLUNGEN:
           if (tEinstellungenSession.isErstregistrierung()) {
               setzeAppTitel("284");
                setzeUeberschrift("23");
            } else {
                setzeAppTitel("285");
                setzeUeberschrift("36");
            }
            setzeStarttext("1253");
            setzeLogoutButtons();
            setzeButtonRechts(false, "34");
            break;

        case KonstPortalView.P_EINSTELLUNGEN:
            if (tEinstellungenSession.isErstregistrierung()) {
                setzeAppTitel("1699");
                 setzeUeberschrift("1700");
             } else {
                 setzeAppTitel("1701");
                 setzeUeberschrift("1702");
             }
             setzeStarttext("1703");
             setzeLogoutButtons();
             setzeButtonRechts(false, "1704");
             break;

        case KonstPortalView.EINSTELLUNGEN_BESTAETIGUNG:
            setzeAppTitel("1421");
            setzeUeberschrift("618");
            setzeStarttext("628");
            setzeLogoutButtons();
            setzeButtonLinks("54");
            setzeButtonRechts(false, "630");
            break;
        case KonstPortalView.P_EINSTELLUNGEN_BESTAETIGUNG:
            setzeAppTitel("1705");
            setzeUeberschrift("1706");
            setzeStarttext("1707");
            setzeLogoutButtons();
            setzeButtonLinks("54");
            setzeButtonRechts(false, "1708");
            break;

        case KonstPortalView.BESITZ_VERTRETUNG_ABFRAGEN:
            setzeAppTitel("1456");
            setzeUeberschrift("1448");
            setzeStarttext("1449");
            setzeLogoutButtons();
            break;


        case KonstPortalView.STREAM_START:
            setzeAppTitel("1419");
            setzeUeberschrift("794");
            setzeStarttext("795");
            setzeLogoutButtons();
            setzeButtonLinks("54");
            break;

        case KonstPortalView.UNTERLAGEN:
            setzeAppTitel(KonstPortalTexte.IUNTERLAGEN_APPTITEL);
            setzeUeberschrift(KonstPortalTexte.IUNTERLAGEN_UEBERSCHRIFT);
            setzeStarttext(KonstPortalTexte.IUNTERLAGEN_STARTTEXT);
            setzeLogoutButtons();
            setzeButtonLinks("54");
            break;

        case KonstPortalView.BOTSCHAFTEN_ANZEIGEN:
            setzeAppTitel(KonstPortalTexte.IBOTSCHAFTEN_ANZEIGEN_APPTITEL);
            setzeUeberschrift(KonstPortalTexte.IBOTSCHAFTEN_ANZEIGEN_UEBERSCHRIFT);
            setzeStarttext(KonstPortalTexte.IBOTSCHAFTEN_ANZEIGEN_STARTTEXT);
            setzeLogoutButtons();
            setzeButtonLinks("54");
            break;

        case KonstPortalView.FRAGEN:
            setzeAppTitel("884");
            setzeUeberschrift("768");
            setzeStarttext(""); //keiner vorhanden
            setzeLogoutButtons();
            setzeButtonLinks("54");
            break;

        case KonstPortalView.RUECKFRAGEN:
            setzeAppTitel("1885");
            setzeUeberschrift("1886");
            setzeStarttext(""); //keiner vorhanden
            setzeLogoutButtons();
            setzeButtonLinks("54");
            break;

        case KonstPortalView.WORTMELDUNGEN:
           setzeAppTitel("1411");
           setzeUeberschrift("1130");
            setzeStarttext(""); //keiner vorhanden
            setzeLogoutButtons();
            setzeButtonLinks("54");
            break;

        case KonstPortalView.WIDERSPRUECHE:
            setzeAppTitel("885");
           setzeUeberschrift("808");
            setzeStarttext(""); //keiner vorhanden
            setzeLogoutButtons();
            setzeButtonLinks("54");
            break;
            
        case KonstPortalView.ANTRAEGE:
            setzeAppTitel("1412");
            setzeUeberschrift("1413");
            setzeStarttext(""); //keiner vorhanden
            setzeLogoutButtons();
            setzeButtonLinks("54");
            break;

        case KonstPortalView.SONSTIGEMITTEILUNGEN:
            setzeAppTitel("1414");
            setzeUeberschrift("1415");
            setzeStarttext(""); //keiner vorhanden
            setzeLogoutButtons();
            setzeButtonLinks("54");
            break;
            
        case KonstPortalView.BOTSCHAFTEN_EINREICHEN:
            setzeAppTitel("1887");
            setzeUeberschrift("1888");
            setzeStarttext(""); //keiner vorhanden
            setzeLogoutButtons();
            setzeButtonLinks("54");
            break;

        case KonstPortalView.TEILNEHMERVERZEICHNIS:
            setzeAppTitel("886");
            setzeUeberschrift("842");
            setzeStarttext("843"); 
            setzeLogoutButtons();
            setzeButtonLinks("54");
            setzeButtonRechts(false, "847");
            break;

        case KonstPortalView.ABSTIMMUNGSERGEBNISSE:
            setzeAppTitel("887");
            setzeUeberschrift("848");
            setzeStarttext("849"); 
            setzeLogoutButtons();
            setzeButtonLinks("54");
            setzeButtonRechts(false, "853");
            break;

        case KonstPortalView.MONITORING:
            setzeAppTitel("");
            setzeUeberschrift("1459");
            setzeStarttext("1460"); 
            setzeLogoutButtons();
            setzeButtonLinks("54");
            setzeButtonRechts(false, "1461");
            break;


      case KonstPortalView.AUSWAHL:
          setzeAppTitel("604");
            setzeUeberschrift("38");
            setzeStarttext("1225");
            setzeLogoutButtons();
            break;

      case KonstPortalView.AUSWAHL1:
//          setzeAppTitel("604");
            setzeUeberschrift("570");
            setzeStarttext("574");
            setzeLogoutButtons();
            break;

      case KonstPortalView.P_AUSWAHL:
//        setzeAppTitel("604");
          setzeUeberschrift("570");
          setzeStarttext("574");
          setzeLogoutButtons();
          break;

      case KonstPortalView.AUSWAHL1_GENERALVERSAMMLUNG:
//        setzeAppTitel("604");
          setzeUeberschrift("672");
          setzeStarttext(""); //keiner vorhanden
          setzeButtonLinks("1091");
          setzeLogoutButtons();
          break;

      case KonstPortalView.AUSWAHL1_GENERALVERSAMMLUNG_BRIEFWAHL:
//        setzeAppTitel("604");
          setzeUeberschrift("1969");
          setzeStarttext(""); //keiner vorhanden
          setzeButtonLinks("1091");
          setzeLogoutButtons();
          break;

      case KonstPortalView.AUSWAHL1_GENERALVERSAMMLUNG_BEIRATSWAHL:
//        setzeAppTitel("604");
          setzeUeberschrift("2149"); /*1969*/
          setzeStarttext(""); //keiner vorhanden
          setzeButtonLinks("2150"); /*1091*/
          setzeLogoutButtons();
          break;

      case KonstPortalView.AUSWAHL1_DIALOGVERANSTALTUNGEN:
//        setzeAppTitel("604");
          setzeUeberschrift("675");
          setzeStarttext(""); //keiner vorhanden
          setzeButtonLinks("54");
          setzeLogoutButtons();
          break;

      case KonstPortalView.AUSWAHL1_ANMELDEN_DIALOGVERANSTALTUNGEN:
//        setzeAppTitel("604");
          setzeUeberschrift("681");
          setzeStarttext(""); //keiner vorhanden
          setzeButtonLinks("54");
          if (eclParamM.getParam().paramPortal.veranstaltungMehrfachAuswaehlbarJeGruppe==1) {
              setzeButtonRechts(false, "1726");
          }
          setzeLogoutButtons();
          break;

        case KonstPortalView.NUR_ANMELDUNG:
            setzeAppTitel("1416");
           setzeUeberschrift("1245");
            setzeStarttext("1242");
            setzeLogoutButtons();
            setzeButtonLinks("54");
            setzeButtonRechts(false, "1244");
            break;

        case KonstPortalView.NUR_ANMELDUNG_QUITTUNG:
            setzeAppTitel("1417");
            setzeUeberschrift("1249");
            setzeStarttext("1250");
            setzeLogoutButtons();
            setzeButtonRechts(false, "1251");
            break;

        case KonstPortalView.EINTRITTSKARTE:
            tSeitensteuerungSession.setEkvVollmachtEingeben(false);
            tSeitensteuerungSession.setAppTitelAnzeigen(false);
            
            setzeAppTitel("356");
            switch (tWillenserklaerungSession.getIntAusgewaehlteAktion()) {
            case KonstPortalAktion.EINE_EK_SELBST:
                setzeUeberschrift("56");
                setzeStarttext("360");

                setzeEkvStarttext("78");
                tSeitensteuerungSession.setEkvTextNummerVersandLautAktienregister("68"); //1
                tSeitensteuerungSession.setEkvTextNummerVersandAbweichendeAdresse("69"); //2
                tSeitensteuerungSession.setEkvTextNummerVersandOnlineAusdruck("66"); //3
                tSeitensteuerungSession.setEkvTextNummerVersandOnlineEmail("67"); //4

                tSeitensteuerungSession.setEkvTextNummerVorEmail("70");
                tSeitensteuerungSession.setEkvTextNummerVorEmailBestaetigen("71");

                tSeitensteuerungSession.setEkvTextNummerVorAbweichenderAdresse("72");
                tSeitensteuerungSession.setEkvTextNummerNachAbweichenderAdresse("73");

                break;
            case KonstPortalAktion.ZWEI_EK_SELBST_PERSGEMEINSCHAFT:
                setzeUeberschrift("60");
                setzeStarttext("65");
                
                /*TODO Unvollständig*/

               break;
            case KonstPortalAktion.EINE_EK_MIT_VOLLMACHT:
                setzeUeberschrift("58");
                setzeStarttext("63");

                tSeitensteuerungSession.setEkvVollmachtEingeben(true);
                setzeEkvVollmachtStarttext("329");
                tSeitensteuerungSession.setEkvTextNummerVorVollmachtName("75");
                tSeitensteuerungSession.setEkvTextNummerVorVollmachtVorname("76");
                tSeitensteuerungSession.setEkvTextNummerVorVollmachtOrt("77");
                setzeEkvVollmachtEndetext("359");

                setzeEkvStarttext("78");

                tSeitensteuerungSession.setEkvTextNummerVersandLautAktienregister("68"); //1
                tSeitensteuerungSession.setEkvTextNummerVersandAbweichendeAdresse("69"); //2
                tSeitensteuerungSession.setEkvTextNummerVersandOnlineAusdruck("66"); //3
                tSeitensteuerungSession.setEkvTextNummerVersandOnlineEmail("67"); //4

                tSeitensteuerungSession.setEkvTextNummerVorEmail("70");
                tSeitensteuerungSession.setEkvTextNummerVorEmailBestaetigen("71");

                tSeitensteuerungSession.setEkvTextNummerVorAbweichenderAdresse("72");
                tSeitensteuerungSession.setEkvTextNummerNachAbweichenderAdresse("73");

                break;
            case KonstPortalAktion.ZWEI_EK_SELBST_ODER_VOLLMACHT:
                setzeUeberschrift("59");
                setzeStarttext("64");
                
                /*TODO Unvollständig*/
               break;
            case KonstPortalAktion.ZWEI_EK_SELBST_FUER_ALLE:
                setzeUeberschrift("57");
                setzeStarttext("62");

                setzeEkvStarttext("361");
                tSeitensteuerungSession.setEkvTextNummerVersandLautAktienregister("366"); //1
                tSeitensteuerungSession.setEkvTextNummerVersandAbweichendeAdresse("367"); //2
                tSeitensteuerungSession.setEkvTextNummerVersandOnlineAusdruck("362"); //3
                tSeitensteuerungSession.setEkvTextNummerVersandOnlineEmail("363"); //4

                tSeitensteuerungSession.setEkvTextNummerVorEmail("364");
                tSeitensteuerungSession.setEkvTextNummerVorEmailBestaetigen("365");

                tSeitensteuerungSession.setEkvTextNummerVorAbweichenderAdresse("368");
                tSeitensteuerungSession.setEkvTextNummerNachAbweichenderAdresse("369");

                break;
                
            }

            setzeButtonLinks("54");
            setzeLogoutButtons();
            setzeButtonRechts(false, "74");

            break;

        case KonstPortalView.EINTRITTSKARTE_QUITTUNG:
            tSeitensteuerungSession.setEkvVollmachtEingeben(false);
            tSeitensteuerungSession.setEkvVersandadresseAnzeigen(false);
            tSeitensteuerungSession.setEkvEKDruck1ButtonAnzeigen(false);
            tSeitensteuerungSession.setEkvEKDruck2ButtonAnzeigen(false);
            tSeitensteuerungSession.setEkvEKMail1ButtonAnzeigen(false);
            tSeitensteuerungSession.setEkvEKMail2ButtonAnzeigen(false);

            setzeAppTitel("403");
            if (tWillenserklaerungSession.getIntAusgewaehlteHauptAktion()==KonstPortalAktion.HAUPT_NEUANMELDUNG) {
                setzeStarttext("80");
            }
            else {
                setzeStarttext("81");
            }
            switch (tWillenserklaerungSession.getIntAusgewaehlteAktion()) {
            case KonstPortalAktion.EINE_EK_SELBST:
                setzeUeberschrift("79");

                setzeEkvArtStarttext("403");

                switch (Integer.parseInt(tWillenserklaerungSession.getEintrittskarteVersandart())) {
                case KonstWillenserklaerungVersandartEK.VERSANDLAUF_ADRESSE_LAUT_AKTIENREGISTER:
                    setzeEkvQuittungVersandart("380");
                    break;
                case KonstWillenserklaerungVersandartEK.VERSANDLAUF_ADRESSE_ABWEICHEND_WIE_EINGEGEBEN:
                    setzeEkvQuittungVersandart("381");
                    tSeitensteuerungSession.setEkvVersandadresseAnzeigen(true);
                    break;
                case KonstWillenserklaerungVersandartEK.ONLINE_AUSDRUCK:
                    setzeEkvQuittungVersandart("376");
                    tSeitensteuerungSession.setEkvEKDruck1ButtonAnzeigen(true);
                    break;
                case KonstWillenserklaerungVersandartEK.ONLINE_EMAIL:
                    setzeEkvQuittungVersandart("378");
                    tSeitensteuerungSession.setEkvEKMail1ButtonAnzeigen(true);
                    break;
                }

                break;
            case KonstPortalAktion.ZWEI_EK_SELBST_PERSGEMEINSCHAFT:
                setzeUeberschrift("404");

                setzeEkvArtStarttext("405");

                /*TODO unvollständig*/
                break;
            case KonstPortalAktion.EINE_EK_MIT_VOLLMACHT:
                setzeUeberschrift("406");

                tSeitensteuerungSession.setEkvVollmachtEingeben(true);
                setzeEkvVollmachtStarttext("370");
                setzeEkvVollmachtEndetext("371");

                setzeEkvArtStarttext("407");

                switch (Integer.parseInt(tWillenserklaerungSession.getEintrittskarteVersandart())) {
                case KonstWillenserklaerungVersandartEK.VERSANDLAUF_ADRESSE_LAUT_AKTIENREGISTER:
                    setzeEkvQuittungVersandart("380");
                    break;
                case KonstWillenserklaerungVersandartEK.VERSANDLAUF_ADRESSE_ABWEICHEND_WIE_EINGEGEBEN:
                    setzeEkvQuittungVersandart("381");
                    tSeitensteuerungSession.setEkvVersandadresseAnzeigen(true);
                    break;
                case KonstWillenserklaerungVersandartEK.ONLINE_AUSDRUCK:
                    setzeEkvQuittungVersandart("376");
                    tSeitensteuerungSession.setEkvEKDruck1ButtonAnzeigen(true);
                    break;
                case KonstWillenserklaerungVersandartEK.ONLINE_EMAIL:
                    setzeEkvQuittungVersandart("378");
                    tSeitensteuerungSession.setEkvEKMail1ButtonAnzeigen(true);
                    break;
                }

                break;
            case KonstPortalAktion.ZWEI_EK_SELBST_ODER_VOLLMACHT:
                setzeUeberschrift("408");

                setzeEkvArtStarttext("409");

                /*TODO unvollständig*/
                break;
            case KonstPortalAktion.ZWEI_EK_SELBST_FUER_ALLE:
                setzeUeberschrift("410");

                setzeEkvArtStarttext("411");

                switch (Integer.parseInt(tWillenserklaerungSession.getEintrittskarteVersandart())) {
                case KonstWillenserklaerungVersandartEK.VERSANDLAUF_ADRESSE_LAUT_AKTIENREGISTER:
                    setzeEkvQuittungVersandart("388");
                    break;
                case KonstWillenserklaerungVersandartEK.VERSANDLAUF_ADRESSE_ABWEICHEND_WIE_EINGEGEBEN:
                    setzeEkvQuittungVersandart("389");
                    tSeitensteuerungSession.setEkvVersandadresseAnzeigen(true);
                    break;
                case KonstWillenserklaerungVersandartEK.ONLINE_AUSDRUCK:
                    setzeEkvQuittungVersandart("382");
                    tSeitensteuerungSession.setEkvEKDruck2ButtonAnzeigen(true);
                    break;
                case KonstWillenserklaerungVersandartEK.ONLINE_EMAIL:
                    setzeEkvQuittungVersandart("385");
                    tSeitensteuerungSession.setEkvEKMail2ButtonAnzeigen(true);
                    break;
                }

                break;
            }

            setzeLogoutButtons();
            setzeButtonRechts(false, "34");

            break;

        case KonstPortalView.EINTRITTSKARTE_DETAIL:
            tSeitensteuerungSession.setEkvVollmachtEingeben(false);
            tSeitensteuerungSession.setEkvVersandadresseAnzeigen(false);
            tSeitensteuerungSession.setEkvEKDruck1ButtonAnzeigen(false);
            tSeitensteuerungSession.setEkvEKMail1ButtonAnzeigen(false);

            setzeAppTitel("412");
            setzeUeberschrift("117");
            setzeStarttext("118");

            switch (tEintrittskarteQuittungUDetailSession.getArtEintrittskarte()) {
            case KonstEintrittskarteDetailArt.EINTRITTSKARTE:
                setzeEkvDetailArtStarttext("119");
                break;
            case KonstEintrittskarteDetailArt.EINTRITTSKARTE_MIT_VOLLMACHT:
                setzeEkvDetailArtStarttext("120");
                tSeitensteuerungSession.setEkvVollmachtEingeben(true);
                break;
            case KonstEintrittskarteDetailArt.GASTKARTE:
                setzeEkvDetailArtStarttext("126");
                break;
            }

            switch (Integer.parseInt(tWillenserklaerungSession.getEintrittskarteVersandart())) {
            case KonstWillenserklaerungVersandartEK.VERSANDLAUF_ADRESSE_LAUT_AKTIENREGISTER:
                setzeEkvArtStarttext("122");
                break;
            case KonstWillenserklaerungVersandartEK.VERSANDLAUF_ADRESSE_ABWEICHEND_WIE_EINGEGEBEN:
                setzeEkvArtStarttext("123");
                tSeitensteuerungSession.setEkvVersandadresseAnzeigen(true);
                break;
            case KonstWillenserklaerungVersandartEK.ONLINE_AUSDRUCK:
                setzeEkvArtStarttext("124");
                tSeitensteuerungSession.setEkvEKDruck1ButtonAnzeigen(true);
                break;
            case KonstWillenserklaerungVersandartEK.ONLINE_EMAIL:
                setzeEkvArtStarttext("125");
                tSeitensteuerungSession.setEkvEKDruck1ButtonAnzeigen(true);
                tSeitensteuerungSession.setEkvEKMail1ButtonAnzeigen(true);
                break;


            }

            setzeLogoutButtons();
            setzeButtonRechts(false, "34");

            break;

        case KonstPortalView.EINTRITTSKARTE_STORNIEREN:
            
            setzeAppTitel("413");
            

            switch (tWillenserklaerungSession.getIntAusgewaehlteAktion()) {
            case KonstPortalAktion.EK_STORNIEREN:
                setzeUeberschrift("151");
                setzeStarttext("153");
                                break;
            case KonstPortalAktion.EK_MIT_VOLLMACHT_STORNIEREN:
                setzeUeberschrift("152");
                setzeStarttext("154");
               break;
            case KonstPortalAktion.GK_STORNIEREN:
                setzeUeberschrift("414");
                setzeStarttext("416");
                break;
            }
  
            setzeButtonLinks("54");
            setzeLogoutButtons();
            setzeButtonRechts(false, "155");

            break;

        case KonstPortalView.EINTRITTSKARTE_STORNIEREN_QUITTUNG:

            setzeAppTitel("420");

            switch (tWillenserklaerungSession.getIntAusgewaehlteAktion()) {
            case KonstPortalAktion.EK_STORNIEREN:
                setzeUeberschrift("156");
                setzeStarttext("158");
                break;
            case KonstPortalAktion.EK_MIT_VOLLMACHT_STORNIEREN:
                setzeUeberschrift("157");
                setzeStarttext("159");
               break;
            case KonstPortalAktion.GK_STORNIEREN:
                setzeUeberschrift("421");
                setzeStarttext("422");
                break;
            }
  
            setzeLogoutButtons();
            setzeButtonRechts(false, "34");

            break;

        case KonstPortalView.WEISUNG:
            switch (tWillenserklaerungSession.getIntAusgewaehlteAktion()) {
            case KonstPortalAktion.SRV_NEU:
                setzeAppTitel("100");
                setzeUeberschrift("160");
                setzeStarttext("162");
                break;
            case KonstPortalAktion.BRIEFWAHL_NEU:
                setzeAppTitel("101");
              setzeUeberschrift("161");
                setzeStarttext("163");
                break;
            }

            setzeLogoutButtons();
            setzeButtonLinks("54");

            if (eclParamM.isBestaetigenDialog()) {
                /**Es kommt erst Bestätigungsseite*/
                setzeButtonRechts(false, "34");
            } else {
                /**Es wird direkt die Weisung erteilt, keine Bestätigung dazwischen*/
                switch (tWillenserklaerungSession.getIntAusgewaehlteAktion()) {
                case KonstPortalAktion.SRV_NEU:
                    setzeButtonRechts(false, "455");
                    break;
                case KonstPortalAktion.BRIEFWAHL_NEU:
                    setzeButtonRechts(false, "456");
                    break;
                }
            }
            break;

        case KonstPortalView.WEISUNG_BESTAETIGUNG:
            switch (tWillenserklaerungSession.getIntAusgewaehlteAktion()) {
            case KonstPortalAktion.SRV_NEU:
                setzeAppTitel("431");
               setzeUeberschrift("169");
                setzeStarttext("173");
                setzeButtonRechts(false, "174");
                break;
            case KonstPortalAktion.BRIEFWAHL_NEU:
                setzeAppTitel("432");
                setzeUeberschrift("170");
                setzeStarttext("111");
                setzeButtonRechts(false, "114");
                break;
            case KonstPortalAktion.SRV_AENDERN:
                setzeAppTitel("433");
                setzeUeberschrift("171");
                setzeStarttext("112");
                setzeButtonRechts(false, "175");
                break;
            case KonstPortalAktion.BRIEFWAHL_AENDERN:
                setzeAppTitel("434");
                setzeUeberschrift("172");
                setzeStarttext("113");
                setzeButtonRechts(false, "115");
                break;
            }

            setzeLogoutButtons();
            setzeButtonLinks("54");

            break;

        case KonstPortalView.WEISUNG_QUITTUNG:
            switch (tWillenserklaerungSession.getIntAusgewaehlteAktion()) {
            case KonstPortalAktion.SRV_NEU:
                setzeAppTitel("435");
                setzeUeberschrift("176");
                setzeStarttext("180");
                break;
            case KonstPortalAktion.BRIEFWAHL_NEU:
                setzeAppTitel("436");
                setzeUeberschrift("177");
                setzeStarttext("181");
                break;
            case KonstPortalAktion.SRV_AENDERN:
                setzeAppTitel("437");
               setzeUeberschrift("178");
                setzeStarttext("182");
                break;
            case KonstPortalAktion.BRIEFWAHL_AENDERN:
                setzeAppTitel("438");
               setzeUeberschrift("179");
                setzeStarttext("183");
                break;
            }

            setzeLogoutButtons();
            setzeButtonRechts(false, "34");

            break;

        case KonstPortalView.WEISUNG_STORNIEREN:
            switch (tWillenserklaerungSession.getIntAusgewaehlteAktion()) {
            case KonstPortalAktion.SRV_STORNIEREN:
                setzeAppTitel("459");
               setzeUeberschrift("184");
                setzeStarttext("186");
                setzeButtonRechts(false, "465");
                break;
            case KonstPortalAktion.BRIEFWAHL_STORNIEREN:
                setzeAppTitel("460");
                setzeUeberschrift("185");
                setzeStarttext("187");
                setzeButtonRechts(false, "466");
                break;
            }

            setzeButtonLinks("54");
            setzeLogoutButtons();

            break;

        case KonstPortalView.WEISUNG_STORNIEREN_QUITTUNG:
            switch (tWillenserklaerungSession.getIntAusgewaehlteAktion()) {
            case KonstPortalAktion.SRV_STORNIEREN:
                setzeAppTitel("467");
                setzeUeberschrift("189");
                setzeStarttext("191");
                break;
            case KonstPortalAktion.BRIEFWAHL_STORNIEREN:
                setzeAppTitel("468");
                setzeUeberschrift("190");
                setzeStarttext("192");
                break;
            }

            setzeLogoutButtons();
            setzeButtonRechts(false, "34");

            break;

        case KonstPortalView.WEISUNG_AENDERN:
            switch (tWillenserklaerungSession.getIntAusgewaehlteAktion()) {
            case KonstPortalAktion.SRV_AENDERN:
                setzeAppTitel("447");
                setzeUeberschrift("193");
                setzeStarttext("449");
                break;
            case KonstPortalAktion.BRIEFWAHL_AENDERN:
                setzeAppTitel("448");
                setzeUeberschrift("194");
                setzeStarttext("450");
                break;
            }

            setzeLogoutButtons();
            setzeButtonLinks("54");

            if (eclParamM.isBestaetigenDialog()) {
                /**Es kommt erst Bestätigungsseite*/
                setzeButtonRechts(false, "34");
            } else {
                /**Es wird direkt die Weisung erteilt, keine Bestätigung dazwischen*/
                switch (tWillenserklaerungSession.getIntAusgewaehlteAktion()) {
                case KonstPortalAktion.SRV_AENDERN:
                    setzeButtonRechts(false, "457");
                    break;
                case KonstPortalAktion.BRIEFWAHL_AENDERN:
                    setzeButtonRechts(false, "458");
                    break;
                }
            }

            break;

        case KonstPortalView.GENERALVERSAMMLUNG_ANABMELDEN:
//          setzeAppTitel("604");
            setzeUeberschrift("595");
            setzeStarttext("596");
            setzeButtonLinks("54");
            setzeButtonRechts(false, "603");
           setzeLogoutButtons();
            break;

        case KonstPortalView.AUSWAHL1_TEILNAHME:
//          setzeAppTitel("604");
            setzeUeberschrift("1047");
            setzeStarttext(""); //keiner vorhanden
            if (eclBesitzGesamtAuswahl1M.getAnzPraesenteVorhanden()==0) {
                setzeButtonLinks("1091");
            }
           setzeLogoutButtons();
            break;

        case KonstPortalView.STIMMABGABE_DERZEIT_NICHT_MOEGLICH:
//          setzeAppTitel("604");
            setzeUeberschrift("1209");
            setzeStarttext("1210"); 
            setzeButtonLinks("54");
            setzeButtonRechts(false, "1211");
            setzeLogoutButtons();
            break;

        case KonstPortalView.STIMMABGABE_ku310:
        case KonstPortalView.STIMMABGABE:
//          setzeAppTitel("604");
            setzeUeberschrift("1184");
            setzeStarttext("1185"); 
//            setzeButtonLinks("54");
            setzeButtonRechts(false, "1187");
            setzeLogoutButtons();
            break;

        case KonstPortalView.STIMMABGABE_QUITTUNG:
//          setzeAppTitel("604");
            setzeUeberschrift("1188");
            setzeStarttext("1189"); 
            setzeButtonRechts(false, "1191");
            setzeLogoutButtons();
            break;

        case KonstPortalView.AUSWAHL1_TEILNAHMEGAST:
//          setzeAppTitel("604");
            setzeUeberschrift("1112");
            setzeStarttext(""); //nicht vorhanden 
            if (eclBesitzGesamtAuswahl1M.isGastPraesent()==false) {
                setzeButtonLinks("1091");
            }
            setzeLogoutButtons();
            break;


        case KonstPortalView.STIMMABGABE_AUSWAHL:
//          setzeAppTitel("604");
            setzeUeberschrift("1176");
            setzeStarttext("1177");
            setzeButtonLinks("1180");
            setzeLogoutButtons();
            break;

        case KonstPortalView.PRAESENZ_ABGANG_BESTAETIGUNG:
//          setzeAppTitel("604");
            setzeUeberschrift("618");
            setzeStarttext("628");
            setzeButtonLinks("54");
            setzeButtonRechts(false, "630");
            setzeLogoutButtons();
            break;

        case KonstPortalView.PRAESENZ_ZUGANG_ABFRAGE_PERSON:
//          setzeAppTitel("604");
            setzeUeberschrift("1069");
            setzeStarttext("");
            setzeButtonLinks("54");
            setzeButtonRechts(false, "1072");
            setzeLogoutButtons();
            break;

        case KonstPortalView.PRAESENZ_ZUGANG_ABFRAGE_STORNIERUNG:
//          setzeAppTitel("604");
            setzeUeberschrift("1124");
            setzeStarttext("");
            setzeButtonLinks("54");
            setzeButtonRechts(false, "1125");
            setzeLogoutButtons();
            break;

        case KonstPortalView.PRAESENZ_ZUGANG_FALSCHE_PERSON:
//          setzeAppTitel("604");
            setzeUeberschrift("1073");
            setzeStarttext("1074");
            setzeButtonLinks("54");
            setzeButtonRechts(false, "1075");
            setzeLogoutButtons();
            break;

        case KonstPortalView.PRASESENZ_ZUGANG_PERSON_BESTAETIGEN:
//          setzeAppTitel("604");
            setzeUeberschrift("1076");
            setzeStarttext("");
            setzeButtonLinks("54");
            setzeLogoutButtons();
            break;

        case KonstPortalView.GENERALVERSAMMLUNG_BRIEFW_MITGLIED_DURCH_BEVOLL_STORNO:
//          setzeAppTitel("604");
            setzeUeberschrift("1096");
            setzeStarttext("1097");
            setzeButtonLinks("54");
            setzeButtonRechts(false, "1098");
           setzeLogoutButtons();
            break;

        case KonstPortalView.GENERALVERSAMMLUNG_VOLLMACHT_STORNIEREN:
//          setzeAppTitel("604");
            setzeUeberschrift("993");
            setzeStarttext("994");
            setzeButtonLinks("54");
            setzeButtonRechts(false, "996");
            setzeLogoutButtons();
            break;

            
        case KonstPortalView.P_LOGIN:
            this.setzeLogoutButtonsOff();
            break;

        case KonstPortalView.P_REGISTRIEREN:
            this.setzeLogoutButtonsOff();
            break;

        case KonstPortalView.P_REGISTRIEREN_QUITTUNG:
            this.setzeLogoutButtonsOff();
            break;

        case KonstPortalView.ZUORDNUNG_AUFHEBEN:
//          setzeAppTitel("604");
            setzeUeberschrift("2154");
            setzeStarttext("2155");
            setzeButtonLinks("54");
            setzeButtonRechts(false, "2156");
            setzeLogoutButtons();
            break;

        case KonstPortalView.ZUORDNUNG_EINRICHTEN:
//          setzeAppTitel("604");
            setzeUeberschrift("2162");
            setzeStarttext("2163");
            setzeButtonLinks("54");
            setzeButtonRechts(false, "2164");
            setzeLogoutButtons();
            break;

        case KonstPortalView.ZUORDNUNG_EINRICHTEN_QUITTUNG:
//          setzeAppTitel("604");
            setzeUeberschrift("2168");
            setzeStarttext("2169");
            setzeButtonRechts(false, "2170");
            setzeLogoutButtons();
            break;

        case KonstPortalView.GASTKARTE_UEBERSICHT:
//          setzeAppTitel("604");
            setzeUeberschrift("2261");
            setzeStarttext("2262");
            setzeButtonLinks("54");
//            setzeButtonRechts(false, "2170");
            setzeLogoutButtons();
            break;

        case KonstPortalView.GASTKARTE_EINGABE:
//          setzeAppTitel("604");
            setzeUeberschrift("2269");
            setzeStarttext("2270");
            setzeButtonLinks("2271");
            setzeButtonRechts(false, "2272");
            setzeLogoutButtons();
            break;

        default: 
            setzeLogoutButtons();
        }

        tSeitensteuerungSession.logAusgabe();
    }

    /**++++++++++++++++++++++++Funktionen Startbereich - Überschrift++++++++++++*/
    public String holeTextUeberschrift() {
        return eclPortalTexteM.holeText(tSeitensteuerungSession.getTextNummerUeberschrift());
    }

    public String holeTextAppTitel() {
        if (tSeitensteuerungSession.isAppTitelAnzeigen()) {
            return eclPortalTexteM.holeText(tSeitensteuerungSession.getTextNummerAppTitel());
        }
        return "noch nicht programmiert";
    }

    /**++++++++++++++++++++++++Funktionen Startbereich - Startbereich++++++++++*/
    public String holeTextStarttext() {
        return eclPortalTexteM.holeText(tSeitensteuerungSession.getTextNummerStarttext());
    }

    /**+++++++++++++++++++Funktionen für linken Button+++++++++++++++++*/
    public String holeTextLinkerButton() {
        return eclPortalTexteM.holeText(tSeitensteuerungSession.getTextNummerLinkerButton());
    }

    public void doLinkerButton() {
        switch (tSession.getViewnummer()) {
        case KonstPortalView.NUR_ANMELDUNG:
            tAnmeldenOhneErklaerung.doAnmeldenOhneWKZurueck();
            break;
        case KonstPortalView.EINSTELLUNGEN_BESTAETIGUNG:
            tEinstellungen.doBestaetigungZurueck();
            break;
        case KonstPortalView.P_EINSTELLUNGEN_BESTAETIGUNG:
            tEinstellungen.doBestaetigungZurueck();
            break;
        case KonstPortalView.PASSWORT_VERGESSEN:
            tPasswortVergessen.doZumLogin();;
            break;
        case KonstPortalView.P_PASSWORT_VERGESSEN:
            tPasswortVergessen.doPZumLogin();;
            break;
        case KonstPortalView.STREAM_START:
            break;
        case KonstPortalView.UNTERLAGEN:
            tUnterlagen.doZurueck();
            break;
        case KonstPortalView.BOTSCHAFTEN_ANZEIGEN:
            tUnterlagen.doZurueck();
            break;
        case KonstPortalView.FRAGEN:
        case KonstPortalView.RUECKFRAGEN:
        case KonstPortalView.WORTMELDUNGEN:
        case KonstPortalView.WIDERSPRUECHE:
        case KonstPortalView.ANTRAEGE:
        case KonstPortalView.SONSTIGEMITTEILUNGEN:
        case KonstPortalView.BOTSCHAFTEN_EINREICHEN:
            tMitteilung.doZurueck();
            break;
        case KonstPortalView.TEILNEHMERVERZEICHNIS:
            tTeilnehmerverz.doZurueck();
            break;
        case KonstPortalView.ABSTIMMUNGSERGEBNISSE:
            tAbstimmungserg.doZurueck();
            break;
        case KonstPortalView.MONITORING:
            tMonitoring.doZurueck();
            break;
     case KonstPortalView.EINTRITTSKARTE:
            tEintrittskarte.doZurueck();
            break;
        case KonstPortalView.EINTRITTSKARTE_STORNIEREN:
            tEintrittskarteStornieren.doZurueck();
            break;
        case KonstPortalView.WEISUNG:
            tWeisung.doZurueck();
            break;
        case KonstPortalView.WEISUNG_BESTAETIGUNG:
            if (tWillenserklaerungSession.getIntAusgewaehlteAktion() == KonstPortalAktion.SRV_NEU
                    || tWillenserklaerungSession.getIntAusgewaehlteAktion() == KonstPortalAktion.BRIEFWAHL_NEU) {
                tWeisungBestaetigung.doZurueck();
            }
            if (tWillenserklaerungSession.getIntAusgewaehlteAktion() == KonstPortalAktion.SRV_AENDERN
                    || tWillenserklaerungSession.getIntAusgewaehlteAktion() == KonstPortalAktion.BRIEFWAHL_AENDERN) {
                tWeisungBestaetigung.doZurueckAendern();
            }
            break;
        case KonstPortalView.WEISUNG_STORNIEREN:
            tWeisungStornieren.doZurueck();
            break;
        case KonstPortalView.WEISUNG_AENDERN:
            tWeisungAendern.doZurueck();
            break;
        case KonstPortalView.AUSWAHL1_GENERALVERSAMMLUNG:
            tAuswahl1.doGeneralversammlungZurueck();
            break;
        case KonstPortalView.AUSWAHL1_GENERALVERSAMMLUNG_BRIEFWAHL:
            tAuswahl1.doGeneralversammlungBriefwahlZurueck();
            break;
        case KonstPortalView.AUSWAHL1_GENERALVERSAMMLUNG_BEIRATSWAHL:
            tAuswahl1.doGeneralversammlungBeiratswahlZurueck();
            break;
       case KonstPortalView.GENERALVERSAMMLUNG_ANABMELDEN:
            tGeneralversammlung.doZurueck();
            break;
        case KonstPortalView.AUSWAHL1_TEILNAHME:
            tAuswahl1Teilnahme.doZurueck();
            break;
        case KonstPortalView.AUSWAHL1_DIALOGVERANSTALTUNGEN:
            tAuswahl1.doDialogveranstaltungenZurueck();
            break;
        case KonstPortalView.AUSWAHL1_ANMELDEN_DIALOGVERANSTALTUNGEN:
            tDialogveranstaltungen.doZurueck();
            break;

        case KonstPortalView.STIMMABGABE_DERZEIT_NICHT_MOEGLICH:
            tStimmabgabe.doNichtMoeglichZurueck();
            break;
        case KonstPortalView.STIMMABGABE:
            tStimmabgabe.doEingabeZurueck();
            break;
        case KonstPortalView.AUSWAHL1_TEILNAHMEGAST:
            tAuswahl1Teilnahme.doZurueckGast();
            break;
        case KonstPortalView.STIMMABGABE_AUSWAHL:
            tStimmabgabe.doBeendenStimmabgabeAuswahl();
            break;
        case KonstPortalView.PRAESENZ_ABGANG_BESTAETIGUNG:
            tPraesenzZugangAbgang.doAbgangZurueck();
            break;
        case KonstPortalView.PRAESENZ_ZUGANG_ABFRAGE_PERSON:
            tPraesenzZugangAbgang.doPersonAuswaehlenZurueck();
            break;
        case KonstPortalView.PRAESENZ_ZUGANG_ABFRAGE_STORNIERUNG:
            tPraesenzZugangAbgang.doAbfrageStornierungZurueck();
            break;
        case KonstPortalView.PRAESENZ_ZUGANG_FALSCHE_PERSON:
            tPraesenzZugangAbgang.doFalschePersonZurueck();
            break;
        case KonstPortalView.PRASESENZ_ZUGANG_PERSON_BESTAETIGEN:
            tPraesenzZugangAbgang.doPersonBestaetigenZurueck();
            break;
        case KonstPortalView.GENERALVERSAMMLUNG_BRIEFW_MITGLIED_DURCH_BEVOLL_STORNO:
            tGeneralversammlung.doZurueckBriefwMitglStornoDurchBevoll();
            break;
        case KonstPortalView.GENERALVERSAMMLUNG_VOLLMACHT_STORNIEREN:
            tGeneralversammlung.doZurueckStornieren();
            break;
        case KonstPortalView.ZUORDNUNG_AUFHEBEN:
            tZuordnung.doZurueckZuordnungAufheben();
            break;
        case KonstPortalView.ZUORDNUNG_EINRICHTEN:
            tZuordnung.doZurueckZuordnungEinrichten();
            break;
        case KonstPortalView.GASTKARTE_UEBERSICHT:
            tGaeste.doZurueckUebersicht();
            break;
        case KonstPortalView.GASTKARTE_EINGABE:
            tGaeste.doZurueckEingabe();
            break;
      }
    }

    /**++++++++++++++Funktionen für rechten Button++++++++++++++++++++++++*/
    public String holeTextRechterButton() {
        return eclPortalTexteM.holeText(tSeitensteuerungSession.getTextNummerRechterButton());
    }

    public void doRechterButton() {
        CaBug.druckeLog("", logDrucken, 2);
        switch (tSession.getViewnummer()) {
        
        case KonstPortalView.FEHLER_DIALOG:
            tFehlerView.doFehlerDialogZumLogin();
            break;
        case KonstPortalView.FEHLER_VERAENDERT:
            tFehlerView.doFehlerVeraendertZumLogin();
            break;
        case KonstPortalView.FEHLER_VIEW:
            tFehlerView.doZumNextView();
            break;
      case KonstPortalView.BESTAETIGEN:
            tBestaetigen.doBestaetigen();
            break;
        case KonstPortalView.BESTAETIGEN2:
            tBestaetigen.doBestaetigen2();
            break;
        case KonstPortalView.PW_ZURUECK:
            tPasswortVergessen.doSpeichern();;
            break;
 
        case KonstPortalView.PASSWORT_VERGESSEN:
            tPasswortVergessen.doPasswortZuruecksetzen();
            break;
        case KonstPortalView.P_PASSWORT_VERGESSEN:
            tPasswortVergessen.doPPasswortZuruecksetzen();
            break;
 
         case KonstPortalView.EINSTELLUNGEN:
            tEinstellungen.doWeiter();
            break;
         case KonstPortalView.P_EINSTELLUNGEN:
             tEinstellungen.doWeiter();
             break;
        case KonstPortalView.EINSTELLUNGEN_BESTAETIGUNG:
            tEinstellungen.doBestaetigungWeiter();
            break;
        case KonstPortalView.P_EINSTELLUNGEN_BESTAETIGUNG:
            tEinstellungen.doBestaetigungWeiter();
            break;
           
        case KonstPortalView.TEILNEHMERVERZEICHNIS:
            tTeilnehmerverz.doRefresh();
            break;
        case KonstPortalView.ABSTIMMUNGSERGEBNISSE:
            tAbstimmungserg.doRefresh();
            break;

        case KonstPortalView.MONITORING:
            tMonitoring.doRefresh();
            break;
        case KonstPortalView.AUSWAHL1_ANMELDEN_DIALOGVERANSTALTUNGEN:
            tDialogveranstaltungen.doAnmeldenMulti();
            break;

        case KonstPortalView.NUR_ANMELDUNG:
            tAnmeldenOhneErklaerung.doAnmeldenOhneWKAusfuehren();
            break;
        case KonstPortalView.NUR_ANMELDUNG_QUITTUNG:
            tAnmeldenOhneErklaerung.doAnmeldenOhneWKQuittungWeiter();
            break;
        case KonstPortalView.EINTRITTSKARTE:
            tEintrittskarte.doAusstellen();
            break;
        case KonstPortalView.EINTRITTSKARTE_QUITTUNG:
            tEintrittskarteQuittungUDetail.doQuittungWeiter();
            break;
        case KonstPortalView.EINTRITTSKARTE_STORNIEREN:
            tEintrittskarteStornieren.doStornieren();
            break;
        case KonstPortalView.EINTRITTSKARTE_STORNIEREN_QUITTUNG:
            tEintrittskarteStornieren.doWeiterQuittung();
            break;
        case KonstPortalView.EINTRITTSKARTE_DETAIL:
            tEintrittskarteQuittungUDetail.doDetailWeiter();
            break;

        case KonstPortalView.WEISUNG:
            tWeisung.doWeiter();
            break;
        case KonstPortalView.WEISUNG_BESTAETIGUNG:
            if (tWillenserklaerungSession.getIntAusgewaehlteAktion() == KonstPortalAktion.SRV_NEU
                    || tWillenserklaerungSession.getIntAusgewaehlteAktion() == KonstPortalAktion.BRIEFWAHL_NEU) {
                tWeisungBestaetigung.doErteilen();
            }
            if (tWillenserklaerungSession.getIntAusgewaehlteAktion() == KonstPortalAktion.SRV_AENDERN
                    || tWillenserklaerungSession.getIntAusgewaehlteAktion() == KonstPortalAktion.BRIEFWAHL_AENDERN) {
                tWeisungBestaetigung.doAendernSpeichern();
            }
            break;
        case KonstPortalView.WEISUNG_QUITTUNG:
            tWeisungQuittung.doWeiter();
            break;
        case KonstPortalView.WEISUNG_STORNIEREN:
            tWeisungStornieren.doStornieren();
            break;
        case KonstPortalView.WEISUNG_STORNIEREN_QUITTUNG:
            tWeisungStornierenQuittung.doWeiter();
            break;
        case KonstPortalView.WEISUNG_AENDERN:
            tWeisungAendern.doWeiter();
            break;

        case KonstPortalView.GENERALVERSAMMLUNG_ANABMELDEN:
            tGeneralversammlung.doWeiter();
            break;

        case KonstPortalView.STIMMABGABE_DERZEIT_NICHT_MOEGLICH:
            tStimmabgabe.doNichtMoeglichAktualisieren();
            break;
        case KonstPortalView.STIMMABGABE:
            tStimmabgabe.doStimmeAbgeben();
            break;
        case KonstPortalView.STIMMABGABE_ku310:
            tStimmabgabeku310.doWeiter();
            break;
        case KonstPortalView.STIMMABGABE_QUITTUNG:
            tStimmabgabe.doWeiterQuittung();
            break;
        case KonstPortalView.PRAESENZ_ABGANG_BESTAETIGUNG:
            tPraesenzZugangAbgang.doAbgangWeiter();
            break;
        case KonstPortalView.PRAESENZ_ZUGANG_ABFRAGE_PERSON:
            tPraesenzZugangAbgang.doPersonAuswaehlen();
            break;
        case KonstPortalView.PRAESENZ_ZUGANG_ABFRAGE_STORNIERUNG:
            tPraesenzZugangAbgang.doAbfrageStornierungWeiter();
            break;
        case KonstPortalView.PRAESENZ_ZUGANG_FALSCHE_PERSON:
            tPraesenzZugangAbgang.doFalschePersonWeiter();
            break;
        case KonstPortalView.GENERALVERSAMMLUNG_BRIEFW_MITGLIED_DURCH_BEVOLL_STORNO:
            tGeneralversammlung.doStornierenBestaetigenBriefwMitglStornoDurchBevoll();
            break;
        case KonstPortalView.GENERALVERSAMMLUNG_VOLLMACHT_STORNIEREN:
            tGeneralversammlung.doStornierenBestaetigen();
            break;
        case KonstPortalView.ZUORDNUNG_AUFHEBEN:
            tZuordnung.doAusfuehrenZuordnungAufheben();
            break;
        case KonstPortalView.ZUORDNUNG_EINRICHTEN:
            tZuordnung.doAusfuehrenZuordnungEinrichten();
            break;
        case KonstPortalView.ZUORDNUNG_EINRICHTEN_QUITTUNG:
            tZuordnung.doAusfuehrenZuordnungEinrichtenQuittung();
            break;

        case KonstPortalView.GASTKARTE_EINGABE:
            tGaeste.doSpeichernNeuerGast();
            break;
        }
    }

    /*+++++++++++++++++++Eintrittskartenausstellung+++++++++++++++++++++++*/

    public String holeTextEkvTextArtStarttext() {
        return eclPortalTexteM.holeText(tSeitensteuerungSession.getEkvTextNummerArtStarttext());
    }

    public String holeTextEkvTextDetailArtStarttext() {
        return eclPortalTexteM.holeText(tSeitensteuerungSession.getEkvTextNummerDetailArtStarttext());
    }

    public String holeTextEkvTextQuittungVersandart() {
        return eclPortalTexteM.holeText(tSeitensteuerungSession.getEkvTextNummerQuittungVersandart());
    }

    public String holeTextEkvTextVollmachtStarttext() {
        return eclPortalTexteM.holeText(tSeitensteuerungSession.getEkvTextNummerVollmachtStarttext());
    }

    public String holeTextEkvTextVollmachtEndetext() {
        return eclPortalTexteM.holeText(tSeitensteuerungSession.getEkvTextNummerVollmachtEndetext());
    }

    public String holeTextEkvTextStarttext() {
        return eclPortalTexteM.holeText(tSeitensteuerungSession.getEkvTextNummerStarttext());
    }

    public String holeTextEkvTextVorVollmachtName() {
        return eclPortalTexteM.holeText(tSeitensteuerungSession.getEkvTextNummerVorVollmachtName());
    }

    public String holeTextEkvTextVorVollmachtVorname() {
        return eclPortalTexteM.holeText(tSeitensteuerungSession.getEkvTextNummerVorVollmachtVorname());
    }

    public String holeTextEkvTextVorVollmachtOrt() {
        return eclPortalTexteM.holeText(tSeitensteuerungSession.getEkvTextNummerVorVollmachtOrt());
    }

    public String holeTextEkvTextVersandLautAktienregister() {
        return eclPortalTexteM.holeText(tSeitensteuerungSession.getEkvTextNummerVersandLautAktienregister());
    }

    public String holeTextEkvTextVersandAbweichendeAdresse() {
        return eclPortalTexteM.holeText(tSeitensteuerungSession.getEkvTextNummerVersandAbweichendeAdresse());
    }

    public String holeTextEkvTextVersandOnlineAusdruck() {
        return eclPortalTexteM.holeText(tSeitensteuerungSession.getEkvTextNummerVersandOnlineAusdruck());
    }

    public String holeTextEkvTextVersandOnlineEmail() {
        return eclPortalTexteM.holeText(tSeitensteuerungSession.getEkvTextNummerVersandOnlineEmail());
    }

    public String holeTextEkvTextVorEmail() {
        return eclPortalTexteM.holeText(tSeitensteuerungSession.getEkvTextNummerVorEmail());
    }

    public String holeTextEkvTextVorEmailBestaetigen() {
        return eclPortalTexteM.holeText(tSeitensteuerungSession.getEkvTextNummerVorEmailBestaetigen());
    }

    public String holeTextEkvTextVorAbweichenderAdresse() {
        return eclPortalTexteM.holeText(tSeitensteuerungSession.getEkvTextNummerVorAbweichenderAdresse());
    }

    public String holeTextEkvTextNachAbweichenderAdresse() {
        return eclPortalTexteM.holeText(tSeitensteuerungSession.getEkvTextNummerNachAbweichenderAdresse());
    }

    /*****************Interne Funktionen****************************************/
    private void setzeUeberschrift(String pTextNrUeberschrift) {
        if (eclPortalTexteM.pruefeTextVorhanden(pTextNrUeberschrift)) {
            tSeitensteuerungSession.setUeberschriftAnzeigen(true);
            tSeitensteuerungSession.setTextNummerUeberschrift(pTextNrUeberschrift);
        }
    }

    private void setzeAppTitel(String pTextNrAppTitel) {
        if (!pTextNrAppTitel.isBlank() && eclPortalTexteM.pruefeTextVorhanden(pTextNrAppTitel)) {
            tSeitensteuerungSession.setAppTitelAnzeigen(true);
            tSeitensteuerungSession.setTextNummerAppTitel(pTextNrAppTitel);
        }
        else {
            tSeitensteuerungSession.setAppTitelAnzeigen(false);
        }
    }

    private void setzeStarttext(String pTextNrUeberschrift) {
        if (!pTextNrUeberschrift.isEmpty() && eclPortalTexteM.pruefeTextVorhanden(pTextNrUeberschrift)) {
            tSeitensteuerungSession.setStarttextAnzeigen(true);
            tSeitensteuerungSession.setTextNummerStarttext(pTextNrUeberschrift);
        }
    }

    private void setzeButtonLinks(String pTextNummerLinkerButton) {
        tSeitensteuerungSession.setLinkenButtonAnzeigen(true);
        tSeitensteuerungSession.setTextNummerLinkerButton(pTextNummerLinkerButton);
    }

    private void setzeLogoutButtonsOff() {
        tSeitensteuerungSession.setLogoutAnzeigen(false);
        tSeitensteuerungSession.setLogoutSeiteVerlassenAnzeigen(false);
        tSeitensteuerungSession.setLogoutNormalAnzeigen(false);
        tSeitensteuerungSession.setEndeButtonAnzeigen(false);
    }
    private void setzeLogoutButtonsLoginSeite() {
        /**Logout-Buttons*/
        if (eclParamM.isEndeButtonAufLogin()) {
            tSeitensteuerungSession.setLogoutAnzeigen(true);
            tSeitensteuerungSession.setLogoutSeiteVerlassenAnzeigen(false);
            tSeitensteuerungSession.setLogoutNormalAnzeigen(false);
            tSeitensteuerungSession.setEndeButtonAnzeigen(true);
        } else {
            tSeitensteuerungSession.setLogoutAnzeigen(false);
            tSeitensteuerungSession.setLogoutSeiteVerlassenAnzeigen(false);
            tSeitensteuerungSession.setLogoutNormalAnzeigen(false);
            tSeitensteuerungSession.setEndeButtonAnzeigen(false);
        }
    }

    private void setzeLogoutButtons() {
        /**Logout-Buttons*/
        if (eclParamM.isEndeButtonAufLogin()) {
            tSeitensteuerungSession.setLogoutAnzeigen(true);
            tSeitensteuerungSession.setLogoutSeiteVerlassenAnzeigen(true);
            tSeitensteuerungSession.setLogoutNormalAnzeigen(false);
            tSeitensteuerungSession.setEndeButtonAnzeigen(false);
        } else {
            tSeitensteuerungSession.setLogoutAnzeigen(true);
            tSeitensteuerungSession.setLogoutSeiteVerlassenAnzeigen(false);
            tSeitensteuerungSession.setLogoutNormalAnzeigen(true);
            tSeitensteuerungSession.setEndeButtonAnzeigen(false);
        }
    }

    private void setzeButtonRechts(boolean pRenderKomplett, String pTextNummerRechterButton) {
        tSeitensteuerungSession.setRechtenButtonAnzeigen(true);
        tSeitensteuerungSession.setRechterButtonRenderKomplett(pRenderKomplett);
        tSeitensteuerungSession.setTextNummerRechterButton(pTextNummerRechterButton);
    }

    private void setzeEkvArtStarttext(String pTextNrEkvArtStarttext) {
        if (eclPortalTexteM.pruefeTextVorhanden(pTextNrEkvArtStarttext)) {
            tSeitensteuerungSession.setEkvArtStarttextAnzeigen(true);
            tSeitensteuerungSession.setEkvTextNummerArtStarttext(pTextNrEkvArtStarttext);
        } else {
            tSeitensteuerungSession.setEkvArtStarttextAnzeigen(false);
        }
    }

    private void setzeEkvDetailArtStarttext(String pTextNrEkvDetailArtStarttext) {
        if (eclPortalTexteM.pruefeTextVorhanden(pTextNrEkvDetailArtStarttext)) {
            tSeitensteuerungSession.setEkvDetailArtStarttextAnzeigen(true);
            tSeitensteuerungSession.setEkvTextNummerDetailArtStarttext(pTextNrEkvDetailArtStarttext);
        } else {
            tSeitensteuerungSession.setEkvDetailArtStarttextAnzeigen(false);
        }
    }

    private void setzeEkvQuittungVersandart(String pTextNrQuittungVersandart) {
        if (eclPortalTexteM.pruefeTextVorhanden(pTextNrQuittungVersandart)) {
            tSeitensteuerungSession.setEkvQuittungVersandartAnzeigen(true);
            tSeitensteuerungSession.setEkvTextNummerQuittungVersandart(pTextNrQuittungVersandart);
        } else {
            tSeitensteuerungSession.setEkvQuittungVersandartAnzeigen(false);
        }
    }

    private void setzeEkvVollmachtStarttext(String pTextNrEkvVollmachtStarttext) {
        if (eclPortalTexteM.pruefeTextVorhanden(pTextNrEkvVollmachtStarttext)) {
            tSeitensteuerungSession.setEkvVollmachtStarttextAnzeigen(true);
            tSeitensteuerungSession.setEkvTextNummerVollmachtStarttext(pTextNrEkvVollmachtStarttext);
        } else {
            tSeitensteuerungSession.setEkvVollmachtStarttextAnzeigen(false);
        }
    }

    private void setzeEkvVollmachtEndetext(String pTextNrEkvVollmachtEndetext) {
        if (eclPortalTexteM.pruefeTextVorhanden(pTextNrEkvVollmachtEndetext)) {
            tSeitensteuerungSession.setEkvVollmachtEndetextAnzeigen(true);
            tSeitensteuerungSession.setEkvTextNummerVollmachtEndetext(pTextNrEkvVollmachtEndetext);
        } else {
            tSeitensteuerungSession.setEkvVollmachtEndetextAnzeigen(false);
        }
    }

    private void setzeEkvStarttext(String pTextNrEkvStarttext) {
        if (eclPortalTexteM.pruefeTextVorhanden(pTextNrEkvStarttext)) {
            tSeitensteuerungSession.setEkvStarttextAnzeigen(true);
            tSeitensteuerungSession.setEkvTextNummerStarttext(pTextNrEkvStarttext);
        } else {
            tSeitensteuerungSession.setEkvStarttextAnzeigen(false);
        }
    }

}
