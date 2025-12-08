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
import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComBl.BlWillenserklaerung;
import de.meetingapps.meetingportal.meetComEclM.EclDbM;
import de.meetingapps.meetingportal.meetComEntities.EclPersonenNatJur;
import de.meetingapps.meetingportal.meetComEntities.EclWillenserklaerungStatusNeu;
import de.meetingapps.meetingportal.meetComEntities.EclZugeordneteMeldungNeu;
import de.meetingapps.meetingportal.meetingportTFunktionen.TFunktionen;
import de.meetingapps.meetingportal.meetingportTFunktionen.TQuittungen;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@RequestScoped
@Named
/**Funktion ist noch nicht für Portal aufbereitet, sondern nur für WAIntern / Bestandsverwaltung*/
public class TVollmachtDritteStornieren {

    private int logDrucken=10;
    private @Inject TSession tSession;
    private @Inject TWillenserklaerungSession tWillenserklaerungSession;
    private @Inject EclDbM eclDbM;
    private @Inject TFunktionen tFunktionen;
    private @Inject TQuittungen tQuittungen;

    
    public boolean stornierenAusfuehren(boolean pMitBestaetigung) {
        for (int i = 0; i < tWillenserklaerungSession.getZugeordneteMeldungFuerAusfuehrungListe().size(); i++) {
            EclZugeordneteMeldungNeu iEclZugeordneteMeldungNeu = tWillenserklaerungSession
                    .getZugeordneteMeldungFuerAusfuehrungListe().get(i);
            EclWillenserklaerungStatusNeu iEclWillenserklaerungStatusNeu=
                    tWillenserklaerungSession.getZugeordneteWillenserklaerungStatusListe().get(i);

            if (tFunktionen.reCheckKeineNeueWillenserklaerungen(eclDbM.getDbBundle(),
                    iEclZugeordneteMeldungNeu.getMeldungsIdent(),
                    iEclZugeordneteMeldungNeu.getIdentHoechsteWillenserklaerung()) == false) {
                 tSession.trageFehlerEin(CaFehler.afAndererUserAktiv);
            }

            CaBug.druckeLog("iEclZugeordneteMeldungNeu.artBeziehung=" + iEclZugeordneteMeldungNeu.artBeziehung,
                    logDrucken, 10);
            CaBug.druckeLog("iEclZugeordneteMeldungNeu.vollmachtsart=" + iEclZugeordneteMeldungNeu.vollmachtsart,
                    logDrucken, 10);

            BlWillenserklaerung willenserklaerung = new BlWillenserklaerung();
            willenserklaerung.pErteiltAufWeg = tWillenserklaerungSession.getEingabeQuelle();
            willenserklaerung.pErteiltZeitpunkt = tWillenserklaerungSession.getErteiltZeitpunkt();

            willenserklaerung.piMeldungsIdentAktionaer = iEclZugeordneteMeldungNeu.getMeldungsIdent(); //tWillenserklaerungSession.getMeldungsIdentListe().get(i);
            
            EclPersonenNatJur lPersonenNatJur = new EclPersonenNatJur();
            lPersonenNatJur.ident = iEclWillenserklaerungStatusNeu.getBevollmaechtigterDritterIdent();
            willenserklaerung.pEclPersonenNatJur = lPersonenNatJur;
            CaBug.druckeLog(
                    "willenserklaerung.pEclPersonenNatJur.ident=" + willenserklaerung.pEclPersonenNatJur.ident,
                    logDrucken, 10);

            if (iEclZugeordneteMeldungNeu.personNatJurIdent==0) {
                willenserklaerung.pWillenserklaerungGeberIdent = -1; /*Aktionär*/
            }
            else {
                willenserklaerung.pWillenserklaerungGeberIdent = iEclZugeordneteMeldungNeu.personNatJurIdent;
            }
            
            CaBug.druckeLog(
                    "willenserklaerung.pWillenserklaerungGeberIdent=" + willenserklaerung.pWillenserklaerungGeberIdent,
                    logDrucken, 10);
 

            willenserklaerung.widerrufVollmachtAnDritte(eclDbM.getDbBundle());

            /*Falls nicht möglich: Fehlermeldung, zum Anmelden*/
            if (willenserklaerung.rcIstZulaessig == false) {
                tSession.trageFehlerEin(willenserklaerung.rcGrundFuerUnzulaessig);
                return false;
            }
            if (pMitBestaetigung) {
                tQuittungen.bestaetigenVollmachtDritte(
                        willenserklaerung.rcWillenserklaerungIdent, 
                        iEclZugeordneteMeldungNeu.eclMeldung,  
                        lPersonenNatJur.ident, "", "", "",
                        true);
            }

        }

     
        
        return true;

    }
    
}
