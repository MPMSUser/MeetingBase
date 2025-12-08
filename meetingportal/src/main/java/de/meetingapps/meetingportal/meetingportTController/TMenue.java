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
package de.meetingapps.meetingportal.meetingportTController;

import java.util.LinkedList;
import java.util.List;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComEclM.EclDbM;
import de.meetingapps.meetingportal.meetComEclM.EclParamM;
import de.meetingapps.meetingportal.meetComEntities.EclMenueEintrag;
import de.meetingapps.meetingportal.meetComKonst.KonstPMenueFunktionscode;
import de.meetingapps.meetingportal.meetComKonst.KonstPortalUnterlagen;
import de.meetingapps.meetingportal.meetComKonst.KonstPortalView;
import de.meetingapps.meetingportal.meetingportTFunktionen.TPruefeStartNachOpen;
import de.meetingapps.meetingportal.meetingportTFunktionen.TRemoteAR;
import de.meetingapps.meetingportal.meetingportTFunktionen.TSessionVerwaltung;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@RequestScoped
@Named
public class TMenue {

    private int logDrucken=3;

    private @Inject TKontaktformular tKontaktformular;
    private @Inject PBestand pBestand;
    private @Inject PBestandhistorie pBestandhistorie;
    private @Inject PBeteiligungserhoehung pBeteiligungserhoehung;
    private @Inject PAktionaersdaten pAktionaersdaten;
    private @Inject TPublikationen tPublikationen;
    private @Inject PHVEinladungsversand pHVEinladungsversand;
    private @Inject PHVEinladungsversand pHVzurHV;
    private @Inject PEinstellungenAendern pEinstellungenAendern;
    private @Inject TNachrichten tNachrichten;
    private @Inject TNachrichtenSession tNachrichtenSession;
    private @Inject TDialogveranstaltungen tDialogveranstaltungen;
    private @Inject TVeranstaltungen tVeranstaltungen;
    private @Inject TUnterlagen tUnterlagen;

    private @Inject TPruefeStartNachOpen tPruefeStartNachOpen;
    
    private @Inject TAuswahl1 tAuswahl1;

