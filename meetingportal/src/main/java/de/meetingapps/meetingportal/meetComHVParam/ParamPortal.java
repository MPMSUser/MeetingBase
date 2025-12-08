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
package de.meetingapps.meetingportal.meetComHVParam;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import de.meetingapps.meetingportal.meetComEntities.EclKontaktformularThema;
import de.meetingapps.meetingportal.meetComEntities.EclMenueEintrag;
import de.meetingapps.meetingportal.meetComEntities.EclPortalFunktion;
import de.meetingapps.meetingportal.meetComEntities.EclPortalPhase;
import de.meetingapps.meetingportal.meetComEntities.EclPortalUnterlagen;
import de.meetingapps.meetingportal.meetComEntities.EclVeranstaltungenQuittungElement;
import de.meetingapps.meetingportal.meetComEntities.EclVeranstaltungenVeranstaltung;
import de.meetingapps.meetingportal.meetComEntities.EclWortmeldetischAktion;
import de.meetingapps.meetingportal.meetComEntities.EclWortmeldetischStatus;
import de.meetingapps.meetingportal.meetComEntities.EclWortmeldetischStatusWeiterleitung;
import de.meetingapps.meetingportal.meetComEntities.EclWortmeldetischView;
import de.meetingapps.meetingportal.meetComKonst.KonstHinweisWeitere;

/*************************Allgemeine Konzepte zu den Portal-Parametern*****************************************************************
 * Eine Funktion ist sichtbar, wenn sie aktiv ist UND für die entsprechende Meldung (Gast oder Aktionär) die Berechtigung eingetragen ist.
 *
 * Berechtigung:
 * =============
 * Vordefinierte "Berechtigungsklassen":
 * -------------------------------------
 * Automatisch vergeben werden:
 * > Aktionär
 * > Angemeldeter Aktionär (Automatisch)
 * > Online-Teilnahme-Aktionär
 * > Insti (für diesen gelten automatisch Aktionär/angemeldeter Aktionär bzw. Online-TeilnahmeAktionär)
 * Beim Gast eingetragen werden muß:
 * > Gast1 bis Gast10 - es gilt dann jenachdem Gast mit oder ohne Online-Teilnahme
 *
 * Einzelberechtigungen je Gast
 * ----------------------------
 * Je Gast kann für jede Funktion einzeln angegeben werden, ob er dafür eine Berechtigung hat oder nicht. Diese Berechtigung gilt dann
 * unabhängig vom Status Online oder nicht. Nur gedacht für "Einzelpersonen", um Gastgruppen anderweitig nutzen zu können.
 *
 *
 * Eine Funktion ist aktiv wenn ...
 * ================================
 * Für die "Anmeldefunktionen" (Anmeldung, Weisung, ...) gilt:
 * -----------------------------------------------------------
 * > Aktiv wird über die Phasen gesetzt.
 *
 * Für die restlichen Funktionen gilt (Stream ...):
 * ------------------------------------------------
 * > Je Funktion wird gesetzt, ob "Aktiv" gesteuert wird über
 * 		> Phasen
 * 		> Eingetragene Zeit im "Termin" der jeweiligen Funktion
 */

public class ParamPortal implements Serializable {
    private static final long serialVersionUID = -8200899650941185697L;

    public ParamPortal() {
        eclPortalFunktion = new EclPortalFunktion[41];
        for (int i = 0; i < 41; i++) {
            eclPortalFunktion[i] = new EclPortalFunktion();
        }

        eclPortalPhase = new EclPortalPhase[21];
        for (int i = 0; i < 21; i++) {
            eclPortalPhase[i] = new EclPortalPhase();
        }

        inhaltsHinweiseTextDE=new String[10];
        inhaltsHinweiseTextEN=new String[10];
        inhaltsHinweiseAktiv=new boolean[10][6];

        for (int i=0;i<10;i++) {
            inhaltsHinweiseTextDE[i]="";
            inhaltsHinweiseTextEN[i]="";
            for (int i1=0;i1<6;i1++) {
                inhaltsHinweiseAktiv[i][i1]=false;
            }
        }


        konfRaumId=new String[2][6];
        for (int i=0;i<2;i++) {
            for (int i1=0;i1<6;i1++) {
                konfRaumId[i][i1]="";
            }
        }

        konfRaumBId=new String[2][6];
        for (int i=0;i<2;i++) {
            for (int i1=0;i1<6;i1++) {
                konfRaumBId[i][i1]="";
            }
        }

        konfRaumBIdPW=new String[2][6];
        for (int i=0;i<2;i++) {
            for (int i1=0;i1<6;i1++) {
                konfRaumBIdPW[i][i1]="";
            }
        }

        veranstaltungenAktivFuerGattung=new boolean[5];
        freiwilligeAnmeldungAktivFuerGattung=new boolean[5];
        for (int i=0;i<5;i++) {
            veranstaltungenAktivFuerGattung[i]=false;
            freiwilligeAnmeldungAktivFuerGattung[i]=false;
        }

    }

    /***************grundsätzlich angebotene Funktionalitäten (unabhängig, ob in jeweiliger Phase aktiv!)*********************/

    /**+++Portal allgemein+++*/

    /**Test-/Entwicklungsmodus. Werte addieren
     * 0=keinerlei Unterstützung
     * 1=über test=1 werden Textpflege-Buttons sowie Fehlermeldungen mit Nummern aktiviert (Standard)
     * 2=über zeit=TTMMJJJJHHMMSS kann die Startzeit aktiviert werden.
     */
    public int testModus = 1; //2276 tfTestModus

    /**++++Registrierung, Passwort etc.++++*/

    /**Eigenes Passwort vergeben
     * 1=wird optional angeboten
     * 2=ist zwangsweise, wenn Registrierung für elektronischen Einladungsversand
     */
    public int dauerhaftesPasswortMoeglich = 2;//157 OK

    /**1 => Registrierung für Email-Versand wird angeboten*/
    public int registrierungFuerEmailVersandMoeglich = 1;//154 OK

    /**Falls registrierungFuerEmailVersandMoeglich==1:
     * 0 oder 1 = Aktive Registrierung für E-Mail-Einladungsversand
     *          wegen Initialisierungsfehler derzeit sowohl 0 als auch 1 möglich!!!
     *          (in eclParamM wird 0 auf 1 umgesetzt)
     * 2 = Aktiver Widerspruch gegen E-Mail-Einladungsversand
     */
    public int emailVersandRegistrierungOderWiderspruch=1; //tfEmailVersandRegistrierungOderWiderspruch 2630
    
    /**1 => die E-Mail-Adresse aus dem Aktienregister wird als Zweit-EMail-Adresse
     * eingespielt.
     * Im Portal kann die Nutzung der Zweit-EMail-Adresse vom Aktionär deaktiviert werden.
     */
    public int emailVersandZweitEMailAusRegister=0; //2663
    
    
    /**1 => Adressänderung möglich (derzeit noch nicht unterstützt)*/
    public int adressaenderungMoeglich = 0;//241 OK

    /**Kommunikationssprache Auswahl in Portal*/
    public int kommunikationsspracheAuswahl = 1;//244 OK

    /**Publikationen - Hinweistext anbieten; Links werden dann in den Hinweistext übernommen*/
    public int publikationenAnbieten = 1; //245 OK

    /**Kontaktdetails (Telefon private/geschäftlich, Mobil, Fax) zum Eingeben anbieten*/
    public int kontaktDetailsAnbieten = 1;//246 OK

    /**Handhabung E-Mail-Adresse. 1= eine Email-Adresse darf nur eingegeben werden, wenn entweder Registrierung für
     * Email-Versand oder ein eigenes Passwort erfolgte.
     */
    public int emailNurBeiEVersandOderPasswort = 0; //250

    /**1 = Cookie-Hinweis wird beim Start des Portals einmal eingeblendet*/
    public int cookieHinweis=0; //2336 tfCookieHinweis

    /**=1 => im "normalen" HV-Portal sind keine Einstellungen möglich für Aktionäre;
     * für Gäste schon
     */
    public int inHVPortalKeineEinstellungenFuerAktionaere=0; //tfInHVPortalKeineEinstellungenFuerAktionaere Sonder 2580

    /**=1 => im "normalen" HV-Portal wird auf Registrierungsseite (nach Login)
     * für Aktionäre keine E-Mail-Adresse und keine Passwortänderung angeboten,
     * egal was sonst in Parametern steht. (gilt nicht für Gast-Kennungen)
     */
    public int inHVPortalKeineEmailUndKeinPasswortFuerAktionaere=0; //tfInHVPortalKeineEmailUndKeinPasswortFuerAktionaere Sonder 2581

    //tfReihenfolgeRegistrierung
    /**=1 => Maileingabe durch Serviceline ist möglich*/
    public int mailEingabeServiceline=0; //tfMailEingabeServiceline 2585

    /**+++Angebotene Willenserklärungen+++*/
    /**1 => SRV wird angeboten*/
    public int srvAngeboten = 1;//268 OK

    /**1 => Briefwahl wird angeboten*/
    public int briefwahlAngeboten = 1;//187 OK


    /**1 => Vollmacht Dritte wird angeboten*/
    public int vollmachtDritteAngeboten = 1;//188 OK

    /**1 => Vollmacht KIAV wird angeboten*/
    public int vollmachtKIAVAngeboten = 1;//189 tfVollmachtKIAVAngeboten

    /**1 => derzeit ist -l unabhängig von allem anderen -l im Portal keine Weisungserteilung,
     * Änderung, Stornierung möglich. Dient zum temporären Deaktivieren während der
     * Abstimmungsauswertung.
     */
    public int weisungenAktuellNichtMoeglich=0; //2490 tfWeisungAktuellNichtMoeglich
    public int weisungenAktuellNichtMoeglichAberBriefwahlSchon=0; //tfWeisungAktuellNichtMoeglichAberBriefwahlSchon 2648

    /**Portal ist für die jeweilige Gattung überhaupt aktiv
     * [gattung=0 bis 4]
     * */
    public int[] portalFuerGattungMoeglich = { 1, 0, 0, 0, 0 };//190, Offset 0 bis 4 OK

    /**SRV, briefwahl, KIAV für die jeweilige Gattung möglich, soweit überhaupt angeboten.
     * Wird nicht gepflegt, sondern gemäß aktiven Tagesordnungspunkten für Gattung automatisch
     * gefüllt
     * [gattung=0 bis 4]
     */
    public int[] stimmabgabeFuerGattungMoeglich = { 1, 0, 0, 0, 0 }; //191, Offset 0 bis 4

    /*AAAAA neuer Parameter Portal Logik Gastkarten*/
    /**0 => Gastkarte wird nicht angeboten im Portal
     * -1 => Gastkartenanzahl wird aus Feld "gruppe" im Aktienregister geholt
     * -2 => keine neuen Gastkarten ausstellbar, aber bestehende noch ausdruckbar
     * >0 => Gastkartenanzahl, die angefordert werden kann*/
    public int gastkartenAnforderungMoeglich = 0;//OK 174

    /**Öffentliche ID möglich*/
    public int oeffentlicheIDMoeglich = 0;//OK 175

    /**EK Vollmacht Dritte auch bei bereits ausgestellter EK 1=Ja 0=nein
     * 1 = eine Eintrittskarte Dritte kann auch dann ausgestellt werden (im Portal), wenn bereits eine
     * Eintrittskarte Selbst oder eine andere Eintrittskarte Dritte ausgestellt wurde
     * 0 = eine bereits vorliegende Eintrittskarte muß storniert werden, bevor eine weitere Eintrittskarte
     * ausgestellt werden kann.
     */
    public int zusaetzlicheEKDritteMoeglich = 1;//OK 10

    /**EK und SRV/Briefwahl/KIAV gleichzeitig) 1=Ja 0=nein
     * 1 = eine Eintrittskarte kann auch dann ausgestellt werden (im Portal), wenn bereits eine
     * Vollmacht an SRV oder Briefwahl oder KIAV erteilt wurde. Ebenso umgekehrt
     * 0 = eine bereits vorliegende Willenserklärung muß immer storniert werden werden, bevor eine andere abgegeben werden kann.
     * Einzige Ausnahme: Vollmacht an Dritte. Die geht immer :-)
     */
    public int ekUndWeisungGleichzeitigMoeglich = 1;//OK 11

    /**=1 => wenn SRV erteilt ist, kann zusätzlich Briefwahl erteilt werden. Sinnvoll, wenn SRV im Portal nicht änderbar
     * Achtung - SRV wird dann nur deaktiviert, und lebt nicht wieder auf - ggf. manuelle Überprüfung / Korrektur erforderlich!*/
    public int briefwahlZusaetzlichZuSRVMoeglich = 0; //2232

    /**=1 => wenn Briefwahl erteilt ist, kann zusätzlich SRV erteilt werden. Sinnvoll, wenn Briefwahl im Portal nicht änderbar
     * (oder wie z.B. bei Singulus Briefwahl als "Online-Abstimmung" zweckentfremdet wird).
     * Achtung - Briefwahl wird dann nur deaktiviert, und lebt nicht wieder auf - ggf. manuelle Überprüfung / Korrektur erforderlich!
     * AAAAA srvZusaetzlichZuBriefwahlMoeglich noch keine Oberfläche*/
    public int srvZusaetzlichZuBriefwahlMoeglich = 0; //2645

    /**0 => Vollmacht Dritte und andere Willenserklärung ist möglich, 1=nein*/
    public int vollmachtDritteUndAndereWKMoeglich=0; //tfVollmachtDritteUndAndereWKMoeglich 2337

    
    /**=0 => Standard; =1 => Briefwahl wird als Online-Stimmabgabe bewirkt:
     * > Briefwahl darf nicht icht storniert werden.
     * > Briefwahl-Button Ändern hat Sondernummer (Text 2305 statt 137)
     * 
     * AAAAA briefwahlAlsOnlineStimmabgabe noch keine Oberfläche
     */
    public int briefwahlAlsOnlineStimmabgabe=0; //2646
    
