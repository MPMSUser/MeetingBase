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

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaDatumZeit;
import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComBa.BaMailM;
import de.meetingapps.meetingportal.meetComBrM.BrMKontaktformular;
import de.meetingapps.meetingportal.meetComEclM.EclDbM;
import de.meetingapps.meetingportal.meetComEclM.EclLoginDatenM;
import de.meetingapps.meetingportal.meetComEclM.EclParamM;
import de.meetingapps.meetingportal.meetComEclM.EclPortalTexteM;
import de.meetingapps.meetingportal.meetComEntities.EclAuftrag;
import de.meetingapps.meetingportal.meetComEntities.EclKontaktformularThema;
import de.meetingapps.meetingportal.meetComKonst.KonstAuftragEingangsweg;
import de.meetingapps.meetingportal.meetComKonst.KonstAuftragModul;
import de.meetingapps.meetingportal.meetComKonst.KonstPortalView;
import de.meetingapps.meetingportal.meetingportTFunktionen.TRemoteAR;
import de.meetingapps.meetingportal.meetingportTFunktionen.TSessionVerwaltung;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@RequestScoped
@Named
public class TKontaktformular {
    
    private int logDrucken=3;

    private @Inject EclDbM eclDbM;
    private @Inject BaMailM baMailm;

    private @Inject TSessionVerwaltung tSessionVerwaltung;
    private @Inject TSession tSession;
    private @Inject TMenueSession tMenueSession;
    private @Inject TMenue tMenue;
    private @Inject TLanguage tLanguage;
    private @Inject TKontaktformularSession tKontaktformularSession;
    private @Inject EclLoginDatenM eclLoginDatenM;
    private @Inject EclPortalTexteM eclPortalTexteM;

    private @Inject BrMKontaktformular brMKontaktformular;
    private @Inject EclParamM eclParamM;

    private @Inject TRemoteAR tRemoteAR;

    /**pOpenDurchfuehren=true => es wird der Open/close in init durchgeführt.
     * 
     * pSeparatesFenster=true => wurde als separates Fenster (ggf. ohne Anmeldung), ausgeführt.
     *      In diesem Fall werden alle Werte auf "Standard" zurückgesetzt - so als ob kein
     *      Aktionär eingeloggt ist.
     *      Wird auch immer am Ende nach Verlassen des Dialogs aufgerufen, um sicherzustellen
     *      dass beim Neuaufruf nur als Link alles "leer" ist. 
     */
    public void init(boolean pOpenDurchfuehren, boolean pSeparatesFenster) {
        tKontaktformularSession.setIdentThemenListeGewaehlt(-1);
        tKontaktformularSession.setTextEingabe("");
    }
    
    /**Wird beim Initialisieren der Maske aufgerufen, um Änderungsverfolgung zu initialisieren -
     * Änderungsverfolgung dient dazu, um beim Verlassen über menü festzustellen,
     * ob noch Eingaben unabgespeichert sind
     */
    public void aenderungsverfolgungStart() {
        
    }
    
