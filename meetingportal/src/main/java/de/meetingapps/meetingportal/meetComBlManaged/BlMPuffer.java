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

import java.io.Serializable;
import java.util.ArrayList;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComEclM.EclParamM;
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
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

/**
 * Hält häufig benötigte globale Daten gepuffert im Speicher. Zugriff erfolgt über BlMFuellePuffer: > Füllen der Puffer
 * > Übertragen der Puffer in die Session-Scoped-Entities, die dann in den Application Beans benutzt werden können
 *
 */
@ApplicationScoped
@Named
public class BlMPuffer implements Serializable {
    private static final long serialVersionUID = -286884543483611201L;

    /** Auf true setzen => Das Laden und der Zugriff auf die Puffer wird im Systemlog protokolliert */
    private int logDrucken = 3;

    @Inject
    EclParamM eclParamM;

    /** Löschen aller Puffer aller Mandanten */
    synchronized public void clearAllPuffer() {
        klassenNummerFuerServer9999 = 0;
        paramServer = null;
        eclGeraeteSet = null;
        eclGeraetKlasseSetZuordnungList = null;
        eclGeraetKlasseSetZuordnungListTemp = null;
        paramGeraetList = null;
        paramGeraetListTemp = null;
        paramListe = null;
        texteVerion = null;
        deutschFehlerListe = null;
        englischFehlerListe = null;
        portalTexteDE = null;
        portalTexteEN = null;
        portalTexteAdaptivDE = null;
        portalTexteAdaptivEN = null;
        terminListenVersion = null;
        terminListen = null;
        weisungenVersion = null;
        abstimmungenVersion = null;
        abstimmungenWeisungenOhneAbbruchVersion = null;
        abstimmungSet = null;
        emittentenArray = null;
        userLoginListe = null;
        CaBug.druckeLog("BlMPuffer.clearAllPuffer durchgeführt", logDrucken, 1);
    }

    /************ Hilfswerte - nur intern **************************************************/
    /**
     * Nummer der Klasse, die für den Arbeitsplatz 9999=Server verwendet wird. Wird gesetzt von
     * addEclGeraetKlasseSetZuordnung, und verwendet in addParamGeraet um in ClGlobalVar die entsprechenden Parameter zu
     * setzen
     */
    private int klassenNummerFuerServer9999 = 0;

    /******************************* Server-Parameter **************************************/
    private ParamServer paramServer = null;

    synchronized public void setParamServer(ParamServer pParamServer, int pReloadVersion) {
        paramServer = pParamServer;
        paramServer.reloadVersionParameterServer = pReloadVersion;
    }

    /**
     * Kann null liefern. Muß solange aufgerufen werden, bis es != null ist (wg. Synchronisierungseffekt mit set!
     */
    synchronized public ParamServer getParamServer() {
        return paramServer;
    }

    /****************************** Geräteset **************************************************/
    private EclGeraeteSet eclGeraeteSet = null;

    synchronized public void setEclGeraeteSet(EclGeraeteSet pEclGeraeteSet, int pReloadVersion) {
        eclGeraeteSet = pEclGeraeteSet;
        eclGeraeteSet.reloadVersionGeraete = pReloadVersion;
    }

    /**
     * Kann null liefern. Muß solange aufgerufen werden, bis es != null ist (wg. Synchronisierungseffekt mit set!
     */
    synchronized public EclGeraeteSet getEclGeraeteSet() {
        return eclGeraeteSet;
    }

    /******************** Zuordnung Geräte Klasse - nur für das aktive Set! ************************/
    private ArrayList<EclGeraetKlasseSetZuordnung> eclGeraetKlasseSetZuordnungList = null;

    /* Nur zum temporären Übertragen */
    private ArrayList<EclGeraetKlasseSetZuordnung> eclGeraetKlasseSetZuordnungListTemp = null;

    /**** Schreib-Funktionen ****/
    synchronized public void setEclGeraetKlasseSetZuordnungStart() {
        eclGeraetKlasseSetZuordnungListTemp = new ArrayList<EclGeraetKlasseSetZuordnung>();
    }

