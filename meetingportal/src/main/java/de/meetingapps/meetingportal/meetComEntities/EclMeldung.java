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

import de.meetingapps.meetingportal.meetComDb.DbBundle;

public class EclMeldung implements Cloneable, Serializable {
    private static final long serialVersionUID = 2019824777297024846L;

    private int logDrucken = 10;

    /** eindeutiger Key für Meldesatz (zusammen mit mandant), der unveränderlich ist. Über MeldungsIdent Zuordnung z.B. 
     * zu Stimmausschlüssen, zu virtuellen Karten, in andere Dateien etc.
     */
    public int meldungsIdent = 0;

    /**Versionsnummer - manuell nicht verändern! Ausschließlich durch Verwendung in DbMeldungen.
     * 
     * Dient auch dazu, um während der Online-Teilnahme im Puffer gehaltene Meldungssätze zu überprüfen, ob
     * neue Willenserklärungen für diese erteilt wurden (um diese dann weiter einzulesen). D.h. bei Veränderung
     * der Willenserklärung konsequent darauf achten, dass db_version hochgezählt wird!
     */
    public long db_version;

    /**Versionsnummer von lPersonNatJur - wird benötigt für Update etc., wenn personNatJur aus dbMeldungen heraus "gefüttert" wird*/
    public long db_version_personenNatJur = 0;

    /**Mandantennummer*/
    public int mandant = 0;

    /** =1 => ist aktiv; =2 => stornierter Meldungssatz; 
     * (Verarbeitungshinweis: wenn ein Meldungssatz storniert wird, müssen damit auch alle 
     * zutrittsIdent und Stimmkarten gesperrt werden!)
     * 
     * Früher auch noch: ist aber mittlerweile stillgelegt!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
     *  =3 => stornierte ZutrittsIdent
     *  =4 => stornierte Stimmkarte; =5 => stornierte StimmkarteSecond
     *  
     *  Verarbeitungshinweis:
     *  3, 4, und 5 sind unabhängig vom eigentlichen Meldesatz zu sehen, d.h. in diesen Fällen muß
     *  jeweils ein eigener Meldungssatz (verknüpft über meldungsIdentNachfolger) angelegt werden,
     *  so dass die eigentliche Anmeldung weiter erhalten bleibt (und dementsprechend z.B. bei
     *  einem stornierten Aktionär weiterhin als Gast teilnehmen kann)
     *  
     *  D.h. konkret: es muß unterschieden werden zwischen stornierten Identifikationen, und
     *  stornierten Meldungen!
     *  (Nebenbei: soll z.B. mit aller Gewalt verhindert werden, dass eine bestimmte stornierte Meldung als
     *  Gast zugeht [auch wenn dies allgemein zulässig wäre], dann kann dies z.B. über ein VIP-KZ gelöst werden)
     *  
     *  D.h. aber auch für einen Aktionär:
     *  > Stornierte ZutrittsIdent -> mit dieser ZutrittsIdent ist nichts mehr möglich, auch kein Gastzugang
     *  	z.B. wenn Eintrittskarte vergessen wurde, und diese ersetzt wurde
     *  > Stornierte Meldung -> die Meldung an sich ist storniert, die ZutrittsIdent für einen Aktionärszugang
     *  	nicht mehr gültig, sie kann jedoch als Gastzugang weiter verwendet werden.
     *  
     *  
     *  Zum Handhaben der Stimmkarten: "wiederverwendbare" Stimmkarten (z.B. Televoter) dürfen nicht als stornierte
     *  behandelt werden - diese werden nur in den Willenserklärungen mit gespeichert, die jeweils
     *  aktuelle im zugehörigen Meldungssatz. Allerdings können auch hier tatsächlich "gesperrte" Stimmkarten
     *  weiterhin als "storniert" in der Meldungsdatei vorhanden sein (z.B.: Televoter verloren).
     *  */
    public int meldungAktiv = 1;

    /** 0 Gast, 1 = aktienrechtliche Anmeldung; Achtung, früher umgekehrt!
     * Zusammen mit zutrittsIdent und mandant UNIQUE
     */
    public int klasse = 0;

    /**Aktionärsnummer aus Aktienregister
     * Length=20
     */
    public String aktionaersnummer = "";

    /**Eigentlich Redundanz zu Aktionärsnummer aus Aktienregister, aber wg. einfacher Suche erforderlich.
     * Achtung: muß auch für die Gastkarten, die von dieser AktienregisterIdent aus gefüllt wurden,
     * entsprechend gesetzt werden.*/
    public int aktienregisterIdent = 0;

    /**Key, mit dem auf externe Systeme verwiesen wird (Eindeutig, außer bei Split!)*/
    public String externeIdent = "";

    /**0 = Gastkarte; 1 = Einzelkarte-Aktionär; 2 = Sammelkarte; 3 = in Sammelkarte
     * Entsprechend EnMeldungKartenart */
    public int meldungstyp = 0;

    /**falls meldungstyp=2 Sammelkarte: 1 => Sammelkarte nimmt Weisungen auf
     * Verwendung derzeit unklar - ist ja eigentlich jetzt in skWeisungsartZulaessig abgebildet*/
    @Deprecated
    public int skMitWeisungen = 0;

    /**falls Meldungstyp=2 Sammelkarte: (siehe: IntMeldungInSammelkarte)
     *1=KIAV
     *2=SRV
     *3=organisatorisch
     *4=Briefwahl
     *5=Dauervollmacht
     *Werte gemäß KonstSkIst*/
    public int skIst = 0;

    /**Weisung zulässig für diese Sammelkarte (nur bei Meldungstyp=2)
     * =1 Weisungen sind grundsätzlich möglich - und werden damit auch zwingend gespeichert (ggf. unmarkiert = frei)
     * 		Aktuell: 1 wird immer gesetzt - d.h. es gibt keine Sammelkarten ohne Weisungsvergabe.
     *      
     * 
     * Hinweis: Mischung aus "frei" und "Weisung" wird aktuell nicht unterstützt - mindestens Anpassung bei
     * Abstimmungsauswertung, bei der abgefragt wird ob Sammelkarte Weisungen enthält oder nicht.
     * 
     * =2 Freie Weisungen möglich (d.h. Weisungsempfänger kann frei abstimmen, d.h. ohne Weisung)
     * =4 Dedizierte Weisungen möglich
     * 
     * =8 Weisung gemäß eigener Empfehlung (fix, ohne Anpassung bei Veränderungen) möglich
     * =16 Weisung gemäß eigener Empfehlung (verändernd nur bei bestehenden Beschlussvorschlägen) möglich
     * =32 Weisung gemäß eigener Empfehlung (verändernd nur bei neuen Beschlussvorschlägen) möglich
     * 		Hinweis: bei neuen und bestehenden verändern => 16 UND 32 setzen!
     * 
     * =64 Weisung gemäß fremder Empfehlung (fix, ohne Anpassung bei Veränderungen) möglich
     * =128 Weisung gemäß fremder Empfehlung (verändernd bei bestehenden Beschlussvorschlägene) möglich
     * =256 Weisung gemäß fremder Empfehlung (verändernd bei neuen Beschlussvorschlägen) möglich
     * 
     * Hinweis: 1 muß immer gesetzt sein, auch bei 8, 16, 32, .... etc.
     * 
     * */
    public int skWeisungsartZulaessig = 0;

    public boolean sammelkarteIstWeisungskarte() {
//        CaBug.druckeLog("skWeisungsartZulaessig="+skWeisungsartZulaessig, logDrucken, 10);
        if ((skWeisungsartZulaessig & 4)==4) {
            return true;
        }
        return false;
    }

    /**falls Meldungstyp=2 Sammelkarte: Wird dann in Auswahl angeboten
     * Bei SRV und Briefwahl: nur für eine Sammelkarte zulässig*/
    public int skBuchbarInternet = 0;

    /**falls Meldungstyp=2 Sammelkarte: Wird dann in Auswahl angeboten
     * Bei SRV und Briefwahl: nur für eine Sammelkarte zulässig*/
    public int skBuchbarPapier = 0;

    /**falls Meldungstyp=2 Sammelkarte: Wird dann in Auswahl angeboten
     * Bei SRV und Briefwahl: nur für eine Sammelkarte zulässig*/
    public int skBuchbarHV = 0;

    /**falls Meldungstyp=2 Sammelkarte: Wird dann in Auswahl angeboten*/
    public int skBuchbarVollmachtDritte = 0;

    /**falls Meldungstyp=2 Sammelkarte: Wird dann in Auswahl angeboten
     * Konkret: Im Frontoffice-Tool wird dann der entsprechende Bevollmächtigte in der Vertreterauswahlliste angezeigt*/
    public int skBuchbarVollmachtDritteHV = 0;

    /**falls Sammelkarte: 0=Offenlegung wie Parameter , 1=Offenlegung, -1=keine Offenlegung.
     * Zugriff zur Bestimmung der wirksamen Offenlegung muß über
     * liefereOffenlegungTatsaechlich erfolgen!*/
    public int skOffenlegung = 0;

    /**Nur für Gastkarten: Insti-Ident, die durch diese Gastkarte vertreten wird.
     * (TODO: Insti-Ident im Meldebestand als separates Feld wohl noch nicht verwendet)
     * 
     * Nur für Sammelkarten: Insti-Ident, zu dem diese Sammelkarte zugeordnet ist.
     * Für "pure" SRV / Briefwahlkarten (also diejenigen Sammelkarten, die im Portal
     * für die Retail-Aktionäre verwendet werden): 0*/
    public int instiIdent = 0;

