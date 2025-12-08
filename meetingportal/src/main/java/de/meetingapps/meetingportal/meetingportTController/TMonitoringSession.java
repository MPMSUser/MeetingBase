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

import de.meetingapps.meetingportal.meetComEh.EhIdStringFuerAuswahl;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

@SessionScoped
@Named
public class TMonitoringSession implements Serializable {
    private static final long serialVersionUID = -1345734918547603747L;

    /**Zeitpunkt, zu dem die Monitoring-Seite aufgerufen wurde*/
    private String datumZeit = "";

    private boolean sammelkarteSRVVorhanden=false;
    private boolean sammelkarteBriefwahlVorhanden=false;
    
    private String mail1Fragen="";
    private String mail2Fragen="";
    private String mail3Fragen="";
 
    private String mail1Wortmeldungen="";
    private String mail2Wortmeldungen="";
    private String mail3Wortmeldungen="";

    private String mail1Widersprueche="";
    private String mail2Widersprueche="";
    private String mail3Widersprueche="";

    private String mail1Antraege="";
    private String mail2Antraege="";
    private String mail3Antraege="";

    private String mail1SonstigeMitteilungen="";
    private String mail2SonstigeMitteilungen="";
    private String mail3SonstigeMitteilungen="";

    private String mail1Botschaften="";
    private String mail2Botschaften="";
    private String mail3Botschaften="";
    
    private boolean anzeigenAbstimmungen=false;
    
    private boolean auswahlSRVBriefwahlMoeglich=false;
    private String anzeigenSRVBriefwahl="1";
    
    private boolean auswahlGattungMoeglich=false;
    private String ausgewaehlteGattung="";
    private List<EhIdStringFuerAuswahl> auswahlGattungen=null;
    
    /**true => Preview-Sicht wird angezeigt*/
    private boolean anzeigenPreview=false;
    
    private String abstimmungUeberschrift="";
    
    /*************Standard-Getter und Setter*************************/

    public String getDatumZeit() {
        return datumZeit;
    }

    public void setDatumZeit(String datumZeit) {
        this.datumZeit = datumZeit;
    }

    public String getMail1Fragen() {
        return mail1Fragen;
    }

    public void setMail1Fragen(String mail1Fragen) {
        this.mail1Fragen = mail1Fragen;
    }

    public String getMail2Fragen() {
        return mail2Fragen;
    }

    public void setMail2Fragen(String mail2Fragen) {
        this.mail2Fragen = mail2Fragen;
    }

    public String getMail3Fragen() {
        return mail3Fragen;
    }

    public void setMail3Fragen(String mail3Fragen) {
        this.mail3Fragen = mail3Fragen;
    }

    public String getMail1Wortmeldungen() {
        return mail1Wortmeldungen;
    }

    public void setMail1Wortmeldungen(String mail1Wortmeldungen) {
        this.mail1Wortmeldungen = mail1Wortmeldungen;
    }

    public String getMail2Wortmeldungen() {
        return mail2Wortmeldungen;
    }

    public void setMail2Wortmeldungen(String mail2Wortmeldungen) {
        this.mail2Wortmeldungen = mail2Wortmeldungen;
    }

    public String getMail3Wortmeldungen() {
        return mail3Wortmeldungen;
    }

    public void setMail3Wortmeldungen(String mail3Wortmeldungen) {
        this.mail3Wortmeldungen = mail3Wortmeldungen;
    }

    public String getMail1Widersprueche() {
        return mail1Widersprueche;
    }

    public void setMail1Widersprueche(String mail1Widersprueche) {
        this.mail1Widersprueche = mail1Widersprueche;
    }

    public String getMail2Widersprueche() {
        return mail2Widersprueche;
    }

    public void setMail2Widersprueche(String mail2Widersprueche) {
        this.mail2Widersprueche = mail2Widersprueche;
    }

    public String getMail3Widersprueche() {
        return mail3Widersprueche;
    }

    public void setMail3Widersprueche(String mail3Widersprueche) {
        this.mail3Widersprueche = mail3Widersprueche;
    }

    public String getMail1Antraege() {
        return mail1Antraege;
    }

    public void setMail1Antraege(String mail1Antraege) {
        this.mail1Antraege = mail1Antraege;
    }

    public String getMail2Antraege() {
        return mail2Antraege;
    }

    public void setMail2Antraege(String mail2Antraege) {
        this.mail2Antraege = mail2Antraege;
    }

    public String getMail3Antraege() {
        return mail3Antraege;
    }