    /**Absoluter Spezialparameter. =1 => ein Portaluser fliegt nicht raus, wenn ein anderer für den Meldebestand
     * parallel was gemacht hat. darf nur in Extremsituationen auf 1 geschaltet werden!
     * 
     * AAAAA multiUserImPortalIgnorieren achtung Achtung achtung!
     */
    public int multiUserImPortalIgnorieren=0; //2647
    
    /**0=Stornierung bei Übernahme der Aktionärsrechte
     * 1=Der der Erteilt hat darf
     */
    public int handhabungWeisungDurchVerschiedene=0; //tfHandhabungWeisungDurchVerschiedene

    /**Falls 1, und zusatzfeld5 der Sammelkarte==1, dann dürfen Willenserklärungen
     * der so gekennzeichneten Sammelkarten über das Portal nicht geändert oder
     * widerrufen werden
     */
    public int sammelkartenFuerAenderungSperren=0; //tfSammelkartenFuerAenderungSperren 2491

    /**1=EK
     * 2=Briefwahl
     * 3=SRV
     * 4=VollmachtDritte
     * 5=KIV
     */
    public int erklAnPos1=1; //tfErklAnPos1 2492
    public int erklAnPos2=2; //tfErklAnPos2 2493
    public int erklAnPos3=3; //tfErklAnPos3 2494
    public int erklAnPos4=4; //tfErklAnPos4 2495
    public int erklAnPos5=5; //tfErklAnPos5 2496


    /**++++Angebotene Varianten bei EK-Ausstellung++++*/

    public int ekSelbstMoeglich = 0;//OK 176
    public int ekVollmachtMoeglich = 0;//OK 177
    public int ek2PersonengemeinschaftMoeglich = 0;//OK 178
    public int ek2MitOderOhneVollmachtMoeglich = 0;//OK 179
    public int ek2SelbstMoeglich = 0;//OK tfEk2SelbstMoeglich 180

    /*XXXXX-Parameter ekVersandFuerAlleImPortalAngeforderten*/
    /**0=Standard, Papierversand erfolgt nur wenn angefordert;
     * 1=Papierversand erfolgt auch für die "Selbstgedruckten" oder "Selbstgemailten"
     */
    public int ekVersandFuerAlleImPortalAngeforderten=0; //2659
    
    
    /**+++++Reine Anmeldung (ohne weitere Willenserklärungen) möglich*/
    public int anmeldenOhneWeitereWK = 0; //tfAnmeldenOhneWeitereWK 275

    /*+++++++Sonstiges Willenserklärung++++*/
    /**Nach Abgabe einer Weisung/Abstimmung gibt es die Möglichkeit,
     * eine Bestätigung der Abgabe (SRD II) auszugeben.
     * Addieren - 1 bei Briefwahl, 2 bei SRV
     */
    public int bestaetigungBeiWeisung = 0; //tfBestaetigungBeiWeisung 279


    /**Das BestätigungsPDF (SRDII), das als Weisungsbestätigung erzeugt wird,
     * wird mit oder ohne den einzelnen TOPe erzeugt
     */
    public int bestaetigungBeiWeisungMitTOP=0; //tfBestaetigungBeiWeisungMitTOP 2488

    //tfVollmachtDritteAngeboten
    /**=1 => auf Login-Seite ist der Vollmachtsnachweis aktiv.
     * Ist eigentlich hier eine Quick-and-Dirty-Lösung, da das ganze
     * Phasenabhängig sein sollte. Ist aber jetzt zu aufwändig.
     */
    /*XXX*/
    public int vollmachtsnachweisAufStartseiteAktiv=0; //tfVollmachtsnachweisAufStartseiteAktiv 2581
    
    /*AAAAA neuer Parameter Portal Logik bestaetigungStimmabgabeNachHV
     * 1 => nach der HV kann eine Stimmabgabebestätigung abgerufen werden - nur Hinweis darauf
     * 2 => Jetzt kann auch die Stimmabgabebestätigung abgerufen werden*/
    public int bestaetigungStimmabgabeNachHV=0; //tfBestaetigungStimmabgabeNachHV 2639

    /***************************Phasen*********************************************/
    /**Grundsätzlich frei verwendbar, aber vorgesehen sind:
     * 1 = Portal noch nicht in Betrieb - Phase vor Portal Start.
     * 2 = Reine Email-Registrierung - Phase vor Einladungsversand
     * 3 = Anmeldung möglich - Start Einladungsversand bis Letzter Anmeldetag
     * 4 = Anmeldung nicht mehr möglich, aber Rest bis erstes Ende (z.B. Schluß SRV)
     * 5 = erstes Ende bis zweites Ende (z.B. Briefwahl ändern noch möglich nach Schluß SRV - nicht im Standard)
     * 6 = zweites Ende bis drittes Ende (z.B. Vollmacht an Dritte auch noch während HV)
     * 7 = Nach der HV
     *
     * 11= Beginn Hotline
     * 12= Ende Hotline
     * 13= Beginn Streaming Test
     * 14= Beginn HV Streaming
     * 15= Beginn Fragen
     * 16= Ende Fragen
     */
    @Deprecated
    public int phasePortal = 3; //192

    /**[phase-Nummer=1 bis 20][Detaileinstellung, in der folgenden Reihenfolge, beginnend bei 1 bis 9]
     * 
     * Entfernt 05.02.2025*/
//    public int[][] phasenDetails = new int[21][20]; //201 bis 220; Offset 0 bis einschließlich 19
    /**++++Detail-Einstellungen, je Phase++++*/

    @Deprecated
    public int gewinnspielAktiv = 0; //155

    @Deprecated
    public int lfdHVPortalInBetrieb = 1; //501

    /**1=vor der HV, 2=nach der HV (Parameter Nr. 515)*/
    @Deprecated
    public int lfdVorDerHVNachDerHV = 0; //515

    @Deprecated
   public int lfdHVPortalErstanmeldungIstMoeglich = 1; //502
    @Deprecated
    public int lfdHVPortalEKIstMoeglich = 1; //503
    @Deprecated
    public int lfdHVPortalSRVIstMoeglich = 1; //504
    @Deprecated
    public int lfdHVPortalBriefwahlIstMoeglich = 1; //505
    @Deprecated
   public int lfdHVPortalKIAVIstMoeglich = 1; //506
    @Deprecated
    public int lfdHVPortalVollmachtDritteIstMoeglich = 1; //507

    //	public int lfdHVStreamIstMoeglich=1;
    @Deprecated
    public int lfdHVMitteilungIstMoeglich = 1; //2202
    @Deprecated
    public int lfdHVFragenStufe1IstMoeglich = 1; //2203
    @Deprecated
    public int lfdHVFragenStufe2IstMoeglich = 1; //2204
    @Deprecated
    public int lfdHVFragenStufe3IstMoeglich = 1; //2205
    @Deprecated
    public int lfdHVFragenStufe4IstMoeglich = 1; //2206 cbAfdHVFragenStufe4IstMoeglich
    @Deprecated
    public int lfdHVTeilnehmerverzIstMoeglich = 1; //2272 cbALfdHVTeilnehmerverzIstMoeglich
    @Deprecated
    public int lfdHVAbstimmungsergIstMoeglich = 1; //2247 cbALfdHVAbstimmungsergIstMoeglich

    /*????????????????????????????????????????????????????????????????????????????????????????????????????????????????*/

    /**++++++++Sonderphase++++++++++++*/
    /**Spezial-Parameter für ku178, nur ausgewertet wenn varianteDialogablauf=1*/

    /**>0 => angezeigt und möglich, und gibt Position in Menü an
     * <0 => angezeigt aber nicht möglich, und gibt Position in Menü an
     * 0 => weder angezeigt noch möglich*/
    public int lfdHVDialogVeranstaltungenInMenue = 0; //tfDialogveranstaltung Sonder 254 Aktuell: Dialogveranstaltung früher: lfdHVBeiratswahlInMenue
    public int lfdHVBeiratswahlInMenue = 0; //tfBeiratswahl Sonder 281
    public int lfdHVGeneralversammlungInMenue = 0; //tfGeneralversammlung Sonder 261
    public int lfdHVGeneralversammlungBriefwahlInMenue = 0; //tfGeneralversammlungBriefwahl Sonder 264
    public int lfdHVUnterlagenInMenue = 0; //tfUnterlagen Sonder 262 früher: lfdHVDialogVeranstaltungenInMenue
    public int lfdHVEinstellungenInMenue = 0; //tfEinstellungen Sonder 263
    public int lfdHVGeneralversammlungTeilnahmeInMenue = 0; //tfGeneralversammlungTeilnahme Sonder 277
    public int lfdHVGeneralversammlungTeilnahmeGast = 0; //tfGeneralversammlungGast 280

    //public int lfdHVDialogVeranstaltungenInMenue = 0;

    //public int lfdMitgliederDatenInMenue = 0; //Nicht mehr verwendet. Zukünftig: Generalversammlung Briefwahl


    /*******************Dialogveranstaltungen*********************************/
    /**Steuert, ob Dialogveranstaltung grundsätzlich aktiviert sind. Damit ist
     * die Anmeldung zu Dialogveranstaltungen im Register-Portal grundsätzlich möglich.
     *
     * Soll die Dialogveranstaltung auch im HV-Portal aktiv sein, ist zusätzlich
     * der entsprechende Eintrag im Sonder-Menü zu aktivieren.
     */
    public int dialogveranstaltungAktiv=0; //tfDialogveranstaltungAktiv Sonder 2576

    public List<EclVeranstaltungenVeranstaltung> veranstaltungsListe=null; //dbVeranstaltungenAktion
    public List<EclVeranstaltungenQuittungElement> veranstaltungenQuittungListe=null; //dbVeranstaltungenQuittungElement
    
    /********************Datum und Termine***********************************/

    /**Datumsformat Deutsch für Anzeige Portal.
     * 1= TT.MM.JJJJ
     * 2= TT. Monat JJJJ
     */
    public int datumsformatDE = 1; //193 OK

    /**Datumsformat Englisch für Anzeige Portal.
     * 1= MM/TT/JJJJ
     * 2 = 12 October 2018
     * 3 = October 12, 2018
     * 4 = 12th October 2018
     * 5 = October 12th 2018
     */
    public int datumsformatEN = 1; //194 OK

    /**Datum in der Form TT.MM.JJJJ. Aktuell nur verwendet für die Anzeige "Sie sind mit Datum .... im Aktienregister eingetragen.*/
    public String letzterAktienregisterUpdate = ""; //153 OK

    /*******************************Grundsätzlicher Dialogablauf***************************************************/

    /** 1=Bestaetigen-Maske in Dialogen anzeigen, 0=nein
     *
     * Wirkt bei:
     * > Vollmacht/Weisung Erteilen
     * > Weisungen ändern
     * > Vollmacht an Dritte
     *
     * Wirkt nicht (da kein Bestätigungsmaske sinnvoll) bei:
     * > Stornierungen Vollmacht/Weisung
     * > Stornierung Vollmacht/Dritte
     *
     * Wirkt nicht bei Eintrittskartenausstellung, da dort immer fester Dialog erforderlich (z.B. wg. Email etc.)
     * */
    public int bestaetigenDialog = 0;//OK 1

    /** 1=Quittungs-Maske in Dialogen anzeigen, 0=nein
     * Wirkt nicht bei Eintrittskartenausstellung, da dort immer fester Dialog erforderlich (z.B. wg. Email etc.)
     * */
    public int quittungDialog = 0;//OK 2

    /** Darstellung der Sprachumschaltung (wenn englisch aktiv): 1=Texte, 2=Flaggen*/
    public int artSprachumschaltung = 1; //OK 242

    /**0 = Standardablauf für HV (mit Anmeldung usw.)
     * 1 = Ablauf für ku178 (Anmeldung optionaler freiwilliger Lauf)
     */
    public int varianteDialogablauf = 0; // 251

    
    /**Temporär für ku178*/
    public int ku178SepaIstAktiv=0; //2640 - keine Oberfläche
    public boolean liefereku178SepaIstAktiv() {
        return ku178SepaIstAktiv==1;
    }
    
    /***************Steuerung Login, Passwort, Passwortvergessen***********************************************/

    /** 1=Passwort ist Case-Sensitiv, 0=nein */
    public int passwortCaseSensitiv = 0;//OK 3

    /** Mindestlaenge Passwort */
    public int passwortMindestLaenge = 8;//OK 4

    /**
     * 0 = kein Passwort-Vergessen-Verfahren
     * 1 = Passwort-Vergessen für Email-Registrierung "automatisch" durchführen - schließt 2 aus
     * 2 = Passwort-Vergessen für alle "manuell" (d.h. Aufgabe an Anmeldestelle) - schließt 1 und 4 aus
     * 4 = Passwort-Vergessen für Nicht-Email-Registrierte "manuell"
     * 8 = Für App: es wird ein Code statt einem Link verschickt und dann direkt in der App eingegeben (gilt auch für E-Mail-Bestätigung!)
     * (Falls nur 1 gesetzt ist, dann kann ein neues Passwort nur für Aktionäre mit hinterlegter
     * Email-Adresse angefordert werden)
     */
    public int verfahrenPasswortVergessen = 1;//OK 181

    /**Spezial-Passwortverfahren für ku178;
     * 0 = Standard
     * 1 = Mitgliedsnummer und Geburtsdatum, sowie Box "immer per Post verschicken"
     */
    public int verfahrenPasswortVergessenAblauf = 0; //250

    /**Wenn E-Mail hinterlegt, dann ist Zustellung auch per Post in Auswahl (1)
     * oder automatisch nur E-Mail (0)
     */
    public int verfahrenPasswortVergessenBeiEmailHinterlegtAuchPost=1; //2527 tfVerfahrenPasswortVergessenBeiEmailHinterlegtAuchPost

    /**=1 => es ist kein Login möglich.
     * Achtung - zusätzlich auch eine Phase aktivieren, bei der keine Willenserklärungen möglich sind - wg. bereits eingeloggter Usern!
     */
    public int loginGesperrt = 0;
    /**LEN max. 200, Lang, 11*/
    public String loginGesperrtTextDeutsch = "Der Internetservice steht voraussichtlich ab 03.04.2019, 18.00 Uhr wieder zur Verfügung. Wir bitten um Ihr Verständnis.";
    /**LEN max. 200, Lanf 12*/
    public String loginGesperrtTextEnglisch = "This internet service is expected to be available again from 3 April 2019, 6 p.m. We ask for your understanding.";

