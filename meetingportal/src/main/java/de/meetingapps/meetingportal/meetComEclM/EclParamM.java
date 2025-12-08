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
import java.util.List;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaEnvironment;
import de.meetingapps.meetingportal.meetComAllg.CaString;
import de.meetingapps.meetingportal.meetComEntities.EclEmittenten;
import de.meetingapps.meetingportal.meetComEntities.EclGeraetKlasseSetZuordnung;
import de.meetingapps.meetingportal.meetComEntities.EclGeraeteSet;
import de.meetingapps.meetingportal.meetComEntities.EclKontaktformularThema;
import de.meetingapps.meetingportal.meetComEntities.EclTermine;
import de.meetingapps.meetingportal.meetComEntities.EclUserLogin;
import de.meetingapps.meetingportal.meetComHVParam.ClGlobalVar;
import de.meetingapps.meetingportal.meetComHVParam.HVParam;
import de.meetingapps.meetingportal.meetComHVParam.ParamGeraet;
import de.meetingapps.meetingportal.meetComHVParam.ParamKeys;
import de.meetingapps.meetingportal.meetComHVParam.ParamLogo;
import de.meetingapps.meetingportal.meetComHVParam.ParamServer;
import de.meetingapps.meetingportal.meetComKonst.KonstPortalFunktionen;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

/**Wird beim ersten - vollständigen ? - EclDbM.Open gefüllt.
 * Zu vollständig:
 * > je nach Open mit /ohne Mandant werden die entsprechenden Parameter gefüllt. 
 */
@SessionScoped
@Named
public class EclParamM implements Serializable {
    private static final long serialVersionUID = 8957867999114924277L;

    private int logDrucken = 3;

    private @Inject EclLoginDatenM eclLoginDatenM;
    
    /**Löschen*/
    private int portalAuchInEnglischVerfuegbar = 0;

    private ClGlobalVar clGlobalVar = null;
    private HVParam param = null;
    private ParamLogo paramLogo = null;
    private ParamKeys paramKeys = null;

    private ParamServer paramServer = null;
    private EclGeraeteSet eclGeraeteSet = null;
    private EclGeraetKlasseSetZuordnung eclGeraetKlasseSetZuordnung = null;
    private ParamGeraet paramGeraet = null;
    private EclEmittenten eclEmittent = null;

    private EclTermine[] terminlisteTechnisch = null;

    private EclUserLogin eclUserLogin = null;

    /**Parameter, die nur im Speicher "gespeichert" werden. Werden beim Start automatisch eingelesen*/
    /**Nummer des Wildfly-Servers innerhalb des Clusters. 9999 ist reserviert für App.
     * Wird verwendet für EclLoginDaten.letzterLoginAufServer für User-Verwaltung*/
    private int bmServernummer = 0;

    /**Adresse des lokalen Wildfly für Socket-Verbindung*/
    private String bmSocketadresse = "";

    public EclParamM() {
        clGlobalVar = new ClGlobalVar();
        param = new HVParam();
        paramServer = new ParamServer();
        eclGeraeteSet = new EclGeraeteSet();
        eclGeraetKlasseSetZuordnung = new EclGeraetKlasseSetZuordnung();
        paramGeraet = new ParamGeraet();
        eclEmittent = new EclEmittenten();
        eclUserLogin = new EclUserLogin();

        /**Server-Daten holen*/
        CaEnvironment caEnvironment = new CaEnvironment();
        String bmServernummerString = caEnvironment.holeVariable("BMServernummer");
        if (bmServernummerString == null || !CaString.isNummern(bmServernummerString)) {
            CaBug.drucke("001 - Environmentvariable BMServernummer nicht gefunden oder falsch");
            return;
        }
        bmServernummer = Integer.parseInt(bmServernummerString);
        if (bmServernummer==0) {
            CaBug.drucke("003 - Environmentvariable BMServernummer ist 0 - bitte auf Wert > 0 setzen");
        }
        CaBug.druckeLog("bmServernummer=" + bmServernummer, logDrucken, 10);

        bmSocketadresse = caEnvironment.holeVariable("BMSocketadresse");
        if (bmSocketadresse == null || bmSocketadresse.isEmpty()) {
            CaBug.drucke("002 - Environmentvariable BMSocketadresse nicht gefunden oder falsch");
            return;
        }
        CaBug.druckeLog("bmSocketAdresse=" + bmSocketadresse, logDrucken, 10);
    }

    /********************Parameter zum Zugriff über JSF**********************************/

    /** Darstellung der Sprachumschaltung (wenn englisch aktiv): 1=Texte, 2=Flaggen*/
    public int getArtSprachumschaltung() {
        return param.paramPortal.artSprachumschaltung;
    }

    /**Falls 1, wird auf der Login-Seite der Text geteilt in
     * > Textbereich 1
     * > Button IOS
     * > Button Android
     * > Textbereich 2 
     */
    public int getAppInstallButtonsAnzeigen() {
        return param.paramPortalServer.appInstallButtonsAnzeigen;
    }