    public void setMail3Antraege(String mail3Antraege) {
        this.mail3Antraege = mail3Antraege;
    }

    public String getMail1SonstigeMitteilungen() {
        return mail1SonstigeMitteilungen;
    }

    public void setMail1SonstigeMitteilungen(String mail1SonstigeMitteilungen) {
        this.mail1SonstigeMitteilungen = mail1SonstigeMitteilungen;
    }

    public String getMail2SonstigeMitteilungen() {
        return mail2SonstigeMitteilungen;
    }

    public void setMail2SonstigeMitteilungen(String mail2SonstigeMitteilungen) {
        this.mail2SonstigeMitteilungen = mail2SonstigeMitteilungen;
    }

    public String getMail3SonstigeMitteilungen() {
        return mail3SonstigeMitteilungen;
    }

    public void setMail3SonstigeMitteilungen(String mail3SonstigeMitteilungen) {
        this.mail3SonstigeMitteilungen = mail3SonstigeMitteilungen;
    }

    public boolean isSammelkarteSRVVorhanden() {
        return sammelkarteSRVVorhanden;
    }

    public void setSammelkarteSRVVorhanden(boolean sammelkarteSRVVorhanden) {
        this.sammelkarteSRVVorhanden = sammelkarteSRVVorhanden;
    }

    public boolean isSammelkarteBriefwahlVorhanden() {
        return sammelkarteBriefwahlVorhanden;
    }

    public void setSammelkarteBriefwahlVorhanden(boolean sammelkarteBriefwahlVorhanden) {
        this.sammelkarteBriefwahlVorhanden = sammelkarteBriefwahlVorhanden;
    }

    public boolean isAnzeigenAbstimmungen() {
        return anzeigenAbstimmungen;
    }

    public void setAnzeigenAbstimmungen(boolean anzeigenAbstimmungen) {
        this.anzeigenAbstimmungen = anzeigenAbstimmungen;
    }

    public boolean isAuswahlSRVBriefwahlMoeglich() {
        return auswahlSRVBriefwahlMoeglich;
    }

    public void setAuswahlSRVBriefwahlMoeglich(boolean auswahlSRVBriefwahlMoeglich) {
        this.auswahlSRVBriefwahlMoeglich = auswahlSRVBriefwahlMoeglich;
    }

    public String getAnzeigenSRVBriefwahl() {
        return anzeigenSRVBriefwahl;
    }

    public void setAnzeigenSRVBriefwahl(String anzeigenSRVBriefwahl) {
        this.anzeigenSRVBriefwahl = anzeigenSRVBriefwahl;
    }

    public boolean isAuswahlGattungMoeglich() {
        return auswahlGattungMoeglich;
    }

    public void setAuswahlGattungMoeglich(boolean auswahlGattungMoeglich) {
        this.auswahlGattungMoeglich = auswahlGattungMoeglich;
    }

    public String getAusgewaehlteGattung() {
        return ausgewaehlteGattung;
    }

    public void setAusgewaehlteGattung(String ausgewaehlteGattung) {
        this.ausgewaehlteGattung = ausgewaehlteGattung;
    }

    public List<EhIdStringFuerAuswahl> getAuswahlGattungen() {
        return auswahlGattungen;
    }

    public void setAuswahlGattungen(List<EhIdStringFuerAuswahl> auswahlGattungen) {
        this.auswahlGattungen = auswahlGattungen;
    }

    public boolean isAnzeigenPreview() {
        return anzeigenPreview;
    }

    public void setAnzeigenPreview(boolean anzeigenPreview) {
        this.anzeigenPreview = anzeigenPreview;
    }

    public String getAbstimmungUeberschrift() {
        return abstimmungUeberschrift;
    }

    public void setAbstimmungUeberschrift(String abstimmungUeberschrift) {
        this.abstimmungUeberschrift = abstimmungUeberschrift;
    }

    public String getMail1Botschaften() {
        return mail1Botschaften;
    }

    public void setMail1Botschaften(String mail1Botschaften) {
        this.mail1Botschaften = mail1Botschaften;
    }

    public String getMail2Botschaften() {
        return mail2Botschaften;
    }

    public void setMail2Botschaften(String mail2Botschaften) {
        this.mail2Botschaften = mail2Botschaften;
    }

    public String getMail3Botschaften() {
        return mail3Botschaften;
    }

    public void setMail3Botschaften(String mail3Botschaften) {
        this.mail3Botschaften = mail3Botschaften;
    }

}