    /**IP-Tracking beim Login Portal (für ku178 ...) aktiv*/
    public int loginIPTrackingAktiv = 0; //253

    /**E-Mails mit "Links" enthalten einen Link (1) oder einen Code (2)
     * zum Übertragen in Portal
     */
    public int linkEmailEnthaeltLinkOderCode=1; //tfLinkEmailEnthaeltLinkOderCode 2528

    /**1=Ja; Ja sollte nur gesetzt werden, wenn EMail-Bestätigungsmails einen Code enthalten (keinen Link)*/
    /*XXX*/
    public int emailBestaetigenIstZwingend=0; //tfEmailBestaetigenIstZwingend 2574

    /**Bestätigungen von dieser Mailadresse versenden:
     * 1=aktionaersportal2023 (Standard)
     * 2=portal2023 (Standard)
     * 3=ku178
     * 4=onlineservice2023 (Standard)
     * 5=aktionaersportal2024 ("Next Year")
     */
    public int absendeMailAdresse = 1; //260

    /**=1 => Passwortanforderungen ohne E-Mail-Adresse, die per Post verschickt werden müssen,
     * müssen vorher individuell geprüft werden
     */
    public int passwortPerPostPruefen = 1; //266

    /**0 = normales Login-Verfahren, d.h. immer Kennung/Passwort
     * 1 = Inhaberaktien login (geplant! Noch nicht implementiert)
     *
     * 99=Spezial ku216, Kennung / Name / Datum
     */
    public int loginVerfahren = 0; //tfLoginVerfahren 278
    
    /**Captcha-Steuerung:
     * 0 = kein Captcha
     * 1 = immer Captcha
     * 2 ... = ab dem x.ten Fehlversuch Captcha
     */
    public int captchaVerwenden=0;//tfCaptchaVerwenden 282
    
    /**Zeit-Verzoegerung bei Fehlerhaftem Login
     * 0 = aus
     * >0 = ein ab dem jeweils x.ten Versuch wird die Zeitverzögerung um loginVerzoegerungSekunden erhöht
     */
    public int loginVerzoegerungAbVersuch=0;//tfLoginVerzoegerungAbVersuch 283
    public int loginVerzoegerungSekunden=0;//tfLoginVerzoegerungSekunden 287
    
    /**Alternative Login-Kennung verwenden
     * 0=nein
     * 1=ja, zusätzlich zur normalen Login-Kennung
     * 2=ja, ausschließlich die alternative Login-Kennung verwenden (gilt für Gäste und Aktionäre!)
     */
    public int alternativeLoginKennung=0;//tfAlternativeLoginKennung 284
    
    /**Werden beim Einlesen aus tbl_eindeutigeKennungen gefüllt. Beim Speichern der Parameter
     * wird überprüft, ob Vorhanden und VorhandenVorAenderung abweicht - dann werden ggf. weitere eindeutige
     * Kennungen angelegt. 
     */
    public int anzahlEindeutigeKennungenVorhanden=0; //tfAnzahlEindeutigeKennungenVorhanden aus tbl_eindeutigeKennungen
    public int anzahlEindeutigeKennungenVorhandenVorAenderung=0;  //aus tbl_eindeutigeKennungen
    public int anzahlEindeutigeKennungenVerbraucht=0; //lblAnzahlEindeutigeKennungenVerbraucht aus tbl_eindeutigeKennungen
    
    /*AAAAA neuer Parameter Portal Logik teilnehmerKannSichWeitereKennungenZuordnen*/
    /**Teilnehmer kann sich über eine Zuordnungsfunktion im Portal weitere Kennungen zuordnen
     * 0=nein
     * 1=ja
     * zur Funktionsweise: 
     * > loggt sich ein zugeordneter Teilnehmer direkt ein, oder wird ein bereits zugeordneter Teilnehmer
     * einer anderen Kennung zugeordnet, dann wird die bisherige Zuordnung wieder rückgängig gemacht (und ggf.
     * die "übergeordnete" Kennug auch ausgeloggt)
     * > Beim Login mit zugeordneten Kennungen werden auch die zugeordneten Kennungen anderweitig ausgeloggt.
     */
    public int teilnehmerKannSichWeitereKennungenZuordnen=0;//tfTeilnehmerKannSichWeitereKennungenZuordnen 285
    
    /**E-Mail-Bestätigung für alle Aktionen (unabhängig von der SRD-2-Funktion) grundsätzlich zulassen
     * 0=nein
     * 1=ja (ohne PDF bei Weisungen)
     * 2=ja (mit PDF bei Weisungen)
     */
    public int bestaetigungPerEmailUeberallZulassen=0;//tfBestaetigungPerEmailUeberallZulassen 286
    
    /**Aufbereiten der Login-Kennung nach der Eingabe
     * 0=Standard
     *      S-Kennungen, A-Kennungen, oder Buchstabe enthalten: keine Aufbereitung
     *      Wenn keine Buchstaben enthalten:
     *          Inhaberaktien: führende 0 abschneiden
     *          Namensaktien: wenn kleiner als Aktionärsnummer: führende 0 bis Länge ergänzen, und dann noch 0 anfügen.
     * 1=
     *      S- und A-Kennungen keine Aufbereitung;
     *      Ansonsten: wie bei 0 aufbereiten, auch wenn Buchstaben enthalten.
     * 2=keine Aufbereitung
     *      
     */
    public int kennungAufbereiten=0; //tfKennungAufbereiten 288
    
    /*AAAAA neuer Parameter Portal Logik kennungAufbereitenFuerAnzeige*/
    /**Aufbereiten der Login-Kennung für die Anzeige*/
    public int kennungAufbereitenFuerAnzeige=0; //tfKennungAufbereitenFuerAnzeige 289
    

    /************************Logout-Verfahren**************************/

    /**Wenn nicht leer, dann wird diese Ziel-Maske beim Logout aufgerufen, und gleichzeitig die Session beendet*/
    public String logoutZiel = ""; //tfLogoutZiel 20

    /*****************Steuerung Erst-Registrierung / Kommunikationseinstellung / Einstellungen***************/

    /** Hinweis Aktionärsportal in Portal*/
    @Deprecated
    public int bestaetigungSeparateAktionaersPortalHinweiseErforderlich = 0; //160

    /** Hinweis Aktionärsportal in Portal
     *  1=Muß bei Erstanmeldung bestätigt werden
     *  2=Muß bei jeder Anmeldung neu bestätigt werden
     * */
    public int bestaetigenHinweisAktionaersportal = 0;//OK 8

    /** Hinweis HVportal in Portal*/
    @Deprecated
    public int bestaetigungSeparateHVPortalHinweiseErforderlich = 0; //161

    /** Hinweis HVportal in Portal
     *  1=Muß bei Erstanmeldung bestätigt werden
     *  2=Muß bei jeder Anmeldung neu bestätigt werden
     * */
    public int bestaetigenHinweisHVportal = 1;//OK 9

    public int separateDatenschutzerklaerung = 0;//OK 158

    /**Bitweise für weitere Hinweistexte.
     * Siehe auch KonstHinweisWeitere:
     * 1=Permanent-Portal Hinweis 1 Sonder tfBestaetigenHinweisP1
     * 2=Permanent-Portal Hinweis 2 Sonder tfBestaetigenHinweisP2
     */
    public int bestaetigenHinweisWeitere=0; //2529

    /**pOffset=1, 2, 4, ... gemäß KonstHinweisWeitere*/
    public int liefereBestaetigenHinweisWeiter(int pOffset) {
        int ergebnis=0;
        switch (pOffset) {
        case 1:ergebnis= bestaetigenHinweisWeitere & 1;break;
        case 2:ergebnis= bestaetigenHinweisWeitere & 2;break;
        }
        if (ergebnis>0) {return 1;}
        return 0;
    }


    /**pOffset=1, 2, 4, ... gemäß KonstHinweisWeitere; pWert=0 oder 1 */
    public void schreibeBestaetigenHinweisWeitere(int pOffset, int pWert) {
        int gesamt=KonstHinweisWeitere.MAX_WERT-pOffset;
        bestaetigenHinweisWeitere=bestaetigenHinweisWeitere & gesamt;
        if (pWert>0) {
            switch (pOffset) {
            case 1: bestaetigenHinweisWeitere = bestaetigenHinweisWeitere | 1;
            case 2: bestaetigenHinweisWeitere = bestaetigenHinweisWeitere | 2;
            }
        }
    }

    @Deprecated
    public int separateNutzungshinweise = 0; //159

    /**0 = Erstregistrierung nur nach Anhaken des Feldes
     * 1 = Erstregistrierung wird immer abgefragt*/
    @Deprecated
    public int erstregistrierungImmer = 0; //16

    public int separateTeilnahmebedingungenFuerGewinnspiel = 0;//OK 156

    /**Hilfe/Kontakt-Fenster anzeigen*/
    public int kontaktFenster = 1; //OK 247

    /**Emittentenspezifisches Impressum in App*/
    public int impressumEmittent = 0; //OK 249

    public int bestaetigungsseiteEinstellungen = 0; //258

    /**0 = Einladungsregistrierung, Passwort, E-Mail, Hinweis
     * 1 = Einladungsregistrierung, E-Mail, Passwort, Hinweis
     */
    public int reihenfolgeRegistrierung = 0; //265

    /****************Steuerung Stimmabgabe********************************************/

    /** NichtMarkiert speichern als 1=Ja,2=Nein,3=Enthaltung,0=nichts */
    public int pNichtmarkiertSpeichernAls = 0; //OK 5

    public int gesamtMarkierungJa = 0; //162
    public int gesamtMarkierungNein = 0; //163
    public int gesamtMarkierungEnthaltung = 0; //185
    public int gesamtMarkierungImSinne = 0; //164
    public int gesamtMarkierungGegenSinne = 0; //165

    public int gesamtMarkierungAllesLoeschen = 0; //196

    public int markierungJa = 0; //166
    public int markierungNein = 0; //167
    public int markierungEnthaltung = 0; //168
    public int markierungLoeschen = 0;  //169

    /**=1 Weisungen für Gegenanträge werden unterstützt*/
    public int gegenantraegeWeisungenMoeglich = 0; //OK 197

    /**Text zu "Gegenanträgen"
     * 0 = keinerlei Text dazu anzeigen
     * 1 = "Derzeit liegen keine Gegenanträge vor"
     * 2 = "Gegenanträge finden Sie ...."
     * 3 = "Gegenanträge können Sie im folgenden unterstützen"
     * */
    public int gegenantragsText = 0; //OK 199

    /**Wenn 1, dann muß zur Stimmabgabe ein Haken explizit gesetzt werden*/
    public int checkboxBeiSRV = 0; //cbCheckboxBeiSRV 255
    public int checkboxBeiKIAV = 0; // 256
    public int checkboxBeiBriefwahl = 0; // 257

    /*AAAAA neuer Parameter Portal Logik BestätigungBeiVollmacht*/
    /*wenn 1, dann muß bei jeder Vollmacht an Dritte ein Haken explizit gesetzt werden*/
    public int checkboxBeiVollmacht=0; //cbCheckboxBeiVollmacht 290
    
    /**Wenn 1, dann muß auf der Stimmabgabequittung (Weisung, SRV, Briefwahl) eine von zwei Select-Boxen
     * angewählt werden, sonst kommt man nicht weiter.
     * 
     * Ist in den XHTMLs nur für ku178 implementiert.
     * 
     * Muß bei allen Kunden zwingend auf 0 stehen.
     */
    public int jnAbfrageBeiWeisungQuittung=0; //cbJnAbfrageBeiWeisungQuittung 2629
    
    /************Datenaufbereitung Aktionärsdaten***********************************/

    /**Definiert, inwieweit Zeilen Aktionär/Eingeloggte Person mit/ohne Anrede gefüllt werden sollen:
     * 0 => Anrede nie aufnehmen
     * 1 => Anrede immer aufnehmen
     * 2 => Anrede nur für natürliche Personen aufnehmen
     */
    @Deprecated
    public int personenAnzeigeAnredeMitAufnahmen = 0;//OK 151

    /**Wenn im Feld Postfach im Aktienregister der Text "Postfach" bereits enthalten ist, dann
     * diesen Parameter auf 0 setzen. Bei 1 wird der Text "Postfach" vom Programm für die
     * Bildschirmaufbereitung noch mit aufgenommen.
     */
    @Deprecated
    public int textPostfachMitAufnahmen = 1; //152

    /**=0 => die Aktien-/Stimmenanzahl wird nicht mit angezeigt. Sonder-Parameter für Vereine etc. mit One-Man-One-Vote*/
    public int anzeigeStimmen = 1; //Spezial tfAnzeigeStimmen 274

    public int anzeigeStimmenKennung=0; //tfAnzeigeStimmenKennung 2489

    /*********************Technik*******************************/

    /**1 => Timeout wird für diesen Mandant auf "lang" (8 Stunden) gestellt*/
    public int timeoutAufLang = 0; //tfTimeoutAufLang - 2248

    /**1 => Websockets werden genutzt*/
    public int websocketsMoeglich = 0; //tfWebsocketsMoeglich - 2277

    /**1 => Doppel-Login wird verhindert*/
    public int doppelLoginGesperrt = 0; //tfDoppelLoginGesperrt - 2278

    /*************Virtuelle HV****************************/

    /**[0] unbenutzt; [KonstPortalFunktion]*/
    public EclPortalFunktion[] eclPortalFunktion = null; //90001 bis 90040

    /**[0] unbenutzt; [Phasennummer=1 bis 20, entspricht KonstTermine 101 bis 120]*/
    public EclPortalPhase[] eclPortalPhase = null; //90201 bis 90220

    public List<EclPortalUnterlagen> eclPortalUnterlagen = new LinkedList<EclPortalUnterlagen>(); //91001 bis 92000

    /**++++++++++++++Online-Teilnahme+++++++++++*/

