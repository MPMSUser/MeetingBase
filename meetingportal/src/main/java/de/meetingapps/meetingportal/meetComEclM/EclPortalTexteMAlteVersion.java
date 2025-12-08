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

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaDatumZeit;
import de.meetingapps.meetingportal.meetComAllg.CaString;
import de.meetingapps.meetingportal.meetComBl.BlTermine;
import de.meetingapps.meetingportal.meetComEntities.EclFehler;
import de.meetingapps.meetingportal.meetComEntities.EclTermine;
import de.meetingapps.meetingportal.meetComKonst.KonstTermine;
import de.meetingapps.meetingportal.meetingport.ADlgVariablen;
import de.meetingapps.meetingportal.meetingport.ALanguage;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@SessionScoped
@Named
public class EclPortalTexteMAlteVersion implements Serializable {
    private static final long serialVersionUID = -4023685088637960573L;

    @Inject
    ADlgVariablen aDlgVariablen;
    @Inject
    EclParamM eclParamM;
    @Inject
    ALanguage aLanguage;

    @Inject
    EclZugeordneteMeldungM eclZugeordneteMeldungM;

    private boolean pflegen = false;
    private boolean adaptivePflegen = true;
    private boolean appPflegen = true;
    private boolean enPflegen = true;

    private boolean nummernAnzeigen = false;

    private int texteVersion = 0;

    private EclFehler fehlerDeutschArray[] = null;
    private EclFehler fehlerEnglischArray[] = null;

    private String[] portalTexteDEArray = null;
    private String[] portalTexteENArray = null;
    private String[] portalTexteAdaptivDEArray = null;
    private String[] portalTexteAdaptivENArray = null;

    public EclPortalTexteMAlteVersion() {
    }

    private int[] portalNichtUeberNeueTexteArray = { 8, 55, 97, 114, 115 };

    public boolean portalNichtUeberNeueTexte() {
        int hMandant = eclParamM.getClGlobalVar().mandant;
        for (int i = 0; i < portalNichtUeberNeueTexteArray.length; i++) {
            if (portalNichtUeberNeueTexteArray[i] == hMandant) {
                return true;
            }
        }

        return false;
    }

    public boolean pruefeTextVorhanden(String ptextNr) {
        int sprache = aLanguage.getLang();
        int iTextNr = Integer.parseInt(ptextNr);
        String ergebnisText = "";
        if (sprache == 1) {
            ergebnisText = portalTexteDEArray[iTextNr];
        } else {
            ergebnisText = portalTexteENArray[iTextNr];
        }
        if (ergebnisText.isEmpty() && (!nummernAnzeigen)) {
            return false;
        } else {
            return true;
        }

    }

    public String holeText(String ptextNr) {
        int sprache = aLanguage.getLang();
        int iTextNr = Integer.parseInt(ptextNr);
        //		if (portalTexteDEArray==null){System.out.println("portalTexteDEArray ist null");}
        //		else{
        //			int laenge=portalTexteDEArray.length;
        //			System.out.println("portalTexteDEArray Länge="+laenge);
        //			if (laenge>20){laenge=20;}
        //			for (int i=0;i<laenge;i++){
        //				System.out.println(i+"="+portalTexteDEArray[i]);
        //			}
        //		}
        //		
        //		System.out.println("ErgebnisText Nr ="+iTextNr);
        String ergebnisText = "";
        if (sprache == 1) {
            ergebnisText = ersetzeAlles(portalTexteDEArray[iTextNr]);
        } else {
            ergebnisText = ersetzeAlles(portalTexteENArray[iTextNr]);
        }
        //		System.out.println("ErgebnisText = "+ergebnisText);
        if (nummernAnzeigen) {
            ergebnisText = "(" + ptextNr + ") " + ergebnisText;
        }
        return ergebnisText;
    }

