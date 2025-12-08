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
import de.meetingapps.meetingportal.meetComBl.BlWillenserklaerungStatusNeu;
import de.meetingapps.meetingportal.meetComEntities.EclAktienregister;
import de.meetingapps.meetingportal.meetComEntities.EclBesitzJeKennung;
import de.meetingapps.meetingportal.meetComEntities.EclMeldung;
import de.meetingapps.meetingportal.meetComEntities.EclZugeordneteMeldungNeu;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

/**Gesamtbesitz, der der aktuellen Kennung zugeordnet ist. Ausgangs-Bean für die Anzeige des Besitzes im Portal.
 * 
 * Grundsatzüberlegungen
 * =====================
 * Kurzer Überblick
 * ----------------
 * Der gerade eingeloggten Kennung werden folgende Besitze zugeordnet:
 * > die direkt der eingeloggten Kennung zugeordneten Besitze (=erstes ("nulltes") Element in besitzJeKennungMListe) 
 * > die Besitze aller der Kennungen, die mit der eingeloggten Kennung verbunden sind (alle folgenden Elemente)
 * 
 * Zugeordnete Insti-Bestände
 * --------------------------
 * Anmeldungen / Weisungen: normale Bearbeitung (hierfür keine Vollmacht nötig)
 * Präsenz: keetdbne Präsenzveränderungen möglich.
 * 		Begründung: hierfür ist Vollmacht nötig. Diese Vollmacht wird entweder vom BO gebucht (dann der Hauptkennung zugeordnet),
 * 		oder sie werden vom Insti selbst als weitere Kennung hinzugebucht.
 *
 * Handling von Mehrfachzuordnungen
 * --------------------------------
 * Durch Vollmachten, Instizuordnung und Zuordnung weiterer Kennungen ist es möglich, dass dem selben Benutzer der gleiche
 * Meldebestand mehrfach zugeordnet wird. Diese Doppeltzuordnungen müssen im Portal zwar angezeigt werden, für diese darf
 * jedoch keine Sammel-Aktion ausgeführt werden (Einzelaktionen sind allerdings sehr wohl möglich, und müssen auch möglich sein -
 * sonst kann z.B. ein Insti-Bestand, für den eine Vollmacht auf die Kennung ausgestellt wurde, nicht mehr bearbeitet werden).
 * 
 * Nach folgendem Algorithmus wird priorisiert (erstgenannte sind die höchstpriorisierten):
 * > Direkt der eigenen Kennung zugeordnete Vollmachten
 * > Direkt zugeordneten Kennungen zugeordnete Vollmachten
 * > Direkt der eigenen Kennung zugeordnete Bestände
 * > Direkt zugeordnete Kennungen zugeordnete Bestände
 * > Insti über AR zugeordnete Bestände
 * > Insti über Meldungen zugeordnete Bestände
 * 
 */
@SessionScoped
@Named
public class EclBesitzGesamtM implements Serializable {
    private static final long serialVersionUID = 5089027272894627379L;

    private int logDrucken=3;

    /**Speziell für virtuelle HV - Kennung ist zugeschaltet.*/
    private boolean kennungIstOnlinePraesent=false;
    
    private boolean kennungHatWortmeldungErteilt=false;

    /**Wenn true, dann sind im EclBesitzGesamtM mehrere Bestände vorhanden, d.h.:
     * > Im Portal ist eine Mehrfachauswahl möglich
     * > Zu jedem Bestand / jeder Meldung wird eine laufende Nummer angezeigt
     * > Fehlermeldungen bzgl. z.B. Zugang/Abgang enthalten die lfd.Nr.
     */
    private boolean mehrereBestaendeVorhanden = false;

    /**Wenn true, dann wurden der eingeloggten Kennung weitere Kennungen zugeordnet - im Portal
     * wird dann vor den Besitzen jeder Kennung ein Trenn-Text angezeigt.
     */
    private boolean weitereKennungen = false;

    /**true >= mindestens eine Kennung ist eine Insti-Kennung*/
    private boolean instiKennungZugeordnet=false;

