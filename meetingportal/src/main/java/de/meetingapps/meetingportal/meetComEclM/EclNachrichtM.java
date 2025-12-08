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

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaDatumZeit;
import de.meetingapps.meetingportal.meetComEntities.EclNachricht;
import de.meetingapps.meetingportal.meetComKonst.KonstNachrichtVerwendungsCode;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

@SessionScoped
@Named
public class EclNachrichtM implements Serializable {
    private static final long serialVersionUID = 5533186607327074646L;

    private int logDrucken = 10;

    /**Eindeutige Ident je gespeicherte Mail in der Datenbank*/
    private int ident = 0;

    private long db_version = 0;

    /**Siehe ParamServer*/
    private int dbServerIdent=0;

    /**Eindeutige Ident je versendeter Mail.
     * D.h. darüber lassen sich Mails an mehrere sowohl beim Sender als auch ggf.
     * beim Antworten an alle wieder zusammenfassen.
     */
    private int identMail = 0;

    private int mandant = 0;
    private int hvJahr = 0;
    private String hvNummer = "A";
    private String dbArt = "P";

    /**Wird von BlMNachrichten.einlesenEmpfangeneNachrichten gefüllt, aber nur wenn "Aktionrs-Mail"*/
    private String emittentenName = "";

    private boolean antwortZuMail = false;

    /**>0 => diese Mail ist eine Antwort auf diese Mail. Verweis auf identMail und dbServerIdent*/
    private int antwortZuMailIdent = 0;
    /**Siehe ParamServer*/
    private int antwortZuMailIdentDbServer=0;

    /**>0 => Verweis auf EclUserLogin (Mandantenübergreifend)
     * <0 => Verweis auf EclLoginDaten (des jeweiligen Mandanten)
     * =0 => Systemmail*/
    private int userIdAbsender = 0;

    /**>0 => Verweis auf EclUserLogin (Mandantenübergreifend)
     * Gliederung nach:
     * Emittent
     * Dritte
     * Insti, 
     * BO-Personal,
     * Bei "Gruppen" wie Emittent, Dritte etc: immer alle, oder ausgewählte anbieten.
     * 
     * <0 => Verweis auf EclLoginDaten (des jeweiligen Mandanten)
     * 
     * 
     * Falls eine Mail an mehrere geht, wird jeweils eine Kopie angelegt.
     */
    private int userIdEmpfaenger = 0;

    private boolean anzeigeBeimEmpfaengerAusblenden = false;
    private boolean anzeigeBeimSenderAusblenden = false;
    /**der Empfänger hat das Mail gelesen*/
    private boolean anzeigeBeimEmpfaengerGelesen=false;

    /**Leer => keine Reaktion erforderlich
     * LEN=19*/
    private String bearbeitenBis = "";
    private boolean mailIstBearbeitetVomEmpfaengerGesetzt = false;
    private boolean mailIstBearbeitetVomSenderGesetzt = false;

    /**LEN=19*/
    private String sendezeitpunkt = "";

    /**Wird automatisch gesetzt*/
    private String sendezeitpunktAnzeige = "";

    /**=0 => Es wird - als E-Mail - nur ein Standard-Text versandt, "Sie haben eine Nachricht im Portal".
     * =1 => MailText und Betreff werden auch im E-Mail versandt, aber nicht die Anlagen*/
    private boolean mailTextAuchInEmailAuffuehren = false;

    /**=1 => es sind Anlagen-vorhanden*/
    private boolean anlagenSindVorhanden = false;

    /**Separate Codes z.B. für "Zwischenmeldung", "Weisungsbestätigung etc.".
     * Siehe KonstNachrichtVerwendungsCode*/
   private int verwendungsCode = 0;
    /**Wird aus KonstNachrichtenVerwendungsCode generiert*/
    private String verwendungsCodeText = "";

