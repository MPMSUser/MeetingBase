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
package de.meetingapps.meetingportal.meetComEntities;

import java.io.Serializable;
import java.util.List;

import de.meetingapps.meetingportal.meetComAllg.CaDatumZeit;

public class EclNachricht  implements Serializable {
    private static final long serialVersionUID = -2292001405384310356L;

    /**Eindeutige Ident je gespeicherte Mail in der Datenbank*/
    public int ident = 0;

    public long db_version = 0;
 
    /**Siehe ParamServer
     * Gehört zu ident und identMail*/
    public int dbServerIdent=0;
    
    /**Eindeutige Ident je versendeter Mail.
     * D.h. darüber lassen sich Mails an mehrere sowohl beim Sender als auch ggf.
     * beim Antworten an alle wieder zusammenfassen.
     */
    public int identMail = 0;
 
    public int mandant = 0;
    public int hvJahr = 0;
    public String hvNummer = "A";
    public String dbArt = "P";

    /**>0 => diese Mail ist eine Antwort auf diese Mail. Verweis auf identMail und dbServerIdent*/
    public int antwortZuMailIdent = 0;
    /**Siehe ParamServer*/
    public int antwortZuMailIdentDbServer=0;

    /**>0 => Verweis auf EclUserLogin (Mandantenübergreifend)
     * <0 => Verweis auf EclLoginDaten (des jeweiligen Mandanten)
     * =0 => Systemmail*/
    public int userIdAbsender = 0;
    

    /**>0 => Verweis auf EclUserLogin (Mandantenübergreifend)
     * Gliederung nach:
     * Emittent
     * Dritte
     * Insti, 
     * BO-Personal,
     * Bei "Gruppen" wie Emittent, Dritte etc: immer alle, oder ausgewählte anbieten.
     * 
     * 0= System 
     * 
     * <0 => Verweis auf EclLoginDaten (des jeweiligen Mandanten)
     * 
     * 
     * Falls eine Mail an mehrere geht, wird jeweils eine Kopie angelegt.
     */
    public int userIdEmpfaenger = 0;

    public int anzeigeBeimEmpfaengerAusblenden = 0;
    public int anzeigeBeimSenderAusblenden = 0;
    /**=1 => der Empfänger hat das Mail gelesen*/
    public int anzeigeBeimEmpfaengerGelesen=0;
    
    /**Leer => keine Reaktion erforderlich
     * LEN=19*/
    public String bearbeitenBis = "";
    public int mailIstBearbeitetVomEmpfaengerGesetzt = 0;
    public int mailIstBearbeitetVomSenderGesetzt = 0;

    /**LEN=19*/
    public String sendezeitpunkt = "";

    /**=0 => Es wird - als E-Mail - nur ein Standard-Text versandt, "Sie haben eine Nachricht im Portal".
     * =1 => MailText und Betreff werden auch im E-Mail versandt, aber nicht die Anlagen*/
    public int mailTextAuchInEmailAuffuehren = 0;

    /**=1 => es sind Anlagen-vorhanden*/
    public int anlagenSindVorhanden = 0;

    /**Separate Codes z.B. für "Zwischenmeldung", "Weisungsbestätigung etc.".
     * Siehe KonstNachrichtVerwendungsCode*/
    public int verwendungsCode = 0;

    /**Verwendung der Parameter je nach verwendungsCode:
     * insti_weisungsEmpfehlung: 
     * > parameter1 = Verweis auf ident von EclAbstimmungsVorschlagEmpfehlung
     * > parameter2 = Datenbankschema des Mandanten (damit der Mandant identifiziert werden kann)
     */
    /**LEN=20*/
    public String parameter1 = "";
    /**LEN=20*/
    public String parameter2 = "";
    /**LEN=20*/
    public String parameter3 = "";
    /**LEN=20*/
    public String parameter4 = "";
    /**LEN=20*/
    public String parameter5 = "";

    /**LEN=80*/
    public String betreff = "";
    /**LEN=10000*/
    public String mailText = "";

    /*********************Nicht in Datenbank**********************/
    
    public boolean detailNachrichtWirdAngezeigt=false;
    
    public String senderName = "";
    public String empfaengerName = "";
    
    /**wenn !=0, dann werden die entsprechenden Text vor / nach betreff/mailText angezeigt.*/
    public int betreffTextNrVor=0;
    public int betreffTextNrNach=0;
    
    public int mailTextTextNrVor=0;
    public int mailTextTextNrNach=0;
    
    /**Wenn !=0, dann wird nicht der mailText ausgegeben, sondern dieser Text. Die Variablen in den folgenden Listen
     * werden dabei ersetzt.
     * Muß allerdings beim Anzeigen der Nachrichten "manuell" durchgeführt werden. Derzeit nur bei den Nachrichten für Aktionäre
     * implementiert.*/
    public int mailTextNr=0;
    public List<String> variablenListe=null;
    public List<String> inhaltListe=null;

    
    /**wenn!=0, dann werden die entsprechenden Funktionsbuttons angezeigt*/
    public int funktionsButton1TextNr=0;
    public int funktionsButton2TextNr=0;
    
    
    /**true=> diese Nachricht ist nicht aus Mail-Datei, sondern wurde
     * aufgrund sonstiger Ereignisse generiert
     */
    public boolean generierteNachricht=false;

    /**Wenn generierteNachricht==true, dann hier Verweis auf den Auftrag, aufgrund dessen die Nachricht generiert wurde*/
    public EclAuftrag zugehoerigerAuftrag=null;
    
