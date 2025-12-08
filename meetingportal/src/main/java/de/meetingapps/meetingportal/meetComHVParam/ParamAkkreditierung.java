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

public class ParamAkkreditierung implements Serializable {
    private static final long serialVersionUID = -4622305217342699804L;

    public boolean gaesteKartenHabenWiederzugangAbgangsCode = false; //400

    /**Verwendungszweck:
     * Im Normalfall steht das auf false - d.h.:
     * > Zugang mit Eintrittskarte, Abgang/Vertreterwechsel etc. mit Stimmkarte 
     * > bei präsentem Aktionär und Eingabe einer Eintrittskartennummer (ohne Aktionsnummer)
     * 		keine Abgang/Vollmachtswechsel-Funktion möglich.
     * 
     * Aber z.B. Mitgliederausweis als Zugangs/Abgangsmedium: 
     * > dann steht das auf True
     * > eine Abgangsbuchung ist auch mit der Eintrittskarte möglich.
     * 
     */
    public boolean eintrittskarteWirdAuchFuerAbgangWiederzugangVerwendet = true; //401
    
    /**Entspricht in etwa dem früheren "Tausch beliebig". 
     * Alt: Mus in der Oberfläche abgefangen werden.
     * Neu: Wenn true, dann wird die Eintrittskarte automatisch zur ersten zugeordneten Stimmkarte werden.
     * D.h. also entweder bei Tausch 1:1, oder wenn Stimmunterlagen mit an den Aktionär verschickt wurden.
     * Wichtig: Stimmkarten[0] wird dafür verwendet!
     * 
     * 19.01.2025: obige Beschreibung scheint nicht mehr passend :-(
     */
    public boolean eintrittskarteWirdStimmkarte = false; //402

    /**Falls die verarbeitete / gebuchte Nummer (QR-Code) aus einer App gelesen wurde (bei der Akkreditierung),
     * wird dann ebenfalls Labels ausgedruckt für neu zugeordnete Stimmkarten?
     * Muss in der Oberfläche abgefangen werden.
     * 
     * Begründung für diesen Parameter: die Zuordnung kann (wenn mal alles fertig ist) auch in der App
     * gesehen werden - deshalb ist das Label überflüssig (bzw. das Entfallen trägt zum Datenschutz bei)
     */
    public boolean labelAuchFuerAppIdent = false;//403

    /**1= Vertretername / Vertretervorname bei Erfassung; 2=Vertretervorname/Vertretername bei Erfassung*/
    public int positionVertretername = 2; //404

    /**Steuert Delay während der Abstimmung
     * 1 = Standardeinstellung. Zugänge werden gebucht, Abgänge delayed.
     * 2 = Sondervariante. Zugänge werden delayed, Abgänge gebucht. Ist sinnvoll, wenn Türen des Saals während der Abstimmung geschlossen sind. 
     * 3 = Sondervariante. Zugänge und Abgänge werden delayed
     */
    public int delayArt = 1; //405

    /**0 = keine Veränderung der Sammelkarte; Stornierung aus Sammelkarte muß manuell durchgeführt werden, sonst keine Präsenzbuchung möglich
     * 1 = automatische Stornierung der Sammelkarte; bei Präsenzbuchung erfolgt Warnhinweis
     * TODO Akkreditierung: Stornierung aus Sammelkarte nur teilweise implementiert, aktuell wohl nur für ku178
     * 2 = inaktiv-Setzen der Sammelkarte
     * TODO Akkreditierung: 2 nur geplant; erfordert ausführliche Tests der Inaktivität
     * 
     */
    public int beiZugangStornierenAusSammelAutomatisch = 0; //406 tfBeiZugangStornierenAusSammelAutomatisch
    
    /**=1 Wenn für eine Meldung ein Vertreter eingetragen ist, und der Aktionär selbst kommt, ist der Zugang möglich.
     * Bei 0 nicht (dann muß quasi vorher der Vertreter storniert werden)
     */
    public int beiZugangSelbstVertreterIgnorierenZulaessig=1; //tfBeiZugangSelbstVertreterIgnorierenZulaessig 2592

    /**=1 Meldungen, die als "ungeprüft" gekennzeichnet sind (derzeit: zusatzfeld1=-1 oder -2) dürfen nicht gebucht werden*/
    public int ungepruefteKartenNichtBuchen=0; //tfUngepruefteKartenNichtBuchen 2593
    
