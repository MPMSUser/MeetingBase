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
package de.meetingapps.meetingportal.meetingport;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComAllg.CaString;
import de.meetingapps.meetingportal.meetComBa.BaMailM;
import de.meetingapps.meetingportal.meetComBl.BlAktienregisterNummerAufbereiten;
import de.meetingapps.meetingportal.meetComBl.BlGastkarte;
import de.meetingapps.meetingportal.meetComBl.BlNummernformen;
import de.meetingapps.meetingportal.meetComBl.BlWillenserklaerung;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEclM.EclDbM;
import de.meetingapps.meetingportal.meetComEclM.EclParamM;
import de.meetingapps.meetingportal.meetComEclM.EclPortalTexteM;
import de.meetingapps.meetingportal.meetComEclM.EclTeilnehmerLoginM;
import de.meetingapps.meetingportal.meetComEclM.EclTeilnehmerVersandadresse2M;
import de.meetingapps.meetingportal.meetComEclM.EclTeilnehmerVersandadresseM;
import de.meetingapps.meetingportal.meetComEclM.EclZugeordneteMeldungM;
import de.meetingapps.meetingportal.meetComEntities.EclAktienregister;
import de.meetingapps.meetingportal.meetComEntities.EclAnrede;
import de.meetingapps.meetingportal.meetComEntities.EclLoginDaten;
import de.meetingapps.meetingportal.meetComEntities.EclMeldung;
import de.meetingapps.meetingportal.meetComEntities.EclPersonenNatJur;
import de.meetingapps.meetingportal.meetComEntities.EclPersonenNatJurVersandadresse;
import de.meetingapps.meetingportal.meetComEntities.EclWillenserklaerung;
import de.meetingapps.meetingportal.meetComEntities.EclWillenserklaerungZusatz;
import de.meetingapps.meetingportal.meetComEntities.EclZutrittsIdent;
import de.meetingapps.meetingportal.meetComEntities.EclZutrittskarten;
import de.meetingapps.meetingportal.meetComKonst.KonstKartenart;
import de.meetingapps.meetingportal.meetComKonst.KonstKartenklasse;
import de.meetingapps.meetingportal.meetComKonst.KonstWillenserklaerung;
import de.meetingapps.meetingportal.meetingCoreReport.RpDrucken;
import de.meetingapps.meetingportal.meetingCoreReport.RpVariablen;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@RequestScoped
@Named
@Deprecated
public class AControllerEintrittskarte {

    private int logDrucken = 10;

    //	private DbBundle lDbBundle=null;

    @Inject
    private ADlgVariablen aDlgVariablen;
    @Inject
    private AFunktionen aFunktionen;
    @Inject
    private EclPortalTexteM eclPortalTexteM;
    @Inject
    private EclDbM eclDbM;

    @Inject
    private EclTeilnehmerLoginM eclTeilnehmerLoginM;
    @Inject
    private EclTeilnehmerVersandadresseM eclTeilnehmerVersandadresseM;
    @Inject
    private EclTeilnehmerVersandadresse2M eclTeilnehmerVersandadresse2M;
    @Inject
    private EclZugeordneteMeldungM eclZugeordneteMeldungM;
    @Inject
    private BaMailM baMailm;
    @Inject
    private EclParamM eclParamM;

    /**Wird während dem Ausstellen der Eintrittskarte gefüllt. Wird ggf. später für das
     * Ausstellen der Gastkarte wieder benötigt! Deshalb hier so relativ global ...
     * 
     * Es wird davon ausgegangen, dass dies aktuell nicht Session-mäßig erforderlich ist ...
     */
    private EclAktienregister aktienregisterEintrag = null;
    //	private EclAktienregisterLoginDaten aktienregisterLoginDaten=null;
    private EclLoginDaten loginDaten = null;
    //	private EclAktienregisterZusatz aktienregisterZusatz=null;

    private String quelle = "";

    public String doZurueck() {
        String naechsteMaske = "";
        if (!aFunktionen.pruefeStart("aEintrittskarte")) {
            aFunktionen.setzeEnde();
            return "aDlgFehler";
        }
        if (aDlgVariablen.getAusgewaehlteHauptAktion().compareTo("1") == 0) {
            naechsteMaske = "aAnmeldenEK";
        } else {
            naechsteMaske = "aNeueWillenserklaerungEK";
        }

        aDlgVariablen.clearDlgOhneGastkarteAnforderung();
        return aFunktionen.setzeEnde(naechsteMaske, true, false);

    }

    /********************Funktionen für Gastkarte****************************************************/

    /**Für Gastkarte: prüfen, ob alle Eingabefelder richtig und vollständig gefüllt sind
     * 
     * Eingabefelder:
     * 		DlgVariablen.gastkarteVersandart
     * 		DlgVariablen.gastkarteEmail (je nach Versandart)
     * 		DlgVariablen.gastkarteName
     * 		DlgVariablen.gastkarteOrt
     * 
     * Returnwert: 	true = ok, 
     * 				false= Daten sind falsch, Fehlermeldung in DlgVariablen.fehlerMeldung/.fehlerNr
     * */
    public boolean pruefeEingabenFuerGastkarte() {
        if (aDlgVariablen.getGastkarteVersandart() == null || aDlgVariablen.getGastkarteVersandart().isEmpty()) {
            aDlgVariablen.setFehlerMeldung(eclPortalTexteM.getFehlertext(CaFehler.afAusstellungsartGastkarteFehlt));
            aDlgVariablen.setFehlerNr(CaFehler.afAusstellungsartGastkarteFehlt);
            return false;
        }
        switch (aDlgVariablen.getGastkarteVersandart()) {
        case "2":/*Aufnahme in Sammelbatch, an Versandadresse - bei nächstem Drucklauf ausdrucken und versenden => abweichende Versandadresse gültig?*/
            /*Tja - wie kann man die Versandadresse überprüfen?*/
            break;
        case "4":/*Versand per Email (im Portal) erfolgt => Mailadresse gültig?*/
            if (aDlgVariablen.getGastkarteEmail() == null || aDlgVariablen.getGastkarteEmail().isEmpty()
                    || !CaString.isMailadresse(aDlgVariablen.getGastkarteEmail())) {
                aDlgVariablen.setFehlerMeldung(eclPortalTexteM.getFehlertext(CaFehler.afKeineGueltigeEmailGastkarte));
                aDlgVariablen.setFehlerNr(CaFehler.afKeineGueltigeEmailGastkarte);
                return false;
            }
            if (aDlgVariablen.getGastkarteEmailBestaetigen() == null
                    || aDlgVariablen.getGastkarteEmailBestaetigen().isEmpty()
                    || aDlgVariablen.getGastkarteEmailBestaetigen().compareTo(aDlgVariablen.getGastkarteEmail()) != 0) {
                aDlgVariablen.setFehlerMeldung(eclPortalTexteM.getFehlertext(CaFehler.afEmailGKBestaetigungFalsch));
                aDlgVariablen.setFehlerNr(CaFehler.afEmailGKBestaetigungFalsch);
                return false;
            }

            break;
        }
        if ((aDlgVariablen.getGastkarteName() == null || aDlgVariablen.getGastkarteName().isEmpty())
                && aDlgVariablen.getGastkarteNrPersNatJur() == 0) {
            aDlgVariablen.setFehlerMeldung(eclPortalTexteM.getFehlertext(CaFehler.afNameGastkarteFehlt));
            aDlgVariablen.setFehlerNr(CaFehler.afNameGastkarteFehlt);
            return false;
        }
        if ((aDlgVariablen.getGastkarteOrt() == null || aDlgVariablen.getGastkarteOrt().isEmpty())
                && aDlgVariablen.getGastkarteNrPersNatJur() == 0) {
            aDlgVariablen.setFehlerMeldung(eclPortalTexteM.getFehlertext(CaFehler.afOrtGastkarteFehlt));
            aDlgVariablen.setFehlerNr(CaFehler.afOrtGastkarteFehlt);
            return false;
        }

        return true;
    }