    /*******************Funktionen für Aufbereitung********************/
    public String liefereSendezeitpunkt() {
        if (sendezeitpunkt.length()>11) {
            return CaDatumZeit.DatumZeitStringFuerAnzeige(sendezeitpunkt);
        }
        else {
            return CaDatumZeit.DatumStringFuerAnzeige(sendezeitpunkt);
        }
    }
    
    
    /****************Standard getter und Setter*******************************************************/
    public int getIdent() {
        return ident;
    }

    public void setIdent(int ident) {
        this.ident = ident;
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

    public int getAnzeigeBeimEmpfaengerAusblenden() {
        return anzeigeBeimEmpfaengerAusblenden;
    }

    public void setAnzeigeBeimEmpfaengerAusblenden(int anzeigeBeimEmpfaengerAusblenden) {
        this.anzeigeBeimEmpfaengerAusblenden = anzeigeBeimEmpfaengerAusblenden;
    }

    public int getAnzeigeBeimSenderAusblenden() {
        return anzeigeBeimSenderAusblenden;
    }

    public void setAnzeigeBeimSenderAusblenden(int anzeigeBeimSenderAusblenden) {
        this.anzeigeBeimSenderAusblenden = anzeigeBeimSenderAusblenden;
    }

    public int getAnzeigeBeimEmpfaengerGelesen() {
        return anzeigeBeimEmpfaengerGelesen;
    }

    public void setAnzeigeBeimEmpfaengerGelesen(int anzeigeBeimEmpfaengerGelesen) {
        this.anzeigeBeimEmpfaengerGelesen = anzeigeBeimEmpfaengerGelesen;
    }

    public String getBearbeitenBis() {
        return bearbeitenBis;
    }

    public void setBearbeitenBis(String bearbeitenBis) {
        this.bearbeitenBis = bearbeitenBis;
    }

    public int getMailIstBearbeitetVomEmpfaengerGesetzt() {
        return mailIstBearbeitetVomEmpfaengerGesetzt;
    }

    public void setMailIstBearbeitetVomEmpfaengerGesetzt(int mailIstBearbeitetVomEmpfaengerGesetzt) {
        this.mailIstBearbeitetVomEmpfaengerGesetzt = mailIstBearbeitetVomEmpfaengerGesetzt;
    }

    public int getMailIstBearbeitetVomSenderGesetzt() {
        return mailIstBearbeitetVomSenderGesetzt;
    }

    public void setMailIstBearbeitetVomSenderGesetzt(int mailIstBearbeitetVomSenderGesetzt) {
        this.mailIstBearbeitetVomSenderGesetzt = mailIstBearbeitetVomSenderGesetzt;
    }

    public String getSendezeitpunkt() {
        return sendezeitpunkt;
    }

    public void setSendezeitpunkt(String sendezeitpunkt) {
        this.sendezeitpunkt = sendezeitpunkt;
    }

    public int getMailTextAuchInEmailAuffuehren() {
        return mailTextAuchInEmailAuffuehren;
    }

    public void setMailTextAuchInEmailAuffuehren(int mailTextAuchInEmailAuffuehren) {
        this.mailTextAuchInEmailAuffuehren = mailTextAuchInEmailAuffuehren;
    }

    public int getAnlagenSindVorhanden() {
        return anlagenSindVorhanden;
    }

    public void setAnlagenSindVorhanden(int anlagenSindVorhanden) {
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

    public String getEmpfaengerName() {
        return empfaengerName;
    }

    public void setEmpfaengerName(String empfaengerName) {
        this.empfaengerName = empfaengerName;
    }

    public boolean isGenerierteNachricht() {
        return generierteNachricht;
    }

    public void setGenerierteNachricht(boolean generierteNachricht) {
        this.generierteNachricht = generierteNachricht;
    }


    public int getBetreffTextNrVor() {
        return betreffTextNrVor;
    }


    public void setBetreffTextNrVor(int betreffTextNrVor) {
        this.betreffTextNrVor = betreffTextNrVor;
    }


    public int getBetreffTextNrNach() {
        return betreffTextNrNach;
    }


    public void setBetreffTextNrNach(int betreffTextNrNach) {
        this.betreffTextNrNach = betreffTextNrNach;
    }


    public int getMailTextTextNrVor() {
        return mailTextTextNrVor;
    }


    public void setMailTextTextNrVor(int mailTextTextNrVor) {
        this.mailTextTextNrVor = mailTextTextNrVor;
    }


    public int getMailTextTextNrNach() {
        return mailTextTextNrNach;
    }


    public void setMailTextTextNrNach(int mailTextTextNrNach) {
        this.mailTextTextNrNach = mailTextTextNrNach;
    }


    public int getFunktionsButton1TextNr() {
        return funktionsButton1TextNr;
    }


    public void setFunktionsButton1TextNr(int funktionsButton1TextNr) {
        this.funktionsButton1TextNr = funktionsButton1TextNr;
    }


    public int getFunktionsButton2TextNr() {
        return funktionsButton2TextNr;
    }


    public void setFunktionsButton2TextNr(int funktionsButton2TextNr) {
        this.funktionsButton2TextNr = funktionsButton2TextNr;
    }


    public boolean isDetailNachrichtWirdAngezeigt() {
        return detailNachrichtWirdAngezeigt;
    }


    public void setDetailNachrichtWirdAngezeigt(boolean detailNachrichtWirdAngezeigt) {
        this.detailNachrichtWirdAngezeigt = detailNachrichtWirdAngezeigt;
    }
}