    synchronized public void addEclGeraetKlasseSetZuordnung(EclGeraetKlasseSetZuordnung pEclGeraetKlasseSetZuordnung) {
        int gef = 0;
        for (int i = 0; i < eclGeraetKlasseSetZuordnungListTemp.size(); i++) {
            if (eclGeraetKlasseSetZuordnungListTemp
                    .get(i).geraeteNummer == pEclGeraetKlasseSetZuordnung.geraeteNummer) {
                gef = 1;
            }
        }
        if (gef == 0) {
            eclGeraetKlasseSetZuordnungListTemp.add(pEclGeraetKlasseSetZuordnung);
        }
        if (pEclGeraetKlasseSetZuordnung.geraeteNummer == 9999) {
            klassenNummerFuerServer9999 = pEclGeraetKlasseSetZuordnung.geraeteKlasseIdent;
        }
    }

    synchronized public void setEclGeraetKlasseSetZuordnungEnd() {
        eclGeraetKlasseSetZuordnungList = eclGeraetKlasseSetZuordnungListTemp;
        if (klassenNummerFuerServer9999 == 0) {
            CaBug.drucke("BlMPuffer.setEclGeraetKlasseSetZuordnungEnd 001 Server 9999 keine Klasse zugeordnet");
        }
    }

    /**** Lese-Funktionen ****/
    public boolean pruefeObVorhandenEclGeraetKlasseSetZuordnung() {
        if (eclGeraetKlasseSetZuordnungList == null) {
            return false;
        }
        return true;
    }

    public EclGeraetKlasseSetZuordnung getEclGeraetKlasseSetZuordnung(int pGeraeteNummer) {
        if (eclGeraetKlasseSetZuordnungList == null) {
            CaBug.drucke("BlMPuffer.getEclGeraetKlasseSetZuordnung 001");
            return null;
        }
        for (int i = 0; i < eclGeraetKlasseSetZuordnungList.size(); i++) {
            if (eclGeraetKlasseSetZuordnungList.get(i).geraeteNummer == pGeraeteNummer) {
                return eclGeraetKlasseSetZuordnungList.get(i);
            }
        }
        CaBug.drucke("BlMPuffer.getEclGeraetKlasseSetZuordnung 002 - Gerät " + pGeraeteNummer + " nicht gefunden");
        return null;
    }

    /******************** Geräteklassen, die in aktivem Set vorkommen ************************************/
    private ArrayList<ParamGeraet> paramGeraetList = null;

    /* Nur zum temporären Übertragen */
    private ArrayList<ParamGeraet> paramGeraetListTemp = null;

    /**** Schreib-Funktionen ****/
    synchronized public void setParamGeraetStart() {
        paramGeraetListTemp = new ArrayList<ParamGeraet>();
    }

    synchronized public void addParamGeraet(ParamGeraet pParamGeraet) {
        int gef = 0;
        for (int i = 0; i < paramGeraetListTemp.size(); i++) {
            if (paramGeraetListTemp.get(i).identKlasse == pParamGeraet.identKlasse) {
                gef = 1;
            }
        }
        if (gef == 0) {
            paramGeraetListTemp.add(pParamGeraet);
        }
    }

    synchronized public void setParamGeraetEnd() {
        paramGeraetList = paramGeraetListTemp;
    }

    /**** Lese-Funktionen ****/
    public boolean pruefeObVorhandenParamGeraet() {
        if (paramGeraetList == null) {
            return false;
        }
        return true;
    }

    public ParamGeraet getParamGeraetZuKlasse(int pKlassenNummer) {
        if (paramGeraetList == null) {
            CaBug.drucke("BlMPuffer.getParamGeraetZuKlasse 001");
            return null;
        }
        for (int i = 0; i < paramGeraetList.size(); i++) {
            if (paramGeraetList.get(i).identKlasse == pKlassenNummer) {
                return paramGeraetList.get(i);
            }
        }
        CaBug.drucke("BlMPuffer.getParamGeraetZuKlasse 002 - Klasse " + pKlassenNummer + " nicht gefunden");
        return null;
    }

