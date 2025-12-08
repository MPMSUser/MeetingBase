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

import de.meetingapps.meetingportal.meetComBl.BlWillenserklaerungStatus;
import de.meetingapps.meetingportal.meetComEntities.EclZugeordneteMeldung;
import de.meetingapps.meetingportal.meetComHVParam.HVParam;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@SessionScoped
@Named
@Deprecated
public class EclZugeordneteMeldungListeM implements Serializable {
    private static final long serialVersionUID = -3591435313847180400L;

    /**Sonderablauf für nurRawLiveAbstimmung:
     * Es wird nur zugeordneteMeldungenEigeneAktienListeM gefüllt, ohne Willenserklärungen.
     */

    @Inject
    EclParamM eclParamM;

    /**Ergebnis: alle Anmeldungen mit eigenen Aktien, die auf den Teilnehmer erfolgt sind.
     * Achtung: "eigene" heißt in diesem Fall auch Anmeldungen auf Fremdbesitz.
     * 
     * Hinweis: bei der Suche über den Aktienregistereintrag (also für die Standardportalsicht "Aktionär" sozusagen)
     * sind hierin auch die Gästekarten enthalten, die der Aktionär angefordert hat
     * (=Anmeldung von diesem Aktionär ausgehend, die ja nicht auf ihn ausgehen)
     */
    private List<EclZugeordneteMeldungM> zugeordneteMeldungenEigeneAktienListeM;
    private int anzZugeordneteMeldungenEigeneAktienListeM;
    private int gastKartenGemeldet = 0;

    /**Ergebnis: alle Anmeldungen als Gast, die auf den Teilnehmer erfolgt sind.
     * Können auch Gruppenkarten-Ausstellungen sein.
     * 
     * Hinweis: falls eine Gastkarte storniert wurde, ist hier ein Eintrag OHNE Zutrittsident
     * enthalten (da ja der Gast möglicherweise noch andere Aktivitäten durchgeführt hat, bzw. auch
     * Vollmachten erhalten hat).
     * Für Gästeanmeldungen kann KEINE weitere Willenserklärung abgegeben werden, insbesondere KEINE neue
     * ZutrittsIdent ausgestellt werden!
     */
    private List<EclZugeordneteMeldungM> zugeordneteMeldungenEigeneGastkartenListeM;
    private int anzZugeordneteMeldungenEigeneGastkartenListeM;

    /**Ergebnis: alle Meldungen, bei denen Vollmachten auf den Teilnehmer ausgestellt sind
     * (direkt oder indirekt).
     * Hierzu sind weitere Willenserklärungen möglicherweise vorhanden, bzw. können diese geändert
     * oder storniert werden (ähnlich wie bei eigenen Meldungen).
     */
    private List<EclZugeordneteMeldungM> zugeordneteMeldungenBevollmaechtigtListeM;
    private int anzZugeordneteMeldungenBevollmaechtigtListeM;

    /**Ergebnis der Analyse, wozu die verwendet werden: wahrscheinlich nur für
     * ku178 Sonderaktion
     */
    private boolean briefwahlVorhanden = false;
    private boolean srvVorhanden = false;

    /**
     * Gibt an, ob in allen zugeordneten / eingelesenen Sätzen auch ein präsenter vorhanden ist.
     * 
     * Wird beim Einlesen des Status gefüllt, aber (insbesondere bei nurRawLiveAbstimmung==1) auch im Speicher
     * gefüllt.
     * Dient als Render-Anzeige für die Auswahlmaske.
     * 
     * Achtung, bei nurRawLiveAbstimmung==1 immer false, da in diesem Fall der Präsenzstatus nicht
     * in die Tables zurückgespeichert wird, sondern nur im Speicher gehalten wird.
     */
    private boolean praesenteVorhanden = false;
    /**
     * Gibt an, ob in allen zugeordneten / eingelesenen Sätzen auch ein präsenter vorhanden ist.
     * 
     * Wird beim Einlesen des Status gefüllt, aber (insbesondere bei nurRawLiveAbstimmung==1) auch im Speicher
     * gefüllt.
     * Dient als Render-Anzeige für die Auswahlmaske.
     * 
     * Achtung, bei nurRawLiveAbstimmung==1 immer false, da in diesem Fall der Präsenzstatus nicht
     * in die Tables zurückgespeichert wird, sondern nur im Speicher gehalten wird.
     */
    private boolean nichtPraesenteVorhanden = false;

