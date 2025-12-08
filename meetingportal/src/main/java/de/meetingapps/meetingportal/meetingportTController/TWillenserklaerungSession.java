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
import java.util.LinkedList;
import java.util.List;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComEntities.EclBesitzAREintrag;
import de.meetingapps.meetingportal.meetComEntities.EclWillenserklaerungStatusNeu;
import de.meetingapps.meetingportal.meetComEntities.EclZugeordneteMeldungNeu;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

/**Session-Variablen für alle Dialoge mit Willenserklärungen (EK, Briewahl etc.)*/
@SessionScoped
@Named
public class TWillenserklaerungSession implements Serializable {
    private static final long serialVersionUID = -7121824218375047398L;
    
    int logDrucken=3;

    /************************Haupt-Auswahl**************************************************************
     * gibt an, welcher "Hauptstrang" des Dialoges gerade bearbeitet werden.
     * 1 = Neuanmeldung (Namensaktien) - bisher waren noch keine Anmeldungen des Aktionärs vorhanden, die ausgewaehlteAktion
     * 		wird zusammen mit der Erstanmeldung des Aktienbesitzes durchgeführt.
     * 
     * 		Wesentliche Auswahlmaske ist aAnmelden.
     * 
     * 2 = Erstanmeldung (Namensaktien) ist bereits früher erfolgt. D.h. nur noch Willenserklärungen speichern, keine
     * 		Erstanmeldung mehr durchführen.
     * 
     * 		Wesentliche Auswahlmaske ist aStatus, und dann aMenue (aMenue entspricht im wesentlichen aAnmelden, siehe oben).
     * 
     * 		Im Fall ==2 gilt: 
     * 			>in EclZugeordneteMeldungM steht (nach Durchlaufen von aStatus) die Meldung zur Verfügung, für 
     * 				die weitere Willenserklärungen bearbeitet werden sollen.
     * 			>Im Falle von Stornieren oder Ändern von Willenserklärungen steht in EclWillenserklaerungStatusM die
     * 				Willenserklärung zur Verfügung, die bearbeitet werden soll.
     * 
     * siehe KonstPortalAktion
     */
    private String ausgewaehlteHauptAktion = "";

    /****************************Ausgewählte Aktion******************************************************
     * Gibt an, welche "Detail"-Aktion gerade ausgewählt ist und ausgeführt ist.
     * 0 bzw. leer = keine Aktion ausgewählt
     * 1 = Eine Eintrittskarte auf meinen Namen ausstellen
     * 2 = zwei Eintrittskarten auf unsere Namen ausstellen (nur bei Personengemeinschaften)
     * 3 = eine Eintrittskarte auf einen Bevollmächtigten ausstellen
     * siehe auch 28! 
     * 4 = Vollmacht und Weisung an Stimmrechtsvertreter erteilen
     * 5 = Per Briefwahl abstimmen
     * 6 = Vollmacht (und Weisung) an KI/AV - noch nicht näher qualifiziert
     * 7 = nur Vollmacht an KI/AV
     * 8 = Vollmacht und Weisung (dediziert) an KI/AV
     * 9 = Vollmacht und Weisung gemäß Vorschlag an KI/AV
     * 10 = Vollmacht/Weisung SRV ändern
     * 11 = Briefwahl ändern
     * 12 = KIAV Weisung ändern
     * 13 = Vollmacht/Weisung SRV stornieren
     * 14 = Briefwahl stornieren
     * 15 = KIAV stornieren
     * 16 = Gastkarte stornieren
     * 17 = Aktionärs-Eintrittskarte stornieren
     * 18 = Aktionärs-Eintrittskarte mit Vollmacht stornieren
     * 19 = Vollmacht Dritte stornieren
     * 20 = Gastkarte anlegen
     * 21 = Vollmacht an Dritte - aufruf aus aNeueWillenserklaerung (siehe auch 29!)
     * 25 = zusätzliche Willenserklärung
     * 26 = Detailanzeige Eintrittskarte
     * 27 = Detailanzeige Gastkarte
     * 28 = zwei Eintrittskarten mit oder ohne Vollmacht (bei allen Anmeldungen)
     * 29 = Vollmacht an Dritte - Aufruf aus aStatus (siehe auch 21!)
     * 30 = zwei Eintrittskarten auch mich selbst (bei allen Anmeldungen)
     * 
     * 31 = Vollmacht (und Weisung) Dauervollmacht (noch nicht näher qualifiziert)
     * 32 = nur Vollmacht an Dauervollmacht
     * 33 = Vollmacht und Weisung (dediziert) an Dauervollmacht
     * 34 = Vollmacht und Weisung gemäß Vorschlag an Dauervollmacht
     * 
     * 35 = Vollmacht (und Weisung) Organisatorisch (noch nicht näher qualifiziert)
     * 36 = nur Vollmacht an Organisatorisch
     * 37 = Vollmacht und Weisung (dediziert) an Organisatorisch
     * 38 = Vollmacht und Weisung gemäß Vorschlag an Organisatorisch
     * 
     * 39 = Dauervollmacht ändern
     * 40 = Organisatorisch ändern
     * 
     * 41 = Dauervollmacht stornieren
     * 42 = Organisatorisch stornieren
     * 
     * 43 = Nur Anmeldung durchführen ohne weitere Abgabe einer Willenserklärung
     * 
     * siehe KonstPortalAktion
     * */
    private String ausgewaehlteAktion = "";

    /*++++++++++++++++++++Div. Infos, die zur Weiterverarbeitung erforderlich sind++++++++++++*/

    /**Wird ins Textfeld der Willenserklärung (pQuelle) übernommen - z.B. Zuordnung zu Scan-Datei+*/
    private String quelle = "";

    /**Weg, über den die Willenserklärung abgegeben wurde*/
    private int eingabeQuelle = 21;

    private String erteiltZeitpunkt = ""; /*JJJJ.MM.TT HH:MM:SS, oder leer*/