    /**Stornieren aus einer Sammel-Karte (bei HV-Besuch) ist zwingend (wenn nicht mehr möglich, dann kein Zugang!
     * Position in Array= Sammelkartenart
     * 
     * Derzeit gibt es dafür keine Oberfläche. Die Frage ist, warum das trotz Standardeinstellung==0 funktioniert ...
     * Wirkung mal testen*/
    public int[] pPraesenzStornierenAusSammelZwingend = { 0, 0, 0, 0, 0, 0 }; //17, Position 0 bis 5

    /**=1 => nach Abschluß der Buchung wird in einer Meldung angezeigt, ob bzw. welche Stimmkarten an den Teilnehmer auszugeben sind.*/
    public int auszugebendeStimmkartenInMeldungAnzeigen=0; //tfAuszugebendeStimmkartenInMeldungAnzeigen 2594
    
    /**Diese Stimmkarte ist aktiviert (d.h. wird für Druck angeboten, und ggf. bei der Akkreditierung berücksichtigt*/
    public int[][] pPraesenzStimmkarteAktiv=null; //{0,0,0,0} Lang, 118 - Offset 0 bis 24. 0 bis 4 erste Gattung, etc.
   
    /**[Gattung][StimmkartenNr] - Zuordnung von Stimmkarten zur Gattung [1-5 minus 1]; Maximal 4 Nummernkreise + virtuell=5*/
    public int[][] pPraesenzStimmkartenZuordnenGattung = null; //{0,0,0,0}; 18, 0 bis 4
    /**Zuordnung von Stimmkarten zur Gattung 1 bei Verwendung der App; Maximal 4 Nummernkreise*/
    public int[][] pPraesenzStimmkartenZuordnenAppGattung = null; //{0,0,0,0}; 19, 0 bis 4
    /**Bezeichnugnen für die einzelnen, zuzuordnenden Stimmkartennummernkreise gemäß pPraesenzStimmkartenZuordnenGattung1*/
    public String[][] pPraesenzStimmkartenZuordnenGattungText = null; //{"Stimmblock","ErgänzungsStimmblock","",""}; 700 bis 724, 700 bis 704 erste Gattung
    /**Zuordnung von StimmkarteSecond zur Gattung 1*/
    /*XXX*/
    public String[][] pPraesenzStimmkarteFormularnummer=null; //{0,0,0,0} Lang, 118, Offset 25 bis 74 (2-stellig)
    /**1 => Tausch findet beliebig statt; 0 => 1:1 Zuordnung*/
    public int[][] pPraesenzStimmkarteTauschBeliebig=null; // Lang, 118, Offset 75 bis 99
    /**1 => Diese Stimmkarte wird zum Nachdruck am Servicedesk verwendet*/
    public int[][] pPraesenzStimmkarteNachdruckServiceDesk=null; //Lang, 118, Offset 100 bis 124
    /**01 etc.  => Diese Stimmkarte wird bei jedem Erstzugang auf dem Drucker gedruckt. Leer = kein Druck*/
    public String[][] pPraesenzStimmkarteDruckerBeiErstzugang=null;  //Lang, 118, Offset 125 bis 174 (2stellig)
    
    public int[] pPraesenzStimmkarteSecondZuordnenGattung = null; //0; Parameter-Nr.23, Offset 0 bis 4
    public String[] pPraesenzStimmkarteSecondZuordnenGattungText = null; //"Televoter"; 725 bis 729

    /**Gattung = 1 bis 5*/
    public boolean stimmkartenZumZuordnenVorhanden(int pGattung) {
        int anz=0;
        for (int i=0;i<4;i++) {
            if (pPraesenzStimmkarteAktiv[pGattung-1][i]==1) {
                if (pPraesenzStimmkartenZuordnenGattung[pGattung-1][i]==1) {
                    anz++;
                }
            }
        }
        if (anz>0) {return true;}
        return false;
    }
    
    /**Gattung = 1 bis 5*/
    public boolean stimmkartenZumDruckenAmServiceDeskVorhanden(int pGattung) {
        int anz=0;
        for (int i=0;i<4;i++) {
            if (pPraesenzStimmkarteAktiv[pGattung-1][i]==1) {
                if (pPraesenzStimmkarteNachdruckServiceDesk[pGattung-1][i]==1) {
                    anz++;
                }
            }
        }
        if (anz>0) {return true;}
        return false;
    }

