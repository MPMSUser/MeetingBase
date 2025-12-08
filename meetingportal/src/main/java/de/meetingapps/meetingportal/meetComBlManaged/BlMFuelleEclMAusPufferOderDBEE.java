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
package de.meetingapps.meetingportal.meetComBlManaged;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComBVerwaltung.BvReload;
import de.meetingapps.meetingportal.meetComEclM.EclAbstimmungSetM;
import de.meetingapps.meetingportal.meetComEclM.EclDbM;
import de.meetingapps.meetingportal.meetComEclM.EclParamM;
import de.meetingapps.meetingportal.meetComEclM.EclPortalTexteM;
import de.meetingapps.meetingportal.meetComEh.EhGetAbstimmungen;
import de.meetingapps.meetingportal.meetComEh.EhGetHVParam;
import de.meetingapps.meetingportal.meetComEh.EhGetHVTexte;
import de.meetingapps.meetingportal.meetComEh.EhGetTechnischeTermine;
import de.meetingapps.meetingportal.meetComEntities.EclAbstimmungSet;
import de.meetingapps.meetingportal.meetComEntities.EclEmittenten;
import de.meetingapps.meetingportal.meetComEntities.EclFehler;
import de.meetingapps.meetingportal.meetComEntities.EclGeraetKlasseSetZuordnung;
import de.meetingapps.meetingportal.meetComEntities.EclGeraeteSet;
import de.meetingapps.meetingportal.meetComEntities.EclTermine;
import de.meetingapps.meetingportal.meetComEntities.EclUserLogin;
import de.meetingapps.meetingportal.meetComHVParam.ClGlobalVar;
import de.meetingapps.meetingportal.meetComHVParam.HVParam;
import de.meetingapps.meetingportal.meetComHVParam.ParamGeraet;
import de.meetingapps.meetingportal.meetComHVParam.ParamKeys;
import de.meetingapps.meetingportal.meetComHVParam.ParamLogo;
import de.meetingapps.meetingportal.meetComHVParam.ParamServer;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

/**Füllt die EclM-Beans für Parameter entweder aus dem Puffer (wenn vorhanden, und wenn nicht veraltet)
 * oder aus Datenbank. Ggf. wird Puffer dabei aktualisiert.
 *
 * EclDbM muß geöffnet sein (beim Erst-Laden bei Session-Beginn OHNE Refresh/Reload der Parameter!)
 */
@RequestScoped
@Named
public class BlMFuelleEclMAusPufferOderDBEE {

    /**Auf true setzen => Das Laden und der Zugriff auf die Puffer wird im Systemlog protokolliert*/
    private int logDrucken = 0;

    @Inject
    EclParamM eclParamM;
    @Inject
    EclAbstimmungSetM eclAbstimmungSetM;
    @Inject
    EclPortalTexteM eclTextePortalM;
    @Inject
    BlMPuffer blmPuffer;
    @Inject
    BlMFuellePuffer blmFuellePuffer;

    @Inject
    EclDbM eclDbM;

    /**Aufrufen in jedwedem Portal, wenn dies möglicherweise zum erstenmal für diesen Mandanten gestartet ist.
     * sysGeraeteSetNichtVorhanden
     * sysGeraeteKlasseSetZuordnungNichtVorhanden
     * sysGeraeteKlasseNichtVorhanden
     * */
    public int fuelleAlleVariablenBeiStart() {
        int mandant = eclDbM.getDbBundle().clGlobalVar.mandant;
        int rc;

        BvReload bvReload = new BvReload(eclDbM.getDbBundle());
        bvReload.checkReload(mandant);

        rc = this.fuelleGlobalParam(false, bvReload);
        if (rc < 0) {
            return rc;
        }

        if (mandant != 0 && mandant != 9999) {
            if (eclDbM.getDbBundle().clGlobalVar.hvJahr == 0) {
                EclEmittenten lEmittenten = blmPuffer.getStandardEmittentFuerEmittentenPortal(mandant);
                eclParamM.getClGlobalVar().hvJahr = lEmittenten.hvJahr;
                eclParamM.getClGlobalVar().hvNummer = lEmittenten.hvNummer;
                eclParamM.getClGlobalVar().datenbereich = lEmittenten.dbArt;
            }
            rc = this.fuelleMandantenParam(true, bvReload);
            if (rc < 0) {
                return rc;
            }
        }

        rc = this.fuelleUserLogin(true, bvReload);
        if (rc < 0) {
            return rc;
        }

        return 1;
    }