    /**Wenn true, dann können zwei Anmeldungen eines AKtienregistereintrags ohne weitere Willenserklärungen
     * zu einer zusammengefaßt werden
     */
    private boolean zusammenfassenVonAnmeldungenMoeglich=true;
    
    /*++++++++++++++Ausgangsbasis - Meldung bzw. Aktienregistereintrag++++++++++++++++++++++++++*/
//    /**Ausgangsbasis - Willenserklärung ist für diese Meldung auszuführen, wenn bereits angemeldet*/
//    @Deprecated
//    private EclZugeordneteMeldungNeu zugeordneteMeldungFuerAusfuehrung = null;

//    /**Ausgangsbasis - Willenserklärung ist für diese Willenserklärung auszuführen, wenn bereits angemeldet*/
//    @Deprecated
//    private EclWillenserklaerungStatusNeu zugeordneteWillenserklaerungStatus = null;

    /**Ausgangsbasis - Willenserklärung ist für diesen Aktienregistereintrag auszuführen*/
    @Deprecated
    private EclBesitzAREintrag besitzAREintrag = null;

    /*++++++++++++++Ausgangsbasis - Liste der Daten, für die die Willenserklärung ausgeführt wird+++++++++++++++*/
    private int listeARoderMeldungen = 1;

    /*++++Aktienregister-Liste++++*/
    private List<EclBesitzAREintrag> besitzAREintragListe = null;

    public void initBesitzAREintragListe() {
        listeARoderMeldungen = 1;
        besitzAREintragListe = new LinkedList<EclBesitzAREintrag>();
    }

    public void addBesitzAREintragListe(EclBesitzAREintrag pEclBesitzAREintrag) {
        besitzAREintragListe.add(pEclBesitzAREintrag);
    }

    /*++++++Meldungs-Liste+++++*/
    private List<EclZugeordneteMeldungNeu> zugeordneteMeldungFuerAusfuehrungListe = null;
    private List<EclWillenserklaerungStatusNeu> zugeordneteWillenserklaerungStatusListe = null;

    /*+++Ergänzungen für Weisungen+++*/
    /**Siehe EclWillenserklaerungStatusM:
     * =1 => dedizierte vorhanden
     * =2 => gemäß Vorschlag vorhanden
     * =3 => nur freie
     * 
     * siehe KonstWeisungenSind
     */
    private List<Integer> weisungenSindListe = null;

    /**Meldungsident des Aktionärs, für den gerade die Weisung geändert wird*/
    private List<Integer> meldungsIdentListe = null;

    /**WillenserklärungIdent der ursprüngliche Vollmacht/Weisung*/
    private List<Integer> willenserklaerungIdentListe = null;

    /**SammelkartenIdent, für die die ursprüngliche Vollmacht/Weisung erteilt wurde*/
    private List<Integer> sammelIdentListe = null;

    /**Ident der ursprünglichen Weisung*/
    private List<Integer> weisungsIdentListe = null;

    public void initZugeordneteMeldungFuerAusfuehrungListe() {
        listeARoderMeldungen = 2;
        zugeordneteMeldungFuerAusfuehrungListe = new LinkedList<EclZugeordneteMeldungNeu>();
        zugeordneteWillenserklaerungStatusListe = new LinkedList<EclWillenserklaerungStatusNeu>();

        weisungenSindListe = new LinkedList<Integer>();
        meldungsIdentListe = new LinkedList<Integer>();
        willenserklaerungIdentListe = new LinkedList<Integer>();
        sammelIdentListe = new LinkedList<Integer>();
        weisungsIdentListe = new LinkedList<Integer>();

    }

    public void addZugeordneteMeldungFuerAusfuehrungListe(EclZugeordneteMeldungNeu pEclZugeordneteMeldungNeu,
            EclWillenserklaerungStatusNeu pEclWillenserklaerungStatusNeu) {
        zugeordneteMeldungFuerAusfuehrungListe.add(pEclZugeordneteMeldungNeu);
        zugeordneteWillenserklaerungStatusListe.add(pEclWillenserklaerungStatusNeu);

        /*Die folgenden werden vorinitialisiert, eigentliche Belegung erfolgt dann mit setLetzte soweit benötigt*/
        weisungenSindListe.add(0);
        meldungsIdentListe.add(0);
        willenserklaerungIdentListe.add(0);
        sammelIdentListe.add(0);
        weisungsIdentListe.add(0);
    }

    public void setLastElementOfWeisungenSindListe(Integer pWert) {
        weisungenSindListe.set(weisungenSindListe.size() - 1, pWert);
    }

    public void setLastElementOfMeldungsIdentListe(Integer pWert) {
        meldungsIdentListe.set(meldungsIdentListe.size() - 1, pWert);
    }

    public void setLastElementOfWillenserklaerungIdentListe(Integer pWert) {
        willenserklaerungIdentListe.set(willenserklaerungIdentListe.size() - 1, pWert);
    }

    public void setLastElementOfSammelIdentListe(Integer pWert) {
        sammelIdentListe.set(sammelIdentListe.size() - 1, pWert);
    }

    public void setLastElementOfWeisungsIdentListe(Integer pWert) {
        weisungsIdentListe.set(weisungsIdentListe.size() - 1, pWert);
    }

    /*++++++++++++++++++++++++++++++++Ermittle Gattung für auszuführende Meldungen / Aktienregistereinträge+++++++++++++++++*/
    /**[gattung]==1 => in der Liste ist ein Eintrag mit dieser Gattung vorhanden*/
    private int[] gattungVorhanden = null;

    /**Wenn mehrere vorhanden, dann 0*/
    private int gattungEinzigeVorhanden=0;
    