    private @Inject EclParamM eclParamM;
    private @Inject TMenueSession tMenueSession;
    private @Inject TSession tSession;
    private @Inject EclDbM eclDbM;
    private @Inject TSessionVerwaltung tSessionVerwaltung;
    private @Inject TRemoteAR tRemoteAR;

    
    /**eclDbM im Normalfall nicht erforderlich.
     * eclDbM muß vor dem Aufruf auch geschlossen sein, da bei Deep-Link ggf. in den dortigen 
     * Initialisierungsfunktionen wieder geöffnt wird!
     * 
     * Liefert konstPortalView.P_AUSWAHL zurück, wenn kein Deep-Link ausgewählt.
     * Ansonsten View-Nummer des ausgewählten Deep-Links, dieser wird auch vorher aktiviert.*/
    public int init() {
        List <EclMenueEintrag> lMenue=eclParamM.getParam().paramPortal.menueListe;

        List <EclMenueEintrag> zielMenue=new LinkedList<EclMenueEintrag>();

        EclMenueEintrag letzterHauptmenue=null;
        EclMenueEintrag letzterSubmenue=null;
        EclMenueEintrag letzterSubsubmenue=null;

        for ( EclMenueEintrag iMenueEintrag : lMenue) {
            CaBug.druckeLog("Hauptmenüeintrag ="+iMenueEintrag.ident, logDrucken, 10);
            letzterHauptmenue=new EclMenueEintrag(iMenueEintrag);
            zielMenue.add(letzterHauptmenue);
            ergaenzeMenue(letzterHauptmenue);

            if (iMenueEintrag.submenueVorhanden()) {
                for ( EclMenueEintrag i1MenueEintrag : iMenueEintrag.submenueList) {
                    letzterSubmenue=new EclMenueEintrag(i1MenueEintrag);
                    letzterHauptmenue.submenueList.add(letzterSubmenue);
                    ergaenzeMenue(letzterSubmenue);

                    if (i1MenueEintrag.submenueVorhanden()) {
                        for ( EclMenueEintrag i2MenueEintrag : i1MenueEintrag.submenueList) {
                            letzterSubsubmenue=new EclMenueEintrag(i2MenueEintrag);
                            letzterSubmenue.submenueList.add(letzterSubsubmenue);
                            ergaenzeMenue(letzterSubsubmenue);

                        }
                    }
                }
            }
        }

        tMenueSession.setAuswahlMenue(zielMenue);

        clearMenue();
        printMenue();

        CaBug.druckeLog("tMenueSession.getDeepLinkPMenueFunktionscode()="+tMenueSession.getDeepLinkPMenueFunktionscode(), logDrucken, 10);

        if (tMenueSession.getDeepLinkPMenueFunktionscode()!=0) {
            int neuesMenue=0;
            neuesMenue=aktiviereAusgewaehltenMenuepunkt(tMenueSession.getDeepLinkPMenueFunktionscode(), tMenueSession.getDeepLinkPMenueFunktionscodeSub());
            CaBug.druckeLog("neuesMenue="+neuesMenue, logDrucken, 10);
            tMenueSession.setDeepLinkPMenueFunktionscode(0);
            tMenueSession.setDeepLinkPMenueFunktionscodeSub(0);
            
            if (neuesMenue<=0) {
                return KonstPortalView.P_AUSWAHL;
            }
            else {
                return neuesMenue;
            }
        }
        else {
            return KonstPortalView.P_AUSWAHL;
        }
    }
    
    
    private void ergaenzeMenue(EclMenueEintrag pMenueEintrag) {
        CaBug.druckeLog("textNrGespeichert="+pMenueEintrag.textNrGespeichert+" pMenueEintrag.funktionscode="+pMenueEintrag.funktionscode, logDrucken, 10);
        if (pMenueEintrag.textNrGespeichert==0) {
            pMenueEintrag.textnr=KonstPMenueFunktionscode.textNr(pMenueEintrag.funktionscode);
        }
        else {
            pMenueEintrag.textnr=pMenueEintrag.textNrGespeichert;
        }
        pMenueEintrag.iconCode=KonstPMenueFunktionscode.iconNr(pMenueEintrag.funktionscode);
        switch (pMenueEintrag.funktionscode) {
        case KonstPMenueFunktionscode.POSTEINGANG:
            int anzahlUngeleseneNachrichten=tNachrichtenSession.getAnzahlUngeleseneNachrichten();
            if (anzahlUngeleseneNachrichten>0) {
                pMenueEintrag.textErgaenzung=" ("+Integer.toString(anzahlUngeleseneNachrichten)+")";
            }
            else {
                pMenueEintrag.textErgaenzung="";
            }
            break;
        }
    }
    
    /**Setzt im Menü alle Einträge auf "nicht gewählt"*/
    public void clearMenue() {
        /*Kopieren ist erforderlich, da sonst - über den gemeinsamen
         * Parameter-Puffer - z.B. ausgewählt etc. für alle User
         * gleich gesetzt werden würde ...
         */
        List <EclMenueEintrag> lMenue=tMenueSession.getAuswahlMenue();
        for ( EclMenueEintrag iMenueEintrag : lMenue) {
            iMenueEintrag.aktuellAusgewaehlt=false;  
            
            if (iMenueEintrag.submenueVorhanden()) {
                for ( EclMenueEintrag i1MenueEintrag : iMenueEintrag.submenueList) {
                    i1MenueEintrag.aktuellAusgewaehlt=false;
                    
                    if (i1MenueEintrag.submenueVorhanden()) {
                        for ( EclMenueEintrag i2MenueEintrag : i1MenueEintrag.submenueList) {
                            i2MenueEintrag.aktuellAusgewaehlt=false;
                            
                        }
                    }
                }
            }
        }
        
        
    }
    
    public void belegeUngeleseneNachrichtenZahlNeu() {
        List <EclMenueEintrag> lMenue=tMenueSession.getAuswahlMenue();
        for ( EclMenueEintrag iMenueEintrag : lMenue) {
            if (iMenueEintrag.funktionscode==KonstPMenueFunktionscode.POSTEINGANG) {
                int anzahlUngeleseneNachrichten=tNachrichtenSession.getAnzahlUngeleseneNachrichten();
                if (anzahlUngeleseneNachrichten>0) {
                    iMenueEintrag.textErgaenzung=" ("+Integer.toString(anzahlUngeleseneNachrichten)+")";
                }
                else {
                    iMenueEintrag.textErgaenzung="";
                }
            }
        }
        
    }
    
    
    