    public ParamGeraet getParamGeraetZuGeraetenummer(int pGeraeteNummer) {
        int klassennummer = 0;
        if (paramGeraetList == null) {
            CaBug.drucke("BlMPuffer.getParamGeraetZuGeraetenummer 001");
            return null;
        }
        if (eclGeraetKlasseSetZuordnungList == null) {
            CaBug.drucke("BlMPuffer.getParamGeraetZuGeraetenummer 003");
            return null;
        }
        /* Klasse aus Zuordnungstabelle ermitteln */
        for (int i = 0; i < eclGeraetKlasseSetZuordnungList.size(); i++) {
            if (eclGeraetKlasseSetZuordnungList.get(i).geraeteNummer == pGeraeteNummer) {
                klassennummer = eclGeraetKlasseSetZuordnungList.get(i).geraeteKlasseIdent;
            }
        }
        if (klassennummer == 0) {
            CaBug.drucke("BlMPuffer.getParamGeraetZuGeraetenummer 004 Gerät in Zuordnung nicht gefunden");
            return null;
        }
        for (int i = 0; i < paramGeraetList.size(); i++) {
            if (paramGeraetList.get(i).identKlasse == klassennummer) {
                return paramGeraetList.get(i);
            }
        }
        CaBug.drucke("BlMPuffer.getParamGeraetZuGeraetenummer 002 - Klasse " + klassennummer + " nicht gefunden");
        return null;
    }

    synchronized public ParamGeraet getParamGeraetZuGeraetenummerServer9999() {
        if (paramGeraetList == null) {
            CaBug.drucke("BlMPuffer.getParamGeraetZuGeraetenummer 001");
            return null;
        }
        if (eclGeraetKlasseSetZuordnungList == null) {
            CaBug.drucke("BlMPuffer.getParamGeraetZuGeraetenummer 003");
            return null;
        }

        /* Klasse aus Zuordnungstabelle ermitteln */
        for (int i = 0; i < paramGeraetList.size(); i++) {
            if (paramGeraetList.get(i).identKlasse == klassenNummerFuerServer9999) {
                return paramGeraetList.get(i);
            }
        }
        CaBug.drucke("BlMPuffer.getParamGeraetZuGeraetenummerServer9999 002 - Klasse für Gerät 9999 nicht gefunden");
        return null;
    }

    /************************** HVParam **********************************************/
    private ArrayList<HVParam> paramListe = null;
    private ArrayList<ParamLogo> paramLogoListe = null;
    private ArrayList<ParamKeys> paramKeysListe=null;

    /*
     * Hinweis zu den folgenden ArrayLists: diese werden beim Einlesen von HVParam reserviert, damit die Elemente an
     * selber Stelle sind wie in paramListe und über die Position in paramListe gefunden werden können. Eingetragen
     * werden sie dann mit separaten Funktionen!
     */
    private ArrayList<Integer> texteVerion = null; // Gilt für alle folgenden Text-Arrays
    private ArrayList<EclFehler[]> deutschFehlerListe = null;
    private ArrayList<EclFehler[]> englischFehlerListe = null;
    private ArrayList<String[]> portalTexteDE = null;
    private ArrayList<String[]> portalTexteEN = null;
    private ArrayList<String[]> portalTexteAdaptivDE = null;
    private ArrayList<String[]> portalTexteAdaptivEN = null;

    private ArrayList<Integer> terminListenVersion = null; // Derzeit identisch mit Parametern. Kann aber getrennt
                                                           // werden :-)
    private ArrayList<EclTermine[]> terminListen = null;

    private ArrayList<Integer> weisungenVersion = null;
    private ArrayList<Integer> abstimmungenVersion = null;
    private ArrayList<Integer> abstimmungenWeisungenOhneAbbruchVersion = null;
    private ArrayList<EclAbstimmungSet> abstimmungSet = null;