    public void ermittleGattungenFuerBesitzAREintragListe() {
        gattungVorhanden = new int[6];
        for (int i = 0; i < 6; i++) {
            gattungVorhanden[i] = 0;
        }
        if (besitzAREintragListe!=null) {
            for (EclBesitzAREintrag iEclBesitzAREintrag : besitzAREintragListe) {
                gattungVorhanden[iEclBesitzAREintrag.aktienregisterEintrag.getGattungId()] = 1;
            }
        }
        int anz=0;
        for (int i=1;i<=5;i++) {
            if (gattungVorhanden[i]==1) {
                anz++;
                gattungEinzigeVorhanden=i;
            }
        }
        if (anz>1) {gattungEinzigeVorhanden=0;}
    }
    
    /**Darf nur aufgerufen werden, wenn sicher ist, dass nur ein eine gemeinsame Gattung in besitzAREintragListe enthalten ist.
     * Für Scannen z.B.. Ergebnis ist in gattungEinzigeVorhanden*/
    public int ermittleGattungFuerEinzigenBesitzAREintragList() {
        for (EclBesitzAREintrag iEclBesitzAREintrag : besitzAREintragListe) {
            gattungEinzigeVorhanden=iEclBesitzAREintrag.aktienregisterEintrag.getGattungId();
            return gattungEinzigeVorhanden;
        }
        return 1;
    }

    public void ermittleGattungenFuerZugeordneteMeldungFuerAusfuehrungListe() {
        gattungVorhanden = new int[6];
        for (int i = 0; i < 6; i++) {
            gattungVorhanden[i] = 0;
        }
        for (EclZugeordneteMeldungNeu iEclZugeordneteMeldungNeu : zugeordneteMeldungFuerAusfuehrungListe) {
            gattungVorhanden[iEclZugeordneteMeldungNeu.getGattung()] = 1;
        }
    }

    /**Darf nur aufgerufen werden, wenn sicher ist, dass nur ein eine gemeinsame Gattung in zugeordneteMeldungFuerAusfuehrungListe enthalten ist.
     * Für Scannen z.B.. Ergebnis ist in gattungEinzigeVorhanden*/
    public int ermittleGattungFuerEinzigenBesitzZugeordneteMeldungFuerAusfuehrungList() {
        for (EclZugeordneteMeldungNeu iEclZugeordneteMeldungNeu : zugeordneteMeldungFuerAusfuehrungListe) {
            gattungEinzigeVorhanden=iEclZugeordneteMeldungNeu.getGattung();
            return gattungEinzigeVorhanden;
        }
        return 1;
    }

    /*++++++++++++++++++++++Wer ist geber - Aktionär oder Vertreter?++++++++++++++++++++++++++++*/
    private boolean geberIstDerAktionaerSelbst = true;

    /*++++++++++++++++++++++++++++++++++Eintrittskarte+++++++++++++++++++++++++++++++++++*/
    /*****Erste Eintrittskarte*****/
    private String eintrittskarteVersandart = "";
    /**0 = keinerlei Weiterverarbeitung erforderlich (alles bereits vom Programm adhoc erledigt
     * 1 = Aufnahme in Sammelbatch, an Adresse im Aktienregister - bei nächstem Drucklauf ausdrucken und versenden
     * 2 = Aufnahme in Sammelbatch, an Versandadresse - bei nächstem Drucklauf ausdrucken und versenden
     * 3 = Online-Ausdruck (im Portal) erfolgt
     * 4 = Versand per Email (im Portal) erfolgt
     * 5 = automatische Aufnahme in App
     * 6 = selbe Versandadresse wie zeitgleich ausgestellte Eintrittskarte (nur Gastkarte!)
     * 7 = Aufnahme in Sammelbatch, an Adresse im Aktienregister - bei nächstem Drucklauf ausdrucken und versenden (nur Gastkarte!)
     * 
     * Siehe KonstWillenserklaerungVersandartEK
     */

    /**Dateinummer, unter der das PDF für Emailversand oder Selbstdruck abgelegt wurde*/
    private int eintrittskartePdfNr = 0;

    /**Ausgestellte ZutrittsIdent*/
    private String zutrittsIdent = "";
    private String zutrittsIdentNeben = "";

    private String eintrittskarteEmail = "";
    private String eintrittskarteEmailBestaetigen = "";

    private String eintrittskarteAbweichendeAdresse1 = "";
    private String eintrittskarteAbweichendeAdresse2 = "";
    private String eintrittskarteAbweichendeAdresse3 = "";
    private String eintrittskarteAbweichendeAdresse4 = "";
    private String eintrittskarteAbweichendeAdresse5 = "";

    /**Vollmacht*/
    private String vollmachtName = "";
    private String vollmachtVorname = "";
    private String vollmachtOrt = "";

    private String zielOeffentlicheID = "";
    private int personNatJurOeffentlicheID = 0;

    /**Falls True, dann wird die EK nicht versandt, sondern intern übermittelt*/
    private boolean ueberOeffentlicheID = false;

    /**Verwendung als "Render"-Feld bei ZWEI_EK_SELBST_ODER_VOLLMACHT.
     * Gibt an, ob eine Vollmacht für diese Anmeldung eingegeben wurde oder nicht*/
    private boolean vollmachtEingeben = false;

    /*****Zweite Eintrittskarte*****/
    private String eintrittskarteVersandart2 = "";
    /**0 = keinerlei Weiterverarbeitung erforderlich (alles bereits vom Programm adhoc erledigt
     * 1 = Aufnahme in Sammelbatch, an Adresse im Aktienregister - bei nächstem Drucklauf ausdrucken und versenden
     * 2 = Aufnahme in Sammelbatch, an Versandadresse - bei nächstem Drucklauf ausdrucken und versenden
     * 3 = Online-Ausdruck (im Portal) erfolgt
     * 4 = Versand per Email (im Portal) erfolgt
     * 5 = automatische Aufnahme in App
     * 6 = selbe Versandadresse wie zeitgleich ausgestellte Eintrittskarte (nur Gastkarte!)
     */

    /**Dateinummer, unter der das PDF für Emailversand oder Selbstdruck abgelegt wurde*/
    private int eintrittskartePdfNr2 = 0;

