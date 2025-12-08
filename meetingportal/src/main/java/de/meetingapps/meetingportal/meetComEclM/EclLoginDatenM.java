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
package de.meetingapps.meetingportal.meetComEclM;

import java.io.Serializable;
import java.util.List;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComBl.BlTeilnehmerLoginNeu;
import de.meetingapps.meetingportal.meetComEntities.EclAktienregister;
import de.meetingapps.meetingportal.meetComEntities.EclAktienregisterErgaenzung;
import de.meetingapps.meetingportal.meetComEntities.EclLoginDaten;
import de.meetingapps.meetingportal.meetComEntities.EclPersonenNatJur;
import de.meetingapps.meetingportal.meetComEntities.EclTLoginDatenM;
import de.meetingapps.meetingportal.meetComKonst.KonstLoginKennungArt;
import de.meetingapps.meetingportal.meetComKonst.KonstPortalTexte;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

/**Enthält - für die JSF-Oberfläche bzw. für die App-aufbereitet - die
 * Daten aus BlTeilnehmerLogin
 */
@SessionScoped
@Named
public class EclLoginDatenM implements Serializable {
    private static final long serialVersionUID = 840607627274652477L;

    private int logDrucken = 3;

    /**Immer gefüllt - gemäß eingegebener Login-Kennung*/
    private EclLoginDaten eclLoginDaten = new EclLoginDaten();

    /**Gefüllt, je nachdem welche Kennungart verwendet wurde*/
    private EclAktienregister eclAktienregister = new EclAktienregister();
    private EclPersonenNatJur eclPersonenNatJur = new EclPersonenNatJur();
    private EclAktienregisterErgaenzung eclAktienregisterErgaenzung = new EclAktienregisterErgaenzung();

    private String anmeldeKennungFuerAnzeige = "";

    /**Wird nur für ku310-Mandant benötigt*/
    public String liefereAnmeldeKennungFuerAnzeigeku310() {
        if (anmeldeKennungFuerAnzeige.length()<8) {return anmeldeKennungFuerAnzeige;}
        String vorne=anmeldeKennungFuerAnzeige.substring(0,7);
        String hinten=anmeldeKennungFuerAnzeige.substring(7);
        return vorne+"-"+hinten;
    }

    
    private String titelVornameName = "";
    private String ort = "";

    private String titel = "";
    private String name = "";
    private String vorname = "";
    private String strasse = "";
    private String plzOrt = "";

    private List<String> adresszeilen = null;

    private String testanzeige = "";

    private long stimmen = 0;
    private String stimmenDE = "";
    /**String formatiert im Deutschen Format*/
    private String stimmenEN = "";
    /**String formatiert im Englischen Format*/

    /**Wird durch pruefeErstregistrierung gefuellt - 
     * true => Erst-Registrierungsseite muß nach Login aufgerufen werden*/
    private boolean erstregistrierungAufrufen = false;

    /**Falls Passwort verändert wurde: Passwort verschlüsselt, für Rückgabe
     * an App
     */
    private String passwortVerschluesselt = "";

    
    
    /**1=Aktienregister-Nummer (Namensaktien)/ HV-Ticket-Nummer (Inhaberaktien)
     * 2=Kennung (Gäste, Bevollmächtigte, ...)
     */
    public int liefereKennungArt() {
        return eclLoginDaten.kennungArt;
    }

    public int liefereTextNrGattung() {
        int lGattung=liefereGattung();
        switch (lGattung) {
        case 1:return KonstPortalTexte.ALLGEMEIN_GATTUNG1_KENNUNG_VOR_AKTIEN;
        case 2:return KonstPortalTexte.ALLGEMEIN_GATTUNG2_KENNUNG_VOR_AKTIEN;
        case 3:return KonstPortalTexte.ALLGEMEIN_GATTUNG3_KENNUNG_VOR_AKTIEN;
        case 4:return KonstPortalTexte.ALLGEMEIN_GATTUNG4_KENNUNG_VOR_AKTIEN;
        case 5:return KonstPortalTexte.ALLGEMEIN_GATTUNG5_KENNUNG_VOR_AKTIEN;
        }
        return KonstPortalTexte.ALLGEMEIN_GATTUNG1_KENNUNG_VOR_AKTIEN;
    }
    