    synchronized public void addOderReplaceHVParam(HVParam pHVParam, ParamLogo pParamLogo, ParamKeys pParamKeys, int pReloadParameter) {
        int gef = -1;
        pHVParam.reloadVersionParameter = pReloadParameter;

        if (paramListe == null) {
            paramListe = new ArrayList<HVParam>();
            paramLogoListe = new ArrayList<ParamLogo>();
            paramKeysListe = new ArrayList<ParamKeys>();

            texteVerion = new ArrayList<Integer>();
            deutschFehlerListe = new ArrayList<EclFehler[]>();
            englischFehlerListe = new ArrayList<EclFehler[]>();
            portalTexteDE = new ArrayList<String[]>();
            portalTexteEN = new ArrayList<String[]>();
            portalTexteAdaptivDE = new ArrayList<String[]>();
            portalTexteAdaptivEN = new ArrayList<String[]>();

            terminListenVersion = new ArrayList<Integer>();
            terminListen = new ArrayList<EclTermine[]>();

            weisungenVersion = new ArrayList<Integer>();
            abstimmungenVersion = new ArrayList<Integer>();
            abstimmungenWeisungenOhneAbbruchVersion = new ArrayList<Integer>();
            abstimmungSet = new ArrayList<EclAbstimmungSet>();

        }

        for (int i = 0; i < paramListe.size(); i++) {
            HVParam hHVParam = paramListe.get(i);
            if (hHVParam.mandant == pHVParam.mandant && hHVParam.hvJahr == pHVParam.hvJahr
                    && hHVParam.hvNummer.compareTo(pHVParam.hvNummer) == 0
                    && hHVParam.datenbereich.compareTo(pHVParam.datenbereich) == 0) {
                gef = i;
                paramListe.set(i, pHVParam);
                paramLogoListe.set(i, pParamLogo);
                paramKeysListe.set(i, pParamKeys);

            }
        }
        if (gef == -1) {
            paramListe.add(pHVParam);
            paramLogoListe.add(pParamLogo);
            paramKeysListe.add(pParamKeys);

            Integer hInt = Integer.valueOf(0);
            texteVerion.add(hInt);
            deutschFehlerListe.add(null);
            englischFehlerListe.add(null);
            portalTexteDE.add(null);
            portalTexteEN.add(null);
            portalTexteAdaptivDE.add(null);
            portalTexteAdaptivEN.add(null);

            terminListenVersion.add(hInt);
            terminListen.add(null);

            weisungenVersion.add(hInt);
            abstimmungenVersion.add(hInt);
            abstimmungenWeisungenOhneAbbruchVersion.add(hInt);
            abstimmungSet.add(null);

        }
    }


    /** Füllt auch paramLogo - kann danach mit get geholt werden */
    synchronized public EhGetHVParam getHVParam(int pMandant, int pHVJahr, String pHVNummer, String pDatenbereich) {
        EhGetHVParam ehGetHVParam=new EhGetHVParam(); 
        if (paramListe == null) {
            return null;
        }
        for (int i = 0; i < paramListe.size(); i++) {
            HVParam hHVParam = paramListe.get(i);
            if (hHVParam.mandant == pMandant && hHVParam.hvJahr == pHVJahr
                    && hHVParam.hvNummer.compareTo(pHVNummer) == 0
                    && hHVParam.datenbereich.compareTo(pDatenbereich) == 0) {
                ehGetHVParam.paramLogo=paramLogoListe.get(i);
                ehGetHVParam.paramKeys=paramKeysListe.get(i);
                ehGetHVParam.hvParam=hHVParam;
                return ehGetHVParam;
            }
        }
        return null;
    }


    /****************************** Texte für Portal etc. *******************************************/
    /* Listen werden in Zusammenhang mit HV-Parametern initialisiert und sind an selber Stelle zu finden! */

    synchronized public void addOderReplaceHVTexte(ClGlobalVar pClGlobalVar, EclFehler pFehlerDeutschArray[],
            EclFehler pFehlerEnglischArray[], String[] pPortalTextDE, String[] pPortalAdaptivTextDE,
            String[] pPortalTextEN, String[] pPortalAdaptivTextEN, int pReloadTexte) {
        int gef = -1;

        if (paramListe == null) {
            CaBug.drucke("001");
            return;
        }
        for (int i = 0; i < paramListe.size(); i++) {
            HVParam hHVParam = paramListe.get(i);
            if (hHVParam.mandant == pClGlobalVar.mandant && hHVParam.hvJahr == pClGlobalVar.hvJahr
                    && hHVParam.hvNummer.compareTo(pClGlobalVar.hvNummer) == 0
                    && hHVParam.datenbereich.compareTo(pClGlobalVar.datenbereich) == 0) {
                Integer hInt = Integer.valueOf(pReloadTexte);
                texteVerion.set(i, hInt);
                deutschFehlerListe.set(i, pFehlerDeutschArray);
                englischFehlerListe.set(i, pFehlerEnglischArray);
                portalTexteDE.set(i, pPortalTextDE);
                portalTexteEN.set(i, pPortalTextEN);
                portalTexteAdaptivDE.set(i, pPortalAdaptivTextDE);
                portalTexteAdaptivEN.set(i, pPortalAdaptivTextEN);

                gef = i;
            }
        }
        if (gef == -1) {
            CaBug.drucke("002");
        }
    }