    /**Vorabzuordnung
     * Length=20
     * */
    public String stimmkarteVorab = "";
    /**Vorabzuordnung
     * Length=20*/
    public String stimmkarteSecondVorab = "";

    /**Vorzüge, Stämme etc. - Verweis auf Grunddaten-Id.
     * Noch nicht fertig durchdacht.
     * Beginnend bei 1! (D.h. Stämme = 1, Vz=2 i.d.R.
     */
    public int gattung = 0;

    /**1 => Fixanmeldung*/
    public int fixAnmeldung = 0;

    public long stueckAktien = 0;

    public long stimmen = 0;

    /**Noch offen: was bei mehreren Drucken? Bei mehreren
     * Ausstellungen? Evtl. hier raus, und in eine
     * gesonderte Table der herausgegebenen ZutrittsIds rein
     */
    public long stueckAktienDruckEK = 0;

    public long stimmenDruckEK = 0;

    /**E/F/V
     * Length=1*/
    public String besitzart = "";

    /**13 Stellen: V/A/S/1-9,E (E bedeutet: es liegt für diese Karte ein einzelner Ausschluß, Kartenbezogen auf Kandidat, vor).
     * Die Kennzeichen stehen ohne ";" - einfach aneinandergereiht - in diesem Feld.
     */
    public String stimmausschluss = "";
    public String[] liefereStimmausschlussArray() {
        stimmausschluss=stimmausschluss
                .replace(" ", "");
        /*; wegen Aufwärtskompatibilität raus - eigentlich nicht mehr erforderlich*/
        stimmausschluss=stimmausschluss.replace(";", "");
        String[] ergebnisArray=new String[stimmausschluss.length()];
        for (int i=0;i<stimmausschluss.length();i++) {
            ergebnisArray[i]=stimmausschluss.substring(i, i+1);
        }
        return ergebnisArray;
    }

    /**0 / 1 (für Gäste, z.B. wenn Karte auch für Wortmeldung benutzt werden kann)*/
    public int zusatzrechte = 0;

    /**Verweis auf die ausgelagerten Personendaten in DbPersonenNatJur
     * Hinweis: mehrere Meldungssätze können auf die selbe personenNatJur verweisen! Konkrete Fälle z.B.:
     * > wenn einem Aktienregistersatz mehrere "EKs" ausgestellt werden
     * > wenn die Inhaberanmeldestelle 2 Eintrittskarten für die selbe Person ausstellt
     * 
     *  Beim Ändern dieser Daten (durch ein Dialogprogramm) muß der Benutzer ggf. auf diesen Umstand hingewiesen
     *  werden!
     */
    public int personenNatJurIdent;

    /*********************************************Ausgelagert in PersonenNatJur***************************************/
    /**Ident, die auf einem Smartphone aktiv war, und "gegen die" verglichen wurde dass diese ident
     * die selbe Person ist wie diese Entity */
    public int istSelbePersonWieIdent = 0;
    /**=1 => Übereinstimmung von ident und istSelbePersonWieIdent wurde "augenscheinlich" überprüft.
     * =0 => nur vom Aktioär selbst so eingegeben*/
    public int uebereinstimmungSelbePersonWurdeUeberprueft = 0;

    /**Für Teilnehmerverzeichnis. Wird aus Detailfelder zusammengesetzt, wenn diese gefüllt
     * Length=80*/
    public String kurzName = "";

    /**Für Teilnehmerverzeichnis. Wird aus Detailfelder zusammengesetzt, wenn diese gefüllt
     * Length=80*/
    public String kurzOrt = "";

    /**Verweis auf Anredendatei*/
    public int anrede = 0;

    /**Length=30*/
    public String titel = "";

    /**Length=30*/
    public String adelstitel = "";

    /**Length=80*/
    public String name = "";

    /**Length=80*/
    public String vorname = "";

    public String liefereVornameName() {
        String hVornameName="";
        if (!titel.isEmpty()) {
            hVornameName=titel+" ";
        }
        if (!vorname.isEmpty()) {
            hVornameName+=vorname+" ";
        }
        hVornameName+=name;
        return hVornameName;
    }
    /**Length=80*/
    public String zuHdCo = "";

    /**Length=80*/
    public String zusatz1 = "";

    /**Length=80*/
    public String zusatz2 = "";

    /**Length=80*/
    public String strasse = "";

    /**Length=4*/
    public String land = "";

    /**Length=20*/
    public String plz = "";

    /**Length=80*/
    public String ort = "";

    /**Verweis auf Satz in tbl_personenNatJurVersandadresse*/
    public int identVersandadresse = 0;

    /**Mailadresse für elektronischen Versand von EKs etc.
     * (nicht zu verwechseln mit der Registrierung im
     * Email-Portal für den elektronischen Einladungsversand)
     * Length=80*/
    public String mailadresse = "";

    /**Konkrete Verwendung noch nicht ganz klar – wird im Rahmen des Gästemoduls entwickelt.
     * Aktuell: >0 => Gastkarte gehört zur entsprechenden Insti-Karte*/
    public int kommunikationssprache = 0;

    /** Für die jeweiligen Person selbst. Wird übernommen in PersonNatJur - siehe dort
     * Length=20
     */
    public String loginKennung = "";

    /**Length=20*/
    @Deprecated
    public String loginPasswort = "";

    /**Length=30
     * AAA = Mandantenschlüssel
     * BBBBBB = HV-Datum
     * CC=zwei Buchstaben Vorname
     * DD=zwei Buchstaben Ort
     * EEEEE =lfd Nummer*/
    public String oeffentlicheID = "";

    /**********************Bis hierher: ausgelagert in PersonenNatJur***********************************/

    /**Für Sammelkartenverwendung:
     * Leer = darf weitergegeben werden
     * 1 = darf nur in Gesamtsumme aller Weisungen weitergegeben werden
     * 2 = darf nur in Gesamtsumme je Listenkarte weitergegeben werden
     * 3 = darf gar nicht weitergegeben werden
     * 
     * 
     * Für Verein (Ku254): Ausgabe von Unterlagen / Zutrittsorganisation (jeweils 1=Erhält/Darf)
     * Stelle 1: Zutrittsberechtigt 
     * Stelle 2: Wahlunterlagen
     * Stelle 3: Voucher
     * Stelle 4: Zutritt nur in Begleitung des Mitglieds unter 14
     * Stelle 5: Zutritt nur in Begleitung des Mitglieds
     * 
     * Für Vereins-Anmeldung:
     * "" => keine Prüfung erforderlich
     * "-1" => Prüfung erforderlich
     * "-2" => keine hinterlegte Vollmacht gefunden
     * "1" => geprüft
     * 
     **Length=40*/
    public String zusatzfeld1 = "";

    /**Für Sammelkartenverwendung:
     * Kommentar zur Verwendung der Sammelkarte (nur intern)
     * 
     * Für Verein: S = Stimmberechtigt
     * 
     *Length=40*/
    public String zusatzfeld2 = "";

    /**
     * Für Portal (speziell ku178):
     * Div. Infos für Anmeldung.
     * Pos 1 = Anmelden (1), Abmelden (2), sonst undefiniert
     * 
     * Für Verein: Bonuskartennummer
     * 
     * Fall paramBasis.hoechststimmrechtHackAktiv==1: In diesem Feld steht die
     * zulässige (d.h. gekürzte) Stimmenzahl der Meldung.
     *Length=40*/
    public String zusatzfeld3 = "";

    /**Für Verein:
     * (Zahlen beziehen sich auf den entsprechende Eintrag in zusatzfeld 1)
     * Mitglied - Mitglied, je nachdem Zutrittsberechtigt oder nicht und ggf. Stimmberechtigt
     * EMW – Ersatzkarte mit Wahlberechtigung - 11100
     * EOW – Ersatzkarte ohne Wahlberechtigung - 10100
     * BOW – Begleitkarte Kind - 10110 
     * BMO – Begleitkarte Mitglied - 10101 
     * GOW – Gast 10100
     *	
     * Für Portal (speziell ku178):
     * Bundesland (für Beiratswahl und Dialoganmeldung)
     *
     * Für Hybrid-Mitgliederveranstaltungen (auf Präsenz-Server): statusPraesenz
     * 
     * Length=40*/
    public String zusatzfeld4 = "";

    /**Für Verein: "VIP-Kennzeichen", d.h. =1 => wird in Warnungen berücksichtigt
     * 
     * Standard: falls ==1, und Parameter sammelkartenFuerAenderungSperren==1, dann
     * dürfen Willenserklärungen dieser Sammelkarte nicht übers Portal widerrufen
     * oder geändert werden.
     * Length=40*/
    public String zusatzfeld5 = "";

    /**Verweis auf Gruppendatei - zugeordnete Gruppe*/
    public int gruppe = 0;

    /**Datum, Uhrzeit
     * Length=19
     */
    public String neuanlageDatumUhrzeit = "";

    /**Datum, Uhrzeit
     * Length=19
     */
    public String geaendertDatumUhrzeit = "";

    /**1=J/2=N*/
    public int manuellGeaendertSeitLetztemImport;

    /****************************Willenserklärungs-Felder***********************************************
     * Die folgenden Felder werden nicht direkt gepflegt, sondern über Willenserklärungen eingebucht.
     * Sie geben jeweils den aktuellen Stand (mit / ohne Delayed) wieder.
     */