    /**In besitzeJeKennungListe sind nicht-angemeldete Bestände vorhanden. D.h. dann:
     * > Anzeige eines Starttexte bzgl. Anmeldung noch erforderlich bzw. (nach Anmeldeschluß) Anmeldeschluß abgelaufen, Anmeldungen der Bestände nicht mehr möglich
     * > Ggf. "Sammel-Anmeldebuttons" anbieten.
     */
    private boolean nichtAngemeldeteVorhanden = false;

    /**In besitzeJeKennungListe sind angemeldete Bestände vorhanden. D.h. dann:
     * > Anzeige eines Starttextes, dass ggf. weitere Aktionen möglich sind.
     * > Ggf. "Sammel-Aktionrs-Buttons" anbieten.
     */
    private boolean angemeldeteVorhanden = false;

    /**In besitzeJeKennungListe sind angemeldete Bestände vorhanden, die
     * NICHT ausgeblendet sind. D.h. ausschlaggebender Wert
     * für die Wahrnehmung von Aktionärsrechten mit der Bedingung
     * "Anmeldeter Aktionär".:
     */
    private boolean angemeldeteNichtAusgeblendeteVorhanden = false;

    /**Nur Portal: In besitzeJeKennungListe sind Bestände vorhanden, die ausgeblendet sind,
     * d.h. von eingeloggten Person nicht vertreten werden.
     */
    private boolean ausgeblendeteVorhanden = false;

    /**Für Portal: Es sind Einträge vorhanden, für die von einer anderen als der angemeldeten
     * Person Weisung erteilt wurden
     */
    private boolean bereitsErteiltWeisungBriefwahlAllgemeinDurchAndereVorhanden = false;

    /**Anzahl der Aktien, die insgesamt angemeldet / Vertreten werden. D.h. Summe aus allen Meldungen,
     * die ausgeblendet=false haben
     */
    private long aktienInsgesamtAngemeldet=0;

    /**Liste aller Kennungen, die zusammengefaßt wurden. [0]=die Kennung, mit der
     * gerade eingeloggt ist
     */
    private List<EclBesitzJeKennung> besitzJeKennungListe = null;

    /**Liste aller Kennungen, die nicht präsent sind.
     */
    private List<EclBesitzJeKennung> besitzJeKennungListeNichtPraesent = null;

    /**Liste aller Kennungen, die bereits präsent sind.
     */
    private List<EclBesitzJeKennung> besitzJeKennungListePraesent = null;

    /**Portal: Liste aller Meldungen, für die von einer anderen Person bereits Weisung erteilt wurde*/
    private List<EclZugeordneteMeldungNeu> meldungenBereitsErteiltWeisungAllgemeinDurchAndereListe=null;

    /**
     * Gibt an, ob in allen zugeordneten / eingelesenen Sätzen auch ein präsenter vorhanden ist.
     * 
     * Wird beim Einlesen des Status gefüllt.
     * Dient als Render-Anzeige für die Auswahlmaske.
     * 
     * Achtung, bei nurRawLiveAbstimmung==1 immer false, da in diesem Fall der Präsenzstatus nicht
     * in die Tables zurückgespeichert wird, sondern nur im Speicher gehalten wird.
     */
    private int anzPraesenteVorhanden = 0;
    private int anzPraesenteVorhandenEigene = 0;
    private int anzPraesenteVorhandenVertretene = 0;
    /**
     * Gibt an, ob in allen zugeordneten / eingelesenen Sätzen auch ein präsenter vorhanden ist.
     * 
     * Wird beim Einlesen des Status gefüllt.
     * Dient als Render-Anzeige für die Auswahlmaske.
     * 
     * Achtung, bei nurRawLiveAbstimmung==1 immer false, da in diesem Fall der Präsenzstatus nicht
     * in die Tables zurückgespeichert wird, sondern nur im Speicher gehalten wird.
     */
    private int anzNichtPraesenteVorhanden = 0;
    private int anzNichtPraesenteVorhandenEigene = 0;
    private int anzNichtPraesenteVorhandenVertretene = 0;

    /**[gattung-1]*/
    private int gattungen[] = { 0, 0, 0, 0, 0 };

