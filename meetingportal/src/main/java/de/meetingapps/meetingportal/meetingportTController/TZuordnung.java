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
import de.meetingapps.meetingportal.meetComBl.BlTeilnehmerLoginNeu;
import de.meetingapps.meetingportal.meetComEclM.EclDbM;
import de.meetingapps.meetingportal.meetComEclM.EclLoginDatenM;
import de.meetingapps.meetingportal.meetComEntities.EclBesitzJeKennung;
import de.meetingapps.meetingportal.meetComEntities.EclZugeordneteKennung;
import de.meetingapps.meetingportal.meetComKonst.KonstLoginKennungArt;
import de.meetingapps.meetingportal.meetComKonst.KonstPortalView;
import de.meetingapps.meetingportal.meetingportTFunktionen.TPruefeStartNachOpen;
import de.meetingapps.meetingportal.meetingportTFunktionen.TSessionVerwaltung;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@RequestScoped
@Named
public class TZuordnung {

    private @Inject EclDbM eclDbM;
 
    private @Inject TZuordnungSession tZuordnungSession;
    private @Inject TSessionVerwaltung tSessionVerwaltung;
    private @Inject TSession tSession;
    private @Inject EclLoginDatenM eclLoginDatenM;
    private @Inject TPruefeStartNachOpen tPruefeStartNachOpen;
    private @Inject TAuswahl tAuswahl;

    /********************************Zuordnung aufheben*************************/
    public int initZuordnungAufheben(EclBesitzJeKennung pBesitzJeKennung) {
        
        tZuordnungSession.setBesitzJeKennung(pBesitzJeKennung);
        tZuordnungSession.setKennung(pBesitzJeKennung.kennungFuerAnzeige);
        if (pBesitzJeKennung.kennungArt==KonstLoginKennungArt.aktienregister) {
            tZuordnungSession.setName(pBesitzJeKennung.eclAktienregister.liefereTitelVornameName());
            tZuordnungSession.setOrt(pBesitzJeKennung.eclAktienregister.ort);
        }
        else { //personNatJur
            tZuordnungSession.setName(pBesitzJeKennung.eclPersonenNatJur.liefereTitelVornameName());
            tZuordnungSession.setOrt(pBesitzJeKennung.eclPersonenNatJur.ort);
        }
        
        return 1;
    }
    
