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

import java.util.LinkedList;
import java.util.List;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaDatumZeit;
import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComBl.BlNachrichten;
import de.meetingapps.meetingportal.meetComBl.BlNummernformBasis;
import de.meetingapps.meetingportal.meetComEclM.EclDbM;
import de.meetingapps.meetingportal.meetComEntitiesGenossenschaftSys.EgxGetMitgliedJNRC;
import de.meetingapps.meetingportal.meetComEntitiesGenossenschaftSys.EgxGetPersoenlicheDatenRC;
import de.meetingapps.meetingportal.meetComEntitiesGenossenschaftSys.EgxGetPersonendatenRC;
import de.meetingapps.meetingportal.meetComEntitiesGenossenschaftSys.EgxGetPersonendatenResult;
import de.meetingapps.meetingportal.meetComKonst.KonstAuftragArt;
import de.meetingapps.meetingportal.meetComKonst.KonstAuftragModul;
import de.meetingapps.meetingportal.meetComKonst.KonstNummerBasis;
import de.meetingapps.meetingportal.meetingportTController.TNachrichtenSession;
import de.meetingapps.meetingportal.meetingportTController.TPermanentSession;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

/**Routinen zum überprüfen der Aktionärsdaten im Register beim Login*/
@RequestScoped
@Named
public class BrMLogin {

    private int logDrucken=3;
    
    private @Inject EclDbM eclDbM;

    private @Inject TPermanentSession tPermanentSession;
    private @Inject TNachrichtenSession tNachrichtenSession;
    
    private @Inject BrMGenossenschaftCall brMGenossenschaftCall;

    /**Abfrage an Register hat ergeben:
     * -1 Aktionär ist nicht in Register vorhanden
     * 1 Aktionär ist in Register vorhanden
     */
    private int rcAktionaerInRegisterVorhanden=-1;
    
    /**-1 Aktionär bisher noch nicht im BetterMeeting vorhanden
     * 1 Aktionär ist bereits im BetterMeeting vorhanden*/
    private int rcAktionaerBereitsInMeetingVorhanden=-1;

    private String rcEMailInRemoteRegister="";
    
    /**Wird auf true gesetzt, wenn bereits lokal Änderungsaufträge vorhanden sind.
     * Dann muß nämlich die E-Mail-Adresse aus dem Remote-Register
     * ignoriert werden.
     */
    private boolean rcEmailAuftrageVorhanden=false;
    
    /**Wird durch pruefeNachLogin gefüllt, wenn Mitglied im Register enthalten ist*/
    private EgxGetPersoenlicheDatenRC rcEgxGetPersoenlicheDatenRC=null;
    private EgxGetPersonendatenRC rcEgxGetPersonendatenRC=null;
    
