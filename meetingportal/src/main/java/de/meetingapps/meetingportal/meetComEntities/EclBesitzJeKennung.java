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
import java.util.LinkedList;
import java.util.List;


public class EclBesitzJeKennung implements Serializable {
    private static final long serialVersionUID = 6145031485007518617L;

     /**Zugehörige "Login"-Daten / Ausgangsdaten für den Besitz dieser Kennung*/
    public String kennungIntern = "";
    
    public EclAktienregister eclAktienregister = null; //new EclAktienregister();
    public EclPersonenNatJur eclPersonenNatJur = null; //new EclPersonenNatJur();

    /**siehe eclLoginDaten*/
    public int kennungArt=0;
    public long berechtigungPortal=0;

    
    /**Wird hier separat (redundant) gespeichert.
     * Wird gefüllt durch leseMeldungenZuAktienregister oder leseMeldungenEigeneGastkartenZuPersonNatJur -
     * je nachdem welche Art von Kennung das ist (nur eines von beiden möglich :-) )
     */
    public int personNatJurIdent = 0;

    /**Wird hier separat - teilweise redundant - gespeichert. Wird ermittelt aus den
     * zugeordneten Gästekarten (nur diese können eine InstiIdent enthalten) 
     */
    public int instiIdent = 0;

    /**diese EclBesitzJeKennung ist direkt der login-Kennung zugeordnet*/
    public boolean besitzIstUrsprungVonKennung = true;

    /**Je nach Parameterstellung muß eine Zuordnung erst verifiziert werden, bevor sie 
     * für Präsenz-Zugänge von der jeweiligen Kennung aus bearbeitet werden kann. 
     * Wenn false, dann werden die zugeordneten
     * Besitze zwar angezeigt, können aber nicht als Zugang gebucht werden.
     * 
     * Hintergrund: 
     * > Wenn die Strategie gefahren wird "Vor einem PC sitzt nur eine Person", 
     * dann sind Zuordnung von Kennungen anderer Personen für die Präsenz nicht wirksam (muß über Vollmacht 
     * erfolgen). Dies kann aber nur manuell verifiziert werden.
     * > Alternativ ist die Strategie "Vor einem PC können auch mehrere Personen sitzen", dann sind Zuordnungen
     * von Kennungen anderer Personen möglich und sinnvoll, da technisch sonst nicht anders machbar.
     */
    public boolean zuordnungIstFuerPraesenzVerifiziert = true;

    /**0=in Arbeit, 1=genehmigt, 2=abgelehnt*/
    public int zuordnungFuerPraesenzStatus = 1;

    /**++++++++++++++++++++++Kennungsdaten+++++++++++++++++++++++++++++++++++++++++++ 
     * Werden (falls mehrere Kennungen zusammengefaßt wurden)
     * als Trenndaten zwischen den Kennungen angezeigt.
     * Wert gemäßg KonstLoginKennungArt: 1=Aktienregister, 2=sonstige Person
     * */
    public int kennungsArt = 0;
    public String kennungFuerAnzeige = "";

    /**++++++++++++++++++++Eigener AREintrag dieser Kennung+++++++++++++++++++
     * Kann hier nur ein Eintrag sein. Aus Kompatibilitäsgründen zu InstiAREInträge jedoch auch hier
     * Liste.
     * Hierin können auch Gästekarten sein, die von diesem Aktienregistereintrag aus ausgestellt wurden*/
    /*LfdNummer jeweils in EclBesitzAREintrag*/
    public boolean eigenerAREintragVorhanden = false;

    public List<EclBesitzAREintrag> eigenerAREintragListe = new LinkedList<EclBesitzAREintrag>();

    /**++++++++++++++++++++für diese Kennung ausgestellte Gastkarten+++++++++++
     * Ergebnis: alle Anmeldungen als Gast, die auf den Teilnehmer erfolgt sind.
     * Können auch Gruppenkarten-Ausstellungen sein.
     * 
     * Hinweis: falls eine Gastkarte storniert wurde, ist hier ein Eintrag OHNE Zutrittsident
     * enthalten (da ja der Gast möglicherweise noch andere Aktivitäten durchgeführt hat, bzw. auch
     * Vollmachten erhalten hat).
     * Für Gästeanmeldungen kann KEINE weitere Willenserklärung abgegeben werden, insbesondere KEINE neue
     * ZutrittsIdent ausgestellt werden!
     */
    public boolean gastkartenVorhanden = false;
    public List<EclZugeordneteMeldungNeu> zugeordneteMeldungenEigeneGastkartenListe = new LinkedList<EclZugeordneteMeldungNeu>();

