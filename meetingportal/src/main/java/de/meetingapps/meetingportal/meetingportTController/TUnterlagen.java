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
import de.meetingapps.meetingportal.meetComEclM.EclDbM;
import de.meetingapps.meetingportal.meetComEclM.EclParamM;
import de.meetingapps.meetingportal.meetComEclM.EclPortalUnterlagenM;
import de.meetingapps.meetingportal.meetComKonst.KonstPortalFunktionen;
import de.meetingapps.meetingportal.meetComKonst.KonstPortalTexte;
import de.meetingapps.meetingportal.meetComKonst.KonstPortalUnterlagen;
import de.meetingapps.meetingportal.meetComKonst.KonstPortalView;
import de.meetingapps.meetingportal.meetingCoreReport.RpBrowserAnzeigen;
import de.meetingapps.meetingportal.meetingportTFunktionen.TPortalFunktionen;
import de.meetingapps.meetingportal.meetingportTFunktionen.TPruefeStartNachOpen;
import de.meetingapps.meetingportal.meetingportTFunktionen.TSessionVerwaltung;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@RequestScoped
@Named
public class TUnterlagen {

    private @Inject EclDbM eclDbM;
    private @Inject EclParamM eclParamM;
    private @Inject TSessionVerwaltung tSessionVerwaltung;
    private @Inject TSession tSession;
    private @Inject TAuswahlSession tAuswahlSession;
    private @Inject TLoginLogoutSession tLoginLogoutSession;

    private @Inject TPortalFunktionen tPortalFunktionen;

    private @Inject TUnterlagenSession tUnterlagenSession;
    private @Inject TPruefeStartNachOpen tPruefeStartNachOpen;
    
    private int logDrucken = 10;

    
    private int[] aufrufSeiten= {KonstPortalView.UNTERLAGEN, KonstPortalView.BOTSCHAFTEN_ANZEIGEN, KonstPortalView.LOGIN,  KonstPortalView.P_UNTERLAGEN};
    
    /**
     * pUnterlagenbereich aus KonstPortalUnterlagen
     * 
     * pUnterlagenSubBereich wird nur verwendet, wenn pUnterlagenbereich=KonstPortalUnterlagen.ANZEIGEN_PPORTAL.
     * In diesem Fall wird dann funktionscodeSub des ausgewählten Menüeintrages hierfür verwendet.
     * 
     * Open in aufrufender Funktion*/
    public int init(int pUnterlagenbereich, int pUnterlagenSubBereich) {
        CaBug.druckeLog("pUnterlagenbereich="+pUnterlagenbereich+" pUnterlagenSubBereich="+pUnterlagenSubBereich, logDrucken, 10);
        tUnterlagenSession.setUnterlagenbereich(pUnterlagenbereich);
        tUnterlagenSession.setUnterlagenSubBereich(pUnterlagenSubBereich);
        
        List<EclPortalUnterlagenM> unterlagenListe =tPortalFunktionen.erzeugeUnterlagenliste(pUnterlagenbereich, pUnterlagenSubBereich);
        switch (pUnterlagenbereich) {
        case KonstPortalUnterlagen.ANZEIGEN_EXTERNE_SEITE:
            break;
        case KonstPortalUnterlagen.ANZEIGEN_LOGIN_OBEN:
            tLoginLogoutSession.setUnterlagenListeOben(unterlagenListe);
            break;
        case KonstPortalUnterlagen.ANZEIGEN_LOGIN_UNTEN:
            tLoginLogoutSession.setUnterlagenListeUnten(unterlagenListe);
            break;
        case KonstPortalUnterlagen.ANZEIGEN_BOTSCHAFTEN:
        case KonstPortalUnterlagen.ANZEIGEN_UNTERLAGEN:
        case KonstPortalUnterlagen.ANZEIGEN_PPORTAL:
            tUnterlagenSession.setUnterlagenListe(unterlagenListe);
            break;
        }
        
        if (pUnterlagenbereich==KonstPortalUnterlagen.ANZEIGEN_UNTERLAGEN) {
            tUnterlagenSession.setStarttextAktiv(KonstPortalTexte.IUNTERLAGEN_STARTTEXT_AKTIV);
            tUnterlagenSession.setTextWennAktivAberListeLeer(KonstPortalTexte.IUNTERLAGEN_TEXT_WENN_AKTIV_ABER_LISTE_LEER);
            tUnterlagenSession.setEndetextAktiv(KonstPortalTexte.IUNTERLAGEN_ENDETEXT_AKTIV);
            tUnterlagenSession.setTextInaktiv(KonstPortalTexte.IUNTERLAGEN_TEXT_INAKTIV);
            tUnterlagenSession.setEndetextImmer(KonstPortalTexte.IUNTERLAGEN_ENDETEXT_IMMER);
            
            tUnterlagenSession.setAnsehenAktiv(tAuswahlSession.unterlagenAktiv());
        }
        if (pUnterlagenbereich==KonstPortalUnterlagen.ANZEIGEN_BOTSCHAFTEN) {
            tUnterlagenSession.setStarttextAktiv(KonstPortalTexte.IBOTSCHAFTEN_ANZEIGEN_STARTTEXT_AKTIV);
            tUnterlagenSession.setTextWennAktivAberListeLeer(KonstPortalTexte.IBOTSCHAFTEN_TEXT_WENN_AKTIV_ABER_LISTE_LEER);
            tUnterlagenSession.setEndetextAktiv(KonstPortalTexte.IBOTSCHAFTEN_ANZEIGEN_ENDETEXT_AKTIV);
            tUnterlagenSession.setTextInaktiv(KonstPortalTexte.IBOTSCHAFTEN_ANZEIGEN_TEXT_INAKTIV);
            tUnterlagenSession.setEndetextImmer(KonstPortalTexte.IBOTSCHAFTEN_ANZEIGEN_ENDETEXT_IMMER);
            
            tUnterlagenSession.setAnsehenAktiv(tAuswahlSession.isBotschaftenAnzeigeAktiv());
        }
        return 1;
    }

