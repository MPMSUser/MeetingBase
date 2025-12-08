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

import java.util.List;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComBl.BlNachrichten;
import de.meetingapps.meetingportal.meetComBl.BlTexte;
import de.meetingapps.meetingportal.meetComBrM.BrMAuftraege;
import de.meetingapps.meetingportal.meetComEclM.EclDbM;
import de.meetingapps.meetingportal.meetComEclM.EclLoginDatenM;
import de.meetingapps.meetingportal.meetComEclM.EclPortalTexteM;
import de.meetingapps.meetingportal.meetComEntities.EclNachricht;
import de.meetingapps.meetingportal.meetComKonst.KonstPortalView;
import de.meetingapps.meetingportal.meetingportTFunktionen.TRemoteAR;
import de.meetingapps.meetingportal.meetingportTFunktionen.TSessionVerwaltung;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@RequestScoped
@Named
public class TNachrichten {

    private int logDrucken=10;

    private @Inject EclDbM eclDbM;
    private @Inject EclLoginDatenM eclLoginDatenM;

    private @Inject TSession tSession;
    private @Inject TNachrichtenSession tNachrichtenSession;
    private @Inject EclPortalTexteM eclPortalTexteM;
    
    private @Inject TMenue tMenue;
    private @Inject TSessionVerwaltung tSessionVerwaltung;
   
    private @Inject BrMAuftraege brMAuftraege;

    private @Inject TRemoteAR tRemoteAR;

    /**pOpenDurchfuehren=true => es wird der Open/close in init durchgeführt.
     */
    public void init(boolean pOpenDurchfuehren) {
        if (pOpenDurchfuehren) {
            eclDbM.openAll();
        }
     
        if (tSession.isPermanentPortal()) {
            /*Status Aufträge holen und updaten*/
            int rc=brMAuftraege.aktualisiereLokaleAuftraege(eclLoginDatenM.getEclLoginDaten().loginKennung, eclLoginDatenM.getEclLoginDaten().ident*(-1));
            if (tRemoteAR.pruefeVerfuegbar(rc)==false) {
                if (pOpenDurchfuehren) {
                    eclDbM.closeAll();
                }
                return;
            }
        }
        
        BlNachrichten blNachrichten=new BlNachrichten(true, eclDbM.getDbBundle());
        blNachrichten.leseNachrichtenUndAuftraegeFuerAktionaer(eclLoginDatenM.getEclLoginDaten().ident*(-1));
        
        List<EclNachricht> nachrichten=blNachrichten.rcEmpfangeneNachrichtenList;
        if (nachrichten!=null) {
            for (int i=0;i<nachrichten.size();i++) {
                EclNachricht lNachricht=nachrichten.get(i);
                if (lNachricht.mailTextNr!=0) {
                    lNachricht.mailText=eclPortalTexteM.holeIText(lNachricht.mailTextNr);
                    BlTexte blTexte=new BlTexte();
                    lNachricht.mailText=blTexte.ersetzeVariablenInText(eclPortalTexteM.holeIText(lNachricht.mailTextNr), lNachricht.variablenListe, lNachricht.inhaltListe);

                }
            }
        }
        
        tNachrichtenSession.setEmpfangeneNachrichten(blNachrichten.rcEmpfangeneNachrichtenList);
        tNachrichtenSession.setAnzahlUngeleseneNachrichten(blNachrichten.rcEmpfangeneNachrichtenAnzahlUngelesen);
        
        tMenue.belegeUngeleseneNachrichtenZahlNeu();
        
        /*Angezeigte Detailnachricht auf leer setzen*/
        tNachrichtenSession.setNachrichtDetailText("");
        tNachrichtenSession.setNachrichtDetailWirdAngezeigt(false);
        
        if (pOpenDurchfuehren) {
            eclDbM.closeAll();
        }

    }
    
    public void doNachrichtAnzeigen(EclNachricht pNachricht) {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.NACHRICHTEN)) {
            return;
        }
        /*Nachrichten-Detailanzeige belegen*/
        tNachrichtenSession.setNachrichtDetailWirdAngezeigt(true);
        tNachrichtenSession.setNachrichtDetailText(pNachricht.mailText);
        tNachrichtenSession.setNachrichtDetailNachricht(pNachricht);
        tNachrichtenSession.setNachrichtDetailMailTextTextNrVor(pNachricht.mailTextTextNrVor);
        tNachrichtenSession.setNachrichtDetailMailTextTextNrNach(pNachricht.mailTextTextNrNach);
        tNachrichtenSession.setNachrichtDetailFunktionsButton1TextNr(pNachricht.funktionsButton1TextNr);
        tNachrichtenSession.setNachrichtDetailFunktionsButton2TextNr(pNachricht.funktionsButton2TextNr);
        
        /*Falls Nachricht noch nicht gelesen, dann auf Gelesen setzen*/
        if (pNachricht.anzeigeBeimEmpfaengerGelesen==0) {
            pNachricht.anzeigeBeimEmpfaengerGelesen=1;
            
            /*Gelesen-Status in Table updaten*/
            eclDbM.openAll();eclDbM.openWeitere();
            BlNachrichten blNachrichten=new BlNachrichten(true, eclDbM.getDbBundle());
            blNachrichten.nachrichtSetzeGelesenBeimEmpfaenger(pNachricht);
            int anzahlUngeleseneNachrichten=tNachrichtenSession.getAnzahlUngeleseneNachrichten();
            tNachrichtenSession.setAnzahlUngeleseneNachrichten(anzahlUngeleseneNachrichten-1);
            tMenue.belegeUngeleseneNachrichtenZahlNeu();
            eclDbM.closeAll();
        }
        
        List<EclNachricht> empfangeneNachrichten = tNachrichtenSession.getEmpfangeneNachrichten();
        for (EclNachricht iNachricht: empfangeneNachrichten) {
            iNachricht.detailNachrichtWirdAngezeigt=false;
        }
        
        pNachricht.detailNachrichtWirdAngezeigt=true;
        
        tSessionVerwaltung.setzeEnde();
    }
    
    public void doNachrichtLoeschen() {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.NACHRICHTEN)) {
            return;
        }
        EclNachricht lNachricht=tNachrichtenSession.getNachrichtDetailNachricht();
        
        CaBug.druckeLog("Start", logDrucken, 10);
        eclDbM.openAll();eclDbM.openWeitere();
        
       /*Gelöscht-Status in Table updaten*/
        BlNachrichten blNachrichten=new BlNachrichten(true, eclDbM.getDbBundle());
        blNachrichten.nachrichtAusblendenBeimEmpfaenger(lNachricht);
        
        /*Nachrichten neu einlesen*/
        init(false);
        
        eclDbM.closeAll();
        
        CaBug.druckeLog("Ende", logDrucken, 10);
        tNachrichtenSession.setNachrichtDetailWirdAngezeigt(false);
        tSessionVerwaltung.setzeEnde();

    }
}