    public String getFehlertext(int pFehlerNr) {

        String fehlerText = "";
        String zText = "";
        int laenge;

        if (aDlgVariablen.getTest().compareTo("1") == 0) {
            fehlerText = Integer.toString(pFehlerNr) + " ";
        }

        if (/*aDlgVariablen.getSprache().compareTo("DE")==0*/
        eclParamM.getClGlobalVar().sprache == 1) {
            laenge = fehlerDeutschArray.length;
            for (int i = 0; i < laenge; i++) {
                if (fehlerDeutschArray[i].ident == pFehlerNr) {
                    zText = fehlerDeutschArray[i].fehlermeldung;
                }
            }
        }
        if (/*aDlgVariablen.getSprache().compareTo("EN")==0*/
        eclParamM.getClGlobalVar().sprache == 2) {
            laenge = fehlerEnglischArray.length;
            for (int i = 0; i < laenge; i++) {
                if (fehlerEnglischArray[i].ident == pFehlerNr) {
                    zText = fehlerEnglischArray[i].fehlermeldung;
                }
            }
        }

        fehlerText = fehlerText + zText;

        return fehlerText;

    }

    private String ersetzeAlles(String pErgebnisText) {

        /**Falls für einen Text kein Englischer gepflegt wurde, dann
         * ist String[] == null!
         */
        if (pErgebnisText == null) {
            pErgebnisText = "";
        }

        pErgebnisText = ersetzeAlleVariablen(pErgebnisText);

        pErgebnisText = ersetzeKette(pErgebnisText, 1);
        pErgebnisText = ersetzeKette(pErgebnisText, 2);
        pErgebnisText = ersetzeKette(pErgebnisText, 3);

        //		System.out.println("Vor Ersetze="+pErgebnisText);
        //		pErgebnisText=ersetzeSteuerzeichen(pErgebnisText, "&", "&amp;");
        //		System.out.println("Nach4 Ersetze="+pErgebnisText);
        return pErgebnisText;

    }

    /*++++++++++++++++++++unterschiedliche Funktionen für Java und App+++++++++++++++++++++++++++++++*/
    private int liefereAktuelleSprache() {
        return aLanguage.getLang();
    }

    private EclTermine[] liefereTerminlisteTechnisch() {
        return eclParamM.getTerminlisteTechnisch();
    }

    /**************Ab hier Funktionen zum Ersetzen der Variablen*******************************************/

    /*++++++++++++++++++identische Funktionen für Java und App++++++++++++++++++++++++++++++++++++++*/
    private String[] variablenNamen = { "HVDatum", "Gesellschaft", "HVOrt", "TextVeröffentlichungDatum",
            "TextEinberufungDatum", "TextEndeTOErweiterungDatum", /*5*/
            "TextEndeGegenanträgeDatum", "TextRecordDateDatum", "TextLetzterAnmeldetagDatum", "TextHVDatum", /*9*/
            "TextPhase1Datum", "TextPhase2Datum", "TextPhase3Datum", "TextPhase4Datum", "TextPhase5Datum", /*14*/
            "TextPhase6Datum", "TextPhase7Datum", "TextPhase8Datum", "TextPhase9Datum", "TextPhase10Datum", /*19*/
            "TextPhase11Datum", "TextPhase12Datum", "TextPhase13Datum", "TextPhase14Datum", "TextPhase15Datum", /*24*/
            "TextPhase16Datum", "TextPhase17Datum", "TextPhase18Datum", "TextPhase19Datum", "TextPhase20Datum", /*29*/
            "MailGesellschaftLink", "MailGesellschaftText", "Stimmrechtsvertreter", /*32*/
            "Link", "MindestLaengePasswort", /*34*/
            "MeldungAktien", /*35*/
            "NL", "B", "/B", "I", "/I", "U", "/U", "H1", "/H1", "H2", "/H2", "H3", "/H3", "H4", "/H4" };
    private String[] variablenInhalt = { "", "", "", "", "", "", /*5*/
            "", "", "", "", /*9*/
            "", "", "", "", "", /*14*/
            "", "", "", "", "", /*19*/
            "", "", "", "", "", /*24*/
            "", "", "", "", "", /*29*/
            "", "", "", /*32*/
            "", "", /*34*/
            "", /*35*/
            "<br />", "<b>", "</b>", "<i>", "</i>", "<u>", "</u>", "<h1>", "</h1>", "<h2>", "</h2>", "<h3>", "</h3>",
            "<h4>", "</h4>" };