    //	public int onlineTeilnahmeAngeboten=0; //tfOnlineTeilnahmeAngeboten
    //	public int onlineTeilnahmeAktiv=0; //tfOnlineTeilnahmeAktiv

    /**1 => spezieller Ablauf für Vereins-ähnliche Versammlungen:
     * > Anmeldung aller Mitglieder zwingend vorhanden (aber nur eine)
     * > keine Vollmachten, kein SRV, keine Briefwahl
     * > keine Zusammenfassung / Zuordnung von anderen Teilnehmern zur eigene Kennung
     *
     * D.h.: 1 Kennung = 1 Aktienregister (oder Gast) = 1 Anmeldung
     *
     * Wurde eingeführt, um Datenbankzugriff drastisch zu minimieren (also z.B. keine Statusabfrage),
     * bzw. Performance massiv zu erhöhen
     * */
    public int nurRawLiveAbstimmung = 0; //Spezial tfNurRawLiveAbstimmung 2275

    public int onlineTeilnehmerAbfragen = 0; //2279 tfOnlineTeilnehmerAbfragen

    public int onlineTeilnahmeSeparateNutzungsbedingungen = 0; //2289 tfOnlineTeilnahmeSeparateNutzungsbedingungen
    
    /*AAAAA neuer Parameter Portal Oberfl*/
    /**0 = kein separater Haken
     * 1 = Bestätigungshaken (an selber Stelle wie separate Nutzungsbedingungen - siehe dort*/
    public int onlineTeilnahmeTeilnehmerNameBestaetigen =0; //2638

    /**0 / 1 : generell möglich (im Auswahlmenü)
     * 0 / 2 : nur in Briefwahlmenü möglich (nicht unterstützt wenn separates Gastmenü!, d.h. Zugang als Aktionär/Gast nur möglich, wenn Briefwahl erteilt)
     * 		Funktioniert nur eingeschränkt, sprich wenn er teils Briefwahl teils nicht dann darf er als Gast teilnehmen
     *
     */
    public int onlineTeilnahmeAktionaerAlsGast = 1; //2292 tfOnlineTeilnahmeAktionaerAlsGast

    public int onlineTeilnahmeGastInSeparatemMenue = 0; //Spezial 2290 tfOnlineTeilnahmeGastInSeparatemMenue
    public int onlineTeilnahmeNurFuerFreiwilligAngemeldete = 0; //Spezial 2291 tfOnlineTeilnahmeNurFuerFreiwilligAngemeldete

    public int onlineAbstimmungBerechtigungSeparatPruefen = 0; //Spezial 2588 tfOnlineAbstimmungBerechtigungSeparatPruefen

    /** =1 => Mitgliederversammlung findet als Hybridveranstaltung statt. Muß auf HV-Präsenzserver gesetzt werden!*/
    public int hybridTeilnahmeAktiv=0; //Spezial 2589 tfHybridTeilnahmeAktiv

    /*++++++++++++++++++Veranstaltungen++++++++++++++++++++*/
    /**=0 => es wird nichts verschickt
     * =1 => Bestätigungsmail (ohne Anhang) verschicken
     * =2 => Bestätigungsmail (mit Anhang) verschicken
     * =3 => Bestätigungsmail mit individuellem Anhang verschicken ("Eintrittskarte")
     */
    /*AAAAA ku217*/
    public int veranstaltungMailVerschicken=0; //tfVeranstaltungMailVerschicken 2534

    /**=1 => Personenzahl wird abgefragt*/
    public int veranstaltungPersonenzahlEingeben=0; //tfVeranstaltungPersonenzahlEingeben 2535

    /**=1 => Mehrfachauswahl möglich je Gruppe*/
    public int veranstaltungMehrfachAuswaehlbarJeGruppe=0; //tfVeranstaltungMehrfachAuswaehlbarJeGruppe 2536

    /*AAAAA ku217*/
    /*[gattung-1]; gehört eigentlich auch in den Veranstaltungstable. Hier nur generelle Auswählbarkeit*/
    public boolean[] veranstaltungenAktivFuerGattung=null; //2617, Offset 0 bis 4, cbVeranstaltungenAktivFuerGattung

    /**++++++++++++++++++++FreiwilligeAnmeldung+++++++++++++++++++++++++++++++
     * Parameter für die "freiwillige" Anmeldung von Vereinen u.ä.*/

     /**Wenn auf 1, dann wird bei der Anmeldung unterschieden zwischen Präsenz und Online-Teilnahme*/
     public int freiwillingAnmeldenPraesenzOderOnline=0; //tfFreiwillingAnmeldenPraesenzOderOnline 2533

     /**Wenn auf 1, dann können freiwillig angemeldete eine EK ausdrucken*/
     public int freiwilligeAnmeldungEKDruckMoeglich=0; //tfFreiwilligeAnmeldungEKDruckMoeglich Sonder 2582

     /**=1 => es gibt in der Anmeldemaske keine Möglichkeit, sich an oder abzumelden, sondern nur ein Formular auszudrucken*/
     public int freiwilligeAnmeldungNurPapier=0; //tfFreiwilligeAnmeldungNurPapier Sonder 2587

     /**=1 => im Portal können bei gesetzlichen Vertretern und Vollmachten Name/Ort mit eingegeben werden.
      * =2 => sind Pflichtfelder
      */
     public int freiwilligeAnmeldungMitVertretereingabe=0; // tfFreiwilligeAnmeldungMitVertretereingabe, Sonder 2590

     /**=1 => Zwei Personen
      * =2 => Zwei Personen plus Gastkarte für das Kind
      */
     public int freiwilligeAnmeldungZweiPersonenFuerMinderjaehrigeMoeglich=0; // tfFreiwilligeAnmeldungZweiPersonenFuerMinderjaehrigeMoeglich, Sonder 2591

     /*AAAAA ku217*/
     /*[gattung-1]; gehört eigentlich auch in den Veranstaltungstable. Hier nur generelle Auswählbarkeit*/
     public boolean[] freiwilligeAnmeldungAktivFuerGattung=null; //2618 tfFreiwilligeAnmeldungAktivFuerGattung

    /**++++++VideoStream+++*/
    //	public int streamAngeboten=0;
    @Deprecated
    public int streamNurFuerAngemeldete = 0; //2226

    /**0=noch keiner eingetragen; 2=Haus Bay. Wirtschaft; 3=ku152*/
    public int streamAnbieter = 0; //2211

    /**Parameter 1*/
    public String streamLink = ""; //17 Lang

    /**Parameter 2*/
    public String streamID = ""; //18 Lang

    @Deprecated
    public int streamTestlinkWirdAngeboten = 0; //2212

    /**Backup-Stream*/
    public int streamAnbieter2 = 0; //2261
    public String streamLink2 = ""; //47 Lang
    public String streamID2 = ""; //48 Lang


    /*++++++++++++++++++++++Videobotschaften - Technik++++++++++++++++++++++++++*/
    public String shrinkFormat=""; //tfShrinkFormat //2571
    public String shrinkAufloesung=""; //tfShrinkAufloesung //2572

    /*+++++++++++++++++++++++++++++Konferenzing++++++++++++++++++++++++++*/

    public String konfSessionId="";  //Lang-Parameter 112 tfKonfSessionId

    public String konfEventId=""; //Lang 140 tfKonfEventId
    /**[0]=Test, [1]=Reden*/
    public int[] konfRaumAnzahl= {0, 0}; //2612, 2613 tfKonfRaumAnzahlTest, tfKonfRaumAnzahlReden
    
    /*[0][0..5] und [1][0..5]*/
    public String[][] konfRaumId=null; //Lang 141,142,143 und 144,145,146; 154,155,156 und 157,158,159 tfKonfRaumIdT1 ..3, tfKonfRaumIdR1 ..3

    public String konfBackupServer=""; //Lang 147 tfKonfBackupServer

    /**analog konfRaumId aber für Backup Räume*/
    public String[][] konfRaumBId=null; //Lang 148,149,150 und 151,152,153; 160,161,162 und 163,164,165  tfKonfRaumIdBT1 ..3, tfKonfRaumIdBR1 ..3
    public String[][] konfRaumBIdPW=null; // [0][x] 2621, 2622, 2623; [1][x]: 2624, 2625, 2626 enthält die Passwörter für die Backup-Räume
    public int konfBackupAktiv=0; //2620

    /*TODO VidKonf Globale Testräume nutzen*/
    public int konfGlobaleTestraeumeNutzen=0; //2614 tfKonfGlobaleTestraeumeNutzen


    /**++++++++Fragen++++++++*/

    /**Separate Maske: Personenabfrage "Sind Sie" Ja/Nein
     * Wenn Nein:
     *      Wenn Eingabe von Name aktiv, dann zu Name
     *      Sonst Fehlerseite dass nicht möglich
     */
    public int fragenStellerAbfragen = 0; //2223 tfFragenStellerAbfragen

    /**Der Name des Stellers wird nochmals explizit abgefragt (addieren)
     * 0=Nein, 1=Ja, 2=Eingabe zwingend erforderlich (natürliche Personen), 4=Eingabe zwingend erforderlich (juristische Personen), 8=Name bei natürlichen Personen vorschlagen*/
    /*TODO VidKonf Name Steller abfragen*/
    public int fragenNameAbfragen=0; //2294 tfFragenNameAbfragen

    /**Abfragen der Kontaktdaten, z.b. Mail, Telefonnummer o.ä.
     * 0=nein, 1=ja, 2=Eingabe zwingend
     */
    public int fragenKontaktdatenAbfragen=0; //2295 tfFragenKontaktdatenAbfragen

    /**1 => die in Einstellungen des Logins abgespeicherte Mail-Adresse (falls ...) wird
     * in das Eingabefeld übernommen und damit vorgeschlagen (bei Wortmeldungen unbedingt
     * auf 0 setzen!*/
    public int fragenKontaktdatenEMailVorschlagen=0; // 2296 tfFragenKontaktdatenEMailVorschlagen

    /**Abfragen der Kontaktdaten - nur Telefon
     * 0=nein, 1=ja, 2=Eingabe zwingend
     */
    public int fragenKontaktdatenTelefonAbfragen=0; //2595 tfFragenKontaktdatenTelefonAbfragen


    /**0=nein, 1=ja, 2=zwingend*/
    public int fragenKurztextAbfragen=0; //2297 tfFragenKurztextAbfragen

    /**0=nein, 1=ja, 2=zwingend*/
    public int fragenTopListeAnbieten=0;   //2298 tfFragenTopListeAnbieten

    /**0=nein, 1=ja, 2=zwingend*/
    public int fragenLangtextAbfragen=0;   //2299 tfFragenLangtextAbfragen

    /**0=nein, 1=ja*/
    public int fragenZurueckziehenMoeglich=0;   //2300 tfFragenZurueckziehenMoeglich

    public int fragenLaenge = 10000; //2216 tfFragenLaenge
    public int fragenAnzahlJeAktionaer = 1000; //2215 tfFragenAnzahlJeAktionaer

    /**Über & verknüpft: 1=auch Gemeinschaftskonten/Personengemeinschaften, 2=auch Juristische Personen*/
    public int fragenStellerZulaessig = 0; //2239 tfFragenStellerZulaessig

    /**=1 => während der HV "nur Rückfragen" ermöglichen (beeinflußt Texte; Rückfragen-Funktion muß
     * separat als Portalfunktion aktiviert werden*/
    public int fragenRueckfragenErmoeglichen=0; //2564 tfFragenRueckfragenErmoeglichen

    /**1 = Mail-Versand automatisch bei Speichern*/
    public int fragenMailBeiEingang = 0; //2250 tfFragenMailBeiEingang
    public String fragenMailVerteiler1 = ""; //Lang 24 tfFragenMailVerteiler1
    public String fragenMailVerteiler2 = ""; //Lang 25 tfFragenMailVerteiler2
    public String fragenMailVerteiler3 = ""; //Lang 26 tfFragenMailVerteiler3

    /**1=angehakt, 0=nicht angehakt*/
    public int fragenHinweisVorbelegenMit=0; //2565 tfFragenHinweisVorbelegenMit
    /**0=kein Hinweis zum Bestätigen; 1=Freiwillig, 2=zwingend*/
    public int fragenHinweisGelesen=0; //2555 tfFragenHinweisGelesen

    public int fragenZurueckziehenMoeglichNurWennAktiv=1; //2631
    
    public int fragenExternerZugriff = 0; //2241 tfFragenExternerZugriff

    /*XXXXX--Parameter fragenTextInOberflaeche* */
    /**Text, der in der Oberfläche für diese Mitteilungsart angezeigt wird*/
    public String fragenTextInOberflaecheLang="Fragen"; //2653
    public String fragenTextInOberflaecheKurz="Fragen"; //2654
    
    @Deprecated
    public int fragenAngeboten = 0; //2227
    @Deprecated
    public int fragenNurFuerAngemeldete = 0; //2228


    /**von 1 bis fragenBisAktien, von fragenBisAktien bis fragenAbAktien, ab fragenAbAktien*/
    @Deprecated
    public int fragenBisAktien = 1; //2213
    @Deprecated
    public int fragenAbAktien = 1; //2214

    @Deprecated
    public int fragenAnzahlStufe2 = 999999; //2217
    @Deprecated
    public int fragenLaengeStufe2 = 999999; //2218
    @Deprecated
    public int fragenAnzahlStufe3 = 999999; //2219
    @Deprecated
    public int fragenLaengeStufe3 = 999999; //2220
    @Deprecated
    public int fragenAnzahlStufe4 = 999999; //2221
    @Deprecated
    public int fragenLaengeStufe4 = 999999; //2222

    /**+++++++++++++++Wortmeldungen++++++++++++++*/

    /**Addieren: 1 = per Telefon, 2=per Videokonferenz*/
    public int wortmeldungArt=0; //tfWortmeldungArt 2552

    /**Separate Maske: Personenabfrage "Sind Sie" Ja/Nein
     * Wenn Nein:
     *      Wenn Eingabe von Name aktiv, dann zu Name
     *      Sonst Fehlerseite dass nicht möglich
     */
   public int wortmeldungStellerAbfragen = 0; //2281 tfWortmeldungStellerAbfragen