    /**=1 => es sind delayed-Willenserklärungen vorhanden*/
    public int delayedVorhanden = 0;

    /**=1 => es sind pending-Willenserklärungen vorhanden*/
    public int pendingVorhanden = 0;

    /**Eindeutiger Zutritts-Key, mit dem sich der Aktionär ausweisen kann. Kann aber wechseln! Kann „Null“ sein.
     * UNIQUE zusammen mit mandant und klasse (d.h. Gastkarte und Aktionärskarte kann selbe ZutrittsIdent haben,
     * sollte bei interner Anwendung vermieden werden, kann aber gegenüber arfuehrer001 und arfuehrer005 nicht
     * ausgeschlossen werden).
     * Length=20
     */
    public String zutrittsIdent = "";
    public String zutrittsIdent_Delayed = "";

    /**Lenght=20*/
    public String stimmkarte = "";
    public String stimmkarte_Delayed = "";
    /**zweite zugeordnete Stimmkarte, z.B. für Voter
     * Length=20
     */
    public String stimmkarteSecond = "";
    public String stimmkarteSecond_Delayed = "";

    /**aktuelle Willenserklärung (Art, aus Enwillenserklaerung), gemäß der eine Sammelkartenzuordnung durchgeführt wurde.
     * D.h. es wird die Willenserklärung-Art eingetragen, die zu dem Eintrag in die Sammelkarte geführt hat (also nur SRV, KIAV, Briefwahl,
     * Dauervollmachten, organisatorisches).
     * Näheres siehe auch Doku in BlWillenserklaerung.
     * =0 => meldung ist nicht in Sammelkarte.
     */
    public int willenserklaerung = 0;
    public int willenserklaerung_Delayed = 0;
    /**Hier wird die Willenserklärung-ID eingetragen, die zu der Zuordnung der Sammelkarte geführt hat.
     * Bei "Weisungs-Änderungen": hier wird die letzte Willenserklärungsnummer eingetragen (also die der letzten Änderung), da gegen diese
     * bei neuen Willenserklärungen verglichen werden muß. (als Art bleibt aber weiterhin die ursprüngliche Erteilung, nicht die 
     * "Änderungs-Art")
     */
    public int willenserklaerungIdent = 0;
    public int willenserklaerungIdent_Delayed = 0;

    /**Veränderungszeit, zu der die letzte Willenserklaerung/Sammelkarte (siehe willenserklaerung) erteilt wurde. Ist hier gespeichert zum leichteren Abgleich,
     * ob eine neuere Willenserklärung diese überschreibt
     * Length=19 
     */
    public String veraenderungszeit = "";
    public String veraenderungszeit_Delayed = "";
    /**Weg, auf dem die letzte Willenserklaerung/Sammelkarte (siehe willenserklaerung) erteilt wurde. Ist hier gespeichert zum leichteren Abgleich,
     * ob eine neuere Willenserklärung diese überschreibt
     */
    public int erteiltAufWeg = 0;
    public int erteiltAufWeg_Delayed = 0;

    /** !=0 => Karte ist in Sammelkarte, Verweis auf Sammelkarte (ident!). Nur zulässig bei meldungstyp=1*/
    public int meldungEnthaltenInSammelkarte = 0;
    public int meldungEnthaltenInSammelkarte_Delayed = 0;
    /**Falls Karte in Sammelkarte, dann ist hier Art der Sammelkarte dokumentiert
     *  1=KIAV; 2=SRV; 3=organisatorisch; 4=Briefwahl 5=Dauervollmacht
     *  (KonstSkIst)
     */
    public int meldungEnthaltenInSammelkarteArt = 0;
    public int meldungEnthaltenInSammelkarteArt_Delayed = 0;

    /**vor allem für Einzelkarten vorgesehen; für Sammelkarten noch unklar*/
    public int weisungVorhanden = 0;
    public int weisungVorhanden_Delayed = 0;

    /** Aktueller Vertreter
     * Length=80
     */
    public String vertreterName = "";
    public String vertreterName_Delayed = "";
    /** Aktueller VertreterVorname
     * Length=80
     */
    public String vertreterVorname = "";
    public String vertreterVorname_Delayed = "";
    /** Aktueller Vertreterort
     * Length=80
     */
    public String vertreterOrt = "";
    public String vertreterOrt_Delayed = "";

    /**Verweis auf personNatJur des Vertreters*/
    /*TODO _Willenserklaerungen: Noch in willenserklärungen etc. berücksichtigen*/
    public int vertreterIdent = 0;
    public int vertreterIdent_Delayed = 0;

    /**Verweis auf Vollmacht in EclWillenserklaerung - wird offensichtlich derzeit nicht gefüllt?*/
    @Deprecated
    public int willenserklaerungMitVollmacht = 0;
    @Deprecated
    public int willenserklaerungMitVollmacht_Delayed = 0;

    /**Gibt den GERADE aktuellen Präsenzstatus wieder (kein Rückschluß auf
     * Historie!) - als Aktionär.
     * Wird nur für Einzelkarten / Sammelkarte gefüllt, nicht für
     * "in Sammelkarten enthaltene". 
     * Eine Karte, die bereits in Sammelkarte ist, aber früher selbst anwesend war,
     * ist über "statusWarPraesenz" gekennzeichnet.
     * 
     * 0 = nicht anwesend; 1 = anwesend; 2 = war anwesend; 4 = anwesend in Sammelkarte
     * KonstPraesenzStatus
     * */
    public int statusPraesenz = 0;
    /**Delayed ist der Status, der für die Abstimmung zählt (also eingefroren ist).
     */
    public int statusPraesenz_Delayed = 0;

    /**Historie - diese Karte war als Einzelkarte (nicht in Sammelkarte enthalten)
     * irgendwann mal als Teilnehmer (selbst oder Bevollmächtigt) präsent
     * */
    public int statusWarPraesenz = 0;
    public int statusWarPraesenz_Delayed = 0;

    /**Falls diese Meldung über virtuelle Teilnahme präsent gesetzt wurde: ident der Kennung,
     * mit der der Präsenzugang erfolgt ist
     */
    public int virtuellerTeilnehmerIdent = 0;

    /*Unklar, ob dieses Feld noch benötigt wird*/
    /**Noch unklar, wofür das benötigt wird:
     * 1 = persönlich; 2 = Vertretung; 3 = Fernteilnahme; 4 = Briefwahl
     */
    /*TODO _DBbereinigung tbl_meldungen: Unklar, ob dieses Feld noch benötigt wird*/
    public int teilnahmeArt = 0;

    /*+++++++++++Felder für vorläufige Anmeldung / Vollmacht+++++++++++++++++++*/

    /**Für vorlAnmeldung, vorlAnmeldungAkt, vorlAnmeldungSer:
     * 
     * 0=bisher keine Aktion (exclusiv-Wert)
     * >0, <1000: mit x Personen angemeldet
     * 1024: abgemeldet (exclusiv-Wert)
     * 2048: Bevollmächtigten angemeldet
     * 4096: Online angemeldet
     * (exclusiv bedeutet: nicht in Kombination mit anderen Werten)
     * vorlAnmeldung: tatsächlicher, aktuell gültiger Stand
     * vorlAnmeldungAkt: durchgeführt durch Aktionär
     * vorlAnmeldungSer: durchgeführt durch Service
     */
    public int vorlAnmeldung = 0;
    public int vorlAnmeldungAkt = 0;
    public int vorlAnmeldungSer = 0;

    /*Die folgenden Felder werden nicht mehr verwendet. Nur noch für Compilierzwecke enthalten.
     * In Datenbank bereits entfernt*/
    /*TODO _DBbereinigung tbl_meldungen: Die folgenden Felder werden nicht mehr verwendet. Nur noch für Compilierzwecke enthalten.*/
    /** „früherer Datensatz“, d.h. bei Ausstellung einer Ersatzkarte steht in meldungsIdentVorgaenger die „alte“ stornierte ZutrittsIdent*/
    public int meldungsIdentVorgaenger = 0;

    /** „nachfolgender Datensatz“, d.h. bei einer „alten“ stornierte ZutrittsIdent steht dort der Verweis auf den (nächstgültigen) neueren Datensatz */
    public int meldungsIdentNachfolger = 0;

    /**„Versionierung“ von stornierten, aber wieder freigegebenen zutrittsIdent. Standardmäßig = 0*/
    public int zutrittsIdentVers = 0;

    /**Verweis auf Vertretername/ort in separater Tabelle (der dann  einheitlich ist)*/
    @Deprecated
    public int vertreterNummer = 0;

    /**Gibt den GERADE aktuellen Präsenzstatus  wieder (kein Rückschluß auf
     * Historie!) - als Gast
     * 0 = nicht anwesend; 1 = anwesend; 2 = war anwesend;
     * Nicht mehr verwendet! Aktionäre Anmelden
     * */
    @Deprecated
    public int statusPraesenzGast = 0;

    /**Historie - war irgendwann mal als Gast präsent
     * Nicht mehr verwendet!*/
    public int statusWarPraesenzGast = 0;

    @Deprecated
    /**Length=10*/
    public String gebDatum = "";

    /* ***************************************************************************************************
     * Die folgenden Felder werden beim read mit eingelesen, sind jedoch Informationen aus anderen Sätzen.
     * Sie dürfen nicht verändert werden, bzw. werden beim Update / Insert nicht mitgespeichert / berücksichtigt!
     */