    /**Für Gastkarte: Anmelden, Zuordnen zum Aktienregistereintrag, PDF vorbereiten, ggf. Emailen
     * 
     * Parameter:
     * 		EclAktienregisterEintrag	Zu diesem AktienregisterEintrag wird die Gastkarte verknüpft.
     * 									Kann theoretisch null sein.
     * 									Falls Versandart an Adresse im Aktienregister ist, dann 
     * 									muß dieses Feld komplett gefüllt sein (also nicht nur Nummer!).
     * 
     * Eingabefelder:
     * 		DlgVariablen.gastkarteNrPersNatJur (falls !=0, dann wird eine bestehende PersNatJur verwendet)
     * 		DlgVariablen.gastkarteName/.gastkarteVorname/.gastkarteOrt
     * 		DlgVariablen.gastkarteVersandart
     * 		je nach gastkarteVersandart:
     * 			DlgVariablen.gastkarteVersandart
     * 			DlgVariablen.gastkarteAbweichendeAdressse1/2/3/4/5
     * 			DlgVariablen.gastkarteEmail
     * 
     * Returnwert:
     * 		true => ok
     * 		false => DlgVariablen.fehlerMeldung/.fehlerNr gesetzt
     * 
     * Ausgabefelder:
     * 		DlgVariablen.gastkartePdfNr
     * 		Falls Postversand gewählt wurde:
     * 			DlgVariablen.gastkarteAbweichendeAdresse1/2/3/4/5 gesetzt mit der Adresse, an die
     * 				verschickt wird
     * 
     * */
    public boolean anlegenGastkarte(EclAktienregister pAktienregisterEintrag, DbBundle pDbBundle) {
        int erg;

        if (aFunktionen.reCheckKeineGastanmeldungen(pDbBundle, pAktienregisterEintrag) == false) {
            System.out.println("afAndererUserAktiv anlegenGastkarte 001");
            aDlgVariablen.setFehlerMeldung(eclPortalTexteM.getFehlertext(CaFehler.afAndererUserAktiv));
            aDlgVariablen.setFehlerNr(CaFehler.afAndererUserAktiv);
            return false;
        }

        BlGastkarte blGastkarte = new BlGastkarte(pDbBundle);
        EclMeldung lGastMeldung = new EclMeldung();

        /*Anmelden*/
        /*Meldung Gast füllen*/
        lGastMeldung.name = aDlgVariablen.getGastkarteName();
        if (aDlgVariablen.getGastkarteVorname() != null) {
            lGastMeldung.vorname = aDlgVariablen.getGastkarteVorname();
        }
        lGastMeldung.ort = aDlgVariablen.getGastkarteOrt();
        lGastMeldung.personenNatJurIdent = aDlgVariablen.getGastkarteNrPersNatJur();

        /*Versandart*/
        int lVersandart = 0;
        int lVersandAnAdresseAusAktienregister = 0;
        String[] lVersandadresse = new String[5];
        int ii;
        for (ii = 0; ii < 5; ii++) {
            lVersandadresse[ii] = "";
        }

        switch (aDlgVariablen.getGastkarteVersandart()) {
        case "2":/*Aufnahme in Sammelbatch, an Versandadresse Gast- bei nächstem Drucklauf ausdrucken und versenden*/
            lVersandart = 2;
            lVersandadresse[0] = aDlgVariablen.getGastkarteAbweichendeAdresse1();
            lVersandadresse[1] = aDlgVariablen.getGastkarteAbweichendeAdresse2();
            lVersandadresse[2] = aDlgVariablen.getGastkarteAbweichendeAdresse3();
            lVersandadresse[3] = aDlgVariablen.getGastkarteAbweichendeAdresse4();
            lVersandadresse[4] = aDlgVariablen.getGastkarteAbweichendeAdresse5();
            break;
        case "3":/*Online-Ausdruck (im Portal) erfolgt*/
            lVersandart = 3;
            lVersandadresse = null;
            break;
        case "4":/*Versand per Email (im Portal) erfolgt*/
            lVersandart = 4;
            lVersandadresse = null;
            lGastMeldung.mailadresse = aDlgVariablen.getGastkarteEmail();
            break;
        case "6":/*Versand an gleiche Adresse wie Aktionär*/
            switch (aDlgVariablen.getEintrittskarteVersandart()) {
            case "1":
            case "3":
            case "4":
            case "7":/*An im Aktienregister eingetragene Adresse*/
                lVersandart = 2;
                lVersandAnAdresseAusAktienregister = 1;
                break;
            case "2": /*Separate Versandadresse*/
                lVersandart = 2;
                lVersandadresse[0] = aDlgVariablen.getGastkarteAbweichendeAdresse1();
                lVersandadresse[1] = aDlgVariablen.getGastkarteAbweichendeAdresse2();
                lVersandadresse[2] = aDlgVariablen.getGastkarteAbweichendeAdresse3();
                lVersandadresse[3] = aDlgVariablen.getGastkarteAbweichendeAdresse4();
                lVersandadresse[4] = aDlgVariablen.getGastkarteAbweichendeAdresse5();
                break;
            }
            break;
        }

        blGastkarte.pGast = lGastMeldung;
        blGastkarte.pWillenserklaerungGeberIdent = -1; /*Aktionär gibt in diesem Fall*/
        if (lGastMeldung.personenNatJurIdent == 0) { /*Schnellösung, um die bei der Willenserklärung GastAnmelden beschriebene Situation zu vermeiden*/
            blGastkarte.pAktienregisterEintrag = pAktienregisterEintrag;
        }
        blGastkarte.pVersandart = lVersandart;
        blGastkarte.pVersandadresse = lVersandadresse;

        erg = blGastkarte.ausstellen();
        aDlgVariablen.setGastkarteMeldeIdent(blGastkarte.pGast.meldungsIdent);
        aDlgVariablen.setGastkartenZutrittsIdent(blGastkarte.rcZutrittsIdent);
        aDlgVariablen.setGastkartenZutrittsIdentNeben(blGastkarte.rcZutrittsIdentNeben);

        if (erg < 0) {
            aDlgVariablen.setFehlerMeldung(eclPortalTexteM.getFehlertext(erg));
            aDlgVariablen.setFehlerNr(erg);
            return false;
        }

        if (blGastkarte.pVersandadresse != null) {
            if (blGastkarte.pVersandadresse.length >= 1) {
                aDlgVariablen.setGastkarteAbweichendeAdresse1(blGastkarte.pVersandadresse[0]);
            }
            if (blGastkarte.pVersandadresse.length >= 2) {
                aDlgVariablen.setGastkarteAbweichendeAdresse2(blGastkarte.pVersandadresse[1]);
            }
            if (blGastkarte.pVersandadresse.length >= 3) {
                aDlgVariablen.setGastkarteAbweichendeAdresse3(blGastkarte.pVersandadresse[2]);
            }
            if (blGastkarte.pVersandadresse.length >= 4) {
                aDlgVariablen.setGastkarteAbweichendeAdresse4(blGastkarte.pVersandadresse[3]);
            }
            if (blGastkarte.pVersandadresse.length >= 5) {
                aDlgVariablen.setGastkarteAbweichendeAdresse5(blGastkarte.pVersandadresse[4]);
            }
        }

        /*Gastkarten-PDF erzeugen*/
        if (aDlgVariablen.getGastkarteVersandart().compareTo("3") == 0
                || aDlgVariablen.getGastkarteVersandart().compareTo("4") == 0) {
            aDlgVariablen.setGastkartePdfNr(blGastkarte.rcGastkartePdfNr);

            if (aDlgVariablen.getGastkarteVersandart().compareTo("4") == 0) {
                /*Nun per Mail versenden*/
                baMailm.sendenMitAnhang(aDlgVariablen.getGastkarteEmail(), "Ihr Zutrittsdokument",
                        "Anbei erhalten Sie Ihr Zutrittsdokument",
                        eclParamM.getClGlobalVar().lwPfadAllgemein + "\\meetingausdrucke\\" + eclParamM.getMandantPfad()
                                + "\\zutrittsdokumentM" + eclParamM.getClGlobalVar().getMandantString()
                                + Integer.toString(blGastkarte.rcGastkartePdfNr) + ".pdf");
            }
        }
        return true;

    }

    /****************************Funktionen für Aktionärs-Eintrittskarte*********************************/