   /**Der Name des Stellers wird nochmals explizit abgefragt (addieren)
    * 0=Nein, 1=Ja, 2=Eingabe zwingend erforderlich (natürliche Personen), 4=Eingabe zwingend erforderlich (juristische Personen), 8=Name bei natürlichen Personen vorschlagen*/
   public int wortmeldungNameAbfragen = 0; //2282 tfWortmeldungNameAbfragen

   /**Abfragen der Kontaktdaten, z.b. Mail, Telefonnummer o.ä.
    * 0=nein, 1=ja, 2=Eingabe zwingend
    */
  public int wortmeldungKontaktdatenAbfragen=0; //2301 tfWortmeldungKontaktdatenAbfragen
   /**1 => die in Einstellungen des Logins abgespeicherte Mail-Adresse (falls ...) wird
    * in das Eingabefeld übernommen und damit vorgeschlagen (bei Wortmeldungen unbedingt
    * auf 0 setzen!*/
   public int wortmeldungKontaktdatenEMailVorschlagen=0; //2302 tfWortmeldungKontaktdatenEMailVorschlagen

   /**Abfragen der Kontaktdaten - nur Telefon
    * 0=nein, 1=ja, 2=Eingabe zwingend
    */
   public int wortmeldungKontaktdatenTelefonAbfragen=0; //2596 tfWortmeldungKontaktdatenTelefonAbfragen


   /**0=nein, 1=ja, 2=zwingend*/
   public int wortmeldungKurztextAbfragen = 0; //2283 tfWortmeldungKurztextAbfragen

   /**0=nein, 1=ja, 2=zwingend*/
   public int wortmeldungTopListeAnbieten=0;   //2303 tfWortmeldungTopListeAnbieten

   /**0=nein, 1=ja, 2=zwingend*/
   public int wortmeldungLangtextAbfragen = 0; //2284 tfWortmeldungLangtextAbfragen

   /**0=nein, 1=ja*/
   public int wortmeldungZurueckziehenMoeglich=0;   //2304 tfWortmeldungZurueckziehenMoeglich

   public int wortmeldungLaenge = 10000; //2288 tfWortmeldungLaenge
   public int wortmeldungAnzahlJeAktionaer = 1000; //2287 tfWortmeldungAnzahlJeAktionaer

   /**Über & verknüpft: 1=auch Gemeinschaftskonten, 2=auch Juristische Personen*/
   public int wortmeldungStellerZulaessig = 0; //2286 tfWortmeldungStellerZulaessig

   /**Wortmeldeliste wird angezeigt
    * =0 => keine Anzeige
    * >1 => diese Anzahl der nächsten Wortmeldungen wird maximal angezeigt*/
   public int wortmeldungListeAnzeigen = 0; //2285 tfWortmeldungListeAnzeigen

   /**Anzeigen für Versammlungsleiter*/
   public int wortmeldungVLListeAnzeigen=0; //2526 tfWortmeldungVLListeAnzeigen

    /**1 = Mail-Versand automatisch bei Speichern*/
    public int wortmeldungMailBeiEingang = 0; //2280 tfWortmeldungMailBeiEingang
    public String wortmeldungMailVerteiler1 = ""; //Lang 69 tfWortmeldungMailVerteiler1
    public String wortmeldungMailVerteiler2 = ""; //Lang 70 tfWortmeldungMailVerteiler2
    public String wortmeldungMailVerteiler3 = ""; //Lang 71 tfWortmeldungMailVerteiler3

    /**1=angehakt, 0=nicht angehakt*/
    public int wortmeldungHinweisVorbelegenMit=0; //2566 tfWortmeldungHinweisVorbelegenMit
    /**0=kein Hinweis zum Bestätigen; 1=Freiwillig, 2=zwingend*/
    public int wortmeldungHinweisGelesen=0; //2556 tfWortmeldungHinweisGelesen

    /*AAAAA neuer Parameter Portal Oberfl*/
    public int wortmeldungZurueckziehenMoeglichNurWennAktiv=0; //2632

    /**Die konfigurierten und einzeln aktivierten Inhaltshinweise sind:
     * 0=nicht aktiv
     * 1=aktiv, aber keiner zwingend
     * 2=aktiv, mindestens einer muß angehakt werden
     */
    public int wortmeldungInhaltsHinweiseAktiv=0; //2627 tfWortmeldungInhaltsHinweiseAktiv

    /**0=nein; 1=Ja (1 Versuch), 2=Ja (2 Versuche)*/
    /*TODO VidKonf 2 Runden nur bei Parametereinstellung*/
    public int wortmeldungTestDurchfuehren=0; //2610 tfWortmeldungTestDurchfuehren
    /**0=nein, 1=Ja*/
    /*TODO VidKonf  2 Runden nur bei Parametereinstellung*/
    public int wortmeldungRedeAufrufZweitenVersuchDurchfuehren=0; //2611 tfWortmeldungRedeAufrufZweitenVersuchDurchfuehren

    /**0=nein, 1=Ja*/
    public int wortmeldungNachTestManuellInRednerlisteAufnehmen=0; //2615 tfWortmeldungNachTestManuellInRednerlisteAufnehmen

    /**1 => es ist nur eine einzige offene Wortmeldung gleichzeitig zulässig. Aktionär kann solange keine weitere
     * Wortmeldung eingeben, solange seine erste Wortmeldung nicht eingegeben ist.
     */
    public int wortmeldungNurEineOffeneZulaessig=0; //2644
   
    /*XXXXX-Parameter wortmeldungTextInOberflaeche* */
    /**Text, der in der Oberfläche für diese Mitteilungsart angezeigt wird*/
    public String wortmeldungTextInOberflaecheLang="Wortmeldungen"; //2660
    public String wortmeldungTextInOberflaecheKurz="Wortmeldungen"; //2661

    //tfWortmeldungNachTestManuellInRednerlisteAufnehmen

    /*+++++++++++++++++++++++++++++Wortmeldetisch+++++++++++++++++++++++++++++++++++++++++*/
    public int wortmeldetischSetNr=1; //2619 tfWortmeldetischSetNr

    /**
     * TODO Parameter in DB Eintragen und in Meetingclient Umsetzen
     */
    public String schriftgroesseVersammlunsleiterView = "30"; //2628 tfSchriftgroesseVersammlunsleiterView

    /**Die folgenden Parameter werden aus separate Tables (wortmeldetisch*) erzeugt*/
    public EclWortmeldetischStatus[] wortmeldetischStatusArray=null; //separate Table
    public EclWortmeldetischView[] wortmeldetischViewArray=null; //separate Table
    public List<EclWortmeldetischStatusWeiterleitung> wortmeldetischStatusWeiterleitung=null; //separate Table
    public List<EclWortmeldetischAktion> wortmeldetischAktion=null; //separate Table

    /**+++++Widersprüche++++*/

    /**Separate Maske: Personenabfrage "Sind Sie" Ja/Nein
     * Wenn Nein:
     *      Wenn Eingabe von Name aktiv, dann zu Name
     *      Sonst Fehlerseite dass nicht möglich
     */
    public int widerspruecheStellerAbfragen = 0; //2229 tfWiderspruecheStellerAbfragen

    /**Der Name des Stellers wird nochmals explizit abgefragt (addieren)
     * 0=Nein, 1=Ja, 2=Eingabe zwingend erforderlich (natürliche Personen), 4=Eingabe zwingend erforderlich (juristische Personen), 8=Name bei natürlichen Personen vorschlagen*/
    /*TODO VidKonf Name abfragen Parameter*/
    public int widerspruecheNameAbfragen=0; //2305 tfWiderspruecheNameAbfragen

    /**Abfragen der Kontaktdaten, z.b. Mail, Telefonnummer o.ä.
     * 0=nein, 1=ja, 2=Eingabe zwingend
     */
    public int widerspruecheKontaktdatenAbfragen=0; //2306 tfWiderspruecheKontaktdatenAbfragen

    /**1 => die in Einstellungen des Logins abgespeicherte Mail-Adresse (falls ...) wird
     * in das Eingabefeld übernommen und damit vorgeschlagen (bei Wortmeldungen unbedingt
     * auf 0 setzen!*/
    public int widerspruecheKontaktdatenEMailVorschlagen=0; //2307 tfWiderspruecheKontaktdatenEMailVorschlagen

    /**Abfragen der Kontaktdaten - nur Telefon
     * 0=nein, 1=ja, 2=Eingabe zwingend
     */
    public int widerspruecheKontaktdatenTelefonAbfragen=0; //2597 tfWiderspruecheKontaktdatenTelefonAbfragen



    /**0=nein, 1=ja, 2=zwingend*/
    public int widerspruecheKurztextAbfragen=0; //2308 tfWiderspruecheKurztextAbfragen

    /**0=nein, 1=ja, 2=zwingend*/
    public int widerspruecheTopListeAnbieten=0;   //2309 tfWiderspruecheTopListeAnbieten

    /**0=nein, 1=ja, 2=zwingend*/
    public int widerspruecheLangtextAbfragen=0;   //2310 tfWiderspruecheLangtextAbfragen

    /**0=nein, 1=ja*/
     public int widerspruecheZurueckziehenMoeglich=0;   //2311 tfWiderspruecheZurueckziehenMoeglich

    public int widerspruecheLaenge = 10000; //2208 tfAntraegeLaenge
    public int widerspruecheAnzahlJeAktionaer = 1000; //2207 tfWiderspruecheAnzahlJeAktionaer

    /**Über & verknüpft: 1=auch Gemeinschaftskonten, 2=auch Juristische Personen*/
    public int widerspruecheStellerZulaessig = 0; //2238 tfWiderspruecheStellerZulaessig

    /**1 = Mail-Versand automatisch bei Speichern*/
    public int widerspruecheMailBeiEingang = 0; //2240 tfWiderspruecheMailBeiEingang
    public String widerspruecheMailVerteiler1 = ""; //Lang 21 tfWiderspruecheMailVerteiler1
    public String widerspruecheMailVerteiler2 = ""; //Lang 22 tfWiderspruecheMailVerteiler2
    public String widerspruecheMailVerteiler3 = ""; //Lang 23 tfWiderspruecheMailVerteiler3

    /**1=angehakt, 0=nicht angehakt*/
    public int widerspruecheHinweisVorbelegenMit=0; //2567 tfWiderspruecheHinweisVorbelegenMit
    /**0=kein Hinweis zum Bestätigen; 1=Freiwillig, 2=zwingend*/
    public int widerspruecheHinweisGelesen=0; //2557 tfWiderspruecheHinweisGelesen

    /*AAAAA neuer Parameter Portal Oberfl*/
    public int widerspruecheZurueckziehenMoeglichNurWennAktiv=0; //2633

    
    /*XXXXX-Parameter widerspruecheTextInOberflaeche* */
    /**Text, der in der Oberfläche für diese Mitteilungsart angezeigt wird*/
    public String widerspruecheTextInOberflaecheLang="Widersprüche"; //2657
    public String widerspruecheTextInOberflaecheKurz="Wider-Spr.";   //2658

    
    /**=1 => der MitteilungsButton ist auch sichtbar, wenn Mitteilungen gerade inaktiv
     * (aber grundsätzlich angeboten)*/
    @Deprecated
    public int mitteilungsButtonImmerSichtbar = 0; //2249 tfMitteilungsButtonImmerSichtbar

    @Deprecated
    public int mitteilungenAngeboten = 0; //2224

    @Deprecated
    public int mitteilungenDialogTopAngeboten = 0; //2230
    @Deprecated
    public int mitteilungenTextAngeboten = 0; //2231



    @Deprecated
    public int mitteilungNurFuerAngemeldete = 0; //2209
    /**0=für alle; 1=nur nach Stimmabgabe; 2=für alle, aber mit Warnung falls keine Stimmabgabe
     */
    @Deprecated
    public int mitteilungNurMitAbgegebenerStimme = 0; //2210



    /**+++++Anträge++++*/

    /**Separate Maske: Personenabfrage "Sind Sie" Ja/Nein
     * Wenn Nein:
     *      Wenn Eingabe von Name aktiv, dann zu Name
     *      Sonst Fehlerseite dass nicht möglich
     */
    public int antraegeStellerAbfragen = 0; //2312 tfAntraegeStellerAbfragen

    /**Der Name des Stellers wird nochmals explizit abgefragt (addieren)
     * 0=Nein, 1=Ja, 2=Eingabe zwingend erforderlich (natürliche Personen), 4=Eingabe zwingend erforderlich (juristische Personen), 8=Name bei natürlichen Personen vorschlagen*/
    /*TODO VidKonf */
    public int antraegeNameAbfragen=0; //2313 tfAntraegeNameAbfragen

    /**Abfragen der Kontaktdaten, z.b. Mail, Telefonnummer o.ä.
     * 0=nein, 1=ja, 2=Eingabe zwingend
     */
    public int antraegeKontaktdatenAbfragen=0; //2314 tfAntraegeKontaktdatenAbfragen

    /**1 => die in Einstellungen des Logins abgespeicherte Mail-Adresse (falls ...) wird
     * in das Eingabefeld übernommen und damit vorgeschlagen (bei Wortmeldungen unbedingt
     * auf 0 setzen!*/
    public int antraegeKontaktdatenEMailVorschlagen=0; //2315 tfAntraegeKontaktdatenEMailVorschlagen

    /**Abfragen der Kontaktdaten - nur Telefon
     * 0=nein, 1=ja, 2=Eingabe zwingend
     */
    /*TODO VidKonf - Bisher nicht in Datenbank*/
    public int antraegeKontaktdatenTelefonAbfragen=0; //tfAntraegeKontaktdatenTelefonAbfragen

    /**0=nein, 1=ja, 2=zwingend*/
    public int antraegeKurztextAbfragen=0; //2316 tfAntraegeKurztextAbfragen

    /**0=nein, 1=ja, 2=zwingend*/
    public int antraegeTopListeAnbieten=0;   //2317 tfAntraegeTopListeAnbieten

    /**0=nein, 1=ja, 2=zwingend*/
    public int antraegeLangtextAbfragen=0;   //2318 tfAntraegeLangtextAbfragen