    /** return=Versiosnummer => ok, =-1 => nicht gefunden */
    synchronized public EhGetHVTexte getHVTexte(int pMandant, int pHVJahr, String pHVNummer, String pDatenbereich) {
        
        EhGetHVTexte ehGetHVTexte=new EhGetHVTexte();
        
        if (paramListe == null) {
            ehGetHVTexte.tempTexteVersion=-1;
            ehGetHVTexte.tempFehlerDeutschArray = null;
            ehGetHVTexte.tempFehlerEnglischArray = null;
            ehGetHVTexte.tempPortalTexteDEArray = null;
            ehGetHVTexte.tempPortalTexteENArray = null;
            ehGetHVTexte.tempPortalTexteAdaptivDEArray = null;
            ehGetHVTexte.tempPortalTexteAdaptivENArray = null;
            CaBug.druckeLog("Return 1", logDrucken, 10);
            return ehGetHVTexte;
        }
        for (int i = 0; i < paramListe.size(); i++) {
            HVParam hHVParam = paramListe.get(i);
            if (hHVParam.mandant == pMandant && hHVParam.hvJahr == pHVJahr
                    && hHVParam.hvNummer.compareTo(pHVNummer) == 0
                    && hHVParam.datenbereich.compareTo(pDatenbereich) == 0) {
                ehGetHVTexte.tempFehlerDeutschArray = deutschFehlerListe.get(i);
                ehGetHVTexte.tempFehlerEnglischArray = englischFehlerListe.get(i);
                ehGetHVTexte.tempPortalTexteDEArray = portalTexteDE.get(i);
                ehGetHVTexte.tempPortalTexteENArray = portalTexteEN.get(i);
                ehGetHVTexte.tempPortalTexteAdaptivDEArray = portalTexteAdaptivDE.get(i);
                ehGetHVTexte.tempPortalTexteAdaptivENArray = portalTexteAdaptivEN.get(i);
                ehGetHVTexte.tempTexteVersion = texteVerion.get(i);
                CaBug.druckeLog("Return 2", logDrucken, 10);
                return ehGetHVTexte;
            }
        }
        ehGetHVTexte.tempTexteVersion=-1;
        ehGetHVTexte.tempFehlerDeutschArray = null;
        ehGetHVTexte.tempFehlerEnglischArray = null;
        ehGetHVTexte.tempPortalTexteDEArray = null;
        ehGetHVTexte.tempPortalTexteENArray = null;
        ehGetHVTexte.tempPortalTexteAdaptivDEArray = null;
        ehGetHVTexte.tempPortalTexteAdaptivENArray = null;
        CaBug.druckeLog("Return 3", logDrucken, 10);
        return ehGetHVTexte;
    }

 
    /****************************** technischen Termine etc. *******************************************/
    /* Listen werden in Zusammenhang mit HV-Parametern initialisiert und sind an selber Stelle zu finden! */

    synchronized public void addOderReplaceTechnischeTermine(ClGlobalVar pClGlobalVar, EclTermine[] pTerminliste,
            int pReloadTermine) {
        int gef = -1;

        if (paramListe == null) {
            CaBug.drucke("BlmPuffer.addOderReplaceTechnischeTermine 001");
            return;
        }
        for (int i = 0; i < paramListe.size(); i++) {
            HVParam hHVParam = paramListe.get(i);
            if (hHVParam.mandant == pClGlobalVar.mandant && hHVParam.hvJahr == pClGlobalVar.hvJahr
                    && hHVParam.hvNummer.compareTo(pClGlobalVar.hvNummer) == 0
                    && hHVParam.datenbereich.compareTo(pClGlobalVar.datenbereich) == 0) {
                Integer hInt = Integer.valueOf(pReloadTermine);
                terminListenVersion.set(i, hInt);
                terminListen.set(i, pTerminliste);

                gef = i;
            }
        }
        if (gef == -1) {
            CaBug.drucke("BlmPuffer.addOderReplaceTechnischeTermine 002");
        }
    }