    /**Gattung = 1 bis 5*/
    public boolean stimmkartenTauschBeliebigVorhanden(int pGattung) {
        int anz=0;
        for (int i=0;i<4;i++) {
            if (pPraesenzStimmkarteAktiv[pGattung-1][i]==1) {
                if (pPraesenzStimmkarteTauschBeliebig[pGattung-1][i]==1) {
                    anz++;
                }
            }
        }
        if (anz>0) {return true;}
        return false;
    }

    //	/**Zuordnung von Stimmkarten zur Gattung 2; Maximal 4 Nummernkreise*/
    //	public  int[] pPraesenzStimmkartenZuordnenGattung2={0,0,0,0};
    //	/**Zuordnung von Stimmkarten zur Gattung 2 bei Verwendung der App; Maximal 4 Nummernkreise*/
    //	public  int[] pPraesenzStimmkartenZuordnenAppGattung2={0,0,0,0};
    //	/**Bezeichnugnen für die einzelnen, zuzuordnenden Stimmkartennummernkreise gemäß pPraesenzStimmkartenZuordnenGattung2*/
    //	public  String[] pPraesenzStimmkartenZuordnenGattung2Text={"VorzugsStimmblock","VorzugsErgänzungsStimmblock","",""};
    //	/**Zuordnung von StimmkarteSecond zur Gattung 2*/
    //	public  int pPraesenzStimmkarteSecondZuordnenGattung2=0;
    //	public  String pPraesenzStimmkarteSecondZuordnenGattung2Text="Televoter";

    /**Zuordnung von Stimmkarten, auch wenn der Zugang mit einer AppIdent erfolgt - 18.01.2025 entfernt*/
//    public int ppPraesenzStimmkartenZuordnenAuchBeiAppIdent = 0; //32

    /**Erscheint eine Person zur HV, dann:
     * =0 => andere Vollmachten bleiben bestehen
     * =1 => andere Vollmachten werden automatisch widerrufen
     * =2 => andere Vollmachten müssen in Textform widerrufen werden
     * 
     * Funktion aktuell nicht freigegeben. In "alter" Parameterpflege keine Oberfläche
     */
    public int pPraesenzBeiErscheinenAndereVollmachtenStornieren = 0; //33

    /**Stornieren aus einer Sammel-Karte (bei HV-Besuch) ist noch möglich
     * 
     * Funktion aktuell nicht freigegeben. In "alter" Parameterpflege keine Oberfläche*/
    public int[] pLfdPraesenzStornierenAusSammelMoeglich = { 0, 1, 1, 1, 1, 1 }; //508, Offset 0 bis 5
    public int[] pLfdPraesenzDeaktivierenAusSammelMoeglich = { 0, 1, 1, 1, 1, 1 };//509, Offset 0 bis 5

    /**Erteilen Vollmacht/Weisung in eine Sammelkarte bbei HV-Besuch - Abgang) ist noch möglich
     * 
     * Funktion aktuell nicht freigegeben. In "alter" Parameterpflege keine Oberfläche
     * */
    public int[] pLfdPraesenzErteilenInSammelMoeglich = { 0, 1, 1, 1, 1, 1 }; //510, Offset 0 bis 5

    /**1 HV ist bereits gestartet, ab jetzt Präsenzsummen korrigieren etc.
     * Wird nach ersten Recherchen schon nirgendwo mehr verwendet!*/
    @Deprecated
    public int plfdHVGestartet = 0; //512

    
    /***********************************Delay- und Pending-Funktionen***********************************************/
     /**Delayed: Delayed 0=kein Delay,1=Abstimmung, 2=Auswertung. Verwendung sollte über die entsprechenden
     * Funktionen erfolgen
     * 
     * Zur Verwendung der Stufen:
     * Stufe 1 = während der Abstimmung. Hier sind je nach Konfiguration noch vereinzelte Willenserklärungen möglich.
     * Stufe 2 = Während Auswertung. Hier sind alle Willenserklärungen delayed, um quasi den Stand am Abstimmungsende für die
     *          Dauer der Abstimmung einzufrieren.
     * */
    public int plfdHVDelayed = 0; //513 - keine Parameterpflege-Oberfläche!

