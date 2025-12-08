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
import java.util.LinkedList;
import java.util.List;

import de.meetingapps.meetingportal.meetComEntities.EclMeldung;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

@SessionScoped
@Named
public class EclMeldungM implements Serializable {
    private static final long serialVersionUID = 7438761678241678662L;

    /** eindeutiger Key für Meldesatz (zusammen mit mandant), der unveränderlich ist. Über MeldungsIdent Zuordnung z.B. 
     * zu Stimmausschlüssen, zu virtuellen Karten, in andere Dateien etc.
     */
    private int meldungsIdent = 0;

    /**Versionsnummer - manuell nicht verändern! Ausschließlich durch Verwendung in DbMeldungen*/
    private long db_version;

    /**Versionsnummer von lPersonNatJur - wird benötigt für Update etc., wenn personNatJur aus dbMeldungen heraus "gefüttert" wird*/
    private long db_version_personenNatJur = 0;

    /**Mandantennummer*/
    private int mandant = 0;

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
    private int meldungAktiv = 1;

    /** 0 Gast, 1 = aktienrechtliche Anmeldung; Achtung, früher umgekehrt!
     * Zusammen mit zutrittsIdent und mandant UNIQUE
     */
    private int klasse = 0;

    /**Aktionärsnummer aus Aktienregister
     * Length=20
     */
    private String aktionaersnummer = "";

    /**Key, mit dem auf externe Systeme verwiesen wird (Eindeutig, außer bei Split!)*/
    private String externeIdent = "";

    /**0 = Gastkarte; 1 = Einzelkarte-Aktionär; 2 = Sammelkarte; 3 = in Sammelkarte
     * Entsprechend EnMeldungKartenart */
    private int meldungstyp = 0;

    /**falls meldungstyp=2 Sammelkarte: 1 => Sammelkarte nimmt Weisungen auf
     * Verwendung derzeit unklar - ist ja eigentlich jetzt in skWeisungsartZulaessig abgebildet*/
    @Deprecated
    private int skMitWeisungen = 0;

    /**falls Meldungstyp=2 Sammelkarte:
     *1=KIAV
     *2=SRV
     *3=organisatorisch
     *4=Briefwahl
     *5=Dauervollmacht*/
    private int skIst = 0;

    /**Weisung zulässig für diese Sammelkarte (nur bei Meldungstyp=2)
     * =1 Weisungen sind grundsätzlich möglich - und damit auch zwingend zu speichern (ggf. unmarkiert = frei)
     * =2 Freie Weisungen möglich (d.h. Weisungsempfänger kann frei abstimmen)
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
    private int skWeisungsartZulaessig = 0;

    /**falls Meldungstyp=2 Sammelkarte: Wird dann in Auswahl angeboten*/
    private int skBuchbarInternet = 0;

    /**falls Meldungstyp=2 Sammelkarte: Wird dann in Auswahl angeboten*/
    private int skBuchbarPapier = 0;

    /**falls Meldungstyp=2 Sammelkarte: Wird dann in Auswahl angeboten*/
    private int skBuchbarHV = 0;

    /**falls Meldungstyp=2 Sammelkarte: Wird dann in Auswahl angeboten*/
    private int skBuchbarVollmachtDritte = 0;

    /**falls Meldungstyp=2 Sammelkarte: Wird dann in Auswahl angeboten*/
    private int skBuchbarVollmachtDritteHV = 0;

    /**falls Sammelkarte: 0=Offenlegung wie Parameter (noch nicht unterstützt), 1=Offenlegung, -1=keine Offenlegung*/
    private int skOffenlegung;

    /**Vorabzuordnung
     * Length=20
     * */
    private String stimmkarteVorab = "";
    /**Vorabzuordnung
     * Length=20*/
    private String stimmkarteSecondVorab = "";

    /**Vorzüge, Stämme etc. - Verweis auf Grunddaten-Id.
     * Noch nicht fertig durchdacht.
     */
    private int gattung = 0;

    private long stueckAktien = 0;

    private long stimmen = 0;

    /**Noch offen: was bei mehreren Drucken? Bei mehreren
     * Ausstellungen? Evtl. hier raus, und in eine
     * gesonderte Table der herausgegebenen ZutrittsIds rein
     */
    private long stueckAktienDruckEK = 0;

    private long stimmenDruckEK = 0;

    /**E/F/V
     * Length=1*/
    private String besitzart = "";