    /**Zwei Aufrufvarianten:
     * a) pImmerLaden=true => alle Parameter werden aus DB nach EclM-Session-Beans übetragen, und Version gemäß bvReload belegt.
     * 		(Offen: wann wird das eigentlich überhaupt verwendet? Doch eigentlich nie ...?)
     * b) pImmerLaden=false, bvReload (vorbelegt), und Parameter sind bereits eingelesen =>
     * 		Parameter werden neu in Puffer eingelesen und neu in EclM-Session_Beans übertragen
     *
     * Fehler-Return-Codes:
     * sysGeraeteSetNichtVorhanden
     * sysGeraeteKlasseSetZuordnungNichtVorhanden
     * sysGeraeteKlasseNichtVorhanden
     */
    public int fuelleGlobalParam(boolean pImmerLaden, BvReload pBvReload) {
        int rc = 0;

        /*Server-Parameter*/
        boolean reloadServer = false; //EclM-Session-Beans wurden neu geladen
        boolean reloadServerPuffer = false; //Puffer wurde neu geladen
        if (pImmerLaden || eclParamM.getParamServer().reloadVersionParameterServer != pBvReload.reloadParameterServer) {
            ParamServer lParamServer = null;

            lParamServer = blmPuffer.getParamServer();
            reloadServer = true;

            if (lParamServer == null || lParamServer.reloadVersionParameterServer != pBvReload.reloadParameterServer) {
                blmFuellePuffer.fuelleServerParameter(pBvReload.reloadParameterServer);
                reloadServerPuffer = true;
                reloadServer = true;
                lParamServer = blmPuffer.getParamServer();
            }
            while (lParamServer == null) {
                lParamServer = blmPuffer.getParamServer();
                reloadServer = true;
            }

            eclParamM.setParamServer(lParamServer);
            eclDbM.getDbBundle().paramServer = lParamServer;
        }

        /*Geräte-Set*/
        boolean reloadGeraete = false, reloadGeraetePuffer = false;
        if (pImmerLaden || reloadServer || reloadServerPuffer
                || eclParamM.getEclGeraeteSet().reloadVersionGeraete != pBvReload.reloadParameterGeraete) {
            EclGeraeteSet lEclGeraeteSet = null;
            lEclGeraeteSet = blmPuffer.getEclGeraeteSet();
            reloadGeraete = true;

            if (lEclGeraeteSet == null || lEclGeraeteSet.reloadVersionGeraete != pBvReload.reloadParameterGeraete) {
                rc = blmFuellePuffer.fuelleEclGeraeteSet(pBvReload.reloadParameterGeraete);
                if (rc < 0) {
                    CaBug.drucke("BlMFuelleEclMAusPufferOderDBEE.fuelleGlobalParam 001 - Geräteset nicht gefunden");
                    return rc;
                }
                reloadGeraete = true;
                reloadGeraetePuffer = true;
                lEclGeraeteSet = blmPuffer.getEclGeraeteSet();
            }
            if (lEclGeraeteSet == null) {
                lEclGeraeteSet = blmPuffer.getEclGeraeteSet();
            }
            eclParamM.setEclGeraeteSet(lEclGeraeteSet);
            eclDbM.getDbBundle().eclGeraeteSet = lEclGeraeteSet;
        }

        /*Geräte-Set-Zuordnung*/
        if (pImmerLaden || reloadGeraete || reloadGeraetePuffer) {
            /*Überhaupt schon eingelesen?*/
            if (blmPuffer.pruefeObVorhandenEclGeraetKlasseSetZuordnung() == false || reloadGeraetePuffer) {
                blmFuellePuffer.fuelleEclGeraetKlasseSetZuordnung();
                if (blmPuffer.pruefeObVorhandenEclGeraetKlasseSetZuordnung() == false) {
                    blmFuellePuffer.fuelleEclGeraetKlasseSetZuordnung();
                    while (blmPuffer.pruefeObVorhandenEclGeraetKlasseSetZuordnung() == false) {
                        blmFuellePuffer.fuelleEclGeraetKlasseSetZuordnung();
                    }
                }
            }

            EclGeraetKlasseSetZuordnung lGeraetKlasseSetZuordnung = null;
            lGeraetKlasseSetZuordnung = blmPuffer
                    .getEclGeraetKlasseSetZuordnung(eclParamM.getClGlobalVar().arbeitsplatz);
            if (lGeraetKlasseSetZuordnung == null) {
                CaBug.drucke("BlMFuelleEclMAusPufferOderDBEE.fuelleGlobalParam 002 - Klassenzuordnung für Gerät "
                        + eclParamM.getClGlobalVar().arbeitsplatz + "nicht gefunden");
                return CaFehler.sysGeraeteKlasseSetZuordnungNichtVorhanden;
            }

            eclParamM.setEclGeraetKlasseSetZuordnung(lGeraetKlasseSetZuordnung);
            eclDbM.getDbBundle().eclGeraetKlasseSetZuordnung = lGeraetKlasseSetZuordnung;
        }

        /*Geräte-Klasse*/
        if (pImmerLaden || reloadGeraete || reloadGeraetePuffer) {
            /*Überhaupt schon eingelesen?*/
            if (blmPuffer.pruefeObVorhandenParamGeraet() == false || reloadGeraetePuffer) {
                blmFuellePuffer.fuelleParamGeraet();
                if (blmPuffer.pruefeObVorhandenParamGeraet() == false) {
                    blmFuellePuffer.fuelleParamGeraet();
                }
            }

            ParamGeraet lParamGeraet = null;
            lParamGeraet = blmPuffer.getParamGeraetZuGeraetenummer(eclParamM.getClGlobalVar().arbeitsplatz);
            if (lParamGeraet == null) {
                CaBug.drucke("BlMFuelleEclMAusPufferOderDBEE.fuelleGlobalParam 003 - Klasse für Gerät "
                        + eclParamM.getClGlobalVar().arbeitsplatz + "nicht gefunden");
                return CaFehler.sysGeraeteKlasseNichtVorhanden;
            }

            eclParamM.setParamGeraet(lParamGeraet);
            eclDbM.getDbBundle().paramGeraet = lParamGeraet;

            ParamGeraet lParamGeraet9999 = blmPuffer.getParamGeraetZuGeraetenummerServer9999();
            if (lParamGeraet9999 == null) {
                CaBug.drucke(
                        "BlMFuelleEclMAusPufferOderDBEE.fuelleGlobalParam 004 - Parameter für Gerät 9999 - Server nicht gefunden");
                return CaFehler.sysGeraeteKlasseNichtVorhanden;
            }

            /*GlobalVar für Server*/
            if (eclParamM.getClGlobalVar() == null) {
                eclParamM.setClGlobalVar(new ClGlobalVar());
            }
            eclParamM.getClGlobalVar().lwPfadAllgemein = lParamGeraet9999.lwPfadAllgemein;
            eclParamM.getClGlobalVar().lwPfadGrossdokumente = lParamGeraet9999.lwPfadGrossdokumente;
            eclParamM.getClGlobalVar().lwPfadSicherung1 = lParamGeraet9999.lwPfadSicherung1;
            eclParamM.getClGlobalVar().lwPfadSicherung2 = lParamGeraet9999.lwPfadSicherung2;
            eclParamM.getClGlobalVar().lwPfadExportFuerPraesentation = lParamGeraet9999.lwPfadExportFuerPraesentation;
            eclParamM.getClGlobalVar().lwPfadExportFuerBuehnensystem = lParamGeraet9999.lwPfadExportFuerBuehnensystem;
            eclParamM
                    .getClGlobalVar().lwPfadExportExcelFuerPowerpoint = lParamGeraet9999.lwPfadExportExcelFuerPowerpoint;
            eclDbM.getDbBundle().clGlobalVar = eclParamM.getClGlobalVar();
        }

        /*Emittenten - hier wird nur sichergestellt, dass die Emittentenliste überhaupt gefüllt ist, und die richtige Version hat*/
        if (pImmerLaden || blmPuffer.getEmittentenArray() == null || blmPuffer.getEmittentenArray().length == 0
                || blmPuffer.getEmittentenArray()[0].reloadVersionEmittenten != pBvReload.reloadEmittenten) {
            blmFuellePuffer.fuelleEmittentenArray(pBvReload.reloadEmittenten);
        }

        return 1;
    }