    public void auswaehlen(EclMenueEintrag pMenueEintrag) {
        /*Wenn true, dann war eine Maske mit Änderungsmöglichkeit ausgewählt, die nicht abgeschlossen wurd.
         * D.h. dann Warnhinweis bringen
         */
        boolean aenderungsmodusWarAktiv=false;
        
        /*Checken, ob zulässige "Absprungsmaske"*/
        int[] zulaessigeMasken= {KonstPortalView.P_AUSWAHL, KonstPortalView.NACHRICHTEN, KonstPortalView.P_AKTIONAERSDATEN, 
                KonstPortalView.P_BESTAND, KonstPortalView.P_KONTAKTFORMULAR, KonstPortalView.P_PUBLIKATIONEN, 
                KonstPortalView.P_HV_EINLADUNGSVERSAND, KonstPortalView.P_HV_ZUR_HV, KonstPortalView.P_HV_ZUR_BEIRATSWAHL, KonstPortalView.P_EINSTELLUNG_AENDERN, 
                 KonstPortalView.AUSWAHL1_DIALOGVERANSTALTUNGEN, KonstPortalView.AUSWAHL1_ANMELDEN_DIALOGVERANSTALTUNGEN,
                 KonstPortalView.P_BESTAND_HISTORIE, KonstPortalView.P_BETEILIGUNGSERHOEHUNG, KonstPortalView.P_UNTERLAGEN, KonstPortalView.P_VERANSTALTUNGEN_AUSWAHL,
                 KonstPortalView.P_VERANSTALTUNGEN_DETAIL};
        if (!tSessionVerwaltung.pruefeStart(zulaessigeMasken)) {
            return;
        }
        
        eclDbM.openAll();
        boolean brc=tPruefeStartNachOpen.pruefeStartNachOpen();
        eclDbM.closeAll();
        if (brc==false) {
            return;
        }
        
        /*Schon was vorher ausgewählt und angezeigt?*/
        EclMenueEintrag bisherigeAuswahl=tMenueSession.getAusgewaehltesMenue();
        if (bisherigeAuswahl!=null) {

            /*Prüfen, ob bisher angezeigte Maske eine "Eingabemaske" ist, und dementsprechend
             * Warnung beim Verlassen gebracht werden muß
             */
            if (tMenueSession.isAenderungsmodus()) {
                switch (bisherigeAuswahl.funktionscode) {
                case KonstPMenueFunktionscode.KONTAKTFORMULAR:
                    if (tKontaktformular.aenderungsverfolgungEtwasUngespeichert()) {
                        aenderungsmodusWarAktiv=true;
                    }
                    break;
                case KonstPMenueFunktionscode.PUBLIKATIONEN:
                    if (tPublikationen.aenderungsverfolgungEtwasUngespeichert()) {
                        aenderungsmodusWarAktiv=true;
                    }
                      break;
              case KonstPMenueFunktionscode.EINSTELLUNGEN:
                    if (pEinstellungenAendern.aenderungsverfolgungEtwasUngespeichert()) {
                        aenderungsmodusWarAktiv=true;
                    }
                    break;
                case KonstPMenueFunktionscode.HAUPTVERSAMMLUNG_EINLADUNGSVERSAND:
                    if (pHVEinladungsversand.aenderungsverfolgungEtwasUngespeichert()) {
                        aenderungsmodusWarAktiv=true;
                    }
                     break;
               }

                
            }

            /*Ggf. Ende-Verarbeitung durchführen*/
            switch (bisherigeAuswahl.funktionscode) {
            case KonstPMenueFunktionscode.KONTAKTFORMULAR:
                tKontaktformular.init(true, true);
                break;
            case KonstPMenueFunktionscode.PUBLIKATIONEN:
                 break;
            case KonstPMenueFunktionscode.EINSTELLUNGEN:
                break;
            case KonstPMenueFunktionscode.HAUPTVERSAMMLUNG_EINLADUNGSVERSAND:
                break;
           }

            /*Bisherige Menü-Auswahl zurücksetzen*/
            CaBug.druckeLog("Clear aufrufen", logDrucken, 10);
            clearMenue();
        }
        
        tMenueSession.setAenderungsmodus(false);
        /*Ausgewählten Menüeintrag als markiert anzeigen*/
        setzeAusgewaehlt(pMenueEintrag);
        printMenue();
        
        /*Ausgewählte Funktion aktivieren und anzeigen*/
        int neuesFenster=0;
        int rc=aktiviereAusgewaehltenMenuepunkt(pMenueEintrag.funktionscode, pMenueEintrag.funktionscodeSub);
        if (rc==-1) {
            return;
        }
        neuesFenster=rc;
        
        if (aenderungsmodusWarAktiv) {
            tSession.trageFehlerEin(CaFehler.perAenderungenWerdenNichtUebernommen);
        }
        
        tSessionVerwaltung.setzeEnde(neuesFenster);

    }

