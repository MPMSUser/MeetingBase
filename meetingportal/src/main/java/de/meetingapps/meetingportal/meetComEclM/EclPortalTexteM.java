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
import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComAllg.CaString;
import de.meetingapps.meetingportal.meetComBl.BlTermine;
import de.meetingapps.meetingportal.meetComEntities.EclEmittenten;
import de.meetingapps.meetingportal.meetComEntities.EclFehler;
import de.meetingapps.meetingportal.meetComEntities.EclTermine;
import de.meetingapps.meetingportal.meetComHVParam.ParamPortal;
import de.meetingapps.meetingportal.meetComKonst.KonstTermine;
import de.meetingapps.meetingportal.meetingportTController.TDialogveranstaltungenSession;
import de.meetingapps.meetingportal.meetingportTController.TLanguage;
import de.meetingapps.meetingportal.meetingportTController.TLinkSession;
import de.meetingapps.meetingportal.meetingportTController.TSession;
import de.meetingapps.meetingportal.meetingportTController.TWillenserklaerungSession;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@SessionScoped
@Named
public class EclPortalTexteM implements Serializable {
    private static final long serialVersionUID = -4023685088637960573L;

    private int logDrucken=10;
    
    @Inject
    EclParamM eclParamM;
    @Inject
    TLanguage tLanguage;

    
    private @Inject
    TWillenserklaerungSession tWillenserklaerungSession;
    private @Inject
    TSession tSession;

    private @Inject TLinkSession tLinkSession;
    private @Inject TDialogveranstaltungenSession tDialogveranstaltungenSession;

    private boolean pflegen = false;
    private boolean adaptivePflegen = true;
    private boolean appPflegen = true;
    private boolean enPflegen = true;

    private boolean nummernAnzeigen = false;

    /**Für Verwendung von "Quell-Variablen" -alte oder neue Version*/
    private boolean neueQuelle = false;
    private int texteVersion = 0;

    private EclFehler fehlerDeutschArray[] = null;
    private EclFehler fehlerEnglischArray[] = null;

    private String[] portalTexteDEArray = null;
    private String[] portalTexteENArray = null;
    private String[] portalTexteAdaptivDEArray = null;
    private String[] portalTexteAdaptivENArray = null;

    public EclPortalTexteM() {
    }

    private int[] portalNichtUeberNeueTexteArray = { 0 };

    public boolean portalTexteSindVorhanden() {
        if (portalTexteDEArray==null || portalTexteDEArray.length<10) {
            return false;
        }
        return true;
    }
    
    public boolean portalNichtUeberNeueTexte() {
        int hMandant = eclParamM.getClGlobalVar().mandant;
        for (int i = 0; i < portalNichtUeberNeueTexteArray.length; i++) {
            if (portalNichtUeberNeueTexteArray[i] == hMandant) {
                return true;
            }
        }

        return false;
    }

    //	private int textzaehler=0;

    public boolean pruefeTextVorhanden(String ptextNr) {
        //		textzaehler+=1;
        //		System.out.println("textzaehler="+textzaehler+" pText=+"+ptextNr);
        return pruefeITextVorhanden(Integer.parseInt(ptextNr));
    }
    
