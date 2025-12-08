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

/**Zuordnung einer Meldung zu einer PersonNatJur
 * 
 * Verwendung z.B. um alle Meldungen in einem Array zu speichern, die eine PersonNatJur (und aller dieser zugeordneten
 * PersonNatJur) in irgendeiner Form vertreten kann (z.B. als Bevollmächtigter, aber auch als Aktionär selbst).
 */

@Deprecated
public class EclMeldungPersonNatJur {

    private int identPersonNatJur = 0;

    /**Ident der Nat-Jur-Person, die der Meldung zugeordnet ist*/
    public int getIdentPersonNatJur() {
        return identPersonNatJur;
    }

    /**Ident der Nat-Jur-Person, die der Meldung zugeordnet ist*/
    public void setIdentPersonNatJur(int identPersonNatJur) {
        this.identPersonNatJur = identPersonNatJur;
    }

    private int identMeldung = 0;

    public int getIdentMeldung() {
        return identMeldung;
    }

    public void setIdentMeldung(int identMeldung) {
        this.identMeldung = identMeldung;
    }

    private String nameVornameMeldung = "";

    public String getNameVornameMeldung() {
        return nameVornameMeldung;
    }

    public void setNameVornameMeldung(String nameVornameMeldung) {
        this.nameVornameMeldung = nameVornameMeldung;
    }

    private String ortMeldung = "";

    public String getOrtMeldung() {
        return ortMeldung;
    }

    public void setOrtMeldung(String ortMeldung) {
        this.ortMeldung = ortMeldung;
    }

    private int aktienMeldung = 0;

    public int getAktienMeldung() {
        return aktienMeldung;
    }

    public void setAktienMeldung(int aktienMeldung) {
        this.aktienMeldung = aktienMeldung;
    }

    private boolean istImBesitz = false;

    /**=true => die Aktien der Meldung sind im Besitz der zugeordneten NatJur-Person, d.h. 
     * die NatJur-Person ist entweder der Aktionär selbst, oder die Meldung ist ein Fremdbesitz
     * (oder Vollmachtsbesitz).
     * Technisch gesehen: die in diesem Array zugeordnete NatJur-Person ist in diesem Fall die direkt der Meldung
     * zugeordneten NatJurPerson.
     */
    public boolean isIstImBesitz() {
        return istImBesitz;
    }

    /**=true => die Aktien der Meldung sind im Besitz der zugeordneten NatJur-Person, d.h. 
     * die NatJur-Person ist entweder der Aktionär selbst, oder die Meldung ist ein Fremdbesitz
     * (oder Vollmachtsbesitz).
     * Technisch gesehen: identPersonNatJur ist in diesem Fall die direkt der Meldung
     * zugeordneten NatJurPerson.
     */
    public void setIstImBesitz(boolean istImBesitz) {
        this.istImBesitz = istImBesitz;
    }

    private boolean vollmachtOderWeisungErteilt = false;

    /**=true => von der identPersonNatJur wurde zu dieser Eintrittskarte eine weitere Willenserklärung abgegeben,
     * und zwar Vollmacht an Dritte, oder an KI/AV, oder an SRV, oder Briefwahl 
     */
    public boolean isVollmachtOderWeisungErteilt() {
        return vollmachtOderWeisungErteilt;
    }

    /**=true => von der identPersonNatJur wurde zu dieser Eintrittskarte eine weitere Willenserklärung abgegeben,
     * und zwar Vollmacht an Dritte, oder an KI/AV, oder an SRV, oder Briefwahl 
     */
    public void setVollmachtOderWeisungErteilt(boolean vollmachtOderWeisungErteilt) {
        this.vollmachtOderWeisungErteilt = vollmachtOderWeisungErteilt;
    }

    private boolean bearbeiten = false;

    /**Dient dazu, um diese Zuordnung für eine spätere Bearbeitung auszuwählen*/
    public boolean isBearbeiten() {
        return bearbeiten;
    }

    public void setBearbeiten(boolean bearbeiten) {
        this.bearbeiten = bearbeiten;
    }

}