    /**-1 => Fehler aufgetreten*/
    private int aktiviereAusgewaehltenMenuepunkt(int pFunktionscode, int pFunktionscodeSub) {
    
        int neuesFenster=0;
        int rc=0;
        
        switch (pFunktionscode) {
        case KonstPMenueFunktionscode.KONTAKTFORMULAR:
            tKontaktformular.init(true, false);
            neuesFenster=KonstPortalView.P_KONTAKTFORMULAR;
            tMenueSession.setAenderungsmodus(true);
            tKontaktformular.aenderungsverfolgungStart();
            break;
        case KonstPMenueFunktionscode.MEINE_DATEN:
            rc=pAktionaersdaten.init(true);
            if (tRemoteAR.pruefeVerfuegbar(rc)==false) {return -1;}
            neuesFenster=KonstPortalView.P_AKTIONAERSDATEN;
            tMenueSession.setAenderungsmodus(false);
            break;
        case KonstPMenueFunktionscode.DATEN_BESTAND:
            rc=pBestand.init(true);
            if (tRemoteAR.pruefeVerfuegbar(rc)==false) {return -1;}
            neuesFenster=KonstPortalView.P_BESTAND;
            tMenueSession.setAenderungsmodus(false);
            break;
        case KonstPMenueFunktionscode.DATEN_BESTAND_SUB1:
            rc=pBestandhistorie.init(true);
            if (tRemoteAR.pruefeVerfuegbar(rc)==false) {return -1;}
            neuesFenster=KonstPortalView.P_BESTAND_HISTORIE;
            tMenueSession.setAenderungsmodus(false);
            break;
        case KonstPMenueFunktionscode.DATEN_BESTAND_SUB2:
            rc=pBeteiligungserhoehung.init(true);
            if (tRemoteAR.pruefeVerfuegbar(rc)==false) {return -1;}
            neuesFenster=KonstPortalView.P_BETEILIGUNGSERHOEHUNG;
            tMenueSession.setAenderungsmodus(false);
            break;
        case KonstPMenueFunktionscode.PUBLIKATIONEN:
            tPublikationen.init(true);
            neuesFenster=KonstPortalView.P_PUBLIKATIONEN;
            tMenueSession.setAenderungsmodus(true);
            tPublikationen.aenderungsverfolgungStart();
            break;
        case KonstPMenueFunktionscode.POSTEINGANG:
            tNachrichten.init(true);
            neuesFenster=KonstPortalView.NACHRICHTEN;
            tMenueSession.setAenderungsmodus(false);
            break;
        case KonstPMenueFunktionscode.STARTSEITE:
            tNachrichten.init(true);
            neuesFenster=KonstPortalView.P_AUSWAHL;
            tMenueSession.setAenderungsmodus(false);
            break;
        case KonstPMenueFunktionscode.EINSTELLUNGEN:
            pEinstellungenAendern.init();
            neuesFenster=KonstPortalView.P_EINSTELLUNG_AENDERN;
            tMenueSession.setAenderungsmodus(true);
            pEinstellungenAendern.aenderungsverfolgungStart();
            break;
        case KonstPMenueFunktionscode.HAUPTVERSAMMLUNG_EINLADUNGSVERSAND:
            pHVEinladungsversand.init(true);
            neuesFenster = KonstPortalView.P_HV_EINLADUNGSVERSAND;
            tMenueSession.setAenderungsmodus(true);
            pHVEinladungsversand.aenderungsverfolgungStart();
            break;
        case KonstPMenueFunktionscode.HAUPTVERSAMMLUNG_ZUM_HV_PORTAL:
            pHVzurHV.init(true);
            neuesFenster = KonstPortalView.P_HV_ZUR_HV;
            tMenueSession.setAenderungsmodus(false);
            break;
        case KonstPMenueFunktionscode.BEIRATSWAHL_ZUM_HV_PORTAL:
            pHVzurHV.init(true);
            neuesFenster = KonstPortalView.P_HV_ZUR_BEIRATSWAHL;
            tMenueSession.setAenderungsmodus(false);
            break;
        case KonstPMenueFunktionscode.VERANSTALTUNGEN:
            tAuswahl1.belegeAktiveModule();
            tVeranstaltungen.initMenue(true, pFunktionscodeSub);
            neuesFenster=KonstPortalView.P_VERANSTALTUNGEN_AUSWAHL;
            tMenueSession.setAenderungsmodus(true);
            tVeranstaltungen.aenderungsverfolgungStart();
            break;
        case KonstPMenueFunktionscode.DIALOGVERANSTALTUNGEN:
            tAuswahl1.belegeAktiveModule();
            tDialogveranstaltungen.init(true);
            neuesFenster=KonstPortalView.AUSWAHL1_DIALOGVERANSTALTUNGEN;
            tMenueSession.setAenderungsmodus(true);
            tDialogveranstaltungen.aenderungsverfolgungStart();
            break;
        case KonstPMenueFunktionscode.UNTERLAGEN:
            eclDbM.openAll();
            tAuswahl1.belegeAktiveModule();
            tUnterlagen.init(KonstPortalUnterlagen.ANZEIGEN_PPORTAL, pFunktionscodeSub);
            neuesFenster=KonstPortalView.P_UNTERLAGEN;
            tMenueSession.setAenderungsmodus(false);
            eclDbM.closeAll();
            break;
        default:
            neuesFenster=KonstPortalView.P_AUSWAHL;
            CaBug.drucke("Was nicht fertiges ausgewählt");
            break;
        }

        
        
        
        return neuesFenster;
    }
    
    
    
    
    
    
    private void setzeAusgewaehlt(EclMenueEintrag pMenueEintrag) {
        List <EclMenueEintrag> lMenue=tMenueSession.getAuswahlMenue();
        for ( EclMenueEintrag iMenueEintrag : lMenue) { // Hauptmenü-Einträge
            if (pMenueEintrag.nrHaupt==iMenueEintrag.nrHaupt) {
                iMenueEintrag.aktuellAusgewaehlt=true;
                if (iMenueEintrag.submenueVorhanden() && pMenueEintrag.nrSub!=0) {
                    for ( EclMenueEintrag i1MenueEintrag : iMenueEintrag.submenueList) { // Sub-Menü-Einträge
                        if (pMenueEintrag.nrSub==i1MenueEintrag.nrSub) {
                            i1MenueEintrag.aktuellAusgewaehlt=true;
                            if (i1MenueEintrag.submenueVorhanden() && pMenueEintrag.nrSubSub!=0) {
                                for ( EclMenueEintrag i2MenueEintrag : i1MenueEintrag.submenueList) {//Sub-Sub-Menü-Einträge
                                    if (pMenueEintrag.nrSubSub==iMenueEintrag.nrSubSub) {
                                        i2MenueEintrag.aktuellAusgewaehlt=true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        tMenueSession.setAusgewaehltesMenue(pMenueEintrag);

    }
    
    public void printMenue() {
        if (CaBug.pruefeLog(logDrucken, 10)==false) {return;}
        System.out.println("--------------------------------------------Menue-----------------------------");
        List <EclMenueEintrag> lMenue=tMenueSession.getAuswahlMenue();
        for ( EclMenueEintrag iMenueEintrag : lMenue) {
            printMenueEintrag(iMenueEintrag, 0);
            if (iMenueEintrag.submenueVorhanden()) {
                for ( EclMenueEintrag i1MenueEintrag : iMenueEintrag.submenueList) {
                    printMenueEintrag(i1MenueEintrag, 1);
                    if (i1MenueEintrag.submenueVorhanden()) {
                        for ( EclMenueEintrag i2MenueEintrag : i1MenueEintrag.submenueList) {
                            printMenueEintrag(i2MenueEintrag, 2);
                        }
                    }
                }
            }
        }
       
    }
    
    /**Ebene = 0, 1, 2*/
    private void printMenueEintrag(EclMenueEintrag pMenueEintrag, int pEbene) {
        String vorlauf="";
        switch (pEbene) {
        case 0:vorlauf=">"; break;
        case 1:vorlauf=">>>"; break;
        case 2:vorlauf=">>>>>>"; break;
        }
        System.out.println(vorlauf+"ident="+pMenueEintrag.ident+" "+pMenueEintrag.aktuellAusgewaehlt+" "+pMenueEintrag.nrHaupt+" "+pMenueEintrag.nrSub+" "+pMenueEintrag.nrSubSub);
    }
    
}