    /**Hinweis zu den folgenden Funktionen: diese sind redundant in ParamAkkreditierung und DbBundle enthalten,
     * um je nach Situation leichten Zugriff zu erreichen. Unbedingt immer identisch halten!
     */
    
    /**++++++++Alte Delay-Funktionen - derzeit keine Logik dahinter. Nur aufgenommen, 
     * um die Reste aus DbBasis zu entfernen. Auch keine Oberfläche, und kein Speichern in der Datenbank ++++++*/
    /** Delay für alle Willenserklärungen */
    @Deprecated
    private boolean lDelayAll = false;
    @Deprecated
    private boolean lDelayAufloesenAbZuGleicheIdent = false;
    @Deprecated
    private boolean lDelayAufloesenAbZuVerschiedeneIdent = false;
    @Deprecated
    private boolean lDelayWiderrufVollmachtBriefwahl = false;

    
    /**
     * Verwaltung des "Delay" von Willenserklärungen Parameter = Willenserklärung Rückgabe = true => Willenserklärung is
     * delayed, ansonsten false
     */
    @Deprecated
    public boolean willenserklaerungIstDelayed(int willenserklaerung) {
        if (lDelayAll == true) {
            return true;
        }
        return false;
    }

    /**
     * Falls true => erfolgt Zu/Abgang mit der selben Ident, dann: > delayed Zugang mit Abgang wird aufgelöst > delayed
     * Abgang mit Zugang wird aufgelöst
     */
    @Deprecated
    public boolean delayAufloesenAbZuGleicheIdent() {
        return lDelayAufloesenAbZuGleicheIdent;
    }

    /**
     * Falls true => erfolgt Zu/Abgang auch ggf. mit unterschiedlichen Ident, dann: > delayed Zugang mit Abgang wird
     * aufgelöst > delayed Abgang mit Zugang wird aufgelöst
     */
    @Deprecated
   public boolean delayAufloesenAbZuVerschiedeneIdent() {
        return lDelayAufloesenAbZuVerschiedeneIdent;
    }

    /**
     * Falls true, dann kann eine Vollmacht, oder auch Briefwahl, die delayed ist, widerrufen werden und der Vorgang
     * wird dann "nicht-delayed" durchgeführt.
     */
    @Deprecated
    public boolean delayWiderrufVollmachtBriefwahl() {
        return lDelayWiderrufVollmachtBriefwahl;
    }

    
    /**++++++++Pending - derzeit keine Logik dahinter. Nur aufgenommen, um die Reste aus DbBasis zu entfernen
     * Weder Oberfläche noch Speicherung in Datenbank+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
    
    public boolean pendingMoeglich=false;
    public boolean pendingZulaessig=false;
    
    public boolean pendingIstMoeglich(int willenserklaerung) {
        return pendingMoeglich;
    }
    public boolean pendingIstZulaessig() {
        return pendingZulaessig;
    }
    
    
    /***********************************Delay- und Pending-Funktionen Ende***********************************************/

   
    public int skOffenlegungSRV = 1; //407
    public int skOffenlegungKIAV = 1; //408
    public int skOffenlegungDauer = 1; //409
    public int skOffenlegungOrga = 1; //410

    //Vorher: cbLabelAuchFuerAppIdent
    
    /**1 => nach der Buchung eines Erstzugangs wird analog zum Label (aber Parametermäßig unabhängig von diesem)
     * ein Formular ausgedruckt, z.B. zum Ausdruck von Zugangsdaten
     * 
     * Achtung - wenn auf 1, dann muß auf Front-Office-Clients der Zugriff auf die Reports-Datenstruktur
     * möglich sein!
     * */
    public int formularNachErstzugang = 0; //tfFormularNachErstzugang 411

    /**1 => bei einer Erstzugangsbuchung wird für die Kennung (nicht für Sammelkarten!) des Aktionärs ein
     * neues Initialpasswort vergeben, das zusätzlich zum möglicherweise bereits vergebenen Passwort gilt.
     * 
     * Das ganze ist möglicherweise noch verfeinerungsbedürftig, denn:
     * a) Vertreterproblematik
     * b) Problematik bei mehreren Meldungen pro Aktionär
     * 
     * TODO - derzeit nicht implementiert. Sollte für ku217 verwendet werden, ging aber dort nicht
     * wegen getrennten Systemen.
     */
    public int zusaetzlichesInitialpasswortBeiErstzugang=0; //tfZusaetzlichesInitialpasswortBeiErstzugang 412
    
