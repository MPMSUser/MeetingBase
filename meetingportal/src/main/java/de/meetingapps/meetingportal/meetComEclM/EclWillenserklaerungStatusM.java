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

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComEntities.EclWillenserklaerungStatus;
import de.meetingapps.meetingportal.meetComHVParam.HVParam;
import de.meetingapps.meetingportal.meetComKonst.KonstWillenserklaerung;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

@SessionScoped
@Named
@Deprecated
public class EclWillenserklaerungStatusM implements Serializable {
    private static final long serialVersionUID = 5213800206669936675L;

    private int logDrucken = 3;

    /*Hinweis: in dieser Bean dürfen keine Injects verwendet werden, da diese Bean auch als "normale Klasse" verwendet wird!*/

    private int willenserklaerungIdent = 0;

    /**Zweit-Willenserklärung-ID für "Kombi-Willenserklärung", aktuell nur für
     * Eintrittskarte und Vollmacht - in diesem Fall enthält willenserklaerungIdent2 die ident der
     * Vollmachts-Willenserklärung (wird für Storno benötigt!)
     */
    private int willenserklaerungIdent2 = 0;

    /**Art der Willenserklärung, die durch diesen Satz verkörpert wird*/
    private int willenserklaerung = 0;

    /**Für Eintrittskarten/Gastkarten*/
    private String zutrittsIdent = "";
    private String zutrittsIdentNeben = "";

    /**Ident der Person, die die Vollmacht gegeben hat*/
    private int willenserklaerungGeberIdent;

    /**Für Vollmachten - wird u.benötigt für spätere Stornierung - wird u.a. benötigt für spätere Stornierung.
     * (Früher: personenNatJurIdent)*/
    private int bevollmaechtigterDritterIdent; /*NEU. früher: personenNatJurIdent*/

    private List<String> textListe;
    private List<String> textListeEN; /*Englische Version der Textliste. Achtung, derzeit nicht mandantenabhängig übersetzbar!*/

    private boolean storniert = false;

    /**=1 => dedizierte vorhanden
     * =2 => gemäß Vorschlag vorhanden
     * =3 => nur freie
     */
    private int weisungenSind = 0;

    private boolean aendernIstZulaessig = false;
    private boolean stornierenIstZulaessig = false;

    /**Hier wird auch berücksichtigt, ob die jeweilige Funktion im Portal gerade aktiv ist*/
    private boolean aendernIstZulaessigPortal = false;
    private boolean stornierenIstZulaessigPortal = false;

    /**z.B. für Eintrittskarten, die auf den "Anzeiger" ausgestellt sind; diese dürfen von ihm zwar nicht
     * verändert / storniert werden, aber durchaus in der Detailanzeige erneut ausgedruckt werden
     */
    private boolean detailAnzeigeIstZulaessig = false; /*NEU*/

    /**Ein Satz mit istLeerDummy=true wird in die Willenserklärungsliste eingefügt, wenn keinerlei andere Willenserkläerungen zu
     * dieser Anmeldung gegeben wurden. 
     */
    private boolean istLeerDummy = false;

    public boolean willenserklaerungPortalAktiv(int pWillenserklaerungIdent, HVParam pHVParam) {
        CaBug.druckeLog("pWillenserklaerungIdent=" + pWillenserklaerungIdent,
                logDrucken, 10);
        switch (pWillenserklaerungIdent) {
        case KonstWillenserklaerung.neueZutrittsIdentZuMeldung: {
            if (pHVParam.paramPortal.ekSelbstMoeglich == 0 || pHVParam.paramPortal.lfdHVPortalEKIstMoeglich == 0) {
                return false;
            }
            break;
        }
        case KonstWillenserklaerung.neueZutrittsIdentZuMeldung_VollmachtAnDritte: {
            if (pHVParam.paramPortal.ekVollmachtMoeglich == 0 || pHVParam.paramPortal.lfdHVPortalEKIstMoeglich == 0) {
                return false;
            }
            break;
        }
        case KonstWillenserklaerung.vollmachtUndWeisungAnSRV: {
            if (pHVParam.paramPortal.srvAngeboten == 0 || pHVParam.paramPortal.lfdHVPortalSRVIstMoeglich == 0) {
                return false;
            }
            break;
        }
        case KonstWillenserklaerung.briefwahl: {
            if (pHVParam.paramPortal.briefwahlAngeboten == 0
                    || pHVParam.paramPortal.lfdHVPortalBriefwahlIstMoeglich == 0) {
                return false;
            }
            break;
        }
        case KonstWillenserklaerung.vollmachtAnDritte: {
            if (pHVParam.paramPortal.vollmachtDritteAngeboten == 0
                    || pHVParam.paramPortal.lfdHVPortalVollmachtDritteIstMoeglich == 0) {
                return false;
            }
            break;
        }
        }
        return true;
    }

