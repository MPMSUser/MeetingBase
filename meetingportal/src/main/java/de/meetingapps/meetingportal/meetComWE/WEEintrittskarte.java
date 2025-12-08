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
package de.meetingapps.meetingportal.meetComWE;

import de.meetingapps.meetingportal.meetComEclM.EclZugeordneteMeldungM;
import de.meetingapps.meetingportal.meetComEntities.EclZugeordneteMeldungNeu;

public class WEEintrittskarte {

    public WELoginVerify weLoginVerify = null;

    /**Siehe KonstPortalAktion.HAUPT_*/
    public int ausgewaehlteHauptAktion = 0;

    /**Siehe KonstPortalAktion*/
    public int ausgewaehlteAktion = 0;

    /**Muß mit der AnmeldeAktionaersnummer gefüllt werden aus dem "aktuell bearbeiteten Aktionär"*/
    public String anmeldeAktionaersnummer = "";

    
    /**siehe KonstWillenserklaerungVersandartEK*/
   public int eintrittskarteVersandart = 0;

   public String eintrittskarteAbweichendeAdresse1 = "";
   public String eintrittskarteAbweichendeAdresse2 = "";
   public String eintrittskarteAbweichendeAdresse3 = "";
   public String eintrittskarteAbweichendeAdresse4 = "";
   public String eintrittskarteAbweichendeAdresse5 = "";

   public String vollmachtName = "";
   public String vollmachtVorname = "";
   public String vollmachtOrt = "";

   public int eintrittskarteVersandart2 = 0;

   private String eintrittskarteAbweichendeAdresse12 = "";
   private String eintrittskarteAbweichendeAdresse22 = "";
   private String eintrittskarteAbweichendeAdresse32 = "";
   private String eintrittskarteAbweichendeAdresse42 = "";
   private String eintrittskarteAbweichendeAdresse52 = "";

   private String vollmachtName2 = "";
   private String vollmachtVorname2 = "";
   private String vollmachtOrt2 = "";
   
   /**Muß gefüllt werden, wenn ausgewaehlteHauptaktion==HAUPT_BEREITSANGEMELDET ist, denn dann wird die Eintrittskarte zu dieser
    * Meldung ausgestellt.
    */
   public EclZugeordneteMeldungNeu eclZugeordneteMeldung = null;

   public String quelle = "";

    /***********************************************Ab hier noch nicht überarbeitet*****************************************/
    /*Beschreibungen siehe ADlgVariablen*/

    /**1=Neuanmeldung; 2=zusätzliche EK zu bestehender Anmeldung*/
    @Deprecated
    private String ausgewaehlteHauptAktionString = "";

    /****************************Ausgewählte Aktionärt******************************************************
     * Gibt an, welche "Detail"-Aktion gerade ausgewählt ist und ausgeführt ist.
     * 1 = Eine Eintrittskarte auf meinen Namen ausstellen
     * 2 = zwei Eintrittskarten auf unsere Namen ausstellen (nur bei Personengemeinschaften)
     * 3 = eine Eintrittskarte auf einen Bevollmächtigten ausstellen
     * siehe auch 28! 
     * 28 = zwei Eintrittskarten mit oder ohne Vollmacht (bei allen Anmeldungen)
     * 30 = zwei Eintrittskarten auch mich selbst (bei allen Anmeldungen)
     * */

    @Deprecated
    private String ausgewaehlteAktionString = "";

    /**
     * 1 = Aufnahme in Sammelbatch, an Adresse im Aktienregister - bei nächstem Drucklauf ausdrucken und versenden
     * 2 = Aufnahme in Sammelbatch, an Versandadresse - bei nächstem Drucklauf ausdrucken und versenden
     * 3 = Online-Ausdruck (im Portal) erfolgt - in diesem Fall: Eintrittskarte wird sofort erzeugt, Nr im Return mit zurückgegeben,
     * 			und kann dann später angefordert und lokal ausgedruckt werden (quasi als Sofortdruck).
     * 4 = Versand per Email (im Portal) erfolgt
     * 5 = automatische Aufnahme in App (nicht verwendet)
     */
    @Deprecated
    private String eintrittskarteVersandartString = "";

    private String eintrittskarteEmail = "";

    private String zielOeffentlicheID = "";
    private boolean ueberOeffentlicheID = false;
    private int personNatJurOeffentlicheID = 0;

    @Deprecated
    private String eintrittskarteVersandart2String = "";

    private String eintrittskarteEmail2 = "";