    /* Infos zu Ersatz-Eintrittskarten - Vorgänger und Nachfolger zutrittsIdent, jeweils der
     * in meldungsIdentVorgaenger und meldungsIdentNachfolger entsprechenden Sätzen
     */
    public String zutrittsIdentVorgaenger = "";
    public int zutrittsIdentVersVorgaenger = 0;
    public String zutrittsIdentNachfolger = "";
    public int zutrittsIdentVersNachfolger = 0;

    public EclWillenserklaerung praesenzveraenderungarray[];
    public EclMeldungAusstellungsgrund meldungausstellungsgrundarray[];
    public EclMeldungVipKZ meldungVipKZarray[];
    public EclMeldungenMeldungen zuMeldungenarray[];
    public EclMeldungenMeldungen vonMeldungenarray[];
    public EclAenderungslog aenderungslogarray[];

    public EclMeldung() {
        super();
    }

    /** Kopieren des Objekts nach neu */
    public void copyTo(EclMeldung neu) {

        neu.meldungsIdent = this.meldungsIdent;
        neu.db_version = this.db_version;
        neu.db_version_personenNatJur = this.db_version_personenNatJur;
        neu.mandant = this.mandant;
        neu.meldungAktiv = this.meldungAktiv;
        neu.klasse = this.klasse;
        neu.aktionaersnummer = this.aktionaersnummer;
        neu.externeIdent = this.externeIdent;
        neu.meldungstyp = this.meldungstyp;
        neu.skMitWeisungen = this.skMitWeisungen;
        neu.skIst = this.skIst;
        neu.skWeisungsartZulaessig = this.skWeisungsartZulaessig;
        neu.skBuchbarInternet = this.skBuchbarInternet;
        neu.skBuchbarPapier = this.skBuchbarPapier;
        neu.skBuchbarHV = this.skBuchbarHV;
        neu.skBuchbarVollmachtDritte = this.skBuchbarVollmachtDritte;
        neu.skBuchbarVollmachtDritteHV = this.skBuchbarVollmachtDritteHV;
        neu.skOffenlegung = this.skOffenlegung;
        neu.stimmkarteVorab = this.stimmkarteVorab;
        neu.stimmkarteSecondVorab = this.stimmkarteSecondVorab;
        neu.gattung = this.gattung;
        neu.stueckAktien = this.stueckAktien;
        neu.stimmen = this.stimmen;
        neu.stueckAktienDruckEK = this.stueckAktienDruckEK;
        neu.stimmenDruckEK = this.stimmenDruckEK;
        neu.besitzart = this.besitzart;
        neu.stimmausschluss = this.stimmausschluss;
        neu.zusatzrechte = this.zusatzrechte;
        neu.personenNatJurIdent = this.personenNatJurIdent;
        neu.kurzName = this.kurzName;
        neu.kurzOrt = this.kurzOrt;
        neu.anrede = this.anrede;
        neu.titel = this.titel;
        neu.adelstitel = this.adelstitel;
        neu.name = this.name;
        neu.vorname = this.vorname;
        neu.zuHdCo = this.zuHdCo;
        neu.zusatz1 = this.zusatz1;
        neu.zusatz2 = this.zusatz2;
        neu.strasse = this.strasse;
        neu.land = this.land;
        neu.plz = this.plz;
        neu.ort = this.ort;
        neu.identVersandadresse = this.identVersandadresse;
        neu.mailadresse = this.mailadresse;
        neu.kommunikationssprache = this.kommunikationssprache;
        neu.loginKennung = this.loginKennung;
        neu.loginPasswort = this.loginPasswort;
        neu.oeffentlicheID = this.oeffentlicheID;
        neu.zusatzfeld1 = this.zusatzfeld1;
        neu.zusatzfeld2 = this.zusatzfeld2;
        neu.zusatzfeld3 = this.zusatzfeld3;
        neu.zusatzfeld4 = this.zusatzfeld4;
        neu.zusatzfeld5 = this.zusatzfeld5;
        neu.gruppe = this.gruppe;
        neu.neuanlageDatumUhrzeit = this.neuanlageDatumUhrzeit;
        neu.geaendertDatumUhrzeit = this.geaendertDatumUhrzeit;
        neu.manuellGeaendertSeitLetztemImport = this.manuellGeaendertSeitLetztemImport;

        neu.delayedVorhanden = this.delayedVorhanden;
        neu.pendingVorhanden = this.pendingVorhanden;
        neu.zutrittsIdent = this.zutrittsIdent;
        neu.zutrittsIdent_Delayed = this.zutrittsIdent_Delayed;
        neu.stimmkarte = this.stimmkarte;
        neu.stimmkarte_Delayed = this.stimmkarte_Delayed;
        neu.stimmkarteSecond = this.stimmkarteSecond;
        neu.stimmkarteSecond_Delayed = this.stimmkarteSecond_Delayed;

        neu.willenserklaerung = this.willenserklaerung;
        neu.willenserklaerung_Delayed = this.willenserklaerung_Delayed;
        neu.willenserklaerungIdent = this.willenserklaerungIdent;
        neu.willenserklaerungIdent_Delayed = this.willenserklaerungIdent_Delayed;
        neu.veraenderungszeit = this.veraenderungszeit;
        neu.veraenderungszeit_Delayed = this.veraenderungszeit_Delayed;
        neu.erteiltAufWeg = this.erteiltAufWeg;
        neu.erteiltAufWeg_Delayed = this.erteiltAufWeg_Delayed;

        neu.meldungEnthaltenInSammelkarte = this.meldungEnthaltenInSammelkarte;
        neu.meldungEnthaltenInSammelkarte_Delayed = this.meldungEnthaltenInSammelkarte_Delayed;
        neu.meldungEnthaltenInSammelkarteArt = this.meldungEnthaltenInSammelkarteArt;
        neu.meldungEnthaltenInSammelkarteArt_Delayed = this.meldungEnthaltenInSammelkarteArt_Delayed;
        neu.weisungVorhanden = this.weisungVorhanden;
        neu.weisungVorhanden_Delayed = this.weisungVorhanden_Delayed;
        neu.vertreterName = this.vertreterName;
        neu.vertreterName_Delayed = this.vertreterName_Delayed;
        neu.vertreterVorname = this.vertreterVorname;
        neu.vertreterVorname_Delayed = this.vertreterVorname_Delayed;
        neu.vertreterOrt = this.vertreterOrt;
        neu.vertreterOrt_Delayed = this.vertreterOrt_Delayed;
        neu.vertreterIdent = this.vertreterIdent;
        neu.vertreterIdent_Delayed = this.vertreterIdent_Delayed;
        neu.willenserklaerungMitVollmacht = this.willenserklaerungMitVollmacht;
        neu.willenserklaerungMitVollmacht_Delayed = this.willenserklaerungMitVollmacht_Delayed;
        neu.statusPraesenz = this.statusPraesenz;
        neu.statusPraesenz_Delayed = this.statusPraesenz_Delayed;
        neu.statusWarPraesenz = this.statusWarPraesenz;
        neu.statusWarPraesenz_Delayed = this.statusWarPraesenz_Delayed;

        neu.teilnahmeArt = this.teilnahmeArt;

    }