    /**0=nein, 1=ja*/
    public int antraegeZurueckziehenMoeglich=0;   //2319 tfAntraegeZurueckziehenMoeglich

    public int antraegeLaenge = 10000; //2320 tfAntraegeLaenge
    public int antraegeAnzahlJeAktionaer = 1000; //2321 tfAntraegeAnzahlJeAktionaer

    /**Über & verknüpft: 1=auch Gemeinschaftskonten, 2=auch Juristische Personen*/
    public int antraegeStellerZulaessig = 0; //2322 tfAntraegeStellerZulaessig

    /**1 = Mail-Versand automatisch bei Speichern*/
    public int antraegeMailBeiEingang = 0; //2323 tfAntraegeMailBeiEingang
    public String antraegeMailVerteiler1 = ""; //Lang 72 tfAntraegeMailVerteiler1
    public String antraegeMailVerteiler2 = ""; //Lang 73 tfAntraegeMailVerteiler2
    public String antraegeMailVerteiler3 = ""; //Lang 74 tfAntraegeMailVerteiler3

    /**1=angehakt, 0=nicht angehakt*/
    public int antraegeHinweisVorbelegenMit=0; //2568 tfAntraegeHinweisVorbelegenMit
    /**0=kein Hinweis zum Bestätigen; 1=Freiwillig, 2=zwingend*/
    public int antraegeHinweisGelesen=0; //2558 tfAntraegeHinweisGelesen

    /*AAAAA neuer Parameter Portal Oberfl*/
    public int antraegeZurueckziehenMoeglichNurWennAktiv=0; //2634

    /*XXXXX--Parameter antraegeTextInOberflaeche* */
    /**Text, der in der Oberfläche für diese Mitteilungsart angezeigt wird*/
    public String antraegeTextInOberflaecheLang="Antrag nach § 131 Abs. 4"; //2649
    public String antraegeTextInOberflaecheKurz="Antrag § 131 4";    //2650

    /**+++++Sonstige Mitteilungen++++*/

    /**Separate Maske: Personenabfrage "Sind Sie" Ja/Nein
     * Wenn Nein:
     *      Wenn Eingabe von Name aktiv, dann zu Name
     *      Sonst Fehlerseite dass nicht möglich
     */
    public int sonstMitteilungenStellerAbfragen = 0; //2324 tfSonstMitteilungenStellerAbfragen

    /**Der Name des Stellers wird nochmals explizit abgefragt (addieren)
     * 0=Nein, 1=Ja, 2=Eingabe zwingend erforderlich (natürliche Personen), 4=Eingabe zwingend erforderlich (juristische Personen), 8=Name bei natürlichen Personen vorschlagen*/
    /*TODO VidKonf */
    public int sonstMitteilungenNameAbfragen=0; //2325 tfSonstMitteilungenNameAbfragen

    /**Abfragen der Kontaktdaten, z.b. Mail, Telefonnummer o.ä.
     * 0=nein, 1=ja, 2=Eingabe zwingend
     */
    public int sonstMitteilungenKontaktdatenAbfragen=0; //2326 tfSonstMitteilungenKontaktdatenAbfragen

    /**1 => die in Einstellungen des Logins abgespeicherte Mail-Adresse (falls ...) wird
     * in das Eingabefeld übernommen und damit vorgeschlagen (bei Wortmeldungen unbedingt
     * auf 0 setzen!*/
    public int sonstMitteilungenKontaktdatenEMailVorschlagen=0; //2327 tfSonstMitteilungenKontaktdatenEMailVorschlagen

    /**Abfragen der Kontaktdaten - nur Telefon
     * 0=nein, 1=ja, 2=Eingabe zwingend
     */
    /*TODO VidKonf */
    public int sonstMitteilungenKontaktdatenTelefonAbfragen=0; //2598 tfSonstMitteilungenKontaktdatenTelefonAbfragen


    /**0=nein, 1=ja, 2=zwingend*/
    public int sonstMitteilungenKurztextAbfragen=0; //2328 tfSonstMitteilungenKurztextAbfragen

    /**0=nein, 1=ja, 2=zwingend*/
    public int sonstMitteilungenTopListeAnbieten=0;   //2329 tfSonstMitteilungenTopListeAnbieten

    /**0=nein, 1=ja, 2=zwingend*/
    public int sonstMitteilungenLangtextAbfragen=0;   //2330 tfSonstMitteilungenLangtextAbfragen

    /**0=nein, 1=ja*/
    public int sonstMitteilungenZurueckziehenMoeglich=0;   //2331 tfSonstMitteilungenZurueckziehenMoeglich

    public int sonstMitteilungenLaenge = 10000; //2332 tfSonstMitteilungenLaenge
    public int sonstMitteilungenAnzahlJeAktionaer = 1000; //2333 tfSonstMitteilungenAnzahlJeAktionaer

    /**Über & verknüpft: 1=auch Gemeinschaftskonten, 2=auch Juristische Personen*/
    public int sonstMitteilungenStellerZulaessig = 0; //2334 tfSonstMitteilungenStellerZulaessig

    /**1 = Mail-Versand automatisch bei Speichern*/
    public int sonstMitteilungenMailBeiEingang = 0; //2335 tfSonstMitteilungenMailBeiEingang
    public String sonstMitteilungenMailVerteiler1 = ""; //Lang 75 tfSonstMitteilungenMailVerteiler1
    public String sonstMitteilungenMailVerteiler2 = ""; //Lang 76 tfSonstMitteilungenMailVerteiler2
    public String sonstMitteilungenMailVerteiler3 = ""; //Lang 77tfSonstMitteilungenMailVerteiler3

    /**1=angehakt, 0=nicht angehakt*/
    public int sonstMitteilungenHinweisVorbelegenMit=0; //2569 tfSonstMitteilungenHinweisVorbelegenMit
    /**0=kein Hinweis zum Bestätigen; 1=Freiwillig, 2=zwingend*/
    public int sonstMitteilungenHinweisGelesen=0; //2559 tfSonstMitteilungenHinweisGelesen

    /*AAAAA neuer Parameter Portal Oberfl*/
    public int sonstMitteilungenZurueckziehenMoeglichNurWennAktiv=0; //2635

    /*XXXXX--Parameter sonstMitteilungenTextInOberflaeche* */
    /**Text, der in der Oberfläche für diese Mitteilungsart angezeigt wird*/
    public String sonstMitteilungenTextInOberflaecheLang="Antrag nach § 131 Abs. 5"; //2655
    public String sonstMitteilungenTextInOberflaecheKurz="Antrag § 131 5";   //2656

    /**+++++Botschaften++++*/

    /**Separate Maske: Personenabfrage "Sind Sie" Ja/Nein
     * Wenn Nein:
     *      Wenn Eingabe von Name aktiv, dann zu Name
     *      Sonst Fehlerseite dass nicht möglich
     */
    public int botschaftenStellerAbfragen = 0; //2537 tfBotschaftenStellerAbfragen

    /**Der Name des Stellers wird nochmals explizit abgefragt (addieren)
     * 0=Nein, 1=Ja, 2=Eingabe zwingend erforderlich (natürliche Personen), 4=Eingabe zwingend erforderlich (juristische Personen), 8=Name bei natürlichen Personen vorschlagen*/
    /*TODO VidKonf */
    public int botschaftenNameAbfragen=0; //2538 tfBotschaftenNameAbfragen

    /**Abfragen der Kontaktdaten, z.b. Mail, Telefonnummer o.ä.
     * 0=nein, 1=ja, 2=Eingabe zwingend
     */
    public int botschaftenKontaktdatenAbfragen=0; //2539 tfBotschaftenKontaktdatenAbfragen

    /**1 => die in Einstellungen des Logins abgespeicherte Mail-Adresse (falls ...) wird
     * in das Eingabefeld übernommen und damit vorgeschlagen (bei Wortmeldungen unbedingt
     * auf 0 setzen!*/
    public int botschaftenKontaktdatenEMailVorschlagen=0; //2540 tfBotschaftenKontaktdatenEMailVorschlagen

    /**Abfragen der Kontaktdaten - nur Telefon
     * 0=nein, 1=ja, 2=Eingabe zwingend
     */
    /*TODO VidKonf */
    public int botschaftenKontaktdatenTelefonAbfragen=0; //2599 tfBotschaftenKontaktdatenTelefonAbfragen

    /**0=nein, 1=ja, 2=zwingend*/
    public int botschaftenKurztextAbfragen=0; //2541 tfBotschaftenKurztextAbfragen

    /**0=nein, 1=ja, 2=zwingend*/
    public int botschaftenTopListeAnbieten=0;   //2542 tfBotschaftenTopListeAnbieten

    /**0=nein, 1=ja, 2=zwingend*/
    public int botschaftenLangtextAbfragen=0;   //2543 tfBotschaftenLangtextAbfragen

    /**1 => es kann nur entweder Langtext oder Datei eingereicht werden*/
    public int botschaftenLangtextUndDateiNurAlternativ=0; //2575 tfBotschaftenLangtextUndDateiNurAlternativ 2575

    /**0=nein, 1=ja*/
    public int botschaftenZurueckziehenMoeglich=0;   //2544 tfBotschaftenZurueckziehenMoeglich

    public int botschaftenLaenge = 10000; //2554 tfBotschaftenLaenge

    public int botschaftenAnzahlJeAktionaer = 1000; //2545 tfBotschaftenAnzahlJeAktionaer

    /**Über & verknüpft: 1=auch Gemeinschaftskonten, 2=auch Juristische Personen*/
    public int botschaftenStellerZulaessig = 0; //2546 tfBotschaftenStellerZulaessig

    /**1 = Mail-Versand automatisch bei Speichern*/
    public int botschaftenMailBeiEingang = 0; //2547 tfBotschaftenMailBeiEingang
    public String botschaftenMailVerteiler1 = ""; //Lang 113 tfBotschaftenMailVerteiler1
    public String botschaftenMailVerteiler2 = ""; //Lang 114 tfBotschaftenMailVerteiler2
    public String botschaftenMailVerteiler3 = ""; //Lang 115 tfBotschaftenMailVerteiler3

    /**1=angehakt, 0=nicht angehakt*/
    public int botschaftenHinweisVorbelegenMit=0; //2570 tfBotschaftenHinweisVorbelegenMit
    /**0=kein Hinweis zum Bestätigen; 1=Freiwillig, 2=zwingend*/
    public int botschaftenHinweisGelesen=0; //2560 tfBotschaftenHinweisGelesen

    /*AAAAA neuer Parameter Portal Oberfl*/
    public int botschaftenZurueckziehenMoeglichNurWennAktiv=0; //2636

    /**=1 => Voranmeldung erforderlich*/
    public int botschaftenVoranmeldungErforderlich; //2561 tfBotschaftenVoranmeldungErforderlich

    /**1 = möglich*/
    public int botschaftenVideo=0; //2548 tfBotschaftenVideo

    /**[1] bis 15 - jeweiliges Format ist zulässig*/
    public String[] botschaftenVideoZusatz= {"","MPEG","AVI-Typ1","AVI-Typ2","WMV","MOV","MP4","","","","","","","","",""}; //Nicht in DB, fest codiert
    public int[] botschaftenVideoFormate= {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}; //2549, Offset 1 bis 15, tfBotschaftenVideoFormate

//    /**1 = möglich*/
//    public int botschaftenText=0; //tfBotschaftenText

    /**1 = möglich*/
    public int botschaftenTextDatei=0; //2562 tfBotschaftenTextDatei

    /**[1] bis 15 - jeweiliges Format ist zulässig*/
    public String[] botschaftenTextZusatz= {"","TXT","PDF","DOCX","PPTX","XLSX","CSV","ODT","","","","","","","",""};
    public int[] botschaftenTextFormate= {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}; //2551, Offset 1 bis 15, tfBotschaftenTextFormate

    /**Länge in MB*/
    public int botschaftenVideoLaenge = 4096; //2553 tfBotschaftenVideoLaenge
//    /**Länge in Byte*/
//    public int botschaftenTextLaenge = 10000; //tfBotschaftenTextLaenge
    /**Länge in Byte*/
    public int botschaftenTextDateiLaenge=10000000; //2563 tfBotschaftenTextDateiLaenge

    /*XXXXX--Parameter botschaftenTextInOberflaeche* */
    /**Text, der in der Oberfläche für diese Mitteilungsart angezeigt wird*/
    public String botschaftenTextInOberflaecheLang="Stellungnahme"; //2651
    public String botschaftenTextInOberflaecheKurz="Stell.Nah."; //2652

    
    /*+++++Inhalts-Hinweise++++++++++++++++*/

    /**[0 bis 9] - benannt 1 bis 10*/
    public String[] inhaltsHinweiseTextDE=null; //Lang 120 bis 129
    public String[] inhaltsHinweiseTextEN=null; //Lang 130 bis 139
    /**[][Fragen, Wortmeldungen, Widersprüche, Anträge, Sonst. Mitteilungen, Botschaften]*/
    public boolean[][] inhaltsHinweiseAktiv=null; //[2600 bis 2609][Offset 0 bis 5]


    /**++++*Teilnehmerverzeichnis++++++++++*/
    @Deprecated
    public int teilnehmerverzAngeboten = 0; //2233 tfTeilnehmerverzAngeboten
    /**0=Beginnend bei 0 (Teilnehmerverzeichnis, Nachträge) oder bei 1 (Verzeichnis 1, Verzeichnis 2, ...)*/
    public int teilnehmerverzBeginnendBei = 0; //2293 tfTeilnehmerverzBeginnendBei, 2293
    public int teilnehmerverzZusammenstellung = 0; //2234 tfTeilnehmerverzZusammenstellung
    /**-1=noch keines vorhanden; 0=Erstpräsenz; >0 =Nachtragsnummer*/
    public int teilnehmerverzLetzteNr = -1; //2235 tfTeilnehmerverzLetzteNr

    /*+++++Abstimmungsergebnis+++++++++++++*/
//    public int abstimmungsergAngeboten = 0; //tfAbstimmungsergAngeboten
    /**-1=noch keines vorhanden; >0 =lfd nummer*/
    public int abstimmungsergLetzteNr = -1; //2237 tfAbstimmungsergLetzteNr