    /**Muß gefüllt werden, wenn ausgewaehlteHauptaktion==2 ist, denn dann wird die Eintrittskarte zu dieser
     * Meldung ausgestellt.
     */
    @Deprecated
    private EclZugeordneteMeldungM eclZugeordneteMeldungM = null;



    //	private int eintrittskartePdfNr2=0; //War ursprünglich mal in App mit drin - überprüft - wird nicht verwendet

    /**********************Ab hier Standard-Setter/Getter**************************************************/
    public WELoginVerify getWeLoginVerify() {
        return weLoginVerify;
    }

    public void setWeLoginVerify(WELoginVerify weLoginVerify) {
        this.weLoginVerify = weLoginVerify;
    }

    @Deprecated
    public String getAusgewaehlteHauptAktionString() {
        return ausgewaehlteHauptAktionString;
    }

    @Deprecated
    public void setAusgewaehlteHauptAktionString(String ausgewaehlteHauptAktion) {
        this.ausgewaehlteHauptAktionString = ausgewaehlteHauptAktion;
    }

    @Deprecated
    public String getAusgewaehlteAktionString() {
        return ausgewaehlteAktionString;
    }

    @Deprecated
    public void setAusgewaehlteAktionString(String ausgewaehlteAktion) {
        this.ausgewaehlteAktionString = ausgewaehlteAktion;
    }

    @Deprecated
    public String getEintrittskarteVersandartString() {
        return eintrittskarteVersandartString;
    }

    @Deprecated
    public void setEintrittskarteVersandartString(String eintrittskarteVersandart) {
        this.eintrittskarteVersandartString = eintrittskarteVersandart;
    }

    public String getEintrittskarteEmail() {
        return eintrittskarteEmail;
    }

    public void setEintrittskarteEmail(String eintrittskarteEmail) {
        this.eintrittskarteEmail = eintrittskarteEmail;
    }

    public String getEintrittskarteAbweichendeAdresse1() {
        return eintrittskarteAbweichendeAdresse1;
    }

    public void setEintrittskarteAbweichendeAdresse1(String eintrittskarteAbweichendeAdresse1) {
        this.eintrittskarteAbweichendeAdresse1 = eintrittskarteAbweichendeAdresse1;
    }

    public String getEintrittskarteAbweichendeAdresse2() {
        return eintrittskarteAbweichendeAdresse2;
    }

    public void setEintrittskarteAbweichendeAdresse2(String eintrittskarteAbweichendeAdresse2) {
        this.eintrittskarteAbweichendeAdresse2 = eintrittskarteAbweichendeAdresse2;
    }

    public String getEintrittskarteAbweichendeAdresse3() {
        return eintrittskarteAbweichendeAdresse3;
    }

    public void setEintrittskarteAbweichendeAdresse3(String eintrittskarteAbweichendeAdresse3) {
        this.eintrittskarteAbweichendeAdresse3 = eintrittskarteAbweichendeAdresse3;
    }

    public String getEintrittskarteAbweichendeAdresse4() {
        return eintrittskarteAbweichendeAdresse4;
    }

    public void setEintrittskarteAbweichendeAdresse4(String eintrittskarteAbweichendeAdresse4) {
        this.eintrittskarteAbweichendeAdresse4 = eintrittskarteAbweichendeAdresse4;
    }

    public String getEintrittskarteAbweichendeAdresse5() {
        return eintrittskarteAbweichendeAdresse5;
    }

    public void setEintrittskarteAbweichendeAdresse5(String eintrittskarteAbweichendeAdresse5) {
        this.eintrittskarteAbweichendeAdresse5 = eintrittskarteAbweichendeAdresse5;
    }

    public String getVollmachtName() {
        return vollmachtName;
    }

    public void setVollmachtName(String vollmachtName) {
        this.vollmachtName = vollmachtName;
    }

    public String getVollmachtVorname() {
        return vollmachtVorname;
    }

    public void setVollmachtVorname(String vollmachtVorname) {
        this.vollmachtVorname = vollmachtVorname;
    }

    public String getVollmachtOrt() {
        return vollmachtOrt;
    }

    public void setVollmachtOrt(String vollmachtOrt) {
        this.vollmachtOrt = vollmachtOrt;
    }

    public String getZielOeffentlicheID() {
        return zielOeffentlicheID;
    }

    public void setZielOeffentlicheID(String zielOeffentlicheID) {
        this.zielOeffentlicheID = zielOeffentlicheID;
    }

    public boolean isUeberOeffentlicheID() {
        return ueberOeffentlicheID;
    }