    public boolean pruefeITextVorhanden(int iTextNr) {
        int sprache = tLanguage.getLang();
        String ergebnisText = "";
        if (portalTexteDEArray==null || portalTexteDEArray.length<10){
            return false;
        }
        else{
            int laenge=portalTexteDEArray.length;
            if (iTextNr>=laenge) {
                CaBug.drucke("Mandant "+eclParamM.getMandantString()+" Portaltext "+Integer.toString(iTextNr)+" nicht vorhanden");
                return false;
            }
            /*TODO VidKonf  Fehler-Protokoll bei portaltexten deaktiviert*/
//            CaBug.drucke("EclPortalTexteM.pruefeTextVorhanden 001 sprache=" + Integer.toString(sprache) + " iTextNr="
//                    + Integer.toString(iTextNr));
//            CaBug.drucke("Mandant=" + eclParamM.getClGlobalVar().mandant + " HVJahr="
//                    + eclParamM.getClGlobalVar().hvJahr + " HVNummer=" + eclParamM.getClGlobalVar().hvNummer
//                    + " Datenbankbereich=" + eclParamM.getClGlobalVar().datenbereich);
//            return false;
        }
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

    /**Texte für normales Portal*/
    public String holeText(String ptextNr) {
        return holeTextNormalOderMobile(Integer.parseInt(ptextNr), 1);
    }

    public String holeIText(int pTextNr) {
        return holeTextNormalOderMobile(pTextNr, 1); 
    }
    
    public String holeITextOhneCRLF(int pTextNr) {
        return CaString.entferneCRLF(holeTextNormalOderMobile(pTextNr, 1)); 
    }

    /**Texte für mobiles Design*/
    public String holeTextM(String ptextNr) {
        return holeTextNormalOderMobile(Integer.parseInt(ptextNr), 2);
    }

    /**1=Noraml, 2=Mobil*/
    private String holeTextNormalOderMobile(int iTextNr, int pNr) {
        int sprache = tLanguage.getLang();
        if (portalTexteDEArray==null || portalTexteDEArray.length<10){
            return "";
        }
        else{
        	int laenge=portalTexteDEArray.length;
        	if (iTextNr>=laenge) {
        	    CaBug.drucke("Mandant "+eclParamM.getMandantString()+" Portaltext "+Integer.toString(iTextNr)+" nicht vorhanden");
        	    return "";
        	}
        }
//        	System.out.println("portalTexteDEArray Länge="+laenge);
        //			if (laenge>20){laenge=20;}
        //			for (int i=0;i<laenge;i++){
        //				System.out.println(i+"="+portalTexteDEArray[i]);
        //			}
        //		}
        //		
        //		System.out.println("ErgebnisText Nr ="+iTextNr);
        String ergebnisText = "";

        String quelltext = "";
        if (pNr == 1) {
            if (sprache == 1) {
                if (portalTexteDEArray != null) {
                    quelltext = portalTexteDEArray[iTextNr];
                } else {
                    quelltext = "";
                    /*AAAAA Portaltexte Fehlertext bei Portaltexten deaktiviert*/
//                    CaBug.drucke("EclPortalTexteM.holeTextNormalOderMobile 001 sprache=" + Integer.toString(sprache)
//                            + " iTextNr=" + Integer.toString(iTextNr));
//                    CaBug.drucke("Mandant=" + eclParamM.getClGlobalVar().mandant + " HVJahr="
//                            + eclParamM.getClGlobalVar().hvJahr + " HVNummer=" + eclParamM.getClGlobalVar().hvNummer
//                            + " Datenbankbereich=" + eclParamM.getClGlobalVar().datenbereich);

                }
            } else {
                quelltext = portalTexteENArray[iTextNr];
            }
        } else {
            if (sprache == 1) {
                quelltext = portalTexteAdaptivDEArray[iTextNr];
            } else {
                quelltext = portalTexteAdaptivENArray[iTextNr];
            }
        }

        ergebnisText = ersetzeAlles(quelltext);
        //		System.out.println("ErgebnisText = "+ergebnisText);
        if (nummernAnzeigen) {
            ergebnisText = "(" + Integer.toString(iTextNr) + ") " + ergebnisText;
        }
        return ergebnisText;
    }

    public String getFehlertext(int pFehlerNr) {

        String fehlerText = "";
        String zText = CaFehler.getFehlertext(pFehlerNr, 0);
        int laenge;

        if (tSession.testModusNummernAnzeigenAktiv()) {
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

    /**************Ab hier Funktionen zum Ersetzen der Variablen*******************************************/

    /*++++++++++++++++++identische Funktionen für Java und App++++++++++++++++++++++++++++++++++++++*/
    private String[] variablenNamen = { "HVDatum", "Gesellschaft", "HVOrt", "TextVeröffentlichungDatum",
            "TextEinberufungDatum", "TextEndeTOErweiterungDatum", /*5*/
            "TextEndeGegenanträgeDatum", "TextRecordDateDatum", "TextLetzterAnmeldetagDatum", "TextHVDatum", /*9*/
            "TextPhase1Datum", "TextPhase2Datum", "TextPhase3Datum", "TextPhase4Datum", "TextPhase5Datum", /*14*/
            "TextPhase6Datum", "TextPhase7Datum", "TextPhase8Datum", "TextPhase9Datum", "TextPhase10Datum", /*19*/
            "TextPhase11Datum", "TextPhase12Datum", "TextPhase13Datum", "TextPhase14Datum", "TextPhase15Datum", /*24*/
            "TextPhase16Datum", "TextPhase17Datum", "TextPhase18Datum", "TextPhase19Datum", "TextPhase20Datum", /*29*/
            "MailHotlineLink", "MailHotlineText", "Stimmrechtsvertreter", /*32*/
            "Link", "MindestLaengePasswort", /*34*/
            "MeldungAktien", "AnredeKomplett", "LinkNurCode", "MailEK1", "MailEK2", "EkNummer", /*40*/
            "AnzDialog"/*41*/, "LinkTagesordnung", "LinkGegenantraege", "LinkEinladungsPDF", /*44*/
            "LinkNutzungAktionaersPortal", "LinkNutzungHVPortal", "LinkDatenschutzHinweise", "LinkImpressum",
            "MailGesellschaftLink", "LinkDatenschutzHinweiseKunde", /*50*/
            "TextGattung", "TextEK", "TextEKMitArtikel", "MailVollmachtLink", "MailVollmachtText", /*55*/
            "TextGattung1", "TextGattung2", "TextGattung3", "TextGattung4", "TextGattung5", /*60*/
            "NL", "B", "/B", "I", "/I", "U", "/U", "H1", "/H1", "H2", "/H2", "H3", "/H3", "H4", "/H4" };
    private String[] variablenInhalt = { "", "", "", "", "", "", /*5*/
            "", "", "", "", /*9*/
            "", "", "", "", "", /*14*/
            "", "", "", "", "", /*19*/
            "", "", "", "", "", /*24*/
            "", "", "", "", "", /*29*/
            "", "", "", /*32*/
            "", "", /*34*/
            "", "", "", "", "", "", /*40*/
            "", /*41*/ "", "", "", /*44*/
            "", "", "", "", "", "", /*50*/
            "", "", "", "", "", /*55*/
            "", "", "", "", "", /*60*/
            "<br />", "<b>", "</b>", "<i>", "</i>", "<u>", "</u>", "<h1>", "</h1>", "<h2>", "</h2>", "<h3>", "</h3>",
            "<h4>", "</h4>" };

    /**++++++++++++Lokale Werte (L_)+++++++++++++++++*/
    /**Beim Versenden von E-Mails mit Gastkarte aus uLogin - Mailtext*/
    private String[] lokaleVariablenNamen=null;
    private String[] lokaleVariablenInhalt=null;
    
    /*Variablen aus App:
     * TODO _App
     *      pErgebnisText = this.ersetze(pErgebnisText, "<<<NL>>>", "\u000A");
            pErgebnisText = this.ersetze(pErgebnisText, "<<<MindestLaengePasswort>>>", App.paramPortal.passwortMindestLaenge.ToString());
            if (App.eclKIAVM != null)
            {
                pErgebnisText = this.ersetze(pErgebnisText, "<<<KIAVMKurztext>>>", App.eclKIAVM.kurzText);
            }
            if (App.eclZugeordneteMeldungM != null)
            {
                pErgebnisText = this.ersetze(pErgebnisText, "<<<MeldungAktien>>>", App.eclZugeordneteMeldungM.stueckAktien.ToString());
            }
    
     * 
     */

    private int sprache = 0;
    private BlTermine blTermine = null;

    private String ersetzeAlleVariablen(String pErgebnisText) {

        /*Initialisierung*/
        sprache = liefereAktuelleSprache();
        blTermine = new BlTermine(liefereTerminlisteTechnisch());

        /**Kommentare <** **> ersetzen*/
        int pos=CaString.indexOf(pErgebnisText, "<**");
        while (pos != -1) {
            int posEnde = CaString.indexOf(pErgebnisText, "**>");
            if (posEnde == -1) {
                pErgebnisText = "Fehler im Text: <** ohne korrespondierende **> ";
                return pErgebnisText;
            }
            pErgebnisText = pErgebnisText.substring(0, pos)+pErgebnisText.substring(posEnde+3);
            pos = CaString.indexOf(pErgebnisText, "<**");
        }
        
        
        
        /**Restliche Variablen ersetzen*/
        pos = CaString.indexOf(pErgebnisText, "<<<");
        while (pos != -1) {
            int posEnde = CaString.indexOf(pErgebnisText, ">>>");
            if (posEnde == -1) {
                pErgebnisText = "Fehler im Text: < < < ohne korrespondierende > > > ";
                return pErgebnisText;
            }
            String variablenname = CaString.substring(pErgebnisText, pos + 3, posEnde);
            pErgebnisText = ersetzeEinzelneVariable(pErgebnisText, variablenname);
            pos = CaString.indexOf(pErgebnisText, "<<<");
        }

        return pErgebnisText;

    }

    String ersetzeEinzelneVariable(String pErgebnisText, String pVariablenname) {
        int variablenNummer = -1;
        /*Variablennummer bestimmen*/
        for (int i = 0; i < liefereAnzVariablen(); i++) {
            if (CaString.compareTo(pVariablenname, variablenNamen[i]) == 0) {
                variablenNummer = i;
            }
        }
        if (variablenNummer==-1 && lokaleVariablenNamen!=null) {
            for (int i=0;i<lokaleVariablenNamen.length;i++) {
                if (CaString.compareTo(pVariablenname, lokaleVariablenNamen[i]) == 0) {
                    variablenNummer = i+1000;
                }
            }
        }
        if (variablenNummer == -1) {
            pErgebnisText = "Fehler im Text: Variable " + pVariablenname + " nicht gefunden!";
        } else {
            /*Variablenwert bestimmen*/
            String variablenWert = liefereVariablenWert(variablenNummer);
            /*Variable ersetzen*/
            pErgebnisText = ersetzeVariable(pErgebnisText, pVariablenname, variablenWert);
        }
        return pErgebnisText;
    }

    /**Liefert Wert für eine Variable (Nummer=Offset im Array)*/
    String liefereVariablenWert(int pVariablenNummer) {
        if (pVariablenNummer>=1000) {
            return lokaleVariablenInhalt[pVariablenNummer-1000];
        }
        
        switch (pVariablenNummer) {
        case 0: {
            String lHVDatum = liefereEclEmittent().hvDatum;
            int lHVDatumFormat = liefereParamPortal().datumsformatDE;

            switch (lHVDatumFormat) {
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
            if (liefereEclEmittent().bezeichnungsArtPortal == 1) {
                variablenInhalt[1] = liefereEclEmittent().bezeichnungKurz;
            } else {
                variablenInhalt[1] = liefereEclEmittent().bezeichnungLang;
            }
            break;
        }
        case 2: {
            variablenInhalt[2] = liefereEclEmittent().hvOrt;
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
            variablenInhalt[pVariablenNummer] = blTermine.holePortalText(sprache, 101 + pVariablenNummer - 10);
            break;
        }
        case 49: {
            variablenInhalt[49] = liefereParamPortal().emailAdresseLink;
            break;
        }
        case 30: {
            variablenInhalt[30] = liefereParamPortal().emailAdresseLink;
            break;
        }
        case 31: {
            variablenInhalt[31] = liefereParamPortal().emailAdresseText;
            break;
        }
        case 32: {
            if (sprache == 1) {
                variablenInhalt[32] = liefereParamPortal().stimmrechtsvertreterNameDE;
            } else {
                variablenInhalt[32] = liefereParamPortal().stimmrechtsvertreterNameEN;
            }
            break;
        }
        case 33: {
            variablenInhalt[33] = liefereLinkPasswortZuruecksetzenEmail();
            break;
        }
        case 34: {
            variablenInhalt[34] = CaString.integerToString(liefereParamPortal().passwortMindestLaenge);
            break;
        }
        case 35: {
            if (sprache == 1) {
                variablenInhalt[35] = "Deprecated"; //CaString.toStringDE(liefereZugeordneteMeldung().getStueckAktien());
            } else {
                variablenInhalt[35] = "Deprecated"; //CaString.toStringEN(liefereZugeordneteMeldung().getStueckAktien());
            }
            break;
        }
        case 36: {
            if (sprache == 1) {
                variablenInhalt[36] = "Deprecated"; //(liefereLoginDaten().getKompletteAnredeDE());
            } else {
                variablenInhalt[36] = "Deprecated";//liefereLoginDaten().getKompletteAnredeEN();
            }
            break;
        }
        case 37: {
            variablenInhalt[37] = liefereLinkNurCodePasswortZuruecksetzenEmail();
            break;
        }
        case 38: {
            variablenInhalt[38] = liefereMailEK1();
            break;
        }
        case 39: {
            variablenInhalt[39] = liefereMailEK2();
            break;
        }
        case 40: {
            variablenInhalt[40] = liefereEkNummer();
            break;
        }
        case 41: {
            variablenInhalt[41] = tDialogveranstaltungenSession.getAnzPersonen();
            break;
        }
        case 42: {
            variablenInhalt[42] = liefereParamPortal().linkTagesordnung;
            break;
        }
        case 43: {
            variablenInhalt[43] = liefereParamPortal().linkGegenantraege;
            break;
        }
        case 44: {
            variablenInhalt[44] = liefereParamPortal().linkEinladungsPDF;
            break;
        }
        case 45: {
            variablenInhalt[45] = liefereParamPortal().linkNutzungsbedingungenAktionaersPortal;
            break;
        }
        case 46: {
            variablenInhalt[46] = liefereParamPortal().linkNutzungsbedingungenHVPortal;
            break;
        }
        case 47: {
            variablenInhalt[47] = liefereParamPortal().linkDatenschutzhinweise;
            break;
        }
        case 48: {
            variablenInhalt[48] = liefereParamPortal().linkImpressum;
            break;
        }
        /*49 siehe 30*/
        case 50: {
            variablenInhalt[50] = liefereParamPortal().linkDatenschutzhinweiseKunde;
            break;
        }
        case 51: {
            variablenInhalt[51] = "Gattung Deprecated";
            break;
        }
        case 52: {
            if (sprache == 1) {
                variablenInhalt[52] = liefereParamPortal().ekText;
            } else {
                variablenInhalt[52] = liefereParamPortal().ekTextEN;
            }
            break;
        }
        case 53: {
            if (sprache == 1) {
                variablenInhalt[53] = liefereParamPortal().ekTextMitArtikel;
            } else {
                variablenInhalt[53] = liefereParamPortal().ekTextENMitArtikel;
            }
            break;
        }
        case 54: {
            variablenInhalt[54] = liefereParamPortal().vollmachtEmailAdresseLink;
            break;
        }
        case 55: {
            variablenInhalt[55] = liefereParamPortal().vollmachtEmailAdresseText;
            break;
        }
        case 56: {
            variablenInhalt[56] = liefereGattungsText(1);
            break;
        }
        case 57: {
            variablenInhalt[57] = liefereGattungsText(2);
            break;
        }
        case 58: {
            variablenInhalt[58] = liefereGattungsText(3);
            break;
        }
        case 59: {
            variablenInhalt[59] = liefereGattungsText(4);
            break;
        }
        case 60: {
            variablenInhalt[60] = liefereGattungsText(5);
            break;
        }
        }

        return variablenInhalt[pVariablenNummer];
    }

    /*Ersetzt <<<pVariable>>> durch den Wert pWert*/
    private String ersetzeVariable(String pErgebnisText, String pVariable, String pWert) {
        String lErgebnisText = pErgebnisText;
        int pos = CaString.indexOf(lErgebnisText, "<<<" + pVariable + ">>>");
        while (pos != -1) {
            lErgebnisText = CaString.substring(lErgebnisText, 0, pos) + pWert
                    + CaString.substring(lErgebnisText, pos + CaString.length(pVariable) + 6);
            pos = CaString.indexOf(lErgebnisText, "<<<" + pVariable + ">>>");
        }
        return lErgebnisText;
    }

    /*++++++++++++++++++++unterschiedliche Funktionen für Java und App+++++++++++++++++++++++++++++++*/
    /* Für Java*/
    private int liefereAktuelleSprache() {
        return tLanguage.getLang();
    }

    private EclTermine[] liefereTerminlisteTechnisch() {
        return eclParamM.getTerminlisteTechnisch();
    }

    ParamPortal liefereParamPortal() {
        return eclParamM.getParam().paramPortal;
    }

    
    /**pGattungId=1 bis 5*/
    private String liefereGattungsText(int pGattungId) {
        if (liefereAktuelleSprache()==1) {
            return eclParamM.getParam().paramBasis.getGattungBezeichnung(pGattungId);
        }
        else {
            return eclParamM.getParam().paramBasis.getGattungBezeichnungEN(pGattungId);
        }
    }

    /**Liefert 1 bis 5; oder 0, wenn mehrere Gattungen ausgewählt*/
    int liefereGattungNr() {
        return tWillenserklaerungSession.getGattungEinzigeVorhanden();
    }

    EclEmittenten liefereEclEmittent() {
        return (eclParamM.getEclEmittent());
    }

//    EclZugeordneteMeldungM liefereZugeordneteMeldung() {
//        return (eclZugeordneteMeldungM);
//    }

    String liefereLinkPasswortZuruecksetzenEmail() {
        //In App nicht erforderlich!
        return (tLinkSession.getEinsprungsLinkFuerEmail());
    }

    String liefereLinkNurCodePasswortZuruecksetzenEmail() {
        //In App nicht erforderlich!
        return (tLinkSession.getEinsprungsLinkNurCode());
    }

    int liefereAnzVariablen() {
        return (variablenNamen.length);
    }

//    private EclLoginDatenM liefereLoginDaten() {
//        return (eclLoginDatenM);
//    }

    String liefereMailEK1() {
        return tWillenserklaerungSession.getEintrittskarteEmail();
    }

    String liefereMailEK2() {
        return tWillenserklaerungSession.getEintrittskarteEmail2();
    }

    String liefereEkNummer() {
//        return eclWillenserklaerungStatusM.getZutrittsIdent();
        CaBug.druckeLog("", logDrucken, 10);
      return tWillenserklaerungSession.getZutrittsIdent();
    }

    /* */

    /*Für App
     * 
        private int liefereAktuelleSprache()
        {
            return App.aktuelleSprache;
        }
    
        private EclTermine[] liefereTerminlisteTechnisch()
        {
            return App.terminlisteTechnisch;
        }
    
        ParamPortal liefereParamPortal()
        {
            return App.paramPortal;
        }
    
        EclEmittenten liefereEclEmittent()
        {
            return (App.aktuellerEmittent);
        }
    
        EclZugeordneteMeldungM liefereZugeordneteMeldung()
        {
            return (App.eclZugeordneteMeldungM);
        }
    
    
        String liefereLinkPasswortZuruecksetzenEmail()
        {
            //In App nicht erforderlich!
            return ("");
        }
    
       String liefereLinkNurCodePasswortZuruecksetzenEmail()
        {
            //In App nicht erforderlich!
            return ("");
        }
    
        int liefereAnzVariablen()
        {
            return (variablenNamen.Length);
        }
        
    
        EclTeilnehmerLoginM liefereLoginDaten(){
    		return (App.eclTeilnehmerLoginM);
    	}
    
    String liefereMailEK1(){
    	return App.aDlgVariablen.eintrittskarteEmail;
    }
    
    String liefereMailEK2(){
    	return App.aDlgVariablen.eintrittskarteEmail2;
    }
    
    String liefereEkNummer(){
    	return App.eclWillenserklaerungStatusM.zutrittsIdent;
    }
    
     * 
     */

    private int uebriggebliebenesBefehlEnde = 0;

    /**pKettenart
     * 1 = $$$ - Befehlsketten
     * 2 = [[[ ]]] - Bedingungen
     * 3 = ### ### Phasen
      */
    private String ersetzeKette(String pErgebnisText, int pKettenart) {
        String lErgebnisText = pErgebnisText;
        String beginnKZ = "", endeKZ = "";
        /*Java*/
        boolean bedingung = false;
        /**/
        /*App
        Boolean bedingung=false;
        */

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
        int pos = CaString.indexOf(lErgebnisText, beginnKZ);
        while (pos != -1) {
            /*Befehlanfang suchen und in befehl ablegen*/
            String ergebnisNeu = CaString.substring(lErgebnisText, 0, pos);
            String restString = CaString.substring(lErgebnisText, pos + 3);
            int pos1 = CaString.indexOf(restString, endeKZ);
            if (pos1 == -1) {
                lErgebnisText = "Fehler: Ende-Kennzeichen für Befehl nicht gefunden";
                return lErgebnisText;
            }
            String befehl = CaString.substring(restString, 0, pos1);

            /*befehlNr festlegen, sowie befehlEnde besetzen. Negative Nummer = übriggebliebener Ende-Befehl*/
            String befehlEnde = "";
            int befehlNr = -99999;
            if (CaString.compareTo(befehl, "LINK") == 0) {
                befehlNr = 1;
            }
            if (CaString.compareTo(befehl, "APP_VORHANDEN") == 0) {
                befehlNr = 101;
                befehlEnde = "/APP_VORHANDEN";
            }
            if (CaString.compareTo(befehl, "BRIEFWAHL_VORHANDEN") == 0) {
                befehlNr = 102;
                befehlEnde = "/BRIEFWAHL_VORHANDEN";
            }
            if (CaString.compareTo(befehl, "EMAILREGISTRIERUNG_VORHANDEN") == 0) {
                befehlNr = 103;
                befehlEnde = "/EMAILREGISTRIERUNG_VORHANDEN";
            }
            if (CaString.compareTo(befehl, "ADRESSÄNDERUNG_VORHANDEN") == 0) {
                befehlNr = 104;
                befehlEnde = "/ADRESSÄNDERUNG_VORHANDEN";
            }
            if (CaString.compareTo(befehl, "SRV0") == 0) {
                befehlNr = 105;
                befehlEnde = "/SRV0";
            }
            if (CaString.compareTo(befehl, "SRV1") == 0) {
                befehlNr = 106;
                befehlEnde = "/SRV1";
            }
            if (CaString.compareTo(befehl, "SRV2") == 0) {
                befehlNr = 107;
                befehlEnde = "/SRV2";
            }
            if (CaString.compareTo(befehl, "SRV_VORHANDEN") == 0) {
                befehlNr = 108;
                befehlEnde = "/SRV_VORHANDEN";
            }
            if (CaString.compareTo(befehl, "KIAV_VORHANDEN") == 0) {
                befehlNr = 109;
                befehlEnde = "/KIAV_VORHANDEN";
            }
            if (CaString.compareTo(befehl, "VDRITTE_VORHANDEN") == 0) {
                befehlNr = 110;
                befehlEnde = "/VDRITTE_VORHANDEN";
            }
            if (CaString.compareTo(befehl, "STREAMTEST_VORHANDEN") == 0) {
                befehlNr = 111;
                befehlEnde = "/STREAMTEST_VORHANDEN";
            }

            if (CaString.compareTo(befehl, "GATTUNG1") == 0) {
                befehlNr = 112;
                befehlEnde = "/GATTUNG1";
            }
            if (CaString.compareTo(befehl, "GATTUNG2") == 0) {
                befehlNr = 113;
                befehlEnde = "/GATTUNG2";
            }
            if (CaString.compareTo(befehl, "GATTUNG3") == 0) {
                befehlNr = 114;
                befehlEnde = "/GATTUNG3";
            }
            if (CaString.compareTo(befehl, "GATTUNG4") == 0) {
                befehlNr = 115;
                befehlEnde = "/GATTUNG4";
            }
            if (CaString.compareTo(befehl, "GATTUNG5") == 0) {
                befehlNr = 116;
                befehlEnde = "/GATTUNG5";
            }
            if (CaString.compareTo(befehl, "GATTUNG0") == 0) {
                befehlNr = 117;
                befehlEnde = "/GATTUNG0";
            }

            if (CaString.length(befehl) > 6 && CaString.compareTo(CaString.substring(befehl, 0, 6), "PHASE=") == 0) {
                befehlNr = 201;
                befehlEnde = "/PHASE";
            }

            //LINK darf nicht übrigbleiben!
            if (CaString.compareTo(befehl, "/APP_VORHANDEN") == 0) {
                befehlNr = -101;
            }
            if (CaString.compareTo(befehl, "/BRIEFWAHL_VORHANDEN") == 0) {
                befehlNr = -102;
            }
            if (CaString.compareTo(befehl, "/EMAILREGISTRIERUNG_VORHANDEN") == 0) {
                befehlNr = -103;
            }
            if (CaString.compareTo(befehl, "/ADRESSÄNDERUNG_VORHANDEN") == 0) {
                befehlNr = -104;
            }
            if (CaString.compareTo(befehl, "/SRV0") == 0) {
                befehlNr = -105;
            }
            if (CaString.compareTo(befehl, "/SRV1") == 0) {
                befehlNr = -106;
            }
            if (CaString.compareTo(befehl, "/SRV2") == 0) {
                befehlNr = -107;
            }
            if (CaString.compareTo(befehl, "/SRV_VORHANDEN") == 0) {
                befehlNr = -108;
            }
            if (CaString.compareTo(befehl, "/KIAV_VORHANDEN") == 0) {
                befehlNr = -109;
            }
            if (CaString.compareTo(befehl, "/VDRITTE_VORHANDEN") == 0) {
                befehlNr = -110;
            }
            if (CaString.compareTo(befehl, "/STREAMTEST_VORHANDEN") == 0) {
                befehlNr = -111;
            }

            if (CaString.compareTo(befehl, "/GATTUNG1") == 0) {
                befehlNr = -112;
            }
            if (CaString.compareTo(befehl, "/GATTUNG2") == 0) {
                befehlNr = -113;
            }
            if (CaString.compareTo(befehl, "/GATTUNG3") == 0) {
                befehlNr = -114;
            }
            if (CaString.compareTo(befehl, "/GATTUNG4") == 0) {
                befehlNr = -115;
            }
            if (CaString.compareTo(befehl, "/GATTUNG5") == 0) {
                befehlNr = -116;
            }
            if (CaString.compareTo(befehl, "/GATTUNG0") == 0) {
                befehlNr = -117;
            }

            if (CaString.compareTo(befehl, "/PHASE=") == 0) {
                befehlNr = -201;
            }

            if (befehlNr == -99999) {
                lErgebnisText = "Fehler: unbekannter Befehl " + befehl;
                return lErgebnisText;
            }
            if (befehlNr < 0) {
                /*es handelt sich um einen übriggebliebenen Ende-Befehl*/
                /*Falls der Endebefehl zu einem "als nicht auszuführenden Teil" gemerkt wurde, dann alles bisher ignorieren,
                 * und übriggebliebenen Endebefehl resetten.
                 * Ansonsten: Endebefehl einfach ignorieren, und weitermachen
                 */
                if (uebriggebliebenesBefehlEnde == 0 || uebriggebliebenesBefehlEnde != befehlNr * (-1)) {
                    /*Nur Befehlsende Ignorieren*/
                    lErgebnisText = ergebnisNeu + CaString.substring(restString, pos1 + 3);
                } else {
                    /*Alles bis einschließlich Befehlsende ignorieren*/
                    lErgebnisText = CaString.substring(restString, pos1 + 3);
                    uebriggebliebenesBefehlEnde = 0;
                }

            } else { /*Normalen Befehl gefunden*/
                switch (befehlNr) {
                case 1: /*LINK LINKTEXT /LINK */
                {
                    restString = CaString.substring(restString, pos1 + 3);
                    int pos2 = CaString.indexOf(restString, "$$$LINKTEXT$$$");
                    if (pos2 == -1) {
                        lErgebnisText = "Fehler: Link-Konstrukt fehlerhaft";
                        return lErgebnisText;
                    }
                    String komponentenLink = CaString.substring(restString, 0, pos2);
                    restString = CaString.substring(restString, pos2 + 14);

                    int pos3 = CaString.indexOf(restString, "$$$/LINK$$$");
                    if (pos3 == -1) {
                        lErgebnisText = "Fehler: Link-Konstrukt fehlerhaft";
                        return lErgebnisText;
                    }
                    String komponentenText = CaString.substring(restString, 0, pos3);
                    restString = CaString.substring(restString, pos3 + 11);

                    ergebnisNeu = ergebnisNeu + "<a href=\"" + komponentenLink + "\"target=\"_blank\">"
                            + komponentenText + "</a>" + restString;
                    lErgebnisText = ergebnisNeu;
                    break;
                } /*LINK Ende*/
                case 101:/*Bedingungen*/
                case 102:
                case 103:
                case 104:
                case 105:
                case 106:
                case 107:
                case 108:
                case 109:
                case 110:
                case 111:
                case 112:
                case 113:
                case 114:
                case 115:
                case 116: 
                case 117: {
                    bedingung = false;

                    switch (befehlNr) {
                    case 101: {
                        if ((liefereEclEmittent().appVorhanden & 1) == 1) {
                            bedingung = true;
                        }
                        break;
                    }
                    case 102: {
                        if (liefereParamPortal().briefwahlAngeboten == 1) {
                            bedingung = true;
                        }
                        break;
                    }
                    case 103: {
                        if (liefereParamPortal().registrierungFuerEmailVersandMoeglich == 1) {
                            bedingung = true;
                        }
                        break;
                    }
                    case 104: {
                        if (liefereParamPortal().adressaenderungMoeglich == 1) {
                            bedingung = true;
                        }
                        break;
                    }
                    case 105: {
                        if (liefereParamPortal().mehrereStimmrechtsvertreter == 0) {
                            bedingung = true;
                        }
                        break;
                    }
                    case 106: {
                        if (liefereParamPortal().mehrereStimmrechtsvertreter == 1) {
                            bedingung = true;
                        }
                        break;
                    }
                    case 107: {
                        if (liefereParamPortal().mehrereStimmrechtsvertreter == 2) {
                            bedingung = true;
                        }
                        break;
                    }
                    case 108: {
                        if (liefereParamPortal().srvAngeboten == 1) {
                            bedingung = true;
                        }
                        break;
                    }
                    case 109: {
                        if (liefereParamPortal().vollmachtKIAVAngeboten == 1) {
                            bedingung = true;
                        }
                        break;
                    }
                    case 110: {
                        if (liefereParamPortal().vollmachtDritteAngeboten == 1) {
                            bedingung = true;
                        }
                        break;
                    }
                    case 111: {
                        if (liefereParamPortal().streamTestlinkWirdAngeboten == 1) {
                            bedingung = true;
                        }
                        break;
                    }
                    case 112: {
                        if (this.liefereGattungNr() == 1) {
                            bedingung = true;
                        }
                        break;
                    }
                    case 113: {
                        if (this.liefereGattungNr() == 2) {
                            bedingung = true;
                        }
                        break;
                    }
                    case 114: {
                        if (this.liefereGattungNr() == 3) {
                            bedingung = true;
                        }
                        break;
                    }
                    case 115: {
                        if (this.liefereGattungNr() == 4) {
                            bedingung = true;
                        }
                        break;
                    }
                    case 116: {
                        if (this.liefereGattungNr() == 5) {
                            bedingung = true;
                        }
                        break;
                    }
                    case 117: {
                        if (this.liefereGattungNr() == 0) {
                            bedingung = true;
                        }
                        break;
                    }
                    }

                    restString = CaString.substring(restString, pos1 + 3);
                    /*Nun das Ende zu diesem Befehl suchen.*/
                    int pos3 = CaString.indexOf(restString, beginnKZ + befehlEnde + endeKZ);

                    String komponentenText = "";
                    if (pos3 == -1) {
                        /*Kein Abschluß gefunden. Kann möglich sein, insbesondere bei App.*/
                        if (bedingung) {
                            komponentenText = restString;
                            restString = "";
                        } else {/*Restlichen String ausblenden. "Übriggebliebenes Ende" für nächsten String setzen (App)*/
                            komponentenText = "";
                            restString = "";
                            uebriggebliebenesBefehlEnde = befehlNr;
                        }
                    } else {
                        komponentenText = CaString.substring(restString, 0, pos3);
                        restString = CaString.substring(restString, pos3 + 6 + CaString.length(befehlEnde));
                    }

                    if (bedingung) {
                        ergebnisNeu = ergebnisNeu + komponentenText;
                    }
                    ergebnisNeu = ergebnisNeu + restString;
                    lErgebnisText = ergebnisNeu;

                    break;
                } /*Bedingungen Ende*/

                case 201: /*PHASE*/
                {
                    String phasenString = CaString.substring(restString, 6, pos1);
                    bedingung = false;
                    while (bedingung == false && !CaString.isEmpty(phasenString)) {
                        int gefKomma = CaString.indexOf(phasenString, ",");
                        String phaseZuUntersuchen = "";
                        if (gefKomma == -1) {
                            phaseZuUntersuchen = phasenString;
                            phasenString = "";
                        } else {
                            phaseZuUntersuchen = CaString.substring(phasenString, 0, gefKomma);
                            phasenString = CaString.substring(phasenString, gefKomma + 1);
                        }
                        if (!CaString.isNummern(phaseZuUntersuchen)) {
                            lErgebnisText = "Fehler: Phasen-Nummer unzulässig";
                            return lErgebnisText;
                        }
                        int phasenNummer = CaString.integerParseInt(phaseZuUntersuchen);
                        if (tSession.getAktuellePortalPhaseNr() == phasenNummer) {
                            bedingung = true;
                        }
                    }

                    restString = CaString.substring(restString, pos1 + 3);
                    int pos3 = CaString.indexOf(restString, beginnKZ + befehlEnde + endeKZ);

                    String komponentenText = "";
                    if (pos3 == -1) {
                        /*Kein Abschluß gefunden. Kann möglich sein, insbesondere bei App.*/
                        if (bedingung) {
                            komponentenText = restString;
                            restString = "";
                        } else {/*Restlichen String ausblenden. "Übriggebliebenes Ende" für nächsten String setzen (App)*/
                            komponentenText = "";
                            restString = "";
                            uebriggebliebenesBefehlEnde = befehlNr;
                        }
                    } else {
                        komponentenText = CaString.substring(restString, 0, pos3);
                        restString = CaString.substring(restString, pos3 + 6 + CaString.length(befehlEnde));
                    }

                    if (bedingung) {
                        ergebnisNeu = ergebnisNeu + komponentenText;
                    }
                    ergebnisNeu = ergebnisNeu + restString;
                    lErgebnisText = ergebnisNeu;

                    break;
                } /*Phase Ende*/

                }
            } /*Ende "normalen Befehl gefunden*/
            pos = CaString.indexOf(lErgebnisText, beginnKZ);
        }

        return lErgebnisText;
    }

    /**************Bis hier Funktionen zum Ersetzen der Variablen*******************************************/

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

    public boolean isNeueQuelle() {
        return neueQuelle;
    }

    public void setNeueQuelle(boolean neueQuelle) {
        this.neueQuelle = neueQuelle;
    }

    public String[] getLokaleVariablenNamen() {
        return lokaleVariablenNamen;
    }

    public void setLokaleVariablenNamen(String[] lokaleVariablenNamen) {
        this.lokaleVariablenNamen = lokaleVariablenNamen;
    }

    public String[] getLokaleVariablenInhalt() {
        return lokaleVariablenInhalt;
    }

    public void setLokaleVariablenInhalt(String[] lokaleVariablenInhalt) {
        this.lokaleVariablenInhalt = lokaleVariablenInhalt;
    }

 
}