    /**Liefert beim Verlassen der Maske über Menü true, wenn unabgespeicherte
     * Änderungen vorhanden sind
      */
    public boolean aenderungsverfolgungEtwasUngespeichert() {
        CaBug.druckeLog("(tKontaktformularSession.getIdentThemenListeGewaehlt()="+tKontaktformularSession.getIdentThemenListeGewaehlt(), logDrucken, 10);
        if (tKontaktformularSession.getIdentThemenListeGewaehlt()>0) {return true;}
        if (!tKontaktformularSession.getTextEingabe().isEmpty()) {return true;}
        return false;
    }
    
    
    public void doSenden() {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.P_KONTAKTFORMULAR)) {
            return;
        }

        int kontaktThemaIdent=tKontaktformularSession.getIdentThemenListeGewaehlt();
        String kontaktThemaWeitergabeText="";
        String kontaktThemaText="";
        
        /*Thema überprüfen / verarbeiten*/
        if (eclParamM.getParam().paramPortal.kontaktformularThemaAnbieten!=0) {
            CaBug.druckeLog("Themen-Ident="+kontaktThemaIdent, logDrucken, 10);
        }
        
        /*Text überprüfen / verarbeiten*/
        String kontaktText=tKontaktformularSession.getTextEingabe().trim();
        if (kontaktText.length()==0) {
            tSession.trageFehlerEin(CaFehler.perKontaktformularTextFehlt);
            tSessionVerwaltung.setzeEnde();
        }
        
        
        eclDbM.openAll();eclDbM.openWeitere();
        if (kontaktThemaIdent>0) {
            for (EclKontaktformularThema iKontaktformularThema:eclParamM.getKontaktformularThemenListe()) {
                if (iKontaktformularThema.ident==kontaktThemaIdent) {
                    kontaktThemaWeitergabeText=iKontaktformularThema.weitergabeText;
                    if (tLanguage.getLang()==1) {
                        kontaktThemaText=iKontaktformularThema.spracheDEText;
                    }
                    else {
                        kontaktThemaText=iKontaktformularThema.spracheENText;
                    }
                }
            }
        }
        
        String aktionaersnummer=eclLoginDatenM.getAnmeldeKennungFuerAnzeige();
        if (kontaktThemaText.trim().isEmpty()) {
            tSession.trageFehlerEin(CaFehler.perKontaktformularThemaFehlt);
            eclDbM.closeAll();
            tSessionVerwaltung.setzeEnde();
            return;
        }
        
        /*Verarbeitung durchführen*/
        String hBetreff = eclPortalTexteM.holeIText(1) + eclParamM.getEmittentName();
        if (!kontaktThemaWeitergabeText.isEmpty()) {
            hBetreff += " - " + kontaktThemaWeitergabeText;
        }
        hBetreff+=" - "+aktionaersnummer;

        String hMailText = kontaktText;

        if (eclParamM.getParam().paramPortal.kontaktformularBeiEingangMail==1) {
            String empfaengerListe = eclParamM.getParam().paramPortal.kontaktformularBeiEingangMailAn;
            String[] zeileSplitKomma=empfaengerListe.split(";");
            for (int i1=0;i1<zeileSplitKomma.length;i1++) {
                String empfaenger = zeileSplitKomma[i1].trim();
                if (!empfaenger.isEmpty()) {
                    System.out.println("Kontaktanfrage senden an:" + empfaenger);
                    baMailm.senden(empfaenger, hBetreff, hMailText);
                }
            }
        }
        if (eclParamM.getParam().paramPortal.kontaktformularBeiEingangAufgabe==1) {
            String lSchluessel="";
            if (tSession.isPermanentPortal()) {
                lSchluessel=brMKontaktformular.senden(aktionaersnummer, kontaktThemaIdent, kontaktThemaWeitergabeText, kontaktThemaText, hMailText);
                if (lSchluessel==null) {
                    tRemoteAR.pruefeVerfuegbar(CaFehler.perRemoteAktienregisterNichtVerfuegbar);
                    eclDbM.closeAll();
                    tSessionVerwaltung.setzeEnde();
                    return;
                }
            }
            

            EclAuftrag lAuftrag=new EclAuftrag();
            lAuftrag.schluessel=lSchluessel;
            lAuftrag.gehoertZuModul=KonstAuftragModul.KONTAKT_FORMULAR;
            lAuftrag.zeitStart=CaDatumZeit.DatumZeitStringFuerDatenbank();
            lAuftrag.eingangsweg=KonstAuftragEingangsweg.aktionaerPortal;
            lAuftrag.userIdAuftraggeber=eclLoginDatenM.getEclLoginDaten().ident*(-1);
            lAuftrag.freitextBeschreibung=hMailText;
            lAuftrag.parameterTextLang[0]=kontaktThemaWeitergabeText;
            lAuftrag.parameterTextLang[1]=kontaktThemaText;
            lAuftrag.parameterInt[0]=kontaktThemaIdent;
            eclDbM.getDbBundle().dbAuftrag.mandantenabhaengig=false;
            eclDbM.getDbBundle().dbAuftrag.insert(lAuftrag);
            eclDbM.getDbBundle().dbAuftrag.mandantenabhaengig=true;
        }
        
        eclDbM.closeAll();
        
        CaBug.druckeLog("Kontaktformular verarbeitet", logDrucken, 10);
        
        tMenueSession.setAenderungsmodus(false);
        tMenue.clearMenue();
        tSession.trageQuittungTextNr(1563, 2);
        tSessionVerwaltung.setzeEnde(KonstPortalView.P_AUSWAHL);

    }
    
}