    /**Ausgestellte ZutrittsIdent*/
    private String zutrittsIdent2 = "";
    private String zutrittsIdentNeben2 = "";

    private String eintrittskarteEmail2 = "";
    private String eintrittskarteEmail2Bestaetigen = "";

    private String eintrittskarteAbweichendeAdresse12 = "";
    private String eintrittskarteAbweichendeAdresse22 = "";
    private String eintrittskarteAbweichendeAdresse32 = "";
    private String eintrittskarteAbweichendeAdresse42 = "";
    private String eintrittskarteAbweichendeAdresse52 = "";

    /**Vollmacht*/

    private String vollmachtName2 = "";
    private String vollmachtVorname2 = "";
    private String vollmachtOrt2 = "";

    /**Verwendung als "Render"-Feld bei ZWEI_EK_SELBST_ODER_VOLLMACHT.
     * Gibt an, ob eine Vollmacht für diese Anmeldung eingegeben wurde oder nicht*/
    private boolean vollmachtEingeben2 = false;

    /*++++++++++++++Weisungen+++++++++++++++++++*/
    /**Abstimmungsvorschlag für die jeweils zutreffende Sammelkarte (Gesellschaft, oder KIAV)*/
    private int abstimmungsVorschlagIdent = 0;

    private boolean bestaetigtDassBerechtigt = false;

    private String jnBeiQuittung="";
    
    /**Wenn !=-1, dann wird die eingetragene Nummer für Buchung der Weisung verwendet*/
    private int abweichendeSammelkarte = -1;

    /**Siehe EclWillenserklaerungStatusM:
     * =1 => dedizierte vorhanden
     * =2 => gemäß Vorschlag vorhanden
     * =3 => nur freie
     * 
     * siehe KonstWeisungenSind
     */
    @Deprecated
    private int weisungenSind = 0;

    /**Meldungsident des Aktionärs, für den gerade die Weisung geändert wird*/
    @Deprecated
    private int meldungsIdent = 0;

    /**WillenserklärungIdent der ursprüngliche Vollmacht/Weisung*/
    @Deprecated
    private int willenserklaerungIdent = 0;

    /**SammelkartenIdent, für die die ursprüngliche Vollmacht/Weisung erteilt wurde*/
    @Deprecated
    private int sammelIdent = 0;

    /**Ident der ursprünglichen Weisung*/
    @Deprecated
    private int weisungsIdent = 0;

    
    
    /*--------------Bestätigungsfelder Neu--------------------*/
    
    /**wenn true, dann in Oberfläche anbieten. Wird abhängig von Briefwahl/SRV gesetzt*/
    private boolean bestaetigungErmoeglichen=false;
    
    private boolean bestaetigenPerMailDurchfuehren=false;
    
    /**true, wenn im Rahmen der Vorbereitung aufgrund der erforderlichen E-Mail-Bestätigung
     * das PDF bereits erzeugt wurde.
     */
    private boolean pdfWurdeBereitsErzeugt=false;
    
    /**True => Es wurde eine Erstanmeldung durchgeführt
     * Achtung, leider nicht immer richtig gefüllt ....*/
    private boolean anmeldungDurchgefuehrt=false;

    /**1=Briefwahl, 2=SRV (für "offizielles" Bestätigungs-PDF)
     * Bei Storno auch:
     * 3=KIAV, 4=Dauervollmacht, 5=Organisatorisch*/
    private String art="";
    
    private String ausstellungsZeit="";
    private String lfdNrPDF="";
    
    /**Letzte beiden Ziffer Jahr, Mandantennummer, LfdHV-Nummer, lfdNummer 6 stellig Bestätigung (=lfdNrPDF)*/
    private String nummerBestaetigung="";
    private String kennungVeranstaltung="";
    private String isin="";
    /**HV -Datum - Datumsformat so vorgegeben von Verordnung JJJJMMTT*/
    private String datum="";
    private String nameEmittent="";
    private String nameBestaetigender="";
    private String nameAbstimmender="";

    private String aktionaersnummer="";
    private String aktionaersname="";

    /**EK-Nummer, falls eine EK ausgestellt wurde*/
    private String ekNummer="";
    
    /**Bevollmächtigter, falls eine Vollmacht an Dritte (ggf. mit EK)
     * ausgestellt wurde
     */
    private String bevollmaechtigterVorname="";
    private String bevollmaechtigterName="";
    private String bevollmaechtigterOrt="";
       
    /**Enthält die TOP-Punkte, für die abgestimmt wurde, oder
     * für die Widerspruch o.ä. erteilt wurde - in "einfach aufbereiteter Form", 
     * d.h. TOP-Nummer, ": " Abstimmungsverhalten (nur bei SRV/Briefwahl), TOP-Text (wie im Portal verwendet)
     */
    private String topListe="";

    /**Eingabefeld - Email, an die geschickt werden soll*/
    private String emailFuerBestaetigung="";


    /*----------Zusätzliche Bestätigungsfelder für E-Mail-Bestätigung aller Willenserklärungen-----*/
    /**Steht nicht bei Bestätigung von Mitteilungen zur Verfügung*/
    
    private String artUmfassend="";
    
    
    
    
    public void clearBestaetigungsfelder() {
        pdfWurdeBereitsErzeugt=false;
        bestaetigungErmoeglichen=false;
        bestaetigenPerMailDurchfuehren=false;
        anmeldungDurchgefuehrt=false;
        
        art="";
        ausstellungsZeit="";
        lfdNrPDF="";
        nummerBestaetigung="";
        kennungVeranstaltung="";
        isin="";
        datum="";
        nameEmittent="";
        nameBestaetigender="";
        nameAbstimmender="";
        aktionaersnummer="";
        aktionaersname="";
        ekNummer="";
        bevollmaechtigterVorname="";
        bevollmaechtigterName="";
        bevollmaechtigterOrt="";
        topListe="";

        emailFuerBestaetigung="";

        artUmfassend="";

    }
    
    
    /*++++++++++++++++++++++++++++Nur für ku302_303++++++++++++++++++++++++++++++++++*/
    private String nzVertretungsart="";
    private String nzGpNummer="";
    private String nzVerband="";
    