    public int pruefeNachLogin(String pAktionaersnummer) {
        rcAktionaerInRegisterVorhanden=-1;
        rcAktionaerBereitsInMeetingVorhanden=-1;
        
        String aktionaersnummer=pAktionaersnummer.trim();
        if (aktionaersnummer.isEmpty()) {
            rcAktionaerInRegisterVorhanden=-1;
            CaBug.druckeLog("isEmpty", logDrucken, 10);
            return 1;
        }
        
        /*Aktionärsnummer aufbereiten*/
        BlNummernformBasis blNummernformBasis = new BlNummernformBasis(eclDbM.getDbBundle());
        aktionaersnummer = blNummernformBasis.loginKennungAufbereitenFuerIntern(aktionaersnummer);
        int nummernformBasis = blNummernformBasis.rcNummernformBasis;
        if (nummernformBasis!=KonstNummerBasis.nurNummer) {
            rcAktionaerInRegisterVorhanden=-1;
            CaBug.druckeLog("nurNummer", logDrucken, 10);
            return 1;
        }
        
        String aktionaersnummerFuerRegister=blNummernformBasis.aufbereitenKennungFuerRegisterzugriff(aktionaersnummer);
        String aktionaersnummerFuerGenossenschaftSysWebrequest = blNummernformBasis.aufbereitenKennungFuerGenossenschaftSysWebrequest(aktionaersnummer);
        CaBug.druckeLog(aktionaersnummer, logDrucken, 10);
        CaBug.druckeLog(aktionaersnummerFuerRegister, logDrucken, 10);
		CaBug.druckeLog(aktionaersnummerFuerGenossenschaftSysWebrequest, logDrucken, 10);
        
        /*Checken, ob Aktionärsnummer in Aktienregister vorhanden*/
        if (tPermanentSession.isTestModus()) {
            CaBug.druckeLog("Prüfen ob in Aktienregister: aktionaersnummer="+aktionaersnummer, logDrucken, 10);
            if (aktionaersnummer.equals("0000000002") || aktionaersnummer.equals("0000000003")) {
                CaBug.druckeLog("2 oder 3", logDrucken, 10);
                rcAktionaerInRegisterVorhanden=1;
            }
            if (rcAktionaerInRegisterVorhanden==-1) {return 1;}
        }
        else {
            /*Über Schnittstelle überprüfen, ob Aktionärsnummer aktionaersnummerFuerRegister in Register enthalten ist*/
            CaBug.druckeLog("aktionaersnummerFuerRegister="+aktionaersnummerFuerRegister, logDrucken, 10);
            CaBug.druckeLog("aktionaersnummerFuerGenossenschaftSysWebrequest="+aktionaersnummerFuerGenossenschaftSysWebrequest, logDrucken, 10);
            
            EgxGetMitgliedJNRC egxMitgliedJNRC = brMGenossenschaftCall.doGetRequestMitgliedJN(aktionaersnummerFuerGenossenschaftSysWebrequest);
            if (egxMitgliedJNRC==null) {
                return CaFehler.perRemoteAktienregisterNichtVerfuegbar;
            }

            if(egxMitgliedJNRC.mitgliedjn) {
                rcAktionaerInRegisterVorhanden=1;
                CaBug.druckeLog("Ist Mitglied laut GenossenschaftSys", logDrucken, 10);
            } else {
                CaBug.druckeLog("Kein Mitglied laut GenossenschaftSys", logDrucken, 10);
            }
            if (rcAktionaerInRegisterVorhanden==-1) {return 1;}
        }
        
        /*Daten aus Aktienregister holen*/
        List<String> lAnzeigedatenMitglied=new LinkedList<String>();
        String hString="";
        hString=BlNummernformBasis.aufbereitenKennungFuerExtern(aktionaersnummer, eclDbM.getDbBundle());
        tPermanentSession.setAnmeldeKennungFuerAnzeige(hString);
        
        lAnzeigedatenMitglied.add(hString);
        if (tPermanentSession.isTestModus()) {
            switch (aktionaersnummer) {
            case "0000000002":
                tPermanentSession.setTitelVornameName("Max Mustermann");
                tPermanentSession.setOrt("München");
                tPermanentSession.setAnteile(20);
                break;
            case "0000000003":
                tPermanentSession.setTitelVornameName("Erika Musterfrau");
                tPermanentSession.setOrt("München");
                tPermanentSession.setAnteile(30);
                break;
            default:
                tPermanentSession.setTitelVornameName("Sepp Hinterhuber");
                tPermanentSession.setOrt("München");
                tPermanentSession.setAnteile(91);
                break;
            }
        }
        else {
            /*Über Schnittstelle Aktionärsdaten (allgemein) holen*/
        	rcEgxGetPersoenlicheDatenRC = brMGenossenschaftCall.doGetRequestPersoenlicheDaten(aktionaersnummerFuerGenossenschaftSysWebrequest);
            if (rcEgxGetPersoenlicheDatenRC==null) {
                return CaFehler.perRemoteAktienregisterNichtVerfuegbar;
            }
            
            rcEgxGetPersonendatenRC = brMGenossenschaftCall
                    .doGetRequestPersonendaten(aktionaersnummerFuerGenossenschaftSysWebrequest);
            if (rcEgxGetPersonendatenRC==null) {
                return CaFehler.perRemoteAktienregisterNichtVerfuegbar;
            }

        	
            tPermanentSession.setAnmeldeKennungFuerAnzeige(Integer.toString(rcEgxGetPersoenlicheDatenRC.kunde)/*+" aus GeDix"*/);
 
        	String lTitelVornameName=rcEgxGetPersoenlicheDatenRC.nachname;
        	if (!rcEgxGetPersoenlicheDatenRC.vorname.isEmpty()) {
        	    lTitelVornameName=rcEgxGetPersoenlicheDatenRC.vorname+" "+rcEgxGetPersoenlicheDatenRC.nachname;
        	}
            tPermanentSession.setTitelVornameName(lTitelVornameName/*+" aus GeDix"*/);
            tPermanentSession.setOrt(rcEgxGetPersoenlicheDatenRC.adr_ort/*+" aus GeDix"*/);
            tPermanentSession.setAnteile(rcEgxGetPersoenlicheDatenRC.anteile);
            rcEMailInRemoteRegister=rcEgxGetPersoenlicheDatenRC.email;
            
            eclDbM.getDbBundle().dbAuftrag.read_auftraegeVorhanden(KonstAuftragModul.ANBINDUNG_AKTIENREGISTER, KonstAuftragArt.ANBINDUNG_AKTIENREGISTER_AENDERN_MEINEDATEN_EMAIL);
            if (eclDbM.getDbBundle().dbAuftrag.anzErgebnis()!=0) {
                rcEmailAuftrageVorhanden=true;
            }

        }
       
        /*Checken, ob bereits registriert*/
        if (tPermanentSession.isTestModus()) {
            CaBug.druckeLog("Prüfen ob registriert: aktionaersnummer="+aktionaersnummer, logDrucken, 10);
            if (aktionaersnummer.equals("00000000011")) {
                rcAktionaerBereitsInMeetingVorhanden=-1;
                return -1;
            }
        }
        eclDbM.getDbBundle().dbLoginDaten.read_loginKennung(aktionaersnummer);
        if (eclDbM.getDbBundle().dbLoginDaten.anzErgebnis()==0) {
            rcAktionaerBereitsInMeetingVorhanden=-1;
            return -1;
        }
        else {
            rcAktionaerBereitsInMeetingVorhanden=1;
           
        }
        
        /*Ticket-Status updaten*/
        int loginIdent=eclDbM.getDbBundle().dbLoginDaten.ergebnisPosition(0).ident;
        /*Nachrichten einlesen, um Anzahl ungelesene Nachrichten in Menü anzuzeigen*/
        BlNachrichten blNachrichten=new BlNachrichten(true, eclDbM.getDbBundle());
        blNachrichten.leseNachrichtenUndAuftraegeFuerAktionaer(loginIdent*(-1));

        tNachrichtenSession.setEmpfangeneNachrichten(blNachrichten.rcEmpfangeneNachrichtenList);
        tNachrichtenSession.setAnzahlUngeleseneNachrichten(blNachrichten.rcEmpfangeneNachrichtenAnzahlUngelesen);

        return 1;
    }
    
