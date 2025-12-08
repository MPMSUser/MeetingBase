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
package de.meetingapps.meetingportal.meetComBrM;

import java.util.List;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComBr.BrLiefereMitgliedArt;
import de.meetingapps.meetingportal.meetComEclM.EclDbM;
import de.meetingapps.meetingportal.meetComEclM.EclLoginDatenM;
import de.meetingapps.meetingportal.meetComEntities.EclAktienregisterWeiterePerson;
import de.meetingapps.meetingportal.meetComEntities.EclLoginDaten;
import de.meetingapps.meetingportal.meetComKonst.KonstGruppen;
import de.meetingapps.meetingportal.meetingportTController.PAktionaersdatenSession;
import de.meetingapps.meetingportal.meetingportTController.PHVEinladungsversandSession;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@RequestScoped
@Named
public class BrMHVEinladungsversand {
    
//    private int logDrucken = 10;

    private @Inject EclDbM eclDbM;
    private @Inject PAktionaersdatenSession pAktionaersdatenSession;
    private @Inject PHVEinladungsversandSession pHVEinladungenSession;
    private @Inject EclLoginDatenM eclLoginDatenM;


    /**pAktionaersnummer muß im Format "für intern" sein*/
    public int holeAktuelleEinstellungen(String pAktionaersnummer) {
        
        
        eclDbM.getDbBundle().dbLoginDaten.read_loginKennung(pAktionaersnummer);
        if ( eclDbM.getDbBundle().dbLoginDaten.anzErgebnis()<1) {
            CaBug.drucke("001");
            return -1;
        }
        EclLoginDaten lLoginDaten=eclDbM.getDbBundle().dbLoginDaten.ergebnisPosition(0);
        
        String vorname = pAktionaersdatenSession.getAktionaerVorname();
        String nachname = pAktionaersdatenSession.getAktionaerNachname();
        List<EclAktienregisterWeiterePerson> zugeordneteWeiterPersonen = pAktionaersdatenSession
                .getZugeordneteWeiterPersonen();

        BrLiefereMitgliedArt brLiefereMitgliedArt=new BrLiefereMitgliedArt();
        int personenArt=brLiefereMitgliedArt.liefereMitgliedstatus(vorname, nachname, zugeordneteWeiterPersonen);
        /*
         * Gesamthandsgemeinschaften: Es ist kein Vorname angegeben. Das Wort
         * „Gesamthandsgemeinschaft“ befindet sich im Nachnamen
         */
        boolean einladungsversandZulaessig=true;
        switch (personenArt) {
        case KonstGruppen.eheleuteGesamthans:einladungsversandZulaessig=false;break;
        case KonstGruppen.erbengemeinschaft:einladungsversandZulaessig=false;break;
        case KonstGruppen.firmen:einladungsversandZulaessig=false;break;
        case KonstGruppen.minderjaehrigesEinzelmitglied:einladungsversandZulaessig=false;break;
       }

        pHVEinladungenSession.setRegistrierungZulaessig(einladungsversandZulaessig);
        pHVEinladungenSession.setMailAdresse(lLoginDaten.eMailFuerVersand);
        if (lLoginDaten.eVersandRegistriert()) {
            pHVEinladungenSession.setAuswahl("2");
            pHVEinladungenSession.setAuswahlAlt("2");
        }
        else {
            pHVEinladungenSession.setAuswahl("1");
            pHVEinladungenSession.setAuswahlAlt("1");
        }
        
        return 1;
    }
    
    /*
     * Return 
     * ==1 => ok, Post wurde gespeichert
     * ==2 => ok, Mail wurde gespeichert
     * 
     * ==3 => ok, aber keine Änderungen, d.h. nichts gespeichert
      * 
     * ==0 => Fehlermeldung wurde erzeugt, Kein Speichern erfolgt, Änderungsfenster
     * muß angezeigt bleiben
     */
   public int speichern(String pAktionaersnummer) {
       /*****************
        * Checken, ob überhaupt Änderung vorhanden
        **********************/
        if (pHVEinladungenSession.getAuswahlAlt().equals(pHVEinladungenSession.getAuswahl())) {
            return 3;
        }
        
        eclDbM.getDbBundle().dbLoginDaten.read_loginKennung(pAktionaersnummer);
        if ( eclDbM.getDbBundle().dbLoginDaten.anzErgebnis()<1) {
            CaBug.drucke("001");
            return -1;
        }
        EclLoginDaten lLoginDaten=eclDbM.getDbBundle().dbLoginDaten.ergebnisPosition(0);
        if (pHVEinladungenSession.getAuswahl().equals("1")) {
            /*Setzen auf Post*/
            lLoginDaten.eVersandRegistrierung=1;
        }
        else {
            /*Setzen auf Mail*/
            lLoginDaten.eVersandRegistrierung=99;
        }
        eclDbM.getDbBundle().dbLoginDaten.update(lLoginDaten);
        eclLoginDatenM.setEclLoginDaten(lLoginDaten);

        
        if (pHVEinladungenSession.getAuswahl().equals("1")) {
            return 1;
        }
        else {
            return 2;
        }
    }
    
}