    /**LEN=20*/
    private String parameter1 = "";
    /**LEN=20*/
    private String parameter2 = "";
    /**LEN=20*/
    private String parameter3 = "";
    /**LEN=20*/
    private String parameter4 = "";
    /**LEN=20*/
    private String parameter5 = "";

    /**LEN=80*/
    private String betreff = "";
    /**LEN=2000*/
    private String mailText = "";

    private String senderName = "";
    private String empfaengerName = "";

    /**Wird automatisch gesetzt, wenn NachrichtVerwendungsCode>100.000, d.h.
     * darf dann in der "normalen" Oberfläche nicht bearbeitet werden.
     */
    private boolean technischeNachricht = false;

    /**true=> diese Nachricht ist nicht aus Mail-Datei, sondern wurde
     * aufgrund sonstiger Ereignisse generiert
     */
    private boolean generierteNachricht=false;

    public EclNachrichtM() {
        return;
    }

    public EclNachrichtM(EclNachricht pEclNachricht) {
        copyFrom(pEclNachricht);
    }

    public void copyFrom(EclNachricht pEclNachricht) {
        ident = pEclNachricht.ident;
        db_version = pEclNachricht.db_version;
        dbServerIdent = pEclNachricht.dbServerIdent;
        identMail = pEclNachricht.identMail;
        
        mandant = pEclNachricht.mandant;
        hvJahr = pEclNachricht.hvJahr;
        hvNummer = pEclNachricht.hvNummer;
        dbArt = pEclNachricht.dbArt;
        
        if (pEclNachricht.antwortZuMailIdent == 1) {
            antwortZuMail = true;
        }
        antwortZuMailIdent = pEclNachricht.antwortZuMailIdent;
        antwortZuMailIdentDbServer = pEclNachricht.antwortZuMailIdentDbServer;
        
        userIdAbsender = pEclNachricht.userIdAbsender;
        userIdEmpfaenger = pEclNachricht.userIdEmpfaenger;
        if (pEclNachricht.anzeigeBeimEmpfaengerAusblenden == 1) {
            anzeigeBeimEmpfaengerAusblenden = true;
        }
        if (pEclNachricht.anzeigeBeimSenderAusblenden == 1) {
            anzeigeBeimSenderAusblenden = true;
        }
        if (pEclNachricht.anzeigeBeimEmpfaengerGelesen == 1) {
            anzeigeBeimEmpfaengerGelesen = true;
        }
        
        bearbeitenBis = pEclNachricht.bearbeitenBis;
        if (pEclNachricht.mailIstBearbeitetVomEmpfaengerGesetzt == 1) {
            mailIstBearbeitetVomEmpfaengerGesetzt = true;
        }
        if (pEclNachricht.mailIstBearbeitetVomSenderGesetzt == 1) {
            mailIstBearbeitetVomSenderGesetzt = true;
        }
        
        sendezeitpunkt = pEclNachricht.sendezeitpunkt;
        sendezeitpunktAnzeige = CaDatumZeit.DatumZeitStringFuerAnzeige(sendezeitpunkt);

        if (pEclNachricht.mailTextAuchInEmailAuffuehren == 1) {
            mailTextAuchInEmailAuffuehren = true;
        }
        if (pEclNachricht.anlagenSindVorhanden == 1) {
            anlagenSindVorhanden = true;
        }
        verwendungsCode = pEclNachricht.verwendungsCode;
        if (verwendungsCode > 100000) {
            technischeNachricht = true;
        } else {
            technischeNachricht = false;
        }
        verwendungsCodeText = KonstNachrichtVerwendungsCode.getText(verwendungsCode);
        generierteNachricht = pEclNachricht.generierteNachricht;

        parameter1 = pEclNachricht.parameter1;
        parameter2 = pEclNachricht.parameter2;
        parameter3 = pEclNachricht.parameter3;
        parameter4 = pEclNachricht.parameter4;
        parameter5 = pEclNachricht.parameter5;
        betreff = pEclNachricht.betreff;
        mailText = pEclNachricht.mailText;
        senderName = pEclNachricht.senderName;
        empfaengerName = pEclNachricht.empfaengerName;
    }

