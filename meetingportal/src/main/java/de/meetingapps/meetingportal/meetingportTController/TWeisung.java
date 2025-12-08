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
import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComEclM.EclAbstimmungM;
import de.meetingapps.meetingportal.meetComEclM.EclAbstimmungenListeM;
import de.meetingapps.meetingportal.meetComEclM.EclDbM;
import de.meetingapps.meetingportal.meetComEclM.EclParamM;
import de.meetingapps.meetingportal.meetComKonst.KonstPortalView;
import de.meetingapps.meetingportal.meetingportTFunktionen.TFunktionen;
import de.meetingapps.meetingportal.meetingportTFunktionen.TPruefeStartNachOpen;
import de.meetingapps.meetingportal.meetingportTFunktionen.TSessionVerwaltung;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@RequestScoped
@Named
public class TWeisung {

    private @Inject EclAbstimmungenListeM eclAbstimmungenListeM;
    private @Inject TSessionVerwaltung tSessionVerwaltung;
    private @Inject EclDbM eclDbM;
    private @Inject EclParamM eclParamM;
    private @Inject TSession tSession;
    private @Inject TWeisungBestaetigung tWeisungBestaetigung;
    private @Inject TFunktionen tFunktionen;
    private @Inject TPruefeStartNachOpen tPruefeStartNachOpen;

    private @Inject TAuswahl tAuswahl;

    /*++++++++++++++++++++++Die folgenden Buttons werden auch aus iWeisungAendern heraus aufgerufen+++++++++++++++++++++++++++++++++
     * deshalb keine Abfrage der aufrufenden Seite zulässig.
     */
    public void doAllesJa() {
        setzen("J");
        tSession.clearFehler();
    }

    public void doAllesNein() {
        setzen("N");
        tSession.clearFehler();
    }

    public void doAllesEnthaltung() {
        setzen("E");
        tSession.clearFehler();
    }

    public void doAllesLoeschen() {
        setzen("");
        tSession.clearFehler();
    }

    public void doAllesImSinne() {
        setzenSinne(true);
        tSession.clearFehler();
    }
    
    public void doAllesGegenSinne() {
        setzenSinne(false);
        tSession.clearFehler();
    }

    
    private void setzenSinne(boolean pImSinne) {
        List<EclAbstimmungM> lAbstimmungenListe = eclAbstimmungenListeM.getAbstimmungenListeM();
        int i;

        for (i = 0; i < lAbstimmungenListe.size(); i++) {
            if (!lAbstimmungenListe.get(i).isUeberschrift()) {

                String neuerAbstimmungsWert=lAbstimmungenListe.get(i).getAbstimmungsvorschlagGesellschaft();
                if (pImSinne==false) {
                    switch (neuerAbstimmungsWert) {
                    case "J":neuerAbstimmungsWert="N";break;
                    case "N":neuerAbstimmungsWert="J";break;
                    }
                }
                lAbstimmungenListe.get(i).setGewaehlt(neuerAbstimmungsWert);
            }
        }

        return;
    }


    private void setzen(String pSetzen) {
        List<EclAbstimmungM> lAbstimmungenListe = eclAbstimmungenListeM.getAbstimmungenListeM();
        int i;

        for (i = 0; i < lAbstimmungenListe.size(); i++) {
            if (!lAbstimmungenListe.get(i).isUeberschrift()) {

                lAbstimmungenListe.get(i).setGewaehlt(pSetzen);
            }
        }

        return;
    }

    public void doEinzelLoeschen(EclAbstimmungM abstimmung) {
        abstimmung.setGewaehlt("");
        tSession.clearFehler();
   }

    /*+++++++++++++++++++++++++++++Verarbeiten Weisungserteilung+++++++++++++++++++++++++*/
    /**Allgemeiner Teil - wird auch aus TWeisungAendern aufgerufen*/
    public boolean verarbeitenAllgemein() {


        String setzen = "";
        List<EclAbstimmungM> lAbstimmungenListe = eclAbstimmungenListeM.getAbstimmungenListeM();

        /*Ggf nicht markierte speichern wie Vorgabe*/
        if (eclParamM.getParam().paramPortal.pNichtmarkiertSpeichernAls != 0) {
            switch (eclParamM.getParam().paramPortal.pNichtmarkiertSpeichernAls) {
            case 1: {
                setzen = "J";
                break;
            }
            case 2: {
                setzen = "N";
                break;
            }
            case 3: {
                setzen = "E";
                break;
            }
            }

            setzeNichtMarkierteAuf(lAbstimmungenListe, setzen);
            setzeNichtMarkierteAuf(eclAbstimmungenListeM.getGegenantraegeListeM()   , setzen);
           
        }

        /*Gruppe überprüfen*/
        if (pruefeAbstimmungsgruppen(lAbstimmungenListe)==false) {
            tSessionVerwaltung.setzeEnde();
            return false;
        }

        return true;
    }