    /**ServiceDesk*/
    public int serviceDeskSetNr=1; //tfServiceDeskSetNr 413 tfUngepruefteKartenNichtBuchen
    
//  @formatter:off
    /********************************Labeldrucker*************************************/

    /**0=Standard; Druck nur bei Stimmkartentausch beliebig
     * 1=für Tausch 1:1, wenn Vertreter eingetragen
     */
    public int labeldruckVerfahren=0; //tbl_parameter 415
    
    /**0=???LabelPrinter???*/
    public int label_Font=0; //tbl_parameter 414
    
    /*Wird in tbl_parameterlang gespeichert:
     * XXXYYYSSBBString 172 bis 181 - Lang
     */

    public int[] label_xPos= {25, 25, 25, 25, 25, 25, 25, 0, 0, 0};
    public int[] label_yPos= {40, 70, 110, 140, 180, 220, 260, 0, 0, 0};
    public int[] label_fontSize= {5, 5, 5, 5, 6, 6, 5, 0, 0, 0};
    
    /**Zeile wird nur dann ausgegeben, wenn Bedingung erfüllt ist.
     * 0 => wird immer gedruckt (außer labelString gleich leer)
     * 1 => wird nur gedruckt, wenn nameVertreter nicht leer ist
     * 2 => wird nur gedruckt, wenn SKNummer nicht leer ist
     */
    public int[] label_zeilenBedingung= {0, 0, 1, 1, 2, 0, 0, 0, 0, 0};
    /**Maximale Länge=170*/
    public String[] labelString= {
            "<<<VornameAktionaer>>> <<<NachnameAktionaer>>>",
            "<<<OrtAktionaer>>>",
            "Vertr.: <<<VornameVertreter>>> <<<NachnameVertreter>>>",
            "<<<OrtVertreter>>>",
            "<<<SKNummer>>>",
            "<<<EKNummer>>>",
            "<<<Aktienzahl>>>",
            "",
            "",
            ""
    };
    /**tbl_parameterlang, 182*/
    public String label_QRCode="^FO365,140^FH\\^BQ,,4\n^FDQA,<<<SKNummerBar>>>^FS";
//          @formatter:on
    
    
    
    
    public ParamAkkreditierung() {
        pPraesenzStimmkarteAktiv = new int[5][5];
        pPraesenzStimmkartenZuordnenGattung = new int[5][5];
        pPraesenzStimmkartenZuordnenAppGattung = new int[5][5];
        pPraesenzStimmkartenZuordnenGattungText = new String[5][5];
        pPraesenzStimmkarteFormularnummer = new String[5][5];
        pPraesenzStimmkarteTauschBeliebig = new int[5][5];
        pPraesenzStimmkarteNachdruckServiceDesk = new int[5][5];
        pPraesenzStimmkarteDruckerBeiErstzugang = new String[5][5];
      
        pPraesenzStimmkarteSecondZuordnenGattung = new int[5];
        pPraesenzStimmkarteSecondZuordnenGattungText = new String[5];
        for (int i = 0; i < 5; i++) {
            for (int i1 = 0; i1 < 5; i1++) {
                pPraesenzStimmkarteAktiv[i][i1] = 0;
                pPraesenzStimmkartenZuordnenGattung[i][i1] = 0;
                pPraesenzStimmkartenZuordnenAppGattung[i][i1] = 0;
                pPraesenzStimmkartenZuordnenGattungText[i][i1] = "Stimmblock";

                pPraesenzStimmkarteFormularnummer[i][i1] = "01";
                pPraesenzStimmkarteTauschBeliebig[i][i1] = 0;
                pPraesenzStimmkarteNachdruckServiceDesk[i][i1] = 0;
                pPraesenzStimmkarteDruckerBeiErstzugang[i][i1] = "";

            }
            pPraesenzStimmkarteSecondZuordnenGattung[i] = 0;
            pPraesenzStimmkarteSecondZuordnenGattungText[i] = "Televoter";
        }
    }

}