    /**********************Standard getter und setter*****************************************/
    public int getIdent() {
        return ident;
    }

    public void setIdent(int ident) {
        this.ident = ident;
    }

    public int getIdentMail() {
        return identMail;
    }

    public void setIdentMail(int identMail) {
        this.identMail = identMail;
    }

    public int getMandant() {
        return mandant;
    }

    public void setMandant(int mandant) {
        this.mandant = mandant;
    }

    public int getHvJahr() {
        return hvJahr;
    }

    public void setHvJahr(int hvJahr) {
        this.hvJahr = hvJahr;
    }

    public String getHvNummer() {
        return hvNummer;
    }

    public void setHvNummer(String hvNummer) {
        this.hvNummer = hvNummer;
    }

    public String getDbArt() {
        return dbArt;
    }

    public void setDbArt(String dbArt) {
        this.dbArt = dbArt;
    }

    public int getUserIdAbsender() {
        return userIdAbsender;
    }

    public void setUserIdAbsender(int userIdAbsender) {
        this.userIdAbsender = userIdAbsender;
    }

    public int getUserIdEmpfaenger() {
        return userIdEmpfaenger;
    }

    public void setUserIdEmpfaenger(int userIdEmpfaenger) {
        this.userIdEmpfaenger = userIdEmpfaenger;
    }

    public boolean isAnzeigeBeimEmpfaengerAusblenden() {
        return anzeigeBeimEmpfaengerAusblenden;
    }

    public void setAnzeigeBeimEmpfaengerAusblenden(boolean anzeigeBeimEmpfaengerAusblenden) {
        this.anzeigeBeimEmpfaengerAusblenden = anzeigeBeimEmpfaengerAusblenden;
    }

    public boolean isAnzeigeBeimSenderAusblenden() {
        return anzeigeBeimSenderAusblenden;
    }

    public void setAnzeigeBeimSenderAusblenden(boolean anzeigeBeimSenderAusblenden) {
        this.anzeigeBeimSenderAusblenden = anzeigeBeimSenderAusblenden;
    }

    public String getBearbeitenBis() {
        return bearbeitenBis;
    }

    public void setBearbeitenBis(String bearbeitenBis) {
        this.bearbeitenBis = bearbeitenBis;
    }

    public boolean isMailIstBearbeitetVomEmpfaengerGesetzt() {
        return mailIstBearbeitetVomEmpfaengerGesetzt;
    }

    public void setMailIstBearbeitetVomEmpfaengerGesetzt(boolean mailIstBearbeitetVomEmpfaengerGesetzt) {
        this.mailIstBearbeitetVomEmpfaengerGesetzt = mailIstBearbeitetVomEmpfaengerGesetzt;
    }

    public boolean isMailIstBearbeitetVomSenderGesetzt() {
        return mailIstBearbeitetVomSenderGesetzt;
    }

    public void setMailIstBearbeitetVomSenderGesetzt(boolean mailIstBearbeitetVomSenderGesetzt) {
        this.mailIstBearbeitetVomSenderGesetzt = mailIstBearbeitetVomSenderGesetzt;
    }

    public String getSendezeitpunkt() {
        return sendezeitpunkt;
    }

    public void setSendezeitpunkt(String sendezeitpunkt) {
        this.sendezeitpunkt = sendezeitpunkt;
    }

    public boolean isMailTextAuchInEmailAuffuehren() {
        return mailTextAuchInEmailAuffuehren;
    }

    public void setMailTextAuchInEmailAuffuehren(boolean mailTextAuchInEmailAuffuehren) {
        this.mailTextAuchInEmailAuffuehren = mailTextAuchInEmailAuffuehren;
    }

    public boolean isAnlagenSindVorhanden() {
        return anlagenSindVorhanden;
    }

    public void setAnlagenSindVorhanden(boolean anlagenSindVorhanden) {
        this.anlagenSindVorhanden = anlagenSindVorhanden;
    }