    /**Enthält die Namen der ausgewählten Aktionäre, z.B. für die Anzeige während der Stimmabgabe*/
    private List<String> ausgewaehlteAktionaere = null;

    
    
    /**Meldungen, die derzeit nicht vertreten werden. Wird über die gesamte Session
     * behalten, und beim Login gelöscht
     */
    private List<Integer> ausblendenMeldungen = null;
  
    public void copyFrom(BlWillenserklaerungStatusNeu blWillenserklaerungStatus) {
        weitereKennungen = blWillenserklaerungStatus.weitereKennungen;
        instiKennungZugeordnet = blWillenserklaerungStatus.instiKennungZugeordnet;
        CaBug.druckeLog("instiKennungZugeordnet="+instiKennungZugeordnet, logDrucken, 10);
        nichtAngemeldeteVorhanden = blWillenserklaerungStatus.nichtAngemeldeteVorhanden;
        angemeldeteVorhanden = blWillenserklaerungStatus.angemeldeteVorhanden;
        angemeldeteNichtAusgeblendeteVorhanden = blWillenserklaerungStatus.angemeldeteNichtAusgeblendeteVorhanden;
        ausgeblendeteVorhanden = blWillenserklaerungStatus.ausgeblendeteVorhanden;
        bereitsErteiltWeisungBriefwahlAllgemeinDurchAndereVorhanden = blWillenserklaerungStatus.bereitsErteiltWeisungBriefwahlAllgemeinDurchAndereVorhanden;
        aktienInsgesamtAngemeldet = blWillenserklaerungStatus.aktienInsgesamtAngemeldet;
       
        besitzJeKennungListe = blWillenserklaerungStatus.besitzJeKennungListe;
        besitzJeKennungListeNichtPraesent = blWillenserklaerungStatus.besitzJeKennungNichtPraesentListe;
        besitzJeKennungListePraesent = blWillenserklaerungStatus.besitzJeKennungPraesentListe;
        meldungenBereitsErteiltWeisungAllgemeinDurchAndereListe=blWillenserklaerungStatus.meldungenBereitsErteiltWeisungAllgemeinDurchAndereListe;
        
        anzPraesenteVorhanden = blWillenserklaerungStatus.anzPraesenteVorhanden;
        anzPraesenteVorhandenEigene = blWillenserklaerungStatus.anzPraesenteVorhandenEigene;
        anzPraesenteVorhandenVertretene = blWillenserklaerungStatus.anzPraesenteVorhandenVertretene;
        anzNichtPraesenteVorhanden = blWillenserklaerungStatus.anzNichtPraesenteVorhanden;
        anzNichtPraesenteVorhandenEigene = blWillenserklaerungStatus.anzNichtPraesenteVorhandenEigene;
        anzNichtPraesenteVorhandenVertretene = blWillenserklaerungStatus.anzNichtPraesenteVorhandenVertretene;

        gattungen = blWillenserklaerungStatus.gattungen;
    }
    
    public void clearBeiLogin() {
        ausblendenMeldungen=null; 
        kennungIstOnlinePraesent=false;
        kennungHatWortmeldungErteilt=false;
    }
    
    /*****************Liefern des eigenen Bestands - für "einfachen" Anmeldeablauf wie Vereine,
     * Genossenschaften etc.
     */
    /**Aktienregister zum eigenen Bestand (nur für Vereine, Genossen etc.)*/
    public EclAktienregister eigenerBestandAktienregister() {
        return besitzJeKennungListe.get(0).eigenerAREintragListe.get(0).aktienregisterEintrag;
    }

    /**Meldung zum eigenen Bestand (nur für Vereine, Genossen etc.)*/
    public EclMeldung eigenerBestandMeldung() {
        if (    besitzJeKennungListe.get(0)==null ||
                besitzJeKennungListe.get(0).eigenerAREintragListe==null ||
                besitzJeKennungListe.get(0).eigenerAREintragListe.get(0).zugeordneteMeldungenListe==null ||
                besitzJeKennungListe.get(0).eigenerAREintragListe.get(0).zugeordneteMeldungenListe.size()==0
                ) {return null;}
        return besitzJeKennungListe.get(0).eigenerAREintragListe.get(0).zugeordneteMeldungenListe.get(0).eclMeldung;
    }