    public boolean pruefeObNzVerbandAusgewaehlt() {
        CaBug.druckeLog("nzVerband="+nzVerband, logDrucken, 10);
        if (nzVerband==null) {return false;}
        if (nzVerband.isEmpty()) {return false;}
        return true;
    }
    
    public boolean pruefeObNZVertretungsartAusgewaehlt() {
        CaBug.druckeLog("nzVertretungsart="+nzVertretungsart, logDrucken, 10);
        if (nzVertretungsart==null) {return false;}
        if (nzVertretungsart.isEmpty()) {return false;}
        return true;
    }
    
    /*+++++++++++++++++++++++++++++++Div. Return-Werte, insbesondere auch für WAServices / WAIntern++++++++++++++++++*/

    /**WillenserkärungsIdent für die durchgeführte Willenserklärung (also z.B. Weisung, EK etc.)
     * Wichtig: wenn 2 EKs ausgestellt werden, dann werden diese Variablen nicht korrekt / vollständig gefüllt - 
     * Verwendung dafür dann nicht sinnvoll!*/
    private int rcWillenserklaerungIdentAusgefuehrt = 0;

    /**WillenserkärungsIdent für die durchgeführte Zweit-Willenserklärung (also z.B. VollmachtDritte zu EK)*/
    private int rcWillenserklaerungIdentAusgefuehrtZweit = 0;

    /**+++++++++++zurücksetzen aller Session-Prozesse++++++++++++++*/
    public void clear() {
        nzVertretungsart="";
        nzGpNummer="";
        nzVerband="";
        
        ausgewaehlteHauptAktion = "";
        ausgewaehlteAktion = "";

        eintrittskarteVersandart = "";
        eintrittskartePdfNr = 0;
        zutrittsIdent = "";
        zutrittsIdentNeben = "";

        eintrittskarteEmail = "";
        eintrittskarteEmailBestaetigen = "";
        eintrittskarteAbweichendeAdresse1 = "";
        eintrittskarteAbweichendeAdresse2 = "";
        eintrittskarteAbweichendeAdresse3 = "";
        eintrittskarteAbweichendeAdresse4 = "";
        eintrittskarteAbweichendeAdresse5 = "";
        vollmachtName = "";
        vollmachtVorname = "";
        vollmachtOrt = "";

        eintrittskarteVersandart2 = "";
        eintrittskartePdfNr2 = 0;
        zutrittsIdent2 = "";
        zutrittsIdentNeben2 = "";
        eintrittskarteEmail2 = "";
        eintrittskarteEmail2Bestaetigen = "";
        eintrittskarteAbweichendeAdresse12 = "";
        eintrittskarteAbweichendeAdresse22 = "";
        eintrittskarteAbweichendeAdresse32 = "";
        eintrittskarteAbweichendeAdresse42 = "";
        eintrittskarteAbweichendeAdresse52 = "";
        vollmachtName2 = "";
        vollmachtVorname2 = "";
        vollmachtOrt2 = "";

        abstimmungsVorschlagIdent = 0;
        bestaetigtDassBerechtigt = false;
        jnBeiQuittung="";
    }

    
    
    
    /***************Sonder getter und setter****************************************/
    public void setAusgewaehlteHauptAktion(int ausgewaehlteHauptAktion) {
        this.ausgewaehlteHauptAktion = Integer.toString(ausgewaehlteHauptAktion);
    }

    public int getIntAusgewaehlteHauptAktion() {
        if (ausgewaehlteHauptAktion.isEmpty()) {return 0;}
        return Integer.parseInt(ausgewaehlteHauptAktion);
    }

    public void setAusgewaehlteAktion(int ausgewaehlteAktion) {
        this.ausgewaehlteAktion = Integer.toString(ausgewaehlteAktion);
    }

    public int getIntAusgewaehlteAktion() {
        return Integer.parseInt(ausgewaehlteAktion);
    }

    public void setEintrittskarteVersandart(int eintrittskarteVersandart) {
        this.eintrittskarteVersandart = Integer.toString(eintrittskarteVersandart);
    }

    public void setEintrittskarteVersandart2(int eintrittskarteVersandart2) {
        this.eintrittskarteVersandart2 = Integer.toString(eintrittskarteVersandart2);
    }

    /*********************Standard getter und setter*****************************************/

    /*Bleibt erhalten wg. JSF. Aber temporär auf deprecated wg. Umstellung im Java-Code*/
    @Deprecated
    public String getAusgewaehlteAktion() {
        return ausgewaehlteAktion;
    }

    @Deprecated
    public void setAusgewaehlteAktion(String ausgewaehlteAktion) {
        this.ausgewaehlteAktion = ausgewaehlteAktion;
    }

//    @Deprecated
//    public EclZugeordneteMeldungNeu getZugeordneteMeldungFuerAusfuehrung() {
//        return zugeordneteMeldungFuerAusfuehrung;
//    }
//
//    @Deprecated
//    public void setZugeordneteMeldungFuerAusfuehrung(EclZugeordneteMeldungNeu zugeordneteMeldungFuerAusfuehrung) {
//        this.zugeordneteMeldungFuerAusfuehrung = zugeordneteMeldungFuerAusfuehrung;
//    }

    /*Bleibt erhalten wg. JSF. Aber temporär auf deprecated wg. Umstellung im Java-Code*/
    @Deprecated
    public String getAusgewaehlteHauptAktion() {
        return ausgewaehlteHauptAktion;
    }

    @Deprecated
    public void setAusgewaehlteHauptAktion(String ausgewaehlteHauptAktion) {
        this.ausgewaehlteHauptAktion = ausgewaehlteHauptAktion;
    }

    public int getAbstimmungsVorschlagIdent() {
        return abstimmungsVorschlagIdent;
    }