    /**13 Stellen: V/A/S/1-9,0*/
    private String stimmausschluss = "";

    /**0 / 1 (für Gäste, z.B. wenn Karte auch für Wortmeldung benutzt werden kann)*/
    private int zusatzrechte = 0;

    /**Verweis auf die ausgelagerten Personendaten in DbPersonenNatJur
     * Hinweis: mehrere Meldungssätze können auf die selbe personenNatJur verweisen! Konkrete Fälle z.B.:
     * > wenn einem Aktienregistersatz mehrere "EKs" ausgestellt werden
     * > wenn die Inhaberanmeldestelle 2 Eintrittskarten für die selbe Person ausstellt
     * 
     *  Beim Ändern dieser Daten (durch ein Dialogprogramm) muß der Benutzer ggf. auf diesen Umstand hingewiesen
     *  werden!
     */
    private int personenNatJurIdent;

    /*********************************************Ausgelagert in PersonenNatJur***************************************/
    /**Für Teilnehmerverzeichnis. Wird aus Detailfelder zusammengesetzt, wenn diese gefüllt
     * Length=80*/
    private String kurzName = "";

    /**Für Teilnehmerverzeichnis. Wird aus Detailfelder zusammengesetzt, wenn diese gefüllt
     * Length=80*/
    private String kurzOrt = "";

    /**Verweis auf Anredendatei*/
    private int anrede = 0;

    /**Length=30*/
    private String titel = "";

    /**Length=30*/
    private String adelstitel = "";

    /**Length=80*/
    private String name = "";

    /**Length=80*/
    private String vorname = "";

    /**Length=80*/
    private String zuHdCo = "";

    /**Length=80*/
    private String zusatz1 = "";

    /**Length=80*/
    private String zusatz2 = "";

    /**Length=80*/
    private String strasse = "";

    /**Length=4*/
    private String land = "";

    /**Length=20*/
    private String plz = "";

    /**Length=80*/
    private String ort = "";

    /**Verweis auf Satz in tbl_personenNatJurVersandadresse*/
    private int identVersandadresse = 0;

    /**Mailadresse für elektronischen Versand von EKs etc.
     * (nicht zu verwechseln mit der Registrierung im
     * Email-Portal für den elektronischen Einladungsversand)
     * Length=80*/
    private String mailadresse = "";

    /**Konkrete Verwendung noch nicht ganz klar – wird im Rahmen des Gästemoduls entwickelt*/
    private int kommunikationssprache = 0;

    /** Für die jeweiligen Person selbst.
     * Length=20
     */
    private String loginKennung = "";

    /**Length=20*/
    private String loginPasswort = "";

    /**Length=30
     * AAA = Mandantenschlüssel
     * BBBBBB = HV-Datum
     * CC=zwei Buchstaben Vorname
     * DD=zwei Buchstaben Ort
     * EEEEE =lfd Nummer*/
    private String oeffentlicheID = "";

    /**********************Bis hierher: ausgelagert in PersonenNatJur***********************************/

    /**Length=40*/
    private String zusatzfeld1 = "";

    /**Length=40*/
    private String zusatzfeld2 = "";

    /**Length=40*/
    private String zusatzfeld3 = "";

    /**Length=40*/
    private String zusatzfeld4 = "";

    /**Length=40*/
    private String zusatzfeld5 = "";

    /**Verweis auf Gruppendatei - zugeordnete Gruppe*/
    private int gruppe = 0;

    /**Datum, Uhrzeit
     * Length=19
     */
    private String neuanlageDatumUhrzeit = "";

    /**Datum, Uhrzeit
     * Length=19
     */
    private String geaendertDatumUhrzeit = "";

    /**1=J/2=N*/
    private int manuellGeaendertSeitLetztemImport;

    /****************************Willenserklärungs-Felder***********************************************
     * Die folgenden Felder werden nicht direkt gepflegt, sondern über Willenserklärungen eingebucht.
     * Sie geben jeweils den aktuellen Stand (mit / ohne Delayed) wieder.
     */

    /**=1 => es sind delayed-Willenserklärungen vorhanden*/
    private int delayedVorhanden = 0;

    /**=1 => es sind pending-Willenserklärungen vorhanden*/
    private int pendingVorhanden = 0;