    /************************Standard getter und setter***************************************/
    public boolean isWeitereKennungen() {
        return weitereKennungen;
    }

    public void setWeitereKennungen(boolean weitereKennungen) {
        this.weitereKennungen = weitereKennungen;
    }

    public List<EclBesitzJeKennung> getBesitzJeKennungListe() {
        return besitzJeKennungListe;
    }

    public void setBesitzJeKennungListe(List<EclBesitzJeKennung> besitzJeKennungListe) {
        this.besitzJeKennungListe = besitzJeKennungListe;
    }

    public boolean isMehrereBestaendeVorhanden() {
        return mehrereBestaendeVorhanden;
    }

    public void setMehrereBestaendeVorhanden(boolean mehrereBestaendeVorhanden) {
        this.mehrereBestaendeVorhanden = mehrereBestaendeVorhanden;
    }

    public boolean isNichtAngemeldeteVorhanden() {
        return nichtAngemeldeteVorhanden;
    }

    public void setNichtAngemeldeteVorhanden(boolean nichtAngemeldeteVorhanden) {
        this.nichtAngemeldeteVorhanden = nichtAngemeldeteVorhanden;
    }

    public boolean isAngemeldeteVorhanden() {
        return angemeldeteVorhanden;
    }

    public void setAngemeldeteVorhanden(boolean angemeldeteVorhanden) {
        this.angemeldeteVorhanden = angemeldeteVorhanden;
    }

    public int getAnzPraesenteVorhanden() {
        return anzPraesenteVorhanden;
    }

    public void setAnzPraesenteVorhanden(int anzPraesenteVorhanden) {
        this.anzPraesenteVorhanden = anzPraesenteVorhanden;
    }

    public int getAnzPraesenteVorhandenEigene() {
        return anzPraesenteVorhandenEigene;
    }

    public void setAnzPraesenteVorhandenEigene(int anzPraesenteVorhandenEigene) {
        this.anzPraesenteVorhandenEigene = anzPraesenteVorhandenEigene;
    }

    public int getAnzPraesenteVorhandenVertretene() {
        return anzPraesenteVorhandenVertretene;
    }

    public void setAnzPraesenteVorhandenVertretene(int anzPraesenteVorhandenVertretene) {
        this.anzPraesenteVorhandenVertretene = anzPraesenteVorhandenVertretene;
    }

    public int getAnzNichtPraesenteVorhanden() {
        return anzNichtPraesenteVorhanden;
    }

    public void setAnzNichtPraesenteVorhanden(int anzNichtPraesenteVorhanden) {
        this.anzNichtPraesenteVorhanden = anzNichtPraesenteVorhanden;
    }

    public int getAnzNichtPraesenteVorhandenEigene() {
        return anzNichtPraesenteVorhandenEigene;
    }

    public void setAnzNichtPraesenteVorhandenEigene(int anzNichtPraesenteVorhandenEigene) {
        this.anzNichtPraesenteVorhandenEigene = anzNichtPraesenteVorhandenEigene;
    }

    public int getAnzNichtPraesenteVorhandenVertretene() {
        return anzNichtPraesenteVorhandenVertretene;
    }

    public void setAnzNichtPraesenteVorhandenVertretene(int anzNichtPraesenteVorhandenVertretene) {
        this.anzNichtPraesenteVorhandenVertretene = anzNichtPraesenteVorhandenVertretene;
    }

    public List<EclBesitzJeKennung> getBesitzJeKennungListeNichtPraesent() {
        return besitzJeKennungListeNichtPraesent;
    }

    public void setBesitzJeKennungListeNichtPraesent(List<EclBesitzJeKennung> besitzJeKennungListeNichtPraesent) {
        this.besitzJeKennungListeNichtPraesent = besitzJeKennungListeNichtPraesent;
    }

    public List<EclBesitzJeKennung> getBesitzJeKennungListePraesent() {
        return besitzJeKennungListePraesent;
    }