    /**Für Eintrittskarte/Aktionär: prüfen, ob alle Eingabefelder richtig und vollständig gefüllt sind
     * 
     * Eingabefelder:
     * 
     * 		DlgVariablen.ausgewaehlteAktion (Legt fest, welche Eintrittskarten ausgestellt werden sollen)
     * 		DlgVariablen.ueberOeffentlicheID
     * 		DlgVariablen.eintrittskarteVersandart (außer bei Übertragung mittels öffentlicher ID)
     * 		DlgVariablen.eintrittskarteEmail (je nach Versandart)
     * 		DlgVariablen.vollmachtName/.vollmachtVorname/.vollmachtOrt
     * 
     * 		Falls 2. Eintrittskarte angefordert (bei Personengemeinschaften ....:
     * 		DlgVariablen.eintrittskarteVersandar2 (außer bei Übertragung mittels öffentlicher ID)
     * 		DlgVariablen.eintrittskarteEmail2 (je nach Versandart)
     * 		DlgVariablen.vollmachtName2/.vollmachtVorname2/.vollmachtOrt2
     * 
     * Returnwert: 	true = ok, 
     * 				false= Daten sind falsch, Fehlermeldung in DlgVariablen.fehlerMeldung/.fehlerNr
     * */
    public boolean pruefeEingabenFuerAktionaerEintrittskarte() {

        /*1. Eintrittskarte*/
        if (aDlgVariablen.isUeberOeffentlicheID()) {
            aDlgVariablen.setEintrittskarteVersandart("");
        } else {
            if (aDlgVariablen.getEintrittskarteVersandart() == null
                    || aDlgVariablen.getEintrittskarteVersandart().isEmpty()) {
                if (aDlgVariablen.getAusgewaehlteAktion().compareTo("2") == 0
                        || aDlgVariablen.getAusgewaehlteAktion().compareTo("28") == 0) { /*Erste EK*/
                    aDlgVariablen
                            .setFehlerMeldung(eclPortalTexteM.getFehlertext(CaFehler.afAusstellungsArtErsteEKFehlt));
                    aDlgVariablen.setFehlerNr(CaFehler.afAusstellungsArtErsteEKFehlt);
                } else { /*Nur eine EK vorhanden*/
                    aDlgVariablen.setFehlerMeldung(eclPortalTexteM.getFehlertext(CaFehler.afAusstellungsArtEKFehlt));
                    aDlgVariablen.setFehlerNr(CaFehler.afAusstellungsArtEKFehlt);
                }
                return false;
            }
        }

        switch (aDlgVariablen.getEintrittskarteVersandart()) {
        case "2":/*Aufnahme in Sammelbatch, an Versandadresse - bei nächstem Drucklauf ausdrucken und versenden => abweichende Versandadresse gültig?*/
            /*Tja - wie kann man die Versandadresse überprüfen?*/
            break;
        case "4":/*Versand per Email (im Portal) erfolgt => Mailadresse gültig?*/
            if (aDlgVariablen.getEintrittskarteEmail() == null || aDlgVariablen.getEintrittskarteEmail().isEmpty()
                    || !CaString.isMailadresse(aDlgVariablen.getEintrittskarteEmail())) {
                if (aDlgVariablen.getAusgewaehlteAktion().compareTo("2") == 0
                        || aDlgVariablen.getAusgewaehlteAktion().compareTo("28") == 0) { /*Erste EK*/
                    aDlgVariablen.setFehlerMeldung(eclPortalTexteM.getFehlertext(CaFehler.afKeineGueltigeEmailErsteEK));
                    aDlgVariablen.setFehlerNr(CaFehler.afKeineGueltigeEmailErsteEK);
                } else { /*Nur eine EK vorhanden*/
                    aDlgVariablen.setFehlerMeldung(eclPortalTexteM.getFehlertext(CaFehler.afKeineGueltigeEmailEK));
                    aDlgVariablen.setFehlerNr(CaFehler.afKeineGueltigeEmailEK);
                }
                return false;
            }
            if (aDlgVariablen.getEintrittskarteEmailBestaetigen() == null
                    || aDlgVariablen.getEintrittskarteEmailBestaetigen().isEmpty()
                    || aDlgVariablen.getEintrittskarteEmailBestaetigen()
                            .compareTo(aDlgVariablen.getEintrittskarteEmail()) != 0) {
                if (aDlgVariablen.getAusgewaehlteAktion().compareTo("2") == 0
                        || aDlgVariablen.getAusgewaehlteAktion().compareTo("28") == 0) { /*Erste EK*/
                    aDlgVariablen
                            .setFehlerMeldung(eclPortalTexteM.getFehlertext(CaFehler.afEmailErsteEKBestaetigungFalsch));
                    aDlgVariablen.setFehlerNr(CaFehler.afEmailErsteEKBestaetigungFalsch);
                } else { /*Nur eine EK vorhanden*/
                    aDlgVariablen.setFehlerMeldung(eclPortalTexteM.getFehlertext(CaFehler.afEmailEKBestaetigungFalsch));
                    aDlgVariablen.setFehlerNr(CaFehler.afEmailEKBestaetigungFalsch);
                }
                return false;
            }
            break;
        }

        if (aDlgVariablen.getAusgewaehlteAktion().compareTo("2") == 0
                || aDlgVariablen.getAusgewaehlteAktion().compareTo("3") == 0 ||

                /*Bei 2 Eintrittskarten für alle: Vollmacht ist optional => Vollamcht nur überprüfen, wenn eines der Vollmachtsfelder gefüllt - war früher, jetzt: falls Häckchen gesetzt*/
                (aDlgVariablen.getAusgewaehlteAktion().compareTo("28") == 0
                        && /*(!aDlgVariablen.getVollmachtName().isEmpty() || !aDlgVariablen.getVollmachtVorname().isEmpty() || !aDlgVariablen.getVollmachtOrt().isEmpty())*/ /*War mal :-) */
                        aDlgVariablen.isVollmachtEingeben())) {
            /*Vollmacht prüfen*/
            if (aDlgVariablen.getVollmachtName().isEmpty()) {
                if (aDlgVariablen.getAusgewaehlteAktion().compareTo("2") == 0
                        || aDlgVariablen.getAusgewaehlteAktion().compareTo("28") == 0) { /*Erste EK*/
                    aDlgVariablen.setFehlerMeldung(eclPortalTexteM.getFehlertext(CaFehler.afVollmachtNameErsteEKFehlt));
                    aDlgVariablen.setFehlerNr(CaFehler.afVollmachtNameErsteEKFehlt);
                } else { /*Nur eine EK vorhanden*/
                    aDlgVariablen.setFehlerMeldung(eclPortalTexteM.getFehlertext(CaFehler.afVollmachtNameEKFehlt));
                    aDlgVariablen.setFehlerNr(CaFehler.afVollmachtNameEKFehlt);
                }
                return false;
            }
            if (aDlgVariablen.getVollmachtOrt().isEmpty()) {
                if (aDlgVariablen.getAusgewaehlteAktion().compareTo("2") == 0
                        || aDlgVariablen.getAusgewaehlteAktion().compareTo("28") == 0) { /*Erste EK*/
                    aDlgVariablen.setFehlerMeldung(eclPortalTexteM.getFehlertext(CaFehler.afVollmachtOrtErsteEKFehlt));
                    aDlgVariablen.setFehlerNr(CaFehler.afVollmachtOrtErsteEKFehlt);
                } else { /*Nur eine EK vorhanden*/
                    aDlgVariablen.setFehlerMeldung(eclPortalTexteM.getFehlertext(CaFehler.afVollmachtOrtEKFehlt));
                    aDlgVariablen.setFehlerNr(CaFehler.afVollmachtOrtEKFehlt);
                }
                return false;
            }
        }

        /*Ggf.2. Eintrittskarte*/
        if (aDlgVariablen.getAusgewaehlteAktion().compareTo("2") == 0
                || aDlgVariablen.getAusgewaehlteAktion().compareTo("28") == 0) {
            if (aDlgVariablen.getEintrittskarteVersandart2() == null
                    || aDlgVariablen.getEintrittskarteVersandart2().isEmpty()) {
                aDlgVariablen.setFehlerMeldung(eclPortalTexteM.getFehlertext(CaFehler.afAusstellungsArtZweiteEKFehlt));
                aDlgVariablen.setFehlerNr(CaFehler.afAusstellungsArtZweiteEKFehlt);
                return false;
            }
            switch (aDlgVariablen.getEintrittskarteVersandart2()) {
            case "2":/*Aufnahme in Sammelbatch, an Versandadresse - bei nächstem Drucklauf ausdrucken und versenden => abweichende Versandadresse gültig?*/
                /*Tja - wie kann man die Versandadresse überprüfen?*/
                break;
            case "4":/*Versand per Email (im Portal) erfolgt => Mailadresse gültig?*/
                if (aDlgVariablen.getEintrittskarteEmail2() == null || aDlgVariablen.getEintrittskarteEmail2().isEmpty()
                        || !CaString.isMailadresse(aDlgVariablen.getEintrittskarteEmail2())) {
                    aDlgVariablen
                            .setFehlerMeldung(eclPortalTexteM.getFehlertext(CaFehler.afKeineGueltigeEmailZweiteEK));
                    aDlgVariablen.setFehlerNr(CaFehler.afKeineGueltigeEmailZweiteEK);
                    return false;
                }
                if (aDlgVariablen.getEintrittskarteEmail2Bestaetigen() == null
                        || aDlgVariablen.getEintrittskarteEmail2Bestaetigen().isEmpty()
                        || aDlgVariablen.getEintrittskarteEmail2Bestaetigen()
                                .compareTo(aDlgVariablen.getEintrittskarteEmail2()) != 0) {
                    aDlgVariablen.setFehlerMeldung(
                            eclPortalTexteM.getFehlertext(CaFehler.afEmailZweiteEKBestaetigungFalsch));
                    aDlgVariablen.setFehlerNr(CaFehler.afEmailZweiteEKBestaetigungFalsch);
                    return false;
                }
                break;
            }
            if (aDlgVariablen.getAusgewaehlteAktion().compareTo("2") == 0 ||

            /*Bei 2 Eintrittskarten für alle: Vollmacht ist optional => Vollamcht nur überprüfen, wenn eines der Vollmachtsfelder gefüllt*/
                    (aDlgVariablen.getAusgewaehlteAktion().compareTo("28") == 0
                            && /*(!aDlgVariablen.getVollmachtName2().isEmpty() || !aDlgVariablen.getVollmachtVorname2().isEmpty() || !aDlgVariablen.getVollmachtOrt2().isEmpty())*/
                            aDlgVariablen.isVollmachtEingeben2())) {

                /*Vollmacht prüfen*/
                if (aDlgVariablen.getVollmachtName2().isEmpty()) {
                    aDlgVariablen
                            .setFehlerMeldung(eclPortalTexteM.getFehlertext(CaFehler.afVollmachtNameZweiteEKFehlt));
                    aDlgVariablen.setFehlerNr(CaFehler.afVollmachtNameZweiteEKFehlt);
                    return false;
                }
                if (aDlgVariablen.getVollmachtOrt2().isEmpty()) {
                    aDlgVariablen.setFehlerMeldung(eclPortalTexteM.getFehlertext(CaFehler.afVollmachtOrtZweiteEKFehlt));
                    aDlgVariablen.setFehlerNr(CaFehler.afVollmachtOrtZweiteEKFehlt);
                    return false;
                }
            }
        }

        return true;
    }

    /**Sub-Funktion zu anlegenAktionaerEK*/
    private void leseAktienregisterLoginDatenZuAktienregisterEintrag(DbBundle pDbBundle) {
        pDbBundle.dbLoginDaten.read_aktienregisterIdent(aktienregisterEintrag.aktienregisterIdent);
        loginDaten = pDbBundle.dbLoginDaten.ergebnisPosition(0);

        //		aktienregisterZusatz=new EclAktienregisterZusatz();
        //		aktienregisterZusatz.aktienregisterIdent=aktienregisterEintrag.aktienregisterIdent;
        //		pDbBundle.dbAktienregisterZusatz.read(aktienregisterZusatz);
        //		if (pDbBundle.dbAktienregisterZusatz.anzErgebnis()>0) {
        //			aktienregisterZusatz=pDbBundle.dbAktienregisterZusatz.ergebnisPosition(0);
        //		}
    }