    public void setAbstimmungsVorschlagIdent(int abstimmungsVorschlagIdent) {
        this.abstimmungsVorschlagIdent = abstimmungsVorschlagIdent;
    }

    public boolean isBestaetigtDassBerechtigt() {
        return bestaetigtDassBerechtigt;
    }

    public void setBestaetigtDassBerechtigt(boolean bestaetigtDassBerechtigt) {
        this.bestaetigtDassBerechtigt = bestaetigtDassBerechtigt;
    }

    @Deprecated
    public EclBesitzAREintrag getBesitzAREintrag() {
        return besitzAREintrag;
    }

    @Deprecated
    public void setBesitzAREintrag(EclBesitzAREintrag besitzAREintrag) {
        this.besitzAREintrag = besitzAREintrag;
    }

    public String getQuelle() {
        return quelle;
    }

    public void setQuelle(String quelle) {
        this.quelle = quelle;
    }

    public int getEingabeQuelle() {
        return eingabeQuelle;
    }

    public void setEingabeQuelle(int eingabeQuelle) {
        this.eingabeQuelle = eingabeQuelle;
    }

    public String getErteiltZeitpunkt() {
        return erteiltZeitpunkt;
    }

    public void setErteiltZeitpunkt(String erteiltZeitpunkt) {
        this.erteiltZeitpunkt = erteiltZeitpunkt;
    }

    public int getAbweichendeSammelkarte() {
        return abweichendeSammelkarte;
    }

    public void setAbweichendeSammelkarte(int abweichendeSammelkarte) {
        this.abweichendeSammelkarte = abweichendeSammelkarte;
    }

    public int getRcWillenserklaerungIdentAusgefuehrt() {
        return rcWillenserklaerungIdentAusgefuehrt;
    }

    public void setRcWillenserklaerungIdentAusgefuehrt(int rcWillenserklaerungIdentAusgefuehrt) {
        this.rcWillenserklaerungIdentAusgefuehrt = rcWillenserklaerungIdentAusgefuehrt;
    }

    @Deprecated
    public int getWeisungenSind() {
        return weisungenSind;
    }

    @Deprecated
    public void setWeisungenSind(int weisungenSind) {
        this.weisungenSind = weisungenSind;
    }

    @Deprecated
    public int getMeldungsIdent() {
        return meldungsIdent;
    }

    @Deprecated
    public void setMeldungsIdent(int meldungsIdent) {
        this.meldungsIdent = meldungsIdent;
    }

    @Deprecated
    public int getWillenserklaerungIdent() {
        return willenserklaerungIdent;
    }

    @Deprecated
    public void setWillenserklaerungIdent(int willenserklaerungIdent) {
        this.willenserklaerungIdent = willenserklaerungIdent;
    }

    @Deprecated
    public int getSammelIdent() {
        return sammelIdent;
    }

    @Deprecated
    public void setSammelIdent(int sammelIdent) {
        this.sammelIdent = sammelIdent;
    }

    @Deprecated
    public int getWeisungsIdent() {
        return weisungsIdent;
    }

    @Deprecated
    public void setWeisungsIdent(int weisungsIdent) {
        this.weisungsIdent = weisungsIdent;
    }

//    @Deprecated
//    public EclWillenserklaerungStatusNeu getZugeordneteWillenserklaerungStatus() {
//        return zugeordneteWillenserklaerungStatus;
//    }
//
//    @Deprecated
//    public void setZugeordneteWillenserklaerungStatus(
//            EclWillenserklaerungStatusNeu zugeordneteWillenserklaerungStatus) {
//        this.zugeordneteWillenserklaerungStatus = zugeordneteWillenserklaerungStatus;
//    }

    public boolean isGeberIstDerAktionaerSelbst() {
        return geberIstDerAktionaerSelbst;
    }

    public void setGeberIstDerAktionaerSelbst(boolean geberIstDerAktionaerSelbst) {
        this.geberIstDerAktionaerSelbst = geberIstDerAktionaerSelbst;
    }

    public List<EclBesitzAREintrag> getBesitzAREintragListe() {
        return besitzAREintragListe;
    }

    public void setBesitzAREintragListe(List<EclBesitzAREintrag> besitzAREintragListe) {
        this.besitzAREintragListe = besitzAREintragListe;
    }

    public int[] getGattungVorhanden() {
        return gattungVorhanden;
    }

    public void setGattungVorhanden(int[] gattungVorhanden) {
        this.gattungVorhanden = gattungVorhanden;
    }

    public List<EclZugeordneteMeldungNeu> getZugeordneteMeldungFuerAusfuehrungListe() {
        return zugeordneteMeldungFuerAusfuehrungListe;
    }

    public void setZugeordneteMeldungFuerAusfuehrungListe(
            List<EclZugeordneteMeldungNeu> zugeordneteMeldungFuerAusfuehrungListe) {
        this.zugeordneteMeldungFuerAusfuehrungListe = zugeordneteMeldungFuerAusfuehrungListe;
    }

    public List<EclWillenserklaerungStatusNeu> getZugeordneteWillenserklaerungStatusListe() {
        return zugeordneteWillenserklaerungStatusListe;
    }

    public void setZugeordneteWillenserklaerungStatusListe(
            List<EclWillenserklaerungStatusNeu> zugeordneteWillenserklaerungStatusListe) {
        this.zugeordneteWillenserklaerungStatusListe = zugeordneteWillenserklaerungStatusListe;
    }

    public List<Integer> getWeisungenSindListe() {
        return weisungenSindListe;
    }

    public void setWeisungenSindListe(List<Integer> weisungenSindListe) {
        this.weisungenSindListe = weisungenSindListe;
    }

    public List<Integer> getMeldungsIdentListe() {
        return meldungsIdentListe;
    }

    public void setMeldungsIdentListe(List<Integer> meldungsIdentListe) {
        this.meldungsIdentListe = meldungsIdentListe;
    }

    public List<Integer> getWillenserklaerungIdentListe() {
        return willenserklaerungIdentListe;
    }