    public int getVerwendungsCode() {
        return verwendungsCode;
    }

    public void setVerwendungsCode(int verwendungsCode) {
        this.verwendungsCode = verwendungsCode;
    }

    public String getParameter1() {
        return parameter1;
    }

    public void setParameter1(String parameter1) {
        this.parameter1 = parameter1;
    }

    public String getParameter2() {
        return parameter2;
    }

    public void setParameter2(String parameter2) {
        this.parameter2 = parameter2;
    }

    public String getParameter3() {
        return parameter3;
    }

    public void setParameter3(String parameter3) {
        this.parameter3 = parameter3;
    }

    public String getParameter4() {
        return parameter4;
    }

    public void setParameter4(String parameter4) {
        this.parameter4 = parameter4;
    }

    public String getParameter5() {
        return parameter5;
    }

    public void setParameter5(String parameter5) {
        this.parameter5 = parameter5;
    }

    public String getBetreff() {
        CaBug.druckeLog("EclNachrichtM.getBetreff", logDrucken, 10);
        return betreff;
    }

    public void setBetreff(String betreff) {
        this.betreff = betreff;
    }

    public String getMailText() {
        return mailText;
    }

    public void setMailText(String mailText) {
        this.mailText = mailText;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public boolean isTechnischeNachricht() {
        return technischeNachricht;
    }

    public void setTechnischeNachricht(boolean technischeNachricht) {
        this.technischeNachricht = technischeNachricht;
    }

    public String getVerwendungsCodeText() {
        return verwendungsCodeText;
    }

    public void setVerwendungsCodeText(String verwendungsCodeText) {
        this.verwendungsCodeText = verwendungsCodeText;
    }

    public String getSendezeitpunktAnzeige() {
        return sendezeitpunktAnzeige;
    }

    public void setSendezeitpunktAnzeige(String sendezeitpunktAnzeige) {
        this.sendezeitpunktAnzeige = sendezeitpunktAnzeige;
    }

    public String getEmittentenName() {
        return emittentenName;
    }

    public void setEmittentenName(String emittentenName) {
        this.emittentenName = emittentenName;
    }

    public String getEmpfaengerName() {
        return empfaengerName;
    }

    public void setEmpfaengerName(String empfaengerName) {
        this.empfaengerName = empfaengerName;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public long getDb_version() {
        return db_version;
    }

    public void setDb_version(long db_version) {
        this.db_version = db_version;
    }

    public int getDbServerIdent() {
        return dbServerIdent;
    }

    public void setDbServerIdent(int dbServerIdent) {
        this.dbServerIdent = dbServerIdent;
    }

    public boolean isAntwortZuMail() {
        return antwortZuMail;
    }

    public void setAntwortZuMail(boolean antwortZuMail) {
        this.antwortZuMail = antwortZuMail;
    }

    public int getAntwortZuMailIdent() {
        return antwortZuMailIdent;
    }

    public void setAntwortZuMailIdent(int antwortZuMailIdent) {
        this.antwortZuMailIdent = antwortZuMailIdent;
    }

    public int getAntwortZuMailIdentDbServer() {
        return antwortZuMailIdentDbServer;
    }

    public void setAntwortZuMailIdentDbServer(int antwortZuMailIdentDbServer) {
        this.antwortZuMailIdentDbServer = antwortZuMailIdentDbServer;
    }

    public boolean isAnzeigeBeimEmpfaengerGelesen() {
        return anzeigeBeimEmpfaengerGelesen;
    }

    public void setAnzeigeBeimEmpfaengerGelesen(boolean anzeigeBeimEmpfaengerGelesen) {
        this.anzeigeBeimEmpfaengerGelesen = anzeigeBeimEmpfaengerGelesen;
    }

    public boolean isGenerierteNachricht() {
        return generierteNachricht;
    }

    public void setGenerierteNachricht(boolean generierteNachricht) {
        this.generierteNachricht = generierteNachricht;
    }
}