    /**pGeburtsdatum in der Form TT.MM.JJJJ
     * 
     * Voraussetzung: pruefeNachLogin wurde aufgerufen*/
    public boolean pruefeGeburtsdatum(String pGeburtsdatum){
        if (rcEgxGetPersonendatenRC.result.size() > 0) {
            for (EgxGetPersonendatenResult result : rcEgxGetPersonendatenRC.result) {
                if (result.art.equals("natürliche Person (Mitglied / Teil des Mitglieds)")) {
                    if (pGeburtsdatum.equals(CaDatumZeit.datumJJJJ_MM_TTzuNormal(result.geburt))){
                        return true;
                    };
                }
            }
        }
        return false;
    }
    
    
    /*****************Standard getter und setter*****************************************************/
    public int getRcAktionaerInRegisterVorhanden() {
        return rcAktionaerInRegisterVorhanden;
    }

    public void setRcAktionaerInRegisterVorhanden(int rcAktionaerInRegisterVorhanden) {
        this.rcAktionaerInRegisterVorhanden = rcAktionaerInRegisterVorhanden;
    }

    public int getRcAktionaerBereitsInMeetingVorhanden() {
        return rcAktionaerBereitsInMeetingVorhanden;
    }

    public void setRcAktionaerBereitsInMeetingVorhanden(int rcAktionaerBereitsInBetterMeetingVorhanden) {
        this.rcAktionaerBereitsInMeetingVorhanden = rcAktionaerBereitsInBetterMeetingVorhanden;
    }

    public EgxGetPersonendatenRC getRcEgxGetPersonendatenRC() {
        return rcEgxGetPersonendatenRC;
    }

    public void setRcEgxGetPersonendatenRC(EgxGetPersonendatenRC rcEgxGetPersonendatenRC) {
        this.rcEgxGetPersonendatenRC = rcEgxGetPersonendatenRC;
    }

    public EgxGetPersoenlicheDatenRC getRcEgxGetPersoenlicheDatenRC() {
        return rcEgxGetPersoenlicheDatenRC;
    }

    public void setRcEgxGetPersoenlicheDatenRC(EgxGetPersoenlicheDatenRC rcEgxGetPersoenlicheDatenRC) {
        this.rcEgxGetPersoenlicheDatenRC = rcEgxGetPersoenlicheDatenRC;
    }

    public String getRcEMailInRemoteRegister() {
        return rcEMailInRemoteRegister;
    }

    public void setRcEMailInRemoteRegister(String rcEMailInRemoteRegister) {
        this.rcEMailInRemoteRegister = rcEMailInRemoteRegister;
    }

    public boolean isRcEmailAuftrageVorhanden() {
        return rcEmailAuftrageVorhanden;
    }

    public void setRcEmailAuftrageVorhanden(boolean rcEmailAuftrageVorhanden) {
        this.rcEmailAuftrageVorhanden = rcEmailAuftrageVorhanden;
    }
     
}