    private String ersetzeAlleVariablen(String pErgebnisText) {

        /*Initialisierung*/
        int sprache = liefereAktuelleSprache();
        BlTermine blTermine = new BlTermine(liefereTerminlisteTechnisch());

        for (int i = 0; i < variablenNamen.length; i++) {
            switch (i) {
            case 0: {
                String lHVDatum = eclParamM.getEclEmittent().hvDatum;
                switch (eclParamM.getParam().paramPortal.datumsformatDE) {
                case 1:
                    variablenInhalt[0] = lHVDatum;
                    break;
                case 2:
                    variablenInhalt[0] = CaDatumZeit.DatumStringFuerAnzeigeMonatAusgeschrieben(lHVDatum);
                    break;
                }

                break;
            }
            case 1: {
                if (eclParamM.getEclEmittent().bezeichnungsArtPortal == 1) {
                    variablenInhalt[1] = eclParamM.getEclEmittent().bezeichnungKurz;
                } else {
                    variablenInhalt[1] = eclParamM.getEclEmittent().bezeichnungLang;
                }
                break;
            }
            case 2: {
                variablenInhalt[2] = eclParamM.getEclEmittent().hvOrt;
                break;
            }
            case 3: {
                variablenInhalt[3] = blTermine.holePortalText(sprache, KonstTermine.veroeffentlichung);
                break;
            }
            case 4: {
                variablenInhalt[4] = blTermine.holePortalText(sprache, KonstTermine.einberufung);
                break;
            }
            case 5: {
                variablenInhalt[5] = blTermine.holePortalText(sprache, KonstTermine.endeTOErweiterung);
                break;
            }
            case 6: {
                variablenInhalt[6] = blTermine.holePortalText(sprache, KonstTermine.endeGegenantraege);
                break;
            }
            case 7: {
                variablenInhalt[7] = blTermine.holePortalText(sprache, KonstTermine.recordDate);
                break;
            }
            case 8: {
                variablenInhalt[8] = blTermine.holePortalText(sprache, KonstTermine.letzterAnmeldetag);
                break;
            }
            case 9: {
                variablenInhalt[9] = blTermine.holePortalText(sprache, KonstTermine.hvTag);
                break;
            }
            case 10: //PhasenDatum
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
            case 24:
            case 25:
            case 26:
            case 27:
            case 28:
            case 29: {
                variablenInhalt[i] = blTermine.holePortalText(sprache, 101 + i - 10);
                break;
            }
            case 30: {
                variablenInhalt[30] = eclParamM.getParam().paramPortal.emailAdresseLink;
                break;
            }
            case 31: {
                variablenInhalt[31] = eclParamM.getParam().paramPortal.emailAdresseText;
                break;
            }
            case 32: {
                if (sprache == 1) {
                    variablenInhalt[32] = eclParamM.getParam().paramPortal.stimmrechtsvertreterNameDE;
                } else {
                    variablenInhalt[32] = eclParamM.getParam().paramPortal.stimmrechtsvertreterNameEN;
                }
                break;
            }
            case 33: {
                variablenInhalt[33] = aDlgVariablen.getEinsprungsLinkFuerEmail();
                break;
            }
            case 34: {
                variablenInhalt[34] = Integer.toString(eclParamM.getParam().paramPortal.passwortMindestLaenge);
                break;
            }
            case 35: {
                if (sprache == 1) {
                    variablenInhalt[35] = CaString.toStringDE(eclZugeordneteMeldungM.getStueckAktien());
                } else {
                    variablenInhalt[35] = CaString.toStringEN(eclZugeordneteMeldungM.getStueckAktien());
                }
                break;
            }
            }

            pErgebnisText = this.ersetzeVariable(pErgebnisText, variablenNamen[i], variablenInhalt[i]);
        }

        return pErgebnisText;

    }