    public void setBesitzJeKennungListePraesent(List<EclBesitzJeKennung> besitzJeKennungListePraesent) {
        this.besitzJeKennungListePraesent = besitzJeKennungListePraesent;
    }

    public int[] getGattungen() {
        return gattungen;
    }

    public void setGattungen(int[] gattungen) {
        this.gattungen = gattungen;
    }

    public List<String> getAusgewaehlteAktionaere() {
        return ausgewaehlteAktionaere;
    }

    public void setAusgewaehlteAktionaere(List<String> ausgewaehlteAktionaere) {
        this.ausgewaehlteAktionaere = ausgewaehlteAktionaere;
    }

    public boolean isAngemeldeteNichtAusgeblendeteVorhanden() {
        return angemeldeteNichtAusgeblendeteVorhanden;
    }

    public void setAngemeldeteNichtAusgeblendeteVorhanden(boolean angemeldeteNichtAusgeblendeteVorhanden) {
        this.angemeldeteNichtAusgeblendeteVorhanden = angemeldeteNichtAusgeblendeteVorhanden;
    }

    public boolean isAusgeblendeteVorhanden() {
        return ausgeblendeteVorhanden;
    }

    public void setAusgeblendeteVorhanden(boolean ausgeblendeteVorhanden) {
        this.ausgeblendeteVorhanden = ausgeblendeteVorhanden;
    }

    public boolean isBereitsErteiltWeisungBriefwahlAllgemeinDurchAndereVorhanden() {
        return bereitsErteiltWeisungBriefwahlAllgemeinDurchAndereVorhanden;
    }

    public void setBereitsErteiltWeisungBriefwahlAllgemeinDurchAndereVorhanden(boolean bereitsErteiltWeisungBriefwahlAllgemeinDurchAndereVorhanden) {
        this.bereitsErteiltWeisungBriefwahlAllgemeinDurchAndereVorhanden = bereitsErteiltWeisungBriefwahlAllgemeinDurchAndereVorhanden;
    }

    public List<EclZugeordneteMeldungNeu> getMeldungenBereitsErteiltWeisungAllgemeinDurchAndereListe() {
        return meldungenBereitsErteiltWeisungAllgemeinDurchAndereListe;
    }

    public void setMeldungenBereitsErteiltWeisungAllgemeinDurchAndereListe(List<EclZugeordneteMeldungNeu> meldungenBereitsErteiltWeisungAllgemeinDurchAndereListe) {
        this.meldungenBereitsErteiltWeisungAllgemeinDurchAndereListe = meldungenBereitsErteiltWeisungAllgemeinDurchAndereListe;
    }

    public long getAktienInsgesamtAngemeldet() {
        return aktienInsgesamtAngemeldet;
    }

    public void setAktienInsgesamtAngemeldet(long aktienInsgesamtAngemeldet) {
        this.aktienInsgesamtAngemeldet = aktienInsgesamtAngemeldet;
    }

    public List<Integer> getAusblendenMeldungen() {
        return ausblendenMeldungen;
    }

    public void setAusblendenMeldungen(List<Integer> ausblendenMeldungen) {
        this.ausblendenMeldungen = ausblendenMeldungen;
    }

    public boolean isInstiKennungZugeordnet() {
        return instiKennungZugeordnet;
    }

    public void setInstiKennungZugeordnet(boolean instiKennungZugeordnet) {
        this.instiKennungZugeordnet = instiKennungZugeordnet;
    }

    public boolean isKennungIstOnlinePraesent() {
        return kennungIstOnlinePraesent;
    }

    public void setKennungIstOnlinePraesent(boolean kennungIstOnlinePraesent) {
        this.kennungIstOnlinePraesent = kennungIstOnlinePraesent;
    }

    public boolean isKennungHatWortmeldungErteilt() {
        return kennungHatWortmeldungErteilt;
    }

    public void setKennungHatWortmeldungErteilt(boolean kennungHatWortmeldungErteilt) {
        this.kennungHatWortmeldungErteilt = kennungHatWortmeldungErteilt;
    }

}