    /**Für Aktionärseintrittskarte: Anmelden, Zuordnen zum Aktienregistereintrag, PDF vorbereiten, ggf. Emailen
     * 
     * Parameter:
     * 
     * Eingabefelder:
     * 
     * 		EclTeilnehmerLoginM.anmeldeIdentPersonenNatJur
     * 
     * 		DlgVariablen.ausgewaehlteHauptAktion (Erstanmeldung oder Folgeausstellungen)
     * 
     * 		DlgVariablen.ausgewaehlteAktion (Legt fest, welche Eintrittskarten ausgestellt werden sollen)
     * 		DlgVariablen.ueberOeffentlicheID
     * 		DlgVariablen.personNatJurOeffentlicheID
     * 
     * 		DlgVariablen.eintrittskarteVersandart (außer bei Übertragung mittels öffentlicher ID)
     * 	 	je nach eintrittskarteVersandart:
     * 			DlgVariablen.eintrittskarteAbweichendeAdressse1/2/3/4/5
     * 			DlgVariablen.eintrittskarteEmail
     * 		DlgVariablen.vollmachtName/.vollmachtVorname/.vollmachtOrt
     * 
     * 		Falls 2. Eintrittskarte angefordert (bei Personengemeinschaften, oder auch bei allen ....:
     * 		DlgVariablen.eintrittskarteVersandart2 (außer bei Übertragung mittels öffentlicher ID)
     * 	 	je nach eintrittskarteVersandart2:
     * 			DlgVariablen.eintrittskarteAbweichendeAdressse12/22/32/42/52
     * 			DlgVariablen.eintrittskarteEmail2
     * 		DlgVariablen.vollmachtName2/.vollmachtVorname2/.vollmachtOrt2
     * 
     * 		Für Hauptaktion == 1 (d.h. erstmaliges Anmelden):
     * 			EclTeilnehmerLoginM.anmeldeAktionaersnummer (zu dieser Nummer werden die Anmeldungen erstellt)
     * 
     * 		Für Hauptaktion ==2 (d.h. Anmeldung existiert bereits)
     * 			EclzugeordneteMeldungM    Meldung, von der die Aktion ausgeht
     * 			EclTeilnehmerLoginM.anmeldeAktionaersnummer (zu dieser Nummer werden die Anmeldungen erstellt;
     * 					falls "Aussteller" = Aktionär selbst!)
     * 
     * Returnwert:
     * 		true => ok
     * 		false => DlgVariablen.fehlerMeldung/.fehlerNr gesetzt
     * 
     * Ausgabefelder:
     * 		AControllerEintrittskarte.aktienregisterEintrag (wird später noch für Gastkartenausstellung benötigt)
     * 
     * 		DlgVariablen.eintrittskartePdfNr
     * 		DlgVariablen.eintrittskartePdfNr2
     * 
     * 
     * */
    public boolean anlegenAktionaerEK(DbBundle pDbBundle) {

        int versandadresseverwenden = 0; /*1 = Aktionär; 2=gespeicherte Versandadresse des Aktionärs; 3=eingegebene Versandadresse*/
        EclMeldung lEclMeldung = null; /*Meldung, für die die Eintrittskarte ausgestellt werden soll*/
        long stimmenEK = 0;
        int erg;
        aktienregisterEintrag = null;
        loginDaten = null;

        EclZutrittsIdent zutrittsIdentAktionaer = new EclZutrittsIdent();

        aDlgVariablen.clearRCWillenserklaerung();
        eclTeilnehmerVersandadresse2M.init();
        eclTeilnehmerVersandadresseM.init();

        BlWillenserklaerung vmWillenserklaerung = null;

        /*Idents der PersoneneNatJur, die für den ersten bzw. zweiten Bevollmächtigten vergeben werden*/
        int identPersonenNatJurVollmacht = 0;
        int identPersonenNatJurVollmacht2 = 0;

        BlWillenserklaerung lWillenserklaerung = null; /*Wird grundsätzlich nur bei "Erstanmeldung" benötigt - aber in späterer
                                                       Schleife für alle Eintrittskarten im "If-Zweig" enthalten - deshalb hier
                                                       Definition*/

        int anzMeldungen = 0; /*Bei Hauptaktion==1: Anzahl der durchgeführten Anmeldungen. Sonst immer 1*/
        if (aDlgVariablen.getAusgewaehlteHauptAktion().compareTo("1") == 0) {

            if (aFunktionen.reCheckKeineAktienanmeldungen(pDbBundle) == false) {
                System.out.println("afAndererUserAktiv anlegenAktionaerEK 001");

                aDlgVariablen.setFehlerMeldung(eclPortalTexteM.getFehlertext(CaFehler.afAndererUserAktiv));
                aDlgVariablen.setFehlerNr(CaFehler.afAndererUserAktiv);
                return false;
            }

            /******************************Anmelden - aus Aktienregister***************************************/
            lWillenserklaerung = new BlWillenserklaerung();
            lWillenserklaerung.pQuelle = quelle;
            lWillenserklaerung.pErteiltAufWeg = aDlgVariablen.getEingabeQuelle();
            lWillenserklaerung.pErteiltZeitpunkt = aDlgVariablen.getErteiltZeitpunkt();
            /*Aktienregister füllen*/
            aktienregisterEintrag = new EclAktienregister();
            aktienregisterEintrag.aktionaersnummer = eclTeilnehmerLoginM.getAnmeldeAktionaersnummer();
            erg = pDbBundle.dbAktienregister.leseZuAktienregisterEintrag(aktienregisterEintrag);
            if (erg <= 0) {/*Aktienregistereintrag nicht mehr vorhanden*/

                aDlgVariablen.setFehlerMeldung(
                        eclPortalTexteM.getFehlertext(CaFehler.afAktienregisterEintragNichtMehrVorhanden));
                aDlgVariablen.setFehlerNr(CaFehler.afAktienregisterEintragNichtMehrVorhanden);
                return false;
            }

            aktienregisterEintrag = pDbBundle.dbAktienregister.ergebnisPosition(0);
            lWillenserklaerung.pEclAktienregisterEintrag = aktienregisterEintrag;
            CaBug.druckeLog("aktienregisterEintrag.aktionaersnummer=" + aktienregisterEintrag.aktionaersnummer,
                    logDrucken, 10);
            CaBug.druckeLog("aktienregisterEintrag.gattungId=" + aktienregisterEintrag.gattungId,
                   logDrucken, 10);
            /*Login-Daten füllen - wg. Initial-Passwort*/
            leseAktienregisterLoginDatenZuAktienregisterEintrag(pDbBundle);

            /*Restliche Parameter füllen*/
            lWillenserklaerung.pAktienAnmelden = -1; /*Alle Aktien anmelden*/
            lWillenserklaerung.pAnmeldungFix = false; /*Nicht "Fix" anmelden*/
            if (aDlgVariablen.getAusgewaehlteAktion().compareTo("2") == 0
                    || aDlgVariablen.getAusgewaehlteAktion().compareTo("28") == 0
                    || aDlgVariablen.getAusgewaehlteAktion().compareTo("30") == 0) {
                lWillenserklaerung.pAnzahlAnmeldungen = 2;
            } else {
                lWillenserklaerung.pAnzahlAnmeldungen = 1;

            }
            lWillenserklaerung.pWillenserklaerungGeberIdent = -1; /*Aktionär gibt in diesem Fall*/

            lWillenserklaerung.anmeldungAusAktienregister(pDbBundle);

            if (lWillenserklaerung.rcIstZulaessig == false) {
                aDlgVariablen
                        .setFehlerMeldung(eclPortalTexteM.getFehlertext(lWillenserklaerung.rcGrundFuerUnzulaessig));
                aDlgVariablen.setFehlerNr(lWillenserklaerung.rcGrundFuerUnzulaessig);
                return false;

            }

            anzMeldungen = lWillenserklaerung.rcMeldungen.length;
        } /***********************Anmelden beendet****************************************************/

        else {/*Hauptaktion ==2*/

            if (aFunktionen.reCheckKeineNeueWillenserklaerungen(pDbBundle, eclZugeordneteMeldungM.getMeldungsIdent(),
                    eclZugeordneteMeldungM.getIdentHoechsteWillenserklaerung()) == false) {
                //				System.out.println("afAndererUserAktiv anlegenAktionaerEK 002");
                aDlgVariablen.setFehlerMeldung(eclPortalTexteM.getFehlertext(CaFehler.afAndererUserAktiv));
                aDlgVariablen.setFehlerNr(CaFehler.afAndererUserAktiv);
                return false;
            }

            if (aDlgVariablen.getAusgewaehlteAktion().compareTo("2") == 0
                    || aDlgVariablen.getAusgewaehlteAktion().compareTo("28") == 0
                    || aDlgVariablen.getAusgewaehlteAktion().compareTo("30") == 0) {
                /*Zwar bereits angemeldet, aber nun zwei EKs ausgestellt.
                 * D.h. hier ist Sonderfall: bisherige (1) Anmeldung muß storniert werden, und zwei neue Anmeldungen
                 * erzeugt werden
                 */

                /*******Stornieren*******/
                lWillenserklaerung = new BlWillenserklaerung();
                lWillenserklaerung.pQuelle = quelle;
                lWillenserklaerung.pErteiltAufWeg = aDlgVariablen.getEingabeQuelle();
                lWillenserklaerung.pErteiltZeitpunkt = aDlgVariablen.getErteiltZeitpunkt();
                lWillenserklaerung.pEclAktienregisterEintrag = new EclAktienregister();
                lWillenserklaerung.pEclAktienregisterEintrag.aktienregisterIdent = eclTeilnehmerLoginM
                        .getAnmeldeIdentAktienregister();
                lWillenserklaerung.anmeldungenAusAktienregisterStornieren(pDbBundle);
                //				System.out.println("Storno wg. neuer 2 EK erfolgt");
                /******************************Neu Anmelden - aus Aktienregister mit zwei Aktienbeständen***************************************/
                lWillenserklaerung = new BlWillenserklaerung();
                lWillenserklaerung.pQuelle = quelle;
                lWillenserklaerung.pErteiltAufWeg = aDlgVariablen.getEingabeQuelle();
                lWillenserklaerung.pErteiltZeitpunkt = aDlgVariablen.getErteiltZeitpunkt();
                /*Aktienregister füllen*/
                aktienregisterEintrag = new EclAktienregister();
                aktienregisterEintrag.aktionaersnummer = eclTeilnehmerLoginM.getAnmeldeAktionaersnummer();
                erg = pDbBundle.dbAktienregister.leseZuAktienregisterEintrag(aktienregisterEintrag);
                if (erg <= 0) {/*Aktienregistereintrag nicht mehr vorhanden*/

                    aDlgVariablen.setFehlerMeldung(
                            eclPortalTexteM.getFehlertext(CaFehler.afAktienregisterEintragNichtMehrVorhanden));
                    aDlgVariablen.setFehlerNr(CaFehler.afAktienregisterEintragNichtMehrVorhanden);
                    return false;
                }

                aktienregisterEintrag = pDbBundle.dbAktienregister.ergebnisPosition(0);
                /*Login-Daten füllen - wg. Initial-Passwort*/
                leseAktienregisterLoginDatenZuAktienregisterEintrag(pDbBundle);

                lWillenserklaerung.pEclAktienregisterEintrag = aktienregisterEintrag;

                /*Restliche Parameter füllen*/
                lWillenserklaerung.pAktienAnmelden = -1; /*Alle Aktien anmelden*/
                lWillenserklaerung.pAnmeldungFix = false; /*Nicht "Fix" anmelden*/
                lWillenserklaerung.pAnzahlAnmeldungen = 2;
                lWillenserklaerung.pWillenserklaerungGeberIdent = -1; /*Aktionär gibt in diesem Fall*/
                lWillenserklaerung.pPersonNatJurFuerAnmeldungVerwenden = eclTeilnehmerLoginM
                        .getAnmeldeIdentPersonenNatJur();

                lWillenserklaerung.anmeldungAusAktienregister(pDbBundle);

                if (lWillenserklaerung.rcIstZulaessig == false) {
                    aDlgVariablen
                            .setFehlerMeldung(eclPortalTexteM.getFehlertext(lWillenserklaerung.rcGrundFuerUnzulaessig));
                    aDlgVariablen.setFehlerNr(lWillenserklaerung.rcGrundFuerUnzulaessig);
                    return false;

                }
                anzMeldungen = lWillenserklaerung.rcMeldungen.length;

            }

            else {
                /************Anmeldung wurde bereits in früherem Vorgang durchgeführt; entsprechende Variablen füllen********/
                anzMeldungen = 1;
                if (eclZugeordneteMeldungM.getArtBeziehung() == 1) {
                    aktienregisterEintrag = new EclAktienregister();
                    aktienregisterEintrag.aktionaersnummer = eclTeilnehmerLoginM.getAnmeldeAktionaersnummer();
                    erg = pDbBundle.dbAktienregister.leseZuAktienregisterEintrag(aktienregisterEintrag);
                    if (erg <= 0) {/*Aktienregistereintrag nicht mehr vorhanden*/

                        aDlgVariablen.setFehlerMeldung(
                                eclPortalTexteM.getFehlertext(CaFehler.afAktienregisterEintragNichtMehrVorhanden));
                        aDlgVariablen.setFehlerNr(CaFehler.afAktienregisterEintragNichtMehrVorhanden);
                        return false;
                    }
                    aktienregisterEintrag = pDbBundle.dbAktienregister.ergebnisPosition(0);
                    /*Login-Daten füllen - wg. Initial-Passwort*/
                    leseAktienregisterLoginDatenZuAktienregisterEintrag(pDbBundle);

                } else {
                    EclMeldung hMeldung = new EclMeldung();
                    hMeldung.meldungsIdent = eclZugeordneteMeldungM.getMeldungsIdent();
                    pDbBundle.dbMeldungen.leseZuMeldungsIdent(hMeldung);
                    aktienregisterEintrag = new EclAktienregister();
                    aktienregisterEintrag.aktionaersnummer = pDbBundle.dbMeldungen.meldungenArray[0].aktionaersnummer;
                    erg = pDbBundle.dbAktienregister.leseZuAktienregisterEintrag(aktienregisterEintrag);
                    if (erg <= 0) {/*Aktienregistereintrag nicht mehr vorhanden*/
                        aDlgVariablen.setFehlerMeldung(
                                eclPortalTexteM.getFehlertext(CaFehler.afAktienregisterEintragNichtMehrVorhanden));
                        aDlgVariablen.setFehlerNr(CaFehler.afAktienregisterEintragNichtMehrVorhanden);
                        return false;
                    }
                    aktienregisterEintrag = pDbBundle.dbAktienregister.ergebnisPosition(0);
                    /*Login-Daten füllen - wg. Initial-Passwort*/
                    leseAktienregisterLoginDatenZuAktienregisterEintrag(pDbBundle);

                }
            }

        }

        /*Nun Eintrittskarten erzeugen für alle erzeugten Meldungen*/
        int i;
        for (i = 0; i < anzMeldungen; i++) {

            BlWillenserklaerung ekWillenserklaerung = new BlWillenserklaerung();
            ekWillenserklaerung.pQuelle = quelle;
            ekWillenserklaerung.pErteiltAufWeg = aDlgVariablen.getEingabeQuelle();
            ekWillenserklaerung.pErteiltZeitpunkt = aDlgVariablen.getErteiltZeitpunkt();

            if (aDlgVariablen.getAusgewaehlteHauptAktion().compareTo("1") == 0 /*Erstanmeldung*/
                    || (aDlgVariablen.getAusgewaehlteHauptAktion().compareTo("2") == 0
                            && (aDlgVariablen.getAusgewaehlteAktion().compareTo("2") == 0
                                    || aDlgVariablen.getAusgewaehlteAktion().compareTo("28") == 0
                                    || aDlgVariablen.getAusgewaehlteAktion().compareTo(
                                            "30") == 0)) /*Bereits angemeldet, aber nun 2 EKs neu ausgestellt*/
            ) {
                ekWillenserklaerung.piMeldungsIdentAktionaer = lWillenserklaerung.rcMeldungen[i];
                ekWillenserklaerung.pFolgeFuerWillenserklaerungIdent = lWillenserklaerung.rcWillenserklaerungIdent;
            } else {/*Anmeldung bereits früher durchgeführt*/
                ekWillenserklaerung.piMeldungsIdentAktionaer = eclZugeordneteMeldungM.getMeldungsIdent();
            }

            /*Meldung einlesen, für die die Eintrittskarte ausgestellt werden soll*/
            lEclMeldung = new EclMeldung();
            lEclMeldung.meldungsIdent = ekWillenserklaerung.piMeldungsIdentAktionaer;
            pDbBundle.dbMeldungen.leseZuMeldungsIdent(lEclMeldung);
            lEclMeldung = pDbBundle.dbMeldungen.meldungenArray[0];

            versandadresseverwenden = 1;
            /*Versandadresse einlesen .... ist noch relativ temporär, wird ggf. doppelt gemacht*/
            stimmenEK = lEclMeldung.stimmen;
            if (pDbBundle.dbMeldungen.meldungenArray[0].identVersandadresse != 0) {
                EclPersonenNatJurVersandadresse l1PersonenNatJurVersandadresse = new EclPersonenNatJurVersandadresse();
                l1PersonenNatJurVersandadresse.ident = pDbBundle.dbMeldungen.meldungenArray[0].identVersandadresse;
                pDbBundle.dbPersonenNatJurVersandadresse.read(l1PersonenNatJurVersandadresse);
                if (i == 0) {
                    eclTeilnehmerVersandadresseM.copyFrom(pDbBundle.dbPersonenNatJurVersandadresse.ergebnisPosition(0));
                } else {
                    eclTeilnehmerVersandadresse2M
                            .copyFrom(pDbBundle.dbPersonenNatJurVersandadresse.ergebnisPosition(0));
                }
                versandadresseverwenden = 2;
            }

            if (aDlgVariablen.getAusgewaehlteHauptAktion().compareTo("1") == 0 /*Erstanmeldung*/
                    || (aDlgVariablen.getAusgewaehlteHauptAktion().compareTo("2") == 0
                            && (aDlgVariablen.getAusgewaehlteAktion().compareTo("2") == 0
                                    || aDlgVariablen.getAusgewaehlteAktion().compareTo("28") == 0
                                    || aDlgVariablen.getAusgewaehlteAktion().compareTo(
                                            "30") == 0)) /*Bereits angemeldet, aber nun 2 EKs neu ausgestellt*/
            ) {
                ekWillenserklaerung.pWillenserklaerungGeberIdent = -1; /*Aktionär*/
            } else {
                if (eclZugeordneteMeldungM.getArtBeziehung() == 1) {
                    ekWillenserklaerung.pWillenserklaerungGeberIdent = -1; /*Aktionär*/
                } else { /*Hier: es handelt sich um eine Untervollmacht. D.h. Geber = angemeldete personNatJur.*/
                    ekWillenserklaerung.pWillenserklaerungGeberIdent = eclTeilnehmerLoginM
                            .getAnmeldeIdentPersonenNatJur();
                }
            }

            /*Versandart*/
            String lVersandart = "";
            if (i == 0 || aDlgVariablen.getAusgewaehlteAktion().compareTo("30") == 0) {
                lVersandart = aDlgVariablen.getEintrittskarteVersandart();
                if (aDlgVariablen.isUeberOeffentlicheID()) {
                    lVersandart = "3";
                } /*Übertragung an Bevollmächtigten über ÖffentlicheID!*/
            } else {
                lVersandart = aDlgVariablen.getEintrittskarteVersandart2();
            }
            switch (lVersandart) {
            case "1":/*Aufnahme in Sammelbatch, an Adresse im Aktienregister - bei nächstem Drucklauf ausdrucken und versenden*/
                ekWillenserklaerung.pVersandartEK = 1;
                break;
            case "2":/*Aufnahme in Sammelbatch, an Versandadresse - bei nächstem Drucklauf ausdrucken und versenden*/
                versandadresseverwenden = 3;
                ekWillenserklaerung.pVersandartEK = 2;
                if (i == 0 || aDlgVariablen.getAusgewaehlteAktion().compareTo("30") == 0) {
                    ekWillenserklaerung.pVersandadresse1 = aDlgVariablen.getEintrittskarteAbweichendeAdresse1();
                    ekWillenserklaerung.pVersandadresse2 = aDlgVariablen.getEintrittskarteAbweichendeAdresse2();
                    ekWillenserklaerung.pVersandadresse3 = aDlgVariablen.getEintrittskarteAbweichendeAdresse3();
                    ekWillenserklaerung.pVersandadresse4 = aDlgVariablen.getEintrittskarteAbweichendeAdresse4();
                    ekWillenserklaerung.pVersandadresse5 = aDlgVariablen.getEintrittskarteAbweichendeAdresse5();
                } else {
                    ekWillenserklaerung.pVersandadresse1 = aDlgVariablen.getEintrittskarteAbweichendeAdresse12();
                    ekWillenserklaerung.pVersandadresse2 = aDlgVariablen.getEintrittskarteAbweichendeAdresse22();
                    ekWillenserklaerung.pVersandadresse3 = aDlgVariablen.getEintrittskarteAbweichendeAdresse32();
                    ekWillenserklaerung.pVersandadresse4 = aDlgVariablen.getEintrittskarteAbweichendeAdresse42();
                    ekWillenserklaerung.pVersandadresse5 = aDlgVariablen.getEintrittskarteAbweichendeAdresse52();
                }
                break;
            case "3":/*Online-Ausdruck (im Portal) erfolgt*/
                ekWillenserklaerung.pVersandartEK = 3;
                break;
            case "4":/*Versand per Email (im Portal) erfolgt*/
                ekWillenserklaerung.pVersandartEK = 4;
                if (i == 0 || aDlgVariablen.getAusgewaehlteAktion().compareTo("30") == 0) {
                    ekWillenserklaerung.pEmailAdresseEK = aDlgVariablen.getEintrittskarteEmail();
                } else {
                    ekWillenserklaerung.pEmailAdresseEK = aDlgVariablen.getEintrittskarteEmail2();
                }
                break;
            }

            ekWillenserklaerung.neueZutrittsIdentZuMeldung(pDbBundle);
            aDlgVariablen.setRcWillenserklaerungIdentAusgefuehrt(ekWillenserklaerung.rcWillenserklaerungIdent);
            if (ekWillenserklaerung.rcIstZulaessig == false) {
                aDlgVariablen
                        .setFehlerMeldung(eclPortalTexteM.getFehlertext(ekWillenserklaerung.rcGrundFuerUnzulaessig));
                aDlgVariablen.setFehlerNr(ekWillenserklaerung.rcGrundFuerUnzulaessig);
                return false;
            }

            zutrittsIdentAktionaer.zutrittsIdent = ekWillenserklaerung.pZutrittsIdent.zutrittsIdent;
            zutrittsIdentAktionaer.zutrittsIdentNeben = ekWillenserklaerung.pZutrittsIdent.zutrittsIdentNeben;
            if (i == 0) {
                aDlgVariablen.setZutrittsIdent(zutrittsIdentAktionaer.zutrittsIdent);
                aDlgVariablen.setZutrittsIdentNeben(zutrittsIdentAktionaer.zutrittsIdentNeben);
            } else {
                aDlgVariablen.setZutrittsIdent2(zutrittsIdentAktionaer.zutrittsIdent);
                aDlgVariablen.setZutrittsIdentNeben2(zutrittsIdentAktionaer.zutrittsIdentNeben);
            }
            int dateinr = ekWillenserklaerung.rcWillenserklaerungIdent;

            /*Ggf. Vollmachten eintragen*/
            if (aDlgVariablen.getAusgewaehlteAktion().compareTo("2") == 0
                    || aDlgVariablen.getAusgewaehlteAktion().compareTo("3") == 0
                    || (aDlgVariablen.getAusgewaehlteAktion().compareTo("28") == 0
                            && /*Bei 2 EK für alle: Vollmacht ist optional*/
                            ((i == 0 && aDlgVariablen.isVollmachtEingeben())
                                    || (i == 1 && aDlgVariablen.isVollmachtEingeben2())))) {
                vmWillenserklaerung = new BlWillenserklaerung();
                vmWillenserklaerung.pQuelle = quelle;
                vmWillenserklaerung.pErteiltAufWeg = aDlgVariablen.getEingabeQuelle();
                vmWillenserklaerung.pErteiltZeitpunkt = aDlgVariablen.getErteiltZeitpunkt();

                if (aDlgVariablen.getAusgewaehlteHauptAktion().compareTo("1") == 0) { /*Erstanmeldung*/
                    vmWillenserklaerung.piMeldungsIdentAktionaer = lWillenserklaerung.rcMeldungen[i];
                } else {/*Anmeldung bereits früher durchgeführt*/
                    vmWillenserklaerung.piMeldungsIdentAktionaer = eclZugeordneteMeldungM.getMeldungsIdent();
                }

                vmWillenserklaerung.pFolgeFuerWillenserklaerungIdent = ekWillenserklaerung.rcWillenserklaerungIdent;

                if (aDlgVariablen.getAusgewaehlteHauptAktion().compareTo("1") == 0) { /*Erstanmeldung*/
                    vmWillenserklaerung.pWillenserklaerungGeberIdent = -1; /*Aktionär*/
                } else {
                    if (eclZugeordneteMeldungM.getArtBeziehung() == 1) {
                        vmWillenserklaerung.pWillenserklaerungGeberIdent = -1; /*Aktionär*/
                    } else { /*Hier: es handelt sich um eine Untervollmacht. D.h. Geber = angemeldete personNatJur.*/
                        vmWillenserklaerung.pWillenserklaerungGeberIdent = eclTeilnehmerLoginM
                                .getAnmeldeIdentPersonenNatJur();
                    }
                }

                EclPersonenNatJur personNatJur = new EclPersonenNatJur();
                ;
                if (!aDlgVariablen.isUeberOeffentlicheID()) { /*Dann neuen Bevollmächtigten anlegen*/
                    personNatJur.ident = 0; /*Neue Person*/
                    if (i == 0) {
                        personNatJur.vorname = aDlgVariablen.getVollmachtVorname();
                        personNatJur.name = aDlgVariablen.getVollmachtName();
                        personNatJur.ort = aDlgVariablen.getVollmachtOrt();
                    } else {
                        personNatJur.vorname = aDlgVariablen.getVollmachtVorname2();
                        personNatJur.name = aDlgVariablen.getVollmachtName2();
                        personNatJur.ort = aDlgVariablen.getVollmachtOrt2();
                    }
                } else { /*Auf bestehenden Bevollmächtigten über öffentliche ID übertragen*/
                    personNatJur.ident = aDlgVariablen.getPersonNatJurOeffentlicheID();
                }
                vmWillenserklaerung.pEclPersonenNatJur = personNatJur;
                vmWillenserklaerung.vollmachtAnDritte(pDbBundle);
                aDlgVariablen.setRcWillenserklaerungIdentAusgefuehrtZweit(vmWillenserklaerung.rcWillenserklaerungIdent);

                if (vmWillenserklaerung.rcIstZulaessig == false) {
                    aDlgVariablen.setFehlerMeldung(
                            eclPortalTexteM.getFehlertext(vmWillenserklaerung.rcGrundFuerUnzulaessig));
                    aDlgVariablen.setFehlerNr(vmWillenserklaerung.rcGrundFuerUnzulaessig);
                    return false;
                }
                if (i == 0) {
                    identPersonenNatJurVollmacht = vmWillenserklaerung.pEclPersonenNatJur.ident;
                } else {
                    identPersonenNatJurVollmacht2 = vmWillenserklaerung.pEclPersonenNatJur.ident;
                }

                /*Nun noch die Eintrittskarten-WillenserklärungZusatz um die IdentPersonNatJur des Vertreters ergänzen*/

                EclWillenserklaerung tWillenserklaerung = new EclWillenserklaerung();
                EclWillenserklaerungZusatz tWillenserklaerungZusatz = new EclWillenserklaerungZusatz();
                pDbBundle.dbWillenserklaerung.leseZuIdent(ekWillenserklaerung.rcWillenserklaerungIdent);
                tWillenserklaerung = pDbBundle.dbWillenserklaerung.willenserklaerungGefunden(0);
                pDbBundle.dbWillenserklaerungZusatz.leseZuIdent(ekWillenserklaerung.rcWillenserklaerungIdent);
                tWillenserklaerungZusatz = pDbBundle.dbWillenserklaerungZusatz.willenserklaerungGefunden(0);
                tWillenserklaerungZusatz.identVertreterPersonNatJur = vmWillenserklaerung.pEclPersonenNatJur.ident;
                pDbBundle.dbWillenserklaerung.updateMitZusatz(tWillenserklaerung, tWillenserklaerungZusatz);

                /*TODO $9BlWillenserklaerung: Hier steckt noch "im Portal" zuviel Businesslogik (bei Eintrittskarte auf Vertreter), z.B. ausgestelltAufPersonenNatJurIdent, identVertreterPersonNatJur*/

                /*Nun noch die Eintrittskarte erweitern um die ausgestellte Person*/
                pDbBundle.dbZutrittskarten.readAktionaer(zutrittsIdentAktionaer, 1);
                EclZutrittskarten lEclZutrittskarte = pDbBundle.dbZutrittskarten.ergebnisPosition(0);
                lEclZutrittskarte.ausgestelltAufPersonenNatJurIdent = vmWillenserklaerung.pEclPersonenNatJur.ident;
                pDbBundle.dbZutrittskarten.update(lEclZutrittskarte);

            }

            //			System.out.println("AController Eintrittskarte");
            //			System.out.println(pDbBundle.globalVar.mandant);
            //			System.out.println("Nach Print");

            /*Eintrittskarten-PDF erzeugen*/
            if (lVersandart.compareTo("3") == 0 || lVersandart.compareTo("4") == 0) {
                if (i == 0) {
                    aDlgVariablen.setEintrittskartePdfNr(dateinr);
                } else {
                    aDlgVariablen.setEintrittskartePdfNr2(dateinr);
                }

                RpDrucken rpDrucken = new RpDrucken();
                rpDrucken.initServer();
                rpDrucken.exportFormat = 8;
                rpDrucken.exportDatei = "zutrittsdokumentM" + eclParamM.getClGlobalVar().getMandantString()
                        + Integer.toString(dateinr);
                rpDrucken.initFormular(pDbBundle);

                /*Variablen füllen - sowie Dokumentvorlage*/
                RpVariablen rpVariablen = new RpVariablen(pDbBundle);
                if (lVersandart.compareTo("3") == 0) {
                    rpVariablen.ekSelbstdruck(rpDrucken);
                } else {
                    rpVariablen.ekMail(rpDrucken);
                }
                rpDrucken.startFormular();

                BlNummernformen blNummernformen = new BlNummernformen(pDbBundle);

                rpVariablen.fuelleVariable(rpDrucken, "Nummern.ZutrittsIdent", BlNummernformen.verketteEKMitNeben(
                        zutrittsIdentAktionaer.zutrittsIdent, zutrittsIdentAktionaer.zutrittsIdentNeben, 0));
                rpVariablen.fuelleVariable(rpDrucken, "Nummern.ZutrittsIdentKurz",
                        zutrittsIdentAktionaer.zutrittsIdent);
                rpVariablen.fuelleVariable(rpDrucken, "Nummern.ZutrittsIdentKomplett",
                        blNummernformen.formatiereNrKomplett(zutrittsIdentAktionaer.zutrittsIdent,
                                zutrittsIdentAktionaer.zutrittsIdentNeben, KonstKartenklasse.eintrittskartennummerNeben,
                                KonstKartenart.erstzugang));
                //				System.out.println("ZutrittsIdentKomplett="+blNummernformen.formatiereNrKomplett(zutrittsIdentAktionaer.zutrittsIdent, zutrittsIdentAktionaer.zutrittsIdentNeben, KonstKartenklasse.eintrittskartennummerNeben, KonstKartenart.erstzugang));
                //				rpVariablen.fuelleVariable(rpDrucken, "Nummern.ZutrittsIdentKomplett", blNummernformen.formatiereEKNrKomplett(zutrittsIdentAktionaer.zutrittsIdent, zutrittsIdentAktionaer.zutrittsIdentNeben));
                if (pDbBundle.param.paramBasis.namensaktienAktiv) {
                    rpVariablen.fuelleVariable(rpDrucken, "Nummern.Aktionaersnummer",
                            BlAktienregisterNummerAufbereiten.aufbereitenFuerKlarschrift(lEclMeldung.aktionaersnummer));
                } else {
                    rpVariablen.fuelleVariable(rpDrucken, "Nummern.Aktionaersnummer", lEclMeldung.aktionaersnummer);
                }

                rpVariablen.fuelleVariable(rpDrucken, "Besitz.Stimmen", Long.toString(stimmenEK));
                rpVariablen.fuelleVariable(rpDrucken, "Besitz.StimmenDE", CaString.toStringDE(stimmenEK));
                rpVariablen.fuelleVariable(rpDrucken, "Besitz.StimmenEN", CaString.toStringEN(stimmenEK));

                int laengeInitialPasswort = eclParamM.getParam().paramPortal.passwortMindestLaenge;
                System.out.println("laengeInitialPasswort=" + laengeInitialPasswort);
                String hInitialPW = loginDaten.passwortInitial.trim();
                //				if (hInitialPW.length()>=2*laengeInitialPasswort){
                //					hInitialPW=hInitialPW.substring(laengeInitialPasswort,laengeInitialPasswort*2);
                //				}
                //				else{
                //					hInitialPW="";
                //				}
                if (hInitialPW.length() > 0) {
                    laengeInitialPasswort = hInitialPW.length() / 3;
                    hInitialPW = hInitialPW.substring(laengeInitialPasswort, laengeInitialPasswort * 2);
                } else {
                    hInitialPW = "";
                }

                //				if (aktienregisterZusatz.eigenesPasswort>=98) {
                //					/**Dann eigenes Passwort "in der Mache irgendwie"*/
                //					hInitialPW="";
                //				}

                rpVariablen.fuelleVariable(rpDrucken, "Aktionaer.Passwort", hInitialPW);

                rpVariablen.fuelleVariable(rpDrucken, "Aktionaer.Titel", lEclMeldung.titel);
                rpVariablen.fuelleVariable(rpDrucken, "Aktionaer.Name", lEclMeldung.name);
                rpVariablen.fuelleVariable(rpDrucken, "Aktionaer.Vorname", lEclMeldung.vorname);
                rpVariablen.fuelleVariable(rpDrucken, "Aktionaer.Plz", lEclMeldung.plz);
                rpVariablen.fuelleVariable(rpDrucken, "Aktionaer.Ort", lEclMeldung.ort);
                if (!lEclMeldung.land.isEmpty()) {
                    pDbBundle.dbStaaten.readCode(lEclMeldung.land);
                    if (pDbBundle.dbStaaten.anzErgebnis() > 0) {
                        rpVariablen.fuelleVariable(rpDrucken, "Aktionaer.Landeskuerzel",
                                pDbBundle.dbStaaten.ergebnisPosition(0).code);
                        rpVariablen.fuelleVariable(rpDrucken, "Aktionaer.Land",
                                pDbBundle.dbStaaten.ergebnisPosition(0).nameDE);
                    }
                }
                rpVariablen.fuelleVariable(rpDrucken, "Aktionaer.Strasse", lEclMeldung.strasse);
                /*Anrede füllen*/
                int anredenNr = lEclMeldung.anrede;
                EclAnrede hAnrede = new EclAnrede();
                if (anredenNr != 0) {
                    pDbBundle.dbAnreden.SetzeSprache(2, 0);
                    pDbBundle.dbAnreden.ReadAnrede_Anredennr(anredenNr);
                    hAnrede = new EclAnrede();
                    if (pDbBundle.dbAnreden.AnzAnredenInReadArray > 0) {
                        hAnrede = pDbBundle.dbAnreden.anredenreadarray[0];
                    }
                    rpVariablen.fuelleVariable(rpDrucken, "Aktionaer.AnredeDE", hAnrede.anredentext);
                    rpVariablen.fuelleVariable(rpDrucken, "Aktionaer.AnredeEN", hAnrede.anredentextfremd);
                    rpVariablen.fuelleVariable(rpDrucken, "Aktionaer.BriefanredeDE", hAnrede.anredenbrief);
                    rpVariablen.fuelleVariable(rpDrucken, "Aktionaer.BriefanredeEN", hAnrede.anredenbrieffremd);
                }

                /*Kombi-Felder füllen*/
                String titelVornameName = "";
                if (lEclMeldung.titel.length() != 0) {
                    titelVornameName = titelVornameName + lEclMeldung.titel + " ";
                }
                if (lEclMeldung.vorname.length() != 0) {
                    titelVornameName = titelVornameName + lEclMeldung.vorname + " ";
                }
                titelVornameName = titelVornameName + lEclMeldung.name;
                rpVariablen.fuelleVariable(rpDrucken, "Aktionaer.TitelVornameName", titelVornameName);

                String nameVornameTitel = "";
                nameVornameTitel = lEclMeldung.name;
                if (lEclMeldung.titel.length() != 0 || lEclMeldung.vorname.length() != 0) {
                    nameVornameTitel = nameVornameTitel + ",";
                }
                if (lEclMeldung.titel.length() != 0) {
                    nameVornameTitel = nameVornameTitel + " " + lEclMeldung.titel;
                }
                if (lEclMeldung.vorname.length() != 0) {
                    nameVornameTitel = nameVornameTitel + " " + lEclMeldung.vorname;
                }
                rpVariablen.fuelleVariable(rpDrucken, "Aktionaer.NameVornameTitel", nameVornameTitel);

                String kompletteAnredeDE = hAnrede.anredenbrief;
                String kompletteAnredeEN = hAnrede.anredenbrieffremd;
                if (hAnrede.istjuristischePerson != 1) {
                    if (lEclMeldung.titel.length() != 0) {
                        kompletteAnredeDE = kompletteAnredeDE + " " + lEclMeldung.titel;
                        kompletteAnredeEN = kompletteAnredeEN + " " + lEclMeldung.titel;
                    }
                    if (lEclMeldung.name.length() != 0) {
                        kompletteAnredeDE = kompletteAnredeDE + " " + lEclMeldung.name;
                        kompletteAnredeEN = kompletteAnredeEN + " " + lEclMeldung.name;
                    }
                }
                rpVariablen.fuelleVariable(rpDrucken, "Aktionaer.KompletteAnredeDE", kompletteAnredeDE);
                rpVariablen.fuelleVariable(rpDrucken, "Aktionaer.KompletteAnredeEN", kompletteAnredeEN);

                String besitzArtKuerzel = lEclMeldung.besitzart;
                String besitzArt = "", besitzArtEN = "";
                switch (besitzArtKuerzel) {
                case "E": {
                    besitzArt = "Eigenbesitz";
                    besitzArtEN = "Proprietary Possession";
                    break;
                }
                case "F": {
                    besitzArt = "Fremdbesitz";
                    besitzArtEN = "Minority Interests";
                    break;
                }
                case "V": {
                    besitzArt = "Vollmachtsbesitz";
                    besitzArtEN = "Proxy Possession";
                    break;
                }
                }
                rpVariablen.fuelleVariable(rpDrucken, "Aktionaer.BesitzArtKuerzel", besitzArtKuerzel);
                rpVariablen.fuelleVariable(rpDrucken, "Aktionaer.BesitzArt", besitzArt);
                rpVariablen.fuelleVariable(rpDrucken, "Aktionaer.BesitzArtEN", besitzArtEN);
                rpVariablen.fuelleVariable(rpDrucken, "Aktionaer.GattungKurz",
                        eclParamM.getParam().paramBasis.getGattungBezeichnungKurz(lEclMeldung.liefereGattung()));

                if (aDlgVariablen.getAusgewaehlteAktion().equals("2")
                        || aDlgVariablen.getAusgewaehlteAktion().equals("3")
                        || (aDlgVariablen.getAusgewaehlteAktion().compareTo("28") == 0
                                && /*Bei 2 EK für alle: Vollmacht ist optional*/
                                ((i == 0 && aDlgVariablen.isVollmachtEingeben())
                                        || (i == 1 && aDlgVariablen.isVollmachtEingeben2())))) {
                    rpVariablen.fuelleVariable(rpDrucken, "Vertreter.Kennung",
                            vmWillenserklaerung.pEclPersonenNatJur.loginKennung);
                    rpVariablen.fuelleVariable(rpDrucken, "Vertreter.Passwort",
                            vmWillenserklaerung.pEclPersonenNatJur.loginPasswort);
                    if (i == 0) {
                        rpVariablen.fuelleVariable(rpDrucken, "Vertreter.Name", aDlgVariablen.getVollmachtName());
                        rpVariablen.fuelleVariable(rpDrucken, "Vertreter.Vorname", aDlgVariablen.getVollmachtVorname());
                        rpVariablen.fuelleVariable(rpDrucken, "Vertreter.Ort", aDlgVariablen.getVollmachtOrt());
                    } else {
                        rpVariablen.fuelleVariable(rpDrucken, "Vertreter.Name", aDlgVariablen.getVollmachtName2());
                        rpVariablen.fuelleVariable(rpDrucken, "Vertreter.Vorname",
                                aDlgVariablen.getVollmachtVorname2());
                        rpVariablen.fuelleVariable(rpDrucken, "Vertreter.Ort", aDlgVariablen.getVollmachtOrt2());
                    }
                } else {
                    rpVariablen.fuelleVariable(rpDrucken, "Vertreter.Name", "");
                    rpVariablen.fuelleVariable(rpDrucken, "Vertreter.Vorname", "");
                    rpVariablen.fuelleVariable(rpDrucken, "Vertreter.Ort", "");
                    rpVariablen.fuelleVariable(rpDrucken, "Vertreter.Kennung", "");
                    rpVariablen.fuelleVariable(rpDrucken, "Vertreter.Passwort", "");
                }

                if (i == 0 || aDlgVariablen.getAusgewaehlteAktion().compareTo("30") == 0) {
                    rpVariablen.fuelleVariable(rpDrucken, "Versandadresse.Zeile1",
                            aDlgVariablen.getEintrittskarteAbweichendeAdresse1());
                    rpVariablen.fuelleVariable(rpDrucken, "Versandadresse.Zeile2",
                            aDlgVariablen.getEintrittskarteAbweichendeAdresse2());
                    rpVariablen.fuelleVariable(rpDrucken, "Versandadresse.Zeile3",
                            aDlgVariablen.getEintrittskarteAbweichendeAdresse3());
                    rpVariablen.fuelleVariable(rpDrucken, "Versandadresse.Zeile4",
                            aDlgVariablen.getEintrittskarteAbweichendeAdresse4());
                    rpVariablen.fuelleVariable(rpDrucken, "Versandadresse.Zeile5",
                            aDlgVariablen.getEintrittskarteAbweichendeAdresse5());
                } else {
                    rpVariablen.fuelleVariable(rpDrucken, "Versandadresse.Zeile1",
                            aDlgVariablen.getEintrittskarteAbweichendeAdresse12());
                    rpVariablen.fuelleVariable(rpDrucken, "Versandadresse.Zeile2",
                            aDlgVariablen.getEintrittskarteAbweichendeAdresse22());
                    rpVariablen.fuelleVariable(rpDrucken, "Versandadresse.Zeile3",
                            aDlgVariablen.getEintrittskarteAbweichendeAdresse32());
                    rpVariablen.fuelleVariable(rpDrucken, "Versandadresse.Zeile4",
                            aDlgVariablen.getEintrittskarteAbweichendeAdresse42());
                    rpVariablen.fuelleVariable(rpDrucken, "Versandadresse.Zeile5",
                            aDlgVariablen.getEintrittskarteAbweichendeAdresse52());

                }
                rpVariablen.fuelleVariable(rpDrucken, "Versandadresse.Zeile6", "");
                rpVariablen.fuelleVariable(rpDrucken, "Versandadresse.Zeile7", "");

                /*
                 * Versandadresse paßt so nicht mehr (alte Logik) - und Online steht keine Versandardresse auf der EK
                 * zur Verfügung! Also ersatzlos streichen :-)
                 */
                //				Ll.LlDefineVariable(nLLJob_, "Versandadresse.Verwenden", Integer.toString(versandadresseverwenden));
                //
                //				if (i==0 || aDlgVariablen.getAusgewaehlteAktion().compareTo("30")==0){
                //					Ll.LlDefineVariable(nLLJob_, "Versandadresse.NameVersand", eclTeilnehmerVersandadresseM.getNameVersand());
                //					Ll.LlDefineVariable(nLLJob_, "Versandadresse.VornameVersand", eclTeilnehmerVersandadresseM.getVornameVersand());
                //					Ll.LlDefineVariable(nLLJob_, "Versandadresse.StrasseVersand", eclTeilnehmerVersandadresseM.getStrasseVersand());
                //					Ll.LlDefineVariable(nLLJob_, "Versandadresse.PostleitzahlVersand", eclTeilnehmerVersandadresseM.getPostleitzahlVersand());
                //					Ll.LlDefineVariable(nLLJob_, "Versandadresse.OrtVersand", eclTeilnehmerVersandadresseM.getOrtVersand());
                //					Ll.LlDefineVariable(nLLJob_, "Versandadresse.StaatCodeVersand", eclTeilnehmerVersandadresseM.getStaatCodeVersand());
                //					Ll.LlDefineVariable(nLLJob_, "Versandadresse.StaatNameDEVersand", eclTeilnehmerVersandadresseM.getStaatNameDEVersand());
                //				}
                //				else{
                //					Ll.LlDefineVariable(nLLJob_, "Versandadresse.NameVersand", eclTeilnehmerVersandadresse2M.getNameVersand());
                //					Ll.LlDefineVariable(nLLJob_, "Versandadresse.VornameVersand", eclTeilnehmerVersandadresse2M.getVornameVersand());
                //					Ll.LlDefineVariable(nLLJob_, "Versandadresse.StrasseVersand", eclTeilnehmerVersandadresse2M.getStrasseVersand());
                //					Ll.LlDefineVariable(nLLJob_, "Versandadresse.PostleitzahlVersand", eclTeilnehmerVersandadresse2M.getPostleitzahlVersand());
                //					Ll.LlDefineVariable(nLLJob_, "Versandadresse.OrtVersand", eclTeilnehmerVersandadresse2M.getOrtVersand());
                //					Ll.LlDefineVariable(nLLJob_, "Versandadresse.StaatCodeVersand", eclTeilnehmerVersandadresse2M.getStaatCodeVersand());
                //					Ll.LlDefineVariable(nLLJob_, "Versandadresse.StaatNameDEVersand", eclTeilnehmerVersandadresse2M.getStaatNameDEVersand());
                //				}

                //Start printing
                rpDrucken.druckenFormular();
                rpDrucken.endeFormular();

                if (lVersandart.compareTo("4") == 0) {
                    /*Nun per Mail versenden*/
                    String lMail = "";
                    if (i == 0 || aDlgVariablen.getAusgewaehlteAktion().compareTo("30") == 0) {
                        lMail = aDlgVariablen.getEintrittskarteEmail();
                    } else {
                        lMail = aDlgVariablen.getEintrittskarteEmail2();
                    }

                    String hMailText = "", hBetreff = "";
                    if (eclPortalTexteM.portalNichtUeberNeueTexte()) {
                   } else {
                        hBetreff = eclPortalTexteM.holeText("218");
                        hMailText = eclPortalTexteM.holeText("219");

                    }
                    baMailm.sendenMitAnhang(lMail, hBetreff, hMailText, eclParamM.getClGlobalVar().lwPfadAllgemein
                            + "\\meetingausdrucke\\" + eclParamM.getMandantPfad() + "\\zutrittsdokumentM"
                            + eclParamM.getClGlobalVar().getMandantString() + Integer.toString(dateinr) + ".pdf");
                }
            }
        }

        /*Nun noch ggf. zweite Vollmacht (bei Personengemeinschaften) eintragen*/
        if (aDlgVariablen.getAusgewaehlteAktion().compareTo("2") == 0) {
            for (i = 0; i < 2; i++) {
                /*Nun noch zweiten Bevollmächtigten eintragen*/
                vmWillenserklaerung = new BlWillenserklaerung();
                vmWillenserklaerung.pQuelle = quelle;
                vmWillenserklaerung.pErteiltAufWeg = aDlgVariablen.getEingabeQuelle();
                vmWillenserklaerung.pErteiltZeitpunkt = aDlgVariablen.getErteiltZeitpunkt();
                vmWillenserklaerung.piMeldungsIdentAktionaer = lWillenserklaerung.rcMeldungen[i];
                vmWillenserklaerung.pFolgeFuerWillenserklaerungIdent = lWillenserklaerung.rcWillenserklaerungIdent;
                vmWillenserklaerung.pWillenserklaerungGeberIdent = -1; /*Aktionär*/
                EclPersonenNatJur personNatJur = new EclPersonenNatJur();

                personNatJur.ident = 0; /*Neue Person*/
                if (i == 1) { /*Zweite EK => 1. Vertreter noch eintragen*/
                    personNatJur.ident = identPersonenNatJurVollmacht;
                } else { /*Erste EK => 2. Vertreter noch eintragen*/
                    personNatJur.ident = identPersonenNatJurVollmacht2;
                }
                vmWillenserklaerung.pEclPersonenNatJur = personNatJur;
                vmWillenserklaerung.vollmachtAnDritte(pDbBundle);
                if (vmWillenserklaerung.rcIstZulaessig == false) {
                    aDlgVariablen.setFehlerMeldung(
                            eclPortalTexteM.getFehlertext(vmWillenserklaerung.rcGrundFuerUnzulaessig));
                    aDlgVariablen.setFehlerNr(vmWillenserklaerung.rcGrundFuerUnzulaessig);
                    return false;
                }
            }

        }

        return true;
    }