    public EclZugeordneteMeldungListeM() {
        zugeordneteMeldungenEigeneAktienListeM = null;
        zugeordneteMeldungenEigeneGastkartenListeM = null;
        zugeordneteMeldungenBevollmaechtigtListeM = null;
    }

    public void copyFrom(BlWillenserklaerungStatus pBlWillenserklaerungStatus, boolean pMitStorno) {
        zugeordneteMeldungenEigeneAktienCopyFrom(pBlWillenserklaerungStatus.zugeordneteMeldungenEigeneAktienArray,
                pMitStorno);
        setGastKartenGemeldet(pBlWillenserklaerungStatus.gastKartenGemeldetEigeneAktien);
        zugeordneteMeldungenEigeneGastkartenCopyFrom(
                pBlWillenserklaerungStatus.zugeordneteMeldungenEigeneGastkartenArray, pMitStorno);
        zugeordneteMeldungenBevollmaechtigtCopyFrom(pBlWillenserklaerungStatus.zugeordneteMeldungenBevollmaechtigtArray,
                pMitStorno);
        setBriefwahlVorhanden(pBlWillenserklaerungStatus.briefwahlVorhanden);
        setSrvVorhanden(pBlWillenserklaerungStatus.srvVorhanden);
        praesenteVorhanden = pBlWillenserklaerungStatus.praesenteVorhanden;
        nichtPraesenteVorhanden = pBlWillenserklaerungStatus.nichtPraesenteVorhanden;

    }

    public void copyFromMMitStorno(EclZugeordneteMeldungListeM pEclZugeordneteMeldungListeM) {
        copyFromM(pEclZugeordneteMeldungListeM, true);
    }

    public void copyFromMOhneStorno(EclZugeordneteMeldungListeM pEclZugeordneteMeldungListeM) {
        copyFromM(pEclZugeordneteMeldungListeM, false);
    }

    public void copyFromM(EclZugeordneteMeldungListeM pEclZugeordneteMeldungListeM, boolean komplett) {
        int i;

        this.zugeordneteMeldungenEigeneAktienListeM = new LinkedList<>();
        for (i = 0; i < pEclZugeordneteMeldungListeM.zugeordneteMeldungenEigeneAktienListeM.size(); i++) {
            EclZugeordneteMeldungM lZugeordneteMeldungM = new EclZugeordneteMeldungM();
            lZugeordneteMeldungM.copyFromM(pEclZugeordneteMeldungListeM.zugeordneteMeldungenEigeneAktienListeM.get(i),
                    komplett);
            this.zugeordneteMeldungenEigeneAktienListeM.add(lZugeordneteMeldungM);
        }
        this.anzZugeordneteMeldungenEigeneAktienListeM = pEclZugeordneteMeldungListeM.anzZugeordneteMeldungenEigeneAktienListeM;
        this.gastKartenGemeldet = pEclZugeordneteMeldungListeM.gastKartenGemeldet;

        this.zugeordneteMeldungenEigeneGastkartenListeM = new LinkedList<>();
        for (i = 0; i < pEclZugeordneteMeldungListeM.zugeordneteMeldungenEigeneGastkartenListeM.size(); i++) {
            EclZugeordneteMeldungM lZugeordneteMeldungM = new EclZugeordneteMeldungM();
            lZugeordneteMeldungM.copyFromM(
                    pEclZugeordneteMeldungListeM.zugeordneteMeldungenEigeneGastkartenListeM.get(i), komplett);
            this.zugeordneteMeldungenEigeneGastkartenListeM.add(lZugeordneteMeldungM);
        }
        this.anzZugeordneteMeldungenEigeneGastkartenListeM = pEclZugeordneteMeldungListeM.anzZugeordneteMeldungenEigeneGastkartenListeM;

        this.zugeordneteMeldungenBevollmaechtigtListeM = new LinkedList<>();
        for (i = 0; i < pEclZugeordneteMeldungListeM.zugeordneteMeldungenBevollmaechtigtListeM.size(); i++) {
            EclZugeordneteMeldungM lZugeordneteMeldungM = new EclZugeordneteMeldungM();
            lZugeordneteMeldungM
                    .copyFromM(pEclZugeordneteMeldungListeM.zugeordneteMeldungenBevollmaechtigtListeM.get(i), komplett);
            this.zugeordneteMeldungenBevollmaechtigtListeM.add(lZugeordneteMeldungM);
        }
        this.anzZugeordneteMeldungenBevollmaechtigtListeM = pEclZugeordneteMeldungListeM.anzZugeordneteMeldungenBevollmaechtigtListeM;
        this.briefwahlVorhanden = pEclZugeordneteMeldungListeM.briefwahlVorhanden;
        this.srvVorhanden = pEclZugeordneteMeldungListeM.srvVorhanden;
    }