    /*+++++++++++Virtuelle HV Neu+++++++++++++*/
    /**0 => Aktionär wird zum Menü geleitet, keine automatische Zuschaltung
     * 1 => Aktionär wird sofort nach dem Login zugeschaltet
     * 2 => Abfrage nach Login wenn Zuschaltung möglich, ob Zuschaltung erfolgen soll*/
    /*AAAAA neuer Parameter Portal Logik zuschaltungHVAutomatischNachLogin*/
    public int zuschaltungHVAutomatischNachLogin=0; //tfZuschaltungHVAutomatischNachLogin 2616
    
    /*AAAAA neuer Parameter Portal Logik zuschaltungHVStreamAutomatischStarten*/
    /**0 => kein Autostawrt
     * 1 => Autostart
     */
    public int zuschaltungHVStreamAutomatischStarten=0; //tfZuschaltungHVStreamAutomatischStarten 2637

    /*++++++++++++Unterlagen einsehen++++++++++++++++++++*/
    @Deprecated
    public int unterlagenAngeboten = 0;//tfUnterlagenAngeboten

    @Deprecated
    public int unterlage1Aktiv = 0;//tfUnterlage1Aktiv
    @Deprecated
    public String unterlageButton1DE = ""; //tfUnterlageButton1DE
    @Deprecated
    public String unterlageButton1EN = ""; //tfUnterlageButton1EN

    @Deprecated
    public int unterlage2Aktiv = 0;
    @Deprecated
    public String unterlageButton2DE = "";
    @Deprecated
    public String unterlageButton2EN = "";

    @Deprecated
    public int unterlage3Aktiv = 0;
    @Deprecated
    public String unterlageButton3DE = "";
    @Deprecated
    public String unterlageButton3EN = "";

    @Deprecated
    public int unterlage4Aktiv = 0;
    @Deprecated
    public String unterlageButton4DE = "";
    @Deprecated
    public String unterlageButton4EN = "";

    @Deprecated
    public int unterlage5Aktiv = 0;
    @Deprecated
    public String unterlageButton5DE = "";
    @Deprecated
    public String unterlageButton5EN = "";

    @Deprecated
    public int unterlage6Aktiv = 0;
    @Deprecated
    public String unterlageButton6DE = "";
    @Deprecated
    public String unterlageButton6EN = "";

    @Deprecated
    public int unterlage7Aktiv = 0;
    @Deprecated
    public String unterlageButton7DE = "";
    @Deprecated
    public String unterlageButton7EN = "";

    @Deprecated
    public int unterlage8Aktiv = 0;
    @Deprecated
    public String unterlageButton8DE = "";
    @Deprecated
    public String unterlageButton8EN = "";

    @Deprecated
    public int unterlage9Aktiv = 0;
    @Deprecated
    public String unterlageButton9DE = "";
    @Deprecated
    public String unterlageButton9EN = "";

    @Deprecated
    public int unterlage10Aktiv = 0;
    @Deprecated
    public String unterlageButton10DE = "";
    @Deprecated
    public String unterlageButton10EN = "";

    @Deprecated
    public int unterlage11Aktiv = 0;
    @Deprecated
    public String unterlageButton11DE = "";
    @Deprecated
    public String unterlageButton11EN = "";

    @Deprecated
    public int unterlage12Aktiv = 0;
    @Deprecated
    public String unterlageButton12DE = "";
    @Deprecated
    public String unterlageButton12EN = "";

    @Deprecated
    public int unterlage13Aktiv = 0;
    @Deprecated
    public String unterlageButton13DE = "";
    @Deprecated
    public String unterlageButton13EN = "";

    @Deprecated
    public int unterlage14Aktiv = 0;
    @Deprecated
    public String unterlageButton14DE = "";
    @Deprecated
    public String unterlageButton14EN = "";

    @Deprecated
    public int unterlage15Aktiv = 0;
    @Deprecated
    public String unterlageButton15DE = "";
    @Deprecated
    public String unterlageButton15EN = "";

    @Deprecated
    public int unterlage16Aktiv = 0;
    @Deprecated
    public String unterlageButton16DE = "";
    @Deprecated
    public String unterlageButton16EN = "";

    @Deprecated
    public int unterlage17Aktiv = 0;
    @Deprecated
    public String unterlageButton17DE = "";
    @Deprecated
    public String unterlageButton17EN = "";

    @Deprecated
    public int unterlage18Aktiv = 0;
    @Deprecated
    public String unterlageButton18DE = "";
    @Deprecated
    public String unterlageButton18EN = "";

    @Deprecated
    public int unterlage19Aktiv = 0;
    @Deprecated
    public String unterlageButton19DE = "";
    @Deprecated
    public String unterlageButton19EN = "";

    @Deprecated
    public int unterlage20Aktiv = 0;
    @Deprecated
    public String unterlageButton20DE = "";
    @Deprecated
    public String unterlageButton20EN = "";


    /**************Kontakt/Hilfe******************/

    /*++++++Kontaktformular+++++++++*/
    public int kontaktformularAktiv=0;//tfKontaktformularAktiv 2513

    public int kontaktformularBeiEingangMail=0;//tfKontaktformularBeiEingangMail 2514
    public String kontaktformularBeiEingangMailAn="";//tfKontaktformularBeiEingangMailAn Lang-81
    public int kontaktformularBeiEingangMailInhaltAufnehmen=0;//tfKontaktformularBeiEingangMailInhaltAufnehmen 2515

    public int kontaktformularBeiEingangAufgabe=0;//tfKontaktformularBeiEingangAufgabe 2516

    public int kontaktformularAnzahlKontaktfelder=0;//tfKontaktformularAnzahlKontaktfelder 2517
    /**0=nein, 1=ja, 2=zwingend*/
    public int kontaktformularTelefonKontaktAbfragen=0;//tfKontaktformularTelefonKontaktAbfragen 2518
    /**0=nein, 1=ja, 2=zwingend*/
    public int kontaktformularMailKontaktAbfragen=0;//tfKontaktformularMailKontaktAbfragen 2519

    /**0=nein, 1=ja, 2=zwingend*/
    public int kontaktformularThemaAnbieten=0; //tfKontaktformularThemaAnbieten 2520

    public List<EclKontaktformularThema> kontaktformularThemenListe=null; //nicht in Parameter-Table

    /**ThemenListe ist 1=global, 2=lokal, 1+2=3=Global ergänzt durch lokal*/
    public int kontaktformularThemenListeGlobalLokal=1; //2530 tfKontaktformularThemenListeGlobalLokal

    /*+++++++sonstige Kontaktmöglichkeiten++++++++*/
    /**0=keine, 1=Telefon, 2=E-Mail (addieren)*/
    public int kontaktSonstigeMoeglichkeitenAnbieten=3;//tfKontaktSonstigeMoeglichkeitenAnbieten 2521
   /*1=Oben, 2=Unten*/
    public int kontaktSonstigeMoeglichkeitenObenOderUnten=1;//tfKontaktSonstigeMoeglichkeitenObenOderUnten 2522

    /******************Menü*************************************/
    public List<EclMenueEintrag> menueListe=null; //Nicht in Parameter-Table

    /***************Register-Anbindung****************/
    /**0=keine; 1=GenossenschaftDat/ku178*/
    public int registerAnbindung=0;//tfRegisterAnbindung Sonder 2523

    /**0=keine; 1=ku178*/
    public int registerAnbindungOberflaeche=0;//tfRegisterAnbindungOberflaeche Sonder 2524

    public int registerAnbindungCheckBeiLogin=0;//tfRegisterAnbindungCheckBeiLogin Sonder 2525

    //tfInHVPortalKeineEmailUndKeinPasswortFuerAktionaere

    /**=1 => vom Register-Portal zur HV kann gesprungen werden*/
    public int registerAnbindungZurHV=0; //tfRegisterAnbindungZurHV Sonder 2583

    /**=1 => vom HV-Portal kann zum Registerportal gesprungen werden*/
    public int registerAnbindungVonHV=0; //tfRegisterAnbindungVonHV Sonder 2584

    /*++++Werte für Kommunikation zur Registeranbindung++++*/
    /*Hinweis: Private Key separat abzufragen*/

    public String api_client_id=""; // tfApi_client_id Sonder lang 82
    public String api_client_secret=""; //tfApi_client_secret Sonder lang 83
    public String api_base_url=""; //tfApi_base_url Sonder lang 84
    public String api_key_id=""; //tfApi_key_id Sonder lang 85
    public int api_jwt_expiration_time=0; //tfApi_jwt_expiration_time Sonder 2531

    public String api_key_name=""; //tfApi_key_name Sonder 2532
    public String api_ping_url=""; //tfApi_ping_url Sonder lang 86

    public String apiku178_url=""; //tfApiku178_url Sonder lang 119

    /*************Für Texte*********************************************/

    /**1= wenn kein mandantenspezifischer Text vorhanden, dann wird der Standardtext genommen.
     * 0= wenn kein mandantenspezifsicher Text vorhanden, dann wird der Text "leer" gesetzt.
     */
    public int standardTexteBeruecksichtigen = 1; //248

    /**Falls standardTexteBeruecksichtigen=1: gibt die Releasenummer der Standard-Texte an, die verwendet
     * werden sollen
     */
    public int basisSetStandardTexteVerwenden = 0; //267

    /**Anzeige eines Fragezeichens für Hinweise bei Mouse-Over. Werte addieren.
     * 0 => Keine Hinweise
     * 1 => Hinweise aktiviert
     * 2 => Hinweise aktiv und normale Texte abgeschaltet (sonst: Texte eingeschaltet)
     * 4 => Fragezeichen recht (sonst links)
     *
     */
    public int fragezeichenHinweiseVerwenden=0; //2573 tfFragezeichenHinweiseVerwenden
    public boolean liefereFragezeichenHinweiseKeineHints() {
        return fragezeichenHinweiseVerwenden==0;
    }
    public boolean liefereFragezeichenHinweiseAktiviert() {
        return fragezeichenHinweiseVerwenden>0;
    }
    public boolean liefereFragezeichenHinweiseNormaleTexteAusblenden() {
        return ((fragezeichenHinweiseVerwenden & 2)==2);
    }
    public boolean liefereFragezeichenHinweisePositionLinks() {
        return ((fragezeichenHinweiseVerwenden & 4)==0);
    }
    public boolean liefereFragezeichenHinweisePositionRechts() {
        return ((fragezeichenHinweiseVerwenden & 4)==4);
    }


    /**LEN für alle Textfelder: 200 (parameterlang)*/
    /**=0 => nur 1 Stimmrechtsvertreter; 1 = 2 (oder mehr) Stimmrechtsvertreter*/
    public int mehrereStimmrechtsvertreter = 1; //200

    public String stimmrechtsvertreterNameDE = ""; //Lang 3
    public String stimmrechtsvertreterNameEN = ""; //Lang 9

    /**Verwendung:
     * > Einsprungslink auf Gastkarte
     * > Einsprungslink in allgemeingültigen Variablen in L&L
     * > Einsprungslink + ?nummer= etc. für Einladungen
     * 
     */
    public String kurzLinkPortal=""; //tfKurzLinkPortal Lang 116


    /*XXX*/
    public String subdomainPortal=""; // tfSubdomainPortal Lang 117

    public String linkTagesordnung = ""; //tfLinkTagesordnung Lang 4
    public String linkGegenantraege = ""; //Lang 5
    public String linkEinladungsPDF = ""; //Lang 6

    public String linkNutzungsbedingungenAktionaersPortal = ""; //Lang 13
    public String linkNutzungsbedingungenHVPortal = "aNutzungshinweise.xhtml"; //Lang 14
    public String linkDatenschutzhinweise = ""; //Lang 15
    public String linkDatenschutzhinweiseKunde = "";//tfLinkDatenschutzhinweiseKunde Lang 19
    public String linkImpressum = ""; //Lang 16

    public String emailAdresseLink = ""; //tfEmailAdresseLink Lang 7
    public String emailAdresseText = ""; //Lang 8

    public String vollmachtEmailAdresseLink = ""; //tfVollmachtEmailAdresseLink Lang 79
    public String vollmachtEmailAdresseText = ""; //tfVollmachtEmailAdresseText Lang 80

    /**Eintrittskarte Nr.*/
    public String ekText = "HV-Ticket"; //tfEkText 2242
    public String ekTextMitArtikel = ""; //tfEkTextMitArtikel 2243
    /**Admission ticket*/
    public String ekTextEN = "Admission ticket"; //tfEkTextEN 2244
    public String ekTextENMitArtikel = ""; //ekTextENMitArtikel 2245

    /************Design*******************************/
    public int logoutObenOderUnten = 2; //tfLogoutObenOderUnten 276

    /**0=links, 1=mitte, 2=rechts*/
    public int logoPosition=0; //tfLogoPosition 2354 
    public String logoName = "beor.png"; //269
    public int logoBreite = 80; //270
    @Deprecated
    public int logoHoehe = 45; //271
    public String cssName = "beor.css"; //tfCSSName 272

    public String designKuerzel = "T001"; //tfDesignKuerzel 273

    public String farbeHeader="#ffffff"; //tfFarbeHeader 2339
    public String farbeButton="#7d7d7d"; //tfFarbeButton 2340
    public String farbeButtonHover="#585858"; //tfFarbeButtonHover 2341
    public String farbeButtonSchrift="#D8D8D8"; //tfFarbeButtonSchrift 2342
    public String farbeLink="#7d7d7d"; //tfFarbeLink 2343
    public String farbeLinkHover="#7d7d7d"; //tfFarbeLinkHover 2344
    public String farbeListeUngerade="#D8D8D8"; //tfFarbeListeUngerade 2345
    public String farbeListeGerade="#ffffff"; //tfFarbeListeGerade 2346 
    public String farbeHintergrund="#ffffff"; //tfFarbeHintergrund 2347
    public String farbeText="#000000"; //tfFarbeText 2348
    public String farbeUeberschriftHintergrund="#585858"; //tfFarbeUeberschriftHintergrund 2349
    public String farbeUeberschrift="#ffffff"; //tfFarbeUeberschrift 2350


