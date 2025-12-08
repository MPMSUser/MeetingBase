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
package de.meetingapps.meetingportal.meetingportTFunktionen;

import java.util.LinkedList;
import java.util.List;

import de.meetingapps.meetingportal.meetComEclM.EclBesitzGesamtM;
import de.meetingapps.meetingportal.meetComEntities.EclBesitzAREintrag;
import de.meetingapps.meetingportal.meetComEntities.EclBesitzJeKennung;
import de.meetingapps.meetingportal.meetComEntities.EclZugeordneteMeldungNeu;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@RequestScoped
@Named
public class TBestandDurcharbeiten {
    private @Inject EclBesitzGesamtM eclBesitzGesamtM;
    
    private List<String> rcAktienregisternummer =null;
    private List<Integer> rcMeldungsIdent =null;
   
    /*Siehe nachfolgenden Konstanten*/
    private int funktion=0;
    
    /*1=erzeugenARNummerMeldung*/
    private final int ERZEUGEN_ARNUMMER_MELDUNG=1;
    
    /**Füllt die Listen rcAktienregisternummer und - falls nicht AR-Bestand zugeordnet -l rcMeldungsIdent*/
    public void erzeugenARNummerMeldung() {
        rcAktienregisternummer=new LinkedList<String>();
        rcMeldungsIdent=new LinkedList<Integer>();
        funktion=ERZEUGEN_ARNUMMER_MELDUNG;
        durchlaufen();
    }
    
    private void durchlaufen() {
            if (eclBesitzGesamtM.getBesitzJeKennungListe()!=null) {
                for (EclBesitzJeKennung iEclBesitzJeKennung: eclBesitzGesamtM.getBesitzJeKennungListe()) {
                    if (iEclBesitzJeKennung.eigenerAREintragListe!=null) {
                        /*Eigene Aktien*/
                        for (EclBesitzAREintrag iEclBesitzAREintrag: iEclBesitzJeKennung.eigenerAREintragListe) {
                            verarbeiteEclBesitzAREintrag(iEclBesitzAREintrag);
                        }
                    }
                    
                    if (iEclBesitzJeKennung.zugeordneteMeldungenEigeneGastkartenListe!=null) {
                        /*Ausgestellte Gastkarten*/
//                        for (EclZugeordneteMeldungNeu iEclZugeordneteMeldungNeu: iEclBesitzJeKennung.zugeordneteMeldungenEigeneGastkartenListe) {
////                            verarbeiteEclBesitzAREintrag(iEclBesitzAREintrag);
//                        }
                    }
                    
                    if (iEclBesitzJeKennung.zugeordneteMeldungenBevollmaechtigtListe!=null) {
                        /*Erhaltene Vollmachten*/
                        for (EclZugeordneteMeldungNeu iEclZugeordneteMeldungNeu: iEclBesitzJeKennung.zugeordneteMeldungenBevollmaechtigtListe) {
                            verarbeiteEclZugeordneteMeldungNeu(iEclZugeordneteMeldungNeu);
                        }
                    }
 
                    if (iEclBesitzJeKennung.zugeordneteMeldungenBevollmaechtigtMitGesetzlichListe!=null) {
                        /*Erhaltene Gesetzliche Vollmachten*/
//                        for (EclZugeordneteMeldungNeu iEclZugeordneteMeldungNeu: iEclBesitzJeKennung.zugeordneteMeldungenBevollmaechtigtMitGesetzlichListe) {
////                            verarbeiteEclBesitzAREintrag(iEclBesitzAREintrag);
//                        }
                    }

                    if (iEclBesitzJeKennung.instiAREintraegeListe!=null) {
                        /*Insti-AR-Einträge*/
                        for (EclBesitzAREintrag iEclBesitzAREintrag: iEclBesitzJeKennung.instiAREintraegeListe) {
                            verarbeiteEclBesitzAREintrag(iEclBesitzAREintrag);
                        }
                    }

                    if (iEclBesitzJeKennung.zugeordneteMeldungenInstiListe!=null) {
                        /*Insti-zugeordnete Meldungen*/
                        for (EclZugeordneteMeldungNeu iEclZugeordneteMeldungNeu: iEclBesitzJeKennung.zugeordneteMeldungenInstiListe) {
                            verarbeiteEclZugeordneteMeldungNeu(iEclZugeordneteMeldungNeu);
                        }
                    }

                }
            }
    }
    
    private void verarbeiteEclBesitzAREintrag(EclBesitzAREintrag iEclBesitzAREintrag) {
        switch (funktion) {
        case ERZEUGEN_ARNUMMER_MELDUNG:
            rcAktienregisternummer.add(iEclBesitzAREintrag.aktienregisterEintrag.aktionaersnummer);
            rcMeldungsIdent.add(0);
            break;
        }
    }

    private void verarbeiteEclZugeordneteMeldungNeu(EclZugeordneteMeldungNeu iEclZugeordneteMeldungNeu) {
        switch (funktion) {
        case ERZEUGEN_ARNUMMER_MELDUNG:
            rcAktienregisternummer.add(iEclZugeordneteMeldungNeu.eclMeldung.aktionaersnummer);
            rcMeldungsIdent.add(iEclZugeordneteMeldungNeu.meldungsIdent);
            break;
        }
    }

    public List<String> getRcAktienregisternummer() {
        return rcAktienregisternummer;
    }

    public void setRcAktienregisternummer(List<String> rcAktienregisternummer) {
        this.rcAktienregisternummer = rcAktienregisternummer;
    }

    public List<Integer> getRcMeldungsIdent() {
        return rcMeldungsIdent;
    }

    public void setRcMeldungsIdent(List<Integer> rcMeldungsIdent) {
        this.rcMeldungsIdent = rcMeldungsIdent;
    }

}