    /** Vergleicht das Objekt meldung mit der meldung vergleich
     * Wichtig: Es wird nur die "meldungs-basis" verglichen, d.h. keine Anhängsel wie z.B.
     * Präsenzhistorie, VIP-KZ o.ä..
     * Verwendung: vor "aufwändigeren Operationen" Prüfen, ob Basis gespeichert werden muß
     * @param vergleich
     * @return
     */
    public boolean equalsTo(EclMeldung vergleich) {

        if (vergleich.meldungsIdent != this.meldungsIdent) {
            return false;
        }
        if (vergleich.db_version != this.db_version) {
            return false;
        }
        if (vergleich.db_version_personenNatJur != this.db_version_personenNatJur) {
            return false;
        }
        if (vergleich.mandant != this.mandant) {
            return false;
        }
        if (vergleich.meldungAktiv != this.meldungAktiv) {
            return false;
        }
        if (vergleich.klasse != this.klasse) {
            return false;
        }
        if (!vergleich.aktionaersnummer.equals(this.aktionaersnummer)) {
            return false;
        }
        if (!vergleich.externeIdent.equals(this.externeIdent)) {
            return false;
        }
        if (vergleich.meldungstyp != this.meldungstyp) {
            return false;
        }
        if (vergleich.skMitWeisungen != this.skMitWeisungen) {
            return false;
        }
        if (vergleich.skIst != this.skIst) {
            return false;
        }
        if (vergleich.skWeisungsartZulaessig != this.skWeisungsartZulaessig) {
            return false;
        }
        if (vergleich.skBuchbarInternet != this.skBuchbarInternet) {
            return false;
        }
        if (vergleich.skBuchbarPapier != this.skBuchbarPapier) {
            return false;
        }
        if (vergleich.skBuchbarHV != this.skBuchbarHV) {
            return false;
        }
        if (vergleich.skBuchbarVollmachtDritte != this.skBuchbarVollmachtDritte) {
            return false;
        }
        if (vergleich.skBuchbarVollmachtDritteHV != this.skBuchbarVollmachtDritteHV) {
            return false;
        }
        if (vergleich.skOffenlegung != this.skOffenlegung) {
            return false;
        }
        if (!vergleich.stimmkarteVorab.equals(this.stimmkarteVorab)) {
            return false;
        }
        if (!vergleich.stimmkarteSecondVorab.equals(this.stimmkarteSecondVorab)) {
            return false;
        }
        if (vergleich.gattung != this.gattung) {
            return false;
        }
        if (vergleich.stueckAktien != this.stueckAktien) {
            return false;
        }
        if (vergleich.stimmen != this.stimmen) {
            return false;
        }
        if (vergleich.stueckAktienDruckEK != this.stueckAktienDruckEK) {
            return false;
        }
        if (vergleich.stimmenDruckEK != this.stimmenDruckEK) {
            return false;
        }
        if (!vergleich.besitzart.equals(this.besitzart)) {
            return false;
        }
        if (!vergleich.stimmausschluss.equals(this.stimmausschluss)) {
            return false;
        }
        if (vergleich.zusatzrechte != this.zusatzrechte) {
            return false;
        }
        if (vergleich.personenNatJurIdent != this.personenNatJurIdent) {
            return false;
        }
        if (vergleich.istSelbePersonWieIdent != this.istSelbePersonWieIdent) {
            return false;
        }
        if (vergleich.uebereinstimmungSelbePersonWurdeUeberprueft != this.uebereinstimmungSelbePersonWurdeUeberprueft) {
            return false;
        }
        if (!vergleich.kurzName.equals(this.kurzName)) {
            return false;
        }
        if (!vergleich.kurzOrt.equals(this.kurzOrt)) {
            return false;
        }
        if (vergleich.anrede != this.anrede) {
            return false;
        }

        if (!vergleich.titel.equals(this.titel)) {
            return false;
        }
        if (!vergleich.adelstitel.equals(this.adelstitel)) {
            return false;
        }
        if (!vergleich.name.equals(this.name)) {
            return false;
        }
        if (!vergleich.vorname.equals(this.vorname)) {
            return false;
        }
        if (!vergleich.zuHdCo.equals(this.zuHdCo)) {
            return false;
        }
        if (!vergleich.zusatz1.equals(this.zusatz1)) {
            return false;
        }
        if (!vergleich.zusatz2.equals(this.zusatz2)) {
            return false;
        }
        if (!vergleich.strasse.equals(this.strasse)) {
            return false;
        }
        if (!vergleich.land.equals(this.land)) {
            return false;
        }
        if (!vergleich.plz.equals(this.plz)) {
            return false;
        }
        if (!vergleich.ort.equals(this.ort)) {
            return false;
        }
        if (vergleich.identVersandadresse != this.identVersandadresse) {
            return false;
        }
        if (!vergleich.mailadresse.equals(this.mailadresse)) {
            return false;
        }
        if (vergleich.kommunikationssprache != this.kommunikationssprache) {
            return false;
        }
        if (!vergleich.loginKennung.equals(this.loginKennung)) {
            return false;
        }
        if (!vergleich.loginPasswort.equals(this.loginPasswort)) {
            return false;
        }
        if (!vergleich.oeffentlicheID.equals(this.oeffentlicheID)) {
            return false;
        }
        if (!vergleich.zusatzfeld1.equals(this.zusatzfeld1)) {
            return false;
        }
        if (!vergleich.zusatzfeld2.equals(this.zusatzfeld2)) {
            return false;
        }
        if (!vergleich.zusatzfeld3.equals(this.zusatzfeld3)) {
            return false;
        }
        if (!vergleich.zusatzfeld4.equals(this.zusatzfeld4)) {
            return false;
        }
        if (!vergleich.zusatzfeld5.equals(this.zusatzfeld5)) {
            return false;
        }
        if (vergleich.gruppe != this.gruppe) {
            return false;
        }
        if (!vergleich.neuanlageDatumUhrzeit.equals(this.neuanlageDatumUhrzeit)) {
            return false;
        }
        if (!vergleich.geaendertDatumUhrzeit.equals(this.geaendertDatumUhrzeit)) {
            return false;
        }
        if (vergleich.manuellGeaendertSeitLetztemImport != this.manuellGeaendertSeitLetztemImport) {
            return false;
        }
        if (vergleich.delayedVorhanden != this.delayedVorhanden) {
            return false;
        }
        if (vergleich.pendingVorhanden != this.pendingVorhanden) {
            return false;
        }
        if (!vergleich.zutrittsIdent.equals(this.zutrittsIdent)) {
            return false;
        }
        if (!vergleich.zutrittsIdent_Delayed.equals(this.zutrittsIdent_Delayed)) {
            return false;
        }
        if (!vergleich.stimmkarte.equals(this.stimmkarte)) {
            return false;
        }
        if (!vergleich.stimmkarte_Delayed.equals(this.stimmkarte_Delayed)) {
            return false;
        }
        if (!vergleich.stimmkarteSecond.equals(this.stimmkarteSecond)) {
            return false;
        }
        if (!vergleich.stimmkarteSecond_Delayed.equals(this.stimmkarteSecond_Delayed)) {
            return false;
        }

        if (vergleich.willenserklaerung != this.willenserklaerung) {
            return false;
        }
        if (vergleich.willenserklaerung_Delayed != this.willenserklaerung_Delayed) {
            return false;
        }
        if (vergleich.willenserklaerungIdent != this.willenserklaerungIdent) {
            return false;
        }
        if (vergleich.willenserklaerungIdent_Delayed != this.willenserklaerungIdent_Delayed) {
            return false;
        }
        if (!vergleich.veraenderungszeit.equals(this.veraenderungszeit)) {
            return false;
        }
        if (!vergleich.veraenderungszeit_Delayed.equals(this.veraenderungszeit_Delayed)) {
            return false;
        }
        if (vergleich.erteiltAufWeg != this.erteiltAufWeg) {
            return false;
        }
        if (vergleich.erteiltAufWeg_Delayed != this.erteiltAufWeg_Delayed) {
            return false;
        }

        if (vergleich.meldungEnthaltenInSammelkarte != this.meldungEnthaltenInSammelkarte) {
            return false;
        }
        if (vergleich.meldungEnthaltenInSammelkarte_Delayed != this.meldungEnthaltenInSammelkarte_Delayed) {
            return false;
        }
        if (vergleich.meldungEnthaltenInSammelkarteArt != this.meldungEnthaltenInSammelkarteArt) {
            return false;
        }
        if (vergleich.meldungEnthaltenInSammelkarteArt_Delayed != this.meldungEnthaltenInSammelkarteArt_Delayed) {
            return false;
        }
        if (vergleich.weisungVorhanden != this.weisungVorhanden) {
            return false;
        }
        if (vergleich.weisungVorhanden_Delayed != this.weisungVorhanden_Delayed) {
            return false;
        }
        if (!vergleich.vertreterName.equals(this.vertreterName)) {
            return false;
        }
        if (!vergleich.vertreterName_Delayed.equals(this.vertreterName_Delayed)) {
            return false;
        }
        if (!vergleich.vertreterVorname.equals(this.vertreterVorname)) {
            return false;
        }
        if (!vergleich.vertreterVorname_Delayed.equals(this.vertreterVorname_Delayed)) {
            return false;
        }
        if (!vergleich.vertreterOrt.equals(this.vertreterOrt)) {
            return false;
        }
        if (!vergleich.vertreterOrt_Delayed.equals(this.vertreterOrt_Delayed)) {
            return false;
        }
        if (vergleich.vertreterIdent != this.vertreterIdent) {
            return false;
        }
        if (vergleich.vertreterIdent_Delayed != this.vertreterIdent_Delayed) {
            return false;
        }
        if (vergleich.willenserklaerungMitVollmacht != this.willenserklaerungMitVollmacht) {
            return false;
        }
        if (vergleich.willenserklaerungMitVollmacht_Delayed != this.willenserklaerungMitVollmacht_Delayed) {
            return false;
        }
        if (vergleich.statusPraesenz != this.statusPraesenz) {
            return false;
        }
        if (vergleich.statusPraesenz_Delayed != this.statusPraesenz_Delayed) {
            return false;
        }
        if (vergleich.statusWarPraesenz != this.statusWarPraesenz) {
            return false;
        }
        if (vergleich.statusWarPraesenz_Delayed != this.statusWarPraesenz_Delayed) {
            return false;
        }

        if (vergleich.teilnahmeArt != this.teilnahmeArt) {
            return false;
        }

        return true;
    }