    /** return=Versiosnummer => ok, =-1 => nicht gefunden */
    synchronized public EhGetTechnischeTermine getTechnischeTermine(int pMandant, int pHVJahr, String pHVNummer, String pDatenbereich) {
        
        EhGetTechnischeTermine ehGetTechnischeTermine=new EhGetTechnischeTermine();
        
        if (paramListe == null) {
            ehGetTechnischeTermine.tempTechnischeTermineVersion=-1;
            return ehGetTechnischeTermine;
        }
        for (int i = 0; i < paramListe.size(); i++) {
            HVParam hHVParam = paramListe.get(i);
            if (hHVParam.mandant == pMandant && hHVParam.hvJahr == pHVJahr
                    && hHVParam.hvNummer.compareTo(pHVNummer) == 0
                    && hHVParam.datenbereich.compareTo(pDatenbereich) == 0) {
                ehGetTechnischeTermine.tempTechnischeTermineArray = terminListen.get(i);
                ehGetTechnischeTermine.tempTechnischeTermineVersion = terminListenVersion.get(i);
                return ehGetTechnischeTermine;
            }
        }
        ehGetTechnischeTermine.tempTechnischeTermineVersion=-1;
        return ehGetTechnischeTermine;
    }


    /********************* Emittenten *********************************/
    private EclEmittenten[] emittentenArray = null;

    synchronized public void setEmittentenArray(EclEmittenten[] pEmittentenArray, int pReloadVersion) {
        emittentenArray = pEmittentenArray;
        for (int i = 0; i < emittentenArray.length; i++) {
            emittentenArray[i].reloadVersionEmittenten = pReloadVersion;
        }
    }

    synchronized public EclEmittenten[] getEmittentenArray() {
        return emittentenArray;
    }

    public EclEmittenten getEmittent(int pMandant, int pHVJahr, String pHVNummer, String pDatenbereich) {
        if (emittentenArray == null) {
            return null;
        }
        for (int i = 0; i < emittentenArray.length; i++) {
            EclEmittenten hEmittenten = emittentenArray[i];
            if (hEmittenten.mandant == pMandant && hEmittenten.hvJahr == pHVJahr
                    && hEmittenten.hvNummer.compareTo(pHVNummer) == 0
                    && hEmittenten.dbArt.compareTo(pDatenbereich) == 0) {
                return hEmittenten;
            }
        }
        return null;
    }

    /** Liefert den Detail-Emittentsatz, für den das Aktionärsportal gerade aktiv ist */
    public EclEmittenten getStandardEmittentFuerEmittentenPortal(int pMandant) {
        if (emittentenArray == null) {
            return null;
        }
        for (int i = 0; i < emittentenArray.length; i++) {
            EclEmittenten hEmittenten = emittentenArray[i];
            if (hEmittenten.mandant == pMandant && hEmittenten.portalAktuellAktiv == 1
                    && hEmittenten.portalStandard == 1) {
                return hEmittenten;
            }
        }
        return null;
    }

    /********************************** UserLogin ************************************/
    private ArrayList<EclUserLogin> userLoginListe = null;

    synchronized public void addOderReplaceEclUserLogin(EclUserLogin pEclUserLogin, int pReloadUserLogin) {
        int gef = -1;
        pEclUserLogin.reloadVersionUserLogin = pReloadUserLogin;

        if (userLoginListe == null) {
            userLoginListe = new ArrayList<EclUserLogin>();
        }
        for (int i = 0; i < userLoginListe.size(); i++) {
            EclUserLogin hEclUserLogin = userLoginListe.get(i);
            if (hEclUserLogin.userLoginIdent == pEclUserLogin.userLoginIdent) {
                gef = i;
                userLoginListe.set(i, pEclUserLogin);
            }
        }
        if (gef == -1) {
            userLoginListe.add(pEclUserLogin);
        }
    }