    /**++++++++++++++++++++++++Erhaltene Vollmachten dieser Kennung++++++++++++
     * Ergebnis: alle Meldungen, bei denen Vollmachten auf den Teilnehmer ausgestellt sind
     * (direkt oder indirekt).
     * Hierzu sind weitere Willenserklärungen möglicherweise vorhanden, bzw. können diese geändert
     * oder storniert werden (ähnlich wie bei eigenen Meldungen).
     */
    public boolean erhalteneVollmachtenVorhanden = false;
    public List<EclZugeordneteMeldungNeu> zugeordneteMeldungenBevollmaechtigtListe = new LinkedList<EclZugeordneteMeldungNeu>();

    /**++++++++++++++++++++++++Vollmachten, die mit gesetzlicher Vollmacht "mitgeerbt" wurden++++++++++++
     * Nur für ku178-Spezialablauf
     * Hierzu sind weitere Willenserklärungen möglicherweise vorhanden, bzw. können diese geändert
     * oder storniert werden (ähnlich wie bei eigenen Meldungen). Allerdings werden diese Willenserklärungen mit der
     * PersonIdent des gesetzlich vertretenen ausgeführt
     */
    public boolean erhalteneVollmachtenMitGesetzlichVorhanden = false;
    public List<EclZugeordneteMeldungNeu> zugeordneteMeldungenBevollmaechtigtMitGesetzlichListe = new LinkedList<EclZugeordneteMeldungNeu>();

    /**++++++++++++++Dieser Kennung als Insti zugeordnete AREinträge+++++++++++++++
     * Hierin können auch Gästekarten sein, die von dem jeweiligen Aktienregistereintrag aus
     * ausgestellt wurden*/
    /*LfdNummer jeweils in EclBesitzAREintrag*/
    public boolean instiAREintraegeVorhanden = false;

    public List<EclBesitzAREintrag> instiAREintraegeListe = new LinkedList<EclBesitzAREintrag>();

    /**++++++++++++++Dieser Kennung als Insti zugeordnete Meldungen+++++++++++++++*/
    public boolean instiMeldungenVorhanden = false;
    public List<EclZugeordneteMeldungNeu> zugeordneteMeldungenInstiListe = new LinkedList<EclZugeordneteMeldungNeu>();

    /*************************Standard getter und setter*****************************/
    public boolean isEigenerAREintragVorhanden() {
        return eigenerAREintragVorhanden;
    }

    public void setEigenerAREintragVorhanden(boolean eigenerAREintragVorhanden) {
        this.eigenerAREintragVorhanden = eigenerAREintragVorhanden;
    }

    public List<EclBesitzAREintrag> getEigenerAREintragListe() {
        return eigenerAREintragListe;
    }

    public void setEigenerAREintragListe(List<EclBesitzAREintrag> eigenerAREintragListe) {
        this.eigenerAREintragListe = eigenerAREintragListe;
    }

    public boolean isErhalteneVollmachtenVorhanden() {
        return erhalteneVollmachtenVorhanden;
    }

    public void setErhalteneVollmachtenVorhanden(boolean erhalteneVollmachtenVorhanden) {
        this.erhalteneVollmachtenVorhanden = erhalteneVollmachtenVorhanden;
    }

    public boolean isGastkartenVorhanden() {
        return gastkartenVorhanden;
    }

    public void setGastkartenVorhanden(boolean gastkartenVorhanden) {
        this.gastkartenVorhanden = gastkartenVorhanden;
    }

    public boolean isInstiAREintraegeVorhanden() {
        return instiAREintraegeVorhanden;
    }

    public void setInstiAREintraegeVorhanden(boolean instiAREintraegeVorhanden) {
        this.instiAREintraegeVorhanden = instiAREintraegeVorhanden;
    }

    public List<EclBesitzAREintrag> getInstiAREintraegeListe() {
        return instiAREintraegeListe;
    }

    public void setInstiAREintraegeListe(List<EclBesitzAREintrag> instiAREintraegeListe) {
        this.instiAREintraegeListe = instiAREintraegeListe;
    }

    public boolean isInstiMeldungenVorhanden() {
        return instiMeldungenVorhanden;
    }

    public void setInstiMeldungenVorhanden(boolean instiMeldungenVorhanden) {
        this.instiMeldungenVorhanden = instiMeldungenVorhanden;
    }

    public boolean isZuordnungIstFuerPraesenzVerifiziert() {
        return zuordnungIstFuerPraesenzVerifiziert;
    }

    public void setZuordnungIstFuerPraesenzVerifiziert(boolean zuordnungIstFuerPraesenzVerifiziert) {
        this.zuordnungIstFuerPraesenzVerifiziert = zuordnungIstFuerPraesenzVerifiziert;
    }