    /*Ersetzt <<<pVariable>>> durch den Wert pWert*/
    private String ersetzeVariable(String pErgebnisText, String pVariable, String pWert) {
        String lErgebnisText = pErgebnisText;
        int pos = lErgebnisText.indexOf("<<<" + pVariable + ">>>");
        while (pos != -1) {
            lErgebnisText = lErgebnisText.substring(0, pos) + pWert
                    + lErgebnisText.substring(pos + pVariable.length() + 6);
            pos = lErgebnisText.indexOf("<<<" + pVariable + ">>>");
        }
        return lErgebnisText;
    }

    /**************Bis hier Funktionen zum Ersetzen der Variablen*******************************************/

    /**pKettenart
     * 1 = $$$ - Befehlsketten
     * 2 = [[[ ]]] - Bedingungen
     * 3 = ### ### Phasen
      */
    private String ersetzeKette(String pErgebnisText, int pKettenart) {
        String lErgebnisText = pErgebnisText;
        String beginnKZ = "", endeKZ = "";
        switch (pKettenart) {
        case 1:
            beginnKZ = "$$$";
            endeKZ = "$$$";
            break;
        case 2:
            beginnKZ = "[[[";
            endeKZ = "]]]";
            break;
        case 3:
            beginnKZ = "###";
            endeKZ = "###";
            break;
        }
        int pos = lErgebnisText.indexOf(beginnKZ);
        while (pos != -1) {
            String ergebnisNeu = lErgebnisText.substring(0, pos);
            String restString = lErgebnisText.substring(pos + 3);
            int pos1 = restString.indexOf(endeKZ);
            if (pos1 == -1) {
                CaBug.drucke("EclPortalTexteM.ersetzeKette 001 - " + lErgebnisText);
                return lErgebnisText;
            }
            String befehl = restString.substring(0, pos1);
            String befehlEnde = "";
            int befehlNr = -1;
            if (befehl.compareTo("LINK") == 0) {
                befehlNr = 1;
            }
            if (befehl.compareTo("APP_VORHANDEN") == 0) {
                befehlNr = 101;
                befehlEnde = "/APP_VORHANDEN";
            }
            if (befehl.compareTo("BRIEFWAHL_VORHANDEN") == 0) {
                befehlNr = 102;
                befehlEnde = "/BRIEFWAHL_VORHANDEN";
            }
            if (befehl.compareTo("EMAILREGISTRIERUNG_VORHANDEN") == 0) {
                befehlNr = 103;
                befehlEnde = "/EMAILREGISTRIERUNG_VORHANDEN";
            }
            if (befehl.compareTo("ADRESSÄNDERUNG_VORHANDEN") == 0) {
                befehlNr = 104;
                befehlEnde = "/ADRESSÄNDERUNG_VORHANDEN";
            }
            if (befehl.length() > 6 && befehl.substring(0, 6).compareTo("PHASE=") == 0) {
                befehlNr = 201;
                befehlEnde = "/PHASE";
            }
            if (befehlNr == -1) {
                CaBug.drucke("EclPortalTexteM.ersetzeKette 002 - " + lErgebnisText);
                return lErgebnisText;
            }

            switch (befehlNr) {
            case 1: /*LINK LINKTEXT /LINK */
            {
                restString = restString.substring(pos1 + 3);
                int pos2 = restString.indexOf("$$$LINKTEXT$$$");
                if (pos2 == -1) {
                    CaBug.drucke("EclPortalTexteM.ersetzeKette 003 - " + lErgebnisText);
                    return lErgebnisText;
                }
                String komponentenLink = restString.substring(0, pos2);
                restString = restString.substring(pos2 + 14);

                int pos3 = restString.indexOf("$$$/LINK$$$");
                if (pos3 == -1) {
                    CaBug.drucke("EclPortalTexteM.ersetzeKette 004 - " + lErgebnisText);
                    return lErgebnisText;
                }
                String komponentenText = restString.substring(0, pos3);
                restString = restString.substring(pos3 + 11);

                ergebnisNeu = ergebnisNeu + "<a href=\"" + komponentenLink + "\"target=\"_blank\">" + komponentenText
                        + "</a>" + restString;
                lErgebnisText = ergebnisNeu;
                break;
            } /*LINK Ende*/
            case 101:/*Bedingungen*/
            case 102:
            case 103:
            case 104: {
                boolean bedingung = false;
                switch (befehlNr) {
                case 101: {
                    if (eclParamM.getEclEmittent().appVorhanden == 1) {
                        bedingung = true;
                    }
                    break;
                }
                case 102: {
                    if (eclParamM.getParam().paramPortal.briefwahlAngeboten == 1) {
                        bedingung = true;
                    }
                    break;
                }
                case 103: {
                    if (eclParamM.getParam().paramPortal.registrierungFuerEmailVersandMoeglich == 1) {
                        bedingung = true;
                    }
                    break;
                }
                case 104: {
                    if (eclParamM.getParam().paramPortal.adressaenderungMoeglich == 1) {
                        bedingung = true;
                    }
                    break;
                }
                }

                restString = restString.substring(pos1 + 3);
                int pos3 = restString.indexOf(beginnKZ + befehlEnde + endeKZ);
                if (pos3 == -1) {
                    CaBug.drucke("EclPortalTexteM.ersetzeKette 005 - " + lErgebnisText);
                    return lErgebnisText;
                }
                String komponentenText = restString.substring(0, pos3);
                restString = restString.substring(pos3 + 6 + befehlEnde.length());

                if (bedingung) {
                    ergebnisNeu = ergebnisNeu + komponentenText;
                }
                ergebnisNeu = ergebnisNeu + restString;
                lErgebnisText = ergebnisNeu;

                break;
            } /*Bedingungen Ende*/

            case 201: /*PHASE*/
            {
                String phasenString = restString.substring(6, pos1);
                boolean bedingung = false;
                while (bedingung == false && !phasenString.isEmpty()) {
                    int gefKomma = phasenString.indexOf(",");
                    String phaseZuUntersuchen = "";
                    if (gefKomma == -1) {
                        phaseZuUntersuchen = phasenString;
                        phasenString = "";
                    } else {
                        phaseZuUntersuchen = phasenString.substring(0, gefKomma);
                        phasenString = phasenString.substring(gefKomma + 1);
                    }
                    if (!CaString.isNummern(phaseZuUntersuchen)) {
                        CaBug.drucke("EclPortalTexteM.ersetzeKette 007 - " + lErgebnisText);
                        return lErgebnisText;
                    }
                    int phasenNummer = Integer.parseInt(phaseZuUntersuchen);
                    if (eclParamM.getParam().paramPortal.phasePortal == phasenNummer) {
                        bedingung = true;
                    }
                }

                restString = restString.substring(pos1 + 3);
                int pos3 = restString.indexOf(beginnKZ + befehlEnde + endeKZ);
                if (pos3 == -1) {
                    CaBug.drucke("EclPortalTexteM.ersetzeKette 006 - " + lErgebnisText);
                    return lErgebnisText;
                }
                String komponentenText = restString.substring(0, pos3);
                restString = restString.substring(pos3 + 6 + befehlEnde.length());

                if (bedingung) {
                    ergebnisNeu = ergebnisNeu + komponentenText;
                }
                ergebnisNeu = ergebnisNeu + restString;
                lErgebnisText = ergebnisNeu;

                break;
            } /*Phase Ende*/

            }

            pos = lErgebnisText.indexOf(beginnKZ);
        }

        return lErgebnisText;
    }

