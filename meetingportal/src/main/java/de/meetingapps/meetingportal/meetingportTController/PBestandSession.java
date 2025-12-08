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

import java.io.Serializable;
import java.util.List;

import de.meetingapps.meetingportal.meetComEntities.EclAktienregisterBestandsaenderungen;
import de.meetingapps.meetingportal.meetComEntities.EclAktienregisterWeiterePerson;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

@SessionScoped
@Named
public class PBestandSession implements Serializable {
    private static final long serialVersionUID = -4524063798890204019L;

    /**tagesdatum_abfrage, umformatiert für Anzeige*/
    private String standDatum="";
    
    /**geschaeftsanteile_mitglied, als Zahlenwert und formatiert*/
    private int geschaeftsanteile=0;
    private String geschaeftsanteileDE="";

	/**geschaeftsguthaben_nominal_mitglied*/
    private double geschaeftsguthabenNominal=0.0;
    private String geschaeftsguthabenNominalDE="";
 
    /**geschaefguthaben_aktuell_mitglied*/
    private double geschaeftsguthabenAktuell=0.0;
    private String geschaeftsguthabenAktuellDE="";
    
    /**auffuellbetrag_mitglied*/
    private double auffuellBetrag=0.0;
    private String auffuellBetragDE="";

    private boolean kuendigungVorhanden=false;
    private boolean kuendigungInBearbeitung=false;
    private boolean showKuendigungModal=false;
    private boolean showKuendigungErfolgModal=false;
    /**Enthält für ku178 die Kündigungen*/
    private List<EclAktienregisterBestandsaenderungen> aktienregisterBestandsaenderungen=null;
    
    /**true => hinterlegte Ansprechpartner oder Vollmachten oder Postempfänger sind vorhanden*/
    private boolean weiterePersonenVorhanden=false;
    
    private boolean hinterlegteAnsprechpartnerVorhanden=false;
    private List<EclAktienregisterWeiterePerson> hinterlegteAnprechpartner=null;
    
    private boolean vollmachtenVorhanden=false;
    private List<EclAktienregisterWeiterePerson> vollmachten=null;
    
    private boolean postempfaengerVorhanden=false;
   
    private List<EclAktienregisterWeiterePerson> postempfaenger=null;
    
    /**Fuer ku178 Kündigungsrücknahme*/
    private EclAktienregisterBestandsaenderungen gewaehlteBestandsaenderung = null;
    