    public int doZurueckZuordnungAufheben() {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.ZUORDNUNG_AUFHEBEN)) {
            return 1;
        }

        tSessionVerwaltung.setzeEnde(KonstPortalView.AUSWAHL);
        return 1;

    }

    public int doAusfuehrenZuordnungAufheben() {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.ZUORDNUNG_AUFHEBEN)) {
            return 1;
        }

        eclDbM.openAll();
        
        if (!tPruefeStartNachOpen.pruefeStartNachOpen()) {
            tSessionVerwaltung.setzeEnde();
            eclDbM.closeAll();
            return 0;
        }

        EclZugeordneteKennung zuLoeschendeZuordnung=new EclZugeordneteKennung();
        zuLoeschendeZuordnung.loginKennung=eclLoginDatenM.getEclLoginDaten().loginKennung;
        zuLoeschendeZuordnung.zugeordneteKennung=tZuordnungSession.getBesitzJeKennung().kennungIntern;
        eclDbM.getDbBundle().dbZuordnungKennung.delete(zuLoeschendeZuordnung);
        
        tAuswahl.startAuswahl(true);
        
        eclDbM.closeAll();
        
        
        tSessionVerwaltung.setzeEnde(KonstPortalView.AUSWAHL);
        
        return 1;
    }
    
    /***************************************Zuordnung einrichten***************/
    public int initZuordnungEinrichten() {
        tZuordnungSession.setKennung("");
        tZuordnungSession.setPasswort("");
        return 1;
    }
    
    public int doZurueckZuordnungEinrichten() {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.ZUORDNUNG_EINRICHTEN)) {
            return 1;
        }

        tSessionVerwaltung.setzeEnde(KonstPortalView.AUSWAHL);
        return 1;

    }

    public int doAusfuehrenZuordnungEinrichten() {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.ZUORDNUNG_EINRICHTEN)) {
            return 1;
        }

        eclDbM.openAll();
        
        if (!tPruefeStartNachOpen.pruefeStartNachOpen()) {
            tSessionVerwaltung.setzeEnde();
            eclDbM.closeAll();
            return 0;
        }

        String lKennung = tZuordnungSession.getKennung();
        String lPasswort = tZuordnungSession.getPasswort();
        
        BlTeilnehmerLoginNeu blTeilnehmerLogin = new BlTeilnehmerLoginNeu(tSession.isPermanentPortal());
        blTeilnehmerLogin.initDB(eclDbM.getDbBundle());
        int erg = blTeilnehmerLogin.findeUndPruefeKennung(lKennung, lPasswort, true);

        if (erg < 0) {
            /*Fehler aufgetreten*/
            tSessionVerwaltung.setzeEnde();
            eclDbM.closeAll();
            tSession.trageFehlerEin(erg);
            return 0;
        }

        String lLoginKennung=eclLoginDatenM.getEclLoginDaten().loginKennung;
        String lKennungIntern=blTeilnehmerLogin.eclLoginDaten.loginKennung;
        String lKennungFuerAnzeige=blTeilnehmerLogin.anmeldeKennungFuerAnzeige;
        int lKennungArt=blTeilnehmerLogin.eclLoginDaten.kennungArt;
        
        tZuordnungSession.setKennung(lKennungFuerAnzeige);
        if (lKennungArt==KonstLoginKennungArt.aktienregister) {
            tZuordnungSession.setName(blTeilnehmerLogin.eclAktienregister.liefereTitelVornameName());
            tZuordnungSession.setOrt(blTeilnehmerLogin.eclAktienregister.ort);
        }
        else { //personNatJur
            tZuordnungSession.setName(blTeilnehmerLogin.eclPersonenNatJur.liefereTitelVornameName());
            tZuordnungSession.setOrt(blTeilnehmerLogin.eclPersonenNatJur.ort);
        }

        if (lKennungIntern.equals(lLoginKennung)) {
            /*Eigene Kennung kann nicht zugeordnet werden*/
            tSessionVerwaltung.setzeEnde();
            eclDbM.closeAll();
            tSession.trageFehlerEin(CaFehler.afEigeneKennungNichtZuordenbar);
            return 0;
            
        }
        
        /*TODO Zuordnung Kennung: Nebenläufigkeit nicht gegeben!*/
        int anzVorhanden=eclDbM.getDbBundle().dbZuordnungKennung.read_zugeordneteKennung(lKennungIntern);
        if (anzVorhanden>0) {
            /*Bereits eine Zuordnung vorhanden*/
            tSessionVerwaltung.setzeEnde();
            eclDbM.closeAll();
            tSession.trageFehlerEin(CaFehler.afKennungBereitsZugeordnet);
            return 0;
        }

        /*TODO Zuordnung Kennung: Weitere Entwicklung erforderlich, z.B.
         * Wenn Zugeschaltet, dann auch zugeordnete Kennungen zugeschaltet.
         * Wenn die zuzuordnende Kennung eingeloggt, dann ausloggen.
         * (Umgekehrt: beim Einloggen eine Zuordnung zu einer anderen Kennung entfernen)
         * Insti-Verbindungen
         */
                
        EclZugeordneteKennung neueZuordnung=new EclZugeordneteKennung();
        neueZuordnung.loginKennung=lLoginKennung;
        neueZuordnung.zugeordneteKennung=lKennungIntern;
        neueZuordnung.kennungArt=lKennungArt;
        neueZuordnung.zuordnungIstFuerPraesenzVerifiziert=1;
        eclDbM.getDbBundle().dbZuordnungKennung.insert(neueZuordnung);
        
        tAuswahl.startAuswahl(true);

        eclDbM.closeAll();
       
        tSessionVerwaltung.setzeEnde(KonstPortalView.ZUORDNUNG_EINRICHTEN_QUITTUNG);

        
        return 1;
    }

    
    
    /***************************************Zuordnung einrichten Quittung***************/

    public int doAusfuehrenZuordnungEinrichtenQuittung() {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.ZUORDNUNG_EINRICHTEN_QUITTUNG)) {
            return 1;
        }

        tSessionVerwaltung.setzeEnde(KonstPortalView.AUSWAHL);
        return 1;
    }

    
}