    /*++++++++Logout++++++*/
    public boolean isEndeButtonAufLogin() {
        if (param.paramPortal.logoutZiel.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    /**+++++++++++++++++++++++++++++++Während des Zeitablaufs wechselnde Parameter - Phasen++++++++++++++++++++++*/
    /**Wg. XHTMLS noch enthalten*/
    @Deprecated
    public int getLfdHVPortalErstanmeldungIstMoeglich() {
        return param.paramPortal.lfdHVPortalErstanmeldungIstMoeglich;
    }

    /**Wg. XHTMLS noch enthalten*/
    @Deprecated
    public int getLfdHVPortalInBetrieb() {
        return param.paramPortal.lfdHVPortalInBetrieb;
    }

    /**Wg. XHTMLS noch enthalten*/
    @Deprecated
    public int getLfdVorDerHVNachDerHV() {
        return param.paramPortal.lfdVorDerHVNachDerHV;
    }

    /**Wg. XHTMLS noch enthalten*/
    @Deprecated
    public int getLfdHVPortalBriefwahlIstMoeglich() {
        return param.paramPortal.lfdHVPortalBriefwahlIstMoeglich;
    }

    /**Wg. XHTMLS noch enthalten*/
    @Deprecated
    public int getLfdHVPortalVollmachtDritteIstMoeglich() {
        return param.paramPortal.lfdHVPortalVollmachtDritteIstMoeglich;
    }

    @Deprecated
    public int getHVPortalInBetrieb() {
        return param.paramPortal.lfdHVPortalInBetrieb;
    }
    
    @Deprecated
    public int getHVPortalErstanmeldungIstMoeglich() {
        return param.paramPortal.lfdHVPortalErstanmeldungIstMoeglich;
    }
 
    @Deprecated
    public int getVorDerHVNachDerHV() {
        return param.paramPortal.lfdVorDerHVNachDerHV;
    }
    
    @Deprecated
    public int getHVPortalBriefwahlIstMoeglich() {
        return param.paramPortal.lfdHVPortalBriefwahlIstMoeglich;
    }

    @Deprecated
    public int getHVPortalVollmachtDritteIstMoeglich() {
        return param.paramPortal.lfdHVPortalVollmachtDritteIstMoeglich;
    }

    /**++virtuelle HV++*/
    @Deprecated
    public int getLfdHVMitteilungIstMoeglich() {
        return param.paramPortal.lfdHVMitteilungIstMoeglich;
    }

    @Deprecated
    public int getLfdHVFragenIstMoeglich() {
        if (param.paramPortal.lfdHVFragenStufe1IstMoeglich == 1) {
            return 1;
        }
        if (param.paramPortal.lfdHVFragenStufe2IstMoeglich == 1) {
            return 1;
        }
        if (param.paramPortal.lfdHVFragenStufe3IstMoeglich == 1) {
            return 1;
        }
        if (param.paramPortal.lfdHVFragenStufe4IstMoeglich == 1) {
            return 1;
        }
        return 0;
    }

    @Deprecated
    public int getLfdHVTeilnehmerverzIstMoeglich() {
        return param.paramPortal.lfdHVTeilnehmerverzIstMoeglich;
    }

    @Deprecated
    public int getLfdHVAbstimmungsergIstMoeglich() {
        return param.paramPortal.lfdHVAbstimmungsergIstMoeglich;
    }

    /**++++++++++++++++Parameter für virtuelle HV+++++++++++++++++++++*/
    public int getStreamTestlinkWirdAngeboten() {
        return param.paramPortal.streamTestlinkWirdAngeboten;
    }

    public int getStreamAnbieter2() {
        return param.paramPortal.streamAnbieter2;
    }
    
    
    @Deprecated
    public int getMitteilungsstellerAbfragen() {
        return param.paramPortal.widerspruecheStellerAbfragen;
    }

    @Deprecated
    public int getMitteilungenAngeboten() {
        return param.paramPortal.mitteilungenAngeboten;
    }

    @Deprecated
    public int getMitteilungsButtonImmerSichtbar() {
        return param.paramPortal.mitteilungsButtonImmerSichtbar;
    }

    @Deprecated
    public int getMitteilungNurFuerAngemeldete() {
        return param.paramPortal.mitteilungNurFuerAngemeldete;
    }

    @Deprecated
    public int getMitteilungenDialogTopAngeboten() {
        return param.paramPortal.mitteilungenDialogTopAngeboten;
    }

    @Deprecated
   public int getMitteilungenTextAngeboten() {
        return param.paramPortal.mitteilungenTextAngeboten;
    }

    @Deprecated
    public int getMitteilungNurMitAbgegebenerStimme() {
        return param.paramPortal.mitteilungNurMitAbgegebenerStimme;
    }

    @Deprecated
    public int getStreamNurFuerAngemeldete() {
        return param.paramPortal.streamNurFuerAngemeldete;
    }


    @Deprecated
    public int getFragenAngeboten() {
        return param.paramPortal.fragenAngeboten;
    }

    @Deprecated
    public int getFragenNurFuerAngemeldete() {
        return param.paramPortal.fragenNurFuerAngemeldete;
    }

    @Deprecated
    public int getFragenstellerAbfragen() {
        return param.paramPortal.fragenStellerAbfragen;
    }

    @Deprecated
    public int getWortmelderAbfragen() {
        return param.paramPortal.wortmeldungStellerAbfragen;
    }

    @Deprecated
    public int getWortmelderEingeben() {
        return param.paramPortal.wortmeldungNameAbfragen;
    }

    @Deprecated
    public int getWortmeldungTOPEingeben() {
        return param.paramPortal.wortmeldungKurztextAbfragen;
    }

    @Deprecated
   public int getWortmeldungTextEingeben() {
        return param.paramPortal.wortmeldungLangtextAbfragen;
    }

    /**Liefert nur 0 oder 1. Parameter kann auch >1 sein!*/
    @Deprecated
    public int getWortmeldeListeAnzeigen() {
        if (param.paramPortal.wortmeldungListeAnzeigen == 0) {
            return 0;
        } else {
            return 1;
        }
    }

    @Deprecated
   public int getTeilnehmerverzAngeboten() {
        return param.paramPortal.teilnehmerverzAngeboten;
    }

    @Deprecated
    public int getTeilnehmerverzZusammenstellung() {
        return param.paramPortal.teilnehmerverzZusammenstellung;
    }

    @Deprecated
    public int getAbstimmungsergAngeboten() {
        return 0;//param.paramPortal.abstimmungsergAngeboten;
    }

    @Deprecated
    public boolean isUnterlage1Aktiv() {
        if (param.paramPortal.unterlage1Aktiv == 0) {
            return false;
        }
        return true;
    }

    @Deprecated
    public String getUnterlageButton1DE() {
        return param.paramPortal.unterlageButton1DE;
    }

    @Deprecated
    public String getUnterlageButton1EN() {
        return param.paramPortal.unterlageButton1EN;
    }

    @Deprecated
    public boolean isUnterlage2Aktiv() {
        if (param.paramPortal.unterlage2Aktiv == 0) {
            return false;
        }
        return true;
    }

    @Deprecated
   public String getUnterlageButton2DE() {
        return param.paramPortal.unterlageButton2DE;
    }

    @Deprecated
    public String getUnterlageButton2EN() {
        return param.paramPortal.unterlageButton2EN;
    }

    @Deprecated
    public boolean isUnterlage3Aktiv() {
        if (param.paramPortal.unterlage3Aktiv == 0) {
            return false;
        }
        return true;
    }

    @Deprecated
    public String getUnterlageButton3DE() {
        return param.paramPortal.unterlageButton3DE;
    }

    @Deprecated
    public String getUnterlageButton3EN() {
        return param.paramPortal.unterlageButton3EN;
    }

    @Deprecated
    public boolean isUnterlage4Aktiv() {
        if (param.paramPortal.unterlage4Aktiv == 0) {
            return false;
        }
        return true;
    }

    @Deprecated
    public String getUnterlageButton4DE() {
        return param.paramPortal.unterlageButton4DE;
    }

    @Deprecated
    public String getUnterlageButton4EN() {
        return param.paramPortal.unterlageButton4EN;
    }

    @Deprecated
    public boolean isUnterlage5Aktiv() {
        if (param.paramPortal.unterlage5Aktiv == 0) {
            return false;
        }
        return true;
    }

    @Deprecated
    public String getUnterlageButton5DE() {
        return param.paramPortal.unterlageButton5DE;
    }

    @Deprecated
    public String getUnterlageButton5EN() {
        return param.paramPortal.unterlageButton5EN;
    }

    @Deprecated
    public boolean isUnterlage6Aktiv() {
        if (param.paramPortal.unterlage6Aktiv == 0) {
            return false;
        }
        return true;
    }

    @Deprecated
   public String getUnterlageButton6DE() {
        return param.paramPortal.unterlageButton6DE;
    }

    @Deprecated
    public String getUnterlageButton6EN() {
        return param.paramPortal.unterlageButton6EN;
    }

    @Deprecated
    public boolean isUnterlage7Aktiv() {
        if (param.paramPortal.unterlage7Aktiv == 0) {
            return false;
        }
        return true;
    }

    @Deprecated
    public String getUnterlageButton7DE() {
        return param.paramPortal.unterlageButton7DE;
    }

    @Deprecated
    public String getUnterlageButton7EN() {
        return param.paramPortal.unterlageButton7EN;
    }

    @Deprecated
   public boolean isUnterlage8Aktiv() {
        if (param.paramPortal.unterlage8Aktiv == 0) {
            return false;
        }
        return true;
    }

    @Deprecated
    public String getUnterlageButton8DE() {
        return param.paramPortal.unterlageButton8DE;
    }

    @Deprecated
   public String getUnterlageButton8EN() {
        return param.paramPortal.unterlageButton8EN;
    }

    @Deprecated
    public boolean isUnterlage9Aktiv() {
        if (param.paramPortal.unterlage9Aktiv == 0) {
            return false;
        }
        return true;
    }

    @Deprecated
    public String getUnterlageButton9DE() {
        return param.paramPortal.unterlageButton9DE;
    }

    @Deprecated
    public String getUnterlageButton9EN() {
        return param.paramPortal.unterlageButton9EN;
    }

    public boolean isUnterlage10Aktiv() {
        if (param.paramPortal.unterlage10Aktiv == 0) {
            return false;
        }
        return true;
    }

    public String getUnterlageButton10DE() {
        return param.paramPortal.unterlageButton10DE;
    }

    public String getUnterlageButton10EN() {
        return param.paramPortal.unterlageButton10EN;
    }

    /**++++++++++++++++++++++++Virtuelle HV+++++++++++++++++++++++++++*/
    public boolean isFragenAngeboten() {
        return param.paramPortal.eclPortalFunktion[KonstPortalFunktionen.fragen].wirdAngeboten;
    }
    
    public boolean isWortmeldungenAngeboten() {
        return param.paramPortal.eclPortalFunktion[KonstPortalFunktionen.wortmeldungen].wirdAngeboten;
    }

     public boolean isWiderspruecheAngeboten() {
        return param.paramPortal.eclPortalFunktion[KonstPortalFunktionen.widersprueche].wirdAngeboten;
    }

    public boolean isAntraegeAngeboten() {
        return param.paramPortal.eclPortalFunktion[KonstPortalFunktionen.antraege].wirdAngeboten;
    }

    public boolean isSonstigeMitteilungenAngeboten() {
        return param.paramPortal.eclPortalFunktion[KonstPortalFunktionen.sonstigeMitteilungen].wirdAngeboten;
    }
    
    public boolean isBotschaftenEinreichenAngeboten() {
        return param.paramPortal.eclPortalFunktion[KonstPortalFunktionen.botschaftenEinreichen].wirdAngeboten;
    }

    public boolean liefereWortmeldungenAlsVideokonferenz() {
        return param.paramPortal.wortmeldungArt==2;
    }
    
    /**+++++++++++++++++++++++++++++++Online-HV++++++++++++++++++++++++*/
    public boolean isOnlineTeilnahmeSeparateNutzungsbedingungen() {
        return (param.paramPortal.onlineTeilnahmeSeparateNutzungsbedingungen == 1);
    }

    public boolean isOnlineTeilnahmeGastInSeparatemMenue() {
        return (param.paramPortal.onlineTeilnahmeGastInSeparatemMenue == 1);

    }

    public boolean isAktionaerAlsGastMitBriefwahl() {
        return (param.paramPortal.onlineTeilnahmeAktionaerAlsGast == 2
                || param.paramPortal.onlineTeilnahmeAktionaerAlsGast == 3);
    }

    public boolean isAktionaerAlsGastAuchOhneBriefwahl() {
        return (param.paramPortal.onlineTeilnahmeAktionaerAlsGast == 1
                || param.paramPortal.onlineTeilnahmeAktionaerAlsGast == 3);
    }

    
    /**+++++++++++++++++++Virtuelle HV 2023++++++++++++++++++++++++++*/
    public boolean liefereZuschaltungHVAutomatischNachLogin() {
        return param.paramPortal.zuschaltungHVAutomatischNachLogin==1;
    }
    
    /**++++++++++++++++++++++++++++++++++++++++++++++++++*/
    public int getVerfahrenPasswortVergessen() {
        return param.paramPortal.verfahrenPasswortVergessen;
    }

    public int getVerfahrenPasswortVergessenAblauf() {
        return param.paramPortal.verfahrenPasswortVergessenAblauf;
    }

    public int getDauerhaftesPasswortMoeglich() {
        return param.paramPortal.dauerhaftesPasswortMoeglich;
    }

    public int getAdressaenderungMoeglich() {
        return param.paramPortal.adressaenderungMoeglich;
    }

    public int getPublikationenAnbieten() {
        return param.paramPortal.publikationenAnbieten;
    }

    public int getKontaktDetailsAnbieten() {
        return param.paramPortal.kontaktDetailsAnbieten;
    }

    public int getKommunikationsspracheAuswahl() {
        return param.paramPortal.kommunikationsspracheAuswahl;
    }

    public int getSeparateTeilnahmebedingungenFuerGewinnspiel() {
        return param.paramPortal.separateTeilnahmebedingungenFuerGewinnspiel;
    }

    @Deprecated
    public int getGewinnspielAktiv() {
        return param.paramPortal.gewinnspielAktiv;
    }

    public int getRegistrierungFuerEmailVersandMoeglich() {
        return param.paramPortal.registrierungFuerEmailVersandMoeglich;
    }
    
    public int getLinkEmailEnthaeltLinkOderCode() {
        return param.paramPortal.linkEmailEnthaeltLinkOderCode;
    }
    /*"Die angeboten-Funktionen sind die aktuellen mit aktueller Nomenklatur 17.02.2021.
     * Die untrigen mit "moeglich" sind mißverständlich und sollten eliminiert werden
     */
    /**Irgendeine EK-Form ist Angeboten*/
    public int liefereEKAngeboten() {
        if (param.paramPortal.ekSelbstMoeglich==1) {return 1;}
        if (param.paramPortal.ekVollmachtMoeglich==1) {return 1;}
        if (param.paramPortal.ek2PersonengemeinschaftMoeglich==1) {return 1;}
        if (param.paramPortal.ek2MitOderOhneVollmachtMoeglich==1) {return 1;}
        if (param.paramPortal.ek2SelbstMoeglich==1) {return 1;}
        
        return 0;
    }
    
    public int getBriefwahlAngeboten() {
        return param.paramPortal.briefwahlAngeboten;
    }

    public int getSrvAngeboten() {
        return param.paramPortal.srvAngeboten;
    }

    public int getVollmachtDritteAngeboten() {
        return param.paramPortal.vollmachtDritteAngeboten;
    }
   
    public int getWeisungenAktuellNichtMoeglich() {
        return param.paramPortal.weisungenAktuellNichtMoeglich;
    }
    
    public int liefereTeilnehmerKannSichWeitereKennungenZuordnen() {
        return param.paramPortal.teilnehmerKannSichWeitereKennungenZuordnen;
    }
    
    public int getEkSelbstMoeglich() {
        return param.paramPortal.ekSelbstMoeglich;
    }

    public int getEk2SelbstMoeglich() {
        return param.paramPortal.ek2SelbstMoeglich;
    }

    public int getAnmeldenOhneWeitereWK() {
        return param.paramPortal.anmeldenOhneWeitereWK;
    }

    public int getEkVollmachtMoeglich() {
        return param.paramPortal.ekVollmachtMoeglich;
    }

    public int getEk2MitOderOhneVollmachtMoeglich() {
        return param.paramPortal.ek2MitOderOhneVollmachtMoeglich;
    }

    public int getEk2PersonengemeinschaftMoeglich() {
        return param.paramPortal.ek2PersonengemeinschaftMoeglich;
    }

    public int getBriefwahlMoeglich() {
        return param.paramPortal.briefwahlAngeboten;
    }

    public int getSrvMoeglich() {
        return param.paramPortal.srvAngeboten;
    }

    
    public int getErklAnPos1() {
        return param.paramPortal.erklAnPos1;
    }
    public int getErklAnPos2() {
        return param.paramPortal.erklAnPos2;
    }
    public int getErklAnPos3() {
        return param.paramPortal.erklAnPos3;
    }
    public int getErklAnPos4() {
        return param.paramPortal.erklAnPos4;
    }
    public int getErklAnPos5() {
        return param.paramPortal.erklAnPos5;
    }
    
    public boolean isGesamtMarkierung() {
        if (param.paramPortal.gesamtMarkierungJa == 1 || param.paramPortal.gesamtMarkierungNein == 1
                || param.paramPortal.gesamtMarkierungEnthaltung == 1 || param.paramPortal.gesamtMarkierungImSinne == 1 || param.paramPortal.gesamtMarkierungGegenSinne == 1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isGesamtMarkierungJa() {
        if (param.paramPortal.gesamtMarkierungJa == 1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isGesamtMarkierungNein() {
        if (param.paramPortal.gesamtMarkierungNein == 1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isGesamtMarkierungEnthaltung() {
        if (param.paramPortal.gesamtMarkierungEnthaltung == 1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isGesamtMarkierungImSinne() {
        if (param.paramPortal.gesamtMarkierungImSinne == 1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isGesamtMarkierungGegenSinne() {
        if (param.paramPortal.gesamtMarkierungGegenSinne == 1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isGesamtMarkierungLoeschen() {
        if (param.paramPortal.gesamtMarkierungAllesLoeschen == 1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isMarkierungJa() {
        if (param.paramPortal.markierungJa == 1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isMarkierungNein() {
        if (param.paramPortal.markierungNein == 1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isMarkierungEnthaltung() {
        if (param.paramPortal.markierungEnthaltung == 1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isBestaetigenDialog() {
        if (param.paramPortal.bestaetigenDialog == 1) {
            return true;
        } else {
            return false;
        }
    }

    public int getGegenantragsText() {
        return param.paramPortal.gegenantragsText;
    }

    public boolean mandantIstAusgewaehlt() {
        if (clGlobalVar.mandant == 0) {
            return false;
        }
        return true;
    }

    public String getMandantString() {
        return clGlobalVar.getMandantString();
    }

    /**Liefert "M"+Mandantennummer + j + HVJAhr + hvnummer+datenbereich, um dies in den Pfad aufzunehmen*/
    public String getMandantPfad() {
        return clGlobalVar.getMandantPfad();
        /*       
        return "M" + getMandantString() + "J" + CaString.fuelleLinksNull(Integer.toString(clGlobalVar.hvJahr), 4)
                + clGlobalVar.hvNummer + clGlobalVar.datenbereich;
                */
    }

    public String getEmittentName() {
        return eclEmittent.bezeichnungKurz;
    }

    public String getHVDatum() {
        return eclEmittent.hvDatum;
    }

    public boolean isCheckboxBeiSRV() {
        return param.paramPortal.checkboxBeiSRV == 1;
    }

    public boolean isCheckboxBeiBriefwahl() {
        return param.paramPortal.checkboxBeiBriefwahl == 1;
    }

    public boolean isCheckboxBeiKIAV() {
        return param.paramPortal.checkboxBeiKIAV == 1;
    }

    public boolean liefereCheckboxBeiVollmacht() {
        return param.paramPortal.checkboxBeiVollmacht == 1;
    }

    public boolean isJnBeiQuittung() {
        return param.paramPortal.jnAbfrageBeiWeisungQuittung==1;
    }
    
    public int varianteDialogablauf() {
        return param.paramPortal.varianteDialogablauf;
    }

    public int getLogoPosition() {
        return param.paramPortal.logoPosition;
    }
    
    public String getLogoName() {
        return param.paramPortal.logoName;
    }

    public String getLogoBreite() {
        return Integer.toString(param.paramPortal.logoBreite) + "px";
    }

    public String getLogoHoehe() {
        return Integer.toString(param.paramPortal.logoHoehe);
    }

    public String getCssName() {
        return param.paramPortal.cssName;
    }

    public String getFarbeHeader() {
        return param.paramPortal.farbeHeader;
    }
    
    public String getFarbeButton() {
        return param.paramPortal.farbeButton;
    }
    
    public String getFarbeButtonHover() {
        return param.paramPortal.farbeButtonHover;
    }
    
    public String getFarbeButtonSchrift() {
        return param.paramPortal.farbeButtonSchrift;
    }
    
    public String getFarbeLink() {
        return param.paramPortal.farbeLink;
    }
    
    public String getFarbeLinkHover() {
        return param.paramPortal.farbeLinkHover;
    }
    
    public String getFarbeListeUngerade() {
        return param.paramPortal.farbeListeUngerade;
    }
    
    public String getFarbeListeGerade() {
        return param.paramPortal.farbeListeGerade;
    }
    
    public String getFarbeHintergrund() {
        return param.paramPortal.farbeHintergrund;
    }
    
    public String getFarbeText() {
        return param.paramPortal.farbeText;
    }
    
    public String getFarbeUeberschriftHintergrund() {
        return param.paramPortal.farbeUeberschriftHintergrund;
    }
    
    public String getFarbeUeberschrift() {
        return param.paramPortal.farbeUeberschrift;
    }
    
    public String getKachelFarbe() {
    	return param.paramPortal.kachelFarbe;
    }
    
    public String getThemeFarbe() {
    	return param.paramPortal.themeFarbe;
    }
    
    public String getSchriftgroesseGlobal() {
    	return param.paramPortal.schriftgroesseGlobal;
    }
    public String getLogoMindestbreite() {
    	return param.paramPortal.logoMindestbreite;
    }
    public String getFarbeHintergrundBtn00() {
    	return param.paramPortal.farbeHintergrundBtn00;
    }
    public String getFarbeSchriftBtn00() {
    	return param.paramPortal.farbeSchriftBtn00;
    }
    public String getFarbeRahmenBtn00() {
    	return param.paramPortal.farbeRahmenBtn00;
    }
    public String getBreiteRahmenBtn00() {
    	return param.paramPortal.breiteRahmenBtn00;
    }
    public String getRadiusRahmenBtn00() {
    	return param.paramPortal.radiusRahmenBtn00;
    }
    public String getStilRahmenBtn00() {
    	return param.paramPortal.stilRahmenBtn00;
    }
    public String getFarbeHintergrundBtn00Hover() {
    	return param.paramPortal.farbeHintergrundBtn00Hover;
    }
    public String getFarbeSchriftBtn00Hover() {
    	return param.paramPortal.farbeSchriftBtn00Hover;
    }

    public String getFarbeRahmenBtn00Hover() {
    	return param.paramPortal.farbeRahmenBtn00Hover;
    }
    public String getBreiteRahmenBtn00Hover() {
    	return param.paramPortal.breiteRahmenBtn00Hover;
    }
    public String getRadiusRahmenBtn00Hover() {
    	return param.paramPortal.radiusRahmenBtn00Hover;
    }
    public String getStilRahmenBtn00Hover() {
    	return param.paramPortal.stilRahmenBtn00Hover;
    }
    public String getFarbeFocus() {
    	return param.paramPortal.farbeFocus;
    }
    public String getFarbeError() {
    	return param.paramPortal.farbeError;
    }
    public String getFarbeErrorSchrift() {
    	return param.paramPortal.farbeErrorSchrift;
    }
    public String getFarbeWarning() {
    	return param.paramPortal.farbeWarning;
    }
    public String getFarbeWarningSchrift() {
    	return param.paramPortal.farbeWarningSchrift;
    }
    public String getFarbeSuccess() {
    	return param.paramPortal.farbeSuccess;
    }

    public String getFarbeSuccessSchrift() {
    	return param.paramPortal.farbeSuccessSchrift;
    }
    public String getFarbeRahmenEingabefelder() {
    	return param.paramPortal.farbeRahmenEingabefelder;
    }
    public String getBreiteRahmenEingabefelder() {
    	return param.paramPortal.breiteRahmenEingabefelder;
    }
    public String getRadiusRahmenEingabefelder() {
    	return param.paramPortal.radiusRahmenEingabefelder;
    }
    public String getStilRahmenEingabefelder() {
    	return param.paramPortal.stilRahmenEingabefelder;
    }
    public String getFarbeHintergrundLogoutBtn() {
    	return param.paramPortal.farbeHintergrundLogoutBtn;
    }
    public String getFarbeSchriftLogoutBtn() {
    	return param.paramPortal.farbeSchriftLogoutBtn;
    }
    public String getFarbeRahmenLogoutBtn() {
    	return param.paramPortal.farbeRahmenLogoutBtn;
    }
    public String getBreiteRahmenLogoutBtn() {
    	return param.paramPortal.breiteRahmenLogoutBtn;
    }
    public String getRadiusRahmenLogoutBtn() {
    	return param.paramPortal.radiusRahmenLogoutBtn;
    }

    public String getStilRahmenLogoutBtn() {
    	return param.paramPortal.stilRahmenLogoutBtn;
    }
    public String getFarbeHintergrundLogoutBtnHover() {
    	return param.paramPortal.farbeHintergrundLogoutBtnHover;
    }
    public String getFarbeSchriftLogoutBtnHover() {
    	return param.paramPortal.farbeSchriftLogoutBtnHover;
    }
    public String getFarbeRahmenLogoutBtnHover() {
    	return param.paramPortal.farbeRahmenLogoutBtnHover;
    }
    public String getBreiteRahmenLogoutBtnHover() {
    	return param.paramPortal.breiteRahmenLogoutBtnHover;
    }
    public String getRadiusRahmenLogoutBtnHover() {
    	return param.paramPortal.radiusRahmenLogoutBtnHover;
    }
    public String getStilRahmenLogoutBtnHover() {
    	return param.paramPortal.stilRahmenLogoutBtnHover;
    }
    public String getFarbeHintergrundLoginBtn() {
    	return param.paramPortal.farbeHintergrundLoginBtn;
    }
    public String getFarbeSchriftLoginBtn() {
    	return param.paramPortal.farbeSchriftLoginBtn;
    }
    public String getFarbeRahmenLoginBtn() {
    	return param.paramPortal.farbeRahmenLoginBtn;
    }

    public String getBreiteRahmenLoginBtn() {
    	return param.paramPortal.breiteRahmenLoginBtn;
    }
    public String getRadiusRahmenLoginBtn() {
    	return param.paramPortal.radiusRahmenLoginBtn;
    }
    public String getStilRahmenLoginBtn() {
    	return param.paramPortal.stilRahmenLoginBtn;
    }
    public String getFarbeHintergrundLoginBtnHover() {
    	return param.paramPortal.farbeHintergrundLoginBtnHover;
    }
    public String getFarbeSchriftLoginBtnHover() {
    	return param.paramPortal.farbeSchriftLoginBtnHover;
    }
    public String getFarbeRahmenLoginBtnHover() {
    	return param.paramPortal.farbeRahmenLoginBtnHover;
    }
    public String getBreiteRahmenLoginBtnHover() {
    	return param.paramPortal.breiteRahmenLoginBtnHover;
    }
    public String getRadiusRahmenLoginBtnHover() {
    	return param.paramPortal.radiusRahmenLoginBtnHover;
    }
    public String getStilRahmenLoginBtnHover() {
    	return param.paramPortal.stilRahmenLoginBtnHover;
    }
    public String getFarbeRahmenLoginBereich() {
    	return param.paramPortal.farbeRahmenLoginBereich;
    }

    public String getBreiteRahmenLoginBereich() {
    	return param.paramPortal.breiteRahmenLoginBereich;
    }
    public String getRadiusRahmenLoginBereich() {
    	return param.paramPortal.radiusRahmenLoginBereich;
    }
    public String getStilRahmenLoginBereich() {
    	return param.paramPortal.stilRahmenLoginBereich;
    }
    public String getFarbeHintergrundLoginBereich() {
    	return param.paramPortal.farbeHintergrundLoginBereich;
    }
    public String getFarbeLinkLoginBereich() {
    	return param.paramPortal.farbeLinkLoginBereich;
    }
    public String getFarbeLinkHoverLoginBereich() {
    	return param.paramPortal.farbeLinkHoverLoginBereich;
    }
    public String getFarbeRahmenEingabefelderLoginBereich() {
    	return param.paramPortal.farbeRahmenEingabefelderLoginBereich;
    }
    public String getBreiteRahmenEingabefelderLoginBereich() {
    	return param.paramPortal.breiteRahmenEingabefelderLoginBereich;
    }
    public String getRadiusRahmenEingabefelderLoginBereich() {
    	return param.paramPortal.radiusRahmenEingabefelderLoginBereich;
    }
    public String getStilRahmenEingabefelderLoginBereich() {
    	return param.paramPortal.stilRahmenEingabefelderLoginBereich;
    }

    public String getFarbeBestandsbereichUngeradeReihe() {
    	return param.paramPortal.farbeBestandsbereichUngeradeReihe;
    }
    public String getFarbeBestandsbereichGeradeReihe() {
    	return param.paramPortal.farbeBestandsbereichGeradeReihe;
    }
    public String getFarbeLineUntenBestandsbereich() {
    	return param.paramPortal.farbeLineUntenBestandsbereich;
    }
    public String getBreiteLineUntenBestandsbereich() {
    	return param.paramPortal.breiteLineUntenBestandsbereich;
    }
    public String getStilLineUntenBestandsbereich() {
    	return param.paramPortal.stilLineUntenBestandsbereich;
    }
    public String getFarbeRahmenAnmeldeuebersicht() {
    	return param.paramPortal.farbeRahmenAnmeldeuebersicht;
    }
    public String getBreiteRahmenAnmeldeuebersicht() {
    	return param.paramPortal.breiteRahmenAnmeldeuebersicht;
    }
    public String getRadiusRahmenAnmeldeuebersicht() {
    	return param.paramPortal.radiusRahmenAnmeldeuebersicht;
    }
    public String getStilRahmenAnmeldeuebersicht() {
    	return param.paramPortal.stilRahmenAnmeldeuebersicht;
    }

    public String getFarbeTrennlinieAnmeldeuebersicht() {
    	return param.paramPortal.farbeTrennlinieAnmeldeuebersicht;
    }
    public String getBreiteTrennlinieAnmeldeuebersicht() {
    	return param.paramPortal.breiteTrennlinieAnmeldeuebersicht;
    }
    public String getStilTrennlinieAnmeldeuebersicht() {
    	return param.paramPortal.stilTrennlinieAnmeldeuebersicht;
    }
    public String getFarbeRahmenErteilteWillenserklärungen() {
    	return param.paramPortal.farbeRahmenErteilteWillenserklärungen;
    }
    public String getBreiteRahmenErteilteWillenserklärungen() {
    	return param.paramPortal.breiteRahmenErteilteWillenserklärungen;
    }
    public String getRadiusRahmenErteilteWillenserklärungen() {
    	return param.paramPortal.radiusRahmenErteilteWillenserklärungen;
    }
    public String getStilRahmenErteilteWillenserklärungen() {
    	return param.paramPortal.stilRahmenErteilteWillenserklärungen;
    }
    public String getFarbeHintergrundErteilteWillenserklärungen() {
    	return param.paramPortal.farbeHintergrundErteilteWillenserklärungen;
    }
    public String getFarbeSchriftErteilteWillenserklärungen() {
    	return param.paramPortal.farbeSchriftErteilteWillenserklärungen;
    }
    public String getFarbeRahmenAbstimmungstabelle() {
    	return param.paramPortal.farbeRahmenAbstimmungstabelle;
    }
    public String getBreiteRahmenAbstimmungstabelle() {
    	return param.paramPortal.breiteRahmenAbstimmungstabelle;
    }

    public String getRadiusRahmenAbstimmungstabelle() {
    	return param.paramPortal.radiusRahmenAbstimmungstabelle;
    }
    public String getStilRahmenAbstimmungstabelle() {
    	return param.paramPortal.stilRahmenAbstimmungstabelle;
    }
    public String getFarbeHintergrundAbstimmungstabelleUngeradeReihen() {
    	return param.paramPortal.farbeHintergrundAbstimmungstabelleUngeradeReihen;
    }
    public String getFarbeSchriftAbstimmungstabelleUngeradeReihen() {
    	return param.paramPortal.farbeSchriftAbstimmungstabelleUngeradeReihen;
    }
    public String getFarbeHintergrundAbstimmungstabelleGeradeReihen() {
    	return param.paramPortal.farbeHintergrundAbstimmungstabelleGeradeReihen;
    }
    public String getFarbeSchriftAbstimmungstabelleGeradeReihen() {
    	return param.paramPortal.farbeSchriftAbstimmungstabelleGeradeReihen;
    }
    public String getFarbeHintergrundWeisungJa() {
    	return param.paramPortal.farbeHintergrundWeisungJa;
    }
    public String getFarbeSchriftWeisungJa() {
    	return param.paramPortal.farbeSchriftWeisungJa;
    }
    public String getFarbeRahmenWeisungJa() {
    	return param.paramPortal.farbeRahmenWeisungJa;
    }
    public String getFarbeHintergrundWeisungJaChecked() {
    	return param.paramPortal.farbeHintergrundWeisungJaChecked;
    }

    public String getFarbeSchriftWeisungJaChecked() {
    	return param.paramPortal.farbeSchriftWeisungJaChecked;
    }
    public String getFarbeRahmenWeisungJaChecked() {
    	return param.paramPortal.farbeRahmenWeisungJaChecked;
    }
    public String getFarbeHintergrundWeisungNein() {
    	return param.paramPortal.farbeHintergrundWeisungNein;
    }
    public String getFarbeSchriftWeisungNein() {
    	return param.paramPortal.farbeSchriftWeisungNein;
    }
    public String getFarbeRahmenWeisungNein() {
    	return param.paramPortal.farbeRahmenWeisungNein;
    }
    public String getFarbeHintergrundWeisungNeinChecked() {
    	return param.paramPortal.farbeHintergrundWeisungNeinChecked;
    }
    public String getFarbeSchriftWeisungNeinChecked() {
    	return param.paramPortal.farbeSchriftWeisungNeinChecked;
    }
    public String getFarbeRahmenWeisungNeinChecked() {
    	return param.paramPortal.farbeRahmenWeisungNeinChecked;
    }
    public String getFarbeHintergrundWeisungEnthaltung() {
    	return param.paramPortal.farbeHintergrundWeisungEnthaltung;
    }
    public String getFarbeSchriftWeisungEnthaltung() {
    	return param.paramPortal.farbeSchriftWeisungEnthaltung;
    }

    public String getFarbeRahmenWeisungEnthaltung() {
    	return param.paramPortal.farbeRahmenWeisungEnthaltung;
    }
    public String getFarbeHintergrundWeisungEnthaltungChecked() {
    	return param.paramPortal.farbeHintergrundWeisungEnthaltungChecked;
    }
    public String getFarbeSchriftWeisungEnthaltungChecked() {
    	return param.paramPortal.farbeSchriftWeisungEnthaltungChecked;
    }
    public String getFarbeRahmenWeisungEnthaltungChecked() {
    	return param.paramPortal.farbeRahmenWeisungEnthaltungChecked;
    }
    public String getFarbeHintergrundFooterTop() {
    	return param.paramPortal.farbeHintergrundFooterTop;
    }
    public String getFarbeSchriftFooterTop() {
    	return param.paramPortal.farbeSchriftFooterTop;
    }
    public String getFarbeLinkFooterTop() {
    	return param.paramPortal.farbeLinkFooterTop;
    }
    public String getFarbeLinkFooterTopHover() {
    	return param.paramPortal.farbeLinkFooterTopHover;
    }
    public String getFarbeHintergrundFooterBottom() {
    	return param.paramPortal.farbeHintergrundFooterBottom;
    }
    public String getFarbeSchriftFooterBottom() {
    	return param.paramPortal.farbeSchriftFooterBottom;
    }

    public String getFarbeLinkFooterBottom() {
    	return param.paramPortal.farbeLinkFooterBottom;
    }
    public String getFarbeLinkFooterBottomHover() {
    	return param.paramPortal.farbeLinkFooterBottomHover;
    }
    public String getFarbeHintergrundModal() {
    	return param.paramPortal.farbeHintergrundModal;
    }
    public String getFarbeSchriftModal() {
    	return param.paramPortal.farbeSchriftModal;
    }
    public String getFarbeHintergrundModalHeader() {
    	return param.paramPortal.farbeHintergrundModalHeader;
    }
    public String getFarbeSchriftModalHeader() {
    	return param.paramPortal.farbeSchriftModalHeader;
    }
    public String getFarbeTrennlinieModal() {
    	return param.paramPortal.farbeTrennlinieModal;
    }
    public String getFarbeHintergrundUntenButtons() {
    	return param.paramPortal.farbeHintergrundUntenButtons;
    }
    public String getFarbeSchriftUntenButtons() {
    	return param.paramPortal.farbeSchriftUntenButtons;
    }
    public String getFarbeRahmenUntenButtons() {
    	return param.paramPortal.farbeRahmenUntenButtons;
    }

    public String getBreiteRahmenUntenButtons() {
    	return param.paramPortal.breiteRahmenUntenButtons;
    }
    public String getRadiusRahmenUntenButtons() {
    	return param.paramPortal.radiusRahmenUntenButtons;
    }
    public String getStilRahmenUntenButtons() {
    	return param.paramPortal.stilRahmenUntenButtons;
    }
    public String getFarbeHintergrundUntenButtonsHover() {
    	return param.paramPortal.farbeHintergrundUntenButtonsHover;
    }
    public String getFarbeSchriftUntenButtonsHover() {
    	return param.paramPortal.farbeSchriftUntenButtonsHover;
    }
    public String getFarbeRahmenUntenButtonsHover() {
    	return param.paramPortal.farbeRahmenUntenButtonsHover;
    }
    public String getBreiteRahmenUntenButtonsHover() {
    	return param.paramPortal.breiteRahmenUntenButtonsHover;
    }
    public String getRadiusRahmenUntenButtonsHover() {
    	return param.paramPortal.radiusRahmenUntenButtonsHover;
    }
    public String getStilRahmenUntenButtonsHover() {
    	return param.paramPortal.stilRahmenUntenButtons;
    }
    
    public String getFarbeCookieHintHintergrund() {
        return param.paramPortal.farbeCookieHintHintergrund;
    }
    
    public String getFarbeCookieHintSchrift() {
        return param.paramPortal.farbeCookieHintSchrift;
    }
    
    public String getFarbeCookieHintButton() {
        return param.paramPortal.farbeCookieHintButton;
    }
    
    public String getFarbeCookieHintButtonSchrift() {
        return param.paramPortal.farbeCookieHintButtonSchrift;
    }
    
    public int getCookieHinweis() {
        return param.paramPortal.cookieHinweis;
    }
    
    public int getStreamAnbieter() {
        return param.paramPortal.streamAnbieter;
    }
    
    public String getSchriftgroesseVersammlunsleiterView() {
        return param.paramPortal.schriftgroesseVersammlunsleiterView;
    }

    /**Parameter 1*/
    public String getStreamLink() {
        return param.paramPortal.streamLink;
    }

    /**Parameter 2*/
    public String getStreamID() {
        return param.paramPortal.streamID;
    }

    
    public boolean isImpressum() {
        return (param.paramPortal.impressumEmittent == 1);
    }

    public boolean isSeparateDatenschutzhinweise() {
        return (param.paramPortal.separateDatenschutzerklaerung == 1);
    }

    /**+++++investorenSind++++*/
    public String investorenNr() {
        switch (param.paramBasis.investorenSind) {
        case 1:
            return "Aktionärs-Nummer";
        case 2:
            return "Mitglieds-Nummer";
        }
        return "";
    }

    public String getMailConsultant() {
        return param.paramBasis.mailConsultant;
    }
    
    public boolean isWebsocketsMoeglich() {
        return (param.paramPortal.websocketsMoeglich == 1);
    }

    /*********Standard Getter und Setter*************************/
    public HVParam getParam() {
        return param;
    }

    public void setParam(HVParam param) {
        this.param = param;
    }

    public ParamServer getParamServer() {
        return paramServer;
    }

    public void setParamServer(ParamServer paramServer) {
        this.paramServer = paramServer;
    }

    public EclGeraeteSet getEclGeraeteSet() {
        return eclGeraeteSet;
    }

    public void setEclGeraeteSet(EclGeraeteSet eclGeraeteSet) {
        this.eclGeraeteSet = eclGeraeteSet;
    }

    public ParamGeraet getParamGeraet() {
        return paramGeraet;
    }

    public void setParamGeraet(ParamGeraet paramGeraet) {
        this.paramGeraet = paramGeraet;
    }

    public EclGeraetKlasseSetZuordnung getEclGeraetKlasseSetZuordnung() {
        return eclGeraetKlasseSetZuordnung;
    }

    public void setEclGeraetKlasseSetZuordnung(EclGeraetKlasseSetZuordnung eclGeraetKlasseSetZuordnung) {
        this.eclGeraetKlasseSetZuordnung = eclGeraetKlasseSetZuordnung;
    }

    public ClGlobalVar getClGlobalVar() {
        return clGlobalVar;
    }

    public void setClGlobalVar(ClGlobalVar clGlobalVar) {
        this.clGlobalVar = clGlobalVar;
    }

    public EclEmittenten getEclEmittent() {
        return eclEmittent;
    }

    public void setEclEmittent(EclEmittenten eclEmittent) {
        this.eclEmittent = eclEmittent;
    }

    public EclUserLogin getEclUserLogin() {
        return eclUserLogin;
    }

    public void setEclUserLogin(EclUserLogin eclUserLogin) {
        this.eclUserLogin = eclUserLogin;
    }

    public EclTermine[] getTerminlisteTechnisch() {
        return terminlisteTechnisch;
    }

    public void setTerminlisteTechnisch(EclTermine[] terminlisteTechnisch) {
        this.terminlisteTechnisch = terminlisteTechnisch;
    }

    public int getBmServernummer() {
        return bmServernummer;
    }

    public void setBmServernummer(int bmServernummer) {
        this.bmServernummer = bmServernummer;
    }

    public String getBmSocketadresse() {
        return bmSocketadresse;
    }

    public void setBmSocketadresse(String bmSocketadresse) {
        this.bmSocketadresse = bmSocketadresse;
    }

    public ParamLogo getParamLogo() {
        return paramLogo;
    }

    public void setParamLogo(ParamLogo paramLogo) {
        this.paramLogo = paramLogo;
    }

    public int getLoginVerfahren() {
        return param.paramPortal.loginVerfahren;
    }
    
    public int getPortalAuchInEnglischVerfuegbar() {
        return portalAuchInEnglischVerfuegbar;
    }

    public void setPortalAuchInEnglischVerfuegbar(int portalAuchInEnglischVerfuegbar) {
        this.portalAuchInEnglischVerfuegbar = portalAuchInEnglischVerfuegbar;
    }

    public ParamKeys getParamKeys() {
        return paramKeys;
    }

    public void setParamKeys(ParamKeys paramKeys) {
        this.paramKeys = paramKeys;
    }
    
    public List<EclKontaktformularThema> getKontaktformularThemenListe(){
        return param.paramPortal.kontaktformularThemenListe;
    }
    
    public int getRegisterAnbindung() {
        return param.paramPortal.registerAnbindung;
    }

    /**+++++++++Test-Bestand, Datenmanipulation, Produktionsbestand++++++++++++++++++*/
    public boolean liefereDatenbestandIstProduktiv() {
        return (eclEmittent.liefereDatenbestandIstProduktiv());
    }
    public boolean liefereDatenbestandDatenmanipulationIstGesperrt() {
        return (eclEmittent.liefereDatenbestandDatenmanipulationIstGesperrt());
    }
    public boolean liefereDatenbestandMusterAufFormularenAusgeblendet() {
        return (eclEmittent.liefereDatenbestandMusterAufFormularenAusgeblendet());
    }
    public boolean liefereDatenbestandMusterAufPortalStartseitenAusgeblendet() {
        return (eclEmittent.liefereDatenbestandMusterAufPortalStartseitenAusgeblendet());
    }
    public boolean liefereDatenbestandMusterAufPortalInnenseitenAusgeblendet() {
        return (eclEmittent.liefereDatenbestandMusterAufPortalInnenseitenAusgeblendet());
    }
    public boolean liefereDatenbestandPasswortVergessenSchreibenDrucken() {
        return (eclEmittent.liefereDatenbestandPasswortVergessenSchreibenDrucken());
    }

    /*++++++++++++++++++Hinweis-Fragezeichen im Portal++++++++++++++++++++++++++++*/
    public boolean liefereFragezeichenHinweiseKeineHints() {
        return param.paramPortal.liefereFragezeichenHinweiseKeineHints();
    }
    public boolean liefereFragezeichenHinweiseAktiviert() {
        return param.paramPortal.liefereFragezeichenHinweiseAktiviert();
    }
    public boolean liefereFragezeichenHinweiseNormaleTexteAusblenden() {
        return param.paramPortal.liefereFragezeichenHinweiseNormaleTexteAusblenden();
    }
    public boolean liefereFragezeichenHinweisePositionLinks() {
        return param.paramPortal.liefereFragezeichenHinweisePositionLinks();
    }
    public boolean liefereFragezeichenHinweisePositionRechts() {
        return param.paramPortal.liefereFragezeichenHinweisePositionRechts();
    }
    
    /*+++++++++++++++++++Permanent-Portal+++++++++++++++++++++++++++*/
    public int liefereRegisterAnbindungZurHV() {
        return param.paramPortal.registerAnbindungZurHV;
    }
    public int liefereRegisterAnbindungVonHV() {
        return param.paramPortal.registerAnbindungVonHV;
    }
     
    /*++++++++++++++++Freiwillige Anmeldung++++++++++++++++++++++++++++++++++*/
    public boolean liefereFreiwilligeAnmeldungNurPapier() {
        return param.paramPortal.freiwilligeAnmeldungNurPapier==1;
    }
    public boolean liefereFreiwilligeAnmeldungMitVertretereingabe() {
        return (param.paramPortal.freiwilligeAnmeldungMitVertretereingabe>0);
    }
    
    /**Parameter =1 oder 2, freiwillig oder Pflicht*/
    public boolean liefereFreiwilligeAnmeldungMitVertretereingabeNormal() {
        return (param.paramPortal.freiwilligeAnmeldungMitVertretereingabe==1 || param.paramPortal.freiwilligeAnmeldungMitVertretereingabe==2);
    }
    /**Parameter =3, ku178*/
    public boolean liefereFreiwilligeAnmeldungMitVertretereingabeku178() {
        return (param.paramPortal.freiwilligeAnmeldungMitVertretereingabe==3);
    }
    
    
    /*+++++++++++++Wortmeldungen++++++++++++++++++++++++++++++++*/
    public int liefereWortmeldungInhaltsHinweiseAktiv() {
        return param.paramPortal.wortmeldungInhaltsHinweiseAktiv;
    }
    
    /*++++++++++++++Bestätigungen im Aktionärsportal++++++++++++++++*/
    public int liefereBestaetigungsStimmabgabeNachHV() {
        return param.paramPortal.bestaetigungStimmabgabeNachHV;
    }
    
    public boolean liefereBestaetigungPerEmailUeberallZulassen() {
        return (param.paramPortal.bestaetigungPerEmailUeberallZulassen!=0);
    }
    
    /*++++++++++++++++++++E-Einladung+++++++++++++++++++++*/
    public int liefereEmailVersandRegistrierungOderWiderspruch() {
        /**Nur drin, weil bei Ersterstellung der Parameter immer auf 0 stand :-( -was jetzt ja
         * nicht mehr zulässig ist ....
         */
        if (param.paramPortal.emailVersandRegistrierungOderWiderspruch==0) {return 1;}
        
        return param.paramPortal.emailVersandRegistrierungOderWiderspruch;
        
    }
    
    public boolean liefereEmail2AdresseAusRegVerwenden() {
        return (param.paramPortal.emailVersandZweitEMailAusRegister==1);
    }
    
    
    /*+++++++++++++++++Gastkarten im Aktionärsportal+++++++++++++++++++++++++++*/
    public boolean liefereGastkartenButtonAnzeigen() {
        if (param.paramPortal.gastkartenAnforderungMoeglich==0) {
            return false;
        }
        
        if (eclLoginDatenM.liefereKennungArt()==2) {
            return false;
        }
        return true;
    }
    
    /*****************ku178 - temporär für Sepa aktiv***********************/
    public boolean liefereku178SepaAktiv() {
        return param.paramPortal.liefereku178SepaIstAktiv(); 
    }
    
    /***************Weisungsgestaltung***********************************/
    public boolean liefereMarkierungLoeschen() {
        return param.paramPortal.markierungLoeschen==1;
    }
    
    /*******************Bezeichnungen Mitteilung*************************/
    public String liefereAntraegeTextInOberflaecheLang() {
        return param.paramPortal.antraegeTextInOberflaecheLang;
    }
    public String liefereBotschaftenTextInOberflaecheLang() {
        return param.paramPortal.botschaftenTextInOberflaecheLang;
    }
    public String liefereFragenTextInOberflaecheLang() {
        return param.paramPortal.fragenTextInOberflaecheLang;
    }
    public String liefereSonstMitteilungenTextInOberflaecheLang() {
        return param.paramPortal.sonstMitteilungenTextInOberflaecheLang;
    }
    public String liefereWiderspruecheTextInOberflaecheLang() {
        return param.paramPortal.widerspruecheTextInOberflaecheLang;
    }
    public String liefereWortmeldungTextInOberflaecheLang() {
        return param.paramPortal.wortmeldungTextInOberflaecheLang;
    }
}