    public void copyToMMitStorno(EclZugeordneteMeldungListeM pEclZugeordneteMeldungListeM) {
        copyToM(pEclZugeordneteMeldungListeM, true);
    }

    public void copyToMOhneStorno(EclZugeordneteMeldungListeM pEclZugeordneteMeldungListeM) {
        copyToM(pEclZugeordneteMeldungListeM, false);
    }

    public void copyToM(EclZugeordneteMeldungListeM pEclZugeordneteMeldungListeM, boolean komplett) {
        int i;

        pEclZugeordneteMeldungListeM.zugeordneteMeldungenEigeneAktienListeM = new LinkedList<>();
        for (i = 0; i < this.zugeordneteMeldungenEigeneAktienListeM.size(); i++) {
            EclZugeordneteMeldungM lZugeordneteMeldungM = new EclZugeordneteMeldungM();
            lZugeordneteMeldungM.copyFromM(this.zugeordneteMeldungenEigeneAktienListeM.get(i), komplett);
            pEclZugeordneteMeldungListeM.zugeordneteMeldungenEigeneAktienListeM.add(lZugeordneteMeldungM);
        }
        pEclZugeordneteMeldungListeM.anzZugeordneteMeldungenEigeneAktienListeM = this.anzZugeordneteMeldungenEigeneAktienListeM;
        pEclZugeordneteMeldungListeM.gastKartenGemeldet = this.gastKartenGemeldet;

        pEclZugeordneteMeldungListeM.zugeordneteMeldungenEigeneGastkartenListeM = new LinkedList<>();
        for (i = 0; i < this.zugeordneteMeldungenEigeneGastkartenListeM.size(); i++) {
            EclZugeordneteMeldungM lZugeordneteMeldungM = new EclZugeordneteMeldungM();
            lZugeordneteMeldungM.copyFromM(this.zugeordneteMeldungenEigeneGastkartenListeM.get(i), komplett);
            pEclZugeordneteMeldungListeM.zugeordneteMeldungenEigeneGastkartenListeM.add(lZugeordneteMeldungM);
        }
        pEclZugeordneteMeldungListeM.anzZugeordneteMeldungenEigeneGastkartenListeM = this.anzZugeordneteMeldungenEigeneGastkartenListeM;

        pEclZugeordneteMeldungListeM.zugeordneteMeldungenBevollmaechtigtListeM = new LinkedList<>();
        for (i = 0; i < this.zugeordneteMeldungenBevollmaechtigtListeM.size(); i++) {
            EclZugeordneteMeldungM lZugeordneteMeldungM = new EclZugeordneteMeldungM();
            lZugeordneteMeldungM.copyFromM(this.zugeordneteMeldungenBevollmaechtigtListeM.get(i), komplett);
            pEclZugeordneteMeldungListeM.zugeordneteMeldungenBevollmaechtigtListeM.add(lZugeordneteMeldungM);
        }
        pEclZugeordneteMeldungListeM.anzZugeordneteMeldungenBevollmaechtigtListeM = this.anzZugeordneteMeldungenBevollmaechtigtListeM;
        pEclZugeordneteMeldungListeM.briefwahlVorhanden = this.briefwahlVorhanden;
        pEclZugeordneteMeldungListeM.srvVorhanden = this.srvVorhanden;

    }

    public void zugeordneteMeldungenEigeneAktienCopyFromOhneStorno(EclZugeordneteMeldung[] pListe) {
        zugeordneteMeldungenEigeneAktienCopyFrom(pListe, false);
    }