    /**Eindeutiger Zutritts-Key, mit dem sich der Aktionär ausweisen kann. Kann aber wechseln! Kann „Null“ sein.
     * UNIQUE zusammen mit mandant und klasse (d.h. Gastkarte und Aktionärskarte kann selbe ZutrittsIdent haben,
     * sollte bei interner Anwendung vermieden werden, kann aber gegenüber arfuehrer001 und arfuehrer005 nicht
     * ausgeschlossen werden).
     * Length=20
     */
    private String zutrittsIdent = "";
    private String zutrittsIdent_Delayed = "";

    /**Lenght=20*/
    private String stimmkarte = "";
    private String stimmkarte_Delayed = "";
    /**zweite zugeordnete Stimmkarte, z.B. für Voter
     * Length=20
     */
    private String stimmkarteSecond = "";
    private String stimmkarteSecond_Delayed = "";

    /**aktuelle Willenserklärung (Art, aus Enwillenserklaerung), gemäß der eine Sammelkartenzuordnung durchgeführt wurde.
     * D.h. es wird die Willenserklärung eingetragen, die zu dem Eintrag in die Sammelkarte geführt hat (also nur SRV, KIAV, Briefwahl,
     * Dauervollmachten, organisatorisches).
     * Näheres siehe auch Doku in BlWillenserklaerung.
     * =0 => meldung ist nicht in Sammelkarte.
     */
    private int willenserklaerung = 0;
    private int willenserklaerung_Delayed = 0;
    /**Hier wird die Willenserklärung-ID eingetragen, die zu der Zuordnung geführt hat.
     * Bei "Weisungs-Änderungen": hier wird die letzte Willenserklärungsnummer eingetragen (also die der letzten Änderung), da gegen diese
     * bei neuen Willenserklärungen verglichen werden muß. (als Art bleibt aber weiterhin die ursprüngliche Erteilung, nicht die 
     * "Änderungs-Art")
     */
    private int willenserklaerungIdent = 0;
    private int willenserklaerungIdent_Delayed = 0;

    /**Veränderungszeit, zu der die letzte Willenserklaerung/Sammelkarte (siehe willenserklaerung) erteilt wurde. Ist hier gespeichert zum leichteren Abgleich,
     * ob eine neuere Willenserklärung diese überschreibt
     * Length=19 
     */
    private String veraenderungszeit = "";
    private String veraenderungszeit_Delayed = "";
    /**Weg, auf dem die letzte Willenserklaerung/Sammelkarte (siehe willenserklaerung) erteilt wurde. Ist hier gespeichert zum leichteren Abgleich,
     * ob eine neuere Willenserklärung diese überschreibt
     */
    private int erteiltAufWeg = 0;
    private int erteiltAufWeg_Delayed = 0;

    /** !=0 => Karte ist in Sammelkarte, Verweis auf Sammelkarte (ident!). Nur zulässig bei meldungstyp=1*/
    private int meldungEnthaltenInSammelkarte = 0;
    private int meldungEnthaltenInSammelkarte_Delayed = 0;
    /**Falls Karte in Sammelkarte, dann ist hier Art der Sammelkarte dokumentiert
     *  1=KIAV; 2=SRV; 3=organisatorisch; 4=Briefwahl (analog EnMeldungKartenart)
     */
    private int meldungEnthaltenInSammelkarteArt = 0;
    private int meldungEnthaltenInSammelkarteArt_Delayed = 0;

    /**vor allem für Einzelkarten vorgesehen; für Sammelkarten noch unklar*/
    private int weisungVorhanden = 0;
    private int weisungVorhanden_Delayed = 0;

    /** Aktueller Vertreter
     * Length=80
     */
    private String vertreterName = "";
    private String vertreterName_Delayed = "";
    /** Aktueller VertreterVorname
     * Length=80
     */
    private String vertreterVorname = "";
    private String vertreterVorname_Delayed = "";
    /** Aktueller Vertreterort
     * Length=80
     */
    private String vertreterOrt = "";
    private String vertreterOrt_Delayed = "";
    /**Verweis auf personNatJur des Vertreters*/
    private int vertreterIdent = 0;
    private int vertreterIdent_Delayed = 0;

    /**Verweis auf Vollmacht in EclWillenserklaerung*/
    @Deprecated
    private int willenserklaerungMitVollmacht = 0;
    @Deprecated
    private int willenserklaerungMitVollmacht_Delayed = 0;

