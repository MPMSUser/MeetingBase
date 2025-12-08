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

import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComBl.BlWillenserklaerung;
import de.meetingapps.meetingportal.meetComEclM.EclDbM;
import de.meetingapps.meetingportal.meetComEntities.EclPersonenNatJur;
import de.meetingapps.meetingportal.meetComEntities.EclZugeordneteMeldungNeu;
import de.meetingapps.meetingportal.meetComKonst.KonstZugeordneteMeldungArtBeziehung;
import de.meetingapps.meetingportal.meetingportTFunktionen.TQuittungen;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@RequestScoped
@Named
/**Funktion ist noch nicht für Portal aufbereitet, sondern nur für WAIntern / Bestandsverwaltung*/
public class TVollmachtDritte {

    private @Inject TSession tSession;
    private @Inject TWillenserklaerungSession tWillenserklaerungSession;
    private @Inject EclDbM eclDbM;
    private @Inject TQuittungen tQuittungen;
  
    public boolean ausstellen(boolean pMitBestaetigung) {
        /*Prüfen, ob Vollmacht vollständig*/
        if (tWillenserklaerungSession.getVollmachtName().isEmpty()) {
            tSession.trageFehlerEin(CaFehler.afVollmachtNameEKFehlt);
            return false;
        }
        if (tWillenserklaerungSession.getVollmachtOrt().isEmpty()) {
            tSession.trageFehlerEin(CaFehler.afVollmachtOrtEKFehlt);
            return false;
        }

 
        for (int i = 0; i < tWillenserklaerungSession.getZugeordneteMeldungFuerAusfuehrungListe().size(); i++) {
            EclZugeordneteMeldungNeu iEclZugeordneteMeldungNeu = tWillenserklaerungSession
                    .getZugeordneteMeldungFuerAusfuehrungListe().get(i);

 
            BlWillenserklaerung vmWillenserklaerung = new BlWillenserklaerung();
            vmWillenserklaerung.pErteiltAufWeg = tWillenserklaerungSession.getEingabeQuelle();
            vmWillenserklaerung.pErteiltZeitpunkt = tWillenserklaerungSession.getErteiltZeitpunkt();

            vmWillenserklaerung.piMeldungsIdentAktionaer = iEclZugeordneteMeldungNeu.getMeldungsIdent();

            if (iEclZugeordneteMeldungNeu
                    .getArtBeziehung() == KonstZugeordneteMeldungArtBeziehung.EIGENE_AKTIEN_AUS_AKTIENREGISTER
                    || iEclZugeordneteMeldungNeu
                            .getArtBeziehung() == KonstZugeordneteMeldungArtBeziehung.INSTI_AKTIENREGISTER
                    || iEclZugeordneteMeldungNeu.getArtBeziehung() == KonstZugeordneteMeldungArtBeziehung.INSTI_MELDUNG) {
                vmWillenserklaerung.pWillenserklaerungGeberIdent = -1; /*Aktionär*/
            } else { /*Hier: es handelt sich um eine Untervollmacht. D.h. Geber = angemeldete personNatJur.*/
                vmWillenserklaerung.pWillenserklaerungGeberIdent = iEclZugeordneteMeldungNeu.personNatJurIdent;
            }


            EclPersonenNatJur personNatJur = new EclPersonenNatJur();

            personNatJur.ident = 0; /*Neue Person*/
            personNatJur.vorname = tWillenserklaerungSession.getVollmachtVorname();
            personNatJur.name = tWillenserklaerungSession.getVollmachtName();
            personNatJur.ort = tWillenserklaerungSession.getVollmachtOrt();
            vmWillenserklaerung.pEclPersonenNatJur = personNatJur;

            /*Hinweis: die Ausstellung von Vollmachten an Dritte ist immer nur als zusätzliche Aktion - wenn bereits vorher eine
             * Anmeldung erfolgte - möglich. D.h. immer ausgewaehlteHauptaktion=2
             */

            vmWillenserklaerung.vollmachtAnDritte(eclDbM.getDbBundle());
            if (vmWillenserklaerung.rcIstZulaessig == false) {
                tSession.trageFehlerEin(vmWillenserklaerung.rcGrundFuerUnzulaessig);
                return false;
            }
            
            if (pMitBestaetigung) {
                tQuittungen.bestaetigenVollmachtDritte(
                        vmWillenserklaerung.rcWillenserklaerungIdent, 
                        iEclZugeordneteMeldungNeu.eclMeldung,  
                        0, personNatJur.vorname, personNatJur.name, personNatJur.ort,
                        false);
            }


        }
       return true;
    }
}