    public void setWillenserklaerungIdentListe(List<Integer> willenserklaerungIdentListe) {
        this.willenserklaerungIdentListe = willenserklaerungIdentListe;
    }

    public List<Integer> getSammelIdentListe() {
        return sammelIdentListe;
    }

    public void setSammelIdentListe(List<Integer> sammelIdentListe) {
        this.sammelIdentListe = sammelIdentListe;
    }

    public List<Integer> getWeisungsIdentListe() {
        return weisungsIdentListe;
    }

    public void setWeisungsIdentListe(List<Integer> weisungsIdentListe) {
        this.weisungsIdentListe = weisungsIdentListe;
    }

    public int getListeARoderMeldungen() {
        return listeARoderMeldungen;
    }

    public void setListeARoderMeldungen(int listeARoderMeldungen) {
        this.listeARoderMeldungen = listeARoderMeldungen;
    }

    public String getEintrittskarteVersandart() {
        return eintrittskarteVersandart;
    }

    public void setEintrittskarteVersandart(String eintrittskarteVersandart) {
        this.eintrittskarteVersandart = eintrittskarteVersandart;
    }

    public String getEintrittskarteEmail() {
        return eintrittskarteEmail;
    }

    public void setEintrittskarteEmail(String eintrittskarteEmail) {
        this.eintrittskarteEmail = eintrittskarteEmail;
    }

    public String getEintrittskarteEmailBestaetigen() {
        return eintrittskarteEmailBestaetigen;
    }