    public int fuelleMandantenParam(boolean pImmerLaden, BvReload pBvReload) {
        if (eclParamM.getClGlobalVar().mandant==0) {
            CaBug.druckeLog("mit mandant==0 aufgerufen", logDrucken, 3);
            return 1;
        }
        /*Emittenten - hier nun konkreten Emittenten laden*/
        if (pImmerLaden || eclParamM.getEclEmittent() == null
                || eclParamM.getEclEmittent().reloadVersionEmittenten != pBvReload.reloadEmittenten) {
            EclEmittenten lEmittenten = new EclEmittenten();
            lEmittenten = blmPuffer.getEmittent(eclParamM.getClGlobalVar().mandant, eclParamM.getClGlobalVar().hvJahr,
                    eclParamM.getClGlobalVar().hvNummer, eclParamM.getClGlobalVar().datenbereich);
            if (lEmittenten == null || lEmittenten.reloadVersionEmittenten != pBvReload.reloadEmittenten) {
                if (logDrucken >= 1) {
                    CaBug.druckeLog("BlmFuelleEclMAusPufferOderDBEE.fuelleGlobalParam Emittenten Nachladen", logDrucken,
                            1);
                    if (lEmittenten == null) {
                        CaBug.druckeLog("lEmittenten ist null", logDrucken, 1);
                    } else {
                        CaBug.druckeLog("lEmittenten.reloadVersionEmittenten=" + lEmittenten.reloadVersionEmittenten,
                                logDrucken, 10);
                    }
                    CaBug.druckeLog("pBvReload.reloadEmittenten=" + pBvReload.reloadEmittenten, logDrucken, 10);
                }
                blmFuellePuffer.fuelleEmittentenArray(pBvReload.reloadEmittenten);
                lEmittenten = blmPuffer.getEmittent(eclParamM.getClGlobalVar().mandant,
                        eclParamM.getClGlobalVar().hvJahr, eclParamM.getClGlobalVar().hvNummer,
                        eclParamM.getClGlobalVar().datenbereich);
            }
            eclParamM.setEclEmittent(lEmittenten);
            eclDbM.getDbBundle().eclEmittent = lEmittenten;
        }

        boolean hvParameterNeuGeladen = false;
        /*HV-Parameter*/
        if (pImmerLaden || eclParamM.getParam().reloadVersionParameter != pBvReload.reloadParameter) {
            CaBug.druckeLog("HV-Parameter laden", logDrucken, 1);
            HVParam lHVParam = new HVParam();
            ParamLogo lParamLogo = new ParamLogo();
            ParamKeys lParamKeys=new ParamKeys();

            EhGetHVParam ehGetHVParam = blmPuffer.getHVParam(eclParamM.getClGlobalVar().mandant, eclParamM.getClGlobalVar().hvJahr,
                    eclParamM.getClGlobalVar().hvNummer, eclParamM.getClGlobalVar().datenbereich);
            if (ehGetHVParam!=null) {
                lHVParam =ehGetHVParam.hvParam;
                lParamLogo = ehGetHVParam.paramLogo;
                lParamKeys=ehGetHVParam.paramKeys;
            }

            if (lHVParam == null || lHVParam.reloadVersionParameter != pBvReload.reloadParameter) {
                blmFuellePuffer.fuelleMandantenParam(pBvReload.reloadParameter);
                EhGetHVParam ehGetHVParam1=blmPuffer.getHVParam(eclParamM.getClGlobalVar().mandant, eclParamM.getClGlobalVar().hvJahr,
                        eclParamM.getClGlobalVar().hvNummer, eclParamM.getClGlobalVar().datenbereich);
                lHVParam =ehGetHVParam1.hvParam;
                lParamLogo = ehGetHVParam1.paramLogo;
                lParamKeys=ehGetHVParam1.paramKeys;
                hvParameterNeuGeladen = true;
            }

            eclParamM.setParam(lHVParam);
            eclParamM.setParamLogo(lParamLogo);
            eclParamM.setParamKeys(lParamKeys);
            eclDbM.getDbBundle().param = lHVParam;

            /*TechnischeTermine derzeit hier drin*/
            EclTermine[] lTechnischeTermineArray = null;
            EhGetTechnischeTermine ehGetTechnischeTermine= blmPuffer.getTechnischeTermine(eclParamM.getClGlobalVar().mandant,
                    eclParamM.getClGlobalVar().hvJahr, eclParamM.getClGlobalVar().hvNummer,
                    eclParamM.getClGlobalVar().datenbereich);
            int rcVersion =ehGetTechnischeTermine.tempTechnischeTermineVersion;
            lTechnischeTermineArray = ehGetTechnischeTermine.tempTechnischeTermineArray;
            
            if (rcVersion == -1 || lTechnischeTermineArray == null || rcVersion != pBvReload.reloadParameter) {
                blmFuellePuffer.fuelleTechnischeTermine(pBvReload.reloadParameter);
                ehGetTechnischeTermine = blmPuffer.getTechnischeTermine(eclParamM.getClGlobalVar().mandant,
                        eclParamM.getClGlobalVar().hvJahr, eclParamM.getClGlobalVar().hvNummer,
                        eclParamM.getClGlobalVar().datenbereich);
                rcVersion=ehGetTechnischeTermine.tempTechnischeTermineVersion;
                lTechnischeTermineArray = ehGetTechnischeTermine.tempTechnischeTermineArray;
            }
            eclParamM.setTerminlisteTechnisch(lTechnischeTermineArray);
            eclDbM.getDbBundle().terminlisteTechnisch = lTechnischeTermineArray;

        }

        /*Texte*/
        if (pImmerLaden || eclTextePortalM.getTexteVersion() != pBvReload.reloadTexte ||
        /*Es wird zusätzlich noch Parameter-Änderung abgefragt, um einen "Auto-Reload" auch
         * der Texte zu erreichen (z.B. wenn nur globale Texte geändert wurden)
         */
                hvParameterNeuGeladen == true) {
            CaBug.druckeLog("Texte laden", logDrucken, 1);
            EclFehler lFehlerDeutschArray[] = null;
            EclFehler lFehlerEnglischArray[] = null;
            String[] lPortalTexteDEArray = null;
            String[] lPortalTexteENArray = null;
            String[] lPortalTexteAdaptivDEArray = null;
            String[] lPortalTexteAdaptivENArray = null;
            
            EhGetHVTexte ehGetHVTexte = blmPuffer.getHVTexte(eclParamM.getClGlobalVar().mandant, eclParamM.getClGlobalVar().hvJahr,
                    eclParamM.getClGlobalVar().hvNummer, eclParamM.getClGlobalVar().datenbereich);
            int rcVersion = ehGetHVTexte.tempTexteVersion;
            lFehlerDeutschArray = ehGetHVTexte.tempFehlerDeutschArray;
            lFehlerEnglischArray = ehGetHVTexte.tempFehlerEnglischArray;
            lPortalTexteDEArray = ehGetHVTexte.tempPortalTexteDEArray;
            lPortalTexteENArray = ehGetHVTexte.tempPortalTexteENArray;
            lPortalTexteAdaptivDEArray = ehGetHVTexte.tempPortalTexteAdaptivDEArray;
            lPortalTexteAdaptivENArray = ehGetHVTexte.tempPortalTexteAdaptivENArray;

            CaBug.druckeLog("rcVersion="+rcVersion+" pBvReload.reloadTexte="+pBvReload.reloadTexte+" hvParameterNeuGeladen="+hvParameterNeuGeladen+" lFehlerDeutschArray==null="+(lFehlerDeutschArray==null), logDrucken, 3);
            if (rcVersion == -1 || rcVersion != pBvReload.reloadTexte ||
            /*Es wird zusätzlich noch Parameter-Änderung abgefragt, um einen "Auto-Reload" auch
             * der Texte zu erreichen (z.B. wenn nur globale Texte geändert wurden)
             */
                    hvParameterNeuGeladen == true || lFehlerDeutschArray == null) {
                blmFuellePuffer.fuelleMandantenTexte(pBvReload.reloadTexte);
                
                
                ehGetHVTexte = blmPuffer.getHVTexte(eclParamM.getClGlobalVar().mandant, eclParamM.getClGlobalVar().hvJahr,
                        eclParamM.getClGlobalVar().hvNummer, eclParamM.getClGlobalVar().datenbereich);
                rcVersion = ehGetHVTexte.tempTexteVersion;
                lFehlerDeutschArray = ehGetHVTexte.tempFehlerDeutschArray;
                lFehlerEnglischArray = ehGetHVTexte.tempFehlerEnglischArray;
                lPortalTexteDEArray = ehGetHVTexte.tempPortalTexteDEArray;
                lPortalTexteENArray = ehGetHVTexte.tempPortalTexteENArray;
                lPortalTexteAdaptivDEArray = ehGetHVTexte.tempPortalTexteAdaptivDEArray;
                lPortalTexteAdaptivENArray = ehGetHVTexte.tempPortalTexteAdaptivENArray;
            }

            /*Fehlermeldung*/
            eclTextePortalM.setFehlerDeutschArray(lFehlerDeutschArray);
            eclDbM.getDbBundle().fehlerDeutschArray = lFehlerDeutschArray;

            eclTextePortalM.setEnglischarray(lFehlerEnglischArray);
            eclDbM.getDbBundle().fehlerEnglischArray = lFehlerEnglischArray;

            /*Texte - nicht in DbBundle enthalten*/
            eclTextePortalM.setPortalTexteDEArray(lPortalTexteDEArray);
            eclTextePortalM.setPortalTexteENArray(lPortalTexteENArray);
            eclTextePortalM.setPortalTexteAdaptivDEArray(lPortalTexteAdaptivDEArray);
            eclTextePortalM.setPortalTexteAdaptivENArray(lPortalTexteAdaptivENArray);
        }

        /*Abstimmungen/Weisungen*/
        if (pImmerLaden || eclAbstimmungSetM.getVersionAbstimmungenAktuell() != pBvReload.reloadAbstimmungen
                || eclAbstimmungSetM.getVersionWeisungenAktuell() != pBvReload.reloadWeisungen || eclAbstimmungSetM
                        .getVersionAbstimmungenWeisungenOhneAbbruchAktuell() != pBvReload.reloadAbstimmungenWeisungenOhneAbbruch) {
            CaBug.druckeLog("Abstimmungen/Weisungen neu laden", logDrucken, 1);
            
            EhGetAbstimmungen ehGetAbstimmungen=
                    blmPuffer.getAbstimmungen(eclParamM.getClGlobalVar().mandant, eclParamM.getClGlobalVar().hvJahr,
                    eclParamM.getClGlobalVar().hvNummer, eclParamM.getClGlobalVar().datenbereich);
            EclAbstimmungSet lAbstimmungSet = ehGetAbstimmungen.tempAbstimmungSet;
            int lVersionAbstimmungen = ehGetAbstimmungen.tempAbstimmungenVersion;
            int lVersionWeisungen = ehGetAbstimmungen.tempWeisungenVersion;
            int lVersionAbstimmungenWeisungenOhneAbbruch = ehGetAbstimmungen.tempAbstimmungenWeisungenOhneAbbruchVersion;

            CaBug.druckeLog("lVersionAbstimmungen=" + lVersionAbstimmungen, logDrucken, 10);
            CaBug.druckeLog("pBvReload.reloadAbstimmungen=" + pBvReload.reloadAbstimmungen, logDrucken, 10);

            if (lAbstimmungSet == null || lVersionAbstimmungen != pBvReload.reloadAbstimmungen
                    || lVersionWeisungen != pBvReload.reloadWeisungen
                    || lVersionAbstimmungenWeisungenOhneAbbruch != pBvReload.reloadAbstimmungenWeisungenOhneAbbruch) {
                CaBug.druckeLog("Abstimmungen/Weisungen neu belegen - erneut blmFuellePuffer aufrufen", logDrucken, 1);
                blmFuellePuffer.fuelleAbstimmungSet(pBvReload.reloadWeisungen, pBvReload.reloadAbstimmungen,
                        pBvReload.reloadAbstimmungenWeisungenOhneAbbruch);
                ehGetAbstimmungen=blmPuffer.getAbstimmungen(eclParamM.getClGlobalVar().mandant, eclParamM.getClGlobalVar().hvJahr,
                        eclParamM.getClGlobalVar().hvNummer, eclParamM.getClGlobalVar().datenbereich);
                lAbstimmungSet = ehGetAbstimmungen.tempAbstimmungSet;
                lVersionAbstimmungen = ehGetAbstimmungen.tempAbstimmungenVersion;
                lVersionWeisungen = ehGetAbstimmungen.tempWeisungenVersion;
                lVersionAbstimmungenWeisungenOhneAbbruch = ehGetAbstimmungen.tempAbstimmungenWeisungenOhneAbbruchVersion;
            }

            eclAbstimmungSetM.setVersionAbstimmungenAktuell(lVersionAbstimmungen);
            eclAbstimmungSetM.setVersionWeisungenAktuell(lVersionWeisungen);
            CaBug.druckeLog("lVersionWeisungen="+lVersionWeisungen, logDrucken, 10);
            eclAbstimmungSetM
                    .setVersionAbstimmungenWeisungenOhneAbbruchAktuell(lVersionAbstimmungenWeisungenOhneAbbruch);
            eclAbstimmungSetM.setAbstimmungSet(lAbstimmungSet);

        }

        return 1;
    }

    /**Kann immer aufgerufen werden - "noch kein User eingeladen" und trotzdem pImmerLaden=false ist abgefangen!*/
    public int fuelleUserLogin(boolean pImmerLaden, BvReload pBvReload) {
        if (pImmerLaden || (eclParamM.getEclUserLogin() != null
                && eclParamM.getEclUserLogin().reloadVersionUserLogin != pBvReload.reloadUserLogin)) {
            EclUserLogin lUserLogin = new EclUserLogin();
            lUserLogin = blmPuffer.getEclUserLogin(eclParamM.getClGlobalVar().benutzernr);
            if (lUserLogin == null || lUserLogin.reloadVersionUserLogin != pBvReload.reloadUserLogin) {
                blmFuellePuffer.fuelleUserLogin(eclParamM.getClGlobalVar().benutzernr, pBvReload.reloadUserLogin);
                lUserLogin = blmPuffer.getEclUserLogin(eclParamM.getClGlobalVar().benutzernr);
            }
            eclParamM.setEclUserLogin(lUserLogin);
            eclDbM.getDbBundle().eclUserLogin = lUserLogin;
        }

        return 1;
    }

}