    public void doUnterlageDE(EclPortalUnterlagenM pPortalUnterlageM) {
        CaBug.druckeLog("", logDrucken, 10);
        doUnterlage(pPortalUnterlageM.getDateiname(), "DE");
    }

    public void doUnterlageEN(EclPortalUnterlagenM pPortalUnterlageM) {
        doUnterlage(pPortalUnterlageM.getDateiname(), "EN");
    }

    private void doUnterlage(String pDateiname, String pSprache) {
        if (!tSessionVerwaltung.pruefeStart(aufrufSeiten)) {
            return;
        }

        eclDbM.openAll();
        boolean brc=true;
        int anzeigeBereich=tUnterlagenSession.getUnterlagenbereich();
        int anzeigeSubBereich=tUnterlagenSession.getUnterlagenSubBereich();
        
        switch (tSession.getViewnummer()) {
        case KonstPortalView.UNTERLAGEN:
            brc=tPruefeStartNachOpen.pruefeNachOpenPortalFunktion(KonstPortalFunktionen.unterlagen, false);
            break;
        case KonstPortalView.BOTSCHAFTEN_ANZEIGEN:
            brc=tPruefeStartNachOpen.pruefeNachOpenPortalFunktion(KonstPortalFunktionen.botschaftenAnzeige, false);
            break;
        case KonstPortalView.LOGIN:
            break;
        }
        if (brc==false) {
            return;
        }
        eclDbM.closeAll();

        CaBug.druckeLog("anzeigeBereich="+anzeigeBereich+" anzeigeSubgBereich="+anzeigeSubBereich, logDrucken, 10);
        List<EclPortalUnterlagenM> unterlagenListeM = tPortalFunktionen.erzeugeUnterlagenliste(anzeigeBereich, anzeigeSubBereich); 
        int gef = -1;
        for (int i = 0; i < unterlagenListeM.size(); i++) {
            CaBug.druckeLog("i="+i, logDrucken, 10);
            if (unterlagenListeM.get(i).getArt() == 0 && unterlagenListeM.get(i).getDateiname().equals(pDateiname)) {
                gef = i;
            }
        }
        if (gef == -1) {
            pDateiname = "nichtMoeglich";
        }

 
        String pfadUndDateiname=eclParamM.getParamGeraet().lwPfadGrossdokumente + "\\" + eclParamM.getMandantPfad()+"\\" + pDateiname ;
        if (unterlagenListeM.get(gef).getDateiMehrsprachigVorhanden()==1) {
            pfadUndDateiname=pfadUndDateiname + pSprache;
        }
        pfadUndDateiname=pfadUndDateiname + ".pdf";
        
        RpBrowserAnzeigen rpBrowserAnzeigen = new RpBrowserAnzeigen();
        CaBug.druckeLog("pfadUndDateiname="+pfadUndDateiname, logDrucken, 10);
        rpBrowserAnzeigen.zeigen(pfadUndDateiname);

        if (tSession.getViewnummer()==KonstPortalView.LOGIN) {
            tSessionVerwaltung.setzeEnde(KonstPortalView.LOGIN);
        }
        if (tSession.getViewnummer()==KonstPortalView.BOTSCHAFTEN_ANZEIGEN) {
            tSessionVerwaltung.setzeEnde(KonstPortalView.BOTSCHAFTEN_ANZEIGEN);
        }
        if (tSession.getViewnummer()==KonstPortalView.UNTERLAGEN) {
            tSessionVerwaltung.setzeEnde(KonstPortalView.UNTERLAGEN);
        }
        if (tSession.getViewnummer()==KonstPortalView.P_UNTERLAGEN) {
            tSessionVerwaltung.setzeEnde(KonstPortalView.P_UNTERLAGEN);
        }
        return;

    }

    public void doZurueck() {
        if (!tSessionVerwaltung.pruefeStart(aufrufSeiten)) {
            return;
        }

        tSessionVerwaltung.setzeEnde(tSession.getRueckkehrZuMenue());
        return;

    }

    public void doZurueckAuswahl1() {
        doZurueck();
     }

}