    synchronized public EclUserLogin getEclUserLogin(int pUserLoginIdent) {
        if (userLoginListe == null) {
            return null;
        }
        for (int i = 0; i < userLoginListe.size(); i++) {
            EclUserLogin hEclUserLogin = userLoginListe.get(i);
            if (hEclUserLogin.userLoginIdent == pUserLoginIdent) {
                return hEclUserLogin;
            }
        }
        return null;
    }

    

    /****************************** Abstimmungen/Weisungen *******************************************/
    /* Listen werden in Zusammenhang mit HV-Parametern initialisiert und sind an selber Stelle zu finden! */

    synchronized public void addOderReplaceAbstimmungen(ClGlobalVar pClGlobalVar, EclAbstimmungSet pAbstimmungSet,
            int pReloadWeisungen, int pReloadAbstimmungen, int pReloadAbstimmungenWeisungenOhneAbbruch) {
        int gef = -1;

        if (paramListe == null) {
            CaBug.drucke("001");
            return;
        }
        for (int i = 0; i < paramListe.size(); i++) {
            HVParam hHVParam = paramListe.get(i);
            if (hHVParam.mandant == pClGlobalVar.mandant && hHVParam.hvJahr == pClGlobalVar.hvJahr
                    && hHVParam.hvNummer.compareTo(pClGlobalVar.hvNummer) == 0
                    && hHVParam.datenbereich.compareTo(pClGlobalVar.datenbereich) == 0) {
                Integer hIntW = Integer.valueOf(pReloadWeisungen);
                CaBug.druckeLog("pReloadWeisungen=" + pReloadWeisungen, logDrucken, 10);
                Integer hIntA = Integer.valueOf(pReloadAbstimmungen);
                CaBug.druckeLog("pReloadAbstimmungen=" + pReloadAbstimmungen, logDrucken, 10);
                Integer hIntOH = Integer.valueOf(pReloadAbstimmungenWeisungenOhneAbbruch);
                CaBug.druckeLog("pReloadAbstimmungenWeisungenOhneAbbruch=" + pReloadAbstimmungenWeisungenOhneAbbruch,
                        logDrucken, 10);
                weisungenVersion.set(i, hIntW);
                abstimmungenVersion.set(i, hIntA);
                abstimmungenWeisungenOhneAbbruchVersion.set(i, hIntOH);
                abstimmungSet.set(i, pAbstimmungSet);

                gef = i;
            }
        }
        if (gef == -1) {
            CaBug.drucke("002");
        }
    }


    /** return=Versiosnummer Weisungen => ok, =-1 => nicht gefunden */
    synchronized public EhGetAbstimmungen getAbstimmungen(int pMandant, int pHVJahr, String pHVNummer, String pDatenbereich) {
        EhGetAbstimmungen ehGetAbstimmungen=new EhGetAbstimmungen();
        if (paramListe == null) {
            ehGetAbstimmungen.tempAbstimmungSet = null;
            ehGetAbstimmungen.tempWeisungenVersion= -1;
            return ehGetAbstimmungen;
        }
        for (int i = 0; i < paramListe.size(); i++) {
            HVParam hHVParam = paramListe.get(i);
            if (hHVParam.mandant == pMandant && hHVParam.hvJahr == pHVJahr
                    && hHVParam.hvNummer.compareTo(pHVNummer) == 0
                    && hHVParam.datenbereich.compareTo(pDatenbereich) == 0) {
                ehGetAbstimmungen.tempAbstimmungSet = abstimmungSet.get(i);
                ehGetAbstimmungen.tempWeisungenVersion = weisungenVersion.get(i);
                ehGetAbstimmungen.tempAbstimmungenVersion = abstimmungenVersion.get(i);
                ehGetAbstimmungen.tempAbstimmungenWeisungenOhneAbbruchVersion = abstimmungenWeisungenOhneAbbruchVersion.get(i);
                return ehGetAbstimmungen;
            }
        }
        ehGetAbstimmungen.tempWeisungenVersion=-1;
        return ehGetAbstimmungen;
    }


    /*
    synchronized public ArrayList<ParamLogo> getParamLogoListe() {
        return paramLogoListe;
    }

    synchronized public void setParamLogoListe(ArrayList<ParamLogo> paramLogoListe) {
        this.paramLogoListe = paramLogoListe;
    }
    */

}