    /** ?=Steuerzeichen; +++zeichen+++ wird ersetzt durch +++H+++; dann zeichen durch zeichenText; 
     * dann +++H+++ durch zeichen
     * 
     *  Funktioniert nicht für zeichen=+ !!!
     */
    //	private String ersetzeSteuerzeichen(String pErgebnisText, String zeichen, String zeichenText){
    //        String lErgebnisText = pErgebnisText;
    //        
    //        String lWert="+++"+zeichen+"+++";
    //        String lZielwert="+++U+++";
    //	
    //        int pos = lErgebnisText.indexOf(lWert);
    //        while (pos != -1)
    //        {
    //           lErgebnisText = lErgebnisText.substring(0, pos) + lZielwert + lErgebnisText.substring(pos + lWert.length());
    //           pos = lErgebnisText.indexOf(lWert);
    //         }
    //
    //        
    //        lWert=zeichen;
    //        lZielwert="+++V+++";
    //	
    //        pos = lErgebnisText.indexOf(lWert);
    //        while (pos != -1)
    //        {
    //           lErgebnisText = lErgebnisText.substring(0, pos) + lZielwert + lErgebnisText.substring(pos + lWert.length());
    //           pos = lErgebnisText.indexOf(lWert);
    //         }
    //
    //        lWert="+++V+++";
    //        lZielwert=zeichenText;
    //	
    //        pos = lErgebnisText.indexOf(lWert);
    //        while (pos != -1)
    //        {
    //           lErgebnisText = lErgebnisText.substring(0, pos) + lZielwert + lErgebnisText.substring(pos + lWert.length());
    //           pos = lErgebnisText.indexOf(lWert);
    //         }
    //
    //        lWert="+++U+++";
    //        lZielwert=zeichen;
    //	
    //        pos = lErgebnisText.indexOf(lWert);
    //        while (pos != -1)
    //        {
    //           lErgebnisText = lErgebnisText.substring(0, pos) + lZielwert + lErgebnisText.substring(pos + lWert.length());
    //           pos = lErgebnisText.indexOf(lWert);
    //         }
    //
    //
    //        return lErgebnisText;
    //	}