    /**Liefert Gattung der Login-Kennung:
     * > wenn Aktionär: 1 bis 5
     * > Wenn Gast oder Bevollmächtigter: -1
      */
    public int liefereGattung() {
        if (eclLoginDaten.kennungArt==2) {return -1;}
        int hGattung=eclAktienregister.gattungId;
        if (hGattung==0) {hGattung=1;}
        return hGattung;
    }
    
    public int lieferePersonNatJurIdent() {
        if (eclLoginDaten.kennungArt == KonstLoginKennungArt.aktienregister) {
            return eclAktienregister.personNatJur;
        } else {
            return eclPersonenNatJur.ident;
        }
    }

    public String liefereNameVornameTitel() {
        return name+" "+vorname+" "+titel;
    }
    
    public void clear() {
        eclLoginDaten = new EclLoginDaten();
        eclAktienregister = new EclAktienregister();
        eclPersonenNatJur = new EclPersonenNatJur();
        eclAktienregisterErgaenzung = new EclAktienregisterErgaenzung();
        anmeldeKennungFuerAnzeige = "";
        titelVornameName = "";
        ort = "";
        titel = "";
        name = "";
        vorname = "";
        strasse = "";
        plzOrt = "";

        adresszeilen = null;
        stimmen = 0;
        stimmenDE = "";
        stimmenEN = "";

        erstregistrierungAufrufen = false;
        
        passwortVerschluesselt="";
    }

    public void copyFrom(BlTeilnehmerLoginNeu pBlGTeilnehmerLogin) {
        eclLoginDaten = pBlGTeilnehmerLogin.eclLoginDaten;
        eclAktienregister = pBlGTeilnehmerLogin.eclAktienregister;
        eclPersonenNatJur = pBlGTeilnehmerLogin.eclPersonenNatJur;
        eclAktienregisterErgaenzung = pBlGTeilnehmerLogin.eclAktienregisterErgaenzung;

        anmeldeKennungFuerAnzeige = pBlGTeilnehmerLogin.anmeldeKennungFuerAnzeige;
        titelVornameName = pBlGTeilnehmerLogin.titelVornameName;
        ort = pBlGTeilnehmerLogin.ort;

        titel = pBlGTeilnehmerLogin.titel;
        name = pBlGTeilnehmerLogin.name;
        vorname = pBlGTeilnehmerLogin.vorname;
        strasse = pBlGTeilnehmerLogin.strasse;
        plzOrt = pBlGTeilnehmerLogin.plzOrt;

        adresszeilen = pBlGTeilnehmerLogin.adresszeilen;
        stimmen = pBlGTeilnehmerLogin.stimmen;
        stimmenDE = pBlGTeilnehmerLogin.stimmenDE;
        stimmenEN = pBlGTeilnehmerLogin.stimmenEN;

        erstregistrierungAufrufen = pBlGTeilnehmerLogin.erstregistrierungAufrufen;
        
        passwortVerschluesselt = pBlGTeilnehmerLogin.passwortVerschluesselt;

    }

    public void copyEclLoginDatenFrom(BlTeilnehmerLoginNeu pBlGTeilnehmerLogin) {
        eclLoginDaten = pBlGTeilnehmerLogin.eclLoginDaten;
    }
    
    
    
    public void copyToForReload(BlTeilnehmerLoginNeu pBlGTeilnehmerLogin) {
        pBlGTeilnehmerLogin.eclLoginDaten = eclLoginDaten;
        pBlGTeilnehmerLogin.eclAktienregister = eclAktienregister;
        pBlGTeilnehmerLogin.eclPersonenNatJur = eclPersonenNatJur;
    }

    public void copyToEclTLoginDatenM(EclTLoginDatenM eclTLoginDatenM) {
        eclTLoginDatenM.eclLoginDaten=eclLoginDaten;
        
        eclTLoginDatenM.eclAktienregister=eclAktienregister;
        eclTLoginDatenM.eclPersonenNatJur=eclPersonenNatJur;
        eclTLoginDatenM.eclAktienregisterErgaenzung=eclAktienregisterErgaenzung;
        
        eclTLoginDatenM.anmeldeKennungFuerAnzeige=anmeldeKennungFuerAnzeige;
        
        eclTLoginDatenM.titelVornameName=titelVornameName;
        eclTLoginDatenM.ort=ort;
        
        eclTLoginDatenM.titel=titel;
        eclTLoginDatenM.name=name;
        eclTLoginDatenM.vorname=vorname;
        eclTLoginDatenM.strasse=strasse;
        eclTLoginDatenM.plzOrt=plzOrt;
        
        eclTLoginDatenM.adresszeilen=adresszeilen;
        
        eclTLoginDatenM.testanzeige=testanzeige;
        
        eclTLoginDatenM.stimmen=stimmen;
        eclTLoginDatenM.stimmenDE=stimmenDE;
        eclTLoginDatenM.stimmenEN=stimmenEN;
        
        eclTLoginDatenM.erstregistrierungAufrufen=erstregistrierungAufrufen;
        
        eclTLoginDatenM.passwortVerschluesselt=passwortVerschluesselt;

     }
    