    /*************************Allgemeine Funktion******************************/

    public String doAusstellen() {
        if (!aFunktionen.pruefeStart("aEintrittskarte")) {
            aFunktionen.setzeEnde();
            return "aDlgFehler";
        }

        boolean ergBool = false;
        /****Überprüfen, ob Eingabe vollständig erfolgt ist für AktionärsEintrittskarte(n)****/
        ergBool = pruefeEingabenFuerAktionaerEintrittskarte();
        if (ergBool == false) {
            aFunktionen.setzeEnde();
            return "";
        }

        /********Überprüfen, ob Eingaben für Gastkarte vollständig ist****************************************/
        aDlgVariablen.setGastkarteNrPersNatJur(0);
        if (aDlgVariablen.isGastKarte()) {
            ergBool = pruefeEingabenFuerGastkarte();
            if (ergBool == false) {
                aFunktionen.setzeEnde();
                return "";
            }
        }

        /*********Initialisieren************/
        eclDbM.openAll();

        if (!aFunktionen.pruefeOBWillenserklaerungZulaessig(eclDbM.getDbBundle(),
                KonstWillenserklaerung.neueZutrittsIdentZuMeldung, aDlgVariablen.getAusgewaehlteHauptAktion())) {
            eclDbM.closeAllAbbruch();
            return aFunktionen.setzeEnde("aTransaktionAbbruch", true, true);
        }

        /***********Eintrittskarte für Aktionär**********/
        ergBool = anlegenAktionaerEK(eclDbM.getDbBundle());
        if (ergBool == false) {
            if (aDlgVariablen.getFehlerNr() == CaFehler.afAndererUserAktiv) {
                eclDbM.closeAllAbbruch();
                return aFunktionen.setzeEnde("aDlgAbbruch", true, true);

            }
            eclDbM.closeAllAbbruch();
            aFunktionen.setzeEnde();
            return "";
        }

        /****Eintrittskarte für Gast****/
        if (aDlgVariablen.isGastKarte()) {
            ergBool = anlegenGastkarte(aktienregisterEintrag, eclDbM.getDbBundle());
            if (ergBool == false) {
                if (aDlgVariablen.getFehlerNr() == CaFehler.afAndererUserAktiv) {
                    eclDbM.closeAllAbbruch();
                    return aFunktionen.setzeEnde("aDlgAbbruch", true, true);

                }
                eclDbM.closeAllAbbruch();
                aFunktionen.setzeEnde();
                return "";
            }

        }

        eclDbM.closeAll();
        return aFunktionen.setzeEnde("aEintrittskarteQuittung", true, false);
    }