    public void copyFrom(EclWillenserklaerungStatus pWillenserklaerungStatus, HVParam pHVParam) {
        /*Achtung: pHVParam müssen hier übergeben werden, da in "Folgefunktionen" benötigt, und diese Methode auch als POJO
         * verwendet wird und darin keine Injects verarbeitet werden dürfen!*/

        int i;
        this.willenserklaerungIdent = pWillenserklaerungStatus.willenserklaerungIdent;
        this.willenserklaerungIdent2 = pWillenserklaerungStatus.willenserklaerungIdent2;
        this.willenserklaerung = pWillenserklaerungStatus.willenserklaerung;
        this.zutrittsIdent = pWillenserklaerungStatus.zutrittsIdent;
        this.zutrittsIdentNeben = pWillenserklaerungStatus.zutrittsIdentNeben;
        this.willenserklaerungGeberIdent = pWillenserklaerungStatus.willenserklaerungGeberIdent;
        this.bevollmaechtigterDritterIdent = pWillenserklaerungStatus.bevollmaechtigterDritterIdent;
        this.textListe = new LinkedList<>();
        if (pWillenserklaerungStatus.textListe != null) {
            for (i = 0; i < pWillenserklaerungStatus.textListe.size(); i++) {
                this.textListe.add(pWillenserklaerungStatus.textListe.get(i));
            }
        }
        this.textListeEN = new LinkedList<>();
        if (pWillenserklaerungStatus.textListeEN != null) {
            for (i = 0; i < pWillenserklaerungStatus.textListeEN.size(); i++) {
                this.textListeEN.add(pWillenserklaerungStatus.textListeEN.get(i));
            }
        }
        this.storniert = pWillenserklaerungStatus.storniert;
        this.weisungenSind = pWillenserklaerungStatus.weisungenSind;
        this.aendernIstZulaessig = pWillenserklaerungStatus.aendernIstZulaessig;
        this.aendernIstZulaessigPortal = pWillenserklaerungStatus.aendernIstZulaessig
                && willenserklaerungPortalAktiv(willenserklaerung, pHVParam);
        this.stornierenIstZulaessig = pWillenserklaerungStatus.stornierenIstZulaessig;
        this.stornierenIstZulaessigPortal = pWillenserklaerungStatus.stornierenIstZulaessig
                & willenserklaerungPortalAktiv(willenserklaerung, pHVParam);

        /*TODO Achtung - früher wurde dbBundle stat eclParameterM verwendet. D.h. wenn hier "Fehlfunktion", dann sind wohl
         * Parameter nicht richtig eingelesen
         */
        if (this.willenserklaerung == KonstWillenserklaerung.vollmachtUndWeisungAnSRV
                && pHVParam.paramPortal.lfdHVPortalSRVIstMoeglich == 0) {
            this.aendernIstZulaessig = false;
            this.stornierenIstZulaessig = false;
        }
        if (this.willenserklaerung == KonstWillenserklaerung.vollmachtUndWeisungAnKIAV
                && pHVParam.paramPortal.lfdHVPortalKIAVIstMoeglich == 0) {
            this.aendernIstZulaessig = false;
            this.stornierenIstZulaessig = false;
        }
        if (this.willenserklaerung == KonstWillenserklaerung.briefwahl
                && pHVParam.paramPortal.lfdHVPortalBriefwahlIstMoeglich == 0) {
            this.aendernIstZulaessig = false;
            this.stornierenIstZulaessig = false;
        }

        if ((this.willenserklaerung == KonstWillenserklaerung.neueZutrittsIdentZuMeldung
                || this.willenserklaerung == KonstWillenserklaerung.neueZutrittsIdentZuMeldung_VollmachtAnDritte)
                && pHVParam.paramPortal.lfdHVPortalEKIstMoeglich == 0) {
            this.aendernIstZulaessig = false;
            this.stornierenIstZulaessig = false;
        }

        this.detailAnzeigeIstZulaessig = pWillenserklaerungStatus.detailAnzeigeIstZulaessig;
        this.istLeerDummy = pWillenserklaerungStatus.istLeerDummy;
    }

    public void copyFromM(EclWillenserklaerungStatusM pWillenserklaerungStatus) {
        int i;
        this.willenserklaerungIdent = pWillenserklaerungStatus.willenserklaerungIdent;
        this.willenserklaerungIdent2 = pWillenserklaerungStatus.willenserklaerungIdent2;
        this.willenserklaerung = pWillenserklaerungStatus.willenserklaerung;
        this.zutrittsIdent = pWillenserklaerungStatus.zutrittsIdent;
        this.zutrittsIdentNeben = pWillenserklaerungStatus.zutrittsIdentNeben;
        this.willenserklaerungGeberIdent = pWillenserklaerungStatus.willenserklaerungGeberIdent;
        this.bevollmaechtigterDritterIdent = pWillenserklaerungStatus.bevollmaechtigterDritterIdent;
        this.textListe = new LinkedList<>();
        if (pWillenserklaerungStatus.textListe != null) {
            for (i = 0; i < pWillenserklaerungStatus.textListe.size(); i++) {
                this.textListe.add(pWillenserklaerungStatus.textListe.get(i));
            }
        }
        this.textListeEN = new LinkedList<>();
        if (pWillenserklaerungStatus.textListeEN != null) {
            for (i = 0; i < pWillenserklaerungStatus.textListeEN.size(); i++) {
                this.textListeEN.add(pWillenserklaerungStatus.textListeEN.get(i));
            }
        }
        this.storniert = pWillenserklaerungStatus.storniert;
        this.weisungenSind = pWillenserklaerungStatus.weisungenSind;
        this.aendernIstZulaessig = pWillenserklaerungStatus.aendernIstZulaessig;
        this.stornierenIstZulaessig = pWillenserklaerungStatus.stornierenIstZulaessig;
        this.detailAnzeigeIstZulaessig = pWillenserklaerungStatus.detailAnzeigeIstZulaessig;
        this.istLeerDummy = pWillenserklaerungStatus.istLeerDummy;
    }