    /*******************Standard getter und setter***************************/
    public String getStandDatum() {
        return standDatum;
    }
    public void setStandDatum(String standDatum) {
        this.standDatum = standDatum;
    }
    public int getGeschaeftsanteile() {
        return geschaeftsanteile;
    }
    public void setGeschaeftsanteile(int geschaeftsanteile) {
        this.geschaeftsanteile = geschaeftsanteile;
    }
    public String getGeschaeftsanteileDE() {
        return geschaeftsanteileDE;
    }
    public void setGeschaeftsanteileDE(String geschaeftsanteileDE) {
        this.geschaeftsanteileDE = geschaeftsanteileDE;
    }
    public double getGeschaeftsguthabenNominal() {
        return geschaeftsguthabenNominal;
    }
    public void setGeschaeftsguthabenNominal(double geschaeftsguthabenNominal) {
        this.geschaeftsguthabenNominal = geschaeftsguthabenNominal;
    }
    public String getGeschaeftsguthabenNominalDE() {
        return geschaeftsguthabenNominalDE;
    }
    public void setGeschaeftsguthabenNominalDE(String geschaeftsguthabenNominalDE) {
        this.geschaeftsguthabenNominalDE = geschaeftsguthabenNominalDE;
    }
    public double getGeschaeftsguthabenAktuell() {
        return geschaeftsguthabenAktuell;
    }
    public void setGeschaeftsguthabenAktuell(double geschaeftsguthabenAktuell) {
        this.geschaeftsguthabenAktuell = geschaeftsguthabenAktuell;
    }
    public String getGeschaeftsguthabenAktuellDE() {
        return geschaeftsguthabenAktuellDE;
    }
    public void setGeschaeftsguthabenAktuellDE(String geschaeftsguthabenAktuellDE) {
        this.geschaeftsguthabenAktuellDE = geschaeftsguthabenAktuellDE;
    }
    public double getAuffuellBetrag() {
        return auffuellBetrag;
    }
    public void setAuffuellBetrag(double auffuellBetrag) {
        this.auffuellBetrag = auffuellBetrag;
    }
    public String getAuffuellBetragDE() {
        return auffuellBetragDE;
    }
    public void setAuffuellBetragDE(String auffuellBetragDE) {
        this.auffuellBetragDE = auffuellBetragDE;
    }
    public boolean isKuendigungVorhanden() {
        return kuendigungVorhanden;
    }
    public void setKuendigungVorhanden(boolean kuendigungVorhanden) {
        this.kuendigungVorhanden = kuendigungVorhanden;
    }
    public List<EclAktienregisterBestandsaenderungen> getAktienregisterBestandsaenderungen() {
        return aktienregisterBestandsaenderungen;
    }
    public void setAktienregisterBestandsaenderungen(List<EclAktienregisterBestandsaenderungen> aktienregisterBestandsaenderungen) {
        this.aktienregisterBestandsaenderungen = aktienregisterBestandsaenderungen;
    }
    public boolean isWeiterePersonenVorhanden() {
        return weiterePersonenVorhanden;
    }
    public void setWeiterePersonenVorhanden(boolean weiterePersonenVorhanden) {
        this.weiterePersonenVorhanden = weiterePersonenVorhanden;
    }
    public boolean isHinterlegteAnsprechpartnerVorhanden() {
        return hinterlegteAnsprechpartnerVorhanden;
    }
    public void setHinterlegteAnsprechpartnerVorhanden(boolean hinterlegteAnsprechpartnerVorhanden) {
        this.hinterlegteAnsprechpartnerVorhanden = hinterlegteAnsprechpartnerVorhanden;
    }
    public List<EclAktienregisterWeiterePerson> getHinterlegteAnprechpartner() {
        return hinterlegteAnprechpartner;
    }
    public void setHinterlegteAnprechpartner(List<EclAktienregisterWeiterePerson> hinterlegteAnprechpartner) {
        this.hinterlegteAnprechpartner = hinterlegteAnprechpartner;
    }
    public boolean isVollmachtenVorhanden() {
        return vollmachtenVorhanden;
    }
    public void setVollmachtenVorhanden(boolean vollmachtenVorhanden) {
        this.vollmachtenVorhanden = vollmachtenVorhanden;
    }
    public List<EclAktienregisterWeiterePerson> getVollmachten() {
        return vollmachten;
    }
    public void setVollmachten(List<EclAktienregisterWeiterePerson> vollmachten) {
        this.vollmachten = vollmachten;
    }
    public boolean isPostempfaengerVorhanden() {
        return postempfaengerVorhanden;
    }
    public void setPostempfaengerVorhanden(boolean postempfaengerVorhanden) {
        this.postempfaengerVorhanden = postempfaengerVorhanden;
    }
    public List<EclAktienregisterWeiterePerson> getPostempfaenger() {
        return postempfaenger;
    }
    public void setPostempfaenger(List<EclAktienregisterWeiterePerson> postempfaenger) {
        this.postempfaenger = postempfaenger;
    }
    public EclAktienregisterBestandsaenderungen getGewaehlteBestandsaenderung() {
        return gewaehlteBestandsaenderung;
    }
    public void setGewaehlteBestandsaenderung(EclAktienregisterBestandsaenderungen gewaehlteBestandsaenderung) {
        this.gewaehlteBestandsaenderung = gewaehlteBestandsaenderung;
    }
    public boolean isKuendigungInBearbeitung() {
        return kuendigungInBearbeitung;
    }
    public void setKuendigungInBearbeitung(boolean kuendigungInBearbeitung) {
        this.kuendigungInBearbeitung = kuendigungInBearbeitung;
    }
    public boolean isShowKuendigungModal() {
        return showKuendigungModal;
    }
    public void setShowKuendigungModal(boolean showKuendigungModal) {
        this.showKuendigungModal = showKuendigungModal;
    }
    public boolean isShowKuendigungErfolgModal() {
        return showKuendigungErfolgModal;
    }
    public void setShowKuendigungErfolgModal(boolean showKuendigungErfolgModal) {
        this.showKuendigungErfolgModal = showKuendigungErfolgModal;
    }
	
}