    /**Überprüfen des eingegebenen öffentlichen Schlüssels des Bevollmächtigten und Holen des Bevollmächtigten
     * 
     * Eingabeparameter:
     * 		ADlgVariablen.ausgewaehlteHauptAktion
     * 		ADlgVariablen.zielOeffentlicheID
     * 		EclZugeordneteMeldungM.meldungsIdent (bei Hauptaktion==2 - bereits angemeldet)
     * 
     * Ausgabeparameter:
     * 		ADlgVariablen.fehlerMeldung/.fehlerNr
     * 		ADlgVariablen.personNatJurOeffentlicheID
     * 		ADlgVariablen.vollmachtName
     * 		ADlgVariablen.vollmachtVorname
     * 		ADlgVariablen.vollmachtVorname
     * 
     * Hinweis: Open und Close von DBBundle erfolgt innerhalb dieser Funktion!
     */
    public String doOeffentlicheIDPruefen() {

        if (!aFunktionen.pruefeStart("aEintrittskarte")) {
            return "aDlgFehler";
        }

        eclDbM.openAll();

        aDlgVariablen.clearFehlerMeldung();
        aDlgVariablen.setUeberOeffentlicheID(false);

        EclPersonenNatJur lPersonenNatJur = new EclPersonenNatJur();
        lPersonenNatJur.oeffentlicheID = aDlgVariablen.getZielOeffentlicheID();
        if (lPersonenNatJur.oeffentlicheID == null || lPersonenNatJur.oeffentlicheID.isEmpty()) {
            aDlgVariablen.setFehlerMeldung(eclPortalTexteM.getFehlertext(CaFehler.afOeffentlicherSchluesselFehlt));
            aDlgVariablen.setFehlerNr(CaFehler.afOeffentlicherSchluesselFehlt);
            eclDbM.closeAllAbbruch();
            aFunktionen.setzeEnde();
            return "";

        }
        eclDbM.getDbBundle().dbPersonenNatJur.leseZuPersonenNatJur(lPersonenNatJur);
        if (eclDbM.getDbBundle().dbPersonenNatJur.personenNatJurArray.length == 0) {
            aDlgVariablen.setFehlerMeldung(eclPortalTexteM.getFehlertext(CaFehler.afBevollmaechtigterNichtVorhanden));
            aDlgVariablen.setFehlerNr(CaFehler.afBevollmaechtigterNichtVorhanden);
            eclDbM.closeAllAbbruch();
            aFunktionen.setzeEnde();
            return "";
        }

        int lBevollmaechtigterIdent = eclDbM.getDbBundle().dbPersonenNatJur.personenNatJurArray[0].ident;

        if (aDlgVariablen.getAusgewaehlteHauptAktion().compareTo("1") == 0) {
            /*Neuanmeldung - noch keine Vollmacht erteilt - also immer ok!*/
        } else {
            BlWillenserklaerung lWillenserklaerung = new BlWillenserklaerung();
            lWillenserklaerung.pQuelle = quelle;
            lWillenserklaerung.pErteiltAufWeg = aDlgVariablen.getEingabeQuelle();
            lWillenserklaerung.pErteiltZeitpunkt = aDlgVariablen.getErteiltZeitpunkt();
            lWillenserklaerung.setzeDbBundle(eclDbM.getDbBundle());
            lWillenserklaerung.piMeldungsIdentAktionaer = eclZugeordneteMeldungM.getMeldungsIdent();
            lWillenserklaerung.einlesenVollmachtenAnDritte();
            lWillenserklaerung.rcIstZulaessig = true;
            lWillenserklaerung.pWillenserklaerungGeberIdent = lBevollmaechtigterIdent;
            lWillenserklaerung.pruefenObVollmachtgeberVollmachtHat();
            if (lWillenserklaerung.rcIstZulaessig) {/*Vollmacht ist schon vorhanden*/
                aDlgVariablen.setFehlerMeldung(
                        eclPortalTexteM.getFehlertext(CaFehler.afBevollmaechtigterBereitsBevollmaechtigt));
                aDlgVariablen.setFehlerNr(CaFehler.afBevollmaechtigterBereitsBevollmaechtigt);
                eclDbM.closeAllAbbruch();
                aFunktionen.setzeEnde();
                return "";
            }
        }

        aDlgVariablen.setUeberOeffentlicheID(true);
        aDlgVariablen.setPersonNatJurOeffentlicheID(lBevollmaechtigterIdent);
        aDlgVariablen.setVollmachtName(eclDbM.getDbBundle().dbPersonenNatJur.personenNatJurArray[0].name);
        aDlgVariablen.setVollmachtVorname(eclDbM.getDbBundle().dbPersonenNatJur.personenNatJurArray[0].vorname);
        aDlgVariablen.setVollmachtOrt(eclDbM.getDbBundle().dbPersonenNatJur.personenNatJurArray[0].ort);

        aDlgVariablen.setFehlerNr(1);
        eclDbM.closeAll();
        aFunktionen.setzeEnde("aEintrittskarte", true, false);
        return "";
    }

    /**Abmelden*/
    public String doAbmelden() {
        aDlgVariablen.clearLogin();
        return aFunktionen.waehleLogout();
    }

    /********************Ab hier Standard-Setter und -Getter********************************/
    public EclAktienregister getAktienregisterEintrag() {
        return aktienregisterEintrag;
    }

    public void setAktienregisterEintrag(EclAktienregister aktienregisterEintrag) {
        this.aktienregisterEintrag = aktienregisterEintrag;
    }

    public String getQuelle() {
        return quelle;
    }

    public void setQuelle(String quelle) {
        this.quelle = quelle;
    }

}
