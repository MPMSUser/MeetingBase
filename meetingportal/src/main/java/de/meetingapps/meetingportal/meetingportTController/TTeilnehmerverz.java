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
import de.meetingapps.meetingportal.meetComEclM.EclAbstTeilButtonM;
import de.meetingapps.meetingportal.meetComEclM.EclDbM;
import de.meetingapps.meetingportal.meetComEclM.EclParamM;
import de.meetingapps.meetingportal.meetComKonst.KonstPortalFunktionen;
import de.meetingapps.meetingportal.meetComKonst.KonstPortalView;
import de.meetingapps.meetingportal.meetingCoreReport.RpBrowserAnzeigen;
import de.meetingapps.meetingportal.meetingportTFunktionen.TFunktionen;
import de.meetingapps.meetingportal.meetingportTFunktionen.TPruefeStartNachOpen;
import de.meetingapps.meetingportal.meetingportTFunktionen.TSessionVerwaltung;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@RequestScoped
@Named
public class TTeilnehmerverz {

    private int logDrucken = 3;

    private @Inject TTeilnehmerverzSession tTeilnehmerverzSession;
    private @Inject EclDbM eclDbM;
    private @Inject EclParamM eclParamM;
    private @Inject TSessionVerwaltung tSessionVerwaltung;
    private @Inject TFunktionen tFunktionen;
    private @Inject TPruefeStartNachOpen tPruefeStartNachOpen;
    private @Inject TAuswahlSession tAuswahlSession;

    /**open wird hier in der aufrufenden Funktion gehandelt*/
    public void init(boolean pFuelleFuerMonitoring) {
        List<EclAbstTeilButtonM> absTeilButtonList = new LinkedList<EclAbstTeilButtonM>();
        int lfdNr = 0;
        CaBug.druckeLog("tAuswahlSession.isTeilnehmerverzeichnisAktiv()=" + tAuswahlSession.isTeilnehmerverzeichnisAktiv(), logDrucken, 10);
        if (tAuswahlSession.isTeilnehmerverzeichnisAktiv() == true || pFuelleFuerMonitoring==true) {

            if (eclParamM.getParam().paramPortal.teilnehmerverzLetzteNr > -1 && eclParamM.getParam().paramPortal.teilnehmerverzBeginnendBei == 0) {
                EclAbstTeilButtonM lAbstTeilButtonM = new EclAbstTeilButtonM();
                lAbstTeilButtonM.setLfdNr(lfdNr);
                lfdNr++;

                /*Erstpräsenz*/
                lAbstTeilButtonM.setButtontext1Vorne("1221");
                lAbstTeilButtonM.setButtontext1Ziffer("");
                lAbstTeilButtonM.setButtontext1Hinten("1422");
                lAbstTeilButtonM.setDateiname1("Praesenz0");

                /*Zusammenstellung Erstpräsenz*/
                lAbstTeilButtonM.setButtontext2Vorne("1222");
                lAbstTeilButtonM.setButtontext2Ziffer(""); /*Erstpräsenz*/
                lAbstTeilButtonM.setButtontext2Hinten("1423");/*Erstpräsenz*/
                lAbstTeilButtonM.setDateiname2("Praesenz0zusammen");

                absTeilButtonList.add(lAbstTeilButtonM);
            }

            for (int i = 1; i <= eclParamM.getParam().paramPortal.teilnehmerverzLetzteNr; i++) {
                EclAbstTeilButtonM lAbstTeilButtonM = new EclAbstTeilButtonM();
                lAbstTeilButtonM.setLfdNr(lfdNr);
                lfdNr++;

                /*Nachtrag + Nummer*/
                lAbstTeilButtonM.setButtontext1Vorne("1223");
                lAbstTeilButtonM.setButtontext1Ziffer(Integer.toString(i));
                lAbstTeilButtonM.setButtontext1Hinten("1424");
                lAbstTeilButtonM.setDateiname1("Praesenz" + Integer.toString(i));

                /*Zusammenstellung Nachtrag*/
                lAbstTeilButtonM.setButtontext2Vorne("1224");
                lAbstTeilButtonM.setButtontext2Ziffer(Integer.toString(i));
                lAbstTeilButtonM.setButtontext2Hinten("1425");
                lAbstTeilButtonM.setDateiname2("Praesenz" + Integer.toString(i) + "zusammen");
                absTeilButtonList.add(lAbstTeilButtonM);
            }
        }

        tTeilnehmerverzSession.setAbsTeilButtonList(absTeilButtonList);

    }