    /*******************getters und setters******************************/

    public EclLoginDaten getEclLoginDaten() {
        return eclLoginDaten;
    }

    public void setEclLoginDaten(EclLoginDaten eclLoginDaten) {
        this.eclLoginDaten = eclLoginDaten;
    }

    public EclAktienregister getEclAktienregister() {
        return eclAktienregister;
    }

    public void setEclAktienregister(EclAktienregister eclAktienregister) {
        this.eclAktienregister = eclAktienregister;
    }

    public EclPersonenNatJur getEclPersonenNatJur() {
        return eclPersonenNatJur;
    }

    public void setEclPersonenNatJur(EclPersonenNatJur eclPersonenNatJur) {
        this.eclPersonenNatJur = eclPersonenNatJur;
    }

    public String getAnmeldeKennungFuerAnzeige() {
        CaBug.druckeLog("anmeldeKennungFuerAnzeige=" + anmeldeKennungFuerAnzeige, logDrucken, 10);
        return anmeldeKennungFuerAnzeige;
    }

    public void setAnmeldeKennungFuerAnzeige(String anmeldeKennungFuerAnzeige) {
        this.anmeldeKennungFuerAnzeige = anmeldeKennungFuerAnzeige;
    }

    public boolean isErstregistrierungAufrufen() {
        return erstregistrierungAufrufen;
    }

    public void setErstregistrierungAufrufen(boolean erstregistrierungAufrufen) {
        this.erstregistrierungAufrufen = erstregistrierungAufrufen;
    }

    public String getTitelVornameName() {
        return titelVornameName;
    }

    public void setTitelVornameName(String titelVornameName) {
        this.titelVornameName = titelVornameName;
    }

    public String getOrt() {
        return ort;
    }

    public void setOrt(String ort) {
        this.ort = ort;
    }

    public List<String> getAdresszeilen() {
        return adresszeilen;
    }

    public void setAdresszeilen(List<String> adresszeilen) {
        this.adresszeilen = adresszeilen;
    }

    public long getStimmen() {
        return stimmen;
    }

    public void setStimmen(long stimmen) {
        this.stimmen = stimmen;
    }

    public String getStimmenDE() {
        return stimmenDE;
    }

    public void setStimmenDE(String stimmenDE) {
        this.stimmenDE = stimmenDE;
    }

    public String getStimmenEN() {
        return stimmenEN;
    }

    public void setStimmenEN(String stimmenEN) {
        this.stimmenEN = stimmenEN;
    }

    public String getTestanzeige() {
        return testanzeige;
    }

    public void setTestanzeige(String testanzeige) {
        this.testanzeige = testanzeige;
    }

    public String getTitel() {
        return titel;
    }

    public void setTitel(String titel) {
        this.titel = titel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVorname() {
        return vorname;
    }

    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

    public String getStrasse() {
        return strasse;
    }

    public void setStrasse(String strasse) {
        this.strasse = strasse;
    }

    public String getPlzOrt() {
        return plzOrt;
    }

    public void setPlzOrt(String plzOrt) {
        this.plzOrt = plzOrt;
    }

    public EclAktienregisterErgaenzung getEclAktienregisterErgaenzung() {
        return eclAktienregisterErgaenzung;
    }

    public void setEclAktienregisterErgaenzung(EclAktienregisterErgaenzung eclAktienregisterErgaenzung) {
        this.eclAktienregisterErgaenzung = eclAktienregisterErgaenzung;
    }

    public String getPasswortVerschluesselt() {
        return passwortVerschluesselt;
    }

    public void setPasswortVerschluesselt(String passwortVerschluesselt) {
        this.passwortVerschluesselt = passwortVerschluesselt;
    }

}