    public void zugeordneteMeldungenEigeneAktienCopyFromMitStorno(EclZugeordneteMeldung[] pListe) {
        zugeordneteMeldungenEigeneAktienCopyFrom(pListe, true);
    }

    public void zugeordneteMeldungenEigeneAktienCopyFrom(EclZugeordneteMeldung[] pListe, boolean komplett) {
        zugeordneteMeldungenEigeneAktienCopyFromOhneInject(pListe, komplett, eclParamM.getParam());
    }

    /**Muß anstelle zugeordneteMeldungenEigeneAktienCopyFrom verwendet werden, wenn diese Klasse nicht als 
     * ManagedBean, sondern als einzelnes Objekt verwendet wird
     */
    public void zugeordneteMeldungenEigeneAktienCopyFromOhneInject(EclZugeordneteMeldung[] pListe, boolean komplett,
            HVParam hvParam) {
        zugeordneteMeldungenEigeneAktienListeM = new LinkedList<>();
        anzZugeordneteMeldungenEigeneAktienListeM = 0;
        if (pListe != null && pListe.length != 0) {
            for (int i = 0; i < pListe.length; i++) {
                EclZugeordneteMeldungM lZugeordneteMeldungM = new EclZugeordneteMeldungM();
                lZugeordneteMeldungM.copyFrom(pListe[i], komplett, hvParam);
                zugeordneteMeldungenEigeneAktienListeM.add(lZugeordneteMeldungM);
            }
            anzZugeordneteMeldungenEigeneAktienListeM = zugeordneteMeldungenEigeneAktienListeM.size();
        }
    }

    public void zugeordneteMeldungenEigeneGastkartenCopyFromOhneStorno(EclZugeordneteMeldung[] pListe) {
        zugeordneteMeldungenEigeneGastkartenCopyFrom(pListe, false);
    }

    public void zugeordneteMeldungenEigeneGastkartenCopyFromMitStorno(EclZugeordneteMeldung[] pListe) {
        zugeordneteMeldungenEigeneGastkartenCopyFrom(pListe, true);
    }

    public void zugeordneteMeldungenEigeneGastkartenCopyFrom(EclZugeordneteMeldung[] pListe, boolean komplett) {
        int i;
        zugeordneteMeldungenEigeneGastkartenListeM = new LinkedList<>();
        anzZugeordneteMeldungenEigeneGastkartenListeM = 0;
        if (pListe != null && pListe.length != 0) {
            for (i = 0; i < pListe.length; i++) {
                EclZugeordneteMeldungM lZugeordneteMeldungM = new EclZugeordneteMeldungM();
                lZugeordneteMeldungM.copyFrom(pListe[i], komplett, eclParamM.getParam());
                zugeordneteMeldungenEigeneGastkartenListeM.add(lZugeordneteMeldungM);
            }
            anzZugeordneteMeldungenEigeneGastkartenListeM = zugeordneteMeldungenEigeneGastkartenListeM.size();
        }

    }

    public void zugeordneteMeldungenBevollmaechtigtCopyFromOhneStorno(EclZugeordneteMeldung[] pListe) {
        zugeordneteMeldungenBevollmaechtigtCopyFrom(pListe, false);
    }

    public void zugeordneteMeldungenBevollmaechtigtCopyFromMitStorno(EclZugeordneteMeldung[] pListe) {
        zugeordneteMeldungenBevollmaechtigtCopyFrom(pListe, true);
    }

    public void zugeordneteMeldungenBevollmaechtigtCopyFrom(EclZugeordneteMeldung[] pListe, boolean komplett) {
        int i;
        zugeordneteMeldungenBevollmaechtigtListeM = new LinkedList<>();
        anzZugeordneteMeldungenBevollmaechtigtListeM = 0;
        if (pListe != null && pListe.length != 0) {
            for (i = 0; i < pListe.length; i++) {
                EclZugeordneteMeldungM lZugeordneteMeldungM = new EclZugeordneteMeldungM();
                lZugeordneteMeldungM.copyFrom(pListe[i], komplett, eclParamM.getParam());
                zugeordneteMeldungenBevollmaechtigtListeM.add(lZugeordneteMeldungM);
            }
            anzZugeordneteMeldungenBevollmaechtigtListeM = zugeordneteMeldungenBevollmaechtigtListeM.size();
        }

    }