    public void setEintrittskarteEmailBestaetigen(String eintrittskarteEmailBestaetigen) {
        this.eintrittskarteEmailBestaetigen = eintrittskarteEmailBestaetigen;
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

    public int getPersonNatJurOeffentlicheID() {
        return personNatJurOeffentlicheID;
    }

    public void setPersonNatJurOeffentlicheID(int personNatJurOeffentlicheID) {
        this.personNatJurOeffentlicheID = personNatJurOeffentlicheID;
    }

    public boolean isUeberOeffentlicheID() {
        return ueberOeffentlicheID;
    }

    public void setUeberOeffentlicheID(boolean ueberOeffentlicheID) {
        this.ueberOeffentlicheID = ueberOeffentlicheID;
    }

    public boolean isVollmachtEingeben() {
        return vollmachtEingeben;
    }

    public void setVollmachtEingeben(boolean vollmachtEingeben) {
        this.vollmachtEingeben = vollmachtEingeben;
    }

    public String getEintrittskarteVersandart2() {
        return eintrittskarteVersandart2;
    }

    public void setEintrittskarteVersandart2(String eintrittskarteVersandart2) {
        this.eintrittskarteVersandart2 = eintrittskarteVersandart2;
    }

    public String getEintrittskarteEmail2() {
        return eintrittskarteEmail2;
    }

    public void setEintrittskarteEmail2(String eintrittskarteEmail2) {
        this.eintrittskarteEmail2 = eintrittskarteEmail2;
    }

    public String getEintrittskarteEmail2Bestaetigen() {
        return eintrittskarteEmail2Bestaetigen;
    }

    public void setEintrittskarteEmail2Bestaetigen(String eintrittskarteEmail2Bestaetigen) {
        this.eintrittskarteEmail2Bestaetigen = eintrittskarteEmail2Bestaetigen;
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

    public boolean isVollmachtEingeben2() {
        return vollmachtEingeben2;
    }

    public void setVollmachtEingeben2(boolean vollmachtEingeben2) {
        this.vollmachtEingeben2 = vollmachtEingeben2;
    }

    public String getVollmachtOrt2() {
        return vollmachtOrt2;
    }

    public void setVollmachtOrt2(String vollmachtOrt2) {
        this.vollmachtOrt2 = vollmachtOrt2;
    }

    public String getZutrittsIdent() {
        return zutrittsIdent;
    }

    public void setZutrittsIdent(String zutrittsIdent) {
        this.zutrittsIdent = zutrittsIdent;
    }

    public String getZutrittsIdentNeben() {
        return zutrittsIdentNeben;
    }

    public void setZutrittsIdentNeben(String zutrittsIdentNeben) {
        this.zutrittsIdentNeben = zutrittsIdentNeben;
    }

    public String getZutrittsIdent2() {
        return zutrittsIdent2;
    }

    public void setZutrittsIdent2(String zutrittsIdent2) {
        this.zutrittsIdent2 = zutrittsIdent2;
    }

    public String getZutrittsIdentNeben2() {
        return zutrittsIdentNeben2;
    }

    public void setZutrittsIdentNeben2(String zutrittsIdentNeben2) {
        this.zutrittsIdentNeben2 = zutrittsIdentNeben2;
    }

    public int getRcWillenserklaerungIdentAusgefuehrtZweit() {
        return rcWillenserklaerungIdentAusgefuehrtZweit;
    }

    public void setRcWillenserklaerungIdentAusgefuehrtZweit(int rcWillenserklaerungIdentAusgefuehrtZweit) {
        this.rcWillenserklaerungIdentAusgefuehrtZweit = rcWillenserklaerungIdentAusgefuehrtZweit;
    }

    public int getEintrittskartePdfNr() {
        return eintrittskartePdfNr;
    }

    public void setEintrittskartePdfNr(int eintrittskartePdfNr) {
        this.eintrittskartePdfNr = eintrittskartePdfNr;
    }

    public int getEintrittskartePdfNr2() {
        return eintrittskartePdfNr2;
    }

    public void setEintrittskartePdfNr2(int eintrittskartePdfNr2) {
        this.eintrittskartePdfNr2 = eintrittskartePdfNr2;
    }

    public boolean isBestaetigungErmoeglichen() {
        return bestaetigungErmoeglichen;
    }

    public void setBestaetigungErmoeglichen(boolean bestaetigungErmoeglichen) {
        this.bestaetigungErmoeglichen = bestaetigungErmoeglichen;
    }

    public String getArt() {
        return art;
    }

    public void setArt(String art) {
        this.art = art;
    }

    public String getAusstellungsZeit() {
        return ausstellungsZeit;
    }

    public void setAusstellungsZeit(String ausstellungsZeit) {
        this.ausstellungsZeit = ausstellungsZeit;
    }

    public String getLfdNrPDF() {
        return lfdNrPDF;
    }

    public void setLfdNrPDF(String lfdNrPDF) {
        this.lfdNrPDF = lfdNrPDF;
    }

    public String getNummerBestaetigung() {
        return nummerBestaetigung;
    }

    public void setNummerBestaetigung(String nummerBestaetigung) {
        this.nummerBestaetigung = nummerBestaetigung;
    }

    public String getKennungVeranstaltung() {
        return kennungVeranstaltung;
    }

    public void setKennungVeranstaltung(String kennungVeranstaltung) {
        this.kennungVeranstaltung = kennungVeranstaltung;
    }

    public String getIsin() {
        return isin;
    }

    public void setIsin(String isin) {
        this.isin = isin;
    }

    public String getDatum() {
        return datum;
    }

    public void setDatum(String datum) {
        this.datum = datum;
    }

    public String getNameEmittent() {
        return nameEmittent;
    }

    public void setNameEmittent(String nameEmittent) {
        this.nameEmittent = nameEmittent;
    }

    public String getNameBestaetigender() {
        return nameBestaetigender;
    }

    public void setNameBestaetigender(String nameBestaetigender) {
        this.nameBestaetigender = nameBestaetigender;
    }

    public String getNameAbstimmender() {
        return nameAbstimmender;
    }

    public void setNameAbstimmender(String nameAbstimmender) {
        this.nameAbstimmender = nameAbstimmender;
    }

    public String getEmailFuerBestaetigung() {
        return emailFuerBestaetigung;
    }

    public void setEmailFuerBestaetigung(String emailFuerBestaetigung) {
        this.emailFuerBestaetigung = emailFuerBestaetigung;
    }

    public boolean isZusammenfassenVonAnmeldungenMoeglich() {
        return zusammenfassenVonAnmeldungenMoeglich;
    }

    public void setZusammenfassenVonAnmeldungenMoeglich(boolean zusammenfassenVonAnmeldungenMoeglich) {
        this.zusammenfassenVonAnmeldungenMoeglich = zusammenfassenVonAnmeldungenMoeglich;
    }

    public int getGattungEinzigeVorhanden() {
        return gattungEinzigeVorhanden;
    }

    public void setGattungEinzigeVorhanden(int gattungEinzigeVorhanden) {
        this.gattungEinzigeVorhanden = gattungEinzigeVorhanden;
    }

    public String getJnBeiQuittung() {
        return jnBeiQuittung;
    }

    public void setJnBeiQuittung(String jnBeiQuittung) {
        this.jnBeiQuittung = jnBeiQuittung;
    }

    public String getNzVertretungsart() {
        return nzVertretungsart;
    }

    public void setNzVertretungsart(String nzVertretungsart) {
        this.nzVertretungsart = nzVertretungsart;
    }

    public String getNzGpNummer() {
        return nzGpNummer;
    }

    public void setNzGpNummer(String nzGpNummer) {
        this.nzGpNummer = nzGpNummer;
    }

    public String getNzVerband() {
        return nzVerband;
    }

    public void setNzVerband(String nzVerband) {
        this.nzVerband = nzVerband;
    }

    public String getAktionaersnummer() {
        return aktionaersnummer;
    }

    public void setAktionaersnummer(String aktionaersnummer) {
        this.aktionaersnummer = aktionaersnummer;
    }

    public String getArtUmfassend() {
        return artUmfassend;
    }

    public void setArtUmfassend(String artUmfassend) {
        this.artUmfassend = artUmfassend;
    }

    public boolean isAnmeldungDurchgefuehrt() {
        return anmeldungDurchgefuehrt;
    }

    public void setAnmeldungDurchgefuehrt(boolean anmeldungDurchgefuehrt) {
        this.anmeldungDurchgefuehrt = anmeldungDurchgefuehrt;
    }

    public String getEkNummer() {
        return ekNummer;
    }

    public void setEkNummer(String ekNummer) {
        this.ekNummer = ekNummer;
    }

    public String getTopListe() {
        return topListe;
    }

    public void setTopListe(String topListe) {
        this.topListe = topListe;
    }

    public boolean isBestaetigenPerMailDurchfuehren() {
        return bestaetigenPerMailDurchfuehren;
    }

    public void setBestaetigenPerMailDurchfuehren(boolean bestaetigenPerMailDurchfuehren) {
        this.bestaetigenPerMailDurchfuehren = bestaetigenPerMailDurchfuehren;
    }

    public String getAktionaersname() {
        return aktionaersname;
    }

    public void setAktionaersname(String aktionaersname) {
        this.aktionaersname = aktionaersname;
    }


    public boolean isPdfWurdeBereitsErzeugt() {
        return pdfWurdeBereitsErzeugt;
    }

    public void setPdfWurdeBereitsErzeugt(boolean pdfWurdeBereitsErzeugt) {
        this.pdfWurdeBereitsErzeugt = pdfWurdeBereitsErzeugt;
    }

    public String getBevollmaechtigterVorname() {
        return bevollmaechtigterVorname;
    }

    public void setBevollmaechtigterVorname(String bevollmaechtigterVorname) {
        this.bevollmaechtigterVorname = bevollmaechtigterVorname;
    }

    public String getBevollmaechtigterName() {
        return bevollmaechtigterName;
    }

    public void setBevollmaechtigterName(String bevollmaechtigterName) {
        this.bevollmaechtigterName = bevollmaechtigterName;
    }

    public String getBevollmaechtigterOrt() {
        return bevollmaechtigterOrt;
    }

    public void setBevollmaechtigterOrt(String bevollmaechtigterOrt) {
        this.bevollmaechtigterOrt = bevollmaechtigterOrt;
    }

}