    /***Ab hier Getters und Setters****/

    public int getWillenserklaerungIdent() {
        return willenserklaerungIdent;
    }

    public void setWillenserklaerungIdent(int willenserklaerungIdent) {
        this.willenserklaerungIdent = willenserklaerungIdent;
    }

    public int getWillenserklaerung() {
        return willenserklaerung;
    }

    public void setWillenserklaerung(int willenserklaerung) {
        this.willenserklaerung = willenserklaerung;
    }

    public boolean isStorniert() {
        return storniert;
    }

    public void setStorniert(boolean storniert) {
        this.storniert = storniert;
    }

    public List<String> getTextListe() {
        return textListe;
    }

    public void setTextListe(List<String> textListe) {
        this.textListe = textListe;
    }

    public int getWeisungenSind() {
        return weisungenSind;
    }

    public void setWeisungenSind(int weisungenSind) {
        this.weisungenSind = weisungenSind;
    }

    public boolean isAendernIstZulaessig() {
        return aendernIstZulaessig;
    }

    public void setAendernIstZulaessig(boolean aendernIstZulaessig) {
        this.aendernIstZulaessig = aendernIstZulaessig;
    }

    public boolean isStornierenIstZulaessig() {
        return stornierenIstZulaessig;
    }

    public void setStornierenIstZulaessig(boolean stornierenIstZulaessig) {
        this.stornierenIstZulaessig = stornierenIstZulaessig;
    }

    public String getZutrittsIdent() {
        return zutrittsIdent;
    }

    public void setZutrittsIdent(String zutrittsIdent) {
        this.zutrittsIdent = zutrittsIdent;
    }

    public int getWillenserklaerungIdent2() {
        return willenserklaerungIdent2;
    }

    public void setWillenserklaerungIdent2(int willenserklaerungIdent2) {
        this.willenserklaerungIdent2 = willenserklaerungIdent2;
    }

    public int getWillenserklaerungGeberIdent() {
        return willenserklaerungGeberIdent;
    }

    public void setWillenserklaerungGeberIdent(int willenserklaerungGeberIdent) {
        this.willenserklaerungGeberIdent = willenserklaerungGeberIdent;
    }

    public int getBevollmaechtigterDritterIdent() {
        return bevollmaechtigterDritterIdent;
    }

    public void setBevollmaechtigterDritterIdent(int bevollmaechtigterDritterIdent) {
        this.bevollmaechtigterDritterIdent = bevollmaechtigterDritterIdent;
    }

    public boolean isDetailAnzeigeIstZulaessig() {
        return detailAnzeigeIstZulaessig;
    }

    public void setDetailAnzeigeIstZulaessig(boolean detailAnzeigeIstZulaessig) {
        this.detailAnzeigeIstZulaessig = detailAnzeigeIstZulaessig;
    }

    public List<String> getTextListeEN() {
        return textListeEN;
    }

    public void setTextListeEN(List<String> textListeEN) {
        this.textListeEN = textListeEN;
    }

    public boolean isIstLeerDummy() {
        return istLeerDummy;
    }

    public void setIstLeerDummy(boolean istLeerDummy) {
        this.istLeerDummy = istLeerDummy;
    }

    public String getZutrittsIdentNeben() {
        return zutrittsIdentNeben;
    }

    public void setZutrittsIdentNeben(String zutrittsIdentNeben) {
        this.zutrittsIdentNeben = zutrittsIdentNeben;
    }

    public boolean isAendernIstZulaessigPortal() {
        return aendernIstZulaessigPortal;
    }

    public void setAendernIstZulaessigPortal(boolean aendernIstZulaessigPortal) {
        this.aendernIstZulaessigPortal = aendernIstZulaessigPortal;
    }

    public boolean isStornierenIstZulaessigPortal() {
        return stornierenIstZulaessigPortal;
    }

    public void setStornierenIstZulaessigPortal(boolean stornierenIstZulaessigPortal) {
        this.stornierenIstZulaessigPortal = stornierenIstZulaessigPortal;
    }

}