    /**Gibt den GERADE aktuellen Präsenzstatus wieder (kein Rückschluß auf
     * Historie!) - als Aktionär.
     * Wird nur für Einzelkarten / Sammelkarte gefüllt, nicht für
     * "in Sammelkarten enthaltene". 
     * Eine Karte, die bereits in Sammelkarte ist, aber früher selbst anwesend war,
     * ist über "statusWarPraesenz" gekennzeichnet.
     * 
     * 0 = nicht anwesend; 1 = anwesend; 2 = war anwesend; 4 = in Sammelkarte
     * */
    private int statusPraesenz = 0;
    private int statusPraesenz_Delayed = 0;

    /**Historie - diese Karte war als Einzelkarte (nicht in Sammelkarte enthalten)
     * irgendwann mal als Teilnehmer (selbst oder Bevollmächtigt) präsent
     * */
    private int statusWarPraesenz = 0;
    private int statusWarPraesenz_Delayed = 0;

    /*Unklar, ob dieses Feld noch benötigt wird*/
    /**Noch unklar, wofür das benötigt wird:
     * 1 = persönlich; 2 = Vertretung; 3 = Fernteilnahme; 4 = Briefwahl
     */
    /*TODO Unklar, ob dieses Feld noch benötigt wird*/
    private int teilnahmeArt = 0;

    /*Die folgenden Felder werden nicht mehr verwendet. Nur noch für Compilierzwecke enthalten.
     * In Datenbank bereits entfernt*/
    /*TODO Die folgenden Felder werden nicht mehr verwendet. Nur noch für Compilierzwecke enthalten.*/
    /** „früherer Datensatz“, d.h. bei Ausstellung einer Ersatzkarte steht in meldungsIdentVorgaenger die „alte“ stornierte ZutrittsIdent*/
    private int meldungsIdentVorgaenger = 0;

    /** „nachfolgender Datensatz“, d.h. bei einer „alten“ stornierte ZutrittsIdent steht dort der Verweis auf den (nächstgültigen) neueren Datensatz */
    private int meldungsIdentNachfolger = 0;

    /**„Versionierung“ von stornierten, aber wieder freigegebenen zutrittsIdent. Standardmäßig = 0*/
    private int zutrittsIdentVers = 0;

    /**Verweis auf Vertretername/ort in separater Tabelle (der dann  einheitlich ist)*/
    private int vertreterNummer = 0;

    /**Gibt den GERADE aktuellen Präsenzstatus  wieder (kein Rückschluß auf
     * Historie!) - als Gast
     * 0 = nicht anwesend; 1 = anwesend; 2 = war anwesend;
     * Nicht mehr verwendet!
     * */
    private int statusPraesenzGast = 0;

    /**Historie - war irgendwann mal als Gast präsent
     * Nicht mehr verwendet!*/
    private int statusWarPraesenzGast = 0;

    @Deprecated
    /**Length=10*/
    private String gebDatum = "";

    /*******************Die folgenden Felder dienen nur der Anzeige in der Browseroberfläche!***********************/
    private List<String> anzeigeSkWeisungsartZulaessig = null;
    private List<EclWillensErklVollmachtenAnDritteM> vollmachtenAnDritte = null;

    /* ***************************************************************************************************
     * Die folgenden Felder werden beim read mit eingelesen, sind jedoch Informationen aus anderen Sätzen.
     * Sie dürfen nicht verändert werden, bzw. werden beim Update / Insert nicht mitgespeichert / berücksichtigt!
     */

    /* Infos zu Ersatz-Eintrittskarten - Vorgänger und Nachfolger zutrittsIdent, jeweils der
     * in meldungsIdentVorgaenger und meldungsIdentNachfolger entsprechenden Sätzen
     */
    private String zutrittsIdentVorgaenger = "";
    private int zutrittsIdentVersVorgaenger = 0;
    private String zutrittsIdentNachfolger = "";
    private int zutrittsIdentVersNachfolger = 0;