    private void setzeNichtMarkierteAuf(List<EclAbstimmungM> lAbstimmungenListe, String setzen ) {
        for (int i = 0; i < lAbstimmungenListe.size(); i++) {
            if (!lAbstimmungenListe.get(i).isUeberschrift()) {
                if (lAbstimmungenListe.get(i).getGewaehlt() == null
                        || (lAbstimmungenListe.get(i).getGewaehlt().compareTo("J") != 0
                                && lAbstimmungenListe.get(i).getGewaehlt().compareTo("N") != 0
                                && lAbstimmungenListe.get(i).getGewaehlt().compareTo("E") != 0)) {
                    lAbstimmungenListe.get(i).setGewaehlt(setzen);
                }
            }
        }
     }
    
    public boolean pruefeAbstimmungsgruppen(List<EclAbstimmungM> lAbstimmungenListe) {
        int[] anzahlJeGruppe = new int[11];
        for (int i1 = 1; i1 <= 10; i1++) {
            anzahlJeGruppe[i1] = 0;
        }
        
        for (int i = 0; i < lAbstimmungenListe.size(); i++) {
            if (!lAbstimmungenListe.get(i).isUeberschrift()) {

                int gruppe = lAbstimmungenListe.get(i).getZuAbstimmungsgruppe();
                //                  System.out.println("Abstimmungsgruppe="+gruppe);
                if (gruppe != 0) {
                    if (lAbstimmungenListe.get(i).getGewaehlt() != null
                            && lAbstimmungenListe.get(i).getGewaehlt().compareTo("J") == 0) {
                        //                          System.out.println("Gruppe++");
                        anzahlJeGruppe[gruppe]++;
                    }
                }
            }
        }

        for (int i1 = 1; i1 <= 10; i1++) {
            if (anzahlJeGruppe[i1] > eclParamM.getParam().paramAbstimmungParameter.anzahlJaJeAbstimmungsgruppe[i1]) {
                switch (i1) {
                case 1:
                    tSession.trageFehlerEin(CaFehler.afAbstimmgruppe1ZuViel);
                    break;
                case 2:
                    tSession.trageFehlerEin(CaFehler.afAbstimmgruppe2ZuViel);
                    break;
                case 3:
                    tSession.trageFehlerEin(CaFehler.afAbstimmgruppe3ZuViel);
                    break;
                case 4:
                    tSession.trageFehlerEin(CaFehler.afAbstimmgruppe4ZuViel);
                    break;
                case 5:
                    tSession.trageFehlerEin(CaFehler.afAbstimmgruppe5ZuViel);
                    break;
                case 6:
                    tSession.trageFehlerEin(CaFehler.afAbstimmgruppe6ZuViel);
                    break;
                case 7:
                    tSession.trageFehlerEin(CaFehler.afAbstimmgruppe7ZuViel);
                    break;
                case 8:
                    tSession.trageFehlerEin(CaFehler.afAbstimmgruppe8ZuViel);
                    break;
                case 9:
                    tSession.trageFehlerEin(CaFehler.afAbstimmgruppe9ZuViel);
                    break;
                case 10:
                    tSession.trageFehlerEin(CaFehler.afAbstimmgruppe10ZuViel);
                    break;
                }
                tSessionVerwaltung.setzeEnde();
                return false;
            }
        }
       
        return true;
    }
    public void doWeiter() {
        try {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.WEISUNG)) {
            return;
        }

        boolean brc = verarbeitenAllgemein();
        if (brc == false) {
            return;
        }

        if (eclParamM.getParam().paramPortal.bestaetigenDialog == 1) {
            tSessionVerwaltung.setzeEnde(KonstPortalView.WEISUNG_BESTAETIGUNG);
            return;
        } else {
            eclDbM.openAll();

            if (!tPruefeStartNachOpen.pruefeNachOpenPortalAktuelleHauptAktionAktion()) {
                eclDbM.closeAll();
                return;
            }

            
            brc = tWeisungBestaetigung.erteilenPortal();
            if (brc == false) {
                eclDbM.closeAllAbbruch();
                tSessionVerwaltung.setzeEnde();
                return;
            }

            if (eclParamM.getParam().paramPortal.quittungDialog == 1) {
                eclDbM.closeAll();
                tSessionVerwaltung.setzeEnde(KonstPortalView.WEISUNG_QUITTUNG);
                return;
            } else {
                int rcAuswahl=tAuswahl.startAuswahl(true);
                eclDbM.closeAll();
                tSessionVerwaltung.setzeEnde(rcAuswahl);
                return;
            }
        }
        } catch (Exception e) {
            CaBug.drucke("Exception");
            System.out.println(e.getMessage());
            e.printStackTrace();
            eclDbM.closeAll();
        }
    }

    public void doZurueck() {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.WEISUNG)) {
            return;
        }

        //		String auswahlMaske=aDlgVariablen.getAuswahlMaske();
        //		if (!auswahlMaske.isEmpty()) {
        //			aDlgVariablen.setAuswahlMaske("");
        //			return aFunktionen.setzeEnde(auswahlMaske, true, true);
        //		}

        tSessionVerwaltung.setzeEnde(tFunktionen.waehleAuswahl());
    }

}