    public int getKennungsArt() {
        return kennungsArt;
    }

    public void setKennungsArt(int kennungsArt) {
        this.kennungsArt = kennungsArt;
    }

    public String getKennungIntern() {
        return kennungIntern;
    }

    public void setKennungIntern(String kennungIntern) {
        this.kennungIntern = kennungIntern;
    }


    public EclAktienregister getEclAktienregister() {
        return eclAktienregister;
    }

    public void setEclAktienregister(EclAktienregister eclAktienregister) {
        this.eclAktienregister = eclAktienregister;
    }

    public EclPersonenNatJur getEclPersonenNatJur() {
        return eclPersonenNatJur;
    }

    public void setEclPersonenNatJur(EclPersonenNatJur eclPersonenNatJur) {
        this.eclPersonenNatJur = eclPersonenNatJur;
    }

    public int getPersonNatJurIdent() {
        return personNatJurIdent;
    }

    public void setPersonNatJurIdent(int personNatJurIdent) {
        this.personNatJurIdent = personNatJurIdent;
    }

    public int getInstiIdent() {
        return instiIdent;
    }

    public void setInstiIdent(int instiIdent) {
        this.instiIdent = instiIdent;
    }

    public String getKennungFuerAnzeige() {
        return kennungFuerAnzeige;
    }

    public void setKennungFuerAnzeige(String kennungFuerAnzeige) {
        this.kennungFuerAnzeige = kennungFuerAnzeige;
    }

    public List<EclZugeordneteMeldungNeu> getZugeordneteMeldungenEigeneGastkartenListe() {
        return zugeordneteMeldungenEigeneGastkartenListe;
    }

    public void setZugeordneteMeldungenEigeneGastkartenListe(
            List<EclZugeordneteMeldungNeu> zugeordneteMeldungenEigeneGastkartenListe) {
        this.zugeordneteMeldungenEigeneGastkartenListe = zugeordneteMeldungenEigeneGastkartenListe;
    }

    public List<EclZugeordneteMeldungNeu> getZugeordneteMeldungenBevollmaechtigtListe() {
        return zugeordneteMeldungenBevollmaechtigtListe;
    }

    public void setZugeordneteMeldungenBevollmaechtigtListe(
            List<EclZugeordneteMeldungNeu> zugeordneteMeldungenBevollmaechtigtListe) {
        this.zugeordneteMeldungenBevollmaechtigtListe = zugeordneteMeldungenBevollmaechtigtListe;
    }

    public List<EclZugeordneteMeldungNeu> getZugeordneteMeldungenInstiListe() {
        return zugeordneteMeldungenInstiListe;
    }

    public void setZugeordneteMeldungenInstiListe(List<EclZugeordneteMeldungNeu> zugeordneteMeldungenInstiListe) {
        this.zugeordneteMeldungenInstiListe = zugeordneteMeldungenInstiListe;
    }

    public int getZuordnungFuerPraesenzStatus() {
        return zuordnungFuerPraesenzStatus;
    }

    public void setZuordnungFuerPraesenzStatus(int zuordnungFuerPraesenzStatus) {
        this.zuordnungFuerPraesenzStatus = zuordnungFuerPraesenzStatus;
    }

    public boolean isErhalteneVollmachtenMitGesetzlichVorhanden() {
        return erhalteneVollmachtenMitGesetzlichVorhanden;
    }

    public void setErhalteneVollmachtenMitGesetzlichVorhanden(boolean erhalteneVollmachtenMitGesetzlichVorhanden) {
        this.erhalteneVollmachtenMitGesetzlichVorhanden = erhalteneVollmachtenMitGesetzlichVorhanden;
    }

    public List<EclZugeordneteMeldungNeu> getZugeordneteMeldungenBevollmaechtigtMitGesetzlichListe() {
        return zugeordneteMeldungenBevollmaechtigtMitGesetzlichListe;
    }

    public void setZugeordneteMeldungenBevollmaechtigtMitGesetzlichListe(
            List<EclZugeordneteMeldungNeu> zugeordneteMeldungenBevollmaechtigtMitGesetzlichListe) {
        this.zugeordneteMeldungenBevollmaechtigtMitGesetzlichListe = zugeordneteMeldungenBevollmaechtigtMitGesetzlichListe;
    }

    public boolean isBesitzIstUrsprungVonKennung() {
        return besitzIstUrsprungVonKennung;
    }

    public void setBesitzIstUrsprungVonKennung(boolean besitzIstUrsprungVonKennung) {
        this.besitzIstUrsprungVonKennung = besitzIstUrsprungVonKennung;
    }

}