    public boolean meldungIstEinGast() {
        if (klasse == 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean meldungIstEinAktionaer() {
        if (klasse == 1) {
            return true;
        } else {
            return false;
        }
    }

    /**==1 => ist gerade präsent; 0 = ist gerade nicht präsent*/
    public int meldungIstPraesent() {
        if (statusPraesenz == 1) {
            return 1;
        } else {
            return 0;
        }
    }

    /**Liefert die präsente Personen-ID (d.h. auch bei selbstanwesenden*/
    public int meldungPraesentePerson() {
        if (vertreterIdent > 0) {
            return vertreterIdent;
        } else {
            return personenNatJurIdent;
        }
    }

    public int liefereGattung() {
        if (gattung == 0) {
            return 1;
        } else {
            return gattung;
        }
    }

    /**Werte auch Parameterstellugn aus, falls skOffenlegung==0*/
    public int liefereOffenlegungTatsaechlich(DbBundle pDbBundle) {
        int hOffenlegung = skOffenlegung, hParameter = 0;
        if (hOffenlegung == 0) {
            switch (skIst) {
            case 1:
                hParameter = pDbBundle.param.paramAkkreditierung.skOffenlegungKIAV;
                break;
            case 2:
                hParameter = pDbBundle.param.paramAkkreditierung.skOffenlegungSRV;
                break;
            case 3:
                hParameter = pDbBundle.param.paramAkkreditierung.skOffenlegungOrga;
                break;
            case 5:
                hParameter = pDbBundle.param.paramAkkreditierung.skOffenlegungDauer;
                break;
            default:
                hParameter = 1;
                break;
            }
            if (hParameter == 1) {
                hOffenlegung = 1;
            } else {
                hOffenlegung = -1;
            }

        }
        return hOffenlegung;
    }

    public String liefereSammelkartenBezeichnungKomplettIntern() {
        return this.name + " " + this.zusatzfeld2;
    }

    /************Für Sammelkarten******************/
    public boolean akzeptiertDedizierteWeisung() {
        if ((skWeisungsartZulaessig & 4) == 4) {
            return true;
        } else {
            return false;
        }
    }

    public boolean akzeptiertOhneWeisung() {
        if ((skWeisungsartZulaessig & 2) == 2) {
            return true;
        } else {
            return false;
        }
    }

    public boolean akzeptiertWieVorschlag() {
        if ((skWeisungsartZulaessig & 8) == 8) {
            return true;
        } else {
            return false;
        }
    }

    public int getLogDrucken() {
        return logDrucken;
    }

    public void setLogDrucken(int logDrucken) {
        this.logDrucken = logDrucken;
    }

    public int getMeldungsIdent() {
        return meldungsIdent;
    }

    public void setMeldungsIdent(int meldungsIdent) {
        this.meldungsIdent = meldungsIdent;
    }

    public long getDb_version() {
        return db_version;
    }

    public void setDb_version(long db_version) {
        this.db_version = db_version;
    }

    public long getDb_version_personenNatJur() {
        return db_version_personenNatJur;
    }

    public void setDb_version_personenNatJur(long db_version_personenNatJur) {
        this.db_version_personenNatJur = db_version_personenNatJur;
    }

    public int getMandant() {
        return mandant;
    }

    public void setMandant(int mandant) {
        this.mandant = mandant;
    }

    public int getMeldungAktiv() {
        return meldungAktiv;
    }

    public void setMeldungAktiv(int meldungAktiv) {
        this.meldungAktiv = meldungAktiv;
    }

    public int getKlasse() {
        return klasse;
    }

    public void setKlasse(int klasse) {
        this.klasse = klasse;
    }

    public String getAktionaersnummer() {
        return aktionaersnummer;
    }

    public void setAktionaersnummer(String aktionaersnummer) {
        this.aktionaersnummer = aktionaersnummer;
    }

    public int getAktienregisterIdent() {
        return aktienregisterIdent;
    }

    public void setAktienregisterIdent(int aktienregisterIdent) {
        this.aktienregisterIdent = aktienregisterIdent;
    }

    public String getExterneIdent() {
        return externeIdent;
    }

    public void setExterneIdent(String externeIdent) {
        this.externeIdent = externeIdent;
    }

    public int getMeldungstyp() {
        return meldungstyp;
    }

    public void setMeldungstyp(int meldungstyp) {
        this.meldungstyp = meldungstyp;
    }

    public int getSkMitWeisungen() {
        return skMitWeisungen;
    }

    public void setSkMitWeisungen(int skMitWeisungen) {
        this.skMitWeisungen = skMitWeisungen;
    }

    public int getSkIst() {
        return skIst;
    }

    public void setSkIst(int skIst) {
        this.skIst = skIst;
    }

    public int getSkWeisungsartZulaessig() {
        return skWeisungsartZulaessig;
    }

    public void setSkWeisungsartZulaessig(int skWeisungsartZulaessig) {
        this.skWeisungsartZulaessig = skWeisungsartZulaessig;
    }

    public int getSkBuchbarInternet() {
        return skBuchbarInternet;
    }

    public void setSkBuchbarInternet(int skBuchbarInternet) {
        this.skBuchbarInternet = skBuchbarInternet;
    }

    public int getSkBuchbarPapier() {
        return skBuchbarPapier;
    }

    public void setSkBuchbarPapier(int skBuchbarPapier) {
        this.skBuchbarPapier = skBuchbarPapier;
    }

    public int getSkBuchbarHV() {
        return skBuchbarHV;
    }

    public void setSkBuchbarHV(int skBuchbarHV) {
        this.skBuchbarHV = skBuchbarHV;
    }

    public int getSkBuchbarVollmachtDritte() {
        return skBuchbarVollmachtDritte;
    }

    public void setSkBuchbarVollmachtDritte(int skBuchbarVollmachtDritte) {
        this.skBuchbarVollmachtDritte = skBuchbarVollmachtDritte;
    }

    public int getSkBuchbarVollmachtDritteHV() {
        return skBuchbarVollmachtDritteHV;
    }

    public void setSkBuchbarVollmachtDritteHV(int skBuchbarVollmachtDritteHV) {
        this.skBuchbarVollmachtDritteHV = skBuchbarVollmachtDritteHV;
    }

    public int getSkOffenlegung() {
        return skOffenlegung;
    }

    public void setSkOffenlegung(int skOffenlegung) {
        this.skOffenlegung = skOffenlegung;
    }

    public int getInstiIdent() {
        return instiIdent;
    }

    public void setInstiIdent(int instiIdent) {
        this.instiIdent = instiIdent;
    }

    public String getStimmkarteVorab() {
        return stimmkarteVorab;
    }

    public void setStimmkarteVorab(String stimmkarteVorab) {
        this.stimmkarteVorab = stimmkarteVorab;
    }

    public String getStimmkarteSecondVorab() {
        return stimmkarteSecondVorab;
    }

    public void setStimmkarteSecondVorab(String stimmkarteSecondVorab) {
        this.stimmkarteSecondVorab = stimmkarteSecondVorab;
    }

    public int getGattung() {
        return gattung;
    }

    public void setGattung(int gattung) {
        this.gattung = gattung;
    }

    public int getFixAnmeldung() {
        return fixAnmeldung;
    }

    public void setFixAnmeldung(int fixAnmeldung) {
        this.fixAnmeldung = fixAnmeldung;
    }

    public long getStueckAktien() {
        return stueckAktien;
    }

    public void setStueckAktien(long stueckAktien) {
        this.stueckAktien = stueckAktien;
    }

    public long getStimmen() {
        return stimmen;
    }

    public void setStimmen(long stimmen) {
        this.stimmen = stimmen;
    }

    public long getStueckAktienDruckEK() {
        return stueckAktienDruckEK;
    }

    public void setStueckAktienDruckEK(long stueckAktienDruckEK) {
        this.stueckAktienDruckEK = stueckAktienDruckEK;
    }

    public long getStimmenDruckEK() {
        return stimmenDruckEK;
    }

    public void setStimmenDruckEK(long stimmenDruckEK) {
        this.stimmenDruckEK = stimmenDruckEK;
    }

    public String getBesitzart() {
        return besitzart;
    }

    public void setBesitzart(String besitzart) {
        this.besitzart = besitzart;
    }

    public String getStimmausschluss() {
        return stimmausschluss;
    }

    public void setStimmausschluss(String stimmausschluss) {
        this.stimmausschluss = stimmausschluss;
    }

    public int getZusatzrechte() {
        return zusatzrechte;
    }

    public void setZusatzrechte(int zusatzrechte) {
        this.zusatzrechte = zusatzrechte;
    }

    public int getPersonenNatJurIdent() {
        return personenNatJurIdent;
    }

    public void setPersonenNatJurIdent(int personenNatJurIdent) {
        this.personenNatJurIdent = personenNatJurIdent;
    }

    public int getIstSelbePersonWieIdent() {
        return istSelbePersonWieIdent;
    }

    public void setIstSelbePersonWieIdent(int istSelbePersonWieIdent) {
        this.istSelbePersonWieIdent = istSelbePersonWieIdent;
    }

    public int getUebereinstimmungSelbePersonWurdeUeberprueft() {
        return uebereinstimmungSelbePersonWurdeUeberprueft;
    }

    public void setUebereinstimmungSelbePersonWurdeUeberprueft(int uebereinstimmungSelbePersonWurdeUeberprueft) {
        this.uebereinstimmungSelbePersonWurdeUeberprueft = uebereinstimmungSelbePersonWurdeUeberprueft;
    }

    public String getKurzName() {
        return kurzName;
    }

    public void setKurzName(String kurzName) {
        this.kurzName = kurzName;
    }

    public String getKurzOrt() {
        return kurzOrt;
    }

    public void setKurzOrt(String kurzOrt) {
        this.kurzOrt = kurzOrt;
    }

    public int getAnrede() {
        return anrede;
    }

    public void setAnrede(int anrede) {
        this.anrede = anrede;
    }

    public String getTitel() {
        return titel;
    }

    public void setTitel(String titel) {
        this.titel = titel;
    }

    public String getAdelstitel() {
        return adelstitel;
    }

    public void setAdelstitel(String adelstitel) {
        this.adelstitel = adelstitel;
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

    public String getZuHdCo() {
        return zuHdCo;
    }

    public void setZuHdCo(String zuHdCo) {
        this.zuHdCo = zuHdCo;
    }

    public String getZusatz1() {
        return zusatz1;
    }

    public void setZusatz1(String zusatz1) {
        this.zusatz1 = zusatz1;
    }

    public String getZusatz2() {
        return zusatz2;
    }

    public void setZusatz2(String zusatz2) {
        this.zusatz2 = zusatz2;
    }

    public String getStrasse() {
        return strasse;
    }

    public void setStrasse(String strasse) {
        this.strasse = strasse;
    }

    public String getLand() {
        return land;
    }

    public void setLand(String land) {
        this.land = land;
    }

    public String getPlz() {
        return plz;
    }

    public void setPlz(String plz) {
        this.plz = plz;
    }

    public String getOrt() {
        return ort;
    }

    public void setOrt(String ort) {
        this.ort = ort;
    }

    public int getIdentVersandadresse() {
        return identVersandadresse;
    }

    public void setIdentVersandadresse(int identVersandadresse) {
        this.identVersandadresse = identVersandadresse;
    }

    public String getMailadresse() {
        return mailadresse;
    }

    public void setMailadresse(String mailadresse) {
        this.mailadresse = mailadresse;
    }

    public int getKommunikationssprache() {
        return kommunikationssprache;
    }

    public void setKommunikationssprache(int kommunikationssprache) {
        this.kommunikationssprache = kommunikationssprache;
    }

    public String getLoginKennung() {
        return loginKennung;
    }

    public void setLoginKennung(String loginKennung) {
        this.loginKennung = loginKennung;
    }

    public String getLoginPasswort() {
        return loginPasswort;
    }

    public void setLoginPasswort(String loginPasswort) {
        this.loginPasswort = loginPasswort;
    }

    public String getOeffentlicheID() {
        return oeffentlicheID;
    }

    public void setOeffentlicheID(String oeffentlicheID) {
        this.oeffentlicheID = oeffentlicheID;
    }

    public String getZusatzfeld1() {
        return zusatzfeld1;
    }

    public void setZusatzfeld1(String zusatzfeld1) {
        this.zusatzfeld1 = zusatzfeld1;
    }

    public String getZusatzfeld2() {
        return zusatzfeld2;
    }

    public void setZusatzfeld2(String zusatzfeld2) {
        this.zusatzfeld2 = zusatzfeld2;
    }

    public String getZusatzfeld3() {
        return zusatzfeld3;
    }

    public void setZusatzfeld3(String zusatzfeld3) {
        this.zusatzfeld3 = zusatzfeld3;
    }

    public String getZusatzfeld4() {
        return zusatzfeld4;
    }

    public void setZusatzfeld4(String zusatzfeld4) {
        this.zusatzfeld4 = zusatzfeld4;
    }

    public String getZusatzfeld5() {
        return zusatzfeld5;
    }

    public void setZusatzfeld5(String zusatzfeld5) {
        this.zusatzfeld5 = zusatzfeld5;
    }

    public int getGruppe() {
        return gruppe;
    }

    public void setGruppe(int gruppe) {
        this.gruppe = gruppe;
    }

    public String getNeuanlageDatumUhrzeit() {
        return neuanlageDatumUhrzeit;
    }

    public void setNeuanlageDatumUhrzeit(String neuanlageDatumUhrzeit) {
        this.neuanlageDatumUhrzeit = neuanlageDatumUhrzeit;
    }

    public String getGeaendertDatumUhrzeit() {
        return geaendertDatumUhrzeit;
    }

    public void setGeaendertDatumUhrzeit(String geaendertDatumUhrzeit) {
        this.geaendertDatumUhrzeit = geaendertDatumUhrzeit;
    }

    public int getManuellGeaendertSeitLetztemImport() {
        return manuellGeaendertSeitLetztemImport;
    }

    public void setManuellGeaendertSeitLetztemImport(int manuellGeaendertSeitLetztemImport) {
        this.manuellGeaendertSeitLetztemImport = manuellGeaendertSeitLetztemImport;
    }

    public int getDelayedVorhanden() {
        return delayedVorhanden;
    }

    public void setDelayedVorhanden(int delayedVorhanden) {
        this.delayedVorhanden = delayedVorhanden;
    }

    public int getPendingVorhanden() {
        return pendingVorhanden;
    }

    public void setPendingVorhanden(int pendingVorhanden) {
        this.pendingVorhanden = pendingVorhanden;
    }

    public String getZutrittsIdent() {
        return zutrittsIdent;
    }

    public void setZutrittsIdent(String zutrittsIdent) {
        this.zutrittsIdent = zutrittsIdent;
    }

    public String getZutrittsIdent_Delayed() {
        return zutrittsIdent_Delayed;
    }

    public void setZutrittsIdent_Delayed(String zutrittsIdent_Delayed) {
        this.zutrittsIdent_Delayed = zutrittsIdent_Delayed;
    }

    public String getStimmkarte() {
        return stimmkarte;
    }

    public void setStimmkarte(String stimmkarte) {
        this.stimmkarte = stimmkarte;
    }

    public String getStimmkarte_Delayed() {
        return stimmkarte_Delayed;
    }

    public void setStimmkarte_Delayed(String stimmkarte_Delayed) {
        this.stimmkarte_Delayed = stimmkarte_Delayed;
    }

    public String getStimmkarteSecond() {
        return stimmkarteSecond;
    }

    public void setStimmkarteSecond(String stimmkarteSecond) {
        this.stimmkarteSecond = stimmkarteSecond;
    }

    public String getStimmkarteSecond_Delayed() {
        return stimmkarteSecond_Delayed;
    }

    public void setStimmkarteSecond_Delayed(String stimmkarteSecond_Delayed) {
        this.stimmkarteSecond_Delayed = stimmkarteSecond_Delayed;
    }

    public int getWillenserklaerung() {
        return willenserklaerung;
    }

    public void setWillenserklaerung(int willenserklaerung) {
        this.willenserklaerung = willenserklaerung;
    }

    public int getWillenserklaerung_Delayed() {
        return willenserklaerung_Delayed;
    }

    public void setWillenserklaerung_Delayed(int willenserklaerung_Delayed) {
        this.willenserklaerung_Delayed = willenserklaerung_Delayed;
    }

    public int getWillenserklaerungIdent() {
        return willenserklaerungIdent;
    }

    public void setWillenserklaerungIdent(int willenserklaerungIdent) {
        this.willenserklaerungIdent = willenserklaerungIdent;
    }

    public int getWillenserklaerungIdent_Delayed() {
        return willenserklaerungIdent_Delayed;
    }

    public void setWillenserklaerungIdent_Delayed(int willenserklaerungIdent_Delayed) {
        this.willenserklaerungIdent_Delayed = willenserklaerungIdent_Delayed;
    }

    public String getVeraenderungszeit() {
        return veraenderungszeit;
    }

    public void setVeraenderungszeit(String veraenderungszeit) {
        this.veraenderungszeit = veraenderungszeit;
    }

    public String getVeraenderungszeit_Delayed() {
        return veraenderungszeit_Delayed;
    }

    public void setVeraenderungszeit_Delayed(String veraenderungszeit_Delayed) {
        this.veraenderungszeit_Delayed = veraenderungszeit_Delayed;
    }

    public int getErteiltAufWeg() {
        return erteiltAufWeg;
    }

    public void setErteiltAufWeg(int erteiltAufWeg) {
        this.erteiltAufWeg = erteiltAufWeg;
    }

    public int getErteiltAufWeg_Delayed() {
        return erteiltAufWeg_Delayed;
    }

    public void setErteiltAufWeg_Delayed(int erteiltAufWeg_Delayed) {
        this.erteiltAufWeg_Delayed = erteiltAufWeg_Delayed;
    }

    public int getMeldungEnthaltenInSammelkarte() {
        return meldungEnthaltenInSammelkarte;
    }

    public void setMeldungEnthaltenInSammelkarte(int meldungEnthaltenInSammelkarte) {
        this.meldungEnthaltenInSammelkarte = meldungEnthaltenInSammelkarte;
    }

    public int getMeldungEnthaltenInSammelkarte_Delayed() {
        return meldungEnthaltenInSammelkarte_Delayed;
    }

    public void setMeldungEnthaltenInSammelkarte_Delayed(int meldungEnthaltenInSammelkarte_Delayed) {
        this.meldungEnthaltenInSammelkarte_Delayed = meldungEnthaltenInSammelkarte_Delayed;
    }

    public int getMeldungEnthaltenInSammelkarteArt() {
        return meldungEnthaltenInSammelkarteArt;
    }

    public void setMeldungEnthaltenInSammelkarteArt(int meldungEnthaltenInSammelkarteArt) {
        this.meldungEnthaltenInSammelkarteArt = meldungEnthaltenInSammelkarteArt;
    }

    public int getMeldungEnthaltenInSammelkarteArt_Delayed() {
        return meldungEnthaltenInSammelkarteArt_Delayed;
    }

    public void setMeldungEnthaltenInSammelkarteArt_Delayed(int meldungEnthaltenInSammelkarteArt_Delayed) {
        this.meldungEnthaltenInSammelkarteArt_Delayed = meldungEnthaltenInSammelkarteArt_Delayed;
    }

    public int getWeisungVorhanden() {
        return weisungVorhanden;
    }

    public void setWeisungVorhanden(int weisungVorhanden) {
        this.weisungVorhanden = weisungVorhanden;
    }

    public int getWeisungVorhanden_Delayed() {
        return weisungVorhanden_Delayed;
    }

    public void setWeisungVorhanden_Delayed(int weisungVorhanden_Delayed) {
        this.weisungVorhanden_Delayed = weisungVorhanden_Delayed;
    }

    public String getVertreterName() {
        return vertreterName;
    }

    public void setVertreterName(String vertreterName) {
        this.vertreterName = vertreterName;
    }

    public String getVertreterName_Delayed() {
        return vertreterName_Delayed;
    }

    public void setVertreterName_Delayed(String vertreterName_Delayed) {
        this.vertreterName_Delayed = vertreterName_Delayed;
    }

    public String getVertreterVorname() {
        return vertreterVorname;
    }

    public void setVertreterVorname(String vertreterVorname) {
        this.vertreterVorname = vertreterVorname;
    }

    public String getVertreterVorname_Delayed() {
        return vertreterVorname_Delayed;
    }

    public void setVertreterVorname_Delayed(String vertreterVorname_Delayed) {
        this.vertreterVorname_Delayed = vertreterVorname_Delayed;
    }

    public String getVertreterOrt() {
        return vertreterOrt;
    }

    public void setVertreterOrt(String vertreterOrt) {
        this.vertreterOrt = vertreterOrt;
    }

    public String getVertreterOrt_Delayed() {
        return vertreterOrt_Delayed;
    }

    public void setVertreterOrt_Delayed(String vertreterOrt_Delayed) {
        this.vertreterOrt_Delayed = vertreterOrt_Delayed;
    }

    public int getVertreterIdent() {
        return vertreterIdent;
    }

    public void setVertreterIdent(int vertreterIdent) {
        this.vertreterIdent = vertreterIdent;
    }

    public int getVertreterIdent_Delayed() {
        return vertreterIdent_Delayed;
    }

    public void setVertreterIdent_Delayed(int vertreterIdent_Delayed) {
        this.vertreterIdent_Delayed = vertreterIdent_Delayed;
    }

    public int getWillenserklaerungMitVollmacht() {
        return willenserklaerungMitVollmacht;
    }

    public void setWillenserklaerungMitVollmacht(int willenserklaerungMitVollmacht) {
        this.willenserklaerungMitVollmacht = willenserklaerungMitVollmacht;
    }

    public int getWillenserklaerungMitVollmacht_Delayed() {
        return willenserklaerungMitVollmacht_Delayed;
    }

    public void setWillenserklaerungMitVollmacht_Delayed(int willenserklaerungMitVollmacht_Delayed) {
        this.willenserklaerungMitVollmacht_Delayed = willenserklaerungMitVollmacht_Delayed;
    }

    public int getStatusPraesenz() {
        return statusPraesenz;
    }

    public void setStatusPraesenz(int statusPraesenz) {
        this.statusPraesenz = statusPraesenz;
    }

    public int getStatusPraesenz_Delayed() {
        return statusPraesenz_Delayed;
    }

    public void setStatusPraesenz_Delayed(int statusPraesenz_Delayed) {
        this.statusPraesenz_Delayed = statusPraesenz_Delayed;
    }

    public int getStatusWarPraesenz() {
        return statusWarPraesenz;
    }

    public void setStatusWarPraesenz(int statusWarPraesenz) {
        this.statusWarPraesenz = statusWarPraesenz;
    }

    public int getStatusWarPraesenz_Delayed() {
        return statusWarPraesenz_Delayed;
    }

    public void setStatusWarPraesenz_Delayed(int statusWarPraesenz_Delayed) {
        this.statusWarPraesenz_Delayed = statusWarPraesenz_Delayed;
    }

    public int getVirtuellerTeilnehmerIdent() {
        return virtuellerTeilnehmerIdent;
    }

    public void setVirtuellerTeilnehmerIdent(int virtuellerTeilnehmerIdent) {
        this.virtuellerTeilnehmerIdent = virtuellerTeilnehmerIdent;
    }

    public int getTeilnahmeArt() {
        return teilnahmeArt;
    }

    public void setTeilnahmeArt(int teilnahmeArt) {
        this.teilnahmeArt = teilnahmeArt;
    }

    public int getVorlAnmeldung() {
        return vorlAnmeldung;
    }

    public void setVorlAnmeldung(int vorlAnmeldung) {
        this.vorlAnmeldung = vorlAnmeldung;
    }

    public int getVorlAnmeldungAkt() {
        return vorlAnmeldungAkt;
    }

    public void setVorlAnmeldungAkt(int vorlAnmeldungAkt) {
        this.vorlAnmeldungAkt = vorlAnmeldungAkt;
    }

    public int getVorlAnmeldungSer() {
        return vorlAnmeldungSer;
    }

    public void setVorlAnmeldungSer(int vorlAnmeldungSer) {
        this.vorlAnmeldungSer = vorlAnmeldungSer;
    }

    public int getMeldungsIdentVorgaenger() {
        return meldungsIdentVorgaenger;
    }

    public void setMeldungsIdentVorgaenger(int meldungsIdentVorgaenger) {
        this.meldungsIdentVorgaenger = meldungsIdentVorgaenger;
    }

    public int getMeldungsIdentNachfolger() {
        return meldungsIdentNachfolger;
    }

    public void setMeldungsIdentNachfolger(int meldungsIdentNachfolger) {
        this.meldungsIdentNachfolger = meldungsIdentNachfolger;
    }

    public int getZutrittsIdentVers() {
        return zutrittsIdentVers;
    }

    public void setZutrittsIdentVers(int zutrittsIdentVers) {
        this.zutrittsIdentVers = zutrittsIdentVers;
    }

    public int getVertreterNummer() {
        return vertreterNummer;
    }

    public void setVertreterNummer(int vertreterNummer) {
        this.vertreterNummer = vertreterNummer;
    }

    public int getStatusPraesenzGast() {
        return statusPraesenzGast;
    }

    public void setStatusPraesenzGast(int statusPraesenzGast) {
        this.statusPraesenzGast = statusPraesenzGast;
    }

    public int getStatusWarPraesenzGast() {
        return statusWarPraesenzGast;
    }

    public void setStatusWarPraesenzGast(int statusWarPraesenzGast) {
        this.statusWarPraesenzGast = statusWarPraesenzGast;
    }

    public String getGebDatum() {
        return gebDatum;
    }

    public void setGebDatum(String gebDatum) {
        this.gebDatum = gebDatum;
    }

    public String getZutrittsIdentVorgaenger() {
        return zutrittsIdentVorgaenger;
    }

    public void setZutrittsIdentVorgaenger(String zutrittsIdentVorgaenger) {
        this.zutrittsIdentVorgaenger = zutrittsIdentVorgaenger;
    }

    public int getZutrittsIdentVersVorgaenger() {
        return zutrittsIdentVersVorgaenger;
    }

    public void setZutrittsIdentVersVorgaenger(int zutrittsIdentVersVorgaenger) {
        this.zutrittsIdentVersVorgaenger = zutrittsIdentVersVorgaenger;
    }

    public String getZutrittsIdentNachfolger() {
        return zutrittsIdentNachfolger;
    }

    public void setZutrittsIdentNachfolger(String zutrittsIdentNachfolger) {
        this.zutrittsIdentNachfolger = zutrittsIdentNachfolger;
    }

    public int getZutrittsIdentVersNachfolger() {
        return zutrittsIdentVersNachfolger;
    }

    public void setZutrittsIdentVersNachfolger(int zutrittsIdentVersNachfolger) {
        this.zutrittsIdentVersNachfolger = zutrittsIdentVersNachfolger;
    }

    public EclWillenserklaerung[] getPraesenzveraenderungarray() {
        return praesenzveraenderungarray;
    }

    public void setPraesenzveraenderungarray(EclWillenserklaerung[] praesenzveraenderungarray) {
        this.praesenzveraenderungarray = praesenzveraenderungarray;
    }

    public EclMeldungAusstellungsgrund[] getMeldungausstellungsgrundarray() {
        return meldungausstellungsgrundarray;
    }

    public void setMeldungausstellungsgrundarray(EclMeldungAusstellungsgrund[] meldungausstellungsgrundarray) {
        this.meldungausstellungsgrundarray = meldungausstellungsgrundarray;
    }

    public EclMeldungVipKZ[] getMeldungVipKZarray() {
        return meldungVipKZarray;
    }

    public void setMeldungVipKZarray(EclMeldungVipKZ[] meldungVipKZarray) {
        this.meldungVipKZarray = meldungVipKZarray;
    }

    public EclMeldungenMeldungen[] getZuMeldungenarray() {
        return zuMeldungenarray;
    }

    public void setZuMeldungenarray(EclMeldungenMeldungen[] zuMeldungenarray) {
        this.zuMeldungenarray = zuMeldungenarray;
    }

    public EclMeldungenMeldungen[] getVonMeldungenarray() {
        return vonMeldungenarray;
    }

    public void setVonMeldungenarray(EclMeldungenMeldungen[] vonMeldungenarray) {
        this.vonMeldungenarray = vonMeldungenarray;
    }

    public EclAenderungslog[] getAenderungslogarray() {
        return aenderungslogarray;
    }

    public void setAenderungslogarray(EclAenderungslog[] aenderungslogarray) {
        this.aenderungslogarray = aenderungslogarray;
    }

    /*
     * Für Suche in Präsenz Übersicht
     */
    public String searchString() {
        return aktionaersnummer + " " + zutrittsIdent + " " + vorname + " " + name + " " + vertreterVorname + " "
                + vertreterName;
    }

}