    public void setUeberOeffentlicheID(boolean ueberOeffentlicheID) {
        this.ueberOeffentlicheID = ueberOeffentlicheID;
    }

    @Deprecated
    public String getEintrittskarteVersandart2String() {
        return eintrittskarteVersandart2String;
    }

    @Deprecated
   public void setEintrittskarteVersandart2String(String eintrittskarteVersandart2String) {
        this.eintrittskarteVersandart2String = eintrittskarteVersandart2String;
    }

    //	public int getEintrittskartePdfNr2() {
    //		return eintrittskartePdfNr2;
    //	}
    //
    //	public void setEintrittskartePdfNr2(int eintrittskartePdfNr2) {
    //		this.eintrittskartePdfNr2 = eintrittskartePdfNr2;
    //	}

    public String getEintrittskarteEmail2() {
        return eintrittskarteEmail2;
    }

    public void setEintrittskarteEmail2(String eintrittskarteEmail2) {
        this.eintrittskarteEmail2 = eintrittskarteEmail2;
    }

    public String getEintrittskarteAbweichendeAdresse12() {
        return eintrittskarteAbweichendeAdresse12;
    }

    public void setEintrittskarteAbweichendeAdresse12(String eintrittskarteAbweichendeAdresse12) {
        this.eintrittskarteAbweichendeAdresse12 = eintrittskarteAbweichendeAdresse12;
    }

    public String getEintrittskarteAbweichendeAdresse22() {
        return eintrittskarteAbweichendeAdresse22;
    }

    public void setEintrittskarteAbweichendeAdresse22(String eintrittskarteAbweichendeAdresse22) {
        this.eintrittskarteAbweichendeAdresse22 = eintrittskarteAbweichendeAdresse22;
    }

    public String getEintrittskarteAbweichendeAdresse32() {
        return eintrittskarteAbweichendeAdresse32;
    }

    public void setEintrittskarteAbweichendeAdresse32(String eintrittskarteAbweichendeAdresse32) {
        this.eintrittskarteAbweichendeAdresse32 = eintrittskarteAbweichendeAdresse32;
    }

    public String getEintrittskarteAbweichendeAdresse42() {
        return eintrittskarteAbweichendeAdresse42;
    }

    public void setEintrittskarteAbweichendeAdresse42(String eintrittskarteAbweichendeAdresse42) {
        this.eintrittskarteAbweichendeAdresse42 = eintrittskarteAbweichendeAdresse42;
    }

    public String getEintrittskarteAbweichendeAdresse52() {
        return eintrittskarteAbweichendeAdresse52;
    }

    public void setEintrittskarteAbweichendeAdresse52(String eintrittskarteAbweichendeAdresse52) {
        this.eintrittskarteAbweichendeAdresse52 = eintrittskarteAbweichendeAdresse52;
    }

    public String getVollmachtName2() {
        return vollmachtName2;
    }

    public void setVollmachtName2(String vollmachtName2) {
        this.vollmachtName2 = vollmachtName2;
    }

    public String getVollmachtVorname2() {
        return vollmachtVorname2;
    }

    public void setVollmachtVorname2(String vollmachtVorname2) {
        this.vollmachtVorname2 = vollmachtVorname2;
    }

    public String getVollmachtOrt2() {
        return vollmachtOrt2;
    }

    public void setVollmachtOrt2(String vollmachtOrt2) {
        this.vollmachtOrt2 = vollmachtOrt2;
    }

    @Deprecated
    public EclZugeordneteMeldungM getEclZugeordneteMeldungM() {
        return eclZugeordneteMeldungM;
    }

    @Deprecated
    public void setEclZugeordneteMeldungM(EclZugeordneteMeldungM eclZugeordneteMeldungM) {
        this.eclZugeordneteMeldungM = eclZugeordneteMeldungM;
    }

    public String getAnmeldeAktionaersnummer() {
        return anmeldeAktionaersnummer;
    }

    public void setAnmeldeAktionaersnummer(String anmeldeAktionaersnummer) {
        this.anmeldeAktionaersnummer = anmeldeAktionaersnummer;
    }

    public int getPersonNatJurOeffentlicheID() {
        return personNatJurOeffentlicheID;
    }

    public void setPersonNatJurOeffentlicheID(int personNatJurOeffentlicheID) {
        this.personNatJurOeffentlicheID = personNatJurOeffentlicheID;
    }

    public String getQuelle() {
        return quelle;
    }

    public void setQuelle(String quelle) {
        this.quelle = quelle;
    }

}