    public int getGastKartenGemeldet() {
        return gastKartenGemeldet;
    }

    public void setGastKartenGemeldet(int gastKartenGemeldet) {
        this.gastKartenGemeldet = gastKartenGemeldet;
    }

    public List<EclZugeordneteMeldungM> getZugeordneteMeldungenEigeneAktienListeM() {
        return zugeordneteMeldungenEigeneAktienListeM;
    }

    public void setZugeordneteMeldungenEigeneAktienListeM(
            List<EclZugeordneteMeldungM> zugeordneteMeldungenEigeneAktienListeM) {
        this.zugeordneteMeldungenEigeneAktienListeM = zugeordneteMeldungenEigeneAktienListeM;
    }

    public int getAnzZugeordneteMeldungenEigeneAktienListeM() {
        return anzZugeordneteMeldungenEigeneAktienListeM;
    }

    public void setAnzZugeordneteMeldungenEigeneAktienListeM(int anzZugeordneteMeldungenEigeneAktienListeM) {
        this.anzZugeordneteMeldungenEigeneAktienListeM = anzZugeordneteMeldungenEigeneAktienListeM;
    }

    public List<EclZugeordneteMeldungM> getZugeordneteMeldungenEigeneGastkartenListeM() {
        return zugeordneteMeldungenEigeneGastkartenListeM;
    }

    public void setZugeordneteMeldungenEigeneGastkartenListeM(
            List<EclZugeordneteMeldungM> zugeordneteMeldungenEigeneGastkartenListeM) {
        this.zugeordneteMeldungenEigeneGastkartenListeM = zugeordneteMeldungenEigeneGastkartenListeM;
    }

    public int getAnzZugeordneteMeldungenEigeneGastkartenListeM() {
        return anzZugeordneteMeldungenEigeneGastkartenListeM;
    }

    public void setAnzZugeordneteMeldungenEigeneGastkartenListeM(int anzZugeordneteMeldungenEigeneGastkartenListeM) {
        this.anzZugeordneteMeldungenEigeneGastkartenListeM = anzZugeordneteMeldungenEigeneGastkartenListeM;
    }

    public List<EclZugeordneteMeldungM> getZugeordneteMeldungenBevollmaechtigtListeM() {
        return zugeordneteMeldungenBevollmaechtigtListeM;
    }

    public void setZugeordneteMeldungenBevollmaechtigtListeM(
            List<EclZugeordneteMeldungM> zugeordneteMeldungenBevollmaechtigtListeM) {
        this.zugeordneteMeldungenBevollmaechtigtListeM = zugeordneteMeldungenBevollmaechtigtListeM;
    }

    public int getAnzZugeordneteMeldungenBevollmaechtigtListeM() {
        return anzZugeordneteMeldungenBevollmaechtigtListeM;
    }

    public void setAnzZugeordneteMeldungenBevollmaechtigtListeM(int anzZugeordneteMeldungenBevollmaechtigtListeM) {
        this.anzZugeordneteMeldungenBevollmaechtigtListeM = anzZugeordneteMeldungenBevollmaechtigtListeM;
    }

    public boolean isBriefwahlVorhanden() {
        return briefwahlVorhanden;
    }

    public void setBriefwahlVorhanden(boolean briefwahlVorhanden) {
        this.briefwahlVorhanden = briefwahlVorhanden;
    }

    public boolean isSrvVorhanden() {
        return srvVorhanden;
    }

    public void setSrvVorhanden(boolean srvVorhanden) {
        this.srvVorhanden = srvVorhanden;
    }

    public boolean isPraesenteVorhanden() {
        return praesenteVorhanden;
    }

    public void setPraesenteVorhanden(boolean praesenteVorhanden) {
        this.praesenteVorhanden = praesenteVorhanden;
    }

    public boolean isNichtPraesenteVorhanden() {
        return nichtPraesenteVorhanden;
    }

    public void setNichtPraesenteVorhanden(boolean nichtPraesenteVorhanden) {
        this.nichtPraesenteVorhanden = nichtPraesenteVorhanden;
    }

}