    /*************Standard Getter und Setter**************************/

    public EclFehler[] getFehlerDeutschArray() {
        return fehlerDeutschArray;
    }

    public void setFehlerDeutschArray(EclFehler[] fehlerDeutschArray) {
        this.fehlerDeutschArray = fehlerDeutschArray;
    }

    public EclFehler[] getEnglischarray() {
        return fehlerEnglischArray;
    }

    public void setEnglischarray(EclFehler[] englischarray) {
        this.fehlerEnglischArray = englischarray;
    }

    public String[] getPortalTexteDEArray() {
        return portalTexteDEArray;
    }

    public void setPortalTexteDEArray(String[] portalTexteDEArray) {
        this.portalTexteDEArray = portalTexteDEArray;
    }

    public String[] getPortalTexteENArray() {
        return portalTexteENArray;
    }

    public void setPortalTexteENArray(String[] portalTexteENArray) {
        this.portalTexteENArray = portalTexteENArray;
    }

    public String[] getPortalTexteAdaptivDEArray() {
        return portalTexteAdaptivDEArray;
    }

    public void setPortalTexteAdaptivDEArray(String[] portalTexteAdaptivDEArray) {
        this.portalTexteAdaptivDEArray = portalTexteAdaptivDEArray;
    }

    public String[] getPortalTexteAdaptivENArray() {
        return portalTexteAdaptivENArray;
    }

    public void setPortalTexteAdaptivENArray(String[] portalTexteAdaptivENArray) {
        this.portalTexteAdaptivENArray = portalTexteAdaptivENArray;
    }

    public int getTexteVersion() {
        return texteVersion;
    }

    public void setTexteVersion(int texteVersion) {
        this.texteVersion = texteVersion;
    }

    public boolean isPflegen() {
        return pflegen;
    }

    public void setPflegen(boolean pflegen) {
        this.pflegen = pflegen;
    }

    public boolean isAdaptivePflegen() {
        return adaptivePflegen;
    }

    public void setAdaptivePflegen(boolean adaptivePflegen) {
        this.adaptivePflegen = adaptivePflegen;
    }

    public boolean isAppPflegen() {
        return appPflegen;
    }

    public void setAppPflegen(boolean appPflegen) {
        this.appPflegen = appPflegen;
    }

    public boolean isEnPflegen() {
        return enPflegen;
    }

    public void setEnPflegen(boolean enPflegen) {
        this.enPflegen = enPflegen;
    }

    public boolean isNummernAnzeigen() {
        return nummernAnzeigen;
    }

    public void setNummernAnzeigen(boolean nummernAnzeigen) {
        this.nummernAnzeigen = nummernAnzeigen;
    }

}