    public String kachelFarbe = "#ffffff";
    public String themeFarbe = "#ffffff";
    public String schriftgroesseGlobal="16"; //2359
    public String logoMindestbreite="16";  //2360
    public String farbeHintergrundBtn00="#7d7d7d"; //2361
    public String farbeSchriftBtn00="#D8D8D8"; //2362
    public String farbeRahmenBtn00="#7d7d7d"; //2363
    public String breiteRahmenBtn00="1"; //2364
    public String radiusRahmenBtn00="5"; //2365
    public String stilRahmenBtn00="solid"; //2366
    public String farbeHintergrundBtn00Hover="#585858"; //2367
    public String farbeSchriftBtn00Hover="#D8D8D8";//2368
    public String farbeRahmenBtn00Hover="#585858"; //2369
    public String breiteRahmenBtn00Hover="1";//2370
    public String radiusRahmenBtn00Hover="5";//2371
    public String stilRahmenBtn00Hover="solid";//2372
    public String farbeFocus="#7d7d7d";//2373
    public String farbeError="#FA5858";//2374
    public String farbeErrorSchrift="#ffffff";//2375
    public String farbeWarning="#F5DA81";//2376
    public String farbeWarningSchrift="#000000";//2377
    public String farbeSuccess="#58FA82";//2378
    public String farbeSuccessSchrift="#ffffff";//2379
    public String farbeRahmenEingabefelder="#7d7d7d";//2380
    public String breiteRahmenEingabefelder="1";//2381
    public String radiusRahmenEingabefelder="2";//2382
    public String stilRahmenEingabefelder="solid";//2383
    public String farbeHintergrundLogoutBtn="#7d7d7d";//2384
    public String farbeSchriftLogoutBtn="#D8D8D8";//2385
    public String farbeRahmenLogoutBtn="#7d7d7d";//2386
    public String breiteRahmenLogoutBtn="1";//2387
    public String radiusRahmenLogoutBtn="5";//2388
    public String stilRahmenLogoutBtn="solid";//2389
    public String farbeHintergrundLogoutBtnHover="#585858";//2390
    public String farbeSchriftLogoutBtnHover="#D8D8D8";//2391
    public String farbeRahmenLogoutBtnHover="#585858";//2392
    public String breiteRahmenLogoutBtnHover="1";//2393
    public String radiusRahmenLogoutBtnHover="5"; //2394
    public String stilRahmenLogoutBtnHover="solid";//2395
    public String farbeHintergrundLoginBtn="#7d7d7d";//2396
    public String farbeSchriftLoginBtn="#D8D8D8";//2397
    public String farbeRahmenLoginBtn="#7d7d7d";//2398
    public String breiteRahmenLoginBtn="1";//2399
    public String radiusRahmenLoginBtn="5";//2400
    public String stilRahmenLoginBtn="solid";//2401
    public String farbeHintergrundLoginBtnHover="#585858";//2402
    public String farbeSchriftLoginBtnHover="#D8D8D8";//2403
    public String farbeRahmenLoginBtnHover="#585858";//2404
    public String breiteRahmenLoginBtnHover="1";//2405
    public String radiusRahmenLoginBtnHover="5";//2406
    public String stilRahmenLoginBtnHover="solid";//2407
    public String farbeRahmenLoginBereich="#7d7d7d"; //2408
    public String breiteRahmenLoginBereich="1";//2409
    public String radiusRahmenLoginBereich="5"; //2410
    public String stilRahmenLoginBereich="solid";//2411
    public String farbeHintergrundLoginBereich="#ffffff";//2412
    public String farbeLinkLoginBereich="#585858";//2413
    public String farbeLinkHoverLoginBereich="#585858";//2414
    public String farbeRahmenEingabefelderLoginBereich="#7d7d7d";//2415
    public String breiteRahmenEingabefelderLoginBereich="1";//2416
    public String radiusRahmenEingabefelderLoginBereich="2";//2417
    public String stilRahmenEingabefelderLoginBereich="solid";//2418
    public String farbeBestandsbereichUngeradeReihe="#D8D8D8"; //2419
    public String farbeBestandsbereichGeradeReihe="#ffffff";//2420
    public String farbeLineUntenBestandsbereich="#7d7d7d";//2421
    public String breiteLineUntenBestandsbereich="1";//2422
    public String stilLineUntenBestandsbereich="solid";//2423
    public String farbeRahmenAnmeldeuebersicht="#7d7d7d";//2424
    public String breiteRahmenAnmeldeuebersicht="1";//2425
    public String radiusRahmenAnmeldeuebersicht="5";//2426
    public String stilRahmenAnmeldeuebersicht="solid";//2427
    public String farbeTrennlinieAnmeldeuebersicht="#7d7d7d";//2428
    public String breiteTrennlinieAnmeldeuebersicht="1";//2429
    public String stilTrennlinieAnmeldeuebersicht="solid";//2430
    public String farbeRahmenErteilteWillenserklärungen="#7d7d7d";//2431
    public String breiteRahmenErteilteWillenserklärungen="1";//2432
    public String radiusRahmenErteilteWillenserklärungen="2";//2433
    public String stilRahmenErteilteWillenserklärungen="solid";//2434
    public String farbeHintergrundErteilteWillenserklärungen="#ffffff";//2435
    public String farbeSchriftErteilteWillenserklärungen="#000000";//2436
    public String farbeRahmenAbstimmungstabelle="#7d7d7d";//2437
    public String breiteRahmenAbstimmungstabelle="2";//2438
    public String radiusRahmenAbstimmungstabelle="0";//2439
    public String stilRahmenAbstimmungstabelle="solid";//2440
    public String farbeHintergrundAbstimmungstabelleUngeradeReihen="#D8D8D8";//2441
    public String farbeSchriftAbstimmungstabelleUngeradeReihen="#000000";//2442
    public String farbeHintergrundAbstimmungstabelleGeradeReihen="#ffffff";//2443
    public String farbeSchriftAbstimmungstabelleGeradeReihen="#000000";//2444
    public String farbeHintergrundWeisungJa="#ffffff";//2445
    public String farbeSchriftWeisungJa="#088A08";//2446
    public String farbeRahmenWeisungJa="#088A08";//2447
    public String farbeHintergrundWeisungJaChecked="#088A08";//2448
    public String farbeSchriftWeisungJaChecked="#ffffff";//2449
    public String farbeRahmenWeisungJaChecked="#088A08";//2450
    public String farbeHintergrundWeisungNein="#ffffff";//2451
    public String farbeSchriftWeisungNein="#B40404";//2452
    public String farbeRahmenWeisungNein="#B40404";//2453
    public String farbeHintergrundWeisungNeinChecked="#B40404";//2454
    public String farbeSchriftWeisungNeinChecked="#ffffff";//2455
    public String farbeRahmenWeisungNeinChecked="#B40404";//2456
    public String farbeHintergrundWeisungEnthaltung="#ffffff";//2457
    public String farbeSchriftWeisungEnthaltung="#D7DF01";//2458
    public String farbeRahmenWeisungEnthaltung="#D7DF01";//2459
    public String farbeHintergrundWeisungEnthaltungChecked="#D7DF01";//2460
    public String farbeSchriftWeisungEnthaltungChecked="#000000";//2461
    public String farbeRahmenWeisungEnthaltungChecked="#D7DF01";//2462
    public String farbeHintergrundFooterTop="#585858";//2463
    public String farbeSchriftFooterTop="#D8D8D8";//2464
    public String farbeLinkFooterTop="#D8D8D8";//2465
    public String farbeLinkFooterTopHover="#D8D8D8";//2466
    public String farbeHintergrundFooterBottom="#585858";//2467
    public String farbeSchriftFooterBottom="#D8D8D8";//2468
    public String farbeLinkFooterBottom="#D8D8D8";//2469
    public String farbeLinkFooterBottomHover="#D8D8D8";//2470
    public String farbeHintergrundModal="#D8D8D8";//2471
    public String farbeSchriftModal="#000000";//2472
    public String farbeHintergrundModalHeader="#D8D8D8";//2473
    public String farbeSchriftModalHeader="#000000";//2474
    public String farbeTrennlinieModal="#7d7d7d";//2475
    public String farbeHintergrundUntenButtons="#7d7d7d";//2476
    public String farbeSchriftUntenButtons="#D8D8D8";//2477
    public String farbeRahmenUntenButtons="#7d7d7d";//2478
    public String breiteRahmenUntenButtons="1";//2479
    public String radiusRahmenUntenButtons="5";//2480
    public String stilRahmenUntenButtons="solid";//2481
    public String farbeHintergrundUntenButtonsHover="#585858";//2482
    public String farbeSchriftUntenButtonsHover="#D8D8D8";//2483
    public String farbeRahmenUntenButtonsHover="#585858";//2484
    public String breiteRahmenUntenButtonsHover="1";//2485
    public String radiusRahmenUntenButtonsHover="5";//2486
    public String stilRahmenUntenButtonsHover="solid";//2487

    public String farbeCookieHintHintergrund="#58FA58"; //tfFarbeCookieHintHintergrund 2355
    public String farbeCookieHintSchrift="#ffffff"; //tfFarbeCookieHintSchrift 2356
    public String farbeCookieHintButton="#7d7d7d"; //tfFarbeCookieHintButton 2357
    public String farbeCookieHintButtonSchrift="#D8D8D8"; //tfFarbeCookieHintButtonSchrift 2358

    /*XXX*/
    public String farbeLadebalkenUploadLeer = "#4a873c"; // tfFarbeLadebalkenUploadLeer 2577
    public String farbeLadebalkenUploadFull = "rgba(74, 135, 60, 0.8)"; // tfFarbeLadebalkenUploadFull 2578
    
 


//    /* Farbe Header */
//    --col-header: #ffffff;
//    /* Button:hover Effekt */
//    --col-00: #585858;
//    /* Button */
//    --col-01: #7d7d7d;
//    /* Button Schriftfarbe */
//    --col-btn-font-color: #D8D8D8;
//    /* Link Schriftfarbe */
//    --col-link-font-color: #7d7d7d;
//    /* Link Schriftfarbe wenn hover */
//    --col-link-font-color-hover: #7d7d7d;
//    /* Listenfarbe für ungerade (odd) Reihe*/
//    --col-02: #D8D8D8;
//    /* Listenfarbe für gerade (even) Reihe */
//    --col-03: #ffffff;
//    /* Hintergrundfarbe der gesamten Seite */
//    --col-site-background: #ffffff;
//    /* Textfarbe für gesamte Seite */
//    --col-font-color: #000000;
//    /* Hintergrundfarbe für die Überschrift */
//    --col-font-headline-background: #585858;
//    /* Schriftfarbe für die Überschrift */
//    --col-font-headline: #ffffff;
//    /* Hintergrundfarbe für den Footer */
//    --col-footer-background: #585858;
//    /* Schriftfarbe für die Links im Footer */
//    --col-footer-link: #D8D8D8;
//    /* Schriftfarbe für die Links im Footer wenn hover */
//    --col-footer-link-hover: #D8D8D8;



    /*************************************Nur für App**********************************************/

    /**Behandlung Startseite und Start in App (alt)
     * Mit OR verknüpft:
     * 0 = keinerlei Anzeige einer Startseite
     * 1 = Anzeige nach Auswahl eines Mandanten (entfällt zukünftig)
     * 2 = Anzeige nach Aufruf des Bestandes und vor Weiterverarbeitung im Mandanten
     * 4 = in Anzeige "sonstiger Funktionen" (Seite 916)
     * 32 = Anzeige nach Login über QR-Code (Seite 917)
     *
     *
     * 8 = nach Bestandszuordnung wird unmittelbar "Portal" aufgerufen (je nach "2" mit / ohne Anzeige der Startseite)
     * 16 = nach Bestandszuordnung wird unmittelbar die Startseite aufgerufen (aber nur diese) - nicht gemeinsam mit 8!
     *
     * Neu:
     *
     * 1, 2, 4 schließen sich aus!
     *
     * 1 = (nur nach QR-Code): nach Bestandszuordnung wird die Startseite (917) aufgerufen - aber nur diese
     * 2 = (nur nach QR-Code): nach Bestandszuordnung wird erst Startseite (917) aufgerufen, dann die Portalfunktionalität
     * 4 = (nur nach QR-Code): nach Bestandszuordnung wird sofort die Portalfunktionalität aufgerufen
     * 8 = (nur nach manuelle Eingabe): nach Betandszuordnung wird sofort die Portalfunktionalität aufgerufen
     * 16 = in Anzeige "sonstiger Funktionen" (Seite 916)
     * 32 = Anzeige nach Aufruf des Bestandes und vor Weiterverarbeitung im Mandanten (917)
     */

    /**Behandlung Startseite und Start in App
     * Mit OR verknüpft:
     *
     * QR-Code lesen:
     * 1 = nach Bestandszuordnung wird die Startseite (917) aufgerufen - aber nur diese
     * 2 = nach Bestandszuordnung wird erst Startseite (917) aufgerufen, dann die Portalfunktionalität
     * 4 = nach Bestandszuordnung wird sofort die Portalfunktionalität aufgerufen
     * (wenn 1, 2, 4 alle 3 nicht gesetzt, dann geht die App nach der Zuordnung wieder in die Bestandsübersicht)
     * 64 = auf der Seite mit den Login-Daten werden die Starttexte angezeigt (siehe Beschreibung AControllerLogin)
     *
     * Manueller Login:
     * 8 = Nach Bestandszuordnung wird sofort die Portalfunktionalität aufgerufen (wenn nicht gesetzt, dann geht App
     * wieder in die Bestandsübersicht)
     * 128 = auf der Seite mit dem manuellen Login werden die Starttexte angezeigt (siehe Beschreibung AControllerLogin)
     *
     *  Allgemein:
     *  16 = Startseite kann in der Anzeige "sonstiger Funktionen (Seite 916) angezeigt werden
     *  32 = Nach Aufruf eines (bereits zugeordneten) Bestandes wird die Startseite vor Weiterverarbeitung im Mandanten angezeigt (Seite 917)
     *
     */
    public int anzeigeStartseite = 0; //OK 184

}