    public void doAnzeige(EclAbstTeilButtonM lAbstTeilButtonM) {
        boolean anzeigenMoeglich = true;

        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.TEILNEHMERVERZEICHNIS)) {
            return;
        }
        
        eclDbM.openAll();
        int lPortalFunktion = KonstPortalFunktionen.portalFunktionZuView(KonstPortalView.TEILNEHMERVERZEICHNIS);
        boolean brc = tPruefeStartNachOpen.pruefeNachOpenPortalFunktion(lPortalFunktion, true);
        if (brc == false) {
            anzeigenMoeglich = false;
        }
        eclDbM.closeAll();

        anzeigen(anzeigenMoeglich, lAbstTeilButtonM);

        tSessionVerwaltung.setzeEnde(KonstPortalView.TEILNEHMERVERZEICHNIS);
        return;
    }

    public void doAnzeigeMonitoring(EclAbstTeilButtonM lAbstTeilButtonM) {

        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.MONITORING)) {
            return;
        }

        anzeigen(true, lAbstTeilButtonM);

        tSessionVerwaltung.setzeEnde(KonstPortalView.MONITORING);
        return;
    }

    private void anzeigen(boolean anzeigenMoeglich, EclAbstTeilButtonM lAbstTeilButtonM) {

        String dateiName = "";

        if (anzeigenMoeglich == false) {
            dateiName = "nichtMoeglich";
        } else {
            dateiName = lAbstTeilButtonM.getDateiname1();
        }

        RpBrowserAnzeigen rpBrowserAnzeigen = new RpBrowserAnzeigen();
        rpBrowserAnzeigen.zeigen(eclParamM.getClGlobalVar().lwPfadAllgemein + "\\"+eclParamM.getParamServer().praefixPfadVerzeichnisse
                + "ausdruckeintern\\" + eclParamM.getMandantPfad() + "\\" + dateiName + ".pdf");
    }
    
    public void doAnzeigeZusammen(EclAbstTeilButtonM lAbstTeilButtonM) {

        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.TEILNEHMERVERZEICHNIS)) {
            return;
        }

        boolean anzeigenMoeglich = true;
        eclDbM.openAll();
        int lPortalFunktion = KonstPortalFunktionen.portalFunktionZuView(KonstPortalView.TEILNEHMERVERZEICHNIS);
        boolean brc = tPruefeStartNachOpen.pruefeNachOpenPortalFunktion(lPortalFunktion, true);
        if (brc == false) {
            anzeigenMoeglich = false;
        }
        eclDbM.closeAll();

        anzeigenZusammen(anzeigenMoeglich, lAbstTeilButtonM);

        tSessionVerwaltung.setzeEnde(KonstPortalView.TEILNEHMERVERZEICHNIS);
        return;

    }

    public void doAnzeigeZusammenMonitoring(EclAbstTeilButtonM lAbstTeilButtonM) {

        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.MONITORING)) {
            return;
        }


        anzeigenZusammen(true, lAbstTeilButtonM);

        tSessionVerwaltung.setzeEnde(KonstPortalView.MONITORING);
        return;

    }

    private void anzeigenZusammen(boolean anzeigenMoeglich, EclAbstTeilButtonM lAbstTeilButtonM) {
        String dateiName = "";

        if (anzeigenMoeglich == false) {
            dateiName = "nichtMoeglich";
        } else {
            dateiName = lAbstTeilButtonM.getDateiname2();
        }

        RpBrowserAnzeigen rpBrowserAnzeigen = new RpBrowserAnzeigen();
        rpBrowserAnzeigen.zeigen(eclParamM.getClGlobalVar().lwPfadAllgemein + "\\"+eclParamM.getParamServer().praefixPfadVerzeichnisse
                + "ausdruckeintern\\" + eclParamM.getMandantPfad() + "\\" + dateiName + ".pdf");
        
    }
    
    
    public void doZurueck() {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.TEILNEHMERVERZEICHNIS)) {
            return;
        }
        tSessionVerwaltung.setzeEnde(tFunktionen.waehleAuswahlNachPraesenzfunktion());
        return;
    }

    public void doRefresh() {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.TEILNEHMERVERZEICHNIS)) {
            return;
        }
        eclDbM.openAll();
        int lPortalFunktion = KonstPortalFunktionen.portalFunktionZuView(KonstPortalView.TEILNEHMERVERZEICHNIS);
        boolean brc = tPruefeStartNachOpen.pruefeNachOpenPortalFunktion(lPortalFunktion, false);
        if (brc == false) {
            eclDbM.closeAll();
            return;
        }
        init(false);
        eclDbM.closeAll();
        tSessionVerwaltung.setzeEnde(KonstPortalView.TEILNEHMERVERZEICHNIS);
        return;
    }

}