    public void copyFrom(EclMeldung pEclMeldung) {

        this.meldungsIdent = pEclMeldung.meldungsIdent;
        this.db_version = pEclMeldung.db_version;
        this.mandant = pEclMeldung.mandant;
        this.meldungAktiv = pEclMeldung.meldungAktiv;
        this.klasse = pEclMeldung.klasse;
        this.aktionaersnummer = pEclMeldung.aktionaersnummer;
        this.externeIdent = pEclMeldung.externeIdent;
        this.meldungstyp = pEclMeldung.meldungstyp;
        this.skMitWeisungen = pEclMeldung.skMitWeisungen;
        this.skIst = pEclMeldung.skIst;
        this.skWeisungsartZulaessig = pEclMeldung.skWeisungsartZulaessig;
        this.skBuchbarInternet = pEclMeldung.skBuchbarInternet;
        this.skBuchbarPapier = pEclMeldung.skBuchbarPapier;
        this.skBuchbarHV = pEclMeldung.skBuchbarHV;
        this.skBuchbarVollmachtDritte = pEclMeldung.skBuchbarVollmachtDritte;
        this.skBuchbarVollmachtDritteHV = pEclMeldung.skBuchbarVollmachtDritteHV;
        this.skOffenlegung = pEclMeldung.skOffenlegung;
        this.stimmkarteVorab = pEclMeldung.stimmkarteVorab;
        this.stimmkarteSecondVorab = pEclMeldung.stimmkarteSecondVorab;
        this.gattung = pEclMeldung.gattung;
        this.stueckAktien = pEclMeldung.stueckAktien;
        this.stimmen = pEclMeldung.stimmen;
        this.stueckAktienDruckEK = pEclMeldung.stueckAktienDruckEK;
        this.stimmenDruckEK = pEclMeldung.stimmenDruckEK;
        this.besitzart = pEclMeldung.besitzart;
        this.stimmausschluss = pEclMeldung.stimmausschluss;
        this.zusatzrechte = pEclMeldung.zusatzrechte;
        this.personenNatJurIdent = pEclMeldung.personenNatJurIdent;
        this.kurzName = pEclMeldung.kurzName;
        this.kurzOrt = pEclMeldung.kurzOrt;
        this.anrede = pEclMeldung.anrede;
        this.titel = pEclMeldung.titel;
        this.adelstitel = pEclMeldung.adelstitel;
        this.name = pEclMeldung.name;
        this.vorname = pEclMeldung.vorname;
        this.zuHdCo = pEclMeldung.zuHdCo;
        this.zusatz1 = pEclMeldung.zusatz1;
        this.zusatz2 = pEclMeldung.zusatz2;
        this.strasse = pEclMeldung.strasse;
        this.land = pEclMeldung.land;
        this.plz = pEclMeldung.plz;
        this.ort = pEclMeldung.ort;
        this.identVersandadresse = pEclMeldung.identVersandadresse;
        this.mailadresse = pEclMeldung.mailadresse;
        this.kommunikationssprache = pEclMeldung.kommunikationssprache;
        this.loginKennung = pEclMeldung.loginKennung;
        this.loginPasswort = pEclMeldung.loginPasswort;
        this.oeffentlicheID = pEclMeldung.oeffentlicheID;
        this.zusatzfeld1 = pEclMeldung.zusatzfeld1;
        this.zusatzfeld2 = pEclMeldung.zusatzfeld2;
        this.zusatzfeld3 = pEclMeldung.zusatzfeld3;
        this.zusatzfeld4 = pEclMeldung.zusatzfeld4;
        this.zusatzfeld5 = pEclMeldung.zusatzfeld5;
        this.gruppe = pEclMeldung.gruppe;
        this.neuanlageDatumUhrzeit = pEclMeldung.neuanlageDatumUhrzeit;
        this.geaendertDatumUhrzeit = pEclMeldung.geaendertDatumUhrzeit;
        this.manuellGeaendertSeitLetztemImport = pEclMeldung.manuellGeaendertSeitLetztemImport;

        this.delayedVorhanden = pEclMeldung.delayedVorhanden;
        this.pendingVorhanden = pEclMeldung.pendingVorhanden;
        this.zutrittsIdent = pEclMeldung.zutrittsIdent;
        this.zutrittsIdent_Delayed = pEclMeldung.zutrittsIdent_Delayed;
        this.stimmkarte = pEclMeldung.stimmkarte;
        this.stimmkarte_Delayed = pEclMeldung.stimmkarte_Delayed;
        this.stimmkarteSecond = pEclMeldung.stimmkarteSecond;
        this.stimmkarteSecond_Delayed = pEclMeldung.stimmkarteSecond_Delayed;

        this.willenserklaerung = pEclMeldung.willenserklaerung;
        this.willenserklaerung_Delayed = pEclMeldung.willenserklaerung_Delayed;
        this.willenserklaerungIdent = pEclMeldung.willenserklaerungIdent;
        this.willenserklaerungIdent_Delayed = pEclMeldung.willenserklaerungIdent_Delayed;
        this.veraenderungszeit = pEclMeldung.veraenderungszeit;
        this.veraenderungszeit_Delayed = pEclMeldung.veraenderungszeit_Delayed;
        this.erteiltAufWeg = pEclMeldung.erteiltAufWeg;
        this.erteiltAufWeg_Delayed = pEclMeldung.erteiltAufWeg_Delayed;

        this.meldungEnthaltenInSammelkarte = pEclMeldung.meldungEnthaltenInSammelkarte;
        this.meldungEnthaltenInSammelkarte_Delayed = pEclMeldung.meldungEnthaltenInSammelkarte_Delayed;
        this.meldungEnthaltenInSammelkarteArt = pEclMeldung.meldungEnthaltenInSammelkarteArt;
        this.meldungEnthaltenInSammelkarteArt_Delayed = pEclMeldung.meldungEnthaltenInSammelkarteArt_Delayed;
        this.weisungVorhanden = pEclMeldung.weisungVorhanden;
        this.weisungVorhanden_Delayed = pEclMeldung.weisungVorhanden_Delayed;
        this.vertreterName = pEclMeldung.vertreterName;
        this.vertreterName_Delayed = pEclMeldung.vertreterName_Delayed;
        this.vertreterVorname = pEclMeldung.vertreterVorname;
        this.vertreterVorname_Delayed = pEclMeldung.vertreterVorname_Delayed;
        this.vertreterOrt = pEclMeldung.vertreterOrt;
        this.vertreterOrt_Delayed = pEclMeldung.vertreterOrt_Delayed;
        this.vertreterIdent = pEclMeldung.vertreterIdent;
        this.vertreterIdent_Delayed = pEclMeldung.vertreterIdent_Delayed;
        this.willenserklaerungMitVollmacht = pEclMeldung.willenserklaerungMitVollmacht;
        this.willenserklaerungMitVollmacht_Delayed = pEclMeldung.willenserklaerungMitVollmacht_Delayed;
        this.statusPraesenz = pEclMeldung.statusPraesenz;
        this.statusPraesenz_Delayed = pEclMeldung.statusPraesenz_Delayed;
        this.statusWarPraesenz = pEclMeldung.statusWarPraesenz;
        this.statusWarPraesenz_Delayed = pEclMeldung.statusWarPraesenz_Delayed;

        this.teilnahmeArt = pEclMeldung.teilnahmeArt;

        /*Anzeigefelder füllen*/

        this.anzeigeSkWeisungsartZulaessig = new LinkedList<String>();
        if ((pEclMeldung.skWeisungsartZulaessig & 1) == 1) {
            this.anzeigeSkWeisungsartZulaessig.add("Weisungen möglich und zwingend");
        }
        if ((pEclMeldung.skWeisungsartZulaessig & 2) == 2) {
            this.anzeigeSkWeisungsartZulaessig.add("Freie Weisungen möglich");
        }
        if ((pEclMeldung.skWeisungsartZulaessig & 4) == 4) {
            this.anzeigeSkWeisungsartZulaessig.add("Dedizierte Weisungen möglich");
        }
        if ((pEclMeldung.skWeisungsartZulaessig & 8) == 8) {
            this.anzeigeSkWeisungsartZulaessig.add("Weisungen gemäß eigener Empfehlung");
        }

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

    public int getTeilnahmeArt() {
        return teilnahmeArt;
    }

    public void setTeilnahmeArt(int teilnahmeArt) {
        this.teilnahmeArt = teilnahmeArt;
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

    public List<String> getAnzeigeSkWeisungsartZulaessig() {
        return anzeigeSkWeisungsartZulaessig;
    }

    public void setAnzeigeSkWeisungsartZulaessig(List<String> anzeigeSkWeisungsartZulaessig) {
        this.anzeigeSkWeisungsartZulaessig = anzeigeSkWeisungsartZulaessig;
    }

    public List<EclWillensErklVollmachtenAnDritteM> getVollmachtenAnDritte() {
        return vollmachtenAnDritte;
    }

    public void setVollmachtenAnDritte(List<EclWillensErklVollmachtenAnDritteM> vollmachtenAnDritte) {
        this.vollmachtenAnDritte = vollmachtenAnDritte;
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

    public int getIdentVersandadresse() {
        return identVersandadresse;
    }

    public void setIdentVersandadresse(int identVersandadresse) {
        this.identVersandadresse = identVersandadresse;
    }

    public int getSkOffenlegung() {
        return skOffenlegung;
    }

    public void setSkOffenlegung(int skOffenlegung) {
        this.skOffenlegung = skOffenlegung;
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

}
